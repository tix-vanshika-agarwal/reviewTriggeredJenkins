package com.tiket.page.promo.gift_card_detail_page;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import static com.tiket.core.CoreFunctions.*;

public abstract class GiftCardDetailPage<T extends WebDriver> extends BasePage<T> {
    public GiftCardDetailPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> GiftCardDetailPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new GiftCardDetailPageAndroid<>(session);
            }
            case IOS -> {
                return new GiftCardDetailPageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator NAME_INPUT = new Locator("NAME_INPUT", AppiumBy.xpath("//input[@id='nama-lengkap' or @id='full-name']"));
    private static final Locator PHONE_INPUT = new Locator("PHONE_INPUT", AppiumBy.xpath("//input[@id='phone-number' or @id='nomor-hp']"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", AppiumBy.xpath("//input[@id='email-address' or @id='alamat-email']"));
    private static final Locator SENDER_INPUT = new Locator("SENDER_INPUT", AppiumBy.xpath("//input[@id='sender-name' or @id='nama-pengirim']"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.xpath("//button//span[contains(text(), 'Continue') or contains(text(), 'Lanjut')]"));

    private static final String CHECKOUT_URL_PATH = "gift-card/checkout";

    /** Falls back to the first available webview when no window matches the checkout url. */
    private void switchToCheckoutWebView() {
        if (driverTools.switchToWebViewUrl(CHECKOUT_URL_PATH).isEmpty()) {
            driverTools.switchToWebView();
        }
    }

    public void fillInGuestDetail() {
        driverTools.waitForWebview();
        driverTools.switchToWebView();

        driverTools.waitForElementToPresence(NAME_INPUT);
        driverTools.sendKeys(NAME_INPUT, randomName());
        driverTools.sendKeys(PHONE_INPUT, randomPhone());
        driverTools.sendKeys(EMAIL_INPUT, randomEmail());
        driverTools.hideKeyboard();

        driverTools.switchToNative();
    }

    public void clickContinueToPayment() {
        driverTools.waitForWebview();
        switchToCheckoutWebView();

        driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON);

        driverTools.switchToNative();
    }

    public void inputSendeName() {
        driverTools.waitForWebview();
        switchToCheckoutWebView();

        driverTools.swipeToElement(SENDER_INPUT, Swipe.UP, 3);
        driverTools.sendKeys(SENDER_INPUT, randomName());

        driverTools.hideKeyboard();
        driverTools.switchToNative();
    }
}
