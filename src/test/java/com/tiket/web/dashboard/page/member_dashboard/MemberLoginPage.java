package com.tiket.web.dashboard.page.member_dashboard;

import com.tiket.api.auth.AdminDashboardLoginResponseParser;
import com.tiket.api.auth.AdminDashboardLoginResponseParser.ParsedCookie;
import com.tiket.api.auth.POSTGetAdminAccessTokenDashboardDynamicApi;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.Matchers.is;

public class MemberLoginPage<T extends WebDriver> extends BasePage<T> {

    public MemberLoginPage(Session<T> session) {
        super(session);
    }

    public static final String GK_URL = "https://gatotkaca.tiket.com";
    public static final String PREPROD_URL = "https://preprod.tiket.com";
    public static final String PROD_URL = "https://tiket.com";
    public static final String LOGOUT_PATH_V2 = "/admin/member/v2/logout";
    public static final String LOGOUT_PATH_V1 = "/admin/member/logout";
    private static final String ADMIN_LANDING_PATH = "/admin/member";
    private static final long EXPIRY_SKEW_MS = 60_000L;
    private static final String[] AUTH_COOKIE_NAMES =
            {"aat", "art", "session_access_token", "session_refresh_token", "_auto_admin_email"};
    /**
     * Logged-in indicator for {@code /admin/member} and other dashboards. The member home renders the
     * account/email block ({@code HeaderAccountInfo_*}); the other xpaths cover classic and newer
     * dashboard headers so the check is robust regardless of landing page.
     */
    private static final Locator LOGGED_IN_INDICATOR = new Locator("LOGGED_IN_INDICATOR",
            By.xpath("//div[contains(@class,'HeaderAccountInfo')]"
                    + " | //div[@class='account-card']"
                    + " | //div[contains(@class,'Header_user_info')]"
                    + " | //div[@class='account-toolbar']"));

    /** Session-token cache shared across parallel dashboard tests, keyed by admin username. */
    private static final ConcurrentHashMap<String, CachedAdminSession> ADMIN_SESSION_CACHE = new ConcurrentHashMap<>();

    private record CachedAdminSession(List<ParsedCookie> cookies, long expiryMillis) {}

    private static final Locator AQUA_LOGO_V1 = new Locator("AQUA_LOGO_V1", By.xpath("//div[@class='logo-wrapper']"));
    private static final Locator AQUA_LOGO = new Locator("AQUA_LOGO", By.xpath("logo-wrapper"));
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[@class='account-card']"));
    private static final Locator USERNAME_BAR = new Locator("USERNAME_BAR", By.xpath("//div[@class='text-input hasLabel']/input[@name='username']"));
    private static final Locator PASSWORD_BAR = new Locator("PASSWORD_BAR", By.xpath("//div[@class='password-form']//div[@class='text-input hasLabel']/input[@type='password']"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", By.xpath("//*[contains(@class,'styles_login_layout')]//button[contains(@class, 'button')]"));
    private static final Locator EMAIL_TEXT = new Locator("EMAIL_TEXT", By.xpath("//div[contains(@class,'HeaderAccountInfo_email__')]"));
    private static final Locator EMAIL_LABEL = new Locator("EMAIL_LABEL", By.className("email_label"));
    private static final Locator AQUA_LOGO_V2 = new Locator("AQUA_LOGO_V2", By.xpath("//img[@alt='login logo']"));
    private static final Locator USERNAME_BAR_V2 = new Locator("USERNAME_BAR_V2", By.id("sb-input-username"));
    private static final Locator PASSWORD_BAR_V2 = new Locator("PASSWORD_BAR_V2", By.id("sb-input-password"));
    private static final Locator LOGIN_BUTTON_V2 = new Locator("LOGIN_BUTTON_V2", By.xpath("//button[text()='LOGIN']"));
    private static final Locator LOGGED_IN_USER_EMAIL = new Locator("LOGGED_IN_USER_EMAIL", new By.ByXPath("//div[contains(@class,'HeaderAccountInfo_email')]"));

    private boolean isLogin(String email) {
        driverTools.waitForElementToAppear(AQUA_LOGO_V2);
        driverTools.waitForElementToAppear(EMAIL_TEXT, 5);
        final String emailAdminV2 = driverTools.getText(EMAIL_TEXT).isPresent() ? driverTools.getText(EMAIL_TEXT).get() : "";
        final String emailAdminV1 = driverTools.getText(EMAIL_LABEL).isPresent() ? driverTools.getText(EMAIL_LABEL).get() : "";
        if(emailAdminV1.equals(email) || !driverTools.isElementDisplayed(AQUA_LOGO_V2)) {
            return true;
        } else return emailAdminV2.equals(email);
    }

    public void adminLogin(String email, String password) {
        if (!isLogin(email)) {
            driverTools.waitForElementToAppear(USERNAME_BAR_V2);
            driverTools.sendKeys(USERNAME_BAR_V2, email);
            driverTools.sendKeys(PASSWORD_BAR_V2, password);
            driverTools.waitForElementToAppear(LOGIN_BUTTON_V2, 5);
            driverTools.click(LOGIN_BUTTON_V2);
            driverTools.waitForElementToAppear(USERNAME_GREETING, 20);
        } else {
            logger.log("Already login as: " + email);
        }
    }

    public void gotoMemberLoginPage() {
        driver.manage().deleteAllCookies();
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/affiliate/flight");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/affiliate/flight");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driverTools.get("https://www.tiket.com/admin/affiliate/flight");
        } else {
            throw new NotImplementedException("Environment " + Configs.ENVIRONMENT + " is not implemented yet.");
        }
    }

