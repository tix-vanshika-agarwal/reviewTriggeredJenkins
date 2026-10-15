package com.tiket.page.accommodation.homes.homesroomdetailpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomesRoomDetailPageIOS<T extends WebDriver> extends HomesRoomDetailPage<T> {

    public HomesRoomDetailPageIOS(Session<T> session) {
        super(session);
    }

    private static final Locator LOYALTY_BENEFIT = new Locator("LOYALTY_BENEFIT", AppiumBy.xpath("//*[contains(@label,'benefit') or contains(@label,'Benefit')]//preceding-sibling::XCUIElementTypeOther"));
    private static final Locator SELECT_ROOM_BUTTON = new Locator("SELECT_ROOM_BUTTON", AppiumBy.iOSClassChain("**/*[`name == 'hotelRoomDetailSelectRoomButton' OR label == 'Book' OR label == 'Pesan'`]"));

    @Override
    public boolean checkLoyaltyBenefit() {
        WebElement loyaltyBenefit = driverTools.swipeToElement(LOYALTY_BENEFIT, Swipe.UP, 3).orElseThrow();
        boolean status = driverTools.waitForElementToAppear(loyaltyBenefit).isPresent();
        logger.log("loyaltyBenefit -> " + status);
        return status;
    }

    @Override
    public void selectRoom() {
        WebElement selectRoomBtn = driverTools.waitForElementToAppear(SELECT_ROOM_BUTTON).orElseThrow();
        driverTools.click(selectRoomBtn);
    }

    @Override
    public String getChildOccupancy(int childCount) {
        throw new org.apache.commons.lang3.NotImplementedException("Not implemented");
    }
}
