package com.tiket.web.dashboard.page.platform.eiffel.lounge.reset_password;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Lounge password reset (V2, QAAUT-44146). The reset moved to the centralized, OTP-based member flow
 * (external/forget-password) — the old reward.tiket.com/lounge/reset-password page is now a 404 — so it is
 * reached via the login page's "Forgot your password?" link, keeping the URL correct per environment.
 */
public class ResetPasswordPageWeb<T extends WebDriver> extends WebPage<T> {
    public ResetPasswordPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> ResetPasswordPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new ResetPasswordPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new ResetPasswordPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator FORGOT_PASSWORD_LINK = new Locator("FORGOT_PASSWORD_LINK", By.cssSelector("a[href*='/external/forget-password']"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", By.id("email"));
    private static final Locator NEXT_BUTTON = new Locator("NEXT_BUTTON", By.xpath("//button[normalize-space()='Next']"));
    private static final Locator OTP_INPUT = new Locator("OTP_INPUT", By.cssSelector("[data-testid='otp-input']"));
    private static final Locator NEW_PASSWORD_INPUT = new Locator("NEW_PASSWORD_INPUT", By.id("new-password"));
    private static final Locator CONFIRM_PASSWORD_INPUT = new Locator("CONFIRM_PASSWORD_INPUT", By.id("confirm-new-password"));
    private static final Locator CONFIRM_BUTTON = new Locator("CONFIRM_BUTTON", By.xpath("//button[normalize-space()='Confirm']"));

    public void goToForgetPasswordPage() {
        String url = switch (Configs.ENVIRONMENT) {
            case GK -> "https://reward-gatotkaca.tiket.com/";
            case PREPROD -> "https://reward.preprod.tiket.com/";
            case PROD -> "https://reward.tiket.com/";
            default -> throw new IllegalStateException("Unsupported environment: " + Configs.ENVIRONMENT);
        };
        driver.manage().deleteAllCookies();
        driverTools.get(url);
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToAppear(FORGOT_PASSWORD_LINK, 15);
        driverTools.clickElementByCoordinateDWeb(FORGOT_PASSWORD_LINK);
    }

    public void requestResetOtp(String email) {
        driverTools.waitForElementToAppear(EMAIL_INPUT, 10);
        driverTools.clearInput(EMAIL_INPUT);
        driverTools.sendKeys(EMAIL_INPUT, email);
        driverTools.click(NEXT_BUTTON);
    }

    public void submitOtp(String otp) {
        driverTools.waitForElementToAppear(OTP_INPUT, 10);
        List<WebElement> otpInputs = driver.findElements(OTP_INPUT.by());
        for (int i = 0; i < otpInputs.size() && i < otp.length(); i++) {
            otpInputs.get(i).sendKeys(String.valueOf(otp.charAt(i)));
        }
        driverTools.waitForWebToLoad();
    }

    public void inputPassword(String password) {
        driverTools.waitForElementToAppear(NEW_PASSWORD_INPUT, 10);
        driverTools.clearInput(NEW_PASSWORD_INPUT);
        driverTools.sendKeys(NEW_PASSWORD_INPUT, password);
        driverTools.clearInput(CONFIRM_PASSWORD_INPUT);
        driverTools.sendKeys(CONFIRM_PASSWORD_INPUT, password);
    }

    public void save() {
        driverTools.click(CONFIRM_BUTTON);
        driverTools.waitForWebToLoad();
    }
}
