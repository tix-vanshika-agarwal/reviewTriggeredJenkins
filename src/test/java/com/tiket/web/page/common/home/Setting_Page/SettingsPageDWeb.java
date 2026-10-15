package com.tiket.web.page.common.home.Setting_Page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SettingsPageDWeb <T extends WebDriver> extends SettingsPageWeb<T> {
    public SettingsPageDWeb(Session<T> session) {
        super(session);
    }

    private static final Locator NOTIFICATION_SETTING = new Locator("NOTIFICATION_SETTING", new By.ByXPath("//*[contains(@class,'AccordionCustom_container')]//p[text()='Notification Settings' or text()='Pengaturan Notifikasi']"));
    private static final Locator BACK_BUTTON_NOTIFICATION_SETTINGS = new Locator("BACK_BUTTON_NOTIFICATION_SETTINGS", new By.ByXPath("//*[contains(@class,'back_icon')]"));
    private static final Locator ACCOUNT_CENTER_SECTION = new Locator("ACCOUNT_CENTER_SECTION", new By.ByXPath("//*[contains(@class,'SettingsLayout_account_container')]"));

    public void openNotificationSetting() {
        driverTools.waitForElementToPresence(NOTIFICATION_SETTING);
        driverTools.click(NOTIFICATION_SETTING);
    }
    public void clickBackButtonNotificationSettings() {
        driverTools.click(BACK_BUTTON_NOTIFICATION_SETTINGS);
    }
    public boolean isInSettingsPage() {
        return driverTools.waitForElementToPresence(ACCOUNT_CENTER_SECTION).isPresent();
    }
}
