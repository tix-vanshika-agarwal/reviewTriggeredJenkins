package com.tiket.page.account;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.utilities.message.help_center.HelpCenterPage;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
public class AccountPageAndroid <T extends WebDriver> extends AccountPage<T> {

    private static final Locator PROFILE_DETAIL_HEADER = new Locator("PROFILE_DETAIL_HEADER", AppiumBy.id("tds_header_navigation_appbar"));
    private static final Locator EDIT_PROFILE_DETAILS = new Locator("EDIT_PROFILE_DETAILS", AppiumBy.id("tv_action"));
    private static final Locator SETTING_BUTTON = new Locator("SETTING_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'rv_account')]//*[contains(@resource-id,'tv_action')]"));
    private static final Locator ENGLISH_RADIO_BUTTON = new Locator("englishRadioButton", AppiumBy.xpath("(//*[contains(@resource-id,'tds_subtitle_list')])[2]"));
    private static final Locator BAHASA_RADIO_BUTTON = new Locator("bahasaRadioButton", AppiumBy.xpath("(//*[contains(@resource-id,'tds_subtitle_list')])[1]"));
    private static final Locator SETTINGS = new Locator("Settings", AppiumBy.id("label"));
    private static final Locator BACK_BUTTON = new Locator("Back Button", AppiumBy.id("iv_toolbar_left_button"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE BUTTON", AppiumBy.id("btn_save"));
    private static final Locator CROSS_BUTTON = new Locator("Cross Button", AppiumBy.id("tds_icon_navigation_toolbar"));
    private static final Locator ACCOUNT_DETAIL_FIRSTNAME = new Locator("ACCOUNT_DETAIL_FIRSTNAME", AppiumBy.xpath("//*[contains(@resource-id,'tf_first_middle_name')]//*[contains(@resource-id,'tds_box_input_text')]"));
    private static final Locator ACCOUNT_DETAIL_LASTNAME = new Locator("ACCOUNT_DETAIL_LASTNAME", AppiumBy.xpath("//*[contains(@resource-id,'tf_last_name')]//*[contains(@resource-id,'tds_box_input_text')]"));
    private static final Locator ACCOUNT_DETAIL_DOB = new Locator("ACCOUNT_DETAIL_DOB", AppiumBy.xpath("//*[contains(@resource-id,'tf_date_of_birth')]//*[contains(@resource-id,'tds_box_input_text')]"));
    private static final Locator ACCOUNT_DETAIL_SALUTATION = new Locator("ACCOUNT_DETAIL_SALUTATION", AppiumBy.xpath("//*[contains(@resource-id,'radio_button') and ./*[@index='1']]/following-sibling::*"));
    private static final Locator ACCOUNT_FULL_NAME = new Locator("ACCOUNT_USERNAME", AppiumBy.xpath("//*[contains(@resource-id, 'tv_name')]"));
    private static final Locator LOGIN_CONTAINER = new Locator("LOGIN_CONTAINER", AppiumBy.id("container_login"));
    private static final Locator EDIT_ACCOUNT_BUTTON = new Locator("EDIT_ACCOUNT_BUTTON", AppiumBy.id("tv_action"));
    private static final Locator CLOSE_REGISTER_FACE_ID_FORM_BUTTON = new Locator("CLOSE_REGISTER_FACE_ID_FORM_BUTTON", AppiumBy.id("iv_close"));
    private static final Locator ACTIVATE_FACE_ID_LATER_BUTTON = new Locator("ACTIVATE_FACE_ID_LATER_BUTTON", AppiumBy.id("tds_btn"));
    private static final Locator CLOSE_ADD_PIN = new Locator("CLOSE_ADD_PIN", AppiumBy.id("iv_close"));
    private static final Locator CONFIRM_ADD_PIN_LATER = new Locator("CONFIRM_ADD_PIN_LATER", AppiumBy.xpath("//*[@name='Mengerti']"));
    private static final Locator ACCOUNT_DETAIL_EMAIL = new Locator("ACCOUNT_DETAIL_EMAIL", AppiumBy.id("tv_unm_info_2"));
    private static final Locator LANGUAGE_MENU = new Locator("LANGUAGE_MENU", AppiumBy.xpath("//*[contains(@resource-id,\"menu_language\")]//*[contains(@resource-id,'tv_title')]"));
    private static final Locator LOADING = new Locator("LOADING", AppiumBy.id("blocking_loading_layout"));
    private static final Locator HELP_CENTER_ENTRY_BUTTON = new Locator("HELP_CENTER_ENTRY_BUTTON", AppiumBy.xpath("//*[contains(@text,'Help Center') or contains(@text,'Pusat Bantuan')]"));
    private static final Locator WISHLIST_ENTRY = new Locator("WISHLIST_ENTRY_BUTTON", AppiumBy.xpath("//*[contains(@text,'Wishlist')]"));
    private static final Locator SKIP_PASSENGER_DATA_COACH_MARK = new Locator("SKIP_PASSENGER_DATA_COACH_MARK", AppiumBy.id("tds_link_coach_mark"));
    private static final Locator FINISH_PASSENGER_DATA_COACH_MARK = new Locator("FINISH_PASSENGER_DATA_COACH_MARK",AppiumBy.xpath("//*[contains(@resource-id,'tds_btn') and (@text='Selesai' or @text='Finish' or @text='Done')]"));
    private static final Locator EDIT_EMAIL_PHONE_BTN= new Locator("EDIT_EMAIL_PHONE_BTN", AppiumBy.id("email_phone_edit"));
    private static final Locator CHECK_NOT_VERIFIED_EMAIL = new Locator("CHECK_NOT_VERIFIED_EMAIL", AppiumBy.xpath("//*[contains(@resource-id,'email_container')]//*[contains(@resource-id,'label')]//*[contains(@text,'Not verified') or contains(@text,'Belum diverifikasi')]"));
    private static final Locator CHANGE_OR_VERIFY_BTN = new Locator("CHANGE_OR_VERIFY_BTN", AppiumBy.xpath("//*[contains(@resource-id,'email_container')]//*[contains(@resource-id,'tv_action')]"));
    private static final Locator CHANGE_CONTINUE_BTN = new Locator("CHANGE_OR_VERIFY_BTN", AppiumBy.xpath("//*[contains(@resource-id,'btn_continue')]//*[contains(@resource-id,'tds_button_view')]"));
    private static final Locator USING_PHONE_TO_VERIFY = new Locator("USING_PHONE_TO_VERIFY", AppiumBy.id("iv_go_to_mobile_number"));
    private static final Locator NEW_EMAIL_FIELD = new Locator("NEW_EMAIL_FIELD", AppiumBy.id("tds_box_input_text"));
    private static final Locator VERIFY_BTN = new Locator("VERIFY_BTN", AppiumBy.id("tds_button_view"));
    private static final Locator ACCOUNT_NAVIGATION = new Locator("ACCOUNT_NAVIGATION", AppiumBy.xpath("(//*[contains(@resource-id,'tds_title_bottom_navigation') and not(contains(@text,'Rewards'))])[3]"));
    private static final Locator LOYALTY_PARTNER_ENTRY_BTN = new Locator("LOYALTY_PARTNER_ENTRY_BTN", AppiumBy.xpath("//android.widget.TextView[contains(@text,'Loyal')]"));
    private static final Locator ACCOUNT_DETAIL_HEADER = new Locator("ACCOUNT_DETAIL_HEADER", AppiumBy.id("info_header"));
    private static final Locator COUNTRY_BUTTON = new Locator("COUNTRY_BUTTON", AppiumBy.id("menu_country"));
    private static final Locator COUNTRY_TEXT = new Locator("COUNTRY_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'menu_country')]//*[contains(@resource-id,'tv_info')]"));
    private static final Locator CURRENCY_TEXT = new Locator("CURRENCY_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'menu_currency')]//*[contains(@resource-id,'tv_info')]"));

    //loyalty
    private static final Locator LOYALTY_POINT_VALUE = new Locator("LOYALTY_POINT_VALUE", AppiumBy.xpath("//*[contains(@resource-id,'tv_tiket_points_with_ulp')] | //*[contains(@resource-id,'tv_points')]"));
    private static final Locator LOYALTY_LOCKED_BANNER = new Locator("LOYALTY_LOCKED_BANNER", AppiumBy.id("iv_loyalty_and_tiket_points_locked_background"));
    private static final Locator LOYALTY_CONTENT_POPUP = new Locator("LOYALTY_CONTENT_POPUP", AppiumBy.id("rv_benefits"));
    private static final Locator LOYALTY_LOGIN_REGISTER_BUTTON = new Locator("LOYALTY_LOGIN_REGISTER_BUTTON", AppiumBy.id("tds_button_view"));

    //help center (webview)
    private static final Locator ACCOUNT_DETAIL_PHONE_NUMBER = new Locator("ACCOUNT_DETAIL_PHONE_NUMBER", AppiumBy.id("tv_unm_info_1"));
    private static final Locator VOUCHER_BOX = new Locator("VOUCHER_BOX", AppiumBy.xpath("//*[contains(@resource-id,'tv_label') and contains(@text,'Vouchers')]"));
    private static final Locator VOUCHER_BOX_TITLE = new Locator("VOUCHER_BOX_TITLE", AppiumBy.xpath("//*[contains(@class,'page_title') and contains(text(),'Voucher Box')]"));
    private static final Locator VOUCHER_PAGE_SORT_BY_NEWEST_BUTTON = new Locator("VOUCHER_PAGE_SORT_BY_NEWEST_BUTTON", AppiumBy.xpath("//*[contains(@class,'chip-style') and (contains(text(),'Terbaru') or contains(text(),'Newest'))]"));
    private static final Locator VOUCHER_PAGE_SORT_BY_END_DATE_BTN = new Locator("VOUCHER_PAGE_SORT_BY_END_DATE_BTN", AppiumBy.xpath("//*[contains(@class,'chip-style') and (contains(text(),'Tanggal Berakhir') or contains(text(),'End date'))]"));
    //others section
    private static final Locator DEVICE_INFO_BTN = new Locator("DEVICE_INFO_BTN", AppiumBy.id("menu_device_info"));
    private static final Locator OTHERS_ENTRY_BTN = new Locator("OTHERS_ENTRY_BTN", AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_list') and (contains(@text,'Others') or contains(@text,'Lainnya'))]"));
    private static final Locator DEVICE_INFORMATION_POPUP_HEADER = new Locator("DEVICE_INFORMATION_POPUP_HEADER", AppiumBy.id("tv_device_information_header"));
    private static final Locator DEVICE_INFORMATION_POPUP_SUBTITILE = new Locator("DEVICE_INFORMATION_POPUP_SUBTITILE", AppiumBy.id("tv_device_id_info"));
    private static final Locator DEVICE_ID = new Locator("DEVICE_ID", AppiumBy.id("tv_device_id_token"));
    private static final Locator COPY_DEVICE_ID = new Locator("COPY_DEVICE_ID", AppiumBy.id("tds_button_view"));
    private static final Locator DEVICE_ID_COPIED_SNACKBAR = new Locator("DEVICE_ID_COPIED_SNACKBAR", AppiumBy.id("tds_snackbar_root"));
    private static final Locator TERM_AND_CONDITION = new Locator("TERM_AND_CONDITION", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'Terms and Conditions') or  contains(@text,'Syarat dan Ketentuan')]"));
    private static final Locator PRIVACY_POLICY_MENU = new Locator("PRIVACY_POLICY_MENU", AppiumBy.id("menu_privacy_policy"));
    private static final Locator BACK_BTN_ABOUT_MENU = new Locator("PRIVACY_POLICY_BACK_BUTTON", AppiumBy.id("tds_icon_navigation_toolbar_group"));
    private static final Locator ABOUT_TIKETDOTCOM_TOOLBAR = new Locator("ABOUT_TIKETDOTCOM_TOOLBAR", AppiumBy.xpath("//*[contains(@resource-id,'label') and contains(@text,'Tentang tiket.com')or contains(@text,'About tiket.com')]"));
    private static final Locator ABOUT_TIKETDOTCOM = new Locator("ABOUT_TIKETDOTCOM", AppiumBy.xpath("//*[contains(@text,'Tentang tiket.com')or contains(@text,'About tiket.com')]"));
    private static final Locator SEE_ALL_PAYMENT = new Locator("SEE_ALL_PAYMENT", AppiumBy.id("tv_see_all" ));
    private static final Locator BUTTON_LINKAGE_OVO = new Locator("BUTTON_LINKAGE_OVO", AppiumBy.id("btn_linkage" ));
    private static final Locator OVO_ALERT = new Locator("OVO_ALERT", AppiumBy.xpath("//*[contains(@text,'belum terdaftar') or contains(@text,'not been registered')]" ));
    private static final Locator CLOSE_ALERT = new Locator("CLOSE_ALERT", AppiumBy.id("tv_button" ));
    private static final Locator ADD_BCA_CARD = new Locator("ADD_BCA_CARD", AppiumBy.id("tv_add_bca_one_klik" ));
    private static final Locator BCA_CARD_FIELD = new Locator("BCA_CARD_FIELD", AppiumBy.xpath("//*[@text='NOMOR KARTU']//following::*[contains(@resource-id,'card-no')]" ));
    private static final Locator BCA_LIMIT_FIELD = new Locator("BCA_LIMIT_FIELD", AppiumBy.xpath("//*[@text='LIMIT HARIAN PER KARTU']//following::*[contains(@resource-id,'daily-limit')]" ));
    private static final Locator BCA_PHONE_FIELD = new Locator("BCA_PHONE_FIELD", AppiumBy.xpath("//*[@text='PILIH NOMOR HP m-BCA UNTUK KONFIRMASI REGISTRASI']//following::*[contains(@resource-id,'phone-num')]" ));
    private static final Locator BCA_CONTINUE_BUTTON = new Locator("BCA_CONTINUE_BUTTON", AppiumBy.xpath("//android.widget.Button[@text='Lanjut']" ));
    private static final Locator YOUR_REVIEWS = new Locator("YOUR_REVIEWS", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'Review')]" ));

    //edit account email
    private static final Locator CURRENCY_PAGE = new Locator("CURRENCY_PAGE",AppiumBy.xpath("//*[contains(@resource-id, 'tv_title') and contains(@text, 'Currency') or contains(@text, 'Mata Uang')]"));
    private static final Locator CONTINUE_EDIT_PHONE_NUMBER_CONFIRMATION_BTN = new Locator("CONTINUE_EDIT_PHONE_NUMBER_CONFIRMATION_BTN",AppiumBy.id("btn_continue"));
    private static final Locator SEND_ACCOUNT_OWNERSHIP_OTP_TO_PHONE = new Locator("SEND_ACCOUNT_OWNERSHIP_OTP_TO_PHONE",AppiumBy.id("tv_otp_to_mobile_number_title"));
    private static final Locator NEW_PHONE_NUMBER_INPUT_FIELD = new Locator("NEW_PHONE_NUMBER_INPUT_FIELD",AppiumBy.id("tds_box_input_text"));
    private static final Locator VERIFY_NEW_PHONE_NUMBER_BTN = new Locator("VERIFY_NEW_PHONE_NUMBER_BTN",AppiumBy.id("tds_button_view"));
    private static final Locator CURRENT_PHONE_NUMBER_VALUE = new Locator("CURRENT_PHONE_NUMBER_VALUE",AppiumBy.xpath("//*[contains(@resource-id,'mobile_phone_container')]//*[contains(@resource-id,'tv_value')]"));
    private static final Locator BACK_BUTTNON_ON_CHANGE_EMAIL_OR_PHONE_PAGE = new Locator("BACK_BUTTNON_ON_CHANGE_EMAIL_OR_PHONE_PAGE",AppiumBy.id("tds_icon_navigation_toolbar"));
    //referral page
    private static final Locator REFERRAL_BTN = new Locator("REFERRAL_BTN",AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_list') and contains(@text,'Refer')]"));
    private static final Locator PAYLATER_ENTRY_POINT = new Locator("PAYLATER_ENTRY_POINT",AppiumBy.id("container_method"));
    private static final Locator PAYLATER_CAROUSEL = new Locator("PAYLATER_CAROUSEL", AppiumBy.xpath("//*[contains(@resource-id,'item_banner_carousel')]"));
    private static final Locator ACTIVATE_PAYLATER_BTN = new Locator("ACTIVATE_PAYLATER_BTN", AppiumBy.xpath("//*[contains(@resource-id,'btn_slide')]"));
    private static final Locator PAYLATER_LIMIT_CONTAINER = new Locator("PAYLATER_LIMIT_CONTAINER", AppiumBy.xpath("//*[contains(@resource-id,'tv_limit_value')]"));
    private static final Locator BOTTOM_SHEET_INTRODUCING_FEATURE_FOR_MEMBER_ONLY = new Locator("BOTTOM_SHEET_INTRODUCING_FEATURE_FOR_MEMBER_ONLY", AppiumBy.xpath("//*[contains(@resource-id,'tds_txt_header_info_view') and (contains(@text,'This is a member-only feature') or contains(@text,'Fitur ini hanya untuk anggota'))]"));
    private static final Locator LATER_CTA_BTN = new Locator("LATER_CTA_BTN", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Later') or contains(@text,'Nanti aja'))]"));
    //continue as guest popup after logout on account page via deeplink
    private static final Locator CONTINUE_AS_GUEST = new Locator("CONTINUE_AS_GUEST", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Continue as guest') or contains(@text,'Lanjut sebagai tamu') or contains(@text,'Log in or register') or contains(@text,'Log in atau daftar'))]"));
    private static final Locator AFFILIATE_TITLE = new Locator("AFFILIATE_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (@text='Affiliate' or @text='Afiliasi')]"));

    private static  Map<String, List<String>> SESSION_USER_MAPPING = new HashMap<>();

    public AccountPageAndroid(Session<T> session) {
        super(session);
    }

    private void skipCreatePin(){
        driverTools.waitForElementToAppear(CLOSE_ADD_PIN,5).ifPresent(driverTools::click);
    }

    @Override
    public boolean clickContinueAsGuest(){
        // Post-logout, the app lands on the Account page. Two possible UI states:
        //   1. "Continue as guest" popup is shown over the Account page → click to dismiss.
        //   2. No popup; the Account page just shows the login container directly.
        // Either is a valid logged-out confirmation. We try the popup first (cheaper & is
        // the most common case after the deeplink-driven logout); if it doesn't appear,
        // fall back to the login container as the logged-out signal.
        if (driverTools.waitForElementToPresence(CONTINUE_AS_GUEST, 5).isPresent()) {
            driverTools.click(CONTINUE_AS_GUEST);
            return true;
        }
        logger.log("'Continue as guest' popup not shown — checking for login container as logged-out signal");
        return driverTools.waitForElementToPresence(LOGIN_CONTAINER, 5).isPresent();
    }

    @Override
    public void openAffiliate() {
        driverTools.swipeToElement(AFFILIATE_TITLE, Swipe.UP, 10).ifPresentOrElse(
                driverTools::click,
                () -> { throw new RuntimeException("Affiliate entry not found in Account page"); }
        );
    }

    @Override
    public void chooseCountry(String country) {
        HomePage<T> homePage = HomePage.getInstance(this.session);
        homePage.openAccount();
        Optional<WebElement> currentEl = driverTools.swipeToElement(COUNTRY_TEXT, Swipe.UP, 15);
        String currentCountry = currentEl
                .flatMap(el -> driverTools.getText(COUNTRY_TEXT))
                .orElse("");
        // 2) Already correct? Just assert and return — no picker, no clicks.
        if (country.equalsIgnoreCase(currentCountry)) {
            Assertion.assertThat("Country is already set to " + country,
                    currentCountry, equalToIgnoringCase(country));
            return;
        }

        // 3) Otherwise, open the picker and pick the desired country.
        driverTools.swipeToElement(COUNTRY_BUTTON, Swipe.UP, 5).orElseThrow();
        driverTools.click(COUNTRY_BUTTON);
        WebElement countryOption = driverTools.swipeToElement(countryOption(country), Swipe.UP, 3).orElseThrow();
        driverTools.click(countryOption);
        Assertion.assertThat("Verify selected country", driverTools.getText(COUNTRY_TEXT).orElseThrow(), equalTo(country));
    }

    private static Locator countryOption(String country) {
        // Target the nearest clickable row ancestor that contains the country title text.
        // This avoids depending on the position of the radio_button child (which varies across builds)
        // and ensures the click hits the row's click handler instead of a non-clickable text/icon.
        return new Locator("COUNTRY_OPTION_" + country.toUpperCase(),
                AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and @text='" + country + "']/ancestor::android.view.ViewGroup[@clickable='true'][1]"));
    }

    @Override
    public boolean isAccountDetailOpened() {
        boolean firstNameField = driverTools.waitForElementToAppear(ACCOUNT_DETAIL_FIRSTNAME).isPresent();
        boolean lastNameField = driverTools.waitForElementToAppear(ACCOUNT_DETAIL_LASTNAME).isPresent();
        boolean dobField = driverTools.waitForElementToAppear(ACCOUNT_DETAIL_DOB).isPresent();
        boolean emailField = driverTools.waitForElementToAppear(ACCOUNT_DETAIL_EMAIL).isPresent();
        logger.log("status of firstNameField -> " + firstNameField +
                ", \n lastNameField -> " + lastNameField +
                ", \n dobField -> " + dobField +
                ", \n emailField -> " + emailField);
        return firstNameField && lastNameField && dobField && emailField;
    }

    @Override
    public void openSettings() {
        LoginPage<T> loginPage = LoginPage.getInstance(session);
        String accountNavigationValue = driverTools.getText(ACCOUNT_NAVIGATION).orElseThrow();
        boolean isLogin = accountNavigationValue.equals("Account") || accountNavigationValue.equals("Akun");
        driverTools.click(SETTING_BUTTON);
        if(isLogin){
            loginPage.skipRegisterFaceId();
            this.skipCreatePin();
        }
    }

    @Override
    public void selectLanguage(String language) {
        if("ENG".equalsIgnoreCase(language.trim())) {
            selectEnglish();
        } else {
            selectBahasa();
        }
        driverTools.waitForElementToDisappear(LOADING);
    }

    @Override
    public void clickBackButton() {
        driverTools.click(CROSS_BUTTON);
    }

    @Override
    public void closeAccountPage() {
        selectBahasa();
        driverTools.click(BACK_BUTTON);
        driverTools.click(CROSS_BUTTON);
    }

    @Override
    public boolean isLanguageSelected(String language) {
        driverTools.swipeToElement(LANGUAGE_MENU,Swipe.UP,25);
        String text = driverTools.getText(LANGUAGE_MENU).orElseThrow();
        if("ENG".equalsIgnoreCase(language.trim())) {
            logger.log("Language got ENG->" + text);
            return text.equals("Language");
        }
        else{
            logger.log("Language got IN->" + text);
            return text.equals("Bahasa");
        }
    }

    private void selectEnglish() {
        clickLanguageMenu();
        driverTools.click(ENGLISH_RADIO_BUTTON);
    }

    private void selectBahasa() {
        clickLanguageMenu();
        driverTools.click(BAHASA_RADIO_BUTTON);
    }

    private void clickLanguageMenu(){
        WebElement languageMenu = driverTools.swipeToElement(LANGUAGE_MENU,Swipe.UP,25).orElseThrow();
        driverTools.swipeUp();
        driverTools.clickElementByCoordinate(languageMenu);
    }

    private boolean isEnglishSelected() {
        driverTools.waitForElementToDisappear(LOADING);
        driverTools.swipeToElement(LANGUAGE_MENU,Swipe.UP,25);
        return driverTools.isElementDisplayed(new Locator("English",AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and @text='Language']")));
    }

    private boolean isBahasaSelected() {
        driverTools.waitForElementToDisappear(LOADING);
        driverTools.swipeToElement(LANGUAGE_MENU,Swipe.UP,25);
        return driverTools.isElementDisplayed(new Locator("Bhasha",AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and @text='Bahasa']")));
    }

    @Override
    public void clickAccountDetail(){
        driverTools.waitForElementToAppear(EDIT_ACCOUNT_BUTTON);
        if (!driverTools.isElementDisplayed(EDIT_ACCOUNT_BUTTON))
            driverTools.swipeToElement(EDIT_ACCOUNT_BUTTON,Swipe.DOWN,5).orElseThrow();
        WebElement editAccountButton = driverTools.waitForElementToAppear(EDIT_ACCOUNT_BUTTON).orElseThrow();
        driverTools.clickElementByCoordinate(editAccountButton);
        if(driverTools.waitForElementToAppear(PROFILE_DETAIL_HEADER,5).isEmpty()){
            driverTools.clickElementByCoordinate(EDIT_ACCOUNT_BUTTON);
        }
    }

    @Override
    public boolean verifyAccountDetail(String expectedFirstName,String expectedLastName,String expectedDateOfBirth, String expectedSalutation){
        logger.log("Expected dob : " + expectedDateOfBirth);
        driverTools.swipeToElement(ACCOUNT_DETAIL_HEADER,Swipe.DOWN,5);
        String actualFirstname = driverTools.getText(ACCOUNT_DETAIL_FIRSTNAME).orElseThrow().strip();
        String actualLastName = driverTools.getText(ACCOUNT_DETAIL_LASTNAME).orElseThrow().strip();
        String actualDOB = driverTools.getText(ACCOUNT_DETAIL_DOB).orElseThrow().strip();
        String actualSalutation = driverTools.getText(ACCOUNT_DETAIL_SALUTATION).orElseThrow().strip();
        String expectedDobBahasa = this.translateDobToBhs(expectedDateOfBirth);

        logger.log("Actual user data from account detail page");
        logger.log("Actual first name " + actualFirstname);
        logger.log("Actual last name " + actualLastName);
        logger.log("Actual dob " + actualDOB);
        logger.log("Actual salutation " + actualSalutation);
        logger.log("DOB translation " + this.translateDobToBhs(expectedDateOfBirth));
        logger.log(this.translateSalutation(expectedSalutation));
        Assertion.assertThat("Verify first name", actualFirstname, equalTo(expectedFirstName));
        Assertion.assertThat("Verify last name", actualLastName, equalTo(expectedLastName));
        Assertion.assertThat("Verify salutation",actualSalutation,anyOf(equalTo(expectedSalutation),equalTo(this.translateSalutation(expectedSalutation))));
        Assertion.assertThat("Verify dob", actualDOB, anyOf(equalTo(expectedDobBahasa),equalTo(expectedDateOfBirth)));
        return true;
    }

    @Override
    public void openHelpCenterPage(){
        driverTools.swipeToElement(HELP_CENTER_ENTRY_BUTTON, Swipe.UP, 20).orElseThrow();
        driverTools.click(HELP_CENTER_ENTRY_BUTTON);
        if(!driverTools.waitForElementToDisappear(HELP_CENTER_ENTRY_BUTTON,5)){
            driverTools.swipeUp();
            driverTools.click(HELP_CENTER_ENTRY_BUTTON);
        }
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("help-center");
        driverTools.waitForElementToAppear(HelpCenterPage.SKIP_BTN,10).ifPresent(driverTools::click);;
        driverTools.switchToNative();
    }

    @Override
    public void openWishlistPage(){
        WebElement wishlistButton = driverTools.swipeToElement(WISHLIST_ENTRY, Swipe.UP, 30).orElseThrow();
        driverTools.click(wishlistButton);
    }


    @Override
    public void openMyReviewPage(){
        driverTools.swipeToElement(YOUR_REVIEWS,Swipe.UP,10);
        if(!driverTools.isElementDisplayed(YOUR_REVIEWS)){
            driverTools.swipeToElement(YOUR_REVIEWS,Swipe.DOWN,10);
        }
        driverTools.click(YOUR_REVIEWS);
    }

    @Override
    public void openSmartProfilePage(){} // FIXME: Implementation pending
    @Override
    public void openLoyaltyPartnerPage(){
        driverTools.swipeToElement(LOYALTY_PARTNER_ENTRY_BTN,Swipe.UP,20);
        driverTools.click(LOYALTY_PARTNER_ENTRY_BTN);
    }
    
    @Override
    public boolean checkAccountName(String name){
        WebElement accountName = driverTools.waitForElementToAppear(ACCOUNT_FULL_NAME,10).orElseThrow();
        String accountDetailName = driverTools.getText(accountName);
        logger.log("Expected name : " + name);
        logger.log("Actual name : " + accountDetailName);
        boolean isAccountUsernameCorrect = accountDetailName.contains(name);
        return isAccountUsernameCorrect;
    }
    @Override
    public boolean checkAccountEmailAndName(String expectedEmail,String expectedName){
        String actualFirstname = driverTools.getText(ACCOUNT_DETAIL_FIRSTNAME).orElseThrow().strip();
        String actualEmail = driverTools.getText(ACCOUNT_DETAIL_EMAIL).orElseThrow().strip();
        boolean isEmailAndName = actualFirstname.equals(expectedName) && actualEmail.equals(expectedEmail.toLowerCase());

        logger.log("expected first name: " + expectedName);
        logger.log("actual first name: " + actualFirstname);
        logger.log("expected email: " + expectedEmail.toLowerCase());
        logger.log("actual email: " + actualEmail);

        return isEmailAndName;
    }

    @Override
    public boolean checkVerifiedPhone(){
        return false;
    }

    @Override
    public String userEditAccountDetails() {
        driverTools.swipeToElement(ACCOUNT_DETAIL_LASTNAME, Swipe.UP,2).ifPresent(driverTools::click);
        driverTools.clear(ACCOUNT_DETAIL_LASTNAME);
        String inputNewLastName = CoreFunctions.randomName()+ "aut";;
        driverTools.sendKeys(ACCOUNT_DETAIL_LASTNAME, inputNewLastName);
        driverTools.hideKeyboard(1);
        driverTools.swipeToElement(SAVE_BUTTON,Swipe.UP,5).ifPresent(driverTools::click);
        return inputNewLastName;
    }

    @Override
    public boolean verifyEditedAccountDetail(String expectedData) {
        driverTools.waitForElementToPresence(getUserNameLocator(expectedData)).orElseThrow();
        String[] nameBreakdown = driverTools.getText(getUserNameLocator(expectedData)).orElseThrow().split(" ");
        return nameBreakdown[1].equals(expectedData);
    }

    private Locator getUserNameLocator(String inputNewLastName) {
        return new Locator("FULL_USERNAME", AppiumBy.xpath("//*[contains(@text,'"+inputNewLastName+"')]"));
    }

    @Override
    public boolean isLoggedIn() {
        driverTools.waitForElementToPresence(EDIT_ACCOUNT_BUTTON,3);
        return driverTools.isElementPresent(EDIT_ACCOUNT_BUTTON);
    }

    @Override
    public boolean checkAccountEmailAndPhone(String expectedEmail, String phoneNumber) {
        driverTools.swipeToElement(ACCOUNT_DETAIL_PHONE_NUMBER,Swipe.DOWN,6);
        if (!driverTools.isElementDisplayed(ACCOUNT_DETAIL_PHONE_NUMBER))
            driverTools.swipeToElement(ACCOUNT_DETAIL_PHONE_NUMBER, Swipe.DOWN, 6);
        WebElement phoneNumberElement = driverTools.element(ACCOUNT_DETAIL_PHONE_NUMBER).orElseThrow();
        String actualPhoneNumber = driverTools.getValueAttribute(phoneNumberElement,"text");
        String actualEmail = driverTools.element(ACCOUNT_DETAIL_EMAIL).orElseThrow().getAttribute("text");
        String normalizedActualPhoneNumber = actualPhoneNumber.replaceAll("\\D", "");
        String normalizedExpectedPhoneNumber = phoneNumber.replaceAll("\\D", "");
        boolean phoneNumberMatch = normalizedActualPhoneNumber.endsWith(normalizedExpectedPhoneNumber)
                || normalizedExpectedPhoneNumber.endsWith(normalizedActualPhoneNumber);
        String normalizedActualEmail = actualEmail.toLowerCase();
        String normalizedExpectedEmail = expectedEmail.toLowerCase();
        boolean isEmailAndPhoneNumber = phoneNumberMatch && normalizedActualEmail.equals(normalizedExpectedEmail);
        logger.log("expected phone number: " + phoneNumber);
        logger.log("actual phone number: " + actualPhoneNumber);
        logger.log("normalized expected phone number: " + normalizedExpectedPhoneNumber);
        logger.log("normalized actual phone number: " + normalizedActualPhoneNumber);
        logger.log("expected email: " + normalizedExpectedEmail);
        logger.log("actual email: " + normalizedActualEmail);
        return isEmailAndPhoneNumber;
    }

    private void initSessionUserMapping(String username){
        List<String> usernames = new ArrayList<>(Arrays.asList(username));
        SESSION_USER_MAPPING.put(session.sessionId(), usernames);
    }

    private void updateSessionUserMapping(String username){
        List<String> usernames = SESSION_USER_MAPPING.get(session.sessionId());
        SESSION_USER_MAPPING.put(session.sessionId(), usernames);
    }

    @Override
    public void skipSavePassengerCoachMarkWithCondition(String username) {
        String sessionID = session.sessionId();
        List<String> usernameList = SESSION_USER_MAPPING.get(sessionID);
        if(usernameList != null && usernameList.contains(username)){
            logger.log("username: " + username + "already logged in in this session");
            logger.log("Skipping skip passenger coachmar step");
            return;
        } 
        Optional<WebElement> skipBtn = driverTools.waitForElementToAppear(SKIP_PASSENGER_DATA_COACH_MARK,3);
        Optional<WebElement> finishBtn = driverTools.waitForElementToAppear(FINISH_PASSENGER_DATA_COACH_MARK,3);
        if(skipBtn.isPresent()){
            driverTools.click(skipBtn.orElseThrow());
            driverTools.swipeDown(3);
        }else if(finishBtn.isPresent()){
            driverTools.click(finishBtn.orElseThrow());
            driverTools.swipeDown(3);
        }

        //initialize list if empty
        if(usernameList == null) initSessionUserMapping(username);
        if(usernameList!=null && usernameList.size()!=0) updateSessionUserMapping(username);
    }

    @Override
    public void openVoucherPage() {
       driverTools.swipeToElement(VOUCHER_BOX, Swipe.UP, 10);
       if(!driverTools.isElementDisplayed(VOUCHER_BOX)) driverTools.swipeToElement(VOUCHER_BOX, Swipe.DOWN, 10);
       driverTools.click(VOUCHER_BOX);
    }

    @Override
    public boolean isVoucherPageOpen() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("voucher-box");
        Boolean isVoucherBoxTitleVisible = driverTools.waitForElementToAppear(VOUCHER_BOX_TITLE,20).isPresent();
        Assertion.assertThat("Verify voucher box appear",isVoucherBoxTitleVisible,is(true));     
        driverTools.switchToNative();
        return true;
    }

    public void openOthers() {
       driverTools.swipeToElement(OTHERS_ENTRY_BTN, Swipe.UP,25);
       driverTools.click(OTHERS_ENTRY_BTN);
    }

    @Override
    public void openDeviceInfoMenu() {
        driverTools.waitForElementToAppear(DEVICE_INFO_BTN,3);
        driverTools.click(DEVICE_INFO_BTN);
    }

    @Override
    public boolean verifyDeviceInfoShown() {
        boolean isDeviceInfoHeaderIsShown = driverTools.waitForElementToAppear(DEVICE_INFORMATION_POPUP_HEADER).isPresent();
        boolean isDeviceInfoSubtitleShown = driverTools.waitForElementToAppear(DEVICE_INFORMATION_POPUP_SUBTITILE).isPresent();
        boolean isDeviceIdIsShown = driverTools.waitForElementToAppear(DEVICE_ID).isPresent();
        Assertion.assertThat("Verify deivce info popup header is shown", isDeviceInfoHeaderIsShown, is(true));
        Assertion.assertThat("Verify deivce info subtitle is shown", isDeviceInfoSubtitleShown, is(true));
        Assertion.assertThat("Verify device id is shown", isDeviceIdIsShown, is(true));
        return true;
    }

    @Override
    public boolean verifyCopyDeviceInfoButton() {
        driverTools.click(COPY_DEVICE_ID);
        boolean isCopySnackBarAppear = driverTools.waitForElementToAppear(DEVICE_ID_COPIED_SNACKBAR,2).isPresent();
        Assertion.assertThat("Verify success copy snackbar appear", isCopySnackBarAppear, is(true));
        return true;
    }

    @Override
    public boolean verifyLoyaltyPointValue(String expectedValue) {
        driverTools.swipeDown(2);
        String actualLoyaltyValue = driverTools.getText(LOYALTY_POINT_VALUE).orElseThrow();
        Assertion.assertThat("Verify loyalty value correct", actualLoyaltyValue.replaceAll("[^0-9]",""), equalTo(expectedValue));
        return true;
    }

    @Override
    public String  getLoyaltyPointValue() {
        WebElement e = driverTools.swipeToElement(LOYALTY_POINT_VALUE,Swipe.DOWN,10).orElseThrow();
        return driverTools.getText(e);
    }

    @Override
    public void clickLoyaltyBanner() {
        driverTools.click(LOYALTY_LOCKED_BANNER);
    }

    @Override
    public boolean canClickTiketLoyaltyBanner() {
        return driverTools.isElementDisplayed(LOYALTY_CONTENT_POPUP);
    }

    @Override
    public void clickLoginRegisterButtonLoyaltyPopup() {
        driverTools.click(LOYALTY_LOGIN_REGISTER_BUTTON);
    }

    @Override
    public String getAccountName() {
        WebElement accountFullNameElement = driverTools.swipeToElement(ACCOUNT_FULL_NAME,Swipe.DOWN,5).orElseThrow();
        return driverTools.getText(accountFullNameElement);
    }

    @Override
    public void  openTermAndCondition(){
        driverTools.click(TERM_AND_CONDITION);
    }

    @Override
    public void openPrivacyPolicyMenu() {
        driverTools.swipeToElement(PRIVACY_POLICY_MENU, Swipe.UP, 10);
        if(!driverTools.isElementDisplayed(PRIVACY_POLICY_MENU)) driverTools.swipeToElement(PRIVACY_POLICY_MENU, Swipe.DOWN, 10);
        driverTools.click(PRIVACY_POLICY_MENU);
    }

    @Override
    public void openAboutTiketDotCom(){
        WebElement element = driverTools.swipeToElement(ABOUT_TIKETDOTCOM, Swipe.UP, 20).orElseThrow();
        driverTools.click(element);
    }

    @Override
    public boolean verifyAboutTiketDotCom(){
        WebElement element = driverTools.waitForElementToAppear(ABOUT_TIKETDOTCOM_TOOLBAR, 10).orElseThrow();
        boolean backButton = driverTools.isElementDisplayed(BACK_BTN_ABOUT_MENU);
        logger.log("back button shown" + backButton);
        boolean toolBar = driverTools.isElementDisplayed(element);
        logger.log("tool bar shown" + toolBar);
        return backButton && toolBar;
    }

    @Override
    public void clickSeeAllPayment() {
        driverTools.swipeDown(4);
        driverTools.swipeToElement(SEE_ALL_PAYMENT,Swipe.UP,10);
        driverTools.click(SEE_ALL_PAYMENT);
    }

    @Override
    public void clickAddBcaCard() {
        driverTools.click(ADD_BCA_CARD);
    }

    @Override
    public boolean verifyBCAForm(String email) {
        boolean isEmailPresent =  driverTools.waitForElementToAppear(getEmailLocator(email),15).isPresent();
        logger.log("is email "+email+" present: "+isEmailPresent);
        boolean isCardLimitShown = driverTools.isElementDisplayed(BCA_LIMIT_FIELD);
        logger.log("is card limit field shown: "+isCardLimitShown);
        boolean isButtonContinueShown = driverTools.isElementDisplayed(BCA_CONTINUE_BUTTON);
        logger.log("is continue button shown: "+isButtonContinueShown);

        return isEmailPresent && isCardLimitShown && isButtonContinueShown;
    }

    private Locator getEmailLocator(String email) {
        return new Locator(email,AppiumBy.xpath("//*[contains(@text,'"+email+"')]"));
    }

    private boolean checkCardNumberEditable(long number) {
        String numberStr = Long.toString(number);
        driverTools.sendKeys(BCA_CARD_FIELD, numberStr);
        driverTools.hideKeyboard(1);
        String formattedNumber = numberStr.substring(0, 4) + " " +
                numberStr.substring(4, 8) + " " +
                numberStr.substring(8, 12) + " " +
                numberStr.substring(12);
        logger.log("looking for filled field with: " + formattedNumber);
        Locator fieldFilled = new Locator(formattedNumber, AppiumBy.xpath("//*[contains(@text,'" + formattedNumber + "')]"));
        return driverTools.isElementDisplayed(fieldFilled);
    }

    public void clickEditPhoneAndEmailBtn() {
        driverTools.swipeToElement(EDIT_EMAIL_PHONE_BTN, Swipe.UP, 3);
        driverTools.click(EDIT_EMAIL_PHONE_BTN);
    }

    @Override
    public String getAccountDetailPhoneNumber() {
        driverTools.swipeToElement(ACCOUNT_DETAIL_PHONE_NUMBER, Swipe.UP, 3);
        if (!driverTools.isElementDisplayed(ACCOUNT_DETAIL_PHONE_NUMBER))
            driverTools.swipeToElement(ACCOUNT_DETAIL_PHONE_NUMBER, Swipe.DOWN, 3);
        String removeCountryCode = driverTools.getText(ACCOUNT_DETAIL_PHONE_NUMBER).orElseThrow().replace("+62","");
        return removeCountryCode;
    }

    // @Override //TODO fix this
    // public void changePhoneNumberDetailStep(String newPhoneNumber) {
    //     driverTools.click(EDIT_PHONE_BTN);
    //     driverTools.click(CONTINUE_EDIT_PHONE_NUMBER_CONFIRMATION_BTN);
    //     driverTools.click(SEND_ACCOUNT_OWNERSHIP_OTP_TO_PHONE);
    //     LoginPageWeb<T> loginPage = LoginPageWeb.getInstance(session);
    //     loginPage.inputOtp();
    //     driverTools.click(NEW_PHONE_NUMBER_INPUT_FIELD);
    //     driverTools.clear(NEW_PHONE_NUMBER_INPUT_FIELD);
    //     driverTools.sendKeys(NEW_PHONE_NUMBER_INPUT_FIELD, "+62"+newPhoneNumber);
    //     driverTools.hideKeyboard(3);
    //     driverTools.click(VERIFY_NEW_PHONE_NUMBER_BTN);
    //     loginPage.inputOtp();
    // }

    @Override
    public String getPhoneNumberInChangeEmailOrPhonePage() {
        driverTools.waitForElementToAppear(CURRENT_PHONE_NUMBER_VALUE,10);
        String removeCountryCode = driverTools.getText(CURRENT_PHONE_NUMBER_VALUE).orElseThrow().replace("+62", "");
        return removeCountryCode;
    }

    @Override
    public void closeChangeEmailOrPhonePage() {
        driverTools.click(BACK_BUTTNON_ON_CHANGE_EMAIL_OR_PHONE_PAGE);
    }

    @Override
    public void refresh() {
        driverTools.waitForElementToAppear(ACCOUNT_NAVIGATION,4);
            driverTools.swipeDown();
    }

    @Override
    public void openReferralPage(){
       WebElement referralBtn = driverTools.swipeToElement(REFERRAL_BTN,Swipe.UP,3).orElseThrow();
       driverTools.swipeUp();
       driverTools.click(referralBtn);
    }

    @Override
    public void openCurrencyPage(){
        driverTools.swipeToElement(CURRENCY_PAGE, Swipe.UP, 10).orElseThrow();
        driverTools.click(CURRENCY_PAGE);
    }

    @Override
    public void openPaylaterPage() {
       driverTools.swipeToElement(PAYLATER_ENTRY_POINT, Swipe.UP, 10);
       if(!driverTools.isElementDisplayed(PAYLATER_ENTRY_POINT)) driverTools.swipeToElement(PAYLATER_ENTRY_POINT, Swipe.DOWN, 10);
       driverTools.click(PAYLATER_ENTRY_POINT);
    }

    @Override
    public boolean verifyPaylaterPageOpen() {
        driverTools.waitForElementToAppear(PAYLATER_CAROUSEL,3).orElseThrow();
        Assertion.assertThat("Verify paylater carousel appear", driverTools.isElementDisplayed(PAYLATER_CAROUSEL), is(true));
        Assertion.assertThat("Verify paylater limit container", driverTools.isElementDisplayed(PAYLATER_LIMIT_CONTAINER), is(true));
        Assertion.assertThat("Verify activate paylater button appear", driverTools.isElementDisplayed(ACTIVATE_PAYLATER_BTN), is(true));
        return true;
    }

    @Override
    public boolean verifyIsOnAccountPage(){
        return driverTools.isElementDisplayed(EDIT_ACCOUNT_BUTTON);
    }

    @Override
    public boolean verifyBottomSheetIntroducingFeatureForMemberOnly(){
        return driverTools.isElementDisplayed(BOTTOM_SHEET_INTRODUCING_FEATURE_FOR_MEMBER_ONLY);
    }

    @Override
    public void clickLaterCTABtn(){
        driverTools.click(LATER_CTA_BTN);
    }

    @Override
    public boolean verifyBottomSheetDisappear(){
        return driverTools.waitForElementToDisappear(BOTTOM_SHEET_INTRODUCING_FEATURE_FOR_MEMBER_ONLY);
    }

    @Override
    public String getAccountDetailEmail() {
        WebElement email = driverTools.swipeToElement(ACCOUNT_DETAIL_EMAIL,Swipe.DOWN,5).orElseThrow();
       return driverTools.getText(email);
    }

    @Override
    public void clickEditProfileDetail() {
        driverTools.click(EDIT_PROFILE_DETAILS);
    }

    @Override
    public boolean checkAccountEntity(String entity) {
        Locator entityLocator = getEntityLocator(entity);
        WebElement entityElement = driverTools.swipeToElement(entityLocator, Swipe.DOWN, 6).orElseThrow();
        return driverTools.isElementDisplayed(entityElement);
    }

    private Locator getEntityLocator(String entity) {
        return new Locator(entity, AppiumBy.xpath("//*[contains(@text,'"+entity+"')]"));
    }

    @Override
    public void openAccountPageUsingDeeplink(){
        Deeplink deeplinkAccountPage = new Deeplink("ACCOUNT_DEEPLINK","tiket://m.gatotkaca.tiket.com/myaccount","tiket://m.preprod.tiket.com/myaccount","tiket://m.tiket.com/myaccount");
        try {
            driverTools.deeplink(deeplinkAccountPage);
        }catch (Exception e){
            logger.log(e.getLocalizedMessage());
        }
        driverTools.swipeDown(1);
    }

    @Override
    public void openAccountPageUsingDeeplink(String countryCode, String currency) {
        String localePath = "en-" + countryCode.toLowerCase();
        Deeplink deeplinkAccountPage = new Deeplink(
                "ACCOUNT_DEEPLINK_" + localePath + "_" + currency,
                "tiket://m.gatotkaca.tiket.com/" + localePath + "/myaccount?currency=" + currency,
                "tiket://m.preprod.tiket.com/" + localePath + "/myaccount?currency=" + currency,
                "tiket://m.tiket.com/" + localePath + "/myaccount?currency=" + currency);
        try {
            driverTools.deeplink(deeplinkAccountPage);
        } catch (Exception e) {
            logger.log(e.getLocalizedMessage());
        }
    }

    @Override
    public String getSelectedCountry() {
        driverTools.swipeToElement(COUNTRY_TEXT, Swipe.UP, 10).orElseThrow();
        return driverTools.getText(COUNTRY_TEXT).orElseThrow();
    }

    @Override
    public String getSelectedCurrency() {
        driverTools.swipeToElement(CURRENCY_TEXT, Swipe.UP, 10).orElseThrow();
        return driverTools.getText(CURRENCY_TEXT).orElseThrow();
    }

    @Override
    public boolean isAccountPageDisplayed() {
        return driverTools.waitForElementToAppear(LOGIN_CONTAINER, 10).isPresent()
                || driverTools.isElementDisplayed(COUNTRY_BUTTON);
    }
}
