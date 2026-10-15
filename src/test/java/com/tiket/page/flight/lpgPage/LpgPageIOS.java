package com.tiket.page.flight.lpgPage;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class LpgPageIOS <T extends WebDriver> extends LpgPage<T>{
    public LpgPageIOS(Session<T> session) {
        super(session);
    }
}
