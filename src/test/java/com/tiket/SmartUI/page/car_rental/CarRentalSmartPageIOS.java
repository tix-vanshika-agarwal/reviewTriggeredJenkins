package com.tiket.SmartUI.page.car_rental;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class CarRentalSmartPageIOS<T extends WebDriver> extends CarRentalSmartPage<T> {
    public CarRentalSmartPageIOS(Session<T> session) {
        super(session);
    }
}