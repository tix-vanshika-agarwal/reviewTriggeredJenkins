package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.core.Is.is;

public class LoginPage <T extends WebDriver> extends BasePage<T> {
    public LoginPage(Session<T> session) {
        super(session);
    }

    private static final String MEMBER_LOGIN_URL_GK = "https://gatotkaca.tiket.com/admin/member/login";
    private static final String MM_MODULE_URL_GK = "https://gatotkaca.tiket.com/admin/mm-dashboard";
    private static final String MEMBER_LOGIN_URL_PREPROD = "https://preprod.tiket.com/admin/member/login";
    private static final String MM_MODULE_URL_PREPROD = "https://en.preprod.tiket.com/admin/mm-dashboard";
    private static final Locator LOGIN_TITLE = new Locator("LOGIN_TITLE", new By.ByXPath("//h1[(contains(text(), 'Login'))]"));
    private static final Locator USERNAME_INPUT = new Locator("USERNAME_INPUT", new By.ByXPath("//input[@id='sb-input-username']"));
    private static final Locator PASSWORD_INPUT = new Locator("PASSWORD_INPUT", new By.ByXPath("//input[@id='sb-input-password']"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", new By.ByXPath("//button[text()='LOGIN']"));
    private static final Locator EMAIL_LABEL = new Locator("EMAIL_LABEL", new By.ByXPath("//div[contains(@class, 'HeaderAccountInfo_email__6Lr37')]"));

    public void goToDashboardMemberLoginPage() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get(MEMBER_LOGIN_URL_GK);
            return;
        }
        driverTools.get(MEMBER_LOGIN_URL_PREPROD);
    }

    public boolean isDashboardMemberLoginPageLoaded() {
        driverTools.waitForElementToAppear(LOGIN_TITLE, 3);
        return driverTools.isElementDisplayed(LOGIN_TITLE);
    }

    public void loginToDashboardMember(String username, String password) {
        driverTools.sendKeys(USERNAME_INPUT, username);
        driverTools.sendKeys(PASSWORD_INPUT, password);
        driverTools.click(LOGIN_BUTTON);
    }

    public boolean isLoginToDashboardMemberSuccessful(String username) {
        driverTools.waitForElementToAppear(EMAIL_LABEL);
        return driverTools.getText(EMAIL_LABEL).orElseThrow().contains("@tiket.com");
    }

    public void openMMModuleUrl() {
        try {
            if (Configs.ENVIRONMENT == Environment.GK) {
                driverTools.get(MM_MODULE_URL_GK);
                return;
            }
            driverTools.get(MM_MODULE_URL_PREPROD);
        } catch (Exception timeoutException) {
            logger.log("Got timeout, but let's check if we successfully opened the MM Module");
        }

    }

    /**
     * Consolidated MM-module entry point: authenticates the admin via session-cookie injection
     * ({@link MemberLoginPage#adminLoginViaCookie}) and then opens the MM dashboard. The cookies are
     * scoped to {@code .tiket.com}, so they remain valid when navigating from {@code preprod.tiket.com}
     * (where login happens) to {@code en.preprod.tiket.com/admin/mm-dashboard}.
     */
    public void openMMModule(String username, String password) {
        new MemberLoginPage<>(session).adminLoginViaCookie(username, password);
        openMMModuleUrl();
    }

    /** Original UI form-based MM-module login, retained for the one end-to-end UI smoke test. */
    public void openMMModuleViaUI(String username, String password) {
        goToDashboardMemberLoginPage();
        if (isDashboardMemberLoginPageLoaded()) {
            Assertion.assertThat("Dashboard member login page is loaded", isDashboardMemberLoginPageLoaded(), is(true));
            loginToDashboardMember(username, password);
            Assertion.assertThat("Login to dashboard member successful", isLoginToDashboardMemberSuccessful(username), is(true));
        } else {
            Assertion.assertThat("User has been logged in to Member Dashboard", isLoginToDashboardMemberSuccessful(username), is(true));
        }
        openMMModuleUrl();
    }
}