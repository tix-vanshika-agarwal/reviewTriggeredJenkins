package com.tiket.web.dashboard.page.payment;

import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PaymentDashboardLoginPage<T extends WebDriver> extends BasePage<T> {

    private static final Locator LOGIN_FORM = new Locator("LOGIN_FORM", By.xpath("//img[@src='/external/assets/login-logo.png']"));
    private static final Locator USER_NAME = new Locator("USER_NAME", By.xpath("//*[contains(@id,'username') or contains(@name,'username')]"));
    private static final Locator PASSWORD = new Locator("PASSWORD", By.xpath("//*[contains(@id,'password') or contains(@name,'password')]"));
    private static final Locator LOGIN = new Locator("Login", By.xpath("//button[contains(text(),'LOGIN')]"));

    public PaymentDashboardLoginPage(Session<T> session) {
        super(session);
    }

    public void loginToPaymentDashBoard(String username, String password) {
        driverTools.waitForElementToAppear(USER_NAME, 3).ifPresentOrElse(
            element -> { driverTools.sendKeys(element, username); },
            () -> { throw new DriverToolsException("Failed sending keys: "+username+" to: "+USER_NAME); });
        driverTools.waitForElementToAppear(PASSWORD, 3).ifPresentOrElse(
            element -> { driverTools.sendKeys(element, password); },
            () -> { throw new DriverToolsException("Failed sending keys: "+password+" to: "+PASSWORD); });
        driverTools.click(LOGIN);
    }
}