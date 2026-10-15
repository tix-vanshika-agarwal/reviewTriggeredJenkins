package com.tiket.page.accommodation.homes.homesdetailpage;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class HomesDetailPage<T extends WebDriver> extends BasePage<T> {

    public HomesDetailPage(Session<T> session) { super(session); }

    public static <T extends WebDriver> HomesDetailPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> { return new HomesDetailPageAndroid<>(session); }
            case IOS -> { return new HomesDetailPageIOS<>(session); }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract boolean checkLoyaltyBenefit();
    public abstract boolean isSimilarPlacesButtonShown();
    public abstract void clickSimilarPlacesButton();
    public abstract String getFirstSimilarPlaceName();
    public abstract String getFirstSimilarPlaceLocation();
    public abstract void selectFirstSimilarPlaceHotel();

    /**
     * Compares the location shown on the previous "Similar places" card with the location shown on the PDP.
     * Locations are matched on their main area token (case-insensitive), since the PDP usually shows a
     * more detailed address than the card (e.g. card "Semarang" vs PDP "Semarang, Central Java, Indonesia").
     */
    public boolean isSameLocation(String cardLocation, String pdpLocation) {
        logger.log("card location -> " + cardLocation + " | pdp location -> " + pdpLocation);
        if (cardLocation == null || pdpLocation == null
                || cardLocation.trim().isEmpty() || pdpLocation.trim().isEmpty()) {
            return false;
        }
        String cardMain = cardLocation.split(",")[0].trim().toLowerCase();
        String pdpMain = pdpLocation.split(",")[0].trim().toLowerCase();
        if (cardMain.isEmpty() || pdpMain.isEmpty()) {
            return false;
        }
        return pdpLocation.toLowerCase().contains(cardMain) || cardLocation.toLowerCase().contains(pdpMain);
    }
}