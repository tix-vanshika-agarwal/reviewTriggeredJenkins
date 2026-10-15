package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.google.gson.Gson;
import com.tiket.api.accommodation.hotel.supply.HotelChangeNameListenerApi;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.accommodation.supply.ActivateHotelListenerV2Response;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class HotelLibraryPage <T extends WebDriver> extends BasePage<T> {
    public HotelLibraryPage(Session<T> session) {
        super(session);
    }

    private static final Locator HOTEL_LIBRARY_TITLE = new Locator("HOTEL_LIBRARY_TITLE", new By.ByXPath("//div[@class='w-full']//span[contains(text(),'Hotel Library')]"));
    private static final Locator SEARCH_TYPE_INPUT = new Locator("SEARCH_TYPE_INPUT", new By.ByName("searchType"));
    private static final Locator OFFICIAL_DISTRICT_SEARCH_TYPE_OPTION = new Locator("OFFICIAL_DISTRICT_SEARCH_TYPE_OPTION", new By.ByXPath("//li[@data-name='searchType' and @data-label='Official District']"));
    private static final Locator FREE_DISTRICT_SEARCH_TYPE_OPTION = new Locator("FREE_DISTRICT_SEARCH_TYPE_OPTION", new By.ByXPath("//li[@data-name='searchType' and @data-label='Free District']"));
    private static final Locator COUNTRY_INPUT = new Locator("COUNTRY_INPUT", new By.ByXPath("//div[@id='countryId']//input"));
    private static final Locator REGION_INPUT = new Locator("REGION_INPUT", new By.ByXPath("(//div[@class=\"tmdf-location-select\"])[2]//input"));
    private static final Locator CITY_INPUT = new Locator("CITY_INPUT", new By.ByXPath("(//div[@class=\"tmdf-location-select\"])[3]//input"));
    private static final Locator AREA_INPUT = new Locator("AREA_INPUT", new By.ByXPath("(//div[@class=\"tmdf-location-select\"])[4]//input"));
    private static final Locator CHAIN_INPUT = new Locator("CHAIN_INPUT", new By.ByXPath("(//div[@class=\"tmdf-location-select\"])[last()]//input"));
    private static final Locator STATUS_INPUT = new Locator("STATUS_INPUT", new By.ByName("isActive"));
    private static final Locator SELECT_FREE_DISTRICT_INPUT = new Locator("SELECT_FREE_DISTRICT_INPUT", new By.ByName("freeDistrictId"));
    private static final Locator PROPERTY_NAME_INPUT = new Locator("PROPERTY_NAME_INPUT", new By.ByXPath("//input[@id='enter-property-name']"));
    private static final Locator SEARCH_BY_VENDOR_ID_TAB = new Locator("SEARCH_BY_VENDOR_ID_TAB", new By.ByXPath("//*[text()='Vendor ID']//parent::button"));
    private static final Locator VENDOR_NAME_INPUT = new Locator("VENDOR_NAME_INPUT", new By.ByName("vendorName"));
    private static final Locator VENDOR_ID_INPUT = new Locator("VENDOR_ID_INPUT", new By.ByXPath("//input[@id='enter-vendor-id']"));
    private static final Locator VENDOR_EXTERNAL_ID_INPUT = new Locator("VENDOR_EXTERNAL_ID_INPUT", new By.ByName("vendorExternalId"));
    private static final Locator VENDOR_EXTERNAL_ID_CLEAR_ICON = new Locator("VENDOR_EXTERNAL_ID_CLEAR_ICON", new By.ByXPath("//div[@id='vendorExternalId']//div[@class='btn-area']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[@type='submit']"));
    private static final Locator SEARCH_RESULT_HOTEL_NAME_LABEL = new Locator("SEARCH_RESULT_HOTEL_NAME_LABEL", new By.ByXPath("//table/tbody/tr/td/a[contains(@class,'text-B400')]"));
    private static final Locator SEARCH_RESULT_VENDOR_TABLE = new Locator("SEARCH_RESULT_VENDOR_TABLE", new By.ByXPath("(//div[contains(@class,'dropdown')])[last()]"));
    private static final Locator SEARCH_RESULT_CHAIN_LABEL = new Locator("SEARCH_RESULT_CHAIN_LABEL", new By.ByXPath("//table/tbody/tr/td[@class='table_td'][2]"));
    private static final Locator SEARCH_RESULT_REGION_LABEL = new Locator("SEARCH_RESULT_REGION_LABEL", new By.ByXPath("//table/tbody/tr/td[@class='table_td'][3]"));
    private static final Locator SEARCH_RESULT_CITY_LABEL = new Locator("SEARCH_RESULT_CITY_LABEL", new By.ByXPath("//table/tbody/tr/td[@class='table_td'][4]"));
    private static final Locator SEARCH_RESULT_CITY_LABEL_FIRST_ROW = new Locator("SEARCH_RESULT_CITY_LABEL_FIRST_ROW", new By.ByXPath("//table/tbody/tr[1]/td[@class='table_td'][4]"));
    private static final Locator SEARCH_RESULT_AREA_LABEL = new Locator("SEARCH_RESULT_AREA_LABEL", new By.ByXPath("//table/tbody/tr/td[@class='table_td'][5]"));
    private static final Locator SEARCH_RESULT_HOTEL_STATUS_TOGGLE = new Locator("SEARCH_RESULT_HOTEL_STATUS_TOGGLE", new By.ByXPath("//table/tbody/tr/td[@class='table_td'][8]//input[@type=\"checkbox\"]"));
    private static final Locator HOTEL_STATUS_DEACTIVATE_REASON_TEMPORARILY_CLOSED = new Locator("HOTEL_STATUS_DEACTIVATE_REASON_TEMPORARILY_CLOSED", new By.ByXPath("//span[normalize-space(text())='Temporarily closed']/ancestor::label[contains(@class,'Radio_radio')]"));
    private static final Locator HOTEL_STATUS_DEACTIVATE_BUTTON = new Locator("HOTEL_STATUS_DEACTIVATE_BUTTON", new By.ByXPath("(//button[@type='submit'][contains(@class,'Button_variant_primary')])[2]"));
    private static final Locator HOTEL_STATUS_SNACKBAR_TEXT = new Locator("HOTEL_STATUS_SNACKBAR_TEXT", new By.ByXPath("//div[contains(@class,'Snackbar_snackbar')]//div[contains(@class,'Snackbar_text_content')]"));
    private static final Locator ACTION_BUTTON = new Locator("ACTION_BUTTON", new By.ById("action"));
    private static final Locator EDIT_PROPERTY_BUTTON = new Locator("EDIT_PROPERTY_BUTTON", new By.ByXPath("//span[contains(@class,'pl-3') and (text()='Edit Properti' or text()='Edit Property')]"));
    private static final Locator PROPERTY_GALLERY_BUTTON = new Locator("PROPERTY_GALLERY_BUTTON", new By.ByXPath("//div[contains(@class,'dropdown')]//span[text()='Gallery']"));
    private static final Locator UNMERGE_PROPERTY_BUTTON = new Locator("UNMERGE_PROPERTY_BUTTON", new By.ByXPath("//div[contains(@class,'dropdown')]//span[text()='Unmerge']"));
    private static final Locator EDIT_ROOM = new Locator("EDIT_ROOM", new By.ByXPath("//div[contains(@class,'dropdown')]//span[text()='Edit Room']"));
    private static final Locator UNMERGE_PROPERTY_MODAL = new Locator("UNMERGE_PROPERTY_MODAL", new By.ByXPath("//div[contains(@class,'modal')]"));
    private static final Locator UNMERGE_ALL_RADIO_BUTTON_OPTION = new Locator("UNMERGE_ALL_RADIO_BUTTON_OPTION", new By.ByXPath("(//div[contains(@class,'modal')]//input[@name='unmerge-type'])[1]"));
    private static final Locator UNMERGE_CONTINUE_BUTTON = new Locator("UNMERGE_CONTINUE_BUTTON", new By.ByXPath("//div[contains(@class,'modal')]//button[@type='submit'][contains(normalize-space(.),'Continue')]"));
    private static final Locator UNMERGE_REASON_NAME_DROPDOWN_FIELD = new Locator("UNMERGE_REASON_NAME_DROPDOWN_FIELD", new By.ByXPath("//span[normalize-space()='Name']/following-sibling::div//input"));
    private static final Locator UNMERGE_REASON_ADDRESS_DROPDOWN_FIELD = new Locator("UNMERGE_REASON_ADDRESS_DROPDOWN_FIELD", new By.ByXPath("//span[normalize-space()='Address']/following-sibling::div//input"));
    private static final Locator UNMERGE_REASON_LONGLAT_DROPDOWN_FIELD = new Locator("UNMERGE_REASON_LONGLAT_DROPDOWN_FIELD", new By.ByXPath("//span[normalize-space()='Long Lat']/following-sibling::div//input"));
    private static final Locator UNMERGE_REASON_STAR_RATING_DROPDOWN_FIELD = new Locator("UNMERGE_REASON_STAR_RATING_DROPDOWN_FIELD", new By.ByXPath("//span[normalize-space()='Star']/following-sibling::div//input"));
    private static final Locator UNMERGE_REASON_OTHERS_INPUT = new Locator("UNMERGE_REASON_OTHERS_INPUT", new By.ByXPath("//input[@name='other']"));
    private static final Locator UNMERGE_SUBMIT_BUTTON = new Locator("UNMERGE_SUBMIT_BUTTON", new By.ByXPath("//button[text()='Unmerge']"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", new By.ByXPath("(//div[contains(@class,'hotel-lib-v2__popover-action')]//li)[last()]"));
    private static final Locator DELETE_PROPERTY_MODAL = new Locator("DELETE_PROPERTY_MODAL", new By.ByXPath("//div[@class=\"modal-popup\"]"));
    private static final Locator CONFIRM_DELETE_PROPERTY_BUTTON = new Locator("CONFIRM_DELETE_PROPERTY_BUTTON", new By.ByXPath("(//div[@class=\"modal-footer\"]//button)[last()]"));
    private static final Locator NO_RESULT_RESET_FILTER_BUTTON = new Locator("NO_RESULT_RESET_FILTER_BUTTON", new By.ByXPath("//div[@class=\"footer\"]/button[text()='Reset Filter']"));
    private static final Locator LOADING_INDICATOR_ICON = new Locator("LOADING_INDICATOR_ICON", new By.ByXPath("//div[@class='tix-loading-indicator v4']"));
    private static final Locator SNACKBAR = new Locator("SNACKBAR", new By.ByXPath("//div[contains(@class,'snackbars')]//div[contains(@class,'text')][contains(text(),'success')]"));
    private static final Locator TABLE_HEADINGS = new Locator("TABLE_HEADINGS", By.xpath("//div[contains(@class,'hotel-library-table')]//table//thead//tr/th"));
    private static final Locator MANAGE_ROOM_GROUPS_BUTTON = new Locator("MANAGE_ROOM_GROUPS_BUTTON", new By.ByXPath("//a[contains(@class,'cursor-pointer')]/span[text()='Manage Room Groups' or text()='Manage Room Group']"));
    private static Locator getCountryOption(String countryName) {
        return new Locator("COUNTRY_OPTION", new By.ByXPath(String.format("//div[contains(@class,'the-popoverlist')]//label[text()='%s']/parent::span", countryName)));
    }

    private static Locator getRegionOption(String text) {
        return new Locator("REGION_OPTION", new By.ByXPath(String.format("(//div[@class=\"tmdf-location-select\"])[2]//div[@class=\"the-popoverlist\"]//label[text()='%s']/parent::span", text)));
    }

    private static Locator getCityOption(String text) {
        return new Locator("CITY_OPTION", new By.ByXPath(String.format("(//div[@class=\"tmdf-location-select\"])[3]//div[@class=\"the-popoverlist\"]//label[text()='%s']/parent::span", text)));
    }

    private static Locator getAreaOption(String text) {
        return new Locator("AREA_OPTION", new By.ByXPath(String.format("(//div[@class=\"tmdf-location-select\"])[4]//div[@class=\"the-popoverlist\"]//label[text()='%s']/parent::span", text)));
    }

    private static Locator getChainOption(String text) {
        return new Locator("CHAIN_OPTION", new By.ByXPath(String.format("(//div[@class=\"tmdf-location-select\"])[5]//div[@class=\"the-popoverlist\"]//label[text()='%s']/parent::span", text)));
    }

    private static Locator getStatusOption(boolean isStatusActive) {
        if (isStatusActive) {
            return new Locator("", new By.ByXPath("//input[@name=\"isActive\"]//ancestor::div[contains(@class,\"the-dropdown\")]//div[@class=\"the-popoverlist\"]//label[(text()='Active') or (text()='Aktif')]/parent::span"));
        } else {
            return new Locator("", new By.ByXPath("//input[@name=\"isActive\"]//ancestor::div[contains(@class,\"the-dropdown\")]//div[@class=\"the-popoverlist\"]//label[(text()='Inactive') or (text()='Nonaktif')]/parent::span"));
        }
    }

    private static Locator getFreeDistrictOption(String freeDistrictName) {
        return new Locator("FREE_DISTRICT_OPTION", new By.ByXPath(String.format("//div[contains(@class,'the-popoverlist')]//label[text()='%s']/parent::span", freeDistrictName)));
    }

    private static final Locator VENDOR_NAME_LIST = new Locator("VENDOR_NAME_LIST", new By.ByXPath("//li[@data-name='vendorName']/ancestor::div[contains(@class,'overflow-y-auto')]"));

    private static Locator getVendorNameOption(String vendorName) {
        return new Locator("VENDOR_NAME_OPTION", new By.ByXPath(String.format("//li[@data-name='vendorName' and @data-label='%s']", vendorName)));
    }

    private static Locator getSearchResultVendorToggleLabelByIndex(int index) {
        return new Locator("SEARCH_RESULT_VENDOR_TOGGLE_LABEL", new By.ByXPath(String.format("(//div[@class='hotel-lib-v2__vendor-toggle']/span)[%d]", index)));
    }

    private static Locator getUnmergeReasonDropdownOption(String reason) {
        return new Locator("UNMERGE_REASON_DROPDOWN_OPTION", new By.ByXPath(String.format("//div[@data-testid='dropdown-item' and @aria-label='%s']", reason)));
    }

    private static Locator getHotelStatusToggleLocator(String propertyName) {
        return new Locator("RESULT_HOTEL_STATUS_TOGGLE", new By.ByXPath(
            String.format("//tr[.//td[normalize-space()='%s']]//input[@aria-label='toggle-hotel-status']/parent::label", propertyName)));
    }

    private static Locator getHotelStatusToggleStateLocator(String propertyName) {
        return new Locator("RESULT_HOTEL_STATUS_TOGGLE_STATE", new By.ByXPath(
            String.format("//tr[.//td[normalize-space()='%s']]//input[@aria-label='toggle-hotel-status']", propertyName)));
    }

    private static Locator getAlternativeAllotmentToggleLocator(String propertyName) {
        return new Locator("RESULT_ALTERNATIVE_ALLOTMENT_TOGGLE", new By.ByXPath(
            String.format("//tr[.//td[normalize-space()='%s']]//input[contains(@aria-label,'toggle-alternative-allotment')]/parent::label", propertyName)));
    }

    private static Locator getAlternativeAllotmentToggleStateLocator(String propertyName) {
        return new Locator("RESULT_ALTERNATIVE_ALLOTMENT_TOGGLE_STATE", new By.ByXPath(
            String.format("//tr[.//td[normalize-space()='%s']]//input[contains(@aria-label,'toggle-alternative-allotment')]", propertyName)));
    }

    private static Locator getVendorStatusToggleLocator(String propertyName) {
        return new Locator("RESULT_VENDOR_STATUS_TOGGLE", new By.ByXPath(
            String.format("//tr[.//td[normalize-space()='%s']]//input[@aria-label='toggle-vendor-status']/parent::label", propertyName)));
    }

    private static Locator getVendorStatusToggleStateLocator(String propertyName) {
        return new Locator("RESULT_VENDOR_STATUS_TOGGLE_STATE", new By.ByXPath(
            String.format("//tr[.//td[normalize-space()='%s']]//input[@aria-label='toggle-vendor-status']", propertyName)));
    }

    public boolean isHotelLibraryPageLoaded() {
        WebElement hotelLibraryTitle = driverTools.waitForElementToAppear(HOTEL_LIBRARY_TITLE).orElseThrow();
        return driverTools.isElementDisplayed(hotelLibraryTitle);
    }

    public void clickSearchByVendorId() {
        WebElement searchByVendorIdTab = driverTools.waitForElementToAppear(SEARCH_BY_VENDOR_ID_TAB).orElseThrow();
        driverTools.click(searchByVendorIdTab);
    }

    public void selectVendorName(String vendorName) {
        WebElement vendorNameInput = driverTools.waitForElementToPresence(VENDOR_NAME_INPUT).orElseThrow();
        driverTools.click(vendorNameInput);
        WebElement container = driverTools.waitForElementToAppear(VENDOR_NAME_LIST).orElseThrow();
        driverTools.scrollInsideElement(container);
        WebElement option = driverTools.waitForElementToAppear(getVendorNameOption(vendorName)).orElseThrow();
        driverTools.scrollIntoWebView(option);
        driverTools.clickWithJavascriptExecutor(option);
    }

    public boolean isVendorNameSelected(String vendorName) {
        WebElement vendorNameInput = driverTools.waitForElementToPresence(VENDOR_NAME_INPUT).orElseThrow();
        return driverTools.getValueAttribute(vendorNameInput, "value").equalsIgnoreCase(vendorName);
    }

    public void inputVendorID(String hotelId) {
        WebElement vendorIdInput = driverTools.waitForElementToPresence(VENDOR_ID_INPUT).orElseThrow();
        driverTools.sendKeys(vendorIdInput, hotelId);
    }

    public void inputVendorExternalId(String hotelId) {
        WebElement vendorExternalIdInput = driverTools.waitForElementToPresence(VENDOR_EXTERNAL_ID_INPUT).orElseThrow();
        driverTools.clearInput(vendorExternalIdInput);
        driverTools.sendKeys(vendorExternalIdInput, hotelId);
    }

    public void clearVendorExternalIdInput() {
        WebElement vendorExternalIdClearIcon = driverTools.waitForElementToPresence(VENDOR_EXTERNAL_ID_CLEAR_ICON).orElseThrow();
        driverTools.click(vendorExternalIdClearIcon);
    }

    public void selectSearchType(String option) {
        WebElement searchTypeInput = driverTools.waitForElementToPresence(SEARCH_TYPE_INPUT).orElseThrow();
        driverTools.clickWithJavascriptExecutor(searchTypeInput);
        if(option.equals("official")) {
            WebElement officialDistrictSearchTypeOption = driverTools.waitForElementToBeClickable(OFFICIAL_DISTRICT_SEARCH_TYPE_OPTION).orElseThrow();
            driverTools.clickWithJavascriptExecutor(officialDistrictSearchTypeOption);
        } else if (option.equals("free")) {
            WebElement freeDistrictSearchTypeOption = driverTools.waitForElementToBeClickable(FREE_DISTRICT_SEARCH_TYPE_OPTION).orElseThrow();
            driverTools.clickWithJavascriptExecutor(freeDistrictSearchTypeOption);
        }
    }

    public boolean isSearchTypeSelected(String option) {
        boolean result = false;
        WebElement searchTypeInput = driverTools.waitForElementToPresence(SEARCH_TYPE_INPUT).orElseThrow();
        String value = driverTools.getValueAttribute(searchTypeInput, "value");
        if (option.equals("official")) {
            result = value.contains("Official District");
        } else if (option.equals("free")) {
            result = value.contains("Free District");
        }
        return result;
    }

    public void searchCountry(String countryName) {
        WebElement countryInput = driverTools.waitForElementToPresence(COUNTRY_INPUT).orElseThrow();
        driverTools.click(countryInput);
        driverTools.sendKeys(countryInput, countryName);
    }

    public boolean isCountryDisplayed(String countryName) {
        WebElement countryOption = driverTools.waitForElementToBeClickable(getCountryOption(countryName)).orElseThrow();
        return driverTools.isElementDisplayed(countryOption);
    }

    public void searchAndSelectCountry(String countryName) {
        WebElement countryInput = driverTools.waitForElementToBeClickable(COUNTRY_INPUT).orElseThrow();
        driverTools.click(countryInput);
        driverTools.sendKeys(countryInput, countryName);
        WebElement countryOption = driverTools.waitForElementToBeClickable(getCountryOption(countryName), 10).orElseThrow();
        driverTools.click(countryOption);
    }

    public void searchAndSelectCity(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(CITY_INPUT, 10).orElseThrow();
        driverTools.click(input);
        driverTools.sendKeys(input, text);
        WebElement option = driverTools.waitForElementToBeClickable(getCityOption(text), 10).orElseThrow();
        driverTools.click(option);
    }

    public void searchAndSelectArea(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(AREA_INPUT, 10).orElseThrow();
        driverTools.click(input);
        driverTools.sendKeys(input, text);
        WebElement option = driverTools.waitForElementToBeClickable(getAreaOption(text),10).orElseThrow();
        driverTools.click(option);
    }

    public boolean isCountrySelected(String countryName) {
        WebElement countryInput = driverTools.waitForElementToPresence(COUNTRY_INPUT).orElseThrow();
        return driverTools.getValueAttribute(countryInput, "value").contains(countryName);
    }

    public void searchAndSelectRegion(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(REGION_INPUT).orElseThrow();
        driverTools.click(input);
        driverTools.sendKeys(input, text);
        WebElement option = driverTools.waitForElementToAppear(getRegionOption(text)).orElseThrow();
        driverTools.click(option);
    }

    public boolean isRegionSelected(String text) {
        WebElement input = driverTools.waitForElementToAppear(REGION_INPUT).orElseThrow();
        return driverTools.getValueAttribute(input, "value").contains(text);
    }

    public boolean isCitySelected(String text) {
        WebElement input = driverTools.waitForElementToAppear(CITY_INPUT).orElseThrow();
        return driverTools.getValueAttribute(input, "value").contains(text);
    }

    public boolean isAreaSelected(String text) {
        WebElement input = driverTools.waitForElementToAppear(AREA_INPUT).orElseThrow();
        return driverTools.getValueAttribute(input, "value").contains(text);
    }

    public void searchAndSelectChain(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(CHAIN_INPUT).orElseThrow();
        driverTools.click(input);
        driverTools.sendKeys(input, text);
        WebElement option = driverTools.waitForElementToAppear(getChainOption(text)).orElseThrow();
        driverTools.click(option);
    }

    public boolean isChainSelected(String text) {
        WebElement input = driverTools.waitForElementToAppear(CHAIN_INPUT).orElseThrow();
        return driverTools.getValueAttribute(input, "value").contains(text);
    }

    public void searchAndSelectFreeDistrict(String freeDistrict) {
        WebElement freeDistrictInput = driverTools.waitForElementToBeClickable(SELECT_FREE_DISTRICT_INPUT).orElseThrow();
        driverTools.click(freeDistrictInput);
        driverTools.sendKeys(freeDistrictInput, freeDistrict);
        try {
            WebElement freeDistrictOption = driverTools.waitForElementToAppear(getFreeDistrictOption(freeDistrict), 3).orElseThrow();
            driverTools.click(freeDistrictOption);
        } catch (Exception e) {
            WebElement freeDistrictOption = driverTools.waitForElementToBeClickable(getFreeDistrictOption(freeDistrict), 3).orElseThrow();
            driverTools.click(freeDistrictOption);
        }
    }

    public boolean isFreeDistrictSelected(String freeDistrict) {
        WebElement freeDistrictInput = driverTools.waitForElementToBeClickable(SELECT_FREE_DISTRICT_INPUT).orElseThrow();
        return driverTools.getValueAttribute(freeDistrictInput, "value").contains(freeDistrict);
    }

    public void inputPropertyName(String propertyName) {
        WebElement propertyNameInput = driverTools.waitForElementToAppear(PROPERTY_NAME_INPUT).orElseThrow();
        driverTools.sendKeys(propertyNameInput, propertyName);

        driverTools.dummyWait(5);
    }

    public void selectFilterHotelStatus(boolean isStatusActive) {
        WebElement input = driverTools.waitForElementToBeClickable(STATUS_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement option = driverTools.waitForElementToAppear(getStatusOption(isStatusActive)).orElseThrow();
        driverTools.click(option);
    }

    public boolean isHotelStatusSelected(boolean isStatusActive) {
        WebElement input = driverTools.waitForElementToAppear(STATUS_INPUT).orElseThrow();
        if (isStatusActive) {
            return driverTools.getValueAttribute(input, "value").contains("Active") || driverTools.getValueAttribute(input, "value").contains("Aktif");
        } else {
            return driverTools.getValueAttribute(input, "value").contains("Inactive") || driverTools.getValueAttribute(input, "value").contains("Nonaktif");
        }
    }

    public void clickSearch() {
        WebElement searchButton = driverTools.waitForElementToPresence(SEARCH_BUTTON).orElseThrow();
        try {
            driverTools.click(searchButton);
        } catch (ElementClickInterceptedException elementClickInterceptedException) {
            driverTools.clickWithJavascriptExecutor(searchButton);
        }
        waitForSearchResultsToLoad();
        driverTools.dummyWait(2);
    }

    private void waitForSearchResultsToLoad() {
        driverTools.waitForElementToDisappear(LOADING_INDICATOR_ICON, 60);
        driverTools.waitForElementToAppear(SEARCH_RESULT_HOTEL_NAME_LABEL, 30).orElseThrow();
    }

    public boolean isSearchResultHotelNameLabelDisplayed() {
        waitForSearchResultsToLoad();
        return driverTools.isElementDisplayed(SEARCH_RESULT_HOTEL_NAME_LABEL);
    }

    public boolean isSearchResultHotelNameLabelDisplayed(String hotelName) {
        waitForSearchResultsToLoad();
        return driverTools.getText(SEARCH_RESULT_HOTEL_NAME_LABEL)
                .map(text -> text.contains(hotelName))
                .orElse(false);
    }

    public boolean isPropertyNameDisplayedOnSearchResult(String hotelName) {
        waitForSearchResultsToLoad();
        String text = driverTools.getText(SEARCH_RESULT_HOTEL_NAME_LABEL).orElseThrow();
        return text.equals(hotelName);
    }

    public boolean isSearchResultRegionMatched(String text) {
        List<WebElement> labelList = driverTools.waitForElementsToAppear(SEARCH_RESULT_REGION_LABEL).orElseThrow();
        boolean result = false;
        for (int i=0; i<labelList.size(); i++) {
            result = driverTools.getText(labelList.get(i)).equals(text);
        }
        return result;
    }

    public boolean isSearchResultCityMatched(String text) {
        List<WebElement> labelList = driverTools.waitForElementsToAppear(SEARCH_RESULT_CITY_LABEL).orElseThrow();
        boolean result = false;
        for (int i=0; i<labelList.size(); i++) {
            result = driverTools.getText(labelList.get(i)).equals(text);
        }
        return result;
    }

    public boolean isSearchResultAreaMatched(String text) {
        List<WebElement> labelList = driverTools.waitForElementsToAppear(SEARCH_RESULT_AREA_LABEL).orElseThrow();
        List<String> labels = new ArrayList<>();
        for (int i=0; i<labelList.size(); i++) {
            labels.add(labelList.get(i).getText());
        }
        for (String label : labels) {
            if (label.contains(text)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSearchResultChainMatched(String text) {
        List<WebElement> labelList = driverTools.waitForElementsToAppear(SEARCH_RESULT_CHAIN_LABEL).orElseThrow();
        List<String> labels = new ArrayList<>();
        for (int i=0; i<labelList.size(); i++) {
            labels.add(labelList.get(i).getText());
        }
        for (String label : labels) {
            if (label.equals(text)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSearchResultCityLabelDisplayed(String city) {
        WebElement searchResultCityLabel = driverTools.waitForElementToPresence(SEARCH_RESULT_CITY_LABEL_FIRST_ROW).orElseThrow();
        return driverTools.getText(searchResultCityLabel).contains(city);
    }

    public boolean isSearchResultHotelStatusMatched(boolean isStatusActive) {
        WebElement toggle = driverTools.waitForElementToPresence(SEARCH_RESULT_HOTEL_STATUS_TOGGLE).orElseThrow();
        String c;
        boolean isChecked = false;
        boolean result = false;
        try {
            c = driverTools.getValueAttribute(toggle, "checked");
            if (c.equals(null)) {
                isChecked = false;
            } else {
                isChecked = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isChecked = false;
        }
        if (isChecked == isStatusActive) {
            result = true;
        }
        return result;
    }

    public boolean isSearchResultVendorToggleLabelDisplayed(int index, String vendorName, String vendorExternalId) {
        WebElement searchResultVendorToggleLabel = driverTools.waitForElementToPresence(getSearchResultVendorToggleLabelByIndex(index)).orElseThrow();
        String label = driverTools.getText(searchResultVendorToggleLabel);
        return label.contains(vendorName.toUpperCase()) && label.contains(vendorExternalId);
    }

    public boolean isSearchResultNotFound() {
        return driverTools.waitForElementToAppear(NO_RESULT_RESET_FILTER_BUTTON).isPresent();
    }
  
    public void clickAction() {
        WebElement actionButton = driverTools.waitForElementToPresence(ACTION_BUTTON).orElseThrow();
        driverTools.scrollIntoWebView(actionButton);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(actionButton);
    }

    public void clickEditProperty() {
        WebElement actionButton = driverTools.waitForElementToAppear(EDIT_PROPERTY_BUTTON).orElseThrow();
        driverTools.scrollIntoWebView(actionButton);
        driverTools.clickWithJavascriptExecutor(actionButton);
    }

    public void clickPropertyGallery() {
        WebElement propertyGallerySubmenu = driverTools.waitForElementToAppear(PROPERTY_GALLERY_BUTTON).orElseThrow();
        driverTools.scrollIntoWebView(propertyGallerySubmenu);
        driverTools.clickWithJavascriptExecutor(propertyGallerySubmenu);
    }

    public void clickManageRoomGroups() {
        WebElement manageRoomGroupsSubmenu = driverTools.waitForElementToAppear(MANAGE_ROOM_GROUPS_BUTTON).orElseThrow();
        driverTools.scrollIntoWebView(manageRoomGroupsSubmenu);
        driverTools.clickWithJavascriptExecutor(manageRoomGroupsSubmenu);
    }

    public void clickUnmerge() {
        driverTools.scrollToBottomOfTable(SEARCH_RESULT_VENDOR_TABLE);
        WebElement unmergePropertySubmenu = driverTools.waitForElementToPresence(UNMERGE_PROPERTY_BUTTON).orElseThrow();
        driverTools.click(unmergePropertySubmenu);
    }

    public void clickEditRoom() {
        driverTools.scrollToBottomOfTable(SEARCH_RESULT_VENDOR_TABLE);
        WebElement editRoomSubmenu = driverTools.waitForElementToPresence(EDIT_ROOM).orElseThrow();
        driverTools.click(editRoomSubmenu);
    }

    public boolean isUnmergePropertyModalAppeared() {
        WebElement unmergePropertyModal = driverTools.waitForElementToAppear(UNMERGE_PROPERTY_MODAL).orElseThrow();
        return driverTools.isElementDisplayed(unmergePropertyModal);
    }

    public void selectUnmergeAllPropertyRadioButton() {
        WebElement unmergeAll = driverTools.waitForElementToPresence(UNMERGE_ALL_RADIO_BUTTON_OPTION).orElseThrow();
        driverTools.clickWithJavascriptExecutor(unmergeAll);
    }

    public void clickContinueUnmerge() {
        WebElement continueUnmergeButton = driverTools.waitForElementToPresence(UNMERGE_CONTINUE_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(continueUnmergeButton);
    }

    public void selectUnmergeReasonName(String reason) {
        WebElement dropdown = driverTools.waitForElementToPresence(UNMERGE_REASON_NAME_DROPDOWN_FIELD).orElseThrow();
        driverTools.click(dropdown);
        WebElement option = driverTools.waitForElementToPresence(getUnmergeReasonDropdownOption(reason)).orElseThrow();
        driverTools.click(option);
    }

    public void selectUnmergeReasonAddress(String reason) {
        WebElement dropdown = driverTools.waitForElementToPresence(UNMERGE_REASON_ADDRESS_DROPDOWN_FIELD).orElseThrow();
        driverTools.click(dropdown);
        WebElement option = driverTools.waitForElementToPresence(getUnmergeReasonDropdownOption(reason)).orElseThrow();
        driverTools.click(option);
    }

    public void selectUnmergeReasonLongLat(String reason) {
        WebElement dropdown = driverTools.waitForElementToPresence(UNMERGE_REASON_LONGLAT_DROPDOWN_FIELD).orElseThrow();
        driverTools.click(dropdown);
        WebElement option = driverTools.waitForElementToPresence(getUnmergeReasonDropdownOption(reason)).orElseThrow();
        driverTools.click(option);
    }

    public void selectUnmergeReasonStarRating(String reason) {
        WebElement dropdown = driverTools.waitForElementToPresence(UNMERGE_REASON_STAR_RATING_DROPDOWN_FIELD).orElseThrow();
        driverTools.click(dropdown);
        WebElement option = driverTools.waitForElementToPresence(getUnmergeReasonDropdownOption(reason)).orElseThrow();
        driverTools.click(option);
    }

    public void inputOtherUnmergeReason(String text) {
        WebElement otherReasonInput = driverTools.waitForElementToPresence(UNMERGE_REASON_OTHERS_INPUT).orElseThrow();
        driverTools.scrollAndClick(otherReasonInput);
        driverTools.sendKeys(otherReasonInput, text);
    }

    public void clickUnmergeSubmit() {
        WebElement unmergeSubmitButton = driverTools.waitForElementToPresence(UNMERGE_SUBMIT_BUTTON).orElseThrow();
        driverTools.click(unmergeSubmitButton);
    }

    public void deleteProperty() {
        WebElement button = driverTools.waitForElementToBeClickable(DELETE_BUTTON).orElseThrow();
        driverTools.click(button);
        WebElement modal = driverTools.waitForElementToAppear(DELETE_PROPERTY_MODAL).orElseThrow();
        WebElement confirmButton = driverTools.waitForElementToBeClickable(CONFIRM_DELETE_PROPERTY_BUTTON).orElseThrow();
        driverTools.click(confirmButton);
    }

    public boolean isLoadingIndicatorPresent() {
        WebElement loading = driverTools.waitForElementToPresence(LOADING_INDICATOR_ICON).orElseThrow();
        return driverTools.isElementDisplayed(loading);
    }

    public boolean isSnackbarAppeared() {
        return driverTools.waitForElementToAppear(SNACKBAR, 60).isPresent();
    }

    public void clickResultHotelStatusToggle(String propertyName) {
        Locator locator = getHotelStatusToggleLocator(propertyName);
        driverTools.waitForElementToAppear(locator);
        driverTools.scrollElementToCenter(locator);
        driverTools.click(locator);
    }

    public boolean isOnToggleState(String propertyName) {
        Locator locator = new Locator("ON_TOGGLE_STATE", new By.ByXPath(
                String.format("//tr[.//td[normalize-space()='%s']]//input[@aria-label='toggle-hotel-status' and @aria-checked='true']", propertyName)));
        driverTools.waitForElementToPresence(locator, 2);
        return driverTools.isElementPresent(locator);
    }

    public boolean isOffToggleState(String propertyName) {
        Locator locator = new Locator("OFF_TOGGLE_STATE", new By.ByXPath(
                String.format("//tr[.//td[normalize-space()='%s']]//input[@aria-label='toggle-hotel-status' and @aria-checked='false']", propertyName)));
        driverTools.waitForElementToAppear(locator, 2);
        return driverTools.isElementPresent(locator);
    }

    public boolean isResultHotelStatusToggleOn(String propertyName) {
        WebElement toggleState = driverTools.waitForElementToPresence(getHotelStatusToggleStateLocator(propertyName)).orElseThrow();
        return "true".equals(driverTools.getValueAttribute(toggleState, "aria-checked"));
    }

    public void selectHotelStatusDeactivateReasonTemporarilyClosed() {
        driverTools.waitForElementToPresence(HOTEL_STATUS_DEACTIVATE_REASON_TEMPORARILY_CLOSED).ifPresent(reason -> {
            driverTools.scrollIntoWebView(reason);
            driverTools.clickWithJavascriptExecutor(reason);
        });
        driverTools.dummyWait(2);

    }

    public void clickDeactivateHotelStatus() {
        driverTools.waitForElementToAppear(HOTEL_STATUS_DEACTIVATE_BUTTON);
        driverTools.click(HOTEL_STATUS_DEACTIVATE_BUTTON);
    }

    public String getHotelStatusSnackbarText() {
        WebElement snackbar = driverTools.waitForElementToAppear(HOTEL_STATUS_SNACKBAR_TEXT, 30).orElseThrow();
        return driverTools.getText(snackbar);
    }

    public void clickResultShowAlternativeAllotmentToggle(String propertyName) {
        Locator locator = getAlternativeAllotmentToggleLocator(propertyName);
        driverTools.waitForElementToAppear(locator);
        driverTools.scrollElementToCenter(locator);
        driverTools.click(locator);
    }

    public boolean isResultShowAlternativeAllotmentToggleOn(String propertyName) {
        WebElement toggleState = driverTools.waitForElementToPresence(getAlternativeAllotmentToggleStateLocator(propertyName)).orElseThrow();
        return "true".equals(driverTools.getValueAttribute(toggleState, "aria-checked"));
    }

    public void clickResultVendorStatusToggle(String propertyName) {
        Locator locator = getVendorStatusToggleLocator(propertyName);
        driverTools.waitForElementToAppear(locator);
        driverTools.scrollElementToCenter(locator);
        driverTools.click(locator);
    }

    public boolean isResultVendorStatusToggleOn(String propertyName) {
        WebElement toggleState = driverTools.waitForElementToPresence(getVendorStatusToggleStateLocator(propertyName)).orElseThrow();
        return "true".equals(driverTools.getValueAttribute(toggleState, "aria-checked"));
    }

    public String activateHotelChangeNameListener() {
        HotelChangeNameListenerApi.Inputs hotelChangeNameListenerInputs = new HotelChangeNameListenerApi.Inputs();
        HotelChangeNameListenerApi hotelChangeNameListenerApi = new HotelChangeNameListenerApi(hotelChangeNameListenerInputs);
        ApiResponse hotelChangeNameListenerResponse = hotelChangeNameListenerApi.hitApi(apiTools);
        Gson gson = new Gson();
        ActivateHotelListenerV2Response response = gson.fromJson(hotelChangeNameListenerResponse.getBody(), ActivateHotelListenerV2Response.class);
        String message = response.message;
        logger.log("MESSAGE: " + message);
        return  message;
    }


    public boolean isUnmergeAllPropertyRadioButtonNotSelected(){
        WebElement unmergeAll = driverTools.waitForElementToPresence(UNMERGE_ALL_RADIO_BUTTON_OPTION).orElseThrow();
        boolean isSelected = unmergeAll.isSelected();
        if (!isSelected) {
            String ariaChecked = unmergeAll.getAttribute("aria-checked");
            String classAttribute = unmergeAll.getAttribute("class");
            if ("true".equalsIgnoreCase(ariaChecked) ||
                    (classAttribute != null && (classAttribute.contains("checked") || classAttribute.contains("active")))) {
                isSelected = true;
            }
        }

        return !isSelected;
    }

    private int getTableHeadingIndex(String heading) {
        driverTools.waitForElementToAppear(TABLE_HEADINGS);
        List<WebElement> headings = driverTools.elements(TABLE_HEADINGS).orElseThrow(() -> new IllegalStateException("No headings found"));
        if (headings.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < headings.size(); i++) {
            String text = headings.get(i).getText().trim();
            if (text.isEmpty()) {
                text = headings.get(i).getAttribute("textContent").trim();
            }
            if (text.equalsIgnoreCase(heading)) {
                return i;
            }
        }
        return -1;
    }

    public String getTableFirstValueByHeading(String heading) {
        int index = getTableHeadingIndex(heading);
        if (index == -1) {
            throw new NoSuchElementException("No heading found for heading: " + heading);
        }

        Locator elementLocator = new Locator("ELEMENT", By.xpath("//div[contains(@class,'hotel-library-table')]//tbody//tr[1]//td[%d]".formatted(index + 1)));
        WebElement element = driverTools.waitForElementToAppear(elementLocator).orElseThrow(() -> new NoSuchElementException("Element not found"));
        String text = driverTools.getText(element);
        if (text.isBlank()) {
            text = element.getAttribute("textContent").trim();
        }

        logger.log("First value for %s: %s".formatted(heading, text));
        return text;
    }
    /**
     * Pre-condition guard: if a previous (failed/aborted) run left the source vendor merged with the other
     * vendor, unmerge it so the test starts from a clean state without manual intervention.
     * Expects the Hotel Library page to be open. Never throws and never writes report assertions.
     */
    public void ensurePropertiesNotMerged(String vendorName, String vendorExternalId, String otherVendorExternalId) {
        try {
            if (isStillMerged(vendorName, vendorExternalId, otherVendorExternalId)) {
                unmergeFirstResult();
            }
        } catch (Exception | AssertionError e) {
            logger.warn("Pre-check unmerge skipped: " + e.getMessage());
        }
    }

    /**
     * Best-effort cleanup used from finally blocks, so a failure mid-test never leaves merged test data behind.
     * Never throws and never writes report assertions, so the test's original failure stays the reported one.
     */
    public void cleanupMergedProperties(SidebarPage<?> sidebarPage, String vendorName, String vendorExternalId,
                                        String otherVendorExternalId) {
        try {
            sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.HOTEL_CORE);
            sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.HOTEL_LIBRARY);
            if (isStillMerged(vendorName, vendorExternalId, otherVendorExternalId)) {
                unmergeFirstResult();
            }
        } catch (Exception | AssertionError e) {
            logger.warn("Cleanup unmerge failed, manual unmerge may be needed: " + e.getMessage());
        }
    }

    /**
     * Searches the source vendor and reports whether its first result is merged with the other vendor.
     * Waits until the first row really belongs to the searched vendor (the page shows default results before the
     * search finishes) and matches the other external id as a whole token, so 123 does not match 1234.
     */
    private boolean isStillMerged(String vendorName, String vendorExternalId, String otherVendorExternalId) {
        clickSearchByVendorId();
        selectVendorName(vendorName);
        inputVendorExternalId(vendorExternalId);
        clickSearch();

        java.util.regex.Pattern searched = containsToken(vendorExternalId);
        String vendorDetails = "";
        for (int attempt = 0; attempt < 10; attempt++) {
            vendorDetails = getTableFirstValueByHeading("Vendor Status");
            if (searched.matcher(vendorDetails).find()) {
                return containsToken(otherVendorExternalId).matcher(vendorDetails).find();
            }
            driverTools.dummyWait(1);
        }
        logger.warn("First search result never showed vendor external id " + vendorExternalId + ", treating as not merged");
        return false;
    }

    private static java.util.regex.Pattern containsToken(String id) {
        return java.util.regex.Pattern.compile("(?<![A-Za-z0-9])" + java.util.regex.Pattern.quote(id) + "(?![A-Za-z0-9])");
    }

    /**
     * Unmerges all vendors of the first search result. Returns whether the success snackbar appeared.
     * Throws if a modal does not show up; does not write report assertions, so callers decide how to report.
     */
    public boolean unmergeFirstResult() {
        clickAction();
        clickUnmerge();
        if (!isUnmergePropertyModalAppeared()) {
            return false;
        }
        if (isUnmergeAllPropertyRadioButtonNotSelected()) {
            selectUnmergeAllPropertyRadioButton();
        }
        clickContinueUnmerge();
        if (!isUnmergePropertyModalAppeared()) {
            return false;
        }
        inputOtherUnmergeReason(com.tiket.core.CoreFunctions.generateRandomString(10));
        clickUnmergeSubmit();
        return isSnackbarAppeared();
    }
}
