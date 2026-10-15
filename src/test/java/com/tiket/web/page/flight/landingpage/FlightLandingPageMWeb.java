package com.tiket.web.page.flight.landingpage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FlightLandingPageMWeb <T extends WebDriver> extends FlightLandingPageWeb<T> {

    private static final Locator DATE_SELECTOR_MWEB = new Locator("FLIGHT_DATE_SELECTOR_MWEB", By.xpath("//div[contains(@class,'SearchForm_date_selector')]"));
    private static final Locator SAVE_DATE_MWEB = new Locator("FLIGHT_SAVE_DATE_MWEB", By.xpath("//button[contains(@class,'Button_variant_primary') and .//span[normalize-space()='Save' or normalize-space()='Simpan']]"));

    public FlightLandingPageMWeb(Session<T> session) {
        super(session);
    }

    @Override
    public void selectDate(String dateToSelect) {
        Locator dateLocator = getDateLocator(dateToSelect);
        driverTools.click(DATE_SELECTOR_MWEB);
        driverTools.waitForElementToAppear(dateLocator, 5);
        driverTools.scrollElementToCenter(dateLocator);
        driverTools.clickWithJavascriptExecutor(dateLocator);
        driverTools.click(SAVE_DATE_MWEB);
        driverTools.waitForElementToDisappear(dateLocator);
    }

    @Override
    public void clickSaveButton(){
        driverTools.waitForElementToAppear(SAVE_DATE_MWEB);
        driverTools.click(SAVE_DATE_MWEB);
    }

}
