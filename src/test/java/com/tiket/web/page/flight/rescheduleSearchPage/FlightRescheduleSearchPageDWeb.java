package com.tiket.web.page.flight.rescheduleSearchPage;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class FlightRescheduleSearchPageDWeb <T extends WebDriver> extends FlightRescheduleSearchPageWeb<T>{
    public FlightRescheduleSearchPageDWeb(Session<T> session) {
        super(session);
    }
}
