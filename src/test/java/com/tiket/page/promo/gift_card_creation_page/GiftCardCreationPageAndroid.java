package com.tiket.page.promo.gift_card_creation_page;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class GiftCardCreationPageAndroid<T extends WebDriver> extends GiftCardCreationPage<T> {
    public GiftCardCreationPageAndroid(Session<T> session) {
        super(session);
    }
}
