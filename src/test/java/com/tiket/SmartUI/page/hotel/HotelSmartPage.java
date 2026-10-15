package com.tiket.SmartUI.page.hotel;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.smart.Hotel.HotelSmartPageWeb;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class HotelSmartPage<T extends WebDriver> extends BasePage<T> {
    public HotelSmartPage(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> HotelSmartPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new HotelSmartPageAndroid<>(session);}
            case IOS -> {return new HotelSmartPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }
    public abstract void clickGradientBanner();
    public void waitForWebviewToBeLoaded(){
        HotelSmartPageWeb<?> hotelSmartPageWeb = HotelSmartPageWeb.getInstance(session);
        hotelSmartPageWeb.waitForWebviewToBeLoaded();
    }
    public void goBackToLandingPage(){
        HotelSmartPageWeb<?> hotelSmartPageWeb = HotelSmartPageWeb.getInstance(session);
        hotelSmartPageWeb.goBackToLandingPage();
    }

    public abstract void setDate(String type, String page);

    public abstract void closeCalendar();

    public abstract void openSRPSearchForm();

    public abstract void closeSearchBottomsheet();

}
