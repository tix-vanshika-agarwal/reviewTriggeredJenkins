package com.tiket.page.train;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class TrainPageWeb <T extends WebDriver> extends TrainPage<T> {
    public TrainPageWeb(Session<T> session) {
        super(session);
    }
}
