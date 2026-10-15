package com.tiket.web.dashboard.page.flight;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FlightAddRefundMatrixFormPage<T extends WebDriver> extends BasePage<T> {
    public FlightAddRefundMatrixFormPage(Session<T> session) {
        super(session);
    }

    /**
     * Invalid selects get {@code label.error}; the tag inputs (fare classes, fare basis, branded fare) get
     * {@code InputTag isError}. Both are read via the parent, which holds the field name and the message together —
     * the message is what separates a value the panel never received ("Data tidak boleh kosong") from one it
     * received and rejected on a business rule.
     */
    private static final String REJECTED_FIELD_XPATH = "//label[contains(@class,'error')]/.. "+"| //div[contains(@class,'InputTag') and contains(@class,'isError')]/..";
    /** Pulls the field id out of a dropdown locator, which is always anchored on the label's {@code @for} — exact for
     * most fields, {@code contains} for the three formula ones whose ids are indexed. */
    private static final Pattern LABEL_FOR_IN_XPATH = Pattern.compile("@for[^'\"]*['\"]([^'\"]+)['\"]");
    private static final Locator SUBMIT_BTN = new Locator("SUBMIT_BTN", new By.ByXPath("//button[@type='submit']"));
    private static final Locator REFUND_MATRIX_LIST_ADD_BTN = new Locator("REFUND_MATRIX_LIST_ADD_BTN", new By.ByXPath("(//div[@class='custom-table__add-button-container']//button)[last()]"));
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
    private static final Locator REASON_ID_DROPDOWN = new Locator("REASON_ID_DROPDOWN", new By.ByXPath("//div[@class='tixselect' and ./label[@for='reasonId']]//span[contains(@class,'Select-arrow-zone')]"));
    private static final Locator REASON_ID_SELECT_ENABLED = new Locator("REASON_ID_SELECT_ENABLED", new By.ByXPath("//div[@class='tixselect' and ./label[@for='reasonId']]/div[contains(@class,'select-wrapper') and not(contains(@class,'is-disabled'))]"));
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
    private static final int MAX_DATE_PICK_ATTEMPTS = 3;
    private static final Locator DATE_PICKER_NEXT_MONTH = new Locator("DATE_PICKER_NEXT_MONTH", new By.ByXPath("(//div[@class='MuiPickersCalendarHeader-switchHeader']/button)[2]"));
    private static final Locator DATE_PICKER_PREV_MONTH = new Locator("DATE_PICKER_PREV_MONTH", new By.ByXPath("(//div[@class='MuiPickersCalendarHeader-switchHeader']/button)[1]"));
    private static final Locator DATE_PICKER_OK_BUTTON = new Locator("DATE_PICKER_OK_BUTTON", new By.ByXPath("//button[./span[text()='OK']]"));
    private static final Locator ISSUANCE_START_DATE_FIELDS = new Locator("ISSUANCE_START_DATE_FIELDS", new By.ByXPath("(//div[@class='row wrapper-date'])[1]/div[1]//input"));
    private static final Locator ISSUANCE_END_DATE_FIELDS = new Locator("ISSUANCE_END_DATE_FIELDS", new By.ByXPath("(//div[@class='row wrapper-date'])[1]/div[2]//input"));
    private static final Locator TRAVEL_START_DATE_FIELDS = new Locator("TRAVEL_START_DATE_FIELDS", new By.ByXPath("(//div[@class='row wrapper-date'])[2]/div[1]//input"));
    private static final Locator TRAVEL_END_DATE_FIELDS = new Locator("TRAVEL_END_DATE_FIELDS", new By.ByXPath("(//div[@class='row wrapper-date'])[2]/div[2]//input"));
    private static final Locator FARE_CLASSES_CONTAINER = new Locator("FARE_CLASSES_CONTAINER", new By.ByXPath("//div[@id='fareClasses']"));
    private static final Locator FARE_BASIS_CONTAINER = new Locator("FARE_BASIS_CONTAINER", new By.ByXPath("//div[@id='fareBasisCodes']"));
    private static final Locator BRANDED_FARE = new Locator("BRANDED_FARE", new By.ByXPath("//div[@id='brandedFare']//input[@type='text']"));
    private static final Locator BRANDED_FARE_CLASS = new Locator("BRANDED_FARE_CLASS", new By.ByXPath("//div[@id='brandedFare']"));
    /** Hint line under the label — easier to click than {@code InputTag__Label} when the tag container intercepts clicks. */
    private static final Locator BRANDED_FARE_INFO = new Locator("BRANDED_FARE_INFO",
            new By.ByXPath("//div[@id='brandedFare']//span[contains(@class,'InputTag__Info')][contains(.,'Branded Fare')]"));

    public Map<String, String> fillDetails(String priority, String supplierCode, String airlineCode, String origins, String destinations, String matrixType, String originCountry, String destinationCountry, String refundMethod, String cabinClasses, String fareClasses, String fareBasis, String reasonId, String description, String startDateRangeIssuance, String dayCountIssuance, String startDateRangeTravel, String dayCountTravel, String itemTypes, String relationOperator, String timeFrame, String minutes, String formula) throws InterruptedException {
        Map<String, String> createdRefundMatrix = new HashMap<>();

        Thread.sleep(500);
        priority = choosePriority(priority);
        createdRefundMatrix.put("priority", priority);

        supplierCode = chooseSupplierCode(supplierCode);
        createdRefundMatrix.put("supplierCode", supplierCode);

        airlineCode = chooseAirlineCode(airlineCode);
        createdRefundMatrix.put("airlineCode", airlineCode);
        
        origins = chooseOrigins(origins);
        createdRefundMatrix.put("origins", origins);

        destinations = chooseDestinations(destinations);
        createdRefundMatrix.put("destinations", destinations);
        
        matrixType = chooseMatrixType(matrixType);
        createdRefundMatrix.put("matrixType", matrixType);

        originCountry = chooseOriginCountry(originCountry);
        createdRefundMatrix.put("originCountry", originCountry);
        
        destinationCountry = chooseDestinationCountry(destinationCountry);
        createdRefundMatrix.put("destinationCountry", destinationCountry);

        refundMethod = chooseRefundMethod(refundMethod);
        createdRefundMatrix.put("refundMethod", refundMethod);
        Thread.sleep(500);

        cabinClasses = chooseCabinClasses(cabinClasses);
        createdRefundMatrix.put("cabinClasses", cabinClasses);
        Thread.sleep(500);

        fareClasses = fillFareClasses(fareClasses);
        logger.log("Selected fare classes: " + fareClasses);
        createdRefundMatrix.put("fareClasses", fareClasses);
        Thread.sleep(500);

        fareBasis = fillFareBasis(fareBasis);
        createdRefundMatrix.put("fareBasis", fareBasis);

        fillBrandedFare("123");

        reasonId = chooseReasonId(reasonId);
        createdRefundMatrix.put("reasonId", reasonId);

        description = fillDescription(description);
        createdRefundMatrix.put("description", description);
        
        Map<String, String> issuanceDateRange = fillIssuanceDate(startDateRangeIssuance, dayCountIssuance);
        createdRefundMatrix.put("startDateIssuance", issuanceDateRange.get("startDate"));
        createdRefundMatrix.put("endDateIssuance", issuanceDateRange.get("endDate"));

        Map<String, String> travelDateRange = fillTravelDate(startDateRangeTravel, dayCountTravel);
        createdRefundMatrix.put("startDateTravel", travelDateRange.get("startDate"));
        createdRefundMatrix.put("endDateTravel", travelDateRange.get("endDate"));

        itemTypes = chooseItemTypes(itemTypes);
        createdRefundMatrix.put("itemTypes", itemTypes);

        relationOperator = chooseRelationOperator(relationOperator);
        createdRefundMatrix.put("relationOperator", relationOperator);
        
        timeFrame = chooseTimeFrame(timeFrame);
        createdRefundMatrix.put("timeFrame", timeFrame);

        minutes = fillMinutes(minutes);
        createdRefundMatrix.put("minutes", minutes);

        formula = fillFormula(formula);
        createdRefundMatrix.put("formula", formula);
        return createdRefundMatrix;
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
        int monthDiffsEndDate = CoreFunctions.compareMonth(startDate, endDate, "yyyy-MM-dd");

        pickDate(startDateField, startDate, monthDiffsStartDate);
        pickDate(endDateField, endDate, monthDiffsEndDate);

        result.put("startDate", startDate);
        result.put("endDate", endDate);
        return result;
    }

    /**
     * Days are clicked by their position in the calendar. Clicking while the month slide is still running can land
     * on a neighbouring day, so wait for the target month to be the only one rendered, and read the field back
     * afterwards: a wrong pick is redone instead of being reported as the created value.
     */
    private void pickDate(Locator dateField, String date, int monthDiff) {
        for (int attempt = 1; ; attempt++) {
            driverTools.waitForElementToAppear(dateField, 3);
            driverTools.click(dateField);
            moveMonths(monthDiff);
            waitForMonthToSettle(date);
            driverTools.click(getDatePicker(date.split("-")[2]));
            driverTools.waitForElementToAppear(DATE_PICKER_OK_BUTTON, 1);
            driverTools.click(DATE_PICKER_OK_BUTTON);

            String selectedDate = driverTools.getValue(dateField);
            if (date.equals(selectedDate)) {
                return;
            }
            if (attempt >= MAX_DATE_PICK_ATTEMPTS) {
                throw new IllegalStateException("Date field shows '" + selectedDate + "' instead of '" + date + "'");
            }
            logger.warn("Date field shows '" + selectedDate + "' instead of '" + date + "', picking it again");
            monthDiff = CoreFunctions.compareMonth(selectedDate, date, "yyyy-MM-dd");
        }
    }

    private void moveMonths(int monthDiff) {
        Locator monthButton = monthDiff < 0 ? DATE_PICKER_PREV_MONTH : DATE_PICKER_NEXT_MONTH;
        for (int i = 0; i < Math.abs(monthDiff); i++) {
            driverTools.waitForElementToAppear(monthButton, 1);
            driverTools.click(monthButton);
        }
    }

    private void waitForMonthToSettle(String date) {
        int daysInMonth = LocalDate.parse(date).lengthOfMonth();
        By dayButtons = By.xpath("//button[contains(@class,'MuiPickersDay-day') and not(contains(@class,'hidden'))]");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3)).until(d -> d.findElements(dayButtons).size() == daysInMonth);
        } catch (TimeoutException e) {
            logger.warn("Calendar did not settle on a single month of " + daysInMonth + " days before picking " + date);
        }
    }

    private Locator getDatePicker(String date) {
        return new Locator("DATE_PICKER", new By.ByXPath("(//button[contains(@class,'MuiPickersDay-day') and not(contains(@class,'hidden'))])["+date+"]"));
    }

    private String chooseFromDropdown(String value, Locator dropdownLocator, Locator selectionLocator, String fieldName) {
        driverTools.waitForElementToAppear(dropdownLocator, 1);
        driverTools.clickElementByCoordinateDWeb(dropdownLocator);

        if (!driverTools.waitForElementToAppear(selectionLocator, 1).isPresent()) {
            driverTools.clickElementByCoordinateDWeb(dropdownLocator);
        }
        
        List<WebElement> selections = driverTools.elementsUntilExist(selectionLocator).orElseThrow();
        String targetText;

        if (value.equalsIgnoreCase("random")) {
            targetText = driverTools.getText(selections.get(CoreFunctions.generateRandomInt(0, selections.size() - 1)));
        } else {
            targetText = selections.stream()
                    .map(driverTools::getText)
                    .filter(optionText -> optionText.equalsIgnoreCase(value))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("There is no '" + value + "' " + fieldName));
        }

        selectOption(dropdownLocator, selectionLocator, targetText, fieldName);
        verifySelectionCommitted(dropdownLocator, fieldName, targetText);
        return targetText;
    }

    /**
     * Types the option and confirms with Enter where the field allows it, and clicks it where it does not. The option
     * list is a react-virtualized grid, and on CI a click on one of the lower options is reported as successful while
     * react-select never registers a selection — which left required fields empty and had the panel refuse the submit
     * (Jenkins FLG-Panel-Regression 543-545). Keyboard selection does not care where the option is rendered, so it is
     * used wherever there is an input to type into.
     *
     * <p>Matrix Type, Refund Method, Relation Operator and Time Frame are not searchable and render no input at all,
     * so they are still clicked. Their option lists are short enough to fit without scrolling, which is what makes
     * clicking them safe, and {@link #verifySelectionCommitted} covers them either way.
     */
    private void selectOption(Locator dropdownLocator, Locator selectionLocator, String targetText, String fieldName) {
        Locator searchInput = insideField(dropdownLocator, fieldName, "_SEARCH_INPUT",
                "//div[contains(@class,'Select-input')]//input");
        if (driverTools.element(searchInput, 1).isEmpty()) {
            clickOption(selectionLocator, targetText, fieldName);
            return;
        }
        selectByTyping(searchInput, dropdownLocator, selectionLocator, targetText, fieldName);
    }

    private void clickOption(Locator selectionLocator, String targetText, String fieldName) {
        driverTools.click(driverTools.elements(selectionLocator).orElseGet(List::of).stream()
                .filter(option -> driverTools.getText(option).trim().equalsIgnoreCase(targetText.trim()))
                .findFirst()
                .orElseThrow(() -> new DriverToolsException("'" + targetText + "' left the " + fieldName
                        + " option list before it could be selected.")));
    }

    private void selectByTyping(Locator searchInput, Locator dropdownLocator, Locator selectionLocator,
            String targetText, String fieldName) {
        driverTools.sendKeys(searchInput, targetText);

        Locator filteredOption = insideField(dropdownLocator, fieldName, "_FILTERED_OPTION",
                "//div[contains(@class,'VirtualizedSelectOption') and normalize-space(.)=\"" + targetText + "\"]");
        if (driverTools.waitForElementToAppear(filteredOption, 10).isEmpty()) {
            throw new DriverToolsException("Typing '" + targetText + "' into the " + fieldName + " field left it out "
                    + "of its own filtered option list, so it cannot be selected.");
        }

        // Enter takes the highlighted option, which is the first of whatever survived the filter. The typed text can
        // also be the start of a longer option — "Garuda" and "garuda bumblebee" are both suppliers — so step down
        // to the exact match rather than trusting the first row.
        for (int step = 0; step < positionOfExactMatch(selectionLocator, targetText); step++) {
            driverTools.sendKeys(searchInput, String.valueOf(Keys.ARROW_DOWN));
        }
        driverTools.sendKeys(searchInput, String.valueOf(Keys.ENTER));
    }

    /** Where the exact match sits among the filtered options, so Enter can be walked onto it. */
    private int positionOfExactMatch(Locator selectionLocator, String targetText) {
        List<WebElement> filtered = driverTools.elements(selectionLocator).orElseGet(List::of);
        for (int index = 0; index < filtered.size(); index++) {
            if (driverTools.getText(filtered.get(index)).trim().equalsIgnoreCase(targetText.trim())) {
                return index;
            }
        }
        return 0;
    }

    /**
     * Locates something within one field's wrapper, identified by the label id carried in that field's dropdown
     * locator. Deriving it this way covers Reason Id too, whose locator points inside the wrapper rather than at it.
     */
    private Locator insideField(Locator dropdownLocator, String fieldName, String name, String relativeXPath) {
        Matcher labelFor = LABEL_FOR_IN_XPATH.matcher(dropdownLocator.by().toString());
        if (!labelFor.find()) {
            throw new IllegalArgumentException("Cannot tell which field " + dropdownLocator + " belongs to, so the "
                    + fieldName + " dropdown cannot be driven. Dropdown locators must be anchored on their label.");
        }
        return new Locator(fieldName.toUpperCase().replace(' ', '_') + name,
                new By.ByXPath("//div[@class='tixselect' and ./label[contains(@for,'" + labelFor.group(1) + "')]]"
                        + relativeXPath));
    }

    /**
     * Confirms the control actually took the value, rather than only that an option was clicked. React-select marks
     * the wrapper {@code has-value} and renders the choice as {@code span.Select-value-label}; until it does, the
     * field is empty as far as the panel is concerned. A click that lands while the option list is re-rendering is
     * silently lost, and without this check the only symptom is the panel refusing to submit many fields later,
     * naming a field the log clearly shows being selected.
     *
     * <p>The field is identified by its label's {@code for}, read back out of the dropdown locator, so this works
     * for every dropdown on the form including Reason Id, whose locator points inside the wrapper rather than at it.
     */
    private void verifySelectionCommitted(Locator dropdownLocator, String fieldName, String selectedText) {
        Locator committedValue = insideField(dropdownLocator, fieldName, "_COMMITTED_VALUE",
                "//span[contains(@class,'Select-value-label')]");

        if (driverTools.waitForElementToAppear(committedValue, 5).isEmpty()) {
            throw new DriverToolsException("Clicking '" + selectedText + "' did not set the " + fieldName
                    + " field - the dropdown still holds no value. The option list re-rendered as it was clicked, so "
                    + "the panel would treat this required field as empty and refuse the submit.");
        }

        // Presence alone would also be satisfied by a value the click never chose, which is the same re-render race
        // one step further on: the list shifts under the cursor and a neighbouring option is taken instead.
        List<String> committed = driverTools.elements(committedValue).orElseGet(List::of).stream()
                .map(value -> driverTools.getText(value).trim())
                .collect(Collectors.toList());
        if (committed.stream().noneMatch(value -> value.equalsIgnoreCase(selectedText.trim()))) {
            throw new DriverToolsException("The " + fieldName + " field holds " + committed + " rather than '"
                    + selectedText + "', so the click landed on a different option than the one that was read. The "
                    + "record would be created with a value the test never chose.");
        }
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
        // Reason Id stays disabled until its reason list has been fetched, and react-select renders no control
        // while disabled — clicking it then selects nothing and leaves this required field empty, which the
        // panel only reports later by refusing to submit.
        if (driverTools.waitForElementToAppear(REASON_ID_SELECT_ENABLED, 20).isEmpty()) {
            throw new DriverToolsException("The Reason Id dropdown is still disabled after 20s, so no reason can be "
                    + "selected. Its reason list never loaded, or a field it depends on was not set.");
        }
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
        driverTools.waitForElementToBeClickable(FARE_BASIS_CONTAINER);
        driverTools.click(FARE_CLASSES_CONTAINER);
        driverTools.sendKeys(FARE_CLASSES_FIELDS, fareClasses);
        driverTools.sendKeys(FARE_CLASSES_FIELDS, String.valueOf(Keys.TAB));

        return driverTools.getValue(FARE_CLASSES_FIELDS);
    }

    private String fillFareBasis(String fareBasis) {
        driverTools.waitForElementToBeClickable(FARE_BASIS_CONTAINER);
        driverTools.click(FARE_BASIS_CONTAINER);
        driverTools.sendKeys(FARE_BASIS_FIELDS, fareBasis);
        driverTools.sendKeys(FARE_BASIS_FIELDS, String.valueOf(Keys.TAB));

        return driverTools.getValue(FARE_BASIS_VALUE);
    }


    private void fillBrandedFare(String fareBasis) {
        driverTools.waitForElementToAppear(BRANDED_FARE_INFO, 5);
        driverTools.scrollIntoWebViewOptional(BRANDED_FARE_INFO);
        driverTools.clickWithJavascriptExecutor(BRANDED_FARE_INFO);

        driverTools.waitForElementToBeClickable(BRANDED_FARE, 5);
        driverTools.clickWithJavascriptExecutor(BRANDED_FARE);
        driverTools.sendKeys(BRANDED_FARE, fareBasis);
        driverTools.sendKeys(BRANDED_FARE, String.valueOf(Keys.TAB));
        String reversed = new StringBuilder(fareBasis).reverse().toString();
        driverTools.sendKeys(BRANDED_FARE, reversed);
        driverTools.sendKeys(BRANDED_FARE, String.valueOf(Keys.TAB));

        return;
    }

    private String fillDescription(String description) {
        driverTools.click(DESCRIPTION_FIELDS);
        driverTools.sendKeys(DESCRIPTION_FIELDS, description);

        return driverTools.getText(DESCRIPTION_FIELDS).get();
    }

    private String fillMinutes(String minutes) {
        try {
            Integer.parseInt(minutes);
            driverTools.sendKeys(MINUTES_FIELDS, minutes);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Minutes on testdata is not numeric : '"+minutes+"'");
        }

        return driverTools.getValue(MINUTES_FIELDS);
    }

    private String fillFormula(String formula) {
        driverTools.click(ADD_FORMULA_BUTTON);
        driverTools.sendKeys(FORMULA_FIELDS, formula);
        driverTools.click(FORMULA_VALIDATE_SAVE_BUTTON);

        return driverTools.getText(SAVED_FORMULA_TEXT).get();
    }
    

    public void clickSubmitButton() {
        driverTools.waitForElementToAppear(SUBMIT_BTN, 30);
        driverTools.click(SUBMIT_BTN);

        if (driverTools.waitForElementToAppear(REFUND_MATRIX_LIST_ADD_BTN, 20).isEmpty()) {
            throw new DriverToolsException("Refund matrix was not submitted - the form is still open instead of the "
                    + "refund matrix list. Fields the panel rejected: " + rejectedFieldNames()
                    + ". Current URL: " + driver.getCurrentUrl());
        }
    }

    /**
     * Names of the fields the panel marked invalid when it refused the submit. Without these, a blocked submit
     * only says "still on the form" and gives no clue which field was left empty — the panel reports nothing in
     * the URL and the message itself is a generic "Data tidak boleh kosong" repeated per field.
     *
     * <p>Never throws: this only decorates the failure that is already being raised, and a form still settling can
     * take these elements stale mid-read. Losing the field names is worth far less than losing the real failure.
     */
    private String rejectedFieldNames() {
        try {
            List<String> names = driver.findElements(By.xpath(REJECTED_FIELD_XPATH)).stream()
                    .map(element -> element.getText().trim().replaceAll("\\s+", " "))
                    .filter(text -> !text.isEmpty())
                    .map(text -> text.length() > 70 ? text.substring(0, 70) + "…" : text)
                    .distinct()
                    .collect(Collectors.toList());
            return names.isEmpty() ? "none reported" : String.join("; ", names);
        } catch (RuntimeException e) {
            return "could not be read (" + e.getClass().getSimpleName() + ")";
        }
    }
}
