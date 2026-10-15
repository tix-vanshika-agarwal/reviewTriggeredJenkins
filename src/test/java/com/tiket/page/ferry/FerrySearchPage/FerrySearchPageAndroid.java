package com.tiket.page.ferry.FerrySearchPage;

import com.tiket.model.Deeplink;
import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;


public class FerrySearchPageAndroid  <T extends WebDriver> extends FerrySearchPage<T>{

    public FerrySearchPageAndroid(Session<T> session) {
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
    
}
