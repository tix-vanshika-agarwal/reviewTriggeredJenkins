package com.tiket.page.accommodation.homes.homesroomlistpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Session;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.tiket.page.accommodation.hotel.roomlistpage.RoomListPageIOS.LOYALTY_BADGE;

public class HomesRoomListPageIOS<T extends WebDriver> extends HomesRoomListPage<T> {

    public HomesRoomListPageIOS(Session<T> session) { super(session); }

    @Override
    public void selectRoomWithLoyaltyBenefit() {
        WebElement loyaltyBadge = driverTools.swipeToElement(LOYALTY_BADGE, Swipe.UP, 10).orElseThrow();
        driverTools.click(loyaltyBadge);
    }

    @Override
    public void addChildGuest(int childCount) {
        throw new NotImplementedException("Not implemented");
    }

    @Override
    public void openRoomWithChildOccupancy(int childCount) {
        throw new NotImplementedException("Not implemented");
    }
}
