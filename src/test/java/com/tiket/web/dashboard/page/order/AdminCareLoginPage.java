package com.tiket.web.dashboard.page.order;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.web.dashboard.page.payment.PaymentDashboardLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public class AdminCareLoginPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator LOGIN_FORM = new Locator("LOGIN_FORM", new By.ByClassName("login-form-card"));
    private static final Locator INPUT_USERNAME = new Locator("INPUT_USERNAME", new By.ByXPath("//input[contains(@class,'input-login')][@type='text']"));
    private static final Locator INPUT_PASSWORD = new Locator("INPUT_PASSWORD", new By.ByXPath("//input[contains(@class,'input-login')][@type='password']"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", new By.ByXPath("//button[text()='Sign In']"));
    public AdminCareLoginPage(Session<T> session) {
        super(session);
    }

    public void loginToAdminCare(String username, String password) {
        Optional<WebElement> loginForm = driverTools.waitForElementToAppear(LOGIN_FORM, 3);
        if (loginForm.isEmpty()) {
            PaymentDashboardLoginPage<?> paymentDashboardLoginPage = new PaymentDashboardLoginPage<>(session);
            paymentDashboardLoginPage.loginToPaymentDashBoard(username, password);
        } else {
            driverTools.sendKeys(INPUT_USERNAME, username);
            driverTools.sendKeys(INPUT_PASSWORD, password);
            driverTools.click(LOGIN_BUTTON);
        }
    }

}
