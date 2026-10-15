package com.tiket.page.ttd.bookingformpage;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class TTDBookingFormPageAndroid <T extends WebDriver> extends TTDBookingFormPage<T> {

    public TTDBookingFormPageAndroid(Session<T> session) {
        super(session);
    }

}
