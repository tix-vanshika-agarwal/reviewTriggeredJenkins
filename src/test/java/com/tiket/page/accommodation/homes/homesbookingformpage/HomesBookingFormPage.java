package com.tiket.page.accommodation.homes.homesbookingformpage;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class HomesBookingFormPage<T extends WebDriver> extends BasePage<T> {

    public HomesBookingFormPage(Session<T> session) { super(session); }

    public static <T extends WebDriver> HomesBookingFormPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> { return new HomesBookingFormPageAndroid<>(session); }
            case IOS -> { return new HomesBookingFormPageIOS<>(session); }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract boolean checkLoyaltyBenefit();
}