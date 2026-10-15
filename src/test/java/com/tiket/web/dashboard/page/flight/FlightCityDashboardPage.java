package com.tiket.web.dashboard.page.flight;

import com.google.gson.Gson;
import com.tiket.api.flight.dashboard.CitiesApi;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.flight.dashboard.CitiesApiResponse;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

public class FlightCityDashboardPage<T extends WebDriver> extends BasePage<T> {

    public FlightCityDashboardPage(Session<T> session) {
        super(session);
    }

    private static final Locator ADD_CITY_BTN = new Locator("ADD_CITY_BTN", new By.ByXPath("//div[contains(@class, 'custom-table__add-button-container')]"));
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[@class='account-card']"));
    private static final Locator NEW_ADDED_CITY = new Locator("NEW_ADDED_CITY", By.xpath("//tr[contains(@class,'MuiTableRow-root row_value')][1]"));
    private static final Locator VIEW_CITY_BTN = new Locator("VIEW_CITY_BTN", By.xpath("//tr[contains(@class,'MuiTableRow-root row_value')][1]//button[contains(@class,'btn-detail')]"));
    private static final Locator EDIT_CITY_BTN = new Locator("EDIT_CITY_BTN", By.xpath("//tr[contains(@class,'MuiTableRow-root row_value')][1]//button[contains(@class,'btn-warning')]"));
    private static final Locator DELETE_CITY_BTN = new Locator("DELETE_CITY_BTN", By.xpath("//tr[contains(@class,'MuiTableRow-root row_value')][1]//button[contains(@class,'btn-danger')]"));
    private static final Locator CONFIRM_DELETE_BTN = new Locator("CONFIRM_DELETE_BTN", By.xpath("//span[text()='OK']"));
    private static final Locator VIEW_CITY_CODE = new Locator("VIEW_CITY_CODE", By.xpath("//th[h6[text()='City Code']]/following-sibling::th"));
    private static final Locator VIEW_CITY_NAME = new Locator("VIEW_CITY_NAME", By.xpath("//th[h6[text()='City Name']]/following-sibling::th"));
    private static final Locator VIEW_LATITUDE = new Locator("VIEW_LATITUDE", By.xpath("//th[h6[text()='Latitude']]/following-sibling::th"));
    private static final Locator VIEW_LONGITUDE = new Locator("VIEW_LONGITUDE", By.xpath("//th[h6[text()='Longitude']]/following-sibling::th"));
    private static final Locator VIEW_TIMEZONE = new Locator("VIEW_TIMEZONE", By.xpath("//th[h6[text()='Timezone']]/following-sibling::th"));
    private static final Locator CODE_FILTER_INPUT = new Locator("CODE_FILTER_INPUT", By.id("custom-table__filter--code"));
    private static final Locator APPLY_FILTER_BTN = new Locator("APPLY_FILTER_BTN", By.cssSelector("button.custom-table__filter--submit"));
    private static final Locator RESET_FILTER_BTN = new Locator("RESET_FILTER_BTN", By.cssSelector("button.custom-table__filter--reset"));
    private static final Locator DATA_NOT_EXIST = new Locator("DATA_NOT_EXIST", By.xpath("//td[text()='Data Not Exist']"));

    public void navigateToFlightCityDashboard() {
        switch (Configs.ENVIRONMENT) {
            case GK:
                driverTools.get("https://gatotkaca.tiket.com/admin/flight-ms-v2/flight-city");
                break;
            case PREPROD:
                driverTools.get("https://preprod.tiket.com/admin/flight-ms-v2/flight-city");
                break;
            case PROD:
                driverTools.get("https://www.tiket.com/admin/flight-ms-v2/flight-city");
                break;
        }
    }


    public void clickAddCityBtn() {
        try {
            navigateToFlightCityDashboard();
            driverTools.waitForElementToAppear(ADD_CITY_BTN, 5);
            driverTools.click(ADD_CITY_BTN);
            logger.log("Clicked on Add City button successfully.");
        } catch (Exception e) {
            logger.fail("Failed to click on Add City button.");
            throw e;
        }
    }

    public void clickEditCityBtn() {
        try {
            navigateToFlightCityDashboard();
            driverTools.waitForElementToAppear(EDIT_CITY_BTN, 5);
            driverTools.click(EDIT_CITY_BTN);
            logger.log("Clicked on Edit City button successfully.");
        } catch (Exception e) {
            logger.fail("Failed to click on Edit City button.");
            throw e;
        }
    }

