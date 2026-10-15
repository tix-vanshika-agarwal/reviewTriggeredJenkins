package com.tiket.page.bus.searchresultpage;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class BusSearchResultPageAndroid <T extends WebDriver> extends BusSearchResultPage<T>{

    public BusSearchResultPageAndroid(Session<T> session) {
        super(session);
    }
    
}
