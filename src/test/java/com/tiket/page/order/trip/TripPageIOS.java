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

public class TripPageIOS  <T extends WebDriver> extends TripPage<T> {
    public TripPageIOS(Session<T> session) {
        super(session);
    }

    public static final Locator TRIP_HEADER = new Locator("TRIP_HEADER", AppiumBy.name("trip_header"));
    public static final Locator TRIP_CARD = new Locator("TRIP_CARD", AppiumBy.name("trip_order_card"));

    @Override
    public void skipCoachmark() {
        final var SKIP_POPUP_BUTTON = new Locator("SKIP_POPUP_BUTTON", AppiumBy.name("coachmark_skip"));
        driverTools.waitForElementToAppear(SKIP_POPUP_BUTTON).ifPresent(driverTools::click);
    }

    @Override
    public boolean verifySeeAllOrdersButton() {
        final var SEE_ALL_ORDERS_BUTTON = new Locator("SEE_ALL_ORDERS_BUTTON", AppiumBy.name("trip_button_view_all_orders"));
        final var ORDER_LIST_VIEW = new Locator("ORDER_LIST_VIEW", AppiumBy.name("order_card"));
        driverTools.swipeToFullyVisibleElement(SEE_ALL_ORDERS_BUTTON, Swipe.UP, 10);
        driverTools.click(SEE_ALL_ORDERS_BUTTON);
        driverTools.waitForElementToAppear(ORDER_LIST_VIEW);
        boolean isOrderListVisible = driverTools.isElementVisible(ORDER_LIST_VIEW);
        openTripViewSection();
        return isOrderListVisible;
    }

    @Override
    public String openFirstOrder() {
        // first order is TTD which is coming first in trip view testdata setup.
        final var backButton = new Locator("BACK_BUTTON", AppiumBy.name("back"));
        final var orderDetailPage = OrderDetailPage.getInstance(session);
        WebElement firstCard = driverTools.waitForElementToAppear(TRIP_CARD).orElseThrow();
        driverTools.click(firstCard);
        driverTools.waitForElementToDisappear(TRIP_CARD);
        orderDetailPage.hidePromoBannerIfPresent();
        final var orderID = orderDetailPage.getOrderID();
        driverTools.click(backButton);
        return orderID;
    }

    @Override
    public String openFirstETiket() {
        final var viewETiketButton = new Locator("VIEW_ETIKET_BUTTON", AppiumBy.xpath("//*[contains(@name,'trip_order_primary_action') and @visible='true']//*[contains(@name,'View e-ticket') or contains(@name,'Lihat e-tiket')]"));
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
        final var tripTitle = new Locator("TRIP_TITLE", AppiumBy.xpath(String.format("//*[@name='trip_header_title' and @label='%s']", trip.title())));
        final var tripSubTitle = new Locator("TRIP_SUBTITLE", AppiumBy.xpath(String.format("//*[@name='trip_header_date' and @label='%s']", trip.subTitle())));
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
        final var cardTitle = new Locator("CARD_TITLE", AppiumBy.xpath(String.format("//*[@name='trip_order_title' and @value='%s']", title)));
        driverTools.swipeToFullyVisibleElement(cardTitle, Swipe.UP, 10);
        driverTools.swipeElementToMidScreen(cardTitle);
        Assertion.assertThat("Verify card title is visible", driverTools.isElementDisplayed(cardTitle), is(true));

    }

    @Override
    public boolean verifyTTDCardDetails(TripModel.TTDCard card) {
        gotoCard(card.name());
        final var cardTitle = new Locator("CARD_TITLE", AppiumBy.xpath(String.format("//*[@name='trip_order_title' and @value='%s']", card.name())));
        final var cardDate = new Locator("CARD_DATE", AppiumBy.xpath(String.format("//*[contains(@name,'trip_order_subtitle') and @value='%s']", card.date())));
        final var cardGuestCount = new Locator("CARD_GUEST_COUNT", AppiumBy.xpath(String.format("//*[@name='trip_order_title' and @value='%s']/../../following-sibling::*/XCUIElementTypeStaticText[@value='%s']", card.name(), card.guestCount())));
        Assertion.assertThat("Verify TTD card title is visible", driverTools.isElementDisplayed(cardTitle), is(true));
        Assertion.assertThat("Verify TTD card date is visible", driverTools.isElementDisplayed(cardDate), is(true));
        Assertion.assertThat("Verify TTD card guest count is visible", driverTools.isElementDisplayed(cardGuestCount), is(true));
        return true;
    }

