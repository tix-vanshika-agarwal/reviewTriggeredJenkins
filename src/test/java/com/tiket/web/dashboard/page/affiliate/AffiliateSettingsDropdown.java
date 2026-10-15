package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AffiliateSettingsDropdown<T extends WebDriver> extends BasePage<T> {
    public AffiliateSettingsDropdown(Session<T> session) {
        super(session);
    }

    private static final Locator SettingsDropdown = new Locator("SettingsDropdown", By.xpath("//span[text()='Settings']"));
    private static final Locator CHANGE_PASSWORD_OPTION = new Locator("CHANGE_PASSWORD_OPTION", By.xpath("//span[text()='Change Password']")
    );

    public void clickSettingsDropdown() {
        driverTools.waitForElementToAppear(SettingsDropdown, 1);
        driverTools.click(SettingsDropdown);
    }

    public boolean isSettingDropdown(){
        return driverTools.isElementDisplayed(CHANGE_PASSWORD_OPTION);
    }

}