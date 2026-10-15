package com.tiket.page.accommodation.homes.homesroomdetailpage;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class HomesRoomDetailPage<T extends WebDriver> extends BasePage<T> {

    public HomesRoomDetailPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> HomesRoomDetailPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> { return new HomesRoomDetailPageAndroid<>(session); }
            case IOS -> { return new HomesRoomDetailPageIOS<>(session); }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract boolean checkLoyaltyBenefit();
    public abstract void selectRoom();
    public abstract String getChildOccupancy(int childCount);
}