    @Override
    public boolean verifyHotelCardDetails(TripModel.HotelCard card) {
        gotoCard(card.hotelName());
        final var cardHotelName = new Locator("CARD_TITLE", AppiumBy.xpath(String.format("//*[@name='trip_order_title' and @value='%s']", card.hotelName())));
        final var cardDuration = new Locator("CARD_DURATION", AppiumBy.xpath(String.format("//*[contains(@name,'trip_order_subtitle') and @value='%s']", card.duration())));
        final var cardGuestName = new Locator("CARD_GUEST_NAME", AppiumBy.xpath(String.format("//*[@name='trip_order_title' and @value='%s']/../../following-sibling::*/XCUIElementTypeStaticText[@value='%s']", card.hotelName(), card.guestName())));
        Assertion.assertThat("Verify Hotel card title is visible", driverTools.isElementDisplayed(cardHotelName), is(true));
        Assertion.assertThat("Verify Hotel card duration is visible", driverTools.isElementDisplayed(cardDuration), is(true));
        Assertion.assertThat("Verify Hotel card guest name is visible", driverTools.isElementDisplayed(cardGuestName), is(true));
        return true;
    }

    @Override
    public boolean verifyTrainCardDetails(TripModel.TrainCard card) {
        gotoCard(card.vendorName());
        final var cardTitle = new Locator("CARD_TITLE", AppiumBy.xpath(String.format("//*[@name='trip_order_title' and @value='%s']", card.vendorName())));
        final var cardDate = new Locator("CARD_DATE", AppiumBy.xpath(String.format("//*[@name='trip_order_card']//*[contains(@name,'%s')]", card.origin())));
        final var cardGuestCount = new Locator("CARD_GUEST_COUNT", AppiumBy.xpath(String.format("//*[@name='trip_order_card']//*[contains(@name,'%s')]", card.destination())));
        final var cardStartTime = new Locator("CARD_START_TIME", AppiumBy.xpath(String.format("//*[@name='trip_order_card']//*[contains(@name,'%s')]", card.startTime())));
        final var cardEndTime = new Locator("CARD_END_TIME", AppiumBy.xpath(String.format("//*[@name='trip_order_card']//*[contains(@name,'%s')]", card.endTime())));
        Assertion.assertThat("Verify Train card title is visible", driverTools.isElementDisplayed(cardTitle), is(true));
        Assertion.assertThat("Verify Train card origin is visible", driverTools.isElementDisplayed(cardDate), is(true));
        Assertion.assertThat("Verify Train card destination is visible", driverTools.isElementDisplayed(cardGuestCount), is(true));
        Assertion.assertThat("Verify Train card start time is visible", driverTools.isElementDisplayed(cardStartTime), is(true));
        Assertion.assertThat("Verify Train card end time is visible", driverTools.isElementDisplayed(cardEndTime), is(true));
        return true;

    }

    @Override
    public void gotoLowProfileCrossSell(String title) {
        final var lowProfileCrossSellCard = new Locator("LOW_PROFILE_CROSS_SELL_CARD", AppiumBy.xpath(String.format("//*[@name='trip_banner']//*[@value='%s']", title)));
        driverTools.swipeToFullyVisibleElement(lowProfileCrossSellCard, Swipe.UP, 10);
        driverTools.swipeElementToMidScreen(lowProfileCrossSellCard);
        Assertion.assertThat("Verify low profile cross sell card is visible", driverTools.isElementDisplayed(lowProfileCrossSellCard), is(true));

    }

