package com.tiket.page.accommodation.hotel.bookingdetailpage;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;

public abstract class BookingDetailPage<T extends WebDriver> extends BasePage<T> {

    public BookingDetailPage(Session<T> session) { super(session); }

    public static <T extends WebDriver> BookingDetailPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> { return new BookingDetailAndroid<>(session); }
            case IOS -> { return new BookingDetailIOS<>(session); }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract String getHotelNameBookingDetail();
    public abstract String getRoomNameBookingDetail();
    public abstract String getCheckInDateBookingDetail();
    public abstract String getCheckOutDateBookingDetail();
    public abstract String getCheckInTimeBookingDetail();
    public abstract String getRoomGuestBookingDetail();
    public abstract String getRoomBedBookingDetail(String roomBedText);
    public abstract String getCancellationBookingDetail();
    public boolean isBookingDetailCorrect(String[] formData, String[] detailData){
        boolean[] isEqual = new boolean[]{};
        if (formData.length != detailData.length) {
            logger.log("Array length is not equal");
            return false;
        } else {
            for (int i = 0; i < formData.length; i++) {
                if (formData[i].equals(detailData[i])) {
                    isEqual = ArrayUtils.add(isEqual, true);
                }
                else {
                    isEqual = ArrayUtils.add(isEqual, false);
                    logger.softWarning("Expected is " + formData[i] + ", but actual is " + detailData[i]);
                }
            }
            logger.log("Boolean result: " + Arrays.toString(isEqual));
            for (boolean result : isEqual) if (!result) return false;
            return true;
        }
    }
    public abstract boolean checkChildGuest();
    public abstract boolean checkChildMeal();
    public abstract void openSeeMorePolicies();
    public abstract boolean checkChildrenPolicy();
    public abstract void backToBookingFormPage();
}