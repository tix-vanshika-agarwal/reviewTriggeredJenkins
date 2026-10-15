package com.tiket.page.accommodation.homes.homesroomlistpage;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class HomesRoomListPage<T extends WebDriver> extends BasePage<T> {

    public HomesRoomListPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> HomesRoomListPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> { return new HomesRoomListPageAndroid<>(session); }
            case IOS -> { return new HomesRoomListPageIOS<>(session); }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void selectRoomWithLoyaltyBenefit();
    public abstract void addChildGuest(int childCount);
    public abstract void openRoomWithChildOccupancy(int childCount);
}