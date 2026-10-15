package com.tiket.page.train.searchpage;

import com.tiket.model.Session;
import com.tiket.page.whoosh.searchpage.WhooshSearchPage;
import com.tiket.pojo.train.TrainClass;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.List;


public abstract class TrainSearchPage <T extends WebDriver> extends BasePage<T> {

    public TrainSearchPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TrainSearchPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new TrainSearchPageAndroid<>(session);}
            case IOS -> {return new TrainSearchPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void openTrainPage();
    public abstract void selectOrigin(String stationName);
    public abstract void selectDestination(String stationName);
    public abstract String selectOriginGetStationName(String stationName);
    public abstract String selectDestinationGetStationName(String stationName);
    public abstract void searchTrain();
    public abstract void switchOriginAndDestination();
    public abstract String[] getOriginAndDestination();
    public abstract boolean isResultViewLoaded();
    public abstract void backToHomePage(); //implementation pending on android
    public abstract void toggleTripType(String tripType);
    public abstract void selectDate(String departureDate, String returnDate);
    public abstract void selectDate(String departureDate);
    public abstract void addPassenger(int adults, int infants);
    public abstract void selectTrainViaIndex(int index);
    public abstract void openTrainDetailWithNudges();
    public abstract boolean verifyTrainDetailPrice();
    public abstract void clickChooseInTrainDetail();
    public abstract String getSelectedDate(TrainAndWhooshGeneralData trainAndWhooshGeneralData);
    public abstract boolean verifyRecentlySearch(TrainAndWhooshGeneralData trainAndWhooshGeneralData);
    public abstract void clearRecentlySearch();
    public abstract void clickRecentlySearch(String trainDateEn,String trainDateId);
    public abstract void openFilter();
    public abstract void filterByClass(TrainClass trainClass);
    public abstract void saveFilter();
    public abstract boolean verifyClass(TrainClass trainClass);
    public abstract boolean verifyName(String trainName);
    public abstract void filterByName(String trainName);
    public abstract void openCalendar();
    public abstract void skipPopup();
    public abstract void openViaDeeplink();
    public abstract List<String> getPrices();
    public abstract boolean VerifyAndClickPartialSellBanner();

    public String selectOriginAndGetCityName(String stationName){
        WhooshSearchPage<T> whooshSearchPage = WhooshSearchPage.getInstance(session);
        return whooshSearchPage.selectOriginAndGetCityName(stationName);
    }
    public String selectDestinationAndGetCityName(String stationName){
        WhooshSearchPage<T> whooshSearchPage = WhooshSearchPage.getInstance(session);
        return whooshSearchPage.selectDestinationAndGetCityName(stationName);
    }

    public int getNumberOfPassengers(String paxInfoText){
        String[] paxParts = paxInfoText.split(" ");
        return Integer.parseInt(paxParts[0].trim());
    }
}
