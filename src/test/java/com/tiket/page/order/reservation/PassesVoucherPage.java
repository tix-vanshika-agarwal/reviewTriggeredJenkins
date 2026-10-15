package com.tiket.page.order.reservation;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class PassesVoucherPage<T extends WebDriver> extends BasePage<T> {

    public PassesVoucherPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> PassesVoucherPage<T> getInstance(Session<T> session) {
        return new PassesVoucherPage<>(session);
    }

    private static final Locator X_BUTTON = new Locator("X_BUTTON", AppiumBy.xpath("//button[contains(@class, 'back_button')]"));
    private static final Locator QR_CODE = new Locator("QR_CODE", AppiumBy.xpath("//*[contains(@class, 'qr_code')]"));

    public void clickBackButton() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("isSuccessReserve=true");
        driverTools.click(X_BUTTON);
        driverTools.switchToNative();
    }

    public boolean checkQrCode() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("to-do/reservation");
        boolean qrCodeDisplayed = driverTools.isElementDisplayed(QR_CODE);
        driverTools.switchToNative();
        return qrCodeDisplayed;
    }
}
