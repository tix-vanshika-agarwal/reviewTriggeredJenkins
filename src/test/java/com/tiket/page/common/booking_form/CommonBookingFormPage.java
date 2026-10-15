package com.tiket.page.common.booking_form;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class CommonBookingFormPage <T extends WebDriver> extends BasePage<T> {

    public CommonBookingFormPage(Session<T> session) { super(session); }

    public static <T extends WebDriver> CommonBookingFormPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> { return new BookingFormPageAndroid<>(session); }
            case IOS -> { return new BookingFormPageIOS<>(session); }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void waitProcessingBooking();
}
