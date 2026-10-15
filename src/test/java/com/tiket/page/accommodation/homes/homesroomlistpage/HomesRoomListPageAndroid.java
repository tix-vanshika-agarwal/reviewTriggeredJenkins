package com.tiket.page.accommodation.homes.homesroomlistpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.roomlistpage.RoomListPage;
import com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public class HomesRoomListPageAndroid<T extends WebDriver> extends HomesRoomListPage<T> {

    public HomesRoomListPageAndroid(Session<T> session) { super(session); }

    private static final Locator LOYALTY_BADGE = new Locator("LOYALTY_BADGE", AppiumBy.id("tv_room_loyalty"));

    @Override
    public void selectRoomWithLoyaltyBenefit() {
        driverTools.swipeToElement(LOYALTY_BADGE, Swipe.UP, 3);
        driverTools.click(LOYALTY_BADGE);
    }

    @Override
    public void addChildGuest(int childCount) {
        RoomListPage<T> roomListPage = RoomListPage.getInstance(session);
        roomListPage.clickChangeButton();

        LandingPage<T> landingPage = LandingPage.getInstance(session);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalChildren(childCount);
        landingPage.clickDone();

        SearchResultPage<T> searchResultPage = SearchResultPage.getInstance(session);
        searchResultPage.clickSubmit();
    }

    @Override
    public void openRoomWithChildOccupancy(int childCount) {
        WebElement childOccupancy = driverTools.swipeToElement(getRoomListChildOccupancyLocator(childCount), Swipe.UP, 15).orElseThrow();

        Optional<WebElement> chevron = driverTools.swipeToElement(getRoomDetailChevronByChildOccupancy(childCount), Swipe.UP, 3);
        if (chevron.isPresent()) {
            driverTools.click(chevron.get());
        } else {
            // Fallback: open the detail of the located room by clicking its card (not the first room on the list)
            WebElement roomCard = driverTools.waitForElementToAppear(getRoomCardByChildOccupancy(childCount)).orElse(childOccupancy);
            driverTools.click(roomCard);
        }
    }

    private Locator getRoomListChildOccupancyLocator(int childCount) {
        return new Locator("ROOM_LIST_CHILD_OCCUPANCY", AppiumBy.xpath("//*[contains(@resource-id,'tv_child_occupancy') and (contains(concat(' ', @text), ' " + childCount + " Child') or contains(concat(' ', @text), ' " + childCount + " Anak'))]"));
    }

    private Locator getRoomDetailChevronByChildOccupancy(int childCount) {
        return new Locator("ROOM_DETAIL_CHEVRON_BY_CHILD", AppiumBy.xpath("//*[contains(@resource-id,'tv_child_occupancy') and (contains(concat(' ', @text), ' " + childCount + " Child') or contains(concat(' ', @text), ' " + childCount + " Anak'))]//ancestor::*[contains(@resource-id,'card_room')]//*[contains(@resource-id,'iv_room_detail_chevron')]"));
    }

    private Locator getRoomCardByChildOccupancy(int childCount) {
        return new Locator("ROOM_CARD_BY_CHILD", AppiumBy.xpath("//*[contains(@resource-id,'tv_child_occupancy') and (contains(concat(' ', @text), ' " + childCount + " Child') or contains(concat(' ', @text), ' " + childCount + " Anak'))]//ancestor::*[contains(@resource-id,'card_room')]"));
    }
}
