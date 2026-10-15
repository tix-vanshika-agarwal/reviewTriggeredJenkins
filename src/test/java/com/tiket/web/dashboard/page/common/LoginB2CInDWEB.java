package com.tiket.web.dashboard.page.common;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.web.page.common.home.HomePageWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import javax.security.auth.login.FailedLoginException;

public class LoginB2CInDWEB<T extends WebDriver> extends BasePage<T> {
    private static final Locator LOGINFORM_BUTTON = new Locator("LOGINFORM_BUTTON", new By.ByXPath("//button[contains(@class, 'cta')]"));
    private static final Locator EMAIL_TEXTFIELD = new Locator("EMAIL_TEXTFIELD", new By.ByXPath("//input[contains(@id, 'email')]"));
    private static final Locator PASSWORD_TEXTFIELD = new Locator("PASSWORD_TEXTFIELD", new By.ByXPath("//input[contains(@name, 'password')]"));
    private static final Locator OTP_TEXTFIELD1 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("(//input[contains(@class, 'otp_input')])[1]"));
    private static final Locator OTP_TEXTFIELD2 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("(//input[contains(@class, 'otp_input')])[2]"));
    private static final Locator OTP_TEXTFIELD3 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("(//input[contains(@class, 'otp_input')])[3]"));
    private static final Locator OTP_TEXTFIELD4 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("(//input[contains(@class, 'otp_input')])[4]"));
    private static final Locator OTP_TEXTFIELD5 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("(//input[contains(@class, 'otp_input')])[5]"));
    private static final Locator OTP_TEXTFIELD6 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("(//input[contains(@class, 'otp_input')])[6]"));
    private static final Locator CORPORATE_LOGIN_INDICATOR = new Locator("CORPORATE_LOGIN_INDICATOR", new By.ByXPath("//span[normalize-space()='Company Dashboard']"));
    private static final Locator SKIP_ONBOARDING_BUTTON = new Locator("SKIP_ONBOARDING_BUTTON", new By.ByXPath("//button[contains(text(), 'Skip')]"));
    private static final Locator USER_MODAL = new Locator("USER_MODAL", new By.ByXPath("//button[contains(@class, 'UserModal')]"));
    private static final Locator BOOKING_LIST_SIDEBAR_ITEM = new Locator("BOOKING_LIST_SIDEBAR_ITEM", new By.ByXPath("//*[contains(text(), 'Booking List')]"));
    private static final Locator REGISTER_BUTTON_LOGIN_PAGE = new Locator("REGISTER_BUTTON_LOGIN_PAGE", new By.ByXPath("//button[@type='submit' and @data-testid='btnSubmitForm' and contains(text(),'Register')]"));
    private static final Locator SKIP_PASSKEY = new Locator("SKIP_PASSKEY", new By.ByXPath("//span[contains(@class, 'Setup_skip_cta')]"));
    private static final Locator SKIP_FOR_NOW_BUTTON = new Locator("SKIP_FOR_NOW_BUTTON", new By.ByXPath("//span[text()='Skip for now']"));
    private static final Locator LOGIN_V2 = new Locator("LOGIN_V2", By.xpath("//span[contains(@class, 'SetupV2_button') and (contains(text(), 'nomor HP atau email') or contains(text(), 'Phone or Email'))]"));

    public LoginB2CInDWEB(Session<T> session) {
        super(session);
    }

    public void inputEmail(String email) {
        driverTools.waitForElementToAppear(LOGIN_V2, 2).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(EMAIL_TEXTFIELD);
        driverTools.clickWithJavascriptExecutor(EMAIL_TEXTFIELD);
        driverTools.sendKeys(EMAIL_TEXTFIELD, email);
        driverTools.waitForElementToAppear(LOGINFORM_BUTTON);
        driverTools.clickWithJavascriptExecutor(LOGINFORM_BUTTON);
    }

    public void inputPassword(String password) {
        driverTools.waitForElementToAppear(PASSWORD_TEXTFIELD);
        driverTools.sendKeys(PASSWORD_TEXTFIELD, password);
        driverTools.waitForElementToAppear(LOGINFORM_BUTTON);
        driverTools.click(LOGINFORM_BUTTON);

        driverTools.waitForElementToAppear(SKIP_PASSKEY,5);
        if(driverTools.isElementPresent(SKIP_PASSKEY)){
            driverTools.clickWithJavascriptExecutor(SKIP_PASSKEY);
        }
    }

    public void inputOTPCode(String otp, int retryCounter) throws InterruptedException, FailedLoginException {
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        if (retryCounter > 3) {
            logger.log("Tried logging in three times, but failed");
            throw new FailedLoginException();
        }
        driverTools.waitForElementToAppear(OTP_TEXTFIELD1, 5);
        if (driverTools.isElementDisplayed(OTP_TEXTFIELD1)) {
            driverTools.waitForElementToBeClickable(OTP_TEXTFIELD1, 3).orElseThrow();
            driverTools.sendKeys(OTP_TEXTFIELD1, otp.substring(0,1));
            driverTools.sendKeys(OTP_TEXTFIELD2, otp.substring(1,2));
            driverTools.sendKeys(OTP_TEXTFIELD3, otp.substring(2,3));
            driverTools.sendKeys(OTP_TEXTFIELD4, otp.substring(3,4));
            driverTools.sendKeys(OTP_TEXTFIELD5, otp.substring(4,5));
            driverTools.sendKeys(OTP_TEXTFIELD6, otp.substring(5));

            driverTools.waitForElementToAppear(SKIP_PASSKEY,5);
            if(driverTools.isElementPresent(SKIP_PASSKEY)){
                driverTools.clickWithJavascriptExecutor(SKIP_PASSKEY);
            }

        } else if (driverTools.isElementDisplayed(BOOKING_LIST_SIDEBAR_ITEM)) {
            logger.log("User is already logged in. No OTP needed");}
        else if (homePage.isHomePageOpened()) {
            logger.log("User is already logged in. No OTP needed");
        } else {
            retryCounter++;
            Thread.sleep(30000);
            driverTools.click(LOGINFORM_BUTTON);
            inputOTPCode(otp, retryCounter);
        }
    }

    public void inputOTPCodeWithoutRetry(String otp) {
        driverTools.waitForElementToAppear(OTP_TEXTFIELD1, 5);
        if (driverTools.isElementDisplayed(OTP_TEXTFIELD1)) {
            logger.log("OTP needed");
            driverTools.waitForElementToBeClickable(OTP_TEXTFIELD1, 3).orElseThrow();
            driverTools.sendKeys(OTP_TEXTFIELD1, otp.substring(0,1));
            driverTools.sendKeys(OTP_TEXTFIELD2, otp.substring(1,2));
            driverTools.sendKeys(OTP_TEXTFIELD3, otp.substring(2,3));
            driverTools.sendKeys(OTP_TEXTFIELD4, otp.substring(3,4));
            driverTools.sendKeys(OTP_TEXTFIELD5, otp.substring(4,5));
            driverTools.sendKeys(OTP_TEXTFIELD6, otp.substring(5));
        } else {
            logger.log("No OTP needed");
        }
    }

    public boolean checkIfLoggedIn(){
        driverTools.waitForElementToAppear(CORPORATE_LOGIN_INDICATOR);
        return driverTools.isElementDisplayed(CORPORATE_LOGIN_INDICATOR);
    }

    public boolean checkIfUserInB2CPage(){
        driverTools.waitForElementToAppear(USER_MODAL);
        return driverTools.isElementDisplayed(USER_MODAL);
    }

    public void closeTutorialbanner(){
        boolean isTutorialBannerAppear = driverTools.isElementDisplayed(SKIP_ONBOARDING_BUTTON);
        if (isTutorialBannerAppear) {
            driverTools.waitForElementToAppear(SKIP_ONBOARDING_BUTTON);
            driverTools.click(SKIP_ONBOARDING_BUTTON);
        }
    }

    public void inputEmailToRegister(String email) {
        driverTools.waitForElementToAppear(EMAIL_TEXTFIELD);
        driverTools.click(EMAIL_TEXTFIELD);
        driverTools.sendKeys(EMAIL_TEXTFIELD, email);
        driverTools.waitForElementToAppear(REGISTER_BUTTON_LOGIN_PAGE);
        driverTools.click(REGISTER_BUTTON_LOGIN_PAGE);
    }

    public void clickSkipForNowButton(){
        driverTools.dummyWait(2);
        boolean isSkipForNowButtonAppear = driverTools.isElementPresent(SKIP_FOR_NOW_BUTTON);
        if (!isSkipForNowButtonAppear) {
            logger.log("Skip for now button not appear");
            return;
        }
        driverTools.waitForElementToAppear(SKIP_FOR_NOW_BUTTON);
        driverTools.click(SKIP_FOR_NOW_BUTTON);
    }

}