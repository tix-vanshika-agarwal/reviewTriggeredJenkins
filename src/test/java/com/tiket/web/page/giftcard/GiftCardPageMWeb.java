package com.tiket.web.page.giftcard;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.home.HomePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GiftCardPageMWeb<T extends WebDriver> extends GiftCardPageWeb<T> {
    public GiftCardPageMWeb(Session<T> session) {
        super(session);
    }
    private static final Locator REDEEM_SCRATCH_TEXT = new Locator("REDEEM_SCRATCH_TEXT", new By.ByXPath("//span[contains(@class,'RedeemGiftCardFigure_scratch_text')]"));
    private static final Locator REDEEM_CANVAS = new Locator("REDEEM_CANVAS", new By.ByXPath("//div[contains(@class,'RedeemGiftCardFigure_scratch')]"));
    private static final Locator RECIPIENT_BUTTON = new Locator("RECIPIENT_BUTTON", new By.ByXPath("//div[contains(@class,'Recipients_actions')]"));
    private static final Locator EDIT_RECIPIENT_BUTTON = new Locator("EDIT_RECIPIENT_BUTTON", AppiumBy.xpath("//div[contains(@class,'Card_card_container')]//span[text()='Recipient 1']/ancestor::div[contains(@class,'Recipients_card_body')]//div[contains(@class,'Recipients_actions')]"));
    private static final Locator REDEEM_CLAIM_BUTTON = new Locator("REDEEM_CLAIM_BUTTON", new By.ByXPath("//button[contains(@class,'OnboardingRedeemGiftCard_claim_button')]"));
    public static final Locator EDIT_RECIPIENT = new Locator("EDIT_RECIPIENT", AppiumBy.xpath("//*[@type='XCUIElementTypeImage' and ./preceding-sibling::*[@type='XCUIElementTypeOther']]"));


    public static Locator getVoucherSelection(int index) {
        return new Locator("VOUCHER_SELECTION", new By.ByXPath("(//input[contains(@class, 'SplitVoucherForm_radio')])["+index+"]"));
    }
    private static final Locator SELECTED_CARD_FIGURE = new Locator("SELECTED_CARD_FIGURE", new By.ByXPath("//figure[contains(@class,'GiftCardFigure_figure')]"));
    private static Locator getSpecificCardItem(int index) {
        return new Locator("SPECIFIC_CARD_ITEM", new By.ByXPath("(//div[contains(@class,'GiftCardCarousel_carousel_item')])" + "[" + index + "]"));
    }

    @Override
    public void maximize() {
        // no need for Mweb
    }

    @Override
    public void clickRedeemClaimGiftCard() {
        driverTools.waitForElementToAppear(REDEEM_SCRATCH_TEXT, 10);
        WebElement element = driverTools.element(REDEEM_CANVAS).get();
        driverTools.scrollIntoWebView(element);
        driverTools.scratchWithActions(element);
        driverTools.waitForElementToDisappear(element);
        driverTools.waitForElementToBeClickable(REDEEM_CLAIM_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(REDEEM_CLAIM_BUTTON);
    }

    @Override
    public void selectVoucherOptions() {
        driverTools.waitForElementToAppear(getVoucherSelection(1));
        driverTools.clickWithJavascriptExecutor(getVoucherSelection(1));
    }

    protected void clickSpecificCardItem(int index) {
        int maxAttempts = 5;
        int attempts = 0;
        while (!driverTools.isElementDisplayed(getSpecificCardItem(index))) {
            driverTools.swipeRight(getSpecificCardItem(3*(attempts+1))); // so it will swipe right from the 3rd-6th-9th card
            attempts++;
            if (attempts >= maxAttempts) {
                throw new NotFoundException("Max attempts reached while searching for card item index " + index);
            }
        }
        driverTools.waitForElementToPresence(getSpecificCardItem(index));
        driverTools.click(getSpecificCardItem(index));
        driverTools.waitForElementToAppear(SELECTED_CARD_FIGURE);
    }


    public void clickAddRecipient(){
        if (Configs.PLATFORM == Platform.IMWEB) {
            driverTools.switchToNative();
            driverTools.swipeToElement(EDIT_RECIPIENT, Swipe.UP, 5);
            driverTools.clickElementByCoordinate(EDIT_RECIPIENT);
            driverTools.waitForWebview();
            driverTools.switchToWebView();
        }
        else {
            driverTools.scrollIntoWebView(EDIT_RECIPIENT_BUTTON);
            driverTools.scrollIntoWebView(RECIPIENT_BUTTON);
            driverTools.waitForElementToBeClickable(RECIPIENT_BUTTON);
            driverTools.clickElementByCoordinateDWeb(RECIPIENT_BUTTON);
        }
    }

}
