package com.tiket.web.page.flight.rescheduleCart;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class FlightRescheduleCartDWeb <T extends WebDriver> extends FlightRescheduleCartWeb<T>{
    public FlightRescheduleCartDWeb(Session<T> session) {
        super(session);
    }
}
