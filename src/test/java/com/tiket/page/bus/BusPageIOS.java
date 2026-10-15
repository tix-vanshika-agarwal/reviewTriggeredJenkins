package com.tiket.page.bus;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class BusPageIOS<T extends WebDriver> extends BusPage<T> {

    public BusPageIOS(Session<T> session) {
        super(session);
    }
}
