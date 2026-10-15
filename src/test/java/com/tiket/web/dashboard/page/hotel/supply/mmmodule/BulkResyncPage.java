package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BulkResyncPage<T extends WebDriver> extends BasePage<T> {

    public BulkResyncPage(Session<T> session) {
        super(session);
    }

    private static final Locator PAGE_HEADER = new Locator("PAGE_HEADER", By.xpath("//h1[normalize-space(text())='Bulk Resync'] | //span[normalize-space(text())='Bulk Resync']"));
    private static final Locator ADD_TASK_BUTTON = new Locator("ADD_TASK_BUTTON", By.xpath("//button[normalize-space(text())='Add Task']"));
    private static final Locator TASK_NAME_INPUT = new Locator("TASK_NAME_INPUT", By.id("task-title"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[normalize-space(text())='Submit'] | //button[normalize-space(text())='Confirm']"));
    private static final Locator IN_PROGRESS_TAB = new Locator("IN_PROGRESS_TAB", By.xpath("//button[normalize-space(text())='In Progress'] | //span[normalize-space(text())='In Progress']"));
    private static final Locator COMPLETED_TAB = new Locator("COMPLETED_TAB", By.xpath("//button[normalize-space(text())='Completed'] | //span[normalize-space(text())='Completed']"));
    private static final Locator IN_PROGRESS_TASK_ROW = new Locator("IN_PROGRESS_TASK_ROW", By.xpath("//tbody/tr[1]"));
    private static final Locator COMPLETED_TASK_ROW = new Locator("COMPLETED_TASK_ROW", By.xpath("//tbody/tr[1]"));
    private static final Locator SUBMIT_SUCCESS_TOAST = new Locator("SUBMIT_SUCCESS_TOAST", By.xpath("//div[contains(@class,'Snackbar_text_content')]"));
    private static final Locator VENDOR_TASK_NAME_INPUT = new Locator("VENDOR_TASK_NAME_INPUT", By.id("task-name"));


    private static Locator getTypeRadio(String label) {
        return new Locator("TYPE_RADIO_" + label, 
                By.xpath(String.format("//span[contains(@class,'Radio_children') and normalize-space(text())='%s']", label)));
    }

    private static Locator getInputById(String id) {
        return new Locator("INPUT_" + id, By.id(id));
    }

    private static Locator getDataLevelOption(String dataLabel) {
        return new Locator("DATA_LEVEL_OPTION_" + dataLabel,
                By.xpath(String.format("//li[@data-name='option' and @data-label='%s']", dataLabel)));
    }

    private static Locator getFileInput(String accept) {
        return new Locator("FILE_INPUT_" + accept,
                By.cssSelector("input[type='file'][accept='" + accept + "']"));
    }

    public boolean isBulkResyncPageDisplayed() {
        WebElement header = driverTools.waitForElementToPresence(PAGE_HEADER, 15).orElseThrow();
        return driverTools.isElementDisplayed(header);
    }

    public void clickAddTask() {
        WebElement btn = driverTools.waitForElementToBeClickable(ADD_TASK_BUTTON, 10).orElseThrow();
        driverTools.click(btn);
    }

    public void fillTaskName(String taskName) {   // for non-vendor API
        driverTools.waitForElementToBeClickable(TASK_NAME_INPUT, 10).orElseThrow();
        driverTools.sendKeys(TASK_NAME_INPUT, taskName);
    }

    public void fillVendorTaskName(String taskName) {   // for vendor API
        driverTools.waitForElementToBeClickable(VENDOR_TASK_NAME_INPUT, 10).orElseThrow();
        driverTools.sendKeys(VENDOR_TASK_NAME_INPUT, taskName);
    }

    public void selectType(String type) {
        WebElement radio = driverTools.waitForElementToPresence(getTypeRadio(type), 10).orElseThrow();
        driverTools.scrollIntoWebViewCenter(radio);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(radio);
    }

    public void selectSubType(String subType) {
        WebElement dropdown = driverTools.waitForElementToBeClickable(getInputById("select-data-level"), 10).orElseThrow();
        driverTools.scrollIntoWebViewCenter(dropdown);
        // A success snackbar from the previous submit can cover the dropdown; fall back to a JS click if it intercepts.
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(dropdown);
        WebElement option = driverTools.waitForElementToBeClickable(getDataLevelOption(subType), 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(option);
    }

    public void createAndUploadCsvFile(String hotelId) {
        String directoryPath = System.getProperty("user.dir") + File.separator + "src" + File.separator
                + "test" + File.separator + "resources" + File.separator
                + "dashboard" + File.separator + "hotel" + File.separator;
        String fileName = "bulk_resync_sample_" + CoreFunctions.randomInt(5) + ".csv";

        List<String> content = Arrays.asList("hotel_id", hotelId);
        try {
            File csvFile = CoreFunctions.createCSV(directoryPath, fileName, content);
            logger.log("Created bulk resync CSV: " + csvFile.getAbsolutePath());
            WebElement input = driverTools.waitForElementToPresence(getFileInput(".csv"), 10).orElseThrow();
            driverTools.sendKeys(input, csvFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create bulk resync CSV file", e);
        }
    }

    public void clickSubmit() {
        WebElement btn = driverTools.waitForElementToBeClickable(SUBMIT_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(btn);
    }

    public boolean isSubmitSuccessToastDisplayed() {
        WebElement toast = driverTools.waitForElementToAppear(SUBMIT_SUCCESS_TOAST, 15).orElseThrow();
        return driverTools.isElementDisplayed(toast);
    }

    public void clickInProgressTab() {
        driverTools.waitForElementToBeClickable(IN_PROGRESS_TAB, 10).orElseThrow();
        driverTools.click(IN_PROGRESS_TAB);
    }

    public void clickCompletedTab() {
        driverTools.waitForElementToBeClickable(COMPLETED_TAB, 10).orElseThrow();
        driverTools.click(COMPLETED_TAB);
    }

    public boolean isTaskVisibleInInProgressTab() {
        WebElement row = driverTools.waitForElementToAppear(IN_PROGRESS_TASK_ROW, 30).orElseThrow();
        return driverTools.isElementDisplayed(IN_PROGRESS_TASK_ROW);
    }

    public boolean isTaskVisibleInCompletedTab() {
        driverTools.waitForElementToAppear(COMPLETED_TASK_ROW, 120).orElseThrow();
        return driverTools.isElementDisplayed(COMPLETED_TASK_ROW);
    }
}