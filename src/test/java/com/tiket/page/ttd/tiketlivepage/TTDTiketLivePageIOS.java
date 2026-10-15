package com.tiket.page.ttd.tiketlivepage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class TTDTiketLivePageIOS <T extends WebDriver> extends TTDTiketLivePage<T> {
    public TTDTiketLivePageIOS(Session<T> session) {
        super(session);
    }
    private static final Locator HEADER_TIKET_LIVE = new Locator("HEADER_TIKET_LIVE", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='tiket Live']"));

    @Override
    public boolean userIsOnTiketLivePage() {
        return driverTools.waitForElementToAppear(HEADER_TIKET_LIVE).isPresent();
    }
}
