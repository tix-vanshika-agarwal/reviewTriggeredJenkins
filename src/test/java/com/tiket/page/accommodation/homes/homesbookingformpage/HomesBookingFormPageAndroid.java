package com.tiket.page.accommodation.homes.homesbookingformpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class HomesBookingFormPageAndroid<T extends WebDriver> extends HomesBookingFormPage<T> {

    public HomesBookingFormPageAndroid(Session<T> session) { super(session); }

    public static final Locator LOYALTY_BENEFIT = new Locator("LOYALTY_BENEFIT", AppiumBy.id("tv_loyalty_info"));

    @Override
    public boolean checkLoyaltyBenefit() {
        driverTools.swipeToElement(LOYALTY_BENEFIT, Swipe.UP, 5);
        boolean loyaltyBenefit = driverTools.isElementDisplayed(LOYALTY_BENEFIT);
        return loyaltyBenefit;
    }
}