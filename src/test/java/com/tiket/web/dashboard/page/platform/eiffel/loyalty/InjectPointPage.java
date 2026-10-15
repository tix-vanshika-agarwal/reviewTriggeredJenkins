package com.tiket.web.dashboard.page.platform.eiffel.loyalty;

import com.tiket.core.CoreFunctions;
import com.tiket.dbclient.TiketMysqlDBManager;
import com.tiket.dto.LoyaltyPointsDTO;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import lombok.Getter;
import nl.jiankai.mapper.ResultSetMapperFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import static com.tiket.core.CoreFunctions.log;

public class InjectPointPage<T extends WebDriver> extends BasePage<T> {

    private static final Locator INJECT_POINT = new Locator("INJECT_POINT", new By.ByXPath("//span[text()='Inject Point']"));
    private static final Locator ACCOUNT_ID = new Locator("ACCOUNT_ID", new By.ByXPath("//input[@placeholder='Account Id']"));
    private static final Locator CATEGORY = new Locator("CATEGORY", new By.ByXPath("//input[@id='category']"));
    private static final Locator REWARD = new Locator("REWARD", new By.ByXPath("(//p[text()='REWARD'])[last()]"));
    private static final Locator REFUND = new Locator("REFUND", new By.ByXPath("//p[text()='REFUND']"));
    private static final Locator REFERRAL = new Locator("REFERRAL", new By.ByXPath("//p[text()='REFERRAL']"));
    private static final Locator BIRTHDAY = new Locator("BIRTHDAY", new By.ByXPath("//p[text()='BIRTHDAY']"));
    private static final Locator BPG = new Locator("BPG", new By.ByXPath("//p[text()='BPG']"));
    private static final Locator INSTANT_POINT_CASHBACK = new Locator("INSTANT_POINT_CASHBACK", new By.ByXPath("//p[text()='INSTANT_POINT_CASHBACK']"));
    private static final Locator POINT_AMOUNT = new Locator("POINT_AMOUNT", new By.ByXPath("//input[@data-testid='input-point-amount']"));
    private static final Locator SUBMIT_FILE_BUTTON = new Locator("SUBMIT_FILE_BUTTON", By.xpath("//button[@data-testid='submit-file-button']"));
    private static final Locator SUBMIT = new Locator("SUBMIT", By.xpath("//button[normalize-space()='Submit']"));
    private static final Locator OK = new Locator("OK",new By.ByXPath("//button[.='OK']"));
    private static final Locator MESSAGE = new Locator("MESSAGE",new By.ByXPath("//div[text()='Successfully Inject Point to Approval Page']"));
    private static final Locator ORDER_ID = new Locator("ORDER_ID",new By.ByXPath("//input[@data-testid='input-order-id']"));
    private static final Locator REFERENCE_ID = new Locator("REFERENCE_ID",new By.ByXPath("//input[@data-testid='input-reference-id']"));
    private static final Locator BULK_TAB = new Locator("BULK_TAB", By.xpath("//button[contains(@class,'Tabs_tab__cMRMk')][.//span[text()='Bulk']]"));
    private static final Locator PROCESS_TYPE_DROPDOWN = new Locator("PROCESS_TYPE_DROPDOWN", By.xpath("//input[@data-testid='dropdown-select-bulk-process-type' and @id='process-type']"));
    private static final Locator UPLOAD_FILE_BTN = new Locator("BROWSE_FILE_BTN", By.xpath("//input[@type='file']"));
    private static final Locator INJECT_POINT_PARTNER = new Locator("INJECT_POINT_PARTNER", By.xpath("//span[contains(text(),'Inject Point Partner')]"));
    private static final Locator SUCCESS_MESSAGE = new Locator("SUCCESS_MESSAGE", By.xpath("//div[contains(normalize-space(text()),'Successfully')]"));
    private static final Locator APPROVAL_POINT_MENU = new Locator("APPROVAL_POINT_MENU", By.xpath("//span[normalize-space(text())='Approval Point']"));
    private static final Locator HISTORY_LIST_TAB = new Locator("HISTORY_LIST_TAB", By.xpath("//span[normalize-space(text())='History List']"));
    private static final Locator START_DATE_INPUT = new Locator("START_DATE_INPUT", By.xpath("//input[@id='start-date']"));
    private static final Locator END_DATE_INPUT = new Locator("END_DATE_INPUT", By.xpath("//input[@id='end-date']"));
    private static final Locator SEARCH_BUTTON_HISTORY = new Locator("SEARCH_BUTTON_HISTORY", By.xpath("//button[normalize-space(text())='Search']"));
    private static final Locator HISTORY_TABLE = new Locator("HISTORY_TABLE", By.xpath("//table[@class='Table_table__tRkVf Table_with_pagination__tHI04']"));
    private static final Locator HISTORY_TABLE_ROWS = new Locator("HISTORY_TABLE_ROWS", By.xpath("//table[@class='Table_table__tRkVf Table_with_pagination__tHI04']//tbody//tr"));
    private static final Locator HISTORY_SUBMITTED_DATE_COLUMN = new Locator("HISTORY_SUBMITTED_DATE_COLUMN", By.xpath("//table[@class='Table_table__tRkVf Table_with_pagination__tHI04']//tbody//tr[1]//td[1]"));
    private static final Locator HISTORY_APPROVAL_DATE_COLUMN = new Locator("HISTORY_APPROVAL_DATE_COLUMN", By.xpath("//table[@class='Table_table__tRkVf Table_with_pagination__tHI04']//tbody//tr[1]//td[2]"));
    private static final Locator HISTORY_ACCOUNT_ID_COLUMN = new Locator("HISTORY_ACCOUNT_ID_COLUMN", By.xpath("//table[@class='Table_table__tRkVf Table_with_pagination__tHI04']//tbody//tr[1]//td[3]"));
    private static final Locator HISTORY_STATUS_COLUMN = new Locator("HISTORY_STATUS_COLUMN", By.xpath("//table[@class='Table_table__tRkVf Table_with_pagination__tHI04']//tbody//tr[1]//td[14]"));
    private static final Locator PAGINATION_INFO = new Locator("PAGINATION_INFO", By.xpath("//p[contains(text(),'Showing')]"));

