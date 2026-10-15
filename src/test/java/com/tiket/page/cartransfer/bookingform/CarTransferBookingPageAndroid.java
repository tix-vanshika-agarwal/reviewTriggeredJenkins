package com.tiket.page.cartransfer.bookingform;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class CarTransferBookingPageAndroid <T extends WebDriver> extends CarTransferBookingPage<T>{

    public CarTransferBookingPageAndroid(Session<T> session) {
        super(session);
    }
}
