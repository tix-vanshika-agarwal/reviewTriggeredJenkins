package com.tiket.page.flight;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class FlightPageAndroid <T extends WebDriver> extends FlightPage<T> {
    public FlightPageAndroid(Session<T> session) { super(session); }
}
