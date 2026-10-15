package com.tiket.web.smart.Hotel;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class HotelSmartPageMWeb<T extends WebDriver> extends HotelSmartPageWeb<T> {
    public HotelSmartPageMWeb(Session<T> session) {
        super(session);
    }
}