    public boolean isMemberLoginPageLoaded() {
        return driverTools.getTitle().contains("Member") && driverTools.isElementDisplayed(AQUA_LOGO);
    }

    public void inputLogin(String username, String password) {
        driverTools.click(USERNAME_BAR);
        driverTools.sendKeys(USERNAME_BAR, username);
        driverTools.click(PASSWORD_BAR);
        driverTools.sendKeys(PASSWORD_BAR, password);
        driverTools.click(LOGIN_BUTTON);
    }

    public void clickOnButton() {
        driverTools.click(LOGIN_BUTTON);
    }

    public void goToPage(String path) {
        Character firstChar = path.charAt(0);
        if (!firstChar.equals('/')) {
            path = "/" + path;
        }

        String url;
        switch (Configs.ENVIRONMENT) {
            case GK -> url = GK_URL + path;
            case PREPROD -> url = PREPROD_URL + path;
            case PROD -> url = PROD_URL + path;
            default -> throw new NotImplementedException(Configs.ENVIRONMENT + " is not implemented yet.");
        }
        driverTools.get(url);
    }
    public boolean isAdminLogin() {
        driverTools.waitForElementToAppear(USERNAME_GREETING, 20);
        return driverTools.isElementDisplayed(USERNAME_GREETING);
    }

    public boolean isLoginUsingExpectedEmail(String expectedEmail) {
        driverTools.waitForElementToAppear(EMAIL_TEXT, 1);
        final String currentEmail = driverTools.getText(EMAIL_TEXT).orElse("");
        if(currentEmail.isEmpty() || !currentEmail.equalsIgnoreCase(expectedEmail)) {
            logger.log("---> Current email: " + currentEmail + " does not match expected email: " + expectedEmail);
            return false;
        } else {
            logger.log("---> Current email: " + currentEmail + " matches expected email: " + expectedEmail);
            return true;
        }
    }

    //TODO: remove v2 path and use v1 for logout because v2 is merged to v1
    public void logoutAdmin() {
        goToPage(LOGOUT_PATH_V2);
        if (isPageNotFound()) {
            logger.log("<b>[WARN]</b> v2 logout returned 404, falling back to v1 logout");
            goToPage(LOGOUT_PATH_V1);
        }
    }

