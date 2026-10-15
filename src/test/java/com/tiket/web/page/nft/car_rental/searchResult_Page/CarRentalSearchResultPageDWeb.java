package com.tiket.web.page.nft.car_rental.searchResult_Page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CarRentalSearchResultPageDWeb <T extends WebDriver> extends CarRentalSearchResultPageWeb<T> {
    private static final Locator LOCATION_FORM_SRP = new Locator("LOCATION_FORM_SRP", AppiumBy.xpath("(//span[contains(@class,'ChangeSearch_pickup_location')])[2]"));
    private static final Locator DATE_FORM_SRP = new Locator("DATE_FORM_SRP", AppiumBy.xpath("(//div[contains(@class,'ChangeSearch_date')]/span)[2]"));
    private static final Locator SET_PICKUP_LOCATION_BUTTON = new Locator("SET_PICKUP_LOCATION_BUTTON", new By.ByXPath("//*[contains(@class,'Modal_modal_footer')]//button"));
    private static final Locator WITH_DRIVER = new Locator("WITH_DRIVER", AppiumBy.xpath("(//div[contains(@class,'DriverOptionSelector_option_card')])[1]"));
    private static final Locator SELF_DRIVE = new Locator("SELF_DRIVE", AppiumBy.xpath("(//div[contains(@class,'DriverOptionSelector_option_card')])[2]"));
    private static final Locator DRIVER_FORM_SRP = new Locator("DRIVER_FORM_SRP", AppiumBy.xpath("(//span[contains(@class,'ChangeSearch_driver_option')])[2]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", AppiumBy.xpath("(//button[contains(@class,'ChangeSearch_search_button')])[2]"));
    private static final Locator LOCATION_INPUT = new Locator("LOCATION_INPUT",AppiumBy.xpath("//input[@type='text' and @autocomplete='off' and (contains(@placeholder, 'city') or contains(@placeholder, 'kota'))]"));

    public CarRentalSearchResultPageDWeb(Session<T> session) {
        super(session);
    }

    @Override
    public void editSearchResult(String location, String newDate) {
        driverTools.waitForElementToAppear(LOCATION_FORM_SRP).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(LOCATION_INPUT).ifPresent(input -> {
            driverTools.clear(input);
            driverTools.sendKeys(input, location);
        });
        Locator LOCATION_TEXT = getLocationTextLocator(location);
        driverTools.waitForElementToAppear(LOCATION_TEXT);
        driverTools.clickWithJavascriptExecutor(LOCATION_TEXT);
        driverTools.waitForElementToAppear(SET_PICKUP_LOCATION_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(DATE_FORM_SRP).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(getDateLocator(newDate)).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(SEARCH_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public void editTotalDaysQueryParam(String queryParam, String newValue) {

        logger.log("On DWEB, no need to change the value of " + queryParam + " to " + newValue);
    }

    @Override
    public void changeDriverType(String driverType, String location, String newDate) {
        driverTools.waitForElementToAppear(DRIVER_FORM_SRP, 3).ifPresent(driverTools::clickWithJavascriptExecutor);
        if (driverType.equals("self-drive")) {
            driverTools.waitForElementToAppear(SELF_DRIVE).ifPresent(driverTools::clickWithJavascriptExecutor);
        } else if (driverType.equals("with-driver")) {
            driverTools.waitForElementToAppear(WITH_DRIVER).ifPresent(driverTools::clickWithJavascriptExecutor);
        }
        driverTools.waitForElementToAbsence(SELF_DRIVE, 3);
        driverTools.waitForElementToAbsence(WITH_DRIVER, 3);
        driverTools.waitForElementToAppear(DATE_FORM_SRP).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(getDateLocator(newDate)).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAbsence(DATE_FORM_SRP, 3);
        driverTools.waitForElementToAbsence(getDateLocator(newDate), 3);
        driverTools.waitForElementToAppear(LOCATION_INPUT).ifPresent(input -> {
            driverTools.clear(input);
            driverTools.sendKeys(input, location);
        });
        Locator LOCATION_TEXT = getLocationTextLocator(location);
        driverTools.waitForElementToAppear(LOCATION_TEXT).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(SET_PICKUP_LOCATION_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(SEARCH_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
        if (driverTools.waitForElementToAppear(SELF_DRIVE, 5).isPresent()) {
            logger.log("Self drive option found");
        }
        if (driverTools.waitForElementToAppear(WITH_DRIVER, 5).isPresent()) {
            logger.log("With driver option found");
        }
    }
}
