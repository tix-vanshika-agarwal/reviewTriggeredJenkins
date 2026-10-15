package com.tiket.web.dashboard.page.flight;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.*;
import org.testng.thread.IThreadWorkerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class FlightEditRefundMatrixFormPage<T extends WebDriver> extends BasePage<T> {
    public FlightEditRefundMatrixFormPage(Session<T> session) {
        super(session);
    }

    private static final Locator SUBMIT_BTN = new Locator("SUBMIT_BTN", new By.ByXPath("//button[@type='submit']"));
    private static final Locator PRIORITY_DROPDOWN = new Locator("PRIORITY_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='priority']]"));
    private static final Locator PRIORITY_DROPDOWN_SELECTION = new Locator("PRIORITY_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='priority']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator SUPPLIER_CODE_DROPDOWN = new Locator("SUPPLIER_CODE_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='supplierCode']]"));
    private static final Locator SUPPLIER_CODE_DROPDOWN_SELECTION = new Locator("SUPPLIER_CODE_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='supplierCode']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator AIRLINE_CODE_DROPDOWN = new Locator("AIRLINE_CODE_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='airlineCode']]"));
    private static final Locator AIRLINE_CODE_DROPDOWN_SELECTION = new Locator("AIRLINE_CODE_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='airlineCode']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator ORIGINS_DROPDOWN = new Locator("ORIGINS_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='origins']]"));
    private static final Locator ORIGINS_DROPDOWN_SELECTION = new Locator("ORIGINS_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='origins']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator DESTINATIONS_DROPDOWN = new Locator("DESTINATIONS_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='destinations']]"));
    private static final Locator DESTINATIONS_DROPDOWN_SELECTION = new Locator("DESTINATIONS_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='destinations']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator MATRIX_TYPE_DROPDOWN = new Locator("MATRIX_TYPE_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='matrixType']]"));
    private static final Locator MATRIX_TYPE_DROPDOWN_SELECTION = new Locator("MATRIX_TYPE_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='matrixType']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator ORIGIN_COUNTRY_DROPDOWN = new Locator("ORIGIN_COUNTRY_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='originCountry']]"));
    private static final Locator ORIGIN_COUNTRY_DROPDOWN_SELECTION = new Locator("ORIGIN_COUNTRY_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='originCountry']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator DESTINATION_COUNTRY_DROPDOWN = new Locator("DESTINATION_COUNTRY_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='destinationCountry']]"));
    private static final Locator DESTINATION_COUNTRY_DROPDOWN_SELECTION = new Locator("DESTINATION_COUNTRY_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='destinationCountry']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator REFUND_METHOD_DROPDOWN = new Locator("REFUND_METHOD_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='refundMethod']]"));
    private static final Locator REFUND_METHOD_DROPDOWN_SELECTION = new Locator("REFUND_METHOD_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='refundMethod']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator CABIN_CLASSES_DROPDOWN = new Locator("CABIN_CLASSES_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='cabinClasses']]"));
    private static final Locator CABIN_CLASSES_DROPDOWN_SELECTION = new Locator("CABIN_CLASSES_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='cabinClasses']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator FARE_CLASSES_FIELDS = new Locator("FARE_CLASSES_FIELDS", new By.ByXPath("//div[@id='fareClasses']//input"));
    private static final Locator FARE_CLASSES_VALUE = new Locator("FARE_CLASSES_VALUE", new By.ByXPath("(//div[@id='fareClasses']//li)[1]"));
    private static final Locator FARE_BASIS_FIELDS = new Locator("FARE_BASIS_FIELDS", new By.ByXPath("//div[@id='fareBasisCodes']//input"));
    private static final Locator FARE_BASIS_VALUE = new Locator("FARE_BASIS_VALUE", new By.ByXPath("(//div[@id='fareBasisCodes']//li)[1]"));
    private static final Locator REASON_ID_DROPDOWN = new Locator("REASON_ID_DROPDOWN", new By.ByXPath(
            "//input[@id='reasonId']/ancestor::div[contains(@class,'Select-control')][1]//span[contains(@class,'Select-arrow-zone')]"));
    private static final Locator REASON_ID_DROPDOWN_SELECTION = new Locator("REASON_ID_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[@for='reasonId']]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator DESCRIPTION_FIELDS = new Locator("DESCRIPTION_FIELDS", new By.ByXPath("//textarea[@name='description']"));
    private static final Locator ITEM_TYPE_DROPDOWN = new Locator("ITEM_TYPE_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[contains(@for,'itemTypes')]]"));
    private static final Locator ITEM_TYPE_DROPDOWN_SELECTION = new Locator("ITEM_TYPE_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[contains(@for,'itemTypes')]]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator RELATION_OPERATOR_DROPDOWN = new Locator("RELATION_OPERATOR_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[contains(@for,'relationOperator')]]"));
    private static final Locator RELATION_OPERATOR_DROPDOWN_SELECTION = new Locator("RELATION_OPERATOR_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[contains(@for,'relationOperator')]]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator TIME_FRAME_DROPDOWN = new Locator("TIME_FRAME_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[contains(@for,'timeFrame')]]"));
    private static final Locator TIME_FRAME_DROPDOWN_SELECTION = new Locator("TIME_FRAME_DROPDOWN_SELECTION", new By.ByXPath("//div[@class='tixselect' and ./label[contains(@for,'timeFrame')]]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator MINUTES_FIELDS = new Locator("MINUTES_FIELDS", new By.ByXPath("//input[contains(@name,'minutes')]"));
    private static final Locator ADD_FORMULA_BUTTON = new Locator("ADD_FORMULA_BUTTON", new By.ByXPath("//button[@class='MatrixCalculator__Action']"));
    private static final Locator FORMULA_FIELDS = new Locator("FORMULA_FIELDS", new By.ByXPath("//div[@class='FormulaCalculator']//textarea"));
    private static final Locator FORMULA_VALIDATE_SAVE_BUTTON = new Locator("FORMULA_VALIDATE_SAVE_BUTTON", new By.ByXPath("//div[@class='FormulaCalculator__Action']//button"));
    private static final Locator SAVED_FORMULA_TEXT = new Locator("SAVED_FORMULA_TEXT", new By.ByXPath("//div[@class='MatrixCalculator__Selected']"));
    private static final Locator DATE_PICKER_NEXT_MONTH = new Locator("DATE_PICKER_NEXT_MONTH", new By.ByXPath("(//div[@class='MuiPickersCalendarHeader-switchHeader']/button)[2]"));
    private static final Locator DATE_PICKER_PREV_MONTH = new Locator("DATE_PICKER_PREV_MONTH", new By.ByXPath("(//div[@class='MuiPickersCalendarHeader-switchHeader']/button)[1]"));
    private static final Locator DATE_PICKER_OK_BUTTON = new Locator("DATE_PICKER_OK_BUTTON", new By.ByXPath("//button[./span[text()='OK']]"));
    private static final Locator ISSUANCE_START_DATE_FIELDS = new Locator("ISSUANCE_START_DATE_FIELDS", new By.ByXPath("(//div[@class='row wrapper-date'])[1]/div[1]//input"));
    private static final Locator ISSUANCE_END_DATE_FIELDS = new Locator("ISSUANCE_END_DATE_FIELDS", new By.ByXPath("(//div[@class='row wrapper-date'])[1]/div[2]//input"));
    private static final Locator TRAVEL_START_DATE_FIELDS = new Locator("TRAVEL_START_DATE_FIELDS", new By.ByXPath("(//div[@class='row wrapper-date'])[2]/div[1]//input"));
    private static final Locator TRAVEL_END_DATE_FIELDS = new Locator("TRAVEL_END_DATE_FIELDS", new By.ByXPath("(//div[@class='row wrapper-date'])[2]/div[2]//input"));
    private static final Locator FARE_CLASSES_CONTAINER = new Locator("FARE_CLASSES_CONTAINER", new By.ByXPath("//div[@id='fareClasses']/ul[@class='InputTag__List']"));
    private static final Locator FARE_BASIS_CONTAINER = new Locator("FARE_BASIS_CONTAINER", new By.ByXPath("//div[@id='fareBasisCodes']/ul[@class='InputTag__List']"));
    private static final Locator PRIORITY_CLEAR = new Locator("PRIORITY_CLEAR", new By.ByXPath("//div[@class='tixselect' and ./label[@for='priority']]//span[@class='Select-clear']"));
    private static final Locator SUPPLIER_CODE_CLEAR = new Locator("SUPPLIER_CODE_CLEAR", new By.ByXPath("//div[@class='tixselect' and ./label[@for='supplierCode']]//span[@class='Select-clear']"));
    private static final Locator AIRLINE_CODE_CLEAR = new Locator("AIRLINE_CODE_CLEAR", new By.ByXPath("//div[@class='tixselect' and ./label[@for='airlineCode']]//span[@class='Select-clear']"));
    private static final Locator ORIGINS_CLEAR = new Locator("ORIGINS_CLEAR", new By.ByXPath("//div[@class='tixselect' and ./label[@for='origins']]//span[@class='Select-clear']"));
    private static final Locator DESTINATIONS_CLEAR = new Locator("DESTINATIONS_CLEAR", new By.ByXPath("//div[@class='tixselect' and ./label[@for='destinations']]//span[@class='Select-clear']"));
    private static final Locator MATRIX_TYPE_CLEAR = new Locator("MATRIX_TYPE_CLEAR", new By.ByXPath("//div[@class='tixselect' and ./label[@for='matrixType']]//span[@class='Select-clear']"));
    private static final Locator ORIGIN_COUNTRY_CLEAR = new Locator("ORIGIN_COUNTRY_CLEAR", new By.ByXPath("//div[@class='tixselect' and ./label[@for='originCountry']]//span[@class='Select-clear']"));
    private static final Locator DESTINATION_COUNTRY_CLEAR = new Locator("DESTINATION_COUNTRY_CLEAR", new By.ByXPath("//div[@class='tixselect' and ./label[@for='destinationCountry']]//span[@class='Select-clear']"));
    private static final Locator REFUND_METHOD_CLEAR = new Locator("REFUND_METHOD_CLEAR", new By.ByXPath("//div[@class='tixselect' and ./label[@for='refundMethod']]//span[@class='Select-clear']"));
    private static final Locator CABIN_CLASSES_CLEAR = new Locator("CABIN_CLASSES_CLEAR", new By.ByXPath("//div[@class='tixselect' and ./label[@for='cabinClasses']]//span[@class='Select-clear']"));
    private static final Locator REASON_ID_CLEAR = new Locator("REASON_ID_CLEAR", new By.ByXPath("//div[@class='tixselect' and ./label[@for='reasonId']]//span[@class='Select-clear']"));
    private static final Locator FARE_CLASSES_CLEAR = new Locator("FARE_CLASSES_CLEAR", new By.ByXPath("(//div[@id='fareClasses']//button)[1]"));
    private static final Locator FARE_BASIS_CLEAR = new Locator("FARE_BASIS_CLEAR", new By.ByXPath("(//div[@id='fareBasisCodes']//button)[1]"));
    private static final Locator ITEM_TYPES_CLEAR = new Locator("ITEM_TYPES_CLEAR", new By.ByXPath("//div[@class='tixselect' and ./label[@for='itemPenaltyMatrices[0].itemTypes']]//span[@class='Select-clear']"));
    private static final Locator RELATION_OPERATOR_CLEAR = new Locator("RELATION_OPERATOR_CLEAR", new By.ByXPath("//div[@class='tixselect' and ./label[@for='itemPenaltyMatrices[0].penaltyMatrices[0].relationOperator']]//span[@class='Select-clear']"));
    private static final Locator TIME_FRAME_CLEAR = new Locator("TIME_FRAME_CLEAR", new By.ByXPath("//div[@class='tixselect' and ./label[@for='itemPenaltyMatrices[0].penaltyMatrices[0].timeFrame']]//span[@class='Select-clear']"));    

    public Map<String, String> updateDetails(String priority, String supplierCode, String airlineCode, String origins, String destinations, String matrixType, String originCountry, String destinationCountry, String refundMethod, String cabinClasses, String fareClasses, String fareBasis, String reasonId, String description, String startDateRangeIssuance, String dayCountIssuance, String startDateRangeTravel, String dayCountTravel, String itemTypes, String relationOperator, String timeFrame, String minutes, String formula) throws InterruptedException {
        Map<String, String> updatedRefundMatrix = new HashMap<>();

        clearPriority();
        priority = choosePriority(priority);
        updatedRefundMatrix.put("priority", priority);

        clearSupplierCode();
        supplierCode = chooseSupplierCode(supplierCode);
        updatedRefundMatrix.put("supplierCode", supplierCode);

        clearAirlineCode();
        airlineCode = chooseAirlineCode(airlineCode);
        updatedRefundMatrix.put("airlineCode", airlineCode);
        
        clearOrigins();
        origins = chooseOrigins(origins);
        updatedRefundMatrix.put("origins", origins);

        clearDestinations();
        destinations = chooseDestinations(destinations);
        updatedRefundMatrix.put("destinations", destinations);
        
        clearMatrixType();
        matrixType = chooseMatrixType(matrixType);
        updatedRefundMatrix.put("matrixType", matrixType);

        clearOriginCountry();
        originCountry = chooseOriginCountry(originCountry);
        updatedRefundMatrix.put("originCountry", originCountry);

        clearDestinationCountry();
        destinationCountry = chooseDestinationCountry(destinationCountry);
        updatedRefundMatrix.put("destinationCountry", destinationCountry);

        clearRefundMethod();
        refundMethod = chooseRefundMethod(refundMethod);
        updatedRefundMatrix.put("refundMethod", refundMethod);
        
        clearCabinClasses();
        cabinClasses = chooseCabinClasses(cabinClasses);
        updatedRefundMatrix.put("cabinClasses", cabinClasses);

        clearReasonId();
        reasonId = chooseReasonId(reasonId);
        updatedRefundMatrix.put("reasonId", reasonId);

        description = fillDescription(description);
        updatedRefundMatrix.put("description", description);
        
        Map<String, String> issuanceDateRange = fillIssuanceDate(startDateRangeIssuance, dayCountIssuance);
        updatedRefundMatrix.put("startDateIssuance", issuanceDateRange.get("startDate"));
        updatedRefundMatrix.put("endDateIssuance", issuanceDateRange.get("endDate"));

        Map<String, String> travelDateRange = fillTravelDate(startDateRangeTravel, dayCountTravel);
        updatedRefundMatrix.put("startDateTravel", travelDateRange.get("startDate"));
        updatedRefundMatrix.put("endDateTravel", travelDateRange.get("endDate"));

        clearItemTypes();
        itemTypes = chooseItemTypes(itemTypes);
        updatedRefundMatrix.put("itemTypes", itemTypes);

        clearRelationOperator();
        relationOperator = chooseRelationOperator(relationOperator);
        updatedRefundMatrix.put("relationOperator", relationOperator);
        
        clearTimeFrame();
        timeFrame = chooseTimeFrame(timeFrame);
        updatedRefundMatrix.put("timeFrame", timeFrame);

        minutes = fillMinutes(minutes);
        updatedRefundMatrix.put("minutes", minutes);

        formula = fillFormula(formula);
        updatedRefundMatrix.put("formula", formula);

        clearFareClasses();
        fareClasses = fillFareClasses(fareClasses);
        updatedRefundMatrix.put("fareClasses", fareClasses);

        clearFareBasis();
        fareBasis = fillFareBasis(fareBasis);
        updatedRefundMatrix.put("fareBasis", fareBasis);

        return updatedRefundMatrix;
    }

    /**
     * Updates only priority, matrix type, and fare classes — the fields asserted on the grid in
     * {@code testUpdateRefundMatrix}. Leaves every other edit form value as loaded from the server.
     */
    public Map<String, String> updateDetailsForGridVerification(String priority, String matrixType, String fareClasses)
            throws InterruptedException {
        Map<String, String> updatedRefundMatrix = new HashMap<>();

        clearPriority();
        priority = choosePriority(priority);
        updatedRefundMatrix.put("priority", priority);

        clearMatrixType();
        matrixType = chooseMatrixType(matrixType);
        updatedRefundMatrix.put("matrixType", matrixType);

        clearFareClasses();
        fareClasses = fillFareClasses(fareClasses);
        updatedRefundMatrix.put("fareClasses", fareClasses);

        return updatedRefundMatrix;
    }

    private Map<String, String> fillDateRange(String startDateRange, String dayCount, Locator startDateField, Locator endDateField) {
        Map<String, String> result = new HashMap<>();
        String startDate = CoreFunctions.generateRandomDate(startDateRange);
        String endDate = CoreFunctions.addToDateYYYY_MM_DD(startDate, Integer.parseInt(dayCount));

        driverTools.click(startDateField);
        driverTools.click(DATE_PICKER_OK_BUTTON);

        driverTools.click(endDateField);
        driverTools.click(DATE_PICKER_OK_BUTTON);

        String currentStartDate = driverTools.getValue(startDateField);
        String currentEndDate = driverTools.getValue(endDateField);


        int monthDiffsStartDate = CoreFunctions.compareMonth(currentStartDate, startDate, "yyyy-MM-dd");
        int monthDiffsEndDate = CoreFunctions.compareMonth(currentEndDate, endDate, "yyyy-MM-dd");

        driverTools.waitForElementToAppear(startDateField, 1);
        driverTools.click(startDateField);
        if (monthDiffsStartDate < 0) {
            for (int i = 0; i < monthDiffsStartDate * -1; i++) {
                driverTools.waitForElementToAppear(DATE_PICKER_PREV_MONTH, 1);
                driverTools.click(DATE_PICKER_PREV_MONTH);
            }
        } else if (monthDiffsStartDate > 0) {
            for (int i = 0; i < monthDiffsStartDate; i++) {
                driverTools.waitForElementToAppear(DATE_PICKER_NEXT_MONTH, 1);
                driverTools.click(DATE_PICKER_NEXT_MONTH);
            }
        }
        driverTools.waitForElementToAppear(getDatePicker(startDate.split("-")[2]));
        driverTools.click(getDatePicker(startDate.split("-")[2]));
        driverTools.waitForElementToAppear(DATE_PICKER_OK_BUTTON, 1);
        driverTools.click(DATE_PICKER_OK_BUTTON);

        driverTools.waitForElementToAppear(endDateField, 3);
        driverTools.click(endDateField);
        if (monthDiffsEndDate < 0) {
            for (int i = 0; i < monthDiffsEndDate * -1; i++) {
                driverTools.waitForElementToAppear(DATE_PICKER_PREV_MONTH, 1);
                driverTools.click(DATE_PICKER_PREV_MONTH);
            }
        } else if (monthDiffsEndDate > 0) {
            for (int i = 0; i < monthDiffsEndDate; i++) {
                driverTools.waitForElementToAppear(DATE_PICKER_NEXT_MONTH, 1);
                driverTools.click(DATE_PICKER_NEXT_MONTH);
            }
        }
        driverTools.waitForElementToAppear(getDatePicker(endDate.split("-")[2]));
        driverTools.click(getDatePicker(endDate.split("-")[2]));
        driverTools.waitForElementToAppear(DATE_PICKER_OK_BUTTON, 1);
        driverTools.click(DATE_PICKER_OK_BUTTON);

        result.put("startDate", startDate);
        result.put("endDate", endDate);
        return result;
    }

    private Locator getDatePicker(String date) {
        return new Locator("DATE_PICKER", new By.ByXPath("(//button[contains(@class,'MuiPickersDay-day') and not(contains(@class,'hidden'))])["+date+"]"));
    }

    private String chooseFromDropdown(String value, Locator dropdownLocator, Locator selectionLocator, String fieldName) {
        driverTools.waitForElementToAppear(dropdownLocator, 1);
        driverTools.click(dropdownLocator);

        if (!driverTools.waitForElementToAppear(selectionLocator, 1).isPresent()) {
            driverTools.clickElementByCoordinateDWeb(dropdownLocator);
        }
        
        List<WebElement> selections = driverTools.elementsUntilExist(selectionLocator).orElseThrow();
        WebElement targetElement = null;
        String targetText = "";
        
        if (value.equalsIgnoreCase("random")) {
            int targetIndex = CoreFunctions.generateRandomInt(0, selections.size() - 1);
            targetElement = selections.get(targetIndex);
            targetText = driverTools.getText(targetElement);
            driverTools.click(targetElement);
        } else {
            for (WebElement e : selections) {
                String elementText = driverTools.getText(e);
                if (elementText.equalsIgnoreCase(value)) {
                    targetText = elementText;
                    try {
                        driverTools.click(e);
                    } catch (ElementClickInterceptedException | StaleElementReferenceException er1) {
                        driverTools.clickElementByCoordinateDWeb(e);
                    }

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

    private String choosePriority(String priority) {
        return chooseFromDropdown(priority, PRIORITY_DROPDOWN, PRIORITY_DROPDOWN_SELECTION, "priority");
    }

    private String chooseSupplierCode(String supplierCode) {
        return chooseFromDropdown(supplierCode, SUPPLIER_CODE_DROPDOWN, SUPPLIER_CODE_DROPDOWN_SELECTION, "supplier code");
    }

    private String chooseAirlineCode(String airlineCode) {
        return chooseFromDropdown(airlineCode, AIRLINE_CODE_DROPDOWN, AIRLINE_CODE_DROPDOWN_SELECTION, "airline code");
    }

    private String chooseOrigins(String origins) {
        return chooseFromDropdown(origins, ORIGINS_DROPDOWN, ORIGINS_DROPDOWN_SELECTION, "origins");
    }

    private String chooseDestinations(String destinations) {
        return chooseFromDropdown(destinations, DESTINATIONS_DROPDOWN, DESTINATIONS_DROPDOWN_SELECTION, "destinations");
    }

    private String chooseMatrixType(String matrixType) {
        return chooseFromDropdown(matrixType, MATRIX_TYPE_DROPDOWN, MATRIX_TYPE_DROPDOWN_SELECTION, "matrixType");
    }

    private String chooseOriginCountry(String originCountry) {
        return chooseFromDropdown(originCountry, ORIGIN_COUNTRY_DROPDOWN, ORIGIN_COUNTRY_DROPDOWN_SELECTION, "originCountry");
    }

    private String chooseDestinationCountry(String destinationCountry) {
        return chooseFromDropdown(destinationCountry, DESTINATION_COUNTRY_DROPDOWN, DESTINATION_COUNTRY_DROPDOWN_SELECTION, "destinationCountry");
    }

    private String chooseRefundMethod(String refundMethod) {
        return chooseFromDropdown(refundMethod, REFUND_METHOD_DROPDOWN, REFUND_METHOD_DROPDOWN_SELECTION, "refundMethod");
    }

    private String chooseCabinClasses(String cabinClasses) {
        return chooseFromDropdown(cabinClasses, CABIN_CLASSES_DROPDOWN, CABIN_CLASSES_DROPDOWN_SELECTION, "cabinClasses");
    }

    private String chooseReasonId(String reasonId) {
        return chooseFromDropdown(reasonId, REASON_ID_DROPDOWN, REASON_ID_DROPDOWN_SELECTION, "reasonId");
    }

    private String chooseItemTypes(String itemType) {
        return chooseFromDropdown(itemType, ITEM_TYPE_DROPDOWN, ITEM_TYPE_DROPDOWN_SELECTION, "itemType");
    }

    private String chooseRelationOperator(String relationOperator) {
        return chooseFromDropdown(relationOperator, RELATION_OPERATOR_DROPDOWN, RELATION_OPERATOR_DROPDOWN_SELECTION, "relationOperator");
    }

    private String chooseTimeFrame(String timeFrame) {
        return chooseFromDropdown(timeFrame, TIME_FRAME_DROPDOWN, TIME_FRAME_DROPDOWN_SELECTION, "timeFrame");
    }

    private Map<String, String> fillIssuanceDate(String startDateRangeIssuance, String dayCountIssuance) {
        return fillDateRange(startDateRangeIssuance, dayCountIssuance, ISSUANCE_START_DATE_FIELDS, ISSUANCE_END_DATE_FIELDS);
    }

    private Map<String, String> fillTravelDate(String startDateRangeTravel, String dayCountTravel) {
        return fillDateRange(startDateRangeTravel, dayCountTravel, TRAVEL_START_DATE_FIELDS, TRAVEL_END_DATE_FIELDS);
    }

    private String fillFareClasses(String fareClasses) {
        driverTools.clickElementByCoordinateDWeb(FARE_CLASSES_FIELDS);
        driverTools.sendKeys(FARE_CLASSES_FIELDS, fareClasses);
        driverTools.sendKeys(FARE_CLASSES_FIELDS, String.valueOf(Keys.TAB));

        return driverTools.getValue(FARE_CLASSES_VALUE);
    }

    private String fillFareBasis(String fareBasis) {
        driverTools.clickElementByCoordinateDWeb(FARE_BASIS_CONTAINER);
        driverTools.sendKeys(FARE_BASIS_FIELDS, fareBasis);
        driverTools.sendKeys(FARE_BASIS_FIELDS, String.valueOf(Keys.TAB));

        return driverTools.getValue(FARE_BASIS_VALUE);
    }

    private String fillDescription(String description) {
        driverTools.click(DESCRIPTION_FIELDS);
        driverTools.clearInput(driverTools.element(DESCRIPTION_FIELDS).orElseThrow());
        driverTools.sendKeys(DESCRIPTION_FIELDS, description);

        return driverTools.getText(DESCRIPTION_FIELDS).get();
    }

    private String fillMinutes(String minutes) {
        try {
            Integer.parseInt(minutes);
            driverTools.clearInput(driverTools.element(MINUTES_FIELDS).orElseThrow());
            driverTools.sendKeys(MINUTES_FIELDS, minutes);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Minutes on testdata is not numeric : '"+minutes+"'");
        }

        return driverTools.getValue(MINUTES_FIELDS);
    }

    private String fillFormula(String formula) {
        driverTools.click(ADD_FORMULA_BUTTON);
        driverTools.clearInput(driverTools.element(FORMULA_FIELDS).orElseThrow());
        driverTools.sendKeys(FORMULA_FIELDS, formula);
        driverTools.click(FORMULA_VALIDATE_SAVE_BUTTON);

        return driverTools.getText(SAVED_FORMULA_TEXT).get();
    }

    private void clearPriority() {
        driverTools.click(PRIORITY_CLEAR);
    }

    private void clearSupplierCode() {
        driverTools.click(SUPPLIER_CODE_CLEAR);
    }

    private void clearAirlineCode() {
        driverTools.click(AIRLINE_CODE_CLEAR);
    }

    private void clearOrigins() {
        driverTools.click(ORIGINS_CLEAR);
    }

    private void clearDestinations() {
        driverTools.click(DESTINATIONS_CLEAR);
    }

    private void clearMatrixType() {
        driverTools.click(MATRIX_TYPE_CLEAR);
    }

    private void clearOriginCountry() {
        driverTools.click(ORIGIN_COUNTRY_CLEAR);
    }

    private void clearDestinationCountry() {
        driverTools.click(DESTINATION_COUNTRY_CLEAR);
    }

    private void clearRefundMethod() {
        driverTools.click(REFUND_METHOD_CLEAR);
    }

    private void clearCabinClasses() {
        driverTools.click(CABIN_CLASSES_CLEAR);
    }

    private void clearFareClasses() {
        driverTools.click(FARE_CLASSES_CLEAR);
    }

    private void clearFareBasis() {
        driverTools.click(FARE_BASIS_CLEAR);
    }

    private void clearReasonId() {
        driverTools.click(REASON_ID_CLEAR);
    }

    private void clearItemTypes() {
        driverTools.click(ITEM_TYPES_CLEAR);
    }

    private void clearRelationOperator() {
        driverTools.click(RELATION_OPERATOR_CLEAR);
    }

    private void clearTimeFrame() {
        driverTools.click(TIME_FRAME_CLEAR);
    }

    public void clickSubmitButton() {
        driverTools.waitForElementToAppear(SUBMIT_BTN, 500);
        driverTools.click(SUBMIT_BTN);
    }
} 