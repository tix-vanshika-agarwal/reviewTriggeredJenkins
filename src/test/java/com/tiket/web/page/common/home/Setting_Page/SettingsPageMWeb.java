package com.tiket.web.page.common.home.Setting_Page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;

public class SettingsPageMWeb <T extends WebDriver> extends SettingsPageWeb<T> {
    public SettingsPageMWeb(Session<T> session) {
        super(session);
    }

    private static final Locator NOTIFICATION_SETTING = new Locator("NOTIFICATION_SETTING", new By.ByXPath("//*[@id='__next']//p[contains(text(), 'Notifikasi') or contains(text(), 'Notification')]"));
    private static final Locator BACK_BUTTON_NOTIFICATION_SETTINGS = new Locator("BACK_BUTTON_NOTIFICATION_SETTINGS", new By.ByXPath("//*[contains(@data-testid,'headerBack')]"));
    private static final Locator ACCOUNT_CENTER_SECTION = new Locator("ACCOUNT_CENTER_SECTION", new By.ByXPath("//*[contains(@class,'styles_account_center_card')]"));

    public void openNotificationSetting() {
        try {
            driverTools.waitForElementToPresence(NOTIFICATION_SETTING);
            driverTools.click(NOTIFICATION_SETTING);
        } catch (ElementClickInterceptedException e){
            logger.log("Click intercepted, retrying");
            try {
                driverTools.clickWithJavascriptExecutor(NOTIFICATION_SETTING);
            } catch (Exception ex) {
                logger.log("Retry with JavaScript executor failed: " + ex.getMessage());
                throw ex;
            }
        }
    }
    public void clickBackButtonNotificationSettings() {
        driverTools.click(BACK_BUTTON_NOTIFICATION_SETTINGS);
    }
    public boolean isInSettingsPage() {
        return driverTools.waitForElementToPresence(ACCOUNT_CENTER_SECTION).isPresent();
    }
}
