package com.tiket.page.accommodation.homes.homesdetailpage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class HomesDetailPageIOS<T extends WebDriver> extends HomesDetailPage<T> {

    public HomesDetailPageIOS(Session<T> session) { super(session); }

    private static final Locator LOYALTY_BENEFIT = new Locator("LOYALTY_BENEFIT", AppiumBy.iOSClassChain("**/*[`name CONTAINS 'LoyaltyBenefitView'`]"));

    @Override
    public boolean checkLoyaltyBenefit() {
        return driverTools.waitForElementToAppear(LOYALTY_BENEFIT).isPresent();
    }

    @Override
    public boolean isSimilarPlacesButtonShown() {
        throw new org.apache.commons.lang3.NotImplementedException("Not implemented");
    }

    @Override
    public void clickSimilarPlacesButton() {
        throw new org.apache.commons.lang3.NotImplementedException("Not implemented");
    }

    @Override
    public String getFirstSimilarPlaceName() {
        throw new org.apache.commons.lang3.NotImplementedException("Not implemented");
    }

    @Override
    public String getFirstSimilarPlaceLocation() {
        throw new org.apache.commons.lang3.NotImplementedException("Not implemented");
    }

    @Override
    public void selectFirstSimilarPlaceHotel() {
        throw new org.apache.commons.lang3.NotImplementedException("Not implemented");
    }
}
