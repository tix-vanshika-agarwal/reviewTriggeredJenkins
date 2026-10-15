package com.tiket.web.dashboard.page.ttd.Ignis.OrderReportPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hamcrest.Matchers.is;

public class IgnisOrderReportPageWeb<T extends WebDriver> extends WebPage<T> {
    public IgnisOrderReportPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> IgnisOrderReportPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new IgnisOrderReportPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new IgnisOrderReportPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator APPLY_FILTER_BUTTON = new Locator("APPLY_FILTER_BUTTON", By.xpath("//button[contains(@class,'button variant-secondary')]"));
    private static final Locator EMPTY_RESULT = new Locator("EMPTY_RESULT", By.xpath("//div[@class='general-empty-item order-report-empty']"));
    private static final Locator RESULT_TABLE_ROWS = new Locator("RESULT_TABLE_ROWS", By.xpath("//table/tbody/tr"));
    private static final Locator TOTAL_ORDERS_COUNT_TEXT = new Locator("TOTAL_ORDERS_COUNT_TEXT", By.xpath("//div[@class='total-order-box'][1]/p[2]"));
    private static final Locator TOTAL_TICKETS_COUNT_TEXT = new Locator("TOTAL_TICKETS_COUNT_TEXT", By.xpath("//div[@class='total-order-box'][2]/p[2]"));
    private static final Locator DATE_RANGE_TEXT = new Locator("DATE_RANGE_TEXT", By.xpath("//input[@name='dates']"));
    private static final Locator RESULT_TABLE_ROWS_TRANSACTION_DATE = new Locator("RESULT_TABLE_ROWS_TRANSACTION_DATE", By.xpath("//table/tbody/tr/td[4]"));
    private static final Locator RESULT_TABLE_ROWS_VISIT_DATE = new Locator("RESULT_TABLE_ROWS_VISIT_DATE", By.xpath("//table/tbody/tr/td[5]"));
    private static final Locator RESULT_TABLE_ROWS_SETTLEMENT_DATE = new Locator("RESULT_TABLE_ROWS_SETTLEMENT_DATE", By.xpath("//table/tbody/tr/td[15]"));
    private static final Locator RESULT_TABLE_ROWS_SETTLEMENT_STATUS = new Locator("RESULT_TABLE_ROWS_SETTLEMENT_STATUS", By.xpath("//table/tbody/tr/td[14]//div"));
    private static final Locator RESULT_TABLE_ROWS_REFUND_STATUS = new Locator("RESULT_TABLE_ROWS_REFUND_STATUS", By.xpath("//table/tbody/tr/td[17]//div"));
    private static final Locator SEARCH_BY_ORDER_BUTTON = new Locator("SEARCH_BY_ORDER_BUTTON", By.xpath("//div[@class='segmented-control']/button[2]"));
    private static final Locator SEARCH_BAR = new Locator("SEARCH_BAR", By.xpath("//input[@name='orderId']"));
    private static final Locator QUEUE_NUMBER_TEXT = new Locator("QUEUE_NUMBER_TEXT", By.xpath("//div[@class='ticket-info-text ticket-type']/span"));
    private static final Locator ORDER_CONFIG_BUTTON = new Locator("ORDER_CONFIG_BUTTON", By.xpath("//div[@class='order-summary-row']/button"));
    private static final Locator ORDER_CONFIG_SELECTIONS = new Locator("ORDER_CONFIG_SELECTIONS", By.xpath("//li[@role='menuitem']"));
    private static final Locator ORDER_ID_OPTION = new Locator("ORDER_ID_OPTION", By.xpath("//div[@class='modal__body']//div[@class='radio-container__item'][1]"));
    private static final Locator TICKET_ID_OPTION = new Locator("TICKET_ID_OPTION", By.xpath("//div[@class='modal__body']//div[@class='radio-container__item'][2]"));
    private static final Locator POPUP_PRIMARY_BUTTON = new Locator("POPUP_PRIMARY_BUTTON", By.xpath("//div[@class='modal__footer footer-actions']//button[contains(@class,'variant-primary')]"));
    private static final Locator DETAIL_POPUP_CROSS_BUTTON = new Locator("DETAIL_POPUP_CROSS_BUTTON", By.xpath("//div[@class='order-details']//button[@data-testid='btn-close']"));
    private static final Locator REFUND_OPTION_ORDER_DETAILS_TEXT = new Locator("REFUND_OPTION_ORDER_DETAILS_TEXT", By.xpath("//div[@class='order-details']//tbody/tr[./td[text()='Pilihan Refund' or text()='Refund Option']]/td[last()]"));
    private static final Locator REFUND_STATUS_ORDER_DETAILS_TEXT = new Locator("REFUND_STATUS_ORDER_DETAILS_TEXT", By.xpath("//div[@class='order-details']//tbody/tr[./td[text()='Status Refund' or text()='Refund Status']]//div"));
    private static final Locator PRODUCT_DROPDOWN = new Locator("PRODUCT_DROPDOWN", By.xpath("//div[contains(@class,'product-filter')]"));
    private static final Locator PRODUCT_DROPDOWN_SELECTION = new Locator("PRODUCT_DROPDOWN_SELECTION", By.xpath("//div[@class='menu-item']"));
    private static final Locator PRODUCT_DROPDOWN_SEARCH_BAR = new Locator("PRODUCT_DROPDOWN_SEARCH_BAR", By.xpath("//input[@name='filterable-select-listitem-search']"));
    private static final Locator PACKAGE_DROPDOWN = new Locator("PACKAGE_DROPDOWN", By.xpath("//div[@class='filters-item package-filter']//fieldset"));
    private static final Locator PACKAGE_DROPDOWN_SELECTION = new Locator("PACKAGE_DROPDOWN_SELECTION", By.xpath("//div[@class='menu-item']"));
    private static final Locator PACKAGE_DROPDOWN_SEARCH_BAR = new Locator("PACKAGE_DROPDOWN_SEARCH_BAR", By.xpath("//input[@name='filterable-select-listitem-search']"));
    private static final Locator FILTER_BY_DROPDOWN = new Locator("FILTER_BY_DROPDOWN", By.xpath("//div[@class='filters-item date-filter']//fieldset"));
    private static final Locator FILTER_BY_DROPDOWN_SELECTION = new Locator("FILTER_BY_DROPDOWN_SELECTION", By.xpath("//div[@class='menu-item']"));
    private static final Locator DATE_FILTER = new Locator("DATE_FILTER", By.xpath("//div[@class='filters-item date-range']"));
    private static final Locator CALENDAR_PREVIOUS_BUTTON = new Locator("CALENDAR_PREVIOUS_BUTTON", By.xpath("//div[@class='DayPicker-NavBar nav-bar']/button[1]"));
    private static final Locator CALENDAR_NEXT_BUTTON = new Locator("CALENDAR_NEXT_BUTTON", By.xpath("//div[@class='DayPicker-NavBar nav-bar']/button[2]"));
    private static final Locator CALENDAR_BUTTON_OK = new Locator("CALENDAR_BUTTON_OK", By.xpath("//button[contains(@class,'btn-ok')]"));
    private static final Locator LOADING_CONTAINER = new Locator("LOADING_CONTAINER", By.xpath("//div[@class='table-container__loading']"));
    private static final Locator SUBSIDY_DROPDOWN = new Locator("SUBSIDY_DROPDOWN", By.xpath("//div[@class='filters-item subsidy-filter']"));
    private static final Locator SUBSIDY_DROPDOWN_SELECTION = new Locator("SUBSIDY_DROPDOWN_SELECTION", By.xpath("//div[@class='menu-item']"));
    private static final Locator SUBSIDY_ORDER_DETAIL = new Locator("SUBSIDY_ORDER_DETAIL", By.xpath("//div[@class='order-details']//tbody/tr[./td[contains(text(),'Campaign Discount') or contains(text(),'Bundle Discount') or contains(text(),'Diskon Campaign') or contains(text(),'Diskon Bundle')]]"));
    private static final Locator REDEEM_STATUS_ORDER_DETAIL = new Locator("REDEEM_STATUS_ORDER_DETAIL", By.xpath("//div[@class='order-details__ticket-list-item ticket-info']//div[@class='ticket-info-heading-item'][1]/div[2]"));
    private static final Locator SETTLEMENT_DROPDOWN = new Locator("SETTLEMENT_DROPDOWN", By.xpath("//div[contains(@class, 'settlement-filter')]//fieldset"));
    private static final Locator SETTLEMENT_DROPDOWN_SELECTION = new Locator("SETTLEMENT_DROPDOWN_SELECTION", By.xpath("//div[@class='menu-item']"));
    private static final Locator REDEEM_DROPDOWN = new Locator("REDEEM_DROPDOWN", By.xpath("//div[contains(@class, 'redemption-filter')]"));
    private static final Locator REDEEM_DROPDOWN_SELECTION = new Locator("REDEEM_DROPDOWN_SELECTION", By.xpath("//div[@class='menu-item']"));
    private static final Locator REFUND_DROPDOWN = new Locator("REFUND_DROPDOWN", By.xpath("//div[contains(@class, 'refund-filter')]"));
    private static final Locator REFUND_DROPDOWN_SELECTION = new Locator("REFUND_DROPDOWN_SELECTION", By.xpath("//div[@class='menu-item']"));
    private static final Locator QUESTIONNAIRE_SECTIONS = new Locator("QUESTIONNAIRE_SECTIONS", By.xpath("//div[@class='ticket-additional-info']"));
    private static final Locator TABLE_PREVIOUS_BUTTON = new Locator("TABLE_PREVIOUS_BUTTON", By.xpath("//button[@class='nav__previous btn btn-text']"));
    private static final Locator TABLE_NEXT_BUTTON = new Locator("TABLE_NEXT_BUTTON", By.xpath("//button[@class='nav__next btn btn-text']"));
    private static final Locator MANAGE_ORDER_POPUP = new Locator("MANAGE ORDER_POPUP", By.xpath("//div[@class='manage-order-dialog']"));
    private static final Locator MANAGE_ORDER_POPUP_SELECTION = new Locator("MANAGE_ORDER_POPUP_SELECTION", By.xpath("//div[@class='manage-type-item radio-container']"));
    private static final Locator MANAGE_ORDER_POPUP_CONTINUE_BUTTON = new Locator("MANAGE_ORDER_POPUP_CONTINUE_BUTTON", By.xpath("//button[@data-testid='btn-continue']"));

