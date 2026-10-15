package com.tiket.web.dashboard.page.platform.eiffel.unm;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Assertion;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;

public class UnmBulkLogoutPage<T extends WebDriver> extends BasePage<T> {

    private static final int DEFAULT_TIMEOUT = 10;
    private static final int MAX_RETRIES = 3;
    private static final int MODAL_TIMEOUT = 15;
    private static final int WAIT_AFTER_CLICK = 1;
    private static final int WAIT_AFTER_UPLOAD = 2;
    private static final int WAIT_AFTER_SUBMIT = 2;
    private static final int WAIT_AFTER_CONTINUE = 3;

    private String FILE_NAME;

    private static final Locator BULK_ACTION_MENU = new Locator("BULK_ACTION_MENU", By.xpath("//p[contains(normalize-space(),'Bulk Action')]"));
    private static final Locator FORCE_LOGOUT_SUBMENU = new Locator("FORCE_LOGOUT_SUBMENU", By.xpath("//p[normalize-space()='Force Logout']"));
    private static final Locator FORCE_LOGOUT_BUTTON = new Locator("FORCE_LOGOUT_BUTTON", By.xpath("//button[@data-testid='force-logout-button']"));
    private static final Locator TSV_FILE_INPUT = new Locator("TSV_FILE_INPUT", By.xpath("//input[@accept='.tsv']"));
    private static final Locator SUBMIT_BUTTON_IN_MODAL = new Locator("SUBMIT_BUTTON_IN_MODAL", By.xpath("//div[contains(@class,'modal')]//button[contains(text(),'Bulk Force Logout')]"));
    private static final Locator PASSWORD_INPUT = new Locator("PASSWORD_INPUT", By.xpath("//input[@type='password']"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", By.xpath("//button[contains(text(),'Continue')]"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//*[contains(@class, 'snackbar') and contains(@class, 'type_success')]"));
    private static final Locator HISTORY_LIST_TAB = new Locator("HISTORY_LIST_TAB", By.xpath("//button[contains(.,'History List')]"));

    public UnmBulkLogoutPage(Session<T> session) {
        super(session);
    }

    public void goToForceLogoutMenu() {
        logger.log("Navigating to Bulk Action > Force Logout menu");
        WebElement bulkActionMenu = driverTools.waitForElementToAppear(BULK_ACTION_MENU, DEFAULT_TIMEOUT).orElseThrow(() -> new RuntimeException("Bulk Action menu not found"));
        driverTools.click(bulkActionMenu);
        driverTools.dummyWait(WAIT_AFTER_CLICK);

        WebElement forceLogoutSubmenu = driverTools.waitForElementToAppear(FORCE_LOGOUT_SUBMENU, DEFAULT_TIMEOUT).orElseThrow(() -> new RuntimeException("Force Logout submenu not found"));
        driverTools.click(forceLogoutSubmenu);
        driverTools.waitForWebToLoad();
        logger.log("Navigated to Force Logout page");
    }

    public void clickForceLogoutButton() {
        logger.log("Clicking Force Logout button to open upload modal");
        WebElement forceLogoutBtn = driverTools.waitForElementToAppear(FORCE_LOGOUT_BUTTON, DEFAULT_TIMEOUT).orElseThrow(() -> new RuntimeException("Force Logout button not found"));
        driverTools.scrollElementToCenter(forceLogoutBtn);
        driverTools.click(forceLogoutBtn);
        driverTools.dummyWait(WAIT_AFTER_UPLOAD);
    }

    public void uploadTsvFile(String filePath) {
        logger.log("Uploading TSV file: " + filePath);
        WebElement fileInput = driverTools.waitForElementToPresence(TSV_FILE_INPUT, DEFAULT_TIMEOUT).orElseThrow(() -> new RuntimeException("TSV file input not found"));
        driverTools.sendKeys(fileInput, filePath);
        logger.log("TSV file selected, waiting for upload to process");
        driverTools.dummyWait(WAIT_AFTER_UPLOAD);
    }

    public void clickSubmitButtonInModal() {
        logger.log("Clicking submit button in modal");
        WebElement submitBtn = driverTools.waitForElementToAppear(SUBMIT_BUTTON_IN_MODAL, DEFAULT_TIMEOUT).orElseThrow(() -> new RuntimeException("Submit button in modal not found"));
        driverTools.scrollElementToCenter(submitBtn);
        driverTools.click(submitBtn);
        logger.log("Submit button clicked, waiting for password modal");
        driverTools.dummyWait(WAIT_AFTER_SUBMIT);
    }

    public void enterPassword(String password) {
        logger.log("Entering password for confirmation");
        WebElement passwordInput = driverTools.waitForElementToAppear(PASSWORD_INPUT, DEFAULT_TIMEOUT).orElseThrow(() -> new RuntimeException("Password input not found"));
        driverTools.sendKeys(passwordInput, password);
    }

    public void clickContinueButton() {
        logger.log("Clicking continue button");
        WebElement continueBtn = driverTools.waitForElementToAppear(CONTINUE_BUTTON, DEFAULT_TIMEOUT).orElseThrow(() -> new RuntimeException("Continue button not found"));
        driverTools.scrollElementToCenter(continueBtn);
        driverTools.click(continueBtn);
        logger.log("Continue button clicked, waiting for execution");
        driverTools.dummyWait(WAIT_AFTER_CONTINUE);
    }

    public void verifyBulkLogoutSuccess(String expectedMessage) {
        logger.log("Verifying bulk logout execution success");
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, MODAL_TIMEOUT).ifPresentOrElse(e -> {
            String message = driverTools.getText(e);
            Assertion.assertThat("Verify success message", message, containsStringIgnoringCase(expectedMessage));
        }, () -> {
            Assertion.fail("Success message not found");
        });
    }

    public String generateTsvFile(String[] accountIds) {
        String dirPath = getResourcesPath();
        File directory = new File(dirPath);

        if (!directory.exists()) {
            directory.mkdirs();
            logger.log("Created directory: " + dirPath);
        }

        FILE_NAME = "automation_" + CoreFunctions.getCurrentTime() + ".tsv";
        String filePath = dirPath + File.separator + FILE_NAME;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("account_id");
            writer.newLine();

            for (int i = 0; i < accountIds.length; i++) {
                String trimmedId = accountIds[i].trim();
                if (!trimmedId.isEmpty()) {
                    writer.write(trimmedId);
                    if (i < accountIds.length - 1) {
                        writer.newLine();
                    }
                }
            }

            logger.log("TSV file generated successfully at: " + FILE_NAME);
        } catch (IOException e) {
            Assertion.fail("Failed to generate TSV file: " + e.getMessage());
        }
        return filePath;
    }

