package com.tiket.web.dashboard.page.common;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.tiket.core.CoreFunctions;

import java.time.Duration;
import java.util.List;

public class CentralizedAdminLoginPage<T extends WebDriver> extends BasePage<T> {

    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", By.id("sb-input-username"));
    private static final Locator PASSWORD_INPUT = new Locator("PASSWORD_INPUT", By.id("sb-input-password"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", By.cssSelector("[data-testid='btn-login']"));
    private static final Locator OTP_HEADING = new Locator("OTP_HEADING", By.xpath("//h2[normalize-space()='Verify Your Email']"));
    private static final Locator OTP_INPUT = new Locator("OTP_INPUT", By.cssSelector("input[data-testid='otp-input']"));
    private static final Locator MENU_ACCORDION = new Locator("MENU_ACCORDION", By.cssSelector(".the-accordion"));
    private static final Locator PROFILE_DROPDOWN = new Locator("PROFILE_DROPDOWN", By.xpath("//div[contains(@class, 'head-profile')]"));
    private static final Locator LOGOUT_OPTION = new Locator("LOGOUT_OPTION", By.xpath("//span[contains(@class, 'popoverlist-list')]//label[normalize-space()='Logout']"));

    private static final Locator FORGOT_PASSWORD_LINK = new Locator("FORGOT_PASSWORD_LINK",
            By.xpath("//p[normalize-space()='Forgot your password?']/parent::a"));
    private static final Locator FORGOT_PASSWORD_EMAIL_INPUT = new Locator("FORGOT_PASSWORD_EMAIL_INPUT", By.id("email"));
    private static final Locator NEXT_BUTTON = new Locator("NEXT_BUTTON",
            By.xpath("//button[normalize-space()='Next']"));
    private static final Locator NEW_PASSWORD_INPUT = new Locator("NEW_PASSWORD_INPUT", By.id("new-password"));
    private static final Locator CONFIRM_NEW_PASSWORD_INPUT = new Locator("CONFIRM_NEW_PASSWORD_INPUT", By.id("confirm-new-password"));
    private static final Locator CONFIRM_BUTTON = new Locator("CONFIRM_BUTTON",
            By.xpath("//button[normalize-space()='Confirm']"));
    private static final Locator SET_PASSWORD_HEADING = new Locator("SET_PASSWORD_HEADING",
            By.xpath("//h2[normalize-space()='Set New Password']"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR",
            By.xpath("//*[contains(@class,'Snackbar') or contains(@class,'snackbar')]//*[contains(text(),'Password successfully updated')]"));

    private static final String DEFAULT_OTP = "1234";
    private static final String SPECIAL_CHARS = "!@#$%&*";
    private static final int LOGIN_PAGE_ATTEMPTS = 3;

    public CentralizedAdminLoginPage(Session<T> session) {
        super(session);
    }

    public void goToExternalLoginPage(String businessType) {
        String loginUrl = getEnvironmentBaseUrl() + "/external/login?businessType=" + businessType;

        // A pooled driver can arrive still carrying an admin session. Opening the login URL
        // while those cookies are present makes the app redirect to "Select your dashboard",
        // and refreshing only reloads that redirected page — the login form never appears.
        // So the session is cleared BEFORE navigating, and the landing is verified afterwards.
        clearAdminSession();

        for (int attempt = 1; attempt <= LOGIN_PAGE_ATTEMPTS; attempt++) {
            driverTools.get(loginUrl);
            driverTools.waitForWebToLoad();

            if (isLoginFormDisplayed()) {
                return;
            }

            logger.log("Centralized admin login page did not open (current URL: "
                    + driverTools.getCurrentURL() + ") — clearing session and retrying, attempt "
                    + attempt + "/" + LOGIN_PAGE_ATTEMPTS);
            clearAdminSession();
        }

        throw new AssertionError("Centralized admin login page did not open after "
                + LOGIN_PAGE_ATTEMPTS + " attempts — current URL: " + driverTools.getCurrentURL());
    }

    /** Logs the admin out and drops every cookie so the next navigation starts as a guest. */
    private void clearAdminSession() {
        logoutViaUrl();
        driverTools.deleteAllCookies();
    }

    /** The login page is confirmed by both its URL and its email field, not by URL alone. */
    private boolean isLoginFormDisplayed() {
        String currentUrl = driverTools.getCurrentURL();
        return currentUrl != null
                && currentUrl.contains("/external/login")
                && driverTools.waitForElementToAppear(EMAIL_INPUT, 10).isPresent();
    }

    private String getEnvironmentBaseUrl() {
        return switch (Configs.ENVIRONMENT) {
            case GK -> "https://gatotkaca.tiket.com";
            case PREPROD -> "https://preprod.tiket.com";
            case PROD -> "https://www.tiket.com";
        };
    }

    public void goToExternalLoginPage() {
        goToExternalLoginPage("other");
    }

    public void inputEmail(String email) {
        driverTools.waitForElementToAppear(EMAIL_INPUT, 10)
                .orElseThrow(() -> new AssertionError("Login page did not load — email input not found"));
        driverTools.sendKeys(EMAIL_INPUT, email);
    }

    public void inputPassword(String password) {
        driverTools.sendKeys(PASSWORD_INPUT, password);
    }

    public void clickLogin() {
        driverTools.click(LOGIN_BUTTON);
    }

    public void handleOtp(String otp) {
        String otpCode = (otp != null) ? otp : DEFAULT_OTP;

        if (driverTools.waitForElementToAppear(OTP_HEADING, 10).isPresent()) {
            driverTools.waitForElementToAppear(OTP_INPUT, 5)
                    .orElseThrow(() -> new AssertionError("OTP inputs not found after OTP heading appeared"));
            List<WebElement> otpInputs = driver.findElements(OTP_INPUT.by());
            for (int i = 0; i < otpInputs.size() && i < otpCode.length(); i++) {
                otpInputs.get(i).sendKeys(String.valueOf(otpCode.charAt(i)));
            }
            driverTools.waitForWebToLoad();
        } else {
            logger.log("OTP page not shown — proceeding without OTP entry");
        }
    }

    public void login(String email, String password) {
        login(email, password, DEFAULT_OTP);
    }

    public void login(String email, String password, String otp) {
        inputEmail(email);
        inputPassword(password);
        clickLogin();
        handleOtp(otp);

        driverTools.waitForWebToLoad();
    }

    public boolean isUserLoggedIn() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.not(ExpectedConditions.urlContains("/external/login")));
            return true;
        } catch (Exception e) {
            logger.log("User is NOT logged in — still on login page after timeout");
            return false;
        }
    }

