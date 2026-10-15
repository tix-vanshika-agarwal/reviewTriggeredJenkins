package com.tiket.page.login;

import com.google.gson.Gson;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.member.GenerateOtpApi;
import com.tiket.api.member.PasswordResetApi;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.home.HomePage;
import com.tiket.pojo.member.GenerateOtpResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.common.home.LoginPageWeb;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.NoSuchContextException;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static com.tiket.page.login.LoginPageIOS.EMAIL_FIELD_NATIVE;
import static org.hamcrest.core.Is.is;

public abstract class LoginPage<T extends WebDriver> extends BasePage<T> {

    public LoginPage(Session<T> session) {
        super(session);
    }

    private static Locator IFRAME_GOOGLE = new Locator("IFRAME_GOOGLE", AppiumBy.xpath("//iframe[@title='Sign in with Google Button']"));
    private static Locator LOGIN_USING_FACEBOOK  = new Locator("LOGIN_USING_FACEBOOK", AppiumBy.xpath("//button[(@data-testid='sign-in-with-fb-btn')]"));
    private static Locator LOGIN_USING_GOOGLE  = new Locator("LOGIN_USING_GOOGLE", AppiumBy.xpath("//div[@role='button']"));
    private static Locator EMAIL_OR_PHONE_NUMBER_FIELD = new Locator("EMAIL_OR_PHONE_NUMBER_FIELD", AppiumBy.xpath("//div[contains(@class,'OneField')]//input"));
    public static final Locator LOADING_STATE = new Locator("LOADING_STATE", AppiumBy.xpath("//*[@data-testid='loading-state-blue']"));
    private static Locator LOGIN_BUTTON_FIRST_FORM = new Locator("LOGIN_BUTTON_FIRST_FORM", AppiumBy.xpath("//button[contains(@class,'cta_button')]"));
    private static Locator LOGIN_BUTTON_SECOND_FORM = new Locator("LOGIN_BUTTON_SECOND_FORM", AppiumBy.xpath("//button[contains(@class,'LoginPasswordForm')]"));
    private static Locator PASSWORD_FIELD = new Locator("PASSWORD_FIELD", AppiumBy.xpath("//div[contains(@class,'LoginPasswordForm')]//input[@name='password']"));
    private static Locator ENTER_PIN_HEADING = new Locator("ENTER_PIN_HEADING", AppiumBy.xpath(""));//TODO: add locator
    private static Locator REGISTER_BUTTON = new Locator("REGISTER_BUTTON", AppiumBy.xpath("//*[contains(@class,'LoginRegisterLayout')]//button[@data-testid='btnSubmitForm']"));
    private static Locator OTP_BTM_SHEET = new Locator("OTP_BTM_SHEET", AppiumBy.xpath("//*[contains(@class,'BottomSheet')]//*[contains(text(),'OTP')]"));
    public static Locator OTP_MODULE_BTM_SHEET_INFO_PHN_EMAIL = new Locator("OTP_MODULE_BTM_SHEET_INFO_PHN_EMAIL", AppiumBy.xpath("//*[contains(@class,'OtpModule_break_word')]//*[contains(text(),'*')]"));
    private static Locator PASSWORD_VIEW_BUTTON = new Locator("PASSWORD_VIEW_BUTTON", AppiumBy.xpath("//button[contains(@class,'styles_clickable_icon')]"));
    private static Locator PASSWORD_ALERT = new Locator("PASSWORD_ALERT", AppiumBy.xpath("//div[contains(@class,'danger')]"));
    public static final Locator LOADING = new Locator("LOADING", AppiumBy.xpath("//*[contains(@class,'LoadingIndicator_loading_indicator_container')]"));
    private static final Locator INTRO_BUTTON = new Locator("INTRO_BUTTON", AppiumBy.xpath("//*[contains(@class,'Introduction_sticky_action')]//button"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", AppiumBy.xpath("//*[contains(@class,'AccountForm')]//button"));
    private static final Locator FIRST_BUTTON_POPUP = new Locator("FIRST_BUTTON_POPUP", AppiumBy.xpath("//*[contains(@class,'Figure_secondary')]"));
    private static final Locator SECOND_BUTTON_POPUP = new Locator("SECOND_BUTTON_POPUP", AppiumBy.xpath("//*[contains(@class,'Figure_secondary')]//following::*[contains(@class,'Button_variant_primary')]"));
    private static final Locator POPUP_HEADER = new Locator("POPUP_HEADER", AppiumBy.xpath("//*[contains(@class,'Figure_title')]"));
    private static final Locator REGISTER_NOW_BOTTOM  = new Locator("REGISTER_NOW_BOTTOM", AppiumBy.xpath("//*[contains(@class,'LoginLayout_redirection_link')]"));
    private static final Locator FORGOT_PASSWORD = new Locator("FORGOT_PASSWORD", AppiumBy.xpath("//div[contains(@class,'LoginPasswordForm_forgot_password')]//span"));
    private static final Locator FORGOT_PASSWORD_EMAIL_PHONE_INPUT = new Locator("FORGOT_PASSWORD_EMAIL_PHONE_INPUT", By.xpath("//*[contains(@id,'phone-number-or-email')]"));
    private static final Locator FORGOT_PASSWORD_NEXT = new Locator("FORGOT_PASSWORD_NEXT", AppiumBy.xpath("//div[contains(@class,'ForgotPassword_footer')]//button"));
    private static final Locator FORGOT_PASSWORD_INPUT = new Locator("FORGOT_PASSWORD_INPUT", AppiumBy.xpath("//div[contains(@class,'ForgotPassword_content')]//input[@name='password']"));

    //Register page locators
    private static final Locator INPUT_FULLNAME = new Locator("INPUT_FULLNAME", AppiumBy.xpath("//input[contains(@name,'fullname')]"));
    private static final Locator INPUT_EMAIL = new Locator("INPUT_EMAIL", AppiumBy.xpath("//input[contains(@name,'email')]"));
    private static final Locator INPUT_PASSWORD = new Locator("INPUT_PASSWORD", AppiumBy.xpath("//input[contains(@name,'password')]"));
    private static final Locator INPUT_PHONE_NUMBER = new Locator("INPUT_PHONE_NUMBER", AppiumBy.xpath("//input[contains(@class, 'styles_input') and contains(@class, 'styles_with_prefix') and @type='tel' and contains(@id, 'phone-number') and contains(@placeholder, 'Phone number')]"));
    private static final Locator REGISTER_BUTTON_SUBMIT = new Locator("REGISTER_BUTTON_SUBMIT", AppiumBy.xpath("//*[contains(@data-testid,'btnSubmit')]"));
    private static final Locator OK_CONFIRMATION = new Locator("OK_CONFIRMATION", AppiumBy.xpath("//button[contains(@class, 'Button_button') and (contains(text(), 'OK') or contains(text(), 'Oke'))]"));
    private static final Locator LOGIN_POPUP = new Locator("LOGIN_POPUP", By.xpath("//button[contains(@class, 'BottomSheet_close_button')]"));

    //v2 login
    private static final Locator CONTINUE_BTN_LOGIN_WITH_V2 = new Locator("CONTINUE_BTN_LOGIN_WITH_V2", By.xpath("//*[contains(@text,'Continue with Phone or Email') or contains(@text,'Lanjut dengan nomor HP atau email')]"));
    private static final Locator CONTINUE_BTN_LOGIN_WITH_V2_WEBVIEW = new Locator("CONTINUE_BTN_LOGIN_WITH_V2_WEBVIEW", By.xpath("//*[contains(text(),'Continue with Phone or Email') or contains(text(),'Lanjut dengan nomor HP atau email')]"));
    private static final Locator CONTINUE_BTN_LOGIN_WITH_V2_NATIVE_IOS = new Locator("CONTINUE_BTN_LOGIN_WITH_V2_NATIVE_IOS", By.xpath("//XCUIElementTypeButton[(contains(@label,'Phone')and contains(@label,'Email')) or (contains(@label,'nomor HP') and contains(@label,'email'))]"));

    public static <T extends WebDriver> LoginPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new LoginPageAndroid<>(session);}
            case IOS -> {return new LoginPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    protected enum LoginMethod {
        email, phoneNumber, facebook, google, guest;

        public static LoginMethod parse(String loginMethod) {
            switch (loginMethod.toLowerCase()) {
                case "email" -> {return LoginMethod.email;}
                case "phonenumber" -> {return LoginMethod.phoneNumber;}
                default -> throw new NotImplementedException(String.format("Given login method is not supported: %s is not supported", loginMethod));
            }
        }
    }

    public void handleV2Login(){
        driverTools.waitForElementToAppear(LOGIN_USING_GOOGLE, 5);
        if(Configs.PLATFORM == Platform.IOS){
            if(driverTools.waitForElementToPresence(CONTINUE_BTN_LOGIN_WITH_V2_NATIVE_IOS).isPresent()){
                driverTools.clickElementByCoordinate(CONTINUE_BTN_LOGIN_WITH_V2_NATIVE_IOS);
                driverTools.waitForElementToPresence(EMAIL_FIELD_NATIVE,3).ifPresent(driverTools::clickElementByCoordinate);
            }
        }else {
            if(driverTools.waitForElementToPresence(CONTINUE_BTN_LOGIN_WITH_V2, 5).isPresent()){
                logger.log("Handling v2 login flow");
                driverTools.clickElementByCoordinate(CONTINUE_BTN_LOGIN_WITH_V2);
                driverTools.waitForElementToDisappear(CONTINUE_BTN_LOGIN_WITH_V2);
            }
        }
    }

    private void handleV2LoginWebview(){
        driverTools.waitForElementToAppear(LOGIN_USING_GOOGLE, 5);
        WebViewPage<?> w = WebViewPage.getInstance(session);
        w.switchToWebViewUrl("bliblitiket");
        if (driverTools.waitForElementToPresence(EMAIL_OR_PHONE_NUMBER_FIELD, 3).isPresent()) {
            logger.log("Already on onefield login form, skipping continue button");
            return;
        }
        if(driverTools.waitForElementToPresence(CONTINUE_BTN_LOGIN_WITH_V2_WEBVIEW, 5).isPresent()){
            logger.log("Handling v2 login flow");
            driverTools.clickElementByCoordinate(CONTINUE_BTN_LOGIN_WITH_V2_WEBVIEW);
            driverTools.waitForElementToDisappear(CONTINUE_BTN_LOGIN_WITH_V2_WEBVIEW);
        }
    }

    /**
     * Inputs the provided email or phone number into the login form and handles the login flow.
     *
     * @param username The email or phone number to be inputted into the login form.
     * @return A boolean indicating whether the login flow was successful.
     */
    public boolean inputEmailOrPhoneNumber(String username) {
        boolean loginFlowSuccess = false;
        handleV2LoginWebview();
        try {

            // Check if registration form is displayed
            if (driverTools.isElementDisplayed(REGISTER_BUTTON) && driverTools.getValueAttribute(EMAIL_OR_PHONE_NUMBER_FIELD, "value").equals("")) {
                driverTools.clickWithJavascriptExecutor(EMAIL_OR_PHONE_NUMBER_FIELD);
                driverTools.sendKeys(EMAIL_OR_PHONE_NUMBER_FIELD, username);
                retryClick(REGISTER_BUTTON);
                driverTools.waitForElementToDisappear(REGISTER_BUTTON);
            } else if (driverTools.waitForElementToAppear(EMAIL_OR_PHONE_NUMBER_FIELD).isPresent() && driverTools.getValueAttribute(EMAIL_OR_PHONE_NUMBER_FIELD, "value").equals("")) {
                skipPopupLoginPage();
                driverTools.sendKeys(EMAIL_OR_PHONE_NUMBER_FIELD, username);
            }
        } catch (NoSuchContextException | java.util.NoSuchElementException | org.openqa.selenium.NoSuchContextException e) {
            logger.log("Encountering appium error while inputting email and password: " + e.getMessage());
            logger.log("Automatically login without input anything");
            HomePage<T> homePage = HomePage.getInstance(this.session);
            return homePage.isUserLogin();
        }

        retryClick(LOGIN_BUTTON_FIRST_FORM);
        skipPopupLoginPage();
        driverTools.waitForElementToDisappear(LOADING_STATE);

        // Check for OTP requirement
        if (!username.contains("@") && driverTools.waitForElementToPresence(OTP_MODULE_BTM_SHEET_INFO_PHN_EMAIL,6).isPresent()) {
            logger.log("Inputting OTP for phone number login");
            inputOtp(1);
            return true;
        }
        return loginFlowSuccess;
    }

    private void retryClick(Locator locator) {
        try {
            driverTools.waitForElementToAppear(locator);
            driverTools.clickWithJavascriptExecutor(locator);
        } catch (StaleElementReferenceException e) {
            logger.log("Retrying click due to stale element: " + e.getMessage());
            driverTools.waitForElementToAppear(locator);
            driverTools.clickWithJavascriptExecutor(locator);
        }
    }

    public abstract boolean isChangeEmailButtonVisible();
    public abstract String getChangeEmailButtonText();
    public abstract boolean isB2BContinueButtonVisible();
    public abstract String getB2BContinueButtonText();
    public abstract void clickB2BContinueButton();
    public abstract void skipRegisterFaceId();
    public abstract void openLoginPage();
    public abstract void openLoginPage(String username);
    public abstract boolean isCorporateLoginLinkVisible();
    public abstract void clickCorporateLoginLink();
    //return true if successfully open login page else false
    public abstract boolean openLoginPageWithDeeplink();
    public abstract void inputEmailOrPhoneNumberNative(String username);
    public abstract void closeUnmPageRedirection();
    public boolean isOnLoginPage(){
        closeUnmPageRedirection();
        driverTools.waitForWebview();
        String targetUrl = "bliblitiket";
        driverTools.switchToWebViewUrl(targetUrl);
        boolean isElementDisplayed = driverTools.waitForElementToAppear(REGISTER_BUTTON,5).isPresent() || driverTools.waitForElementToAppear(LOGIN_BUTTON_FIRST_FORM,5).isPresent() || driverTools.waitForElementToAppear(CONTINUE_BTN_LOGIN_WITH_V2,5).isPresent() || driverTools.waitForElementToAppear(CONTINUE_BTN_LOGIN_WITH_V2_WEBVIEW,5).isPresent();
        return isElementDisplayed;
    }

    public boolean isRedirectLoginPage(){
        boolean isElementDisplayed;
        driverTools.switchToWebViewUrl("bliblitiket");
        isElementDisplayed = driverTools.waitForElementToAppear(REGISTER_BUTTON,5).isPresent() || driverTools.waitForElementToAppear(LOGIN_BUTTON_FIRST_FORM,5).isPresent() || driverTools.waitForElementToAppear(CONTINUE_BTN_LOGIN_WITH_V2,5).isPresent() || driverTools.waitForElementToAppear(CONTINUE_BTN_LOGIN_WITH_V2_WEBVIEW,5).isPresent();
        return isElementDisplayed;
    }

    /**
     * True when login fields shown after redirect (e.g. wishlist) — same inputs as {@link #loginFromDifferentPages}.
     */
    public boolean isLoginFormDisplayedForDifferentPages() {
        closeUnmPageRedirection();
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("bliblitiket");
        return isLoginWebViewFormFieldsDisplayed();
    }

    protected boolean isLoginWebViewFormFieldsDisplayed() {
        return driverTools.waitForElementToPresence(EMAIL_OR_PHONE_NUMBER_FIELD, 15).isPresent()
                || driverTools.waitForElementToPresence(REGISTER_BUTTON, 10).isPresent()
                || driverTools.waitForElementToPresence(LOGIN_BUTTON_FIRST_FORM, 10).isPresent()
                || driverTools.waitForElementToPresence(PASSWORD_FIELD, 10).isPresent()
                || driverTools.waitForElementToPresence(CONTINUE_BTN_LOGIN_WITH_V2_WEBVIEW, 10).isPresent();
    }

    public abstract void closeLoginPage(); // FIXME: Implementation pending
    public abstract boolean loginWithNative(String loginMethod,String username,String password,boolean isFromBookingPage);
    public abstract void loginWith(String loginMethod,String username,String password);

    public boolean loginWithWebview(String loginMethod,String username,String password){
        // input credential based on available method.
        // can only be used if user in homepage
        WebViewPage<T> w = WebViewPage.getInstance(session);
        w.switchToWebViewUrl("bliblitiket");
        logger.log("Unm logging in with "+ loginMethod);
        boolean isLoginSuccess = proceedLogin(loginMethod, username, password);
        w.switchToNative();
        logger.log("Is login success: "+isLoginSuccess);
        return isLoginSuccess;
    }

    public boolean loginWithB2BRetry(String loginMethod,String username,String password){
        // input credential based on available method.
        // can only be used if user in homepage
        logger.log("Unm logging in with "+ loginMethod);
        return inputCredentialEmailB2BWithOutAssertion(username, password);
    }

    public void loginWith(String loginMethod, String username, String password, boolean isOpenWebView) {
        logger.log("Unm logging in with "+ loginMethod);
        proceedLogin(loginMethod, username, password);
    }

    public void loginUNM(String loginMethod,String username,String password){
        // input credential based on available method.
        // can only be used if user in homepage
        inputEmailOrPhoneNumber(username);
        if (Configs.ENVIRONMENT == Environment.PREPROD || Configs.ENVIRONMENT == Environment.PROD)
            skipPopupLoginPage();
        driverTools.click(PASSWORD_FIELD);
        driverTools.sendKeys(PASSWORD_FIELD, password);
        driverTools.clickWithJavascriptExecutor(LOGIN_BUTTON_SECOND_FORM);

        if(driverTools.waitForWebToLoad() && driverTools.waitForElementToDisappear(LOADING,15) && driverTools.waitForElementToPresenceSafe(getOtpLocator(1), 5).isPresent()){
            this.inputOtp(1);
        }

        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        loginPageWeb.skipPassKey();
    }

    protected void proceedLoginFromDifferentPage(String loginMethod,String username, String password) {
        switch(LoginMethod.parse(loginMethod)) {
            case email: this.inputCredentialEmailWithoutAssertion(username, password); break;
            case phoneNumber: this.inputCredentialPhoneWithoutAssertion(username); break;
            default : throw new UnsupportedOperationException();
        }
    }

    protected void inputCredentialPhoneWithoutAssertion(String phoneNumber){
        inputEmailOrPhoneNumber(phoneNumber);
        this.inputOtp(1);
    }

    protected void inputCredentialEmailWithoutAssertion(String email,String password) {
        inputEmailOrPhoneNumber(email);
        skipPopupLoginPage();
        driverTools.click(PASSWORD_FIELD);
        driverTools.sendKeys(PASSWORD_FIELD, password);
        driverTools.clickWithJavascriptExecutor(LOGIN_BUTTON_SECOND_FORM);

        this.inputOtp(1);
    }

    protected boolean proceedLogin(String loginMethod, String username, String password) {
        boolean loginSuccess=false;
        switch(LoginMethod.parse(loginMethod)) {
            case email: loginSuccess = this.inputCredentialEmail(username, password); break;
            case phoneNumber: loginSuccess = this.inputCredentialPhone(username); break;
            default : throw new UnsupportedOperationException();
        }
        return loginSuccess;
    }

    protected abstract boolean checkForSsoLogin(String username);

    /**
     * Attempts to input the provided email and password to log in.
     *
     * @param email The email address to be used for login.
     * @param password The password to be used for login.
     * @return true if login is successful, false otherwise.
     * @throws NoSuchContextException if there is an issue with the appium context.
     * @throws java.util.NoSuchElementException if an element is not found during the process.
     * @throws org.openqa.selenium.NoSuchContextException if there is an issue with the selenium context.
     */
    protected boolean inputCredentialEmail(String email, String password) {
        boolean isLoginSuccess;
        try {
            HomePage<T> homePage = HomePage.getInstance(this.session);
            WebViewPage<T> webViewPage = WebViewPage.getInstance(session);
            if (inputEmailOrPhoneNumber(email)) {
                isLoginSuccess = homePage.isUserLogin();
                if (isLoginSuccess) return true;
            } else {
                skipPopupLoginPage();
                driverTools.waitForElementToPresence(PASSWORD_VIEW_BUTTON);
                driverTools.click(PASSWORD_VIEW_BUTTON);
                if (driverTools.isElementDisplayed(PASSWORD_VIEW_BUTTON)) driverTools.clickWithJavascriptExecutor(PASSWORD_VIEW_BUTTON);
                driverTools.waitForElementToAppear(PASSWORD_FIELD);
                driverTools.click(PASSWORD_FIELD);
                driverTools.sendKeys(PASSWORD_FIELD, password);
                driverTools.hideKeyboard();
                driverTools.waitForElementToAppear(LOGIN_BUTTON_SECOND_FORM);
                driverTools.clickWithJavascriptExecutor(LOGIN_BUTTON_SECOND_FORM);
                driverTools.waitForElementToPresence(LOADING_STATE, 2);
                driverTools.waitForElementToAbsence(LOADING_STATE, 10);
            }

            if (!webViewPage.waitForWebview(5)) {
                logger.log("Automatically login without input OTP and without passkey popup notification");
                webViewPage.switchToNative();
                isLoginSuccess = homePage.isUserLogin();
                return isLoginSuccess;
            } else {
                if (driverTools.waitForWebToLoad() && driverTools.waitForElementToDisappear(LOADING, 15) && driverTools.waitForElementToPresenceSafe(getOtpLocator(1), 5).isPresent()) {
                    this.inputOtp(1);
                    driverTools.waitForElementToPresence(LOADING_STATE, 2);
                    driverTools.waitForElementToAbsence(LOADING_STATE, 10);
                }

                if (!webViewPage.waitForWebview(5)) {
                    logger.log("Automatically login without input OTP");
                    webViewPage.switchToNative();
                    isLoginSuccess = homePage.isUserLogin();
                    return isLoginSuccess;
                }

                WebViewPage<T> w = WebViewPage.getInstance(session);
                w.switchToWebViewUrl("bliblitiket");
                LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
                loginPageWeb.skipPassKey();
                driverTools.waitForElementToPresence(LOADING_STATE, 2);
                driverTools.waitForElementToAbsence(LOADING_STATE, 10);
                driverTools.dummyWait(5);
                homePage.closePromoPopup();
                if (!webViewPage.waitForWebview(5)) {
                    logger.log("Automatically login without input OTP");
                    webViewPage.switchToNative();
                    isLoginSuccess = homePage.isUserLogin();
                    return isLoginSuccess;
                }

                homePage.isOnHomePage(10); // wait for homepage to load after login
                homePage.goToHomeUsingDeeplinkWithoutCondition();
                isLoginSuccess = homePage.isUserLogin();
            }

        } catch (WebDriverException e) {
            logger.log("Encountering appium error while inputting email and password: " + e.getMessage());
            isLoginSuccess = checkForSsoLogin(email);
        }
        return isLoginSuccess;
    }

    protected boolean inputCredentialEmailB2BWithOutAssertion(String email,String password) {
        boolean isLoginSuccess = false;
        try{
            HomePage<T> homePage = HomePage.getInstance(this.session);
            WebViewPage<T> webViewPage = WebViewPage.getInstance(session);
            if(inputEmailOrPhoneNumber(email)) {
                isLoginSuccess = homePage.isUserLogin();
                if (isLoginSuccess) return isLoginSuccess;
            } else {
                driverTools.waitForElementToAppear(LOGIN_BUTTON_FIRST_FORM, 10).ifPresent(driverTools::click);
                driverTools.waitForElementToAppear(LOGIN_BUTTON_FIRST_FORM, 10).ifPresent(driverTools::click);
                if (Configs.ENVIRONMENT == Environment.PREPROD || Configs.ENVIRONMENT == Environment.PROD) {
                    skipPopupLoginPage();
                }
                driverTools.waitForElementToAppear(PASSWORD_VIEW_BUTTON);
                driverTools.click(PASSWORD_VIEW_BUTTON);
                driverTools.waitForElementToAppear(PASSWORD_FIELD);
                driverTools.click(PASSWORD_FIELD);
                driverTools.sendKeys(PASSWORD_FIELD, password);
                driverTools.hideKeyboard();
                driverTools.waitForElementToAppear(LOGIN_BUTTON_SECOND_FORM);
                driverTools.clickWithJavascriptExecutor(LOGIN_BUTTON_SECOND_FORM);
            }

            if (webViewPage.waitForWebview(10)) {
                if (driverTools.waitForWebToLoad() && driverTools.waitForElementToDisappear(LOADING, 15) && driverTools.waitForElementToPresenceSafe(getOtpLocator(1), 5).isPresent()) {
                    this.inputOtp(1);
                }
                homePage.isOnHomePage(10); // wait for homepage to load after login
                homePage.goToHomeDeeplink();
            }
            isLoginSuccess = homePage.isUserLogin();

        }catch(NoSuchContextException | java.util.NoSuchElementException | org.openqa.selenium.NoSuchContextException e){
            //Condition for unm is that if user already loggedin, it will redirect to homepage.
            //Sometimes the user is actually still logged in in the mweb so the following step won't be neccessary (input password, input otp)
            logger.log("Encountering appium error while inputting email and password: "+e.getMessage());
            isLoginSuccess = checkForSsoLogin(email);
        }
        return isLoginSuccess;
    }

    private Locator inputPinLocator(int index) {
        return new Locator("PIN_FIELD_"+index,AppiumBy.xpath("//*[contains(@resource-id, 'et_pin_"+index+"')]"));
    }

    protected boolean inputCredentialPhone(String phoneNumber){
        boolean isLoginSuccess = false;
        try{
            inputEmailOrPhoneNumber(phoneNumber);
            this.inputOtp(1);

            driverTools.switchToNative();
            HomePage<T> homePage = HomePage.getInstance(this.session);
            homePage.isOnHomePage(10);
            homePage.goToHomeUsingDeeplinkWithoutCondition();
            isLoginSuccess = homePage.isUserLogin();
        }catch(NoSuchContextException e){
            //Condition for unm is that if user already loggedin, it will redirect to homepage.
            //Sometimes the user is actually still logged in in the mweb so the following step won't be neccessary (input password, input otp)
            logger.log("Encountering appium error while inputting email and password: "+e.getMessage());
            isLoginSuccess = checkForSsoLogin(phoneNumber);
        }
        return isLoginSuccess;
    }

    public boolean loginWithPin(String phone,String pin){
        HomePage<T> homePage = HomePage.getInstance(this.session);
        WebViewPage<T> webViewPage = WebViewPage.getInstance(session);
        if (!webViewPage.waitForWebview(2)) {
            logger.log("Logging in with phone number: " + phone);
            openLoginPage();
        } else if (isOnLoginPage()) {
            logger.log("Already in login page, proceed logging in with phone number: " + phone);
        }else{
            throw new NotImplementedException("not in login page or home screen");
        }
        boolean isLoggedInUsingPin = inputCredentialPin(phone, pin);
        logger.log("Is logged in using pin: "+isLoggedInUsingPin);
        homePage.waitForHomePageToLoad();
        return isLoggedInUsingPin;
    }

    @Deprecated //login with pin feature is not present anymore
    private boolean inputCredentialPin(String phoneNumber, String pin){
        inputEmailOrPhoneNumber(phoneNumber);
        boolean loggedInWithPin = false;
        if(driverTools.waitForElementToAppear(ENTER_PIN_HEADING).isPresent()){
            loggedInWithPin = true;
            String[] pinArray = pin.split("");
            for (int i=1;i<7;i++) {
                Locator pinField = inputPinLocator(i);
                WebElement pin_field_element = driverTools.waitForElementToAppear(pinField,20).orElseThrow();
                driverTools.click(pin_field_element);
                driverTools.sendKeys(pin_field_element,pinArray[i-1]);
            }
            driverTools.waitForElementToDisappear(ENTER_PIN_HEADING);
        }else{
            this.inputOtp(1);
        }
        this.skipRegisterFaceId();
        return loggedInWithPin;
    }

    public void loginWithUnverifiedPhoneNumber(String loginMethod,String username,String password){
        if (openLoginPageWithDeeplink()) {
            logger.log("Logging in with "+ loginMethod);
            //inputCredentialEmailNobPNV(username,password);
        } else {
            logger.log("Fail opening the login page");
            //throw exception so that its obvious that login phase was failing
            Assertion.assertThat("Is login page open", false, is(true));
        }
    }

    public boolean checkLoginWithUnverifiedPhoneNumber(String loginMethod,String username,String password){
        throw new NotImplementedException();
    }

    public abstract void loginAsGuest();

    /**
     * Ensures the app is logged in as the provided account, combining state detection,
     * same-account verification and a logout-then-login fallback in a single entry point.
     *
     * <p>Behavior:
     * <ul>
     *   <li>If the user is currently logged out → calls {@link #loginWith(String, String, String)}.</li>
     *   <li>If the user is currently logged in:
     *     <ul>
     *       <li>Opens Account Center via deeplink and reads the associated email / phone via
     *           {@link HomePage#openAccountCenterViaDeeplink(String)}.</li>
     *       <li>If it matches {@code username} → returns to home page via
     *           {@link HomePage#goToHomeDeeplink()} and does nothing else.</li>
     *       <li>Otherwise → logs out via {@link #loginAsGuest()} and then logs in with the
     *           provided credentials via {@link #loginWith(String, String, String)}.</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * @param loginMethod login method, e.g. {@code "email"} or {@code "phoneNumber"} (see {@link LoginMethod})
     * @param username    email or phone number to authenticate as
     * @param password    password (ignored / can be empty for phone-number / OTP flows)
     */
    public void ensureLoggedInAs(String loginMethod, String username, String password) {
        HomePage<T> homePage = HomePage.getInstance(this.session);

        if (!homePage.isUserLogin()) {
            logger.log("App state: LOGGED OUT — proceeding to login with " + loginMethod);
            loginWith(loginMethod, username, password);
            return;
        }

        logger.log("App state: LOGGED IN — verifying account via Account Center deeplink");
        boolean isSameAccount = homePage.openAccountCenterViaDeeplink(username);
        homePage.goToHomeDeeplink();

        if (isSameAccount) {
            logger.log("Already logged in as " + username + ", returning to home page");
            return;
        }

        logger.log("Logged in as a different account, logging out then re-logging in as " + username);
        loginAsGuest();
        loginWith(loginMethod, username, password);
    }
    public abstract void loginFromBookingForm(String vertical); // FIXME: Implementation pending
    public abstract void loginFromOrderPage(String username,String password);
    public abstract void loginFromDifferentPages(String username,String password);
    public abstract void loginFromDifferentPagesUsingPhoneNumber(String phoneNumber);
    public Locator getOtpLocator(int index){
        return new Locator("OTP_FIELD_"+index, AppiumBy.xpath("//div[contains(@class,'OtpInput_otp_group')]//input["+index+"]"));
    }

    public void inputOtp(int retryCount){
        logger.log("inputting otp");
        for (int i = 1; i < 7; i++) {
            Locator otpField = getOtpLocator(i);
            Optional<WebElement> otp_field_element = driverTools.waitForElementToPresence(otpField, 5);
            if (!otp_field_element.isPresent() && i == 1) break;
            logger.log("Retrying OTP one more time");
            if (otp_field_element.isPresent()) otp_field_element = driverTools.waitForElementToPresence(otpField, 5);
            driverTools.click(otp_field_element.get());
            driverTools.sendKeys(otp_field_element.get(), "" + i + "");
        }
        if (!driverTools.waitForElementToDisappear(OTP_BTM_SHEET, 5) && retryCount > 0) {
            inputOtp(retryCount - 1);
        }
        driverTools.waitForElementToDisappear(getOtpLocator(1));
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        loginPageWeb.skipPassKey();
    }

    public void inputOtpForgotPassword(int retryCount){
        logger.log("inputting otp");
        for (int i = 1; i < 7; i++) {
            Locator otpField = getOtpLocator(i);
            Optional<WebElement> otp_field_element = driverTools.waitForElementToPresence(otpField, 5);
            if (!otp_field_element.isPresent() && i == 1) break;
            //retry one more time
            if (otp_field_element.isPresent()) otp_field_element = driverTools.waitForElementToPresence(otpField, 5);
            driverTools.click(otp_field_element.get());
            driverTools.sendKeys(otp_field_element.get(), "" + i + "");
        }
        if (!driverTools.waitForElementToDisappear(OTP_BTM_SHEET, 5) && retryCount > 0) {
            inputOtp(retryCount - 1);
        }
        driverTools.waitForElementToDisappear(getOtpLocator(1));
    }

    public void inputOtp(String targetUrl){
        logger.log("inputting otp");
        for (int i = 1; i < 7; i++) {
            Locator otpField = getOtpLocator(i);
            Optional<WebElement> otp_field_element = driverTools.waitForElementToPresence(otpField, 5);
            if (!otp_field_element.isPresent() && i == 1) break;
            //retry one more time
            if (otp_field_element.isPresent()) otp_field_element = driverTools.waitForElementToPresence(otpField, 5);
            driverTools.click(otp_field_element.get());
            driverTools.sendKeys(otp_field_element.get(), "" + i + "");
        }
        driverTools.waitForElementToDisappear(getOtpLocator(1));
    }


    public void resetPasswordViaApi(String username,String newPassword) throws InterruptedException, ExecutionException{
        Gson gson = new Gson();
        //generate access token
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String token = dataObject.getString("accessToken");

        //generate reset password otp
        GenerateOtpApi.Inputs generateOtpParameter = new GenerateOtpApi.Inputs("FORGOT_PASSWORD",token,username);
        GenerateOtpApi generateOtpApi = new GenerateOtpApi(generateOtpParameter);
        ApiResponse generateOtpApiResponse = generateOtpApi.hitApi(apiTools);
        GenerateOtpResponse generateOtpResponse = gson.fromJson(generateOtpApiResponse.getBody(), GenerateOtpResponse.class);

        //reset password
        PasswordResetApi.Inputs passwordResetParameter = new PasswordResetApi.Inputs(generateOtpResponse.data.trxId, token, newPassword);
        PasswordResetApi passwordResetApi = new PasswordResetApi(passwordResetParameter);
        ApiResponse responsePasswordResetApi = passwordResetApi.hitApi(apiTools);
        logger.log("complete api respons : \n" + responsePasswordResetApi.getBody());
        Assertion.assertThat("Reset password susccess", responsePasswordResetApi.getStatusCode(), is(200));
    }

    public String forgetPassword(String username, String password) {
        String updatedPassword = password;
        inputEmailOrPhoneNumber(username);
        clickForgotPasswordLoginPage();
        clickNextOnForgotPasswordPage();
        this.inputOtp(1);
        changePasswordOnForgotPasswordPage(updatedPassword);
        return updatedPassword;
    }

    public String forgetPasswordPhoneNumber(String email,String phoneNo,String updatedPassword) {
        WebViewPage<?> w = WebViewPage.getInstance(session);
        w.switchToWebViewUrl("bliblitiket");
        logger.log("===input email or phone number: " + email + "===");
        inputEmailOrPhoneNumber(email);
        logger.log("===Clicking forgot password===");
        clickForgotPasswordLoginPage();
        logger.log("=== Changing email to phone number on forgot password page: " + phoneNo + "===");
        changeEmailToPhoneNumberOnForgotPasswordPage(phoneNo);
        logger.log("===Clicking next on forgot password page===");
        clickNextOnForgotPasswordPage();
        logger.log("===Inputting OTP on forgot password page===");
        this.inputOtp(1);
        logger.log("==Changing password on forgot password page to: " + updatedPassword + "===");
        changePasswordOnForgotPasswordPage(updatedPassword);
        logger.log("===Forgot password process completed, returning updated password===");
        w.switchToNative();
        return updatedPassword;
    }

    private void clickForgotPasswordLoginPage(){
        driverTools.waitForElementToAppear(LOGIN_BUTTON_FIRST_FORM).ifPresent(driverTools::click);
        driverTools.click(FORGOT_PASSWORD);
    }

    private void clickNextOnForgotPasswordPage(){
        driverTools.waitForElementToAppear(FORGOT_PASSWORD_NEXT).ifPresent(driverTools::click);
    }

    private void changeEmailToPhoneNumberOnForgotPasswordPage(String phoneNo){
        driverTools.waitForElementToAppear(FORGOT_PASSWORD_EMAIL_PHONE_INPUT);
        driverTools.clearWithJsExecutor(FORGOT_PASSWORD_EMAIL_PHONE_INPUT);
        driverTools.sendKeys(FORGOT_PASSWORD_EMAIL_PHONE_INPUT,"");
        driverTools.clearWithJsExecutor(FORGOT_PASSWORD_EMAIL_PHONE_INPUT);
        //check if input is empty
        if (driverTools.getValueAttribute(FORGOT_PASSWORD_EMAIL_PHONE_INPUT, "value").isEmpty()) {
            logger.log("Input field is empty, proceeding to send phone number");
        } else {
            logger.log("Input field is not empty, clearing it before sending phone number");
            driverTools.clearWithJsExecutor(FORGOT_PASSWORD_EMAIL_PHONE_INPUT);
        }
        driverTools.sendKeys(FORGOT_PASSWORD_EMAIL_PHONE_INPUT,phoneNo);
    }

    private void changePasswordOnForgotPasswordPage(String updatedPassword){
        driverTools.waitForElementToAppear(FORGOT_PASSWORD_INPUT).ifPresent(driverTools::click);
        driverTools.sendKeys(FORGOT_PASSWORD_INPUT,updatedPassword);
        driverTools.click(FORGOT_PASSWORD_NEXT);
        Boolean isOnLoginPage = driverTools.isElementDisplayed(PASSWORD_VIEW_BUTTON);
        Assertion.assertThat("Fogot password Successful", isOnLoginPage, Matchers.is(true));
    }

    public abstract void openHomepage();
    public abstract void loginWithSuspendedEmail(String email);
    public abstract void registerWithSuspendedEmail(String email);
    public abstract boolean verifySuspendedAccountPopup(String[] expectedTexts);

    public boolean verifySuspendedAccountPopupWebview(String[] expectedTexts){
        String headerText = driverTools.getText(POPUP_HEADER).orElseThrow();
        Assertion.assertThat("Header text as expected to language",headerText,is(expectedTexts[0]));

        String firstButtonText = driverTools.getText(FIRST_BUTTON_POPUP).orElseThrow();
        Assertion.assertThat("First button text as expected to language",firstButtonText,is(expectedTexts[1]));

        String secondButtonText = driverTools.getText(SECOND_BUTTON_POPUP).orElseThrow();
        Assertion.assertThat("Second button text as expected to language",secondButtonText,is(expectedTexts[2]));
        return true;
    }

    public void loginWithSuspendedEmailWebview(String email){
        handleV2Login();
        if(driverTools.isElementDisplayed(REGISTER_BUTTON)){
            driverTools.clickWithJavascriptExecutor(EMAIL_OR_PHONE_NUMBER_FIELD);
            driverTools.sendKeys(EMAIL_OR_PHONE_NUMBER_FIELD, email);
            driverTools.clickWithJavascriptExecutor(REGISTER_BUTTON);
        }
        driverTools.waitForElementToAppear(LOGIN_BUTTON_FIRST_FORM,5).orElseThrow();
        driverTools.clickWithJavascriptExecutor(LOGIN_BUTTON_FIRST_FORM);
    }

    public abstract void registerWithNewPhoneNumberOrEmail(String email);

    public void registerWithNewPhoneNumberWebview(String PhoneNumberOrEmail){
        handleV2Login();

        WebViewPage<T> w = WebViewPage.getInstance(session);
        w.switchToWebViewUrl("bliblitiket");
        logger.log("Registering with new phone number or email: " + PhoneNumberOrEmail);
        driverTools.clickWithJavascriptExecutor(EMAIL_OR_PHONE_NUMBER_FIELD);
        driverTools.sendKeys(EMAIL_OR_PHONE_NUMBER_FIELD, PhoneNumberOrEmail);
        retryClick(LOGIN_BUTTON_FIRST_FORM);
        driverTools.waitForElementToDisappear(LOGIN_BUTTON_FIRST_FORM);
        w.switchToNative();
    }

    public void inputDetailsRegisterPage(String fullname, String email, String password) {
        WebViewPage<T> w = WebViewPage.getInstance(session);
        w.switchToWebViewUrl("bliblitiket");

        logger.log("fullname=="+ fullname + ", email ==" + email + ", password ==" + password);
        driverTools.sendKeys(INPUT_FULLNAME, fullname);
        driverTools.sendKeys(INPUT_EMAIL, email);
        driverTools.sendKeys(INPUT_PASSWORD, password);
        driverTools.clickWithJavascriptExecutor(REGISTER_BUTTON_SUBMIT);
        if (driverTools.waitForElementToPresence(getOtpLocator(1)).isPresent()) {
            inputOtp(1);
        }
        try {
            LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
            loginPageWeb.skipPassKey();
            if(driverTools.waitForElementToPresence(OK_CONFIRMATION).isPresent()){
                driverTools.clickWithJavascriptExecutor(OK_CONFIRMATION);
                driverTools.waitForElementToDisappear(OK_CONFIRMATION);
            }
            loginPageWeb.skipPassKey();
            driverTools.waitForElementToPresence(LOADING_STATE,3);
            driverTools.waitForElementToDisappear(LOADING_STATE);
        }catch (Exception e) {
            logger.log("Exception while switching to native: " + e.getMessage());
        }
        w.switchToNative();
    }

    public void inputDetailsRegisterPageEmail(String fullname, String phoneNumber, String password){
        logger.log("fullname=="+ fullname + "email ==" + phoneNumber + "password ==" + password);
        driverTools.sendKeys(INPUT_FULLNAME, fullname);
        driverTools.sendKeys(INPUT_PHONE_NUMBER, phoneNumber);
        driverTools.sendKeys(INPUT_PASSWORD, password);
        driverTools.clickWithJavascriptExecutor(REGISTER_BUTTON_SUBMIT);
        if (driverTools.waitForElementToPresence(getOtpLocator(1)).isPresent()) {
            inputOtp(1);
        }
        driverTools.waitForElementToAppear(OK_CONFIRMATION, 5).ifPresent(driverTools::click);
    }

    public void loginAfterForgotPasswordWithEmail(String username, String password) {
        WebViewPage<?> w = WebViewPage.getInstance(session);
        w.switchToWebViewUrl("bliblitiket");
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        driverTools.click(PASSWORD_FIELD);
        driverTools.sendKeys(PASSWORD_FIELD, password);
        driverTools.clickWithJavascriptExecutor(LOGIN_BUTTON_SECOND_FORM);
        loginPageWeb.skipPassKey();

        WebViewPage<T> webViewPage = WebViewPage.getInstance(session);
        if(webViewPage.waitForWebview(2)){
            this.inputOtp(1);
        }
        w.switchToNative();
    }

    public void loginUsingFacebook() {
        if(Configs.ENVIRONMENT == Environment.PROD){
            driverTools.waitForElementToPresence(LOGIN_USING_FACEBOOK,30);
        }else {
            driverTools.waitForElementToPresence(LOGIN_USING_FACEBOOK);
        }
        driverTools.click(LOGIN_USING_FACEBOOK);
    }

    public void loginUsingGoogle() {
        driverTools.switchToIframe(driverTools.element(IFRAME_GOOGLE).orElseThrow());
        driverTools.waitForElementToPresence(LOGIN_USING_GOOGLE, 10);
        driverTools.clickWithJavascriptExecutor(LOGIN_USING_GOOGLE);
        driverTools.switchBackFromIframe();
    }

    public abstract void loginOnFacebookPage(String username, String password);
    public abstract void loginOnGooglePage(String username, String password);

    public void skipPopupLoginPage() {
        if (driverTools.waitForElementToPresence(LOGIN_POPUP, 5).isPresent() && driverTools.waitForElementToPresence(OTP_MODULE_BTM_SHEET_INFO_PHN_EMAIL).isEmpty()) {
            driverTools.click(LOGIN_POPUP);
        }
    }
}