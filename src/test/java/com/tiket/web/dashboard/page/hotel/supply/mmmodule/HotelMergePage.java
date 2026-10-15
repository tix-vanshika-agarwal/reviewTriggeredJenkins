package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HotelMergePage <T extends WebDriver> extends BasePage<T> {
    public HotelMergePage(Session<T> session) {
        super(session);
    }

    private static final Locator HOTEL_MERGE_TITLE_LABEL = new Locator("HOTEL_MERGE_TITLE_LABEL", new By.ByXPath("(//span[text()='Manual Merge'])[last()]"));
    private static final Locator MERGE_BUTTON = new Locator("MERGE_BUTTON", new By.ByXPath("//button[@type='button'][contains(text(),'Merge')]"));
    private static final Locator MERGE_CONFIRMATION_MODAL_TITLE = new Locator("MERGE_CONFIRMATION_MODAL_TITLE", new By.ByXPath("//div[contains(@class,'Dialog')]//*[contains(@class,'title')][text()='Merge Confirmation']"));
    private static final Locator MERGE_CONFIRMATION_MODAL_YES_BUTTON = new Locator("MERGE_CONFIRMATION_MODAL_YES_BUTTON", new By.ByXPath("//div[contains(@class,'Dialog')]//button[contains(@class,'button')][text()='Yes']"));
    private static final Locator MERGE_SUCCESSFUL_SNACKBAR = new Locator("MERGE_SUCCESSFUL_SNACKBAR", new By.ByXPath("//div[contains(@class,'snackbars')]//div[contains(@class,'text')][contains(text(),'succes')]"));

    private static Locator getCountryDropdownField(int index) {
        return new Locator("COUNTRY_DROPDOWN_FIELD", new By.ByXPath(String.format("(//*[@id='country'])[%d]", index)));
    }

    private static Locator getCountryDropdownValue(int index, String text) {
        return new Locator("COUNTRY_DROPDOWN_VALUE", new By.ByXPath(String.format("(//div[contains(@class,'Card')])[%d]//li[text()='%s']", index, text)));
    }

    private static Locator getRegionDropdownField(int index) {
        return new Locator("REGION_DROPDOWN_FIELD", new By.ByXPath(String.format("(//*[@id='province'])[%d]", index)));
    }

    private static Locator getRegionDropdownValue(int index, String text) {
        return new Locator("REGION_DROPDOWN_VALUE", new By.ByXPath(String.format("(//div[contains(@class,'Card')])[%d]//li[text()='%s']", index, text)));
    }

    private static Locator getCityDropdownField(int index) {
        return new Locator("CITY_DROPDOWN_FIELD", new By.ByXPath(String.format("(//*[@id='city'])[%d]", index)));
    }

    private static Locator getCityDropdownValue(int index, String text) {
        return new Locator("CITY_DROPDOWN_VALUE", new By.ByXPath(String.format("(//div[contains(@class,'Card')])[%d]//li[text()='%s']", index, text)));
    }

    private static Locator getAreaDropdownField(int index) {
        return new Locator("AREA_DROPDOWN_FIELD", new By.ByXPath(String.format("(//*[@id='area'])[%d]", index)));
    }

    private static Locator getAreaDropdownValue(int index, String text) {
        return new Locator("AREA_DROPDOWN_VALUE", new By.ByXPath(String.format("(//div[contains(@class,'Card')])[%d]//li[text()='%s']", index, text)));
    }

    private static Locator getKeywordInput(int index) {
        return new Locator("KEYWORD_INPUT", new By.ByXPath(String.format("(//*[@id='sb-text-input-name-property'])[%d]", index)));
    }

    private static Locator getSearchButton(int index) {
        return new Locator("SEARCH_BUTTON", new By.ByXPath(String.format("(//button[@type='submit'])[%d]", index)));
    }

    private static Locator getTableHotelNameLabel(int index, String hotelName) {
        return new Locator("TABLE_HOTEL_NAME_LABEL", new By.ByXPath(String.format("(//div[contains(@class,'table')]//table//tbody//tr//td[2])[%d][contains(normalize-space(.),'%s')]", index, hotelName)));
    }

    private static Locator getTableVendorLabel(int index) {
        return new Locator("TABLE_VENDOR_LABEL", new By.ByXPath(String.format("(//div[contains(@class,'table')]//td[3])[%d]", index)));
    }

    private static Locator getSourceTableActionRadioButtonByIndex(int index) {
        return new Locator("SOURCE_TABLE_ACTION_RADIO_BUTTON", new By.ByXPath(String.format("(//div[contains(@class,'table')]//input[@type='radio'][@id='left'])[%d]", index)));
    }

    private static Locator getSourceTableActionRadioButtonByHotelName(String hotelName) {
        return new Locator("SOURCE_TABLE_ACTION_RADIO_BUTTON", new By.ByXPath(String.format("//div[contains(@class,'table')]//td[@class='text-left'][text()='%s']/following-sibling::td/input[@type='radio'][@id='left']", hotelName)));
    }

    private static Locator getSourceTableActionRadioButtonByVendorNameAndExternalId(String vendorName, String externalId) {
        return new Locator("SOURCE_TABLE_ACTION_RADIO_BUTTON", new By.ByXPath(String.format("//div[contains(@class,'table')]//td[contains(normalize-space(.),'%s')][contains(normalize-space(.),'%s')]//following-sibling::td//input[@type='radio'][@name='left']", externalId, vendorName)));
    }

    private static Locator getTargetTableActionRadioButtonByIndex(int index) {
        return new Locator("TARGET_TABLE_ACTION_RADIO_BUTTON", new By.ByXPath(String.format("(//div[contains(@class,'table')]//input[@type='radio'][@id='right'])[%d]", index)));
    }

    private static Locator getTargetTableActionRadioButtonByVendorNameAndExternalId(String vendorName, String externalId) {
        return new Locator("TARGET_TABLE_ACTION_RADIO_BUTTON", new By.ByXPath(String.format("//div[contains(@class,'table')]//td[contains(normalize-space(.),'%s')][contains(normalize-space(.),'%s')]//following-sibling::td//input[@type='radio'][@name='right']", externalId, vendorName)));
    }

    private static Locator getTargetTableActionRadioButtonByHotelName(String hotelName) {
        return new Locator("TARGET_TABLE_ACTION_RADIO_BUTTON", new By.ByXPath(String.format("//div[contains(@class,'table')]//td[@class='text-left'][text()='%s']/following-sibling::td/input[@type='radio'][@id='right']", hotelName)));
    }

    private int getIndexByType(String type) {
        int index = 0;
        switch (type) {
            case "SOURCE" -> index = 1;
            case "TARGET" -> index = 2;
        }
        return index;
    }

    public boolean isHotelMergePageDisplayed() {
        WebElement pageTitle = driverTools.waitForElementToPresence(HOTEL_MERGE_TITLE_LABEL).orElseThrow();
        return driverTools.isElementDisplayed(pageTitle);
    }

    public void selectCountry(String type, String text) {
        driverTools.waitForElementToPresence(getCountryDropdownField(getIndexByType(type))).orElseThrow();
        driverTools.clickWithJavascriptExecutor(getCountryDropdownField(getIndexByType(type)));
        driverTools.waitForElementToPresence(getCountryDropdownValue(getIndexByType(type), text)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(getCountryDropdownValue(getIndexByType(type), text));
    }

    public void selectRegion(String type, String text) {
        driverTools.waitForElementToPresence(getRegionDropdownField(getIndexByType(type))).orElseThrow();
        driverTools.clickWithJavascriptExecutor(getRegionDropdownField(getIndexByType(type)));
        driverTools.waitForElementToPresence(getRegionDropdownValue(getIndexByType(type), text)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(getRegionDropdownValue(getIndexByType(type), text));
    }

    public void selectCity(String type, String text) {
        driverTools.waitForElementToPresence(getCityDropdownField(getIndexByType(type))).orElseThrow();
        driverTools.clickWithJavascriptExecutor(getCityDropdownField(getIndexByType(type)));
        driverTools.waitForElementToPresence(getCityDropdownValue(getIndexByType(type), text)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(getCityDropdownValue(getIndexByType(type), text));
    }

    public void selectArea(String type, String text) {
        driverTools.waitForElementToPresence(getAreaDropdownField(getIndexByType(type))).orElseThrow();
        driverTools.clickWithJavascriptExecutor(getAreaDropdownField(getIndexByType(type)));
        driverTools.waitForElementToPresence(getAreaDropdownValue(getIndexByType(type), text)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(getAreaDropdownValue(getIndexByType(type), text));
    }

    public void inputKeyword(String type, String text) {
        WebElement keywordInput = driverTools.waitForElementToPresence(getKeywordInput(getIndexByType(type))).orElseThrow();
        driverTools.sendKeys(keywordInput, text);
    }

    public void clickSearch(String type) {
        WebElement searchButton = driverTools.waitForElementToPresence(getSearchButton(getIndexByType(type))).orElseThrow();
        driverTools.scrollAndClick(searchButton);
        driverTools.dummyWait(2);
    }

    public boolean isSearchResultAvailableByHotelName(String type, String hotelName) {
        WebElement hotelNameLabel = driverTools.waitForElementToPresence(getTableHotelNameLabel(getIndexByType(type), hotelName)).orElseThrow();
        return driverTools.getText(hotelNameLabel).contains(hotelName);
    }
    public String getSearchResultAvailable(String type, String hotelName) {
        WebElement hotelNameLabel = driverTools.waitForElementToAppear(getTableHotelNameLabel(getIndexByType(type), hotelName)).orElseThrow();
        String text = driverTools.getText(hotelNameLabel);
        if (text.isBlank()) {
            text = driverTools.getAttribute(hotelNameLabel, "textContent").trim();
        }
        logger.log("Search result for source hotel is available: %s".formatted(text));
        return text;
    }

    public void clickSourceActionRadioButtonByHotelName(String hotelName) {
        WebElement actionRadioButton = driverTools.waitForElementToPresence(getSourceTableActionRadioButtonByHotelName(hotelName)).orElseThrow();
        driverTools.click(actionRadioButton);
    }

    public void clickSourceActionRadioButtonByVendorName(String vendorName, String externalId) {
        WebElement actionRadioButton = driverTools.waitForElementToPresence(getSourceTableActionRadioButtonByVendorNameAndExternalId(vendorName.toUpperCase(), externalId)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(actionRadioButton);
    }

    public void clickTargetActionRadioButtonByHotelName(String hotelName) {
        WebElement actionRadioButton = driverTools.waitForElementToPresence(getTargetTableActionRadioButtonByHotelName(hotelName)).orElseThrow();
        driverTools.click(actionRadioButton);
    }

    public void clickTargetActionRadioButtonByVendorName(String vendorName, String externalId) {
        WebElement actionRadioButton = driverTools.waitForElementToPresence(getTargetTableActionRadioButtonByVendorNameAndExternalId(vendorName.toUpperCase(), externalId)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(actionRadioButton);
    }

    public void clickMergeButton() {
        WebElement mergeButton = driverTools.waitForElementToPresence(MERGE_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(mergeButton);
    }

    public boolean isMergeConfirmationModalAppeared() {
        WebElement mergeModal = driverTools.waitForElementToAppear(MERGE_CONFIRMATION_MODAL_TITLE, 10).orElseThrow();
        return driverTools.isElementDisplayed(mergeModal);
    }

    public void confirmHotelMerge() {
        WebElement yesButton = driverTools.waitForElementToPresence(MERGE_CONFIRMATION_MODAL_YES_BUTTON).orElseThrow();
        driverTools.click(yesButton);
    }

    public boolean isSuccessfulSnackbarDisplayed() {
        return driverTools.waitForElementToPresence(MERGE_SUCCESSFUL_SNACKBAR, 60).isPresent();
    }
}
