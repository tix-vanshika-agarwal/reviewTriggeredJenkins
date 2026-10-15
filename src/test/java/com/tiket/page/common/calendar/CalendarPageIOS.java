package com.tiket.page.common.calendar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.utilities.HolidayResponse;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

public class CalendarPageIOS <T extends WebDriver> extends CalendarPage<T> {

    public CalendarPageIOS(Session<T> session) {
        super(session);
    }

    // iOS: name == accessibility id. AppiumBy.id often fails for this label.
    public static final Locator ACTIVE_MONTH = new Locator("ACTIVE_MONTH",
            AppiumBy.accessibilityId("currentActiveMonthLabel"));
    private static final Locator ACTIVE_MONTH_CLASS_CHAIN = new Locator("ACTIVE_MONTH_CC",
            AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`name == 'currentActiveMonthLabel'`]"));
    private static final Locator CALENDAR_MONTH_V3 = new Locator("CALENDAR_MONTH_V3",AppiumBy.iOSClassChain("**/*[`name CONTAINS 'month'`]"));
    private static final Locator CHECKIN_DATE_V3 = new Locator("CHECKIN_DATE_V3", AppiumBy.iOSClassChain("**/XCUIElementTypeButton/XCUIElementTypeStaticText[1]"));
    public static final Locator NEXT_MONTH = new Locator("NEXT_MONTH",AppiumBy.accessibilityId("calendarNextMonthButton"));

    @Override
    public void swipe(Swipe direction) {
        throw new NotImplementedException();
    }

    /*
    This method is assuming already inside calendar page, doesn't include clicking the save button since it may differ per vertical
     */
    @Override public boolean selectDate(String date){

        String dayName = CoreFunctions.getDayFromDate(date, "d MMMM yyyy");
        String month = CoreFunctions.getSectionOfString(date," ", 1);
        String day = CoreFunctions.getSectionOfString(date," ", 0);
        String year = CoreFunctions.getSectionOfString(date, " ", 2);

        String locatorName = dayName + ", " + month +" "+ day;

        RemoteWebDriver remoteWebDriver = (RemoteWebDriver) driver;
        Capabilities capabilities = remoteWebDriver.getCapabilities();
        Set<String> capabilityNames = capabilities.getCapabilityNames();
        boolean onBS = capabilityNames.stream().anyMatch(capabilityName -> capabilityName.contains("browserstack"));
        logger.log("on bs: "+onBS);
        if(onBS){
            locatorName = dayName + ", " + day +" "+ month;
        }

        if (clickVisibleDate(locatorName, 1)) {
            return true;
        }

        if (swipeToDateAndClick(locatorName, month, year)) {
            return true;
        }

        throw new NoSuchElementException("Date not found on calendar: " + locatorName + " (from " + date + ")");
    }

    private boolean clickVisibleDate(String locatorName, int maxTry) {
        Optional<WebElement> e = findDateElement(locatorName, maxTry);
        if (e.isEmpty()) {
            return false;
        }
        WebElement dateEl = e.orElseThrow();
        if (!"true".equals(driverTools.getValueAttribute(dateEl, "visible"))) {
            return false;
        }
        // XCUI marks days under the sticky month header as visible even when not tappable
        if (!isDateInCalendarGrid(dateEl)) {
            logger.log("date found but outside tappable calendar grid: " + locatorName);
            return false;
        }
        driverTools.clickElementByCoordinate(dateEl);
        return true;
    }

    /**
     * Date must sit below the sticky month label and above the check-in/out Save panel.
     */
    private boolean isDateInCalendarGrid(WebElement dateElement) {
        int dateTop = dateElement.getLocation().getY();
        int dateBottom = dateTop + dateElement.getSize().getHeight();

        int minY = 0;
        Optional<WebElement> activeMonth = driverTools.element(ACTIVE_MONTH, 1);
        if (activeMonth.isEmpty()) {
            activeMonth = driverTools.element(ACTIVE_MONTH_CLASS_CHAIN, 1);
        }
        if (activeMonth.isPresent()) {
            WebElement monthEl = activeMonth.get();
            minY = monthEl.getLocation().getY() + monthEl.getSize().getHeight() + 4;
        }

        Dimension size = driver.manage().window().getSize();
        int maxY = (int) (size.height * 0.78);

        return dateTop >= minY && dateBottom <= maxY;
    }

