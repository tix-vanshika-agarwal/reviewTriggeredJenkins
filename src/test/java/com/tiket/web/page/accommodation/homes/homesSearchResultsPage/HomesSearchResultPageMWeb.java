package com.tiket.web.page.accommodation.homes.homesSearchResultsPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomesSearchResultPageMWeb<T extends WebDriver> extends HomesSearchResultPageWeb<T> {

    public HomesSearchResultPageMWeb(Session<T> session) {
        super(session);
    }

    private static final Locator ADVANCED_FILTER_ACTIVE = new Locator("ADVANCED_FILTER_ACTIVE", new By.ByXPath("//button[contains(@class, 'Chip_chip')]//span[contains(text(), 'Filter')]/../following-sibling::*//*[contains(@class, 'Notif_color_green')]"));
    private static final Locator QUICK_FILTER_SORT_ACTIVE = new Locator("QUICK_FILTER_SORT_ACTIVE", new By.ByXPath("//button[contains(@class, 'Chip_chip')]//span[contains(text(), 'Sort') or contains(text(), 'Urutkan')]/../following-sibling::*//*[contains(@class, 'Notif_color_green')]"));
    private static final Locator QUICK_FILTER_STAR_RATING_ACTIVE = new Locator("QUICK_FILTER_STAR_RATING_ACTIVE", new By.ByXPath("//button[contains(@class, 'Chip_active')]//span[contains(text(), 'Star') or contains(text(), 'Bintang')]"));
    private static final Locator SEARCH_BAR = new Locator("SEARCH_BAR", new By.ByXPath("//div[contains(@class, 'SearchBox_placeholder')]//input"));
    private static final Locator HOTEL_INPUT_LOCATION = new Locator("HOTEL_INPUT_LOCATION", new By.ByXPath("//div[contains(@class, 'ChangeSearchMobile_destination_container')]"));
    private static final Locator CHANGE_SEARCH_CRITERIA_BUTTON = new Locator("CHANGE_SEARCH_CRITERIA_BUTTON", new By.ByXPath("//button[contains(@class, 'index_mobile_button_change_search')]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[@data-testid='product-container-mobile-search-submit']"));
    private static final Locator SEARCH_LOCATION_HEADER_LABEL_MWEB = new Locator("SEARCH_LOCATION_HEADER_LABEL_MWEB", new By.ByXPath("//span[contains(@class,'index_mobile_text__Csl__')]"));
    private static final Locator SEARCH_DATE_HEADER_LABEL_MWEB = new Locator("SEARCH_DATE_HEADER_LABEL_MWEB", new By.ByXPath("//div[contains(@class,'index_mobile_date_container')]/span[1]"));
    private static final Locator SEARCH_GUEST_HEADER_LABEL_MWEB = new Locator("SEARCH_GUEST_HEADER_LABEL_MWEB", new By.ByXPath("//div[contains(@class,'index_mobile_date_container')]/span[3]"));

    @Override
    public void applyQuickFilters(int star) {
        Locator QUICK_FILTER_STAR_RATING_ITEM = new Locator("QUICK_FILTER_STAR_ITEM", new By.ByXPath("//h4[contains(text(), '"+star+"')]"));

        driverTools.waitForElementToPresence(QUICK_FILTER_SORT_CHIP);

        driverTools.click(QUICK_FILTER_SORT_CHIP);
        driverTools.click(QUICK_FILTER_SORT_ITEM);
        driverTools.waitForElementToDisappear(SRP_LOADING);

        driverTools.click(QUICK_FILTER_STAR_RATING_CHIP);
        driverTools.click(QUICK_FILTER_STAR_RATING_ITEM);
        driverTools.click(QUICK_FILTER_STAR_RATING_SAVE);
        driverTools.waitForElementToDisappear(SRP_LOADING);
    }



    @Override
    public void applyAdvancedFilters() {
        driverTools.click(ADVANCED_FILTER_CHIP);

        clickAdvancedFilterItem(ADVANCED_FILTER_POPULAR_ITEM, false);
        clickAdvancedFilterItem(ADVANCED_FILTER_SWITCH_INPUT_ITEM, false);
        clickAdvancedFilterItem(ADVANCED_FILTER_FACILITY_GROUP_ITEM, false);
        clickAdvancedFilterItem(ADVANCED_FILTER_CHECKBOX_ITEM, true);

        driverTools.click(ADVANCED_FILTER_SAVE);
        driverTools.waitForElementToDisappear(SRP_LOADING);
    }

    @Override
    public boolean isAdvancedFilterApplied() {
        driverTools.scrollElementToCenter(ADVANCED_FILTER_ACTIVE);

        return driverTools.waitForElementToPresence(ADVANCED_FILTER_ACTIVE).isPresent();
    }


    @Override
    public void updateStayLocation(String location) {
        driverTools.click(CHANGE_SEARCH_CRITERIA_BUTTON);
        driverTools.click(HOTEL_INPUT_LOCATION);
        driverTools.waitForElementToAppear(SEARCH_BAR).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEARCH_BAR);
        driverTools.sendKeys(SEARCH_BAR, location);
        driverTools.waitForElementToAppear(getSearchFormResultLocator(location)).orElseThrow();
        driverTools.click(getSearchFormResultLocator(location));
    }

    @Override
    public void clickSearchOnSRP() {
        driverTools.click(SEARCH_BUTTON);
    }

    public String getLocationNameFromInput() {
        driverTools.waitForElementToAppear(HOTEL_INPUT_LOCATION).orElseThrow();
        return driverTools.getText(HOTEL_INPUT_LOCATION).orElseThrow();
    }

    @Override
    public String getLocationHeaderLabel() {
        driverTools.waitForElementToAppear(SEARCH_LOCATION_HEADER_LABEL_MWEB).orElseThrow();
        return driverTools.getText(SEARCH_LOCATION_HEADER_LABEL_MWEB).orElseThrow();
    }

    @Override
    public String getDateHeaderLabel() {
        driverTools.waitForElementToAppear(SEARCH_DATE_HEADER_LABEL_MWEB).orElseThrow();
        return driverTools.getText(SEARCH_DATE_HEADER_LABEL_MWEB).orElseThrow();
    }

    @Override
    public String getGuestHeaderLabel() {
        driverTools.waitForElementToAppear(SEARCH_GUEST_HEADER_LABEL_MWEB).orElseThrow();
        return driverTools.getText(SEARCH_GUEST_HEADER_LABEL_MWEB).orElseThrow();
    }
}
