package com.tiket.page.insurance.standalone;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class StandaloneSrpIOS<T extends WebDriver> extends StandaloneSrp<T> {
    public StandaloneSrpIOS(Session<T> session) {
        super(session);
    }
}
