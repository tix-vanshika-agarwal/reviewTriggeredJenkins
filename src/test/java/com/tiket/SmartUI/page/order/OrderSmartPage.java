package com.tiket.SmartUI.page.order;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class OrderSmartPage<T extends WebDriver> extends BasePage<T> {
    public OrderSmartPage(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> OrderSmartPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new OrderSmartPageAndroid<>(session);}
            case IOS -> {return new OrderSmartPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }
    public abstract void waitForFilterToBeApplied();
}
