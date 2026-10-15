package com.tiket.page.carrental.searchresultpage;

import com.tiket.model.Session;
import com.tiket.pojo.carrental.CarRentalSrpData;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.nft.car_rental.searchResult_Page.CarRentalSearchResultPageWeb;
import com.tiket.web.page.nft.car_rental.search_Page.CarRentalSearchPageWeb;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class SearchResultPageCarRental  <T extends WebDriver> extends BasePage<T> {

    public SearchResultPageCarRental(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> SearchResultPageCarRental<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new SearchResultPageCarRentalAndroid <>(session);}
            case IOS -> {return new SearchResultPageCarRentalIOS <>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public void switchToWebviewCarRentalsSRP(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("/sewa-mobil");
        driverTools.waitForWebToLoad();
    }

    public String getFirstCarName(){
        switchToWebviewCarRentalsSRP();
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        String carName = carRentalSearchResultPage.getFirstCarName();
        driverTools.switchToNative();
        return carName;
    }

    public void chooseCar(){
        switchToWebviewCarRentalsSRP();
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        carRentalSearchResultPage.selectOnlyFirstCarRentalCard();
        carRentalSearchResultPage.selectFirstCarVendorCard();
        driverTools.switchToNative();
    }
    
    public CarRentalSrpData chooseCar(int duration){
        switchToWebviewCarRentalsSRP();
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        String carName = carRentalSearchResultPage.getFirstCarName();

        carRentalSearchResultPage.selectOnlyFirstCarRentalCard();

        String carVendor = carRentalSearchResultPage.getVendorName();
        String carPrice = carRentalSearchResultPage.getFirstCarRentalPrice();
        logger.log("Selected car vendor: " + carVendor);
        logger.log("Selected car price: " + carPrice);
        logger.log("Selected car name: " + carName);

        carRentalSearchResultPage.selectFirstCarVendorCard();

        // Create SRP data using the new page-specific structure
        CarRentalSrpData srpData = new CarRentalSrpData(carVendor, carName, carPrice,duration);
        driverTools.switchToNative();
        return srpData;
    }

    public abstract void chooseCarWithCarTypeNameAndVendorName(String carType,String vendorName);
    public abstract void chooseTransmissionType(String transmissionType);
    public abstract void changeLocation(String location);
    public abstract void changeDate(String date);
    public abstract String selectRegularTab();
    public abstract String selectAllInTab();
    public abstract boolean packageBanner(String packageTitle);
    public abstract boolean carListContent();
    public abstract boolean carListTransmissionType(String transmissionType);
    public abstract String getPriceCarType();
    public abstract void chooseCarType();
    public abstract void chooseCarVendor();
    public abstract String getPriceCarVendor();
    public abstract String getPricePDP();

    public void changeDurationViaDeeplink(String duration){
        switchToWebviewCarRentalsSRP();
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        carRentalSearchResultPage.selectDurationUsingDeeplink(duration);
        driverTools.switchToNative();
    }

    public void skipNearbyLocationPopup(){
        switchToWebviewCarRentalsSRP();
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        carRentalSearchResultPage.skipNearbyLocationPopup();
        driverTools.switchToNative();
    }

    public boolean verifySRP(String date, String location, String duration, String driverType){
        switchToWebviewCarRentalsSRP();
        CarRentalSearchPageWeb<?> carRentalSearchPageWeb = CarRentalSearchPageWeb.getInstance(session);

        String location_extracted = carRentalSearchPageWeb.getFormValue("location", "SRP");
        String date_extracted = carRentalSearchPageWeb.getFormValue("date", "SRP");
        String duration_extracted = carRentalSearchPageWeb.getFormValue("duration", "SRP");
        String driver_type_extracted = carRentalSearchPageWeb.getFormValue("driverType", "SRP");

        logger.log("Expected location: " + location + ", Extracted location: " + location_extracted);
        logger.log("Expected date: " + date + ", Extracted date: " + date_extracted);
        logger.log("Expected duration: " + duration + ", Extracted duration: " + duration_extracted);
        logger.log("Expected driver type: " + driverType + ", Extracted driver type: " + driver_type_extracted);
        boolean isLocationMatch = location_extracted.equalsIgnoreCase(location);
        boolean isDateMatch = date.contains(date_extracted);
        boolean isDurationMatch = duration_extracted.contains(duration);
        boolean isDriverTypeMatch = driver_type_extracted.equalsIgnoreCase(driverType);

        driverTools.switchToNative();
        return isLocationMatch && isDriverTypeMatch && isDateMatch && isDurationMatch;
    }

    public void clickChangeSearch(String location , String date, String duration, String driverType){
        switchToWebviewCarRentalsSRP();
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        //set date and duration
        carRentalSearchResultPage.changeDriverType(driverType,location,date);
        carRentalSearchResultPage.waitForFirstCar();

        carRentalSearchResultPage.selectDurationUsingDeeplink(duration);
        driverTools.switchToNative();
    }
}
