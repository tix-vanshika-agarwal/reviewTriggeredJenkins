package com.tiket.page.order.trip.switcher;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.order.orderlist.OrderListPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class SwitcherPageIOS  <T extends WebDriver> extends SwitcherPage<T> {
    public SwitcherPageIOS(Session<T> session) {
        super(session);
    }

    public static final Locator TRIP_VIEW = new Locator("TRIP_VIEW", AppiumBy.xpath("//*[contains(@name,'trip_card')]"));

    @Override
    public boolean switchToTrip() {
        final Locator ITENERARY_TAB = new Locator("ITENERARY_TAB", AppiumBy.name("itinerary_view"));
        driverTools.click(ITENERARY_TAB);
        driverTools.waitForElementToAppear(TRIP_VIEW).orElseThrow();
        return true;
    }

    @Override
    public boolean switchToOrder() {
        final Locator ORDER_LIST_TAB = new Locator("ORDER_LIST_TAB", AppiumBy.name("list_view"));
        final var WAITING_PAYMENT_BUTTON = new Locator("WAITING_PAYMENT_BUTTON", AppiumBy.name("waiting_payment_btn"));
        driverTools.click(ORDER_LIST_TAB);
        final Locator ORDER_CARD = new Locator("ORDER_CARD", AppiumBy.name("order_card"));
        return driverTools.isElementPresent(WAITING_PAYMENT_BUTTON) || driverTools.waitForElementToAppear(ORDER_CARD).isPresent();
    }
}
