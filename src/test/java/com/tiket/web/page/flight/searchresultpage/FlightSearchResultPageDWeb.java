package com.tiket.web.page.flight.searchresultpage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.util.List;

public class FlightSearchResultPageDWeb <T extends WebDriver> extends FlightSearchResultPageWeb<T> {
    public FlightSearchResultPageDWeb (Session<T> session)  { super(session); }
    private static final Locator SearchResultLocator = new Locator("SearchResultLocator", new By.ByXPath("//div[@data-testid='srp-flight-card']"));
    private static final Locator FLIGHT_CARDS = new Locator("FLIGHT_CARDS", new By.ByXPath("//div[@data-testid='srp-flight-card']"));
    private static final Locator PROGRESS_BAR_LOADING = new Locator("PROGRESS_BAR_LOADING", new By.ByXPath("//*[contains(@class,'ProgressBar_progress_wrapper')]"));
    private static final Locator TRANSIT_FILTER = new Locator("TRANSIT_FILTER", new By.ByXPath(
            "//div[@data-testid='quick-filter-root']//button[contains(@class,'Chip_chip')][.//span[contains(@class,'Chip_text')][normalize-space()='Transit']]"
                    + " | //button[contains(@class,'Chip_chip')][.//span[contains(@class,'Chip_text')][normalize-space()='Transit']]"));
    private static final Locator FILTER = new Locator("FILTER", new By.ByXPath(
            "//div[@data-testid='quick-filter-root']//button[contains(@class,'Chip_chip')][.//span[contains(@class,'Chip_text')][normalize-space()='Filter' or normalize-space()='Saring']]"
                    + " | //button[contains(@class,'Chip_chip')][.//span[contains(@class,'Chip_text')][normalize-space()='Filter' or normalize-space()='Saring']]"));
    private static final Locator FILTER_POPUP = new Locator("FILTER_POPUP", new By.ByXPath("//section[contains(@class,'Modal_modal_popup')]"));
    private static final Locator LOWEST_PRICE_GUARANTEE = new Locator("LOWEST_PRICE_GUARANTEE", new By.ByXPath("(//label[contains(@class,'FilterOptionRow_row')][.//*[text()='Lowest Price Guarantee' or text()='Jaminan Harga Terendah']])"));
    private static final Locator OTHER_FILTER = new Locator("OTHER_FILTER", new By.ByXPath("//button[.//span[(contains(normalize-space(),'Others') and contains(normalize-space(),'check')) or (contains(normalize-space(),'Lainnya') and contains(normalize-space(),'cek'))]]"));
    private static final Locator SEE_MORE_OTHER_FILTER = new Locator("SEE_MORE_OTHER_FILTER ", new By.ByXPath("(//span[contains(@class,'Text_variant_activity') and contains(normalize-space(),'Show') and contains(normalize-space(),'more')])[last()]"));
    private static final Locator SAVE_OTHER_FILTER = new Locator("SAVE_OTHER_FILTER", new By.ByXPath("//button[contains(@class,'FilterFormSheet_button_save') and (normalize-space()='Save' or normalize-space()='Simpan')]"));
    private static final Locator SAVE = new Locator("SAVE", new By.ByXPath("(//button[contains(@class,'FilterFormSheet_button_save')])[1]"));
    private static final Locator PROGRESS_BAR_FILL = new Locator("PROGRESS_BAR_FILL", By.xpath("//div[contains(@class,'ProgressBar_bar_track')]//div[contains(@class,'ProgressBar_bar_fill')]"));
    private static final Locator SEE_FLIGHTS = new Locator("SEE_FLIGHTS", new By.ByXPath("//button[contains(@class,'AdvancedFilter_save_button')][.//span[contains(text(),'flights')]]"));

    public boolean isSearchResultPagePresent(){
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL, 30);
        boolean res = driverTools.waitForElementToAppear(SearchResultLocator).isPresent();
        driverTools.dummyWait(2);
        return res;
    }

    public void TransitFilter(){
        openTransitFilter();
    }

    public void clickLowestPriceGuarantee(){
        try {
            driverTools.waitForElementToAppear(FILTER);
            driverTools.clickWithJavascriptExecutor(FILTER);
        }catch (Exception e){
            driverTools.waitForElementToAppear(FILTER);
            driverTools.clickWithJavascriptExecutor(FILTER);
        }

        driverTools.waitForElementToAppear(FILTER_POPUP);
        driverTools.waitForElementToAppear(OTHER_FILTER);
        driverTools.clickWithJavascriptExecutor(OTHER_FILTER);

        boolean lowestPriceShowing = driverTools.isElementDisplayed(LOWEST_PRICE_GUARANTEE);
        if (lowestPriceShowing) {
            driverTools.clickWithJavascriptExecutor(LOWEST_PRICE_GUARANTEE);
        } else {
            driverTools.clickWithJavascriptExecutor(SEE_MORE_OTHER_FILTER);
            driverTools.waitForElementToBeVisible(LOWEST_PRICE_GUARANTEE);
            driverTools.clickWithJavascriptExecutor(LOWEST_PRICE_GUARANTEE);
            driverTools.clickWithJavascriptExecutor(SAVE_OTHER_FILTER);
        }

        driverTools.waitForElementToAppear(SEE_FLIGHTS);
        driverTools.clickWithJavascriptExecutor(SEE_FLIGHTS);
        driverTools.waitForElementToDisappear(FILTER_POPUP);
    }
}
