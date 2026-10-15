package com.tiket.web.dashboard.page.platform.eiffel.lounge;

import com.tiket.api.auth.AdminDashboardLoginResponseParser.ParsedCookie;
import com.tiket.apiPage.LoungeAuthApi;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LoungeLoginPageWeb<T extends WebDriver> extends WebPage<T> {
    public LoungeLoginPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> LoungeLoginPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new LoungeLoginPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new LoungeLoginPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", By.id("sb-input-username"));
    private static final Locator PASSWORD_INPUT = new Locator("PASSWORD_INPUT", By.id("sb-input-password"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", By.cssSelector("[data-testid='btn-login']"));
    private static final Locator OTP_INPUT = new Locator("OTP_INPUT", By.cssSelector("[data-testid='otp-input']"));
    private static final Locator OTP_HEADING = new Locator("OTP_HEADING", By.xpath("//h2[text()='Verify Your Email']"));

    private static final Locator PROFILE_DROPDOWN = new Locator("PROFILE_DROPDOWN", By.cssSelector("[class*='UserProfileDropdown_dropdown_trigger']"));
    private static final Locator LOGOUT_OPTION = new Locator("LOGOUT_OPTION", By.cssSelector("a[href*='/external/logout']"));
    private static final Locator SIDEBAR_NAV = new Locator("SIDEBAR_NAV", By.cssSelector("nav[class*='Sidebar_sidebar_nav']"));

    private static final String DEFAULT_OTP = "1234";

    /**
     * Session-cookie cache shared across the Lounge suite, keyed by email + password hash so that a login with a
     * different password (e.g. the change/set-password flows) is always a cache miss that re-validates the new
     * credentials against the real login API instead of silently reusing a stale session.
     */
    private static final ConcurrentHashMap<String, LoungeAuthApi.LoungeSession> SESSION_CACHE = new ConcurrentHashMap<>();

    private String resolveBaseUrl() {
        return switch (Configs.ENVIRONMENT) {
            case GK -> "https://reward-gatotkaca.tiket.com/";
            case PREPROD -> "https://reward.preprod.tiket.com/";
            case PROD -> "https://reward.tiket.com/";
            default -> throw new IllegalStateException("Unsupported environment: " + Configs.ENVIRONMENT);
        };
    }

    public void goToPage() {
        String url = resolveBaseUrl();

        try {
            driver.manage().deleteAllCookies();
            driverTools.get(url, 2);
        } catch (Exception e) {
            if (e instanceof org.openqa.selenium.TimeoutException || e instanceof java.util.concurrent.TimeoutException || 
                (e.getCause() != null && e.getCause() instanceof java.util.concurrent.TimeoutException)) {
                throw new AssertionError(
                        "Lounge page failed to load: " + url + " is unreachable or not responding. " +
                        "Verify the environment is up and accessible from the test runner. " +
                        "Original error: " + e.getMessage(), e);
            }
            throw new AssertionError(
                    "Failed to navigate to Lounge page (" + url + "): " + e.getMessage(), e);
        }
        driverTools.waitForWebToLoad();
    }

    public void login(String username, String password) {
        login(username, password, DEFAULT_OTP);
    }

    /**
     * Fast, self-contained login that authenticates via the member-core admin login API and injects the resulting
     * session cookies, skipping the UI form and the OTP challenge. It is safe to call from any browser state:
     * <ol>
     *     <li><b>Reuse</b> — if the browser is already authenticated as {@code username} (per the {@code aat} cookie),
     *     it returns immediately without re-logging in, so consecutive tests on the same account are near-instant.</li>
     *     <li><b>Switch</b> — otherwise it injects that user's cookies (from the per-email+password cache, or fetched
     *     once via {@link LoungeAuthApi}), which transparently switches away from any other logged-in account. This is
     *     why tests no longer need to {@link #logout()} at the end.</li>
     *     <li><b>Fallback</b> — any failure (API rejection, expired/invalid cookies) falls back to the standard UI
     *     {@link #login(String, String)} so behaviour is preserved.</li>
     * </ol>
     */
    public void loginViaCookie(String username, String password) {
        if (username != null && username.equalsIgnoreCase(loggedInEmail())) {
            logger.log("Already logged in to Lounge as " + username + " — reusing existing session");
            driverTools.refreshWebpage();
            driverTools.waitForWebToLoad();
            return;
        }

        String key = cacheKey(username, password);
        LoungeAuthApi.LoungeSession cached = SESSION_CACHE.get(key);
        if (cached != null && !cached.isExpired() && applySession(cached)) {
            logger.log("Reused cached Lounge session for " + username + " — skipped UI login");
            return;
        }
        SESSION_CACHE.remove(key);

        try {
            LoungeAuthApi.LoungeSession fetched = new LoungeAuthApi<>(this.session).login(username, password);
            if (fetched.isUsable() && applySession(fetched)) {
                SESSION_CACHE.put(key, fetched);
                logger.log("Logged in to Lounge via API cookie injection for " + username);
                return;
            }
            logger.softWarning("Cookie injection did not authenticate " + username + " — falling back to UI login");
        } catch (Exception e) {
            logger.softWarning("API cookie login failed for " + username + " (" + e.getMessage()
                    + ") — falling back to UI login");
        }

        goToPage();
        login(username, password);
    }

    /**
     * Returns the email of the account currently authenticated in the browser, read from the {@code email} claim of
     * the {@code aat} session cookie (a standard JWT), or {@code null} when logged out or the token is missing/expired.
     * This is the same identity shown in the header profile dropdown ({@code UserProfileDropdown_dropdown_trigger});
     * decoding the cookie avoids depending on the header being rendered and works regardless of the current page.
     */
    private String loggedInEmail() {
        try {
            Cookie aat = driver.manage().getCookieNamed("aat");
            if (aat == null) {
                return null;
            }
            String[] parts = aat.getValue().split("\\.");
            if (parts.length < 2) {
                return null;
            }
            String payloadJson = new String(Base64.getUrlDecoder().decode(padBase64(parts[1])), StandardCharsets.UTF_8);
            JSONObject payload = new JSONObject(payloadJson);
            long expSeconds = payload.optLong("exp", 0L);
            if (expSeconds > 0 && System.currentTimeMillis() / 1000L >= expSeconds) {
                return null;
            }
            String email = payload.optString("email", "");
            return email.isBlank() ? null : email;
        } catch (Exception e) {
            return null;
        }
    }

    private static String padBase64(String value) {
        int remainder = value.length() % 4;
        return remainder == 0 ? value : value + "====".substring(remainder);
    }

    private boolean applySession(LoungeAuthApi.LoungeSession session) {
        try {
            ensureOnLoungeDomain();
            driver.manage().deleteAllCookies();
            long now = System.currentTimeMillis();
            int injected = 0;
            for (ParsedCookie cookie : session.cookies()) {
                if (addSessionCookie(cookie, now)) {
                    injected++;
                }
            }
            logger.log("Injected " + injected + " Lounge session cookie(s) for cookie login");

            driverTools.get(resolveBaseUrl());
            driverTools.waitForWebToLoad();
            if (isSidebarVisibleAndLoggedIn(15)) {
                return true;
            }
            driver.navigate().refresh();
            driverTools.waitForWebToLoad();
            return isSidebarVisibleAndLoggedIn(15);
        } catch (Exception e) {
            logger.softWarning("Failed to apply Lounge session cookies: " + e.getMessage());
            return false;
        }
    }

    private boolean isSidebarVisibleAndLoggedIn(int timeoutSeconds) {
        return driverTools.waitForElementToAppear(SIDEBAR_NAV, timeoutSeconds).isPresent() && isUserLoggedIn();
    }

    private void ensureOnLoungeDomain() {
        String current;
        try {
            current = driver.getCurrentUrl();
        } catch (Exception e) {
            current = null;
        }
        if (current == null || !current.contains(".tiket.com")) {
            driverTools.get(resolveBaseUrl());
            driverTools.waitForWebToLoad();
        }
    }

    /**
     * Replays a single {@code Set-Cookie} from the login API into the browser on the shared {@code .tiket.com}
     * domain (falling back to a host-only cookie for stricter WebDriver builds). httpOnly is intentionally not
     * forced so the SPA can read any token it expects to be script-visible.
     */
    private boolean addSessionCookie(ParsedCookie cookie, long now) {
        if (cookie == null || cookie.value() == null || cookie.value().isBlank()) {
            return false;
        }
        Date expiry = cookie.maxAgeMillis() > 0 ? new Date(now + cookie.maxAgeMillis()) : null;
        try {
            driver.manage().addCookie(new Cookie.Builder(cookie.name(), cookie.value())
                    .domain(".tiket.com")
                    .path("/")
                    .expiresOn(expiry)
                    .build());
            return true;
        } catch (Exception e) {
            try {
                driver.manage().addCookie(new Cookie.Builder(cookie.name(), cookie.value()).path("/").build());
                return true;
            } catch (Exception ignored) {
                return false;
            }
        }
    }

    private static String cacheKey(String email, String password) {
        return (email == null ? "" : email.toLowerCase()) + ":" + sha256(password);
    }

    private static String sha256(String value) {
        String input = value == null ? "" : value;
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                sb.append(Character.forDigit((b >> 4) & 0xF, 16)).append(Character.forDigit(b & 0xF, 16));
            }
            return sb.toString();
        } catch (Exception e) {
            return Integer.toHexString(input.hashCode());
        }
    }

    public void login(String username, String password, String otp) {
        if (!isUserLoggedIn()) {
            driverTools.waitForElementToAppear(EMAIL_INPUT, 10)
                    .orElseThrow(() -> new AssertionError("Login page did not load — email input not found"));
            driverTools.sendKeys(EMAIL_INPUT, username);
            driverTools.sendKeys(PASSWORD_INPUT, password);
            driverTools.click(LOGIN_BUTTON);

            handleOtpIfVisible(otp);
        }
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToAppear(SIDEBAR_NAV, 15);
    }

    private void handleOtpIfVisible(String otp) {
        if (driverTools.waitForElementToAppear(OTP_HEADING, 5).isPresent()) {
            List<WebElement> otpInputs = driver.findElements(OTP_INPUT.by());
            for (int i = 0; i < otpInputs.size() && i < otp.length(); i++) {
                otpInputs.get(i).sendKeys(String.valueOf(otp.charAt(i)));
            }
            driverTools.dummyWait(3);
        }
    }

    public boolean isUserLoggedIn() {
        driverTools.dummyWait(2);
        String currentUrl = driverTools.getCurrentURL();
        return !currentUrl.contains("/lounge/login") && !currentUrl.contains("/external/login");
    }

    /**
     * Idempotent logout. Some flows (e.g. changing your own password) invalidate the session and bounce to
     * the centralized login page where the profile dropdown no longer exists; if the trigger isn't present
     * we're already logged out, so there's nothing to do.
     */
    public void logout() {
        driverTools.dummyWait(3);
        if (driverTools.waitForElementToAppear(PROFILE_DROPDOWN, 5).isEmpty()) {
            logger.log("Profile dropdown not present — assuming already logged out, skipping logout");
            return;
        }
        driverTools.click(PROFILE_DROPDOWN);

        driverTools.waitForElementToAppear(LOGOUT_OPTION, 5);
        driverTools.click(LOGOUT_OPTION);
    }
}
