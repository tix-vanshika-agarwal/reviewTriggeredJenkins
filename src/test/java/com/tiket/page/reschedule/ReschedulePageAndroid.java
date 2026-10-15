package com.tiket.page.reschedule;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class ReschedulePageAndroid <T extends WebDriver> extends ReschedulePage<T>{

    public ReschedulePageAndroid(Session<T> session) {
        super(session);
    }
    
}