    private boolean isPageNotFound() {
        String pageSource = driver.getPageSource();
        String title = driver.getTitle();
        return title.contains("404") || title.contains("Not Found")
                || pageSource.contains("\"statusCode\":404")
                || pageSource.contains("\"status\":404");
    }

    /**
     * Logs into the admin dashboard via session-cookie injection instead of the UI form — a port of
     * the dWeb {@code loginWithSpecificAccountViaCookie} flow. The admin login API ({@code member-core}
     * REST {@code /tix-member-core/v3/admin/login}) returns no MFA challenge for internal accounts, so
     * the whole auth cookie set ({@code aat}, {@code art}, {@code session_access_token},
     * {@code session_refresh_token}) is fetched once and replayed into the browser.
     *
     * <p>Behaviour mirrors dWeb: valid-session reuse, same-user UI re-verify (re-injecting if the cookie
     * is present but the UI shows logged out), different-user logout, one self-healing retry, and a
     * PROD guardrail that falls back to UI login (the REST login host is not addressable on PROD).
     */
    public void adminLoginViaCookie(String username, String password) {
        adminLoginViaCookieInjection(username, password, true);
    }

    private void adminLoginViaCookieInjection(String username, String password, boolean logoutDifferentUser) {
        if (Configs.ENVIRONMENT == Environment.PROD) {
            logger.log("[Admin Cookie Login] PROD -> falling back to UI login for: " + username);
            gotoMemberLoginPage();
            adminLogin(username, password);
            return;
        }

        driverTools.maximizeWindow();
        // Land on the dashboard home (same .tiket.com origin) so cookies can be read/written.
        // If not yet authenticated this simply redirects to the member login page.
        goToPage(ADMIN_LANDING_PATH);
        if (isAdminSessionActive(3)) {
            String loggedInUser = getLoggedInUserEmail();
            boolean sameUser = username.equalsIgnoreCase(loggedInUser);
            if (sameUser && isCookieStillValid(driver.manage().getCookieNamed("session_access_token"))) {
                Assertion.assertThat("Logged-in user matches test data", loggedInUser.equalsIgnoreCase(username), is(true));
                logger.log("[Admin Cookie Login] Already logged in as " + username + ", keeping existing session");
                return;
            }
            if (!sameUser && logoutDifferentUser) {
                logger.log("[Admin Cookie Login] Logged in as " + loggedInUser + ", logging out before switching to " + username);
                logoutAdmin();
            }
        }
        Cookie existingCookie = driver.manage().getCookieNamed("session_access_token");
        Cookie userCookie = driver.manage().getCookieNamed("_auto_admin_email");

        if (isCookieStillValid(existingCookie)) {
            String sessionUser = (userCookie != null) ? userCookie.getValue() : null;
            if (username.equalsIgnoreCase(sessionUser)) {
                logger.log("[Admin Cookie Login] Valid session for " + username + ", verifying UI state");
                goToPage(ADMIN_LANDING_PATH);
                driver.navigate().refresh();
                if (isAdminSessionActive(10)) {
                    logger.log("[Admin Cookie Login] Confirmed logged in as " + username);
                    return;
                }
                logger.log("[Admin Cookie Login] Cookie matches but UI not logged in, re-injecting");
            } else {
                logger.log("[Admin Cookie Login] Session belongs to " + sessionUser + ", need to re-login as " + username);
            }
        }

        if (injectFreshAdminSessionAndVerify(username, password)) {
            return;
        }
        logger.log("[Admin Cookie Login] Injected session did not authenticate. Invalidating cache and retrying once.");
        invalidateAdminSession(username);
        Assertion.assertThat("Admin session-cookie injection produced logged-in UI for " + username,
                injectFreshAdminSessionAndVerify(username, password), is(true));
    }
    private boolean injectFreshAdminSessionAndVerify(String username, String password) {
        List<ParsedCookie> cookies = getAdminSessionCookies(username, password);
        for (String name : AUTH_COOKIE_NAMES) {
            driver.manage().deleteCookieNamed(name);
        }
        long now = System.currentTimeMillis();
        for (ParsedCookie pc : cookies) {
            driver.manage().addCookie(new Cookie.Builder(pc.name(), pc.value())
                    .domain(".tiket.com")
                    .path("/")
                    .expiresOn(new Date(now + pc.maxAgeMillis()))
                    .build());
        }
        driver.manage().addCookie(new Cookie.Builder("_auto_admin_email", username.toLowerCase())
                .domain(".tiket.com")
                .path("/")
                .build());
        logger.log("[Admin Cookie Login] Injected " + cookies.size() + " session cookie(s) for " + username);

        goToPage(ADMIN_LANDING_PATH);
        driver.navigate().refresh();
        if (isAdminSessionActive(15)) {
            Assertion.assertThat("Logged-in user matches test data",
                    getLoggedInUserEmail().equalsIgnoreCase(username), is(true));
            return true;
        }
        driver.navigate().refresh();

        if (isAdminSessionActive(15)) {
            Assertion.assertThat("Logged-in user matches test data",
                    getLoggedInUserEmail().equalsIgnoreCase(username), is(true));
            return true;
        }
        return false;
    }
    private String getLoggedInUserEmail() {
        return driverTools.getText(LOGGED_IN_USER_EMAIL).orElse("");
    }
    private List<ParsedCookie> getAdminSessionCookies(String username, String password) {
        CachedAdminSession cached = ADMIN_SESSION_CACHE.get(username);
        if (cached != null && cached.expiryMillis() - EXPIRY_SKEW_MS > System.currentTimeMillis()) {
            logger.log("[Admin Cookie Login] Reusing cached session cookies for " + username);
            return cached.cookies();
        }
        return fetchAdminSessionCookies(username, password);
    }

