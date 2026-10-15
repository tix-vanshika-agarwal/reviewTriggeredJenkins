package com.tiket.web.page.common.home;

import com.tiket.appium.PopupObserver;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.*;

import org.testng.SkipException;

import javax.security.auth.login.FailedLoginException;
import java.util.List;
import java.util.Optional;

public abstract class LoginPageWeb<T extends WebDriver> extends WebPage<T> {

    public LoginPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> LoginPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
        case DWEB -> {
            return new LoginPageDWeb<>(session);
        }
        case AMWEB, IMWEB, ANDROID, IOS -> {
            return new LoginPageMWeb<>(session);
        }
        default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator LOGINFORM_BUTTON = new Locator("LOGINFORM_BUTTON", new By.ByXPath("//button[contains(@class, 'cta')]"));
    private static final Locator EMAIL_TEXTFIELD = new Locator("EMAIL_TEXTFIELD", new By.ByXPath("//input[contains(@id, 'email') or @id='nomor-hp-atau-email']"));
    private static final Locator PASSWORD_TEXTFIELD = new Locator("PASSWORD_TEXTFIELD", new By.ByXPath("//input[contains(@name, 'password')]"));
    private static Locator otpField(int index) {
        return new Locator("OTP_TEXTFIELD" + index, new By.ByXPath("//input[@data-testid='otp-input-" + index + "']"));
    }
    private static final Locator BOOKING_LIST_SIDEBAR_ITEM = new Locator("BOOKING_LIST_SIDEBAR_ITEM", new By.ByXPath("//*[contains(text(), 'Booking List')]"));
    /** Back-off before the second precondition login attempt, to ride out a transient environment problem. */
    protected static final int LOGIN_RETRY_WAIT_SECONDS = 3;
    /** Precondition login attempts: the first one plus a single retry from a reset browser session. */
    private static final int LOGIN_ATTEMPTS = 2;
    private static final int NEW_PASSWORD_FILL_ATTEMPTS = 3;
    private static final String STAGING_URL = "https://gatotkaca.tiket.com/";
    private static final String PREPROD_URL = "https://preprod.tiket.com/";
    private static final String PROD_URL = "https://tiket.com/";
    protected static final Locator ACCOUNT_POP_UP = new Locator("ACCOUNT_POP_UP", new By.ByXPath("//button[contains(@class, 'Coachmark_coachmark_done_btn')]"));
    private static final Locator EMAIL_PASS_NOT_MATCH = new Locator("EMAIL_PASS_NOT_MATCH", new By.ByXPath("//span[text()='Email atau kata sandi salah.' or text()=\"Email or password doesn't match.\"]"));
    private static final Locator SKIP_FOR_NOW_BUTTON = new Locator("SKIP_FOR_NOW_BUTTON", new By.ByXPath("//span[@role='button' and (contains(text(), 'Skip for now') or contains(text(),'Nanti saja'))]"));
    private static final Locator GOOGLE_SIGNUP = new Locator("GOOGLE_SIGNUP", new By.ByXPath("//h2[contains(text(), 'Log in faster with Google!') or contains(text(),'Log in sat-set pakai Google!')]"));
    private static final Locator FORGOT_PASSWORD = new Locator("FORGOT_PASSWORD", new By.ByXPath("//span[text()='Forgot Password' or text()='Lupa Kata Sandi']"));
    private static final Locator NEXT_BUTTON = new Locator("NEXT_BUTTON", new By.ByXPath("//button[contains(., 'Next') or contains(., 'Lanjutkan')]"));
    private static final Locator NEW_PASSWORD_TEXTFIELD = new Locator("NEW_PASSWORD_TEXTFIELD", new By.ByXPath("//input[contains(@id, 'new-password') or contains(@id, 'kata-sandi-baru')]"));
    private static final Locator CONFIRM_BUTTON = new Locator("CONFIRM_BUTTON", new By.ByXPath("//button[contains(., 'Confirm') or contains(., 'Konfirmasi')]"));
    private static final Locator PN_OR_EMAIL_TEXTFIELD = new Locator("PN_OR_EMAIL_TEXTFIELD", new By.ByXPath("//input[(@id='phone-number-or-email' or @id='nomor-hp-atau-email') and not(@disabled)]"));
    private static final Locator SKIP_LINK_GOOGLE_NUDGE = new Locator("SKIP_LINK_GOOGLE_NUDGE", new By.ByXPath("//span[contains(@class,'Nudge_google_nudge_footer')]"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", new By.ByXPath("//button[contains(@class,'InitialForm_cta_button')] | //span[normalize-space()='Continue with Phone or Email']"));
    private static final Locator PASSKEY_POPUP = new Locator("PASSKEY_POPUP", new By.ByXPath("//div[contains(@class, 'Setup_container')]//h2[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'passkey')]"));
    private static final Locator SKIP_PASSKEY = new Locator("SKIP_PASSKEY", new By.ByXPath("//span[contains(@class, 'Setup_skip_cta')]"));
    private static final Locator MAYBE_LATER_BUTTON = new Locator("MAYBE_LATER_BUTTON", new By.ByXPath("//button[normalize-space(text())='Maybe later' or normalize-space(text())='Nanti aja']"));
    private static final Locator EMAIL_OR_PHONE_NUMBER_FIELD = new Locator("EMAIL_OR_PHONE_NUMBER_FIELD", new By.ByXPath("//div[contains(@class,'OneField')]//input"));
    private static final Locator REGISTER_BUTTON = new Locator("REGISTER_BUTTON", new By.ByXPath("//*[contains(@class,'LoginRegisterLayout')]//button[@data-testid='btnSubmitForm']"));
    private static final Locator FULLNAME = new Locator("FULLNAME", new By.ByXPath("//input[contains(@data-testid, 'txtFullname')]"));
    private static final Locator INPUT_EMAIL = new Locator("INPUT_EMAIL", new By.ByXPath("//input[contains(@name,'email')]"));
    private static final Locator INPUT_PASSWORD = new Locator("INPUT_PASSWORD", new By.ByXPath("//input[contains(@name,'password')]"));
    private static final Locator REGISTER_BUTTON_SUBMIT = new Locator("REGISTER_BUTTON_SUBMIT", new By.ByXPath("//*[contains(@data-testid,'btnSubmit')]"));
    private static final Locator OK_CONFIRMATION = new Locator("OK_CONFIRMATION", new By.ByXPath("//button[contains(@class, 'Button_button') and (contains(text(), 'OK') or contains(text(), 'Oke'))]"));
    private static final Locator REGISTER_NOW = new Locator("REGISTER_NOW", new By.ByXPath("//*[contains(text(), \"Register now!\") or contains(text(), \"Daftar, yuk!\")]"));
    private static final Locator PAYMENT_PAGE_INDICATOR = new Locator("PAYMENT_PAGE_INDICATOR", new By.ByXPath("//div[contains(@class,'Countdown_countdown')]"));
    private static final String PREPROD_ACCOUNT_DETAILS_PAGE = "https://preprod.tiket.com/myaccount/passengers/detail?ref=myaccount";
    private static final Locator LOGIN_V2 = new Locator("LOGIN_V2", By.xpath("//button[.//span[contains(@class, 'SetupV2_button') and (contains(., 'nomor HP atau email') or contains(., 'Phone or Email'))]]"));
    private static final Locator V2_ONEFIELD = new Locator("V2_ONEFIELD", By.xpath("//input[@id='phone-number-or-email' or @id='nomor-hp-atau-email']"));
    private static final Locator V2_NEXT_BUTTON = new Locator("V2_NEXT_BUTTON", By.xpath("//button[@type='submit']"));
    private static final Locator V2_PASSWORD_FIELD = new Locator("V2_PASSWORD_FIELD", By.xpath("//input[@data-testid='txtPassword']"));
    private static final Locator ERROR_SNACKBAR_B2B = new Locator("ERROR_SNACKBAR_B2B", new By.ByXPath("//div[contains(@class, 'Snackbar_type_error')]//div[contains(@class, 'Snackbar_text_content')]"));
    private static final Locator ERROR_SNACKBAR_GENERAL = new Locator("ERROR_SNACKBAR_GENERAL", new By.ByXPath("//div[contains(@class, 'Snackbar_type_error')] | //div[contains(@class, 'Toaster_type_error')] | //div[contains(@class, 'snackbar') and contains(@class, 'error')]"));

    public void loginWebV2(String email, String password, String otp, int retryCounter) throws FailedLoginException, InterruptedException {
        logger.log("Getting UNM v2 Login. Proceed with it.");
        // driverTools.waitForElementToAppear(LOGIN_V2, 2).ifPresent(driverTools::clickWithJavascriptExecutor);
        clickContinueWithPhoneOrEmail();
        driverTools.waitForElementToAppear(V2_ONEFIELD);
        driverTools.sendKeys(V2_ONEFIELD, email);
        driverTools.modifyApiResponse("/api/v1/auth/methods", "(res) => { if (res?.data?.passkey) { res.data.passkey.isPasskeyExist = false; } return res; }");
        driverTools.clickWithJavascriptExecutor(V2_NEXT_BUTTON);
        PopupObserver.inject(driver, List.of(SKIP_LINK_GOOGLE_NUDGE));
        driverTools.waitForElementToAppear(PASSWORD_TEXTFIELD);
        driverTools.sendKeys(V2_PASSWORD_FIELD, password);
        driverTools.clickWithJavascriptExecutor(V2_NEXT_BUTTON);
        handlingErrorSnackbar();
        if (driverTools.waitForElementToAppear(otpField(1), 5).isPresent()) {
            logger.log("OTP screen displayed. Proceeding with OTP.");
            this.inputOTPCode(otp, retryCounter);
        } else {
            logger.log("OTP screen not displayed(optional). Login may have completed without OTP.");
        }
    }

    public abstract boolean isRegisterPageOpened();

    public abstract void registerIntoDweb(String email) throws FailedLoginException, InterruptedException;

    public boolean isLoginPageOpened() {
        driverTools.waitForElementToAppear(LOGIN_BUTTON, 10);
        return driverTools.isElementDisplayed(LOGIN_BUTTON);
    }

    public boolean isLoginV2PageOpened() {
        driverTools.waitForElementToAppear(LOGIN_V2, 10);
        return driverTools.isElementDisplayed(LOGIN_V2);
    }

    public abstract String forgotPasswordDweb(String email, String wrongPassword, String otp, int retryCounter) throws FailedLoginException, InterruptedException;

    public abstract void loginAsGuest();

    protected String getTiketWebBaseUrl() {
        return switch (Configs.ENVIRONMENT) {
        case GK -> STAGING_URL;
        case PREPROD -> PREPROD_URL;
        case PROD -> PROD_URL;
        default -> throw new NotImplementedException("Unsupported environment: " + Configs.ENVIRONMENT);
        };
    }

    public void goToAccountPage() {
        driverTools.maximizeWindow();
        String baseUrl = getTiketWebBaseUrl();

        if (!baseUrl.isEmpty()) {
            driverTools.get(baseUrl + "myaccount");
        }
        PopupObserver.inject(driver, List.of(ACCOUNT_POP_UP));
    }

    public abstract void loginIfNotWithSameEmail(String email, String password, String otp, int i) throws FailedLoginException, InterruptedException;

    public abstract void continueIfAlreadyLoggedIn(String email, String password, String otp, int i) throws FailedLoginException, InterruptedException;

    public abstract boolean checkIfLoggedIn();

    public abstract void loginStepsWithoutRetry(String username, String password, String otp);

    public abstract void logoutFromDweb();

    public abstract boolean isAccountHolderNameDisplayed();

    public abstract void loginSteps(String email, String password, String otp, int i) throws FailedLoginException, InterruptedException;

    public abstract void skipUNMPageAfterLogin(String timeOut);

    public abstract String accountHolderName();

    public Boolean loginIntoWeb(String email, String password, String otp, int retryCounter) throws FailedLoginException, InterruptedException {
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        homePage.goToHomePage();
        homePage.clickOnLoginButton();
        loginWebV2(email, password, otp, retryCounter);
        skipPassKey();
        driverTools.waitForElementToAppear(BOOKING_LIST_SIDEBAR_ITEM, 4);
        return true;
    }

    /**
     * UI login that opens the login URL directly instead of clicking the home header's Login link, so a
     * home page in an unexpected state (guest session just installed, popups, header not hydrated) cannot
     * send the flow somewhere other than the login page.
     */
    public void loginIntoWebViaLoginUrl(String email, String password, String otp, int retryCounter) throws FailedLoginException, InterruptedException {
        driverTools.getWithDynamicTimeout(getTiketWebBaseUrl() + "login", 60);
        loginWebV2(email, password, otp, retryCounter);
        skipPassKey();
        driverTools.waitForElementToAppear(BOOKING_LIST_SIDEBAR_ITEM, 4);
    }

    public boolean loginIntoWebFromDifferentPage(String email, String password, String otp, int retryCounter) throws FailedLoginException, InterruptedException {
        driverTools.waitForElementToAppear(LOGIN_V2, 10);
        if (driverTools.isElementDisplayed(LOGIN_V2)) {
            loginWebV2(email, password, otp, retryCounter);
        } else {
            inputEmail(email);
            PopupObserver.inject(driver, List.of(SKIP_LINK_GOOGLE_NUDGE));
            inputPassword(password);
            skipPassKey();
            if (driverTools.waitForElementToAppear(EMAIL_PASS_NOT_MATCH, 5).isPresent())
                return false;
            inputOTPCode(otp, retryCounter);
        }
        skipPassKey();
        return true;
    }

    public void logoutNeeded() {
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        homePage.goToHomePage();
        if (checkIfLoggedIn()) {
            logoutFromDweb();
        }
    }

    public void loginIntoWebAfterPopUpAppears(String email, String password, String otp, int retryCounter) throws FailedLoginException, InterruptedException {
        inputEmail(email);
        if (driverTools.waitForElementToAppear(GOOGLE_SIGNUP, 10).isPresent()) {
            driverTools.swipeToElement(SKIP_FOR_NOW_BUTTON, Swipe.UP, 5);
            driverTools.clickWithJavascriptExecutor(SKIP_FOR_NOW_BUTTON);
        }
        inputPassword(password);
        skipPassKey();
        while (driverTools.waitForElementToAppear(EMAIL_PASS_NOT_MATCH, 5).isPresent() && retryCounter < 4) {
            inputEmail(email);
            inputPassword(password);
            retryCounter++;
            if (retryCounter > 3) {
                logger.log("Tried logging in three times, but failed");
                throw new FailedLoginException();
            }
        }
        inputOTPCode(otp, retryCounter);
        skipPassKey();
    }

    public void inputEmail(String email) {
        driverTools.waitForElementToAppear(LOGIN_V2, 5).ifPresent(driverTools::click);
        if (driverTools.waitForElementToAppear(V2_ONEFIELD, 5).isPresent()) {
            driverTools.clickWithJavascriptExecutor(V2_ONEFIELD);
            driverTools.sendKeys(V2_ONEFIELD, email);
            driverTools.clickWithJavascriptExecutor(V2_NEXT_BUTTON);
        } else {
            driverTools.waitForElementToAppear(EMAIL_TEXTFIELD);
            driverTools.clickWithJavascriptExecutor(EMAIL_TEXTFIELD);
            driverTools.sendKeys(EMAIL_TEXTFIELD, email);
            driverTools.waitForElementToAppear(LOGINFORM_BUTTON);
            driverTools.clickWithJavascriptExecutor(LOGINFORM_BUTTON);
        }
    }

    public void inputPassword(String password) {
        driverTools.waitForElementToAppear(PASSWORD_TEXTFIELD);
        driverTools.sendKeys(PASSWORD_TEXTFIELD, password);
        driverTools.waitForElementToAppear(LOGINFORM_BUTTON);
        driverTools.clickWithJavascriptExecutor(LOGINFORM_BUTTON);
    }

    public void inputOTPCode(String otp, int retryCounter) throws InterruptedException, FailedLoginException {
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        if (retryCounter > 3) {
            logger.log("Tried logging in three times, but failed");
            throw new FailedLoginException();
        }
        driverTools.waitForElementToAppear(otpField(1), 15);
        if (driverTools.isElementDisplayed(otpField(1))) {
            enterOTPs(otp);
        } else if (driverTools.isElementDisplayed(BOOKING_LIST_SIDEBAR_ITEM)
                || homePage.isHomePageOpened()
                || driverTools.isElementDisplayed(PAYMENT_PAGE_INDICATOR)
                || driverTools.isElementDisplayed(PASSKEY_POPUP)
                || driverTools.isElementDisplayed(OK_CONFIRMATION)
                || driverTools.isElementDisplayed(MAYBE_LATER_BUTTON)) {
            logger.log("User is already logged in or registration completed. No OTP needed");
        } else {
            retryCounter++;
            Thread.sleep(30000);
            if (driverTools.isElementDisplayed(LOGINFORM_BUTTON)) {
                driverTools.click(LOGINFORM_BUTTON);
            }
            inputOTPCode(otp, retryCounter);
        }
    }

    public void logOutFromAccountPage() {
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        goToAccountPage();
        if (isAccountHolderNameDisplayed()) {
            logoutFromDweb();
        } else {
            homePage.goToHomePage();
        }
    }

    public void enterEmailAndClickForgotPassword(String email) throws FailedLoginException, InterruptedException {
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        homePage.goToHomePage();
        homePage.clickOnLoginButton();
        inputEmail(email);
        clickForgotPassword();
    }

    public void clickForgotPassword() {
        driverTools.waitForElementToAppear(FORGOT_PASSWORD);
        driverTools.clickWithJavascriptExecutor(FORGOT_PASSWORD);
    }

    public void clickNextButton() {
        driverTools.waitForElementToAppear(NEXT_BUTTON);
        driverTools.clickWithJavascriptExecutor(NEXT_BUTTON);
    }

    public void enterNewPassword(String newPassword, String pn, String otp) {
        enterPhoneNumber(pn);
        clickNextButton();
        enterOTPs(otp);
        inputNewPassword(newPassword);
    }

    public void enterOTPs(String otp) {
        driverTools.waitForElementToAppear(otpField(1), 15);
        driverTools.waitForElementToBeClickable(otpField(1), 10).orElseThrow();
        for (int i = 1; i <= otp.length(); i++) {
            driverTools.sendKeys(otpField(i), String.valueOf(otp.charAt(i - 1)));
        }
    }

    public void inputNewPassword(String newPassword) {
        driverTools.waitForElementToAppear(NEW_PASSWORD_TEXTFIELD, 20);
        typeNewPasswordUntilFilled(newPassword);
        driverTools.waitForElementToAppear(CONFIRM_BUTTON);
        driverTools.waitForElementToBeClickable(CONFIRM_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(CONFIRM_BUTTON);
    }

    /**
     * The "Lupa Kata Sandi" modal is still animating right after the OTP step, so a plain
     * {@code sendKeys} intermittently hits an element that is present but not yet interactable, and the
     * xpath can also resolve a leftover hidden input from the previous modal step. Each attempt therefore
     * re-resolves the visible input, types natively, falls back to the JS native-value setter when the
     * field refuses keystrokes, and only returns once the field really holds the password.
     */
    private void typeNewPasswordUntilFilled(String newPassword) {
        for (int attempt = 1; attempt <= NEW_PASSWORD_FILL_ATTEMPTS; attempt++) {
            WebElement passwordField = visibleNewPasswordField();
            if (passwordField == null) {
                logger.log("New password field is not visible yet, attempt " + attempt);
                driverTools.waitForElementToAppear(NEW_PASSWORD_TEXTFIELD, 5);
                continue;
            }
            WebElement typedField = typeNewPasswordOnce(passwordField, newPassword);
            if (typedField != null && isNewPasswordFilled(typedField, newPassword)) {
                return;
            }
            logger.log("New password field is still empty after attempt " + attempt + ", retrying");
        }
        throw new DriverToolsException("Failed to fill: " + NEW_PASSWORD_TEXTFIELD);
    }

    /**
     * Types the password natively, falling back to the JS native-value setter on a freshly resolved field
     * when the input refuses keystrokes.
     *
     * @return the field the password was typed into, or {@code null} when no visible field was left to type into
     */
    private WebElement typeNewPasswordOnce(WebElement passwordField, String newPassword) {
        driverTools.waitForElementToBeClickable(passwordField, 10);
        driverTools.clearInput(passwordField);
        try {
            driverTools.clickWithJavascriptExecutor(passwordField);
            driverTools.sendKeys(passwordField, newPassword);
            return passwordField;
        } catch (InvalidElementStateException | StaleElementReferenceException e) {
            logger.log("Native typing into the new password field failed ("
                    + e.getClass().getSimpleName() + "), falling back to JS value setter");
        }
        WebElement freshField = visibleNewPasswordField();
        if (freshField == null) {
            return null;
        }
        driverTools.focusWithJavascriptExecutor(freshField);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(freshField, newPassword);
        return freshField;
    }

    /**
     * Returns the first displayed and enabled match so a hidden leftover input from an earlier modal step
     * is never typed into, or {@code null} when none is visible.
     */
    private WebElement visibleNewPasswordField() {
        List<WebElement> fields = driverTools.elements(NEW_PASSWORD_TEXTFIELD).orElseGet(List::of);
        for (WebElement field : fields) {
            try {
                if (driverTools.isElementDisplayed(field) && driverTools.isElementEnabled(field)) {
                    return field;
                }
            } catch (StaleElementReferenceException e) {
                logger.log("Skipping stale new password field candidate");
            }
        }
        return null;
    }

    private boolean isNewPasswordFilled(WebElement passwordField, String newPassword) {
        try {
            return newPassword.equals(driverTools.getValueAttribute(passwordField, "value"));
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    public void enterPhoneNumber(String pn) {
        driverTools.waitForElementToAppear(PN_OR_EMAIL_TEXTFIELD).ifPresent(driverTools::click);
        WebElement changeField = driverTools.element(PN_OR_EMAIL_TEXTFIELD).orElseThrow();
        driverTools.clearInput(changeField);
        driverTools.sendKeys(PN_OR_EMAIL_TEXTFIELD, pn);
    }

    public Boolean isPasswordUpdated() {
        Boolean isOnLoginPage = driverTools.isElementDisplayed(PASSWORD_TEXTFIELD);
        return isOnLoginPage;
    }

    public void skipPassKey() {
        driverTools.waitForElementToAppear(PASSKEY_POPUP, 3).ifPresent(e -> {
            logger.log("Passkey popup appeared, skipping it");
            driverTools.clickWithJavascriptExecutor(SKIP_PASSKEY);
            logger.log("Skipped passkey popup");
        });
    }

    public boolean loginIntoSuspendedAccount(String email) throws FailedLoginException, InterruptedException {
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        goToAccountDetailsPagePreprod();
        if (!isAccountHolderNameDisplayed()) {
            return isFailedLoginOfSuspendedAccount(email);
        } else {
            homePage.goToHomePage();
            logoutFromDweb();
            return isFailedLoginOfSuspendedAccount(email);
        }
    }

    public boolean isFailedLoginOfSuspendedAccount(String email) {
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        homePage.goToHomePage();
        homePage.clickOnLoginButton();
        inputEmail(email);
        if (driverTools.waitForElementToPresence(MAYBE_LATER_BUTTON).isPresent()) {
            driverTools.click(MAYBE_LATER_BUTTON);
            return true;
        }
        return false;
    }

    public void goToAccountDetailsPagePreprod() {
        driverTools.get(PREPROD_ACCOUNT_DETAILS_PAGE);
    }

    public void registerWithNewPhoneNumberOrEmail(String phone) {
        logger.log("Registering with new phone number or email: " + phone);
        driverTools.clickWithJavascriptExecutor(EMAIL_OR_PHONE_NUMBER_FIELD);
        driverTools.sendKeys(EMAIL_OR_PHONE_NUMBER_FIELD, phone);
        driverTools.clickWithJavascriptExecutor(REGISTER_BUTTON);
    }

    public void inputDetailsRegisterPage(String fullname, String email, String password, String otp, int retryCounter) throws FailedLoginException, InterruptedException {
        logger.log("fullname==" + fullname + "email ==" + email + "password ==" + password);
        driverTools.waitForElementToAppear(FULLNAME);
        driverTools.sendKeys(FULLNAME, fullname);
        driverTools.waitForElementToAppear(INPUT_EMAIL);
        driverTools.sendKeys(INPUT_EMAIL, email);
        driverTools.waitForElementToAppear(INPUT_PASSWORD);
        driverTools.sendKeys(INPUT_PASSWORD, password);
        driverTools.clickWithJavascriptExecutor(REGISTER_BUTTON_SUBMIT);
        inputOTPCode(otp, retryCounter);
        skipPassKey();
        driverTools.waitForElementToAppear(OK_CONFIRMATION);
        driverTools.clickWithJavascriptExecutor(OK_CONFIRMATION);
        skipPassKey();
    }

    public void clickRegisterNowButton() {
        driverTools.waitForElementToAppear(REGISTER_NOW);
        driverTools.clickWithJavascriptExecutor(REGISTER_NOW);
    }

    public void clickContinueWithPhoneOrEmail() {
        driverTools.waitForElementToAppear(LOGIN_V2, 10).orElseThrow(() -> new java.util.NoSuchElementException(
                "'Continue with Phone or Email' button not shown; url=" + driverTools.getCurrentURL()
                        + ", title=" + driverTools.getTitle()));
        driverTools.clickWhenPresent(LOGIN_V2);
        boolean isContinueWithPhoneOrEmailButtonDisappeared = driverTools.waitForElementToDisappear(LOGIN_V2, 10);
        if (!isContinueWithPhoneOrEmailButtonDisappeared) {
            driverTools.refreshWebpage();
            driverTools.waitForWebToLoad();
            driverTools.waitForElementToAppear(LOGIN_V2, 10).orElseThrow();
            driverTools.clickWithActions(LOGIN_V2);
            isContinueWithPhoneOrEmailButtonDisappeared = driverTools.waitForElementToDisappear(LOGIN_V2, 10);
            if (!isContinueWithPhoneOrEmailButtonDisappeared) {
                driverTools.clickWithJavascriptExecutor(LOGIN_V2);
                if (!driverTools.waitForElementToDisappear(LOGIN_V2, 5)) {
                    openOneFieldPageDirectly();
                }
            }
        }
    }

    /**
     * The account site's "Continue with Phone or Email" button sometimes ignores every kind of click (seen on
     * Jenkins headless Chrome), while the page it leads to is just the login URL with {@code /login} swapped
     * for {@code /onefield}. Opening that URL keeps the login going instead of failing on a dead button.
     */
    private void openOneFieldPageDirectly() {
        String loginUrl = driverTools.getCurrentURL();
        if (loginUrl == null || !loginUrl.contains("/login?")) {
            logger.log("Cannot derive the onefield page from " + loginUrl + "; leaving the login page as is");
            return;
        }
        String oneFieldUrl = loginUrl.replace("/login?", "/onefield?");
        if (!oneFieldUrl.contains("skipPasskey=")) {
            oneFieldUrl += "&skipPasskey=true";
        }
        logger.log("'Continue with Phone or Email' did not react to clicks; opening " + oneFieldUrl);
        driverTools.get(oneFieldUrl);
    }

    public void inputPhoneAndContinue(String phone) {
        driverTools.waitForElementToBeClickable(V2_ONEFIELD, 15).orElseThrow();
        driverTools.clickWithJavascriptExecutor(V2_ONEFIELD);
        driverTools.sendKeys(V2_ONEFIELD, phone);
        driverTools.clickWithJavascriptExecutor(V2_NEXT_BUTTON);
    }

    public void fillAndSubmitRegistration(String fullName, String email, String password) {
        logger.log("Filling registration form: fullName=" + fullName + ", email=" + email);
        driverTools.waitForElementToBeClickable(FULLNAME, 15).orElseThrow();
        driverTools.clickWithJavascriptExecutor(FULLNAME);
        driverTools.sendKeys(FULLNAME, fullName);
        driverTools.clickWithJavascriptExecutor(INPUT_EMAIL);
        driverTools.sendKeys(INPUT_EMAIL, email);
        driverTools.clickWithJavascriptExecutor(INPUT_PASSWORD);
        driverTools.sendKeys(INPUT_PASSWORD, password);
        clickRegisterButtonWithRetry(3);
    }

    private void clickRegisterButtonWithRetry(int maxRetries) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            logger.log("Clicking register submit button (attempt " + attempt + "/" + maxRetries + ")");
            driverTools.clickWithJavascriptExecutor(REGISTER_BUTTON_SUBMIT);
            if (driverTools.waitForElementToAppear(ERROR_SNACKBAR_GENERAL, 3).isPresent()) {
                String errorText = driverTools.getText(ERROR_SNACKBAR_GENERAL).orElse("unknown error");
                logger.log("Error snackbar appeared: [" + errorText + "]");
                if (attempt < maxRetries) {
                    logger.log("Waiting for snackbar to disappear before retry...");
                    driverTools.waitForElementToDisappear(ERROR_SNACKBAR_GENERAL, 5);
                } else {
                    logger.log("Max retries reached for register button. Proceeding...");
                }
            } else {
                logger.log("No error snackbar detected. Register submit successful.");
                return;
            }
        }
    }

    public void completeRegistration(String otp, int retryCounter) throws InterruptedException, FailedLoginException {
        if (driverTools.waitForElementToAppear(otpField(1), 5).isPresent()) {
            inputOTPCode(otp, retryCounter);
        } else if (driverTools.isElementDisplayed(OK_CONFIRMATION)) {
            logger.log("Registration completed without OTP input — OK confirmation visible");
        } else if (driverTools.isElementDisplayed(MAYBE_LATER_BUTTON)) {
            logger.log("Registration completed without OTP input — Maybe Later visible");
        } else {
            inputOTPCode(otp, retryCounter);
        }
        skipPassKey();
        driverTools.waitForElementToAppear(OK_CONFIRMATION, 15).ifPresent(driverTools::clickWithJavascriptExecutor);
        skipPassKey();
        driverTools.waitForElementToDisappear(OK_CONFIRMATION, 5);
    }

    public abstract void goTillLoginPageWithoutLoggingIn(String email) throws FailedLoginException, InterruptedException;

    /**
     * Specific-account login via cookie/session injection: checks if the correct account is
     * already logged in. If logged in with the same email, does nothing. If logged in with a
     * different email, logs out and re-logs in. If not logged in, logs in.
     * <p>On dWeb this is overridden to use cookie injection; the default falls back to UI.
     */
    public void loginWithSpecificAccountViaCookie(String email, String password) throws FailedLoginException, InterruptedException {
        loginWithOneRetry(email, () -> loginIfNotWithSameEmail(email, password, "123456", 1));
    }

    /** One whole login flow, as retried by {@link #loginWithOneRetry}. */
    @FunctionalInterface
    protected interface LoginAttempt {
        void run() throws Exception;
    }

    /**
     * Runs a precondition login, and if it fails backs off {@value #LOGIN_RETRY_WAIT_SECONDS}s, resets the
     * browser's session state and runs it once more. A second failure fails the case with an
     * {@link AssertionError} naming the login, so the framework's {@code RetryAnalyzer} can still re-run the
     * whole case on a fresh session and a login regression stays visible as a failure, not a skip. A skip, an
     * interrupt or a failing session reset is surfaced as-is rather than reported as a failed login.
     */
    protected void loginWithOneRetry(String email, LoginAttempt attempt) {
        for (int attemptNumber = 1; ; attemptNumber++) {
            try {
                attempt.run();
                return;
            } catch (SkipException e) {
                throw e;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Login as " + email + " was interrupted", e);
            } catch (Exception | AssertionError e) {
                if (attemptNumber >= LOGIN_ATTEMPTS) {
                    throw new AssertionError("Login precondition failed for " + email + " after " + LOGIN_ATTEMPTS
                            + " attempts: " + describeFailure(e), e);
                }
                logger.softWarning("Login as " + email + " failed on attempt " + attemptNumber + " ("
                        + describeFailure(e) + "); retrying in " + LOGIN_RETRY_WAIT_SECONDS + "s");
            }
            driverTools.dummyWait(LOGIN_RETRY_WAIT_SECONDS);
            resetSessionBeforeLoginRetry(email);
        }
    }

    private static String describeFailure(Throwable e) {
        return e.getClass().getSimpleName() + (e.getMessage() != null ? ": " + e.getMessage() : "");
    }

    /** Clears whatever the failed login attempt left behind; platforms with more session state override it. */
    protected void resetSessionBeforeLoginRetry(String email) {
        driverTools.deleteAllCookies();
    }

    /**
     * Logs out by navigating to the {@code /en-sg/logout} URL. The server invalidates the
     * session and clears auth cookies via response headers. Also wipes the in-memory UNM
     * token cache so subsequent logins fetch a fresh token.
     * <p>On dWeb this is overridden with environment-aware URL; the default falls back to
     * the existing UI logout.
     */
    public void logoutViaUrl() {
        loginAsGuest();
    }

    public void injectSessionCookiesForApiCreatedOrder(String email, String password)
            throws FailedLoginException, InterruptedException {
        logger.log("[Cookie Login] Lean injection not implemented for platform=" + Configs.PLATFORM
                + "; falling back to the standard login");
        loginWithSpecificAccountViaCookie(email, password);
    }

    /**
     * Deletes known auth cookies on the tiket origin for the current environment.
     * Navigates to the env base URL first so the driver is on a {@code .tiket.com} document
     * (cookie deletion is unreliable on {@code about:blank} or other origins).
     */
    public void clearSessionCookies() {
        driverTools.get(getTiketWebBaseUrl());
        driver.manage().deleteCookieNamed("session_access_token");
        driver.manage().deleteCookieNamed("session_refresh_token");
        driver.manage().deleteCookieNamed("_auto_session_email");
    }

    public String getDeviceId() {
        Cookie deviceCookie = driver.manage().getCookieNamed("device_id");
        String deviceId = (deviceCookie != null) ? deviceCookie.getValue() : null;
        logger.log("Browser device_id cookie -> " + deviceId);
        return deviceId;
    }

    private void handlingErrorSnackbar() {
        if (driverTools.waitForElementToAppear(ERROR_SNACKBAR_B2B, 2).isPresent()) {
            String errorText = driverTools.getText(ERROR_SNACKBAR_B2B).orElseThrow();
            logger.log("Error snack bar appears with message  [" + errorText + "] Clicking again on login");
            driverTools.dummyWait(2);
            driverTools.clickWithJavascriptExecutor(V2_NEXT_BUTTON);
        }
    }

    private Locator getOtpLocator(int index){
        return new Locator("OTP_FIELD_"+index, By.xpath("//div[contains(@class,'OtpInput_otp_group')]//input["+index+"]"));
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
    public void navigateToExternalCorporateDashboard() {
        driverTools.get(CoreFunctions.getCorporateDashboardBaseUrl());
    }


}
