package com.tiket.web.page.giftcard;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GiftCardPageDWeb<T extends WebDriver> extends GiftCardPageWeb<T> {
    public GiftCardPageDWeb(Session<T> session) {
        super(session);
    }
    private static final Locator REDEEM_SCRATCH_TEXT = new Locator("REDEEM_SCRATCH_TEXT", new By.ByXPath("//span[contains(@class,'RedeemGiftCardFigure_scratch_text')]"));
    private static final Locator REDEEM_CANVAS = new Locator("REDEEM_CANVAS", new By.ByXPath("//div[contains(@class,'RedeemGiftCardFigure_scratch')]"));
    private static final Locator REDEEM_CLAIM_BUTTON = new Locator("REDEEM_CLAIM_BUTTON", new By.ByXPath("//button[contains(@class,'OnboardingRedeemGiftCard_claim_button')]"));
    private static final Locator RECIPIENT_BUTTON = new Locator("RECIPIENT_BUTTON", new By.ByXPath("//div[contains(@class,'Recipients_actions')]"));

    private static final Locator CARD_NEXT_CAROUSEL = new Locator("CARD_NEXT_CAROUSEL", new By.ByXPath("//div[contains(@class,'GiftCardCarousel')]//button[@aria-label='Next']"));
    private static final Locator SELECTED_CARD_FIGURE = new Locator("SELECTED_CARD_FIGURE", new By.ByXPath("//figure[contains(@class,'GiftCardFigure_figure')]"));
    public static Locator getVoucherSelection(int index) {
        return new Locator("VOUCHER_SELECTION", new By.ByXPath(
            "(//div[contains(@class,'SplitVoucherForm_radio_container')])[" + index + "]"));
    }
    private static Locator getSpecificCardItem(int index) {
        return new Locator("SPECIFIC_CARD_ITEM", new By.ByXPath("(//div[contains(@class,'GiftCardCarousel_carousel_item')])" + "[" + index + "]"));
    }

    @Override
    public void maximize() {
        driverTools.maximizeWindow();
    }

    @Override
    public void clickRedeemClaimGiftCard() {
        driverTools.waitForElementToAppear(REDEEM_SCRATCH_TEXT, 10);
        WebElement element = driverTools.element(REDEEM_CANVAS).get();
        driverTools.scrollIntoWebView(element);
        driverTools.scratchWithActions(element);
        driverTools.waitForElementToBeClickable(REDEEM_CLAIM_BUTTON, 10).ifPresent(driverTools::click);
    }

    @Override
    public void selectVoucherOptions() {
        driverTools.waitForElementToAppear(getVoucherSelection(1))
            .ifPresent(driverTools::click);
    }

    protected void clickSpecificCardItem(int index) {
        int maxAttempts = 5;
        int attempts = 0;
        driverTools.waitForElementToAppear(getSpecificCardItem(index));
        while (!driverTools.isElementDisplayed(getSpecificCardItem(index))) {
            if (!driverTools.isElementDisplayed(CARD_NEXT_CAROUSEL)) {
                throw new NoSuchElementException("Next carousel button is no longer visible, but target card item was not found.");
            }
            driverTools.click(CARD_NEXT_CAROUSEL);
            attempts++;
            if (attempts >= maxAttempts) {
                throw new NotFoundException("Max attempts reached while searching for card item index " + index);
            }
        }
        driverTools.waitForElementToBeClickable(getSpecificCardItem(index));
        driverTools.click(getSpecificCardItem(index));
        driverTools.waitForElementToAppear(SELECTED_CARD_FIGURE);
    }

    @Override
    public void clickAddRecipient() {
        driverTools.waitForElementToAppear(RECIPIENT_BUTTON);
        driverTools.waitForElementToBeClickable(RECIPIENT_BUTTON);
        driverTools.click(RECIPIENT_BUTTON);
    }
}
