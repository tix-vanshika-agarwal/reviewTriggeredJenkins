package com.tiket.page.login;

import com.tiket.appium.Swipe;
import com.tiket.capabilities.CapabilitiesHandler;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Exception.ForbiddenException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.account.AccountPage;
import com.tiket.page.account.AccountPageIOS;
import com.tiket.page.home.HomePage;
import com.tiket.page.home.HomePageIOS;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.hamcrest.Matchers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;

import java.util.HashMap;
import java.util.Map;

import static com.tiket.page.home.HomePageIOS.PROMO_ICON;
import static org.hamcrest.core.Is.is;

public class LoginPageIOS <T extends WebDriver> extends LoginPage<T> {

    private static Locator INPUT_FACEBOOK_EMAIL = new Locator("INPUT_FACEBOOK_EMAIL", AppiumBy.xpath("//*[@type=\"XCUIElementTypeTextField\"]"));
    private static Locator INPUT_FACEBOOK_PASSWORD = new Locator("INPUT_FACEBOOK_PASSWORD", AppiumBy.xpath("//*[@type=\"XCUIElementTypeSecureTextField\"]"));
    private static Locator  LOGIN_FACEBOOK= new Locator("LOGIN_FACEBOOK", AppiumBy.xpath("//*[@name=\"Log in\"]"));
    private static final Locator CREATE_ACCOUNT_BUTTON = new Locator("CREATE_ACCOUNT_BUTTON", AppiumBy.accessibilityId("create_account"));
    private static final Locator EMAIL_OR_PHONE_NUMBER_FIELD = new Locator("EMAIL_OR_PHONE_NUMBER_FIELD", AppiumBy.accessibilityId("username_field"));
    private static final Locator CLOSE_REGISTER_FACE_ID_FORM_BUTTON = new Locator("CLOSE_REGISTER_FACE_ID_FORM_BUTTON", AppiumBy.xpath("//*[contains(@label,'Face ID')]//preceding::*[contains(@name,'grey')]"));
    private static final Locator ACTIVATE_FACE_ID_LATER_BUTTON = new Locator("ACTIVATE_FACE_ID_LATER_BUTTON", AppiumBy.accessibilityId("confirm_activate_later"));
    public static final Locator CLOSE_SIGN_GOOGLE = new Locator("CLOSE_SIGN_GOOGLE", AppiumBy.xpath("//*[contains(@name,'Google')]//*[@name='Close']"));
    private static final Locator CONTINUE_BUTTON_UNM_REDIRECTION = new Locator("CONTINUE_BUTTON_UNM_REDIRECTION", AppiumBy.xpath("//XCUIElementTypeAlert//*[@label='Continue']"));
    private static final Locator CLOSE_LOGIN_PAGE = new Locator("CLOSE_LOGIN_PAGE",AppiumBy.accessibilityId("tds ic cross big"));
    private static final Locator LOG_IN_BTN_ORDER_PAGE= new Locator("LOG_IN_BTN_ORDER_PAGE",AppiumBy.accessibilityId("banner_container"));
    public static final Locator LOGIN_BUTTON= new Locator("LOGIN_BUTTON",AppiumBy.xpath("//*[@name='login_button']"));
    private static final Locator CORPORATE_LOGIN_LINK = new Locator("CORPORATE_LOGIN_LINK", AppiumBy.iOSClassChain("**/*[`label CONTAINS 'tiket.com for Corporates'`]"));
    private static final Locator CHANGE_EMAIL_BUTTON = new Locator("CHANGE_EMAIL_BUTTON", AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label == 'No, change email' OR label == 'Tidak, ganti email'`]"));
    private static final Locator B2B_CONTINUE_BUTTON = new Locator("B2B_CONTINUE_BUTTON", AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label == 'Yes, continue' OR label == 'Ya, lanjutkan'`]"));
    public static final Locator OTP_BTM_SHEET_TITLE_NATIVE = new Locator("OTP_BTM_SHEET_TITLE_NATIVE", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Masukkan kode OTP' or contains(@label,'Enter OTP Code')]"));
    private static final Locator FIRST_BUTTON_POPUP = new Locator("FIRST_BUTTON_POPUP", AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label CONTAINS ' '  AND visible =1 AND label == name`][1]"));
    private static final Locator SECOND_BUTTON_POPUP = new Locator("SECOND_BUTTON_POPUP", AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label CONTAINS ' '  AND visible =1 AND label == name`][2]"));
    private static final Locator POPUP_HEADER = new Locator("POPUP_HEADER", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and contains(@label, 'account') or contains(@label, 'akunmu')]"));
    private static final Locator LOGOUT_BUTTON = new Locator("LOGOUT_BUTTON", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Keluar' or @label='Log out']"));
    private static final Locator CONFIRM_LOGOUT = new Locator("CONFIRM_LOGOUT", AppiumBy.id("confirm_log_out"));
    private static final Locator CONTINUE_AS_GUEST = new Locator("CONTINUE_AS_GUEST", AppiumBy.xpath("//*[@label='Continue as guest' or @label='Lanjut sebagai tamu']"));
    private static final Locator CHROME = new Locator("CHROME", AppiumBy.accessibilityId("TopBrowserBar"));
    public static final Locator EMAIL_FIELD_NATIVE = new Locator("EMAIL_FIELD_NATIVE", AppiumBy.className("XCUIElementTypeTextField"));
    public static final Locator REVIEW_APP_STORE = new Locator("REVIEW_APP_STORE", AppiumBy.xpath("//XCUIElementTypeButton[@label='Not Now']"));
    private static final Locator CLOSE_CREDENTIALS_PICKER1 = new Locator("CLOSE_CREDENTIALS_PICKER1", AppiumBy.xpath("//*[@name='UICloseButtonBackground']//parent::XCUIElementTypeButton"));
    private static final Locator CLOSE_CREDENTIALS_PICKER2 = new Locator("CLOSE_CREDENTIALS_PICKER2", AppiumBy.xpath("//*[@name='ASCredentialPickerPaneView']//XCUIElementTypeButton"));
    private static final Locator LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_BTN_PAGE_1 = new Locator("LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_BTN_PAGE_1", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Continue with Phone or Email') or contains(@name,'Lanjut dengan nomor HP atau email')]"));
    private static final Locator LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_FIELD = new Locator("LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_FIELD", AppiumBy.xpath("//XCUIElementTypeTextField[contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'phone number or email') or contains(translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'phone number or email') or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'nomor hp atau email') or contains(translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'nomor hp atau email')] | //XCUIElementTypeStaticText[translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='have an account? skip filling this in, log in' or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='punya akun? kamu bisa skip isi ini dengan log in']"));
    private static final Locator CONTINUE_BTN = new Locator("CONTINUE_BTN", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Continue') or contains(@name,'Lanjutkan') or contains(@name,'Register') or contains(@name,'Daftar')]"));
    private static final Locator PASSWORD_FIELD_NATIVE = new Locator("PASSWORD_FIELD_NATIVE", AppiumBy.xpath("//XCUIElementTypeSecureTextField[contains(@name,'Password') or contains(@value,'Password') or contains(@name,'Kata sandi') or contains(@value,'Kata sandi')]"));
    private static final Locator LOG_IN_BTN_NATIVE = new Locator("LOG_IN_BTN_NATIVE", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Log in') or contains(@value,'Log in')]"));
    private static final Locator SKIP_PASS_KEY_NATIVE = new Locator("SKIP_PASS_KEY_NATIVE", AppiumBy.xpath("//XCUIElementTypeButton[(contains(@name,'Skip for now') or contains(@name,'Nanti aja')) and @visible='true']"));
    //Register page locators
    private static final Locator INPUT_FULLNAME = new Locator("INPUT_FULLNAME", AppiumBy.xpath("//XCUIElementTypeTextField[contains(@value,'Full name') or contains(@value,'Nama lengkap')]"));
    private static final Locator INPUT_EMAIL = new Locator("INPUT_EMAIL", AppiumBy.xpath("//XCUIElementTypeTextField[contains(@name,'Email') or contains(@label,'Email') or contains(@value,'Email')]"));
    private static final Locator INPUT_PASSWORD = new Locator("INPUT_PASSWORD", AppiumBy.xpath("//XCUIElementTypeSecureTextField[contains(@value,'Password') or contains(@value,'Kata sandi')]"));
    private static final Locator INPUT_PHONE_NUMBER = new Locator("INPUT_PHONE_NUMBER", AppiumBy.xpath("//XCUIElementTypeTextField[contains(@value,'Phone number') or contains(@value,'Nomor HP')]"));
    private static final Locator REGISTER_BUTTON_SUBMIT = new Locator("REGISTER_BUTTON_SUBMIT", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'Register') or contains(@label,'Daftar')]"));
    private static final Locator OK_CONFIRMATION = new Locator("OK_CONFIRMATION", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'OK') or contains(@name,'Oke')]"));

    //Forgot password locators
    private static final Locator FORGOT_PASSWORD = new Locator("FORGOT_PASSWORD", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Forgot Password') or contains(@name,'Lupa Kata Sandi')]"));
    private static final Locator FORGOT_PASSWORD_NEXT = new Locator("FORGOT_PASSWORD_NEXT", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Next') or contains(@name,'Lanjutkan')]"));
    private static final Locator FORGOT_PASSWORD_INPUT = new Locator("FORGOT_PASSWORD_INPUT", AppiumBy.xpath("//XCUIElementTypeSecureTextField[contains(@name,'New password') or contains(@name,'Kata sandi baru')]"));
    private static final Locator CONFIRM_PASSWORD = new Locator("CONFIRM_PASSWORD", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Confirm') or contains(@name,'Konfirmasi')]"));

    public LoginPageIOS(Session<T> session) {
        super(session);
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
    public void skipRegisterFaceId(){
        driverTools.waitForElementToBeClickable(CLOSE_REGISTER_FACE_ID_FORM_BUTTON,15).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(ACTIVATE_FACE_ID_LATER_BUTTON,15).ifPresent(driverTools::click);
    }

    @Override
    public void loginAsGuest(){
        logger.log("Logging in as guest");
        AccountPage<T> accountPage = AccountPage.getInstance(session);
        HomePage<T> homePage = HomePage.getInstance(this.session);
        if(homePage.isUserLogin()){
            accountPage.openAccountPageUsingDeeplink();
            WebElement e = driverTools.swipeToElement(LOGOUT_BUTTON,Swipe.UP,25).orElseThrow();
            driverTools.swipeUp();
            driverTools.click(e);
            e = driverTools.waitForElementToAppear(CONFIRM_LOGOUT,10).orElseThrow();
            driverTools.click(e);
            if(!driverTools.waitForElementToDisappear(LOGOUT_BUTTON,10)){
                driverTools.click(LOGOUT_BUTTON);
                e = driverTools.waitForElementToPresence(CONFIRM_LOGOUT,10).orElseThrow();
                driverTools.click(e);
                if (driverTools.waitForElementToPresence(CONTINUE_AS_GUEST,5).isPresent()) driverTools.clickElementByCoordinate(CONTINUE_AS_GUEST);
            }
            driverTools.waitForElementToBeClickable(LOGIN_BUTTON,10);
        }
        if(!driverTools.waitForElementToPresence(PROMO_ICON,20).isPresent()){
            openHomepage();
        }
    }

    @Override
    public void openLoginPage(){
        HomePage<T> homePage = HomePage.getInstance(this.session);
        homePage.openAccount();

        driverTools.swipeToElement(LOGIN_BUTTON,Swipe.DOWN,3);
        driverTools.click(LOGIN_BUTTON);

        closeUnmPageRedirection();
    }

    @Override
    public void openLoginPage(String username){
        HomePage<T> homePage = HomePage.getInstance(this.session);
        homePage.openAccount();

        driverTools.swipeToElement(LOGIN_BUTTON,Swipe.DOWN,3);
        driverTools.clickElementByCoordinate(LOGIN_BUTTON);
        this.inputEmailOrPhoneNumberNative(username);
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
    public void closeLoginPage(){
        driverTools.click(CLOSE_LOGIN_PAGE);
    }

    /**
     * Flag-based app closing optimization
     */
    private static boolean lastDeeplinkFailed = false;

    /**
     * Optimized OTP locator with caching to avoid repeated object creation
     */
    private static final Map<Integer, Locator> OTP_LOCATOR_CACHE = new HashMap<>();

    @Override
    public boolean openLoginPageWithDeeplink() {
        String methodName = "openLoginPageWithDeeplink";
        long startTime = CoreFunctions.startTiming(methodName);

        boolean result = false;
        try {
            Deeplink loginPage = new Deeplink("login page",
                    "tiket://gatotkaca.tiket.com/login",
                    "tiket://preprod.tiket.com/login",
                    "tiket://www.tiket.com/login");

            try {
                // Only close app if previous deeplink failed or should force close
                if (lastDeeplinkFailed || shouldForceAppClose()) {
                    logger.log(methodName + " - Force closing app (previous failure or initial state)");
                    driverTools.closeApp();
                } else {
                    logger.log(methodName + " - Skipping app close (previous success)");
                }

                driverTools.deeplink(loginPage);
                closeUnmPageRedirection();
                result = isOnLoginPageWebview();

                // Update flag based on success
                lastDeeplinkFailed = !result;

            } catch (Exception e) {
                logger.log(methodName + " - Error opening deeplink: " + e.getLocalizedMessage());
                lastDeeplinkFailed = true; // Mark as failed for next attempt
                result = false;
            }
        } finally {
            CoreFunctions.endTimingWithResult(methodName, startTime, result);
        }

        return result;
    }

    private boolean shouldForceAppClose() {
        // Force close on first attempt or when specifically needed
        return lastDeeplinkFailed; // Close if last attempt failed
    }

    public boolean isOnLoginPageWebview() {
        String methodName = "isOnLoginPageWebview";
        long startTime = CoreFunctions.startTiming(methodName);

        boolean isWebview = false;
        try {
            isWebview = driverTools.waitForElementToPresence(CHROME, 5).isPresent();

            if (isWebview) {
                //closeCredentialsPicker();
                driverTools.waitForElementToPresence(CLOSE_SIGN_GOOGLE, 3).ifPresent(driverTools::click);
                driverTools.waitForElementToPresence(CLOSE_CREDENTIALS_PICKER1,3).ifPresent(driverTools::click);
                driverTools.waitForElementToPresence(LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_FIELD, 5).ifPresent(driverTools::clickElementByCoordinate);
            }
        } finally {
            CoreFunctions.endTimingWithResult(methodName, startTime, isWebview);
        }

        return isWebview;
    }

    private void closeCredentialsPicker() {
        String methodName = "closeCredentialsPicker";
        long startTime = CoreFunctions.startTiming(methodName);

        try {
            boolean picker1Present = driverTools.waitForElementToPresence(CLOSE_CREDENTIALS_PICKER1, 3).isPresent();

            if (picker1Present) {
                driverTools.clickElementByCoordinate(CLOSE_CREDENTIALS_PICKER1);
                logger.log(methodName + " - Closed credentials picker 1");
                driverTools.waitForElementToDisappear(CLOSE_CREDENTIALS_PICKER1);
            }

            boolean picker2Present = driverTools.waitForElementToPresence(CLOSE_CREDENTIALS_PICKER2, 2).isPresent();
            if (picker2Present) {
                driverTools.clickElementByCoordinate(CLOSE_CREDENTIALS_PICKER2);
                logger.log(methodName + " - Closed credentials picker 2");
                driverTools.waitForElementToDisappear(CLOSE_CREDENTIALS_PICKER2);
            }

            if (!picker1Present && !picker2Present) {
                logger.log(methodName + " - No credential pickers found");
            }
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void loginWith(String loginMethod, String username, String password) {
        String methodName = "loginWith";
        long startTime = CoreFunctions.startTiming(methodName);

        boolean isLoginSuccess = false;
        HomePage<T> homePage = HomePage.getInstance(this.session);

        try {
            // Step 1: Quick login status check
            boolean isLoggedIn = homePage.isUserLogin();
            if (isLoggedIn) {
                logger.log(methodName + " - User already logged in, skipping login");
                isLoginSuccess = true;
                homePage.coachMarkHandling();
                return;
            }

            // Step 2: Attempt deeplink login with optimized app closing
            logger.log(methodName + " - User not logged in, attempting deeplink login");
            boolean deeplinkSuccess = openLoginPageWithDeeplink();

            if(deeplinkSuccess){
                // Step 3: Manual login process
                logger.log(methodName + " - Proceeding with manual native login");
                isLoginSuccess = loginWithNative(loginMethod, username, password,false);
            }

            if (!isLoginSuccess) {
                throw new Exception("Login completed but user is not logged in");
            }

        } catch (Exception e) {
            logger.softWarning(methodName + " - First attempt failed: " + e.getLocalizedMessage() + ", retrying once");

            try {
                // Step 4: Check for auto-login after deeplink
                homePage.goToHomeDeeplink();
                checkForAutoLogin(username);
                // Force app close on retry since first attempt failed
                lastDeeplinkFailed = true;
                openLoginPageWithDeeplink();
                logger.log(methodName + " - Retry: attempting native login");
                isLoginSuccess = loginWithNative(loginMethod, username, password,false);
            } catch (Exception retryException) {
                logger.log(methodName + " - Retry also failed: " + retryException.getLocalizedMessage());
                isLoginSuccess = checkForAutoLogin(username);;
            }
        } finally {
            // Final cleanup and timing
            if (driverTools.isElementDisplayed(REVIEW_APP_STORE)) {
                driverTools.clickElementByCoordinate(REVIEW_APP_STORE);
            }
            CoreFunctions.endTimingWithSuccess(methodName, startTime, isLoginSuccess);
        }
        homePage.coachMarkHandling();
        Assertion.assertThat("Login success", isLoginSuccess, is(true));
    }

    private boolean checkForAutoLogin(String username){
        String methodName = "checkForAutoLogin";
        long startTime = CoreFunctions.startTiming(methodName);
        boolean isLoginSuccess = false;
        HomePage<T> homePage = HomePage.getInstance(this.session);
        if(homePage.isUserLogin()){
            boolean isSameUserAlreadyLogin = homePage.openAccountCenterViaDeeplink(username);
            logger.log(methodName + " - Auto-login detected " + (isSameUserAlreadyLogin ? "from same user" : "from different user"));

            if (isSameUserAlreadyLogin) {
                logger.log(methodName + " - Same user auto-login successful");
                homePage.goToHomeDeeplink();
                isLoginSuccess = true;
            } else {
                throw new ForbiddenException("Auto-login from different user detected, stopping test");
            }
        }
        CoreFunctions.endTimingWithSuccess(methodName, startTime, isLoginSuccess);
        return isLoginSuccess;
    }

    private String maskUsername(String username) {
        if (username == null || username.length() <= 4) return "****";
        return username.substring(0, 2) + "****" + username.substring(username.length() - 2);
    }

    private String maskPassword(String password) {
        if (password == null) return "<null>";
        return "<masked:" + password.length() + ">";
    }

    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 4) return "****";
        return "****" + phone.substring(phone.length() - 3);
    }


    private void logMaskedCredentials(String username, String password) {
        // Check if username is a phone number (digits only, or starts with + followed by digits, length >= 8)
        // Handles formats like: 627778192345, +627778192345, etc.
        boolean isPhoneNumber = username != null && username.trim().matches("\\+?\\d{8,}");
        String maskedUser = isPhoneNumber ? maskPhone(username) : maskUsername(username);
        String maskedPass = maskPassword(password);
        logger.log(String.format("Validating credentials -> username: [%s], password: [%s]", maskedUser, maskedPass));
    }

    private void checkUserAndPasswordValidation(String username, String password) {
        logMaskedCredentials(username,password);
        if (username == null || username.trim().isEmpty()) {
            logger.log("Username is missing");
            throw new SkipException("Username cannot be empty");
        }

        // Check if username is a phone number (digits only, or starts with + followed by digits, length >= 8)
        // Handles formats like: 627778192345, +627778192345, etc.
        String normalizedUsername = username.trim();
        boolean isPhoneNumber = normalizedUsername.matches("\\+?\\d{8,}");
        
        if (isPhoneNumber) {
            logger.log("Phone number detected (" + normalizedUsername + "), skipping password validation");
            return;
        }

        // Password validation for non-phone users
        if (password == null || password.trim().isEmpty()) {
            logger.log("Password is missing");
            throw new SkipException("Password cannot be empty");
        }

        logger.log("Username and password validation passed for user: " + username);
    }

    @Override
    public boolean loginWithNative(String loginMethod, String username, String password,boolean isFromBookingPage) {
        String methodName = "loginWithNative";
        long startTime = CoreFunctions.startTiming(methodName);
        checkUserAndPasswordValidation(username, password);

        boolean loginSuccess = false;
        try {
            logger.log(methodName + " - Starting native login for: " + username);

            // Step 1: Click email/phone login button with optimized wait
            boolean isOldUi = true;
            if (driverTools.waitForElementToPresence(LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_BTN_PAGE_1, 3).isPresent()) {
                driverTools.click(LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_BTN_PAGE_1);
                logger.log(methodName + " - Clicked email/phone login button");
                driverTools.waitForElementToDisappear(LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_BTN_PAGE_1);
                isOldUi = false;
            }

            // Step 2: Input username with optimized interaction
            WebElement emailOrPhoneField = driverTools.waitForElementToPresence(LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_FIELD, 5).orElseThrow();
            driverTools.click(emailOrPhoneField);
            driverTools.sendKeys(LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_FIELD, username);
            driverTools.click(CONTINUE_BTN);
            logger.log(methodName + " - Entered username and clicked continue");


            if(loginMethod.contains("email")){
                // Step 3: Input password with optimized wait
                if(driverTools.waitForElementToPresence(PASSWORD_FIELD_NATIVE,3).isEmpty()){
                    if (isOldUi)
                        driverTools.click(LOG_IN_BTN_NATIVE);
                    closeCredentialsPicker();
                    if(username.contains("gmail.com")){
                        //google signin popup
                        driverTools.swipeToPresentedElement(SKIP_PASS_KEY_NATIVE,Swipe.UP,1).ifPresent(driverTools::click);
                    }
                }

                WebElement passwordField = driverTools.waitForElementToPresence(PASSWORD_FIELD_NATIVE, 3).orElseThrow();
                driverTools.click(passwordField);
                driverTools.sendKeys(PASSWORD_FIELD_NATIVE, password);
                driverTools.click(LOG_IN_BTN_NATIVE);
                logger.log(methodName + " - Entered password and clicked login");
                // Step 4: Handle OTP if present
                if (driverTools.waitForElementToPresence(OTP_BTM_SHEET_TITLE_NATIVE, 3).isPresent()) {
                    logger.log(methodName + " - OTP required, entering test OTP");
                    handleOTPEntry();
                }
            } else {
                if (isOldUi)
                    driverTools.click(LOG_IN_BTN_NATIVE);

                // Step 3: Handle phone number login with OTP (try twice)
                logger.log(methodName + " - Phone number login, attempting OTP entry");
                
                // First OTP attempt
                if (driverTools.waitForElementToPresence(OTP_BTM_SHEET_TITLE_NATIVE, 5).isPresent()) {
                    logger.log(methodName + " - First OTP attempt for phone number");
                    handleOTPEntry();
                    
                    // Second OTP attempt if still needed
                    if (driverTools.waitForElementToPresence(OTP_BTM_SHEET_TITLE_NATIVE, 5).isPresent()) {
                        logger.log(methodName + " - Second OTP attempt for phone number");
                        handleOTPEntry();
                    }
                }
            }

            // Step 5: Skip passkey setup
            driverTools.swipeToPresentedElement(SKIP_PASS_KEY_NATIVE,Swipe.UP,1);
            HomePage<T> homePage = HomePage.getInstance(this.session);
            if (driverTools.waitForElementToPresence(SKIP_PASS_KEY_NATIVE, 5).isPresent()) {
                driverTools.click(SKIP_PASS_KEY_NATIVE);
                logger.log(methodName + " - Skipped passkey setup");
            } else if (!driverTools.isElementDisplayed(SKIP_PASS_KEY_NATIVE) && !homePage.isUserLogin()) {
                logger.log(methodName + " - Passkey setup is not present");
                homePage.goToHomeUsingDeeplinkWithoutCondition();
            }


            // Step 6: Verify login success
            if(!isFromBookingPage){
                loginSuccess = homePage.isUserLogin();
            }else {
                loginSuccess = driverTools.waitForElementToDisappear(LOG_IN_BTN_NATIVE);
            }

            logger.log(methodName + " - Login result: " + loginSuccess);
        } finally {
            CoreFunctions.endTimingWithSuccess(methodName, startTime, loginSuccess);
        }

        HomePage.getInstance(session).coachMarkHandling();
        return loginSuccess;
    }

    private void inputPhoneNumberOrEmailNative(String PhoneNumberOrEmail,String purpose) {
        String methodName = "setInputPhoneNumberOrEmailNative";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            // Step 1: Click email/phone login button with optimized wait
            boolean isOldUi = true;
            if (driverTools.waitForElementToPresence(LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_BTN_PAGE_1, 3).isPresent()) {
                driverTools.click(LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_BTN_PAGE_1);
                logger.log(methodName + " - Clicked email/phone login button");
                driverTools.waitForElementToDisappear(LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_BTN_PAGE_1);
                isOldUi = false;
            }

            // Step 2: Input username with optimized interaction
            WebElement emailOrPhoneField = driverTools.waitForElementToPresence(LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_FIELD, 5).orElseThrow();
            driverTools.click(emailOrPhoneField);
            driverTools.sendKeys(LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_FIELD, PhoneNumberOrEmail);
            driverTools.click(CONTINUE_BTN);
            logger.log(methodName + " - Entered username and clicked continue");

            if (isOldUi && purpose.equals("login")) {
                driverTools.click(LOG_IN_BTN_NATIVE);
            }

        }finally {
            CoreFunctions.endTimingWithSuccess(methodName, startTime, driverTools.waitForElementToDisappear(CONTINUE_BTN));
        }
    }

    /**
     * Optimized OTP entry method with performance timing
     */
    private void handleOTPEntry() {
        String methodName = "handleOTPEntry";
        long startTime = CoreFunctions.startTiming(methodName);

        try {
            logger.log(methodName + " - Entering OTP digits");
            for (int i = 1; i <= 6; i++) {
                Locator otpField = otpLocator(i);
                if (driverTools.waitForElementToPresence(otpField, 2).isPresent()) {
                    driverTools.click(otpField);
                    driverTools.sendKeys(otpField, String.valueOf(i));
                }
            }
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    private Locator otpLocator(int index) {
        return OTP_LOCATOR_CACHE.computeIfAbsent(index,
                i -> new Locator("OTP_FIELD_" + i,
                        AppiumBy.xpath("(//XCUIElementTypeOther[contains(@name,'OTP')]/following-sibling::XCUIElementTypeTextField)[" + i + "]")));
    }

    @Override
    public boolean loginWithB2BRetry(String loginMethod,String username,String password) {
        // input credential based on available method.
        // can only be used if user in homepage
        HomePage<T> homePage = HomePage.getInstance(this.session);
        boolean isLoggedIn = homePage.isUserLogin();
        if(isLoggedIn){
            logger.log("User already login, skip login");
            return true;
        }
        else {
            openLoginPageWithDeeplink();
            logger.log("Retry in webview unm page");
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
            homePage.coachMarkHandling();
            boolean isLoginSuccess = loginWithWebview(loginMethod, username, password);
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
    public void closeUnmPageRedirection(){
        Map<String, Object> caps = driverTools.getCapabilities();
        boolean autoDismissAlerts = caps.get("autoDismissAlerts")==null? false : (boolean) caps.get("autoDismissAlerts");
        logger.log("autoDismissAlerts: " + autoDismissAlerts);

        boolean isRunningOnDeviceFarm = CapabilitiesHandler.isRunningOnDeviceFarm();
        logger.log("is on bs: " + isRunningOnDeviceFarm);

        if(!autoDismissAlerts || !isRunningOnDeviceFarm){
            driverTools.waitForElementToAppear(CLOSE_SIGN_GOOGLE).ifPresent(driverTools::click);
            driverTools.waitForElementToPresence(CONTINUE_BUTTON_UNM_REDIRECTION).ifPresent(driverTools::click);
        }
    }

    @Override
    public void inputEmailOrPhoneNumberNative(String username){
        if(driverTools.isElementDisplayed(EMAIL_OR_PHONE_NUMBER_FIELD)){
            driverTools.sendKeys(EMAIL_OR_PHONE_NUMBER_FIELD, username);
            driverTools.click(CREATE_ACCOUNT_BUTTON);
            driverTools.waitForElementToDisappear(CREATE_ACCOUNT_BUTTON);
        }
        closeUnmPageRedirection();
    }


    @Override
    public void loginFromBookingForm(String vertical){} // FIXME: Implementation pending


    @Override
    public void loginFromOrderPage(String username,String password){
        driverTools.click(LOG_IN_BTN_ORDER_PAGE);
        closeUnmPageRedirection();
    }

    @Override
    public void loginFromDifferentPages(String username,String password) {
        logger.log("Retry in webview unm page");
        closeUnmPageRedirection();
        loginWithNative("email",username,password,true);
    }

    @Override
    public boolean isLoginFormDisplayedForDifferentPages() {
        closeUnmPageRedirection();
        return driverTools.waitForElementToPresence(LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_BTN_PAGE_1, 10).isPresent()
                || driverTools.waitForElementToPresence(LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_FIELD, 10).isPresent()
                || driverTools.waitForElementToPresence(EMAIL_OR_PHONE_NUMBER_FIELD, 10).isPresent()
                || driverTools.waitForElementToPresence(PASSWORD_FIELD_NATIVE, 10).isPresent();
    }

    @Override
    public void loginFromDifferentPagesUsingPhoneNumber(String phoneNumber) {
        closeUnmPageRedirection();
        loginWithNative("phonenumber", phoneNumber, "",true);
    }

    @Override
    protected boolean checkForSsoLogin(String username){
        HomePage<T> homePage = HomePage.getInstance(this.session);
        boolean isOnHomePage = homePage.isOnHomePage(10);
        if(isOnHomePage){
            boolean isSameUserAlreadyLogin = homePage.openAccountCenterViaDeeplink(username);
            logger.log("Auto-login detected " + (isSameUserAlreadyLogin ? "from same user" : "from different user"));

            if (isSameUserAlreadyLogin) {
                logger.log("Same user auto-login successful");
                homePage.goToHomeDeeplink();
                return true;
            } else {
                throw new ForbiddenException("Auto-login from different user detected, stopping test");
            }
        }
        return false;
    }

    @Override
    public void openHomepage(){
        if(driverTools.waitForElementToPresence(AccountPageIOS.FLAG_TOP_OF_ACCOUNT_PAGE,5).isPresent()){
            logger.log("Clicking home icon");
            driverTools.click(HomePageIOS.HOME);
        }
    }

    @Override
    public void loginWithSuspendedEmail(String email){
        inputEmailOrPhoneNumber(email);
    }

    @Override
    public void registerWithSuspendedEmail(String email) {
        closeUnmPageRedirection();
        inputEmailOrPhoneNumber(email);
    }

    @Override
    public boolean verifySuspendedAccountPopup(String[] expectedTexts){
        String headerText = driverTools.getText(POPUP_HEADER).orElseThrow();
        Assertion.assertThat("Header text as expected to language",headerText,is(expectedTexts[0]));

        String firstButtonText = driverTools.getText(FIRST_BUTTON_POPUP).orElseThrow();
        Assertion.assertThat("First button text as expected to language",firstButtonText,is(expectedTexts[1]));

        String secondButtonText = driverTools.getText(SECOND_BUTTON_POPUP).orElseThrow();
        Assertion.assertThat("Second button text as expected to language",secondButtonText,is(expectedTexts[2]));
        return true;
    }


    @Override
    public void registerWithNewPhoneNumberOrEmail(String PhoneNumberOrEmail){
        closeUnmPageRedirection();
        inputPhoneNumberOrEmailNative(PhoneNumberOrEmail,"register");
    }

    @Override
    public void inputDetailsRegisterPage(String fullname, String email, String password) {
        String methodName = "inputDetailsRegisterPage";
        long startTime = CoreFunctions.startTiming(methodName);
        checkUserAndPasswordValidation(email,password);
        driverTools.click(INPUT_FULLNAME);
        driverTools.sendKeys(INPUT_FULLNAME, fullname);
        driverTools.hideKeyboard();

        driverTools.click(INPUT_EMAIL);
        driverTools.sendKeys(INPUT_EMAIL, email);
        driverTools.hideKeyboard();

        driverTools.click(INPUT_PASSWORD);
        driverTools.sendKeys(INPUT_PASSWORD, password);
        driverTools.swipeUp();
        driverTools.hideKeyboard();

        driverTools.click(REGISTER_BUTTON_SUBMIT);
        // Step 4: Handle OTP if present
        if (driverTools.waitForElementToPresence(OTP_BTM_SHEET_TITLE_NATIVE, 3).isPresent()) {
            logger.log(methodName + " - OTP required, entering test OTP");
            handleOTPEntry();
        }

        try {
            // Step 4: Handle OK confirmation if present
            if(driverTools.waitForElementToPresence(OK_CONFIRMATION).isPresent()){
                driverTools.click(OK_CONFIRMATION);
                driverTools.waitForElementToDisappear(OK_CONFIRMATION);
            }

            // Step 5: Skip passkey setup
            driverTools.swipeToPresentedElement(SKIP_PASS_KEY_NATIVE,Swipe.UP,1);
            if (driverTools.waitForElementToPresence(SKIP_PASS_KEY_NATIVE, 5).isPresent()) {
                driverTools.click(SKIP_PASS_KEY_NATIVE);
                logger.log(methodName + " - Skipped passkey setup");
            }
            driverTools.waitForElementToDisappear(SKIP_PASS_KEY_NATIVE);
            HomePage<T> homePage = HomePage.getInstance(this.session);
            homePage.waitForHomePageToLoad();
        }catch (Exception e) {
            logger.log("Exception while switching to native: " + e.getMessage());
        }finally {
            CoreFunctions.endTimingWithSuccess(methodName, startTime, true);
        }
    }

    @Override
    public void inputDetailsRegisterPageEmail(String fullname, String phoneNumber, String password){
        String methodName = "inputDetailsRegisterPageEmail";
        long startTime = CoreFunctions.startTiming(methodName);
        checkUserAndPasswordValidation(phoneNumber,password);
        driverTools.sendKeys(INPUT_FULLNAME, fullname);
        driverTools.sendKeys(INPUT_PHONE_NUMBER, phoneNumber);
        driverTools.sendKeys(INPUT_PASSWORD, password);
        driverTools.clickWithJavascriptExecutor(REGISTER_BUTTON_SUBMIT);
        // Step 3: Handle OTP if present
        if (driverTools.waitForElementToPresence(OTP_BTM_SHEET_TITLE_NATIVE, 3).isPresent()) {
            logger.log(methodName + " - OTP required, entering test OTP");
            handleOTPEntry();
        }
        try {
            // Step 4: Handle OK confirmation if present
            if(driverTools.waitForElementToPresence(OK_CONFIRMATION).isPresent()){
                driverTools.clickWithJavascriptExecutor(OK_CONFIRMATION);
                driverTools.waitForElementToDisappear(OK_CONFIRMATION);
            }

            // Step 5: Skip passkey setup
            driverTools.swipeToPresentedElement(SKIP_PASS_KEY_NATIVE,Swipe.UP,1);
            if (driverTools.waitForElementToPresence(SKIP_PASS_KEY_NATIVE, 5).isPresent()) {
                driverTools.click(SKIP_PASS_KEY_NATIVE);
                logger.log(methodName + " - Skipped passkey setup");
            }

        }catch (Exception e) {
            logger.log("Exception while switching to native: " + e.getMessage());
        }finally {
            CoreFunctions.endTimingWithSuccess(methodName, startTime, true);
        }
    }

    @Override
    public void loginOnFacebookPage(String username, String password) {
        driverTools.waitForElementToAppear(INPUT_FACEBOOK_EMAIL);
        driverTools.sendKeys(INPUT_FACEBOOK_EMAIL, username);
        driverTools.sendKeys(INPUT_FACEBOOK_PASSWORD, password);
        driverTools.click(LOGIN_FACEBOOK);
    }

    @Override
    public void loginOnGooglePage(String username, String password) {
        //implement it
    }

    @Override
    public String forgetPassword(String username, String password) {
        String methodName = "forgetPassword";
        inputPhoneNumberOrEmailNative(username,"login");
        clickForgotPasswordLoginPage();
        clickNextOnForgotPasswordPage();
        if (driverTools.waitForElementToPresence(OTP_BTM_SHEET_TITLE_NATIVE, 3).isPresent()) {
            logger.log(methodName + " - OTP required, entering test OTP");
            handleOTPEntry();
        }
        changePasswordOnForgotPasswordPage(password);
        return password;
    }

    @Override
    public String forgetPasswordPhoneNumber(String email,String phoneNo,String updatedPassword) {
        String methodName = "forgetPasswordPhoneNumber";
        inputPhoneNumberOrEmailNative(email,"login");
        clickForgotPasswordLoginPage();
        changeEmailToPhoneNumberOnForgotPasswordPage(phoneNo);
        clickNextOnForgotPasswordPage();
        if (driverTools.waitForElementToPresence(OTP_BTM_SHEET_TITLE_NATIVE, 3).isPresent()) {
            logger.log(methodName + " - OTP required, entering test OTP");
            handleOTPEntry();
        }
        changePasswordOnForgotPasswordPage(updatedPassword);
        return updatedPassword;
    }

    private void clickForgotPasswordLoginPage(){
        WebElement forgotPassword = driverTools.waitForElementToPresence(FORGOT_PASSWORD).orElseThrow();
        driverTools.click(forgotPassword);
    }

    private void clickNextOnForgotPasswordPage(){
        WebElement next = driverTools.waitForElementToPresence(FORGOT_PASSWORD_NEXT).orElseThrow();
        driverTools.click(next);
    }

    private void changeEmailToPhoneNumberOnForgotPasswordPage(String phoneNo){
        WebElement emailOrPhoneField = driverTools.waitForElementToPresence(LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_FIELD).orElseThrow();
        driverTools.click(emailOrPhoneField);
        driverTools.clear(LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_FIELD);

        //check if input is empty
        if (driverTools.getValueAttribute(LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_FIELD, "value").isEmpty()) {
            logger.log("Input field is empty, proceeding to send phone number");
        } else {
            logger.log("Input field is not empty, clearing it before sending phone number");
            driverTools.clear(LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_FIELD);
        }
        driverTools.sendKeys(LOGIN_WITH_EMAIL_OR_PHONE_NUMBER_FIELD,phoneNo);
    }

    private void changePasswordOnForgotPasswordPage(String updatedPassword){
        driverTools.waitForElementToAppear(FORGOT_PASSWORD_INPUT).ifPresent(driverTools::click);
        driverTools.sendKeys(FORGOT_PASSWORD_INPUT,updatedPassword);

        logger.log("completing forgot password by clicking confirm password");
        driverTools.hideKeyboard();
        driverTools.click(CONFIRM_PASSWORD);
        driverTools.waitForElementToDisappear(CONFIRM_PASSWORD);

        //verify back to login page
        Boolean isOnLoginPage = driverTools.isElementDisplayed(LOG_IN_BTN_NATIVE);
        Assertion.assertThat("Forgot password Successful", isOnLoginPage, Matchers.is(true));
    }

    @Override
    public void loginAfterForgotPasswordWithEmail(String username, String password) {
        String methodName = "loginAfterForgotPasswordWithEmail";
        // Step 1: Click email/phone login button with optimized wait
        WebElement passwordField = driverTools.waitForElementToPresence(PASSWORD_FIELD_NATIVE, 3).orElseThrow();
        driverTools.click(passwordField);
        driverTools.sendKeys(PASSWORD_FIELD_NATIVE, password);
        driverTools.click(LOG_IN_BTN_NATIVE);
        logger.log(methodName + " - Entered password and clicked login");

        // Step 2: Handle OTP if present
        if (driverTools.waitForElementToPresence(OTP_BTM_SHEET_TITLE_NATIVE, 3).isPresent()) {
            logger.log(methodName + " - OTP required, entering test OTP");
            handleOTPEntry();
        }

        // Step 3: Skip passkey setup
        driverTools.swipeToPresentedElement(SKIP_PASS_KEY_NATIVE,Swipe.UP,1);
        if (driverTools.waitForElementToPresence(SKIP_PASS_KEY_NATIVE, 5).isPresent()) {
            driverTools.click(SKIP_PASS_KEY_NATIVE);
            logger.log(methodName + " - Skipped passkey setup");
        }
    }
}