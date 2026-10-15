package com.tiket.SmartUI.page.order;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class OrderSmartPageAndroid<T extends WebDriver> extends OrderSmartPage<T> {
    public static final Locator ADJUST_FILTER_BUTTON = new Locator("ADJUST_FILTER_BUTTON", AppiumBy.xpath("//*[@resource-id='com.tiket.gits:id/tds_button_view' and (@text='Adjust Filter' or @text='Ubah Filter')]"));

    public OrderSmartPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void waitForFilterToBeApplied() {
        driverTools.waitForElementToAppear(ADJUST_FILTER_BUTTON);
    }
}