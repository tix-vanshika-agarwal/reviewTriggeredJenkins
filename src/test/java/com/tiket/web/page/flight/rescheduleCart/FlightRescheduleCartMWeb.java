package com.tiket.web.page.flight.rescheduleCart;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class FlightRescheduleCartMWeb<T extends WebDriver> extends FlightRescheduleCartWeb<T>{
    public FlightRescheduleCartMWeb(Session<T> session) {
        super(session);
    }
}
