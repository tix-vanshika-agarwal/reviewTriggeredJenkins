package com.tiket.page.promo.gift_card_detail_page;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

import static com.tiket.core.CoreFunctions.*;

public class GiftCardDetailPageIOS<T extends WebDriver> extends GiftCardDetailPage<T> {
    public GiftCardDetailPageIOS(Session<T> session) {
        super(session);
    }

    private static final Locator NAME_INPUT = new Locator("NAME_INPUT", AppiumBy.xpath("//XCUIElementTypeTextField[@value='Full Name' or @value='Nama Lengkap']"));
    private static final Locator PHONE_INPUT = new Locator("PHONE_INPUT", AppiumBy.xpath("//XCUIElementTypeTextField[@value='Phone Number' or @value='Nomor HP']"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", AppiumBy.xpath("//XCUIElementTypeTextField[@value='Email Address' or @value='Alamat Email']"));
    private static final Locator SENDER_INPUT = new Locator("SENDER_INPUT", AppiumBy.xpath("//XCUIElementTypeTextField[@value='Sender Name' or @value='Nama Pengirim']"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name, 'Continue') or contains(@name, 'Lanjut')]"));

    @Override
    public void fillInGuestDetail() {
        driverTools.waitForElementToPresence(NAME_INPUT);
        driverTools.sendKeys(NAME_INPUT, randomName());
        driverTools.sendKeys(PHONE_INPUT, randomPhone());
        driverTools.sendKeys(EMAIL_INPUT, randomEmail());
        driverTools.hideKeyboard();
    }

    @Override
    public void clickContinueToPayment() {
        driverTools.click(CONTINUE_BUTTON);
    }

    @Override
    public void inputSendeName() {
        driverTools.swipeToElement(SENDER_INPUT, Swipe.UP, 3);
        driverTools.sendKeys(SENDER_INPUT, randomName());
        driverTools.hideKeyboard();
    }

}
