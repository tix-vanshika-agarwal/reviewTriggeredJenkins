package com.tiket.SmartUI.page.order;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class OrderSmartPageIOS<T extends WebDriver> extends OrderSmartPage<T> {
    public static final Locator ADJUST_FILTER_BUTTON = new Locator("ADJUST_FILTER_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Adjust Filter') or contains(@name,'Ubah Filter')]"));

    public OrderSmartPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public void waitForFilterToBeApplied() {
        driverTools.waitForElementToAppear(ADJUST_FILTER_BUTTON);
    }

}