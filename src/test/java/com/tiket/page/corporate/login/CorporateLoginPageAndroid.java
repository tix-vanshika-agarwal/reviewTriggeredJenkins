package com.tiket.page.corporate.login;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.login.LoginPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class CorporateLoginPageAndroid<T extends WebDriver> extends CorporateLoginPage<T> {

    public CorporateLoginPageAndroid(Session<T> session) {
        super(session);
    }

    private static final Locator CORPORATE_LOGIN_TITLE = new Locator("CORPORATE_LOGIN_TITLE", AppiumBy.id("tv_title"));
    private static final Locator EMAIL_FIELD = new Locator("EMAIL_FIELD", AppiumBy.xpath("//*[contains(@resource-id,'tf_email')]//*[contains(@resource-id,'tds_box_input_text')]"));
    private static final Locator PASSWORD_FIELD = new Locator("PASSWORD_FIELD", AppiumBy.xpath("//*[contains(@resource-id,'tf_password')]//*[contains(@resource-id,'tds_box_input_text')]"));
    private static final Locator FORGOT_PASSWORD_BUTTON = new Locator("FORGOT_PASSWORD_BUTTON", AppiumBy.id("tv_forgot_password"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.id("btn_submit"));
    private static final Locator TITLE_ERROR_MESSAGE = new Locator("TITLE_ERROR_MESSAGE", AppiumBy.id("tds_txt_header_info_view"));
    private static final Locator CHANGE_EMAIL_BUTTON = new Locator("CHANGE_EMAIL_BUTTON", AppiumBy.id("tds_btn_primary_info_view"));
    private static final Locator FORGOT_PASSWORD= new Locator("FORGOT_PASSWORD",AppiumBy.id("tv_forgot_password"));
    private static final Locator FORGOT_PASSWORD_FIELD= new Locator("FORGOT_PASSWORD_FIELD",AppiumBy.id("tds_box_input_text"));
    private static final Locator FORGOT_PASSWORD_BTN= new Locator("FORGOT_PASSWORD_BTN",AppiumBy.id("tds_button_view"));

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
        return driverTools.getText(EMAIL_FIELD).orElseThrow();
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
        driverTools.click(CONTINUE_BUTTON);
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
     public void forgotPasswordB2B(String username, String password) {
         LoginPage<T> loginPage = LoginPage.getInstance(session);
         driverTools.click(FORGOT_PASSWORD);
         driverTools.sendKeys(FORGOT_PASSWORD_FIELD,username);
         driverTools.hideKeyboard(1);
         driverTools.click(FORGOT_PASSWORD_BTN);
         driverTools.waitForElementToAppear(FORGOT_PASSWORD_FIELD);
         driverTools.sendKeys(FORGOT_PASSWORD_FIELD,password);
         driverTools.hideKeyboard(1);
         driverTools.click(FORGOT_PASSWORD_BTN);
     }
}
