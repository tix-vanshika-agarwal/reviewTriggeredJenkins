package com.tiket.page.ttd.productdetailpage;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class TTDProductDetailPage <T extends WebDriver> extends BasePage<T> {

    public TTDProductDetailPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TTDProductDetailPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new TTDProductDetailPageAndroid<>(session);}
            case IOS -> {return new TTDProductDetailPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract String getPartnerName();
    public abstract String getProductLocation(String location);
    public abstract boolean isPopularPartnerPageValid(String name);
    public abstract boolean checkProductNameNative(String productName);
    public abstract void swipeUntilTabVisible();
    public abstract void clickViewMap();
}