package com.tiket.page.wishlist.landing;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class WishlistPageIOS<T extends WebDriver> extends WishlistPage<T> {

    private static final Locator SKIP_LOCATOR = new Locator("SKIP_BUTTON", AppiumBy.xpath("//XCUIElementTypeStaticText[@name=\"skip_button\"]"));

    public WishlistPageIOS(Session<T> session) {
        super(session);
    }
}
