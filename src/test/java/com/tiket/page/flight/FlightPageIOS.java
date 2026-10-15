package com.tiket.page.flight;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class FlightPageIOS <T extends WebDriver> extends FlightPage<T> {
    public FlightPageIOS(Session<T> session) { super(session); }
}
