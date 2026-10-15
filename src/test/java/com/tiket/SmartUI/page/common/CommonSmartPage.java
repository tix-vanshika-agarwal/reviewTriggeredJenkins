package com.tiket.SmartUI.page.common;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class CommonSmartPage<T extends WebDriver> extends BasePage<T> {
    public CommonSmartPage(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> CommonSmartPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new CommonSmartPageAndroid<>(session);}
            case IOS -> {return new CommonSmartPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }
    public abstract void openVoucherBox();
    public abstract void waitForHotelLoginBanner();
}
