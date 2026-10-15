package com.tiket.web.dashboard.page.flight;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.core.Is.is;

public class FlightCountryDashboardPage<T extends WebDriver> extends BasePage<T> {

    public FlightCountryDashboardPage(Session<T> session) {
        super(session);
    }

    private static final Locator ADD_COUNTRY_BTN = new Locator("ADD_COUNTRY_BTN", new By.ByXPath("//div[contains(@class, 'custom-table__add-button-container')]"));
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[@class='account-card']"));
    private static final Locator FIRST_ROW_COUNTRY = new Locator("FIRST_ROW_COUNTRY", By.xpath("//tr[contains(@class,'MuiTableRow-root row_value')][1]"));
    private static final Locator DELETE_COUNTRY_BTN = new Locator("DELETE_COUNTRY_BTN", By.xpath("//tr[contains(@class,'MuiTableRow-root row_value')][1]//button[contains(@class,'btn-danger')]"));
    private static final Locator CONFIRM_DELETE_BTN = new Locator("CONFIRM_DELETE_BTN", By.xpath("//span[text()='OK']"));
    private static final Locator COUNTRY_TABLE_ROWS = new Locator("COUNTRY_TABLE_ROWS", By.xpath("//tbody/tr"));
    private static final Locator TOTAL_COUNTRY_COUNT = new Locator("TOTAL_COUNTRY_COUNT", By.xpath("//tfoot//p"));
    private static final Locator CODE_FILTER_INPUT = new Locator("CODE_FILTER_INPUT", By.id("custom-table__filter--code"));
    private static final Locator APPLY_FILTER_BTN = new Locator("APPLY_FILTER_BTN", By.cssSelector("button.custom-table__filter--submit"));
    private static final Locator RESET_FILTER_BTN = new Locator("RESET_FILTER_BTN", By.cssSelector("button.custom-table__filter--reset"));
    private static final Locator DATA_NOT_EXIST = new Locator("DATA_NOT_EXIST", By.xpath("//td[text()='Data Not Exist']"));

    public void clickAddCountryBtn() {
        try {
            goToFlightCountryPage();
            driverTools.waitForElementToAppear(ADD_COUNTRY_BTN, 5);
            driverTools.click(ADD_COUNTRY_BTN);
            logger.log("Clicked on Add Country button successfully.");
        } catch (Exception e) {
            logger.fail("Failed to click on Add Country button.");
            throw e;
        }
    }

    public boolean isFlightLoggedIn() {
        driverTools.waitForElementToAppear(USERNAME_GREETING, 3);
        return driverTools.isElementDisplayed(USERNAME_GREETING);
    }

    public boolean checkCountry(String countryCode, String countryName, String countryCurrency) {
        driverTools.waitForElementToAppear(FIRST_ROW_COUNTRY, 4).orElseThrow();
        String AddedCountry = driverTools.getText(FIRST_ROW_COUNTRY).orElseThrow();
        logger.log("Country data: " + AddedCountry);

        return AddedCountry.contains(countryCode) && AddedCountry.contains(countryName) && AddedCountry.contains(countryCurrency);
    }

    public boolean checkCountryDeleted(String countryCode, String countryName, String countryCurrency) {
        driverTools.refreshWebpage();
        driverTools.waitForElementToAppear(FIRST_ROW_COUNTRY, 4).orElseThrow();
        String AddedCountry = driverTools.getText(FIRST_ROW_COUNTRY).orElseThrow();
        logger.log("Added Country: " + AddedCountry);
        return !(AddedCountry.contains(countryCode) && AddedCountry.contains(countryName) && AddedCountry.contains(countryCurrency));
    }

    public void deleteCountry() {
        driverTools.click(DELETE_COUNTRY_BTN);
        driverTools.waitForElementToAppear(CONFIRM_DELETE_BTN);
        driverTools.click(CONFIRM_DELETE_BTN);
    }

    public void deleteCountry(String countryCode) {
        Locator deleteButton = getDeleteButtonLocatorByCountryCode(countryCode);
        driverTools.click(deleteButton);

        driverTools.waitForElementToAppear(CONFIRM_DELETE_BTN);
        driverTools.click(CONFIRM_DELETE_BTN);
    }

    public void deleteCountryIfExists(String countryCode) {
        goToFlightCountryPage();
        driverTools.waitForElementToAppear(CODE_FILTER_INPUT, 5).orElseThrow();
        driverTools.clear(CODE_FILTER_INPUT);
        driverTools.sendKeys(CODE_FILTER_INPUT, countryCode);
        driverTools.click(APPLY_FILTER_BTN);

        Locator countryCodeCell = getCountryCodeCellLocator(countryCode);
        if (driverTools.waitForElementToAppear(countryCodeCell, 5).isPresent()) {
            logger.log("Country code " + countryCode + " is present. Deleting it.");
            deleteCountry(countryCode);
            Assertion.assertThat("Country " + countryCode + " is removed after delete",
                    driverTools.waitForElementToAppear(DATA_NOT_EXIST, 5).isPresent(), is(true));
        } else {
            logger.log("Country code " + countryCode + " is not present.");
            Assertion.assertThat("Filter for country " + countryCode + " shows Data Not Exist",
                    driverTools.waitForElementToAppear(DATA_NOT_EXIST, 5).isPresent(), is(true));
        }

        driverTools.waitForElementToAppear(RESET_FILTER_BTN, 5).orElseThrow();
        driverTools.click(RESET_FILTER_BTN);
    }

    private Locator getCountryCodeCellLocator(String countryCode) {
        return new Locator("COUNTRY_CODE_CELL_" + countryCode.toUpperCase(),
                By.xpath("//tbody[contains(@class,'custom-table__body')]/tr[contains(@class,'row_value')]/td[1][text()='" + countryCode.toUpperCase() + "']"));
    }

    private Locator getDeleteButtonLocatorByCountryCode(String countryCode) {
        return new Locator("DELETE_COUNTRY_" + countryCode.toUpperCase(), By.xpath("//tbody/tr[./td[1 and text()='"+countryCode.toUpperCase()+"']]/td[last()]//button[contains(@class,'btn-danger')]"));
    }

    public void goToFlightCountryPage() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/flight-ms-v2/flight-country");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/flight-ms-v2/flight-country");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://wwww.tiket.com/admin/flight-ms-v2/flight-country");
        }
    }

    public Boolean checkCountryTableIsExist() {
        driverTools.waitForElementToAppear(TOTAL_COUNTRY_COUNT);

        int totalCountryCount = Integer.parseInt(driverTools.getText(TOTAL_COUNTRY_COUNT).orElseThrow().split(" of ")[1]);
        int expectedCountries = Math.min(10, totalCountryCount);
        List<WebElement> tableRowElement = driverTools.elementsUntilExist(COUNTRY_TABLE_ROWS).orElseThrow();

        Assertion.assertThat("Table is showing " + expectedCountries + " countries", tableRowElement.size(), is(expectedCountries));

        return tableRowElement.size() == expectedCountries;
    }

    public void clickUpdateCountryBtn(String countryCode) {
        Locator updateButton = getUpdateButtonLocatorByCountryCode(countryCode);
        driverTools.click(updateButton);
    }

    private Locator getUpdateButtonLocatorByCountryCode(String countryCode) {
        return new Locator("UPDATE_COUNTRY_" + countryCode.toUpperCase(), By.xpath("//tbody/tr[./td[1 and text()='"+countryCode.toUpperCase()+"']]/td[last()]//button[contains(@class,'btn-warning')]"));
    }
}
