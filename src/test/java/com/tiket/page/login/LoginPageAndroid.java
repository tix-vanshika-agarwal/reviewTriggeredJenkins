package com.tiket.page.login;

import com.tiket.appium.Swipe;
import com.tiket.model.Deeplink;
import com.tiket.model.Exception.ForbiddenException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.account.AccountPage;
import com.tiket.page.home.HomePage;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.core.Is.is;

public class LoginPageAndroid <T extends WebDriver> extends LoginPage<T> {

    private static final Locator LOADING_STATE = new Locator("LOADING_STATE", AppiumBy.xpath("//*[contains(@resource-id,'modal-root')]//android.widget.Image"));
    private static final Locator LOG_IN_BTN_ORDER_PAGE= new Locator("LOG_IN_BTN_ORDER_PAGE",AppiumBy.id("tds_subtitle_notification_banner"));
    private static final Locator CREATE_ACCOUNT_BUTTON = new Locator("CREATE_ACCOUNT_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Buat Akun') or contains(@text,'Log In') or contains(@text,'Create Account') or contains(@text,'Continue') or contains(@text,'Lanjutkan')) and not(contains(@text,'Payment') or contains(@text,'Pembayaranmu'))]"));
    private static final Locator EMAIL_OR_PHONE_NUMBER_FIELD = new Locator("EMAIL_OR_PHONE_NUMBER_FIELD", AppiumBy.id("tds_box_input_text"));
    private static final Locator PASSWORD_FIELD = new Locator("PASSWORD_FIELD",AppiumBy.xpath("//*[contains(@resource-id,'tf_password')]//android.widget.EditText"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.id("tds_button_view"));
    private static final Locator BACK_BUTTON_IN_VERIFY_PHONE_NUMBER_FORM = new Locator("BACK_BUTTON_IN_VERIFY_PHONE_NUMBER_FORM", AppiumBy.id("tds_icon_navigation_toolbar"));
    private static final Locator CLOSE_REGISTER_FACE_ID_FORM_BUTTON = new Locator("CLOSE_REGISTER_FACE_ID_FORM_BUTTON", AppiumBy.id("iv_close"));
    private static final Locator ACTIVATE_FACE_ID_LATER_BUTTON = new Locator("ACTIVATE_FACE_ID_LATER_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and not(contains(@text,'Pembayaran') or contains(@text,'Payment') or contains(@text,'Cari') or contains(@text,'Search') or contains(@text,'See All') or contains(@text,'Lihat Semua'))]"));
    private static final Locator CLOSE_LOGIN_PAGE = new Locator("CLOSE_LOGIN_PAGE", AppiumBy.id("tds_icon_navigation_toolbar"));
    private static final Locator VERIFY_PHONE_NUMBER_PAGE = new Locator("VERIFY_PHONE_NUMBER_PAGE", AppiumBy.id("tds_header_navigation_appbar"));
    private static final Locator REGISTER_FULL_NAME = new Locator("REGISTER_FULL_NAME",AppiumBy.xpath("//*[contains(@resource-id,'tf_full_name')]//android.widget.EditText"));
    private static final Locator REGISTER_EMAIL= new Locator("REGISTER_EMAIL",AppiumBy.xpath("//*[contains(@resource-id,'tf_email')]//android.widget.EditText"));
    private static final Locator REGISTER_SUCCESS_POPUP = new Locator("REGISTER_SUCCESS_POPUP",AppiumBy.id("design_bottom_sheet"));
    private static final Locator REGISTER_SUCCESS_CONTINUE_BUTTON = new Locator("REGISTER_SUCCESS_CONTINUE_BUTTON", AppiumBy.id("tds_button_view"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and contains(@text,'Log in')]"));
    private static final Locator FORGOT_PASSWORD= new Locator("FORGOT_PASSWORD",AppiumBy.id("tv_forgot_password"));
    private static final Locator FORGOT_PASSWORD_FIELD= new Locator("FORGOT_PASSWORD_FIELD",AppiumBy.id("tds_box_input_text"));
    private static final Locator FORGOT_PASSWORD_BTN= new Locator("FORGOT_PASSWORD_BTN",AppiumBy.id("tds_button_view"));
    private static final Locator PHONE_FIELD= new Locator("PHONE_FIELD",AppiumBy.xpath("//*[contains(@resource-id,'tf_phone_number')]//android.widget.EditText"));
    private static final Locator VERIFY_EMAIL= new Locator("VERIFY_EMAIL",AppiumBy.id("tds_btn_primary_info_view"));
    private static final Locator JOIN_NOW_SRP= new Locator("JOIN_NOW_SRP",AppiumBy.xpath("//*[contains(@resource-id,'tds_btn') and (contains(@text,'Gabung Sekarang') or contains(@text,'Join Now'))]"));
    private static final Locator TDS_BTN = new Locator("TDS_BTN", AppiumBy.xpath("//*[contains(@resource-id,'tds_btn')]"));
    private static final Locator NOTIFICATION_PERMISSION_MAYBE_LATER_BUTTON = new Locator("NOTIFICATION_PERMISSION_MAYBE_LATER_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (@text='Maybe later' or @text='Nanti saja')]"));
    private static final Locator ENTER_PIN_FIELD= new Locator("ENTER_PIN_FIELD",AppiumBy.xpath("//*[contains(@resource-id, 'et_pin_')]"));
    private static final Locator ENTER_PIN_HEADING= new Locator("ENTER_PIN_HEADING",AppiumBy.id("ll_wrapper"));
    private static final Locator INDONESIA_PHONE= new Locator("INDONESIA_PHONE",AppiumBy.xpath("//*[contains(@text,'+62')]"));
    private static final Locator COUNTRY_CODE= new Locator("COUNTRY_CODE",AppiumBy.id("tds_country_code_icon_image"));
    private static final Locator PHONE_NUMBER_VERIFICATION_BTN= new Locator("PHONE_NUMBER_VERIFICATION_BTN",AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Verify Mobile Number') or contains(@text,'Verifikasi Nomor Ponsel'))]"));
    private static final Locator CORPORATE_LOGIN_LINK = new Locator("CORPORATE_LOGIN_LINK", AppiumBy.xpath("//*[contains(@resource-id,'iv_corporate_login')]//following-sibling::*[contains(@text,'tiket.com for Corporates')]"));
    private static final Locator CHANGE_EMAIL_BUTTON = new Locator("CHANGE_EMAIL_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_btn_secondary_info_view')]//*[contains(@resource-id,'tds_button_view')]"));
    private static final Locator B2B_CONTINUE_BUTTON = new Locator("B2B_CONTINUE_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_btn_primary_info_view')]//*[contains(@resource-id,'tds_button_view')]"));
    private static final Locator CROSS_BTN_PNV = new Locator("CROSS_BTN_PNV", AppiumBy.id("tds_icon_navigation_toolbar_group"));
    private static final Locator LOGOUT_BUTTON = new Locator("LOGOUT_BUTTON", AppiumBy.id("menu_logout"));
    private static final Locator CONFIRM_LOGOUT = new Locator("CONFIRM_LOGOUT",AppiumBy.xpath("//android.widget.Button[contains(@resource-id,'tds_button_view') and @text='Log out' or @text='Keluar']"));
    public static final Locator GOOGLE_LOGIN_NATIVE_ANDROID = new Locator("GOOGLE_LOGIN_NATIVE_ANDROID", AppiumBy.xpath("//*[contains(@resource-id,'sign-in-with-google')]"));
    public static final Locator CONTINUE_AS_GUEST = new Locator("CONTINUE_AS_GUEST", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Continue as guest') or contains(@text,'Lanjut sebagai tamu'))]"));
    private static Locator INPUT_FACEBOOK_EMAIL = new Locator("INPUT_FACEBOOK_EMAIL", AppiumBy.xpath("//*[@resource-id=\"m_login_email\"]"));
    private static Locator INPUT_FACEBOOK_PASSWORD = new Locator("INPUT_FACEBOOK_PASSWORD", AppiumBy.xpath("//*[@resource-id=\"m_login_password\"]"));
    private static Locator  LOGIN_FACEBOOK= new Locator("LOGIN_FACEBOOK", AppiumBy.xpath("//*[@text=\"Log in\"]"));
    private static Locator INPUT_GOOGLE_EMAIL = new Locator("INPUT_GOOGLE_EMAIL", AppiumBy.xpath("//*[@resource-id=\"identifierId\"]"));
    private static Locator INPUT_GOOGLE_PASSWORD = new Locator("INPUT_GOOGLE_PASSWORD", AppiumBy.xpath("//*[@class=\"android.widget.EditText\"]"));
    private static Locator  NEXT_GOOGLE_EMAIL= new Locator("NEXT_GOOGLE", AppiumBy.xpath("//*[@resource-id=\"identifierNext\"]//android.widget.Button"));
    private static Locator  NEXT_GOOGLE_PASSWORD= new Locator("NEXT_GOOGLE", AppiumBy.xpath("//*[@resource-id=\"passwordNext\"]//android.widget.Button"));
    private static Locator  NOTNOW_FACEBOOK= new Locator("NOTNOW_FACEBOOK", AppiumBy.xpath("//*[@text='Not now']"));
    private static Locator  CHROME= new Locator("CHROME", AppiumBy.xpath("//*[contains(@resource-id,'chrome')]"));
    private static Locator DIFF_DEVICE_PASSKEY = new Locator("DIFF_DEVICE_PASSKEY", AppiumBy.id("com.google.android.gms:id/cancel_or_use_another_device_button"));

    public LoginPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void loginAsGuest() {
        logger.log("Logging in as guest");
        driverTools.waitForElementToPresence(TDS_BTN, 10).ifPresent(driverTools::click);
        AccountPage<T> accountPage = AccountPage.getInstance(session);
        HomePage<T> homePage = HomePage.getInstance(this.session);
        if (homePage.isUserLogin()) {
            logger.log("App state : USER LOGIN");
            homePage.openAccount();
            driverTools.swipeToElement(LOGOUT_BUTTON,Swipe.UP,25);
            driverTools.swipeUp();
            driverTools.click(LOGOUT_BUTTON);
            if(driverTools.waitForElementToPresence(CONFIRM_LOGOUT).isEmpty()){
                logger.log("Logout button not found, try again using deeplink");
                driverTools.clickElementByCoordinate(LOGOUT_BUTTON);
                if(driverTools.waitForElementToPresence(CONFIRM_LOGOUT).isEmpty()){
                    driverTools.closeApp();
                    homePage.goToHomeDeeplink();
                    accountPage.openAccountPageUsingDeeplink();
                    driverTools.swipeToElement(LOGOUT_BUTTON,Swipe.UP,25);
                    driverTools.swipeUp();
                    driverTools.click(LOGOUT_BUTTON);
                }
                logger.log("Clicked logout button on retry");
            }
            driverTools.click(CONFIRM_LOGOUT);
            driverTools.waitForElementToDisappear(CONFIRM_LOGOUT);
            driverTools.waitForElementToPresence(CONTINUE_AS_GUEST,3).ifPresent(driverTools::clickElementByCoordinate);
            if(!driverTools.waitForElementToDisappear(LOGOUT_BUTTON,10)){
                driverTools.click(LOGOUT_BUTTON);
                driverTools.waitForElementToAppear(CONFIRM_LOGOUT,10).orElseThrow();
                driverTools.click(CONFIRM_LOGOUT);
                if (driverTools.waitForElementToPresence(CONTINUE_AS_GUEST,5).isPresent()) driverTools.clickElementByCoordinate(CONTINUE_AS_GUEST);
            }
            //wait for logout complete
            homePage.waitForHomePageToLoad();
        }
    }

    private boolean isOnLoginPageWebview() {        
        boolean isWebview = driverTools.waitForElementToPresence(CHROME,5).isPresent();
        driverTools.waitForElementToDisappear(LOADING_STATE);
        closeCredentialsPicker();
        return isWebview;
    }

    private void closeCredentialsPicker() {
        if (driverTools.waitForElementToPresence(DIFF_DEVICE_PASSKEY, 5).isPresent())
            driverTools.back();
    }

    @Override
    public void inputEmailOrPhoneNumberNative(String username){
        driverTools.waitForElementToAppear(EMAIL_OR_PHONE_NUMBER_FIELD);
        if(driverTools.isElementDisplayed(EMAIL_OR_PHONE_NUMBER_FIELD)){
            driverTools.sendKeys(EMAIL_OR_PHONE_NUMBER_FIELD, username);
            driverTools.click(CREATE_ACCOUNT_BUTTON);
            driverTools.waitForElementToDisappear(CREATE_ACCOUNT_BUTTON);
        }
    }

    @Override
    public void closeUnmPageRedirection(){
    }//no implementation needed

    @Override
    public void loginWith(String loginMethod,String username,String password) {
        boolean isLoginSuccess=false;
        HomePage<T> homePage = HomePage.getInstance(this.session);
        try{
            if(homePage.isUserLogin()){
                logger.log("User already login, skip login");
                isLoginSuccess = true;
            }else {
                logger.log("webview unm page");
                //reset chrome app to avoid webview stuck
                driverTools.terminateAppAndroid("com.android.chrome");
                openLoginPageWithDeeplink(); // here autologin is happening
                if(homePage.isOnHomePage() && homePage.isUserLogin()) {
                    boolean isSameUserAlreadyLogin = homePage.openAccountCenterViaDeeplink(username);
                    logger.log("Autologin detected " + (isSameUserAlreadyLogin ? "from same user" : "from different user"));
                    if(isSameUserAlreadyLogin){
                        logger.log("User already login, skip login");
                        homePage.goToHomeDeeplink();
                        return;
                    }else{
                        throw new ForbiddenException("Existing/Autologin from different user detected, stopping test");
                    }
                }
                driverTools.waitForElementToDisappear(LOADING_STATE);
                isLoginSuccess = loginWithWebview(loginMethod, username, password);
                if(!isLoginSuccess){
                    throw new Exception("Login got no exception but status is not logged in");
                }
            }
        } catch (ForbiddenException forbiddenException) {
            throw forbiddenException;   // Terminate session to avoid auto-login loop in next tests
        } catch (Exception e){
            logger.softWarning("Login first attempt failed, error: "+e.getLocalizedMessage() +" trying one more time");
            homePage.goToHomeDeeplink();
            logger.log("webview unm page");
            openLoginPageWithDeeplink();
            driverTools.switchToNative();
            driverTools.waitForElementToDisappear(LOADING_STATE);
            if(driverTools.waitForElementToPresence(CONTINUE_BUTTON, 5).isPresent()){
                driverTools.click(CONTINUE_BUTTON);
                driverTools.waitForElementToDisappear(CONTINUE_BUTTON);
                isLoginSuccess = homePage.isUserLogin();
            } else {
                isLoginSuccess = loginWithWebview(loginMethod, username, password);
            }
        }
        homePage.coachMarkHandling();
        Assertion.assertThat("Login success",isLoginSuccess,is(true));
    }

    @Override
    public boolean loginWithB2BRetry(String loginMethod,String username,String password) {
        // input credential based on available method.
        // can only be used if user in homepage
        HomePage<T> homePage = HomePage.getInstance(this.session);
        boolean isUserLogin = homePage.isUserLogin();
        if(isUserLogin){
            logger.log("User already login, skip login");
            return true;
        }else {
            logger.log("webview unm page");
            openLoginPageWithDeeplink();
            driverTools.switchToNative();
            homePage.coachMarkHandling();
            return super.loginWithB2BRetry(loginMethod, username, password);
        }
    }

    @Override
    public void loginWith(String loginMethod, String username, String password, boolean isOpenWebView) {
        HomePage<T> homePage = HomePage.getInstance(this.session);
        if (homePage.isUserLogin()) {
            logger.log("User already login, skip login");
        } else {
            openLoginPage(isOpenWebView);
            boolean isLoginSuccess = loginWithWebview(loginMethod, username, password);
            homePage.coachMarkHandling();
            Assertion.assertThat("Login success", isLoginSuccess, is(true));
        }
    }

    private void openLoginPage(boolean isOpenWebView) {
        if (isOpenWebView) {
            logger.log("webview unm page");
            openLoginPageWithDeeplink();
            driverTools.switchToNative();
        } else {
            logger.log("open login page with native button");
            openLoginPage();
        }
    }

    @Override
    public boolean isChangeEmailButtonVisible() {
        return driverTools.waitForElementToPresence(CHANGE_EMAIL_BUTTON).isPresent();
    }


    @Override
    public String getChangeEmailButtonText() {
        return driverTools.getText(CHANGE_EMAIL_BUTTON).orElseThrow();
    }

    @Override
    public boolean isB2BContinueButtonVisible() {
        return driverTools.waitForElementToPresence(B2B_CONTINUE_BUTTON).isPresent();
    }

    @Override
    public String getB2BContinueButtonText() {
        return driverTools.getText(B2B_CONTINUE_BUTTON).orElseThrow();
    }

    @Override
    public void clickB2BContinueButton() {
        driverTools.click(B2B_CONTINUE_BUTTON);
    }


    @Override
    public void skipRegisterFaceId() {
        Optional<WebElement> e =driverTools.waitForElementToAppear(CLOSE_REGISTER_FACE_ID_FORM_BUTTON,5);
        if (e.isPresent()) {
            driverTools.click(e.get());
        }
        driverTools.waitForElementToAppear(ACTIVATE_FACE_ID_LATER_BUTTON,5).ifPresent(driverTools::click);
    }

    @Override
    public void openLoginPage() {
        //open login page, with assumption user must come from from home page.
        HomePage<T> homePage = HomePage.getInstance(this.session);
        homePage.openAccount();
        driverTools.click(LOGIN_BUTTON);

        if(isOnLoginPageWebview()){
            return;
        }

        boolean isOnLoginPage = this.isOnLoginPage();
        if (!isOnLoginPage) {
            throw new NoSuchElementException("Login page did not open");
        }
    }

    @Override
    public void openLoginPage(String username) {
        //open login page, with assumption user must come from from home page.
        HomePage<T> homePage = HomePage.getInstance(this.session);
        homePage.openAccount();
        driverTools.click(LOGIN_BUTTON);
    }

    @Override
    public boolean isCorporateLoginLinkVisible() {
        return driverTools.waitForElementToPresence(CORPORATE_LOGIN_LINK).isPresent();
    }

    @Override
    public void clickCorporateLoginLink() {
        driverTools.click(CORPORATE_LOGIN_LINK);
    }

    @Override
    public boolean openLoginPageWithDeeplink() {
        Deeplink loginPage = new Deeplink("login page",
                "tiket://gatotkaca.tiket.com/login",
                "tiket://preprod.tiket.com/login",
                "tiket://www.tiket.com/login");
        try {
            driverTools.switchToNative();
            driverTools.closeApp();
            driverTools.deeplink(loginPage);
        }catch (Exception e){
            logger.log("error openning deeplink : " + e.getLocalizedMessage());
        }
        return isOnLoginPageWebview();
    }
  
    @Override
    public void closeLoginPage() {
        driverTools.click(CLOSE_LOGIN_PAGE);
    }

    @Override
    public void loginFromBookingForm(String vertical) {
    } // FIXME: Implementation pending

    private void changePhoneCountry(){
        driverTools.click(COUNTRY_CODE);
        driverTools.click(INDONESIA_PHONE);
    }

    
    @Override
    public void loginFromOrderPage(String username,String password) {
        driverTools.click(LOG_IN_BTN_ORDER_PAGE);
        loginFromDifferentPages(username, password);
    }


    @Override
    public void loginFromDifferentPages(String username,String password) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("bliblitiket");
        super.proceedLoginFromDifferentPage("email", username, password);
        driverTools.switchToNative();
    }

    @Override
    public boolean isLoginFormDisplayedForDifferentPages() {
        closeUnmPageRedirection();
        if (driverTools.waitForElementToPresence(EMAIL_OR_PHONE_NUMBER_FIELD, 10).isPresent()
                || driverTools.waitForElementToPresence(PASSWORD_FIELD, 10).isPresent()) {
            return true;
        }
        return super.isLoginFormDisplayedForDifferentPages();
    }

    @Override
    public void loginFromDifferentPagesUsingPhoneNumber(String phoneNumber) {
        logger.log("Retry in webview unm page");
        super.proceedLoginFromDifferentPage("phonenumber",phoneNumber, "");
    }

    @Override
    protected boolean checkForSsoLogin(String username) {
        driverTools.switchToNative();
        HomePage<T> homePage = HomePage.getInstance(this.session);
        boolean isOnHomePage = homePage.isOnHomePage(10);
        if(isOnHomePage){
            boolean isSameUserAlreadyLogin = homePage.openAccountCenterViaDeeplink(username);
            logger.log("Autologin detected " + (isSameUserAlreadyLogin ? "from same user" : "from different user"));
            if(isSameUserAlreadyLogin){
                logger.log("User already login, skip login");
                homePage.goToHomeDeeplink();
                return true;
            }else{
                throw new ForbiddenException("Autologin from different user detected, stopping test");
            }
        }
        return false;
    }

    @Override
    public void openHomepage(){
    }//fixme pending implementation

    @Override
    public void loginWithSuspendedEmail(String email){
        inputEmailOrPhoneNumber(email);
    }

    @Override
    public void registerWithSuspendedEmail(String email) {
        inputEmailOrPhoneNumber(email);
    }

    @Override
    public boolean verifySuspendedAccountPopup(String[] expectedTexts){
        return verifySuspendedAccountPopupWebview(expectedTexts);
    }

    @Override
    public void registerWithNewPhoneNumberOrEmail(String PhoneNumber){
        registerWithNewPhoneNumberWebview(PhoneNumber);
    }

    @Override
    public void loginOnFacebookPage(String username, String password) {
         driverTools.waitForElementToAppear(INPUT_FACEBOOK_EMAIL);
        driverTools.sendKeys(INPUT_FACEBOOK_EMAIL, username);
        driverTools.sendKeys(INPUT_FACEBOOK_PASSWORD, password);
        driverTools.click(LOGIN_FACEBOOK);
        driverTools.click(NOTNOW_FACEBOOK);
    }

    @Override
    public void loginOnGooglePage(String username, String password) {
        driverTools.waitForElementToAppear(INPUT_GOOGLE_EMAIL);
        driverTools.sendKeys(INPUT_GOOGLE_EMAIL, username);
        driverTools.click(NEXT_GOOGLE_EMAIL);
        driverTools.waitForElementToAppear(INPUT_GOOGLE_PASSWORD);
        driverTools.sendKeys(INPUT_GOOGLE_PASSWORD, password);
        driverTools.click(NEXT_GOOGLE_PASSWORD);
    }

    @Override
    public boolean loginWithNative(String loginMethod,String username,String password,boolean isFromBookingPage) {
        super.proceedLoginFromDifferentPage(loginMethod, username, password);
        HomePage<T> homePage = HomePage.getInstance(this.session);
        homePage.coachMarkHandling();
        return true;
    }
}