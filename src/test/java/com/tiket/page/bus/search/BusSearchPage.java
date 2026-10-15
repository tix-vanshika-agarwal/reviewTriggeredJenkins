package com.tiket.page.bus.search;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.home.HomePage;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.common.calendar.CalendarPageWeb;
import com.tiket.web.page.nft.bus.search_Page.BusSearchPageWeb;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import com.tiket.page.bus.searchresultpage.BusSearchResultPage;

public abstract class BusSearchPage <T extends WebDriver> extends BasePage<T> {

    private static final Locator ORIGIN_LOCATION_INPUT_CONTAINER = new Locator("ORIGIN_LOCATION_INPUT_CONTAINER", AppiumBy.xpath("(//div[contains(@class,'LocationInput_container')])[1]"));
    private static final Locator ORIGIN_LOCATION_TEXT= new Locator("ORIGIN_LOCATION_TEXT", AppiumBy.xpath("(//div[contains(@class,'LocationInput_input_text')]//span)[1]"));
    private static final Locator DESTINATION_LOCATION_TEXT= new Locator("DESTINATION_LOCATION_TEXT", AppiumBy.xpath("(//div[contains(@class,'LocationInput_input_text')]//span)[2]"));
    private static final Locator CALENDAR_TEXT= new Locator("CALENDAR_TEXT", AppiumBy.xpath("//span[contains(@class,'DateInput_text')]"));
    public static final Locator PAX_TEXT= new Locator("PAX_TEXT", AppiumBy.xpath("//*[contains(@class,'PassengerInput_text')]"));
    private static final Locator DESTINATION_LOCATION_INPUT_CONTAINER = new Locator("DESTINATION_LOCATION_INPUT_CONTAINER", AppiumBy.xpath("(//div[contains(@class,'LocationInput_container')])[2]"));
    private static final Locator LOCATION_SEARCH_BOX = new Locator("LOCATION_SEARCH_BOX", AppiumBy.xpath("(//div[contains(@class,'LocationPicker_search_box')])[1]//input"));
    private static final Locator SKIP_COACHMARK = new Locator("SKIP_COACHMARK",AppiumBy.xpath("//*[contains(text(),'Lewati') or contains(text(),'Skip')]"));
    private static final Locator SEARCH_TOMORROW_BTN = new Locator("SEARCH_TOMORROW_BTN", AppiumBy.xpath("//button[span[contains(text(),'Besok') or contains(text(),'Tomorrow')]]"));
    private static final Locator SEACH_BUS_BTN = new Locator("SEARCH_BUS_BTN", AppiumBy.xpath("//span[contains(text(),'Search bus') or contains(text(),'Cari bus')]//parent::button"));
    public static final Locator SAVE_CALENDAR = new Locator("SAVE_CALENDAR", AppiumBy.xpath("//*[contains(@class,'confirmation')]//button"));
    private static final Locator ONE_WAY_TAB = new Locator("ONE_WAY_TAB", AppiumBy.xpath("(//*[contains(@class,'TripTypeToggler')]//button)[1]"));
    private static final Locator ROUNDTRIP_TAB = new Locator("ROUNDTRIP_TAB", AppiumBy.xpath("(//*[contains(@class,'TripTypeToggler')]//button)[2]"));
    public static final Locator PASSENGER_PICKER_WHEEL = new Locator("PASSENGER_PICKER_WHEEL", AppiumBy.xpath("//*[contains(@class,'PassengerInput')]"));
    public static final Locator SAVE_PASSENGER= new Locator("SAVE_PASSENGER", AppiumBy.xpath("//*[contains(@class,'PassengerPopUp_footer')]//button"));
    public static final Locator DATE_SECTION= new Locator("DATE_SECTION", AppiumBy.xpath("//*[contains(@class,'DateInput') and contains(@class,'Text')]"));

    public BusSearchPage(Session<T> session) {
        super(session);
    }