    public InjectPointPage(Session<T> session) {
        super(session);
    }

    public void injectpointsdashboard(){
        WebElement injectpoints = driverTools.waitForElementToAppear(INJECT_POINT).orElseThrow();
        driverTools.click(injectpoints);
    }

    public void addAccountId(String input) {
        driverTools.waitForElementToAppear(ACCOUNT_ID);
        driverTools.sendKeys(ACCOUNT_ID, input);
    }

    public void selectCategory(String category) {
        driverTools.waitForElementToAppear(CATEGORY);
        driverTools.click(CATEGORY);
        selectSubCategory(category);
    }

    public void selectSubCategory(String subCategory) {
        switch (subCategory) {
            case "REWARD":
                driverTools.click(REWARD);
                return;
            case "REFUND":
                driverTools.click(REFUND);
                return;
            case "REFERRAL":
                driverTools.click(REFERRAL);
                return;
            case "BPG":
                driverTools.click(BPG);
                return;
            case "BIRTHDAY":
                driverTools.click(BIRTHDAY);
                return;
            case "INSTANTPOINTCASHBACK":
                driverTools.click(INSTANT_POINT_CASHBACK);
        }
    }

    public void addPoint(String input) {
        driverTools.sendKeys(POINT_AMOUNT, input);
    }

    public void addOrderId(String input) {
        driverTools.sendKeys(ORDER_ID, input);
    }

    public void addReferenceId(String input) {
        driverTools.sendKeys(REFERENCE_ID, input);
    }

    public void submitIcon() {
        WebElement submitIcon = driverTools.waitForElementToAppear(SUBMIT).orElseThrow();
        driverTools.click(submitIcon);
    }

    public void submitFileIcon() {
        WebElement submitFileIcon = driverTools.waitForElementToAppear(SUBMIT_FILE_BUTTON).orElseThrow();
        driverTools.click(submitFileIcon);
    }

    public void clickOnOk() {
        WebElement okIcon = driverTools.waitForElementToAppear(OK).orElseThrow();
        driverTools.click(okIcon);
    }

    public boolean successMessage(String input) {
        WebElement successMessage = driverTools.waitForElementToAppear(MESSAGE,3).orElseThrow();
        return driverTools.getText(successMessage).contains(input);
    }

