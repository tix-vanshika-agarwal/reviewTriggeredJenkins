package com.tiket.page.promo.gift_card_splitting_page;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

import static com.tiket.core.CoreFunctions.*;

public class GiftCardSplittingPageIOS<T extends WebDriver> extends GiftCardSplittingPage<T> {
    public GiftCardSplittingPageIOS(Session<T> session) {
        super(session);
    }
}
