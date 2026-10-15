package com.tiket.web.smart.Hotel;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class HotelSmartPageWeb<T extends WebDriver> extends WebPage<T> {
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", new By.ByXPath("//*[contains(@class, 'MobileBackButton')]"));
    private static final Locator WEBVIEW_BANNER = new Locator("WEBVIEW_BANNER", new By.ByXPath("//*[contains(@class, 'Banner')]"));

    public HotelSmartPageWeb(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> HotelSmartPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new HotelSmartPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new HotelSmartPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public void goBackToLandingPage() {
        driverTools.waitForElementToAppear(BACK_BUTTON);
        driverTools.clickWithJavascriptExecutor(BACK_BUTTON);
    }

    public void waitForWebviewToBeLoaded(){
        driverTools.waitForElementToAppear(WEBVIEW_BANNER);
    }
}