    public void goToHistoryListTab() {
        logger.log("Clicking on History List tab");
        WebElement historyTab = driverTools.waitForElementToAppear(HISTORY_LIST_TAB, DEFAULT_TIMEOUT).orElseThrow(() -> new RuntimeException("History List tab not found"));
        driverTools.click(historyTab);
        driverTools.dummyWait(WAIT_AFTER_UPLOAD);
    }

    public boolean verifyFileInHistoryList() {
        logger.log("Verifying file in History List: " + FILE_NAME);

        for (int i = 1; i <= MAX_RETRIES; i++) {
            Locator fileRowLocator = new Locator("FILE_ROW_COMPLETED", By.xpath("//table//tr[contains(., '" + FILE_NAME + "') and contains(., 'Completed')]"));

            Optional<WebElement> completedRow = driverTools.waitForElementToAppear(fileRowLocator, DEFAULT_TIMEOUT);

            if (completedRow.isPresent()) {
                String rowText = driverTools.getText(completedRow.get());
                logger.log("Found completed file row: " + rowText);
                return true;
            }

            if (i < MAX_RETRIES) {
                logger.log("File " + FILE_NAME + " not yet 'Completed' or not found. Retrying " + i + "/" + MAX_RETRIES + "...");
                driverTools.refreshWebpage();
                goToHistoryListTab();
            }
        }

        logger.warn("File " + FILE_NAME + " did not reach 'Completed' status after " + MAX_RETRIES + " attempts");
        return false;
    }

    public void deleteTsvFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                logger.log("TSV file deleted successfully: " + filePath);
            } else {
                logger.warn("Failed to delete TSV file: " + filePath);
            }
        } else {
            logger.warn("TSV file does not exist: " + filePath);
        }
    }

    private String getResourcesPath() {
        String basePath = System.getProperty("user.dir");
        return basePath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "dashboard" + File.separator + "eiffel";
    }
}
