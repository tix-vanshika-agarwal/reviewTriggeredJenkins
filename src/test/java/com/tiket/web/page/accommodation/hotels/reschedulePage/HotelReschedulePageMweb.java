package com.tiket.web.page.accommodation.hotels.reschedulePage;

import java.time.LocalDate;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.page.common.calendar.CalendarPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;

import io.appium.java_client.AppiumBy;

public class HotelReschedulePageMweb<T extends WebDriver> extends HotelReschedulePageWeb<T> {

    public HotelReschedulePageMweb(Session<T> session) {
        super(session);
    }

    private static final Locator ROOM_NAME = new Locator("ROOM_NAME", AppiumBy.xpath("//*[contains(@class,'room_group_expanded_title')]//*[contains(@class,'text')]"));
    private static final Locator ROOM_NAME_ADDITIONAL_PRICE = new Locator("ROOM_NAME_ADDITIONAL_PRICE", AppiumBy.xpath("//*[contains(text(),'+')]//preceding::*[contains(@class,'FacilitiesNudges')]//preceding-sibling::*[contains(@class,'Text_variant') and not(contains(@class,'facility_item'))]"));

    // Refundable flow still renders the legacy DatePicker bottom-sheet (separate component
    // from the non-refundable calendar). Keep these locators for changeNightDuration().
    private static final Locator CHECK_IN_DATE = new Locator("CHECK_IN_DATE", AppiumBy.xpath("//*[contains(@class,'DatePicker_check_in')]//p"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", AppiumBy.xpath("//button[contains(@class,'DatePicker_button_save')]"));

    // The non-refundable reschedule date picker is the shared CalendarMobile bottom-sheet
    // ({@code <div class="CalendarMobile_calendar">} inside a {@code data-testid='bottom-sheet-body'}).
    // We wait on the bottom-sheet body scoped by the calendar root so we don't false-match
    // unrelated bottom sheets (reason picker, etc.).
    private static final Locator NON_REFUND_CALENDAR_BOTTOM_SHEET = new Locator(
            "RESCHEDULE_NON_REFUND_CALENDAR_BOTTOM_SHEET",
            AppiumBy.xpath("//*[@data-testid='bottom-sheet-body'][.//*[contains(@class,'CalendarMobile_calendar')]]"));

    @Override
    public String getRoomName() {
        WebElement roomName = driverTools.waitForElementToAppear(ROOM_NAME).orElseThrow();
        return driverTools.getText(roomName);
    }

    @Override
    public String openRoomDetail() {
        driverTools.waitForElementToAppear(ROOM_NAME);
        String roomName = driverTools.getText(ROOM_NAME).orElseThrow();
        logger.log("Room name: " + roomName);
        driverTools.click(ROOM_NAME);
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
        driverTools.waitForElementToAppear(CHECK_IN_DATE).orElseThrow();

        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
            homePageWeb.continueWithoutApp(5);
        }

        if (nightDuration > 0) {
            //choose new check-in date by adding 1 day from current check-in date
            driverTools.scrollIntoWebView(nextDateLocator(1));
            driverTools.clickWithJavascriptExecutor(nextDateLocator(1));

            //choose new check-out date by adding as per night duration from new check-in date
            driverTools.scrollIntoWebView(nextDateLocator(nightDuration));
            driverTools.clickWithJavascriptExecutor(nextDateLocator(nightDuration));
        }

        String checkInDate = driverTools.getText(CHECK_IN_DATE).orElseThrow().split(" ")[0].replaceAll("[^0-9]", "");
        logger.log("checkInDate: " + checkInDate);

        driverTools.click(SAVE_BUTTON);
        WebElement checkAvailButton = driverTools.waitForElementToAppear(CHECK_AVAILABILITY_BUTTON).orElseThrow();
        driverTools.click(checkAvailButton);
        return checkInDate;
    }

    @Override
    public String changeNonRefundableNightDuration(int addDays) {
        // No-op short-circuit: caller asked for "don't change the date".
        if (addDays <= 0) {
            return getNonRefundableSelectedDateText();
        }

        // 1) Anchor the current check-in BEFORE we open the picker — the banner element
        //    detaches once the bottom sheet covers it.
        LocalDate currentCheckIn = parseBannerDate(getNonRefundableCheckInDateBannerText());
        LocalDate newCheckIn = currentCheckIn.plusDays(addDays);

        // 2) Open the calendar bottom-sheet. The non-refundable flow renders the shared
        //    CalendarMobile_calendar widget that {@link CalendarPageMWeb} already targets
        //    (data-month-index + aria-label day buttons). The legacy DatePicker_check_in
        //    DOM is NOT used here.
        driverTools.waitForElementToAppear(NON_REFUND_SELECT_DATE_BUTTON, 15).orElseThrow();
        driverTools.clickWithJavascriptExecutor(NON_REFUND_SELECT_DATE_BUTTON);

        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
            homePageWeb.continueWithoutApp(5);
        }

        driverTools.waitForElementToAppear(NON_REFUND_CALENDAR_BOTTOM_SHEET, 15).orElseThrow();

        // 3) Pick the new check-in. The non-refundable picker is a SINGLE-tap widget: it
        //    auto-closes after one click and the backend preserves the original night
        //    count, so the new check-out is derived from the new check-in automatically.
        //    Do NOT call selectDate(checkIn, checkOut) here — the second click would race
        //    with the bottom-sheet unmount and time out trying to re-locate month headers.
        CalendarPageWeb<T> calendar = CalendarPageWeb.getInstance(session);
        calendar.selectSingleDate(newCheckIn.format(CALENDAR_DATE_FORMAT));

        // 4) Wait for the bottom-sheet to actually unmount before reading the form text,
        //    so we don't grab a stale value while the picker is animating out.
        driverTools.waitForElementToDisappear(NON_REFUND_CALENDAR_BOTTOM_SHEET, 10);

        String newDateText = getNonRefundableSelectedDateText();
        logger.log("New non-refundable reschedule date range: " + newDateText);
        return newDateText;
    }
}
