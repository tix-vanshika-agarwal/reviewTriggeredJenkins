package com.tiket.page.common.nftPopup;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.common.webview.WebViewPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class NFTCommonPopupAndroid<T extends WebDriver> extends NFTCommonPopup<T> {
    public NFTCommonPopupAndroid(Session<T> session) {
        super(session);
    }

    public static final Locator COMMON_POPUP = new Locator("COMMON_POPUP", AppiumBy.id("iv_close"));

    @Override
    public void clickCloseButton() {
        driverTools.waitForWebview();
        driverTools.switchToNative();
        driverTools.waitForElementToPresence(COMMON_POPUP,5).ifPresent(driverTools::click);
    }
}
