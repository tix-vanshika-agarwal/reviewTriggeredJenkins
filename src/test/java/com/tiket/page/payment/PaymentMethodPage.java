package com.tiket.page.payment;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class PaymentMethodPage<T extends WebDriver> extends BasePage<T> {

    private static final Locator PAYMENT_METHODS = new Locator("PAYMENT_METHODS", AppiumBy.xpath("//*[contains(@class,'Items_method_title')]"));

    public PaymentMethodPage(Session<T> session) {
        super(session);
    }

    public List<String> getPaymentMethods(){
        driverTools.switchToWebViewUrl("smart_pay");
        driverTools.waitForElementToAppear(PAYMENT_METHODS);
        List<String> paymentMethods =driverTools.getTexts(PAYMENT_METHODS);
        driverTools.switchToNative();
        return paymentMethods;
    }
}