    public void selectUpdateOptionManageOrderPopup(String option) {
        driverTools.waitForElementsToAppear(MANAGE_ORDER_POPUP);
        List<WebElement> options = driverTools.waitForElementsToAppear(MANAGE_ORDER_POPUP_SELECTION).orElse(null);

        if (options == null) {
            throw new NoSuchElementException("No options for manage order update option");
        }

        switch (option.toLowerCase()) {
            case "update visit date", "update tanggal kunjungan" -> {
                driverTools.click(options.get(0));
            }
            case "update validity", "update masa berlaku" -> {
                driverTools.click(options.get(1));
            }
        }
    }

    public void clickContinueOnManageOrderPopup() {
        driverTools.click(MANAGE_ORDER_POPUP_CONTINUE_BUTTON);
        driverTools.dummyWait(3);
    }

    // Export Pop Up Confirmation
    private static final Locator EXPORT_CONFIRMATION_POPUP = new Locator("EXPORT_CONFIRMATION_POPUP", By.xpath("//div[@class='export-order-confimation-popup']"));
    private static final Locator EXPORT_CONFIRMATION_POPUP_ORDER_ID_RADIO = new Locator("EXPORT_CONFIRMATION_POPUP_ORDER_ID_RADIO", By.xpath("//div[@class='radio-container__item'][1]"));
    private static final Locator EXPORT_CONFIRMATION_POPUP_TICKET_ID_RADIO = new Locator("EXPORT_CONFIRMATION_POPUP_TICKET_ID_RADIO", By.xpath("//div[@class='radio-container__item'][2]"));
    private static final Locator EXPORT_NOW_BUTTON = new Locator("EXPORT_NOW_BUTTON", By.xpath("//button[@class='button variant-primary size-medium horizontal-padding']"));