    public boolean isDashboardLoaded() {
        return driverTools.waitForElementToAppear(MENU_ACCORDION, 15).isPresent();
    }

    public void logout() {
        driverTools.waitForElementToAppear(PROFILE_DROPDOWN, 5);
        driverTools.click(PROFILE_DROPDOWN);
        driverTools.waitForElementToAppear(LOGOUT_OPTION, 5);
        driverTools.click(LOGOUT_OPTION);
    }

    public void clickForgotPassword() {
        driverTools.waitForElementToAppear(FORGOT_PASSWORD_LINK, 10)
                .orElseThrow(() -> new AssertionError("Forgot password link not found"));
        driverTools.click(FORGOT_PASSWORD_LINK);
        driverTools.waitForWebToLoad();
    }

    public void inputForgotPasswordEmail(String email) {
        driverTools.waitForElementToAppear(FORGOT_PASSWORD_EMAIL_INPUT, 10)
                .orElseThrow(() -> new AssertionError("Forgot password email input not found"));
        driverTools.sendKeys(FORGOT_PASSWORD_EMAIL_INPUT, email);
    }

    public void clickNext() {
        driverTools.waitForElementToAppear(NEXT_BUTTON, 10)
                .orElseThrow(() -> new AssertionError("Next button not found or not visible"));
        driverTools.click(NEXT_BUTTON);
        driverTools.waitForWebToLoad();
    }

