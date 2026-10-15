package com.tiket.web.page.nft.bus.searchResult_Page;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hamcrest.core.Is.is;

public class BusSearchResultPageMWeb<T extends WebDriver> extends BusSearchResultPageWeb<T> {

    public BusSearchResultPageMWeb(Session<T> session) {
        super(session);
    }

    private static final Locator CHANGE_SRP_HEADER = new Locator("CHANGE_SRP_HEADER", new By.ByXPath("//*[contains(@class, 'SRPHeader_change')]"));
    private static final Locator DEPARTURE_ROUTE_TEXT = new Locator("DEPARTURE_ROUTE_TEXT", new By.ByXPath("//*[contains(@class, 'SRPHeader_full')]"));
    private static final Locator SRP_HEADER_SCHEDULE_PASSENGER = new Locator("SRP_HEADER_SCHEDULE_PASSENGER", new By.ByXPath("//*[contains(@class, 'SRPHeader_schedule')]"));
    private static final Locator EXECUTIVE_CLASS = new Locator("EXECUTIVE_CLASS", new By.ByXPath("//*[contains(@class, 'CheckStyleForm_label_sublabel_container')]//*[contains(text(), 'Eks') or contains(text(), 'Ex') or contains(text(), 'EX')]"));
    private static final Locator SAVE_FILTER = new Locator("SAVE_FILTER", new By.ByXPath("//*[contains(@class, 'Button_variant_primary')]"));

    public void verifySearchResultIsCorrect(String origin, String destination, String departureDate, String returnDate, int totalPassenger, String language) {
        boolean isOnSRP = isUserOnSRP();
        Assertion.assertThat("User is on SRP", isOnSRP, is(true));

        boolean isOriginDestinationCorrect = verifyOriginAndDestinationSRP(origin, destination);
        Assertion.assertThat("Origin and Destination should match", isOriginDestinationCorrect, is(true));

        String actualTravelDate = getTravelDateFromSRP(departureDate);
        Assertion.assertThat("Expected travel date: " + departureDate + ", Actual travel date: " + actualTravelDate,
                CoreFunctions.compareDates(departureDate, actualTravelDate), is(true));

        int actualPassengerCount = getPassengerCountFromSRP();
        Assertion.assertThat("Expected: " + totalPassenger + ", Actual: " + actualPassengerCount,
                actualPassengerCount, is(totalPassenger));
    }

    public boolean isUserOnSRP() {
        driverTools.waitForElementToAppear(CHANGE_SRP_HEADER, 5);
        boolean isChangeSrpHeaderDisplayed = driverTools.isElementDisplayed(CHANGE_SRP_HEADER);
        Assertion.assertThat("is change srp header displayed: ", isChangeSrpHeaderDisplayed, is(true));
        boolean isFilterSortChipsDisplayed = driverTools.isElementDisplayed(FILTER_SORT);
        Assertion.assertThat("is filter & sort chips displayed: ", isChangeSrpHeaderDisplayed, is(true));

        return isChangeSrpHeaderDisplayed && isFilterSortChipsDisplayed;
    }

    public String[] getOriginAndDestinationFromSRP() {
        WebElement routeTextElement = driverTools.waitForElementToAppear(DEPARTURE_ROUTE_TEXT).orElseThrow();
        String routeText = driverTools.getText(routeTextElement).trim();
        String separator = routeText.contains(" ke ") ? " ke " : " to ";

        return routeText.split(separator);
    }

    public boolean verifyOriginAndDestinationSRP(String origin, String destination) {
        String[] srpLocations = getOriginAndDestinationFromSRP();
        String originOnSrp = srpLocations[0].trim();
        String destinationOnSrp = srpLocations[1].trim();

        Assertion.assertThat("Expected origin: " + origin + ", Actual origin: " + originOnSrp,
                originOnSrp.equalsIgnoreCase(origin), is(true));

        Assertion.assertThat("Expected destination: " + destination + ", Actual destination: " + destinationOnSrp,
                destinationOnSrp.equalsIgnoreCase(destination), is(true));

        return originOnSrp.equalsIgnoreCase(origin) && destinationOnSrp.equalsIgnoreCase(destination);
    }

    public String getTravelDateFromSRP(String departureDate) {
        WebElement srpHeaderTextElement = driverTools.waitForElementToAppear(SRP_HEADER_SCHEDULE_PASSENGER).orElseThrow();
        String srpHeaderText = driverTools.getText(srpHeaderTextElement).trim();
        String travelDateSRP = srpHeaderText.replaceAll("\\d+\\s*Seat.*", "").trim();
        String[] travelDateFormatted = travelDateSRP.split(" - ");
        String travelDate = travelDateFormatted[0];
        String year = CoreFunctions.getSectionOfString(departureDate," ", 2);
        travelDateSRP = travelDate + " " + year;

        logger.log("Expected travel date: " + departureDate + ", Found on SRP: " + travelDateSRP);

        return travelDateSRP;
    }

    public int getPassengerCountFromSRP() {
        WebElement srpHeaderTextElement = driverTools.waitForElementToAppear(SRP_HEADER_SCHEDULE_PASSENGER).orElseThrow();
        String srpHeaderText = driverTools.getText(srpHeaderTextElement).trim();
        String passengerCountSRPText = srpHeaderText.replaceAll(".*?(\\d+)\\s*Seats?", "$1").trim();
        int passengerCountSRPInt = Integer.parseInt(passengerCountSRPText);

        logger.log("Passenger count found on SRP: " + passengerCountSRPInt);

        return passengerCountSRPInt;
    }

    public void selectFilterByExecutiveClass() {
        WebElement executiveClass = driverTools.waitForElementToAppear(EXECUTIVE_CLASS).orElseThrow();
        driverTools.scrollIntoWebView(executiveClass);
        driverTools.clickWithJavascriptExecutor(EXECUTIVE_CLASS);
        driverTools.click(SAVE_FILTER);

        driverTools.waitForElementToDisappear(SAVE_FILTER);
    }

    public void editPassengerQueryParam(String queryParam, String newValue) {
        driverTools.editUrlQueryParam(queryParam, newValue);
    }
}
