package com.tiket.page.accommodation.homes.homesbookingformpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class HomesBookingFormPageIOS<T extends WebDriver> extends HomesBookingFormPage<T> {

    public HomesBookingFormPageIOS(Session<T> session) { super(session); }

    private static final Locator LOYALTY_BENEFIT = new Locator("LOYALTY_BENEFIT", AppiumBy.id("TVCell<TACMMultiBFBenefitBannerView>"));

    @Override
    public boolean checkLoyaltyBenefit() {
        driverTools.swipeToElement(LOYALTY_BENEFIT, Swipe.UP, 5);
        return driverTools.isElementDisplayed(LOYALTY_BENEFIT);
    }
}
