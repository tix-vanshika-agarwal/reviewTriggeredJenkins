package com.tiket.web.page.common.home;

import com.tiket.apiPage.Unm;
import com.tiket.appium.PopupObserver;
import com.tiket.model.Deeplink;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.common.LoginB2CInDWEB;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import javax.security.auth.login.FailedLoginException;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.tiket.core.CoreFunctions.randomInt;
import static org.hamcrest.Matchers.is;

public class
LoginPageDWeb <T extends WebDriver> extends LoginPageWeb<T> {

    private static final Locator LOGIN_PROFILE_ICON = new Locator("LOGIN_PROFILE_ICON", new By.ByXPath("//div[contains(@class,'blink_icon')]"));
    private static final Locator LOGOUT_DIV_LINK = new Locator("LOGOUT_DIV_LINK", new By.ByXPath("//a[text() = 'Log Out' or text()='Keluar']"));
    private static final Locator LOGOUT_BUTTON = new Locator("LOGOUT_BUTTON", new By.ByXPath("//button[.//span[normalize-space(text())='Log out' or normalize-space(text())='Keluar']]"));
    private static final Locator RELOAD_BUTTON = new Locator("RELOAD_BUTTON", new By.ByXPath("//button[contains(@class, 'ErrorView_btn')]"));
    private static final Locator ACCOUNT_HOLDER_NAME = new Locator("ACCOUNT_HOLDER_NAME", new By.ByXPath("//div[contains(@class,'Profile_list')]//span[contains(@class,'Text')][3]"));
    private static final String STAGING_ACCOUNT_DETAILS_PAGE = "https://gatotkaca.tiket.com/myaccount?utm_section=navigationBar;edit_profile&utm_logic=none";
    private static final String PROD_ACCOUNT_DETAILS_PAGE = "https://www.tiket.com/id-sg/myaccount?utm_section=navigationBar%3Bedit_profile&utm_logic=none";
    private static final String PREPROD_ACCOUNT_DETAILS_PAGE = "https://preprod.tiket.com/myaccount?utm_section=navigationBar;edit_profile&utm_logic=none";
    private static final Locator CREATE_ACCOUNT_BUTTON = new Locator("CREATE_ACCOUNT_BUTTON", new By.ByXPath("//span[contains(@class, 'LoginLayout_redirection_link') and (contains(text(),'Register now!') or (contains(text(),'Daftar, yuk!')]"));
    private static final Locator REGISTER_BUTTON_LOGIN_PAGE = new Locator("REGISTER_BUTTON_LOGIN_PAGE", new By.ByXPath("//button[@type='submit' and @data-testid='btnSubmitForm' and (contains(text(),'Register') or contains(text(),'Daftar'))]"));
    private static Locator FORGOT_PASSWORD = new Locator("FORGOT_PASSWORD", AppiumBy.xpath("//span[@role='button' and contains(@class, 'Text')and contains(text(), 'Forgot Password') or (contains(text(),'Lupa Kata Sandi?')]"));
    private static Locator FORGOT_PASSWORD_NEXT = new Locator("FORGOT_PASSWORD_NEXT", AppiumBy.xpath("//button[contains(@class, 'Button_button') and contains(text(),'Next') or (contains(text(),'Lajutkan')]"));
    private static Locator FORGOT_PASSWORD_INPUT = new Locator("FORGOT_PASSWORD_INPUT", AppiumBy.xpath("(//input[@data-testid='txtPassword'])[2]"));
    private static Locator PASSWORD_VIEW_BUTTON = new Locator("PASSWORD_VIEW_BUTTON", AppiumBy.xpath("//button[contains(@class,'styles_clickable_icon')]"));
    private static Locator FORGOT_PASSWORD_CONFIRM = new Locator("FORGOT_PASSWORD_CONFIRM", AppiumBy.xpath("(//button[contains(@class, 'Button_button') and contains(@class, 'Button_variant_primary')])[2]"));
    private static Locator FORGOT_PASSWORD_TITLE = new Locator("FORGOT_PASSWORD_TITLE", AppiumBy.xpath("//h2[contains(@class,'Text_text') and contains(text(),'Forgot Password?') or (contains(text(),'Lupa Kata Sandi?')]"));

    /** Cookie-injection logins to try before falling back to the UI login; each retry authenticates from scratch. */
    private static final int SESSION_INJECTION_ATTEMPTS = 2;

    /** OTP accepted by the non-production environments, used by the UI-login fallback. */
    private static final String DEFAULT_PREPROD_OTP = "123456";
    /** Cloudflare bot-management cookie; bound to the client that received it, so never copied into the browser. */
    private static final String CLOUDFLARE_BOT_COOKIE = "__cf_bm";
    /** How long the SSO bounce may take to land back on a tiket page. */
    private static final int SSO_BOUNCE_SECONDS = 15;

    /**
     * How long an injected session must survive before it is trusted. The app's own session bootstrap
     * writes its cookie asynchronously after the page renders, so a shorter window declares success
     * before the session can be taken away.
     */
    private static final int SESSION_SETTLE_SECONDS = 3;

    /** How long a logout waits for the member token to go away. */
    private static final int LOGOUT_CONFIRM_SECONDS = 3;

    /**
     * How long a logout looks for a lingering profile icon once the member token is already gone. The token is the
     * primary signal, so this is only a short cross-check rather than a full wait on every successful logout.
     */
    private static final int PROFILE_ICON_RECHECK_SECONDS = 1;

    /** Cookie domain that covers every tiket web host used by the automation environments. */
    private static final String TIKET_COOKIE_DOMAIN = ".tiket.com";

    /** Logout paths named in the final "Logout confirmed" log line. */
    private static final String LOGOUT_PATH_ACCOUNT_MENU = "account menu";
    private static final String LOGOUT_PATH_URL = "/logout URL";

    private static final String UNM_URL = "flashsale-dev.gdn-app.com";
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", new By.ByXPath("//button[contains(@class, 'InitialForm_cta_button')]"));

    public LoginPageDWeb(Session<T> session) {
        super(session);
    }

    public boolean checkIfLoggedIn(){
        driverTools.waitForElementToAppear(LOGIN_PROFILE_ICON,5);

        return driverTools.isElementDisplayed(LOGIN_PROFILE_ICON);
    }

    /**
     * Logs out through the account menu (profile icon → Log Out → confirm) from the current page.
     * Falls back to the {@code /logout} URL when the menu is unavailable or the session survives the
     * click sequence, and fails loudly if the user is still logged in after every attempt.
     */
    @Override
    public void logoutFromDweb() {
        logger.log("[Logout] Logging out via account menu");
        completeLogout(logoutThroughAccountMenu() ? LOGOUT_PATH_ACCOUNT_MENU
                : logoutThroughUrl() ? LOGOUT_PATH_URL : null);
    }

    /**
     * Clicks through the account menu logout, then reloads the page so the confirmation proves the
     * logout survives a page load instead of being silently restored by the SSO bootstrap.
     *
     * @return {@code true} when the member session is confirmed gone.
     */
    private boolean logoutThroughAccountMenu() {
        if (!clickAccountMenuLogout()) {
            return false;
        }
        dismissErrorViewIfShown();
        driverTools.refreshWebpage();
        return isLoggedOutConfirmed();
    }

    /**
     * Clicks profile icon → Log Out → confirm, and waits for the confirmation dialog to close.
     *
     * @return {@code false} when any step of the account menu was unavailable or interrupted.
     */
    private boolean clickAccountMenuLogout() {
        try {
            Optional<WebElement> profileIcon = driverTools.waitForElementToAppear(LOGIN_PROFILE_ICON, LOGOUT_CONFIRM_SECONDS);
            if (profileIcon.isEmpty()) {
                logger.log("[Logout] Profile icon not shown on the current page; account menu logout unavailable");
                return false;
            }
            driverTools.click(profileIcon.get());

            Optional<WebElement> logoutLink = driverTools.waitForElementToAppear(LOGOUT_DIV_LINK);
            if (logoutLink.isEmpty()) {
                logger.log("[Logout] Log Out link not shown in the account menu");
                return false;
            }
            driverTools.scrollIntoWebView(logoutLink.get());
            driverTools.clickWithJavascriptExecutor(logoutLink.get());

            Optional<WebElement> logoutButton = driverTools.waitForElementToAppear(LOGOUT_BUTTON);
            if (logoutButton.isEmpty()) {
                logger.log("[Logout] Logout confirmation button not shown");
                return false;
            }
            driverTools.clickWithJavascriptExecutor(logoutButton.get());
            driverTools.waitForElementToDisappear(LOGOUT_BUTTON, 10);
            return true;
        } catch (WebDriverException e) {
            logger.log("[Logout] Account menu logout interrupted: " + e.getMessage());
            return false;
        }
    }

    public String accountHolderName(){
        WebElement accountHolderName = driverTools.waitForElementToAppear(ACCOUNT_HOLDER_NAME).orElseThrow();
        return accountHolderName.getText();
    }

    public boolean isAccountHolderNameDisplayed(){
        driverTools.waitForElementToAppear(ACCOUNT_HOLDER_NAME,2);
        return driverTools.isElementDisplayed(ACCOUNT_HOLDER_NAME);
    }

    public void goToAccountPage() {
        switch (Configs.ENVIRONMENT) {
            case GK:
                driverTools.get(STAGING_ACCOUNT_DETAILS_PAGE);
                break;
            case PREPROD:
                driverTools.get(PREPROD_ACCOUNT_DETAILS_PAGE);
                break;
            case PROD:
                driverTools.get(PROD_ACCOUNT_DETAILS_PAGE);
                break;
            default: throw new NotImplementedException("Unimplemented ENV type: " + Configs.ENVIRONMENT.name());
        }
        PopupObserver.inject(driver, List.of(ACCOUNT_POP_UP));
    }

    public void loginIfNotWithSameEmail(String email ,String password,String otp , int retryCounter) throws FailedLoginException, InterruptedException {
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        goToAccountPage();
        if(!isAccountHolderNameDisplayed()){
            homePage.goToHomePage();
            this.loginIntoWeb(email,password,otp,retryCounter);
        }
        else{
            String accountHolderName = accountHolderName();
            if(accountHolderName.equals(email)){
                homePage.goToHomePage();
            }
            else{
                homePage.goToHomePage();
                logoutFromDweb();
                this.loginIntoWeb(email,password,otp,retryCounter);
            }
        }

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.waitForElementToAppear(RELOAD_BUTTON, 5);
            int retryCount = 0;
            int maxRetries = 5;
            while (driverTools.isElementDisplayed(RELOAD_BUTTON) && retryCount < maxRetries) {
                driver.navigate().refresh();
                retryCount++;
            }
        }
    }

    @Override
    public void continueIfAlreadyLoggedIn(String email, String password, String otp, int i) throws FailedLoginException, InterruptedException {
        loginIfNotWithSameEmail(email, password, otp, i);
    }

    public void skipUNMPageAfterLogin(String timeOutSec) {
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        long startTime = Instant.now().toEpochMilli();
        logger.log("Start time: "+startTime);
        long waitTime = startTime + (Integer.parseInt(timeOutSec) * 1000);
        logger.log("Wait time: "+waitTime);

        while (Instant.now().toEpochMilli() < waitTime){
            if (driver.getCurrentUrl().contains(UNM_URL)) {
                logger.log("Got UNM page: "+driver.getCurrentUrl());
                homePage.goToHomePage();
                homePage.waitForHomePageToOpen();
                break;
            }
        }
    }

    /**
     * Specific-account login via cookie injection. If a <em>different</em> user is logged in,
     * logs out via {@code /en-sg/logout} URL first, then injects fresh tokens.
     *
     * <p><b>PROD guardrail:</b> falls back to UI login on production since UNM API is unavailable.
     */
    @Override
    public void loginWithSpecificAccountViaCookie(String email, String password) throws FailedLoginException, InterruptedException {
        if (Configs.ENVIRONMENT == Environment.PROD) {
            logger.log("[PROD] Cookie injection unavailable, falling back to UI login for: " + email);
            super.loginWithSpecificAccountViaCookie(email, password);
            return;
        }
        loginWithOneRetry(email, () -> loginViaCookieInjection(email, password, true));
    }

    /**
     * Drops the cached UNM tokens and the cookies on both the account (bliblitiket) and tiket domains, so the
     * retry starts from a clean guest browser instead of the session the failed attempt left behind.
     */
    @Override
    protected void resetSessionBeforeLoginRetry(String email) {
        new Unm<>(session).invalidateCachedTokens(email);
        clearBliblitiketCookies();
        driverTools.get(getTiketWebBaseUrl());
        driverTools.deleteAllCookies();
    }

    /**
     * Shared cookie-injection login logic for dWeb.
     *
     * <ol>
     *   <li>Navigates to the home page for the current environment.</li>
     *   <li>If a valid session cookie already exists for {@code email} and the UI confirms
     *       the user is logged in, returns immediately (session reuse).</li>
     *   <li>If a <em>different</em> user is logged in and {@code logoutDifferentUser} is
     *       {@code true}, logs that user out via URL first.</li>
     *   <li>Fetches a fresh access token from the UNM API and injects it as a cookie.</li>
     *   <li>Self-heals: if an injected session does not survive (revoked token, or the app
     *       replacing it with a guest session), invalidates the cache and retries with a
     *       guaranteed-fresh token.</li>
     * </ol>
     */
    private void loginViaCookieInjection(String email, String password, boolean logoutDifferentUser) throws FailedLoginException, InterruptedException {
        logger.log("Session login (dWeb) for: " + email + " [logoutOther=" + logoutDifferentUser + "]");

        driverTools.maximizeWindow();
        String homeUrl = switch (Configs.ENVIRONMENT) {
            case GK -> "https://gatotkaca.tiket.com/";
            case PREPROD -> "https://preprod.tiket.com/";
            case PROD -> "https://www.tiket.com/";
        };
        driverTools.get(homeUrl, 2);

        String existingToken = driverTools.getCookieValue("session_access_token");
        String emailCookieValue = driverTools.getCookieValue("_auto_session_email");

        // A guest session_access_token is indistinguishable from a member one by name and expiry, so a
        // guest token here would otherwise be treated as "someone else is logged in" and drive a
        // pointless /logout round trip. Drop it and go straight to injection.
        if (existingToken != null && !Unm.isMemberToken(existingToken)) {
            logger.log("[Cookie Login] Existing session_access_token is a guest token (no member claims); discarding it");
            clearSessionCookiesWithoutChangingPage();
            existingToken = null;
        }

        if (existingToken != null && !Unm.isTokenExpired(existingToken)) {
            // The token's own email claim is authoritative; the _auto_session_email marker is only a
            // fallback for sessions created before the marker existed.
            String sessionEmail = Unm.getEmailClaim(existingToken);
            if (sessionEmail == null) {
                sessionEmail = emailCookieValue;
            }
            if (email.equalsIgnoreCase(sessionEmail)) {
                logger.log("[Cookie Login] Valid session for " + email + ", verifying UI state");
                driver.navigate().refresh();
                HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
                homePage.waitForHomePageToOpen();
                if (isMemberSessionSettled(email)) {
                    logger.log("[Cookie Login] Confirmed logged in as " + email);
                    return;
                }
                logger.log("[Cookie Login] Cookie matches but session did not hold, re-injecting");
            } else {
                if (logoutDifferentUser) {
                    // Only hit /logout when the UI actually shows a logged-in session. If only stale
                    // token cookies remain (e.g. server-invalidated, cleared tracking cookie, fresh
                    // browser profile), navigating to /logout is wasted; drop the stale cookies and
                    // let fresh-token injection proceed.
                    if (checkIfLoggedIn()) {
                        logger.log("[Cookie Login] Logged in as " + sessionEmail + ", logging out before switching to " + email);
                        logoutViaUrl();
                    } else {
                        logger.log("[Cookie Login] Stale token cookie for "
                                + (sessionEmail != null ? sessionEmail : "<unknown user>")
                                + " but UI shows logged out; skipping /logout navigation");
                        clearSessionCookiesWithoutChangingPage();
                    }
                } else {
                    logger.log("[Cookie Login] Session belongs to " + sessionEmail + ", need to re-login as " + email);
                }
            }
        }

        Unm<T> unm = new Unm<>(session);
        for (int attempt = 1; attempt <= SESSION_INJECTION_ATTEMPTS; attempt++) {
            if (attempt > 1) {
                // The previous attempt's token may have been revoked server-side while its JWT exp is
                // still in the future, so force a brand-new authentication rather than reuse the cache,
                // and drop every cookie first so the app re-bootstraps instead of resuming the guest
                // session it just decided on.
                logger.log("[Cookie Login] Attempt " + (attempt - 1) + " did not hold. Clearing cookies and retrying with a fresh token.");
                unm.invalidateCachedTokens(email);
                driverTools.deleteAllCookies();
            }
            if (injectFreshSessionAndVerify(email, password, unm)) {
                return;
            }
        }

        // Cookie injection depends on the app accepting a session it did not create, and it sometimes
        // refuses and installs a guest session instead. Falling back to the UI login keeps a whole
        // suite from collapsing on a login problem: it is slower, but it is the flow the app owns.
        logger.softWarning("[Cookie Login] Cookie injection did not hold after " + SESSION_INJECTION_ATTEMPTS
                + " attempts for " + email + "; falling back to UI login");
        driverTools.deleteAllCookies();
        unm.invalidateCachedTokens(email);
        if (!loginViaSsoBounce(email)) {
            loginIntoWebViaLoginUrl(email, password, DEFAULT_PREPROD_OTP, 1);
        }
        Assertion.assertThat("Logged in as " + email + " after cookie injection fell back to UI login",
                checkIfLoggedIn(), is(true));
    }

    /**
     * Opens the tiket login URL while the account-domain (bliblitiket) session from the API login is still
     * in the browser. The account site recognises that session and redirects straight back to tiket, which
     * then mints its own member session, so no tiket cookie is forged.
     *
     * <p>If the browser lands on the login form instead, or comes back without a member session for
     * {@code email}, the account-domain cookies are cleared so the caller's UI login sees a real login form
     * rather than being bounced to the home page again.
     *
     * @return true when the bounce produced a settled member session for {@code email}
     */
    private boolean loginViaSsoBounce(String email) {
        logger.log("[Cookie Login] Trying SSO bounce through " + getTiketWebBaseUrl() + "login for " + email);
        driverTools.getWithDynamicTimeout(getTiketWebBaseUrl() + "login", 60);
        boolean bouncedBack = driverTools.waitUntil(d -> isTiketPageOtherThanLogin(d.getCurrentUrl()) ? Boolean.TRUE : null,
                SSO_BOUNCE_SECONDS).isPresent();
        if (bouncedBack) {
            HomePageWeb.getInstance(session).waitForHomePageToOpen();
            if (isMemberSessionSettled(email)) {
                logger.log("[Cookie Login] SSO bounce logged in as " + email);
                return true;
            }
            logger.log("[Cookie Login] SSO bounce returned to " + driverTools.getCurrentURL() + " without a member session for " + email);
        } else {
            logger.log("[Cookie Login] No SSO bounce; still on " + driverTools.getCurrentURL());
        }
        clearBliblitiketCookies();
        driverTools.deleteAllCookies();
        return false;
    }

    /** True for a tiket.com page that is not the login route. */
    private static boolean isTiketPageOtherThanLogin(String url) {
        if (!isTiketHost(url)) {
            return false;
        }
        String path = URI.create(url).getPath();
        return path == null || !path.endsWith("/login");
    }

    /**
     * True when {@code url} is served by tiket.com or one of its subdomains. Judged on the parsed host, so neither
     * a {@code ref=...tiket.com} query parameter on the account-site login page nor a {@code *.bliblitiket.com}
     * host counts.
     */
    private static boolean isTiketHost(String url) {
        if (url == null) {
            return false;
        }
        try {
            String host = URI.create(url).getHost();
            return host != null && (host.equals("tiket.com") || host.endsWith(".tiket.com"));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void clearBliblitiketCookies() {
        String bliblitiketUrl = bliblitiketAccountUrl();
        if (bliblitiketUrl == null) {
            return;
        }
        driverTools.get(bliblitiketUrl);
        driverTools.deleteAllCookies();
    }

    private String bliblitiketAccountUrl() {
        return switch (Configs.ENVIRONMENT) {
            case GK -> "https://account.test.bliblitiket.dev/";
            case PREPROD -> "https://account.preprod.bliblitiket.dev/";
            case PROD -> null;
        };
    }

    private boolean injectFreshSessionAndVerify(String email, String password, Unm<T> unm) {
        logger.log("[Cookie Login] Logging in via API for: " + email);
        String accessToken = unm.getAccessToken(email, password, "B2C");
        String rawToken = accessToken.replace("Bearer ", "");
        String refreshToken = unm.getRefreshToken(email, password);

        List<String> bliblitiketSetCookies = unm.getBliblitiketCookies(email);
        injectBliblitiketCookies(bliblitiketSetCookies);

        String homeUrl = switch (Configs.ENVIRONMENT) {
            case GK -> "https://gatotkaca.tiket.com/";
            case PREPROD -> "https://preprod.tiket.com/";
            case PROD -> "https://www.tiket.com/";
        };
        driverTools.getWithDynamicTimeout(homeUrl, 60);

        driverTools.deleteCookieNamed("session_access_token");
        driverTools.deleteCookieNamed("session_refresh_token");
        driverTools.deleteCookieNamed("_auto_session_email");
        driverTools.addCookie("session_access_token", rawToken, TIKET_COOKIE_DOMAIN, "/");
        // The web session is the access/refresh pair, and with the access token alone the app cannot
        // refresh it. The pair does not by itself stop the app from installing a guest session over
        // ours - that still happens now and then - which is what the settle check below is for.
        if (refreshToken != null && !refreshToken.isBlank()) {
            driverTools.addCookie("session_refresh_token", refreshToken, TIKET_COOKIE_DOMAIN, "/");
        } else {
            logger.softWarning("[Cookie Login] No session_refresh_token available for " + email
                    + "; the app may replace the injected session with a guest one");
        }
        driverTools.addCookie("_auto_session_email", email.toLowerCase(), TIKET_COOKIE_DOMAIN, "/");
        logger.log("[Cookie Login] Injected session_access_token" + (refreshToken != null && !refreshToken.isBlank()
                ? " and session_refresh_token" : "") + " cookie for " + email);

        driver.navigate().refresh();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        homePage.waitForHomePageToOpen();
        if (isMemberSessionSettled(email)) {
            return true;
        }
        driver.navigate().refresh();
        homePage.waitForHomePageToOpen();
        return isMemberSessionSettled(email);
    }

    /**
     * Confirms the injected session is really in place <em>and</em> stays in place.
     *
     * <p>The web app bootstraps its own session on every page load. When it cannot validate or refresh
     * the session it finds, it mints a fresh <em>guest</em> {@code session_access_token} and writes it
     * over ours; that write is asynchronous, so the profile icon can be on screen at the moment we look
     * and the session can still be a guest one a second later. Checking only the icon therefore reports
     * a login that the next navigation reveals as logged out, and any page reading the cookie (e.g.
     * loyalty points via the member API) gets a guest token with no {@code accountid}.
     *
     * <p>So this polls the cookie for the whole settle window, requiring the member token for
     * {@code email} to survive, and only then looks at the UI.
     */
    private boolean isMemberSessionSettled(String email) {
        long deadline = Instant.now().toEpochMilli() + (SESSION_SETTLE_SECONDS * 1000L);
        while (Instant.now().toEpochMilli() < deadline) {
            String cookieToken = driverTools.getCookieValue("session_access_token");
            if (cookieToken == null) {
                logger.log("[Cookie Login] session_access_token disappeared while the page settled");
                return false;
            }
            if (!Unm.isMemberToken(cookieToken)) {
                logger.log("[Cookie Login] session_access_token was replaced by a guest token while the page settled");
                logSessionCookies();
                return false;
            }
            String cookieEmail = Unm.getEmailClaim(cookieToken);
            if (cookieEmail != null && !cookieEmail.equalsIgnoreCase(email)) {
                logger.log("[Cookie Login] session_access_token belongs to " + cookieEmail + ", expected " + email);
                return false;
            }
            driverTools.dummyWait(1);
        }
        boolean loggedIn = checkIfLoggedIn();
        if (!loggedIn) {
            logger.log("[Cookie Login] Member cookie held but the UI still shows a logged-out header");
        }
        return loggedIn;
    }

    /** Prints every session_access_token cookie with its domain, so a guest cookie written next to the
     *  injected one is visible instead of being hidden behind whichever the driver returns first. */
    private void logSessionCookies() {
        for (Cookie cookie : driverTools.getCookiesNamed("session_access_token")) {
            logger.log("[Cookie Login] session_access_token cookie -> domain=" + cookie.getDomain()
                    + ", path=" + cookie.getPath()
                    + ", member=" + Unm.isMemberToken(cookie.getValue())
                    + ", email=" + Unm.getEmailClaim(cookie.getValue()));
        }
    }

    /**
     * Copies the bliblitiket cookies from the API login's Set-Cookie headers into the browser.
     * Cloudflare's {@code __cf_bm} bot-management cookie is skipped: it is bound to the client that received
     * it (the API client, not this browser), so carrying it over can only make Cloudflare distrust the browser.
     */
    private void injectBliblitiketCookies(List<String> rawSetCookies) {
        if (rawSetCookies == null || rawSetCookies.isEmpty()) {
            logger.log("[Cookie Login] No bliblitiket Set-Cookie headers to inject");
            return;
        }

        List<Cookie> cookiesToInject = new ArrayList<>();

        for (String raw : rawSetCookies) {
            String[] parts = raw.split(";");
            String nameValue = parts[0].trim();
            int eqIdx = nameValue.indexOf('=');
            if (eqIdx < 0) continue;

            String name = nameValue.substring(0, eqIdx).trim();
            String value = nameValue.substring(eqIdx + 1).trim();

            String domain = null;
            String path = "/";
            boolean secure = false;

            for (int i = 1; i < parts.length; i++) {
                String part = parts[i].trim();
                if (part.toLowerCase().startsWith("domain=")) {
                    domain = part.substring("domain=".length()).trim();
                } else if (part.toLowerCase().startsWith("path=")) {
                    path = part.substring("path=".length()).trim();
                } else if (part.equalsIgnoreCase("secure")) {
                    secure = true;
                }
            }

            if (name.equals(CLOUDFLARE_BOT_COOKIE)) {
                continue;
            }

            if (domain != null && domain.contains("bliblitiket")) {
                Cookie.Builder cb = new Cookie.Builder(name, value)
                        .domain(domain)
                        .path(path)
                        .isSecure(secure);
                cookiesToInject.add(cb.build());
            }
        }

        if (cookiesToInject.isEmpty()) {
            logger.log("[Cookie Login] No bliblitiket cookies found in Set-Cookie headers");
            return;
        }

        String bliblitiketUrl = bliblitiketAccountUrl();
        if (bliblitiketUrl == null) {
            logger.log("[Cookie Login] Bliblitiket cookie injection not supported on PROD");
            return;
        }

        logger.log("[Cookie Login] Navigating to " + bliblitiketUrl + " for bliblitiket cookie injection");
        try {
            driverTools.get(bliblitiketUrl);
        } catch (Exception e) {
            logger.log("[Cookie Login] Navigation to " + bliblitiketUrl + " threw: " + e.getMessage()
                    + "; attempting cookie injection anyway");
        }

        for (Cookie c : cookiesToInject) {
            try {
                driver.manage().deleteCookieNamed(c.getName());
                driver.manage().addCookie(c);
                logger.log("[Cookie Login] Injected bliblitiket cookie: " + c.getName());
            } catch (Exception e) {
                logger.log("[Cookie Login] Failed to inject bliblitiket cookie " + c.getName() + ": " + e.getMessage());
            }
        }
        logger.log("[Cookie Login] Bliblitiket cookie injection complete (" + cookiesToInject.size() + " cookies)");
    }

    /**
     * Logs out by navigating to {@code /en-sg/logout}; the server invalidates the session and
     * replaces the auth cookies. Falls back to the account menu when the session survives the URL,
     * and fails loudly if the user is still logged in after every attempt. Skips the navigation when
     * the current tiket page already shows a guest session.
     */
    @Override
    public void logoutViaUrl() {
        if (isOnTiketOrigin() && !hasMemberSessionCookie() && !driverTools.isElementDisplayed(LOGIN_PROFILE_ICON)) {
            logger.log("[Logout] No member session on the current page; already logged out");
            completeLogout("no member session on the current page");
            return;
        }
        completeLogout(logoutThroughUrl() ? LOGOUT_PATH_URL
                : logoutThroughAccountMenu() ? LOGOUT_PATH_ACCOUNT_MENU : null);
    }

    /**
     * Navigates to the environment's {@code /en-sg/logout} URL and confirms the member session is gone
     * on the page it lands on.
     *
     * @return {@code true} when the member session is confirmed gone.
     */
    private boolean logoutThroughUrl() {
        String logoutUrl = getTiketWebBaseUrl() + "en-sg/logout";
        logger.log("[Logout] Navigating to: " + logoutUrl);
        driverTools.get(logoutUrl);
        HomePageWeb.getInstance(session).waitForHomePageToOpen();
        dismissErrorViewIfShown();
        return isLoggedOutConfirmed();
    }

    /**
     * Final step shared by every dWeb logout. When neither the account menu nor the URL ended the
     * session, drops the session cookies locally and reloads as a last resort. Always clears the
     * automation email marker and the UNM token cache so the next cookie login starts clean.
     *
     * @param endedBy which earlier logout path confirmed the session gone, or {@code null} when none did.
     * @throws IllegalStateException when a member session is still active after every attempt.
     */
    private void completeLogout(String endedBy) {
        if (endedBy == null) {
            logger.softWarning("[Logout] Account menu and /logout URL did not end the session; "
                    + "dropping session cookies locally");
            clearSessionCookiesWithoutChangingPage();
            driverTools.refreshWebpage();
            if (isLoggedOutConfirmed()) {
                endedBy = "local cookie cleanup";
            }
        }
        if (isOnTiketOrigin()) {
            driverTools.deleteCookieNamed("_auto_session_email");
        }
        new Unm<>(session).invalidateAllCachedTokensForCurrentSession();
        if (endedBy == null) {
            throw new IllegalStateException("[Logout] Member session still active after account menu, "
                    + "/logout URL and local cookie cleanup");
        }
        logger.log("[Logout] Logout confirmed via " + endedBy);
    }

    /**
     * Confirms the browser no longer holds a member session: the {@code session_access_token} cookie
     * must stop carrying a member token and the header must not show the profile icon.
     */
    private boolean isLoggedOutConfirmed() {
        boolean memberTokenGone = driverTools.waitUntil(d -> !hasMemberSessionCookie(), LOGOUT_CONFIRM_SECONDS).isPresent();
        if (!memberTokenGone) {
            logger.log("[Logout] session_access_token still carries a member token");
            return false;
        }
        if (driverTools.waitForElementToAppear(LOGIN_PROFILE_ICON, PROFILE_ICON_RECHECK_SECONDS).isPresent()) {
            logger.log("[Logout] Member cookie is gone but the header still shows the profile icon");
            return false;
        }
        return true;
    }

    private boolean hasMemberSessionCookie() {
        String accessToken = driverTools.getCookieValue("session_access_token");
        return accessToken != null && Unm.isMemberToken(accessToken);
    }

    private boolean isOnTiketOrigin() {
        return isTiketHost(driverTools.getCurrentURL());
    }

    private void dismissErrorViewIfShown() {
        driverTools.waitForElementToAppear(RELOAD_BUTTON, 2).ifPresent(driverTools::click);
    }

    /**
     * Ensures a guest session: logs out when a member session is active, then drops the session
     * cookies without leaving the current page and wipes the UNM token cache.
     */
    public void loginAsGuest() {
        boolean loggedIn = hasMemberSessionCookie() || checkIfLoggedIn();
        logger.log("is user logged in? " + loggedIn);
        if (loggedIn) {
            logoutViaUrl();
        }
        clearSessionCookiesWithoutChangingPage();
        new Unm<>(session).invalidateAllCachedTokensForCurrentSession();
    }

    private void clearSessionCookiesWithoutChangingPage() {
        String currentUrl = driverTools.getCurrentURL();
        if (isOnTiketOrigin()) {
            driverTools.deleteCookieNamed("session_access_token");
            driverTools.deleteCookieNamed("session_refresh_token");
            driverTools.deleteCookieNamed("_auto_session_email");
            return;
        }

        clearSessionCookies();
        if (currentUrl != null && !currentUrl.isBlank()) {
            try {
                if (!currentUrl.equals(driverTools.getCurrentURL())) {
                    driverTools.get(currentUrl);
                }
            } catch (Exception e) {
                logger.log("Unable to restore URL after cookie cleanup: " + e.getMessage());
            }
        }
    }

    public void loginStepsWithoutRetry(String email, String password, String otp) {
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        loginB2CInDWEB.inputEmail(email);
        loginB2CInDWEB.inputPassword(password);
        loginB2CInDWEB.inputOTPCodeWithoutRetry(otp);
    }

    public void loginSteps(String email , String password , String otp , int retryCounter) throws FailedLoginException, InterruptedException{
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        loginB2CInDWEB.inputEmail(email);
        loginB2CInDWEB.inputPassword(password);
        try{
            loginB2CInDWEB.inputOTPCode(otp, retryCounter);
        }catch (Exception e){
            logger.log(e.getMessage());
        }
    }

    public boolean isRegisterPageOpened() {
        driverTools.waitForElementToAppear(REGISTER_BUTTON_LOGIN_PAGE);
        return driverTools.isElementDisplayed(REGISTER_BUTTON_LOGIN_PAGE);
    }

    public void registerIntoDweb(String email) throws FailedLoginException, InterruptedException{
        driverTools.waitForElementToAppear(REGISTER_BUTTON_LOGIN_PAGE, 20);
        driverTools.waitForElementToBeClickable(REGISTER_BUTTON_LOGIN_PAGE, 20);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        loginB2CInDWEB.inputEmailToRegister(email);
    }

    public void openLoginPage() {
        //open login page, with assumption user must come from from home page.
        com.tiket.page.home.HomePage<T> homePage = com.tiket.page.home.HomePage.getInstance(this.session);
        homePage.openAccount();
        driverTools.click(LOGIN_BUTTON);
        if(isOnLoginPageNative()){
            return;
        }

        boolean isOnLoginPage = this.isLoginPageOpened();
        if (!isOnLoginPage) {
            throw new NoSuchElementException("Login page did not open");
        }
    }

    protected boolean openLoginPageWithDeeplink() {
        Deeplink loginPage = new Deeplink("login page","tiket://m.gatotkaca.tiket.com/login", "tiket://m.preprod.tiket.com/login", "tiket://m.tiket.com/login");
        try {
            driverTools.deeplink(loginPage);
        }catch (Exception e) {
            logger.log("error opening deeplink : " + e.getLocalizedMessage());
        }
        return isOnLoginPageNative();
    }

    public String forgotPasswordDweb(String username, String password, String otp, int retryCounter) throws FailedLoginException, InterruptedException {
        String updatedPassword = password + randomInt(1);

        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        openLoginPageWithDeeplink();

        driverTools.waitForElementToAppear(LOGIN_BUTTON).ifPresent(driverTools::click);
        driverTools.waitForElementToBeClickable(FORGOT_PASSWORD, 20);
        driverTools.click(FORGOT_PASSWORD);
        driverTools.isElementDisplayed(FORGOT_PASSWORD_TITLE);
        driverTools.waitForElementToAppear(FORGOT_PASSWORD_NEXT, 30);
        driverTools.waitForElementToBeClickable(FORGOT_PASSWORD_NEXT, 30);
        driverTools.click(FORGOT_PASSWORD_NEXT);
        driverTools.switchToNative();
        loginB2CInDWEB.inputOTPCode(otp, retryCounter);
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("bliblitiket.com/login");
        driverTools.waitForElementToAppear(FORGOT_PASSWORD_INPUT).ifPresent(driverTools::click);
        driverTools.sendKeys(FORGOT_PASSWORD_INPUT,updatedPassword);
        driverTools.clickWithJavascriptExecutor(FORGOT_PASSWORD_CONFIRM);
        Boolean isOnLoginPage = driverTools.isElementDisplayed(PASSWORD_VIEW_BUTTON);
        driverTools.switchToNative();
        Assertion.assertThat("Forgot password is successful", isOnLoginPage, is(true));
        return updatedPassword;
    }

    private boolean isOnLoginPageNative() {
        boolean isElementDisplayed = driverTools.waitForElementToAppear(CREATE_ACCOUNT_BUTTON,5).isPresent();
        return isElementDisplayed;
    }

    public void goTillLoginPageWithoutLoggingIn(String email) throws FailedLoginException, InterruptedException {
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        goToAccountPage();
        if(!isAccountHolderNameDisplayed()){
            homePage.goToHomePage();
            homePage.clickOnLoginButton();
        }
        else{
            String accountHolderName = accountHolderName();
            if(accountHolderName.equals(email)){
                homePage.goToHomePage();
            }
            else{
                homePage.goToHomePage();
                logoutFromDweb();
                homePage.clickOnLoginButton();
            }
        }
    }


}