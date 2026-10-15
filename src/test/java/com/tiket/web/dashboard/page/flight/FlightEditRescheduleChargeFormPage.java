package com.tiket.web.dashboard.page.flight;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class FlightEditRescheduleChargeFormPage<T extends WebDriver> extends BasePage<T> {
    public FlightEditRescheduleChargeFormPage(Session<T> session) {
        super(session);
    }

    private static final Locator LOADING_OVERLAY = new Locator("LOADING_OVERLAY", new By.ByXPath("//div[contains(@class,'loading-overlay')]"));
    private static final Locator SUBMIT_BTN = new Locator("SUBMIT_BTN", new By.ByXPath("//button[@type='submit']"));
    private static final Locator AIRLINE_CODE_DROPDOWN = new Locator("AIRLINE_CODE_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='airlineCodes']]"));
    private static final Locator AIRLINE_CODE_INPUT = new Locator("AIRLINE_CODE_INPUT", new By.ByXPath("//div[@class='tixselect' and ./label[@for='airlineCodes']]//div[contains(@class,'Select-input')]//input"));
    private static final Locator AIRLINE_CODE_DROPDOWN_SELECTION = new Locator("AIRLINE_CODE_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='airlineCodes']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator AIRLINE_CODE_CLEAR_INPUT = new Locator("AIRLINE_CODE_CLEAR_INPUT", new By.ByXPath("//div[@class='tixselect' and ./label[@for='airlineCodes']]//span[@class='Select-clear']"));
    private static final Locator SUPPLIER_CODE_DROPDOWN = new Locator("SUPPLIER_CODE_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='supplierCode']]"));
    private static final Locator SUPPLIER_CODE_INPUT = new Locator("SUPPLIER_CODE_INPUT", new By.ByXPath("//div[@class='tixselect' and ./label[@for='supplierCode']]//div[contains(@class,'Select-input')]//input"));
    private static final Locator SUPPLIER_CODE_DROPDOWN_SELECTION = new Locator("SUPPLIER_CODE_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='supplierCode']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator SUPPLIER_CODE_CLEAR_INPUT = new Locator("SUPPLIER_CODE_CLEAR_INPUT", new By.ByXPath("//div[@class='tixselect' and ./label[@for='supplierCode']]//span[@class='Select-clear']"));
    private static final Locator CURRENCY_FIELDS = new Locator("CURRENCY_FIELDS", new By.ByXPath("//input[@name='currency']"));
    private static final Locator CHARGE_TYPE_DROPDOWN = new Locator("CHARGE_TYPE_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='type']]"));
    private static final Locator CHARGE_TYPE_DROPDOWN_SELECTION = new Locator("CHARGE_TYPE_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='type']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator VALUE_FIELDS = new Locator("VALUE_FIELDS", new By.ByXPath("//input[@name='value']"));
    private static final Locator JOURNEY_TYPE_DROPDOWN = new Locator("JOURNEY_TYPE_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='journeyType']]"));
    private static final Locator JOURNEY_TYPE_DROPDOWN_SELECTION = new Locator("JOURNEY_TYPE_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='journeyType']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator ROUTE_DROPDOWN = new Locator("ROUTE_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='route']]"));
    private static final Locator ROUTE_DROPDOWN_SELECTION = new Locator("ROUTE_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='route']]//div[contains(@class,'VirtualizedSelectOption')]"));

    public Map<String, String> updateDetails(Map<String, String> createdRescheduleCharge, String updatedAirlineCodes, String updatedSupplierCode, String updatedCurrency, String updatedChargeType, String updatedValue, String updatedJourneyType, String updatedRoute) throws InterruptedException {
        Map<String, String> updatedRescheduleCharge = new HashMap<>();

        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 10);
        
        driverTools.waitForElementToBeClickable(AIRLINE_CODE_DROPDOWN, 5).orElseThrow();

        clearAirlineCodeInput();
        updatedAirlineCodes = chooseAirlineCode(updatedAirlineCodes);
        updatedRescheduleCharge.put("airlineCodes", updatedAirlineCodes);

        clearSupplierCodeInput();
        updatedSupplierCode = chooseSupplierCode(updatedSupplierCode);
        updatedRescheduleCharge.put("supplierCode", updatedSupplierCode);

        clearCurrencyInput();
        updatedCurrency = fillCurrency(updatedCurrency);
        updatedRescheduleCharge.put("currency", updatedCurrency);

        updatedChargeType = chooseChargeType(updatedChargeType);
        updatedRescheduleCharge.put("chargeType", updatedChargeType);

        clearValueInput();
        updatedValue = fillValue(updatedValue);
        updatedRescheduleCharge.put("value", updatedValue);

        updatedJourneyType = chooseJourneyType(updatedJourneyType);
        updatedRescheduleCharge.put("journeyType", updatedJourneyType);

        updatedRoute = chooseRoute(updatedRoute);
        updatedRescheduleCharge.put("route", updatedRoute);

        return updatedRescheduleCharge;
    }

    private String chooseFromDropdown(String value, Locator dropdownLocator, Locator selectionLocator, String fieldName) {
        return chooseFromDropdown(value, dropdownLocator, selectionLocator, null, fieldName);
    }

    /**
     * Options are virtualized, so an option picked by raw index may sit outside the visible menu and the click
     * is lost. When the dropdown is searchable (inputLocator != null) the option text is typed and confirmed
     * with ENTER, same as the add form; otherwise the option is scrolled into view before clicking.
     */
    private String chooseFromDropdown(String value, Locator dropdownLocator, Locator selectionLocator, Locator inputLocator, String fieldName) {
        driverTools.waitForElementToAppear(dropdownLocator, 5);
        driverTools.click(dropdownLocator);

        driverTools.waitForElementToAppear(selectionLocator);
        List<WebElement> selections = driverTools.elementsUntilExist(selectionLocator).orElseThrow();
        WebElement targetElement = null;
        String targetText = "";

        if (value.equalsIgnoreCase("random")) {
            int targetIndex = CoreFunctions.generateRandomInt(1, selections.size() - 1);
            targetElement = selections.get(targetIndex);
            targetText = driverTools.getText(targetElement);
            logger.log("Selecting " + fieldName + ": " + targetText);
            if (inputLocator != null) {
                WebElement input = driverTools.waitForElementToAppear(inputLocator, 5).orElseThrow();
                driverTools.sendKeys(input, targetText);
                input.sendKeys(Keys.ENTER);
            } else {
                clickOption(targetElement);
            }
        } else {
            for (WebElement e : selections) {
                String elementText = driverTools.getText(e);
                if (elementText.equalsIgnoreCase(value)) {
                    targetText = elementText;
                    clickOption(e);
                    targetElement = e;
                    break;
                }
            }

            if (targetElement == null) {
                throw new NoSuchElementException("There is no '" + value + "' " + fieldName);
            }
        }

        return targetText;
    }

    private void clickOption(WebElement option) {
        driverTools.scrollIntoWebView(option);
        driverTools.click(option);
    }

    private String fillInputField(String value, Locator fieldLocator, boolean isNumeric) {
        if (isNumeric) {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Value on testdata is not numeric : '" + value + "'");
            }
        }
        driverTools.sendKeys(fieldLocator, value);
        return driverTools.getValue(fieldLocator);
    }

    private void clearInputField(Locator fieldLocator) {
        driverTools.waitForElementToAppear(fieldLocator, 1);
        driverTools.clear(fieldLocator);
    }

    private String chooseRoute(String route) {
        return chooseFromDropdown(route, ROUTE_DROPDOWN, ROUTE_DROPDOWN_SELECTION, "route selection");
    }

    private String chooseJourneyType(String journeyType) {
        return chooseFromDropdown(journeyType, JOURNEY_TYPE_DROPDOWN, JOURNEY_TYPE_DROPDOWN_SELECTION, "journey type");
    }

    private String fillValue(String value) {
        return fillInputField(value, VALUE_FIELDS, true);
    }

    private String chooseChargeType(String chargeType) {
        return chooseFromDropdown(chargeType, CHARGE_TYPE_DROPDOWN, CHARGE_TYPE_DROPDOWN_SELECTION, "charge type");
    }

    private String fillCurrency(String currency) {
        return fillInputField(currency, CURRENCY_FIELDS, false);
    }

    private String chooseSupplierCode(String supplierCode) {
        return chooseFromDropdown(supplierCode, SUPPLIER_CODE_DROPDOWN, SUPPLIER_CODE_DROPDOWN_SELECTION, SUPPLIER_CODE_INPUT, "supplier code");
    }

    private String chooseAirlineCode(String airlineCodes) {
        return chooseFromDropdown(airlineCodes, AIRLINE_CODE_DROPDOWN, AIRLINE_CODE_DROPDOWN_SELECTION, AIRLINE_CODE_INPUT, "airline code");
    }

    private void clearAirlineCodeInput() {
        driverTools.waitForElementToAppear(AIRLINE_CODE_CLEAR_INPUT, 1);
        driverTools.click(AIRLINE_CODE_CLEAR_INPUT);
    }

    private void clearSupplierCodeInput() {
        driverTools.waitForElementToAppear(SUPPLIER_CODE_CLEAR_INPUT, 1);
        driverTools.click(SUPPLIER_CODE_CLEAR_INPUT);
    }

    private void clearCurrencyInput() {
        clearInputField(CURRENCY_FIELDS);
    }

    private void clearValueInput() {
        clearInputField(VALUE_FIELDS);
    }

    public void clickSubmitButton() {
        driverTools.waitForElementToAppear(SUBMIT_BTN, 5);
        driverTools.click(SUBMIT_BTN);
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 5);
    }
} 