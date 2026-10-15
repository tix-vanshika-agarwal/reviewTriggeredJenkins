package com.tiket.page.promo.gift_card_splitting_page;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class GiftCardSplittingPage<T extends WebDriver> extends BasePage<T> {
    public GiftCardSplittingPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> GiftCardSplittingPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new GiftCardSplittingPageAndroid<>(session);
            }
            case IOS -> {
                return new GiftCardSplittingPageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator VOUCHER_CODE = new Locator("VOUCHER_CODE", AppiumBy.xpath("//div[contains(@class, 'GiftCardCode_voucher_content')]//p[contains(@class, 'Text_weight_bold')]"));
    private static final Locator VOUCHER_BOX_BUTTON = new Locator("VOUCHER_BOX_BUTTON", AppiumBy.xpath("//a[contains(@class, 'GiftCardDetailContainer_button_mobile') and @role='link']//span[contains(text(), 'Kotak Voucher') or contains(text(), 'Voucher Box')]"));

    public void switchToWebview() {
        driverTools.waitForWebview();
        driverTools.switchToWebView();
    }

    public String getSplittedVoucherCode() {
        driverTools.switchToWebViewUrl("gift-card");
        String value = driverTools.getText(VOUCHER_CODE).orElseThrow();
        driverTools.switchToNative();
        return value;
    }

    public void openVoucherBox() {
        driverTools.switchToWebViewUrl("gift-card");
        driverTools.clickWithJavascriptExecutor(VOUCHER_BOX_BUTTON);
        driverTools.switchToNative();
    }

    private Locator getVoucherBoxCode(String code) {
        return new Locator("VOUCHER_BOX_CODE", AppiumBy.xpath("//span[contains(@class, 'VoucherCodeCard_code') and contains(@class, 'Text_text') and text()='"+ code +"']"));
    }

    public boolean verifyCodeinVoucherBox(String code) {
        driverTools.switchToWebViewUrl("voucher-box");
        Locator voucherCode = getVoucherBoxCode(code);
        boolean value = driverTools.isElementPresent(voucherCode);
        driverTools.switchToNative();

        return value;
    }
}
