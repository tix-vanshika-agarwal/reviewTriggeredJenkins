package com.tiket.web.page.nft.car_rental.bookingForm_Page;

import com.tiket.model.Locator;
import com.tiket.model.Session;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CarRentalBookingPageDWeb<T extends WebDriver> extends CarRentalBookingPageWeb<T> {

    public CarRentalBookingPageDWeb(Session<T> session) {
        super(session);
    }

    private static final Locator SET_PICKUP_LOCATION = new Locator("SET_PICKUP_LOCATION", new By.ByXPath("(//*[contains(@class, 'Modal_modal_footer')])//button"));
    private static final Locator DELIVERABLE_LOCATION = new Locator("DELIVERABLE_LOCATION", new By.ByXPath("(//*[contains(@class, 'BookingLocationInput_options')])[2]"));
    private static final Locator PICK_UP_AREA_CONTAINER = new Locator("PICK_UP_AREA_CONTAINER", By.xpath("(//span[contains(@class, 'Text_size_b2') and contains(text(), 'Area')])[2]"));
    private static final Locator DROP_OFF_AREA_CONTAINER = new Locator("DROP_OFF_AREA_CONTAINER", By.xpath("(//span[contains(@class, 'Text_size_b2') and contains(text(), 'Area')])[4]"));

    @Override
    public String setPickUpLocation(String addLocation) {
        driverTools.waitForElementToAppear(SELECT_PICKUP_LOCATION);
        driverTools.click(SELECT_PICKUP_LOCATION);

        driverTools.waitForElementToAppear(DELIVERABLE_LOCATION);
        if (driverTools.isElementDisplayed(DELIVERABLE_LOCATION)) driverTools.clickWithJavascriptExecutor(DELIVERABLE_LOCATION);

        driverTools.waitForElementToAppear(PICKUP_LOCATION_SEARCH_BOX, 3);
        driverTools.clickWithJavascriptExecutor(PICKUP_LOCATION_SEARCH_BOX);
        driverTools.sendKeys(PICKUP_LOCATION_SEARCH_BOX, addLocation);

        driverTools.waitForElementToAppear(PICK_FIRST_LOCATION);
        driverTools.click(PICK_FIRST_LOCATION);
        driverTools.click(SET_PICKUP_LOCATION);

        driverTools.waitForElementToAppear(SNACK_BAR_OK, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
        return driverTools.waitForElementToAppear(SELECT_PICKUP_LOCATION).orElseThrow().getText();
    }

    public String setSetDropOffLocation(String addDestination) {
        driverTools.waitForElementToAppear(SELECT_DROP_OFF_LOCATION, 3);
        driverTools.clickWithJavascriptExecutor(SELECT_DROP_OFF_LOCATION);

        driverTools.waitForElementToAppear(DELIVERABLE_LOCATION);
        if (driverTools.isElementDisplayed(DELIVERABLE_LOCATION)) driverTools.clickWithJavascriptExecutor(DELIVERABLE_LOCATION);

        WebElement addDestinationInput = driverTools.waitForElementToAppear(DROP_OFF_LOCATION_SEARCH_BOX).orElseThrow();
        if (!driverTools.getValue(addDestinationInput).equals(addDestination)) {
            driverTools.clearInput(addDestinationInput);
            driverTools.sendKeys(addDestinationInput, addDestination);
        }
        driverTools.waitForElementToAppear(PICK_FIRST_LOCATION);
        driverTools.click(PICK_FIRST_LOCATION);


        driverTools.click(SET_DROP_OFF_LOCATION);
        driverTools.waitForElementToAppear(SNACK_BAR_OK, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
        return driverTools.waitForElementToAppear(SELECT_DROP_OFF_LOCATION).orElseThrow().getText();
    }

    public void continueBookFormMWEB() {
    } // Not implemented for DWeb platform, as it is not applicable in this context.

    @Override
    public String verifiesPickUpArea() {
        driverTools.waitForElementToAppear(PICK_UP_AREA_CONTAINER);
        return driverTools.getText(PICK_UP_AREA_CONTAINER).orElseThrow();
    }

    @Override
    public String verifiesDropOffArea() {
        driverTools.waitForElementToAppear(DROP_OFF_AREA_CONTAINER);
        return driverTools.getText(DROP_OFF_AREA_CONTAINER).orElseThrow();
    }
}