    public void handleForgotPasswordOtp(String otp) {
        String otpCode = (otp != null) ? otp : DEFAULT_OTP;

        driverTools.waitForElementToAppear(OTP_HEADING, 15)
                .orElseThrow(() -> new AssertionError("OTP verification page not shown"));
        driverTools.waitForElementToAppear(OTP_INPUT, 5)
                .orElseThrow(() -> new AssertionError("OTP inputs not found"));
        List<WebElement> otpInputs = driver.findElements(OTP_INPUT.by());
        for (int i = 0; i < otpInputs.size() && i < otpCode.length(); i++) {
            otpInputs.get(i).sendKeys(String.valueOf(otpCode.charAt(i)));
        }
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.or(
                        ExpectedConditions.visibilityOfElementLocated(SET_PASSWORD_HEADING.by()),
                        ExpectedConditions.not(ExpectedConditions.urlContains("/otp"))
                ));
    }

    public boolean isSetNewPasswordPageDisplayed() {
        return driverTools.waitForElementToAppear(SET_PASSWORD_HEADING, 10).isPresent();
    }

    public void inputNewPassword(String password) {
        driverTools.waitForElementToAppear(NEW_PASSWORD_INPUT, 10)
                .orElseThrow(() -> new AssertionError("New password input not found"));
        driverTools.sendKeys(NEW_PASSWORD_INPUT, password);
    }

    public void inputConfirmNewPassword(String password) {
        driverTools.waitForElementToAppear(CONFIRM_NEW_PASSWORD_INPUT, 10)
                .orElseThrow(() -> new AssertionError("Confirm new password input not found"));
        driverTools.sendKeys(CONFIRM_NEW_PASSWORD_INPUT, password);
    }

    public void clickConfirm() {
        driverTools.waitForElementToAppear(CONFIRM_BUTTON, 10)
                .orElseThrow(() -> new AssertionError("Confirm button not found"));
        driverTools.click(CONFIRM_BUTTON);
        driverTools.waitForWebToLoad();
    }

    public boolean isSuccessSnackbarDisplayed() {
        return driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 5).isPresent();
    }

    public boolean isLoginPageDisplayedWithSnackbar() {
        boolean snackbarVisible = driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 10).isPresent();
        boolean onLoginPage;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.urlContains("/external/login"));
            onLoginPage = true;
        } catch (Exception e) {
            onLoginPage = false;
        }
        return snackbarVisible && onLoginPage;
    }

    public boolean isLoginPageDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.urlContains("/external/login"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOnLoginPage() {
        String currentUrl;
        try {
            currentUrl = driver.getCurrentUrl();
        } catch (Exception e) {
            currentUrl = null;
        }

        if (currentUrl != null && currentUrl.startsWith("http")) {
            // A real page is loaded — the login page is identified by its URL path.
            return currentUrl.contains("/external/login");
        }

        // No real page yet (e.g. "data:," / "about:blank" on a fresh driver) — confirm via the login form.
        return driverTools.waitForElementToAppear(LOGIN_BUTTON, 3).isPresent();
    }

    public void logoutViaUrl() {
        driverTools.get(getEnvironmentBaseUrl() + "/admin/member/logout");
        driverTools.waitForWebToLoad();
    }

    /**
     * Generates a password satisfying: min 10 chars, at least one uppercase,
     * one lowercase, one digit, and one special character.
     */
    public static String generateDynamicPassword() {
        String lowerPart = CoreFunctions.generateRandomString(5).toLowerCase();
        String upperPart = CoreFunctions.generateRandomString(2).toUpperCase();
        long digits = CoreFunctions.generateRandomInt(2);
        char special = SPECIAL_CHARS.charAt((int) (Math.random() * SPECIAL_CHARS.length()));
        return upperPart + lowerPart + digits + special;
    }
}
