package com.tiket.web.page.flight.landingpage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.model.Platform;
import com.tiket.web.WebPage;
import com.tiket.web.page.flight.searchresultpage.FlightSearchResultPageWeb;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.core.Is.is;

public abstract class FlightLandingPageWeb<T extends WebDriver> extends WebPage<T> {
    public FlightLandingPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FlightLandingPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new FlightLandingPageDWeb<>(session);
            }
            case AMWEB, IMWEB -> {
                return new FlightLandingPageMWeb<>(session);
            }
            default -> throw new UnsupportedOperationException("Platform not supported");
        }
    }

private static final Locator SELECTED_DEPARTURE_DATE = new Locator("SELECTED_DEPARTURE_DATE", By.xpath("//p[normalize-space()='Departure' or normalize-space()='Pergi' or normalize-space()='Keberangkatan']/following-sibling::span"));
private static final Locator SELECTED_RETURN_DATE = new Locator("SELECTED_RETURN_DATE", By.xpath("//p[normalize-space()='Return' or normalize-space()='Pulang' or normalize-space()='Kembali']/following-sibling::span"));
    private static final Locator TRIP_TOGGLE = new Locator("TRIP_TOGGLE", new By.ByXPath("//*[contains(@class,'SearchForm')]//*[contains(@class,'Toggle_knob')]"));
    protected static final Locator DEPART_CALENDAR = new Locator("DEPART_CALENDAR", new By.ByXPath("//*[contains(@class,'SearchForm_date_selector') or normalize-space()='Departure' or normalize-space()='Pergi']"));
    private static final Locator DESTINATION_LOCATION = new Locator("DESTINATION_LOCATION", new By.ByXPath("//*[@data-testid='clickable-arrival-input']"));
    private static final Locator RETURN_CALENDAR = new Locator("RETURN_CALENDAR", new By.ByXPath("(//*[contains(@class,'SearchForm_date_selector') or contains(@class,'SearchForm_departure_return')])[2]"));
    private static final Locator ORIGIN_LOCATION = new Locator("ORIGIN_LOCATION", new By.ByXPath("//*[@data-testid='clickable-departure-input']"));
    private static final Locator INPUT_LOCATION = new Locator("INPUT_LOCATION",new By.ByXPath("//div[contains(@class,'SearchBox_placeholder')]//input[contains(@placeholder,'city or airport') or contains(@placeholder,'Masukkan nama kota atau bandara')]"));
    private static final Locator DESTINATION_INPUT_LOCATION = new Locator("DESTINATION_INPUT_LOCATION",
            new By.ByXPath("//*[self::section or self::div][.//h2[contains(normalize-space(.), 'Going Anywhere?') or contains(normalize-space(.), 'Mau ke Mana?')]]//div[contains(@class,'SearchBox_placeholder')]//input[contains(@placeholder,'city or airport') or contains(@placeholder,'Masukkan nama kota atau bandara')]"));
    private static final Locator SELECTED_DATE = new Locator("SELECTED_DATE", new By.ByXPath("//*[contains(@class,'Day_day_selected')]//span[contains(@class,'Day_day_number') and contains(@class,'Day_day_number')]//span"));
    private static final Locator PREVIOUS_MONTH_BTN = new Locator("PREVIOUS_MONTH_BTN", new By.ByXPath("//*[contains(@class,'CalendarDesktop_prev_button')]"));
    private static final Locator NEXT_MONTH_BTN = new Locator("NEXT_MONTH_BTN", new By.ByXPath("//*[contains(@class,'CalendarDesktop_next_button')]"));
    private static final Locator ADD_PASSENGER = new Locator("ADD_PASSENGER", new By.ByXPath("//*[contains(@class,'SearchForm_passenger_picker')]"));
    private static final Locator SAVE_BUTTON_PASSENGER = new Locator("SAVE_BUTTON_PASSENGER", new By.ByXPath("//*[contains(@class,'PassengerForm_btn_save')]"));
    private static final Locator ECONOMY_CLASS = new Locator("ECONOMY_CLASS", new By.ByXPath("//*[contains(@class,'PassengerForm_cabin__')]//span[text()='Economy' or text()='Ekonomi']"));
    private static final Locator ECONOMY_CLASS_SELECTED = new Locator("ECONOMY_CLASS_SELECTED", new By.ByXPath("//*[contains(@class,'PassengerForm_cabin__') and contains(@class,'Chip_active')]//span[text()='Economy' or text()='Ekonomi']"));
    private static final Locator PREMIUM_ECONOMY_CLASS = new Locator("PREMIUM_ECONOMY_CLASS", new By.ByXPath("//*[contains(@class,'PassengerForm_cabin__')]//span[text()='Premium Economy' or text()='Premium Ekonomi']"));
    private static final Locator PREMIUM_ECONOMY_CLASS_SELECTED = new Locator("PREMIUM_ECONOMY_CLASS_SELECTED", new By.ByXPath("//*[contains(@class,'PassengerForm_cabin__') and contains(@class,'Chip_active')]//span[text()='Premium Economy' or text()='Premium Ekonomi']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[contains(@class,'SearchForm_btn_submit')]"));
    private static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON", new By.ByXPath("//button[@data-testid='modal-close-button']"));
    private static final Locator POPULAR_CITIES_BTN = new Locator("POPULAR_CITIES_BTN", new By.ByXPath("//button[contains(@class,'AirportForm_chip_popular_cities')]"));
    private static final Locator SAVE_DATE = new Locator("SAVE_DATE", new By.ByXPath("//*[contains(@class,'DatePicker_confirmation_date')]/following-sibling::button"));
    private static final Locator FIRST_HIGHLIGHTED_SEARCH_RESULT = new Locator("FIRST_HIGHLIGHTED_SEARCH_RESULT", AppiumBy.xpath("(//span[contains(@class, 'highlighted')])[1]"));
    private static final Locator SAVE_PASSENGER_BUTTON = new Locator("SAVE_PASSENGER_BUTTON", new By.ByXPath("//button[contains(@class,'SearchForm_btn_submit')]"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", By.xpath("//button[.//span[normalize-space()='Continue' or normalize-space()='Lanjutkan']]"));
    private static final Locator DEPARTURE_BOX = new Locator("DEPARTURE_BOX", By.xpath("//h2[contains(normalize-space(.), 'Select a City or Airport') or contains(normalize-space(.), 'Pilih Kota atau Bandara')]"));
    private static final Locator DESTINATION_BOX = new Locator("DESTINATION_BOX", By.xpath("//h2[contains(normalize-space(.), 'Going Anywhere?') or contains(normalize-space(.), 'Mau ke Mana?')]"));
    private static final Locator GOT_IT_BUTTON = new Locator("GOT_IT_BUTTON", By.xpath("//button[.//span[normalize-space()='Got it!' or normalize-space()='Mengerti!']]"));
    private static final Locator DEPART_DATE_CALENDAR = new Locator("DEPART_DATE_CALENDAR", new By.ByXPath("//*[contains(@class,'SearchForm_departure_return') and (normalize-space(.)='Departure' or normalize-space(.)='Pergi' or normalize-space(.)='Keberangkatan')]"));

    /** Root of the destination airport modal (“Going Anywhere?” / “Mau ke Mana?”). */
    private static final String DESTINATION_MODAL_ROOT =
            "//*[self::section or self::div][.//h2[contains(normalize-space(.), 'Going Anywhere?') or contains(normalize-space(.), 'Mau ke Mana?')]]";

    private static String exactAirportCodeTextEquals(String airportCode) {
        return "translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='"
                + airportCode.toLowerCase() + "'";
    }

    public void clickSaveButton() {
        driverTools.waitForElementToAppear(SAVE_PASSENGER_BUTTON);
        driverTools.click(SAVE_PASSENGER_BUTTON);
    }
    private static final Locator LOWEST_PRICE_GUARANTEE_TAG = new Locator("LOWEST_PRICE_GUARANTEE_TAG", new By.ByXPath("//*[contains(@class,'GradientBanner_content')]"));

    public void selectOrigin(String origin) {
        driverTools.clickWithJavascriptExecutor(ORIGIN_LOCATION);
        try {
            driverTools.click(INPUT_LOCATION);
        } catch (Exception e) {
            logger.log("Exception while scrolling to input location: " + e.getMessage());
            driverTools.click(CLOSE_BUTTON);
            driverTools.clickWithJavascriptExecutor(ORIGIN_LOCATION);
            driverTools.waitForElementToPresence(CLOSE_BUTTON).orElseThrow();
        }
        driverTools.click(INPUT_LOCATION);
        driverTools.waitForElementToPresence(POPULAR_CITIES_BTN);
        logger.log("Searching origin: " + origin);
        if (origin.contains("/") || origin.length() <= 4) {
            selectOriginViaAirPortCode(origin);
        } else {
            driverTools.sendKeys(INPUT_LOCATION, origin);
            driverTools.click(getLocatorLocationSearch(CoreFunctions.capitalize(origin)));
        }
        driverTools.waitForElementToDisappear(DEPARTURE_BOX);
    }

    public boolean isSearchResultPresent(String origin) {
        driverTools.clickWithJavascriptExecutor(ORIGIN_LOCATION);
        try {
            driverTools.click(INPUT_LOCATION);
        } catch (Exception e) {
            logger.log("Exception while scrolling to input location: " + e.getMessage());
            driverTools.click(CLOSE_BUTTON);
            driverTools.clickWithJavascriptExecutor(ORIGIN_LOCATION);
            driverTools.waitForElementToPresence(CLOSE_BUTTON).orElseThrow();
            driverTools.scrollElementToCenter(INPUT_LOCATION);
        }
        driverTools.click(INPUT_LOCATION);
        driverTools.waitForElementToPresence(POPULAR_CITIES_BTN);
        logger.log("Searching origin: " + origin);
        driverTools.sendKeys(INPUT_LOCATION, origin);
        return driverTools.isElementPresent(getLocatorLocationSearch(CoreFunctions.capitalize(origin)));
    }

    public String getFirstHighlightedResult() {
        return driverTools.getText(FIRST_HIGHLIGHTED_SEARCH_RESULT).orElseThrow();
    }

    private Locator getLocatorLocationSearch(String location) {
        return new Locator("LOCATOR_SEARCH_SUGGESTION", new By.ByXPath("(//*[contains(@class,'AirportForm_airport')]//span[contains(text(),'" + location + "')])[1]"));
    }

    private Locator getLocatorBasedOnAirportCode(String airportCode) {
        return new Locator("LOCATOR_AIRPORT_CODE", new By.ByXPath(
                "//*[contains(@class,'AirportForm_airport_code') and " + exactAirportCodeTextEquals(airportCode) + "]"));
    }

    private Locator getDestinationLocatorBasedOnAirportCode(String airportCode) {
        return new Locator("DESTINATION_LOCATOR_AIRPORT_CODE",
                new By.ByXPath(DESTINATION_MODAL_ROOT + "//*[contains(@class,'AirportForm_airport_code') and "
                        + exactAirportCodeTextEquals(airportCode) + "]"));
    }

    private Locator getDestinationFirstHighlightedResult() {
        return new Locator("DESTINATION_FIRST_HIGHLIGHTED_RESULT",
                new By.ByXPath("(//*[self::section or self::div][.//h2[contains(normalize-space(.), 'Going Anywhere?') or contains(normalize-space(.), 'Mau ke Mana?')]]//span[contains(@class,'highlighted')])[1]"));
    }

    private Locator getDestinationPopularCityByKeyword(String keyword) {
        return new Locator("DESTINATION_POPULAR_CITY_BY_KEYWORD",
                new By.ByXPath("//*[self::section or self::div][.//h2[contains(normalize-space(.), 'Going Anywhere?') or contains(normalize-space(.), 'Mau ke Mana?')]]//button[contains(@class,'AirportForm_chip_popular_cities')][contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'"
                        + keyword.toLowerCase() + "')]"));
    }

    private void sendKeysToAirportSearchInput(String keys) {
        sendKeysToAirportSearchInput(keys, INPUT_LOCATION);
    }

    private void sendKeysToAirportSearchInput(String keys, Locator inputLocator) {
        driverTools.waitForElementToBeClickable(inputLocator).orElseThrow();
        driverTools.clickWithJavascriptExecutor(inputLocator);
        try {
            driverTools.sendKeys(inputLocator, keys);
        } catch (StaleElementReferenceException e) {
            logger.log("Input became stale during sendKeys; retrying with JS value + events: " + e.getMessage());
            driverTools.waitForElementToBeClickable(inputLocator).orElseThrow();
            driverTools.clickWithJavascriptExecutor(inputLocator);
            driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(inputLocator, keys);
        } catch (ElementNotInteractableException e) {
            logger.log("Input locator not interactable for sendKeys; using JS value + events: " + e.getMessage());
            driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(inputLocator, keys);
        }
    }
    public void selectOriginViaAirPortCode(String origin) {
        if (origin.length() <= 4) {
            sendKeysToAirportSearchInput(origin);
            driverTools.click(getLocatorBasedOnAirportCode(origin.toUpperCase()));
        } else {
            String airportName = origin.split("/")[0];
            String airportCode = origin.split("/")[1].toUpperCase();
            sendKeysToAirportSearchInput(airportName);
            driverTools.minimizeKeyboardMweb();
            driverTools.click(getLocatorBasedOnAirportCode(airportCode.toUpperCase()));
        }
    }

    public void selectDestination(String destination) {
        fillDestination(destination);
        driverTools.waitForElementToDisappear(DESTINATION_BOX);
        if(driverTools.isElementPresent(DESTINATION_BOX)) {
            driverTools.clickWithJavascriptExecutor(CLOSE_BUTTON);
            fillDestination(destination);
        }
    }

    public void fillDestination(String destination){
        driverTools.dummyWait(2);
        driverTools.clickWithJavascriptExecutor(DESTINATION_LOCATION);
        try {
            driverTools.waitForElementToPresence(DESTINATION_INPUT_LOCATION, 3);
            driverTools.clickWithJavascriptExecutor(DESTINATION_INPUT_LOCATION);
        } catch (Exception e) {
            logger.log("Exception while scrolling to input location: " + e.getMessage());
            driverTools.clickWithJavascriptExecutor(CLOSE_BUTTON);
            driverTools.clickWithJavascriptExecutor(DESTINATION_LOCATION);
            driverTools.waitForElementToPresence(DESTINATION_INPUT_LOCATION, 3);
            driverTools.clickWithJavascriptExecutor(DESTINATION_INPUT_LOCATION);
        }
        driverTools.waitForElementToPresence(POPULAR_CITIES_BTN);
        logger.log("Searching destination: " + destination);
        if (destination.contains("/") || destination.length() <= 4) {
            selectDestinationViaAirPortCode(destination);
        } else {
            sendKeysToAirportSearchInput(destination, DESTINATION_INPUT_LOCATION);
            driverTools.clickWithJavascriptExecutor(getLocatorLocationSearch(CoreFunctions.capitalize(destination)));
        }
    }

    public void selectDestinationViaAirPortCode(String destination) {
        if (destination.length() <= 4) {
            sendKeysToAirportSearchInput(destination, DESTINATION_INPUT_LOCATION);
            waitForDestinationModalAirportSuggestions(12);
            clickDestinationAirportCode(destination.toUpperCase());
        } else {
            String airportName = destination.split("/")[0];
            String airportCode = destination.split("/")[1].toUpperCase();
            sendKeysToAirportSearchInput(airportName, DESTINATION_INPUT_LOCATION);
            driverTools.minimizeKeyboardMweb();
            waitForDestinationModalAirportSuggestions(12);
            clickDestinationAirportCode(airportCode);
        }
    }

    private Locator destinationModalAnyAirportRow() {
        return new Locator("DESTINATION_MODAL_ANY_AIRPORT_ROW",
                new By.ByXPath(DESTINATION_MODAL_ROOT + "//*[contains(@class,'AirportForm_airport')]"));
    }

    private void waitForDestinationModalAirportSuggestions(int seconds) {
        driverTools.waitForElementToPresence(destinationModalAnyAirportRow(), seconds);
    }

    private Locator getDestinationFlexibleAirportRow(String airportCode) {
        return new Locator("DESTINATION_FLEXIBLE_AIRPORT_ROW",
                new By.ByXPath(DESTINATION_MODAL_ROOT + "//*[contains(@class,'AirportForm_airport')][.//*[contains(@class,'AirportForm_airport_code') and "
                        + exactAirportCodeTextEquals(airportCode) + "]]"));
    }

    private Locator getGlobalFlexibleAirportRow(String airportCode) {
        return new Locator("GLOBAL_FLEXIBLE_AIRPORT_ROW",
                new By.ByXPath("(//*[contains(@class,'AirportForm_airport')][.//*[contains(@class,'AirportForm_airport_code') and "
                        + exactAirportCodeTextEquals(airportCode) + "]])[1]"));
    }

    private Locator getDestinationScopedLocationSearch(String locationFragment) {
        return new Locator("DESTINATION_LOCATOR_SEARCH_SUGGESTION",
                new By.ByXPath("(" + DESTINATION_MODAL_ROOT + "//*[contains(@class,'AirportForm_airport')]//span[contains(text(),'"
                        + locationFragment + "')])[1]"));
    }

    private Optional<String> getExpandedSearchQueryForAirportCode(String airportCode) {
        return switch (airportCode.toUpperCase()) {
            case "KUL" -> Optional.of("Kuala Lumpur");
            case "SIN" -> Optional.of("Singapore");
            case "BKK" -> Optional.of("Bangkok");
            default -> Optional.empty();
        };
    }

    private Optional<String> getCityNameTokenForSuggestionClick(String airportCode) {
        return switch (airportCode.toUpperCase()) {
            case "KUL" -> Optional.of("Kuala");
            case "SIN" -> Optional.of("Singapore");
            case "BKK" -> Optional.of("Bangkok");
            default -> Optional.empty();
        };
    }

    private void clickDestinationAirportCode(String airportCode) {
        String codeUpper = airportCode.toUpperCase();
        for (int attempt = 1; attempt <= 2; attempt++) {
            if (attempt == 2) {
                Optional<String> expanded = getExpandedSearchQueryForAirportCode(codeUpper);
                if (expanded.isEmpty()) {
                    break;
                }
                logger.log("Retrying destination search with expanded query: " + expanded.get());
                driverTools.clearWithJsExecutor(DESTINATION_INPUT_LOCATION);
                sendKeysToAirportSearchInput(expanded.get(), DESTINATION_INPUT_LOCATION);
                waitForDestinationModalAirportSuggestions(15);
            }
            if (trySelectDestinationForAirportCode(codeUpper)) {
                return;
            }
        }
        throw new RuntimeException("No destination option found for airport code: " + codeUpper);
    }

    public boolean isSelectedDatesSame(String expectedDepartureDate, String expectedReturnDate) {
        String actualDepartureDate = driverTools.getText(SELECTED_DEPARTURE_DATE).orElseThrow();
        String actualReturnDate = driverTools.getText(SELECTED_RETURN_DATE).orElseThrow();

        String expectedDeparture = formatDateForDisplay(expectedDepartureDate);
        String expectedReturn = formatDateForDisplay(expectedReturnDate);

        logger.log("Expected departure date: " + expectedDeparture);
        logger.log("Actual departure date: " + actualDepartureDate);
        logger.log("Expected return date: " + expectedReturn);
        logger.log("Actual return date: " + actualReturnDate);

        return actualDepartureDate.equals(expectedDeparture)
                && actualReturnDate.equals(expectedReturn);
    }

    private String formatDateForDisplay(String date) {
        LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH));
        return parsedDate.format(DateTimeFormatter.ofPattern("EEE, d MMM", Locale.ENGLISH));
    }

    private boolean trySelectDestinationForAirportCode(String airportCode) {
        Locator destinationAirportCode = getDestinationLocatorBasedOnAirportCode(airportCode);
        if (driverTools.waitForElementToPresence(destinationAirportCode, 8).isPresent()) {
            safeClickDestinationOption(destinationAirportCode);
            return true;
        }

        Locator fallbackAirportCode = getLocatorBasedOnAirportCode(airportCode);
        if (driverTools.waitForElementToPresence(fallbackAirportCode, 6).isPresent()) {
            safeClickDestinationOption(fallbackAirportCode);
            return true;
        }

        Locator destFlex = getDestinationFlexibleAirportRow(airportCode);
        if (driverTools.waitForElementToPresence(destFlex, 6).isPresent()) {
            safeClickDestinationOption(destFlex);
            return true;
        }

        Locator globalFlex = getGlobalFlexibleAirportRow(airportCode);
        if (driverTools.waitForElementToPresence(globalFlex, 5).isPresent()) {
            safeClickDestinationOption(globalFlex);
            return true;
        }

        logger.log("Airport code option not found for " + airportCode + ". Trying fallback destination selectors.");

        Optional<String> cityToken = getCityNameTokenForSuggestionClick(airportCode);
        if (cityToken.isPresent()) {
            Locator suggestion = getDestinationScopedLocationSearch(cityToken.get());
            if (driverTools.waitForElementToPresence(suggestion, 6).isPresent()) {
                safeClickDestinationOption(suggestion);
                return true;
            }
        }

        Locator highlightedResult = getDestinationFirstHighlightedResult();
        if (driverTools.waitForElementToPresence(highlightedResult, 5).isPresent()) {
            safeClickDestinationOption(highlightedResult);
            return true;
        }

        if (driverTools.waitForElementToPresence(FIRST_HIGHLIGHTED_SEARCH_RESULT, 3).isPresent()) {
            safeClickDestinationOption(FIRST_HIGHLIGHTED_SEARCH_RESULT);
            return true;
        }

        for (String keyword : getAirportCodeFallbackKeywords(airportCode)) {
            Locator popularCityLocator = getDestinationPopularCityByKeyword(keyword);
            if (driverTools.waitForElementToPresence(popularCityLocator, 4).isPresent()) {
                safeClickDestinationOption(popularCityLocator);
                return true;
            }
        }

        return false;
    }

    private void safeClickDestinationOption(Locator locator) {
        try {
            driverTools.clickWithJavascriptExecutor(locator);
        } catch (Exception e) {
            logger.log("JS click failed for destination option, using regular click: " + e.getMessage());
            driverTools.click(locator);
        }
    }

    private ArrayList<String> getAirportCodeFallbackKeywords(String airportCode) {
        ArrayList<String> keywords = new ArrayList<>();
        keywords.add(airportCode.toLowerCase());
        switch (airportCode.toUpperCase()) {
            case "SUB" -> keywords.add("surabaya");
            case "CGK" -> keywords.add("jakarta");
            case "DPS" -> keywords.add("bali");
            case "KNO" -> keywords.add("medan");
            case "UPG" -> keywords.add("makassar");
            case "YIA" -> keywords.add("yogyakarta");
            case "MDC" -> keywords.add("manado");
            case "BPN" -> keywords.add("balikpapan");
            case "BTH" -> keywords.add("batam");
            case "PLM" -> keywords.add("palembang");
            case "KUL" -> {
                keywords.add("kuala lumpur");
                keywords.add("kuala");
            }
            default -> {
            }
        }
        return keywords;
    }

    public boolean isRoundTripToggleOn() {
        return driverTools.isElementDisplayed(RETURN_CALENDAR);
    }

    public void toggleTripType(String tripType) {
        tripType = tripType.toLowerCase().replace("_", "").replace(" ", "");
        boolean isRoundTrip = isRoundTripToggleOn();
        logger.log("Toggling trip type: " + tripType + ", current round trip: " + isRoundTrip);
        switch (tripType) {
            case "roundtrip" -> {
                if (!isRoundTrip) {
                    driverTools.clickWithJavascriptExecutor(TRIP_TOGGLE);
                }
                driverTools.waitForElementToAppear(RETURN_CALENDAR).orElseThrow();
            }
            case "oneway" -> {
                if (isRoundTrip) {
                    driverTools.clickWithJavascriptExecutor(TRIP_TOGGLE);
                }
                driverTools.waitForElementToDisappear(RETURN_CALENDAR);
            }
            default -> throw new NotImplementedException("Not implemented for trip type: " + tripType);
        }
    }

    public void addPassenger(Integer adult, Integer child, Integer infant) {
        driverTools.waitForElementToAppear(ADD_PASSENGER, 15);
        driverTools.clickWithJavascriptExecutor(ADD_PASSENGER);
        logger.log("adding adult: " + adult);
        addPassenger(adult, "adult");
        logger.log("adding child: " + child);
        addPassenger(child, "child");
        logger.log("adding infant: " + infant);
        addPassenger(infant, "infant");
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON_PASSENGER);
    }

    private Locator getPassengerCountLocator(int passengerIndex, String type) {
        return new Locator("Passenger Count " + type.toUpperCase(), new By.ByXPath("(//input[contains(@class,'QuantityEditor_quantity_input')])[" + passengerIndex + "]"));
    }

    private Locator getPassengerAddButtonLocator(int index, String type) {
        return new Locator("Passenger Add Subtract " + type.toUpperCase(), new By.ByXPath("(//*[contains(@class,'PassengerForm_counter')])[" + index + "]//*[contains(@class,'QuantityEditor_plus_icon')]//parent::button[contains(@class,'QuantityEditor_operation_button')]"));
    }

    private Locator getPassengerSubtractButtonLocatorButtonLocator(int index, String type) {
        return new Locator("Passenger Add Subtract " + type.toUpperCase(), new By.ByXPath("(//*[contains(@class,'PassengerForm_counter')])[" + index + "]//*[contains(@class,'QuantityEditor_minus_icon')]//parent::button[contains(@class,'QuantityEditor_operation_button')]"));
    }

    private int getIndexOfPassengerType(String passengerType) {
        return switch (passengerType) {
            case "adult" -> 1;
            case "child" -> 2;
            default -> 3;
        };
    }

    private void addPassenger(int amount, String passengerType) {

        int passengerIndex = getIndexOfPassengerType(passengerType);

        Locator count = getPassengerCountLocator(passengerIndex, passengerType);
        Locator increasePassenger = getPassengerAddButtonLocator(passengerIndex, passengerType);
        Locator decreasePassenger = getPassengerSubtractButtonLocatorButtonLocator(passengerIndex, passengerType);

        WebElement countElement = driverTools.waitForElementToAppear(count).orElseThrow();
        int currentCount = Integer.parseInt(driverTools.getValueAttribute(countElement, "value"));
        int toAdd = amount - currentCount;
        logger.log("current count: " + currentCount + " to add: " + toAdd);

        boolean increase = true;
        if (toAdd < 0) {
            increase = false;
            toAdd *= -1;
        }

        for (int i = 0; i < toAdd; i++) {
            logger.log("loop to increase/decrease passenger count");
            if (increase) {
                logger.log("increase count by 1");
                driverTools.clickWithJavascriptExecutor(increasePassenger);
            } else {
                logger.log("decrease count by 1");
                driverTools.clickWithJavascriptExecutor(decreasePassenger);
            }
        }

        currentCount = Integer.parseInt(driverTools.getValueAttribute(count, "value"));

        Assertion.assertThat("Correct Passenger Amount Added",
                currentCount == amount, is(true));
    }

    public void selectFlightClass(String flightClass) {
        driverTools.click(ADD_PASSENGER);
        logger.log("Selecting flight class: " + flightClass);
        if (!isAlreadySelectedClass(flightClass)) {
            switch (flightClass.toLowerCase()) {
                case "economy":
                    driverTools.click(ECONOMY_CLASS);
                    driverTools.waitForElementToAppear(ECONOMY_CLASS_SELECTED).orElseThrow();
                    break;
                case "premium economy":
                    driverTools.click(PREMIUM_ECONOMY_CLASS);
                    driverTools.waitForElementToAppear(PREMIUM_ECONOMY_CLASS_SELECTED).orElseThrow();
                    break;
                default:
                    throw new IllegalArgumentException("flight class: " + flightClass + " not implemented");
            }
        }
        driverTools.click(SAVE_BUTTON_PASSENGER);
    }

    private boolean isAlreadySelectedClass(String flightClass) {
        return switch (flightClass.toLowerCase()) {
            case "economy" -> driverTools.isElementDisplayed(ECONOMY_CLASS_SELECTED);
            case "premium economy" -> driverTools.isElementDisplayed(PREMIUM_ECONOMY_CLASS_SELECTED);
            default -> throw new IllegalArgumentException("flight class: " + flightClass + " not implemented");
        };
    }

    public void selectDate(String dateToSelect, String returnDate) {
        driverTools.waitForElementToPresence(DEPART_CALENDAR);
        driverTools.clickWithJavascriptExecutor(DEPART_CALENDAR);
        logger.log("Departing on: " + dateToSelect);
        clickDate(dateToSelect);
        logger.log("Departing on: " + returnDate);
        clickDate(returnDate);
        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            driverTools.clickWithJavascriptExecutor(SAVE_DATE);
        }
    }

    public void selectDate(String dateToSelect) {
        driverTools.waitForElementToPresence(DEPART_CALENDAR);
        driverTools.clickWithJavascriptExecutor(DEPART_CALENDAR);
        logger.log("Departing on: " + dateToSelect);
        clickDate(dateToSelect);
        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            driverTools.clickWithJavascriptExecutor(SAVE_DATE);
        }
    }

    private int getDirection(String dateToSelect) {
        WebElement selectedDateElement = driverTools.waitForElementToAppear(SELECTED_DATE).orElseThrow();
        String selectedDate = selectedDateElement.getAttribute("aria-label");

        logger.log("date to select : " + dateToSelect);
        logger.log("selected date : " + selectedDate);

        String[] dateToSelectArr = dateToSelect.split(" ");
        String dateToSelectMonth = dateToSelectArr[1];
        int dateToSelectYear = Integer.parseInt(dateToSelectArr[2]);

        String[] selectedDateArr = selectedDate.split(" ");
        String selectedDateMonth = selectedDateArr[1];
        int SelectedDateYear = Integer.parseInt(selectedDateArr[2]);

        int selectedMonthNum = CoreFunctions.monthToMonthNumber(selectedDateMonth);
        logger.log("selected month number: " + selectedMonthNum);
        int dateToSelectMonthNum = CoreFunctions.monthToMonthNumber(dateToSelectMonth);
        logger.log("date to select month number: " + dateToSelectMonthNum);

        int yearDif = dateToSelectYear - SelectedDateYear;
        if (yearDif == 0) {
            int monthDif = dateToSelectMonthNum - selectedMonthNum;
            return monthDif < 0 ? -1 : 1;
        }
        return yearDif < 0 ? -1 : 1;
    }

    protected void clickDate(String date) {
        int direction = getDirection(date);
        logger.log("direction : " + direction);

        String year = CoreFunctions.getSectionOfString(date, " ", 2);
        String monthEng = CoreFunctions.getSectionOfString(date, " ", 1);
        String monthId = CoreFunctions.monthEngToId(monthEng);
        String day = CoreFunctions.getSectionOfString(date, " ", 0);
        Locator dateLocator = getDateLocator(year, monthId, monthEng, day);

        for (int i = 10; i > 0; i--) {
            boolean isDateDisplayed = driverTools.isElementDisplayed(dateLocator);
            if (!isDateDisplayed) {
                if (direction == -1) {
                    driverTools.clickWithJavascriptExecutor(PREVIOUS_MONTH_BTN);
                } else {
                    driverTools.clickWithJavascriptExecutor(NEXT_MONTH_BTN);
                }
            } else {
                driverTools.clickWithJavascriptExecutor(dateLocator);
                break;
            }
        }
    }

    private Locator getDateLocator(String year, String monthId, String monthEng, String day) {
        String monthYearEn = monthEng + " " + year;
        String monthYearId = monthId + " " + year;
        return new Locator("Date Locator", new By.ByXPath("//span[(contains(@aria-label,'" + monthYearEn + "') or contains(@aria-label,'" + monthYearId + "')) and text()='" + day + "']"));
    }

    protected Locator getDateLocator(String dateToSelect) {
        String year = CoreFunctions.getSectionOfString(dateToSelect, " ", 2);
        String monthEng = CoreFunctions.getSectionOfString(dateToSelect, " ", 1);
        String monthId = CoreFunctions.monthEngToId(monthEng);
        String day = CoreFunctions.getSectionOfString(dateToSelect, " ", 0);
        return getDateLocator(year, monthId, monthEng, day);
    }

    public void searchFlight() {
        searchFlightAndWaitForSrp();
        driverTools.refreshWebpage();
    }

    /** Search and wait for SRP to fully load without refreshing (keeps analytics events intact). */
    public void searchFlightAndWaitForSrp() {
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        flightSearchResultPage.skipRefund100P();
        flightSearchResultPage.isSRPLoaded();
    }

    /** Search and wait for empty/no-result SRP without refreshing (keeps analytics events intact). */
    public void searchFlightAndWaitForEmptySrp() {
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        flightSearchResultPage.skipRefund100P();
        flightSearchResultPage.isEmptySRPLoaded();
    }

    public boolean isOnFlightPage() {
        driverTools.waitForElementToAppear(DEPART_CALENDAR);
        boolean isDepartCalenderDisplayed = driverTools.isElementDisplayed(DEPART_CALENDAR);
        logger.log("isDepartCalenderDisplayed: " + isDepartCalenderDisplayed);

        boolean isToggleTripDisplayed = driverTools.isElementDisplayed(TRIP_TOGGLE);
        logger.log("isToggleTripDisplayed: " + isToggleTripDisplayed);

        return isDepartCalenderDisplayed && isToggleTripDisplayed;
    }

    public boolean isOriginLocation() {
        return driverTools.waitForElementToAppear(ORIGIN_LOCATION).isPresent();
    }

    public void checkVIFlight(String originAirport, String destinationAirport  ) {
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);

        ArrayList<String> destinations = new ArrayList<>(Arrays.asList(destinationAirport.split("-")));
        ArrayList<String> origins = new ArrayList<>(Arrays.asList(originAirport.split("-")));

        int totalIterations = Math.min(origins.size(), destinations.size());


        for (int i = 0; i < totalIterations; i++) {

            String origin1 = origins.get(i);
            String destination1 = destinations.get(i);
            selectOrigin(origin1);
            selectDestination(destination1);
            searchFlight();

            if (i == totalIterations - 1) {
                break;
            }
            else {
                flightSearchResultPage.backToFlightPage();
                isOriginLocation();
            }
        }
    }

    public void clickContinueButton(){
        driverTools.waitForElementToAppear(CONTINUE_BUTTON);
        driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON);
    }

    public boolean isLowestPriceGuaranteeTagDisplayed(){
        return driverTools.waitForElementsToAppear(LOWEST_PRICE_GUARANTEE_TAG,20).isPresent();
    }

    public void clickLowestPriceGuaranteeTag(){
        driverTools.waitForElementToAppear(LOWEST_PRICE_GUARANTEE_TAG);
        driverTools.clickWithJavascriptExecutor(LOWEST_PRICE_GUARANTEE_TAG);
    }
    public void turnOffRTtoggle(){
        driverTools.waitForElementToAppear(TRIP_TOGGLE);
        driverTools.clickWithJavascriptExecutor(TRIP_TOGGLE);
    }

    public void handleGotItPopup() {
        if (driverTools.isElementDisplayed(GOT_IT_BUTTON)) {
            driverTools.clickWithJavascriptExecutor(GOT_IT_BUTTON);
        }
    }

    public void clickOnCalendarWidgetToOpen() {
        logger.log("Opening calendar widget...");
        driverTools.clickWithJavascriptExecutor(DEPART_DATE_CALENDAR);
    }

}