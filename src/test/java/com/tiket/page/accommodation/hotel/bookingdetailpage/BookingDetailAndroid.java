package com.tiket.page.accommodation.hotel.bookingdetailpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.tiket.page.accommodation.hotel.bookingformpage.BookingFormAndroid.BACK_BUTTON;
import static com.tiket.page.accommodation.hotel.detailpage.DetailPageAndroid.CLOSE_POPUP_BUTTON;

public class BookingDetailAndroid<T extends WebDriver> extends BookingDetailPage<T> {

    public BookingDetailAndroid(Session<T> session) { super(session); }

    private static final Locator HOTEL_NAME_TEXT = new Locator("HOTEL_NAME_TEXT", AppiumBy.id("tv_hotel_name"));
    private static final Locator CHECKIN_TEXT = new Locator("CHECKIN_TEXT", AppiumBy.id("tv_date_check_in"));
    private static final Locator ROOM_NAME_TEXT = new Locator("ROOM_NAME_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'header')]//*[contains(@resource-id,'tv_title')]"));
    private static final Locator CHECKOUT_TEXT = new Locator("CHECKOUT_TEXT", AppiumBy.id("tv_date_check_out"));
    private static final Locator CHECKIN_TIME_TEXT = new Locator("CHECKIN_TIME_TEXT", AppiumBy.id("tv_time_check_in"));
    private static final Locator ROOM_GUEST_TEXT = new Locator("ROOM_GUEST_TEXT",  AppiumBy.xpath("//*[contains(@resource-id, 'tv_item_title') and (contains(@text, 'Guest') or contains(@text, 'Tamu') or contains(@text, 'Adult') or contains(@text, 'Dewasa'))]"));
    private static final Locator CANCELLATION_TEXT = new Locator("CANCELLATION_TEXT", AppiumBy.id("tv_cancellation"));
    private static final Locator CHILD_GUEST_COUNT = new Locator("CHILD_GUEST_COUNT", AppiumBy.xpath("//*[contains(@resource-id,'tv_item_title') and (contains(@text,'Child') or contains(@text,'Anak')) and not(contains(@text,'('))]"));
    private static final Locator CHILD_MEAL_COUNT = new Locator("CHILD_MEAL_COUNT", AppiumBy.xpath("//*[contains(@resource-id,'tv_item_title') and (contains(@text,'Child') or contains(@text,'Anak')) and contains(@text,'(')]"));
    private static final Locator SEE_MORE_POLICIES_BUTTON = new Locator("SEE_MORE_POLICIES_BUTTON", AppiumBy.id("btn_see_more_policy"));
    private static final Locator CHILDREN_POLICY_DESC = new Locator("CHILDREN_POLICY_DESC", AppiumBy.xpath("//*[contains(@resource-id,'tv_item_description') and (contains(@text,'Children') or contains(@text,'Anak'))]"));

    @Override
    public String getHotelNameBookingDetail() {
        String hotelName = driverTools.getText(HOTEL_NAME_TEXT).orElseThrow();
        logger.log("Hotel Name Booking Detail: " + hotelName);
        return hotelName;
    }

    @Override
    public String getRoomNameBookingDetail() {
        String roomName = driverTools.getText(ROOM_NAME_TEXT).orElseThrow();
        logger.log("Room Name Booking Detail: " + roomName);
        return roomName;
    }

    @Override
    public String getCheckInDateBookingDetail() {
        String checkInDate = driverTools.getText(CHECKIN_TEXT).orElseThrow();
        String[] dateRawArray = checkInDate.split(" ");
        if (dateRawArray[1].length() == 1) {
            dateRawArray[1] = "0" + dateRawArray[1];
            checkInDate = String.join(" ", dateRawArray);
        }
        logger.log("Check In Date Booking Detail: " + checkInDate);
        return checkInDate;
    }

    @Override
    public String getCheckOutDateBookingDetail() {
        String checkOutDate = driverTools.getText(CHECKOUT_TEXT).orElseThrow();
        String[] dateRawArray = checkOutDate.split(" ");
        if (dateRawArray[1].length() == 1) {
            dateRawArray[1] = "0" + dateRawArray[1];
            checkOutDate = String.join(" ", dateRawArray);
        }
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
        return new Locator(roomBedText, AppiumBy.xpath("//*[contains(@resource-id, 'tv_item_title') and contains(@text, '" + roomBedText + "')]"));
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
        driverTools.waitForElementToAppear(CHILD_GUEST_COUNT);
        driverTools.swipeToElement(CHILD_GUEST_COUNT, Swipe.UP, 3);
        return driverTools.isElementDisplayed(CHILD_GUEST_COUNT);
    }

    @Override
    public boolean checkChildMeal() {
        driverTools.waitForElementToAppear(CHILD_MEAL_COUNT);
        driverTools.swipeToElement(CHILD_MEAL_COUNT, Swipe.UP, 3);
        return driverTools.isElementDisplayed(CHILD_MEAL_COUNT);
    }

    @Override
    public void openSeeMorePolicies() {
        driverTools.swipeToElement(SEE_MORE_POLICIES_BUTTON, Swipe.UP, 10).ifPresent(driverTools::click);
    }

    @Override
    public boolean checkChildrenPolicy() {
        driverTools.waitForElementToAppear(CHILDREN_POLICY_DESC);
        driverTools.swipeToElement(CHILDREN_POLICY_DESC, Swipe.UP, 10);
        return driverTools.isElementDisplayed(CHILDREN_POLICY_DESC);
    }

    @Override
    public void backToBookingFormPage() {
        driverTools.click(CLOSE_POPUP_BUTTON);
        driverTools.click(CLOSE_POPUP_BUTTON);
    }
}