    private Optional<WebElement> findDateElement(String locatorName, int maxTry) {
        Locator byAccessibility = new Locator("Date Calendar", AppiumBy.accessibilityId(locatorName));
        Optional<WebElement> e = driverTools.element(byAccessibility, maxTry);
        if (e.isPresent()) {
            return e;
        }
        Locator byClassChain = new Locator("Date Calendar Button",
                AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`name == '" + locatorName + "'`]"));
        e = driverTools.element(byClassChain, maxTry);
        if (e.isPresent()) {
            return e;
        }
        // Exact day-month at end of name — CONTAINS 'August 9' wrongly matches August 19
        String dayMonth = locatorName.contains(", ")
                ? locatorName.substring(locatorName.indexOf(", ") + 2)
                : locatorName;
        Locator byEndsWithDay = new Locator("Date Calendar DayMonth",
                AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`name MATCHES '.*" + dayMonth + "$'`]"));
        return driverTools.element(byEndsWithDay, maxTry);
    }

    private boolean swipeToDateAndClick(String locatorName, String month, String year) {
        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        // Swipe inside calendar grid (mid sheet) — do not depend on ACTIVE_MONTH bounds
        int yLaterStart = (int) (size.height * 0.62);
        int yLaterEnd = (int) (size.height * 0.40);
        int yEarlierStart = yLaterEnd;
        int yEarlierEnd = yLaterStart;

        int distanceToCorrectMonth = resolveMonthDistance(month, year);
        logger.log("calendar month distance to target: " + distanceToCorrectMonth);

        // Later months → swipe up first. Earlier months → swipe down first.
        // Same month (0): after check-in the sheet auto-scrolls forward and hides nearby
        // checkout days under the sticky header — swipe earlier first, then later.
        if (distanceToCorrectMonth > 0) {
            if (swipeUntilDateFound(locatorName, x, yLaterStart, yLaterEnd,
                    Math.max(Math.min(Math.abs(distanceToCorrectMonth) + 2, 8), 3))) {
                return true;
            }
            return swipeUntilDateFound(locatorName, x, yEarlierStart, yEarlierEnd, 6);
        }
        if (swipeUntilDateFound(locatorName, x, yEarlierStart, yEarlierEnd,
                Math.max(Math.min(Math.abs(distanceToCorrectMonth) + 2, 8), 3))) {
            return true;
        }
        return swipeUntilDateFound(locatorName, x, yLaterStart, yLaterEnd, 6);
    }

    private int resolveMonthDistance(String targetMonth, String targetYear) {
        Optional<String> monthAndYear = readActiveMonthText();
        if (monthAndYear.isEmpty()) {
            // Same-month recovery (earlier-first) — typical after check-in auto-scroll.
            logger.log("ACTIVE_MONTH unavailable — defaulting to same-month earlier swipe");
            return 0;
        }
        String currentMonth = CoreFunctions.getSectionOfString(monthAndYear.get(), " ", 0);
        String currentYear = CoreFunctions.getSectionOfString(monthAndYear.get(), " ", 1);
        return distanceToCorrectMonth(currentMonth, targetMonth)
                + distanceToCorrectYear(currentYear, targetYear) * 12;
    }

    private Optional<String> readActiveMonthText() {
        Optional<WebElement> e = driverTools.element(ACTIVE_MONTH, 1);
        if (e.isEmpty()) {
            e = driverTools.element(ACTIVE_MONTH_CLASS_CHAIN, 1);
        }
        return e.map(driverTools::getText);
    }

