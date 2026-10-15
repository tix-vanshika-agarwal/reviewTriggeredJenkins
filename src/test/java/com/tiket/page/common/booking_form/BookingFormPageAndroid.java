package com.tiket.page.common.booking_form;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class BookingFormPageAndroid <T extends WebDriver> extends CommonBookingFormPage<T> {

    public BookingFormPageAndroid(Session<T> session) {
        super(session);
    }

    public static final Locator CONTACT_SWITCH_V3 = new Locator("CONTACT_SWITCH_V3",AppiumBy.id("sw_sameascontactdetail"));
    public static final Locator SAVE_BUTTON_GUEST_PASSENGER = new Locator("PASSENGER_SAVE_V3", AppiumBy.id("btn_flight_passenger_save"));
    public static final Locator SAVE_CONTACT_V3 = new Locator("SAVE_CONTACT_V3",AppiumBy.id("tv_save_contact_details"));
    public static final Locator PROCESSING_BOOKING = new Locator("PROCESSING_BOOKING", AppiumBy.xpath("//*[contains(@resource-id,'content_loading_dialog')]"));

    @Override
    public void waitProcessingBooking() {
        driverTools.waitForElementToAppear(PROCESSING_BOOKING);
        driverTools.waitForElementToDisappear(PROCESSING_BOOKING, 30);
    }
}
