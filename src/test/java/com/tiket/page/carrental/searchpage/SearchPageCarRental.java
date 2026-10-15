package com.tiket.page.carrental.searchpage;

import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.page.carrental.searchresultpage.SearchResultPageCarRental;
import com.tiket.page.cartransfer.searchpage.CarTransferSearchPage;
import com.tiket.pojo.carrental.CarRentalLandingPageData;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.nft.car_rental.search_Page.CarRentalSearchPageWeb;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class SearchPageCarRental <T extends WebDriver> extends BasePage<T> {
    
    public SearchPageCarRental(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> SearchPageCarRental<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new SearchPageCarRentalAndroid <>(session);}
            case IOS -> {return new SearchPageCarRentalIOS <>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private void switchToWebviewCarRentals(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("/sewa-mobil");
        driverTools.waitForWebToLoad();
    }

    public abstract void backToHomePage();//implementation pending in android
    public void closeInformationPopUp(){
        switchToWebviewCarRentals();
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        carRentalSearchPage.clickSkipButton();
        driverTools.switchToNative();
    }
    public void allowLocationPermission(){
        CarTransferSearchPage<?> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        carTransferSearchPage.allowLocationAccess();
    }
    public void chooseDriver(String driver){
        switchToWebviewCarRentals();
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        carRentalSearchPage.selectDriverType(driver);
        driverTools.switchToNative();
    }
    public void chooseLocation(String location){
        switchToWebviewCarRentals();
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        carRentalSearchPage.selectCarRentalLocationBySearch(location);
        driverTools.switchToNative();
    }

    public void searchCar(String carRentalDuration){
        switchToWebviewCarRentals();
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        carRentalSearchPage.searchCarRental();
        driverTools.switchToNative();

        //set duration via deeplink since we can not set it via UI in mobile web
        SearchResultPageCarRental<T> searchResultPageCarRental = SearchResultPageCarRental.getInstance(session);
        searchResultPageCarRental.changeDurationViaDeeplink(carRentalDuration);
        searchResultPageCarRental.skipNearbyLocationPopup();
    }

    public void searchCar(){
        switchToWebviewCarRentals();
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        carRentalSearchPage.searchCarRental();
        driverTools.switchToNative();
    }

    public CarRentalLandingPageData getSelectedDataLandingPage(){
        switchToWebviewCarRentals();
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalLandingPageData carRentalLandingPageData = carRentalSearchPage.getSelectedData();
        driverTools.switchToNative();
        return carRentalLandingPageData;
    }
    public void selectDate(String date){
        switchToWebviewCarRentals();
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        carRentalSearchPage.selectDate(date);
        driverTools.switchToNative();
    }
    public abstract void swipeToInfoSection();
    public abstract void clickInfoSection(String infoSubSectionType);
    public abstract boolean verifyHowToRentPage(Environment environment);
    public abstract void clickBackFromHowToRent();
    public abstract void swipeToHowToRentVideo();
    public abstract void clickToHowToRentVideo();
    public abstract boolean verifyHowToRentVideoPlayed();
    public abstract void openViaDeeplink();
    public abstract void allowLocationAccess();
    public abstract Boolean isLandingPageValid();
}