    public void selectExportRadioButton(String option) {
        driverTools.waitForElementToAppear(EXPORT_CONFIRMATION_POPUP, 2);
        switch (option.toLowerCase()) {
            case "order id" -> {
                driverTools.click(EXPORT_CONFIRMATION_POPUP_ORDER_ID_RADIO);
            }
            case "ticket id" -> {
                driverTools.click(EXPORT_CONFIRMATION_POPUP_TICKET_ID_RADIO);
            }
            default -> {
                throw new IllegalArgumentException("No option to select were given");
            }
        }
    }

    public void clickExportNowButton() {
        driverTools.click(EXPORT_NOW_BUTTON);
        driverTools.dummyWait(3);
    }


    public void clickApplyFilter() {
        driverTools.scrollElementToCenter(APPLY_FILTER_BUTTON);
        driverTools.click(APPLY_FILTER_BUTTON);
        driverTools.waitForElementToAppear(LOADING_CONTAINER);
        driverTools.waitForElementToDisappear(LOADING_CONTAINER);
    }

    public int getTotalOrderCountText() {
        String totalOrderText = driverTools.getText(TOTAL_ORDERS_COUNT_TEXT).orElseThrow();

        if (totalOrderText.contains("-")) {
            return 0;
        }

        return Integer.parseInt(totalOrderText);
    }

