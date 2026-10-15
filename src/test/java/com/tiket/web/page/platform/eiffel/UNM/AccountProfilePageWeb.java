package com.tiket.web.page.platform.eiffel.UNM;

import com.tiket.api.unm.POSTOtpGenerateUNMApi;
import com.tiket.api.unm.POSTOtpVerifyUNMApi;
import com.tiket.api.unm.PUTV1SecurityEmail;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.Setting_Page.SettingsPageWeb;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import com.tiket.model.Deeplink;
import com.tiket.model.Environment;
import com.tiket.testbase.Assertion;
import com.tiket.web.dashboard.page.common.LoginB2CInDWEB;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;

import static org.hamcrest.core.Is.is;
import javax.security.auth.login.FailedLoginException;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public abstract class AccountProfilePageWeb<T extends WebDriver> extends WebPage<T> {
    
    public AccountProfilePageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> AccountProfilePageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new AccountProfilePageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new AccountProfilePageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    
    private static final Locator EDIT_NAME_PENCIL_ICON = new Locator("EDIT_NAME_PENCIL_ICON", new By.ByXPath("//button[contains(@class,'styles_clickable_icon')]"));
    private static final Locator ACCOUNT_FIRST_NAME = new Locator("ACCOUNT_FIRST_NAME", new By.ByXPath("//input[@data-testid='text-dialog-input']"));
    private static final Locator SAVE_BUTTON_INPUT = new Locator("SAVE_BUTTON", new By.ByXPath("//button[@data-testid='text-dialog-submit']")) ;
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("(//button[text()='Simpan' or text()='Save'])[2]")) ;
    private static final Locator UNDERSTAND_BUTTON = new Locator("UNDERSTAND_BUTTON", new By.ByXPath("//button[text()='Mengerti' or text()='Got it']")) ;
    private static final Locator FULL_NAME_VALUE = new Locator("FULL_NAME_VALUE", new By.ByXPath("//input[@value]")) ;
    private static final Locator CHANGE_EMAIL = new Locator("CHANGE_EMAIL", new By.ByXPath("(//span[text()='Change' or text()='Ubah'])[2]"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", new By.ByXPath("//button[contains(., 'Continue') or contains(., 'Lanjutkan')]")) ;
    private static final Locator INPUT_EMAIL = new Locator("INPUT_EMAIL", new By.ByXPath("//input[@id='email']")) ;
    private static final Locator VERIFY_EMAIL = new Locator("VERIFY_EMAIL", new By.ByXPath("//button[@data-testid='btnSubmitChangeEmail']"));
    private static final Locator VERIFY_EMAIL_CTA = new Locator("VERIFY_EMAIL_CTA", new By.ByXPath("//*[self::button or self::a or self::span][normalize-space()='Verify' or normalize-space()='Verifikasi' or normalize-space()='Verify email' or normalize-space()='Verifikasi email']"));
    private static final Locator VERIFY_EMAIL_CTA_2 = new Locator("VERIFY_EMAIL_CTA_2", new By.ByXPath("//*[@data-testid='btnSubmitChangeEmail']"));
    private static final Locator EMAIL_VERIFIED_BADGE = new Locator("EMAIL_VERIFIED_BADGE", new By.ByXPath("//*[(text()='Email')]//following-sibling::*//*[contains(text(),'Verified') or contains(text(), 'diverifikasi')]"));
    private static final Locator EMAIL_VALUE = new Locator("EMAIL_VALUE", new By.ByXPath("(//div[contains(@class, 'ContactPreview_content_left')])[2]//span[contains(@class, 'text')]")) ;
    private static final Locator OTP_TEXTFIELD1 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("(//input[contains(@class, 'otp_input')])[1]"));
    private static final Locator OTP_TEXTFIELD2 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("(//input[contains(@class, 'otp_input')])[2]"));
    private static final Locator OTP_TEXTFIELD3 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("(//input[contains(@class, 'otp_input')])[3]"));
    private static final Locator OTP_TEXTFIELD4 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("(//input[contains(@class, 'otp_input')])[4]"));
    private static final Locator OTP_TEXTFIELD5 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("(//input[contains(@class, 'otp_input')])[5]"));
    private static final Locator OTP_TEXTFIELD6 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("(//input[contains(@class, 'otp_input')])[6]"));
    private static final Locator SNACKBAR = new Locator("SNACKBAR", new By.ByXPath("//div[contains(@class,'Snackbar_text')]")) ;
    private static final Locator CHANGE_PN = new Locator("CHANGE_PN", new By.ByXPath("(//span[text()='Change' or text()='Ubah'])[1]")) ;
    // The account center localises input ids: phone-number (en) / nomor-hp (id).
    private static final Locator INPUT_PN = new Locator("INPUT_PN", new By.ByXPath("//input[@id='phone-number' or @id='nomor-hp']")) ;
    // The label sits in a nested span, so text() never matches it.
    private static final Locator VERIFY_PN = new Locator("VERIFY_PN", new By.ByXPath("//button[normalize-space(.)='Verify phone number' or normalize-space(.)='Verifikasi nomor HP']")) ;
    private static final Locator SNACKBAR_CHANGE_PN = new Locator("SNACKBAR_CHANGE_PN", new By.ByXPath("//div[text()='Nomor HP-mu berhasil diverifikasi.' or text()='Your phone number successfully changed.']")) ;
    private static final Locator PN_VALUE = new Locator("PN_VALUE", new By.ByXPath("(//div[contains(@class, 'ContactPreview_content_left')])[1]//span[contains(@class, 'text')]")) ;
    private static final Locator DOB_LOCATOR = new Locator("DOB_LOCATOR", new By.ByXPath("//input[@data-testid = 'change-date-of-birth-dropdown-form-display']"));
    private static final Locator DELETE_BTN = new Locator("DELETE_BTN", new By.ByXPath("//p[text()=\"Delete account\" or text()=\"Hapus akun\"]"));
    private static final Locator TNC_BTN = new Locator("TNC_BTN", new By.ByXPath("//*[contains(@class,'custom_checkbox')]"));
    private static final Locator CONTINUE_DELETE_BTN = new Locator("CONTINUE_DELETE_BTN", new By.ByXPath("//*[@data-testid='btnContinueDeletion']"));
    private static final Locator REASON_BTN = new Locator("REASON_BTN", new By.ByXPath("//*[@data-testid='ddSelectDeletionReason']"));
    private static final Locator REASON_CHECKBOX = new Locator("REASON_CHECKBOX", new By.ByXPath("(//*[contains(@class,'Radio_checkbox')])[1]"));
    private static final Locator REASON_SUBMIT = new Locator("REASON_SUBMIT", new By.ByXPath("//button[normalize-space(.)=\"Save\" or normalize-space(.)=\"Simpan\"]"));
    private static final Locator CONFIRM_DELETE_BTN = new Locator("CONFIRM_DELETE_BTN", new By.ByXPath("//*[contains(@class,'Button_variant_primary')]"));
    private static final Locator SEE_YOU_BTN = new Locator("SEE_YOU_BTN", new By.ByXPath("//*[contains(@class,'Dialog_body')]//button"));
    private static final Locator GENDER_LOCATOR = new Locator("GENDER_LOCATOR", new By.ByXPath("//input[contains(@data-testid,'change-gender-dropdown')]"));
    private static final Locator CLICK_SAVE_GENDER = new Locator("CLICK_SAVE_GENDER", new By.ByXPath("//button[@data-testid='select-radio-submit']"));
    private static final Locator DOB_SUBMIT = new Locator("DOB_SUBMIT", new By.ByXPath("//button[normalize-space(.)=\"Save\" or normalize-space(.)=\"Simpan\"]"));
    private static final Locator CONFIRM_SAVE_NAME = new Locator("CONFIRM_SAVE_NAME", new By.ByXPath("(//*[contains(@class,'Button_variant_primary')])[2]"));

    public void clickEditNameIcon() {
        driverTools.waitForElementToAppear(EDIT_NAME_PENCIL_ICON, 3) ;
        driverTools.isElementDisplayed(EDIT_NAME_PENCIL_ICON);
        driverTools.clickWithJavascriptExecutor(EDIT_NAME_PENCIL_ICON);
    }

    public void inputnewName() {
        String name = CoreFunctions.randomName();
        driverTools.waitForElementToAppear(ACCOUNT_FIRST_NAME);
        driverTools.deleteUsingBackspace(ACCOUNT_FIRST_NAME);
        driverTools.sendKeys(ACCOUNT_FIRST_NAME, name);
        clickSaveButtonInput();
    }

    public void clickSaveButtonInput() {
        driverTools.waitForElementToAppear(SAVE_BUTTON_INPUT) ;
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON_INPUT) ;
    }

    public void clickSaveButton() {
        driverTools.waitForElementToAppear(SAVE_BUTTON) ;
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON) ;
    }

    public void editName() {
        clickEditNameIcon();
        inputnewName();
        clickSaveButton();
    }

    public boolean verifyName() {
        if (driverTools.waitForElementToAppear(UNDERSTAND_BUTTON).isPresent()) {
            driverTools.click(UNDERSTAND_BUTTON) ;
            Assertion.assertThat("Can only update name once in 90 days", true, is(true));
            return true ;
        }

        driverTools.waitForElementToAppear(FULL_NAME_VALUE);
        logger.log("FULL NAME ->"+ FULL_NAME_VALUE) ;
        boolean isDisplayed = driverTools.isElementDisplayed(FULL_NAME_VALUE);
        logger.log("Is fullName element displayed: " + isDisplayed);
        return isDisplayed;
    }

    public void clickChangeEmail() {
        driverTools.waitForElementToPresence(CHANGE_EMAIL) ;
        driverTools.clickWithJavascriptExecutor(CHANGE_EMAIL) ;
    }

    public void clickContinueButton() {
        driverTools.waitForElementToPresence(CONTINUE_BUTTON) ;
        driverTools.click(CONTINUE_BUTTON) ;
    }

    public void inputRandomMail(String email) {
        driverTools.waitForElementToAppear(INPUT_EMAIL);
        driverTools.clickWithJavascriptExecutor(INPUT_EMAIL);
        driverTools.sendKeys(INPUT_EMAIL, email);
        driverTools.waitForElementToAppear(VERIFY_EMAIL);
        driverTools.clickWithJavascriptExecutor(VERIFY_EMAIL);
    }

    public void changeEmail(String email, String otp) {
        clickChangeEmail();
        clickContinueButton();
        inputOTPCode(otp, 1);
        inputRandomMail(email);
        inputOTPCode(otp, 1);
        waitsnackBar() ;
    }

    public void inputOTPCode(String otp, int retryCounter) {
        driverTools.waitForElementToAppear(OTP_TEXTFIELD1, 5);
        driverTools.waitForElementToBeClickable(OTP_TEXTFIELD1, 3).orElseThrow();
        driverTools.sendKeys(OTP_TEXTFIELD1, otp.substring(0,1));
        driverTools.sendKeys(OTP_TEXTFIELD2, otp.substring(1,2));
        driverTools.sendKeys(OTP_TEXTFIELD3, otp.substring(2,3));
        driverTools.sendKeys(OTP_TEXTFIELD4, otp.substring(3,4));
        driverTools.sendKeys(OTP_TEXTFIELD5, otp.substring(4,5));
        driverTools.sendKeys(OTP_TEXTFIELD6, otp.substring(5));
    }

    public Boolean verifyEmail(String expectedEmail) {
        driverTools.waitForElementToAppear(EMAIL_VALUE,5) ;
        String actualEmail = driverTools.getText(EMAIL_VALUE).orElseThrow() ;
        logger.log("Actual Email -> " + actualEmail) ;
        logger.log("Expected email -> " + expectedEmail) ;
        Boolean value = expectedEmail.equalsIgnoreCase(actualEmail) ;
        return value ;
    }

    private void waitsnackBar() {
        driverTools.waitForElementToAppear(SNACKBAR) ;
        String text = driverTools.getText(SNACKBAR).orElseThrow() ;
        Assertion.assertThat("Your email changed successfully", text, anyOf(containsStringIgnoringCase("successfully changed"),containsStringIgnoringCase("berhasil diganti")));
    }

    public void clickVerifyEmailOnEmailSection() {
        driverTools.waitForElementToAppear(VERIFY_EMAIL_CTA, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(VERIFY_EMAIL_CTA);
        driverTools.waitForElementToAppear(VERIFY_EMAIL_CTA_2, 5).orElseThrow();
        driverTools.clickWithJavascriptExecutor(VERIFY_EMAIL_CTA_2);
    }

    public void followEmailVerificationFlow(String otp) {
        driverTools.waitForElementToAppear(CONTINUE_BUTTON, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
        inputOTPCode(otp, 1);
    }

    public boolean isEmailVerified() {
        boolean badge = driverTools.waitForElementToAppear(EMAIL_VERIFIED_BADGE, 10).isPresent();
        logger.log("Verified badge found; verify badge -> " + badge);
        return badge;
    }

    public void changePN(String PN, String otp) {
        clickChangePN();
        inputOTPCode(otp, 1);
        inputRandomPN(PN);
        inputOTPCode(otp, 1);
        waitsnackBarForChangePN() ;
    }

    public void clickChangePN() {
        driverTools.waitForElementToPresence(CHANGE_PN) ;
        driverTools.clickWithJavascriptExecutor(CHANGE_PN) ;
    }

    public void inputRandomPN(String PN) {
        driverTools.waitForElementToAppear(INPUT_PN);
        driverTools.clickWithJavascriptExecutor(INPUT_PN);
        driverTools.clearWithJsExecutor(INPUT_PN);
        driverTools.sendKeys(INPUT_PN, PN);
        driverTools.waitForElementToAppear(VERIFY_PN);
        driverTools.clickWithJavascriptExecutor(VERIFY_PN);
    }

    private void waitsnackBarForChangePN() {
        driverTools.waitForElementToAppear(SNACKBAR_CHANGE_PN) ;
        String text = driverTools.getText(SNACKBAR_CHANGE_PN).orElseThrow() ;
        Assertion.assertThat("Your PN changed successfully", text, anyOf(containsString("successfully changed"),containsString("diverifikasi")));
    }

    public Boolean verifyPN(String expectedPN) {
        driverTools.waitForElementToAppear(PN_VALUE,5) ;
        driverTools.waitForTextToBePresent(PN_VALUE, expectedPN) ;
        String actualPN = driverTools.getText(PN_VALUE).orElseThrow() ;
        actualPN = actualPN.substring(4);
        logger.log("Actual PN -> " + actualPN) ;
        logger.log("Expected PN -> " + expectedPN) ;
        Boolean value = expectedPN.equalsIgnoreCase(actualPN) ;
        return value ;
    }

    public String getCurrentUNMAccessToken() {
        Set<Cookie> allCookies = driver.manage().getCookies();
        Cookie unmCookie = allCookies.stream()
                .filter(c -> "session_access_token".equals(c.getName()))
                .filter(c -> c.getDomain() != null && c.getDomain().contains("bliblitiket"))
                .findFirst()
                .orElse(null);

        if (unmCookie == null) {
            Cookie fallback = driver.manage().getCookieNamed("session_access_token");
            if (fallback == null) {
                throw new RuntimeException("session_access_token cookie not found. "
                        + "Current URL: " + driver.getCurrentUrl()
                        + ". Ensure token is captured before navigating away from tiket.com.");
            }
            return fallback.getValue();
        }
        return unmCookie.getValue();
    }

    public void changeEmailAPI(String newEmail, String oldEmail, String accessToken) {
        String otpIdOldEmail = generateOtp(oldEmail, "CHANGE_EMAIL", "EMAIL", accessToken);
        String passCodeOldEmail = verifyOtp(otpIdOldEmail, "CHANGE_EMAIL", accessToken);

        String otpIdNewEmail = generateOtp(newEmail, "VERIFY_EMAIL", "EMAIL", accessToken);
        String passCodeNewEmail = verifyOtp(otpIdNewEmail, "VERIFY_EMAIL", accessToken);

        PUTV1SecurityEmail.Inputs inputs = new PUTV1SecurityEmail.Inputs(
                newEmail,
                passCodeNewEmail,
                passCodeOldEmail,
                "EMAIL_OTP",
                Optional.of(accessToken)
        );

        PUTV1SecurityEmail changeEmailApi = new PUTV1SecurityEmail(inputs);
        ApiResponse changeEmailApiResponse = changeEmailApi.hitApi(apiTools);
        JSONObject changeEmailApiObject = new JSONObject(changeEmailApiResponse.getBody());
        String code = changeEmailApiObject.getString("code");
        if(code.equalsIgnoreCase("SUCCESS")) {
            logger.log(String.format("Success change email: '%s' to '%s' via API", oldEmail, newEmail));
        } else {
            throw new RuntimeException("Change email API failed with code: " + code + "\n and response: " + changeEmailApiResponse.getBody());
        }
    }

    private String generateOtp(String recipient, String action, String channel, String accessToken) {
        POSTOtpGenerateUNMApi.Inputs inputs = new POSTOtpGenerateUNMApi.Inputs(
                recipient,
                action,
                channel,
                Optional.of(accessToken)
        );

        POSTOtpGenerateUNMApi postOtpGenerateUNMApi = new POSTOtpGenerateUNMApi(inputs);
        ApiResponse postOtpGenerateUNMApiResponse = postOtpGenerateUNMApi.hitApi(apiTools);
        JSONObject postOtpGenerateUNMApiObject = new JSONObject(postOtpGenerateUNMApiResponse.getBody());
        logger.log("code otp generate : -> " + postOtpGenerateUNMApiObject.getString("code"));
        return postOtpGenerateUNMApiObject.getJSONObject("data").getString("otpId");
    }

    private String verifyOtp(String otpId, String action, String accessToken) {
        POSTOtpVerifyUNMApi.Inputs inputs = new POSTOtpVerifyUNMApi.Inputs(
                otpId,
                action,
                Optional.of(accessToken)
        );

        POSTOtpVerifyUNMApi postOtpVerifyUNMApi = new POSTOtpVerifyUNMApi(inputs);
        ApiResponse postOtpVerifyUNMApiResponse = postOtpVerifyUNMApi.hitApi(apiTools);
        JSONObject postOtpVerifyUNMApiObject = new JSONObject(postOtpVerifyUNMApiResponse.getBody());
        logger.log("passCode -> " + postOtpVerifyUNMApiObject.getString("code"));
        return postOtpVerifyUNMApiObject.getJSONObject("data").getString("passCode");
    }

    public String getDOBText() {
        driverTools.waitForElementToPresence(DOB_LOCATOR) ;
        String dob = driverTools.getValue(DOB_LOCATOR);
        return dob;
    }

    public void deleteAccount() {
        driverTools.waitForElementToPresence(DELETE_BTN) ;
        driverTools.clickWithJavascriptExecutor(DELETE_BTN);
        driverTools.waitForElementToPresence(TNC_BTN) ;
        driverTools.clickWithJavascriptExecutor(TNC_BTN);
        driverTools.clickWithJavascriptExecutor(CONTINUE_DELETE_BTN);
    }

    public void confirmDeleteAccount() {
        driverTools.waitForElementToPresence(REASON_BTN) ;
        driverTools.clickWithJavascriptExecutor(REASON_BTN);
        driverTools.clickWithJavascriptExecutor(REASON_CHECKBOX);
        driverTools.clickWithJavascriptExecutor(REASON_SUBMIT);
        driverTools.clickWithJavascriptExecutor(CONTINUE_DELETE_BTN);
        driverTools.clickWithJavascriptExecutor(CONFIRM_DELETE_BTN);
    }

    public boolean isSeeYouAgain() {
        return driverTools.waitForElementToAppear(SEE_YOU_BTN).isPresent();
    }

    public String getGenderText() {
        driverTools.waitForElementToPresence(GENDER_LOCATOR) ;
        String gender = driverTools.getValue(GENDER_LOCATOR);
        logger.log(gender);
        return gender;
    }

    public void changeGender(String selectedGender) {
        driverTools.waitForElementToAppear(GENDER_LOCATOR, 5);
        driverTools.click(GENDER_LOCATOR);
        driverTools.clickWithJavascriptExecutor(getDifferentGenderRadioButtonLocator(selectedGender));
        if(driverTools.waitForElementToAppear(CLICK_SAVE_GENDER,5).isPresent()){
            driverTools.click(CLICK_SAVE_GENDER);
        }
    }

    private Locator getDifferentGenderRadioButtonLocator(String selectedGender) {
        return new Locator("GENDER_RADIO_BUTTON", new By.ByXPath("(//div[contains(@class,'DropdownContent_custom_radio_option')]//label[contains(@class,'Text') and not(text()= '"+selectedGender+"')])[1]"));
    }

    public void changeDOB() {
        driverTools.waitForElementToPresence(DOB_LOCATOR) ;
        driverTools.click(DOB_LOCATOR);
        driverTools.click(DOB_SUBMIT);
        if(driverTools.waitForElementToAppear(CONFIRM_SAVE_NAME).isPresent()) {
            driverTools.click(CONFIRM_SAVE_NAME);
        }
    }

}