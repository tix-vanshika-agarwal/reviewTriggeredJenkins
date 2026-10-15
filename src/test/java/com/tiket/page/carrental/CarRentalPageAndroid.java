package com.tiket.page.carrental;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class CarRentalPageAndroid<T extends WebDriver> extends CarRentalPage<T> {
    public CarRentalPageAndroid(Session<T> session) {
        super(session);
    }
}