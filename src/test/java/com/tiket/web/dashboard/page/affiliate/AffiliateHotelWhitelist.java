package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.b2b.corporate.OrderDetailResponse;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AffiliateHotelWhitelist<T extends WebDriver> extends BasePage<T> {
    public AffiliateHotelWhitelist(Session<T> session) {
        super(session);
    }

    private static final Locator HOTEL_API = new Locator("HOTEL_API", By.xpath("//p[text()='Hotel API']"));
    private static final Locator WHITELIST_SECTION = new Locator("WHITELIST_SECTION", By.xpath("(//div[text()='Whitelist'])[1]"));
    private static final Locator FILE_LOG_BUTTON = new Locator("FILE_LOG_BUTTON", By.xpath("//button[normalize-space(text())='File Log']"));
    private static final Locator FILE_LOG_PAGE_BANNER = new Locator("FILE_LOG_PAGE_BANNER", By.xpath("//span[text()='Manual Whitelisting Hotel File Log Affiliate Level']"));
    private static final Locator VENDOR_SECTION = new Locator("VENDOR_SECTION", By.xpath("//span[text()='Vendor']"));
    private static final Locator ADD_SINGLE = new Locator("ADD_SINGLE", By.xpath("//button[contains(text(),'Add Single')]"));
    private static final Locator ADD_BULK = new Locator("ADD_BULK", By.xpath("//button[contains(text(),'Add Bulk')]"));
    private static final Locator REMOVE_BULK = new Locator("REMOVE_BULK", By.xpath("//button[contains(text(),'Remove Bulk')]"));
    private static final Locator DOWNLOAD_DATA = new Locator("DOWNLOAD_DATA", By.xpath("//button[contains(text(),'Download Data')]"));
    private static final Locator DOWNLOAD_DATA_FILTER1 = new Locator("DOWNLOAD_DATA_FILTER1", By.xpath("(//div[contains(@class, 'css-qhgteh-control') and contains(@class, 'InputSearch_select_search')])[2]"));
    private static final Locator DOWNLOAD_DATA_COUNTRY_FILTER = new Locator("DOWNLOAD_DATA_COUNTRY_FILTER", By.xpath("//div[text()='Country' and @role='option']"));
    private static final Locator DOWNLOAD_DATA_FILTER_INPUT1 = new Locator("DOWNLOAD_DATA_FILTER_INPUT1", By.xpath("(//div[contains(@class, 'css-19bb58m') and @data-value=''])[3]"));
    private static final Locator COUNTRY_DOWNLOAD_OPT = new Locator("COUNTRY_DOWNLOAD_OPT", By.xpath("//*[text()='Indonesia']"));
    private static final Locator DOWNLOAD_BTN = new Locator("DOWNLOAD_BTN", By.xpath("(//button[contains(text(),'Download')])[2]"));
    private static final Locator REMOVE_BTN = new Locator("REMOVE_BTN", By.xpath("(//button[contains(text(),'Remove')])[2]"));
    private static final Locator COUNTRY_RADIO_BUTTON_REMOVE_BULK = new Locator("COUNTRY_RADIO_BUTTON_REMOVE_BULK", By.xpath("(//span[@class='Radio_icon__VgKsO'])[2]"));
    private static final Locator COUNTRY_NAME_PLACEHOLDER = new Locator("COUNTRY_NAME_PLACEHOLDER", By.xpath("(//div[contains(@class, 'InputSearch_select_search__') and contains(@class, 'css-qhgteh-control')]//div[contains(@class, 'css-19bb58m')])[2]"));
    private static final Locator COUNTRY_OPTION_REMOVE_BULK = new Locator("COUNTRY_OPTION_REMOVE_BULK", By.xpath("//*[text()='HongKong']"));
    private static final Locator POP_UP_ADD_HOTEL_WHITELIST = new Locator("POP_UP_ADD_HOTEL_WHITELIST", By.xpath("//div[contains(@class,'Dialog_pop_up')]"));
    private static final Locator HOTEL_ID_INPUT = new Locator("HOTEL_ID_INPUT", By.xpath("//input[@id='hotel-id']"));
    private static final Locator HOTEL_ID_RESULT = new Locator("HOTEL_ID_RESULT", By.xpath("//p[text()='Hotel ID']/following-sibling::p"));
    private static final Locator SEARCH_BTN = new Locator("SEARCH_BTN", By.xpath("//div[contains(@class, 'Dialog_container')]//button[normalize-space()='Search']"));
    private static final Locator FILTER_BY_HOTEL_ID = new Locator("FILTER_BY_HOTEL_ID", By.xpath("//div[@role='option' and text()='Hotel ID']"));
    private static final Locator FILTER_BY_COUNTRY = new Locator("FILTER_BY_COUNTRY", By.xpath("//div[@role='option' and text()='Country']"));
    private static final Locator COUNTRY_OPTION = new Locator("COUNTRY_OPTION", By.xpath("//*[text()='Indonesia']"));
    private static final Locator FILTER_BY_CITY = new Locator("FILTER_BY_CITY", By.xpath("//div[@role='option' and text()='City']"));
    private static final Locator CITY_OPTION = new Locator("CITY_OPTION", By.xpath("(//div[@role='option'])[3]"));
    private static final Locator ALL_ROW = new Locator("ALL_ROW", By.xpath("//tbody/tr"));
    private static final Locator ADD_WHITELIST_BTN = new Locator("ADD_WHITELIST_BTN", By.xpath("//div[contains(@class, 'Dialog_container')]//button[normalize-space()='Add Whitelist']"));
    private static final Locator FILTER_BY_DROPDOWN_1 = new Locator("FILTER_BY_DROPDOWN_1", By.xpath("//div[contains(@class, 'css-19bb58m')]//input[@type='text' and @role='combobox']"));
    private static final Locator FILTER_BY_DROPDOWN_2 = new Locator("FILTER_BY_DROPDOWN_2", By.xpath("(//div[contains(@class, 'css-19bb58m')]//input[@type='text' and @role='combobox'])[3]"));
    private static final Locator HOTEL_NAME_FILTER_BY_OPTION = new Locator("HOTEL_NAME_FILTER_BY_OPTION", By.xpath("//input[@id='react-select-2-input']"));
    private static final Locator ENTER_VALUE_FILTER_BY_1 = new Locator("ENTER_VALUE_FILTER_BY_1", By.xpath("//input[@id='enter-value']"));
    private static final Locator ENTER_VALUE_FILTER_1 = new Locator("ENTER_VALUE_FILTER_1", By.xpath("(//div[contains(@class, 'css-19bb58m')]//input[@type='text' and @role='combobox'])[2]"));
    private static final Locator ENTER_VALUE_FILTER_BY_2 = new Locator("ENTER_VALUE_FILTER_BY_2", By.xpath("(//div[contains(@class, 'css-19bb58m') and @data-value=''])[4]"));
    private static final Locator SEARCH_BUTTON_FILTER = new Locator("SEARCH_BUTTON_FILTER", By.xpath("//button[normalize-space()='Search']"));
    private static final Locator DROPDOWN_OPTION = new Locator("DROPDOWN_OPTION", By.xpath("//div[starts-with(@id, 'react-select-') and contains(@id, '-option') and text()='Hotel Name']"));
    private static final Locator BROWSE_FILE_BTN = new Locator("BROWSE_FILE_BTN", By.xpath("//span[normalize-space()='Upload File']/following-sibling::button"));
    private static final Locator SNACKBAR = new Locator("SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_text_content')]"));

    private Locator getHotelNameLocator(String firstHotelName) {
        String xpath = String.format("//td[normalize-space()='%s']", firstHotelName);
        return new Locator("FIRST_DATA_ROW", new By.ByXPath(xpath));
    }

    public void goToAffiliateLandingWhitelistPage() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/affiliate/hotel/whitelist/hotel");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/affiliate/hotel/whitelist/hotel");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://tiket.com/admin/affiliate/hotel/whitelist/hotel");
        }
    }

    public void clickHotelAPIManualWhitelist() {
        driverTools.waitForElementToAppear(HOTEL_API, 5);
        driverTools.click(HOTEL_API);
        driverTools.waitForElementToAppear(WHITELIST_SECTION, 5);
        driverTools.click(WHITELIST_SECTION);
    }

    public void clickAddSingleButton() {
        driverTools.waitForElementToAppear(ADD_SINGLE, 5);
        driverTools.click(ADD_SINGLE);
    }

    public void clickDownloadDataButton() {
        driverTools.waitForElementToAppear(DOWNLOAD_DATA, 5);
        driverTools.click(DOWNLOAD_DATA);
    }

    public void clickAddBulkButton() {
        driverTools.waitForElementToAppear(ADD_BULK, 5);
        driverTools.click(ADD_BULK);
    }

    public void clickRemoveBulkButton() {
        driverTools.waitForElementToAppear(REMOVE_BULK, 5);
        driverTools.click(REMOVE_BULK);
    }

    public void clickRemoveButton() {
        driverTools.waitForElementToAppear(REMOVE_BTN, 5);
        driverTools.click(REMOVE_BTN);
    }

    public void clickDownloadButton() {
        driverTools.waitForElementToAppear(DOWNLOAD_BTN, 5);
        driverTools.click(DOWNLOAD_BTN);
    }

    public void searchByHotelIdWhitelist(String hotelId) {
        driverTools.waitForElementToAppear(HOTEL_ID_INPUT);
        driverTools.clickWithJavascriptExecutor(HOTEL_ID_INPUT);
        driverTools.sendKeys(HOTEL_ID_INPUT, hotelId);
    }

    public void clickSearchHotelButton() {
        driverTools.waitForElementToAppear(HOTEL_ID_INPUT,3);
        driverTools.click(SEARCH_BTN);
    }

    public void clickAddWhitelistButton() {
        driverTools.waitForElementToAppear(HOTEL_ID_RESULT,5);
        driverTools.click(ADD_WHITELIST_BTN);
    }

    public void clickHotelAPIManualWhitelistPage() {
        driverTools.waitForElementToAppear(HOTEL_API, 5);
        driverTools.click(HOTEL_API);
    }

    public void clickFilter1DropDown() {
        driverTools.waitForElementToAppear(FILTER_BY_DROPDOWN_1, 5);
        driverTools.click(FILTER_BY_DROPDOWN_1);
    }

    public void clickFilter2DropDown() {
        driverTools.waitForElementToAppear(FILTER_BY_DROPDOWN_2, 5);
        driverTools.click(FILTER_BY_DROPDOWN_2);
    }

    public void selectHotelNameOption(String filterBy) {
        driverTools.waitForElementToAppear(HOTEL_NAME_FILTER_BY_OPTION, 1);
        driverTools.click(HOTEL_NAME_FILTER_BY_OPTION);
        driverTools.sendKeys(HOTEL_NAME_FILTER_BY_OPTION, filterBy);
        driverTools.click(DROPDOWN_OPTION);
    }

    public void insertHotelNameforEnterValueField(String hotelName) {
        driverTools.waitForElementToAppear(ENTER_VALUE_FILTER_BY_1, 5);
        driverTools.click(ENTER_VALUE_FILTER_BY_1);
        driverTools.sendKeys(ENTER_VALUE_FILTER_BY_1, hotelName);
    }

    public void clickSearchButtonFilter() {
        driverTools.click(SEARCH_BUTTON_FILTER);
    }

    public String getHotelNameResult(String firstHotelName) {
        Locator firstRowData = getHotelNameLocator(firstHotelName);
        driverTools.waitForElementToAppear(firstRowData, 4).orElseThrow();
        return driverTools.getText(firstRowData).orElseThrow();
    }

    public void clickFileLogButton() {
        driverTools.waitForElementToAppear(FILE_LOG_BUTTON, 5);
        driverTools.click(FILE_LOG_BUTTON);
    }

    public boolean isOnFileLogPage() {
        driverTools.waitForElementToAppear(FILE_LOG_PAGE_BANNER, 2);
        return driverTools.getCurrentURL().contains("/admin/affiliate/hotel/whitelist/file-log");
    }

    public void clickVendorButton() {
        driverTools.waitForElementToAppear(VENDOR_SECTION, 5);
        driverTools.click(VENDOR_SECTION);
    }

    public void filterByHotelId(String hotelId) {
        driverTools.click(FILTER_BY_HOTEL_ID);
        driverTools.waitForElementToAppear(ENTER_VALUE_FILTER_BY_1, 5);
        driverTools.click(ENTER_VALUE_FILTER_BY_1);
        driverTools.sendKeys(ENTER_VALUE_FILTER_BY_1, hotelId);
    }

    public void filterByCountry(String expectedCountry) {
        driverTools.click(FILTER_BY_COUNTRY);
        driverTools.waitForElementToAppear(ENTER_VALUE_FILTER_1, 2);
        driverTools.click(ENTER_VALUE_FILTER_1);
        driverTools.sendKeys(ENTER_VALUE_FILTER_1, expectedCountry);
        Locator dynamicCountryOption = new Locator(
                "DYNAMIC_COUNTRY_OPTION",
                By.xpath("//div[@role='option' and normalize-space()='" + expectedCountry + "']")
        );
        driverTools.waitForElementToAppear(dynamicCountryOption, 2);
        driverTools.click(dynamicCountryOption);
    }

    public void filterByCity() {
        driverTools.click(FILTER_BY_CITY);
        driverTools.waitForElementToAppear(ENTER_VALUE_FILTER_BY_2, 2);
        driverTools.click(ENTER_VALUE_FILTER_BY_2);
        driverTools.waitForElementToAppear(CITY_OPTION, 2);
        driverTools.click(CITY_OPTION);
    }

    public boolean checkSearchByHotelId(String hotelId) {
        Locator FIRST_ROW = new Locator("FIRST_ROW", By.xpath("//tr[td[normalize-space(text())='" + hotelId + "']]"));
        driverTools.waitForElementToAppear(FIRST_ROW, 5);
        String actualHotelId = driverTools.getText(FIRST_ROW).orElseThrow();
        logger.log("Actual Hotel ID: " + actualHotelId);
        if (!actualHotelId.contains(hotelId)) {
            return false;
        }
        return true;
    }

    public boolean checkSearchBy2FilterField(String expectedCity, String expectedCountry) {
        List<WebElement> rows = driverTools.elements(ALL_ROW).orElseThrow();
        for (int i = 1; i <= rows.size(); i++) {
            Locator CITY = new Locator("CITY", By.xpath("(//td[contains(text(),'" + expectedCity + "')])[" + i + "]"));
            Locator COUNTRY = new Locator("COUNTRY", By.xpath("(//td[contains(text(),'" + expectedCountry + "')])[" + i + "]"));
            driverTools.waitForElementToAppear(CITY, 1);
            driverTools.waitForElementToAppear(COUNTRY, 1);
            String actualCity = driverTools.getText(CITY).orElse("").trim();
            String actualCountry = driverTools.getText(COUNTRY).orElse("").trim();
            logger.log("Row " + i + ": City = " + actualCity + ", Country = " + actualCountry);

            if (!actualCity.contains(expectedCity) || !actualCountry.contains(expectedCountry)) {
                return false;
            }
        }
        return true;
    }

    public void pushData(String filePath) {
        File data = new File(filePath);
        driverTools.waitForElementToAppear(BROWSE_FILE_BTN, 5);
        driverTools.sendKeys(BROWSE_FILE_BTN, data.getAbsolutePath());
    }

    public void removeBulkWhitelistHotel() throws InterruptedException {
        driverTools.waitForElementToAppear(COUNTRY_RADIO_BUTTON_REMOVE_BULK, 10);
        driverTools.click(COUNTRY_RADIO_BUTTON_REMOVE_BULK);

        driverTools.waitForElementToAppear(COUNTRY_NAME_PLACEHOLDER, 5);
        driverTools.click(COUNTRY_NAME_PLACEHOLDER);

        driverTools.waitForElementToAppear(COUNTRY_OPTION_REMOVE_BULK, 5);
        driverTools.click(COUNTRY_OPTION_REMOVE_BULK);
        driverTools.waitForElementToBeClickable(COUNTRY_OPTION_REMOVE_BULK,5);
        clickRemoveButton();
    }

    public void fillDownLoadDataDetails(){
        driverTools.waitForElementToAppear(DOWNLOAD_DATA_FILTER1, 2);
        driverTools.click(DOWNLOAD_DATA_FILTER1);

        driverTools.waitForElementToAppear(DOWNLOAD_DATA_COUNTRY_FILTER, 2);
        driverTools.click(DOWNLOAD_DATA_COUNTRY_FILTER);

        driverTools.waitForElementToAppear(DOWNLOAD_DATA_FILTER_INPUT1, 2);
        driverTools.click(DOWNLOAD_DATA_FILTER_INPUT1);

        driverTools.waitForElementToAppear(COUNTRY_DOWNLOAD_OPT, 2);
        driverTools.click(COUNTRY_DOWNLOAD_OPT);
    }

    public boolean checkHotelWhitelistedSnackbar(String expectedSnackbarText){
        driverTools.waitForElementToAppear(SNACKBAR, 2).orElseThrow();
        String snackbarText = driverTools.getText(SNACKBAR).orElseThrow();
        return driverTools.isElementPresent(SNACKBAR) && snackbarText.contains(expectedSnackbarText);
    }
}