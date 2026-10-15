package com.tiket.page.whoosh.searchpage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.train.searchpage.TrainSearchPage;
import com.tiket.page.whoosh.searchresultpage.WhooshSearchResultPage;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class WhooshSearchPage <T extends WebDriver> extends BasePage<T> {

    public WhooshSearchPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> com.tiket.page.whoosh.searchpage.WhooshSearchPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new WhooshSearchPageAndroid<>(session);
            }
            case IOS -> {
                return new WhooshSearchPageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public void skipPopup(){
        TrainSearchPage<T> trainSearchPage = TrainSearchPage.getInstance(session);
        trainSearchPage.skipPopup();
    }
    public abstract void selectOrigin(String stationName);
    public abstract String selectOriginAndGetCityName(String stationName);
    public abstract void selectDestination(String stationName);
    public abstract String selectDestinationAndGetCityName(String stationName);
    public void selectDate(String departureDate){
        TrainSearchPage<T> trainSearchPage = TrainSearchPage.getInstance(session);
        trainSearchPage.selectDate(departureDate);
    }
    public void selectDate(String departureDate,String returnDate){
        TrainSearchPage<T> trainSearchPage = TrainSearchPage.getInstance(session);
        trainSearchPage.selectDate(departureDate,returnDate);
    }
    public abstract void searchTrain();
    public abstract void selectTrainViaIndex(int index);
    public void selectRoundTripToggle(String tripType){
        TrainSearchPage<T> trainSearchPage = TrainSearchPage.getInstance(session);
        trainSearchPage.toggleTripType(tripType);
    }
    public void addPassenger(int adults, int infants){
        TrainSearchPage<T> trainSearchPage = TrainSearchPage.getInstance(session);
        trainSearchPage.addPassenger(adults,infants);
    }
    public void clearRecentlySearch(){
        TrainSearchPage<T> trainSearchPage = TrainSearchPage.getInstance(session);
        trainSearchPage.clearRecentlySearch();
    }
    public String getWhooshDate(TrainAndWhooshGeneralData whooshGeneralData){
        TrainSearchPage<T> trainSearchPage = TrainSearchPage.getInstance(session);
        return trainSearchPage.getSelectedDate(whooshGeneralData);
    }

    public boolean verifyRecentlySearch(TrainAndWhooshGeneralData trainAndWhooshGeneralData){
        TrainSearchPage<T> trainSearchPage = TrainSearchPage.getInstance(session);
        return trainSearchPage.verifyRecentlySearch(trainAndWhooshGeneralData);
    }
    public void clickRecentlySearch(String whooshDateEn,String whooshDateId){
        TrainSearchPage<T> trainSearchPage = TrainSearchPage.getInstance(session);
        trainSearchPage.clickRecentlySearch(whooshDateEn,whooshDateId);
    }

    /**
     * iOS-only helper that fills the Whoosh search form date via deeplink, bypassing
     * the native calendar widget (which on iOS sometimes drops same-week dates from
     * the accessibility tree after the depart date is picked).
     * <p>
     * Mirrors the pattern used by {@code FlightSearchPage#changeDateViaDeeplink}.
     * Origin/destination are hard-coded to {@code Padalarang Station (IDPGA) -> Halim (IDHMA)}
     * because the deeplink only needs to populate the date; the caller is expected to
     * re-select the real origin/destination afterwards.
     * <p>
     * No-op on Android.
     *
     * @param departureDate departure date in long format {@code "d MMMM yyyy"} (e.g. {@code "26 May 2026"}).
     * @param returnDate    return date in long format {@code "d MMMM yyyy"}; ignored when {@code tripType} is {@code oneway}.
     * @param tripType      {@code "roundtrip"} or {@code "oneway"} (case-insensitive).
     */
    public void changeDateViaDeeplink(String departureDate, String returnDate, String tripType) {
        String methodName = "changeDateViaDeeplink";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            if (Configs.PLATFORM == Platform.ANDROID) {
                logger.log("changeDateViaDeeplink is iOS-only, skipping on Android");
                if (tripType.toLowerCase().contains("oneway"))
                    selectDate(departureDate);
                else
                    selectDate(departureDate, returnDate);
                return;
            }

            WhooshSearchPage<T> whooshSearchPage = WhooshSearchPage.getInstance(session);
            WhooshSearchResultPage<T> whooshSearchResultPage = WhooshSearchResultPage.getInstance(session);

            String yyyyMMddDepart = CoreFunctions.convertDateFormat(departureDate);
            boolean isOneWay = tripType != null && tripType.toLowerCase().contains("oneway");

            if (isOneWay) {
                whooshSearchPage.deeplinkToWhooshSrp(yyyyMMddDepart, 1, 0, 0);
            } else {
                if (returnDate == null || returnDate.isBlank()) {
                    throw new IllegalArgumentException("returnDate must be provided for round-trip");
                }
                String yyyyMMddReturn = CoreFunctions.convertDateFormat(returnDate);
                whooshSearchPage.deeplinkToWhooshSrpRoundTrip(yyyyMMddDepart, yyyyMMddReturn, 1, 0, 0);
            }

            // Whoosh SRP does not expose the Train app's `trainSearchResultSearchInformationView`
            // accessibility id, so we cannot reuse TrainSearchPage.isResultViewLoaded() here.
            // The "Filters & Sort" coachmark and any NFT modal can render a full-screen scrim
            // over the SRP that intercepts taps (including on the back button), so we must
            // dismiss them before navigating back.
            whooshSearchPage.skipPopup();
            whooshSearchResultPage.backToSearchPage();
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    /**
     * iOS implementation: hits the Whoosh SRP deeplink for a one-way search with the
     * given {@code yyyy-MM-dd} departure date. Hard-codes Padalarang -> Halim.
     * Android impl is a no-op.
     */
    public abstract void deeplinkToWhooshSrp(String yyyyMMddDate, int adult, int infant, int child);

    /**
     * iOS implementation: hits the Whoosh SRP deeplink for a round-trip search with
     * the given {@code yyyy-MM-dd} depart/return dates. Hard-codes Padalarang -> Halim.
     * Android impl is a no-op.
     */
    public abstract void deeplinkToWhooshSrpRoundTrip(String yyyyMMddDepart, String yyyyMMddReturn, int adult, int infant, int child);
}