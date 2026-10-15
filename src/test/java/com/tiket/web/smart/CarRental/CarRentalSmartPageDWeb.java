package com.tiket.web.smart.CarRental;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class CarRentalSmartPageDWeb<T extends WebDriver> extends CarRentalSmartPageWeb<T> {
    public CarRentalSmartPageDWeb(Session<T> session) {
        super(session);
    }
}
