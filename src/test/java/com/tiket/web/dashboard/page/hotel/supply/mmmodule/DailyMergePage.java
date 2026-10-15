package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class DailyMergePage <T extends WebDriver> extends BasePage<T> {
    public DailyMergePage(Session<T> session) {
        super(session);
    }

    private static final Locator DAILY_MERGE_REVIEW_SECTION = new Locator("DAILY_MERGE_REVIEW_SECTION", new By.ById("daily-merging-review-page"));
    private static final Locator TMDF_LOCATION_COUNTRY_INPUT = new Locator("TMDF_LOCATION_COUNTRY_INPUT", new By.ByXPath("(//div[@class=\"tmdf-location-select\"])[1]//input"));
    private static final Locator TMDF_LOCATION_REGION_INPUT = new Locator("TMDF_LOCATION_REGION_INPUT", new By.ByXPath("(//div[@class=\"tmdf-location-select\"])[2]//input"));
    private static final Locator TMDF_LOCATION_CITY_INPUT = new Locator("TMDF_LOCATION_CITY_INPUT", new By.ByXPath("(//div[@class=\"tmdf-location-select\"])[3]//input"));
    private static final Locator TMDF_LOCATION_AREA_INPUT = new Locator("TMDF_LOCATION_AREA_INPUT", new By.ByXPath("(//div[@class=\"tmdf-location-select\"])[4]//input"));
    private static final Locator STATUS_INPUT = new Locator("STATUS_INPUT", new By.ByName("merged"));
    private static final Locator REVIEWED_INPUT = new Locator("REVIEWED_INPUT", new By.ByName("reviewed"));
    private static final Locator KEYWORD_INPUT = new Locator("KEYWORD_INPUT", new By.ByName("keyword"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[@type=\"submit\"]"));
    private static final Locator OLD_HOTEL_CELL_CHECKBOX = new Locator("OLD_HOTEL_CELL_CHECKBOX", new By.ByXPath("//div[contains(@class,'table-container')]//div[contains(@class,\"cell false\")][2]//div[contains(@class,'inner')]//input[@type=\"checkbox\"]/following-sibling::span"));
    private static final Locator ACTION_BUTTON = new Locator("", new By.ByXPath("//img[@data-testid=\"toggle-action-list-btn\"]"));
    private static final Locator REVIEW_BUTTON = new Locator("", new By.ByXPath("//div[@class=\"the-popoverlist\"]//label[text()='Review']"));
    private static final Locator MERGE_BUTTON = new Locator("", new By.ByXPath("//div[@class=\"the-popoverlist\"]//label[text()='Merge']"));
    private static final Locator UNMERGE_BUTTON = new Locator("", new By.ByXPath("//div[@class=\"the-popoverlist\"]//label[text()='Unmerge']"));
    private static final Locator REVIEWED_LABEL = new Locator("", new By.ByXPath("//div[@class=\"cell table-right-fixed odd\"]//div[contains(@class,'text-grey')][text()='Telah direview']"));

    private static Locator getTmdfLocationFilterCountryDropdownList(String text) {
        return new Locator("TMDF_LOCATION_COUNTRY_DROPDOWN_LIST", new By.ByXPath(String.format("(//div[@class=\"tmdf-location-select\"])[1]//label[text()='%s']/parent::span[contains(@class,\"popoverlist-list\")]", text)));
    }

    private static Locator getTmdfLocationFilterRegionDropdownList(String text) {
        return new Locator("TMDF_LOCATION_REGION_DROPDOWN_LIST", new By.ByXPath(String.format("(//div[@class=\"tmdf-location-select\"])[2]//label[text()='%s']/parent::span[contains(@class,\"popoverlist-list\")]", text)));
    }

    private static Locator getTmdfLocationFilterCityDropdownList(String text) {
        return new Locator("TMDF_LOCATION_CITY_DROPDOWN_LIST", new By.ByXPath(String.format("(//div[@class=\"tmdf-location-select\"])[3]//label[text()='%s']/parent::span[contains(@class,\"popoverlist-list\")]", text)));
    }

    private static Locator getTmdfLocationFilterAreaDropdownList(String text) {
        return new Locator("TMDF_LOCATION_AREA_DROPDOWN_LIST", new By.ByXPath(String.format("(//div[@class=\"tmdf-location-select\"])[4]//label[text()='%s']/parent::span[contains(@class,\"popoverlist-list\")]", text)));
    }

    private static Locator getFilterStatusDropdownList(String text) {
        return new Locator("STATUS_DROPDOWN_LIST", new By.ByXPath(String.format("//div[@id=\"merged\"]//label[text()='%s']/parent::span[contains(@class,\"popoverlist-list\")]", text)));
    }

    private static Locator getFilterReviewedDropdownList(String text) {
        return new Locator("REVIEWED_DROPDOWN_LIST", new By.ByXPath(String.format("//div[@id=\"reviewed\"]//label[text()='%s']/parent::span[contains(@class,\"popoverlist-list\")]", text)));
    }

    private static List<Locator> getSearchResultHotelDetailCellBySearchCriteria(String criteria) {
        List<Locator> cells = new ArrayList<>();
        String index = switch (criteria) {
            case "HOTEL_NAME" -> "1";
            case "COUNTRY" -> "last()-3";
            case "REGION" -> "last()-2";
            case "CITY" -> "last()-1";
            case "AREA" -> "last()";
            default -> throw new IllegalStateException("Unexpected value: " + criteria);
        };
        cells.add(new Locator("NEW_HOTEL_CELL", new By.ByXPath(String.format("//div[contains(@class,'table-container')]//div[contains(@class,\"cell false\")][1]//div[contains(@class,'inner')]/div[%s]", index))));
        cells.add(new Locator("OLD_HOTEL_CELL", new By.ByXPath(String.format("//div[contains(@class,'table-container')]//div[contains(@class,\"cell false\")][2]//div[contains(@class,'inner')]/div[%s]", index))));
        return cells;
    }

    private static Locator getSearchResultNewHotelCellBySearchCriteria(String text) {
        return new Locator("SEARCH_RESULT_NEW_HOTEL_CELL", new By.ByXPath(String.format("//div[contains(@class,'table-container')]//div[contains(@class,\"cell false\")][1]//div[contains(text(),'%s')]", text)));
    }

    private static Locator getSearchResultOldHotelCellBySearchCriteria(String text) {
        return new Locator("SEARCH_RESULT_OLD_HOTEL_CELL", new By.ByXPath(String.format("//div[contains(@class,'table-container')]//div[contains(@class,\"cell false\")][2]//div[contains(text(),'%s')]", text)));
    }

    public boolean isDailyMergePageDisplayed() {
        return driverTools.waitForElementToAppear(DAILY_MERGE_REVIEW_SECTION).isPresent();
    }

    public void selectFilterCountry(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(TMDF_LOCATION_COUNTRY_INPUT, 10).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getTmdfLocationFilterCountryDropdownList(text)).orElseThrow();
        driverTools.click(dropdown);
    }

    public void selectFilterRegion(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(TMDF_LOCATION_REGION_INPUT, 10).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getTmdfLocationFilterRegionDropdownList(text)).orElseThrow();
        driverTools.click(dropdown);
    }

    public void selectFilterCity(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(TMDF_LOCATION_CITY_INPUT, 10).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getTmdfLocationFilterCityDropdownList(text)).orElseThrow();
        driverTools.click(dropdown);
    }

    public void selectFilterArea(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(TMDF_LOCATION_AREA_INPUT, 10).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getTmdfLocationFilterAreaDropdownList(text)).orElseThrow();
        driverTools.click(dropdown);
    }

    public void selectFilterStatus(String status) {
        WebElement input = driverTools.waitForElementToBeClickable(STATUS_INPUT, 10).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = null;
        switch (status) {
            case "UNMERGED" -> dropdown = driverTools.waitForElementToBeClickable(getFilterStatusDropdownList("Belum dimerge")).orElseThrow();

            case "MERGED" -> dropdown = driverTools.waitForElementToBeClickable(getFilterStatusDropdownList("Sudah dimerge")).orElseThrow();
        }
        assert dropdown != null;
        driverTools.click(dropdown);
    }

    public void selectFilterReviewed(boolean reviewed) {
        WebElement input = driverTools.waitForElementToBeClickable(REVIEWED_INPUT, 10).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = null;
        if (reviewed) {
            dropdown = driverTools.waitForElementToBeClickable(getFilterReviewedDropdownList("Ya")).orElseThrow();
        } else {
            dropdown = driverTools.waitForElementToBeClickable(getFilterReviewedDropdownList("Tidak")).orElseThrow();
        }
        driverTools.click(dropdown);
    }

    public void enterKeyword(String text) {
        WebElement input = driverTools.waitForElementToAppear(KEYWORD_INPUT).orElseThrow();
        driverTools.sendKeys(input, text);
    }

    public void clickSearch() {
        WebElement button = driverTools.waitForElementToBeClickable(SEARCH_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void assertSearchResultNewHotelDisplayedBySearchCriteria(String criteria, String text) {
        List<WebElement> newHotelCells = driverTools.waitForElementsToAppear(getSearchResultHotelDetailCellBySearchCriteria(criteria).get(0)).orElseThrow();
        List<String> labels = new ArrayList<>();
        for (int i=0; i<newHotelCells.size(); i++) {
            labels.add(newHotelCells.get(i).getText());
        }
        for (String str : labels) {
            System.out.println("new hotel label: " + str);
            Assertion.assertThat(String.format("Search result by %s is available on new hotel column", text), str.contains(text), is(true));
        }
    }

    public boolean assertSearchResultOldHotelDisplayedBySearchCriteria(String criteria, String text) {
        List<WebElement> oldHotelCells = driverTools.waitForElementsToAppear(getSearchResultHotelDetailCellBySearchCriteria(criteria).get(1)).orElseThrow();
        List<String> labels = new ArrayList<>();
        for (int i=0; i<oldHotelCells.size(); i++) {
            labels.add(oldHotelCells.get(i).getText());
        }
        for (String str : labels) {
            Assertion.assertThat(String.format("Search result by %s is available on old hotel column", text), str.contains(text), is(true));
        }
        return false;
    }

    public boolean isSearchResultDisplayedByStatus(String status) {
        boolean result = false;
        WebElement actionButton = driverTools.waitForElementToBeClickable(ACTION_BUTTON).orElseThrow();
        driverTools.click(actionButton);
        switch (status) {
            case "UNMERGED" -> {
                List<WebElement> checkbox = driverTools.waitForElementsToAppear(OLD_HOTEL_CELL_CHECKBOX, 20).orElseThrow();
                result = driverTools.waitForElementToBeClickable(MERGE_BUTTON).isPresent();
            }
            case "MERGED" -> result = driverTools.waitForElementToBeClickable(UNMERGE_BUTTON).isPresent();
        }
        return result;
    }

    public boolean isSearchResultDisplayedByReviewed(boolean expected) {
        if (expected) {
            return driverTools.waitForElementToAppear(REVIEWED_LABEL).isPresent();
        } else {
            WebElement actionButton = driverTools.waitForElementToBeClickable(ACTION_BUTTON).orElseThrow();
            actionButton.click();
            return driverTools.waitForElementToBeClickable(REVIEW_BUTTON).isPresent();
        }
    }
}
