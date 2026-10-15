package com.tiket.web.dashboard.page.refund;

import com.tiket.core.CoreFunctions;
import com.tiket.dbclient.TiketMysqlDBManager;
import com.tiket.dto.RefundBulkPreviewDTO;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import nl.jiankai.mapper.ResultSetMapperFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class RefundBulkPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator MENU_REFUND_BULK = new Locator("MENU_REFUND_BULK", new By.ByXPath("//span[@class='menu-title'][text()='Bulk Refund Upload']/parent::a"));
    private static final Locator REUPLOAD_DATA_LABEL = new Locator("REUPLOAD_DATA_LABEL", new By.ByXPath("//label[text()='Re-upload data']"));
    private static final Locator UPLOAD_DATA_LABEL = new Locator("UPLOAD_DATA_LABEL", new By.ByXPath("//label[text()='Upload data and review']"));
    private static final Locator INPUT_FILE = new Locator("INPUT_FILE", new By.ByXPath("//input[@type='file']"));
    private static final Locator TAB_DATA_READY = new Locator("TAB_DATA_READY", new By.ByXPath("//div[text()='Bulk Data Ready to Execute']"));
    private static final Locator TAB_DATA_ERROR = new Locator("TAB_DATA_READY", new By.ByXPath("//div[text()='Bulk Data Error']"));
    private static final Locator FLASH_SUCCESS = new Locator("FLASH_SUCCESS", new By.ByXPath("//div[@class='flash success']"));
    private static final Locator IMAGE_EMPTY = new Locator("IMAGE_EMPTY", new By.ByXPath("//img[@src='/admin/care/refund/assets/empty-bulk-data.png']"));
    private static final Locator IMAGE_READY = new Locator("IMAGE_READY", new By.ByXPath("//div[contains(@class,'selected')]//img[@src='/admin/care/refund/assets/ready-all-data.png']"));
    private static final Locator TITLE_READY = new Locator("TITLE_READY", new By.ByXPath("//div[contains(@class,'selected')]//h3[text()='Ready to execute all data']"));
    private static final Locator SUBTITLE_READY = new Locator("SUBTITLE_READY", new By.ByXPath("//div[contains(@class,'selected')]//p[text()='Good! We found no data error on your file. You may execute them now.']"));
    private static final Locator ALERT_READY_EXECUTE = new Locator("ALERT_READY_EXECUTE", new By.ByXPath("//div[contains(@class,'selected')]//p[@data-testid='success-validation-alert']"));
    private static final Locator ALERT_SUCCESS_EXECUTE = new Locator("ALERT_SUCCESS_EXECUTE", new By.ByXPath("//div[@class='alert alert-success']/p"));
    private static final Locator BUTTON_EXPORT_DATA = new Locator("BUTTON_EXPORT_DATA", new By.ByXPath("//h2[text()='Export Data']/following-sibling::div/button"));
    private static Locator getButtonLocator(String button) {
        return new Locator("BUTTON_"+button.toUpperCase(), new By.ByXPath("//button[contains(text(),'"+button+"')]"));
    }
    private static Locator getLocatorByMessage(String message) {
        return new Locator("LOADER_FULL", new By.ByXPath("//div[contains(@class,'loader')]/p[text()='"+message+"']"));
    }
    private static Locator getColumnValue(String column) {
        return new Locator("COLUMN_"+column.toUpperCase(), new By.ByXPath("//div[contains(@class,'selected')]//td[position()=count(//div[contains(@class,'selected')]//span[text()='"+column+"']/ancestor::th/preceding-sibling::th)+1]"));
    }
    private static Locator getColumnValueSummary(String column) {
        return new Locator("COLUMN_SUMMARY_"+column.toUpperCase(), new By.ByXPath("//table[contains(@class,'summary-table')]//td[position()=count(//table[contains(@class,'summary-table')]//span[text()='"+column+"']/ancestor::th/preceding-sibling::th)+1]"));
    }
    private static Locator getUploadedFile(String fileName) {
        return new Locator("UPLOADED_FILE", new By.ByXPath("//div[@class='upload-section']//p[text()='"+fileName+"']"));
    }

    public RefundBulkPage(Session<T> session) { super(session); }
    
    public void goToRefundBulk() {
        WebElement menuBulk = driverTools.waitForElementToAppear(MENU_REFUND_BULK).orElseThrow();
        driverTools.click(menuBulk);
    }

    public String getRequestIdFromDB(String query) {
        var db = TiketMysqlDBManager.getInstance("refund");
        try {
            List<RefundBulkPreviewDTO> dbExecute = ResultSetMapperFactory.getResultSetMapperIdentity().map(db.executeQuery(query),RefundBulkPreviewDTO.class);
            if (dbExecute.stream().count() < 1) {
                throw new RuntimeException("Data not found in Database!");
            }
            logger.log("Retrieved data -> "+dbExecute.toString());
            return String.valueOf(dbExecute.get(0).getRefund_request_id());
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public void clickButton(String buttonName) {
        WebElement button = driverTools.waitForElementToAppear(getButtonLocator(buttonName)).orElseThrow();
        driverTools.click(button);
    }

    public void clickExportData() {
        WebElement button = driverTools.waitForElementToAppear(BUTTON_EXPORT_DATA).orElseThrow();
        driverTools.click(button);
    }

    public void uploadNewBatch() {
        Optional<WebElement> uploadNew = driverTools.waitForElementToAppear(getButtonLocator("Upload new batch"), 2);
        if (uploadNew.isPresent()) {
            driverTools.click(uploadNew.get());
            clickButton("Yes, upload new batch");
            if (!verifyRefundBulkEmpty(true)) {
                Assertion.fail("Refund Bulk initial page is incorrect");
            }
        }
    }

    public void uploadCSVRefundBulk(String filePath) {
        Optional<WebElement> inputUpload = driverTools.waitForElementToAppear(UPLOAD_DATA_LABEL, 2);
        if (inputUpload.isEmpty()) {
            inputUpload = driverTools.waitForElementToAppear(REUPLOAD_DATA_LABEL, 2);
        }
        driverTools.sendKeys(INPUT_FILE, filePath);
        logger.log("Uploading this file -> "+filePath);
    }

    public void waitForLoader(String message, int timeOutSec) {
        Optional<WebElement> loader = driverTools.waitForElementToAppear(getLocatorByMessage(message), 1);
        if (loader.isPresent()) {
            driverTools.waitForElementToDisappear(loader.get(), timeOutSec);
        }
    }

    public String setExpiryTime(String nextHour) {
        LocalDateTime now = LocalDateTime.now();
        now = now.plusHours(Long.parseLong(nextHour));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String expiry = now.format(formatter);
        logger.log("Refund Expiry -> "+expiry);
        return expiry;
    }

    public String generateCSVRefundBulk(String orderId, String orderDetailId, String amount, String reasonId, String expiry, String remark) {
        String downloadDir = System.getProperty("user.home")+File.separator+"Downloads"+File.separator;
        String random = CoreFunctions.generateRandomString(3);
        String fileName = downloadDir+"refundbulk_"+random+".csv";
        File file = new File(fileName);
        try (FileWriter fileWriter = new FileWriter(file);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(String.join(",","Order ID","Order Detail ID","Refund Amount","Refund Reason","Refund Submission Expiry","Remark"));
            printWriter.println(String.join(",",orderId,orderDetailId,amount,reasonId,expiry,remark));
            logger.log("Data has been written to -> "+fileName);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write data to file -> "+fileName);
        }
    }

    public void deleteCSVFileOnMachine(String filePath) {
        File file = new File(filePath);
        if (!file.delete()) {
            logger.warn("Failed to delete file -> "+file.getName());
        }
    }

    public void validateRefundBulk() {
        clickButton("Validate Data");
        waitForLoader("Validating your file...", 120);
    }

    public void submitRefundBulk() {
        clickButton("Start bulk upload");
        clickButton("Execute now");
        waitForLoader("Executing bulk refund...", 180);
    }

    public boolean verifyRefundBulkEmpty(boolean isInitialPage) {
        Optional<WebElement> image = isInitialPage ? driverTools.waitForElementToAppear(IMAGE_EMPTY, 2) : driverTools.waitForElementToAppear(IMAGE_READY, 2);
        if (image.isEmpty()) {
            String message = isInitialPage ? "empty" : "ready";
            logger.fail("Image data "+message+" not shown");
            return false;
        }
        boolean description = driverTools.getText(getColumnValueSummary("Description")).orElseThrow().equals("No data") ? true : false;
        boolean amount = driverTools.getText(getColumnValueSummary("Amount")).orElseThrow().equals("No data") ? true : false;
        boolean quantity = driverTools.getText(getColumnValueSummary("Quantity")).orElseThrow().equals("0") ? true : false;
        if (!description || !amount || !quantity) {
            logger.log("Description -> "+description);
            logger.log("Amount -> "+amount);
            logger.log("Quantity -> "+quantity);
            return false;
        }
        if (!isInitialPage) {
            Optional<WebElement> title = driverTools.waitForElementToAppear(TITLE_READY);
            Optional<WebElement> subtitle = driverTools.waitForElementToAppear(SUBTITLE_READY);
            if (title.isEmpty() || subtitle.isEmpty()) {
                logger.fail("Title Subtitle not shown");
                return false;
            }
        }
        return true;
    }

    public boolean verifyFlashSuccess(String expectedMessage) {
        Optional<WebElement> flashSuccess = driverTools.waitForElementToAppear(FLASH_SUCCESS, 1);
        if (flashSuccess.isEmpty()) {
            logger.log("Flash success not shown");
            return false;
        }
        String message = driverTools.getText(flashSuccess.get());
        if (!message.equals(expectedMessage)) {
            logger.fail("Flash message -> "+message+", is not same with expected -> "+expectedMessage);
            return false;
        }
        return true;
    }

    public boolean verifyFileUploaded(String filePath) {
        String fileName = Paths.get(filePath).getFileName().toString();
        Optional<WebElement> file = driverTools.waitForElementToAppear(getUploadedFile(fileName));
        if (file.isEmpty()) {
            logger.fail("File not uploaded. filename -> "+fileName);
            return false;
        }
        return true;
    }

    public boolean verifySummaryValidateSucces(String refundMethod, String amount, String currency, String quantity) {
        WebElement description = driverTools.waitForElementToAppear(getColumnValueSummary("Description"), 2).orElseThrow();
        WebElement amountSummary = driverTools.waitForElementToAppear(getColumnValueSummary("Amount"), 2).orElseThrow();
        WebElement quantityElement = driverTools.waitForElementToAppear(getColumnValueSummary("Quantity"), 2).orElseThrow();
        if (!description.getText().contains(refundMethod)) {
            logger.fail("Refund method on summary: "+description.getText()+". Refund method expected: "+refundMethod);
            return false;
        }
        String actualAmount = amountSummary.getText().replace(",", "");
        String expectedAmount = currency+" "+amount;
        if (!actualAmount.equals(expectedAmount)) {
            logger.fail("Actual amount on summary: "+actualAmount+". expected amount: "+expectedAmount);
            return false;
        }
        if (!quantityElement.getText().equals(quantity)) {
            logger.fail("Actual quantity on summary: "+quantityElement.getText()+". expected quantity: "+quantity);
            return false;
        }
        return true;
    }

    public boolean validateTabDataErrorEmpty() {
        WebElement tabDataError = driverTools.waitForElementToAppear(TAB_DATA_ERROR).orElseThrow();
        driverTools.click(tabDataError);
        Optional<WebElement> image = driverTools.waitForElementToAppear(IMAGE_READY, 2);
        if (image.isEmpty()) {
            logger.fail("Image data ready not shown");
            return false;
        }
        Optional<WebElement> title = driverTools.waitForElementToAppear(TITLE_READY);
        Optional<WebElement> subtitle = driverTools.waitForElementToAppear(SUBTITLE_READY);
        if (title.isEmpty() || subtitle.isEmpty()) {
            logger.fail("Title Subtitle not shown");
            return false;
        }
        String successAlert = driverTools.getText(ALERT_READY_EXECUTE).orElseThrow();
        if (!successAlert.equals("Your data is all good. The file is ready to execute")) {
            logger.fail("wrong success alert wording -> "+successAlert);
            return false;
        }
        return true;
    }

    public boolean validateTabDataReady(String orderId, String orderDetailId, String currency, String amount, String reasonId, String expiry, String remark, String method) {
        WebElement tabDataReady = driverTools.waitForElementToAppear(TAB_DATA_READY).orElseThrow();
        driverTools.click(tabDataReady);
        String refundAmount = currency+" "+amount;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(expiry, formatter);
        logger.log("expiry UTC -> "+expiry);
        date = date.plusHours(7); // adjust date to UTC+7, as shown in panel
        expiry = date.format(formatter);
        logger.log("expiry UTC+7 -> "+expiry);
        String[] columns = {"Order ID", "Order Detail ID", "Refund Amount", "Refund Reason", "Refund Submission Expiry Time", "Remark", "Refund Method"};
        String[] values = {orderId, orderDetailId, refundAmount, reasonId, expiry, remark, method};

        WebElement columnElement = driverTools.waitForElementToAppear(getColumnValue("Order ID")).orElseThrow();
        String columnValue = driverTools.getText(columnElement);
        String successAlert = driverTools.getText(ALERT_READY_EXECUTE).orElseThrow();
        if (!successAlert.equals("Your data is all good. The file is ready to execute")) {
            logger.fail("wrong success alert wording -> "+successAlert);
            return false;
        }
        
        for (int i = 0; i < columns.length; i++) {
            columnValue = driverTools.getText(getColumnValue(columns[i])).orElseThrow();
            if (columns[i].equals("Refund Amount")) {
                columnValue = columnValue.replace(",", "");
            }
            if (!columnValue.equals(values[i])) {
                logger.fail("Wrong actual column value: "+columnValue+", expected value: "+values[i]);
                return false;
            }
        }
        return true;
    }

    public boolean verifyDownloadExportCSV(String expiry, String timeOutMiliSec) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate date = LocalDate.parse(expiry, formatter);
        String fileName = "BulkRefundSuccessReport_"+date.toString()+".csv";
        logger.log("expected filename: "+fileName);
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        logger.log("Download folder: "+downloadDir);

        boolean fileDownloaded = false;
        long startTime = Instant.now().toEpochMilli();
        logger.log("Start time: "+startTime);
        long waitTime = startTime + Integer.parseInt(timeOutMiliSec);
        logger.log("Wait time: "+waitTime);

        while (Instant.now().toEpochMilli() < waitTime){
            File[] listFiles = new File(downloadDir).listFiles();
            for (File file : listFiles){
                if (file.getName().equals(fileName)){
                    String[] listFilesName = new File(downloadDir).list(); //logging purpose
                    for (String name : listFilesName) { logger.log("File name on dir: "+name); }
                    fileDownloaded = true;
                    if (!file.delete()) logger.warn("Failed to delete file -> "+file.getName());
                    break;
                }
            }
            if (fileDownloaded) break;
        }
        return fileDownloaded;
    }
    
    public boolean verifySuccessExecuteBulk(String dataCount) {
        String alert = driverTools.getText(driverTools.waitForElementToAppear(ALERT_SUCCESS_EXECUTE).orElseThrow());
        String expectedMessage = "Successfully executed ("+dataCount+") data. Please take a look at the summary below.";
        if (!alert.equals(expectedMessage)) {
            logger.fail("wrong alert message: "+alert+", expected is: "+expectedMessage);
            return false;
        }
        Optional<WebElement> buttonUpload = driverTools.waitForElementToAppear(getButtonLocator("Upload new batch"));
        if (buttonUpload.isEmpty()) {
            logger.fail("Button upload new batch not shown");
            return false;
        }
        WebElement buttonStartBulk = driverTools.waitForElementToAppear(getButtonLocator("Start bulk upload")).orElseThrow();
        Object disabled = driverTools.getValueAttribute(buttonStartBulk, "disabled");
        if (disabled.equals(null)) {
            logger.fail("button start bulk upload is not disabled");
            return false;
        }
        return true;
    }
}
