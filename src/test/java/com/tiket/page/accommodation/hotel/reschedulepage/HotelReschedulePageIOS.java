package com.tiket.page.accommodation.hotel.reschedulepage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.common.calendar.CalendarPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.tiket.core.CoreFunctions.*;

public class HotelReschedulePageIOS<T extends WebDriver> extends HotelReschedulePage<T> {

    public HotelReschedulePageIOS(Session<T> session) {
        super(session);
    }

    public static final Locator CHECK_IN_VIEW = new Locator("CHECK_IN_VIEW", By.xpath("//XCUIElementTypeStaticText[@name='dateCheckInLabel']/parent::*"));
    public static final Locator CHECK_IN_DATE = new Locator("CHECK_IN_DATE", By.name("dateCheckIn"));
    public static final Locator LAST_DATE = new Locator("LAST_DATE", By.xpath("(//XCUIElementTypeCell[contains(@name,'calendarHotelCell')])[last()]//XCUIElementTypeStaticText"));
    public static final Locator NEXT_MONTH_BUTTON = new Locator("NEXT_MONTH_BUTTON", By.xpath("//XCUIElementTypeOther[@name='calendarHotelNextMonthButton']"));
    public static final Locator SUBMIT_RESCHEDULE_SEARCH = new Locator("SUBMIT_RESCHEDULE_SEARCH", By.xpath("//*[@name='Check availability' or @name='Cek ketersediaan']"));
    private static final Locator CHECK_IN_VIEW_VALUE = new Locator("CHECK_IN_VIEW_VALUE", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Select New Dates' or @name='Pilih Tanggal Baru']/following-sibling::XCUIElementTypeStaticText[1]"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.name("leftBarButtonItem"));

    @Override
    public String chooseNewDate(boolean changeDate) {
        int newStartDate;
        if (changeDate) {
            clickCheckIn();
            driverTools.waitForElementToPresence(CHECK_IN_DATE);
            int checkInDate = Integer.parseInt(driverTools.getValueAttribute(CHECK_IN_DATE, "value").split(" ")[0]);

            driverTools.waitForElementToPresence(LAST_DATE);
            int dateLast = Integer.parseInt(driverTools.getValueAttribute(LAST_DATE, "value"));

            boolean isNextMonth = false;
            if (dateLast - checkInDate < 3) {
                clickNextMonth();
                isNextMonth = true;
            }

            driverTools.waitForElementToPresence(LAST_DATE);
            dateLast = Integer.parseInt(driverTools.getValueAttribute(LAST_DATE, "value"));
            newStartDate = checkInDate;
            int count = 0;

            while (newStartDate == checkInDate && count < 5) {
                newStartDate = !isNextMonth ? generateRandomInt(checkInDate, dateLast - 1) : generateRandomInt(1, dateLast);
                count++;
            }

            clickDate(newStartDate);

            int newUntilDate = generateRandomInt(newStartDate + 1, dateLast);
            clickDate(newUntilDate);
        } else {
            driverTools.swipeToPresentedElement(CHECK_IN_VIEW_VALUE, Swipe.UP, 5);
            driverTools.waitForElementToPresence(CHECK_IN_VIEW_VALUE);
            String dateRange = driverTools.getText(CHECK_IN_VIEW_VALUE).orElseThrow();
            newStartDate = CoreFunctions.getIntFromString(dateRange.split("-")[0]).orElseThrow();
        }

        clickSubmitSearch();

        return Integer.toString(newStartDate);
    }

    @Override
    public void clickSubmitSearch() {
        driverTools.swipeToElement(SUBMIT_RESCHEDULE_SEARCH, Swipe.UP, 5);
        driverTools.click(SUBMIT_RESCHEDULE_SEARCH);
    }

    private void clickDate(int date) {
        driverTools.click(getDateLocator(date));
    }

    private Locator getDateLocator(int date) {
        return new Locator("DATE_"+date, By.xpath("//XCUIElementTypeCell[contains(@name,'calendarHotelCell"+date+"')]"));
    }

    private void clickNextMonth() {
        driverTools.click(NEXT_MONTH_BUTTON);
    }

    private void clickCheckIn() {
        driverTools.click(CHECK_IN_VIEW);
    }

    @Override
    public String changeNightDuration(String nightDuration) {
        clickCheckIn();
        WebElement checkInDate = driverTools.waitForElementToPresence(CHECK_IN_DATE, 60).orElseThrow();
        String checkIn = driverTools.getValueAttribute(checkInDate, "value");
        String checkInFormat = convertDateFormat(checkIn, "dd MMM yyyy", "d MMMM yyyy");

        CalendarPage<T> calendarPage = CalendarPage.getInstance(session);
        calendarPage.selectDate(checkInFormat);

        String checkOut = addToLongDate(checkInFormat, Integer.parseInt(nightDuration));
        calendarPage.selectDate(checkOut);

        LandingPage<T> landingPage = LandingPage.getInstance(session);
        landingPage.clickSubmit();

        clickSubmitSearch();

        String startDate = checkIn.split(" ")[0];
        logger.log("startDate-> " + startDate);
        return startDate;
    }

    @Override
    public String changeDate(String departDate,int night) {
        return null;
    }//fixme: implementation pending

    @Override
    public boolean verifyRescheduleNotAllowedPresent() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyRescheduleNotAllowedPresent'");
    }

    @Override
    public void clickTNCCheckboxNative() {
        //Android Only
    }
}
