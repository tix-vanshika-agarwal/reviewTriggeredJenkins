package com.tiket.web.page.nft.shinkansen.search_page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ShinkansenSearchPageMWeb<T extends WebDriver> extends ShinkansenSearchPageWeb<T> {

    public ShinkansenSearchPageMWeb(Session<T> session) {
        super(session);
    }

    // Locators mirrored from Whoosh MWeb for now.
    private static final Locator ORIGIN_SEARCH_FIELD = new Locator("ORIGIN_SEARCH_FIELD", new By.ByXPath("(//input[@type='text'])[1]"));
    private static final Locator DESTINATION_SEARCH_FIELD = new Locator("DESTINATION_SEARCH_FIELD", new By.ByXPath("(//input[@type='text'])[1]"));
    private static final Locator SAVE_DATE_BTN = new Locator("SAVE_DATE_BTN", new By.ByXPath("//button[normalize-space(text())='Save' or normalize-space(text())='Simpan']"));

    @Override
    public void selectOrigin(String stationName) {
        driverTools.waitForElementToAppear(ORIGIN);
        driverTools.clickWithJavascriptExecutor(ORIGIN);
        driverTools.waitForElementToAppear(ORIGIN_SEARCH_FIELD);
        driverTools.clickWithJavascriptExecutor(ORIGIN_SEARCH_FIELD);
        driverTools.sendKeys(ORIGIN_SEARCH_FIELD, stationName);
        driverTools.waitForElementToAppear(FIRST_STATION);
        driverTools.clickWithJavascriptExecutor(FIRST_STATION);
    }

    @Override
    public void selectDestination(String stationName) {
        driverTools.waitForElementToBeClickable(DESTINATION);
        driverTools.clickWithJavascriptExecutor(DESTINATION);
        driverTools.waitForElementToBeClickable(DESTINATION_SEARCH_FIELD);
        driverTools.clickWithJavascriptExecutor(DESTINATION_SEARCH_FIELD);
        driverTools.sendKeys(DESTINATION_SEARCH_FIELD, stationName);
        driverTools.waitForElementToAppear(FIRST_STATION);
        driverTools.clickWithJavascriptExecutor(FIRST_STATION);
    }

    @Override
    public void selectPassenger(int adultCount, int infantCount) {
        driverTools.waitForElementToBeClickable(PASSENGER_BTN, 20).ifPresent(driverTools::clickWithJavascriptExecutor);

        driverTools.waitForElementToAppear(ADULT_CNT);
        driverTools.waitForElementToAppear(INFANT_CNT);
        int currentAdultCount = Integer.parseInt(
                driverTools.getAttribute(ADULT_CNT.by(), "value")
        );

        int currentInfantCount = Integer.parseInt(
                driverTools.getAttribute(INFANT_CNT.by(), "value")
        );

        if (adultCount < 1 || adultCount >= 5) {
            throw new IllegalArgumentException("Adult count must be between 1 and 5");
        }

        if (infantCount < 0 || infantCount >= 5) {
            throw new IllegalArgumentException("Infant count must be between 0 and 5");
        }

        while (currentAdultCount < adultCount) {
            driverTools.clickWithJavascriptExecutor(ADULT_INC);
            currentAdultCount++;
        }
        while (currentAdultCount > adultCount) {
            driverTools.clickWithJavascriptExecutor(ADULT_DEC);
            currentAdultCount--;
        }

        while (currentInfantCount < infantCount) {
            driverTools.clickWithJavascriptExecutor(INFANT_INC);
            currentInfantCount++;
        }
        while (currentInfantCount > infantCount) {
            driverTools.clickWithJavascriptExecutor(INFANT_DEC);
            currentInfantCount--;
        }
    }

    @Override
    public void savePassengers() {
        driverTools.waitForElementToBeClickable(SAVE_DATE_BTN);
        driverTools.clickWithJavascriptExecutor(SAVE_DATE_BTN);
    }
}