    public int getTotalOrderCountTableRow() {
        if (driverTools.waitForElementToAppear(EMPTY_RESULT).isPresent()) {
            return 0;
        }

        List<WebElement> rowElements = driverTools.elementsUntilExist(RESULT_TABLE_ROWS).get();
        return rowElements.size();
    }

    public Boolean verifyTableDateWithFilterDate(Locator tableColumn) {
        if (driverTools.waitForElementToAppear(EMPTY_RESULT).isPresent()) {
            Assertion.assertThat("Result is empty, no data to be verified", true, is(true));
            return true;
        }

        String appliedFilterDates = driverTools.getValue(DATE_RANGE_TEXT);
        String[] startDateArray = appliedFilterDates.split(" - ")[0].split(" ");
        LocalDate startDate = LocalDate.of(Integer.parseInt(startDateArray[2]), CoreFunctions.getMonthNumber(startDateArray[1]), Integer.parseInt(startDateArray[0]));
        String[] endDateArray = appliedFilterDates.split(" - ")[1].split(" ");
        LocalDate endDate = LocalDate.of(Integer.parseInt(endDateArray[2]), CoreFunctions.getMonthNumber(endDateArray[1]), Integer.parseInt(endDateArray[0]));

        List<String> rowDateElements = driverTools.getTexts(tableColumn);
        for (int i = 0; i < rowDateElements.size(); i++) {
            int date = Integer.parseInt(rowDateElements.get(i).split(" ")[0]);
            int month = CoreFunctions.getMonthNumber(rowDateElements.get(i).split(" ")[1]);
            int year = Integer.parseInt(rowDateElements.get(i).split(" ")[2]);
            LocalDate dataDate = LocalDate.of(year, month, date);

            Assertion.assertThat("Assert if data date is valid", dataDate.compareTo(startDate) >= 0 && dataDate.compareTo(endDate) <= 0, is(true));
        }

        return true;
    }

    public Boolean verifyTransactionDateWithFilterDate() {
        return verifyTableDateWithFilterDate(RESULT_TABLE_ROWS_TRANSACTION_DATE);
    }

    public Boolean verifyVisitDateWithFilterDate() {
        return verifyTableDateWithFilterDate(RESULT_TABLE_ROWS_VISIT_DATE);
    }

    public Boolean verifyVisitDateWithDateString(String dateStr) {
        if (driverTools.waitForElementToAppear(EMPTY_RESULT).isPresent()) {
            return true;
        }

        List<String> rowDates = driverTools.getTexts(RESULT_TABLE_ROWS_VISIT_DATE);
        for (String rowDate: rowDates) {
            if (!rowDate.equals(dateStr)) {
                return false;
            }
        }
        return true;
    }

    public Boolean verifySettlementDateWithFilterDate() {
        return verifyTableDateWithFilterDate(RESULT_TABLE_ROWS_SETTLEMENT_DATE);
    }

