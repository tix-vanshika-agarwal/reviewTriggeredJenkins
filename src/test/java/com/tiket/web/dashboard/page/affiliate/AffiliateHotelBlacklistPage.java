package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AffiliateHotelBlacklistPage<T extends WebDriver> extends BasePage<T> {
    public AffiliateHotelBlacklistPage(Session<T> session) {
        super(session);
    }

    private static final Locator SETTINGS_DROPDOWN_ACTIVE = new Locator("SETTINGS_DROPDOWN_ACTIVE", By.xpath("(//tr[.//div[text()='Active']]//input[@id='settings'])[1]"));
    private static final Locator BLACKLIST_OPTION = new Locator("BLACKLIST_OPTION", By.xpath("(//div[.//span[normalize-space()='Blacklist']])[4]"));
    private static final Locator BLACKLIST_PARTNER_LEVEL_TITLE = new Locator("BLACKLIST_PARTNER_LEVEL_TITLE", By.xpath("(//span[contains(normalize-space(),'Blacklisting Hotel Partner')])[1]"));
    private static final Locator FILTER_BY_DROPDOWN = new Locator("FILTER_BY_DROPDOWN", By.xpath("//div[contains(@class,'InputSearch_select_search') and contains(@class,'css-qhgteh-control')]"));
    private static final Locator FILTER_BY_INPUT = new Locator("FILTER_BY_INPUT", By.xpath("(//div[contains(@class,'InputSearch_select_search')]//input[@role='combobox'])[1]"));
    private static final Locator HOTEL_NAME_OPTION = new Locator("HOTEL_NAME_OPTION", By.xpath("//div[@role='option' and text()='Hotel Name']"));
    private static final Locator ENTER_VALUE_INPUT = new Locator("ENTER_VALUE_INPUT", By.xpath("(//input[@id='enter-value'])[1]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[contains(@class,'Button_variant_secondary') and text()='Search']"));
    private static final Locator FIRST_ROW_HOTEL_NAME = new Locator("FIRST_ROW_HOTEL_NAME", By.xpath("(//tr[contains(@class,'odd:bg-f4f7fe') or contains(@class,'even:bg-white')]/td[2])[1]"));

    public void openActiveSetting() {
        driverTools.waitForElementToAppear(SETTINGS_DROPDOWN_ACTIVE);
        driverTools.clickWithJavascriptExecutor(SETTINGS_DROPDOWN_ACTIVE);
    }

    public void openBlacklistHotel() {
        driverTools.waitForElementToAppear(BLACKLIST_OPTION);
        driverTools.clickWithJavascriptExecutor(BLACKLIST_OPTION);
    }

    public boolean isBlacklistPageOpens(){
        driverTools.waitForElementToAppear(BLACKLIST_PARTNER_LEVEL_TITLE);
        return  driverTools.isElementPresent(BLACKLIST_PARTNER_LEVEL_TITLE);
    }

    public void clickFilterByDropdown() {
        driverTools.waitForElementToAppear(FILTER_BY_DROPDOWN);
        driverTools.click(FILTER_BY_DROPDOWN);
    }

    public void selectHotelNameFilter(String filterValue) {
        driverTools.waitForElementToAppear(FILTER_BY_INPUT);
        driverTools.sendKeys(FILTER_BY_INPUT, filterValue);
        driverTools.waitForElementToAppear(HOTEL_NAME_OPTION);
        driverTools.click(HOTEL_NAME_OPTION);
    }

    public void enterSearchValue(String value) {
        driverTools.waitForElementToAppear(ENTER_VALUE_INPUT);
        driverTools.sendKeys(ENTER_VALUE_INPUT, value);
    }

    public void clickSearchButton() {
        driverTools.waitForElementToAppear(SEARCH_BUTTON);
        driverTools.click(SEARCH_BUTTON);
    }

    public boolean verifyFirstRowHotelName(String expectedHotelName) {
        driverTools.waitForElementToAppear(FIRST_ROW_HOTEL_NAME);
        String actualHotelName = driverTools.getText(FIRST_ROW_HOTEL_NAME).orElse("");
        return actualHotelName.contains(expectedHotelName);
    }
}

