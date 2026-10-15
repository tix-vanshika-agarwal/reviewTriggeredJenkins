package com.tiket.web.dashboard.page.platform.liberty;

import java.io.File;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;

public class LibertySendBulkEmailPageWeb<T extends WebDriver> extends WebPage<T> {
    public LibertySendBulkEmailPageWeb(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> LibertySendBulkEmailPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new LibertySendBulkEmailPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new LibertySendBulkEmailPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator BROWSE_FILE_BTN = new Locator("BROWSE_FILE_BTN", By.xpath("//input[@accept='.tsv']"));
    private static final Locator SELECT_CATEGORY = new Locator("SELECT_CATEGORY", By.xpath("//input[@id='sb-text-input-Select Category' and @placeholder='Select Category']"));
    private static final Locator CATEGORY_REMINDER_OPTION = new Locator("CATEGORY_REMINDER_OPTION", By.xpath("//div[@data-testid='autocomplete_option_label' and normalize-space()='Reminder']"));
    private static final Locator SELECT_TEMPLATE = new Locator("SELECT_TEMPLATE", By.xpath("//input[@id='sb-text-input-Select Template' and @placeholder='Select Template']"));
    private static final Locator CATEGORY_VERTICAL_FOR_AUTOMATION_OPTION = new Locator("CATEGORY_VERTICAL_FOR_AUTOMATION_OPTION", By.xpath("//div[@data-testid='autocomplete_option_label' and normalize-space()='vertical_for_automation_category_for_automation']"));
    private static final Locator SELECT_LANGUAGE = new Locator("SELECT_LANGUAGE", By.xpath("//input[@id='sb-text-input-Select Language' and @placeholder='Select Language']"));
    private static final Locator LANGUAGE_EN_OPTION = new Locator("LANGUAGE_EN_OPTION", By.xpath("//div[@data-testid='autocomplete_option_label' and normalize-space()='EN']"));
    private static final Locator DOWNLOAD_TSV_BUTTON = new Locator("DOWNLOAD_TSV_BUTTON", By.xpath("//button[contains(@class,'btn_download_tsv')]"));
    private static final Locator SCHEDULE_TOGGLE = new Locator("SCHEDULE_TOGGLE", By.xpath("//input[@data-testid='toogle-schedule']/following-sibling::div"));
    private static final Locator SELECT_DATE_SCHEDULE_LABEL = new Locator("SELECT_DATE_SCHEDULE_LABEL", By.xpath("//label[normalize-space()='Select Date Schedule']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[contains(@class,'submit_btn')][.//span[normalize-space()='Submit']]"));
    private static final Locator EMAIL_STATUS_FIRST_ROW = new Locator("EMAIL_STATUS_FIRST_ROW", By.xpath("//tbody[contains(@class,'table_msg_body')]//tr[1]"));

    public void goToSendBulkEmailPage() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/message/email/send-bulk");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/message/email/send-bulk");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://tiket.com/admin/message/email/send-bulk");
        }
    }

    public void clickSelectCategoryOption() {
        driverTools.waitForElementToAppear(SELECT_CATEGORY, 5);
        driverTools.click(SELECT_CATEGORY);
        driverTools.waitForElementToAppear(CATEGORY_REMINDER_OPTION, 5);
        driverTools.scrollIntoWebView(CATEGORY_REMINDER_OPTION);
        driverTools.clickWithJavascriptExecutor(CATEGORY_REMINDER_OPTION);
    }

    public void clickSelectTemplateOption() {
        driverTools.waitForElementToAppear(SELECT_TEMPLATE, 5);
        driverTools.click(SELECT_TEMPLATE);
        driverTools.waitForElementToAppear(CATEGORY_VERTICAL_FOR_AUTOMATION_OPTION, 5);
        driverTools.scrollIntoWebView(CATEGORY_VERTICAL_FOR_AUTOMATION_OPTION);
        driverTools.clickWithJavascriptExecutor(CATEGORY_VERTICAL_FOR_AUTOMATION_OPTION);
    }

    public void clickSelectLanguageOption() {
        driverTools.waitForElementToAppear(SELECT_LANGUAGE, 5);
        driverTools.click(SELECT_LANGUAGE);
        driverTools.waitForElementToAppear(LANGUAGE_EN_OPTION, 5);
        driverTools.scrollIntoWebView(LANGUAGE_EN_OPTION);
        driverTools.clickWithJavascriptExecutor(LANGUAGE_EN_OPTION);
    }

    public boolean clickDownloadTsvButton(){
        driverTools.waitForElementToAppear(DOWNLOAD_TSV_BUTTON, 5);
        return driverTools.isElementClickable(DOWNLOAD_TSV_BUTTON, 5);
    }

    public void pushData(String filePath) {
        File data = new File(filePath);
        if (!data.exists()) {
            data = new File(System.getProperty("user.dir"), "src/test/resources/dashboard/liberty/" + data.getName());
        }
        if (!data.exists()) {
            throw new java.util.NoSuchElementException("TSV file not found: " + data.getAbsolutePath());
        }
        logger.log("Uploading TSV from: " + data.getAbsolutePath());
        driverTools.sendKeys(BROWSE_FILE_BTN, data.getAbsolutePath());
    }

    public void clickScheduleToggle() {
        driverTools.waitForElementToAppear(SCHEDULE_TOGGLE, 5);
        driverTools.scrollIntoWebView(SCHEDULE_TOGGLE);
        driverTools.clickWithJavascriptExecutor(SCHEDULE_TOGGLE);
    }

    public boolean isSelectDateScheduleDisplayed(){
        driverTools.waitForElementToAppear(SELECT_DATE_SCHEDULE_LABEL, 5);
        return driverTools.isElementDisplayed(SELECT_DATE_SCHEDULE_LABEL);
    }

    public void clickSubmitButton(){
        driverTools.waitForElementToAppear(SUBMIT_BUTTON);
        driverTools.scrollIntoWebView(SUBMIT_BUTTON);
        driverTools.clickWithJavascriptExecutor(SUBMIT_BUTTON);
    }

    public boolean checkEmailStatusIsInApprovalState(String templateName, String statusText){
        driverTools.waitForElementToAppear(EMAIL_STATUS_FIRST_ROW, 5);
        driverTools.scrollToElement(EMAIL_STATUS_FIRST_ROW);
        String rowText = driver.findElement(EMAIL_STATUS_FIRST_ROW.by()).getText().trim();
        logger.log("First Row Text: " + rowText);
        logger.log("Template Name: " + templateName);
        logger.log("Status Text: " + statusText);
        return rowText.contains(templateName) && rowText.contains(statusText);
    }
}