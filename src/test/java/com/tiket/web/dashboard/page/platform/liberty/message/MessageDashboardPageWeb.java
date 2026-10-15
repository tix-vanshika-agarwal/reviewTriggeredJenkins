package com.tiket.web.dashboard.page.platform.liberty.message;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;

public class MessageDashboardPageWeb<T extends WebDriver> extends WebPage<T> {
    public MessageDashboardPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> MessageDashboardPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new MessageDashboardPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new MessageDashboardPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator PROFILE_MENU_DROPDOWN = new Locator("PROFILE_MENU_DROPDOWN", By.cssSelector("[data-testid='toggle_account_btn']"));
    private static final Locator LOGOUT_BUTTON = new Locator("LOGOUT_BUTTON", By.cssSelector("div.account_detail a"));

    public void logout() {
        driverTools.clickWithSyntheticEvents(PROFILE_MENU_DROPDOWN);
        if (driverTools.waitForElementToAppear(LOGOUT_BUTTON, 3).isEmpty()) {
            driverTools.clickWithActions(PROFILE_MENU_DROPDOWN);
        }
        if (driverTools.waitForElementToAppear(LOGOUT_BUTTON, 5).isEmpty()) {
            String origin = driver.getCurrentUrl().replaceAll("(https?://[^/]+).*", "$1");
            driver.get(origin + "/admin/member/logout?ref=%2Fadmin%2Fmember%2Flogin");
            return;
        }
        driverTools.clickWithJavascriptExecutor(LOGOUT_BUTTON);
    }
}
