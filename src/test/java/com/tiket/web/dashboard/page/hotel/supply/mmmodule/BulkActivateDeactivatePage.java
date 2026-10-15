package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;

public class BulkActivateDeactivatePage<T extends WebDriver> extends BasePage<T> {

    public BulkActivateDeactivatePage(Session<T> session) {
        super(session);
    }

    private static final Locator PAGE_HEADER = new Locator("PAGE_HEADER",By.xpath("//span[contains(@class,'Text_size_h3') and normalize-space()='Bulk Active/Deactivate']"));
    private static final Locator ADD_TASK_BUTTON = new Locator("ADD_TASK_BUTTON", By.xpath("//a[@data-testid='button-upload-csv']"));
    private static final Locator LEVEL_DROPDOWN = new Locator("LEVEL_DROPDOWN", By.id("select-the-level"));
    private static final Locator DESCRIPTION_INPUT = new Locator("DESCRIPTION_INPUT", By.id("text-area-description-bulk-active"));
    private static final Locator UPLOAD_CSV_INPUT = new Locator("UPLOAD_CSV_INPUT", By.xpath("//input[@data-testid='dropzone-input']"));
    private static final Locator UPLOAD_CSV_FILE_NAME_LABEL = new Locator("UPLOAD_CSV_FILE_NAME_LABEL", By.xpath("//div[@data-testid='filled-content-upload']//span[contains(@class,'Text_weight_bold')]"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON",By.xpath("//button[normalize-space(text())='Submit']"));

    private static Locator getActionTypeRadio(String actionType) {
        return new Locator("ACTION_TYPE_RADIO_" + actionType,
                By.xpath(String.format("//span[contains(@class,'Radio_children') and normalize-space(text())='%s']", actionType)));
    }

    private static Locator getLevelOption(String level) {
        return new Locator("LEVEL_OPTION_" + level,
                    By.xpath(String.format("//li[@data-name='category' and contains(@data-label,'%s')]", level)));    }

    private static Locator getActivityRow(String description, String level, String actionType) {
        return new Locator("ACTIVITY_ROW_" + description,
                By.xpath(String.format(
                        "//tr[@data-testid='tr-table-body'][td[2][normalize-space()='%s'] and td[3]//span[contains(normalize-space(.),'%s')] and td[4][normalize-space()='%s']]",
                        description, level, actionType)));
    }

    public boolean isActivityRowDisplayed(String description, String level, String actionType) {
        WebElement row = driverTools.waitForElementToAppear(getActivityRow(description, level, actionType), 30).orElseThrow();
        return driverTools.isElementDisplayed(row);
    }

    public boolean isBulkActivateDeactivatePageDisplayed() {
        WebElement header = driverTools.waitForElementToPresence(PAGE_HEADER, 15).orElseThrow();
        return driverTools.isElementDisplayed(header);
    }

    public void clickAddTask() {
        WebElement btn = driverTools.waitForElementToBeClickable(ADD_TASK_BUTTON, 10).orElseThrow();
        driverTools.click(btn);
    }

    public void selectActionType(String actionType) {
        WebElement radio = driverTools.waitForElementToBeClickable(getActionTypeRadio(actionType), 10).orElseThrow();
        driverTools.click(radio);
    }

    public void selectLevel(String level) {
        WebElement dropdown = driverTools.waitForElementToBeClickable(LEVEL_DROPDOWN, 10).orElseThrow();
        driverTools.click(dropdown);
        WebElement option = driverTools.waitForElementToBeClickable(getLevelOption(level), 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(option);
    }

    public void fillDescription(String description) {
        driverTools.waitForElementToBeClickable(DESCRIPTION_INPUT, 10).orElseThrow();
        driverTools.sendKeys(DESCRIPTION_INPUT, description);
    }

    public void uploadCsvFile(File file) {
        WebElement uploadCsvInput = driverTools.waitForElementToPresence(UPLOAD_CSV_INPUT).orElseThrow();
        driverTools.sendKeys(uploadCsvInput, file.getAbsolutePath());
        driverTools.dummyWait(2);
    }

    public boolean isCsvFileUploaded(String fileName) {
        WebElement fileNameLabel = driverTools.waitForElementToPresence(UPLOAD_CSV_FILE_NAME_LABEL).orElseThrow();
        return driverTools.getText(fileNameLabel).contains(fileName);
    }

    public void clickSubmit() {
        WebElement btn = driverTools.waitForElementToBeClickable(SUBMIT_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(btn);
    }

}
