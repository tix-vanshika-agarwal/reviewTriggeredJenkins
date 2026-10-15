package com.tiket.page.bus;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class BusPageAndroid<T extends WebDriver> extends BusPage<T> {

    public BusPageAndroid(Session<T> session) {
        super(session);
    }
}
