package com.tiket.web;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.WebDriver;

public class WebPage <T extends WebDriver> extends BasePage<T> {

    public WebPage(Session<T> session) {
        super(session);
    }


}
