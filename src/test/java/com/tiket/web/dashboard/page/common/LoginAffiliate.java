package com.tiket.web.dashboard.page.common;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginAffiliate<T extends WebDriver> extends BasePage<T> {
    public LoginAffiliate(Session<T> session) {
        super(session);
    }

    private static final Locator EMAIL_FIELD = new Locator("EMAIL_FIELD", By.xpath("//input[@id='sb-input-username']"));
    private static final Locator PASSWORD_FIELD = new Locator("PASSWORD_FIELD", By.xpath("//input[@id='sb-input-password']"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", By.xpath("//button[@data-testid='btn-login']"));
    private static final Locator DASHBOARD_HEADING = new Locator("DASHBOARD_HEADING", By.xpath("//h1[text()='Welcome to Affiliate External Dashboard!']"));
    private static final Locator SELECT_PARTNER = new Locator("SELECT_PARTNER", By.xpath("//div[.//span[text()='Business ID:']]/div/span[last()]"));

    public boolean isLoggedIn() {
        driverTools.waitForElementToAppear(DASHBOARD_HEADING, 10);
        return driverTools.isElementDisplayed(DASHBOARD_HEADING);
    }

    public void selectPartner() {
        driverTools.waitForElementToAppear(SELECT_PARTNER, 10);
        driverTools.clickWithJavascriptExecutor(SELECT_PARTNER);
    }

    public void inputLogin(String username, String password) {
        driverTools.click(EMAIL_FIELD);
        driverTools.sendKeys(EMAIL_FIELD, username);
        driverTools.click(PASSWORD_FIELD);
        driverTools.sendKeys(PASSWORD_FIELD, password);
        driverTools.click(LOGIN_BUTTON);
    }

    public void goToAffiliateDashboardLogin() {
        driver.manage().deleteAllCookies();
        try {
            if (Configs.ENVIRONMENT == Environment.GK) {
                driverTools.get("https://gatotkaca.tiket.com/affiliate/v2/dashboard/login");
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                driverTools.get("https://preprod.tiket.com/affiliate/v2/dashboard/login");
            } else if (Configs.ENVIRONMENT == Environment.PROD) {
                driverTools.get("https://www.tiket.com/affiliate/v2/dashboard/login");
            }
            logger.log("Navigated to Affiliated Dashboard successfully.");
        } catch (Exception e) {
            logger.fail("Failed to Navigate to Affiliated Dashboard.");
            throw e;
        }
    }
}