package com.tiket.web.dashboard.page.flight;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.core.Is.is;

public class FlightAirlineDashboardPage<T extends WebDriver> extends BasePage<T> {
    public FlightAirlineDashboardPage(Session<T> session) {
        super(session);
    }
    private static final Locator ADD_AIRLINE_BTN = new Locator("ADD_AIRLINE_BTN", By.xpath("//div[contains(@class,'add-button-container')]"));
    private static final Locator UPDATE_AIRLINE_BTN = new Locator("UPDATE_AIRLINE_BTN", By.xpath("(//button[contains(@class,'btn-warning')])[1]"));
    private static final Locator CONFIRM_DELETE_BTN = new Locator("CONFIRM_DELETE_BTN", By.xpath("//button[contains(@class,'btn-primary') and .//span[normalize-space()='OK']]"));
    private static final Locator NEW_ADDED_AIRLINE = new Locator("NEW_ADDED_AIRLINE", By.xpath("(//tr[contains(@class,'MuiTableRow-root row_value')])[1]"));
    private static final Locator READ_BTN = new Locator("READ_BTN", By.xpath("(//button[contains(@class, 'btn-detail')]//i[contains(@class, 'fa-eye')])[1]"));
    private static final Locator CANCEL_BUTTON = new Locator("CANCEL_BUTTON", By.xpath("//button[.//span[text()='Cancel']]"));
    private static final Locator CODE_FILTER_INPUT = new Locator("CODE_FILTER_INPUT", By.id("custom-table__filter--code"));
    private static final Locator APPLY_FILTER_BTN = new Locator("APPLY_FILTER_BTN", By.cssSelector("button.custom-table__filter--submit"));
    private static final Locator RESET_FILTER_BTN = new Locator("RESET_FILTER_BTN", By.cssSelector("button.custom-table__filter--reset"));
    private static final Locator DATA_NOT_EXIST = new Locator("DATA_NOT_EXIST", By.xpath("//td[text()='Data Not Exist']"));


    public void clickAddAirlineBtn() {
        try {
            if (Configs.ENVIRONMENT == Environment.GK) {
                driver.get("https://gatotkaca.tiket.com/admin/flight-ms-v2/airline");
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                driver.get("https://preprod.tiket.com/admin/flight-ms-v2/airline");
            } else if (Configs.ENVIRONMENT == Environment.PROD) {
                driver.get("https://www.tiket.com/admin/flight-ms-v2/airline");
            }
            driverTools.waitForElementToAppear(ADD_AIRLINE_BTN, 5);
            driverTools.click(ADD_AIRLINE_BTN);
            logger.log("Clicked on Add Airline button successfully.");
        } catch (Exception e) {
            logger.fail("Failed to click on Add Airline button.");
            throw e;
        }
    }

    public void clickUpdateAirlineBtn() {
        driverTools.waitForElementToAppear(UPDATE_AIRLINE_BTN, 5);
        driverTools.click(UPDATE_AIRLINE_BTN);
    }

    public void deleteAirlineIfExists(String airlineCode) {
        clickAirlinePage();
        driverTools.waitForElementToAppear(CODE_FILTER_INPUT, 5).orElseThrow();
        driverTools.clear(CODE_FILTER_INPUT);
        driverTools.sendKeys(CODE_FILTER_INPUT, airlineCode);
        clickWithInterceptFallback(APPLY_FILTER_BTN);

        Locator matchingAirlineRow = getMatchingAirlineRowLocator(airlineCode);
        if (driverTools.waitForElementToAppear(matchingAirlineRow, 5).isPresent()) {
            logger.log("Airline code " + airlineCode + " is present. Deleting it.");
            deleteAirline(airlineCode);
            Assertion.assertThat("Airline " + airlineCode + " is removed after delete",
                    driverTools.waitForElementToAppear(DATA_NOT_EXIST, 5).isPresent(), is(true));
        } else {
            logger.log("Airline code " + airlineCode + " is not present.");
            Assertion.assertThat("Filter for airline " + airlineCode + " shows Data Not Exist",
                    driverTools.waitForElementToAppear(DATA_NOT_EXIST, 5).isPresent(), is(true));
        }

        driverTools.waitForElementToAppear(RESET_FILTER_BTN, 5).orElseThrow();
        clickWithInterceptFallback(RESET_FILTER_BTN);
    }

    public void deleteAirline(String airlineCode) {
        clickWithInterceptFallback(getDeleteButtonLocatorByAirlineCode(airlineCode));
        driverTools.waitForElementToAppear(CONFIRM_DELETE_BTN, 5);
        driverTools.click(CONFIRM_DELETE_BTN);
    }

