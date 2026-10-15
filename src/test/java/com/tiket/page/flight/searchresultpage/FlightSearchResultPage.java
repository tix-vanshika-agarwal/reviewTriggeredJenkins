package com.tiket.page.flight.searchresultpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public abstract class FlightSearchResultPage<T extends WebDriver> extends BasePage<T> {

    public FlightSearchResultPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FlightSearchResultPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new FlightSearchResultPageAndroid<>(session);}
            case IOS -> {return new FlightSearchResultPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public void filterAirline(String airline){
        logger.log("Filtering by airline: " + airline);
        ArrayList<String> airlineList = CoreFunctions.makeAirlineArrayList(airline);
        filterAirline(airlineList);
    }
    public abstract void filterAirline(ArrayList<String> airline);
    public abstract String filterFirstFromListAirline(ArrayList<String> airline);
    public abstract void checkReloadPopup();
    public abstract void changeDate(String date);
    public abstract void changeTrip(String tripType);
    public abstract void openFilter();
    public abstract void openAdvancedFilter();
    public abstract void resetFilter();
    public abstract void filterMorningFlight();
    public abstract void saveFilter();
    public abstract void filterOther(String filterType);
    public abstract boolean isPromoPagePresent();
    public abstract boolean isTravelInfoPagePresent();
    public abstract boolean isImportantInfoPagePresent();
    public abstract void selectFlight();
    public abstract void selectFlightSpecialCampaign(String airline);
    public abstract void selectFlightVI(String airline);
    public abstract void selectFlightCorporateDeals();
    public abstract void isSearchDateToday(String departureDate,String lang);
    public abstract void selectFlightStopOver(); //fixme implementation pending ios -> missing unique id
    public abstract boolean isOtherFacilitiesPresent();
    public abstract void clickPricePolicyTab();
    public abstract boolean isRefundReschedulePolicyCorrect();
    public abstract boolean isPriceBreakdownCorrect(int adult, int child, int infant);
    public abstract String getDepartureDate();
    public abstract String getDepartureAirport();
    public abstract String getArrivalDate();
    public abstract String getArrivalAirport();
    @Deprecated
    public abstract void selectTicket();
    public abstract boolean isLowestPriceGuaranteePagePresent();
    public abstract String getPrice(String type, int amount);
    public abstract void filterTransitType(String type);
    public abstract void filterChips(String type);
    public abstract boolean isFreeTransfer();
    public abstract boolean isTest();
    public abstract boolean isFreeTransferDetail();
    public abstract boolean checkDepartureDetailsPRP();
    public abstract boolean checkReturnDetailsPRP();
    public abstract boolean isFreeTransferDetailOneWay();
    public abstract boolean isTestDetail();
    public abstract boolean isTestDetailOneWay();
    public abstract boolean verifyLoyaltyBanner(String loyaltyType);
    public abstract boolean isCorporateDealsBannerPresent();
    public abstract boolean gotoCorporateDealsBenefitCard();
    public abstract boolean isTransitInfoPresentOnSrpCardDetail();
    public abstract boolean verifyDiscountTag();
    public abstract double verifyDiscountPrice(String discType);
    public abstract boolean isPriceSumCorrect();
    public abstract boolean isWhatYouGetCorrectSRP(String label);
    public abstract boolean isWhatYouGetCorrect(String label);
    public abstract void backToFlightPage();
    public abstract String getRouteText();
    public abstract boolean isOnSearchResultPage();

    /**
     * Minimizes the app by opening Google Chrome, verifies Chrome is in the foreground, then reopens
     * the app. Used to verify the SRP survives a background/foreground cycle via another app.
     *
     * @return {@code true} if Google Chrome was successfully brought to the foreground.
     */
    public boolean minimizeOpenChromeAndReopenApp() {
        return driverTools.minimizeOpenChromeAndReopen();
    }

    /** Whether an error/retry bottomsheet is currently shown over the search result page. */
    public abstract boolean isErrorBottomSheetDisplayed();
    public abstract boolean isAirlineWithDiscountedPrice();
    public abstract boolean isDiscountTagPresent();
    public abstract String getDiscountPrice();

    /**
     * Returns the per-card "Save IDR &lt;N&gt;" / "Hemat IDR &lt;N&gt;" savings amount shown on the SRP
     * for special-campaign fares such as <em>Harga Gledek</em>. This is distinct from
     * {@link #getDiscountPrice()} (which on Android resolves to the regular fare price element):
     * the value returned here is the TOTAL discount-vs-regular delta and is the figure that
     * must equal the booking-form breakdown's {@code Discount + Tix Points Cashback} sum during
     * special-campaign price verification.
     */
    public abstract String getSpecialCampaignSavingsPrice();
    public abstract boolean isAdjustmentPresentSearch();
    public abstract boolean isAdjustmentPresentDetail();
    public abstract boolean areFlightsAvailable();
    public abstract boolean isTotalPriceWithoutDiscount(int adult);
    public abstract boolean checkLabelOnCardSRP(String label);
    public abstract boolean checkTagPresentOnPriceAndPolicyTab(String expectedTag);
    public abstract boolean isIconDisplayedOnTheRightOfTotalPayment();
    public abstract String getPriceBasedOnLabel(String label);
    public abstract boolean isDiscountPriceForSpecialCampaignCorrectOnSRP(String date);
    public abstract boolean isRegisterBannerDisappear();
    public abstract boolean is100pRefundDisplayed();
    public abstract boolean check100pRefundOnPdp();
    public abstract boolean isMegaphoneIconDisplayed();
    public abstract void clickMegaphoneIcon();
    public abstract void clickLoginBanner();
    public abstract List<String> getPrices();
    public abstract void clickSeeDetailSrp(int index);
    public abstract void clickDetailPRP();
    public abstract boolean checkNudge(String enText,String idText);
    public abstract void clickFlightWithNudge(String enText,String idText);
    public abstract boolean isFloatingIconDisplayed();
    public abstract void clickFloatingIcon();
    public abstract void backFromFloatingIcon();
    public abstract void waitForFloatingIconToDisappeared();

    /**
     * True when sticky promo infinity Apply was handled on SRP (Cancel / applied copy).
     * Default false; Android overrides for the banner that replaced the FAB.
     */
    public boolean isPromoInfinityEntryHandled() {
        return false;
    }

    public abstract void openTransitFilter();
    public abstract void openSortFilter();
    public abstract void sortBy(String type);
    public abstract void verifySortSelected(String type);
    public abstract void verifyResetFilterDisabled();
    public abstract void verifyResetFilterEnabled();
    public abstract void verifyNoSortByGreenDot();
    public abstract void openAdvancedFilterSortTab();
    public abstract void selectSortOptionInAdvancedFilter(String type);
    public abstract void verifySortByFilterSelectedIndicator(String type);
    public abstract void verifySortByFilterReset(String type);

    /**
     * Sorts SRP via Filter &amp; Sort bottom sheet:
     * open Filter, verify default reset/green-dot state, select sort (or See N more),
     * verify reset enabled + green dot, reset, re-select, then Save.
     */
    public void sortByViaAdvancedFilter(String sortOption) {
        logger.log("1: Sorting by " + sortOption + " via Advanced Filter");
        openAdvancedFilter();
        logger.log("2: Verifying reset filter is disabled and no green dot is present");
        verifyResetFilterDisabled();
        logger.log("3: Selecting sort option: " + sortOption);
        openAdvancedFilterSortTab();
        logger.log("4: Verifying no green dot is present on sort option");
        verifyNoSortByGreenDot();
        logger.log("5: Selecting sort option in advanced filter: " + sortOption);
        selectSortOptionInAdvancedFilter(sortOption);
        logger.log("6: Verifying sort option is selected and green dot is present: " + sortOption);
        verifySortByFilterSelectedIndicator(sortOption);
        logger.log("7: Resetting filter");
        resetFilter();
        logger.log("8: Verifying reset filter is disabled and no green dot is present");
        verifySortByFilterReset(sortOption);
        logger.log("9: Re-selecting sort option in advanced filter: " + sortOption);
        selectSortOptionInAdvancedFilter(sortOption);
        logger.log("10: Verifying sort option is selected and green dot is present: " + sortOption);
        saveFilter();
        waitForPageLoad();
    }
    public abstract double getPriceOnDatePicker();
    public abstract double getFirstFlightCardPrice();
    public abstract boolean isRoundTripDealLabelDisplayed();
    public abstract double getLastFlightCardPriceBeforeDeltaDesc();
    public abstract double getFirstFlightCardPriceAfterDeltaDesc();
    public abstract void swipeToDeltaPriceDesc();
    public abstract void selectFirstFlightWithRoundTripDealsLabel(String roundTripDealsFilter);
    public abstract void selectFirstFlightWithoutRoundTripDealsAfterDeltaDesc();
    public abstract void clickChangeBtn();
    public abstract void clickSearchBtn();
    public abstract void verifySelectedDateSRP(String dateSelectedDepart,String dateSelectedReturn,String lang);
    public abstract boolean isSearchResultPageEmptyForEconomy(String currency);
    public abstract boolean isSearchResultShownForPremiumEconomy(String currency);
    public abstract boolean isMeetTravelPolicy();
    public abstract boolean isMeetTravelPolicyAll(String airline);
    public abstract void applyPromoInfinity();

    /**
     * Whether the sticky promo infinity banner's Apply button is shown on SRP. Promo infinity is a
     * one-way only entry point, so this returns {@code false} on a round-trip SRP.
     */
    public abstract boolean isPromoInfinityApplyButtonDisplayed();
    public abstract boolean isPromoInfinityApplied();
    public abstract List<String> getAirlineNames(String Airline,String airline2);
    public abstract boolean checkAirlines(ArrayList<String> airlines,int numberOfSwipe);
    public abstract boolean checkAirlinesOnFilter(ArrayList<String> airlines,int numberOfSwipe);
    public abstract boolean checkAirlineVI(ArrayList<String> Airline);
    public abstract ArrayList<String> getAirlineNames(ArrayList<String> airline);


    public abstract boolean verifyAllVisibleFlightCardsMatchAirlineFilter(String appliedAirline, int maxFlightCardsToCheck);

    /**
     * Same as {@link #verifyAllVisibleFlightCardsMatchAirlineFilter(String, int)} with a default cap of three cards.
     */
    public boolean verifyAllVisibleFlightCardsMatchAirlineFilter(String appliedAirline) {
        return verifyAllVisibleFlightCardsMatchAirlineFilter(appliedAirline, 3);
    }

    /**
     * Reads the count in parentheses from the Airlines filter row label while the sheet is open
     * (e.g. {@code Garuda Indonesia (2)} → {@code 2}). Returns {@code -1} if the label/count is missing.
     */
    public abstract int getAirlineFilterOptionCount(String airline);

    /**
     * Scrolls SRP flight cards and maps each card (1st, 2nd, …) to airline, schedule time
     * ({@code depart - arrival}), and flight name. Returns {@code true} when every mapped schedule
     * is unique, each card's airline/flight name matches {@code expectedAirline}, and the number of
     * mapped cards equals {@code expectedCardCountFromFilter} (from {@code Airline (N)} on the filter).
     */
    public abstract boolean areAllFlightSchedulesUnique(String expectedAirline, int expectedCardCountFromFilter);

    public abstract boolean isVI_AirlineDisplayed();

    /**
     * Reads the first SRP flight card combined airline label (e.g. {@code Lion Air + Super Jet Air}),
     * splits on {@code +}, trims segments, and returns them in order (one element if there is no {@code +}).
     */
    public abstract List<String> getFirstFlightCardAirlineNamesSplitByPlus();

    protected final List<String> splitAirlineNamesCombinedByPlus(String raw) {
        if (raw == null || raw.isBlank()) {
            return List.of();
        }
        return Arrays.stream(raw.split("\\s*\\+\\s*"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    /**
     * Label from the first VI card plus optional airline code resolved against
     * {@link com.tiket.apiPage.FlightApi#getLastOneWayStreamingAirlineCodeToName()} (code → display name).
     */
    public record StreamingAirlineNameAndCode(String displayName, Optional<String> airlineCode) {}

    /**
     * Resolves a UI airline label to an IATA-style code using the streaming directory map (keys are codes, values are
     * display names). Call once per airline segment (e.g. each value from {@link #getFirstFlightCardAirlineNamesSplitByPlus()}).
     */
    public final Optional<String> resolveAirlineCodeFromStreamingDirectory(
            Map<String, String> streamingCodeToName,
            String uiDisplayName) {
        if (streamingCodeToName == null || streamingCodeToName.isEmpty() || uiDisplayName == null) {
            return Optional.empty();
        }
        String needle = uiDisplayName.trim();
        if (needle.isEmpty()) {
            return Optional.empty();
        }
        for (Map.Entry<String, String> e : streamingCodeToName.entrySet()) {
            String mapped = e.getValue();
            if (mapped == null) {
                continue;
            }
            String candidate = mapped.trim();
            if (candidate.equalsIgnoreCase(needle)) {
                String code = e.getKey();
                logger.log("Resolved streaming airline (exact name): UI label='" + needle + "' -> code='" + code + "'");
                return Optional.of(code);
            }
        }
        for (Map.Entry<String, String> e : streamingCodeToName.entrySet()) {
            String mapped = e.getValue();
            if (mapped == null) {
                continue;
            }
            if (displayNamesMatchByTokens(needle, mapped.trim())) {
                String code = e.getKey();
                logger.log("Resolved streaming airline (token match): UI label='" + needle + "' -> code='" + code + "'");
                return Optional.of(code);
            }
        }
        String needleLower = needle.toLowerCase(Locale.ROOT);
        for (Map.Entry<String, String> e : streamingCodeToName.entrySet()) {
            String mapped = e.getValue();
            if (mapped == null) {
                continue;
            }
            String m = mapped.trim();
            if (m.length() < 3 || needle.length() < 3) {
                continue;
            }
            String mLower = m.toLowerCase(Locale.ROOT);
            if (mLower.contains(needleLower) || needleLower.contains(mLower)) {
                String code = e.getKey();
                logger.log("Resolved streaming airline (substring match): UI label='" + needle + "' -> code='" + code + "'");
                return Optional.of(code);
            }
        }
        logger.softWarning("No streaming airline code matched UI label: '" + needle + "'");
        return Optional.empty();
    }

    /**
     * First-card VI segments from {@link #getFirstFlightCardAirlineNamesSplitByPlus()}, each resolved with
     * {@link #resolveAirlineCodeFromStreamingDirectory(Map, String)}.
     */
    public final List<StreamingAirlineNameAndCode> getFirstViCardAirlinesResolvedAgainstStreamingDirectory(
            Map<String, String> streamingCodeToName) {
        List<StreamingAirlineNameAndCode> out = new ArrayList<>();
        for (String name : getFirstFlightCardAirlineNamesSplitByPlus()) {
            out.add(new StreamingAirlineNameAndCode(name, resolveAirlineCodeFromStreamingDirectory(streamingCodeToName, name)));
        }
        return List.copyOf(out);
    }

    /**
     * Same word-count token equality as API-side matching (case-insensitive per token).
     */
    private static boolean displayNamesMatchByTokens(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        a = a.trim();
        b = b.trim();
        if (a.isEmpty() || b.isEmpty()) {
            return false;
        }
        String[] ta = a.split("\\s+");
        String[] tb = b.split("\\s+");
        if (ta.length != tb.length) {
            return false;
        }
        for (int i = 0; i < ta.length; i++) {
            if (!ta[i].equalsIgnoreCase(tb[i])) {
                return false;
            }
        }
        return true;
    }

    public abstract void waitForPageLoad();
    public abstract boolean SaveLabel();
    public abstract boolean CheckCashBackLabel();
    public abstract Boolean verifySRPNudges(String language);
    public abstract void selectTicketFromDetailsPage();

    //WebView
    public static final Locator FILTER_ICON_WEBVIEW = new Locator("FILTER_ICON_WEBVIEW", AppiumBy.xpath("(//*[contains(@class,'FilterSortBottomBar_srp_bottom_bar')])[3]"));
    public static final Locator CLOSE_BTN_WEBVIEW = new Locator("CLOSE_BTN_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'BottomSheet_close_button')]"));
    public static final Locator SAVE_FILTER_BUTTON_WEBVIEW = new Locator("SAVE_FILTER_BUTTON_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'FilterFormSheet_button_save')]"));
    public static final Locator RESCHEDULE_PRICE_1_WEBVIEW = new Locator("RESCHEDULE_PRICE_1_WEBVIEW", AppiumBy.xpath("(//*[contains(@class,'PriceReschedule_price')])[1]"));

    public void openFilterWebView() {
        driverTools.switchToWebViewUrl("pesawat/search");
        driverTools.click(FILTER_ICON_WEBVIEW);
        driverTools.switchToNative();
    }

    public void filterAirlineWebView(String airline) {
        driverTools.switchToWebViewUrl("pesawat/search");
        driverTools.waitForElementToPresence(CLOSE_BTN_WEBVIEW);

        logger.log("Filtering by airline: " + airline);
        airline = CoreFunctions.titleize(airline);
        if (airline.contains("nam")) airline = airline.replace("Nam", "NAM");
        Locator airlineName = new Locator("Airline Name",AppiumBy.xpath("//*[contains(@class,'FilterForm_item')]//*[contains(@class,'Text_text') and contains(text(),'"+airline+"')]"));
        WebElement airlineElement = driverTools.swipeToElement(airlineName,Swipe.UP,6).orElseThrow();
        driverTools.click(airlineElement);
        driverTools.switchToNative();
    }

    public void saveFilterWebView() {
        driverTools.switchToWebViewUrl("pesawat/search");
        driverTools.click(SAVE_FILTER_BUTTON_WEBVIEW);
        driverTools.switchToNative();
    }

    public String getReschedulePrice() {
        driverTools.switchToWebViewUrl("pesawat/search");
        WebElement reschedulePrice = driverTools.waitForElementToAppear(RESCHEDULE_PRICE_1_WEBVIEW).orElseThrow();
        String price = driverTools.getText(reschedulePrice);
        logger.log("Reschedule price: " + price);
        driverTools.switchToNative();
        return price;
    }

    public void selectFlightWebView() {
        driverTools.switchToWebViewUrl("pesawat/search");
        driverTools.click(RESCHEDULE_PRICE_1_WEBVIEW);
        driverTools.switchToNative();
    }

    /**
     * Multi city: selects the 1st flight card once per route. The SRP reloads for the next route
     * after each selection, so it waits for the page load before every selection except the first.
     */
    public void selectFlightForEachRoute(List<String> routes) {
        for (int i = 0; i < routes.size(); i++) {
            logger.log("Select the 1st flight for route " + routes.get(i));
            if (i > 0) {
                waitForPageLoad();
            }
            selectFlight();
        }
    }
}
