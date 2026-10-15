package com.tiket.page.accommodation.homes.homesroomdetailpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomesRoomDetailPageAndroid<T extends WebDriver> extends HomesRoomDetailPage<T> {

    public HomesRoomDetailPageAndroid(Session<T> session) {
        super(session);
    }

    public static final Locator LOYALTY_BENEFIT = new Locator("LOYALTY_BENEFIT", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Tier') or contains(@text,'tier'))]"));
    public static final Locator SELECT_ROOM_BUTTON = new Locator("SELECT_ROOM_BUTTON", AppiumBy.id("tds_button_view"));

    @Override
    public boolean checkLoyaltyBenefit() {
        boolean loyaltyBenefit = driverTools.swipeToElement(LOYALTY_BENEFIT, Swipe.UP, 3).isPresent();
        return loyaltyBenefit;
    }

    @Override
    public void selectRoom() {
        WebElement selectRoomBtn = driverTools.waitForElementToAppear(SELECT_ROOM_BUTTON).orElseThrow();
        driverTools.click(selectRoomBtn);
    }

    @Override
    public String getChildOccupancy(int childCount) {
        WebElement childOccupancy = driverTools.swipeToElement(getChildOccupancyLocator(childCount), Swipe.UP, 8).orElseThrow();
        String childOccupancyText = driverTools.getText(childOccupancy);
        logger.log("child occupancy on room detail -> " + childOccupancyText);
        return childOccupancyText;
    }

    private Locator getChildOccupancyLocator(int childCount) {
        return new Locator("CHILD_OCCUPANCY", AppiumBy.xpath("//*[contains(@resource-id,'rv_item')]//*[contains(@resource-id,'tv_child_occupancy') and (contains(concat(' ', @text), ' " + childCount + " Child') or contains(concat(' ', @text), ' " + childCount + " Anak'))]"));
    }
}