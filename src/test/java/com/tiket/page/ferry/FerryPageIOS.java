package com.tiket.page.ferry;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class FerryPageIOS  <T extends WebDriver> extends FerryPage<T> {

    public FerryPageIOS(Session<T> session) {
        super(session);
    }
    
}
