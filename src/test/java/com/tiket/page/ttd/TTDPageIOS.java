package com.tiket.page.ttd;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class TTDPageIOS<T extends WebDriver> extends TTDPage<T> {

    public TTDPageIOS(Session<T> session) {
        super(session);
    }
}