    private List<ParsedCookie> fetchAdminSessionCookies(String username, String password) {
        POSTGetAdminAccessTokenDashboardDynamicApi.Inputs inputs =
                new POSTGetAdminAccessTokenDashboardDynamicApi.Inputs(username, password);
        ApiResponse response = new POSTGetAdminAccessTokenDashboardDynamicApi(inputs).hitApi(apiTools);

        Assertion.assertThat("Admin login API returned success for " + username,
                AdminDashboardLoginResponseParser.isLoginSuccessBody(response.getBody()), is(true));

        List<ParsedCookie> cookies = AdminDashboardLoginResponseParser.parseSetCookies(response, 3_600_000L);
        Assertion.assertThat("Admin login set session_access_token cookie for " + username,
                cookies.stream().anyMatch(c -> "session_access_token".equals(c.name())), is(true));

        long expiry = cookies.stream()
                .filter(c -> "session_access_token".equals(c.name()))
                .mapToLong(c -> System.currentTimeMillis() + c.maxAgeMillis())
                .findFirst()
                .orElse(System.currentTimeMillis() + 3_600_000L);
        ADMIN_SESSION_CACHE.put(username, new CachedAdminSession(cookies, expiry));
        logger.log("[Admin Cookie Login] Fetched " + cookies.size() + " cookie(s) for " + username
                + ", cache expires " + new Date(expiry));
        return cookies;
    }

    private void invalidateAdminSession(String username) {
        if (ADMIN_SESSION_CACHE.remove(username) != null) {
            logger.log("[Admin Cookie Login] Invalidated cached session for " + username);
        }
    }

    private boolean isCookieStillValid(Cookie cookie) {
        if (cookie == null) {
            return false;
        }
        Date expiry = cookie.getExpiry();
        return expiry == null || expiry.getTime() - EXPIRY_SKEW_MS > System.currentTimeMillis();
    }

    private void clearAdminSessionCookies() {
        for (String name : AUTH_COOKIE_NAMES) {
            driver.manage().deleteCookieNamed(name);
        }
    }

    private boolean isAdminSessionActive(int timeoutSeconds) {
        driverTools.waitForElementToAppear(LOGGED_IN_INDICATOR, timeoutSeconds);
        return driverTools.isElementDisplayed(LOGGED_IN_INDICATOR);
    }
}