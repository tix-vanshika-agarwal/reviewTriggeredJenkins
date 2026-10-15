package com.tiket.page.insurance;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class InsurancePageAndroid<T extends WebDriver> extends InsurancePage<T> {
    public InsurancePageAndroid(Session<T> session) {
        super(session);
    }
}

