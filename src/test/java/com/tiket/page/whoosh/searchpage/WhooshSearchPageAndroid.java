package com.tiket.page.whoosh.searchpage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;

import static com.tiket.page.train.searchpage.TrainSearchPageAndroid.FIRST_STATION;

public class WhooshSearchPageAndroid <T extends WebDriver> extends WhooshSearchPage<T> {
    public WhooshSearchPageAndroid(Session<T> session) {
        super(session);
    }

    private static final Locator ROUND_TRIP_TOGGLE  = new Locator("ROUND_TRIP_TOGGLE", AppiumBy.xpath("//*[contains(@resource-id, 'tds_toggle_circle')]"));
    private static final Locator SEARCH_BUTTON = new Locator("SearchButton", AppiumBy.xpath("//*[contains(@resource-id, 'tds_button_view')]"));
    private static final Locator SKIP_COACHMARK = new Locator("SKIP_COACHMARK", AppiumBy.xpath("//*[contains(@resource-id, 'tds_link_coach_mark') and @text='Skip' or @text='Lewati']"));
    private static final Locator ORIGIN = new Locator("Origin", AppiumBy.id("sf_origin"));
    private static final Locator DESTINATION = new Locator("Destination", AppiumBy.id("sf_destination"));
    private static final Locator ORIGIN_SEARCH_FIELD= new Locator("ORIGIN_SEARCH_FIELD",AppiumBy.xpath("//*[contains(@resource-id, 'tds_search_et')]"));
    private static final Locator RETURN_DATE_FLAG = new Locator("RETURN_DATE_FLAG", AppiumBy.xpath("//*[contains(@resource-id,'tds_search_form_subtitle') and contains(@text,',')]"));
    private static final Locator FIRST_STATION_CITY_NAME = new Locator("FIRST_STATION_CITY_NAME", AppiumBy.xpath("((//*[contains(@resource-id,'recycler_view')]/android.view.ViewGroup)[1]//*[contains(@resource-id,'tv_subtitle')])[1]"));


    @Override
    public void selectOrigin(String stationName) {
        driverTools.waitForElementToAppear(ORIGIN).orElseThrow();
        driverTools.click(ORIGIN);
        logger.log("Searching station: "+stationName);
        driverTools.sendKeys(ORIGIN_SEARCH_FIELD,stationName);
        driverTools.waitForElementToAppear(selectCityNameStationCode(stationName));
        driverTools.click(selectCityNameStationCode(stationName));
    }

    @Override
    public String selectOriginAndGetCityName(String stationName) {
        driverTools.waitForElementToAppear(ORIGIN).orElseThrow();
        driverTools.click(ORIGIN);
        logger.log("Searching station: "+stationName);
        driverTools.sendKeys(ORIGIN_SEARCH_FIELD,stationName);
        driverTools.waitForElementToAppear(selectCityNameStationCode(stationName));
        WebElement cityNameElement = driverTools.waitForElementToPresence(getCityName(stationName)).orElseThrow();
        String cityName = driverTools.getText(cityNameElement);
        logger.log("City name: "+cityName);
        driverTools.click(selectCityNameStationCode(stationName));
        return CoreFunctions.extractCityName(cityName);
    }

    private Locator selectCityNameStationCode(String stationName) {
        if(stationName.length() == 3){
            return FIRST_STATION;
        }

        String capitalizedStationName = stationName.substring(0, 1).toUpperCase() + stationName.substring(1);
        return new Locator(
                "FIRST_STATION",
                AppiumBy.xpath("//*[contains(@resource-id, 'tv_title') and ( contains(@text,'"+capitalizedStationName+"') or contains(@text,'"+capitalizedStationName.toUpperCase()+"') )]")
        );
    }

