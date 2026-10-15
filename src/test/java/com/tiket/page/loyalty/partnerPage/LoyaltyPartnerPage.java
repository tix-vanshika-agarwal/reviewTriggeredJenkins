package com.tiket.page.loyalty.partnerPage;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class LoyaltyPartnerPage<T extends WebDriver> extends BasePage<T> {
    public LoyaltyPartnerPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> LoyaltyPartnerPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new LoyaltyPartnerPageAndroid<>(session);}
            case IOS -> {return new LoyaltyPartnerPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }
    public abstract Boolean isLoyaltyPartnerPageOpen();
}