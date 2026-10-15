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

public class LibertyInboxAndPNPageWeb<T extends WebDriver> extends WebPage<T> {

    public static <T extends WebDriver> LibertyInboxAndPNPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new LibertyInboxAndPNPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new LibertyInboxAndPNPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    public LibertyInboxAndPNPageWeb(Session<T> session) {
        super(session);
    }

    private static final Locator SELECT_TEMPLATE_INPUT = new Locator("SELECT_TEMPLATE_INPUT", By.xpath("//input[@id='sb-text-input-Select template']"));
    private static final Locator SELECT_TEMPLATE_FIRST_OPTION = new Locator("SELECT_TEMPLATE_FIRST_OPTION", By.xpath("(//div[@data-testid='autocomplete_option_label'])[1]"));
    private static final Locator BROWSE_FILE_BTN = new Locator("BROWSE_FILE_BTN", By.xpath("//input[@accept='.tsv']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.cssSelector("[data-testid='submit-button']"));
    private static final Locator INBOX_STATUS_FIRST_ROW = new Locator("INBOX_STATUS_FIRST_ROW", By.cssSelector("tr[data-testid='row-table']"));

    public void goToSendBulkInboxAndPN() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/message/inboxpn/send-bulk");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/message/inboxpn/send-bulk");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://tiket.com/admin/message/inboxpn/send-bulk");
        }
    }

    public void goToInboxAndPNStatus() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/message/inboxpn/status");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/message/inboxpn/status");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://tiket.com/admin/message/inboxpn/status");
        }
    }

    public void clickOnSelectTemplateInput(String selectTemplate) {
        driverTools.waitForElementToAppear(SELECT_TEMPLATE_INPUT, 5);
        driverTools.dummyWait(1);
        driverTools.sendKeys(SELECT_TEMPLATE_INPUT, selectTemplate);
        driverTools.waitForElementToAppear(SELECT_TEMPLATE_FIRST_OPTION, 5);
        driverTools.dummyWait(1);
        driverTools.click(SELECT_TEMPLATE_FIRST_OPTION);
        driverTools.dummyWait(5);
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

    public void clickSubmitButton(){
        driverTools.waitForElementToAppear(SUBMIT_BUTTON);
        driverTools.scrollIntoWebView(SUBMIT_BUTTON);
        driverTools.clickWithJavascriptExecutor(SUBMIT_BUTTON);
    }

    public boolean checkEmailStatusIsInApprovalState(String templateName) {
        driverTools.dummyWait(10);
        driverTools.waitForElementToAppear(INBOX_STATUS_FIRST_ROW, 10);
        driverTools.scrollToElement(INBOX_STATUS_FIRST_ROW);
        String rowText = driver.findElement(INBOX_STATUS_FIRST_ROW.by()).getText().trim();
        logger.log("First Row Text: " + rowText);
        logger.log("Template Name: " + templateName);
        return rowText.contains(templateName);
    }
}