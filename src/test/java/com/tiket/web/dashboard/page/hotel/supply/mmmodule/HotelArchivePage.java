package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HotelArchivePage<T extends WebDriver> extends BasePage<T> {
    public HotelArchivePage(Session<T> session) {
        super(session);
    }

    private static final Locator HOTEL_ARCHIVE_TITLE = new Locator("HOTEL_ARCHIVE_TITLE", new By.ByXPath("//div[@data-testid=\"page-header\"]//span"));
    private static final Locator COUNTRY_INPUT = new Locator("COUNTRY_INPUT", new By.ByName("country"));
    private static final Locator REGION_INPUT = new Locator("REGION_INPUT", new By.ByName("region"));
    private static final Locator CITY_INPUT = new Locator("CITY_INPUT", new By.ByName("city"));
    private static final Locator AREA_INPUT = new Locator("AREA_INPUT", new By.ByName("area"));
    private static final Locator PROPERTY_NAME_INPUT = new Locator("PROPERTY_NAME_INPUT", new By.ById("sb-text-input-name"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[@data-testid=\"search-filter\"]"));
    private static final Locator SEARCH_RESULT_TABLE_RESTORE_BUTTON = new Locator("SEARCH_RESULT_TABLE_RESTORE_BUTTON", new By.ByXPath("//button[contains(@data-testid,'restore')]"));
    private static final Locator RESTORE_CONFIRMATION_MODAL = new Locator("RESTORE_CONFIRMATION_MODAL", new By.ByXPath("//div[contains(@class,'Dialog_pop_up')]"));
    private static final Locator RESTORE_CONFIRM_BUTTON = new Locator("RESTORE_CONFIRM_BUTTON", new By.ByXPath("(//div[contains(@class,'Figure_footer')]/button)[last()]"));
    private static final Locator SEARCH_RESULT_EMPTY_STATE = new Locator("SEARCH_RESULT_EMPTY_STATE", new By.ByXPath("//div[@data-testid=\"empty-case\"]"));
    private static final Locator SNACKBAR = new Locator("SNACKBAR", new By.ByXPath("//div[@class='tix-snackbar-wrapper v4']"));

    private static Locator getCountryOption(String text) {
        return new Locator("COUNTRY_OPTION", new By.ByXPath(String.format("//li[@data-name=\"country\"][text()='%s']", text)));
    }

    private static Locator getRegionOption(String text) {
        return new Locator("REGION_OPTION", new By.ByXPath(String.format("//li[@data-name=\"region\"][text()='%s']", text)));
    }

    private static Locator getCityOption(String text) {
        return new Locator("CITY_OPTION", new By.ByXPath(String.format("//li[@data-name=\"city\"][text()='%s']", text)));
    }

    private static Locator getAreaOption(String text) {
        return new Locator("AREA_OPTION", new By.ByXPath(String.format("//li[@data-name=\"area\"][text()='%s']", text)));
    }

    private static Locator getSearchResultTableHotelNameLabel(String text) {
        return new Locator("HOTEL_NAME_LABEL", new By.ByXPath(String.format("(//div[contains(@class,'hotel-archive-table')]//td)[2][text()='%s']", text)));
    }

    public boolean isHotelArchivePageDisplayed() {
        return driverTools.waitForElementToAppear(HOTEL_ARCHIVE_TITLE).isPresent();
    }

    public void searchAndSelectLocationByType(String type, String text) {
        Locator locatorInput = null;
        Locator locatorOption = null;
        switch (type) {
            case "COUNTRY" -> {
                locatorInput = COUNTRY_INPUT;
                locatorOption = getCountryOption(text);
            }
            case "REGION" -> {
                locatorInput = REGION_INPUT;
                locatorOption = getRegionOption(text);
            }
            case "CITY" -> {
                locatorInput = CITY_INPUT;
                locatorOption = getCityOption(text);
            }
            case "AREA" -> {
                locatorInput = AREA_INPUT;
                locatorOption = getAreaOption(text);
            }
        }
        WebElement input = driverTools.waitForElementToAppear(locatorInput).orElseThrow();
        driverTools.click(input);
        driverTools.sendKeys(input, text);
        WebElement option = driverTools.waitForElementToBeClickable(locatorOption).orElseThrow();
        driverTools.click(option);
    }

    public void inputPropertyName(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(PROPERTY_NAME_INPUT).orElseThrow();
        driverTools.click(input);
        driverTools.sendKeys(input, text);
    }

    public void clickSearch() {
        WebElement button = driverTools.waitForElementToBeClickable(SEARCH_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isSearchResultDisplayedByPropertyName(String text) {
        WebElement label = driverTools.waitForElementToAppear(getSearchResultTableHotelNameLabel(text)).orElseThrow();
        return driverTools.getText(label).equals(text);
    }

    public void restorePropertyToHotelLibrary() {
        WebElement button = driverTools.waitForElementToBeClickable(SEARCH_RESULT_TABLE_RESTORE_BUTTON).orElseThrow();
        driverTools.click(button);
        WebElement modal = driverTools.waitForElementToAppear(RESTORE_CONFIRMATION_MODAL).orElseThrow();
        WebElement confirmButton = driverTools.waitForElementToBeClickable(RESTORE_CONFIRM_BUTTON).orElseThrow();
        driverTools.click(confirmButton);
    }

    public boolean isSearchResultNotFound() {
        return driverTools.waitForElementToAppear(SEARCH_RESULT_EMPTY_STATE).isPresent();
    }
}
