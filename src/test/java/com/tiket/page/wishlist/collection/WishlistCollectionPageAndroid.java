package com.tiket.page.wishlist.collection;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class WishlistCollectionPageAndroid<T extends WebDriver> extends WishlistCollectionPage<T> {
    public WishlistCollectionPageAndroid(Session<T> session) {
        super(session);
    }
}
