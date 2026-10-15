package com.tiket.web.dashboard.page.flight;

import com.tiket.model.Environment;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class FlightRefundMatrixPage<T extends WebDriver> extends BasePage<T> {
    public FlightRefundMatrixPage(Session<T> session) {
        super(session);
    }

    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[@class='header__username']"));
    private static final Locator ADD_NEW_REFUND_MATRIX_BTN = new Locator("ADD_NEW_REFUND_MATRIX_BTN", new By.ByXPath("(//div[@class='custom-table__add-button-container']//button)[last()]"));
    private static final Locator APPLY_FILTER_BTN = new Locator("APPLY_FILTER_BTN", new By.ByXPath("//button[contains(@class,'custom-table__filter--submit')]"));
    private static final Locator SUPPLIER_CODE_FILTER_FIELD = new Locator("SUPPLIER_CODE_FILTER_FIELD", new By.ByXPath("//input[contains(@name,'supplierCode')]"));
    private static final Locator ARILINE_CODE_FILTER_FIELD = new Locator("ARILINE_CODE_FILTER_FIELD", new By.ByXPath("//input[contains(@name,'airlineCode')]"));
    private static final Locator REASON_ID_FILTER_FIELD = new Locator("REASON_ID_FILTER_FIELD", new By.ByXPath("//input[contains(@name,'reasonId')]"));
    private static final Locator REASON_ID_FILTER_SELECT_CONTROL = new Locator("REASON_ID_FILTER_SELECT_CONTROL", new By.ByXPath(
            "//input[@id='reasonId']/ancestor::div[contains(@class,'Select-control')][1]"));
    private static final Locator REASON_ID_FILTER_SELECT_ARROW = new Locator("REASON_ID_FILTER_SELECT_ARROW", new By.ByXPath(
            "//input[@id='reasonId']/ancestor::div[contains(@class,'Select-control')][1]//span[contains(@class,'Select-arrow-zone')]"));
    private static final Locator STATUS_FILTER_DROPDOWN = new Locator("STATUS_FILTER_DROPDOWN", new By.ByXPath("//div[contains(@class,'tixselect') and ./label[contains(@for,'status')]]"));
    private static final Locator STATUS_FILTER_DROPDOWN_SELECTION = new Locator("STATUS_FILTER_DROPDOWN_SELECTION", new By.ByXPath("//div[contains(@class,'tixselect') and ./label[contains(@for,'status')]]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator MATRIX_TYPE_FILTER_DROPDOWN = new Locator("MATRIX_TYPE_FILTER_DROPDOWN", new By.ByXPath("//div[contains(@class,'tixselect') and ./label[contains(@for,'matrixType')]]"));
    private static final Locator MATRIX_TYPE_FILTER_DROPDOWN_SELECTION = new Locator("MATRIX_TYPE_FILTER_DROPDOWN_SELECTION", new By.ByXPath("//div[contains(@class,'tixselect') and ./label[contains(@for,'matrixType')]]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator FARE_CLASSES_FILTER_FIELD = new Locator("FARE_CLASSES_FILTER_FIELD", new By.ByXPath("//input[contains(@name,'fareClasses')]"));
    private static final Locator PRIORITY_FILTER_DROPDOWN = new Locator("PRIORITY_FILTER_DROPDOWN", new By.ByXPath("//div[contains(@class,'tixselect') and ./label[contains(@for,'priority')]]"));
    private static final Locator PRIORITY_FILTER_DROPDOWN_SELECTION = new Locator("PRIORITY_FILTER_DROPDOWN_SELECTION", new By.ByXPath("//div[contains(@class,'tixselect') and ./label[contains(@for,'priority')]]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator OK_PRIMARY_BUTTON = new Locator("OK_PRIMARY_BUTTON", new By.ByXPath("//button[contains(@class,'btn-primary') and contains(@class,'buttonadd')]"));
    /**
     * Main list grid only: {@code div.custom-table} → {@code table.MuiTable-root} → {@code tbody.custom-table__body}.
     * A bare {@code //tbody[...]} can match multiple tables and mix rows (wrong counts / wrong “last” row).
     */
    private static final String REFUND_MATRIX_TBODY =
            "(//div[contains(@class,'custom-table')]//table[contains(@class,'MuiTable-root')]"
                    + "//tbody[contains(@class,'custom-table__body')])[1]";
    private static final Locator NO_DATA_FOUND = new Locator("NO_DATA_FOUND",
            new By.ByXPath(REFUND_MATRIX_TBODY + "/tr[1]/td[text()='Data Not Exist']"));
    /** nth data row — tbody can contain non-data {@code tr} nodes. */
    private static String refundMatrixDataRowXPath(int oneBasedRowIndex) {
        return REFUND_MATRIX_TBODY + "/tr[contains(@class,'row_value')][" + oneBasedRowIndex + "]";
    }

    /**
     * Detail modal table: each row is {@code tr} with label {@code h6} in the first {@code th} and value in {@code th[2]}
     * (MUI). Rows were re-ordered (e.g. Branded Fare, Reason Detail Id); use label text instead of {@code tr[index]}.
     */
    private static final String REFUND_MATRIX_DETAILS_MODAL_TABLE =
            "//div[contains(@class,'text-field') and contains(@class,'modal--container')]"
                    + "//table[contains(@class,'MuiTable-root')]";

    private static Locator detailsModalValueByLabel(String locatorName, String labelText) {
        return new Locator(locatorName, new By.ByXPath(
                REFUND_MATRIX_DETAILS_MODAL_TABLE + "//tr[th//h6[normalize-space(.)=\"" + labelText + "\"]]/th[2]"));
    }

    /** First list chip when the value is a {@code ul}/{@code li} (aligns with single-value test data). */
    private static Locator detailsModalFirstListItemByLabel(String locatorName, String labelText) {
        return new Locator(locatorName, new By.ByXPath(
                REFUND_MATRIX_DETAILS_MODAL_TABLE
                        + "//tr[th//h6[normalize-space(.)=\"" + labelText + "\"]]/th[2]//li[1]"));
    }

    /** First penalty block “1.1 … Minutes” row ({@code h6} may contain {@code &nbsp;}). */
    private static final Locator MINUTES_DETAILS_VALUE = new Locator("MINUTES_DETAILS_VALUE", new By.ByXPath(
            REFUND_MATRIX_DETAILS_MODAL_TABLE + "//tr[th//h6[contains(.,'1.1') and contains(.,'Minutes')]]/th[2]"));

    /** Labels repeat under penalty matrices; use the first block in document order. */
    private static final Locator TIME_FRAME_DETAILS_VALUE = new Locator("TIME_FRAME_DETAILS_VALUE", new By.ByXPath(
            "(" + REFUND_MATRIX_DETAILS_MODAL_TABLE + "//tr[th//h6[contains(normalize-space(.),'Time Frame')]])[1]/th[2]"));

    private static final Locator RELATION_OPERATOR_DETAILS_VALUE = new Locator("RELATION_OPERATOR_DETAILS_VALUE",
            new By.ByXPath("(" + REFUND_MATRIX_DETAILS_MODAL_TABLE
                    + "//tr[th//h6[contains(normalize-space(.),'Relation Operator')]])[1]/th[2]"));

    private static final Locator FORMULA_DETAILS_VALUE = new Locator("FORMULA_DETAILS_VALUE", new By.ByXPath(
            "(" + REFUND_MATRIX_DETAILS_MODAL_TABLE + "//tr[th//h6[contains(normalize-space(.),'Formula')]])[1]/th[2]"));

    private static final Locator PRIORITY_DETAILS_VALUE =
            detailsModalValueByLabel("PRIORITY_DETAILS_VALUE", "Priority");
    private static final Locator ORIGINS_DETAILS_VALUE =
            detailsModalValueByLabel("ORIGINS_DETAILS_VALUE", "Origins");
    private static final Locator DESTINATION_DETAILS_VALUE =
            detailsModalValueByLabel("DESTINATION_DETAILS_VALUE", "Destinations");
    private static final Locator MATRIX_TYPE_DETAILS_VALUE =
            detailsModalValueByLabel("MATRIX_TYPE_DETAILS_VALUE", "Matrix Type");
    private static final Locator REFUND_METHOD_DETAILS_VALUE =
            detailsModalValueByLabel("REFUND_METHOD_DETAILS_VALUE", "Refund Method");
    /** Full value cell: modal lists every cabin as {@code li}; dropdown label (e.g. “all class”) is not shown verbatim. */
    private static final Locator CABIN_CLASSES_DETAILS_VALUE =
            detailsModalValueByLabel("CABIN_CLASSES_DETAILS_VALUE", "Cabin Classes");
    private static final Locator FARE_CLASSES_DETAILS_VALUE =
            detailsModalFirstListItemByLabel("FARE_CLASSES_DETAILS_VALUE", "Fare Classes");
    private static final Locator FARE_BASIS_DETAILS_VALUE =
            detailsModalFirstListItemByLabel("FARE_BASIS_DETAILS_VALUE", "Fare Basis");
    private static final Locator DESCRIPTION_DETAILS_VALUE =
            detailsModalValueByLabel("DESCRIPTION_DETAILS_VALUE", "Description");
    private static final Locator DATE_OF_ISSUANCE_DETAILS_VALUE =
            detailsModalValueByLabel("DATE_OF_ISSUANCE_DETAILS_VALUE", "Date of Issuance");
    private static final Locator DATE_OF_TRAVEL_DETAILS_VALUE =
            detailsModalValueByLabel("DATE_OF_TRAVEL_DETAILS_VALUE", "Date of Travel");
    private static final Locator ITEM_TYPES_DETAILS_VALUE =
            detailsModalFirstListItemByLabel("ITEM_TYPES_DETAILS_VALUE", "1 Item Types");
    private static final Locator CANCEL_DETAILS_BUTTON = new Locator("CANCEL_DETAILS_BUTTON", new By.ByXPath("//button[contains(@class,'btn-secondary')]"));

    public boolean isLoggedIn() {
        driverTools.waitForElementToAppear(USERNAME_GREETING, 3);
        return driverTools.isElementDisplayed(USERNAME_GREETING);
    }

    public void goToRefundMatrixDashboard() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/flight-ms-v2/refund-matrix");
            driver.get("https://gatotkaca.tiket.com/admin/flight-ms-v2/refund-matrix");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/flight-ms-v2/refund-matrix");
            driver.get("https://preprod.tiket.com/admin/flight-ms-v2/refund-matrix");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://www.tiket.com/admin/flight-ms-v2/refund-matrix");
            driver.get("https://www.tiket.com/admin/flight-ms-v2/refund-matrix");
        }
    }

    public void clickAddRefundMatrixBtn() {
        driverTools.waitForElementToAppear(ADD_NEW_REFUND_MATRIX_BTN, 5);
        driverTools.click(ADD_NEW_REFUND_MATRIX_BTN);
    }

    public void clickReasonIdFilterArrow() {
        driverTools.scrollIntoWebViewOptional(REASON_ID_FILTER_SELECT_CONTROL);
        driverTools.waitForElementToAppear(REASON_ID_FILTER_SELECT_ARROW, 5);
        driverTools.click(REASON_ID_FILTER_SELECT_ARROW);
    }

    public void applyFilter(String supplierCode, String airlineCode, String reasonId, String status, String matrixType, String fareClasses, String priority) {
        driverTools.waitForElementToBeClickable(APPLY_FILTER_BTN, 5);

        if (!supplierCode.isEmpty()) {
            fillSupplierCodeFilter(supplierCode);
        }
        if (!airlineCode.isEmpty()) {
            fillAirlineCodeFilter(airlineCode);
        }
        if (!reasonId.isEmpty()) {
            fillReasonIdFilter(reasonId);
        }
        if (!status.isEmpty()) {
            chooseStatusFilter(status);
        }
        if (!matrixType.isEmpty()) {
            chooseMatrixTypeFilter(matrixType);
        }
        if (!priority.isEmpty()) {
            choosePriorityFilter(priority);
        }

        if (!fareClasses.isEmpty()) {
            fillFareClassesFilter(fareClasses);
        }

        if (driverTools.waitForElementToAppear(APPLY_FILTER_BTN, 5).isPresent()) {
            driverTools.clickElementByCoordinateDWeb(APPLY_FILTER_BTN);
        }
    }

    /**
     * Fare classes of the first data row of the unfiltered list. The grid is ordered newest first, so a record that
     * has just been created is expected here.
     */
    public String getFirstRowFareClasses() {
        String firstRow = refundMatrixDataRowXPath(1);
        if (driverTools.waitForElementToAppear(new Locator("FIRST_DATA_ROW", new By.ByXPath(firstRow)), 10).isEmpty()) {
            throw new DriverToolsException("The refund matrix list rendered no data rows, so the first row cannot be "
                    + "read. The list did not load after leaving the form.");
        }
        return getFareClasses(firstRow, "first row on first page");
    }

    /**
     * Narrows the list to the single record carrying this unique fare class, so the first data row is that record.
     * The fare class filter is an exact match, which is what makes the row identity reliable — reading "the last row
     * on the last page" instead picks up whichever unrelated record happens to sort last.
     */
    public void filterByFareClasses(String fareClasses) {
        applyFilter("", "", "", "", "", fareClasses, "");
        waitForFilteredRow(fareClasses);
    }

    /** The grid re-renders asynchronously after Apply Filter; without this wait the pre-filter rows are still shown. */
    private void waitForFilteredRow(String fareClasses) {
        Locator filteredRow = new Locator("FILTERED_ROW_FARE_CLASSES",
                new By.ByXPath(refundMatrixDataRowXPath(1) + "/td[7][normalize-space(.)='" + fareClasses + "']"));
        if (driverTools.waitForElementToAppear(filteredRow, 10).isEmpty()) {
            throw new DriverToolsException("Refund matrix with fare classes '" + fareClasses
                    + "' is not the first row after filtering — it was not created, or the filter did not apply.");
        }
    }

    private void chooseFromDropdown(String value, Locator dropdownLocator, Locator selectionLocator, String fieldName) {
        driverTools.waitForElementToAppear(dropdownLocator, 5);
        driverTools.click(dropdownLocator);

        driverTools.waitForElementToAppear(selectionLocator);
        List<WebElement> selections = driverTools.elementsUntilExist(selectionLocator).orElseThrow();
        WebElement targetElement = null;
        
        for (WebElement e : selections) {
            if (driverTools.getText(e).equalsIgnoreCase(value)) {
                driverTools.click(e);
                targetElement = e;
                break;
            }
        }
        
        if (targetElement == null) {
            throw new NoSuchElementException("There is no '" + value + "' " + fieldName);
        }
    }

    private void fillFilterField(String value, Locator fieldLocator) {
        driverTools.waitForElementToAppear(fieldLocator, 5);
        driverTools.clear(fieldLocator);
        driverTools.sendKeys(fieldLocator, value);
    }

    private void chooseStatusFilter(String status) {
        chooseFromDropdown(status, STATUS_FILTER_DROPDOWN, STATUS_FILTER_DROPDOWN_SELECTION, "status");
    }

    private void chooseMatrixTypeFilter(String matrixType) {
        chooseFromDropdown(matrixType, MATRIX_TYPE_FILTER_DROPDOWN, MATRIX_TYPE_FILTER_DROPDOWN_SELECTION, "matrix type");
    }

    private void choosePriorityFilter(String priority) {
        chooseFromDropdown(priority, PRIORITY_FILTER_DROPDOWN, PRIORITY_FILTER_DROPDOWN_SELECTION, "priority");
    }

    private void fillSupplierCodeFilter(String supplierCode) {
        fillFilterField(supplierCode, SUPPLIER_CODE_FILTER_FIELD);
    }

    private void fillAirlineCodeFilter(String airlineCode) {
        fillFilterField(airlineCode, ARILINE_CODE_FILTER_FIELD);
    }

    private void fillReasonIdFilter(String reasonId) {
        fillFilterField(reasonId, REASON_ID_FILTER_FIELD);
    }

    private void fillFareClassesFilter(String fareClasses) {
        fillFilterField(fareClasses, FARE_CLASSES_FILTER_FIELD);
    }

    /**
     * Reads visible column values for one result row. {@code targetIndex} is 1-based (first data row = {@code 1}).
     * Targets the MUI table body {@code custom-table__body}.
     */

    public Map<String, String> getDisplayedData(int targetIndex) {
        logger.log("Getting displayed data for row index: " + targetIndex);
        return collectDisplayedDataForRow(refundMatrixDataRowXPath(targetIndex), "row index " + targetIndex);
    }

    private Map<String, String> collectDisplayedDataForRow(String dataRowXPath, String rowLogLabel) {
        Map<String, String> displayedData = new HashMap<>();

        driverTools.scrollIntoWebViewOptional(new Locator("REFUND_MATRIX_DATA_ROW_" + rowLogLabel.replace(' ', '_'),
                new By.ByXPath(dataRowXPath)));

        displayedData.put("supplierCode", getSupplierCode(dataRowXPath, rowLogLabel));
        displayedData.put("airlineCode", getAirlineCode(dataRowXPath, rowLogLabel));
        displayedData.put("reasonId", getReasonId(dataRowXPath, rowLogLabel));
        displayedData.put("reasonContent", getReasonContent(dataRowXPath, rowLogLabel));
        displayedData.put("status", getStatus(dataRowXPath, rowLogLabel));
        displayedData.put("matrixType", getMatrixType(dataRowXPath, rowLogLabel));
        displayedData.put("fareClasses", getFareClasses(dataRowXPath, rowLogLabel));
        displayedData.put("priority", getPriority(dataRowXPath, rowLogLabel));
        displayedData.put("reviewAction", getReviewAction(dataRowXPath, rowLogLabel));

        return displayedData;
    }

    private Locator getTableCellLocator(int targetIndex, int columnIndex) {
        return new Locator("CELL_" + targetIndex + "_" + columnIndex,
            new By.ByXPath(REFUND_MATRIX_TBODY + "/tr[" + targetIndex + "]/td[" + columnIndex + "]"));
    }

    private String getCellText(int targetIndex, int columnIndex) {
        return driverTools.getText(getTableCellLocator(targetIndex, columnIndex)).orElse("");
    }

    private Locator tableCellLocator(String dataRowXPath, int columnIndex) {
        return new Locator("CELL_" + columnIndex,
                new By.ByXPath(dataRowXPath + "/td[" + columnIndex + "]"));
    }

    private void scrollTableCellIntoView(String dataRowXPath, int columnIndex) {
        driverTools.scrollIntoWebViewOptional(tableCellLocator(dataRowXPath, columnIndex));
    }

    private String getCellText(String dataRowXPath, int columnIndex) {
        scrollTableCellIntoView(dataRowXPath, columnIndex);
        return driverTools.getText(tableCellLocator(dataRowXPath, columnIndex)).orElse("");
    }

    /**
     * Reason columns may use {@code li} chips or plain cell text (MUI).
     */
    private String getCellTextOrListItems(int targetIndex, int columnIndex) {
        List<String> listItems = driverTools.getTexts(new Locator("CELL_LI_" + targetIndex + "_" + columnIndex,
            new By.ByXPath(REFUND_MATRIX_TBODY + "/tr[" + targetIndex + "]/td[" + columnIndex + "]//li")));
        if (!listItems.isEmpty()) {
            return String.join("--", listItems);
        }
        return getCellText(targetIndex, columnIndex);
    }

    private String getSupplierCode(int targetIndex) {
        String res = getCellText(targetIndex, 1);
        logger.log("supplierCode (row " + targetIndex + "): " + res);
        return res;
    }

    /**
     * Reason columns may use {@code li} chips or plain cell text (MUI).
     */
    private String getCellTextOrListItems(String dataRowXPath, int columnIndex) {
        scrollTableCellIntoView(dataRowXPath, columnIndex);
        List<String> listItems = driverTools.getTexts(new Locator("CELL_LI_" + columnIndex,
                new By.ByXPath(dataRowXPath + "/td[" + columnIndex + "]//li")));
        if (!listItems.isEmpty()) {
            return String.join("--", listItems);
        }
        return getCellText(dataRowXPath, columnIndex);
    }

    private String getAirlineCode(int targetIndex) {
        String res = getCellText(targetIndex, 2);
        logger.log("airlineCode (row " + targetIndex + "): " + res);
        return res;
    }

    private String getSupplierCode(String dataRowXPath, String rowLogLabel) {
        String res = getCellText(dataRowXPath, 1);
        logger.log("supplierCode (" + rowLogLabel + "): " + res);
        return res;
    }

    private String getReasonId(int targetIndex) {
        String res = getCellTextOrListItems(targetIndex, 3);
        logger.log("reasonId (row " + targetIndex + "): " + res);
        return res;
    }

    private String getAirlineCode(String dataRowXPath, String rowLogLabel) {
        String res = getCellText(dataRowXPath, 2);
        logger.log("airlineCode (" + rowLogLabel + "): " + res);
        return res;
    }

    private String getReasonContent(int targetIndex) {
        String res = getCellTextOrListItems(targetIndex, 4);
        logger.log("reasonContent (row " + targetIndex + "): " + res);
        return res;
    }

    private String getReasonId(String dataRowXPath, String rowLogLabel) {
        String res = getCellTextOrListItems(dataRowXPath, 3);
        logger.log("reasonId (" + rowLogLabel + "): " + res);
        return res;
    }

    private String getStatus(int targetIndex) {
        String res = getCellText(targetIndex, 5);
        logger.log("status (row " + targetIndex + "): " + res);
        return res;
    }

    private String getReasonContent(String dataRowXPath, String rowLogLabel) {
        String res = getCellTextOrListItems(dataRowXPath, 4);
        logger.log("reasonContent (" + rowLogLabel + "): " + res);
        return res;
    }

    private String getMatrixType(int targetIndex) {
        String res = getCellText(targetIndex, 6);
        logger.log("matrixType (row " + targetIndex + "): " + res);
        return res;
    }

    private String getStatus(String dataRowXPath, String rowLogLabel) {
        String res = getCellText(dataRowXPath, 5);
        logger.log("status (" + rowLogLabel + "): " + res);
        return res;
    }

    private String getFareClasses(int targetIndex) {
        String res = getCellText(targetIndex, 7);
        logger.log("fareClasses (row " + targetIndex + "): " + res);
        return res;
    }

    private String getMatrixType(String dataRowXPath, String rowLogLabel) {
        String res = getCellText(dataRowXPath, 6);
        logger.log("matrixType (" + rowLogLabel + "): " + res);
        return res;
    }

    private String getPriority(int targetIndex) {
        String res = getCellText(targetIndex, 8);
        logger.log("priority (row " + targetIndex + "): " + res);
        return res;
    }

    private String getFareClasses(String dataRowXPath, String rowLogLabel) {
        String res = getCellText(dataRowXPath, 7);
        logger.log("fareClasses (" + rowLogLabel + "): " + res);
        return res;
    }

    private String getReviewAction(int targetIndex) {
        String res = Boolean.toString(driverTools.element(new Locator("REVIEW_ACTION_" + targetIndex,
                new By.ByXPath(REFUND_MATRIX_TBODY + "/tr[" + targetIndex + "]/td[9]//a[contains(@href,'refund-matrix/review/')]"))).isPresent());
        logger.log("reviewAction (row " + targetIndex + "): " + res);
        return res;
    }

    private String getPriority(String dataRowXPath, String rowLogLabel) {
        String res = getCellText(dataRowXPath, 8);
        logger.log("priority (" + rowLogLabel + "): " + res);
        return res;
    }

    private String getReviewAction(String dataRowXPath, String rowLogLabel) {
        scrollTableCellIntoView(dataRowXPath, 9);
        String safeId = rowLogLabel.replaceAll("[^a-zA-Z0-9_]+", "_");
        String res = Boolean.toString(driverTools.element(new Locator("REVIEW_ACTION_" + safeId,
                new By.ByXPath(dataRowXPath + "/td[9]//a[contains(@href,'refund-matrix/review/')]"))).isPresent());
        logger.log("reviewAction (" + rowLogLabel + "): " + res);
        return res;
    }

    public void deleteRefundMatrix(int targetIndex) {
        Locator deleteBtn = new Locator("DELETE_" + targetIndex,
                new By.ByXPath(refundMatrixDataRowXPath(targetIndex) + "/td[9]//button[contains(@class,'btn-danger')]"));
        clickRowAction(deleteBtn);
        driverTools.click(OK_PRIMARY_BUTTON);
    }

    public boolean isRefundMatrixDeleted(String fareClasses) {
        applyFilter("", "", "", "", "", fareClasses, "");
        return driverTools.waitForElementToAppear(NO_DATA_FOUND, 5).isPresent();
    }

    public void clickEditRefundMatrixBtn(int targetIndex) {
        driverTools.click(new Locator("EDIT_" + targetIndex,
                new By.ByXPath(refundMatrixDataRowXPath(targetIndex) + "/td[9]//a[contains(@href,'refund-matrix/edit/')]")));
    }

    public void clickDetailsRefundMatrixBtn(int targetIndex) {
        clickRowAction(new Locator("DETAILS_" + targetIndex,
                new By.ByXPath(refundMatrixDataRowXPath(targetIndex) + "/td[9]//button[contains(@class,'btn-detail')]")));
    }

    /**
     * The first rows sit just under the table header buttons and the page is often too short to scroll them clear,
     * so a header button can take the click. Fall back to a JavaScript click when that happens.
     */
    private void clickRowAction(Locator rowAction) {
        driverTools.scrollIntoWebViewOptional(rowAction);
        try {
            driverTools.click(rowAction);
        } catch (ElementClickInterceptedException e) {
            logger.log("Row action " + rowAction + " is covered, clicking it with JavaScript");
            driverTools.clickWithJavascriptExecutor(rowAction);
        }
    }

    public Map<String, String> getDisplayedDetailsData() {
        Map<String, String> displayedData = new HashMap<>();

        displayedData.put("priority", driverTools.getText(PRIORITY_DETAILS_VALUE).get());
        displayedData.put("origins", driverTools.getText(ORIGINS_DETAILS_VALUE).get());
        displayedData.put("destinations", driverTools.getText(DESTINATION_DETAILS_VALUE).get());
        displayedData.put("matrixType", driverTools.getText(MATRIX_TYPE_DETAILS_VALUE).get());
        displayedData.put("refundMethod", driverTools.getText(REFUND_METHOD_DETAILS_VALUE).get());
        displayedData.put("cabinClasses", driverTools.getText(CABIN_CLASSES_DETAILS_VALUE).get());
        displayedData.put("fareClasses", driverTools.getText(FARE_CLASSES_DETAILS_VALUE).get());
        displayedData.put("fareBasis", driverTools.getText(FARE_BASIS_DETAILS_VALUE).get());
        displayedData.put("description", driverTools.getText(DESCRIPTION_DETAILS_VALUE).get());

        Map<String, String> issuanceDateRange = getDetailsDate(DATE_OF_ISSUANCE_DETAILS_VALUE);
        displayedData.put("startDateIssuance", issuanceDateRange.get("startDate"));
        displayedData.put("endDateIssuance", issuanceDateRange.get("endDate"));

        Map<String, String> travelDateRange = getDetailsDate(DATE_OF_TRAVEL_DETAILS_VALUE);
        displayedData.put("startDateTravel", travelDateRange.get("startDate"));
        displayedData.put("endDateTravel", travelDateRange.get("endDate"));

        displayedData.put("itemTypes", driverTools.getText(ITEM_TYPES_DETAILS_VALUE).get());
        displayedData.put("relationOperator", driverTools.getText(RELATION_OPERATOR_DETAILS_VALUE).get());
        displayedData.put("timeFrame", driverTools.getText(TIME_FRAME_DETAILS_VALUE).get());
        displayedData.put("minutes", driverTools.getText(MINUTES_DETAILS_VALUE).get());
        displayedData.put("formula", driverTools.getText(FORMULA_DETAILS_VALUE).get());

        return displayedData;
    }

    /**
     * Asserts refund matrix details modal fields against values from the add form (dropdown labels vs. listed codes for cabin, etc.).
     */
    public void assertRefundMatrixDetailsModalMatchesCreated(Map<String, String> displayedRefundMatrix,
            Map<String, String> createdRefundMatrix, String fareClasses, String fareBasis) {
        for (String key : displayedRefundMatrix.keySet()) {
            logger.log("key : " + key);

            if (key.equalsIgnoreCase("fareclasses")) {
                Assertion.assertThat("Verify '" + key + "' is matched", displayedRefundMatrix.get(key).toLowerCase(),
                        is(fareClasses.toLowerCase()));
            } else if (key.equalsIgnoreCase("fareBasis")) {
                Assertion.assertThat("Verify '" + key + "' is matched", displayedRefundMatrix.get(key).toLowerCase(),
                        is(fareBasis.toLowerCase()));
            } else if (key.equalsIgnoreCase("cabinClasses")) {
                String createdCc = normaliseForModalComparison(createdRefundMatrix.get(key));
                String displayedCc = normaliseForModalComparison(displayedRefundMatrix.get(key));
                if (createdCc.contains("all") && createdCc.contains("class")) {
                    Assertion.assertThat("Verify cabin classes detail lists cabin codes when ‘all class’ was selected",
                            displayedCc,
                            anyOf(containsString("economy"), containsString("business"), containsString("first"),
                                    containsString("premier"), containsString("premium")));
                } else {
                    Assertion.assertThat("Verify '" + key + "' is matched", displayedCc, is(createdCc));
                }
            } else if (key.equalsIgnoreCase("relationOperator") || key.equalsIgnoreCase("timeFrame")) {
                Assertion.assertThat("Verify '" + key + "' is matched",
                        displayedRefundMatrix.get(key).toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", " "),
                        containsString(createdRefundMatrix.get(key).toLowerCase()));
            } else if (key.equalsIgnoreCase("priority")) {
                // The details modal leaves priority blank when it is 0, while the grid shows it. Accepted panel
                // behaviour, so blank is tolerated only for a created priority of 0 — any other value must match.
                String createdPriority = createdRefundMatrix.get(key).trim();
                String displayedPriority = displayedRefundMatrix.get(key).trim();
                if ("0".equals(createdPriority) && displayedPriority.isEmpty()) {
                    logger.log("Priority 0 is shown as blank in the details modal - accepted panel behaviour");
                } else {
                    Assertion.assertThat("Verify '" + key + "' is matched", displayedPriority.toLowerCase(),
                            is(createdPriority.toLowerCase()));
                }
            } else {
                Assertion.assertThat("Verify '" + key + "' is matched",
                        normaliseForModalComparison(displayedRefundMatrix.get(key)),
                        is(normaliseForModalComparison(createdRefundMatrix.get(key))));
            }
        }
    }

    /**
     * The details modal prints stored enum codes ({@code premium_economy}) where the add form supplied the
     * dropdown's label ("Premium Economy"), so the two spellings of one value must compare equal. Only the
     * separator and case are normalised — a genuinely different value still fails.
     */
    private static String normaliseForModalComparison(String value) {
        return value == null ? "" : value.trim().toLowerCase().replaceAll("[_\\s]+", " ");
    }

    private Map<String, String> getDetailsDate(Locator dateLocator) {
        Map<String, String> result = new HashMap<>();
        String text = driverTools.getText(dateLocator).orElse("").trim();
        if (text.isEmpty() || "-".equals(text) || !text.contains("From :")) {
            result.put("startDate", "");
            result.put("endDate", "");
            return result;
        }
        result.put("startDate", text.split("From : ")[1].split(" , ")[0]);
        result.put("endDate", text.split("To: ")[1]);
        return result;
    }

    public void clickCancelDetailsBtn() {
        driverTools.click(CANCEL_DETAILS_BUTTON);
    }
}