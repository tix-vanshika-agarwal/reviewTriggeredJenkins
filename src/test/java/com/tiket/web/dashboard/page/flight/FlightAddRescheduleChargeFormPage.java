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

public class FlightAddRescheduleChargeFormPage<T extends WebDriver> extends BasePage<T> {
    public FlightAddRescheduleChargeFormPage(Session<T> session) {
        super(session);
    }

    private static final Locator LOADING_OVERLAY = new Locator("LOADING_OVERLAY", new By.ByXPath("//div[contains(@class,'loading-overlay')]"));
    private static final Locator SUBMIT_BTN = new Locator("SUBMIT_BTN", new By.ByXPath("//button[@type='submit']"));
    private static final Locator AIRLINE_CODE_DROPDOWN = new Locator("AIRLINE_CODE_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='airlineCodes']]"));
    private static final Locator AIRLINE_CODE_INPUT = new Locator("AIRLINE_CODE_INPUT", new By.ByXPath("//div[@class='tixselect' and ./label[@for='airlineCodes']]//div[contains(@class,'Select-input')]//input"));
    private static final Locator AIRLINE_CODE_DROPDOWN_SELECTION = new Locator("AIRLINE_CODE_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='airlineCodes']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator SUPPLIER_CODE_DROPDOWN = new Locator("SUPPLIER_CODE_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='supplierCode']]"));
    private static final Locator SUPPLIER_CODE_INPUT = new Locator("SUPPLIER_CODE_INPUT", new By.ByXPath("//div[@class='tixselect' and ./label[@for='supplierCode']]//div[contains(@class,'Select-input')]//input"));
    private static final Locator SUPPLIER_CODE_DROPDOWN_SELECTION = new Locator("SUPPLIER_CODE_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='supplierCode']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator CURRENCY_FIELDS = new Locator("CURRENCY_FIELDS", new By.ByXPath("//input[@name='currency']"));
    private static final Locator CHARGE_TYPE_DROPDOWN = new Locator("CHARGE_TYPE_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='type']]"));
    private static final Locator CHARGE_TYPE_DROPDOWN_SELECTION = new Locator("CHARGE_TYPE_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='type']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator VALUE_FIELDS = new Locator("VALUE_FIELDS", new By.ByXPath("//input[@name='value']"));
    private static final Locator JOURNEY_TYPE_DROPDOWN = new Locator("JOURNEY_TYPE_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='journeyType']]"));
    private static final Locator JOURNEY_TYPE_DROPDOWN_SELECTION = new Locator("JOURNEY_TYPE_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='journeyType']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator ROUTE_DROPDOWN = new Locator("ROUTE_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='route']]"));
    private static final Locator ROUTE_DROPDOWN_SELECTION = new Locator("ROUTE_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='route']]//div[contains(@class,'VirtualizedSelectOption')]"));

    public Map<String, String> fillDetails(String airlineCodes, String supplierCode, String currency, String chargeType, String value, String journeyType, String route) throws InterruptedException {
        Map<String, String> createdCampaign = new HashMap<>();

        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 10);
        
        driverTools.waitForElementToBeClickable(AIRLINE_CODE_DROPDOWN, 5).orElseThrow();

        airlineCodes = chooseAirlineCode(airlineCodes);
        createdCampaign.put("airlineCodes", airlineCodes);

        supplierCode = chooseSupplierCode(supplierCode);
        createdCampaign.put("supplierCode", supplierCode);

        currency = fillCurrency(currency);
        createdCampaign.put("currency", currency);

        chargeType = chooseChargeType(chargeType);
        createdCampaign.put("chargeType", chargeType);

        value = fillValue(value);
        createdCampaign.put("value", value);

        journeyType = chooseJourneyType(journeyType);
        createdCampaign.put("journeyType", journeyType);

        route = chooseRoute(route);
        createdCampaign.put("route", route);

        return createdCampaign;
    }

    private String chooseRoute(String route) {
        driverTools.waitForElementToAppear(ROUTE_DROPDOWN,1);
        driverTools.click(ROUTE_DROPDOWN);

        driverTools.waitForElementToAppear(ROUTE_DROPDOWN_SELECTION);
        List<WebElement> routeSelection = driverTools.elementsUntilExist(ROUTE_DROPDOWN_SELECTION).orElseThrow();
        WebElement targetElement = null;
        String targetText = "";
        if (route.equalsIgnoreCase("random")) {
            int targetIndex = CoreFunctions.generateRandomInt(0,routeSelection.size()-1);
            logger.log("targetIndex : " + targetIndex);
            targetElement = routeSelection.get(targetIndex);
            targetText = driverTools.getText(targetElement);
            targetElement.click();
        } else {
            for (WebElement e : routeSelection) {
                targetText = driverTools.getText(e);
                if (targetText.equalsIgnoreCase(route)) {
                    driverTools.click(e);
                    targetElement = e;
                    break;
                }
            }

            if (targetElement == null) {
                throw new NoSuchElementException("There is no '"+route+"' route selection");
            }
        }

        return targetText;
    }

    private String chooseJourneyType(String journeyType) {
        driverTools.waitForElementToAppear(JOURNEY_TYPE_DROPDOWN,1);
        driverTools.click(JOURNEY_TYPE_DROPDOWN);

        driverTools.waitForElementToAppear(JOURNEY_TYPE_DROPDOWN_SELECTION);
        List<WebElement> journeyTypeSelection = driverTools.elementsUntilExist(JOURNEY_TYPE_DROPDOWN_SELECTION).orElseThrow();
        WebElement targetElement = null;
        String targetText = "";
        if (journeyType.equalsIgnoreCase("random")) {
            int targetIndex = CoreFunctions.generateRandomInt(0,journeyTypeSelection.size()-1);
            targetElement = journeyTypeSelection.get(targetIndex);
            targetText = driverTools.getText(targetElement);
            targetElement.click();
        } else {
            for (WebElement e : journeyTypeSelection) {
                targetText = driverTools.getText(e);
                if (targetText.equalsIgnoreCase(journeyType)) {
                    driverTools.click(e);
                    targetElement = e;
                    break;
                }
            }

            if (targetElement == null) {
                throw new NoSuchElementException("There is no '"+journeyType+"' journey type");
            }
        }

        return targetText;
    }

    private String fillValue(String value) {
        try {
            Integer.parseInt(value);
            driverTools.sendKeys(VALUE_FIELDS, value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Value on testdata is not numeric : '"+value+"'");
        }

        return driverTools.getValue(VALUE_FIELDS);
    }

    private String chooseChargeType(String chargeType) {
        driverTools.waitForElementToAppear(CHARGE_TYPE_DROPDOWN,1);
        driverTools.click(CHARGE_TYPE_DROPDOWN);

        driverTools.waitForElementToAppear(CHARGE_TYPE_DROPDOWN_SELECTION);
        List<WebElement> chargeTypeSelection = driverTools.elementsUntilExist(CHARGE_TYPE_DROPDOWN_SELECTION).orElseThrow();
        WebElement targetElement = null;
        String targetText = "";
        if (chargeType.equalsIgnoreCase("random")) {
            int targetIndex = CoreFunctions.generateRandomInt(0,chargeTypeSelection.size()-1);
            targetElement = chargeTypeSelection.get(targetIndex);
            targetText = driverTools.getText(targetElement);
            targetElement.click();
        } else {
            for (WebElement e : chargeTypeSelection) {
                targetText = driverTools.getText(e);
                if (targetText.equalsIgnoreCase(chargeType)) {
                    driverTools.click(e);
                    targetElement = e;
                    break;
                }
            }

            if (targetElement == null) {
                throw new NoSuchElementException("There is no '"+chargeType+"' charge type");
            }
        }

        return targetText;
    }

    private String fillCurrency(String currency) {
        driverTools.sendKeys(CURRENCY_FIELDS, currency);
        return driverTools.getValue(CURRENCY_FIELDS);
    }

    private String chooseSupplierCode(String supplierCode) {
        WebElement supplierCodeDropdown = driverTools.waitForElementToAppear(SUPPLIER_CODE_DROPDOWN,5).orElseThrow();
        driverTools.click(supplierCodeDropdown);

        driverTools.waitForElementToAppear(SUPPLIER_CODE_DROPDOWN_SELECTION);
        List<WebElement> supplierCodeSelection = driverTools.elementsUntilExist(SUPPLIER_CODE_DROPDOWN_SELECTION).orElseThrow();
        WebElement targetElement = null;
        String targetText = "";
        if (supplierCode.equalsIgnoreCase("random")) {
            int targetIndex = CoreFunctions.generateRandomInt(1,supplierCodeSelection.size()-1);
            targetElement = supplierCodeSelection.get(targetIndex);
            targetText = driverTools.getText(targetElement);
            WebElement supplierCodeInput = driverTools.waitForElementToAppear(SUPPLIER_CODE_INPUT, 5).orElseThrow();
            driverTools.sendKeys(supplierCodeInput, targetText);
            supplierCodeInput.sendKeys(Keys.ENTER);
        } else {
            for (WebElement e : supplierCodeSelection) {
                targetText = driverTools.getText(e);
                if (targetText.equalsIgnoreCase(supplierCode)) {
                    driverTools.click(e);
                    targetElement = e;
                    break;
                }
            }

            if (targetElement == null) {
                throw new NoSuchElementException("There is no '"+supplierCode+"' supplier code");
            }
        }

        return targetText;
    }

    private String chooseAirlineCode(String airlineCodes) {
        WebElement airlineCodeDropdown = driverTools.waitForElementToAppear(AIRLINE_CODE_DROPDOWN,5).orElseThrow();
        driverTools.click(airlineCodeDropdown);

        driverTools.waitForElementToAppear(AIRLINE_CODE_DROPDOWN_SELECTION);
        List<WebElement> airlineCodeSelection = driverTools.elementsUntilExist(AIRLINE_CODE_DROPDOWN_SELECTION).orElseThrow();
        WebElement targetElement = null;
        String targetText = "";
        if (airlineCodes.equalsIgnoreCase("random")) {
            int targetIndex = CoreFunctions.generateRandomInt(1,airlineCodeSelection.size()-1);
            targetElement = airlineCodeSelection.get(targetIndex);
            targetText = driverTools.getText(targetElement);
            WebElement airlineCodeInput = driverTools.waitForElementToAppear(AIRLINE_CODE_INPUT, 5).orElseThrow();
            driverTools.sendKeys(airlineCodeInput, targetText);
            airlineCodeInput.sendKeys(Keys.ENTER);
        } else {
            for (WebElement e : airlineCodeSelection) {
                targetText = driverTools.getText(e);
                if (targetText.equalsIgnoreCase(airlineCodes)) {
                    driverTools.click(e);
                    targetElement = e;
                    break;
                }
            }

            if (targetElement == null) {
                throw new NoSuchElementException("There is no '"+airlineCodes+"' airline code");
            }
        }

        return targetText;
    }

    public void clickSubmitButton() {
        driverTools.waitForElementToAppear(SUBMIT_BTN, 5);
        driverTools.click(SUBMIT_BTN);
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 5);
    }
}
