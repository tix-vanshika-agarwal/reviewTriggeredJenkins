package com.tiket.web.dashboard.page.platform.liberty.message.email;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;

public class SendBulkEmailPageWeb<T extends WebDriver> extends WebPage<T> {
    public SendBulkEmailPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> SendBulkEmailPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new SendBulkEmailPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new SendBulkEmailPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator CATEGORY_INPUT = new Locator("CATEGORY_INPUT", By.xpath("//input[@id='sb-text-input-Select Category']"));
    private static final Locator CATEGORY_INPUT_SELECTIONS = new Locator("CATEGORY_INPUT_SELECTIONS", By.xpath("//div[./div[@data-testid='autocomplete_option_label']]"));
    private static final Locator TEMPLATE_INPUT = new Locator("TEMPLATE_INPUT", By.xpath("//input[@id='sb-text-input-Select Template']"));
    private static final Locator TEMPLATE_INPUT_SELECTIONS = new Locator("TEMPLATE_INPUT_SELECTIONS", By.xpath("//div[./div[@data-testid='autocomplete_option_label']]"));
    private static final Locator LANGUAGE_INPUT = new Locator("LANGUAGE_INPUT", By.xpath("//input[@id='sb-text-input-Select Language']"));
    private static final Locator LANGUAGE_INPUT_SELECTIONS = new Locator("LANGUAGE_INPUT_SELECTIONS", By.xpath("//div[@data-testid='autocomplete_option_label']"));
    private static final Locator DOWNLOAD_TSV_BUTTON = new Locator("DOWNLOAD_TSV_BUTTON", By.xpath("//button[contains(@class, 'btn_download_tsv')]"));
    private static final Locator SUBMIT_TSV_INPUT = new Locator("SUBMIT_TSV_INPUT", By.xpath("//input[@accept='.tsv']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[contains(@class,'submit_btn')][.//span[normalize-space()='Submit']]"));
    private static final Locator SUBMIT_SNACKBAR = new Locator("SUBMIT_SNACKBAR", By.xpath("//div[contains(@class,'snackbars_positioner')][normalize-space()!='']"));

    private File tsvFile;

    private void select(String type, String input, Locator inputLoc, Locator selectionLoc) {
        driverTools.click(inputLoc);
        driverTools.sendKeys(inputLoc, input);
        List<WebElement> selections = driverTools.waitForElementsToAppear(selectionLoc).orElse(null);
        if (selections == null || selections.isEmpty())
            throw new NoSuchElementException("There are no selections for " + type);
        for (WebElement selection : selections) {
            if (driverTools.getText(selection).equalsIgnoreCase(input)) {
                driverTools.scrollIntoWebView(selection);
                driverTools.click(selection);
                break;
            }
        }
    }

    public void goToPage() {
        driverTools.dummyWait(3);
        switch (Configs.ENVIRONMENT) {
            case GK -> {driver.get("https://gatotkaca.tiket.com/admin/message/email/send-bulk");}
            case PREPROD -> {driver.get("https://preprod.tiket.com/admin/message/email/send-bulk");}
            case PROD -> {driver.get("https://www.tiket.com/admin/message/email/send-bulk");}
        }
        driverTools.waitForWebToLoad();
    }

    public void selectCategory(String category) {
        select("Category", category, CATEGORY_INPUT, CATEGORY_INPUT_SELECTIONS);
    }

    public void selectTemplate(String template) {
        select("Template", template, TEMPLATE_INPUT, TEMPLATE_INPUT_SELECTIONS);
    }

    public void selectLanguage(String lang) {
        select("Language", lang, LANGUAGE_INPUT, LANGUAGE_INPUT_SELECTIONS);
    }

    public void downloadTSV() {
        driverTools.click(DOWNLOAD_TSV_BUTTON);
        driverTools.dummyWait(2);
    }

    public void writeTSVWithEmails(String[] emails) {
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        tsvFile = CoreFunctions.getLastModified(downloadDir);
        if (tsvFile == null) {
            throw new NoSuchElementException("No TSV file found in Downloads: " + downloadDir);
        }
        String filePath = tsvFile.getAbsolutePath();

        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write("\n");

            for (String email : emails) {
                writer.write(email + "\n");
                logger.log("Wrote " + email + " to TSV file");
            }

        } catch (IOException io) {
            logger.warn("An error occurred while writing to the file");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.log(line);
            }
        } catch (IOException io) {
            logger.warn("An error occurred while reading the file");
        }

        File cleanFile = new File(downloadDir, "bulk_email_upload.tsv");
        try {
            Files.copy(tsvFile.toPath(), cleanFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException io) {
            throw new RuntimeException("Failed to copy TSV for upload from " + tsvFile.getAbsolutePath() + " to " + cleanFile.getAbsolutePath(), io);
        }
        tsvFile = cleanFile;
        logger.log("Prepared TSV for upload: " + tsvFile.getAbsolutePath());
    }

    public void uploadTSVToFileInput() {
        driverTools.sendKeys(SUBMIT_TSV_INPUT, tsvFile.getAbsolutePath());
        driverTools.dummyWait(3);
    }

    public void clickSubmitButton() {
        driverTools.scrollElementToCenter(SUBMIT_BUTTON);
        driverTools.click(SUBMIT_BUTTON);
        if (driverTools.waitForElementToAppear(SUBMIT_SNACKBAR, 20).isEmpty()) {
            throw new NoSuchElementException("Submit snackbar did not appear");
        }
        logger.log("Submit snackbar: " + driverTools.getText(SUBMIT_SNACKBAR).orElse(""));
    }

    public boolean verifyTsvHasBeenDownloaded(String template) {
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        File file = CoreFunctions.getLastModified(downloadDir);
        if (file == null) {
            return false;
        }

        if (file.getName().contains(template) && file.getName().contains(".tsv")) return true;
        return false;
    }
}
