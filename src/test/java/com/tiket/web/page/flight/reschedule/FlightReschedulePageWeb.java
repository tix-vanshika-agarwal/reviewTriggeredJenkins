package com.tiket.web.page.flight.reschedule;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hamcrest.core.Is.is;

public class FlightReschedulePageWeb <T extends WebDriver> extends WebPage<T> {

    public FlightReschedulePageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FlightReschedulePageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new FlightReschedulePageDWeb<>(session);
            }
            case AMWEB, IMWEB -> {
                return new FlightReschedulePageMWeb<>(session);
            }
            default -> throw new UnsupportedOperationException("Platform not supported");
        }
    }

    private static final Locator CANCEL_AND_REBOOK_METHOD = new Locator("CANCEL_AND_REBOOK_METHOD", By.xpath("//label[contains(@class,'Radio_radio')][.//input[@name='reschedule-options'][contains(@value,'BASIC')]]"));
    private static final Locator CANCEL_AND_REBOOK_RADIO = new Locator("CANCEL_AND_REBOOK_RADIO", By.xpath("//input[@name='reschedule-options'][contains(@value,'BASIC')]"));
    private static final Locator RESCHEDULE_METHOD_CONTINUE = new Locator("RESCHEDULE_METHOD_CONTINUE", By.xpath("//button[contains(@class,'options_footer_button')]"));
    private static final Locator SELECT_ALL_PASSENGER_TOGGLE = new Locator("SELECT_ALL_PASSENGER_TOGGLE", By.xpath("//label[./div[contains(@class, 'Toggle')]]/input[@type='checkbox']"));
    private static final Locator FIND_NEW_SCHEDULE = new Locator("FIND_NEW_SCHEDULE", By.xpath("//button[contains(@class, 'Button_variant_primary')]"));
    private static final Locator DESTINATION_AIRPORT_BOX = new Locator("DESTINATION_AIRPORT_BOX", By.xpath("//div[contains(@class,'RescheduleSearchForm_airport_box')][.//span[contains(text(),'Destination')]]"));
    private static final Locator AIRPORT_SEARCH_INPUT = new Locator("AIRPORT_SEARCH_INPUT", By.xpath("//input[contains(@class,'SearchBox_accessibility_search')]"));
    private static final Locator FIRST_AIRPORT_RESULT = new Locator("FIRST_AIRPORT_RESULT", By.xpath("(//div[contains(@class,'AirportForm_airport')])[1]"));
    private static final Locator IDR_ZERO_TEXT = new Locator("IDR_ZERO_TEXT", By.xpath("//p[contains(text(),'IDR ')]"));
    private static final Locator SELECT_RESCHEDULE_FLIGHT_BUTTON = new Locator("SELECT_RESCHEDULE_FLIGHT_BUTTON", By.xpath("//button[contains(@class,'FareCard_select_btn')]//span[contains(text(),'Select')]/parent::button"));
    private static final Locator DATE_OF_BIRTH = new Locator("DATE_OF_BIRTH", new By.ByXPath("//input[@id='date-of-birth']"));

    /**
     * Orders that offer both reschedule methods land on a method page first; the rest go straight
     * to the reschedule form, so the step is skipped when the page is not rendered.
     * Cancel and rebook is the only method that allows an airline or route change.
     */
    public void selectCancelAndRebookMethodIfPresent() {
        if (driverTools.waitForElementToAppear(CANCEL_AND_REBOOK_METHOD, 5).isEmpty()) {
            logger.log("Reschedule method page is not displayed, continuing on the reschedule form.");
            return;
        }
        driverTools.clickWithJavascriptExecutor(CANCEL_AND_REBOOK_METHOD);
        Assertion.assertThat("Cancel and rebook is selected as the reschedule method",
                driverTools.isRadioButtonChecked(CANCEL_AND_REBOOK_RADIO), is(true));
        driverTools.clickWithJavascriptExecutor(RESCHEDULE_METHOD_CONTINUE);
        driverTools.waitForElementToPresence(SELECT_ALL_PASSENGER_TOGGLE);
    }

    /**
     * The toggle is already on for orders where every passenger is reschedulable, so clicking it
     * unconditionally would deselect them all.
     */
    public void selectAllPassengers() {
        driverTools.waitForElementToPresence(SELECT_ALL_PASSENGER_TOGGLE);
        if (driverTools.isElementSelected(SELECT_ALL_PASSENGER_TOGGLE)) {
            logger.log("All passengers are already selected.");
            return;
        }
        driverTools.clickWithJavascriptExecutor(SELECT_ALL_PASSENGER_TOGGLE);
    }

    public void pressFindNewScheduleButton() {
        driverTools.dummyWait(7);
        driverTools.waitForElementToPresence(FIND_NEW_SCHEDULE);
        driverTools.clickWithJavascriptExecutor(FIND_NEW_SCHEDULE);
    }

    public void changeDestinationAirport(String destinationAirport) {
        driverTools.waitForElementToAppear(DESTINATION_AIRPORT_BOX);
        driverTools.clickWithJavascriptExecutor(DESTINATION_AIRPORT_BOX);

        driverTools.waitForElementToAppear(AIRPORT_SEARCH_INPUT);
        driverTools.dummyWait(1);
        driverTools.sendKeys(AIRPORT_SEARCH_INPUT, destinationAirport);
        driverTools.dummyWait(2);
        
        driverTools.waitForElementToAppear(FIRST_AIRPORT_RESULT);
        driverTools.clickWithJavascriptExecutor(FIRST_AIRPORT_RESULT);
    }

    public boolean isIDRZeroPresent() {
        driverTools.waitForElementToAppear(IDR_ZERO_TEXT);
        return driverTools.isElementDisplayed(IDR_ZERO_TEXT);
    }

    public void clickSelectRescheduleFlightButton() {
        driverTools.waitForElementToAppear(SELECT_RESCHEDULE_FLIGHT_BUTTON);
        driverTools.clickWithJavascriptExecutor(SELECT_RESCHEDULE_FLIGHT_BUTTON);
    }

    public boolean clickDateOfBirth(){
        WebElement dateOfBirth = driverTools.waitForElementToAppear(DATE_OF_BIRTH).orElseThrow();
        boolean isEnabled = dateOfBirth.isEnabled();
        String readonlyAttr = dateOfBirth.getAttribute("readonly");
        boolean isReadonly = readonlyAttr != null;
        return isEnabled && !isReadonly;
    }
}
