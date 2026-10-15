package com.tiket.web.dashboard.page.nft.train;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Objects;

import static org.hamcrest.Matchers.is;

public class TrainStationsManagement <T extends WebDriver> extends BasePage<T> {
    private static final Locator TRAIN_STATION_MANAGEMENT_MENU = new Locator("TRAIN_STATION_MANAGEMENT_MENU", new By.ByXPath("//div[@class='menu-list' and contains(text(),'Train Stations Management')]"));
    private static final Locator ADD_NEW_STATION_BUTTON = new Locator("ADD_NEW_STATION_BUTTON", new By.ByXPath("//div[@class='btn-model-1' and contains(text(),'ADD NEW STATION')]"));
    private static final Locator CHOOSE_PRODUCT_INPUT = new Locator("CHOOSE_PRODUCT_INPUT", new By.ByXPath("//div[contains(@class, 'product__control')]"));
    private static final Locator KAI_PRODUCT_AS_INPUT = new Locator("KAI_PRODUCT_AS_INPUT", new By.ByXPath("//div[contains(@class, 'product__option') and contains(text(), 'KAI')]"));
    private static final Locator CHOOSE_TYPE_INPUT = new Locator("CHOOSE_TYPE_INPUT", new By.ByXPath("//div[contains(@class, 'type__control')]"));
    private static final Locator CHOOSE_CITY_INPUT = new Locator("CHOOSE_CITY_INPUT", new By.ByXPath("//div[contains(@class, 'city__control')]"));
    private static final Locator STATION_OR_CITY_NAME_INPUT = new Locator("STATION_OR_CITY_NAME_INPUT", new By.ByXPath("//input[@name='name']"));
    private static final Locator STATION_OR_CITY_CODE_INPUT = new Locator("STATION_OR_CITY_CODE_INPUT", new By.ByXPath("//input[@name='code']"));
    private static final Locator LATITUDE_INPUT = new Locator("LATITUDE_INPUT", new By.ByXPath("//input[@name='latitude']"));
    private static final Locator LONGITUDE_INPUT = new Locator("LONGITUDE_INPUT", new By.ByXPath("//input[@name='longitude']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[@class='button-primary' and @type='submit' and contains(text(), 'SAVE')]"));
    private static final Locator SEARCH_INPUT = new Locator("SEARCH_INPUT", new By.ByXPath("//input[@class='form-control' and @type='text' and @placeholder='Search something here…']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[contains(text(), 'SEARCH')]"));
    private static final Locator EDIT_ICON = new Locator("EDIT_ICON", new By.ByXPath("//div[contains(@class, 'button-edit')]"));
    private static final Locator EDIT_STATION_OR_CITY_NAME_INPUT = new Locator("EDIT_STATION_OR_CITY_NAME_INPUT", new By.ByXPath("//p[contains(text(),'Station/City Name')]/following-sibling::input[@class='form-control']"));
    private static final Locator EDIT_STATION_OR_CITY_CODE_INPUT = new Locator("EDIT_STATION_OR_CITY_CODE_INPUT", new By.ByXPath("//p[contains(text(),'Station/City Code')]/following-sibling::input[@class='form-control']"));
    private static final Locator EDIT_LATITUDE_INPUT = new Locator("EDIT_LATITUDE_INPUT", new By.ByXPath("//p[contains(text(),'Latitude')]/following-sibling::input[@class='form-control']"));
    private static final Locator EDIT_LONGITUDE_INPUT = new Locator("EDIT_LONGITUDE_INPUT", new By.ByXPath("//p[contains(text(),'Longitude')]/following-sibling::input[@class='form-control']"));
    private static final Locator SUCCESS_ADD_BANNER = new Locator("SUCCESS_ADD_BANNER", new By.ByXPath("//p[contains(text(), 'Page succesfully added')]"));
    private static final Locator SUCCESS_EDIT_BANNER = new Locator("SUCCESS_EDIT_BANNER", new By.ByXPath("//p[contains(text(), 'Page succesfully edited')]"));
    private static final Locator ACTIVATE_BUTTON = new Locator("ACTIVATE_BUTTON", new By.ByXPath("//div[@class='button-status' and contains(text(),'Activate')]"));
    private static final Locator POPUP_ACTIVATE_BUTTON = new Locator("POPUP_ACTIVATE_BUTTON", new By.ByXPath("//button[contains(text(),'Yes, activate')]"));
    private static final Locator SUCCESS_ACTIVATE_BANNER = new Locator("SUCCESS_ACTIVATE_BANNER", new By.ByXPath("//p[contains(text(), 'Page Successfully Activated')]"));
    private static final Locator DEACTIVATE_BUTTON = new Locator("DEACTIVATE_BUTTON", new By.ByXPath("//div[@class='button-status' and contains(text(),'Deactivate')]"));
    private static final Locator POPUP_DEACTIVATE_BUTTON = new Locator("POPUP_DEACTIVATE_BUTTON", new By.ByXPath("//button[contains(text(),'Yes, deactivate')]"));
    private static final Locator SUCCESS_DEACTIVATE_BANNER = new Locator("SUCCESS_DEACTIVATE_BANNER", new By.ByXPath("//p[contains(text(), 'Page Successfully Deactivated')]"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", new By.ByXPath("//div[contains(@class,'button-delete')]"));
    private static final Locator POPUP_DELETE_BUTTON = new Locator("POPUP_DELETE_BUTTON", new By.ByXPath("//button[contains(text(),'YES, DELETE')]"));
    private static final Locator SUCCESS_DELETE_BANNER = new Locator("SUCCESS_DELETE_BANNER", new By.ByXPath("//p[contains(text(), 'Page succesfully deleted')]"));
    private static final Locator SUCCESS_SEARCH_STASION_OR_CITY_CODE = new Locator("SUCCESS_SEARCH_STASION_OR_CITY_CODE", new By.ByXPath("//table[@class='table table-striped table-bordered']/tbody/tr/td[3]"));

    public TrainStationsManagement(Session<T> session) {
        super(session);
    }

    public void goToTrainStationsManagementMenu(){
        driverTools.waitForElementToAppear(TRAIN_STATION_MANAGEMENT_MENU);
        driverTools.click(TRAIN_STATION_MANAGEMENT_MENU);
    }

    public void addTrainStation(String stationType, String city, String stationOrCityName, String stationOrCityCode,
                                String latitude, String longitude){
        Locator STATION_TYPE_AS_INPUT = new Locator("CHOOSE_STATION_TYPE_AS_INPUT", new By.ByXPath("//*[contains(@class, '-option') and contains(text(), '" + stationType + "')]"));
        Locator CITY_AS_INPUT = new Locator("CHOOSE_STATION_TYPE_AS_INPUT", new By.ByXPath("//div[contains(text(), '" + city + "')]"));

        driverTools.click(ADD_NEW_STATION_BUTTON);

        driverTools.click(CHOOSE_PRODUCT_INPUT);
        driverTools.click(KAI_PRODUCT_AS_INPUT);

        driverTools.click(CHOOSE_TYPE_INPUT);
        driverTools.click(STATION_TYPE_AS_INPUT);

        driverTools.click(CHOOSE_CITY_INPUT);
        driverTools.click(CITY_AS_INPUT);

        driverTools.sendKeys(STATION_OR_CITY_NAME_INPUT, stationOrCityName);
        driverTools.sendKeys(STATION_OR_CITY_CODE_INPUT, stationOrCityCode);
        driverTools.sendKeys(LATITUDE_INPUT, latitude);
        driverTools.sendKeys(LONGITUDE_INPUT, longitude);

        driverTools.click(SAVE_BUTTON);
    }

    public void searchTrainStation(String stationOrCityCode){
        driverTools.click(SEARCH_INPUT);
        driverTools.sendKeys(SEARCH_INPUT, stationOrCityCode);
        driverTools.click(SEARCH_BUTTON);
    }

    public boolean verifyAddTrainStation(String stationOrCityCode){
        Locator STATION_OR_CITY_CODE = new Locator("STATION_OR_CITY_CODE", new By.ByXPath("//td[contains(text(), '" + stationOrCityCode + "')]"));

        searchTrainStation(stationOrCityCode);
        driverTools.waitForElementToAppear(STATION_OR_CITY_CODE);
        Assertion.assertThat("Station or City Code is displayed", driverTools.isElementDisplayed(STATION_OR_CITY_CODE), is(true));
        Assertion.assertThat("Success Add Banner is displayed", driverTools.isElementDisplayed(SUCCESS_ADD_BANNER), is(true));

        return true;
    }

    public void editTrainStation(String existingStationType, String existingCity, String toBeEditedStationType, String toBeEditedCity,
                                 String toBeEditedStationOrCityName, String toBeEditedStationOrCityCode, String toBeEditedLatitude,
                                 String toBeEditedLongitude){
        Locator EXISTING_STATION_TYPE_AS_INPUT = new Locator("EXISTING_STATION_TYPE_AS_INPUT", new By.ByXPath("//*[contains(@class, '-singleValue') and text()='" + existingStationType + "']"));
        Locator EXISTING_CITY_AS_INPUT = new Locator("EXISTING_CITY_AS_INPUT", new By.ByXPath("//*[contains(@class, '-singleValue') and text()='" + existingCity + "']"));

        driverTools.click(EDIT_ICON);

        driverTools.waitForElementToAppear(EXISTING_STATION_TYPE_AS_INPUT);
        driverTools.click(EXISTING_STATION_TYPE_AS_INPUT);
        WebElement toBeEditedStationTypeElement = driver.switchTo().activeElement();
        toBeEditedStationTypeElement.sendKeys(toBeEditedStationType);
        toBeEditedStationTypeElement.sendKeys(Keys.RETURN);

        if (Objects.equals(toBeEditedStationType, "Station")){
            driverTools.click(EXISTING_CITY_AS_INPUT);
            WebElement toBeEditedCityElement = driver.switchTo().activeElement();
            toBeEditedCityElement.sendKeys(toBeEditedCity);
            toBeEditedCityElement.sendKeys(Keys.RETURN);

            driverTools.click(EDIT_STATION_OR_CITY_NAME_INPUT);
            driverTools.clear(EDIT_STATION_OR_CITY_NAME_INPUT);
            driverTools.sendKeys(STATION_OR_CITY_NAME_INPUT, toBeEditedStationOrCityName);

            driverTools.click(EDIT_STATION_OR_CITY_CODE_INPUT);
            driverTools.clear(EDIT_STATION_OR_CITY_CODE_INPUT);
            driverTools.sendKeys(EDIT_STATION_OR_CITY_CODE_INPUT, toBeEditedStationOrCityCode);

        } else{
            driverTools.click(EDIT_STATION_OR_CITY_NAME_INPUT);
            driverTools.clear(EDIT_STATION_OR_CITY_NAME_INPUT);
            driverTools.sendKeys(STATION_OR_CITY_NAME_INPUT, toBeEditedStationOrCityName);
        }
        driverTools.click(EDIT_LATITUDE_INPUT);
        driverTools.clear(EDIT_LATITUDE_INPUT);
        driverTools.sendKeys(EDIT_LATITUDE_INPUT, toBeEditedLatitude);
        driverTools.click(EDIT_LONGITUDE_INPUT);
        driverTools.clear(EDIT_LONGITUDE_INPUT);
        driverTools.sendKeys(EDIT_LONGITUDE_INPUT, toBeEditedLongitude);

        driverTools.click(SAVE_BUTTON);
    }

    public boolean verifyEditTrainStation(String toBeEditedStationOrCityCode){
        Locator STATION_OR_CITY_CODE = new Locator("STATION_OR_CITY_CODE", new By.ByXPath("//td[contains(text(), '" + toBeEditedStationOrCityCode + "')]"));

        searchTrainStation(toBeEditedStationOrCityCode);
        driverTools.waitForElementToAppear(STATION_OR_CITY_CODE);
        Assertion.assertThat("Station or City Code is displayed", driverTools.isElementDisplayed(STATION_OR_CITY_CODE), is(true));
        Assertion.assertThat("Success Edit Banner is displayed", driverTools.isElementDisplayed(SUCCESS_EDIT_BANNER), is(true));
        return true;
    }

    public void activateTrainStation(){
        driverTools.waitForElementToAppear(ACTIVATE_BUTTON);
        driverTools.click(ACTIVATE_BUTTON);

        driverTools.waitForElementToAppear(POPUP_ACTIVATE_BUTTON);
        driverTools.click(POPUP_ACTIVATE_BUTTON);
    }

    public boolean verifyActivateTrainStation(){
        driverTools.waitForElementToAppear(SUCCESS_ACTIVATE_BANNER);
        return driverTools.isElementDisplayed(SUCCESS_ACTIVATE_BANNER);
    }

    public void deactivateTrainStation(){
        driverTools.waitForElementToAppear(DEACTIVATE_BUTTON);
        driverTools.click(DEACTIVATE_BUTTON);

        driverTools.waitForElementToAppear(POPUP_DEACTIVATE_BUTTON);
        driverTools.click(POPUP_DEACTIVATE_BUTTON);
    }

    public boolean verifyDeactivateTrainStation(){
        driverTools.waitForElementToAppear(SUCCESS_DEACTIVATE_BANNER);
        return driverTools.isElementDisplayed(SUCCESS_DEACTIVATE_BANNER);
    }

    public void deleteTrainStation(){
        driverTools.waitForElementToAppear(DELETE_BUTTON);
        driverTools.click(DELETE_BUTTON);

        driverTools.waitForElementToAppear(POPUP_DELETE_BUTTON);
        driverTools.click(POPUP_DELETE_BUTTON);
    }

    public boolean verifyDeleteTrainStation(String stationOrCityCode){
        Locator STATION_OR_CITY_CODE = new Locator("STATION_OR_CITY_CODE", new By.ByXPath("//td[contains(text(), '" + stationOrCityCode + "')]"));

        driverTools.waitForElementToAppear(SUCCESS_DELETE_BANNER);
        searchTrainStation(stationOrCityCode);
        Assertion.assertThat("Station or City Code is not displayed", driverTools.isElementDisplayed(STATION_OR_CITY_CODE), is(false));
        Assertion.assertThat("Success Delete Banner is displayed", driverTools.isElementDisplayed(SUCCESS_DELETE_BANNER), is(true));
        return true;
    }

    public String verifyStationCodeIsShown(){
        driverTools.waitForElementToAppear(SUCCESS_SEARCH_STASION_OR_CITY_CODE);
        return String.valueOf(driverTools.getText(SUCCESS_SEARCH_STASION_OR_CITY_CODE).get().toLowerCase());
    }

}
