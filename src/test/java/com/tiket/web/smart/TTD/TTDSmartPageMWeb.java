package com.tiket.web.smart.TTD;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class TTDSmartPageMWeb<T extends WebDriver> extends TTDSmartPageWeb<T> {
    public TTDSmartPageMWeb(Session<T> session) {
        super(session);
    }
}
