package com.tiket.SmartUI.page.ttd;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class TTDSmartPageAndroid<T extends WebDriver> extends TTDSmartPage<T> {
    public static final Locator SEARCH_BAR = new Locator("SEARCH_BAR", AppiumBy.id("container_tap_area_keyword"));

    public static final Locator PRODUCT_PRICE_LANDING_PAGE = new Locator("PRODUCT_PRICE_LANDING_PAGE", AppiumBy.id("price_module_final_price"));


    public TTDSmartPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void scrollToProductCardLandingPage() {
        driverTools.swipeToElement(PRODUCT_PRICE_LANDING_PAGE, Swipe.UP, 30);
    }

    @Override
    public void clickSearchBar() {
        driverTools.click(SEARCH_BAR);
    }

}