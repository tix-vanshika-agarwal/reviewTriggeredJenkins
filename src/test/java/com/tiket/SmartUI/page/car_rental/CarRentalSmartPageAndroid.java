package com.tiket.SmartUI.page.car_rental;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class CarRentalSmartPageAndroid<T extends WebDriver> extends CarRentalSmartPage<T> {
    public CarRentalSmartPageAndroid(Session<T> session) {
        super(session);
    }

}