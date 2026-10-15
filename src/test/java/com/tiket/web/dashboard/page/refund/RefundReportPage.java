package com.tiket.web.dashboard.page.refund;

import com.tiket.core.CoreFunctions;
import com.tiket.dbclient.TiketMysqlDBManager;
import com.tiket.dto.RefundRequestDTO;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import nl.jiankai.mapper.ResultSetMapperFactory;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;

public class RefundReportPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator WRAP_CCP = new Locator("WRAP_CCP", new By.ByXPath("//div[@class='wrap-ccp']"));
    private static final Locator MENU_REFUND_REPORT = new Locator("MENU_REFUND_REPORT", new By.ByXPath("//span[text()='Report']"));
    private static final Locator FILTER_PANEL_REPORT = new Locator("FILTER_PANEL_REPORT", new By.ByXPath("//h1[text()='Refund Report']/following-sibling::section"));
    private static final Locator IMAGE_DATA_NOT_FOUND = new Locator("IMAGE_DATA_NOT_FOUND", new By.ByXPath("//img[@alt='empty refund list image']"));
    private static final Locator TITLE_DATA_NOT_FOUND = new Locator("TITLE_DATA_NOT_FOUND", new By.ByXPath("//img[@alt='empty refund list image']/following-sibling::p[1]"));
    private static final Locator DESC_DATA_NOT_FOUND = new Locator("DESC_DATA_NOT_FOUND", new By.ByXPath("//img[@alt='empty refund list image']/following-sibling::p[2]"));
    private static final Locator FILTER_DATE_MONTH_YEAR = new Locator("FILTER_DATE_MONTH_YEAR", new By.ByXPath("//div[@class='DayPicker-Caption']/div"));
    private static final Locator FILTER_DATE_PREV_MONTH = new Locator("FILTER_DATE_PREV_MONTH", new By.ByXPath("//span[contains(@class,'nav left')]"));
    private static final Locator FILTER_DATE_NEXT_MONTH = new Locator("FILTER_DATE_NEXT_MONTH", new By.ByXPath("//span[contains(@class,'nav right')]"));
    private static final Locator FILTER_DATE_DONE = new Locator("FILTER_DATE_DONE", new By.ByXPath("//label[@class='submit-date-time']"));
    private static final Locator DROPDOWN_FILTER_OPTION = new Locator("DROPDOWN_FILTER_OPTION", new By.ByXPath("//div[contains(@class,'dropdown-menu__option')]"));
    private static final Locator TABLE_LOADING = new Locator("TABLE_LOADING", new By.ByXPath("//td[contains(@class, 'text-center')]//div[contains(@class, 'rounded-md')]"));
    private static final Locator ERROR_NOT_FILLED = new Locator("ERROR_NOT_FILLED", new By.ByXPath("//div[@class='description danger']"));
    private static final Locator DOWNLOAD_EXCEL = new Locator("DOWNLOAD_EXCEL", new By.ByXPath("//button[contains(text(),'Export To Excel')]"));
    private static final Locator UPLOAD_FILE = new Locator("UPLOAD_FILE", new By.ByXPath("//input[@data-testid='input-file-upload']"));
    private static Locator getButtonByName(String name) {
        return new Locator("BUTTON_"+name.toUpperCase(), new By.ByXPath(("//button[text()='"+name+"']")));
    }
    private static Locator getDayFilterDate(String date) {
        return new Locator("FILTER_DATE_DAY", new By.ByXPath("//span[contains(@aria-label,'" + date + "')]"));
    }
    private static Locator getFilterDate(String filterDate) {
        return new Locator("FILTER_"+filterDate.toUpperCase(), new By.ByXPath("//input[@placeholder='"+filterDate+"']"));
    }
    private static Locator getFilterInput(String filter) {
        return new Locator("FILTER_INPUT_"+filter.toUpperCase(), new By.ByXPath("//label[text()='"+filter+"']/preceding-sibling::input"));
    }
    private static Locator getFilterDropdown(String filter) {
        return new Locator("FILTER_DROPDOWN_"+filter.toUpperCase(), new By.ByXPath("//label[text()='"+filter+"']/preceding-sibling::div"));
    }
    private static Locator getDropdownByOption(String option) {
        return new Locator("DROPDOWN_OPTION_"+option.toUpperCase(), new By.ByXPath("//div[contains(@class,'dropdown-menu__option')][text()='"+option+"']"));
    }
    private static Locator getColumnValue(String columnName) {
        return new Locator("COLUMN_VALUE", new By.ByXPath("//table[@data-testid='table']/tbody/tr[not(contains(@data-testid,'table-collapse'))]/td[position()=count(//span[text()='"+columnName+"']/ancestor::th/preceding-sibling::th)+1]"));
    }
    private static Locator buttonSortColumn(String columnName) {
        return new Locator("SORT_COLUMN_"+columnName.toUpperCase(), new By.ByXPath("//span[text()='"+columnName+"']/following-sibling::i"));
    }
    private static Locator buttonMovePage(boolean isNext) {
        String action = isNext ? "Next" : "Previous";
        return new Locator("MOVE_PAGE_"+action.toUpperCase(), new By.ByXPath("//a[contains(@aria-label,'"+action+"')]/parent::li"));
    }
    private static Locator getFlashUploadCSV(String flash) {
        return new Locator("FLASH_UPLOAD_"+flash.toUpperCase(), new By.ByXPath("//div[@class='flash "+flash+"']"));
    }
    
    public final Map<String, String> refundType = new HashMap<String, String>() {{
        put("erefund", "E-Refund");
        put("b2b-refund", "B2B Refund");
        put("middleware-refund", "Middleware Refund");
        put("reschedule", "Reschedule");
    }};

    public RefundReportPage(Session<T> session) { super(session); }

    public List<RefundRequestDTO> getRefundDataFromDB(String query) {
        var db = TiketMysqlDBManager.getInstance("refund");
        try {
            List<RefundRequestDTO> dbExecute = ResultSetMapperFactory.getResultSetMapperIdentity().map(db.executeQuery(query),RefundRequestDTO.class);
            if (dbExecute.stream().count() < 1) {
                throw new RuntimeException("Data not found in Database!");
            }
            logger.log("Retrieved data -> "+dbExecute.toString());
            return dbExecute;
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
    
    public void goToRefundReport() {
        WebElement report = driverTools.waitForElementToAppear(MENU_REFUND_REPORT).orElseThrow();
        driverTools.click(report);
    }

    public void clickButton(String name) {
        WebElement button = driverTools.waitForElementToAppear(getButtonByName(name)).orElseThrow();
        driverTools.scrollToElement(button);
        driverTools.click(button);
        driverTools.waitForElementToDisappear(TABLE_LOADING);
    }

    public boolean imageDataNotFoundShown() {
        Optional<WebElement> image = driverTools.waitForElementToAppear(IMAGE_DATA_NOT_FOUND, 1);
        boolean isImageDataNotFoundShown = image.isPresent();
        logger.log("T-man Image shown on Refund Report -> "+isImageDataNotFoundShown);
        return isImageDataNotFoundShown;
    }

    public boolean isPanelFilterShown() {
        Optional<WebElement> filter = driverTools.waitForElementToAppear(FILTER_PANEL_REPORT);
        boolean isShownFilter = filter.isPresent();
        logger.log("is Filter panel shown on Refund Report -> "+isShownFilter);
        return isShownFilter;
    }

    public boolean isTitleEmptyDataShown(String title) {
        Optional<WebElement> titleElement = driverTools.waitForElementToAppear(TITLE_DATA_NOT_FOUND);
        if (titleElement.isEmpty()) {
            logger.fail("Title not shown on Refund Report");
            return false;
        } else {
            String titleText = driverTools.getText(titleElement.get());
            if (!titleText.equals(title)) {
                logger.fail("Title wording incorrect on Refund Report. expected -> "+title+". Actual -> "+titleText);
                return false;
            }
        }
        return true;
    }

    public boolean isDescEmptyDataShown(String desc) {
        Optional<WebElement> descElement = driverTools.waitForElementToAppear(DESC_DATA_NOT_FOUND);
        if (descElement.isEmpty()) {
            logger.fail("Description not shown on Refund Report");
            return false;
        } else {
            String descText = driverTools.getText(descElement.get());
            if (!descText.equals(desc)) {
                logger.fail("Description wording incorrect on Refund Report. expected -> "+desc+". Actual -> "+descText);
                return false;
            }
        }
        return true;
    }

    public String getCurrentURL() {
        driverTools.dummyWait(2);
        return driverTools.getCurrentURL();
    }

    public void clickFilterDate(String filterName) {
        WebElement filter = driverTools.waitForElementToAppear(getFilterDate(filterName)).orElseThrow();
        driverTools.click(filter);
    }

    public void selectMonthPickerDate(YearMonth dateToCompare) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        String monthYear = driverTools.getText(FILTER_DATE_MONTH_YEAR).orElseThrow();
        YearMonth nowMonthYear = YearMonth.parse(monthYear, formatter);
        int comparison = nowMonthYear.compareTo(dateToCompare);
        for (int i = 0; i < Math.abs(comparison); i++) {
            if (comparison > 0) {
                driverTools.click(FILTER_DATE_PREV_MONTH);
            } else {
                driverTools.click(FILTER_DATE_NEXT_MONTH);
            }
        }
    }

    public void selectDate(String startDate, String endDate) {
        String[] dateStart = startDate.split(" ", 2);
        String[] dateEnd = endDate.split(" ", 2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        YearMonth startMonthYear = YearMonth.parse(dateStart[1], formatter);
        YearMonth endMonthYear = YearMonth.parse(dateEnd[1], formatter);

        //select start date
        selectMonthPickerDate(startMonthYear);
        driverTools.click(driverTools.waitForElementToAppear(getDayFilterDate(dateStart[0])).orElseThrow());

        //select end date
        selectMonthPickerDate(endMonthYear);
        driverTools.click(driverTools.waitForElementToAppear(getDayFilterDate(dateEnd[0])).orElseThrow());
        driverTools.click(FILTER_DATE_DONE);
    }

    public void selectDateDirectlyFromCalendar(String startDate, String endDate) {
        driverTools.click(driverTools.waitForElementToAppear(getDayFilterDate(startDate)).orElseThrow());
        driverTools.click(driverTools.waitForElementToAppear(getDayFilterDate(endDate)).orElseThrow());
    }

    public void movePageUntilEnd(boolean isNext) {
        String attribute = driverTools.getValueAttribute(buttonMovePage(isNext), "class");
        String requestId = driverTools.getText(driverTools.waitForElementToAppear(getColumnValue("Refund Request ID")).orElseThrow());
        String newRequestId = "";
        while (!attribute.contains("disabled")) {
            driverTools.click(buttonMovePage(isNext));
            attribute = driverTools.getValueAttribute(driverTools.waitForElementToAppear(buttonMovePage(isNext)).orElseThrow(), "class");
            newRequestId = driverTools.getText(driverTools.waitForElementToAppear(getColumnValue("Refund Request ID")).orElseThrow());
            if (requestId.equals(newRequestId)) {
                Assertion.assertThat("Button not disabled", attribute, containsString("disabled"));
            }
        }
        driverTools.waitForElementToDisappear(TABLE_LOADING);
    }

    public boolean verifyDataRequestDate(boolean isStartDate, String inputDate) throws DateTimeParseException {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d MMMM yyyy");
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("EEE, d MMM yyyy - HH:mm");
        LocalDate filterDate = LocalDate.parse(inputDate, dateFormat);

        List<WebElement> requestDateRowList;
        String requestDateRow;
        if (isStartDate) {
            movePageUntilEnd(true);
            driverTools.scrollIntoWebView(WRAP_CCP);
            requestDateRowList = driverTools.elements(getColumnValue("Request Date")).orElseThrow();
            requestDateRow = driverTools.getText(requestDateRowList.get(requestDateRowList.size()-1));
        } else {
            movePageUntilEnd(false);
            driverTools.scrollIntoWebView(WRAP_CCP);
            requestDateRowList = driverTools.elements(getColumnValue("Request Date")).orElseThrow();
            requestDateRow = driverTools.getText(requestDateRowList.get(0));
        }

        LocalDate rowDate = LocalDate.parse(requestDateRow, dateTimeFormat);
        boolean isCorrect = isStartDate ? rowDate.isAfter(filterDate) || rowDate.isEqual(filterDate) : rowDate.isBefore(filterDate) || rowDate.isEqual(filterDate);
        return isCorrect;
    }

    public String getFilterDataFromDTO(RefundRequestDTO result, String dataName) {
        switch (dataName) {
            case "Refund Request ID":
                return String.valueOf(result.getId());
            case "Order ID":
                return String.valueOf(result.getReference_order_id());
            default:
                throw new NotImplementedException("Unimplemented dataName!");
        }
    }

    public void inputDataOnFilter(String filterName, String data) {
        WebElement filter = driverTools.waitForElementToAppear(getFilterInput(filterName)).orElseThrow();
        driverTools.click(filter);
        driverTools.sendKeys(filter, data);
        logger.log("Input this data: "+data+" on this Filter: "+filterName);
    }

    public void clickFilterDropdown(String filterName) {
        WebElement filter = driverTools.waitForElementToAppear(getFilterDropdown(filterName)).orElseThrow();
        driverTools.click(filter);
    }

    public String getRandomOptionFromDropdownFilter() {
        List<WebElement> dropdown = driverTools.elements(DROPDOWN_FILTER_OPTION).orElseThrow();
        int index = CoreFunctions.generateRandomInt(0, dropdown.size()-1);
        logger.log("Got random number for index: "+index);
        return driverTools.getText(dropdown.get(index));
    }

    public void selectFilterDropdownByOption(String option) {
        WebElement dropdown = driverTools.waitForElementToAppear(getDropdownByOption(option)).orElseThrow();
        driverTools.click(dropdown);
        logger.log("Set filter with this dropdown value: "+option);
    }

    public boolean isOtherFilterDisabled(String filterDate) {
        Object disabled = driverTools.getValueAttribute(getFilterDate(filterDate), "disabled");
        if (disabled.equals(null)) {
            logger.fail(filterDate+" is not disabled");
            return false;
        }
        return true;
    }

    public boolean isErrorNotFilled(String expected) {
        Optional<WebElement> error = driverTools.waitForElementToAppear(ERROR_NOT_FILLED);
        if (error.isEmpty()) {
            logger.fail("Error Not Filled not shown");
            return false;
        }
        String message = driverTools.getText(error.get());
        if (!message.equals(expected)) {
            logger.fail("Incorrect error message. expected: "+expected+", actual message: "+message);
            return false;
        }
        return true;
    }

    public boolean verifyRowValueOfColumn(String columnName, String expected) {
        expected = expected.trim();
        if (columnName.equals("Protect Product Name") && expected.equals("100% Refund")) expected = "100pRefund";
        List<String> rowsValue = driverTools.getTexts(getColumnValue(columnName));
        for (String value : rowsValue) {
            value = value.trim();
            boolean isCorrect = columnName.equals("Refund Method") ? value.contains(expected) : value.equals(expected);
            if (!isCorrect) {
                logger.log("Expected value: "+expected+" is not same with actual value: "+value);
                return false;
            }
        }
        return true;
    }

    public void clickDownloadExcel() {
        WebElement button = driverTools.waitForElementToAppear(DOWNLOAD_EXCEL).orElseThrow();
        driverTools.click(button);
    }

    public String excelFilenameBuilderDateD_MMMM_YYYY(String startDate, String endDate) throws DateTimeParseException{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        LocalDate startLocalDate = LocalDate.parse(startDate, formatter);
        LocalDate endLocalDate = LocalDate.parse(endDate, formatter);
        String fileName = "RefundReport_"+ startLocalDate +" 00_00_00_"+ endLocalDate +" 23_59_59.xlsx";
        logger.log("Filename Builder -> "+fileName);
        return fileName;
    }

    public boolean verifyDownloadedExcel(String expectedFilename, String timeOutMiliSec) {
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        logger.log("Download folder: "+downloadDir);
        logger.log("expected filename: "+expectedFilename);
        boolean fileDownloaded = false;

        long startTime = Instant.now().toEpochMilli();
        logger.log("Start time: "+startTime);
        long waitTime = startTime + Integer.parseInt(timeOutMiliSec);
        logger.log("Wait time: "+waitTime);

        while (Instant.now().toEpochMilli() < waitTime){
            File[] listFiles = new File(downloadDir).listFiles();
            for (File file : listFiles){
                if (file.getName().equals(expectedFilename)){
                    String[] listFilesName = new File(downloadDir).list(); //logging purpose
                    for (String fileName : listFilesName) { logger.log("File name on dir: "+fileName); }
                    fileDownloaded = true;
                    if (!file.delete()) logger.warn("Failed to delete file -> "+file.getName());
                    break;
                }
            }
            if (fileDownloaded) break;
        }        
        return fileDownloaded;
    }

    public String generateCSVUploadManualCheck(String requestId, String orderId, String orderDetailId) {
        String downloadDir = System.getProperty("user.home")+File.separator+"Downloads"+File.separator;
        String fileName = downloadDir+"manualcheck.csv";
        File file = new File(fileName);
        try (FileWriter fileWriter = new FileWriter(file);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(String.join(",", "Refund Request Id", "Order Id", "Order Detail Id"));
            printWriter.println(String.join(",", requestId, orderId, orderDetailId));
            logger.log("Data has been written to -> "+fileName);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write data to file -> "+fileName);
        }
    }

    public void uploadFileManualCheck(String filePath) {
        driverTools.sendKeys(UPLOAD_FILE, filePath);
        logger.log("Uploading this file -> "+filePath);
    }

    public boolean verifyFlashUploadCSV(String data) {
        Optional<WebElement> flashSuccess = driverTools.waitForElementToAppear(getFlashUploadCSV("success"));
        if (flashSuccess.isEmpty()) {
            logger.log("Flash success not shown");
            return false;
        }
        String message = driverTools.getText(flashSuccess.get());
        String expectedMessage = "Upload finished - Success: "+data+", Error: 0";
        if (!message.equals(expectedMessage)) {
            logger.fail("Flash message -> "+message+", is not same with expected -> "+expectedMessage);
            return false;
        }
        return true;
    }

    public boolean verifyDisburseStatus(String requestId, int retry, String status) {
        boolean isRefunded = false;
        boolean requestIdShown = false;
        long startTime = Instant.now().toEpochMilli();
        logger.log("Start time: "+startTime);
        long waitTime = startTime + (retry*1000);
        logger.log("Wait time: "+waitTime);

        do {
            goToRefundReport();
            inputDataOnFilter("Refund Request ID", requestId);
            clickButton("Search");
            requestIdShown = verifyRowValueOfColumn("Refund Request ID", requestId);
            if (!requestIdShown) {
                logger.fail("Refund request id not shown after filtering");
                break;
            }
            isRefunded = verifyRowValueOfColumn("Refund Status", status);
            if (isRefunded) break;
        } while (Instant.now().toEpochMilli() < waitTime);
        if (!isRefunded) logger.fail("Refund status not "+status+" yet");
        return isRefunded;
    }

    public boolean verifyMultiRefundMethods(String[] methods) {
        String refundMethod = methods[0];
        if (methods.length > 1) {
            for (int i = 1; i < methods.length; i++) {
                refundMethod = refundMethod+", "+methods[i];
            }
        }
        return verifyRowValueOfColumn("Refund Method", refundMethod);
    }
}
