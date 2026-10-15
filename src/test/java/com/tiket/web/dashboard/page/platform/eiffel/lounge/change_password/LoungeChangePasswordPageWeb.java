package com.tiket.web.dashboard.page.platform.eiffel.lounge.change_password;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoungeChangePasswordPageWeb<T extends WebDriver> extends WebPage<T> {
    public LoungeChangePasswordPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> LoungeChangePasswordPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new LoungeChangePasswordPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new LoungeChangePasswordPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator CURRENT_PASSWORD_INPUT = new Locator("CURRENT_PASSWORD_INPUT", By.id("current-password"));
    private static final Locator NEW_PASSWORD_INPUT = new Locator("NEW_PASSWORD_INPUT", By.id("new-password"));
    private static final Locator CONFIRMATION_PASSWORD_INPUT = new Locator("CONFIRMATION_PASSWORD_INPUT", By.id("confirm-new-password"));
    private static final Locator CHANGE_BUTTON = new Locator("CHANGE_BUTTON", By.xpath("//button[normalize-space()='Change']"));

    public void inputCurrentPassword(String input) {
        inputPassword(CURRENT_PASSWORD_INPUT, input);
    }

    public void inputNewPassword(String input) {
        inputPassword(NEW_PASSWORD_INPUT, input);
    }

    public void inputConfirmPassword(String input) {
        inputPassword(CONFIRMATION_PASSWORD_INPUT, input);
    }

    public void saveChangePassword() {
        driverTools.click(CHANGE_BUTTON);
        driverTools.dummyWait(3);
        driverTools.waitForWebToLoad();
    }

    public boolean isSuccessfullyChangePassword() {
        return !driverTools.getCurrentURL().contains("change-password");
    }

    private void inputPassword(Locator loc, String input) {
        driverTools.click(loc);
        driverTools.sendKeys(loc, input);
    }
}
