package com.tiket.page.insurance.standalone;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class StandaloneSrpAndroid <T extends WebDriver> extends StandaloneSrp<T> {
    public StandaloneSrpAndroid(Session<T> session) {
        super(session);
    }
}
