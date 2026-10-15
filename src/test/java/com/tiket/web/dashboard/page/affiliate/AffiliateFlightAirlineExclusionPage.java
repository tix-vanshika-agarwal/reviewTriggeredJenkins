package com.tiket.web.dashboard.page.affiliate;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AffiliateFlightAirlineExclusionPage<T extends WebDriver> extends BasePage<T> {

    public AffiliateFlightAirlineExclusionPage(Session<T> session) {
        super(session);
    }
    private static final Locator SUPPLIER_SEARCH_INPUT = new Locator("SUPPLIER_SEARCH_INPUT", By.id("search-by-iata-or-supplier-name"));
    private static final Locator AIRLINES_EXCLUSION_SEARCH_BUTTON = new Locator("AIRLINES_EXCLUSION_SEARCH_BUTTON", By.xpath("//button[normalize-space()='Search']"));
    private static final Locator RESET_SEARCH_BUTTON = new Locator("RESET_SEARCH_BUTTON", By.xpath("//button[normalize-space(text())='Reset Search']"));
    private static final Locator ADD_EXCLUSION_BUTTON = new Locator("ADD_EXCLUSION_BUTTON", By.xpath("//button[normalize-space(text())='Add Exclusion']"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", By.xpath("//button[normalize-space()='Delete']"));
    private static final Locator DIALOG_BODY_CONTAINER = new Locator("DIALOG_BODY_CONTAINER", By.xpath("(//section[contains(@class, 'Figure_figure')])"));
    private static final Locator AIRLINE_CHECKBOX = new Locator("AIRLINE_CHECKBOX", By.xpath("(//span[contains(@class, 'Check_custom_checkbox')])[1]"));
    private static final Locator ADD_AIRLINE_BUTTON = new Locator("ADD_AIRLINE_BUTTON", By.xpath("(//button[contains(@class, 'Button_variant_primary')])[2]"));
    private static final Locator SELECT_SUPPLIER_DROPDOWN = new Locator("SELECT_SUPPLIER_DROPDOWN", By.xpath("//div[@class='css-1xc3v61-indicatorContainer']"));
    private static final Locator INPUT_SUPPLIER = new Locator("INPUT_SUPPLIER", By.xpath("//*[text()='Citilink']"));
    private static final Locator SNACKBAR_AIRLINE_ADDED = new Locator("SNACKBAR_AIRLINE_ADDED", By.xpath("//div[contains(@class, 'Snackbar_text_content') and normalize-space(text())='Successfully Added Airline Setting']"));
    private static final Locator SNACKBAR_AIRLINE_DELETED = new Locator("SNACKBAR_AIRLINE_DELETED", By.xpath("//div[contains(@class, 'Snackbar_text_content') and normalize-space(text())='Successfully Deleted Airline Setting']"));
    private static final Locator AIRLINE_DROPDOWN_INPUT = new Locator("AIRLINE_DROPDOWN_INPUT", By.xpath("//input[@id='select-airline']"));
    private static final Locator YES_BUTTON = new Locator("YES_BUTTON", By.xpath("//button[normalize-space()='Yes']"));

    public void clickSearchBtn(String supplierSearchInput) {
        try {
            driverTools.waitForElementToAppear(SUPPLIER_SEARCH_INPUT, 5);
            driverTools.click(SUPPLIER_SEARCH_INPUT);
            driverTools.sendKeys(SUPPLIER_SEARCH_INPUT, supplierSearchInput);
            driverTools.click(AIRLINES_EXCLUSION_SEARCH_BUTTON);
            logger.log("Clicked on Supplier Search button successfully.");
        } catch (Exception e) {
            logger.fail("Failed to click on Supplier Search button.");
            throw e;
        }
    }

    public boolean checkSearchedAirline(String expectedSupplier, String expectedIataCode) {
        Locator supplierCell = new Locator("SUPPLIER_CELL",
                By.xpath("//td[normalize-space(text())='" + expectedSupplier + "']"));

        Locator iataCell = new Locator("IATA_CELL",
                By.xpath("//td[normalize-space(text())='" + expectedSupplier + "']/following-sibling::td[1][normalize-space(text())='" + expectedIataCode + "']"));

        driverTools.waitForElementToAppear(supplierCell, 4).orElseThrow();
        driverTools.waitForElementToAppear(iataCell, 4).orElseThrow();
        String actualSupplier = driverTools.getText(supplierCell).orElseThrow();
        String actualIataCode = driverTools.getText(iataCell).orElseThrow();
        logger.log("Found Airline: Supplier = " + actualSupplier + ", IATA Code = " + actualIataCode);
        return actualSupplier.equals(expectedSupplier) && actualIataCode.equals(expectedIataCode);
    }

    public void clickResetBtn() {
        try {
            driverTools.waitForElementToAppear(RESET_SEARCH_BUTTON, 5);
            driverTools.click(RESET_SEARCH_BUTTON);
            logger.log("Clicked on Reset button successfully.");
        } catch (Exception e) {
            logger.fail("Failed to click on Reset Search button.");
            throw e;
        }
    }

    public boolean checkResetSearchButton(){
        driverTools.waitForElementToAppear(DELETE_BUTTON, 5).orElseThrow();

        // Get all visible Delete buttons
        List<WebElement> deleteButtons = driverTools.elements(DELETE_BUTTON).orElseThrow();
        logger.log("Delete buttons found after reset: " + deleteButtons.size());

        // Assert more than 1 result exists
        return deleteButtons.size() > 1;
    }

    public void clickAddExclusionButton(){
        try {
            driverTools.waitForElementToAppear(ADD_EXCLUSION_BUTTON, 5);
            driverTools.click(ADD_EXCLUSION_BUTTON);
            logger.log("Clicked on Add button successfully.");
        } catch (Exception e) {
            logger.fail("Failed to click on Add Search button.");
            throw e;
        }
    }

    public void fillAddExclusionForm() {
        driverTools.waitForElementToAppear(SELECT_SUPPLIER_DROPDOWN, 5);
        driverTools.click(SELECT_SUPPLIER_DROPDOWN);
        driverTools.click(INPUT_SUPPLIER);
        driverTools.click(AIRLINE_DROPDOWN_INPUT);
        driverTools.waitForElementToAppear(AIRLINE_CHECKBOX, 5);
        driverTools.click(AIRLINE_CHECKBOX);
        driverTools.click(DIALOG_BODY_CONTAINER);
        logger.log("Filled Add Exclusion form with Airline: ");
    }

    public void clickAddAirlineButton(){
        try {
            driverTools.waitForElementToAppear(ADD_AIRLINE_BUTTON, 5);
            driverTools.click(ADD_AIRLINE_BUTTON);
            logger.log("Clicked on Add Airline button successfully.");
        } catch (Exception e) {
            logger.fail("Failed to click on Add Airline button.");
            throw e;
        }
    }

    public boolean isAirlineExclusionAdded() {
        driverTools.waitForElementToAppear(SNACKBAR_AIRLINE_ADDED, 5).orElseThrow();
        String snackbarText = driverTools.getText(SNACKBAR_AIRLINE_ADDED).orElseThrow();
        logger.log("Snackbar text: " + snackbarText);
        return snackbarText.contains("Successfully Added Airline Setting");
    }

    public boolean clickDeleteExclusionButton() {
        try {
            driverTools.waitForElementToAppear(DELETE_BUTTON, 5);
            driverTools.click(DELETE_BUTTON);
            boolean isDeleted = confirmDeletion();
            logger.log("Clicked on Delete button successfully.");
            return isDeleted;
        } catch (Exception e) {
            logger.fail("Failed to click on Delete button.");
            return false;
        }
    }

    private boolean confirmDeletion() {
        driverTools.waitForElementToAppear(YES_BUTTON, 5);
        driverTools.click(YES_BUTTON);
        logger.log("Confirmed deletion of exclusion.");
        return driverTools.waitForElementToAppear(SNACKBAR_AIRLINE_DELETED, 5).isPresent();
    }
}
