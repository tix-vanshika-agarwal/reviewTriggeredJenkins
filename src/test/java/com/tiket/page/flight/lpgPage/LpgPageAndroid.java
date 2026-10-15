package com.tiket.page.flight.lpgPage;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class LpgPageAndroid <T extends WebDriver> extends LpgPage<T> {
    public LpgPageAndroid(Session<T> session) {
        super(session);
    }
}
