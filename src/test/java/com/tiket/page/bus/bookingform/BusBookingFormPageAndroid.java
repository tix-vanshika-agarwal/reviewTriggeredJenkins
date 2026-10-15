package com.tiket.page.bus.bookingform;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class BusBookingFormPageAndroid <T extends WebDriver> extends BusBookingFormPage<T>{

    public BusBookingFormPageAndroid(Session<T> session) {
        super(session);
    }
    
}
