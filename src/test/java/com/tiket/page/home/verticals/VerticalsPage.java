package com.tiket.page.home.verticals;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class VerticalsPage  <T extends WebDriver> extends BasePage<T> {
    public VerticalsPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> VerticalsPage<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new VerticalsPageAndroid<>(session);
            }
            case IOS -> {
                return new VerticalsPageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void openVerticalContainingName(String verticalName);
}
