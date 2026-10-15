package com.tiket.page.order.trip;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.TripModel;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hamcrest.core.Is.is;

public class TripPageAndroid <T extends WebDriver> extends TripPage<T> {
    public TripPageAndroid(Session<T> session) {
        super(session);
    }

    public static final Locator TRIP_HEADER = new Locator("TRIP_HEADER", AppiumBy.xpath("//*[contains(@resource-id,'container_trip_header')]"));
    public static final Locator TRIP_CARD = new Locator("TRIP_CARD", AppiumBy.xpath("//*[contains(@resource-id,'container_trip_product_card')]"));

    @Override
    public void skipCoachmark() {
        final var SKIP_POPUP_BUTTON = new Locator("SKIP_POPUP_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_link_coach_mark')]"));
        driverTools.waitForElementToAppear(SKIP_POPUP_BUTTON).ifPresent(driverTools::click);
    }

    @Override
    public boolean verifySeeAllOrdersButton() {
        final var SEE_ALL_ORDERS_BUTTON = new Locator("SEE_ALL_ORDERS_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'btn_see_all_orders')]"));
        final var ORDER_LIST_VIEW = new Locator("ORDER_LIST_VIEW", AppiumBy.xpath("//*[contains(@resource-id,'active_order_container')]"));
        driverTools.swipeToFullyVisibleElement(SEE_ALL_ORDERS_BUTTON, Swipe.UP, 10);
        driverTools.click(SEE_ALL_ORDERS_BUTTON);
        driverTools.waitForElementToAppear(ORDER_LIST_VIEW);
        boolean isOrderListVisible = driverTools.isElementDisplayed(ORDER_LIST_VIEW);
        openTripViewSection();
        return isOrderListVisible;
    }

    @Override
    public String openFirstOrder() {
        var orderDetailPage = OrderDetailPage.getInstance(session);
        WebElement firstCard = driverTools.waitForElementToAppear(TRIP_CARD).orElseThrow();
        driverTools.click(firstCard);
        driverTools.waitForElementToDisappear(TRIP_CARD);
        orderDetailPage.hidePromoBannerIfPresent();
        var orderID = orderDetailPage.getOrderID();
        orderDetailPage.clickBack();
        return orderID;
    }

    @Override
    public String openFirstETiket() {
        final var viewETiketButton = new Locator("VIEW_ETIKET_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'container_trip_product_card')]//*[contains(@resource-id,'tds_button_view') and (contains(@text,'View e-ticket') or contains(@text,'Lihat e-tiket'))]"));
        WebElement viewETiketButtonElement = driverTools.waitForElementToAppear(viewETiketButton).orElseThrow();
        driverTools.click(viewETiketButtonElement);
        driverTools.waitForElementToDisappear(TRIP_CARD);

        // Get order ID from e-ticket page of TTD which is coming first in trip view testdata setup.
        return getEtiketIdFromTtdDetailPage();
    }

    @Override
    public boolean verifyTripsCanBeExpandedAndCollapsed() {
        if(driverTools.waitForElementToAppear(TRIP_CARD, 2).isEmpty()) { // if trip card is not visible, click to expand
            driverTools.click(TRIP_HEADER);
        }
        boolean isTripCardVisible = driverTools.waitForElementToAppear(TRIP_CARD).isPresent();
        driverTools.click(TRIP_HEADER);
        boolean isTripCardDisappeared = driverTools.waitForElementToDisappear(TRIP_CARD);
        return isTripCardVisible && isTripCardDisappeared;
    }

    @Override
    public void gotoTrip(TripModel.Trip trip) {
        final var tripTitle = new Locator("TRIP_TITLE", AppiumBy.xpath(String.format("//*[contains(@resource-id,'tv_trip_title') and @text='%s']", trip.title())));
        final var tripSubTitle = new Locator("TRIP_SUBTITLE", AppiumBy.xpath(String.format("//*[contains(@resource-id,'tv_trip_subtitle') and @text='%s']", trip.subTitle())));
        driverTools.swipeToFullyVisibleElement(tripTitle, Swipe.UP, 10);
        driverTools.swipeToFullyVisibleElement(tripSubTitle, Swipe.UP, 10);
        Assertion.assertThat("Verify trip title is visible", driverTools.isElementDisplayed(tripTitle), is(true));
        Assertion.assertThat("Verify trip subtitle is visible", driverTools.isElementDisplayed(tripSubTitle), is(true));
    }

    @Override
    public void openTrip(TripModel.Trip trip) {
        gotoTrip(trip);
        if(driverTools.waitForElementToAppear(TRIP_CARD, 2).isPresent()) { // if trip card is visible, click to collapse first
            driverTools.click(TRIP_HEADER);
        }
        clickTripSubtitle(trip.subTitle());
        driverTools.waitForElementToAppear(TRIP_CARD).orElseThrow();
    }

    @Override
    public void gotoCard(String title) {
        final var cardTitle = new Locator("CARD_TITLE", AppiumBy.xpath(String.format("//*[contains(@resource-id,'tv_trip_product_title') and @text='%s']", title)));
        driverTools.swipeToFullyVisibleElement(cardTitle, Swipe.UP, 10);
        driverTools.swipeElementToMidScreen(cardTitle);
        Assertion.assertThat("Verify card title is visible", driverTools.isElementDisplayed(cardTitle), is(true));
    }

    @Override
    public boolean verifyTTDCardDetails(TripModel.TTDCard card) {
        gotoCard(card.name());
        final var cardTitle = new Locator("CARD_TITLE", AppiumBy.xpath(String.format("//*[contains(@resource-id,'tv_trip_product_title') and @text='%s']", card.name())));
        final var cardDate = new Locator("CARD_DATE", AppiumBy.xpath(String.format("//*[contains(@resource-id,'tv_trip_product_subtitle') and @text='%s']", card.date())));
        final var cardGuestCount = new Locator("CARD_GUEST_COUNT", AppiumBy.xpath(String.format("//*[contains(@resource-id,'tv_trip_product_user') and @text='%s']", card.guestCount())));
        Assertion.assertThat("TTD card title is visible", driverTools.isElementDisplayed(cardTitle), is(true));
        Assertion.assertThat("TTD card date is visible", driverTools.isElementDisplayed(cardDate), is(true));
        Assertion.assertThat("TTD card guest count is visible", driverTools.isElementDisplayed(cardGuestCount), is(true));
        return true;
    }

    @Override
    public boolean verifyHotelCardDetails(TripModel.HotelCard card) {
        gotoCard(card.hotelName());
        final var cardTitle = new Locator("CARD_TITLE", AppiumBy.xpath(String.format("//*[contains(@resource-id,'tv_trip_product_title') and @text='%s']", card.hotelName())));
        final var cardDuration = new Locator("CARD_DURATION", AppiumBy.xpath(String.format("//*[contains(@resource-id,'tv_trip_product_subtitle') and @text='%s']", card.duration())));
        final var cardGuestName = new Locator("CARD_GUEST_NAME", AppiumBy.xpath(String.format("//*[contains(@resource-id,'tv_trip_product_user') and @text='%s']", card.guestName())));
        Assertion.assertThat("Hotel card title is visible", driverTools.isElementDisplayed(cardTitle), is(true));
        Assertion.assertThat("Hotel card duration is visible", driverTools.isElementDisplayed(cardDuration), is(true));
        Assertion.assertThat("Hotel card guest name is visible", driverTools.isElementDisplayed(cardGuestName), is(true));
        return true;
    }

    @Override
    public boolean verifyTrainCardDetails(TripModel.TrainCard card) {
        gotoCard(card.vendorName());
        final var cardTitle = new Locator("CARD_TITLE", AppiumBy.xpath(String.format("//*[contains(@resource-id,'tv_trip_product_title') and @text='%s']", card.vendorName())));
        final var cardOrigin = new Locator("CARD_ORIGIN", AppiumBy.xpath(String.format("//*[contains(@resource-id,'tv_trip_route_departure_detail') and @text='%s']", card.origin())));
        final var cardDestination = new Locator("CARD_DESTINATION", AppiumBy.xpath(String.format("//*[contains(@resource-id,'tv_trip_route_arrival_detail') and @text='%s']", card.destination())));
        final var startTime = new Locator("START_TIME", AppiumBy.xpath(String.format("//*[contains(@resource-id,'tv_trip_route_departure_time') and @text='%s']", card.startTime())));
        final var endTime = new Locator("END_TIME", AppiumBy.xpath(String.format("//*[contains(@resource-id,'tv_trip_route_arrival_time') and @text='%s']", card.endTime())));
        final var paxName = new Locator("PAX_NAME", AppiumBy.xpath(String.format("//*[contains(@resource-id,'tv_trip_product_user') and @text='%s']", card.paxName())));
        Assertion.assertThat("Train card title is visible", driverTools.isElementDisplayed(cardTitle), is(true));
        Assertion.assertThat("Train card origin is visible", driverTools.isElementDisplayed(cardOrigin), is(true));
        Assertion.assertThat("Train card destination is visible", driverTools.isElementDisplayed(cardDestination), is(true));
        Assertion.assertThat("Train card start time is visible", driverTools.isElementDisplayed(startTime), is(true));
        Assertion.assertThat("Train card end time is visible", driverTools.isElementDisplayed(endTime), is(true));
        Assertion.assertThat("Train card pax name is visible", driverTools.isElementDisplayed(paxName), is(true));
        return true;
    }

    @Override
    public void gotoLowProfileCrossSell(String title) {
        final var lowProfileCrossSellCard = new Locator("LOW_PROFILE_CROSS_SELL_CARD", AppiumBy.xpath(String.format("//*[contains(@resource-id,'tv_trip_banner_title') and @text='%s']", title)));
        driverTools.swipeToFullyVisibleElement(lowProfileCrossSellCard, Swipe.UP, 10);
        driverTools.swipeElementToMidScreen(lowProfileCrossSellCard);
        Assertion.assertThat("Verify low profile cross sell card is visible", driverTools.isElementDisplayed(lowProfileCrossSellCard), is(true));
    }

    @Override
    public boolean verifyLowProfileCrossSell() {
        final var actionButton = new Locator("ACTION_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'container_trip_banner_offer')]//*[contains(@resource-id,'tds_button_view')]"));
        driverTools.click(actionButton);
        // Car tranfer is coming for setup testdata.
        handleCarTransferPage();
        return true;
    }

    @Override
    public boolean gotoHighProfileCrossSell() {
        final var highProfileCrossSellCardTitle = new Locator("HIGH_PROFILE_CROSS_SELL_CARD_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_title')]"));
        driverTools.swipeToFullyVisibleElement(highProfileCrossSellCardTitle, Swipe.UP, 10);
        driverTools.swipeElementToMidScreen(highProfileCrossSellCardTitle);
        Assertion.assertThat("Verify high profile cross sell card is visible", driverTools.isElementDisplayed(highProfileCrossSellCardTitle), is(true));
        return true;
    }

    @Override
    public boolean verifyHighProfileCrossSell() {
        final var gridImage = new Locator("GRID_IMAGE", AppiumBy.xpath("//*[contains(@resource-id,'iv_banner')]"));
        driverTools.click(gridImage);
        // Car tranfer is coming for setup testdata.
        handleCarTransferPage();
        return true;
    }

    @Override
    public void clickTripSubtitle(String subtitle) {
        final var tripSubTitle = new Locator("TRIP_SUBTITLE", AppiumBy.xpath(String.format("//*[contains(@resource-id,'tv_trip_subtitle') and @text='%s']", subtitle)));
        driverTools.click(tripSubTitle);
    }

    @Override
    protected String getTripHeaderXpath(String location, String shortMonth) {
        return "//*[contains(@resource-id,'tv_trip_title') and contains(" + CoreFunctions.lowerCaseOf("@text") + "," + CoreFunctions.lowerCaseLiteral(location) + ")]"
                + "/following-sibling::*[contains(@resource-id,'tv_trip_subtitle') and contains(" + CoreFunctions.lowerCaseOf("@text") + "," + CoreFunctions.lowerCaseLiteral(shortMonth) + ")]";
    }

    private String getTripProductDateXpath(String day, String shortMonth) {
        return "//*[contains(@resource-id,'tv_trip_product_day') and @text='" + day + "']"
                + "/following-sibling::*[contains(@resource-id,'tv_trip_product_month') and " + CoreFunctions.lowerCaseOf("@text") + "=" + CoreFunctions.lowerCaseLiteral(shortMonth) + "]";
    }

    private String getTripProductTitleXpath(String productName) {
        return "//*[contains(@resource-id,'tv_trip_product_title') and " + CoreFunctions.lowerCaseOf("@text") + "=" + CoreFunctions.lowerCaseLiteral(productName) + "]";
    }

    @Override
    protected Locator getTripCardLocator(String day, String shortMonth, String productName) {
        return new Locator("TRIP_CARD", AppiumBy.xpath(getTripProductDateXpath(day, shortMonth)
                + "/following-sibling::*[contains(@resource-id,'container_trip_product_card')]"
                + getTripProductTitleXpath(productName)));
    }

    @Override
    protected Locator getTripCardAlternativeLocator(String day, String shortMonth, String productName) {
        return new Locator("TRIP_CARD_ALTERNATIVE", AppiumBy.xpath(getTripProductDateXpath(day, shortMonth)
                + "/parent::*/following-sibling::*[contains(@class,'android.view.ViewGroup') and count(*) = 2]"
                + getTripProductTitleXpath(productName)));
    }
}
