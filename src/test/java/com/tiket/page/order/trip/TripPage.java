package com.tiket.page.order.trip;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.model.TripModel;
import com.tiket.page.cartransfer.searchpage.CarTransferSearchPage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.order.trip.switcher.SwitcherPage;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.util.Optional;

public abstract class TripPage<T extends WebDriver> extends BasePage<T> {

    public SwitcherPage<?> switcherPage;

    public TripPage(Session<T> session) {
        super(session);
        switcherPage = SwitcherPage.getInstance(session);
    }

    public static <T extends WebDriver> TripPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new TripPageAndroid<>(session);
            }
            case IOS -> {
                return new TripPageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void skipCoachmark();
    public abstract boolean verifySeeAllOrdersButton();
    public abstract String openFirstOrder();
    public abstract String openFirstETiket();
    public abstract boolean verifyTripsCanBeExpandedAndCollapsed();
    public abstract void gotoTrip(TripModel.Trip trip);
    public abstract void openTrip(TripModel.Trip trip);
    public abstract void clickTripSubtitle(String subtitle);
    public abstract void gotoCard(String title);
    public abstract void gotoLowProfileCrossSell(String title);
    public abstract boolean verifyLowProfileCrossSell();
    public abstract boolean gotoHighProfileCrossSell();
    public abstract boolean verifyHighProfileCrossSell();

    public abstract boolean verifyTTDCardDetails(TripModel.TTDCard card);
    public abstract boolean verifyHotelCardDetails(TripModel.HotelCard card);
    public abstract boolean verifyTrainCardDetails(TripModel.TrainCard card);

    public boolean openTripViewSection() {
        var switcherPage = SwitcherPage.getInstance(session);
        return switcherPage.switchToTrip();
    }

    public boolean openOrderListSection() {
        var switcherPage = SwitcherPage.getInstance(session);
        return switcherPage.switchToOrder();
    }

    public String getEtiketIdFromTtdDetailPage() {
        final var orderIDLocator = new Locator("ORDER_ID", AppiumBy.xpath("(//*[contains(@class,'EVoucherHeader_order_detail_section')]//span)[1]"));
        final var backButton = new Locator("BACK_BUTTON", AppiumBy.xpath("(//*[contains(@class,'EVoucherMobileHeaderNav_cursor_pointer')])[1]"));
        var webviewPage = WebViewPage.getInstance(session);
        if(Configs.PLATFORM == Platform.ANDROID) {
            webviewPage.waitForWebview(10);
        }
        webviewPage.switchToWebViewUrl("voucher");
        String content = driverTools.getText(orderIDLocator).orElseThrow();
        driverTools.click(backButton);
        webviewPage.switchToNative();
        return content;
    }

    public void handleCarTransferPage() {
        final var carTransferBackButton = new Locator("CAR_TRANSFER_BACK_BUTTON", AppiumBy.xpath("//a[contains(@class,'HeroImage_button')]"));
        final var carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        final var webViewPage = WebViewPage.getInstance(session);
        carTransferSearchPage.allowLocationAccess();
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();
        driverTools.click(carTransferBackButton);
        webViewPage.switchToNative();
    }

    public void refreshTripView() {
        final var homepage = HomePage.getInstance(session);
        homepage.openAccount();
        homepage.openMyOrderPage();
    }

    /** XPath of the trip headers ("trip button") whose title contains {@code location} and whose date contains {@code shortMonth}. */
    protected abstract String getTripHeaderXpath(String location, String shortMonth);

    /** 1st locator of the product card of an expanded trip on the given day / short month. */
    protected abstract Locator getTripCardLocator(String day, String shortMonth, String productName);

    /** 2nd (alternative) locator of the product card, used when the 1st one is not found. */
    protected abstract Locator getTripCardAlternativeLocator(String day, String shortMonth, String productName);

    /**
     * On the itinerary view, opens every trip whose title contains {@code location} and whose date contains the
     * short month of {@code startDate} (in the app language), one after another, until a product card with
     * {@code productName} on {@code startDate} is found.
     *
     * @param startDate date in {@code yyyy-MM-dd} format
     * @param language  app language, from {@code homePage.getLanguageApp()} ({@code "en"} or {@code "id"})
     * @return {@code true} when the card is found under one of the matching trips
     */
    public boolean isTripCardDisplayedOnItinerary(String location, String startDate, String productName, String language) {
        LocalDate date = LocalDate.parse(startDate);
        String day = String.valueOf(date.getDayOfMonth());
        String shortMonth = CoreFunctions.getShortMonthName(date.getMonthValue(), language);
        logger.log("Looking for trip card '" + productName + "' on " + day + " " + shortMonth + " under trip location '" + location + "'");

        String tripHeadersXpath = getTripHeaderXpath(location, shortMonth);
        for (int tripIndex = 1; tripIndex <= MAX_TRIPS_TO_CHECK; tripIndex++) {
            Locator tripHeader = new Locator("TRIP_HEADER_" + tripIndex,
                    AppiumBy.xpath("(" + tripHeadersXpath + ")[" + tripIndex + "]"));
            Optional<WebElement> tripHeaderElement = driverTools.swipeToElement(tripHeader, Swipe.UP, 5);
            if (tripHeaderElement.isEmpty()) {
                logger.log("No more trip found for location '" + location + "', checked " + (tripIndex - 1) + " trip(s)");
                break;
            }

            logger.log("Open trip no. " + tripIndex);
            driverTools.click(tripHeaderElement.get());
            boolean isTripCardFound = findTripCard(day, shortMonth, productName).isPresent();
            if (!isTripCardFound && reopenTrip(tripHeader)) {
                isTripCardFound = findTripCard(day, shortMonth, productName).isPresent();
            }

            // Go back to the trip header and collapse it, so the next trip (or the next check) starts from the same state.
            driverTools.swipeToElement(tripHeader, Swipe.DOWN, 5).ifPresent(driverTools::click);
            if (isTripCardFound) {
                logger.log("Trip card '" + productName + "' found under trip no. " + tripIndex);
                return true;
            }
            logger.log("Trip card '" + productName + "' not found under trip no. " + tripIndex);
        }
        return false;
    }

    private static final int MAX_TRIPS_TO_CHECK = 5;

    /** Looks for the product card of an expanded trip: 1st locator by swiping up, then the alternative one by swiping back down. */
    private Optional<WebElement> findTripCard(String day, String shortMonth, String productName) {
        Optional<WebElement> tripCard = driverTools.swipeToElement(getTripCardLocator(day, shortMonth, productName), Swipe.UP, 5);
        if (tripCard.isPresent()) {
            return tripCard;
        }
        // The list has been swiped up to 5 times already, so swipe back down while looking for the 2nd locator.
        logger.log("Trip card not found with the 1st locator, swipe to find it with the 2nd locator");
        return driverTools.swipeToElement(getTripCardAlternativeLocator(day, shortMonth, productName), Swipe.DOWN, 5);
    }

    /**
     * Called when the trip card is not displayed after clicking the trip header, to try opening the trip again.
     * No retry by default; iOS overrides it.
     *
     * @return {@code true} when the trip header was clicked again, so the trip card should be looked up again
     */
    protected boolean reopenTrip(Locator tripHeader) {
        return false;
    }
}
