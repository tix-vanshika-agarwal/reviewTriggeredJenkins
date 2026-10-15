package com.tiket.page.carrental;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class CarRentalPageIOS<T extends WebDriver> extends CarRentalPage<T> {
    public CarRentalPageIOS(Session<T> session) {
        super(session);
    }
}