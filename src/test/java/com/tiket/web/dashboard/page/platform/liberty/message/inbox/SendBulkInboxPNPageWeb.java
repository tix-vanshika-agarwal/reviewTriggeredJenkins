package com.tiket.web.dashboard.page.platform.liberty.message.inbox;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;

public class SendBulkInboxPNPageWeb<T extends WebDriver> extends WebPage<T> {
    public SendBulkInboxPNPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> SendBulkInboxPNPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new SendBulkInboxPNPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new SendBulkInboxPNPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator TEMPLATE_INPUT = new Locator("TEMPLATE_INPUT", By.xpath("//input[@name='sb-text-input-Select template']"));
    private static final Locator TEMPLATE_INPUT_SELECTION = new Locator("TEMPLATE_INPUT_SELECTION", By.xpath("//div[@data-testid='autocomplete_option_label']"));
    private static final Locator TSV_FILES_INPUT = new Locator("TSV_FILES_INPUT", By.xpath("//input[@accept='.tsv']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.cssSelector("[data-testid='submit-button']"));


    public void goToPage() {
        driverTools.dummyWait(3);
        switch (Configs.ENVIRONMENT) {
            case GK -> {driver.get("https://gatotkaca.tiket.com/admin/message/inboxpn/send-bulk");}
            case PREPROD -> {driver.get("https://preprod.tiket.com/admin/message/inboxpn/send-bulk");}
            case PROD -> {driver.get("https://www.tiket.com/admin/message/inboxpn/send-bulk");}
        }
        driverTools.waitForWebToLoad();
    }

    public void selectTemplate(String template) {
        driverTools.click(TEMPLATE_INPUT);
        driverTools.sendKeys(TEMPLATE_INPUT, template);
        List<WebElement> selections = driverTools.waitForElementsToAppear(TEMPLATE_INPUT_SELECTION).orElse(null);
        if (selections == null || selections.isEmpty())
            throw new NoSuchElementException("There are no selections for Template");
        for (WebElement selection : selections) {
            if (driverTools.getText(selection).equalsIgnoreCase(template)) {
                driverTools.scrollIntoWebView(selection);
                driverTools.click(selection);
                break;
            }
        }
    }

    public void uploadFiles(String content) {
        content = content.replace("\\n", "\n");
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        File file = new File(downloadDir, "tsv_for_test.tsv");

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
            logger.log("Wrote " + content);
        } catch (IOException io) {
            logger.warn("IO Exception occurred");
        }

        driverTools.sendKeys(TSV_FILES_INPUT, file.getAbsolutePath());
    }

    public void clickSubmitButton() {
        driverTools.waitForElementToAppear(SUBMIT_BUTTON);
        driverTools.scrollElementToCenter(SUBMIT_BUTTON);
        driverTools.clickWithJavascriptExecutor(SUBMIT_BUTTON);
        driverTools.dummyWait(4);
    }

    public void waitIfNecessary() {
        LocalTime now = LocalTime.now();
        LocalTime nextMinute = now.plusMinutes(1).truncatedTo(ChronoUnit.MINUTES);
        int secondsToWait = (int) now.until(nextMinute, ChronoUnit.SECONDS);
        if (secondsToWait < 10) {
            driverTools.dummyWait(secondsToWait + 2);
        }
    }
}
