package com.tiket.web.page.nft.whoosh.search_page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WhooshSearchPageDWeb<T extends WebDriver> extends WhooshSearchPageWeb<T> {

    public WhooshSearchPageDWeb(Session<T> session) {
        super(session);
    }
    private static final Locator ORIGIN_SEARCH_FIELD = new Locator("ORIGIN_SEARCH_FIELD", new By.ByXPath("(//input[@type='text'])[2]"));

    private static final Locator DESTINATION_SEARCH_FIELD = new Locator("DESTINATION_SEARCH_FIELD", new By.ByXPath("(//input[@type='text'])[2]"));
    private static final Locator SAVE_BTN = new Locator("SAVE BTN", new By.ByXPath("(//button[contains(@class, 'Button_button') and contains(@class, 'Button_variant_primary')])[3]"));

    @Override
    public void selectOrigin(String stationName) {
        driverTools.waitForElementToAppear(ORIGIN);
        driverTools.clickWithJavascriptExecutor(ORIGIN);
        driverTools.waitForElementToAppear(ORIGIN_SEARCH_FIELD);
        driverTools.clickWithJavascriptExecutor(ORIGIN_SEARCH_FIELD);
        driverTools.sendKeys(ORIGIN_SEARCH_FIELD, stationName);
        driverTools.click(FIRST_STATION);
    }

    @Override
    public void selectDestination(String stationName) {
        driverTools.waitForElementToBeClickable(DESTINATION);
        driverTools.click(DESTINATION);
        driverTools.waitForElementToBeClickable(DESTINATION_SEARCH_FIELD);
        driverTools.clickWithJavascriptExecutor(DESTINATION_SEARCH_FIELD);
        driverTools.sendKeys(DESTINATION_SEARCH_FIELD, stationName);
        driverTools.click(FIRST_STATION);
    }

    @Override
    public void selectPassenger(int adultCount, int infantCount) {
        driverTools.waitForElementToBeClickable(PASSENGER_BTN, 20).ifPresent(driverTools::click);

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
        driverTools.waitForElementToBeClickable(SAVE_BTN);
        driverTools.click(SAVE_BTN);
    }
}
