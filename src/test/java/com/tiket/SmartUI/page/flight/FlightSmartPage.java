package com.tiket.SmartUI.page.flight;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class FlightSmartPage <T extends WebDriver> extends BasePage<T> {
    public FlightSmartPage(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> FlightSmartPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new FlightSmartPageAndroid<>(session);}
            case IOS -> {return new FlightSmartPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }
    public abstract void pressBackButtonBookingForm();
    public abstract void pressBackButtonSRP();
    public abstract void checkOtherFlights();
    public abstract void pressChangeDateButton();
    public abstract void closeBottomSheet();
    public abstract void addCardCompound();
    public abstract void cancelCardCompound();
    public abstract void openAndSetCalendar(String departDate, String returnDate);
    public abstract void closeCalendar();
}
