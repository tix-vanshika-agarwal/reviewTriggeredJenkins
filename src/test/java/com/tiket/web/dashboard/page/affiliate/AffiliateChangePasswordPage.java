package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AffiliateChangePasswordPage<T extends WebDriver> extends BasePage<T> {
    public AffiliateChangePasswordPage(Session<T> session) {
        super(session);
    }

    private static final Locator FORGOT_PASSWORD_BTN = new Locator("FORGOT_PASSWORD_BTN", By.xpath("//*[text()='Forgot your password?']"));
    private static final Locator FORGOT_PASSWORD_TEXT = new Locator("FORGOT_PASSWORD_TEXT", By.xpath("//p[normalize-space(text())='Just enter your phone number or email to create a new password.']"));

    public void clickForgotPassword() {
        driverTools.click(FORGOT_PASSWORD_BTN);
    }

    public boolean checkForgotPasswordPage() {
        return driverTools.isElementDisplayed(FORGOT_PASSWORD_TEXT);
    }
}