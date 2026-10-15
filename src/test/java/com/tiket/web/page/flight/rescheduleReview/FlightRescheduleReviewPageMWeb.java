package com.tiket.web.page.flight.rescheduleReview;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class FlightRescheduleReviewPageMWeb <T extends WebDriver> extends FlightRescheduleReviewPageWeb<T>{
    public FlightRescheduleReviewPageMWeb(Session<T> session) {
        super(session);
    }
}
