package com.tiket.SmartUI.page.platform;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class PlatformSmartPage<T extends WebDriver> extends BasePage<T> {
    public PlatformSmartPage(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> PlatformSmartPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new PlatformSmartPageAndroid<>(session);}
            case IOS -> {return new PlatformSmartPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }
    public abstract boolean verifyLocationBanner();

}
