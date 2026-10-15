package com.tiket.page.setting;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.account.AccountPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.home.HomePageAndroid;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

import static com.tiket.page.home.HomePageAndroid.BRAZE_CLOSE;
import static com.tiket.page.home.HomePageAndroid.FLAG_MAP_BRAZE;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class SettingPageAndroid<T extends WebDriver> extends SettingPage<T> {
    public SettingPageAndroid(Session<T> session) {
        super(session);
    }

    private static final Locator ENGLISH_RADIO_BUTTON = new Locator("englishRadioButton", AppiumBy.id("rb_lang_right"));
    private static final Locator BHASA_RADIO_BUTTON = new Locator("bhasaRadioButton", AppiumBy.id("rb_lang_left"));
    public static final Locator BACK_BTN = new Locator("BACK_BTN", AppiumBy.id("tds_icon_navigation_toolbar"));
    public static final Locator HOME_ICON_BOTTOMNAVBAR = new Locator("HOME_ICON_BOTTOMNAVBAR", AppiumBy.xpath("//*[contains(@resource-id,'bottom_bar')]/android.view.ViewGroup[1]"));
    public static final Locator DEVICE_SETTING_BACK_BTN = new Locator("DEVICE_SETTING_BACK_BTN", AppiumBy.accessibilityId("Back"));
    private static final Locator SETTINGS = new Locator("Settings", AppiumBy.id("label"));
    private static final Locator OTP_SETTINGS = new Locator("OTP_SETTINGS", AppiumBy.id("tv_title"));
    private static final Locator SETTINGS_PAGE_HEADER = new Locator("Settings", AppiumBy.id("rv_account"));
    private static final Locator LANGUAGE_MENU = new Locator("LANGUAGE_MENU", AppiumBy.id("menu_language"));
    private static final Locator LOGOUT_BUTTON = new Locator("LOGOUT_BUTTON", AppiumBy.xpath("//*[@resource-id='com.tiket.gits.debug:id/tv_title' and (@text='Log out' or @text='Keluar')]"));
    private static final Locator CONFIRM_LOGOUT = new Locator("CONFIRM_LOGOUT",AppiumBy.xpath("//android.widget.Button[contains(@resource-id,'tds_button_view') and @text='Log out' or @text='Keluar']"));
    private static final Locator MENU_ACCOUNT = new Locator("MENU_ACCOUNT", AppiumBy.id("menu_account"));
    private static final Locator CHANGE_PASSWORD = new Locator("CHANGE_PASSWORD", AppiumBy.id("menu_change_password"));
    private static final Locator NOTIFICATION_MENU = new Locator("NOTIFICATION_MENU", AppiumBy.id("menu_notification"));
    private static final Locator NOTIFICATION_MENU_TITLE = new Locator("NOTIFICATION_MENU_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Notification') or contains(@text,'Notifikasi'))]"));
    private static final Locator NOTIFICATION_ACTIVITY_NEW = new Locator("NOTIFICATION_ACTIVITY_NEW", AppiumBy.xpath("//*[@text='Account Activity' or @text='Aktivitas Akun']"));
    private static final Locator BACK_BTN_ON_NOTIFICATION_MENU = new Locator("BACK_BTN_ON_NOTIFICATION_MENU", AppiumBy.id("tds_icon_navigation_toolbar"));

    // Security Settings
    public static final Locator SECURITY_MENU = new Locator("SECURITY_MENU", AppiumBy.id("menu_security"));
    public static final Locator PIN_MENU = new Locator("PIN_MENU", AppiumBy.id("component_set_pin"));
    public static final Locator PIN_MENU_TEXT = new Locator("PIN_MENU_TEXT", AppiumBy.xpath("(//*[contains(@resource-id,'component_set_pin')]//*[contains(@resource-id,'tv_action')])[1]"));
    public static final Locator CONFIRM_STOP_TRUSTING_DEVICE = new Locator("CONFIRM_STOP_TRUSTING_DEVICE", AppiumBy.id("tds_btn_primary_info_view"));
    public static final Locator TRUST_DEVICE_POPUP_NO = new Locator("TRUST_DEVICE_POPUP_NO", AppiumBy.id("tds_btn_secondary_info_view"));
    public static final Locator SNACKBAR_CREATE_PIN = new Locator("SNACKBAR_CREATE_PIN", AppiumBy.id("tds_main_item_group"));
    public static final Locator ENTER_PIN = new Locator("ENTER_PIN", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Masukkan') or contains(@text,'Enter'))]"));
    public static final Locator PIN_INPUT_BOX = new Locator("PIN_INPUT_BOX", AppiumBy.id("ll_wrapper"));
    public static final Locator CONFIRM_PIN = new Locator("CONFIRM_PIN", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Konfirmasikan') or contains(@text,'Confirm'))]"));
    public static final Locator OTP_POPUP = new Locator("OTP_POPUP", AppiumBy.id("crf_bottom_sheet"));
    public static final Locator OTP_PAGE = new Locator("OTP_PAGE", AppiumBy.id("tv_toolbar_title"));
    public static final Locator SINGLE_VERIFICATION_CHECKBOX = new Locator("SINGLE_VERIFICATION_CHECKBOX", AppiumBy.id("rb_single_verification"));
    public static final Locator DOUBLE_VERIFICATION_CHECKBOX = new Locator("DOUBLE_VERIFICATION_CHECKBOX", AppiumBy.id("rb_double_verification"));
    public static final Locator DOUBLE_VERIFICATION_RADIO= new Locator("DOUBLE_VERIFICATION_RADIO", AppiumBy.xpath("//*[contains(@resource-id,'rb_double_verification') and (@checked = 'true')]"));
    public static final Locator SINGLE_VERIFICATION_RADIO = new Locator("SINGLE_VERIFICATION_RADIO", AppiumBy.xpath("//*[contains(@resource-id,'rb_single_verification') and (@checked = 'true')]"));
    public static final Locator CLOSE_SECURITY_SETTING = new Locator("CLOSE_SECURITY_SETTING", AppiumBy.id("tds_icon_navigation_toolbar"));
    private static final Locator SECURITY_SETTINGS_HEADER = new Locator("SECURITY_SETTINGS_HEADER", AppiumBy.xpath("//*[contains(@resource-id,'tv_toolbar_title') and (@text='Pengaturan Keamanan' or @text='Security Settings')]"));
    
    // Device Management
    private static final Locator DEVICE_MANAGEMENT_MENU = new Locator("DEVICE_MANAGEMENT_MENU", AppiumBy.id("container_device_management"));
    private static final Locator SNACKBAR_DEVICE_MANAGEMENT = new Locator("SNACKBAR_DEVICE_MANAGEMENT", AppiumBy.id("toolbar_view"));
    private static final Locator DEVICE_MANAGEMENT_THIS_DEVICE = new Locator("DEVICE_MANAGEMENT_THIS_DEVICE", AppiumBy.id("tv_this_device"));
    private static final Locator DEVICE_MANAGEMENT_THIS_DEVICE_OS = new Locator("DEVICE_MANAGEMENT_THIS_DEVICE_OS", AppiumBy.xpath("(//*[contains(@resource-id,'tv_os_model')])[1]"));
    private static final Locator TOGGLE_DEVICE_TRUST = new Locator("TOGGLE_DEVICE_TRUST", AppiumBy.id("switch_trusted_device"));
    public static final Locator SNACKBAR_TEXT = new Locator("SNACKBAR_TEXT", AppiumBy.id("tds_snackbar_text"));

    // Location Service
    public static final Locator LOCATION_TOGGLE = new Locator("LOCATION_TOGGLE", AppiumBy.id("switch_action"));
    public static final Locator PRIMARY_BTN = new Locator("PRIMARY_BTN", AppiumBy.id("tds_btn_primary_info_view"));
    public static final Locator SECONDARY_BTN = new Locator("SECONDARY_BTN", AppiumBy.id("tds_btn_secondary_info_view"));
    public static final Locator PERMISSION_TYPE_ALLOW_ONCE = new Locator("PERMISSION_TYPE_ALLOW_ONCE", AppiumBy.xpath("//*[contains(@resource-id,'permission_allow_one_time_button')]"));
    public static final Locator WHILE_USING_APP_PERMISSION_BUTTON = new Locator("WHILE_USING_APP_PERMISSION_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'permission_allow_foreground_only_button')]"));
    public static final Locator TURN_ON_LOCATION_DEVICE = new Locator("TURN_ON_LOCATION_DEVICE", AppiumBy.xpath("//*[@text='Turn on' or @resource-id='android:id/button1']"));
    private static final Locator SAVE_PASSENGER_DATA = new Locator("SAVE_PASSENGER_DATA", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Simpan Data Penumpang') or contains(@text,'Save Passenger Data'))]"));
    private static final Locator CURRENT_CURRENCY = new Locator("CURRENT_CURRENCY", AppiumBy.xpath("//*[contains(@resource-id,'menu_currency')]//*[contains(@resource-id,'tv_info')]"));
    private static final Locator SEARCH_CURRENCY = new Locator("SEARCH_CURRENCY", AppiumBy.id("tds_search_et"));
    private static final Locator MENU_CURRENCY = new Locator("MENY_CURRENCY", AppiumBy.id("menu_currency"));
    private static final Locator CLOSE = new Locator("CLOSE", AppiumBy.id("iv_close"));
    private static final Locator LOADING_STATE = new Locator("LOADING_STATE", AppiumBy.id("loading_blue"));
    private static final Locator CURRENCY_RADIO_BUTTON = new Locator("CURRENCY_RADIO_BUTTON", AppiumBy.id("radio_button"));
    private static final Locator DELETE_ACCOUNT = new Locator("DELETE_ACCOUNT", AppiumBy.id("component_delete_pin"));
    private static final Locator CHECKBOX = new Locator("CHECKBOX", AppiumBy.className("android.widget.CheckBox"));
    private static final Locator CHECKBOX_CHECKED = new Locator("CHECKBOX_CHECKED", AppiumBy.xpath("//android.widget.CheckBox[@checked='true']"));
    private static final Locator CONTINUE = new Locator("CONTINUE", AppiumBy.className("android.widget.Button"));
    private static final Locator CHOOSE_REASON_DROPDOWN = new Locator("CHOOSE_REASON_DROPDOWN", AppiumBy.xpath("//android.widget.EditText//following-sibling::android.widget.Button"));
    private static final Locator CHOOSE_REASON_BUTTON = new Locator("CHOOSE_REASON_BUTTON", AppiumBy.xpath("//button[text()='Pilih' or text()='Choose']"));
    private static final Locator SUBMIT_DELETE = new Locator("SUBMIT_DELETE", AppiumBy.xpath("//*[text()='Submit request' or text()='Kirim pengajuan']"));
    private static final Locator DELETE_NOW = new Locator("DELETE_NOW", AppiumBy.xpath("//*[text()='Request now' or text()='Ajukan sekarang']"));
    private static final Locator SESSION_EXPIRED_ALERT = new Locator("SESSION_EXPIRED_ALERT", AppiumBy.id("tv_title"));
    private static final Locator SKIP_ALERT = new Locator("SKIP_ALERT", AppiumBy.id("tv_button"));
    private static final Locator SECURITY_SETTING_BTM_SHEET = new Locator("SECURITY_SETTING_BTM_SHEET", AppiumBy.id("tds_txt_header_info_view"));
    private static final Locator CHANGE_EMAIL = new Locator("CHANGE_EMAIL_UNVERIFIED", AppiumBy.xpath("(//*[contains(@resource-id,'tv_action')])[2]"));
    private static final Locator CHANGE_EMAIL_UNVERIFIED = new Locator("CHANGE_EMAIL", AppiumBy.id("tv_email_hint_with_link"));
    private static final Locator CONTINUE_CHANGE = new Locator("CONTINUE_CHANGE", AppiumBy.id("btn_continue"));
    private static final Locator CHEVRON_OTP_EMAIL = new Locator("CHEVRON_OTP_EMAIL", AppiumBy.id("iv_go_to_email"));
    private static final Locator CHEVRON_OTP_PHONE = new Locator("CHEVRON_OTP_EMAIL", AppiumBy.id("iv_go_to_email"));
    private static final Locator NEW_EMAIL_FIELD = new Locator("NEW_EMAIL_FIELD", AppiumBy.id("tds_box_input_text"));
    private static final Locator VERIFY_EMAIL_BUTTON = new Locator("VERIFY_EMAIL_BUTTON", AppiumBy.id("btn_primary"));
    private static final Locator VERIFIED_EMAIL_ICON = new Locator("VERIFIED_EMAIL_ICON", AppiumBy.xpath("//*[contains(@resource-id,'email_container')]//*[contains(@text,'Verified') or contains(@text,'Sudah diverifikasi')]"));
    private static final Locator ACCOUNT_CENTER = new Locator("ACCOUNT_CENTER", AppiumBy.id("tv_account_center"));
    private static final Locator LOYALTY_BENEFIT_LOGIN_IDR_FLAG = new Locator("LOYALTY_BENEFIT_LOGIN_IDR_FLAG", AppiumBy.id("tv_loyalty_badge"));
    private static final Locator REWARDS_NON_LOGIN_IDR_FLAG = new Locator("REWARDS_NON_LOGIN_IDR_FLAG", AppiumBy.id("tv_loyalty_and_tiket_points_locked_title"));

    private static final Locator EMAIL_MY_ACCOUNT_PAGE = new Locator("EMAIL_MY_ACCOUNT_PAGE", AppiumBy.id("tv_unm_info_2"));
    private static final Locator PHONE_NUMBER_MY_ACCOUNT_PAGE = new Locator("PHONE_NUMBER_MY_ACCOUNT_PAGE", AppiumBy.id("tv_unm_info_1"));

    @Override
    public boolean isOnSettingPage(){   
        WebElement settingHeaderElement = driverTools.waitForElementToAppear(SETTINGS_PAGE_HEADER,5).orElseThrow();
        boolean isSettingHeaderVisible = driverTools.isElementDisplayed(settingHeaderElement);
        boolean isLangguageMenuVisible = driverTools.isElementDisplayed(LANGUAGE_MENU);
        boolean isCurrencyMenuVisible = driverTools.isElementDisplayed(CURRENT_CURRENCY);
        boolean isLocationVisible = driverTools.isElementDisplayed(LOCATION_TOGGLE);

        logger.log("Setting header visible? -> " + isSettingHeaderVisible);
        logger.log("Langguage menu visible? -> " + isLangguageMenuVisible);
        logger.log("Currency menu visible ? -> " + isCurrencyMenuVisible);
        logger.log("Location visible ? -> " + isLocationVisible);
        return isSettingHeaderVisible && isLangguageMenuVisible && isCurrencyMenuVisible && isLocationVisible;
    }

    @Override
    public void setLocationSwitch(String action){
        switch (action) {
            case ("ON") -> {
                if(!isLocationOn()) {enableLocationService();}
            }
            case ("OFF") -> {
                if(isLocationOn()) {disableLocationService();}
            }
            default -> {
                logger.fail("There is no '"+action+"' option");
            }
        }
    }

    private void disableLocationService() {
        driverTools.restartApp();
    }

    private void enableLocationService() {
        driverTools.swipeToElement(LOCATION_TOGGLE, Swipe.UP, 15);
        driverTools.swipeUp();
        if (driverTools.isElementDisplayed(LOCATION_TOGGLE)) driverTools.click(LOCATION_TOGGLE);
        driverTools.waitForElementToAppear(PRIMARY_BTN).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(PERMISSION_TYPE_ALLOW_ONCE, 2).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(WHILE_USING_APP_PERMISSION_BUTTON, 2).ifPresent(driverTools::click);
    }

    @Override
    public boolean isLocationOn(){
        driverTools.swipeToElement(LOCATION_TOGGLE, Swipe.UP, 15).orElseThrow();
        driverTools.swipeToElement(LOCATION_TOGGLE, Swipe.UP, 5).ifPresent(driverTools::click);
        boolean isLocationOff = driverTools.waitForElementToAppear(SECONDARY_BTN).isPresent();
        if(isLocationOff) {
            driverTools.click(SECONDARY_BTN);
        } else {
            driverTools.click(PRIMARY_BTN);
            driverTools.waitForElementToAppear(DEVICE_SETTING_BACK_BTN).ifPresent(driverTools::click);
        }
        return !isLocationOff;
    }
    
    @Override
    public void openSecuritySettingPage(){
        driverTools.click(SECURITY_MENU);
    }

    @Override
    public void closeSecuritySettingPage(){
        driverTools.click(CLOSE_SECURITY_SETTING);

    }

    @Override
    public void setNewPin(String pin){
        driverTools.swipeToElement(PIN_MENU, Swipe.UP, 3);
        String pinMenuText = driverTools.getText(PIN_MENU_TEXT).orElseThrow();
        if(pinMenuText.contains("Set") || pinMenuText.contains("Buat")) {
            driverTools.click(PIN_MENU);
            driverTools.click(CONFIRM_STOP_TRUSTING_DEVICE);
            driverTools.waitForElementToAppear(SNACKBAR_CREATE_PIN).orElseThrow();
            driverTools.waitForElementToAppear(ENTER_PIN).orElseThrow();
            driverTools.pressKey(PIN_INPUT_BOX, pin);
            driverTools.waitForElementToAppear(CONFIRM_PIN).orElseThrow();
            driverTools.pressKey(driverTools.element(PIN_INPUT_BOX).orElseThrow(), pin, true, null);
            driverTools.waitForElementToAppear(OTP_POPUP).orElseThrow();
            inputOtp();
        } else {
            Assertion.fail("PIN_MENU_TEXT does not contain 'Set' or 'Buat'");
        }
    }

    private Locator getOtpLocator(int index) {
        return new Locator("OTP_FIELD_"+index,AppiumBy.xpath("//android.widget.EditText[contains(@resource-id, 'et_otp_"+index+"') or ./parent::*[contains(@resource-id, 'et_otp_"+index+"')]]"));
    }

    private void inputOtp() {
        for (int i=1;i<5;i++) {
            Locator otpField = getOtpLocator(i);
            Optional<WebElement> otp_field_element = driverTools.waitForElementToAppear(otpField,20);
            if (!otp_field_element.isPresent()) break;
            driverTools.click(otp_field_element.get());
            driverTools.sendKeys(otp_field_element.get(),""+ i +"");
        }
        driverTools.waitForElementToDisappear(getOtpLocator(1));
    }

    @Override
    public void changePin(String pin){} // FIXME: Implementation pending

    @Override
    public void forgotPin(String pin){} // FIXME: Implementation pending

    private boolean isBannerStopUsingTrustedAppear(){
        Optional<WebElement> btmSheet = driverTools.element(SECURITY_SETTING_BTM_SHEET);
        boolean isBtmSheetVisible =  btmSheet.isPresent();
        if(isBtmSheetVisible){
            String btmSheetMessage = driverTools.getText(btmSheet.get());
            logger.log("btm sheet message : " + btmSheetMessage);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void switchDeviceTrust(boolean trustCondition) {
        WebElement toggleDeviceTrust = driverTools.swipeToElement(TOGGLE_DEVICE_TRUST, Swipe.UP, 3).orElseThrow();
        driverTools.click(toggleDeviceTrust);
        boolean isBannerStopUsingTrustedAppear = isBannerStopUsingTrustedAppear();
        if(isBannerStopUsingTrustedAppear == false && trustCondition==false){
            logger.log("toggling on");
            //toggle trust device on and then click toggle device again
            setVerificationStep();
            driverTools.click(TOGGLE_DEVICE_TRUST);
        }else if(isBannerStopUsingTrustedAppear==true && trustCondition==true){
            logger.log("toggling off");
            //press back as we want the trust condition as true and toggle already selected which mean correct condition
            driverTools.click(CONFIRM_STOP_TRUSTING_DEVICE);
            driverTools.click(TOGGLE_DEVICE_TRUST);
        }
    }

    @Override
    public boolean verifyStatusDeviceTrust(boolean trustCondition) {
        WebElement toggleDeviceTrust = driverTools.swipeToElement(TOGGLE_DEVICE_TRUST, Swipe.UP, 3).orElseThrow();
        driverTools.click(toggleDeviceTrust);
        boolean actualDeviceTrust = isBannerStopUsingTrustedAppear();
        logger.log("device trust toggle status -> " + actualDeviceTrust);
        return actualDeviceTrust;
    }

    @Override
    public boolean verifyNotifDeviceTrust(boolean trustCondition) {
        WebElement snackbarText = driverTools.waitForElementToAppear(SNACKBAR_TEXT).orElseThrow();
        String text = driverTools.getText(snackbarText);
        boolean notif = false;
        if (trustCondition && (text.contains("Perangkat sudah dijadikan tepercaya") || text.contains("set this device as trusted"))) {
            notif = true;
        } else if (text.contains("Perangkat ini tidak lagi dijadikan tepercaya") || text.contains("stopped using this as a trusted device")) {
            notif = true;
        }
        if (!notif) logger.warn("actual snackbar message is -> " + text);
        logger.log("device trust notification -> " + notif);
        return notif;
    }

    @Override
    public void setVerificationStep() {
        driverTools.waitForElementToAppear(OTP_SETTINGS).orElseThrow();
        driverTools.waitForElementToDisappear(SNACKBAR_TEXT);
        inputOtp();
    }

    @Override
    public void setTrustDevicePopupYes() {
        WebElement yesPopup = driverTools.waitForElementToPresence(CONFIRM_STOP_TRUSTING_DEVICE).orElseThrow();
        driverTools.click(yesPopup);
    }
    
    @Override
    public void openNotificationSettingPage(){
        driverTools.swipeToFullyVisibleElement(NOTIFICATION_MENU, Swipe.UP, 10).orElseThrow();
        driverTools.click(driverTools.waitForElementToAppear(NOTIFICATION_MENU).orElseThrow());
    }

    @Override
    public void backToHome() {
        driverTools.waitForElementToAppear(HOME_ICON_BOTTOMNAVBAR).ifPresent(driverTools::clickElementByCoordinate);
        String sessionID = session.sessionId();
        HomePage<T> homePage = HomePage.getInstance(this.session);
        if((Configs.ENVIRONMENT == Environment.PROD && FLAG_MAP_BRAZE.get(sessionID)==null) || !homePage.isOnHomePage()){
            if(driverTools.waitForElementToPresence(BRAZE_CLOSE,10).isPresent()){
                driverTools.clickElementByCoordinate(BRAZE_CLOSE);
                initFlagBraze(sessionID,true);
            }
        }
    }

    private void initFlagBraze(String sessionID, boolean check){
        FLAG_MAP_BRAZE.put(sessionID,check);
    }

    @Override
    public void openSavePassengersData() {
        driverTools.swipeToElement(SAVE_PASSENGER_DATA, Swipe.UP, 20).orElseThrow();
        driverTools.swipeUp();
        driverTools.swipeToElement(SAVE_PASSENGER_DATA, Swipe.DOWN, 1).orElseThrow();
        driverTools.click(SAVE_PASSENGER_DATA);
    }

    @Override
    public boolean isNewPINSet() {
        WebElement pinMenuElement = driverTools.waitForElementToAppear(PIN_MENU_TEXT).orElseThrow();
        return driverTools.getText(pinMenuElement).contains("Ganti") || driverTools.getText(pinMenuElement).contains("Change");
    }

    @Override
    public void openDeviceManagement() {
        driverTools.click(DEVICE_MANAGEMENT_MENU);
    }

    @Override
    public boolean verifyDeviceManagementPage() {
        boolean isSnackBarDisplayed = driverTools.waitForElementToAppear(SNACKBAR_DEVICE_MANAGEMENT).isPresent();
        boolean isThisDeviceDisplayed = driverTools.waitForElementToAppear(DEVICE_MANAGEMENT_THIS_DEVICE).isPresent();

        String thisDeviceOSText = driverTools.getText(DEVICE_MANAGEMENT_THIS_DEVICE_OS).orElseThrow().toLowerCase();
        logger.log("is Snackbar displayed : " + isSnackBarDisplayed);
        logger.log("is this device displayed : " + isThisDeviceDisplayed);
        logger.log("device os texg : " + thisDeviceOSText);

        boolean isThisDeviceAndroid = thisDeviceOSText.contains("android") || thisDeviceOSText.contains("google") || thisDeviceOSText.contains("sdk") || thisDeviceOSText.contains("samsung");
        logger.log("is this device android : " + isThisDeviceAndroid);

        return isSnackBarDisplayed && isThisDeviceDisplayed && isThisDeviceAndroid;
    }

    public void userTurnOnSingleVerification() {
        driverTools.waitForElementToAppear(SINGLE_VERIFICATION_CHECKBOX).ifPresent(driverTools::click);
        if(driverTools.isElementDisplayed(OTP_PAGE)){
            inputOtp();
        }
    }

    @Override
    public void userTurnOnDoubleVerification() {
        driverTools.waitForElementToAppear(DOUBLE_VERIFICATION_CHECKBOX).ifPresent(driverTools::click);
    }

    @Override
    public boolean isDoubleVerificationON() {
        return driverTools.isElementDisplayed(DOUBLE_VERIFICATION_RADIO);
    }

    @Override
    public boolean isSingleVerificationON() {
        return driverTools.isElementDisplayed(SINGLE_VERIFICATION_RADIO);
    }

    @Override
    public String checkCurrentCurrency() {
        driverTools.swipeToElement(CURRENT_CURRENCY, Swipe.UP, 10);
        return driverTools.getText(CURRENT_CURRENCY).orElseThrow();
    }

    @Override
    public void changeCurrency(String inputCurrency) {
        driverTools.waitForElementToAppear(BACK_BTN);
        String currentCurrency = checkCurrentCurrency();
        logger.log("Current currency: "+currentCurrency);

        String currencyOptionOne = inputCurrency.split("/")[0].toUpperCase();
        String currencyOptionTwo = inputCurrency.split("/")[1].toUpperCase();

        String currency = currencyOptionOne;
        if(currentCurrency .equals(currency)) {
            currency = currencyOptionTwo;
        }

        driverTools.click(MENU_CURRENCY);
        driverTools.waitForElementToAppear(SEARCH_CURRENCY).orElseThrow();
        driverTools.click(SEARCH_CURRENCY);
        driverTools.sendKeys(SEARCH_CURRENCY,currency);
        driverTools.click(getCurrencyLocator(currency));
        driverTools.waitForElementToAppear(BACK_BTN);
    }

    @Override
    public void changeCurrencyIfOtherCurrencyIsSelected(String currency){
        for (int tries = 0; tries < 2; tries++) {
            WebElement currentCurrencyElement = driverTools.swipeToElement(CURRENT_CURRENCY, Swipe.UP, 25).orElseThrow();
            String currentCurrency = driverTools.getText(currentCurrencyElement);
            logger.log("Current currency: " + currentCurrency);

            if (currentCurrency.equalsIgnoreCase(currency)) break;

            driverTools.swipeUp();
            driverTools.click(CURRENT_CURRENCY);

            if (driverTools.waitForElementToAppear(SEARCH_CURRENCY, 5).isEmpty()) {
                logger.softWarning("Currency sync issue. Retrying...");
                continue;
            }

            driverTools.click(SEARCH_CURRENCY);
            driverTools.sendKeys(SEARCH_CURRENCY, currency);
            logger.log("Checking if currency is already selected after search...");

            if (!isCurrencySelected(currency)) {
                driverTools.click(getCurrencyLocator(currency));
                driverTools.waitForElementToDisappear(CLOSE);
            } else {
                driverTools.click(CLOSE);
            }
            break;
        }

        WebElement currentCurrencyElement2 = driverTools.swipeToElement(CURRENT_CURRENCY,Swipe.UP,25).orElseThrow();
        String currentCurrency2 = driverTools.getText(currentCurrencyElement2);
        logger.log("Current currency: "+currentCurrency2);
        Assertion.assertThat("Is currency correct", currentCurrency2.equalsIgnoreCase(currency),is(true));
    }

    @Override
    public void changeCurrencyIfOtherCurrencyIsSelectedDeeplink(String currency){
        openChangeCurrencyOverlayUsingDeeplink();
        driverTools.waitForElementToPresence(SEARCH_CURRENCY);
        if(driverTools.waitForElementToPresence(SEARCH_CURRENCY,1).isEmpty()){
            HomePageAndroid.getInstance(session).closePromoPopup();
        }
        if(!driverTools.isElementDisplayed(SEARCH_CURRENCY))
            openCurrencyPage();
        selectCurrencyFromOpenBottomSheet(currency);
    }

    @Override
    public void selectCurrencyFromOpenBottomSheet(String currency){
        driverTools.waitForElementToAppear(SEARCH_CURRENCY).orElseThrow();
        driverTools.click(SEARCH_CURRENCY);
        driverTools.sendKeys(SEARCH_CURRENCY, currency);
        logger.log("Checking if currency is already selected after search...");

        if (!isCurrencySelected(currency)) {
            driverTools.click(getCurrencyLocator(currency));
            driverTools.waitForElementToDisappear(CLOSE);
        } else {
            driverTools.click(CLOSE);
        }
        driverTools.waitForElementToDisappear(CLOSE);
    }

    private void openCurrencyPage(){
        HomePage<T> homePage = HomePage.getInstance(session);
        AccountPage<T> accountPage = AccountPage.getInstance(session);
        homePage.openAccount();
        accountPage.openCurrencyPage();
    }

    private Locator getCurrencyLocator(String currency){
        return new Locator("currency",AppiumBy.xpath("//*[contains(@resource-id,'tv_code') and contains(@text,'"+currency.toUpperCase()+"')]"));
    }

    private boolean isCurrencySelected(String currency){
        //solve problem where account currency changed by other test run (different session) but value haven't refelected since app haven't reload yet
        Locator CURRENCY_RADIO_BTN_LOCATOR = new Locator("CURRENCY_RADIO_BTN_LOCATOR", AppiumBy.xpath("//*[contains(@resource-id,'tv_code') and contains(@text,'"+currency.toUpperCase()+"')]"+"//following-sibling::*[contains(@resource-id,'radio_button')]//android.view.View[2]"));
        return driverTools.isElementDisplayed(CURRENCY_RADIO_BTN_LOCATOR);
    }
    
    public boolean isSecurityPageOpen() {
        return driverTools.isElementDisplayed(driverTools.waitForElementToAppear(SECURITY_SETTINGS_HEADER,10).orElseThrow());
    }
    @Override
    public boolean isOnLoginSettingPage() {
        //check for basic menu
       boolean isSettingBasicMenuVisible = isOnSettingPage();
       boolean isLogOutButtonVisible = driverTools.isElementDisplayed(LOGOUT_BUTTON);
       boolean isAccountSettingVisible = driverTools.isElementDisplayed(MENU_ACCOUNT);
       boolean isChangePasswordMenuVisible = driverTools.isElementDisplayed(CHANGE_PASSWORD);
       boolean isSecurityMenuVisible = driverTools.isElementDisplayed(SECURITY_MENU);
       boolean isNotificationMenuVisible = driverTools.isElementDisplayed(NOTIFICATION_MENU);

       logger.log("is logout button visible? -> " + isLogOutButtonVisible);
       logger.log("is account setting visible? -> " + isAccountSettingVisible);
       logger.log("Is change password visible? -> " + isChangePasswordMenuVisible);
       logger.log("is security menu visible? -> " + isSecurityMenuVisible);
       logger.log("is notification menu visible? -> " + isNotificationMenuVisible);

       return isSettingBasicMenuVisible && isAccountSettingVisible && isLogOutButtonVisible 
                && isChangePasswordMenuVisible && isSecurityMenuVisible && isNotificationMenuVisible;
    }

    @Override
    public void logOut() {
        driverTools.swipeToElement(LOGOUT_BUTTON,Swipe.UP,15);
        WebElement e = driverTools.waitForElementToAppear(LOGOUT_BUTTON,10).orElseThrow();
        driverTools.clickElementByCoordinate(e);
        e = driverTools.waitForElementToAppear(CONFIRM_LOGOUT,20).orElseThrow();
        driverTools.clickElementByCoordinate(e);
    }

    private Locator getNotificationSwitchLocator(String category, String switchType){
        return new Locator("Switch", AppiumBy.xpath("//android.widget.TextView[contains(@resource-id,'tv_title') and contains(@text,'"+category+"')]//following-sibling::*[contains(@resource-id,'rv_services')]//*[contains(@text,'"+switchType+"')]//following-sibling::*[contains(@resource-id,'service_toggle')]"));
    }

    private Locator getNotificationSwitchCircleLocator(String category, String switchType){
        return new Locator("Switch", AppiumBy.xpath("//android.widget.TextView[contains(@resource-id,'tv_title') and contains(@text,'"+category+"')]//following-sibling::*[contains(@resource-id,'rv_services')]//*[contains(@text,'"+switchType+"')]//following-sibling::*[contains(@resource-id,'service_toggle')]//*[contains(@resource-id,'tds_toggle_circle')]"));
    }

    @Override
    public boolean isOnNotificationMenu(){
        WebElement notificationTitle = driverTools.waitForElementToAppear(NOTIFICATION_MENU_TITLE,15).orElseThrow();
        boolean isNotificationTitleDisplayed = driverTools.isElementDisplayed(notificationTitle);
        logger.log("is notification title displayed : " + isNotificationTitleDisplayed);
        return isNotificationTitleDisplayed;
    }

    @Override
    public void clickBackBtn(){
        driverTools.click(BACK_BTN);
    }
    
    @Override
    public String getNotificationSettingLanguage() {
        driverTools.swipeToFullyVisibleElement(NOTIFICATION_MENU_TITLE, Swipe.UP, 5).orElseThrow();
        String title = driverTools.getText(NOTIFICATION_MENU_TITLE).orElseThrow();

        String language = switch (title) {
            case "Notifikasi", "Pengaturan Notifikasi" -> "id";
            case "Notification", "Notification Settings" -> "en";
            default -> throw new NotImplementedException("Unimplemented type!");
        };
        logger.log("Language: "+language);
        return language;
    }

    @Override
    public boolean isNotificationSettingNewVersion() {
        boolean isNewVersion = driverTools.isElementDisplayed(NOTIFICATION_ACTIVITY_NEW);
        logger.log("is new version: "+isNewVersion);
        return isNewVersion;
    }

    @Override
    public boolean verifyAllToggle(String[] settings, String language){
        for (String setting: settings
        ) {

            logger.log("Checking for: "+setting);

            if(language.equals("id")){
                setting = switch (setting)
                {
                    case "Activity" ->  "Aktivitas";
                    case "Reminders" -> "Pengingat";
                    case "Special For You" -> "Spesial Untukmu";
                    case "Membership" -> "Keanggotaan";
                    case "Account Activity" ->  "Aktivitas Akun";
                    case "Exclusive Info" -> "Info Eksklusif";
                    case "Blibli Tiket Rewards Info" -> "Info Blibli Tiket Rewards";
                    default -> throw new NotImplementedException("Not implemented: " + setting);
                };
            }

            boolean emailToggle = driverTools.swipeToElement(getNotificationSwitchLocator(setting,"Email"),Swipe.UP,5).isPresent();
            boolean pushNotifToggle = driverTools.swipeToElement(getNotificationSwitchLocator(setting,"Push Notification"),Swipe.UP,5).isPresent();

            Assertion.assertThat("Email toggle for "+setting+" is found? " +emailToggle,emailToggle,is(true));
            Assertion.assertThat("Push Notification toggle for "+setting+" is found? " +pushNotifToggle,pushNotifToggle,is(true));
        }
        return true;
    }

    @Override
    public void clickDeleteAccount(){
        driverTools.click(driverTools.swipeToElement(DELETE_ACCOUNT,Swipe.UP,2).orElseThrow());
    }

    @Override
    public void fillDeleteAccountForm(){
        boolean isCheckBoxPrsent = driverTools.waitForElementToAppear(CHECKBOX,5).isPresent();
        if(isCheckBoxPrsent){
            driverTools.click(CHECKBOX);
        }else{
            driverTools.swipeToElement(CHECKBOX, Swipe.UP, 3);
        }
        driverTools.click(CHECKBOX);
        driverTools.click(CONTINUE);
        driverTools.waitForElementToDisappear(CHECKBOX_CHECKED);
        driverTools.click(CHECKBOX);
        driverTools.click(CONTINUE);
        driverTools.waitForElementToDisappear(CHECKBOX_CHECKED);
        driverTools.click(CHECKBOX);
        driverTools.click(CONTINUE);
        chooseRandomReason();
    }

    private void chooseRandomReason(){
        driverTools.click(driverTools.waitForElementToAppear(CHOOSE_REASON_DROPDOWN).orElseThrow());
        String[][] reasonOptions = {
                {"Want to change or have another account","Ingin mengganti atau memiliki akun lain",
                        "Change email","Ganti email"},

                {"Unpleasant experience when using the app","Pengalaman buruk saat memakai aplikasi",
                        "Contact Customer Care","Hubungi Customer Care"},

                {"Security concerns regarding personal data","Khawatir dengan keamanan data pribadi",
                        "Manage security","Atur keamanan"},

                {"Bothered by promo emails","Terganggu dengan email promo",
                        "Manage notifications","Atur notifikasi"}
        };
        driverTools.switchToWebView();
        int index = CoreFunctions.generateRandomInt(0,reasonOptions.length-1);

        String[] chosenReason = reasonOptions[index];

        Locator reasonLocator = getReasonLocator(chosenReason[0],chosenReason[1]);
        Locator buttonLocator = getButtonLocator(chosenReason[2],chosenReason[3]);

        driverTools.click(reasonLocator);
        driverTools.click(CHOOSE_REASON_BUTTON);
        boolean isButtonCorrect = driverTools.waitForElementToAppear(buttonLocator).isPresent();
        driverTools.switchToNative();
        Assertion.assertThat("Second button is according to chosen reason",isButtonCorrect,is(true));
    }

    private Locator getReasonLocator(String reasonEng,String reasonId){
        return new Locator(reasonEng,AppiumBy.xpath("//*[text()='"+reasonEng+"' or text()='"+reasonId+"']"));
    }

    private Locator getButtonLocator(String buttonEng,String buttonId){
        return new Locator(buttonEng,AppiumBy.xpath("//*[text()='"+buttonEng+"' or text()='"+buttonId+"']"));
    }

    @Override
    public void clickSubmitDelete(){
        driverTools.switchToWebView();
        driverTools.click(SUBMIT_DELETE);
        driverTools.click(DELETE_NOW);
        driverTools.switchToNative();
    }

    @Override
    public boolean isSessionExpired(){
        boolean isAlertShowing = driverTools.waitForElementToAppear(SESSION_EXPIRED_ALERT).isPresent();
        driverTools.click(SKIP_ALERT);
        return isAlertShowing;
    }

    public void openAccountSetting(){
        driverTools.click(MENU_ACCOUNT);
    }
    public boolean isChangeDetailSnackbarDisplayed(String snackbarType){
        String textEng = "Yay! You’ve successfully changed your email.";
        String textId = "Hore! Kamu berhasil mengganti email.";
        if(snackbarType.equals("phone")){
            //todo
        }
        Locator snackbar = new Locator("snackbar",AppiumBy.xpath("//android.widget.TextView[@text='"+textEng+"' or @text='"+textId+"']"));
        return driverTools.waitForElementToAppear(snackbar).isPresent();
    }

    public boolean isNewEmailShowing(String emailToCheck){
        Locator newEmail = new Locator(emailToCheck,AppiumBy.xpath("//android.widget.TextView[@text='"+emailToCheck+"']"));
        return driverTools.waitForElementToAppear(newEmail).isPresent();
    }

    public boolean isEmailVerified(){
        return driverTools.waitForElementToAppear(VERIFIED_EMAIL_ICON).isPresent();
    }

    @Override
    public void clickBackBtnOnNotificationMenu() {
        driverTools.click(CLOSE);
    }

    @Override
    public boolean toggleNotifAll(String[] settings, boolean status, String language) {
        for (String setting: settings
        ) {
            logger.log("Checking for: "+setting);

            if(language.equals("id")){
                setting = switch (setting)
                {
                    case "Activity" ->  "Aktivitas";
                    case "Reminders" -> "Pengingat";
                    case "Special For You" -> "Spesial Untukmu";
                    case "Membership" -> "Keanggotaan";
                    case "Account Activity" ->  "Aktivitas Akun";
                    case "Exclusive Info" -> "Info Eksklusif";
                    case "Blibli Tiket Rewards Info" -> "Info Blibli Tiket Rewards";
                    default -> throw new NotImplementedException("Not implemented: " + setting);
                };
            }

            boolean emailToggle =  toggleNotif(setting,"Email",status);
            boolean pushNotifToggle = toggleNotif(setting,"Push Notification",status);

            Assertion.assertThat("Email toggle for "+setting+" is set to "+status,emailToggle,is(true));
            Assertion.assertThat("Push Notification toggle for "+setting+" is set to "+status,pushNotifToggle,is(true));
        }
        return true;
    }

    private double getSwitchCircleLocation(Locator locator){
        WebElement switchToggleCircle = driverTools.swipeToElement(locator,Swipe.UP,2).orElseThrow();
        String currentPositionCircle = driverTools.getValueAttribute(switchToggleCircle, "bounds");
        currentPositionCircle = currentPositionCircle.split("]")[0].replaceAll("\\[", "");
        return CoreFunctions.convertPriceSafely(currentPositionCircle);
    }

    private boolean toggleNotif(String setting, String type, boolean status){
        WebElement switchToggle = driverTools.swipeToElement(getNotificationSwitchLocator(setting,type),Swipe.UP,5).orElseThrow();
        Locator switchToggleCircle = getNotificationSwitchCircleLocator(setting,type);
        double currentPositionCircle = getSwitchCircleLocation(switchToggleCircle);

        logger.log("toggling "+setting+" "+type+" to "+status+". Current bond position : " + currentPositionCircle);
        driverTools.click(switchToggle);    
        double newPositionCircle = getSwitchCircleLocation(switchToggleCircle);

        if(status == true && (newPositionCircle <  currentPositionCircle)){
            logger.log("Off condition retrying");
            currentPositionCircle = newPositionCircle;
            driverTools.click(switchToggle);
            newPositionCircle = getSwitchCircleLocation(switchToggleCircle);
        }else if(status == false && (newPositionCircle >  currentPositionCircle)){
            logger.log("Off condition retrying");
            currentPositionCircle = newPositionCircle;
            driverTools.click(switchToggle);
            newPositionCircle = getSwitchCircleLocation(switchToggleCircle);
        }
        logger.log("Notification status : " + status + ". Previous position : " + currentPositionCircle+". New position : " + newPositionCircle);
        boolean isCorrectPosition = status ? newPositionCircle >  currentPositionCircle : newPositionCircle <  currentPositionCircle;
        return isCorrectPosition;
    }

    @Override
    public void clickAccountCenter(){
        driverTools.swipeToElement(ACCOUNT_CENTER, Swipe.DOWN, 5).orElseThrow();
        driverTools.click(ACCOUNT_CENTER);
    }

    @Override
    public void openChangeCurrencyOverlayUsingDeeplink(){
        Deeplink deeplinkCurrencyOverlay = new Deeplink("CHANGE_CURRENCY_LIST_DEEPLINK","tiket://gatotkaca.tiket.com/account/currency-list","tiket://preprod.tiket.com/account/currency-list","tiket://tiket.com/account/currency-list");
        try {
            driverTools.deeplink(deeplinkCurrencyOverlay);
        }catch (Exception e){
            logger.fail("got exception : " + e.getLocalizedMessage());
        }
        driverTools.waitForElementToPresence(CLOSE);
        driverTools.waitForElementToDisappear(LOADING_STATE);
        driverTools.waitForElementToPresence(CURRENCY_RADIO_BUTTON);
        driverTools.switchToNative();
    }

    @Override
    public boolean isLoginWithCorrectCredentials(String username){
        String actualUsername = "";
        if(username.contains("@")){
            WebElement emailElement = driverTools.swipeToElement(EMAIL_MY_ACCOUNT_PAGE,Swipe.DOWN,3).orElseThrow();
            actualUsername = driverTools.getText(emailElement).trim().toLowerCase();
        }else {
            WebElement phoneElement = driverTools.swipeToElement(PHONE_NUMBER_MY_ACCOUNT_PAGE,Swipe.DOWN,3).orElseThrow();
            actualUsername = driverTools.getText(phoneElement).trim();
        }
        if(!actualUsername.contains(username.trim().toLowerCase())){
            logger.log("Actual username is -> " + actualUsername + " while expected username is -> " + username);
        }
        return actualUsername.contains(username.trim().toLowerCase());

    }
}