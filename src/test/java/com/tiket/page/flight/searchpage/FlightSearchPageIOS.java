package com.tiket.page.flight.searchpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.flight.FlightPage;
import com.tiket.page.flight.searchresultpage.FlightSearchResultPage;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class FlightSearchPageIOS<T extends WebDriver> extends FlightSearchPage<T> {

    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.accessibilityId("tds_ic_back"));
    public static final Locator ORIGIN = new Locator("Origin", AppiumBy.name("topSearchForm"));
    public static final Locator DESTINATION = new Locator("Destination", AppiumBy.name("bottomSearchForm"));
    public static final Locator TOGGLE_SELECTED = new Locator("TOGGLE_SELECTED", AppiumBy.id("toggle_rt (selected)"));
    public static final Locator TOGGLE_SELECT = new Locator("TOGGLE_SELECT", AppiumBy.id("toggle_rt"));
    public static final Locator AIRPORT_FIELD = new Locator("Airport Field", AppiumBy.xpath("//XCUIElementTypeTextField"));
    public static final Locator ADD_ADULT = new Locator("ADD_ADULT", AppiumBy.xpath("//*[contains(@label,'Adult') or contains(@label,'Dewasa')]//following-sibling::*//*[@name='add_btn']"));
    public static final Locator ADD_CHILD = new Locator("ADD_CHILD", AppiumBy.xpath("//*[contains(@name,'Child') or contains(@name,'Anak')]//following-sibling::*//*[@name='add_btn']"));
    public static final Locator ADD_INFANT = new Locator("ADD_INFANT", AppiumBy.xpath("//*[contains(@name,'Infant') or contains(@name,'Bayi')]//following-sibling::*//*[@name='add_btn']"));
    public static final Locator MINUS_ADULT = new Locator("MINUS_ADULT", AppiumBy.xpath("//*[contains(@label,'Adult') or contains(@label,'Dewasa')]//following-sibling::*//*[@name='subtract_btn']"));
    public static final Locator MINUS_CHILD = new Locator("MINUS_CHILD", AppiumBy.xpath("//*[contains(@name,'Child') or contains(@name,'Anak')]//following-sibling::*//*[@name='subtract_btn']"));
    public static final Locator MINUS_INFANT = new Locator("MINUS_INFANT", AppiumBy.xpath("//*[contains(@name,'Infant') or contains(@name,'Bayi')]//following-sibling::*//*[@name='subtract_btn']"));
    public static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", AppiumBy.id("save_btn"));
    public static final Locator ADD_PASSENGER = new Locator("ADD_PASSENGER", AppiumBy.accessibilityId("select_passenger"));
    public static final Locator FLIGHT_SEARCH_BUTTON = new Locator("FLIGHT_SEARCH_BUTTON", AppiumBy.id("flightSearchButton"));
    public static final Locator BUTTONS = new Locator("BUTTONS", AppiumBy.xpath("//XCUIElementTypeButton[@visible='true']"));
    public static final Locator CLEAR = new Locator("CLEAR",AppiumBy.id("clear_btn"));
    public static final Locator RECENT_SEARCH = new Locator("RECENT_SEARCH",AppiumBy.id("recent_search_flight_0"));
    public static final Locator ECONOMY_CLASS = new Locator("ECONOMY_CLASS", AppiumBy.xpath("(//XCUIElementTypeButton[@name='save_btn']/parent::*//parent::*//parent::*)[1]//XCUIElementTypeStaticText[@name='Economy' or @name='Ekonomi']"));
    public static final Locator PREMIUM_ECONOMY_CLASS = new Locator("PREMIUM_ECONOMY_CLASS", AppiumBy.xpath("(//XCUIElementTypeButton[@name='save_btn']/parent::*//parent::*//parent::*)[1]//XCUIElementTypeStaticText[@name='Premium Economy' or @name='Premium Ekonomi']"));
    public static final Locator ECONOMY_CLASS_SELECTED = new Locator("ECONOMY_CLASS_SELECTED", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Economy (selected)' or @name='Ekonomi (selected)']"));
    public static final Locator PREMIUM_ECONOMY_CLASS_SELECTED = new Locator("PREMIUM_ECONOMY_CLASS_SELECTED", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Premium Economy (selected)' or @name='Premium Ekonomi (selected)']"));
    public static final Locator SELECTED_DATE = new Locator("SELECTED_DATE",AppiumBy.id("tds_calendar_start_date"));
    public static final Locator CALENDAR_SCROLLABLE = new Locator("CALENDAR_SCROLLABLE",AppiumBy.id("tds_calendar_month_rv"));
    public static final Locator LPG_TEXT = new Locator("LPG_TEXT",AppiumBy.xpath("(//*[@type='XCUIElementTypeStaticText' and contains(@name, 'Jaminan Harga Termurah') or contains(@name, 'Lowest Price Guarantee')])"));
    public static final Locator OPEN_FILTERS = new Locator("OPEN_FILTERS",AppiumBy.name("bottomActionItem_filter"));
    public static final Locator OTHER_FILTERS = new Locator("OTHER_FILTERS",AppiumBy.xpath("//*[@name='Others' or @name='Filter Lainnya' or @name='Lainnya' or @name='Other Filters']"));
    public static final Locator SEE_MORE_OTHER_FILTERS = new Locator("SEE_MORE_OTHER_FILTERS",AppiumBy.xpath("//*[@name='btn_see_more_others']"));
    public static final Locator SELF_TRANSFER = new Locator("SELF_TRANSFER",AppiumBy.xpath("//*[@name='filter_group_checkbox_TIKET-VI']"));
    public static final Locator SAVE = new Locator("SAVE",AppiumBy.xpath("(//*[@name='Save' or @name='Simpan'])[1]"));
    public static final Locator SAVE_FILTER = new Locator("SAVE_FILTER",AppiumBy.xpath("//*[@name='see_flights']"));
    public static final Locator CLOSE = new Locator("CLOSE",AppiumBy.xpath("(//*[@name='ic cancel grey'])[2]"));
    public static final Locator CLOSE_AGAIN = new Locator("CLOSE_AGAIN",AppiumBy.xpath("//*[@name='ic cancel grey']"));
    public static final Locator BACK = new Locator("BACK",AppiumBy.xpath("//*[@name='back_btn']"));

    @Getter
    public enum PassengerType {
        ADULT(
                new Locator("Adult Count", AppiumBy.xpath("//*[@name='pass_adult']")),
                ADD_ADULT,
                MINUS_ADULT
        ),
        CHILD(
                new Locator("Child Count", AppiumBy.xpath("//*[@name='pass_child']")),
                ADD_CHILD,
                MINUS_CHILD
        ),
        INFANT(
                new Locator("Infant Count", AppiumBy.xpath("//*[@name='pass_infant']")),
                ADD_INFANT,
                MINUS_INFANT
        );

        private final Locator count;
        private final Locator add;
        private final Locator minus;

        PassengerType(Locator count, Locator add, Locator minus) {
            this.count = count;
            this.add = add;
            this.minus = minus;
        }

    }

    public FlightSearchPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public void openFlightDeeplink() {
        Deeplink flightDeeplink = new Deeplink("Flight",
                "tiket://m.gatotkaca.tiket.com/pesawat",
                "tiket://m.preprod.tiket.com/pesawat",
                "tiket://m.tiket.com/pesawat");
        driverTools.deeplink(flightDeeplink);
        driverTools.switchToNative();
        driverTools.waitForElementToAppear(ORIGIN);
    }

    private Locator getCityLocator(String city) {
        return new Locator("city", AppiumBy.accessibilityId("locationLabel_"+city+"_0"));
    }

    @SneakyThrows
    private Locator getLocatorBasedOnAirportCode(String airportCode){
        return new Locator("LOCATOR_AIRPORT_CODE", AppiumBy.accessibilityId("code_label_"+airportCode.toUpperCase()));
    }

    @Override
    public void selectOrigin(String origin) {
        String methodName = "selectOrigin";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.clickElementByCoordinate(ORIGIN);
            logger.log("Searching origin: " + origin);

            // Fast path: airport code format (e.g. DEL/BOM)
            if (origin.indexOf('/') > 0) {
                selectOriginViaAirPortCode(origin);
                return;
            }

            // Optimized: Wait for field and send keys
            WebElement airportField = driverTools.waitForElementToAppear(AIRPORT_FIELD, 15).orElseThrow();
            driverTools.sendKeys(airportField, origin);

            // Build locator based on input type
            final Locator targetLocator;
            if (origin.length() > 4) {
                String formattedCity = Character.toUpperCase(origin.charAt(0)) + origin.substring(1);
                targetLocator = getCityLocator(formattedCity);
            } else {
                targetLocator = getLocatorBasedOnAirportCode(origin);
            }

            // Optimized: Use WebElement from waitForElementToBeClickable to avoid redundant wait in click()
            WebElement clickableElement = driverTools.waitForElementToBeClickable(targetLocator, 15).orElseThrow();
            // Use WebElement click to avoid redundant waitForElementToBeClickable call
            driverTools.click(clickableElement);

        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void selectOriginViaAirPortCode(String origin) {
        String airportName = origin.split("/")[0];
        String airportCode = origin.split("/")[1].toUpperCase();
        driverTools.waitForElementToAppear(AIRPORT_FIELD, 20);
        driverTools.sendKeys(AIRPORT_FIELD,airportName);
        driverTools.click(getLocatorBasedOnAirportCode(airportCode));
    }

    @Override
    public void selectDestination(String destination) {
        String methodName = "selectDestination";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.click(DESTINATION);
            logger.log("Searching destination: " + destination);
            if(destination.contains("/")){
                selectDestinationViaAirPortCode(destination);
            }else {
                WebElement airportField = driverTools.waitForElementToAppear(AIRPORT_FIELD).orElseThrow();
                driverTools.sendKeys(airportField, destination);
                if (destination.length() > 4) {
                    Locator selectCity = getCityLocator(destination.substring(0, 1).toUpperCase() + destination.substring(1));
                    driverTools.clickElementByCoordinate(selectCity);
                } else {
                    driverTools.click(getLocatorBasedOnAirportCode(destination));
                }
            }
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void selectDestinationViaAirPortCode(String destination) {
        String airportName = destination.split("/")[0];
        String airportCode = destination.split("/")[1].toUpperCase();
        driverTools.sendKeys(AIRPORT_FIELD, airportName);
        driverTools.click(getLocatorBasedOnAirportCode(airportCode));
    }

    @Override
    public void backToHomePage() {
        WebElement backButton = driverTools.waitForElementToAppear(BACK_BUTTON).orElseThrow();
        driverTools.click(backButton);
    }

    @Override
    public void toggleTriptype(String tripType) {
        String methodName = "toggleTriptype";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            boolean current_state = driverTools.waitForElementToAppear(TOGGLE_SELECTED,3).isPresent();
            logger.log("Toggling trip type: " + tripType);
            tripType = tripType.toLowerCase();
            switch (tripType.toLowerCase()) {
                case "roundtrip" -> {
                    if(!current_state) driverTools.click(TOGGLE_SELECT);
                    driverTools.waitForElementToAppear(TOGGLE_SELECTED).orElseThrow();
                }
                case "oneway" -> {
                    if(current_state) driverTools.click(TOGGLE_SELECTED);
                    driverTools.waitForElementToDisappear(TOGGLE_SELECTED);
                }
                default -> throw new NotImplementedException("Not implemented");
            }
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void selectDate(String date) {
    }

    private Swipe getDirection(String dateToSelect){
        WebElement selectedDateElement = driverTools.waitForElementToAppear(SELECTED_DATE).orElseThrow();
        String selectedDate = selectedDateElement.getText();

        logger.log("date to select : " + dateToSelect);
        logger.log("selected date : " + selectedDate);

        String[] dateToSelectArr = dateToSelect.split(" ");
        int dateToSelectDay = Integer.parseInt(dateToSelectArr[0]);
        String dateToSelectMonth = dateToSelectArr[1];
        int dateToSelectYear = Integer.parseInt(dateToSelectArr[2]);

        String[] selectedDateArr = selectedDate.split(" ");
        int selectedDateDay = Integer.parseInt(selectedDateArr[0]);
        String selectedDateMonth = selectedDateArr[1];
        int SelectedDateYear = Integer.parseInt(selectedDateArr[2]);

        int selectedMonthNum = CoreFunctions.monthToMonthNumber(selectedDateMonth);
        logger.log("selected month number: " + selectedMonthNum);
        int dateToSelectMonthNum = CoreFunctions.monthToMonthNumber(dateToSelectMonth);
        logger.log("date to select month number: " + dateToSelectMonthNum);

        int yearDif = dateToSelectYear - SelectedDateYear;
        if(yearDif == 0){
            int monthDif = dateToSelectMonthNum - selectedMonthNum;
            if(monthDif == 0){
                return dateToSelectDay - selectedDateDay < 0 ? Swipe.DOWN: Swipe.UP;
            }
            return monthDif < 0 ? Swipe.DOWN : Swipe.UP;
        }
        return yearDif < 0 ? Swipe.DOWN : Swipe.UP;
    }

    private Locator getDateLocator(String monthId, String monthEng, String day){
        // Anchor on the matching month_name node, then walk the document-following nodes
        // (not just siblings) to the first day cell whose text/content-desc matches `day`.
        // - `following::` is more robust than `following-sibling::*` because the day grid is
        //   typically nested under a different parent than the month header.
        // - `[1]` ensures we always pick the first matching day after the anchored month.
        // - The extra `name`/`label`/`value` predicates cover iOS native attributes for
        //   builds that surface the day on XCUIElementTypeStaticText rather than @text.
        String xpath =
                "(//*[contains(@resource-id,'month_name') "
                        + "and (contains(@text,'" + monthId + "') or contains(@text,'" + monthEng + "'))]"
                        + "/following::*[contains(@resource-id,'day') "
                        + "and not(contains(@resource-id,'header'))]"
                        + "[.//*[@text='" + day + "']"
                        + " or @text='" + day + "'"
                        + " or @name='" + day + "'"
                        + " or @label='" + day + "'"
                        + " or @value='" + day + "'"
                        + " or @content-desc='" + day + "'"
                        + " or contains(@content-desc,' " + day + " ')"
                        + " or starts-with(@content-desc,'" + day + " ')]"
                        + ")[1]";
        return new Locator("Date Locator", AppiumBy.xpath(xpath));
    }

    private void clickDate(String date){
        Swipe direction = getDirection(date);
        logger.log("direction : " + direction);

        String year = CoreFunctions.getSectionOfString(date," ", 2);
        String monthEng = CoreFunctions.getSectionOfString(date," ", 1);
        String monthId = CoreFunctions.monthEngToId(monthEng);
        String day= CoreFunctions.getSectionOfString(date," ", 0);
        Locator dateLocator = getDateLocator(monthId,monthEng,day);

        // Viewport-aware swipe loop. The previous version exited on DOM-presence,
        // which made it click on offscreen day cells when the calendar was mid-scroll.
        for (int i = 0; i < 12; i++) {
            Optional<WebElement> el = driverTools.waitForElementToAppear(dateLocator, 1);
            if (el.isPresent() && isElementInViewport(el.get())) {
                logger.log("Date '" + date + "' is in viewport; clicking.");
                driverTools.click(dateLocator);
                return;
            }
            if (direction == Swipe.UP) {
                driverTools.swipeUp(CALENDAR_SCROLLABLE);
            } else {
                driverTools.swipeDown(CALENDAR_SCROLLABLE);
            }
        }

        // Final fallback: click whatever the locator resolves to (preserves previous behaviour).
        if (driverTools.waitForElementToAppear(dateLocator, 3).isPresent()) {
            logger.log("Date '" + date + "' final fallback click after viewport-aware loop.");
            driverTools.click(dateLocator);
        } else {
            logger.log("WARN: Date '" + date + "' was never located in the calendar.");
        }
    }

    /**
     * Returns true only if the element is fully inside the current device viewport
     * (top edge ≥ 0 and bottom edge ≤ window height, with non-zero size).
     * Used to keep the calendar-swipe loop swiping until the day cell is genuinely
     * tappable, rather than just present in the accessibility tree.
     */
    private boolean isElementInViewport(WebElement el) {
        try {
            Rectangle rect = el.getRect();
            if (rect.getWidth() <= 0 || rect.getHeight() <= 0) {
                return false;
            }
            Dimension win = driverTools.getDriver().manage().window().getSize();
            int top = rect.getY();
            int bottom = rect.getY() + rect.getHeight();
            return top >= 0 && bottom <= win.getHeight();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void selectDate(String departDate,String returnDate) {

    } // FIXME: Implementation Pending

    @Override
    public void selectToday(String date){
        // FIXME: Implementation Pending
    }

    private void addPassenger(int amount, PassengerType passengerType) {

        Locator countLocator = passengerType.getCount();

        int currentCount = Integer.parseInt(
                driverTools.getValueAttribute(countLocator, "label")
        );

        logger.log("PassengerType=" + passengerType +
                ", current=" + currentCount +
                ", target=" + amount);

        int delta = amount - currentCount;

        if (delta == 0) {
            logger.log("Passenger count already correct, skipping");
            Assertion.assertThat("Correct Passenger Amount Added", currentCount,equalTo(amount));
            return;
        }

        boolean increase = delta > 0;
        int steps = Math.abs(delta);

        Locator actionButton =
                increase ? passengerType.getAdd() : passengerType.getMinus();

        logger.log((increase ? "Adding " : "Removing ") +
                steps + " " + passengerType.name().toLowerCase() + "(s)");

        //Click loop: no sleeps, no logging
        for (int i = 0; i < steps; i++) {
            driverTools.click(actionButton);
        }

        // State-based wait instead of sleep
        int finalCount = readPassengerCountWithRetry(countLocator, amount);

        logger.log("Final passenger count: " + finalCount);
        Assertion.assertThat("Correct Passenger Amount Added", finalCount,equalTo(amount));
    }

    private int readPassengerCountWithRetry(Locator countLocator, int expected) {
        int retries = 5;
        int value = -1;

        while (retries-- > 0) {
            value = Integer.parseInt(
                    driverTools.getValueAttribute(countLocator, "label")
            );

            if (value == expected) {
                return value;
            }

            try {
                Thread.sleep(200); // short, bounded, safe
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        return value;
    }



    @Override
    public void addPassenger(Integer adult, Integer child, Integer infant) {
        String methodName = "addPassenger";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.click(ADD_PASSENGER);
            logger.log("adding adult: " + adult);
            addPassenger(adult, PassengerType.ADULT);
            logger.log("adding child: " + child);
            addPassenger(child, PassengerType.CHILD);
            logger.log("adding infant: " + infant);
            addPassenger(infant, PassengerType.INFANT);
            driverTools.click(SAVE_BUTTON);
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void searchFlight() {
        String methodName = "searchFlight";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.click(FLIGHT_SEARCH_BUTTON);

            FlightSearchResultPage<T> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
            flightSearchResultPage.checkReloadPopup();
            flightSearchResultPage.waitForPageLoad();
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void selectSelfTransferFilter() {
        driverTools.waitForElementToPresence(OPEN_FILTERS, 20);
        driverTools.click(OPEN_FILTERS);
        driverTools.swipeToElement(OTHER_FILTERS, Swipe.UP, 10);
        for (int i = 0; i < 5; i++) {
            if (driverTools.isElementVisible(SEE_MORE_OTHER_FILTERS)) {
                driverTools.click(SEE_MORE_OTHER_FILTERS);
                break;
            }
            driverTools.swipeUp();
        }
        driverTools.waitForElementToPresence(SELF_TRANSFER);
        for (int i = 1; i < 5; i++){
            if (driverTools.isElementDisplayed(SELF_TRANSFER)){
                break;
            }
            driverTools.click(CLOSE);
            driverTools.click(CLOSE_AGAIN);
            driverTools.click(BACK);
            FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance((Session<WebDriver>) session);
            flightSearchPage.searchFlight();
            driverTools.waitForElementToAppear(OPEN_FILTERS);
            driverTools.click(OPEN_FILTERS);
            driverTools.swipeToElement(OTHER_FILTERS, Swipe.UP, 10);
            driverTools.swipeUp();
            driverTools.click(SEE_MORE_OTHER_FILTERS);
            driverTools.waitForElementToPresence(SELF_TRANSFER);
        }
        WebElement selfTransferButton = driverTools.waitForElementToPresence(SELF_TRANSFER, 10).orElseThrow();
        driverTools.clickElementFractionWithinBounds(selfTransferButton,0.9d,0.5d);
        driverTools.tryClickWhenPresent(SAVE);
        driverTools.click(SAVE_FILTER);
    }

    @Override
    public boolean isLowestPriceGuaranteePagePresent() {
        return true;
    } // FIXME: Implementation pending


    @Override
    public void closePopup () {
        String methodName = "closePopup";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForElementToPresence(FLIGHT_SEARCH_BUTTON, 3).orElseThrow();

            // OPTIMIZATION: Check only first 2-3 elements individually using indexed XPath
            // This avoids fetching all buttons (e.g., 20+) which is very slow
            // We only click if exactly 1 visible button is found among the first few elements
            int maxElementsToCheck = 2;
            List<WebElement> visibleButtons = new ArrayList<>();

            for (int i = 1; i <= maxElementsToCheck; i++) {
                try {
                    // Use indexed XPath to get only the i-th button: (//XCUIElementTypeButton)[i]
                    Locator buttonLocator = new Locator("BUTTON_" + i,
                            AppiumBy.xpath("(//XCUIElementTypeButton)[" + i + "]"));

                    // Wait for element presence with short timeout (1s per element)
                    WebElement button = driverTools.waitForElementToPresence(buttonLocator, 2)
                            .orElse(null);

                    // If element exists and is visible, add to list
                    if (button != null && button.isDisplayed()) {
                        visibleButtons.add(button);
                    }
                } catch (Exception e) {
                    // Continue to next element if this one fails
                    continue;
                }
            }

            // Only click if exactly 1 visible button is found (preserving original logic)
            if (visibleButtons.size() == 1) {
                visibleButtons.get(0).click();
            }
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void clearRecentSearch(){
        driverTools.waitForElementToAppear(CLEAR,5).ifPresent(driverTools::click);
    }

    @Override
    public void clickOnRecentSearch(){
        driverTools.click(RECENT_SEARCH);
    }

    private Locator getLpgBanner(){
        return new Locator("LPG_BANNER", AppiumBy.xpath("//*[contains(@label,'Lowest Price Guarantee') or contains(@label,'Jaminan Harga Termurah')]"));
    }

    @Override
    public boolean checkLPGBanner(){
        WebElement lpgBanner = driverTools.waitForElementToPresence(getLpgBanner(), 20).orElseThrow();
        return driverTools.waitForElementToPresence(LPG_TEXT).isPresent();
    }

    @Override
    public void clickLPGBanner(){
        driverTools.waitForElementToPresence(getLpgBanner(), 20).ifPresent(driverTools::click);
        driverTools.waitForElementToBeClickable(getLpgBanner(), 10).ifPresent(driverTools::click);
    }

    @Override
    public void verifySelectedDate(String dateSelectedDepart,String dateSelectedReturn,String lang){
        //fixme implementation pending
    }

    @Override
    public void verifySelectedDate(String dateSelectedDepart,String lang){
        //fixme implementation pending
    }

    private Locator getSearchResultLocator(){
        return new Locator("AIRPORT_SEARCH_RESULT", AppiumBy.xpath("//*[contains(@name,'locationLabel_') and contains(@name,'_0')]"));
    }

    @Override
    public String CheckSearchAirportResultsAlwaysOnTop(String location){
        driverTools.clickElementByCoordinate(ORIGIN);
        logger.log("Searching origin: " + location);

        WebElement airportField = driverTools.waitForElementToAppear(AIRPORT_FIELD).orElseThrow();
        driverTools.sendKeys(airportField, location);

        WebElement locationElement = driverTools.waitForElementToAppear(getSearchResultLocator()).orElseThrow();
        return driverTools.getText(locationElement);
    }

    @Override
    public void selectFlightClass(String flightClass){
        driverTools.click(ADD_PASSENGER);
        logger.log("Selecting flight class: " + flightClass);
        if(!isAlreadySelectedClass(flightClass)){
            switch (flightClass.toLowerCase()){
                case "economy":
                    driverTools.click(ECONOMY_CLASS);
                    break;
                case "premium economy":
                    driverTools.click(PREMIUM_ECONOMY_CLASS);
                    break;
                default: throw new IllegalArgumentException("flight class: " + flightClass + " not implemented");
            }
        }
        driverTools.click(SAVE_BUTTON);
    }

    private boolean isAlreadySelectedClass(String flightClass){
        return switch (flightClass.toLowerCase()) {
            case "economy" ->
                    driverTools.isElementDisplayed(ECONOMY_CLASS_SELECTED);
            case "premium economy" ->
                    driverTools.isElementDisplayed(PREMIUM_ECONOMY_CLASS_SELECTED);
            default -> throw new IllegalArgumentException("flight class: " + flightClass + " not implemented");
        };
    }

    @Override
    public boolean isOnFlightLandingPage() {
        driverTools.waitForElementToPresence(ORIGIN,20);
        return driverTools.isElementDisplayed(ORIGIN);
    }

    @Override
    public void deeplinkWithSelectedDateSrp(String origin,String destination,String originCityName,String destinationCityName,int daysToDepartureDate,int adult,int child,int infant) {
        FlightPage<T> flightPage = FlightPage.getInstance(session);
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);

        String url = "/pesawat/search?d=" + origin.toUpperCase() + "&a=" + destination.toUpperCase() + "&date=" + departureDate + "&adult=" + adult + "&child=" + child + "&infant=" + infant + "&class=economy&dType=" + flightPage.selectOriginAndDestinationType(origin) + "&aType=" + flightPage.selectOriginAndDestinationType(destination) + "&dLabel=" + originCityName + "&aLabel=" + destinationCityName + "&type=depart&flexiFare=true";
        String deeplinkGK = "tiket://m.gatotkaca.tiket.com" + url;
        String deeplinkPreProd = "tiket://m.preprod.tiket.com" + url;
        String deeplinkProd = "tiket://m.tiket.com" + url;
        Deeplink verticalPage = new Deeplink("VerticalPage", deeplinkGK, deeplinkPreProd, deeplinkProd);
        try {
            driverTools.deeplink(verticalPage);
        } catch (Exception e) {
            logger.log(e.getLocalizedMessage());
        }
    }

    @Override
    public void deeplinkWithSelectedDateSrpRoundTrip(String origin,String destination,String originCityName,String destinationCityName,int daysToDepartureDate,int duration,int adult,int child,int infant) {
        FlightPage<T> flightPage = FlightPage.getInstance(session);
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String returnDate = CoreFunctions.addToDateYYYY_MM_DD(departureDate, duration);

        String url = "/pesawat/search?d=" + origin.toUpperCase() + "&a=" + destination.toUpperCase() + "&date=" + departureDate + "&adult=" + adult + "&child=" + child + "&infant=" + infant + "&class=economy&dType=" + flightPage.selectOriginAndDestinationType(origin) + "&aType=" + flightPage.selectOriginAndDestinationType(destination) + "&dLabel=" + originCityName + "&aLabel=" + destinationCityName + "&type=depart&flexiFare=true&ret_date=" + returnDate;
        String deeplinkGK = "tiket://m.gatotkaca.tiket.com" + url;
        String deepLinkPreProd = "tiket://m.preprod.tiket.com" + url;
        String deeplinkProd = "tiket://m.tiket.com" + url;
        Deeplink verticalPage = new Deeplink("VerticalPage", deeplinkGK, deepLinkPreProd, deeplinkProd);
        try {
            driverTools.deeplink(verticalPage);
        } catch (Exception e) {
            logger.log(e.getLocalizedMessage());
        }
    }
}
