 package com.tiket.page.flight.searchpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.flight.searchresultpage.FlightSearchResultPage;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Optional;

 import static org.hamcrest.Matchers.containsString;
 import static org.hamcrest.core.Is.is;

public class FlightSearchPageAndroid<T extends WebDriver> extends FlightSearchPage<T> {

    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("(//*[contains(@resource-id, 'tds_icon_navigation_toolbar')])[2]"));
    public static final Locator ORIGIN = new Locator("Origin", AppiumBy.id("tds_search_form_title_group"));
    public static final Locator DESTINATION = new Locator("Destination", AppiumBy.id("tds_search_form_subtitle_group"));
    public static final Locator AIRPORT_FIELD = new Locator("Airport Field", AppiumBy.id("tds_search_et"));
    public static final Locator AIRPORT_RESULT = new Locator("Airport Result", AppiumBy.id("tds_list_parent"));
    public static final Locator TRIP_TOGGLE = new Locator("TRIP_TOGGLE",AppiumBy.id("tds_toggle_group"));
    public static final Locator RETURN_CALENDAR = new Locator("RETURN_CALENDAR",AppiumBy.id("tds_flight_return_date"));
    public static final Locator DEPART_CALENDAR = new Locator("DEPART_CALENDAR",AppiumBy.id("tds_flight_depart_date"));
    public static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON",AppiumBy.id("tds_button_view"));
    public static final Locator SAVE_BUTTON_FILTERS = new Locator("SAVE_BUTTON_FILTERS",AppiumBy.xpath("//*[contains(@resource-id, 'tds_button_view') and (@text='Save' or @text='Simpan')]"));
    public static final Locator SAVE_BUTTON_CALENDAR = new Locator("SAVE_BUTTON",AppiumBy.id("tds_button_view"));
    public static final Locator ADD_PASSENGER = new Locator("ADD_PASSENGER",AppiumBy.id("tds_passenger"));
    public static final Locator LPG_CLOSE = new Locator("LPG_CLOSE",AppiumBy.className("android.widget.ImageButton"));
    public static final Locator LPG_WEBVIEW = new Locator("LPG_WEBVIEW",AppiumBy.xpath("//*[@class='text-page']"));
    public static final Locator CALENDAR_SCROLLABLE = new Locator("CALENDAR_SCROLLABLE",AppiumBy.id("tds_calendar_month_rv"));
    public static final Locator TIMEZONE_CHANGE_POPUP = new Locator("TIMEZONE_CHANGE_POPUP",AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Got it') or contains(@text,'Saya mengerti'))]"));
    public static final Locator CLEAR_RECENT_SEARCH_BTN = new Locator("CLEAR_RECENT_SEARCH_BTN", AppiumBy.id("tv_clear"));
    public static final Locator RECENT_SEARCH_CONTAINER = new Locator("RECENT_SEARCH_CONTAINER",AppiumBy.id("constraint_layout"));
    public static final Locator SELECTED_DATE = new Locator("SELECTED_DATE",AppiumBy.id("tds_calendar_start_date"));
    public static final Locator SELECTED_END_DATE = new Locator("SELECTED_END_DATE",AppiumBy.id("tds_calendar_end_date"));
    public static final Locator STICKY_MONTH_NAME = new Locator("STICKY_MONTH_NAME",AppiumBy.id("tds_calendar_sticky_name"));
    public static final Locator FIRST_VISIBLE_MONTH_HEADER = new Locator("FIRST_VISIBLE_MONTH_HEADER",AppiumBy.xpath("(//*[contains(@resource-id,'month_name')])[1]"));
    private static final Locator TODAY_DAY_CELL = new Locator("TODAY_DAY_CELL",
            AppiumBy.xpath("//*[contains(@resource-id,'ll_day_group')"
                    + " and (.//android.widget.TextView[@text='Today' or @text='Hari ini'"
                    + " or @text='Hari Ini'])]"));
    private static final Locator TODAY_LABEL = new Locator("TODAY_LABEL",
            AppiumBy.xpath("//android.widget.TextView[@text='Today' or @text='Hari ini'"
                    + " or @text='Hari Ini']"));
    public static final Locator LPG_BANNER = new Locator("LPG_BANNER",AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_notification_banner')]//*[contains(@class,'TextView')]"));
    public static final Locator DATE_SELECTED_DEPART = new Locator("DATE_SELECTED_DEPART",AppiumBy.xpath("(//*[contains(@resource-id,'tds_flight_depart_date')]//*[contains(@resource-id,'tds_search_form_title') and not(contains(@resource-id,'image') or contains(@resource-id,'group'))])"));
    public static final Locator DATE_SELECTED_RETURN = new Locator("DATE_SELECTED_RETURN",AppiumBy.xpath("(//*[contains(@resource-id,'tds_flight_return_date')]//*[contains(@resource-id,'tds_search_form_title') and not(contains(@resource-id,'image') or contains(@resource-id,'group'))])"));
    public static final Locator ECONOMY_CLASS = new Locator("ECONOMY_CLASS", AppiumBy.id("tv_economy"));
    public static final Locator PREMIUM_ECONOMY_CLASS = new Locator("PREMIUM_ECONOMY_CLASS", AppiumBy.id("tv_premium"));
    public static final Locator LPG_TEXT = new Locator("LPG_TEXT",AppiumBy.xpath("(//*[contains(@text, 'Lowest Price Guarantee') or contains(@text, 'Jaminan Harga Termurah')])[1]"));
    private static final Locator OPEN_FILTERS = new Locator("OPEN_FILTERS",AppiumBy.xpath("//*[contains(@resource-id, 'tds_title_tv') and contains(normalize-space(@text), 'Filter')]"));
    private static final Locator OTHER_FILTERS = new Locator("OTHER_FILTERS",AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_header_2') and (contains(@text, 'Other Filters') or contains(@text, 'Filter Lainnya') or contains(@text, 'Others') or contains(@text, 'Lainnya'))]"));
    private static final Locator SEE_MORE_OTHER_FILTERS = new Locator("SEE_MORE_OTHER_FILTERS",AppiumBy.xpath("(//*[contains(@resource-id, 'tv_flight_header_2') and (contains(@text, 'Other') or contains(@text, 'Filter Lainnya'))]/following::*[contains(@text, 'Tampilkan') or contains(@text, 'Show') or contains(@text, 'See')])[1]"));
    /** Self-transfer row subtitle + checkbox in the same row (no full package id — use contains for fl_check). */
    private static final Locator SELF_TRANSFER = new Locator("SELF_TRANSFER", AppiumBy.xpath("//*[contains(@resource-id, 'tv_option_label') and (@text='Self-transfer' or @text='Transit Mandiri' or @content-desc='Self-transfer' or @content-desc='Transit Mandiri')]/parent::*//*[contains(@resource-id, 'fl_check')]"));
    private static final Locator SAVE_FLIGHTS_BUTTON = new Locator("SAVE_FLIGHTS_BUTTON",AppiumBy.xpath("//*[contains(@resource-id, 'btn_action_bottom')]//*[contains(@resource-id, 'tds_button_view')]"));
    private static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON",AppiumBy.xpath("//*[contains(@resource-id, 'iv_close')]"));

    public FlightSearchPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void backToHomePage(){
        WebElement backButton = driverTools.waitForElementToAppear(BACK_BUTTON).orElseThrow();
        driverTools.click(backButton);
    }

    @Override
    public void openFlightDeeplink() {

    } // FIXME: Implementation pending

    @Override
    public void selectOrigin(String origin) {
        String methodName = "selectOrigin";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.click(ORIGIN);
            logger.log("Searching origin: " + origin);
            if (origin.contains("/")){
                selectOriginViaAirPortCode(origin);
            }else {
                driverTools.sendKeys(AIRPORT_FIELD,origin);
                if (origin.length() > 4) {
                    driverTools.click(AIRPORT_RESULT);
                } else {
                    driverTools.click(getLocatorBasedOnAirportCode(origin));
                }
            }
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    private Locator getLocatorBasedOnAirportCode(String airportCode){
        return new Locator("LOCATOR_AIRPORT_CODE", AppiumBy.xpath("//*[contains(@resource-id,'tds_selection_area_right')]//*[@text='"+airportCode.toUpperCase()+"']"));
    }

    @Override
    public void selectOriginViaAirPortCode(String origin) {
        String airportName = origin.split("/")[0];
        String airportCode = origin.split("/")[1].toUpperCase();
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
                driverTools.sendKeys(AIRPORT_FIELD,destination);
                if (destination.length() > 4) {
                    driverTools.click(AIRPORT_RESULT);
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
        driverTools.sendKeys(AIRPORT_FIELD,airportName);
        driverTools.click(getLocatorBasedOnAirportCode(airportCode));
    }

    @Override
    public void toggleTriptype(String tripType) {
        String methodName = "toggleTriptype";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            boolean current_state = driverTools.waitForElementToAppear(RETURN_CALENDAR,3).isPresent();
            logger.log("Toggling trip type: " + tripType);
            tripType = tripType.toLowerCase();
            switch (tripType) {
                case "roundtrip" -> {
                    if(!current_state) driverTools.click(TRIP_TOGGLE);
                    driverTools.waitForElementToAppear(RETURN_CALENDAR).orElseThrow();
                }
                case "oneway" -> {
                    if(current_state) driverTools.click(TRIP_TOGGLE);
                    driverTools.waitForElementToDisappear(RETURN_CALENDAR);
                }
                default -> throw new NotImplementedException("Not implemented");
            }
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void selectDate(String date) {
        String methodName = "selectDate";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.click(DEPART_CALENDAR);
            logger.log("Departing on: "+ date);
            clickDate(date);
            driverTools.click(SAVE_BUTTON_CALENDAR);
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void selectToday(String date) {
        driverTools.click(DEPART_CALENDAR);
        logger.log("Departing today: "+ date);
        clickDateToday(date);
        driverTools.click(SAVE_BUTTON_CALENDAR);
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

    private void clickDate(String date){
        // Seed direction from the calendar's currently-selected date (e.g. deeplink
        // left us on a future month). Live sticky/header text still drives each
        // iteration; this seed is used when sticky/header briefly disappear after a swipe.
        Swipe initialDirection = getDirection(date);
        logger.log("initial direction : " + initialDirection);

        String monthEng = CoreFunctions.getSectionOfString(date," ", 1);
        String monthId = CoreFunctions.monthEngToId(monthEng);
        String day = CoreFunctions.getSectionOfString(date," ", 0);

        navigateToTargetMonthAndClick(date, monthId, monthEng, day, 25, 3, initialDirection, false);

        // Post-click verification: confirm the click actually registered in
        // the calendar's selected-date field BEFORE we close the picker. This
        // catches silently-rejected clicks (e.g. tap on a past, unselectable
        // cell, or a near-miss tap that hit a sibling row) and gives us one
        // chance to recover via the geometric resolver while the calendar is
        // still open. Without this guard, a silent miss only surfaces during
        // the post-save verifySelectedDate assertion - by which point the
        // calendar is dismissed and the test fails the iteration.
        if (!isDateRegisteredInCalendar(date, day, monthEng, monthId)) {
            logger.softWarning("Click on '" + date + "' did not register in the "
                    + "calendar's selected-date field; attempting one geometric retry.");
            if (clickDayByGeometry(date, day, monthId, monthEng)) {
                if (isDateRegisteredInCalendar(date, day, monthEng, monthId)) {
                    logger.log("Geometric retry registered '" + date + "' successfully.");
                } else {
                    logger.softWarning("Geometric retry also failed to register '"
                            + date + "'. Letting verifySelectedDate surface the failure.");
                }
            }
        }
    }

    /**
     * Best-effort check that the calendar's selected-date display now reflects
     * the freshly-clicked target date. Returns {@code true} when either
     * {@link #SELECTED_DATE} (depart leg / one-way) OR {@link #SELECTED_END_DATE}
     * (return leg of a round-trip) contains the target's day, month and year
     * tokens.
     *
     * <p><b>Polling rationale.</b> The selected-date field updates
     * asynchronously after the day-cell click - usually within ~300ms, but
     * fuzz runs on LambdaTest have shown updates as slow as ~1.5s when the
     * device is under load (e.g. 4 Aug 2026 and 8 Jul 2026 in the
     * 1779638995443 seed). A single read 300ms after the click produced
     * false-negatives that triggered an unnecessary geometric retry. The
     * polling loop below reads the field every ~250ms for up to ~2s,
     * succeeding the moment the text matches and bailing out only when the
     * window expires.
     *
     * <p>The check is intentionally tolerant of locale: we accept an English
     * month token, an Indonesian month token, or either's short form, so the
     * same code works whether the device is configured to en-US or id-ID.
     *
     * <p>Never throws. Diagnostics are logged via {@code softWarning} so a
     * flaky read of the selected-date field cannot, by itself, fail the test.
     */
    private boolean isDateRegisteredInCalendar(String date, String day,
                                               String monthEng, String monthId) {
        String year = null;
        String[] parts = date.trim().split("\\s+");
        if (parts.length >= 3) year = parts[2];

        long deadline = System.currentTimeMillis() + 2000L;
        String lastSeenStart = null;
        String lastSeenEnd = null;

        while (System.currentTimeMillis() < deadline) {
            try {
                String startText = safeTextNoWait(SELECTED_DATE);
                if (startText != null) lastSeenStart = startText;
                if (startText != null && !startText.isBlank()
                        && matchesDateTokens(startText, day, monthEng, monthId, year)) {
                    logger.log("Calendar selected-date field '" + SELECTED_DATE
                            + "'='" + startText + "' matches target '" + date + "'");
                    return true;
                }

                String endText = safeTextNoWait(SELECTED_END_DATE);
                if (endText != null) lastSeenEnd = endText;
                if (endText != null && !endText.isBlank()
                        && matchesDateTokens(endText, day, monthEng, monthId, year)) {
                    logger.log("Calendar selected-date field '" + SELECTED_END_DATE
                            + "'='" + endText + "' matches target '" + date + "'");
                    return true;
                }
            } catch (Exception e) {
                logger.softWarning("isDateRegisteredInCalendar threw: " + e.getMessage()
                        + "; trusting the click.");
                return true;
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return true;
            }
        }
        logger.log("Calendar selected-date polling timed out for target '" + date
                + "' (last SELECTED_DATE='" + lastSeenStart
                + "', last SELECTED_END_DATE='" + lastSeenEnd + "').");
        return false;
    }

    /**
     * Wait-free analogue of {@link #safeText(Locator)}: uses
     * {@code findElements} (which returns an empty list immediately when
     * nothing matches, with no presence-timeout machinery) so a polling
     * loop's per-iteration cost is just one Appium round-trip rather than
     * the 1-second default of {@code waitForElementToPresenceSafe}. Used
     * by {@link #isDateRegisteredInCalendar(String, String, String, String)}.
     */
    private String safeTextNoWait(Locator locator) {
        try {
            List<WebElement> hits = driverTools.getDriver().findElements(locator.by());
            if (hits == null || hits.isEmpty()) return null;
            return hits.get(0).getText();
        } catch (Exception ignored) {
            // Best effort.
        }
        return null;
    }

    /**
     * Returns true iff {@code actual} contains the day token, the year token
     * and at least one of the month tokens (English long, Indonesian long,
     * English short, or Indonesian short). Day matching is whole-token to
     * avoid {@code "5"} matching {@code "25"}, AND accepts a zero-padded
     * variant ({@code "04"} for day {@code "4"}) because the calendar's
     * {@code tds_calendar_start_date} field renders single-digit days with
     * a leading zero (e.g. "04 Aug 2026") while {@code addDaysToLongCurrentDate}
     * produces unpadded targets ("4 August 2026").
     */
    private boolean matchesDateTokens(String actual, String day, String monthEng,
                                      String monthId, String year) {
        if (actual == null) return false;
        String dayPattern;
        if (day != null && day.length() == 1 && Character.isDigit(day.charAt(0))) {
            dayPattern = "(^|\\D)0?" + java.util.regex.Pattern.quote(day) + "(\\D|$)";
        } else {
            dayPattern = "(^|\\D)" + java.util.regex.Pattern.quote(day) + "(\\D|$)";
        }
        if (!java.util.regex.Pattern.compile(dayPattern).matcher(actual).find()) {
            return false;
        }
        if (year != null && !year.isEmpty() && !actual.contains(year)) {
            return false;
        }
        boolean monthMatched = false;
        if (monthEng != null && !monthEng.isEmpty() && actual.contains(monthEng)) {
            monthMatched = true;
        }
        if (!monthMatched && monthId != null && !monthId.isEmpty() && actual.contains(monthId)) {
            monthMatched = true;
        }
        if (!monthMatched && monthEng != null && !monthEng.isEmpty()) {
            String shortEng = CoreFunctions.monthLongToShortEn(monthEng);
            if (shortEng != null && !shortEng.isEmpty() && actual.contains(shortEng)) {
                monthMatched = true;
            }
        }
        if (!monthMatched && monthId != null && !monthId.isEmpty()) {
            String shortId = CoreFunctions.monthLongToShortId(monthId);
            if (shortId != null && !shortId.isEmpty() && actual.contains(shortId)) {
                monthMatched = true;
            }
        }
        return monthMatched;
    }

    private void clickDateToday(String date){
        String monthEng = CoreFunctions.getSectionOfString(date," ", 1);
        String monthId = CoreFunctions.monthEngToId(monthEng);
        String day = CoreFunctions.getSectionOfString(date," ", 0);

        // Prefer the labelled Today cell - unique on the current month and avoids
        // the fragile day-number XPath (Today cells also carry price/"Today" labels).
        if (tryClickTodayCell() && isDateRegisteredInCalendar(date, day, monthEng, monthId)) {
            return;
        }

        Swipe initialDirection = getDirection(date);
        logger.log("Today cell not yet visible; navigating toward '" + date
                + "' (initial direction=" + initialDirection + ")");
        navigateToTargetMonthAndClick(date, monthId, monthEng, day, 16, 5, initialDirection, true);

        if (!isDateRegisteredInCalendar(date, day, monthEng, monthId)) {
            // Do NOT blindly re-click geometry here: that previously selected
            // "04 Sep" while sticky still said August. One last Today-only attempt.
            if (tryClickTodayCell() && isDateRegisteredInCalendar(date, day, monthEng, monthId)) {
                return;
            }
            dumpCalendarStateOnFailure(date, day, monthId, monthEng,
                    "today-not-registered", readScrollPositionMonthText());
            throw new RuntimeException(
                    "Could not select today '" + date + "' in the calendar "
                            + "(selection did not register as the current month/day).");
        }
    }

    @Override
    public void selectDate(String departDate,String returnDate) {
        driverTools.click(DEPART_CALENDAR);
        logger.log("Departing on: "+ departDate);
        clickDate(departDate);
        logger.log("returning on: "+ returnDate);
        clickDate(returnDate);
        driverTools.waitForElementToAppear(SAVE_BUTTON_CALENDAR, 20);
        driverTools.click(SAVE_BUTTON_CALENDAR);
    }

    /**
     * Sticky-driven calendar navigation. Replaces the original algorithm to fix
     * two production failure modes that were observed back-to-back:
     *
     *   <ul>
     *     <li><b>"month_name virtualised"</b> - after several coarse swipes
     *         the {@code tds_month_name_calendar} element for the target
     *         month gets recycled out of the AccessibilityNodeInfo tree.
     *         The sticky ({@code tds_calendar_sticky_name}) remains in the
     *         tree and still shows "May 2026", but any XPath anchored on
     *         {@code //*[contains(@resource-id,'month_name')]} matches
     *         <i>nothing</i>. The previous algorithm's locator therefore
     *         resolved to zero hits and we burned the timeout budget on
     *         alternating no-op expose swipes that net to zero pixels.</li>
     *     <li><b>Holiday content-desc cells</b> - some day cells (Indonesian
     *         holidays) carry their day number only on
     *         {@code @content-desc="8 en_holiday_..."}, with empty
     *         {@code @text}. A {@code TextView[@text='8']} match misses
     *         them entirely.</li>
     *   </ul>
     *
     * Phases:
     * <ol>
     *   <li><b>Direct probe.</b> Try the broadened day locator in case the
     *       calendar already opened on the right month.</li>
     *   <li><b>Coarse navigation.</b> While the sticky does <i>not</i> show
     *       the target month, swipe 0.5 viewport in the live-computed
     *       direction. Boundary detection trips a hard failure if the
     *       sticky text stops changing.</li>
     *   <li><b>Decisive backtrack.</b> Once the sticky reports the target
     *       month but the day still isn't found, do a SINGLE 0.3-viewport
     *       backward swipe (same direction as the last coarse swipe).
     *       This reliably re-hydrates the just-virtualised section header
     *       and pulls the day grid back into the rendered subtree without
     *       crossing into the previous month. We do NOT alternate - the
     *       old up/down wiggle netted to zero pixels and never recovered.</li>
     *   <li><b>Geometric fallback.</b> If the XPath still misses, scope
     *       the search by sticky-bottom and the next visible month_name's
     *       top-y. This bypasses the virtualised-anchor failure mode
     *       entirely.</li>
     *   <li><b>Diagnostic dump.</b> On final exhaustion, dump the page
     *       source so the next regression is debuggable in one shot.</li>
     * </ol>
     */
    private void navigateToTargetMonthAndClick(String date, String monthId, String monthEng,
                                               String day, int maxAttempts, int maxExposeAttempts,
                                               Swipe initialDirection, boolean preferTodayLabel) {
        Locator dateLocator = getDateLocator(monthId, monthEng, day);

        // Phase 1: direct probe.
        if (preferTodayLabel && tryClickTodayCell()
                && isDateRegisteredInCalendar(date, day, monthEng, monthId)) {
            return;
        }
        if (tryClickIfDayInViewport(dateLocator, date)) {
            if (!preferTodayLabel
                    || isDateRegisteredInCalendar(date, day, monthEng, monthId)) {
                return;
            }
            logger.softWarning("Phase-1 day XPath click did not register '" + date + "'.");
        }
        // Geometric fallback even on the very first probe - covers the case
        // where the calendar opens on the target month with the section
        // header already virtualised.
        if (clickDayByGeometry(date, day, monthId, monthEng)) {
            if (!preferTodayLabel
                    || isDateRegisteredInCalendar(date, day, monthEng, monthId)) {
                return;
            }
            logger.softWarning("Phase-1 geometry click did not register '" + date + "'.");
        }

        String previousMonthText = null;
        String lastKnownMonthText = null;
        Swipe lastCoarseDirection = initialDirection != null ? initialDirection : Swipe.UP;
        int stuckCount = 0;
        int onTargetExposeAttempts = 0;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            // Read sticky and month_name separately. Do NOT use SELECTED_DATE —
            // it stays on the pre-scroll value and poisons direction.
            // Sticky is the floating "current section" label but often lags or
            // disappears mid-swipe; month_name headers track real scroll content
            // (prod 5 Aug 2026: sticky null / lastKnown August while October
            // header was already in-tree).
            String stickyText = safeTextNoWait(STICKY_MONTH_NAME);
            String headerText = readTopmostMonthHeaderText();
            String currentMonthText = firstNonBlank(stickyText, headerText);
            // Progress for stuck-detection: prefer header (scroll content) so a
            // lagging sticky that keeps saying "August" cannot false-trip
            // boundary-stuck while the list has already advanced.
            String progressText = firstNonBlank(headerText, stickyText);
            if (progressText != null) {
                lastKnownMonthText = progressText;
            }

            logger.log("attempt " + attempt + " sticky='" + stickyText
                    + "' header='" + headerText + "' (lastKnown='" + lastKnownMonthText
                    + "', target='" + monthEng + "/" + monthId + "')");

            // Wait-free probe in the swipe loop — a 2s appear-wait here burned
            // ~2s/attempt while the target month was still off-screen.
            if (tryClickIfDayInViewport(dateLocator, date, 0)) {
                if (!preferTodayLabel
                        || isDateRegisteredInCalendar(date, day, monthEng, monthId)) {
                    return;
                }
                logger.softWarning("Day XPath click did not register '" + date
                        + "'; continuing navigation.");
            }

            // Sticky lags one month while scrolling BACK from a future date
            // (prod: sticky='September 2026' while header='October/Oktober 2026').
            // Treating that as on-target stopped coarse navigation and burned
            // expose-swipes on the NEXT month. Header later-than-target means
            // we are not there yet - keep coarse-scrolling, keyed off the header.
            boolean headerLaterThanTarget = isHeaderLaterThanTarget(headerText, date);
            boolean onTargetMonth = !headerLaterThanTarget
                    && (isOnTargetMonth(stickyText, monthId, monthEng)
                    || isOnTargetMonth(headerText, monthId, monthEng)
                    || (currentMonthText == null
                    && isOnTargetMonth(lastKnownMonthText, monthId, monthEng)));
            if (headerLaterThanTarget) {
                logger.log("attempt " + attempt + " sticky on/near target but header='"
                        + headerText + "' is later than '" + date
                        + "' - continuing coarse navigation");
            }

            if (!onTargetMonth) {
                onTargetExposeAttempts = 0;

                // Only a STABLE, readable progress month that does not change
                // counts as stuck. Null sticky/header mid-scroll is expected and
                // must not abort (false boundary-stuck on Aug→Nov).
                if (progressText != null) {
                    if (progressText.equals(previousMonthText)) {
                        stuckCount++;
                        if (stuckCount >= 4) {
                            dumpCalendarStateOnFailure(date, day, monthId, monthEng,
                                    "boundary-stuck", progressText);
                            throw new RuntimeException(
                                    "Calendar appears to be at its boundary at '"
                                            + progressText
                                            + "'; cannot navigate to '" + date + "'.");
                        }
                    } else {
                        stuckCount = 0;
                    }
                    previousMonthText = progressText;
                }

                // Resolve direction from live sticky/header when possible;
                // otherwise keep traveling in the last known correct direction
                // (critical for future→past: null must NOT flip DOWN back to UP).
                // When the header is ahead of the target, the lagging sticky
                // would compute same-month UP and scroll the wrong way.
                String directionMonthText = headerLaterThanTarget ? headerText : currentMonthText;
                Swipe direction = resolveNavigationDirection(
                        directionMonthText, lastKnownMonthText, lastCoarseDirection, date);
                lastCoarseDirection = direction;
                logger.log("attempt " + attempt + " moving " + direction
                        + " toward target month '" + monthEng + "/" + monthId + "'");
                driverTools.swipeByDistance(CALENDAR_SCROLLABLE, direction, 0.5);
                settleCalendarAfterSwipe();
            } else {
                // On target month: Today label first (scrollIntoView if needed),
                // then geometry - but only accept clicks that register the
                // correct month/day (blocks sticky-lag Sep-4 false positives).
                if (preferTodayLabel) {
                    if (tryClickTodayCell()
                            && isDateRegisteredInCalendar(date, day, monthEng, monthId)) {
                        return;
                    }
                    if (scrollTodayIntoView() && tryClickTodayCell()
                            && isDateRegisteredInCalendar(date, day, monthEng, monthId)) {
                        return;
                    }
                }
                if (clickDayByGeometry(date, day, monthId, monthEng)) {
                    if (!preferTodayLabel
                            || isDateRegisteredInCalendar(date, day, monthEng, monthId)) {
                        return;
                    }
                    logger.softWarning("Geometry clicked day '" + day + "' but calendar "
                            + "did not register '" + date + "'; continuing expose.");
                }

                onTargetExposeAttempts++;
                if (onTargetExposeAttempts > maxExposeAttempts) {
                    dumpCalendarStateOnFailure(date, day, monthId, monthEng,
                            "expose-exhausted", currentMonthText);
                    throw new RuntimeException(
                            "Sticky on target month '" + monthEng + "/" + monthId
                                    + "' but day '" + day + "' could not be exposed after "
                                    + onTargetExposeAttempts + " expose-swipes.");
                }

                // Expose swipes while already on the target month:
                //  - Today: DOWN toward the start of the current month.
                //  - Arriving from a past month (last coarse UP): keep exposing
                //    UP. Early-day DOWN at the Aug/Sep boundary bounced back to
                //    August for 25 attempts while targeting 15 September 2026.
                //  - Early days after arriving from the future: DOWN to the start.
                //  - Late days: #1 = last coarse dir, #2+ = opposite.
                Swipe baseDir = lastCoarseDirection != null
                        ? lastCoarseDirection
                        : resolveNavigationDirection(currentMonthText, lastKnownMonthText, initialDirection, date);
                Swipe exposeDir;
                int dayNum = parseDayNumberSafe(day);
                boolean earlyDay = dayNum > 0 && dayNum <= 15;
                if (preferTodayLabel) {
                    exposeDir = Swipe.DOWN;
                } else if (baseDir == Swipe.UP) {
                    exposeDir = Swipe.UP;
                } else if (earlyDay) {
                    exposeDir = Swipe.DOWN;
                } else if (onTargetExposeAttempts == 1) {
                    exposeDir = baseDir;
                } else {
                    exposeDir = (baseDir == Swipe.DOWN) ? Swipe.UP : Swipe.DOWN;
                }
                logger.log("attempt " + attempt + " on target month, day not yet located - "
                        + "expose " + exposeDir + " 0.3 (#" + onTargetExposeAttempts + ")");
                driverTools.swipeByDistance(CALENDAR_SCROLLABLE, exposeDir, 0.3);
                settleCalendarAfterSwipe();
                previousMonthText = progressText != null ? progressText : currentMonthText;
            }
        }

        dumpCalendarStateOnFailure(date, day, monthId, monthEng,
                "max-attempts-exhausted", null);
        throw new RuntimeException(
                "Could not navigate to date '" + date + "' in the calendar after "
                        + maxAttempts + " attempts.");
    }

    /**
     * Picks the scroll direction for coarse month navigation.
     * <p>Order: parse live sticky/header → parse last known sticky/header →
     * keep {@code fallbackDirection} (seeded from {@link #getDirection(String)}).
     * Never invent {@link Swipe#UP} just because sticky briefly went null after a
     * backward swipe — that oscillation is what burned 25 attempts on Nov→Aug.
     */
    private Swipe resolveNavigationDirection(String currentMonthText, String lastKnownMonthText,
                                             Swipe fallbackDirection, String targetDate) {
        if (currentMonthText != null && !currentMonthText.isBlank()) {
            return computeDirectionFromVisibleMonth(currentMonthText, targetDate);
        }
        if (lastKnownMonthText != null && !lastKnownMonthText.isBlank()) {
            Swipe fromLastKnown = computeDirectionFromVisibleMonth(lastKnownMonthText, targetDate);
            logger.log("sticky missing - reusing lastKnown month '" + lastKnownMonthText
                    + "' → direction " + fromLastKnown);
            return fromLastKnown;
        }
        Swipe seeded = fallbackDirection != null ? fallbackDirection : Swipe.UP;
        logger.log("sticky missing and no lastKnown month - keeping seeded direction " + seeded);
        return seeded;
    }

    /**
     * Geometric resolver that bypasses the {@code month_name} anchor entirely.
     * Uses the sticky's bottom edge and the next visible month header's top
     * edge as boundaries to scope the day-cell search to a single month
     * region. Robust against RecyclerView virtualisation that recycles the
     * target month's section header out of the tree.
     *
     * @return true iff the resolver located AND clicked a day cell that
     *         belongs to the target month.
     */
    private boolean clickDayByGeometry(String date, String day, String monthId, String monthEng) {
        try {
            // Accept either sticky OR month_name as on-target. Sticky often lags
            // one month behind the visible header while scrolling UP; requiring
            // sticky alone rejected valid November cells while sticky still said
            // October.
            String stickyText = safeTextNoWait(STICKY_MONTH_NAME);
            String headerText = readTopmostMonthHeaderText();
            if (isHeaderLaterThanTarget(headerText, date)) {
                logger.log("Geometric resolver skipped: header '" + headerText
                        + "' is later than target '" + date + "'");
                return false;
            }
            if (!isOnTargetMonth(stickyText, monthId, monthEng)
                    && !isOnTargetMonth(headerText, monthId, monthEng)) {
                return false;
            }

            int stickyBottom = 0;
            try {
                List<WebElement> stickyHits = driverTools.getDriver().findElements(STICKY_MONTH_NAME.by());
                if (stickyHits != null && !stickyHits.isEmpty()) {
                    Rectangle sr = stickyHits.get(0).getRect();
                    stickyBottom = sr.getY() + sr.getHeight();
                }
            } catch (Exception ignored) {
                // Best-effort; geometry still works with stickyBottom=0.
            }

            Dimension win = driverTools.getDriver().manage().window().getSize();
            int viewportHeight = win.getHeight();

            // Snapshot month headers once - used both for the y-band ceiling and
            // for per-cell closest-preceding attribution (critical: sticky can
            // still say "August" while September day cells are already below it).
            List<WebElement> headers = List.of();
            try {
                headers = driverTools.getDriver().findElements(
                        AppiumBy.xpath("//*[contains(@resource-id,'month_name')]"));
            } catch (Exception ignored) {
                // Fall through with empty headers.
            }

            int bandTop = stickyBottom;
            int bandBottom = viewportHeight;
            for (WebElement h : headers) {
                try {
                    int top = h.getRect().getY();
                    if (top > stickyBottom && top < bandBottom) {
                        bandBottom = top;
                    }
                } catch (Exception ignored) {
                    // Skip stale header.
                }
            }

            // sticky=August + "September" header in view: the sticky-to-header
            // band is August. Re-scope from the target month header downward
            // so September days below that header can be clicked.
            if (!isOnTargetMonth(stickyText, monthId, monthEng)) {
                int targetHeaderBottom = -1;
                int bestHeaderY = Integer.MAX_VALUE;
                for (WebElement h : headers) {
                    try {
                        String ht = h.getText();
                        if (!isOnTargetMonth(ht, monthId, monthEng)) continue;
                        Rectangle hr = h.getRect();
                        if (hr.getY() < bestHeaderY) {
                            bestHeaderY = hr.getY();
                            targetHeaderBottom = hr.getY() + hr.getHeight();
                        }
                    } catch (Exception ignored) {
                        // Skip stale header.
                    }
                }
                if (targetHeaderBottom >= 0) {
                    bandTop = targetHeaderBottom;
                    bandBottom = viewportHeight;
                    for (WebElement h : headers) {
                        try {
                            int top = h.getRect().getY();
                            if (top > targetHeaderBottom && top < bandBottom) {
                                bandBottom = top;
                            }
                        } catch (Exception ignored) {
                            // Skip stale header.
                        }
                    }
                    logger.log("Geometric band re-scoped below target month header y="
                            + bandTop + " to " + bandBottom
                            + " (sticky='" + stickyText + "')");
                }
            }

            // Candidate day cells: ll_day_group containers whose day number
            // appears either as a TextView@text descendant OR as a content-
            // desc on the cell itself (covers Indonesian holiday cells).
            List<WebElement> candidates = driverTools.getDriver().findElements(
                    AppiumBy.xpath("//*[contains(@resource-id,'ll_day_group')"
                            + " and (.//android.widget.TextView[@text='" + day + "']"
                            + "      or @content-desc='" + day + "'"
                            + "      or starts-with(@content-desc,'" + day + " ')"
                            + "      or contains(@content-desc,' " + day + " ')"
                            + "      or contains(@content-desc,'-" + day + "-'))]"));

            for (WebElement cell : candidates) {
                Rectangle r = cell.getRect();
                if (r.getWidth() <= 0 || r.getHeight() <= 0) continue;
                int top = r.getY();
                int bottom = top + r.getHeight();
                // Cell must sit inside the target-month y-band AND the viewport
                // so the click actually lands on it.
                if (top >= bandTop && bottom <= bandBottom && bottom <= viewportHeight) {
                    if (r.getWidth() > 300) continue; // RecyclerView guard.
                    // Closest-preceding month_name must be the target month.
                    // Without this, a lagging sticky + next-month day cell in
                    // the same y-band selects the wrong month (Aug sticky → Sep 4).
                    String ownerMonth = closestPrecedingMonthHeaderText(headers, top);
                    boolean ownerMatches = isOnTargetMonth(ownerMonth, monthId, monthEng);
                    // Target month_name is often virtualised (header recycled);
                    // sticky on-target + header not later is enough to accept.
                    boolean virtualisedTarget = ownerMonth == null
                            && isOnTargetMonth(stickyText, monthId, monthEng);
                    if (!ownerMatches && !virtualisedTarget) {
                        logger.log("Geometric candidate day '" + day + "' at y=["
                                + top + "," + bottom + "] rejected: preceding month='"
                                + ownerMonth + "' (want " + monthEng + "/" + monthId + ")");
                        continue;
                    }
                    logger.log("Geometric resolver matched day '" + day + "' at y=["
                            + top + "," + bottom + "] (band=[" + bandTop + ","
                            + bandBottom + "], preceding_month='" + ownerMonth + "')");
                    cell.click();
                    // Mirror tryClickIfDayInViewport's settle so the calendar's
                    // state machine commits the selection before any subsequent
                    // action (return-leg clickDate, or the SAVE button) fires.
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            logger.softWarning("Geometric resolver threw: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns the text of the month_name header whose top-y is closest to
     * {@code cellTop} from above (visual stand-in for XPath preceding::[last()]).
     */
    private String closestPrecedingMonthHeaderText(List<WebElement> headers, int cellTop) {
        if (headers == null || headers.isEmpty()) return null;
        String bestText = null;
        int bestY = Integer.MIN_VALUE;
        for (WebElement h : headers) {
            try {
                Rectangle hr = h.getRect();
                int hy = hr.getY();
                if (hy <= cellTop && hy >= bestY) {
                    bestY = hy;
                    bestText = h.getText();
                }
            } catch (Exception ignored) {
                // Skip stale header.
            }
        }
        return bestText;
    }

    private int parseDayNumberSafe(String day) {
        try {
            return Integer.parseInt(day.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Best-effort diagnostic dump on calendar navigation failure. Logs the
     * sticky text, the in-tree {@code month_name} headers and their
     * coordinates, and a truncated page-source excerpt. Never throws -
     * called from inside catch/throw paths.
     */
    private void dumpCalendarStateOnFailure(String date, String day, String monthId,
                                            String monthEng, String reason,
                                            String currentMonthText) {
        try {
            logger.log("[CALENDAR FAIL] reason=" + reason
                    + " target='" + date + "' day='" + day
                    + "' monthEng='" + monthEng + "' monthId='" + monthId
                    + "' currentMonthText='" + currentMonthText + "'");

            try {
                List<WebElement> headers = driverTools.getDriver().findElements(
                        AppiumBy.xpath("//*[contains(@resource-id,'month_name')]"));
                logger.log("[CALENDAR FAIL] in-tree month_name headers: " + headers.size());
                for (WebElement h : headers) {
                    Rectangle r = h.getRect();
                    logger.log("[CALENDAR FAIL]   header text='" + h.getText()
                            + "' rect=" + r);
                }
            } catch (Exception ignored) {
                // Best effort.
            }

            try {
                String src = driverTools.getDriver().getPageSource();
                if (src != null) {
                    int len = Math.min(src.length(), 8000);
                    logger.log("[CALENDAR FAIL] pageSource (first " + len + " chars):\n"
                            + src.substring(0, len));
                }
            } catch (Exception ignored) {
                // Best effort.
            }
        } catch (Exception ignored) {
            // Diagnostics must never crash the test.
        }
    }

    /**
     * Clicks the calendar cell labelled {@code Today} / {@code Hari ini} when it
     * is present below the sticky header. Used by {@link #clickDateToday} so we
     * do not depend on the day-number XPath for the current date (that cell also
     * carries price text and a "Today" label, which made the day-4 locator flaky).
     */
    private boolean tryClickTodayCell() {
        try {
            List<WebElement> cells = driverTools.getDriver().findElements(TODAY_DAY_CELL.by());
            if (cells == null || cells.isEmpty()) {
                // Fallback: resolve via the label TextView then its day-group ancestor.
                cells = resolveTodayCellsFromLabels();
            }
            if (cells == null || cells.isEmpty()) {
                return false;
            }

            int stickyBottom = 0;
            try {
                Optional<WebElement> stickyOpt = driverTools.waitForElementToPresenceSafe(
                        STICKY_MONTH_NAME, 1);
                if (stickyOpt.isPresent()) {
                    Rectangle stickyRect = stickyOpt.get().getRect();
                    stickyBottom = stickyRect.getY() + stickyRect.getHeight();
                }
            } catch (Exception ignored) {
                // Best-effort; fall through without sticky guard.
            }

            for (WebElement cell : cells) {
                if (!isElementInViewport(cell)) continue;
                Rectangle r;
                try {
                    r = cell.getRect();
                } catch (Exception e) {
                    continue;
                }
                if (r.getWidth() <= 0 || r.getHeight() <= 0 || r.getWidth() > 300) continue;
                if (stickyBottom > 0 && r.getY() < stickyBottom) {
                    logger.log("Today cell at y=" + r.getY()
                            + " is above sticky_bottom=" + stickyBottom + "; ignoring.");
                    continue;
                }
                logger.log("Clicking Today cell (rect=" + r + ")");
                try {
                    cell.click();
                } catch (Exception clickEx) {
                    logger.softWarning("Direct Today click failed (" + clickEx.getMessage()
                            + "); retrying via coordinate click.");
                    driverTools.clickElementByCoordinate(TODAY_DAY_CELL);
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.softWarning("tryClickTodayCell threw: " + e.getMessage());
            return false;
        }
    }

    private List<WebElement> resolveTodayCellsFromLabels() {
        try {
            List<WebElement> labels = driverTools.getDriver().findElements(TODAY_LABEL.by());
            if (labels == null || labels.isEmpty()) return List.of();
            List<WebElement> cells = new java.util.ArrayList<>();
            for (WebElement label : labels) {
                try {
                    List<WebElement> ancestors = label.findElements(
                            AppiumBy.xpath("./ancestor::*[contains(@resource-id,'ll_day_group')][1]"));
                    if (ancestors != null && !ancestors.isEmpty()) {
                        cells.add(ancestors.get(0));
                    } else {
                        cells.add(label);
                    }
                } catch (Exception ignored) {
                    // Skip stale label.
                }
            }
            return cells;
        } catch (Exception e) {
            return List.of();
        }
    }

    /**
     * Scrolls the calendar RecyclerView until the Today / Hari ini label is in
     * the accessibility tree. Native UiScrollable is more reliable than blind
     * expose swipes when early-month cells have been virtualised away.
     */
    private boolean scrollTodayIntoView() {
        try {
            logger.log("scrollTodayIntoView: scrolling calendar to Today/Hari ini");
            String expr = "new UiScrollable(new UiSelector().resourceIdMatches(\".*:id/tds_calendar_month_rv\"))"
                    + ".setAsVerticalList()"
                    + ".scrollIntoView(new UiSelector().textMatches(\"Today|Hari ini|Hari Ini\"))";
            driverTools.getDriver().findElement(AppiumBy.androidUIAutomator(expr));
            return true;
        } catch (Exception e) {
            logger.log("scrollTodayIntoView did not find Today label: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns true (and performs the click) only when the resolved element is
     * a single day cell, fully in the viewport, AND geometrically below the
     * sticky header. The width sanity check blocks a degenerate match where
     * {@code (...)[1]} would otherwise resolve to the days RecyclerView
     * container itself; the sticky-bottom check blocks the regression we
     * hit at 21:07 IST where a stale May-22 cell rendered above the sticky
     * was clicked while looking for August 22 (tap landed on a past,
     * unselectable Friday).
     *
     * <p>We click the resolved {@link WebElement} directly rather than
     * round-tripping through {@code clickElementByCoordinate(locator)} so
     * the click is provably on the verified element and not on a re-
     * resolution that may pick a different node.
     */
    private boolean tryClickIfDayInViewport(Locator dateLocator, String date) {
        // Phase-1 / post-open probe: allow a short appear wait while the picker animates.
        return tryClickIfDayInViewport(dateLocator, date, 2);
    }

    /**
     * @param appearWaitSeconds {@code 0} = wait-free {@code findElements} probe
     *                          (used inside the swipe loop). {@code >0} waits for
     *                          visibility that many seconds (initial open).
     */
    private boolean tryClickIfDayInViewport(Locator dateLocator, String date, int appearWaitSeconds) {
        Optional<WebElement> elOpt;
        if (appearWaitSeconds <= 0) {
            try {
                List<WebElement> hits = driverTools.getDriver().findElements(dateLocator.by());
                elOpt = (hits == null || hits.isEmpty())
                        ? Optional.empty()
                        : Optional.of(hits.get(0));
            } catch (Exception e) {
                elOpt = Optional.empty();
            }
        } else {
            elOpt = driverTools.waitForElementToAppear(dateLocator, appearWaitSeconds);
        }
        if (elOpt.isEmpty()) return false;
        WebElement el = elOpt.get();
        if (!isElementInViewport(el)) return false;

        Rectangle cellRect;
        try {
            cellRect = el.getRect();
        } catch (Exception e) {
            logger.softWarning("tryClickIfDayInViewport: getRect failed - " + e.getMessage());
            return false;
        }

        // Width sanity: a real day cell is roughly viewportWidth/7 (~150px on
        // 1080px). Anything wider is the days RecyclerView itself or a row
        // container; tapping its centre would land on the wrong day.
        if (cellRect.getWidth() > 300 || cellRect.getWidth() <= 0
                || cellRect.getHeight() <= 0) {
            logger.log("Resolved cell rect=" + cellRect
                    + " is not a single day cell; ignoring this match.");
            return false;
        }

        // Sticky-bottom guard. Any cell whose top-y is ABOVE the sticky's
        // bottom edge belongs to a month section that has scrolled (or is
        // scrolling) out of view - it must NOT be clicked. Without this
        // guard, a stale month_name + day cell still in the
        // AccessibilityNodeInfo tree (but visually obscured by the sticky)
        // would be tapped on its real screen coordinates - we observed this
        // resolving to a past, unselectable May-22 cell while targeting
        // August 22.
        try {
            List<WebElement> stickyHits = driverTools.getDriver().findElements(STICKY_MONTH_NAME.by());
            if (stickyHits != null && !stickyHits.isEmpty()) {
                Rectangle stickyRect = stickyHits.get(0).getRect();
                int stickyBottom = stickyRect.getY() + stickyRect.getHeight();
                if (cellRect.getY() < stickyBottom) {
                    logger.log("Resolved cell at y=" + cellRect.getY()
                            + " is above sticky_bottom=" + stickyBottom
                            + " (likely a stale prior-month cell); ignoring.");
                    return false;
                }
            }
        } catch (Exception ignored) {
            // Sticky read is best-effort; if it fails, fall through and
            // trust the locator's month-attribution predicate.
        }

        logger.log("Date '" + date + "' is in viewport (rect=" + cellRect + "); clicking.");
        try {
            el.click();
        } catch (Exception clickEx) {
            // If the cached element went stale between resolution and click
            // (page changed mid-flight), fall back to coordinate click
            // which re-resolves under the same locator.
            logger.softWarning("Direct click failed (" + clickEx.getMessage()
                    + "); retrying via coordinate click.");
            driverTools.clickElementByCoordinate(dateLocator);
        }
        // Brief settle so the calendar's state machine commits the
        // selection before any subsequent action (clickDate for the return
        // leg, or the SAVE button) fires.
        try {
            Thread.sleep(300);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        return true;
    }

    private boolean isOnTargetMonth(String currentMonthText, String monthId, String monthEng) {
        if (currentMonthText == null) return false;
        return (monthEng != null && !monthEng.isEmpty() && currentMonthText.contains(monthEng))
                || (monthId != null && !monthId.isEmpty() && currentMonthText.contains(monthId));
    }

    /**
     * True when the visible {@code month_name} header is a later calendar month
     * than {@code targetDate}. Used to ignore a lagging sticky that still shows
     * the target while the list is actually on the next month.
     */
    private boolean isHeaderLaterThanTarget(String headerText, String targetDate) {
        if (headerText == null || headerText.isBlank() || targetDate == null || targetDate.isBlank()) {
            return false;
        }
        return computeDirectionFromVisibleMonth(headerText, targetDate) == Swipe.DOWN;
    }

    /**
     * Scroll-position month only (sticky / topmost visible header). Excludes
     * {@link #SELECTED_DATE}, which does not update while the RecyclerView scrolls
     * and must not drive coarse navigation direction.
     *
     * <p>Uses wait-free {@code findElements} reads. The previous 1s presence-wait
     * on sticky routinely timed out mid-scroll (sticky is recycled while the
     * list settles), burned ~1s per attempt, and left the loop blind to month
     * headers that were already in the tree — which is exactly what produced
     * the false {@code boundary-stuck} on Aug→Nov navigation.
     */
    private String readScrollPositionMonthText() {
        return firstNonBlank(safeTextNoWait(STICKY_MONTH_NAME), readTopmostMonthHeaderText());
    }

    private static String firstNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) return a;
        if (b != null && !b.isBlank()) return b;
        return null;
    }

    /**
     * Topmost on-screen {@code month_name} header text, or {@code null}.
     * Wait-free; used when the sticky is mid-recycle after a swipe.
     */
    private String readTopmostMonthHeaderText() {
        try {
            List<WebElement> headers = driverTools.getDriver().findElements(
                    AppiumBy.xpath("//*[contains(@resource-id,'month_name')]"));
            if (headers == null || headers.isEmpty()) return null;

            WebElement best = null;
            int bestY = Integer.MAX_VALUE;
            for (WebElement h : headers) {
                try {
                    Rectangle r = h.getRect();
                    if (r.getWidth() <= 0 || r.getHeight() <= 0) continue;
                    // Prefer the topmost header that still intersects the screen.
                    if (r.getY() < bestY && (r.getY() + r.getHeight()) > 0) {
                        String t = h.getText();
                        if (t != null && !t.isBlank()) {
                            bestY = r.getY();
                            best = h;
                        }
                    }
                } catch (Exception ignored) {
                    // Skip stale header.
                }
            }
            return best != null ? best.getText() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /** Brief settle so sticky / month headers rehydrate after a coarse swipe. */
    private void settleCalendarAfterSwipe() {
        try {
            Thread.sleep(350);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private String safeText(Locator locator) {
        try {
            Optional<WebElement> el = driverTools.waitForElementToPresenceSafe(locator, 1);
            if (el.isPresent()) {
                return el.get().getText();
            }
        } catch (Exception e) {
            logger.softWarning("safeText: failed reading " + locator + " : " + e.getMessage());
        }
        return null;
    }

    /**
     * Live direction computation. UP scrolls forward in time, DOWN scrolls
     * backward. Falls back to UP (forward) if the input can't be parsed -
     * matches the most common test scenario (booking a future date).
     */
    private Swipe computeDirectionFromVisibleMonth(String currentMonthText, String targetDate) {
        if (currentMonthText == null || currentMonthText.isBlank()) return Swipe.UP;

        int targetMonthNum;
        int targetYear;
        try {
            String[] tParts = targetDate.trim().split("\\s+");
            targetMonthNum = CoreFunctions.monthToMonthNumber(tParts[1]);
            targetYear = Integer.parseInt(tParts[2]);
        } catch (Exception e) {
            logger.softWarning("computeDirection: could not parse target '" + targetDate + "'");
            return Swipe.UP;
        }

        String currentMonthLabel;
        int currentYear;
        try {
            String[] cParts = currentMonthText.trim().split("\\s+");
            // sticky / first-visible-header: "Juni 2026" (2 tokens).
            // selected-date fallback:        "24 Mei 2026" (3 tokens).
            if (cParts.length >= 3) {
                currentMonthLabel = cParts[1];
                currentYear = Integer.parseInt(cParts[2]);
            } else if (cParts.length == 2) {
                currentMonthLabel = cParts[0];
                currentYear = Integer.parseInt(cParts[1]);
            } else {
                return Swipe.UP;
            }
        } catch (Exception e) {
            logger.softWarning("computeDirection: could not parse current '" + currentMonthText + "'");
            return Swipe.UP;
        }

        int currentMonthNum;
        try {
            currentMonthNum = CoreFunctions.monthToMonthNumber(currentMonthLabel);
        } catch (Exception e) {
            logger.softWarning("computeDirection: unknown month label '" + currentMonthLabel + "'");
            return Swipe.UP;
        }

        int yearDiff = targetYear - currentYear;
        if (yearDiff != 0) return yearDiff > 0 ? Swipe.UP : Swipe.DOWN;

        int monthDiff = targetMonthNum - currentMonthNum;
        if (monthDiff > 0) return Swipe.UP;
        if (monthDiff < 0) return Swipe.DOWN;
        // Same month - shouldn't reach here because isOnTargetMonth would have
        // already matched. Default to forward as a safe fallback.
        return Swipe.UP;
    }

    private Locator getDateLocator(String monthId, String monthEng, String day){
        // Resolves a day cell that PROVABLY belongs to the target month
        // section. The trick is the {@code closest-preceding month_name}
        // predicate - this is the only XPath shape that survives all four
        // states the booking calendar actually shows in production:
        //
        //   1. Target month's header IS in the tree, no other month_name
        //      precedes the cell  -> closest-preceding is the target ->
        //      cell is included.
        //   2. Target month's header IS in the tree AND another month
        //      (e.g. May) has its header BEFORE it (i.e. we're scanning
        //      the August section while May/June/July headers are still
        //      in the tree above) -> closest-preceding is the target ->
        //      cell is included.
        //   3. Target month's header has been virtualised away by
        //      RecyclerView recycling, but its days are still rendered -
        //      closest-preceding is the previous month (or empty) -> cell
        //      is EXCLUDED. (This is the bug we hit at 21:07 IST: with the
        //      old "no non-target preceding" predicate, an empty preceding
        //      axis trivially satisfied the predicate and we silently
        //      clicked May 22 - a past, unselectable day - while looking
        //      for August 22.)
        //   4. The day cell is rendered but it's a non-target month's cell
        //      whose own header is the closest preceding -> EXCLUDED.
        //
        // Defensive monthTokens build: XPath's contains(@text,'')
        // evaluates to TRUE for every text-bearing node, so empty/null
        // tokens MUST be dropped from the predicate. We require at least
        // one valid token.
        StringBuilder monthTokens = new StringBuilder();
        if (monthId != null && !monthId.isEmpty() && !"null".equals(monthId)) {
            monthTokens.append("contains(@text,'").append(monthId).append("')");
        }
        if (monthEng != null && !monthEng.isEmpty() && !"null".equals(monthEng)) {
            if (monthTokens.length() > 0) monthTokens.append(" or ");
            monthTokens.append("contains(@text,'").append(monthEng).append("')");
        }
        if (monthTokens.length() == 0) {
            throw new IllegalArgumentException(
                    "Cannot build date locator: both monthId and monthEng are blank "
                            + "(monthId='" + monthId + "', monthEng='" + monthEng + "', day='" + day + "')");
        }

        // Day-cell match. The booking calendar uses two cell shapes:
        //   - normal day:   ll_day_group cell containing a TextView@text='<day>'
        //   - holiday day:  ll_day_group cell with @content-desc='<day> en_holiday_*'
        // Either shape resolves the same physical cell.
        String dayCellMatch = "contains(@resource-id,'ll_day_group')"
                + " and (.//android.widget.TextView[@text='" + day + "']"
                + "      or @content-desc='" + day + "'"
                + "      or starts-with(@content-desc,'" + day + " ')"
                + "      or contains(@content-desc,' " + day + " ')"
                + "      or contains(@content-desc,'-" + day + "-'))";

        // CLOSEST-preceding-month-name predicate. The reverse axis
        // [position()=1] step (written here as the trailing [1] on the
        // preceding::* step) selects the immediately-preceding month_name
        // header in document order, then we filter it by target tokens.
        // If no month_name precedes the cell, the step returns an empty
        // node-set and the outer predicate fails - the cell is rejected
        // (no false positives when the target's header is virtualised).
        String xpath =
                "(//*[" + dayCellMatch
                        + " and (preceding::*[contains(@resource-id,'month_name')])[last()]"
                        + "[" + monthTokens + "]"
                        + "])[1]";
        return new Locator("Date Locator", AppiumBy.xpath(xpath));
    }

    /**
     * Returns true only if the element is fully inside the current device viewport
     * (top edge >= 0 and bottom edge <= window height, with non-zero size).
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
    public void addPassenger(Integer adult, Integer child, Integer infant) {
        String methodName = "addPassenger";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.click(ADD_PASSENGER);
            logger.log("adding adult: " + adult);
            addPassenger(adult,"adult");
            logger.log("adding child: " + child);
            addPassenger(child,"child");
            logger.log("adding infant: " + infant);
            addPassenger(infant,"infant");
            driverTools.click(SAVE_BUTTON);
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    private Locator getPassengerCountLocator(String passengerType){
       return new Locator("Passenger Count",AppiumBy.xpath("//*[contains(@resource-id,'"+passengerType+"')]//*[contains(@resource-id,'count')]"));
    }
    private Locator getPassengerAddSubtractButtonLocator(String passengerType,String type){
    return new Locator("Passenger Add Subtract",AppiumBy.xpath("//*[contains(@resource-id,'"+passengerType+"')]//*[contains(@resource-id,'"+type+"')]"));
    }
    private void addPassenger(int amount,String passengerType){

        Locator count = getPassengerCountLocator(passengerType);
        Locator increasePassenger = getPassengerAddSubtractButtonLocator(passengerType,"increment");
        Locator decreasePassenger = getPassengerAddSubtractButtonLocator(passengerType,"decrement");

        int currentCount = Integer.parseInt(driverTools.getText(count).orElseThrow());
        int toAdd = amount-currentCount;

        boolean increase = true;
        if (toAdd < 0){
            increase = false;
            toAdd *= -1;
        }

        for (int i=0; i<toAdd; i++) {
            if(increase){
                driverTools.clickElementByCoordinate(increasePassenger);
            }else{
                driverTools.clickElementByCoordinate(decreasePassenger);
            }
        }

        currentCount = Integer.parseInt(driverTools.getText(count).orElseThrow());

        Assertion.assertThat("Correct Passenger Amount Added",
                currentCount==amount, is(true));
    }

    @Override
    public void searchFlight() {
        String methodName = "searchFlight";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.click(SAVE_BUTTON);
            FlightSearchResultPage<T> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
            flightSearchResultPage.checkReloadPopup();
            flightSearchResultPage.waitForPageLoad();
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void selectSelfTransferFilter() {
        driverTools.waitForElementToAppear(OPEN_FILTERS);
        driverTools.click(OPEN_FILTERS);
        driverTools.swipeToElement(OTHER_FILTERS, Swipe.UP, 30);
        driverTools.swipeToElement(SEE_MORE_OTHER_FILTERS, Swipe.UP, 10).ifPresent(driverTools::click);
        driverTools.waitForElementToPresence(SELF_TRANSFER);
        for (int i = 0; i < 5; i++) {
            if (driverTools.isElementDisplayed(SELF_TRANSFER)) {
                break;
            }
            driverTools.click(CLOSE_BUTTON);
            driverTools.waitForElementToAppear(CLOSE_BUTTON).ifPresent(driverTools::click);
            driverTools.click(BACK_BUTTON);
            FlightSearchPage<T> flightSearchPage = FlightSearchPage.getInstance(session);
            flightSearchPage.searchFlight();
            driverTools.waitForElementToAppear(OPEN_FILTERS);
            driverTools.click(OPEN_FILTERS);
            driverTools.swipeToElement(OTHER_FILTERS, Swipe.UP, 10);
            driverTools.click(SEE_MORE_OTHER_FILTERS);
            driverTools.waitForElementToPresence(SELF_TRANSFER);
        }

        driverTools.click(SELF_TRANSFER);
        driverTools.waitForElementToPresence(SAVE_BUTTON_FILTERS, 5).ifPresent(driverTools::click);
        driverTools.click(SAVE_FLIGHTS_BUTTON);
    }

    @Override
    public boolean isLowestPriceGuaranteePagePresent() {
        logger.log("Verifying lowest price guarantee page");
        driverTools.click(LPG_BANNER);
        driverTools.waitForElementToAppear(LPG_CLOSE);
        driverTools.switchToWebView();
        boolean lpg = driverTools.waitForElementToAppear(LPG_WEBVIEW,10).isPresent();
        driverTools.switchToNative();
        driverTools.click(LPG_CLOSE);
        return lpg;
    }

    @Override
    public void closePopup() {
        String methodName = "closePopup";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForElementToAppear(TIMEZONE_CHANGE_POPUP,3).ifPresent(driverTools::click);
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void clearRecentSearch(){
        if(driverTools.isElementDisplayed(RECENT_SEARCH_CONTAINER)){
            List<WebElement> recentSearches = driverTools.elements(RECENT_SEARCH_CONTAINER).orElseThrow();
            if(recentSearches.size()>0)
                driverTools.click(CLEAR_RECENT_SEARCH_BTN);
        }
    }

    @Override
    public void clickOnRecentSearch(){
        driverTools.click(RECENT_SEARCH_CONTAINER);
    }

    @Override
    public boolean checkLPGBanner(){
        WebElement lpgBanner = driverTools.waitForElementToPresence(LPG_BANNER, 20).orElseThrow();
        return driverTools.waitForElementToPresence(LPG_TEXT).isPresent();
    }

    @Override
    public void clickLPGBanner(){
        driverTools.waitForElementToPresence(LPG_BANNER, 20);
        driverTools.click(LPG_BANNER);
    }

    private String getMonthBasedOnLang(String lang,String date){
        String monthEng = CoreFunctions.getSectionOfString(date," ", 1);
        String monthId = CoreFunctions.monthEngToId(monthEng);
        if(lang.contains("en")){
            return CoreFunctions.monthLongToShortEn(monthEng);
        }
        return CoreFunctions.monthLongToShortId(monthId);
    }

    @Override
    public void verifySelectedDate(String dateSelectedDepart,String dateSelectedReturn,String lang){
        logger.log("depart date verification : " + dateSelectedDepart);
        String monthDepart = getMonthBasedOnLang(lang,dateSelectedDepart);
        String day = CoreFunctions.getSectionOfString(dateSelectedDepart," ", 0);

        String dateDepartExpected = day + " " + monthDepart;
        WebElement actualDateSelectedElement = driverTools.waitForElementToAppear(DATE_SELECTED_DEPART, 25).orElseThrow();
        String actualDateSelected = driverTools.getText(actualDateSelectedElement);
        Assertion.assertThat("is depart date selected is correct",actualDateSelected,is(containsString(dateDepartExpected)));

        logger.log("return date verification : " + dateSelectedReturn);
        String monthReturn = getMonthBasedOnLang(lang,dateSelectedReturn);
        String dayReturn = CoreFunctions.getSectionOfString(dateSelectedReturn," ", 0);

        String dateReturnExpected = dayReturn + " " + monthReturn;
        WebElement actualDateReturnSelectedElement = driverTools.waitForElementToAppear(DATE_SELECTED_RETURN, 25).orElseThrow();
        String actualDateReturnSelected = driverTools.getText(actualDateReturnSelectedElement);
        Assertion.assertThat("is return date selected is correct",actualDateReturnSelected,is(containsString(dateReturnExpected)));
    }

    @Override
    public void verifySelectedDate(String dateSelectedDepart,String lang){
        logger.log("depart date verification : " + dateSelectedDepart);
        String monthDepart = getMonthBasedOnLang(lang,dateSelectedDepart);
        String day = CoreFunctions.getSectionOfString(dateSelectedDepart," ", 0);

        String dateDepartExpected = day + " " + monthDepart;
        WebElement actualDateSelectedElement = driverTools.waitForElementToAppear(DATE_SELECTED_DEPART).orElseThrow();
        String actualDateSelected = driverTools.getText(actualDateSelectedElement);
        Assertion.assertThat("is depart date selected is correct",actualDateSelected,is(containsString(dateDepartExpected)));
    }

    private Locator getSearchResultLocator(){
        return new Locator("AIRPORT_SEARCH_RESULT", AppiumBy.xpath("(//*[contains(@resource-id,'tds_title_list')])[1]"));
    }

    @Override
    public String CheckSearchAirportResultsAlwaysOnTop(String location){
        driverTools.click(ORIGIN);
        logger.log("Searching location: " + location);
        driverTools.sendKeys(AIRPORT_FIELD,location);

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
                    driverTools.waitForElementToAppear(ECONOMY_CLASS).orElseThrow().getAttribute("selected").contains("true");
            case "premium economy" ->
                    driverTools.waitForElementToAppear(PREMIUM_ECONOMY_CLASS).orElseThrow().getAttribute("selected").contains("true");
            default -> throw new IllegalArgumentException("flight class: " + flightClass + " not implemented");
        };
    }

    @Override
    public boolean isOnFlightLandingPage() {
        driverTools.waitForElementToPresence(ORIGIN,5);
        return driverTools.isElementDisplayed(ORIGIN);
    }

    @Override
    public void deeplinkWithSelectedDateSrp(String origin,String destination,String originCityName,String destinationCityName,int daysToDepartureDate,int adult,int child,int infant){
        //only ios implementation
    }

    @Override
    public void deeplinkWithSelectedDateSrpRoundTrip(String origin,String destination,String originCityName,String destinationCityName,int daysToDepartureDate,int duration,int adult,int child,int infant){
        //only ios implementation
    }
}
