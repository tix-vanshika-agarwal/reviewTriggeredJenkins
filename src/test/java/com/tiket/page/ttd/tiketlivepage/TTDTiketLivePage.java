package com.tiket.page.ttd.tiketlivepage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class TTDTiketLivePage<T extends WebDriver> extends BasePage<T> {

    public TTDTiketLivePage(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> TTDTiketLivePage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new TTDTiketLivePageAndroid<>(session);}
            case IOS -> {return new TTDTiketLivePageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public static final Locator PLAY_BUTTON_LIVE = new Locator("PLAY_BUTTON_LIVE", AppiumBy.xpath("//*[contains(@src,'ic-play.')]"));

    public void clickPlayButton() {
        driverTools.switchToWebView();
        driverTools.click(PLAY_BUTTON_LIVE);
        driverTools.switchToNative();
    }

    public boolean isPlayButtonDisappear() {
        driverTools.switchToWebView();
        driverTools.waitForElementToDisappear(PLAY_BUTTON_LIVE);
        boolean result = !driverTools.isElementDisplayed(PLAY_BUTTON_LIVE);
        driverTools.switchToNative();
        return result;
    }

    public abstract boolean userIsOnTiketLivePage();
}