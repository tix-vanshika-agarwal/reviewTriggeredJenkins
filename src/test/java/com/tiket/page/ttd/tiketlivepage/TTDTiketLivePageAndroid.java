package com.tiket.page.ttd.tiketlivepage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class TTDTiketLivePageAndroid <T extends WebDriver> extends TTDTiketLivePage<T> {
    public TTDTiketLivePageAndroid(Session<T> session) {
        super(session);
    }
    private static final Locator HEADER_TIKET_LIVE = new Locator("HEADER_TIKET_LIVE", AppiumBy.id("tds_main_item_group"));

    @Override
    public boolean userIsOnTiketLivePage() {
       return driverTools.isElementDisplayed(HEADER_TIKET_LIVE);
    }
}
