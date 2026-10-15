package com.tiket.web.page.accommodation.hotels.reschedulePage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.web.page.common.calendar.CalendarPageWeb;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;

public class HotelReschedulePageDweb<T extends WebDriver> extends HotelReschedulePageWeb<T> {

    public HotelReschedulePageDweb(Session<T> session) {
        super(session);
    }

    private static final Locator PREVIOUS_MONTH_BUTTON = new Locator("PREVIOUS_MONTH_BUTTON", AppiumBy.xpath("//*[contains(@class,'CalendarDesktop_month')]//*[contains(@class,'CalendarDesktop_prev_button')]"));
    private static final Locator NEXT_MONTH_BUTTON = new Locator("NEXT_MONTH_BUTTON", AppiumBy.xpath("//*[contains(@class,'CalendarDesktop_next_button')]"));
    private static final Locator NON_REFUND_CALENDAR_DESKTOP = new Locator("NON_REFUND_CALENDAR_DESKTOP",
            AppiumBy.xpath("//div[contains(@class,'CalendarDesktop_calendar')]"));
    private static final Locator ROOM_NAME = new Locator("ROOM_NAME", AppiumBy.xpath("//*[@data-testid='room-group-desktop-title-wrapper']//span[contains(@class,'Text_size_b1')]"));
    private static final Locator ROOM_DETAIL_BUTTON = new Locator("ROOM_DETAIL_BUTTON", AppiumBy.xpath("//button[contains(@class,'RatePlanInformation')]"));
    private static final Locator ROOM_NAME_ADDITIONAL_PRICE = new Locator("ROOM_NAME_ADDITIONAL_PRICE", AppiumBy.xpath("//*[contains(text(),'+')]//preceding::*[@data-testid='room-group-desktop-title-wrapper']//*[contains(@class,'Text_variant')]"));

    @Override
    public String getRoomName() {
        WebElement roomName = driverTools.waitForElementToAppear(ROOM_NAME).orElseThrow();
        return driverTools.getText(roomName);
    }

    @Override
    public String openRoomDetail() {
        driverTools.waitForElementToAppear(ROOM_DETAIL_BUTTON);
        String roomName = driverTools.getText(ROOM_NAME).orElseThrow();
        logger.log("Room name: " + roomName);
        driverTools.click(ROOM_DETAIL_BUTTON);
        return roomName;
    }

    @Override
    public String getRoomNameWithAdditionalPrice() {
        driverTools.scrollIntoWebView(ROOM_NAME_ADDITIONAL_PRICE);
        return driverTools.getText(ROOM_NAME_ADDITIONAL_PRICE).orElseThrow();
    }

    @Override
    public String changeNightDuration(int nightDuration) {
        driverTools.waitForElementToAppear(SELECT_NEW_DATE_BUTTON);
        driverTools.click(SELECT_NEW_DATE_BUTTON);

        if (nightDuration > 0) {
            if (driverTools.isElementDisplayed(PREVIOUS_MONTH_BUTTON))
                driverTools.clickWithJavascriptExecutor(PREVIOUS_MONTH_BUTTON);

            //choose new check-in date by adding 1 day from current check-in date
            driverTools.waitForElementToAppear(nextDateLocator(1)).orElseThrow();
            driverTools.clickWithJavascriptExecutor(nextDateLocator(1));

            //choose new check-out date by adding as per night duration from new check-in date
            if (driverTools.waitForElementToAppear(nextDateLocator(nightDuration)).isEmpty()) {
                driverTools.clickWithJavascriptExecutor(NEXT_MONTH_BUTTON);
            }
            driverTools.clickWithJavascriptExecutor(nextDateLocator(nightDuration));
        }

        String checkInDate = driverTools.getText(SELECT_NEW_DATE_BUTTON).orElseThrow().split(" ")[0].replaceAll("[^0-9]", "");
        logger.log("checkInDate: " + checkInDate);

        WebElement checkAvailButton = driverTools.waitForElementToAppear(CHECK_AVAILABILITY_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(checkAvailButton);
        return checkInDate;
    }

    @Override
    public String changeNonRefundableNightDuration(int addDays) {
        // No-op short-circuit (matches mWeb): don't open the picker for a non-positive
        // shift; otherwise the calendar overlay would stay open with no selection.
        if (addDays <= 0) {
            return getNonRefundableSelectedDateText();
        }

        // Anchor the current check-in BEFORE opening the picker — the banner element
        // detaches once the calendar overlay covers it.
        LocalDate currentCheckIn = parseBannerDate(getNonRefundableCheckInDateBannerText());
        LocalDate newCheckIn = currentCheckIn.plusDays(addDays);

        driverTools.waitForElementToAppear(NON_REFUND_SELECT_DATE_BUTTON, 15).orElseThrow();
        driverTools.clickWithJavascriptExecutor(NON_REFUND_SELECT_DATE_BUTTON);
        driverTools.waitForElementToAppear(NON_REFUND_CALENDAR_DESKTOP, 15).orElseThrow();

        // Non-refundable reschedule uses a single-tap calendar: one check-in click closes
        // the picker and the app preserves the original night count for check-out.
        // Do NOT call selectDate(checkIn, checkOut) — the second click can race the overlay
        // unmount and cause flakiness/timeouts.
        CalendarPageWeb<T> calendar = CalendarPageWeb.getInstance(session);
        calendar.selectSingleDate(newCheckIn.format(CALENDAR_DATE_FORMAT));

        driverTools.waitForElementToDisappear(NON_REFUND_CALENDAR_DESKTOP, 10);

        String newDateText = getNonRefundableSelectedDateText();
        logger.log("New non-refundable reschedule date range: " + newDateText);
        return newDateText;
    }
}
