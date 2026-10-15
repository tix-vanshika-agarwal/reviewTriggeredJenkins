package com.tiket.web.dashboard.page.refund;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.tiket.core.CoreFunctions;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class ManualRefundPage <T extends WebDriver> extends BasePage<T> {
    public ManualRefundPage(Session<T> session) {
        super(session);
    }

    private static final Locator MENU_MANUAL_REFUND = new Locator("MENU_MANUAL_REFUND", By.xpath("//span[contains(text(),'Manual Refund')]"));
    private static final Locator MENU_REFUND = new Locator("MENU_REFUND", By.xpath("//span[contains(@class, 'menuLabel') and text()='Refund']"));
    private static final Locator MANUAL_REFUND_HEADING = new Locator("MANUAL_REFUND_HEADING", By.xpath("//h1[text()='Manual Refund']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[normalize-space(.)='Search']"));
    private static final Locator TABLE_HEADINGS = new Locator("TABLE_HEADINGS", By.xpath("//table//thead//tr/th"));
    private static final Locator REFUND_STATUS_BUTTON = new Locator("REFUND_STATUS_BUTTON", By.xpath("//button[normalize-space(.)='Refund Status']"));
    private static final Locator REFUND_REQUEST_ID_INPUT = new Locator("REFUND_REQUEST_ID_INPUT", By.xpath("//input[@data-testid='filter-input-refundRequestId-field']"));
    private static final Locator FIRST_ROW_REFUND_ID = new Locator("FIRST_ROW_REFUND_ID", By.xpath("//tbody/tr[1]/td[1]//span[@role='button']"));
    private static final Locator VERTICAL_DROPDOWN_BTN = new Locator("VERTICAL_DROPDOWN_BTN", By.xpath("//button[@data-testid='filter-input-vertical-field']"));
    private static final Locator EXPORT_TO_CSV_BUTTON = new Locator("EXPORT_TO_CSV_BUTTON", By.xpath("//button[.//span[normalize-space(.)='Export To CSV']]"));
    private static final Locator EMPTY_REFUND_MESSAGE = new Locator("EMPTY_REFUND_MESSAGE", By.xpath("//p[text()='There is no manual refund']"));
    private static final Locator ORDER_ID_INPUT = new Locator("ORDER_ID_INPUT", By.xpath("//input[@data-testid='filter-input-orderId-field']"));
    private static final Locator START_DATE_ICON = new Locator("START_DATE_ICON", By.xpath("//input[@placeholder='Start Date']"));
    private static final Locator END_DATE_ICON = new Locator("END_DATE_ICON", By.xpath("//input[@placeholder='End Date']"));
    private static final Locator END_DATE_ENABLED_INPUT = new Locator("END_DATE_ENABLED_INPUT", By.xpath("//input[@data-testid='field-endRequestedAt' and not(@disabled)]"));
    private static final Locator CALENDAR_PREVIOUS_MONTH = new Locator("CALENDAR_PREVIOUS_MONTH", By.xpath("//div[@role='dialog' and @data-state='open']//button[contains(@class,'CalendarDesktop_prev_button')]"));
    private static final Locator FIRST_ROW_ORDER_ID = new Locator("FIRST_ROW_ORDER_ID", By.xpath("//tbody/tr[1]/td[2]//p[1]"));
    private static final Locator FIRST_ROW_REQUEST_DATE = new Locator("FIRST_ROW_REQUEST_DATE", By.xpath("//tbody/tr[1]/td[4]//p[1]"));
    private static final DateTimeFormatter REQUEST_DATE_FORMAT = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm", Locale.ENGLISH);
    public static final String EXPORTED_CSV_NAME = "ManualRefund.csv";
    public static Locator getFilterRefundStatusOption(String status) {
        return new Locator("REFUND_STATUS_OPTION", By.xpath("//div[@role='option']//span[text()='%s']".formatted(status)));
    }
    public static Locator getCalendarDay(String day) {
        return new Locator("CALENDAR_DAY", By.xpath("(//div[@role='dialog' and @data-state='open']//section[contains(@class,'CalendarDesktop_month')])[1]//span[contains(@class,'Day_day_number_') and normalize-space(text())='%s']/ancestor::button[1]".formatted(day)));
    }
    public static Locator getExpectedFilterRefundStatusOption(String status) {
        return new Locator("EXPECTED_REFUND_STATUS_OPTION", By.xpath("//button[normalize-space(.)='%s']".formatted(status)));
    }

    public void goToManualRefundPage() {
        if (!driverTools.waitForElementToAppear(MENU_MANUAL_REFUND).isPresent()){
            driverTools.scrollAndClick(MENU_REFUND);
        }
        driverTools.scrollAndClick(MENU_MANUAL_REFUND);
    }

    public boolean isOnManualRefundPage() {
        return driverTools.waitForElementToAppear(MANUAL_REFUND_HEADING).isPresent();
    }

    public void selectFilterRefundStatus(String filterRefundStatus) {
        driverTools.click(REFUND_STATUS_BUTTON);
        Locator visibleOption = getFilterRefundStatusOption(filterRefundStatus);
        for (int i = 0; i < 5 && !driverTools.waitForElementToAppear(visibleOption).isPresent(); i++) {
            driverTools.scrollAndClick(REFUND_STATUS_BUTTON);
        }
        driverTools.click(visibleOption);
    }

    public void clickSearchButton() {
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
        driverTools.dummyWait(2);
    }

    public boolean isFilterRefundStatusSelected(String filterRefundStatus) {
        return driverTools.waitForElementToAppear(getExpectedFilterRefundStatusOption(filterRefundStatus)).isPresent();
    }

    private int getTableHeadingIndex(String heading) {
        driverTools.waitForElementToAppear(TABLE_HEADINGS);
        List<WebElement> headings = driverTools.elements(TABLE_HEADINGS).orElseThrow(() -> new IllegalStateException("No headings found"));
        if (headings.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < headings.size(); i++) {
            String text = headings.get(i).getText().trim();
            if (text.isEmpty()) {
                text = headings.get(i).getAttribute("textContent").trim();
            }
            if (text.equalsIgnoreCase(heading)) {
                return i;
            }
        }
        return -1;
    }

    public List<String> getAllTableRowsData(String heading) {
        List<String> allRowsRefundStatus = new ArrayList<>();
        int index = getTableHeadingIndex(heading);
        if (index == -1) {
            return allRowsRefundStatus;
        }

        Locator dataLocator = new Locator("DATA", By.xpath("//table//tbody//tr/td[%d]".formatted(index + 1)));
        List<WebElement> elements = driverTools.elements(dataLocator).orElseThrow(() -> new IllegalStateException("No table data found"));

        for (WebElement element : elements) {
            String text = element.getText().trim();
            if (text.isEmpty()) {
                text = element.getAttribute("textContent").trim();
            }
            allRowsRefundStatus.add(text);
        }

        return allRowsRefundStatus;
    }

    public void enterRefundRequestId(String refundId) {
        driverTools.waitForElementToBeClickable(REFUND_REQUEST_ID_INPUT).orElseThrow();
        driverTools.click(REFUND_REQUEST_ID_INPUT);
        driverTools.clear(REFUND_REQUEST_ID_INPUT);
        driverTools.sendKeys(REFUND_REQUEST_ID_INPUT, refundId);
    }

    public void verifyFilteredRefundRequest(String expectedRefundId) {
        WebElement firstRowIdElement = driverTools.waitForElementToAppear(FIRST_ROW_REFUND_ID).orElseThrow();
        String actualRefundId = firstRowIdElement.getText().trim();
        String normalizedActualId = actualRefundId.replace("#", "");
        String normalizedExpectedId = expectedRefundId.replace("#", "");
        Assertion.assertThat("Verify the filtered Refund Request ID matches the input Expected ID: " + normalizedExpectedId + " | Actual ID: " + normalizedActualId, normalizedActualId, equalTo(normalizedExpectedId));
    }

    public void selectOrderType(String orderType) {
        driverTools.click(VERTICAL_DROPDOWN_BTN);
        Locator dynamicOption = new Locator(
                "DROPDOWN_OPTION_" + orderType.toUpperCase(), By.xpath("//*[@role='option' and normalize-space(.)='" + orderType + "']")
        );
        for (int i = 0; i < 5 && !driverTools.waitForElementToAppear(dynamicOption).isPresent(); i++) {
            driverTools.scrollAndClick(VERTICAL_DROPDOWN_BTN);
        }
        driverTools.click(dynamicOption);
    }

    public void verifyOrderTypeSelected(String expectedOrderType) {
        WebElement dropdownBtn = driverTools.waitForElementToAppear(VERTICAL_DROPDOWN_BTN).orElseThrow();
        String actualSelectedText = dropdownBtn.getText().trim();
        Assertion.assertThat("Verify the Vertical dropdown updated to the correct option", actualSelectedText, equalTo(expectedOrderType));
    }

    public String getFirstRowRefundRequestId() {
        String xpath = "//tbody/tr[1]/td[1]//span[contains(@class, 'Text_variant_activity')]";
        Locator firstRefundIdLoc = new Locator("FIRST_REFUND_ID", By.xpath(xpath));
        WebElement element = driverTools.waitForElementToAppear(firstRefundIdLoc, 5).orElseThrow();
        return element.getText().trim().replace("#", "");
    }

    /** Clicks Export To CSV and returns the downloaded file from ~/Downloads. */
    public File exportToCsv() throws Exception {
        driverTools.clickWithJavascriptExecutor(EXPORT_TO_CSV_BUTTON);
        return CoreFunctions.getDownloadedFileMacOS("ManualRefund");
    }

    public void deleteDownloadedFile(File file) {
        CoreFunctions.deleteFile(file.getAbsolutePath());
    }

    /** Results are displayed when the table has a data row or the empty-state message (no data for the range). */
    public boolean isSearchResultsDisplayed() {
        return driverTools.waitForElementToAppear(FIRST_ROW_REFUND_ID).isPresent()
                || driverTools.waitForElementToAppear(EMPTY_REFUND_MESSAGE).isPresent();
    }

    public String getFirstRowOrderId() {
        String text = driverTools.waitForElementToAppear(FIRST_ROW_ORDER_ID).orElseThrow().getText().trim();
        return text.split("\\s*-\\s*")[0];
    }

    public LocalDate getFirstRowRequestDate() {
        String text = driverTools.waitForElementToAppear(FIRST_ROW_REQUEST_DATE).orElseThrow().getText().trim();
        return java.time.LocalDateTime.parse(text, REQUEST_DATE_FORMAT).toLocalDate();
    }

    private void clickWithRetry(Locator locator) {
        RuntimeException last = null;
        for (int i = 0; i < 3; i++) {
            try {
                driverTools.scrollElementToCenter(locator);
                driverTools.click(locator);
                return;
            } catch (RuntimeException e) {
                last = e;
                driverTools.dummyWait(1);
            }
        }
        throw last;
    }

    // The calendar language follows the browser/user locale (e.g. "Bulan sebelumnya", "Oktober 2026"),
    // so navigate by month count and position instead of matching text.
    private void pickDate(Locator input, LocalDate date) {
        driverTools.clickWithJavascriptExecutor(input);
        long monthsBack = java.time.temporal.ChronoUnit.MONTHS.between(date.withDayOfMonth(1), LocalDate.now().withDayOfMonth(1));
        for (long i = 0; i < monthsBack; i++) {
            driverTools.clickWithJavascriptExecutor(CALENDAR_PREVIOUS_MONTH);
        }
        // After navigating back, the target month is the first (left) month of the open calendar.
        String day = String.valueOf(date.getDayOfMonth());
        driverTools.clickWithJavascriptExecutor(getCalendarDay(day));
    }

    /** Filters by Order Id and a Start/End date range (end date unlocks once the start date is chosen). */
    public void filterByOrderIdAndDateRange(String orderId, LocalDate startDate, LocalDate endDate) {
        driverTools.scrollElementToCenter(ORDER_ID_INPUT);
        driverTools.click(ORDER_ID_INPUT);
        driverTools.clear(ORDER_ID_INPUT);
        driverTools.sendKeys(ORDER_ID_INPUT, orderId);
        pickDate(START_DATE_ICON, startDate);
        driverTools.waitForElementToAppear(END_DATE_ENABLED_INPUT);
        pickDate(END_DATE_ICON, endDate);
    }

    /** Verifies the exported CSV has the expected columns and a row for the filtered order / refund request. */
    public void verifyExportedCsvDetails(File csvFile, String expectedOrderId, String expectedRefundRequestId) throws Exception {
        List<List<String>> records = CoreFunctions.getCSVContent(csvFile);
        records.removeIf(r -> r.isEmpty());
        Assertion.assertThat("Verify the CSV has a header and at least one data row", records.size() > 1, equalTo(true));

        List<String> header = records.get(0).stream().map(h -> h.replace("\uFEFF", "").replace("\"", "").trim()).toList();
        int orderIdCol = header.indexOf("OrderID");
        int refundIdCol = header.indexOf("Refund Request ID");
        Assertion.assertThat("Verify the CSV has the OrderID and Refund Request ID columns", orderIdCol >= 0 && refundIdCol >= 0, equalTo(true));

        boolean allRowsMatchOrder = true;
        boolean refundRequestFound = false;
        for (List<String> row : records.subList(1, records.size())) {
            if (row.size() <= Math.max(orderIdCol, refundIdCol)) {
                continue;
            }
            allRowsMatchOrder &= row.get(orderIdCol).replace("\"", "").trim().equals(expectedOrderId);
            refundRequestFound |= row.get(refundIdCol).replace("\"", "").replace("#", "").trim().equals(expectedRefundRequestId);
        }
        Assertion.assertThat("Verify every CSV row belongs to Order Id " + expectedOrderId, allRowsMatchOrder, equalTo(true));
        Assertion.assertThat("Verify the CSV contains Refund Request ID " + expectedRefundRequestId, refundRequestFound, equalTo(true));
    }
}
