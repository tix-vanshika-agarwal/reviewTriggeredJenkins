package com.tiket.web.dashboard.page.accommodation.external_supplier.dashboard;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hamcrest.core.Is.is;

public class LoginPage<T extends WebDriver> extends BasePage<T> {

    private static final String SUPPLY_DASHBOARD_URL_GK     = "https://supply.gatotkaca.tiket.com";
    private static final String SUPPLY_DASHBOARD_URL_PREPROD = "https://supply.preprod.tiket.com";
    private static final String SUPPLY_DASHBOARD_URL_PROD    = "https://supply.tiket.com";

    private static final Locator EMAIL_INPUT    = new Locator("EMAIL_INPUT",    By.xpath("//input[@type='email' or @name='email' or @id='email' or contains(translate(@placeholder,'EMAIL','email'),'email')]"));
    private static final Locator PASSWORD_INPUT = new Locator("PASSWORD_INPUT", By.xpath("//input[@type='password' or @name='password' or @id='password']"));
    private static final Locator LOGIN_BUTTON   = new Locator("LOGIN_BUTTON",   By.xpath("//button[@type='submit' or normalize-space()='Login' or normalize-space()='Log In' or normalize-space()='LOGIN' or normalize-space()='Sign In']"));

    private static final Locator OTP_TEXTFIELD1 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("//input[@type='number' and @name='0']"));
    private static final Locator OTP_TEXTFIELD2 = new Locator("OTP_TEXTFIELD2", new By.ByXPath("//input[@type='number' and @name='1']"));
    private static final Locator OTP_TEXTFIELD3 = new Locator("OTP_TEXTFIELD3", new By.ByXPath("//input[@type='number' and @name='2']"));
    private static final Locator OTP_TEXTFIELD4 = new Locator("OTP_TEXTFIELD4", new By.ByXPath("//input[@type='number' and @name='3']"));

    private static final Locator DASHBOARD_INDICATOR = new Locator("DASHBOARD_INDICATOR", new By.ByXPath("(//div[.//img[@alt='tiket.com logo'] and .//span[text()='Supplier Dashboard']])[3]"));
    private static final Locator USER_AVATAR = new Locator("USER_AVATAR", By.xpath("//span[@role='button' and contains(@class,'cursor-pointer')]"));
    private static final Locator LOGOUT_BUTTON = new Locator("LOGOUT_BUTTON", By.xpath("//button[.//span[normalize-space()='Log out']]"));
    private static final Locator FORGOT_PASSWORD_LINK = new Locator("FORGOT_PASSWORD_LINK",By.xpath("//div[contains(text(),'Forgot your password')]"));

    private static final Locator SUBMIT_BUTTON          = new Locator("SUBMIT_BUTTON",          By.xpath("//button[@data-testid='btn-submit-login']"));
    private static final Locator NEW_PASSWORD_INPUT      = new Locator("NEW_PASSWORD_INPUT",      By.xpath("//input[@data-testid='password']"));
    private static final Locator CONFIRM_PASSWORD_INPUT  = new Locator("CONFIRM_PASSWORD_INPUT",  By.xpath("//input[@data-testid='confirmPassword']"));

    public LoginPage(Session<T> session) {
        super(session);
    }

    private String getBaseUrl() {
        return switch (Configs.ENVIRONMENT) {
            case PROD    -> SUPPLY_DASHBOARD_URL_PROD;
            case PREPROD -> SUPPLY_DASHBOARD_URL_PREPROD;
            case GK      -> SUPPLY_DASHBOARD_URL_GK;
        };
    }

    public void goToLoginPage() {
        driverTools.getWithDynamicTimeout(getBaseUrl() + "/login", 60);
        driverTools.maximizeWindow();
    }

    public boolean isLoginPageLoaded() {
        return driverTools.waitForElementToAppear(EMAIL_INPUT, 15).isPresent();
    }

    public void enterCredentials(String email, String password) {
        WebElement emailInput = driverTools.waitForElementToAppear(EMAIL_INPUT, 10).orElseThrow();
        driverTools.sendKeys(emailInput, email);
        WebElement passwordInput = driverTools.waitForElementToAppear(PASSWORD_INPUT, 10).orElseThrow();
        driverTools.sendKeys(passwordInput, password);
    }

    public void clickLogin() {
        WebElement loginButton = driverTools.waitForElementToBeClickable(LOGIN_BUTTON, 10).orElseThrow();
        driverTools.click(loginButton);
    }

    public void enterOTP(String otp) {
        driverTools.waitForElementToAppear(OTP_TEXTFIELD1, 10);
        if (driverTools.isElementDisplayed(OTP_TEXTFIELD1)) {
            driverTools.waitForElementToBeClickable(OTP_TEXTFIELD1).orElseThrow();
            driverTools.sendKeys(OTP_TEXTFIELD1, otp.substring(0, 1));

            driverTools.waitForElementToBeClickable(OTP_TEXTFIELD2, 3).orElseThrow();
            driverTools.sendKeys(OTP_TEXTFIELD2, otp.substring(1, 2));

            driverTools.waitForElementToBeClickable(OTP_TEXTFIELD3, 3).orElseThrow();
            driverTools.sendKeys(OTP_TEXTFIELD3, otp.substring(2, 3));

            driverTools.waitForElementToBeClickable(OTP_TEXTFIELD4, 3).orElseThrow();
            driverTools.sendKeys(OTP_TEXTFIELD4, otp.substring(3, 4));
        } else {
            logger.log("[OTP] OTP_TEXTFIELD1 not displayed — skipping OTP entry");
        }
    }

    public boolean isLoginSuccessful() {
        return driverTools.waitForElementToAppear(DASHBOARD_INDICATOR, 30).isPresent();
    }

    public void login(String email, String password, String otp) {
        goToLoginPage();
        Assertion.assertThat("Supplier Dashboard login page is loaded", isLoginPageLoaded(), is(true));
        enterCredentials(email, password);
        clickLogin();
        enterOTP(otp);
        Assertion.assertThat("User is logged in to Supplier Dashboard", isLoginSuccessful(), is(true));
    }

    public void clickForgotPassword() {
        driverTools.waitForElementToBeClickable(FORGOT_PASSWORD_LINK, 10).orElseThrow();
        driverTools.click(FORGOT_PASSWORD_LINK);
    }

    public void resetPassword(String email, String otp, String newPassword) {
        clickForgotPassword();

        WebElement emailField = driverTools.waitForElementToBeClickable(EMAIL_INPUT, 10).orElseThrow();
        driverTools.sendKeys(emailField, email);

        driverTools.waitForElementToBeClickable(SUBMIT_BUTTON, 10).orElseThrow();
        driverTools.click(SUBMIT_BUTTON);

        enterOTP(otp);

        WebElement newPwdField = driverTools.waitForElementToBeClickable(NEW_PASSWORD_INPUT, 10).orElseThrow();
        driverTools.sendKeys(newPwdField, newPassword);

        WebElement confirmPwdField = driverTools.waitForElementToBeClickable(CONFIRM_PASSWORD_INPUT, 10).orElseThrow();
        driverTools.sendKeys(confirmPwdField, newPassword);
        
        driverTools.waitForElementToBeClickable(SUBMIT_BUTTON, 10).orElseThrow();
        driverTools.click(SUBMIT_BUTTON);
    }

    public boolean isBackOnLoginPage() {
        return driverTools.waitForElementToAppear(EMAIL_INPUT, 15).isPresent()
                && driverTools.isElementPresent(LOGIN_BUTTON);
    }

    public void logout() {
        driverTools.waitForElementToBeClickable(USER_AVATAR, 10).orElseThrow();
        driverTools.click(USER_AVATAR);
        driverTools.waitForElementToBeClickable(LOGOUT_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(LOGOUT_BUTTON);
    }

    public boolean isLoggedOut() {
        return driverTools.waitForElementToAppear(LOGIN_BUTTON, 15).isPresent();
    }
}
