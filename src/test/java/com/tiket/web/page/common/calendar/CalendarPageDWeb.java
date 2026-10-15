package com.tiket.web.page.common.calendar;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CalendarPageDWeb<T extends WebDriver> extends CalendarPageWeb<T> {

    public CalendarPageDWeb(Session<T> session) {
        super(session);
    }

    private static final Locator NEXT_BUTTON_LOCATOR = new Locator("NEXT_BUTTON_LOCATOR", new By.ByXPath("//button[contains(@class,'CalendarDesktop_next_button')]"));
    private static final Locator PREV_BUTTON_LOCATOR = new Locator("PREV_BUTTON_LOCATOR", new By.ByXPath("//button[contains(@class,'CalendarDesktop_prev_button')]"));
    private static final Locator CALENDAR_HEADER_LEFT_LOCATOR = new Locator("CALENDAR_HEADER_LEFT_LOCATOR", new By.ByXPath("(//div[contains(@class,'CalendarDesktop_month_heading_container')])[1]//h2"));
    private static final Locator CALENDAR_HEADER_RIGHT_LOCATOR = new Locator("CALENDAR_HEADER_RIGHT_LOCATOR", new By.ByXPath("(//div[contains(@class,'CalendarDesktop_month_heading_container')])[2]//h2"));

    /**
     * Selects departure and (optional) return date.
     * If returnDate is null/empty -> single-trip selection.
     */
    @Override
    public void selectDate(String departureDate, String returnDate) {
        // Departure Date Selection
        logger.log("Selecting Departure Date: " + departureDate);
        selectSingleDate(departureDate);

        // Return Date Selection (Only if provided)
        if (returnDate != null && !returnDate.trim().isEmpty()) {
            logger.log("Selecting Return Date: " + returnDate);
            selectSingleDate(returnDate);
            logger.log("Round-trip dates selected successfully.");
        } else {
            logger.log("No Return Date Provided. Single-trip date selection completed.");
        }
    }

    //Selects a single date in the calendar widget.
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

    //Navigates through the calendar until target month & year are visible.
    @Override
    public void navigateToMonthYear(String targetMonthEng, String targetYear) {
        int maxAttempts = 6; // Covers 6 months forward/back
        int attempt = 0;

        while (!isMonthVisible(targetMonthEng, targetYear)) {
            if (attempt++ > maxAttempts) {
                String message = "Unable to locate target month/year: " + targetMonthEng + " " + targetYear;
                logger.log(message);
                throw new RuntimeException(message);
            }

            int comparison = compareMonthYear(targetMonthEng, targetYear);
            if (comparison > 0) {
                logger.log("Target month/year ahead. Clicking NEXT...");
                if (driverTools.isElementPresent(NEXT_BUTTON_LOCATOR)) {
                    driverTools.clickWithJavascriptExecutor(NEXT_BUTTON_LOCATOR);
                } else {
                    throw new RuntimeException("NEXT button not found but target month is ahead!");
                }
            } else if (comparison < 0) {
                logger.log("Target month/year behind. Clicking PREVIOUS...");
                if (driverTools.isElementPresent(PREV_BUTTON_LOCATOR)) {
                    driverTools.clickWithJavascriptExecutor(PREV_BUTTON_LOCATOR);
                } else {
                    logger.log("PREV button not available, possibly already at earliest month.");
                    break; //If the PREV button is not present (already at the earliest month), it logs a message and breaks the loop, preventing errors.
                }
            } else {
                // comparison == 0 means the target IS the current visible month but
                // isMonthVisible disagreed (e.g. locale mismatch in the header string).
                // Stop looping; we're effectively already at the target.
                logger.log("compareMonthYear reports same month/year as target; treating as visible to break loop.");
                break;
            }
        }

        logger.log("Reached target month/year: " + targetMonthEng + " " + targetYear);
    }

    //Checks if given month/year is visible on either left or right calendar panel.
    @Override
    public boolean isMonthVisible(String monthEng, String year) {
        driverTools.waitForElementToAppear(CALENDAR_HEADER_LEFT_LOCATOR);
        String leftMonthYear = driverTools.getText(CALENDAR_HEADER_LEFT_LOCATOR).orElse("");
        String rightMonthYear = driverTools.getText(CALENDAR_HEADER_RIGHT_LOCATOR).orElse("");

        logger.log("Visible Months -> Left: " + leftMonthYear + ", Right: " + rightMonthYear);

        // CalendarDesktop renders month headings in the app's current locale, which is
        // Bahasa for tiket.com's default (e.g. "Juni 2026") but English in some test
        // environments (e.g. "June 2026"). Accept either form for the input month.
        String monthId = CoreFunctions.monthEngToId(monthEng);
        String targetEng = monthEng + " " + year;
        String targetId = (monthId != null ? monthId : monthEng) + " " + year;

        return leftMonthYear.equalsIgnoreCase(targetEng) || leftMonthYear.equalsIgnoreCase(targetId)
                || rightMonthYear.equalsIgnoreCase(targetEng) || rightMonthYear.equalsIgnoreCase(targetId);
    }

    /**
     * Compares current visible month-year (left panel) with target month-year.
     * Returns positive if target is ahead, negative if behind, 0 if same.
     */
    @Override
    public int compareMonthYear(String targetMonthEng, String targetYear) {
        String currentMonthYear = driverTools.getText(CALENDAR_HEADER_LEFT_LOCATOR)
                .orElseThrow(() -> new RuntimeException("Calendar header not found"));

        String[] parts = currentMonthYear.split(" ");
        String currentMonthEng = parts[0];
        String currentYear = parts[1];

        int currentMonthNum = CoreFunctions.getMonthNumber(currentMonthEng);
        int targetMonthNum = CoreFunctions.getMonthNumber(targetMonthEng);
        int yearDiff = Integer.parseInt(targetYear) - Integer.parseInt(currentYear);

        logger.log("Comparing Current: " + currentMonthEng + " " + currentYear +
                " with Target: " + targetMonthEng + " " + targetYear);

        return (yearDiff != 0) ? yearDiff : targetMonthNum - currentMonthNum;
    }
}