    @Override
    public boolean verifyLowProfileCrossSell() {
        final var actionButton = new Locator("ACTION_BUTTON", AppiumBy.xpath("//*[@name='trip_banner']//*[@name='trip_banner_action']"));
        driverTools.click(actionButton);
        // Car tranfer is coming for setup testdata.
        handleCarTransferPage();
        return true;
    }

    @Override
    public boolean gotoHighProfileCrossSell() {
        final var highProfileCrossSellCardTitle = new Locator("HIGH_PROFILE_CROSS_SELL_CARD_TITLE", AppiumBy.xpath("//*[@name='page_module_title']"));
        driverTools.swipeToFullyVisibleElement(highProfileCrossSellCardTitle, Swipe.UP, 10);
        driverTools.swipeElementToMidScreen(highProfileCrossSellCardTitle);
        Assertion.assertThat("Verify high profile cross sell card is visible", driverTools.isElementDisplayed(highProfileCrossSellCardTitle), is(true));
        return true;
    }

    @Override
    public boolean verifyHighProfileCrossSell() {
        final var gridImage = new Locator("GRID_IMAGE", AppiumBy.xpath("//*[@name='bn_grid']"));
        driverTools.click(gridImage);
        // Car tranfer is coming for setup testdata.
        handleCarTransferPage();
        return true;
    }

    @Override
    public void clickTripSubtitle(String subtitle) {
        final var tripSubTitle = new Locator("TRIP_SUBTITLE", AppiumBy.xpath(String.format("//*[@name='trip_header_date' and @label='%s']", subtitle)));
        driverTools.click(tripSubTitle);
    }

    /** The trip header does not always expand on a normal click on iOS, so re-click it by its coordinate. */
    @Override
    protected boolean reopenTrip(Locator tripHeader) {
        logger.log("Trip card is not displayed, re-click " + tripHeader.name() + " by coordinate");
        driverTools.clickElementByCoordinate(tripHeader);
        return true;
    }

    @Override
    protected String getTripHeaderXpath(String location, String shortMonth) {
        return "//*[@name='trip_header_title' and contains(" + CoreFunctions.lowerCaseOf("@label") + "," + CoreFunctions.lowerCaseLiteral(location) + ")]"
                + "/following-sibling::*[@name='trip_header_date' and contains(" + CoreFunctions.lowerCaseOf("@label") + "," + CoreFunctions.lowerCaseLiteral(shortMonth) + ")]";
    }

    /** Container of the product cards listed under the given day / short month. */
    private String getTripProductDateXpath(String day, String shortMonth) {
        return "(//*[@label='" + day + "']/parent::*/parent::*/following-sibling::*[" + CoreFunctions.lowerCaseOf("@label") + "=" + CoreFunctions.lowerCaseLiteral(shortMonth) + "]"
                + "//parent::*//parent::*//parent::*)[1]";
    }

    private String getTripProductTitleXpath(String productName) {
        return "*[@name='trip_order_title' and " + CoreFunctions.lowerCaseOf("@label") + "=" + CoreFunctions.lowerCaseLiteral(productName) + "]";
    }

    @Override
    protected Locator getTripCardLocator(String day, String shortMonth, String productName) {
        return new Locator("TRIP_CARD", AppiumBy.xpath(getTripProductDateXpath(day, shortMonth)
                + "/following-sibling::*//" + getTripProductTitleXpath(productName)));
    }

    @Override
    protected Locator getTripCardAlternativeLocator(String day, String shortMonth, String productName) {
        return new Locator("TRIP_CARD_ALTERNATIVE", AppiumBy.xpath("(" + getTripProductDateXpath(day, shortMonth)
                + "//following::" + getTripProductTitleXpath(productName)
                + "//parent::*//parent::*//parent::*//parent::*//parent::*/preceding-sibling::*"
                + "//parent::*//parent::*//parent::*//parent::*//parent::*//parent::*//parent::*"
                + "/preceding-sibling::XCUIElementTypeOther)[1]"));
    }
}
