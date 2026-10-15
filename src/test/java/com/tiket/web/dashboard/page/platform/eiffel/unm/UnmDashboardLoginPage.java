package com.tiket.web.dashboard.page.platform.eiffel.unm;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.web.WebPage;
import com.tiket.testbase.Configs;
import com.tiket.api.unm.POSTGenerateTokenApi;
import com.tiket.model.api.ApiResponse;
import org.json.JSONObject;

import static org.hamcrest.core.Is.is;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class UnmDashboardLoginPage<T extends WebDriver> extends WebPage<T> {
    public UnmDashboardLoginPage(Session<T> session) {
        super(session);
    }

    private static final String STAGING_BASE_URL = "https://account.test.bliblitiket.dev";
    private static final String SANDBOX_BASE_URL = "https://account.preprod.bliblitiket.dev";
    private static final String PROD_BASE_URL = "https://account.bliblitiket.com";

    private static final String ADMIN_LOGIN_PATH = "/admin/login";
    private static final String SET_PASSWORD_PATH = "/admin/set-password";

    private static final Locator LOG_IN_TEXT = new Locator("LOG_IN_TEXT", By.xpath("//p[contains(normalize-space(),'Log in')]"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", By.cssSelector("#email"));
    private static final Locator PASSWORD_INPUT = new Locator("PASSWORD_INPUT", By.cssSelector("#password"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", By.xpath("//button[normalize-space()='Log In']"));
    private static final Locator SIDEBAR_MENU_CONTAINER = new Locator("SIDEBAR_MENU_CONTAINER", By.xpath("//div[contains(@class, 'styles_desktop_sidebar')]"));
    private static final Locator USER_LOCATOR = new Locator("USER_LOCATOR", By.xpath("//div[contains(@class, 'styles_user_dropdown_button')]"));
    private static final Locator LOGOUT_BUTTON = new Locator("LOGOUT_BUTTON", By.xpath("//p[normalize-space()='Logout']"));

    private static final Locator FORGOT_PASSWORD_LINK = new Locator("FORGOT_PASSWORD_LINK", By.xpath("//a[contains(@href, '/admin/forget-password')]"));
    private static final Locator SEND_RESET_LINK_BTN = new Locator("SEND_RESET_LINK_BTN", By.xpath("//button[contains(., 'Send Reset Password Link')]"));
    private static final Locator SET_PASSWORD_LOGIN_BTN = new Locator("SET_PASSWORD_LOGIN_BTN", By.cssSelector("[data-testid='set-password-button']"));
    private static final Locator CONFIRM_PASSWORD_INPUT = new Locator("CONFIRM_PASSWORD_INPUT", By.xpath("//input[@label='Confirm Password' or @placeholder='Confirm Password']"));
    private static final Locator NEW_PASSWORD_INPUT = new Locator("NEW_PASSWORD_INPUT", By.xpath("//input[@label='Password' or @placeholder='Password']"));
    private static final Locator ERROR_INPUT = new Locator("ERROR_INPUT", By.xpath("//div[contains(@class, 'helper_text_container') and contains(@class, 'state_danger')]"));

    private Locator getSuccessSnackbarText(String expectedSuccessMessage) {
        String escapedMessage = expectedSuccessMessage.replace("'", "\\'");
        return new Locator("SUCCESS_SNACKBAR_TEXT", By.xpath("//*[contains(text(), '" + escapedMessage + "')]"));
    }

    private static Locator getCurrentUserText(String email) {
        return new Locator("CURRENT_USER_TEXT_" + email, By.xpath("//p[contains(normalize-space(),'" + email + "')]"));
    }

    private String getBaseUrl() {
        return switch (Configs.ENVIRONMENT) {
        case GK -> STAGING_BASE_URL;
        case PREPROD -> SANDBOX_BASE_URL;
        case PROD -> PROD_BASE_URL;
        default -> throw new IllegalStateException("Unknown environment: " + Configs.ENVIRONMENT);
        };
    }

    public void goToUnmLoginPage() {
        driverTools.get(getBaseUrl() + ADMIN_LOGIN_PATH);
    }

    public void loginUnmAdmin(String email, String password) {

        if (isLogin()) {
            boolean isCurrentUser = driverTools.waitForElementToAppear(getCurrentUserText(email), 5).isPresent();
            if (isCurrentUser) {
                logger.log("Already login as: " + email);
                return;
            } else {
                logger.log("Current user does not match the expected email");
                logoutUNMAdmin();
            }
        }

        driverTools.waitForElementToAppear(LOG_IN_TEXT, 5);
        driverTools.sendKeys(EMAIL_INPUT, email);
        driverTools.sendKeys(PASSWORD_INPUT, password);
        driverTools.click(LOGIN_BUTTON);

        boolean isCurrentUser = driverTools.waitForElementToAppear(getCurrentUserText(email), 5).isPresent();
        Assertion.assertThat(String.format("Login to UNM Admin with email: %s successful", email), isCurrentUser, is(true));
    }

    public boolean isLogin() {
        return driverTools.waitForElementToAppear(SIDEBAR_MENU_CONTAINER, 5).isPresent();
    }

    public void clickForgotPassword() {
        driverTools.waitForElementToAppear(FORGOT_PASSWORD_LINK, 5);
        driverTools.clickWithJavascriptExecutor(FORGOT_PASSWORD_LINK);
    }

    public void inputEmail(String email) {
        driverTools.waitForElementToAppear(EMAIL_INPUT, 5);
        driverTools.sendKeys(EMAIL_INPUT, email);
    }

    public void clickSendResetPasswordLink() {
        driverTools.waitForElementToAppear(SEND_RESET_LINK_BTN, 5);
        driverTools.clickWithJavascriptExecutor(SEND_RESET_LINK_BTN);
    }

    public void goToSetPasswordPage(String token) {
        driverTools.get(getBaseUrl() + SET_PASSWORD_PATH + "?token=" + token);
    }

    public void verifySetPasswordPageDisplayed() {
        boolean isPasswordInputDisplayed = driverTools.waitForElementToAppear(NEW_PASSWORD_INPUT, 10).isPresent();
        boolean isSetPasswordBtnDisplayed = driverTools.waitForElementToAppear(SET_PASSWORD_LOGIN_BTN, 10).isPresent();

        Assertion.assertThat("Set Password Page is displayed", isPasswordInputDisplayed && isSetPasswordBtnDisplayed, is(true));
    }

    public void setPasswordAndSubmit(String newPassword) {
        String trimmedPassword = newPassword.trim();

        driverTools.waitForElementToAppear(NEW_PASSWORD_INPUT, 10);
        driverTools.click(NEW_PASSWORD_INPUT);
        driverTools.clearInput(NEW_PASSWORD_INPUT);
        driverTools.sendKeys(NEW_PASSWORD_INPUT, trimmedPassword);

        driverTools.click(CONFIRM_PASSWORD_INPUT);
        driverTools.clearInput(CONFIRM_PASSWORD_INPUT);
        driverTools.sendKeys(CONFIRM_PASSWORD_INPUT, trimmedPassword);

        driverTools.sendKeys(CONFIRM_PASSWORD_INPUT, Keys.TAB.toString());
        driverTools.waitForElementToDisappear(ERROR_INPUT, 2);

        driverTools.clickWithJavascriptExecutor(SET_PASSWORD_LOGIN_BTN);
    }

    public void verifyResetPasswordSuccessSnackbar(String expectedSuccessMessage) {
        boolean isSuccessSnackbarDisplayed = driverTools.waitForElementToAppear(getSuccessSnackbarText(expectedSuccessMessage), 10).isPresent();
        Assertion.assertThat("Success reset password snackbar is displayed", isSuccessSnackbarDisplayed, is(true));
    }

    public String getResetPasswordToken(String email) {
        POSTGenerateTokenApi generateTokenApi = new POSTGenerateTokenApi(new POSTGenerateTokenApi.Inputs(email));
        ApiResponse apiResponse = generateTokenApi.hitApi(apiTools);

        if (apiResponse.getStatusCode() != 201) {
            throw new RuntimeException("Failed to generate reset password token. Status: " + apiResponse.getStatusCode());
        }

        JSONObject responseJson = new JSONObject(apiResponse.getBody());
        if (!responseJson.has("data") || !responseJson.getJSONObject("data").has("token")) {
            throw new RuntimeException("Token not found in API response: " + apiResponse.getBody());
        }

        return responseJson.getJSONObject("data").getString("token");
    }

    public void logoutUNMAdmin() {
        if(isLogin()) {
            driverTools.click(USER_LOCATOR);
            driverTools.waitForElementToAppear(LOGOUT_BUTTON, 1).ifPresent(driverTools::click);
            driverTools.waitForElementToAppear(LOG_IN_TEXT, 5);
        } else {
            logger.log("User is not logged in");
        }
    }

}
