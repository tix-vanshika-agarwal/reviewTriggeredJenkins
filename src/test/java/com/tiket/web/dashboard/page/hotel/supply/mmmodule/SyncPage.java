package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SyncPage<T extends WebDriver> extends BasePage<T> {
    public SyncPage(Session<T> session) {
        super(session);
    }

    private static final Locator LOCATION_POLYGON_FILTER_SECTION = new Locator("LOCATION_POLYGON_FILTER_SECTION", new By.ByXPath("//span[normalize-space()='Sync All Hotels After Polygon Changes in Location Service']"));
    private static final Locator LOCATION_POLYGON_SEARCH_TYPE_INPUT = new Locator("LOCATION_POLYGON_SEARCH_TYPE_INPUT", new By.ByName("searchType"));
    private static final Locator TMDF_LOCATION_COUNTRY_INPUT = new Locator("TMDF_LOCATION_COUNTRY_INPUT", new By.ByXPath("//input[@id='country']"));
    private static final Locator TMDF_LOCATION_COUNTRY_DROPDOWN_BUTTON = new Locator("TMDF_LOCATION_COUNTRY_DROPDOWN_BUTTON", new By.ByXPath("//input[@id='country']/following-sibling::div//button"));
    private static final Locator TMDF_LOCATION_REGION_INPUT = new Locator("TMDF_LOCATION_REGION_INPUT", new By.ByXPath("//input[@id='region']"));
    private static final Locator TMDF_LOCATION_CITY_INPUT = new Locator("TMDF_LOCATION_CITY_INPUT", new By.ByXPath("//input[@id='city']"));
    private static final Locator TMDF_LOCATION_AREA_INPUT = new Locator("TMDF_LOCATION_AREA_INPUT", new By.ByXPath("//input[@id='area']"));
    private static final Locator LOCATION_POLYGON_SYNC_BUTTON = new Locator("LOCATION_POLYGON_SYNC_BUTTON", new By.ByXPath("//button[@testid=\"submit-sync-btn\"]"));
    private static final Locator NEARBY_TITLE_LABEL = new Locator("NEARBY_TITLE_LABEL", new By.ByXPath("//span[contains(@class,'h3')][text()='Sync Nearby']"));
    private static final Locator NEARBY_SEARCH_TYPE_INPUT = new Locator("NEARBY_SEARCH_TYPE_INPUT", new By.ByName("searchType"));
    private static final Locator NEARBY_COUNTRY_INPUT = new Locator("NEARBY_COUNTRY_INPUT", new By.ByName("country"));
    private static final Locator NEARBY_REGION_INPUT = new Locator("NEARBY_REGION_INPUT", new By.ByName("region"));
    private static final Locator NEARBY_CITY_INPUT = new Locator("NEARBY_CITY_INPUT", new By.ByName("city"));
    private static final Locator NEARBY_AREA_INPUT = new Locator("NEARBY_AREA_INPUT", new By.ByName("area"));
    private static final Locator NEARBY_SYNC_BUTTON = new Locator("NEARBY_SYNC_BUTTON", new By.ByXPath("//button[contains(@class,'primary')][text()='SYNC']"));
    private static final Locator WEIGHT_UPDATE_FILTER_SECTION = new Locator("WEIGHT_UPDATE_FILTER_SECTION", new By.ById("update-location-weight"));
    private static final Locator WEIGHT_UPDATE_TYPE_INPUT = new Locator("WEIGHT_UPDATE_TYPE_INPUT", new By.ByName("weightType"));
    private static final Locator WEIGHT_UPDATE_LOCATION_INPUT = new Locator("WEIGHT_UPDATE_LOCATION_INPUT", new By.ByXPath("//div[@class=\"tmdf-location-select\"]//input"));
    private static final Locator WEIGHT_UPDATE_VALUE_INPUT = new Locator("WEIGHT_UPDATE_VALUE_INPUT", new By.ByName("weightValue"));
    private static final Locator WEIGHT_UPDATE_SYNC_BUTTON = new Locator("WEIGHT_UPDATE_SYNC_BUTTON", new By.ByXPath("//button[@data-testid=\"submit-update-location-weight-btn\"]"));
    private static final Locator HOTEL_CONTENT_FILTER_SECTION = new Locator("HOTEL_CONTENT_FILTER_SECTION", new By.ByXPath("//span[contains(text(),'Sync Hotel Content from Vendors')]"));
    private static final Locator HOTEL_CONTENT_VENDOR_NAME_INPUT = new Locator("HOTEL_CONTENT_VENDOR_NAME_INPUT", new By.ByName("vendorName"));
    private static final Locator HOTEL_CONTENT_VENDOR_HOTEL_ID_INPUT = new Locator("HOTEL_CONTENT_VENDOR_HOTEL_ID_INPUT", new By.ById("vendors-hotel-id"));
    private static final Locator HOTEL_CONTENT_SYNC_BUTTON = new Locator("HOTEL_CONTENT_SYNC_BUTTON", new By.ByXPath("//button[@type='submit' and normalize-space()='Sync']"));
    private static final Locator MAIN_PHOTO_FILTER_SECTION = new Locator("MAIN_PHOTO_FILTER_SECTION", new By.ById("sync-image-from-vendor"));
    private static final Locator MAIN_PHOTO_LOCATION_TYPE_INPUT = new Locator("MAIN_PHOTO_LOCATION_TYPE_INPUT", new By.ByXPath("//input[@id='search-type' and @value='All']/following::button[1]"));
    private static final Locator MAIN_PHOTO_SYNC_BUTTON = new Locator("MAIN_PHOTO_SYNC_BUTTON", new By.ByXPath("//button[@data-testid=\"sync-main-img-btn\"]"));
    private static final Locator AURORA_SYNC_TITLE_LABEL = new Locator("AURORA_SYNC_TITLE_LABEL", new By.ByXPath("//div[@class='w-full']//span[text()='Aurora']"));
    private static final Locator SUBMIT_SYNC_BUTTON = new Locator("SUBMIT_SYNC_BUTTON", new By.ByXPath("//button[contains(normalize-space(),'SYNC')]"));
    private static final Locator NO_OF_HOTEL_PER_LOCATION_SYNC_TITLE_LABEL = new Locator("NO_OF_HOTEL_PER_LOCATION_SYNC_TITLE_LABEL", new By.ByXPath("//span[contains(text(),'Sync Number Hotels Per Location')]"));
    private static final Locator SUCCESSFUL_TOAST = new Locator("SUCCESSFUL_TOAST", new By.ByXPath("//div[contains(@class,'Snackbar_text_content')]"));

    private static Locator getLocationPolygonFilterSearchTypeDropdownList(String text) {
        String value = switch (text) {
            case "OFFICIAL" -> "Official District";
            case "FREE" -> "Free District";
            default -> "";
        };
        return new Locator("LOCATION_POLYGON_SEARCH_TYPE_DROPDOWN_LIST", new By.ByXPath(String.format("//li[@data-name='searchType' and @data-label='%s']", value)));
    }

    private static Locator getTmdfLocationFilterCountryDropdownList(String text) {
        return new Locator("TMDF_LOCATION_COUNTRY_DROPDOWN_LIST", new By.ByXPath(String.format("//li[@data-name='country' and @data-label='%s']", text)));
    }

    private static Locator getTmdfLocationFilterRegionDropdownList(String text) {
        return new Locator("TMDF_LOCATION_REGION_DROPDOWN_LIST", new By.ByXPath(String.format("//li[@data-name='region' and @data-label='%s']", text)));
    }

    private static Locator getTmdfLocationFilterCityDropdownList(String text) {
        return new Locator("TMDF_LOCATION_CITY_DROPDOWN_LIST", new By.ByXPath(String.format("//li[@data-name='city' and @data-label='%s']", text)));
    }
    
    private static Locator getTmdfLocationFilterAreaDropdownList(String text) {
        return new Locator("TMDF_LOCATION_AREA_DROPDOWN_LIST", new By.ByXPath(String.format("//li[@data-name='area' and @data-label='%s']", text)));
    }

    private static Locator getNearbyFilterSearchTypeDropdownList(String text) {
        String value = switch (text) {
            case "OFFICIAL" -> "Official District";
            case "FREE" -> "Free District";
            default -> "";
        };
        return new Locator("NEARBY_SEARCH_TYPE_DROPDOWN_LIST", new By.ByXPath(String.format("//li[@data-name=\"searchType\"][text()='%s']", value)));
    }

    private static Locator getNearbyFilterCountryDropdownList(String text) {
        return new Locator("NEARBY_COUNTRY_DROPDOWN_LIST", new By.ByXPath(String.format("//li[@data-name=\"country\"][text()='%s']", text)));
    }

    private static Locator getNearbyFilterRegionDropdownList(String text) {
        return new Locator("NEARBY_REGION_DROPDOWN_LIST", new By.ByXPath(String.format("//li[@data-name=\"region\"][text()='%s']", text)));
    }

    private static Locator getNearbyFilterCityDropdownList(String text) {
        return new Locator("NEARBY_CITY_DROPDOWN_LIST", new By.ByXPath(String.format("//li[@data-name=\"city\"][text()='%s']", text)));
    }

    private static Locator getNearbyFilterAreaDropdownList(String text) {
        return new Locator("NEARBY_AREA_DROPDOWN_LIST", new By.ByXPath(String.format("//li[@data-name=\"area\"][text()='%s']", text)));
    }

    private static Locator getWeightUpdateTypeDropdownList(String text) {
        return new Locator("WEIGHT_UPDATE_TYPE_DROPDOWN_LIST", new By.ByXPath(String.format("//div[@id=\"weightType\"]//label[text()='%s']/parent::span[contains(@class,\"popoverlist-list\")]", text)));
    }

    private static Locator getWeightUpdateLocationDropdownList(String text) {
        return new Locator("WEIGHT_UPDATE_LOCATION_DROPDOWN_LIST", new By.ByXPath(String.format("//div[@class=\"tmdf-location-select\"]//label[text()='%s']/parent::span[contains(@class,\"popoverlist-list\")]", text)));
    }

    private static Locator getWeightUpdateValueDropdownList(String text) {
        String value = switch (text) {
            case "1000" -> "1,000";
            case "10000" -> "10,000";
            case "100000" -> "100,000";
            default -> "";
        };
        return new Locator("WEIGHT_UPDATE_VALUE_DROPDOWN_LIST", new By.ByXPath(String.format("//div[@id=\"weightValue\"]//label[text()='%s']/parent::span[contains(@class,\"popoverlist-list\")]", value)));
    }

    private static Locator getHotelContentVendorNameDropdownList(String text) {
        return new Locator("HOTEL_CONTENT_VENDOR_NAME_DROPDOWN_LIST", new By.ByXPath(String.format("//li[@data-name=\"vendorName\" and @data-label='%s']", text.toUpperCase())));
    }

    private static Locator getMainPhotoFilterLocationTypeDropdownList(String text) {
        return new Locator("MAIN_PHOTO_LOCATION_TYPE_DROPDOWN_LIST", new By.ByXPath(String.format("//li[@data-name='searchType' and @data-label='%s']", text)));
    }

    public boolean isSyncLocationPolygonPageDisplayed() {
        WebElement section = driverTools.waitForElementToPresence(LOCATION_POLYGON_FILTER_SECTION, 10).orElseThrow();
        return driverTools.isElementDisplayed(section);
    }

    public void selectLocationPolygonFilterSearchType(String text) {
        driverTools.click(LOCATION_POLYGON_SEARCH_TYPE_INPUT);
        driverTools.clickWithJavascriptExecutor(getLocationPolygonFilterSearchTypeDropdownList(text));
    }

    public void selectLocationPolygonFilterCountry(String text) {
        driverTools.click(TMDF_LOCATION_COUNTRY_INPUT);
        driverTools.sendKeys(TMDF_LOCATION_COUNTRY_INPUT, text);
        driverTools.clickWithJavascriptExecutor(getTmdfLocationFilterCountryDropdownList(text));
    }

    public void selectLocationPolygonFilterRegion(String text) {
        driverTools.click(TMDF_LOCATION_REGION_INPUT);
        driverTools.sendKeys(TMDF_LOCATION_REGION_INPUT, text);
        driverTools.clickWithJavascriptExecutor(getTmdfLocationFilterRegionDropdownList(text));
    }

    public void selectLocationPolygonFilterCity(String text) {
        driverTools.click(TMDF_LOCATION_CITY_INPUT);
        driverTools.sendKeys(TMDF_LOCATION_CITY_INPUT, text);
        driverTools.clickWithJavascriptExecutor(getTmdfLocationFilterCityDropdownList(text));
    }

    public void selectLocationPolygonFilterArea(String text) {
        driverTools.click(TMDF_LOCATION_AREA_INPUT);
        driverTools.sendKeys(TMDF_LOCATION_AREA_INPUT, text);
        driverTools.clickWithJavascriptExecutor(getTmdfLocationFilterAreaDropdownList(text));
    }

    public void clickLocationPolygonSyncButton() {
        WebElement button = driverTools.waitForElementToBeClickable(SUBMIT_SYNC_BUTTON, 10).orElseThrow();
        driverTools.click(button);
    }

    public boolean isSuccessfulSnackbarDisplayed() {
        WebElement snackbar = driverTools.waitForElementToAppear(SUCCESSFUL_TOAST, 10).orElseThrow();
        return driverTools.isElementDisplayed(snackbar);
    }

    public boolean isSyncNearbyPageDisplayed() {
        WebElement title = driverTools.waitForElementToPresence(NEARBY_TITLE_LABEL, 10).orElseThrow();
        return driverTools.isElementDisplayed(title);
    }

    public void selectNearbyFilterSearchType(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(NEARBY_SEARCH_TYPE_INPUT, 10).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getNearbyFilterSearchTypeDropdownList(text),10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(dropdown);
    }

    public void selectNearbyFilterCountry(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(NEARBY_COUNTRY_INPUT, 10).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getNearbyFilterCountryDropdownList(text), 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(dropdown);
    }

    public void selectNearbyFilterRegion(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(NEARBY_REGION_INPUT, 10).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getNearbyFilterRegionDropdownList(text), 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(dropdown);
    }

    public void selectNearbyFilterCity(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(NEARBY_CITY_INPUT, 10).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getNearbyFilterCityDropdownList(text), 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(dropdown);
    }

    public void selectNearbyFilterArea(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(NEARBY_AREA_INPUT, 10).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getNearbyFilterAreaDropdownList(text), 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(dropdown);
    }

    public void clickNearbySyncButton() {
        WebElement button = driverTools.waitForElementToBeClickable(NEARBY_SYNC_BUTTON, 10).orElseThrow();
        driverTools.click(button);
    }

    public boolean isSuccessfulToastDisplayed() {
        WebElement toast = driverTools.waitForElementToAppear(SUCCESSFUL_TOAST, 10).orElseThrow();
        return driverTools.isElementDisplayed(toast);
    }

    public boolean isSyncWeightUpdatePageDisplayed() {
        WebElement section = driverTools.waitForElementToPresence(WEIGHT_UPDATE_FILTER_SECTION, 10).orElseThrow();
        return driverTools.isElementDisplayed(section);
    }

    public void selectWeightUpdateType(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(WEIGHT_UPDATE_TYPE_INPUT, 10).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getWeightUpdateTypeDropdownList(text)).orElseThrow();
        driverTools.click(dropdown);
    }

    public void selectWeightUpdateLocation(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(WEIGHT_UPDATE_LOCATION_INPUT, 10).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getWeightUpdateLocationDropdownList(text)).orElseThrow();
        driverTools.click(dropdown);
    }

    public void selectWeightUpdateValue(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(WEIGHT_UPDATE_VALUE_INPUT, 10).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getWeightUpdateValueDropdownList(text)).orElseThrow();
        driverTools.click(dropdown);
    }

    public void clickWeightUpdateSyncButton() {
        WebElement button = driverTools.waitForElementToBeClickable(WEIGHT_UPDATE_SYNC_BUTTON, 10).orElseThrow();
        driverTools.click(button);
    }

    public boolean isSyncHotelContentPageDisplayed() {
        WebElement section = driverTools.waitForElementToPresence(HOTEL_CONTENT_FILTER_SECTION, 10).orElseThrow();
        return driverTools.isElementDisplayed(section);
    }

    public void selectHotelContentVendorName(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(HOTEL_CONTENT_VENDOR_NAME_INPUT, 10).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getHotelContentVendorNameDropdownList(text)).orElseThrow();
        driverTools.click(dropdown);
    }

    public void enterHotelContentVendorHotelId(String text) {
        WebElement input = driverTools.waitForElementToPresence(HOTEL_CONTENT_VENDOR_HOTEL_ID_INPUT, 10).orElseThrow();
        driverTools.sendKeys(input, text);
    }

    public void clickHotelContentSyncButton() {
        WebElement button = driverTools.waitForElementToBeClickable(HOTEL_CONTENT_SYNC_BUTTON, 10).orElseThrow();
        driverTools.click(button);
    }

    public boolean isSyncMainPhotoPageDisplayed() {
        WebElement section = driverTools.waitForElementToPresence(MAIN_PHOTO_LOCATION_TYPE_INPUT, 10).orElseThrow();
        return driverTools.isElementDisplayed(section);
    }

    public void selectMainPhotoFilterLocationType(String text) {
        driverTools.clickWhenPresent(MAIN_PHOTO_LOCATION_TYPE_INPUT,10);
        driverTools.clickWithJavascriptExecutor(getMainPhotoFilterLocationTypeDropdownList(text));
    }

    public void selectMainPhotoFilterCountry(String text) {
        driverTools.click(TMDF_LOCATION_COUNTRY_INPUT);
        driverTools.sendKeys(TMDF_LOCATION_COUNTRY_INPUT, text);
        driverTools.click(getTmdfLocationFilterCountryDropdownList(text));
    }

    public void selectMainPhotoFilterRegion(String text) {
        driverTools.click(TMDF_LOCATION_REGION_INPUT);
        driverTools.sendKeys(TMDF_LOCATION_REGION_INPUT, text);
        driverTools.click(getTmdfLocationFilterRegionDropdownList(text));
    }

    public void selectMainPhotoFilterCity(String text) {
        driverTools.click(TMDF_LOCATION_CITY_INPUT);
        driverTools.sendKeys(TMDF_LOCATION_CITY_INPUT, text);
        driverTools.click(getTmdfLocationFilterCityDropdownList(text));
    }

    public void selectMainPhotoFilterArea(String text) {
        driverTools.click(TMDF_LOCATION_AREA_INPUT);
        driverTools.sendKeys(TMDF_LOCATION_AREA_INPUT, text);
        driverTools.click(getTmdfLocationFilterAreaDropdownList(text));
    }

    public void clickMainPhotoSyncButton() {
        WebElement button = driverTools.waitForElementToBeClickable(SUBMIT_SYNC_BUTTON, 10).orElseThrow();
        driverTools.click(button);
    }

    public boolean isSyncAuroraPageDisplayed() {
        WebElement title = driverTools.waitForElementToPresence(AURORA_SYNC_TITLE_LABEL, 10).orElseThrow();
        return driverTools.isElementDisplayed(title);
    }

    public void selectAuroraFilterCountry(String text) {
        driverTools.click(TMDF_LOCATION_COUNTRY_INPUT);
        driverTools.sendKeys(TMDF_LOCATION_COUNTRY_INPUT, text);
        driverTools.click(getTmdfLocationFilterCountryDropdownList(text));
    }

    public void selectAuroraFilterRegion(String text) {
        driverTools.click(TMDF_LOCATION_REGION_INPUT);
        driverTools.sendKeys(TMDF_LOCATION_REGION_INPUT, text);
        driverTools.click(getTmdfLocationFilterRegionDropdownList(text));
    }

    public void selectAuroraFilterCity(String text) {
        driverTools.click(TMDF_LOCATION_CITY_INPUT);
        driverTools.sendKeys(TMDF_LOCATION_CITY_INPUT, text);
        driverTools.click(getTmdfLocationFilterCityDropdownList(text));
    }

    public void selectAuroraFilterArea(String text) {
        driverTools.click(TMDF_LOCATION_AREA_INPUT);
        driverTools.sendKeys(TMDF_LOCATION_AREA_INPUT, text);
        driverTools.click(getTmdfLocationFilterAreaDropdownList(text));
    }

    public void clickAuroraSyncButton() {
        WebElement button = driverTools.waitForElementToBeClickable(SUBMIT_SYNC_BUTTON, 10).orElseThrow();
        driverTools.click(button);
    }

    public boolean isSyncNoOfHotelPerLocationPageDisplayed() {
        WebElement title = driverTools.waitForElementToPresence(NO_OF_HOTEL_PER_LOCATION_SYNC_TITLE_LABEL,10).orElseThrow();
        return driverTools.isElementDisplayed(title);
    }

    public void selectNoOfHotelPerLocationFilterCountry(String text) {
        driverTools.waitForElementToBeClickable(TMDF_LOCATION_COUNTRY_DROPDOWN_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(TMDF_LOCATION_COUNTRY_DROPDOWN_BUTTON);

        driverTools.waitForElementToBeClickable(getTmdfLocationFilterCountryDropdownList(text), 10).orElseThrow();

        driverTools.click(getTmdfLocationFilterCountryDropdownList(text));
    }
    public void selectNoOfHotelPerLocationFilterRegion(String text) {
        driverTools.waitForElementToBeClickable(TMDF_LOCATION_REGION_INPUT, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(TMDF_LOCATION_REGION_INPUT);
        driverTools.sendKeys(TMDF_LOCATION_REGION_INPUT, text);

        driverTools.waitForElementToBeClickable(getTmdfLocationFilterRegionDropdownList(text), 10);
        driverTools.click(getTmdfLocationFilterRegionDropdownList(text));
    }

    public void selectNoOfHotelPerLocationFilterCity(String text) {
        driverTools.waitForElementToBeClickable(TMDF_LOCATION_CITY_INPUT, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(TMDF_LOCATION_CITY_INPUT);
        driverTools.sendKeys(TMDF_LOCATION_CITY_INPUT, text);

        driverTools.waitForElementToBeClickable(getTmdfLocationFilterCityDropdownList(text), 10);
        driverTools.click(getTmdfLocationFilterCityDropdownList(text));
    }

    public void selectNoOfHotelPerLocationFilterArea(String text) {
        driverTools.waitForElementToBeClickable(TMDF_LOCATION_AREA_INPUT, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(TMDF_LOCATION_AREA_INPUT);
        driverTools.sendKeys(TMDF_LOCATION_AREA_INPUT, text);

        driverTools.waitForElementToBeClickable(getTmdfLocationFilterAreaDropdownList(text), 10);
        driverTools.click(getTmdfLocationFilterAreaDropdownList(text));
    }

    public void clickNoOfHotelPerLocationSyncButton() {
        WebElement button = driverTools.waitForElementToBeClickable(SUBMIT_SYNC_BUTTON, 10).orElseThrow();
        driverTools.click(button);
    }
}