    private boolean swipeUntilDateFound(String locatorName, int x, int y1, int y2, int maxSwipes) {
        for (int i = 0; i < maxSwipes; i++) {
            swipeCalendar(x, y1, y2);
            if (clickVisibleDate(locatorName, 1)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clickNextMonthButton() {
        driverTools.click(NEXT_MONTH);
    }

    @SneakyThrows
    private ArrayList<HolidayResponse.Content> getApiHolidayList(HolidayResponse holidayResponse) {
        logger.log("HolidayResponse = " + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(holidayResponse));
        ArrayList<HolidayResponse.Content> listContent = holidayResponse.data.content;
        ArrayList<HolidayResponse.Content> apiHolidayList = new ArrayList<>();

        int restartLopp = 0;
        for (int i = 1; i <= listContent.size(); i++) {
            int apiIndex = i - 1;
            String[] apiDateSplit = listContent.get(apiIndex).date.split("-");
            String apiDate = apiDateSplit[0] + "-" + apiDateSplit[1];
            logger.log("API date = " + apiDate);

            String[] appCalendarMonthV3 = driverTools.getValueAttribute(CALENDAR_MONTH_V3, "name").split("_");
            String appMonthName = appCalendarMonthV3[appCalendarMonthV3.length - 1];
            int monthNumber = CoreFunctions.getMonthNumber(appMonthName);
            String appMonth = monthNumber < 10 ? "0" + monthNumber : String.valueOf(monthNumber);
            String[] checkinDateV3 = driverTools.getValueAttribute(CHECKIN_DATE_V3, "label").split(" ");
            String appYear = checkinDateV3[checkinDateV3.length - 1];
            String appDate = appYear + "-" + appMonth;
            logger.log("App date = " + appDate);

            if (apiDate.equals(appDate)) {
                logger.log("apiDate = " + apiDate + " &  appDate = " + appDate);
                apiHolidayList.add(listContent.get(apiIndex));
                break;
            } else {
                i = 0;
                clickNextMonthButton();
                restartLopp++;
                if (restartLopp > 10) {
                    logger.warn("There is no holiday this month = " + appDate);
                    break;
                }
            }
        }
        logger.log("apiHolidayList = " + apiHolidayList);
        return apiHolidayList;
    }

    @Override
    public boolean isHolidayValid(HolidayResponse holidayResponse, String language) {
        ArrayList<HolidayResponse.Content> apiHolidayList = getApiHolidayList(holidayResponse);
        int totalApiHoliday = apiHolidayList.size();
        logger.log("totalApiHoliday = " + totalApiHoliday);

        boolean isHolidayDateDisplayed = false;
        boolean isHolidayDescriptionDisplayed = false;

        for (int i = 0; i < totalApiHoliday; i++) {
            String apiDateMonth = apiHolidayList.get(i).date;
            String[] dateSplit = apiDateMonth.split("-");
            String apiMonth = dateSplit[1];
            String monthName = CoreFunctions.getMonthName(Integer.parseInt(apiMonth), language.toLowerCase());
            String shortMonth = language.equalsIgnoreCase("id") ? CoreFunctions.monthLongToShortId(monthName) : CoreFunctions.monthLongToShortEn(monthName);
            String apiDate = dateSplit[2];

            isHolidayDateDisplayed = driverTools.isElementDisplayed(holidayText(apiDate + " " + shortMonth, i + 1));
            if (!isHolidayDateDisplayed) logger.warn(i + " index on API doesn't showing the holiday date on App");

            String holidayDescription = "";
            for (HolidayResponse.Description description : apiHolidayList.get(i).descriptions) {
                if (description.lang.equals(language)) holidayDescription = description.content;
            }
            isHolidayDescriptionDisplayed = driverTools.isElementDisplayed(holidayText(holidayDescription, i + 1));
            if (!isHolidayDescriptionDisplayed) logger.warn(i + " index on API doesn't showing the holiday description on App");
        }

        return isHolidayDateDisplayed && isHolidayDescriptionDisplayed;
    }

    private Locator holidayText(String label, int index) {
        return new Locator("HOLIDAY_TEXT", AppiumBy.iOSClassChain("**/XCUIElementTypeTable/XCUIElementTypeCell[" + index + "]/XCUIElementTypeStaticText[`label == \"" + label + "\"`]"));
    }

    private int distanceToCorrectMonth(String currentMonth, String targetMonth){

        logger.log("target month: " + targetMonth);
        int targetMonthNum = CoreFunctions.getMonthNumber(targetMonth);

        logger.log("current month: " + currentMonth);
        int calendarMonthNum = CoreFunctions.getMonthNumber(currentMonth);

        int distanceBetweenMonths = targetMonthNum - calendarMonthNum;

        logger.log("distance between month: " + distanceBetweenMonths);

        return distanceBetweenMonths;
    }

    private int distanceToCorrectYear(String currentYear, String targetYear){
        logger.log("target year : " + targetYear);
        logger.log("current year : " + currentYear);

        int distanceBetweenMonths = Integer.parseInt(targetYear) - Integer.parseInt(currentYear);

        logger.log("distance between year: " + distanceBetweenMonths);

        return distanceBetweenMonths;
    }

    /*
    Swipe using coordinate to save time of finding element
     */
    private void swipeCalendar(int x, int y1, int y2){
        logger.log("swiping calendar");
        TouchAction action = new TouchAction((PerformsTouchActions) driver);
        action.press(PointOption.point(x, y1))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                .moveTo(PointOption.point(x, y2))
                .release()
                .perform();
    }
}
