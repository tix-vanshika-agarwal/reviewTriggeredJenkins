package com.tiket.page.promo.gift_card_landing_page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class GiftCardPageAndroid<T extends WebDriver> extends GiftCardPage<T> {
    private static final Locator REDEEM_GIFT_CARD_BUTTON = new Locator("REDEEM_GIFT_CARD_BUTTON", AppiumBy.xpath("//*[@text='Tukar gift card' or @text='Redeem gift card']"));
    private static final Locator SEND_GIFT_CARD_BUTTON = new Locator("SEND_GIFT_CARD_BUTTON", AppiumBy.xpath("//*[@text='Kirim gift card' or @text='Send gift card']"));


    public GiftCardPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void clickRedeemGiftCard() {
        driverTools.click(REDEEM_GIFT_CARD_BUTTON);
    }

    @Override
    public void clickSendGiftCard() {
        driverTools.waitForElementToPresence(SEND_GIFT_CARD_BUTTON);
        driverTools.click(SEND_GIFT_CARD_BUTTON);
    }
}