    public HashMap<String, Object> getDataDB(String accountId) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        try {
            String sqlQuery = "select * from tix_loyalty_core.pending_task pt WHERE account_id  = '" + accountId + "' order by id DESC limit 1;";
            var db = TiketMysqlDBManager.getInstance("loyalty");
            var dbExecute = ResultSetMapperFactory.getResultSetMapperIdentity().map(db.executeQuery(sqlQuery),
                    LoyaltyPointsDTO.class);
            LoyaltyPointsDTO result;
            if (dbExecute.stream().count() < 1) {
                String msg = "Data not found in Database!";
                logger.fail(msg);
                throw new Exception(msg);
            } else {
                result = dbExecute.get(0);
            }
            logger.log("Data found in Database : \n" + result.toString());
            map.put("task_status", result.getTask_status());
            map.put("account_id", result.getAccount_id());
            map.put("task_type", result.getTask_type());
            map.put("process_type", result.getProcess_type());
            map.put("amount",result.getAmount());
        } catch (SQLException e) {
            Assertion.skip("Error while fetching data from database: " + e.getMessage());
        }
        return map;
    }

    public Boolean verifyContains(String actual, String expected) {
        logger.log("This is actual value "+ actual);
        logger.log("This is expected value "+ expected);
        return expected.contains(actual);
    }

    public void clickBulkTab() {
        driverTools.click(BULK_TAB);
    }

    public void clickProcessTypeDropdown() {
        driverTools.waitForElementToAppear(PROCESS_TYPE_DROPDOWN);
        driverTools.click(PROCESS_TYPE_DROPDOWN);
    }

    public void uploadFile(String filePath) {
        driverTools.dummyWait(1);
        driverTools.waitForElementToAppear(UPLOAD_FILE_BTN, 3);
        driverTools.sendKeys(UPLOAD_FILE_BTN, filePath);
    }

    public void goTOInjectPointPartner() {
        driverTools.waitForElementToAppear(INJECT_POINT_PARTNER);
        driverTools.click(INJECT_POINT_PARTNER);
    }

    public boolean isFileUploaded() {
        driverTools.waitForElementToAppear(SUCCESS_MESSAGE);
        boolean isTrue = driverTools.isElementDisplayed(SUCCESS_MESSAGE);
        driverTools.waitForElementToDisappear(SUCCESS_MESSAGE);
        return isTrue;
    }

    public void clickApprovalPointMenu() {
        driverTools.waitForElementToAppear(APPROVAL_POINT_MENU);
        driverTools.click(APPROVAL_POINT_MENU);
    }

    public void clickHistoryListTab() {
        driverTools.waitForElementToAppear(HISTORY_LIST_TAB);
        driverTools.click(HISTORY_LIST_TAB);
    }

    public void selectStartDate(String date) {
        driverTools.waitForElementToAppear(START_DATE_INPUT);
        driverTools.click(START_DATE_INPUT);
        driverTools.clearInput(START_DATE_INPUT);
        driverTools.sendKeys(START_DATE_INPUT, date);
    }

    public void selectEndDate(String date) {
        driverTools.waitForElementToAppear(END_DATE_INPUT);
        driverTools.click(END_DATE_INPUT);
        driverTools.clearInput(END_DATE_INPUT);
        driverTools.sendKeys(END_DATE_INPUT, date);
    }

    public void clickSearchButtonHistory() {
        driverTools.waitForElementToAppear(SEARCH_BUTTON_HISTORY);
        driverTools.click(SEARCH_BUTTON_HISTORY);
    }

    public boolean isHistoryTableDisplayed() {
        driverTools.waitForElementToAppear(HISTORY_TABLE);
        return driverTools.isElementDisplayed(HISTORY_TABLE);
    }

    public String getHistorySubmittedDate() {
        driverTools.waitForElementToAppear(HISTORY_SUBMITTED_DATE_COLUMN);
        return String.valueOf(driverTools.getText(HISTORY_SUBMITTED_DATE_COLUMN));
    }

    public String getHistoryAccountId() {
        driverTools.waitForElementToAppear(HISTORY_ACCOUNT_ID_COLUMN);
        return String.valueOf(driverTools.getText(HISTORY_ACCOUNT_ID_COLUMN));
    }

    public String getPaginationInfo() {
        driverTools.waitForElementToAppear(PAGINATION_INFO);
        return String.valueOf(driverTools.getText(PAGINATION_INFO));
    }

    public String createUniqueTestFile(String templateFileName) {
        try {
            log("Template file requested: " + templateFileName);
            
            if (!verifyTemplateFileExists(templateFileName)) {
                log("WARNING: Template file not found, using fallback");
                return getTemplateFilePath(templateFileName);
            }
            
            String timestamp = String.valueOf(System.currentTimeMillis());
            
            String baseDir = System.getProperty("user.dir");
            log("Base directory: " + baseDir);
            
            String resourcesPath = baseDir + File.separator + "src" + File.separator + "test" + 
                                 File.separator + "resources" + File.separator + "dashboard" + 
                                 File.separator + "eiffel" + File.separator;
            
            String templateFile = resourcesPath + templateFileName;
            
            String fileExtension = templateFileName.substring(templateFileName.lastIndexOf("."));
            String baseName = templateFileName.substring(0, templateFileName.lastIndexOf("."));
            String uniqueFileName = baseName + "_" + timestamp + fileExtension;
            String uniqueFilePath = resourcesPath + uniqueFileName;
            
            copyFile(templateFile, uniqueFilePath);
            
            log("Successfully created unique test file: " + uniqueFilePath);
            return uniqueFilePath;
            
        } catch (Exception e) {
            log("Error creating unique test file: " + e.getMessage());
            e.printStackTrace();
            log("Falling back to template file path");
            return getTemplateFilePath(templateFileName);
        }
    }

    private void copyFile(String sourcePath, String destPath) throws IOException {
        File sourceFile = new File(sourcePath);
        File destFile = new File(destPath);
        
        destFile.getParentFile().mkdirs();
        
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destFile)) {
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        }
    }

    private String getTemplateFilePath(String templateFileName) {
        String baseDir = System.getProperty("user.dir");
        return baseDir + File.separator + "src" + File.separator + "test" + 
               File.separator + "resources" + File.separator + "dashboard" + 
               File.separator + "eiffel" + File.separator + templateFileName;
    }

    public void cleanupTestFile(String filePath) {
        try {
            if (filePath != null && !filePath.isEmpty()) {
                File file = new File(filePath);
                if (file.exists() && (filePath.matches(".*_\\d{13}\\.csv$") || filePath.matches(".*_bulk_\\d{13}\\.csv$"))) {
                    boolean deleted = file.delete();
                    log("Cleanup test file: " + filePath + " - " + (deleted ? "Success" : "Failed"));
                }
            }
        } catch (Exception e) {
            log("Warning: Could not cleanup test file: " + e.getMessage());
        }
    }

    public String createUniqueTestFileWithCustomData(String templateFileName, String[] accountIds) {
        this.accountIds = accountIds;
        try {
            log("Creating unique bulk test file with custom account IDs: " + Arrays.toString(accountIds));
            
            String timestamp = String.valueOf(System.currentTimeMillis());
            
            String baseDir = System.getProperty("user.dir");
            String resourcesPath = baseDir + File.separator + "src" + File.separator + "test" + 
                                 File.separator + "resources" + File.separator + "dashboard" + 
                                 File.separator + "eiffel" + File.separator;
            
            String fileExtension = templateFileName.substring(templateFileName.lastIndexOf("."));
            String baseName = templateFileName.substring(0, templateFileName.lastIndexOf("."));
            String uniqueFileName = baseName + "_bulk_" + timestamp + fileExtension;
            this.fileName = uniqueFileName;
            String uniqueFilePath = resourcesPath + uniqueFileName;

            createDummyData(accountIds);
            createBulkTestCsvFile(uniqueFilePath, accountIds);
            
            log("Successfully created bulk test file (no empty rows): " + uniqueFilePath);
            return uniqueFilePath;
            
        } catch (Exception e) {
            log("Error creating unique bulk test file: " + e.getMessage());
            e.printStackTrace();
            return createUniqueTestFile(templateFileName);
        }
    }

    private void createBulkTestCsvFile(String filePath, String[] accountIds) throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        
        log("Writing bulk test CSV data to: " + filePath);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("accountId,amount,notes");
            for(int i = 0; i < accountIds.length; i++) {
                writer.newLine();
                writer.write(accountIds[i] + "," + this.amounts[i] + "," + this.notes[i]);
            }

            writer.flush();
            log("Bulk test CSV file created successfully with " + accountIds.length + " data rows and no empty rows");
        }
        generateExpectedData();
    }

    public boolean verifyTemplateFileExists(String templateFileName) {
        String templatePath = getTemplateFilePath(templateFileName);
        File templateFile = new File(templatePath);
        boolean exists = templateFile.exists();
        log("Template file verification: " + templatePath + " - " + (exists ? "EXISTS" : "NOT FOUND"));
        return exists;
    }

    private void createDummyData(String[] accountIds) {
        String[] tempAmounts = new String[accountIds.length];
        String[] tempNotes = new String[accountIds.length];
        int MAX_AMMOUNT_LENGTH = 4;
        int MAX_NOTES_LENGTH = 10;

        for (int i = 0; i < accountIds.length; i++) {
            tempAmounts[i] = String.valueOf(CoreFunctions.randomInt(MAX_AMMOUNT_LENGTH));
            tempNotes[i] = CoreFunctions.generateRandomString(MAX_NOTES_LENGTH);
        }

        this.amounts = tempAmounts;
        this.notes = tempNotes;
    }

    private void generateExpectedData() {
        String[][] temp = new String[accountIds.length][3];
        for (int i = 0; i < accountIds.length; i++) {
            temp[i][0] = accountIds[i];
            temp[i][1] = amounts[i];
            temp[i][2] = notes[i];
        }

        this.expectedData = temp;
    }

    @Getter
    private String fileName;
    @Getter
    private String[] accountIds, amounts, notes;
    @Getter
    private String[][] expectedData;
}
