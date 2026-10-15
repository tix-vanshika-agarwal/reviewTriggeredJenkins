package com.tiket.SmartUI.page.car_rental;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class CarRentalSmartPage<T extends WebDriver> extends BasePage<T> {
    public CarRentalSmartPage(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> CarRentalSmartPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new CarRentalSmartPageAndroid<>(session);}
            case IOS -> {return new CarRentalSmartPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }
}