    public void clickViewCityBtn() {
        try {
            navigateToFlightCityDashboard();
            driverTools.waitForElementToAppear(VIEW_CITY_BTN, 5);
            driverTools.click(VIEW_CITY_BTN);
            logger.log("Clicked on View City button successfully.");
        } catch (Exception e) {
            logger.fail("Failed to click on View City button.");
            throw e;
        }
    }

    public boolean isFlightLoggedIn() {
        driverTools.waitForElementToAppear(USERNAME_GREETING, 3);
        return driverTools.isElementDisplayed(USERNAME_GREETING);
    }

    public boolean checkAddedCity(String cityCode, String cityName, String cityLatitude, String cityLongitude, String cityTimezone) {
        driverTools.waitForElementToAppear(NEW_ADDED_CITY, 4).orElseThrow();
        String AddedCity = driverTools.getText(NEW_ADDED_CITY).orElseThrow();
        logger.log("Added City: " + AddedCity);
        return AddedCity.contains(cityCode) && AddedCity.contains(cityName) && AddedCity.contains(cityLatitude) && AddedCity.contains(cityLongitude) && AddedCity.contains(cityTimezone);
    }

    public boolean isUpdatedCity(String updatedCityName) {
        driverTools.waitForElementToAppear(NEW_ADDED_CITY, 4).orElseThrow();
        String newCity = driverTools.getText(NEW_ADDED_CITY).orElseThrow();
        logger.log("Updated City Details : " + newCity);
        return newCity.contains(updatedCityName);
    }

    public boolean checkCityDetails(String cityCode, String cityName, String cityLatitude, String cityLongitude, String cityTimezone) {
        String displayedCityCode = driverTools.getText(VIEW_CITY_CODE).orElseThrow();
        String displayedCityName = driverTools.getText(VIEW_CITY_NAME).orElseThrow();
        String displayedLatitude = driverTools.getText(VIEW_LATITUDE).orElseThrow();
        String displayedLongitude = driverTools.getText(VIEW_LONGITUDE).orElseThrow();
        String displayedTimezone = driverTools.getText(VIEW_TIMEZONE).orElseThrow();
        logger.log("View City Details : " + displayedCityCode + " " + displayedCityName + " " + displayedLatitude + " " + displayedLongitude + " " + displayedTimezone);
        logger.log("Expected City Details : " + cityCode + " " + cityName + " " + cityLatitude + " " + cityLongitude + " " + cityTimezone);
        return displayedCityCode.contains(cityCode) && displayedCityName.contains(cityName) && displayedLatitude.contains(cityLatitude) && displayedLongitude.contains(cityLongitude) && displayedTimezone.contains(cityTimezone);
    }

    public boolean checkCityDeleted(String cityCode, String cityName, String cityLatitude, String cityLongitude, String cityTimezone) {
        driverTools.refreshWebpage();
        driverTools.waitForElementToAppear(NEW_ADDED_CITY, 4).orElseThrow();
        String AddedCity = driverTools.getText(NEW_ADDED_CITY).orElseThrow();
        logger.log("Added City: " + AddedCity);
        return !(AddedCity.contains(cityCode) && AddedCity.contains(cityName) && AddedCity.contains(cityLatitude) && AddedCity.contains(cityLongitude) && AddedCity.contains(cityTimezone));
    }

    public void refreshPage() {
        driverTools.refreshWebpage();
    }

    public void deleteCity() {
        driverTools.waitForElementToBeClickable(DELETE_CITY_BTN, 5);
        driverTools.click(DELETE_CITY_BTN);
        driverTools.waitForElementToAppear(CONFIRM_DELETE_BTN);
        driverTools.click(CONFIRM_DELETE_BTN);
    }

