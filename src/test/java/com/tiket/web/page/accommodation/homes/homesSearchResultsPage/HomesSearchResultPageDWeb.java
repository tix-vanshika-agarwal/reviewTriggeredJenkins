package com.tiket.web.page.accommodation.homes.homesSearchResultsPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomesSearchResultPageDWeb<T extends WebDriver> extends HomesSearchResultPageWeb<T> {

    public HomesSearchResultPageDWeb(Session<T> session) {
        super(session);
    }

    private static final Locator ADVANCED_FILTER_ACTIVE = new Locator("ADVANCED_FILTER_ACTIVE", new By.ByXPath("//button[contains(@class, 'Chip_chip')]//span[contains(text(), 'Filter')]/following-sibling::*//*[contains(@class, 'Notif_color_green')]"));
    private static final Locator QUICK_FILTER_SORT_ACTIVE = new Locator("QUICK_FILTER_SORT_ACTIVE", new By.ByXPath("//button[contains(@class, 'Chip_chip')]//span[contains(text(), 'Sort') or contains(text(), 'Urutkan')]/following-sibling::*//*[contains(@class, 'Notif_color_green')]"));
    private static final Locator QUICK_FILTER_STAR_RATING_ACTIVE = new Locator("QUICK_FILTER_STAR_RATING_ACTIVE", new By.ByXPath("//button[contains(@class, 'Chip_chip')]//span[contains(text(), 'Star') or contains(text(), 'Bintang')]/following-sibling::*//*[contains(@class, 'Notif_color_green')]"));
    private static final Locator SEARCH_BAR = new Locator("SEARCH_BAR", new By.ByXPath("//div[contains(@class, 'SearchBox_placeholder')]//input"));
    private static final Locator HOTEL_INPUT_LOCATION = new Locator("HOTEL_INPUT_LOCATION", new By.ByXPath("(//span[contains(@class, 'index_desktop')])[1]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[@data-testid='search-button-desktop']"));

    @Override
    public void applyQuickFilters(int star) {
        Locator QUICK_FILTER_STAR_RATING_ITEM = new Locator("QUICK_FILTER_STAR_ITEM", new By.ByXPath("//*[contains(@class,'FilterNhaStar_nha_star_chip')]//*[text()='"+star+"']"));
        
        if (driverTools.isElementDisplayed(QUICK_FILTER_SORT_CHIP)) {
            driverTools.click(QUICK_FILTER_SORT_CHIP);
        }
        if (driverTools.isElementDisplayed(QUICK_FILTER_STAR_RATING_CHIP)) {
            driverTools.click(QUICK_FILTER_STAR_RATING_CHIP);
        }
        
        driverTools.waitForElementToDisappear(SRP_LOADING);
        driverTools.click(QUICK_FILTER_STAR_RATING_ITEM);
        driverTools.dummyWait(3);
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
        driverTools.dummyWait(3);
        driverTools.waitForElementToDisappear(SRP_LOADING);
    }


    @Override
    public boolean isAdvancedFilterApplied() {
        driverTools.scrollElementToCenter(ADVANCED_FILTER_ACTIVE);

        return driverTools.waitForElementToPresence(ADVANCED_FILTER_ACTIVE).isPresent();
    }


    @Override
    public void updateStayLocation(String location) {
        driverTools.click(HOTEL_INPUT_LOCATION);
        driverTools.waitForElementToAppear(SEARCH_BAR).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEARCH_BAR);
        driverTools.sendKeys(SEARCH_BAR, location);
        WebElement searchFormResult = driverTools.waitForElementToAppear(getSearchFormResultLocator(location)).orElseThrow();
        driverTools.click(searchFormResult);
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
        return null;
    }

    @Override
    public String getDateHeaderLabel() {
        return null;
    }

    @Override
    public String getGuestHeaderLabel() {
        return null;
    }
}
