package com.tiket.page.order.trip.switcher;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class SwitcherPageAndroid <T extends WebDriver> extends SwitcherPage<T> {
    public SwitcherPageAndroid(Session<T> session) {
        super(session);
    }

    public static final Locator TRIP_VIEW = new Locator("TRIP_VIEW", AppiumBy.xpath("//*[contains(@resource-id,'view_trip_view_host')]"));
    public static final Locator ORDER_LIST_VIEW = new Locator("ORDER_LIST_VIEW", AppiumBy.xpath("//*[contains(@resource-id,'active_order_container')]"));

    @Override
    public boolean switchToTrip() {
        final Locator ITENERARY_TAB = new Locator("ITENERARY_TAB", AppiumBy.xpath("(//*[contains(@resource-id,'tds_segmented_control_button_title')])[1]"));
        driverTools.click(ITENERARY_TAB);
        driverTools.waitForElementToAppear(TRIP_VIEW).orElseThrow();
        return true;
    }

    @Override
    public boolean switchToOrder() {
        final Locator ORDER_LIST_TAB = new Locator("ORDER_LIST_TAB", AppiumBy.xpath("(//*[contains(@resource-id,'tds_segmented_control_button_title')])[2]"));
        driverTools.click(ORDER_LIST_TAB);
        driverTools.waitForElementToAppear(ORDER_LIST_VIEW).orElseThrow();
        return true;
    }
}
