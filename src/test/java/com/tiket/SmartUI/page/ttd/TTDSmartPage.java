package com.tiket.SmartUI.page.ttd;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class TTDSmartPage<T extends WebDriver> extends BasePage<T> {
    public TTDSmartPage(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> TTDSmartPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new TTDSmartPageAndroid<>(session);}
            case IOS -> {return new TTDSmartPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }
    public abstract void scrollToProductCardLandingPage();
    public abstract void clickSearchBar();
}
