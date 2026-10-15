package com.tiket.page.promo.gift_card_landing_page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class GiftCardPageIOS<T extends WebDriver> extends GiftCardPage<T> {
    private static final Locator REDEEM_GIFT_CARD_BUTTON = new Locator("REDEEM_GIFT_CARD_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Tukar gift') or contains(@name,'Redeem gift') or contains(@label,'Tukar gift') or contains(@label,'Redeem gift')]"));
    private static final Locator SEND_GIFT_CARD_BUTTON = new Locator("SEND_GIFT_CARD_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Kirim gift') or contains(@name,'Send gift') or contains(@label,'Kirim gift') or contains(@label,'Send gift')]"));
    private static final Locator GIFT_CODE_INPUT = new Locator("GIFT_CODE_INPUT", AppiumBy.xpath("//XCUIElementTypeTextField[@name='Enter the redeem code' or @name='Masukin kode penukaran' or @value='Enter the redeem code' or @value='Masukin kode penukaran']"));
    private static final Locator ALREADY_REDEEMED_LABEL = new Locator("ALREADY_REDEEMED_LABEL", AppiumBy.xpath("//*[contains(@name, 'has been redeemed') or contains(@name, 'udah ditukar')]"));

    public GiftCardPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public void clickRedeemGiftCard() {
        driverTools.waitForElementToAppear(REDEEM_GIFT_CARD_BUTTON).ifPresent(driverTools::clickElementByCoordinate);
    }

    @Override
    public void clickSendGiftCard() {
        clickLandingButtonByCoordinate(SEND_GIFT_CARD_BUTTON);
    }

    private void clickLandingButtonByCoordinate(Locator landingButton) {
        driverTools.switchToNative();
        driverTools.waitForElementToPresence(landingButton).orElseThrow();
        for (int attempt = 1; attempt <= 3; attempt++) {
            driverTools.waitForElementToBeClickable(landingButton, 10);
            driverTools.clickElementByCoordinate(landingButton);
            if (driverTools.waitForElementToDisappear(landingButton, 5)) {
                return;
            }
            logger.log("Coordinate click on " + landingButton + " did not navigate (attempt " + attempt + "/3); retrying.");
        }
        throw new RuntimeException("Gift card landing button click did not trigger navigation: " + landingButton);
    }

    @Override
    public void inputGiftCode(String giftCode) {
        if (driverTools.waitForElementToPresence(GIFT_CODE_INPUT, 8).isPresent()) {
            driverTools.sendKeys(GIFT_CODE_INPUT, giftCode);
            driverTools.hideKeyboard();
            return;
        }
        super.inputGiftCode(giftCode);
    }

    @Override
    public boolean codeRedeemSuccessful() {
        return !driverTools.isElementDisplayed(ALREADY_REDEEMED_LABEL) && super.successfulRedeemPageAppears();
    }
}