    public void deleteCityIfExists(String cityCode) {
        navigateToFlightCityDashboard();
        driverTools.waitForElementToAppear(CODE_FILTER_INPUT, 5).orElseThrow();
        driverTools.clear(CODE_FILTER_INPUT);
        driverTools.sendKeys(CODE_FILTER_INPUT, cityCode);
        driverTools.click(APPLY_FILTER_BTN);

        Locator matchingCityRow = getMatchingCityRowLocator(cityCode);
        if (!driverTools.waitForElementToAppear(matchingCityRow, 5).isPresent()) {
            logger.log("City matching filter '" + cityCode + "' is not present.");
            driverTools.waitForElementToAppear(DATA_NOT_EXIST, 5).orElseThrow();
        } else {
            int maxDeletes = 10;
            while (maxDeletes-- > 0 && driverTools.isElementDisplayed(matchingCityRow)) {
                logger.log("City matching filter '" + cityCode + "' is present. Deleting it.");
                deleteCity(cityCode);
                if (driverTools.waitForElementToAppear(DATA_NOT_EXIST, 3).isPresent()) {
                    break;
                }
            }
        }

        driverTools.waitForElementToAppear(RESET_FILTER_BTN, 5).orElseThrow();
        driverTools.click(RESET_FILTER_BTN);
    }

    public void deleteCity(String cityCode) {
        Locator deleteButton = getDeleteButtonLocatorByCityCode(cityCode);
        driverTools.waitForElementToBeClickable(deleteButton, 5);
        driverTools.click(deleteButton);
        driverTools.waitForElementToAppear(CONFIRM_DELETE_BTN);
        driverTools.click(CONFIRM_DELETE_BTN);
    }

    public String getBrowserCookieHeader() {
        StringBuilder cookieHeader = new StringBuilder();
        for (Cookie cookie : driver.manage().getCookies()) {
            if (!cookieHeader.isEmpty()) {
                cookieHeader.append("; ");
            }
            cookieHeader.append(cookie.getName()).append("=").append(cookie.getValue());
        }
        return cookieHeader.toString();
    }

    public boolean verifyCityStoredWithMultiTimezones(String cityCode, String cityName,
                                                      String firstTimezone, String secondTimezone) {
        CitiesApi api = new CitiesApi(new CitiesApi.Inputs(cityCode, getBrowserCookieHeader()));
        ApiResponse apiResponse = api.hitApi(apiTools);
        CitiesApiResponse response = new Gson().fromJson(apiResponse.getBody(), CitiesApiResponse.class);

        if (response == null || response.data == null || response.data.content == null || response.data.content.isEmpty()) {
            logger.log("Cities API returned no content for cityCode=" + cityCode);
            return false;
        }

        CitiesApiResponse.City city = response.data.content.stream()
                .filter(c -> cityCode.equalsIgnoreCase(c.code))
                .findFirst()
                .orElse(null);
        if (city == null) {
            logger.log("Cities API did not return city with code=" + cityCode);
            return false;
        }

        if (city.timezones == null || city.timezones.size() < 2) {
            logger.log("Cities API city=" + city.code + " does not have multiple timezones: " + city.timezones);
            return false;
        }

        boolean nameMatches = cityName == null || cityName.equalsIgnoreCase(city.name);
        boolean entriesValid = city.timezones.stream().allMatch(tz ->
                tz != null
                        && tz.timezone != null
                        && tz.startDate != null && !tz.startDate.isBlank()
                        && tz.endDate != null && !tz.endDate.isBlank());
        double firstTz = Double.parseDouble(firstTimezone);
        double secondTz = Double.parseDouble(secondTimezone);
        boolean hasFirst = city.timezones.stream().anyMatch(tz -> tz.timezone != null && Double.compare(tz.timezone, firstTz) == 0);
        boolean hasSecond = city.timezones.stream().anyMatch(tz -> tz.timezone != null && Double.compare(tz.timezone, secondTz) == 0);

        logger.log("Cities API city=" + city.code + ", name=" + city.name
                + ", rootTimezone=" + city.timezone
                + ", timezonesCount=" + city.timezones.size()
                + ", timezones=" + new Gson().toJson(city.timezones));
        return nameMatches && entriesValid && hasFirst && hasSecond;
    }

    private Locator getMatchingCityRowLocator(String cityCode) {
        String code = cityCode.toUpperCase();
        return new Locator("MATCHING_CITY_ROW_" + code,
                By.xpath("//tbody[contains(@class,'custom-table__body')]/tr[contains(@class,'row_value')][./td[2][translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='" + code + "']]"));
    }

    private Locator getDeleteButtonLocatorByCityCode(String cityCode) {
        String code = cityCode.toUpperCase();
        return new Locator("DELETE_CITY_" + code,
                By.xpath("//tbody[contains(@class,'custom-table__body')]/tr[contains(@class,'row_value')][./td[2][translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='" + code + "']]/td[last()]//button[contains(@class,'btn-danger')]"));
    }
}