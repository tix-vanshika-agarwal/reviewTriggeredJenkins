package com.tiket.web.page.common.calendar;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class CalendarPageMWeb<T extends WebDriver> extends CalendarPageWeb<T> {

    public CalendarPageMWeb(Session<T> session) {
        super(session);
    }

    // Locator for ALL visible month headers in the calendar (not just the current month at index 0)
    private static final Locator CALENDAR_ALL_MONTH_HEADERS = new Locator("CALENDAR_ALL_MONTH_HEADERS",new By.ByXPath("//div[@data-month-index]//h2"));
    private static final Locator SAVE_BUTTON_LOCATOR = new Locator("SAVE_BUTTON_LOCATOR", new By.ByXPath("//section[contains(@class,'CalendarMobile_confirmation_section')]//button"));

    // Selects departure and optionally return date, then confirms selection
    @Override
    public void selectDate(String departureDate, String returnDate) {
        logger.log("MWeb Date Selection Started");
        logger.log("Selecting Departure Date: " + departureDate);
        selectSingleDate(departureDate);

        if (returnDate != null && !returnDate.trim().isEmpty()) {
            logger.log("Selecting Return Date: " + returnDate);
            selectSingleDate(returnDate);
        }
        logger.log("Clicking Save button to confirm selection");
        driverTools.waitForElementToAppear(SAVE_BUTTON_LOCATOR);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON_LOCATOR);

        logger.log("MWeb date selection completed.");
    }

    // Selects a single date by parsing and navigating to the correct month/year
    @Override
    public void selectSingleDate(String date) {
        String year = CoreFunctions.getSectionOfString(date, " ", 2);
        String monthEng = CoreFunctions.getSectionOfString(date, " ", 1);
        String day = CoreFunctions.getSectionOfString(date, " ", 0);
        String monthId = CoreFunctions.monthEngToId(monthEng);

        logger.log("Parsed Date -> Day: " + day + ", Month: " + monthEng + ", Year: " + year);
        navigateToMonthYear(monthEng, year);

        Locator dateLocator = getDateLocator(day, monthEng, monthId, year);
        logger.log("Clicking date element for: " + date);

        driverTools.waitForElementToAppear(dateLocator);
        driverTools.clickWithJavascriptExecutor(dateLocator);

        logger.log("Date selected successfully: " + date);
    }

    // Navigates the calendar to the specified month and year, swiping if necessary
    @Override
    public void navigateToMonthYear(String targetMonthEng, String targetYear) {
        int maxAttempts = 12;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            if (isMonthVisible(targetMonthEng, targetYear)) {
                logger.log("Reached target month/year: " + targetMonthEng + " " + targetYear);
                return;
            }

            int comparison = compareMonthYear(targetMonthEng, targetYear);

            if (comparison > 0) {
                logger.log("Target month/year is ahead → swipe UP");
                driverTools.swipeUp();
            } else {
                logger.log("Target month/year is behind → swipe DOWN");
                driverTools.swipeDown();
            }
        }
        throw new RuntimeException("Unable to locate target month/year: " + targetMonthEng + " " + targetYear);
    }

    // Checks if the specified month and year are currently visible in the calendar
    // This checks ALL visible months in the viewport, not just the first one
    @Override
    public boolean isMonthVisible(String monthEng, String year) {
        driverTools.waitForElementToAppear(CALENDAR_ALL_MONTH_HEADERS);
        List<String> visibleMonths = driverTools.getTexts(CALENDAR_ALL_MONTH_HEADERS);

        logger.log("All Visible Month Headers -> " + visibleMonths);

        // CalendarMobile renders month headings in the app's current locale, which is
        // Bahasa for tiket.com's default (e.g. "Juni 2026") but English in some test
        // environments (e.g. "June 2026"). Accept either form for the input month.
        String monthId = CoreFunctions.monthEngToId(monthEng);
        String targetEng = monthEng + " " + year;
        String targetId = (monthId != null ? monthId : monthEng) + " " + year;
        boolean found = visibleMonths.stream()
                .anyMatch(m -> m.equalsIgnoreCase(targetEng) || m.equalsIgnoreCase(targetId));

        logger.log("Target '" + targetEng + "' / '" + targetId + "' "
                + (found ? "IS" : "IS NOT") + " currently visible");
        return found;
    }

    // Compares the target month/year with the currently visible ones to determine navigation direction
    // Returns: positive if target is in future, negative if target is in past, 0 if visible
    @Override
    public int compareMonthYear(String targetMonthEng, String targetYear) {
        List<String> visibleMonths = driverTools.getTexts(CALENDAR_ALL_MONTH_HEADERS);
        if (visibleMonths.isEmpty()) {
            throw new RuntimeException("No calendar month headers found");
        }
        
        // Get the FIRST visible month (index 0) as our reference point
        String firstVisibleMonthYear = visibleMonths.get(0);
        String[] parts = firstVisibleMonthYear.split(" ");
        if (parts.length < 2) {
            throw new RuntimeException("Invalid month header format: " + firstVisibleMonthYear);
        }
        
        String currentMonthEng = parts[0];
        String currentYear = parts[1];

        int currentMonthNum = CoreFunctions.getMonthNumber(currentMonthEng);
        int targetMonthNum = CoreFunctions.getMonthNumber(targetMonthEng);
        int currentYearNum = Integer.parseInt(currentYear);
        int targetYearNum = Integer.parseInt(targetYear);
        
        int yearDiff = targetYearNum - currentYearNum;

        logger.log("Comparing First Visible Month: " + currentMonthEng + " " + currentYear +
                " (month-index=0) with Target: " + targetMonthEng + " " + targetYear);
        
        // If years are different, year difference is more important
        if (yearDiff != 0) {
            logger.log("Year difference: " + yearDiff + " -> " + (yearDiff > 0 ? "scroll forward" : "scroll backward"));
            return yearDiff;
        }
        
        // Same year, compare months
        int monthDiff = targetMonthNum - currentMonthNum;
        logger.log("Month difference: " + monthDiff + " -> " + (monthDiff > 0 ? "scroll forward" : "scroll backward"));
        return monthDiff;
    }
}