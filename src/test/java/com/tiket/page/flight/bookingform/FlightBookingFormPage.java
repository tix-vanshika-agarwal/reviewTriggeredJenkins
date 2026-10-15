package com.tiket.page.flight.bookingform;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.flight.*;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public abstract class FlightBookingFormPage<T extends WebDriver> extends BasePage<T> {
    public FlightBookingFormPage(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> FlightBookingFormPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new FlightBookingFormPageAndroid<>(session);}
            case IOS -> {return new FlightBookingFormPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }
    protected List<String> passengerNames = new ArrayList<>();
    public List<String> getpassengerNames(){
        logger.log("passenger list: " + passengerNames);
        return passengerNames;
    }

    public abstract boolean isTripDetailCorrect(String tripType,String departureDate, String departureAirport, String arrivalDate, String arrivalAirport);
    public abstract List<String> fillPassengerDetail(int adult, int child, int infant, String flightRoute, String airline);
    public abstract void fillContactDetail(String phone, String email, String loginType,String airline);
    public abstract void continueToSecondHalf();
    public abstract boolean labelsOnBookingPageCorrect();
    public abstract int addBaggage();
    public abstract int getBaggage();
    public abstract boolean isAddOnPresent(String addOn, String direction);
    public abstract void goBackToAddOnSection();
    public abstract int addBaggage(PriceDetail priceDetail);
    public abstract FlightBaggageDetail addBaggageRoundTrip(int baggageAmount, PriceDetail priceDetail,String currency);
    public abstract FlightBaggageDetail addBaggageOneWay(int baggageAmount, PriceDetail priceDetail,String currency);
    public abstract ArrayList<String> addCovidTest();
    public abstract String addMeals(int adultNumber, int childNumber, String currency);
    public abstract FlightMealsDetail addMealsRoundTrip(int adultNumber, int childNumber, String currency);
    public abstract FlightMealsDetail addMealsOneWay(int adultNumber, int childNumber, String currency);
    /**
     * Opens Meals add-on on booking form 2nd half and selects meals for return segment(s) only.
     * Selects up to {@code mealsPerPassenger} meals for each adult/child passenger on each return segment.
     */
    public abstract FlightMealsDetail addMealsReturnOnly(int adultNumber, int childNumber, String currency, int mealsPerPassenger);
    /**
     * Opens Meals from booking-form addons (Add/Change) and selects free meal(s). No currency assert (FREE is valid).
     * @return meal total text (e.g. FREE / IDR …)
     */
    public abstract String selectFreeMealsFromAddons(int adultNumber, int childNumber);
    /**
     * Opens Seats from booking-form addons (Add/Change) and selects free seat(s). No currency assert (FREE is valid).
     * @return seat total text (e.g. FREE / IDR …)
     */
    public abstract String selectFreeSeatsFromAddons(PriceDetail priceDetail);
    /**
     * Opens Meals from booking-form addons (or uses Book Meals if already open), adds meals until
     * max allowed, asserts further Add is disabled, then Saves.
     * @return total number of meals added across all processed adult + child passengers
     */
    public abstract int selectMealsUntilMaximumFromAddons(int adultNumber, int childNumber);
    /**
     * Opens Seats from booking-form addons and selects free/available seat(s). No currency assert.
     */
    public abstract String selectFreeSeatFromAddons(PriceDetail priceDetail);
    public abstract boolean isTravelRequirementInfoPagePresent();
    public abstract boolean isPriceBreakdownCorrect(PriceDetail priceDetail, String currency);
    public abstract void openPriceDetail();
    public abstract boolean checkInsurance();
    public abstract void continueToPayment();
    public abstract void baggageAllowancePopup();
    /**
     * Dismisses the baggage offer add-ons interstitial (shown after the booking form) by choosing
     * "No, thank you". Best-effort: does nothing if the offer page is not displayed.
     */
    public abstract void declineBaggageOffer();
    public abstract void skipFullProtection();
    public abstract void skipFullProtectionPrp();
    public abstract boolean isBookingFormPageDisplayed();
    public abstract boolean verifyDepartureInfoBookingPage();
    public abstract boolean verifyReturnInfoBookingPage();
    public abstract String getBaggagePrice(int baggageAmount);
    public abstract int getBaggageAmount(int baggageAmount,PriceDetail priceDetail);
    public abstract void saveAddBaggage();
    public abstract boolean checkPassportPopup();
    public abstract boolean checkPassportBanner();
    /**
     * True when booking form shows a cached/existing passenger from app cache (guest) or saved list.
     */
    public abstract boolean isExistingPassengerDisplayed();
    /**
     * Selects the cached passenger matching {@code passengerName} without completing missing
     * international identity fields (issuing date / issuing country).
     */
    public abstract void selectCachedPassengerWithoutCompleting(String passengerName);
    /**
     * Opens the selected passenger form and returns true when Issuing Date and Issuing Country are empty.
     */
    public abstract boolean areIssuingDateAndCountryEmpty();
    /**
     * Taps Continue on booking form page 1 and returns true when passenger-selection validation
     * (e.g. red helper "Select 1 adult.") is shown and the user stays on page 1/2.
     */
    public abstract boolean isBlockedByPassengerSelectionValidation();
    /**
     * Selects the first saved passenger on the booking form (any name), completing its missing
     */
    public abstract void selectAnySavedPassenger();
    public abstract String addFullProtection();
    public abstract String addBaggageInsurance();
    public abstract String addCovid19Insurance();
    public abstract ArrayList<String> addExtraProtection(int index);
    public abstract void removeExtraProtection();
    public abstract void removeExtraProtectionAll();
    public abstract String addSeat(PriceDetail priceDetail,String currency);
    public abstract void clickAddOnSeat();
    public abstract void clickChooseSeat();
    public abstract boolean isQuiteZoneBannerDisplayed();
    public abstract String addQuietZoneSeat(PriceDetail priceDetail,String currency);
    public abstract FlightSeatDetails addSeatRoundTrip(PriceDetail priceDetail, String currency);
    public abstract FlightSeatDetails addSeatOneWay(PriceDetail priceDetail, String currency);
    public abstract boolean isWhatYouGetCorrect(String loyalty);
    public abstract boolean isWhatYouGetCorrectVI();
    public abstract boolean isDiscountPriceCorrect(double discount, String discType);
    public abstract boolean isDiscountTagDisplayed();
    public abstract boolean isAdjustmentPresentInBooking();
    public abstract boolean isDiscountPriceRTCorrect(double discount,double discountRT, String discType);
    public abstract boolean offerBreakdown(String label,PriceDetail priceDetail);
    public abstract boolean isTotalPriceWithoutDiscount(int adultPassenger);
    public abstract void isOnBookingPage();
    public abstract boolean verifyChangeEmail(String email);
    public abstract boolean isYellowBannerBookingPage2Displayed();
    public abstract void clickYellowBannerBookingPage2();
    public abstract void openLoginPage();
    public abstract boolean isLoggedIn();
    public abstract void verifyDateFormat(String tripType);
    public abstract List<String> getFlightDateTime(String tripType);
    public abstract boolean verifyPromoInfinitySection();
    public abstract boolean verifyPromoInfinitySection2ndPage();
    public abstract void swipeToPromoInfinityPart();
    public abstract void openPromoInfinity();
    public abstract PromoData applyPromoInfinityRandomAndGetData(int index);
    public abstract void removePromo();
    public abstract boolean isPromoCodeAndAmountShown(PromoData promoData);
    public abstract boolean isPromoCodeAndAmountDisplayedInPriceDetail(PromoData promoData);
    public abstract boolean isConnectingFlightProtectionNudgeDisplayed();
    public abstract boolean verifyMoreBenefit();
    public abstract boolean verifyWarningMassage();
    public abstract boolean connectingFlightProtectionDisplayedOnPriceDetail(int passengerCount,String currency);
    public abstract boolean isGreenBannerVisible();
    public abstract boolean isCashbackVisible();
    public abstract void otherOrderStillWaitingPopUp();
    public abstract void waitForLoadingDialogToDisappear();

    //WebView
    public static final Locator RESCHEDULE_PRICE_WEBVIEW = new Locator("RESCHEDULE_PRICE_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'BookingFooter_price')]//span"));
    private static final Locator CONTINUE_TO_PAYMENT_PAGE_POPUP = new Locator("CONTINUE_TO_PAYMENT_PAGE_POPUP", new By.ByXPath("//div[contains(@class,'Figure_footer')]//button[contains(@class,'primary')]"));
    public static final Locator NEXT_BTN_WEBVIEW = new Locator("NEXT_BTN_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'BookingFooter_footer')]//button"));
    public static final Locator ALERT_YES_WEBVIEW = new Locator("ALERT_YES_WEBVIEW", AppiumBy.xpath("//*[@class='alert-btn']"));
    private static final Locator FLIGHT_MAY_RESCHEDULED_POPUP = new Locator("FLIGHT_MAY_RESCHEDULED_POPUP", new By.ByXPath("//*[@data-testid=\"popup-button-secondary\"]"));
    private static final Locator PRICE_CHANGE_POPUP = new Locator("PRICE_CHANGE_POPUP", new By.ByXPath("//*[contains(text(),'OK, Mengerti') or contains(text(),'OK, Got It')]"));

    public String getReschedulePrice() {
        driverTools.switchToWebViewUrl("cart");
        WebElement reschedulePrice = driverTools.waitForElementToAppear(RESCHEDULE_PRICE_WEBVIEW).orElseThrow();
        String price = driverTools.getText(reschedulePrice);
        logger.log("Reschedule price: " + price);
        driverTools.switchToNative();
        return price;
    }

    public void clickNextButtonWebView() {
        driverTools.switchToWebViewUrl("cart");
        driverTools.waitForElementToAppear(NEXT_BTN_WEBVIEW);
        driverTools.click(NEXT_BTN_WEBVIEW);
        driverTools.switchToNative();
    }

    public void clickContinueToPaymentWebView() {
        driverTools.switchToWebViewUrl("cart");
        driverTools.click(NEXT_BTN_WEBVIEW);
        driverTools.waitForElementToPresence(CONTINUE_TO_PAYMENT_PAGE_POPUP);
        driverTools.click(CONTINUE_TO_PAYMENT_PAGE_POPUP);
        driverTools.switchToNative();
    }

    public void clickNoThanksOnFlightMayReschedulePopup() {
        driverTools.switchToWebViewUrl("cart");
        driverTools.waitForElementToPresence(FLIGHT_MAY_RESCHEDULED_POPUP);
        if(driverTools.isElementDisplayed(FLIGHT_MAY_RESCHEDULED_POPUP)){
            driverTools.clickWithJavascriptExecutor(FLIGHT_MAY_RESCHEDULED_POPUP);
        }
        driverTools.waitForElementToPresence(PRICE_CHANGE_POPUP).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.switchToNative();
    }
}
