package com.tiket.page.common.phone.settings;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.WebDriver;

public abstract class DefaultAppsPage  <T extends WebDriver> extends BasePage<T> {

    public DefaultAppsPage(Session<T> session) {
        super(session);
    }

    public static  <T extends WebDriver> DefaultAppsPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new DefaultAppsPageAndroid<>(session);}
            case IOS -> {return new DefaultAppsPageIOS<>(session);}
            default -> throw new IllegalStateException("Unexpected value: " + Configs.PLATFORM);
        }
    }

    public abstract void clearBrowserDefaultPreferenceOnLambdaTest();
}
