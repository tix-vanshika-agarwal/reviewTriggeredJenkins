package com.tiket.web.page.platform.eiffel.UNM;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class UNMPageWeb<T extends WebDriver> extends WebPage<T> {

    public UNMPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> UNMPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
        case DWEB -> {
            return new UNMPageDWeb<>(session);
        }
        case AMWEB, IMWEB -> {
            return new UNMPageMWeb<>(session);
        }
        default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final String BLIBLI_STAGING_URL = "https://wwwuatb.gdn-app.com/";
    private static final String BLIBLI_PREPROD_URL = "https://wwwpreprod.gdn-app.com/";
    private static final String BLIBLI_PROD_URL = "https://www.blibli.com/";

    protected static final Locator BLIBLI_LOGIN_BUTTON = new Locator("BLIBLI_LOGIN_BUTTON", By.xpath("//button[contains(@class, 'btn__login')]"));
    private static final Locator ONE_FIELD_UNM = new Locator("ONE_FIELD_UNM", By.xpath("//input[@id='phone-number-or-email' or @id='nomor-hp-atau-email']"));
    private static final Locator ONE_FIELD_PASSWORD = new Locator("ONE_FIELD_PASSWORD", By.xpath("//input[@id='password' or @id='kata-sandi']"));
    private static final Locator ONE_FIELD_BUTTON = new Locator("ONE_FIELD_BUTTON", By.xpath("//button[(contains(text(), 'Register') or contains(text(), 'Daftar')) or (contains(text(), 'Log in') or contains(text(), 'Masuk'))]"));
    private static final Locator UNM_REGISTER_PAGE = new Locator("UNM_REGISTER_PAGE", By.xpath("//div[contains(@class,'LoginRegisterLayout_children_content') and contains(@class, 'LoginRegisterLayout_full_screen')]"));
    private static final Locator FULL_NAME_INPUT = new Locator("FULL_NAME_INPUT", By.cssSelector("input[data-testid='txtFullname']"));
    private static final Locator PHONE_NUMBER_INPUT = new Locator("PHONE_NUMBER_INPUT", By.cssSelector("input[id='phone-number'], input[id='nomor-hp']"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", By.cssSelector("input[id='email']"));
    private static final Locator PASSWORD_INPUT = new Locator("PASSWORD_INPUT", By.cssSelector("input[id='password'], input[id='kata-sandi']"));
    private static final Locator REGISTER_BTN = new Locator("REGISTER_BTN", By.xpath("//button[@data-testid='btnSubmit']"));
    private static final Locator OTP_BOTTOM_SHEET_MWEB = new Locator("OTP_BOTTOM_SHEET", By.xpath("//div[@data-testid='bottom-sheet-body']"));
    private static final Locator OTP_BOTTOM_SHEET_DWEB = new Locator("OTP_BOTTOM_SHEET", By.xpath("//div[contains(@class, 'OtpModule_otp_input')]"));
    private static final Locator SUCCESS_REGISTER_PAGE = new Locator("SUCCESS_REGISTER_PAGE", By.xpath("//main[contains(@class, 'styles_success_register_container')]"));
    private static final Locator SUCCESS_REGISTER_BTN = new Locator("SUCCESS_REGISTER_BTN", By.xpath("//button[contains(@class, 'Button_variant_primary') and (contains(text(), 'Oke') or contains(text(), 'OK'))]"));
    private static final Locator PROFILE_NAME_ELEMENT = new Locator("PROFILE_NAME_ELEMENT", By.xpath("//span[contains(@data-testid, 'profile-field-fullname')]"));
    private static final Locator PROFILE_PHONE_NUMBER_ELEMENT = new Locator("PROFILE_PHONE_NUMBER_ELEMENT", By.xpath("//span[contains(@data-testid, 'profile-field-phonenumber')]"));
    private static final Locator PROFILE_EMAIL_ELEMENT = new Locator("PROFILE_EMAIL_ELEMENT", By.xpath("//span[contains(@data-testid, 'profile-field-email')]"));
    private static final Locator SSO_OK_BUTTON = new Locator("SSO_OK_BUTTON", By.xpath("//div[contains(@class, 'sso__footer')]/button[contains(@class, 'b-secondary')]"));

    private static Locator getOtpFieldLocator(int index) {
        return new Locator("OTP_FIELD_" + index, By.xpath("//input[contains(@data-testid, 'otp-input-" + index + "')]"));
    }

    public abstract void ensureUserIsLoggedInOnBlibli();

    public void inputOtp(String otp) {
        Locator otpBottomSheet = Configs.PLATFORM == Platform.DWEB ? OTP_BOTTOM_SHEET_DWEB : OTP_BOTTOM_SHEET_MWEB;
        Optional<?> otpField = driverTools.waitForElementToAppear(otpBottomSheet);
        if (otpField.isPresent()) {
            for (int i = 0; i < otp.length(); i++) {
                Locator otpInput = getOtpFieldLocator(i + 1);
                driverTools.sendKeys(otpInput, String.valueOf(otp.charAt(i)));
            }
            driverTools.waitForElementToDisappear(otpBottomSheet);
        } else {
            throw new IllegalStateException("OTP input field not appeared.");
        }
    }


    public void goToUNMAccountPageViaURL() {
        String url;
        switch (Configs.ENVIRONMENT) {
        case GK -> url = "https://account.test.bliblitiket.dev/";
        case PREPROD -> url = "https://account.preprod.bliblitiket.dev/";
        default -> url = "https://account.bliblitiket.com/";
        }
        driverTools.get(url + "account");
    }

    public Map<String, String> getProfileData() {
        driverTools.waitForElementToAppear(PROFILE_NAME_ELEMENT);
        String fullName = driverTools.getText(PROFILE_NAME_ELEMENT).orElse("");
        String phoneNumber = driverTools.getText(PROFILE_PHONE_NUMBER_ELEMENT).orElse("");
        String email = driverTools.getText(PROFILE_EMAIL_ELEMENT).orElse("");

        Map<String, String> profileData = new HashMap<>();
        profileData.put("fullName", fullName);
        profileData.put("phoneNumber", phoneNumber);
        profileData.put("email", email);
        return profileData;
    }

    public void loginFromBlibli(String email, String password, String otp) {
        switch (Configs.ENVIRONMENT) {
        case GK -> driverTools.get(BLIBLI_STAGING_URL);
        case PREPROD -> driverTools.get(BLIBLI_PREPROD_URL);
        case PROD -> driverTools.get(BLIBLI_PROD_URL);
        default -> throw new IllegalStateException("Unknown environment: " + Configs.ENVIRONMENT);
        }

        driverTools.waitForElementToAppear(BLIBLI_LOGIN_BUTTON, 10).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(ONE_FIELD_UNM);
        driverTools.sendKeys(ONE_FIELD_UNM, email);
        driverTools.click(ONE_FIELD_BUTTON);

        driverTools.waitForElementToAppear(ONE_FIELD_PASSWORD);
        driverTools.sendKeys(ONE_FIELD_PASSWORD, password);
        driverTools.click(ONE_FIELD_BUTTON);

        inputOtp(otp);
        if (!isLoginOnBlibli()) {
            throw new IllegalStateException("Login failed, user is not redirected to Blibli homepage after SSO login");
        }
    }

    public boolean isLoginOnBlibli() {
        return driverTools.waitForElementToAppear(BLIBLI_LOGIN_BUTTON, 5).isEmpty();
    }

    public void clickSSOPopUpButton() {
        driverTools.waitForElementToAppear(SSO_OK_BUTTON, 7).ifPresentOrElse(driverTools::click, () -> {
            Assertion.skip("SSO on Blibli website got issue, skipping case.");
        });
        driverTools.waitForElementToDisappear(SSO_OK_BUTTON);
    }

    public void goToBlibliWeb() {
        switch (Configs.ENVIRONMENT) {
        case GK -> driverTools.get(BLIBLI_STAGING_URL);
        case PREPROD -> driverTools.get(BLIBLI_PREPROD_URL);
        case PROD -> driverTools.get(BLIBLI_PROD_URL);
        default -> throw new IllegalStateException("Unknown environment: " + Configs.ENVIRONMENT);
        }
    }
}
