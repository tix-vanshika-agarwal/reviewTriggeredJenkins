package com.tiket.page.order.trip.switcher;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class SwitcherPage <T extends WebDriver> extends BasePage<T> {
    public SwitcherPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> SwitcherPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new SwitcherPageAndroid<>(session);
            }
            case IOS -> {
                return new SwitcherPageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract boolean switchToTrip();
    public abstract boolean switchToOrder();
}
