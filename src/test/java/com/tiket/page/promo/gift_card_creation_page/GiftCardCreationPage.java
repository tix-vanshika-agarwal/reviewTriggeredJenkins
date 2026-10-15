package com.tiket.page.promo.gift_card_creation_page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import static com.tiket.core.CoreFunctions.randomName;
import static com.tiket.core.CoreFunctions.randomPhone;

public abstract class GiftCardCreationPage<T extends WebDriver> extends BasePage<T> {

    public GiftCardCreationPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> GiftCardCreationPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new GiftCardCreationPageAndroid<>(session);
            }
            case IOS -> {
                return new GiftCardCreationPageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator EDIT_RECIPIENT_BUTTON = new Locator("EDIT_RECIPIENT_BUTTON", AppiumBy.xpath("//*[contains(@class, 'Recipients_card_container')]//div[contains(@class, 'Recipients_actions')]"));
    private static final Locator RECIPIENT_NAME_INPUT = new Locator("RECIPIENT_NAME_INPUT", AppiumBy.xpath("//*[@id='nama-penerima' or @id='recipient-name']"));
    private static final Locator AMOUNT_INPUT = new Locator("AMOUNT_INPUT", AppiumBy.xpath("//*[@id='enter-the-amount' or @id='masukkan-nominal']"));
    private static final Locator PHONE_INPUT = new Locator("PHONE_INPUT", AppiumBy.xpath("//*[@id='mobile-number' or @id='phone-number' or @id='nomor-hp']"));
    private static final Locator FIRST_PRICE_CHIP = new Locator("FIRST_PRICE_CHIP", AppiumBy.xpath("(//*[contains(@class, 'RecipientDynamicForm')]//button[contains(@class, 'chip')])[1]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", AppiumBy.xpath("//button//span[contains(text(), 'Save') or contains(text(), 'Simpan')]"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.xpath("//button//span[contains(text(), 'Continue') or contains(text(), 'Lanjutkan')]"));
    private static final Locator AMOUNT_ERROR = new Locator("AMOUNT_ERROR", AppiumBy.xpath("//*[contains(text(), 'amount must not exceed') or contains(text(), 'Nominal gift card tidak boleh lebih dari')]"));
    private static final Locator HOME_PAGE_SEARCH_BAR = new Locator("HOME_PAGE_SEARCH_BAR", AppiumBy.xpath("tds_search_et"));

    private static final String GIFT_CARD_URL_PATH = "gift-card";

    /**
     * The app keeps webviews from earlier tests alive, so switching by context alone lands on
     * whichever tiket.com window comes first. Target the gift card url instead.
     */
    protected void switchToGiftCardWebView() {
        driverTools.waitForWebview();
        if (driverTools.switchToWebViewUrl(GIFT_CARD_URL_PATH).isEmpty()) {
            driverTools.switchToWebView();
        }
    }

    public void fillInCardDetail() {
        switchToGiftCardWebView();

        driverTools.click(EDIT_RECIPIENT_BUTTON);
        driverTools.waitForElementToPresence(RECIPIENT_NAME_INPUT);
        driverTools.sendKeys(RECIPIENT_NAME_INPUT, randomName());
        driverTools.sendKeysKeyboard(PHONE_INPUT, randomPhone());
        driverTools.hideKeyboardForce();
        driverTools.click(FIRST_PRICE_CHIP);
        driverTools.clickElementByCoordinate(SAVE_BUTTON);

        driverTools.switchToNative();
    }

    public void fillInCardDetail(int specificAmount) {
        switchToGiftCardWebView();

        driverTools.click(EDIT_RECIPIENT_BUTTON);
        driverTools.waitForElementToPresence(RECIPIENT_NAME_INPUT);
        driverTools.sendKeys(RECIPIENT_NAME_INPUT, randomName());
        driverTools.sendKeysKeyboard(PHONE_INPUT, randomPhone());
        driverTools.hideKeyboardForce();
        driverTools.sendKeys(AMOUNT_INPUT, String.valueOf(specificAmount));
        driverTools.clickElementByCoordinate(SAVE_BUTTON);

        driverTools.switchToNative();
    }

    public boolean isAmountErrorShown() {
        switchToGiftCardWebView();
        boolean result = driverTools.isElementDisplayed(AMOUNT_ERROR);
        driverTools.switchToNative();
        return result;
    }

    public void clickContinueButtonFooter() {
        switchToGiftCardWebView();
        driverTools.click(CONTINUE_BUTTON);
        driverTools.switchToNative();
    }
}
