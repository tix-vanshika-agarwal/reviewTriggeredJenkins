package com.tiket.page.accountcenter;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static com.tiket.core.CoreFunctions.monthEngToId;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.core.Is.is;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.accountcenter.profile.ProfilePage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.login.LoginPageIOS;
import com.tiket.pojo.unm.GetProfileDetailApiResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;

import io.appium.java_client.AppiumBy;

public abstract class AccountCenterPage<T extends WebDriver> extends BasePage<T> {
    public AccountCenterPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> AccountCenterPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new AccountCenterPageAndroid<>(session);
            }
            case IOS -> {
                return new AccountCenterPageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private final Locator EDIT_BTN = new Locator("EDIT_BTN", AppiumBy.xpath("//span[contains(@class,'pointer Text_text')]"));
    private final Locator COUNTRY_NAME = new Locator("COUNTRY_NAME", AppiumBy.xpath("//div[@data-testid='settings-country']//div[contains(@class,'right')]"));
    private static final Locator COUNTRY_SEARCH = new Locator("COUNTRY_SEARCH", AppiumBy.xpath("//div[contains(@class,'SearchBox')]//input"));
    private static final Locator CHANGE_PHONE_NUMBER_BTN = new Locator("CHANGE_PHONE_NUMBER_BTN", AppiumBy.xpath("//*[@data-testid='contact-preview-change-phonenumber']"));
    private static final Locator PHONE_NUMBER_FIELD = new Locator("PHONE_NUMBER_FIELD", AppiumBy.xpath("//input[@id='phone-number' or @id='nomor-hp']"));
    private static final Locator VERIFY_PHONE_NUMBER_BUTTON = new Locator("VERIFY_PHONE_NUMBER_BUTTON", AppiumBy.xpath("//button[contains(@class,'Button_button')]"));    
    private static final Locator ERROR_POPUP_PN_CLAIMED = new Locator("ERROR_POPUP_PN_CLAIMED", AppiumBy.xpath("//div[contains(@class,'styles_state_danger')]//span[contains(text(),'?')]"));
    private static Locator OTP_BTM_SHEET = new Locator("OTP_BTM_SHEET", AppiumBy.xpath("//*[contains(@class,'BottomSheet')]//*[contains(text(),'OTP')]"));
    private static Locator GENDER_LOCATOR = new Locator("GENDER_LOCATOR", AppiumBy.xpath("//input[contains(@data-testid,'change-gender-dropdown')]"));
    private static Locator ACCOUNT_NAME_LOCATOR = new Locator("ACCOUNT_NAME_LOCATOR", AppiumBy.xpath("//input[contains(@id,'nama') or contains(@id,'name')]"));
    private static Locator CLICK_SAVE_GENDER = new Locator("CLICK_SAVE_GENDER", AppiumBy.xpath("//button[@data-testid='select-radio-submit']"));
    private final Locator DELETE_BTN = new Locator("DELETE_BTN", AppiumBy.xpath("//*[@data-testid='btnDeleteAccount']"));
    private final Locator TNC_BTN = new Locator("TNC_BTN", AppiumBy.xpath("//*[contains(@class,'custom_checkbox')]"));
    private final Locator CONTINUE_DELETE_BTN = new Locator("CONTINUE_DELETE_BTN", AppiumBy.xpath("//*[@data-testid='btnContinueDeletion']"));
    private final Locator REASON_BTN = new Locator("REASON_BTN", AppiumBy.xpath("//*[@data-testid='ddSelectDeletionReason']"));
    private final Locator REASON_CHECKBOX = new Locator("REASON_CHECKBOX", AppiumBy.xpath("//*[contains(@class,'Radio_checkbox')]"));
    private final Locator REASON_SUBMIT = new Locator("REASON_SUBMIT", AppiumBy.xpath("//*[@data-testid='select-radio-submit']"));
    private final Locator CONFIRM_DELETE_BTN = new Locator("CONFIRM_DELETE_BTN", AppiumBy.xpath("//*[contains(@class,'Button_variant_primary')]"));
    private final Locator SEE_YOU_BTN = new Locator("SEE_YOU_BTN", AppiumBy.xpath("//*[contains(@class,'Dialog_body')]//button"));
    private final Locator CHANGE_PASSWORD_BUTTON = new Locator("CHANGE_PASSWORD_BUTTON", AppiumBy.xpath("//*[contains(@data-testid,'settings-changepassword')]//*[contains(@class,'SettingsItem_item_right')]"));
    private final Locator CURRENT_PASSWROD = new Locator("CURRENT_PASSWROD", AppiumBy.xpath("//input[@placeholder='Current password']"));
    private final Locator NEW_PASSWORD = new Locator("NEW_PASSWORD", AppiumBy.xpath("//input[@placeholder='New password']"));
    private final Locator CONFIRM_PASSWORD = new Locator("CONFIRM_PASSWORD", AppiumBy.xpath("//button[contains(@class, 'CreatePassword_submit')]"));
    private final Locator CONTINUE_PASWORD_CHANGE = new Locator("CONTINUE_PASWORD_CHANGE", AppiumBy.xpath("//button[contains(@class,'AccountVerification_submit__')]"));
    private final Locator CHANGE_NAME_FIELD = new Locator("CHANGE_NAME_FIELD", AppiumBy.xpath("//input[@name='value']"));
    private final Locator CONFIRM_SAVE_NAME = new Locator("CONFIRM_SAVE_NAME", AppiumBy.xpath("(//*[contains(@class,'Button_variant_primary')])[2]"));
    private final Locator DOB_LOCATOR = new Locator("DOB_LOCATOR", AppiumBy.xpath("//input[@data-testid = 'change-date-of-birth-dropdown-form-display']"));
    private final Locator DOB_SUBMIT = new Locator("DOB_SUBMIT", AppiumBy.xpath("//button[contains(@class,'DatePicker')]"));
    private static final Locator CHANGE_OR_VERIFY_BTN = new Locator("CHANGE_OR_VERIFY_BTN", AppiumBy.xpath("//span[contains(@data-testid,'contact-preview-change-email')]"));
    private static final Locator CONTINUE_BTN = new Locator("CONTINUE_BTN", AppiumBy.xpath("//button[contains(@class,'styles_primary_cta')]"));
    private static final Locator NEW_EMAIL_FIELD = new Locator("NEW_EMAIL_FIELD", AppiumBy.xpath("//input[@name='email']"));
    private static final Locator VERIFY_BTN_EMAIL_CHANGE = new Locator("VERIFY_BTN_EMAIL_CHANGE", AppiumBy.xpath("//button[contains(@data-testid,'btnSubmitChangeEmail')]"));
    private static final Locator ACCOUNT_DETAIL_FULL_NAME = new Locator("ACCOUNT_DETAIL_FIRSTNAME", AppiumBy.xpath("//*[contains(@data-testid,'profile-field-fullname')]"));
    private static final Locator ACCOUNT_DETAIL_DOB = new Locator("ACCOUNT_DETAIL_DOB", AppiumBy.xpath("//*[contains(@data-testid,'profile-field-dateofbirth')]"));
    private static final Locator ACCOUNT_DETAIL_SALUTATION = new Locator("ACCOUNT_DETAIL_SALUTATION", AppiumBy.xpath("//*[contains(@data-testid,'profile-field-gender')]"));
    public static final Locator EMAIL_ASSOCIATED_WITH_ACCOUNT = new Locator("EMAIL_ASSOCIATED_WITH_ACCOUNT", AppiumBy.xpath("//*[contains(@data-testid,'profile-field-email')]"));
    public static final Locator PHONE_NUMBER_ASSOCIATED_WITH_ACCOUNT = new Locator("PHONE_NUMBER_ASSOCIATED_WITH_ACCOUNT", AppiumBy.xpath("//*[contains(@data-testid,'profile-field-phonenumber')]"));

    protected String translateDobToBhs(String DOB) {
        String DOB_split[] = DOB.split(" ");

        String mothInBhs = monthEngToId(DOB_split[1]);
        return mothInBhs == null ? DOB : DOB_split[0] + " " + mothInBhs + " " + DOB_split[2];
    }

    protected String translateGender(String salutation) {
        Map<String, String> mapSalutationEnToBhs = new HashMap<String, String>() {
            {
                put("male", "Laki-laki");
                put("female", "Perempuan");
                put("prefer_not_to_say", "-");
            }
        };
        String salutationInBhs = mapSalutationEnToBhs.get(salutation.toLowerCase());
        return salutationInBhs == null ? salutation : salutationInBhs;
    }

    public String changeEmail(String oldEmail){
        String newEmail = CoreFunctions.randomEmail().toLowerCase();
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        driverTools.click(EDIT_BTN);
        ProfilePage<T> profilePage = ProfilePage.getInstance(session);
        driverTools.switchToNative();
        boolean isEmailChangeSuccess = profilePage.verifyAndChangeEmail(oldEmail.toLowerCase(),newEmail);
        Assertion.assertThat("is email change success for email :" + newEmail ,isEmailChangeSuccess,is(true));
        return newEmail;
    }

    public void changeEmailWhenVerified(String newEmail){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");

        driverTools.click(CHANGE_OR_VERIFY_BTN);
        driverTools.click(CONTINUE_BTN);
        inputOtp(1);

        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        WebElement enterNewEmail = driverTools.waitForElementToAppear(NEW_EMAIL_FIELD).orElseThrow();
        driverTools.sendKeys(enterNewEmail,newEmail);
        driverTools.click(VERIFY_BTN_EMAIL_CHANGE);
        inputOtp(1);
        driverTools.switchToWebViewUrl("account");
        driverTools.switchToNative();
    }

    public String getCountryName() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        driverTools.waitForElementToAppear(COUNTRY_NAME,5);
        String country = driverTools.getText(COUNTRY_NAME).orElseThrow();
        driverTools.switchToNative();
        return country;
    }

    public void changeCountry(String country, String initialCountry){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        driverTools.click(COUNTRY_NAME);
        if (initialCountry .equals(country)) {
            country = "Indonesia";
        }
        driverTools.waitForElementToAppear(COUNTRY_SEARCH,5);
        driverTools.sendKeys(COUNTRY_SEARCH, country);
        driverTools.click(countryLocator(country));
        driverTools.switchToNative();
    }

    private Locator countryLocator(String country){
        return new Locator(country, AppiumBy.xpath("//img[@alt='"+country+"']"));
    }

    public void openEditProfile() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        driverTools.clickWithJavascriptExecutor(EDIT_BTN);
        driverTools.switchToNative();
    }

    public void clickChangePhoneNumberBtn() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account/edit");
