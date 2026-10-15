package com.tiket.page.setting;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.account.AccountPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPageIOS;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.tiket.page.home.HomePageIOS.HOME_ICON;
import static org.hamcrest.core.Is.is;

public class SettingPageIOS<T extends WebDriver> extends SettingPage<T> {
    public SettingPageIOS(Session<T> session) {
        super(session);
    }
    private static final Locator SAVE_PASSENGER_DATA = new Locator("SAVE_PASSENGER_DATA", AppiumBy.xpath("//*[@name=\"Save Passenger Data\" or @name=\"Simpan Data Penumpang\"]"));
    private static final Locator LOGOUT_BUTTON = new Locator("LOGOUT_BUTTON", AppiumBy.xpath("//*[@name='Log out']"));
    private static final Locator CONFIRM_LOGOUT = new Locator("CONFIRM_LOGOUT", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and ./parent::*[@name='confirm_log_out']]"));
    private static final Locator LOGIN_ICON =  new Locator("LOGIN_ICON",AppiumBy.id("account"));
    private static final Locator LOCATION_TOGGLE = new Locator("LOCATION_TOGGLE", AppiumBy.accessibilityId("switch"));
    private static final Locator LOCATION_TOGGLE_ON = new Locator("LOCATION_TOGGLE_ON", AppiumBy.accessibilityId("switch (selected)"));
    public static final Locator PRIMARY_BTN_ALLOW = new Locator("PRIMARY_BTN_ALLOW", AppiumBy.xpath("//XCUIElementTypeButton[@label='Allow access' or @label='Beri izin akses']"));
    public static final Locator PERMISSION_TYPE_ALLOW_ONCE = new Locator("PERMISSION_TYPE_ALLOW_ONCE", AppiumBy.xpath("//XCUIElementTypeButton[@label='Allow Once']"));
    private static final Locator ACCOUNT_MENU = new Locator("ACCOUNT_MENU", AppiumBy.accessibilityId("tds_ic_profile_manage"));
    private static final Locator CHANGE_PASSWORD = new Locator("CHANGE_PASSWORD", AppiumBy.accessibilityId("tds_ic_change_password"));
    private static final Locator NOTIFICATION_MENU = new Locator("NOTIFICATION_MENU", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Notification' or @label='Notifikasi']"));
    private static final Locator NOTIFICATION_MENU_TITLE = new Locator("NOTIFICATION_MENU_TITLE", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name=\"notification_header\"])[1]"));
    private static final Locator NOTIFICATION_ACTIVITY_NEW = new Locator("NOTIFICATION_ACTIVITY_NEW", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Account Activity' or @label='Aktivitas Akun']"));


    // security Menu
    private static final Locator SECURITY_MENU = new Locator("SECURITY_MENU", AppiumBy.accessibilityId("tds_ic_security"));
    private static final Locator DEVICE_MANAGEMENT_MENU = new Locator("DEVICE_MANAGEMENT_MENU", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == 'Pengaturan Perangkat' OR label == 'Device Management'`]"));
    private static final Locator CLOSE_SECURITY_SETTING = new Locator("CLOSE_SECURITY_SETTING", AppiumBy.id("tds ic back"));
    private static final Locator SECURITY_SETTINGS_HEADER = new Locator("SECURITY_SETTINGS_HEADER", AppiumBy.id("settings_header"));
    private static final Locator DELETE_ACCOUNT = new Locator("DELETE_ACCOUNT", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Hapus Akun' or @name='Delete Account']"));
    private static final Locator ALERT = new Locator("ALERT", AppiumBy.name("alert"));
    private static final Locator CHECKBOX = new Locator("CHECKBOX", AppiumBy.className("XCUIElementTypeSwitch"));
    private static final Locator CHECKBOX_CHECKED = new Locator("CHECKBOX_CHECKED", AppiumBy.xpath("//XCUIElementTypeSwitch[@value=1]"));
    private static final Locator CONTINUE = new Locator("CONTINUE", AppiumBy.className("XCUIElementTypeButton"));
    private static final Locator CHOOSE_REASON_DROPDOWN = new Locator("CHOOSE_REASON_DROPDOWN", AppiumBy.xpath("//XCUIElementTypeButton[not(@label)]"));
    private static final Locator CHOOSE_REASON_BUTTON = new Locator("CHOOSE_REASON_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@label='Select' or @label='Pilih']"));
    private static final Locator SUBMIT_DELETE = new Locator("SUBMIT_DELETE", AppiumBy.xpath("//XCUIElementTypeButton[@name='Submit request' or @name='Kirim pengajuan']"));
    private static final Locator DELETE_NOW = new Locator("DELETE_NOW", AppiumBy.xpath("//XCUIElementTypeButton[@name='Request now' or @name='Ajukan sekarang']"));
    private static final Locator SESSION_EXPIRED_ALERT = new Locator("SESSION_EXPIRED_ALERT", AppiumBy.xpath("//XCUIElementTypeAlert[@label='Your session is expired' or @label='Sesimu sudah habis']"));
    private static final Locator SKIP_ALERT = new Locator("SKIP_ALERT", AppiumBy.xpath("//XCUIElementTypeButton[@label='Go to Home' or @label='Kembali ke Home']"));

    // Device Management
    private static final Locator DEVICE_MANAGEMENT_SNACKBAR = new Locator("DEVICE_MANAGEMENT_SNACKBAR", AppiumBy.id("device_management_header"));
    private static final Locator DEVICE_MANAGEMENT_THIS_DEVICE = new Locator("DEVICE_MANAGEMENT_THIS_DEVICE", AppiumBy.xpath("//XCUIElementTypeCell//XCUIElementTypeStaticText[contains(@label,'Perangkat Ini') or contains(@label,'This Device')]"));
    private static final Locator DEVICE_MANAGEMENT_THIS_DEVICE_IPHONE = new Locator("DEVICE_MANAGEMENT_THIS_DEVICE_IPHONE", AppiumBy.xpath("//XCUIElementTypeCell//XCUIElementTypeStaticText[contains(@label,'iPhone')]"));

    // PIN Section
    private static final Locator SET_NEW_PIN = new Locator("SET_NEW_PIN", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == 'Buat PIN' OR label == 'Set PIN'`]"));
    private static final Locator CHANGE_PIN = new Locator("CHANGE_PIN", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == 'Ubah PIN' OR label == 'Change PIN' OR label == 'Ganti PIN'`]"));
    private static final Locator TRUST_DEVICE_POPUP_YES = new Locator("TRUST_DEVICE_POPUP_YES", AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label == 'Ya, Lanjutkan' OR label == 'Yes, Continue'`]"));
    private static final Locator ENTER_PIN = new Locator("ENTER_PIN", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == 'Masukkan PIN' OR label == 'Enter your PIN'`]"));
    private static final Locator CONFIRM_PIN = new Locator("CONFIRM_PIN", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == 'Konfirmasikan PIN' OR label == 'Confirm your PIN'`]"));
    private static final Locator SNACKBAR_CREATE_PIN = new Locator("SNACKBAR_CREATE_PIN", AppiumBy.iOSClassChain("**/*[`name == 'Buat PIN' OR name == 'Set PIN'`]"));
    private static final Locator SNACKBAR_VERIFY_ACCOUNT = new Locator("SNACKBAR_VERIFY_ACCOUNT", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Verif')]"));
    private static final Locator PIN_INPUT_BOX = new Locator("PIN_INPUT_BOX", AppiumBy.id("pin_field"));
    private static final Locator OTP_INPUT = new Locator("PIN_INPUT_BOX", AppiumBy.id("otp_field"));
    private static final Locator TOGGLE_DEVICE_TRUST = new Locator("TOGGLE_DEVICE_TRUST", AppiumBy.xpath("//*[contains(@label, 'Device as Trusted') or contains(@label, 'Perangkat Tepercaya')]//following-sibling::*[contains(@name,'switch')]"));
    private static final Locator TURN_ON_TRUST_DEVICE_SUCCESS = new Locator("TURN_ON_TRUST_DEVICE_SUCCESS", AppiumBy.iOSClassChain("**/*[`label CONTAINS 'Perangkat sudah dijadikan tepercaya' OR label CONTAINS 'set this device as trusted'`]"));
    private static final Locator TURN_OFF_TRUST_DEVICE_SUCCESS = new Locator("TURN_OFF_TRUST_DEVICE_SUCCESS", AppiumBy.iOSClassChain("**/*[`label CONTAINS 'Perangkat ini tidak lagi dijadikan tepercaya' OR label CONTAINS 'stopped using this as a trusted device'`]"));
    private static final Locator SINGLE_VERIFICATION_CHECKBOX = new Locator("SINGLE_VERIFICATION_CHECKBOX", AppiumBy.xpath("((((((//XCUIElementTypeStaticText[@label ='Single Verification' or @label='Verifikasi 1 Langkah']//parent::*)//parent::*[1])//parent::*[1])//parent::*[1])//parent::*[1])//preceding-sibling::*)[1]"));
    private static final Locator DOUBLE_VERIFICATION_CHECKBOX = new Locator("DOUBLE_VERIFICATION_CHECKBOX", AppiumBy.xpath("((((((//XCUIElementTypeStaticText[@label ='Double Verification' or @label='Verifikasi 2 Langkah']//parent::*)//parent::*[1])//parent::*[1])//parent::*[1])//parent::*[1])//preceding-sibling::*)[1]"));
    private static final Locator OTP_PAGE = new Locator("OTP_PAGE", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Verify Account' or @label='Verifikasi Akun']"));
    private static final Locator LANGUAGE_MENU = new Locator("LANGUAGE_MENU",AppiumBy.xpath("//*[@label='Language' or @label='Bahasa']"));
    private static final Locator BACK = new Locator("BACK", AppiumBy.accessibilityId("tds ic back"));
    private static final Locator BACK_NOTIFICATION_PAGE = new Locator("BACK_NOTIFICATION_PAGE", AppiumBy.accessibilityId("back"));
    public static final Locator CURRENT_CURRENCY = new Locator("CURRENT_CURRENCY", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Currency' or @label='Mata Uang']/preceding-sibling::XCUIElementTypeStaticText[string-length(@label) = 3 and translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz', '') = '']"));
    public static final Locator SEARCH_CURRENCY = new Locator("SEARCH_CURRENCY", AppiumBy.className("XCUIElementTypeTextField"));
    private static final Locator CLEAR_DEVICE_LIST = new Locator("CLEAR_DEVICE_LIST", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Disconnect All') or contains(@label,'Putuskan Semua')]"));
    private static final Locator CONFIRM_CLEAR_DEVICE = new Locator("CONFIRM_CLEAR_DEVICE", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'YES') or contains(@label,'YA')]"));
    private static final Locator CLOSE_CHANGE_CURRENY_SHEET = new Locator("CLOSE_CHANGE_CURRENY_SHEET", AppiumBy.accessibilityId("ic cancel grey"));
    private static final Locator ACCOUNT_CENTER = new Locator("ACCOUNT_CENTER", AppiumBy.xpath("//*[@type='XCUIElementTypeOther' and ./*[@name='To Account Center' or @name='Ke Pusat Akun']]"));

    //account setting
    private static final Locator CHANGE_EMAIL = new Locator("CHANGE_EMAIL", AppiumBy.xpath("(//XCUIElementTypeButton[@label='Change' or @label='Ganti'])[2]"));
    private static final Locator CHANGE_EMAIL_UNVERIFIED = new Locator("CHANGE_EMAIL_UNVERIFIED", AppiumBy.xpath("//*[contains(@label,'ganti email') or contains(@label,'change your email')]"));
    private static final Locator CONTINUE_CHANGE = new Locator("CONTINUE_CHANGE", AppiumBy.xpath("//XCUIElementTypeButton[@label='Continue' or @label='Lanjutkan']"));
    private static final Locator CHEVRON_OTP_EMAIL = new Locator("CHEVRON_OTP_EMAIL", AppiumBy.xpath("//*[contains(@label,'@')]/parent::*/following-sibling::*[@name='tds_ic_chevron_right']"));
    private static final Locator CHEVRON_OTP_PHONE = new Locator("CHEVRON_OTP", AppiumBy.xpath("//*[contains(@label,'+')]/parent::*/following-sibling::*[@name='tds_ic_chevron_right']"));
    private static final Locator NEW_EMAIL_FIELD = new Locator("NEW_EMAIL_FIELD", AppiumBy.id("emailField"));
    private static final Locator VERIFY_EMAIL_BUTTON = new Locator("VERIFY_EMAIL_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'Verif') and @visible='true']"));
    private static final Locator VERIFIED_EMAIL_ICON = new Locator("VERIFIED_EMAIL_ICON", AppiumBy.xpath("//*[contains(@label,'Email')]//following::*[@name='tds_ic_verified']"));
    private static final Locator LOYALTY_BENEFIT_LOGIN_IDR_FLAG = new Locator("LOYALTY_BENEFIT_LOGIN_IDR_FLAG", AppiumBy.xpath("//*[contains(@label,'Tier')]"));
    private static final Locator REWARDS_NON_LOGIN_IDR_FLAG = new Locator("REWARDS_NON_LOGIN_IDR_FLAG", AppiumBy.xpath("//*[contains(@label,'benefits here') or contains(@label,'Lihat benefit')]"));

    //myaccount page
    private static final Locator EMAIL_MY_ACCOUNT_PAGE = new Locator("EMAIL_MY_ACCOUNT_PAGE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'@') or contains(@label,'@') or contains(@value,'@')]"));
    private static final Locator PHONE_NUMBER_MY_ACCOUNT_PAGE = new Locator("PHONE_NUMBER_MY_ACCOUNT_PAGE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'+')]"));

    @Override
    public boolean isOnSettingPage(){
        WebElement langguageMenuElement = driverTools.waitForElementToAppear(LANGUAGE_MENU,5).orElseThrow();
        boolean isLangguageMenuVisible = driverTools.isElementDisplayed(langguageMenuElement);
        boolean isCurrencyMenuVisible = driverTools.isElementDisplayed(CURRENT_CURRENCY);
        logger.log("is langguage menu visible? -> " + isLangguageMenuVisible);
        logger.log("is currency menu visible ? -> " + isCurrencyMenuVisible);
        return isLangguageMenuVisible && isCurrencyMenuVisible;
    }

    @Override
    public void setLocationSwitch(String action){
        switch (action) {
            case ("ON") -> {
                if(!isLocationOn()) {enableLocationService();}
            }
            case ("OFF") -> {
                driverTools.waitForElementToAppear(LOCATION_TOGGLE_ON, 5).ifPresent(driverTools::click);
            }
            default -> {
                logger.fail("There is no '"+action+"' option");
            }
        }
    }

    private void enableLocationService() {
        driverTools.waitForElementToAppear(LOCATION_TOGGLE).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(PRIMARY_BTN_ALLOW).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(PERMISSION_TYPE_ALLOW_ONCE).ifPresent(driverTools::click);
    }

    @Override
    public boolean isLocationOn(){
        driverTools.waitForElementToAppear(LOCATION_TOGGLE_ON);
        return driverTools.isElementDisplayed(LOCATION_TOGGLE_ON);
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
        driverTools.swipeToElement(SET_NEW_PIN, Swipe.UP, 3).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(TRUST_DEVICE_POPUP_YES).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(SNACKBAR_CREATE_PIN).orElseThrow();
        driverTools.waitForElementToAppear(ENTER_PIN).orElseThrow();
        driverTools.pressKey(PIN_INPUT_BOX, pin);
        driverTools.waitForElementToDisappear(ENTER_PIN);
        driverTools.waitForElementToAppear(CONFIRM_PIN).orElseThrow();
        driverTools.pressKey(PIN_INPUT_BOX, pin);
        driverTools.waitForElementToAppear(SNACKBAR_VERIFY_ACCOUNT).orElseThrow();
        driverTools.pressKey(OTP_INPUT, "1234");
    }

    @Override
    public void changePin(String pin){} // FIXME: Implementation pending

    @Override
    public void forgotPin(String pin){} // FIXME: Implementation pending

    @Override
    public void switchDeviceTrust(boolean trustCondition){
        boolean actualDeviceTrust = false;
        WebElement toggleDeviceTrust = driverTools.swipeToElement(TOGGLE_DEVICE_TRUST, Swipe.UP, 3).orElseThrow();
        String valueDeviceTrust = driverTools.getValueAttribute(toggleDeviceTrust, "name");
        if (valueDeviceTrust.contains("selected")) actualDeviceTrust = true;
        while (trustCondition == actualDeviceTrust) actualDeviceTrust = normalizeDeviceTrust(actualDeviceTrust);
        driverTools.click(TOGGLE_DEVICE_TRUST);
    }

    private boolean normalizeDeviceTrust(boolean actualCondition) {
        WebElement toggleDeviceTrust = driverTools.waitForElementToPresence(TOGGLE_DEVICE_TRUST).orElseThrow();
        driverTools.click(toggleDeviceTrust);
        if (actualCondition) {
            setTrustDevicePopupYes();
        } else {
            setVerificationStep();
        }
        toggleDeviceTrust = driverTools.waitForElementToPresence(TOGGLE_DEVICE_TRUST).orElseThrow();
        String valueDeviceTrust = driverTools.getValueAttribute(toggleDeviceTrust, "name");
        boolean actualDeviceTrust = valueDeviceTrust.contains("selected");
        logger.log("normalizeDeviceTrust -> " + actualDeviceTrust);
        return actualDeviceTrust;
    }

    @Override
    public boolean verifyStatusDeviceTrust(boolean trustCondition) {
        driverTools.waitForElementToAppear(TOGGLE_DEVICE_TRUST).orElseThrow();
        String valueDeviceTrust = driverTools.getValueAttribute(TOGGLE_DEVICE_TRUST, "name");
        boolean actualDeviceTrust = valueDeviceTrust.contains("selected");
        logger.log("device trust toggle status -> " + actualDeviceTrust);
        return actualDeviceTrust;
    }

    @Override
    public boolean verifyNotifDeviceTrust(boolean trustCondition) {
        boolean notif;
        if (trustCondition) {
            notif = driverTools.waitForElementToAppear(TURN_ON_TRUST_DEVICE_SUCCESS).isPresent();
        } else {
            notif = driverTools.waitForElementToAppear(TURN_OFF_TRUST_DEVICE_SUCCESS).isPresent();
        }
        logger.log("device trust notification -> " + notif);
        return notif;
    }

    @Override
    public void setVerificationStep(){
        driverTools.waitForElementToAppear(SNACKBAR_VERIFY_ACCOUNT).orElseThrow();
        driverTools.pressKey(OTP_INPUT, "1234");
    }

    @Override
    public void setTrustDevicePopupYes() {
        WebElement yesPopup = driverTools.waitForElementToPresence(TRUST_DEVICE_POPUP_YES).orElseThrow();
        driverTools.click(yesPopup);
    }

    @Override
    public void openNotificationSettingPage(){
        driverTools.swipeToElement(NOTIFICATION_MENU, Swipe.UP, 25);
        driverTools.click(driverTools.waitForElementToAppear(NOTIFICATION_MENU).orElseThrow());
    }

    @Override
    public void backToHome() {
        driverTools.click(HOME_ICON);
    }

    @Override
    public void openSavePassengersData() {
        WebElement savePassengerDataElement = driverTools.swipeToElement(SAVE_PASSENGER_DATA,Swipe.UP,10).orElseThrow();
        driverTools.click(savePassengerDataElement);
    }

    @Override
    public boolean isNewPINSet() {
        return driverTools.waitForElementToAppear(CHANGE_PIN).isPresent();
    }

    @Override
    public void openDeviceManagement() {
        //trust device so that device list can be cleared
        WebElement toggleDeviceTrust = driverTools.swipeToElement(TOGGLE_DEVICE_TRUST, Swipe.UP, 3).orElseThrow();
        String valueDeviceTrust = driverTools.getValueAttribute(toggleDeviceTrust, "name");
        if (!valueDeviceTrust.contains("selected")){
            driverTools.click(TOGGLE_DEVICE_TRUST);
            setVerificationStep();
        }
        driverTools.click(DEVICE_MANAGEMENT_MENU);
    }

    @Override
    public boolean verifyDeviceManagementPage() {
        boolean isSnackbarDisplayed = driverTools.waitForElementToAppear(DEVICE_MANAGEMENT_SNACKBAR).isPresent();
        boolean isThisDeviceDisplayed = driverTools.waitForElementToAppear(DEVICE_MANAGEMENT_THIS_DEVICE).isPresent();
        boolean isThisDeviceIphone = driverTools.waitForElementToAppear(DEVICE_MANAGEMENT_THIS_DEVICE_IPHONE).isPresent();

        //disconnect all device to prepare for next run (due to long appium loading if too many devices)
        driverTools.swipeToElement(CLEAR_DEVICE_LIST,Swipe.UP,10).ifPresentOrElse(driverTools::click,()->{throw new RuntimeException("Clear device button not found");});
        driverTools.click(CONFIRM_CLEAR_DEVICE);

        return isSnackbarDisplayed && isThisDeviceDisplayed && isThisDeviceIphone;
    }

    public void userTurnOnSingleVerification() {
        driverTools.waitForElementToAppear(SINGLE_VERIFICATION_CHECKBOX).ifPresent(driverTools::click);
        if(driverTools.isElementDisplayed(OTP_PAGE)){
            driverTools.pressKey(OTP_INPUT, "1234");
        }
    }

    @Override
    public void userTurnOnDoubleVerification() {
        driverTools.waitForElementToAppear(DOUBLE_VERIFICATION_CHECKBOX).ifPresent(driverTools::click);
    }
    @Override
    public boolean isDoubleVerificationON() {
        driverTools.click(SINGLE_VERIFICATION_CHECKBOX);
        driverTools.isElementDisplayed(OTP_PAGE);
        driverTools.pressKey(OTP_INPUT, "1234");
        driverTools.isElementDisplayed(CLOSE_SECURITY_SETTING);


        driverTools.click(DOUBLE_VERIFICATION_CHECKBOX);
        return !driverTools.isElementDisplayed(OTP_PAGE) && driverTools.isElementDisplayed(CLOSE_SECURITY_SETTING);
    }


    @Override
    public boolean isSingleVerificationON() {
        driverTools.click(SINGLE_VERIFICATION_CHECKBOX);

        if (driverTools.isElementDisplayed(OTP_PAGE)) {
            driverTools.pressKey(OTP_INPUT, "1234");
            return driverTools.isElementDisplayed(CLOSE_SECURITY_SETTING);
        }
        else{
            return driverTools.isElementDisplayed(SINGLE_VERIFICATION_CHECKBOX);
        }
    }

    @Override
    public String checkCurrentCurrency() {
        driverTools.swipeToElement(CURRENT_CURRENCY,Swipe.UP,5);
        return driverTools.getText(CURRENT_CURRENCY).orElseThrow();
    }

    @Override
    public void changeCurrency(String inputCurrency) {
        driverTools.waitForElementToAppear(BACK);
        driverTools.swipeToElement(CURRENT_CURRENCY, Swipe.UP, 10);
        String currentCurrency = driverTools.getText(CURRENT_CURRENCY).orElseThrow();
        logger.log("Current currency: "+currentCurrency);

        String currencyOptionOne = inputCurrency.split("/")[0].toUpperCase();
        String currencyOptionTwo = inputCurrency.split("/")[1].toUpperCase();

        String currency = currencyOptionOne;
        if(currentCurrency .equals(currency)) {
            currency = currencyOptionTwo;
        }

        driverTools.click(CURRENT_CURRENCY);
        driverTools.waitForElementToAppear(SEARCH_CURRENCY);
        driverTools.click(SEARCH_CURRENCY);
        driverTools.sendKeys(SEARCH_CURRENCY,currency);
        driverTools.click(getCurrencyLocator(currency));

        driverTools.waitForElementToAppear(BACK);
    }

    @Override
    public void changeCurrencyIfOtherCurrencyIsSelected(String currency){
        WebElement currentCurrencyElement = driverTools.swipeToElement(CURRENT_CURRENCY,Swipe.UP,25).orElseThrow();
        String currentCurrency = driverTools.getText(currentCurrencyElement);
        logger.log("Current currency: "+currentCurrency);
        if(!currentCurrency.equalsIgnoreCase(currency)){
            driverTools.swipeUp();
            driverTools.click(CURRENT_CURRENCY);
            driverTools.waitForElementToAppear(SEARCH_CURRENCY);
            driverTools.click(SEARCH_CURRENCY);
            driverTools.sendKeys(SEARCH_CURRENCY,currency);
            WebElement currencyElement = driverTools.waitForElementToAppear(getCurrencyLocator(currency),5).orElseThrow();
            if(isCurrencySelected(currency)){
                driverTools.click(CLOSE_CHANGE_CURRENY_SHEET);
            }else{
                driverTools.click(currencyElement);
            }
        }
        WebElement currentCurrencyElement2 = driverTools.swipeToElement(CURRENT_CURRENCY,Swipe.UP,25).orElseThrow();
        String currentCurrency2 = driverTools.getText(currentCurrencyElement2);
        logger.log("Current currency: "+currentCurrency2);

        Assertion.assertThat("Is currency correct", currentCurrency2.equalsIgnoreCase(currency),is(true));
    }

    @Override
    public void changeCurrencyIfOtherCurrencyIsSelectedDeeplink(String currency){
        //open change currency overlay using deeplink
        openChangeCurrencyOverlayUsingDeeplink();
        driverTools.waitForElementToAppear(SEARCH_CURRENCY);
        if(!driverTools.isElementDisplayed(SEARCH_CURRENCY))
            openCurrencyPage();
        selectCurrencyFromOpenBottomSheet(currency);
    }

    @Override
    public void selectCurrencyFromOpenBottomSheet(String currency){
        driverTools.waitForElementToAppear(SEARCH_CURRENCY).orElseThrow();
        driverTools.click(SEARCH_CURRENCY);
        driverTools.sendKeys(SEARCH_CURRENCY,currency);
        WebElement currencyElement = driverTools.waitForElementToAppear(getCurrencyLocator(currency),5).orElseThrow();
        if(isCurrencySelected(currency)){
            driverTools.click(CLOSE_CHANGE_CURRENY_SHEET);
        }else{
            driverTools.click(currencyElement);
        }
        driverTools.waitForElementToDisappear(CLOSE_CHANGE_CURRENY_SHEET);
    }

    private void openCurrencyPage(){
        HomePage<T> homePage = HomePage.getInstance(session);
        AccountPage<T> accountPage = AccountPage.getInstance(session);
        homePage.openAccount();
        accountPage.openCurrencyPage();
    }

    private boolean isCurrencySelected(String currency){
        //solve problem where account currency changed by other test run (different session) but value haven't refelected since app haven't reload yet
        Locator CURRENCY_RADIO_BTN_LOCATOR = new Locator("CURRENCY_RADIO_BTN_LOCATOR", AppiumBy.xpath("//XCUIElementTypeCell[XCUIElementTypeStaticText[contains(@label,'"+currency+"')]]//*[@name='radioButton (selected)']"));
        return driverTools.isElementDisplayed(CURRENCY_RADIO_BTN_LOCATOR);
    }

    private Locator getCurrencyLocator(String currency){
        return new Locator("currency",AppiumBy.xpath("//XCUIElementTypeOther[contains(@name,'radioButton')]//preceding-sibling::*[@label='"+currency.toUpperCase()+"']"));
    }
    
    public boolean isSecurityPageOpen() {
        boolean isSecurityHeaderVisible =  driverTools.isElementDisplayed(driverTools.waitForElementToAppear(SECURITY_SETTINGS_HEADER,10).orElseThrow());
        return isSecurityHeaderVisible;
    }

    @Override
    public boolean isOnLoginSettingPage() {
       boolean isSettingBasicMenuVisible = isOnSettingPage();
       boolean isLogOutButtonVisible = driverTools.isElementDisplayed(LOGOUT_BUTTON);
       boolean isAccountSettingVisible = driverTools.isElementDisplayed(ACCOUNT_MENU);
       boolean isChangePasswordMenuVisible = driverTools.isElementDisplayed(CHANGE_PASSWORD);
       boolean isSecurityMenuVisible = driverTools.isElementDisplayed(SECURITY_MENU);
       boolean isNotificationMenuVisible = driverTools.isElementDisplayed(NOTIFICATION_MENU);

       logger.log("is logout button visible? -> " + isLogOutButtonVisible);
       logger.log("is security menu visible? -> " + isSecurityMenuVisible);
       logger.log("is ACCOUNT menu visible? -> " + isAccountSettingVisible);
       logger.log("is change password visible? -> "+isChangePasswordMenuVisible);
       logger.log("is notification visible? -> "+isNotificationMenuVisible);
       return isSettingBasicMenuVisible && isLogOutButtonVisible && isAccountSettingVisible && isChangePasswordMenuVisible 
                && isSecurityMenuVisible && isNotificationMenuVisible; 
    }

    @Override
    public void logOut() {
        driverTools.swipeToElement(LOGOUT_BUTTON,Swipe.UP,15);
        WebElement e = driverTools.waitForElementToAppear(LOGOUT_BUTTON,10).orElseThrow();
        driverTools.click(e);
        e = driverTools.waitForElementToAppear(CONFIRM_LOGOUT,10).orElseThrow();
        driverTools.click(e);
        driverTools.waitForElementToBeClickable(LOGIN_ICON,10);
    }

    private Locator getPushNotificationSwitchLocator(String category, String switchType){
        Locator switchTypeLocator = new Locator(switchType,AppiumBy.xpath("//XCUIElementTypeStaticText[@label='"+category+"']/parent::*/*[contains(@label,'"+switchType+"')]"));

        Locator switchesLocator = new Locator("switchesLocator",AppiumBy.xpath("//XCUIElementTypeStaticText[@label='"+category+"']/parent::*/XCUIElementTypeOther"));

        int yValueType = Integer.parseInt(CoreFunctions.getDataFromJsonString(driverTools.getValueAttribute(switchTypeLocator,"rect"),"y"));
        logger.log("y value of "+switchType+" : "+yValueType);

        List<WebElement> switches = driverTools.elements(switchesLocator).orElseThrow();

        int closest=Integer.MAX_VALUE;
        int closestIndex=0;

        for(int index=0;index<switches.size();index++){
            int yValue = Integer.parseInt(CoreFunctions.getDataFromJsonString(driverTools.getValueAttribute(switches.get(index),"rect"),"y"));

            int yDiff = Math.abs((yValueType-yValue));
            if(yDiff < closest ){
                logger.log("closest y value : "+yValue);
                closest = yDiff;
                closestIndex = index;
            }

        }

        return new Locator("switchesLocator",AppiumBy.xpath("(//XCUIElementTypeStaticText[@label='"+category+"']/parent::*/XCUIElementTypeOther)["+(closestIndex+1)+"]"));
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
        driverTools.click(BACK);
    }

    @Override
    public String getNotificationSettingLanguage() {
        String title = driverTools.getText(NOTIFICATION_MENU_TITLE).orElseThrow();

        String language = switch (title) {
            case "Pengaturan Notifikasi" -> "id";
            case "Notification Settings" -> "en";
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

            boolean emailToggle = driverTools.swipeToElement(getPushNotificationSwitchLocator(setting,"Email"),Swipe.UP,5).isPresent();
            boolean pushNotifToggle = driverTools.swipeToElement(getPushNotificationSwitchLocator(setting,"Push Notification"),Swipe.UP,5).isPresent();

            Assertion.assertThat("Email toggle for "+setting+" is found? " +emailToggle,emailToggle,is(true));
            Assertion.assertThat("Push Notification toggle for "+setting+" is found? " +pushNotifToggle,pushNotifToggle,is(true));
        }
        return true;
    }

    @Override
    public void clickDeleteAccount(){
       driverTools.click(driverTools.swipeToElement(DELETE_ACCOUNT,Swipe.UP,3).orElseThrow());
    }

    @Override
    public void fillDeleteAccountForm(){
        driverTools.waitForElementToPresence(ALERT).orElseThrow();
        driverTools.swipeUp();
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

        int index = CoreFunctions.generateRandomInt(0,reasonOptions.length-1);

        String[] chosenReason = reasonOptions[index];

        Locator reasonLocator = getReasonLocator(chosenReason[0],chosenReason[1]);
        Locator buttonLocator = getButtonLocator(chosenReason[2],chosenReason[3]);

        driverTools.click(reasonLocator);
        driverTools.click(CHOOSE_REASON_BUTTON);

        Assertion.assertThat("Second button is according to chosen reason",driverTools.waitForElementToAppear(buttonLocator).isPresent(),is(true));
    }

    private Locator getReasonLocator(String reasonEng,String reasonId){
        return new Locator(reasonEng,AppiumBy.xpath("//XCUIElementTypeStaticText[@name='"+reasonEng+"' or @name='"+reasonId+"']"));
    }

    private Locator getButtonLocator(String buttonEng,String buttonId){
        return new Locator(buttonEng,AppiumBy.xpath("//XCUIElementTypeButton[@label='"+buttonEng+"' or @label='"+buttonId+"']"));
    }

    @Override
    public void clickSubmitDelete(){
        driverTools.click(SUBMIT_DELETE);
        driverTools.waitForElementToAppear(DELETE_NOW).ifPresentOrElse(driverTools::clickElementByCoordinate,()->{throw new RuntimeException("Delete now button not found");});
    }

    @Override
    public boolean isSessionExpired(){
        boolean isAlertShowing = driverTools.waitForElementToAppear(SESSION_EXPIRED_ALERT).isPresent();
        driverTools.click(SKIP_ALERT);
        return isAlertShowing;
    }

    public void openAccountSetting(){
        driverTools.click(ACCOUNT_MENU);
    }

    public boolean isChangeDetailSnackbarDisplayed(String snackbarType){
        String textEng = "Yay! You’ve successfully changed your email.";
        String textId = "Hore! Kamu berhasil mengganti email.";
        if(snackbarType.equals("phone")){
            //todo
        }
        Locator snackbar = new Locator("snackbar",AppiumBy.xpath("//XCUIElementTypeStaticText[@label='"+textEng+"' or @label='"+textId+"']"));
        return driverTools.waitForElementToAppear(snackbar).isPresent();
    }

    public boolean isNewEmailShowing(String emailToCheck){
        Locator newEmail = new Locator(emailToCheck,AppiumBy.xpath("//XCUIElementTypeStaticText[@label='"+emailToCheck+"']"));
        return driverTools.waitForElementToAppear(newEmail).isPresent();
    }

    @Override
    public void clickBackBtnOnNotificationMenu() {
         driverTools.click(BACK_NOTIFICATION_PAGE);
    }

    public boolean isEmailVerified(){
        return driverTools.waitForElementToAppear(VERIFIED_EMAIL_ICON).isPresent();
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

            Assertion.assertThat("Email toggle for "+setting+" is set to "+status,emailToggle,is(status));
            Assertion.assertThat("Push Notification toggle for "+setting+" is set to "+status,pushNotifToggle,is(status));
        }
        return true;
    }

    @SneakyThrows
    private boolean toggleNotif(String setting, String type, boolean status){
        Locator switchLocator = getPushNotificationSwitchLocator(setting,type);
        WebElement switchToggle = driverTools.swipeToElement(switchLocator,Swipe.UP,5).orElseThrow();

        logger.log("toggling "+setting+" "+type+" to "+status);

        boolean currentStatus = !(driverTools.getValueAttribute(switchToggle,"name") == null);
        logger.log("Current "+setting+" "+type+" switch status: " + currentStatus);

        if(currentStatus == status){
            logger.log("Current switch status is the same as expected, clicking to change first");
            driverTools.click(switchToggle);
            Thread.sleep(500);
        }

        switchToggle = driverTools.element(getPushNotificationSwitchLocator(setting,type)).orElseThrow();
        driverTools.click(switchToggle);
        Thread.sleep(500);
        currentStatus = !(driverTools.getValueAttribute(getPushNotificationSwitchLocator(setting,type),"name") == null);
        logger.log("Current "+setting+" "+type+" switch status: " + currentStatus);
        return currentStatus;
    }

    @Override
    public void clickAccountCenter(){
        driverTools.waitForElementToPresence(ACCOUNT_CENTER);
        driverTools.click(ACCOUNT_CENTER);
    }

    @Override
    public boolean isLoginWithCorrectCredentials(String username){
        String actualUsername = "";
        if(username.contains("@")){
            WebElement emailElement = driverTools.swipeToElement(EMAIL_MY_ACCOUNT_PAGE, Swipe.DOWN, 8).orElseThrow();
            actualUsername = driverTools.getText(emailElement).trim().toLowerCase();
        }else {
            WebElement phoneElement = driverTools.swipeToElement(PHONE_NUMBER_MY_ACCOUNT_PAGE, Swipe.DOWN, 3).orElseThrow();
            actualUsername = driverTools.getText(phoneElement).trim().toLowerCase();
        }
        boolean matches = actualUsername.contains(username.trim().toLowerCase());
        if (!matches) {
            logger.log("Actual username is -> " + actualUsername + " while expected username is -> " + username);
        }
        return matches;
    }

    @Override
    public void openChangeCurrencyOverlayUsingDeeplink(){
        Deeplink deeplinkCurrencyOverlay = new Deeplink("CHANGE_CURRENCY_LIST_DEEPLINK","tiket://gatotkaca.tiket.com/account/currency-list","tiket://preprod.tiket.com/account/currency-list","tiket://tiket.com/account/currency-list");
        try {
            driverTools.deeplink(deeplinkCurrencyOverlay);
        }catch (Exception e){
            logger.fail("got exception : " + e.getLocalizedMessage());
        }
        if (driverTools.waitForElementToPresence(LoginPageIOS.REVIEW_APP_STORE, 5).isPresent()) {
            driverTools.clickElementByCoordinate(LoginPageIOS.REVIEW_APP_STORE);
        }
        driverTools.waitForElementToPresence(CLOSE_CHANGE_CURRENY_SHEET);
    }
}

