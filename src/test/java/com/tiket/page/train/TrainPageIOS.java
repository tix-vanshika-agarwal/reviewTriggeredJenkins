package com.tiket.page.train;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class TrainPageIOS<T extends WebDriver> extends TrainPage<T> {

    public TrainPageIOS(Session<T> session) {
        super(session);
    }
}