driverTools.click(driverTools.waitForElementToPresence(CHANGE_PHONE_NUMBER_BTN).orElseThrow());
        driverTools.switchToNative();
    }

    public void changePhoneNumber(String phoneNumber) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account/edit");
        driverTools.click(PHONE_NUMBER_FIELD);
        driverTools.clearWithJsExecutor(PHONE_NUMBER_FIELD);
        driverTools.sendKeys(PHONE_NUMBER_FIELD, Keys.chord(Keys.CONTROL, "a"));
        driverTools.sendKeys(PHONE_NUMBER_FIELD, phoneNumber);
        driverTools.click(VERIFY_PHONE_NUMBER_BUTTON);
        driverTools.switchToNative();
    }

    public boolean isPhoneNumberClaimedErrorDisplayed() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        boolean isDisplayed = driverTools.isElementDisplayed(ERROR_POPUP_PN_CLAIMED);
        driverTools.getText(ERROR_POPUP_PN_CLAIMED).ifPresent(System.out::println);
        driverTools.switchToNative();
        return isDisplayed;
    }

    public void inputOtp(int retryCount){
        LoginPage<T> loginPage = LoginPage.getInstance(session);
        logger.log("inputting otp");
        if(Configs.PLATFORM == Platform.IOS && !driverTools.waitForElementToAppear(LoginPageIOS.OTP_BTM_SHEET_TITLE_NATIVE,4).isPresent()){
            return;
        }else{
            driverTools.switchToWebViewUrl("change");
        }
        for (int i = 1; i < 7; i++) {
            Locator otpField = loginPage.getOtpLocator(i);
            Optional<WebElement> otp_field_element = driverTools.waitForElementToAppear(otpField, 5);
            if (!otp_field_element.isPresent() && i == 1) break;
            //retry one more time
            if (otp_field_element.isPresent()) otp_field_element = driverTools.waitForElementToAppear(otpField, 5);
            driverTools.click(otp_field_element.get());
            driverTools.sendKeys(otp_field_element.get(), "" + i + "");
        }
        if (!driverTools.waitForElementToDisappear(OTP_BTM_SHEET, 5) && retryCount > 0) {
            inputOtp(retryCount - 1);
        }
        driverTools.waitForElementToDisappear(loginPage.getOtpLocator(1));
        driverTools.switchToNative();
    }

    public void changeGender(String selectedGender) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        driverTools.waitForElementToAppear(GENDER_LOCATOR, 5);
        driverTools.click(GENDER_LOCATOR);
        driverTools.clickWithJavascriptExecutor(getDifferentGenderRadioButtonLocator(selectedGender));
        driverTools.click(CLICK_SAVE_GENDER);
        driverTools.switchToNative();
    }

    public void changeName(String name) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        driverTools.waitForElementToAppear(ACCOUNT_NAME_LOCATOR, 5);
        driverTools.click(ACCOUNT_NAME_LOCATOR);
        WebElement changeField = driverTools.element(CHANGE_NAME_FIELD).orElseThrow();
        driverTools.clearInput(changeField);
        driverTools.sendKeys(CHANGE_NAME_FIELD, name);
        driverTools.click(CONFIRM_DELETE_BTN);
        driverTools.click(CONFIRM_SAVE_NAME);
        driverTools.switchToNative();
    }

    private Locator getDifferentGenderRadioButtonLocator(String selectedGender) {
        return new Locator("GENDER_RADIO_BUTTON", AppiumBy.xpath("(//div[contains(@class,'DropdownContent_custom_radio_option')]//label[contains(@class,'Text') and not(text()= '"+selectedGender+"')])[1]"));
    }

    public String getGenderText() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        String gender = driverTools.getValue(GENDER_LOCATOR);
        driverTools.switchToNative();
        return gender;
    }

    public String getNameText() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        String gender = driverTools.getValue(ACCOUNT_NAME_LOCATOR);
        driverTools.switchToNative();
        return gender;
    }

    public void changeDOB() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        driverTools.click(DOB_LOCATOR);
        driverTools.click(DOB_SUBMIT);
        driverTools.click(CONFIRM_SAVE_NAME);
        driverTools.switchToNative();
    }

    public String getDOBText() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        String dob = driverTools.getValue(DOB_LOCATOR);
        driverTools.switchToNative();
        return dob;
    }

    public void deleteAccount() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        driverTools.click(DELETE_BTN);
        driverTools.switchToNative();
        driverTools.switchToWebViewUrl("account");
        driverTools.click(TNC_BTN);
        driverTools.click(CONTINUE_DELETE_BTN);
        driverTools.switchToNative();
    }

    public void confirmDeleteAccount() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        driverTools.click(REASON_BTN);
        List<WebElement> reasonCheckbox = driverTools.elements(REASON_CHECKBOX).orElseThrow();
        int randomList = generateRandomInt(0, reasonCheckbox.size() - 2);
        driverTools.click(reasonCheckbox.get(randomList));
        driverTools.click(REASON_SUBMIT);
        driverTools.click(CONTINUE_DELETE_BTN);
        driverTools.click(CONFIRM_DELETE_BTN);
        driverTools.switchToNative();
    }

    public boolean isSeeYouAgain() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        boolean seeYou = driverTools.waitForElementToAppear(SEE_YOU_BTN).isPresent();
        driverTools.switchToNative();
        return seeYou;
    }

    public void clickSeeYouAgain() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        driverTools.click(SEE_YOU_BTN);
        driverTools.switchToNative();
        if (Configs.PLATFORM == Platform.IOS) {
            LoginPageIOS<T> loginPageIOS = new LoginPageIOS<>(session);
            loginPageIOS.closeUnmPageRedirection();
        }
    }

    public void clickChangePasswordButton() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        driverTools.click(CHANGE_PASSWORD_BUTTON);
        driverTools.switchToNative();
    }

    public void changePassword(String currentPassword, String newPassword) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        driverTools.waitForElementToAppear(CURRENT_PASSWROD,10);
        driverTools.sendKeys(CURRENT_PASSWROD, currentPassword);
        driverTools.click(CONTINUE_PASWORD_CHANGE);
        driverTools.waitForElementToAppear(CONFIRM_PASSWORD,10);
        driverTools.sendKeys(NEW_PASSWORD, newPassword);
        driverTools.clickWithJavascriptExecutor(CONFIRM_PASSWORD);
        driverTools.switchToNative();
    }

    public String getValueOnAccountCenter(String text) {
        //this function is used to get any attribute on account center page
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account/edit");
        WebElement element = driverTools.element(new Locator("text", AppiumBy.xpath("//*[contains(text(),'"+text+"') or @value='"+text+"']"))).orElseThrow();
        driverTools.waitForElementToAppear(element);
        String accountPageText = driverTools.getText(element);
        driverTools.switchToNative();
        logger.log("Value on Account Page: "+ accountPageText);
        return accountPageText;
    }

    public boolean verifyAccountDetail(GetProfileDetailApiResponse profileDetailApiResponse){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        driverTools.waitForWebToLoad();
        String fullName =  driverTools.getText(ACCOUNT_DETAIL_FULL_NAME).orElseThrow();
        String actualDOB = driverTools.getText(ACCOUNT_DETAIL_DOB).orElseThrow();
        String actualSalutation = driverTools.getText(ACCOUNT_DETAIL_SALUTATION).orElseThrow();

        String expectedFullName = profileDetailApiResponse.data.name;
        String expectedGenderInBhs = this.translateGender(profileDetailApiResponse.data.gender);
        String expectedDOB_Bha = this.translateDobToBhs(profileDetailApiResponse.data.getFormattedDob());


        logger.log("Actual user data from account detail page");
        logger.log("Actual name " + fullName);
        logger.log("Actual dob " + actualDOB);
        logger.log("Actual salutation " + actualSalutation);

        Assertion.assertThat("Full name is correct", fullName, is(expectedFullName));
        Assertion.assertThat("Date of birth is correct", actualDOB, anyOf(containsString(expectedDOB_Bha),containsString(profileDetailApiResponse.data.getFormattedDob())));
        Assertion.assertThat("Gender is correct", actualSalutation, anyOf(equalToIgnoringCase(expectedGenderInBhs),equalToIgnoringCase(profileDetailApiResponse.data.gender)));
        driverTools.switchToNative();

        return true;
    }

    public String getEmailAssociatedWithAccount(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        WebElement emailElement = driverTools.waitForElementToPresence(EMAIL_ASSOCIATED_WITH_ACCOUNT).orElseThrow();
        String email = driverTools.getText(emailElement);
        logger.log("Email associated with account: "+ email);
        driverTools.switchToNative();
        return email;
    }

    public String getPhoneNumberAssociatedWithAccount(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
        WebElement emailElement = driverTools.waitForElementToPresence(PHONE_NUMBER_ASSOCIATED_WITH_ACCOUNT).orElseThrow();
        String email = driverTools.getText(emailElement);
        logger.log("Email associated with account: "+ email);
        driverTools.switchToNative();
        return email;
    }
}
