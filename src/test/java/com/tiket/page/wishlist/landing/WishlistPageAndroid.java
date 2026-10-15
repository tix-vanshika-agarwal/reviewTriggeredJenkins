package com.tiket.page.wishlist.landing;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class WishlistPageAndroid<T extends WebDriver> extends WishlistPage<T> {

    private static final Locator SKIP_LOCATOR = new Locator("SKIP_BUTTON", AppiumBy.xpath("//*[contains(@text,'Skip') or contains(@text,'Lewati')]"));

    public WishlistPageAndroid(Session<T> session) {
        super(session);
    }
}
