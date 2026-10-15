package com.tiket.web.dashboard.page.flight;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FlightAddCityFormPage<T extends WebDriver> extends BasePage<T> {

    public FlightAddCityFormPage(Session<T> session) {
        super(session);
    }

    private static final Locator COUNTRY_SELECT_DROPDOWN = new Locator("COUNTRY_SELECT_DROPDOWN", By.xpath("//span[contains(@class,'Select-arrow-zone')]"));
    private static final Locator CITY_CODE_BAR = new Locator("CITY_CODE_BAR", By.xpath("//input[@name='code']"));
    private static final Locator CITY_NAME_BAR = new Locator("CITY_NAME_BAR", By.xpath("//input[@name='name']"));
    private static final Locator LATITUDE_BAR = new Locator("LATITUDE_BAR", By.xpath("//input[@id='latitude']"));
    private static final Locator LONGITUDE_BAR = new Locator("LONGITUDE_BAR", By.xpath("//input[@id='longitude']"));
    private static final Locator TIMEZONE_BAR = new Locator("TIMEZONE_BAR", By.xpath("//input[@name='timezone']"));
    private static final Locator ZONE_INPUT = new Locator("ZONE_INPUT", By.xpath("//input[@name='zone']"));
    private static final Locator SECOND_TIMEZONE_INPUT = new Locator("SECOND_TIMEZONE_INPUT", By.name("timezones[0].timezone"));
    private static final Locator ADD_MORE_TIMEZONE_BTN = new Locator("ADD_MORE_TIMEZONE_BTN", By.cssSelector("[data-testid='add-button']"));
    private static final Locator SUBMIT_BTN = new Locator("SUBMIT_BTN", By.xpath("//button[contains(@class,'general-form__submit') and @type='submit']"));
    private static final Locator DROPDOWN_ITEM = new Locator("DROPDOWN_ITEM", By.xpath("//*[text()='Saint Helena']"));

    public void fillCityDetails(String cityCode, String cityName, String cityLatitude, String cityLongitude, String cityTimezone, String cityZone) {
        selectRandomDropdownOption();
        driverTools.sendKeys(CITY_CODE_BAR, cityCode);
        driverTools.sendKeys(CITY_NAME_BAR, cityName);
        driverTools.sendKeys(LATITUDE_BAR, cityLatitude);
        driverTools.sendKeys(LONGITUDE_BAR, cityLongitude);
        driverTools.sendKeys(TIMEZONE_BAR, cityTimezone);
        driverTools.sendKeys(ZONE_INPUT, cityZone);
        driverTools.sendKeys(SECOND_TIMEZONE_INPUT, cityTimezone);
        driverTools.click(SUBMIT_BTN);
    }

    public void fillCityDetailsWithMultiTimezones(String cityCode, String cityName, String cityLatitude, String cityLongitude,
                                                  String cityTimezone, String cityZone, String secondTimezone) {
        selectRandomDropdownOption();
        driverTools.sendKeys(CITY_CODE_BAR, cityCode);
        driverTools.sendKeys(CITY_NAME_BAR, cityName);
        driverTools.sendKeys(LATITUDE_BAR, cityLatitude);
        driverTools.sendKeys(LONGITUDE_BAR, cityLongitude);
        driverTools.sendKeys(TIMEZONE_BAR, cityTimezone);
        driverTools.sendKeys(ZONE_INPUT, cityZone);
        fillTimezoneEntry(0, cityTimezone);
        driverTools.waitForElementToAppear(ADD_MORE_TIMEZONE_BTN, 5).orElseThrow();
        driverTools.click(ADD_MORE_TIMEZONE_BTN);
        fillTimezoneEntry(1, secondTimezone);
        driverTools.click(SUBMIT_BTN);
    }

    private void fillTimezoneEntry(int index, String timezone) {
        Locator timezoneInput = new Locator("TIMEZONE_ENTRY_" + index, By.name("timezones[" + index + "].timezone"));
        driverTools.waitForElementToAppear(timezoneInput, 5).orElseThrow();
        driverTools.clear(timezoneInput);
        driverTools.sendKeys(timezoneInput, timezone);
    }

    public void selectRandomDropdownOption() {
        driverTools.click(COUNTRY_SELECT_DROPDOWN);
        driverTools.waitForElementToAppear(DROPDOWN_ITEM);
        driverTools.click(DROPDOWN_ITEM);
    }

    public void updateCityDetails(String updatedCityName) {
        WebElement cityNameInput = driverTools.waitForElementToAppear(CITY_NAME_BAR).orElseThrow();
        if (!driverTools.getValue(cityNameInput).equals(updatedCityName)) {
            driverTools.clearInput(cityNameInput);
            driverTools.sendKeys(cityNameInput, updatedCityName);
        }
        logger.log("Updated City Name: " + updatedCityName);
        driverTools.click(SUBMIT_BTN);
    }
}