package com.tiket.web.page.flight.productReview;

import com.tiket.model.Session;
import com.tiket.web.page.flight.bookingForm.FlightBookingFormPageWeb;
import com.tiket.web.page.flight.searchresultpage.FlightSearchResultPageWeb;
import org.openqa.selenium.WebDriver;

public class FlightProductReviewPageDWeb <T extends WebDriver> extends FlightProductReviewPageWeb<T> {
    public FlightProductReviewPageDWeb (Session<T> session)  { super(session); }

    public void continueToBookingForm(){
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPage.waitForBookingPageToLoad();
    }
}