    private Locator getCityName(String stationName) {
        if(stationName.length() == 3){
            return FIRST_STATION_CITY_NAME;
        }
        String capitalizedStationName = stationName.substring(0, 1).toUpperCase() + stationName.substring(1);
        return new Locator(
                "FIRST_STATION",
                AppiumBy.xpath("//*[contains(@resource-id, 'tv_title') and ( contains(@text,'"+capitalizedStationName+"') or contains(@text,'"+capitalizedStationName.toUpperCase()+"') )]//following-sibling::*[contains(@resource-id,'tv_subtitle')]")
        );
    }

    @Override
    public void selectDestination(String stationName) {
        driverTools.click(DESTINATION);
        logger.log("Searching station: "+stationName);
        driverTools.sendKeys(ORIGIN_SEARCH_FIELD,stationName);
        driverTools.waitForElementToAppear(selectCityNameStationCode(stationName));
        driverTools.click(selectCityNameStationCode(stationName));
    }
    public void selectDestinationWhoosh(String stationName) {
        driverTools.click(DESTINATION);
        logger.log("Searching station: "+stationName);
        driverTools.sendKeys(ORIGIN_SEARCH_FIELD,stationName);
        driverTools.waitForElementToAppear(selectCityNameStationCode(stationName));
        driverTools.click(selectCityNameStationCode(stationName));
    }


    @Override
    public String selectDestinationAndGetCityName(String stationName) {
        driverTools.click(DESTINATION);
        logger.log("Searching station: "+stationName);
        driverTools.sendKeys(ORIGIN_SEARCH_FIELD,stationName);
        driverTools.waitForElementToAppear(selectCityNameStationCode(stationName));
        WebElement cityNameElement = driverTools.waitForElementToPresence(getCityName(stationName)).orElseThrow();
        String cityName = driverTools.getText(cityNameElement);
        logger.log("City name: "+cityName);
        driverTools.click(selectCityNameStationCode(stationName));
        return CoreFunctions.extractCityName(cityName);
    }

    @Override
    public void selectRoundTripToggle(String tripType) {
        boolean isRoundTripSelected = driverTools.waitForElementToPresence(RETURN_DATE_FLAG).isPresent();

        if (!tripType.equalsIgnoreCase("oneway")) {
            if (isRoundTripSelected) {
                logger.log("Round trip toggle already selected");
            } else {
                driverTools.click(ROUND_TRIP_TOGGLE);
                driverTools.waitForElementToPresence(RETURN_DATE_FLAG).orElseThrow();
                logger.log("Round trip toggle selected");
            }
        } else { // oneway
            if (isRoundTripSelected) {
                driverTools.click(ROUND_TRIP_TOGGLE);
                driverTools.waitForElementToDisappear(RETURN_DATE_FLAG);
                logger.log("Round trip toggle deselected");
            } else {
                logger.log("Round trip toggle already deselected");
            }
        }
    }

    @Override
    public void selectTrainViaIndex(int index){
        driverTools.waitForElementToAppear(getTrainSelectLocatorByIndex(index),10);
        driverTools.click(getTrainSelectLocatorByIndex(index));
    }

    private Locator getTrainSelectLocatorByIndex(int index){
        return new Locator("Train_"+index, AppiumBy.xpath("(//*[@class='android.view.ViewGroup' and ./*[contains(@resource-id, 'tv_price')]])["+index+"]"));
    }

    @Override
    public void searchTrain() {
        driverTools.click(SEARCH_BUTTON);
        driverTools.waitForElementToDisappear(SEARCH_BUTTON);
    }

    @Override
    public void deeplinkToWhooshSrp(String yyyyMMddDate, int adult, int infant, int child) {
        // iOS-only helper used to bypass the iOS calendar quirk; not needed on Android.
    }

    @Override
    public void deeplinkToWhooshSrpRoundTrip(String yyyyMMddDepart, String yyyyMMddReturn, int adult, int infant, int child) {
        // iOS-only helper used to bypass the iOS calendar quirk; not needed on Android.
    }
}
