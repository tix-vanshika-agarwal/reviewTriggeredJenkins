package com.tiket.web.smart.Hotel;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class HotelSmartPageDWeb<T extends WebDriver> extends HotelSmartPageWeb<T> {
    public HotelSmartPageDWeb(Session<T> session) {
        super(session);
    }
}
