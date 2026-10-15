package com.tiket.page.bus.search;

import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class BusSearchPageAndroid  <T extends WebDriver> extends BusSearchPage<T>{
    public static final Locator BUS_AND_SHUTTLE_TITLE = new Locator("BUS_AND_SHUTTLE_TITLE", AppiumBy.xpath("//android.view.View[@text='Bus & Travel']"));
    public static final Locator PASSENGER_PICKER_WHEEL_SCROLLABLE = new Locator("PASSENGER_PICKER_WHEEL_SCROLLABLE", AppiumBy.xpath("//*[contains(@class,'WheelPicker_slider_wrapper')]"));

    public BusSearchPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void openBusSearchViaDeepLink() {
        Deeplink deepLink = new Deeplink("BUS_SEARCH_PAGE",
                "tiket://m.gatotkaca.tiket.com/bus-travel",
                "tiket://m.preprod.tiket.com/bus-travel",
                "tiket://m.tiket.com/bus-travel");
        driverTools.deeplink(deepLink);
    }

    @Override
    public void skipCoachMark(){
        driverTools.waitForElementToAppear(BUS_AND_SHUTTLE_TITLE,10);
        super.skipCoachMark();
    }
}