    public void goToPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {driver.get("https://gatotkaca.tiket.com/ignis/reports");}
            case PREPROD -> {driver.get("https://preprod.tiket.com/ignis/reports");}
            case PROD -> {driver.get("https://www.tiket.com/ignis/reports");}
        }
        driverTools.waitForWebToLoad();
    }

    public void setSearchByOrder() {
        driverTools.waitForElementToAppear(SEARCH_BY_ORDER_BUTTON);
        driverTools.click(SEARCH_BY_ORDER_BUTTON);
        driverTools.dummyWait(1);
    }

    public void inputOrderId(String orderId) {
        driverTools.waitForElementToAppear(SEARCH_BAR);
        driverTools.clickElementByCoordinateDWeb(SEARCH_BAR);
        driverTools.clearInput(SEARCH_BAR);
        if (CoreFunctions.isMacSystem()) {
            driverTools.sendKeys(SEARCH_BAR, orderId + Keys.RETURN);
        } else {
            driverTools.sendKeys(SEARCH_BAR, orderId + Keys.ENTER);
        }
        driverTools.dummyWait(1);
    }

    public void clickRowByOrderId(String orderId) {
        Locator locator = getRowLocatorByOrderId(orderId);
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);
    }

    private Locator getRowLocatorByOrderId(String orderId) {
        return new Locator("ROW_"+orderId, By.xpath("//tr[.//p[text()='"+orderId+"']]"));
    }

    public String getQueueNumber() {
        driverTools.waitForElementToAppear(QUEUE_NUMBER_TEXT);
        return driverTools.getText(QUEUE_NUMBER_TEXT).orElseThrow().split(": ")[1];
    }

    public void clickOrderConfigButton() {
        driverTools.waitForElementToAppear(ORDER_CONFIG_BUTTON);
        driverTools.click(ORDER_CONFIG_BUTTON);
        driverTools.dummyWait(1);
    }

    public void clickOrderConfigOptions(String targetOption) {
        String targetOptionEn = targetOption.split("/")[0].toLowerCase();
        String targetOptionId = targetOption.split("/")[1].toLowerCase();

        List<WebElement> dropdownSelection = driverTools.elementsUntilExist(ORDER_CONFIG_SELECTIONS).get();

        boolean found = false;
        for(WebElement e : dropdownSelection) {
            String elementText = driverTools.getText(e);
            if (elementText.toLowerCase().contains(targetOptionEn) || elementText.toLowerCase().contains(targetOptionId)) {
                driverTools.click(e);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new NoSuchElementException("There is no option for '"+targetOption+"'");
        }
    }

    public void chooseExportBy(String option) {
        switch (option.toLowerCase()) {
            case "orderid" -> {driverTools.click(ORDER_ID_OPTION);}
            case "ticketid" -> {driverTools.click(TICKET_ID_OPTION);}
        }
    }

    public void clickPopupPrimaryButton() {
        driverTools.waitForElementToAppear(POPUP_PRIMARY_BUTTON);
        driverTools.click(POPUP_PRIMARY_BUTTON);
        driverTools.waitForWebToLoad();
    }

    public void closeDetailPopup() {
        driverTools.waitForElementToAppear(DETAIL_POPUP_CROSS_BUTTON);
        driverTools.click(DETAIL_POPUP_CROSS_BUTTON);
    }

    public String getQueueNumberDownloadedFileByOrderId(String orderId) throws IOException {
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        FileInputStream file = new FileInputStream(CoreFunctions.getLastModified(downloadDir));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        Row row = rowIterator.next(); // skip header
        String result = "";
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            String dataPointOrderId = row.getCell(0).getStringCellValue();
            if (orderId.equalsIgnoreCase(dataPointOrderId)) {
                result = String.valueOf((int) row.getCell(13).getNumericCellValue());
            }
        }

        file.close();

        return result;
    }

    public String isRefundOptionVisible() {
        WebElement element = driverTools.waitForElementToAppear(REFUND_OPTION_ORDER_DETAILS_TEXT).get();
        return driverTools.getText(element);
    }

    public String isRefundStatusVisible() {
        WebElement element = driverTools.waitForElementToAppear(REFUND_STATUS_ORDER_DETAILS_TEXT).get();
        return driverTools.getText(element);
    }

    public void setFilterWithSearchBar(String filterType, String filter, Locator dropdown, Locator dropdownSelection, Locator searchBar) {
        driverTools.waitForElementToAppear(dropdown);
        driverTools.click(dropdown);
        driverTools.dummyWait(1);

        inputDropdownSearchBar(filter.toLowerCase(), searchBar);
        driverTools.dummyWait(2);

        List<WebElement> selections = driverTools.elementsUntilExist(dropdownSelection).orElseThrow();
        boolean found = false;
        for (WebElement e : selections) {
            String elementText = driverTools.getText(e).toLowerCase();
            if (elementText.equalsIgnoreCase(filter)) {
                found = true;
                driverTools.click(e);
                break;
            }
        }

        if (!found) {
            throw new NoSuchElementException(String.format("There is no %s '%s'", filterType, filter));
        }
    }

    public void setProductFilter(String productName) {
        setFilterWithSearchBar("Product", productName, PRODUCT_DROPDOWN, PRODUCT_DROPDOWN_SELECTION, PRODUCT_DROPDOWN_SEARCH_BAR);
    }

    public void setPackageFilter(String packageName) {
        setFilterWithSearchBar("Package", packageName, PACKAGE_DROPDOWN, PACKAGE_DROPDOWN_SELECTION, PACKAGE_DROPDOWN_SEARCH_BAR);
    }

    private void inputDropdownSearchBar(String name, Locator searchBar) {
        driverTools.waitForElementToAppear(searchBar);
        driverTools.clearInput(searchBar);
        driverTools.sendKeys(searchBar, name);
    }

    public void selectFilterBy(String filter) {
        driverTools.scrollElementToCenter(FILTER_BY_DROPDOWN);
        driverTools.click(FILTER_BY_DROPDOWN);

        List<WebElement> filters = driverTools.waitForElementsToAppear(FILTER_BY_DROPDOWN_SELECTION).orElse(null);

        if (filters == null) {
            throw new NoSuchElementException("There is no selection for Filter By Dropdown");
        }
        switch(filter){
            case "transaction", "transaksi" -> {
                if (filters.get(0) != null) {
                    driverTools.click(filters.get(0));
                }
            }
            case "settlement", "pembayaran" -> {
                if (filters.get(1) != null) {
                    driverTools.click(filters.get(1));
                }
            }
            case "visit", "kunjungan" -> {
                if (filters.get(2) != null) {
                    driverTools.click(filters.get(2));
                }
            }
            default -> {
                throw new NoSuchElementException("There is no selection with that filter");
            }
        }
    }

    public void setDateRangeFilter(String dateRange) {
        int minRange = Integer.parseInt(dateRange.split("-")[0]);
        int maxRange = Integer.parseInt(dateRange.split("-")[1]);
        int targetMinusDate = CoreFunctions.generateRandomInt(minRange, maxRange);

        LocalDate todayDate = LocalDate.now();
        LocalDate targetStartdate = todayDate.minusDays(targetMinusDate);

        int diffMonthCount = (((todayDate.getYear()-1) * 12) + todayDate.getMonthValue()) - (((targetStartdate.getYear()-1) * 12) + targetStartdate.getMonthValue());

        driverTools.waitForElementToAppear(DATE_FILTER);
        driverTools.click(DATE_FILTER);

        for (int i = 0 ; i < diffMonthCount ; i++) {
            driverTools.waitForElementToAppear(CALENDAR_PREVIOUS_BUTTON);
            driverTools.click(CALENDAR_PREVIOUS_BUTTON);
        }

        Locator locator = getDateLocatorByDate(targetStartdate);
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);

        for (int i = 0 ; i < diffMonthCount ; i++) {
            driverTools.waitForElementToAppear(CALENDAR_NEXT_BUTTON);
            driverTools.click(CALENDAR_NEXT_BUTTON);
        }

        locator = getDateLocatorByDate(todayDate);
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);

        driverTools.waitForElementToAppear(CALENDAR_BUTTON_OK);
        driverTools.click(CALENDAR_BUTTON_OK);
    }

    public void setDateRangeFilterWithDateRange(String startDateString, String endDateString) {
        driverTools.click(DATE_FILTER);
        clickCalendarDate(startDateString);
        clickCalendarDate(endDateString);
        driverTools.click(CALENDAR_BUTTON_OK);
    }

    private void clickCalendarDate(String targetDateString) {
        LocalDate todaysDate = LocalDate.now();
        LocalDate targetDate  = CoreFunctions.convertStringToTemporal(targetDateString, "dd/MM/yyyy", LocalDate.class);

        int monthDiff = CoreFunctions.getMonthDifference(todaysDate, targetDate);
        int diff = 0;
        if (monthDiff < -1) {
            for (int i = 0; i < monthDiff*-1; i++) {
                diff++;
                driverTools.click(CALENDAR_PREVIOUS_BUTTON);
            }
            clickDate(targetDateString);
            for (int i = 0; i < diff; i++) {
                driverTools.click(CALENDAR_NEXT_BUTTON);
            }
        } else if (monthDiff > 1) {
            for (int i = 0; i < monthDiff-1; i++) {
                diff++;
                driverTools.click(CALENDAR_NEXT_BUTTON);
            }
            for (int i = 0; i < diff; i++) {
                driverTools.click(CALENDAR_PREVIOUS_BUTTON);
            }
            clickDate(targetDateString);
        } else {
            clickDate(targetDateString);
        }
    }

    private void clickDate(String targetDateString) {
        String target = CoreFunctions.convertDateFormat(targetDateString, "dd/MM/yyyy", "E MMM dd yyyy");
        Locator targetLocator = new Locator("TARGET_DATE", By.xpath(String.format("//div[contains(@class,'DayPicker-Day') and @aria-label='%s']", target)));
        driverTools.click(targetLocator);
    }

    private Locator getDateLocatorByDate(LocalDate targetDate) {
        int targetDateNumber = targetDate.getDayOfMonth();
        return new Locator("DATE_" + targetDateNumber, By.xpath("(//div[@class='DayPicker-Month'][1]//div[contains(@class,'DayPicker-Day') and @aria-disabled='false'])["+targetDateNumber+"]"));
    }

    public int getRowSizeOfExportedFile() throws IOException {
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        FileInputStream file = new FileInputStream(CoreFunctions.getLastModified(downloadDir));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);

        int result = sheet.getLastRowNum();

        file.close();

        return result;
    }

    public void setSubsidyFilter(String subsidyFilter) {
        String targetSubsidyFilterId = subsidyFilter.split("/")[0];
        String targetSubsidyFilterEn = subsidyFilter.split("/")[1];

        driverTools.waitForElementToAppear(SUBSIDY_DROPDOWN);
        driverTools.click(SUBSIDY_DROPDOWN);
        driverTools.dummyWait(1);

        List<WebElement> dropdownSelection = driverTools.elementsUntilExist(SUBSIDY_DROPDOWN_SELECTION).orElseThrow();
        boolean found = false;
        for (WebElement e : dropdownSelection) {
            String elementText = driverTools.getText(e).toLowerCase();
            if (elementText.equalsIgnoreCase(targetSubsidyFilterId) || elementText.equalsIgnoreCase(targetSubsidyFilterEn)) {
                found = true;
                driverTools.click(e);
                break;
            }
        }

        if (!found) {
            throw new NoSuchElementException("There is no subsidy filter '"+subsidyFilter+"'");
        }
    }

    public void selectFilter(String filterType, String filter, Locator dropdown, Locator dropdownSelection) {
        driverTools.waitForElementToAppear(dropdown);
        driverTools.click(dropdown);
        driverTools.dummyWait(1);

        List<WebElement> dropdownSelections = driverTools.elementsUntilExist(dropdownSelection).orElseThrow();
        boolean found = false;
        for (WebElement e : dropdownSelections) {
            String elementText = driverTools.getText(e).toLowerCase();
            if (elementText.equalsIgnoreCase(filter)) {
                found = true;
                driverTools.click(e);
                break;
            }
        }

        if (!found) {
            throw new NoSuchElementException(String.format("There is no %s filter '%s'", filterType, filter));
        }
    }

    public void setSettlementFilter(String filter) {
        selectFilter("settlement", filter, SETTLEMENT_DROPDOWN, SETTLEMENT_DROPDOWN_SELECTION);
    }

    public void setRedeemFilter(String filter) {
        selectFilter("redeem", filter, REDEEM_DROPDOWN, REDEEM_DROPDOWN_SELECTION);
    }

    public void setRefundFilter(String filter) {
        selectFilter("refund", filter, REFUND_DROPDOWN, REFUND_DROPDOWN_SELECTION);
    }

    public void clickRowByIndex(int index) {
        Locator locator = getRowLocatorByIndex(index);
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);
    }

    private Locator getRowLocatorByIndex(int index) {
        return new Locator("ROW_"+index, By.xpath("//tbody/tr["+index+"]"));
    }

    public boolean isSubsidyAttributeVisible() {
        return driverTools.waitForElementToAppear(SUBSIDY_ORDER_DETAIL).isPresent();
    }

    public boolean isQuestionnaireSectionVisible() {
        List<WebElement> questionnaireElement = driverTools.elements(QUESTIONNAIRE_SECTIONS).get();

        return !questionnaireElement.isEmpty();
    }

    public String getExtendedValidityDate(String orderDetailId) {
        Locator locator = getExtendedValidityDateLocatorByOrderDetailId(orderDetailId);
        return driverTools.getText(driverTools.waitForElementToAppear(locator).orElseThrow());
    }

    private Locator getExtendedValidityDateLocatorByOrderDetailId(String orderDetailId) {
        return new Locator("EXTENDED_VALIDITY_DATE", By.xpath("//tbody/tr[./td[2][text()='"+orderDetailId+"']]/td[7]"));
    }
    
    public String getOrderDetailId(String orderId) {
        Locator locator = getOrderdetailIdLocatorByOrderId(orderId);
        return driverTools.getText(driverTools.waitForElementToAppear(locator).orElseThrow());
    }

    private Locator getOrderdetailIdLocatorByOrderId(String orderId) {
        return new Locator("ORDER_DETAIL_ID", By.xpath("//tbody/tr[.//p[text()='"+orderId+"']][1]/td[2]"));
    }

    public boolean isOrderRefundable() {
        driverTools.waitForElementToAppear(REFUND_OPTION_ORDER_DETAILS_TEXT, 3);
        String refundOption = driverTools.getText(REFUND_OPTION_ORDER_DETAILS_TEXT).orElseThrow().toLowerCase();
        return refundOption.equalsIgnoreCase("refundable");
    }

    public boolean verifySettlementStatus(String filter) {
        if (filter.equalsIgnoreCase("all") || filter.equalsIgnoreCase("manual")) return true;

        clickApplyFilter();

        if (driverTools.waitForElementToAppear(EMPTY_RESULT).isPresent()) return true;

        while(true) {
            List<WebElement> datas = driverTools.elementsUntilExist(RESULT_TABLE_ROWS_SETTLEMENT_STATUS).orElseThrow();
            for (WebElement data: datas) {
                if (!filter.equalsIgnoreCase(driverTools.getText(data))) return false;
            }

            if (driverTools.isElementEnabled(TABLE_NEXT_BUTTON)) {
                driverTools.scrollElementToCenter(TABLE_NEXT_BUTTON);
                driverTools.click(TABLE_NEXT_BUTTON);
            } else {
                break;
            }
        }
        return true;
    }

    public boolean verifyRefundStatus(String filter) {
        if (filter.equalsIgnoreCase("all")) return true;

        clickApplyFilter();

        if (driverTools.waitForElementToAppear(EMPTY_RESULT).isPresent()) return true;

        while(true) {
            List<WebElement> datas = driverTools.elementsUntilExist(RESULT_TABLE_ROWS_REFUND_STATUS).orElseThrow();
            for (WebElement data: datas) {
                switch(filter.toLowerCase()) {
                    case "no refund request", "tidak ada pengajuan" -> {
                        if (!driverTools.getText(data).equalsIgnoreCase("no refund request") &&
                                !driverTools.getText(data).equalsIgnoreCase("tidak ada pengajuan")
                        ) return false;
                    }
                    case "refunded", "berhasil refund" -> {
                        if (!driverTools.getText(data).equalsIgnoreCase("refunded") &&
                                !driverTools.getText(data).equalsIgnoreCase("berhasil refund")
                        ) return false;
                    }
                    case "rejected" -> {
                        if (!driverTools.getText(data).equalsIgnoreCase("rejected")
                        ) return false;
                    }
                    case "expired" -> {
                        if (!driverTools.getText(data).equalsIgnoreCase("expired")
                        ) return false;
                    }
                    case "on process", "diproses" -> {
                        if (!driverTools.getText(data).equalsIgnoreCase("on process") &&
                                !driverTools.getText(data).equalsIgnoreCase("diproses")
                        ) return false;
                    }
                }
            }

            if (driverTools.isElementEnabled(TABLE_NEXT_BUTTON)) {
                driverTools.scrollElementToCenter(TABLE_NEXT_BUTTON);
                driverTools.click(TABLE_NEXT_BUTTON);
            } else {
                break;
            }
        }
        return true;
    }

    public boolean verifyRedeemStatus(String filter) {
        if (filter.equalsIgnoreCase("all")) return true;

        clickApplyFilter();

        if (driverTools.waitForElementToAppear(EMPTY_RESULT).isPresent()) return true;

        while(true) {
            List<WebElement> rowElements = driverTools.elementsUntilExist(RESULT_TABLE_ROWS).get();
            for (WebElement row : rowElements) {
                driverTools.scrollElementToCenter(row);
                driverTools.click(row);
                List<WebElement> tickets = driverTools.elementsUntilExist(REDEEM_STATUS_ORDER_DETAIL).get();
                int redeemCounter = 0;
                for (WebElement ticket: tickets) {
                    switch (driverTools.getText(ticket).toLowerCase()) {
                        case "redeemed", "sudah ditukar" -> {
                            redeemCounter++;
                        }
                    }
                }

                switch (filter.toLowerCase()) {
                    case "redeemed" -> {
                        if (redeemCounter != tickets.size()) return false;
                    }
                    case "partially redeemed" -> {
                        if (redeemCounter == tickets.size()) return false;
                    }
                    case "not redeemed" -> {
                        if (redeemCounter != 0) return false;
                    }
                }
                driverTools.click(DETAIL_POPUP_CROSS_BUTTON);
            }

            if (driverTools.isElementEnabled(TABLE_NEXT_BUTTON)) {
                driverTools.scrollElementToCenter(TABLE_NEXT_BUTTON);
                driverTools.click(TABLE_NEXT_BUTTON);
            } else {
                break;
            }
        }
        return true;
    }

    public boolean checkIfTableResultIsEmpty() {
        if (driverTools.waitForElementToAppear(EMPTY_RESULT).orElse(null) != null) {
            Assertion.assertThat("Result is empty, no data to be converted to .XLS", true, is(true));
            return true;
        }
        return false;
    }

}
