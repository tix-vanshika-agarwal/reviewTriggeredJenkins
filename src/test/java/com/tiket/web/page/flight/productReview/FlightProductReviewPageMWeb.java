package com.tiket.web.page.flight.productReview;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.web.page.flight.bookingForm.FlightBookingFormPageWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FlightProductReviewPageMWeb <T extends WebDriver> extends FlightProductReviewPageWeb<T> {
    public FlightProductReviewPageMWeb (Session<T> session)  { super(session); }

    private static final Locator SELECT_BUTTON = new Locator("SELECT_BUTTON", By.xpath("//button[span[text()='Select' or text()='Pilih']]"));
    /** Round-trip fare review stepper (e.g. Select depart ticket → Select return ticket); when present, Select must be clicked twice. */
    private static final Locator STEPPER_JOURNEY_ROUND_TRIP = new Locator("STEPPER_JOURNEY_ROUND_TRIP", By.xpath("//div[contains(@class,'StepperJourney_stepper_journey__')]"));
    private static final Locator FACILITY_POPUP = new Locator("FACILITY_POPUP", new By.ByXPath("//div[contains(@class,'BottomSheet_body')]"));
    private static final Locator CLOSE = new Locator("CLOSE", new By.ByXPath("//button[contains(@class,'BottomSheet_close_button')]"));

    @Override
    public boolean isFacilitiesPopupVisible(){
        return driverTools.waitForElementToAppear(FACILITY_POPUP).isPresent();
    }

    public void continueToBookingForm(){
        driverTools.waitForElementToPresence(SELECT_BUTTON);
        driverTools.scrollIntoWebView(SELECT_BUTTON);
        driverTools.clickWithJavascriptExecutor(SELECT_BUTTON);
        if (driverTools.isElementPresent(STEPPER_JOURNEY_ROUND_TRIP)) {
            driverTools.waitForElementToPresence(SELECT_BUTTON);
            driverTools.scrollIntoWebView(SELECT_BUTTON);
            driverTools.clickWithJavascriptExecutor(SELECT_BUTTON);
        }
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPage.waitForBookingPageToLoad();
    }

    @Override
    public void closePopUp(){
        driverTools.waitForElementToAppear(CLOSE);
        driverTools.clickWithJavascriptExecutor(CLOSE);
        driverTools.waitForElementToDisappear(CLOSE);
    }
}
