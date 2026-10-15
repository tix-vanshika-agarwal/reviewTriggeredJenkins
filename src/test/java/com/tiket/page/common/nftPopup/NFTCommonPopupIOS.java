package com.tiket.page.common.nftPopup;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.common.webview.WebViewPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class NFTCommonPopupIOS<T extends WebDriver> extends NFTCommonPopup<T> {
    public NFTCommonPopupIOS(Session<T> session) {
        super(session);
    }

    public static final Locator COMMON_POPUP = new Locator("COMMON_POPUP", AppiumBy.xpath("//XCUIElementTypeButton[@name='ic cancel grey' and @visible='true']"));

    @Override
    public void clickCloseButton() {
        driverTools.waitForElementToPresence(COMMON_POPUP,5).ifPresent(driverTools::click);
    }
}
