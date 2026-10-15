package com.tiket.page.accommodation.hotel;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class HotelPageAndroid  <T extends WebDriver> extends HotelPage<T> {

    public HotelPageAndroid(Session<T> session) {
        super(session);
    }
}
