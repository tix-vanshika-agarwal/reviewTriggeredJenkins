package com.tiket.SmartUI.page.b2b;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class B2BSmartPage<T extends WebDriver> extends BasePage<T> {
    public B2BSmartPage(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> B2BSmartPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new B2BSmartPageAndroid<>(session);}
            case IOS -> {return new B2BSmartPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }
    public abstract void openTravelPlan();
    public abstract void chooseTravelPlan();
    public abstract void waitForTravelPlanToBeOpened();
    public abstract void continueToNextPage();
}
