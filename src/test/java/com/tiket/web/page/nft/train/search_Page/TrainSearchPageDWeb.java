package com.tiket.web.page.nft.train.search_Page;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TrainSearchPageDWeb <T extends WebDriver> extends TrainSearchPageWeb<T> {

    public TrainSearchPageDWeb(Session<T> session) {
        super(session);
    }

    private static final Locator ORIGIN = new Locator("ORIGIN", new By.ByXPath("(//span[contains(@class, 'Location')])[1]"));
    private static final Locator ORIGIN_SEARCH_FIELD = new Locator("ORIGIN_SEARCH_FIELD", new By.ByXPath("(//input[@type='text'])[2]"));
    private static final Locator FIRST_STATION = new Locator("FIRST_STATION", new By.ByXPath("(//*[contains(@class, 'List_right_side')])[1]"));
    private static final Locator DESTINATION = new Locator("DESTINATION", new By.ByXPath("(//span[contains(@class, 'Location')])[2]"));
    private static final Locator DESTINATION_SEARCH_FIELD = new Locator("DESTINATION_SEARCH_FIELD", new By.ByXPath("(//input[@type='text'])[2]"));
    private static final Locator DEPART_DATE_CALENDAR = new Locator("DEPART_DATE_CALENDAR", new By.ByXPath("//span[contains(@class, 'Date')]"));


    @Override
    public void selectOrigin(String stationName) {
        driverTools.waitForElementToAppear(ORIGIN).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(ORIGIN_SEARCH_FIELD).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.sendKeys(ORIGIN_SEARCH_FIELD, stationName);
        driverTools.waitForElementToAppear(FIRST_STATION);
        driverTools.click(FIRST_STATION);
    }

    @Override
    public void selectDestination(String stationName) {
        driverTools.waitForElementToAppear(DESTINATION).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(DESTINATION_SEARCH_FIELD).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.sendKeys(DESTINATION_SEARCH_FIELD, stationName);
        driverTools.waitForElementToAppear(FIRST_STATION);
        driverTools.click(FIRST_STATION);
    }

    @Override
    public void selectDate(String departureDate) {
        driverTools.clickWithJavascriptExecutor(DEPART_DATE_CALENDAR);
        String year = CoreFunctions.getSectionOfString(departureDate," ", 2);
        String monthEng = CoreFunctions.getSectionOfString(departureDate," ", 1);
        String monthId = CoreFunctions.monthEngToId(monthEng);
        String day= CoreFunctions.getSectionOfString(departureDate," ", 0);
        Locator dateLocator = getDateLocator(day, monthEng, monthId, year);
        driverTools.clickWithJavascriptExecutor(dateLocator);
    }

    private Locator getDateLocator(String day, String monthEng, String monthId, String year) {
        return new Locator("DATE_LOCATOR", new By.ByXPath("(//span[contains(@aria-label, '" + day + " " + monthEng + " " + year + "') or contains(@aria-label, '" + day + " " + monthId + " " + year + "')])[1]"));
    }
}
