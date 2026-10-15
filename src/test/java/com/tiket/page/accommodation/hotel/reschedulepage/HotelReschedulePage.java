package com.tiket.page.accommodation.hotel.reschedulepage;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class HotelReschedulePage<T extends WebDriver> extends BasePage<T> {

    public HotelReschedulePage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> HotelReschedulePage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new HotelReschedulePageAndroid<>(session);}
            case IOS -> {return new HotelReschedulePageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract String chooseNewDate(boolean changeDate);
    public abstract void clickSubmitSearch();
    @Deprecated public abstract String changeNightDuration(String nightDuration); //reschedule flow change to webview
    public abstract String changeDate(String startDate, int night);
    public abstract boolean verifyRescheduleNotAllowedPresent();
    public abstract void clickTNCCheckboxNative();
}
