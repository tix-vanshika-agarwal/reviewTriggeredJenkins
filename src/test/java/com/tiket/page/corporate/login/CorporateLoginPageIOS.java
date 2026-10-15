package com.tiket.page.corporate.login;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.login.LoginPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CorporateLoginPageIOS<T extends WebDriver> extends CorporateLoginPage<T> {

    public CorporateLoginPageIOS(Session<T> session) {
        super(session);
    }

    private static final Locator CORPORATE_LOGIN_TITLE = new Locator("CORPORATE_LOGIN_TITLE", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == 'Welcome to tiket.com for Corporates'`]"));
    private static final Locator EMAIL_FIELD = new Locator("EMAIL_FIELD", AppiumBy.name("username"));
    private static final Locator PASSWORD_FIELD = new Locator("PASSWORD_FIELD", AppiumBy.name("password"));
    private static final Locator FORGOT_PASSWORD_BUTTON = new Locator("FORGOT_PASSWORD_BUTTON", AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label CONTAINS 'Lupa Kata Sandi' OR label CONTAINS 'Forgot Your Password'`]"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label == 'Lanjutkan' OR label == 'Continue'`]"));
    private static final Locator TITLE_ERROR_MESSAGE = new Locator("TITLE_ERROR_MESSAGE", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == 'Email ini tidak terdaftar di tiket.com for Corporates' OR label == 'This email is not registered to tiket.com for Corporates'`]"));
    private static final Locator CHANGE_EMAIL_BUTTON = new Locator("CHANGE_EMAIL_BUTTON", AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label == 'Ganti Email' OR label == 'Change Email'`]"));
    private static final Locator FORGOT_PASSWORD= new Locator("FORGOT_PASSWORD",AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Lupa Kata Sandi') or contains(@name,'Forgot Your Password')]"));
    private static final Locator FORGOT_PASSWORD_FIELD= new Locator("FORGOT_PASSWORD_FIELD",AppiumBy.accessibilityId("identifier"));
    private static final Locator FORGOT_PASSWORD_BTN= new Locator("FORGOT_PASSWORD_BTN",AppiumBy.accessibilityId("send_forget_password"));
    private static final Locator CONFIRM_FORGOT_PASSWORD_BTN= new Locator("CONFIRM_FORGOT_PASSWORD_BTN",AppiumBy.xpath("(//XCUIElementTypeButton)[last()]"));
    private static final Locator NEW_PASSWORD_FIELD= new Locator("NEW_PASSWORD_FIELD",AppiumBy.id("password"));

    @Override
    public boolean isCorporateLoginPageVisible() {

        boolean corporateLoginTitle = driverTools.waitForElementToPresence(CORPORATE_LOGIN_TITLE).isPresent();
        boolean emailField = driverTools.waitForElementToPresence(EMAIL_FIELD).isPresent();
        boolean passwordField = driverTools.waitForElementToPresence(PASSWORD_FIELD).isPresent();
        boolean forgotPasswordButton = driverTools.waitForElementToPresence(FORGOT_PASSWORD_BUTTON).isPresent();
        boolean continueButton = driverTools.waitForElementToPresence(CONTINUE_BUTTON).isPresent();

        boolean result = corporateLoginTitle && emailField && passwordField && forgotPasswordButton && continueButton;
        if (!result) logger.softWarning("There is an element that not visible" +
                "\n* corporateLoginTitle -> " + corporateLoginTitle +
                "\n* emailField -> " + emailField +
                "\n* passwordField -> " + passwordField +
                "\n* forgotPasswordButton -> " + forgotPasswordButton +
                "\n* continueButton -> " + continueButton);
        return result;
    }

    @Override
    public String getEmailValue() {
        return driverTools.getValueAttribute(EMAIL_FIELD, "value");
    }

    @Override
    public void inputEmail(String email) {
        driverTools.sendKeys(EMAIL_FIELD, email);
        driverTools.hideKeyboard(1);
    }

    @Override
    public void inputPassword(String password) {
        driverTools.sendKeys(PASSWORD_FIELD, password);
        driverTools.hideKeyboard(1);
    }

    @Override
    public void clickContinueButton() {
        WebElement element = driverTools.waitForElementToPresence(CONTINUE_BUTTON).orElseThrow();
        if (element.isDisplayed()) driverTools.click(element);
        else logger.softWarning("Continue button is not displayed");
    }

    @Override
    public boolean isUnregisteredMessageVisible() {
        boolean titleErrorMessage = driverTools.waitForElementToPresence(TITLE_ERROR_MESSAGE).isPresent();
        boolean changeEmailButton = driverTools.waitForElementToPresence(CHANGE_EMAIL_BUTTON).isPresent();

        boolean result = titleErrorMessage && changeEmailButton;
        if (!result) logger.softWarning("There is an element that not visible" +
                "\n* corporateLoginTitle -> " + titleErrorMessage +
                "\n* emailField -> " + changeEmailButton);
        return result;
    }

     @Override
     public void forgotPasswordB2B(String username, String password){
         LoginPage<T> loginPage = LoginPage.getInstance(session);
         driverTools.click(FORGOT_PASSWORD);
         driverTools.sendKeys(FORGOT_PASSWORD_FIELD,username);
         driverTools.hideKeyboard(1);
         driverTools.click(FORGOT_PASSWORD_BTN);
         driverTools.waitForElementToAppear(NEW_PASSWORD_FIELD);
         driverTools.sendKeys(NEW_PASSWORD_FIELD,password);
         driverTools.hideKeyboard(1);
         driverTools.click(CONFIRM_FORGOT_PASSWORD_BTN);
     }
}
