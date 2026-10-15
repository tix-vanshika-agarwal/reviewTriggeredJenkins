package com.tiket.page.train;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class TrainPageAndroid<T extends WebDriver> extends TrainPage<T> {

    public TrainPageAndroid(Session<T> session) {
        super(session);
    }
}
