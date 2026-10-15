package com.tiket.web.page.flight.landingpage;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class FlightLandingPageDWeb <T extends WebDriver> extends FlightLandingPageWeb<T> {

    public FlightLandingPageDWeb(Session<T> session) {
        super(session);
    }

    @Override
    public void selectDate(String dateToSelect) {
        driverTools.click(DEPART_CALENDAR);
        logger.log("Departing on: " + dateToSelect);
        clickDate(dateToSelect);
    }

    @Override
    public boolean isOnFlightPage() {
        return super.isOnFlightPage();
    }


}
