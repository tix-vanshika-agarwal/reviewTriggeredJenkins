package com.tiket.page.accommodation.hotel.roomdetailpage;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class RoomDetailPage  <T extends WebDriver> extends BasePage<T> {

    public RoomDetailPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> RoomDetailPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> { return new RoomDetailPageAndroid<>(session); }
            case IOS -> { return new RoomDetailPageIOS<>(session); }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract String getHotelRoomNameOnRoomDetail();
    public abstract String getHotelRoomPriceOnRoomDetail();
    public abstract String getHotelRoomPriceOnRoomDetailAfterTax(String currency);

    /**
     * Opens the price detail bottom sheet on the room detail page and returns the
     * "Total to pay (after taxes)" value, which includes other fees and therefore
     * matches the total shown on the booking form (unlike the per-room after-tax price).
     */
    public abstract String getTotalPriceOnRoomDetail();
    public abstract Boolean hotelRoomDetailTabIsValid();
    public abstract void clickBookButton(int totalRoom);
    public abstract void clickAddRoomButton(int totalRoom);
    public abstract void seeAllphotos();
    public abstract boolean isPhotoGalleryVisible();
    public abstract Boolean verifyLoyaltyBenefitSection(String tierLevel);
    public abstract boolean checkChildGuest();
    public abstract boolean checkChildMeal();
    public abstract boolean checkChildRoomPolicy();
    public abstract String getRawHotelRoomNameOnRoomDetail();
    public abstract boolean checkRoomDetail(int totalGuest,int roomCapacity,String searchCriteria);

    /**
     * Whether the room info section on the room detail page shows the expected
     * attributes currently asserted by the platform implementations: capacity
     * (guest/tamu), meal pax (meal/makan/pax), bed type
     * (bed/tempat tidur), and smoking preference.
     *
     * Returns true only if all 4 are visible.
     */
    public abstract boolean isRoomInfoDetailsDisplayed();
    public abstract boolean isPayingThisAmountTextInGreen();
}
