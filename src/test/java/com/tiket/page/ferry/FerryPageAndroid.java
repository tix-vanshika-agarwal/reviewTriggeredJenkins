package com.tiket.page.ferry;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;


public class FerryPageAndroid  <T extends WebDriver> extends FerryPage<T> {

    public FerryPageAndroid(Session<T> session) {
        super(session);
    }
    
}
