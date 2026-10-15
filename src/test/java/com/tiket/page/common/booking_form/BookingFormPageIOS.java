package com.tiket.page.common.booking_form;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class BookingFormPageIOS <T extends WebDriver> extends CommonBookingFormPage<T> {

    public BookingFormPageIOS(Session<T> session) { super(session); }

    public static final Locator PROCESSING_BOOKING = new Locator("PROCESSING_BOOKING", AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND (label CONTAINS[c] 'santai dulu' OR label CONTAINS[c] 'relax and take a breath' OR label CONTAINS[c] 'sedang memproses pesananmu' OR label CONTAINS[c] 'processing your booking')"));

    @Override
    public void waitProcessingBooking() {
        driverTools.waitForElementToAppear(PROCESSING_BOOKING);
        driverTools.waitForElementToDisappear(PROCESSING_BOOKING, 30);
    }
}
