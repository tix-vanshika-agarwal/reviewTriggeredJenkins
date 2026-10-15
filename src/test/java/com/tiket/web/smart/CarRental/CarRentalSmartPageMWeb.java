package com.tiket.web.smart.CarRental;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class CarRentalSmartPageMWeb <T extends WebDriver> extends CarRentalSmartPageWeb<T> {
    public CarRentalSmartPageMWeb(Session<T> session) {
        super(session);
    }
}
