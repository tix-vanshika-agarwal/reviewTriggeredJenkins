package com.tiket.page.ferry.FerrySearchPage;

import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;


public class FerrySearchPageIOS  <T extends WebDriver> extends FerrySearchPage<T>{
    private static final Locator SEARCH_BTN = new Locator("SEARCH_BTN", AppiumBy.xpath("//XCUIElementTypeButton[@name='Search ferry' or @name='Cari ferry']"));
    
    public FerrySearchPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public void openViaDeeplink() {
        Deeplink deepLink = new Deeplink("FERRY_SEARCH_PAGE",
                "tiket://m.gatotkaca.tiket.com/ferry?hideToolbar=true",
                "tiket://m.preprod.tiket.com/ferry?hideToolbar=true",
                "tiket://m.tiket.com/ferry?hideToolbar=true");
        driverTools.deeplink(deepLink);
        driverTools.waitForWebview();
    }

    @Override
    public void skipCoachMark(){
        driverTools.waitForElementToAppear(SEARCH_BTN,5);
        super.skipCoachMark();
    }
}
