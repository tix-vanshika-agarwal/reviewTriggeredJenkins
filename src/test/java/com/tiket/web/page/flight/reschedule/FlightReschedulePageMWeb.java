package com.tiket.web.page.flight.reschedule;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class FlightReschedulePageMWeb <T extends WebDriver> extends FlightReschedulePageWeb<T>{
    public FlightReschedulePageMWeb(Session<T> session) { super(session); }
}
