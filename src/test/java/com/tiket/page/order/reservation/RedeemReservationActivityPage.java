package com.tiket.page.order.reservation;

import com.tiket.model.Locator;
import com.tiket.model.Session;

import com.tiket.testbase.BasePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class RedeemReservationActivityPage<T extends WebDriver> extends BasePage<T> {

    public RedeemReservationActivityPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> RedeemReservationActivityPage<T> getInstance(Session<T> session) {
        return new RedeemReservationActivityPage<>(session);
    }

    private static final Locator FIRST_ACTIVITY_CARD = new Locator("FIRST_ACTIVITY_CARD", AppiumBy.xpath("(//div[contains(@class, 'PassesMultiActivityCard')])[1]"));

    public void selectActivity() {
        driverTools.switchToWebViewUrl("to-do/reservation");
        driverTools.click(FIRST_ACTIVITY_CARD);
        driverTools.switchToNative();
    }
}
