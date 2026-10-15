package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AffiliateCommonPageWeb<T extends WebDriver> extends WebPage<T> {
    public AffiliateCommonPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> AffiliateCommonPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new AffiliateCommonPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new AffiliateCommonPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator ACCOUNT_MENU_BUTTON = new Locator("ACCOUNT_MENU_BUTTON", By.xpath("//div[contains(@class, 'AccountCard_account')]/*[3]"));
    private static final Locator LOGOUT_BUTTON = new Locator("LOGOUT_BUTTON", By.xpath("//li[text()='Logout']"));

    public void logout() {
        driverTools.clickElementByCoordinateDWeb(ACCOUNT_MENU_BUTTON);
        driverTools.clickElementByCoordinateDWeb(LOGOUT_BUTTON);
    }
}
