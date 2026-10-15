package com.tiket.page.ttd.bookingformpage;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class TTDBookingFormPage <T extends WebDriver> extends BasePage<T> {

    public TTDBookingFormPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TTDBookingFormPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> { return new TTDBookingFormPageAndroid<>(session); }
            case IOS -> { return new TTDBookingFormPageIOS<>(session); }
            default -> throw new NotImplementedException("Not implemented");
        }
    }
}
