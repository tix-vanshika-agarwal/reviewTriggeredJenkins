package com.tiket.web.dashboard.page.platform.eiffel.unm;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.web.WebPage;

import org.hamcrest.CoreMatchers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.hamcrest.core.Is.is;

public class UnmEmailBlacklistPage<T extends WebDriver> extends WebPage<T> {

    private static final int SHORT_TIMEOUT = 5;
    private static final int MEDIUM_TIMEOUT = 10;
    private static final int LONG_TIMEOUT = 15;
    private static final String TEST_RESOURCES_DIR = "/src/test/resources/dashboard/eiffel/";

    private static final Locator EMAIL_MENU = new Locator("EMAIL_MENU", By.xpath("//p[normalize-space()='Email']"));
    private static final Locator EMAIL_BLACKLIST_MENU = new Locator("EMAIL_BLACKLIST_MENU", By.xpath("//p[normalize-space()='Email Blacklist']"));
    private static final Locator ADD_EMAIL_BLACKLIST_BUTTON = new Locator("ADD_EMAIL_BLACKLIST_BUTTON", By.xpath("//button[normalize-space()='Add Email Blacklist']"));
    private static final Locator SMTP_DROPDOWN = new Locator("SMTP_DROPDOWN", By.xpath("//input[contains(@placeholder, 'Choose SMTP')]"));
    private static final Locator MAILGUN_OPTION = new Locator("MAILGUN_OPTION", By.xpath("//p[normalize-space()='mailgun'] | //div[normalize-space()='mailgun']"));
    private static final Locator SENDGRID_OPTION = new Locator("SENDGRID_OPTION", By.xpath("//p[normalize-space()='sendgrid'] | //div[normalize-space()='sendgrid']"));
    private static final Locator UPLOAD_FILE_INPUT = new Locator("UPLOAD_FILE_INPUT", By.xpath("//input[@type='file']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[normalize-space()='Submit']"));
    private static final Locator SEARCH_INPUT = new Locator("SEARCH_INPUT", By.xpath("//input[contains(@placeholder, 'Search email address')]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[normalize-space()='Search']"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//div[contains(@class, 'snackbar') and contains(@class, 'type_general')]//div[contains(@class, 'text_content')]"));
    private static final Locator SMTP_CARD_HEADER = new Locator("SMTP_CARD_HEADER", By.xpath("//div[@data-testid='email-blacklist-smtp-card']/div[contains(@class, 'styles_title')]"));

    public UnmEmailBlacklistPage(Session<T> session) {
        super(session);
    }

    public void goToEmailBlacklistMenu() {
        driverTools.click(EMAIL_MENU);
        driverTools.click(EMAIL_BLACKLIST_MENU);
        driverTools.waitForElementToAppear(ADD_EMAIL_BLACKLIST_BUTTON, MEDIUM_TIMEOUT);
    }

    public void clickAddEmailBlacklist() {
        driverTools.click(ADD_EMAIL_BLACKLIST_BUTTON);
        driverTools.waitForElementToAppear(SMTP_DROPDOWN, MEDIUM_TIMEOUT);
    }

    public void selectSmtp(String smtp) {
        driverTools.waitForElementToAppear(SMTP_DROPDOWN, SHORT_TIMEOUT);
        driverTools.click(SMTP_DROPDOWN);

        Locator option = smtp.equalsIgnoreCase("mailgun") ? MAILGUN_OPTION : SENDGRID_OPTION;
        driverTools.waitForElementToAppear(option, SHORT_TIMEOUT);
        driverTools.click(option);
    }

    public void uploadBlacklistFile(String filePath) {
        driverTools.sendKeys(UPLOAD_FILE_INPUT, filePath);
    }

    public void clickSubmit() {
        driverTools.click(SUBMIT_BUTTON);
    }

    public void verifySuccessSnackbar(String expectedText) {
        Optional<WebElement> snackbar = driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, LONG_TIMEOUT);
        Assertion.assertThat("Success snackbar is visible", snackbar.isPresent(), is(true));

        String actualText = snackbar.map(WebElement::getText).orElse("");
        Assertion.assertThat("Success snackbar text matches", actualText, is(expectedText));
    }

    public void searchEmail(String email) {
        driverTools.sendKeys(SEARCH_INPUT, email);
        driverTools.click(SEARCH_BUTTON);
    }

    public void verifyEmailInList(String email, String reason, String blacklistDate, String whitelistDate, String smtp) {
        Locator emailRow = createEmailRowLocator(email);
        Assertion.assertThat("Email is present in the list", driverTools.waitForElementToAppear(emailRow, SHORT_TIMEOUT).isPresent(), is(true));

        verifySmtpProvider(smtp);
        verifyTableColumn(email, 1, "Reason", reason, false);
        verifyTableColumn(email, 2, "Blacklist date", blacklistDate, true);
        verifyTableColumn(email, 3, "Whitelist date", whitelistDate, true);
    }

    public void verifySmtpProvider(String expectedSmtp) {
        if (expectedSmtp == null || expectedSmtp.isBlank()) {
            Assertion.fail("Expected SMTP provider is required");
        }

        String actualSmtp = driverTools.getText(SMTP_CARD_HEADER).orElse("");
        Assertion.assertThat("SMTP provider matches", actualSmtp.equalsIgnoreCase(expectedSmtp), is(true));
    }

    private Locator createEmailRowLocator(String email) {
        return new Locator("EMAIL_ROW", By.xpath("//td[normalize-space()='" + email + "']"));
    }

    private void verifyTableColumn(String email, int columnIndex, String columnName, String expectedValue, boolean partialMatch) {
        Locator columnLocator = new Locator(columnName.toUpperCase().replace(" ", "_") + "_COL", By.xpath("//td[normalize-space()='" + email + "']/following-sibling::td[" + columnIndex + "]"));
        String actualValue = driverTools.getText(columnLocator).orElse("");

        if (partialMatch) {
            Assertion.assertThat(columnName + " contains expected value", actualValue, CoreMatchers.containsStringIgnoringCase(expectedValue));
        } else {
            Assertion.assertThat(columnName + " matches", actualValue, is(expectedValue));
        }
    }

    public String createTsvFile(String email, String reason, String ttlSeconds) throws IOException {
        String fileName = "blacklist_" + System.currentTimeMillis() + ".tsv";
        Path dirPath = Paths.get(System.getProperty("user.dir") + TEST_RESOURCES_DIR);
        Path filePath = dirPath.resolve(fileName);

        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        String content = String.format("Email Address\tReason\tTTL (time to live)%n%s\t%s\t%s%n", email, reason, ttlSeconds);
        Files.writeString(filePath, content);

        return filePath.toString();
    }

    public void deleteFile(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return;
        }

        try {
            Path path = Paths.get(filePath);
            boolean deleted = Files.deleteIfExists(path);
            if (deleted) {
                logger.log("Deleted test file: " + filePath);
            }
        } catch (IOException e) {
            logger.log("Warning: Failed to delete test file: " + filePath + " - " + e.getMessage());
        }
    }
}
