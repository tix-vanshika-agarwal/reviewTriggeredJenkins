package com.tiket.page.accommodation.homes;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class HomesPageAndroid  <T extends WebDriver> extends HomesPage<T> {

    public HomesPageAndroid(Session<T> session) {
        super(session);
    }
}
