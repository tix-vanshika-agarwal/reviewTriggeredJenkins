package com.tiket.web.smart.TTD;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class TTDSmartPageDWeb<T extends WebDriver> extends TTDSmartPageWeb<T> {
    public TTDSmartPageDWeb(Session<T> session) {
        super(session);
    }
}
