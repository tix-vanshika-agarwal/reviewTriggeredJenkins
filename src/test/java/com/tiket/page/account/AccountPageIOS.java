package com.tiket.page.account;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPageIOS;
import com.tiket.page.utilities.message.help_center.HelpCenterPage;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

import static com.tiket.core.CoreFunctions.log;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class AccountPageIOS <T extends WebDriver> extends AccountPage<T> {

    private static final Locator ENGLISH_RADIO_BUTTON = new Locator("englishRadioButton", AppiumBy.xpath("//XCUIElementTypeOther[@name=\"container_english_radio_button\"]//*[contains(@name,'radioButton')]"));
    private static final Locator BAHASA_RADIO_BUTTON = new Locator("bahasaRadioButton", AppiumBy.xpath("//XCUIElementTypeOther[@name=\"container_bahasa_radio_button\"]//*[contains(@name,'radioButton')]"));
    private static final Locator CURRENT_SELECTED_LANGUAGE = new Locator("CURRENT_SELECTED_LANGUAGE", AppiumBy.name("current_language"));
    private static final Locator BACK_BUTTON = new Locator("Back Button", AppiumBy.accessibilityId("tds ic back"));
    private static final Locator BACK_BUTTNON_ON_CHANGE_EMAIL_OR_PHONE_PAGE = new Locator("BACK_BUTTON_ON_CHANGE_EMAIL_OR_PHONE_PAGE", AppiumBy.accessibilityId("back"));
    private static final Locator SETTING_TOP_TAB = new Locator("SETTING_TOP_TAB", AppiumBy.accessibilityId("settings_header"));
    private static final Locator CROSS_BUTTON = new Locator("Cross Button", AppiumBy.accessibilityId("tds ic cross big"));
    private static final Locator EDIT_ACCOUNT_BUTTON = new Locator("EDIT_ACCOUNT_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name=\"Edit profile detail\" or @name=\"Edit detail akun\"]"));
    public static final Locator FLAG_TOP_OF_ACCOUNT_PAGE = new Locator("FLAG_TOP_OF_ACCOUNT_PAGE", AppiumBy.xpath("//*[contains(@name,'tds ic chevron right') or contains(@name,'login_button')]"));
    private static final Locator ACCOUNT_DETAIL_FIRSTNAME = new Locator("ACCOUNT_DETAIL_FIRSTNAME", AppiumBy.accessibilityId("accountFirstName"));
    private static final Locator ACCOUNT_DETAIL_EMAIL = new Locator("ACCOUNT_DETAIL_EMAIL", AppiumBy.xpath("//*[contains(@label,'@')]"));
    private static final Locator ACCOUNT_DETAIL_LASTNAME = new Locator("ACCOUNT_DETAIL_LASTNAME", AppiumBy.accessibilityId("accountLastName"));
    private static final Locator ACCOUNT_DETAIL_DOB = new Locator("ACCOUNT_DETAIL_DOB", AppiumBy.accessibilityId("accountBirthdate"));
    private static final Locator ACCOUNT_DETAIL_SALUTATION = new Locator("ACCOUNT_DETAIL_SALUTATION", AppiumBy.xpath("//*[contains(@name,'(selected)')]//following-sibling::*"));
    private static final Locator ACCOUNT_FULL_NAME = new Locator("ACCOUNT_FULL_NAME", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name, 'Edit')]//preceding-sibling::XCUIElementTypeStaticText"));
    private static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON", AppiumBy.id("ic cancel grey"));
    private static final Locator ACTIVATE_FACE_ID_LATER_BUTTON = new Locator("ACTIVATE_FACE_ID_LATER_BUTTON", AppiumBy.accessibilityId("activate_face_id_later"));
    private static final Locator SAVE_PASSENGERS_DATA = new Locator("SAVE_PASSENGERS_DATA", AppiumBy.accessibilityId("add_smart_profile"));
    private static final Locator EDIT_EMAIL_AND_PHONE_NUMBER = new Locator("EDIT_EMAIL_AND_PHONE_NUMBER",AppiumBy.accessibilityId("edit"));
    private static final Locator EDIT_PHONE_BTN = new Locator("EDIT_PHONE_BTN", AppiumBy.xpath("(//XCUIElementTypeButton[@label='Ganti' or @label='Change'])[1]"));
    private static final Locator CONTINUE_EDIT_PHONE_NUMBER_CONFIRMATION_BTN = new Locator("CONTINUE_EDIT_PHONE_NUMBER_CONFIRMATION_BTN", AppiumBy.xpath("//XCUIElementTypeButton[@label='Lanjut' or @label='Continue']"));
    private static final Locator VERIFIED_PHONE = new Locator("VERIFIED_PHONE",AppiumBy.xpath("//*[@label='Verified' or @label='Sudah diverifikasi' and ./parent::*[./parent::*[./parent::*[./parent::*[(./preceding-sibling::* | ./following-sibling::*)[contains(@label,'+62 ')]]]]]]"));
    private static final Locator LANGUAGE_MENU = new Locator("LANGUAGE_MENU",AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Language' or @label='Bahasa']"));
    private static final Locator POPUP_PIN = new Locator("POPUP_PIN",AppiumBy.xpath("//XCUIElementTypeImage//following::XCUIElementTypeButton"));
    public static final Locator SETTING_BUTTON = new Locator("SETTING_BUTTON",AppiumBy.accessibilityId("setting"));
    public static final Locator EDIT_PROFILE = new Locator("EDIT_PROFILE",AppiumBy.xpath("//XCUIElementTypeButton[@label='Edit profile detail' or @label='Edit detail akun']"));
    private static final Locator LOGIN_BUTTON= new Locator("LOGIN_BUTTON",AppiumBy.xpath("(//XCUIElementTypeButton[@name=\"login_button\"])[1]"));
    private static final Locator SAVE_BTN= new Locator("SAVE_BTN",AppiumBy.id("save_btn"));
    private static final Locator LANGUAGE_MENU_TEXT= new Locator("LANGUAGE_MENU_TEXT",AppiumBy.id("language_label"));
    private static final Locator LOCATION_MENU_TEXT= new Locator("LOCATION_MENU_TEXT",AppiumBy.xpath("//XCUIElementTypeImage[@name='tds_ic_pointer']/following-sibling::XCUIElementTypeStaticText[1]"));
    private static final Locator CURRENCY_MENU_TEXT= new Locator("CURRENCY_MENU_TEXT",AppiumBy.id("currency_label"));
    private static final Locator ACCOUNT_DETAIL_PHONE_NUMBER = new Locator("ACCOUNT_DETAIL_PHONE_NUMBER", AppiumBy.xpath("(//*[*[*[contains(@label,'To Account') or contains(@label,'Ke Pusat Akun')]]]/XCUIElementTypeStaticText)[2]"));
    private static final Locator SKIP_PASSENGER_DATA_COACH_MARK = new Locator("SKIP_PASSENGER_DATA_COACH_MARK", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Skip' or @label='Lewati']"));
    private static final Locator HELP_CENTER_ENTRY = new Locator("HELP_CENTER",AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label, 'Help Center') or contains(@label, 'Pusat Bantuan')]"));
    private static final Locator WISHLIST_ENTRY = new Locator("WISHLIST_ENTRY", AppiumBy.xpath("//*[@name='Wishlist']"));
    private static final Locator WISHLIST_PAGE = new Locator("WISHLIST_PAGE", AppiumBy.id("Wishlist Landing"));
    private static final Locator FINISH_PASSENGER_DATA_COACH_MARK = new Locator("FINISH_PASSENGER_DATA_COACH_MARK", AppiumBy.xpath("//XCUIElementTypeButton[@label='Selesai' or @label='Finish' or @label='Done']"));
    private static final Locator VOUCHER_BOX = new Locator("VOUCHER", AppiumBy.xpath("//*[contains(@label,\"Voucher\")][1]"));
    private static final Locator VOUCHER_BOX_TITLE = new Locator("VOUCHER_BOX_TITLE", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Voucher Box']"));
    private static final Locator LOYALTY_PARTNER_ENTRY_BTN = new Locator("LOYALTY_PARTNER_ENTRY_BTN", AppiumBy.xpath("//*[@name='Partner Loyalitas' or @name='Loyalty Partnership']"));
    private static final Locator COUNTRY_BUTTON = new Locator("COUNTRY_BUTTON", AppiumBy.iOSClassChain("**/*[`label=='Negara' or label=='Country'`]"));
    private static final Locator SETTINGS_SECTION_TITLE = new Locator("SETTINGS_SECTION_TITLE", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Settings' or @name='Settings']"));
    private static final Locator SETTINGS_CURRENCY_VALUE = new Locator("SETTINGS_CURRENCY_VALUE", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Currency' or @name='Currency' or @label='Mata Uang' or @name='Mata Uang']/preceding-sibling::XCUIElementTypeStaticText[string-length(@label)=3 and translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz','')='']"));
    private static final Locator SETTINGS_COUNTRY_VALUE = new Locator("SETTINGS_COUNTRY_VALUE", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Country' or @name='Country' or @label='Negara' or @name='Negara']/preceding-sibling::XCUIElementTypeStaticText[string-length(@label) > 3 and @label != 'English' and @label != 'Language' and @label != 'Currency' and @label != 'Mata Uang'][1]"));

    //these element below are in account page which open after clicking edit phone/email btn inside account detail page
    private static final Locator SEND_ACCOUNT_OWNERSHIP_OTP_TO_PHONE = new Locator("SEND_ACCOUNT_OWNERSHIP_OTP_TO_PHONE", AppiumBy.iOSClassChain("**/XCUIElementTypeImage[`name == \"tds_ic_chevron_right\"`][1]"));
    private static final Locator NEW_PHONE_NUMBER_INPUT_FIELD = new Locator("NEW_PHONE_NUMBER_INPUT_FIELD", AppiumBy.accessibilityId("countryCode"));
    private static final Locator VERIFY_NEW_PHONE_NUMBER_BTN = new Locator("VERIFY_NEW_PHONE_NUMBER_BTN", AppiumBy.xpath("//XCUIElementTypeButton[@label='Verifikasi nomor ponsel' or @label='Verify phone number']"));
    private static final Locator CURRENT_PHONE_NUMBER_VALUE = new Locator("CURRENT_PHONE_NUMBER_VALUE", AppiumBy.xpath("(//XCUIElementTypeButton[@label='Ganti' or @label='Change'])[1]//preceding-sibling::*"));
    
    //loyalty poin
    //TODO : request unique locator
    private static final Locator LOYALTY_POINT_VALUE = new Locator("LOYALTY_POINT_VALUE",AppiumBy.xpath("//*[contains(@label,'Tier')]//preceding-sibling::XCUIElementTypeStaticText[1]"));
    private static final Locator LOYALTY_LOCKED_BANNER = new Locator("LOYALTY_LOCKED_BANNER", AppiumBy.xpath("//*[contains(@label,'Blibli Tiket Rewards')]"));
    private static final Locator LOYALTY_CONTENT_POPUP = new Locator("LOYALTY_CONTENT_POPUP", AppiumBy.xpath("//*[contains(@label,'Blibli Tiket Point')]//parent::*//parent::XCUIElementTypeOther"));
    private static final Locator LOYALTY_LOGIN_REGISTER_BUTTON = new Locator("LOYALTY_LOGIN_REGISTER_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'Log in') and @visible='true']"));
    //others
    private static final Locator OTHERS_ENTRY_BTN = new Locator("OTHERS_ENTRY_BTN", AppiumBy.id("others"));
    private static final Locator DEVICE_INFO_BTN = new Locator("DEVICE_INFO_BTN", AppiumBy.accessibilityId("tds_ic_phone"));
    private static final Locator DEVICE_INFORMATION_POPUP_HEADER = new Locator("DEVICE_INFORMATION_POPUP_HEADER", AppiumBy.id("device_info_title"));
    private static final Locator DEVICE_ID = new Locator("DEVICE_ID", AppiumBy.xpath("//*[@label = 'Device ID:']//preceding-sibling::*//XCUIElementTypeStaticText"));
    private static final Locator COPY_DEVICE_ID = new Locator("COPY_DEVICE_ID", AppiumBy.id("copy_device_id_btn"));
    private static final Locator DEVICE_ID_COPIED_SNACKBAR = new Locator("COPY_DEVICE_ID",  AppiumBy.id("id_copied_snackbar"));
    private static final Locator  CHECK_NOT_VERIFIED_EMAIL = new Locator("CHECK_NOT_VERIFIED_EMAIL", AppiumBy.accessibilityId("tds_ic_warning"));
    private static final Locator  CHANGE_OR_VERIFY_BTN = new Locator("CHANGE_OR_VERIFY_BTN", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'.com')]//following-sibling::XCUIElementTypeButton"));
    private static final Locator  CHANGE_CONTINUE_BTN = new Locator("CHANGE_OR_VERIFY_BTN", AppiumBy.xpath("//XCUIElementTypeButton[@label='Continue' or @label='Lanjutkan']"));
    private static final Locator  USING_PHONE_TO_VERIFY = new Locator("USING_PHONE_TO_VERIFY", AppiumBy.xpath("(//XCUIElementTypeImage[@name='tds_ic_chevron_right'])[1]"));
    private static final Locator  NEW_EMAIL_FIELD = new Locator("NEW_EMAIL_FIELD", AppiumBy.accessibilityId("emailField"));
    private static final Locator  CURRENCY_PAGE = new Locator("CURRENCY_PAGE", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and contains(@name, 'Currency') or contains(@name, 'Mata Uang')]"));
    private static final Locator TERM_AND_CONDITION_PAGE = new Locator("TERM_AND_CONDITION_PAGE", AppiumBy.name("banner"));
    private static final Locator TERM_AND_CONDITION = new Locator("TERM_AND_CONDITION", AppiumBy.accessibilityId("tds_ic_terms_and_condition"));

    private static final Locator PRIVACY_POLICY_MENU = new Locator("PRIVACY_POLICY_MENU", AppiumBy.xpath("//*[@name=\"Kebijakan Privasi\" or @name='Privacy Policy']"));
    private static final Locator PRIVACY_POLICY_TOOLBAR = new Locator("PRIVACY_POLICY_TOOLBAR", AppiumBy.name("banner"));
    private static final Locator PRIVACY_POLICY_BACK_BUTTON = new Locator("PRIVACY_POLICY_BACK_BUTTON", AppiumBy.name("tds ic back"));
    private static final Locator ABOUT_TIKETDOTCOM_TOOLBAR = new Locator("ABOUT_TIKETDOTCOM_TOOLBAR", AppiumBy.name("settings_header" ));
    private static final Locator ABOUT_TIKETDOTCOM = new Locator("ABOUT_TIKETDOTCOM", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'About tiket.com') or contains(@name,'Tentang tiket.com')]"));
    private static final Locator SEE_ALL_PAYMENT = new Locator("SEE_ALL_PAYMENT", AppiumBy.name("see_all_payment"));
    private static final Locator ADD_BCA_CARD = new Locator("ADD_BCA_CARD", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'+')]" ));
    private static final Locator BCA_CARD_FIELD = new Locator("BCA_CARD_FIELD", AppiumBy.xpath("//XCUIElementTypeOther[@label='NOMOR KARTU']//following-sibling::XCUIElementTypeTextField[@enabled='true']" ));
    private static final Locator BCA_LIMIT_FIELD = new Locator("BCA_LIMIT_FIELD", AppiumBy.xpath("//XCUIElementTypeOther[@label='LIMIT HARIAN PER KARTU']//following-sibling::XCUIElementTypeTextField[@value='maks. Rp 3.000.000']" ));
    private static final Locator BCA_PHONE_FIELD = new Locator("BCA_PHONE_FIELD", AppiumBy.xpath("//XCUIElementTypeOther[@label='PILIH NOMOR HP m-BCA UNTUK KONFIRMASI REGISTRASI']//following-sibling::XCUIElementTypeOther[@value='Pilih nomor HP m-BCA']" ));
    private static final Locator BCA_CONTINUE_BUTTON = new Locator("BCA_CONTINUE_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@label='Lanjut']" ));
    private static final Locator YOUR_REVIEWS = new Locator("YOUR_REVIEWS", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Your Review' or @label='Kumpulan Review-mu']" ));
    //referral page
    private static final Locator REFERRAL_BTN = new Locator("REFERRAL_BTN",AppiumBy.id("referral"));
    public static final Locator BLIBLI_PAYLATER_ENTRY_ON_MY_ACCOUNT = new Locator("BLIBLI_PAYLATER_ENTRY_ON_MY_ACCOUNT", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`name == \"blibli_paylater\"`]"));
    private static final Locator PAYLATER_CAROUSEL = new Locator("PAYLATER_CAROUSEL", AppiumBy.xpath("//*[@name='PayLater week is here' or @name='PayLater telah tiba']"));
    private static final Locator ACTIVATE_PAYLATER_BTN = new Locator("ACTIVATE_PAYLATER_BTN", AppiumBy.xpath("//*[@name='Activate to claim benefits' or @name='Aktivasi untuk klaim benefit']"));
    private static final Locator PAYLATER_LIMIT_CONTAINER = new Locator("PAYLATER_LIMIT_CONTAINER", AppiumBy.xpath("//*[@name='Get a limit of up to' or @name='Dapetin limit hingga']"));
    private static final Locator BOTTOM_SHEET_INTRODUCING_FEATURE_FOR_MEMBER_ONLY = new Locator("BOTTOM_SHEET_INTRODUCING_FEATURE_FOR_MEMBER_ONLY", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'This is a member-only feature') or contains(@label,'Fitur ini hanya untuk anggota')]"));
    private static final Locator LATER_CTA_BTN = new Locator("LATER_CTA_BTN", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'Later') or contains(@label,'Nanti aja')]"));
    private static final Locator AFFILIATE_TITLE = new Locator("AFFILIATE_TITLE", AppiumBy.xpath("//*[@name='Affiliate' or @name='Afiliasi']"));
    private static  Map<String, List<String>> SESSION_USER_MAPPING = new HashMap<>();

    public AccountPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public boolean clickContinueAsGuest(){
        return driverTools.waitForElementToPresence(LOGIN_BUTTON, 5).isPresent();
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
        final int SWIPE_TO_COUNTRY_COUNT = 15;
        HomePage<T> homePage = HomePage.getInstance(this.session);
        homePage.openAccount();
        WebElement countryButton = driverTools.swipeToElement(COUNTRY_BUTTON, Swipe.UP, SWIPE_TO_COUNTRY_COUNT).orElseThrow();
        driverTools.click(countryButton);
        WebElement countryOption = driverTools.swipeToElement(countryOption(country), Swipe.UP, 3).orElseThrow();
        driverTools.click(countryOption);
        Assertion.assertThat("Verify country text is shown as expected after selecting country", driverTools.swipeToElement(countryText(country), Swipe.UP, SWIPE_TO_COUNTRY_COUNT).isPresent(), is(equalTo(true)));
    }

    private static Locator countryOption(String country) {
        return new Locator("COUNTRY_OPTION_" + country.toUpperCase(), AppiumBy.xpath("//*[contains(@name,'radioButton')]//preceding-sibling::*[@label='" + country + "']"));
    }

    private static Locator countryText(String country) {
        return new Locator("COUNTRY_TEXT_" + country.toUpperCase(), AppiumBy.xpath("//*[@label='Country' or @label='Negara']/parent::XCUIElementTypeCell/*[@label='" + country + "']"));
    }

    @Override
    public boolean isAccountDetailOpened() {
        return false; //FIXME: Pending implementation
    }

    @Override
    public void openSettings() {
        boolean isLogin = driverTools.isElementDisplayed(LOGIN_BUTTON);
        driverTools.click(SETTING_BUTTON);
        if(!isLogin){
            if(driverTools.waitForElementToAppear(POPUP_PIN,5).isPresent()){
                driverTools.clickElementByCoordinate(SETTING_TOP_TAB);
            }
        }
        driverTools.waitForElementToAppear(BACK_BUTTON).orElseThrow();
    }

    @Override
    public void selectLanguage(String language) {
        if("ENG".equalsIgnoreCase(language.trim()))
            selectEnglish();
        else
            selectBahasa();
    }

    @Override
    public void clickBackButton() {
        driverTools.click(BACK_BUTTON);
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

        if("ENG".equalsIgnoreCase(language.trim())){
            log("ENG ->" + text);
            return text.equals("Language");}
        else{
            log("Bahasa-->" +text);
            return text.equals("Bahasa");
        }
    }

    private void selectEnglish() {
        clickLanguageMenu();
        WebElement englishRadioButton = driverTools.waitForElementToPresence(ENGLISH_RADIO_BUTTON).orElseThrow();
        driverTools.click(englishRadioButton);
    }

    private void selectBahasa() {
        clickLanguageMenu();
        driverTools.waitForElementToAppear(BAHASA_RADIO_BUTTON);
        driverTools.click(BAHASA_RADIO_BUTTON);
    }

    private void clickLanguageMenu(){
        driverTools.swipeToElement(LANGUAGE_MENU,Swipe.UP,25).orElseThrow();
        driverTools.swipeUp();
        WebElement languageMenu = driverTools.waitForElementToPresence(LANGUAGE_MENU).orElseThrow();
        driverTools.click(languageMenu);
        if (driverTools.isElementDisplayed(LANGUAGE_MENU)) driverTools.clickElementByCoordinate(LANGUAGE_MENU);
    }

    private boolean isEnglishSelected() {
        driverTools.swipeToElement(LANGUAGE_MENU,Swipe.UP,25);
        boolean languageSelected = driverTools.getText(CURRENT_SELECTED_LANGUAGE).orElseThrow().contains("English");
        boolean languageMenuText = driverTools.getValueAttribute(LANGUAGE_MENU_TEXT,"label").equalsIgnoreCase("language");
        logger.log("" + languageSelected + languageMenuText );

        return languageSelected && languageMenuText;
    }

    private boolean isBahasaSelected() {
        driverTools.swipeToElement(LANGUAGE_MENU,Swipe.UP,25);
        boolean languageSelected = driverTools.getText(CURRENT_SELECTED_LANGUAGE).orElseThrow().contains("Indonesia");
        boolean languageMenuText = driverTools.getValueAttribute(LANGUAGE_MENU_TEXT,"label").equalsIgnoreCase("bahasa");
        logger.log("" + languageSelected + languageMenuText );

        return languageSelected && languageMenuText ;
    }

    @Override
    public boolean checkAccountName(String name){
        driverTools.waitForElementToPresence(EDIT_ACCOUNT_BUTTON,5).orElseThrow();
        String accountDetailName = driverTools.getValueAttribute(ACCOUNT_FULL_NAME,"label");
        logger.log("Expected name : " + name);
        logger.log("Actual name : " + accountDetailName);
        return accountDetailName.contains(name);
    }

    @Override
    public boolean checkAccountEmailAndName(String expectedEmail,String expectedName){
        String actualFirstname = driverTools.element(ACCOUNT_DETAIL_FIRSTNAME).orElseThrow().getAttribute("value");
        String actualEmail = driverTools.element(ACCOUNT_DETAIL_EMAIL).orElseThrow().getAttribute("value");
        boolean isEmailAndName = actualFirstname.equals(expectedName)&&actualEmail.equals(expectedEmail.toLowerCase());

        logger.log("expected first name: " + expectedName);
        logger.log("actual first name: " + actualFirstname);
        logger.log("expected email: " + expectedEmail.toLowerCase());
        logger.log("actual email: " + actualEmail);

        return isEmailAndName;
    }

    @Override
    public void clickAccountDetail(){
        driverTools.swipeToElement(EDIT_ACCOUNT_BUTTON,Swipe.DOWN,5);
        driverTools.clickElementByCoordinate(EDIT_ACCOUNT_BUTTON);
    }

    @Override
    public boolean verifyAccountDetail(String expectedFirstName,String expectedLastName,String expectedDateOfBirth, String expectedSalutation){
        String actualFirstname =  driverTools.getText(ACCOUNT_DETAIL_FIRSTNAME).orElseThrow();
        String actualLastName =  driverTools.getText(ACCOUNT_DETAIL_LASTNAME).orElseThrow();
        String actualDOB = driverTools.getText(ACCOUNT_DETAIL_DOB).orElseThrow();
        String actualSalutation = driverTools.getText(ACCOUNT_DETAIL_SALUTATION).orElseThrow();


        logger.log("Actual user data from account detail page");
        logger.log("Actual first name " + actualFirstname);
        logger.log("Actual last name " + actualLastName);
        logger.log("Actual dob " + actualDOB);
        logger.log("Actual salutation " + actualSalutation);
        logger.log("DOB translation " + this.translateDobToBhs(expectedDateOfBirth));
        logger.log(this.translateSalutation(expectedSalutation));
        logger.log("first -> " + (actualFirstname.equals(expectedFirstName) && actualLastName.equals(expectedLastName)));
        logger.log("second -> "+ (actualSalutation.equals(expectedSalutation)
        || actualSalutation.equals(this.translateSalutation(expectedSalutation))));
        logger.log("third -> " + (actualDOB.equals(expectedDateOfBirth) || this.translateDobToBhs(actualDOB).equals(this.translateDobToBhs(expectedDateOfBirth))));
        
        boolean isAccountDetailDataCorrect = (actualFirstname.equals(expectedFirstName) && actualLastName.equals(expectedLastName) 
                                                && (actualSalutation.equals(expectedSalutation)
                                                || actualSalutation.equals(this.translateSalutation(expectedSalutation)))
                                                && (actualDOB.equals(expectedDateOfBirth) || this.translateDobToBhs(actualDOB).equals(this.translateDobToBhs(expectedDateOfBirth))));

        return isAccountDetailDataCorrect;
    }

    @Override
    public void openHelpCenterPage(){
        WebElement helpCenterButton = driverTools.swipeToElement(HELP_CENTER_ENTRY, Swipe.UP, 30).orElseThrow();
        driverTools.click(helpCenterButton);
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        driverTools.waitForElementToAppear(HelpCenterPage.SKIP_BTN,10).ifPresent(driverTools::click);;
        driverTools.switchToNative();
    }

    @Override
    public void openWishlistPage(){
        WebElement wishlistButton = driverTools.swipeToPresentedElement(WISHLIST_ENTRY, Swipe.UP, 30).orElseThrow();
        driverTools.click(wishlistButton);
        boolean isInWishlist = driverTools.waitForElementToPresence(WISHLIST_PAGE).isPresent();
        if (!isInWishlist) {
            WebElement retryWishlistButton = driverTools.swipeToPresentedElement(WISHLIST_ENTRY, Swipe.UP, 30).orElseThrow();
            driverTools.click(retryWishlistButton);
            driverTools.waitForElementToPresence(WISHLIST_PAGE).orElseThrow();
        }
    }


    @Override
    public void openMyReviewPage(){
        driverTools.swipeToElement(YOUR_REVIEWS,Swipe.UP,10);
        if(!driverTools.isElementDisplayed(YOUR_REVIEWS)){
            driverTools.swipeToElement(YOUR_REVIEWS,Swipe.DOWN,10);
        }
        driverTools.clickElementByCoordinate(YOUR_REVIEWS);
    }
    
    @Override
    public void openSmartProfilePage(){
        driverTools.swipeToElement(SAVE_PASSENGERS_DATA, Swipe.UP,20);
        driverTools.click(SAVE_PASSENGERS_DATA);
    }
    @Override
    public void openLoyaltyPartnerPage(){
        driverTools.click(driverTools.swipeToElement(LOYALTY_PARTNER_ENTRY_BTN,Swipe.UP,20).orElseThrow());
    }

    @Override
    public boolean checkVerifiedPhone(){
        driverTools.waitForElementToAppear(EDIT_ACCOUNT_BUTTON,30).orElseThrow();
        driverTools.click(EDIT_ACCOUNT_BUTTON);
        driverTools.waitForElementToBeClickable(EDIT_EMAIL_AND_PHONE_NUMBER,35).orElseThrow();
        driverTools.click(EDIT_EMAIL_AND_PHONE_NUMBER);
        boolean isVerifiedPhoneNumber =driverTools.waitForElementToAppear(VERIFIED_PHONE,30).orElseThrow().isDisplayed();
        return isVerifiedPhoneNumber;
    }

    @Override
    public String userEditAccountDetails() {
        driverTools.swipeToElement(ACCOUNT_DETAIL_LASTNAME,Swipe.UP,2).ifPresent(driverTools::click);
        driverTools.clear(ACCOUNT_DETAIL_LASTNAME);
        String inputNewLastName = CoreFunctions.randomName()+ "aut";;
        driverTools.sendKeys(ACCOUNT_DETAIL_LASTNAME, inputNewLastName);
        driverTools.hideKeyboard(1);
        driverTools.swipeToElement(SAVE_BTN,Swipe.UP,5).ifPresent(driverTools::click);
        return inputNewLastName;
    }

    @Override
    public boolean verifyEditedAccountDetail(String expectedData) {
        driverTools.swipeDown(); // required in iOS to refresh the page
        driverTools.waitForElementToPresence(ACCOUNT_FULL_NAME).orElseThrow();
        String[] nameBreakdown = driverTools.getText(ACCOUNT_FULL_NAME).orElseThrow().split(" ");
        return nameBreakdown[1].equals(expectedData);
    }

    @Override
    public boolean isLoggedIn() {
        return driverTools.waitForElementToAppear(EDIT_ACCOUNT_BUTTON).isPresent();
    }

    public boolean checkAccountEmailAndPhone(String expectedEmail, String phoneNumber) {
        String actualPhoneNumber = driverTools.element(ACCOUNT_DETAIL_PHONE_NUMBER).orElseThrow().getAttribute("value");
        String actualEmail = driverTools.element(ACCOUNT_DETAIL_EMAIL).orElseThrow().getAttribute("value");
        logger.log("expected phone number: " + phoneNumber);
        logger.log("actual phone number: " + actualPhoneNumber);
        logger.log("expected email: " + expectedEmail.toLowerCase());
        logger.log("actual email: " + actualEmail);

        Assertion.assertThat("check phone number should be same", actualPhoneNumber, containsString(phoneNumber));
        Assertion.assertThat("check email should be same", actualEmail.toLowerCase(), equalTo(expectedEmail.toLowerCase()));
        return true;
    }

    @Override
    public void openVoucherPage() {
        driverTools.swipeToElement(VOUCHER_BOX, Swipe.UP, 10);
        driverTools.click(VOUCHER_BOX);
    }

    @Override
    public boolean isVoucherPageOpen() {
        Boolean isVoucherBoxTitleVisible = driverTools.waitForElementToAppear(VOUCHER_BOX_TITLE, 40).isPresent();
        Assertion.assertThat("Verify voucher box appear",isVoucherBoxTitleVisible,is(true));
        return true;
    }

    @Override
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
        boolean isDeviceIdIsShown = driverTools.waitForElementToAppear(DEVICE_ID).isPresent();
        Assertion.assertThat("Verify deivce info popup header is shown", isDeviceInfoHeaderIsShown, is(true));
        Assertion.assertThat("Verify device id is shown", isDeviceIdIsShown, is(true));
        return true;
    }

    @Override
    public boolean verifyCopyDeviceInfoButton() {
        driverTools.click(COPY_DEVICE_ID);
        boolean isCopySnackBarAppear = driverTools.waitForElementToAppear(DEVICE_ID_COPIED_SNACKBAR,3).isPresent();
        Assertion.assertThat("Verify success copy snackbar appear", isCopySnackBarAppear, is(true));
        return true;
    }

    @Override
    public boolean verifyLoyaltyPointValue(String expectedValue) {
        driverTools.swipeDown(3);
        String actualLoyaltyValue = driverTools.getText(LOYALTY_POINT_VALUE).orElseThrow();
        Assertion.assertThat("Verify loyalty value correct", actualLoyaltyValue.replaceAll("[^0-9]",""), equalTo(expectedValue));
        return true;
    }

    @Override
    public String  getLoyaltyPointValue() {
        driverTools.swipeToElement(LOYALTY_POINT_VALUE, Swipe.DOWN, 10);
        return driverTools.getValue(LOYALTY_POINT_VALUE);
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
        WebElement accountFullNameElement = driverTools.waitForElementToPresence(ACCOUNT_FULL_NAME).orElseThrow();
        return driverTools.getText(accountFullNameElement);
    }

    @Override
    public boolean verifyTermAndConditionPage(){
        driverTools.waitForElementToAppear(TERM_AND_CONDITION_PAGE,30);
        return driverTools.isElementDisplayed(TERM_AND_CONDITION_PAGE);
    }

    @Override
    public void openTermAndCondition(){
        driverTools.click(TERM_AND_CONDITION);
    }

    @Override
    public void openPrivacyPolicyMenu() {
        driverTools.swipeToElement(PRIVACY_POLICY_MENU, Swipe.UP, 10);
        if(driverTools.isElementDisplayed(PRIVACY_POLICY_MENU) == false) driverTools.swipeToElement(PRIVACY_POLICY_MENU, Swipe.DOWN, 10);
        driverTools.click(PRIVACY_POLICY_MENU);
    }

    @Override
    public boolean verifyPrivacyPolicy() {
        WebElement element = driverTools.waitForElementToAppear(PRIVACY_POLICY_TOOLBAR, 10).orElseThrow();
        boolean toolBar = driverTools.isElementDisplayed(element);
        logger.log("tool bar shown" + toolBar);

        return toolBar;
    }

    @Override
    public void openAboutTiketDotCom(){
        WebElement element = driverTools.swipeToElement(ABOUT_TIKETDOTCOM, Swipe.UP, 20).orElseThrow();
        driverTools.click(element);
    }

    @Override
    public boolean verifyAboutTiketDotCom() {
        WebElement element = driverTools.waitForElementToAppear(ABOUT_TIKETDOTCOM_TOOLBAR, 10).orElseThrow();
        boolean backButton = driverTools.isElementDisplayed(PRIVACY_POLICY_BACK_BUTTON);
        logger.log("back button shown" + backButton);
        boolean toolBar = driverTools.isElementDisplayed(element);
        logger.log("tool bar shown" + toolBar);
        return backButton && toolBar;
    }

    @Override
    public void clickSeeAllPayment() {
        driverTools.swipeDown(4);
        driverTools.swipeToElement(SEE_ALL_PAYMENT,Swipe.UP,5);
        driverTools.click(SEE_ALL_PAYMENT);
    }

    @Override
    public void clickAddBcaCard() {
        driverTools.click(ADD_BCA_CARD);
    }

    @Override
    public boolean verifyBCAForm(String email) {
        boolean isEmailPresent =  driverTools.waitForElementToAppear(getEmailLocator(email)).isPresent();
        logger.log("is email "+email+" present: "+isEmailPresent);
        boolean isCardNumberEditable = checkCardNumberEditable(CoreFunctions.randomInt(16));
        logger.log("is card number field editable: "+isCardNumberEditable);
        boolean isCardLimitShown = driverTools.isElementDisplayed(BCA_LIMIT_FIELD);
        logger.log("is card limit field shown: "+isCardLimitShown);
        boolean isPhoneNumberFieldShown = driverTools.isElementDisplayed(BCA_PHONE_FIELD);
        logger.log("is phone number field shown: "+isPhoneNumberFieldShown);
        boolean isButtonContinueShown = driverTools.isElementDisplayed(BCA_CONTINUE_BUTTON);
        logger.log("is continue button shown: "+isButtonContinueShown);

        return isEmailPresent && isCardNumberEditable && isCardLimitShown && isPhoneNumberFieldShown && isButtonContinueShown;
    }

    private Locator getEmailLocator(String email) {
        return new Locator(email,AppiumBy.xpath("//*[contains(@label,'"+email+"')]"));
    }

    private boolean checkCardNumberEditable(long number){
        String numberStr = Long.toString(number);
        driverTools.sendKeys(BCA_CARD_FIELD,numberStr);
        driverTools.hideKeyboard(1);
        String formattedNumber = numberStr.substring(0, 4) + " " +
                numberStr.substring(4, 8) + " " +
                numberStr.substring(8, 12) + " " +
                numberStr.substring(12);
        logger.log("looking for filled field with: "+formattedNumber);
        Locator fieldFilled = new Locator(formattedNumber, AppiumBy.xpath("//*[contains(@value,'"+formattedNumber+"')]"));
        return driverTools.isElementDisplayed(fieldFilled);
    }

    public void clickEditPhoneAndEmailBtn() {
        driverTools.swipeToElement(EDIT_EMAIL_AND_PHONE_NUMBER, Swipe.UP, 3);
        driverTools.click(EDIT_EMAIL_AND_PHONE_NUMBER);
    }

    @Override
    public String getAccountDetailPhoneNumber() {
        driverTools.swipeToElement(ACCOUNT_DETAIL_PHONE_NUMBER, Swipe.UP, 3);
        return driverTools.getText(ACCOUNT_DETAIL_PHONE_NUMBER).orElseThrow();
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
    //     driverTools.sendKeys(NEW_PHONE_NUMBER_INPUT_FIELD, newPhoneNumber);
    //     driverTools.click(VERIFY_NEW_PHONE_NUMBER_BTN);
    //     loginPage.inputOtp();
    // }

    @Override
    public String getPhoneNumberInChangeEmailOrPhonePage() {
        driverTools.waitForElementToAppear(CURRENT_PHONE_NUMBER_VALUE,10);
        String removeCountryCode = driverTools.getText(CURRENT_PHONE_NUMBER_VALUE).orElseThrow().replace("+62 ", "");
        return removeCountryCode;
    }

    @Override
    public void closeChangeEmailOrPhonePage() {
        driverTools.click(BACK_BUTTNON_ON_CHANGE_EMAIL_OR_PHONE_PAGE);
    }

    @Override
    public void refresh() {
            driverTools.swipeDown();
    }

    @Override
    public void openReferralPage(){
        WebElement referralBtn = driverTools.swipeToElement(REFERRAL_BTN,Swipe.UP,3).orElseThrow();
        driverTools.click(referralBtn);
    }

    @Override
    public void openPaylaterPage() {
        driverTools.swipeToElement(BLIBLI_PAYLATER_ENTRY_ON_MY_ACCOUNT, Swipe.UP, 10);
        if(!driverTools.isElementDisplayed(BLIBLI_PAYLATER_ENTRY_ON_MY_ACCOUNT)) driverTools.swipeToElement(BLIBLI_PAYLATER_ENTRY_ON_MY_ACCOUNT, Swipe.DOWN, 10);
        driverTools.click(BLIBLI_PAYLATER_ENTRY_ON_MY_ACCOUNT);
    }

    @Override
    public void openCurrencyPage(){
        driverTools.swipeToElement(CURRENCY_PAGE, Swipe.UP, 10).orElseThrow();
        driverTools.click(CURRENCY_PAGE);
    }

    @Override
    public boolean verifyPaylaterPageOpen() {
        driverTools.waitForElementToAppear(PAYLATER_CAROUSEL,3).orElseThrow();
        Assertion.assertThat("Verify paylater carousel appear", driverTools.isElementDisplayed(PAYLATER_CAROUSEL), is(true));
        Assertion.assertThat("Verify paylater limit container", driverTools.isElementDisplayed(PAYLATER_LIMIT_CONTAINER), is(true));
        Assertion.assertThat("Verify activate paylater button appear", driverTools.isElementDisplayed(ACTIVATE_PAYLATER_BTN), is(true));
        return true;
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
        if(usernameList != null && usernameList.contains(username)) return;
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
    public boolean verifyIsOnAccountPage() {
        driverTools.swipeToElement(EDIT_ACCOUNT_BUTTON,Swipe.UP,3);
        return driverTools.waitForElementToPresence(EDIT_ACCOUNT_BUTTON).isPresent();
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
        driverTools.swipeToElement(EDIT_ACCOUNT_BUTTON, Swipe.UP, 2).orElseThrow();
        driverTools.clickElementByCoordinate(EDIT_ACCOUNT_BUTTON);
    }

    @Override
    public boolean checkAccountEntity(String entity) {
        Locator emailLocator = new Locator(entity, AppiumBy.xpath("//*[contains(@label,'"+entity+"')]"));
        driverTools.waitForElementToAppear(emailLocator, 10);
        return driverTools.isElementDisplayed(emailLocator);
    }

    @Override
    public void openAccountPageUsingDeeplink(){
        Deeplink deeplinkAccountPage = new Deeplink("ACCOUNT_DEEPLINK","tiket://gatotkaca.tiket.com/en-sg/myaccount","tiket://preprod.tiket.com/en-sg/myaccount","tiket://tiket.com/en-sg/myaccount");
        try {
            driverTools.deeplink(deeplinkAccountPage);
        }catch (Exception e){
            logger.log(e.getLocalizedMessage());
        }
        if (driverTools.isElementDisplayed(LoginPageIOS.REVIEW_APP_STORE)) {
            driverTools.clickElementByCoordinate(LoginPageIOS.REVIEW_APP_STORE);
        }
        if (driverTools.isElementDisplayed(CLOSE_BUTTON)) {
            driverTools.clickElementByCoordinate(CLOSE_BUTTON);
        }
        driverTools.waitForElementToPresence(FLAG_TOP_OF_ACCOUNT_PAGE);
        driverTools.swipeDown(1);
    }

    @Override
    public void openAccountPageUsingDeeplink(String countryCode, String currency) {
        String localePath = "en-" + countryCode.toLowerCase();
        Deeplink deeplinkAccountPage = new Deeplink(
                "ACCOUNT_DEEPLINK_" + localePath + "_" + currency,
                "tiket://gatotkaca.tiket.com/" + localePath + "/myaccount?currency=" + currency,
                "tiket://preprod.tiket.com/" + localePath + "/myaccount?currency=" + currency,
                "tiket://tiket.com/" + localePath + "/myaccount?currency=" + currency);
        try {
            driverTools.deeplink(deeplinkAccountPage);
        } catch (Exception e) {
            logger.log(e.getLocalizedMessage());
        }
        HomePage<T> homePage = HomePage.getInstance(session);
        if (homePage.isCurrencyCountryPopupDisplayed()) {
            return;
        }
        if (driverTools.isElementDisplayed(LoginPageIOS.REVIEW_APP_STORE)) {
            driverTools.clickElementByCoordinate(LoginPageIOS.REVIEW_APP_STORE);
        }
        if (driverTools.isElementDisplayed(CLOSE_BUTTON)) {
            driverTools.clickElementByCoordinate(CLOSE_BUTTON);
        }
    }

    @Override
    public String getSelectedCountry() {
        driverTools.swipeToElement(SETTINGS_SECTION_TITLE, Swipe.UP, 15).orElseThrow();
        return driverTools.getText(SETTINGS_COUNTRY_VALUE).orElseThrow();
    }

    @Override
    public String getSelectedCurrency() {
        driverTools.swipeToElement(SETTINGS_SECTION_TITLE, Swipe.UP, 15).orElseThrow();
        return driverTools.getText(SETTINGS_CURRENCY_VALUE).orElseThrow();
    }

    @Override
    public boolean isAccountPageDisplayed() {
        driverTools.swipeToElement(FLAG_TOP_OF_ACCOUNT_PAGE, Swipe.DOWN, 10);
        return driverTools.waitForElementToAppear(FLAG_TOP_OF_ACCOUNT_PAGE, 10).isPresent();
    }
}