    public boolean isAirlineDeleted(String airlineCode) {
        clickAirlinePage();
        driverTools.waitForElementToAppear(CODE_FILTER_INPUT, 5).orElseThrow();
        driverTools.clear(CODE_FILTER_INPUT);
        driverTools.sendKeys(CODE_FILTER_INPUT, airlineCode);
        clickWithInterceptFallback(APPLY_FILTER_BTN);

        boolean dataNotExist = driverTools.waitForElementToAppear(DATA_NOT_EXIST, 5).isPresent();
        boolean matchingRowPresent = driverTools.isElementDisplayed(getMatchingAirlineRowLocator(airlineCode));
        logger.log("Airline code " + airlineCode + " Data Not Exist: " + dataNotExist + ", matching row present: " + matchingRowPresent);
        return dataNotExist && !matchingRowPresent;
    }

    private void clickWithInterceptFallback(Locator button) {
        driverTools.scrollIntoWebViewOptional(button);
        try {
            driverTools.click(button);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(button);
        }
    }

    private Locator getMatchingAirlineRowLocator(String airlineCode) {
        String code = airlineCode.toUpperCase();
        return new Locator("MATCHING_AIRLINE_ROW_" + code,
                By.xpath("//tbody[contains(@class,'custom-table__body')]/tr[contains(@class,'row_value')][./td[2][translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='" + code + "']]"));
    }

    private Locator getDeleteButtonLocatorByAirlineCode(String airlineCode) {
        String code = airlineCode.toUpperCase();
        return new Locator("DELETE_AIRLINE_" + code,
                By.xpath("//tbody[contains(@class,'custom-table__body')]/tr[contains(@class,'row_value')][./td[2][translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='" + code + "']]/td[last()]//button[contains(@class,'btn-danger')][.//i[contains(@class,'fa-trash')]]"));
    }

    public String getNewAddedAirline() {
        driverTools.waitForElementToAppear(NEW_ADDED_AIRLINE, 5).orElseThrow();
        String airlineName = driverTools.getText(NEW_ADDED_AIRLINE).orElseThrow();
        logger.log("Newly added airline name: " + airlineName);
        return airlineName;
    }

    public boolean isAirlineAdded(String airlineName, String airlineCode, String airlineDisplayName, String airlineShortName, String addedAirline) {
        return addedAirline.contains(airlineName) && addedAirline.contains(airlineCode) && addedAirline.contains(airlineDisplayName) && addedAirline.contains(airlineShortName);
    }

    private static final Locator AIRLINE_DETAILS = new Locator("AIRLINE_DETAILS", By.xpath("//h2[contains(text(),'Airline Detail')]/ancestor::div[contains(@class,'modal--container')]//table"));

    public void clickAirlinePage() {
        try {
            if (Configs.ENVIRONMENT == Environment.GK) {
                driver.get("https://gatotkaca.tiket.com/admin/flight-ms-v2/airline");
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                driver.get("https://preprod.tiket.com/admin/flight-ms-v2/airline");
            }
            else if (Configs.ENVIRONMENT == Environment.PROD) {
                driver.get("https://www.tiket.com/admin/flight-ms-v2/airline");
            }
            logger.log("Navigated to Airline page successfully.");
        } catch (Exception e) {
            logger.fail("Failed to navigate to airline page.");
            throw e;
        }
    }

    public boolean checkAirlineDetailsText(String airlineName, String airlineCode, String airlineDisplayName, String airlineShortName) {
        WebElement table = driver.findElement(AIRLINE_DETAILS.by());
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        StringBuilder details = new StringBuilder();

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("th"));
            if (cells.size() == 2) {
                String key = cells.get(0).getText().trim();
                String value;
                WebElement valueCell = cells.get(1);
                if (!valueCell.findElements(By.tagName("img")).isEmpty()) {
                    value = valueCell.findElement(By.tagName("img")).getAttribute("alt");
                } else if (!valueCell.findElements(By.tagName("span")).isEmpty()) {
                    value = valueCell.getText().trim();
                } else {
                    value = valueCell.getText().trim();
                }
                details.append(key).append(": ").append(value).append("\n");
            }
        }
        String airlineDetails = details.toString();
        return airlineDetails.contains(airlineName) && airlineDetails.contains(airlineCode) && airlineDetails.contains(airlineDisplayName) && airlineDetails.contains(airlineShortName);
    }

    public void readAirline(){
        driverTools.click(READ_BTN);
    }

    public void cancelReadAirline(){
        driverTools.click(CANCEL_BUTTON);
    }
    
}
