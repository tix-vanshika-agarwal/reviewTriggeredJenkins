package com.tiket.web.dashboard.page.nft.train;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TrainLoginPage <T extends WebDriver> extends BasePage<T> {

    private static final Locator LOGIN_FORM = new Locator("LOGIN_FORM", By.xpath("//img[@src='/external/assets/login-logo.png']"));
    private static final Locator USER_NAME = new Locator("USER_NAME", By.xpath("//input[@id='sb-input-username']"));
    private static final Locator PASSWORD = new Locator("PASSWORD", By.xpath("//input[@id='sb-input-password']"));
    private static final Locator LOGIN = new Locator("LOGIN", By.xpath("//button[@type='submit']"));
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[contains(@class, 'header_dashboard')]"));

    public TrainLoginPage(Session<T> session) {
        super(session);
    }

    /**
     * Consolidated train-dashboard entry point: authenticates the admin via session-cookie injection
     * ({@link MemberLoginPage#adminLoginViaCookie}) and then opens the train dashboard. Post-login
     * navigation to {@code /admin/train-dashboard} is preserved exactly as before.
     */
    public void goToTrainPage(String username, String password) {
        driverTools.maximizeWindow();
        new MemberLoginPage<>(session).adminLoginViaCookie(username, password);
        openTrainDashboardUrl();
    }

    /** Original UI form-based train login, retained for the one end-to-end UI smoke test. */
    public void goToTrainPageViaUI(String username, String password) {
        driverTools.maximizeWindow();

        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/member");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/member");
        }

        if (!isTrainLoggedIn()) {
            loginToTrainPage(username, password);
            driverTools.waitForElementToAppear(USERNAME_GREETING, 2);
        }
        openTrainDashboardUrl();
    }

    private void openTrainDashboardUrl() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/train-dashboard");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/train-dashboard");
        }
    }

    public void loginToTrainPage(String username, String password){
        driverTools.waitForElementToAppear(LOGIN).orElseThrow();
        driverTools.sendKeys(USER_NAME, username);
        driverTools.sendKeys(PASSWORD, password);
        driverTools.click(LOGIN);
    }

    public boolean isTrainLoggedIn() {
        driverTools.waitForElementToAppear(USERNAME_GREETING, 2);
        return driverTools.isElementDisplayed(USERNAME_GREETING);
    }
}
