package com.tiket.page.accommodation.hotel.bookingdetailpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BookingDetailIOS<T extends WebDriver> extends BookingDetailPage<T> {

    public BookingDetailIOS(Session<T> session) { super(session); }

    public static final Locator HOTEL_NAME_TEXT = new Locator("HOTEL_NAME_TEXT", AppiumBy.iOSClassChain("**/*[`name=='TVCell<TDSText>'`][1]"));
    public static final Locator ROOM_NAME_TEXT = new Locator("ROOM_NAME_TEXT", AppiumBy.xpath("(//XCUIElementTypeCell[@name='TVCell<TDSText>'])[2]//*[@type='XCUIElementTypeStaticText']"));
    public static final Locator CHECKIN_TEXT = new Locator("CHECKIN_TEXT", AppiumBy.id("check_in_date"));
    public static final Locator CHECKOUT_TEXT = new Locator("CHECKOUT_TEXT", AppiumBy.id("check_out_date"));
    public static final Locator CHECKIN_TIME_TEXT = new Locator("CHECKIN_TIME_TEXT", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Check-out']//preceding-sibling::*[contains(@label, '-') or contains(@label, '24 Hours')]"));
    public static final Locator ROOM_GUEST_TEXT = new Locator("ROOM_GUEST_TEXT", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label, ' /')]"));
    public static final Locator CANCELLATION_TEXT = new Locator("CANCELLATION_TEXT", AppiumBy.name("TVCell<TACMLabelWithImageView>"));

    @Override
    public String getHotelNameBookingDetail() {
        String hotelName = driverTools.getText(HOTEL_NAME_TEXT).orElseThrow();
        logger.log("Hotel Name Booking Detail: " + hotelName);
        return hotelName;
    }

    @Override
    public String getRoomNameBookingDetail() {
        String roomName = driverTools.getText(ROOM_NAME_TEXT).orElseThrow();
        String[] roomNameSplit = roomName.split(" x ");
        roomName = roomNameSplit[1];
        logger.log("Room Name Booking Detail: " + roomName);
        return roomName;
    }

    @Override
    public String getCheckInDateBookingDetail() {
        String checkInDate = driverTools.getText(CHECKIN_TEXT).orElseThrow();
        logger.log("Check In Date Booking Detail: " + checkInDate);
        return checkInDate;
    }

    @Override
    public String getCheckOutDateBookingDetail() {
        String checkOutDate = driverTools.getText(CHECKOUT_TEXT).orElseThrow();
        logger.log("Check Out Date Booking Detail: " + checkOutDate);
        return checkOutDate;
    }

    @Override
    public String getCheckInTimeBookingDetail() {
        String checkInTime = driverTools.getText(CHECKIN_TIME_TEXT).orElseThrow();
        logger.log("Check In Time Booking Detail: " + checkInTime);
        return checkInTime;
    }

    @Override
    public String getRoomGuestBookingDetail() {
        String roomGuestText = driverTools.getText(ROOM_GUEST_TEXT).orElseThrow();
        String[] roomGuestArray = roomGuestText.split(" ");
        String roomGuest = roomGuestArray[1];
        logger.log("Room Guest Booking Detail: " + roomGuest);
        return roomGuest;
    }

    @Override
    public String getRoomBedBookingDetail(String roomBedText) {
        String roomBed = driverTools.getText(getRoomBedLocator(roomBedText)).orElseThrow();
        logger.log("Room Bed Booking Detail: " + roomBed);
        return roomBed;
    }

    private Locator getRoomBedLocator(String roomBedText) {
        return new Locator(roomBedText, AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label, '" + roomBedText + "')]"));
    }

    @Override
    public String getCancellationBookingDetail() {
        WebElement cancellationText = driverTools.swipeToElement(CANCELLATION_TEXT, Swipe.UP, 3).orElseThrow();
        String cancellation = driverTools.getText(cancellationText);
        logger.log("Room Cancellation Booking Detail: " + cancellation);
        return cancellation;
    }

    @Override
    public boolean checkChildGuest() {
        return false; //FIXME: Pending implementation
    }

    @Override
    public boolean checkChildMeal() {
        return false; //FIXME: Pending implementation
    }

    @Override
    public void openSeeMorePolicies() {
        //FIXME: Pending implementation
    }

    @Override
    public boolean checkChildrenPolicy() {
        return false; //FIXME: Pending implementation
    }

    @Override
    public void backToBookingFormPage() {
        //FIXME: Pending implementation
    }
}
