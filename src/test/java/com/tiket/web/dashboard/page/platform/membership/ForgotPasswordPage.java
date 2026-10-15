package com.tiket.web.dashboard.page.platform.membership;

import com.tiket.core.CoreFunctions;
import com.tiket.logging.ILogger;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

public class ForgotPasswordPage<T extends WebDriver> extends BasePage<T> {

    public ForgotPasswordPage(Session<T> session){
        super(session);
    }
    ILogger logger = BaseTest.mainLogger.get();

    private String newPassword;
    public final String ADMIN_MEMBER_URL = "/admin/member/";
    private static final Locator FORGOT_PASSWORD_BUTTON = new Locator("FORGOT_PASSWORD_BUTTON", By.xpath("//div[contains(@class, 'styles_forget_password_container')]//span[contains(text(), 'Forgot Password?')]"));
    private static final Locator FORGOT_PASSWORD_TITLE = new Locator("FORGOT_PASSWORD_TITLE", By.xpath("//div[contains(@class, 'styles_login_layout')]//div[contains(@class, 'styles_right_content')]//div[contains(@class, 'styles_desc')]/h1[contains(text(), 'Forgot Password')]"));
    private static final Locator EMAIL_COLUMN = new Locator("EMAIL_COLUMN", By.id("email"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[text() = 'SUBMIT']"));
    private static final Locator INPUT_OTP_COLUMN = new Locator("INPUT_OTP", By.xpath("//input[contains(@class, 'OtpInput_otp_input')][1]"));
    private static final Locator SET_PASSWORD_TITLE = new Locator("SET_PASSWORD_TITLE", By.xpath("//h1[contains(text(), 'Set Password')]"));
    private static final Locator NEW_PASSWORD_COLUMN = new Locator("NEW_PASSWORD_COLUMN", By.id("new-password"));
    private static final Locator CONFIRM_NEW_PASSWORD_COLUMN = new Locator("CONFIRM_NEW_PASSWORD_COLUMN", By.id("confirm-new-password"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", By.xpath("//button[text()='LOG IN']"));
    private static final Locator LOGIN_LABEL = new Locator("LOGIN_LABEL", By.xpath("//div[contains(@class, 'styles_desc')]//h1[contains(text(), 'Login')]"));
    private static final Locator DASHBOARD_MEMBER_LOGO = new Locator("DASHBOARD_MEMBER_LOGO", By.xpath("//div[contains(@class, 'styles_logo_area')]//span[contains(text(), 'Dashboard Member')]"));
    private static final Locator EMAIL_LABEL  = new Locator("EMAIL_LABEL", By.xpath("//div[contains(@class, 'HeaderAccountInfo_email__6Lr37')]"));

    public void clickForgotPasswordButtonPage(){
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.goToPage(ADMIN_MEMBER_URL);
        driverTools.waitForElementToAppear(FORGOT_PASSWORD_BUTTON,1);
        driverTools.click(FORGOT_PASSWORD_BUTTON);
    }

    public void isForgotTitleExist(){
        driverTools.waitForElementToAppear(FORGOT_PASSWORD_TITLE,2);
        logger.log("Is forgot title exist :" + driverTools.isElementDisplayed(FORGOT_PASSWORD_TITLE));
        final boolean isForgotPasswordTitleExist = driverTools.isElementDisplayed(FORGOT_PASSWORD_TITLE);
        Assertion.assertThat("Verify Forgot Password Title Appear", isForgotPasswordTitleExist, is(true));
    }

    public void inputEmailOTP(String username, int OTP){
        driverTools.click(EMAIL_COLUMN);
        driverTools.sendKeys(EMAIL_COLUMN, username);
        driverTools.click(SUBMIT_BUTTON);
        driverTools.waitForElementToAppear(INPUT_OTP_COLUMN, 2);
        driverTools.sendKeys(INPUT_OTP_COLUMN, String.valueOf(OTP));
        logger.log("Is OTP columns displayed : " + driverTools.isElementDisplayed(INPUT_OTP_COLUMN));
        logger.log("Is field enabled : "+driverTools.isElementEnabled(INPUT_OTP_COLUMN));
    }

    public void isSetNewPasswordTitleExist(){
        driverTools.waitForElementToAppear(SET_PASSWORD_TITLE,2);
        logger.log("Is set password title exist :" + driverTools.isElementDisplayed(SET_PASSWORD_TITLE));
        final boolean isSetNewPasswordAppear = driverTools.isElementDisplayed(SET_PASSWORD_TITLE);
        Assertion.assertThat("Verify Set New Password Title Appear", isSetNewPasswordAppear, is(true));
    }

    public void inputNewPassword(){
        String randomString = CoreFunctions.generateRandomString(4);
        int randomNumber = (int) CoreFunctions.generateRandomInt(2);
        String uniqueNewPassword = "Automation-" + randomString + randomNumber;

        driverTools.waitForElementToAppear(NEW_PASSWORD_COLUMN,1);
        driverTools.click(NEW_PASSWORD_COLUMN);
        driverTools.sendKeys(NEW_PASSWORD_COLUMN, uniqueNewPassword);
        driverTools.click(CONFIRM_NEW_PASSWORD_COLUMN);
        driverTools.sendKeys(CONFIRM_NEW_PASSWORD_COLUMN, uniqueNewPassword);
        driverTools.click(SUBMIT_BUTTON);
        driverTools.waitForElementToAppear(LOGIN_BUTTON,1);
        driverTools.click(LOGIN_BUTTON);

        this.newPassword = uniqueNewPassword;
    }

    public void goToLoginPage(){
        driverTools.waitForElementToAppear(LOGIN_LABEL,2);
        final boolean isLoginPageAppear = driverTools.isElementDisplayed(LOGIN_LABEL);
        Assertion.assertThat("Verify Login Label Appear", isLoginPageAppear, is(true));
    }

    public void inputUsernameNewPassword(String username, String uniqueNewPassword) throws InterruptedException{
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.isMemberLoginPageLoaded();
        memberLoginPage.adminLogin(username, this.newPassword);
        memberLoginPage.clickOnButton();
        logger.log("Current URL :" + driver.getCurrentUrl());
        logger.log("password : "+ this.newPassword);
    }

    public void verifyLoggedIn(){
        driverTools.waitForElementToAppear(DASHBOARD_MEMBER_LOGO,1);
        final boolean dashboardMemberLogo = driverTools.isElementDisplayed(DASHBOARD_MEMBER_LOGO);
        final String emailLabel = driverTools.getText(EMAIL_LABEL).orElse("");

        Assertion.assertThat("Verify Dashboard Logo Appears", dashboardMemberLogo, is(true));
        Assertion.assertThat("Verify Account Email is Correct", emailLabel, is(sheetData.get("username")));
    }

    public String getNewPassword(){
        return newPassword;
    }

    @Setter @Getter
    Map<String, String> sheetData = new HashMap<>();
}