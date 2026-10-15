package com.tiket.page.flight.reschedulepage;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class FlightReschedulePageAndroid <T extends WebDriver> extends FlightReschedulePage<T> {
    public FlightReschedulePageAndroid(Session<T> session) { super(session); }
}
