package com.tiket.web.page.accommodation.homes.LandingPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.web.page.accommodation.hotels.hotelLandingPage.HotelLandingPageWeb;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class HomesLandingPageDWeb<T extends WebDriver> extends HomesLandingPageWeb<T> {

    public HomesLandingPageDWeb(Session<T> session) {
        super(session);
    }

    private static final Locator DONE_SET_ROOM_GUEST = new Locator("DONE_SET_ROOM_GUEST", AppiumBy.xpath("//*[contains(@class,'footer_button')]//*[contains(@class,'Button_variant_primary')]"));

    @Override
    public boolean isSearchHistoryDestinationLabelDisplayed(String expected, int index) {
        return false;
    }

    @Override
    public boolean isSearchHistoryDateLabelDisplayed(String expected, int index) {
        return false;
    }

    @Override
    public boolean isSearchHistoryGuestLabelDisplayed(String expected, int index) {
        return false;
    }

}

