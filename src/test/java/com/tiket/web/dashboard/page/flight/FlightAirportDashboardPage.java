package com.tiket.web.dashboard.page.flight;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;

public class FlightAirportDashboardPage<T extends WebDriver> extends BasePage<T> {

    public FlightAirportDashboardPage(Session<T> session) {
        super(session);
    }

    private static final Locator ADD_AIRPORT_BTN = new Locator("ADD_AIRPORT_BTN", By.xpath("//button[contains(@class, 'custom-table__buttonadd')]//span[text()='Add New']"));
    private static final Locator CONFIRM_DELETE_BTN = new Locator("CONFIRM_DELETE_BTN", By.xpath("//span[text()='OK']"));
    private static final Locator NEW_ADDED_AIRPORT = new Locator("NEW_ADDED_COUNTRY", By.xpath("//tr[contains(@class,'MuiTableRow-root') and contains(@class,'row_value')][1]"));
    private static final Locator DELETE_AIRPORT_BTN = new Locator("DELETE_AIRPORT_BTN", By.xpath("//tr[contains(@class,'MuiTableRow-root') and contains(@class,'row_value')][1]//button[contains(@class,'btn-danger')][.//i[contains(@class,'fa-trash')]]"));
    private static final Locator DETAIL_BTN = new Locator("DETAIL_BTN", By.xpath("(//button[contains(@class,'btn-detail')])[1]"));
    private static final Locator CANCEL_BTN = new Locator("CANCEL_BTN", By.xpath("//button[.//span[text()='Cancel']]"));
    private static final Locator CODE_FILTER_INPUT = new Locator("CODE_FILTER_INPUT", By.id("custom-table__filter--code"));
    private static final Locator APPLY_FILTER_BTN = new Locator("APPLY_FILTER_BTN", By.cssSelector("button.custom-table__filter--submit"));
    private static final Locator RESET_FILTER_BTN = new Locator("RESET_FILTER_BTN", By.cssSelector("button.custom-table__filter--reset"));
    private static final Locator DATA_NOT_EXIST = new Locator("DATA_NOT_EXIST", By.xpath("//td[text()='Data Not Exist']"));
    private static final Locator LOADING_OVERLAY = new Locator("LOADING_OVERLAY", By.cssSelector("div.loading-overlay"));

    public void clickAddCountryBtn() {
        waitForLoadingOverlayToDisappear();
        driverTools.waitForElementToAppear(ADD_AIRPORT_BTN, 5);
        driverTools.click(ADD_AIRPORT_BTN);
        logger.log("Clicked on Add Airport button successfully.");
    }

    public void clickAirport(){
        try {
            if (Configs.ENVIRONMENT == Environment.GK) {
                driverTools.get("https://gatotkaca.tiket.com/admin/flight-ms-v2/airport");
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                driverTools.get("https://preprod.tiket.com/admin/flight-ms-v2/airport");
            } else if (Configs.ENVIRONMENT == Environment.PROD) {
                driverTools.get("https://www.tiket.com/admin/flight-ms-v2/airport");
            }
            logger.log("Navigated to Airport Page.");
        } catch (Exception e) {
            logger.fail("Failed to Navigated to Airport Page.");
            throw e;
        }
    }

    public boolean checkAddedAirport(String code, String content) {
        waitForLoadingOverlayToDisappear();
        driverTools.waitForElementToAppear(NEW_ADDED_AIRPORT, 10).orElseThrow();
        String AddedCountry = driverTools.getText(NEW_ADDED_AIRPORT).orElseThrow();
        logger.log("Added Airport: " + AddedCountry);
        logger.log("code: " + code);
        logger.log("content: " + content);
        logger.log("contains code: " + AddedCountry.contains(code));
        logger.log("contains content: " + AddedCountry.contains(content));

        return AddedCountry.contains(code) && AddedCountry.contains(content);
    }

    public boolean checkAirportDeleted(String code, String content) {
        driverTools.refreshWebpage();
        driverTools.waitForElementToAppear(NEW_ADDED_AIRPORT, 4).orElseThrow();
        String AddedCountry = driverTools.getText(NEW_ADDED_AIRPORT).orElseThrow();
        logger.log("Added Country: " + AddedCountry);
        return !(AddedCountry.contains(code) && AddedCountry.contains(content));
    }

