package com.tiket.page.ttd;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class TTDPageAndroid<T extends WebDriver> extends TTDPage<T> {

    public TTDPageAndroid(Session<T> session) {
        super(session);
    }
}
