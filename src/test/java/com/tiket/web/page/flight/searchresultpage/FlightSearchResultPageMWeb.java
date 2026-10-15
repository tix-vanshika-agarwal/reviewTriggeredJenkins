package com.tiket.web.page.flight.searchresultpage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FlightSearchResultPageMWeb <T extends WebDriver> extends FlightSearchResultPageWeb<T> {
    public FlightSearchResultPageMWeb(Session<T> session) { super(session); }

    private static final Locator FILTER = new Locator("FILTER", new By.ByXPath("//button[contains(@class,'bottom_bar_button') and .//span[contains(., 'Filter')]]"));
    private static final Locator FILTER_POPUP = new Locator("FILTER_POPUP", new By.ByXPath("//div[contains(@class, 'BottomSheet')]//h2[contains(text(), 'Filter')]"));
    private static final Locator OTHER_FILTER = new Locator("OTHER_FILTER", new By.ByXPath("//span[text()='Other Filters' or text()='Filter Lainnya']"));
    private static final Locator SEE_MORE_OTHER_FILTER = new Locator("SEE_MORE_OTHER_FILTER ", new By.ByXPath("//section[@data-section='otherFilter']//button[normalize-space()='See more' or normalize-space()='Lihat semua']"));
    private static final Locator LOWEST_PRICE_GUARANTEE = new Locator("LOWEST_PRICE_GUARANTEE", new By.ByXPath("//label[.//span[text()='Lowest Price Guarantee' or text()='Jaminan Harga Termurah']]//input[@type='checkbox']"));
    private static final Locator SAVE_OTHER_FILTER = new Locator("SAVE_OTHER_FILTER", new By.ByXPath("//button[contains(@class, 'FilterFormSheet_button_save__Tq1Sm')]//span[contains(text(), 'Save') or contains(text(), 'Simpan')]"));
    private static final Locator SAVE = new Locator("SAVE", new By.ByXPath("(//button[contains(@class,'FilterFormSheet_button_save')])[1]"));
    private static final Locator GotIT = new Locator("GotIT", new By.ByXPath("//span[text()='Got it!' or text()='Siaaap!']"));
    private static final Locator SAVE_TRANSIT_FILTER = new Locator("SAVE_TRANSIT_FILTER", new By.ByXPath("//*[contains(@class,'BottomSheet_children_wrapper')]//*[contains(@class,'Button_variant_primary')]"));

    @Override
    public void clickLowestPriceGuarantee() {
        try {
            driverTools.waitForElementToAppear(FILTER);
            driverTools.clickWithJavascriptExecutor(FILTER);
        }catch (Exception e){
            driverTools.waitForElementToAppear(FILTER);
            driverTools.click(FILTER);
        }

        driverTools.waitForElementToBeVisible(FILTER_POPUP);
        driverTools.scrollUntilElementFound(OTHER_FILTER);
        driverTools.waitForElementToAppear(OTHER_FILTER);
        driverTools.clickWithJavascriptExecutor(OTHER_FILTER);

        boolean lowestPriceShowing = driverTools.isElementPresent(LOWEST_PRICE_GUARANTEE);
        if (lowestPriceShowing) {
            driverTools.click(LOWEST_PRICE_GUARANTEE);
        } else {
            driverTools.click(SEE_MORE_OTHER_FILTER);
            driverTools.waitForElementToBeVisible(LOWEST_PRICE_GUARANTEE);
            driverTools.click(LOWEST_PRICE_GUARANTEE);
            driverTools.click(SAVE_OTHER_FILTER);
        }

        driverTools.clickWithJavascriptExecutor(SAVE);
        driverTools.waitForElementToDisappear(FILTER_POPUP);
    }

    @Override
    public void selectDirectFlightFilter() {
        driverTools.scrollUp(100);
        if (driverTools.isElementPresent(GotIT)) {
            driverTools.clickWithJavascriptExecutor(GotIT);
        }
        super.selectDirectFlightFilter();
    }
  
    @Override
    public void selectFlightWithSingleTransit() {
        openTransitFilter();
        driverTools.waitForElementToAppear(CHECKBOX_TRANSIT_ONE);
        driverTools.clickWithJavascriptExecutor(CHECKBOX_TRANSIT_ONE);
        driverTools.waitForElementToAppear(SAVE_TRANSIT_FILTER);
        driverTools.clickWithJavascriptExecutor(SAVE_TRANSIT_FILTER);
    }
}