    public boolean isAirportPresent(String airportCode) {
        waitForLoadingOverlayToDisappear();
        driverTools.waitForElementToAppear(CODE_FILTER_INPUT, 5).orElseThrow();
        driverTools.clear(CODE_FILTER_INPUT);
        driverTools.sendKeys(CODE_FILTER_INPUT, airportCode);
        clickFilterButton(APPLY_FILTER_BTN);
        waitForLoadingOverlayToDisappear();
        boolean isPresent = driverTools.waitForElementToAppear(getMatchingAirportRowLocator(airportCode), 5).isPresent();

        driverTools.waitForElementToAppear(RESET_FILTER_BTN, 5).orElseThrow();
        clickFilterButton(RESET_FILTER_BTN);
        waitForLoadingOverlayToDisappear();
        return isPresent;
    }

    public void deleteAirportIfExists(String airportCode) {
        waitForLoadingOverlayToDisappear();
        driverTools.waitForElementToAppear(CODE_FILTER_INPUT, 5).orElseThrow();
        driverTools.clear(CODE_FILTER_INPUT);
        driverTools.sendKeys(CODE_FILTER_INPUT, airportCode);
        clickFilterButton(APPLY_FILTER_BTN);
        waitForLoadingOverlayToDisappear();

        Locator matchingAirportRow = getMatchingAirportRowLocator(airportCode);
        if (!driverTools.waitForElementToAppear(matchingAirportRow, 5).isPresent()) {
            logger.log("Airport matching filter '" + airportCode + "' is not present.");
            driverTools.waitForElementToAppear(DATA_NOT_EXIST, 5);
        } else {
            int maxDeletes = 10;
            while (maxDeletes-- > 0 && driverTools.isElementDisplayed(matchingAirportRow)) {
                logger.log("Airport matching filter '" + airportCode + "' is present. Deleting it.");
                deleteAirport(airportCode);
                if (driverTools.waitForElementToAppear(DATA_NOT_EXIST, 3).isPresent()) {
                    break;
                }
            }
        }

        driverTools.waitForElementToAppear(RESET_FILTER_BTN, 5).orElseThrow();
        clickFilterButton(RESET_FILTER_BTN);
        waitForLoadingOverlayToDisappear();
    }

    public void deleteAirport(String airportCode) {
        waitForLoadingOverlayToDisappear();
        Locator deleteButton = getDeleteButtonLocatorByAirportCode(airportCode);
        driverTools.waitForElementToAppear(deleteButton, 10).orElseThrow(() ->
                new IllegalStateException("Delete button for airport '" + airportCode + "' is not shown, the page may still be on the form"));
        clickFilterButton(deleteButton);
        driverTools.waitForElementToAppear(CONFIRM_DELETE_BTN);
        driverTools.click(CONFIRM_DELETE_BTN);
        waitForLoadingOverlayToDisappear();
    }

    private void waitForLoadingOverlayToDisappear() {
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 15);
    }

    private void clickFilterButton(Locator button) {
        waitForLoadingOverlayToDisappear();
        driverTools.scrollIntoWebViewOptional(button);
        try {
            driverTools.click(button);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(button);
        }
    }

    private Locator getMatchingAirportRowLocator(String airportCode) {
        String code = airportCode.toUpperCase();
        return new Locator("MATCHING_AIRPORT_ROW_" + code,
                By.xpath("//tbody[contains(@class,'custom-table__body')]/tr[contains(@class,'row_value')][./td[4][translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='" + code + "']]"));
    }

    private Locator getDeleteButtonLocatorByAirportCode(String airportCode) {
        String code = airportCode.toUpperCase();
        return new Locator("DELETE_AIRPORT_" + code,
                By.xpath("//tbody[contains(@class,'custom-table__body')]/tr[contains(@class,'row_value')][./td[4][translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='" + code + "']]/td[last()]//button[contains(@class,'btn-danger')][.//i[contains(@class,'fa-trash')]]"));
    }

    public void readAirport(){
        driverTools.click(DETAIL_BTN);
        driverTools.scrollToElement(CANCEL_BTN);
        driverTools.click(CANCEL_BTN);
    }
}