    public static <T extends WebDriver> BusSearchPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new BusSearchPageAndroid<>(session);}
            case IOS -> {return new BusSearchPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    /**
     * XPath 1.0 string literal for dynamic values inside XPath predicates.
     */
    private static String busLocationXpathStringLiteral(String value) {
        if (value == null || value.isEmpty()) {
            return "''";
        }
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        String[] parts = value.split("'", -1);
        StringBuilder b = new StringBuilder("concat(");
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                b.append(", \"'\", ");
            }
            b.append("'").append(parts[i]).append("'");
        }
        b.append(")");
        return b.toString();
    }

    private Locator getLocationSuggestionLocator(String locationSearchValue) {
        locationSearchValue = locationSearchValue.trim().replaceAll("\\s+", " ");
        String inner;
        int firstSpace = locationSearchValue.indexOf(' ');
        if (firstSpace < 0) {
            String lit = busLocationXpathStringLiteral(locationSearchValue);
            inner = "//span[contains(@class,'Text_variant_activity')][normalize-space(.)=" + lit + "]"
                    + "/ancestor::span[contains(@class,'List_title')][1]";
        } else {
            String first = locationSearchValue.substring(0, firstSpace).trim();
            String second = locationSearchValue.substring(firstSpace + 1).trim();
            String litFirst = busLocationXpathStringLiteral(first);
            String litSecond = busLocationXpathStringLiteral(second);
            inner = "//span[contains(@class,'Text_variant_activity')][normalize-space(.)=" + litFirst + "]"
                    + "/following::span[contains(@class,'Text_variant_activity')][normalize-space(.)=" + litSecond
                    + "][1]/ancestor::span[contains(@class,'List_title')][1]";
        }
        return new Locator("ORIGIN_LOCATION_SUGGESTION", AppiumBy.xpath("(" + inner + ")[1]"));
    }

    public abstract void openBusSearchViaDeepLink();
    
    public void skipCoachMark(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("bus-travel");
        driverTools.waitForElementToAppear(SKIP_COACHMARK,10).ifPresent(driverTools::click);
        driverTools.switchToNative();
    }

    public boolean isBusLandingPageDisplayed(){
        driverTools.waitForWebview();
        String currentUrl = driverTools.switchToWebViewUrl("bus-travel",2);
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToAppear(SKIP_COACHMARK,10).ifPresent(driverTools::click);
        logger.log("Current url: " + currentUrl);
        boolean isLandingUrl = currentUrl != null && currentUrl.contains("bus-travel") && !currentUrl.contains("bus-travel/search");
        boolean isSearchButtonDisplayed = driverTools.waitForElementToAppear(SEACH_BUS_BTN).isPresent();
        driverTools.switchToNative();
        Assertion.assertThat("Bus landing page url is displayed", isLandingUrl, is(true));
        Assertion.assertThat("Search bus button is displayed", isSearchButtonDisplayed, is(true));
        return isLandingUrl && isSearchButtonDisplayed;
    }

    public void inputOriginLocation(String originLocation){
        logger.log("ORIGIN LOCATION : " + originLocation);
        driverTools.switchToWebViewUrl("bus-travel");
        driverTools.click(ORIGIN_LOCATION_INPUT_CONTAINER);
        driverTools.waitForElementToAppear(LOCATION_SEARCH_BOX);
        driverTools.click(LOCATION_SEARCH_BOX);
        driverTools.sendKeys(LOCATION_SEARCH_BOX,originLocation);
        Locator suggestionLocationLocator = this.getLocationSuggestionLocator(originLocation);
        driverTools.click(suggestionLocationLocator);
        driverTools.switchToNative();
    }

    public void inputDestinationLocation(String destinationLocation){
        logger.log("destinationLocation LOCATION : " + destinationLocation);
        driverTools.switchToWebViewUrl("bus-travel");
        driverTools.click(DESTINATION_LOCATION_INPUT_CONTAINER);
        driverTools.waitForElementToAppear(LOCATION_SEARCH_BOX);
        driverTools.click(LOCATION_SEARCH_BOX);
        driverTools.sendKeys(LOCATION_SEARCH_BOX,destinationLocation);
        Locator suggestionLocationLocator = this.getLocationSuggestionLocator(destinationLocation);
        driverTools.click(suggestionLocationLocator);
        driverTools.switchToNative();
    }

    public void chooseTomorrowDate(){
        driverTools.switchToWebViewUrl("bus-travel");
        driverTools.click(SEARCH_TOMORROW_BTN);
        driverTools.switchToNative();

    }

    public void clickSearchBus(){
        driverTools.switchToWebViewUrl("bus-travel");
        driverTools.clickWithJavascriptExecutor(SEACH_BUS_BTN);
        driverTools.switchToNative();
    }

    public void selectDate(String dateDeparture, String dateReturn){
        driverTools.switchToWebViewUrl("bus-travel");
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        busSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(dateDeparture, dateReturn);
        driverTools.switchToNative();
    }

    public void selectDate(String dateDeparture){
        driverTools.switchToWebViewUrl("bus-travel");
        driverTools.click(DATE_SECTION);
        driverTools.clickWithJavascriptExecutor(getDateLocator(dateDeparture));
        driverTools.click(SAVE_CALENDAR);
        driverTools.switchToNative();
    }

    public Locator getDateLocator(String date){
        String year = CoreFunctions.getSectionOfString(date," ", 2);
        String month = CoreFunctions.getSectionOfString(date," ", 1);
        String day = CoreFunctions.getSectionOfString(date," ", 0);
        String monthId = CoreFunctions.monthEngToId(month);
        String locatorTextEng = day + " " + month +" "+ year;
        String locatorTextId = day + " " + monthId +" "+ year;

        logger.log("date locator : " + locatorTextEng);
        return new Locator(locatorTextEng,AppiumBy.xpath("//*[contains(@aria-label,'" + locatorTextEng + "') or contains(@aria-label,'" + locatorTextId + "')]"));
    }

    public void switchTripType(String tripType){
        driverTools.switchToWebViewUrl("bus-travel");
        if(tripType.equals("oneway")){
            driverTools.click(ONE_WAY_TAB);
        }else {
            driverTools.click(driverTools.element(ROUNDTRIP_TAB).orElseThrow());
        }
        driverTools.switchToNative();
    }

    private int getCurrentPassengerInt(){
        driverTools.switchToWebViewUrl("bus-travel");
        String currentPassengerText = driverTools.getText(PAX_TEXT).orElseThrow();
        driverTools.switchToNative();
        
        try {
            // Extract the first number from the text (handles formats like "2 Seat", "2 Kursi", etc.)
            String[] parts = currentPassengerText.trim().split("\\s+");
            if (parts.length == 0) {
                throw new IllegalArgumentException("Empty passenger text received");
            }
            
            // Try to parse the first part as integer
            String firstPart = parts[0].trim();
            if (firstPart.isEmpty()) {
                throw new IllegalArgumentException("First part of passenger text is empty: " + currentPassengerText);
            }
            
            int passengerCount = Integer.parseInt(firstPart);
            logger.log("Extracted passenger count: " + passengerCount + " from text: \"" + currentPassengerText + "\"");
            return passengerCount;
            
        } catch (NumberFormatException e) {
            logger.fail("Failed to parse passenger count from text: \"" + currentPassengerText + 
                       "\". Expected format: 'number Seat' or 'number Kursi'. Error: " + e.getMessage());
            throw new IllegalArgumentException("Invalid passenger text format: \"" + currentPassengerText + 
                                             "\". Expected a number followed by text.", e);
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.fail("Failed to split passenger text: \"" + currentPassengerText + "\"");
            throw new IllegalArgumentException("Unable to parse passenger text: \"" + currentPassengerText + "\"", e);
        }
    }

    /**
     * Selects passenger count using modular approach.
     * Breaks down into: get current count -> open picker -> scroll to target -> save -> verify
     */
    public void selectPassenger(int passenger) {
        if(getCurrentPassengerInt() == passenger){
            logger.log("passenger already set to: " + passenger);
            return;
        }

        HomePage<T> homePage = HomePage.getInstance(session);
        BusSearchResultPage<T> busSearchResultPage = BusSearchResultPage.getInstance(session);
        logger.log("=== Changing passenger via deeplink ===");
        logger.log("clicking search bus to go to search result page");
        clickSearchBus();
        logger.log("waiting for search result page to load");
        busSearchResultPage.skipCoachMark();
        logger.log("changing pax via deeplink to: " + passenger);
        busSearchResultPage.changePaxViaDeeplink(passenger);
        logger.log("saving result after pax change");
        busSearchResultPage.saveResultAfterDeeplinkPaxChange();
        logger.log("returning to bus search page");
        homePage.openVerticalPageDeeplink("Bus", "", "IDR");
        logger.log("verifying passenger selection: " + passenger);
        verifyPassengerSelection(passenger);
    }

    public boolean isDepartureSelected(String departureExpected){
        driverTools.switchToWebViewUrl("bus-travel");
        WebElement departureElement = driverTools.waitForElementToAppear(ORIGIN_LOCATION_TEXT).orElseThrow();
        String departureActual = driverTools.getText(departureElement);
        logger.log("departure expected: " + departureExpected);
        logger.log("departure expected: " + departureActual);
        boolean isCorrectDepartureLocationSelected = departureExpected.toLowerCase().contains(departureActual.toLowerCase());
        logger.log("correct departure selected: " + isCorrectDepartureLocationSelected);
        driverTools.switchToNative();
        return isCorrectDepartureLocationSelected;
    }

    public boolean isDestinationSelected(String destinationExpected){
        driverTools.switchToWebViewUrl("bus-travel");
        WebElement returnElement = driverTools.waitForElementToAppear(DESTINATION_LOCATION_TEXT).orElseThrow();
        String destinationActual = driverTools.getText(returnElement);
        logger.log("destination expected: " + destinationExpected);
        logger.log("destination expected: " + destinationActual);
        boolean isCorrectDestinationLocationSelected = destinationExpected.toLowerCase().contains(destinationActual.toLowerCase());
        logger.log("correct departure selected: " + isCorrectDestinationLocationSelected);
        driverTools.switchToNative();
        return isCorrectDestinationLocationSelected;
    }

    public boolean isDateSelected(String departureDate, String returnDate){
        driverTools.switchToWebViewUrl("bus-travel");
        WebElement dateElement = driverTools.waitForElementToAppear(CALENDAR_TEXT).orElseThrow();
        String dateActual = driverTools.getText(dateElement);

        String dateExpectedEng = getDateSearchFormLocatorText(departureDate).get("eng") + " - " + getDateSearchFormLocatorText(returnDate).get("eng");
        String dateExpectedId = getDateSearchFormLocatorText(departureDate).get("id") + " - " + getDateSearchFormLocatorText(returnDate).get("id");

        logger.log("date expected eng: " + dateExpectedEng);
        logger.log("date expected id: " + dateExpectedId);

        boolean isCorrectDateShown = dateActual.toLowerCase().contains(dateExpectedEng.toLowerCase()) || dateActual.toLowerCase().contains(dateExpectedId.toLowerCase());
        logger.log("correct departure selected: " + isCorrectDateShown);
        driverTools.switchToNative();
        return isCorrectDateShown;
    }

    public boolean isDateSelected(String departureDate){
        driverTools.switchToWebViewUrl("bus-travel");
        WebElement dateElement = driverTools.waitForElementToAppear(CALENDAR_TEXT).orElseThrow();
        String dateActual = driverTools.getText(dateElement);

        String dateExpectedEng = getDateSearchFormLocatorText(departureDate).get("eng").split(", ")[1];
        String dateExpectedId = getDateSearchFormLocatorText(departureDate).get("id").split(", ")[1];

        logger.log("date actual: " + dateActual);
        logger.log("date expected eng: " + dateExpectedEng);
        logger.log("date expected id: " + dateExpectedId);

        boolean isCorrectDateShown = dateActual.toLowerCase().contains(dateExpectedEng.toLowerCase()) || dateActual.toLowerCase().contains(dateExpectedId.toLowerCase());
        logger.log("correct departure selected: " + isCorrectDateShown);
        driverTools.switchToNative();
        return isCorrectDateShown;
    }

    public boolean isPaxSelected(int passenger){
        driverTools.switchToWebViewUrl("bus-travel");
        WebElement paxElement = driverTools.waitForElementToAppear(PAX_TEXT).orElseThrow();
        String paxActual = driverTools.getText(paxElement);

        logger.log("pax expected: " + passenger);
        logger.log("pax actual: " + paxActual);

        boolean isCorrectPaxSelected = paxActual.contains(passenger+" ");
        logger.log("correct pax selected: " + isCorrectPaxSelected);
        driverTools.switchToNative();
        return isCorrectPaxSelected;
    }

    private HashMap<String,String> getDateSearchFormLocatorText(String date){
        String dayNameEng = CoreFunctions.getDayFromDate(date, "d MMMM yyyy");
        String dayNameId = CoreFunctions.dayEngToId(dayNameEng);

        dayNameEng = dayNameEng.substring(0,3);
        dayNameId = dayNameId.substring(0,3);

        String monthEng = CoreFunctions.getSectionOfString(date," ", 1);
        String monthId = CoreFunctions.monthEngToId(monthEng);

        monthEng = monthEng.substring(0,3);
        monthId = monthId.substring(0,3);
        if(monthId.equals("Agu")){ // because in bahasa the August is termed as "Agustus" but in short form written as "Agt"
            monthId="Agt";
        }

        String day = CoreFunctions.getSectionOfString(date," ", 0);

        String locatorTextEng = dayNameEng + ", " + day +" "+ monthEng;
        String locatorTextId = dayNameId + ", " + day +" "+ monthId;

        HashMap<String,String> locatorText = new HashMap<>();
        locatorText.put("eng",locatorTextEng);
        locatorText.put("id",locatorTextId);
        return locatorText;
    }

    /**
     * Verifies that the passenger count was correctly selected
     * @param passenger Expected passenger count
     */
    protected void verifyPassengerSelection(int passenger) {
        driverTools.switchToWebViewUrl("bus-travel");
        WebElement paxElement = driverTools.waitForElementToAppear(PAX_TEXT, 5).orElseThrow();
        String paxActual = driverTools.getText(paxElement);
        logger.log("Passenger count after selection: " + paxActual);

        String expectedPassengerText = passenger + " Seat";
        String expectedPassengerTextAlt = passenger + " Kursi"; // Indonesian format

        boolean isCorrect = paxActual.equals(expectedPassengerText) ||
                paxActual.equals(expectedPassengerTextAlt) ||
                paxActual.contains(String.valueOf(passenger));

        if (!isCorrect) {
            throw new RuntimeException("Passenger count mismatch. Expected: " + expectedPassengerText +
                    " or " + expectedPassengerTextAlt + ", Actual: " + paxActual);
        }

        logger.log("Passenger count successfully set to " + passenger);
        Assertion.assertThat("pax count set correctly",paxActual,anyOf(containsString(expectedPassengerText),containsString (expectedPassengerTextAlt), containsString(String.valueOf(passenger))));
        driverTools.switchToNative();
    }
}
