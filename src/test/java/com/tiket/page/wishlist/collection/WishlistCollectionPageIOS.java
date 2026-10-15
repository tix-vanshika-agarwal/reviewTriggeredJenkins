package com.tiket.page.wishlist.collection;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class WishlistCollectionPageIOS<T extends WebDriver> extends WishlistCollectionPage<T> {
    public WishlistCollectionPageIOS(Session<T> session) {
        super(session);
    }
}
