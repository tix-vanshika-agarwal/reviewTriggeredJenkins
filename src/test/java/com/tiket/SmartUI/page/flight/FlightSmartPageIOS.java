package com.tiket.SmartUI.page.flight;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class FlightSmartPageIOS<T extends WebDriver> extends FlightSmartPage<T> {
    public static final Locator CHECK_OTHER_FLIGHTS = new Locator("CHECK_OTHER_FLIGHTS", AppiumBy.xpath("//*[@value=\"Check Other Flights\" or @value=\"Lihat Penerbangan Lain\"] "));
    public static final Locator CHANGE_DATE = new Locator("CHANGE_DATE", AppiumBy.xpath("//*[@name=\"Change\" or @name=\"Ubah\"]"));
    public static final Locator BACK_BUTTON_BOOKING_FORM = new Locator("BACK_BUTTON_BOOKING_FORM", AppiumBy.xpath("//*[@name=\"back\"]"));
    public static final Locator CLOSE_BOTTOMSHEET = new Locator("CLOSE_BOTTOMSHEET", AppiumBy.xpath("//*[@name=\"ic cancel grey\"]"));
    public static final Locator BACK_BUTTON_SRP = new Locator("BACK_BUTTON_SRP", AppiumBy.xpath("//*[@name=\"back_btn\"]"));
    public static final Locator ADD_CARD_COMPOUND = new Locator("ADD_CARD_COMPOUND", AppiumBy.xpath("//*[@name=\"extraprotection_btn_1\"]"));
    public static final Locator CANCEL_CARD_COMPOUND = new Locator("CANCEL_CARD_COMPOUND", AppiumBy.xpath("//*[@name=\"extraprotection_btn_0\"]"));
    public static final Locator OPEN_CALENDAR = new Locator("OPEN_CALENDAR",AppiumBy.xpath("//*[@name=\"date_ow\"]"));
    public static final Locator CLOSE_CALENDAR = new Locator("CLOSE_CALENDAR",AppiumBy.xpath("//*[@name=\"ic cancel grey\"]"));
    public FlightSmartPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public void pressBackButtonBookingForm() {
        driverTools.click(BACK_BUTTON_BOOKING_FORM);
    }

    @Override
    public void pressBackButtonSRP() {
        driverTools.click(BACK_BUTTON_SRP);
    }

    @Override
    public void checkOtherFlights() {
        driverTools.click(CHECK_OTHER_FLIGHTS);
    }

    @Override
    public void pressChangeDateButton() {
        driverTools.click(CHANGE_DATE);
    }

    @Override
    public void closeBottomSheet() {
        driverTools.click(CLOSE_BOTTOMSHEET);
    }

    @Override
    public void addCardCompound() {
        driverTools.swipeToElement(ADD_CARD_COMPOUND, Swipe.UP, 10);
        driverTools.click(ADD_CARD_COMPOUND);
    }

    @Override
    public void cancelCardCompound() {
        driverTools.click(CANCEL_CARD_COMPOUND);
    }

    @Override
    public void openAndSetCalendar(String departDate, String returnDate) {
        driverTools.click(OPEN_CALENDAR);
    }
    @Override
    public void closeCalendar() {
        driverTools.click(CLOSE_CALENDAR);
    }
}