package com.tiket.page.accommodation.homes.homessearchresultpage;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class HomesSearchResultPage<T extends WebDriver> extends BasePage<T> {

    public HomesSearchResultPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> HomesSearchResultPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new HomesSearchResultPageAndroid<>(session);
            }
            case IOS -> {
                return new HomesSearchResultPageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract boolean verifySrpElements();
    public abstract void clickBedroomsFilterButton();
    public abstract void addTotalBedroom(String totalBedroom);
    public abstract void selectAccomodationTypeFilter(String accomadationType);
    public abstract void setBedroomsFilterHomes( String totalBedRoom);
    public abstract void selectHomesWithLoyaltyBenefit();
    public abstract boolean isFlexibleStayDatesSectionShown();
    public abstract void swipeToFlexibleStayDates();
    public abstract String getFirstFlexibleStayHotelName();
    public abstract String getFirstFlexibleStayLocation();
    public abstract void selectFirstFlexibleStayCard();
}