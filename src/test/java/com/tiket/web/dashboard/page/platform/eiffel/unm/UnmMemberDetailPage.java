package com.tiket.web.dashboard.page.platform.eiffel.unm;

import com.tiket.common.RetryUtils;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.web.WebPage;
import com.tiket.testbase.Assertion;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.function.Predicate;

import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class UnmMemberDetailPage<T extends WebDriver> extends WebPage<T> {
    public UnmMemberDetailPage(Session<T> session) {
        super(session);
    }

    private static final Locator ACTION_BUTTON = new Locator("ACTION_BUTTON", By.id("action"));
    private static final Locator SUSPEND_OPTION = new Locator("SUSPEND_OPTION", By.xpath("//p[normalize-space()='Suspend']"));
    private static final Locator UNSUSPEND_OPTION = new Locator("UNSUSPEND_OPTION", By.xpath("//p[normalize-space()='Unsuspend']"));
    private static final Locator UPDATE_NAME_OPTION = new Locator("UPDATE_NAME_OPTION", By.xpath("//p[normalize-space()='Update Name']"));
    private static final Locator UPDATE_DOB_OPTION = new Locator("UPDATE_DOB_OPTION", By.xpath("//p[normalize-space()='Update Date of Birth']"));
    private static final Locator UPDATE_EMAIL_OPTION = new Locator("UPDATE_EMAIL_OPTION", By.xpath("//p[normalize-space()='Update Email']"));
    private static final Locator REMOVE_PHONE_OPTION = new Locator("REMOVE_PHONE_OPTION", By.xpath("//p[normalize-space()='Remove Phone Number']"));
    private static final Locator SUSPENSION_NOTES_FIELD = new Locator("SUSPENSION_NOTES_FIELD", By.cssSelector("[data-testid='modal-action-notes-input']"));
    private static final Locator NEW_NAME_FIELD = new Locator("NEW_NAME_FIELD", By.xpath("//input[@placeholder='New Name'] | //*[@data-testid='modal-update-name-input']"));
    private static final Locator NEW_EMAIL_FIELD = new Locator("NEW_EMAIL_FIELD", By.id("new-email-address"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", By.xpath("//button[contains(text(), 'Continue')]"));
    private static final Locator DELETE_MODAL_CONTINUE_BUTTON = new Locator("DELETE_MODAL_CONTINUE_BUTTON",
            By.xpath("//*[@id='modal-root']//button[normalize-space()='Continue']"));
    private static final Locator PASSWORD_FIELD = new Locator("PASSWORD_FIELD", By.cssSelector("[data-testid='input-password']"));
    private static final Locator NOTES_REQUIRED_ERROR = new Locator("NOTES_REQUIRED_ERROR",
            By.xpath("//*[normalize-space()='Please fill in notes.']"));
    private static final Locator MEMBER_NAME = new Locator("MEMBER_NAME", By.xpath("//div[contains(@class, 'styles_title_custom_member_personal_info')]/div[1]"));
    private static final Locator MEMBER_STATUS = new Locator("MEMBER_STATUS", By.xpath("//div[contains(@class, 'styles_title_custom_member_personal_info')]/div[2]"));
    private static final Locator MEMBER_DOB = new Locator("MEMBER_DOB", By.xpath("//tbody//td[contains(., 'Change Date of Birth')]"));
    private static final Locator MEMBER_EMAIL = new Locator("MEMBER_EMAIL", By.xpath("//div[contains(@class, 'styles_detail_info_item')][.//text()[contains(., 'Email')]]//div[contains(@class, 'verify')]"));
    private static final Locator BCA_COBRAND_CARD = new Locator("BCA_COBRAND_CARD", By.xpath("//div[contains(@class,'styles_detail_info_item')][contains(.,'BCA Cobrand Card')]//div[contains(@class,'verify')]"));
    private static final Locator MEMBER_LOGS_FIRST_DATA_ROW = new Locator("MEMBER_LOGS_FIRST_DATA_ROW",
            By.xpath("//*[normalize-space()='Member Logs']/following::table[1]//tbody/tr[1]"));
    private static final Locator MEMBER_PHONE_INFO = new Locator("MEMBER_PHONE_INFO", By.xpath("//div[contains(@class, 'detail_info_item')][starts-with(normalize-space(), 'Phone:')]"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//div[contains(@class, 'snackbar') and contains(@class, 'type_success')]"));
    private static final Locator DOB_INPUT = new Locator("DOB_INPUT", By.xpath("//input[@placeholder='Date of Birth'] | //label[normalize-space()='Date of Birth']/preceding-sibling::input"));
    private static final Locator DATE_PICKER_SAVE_BUTTON = new Locator("DATE_PICKER_SAVE_BUTTON", By.xpath("//button[normalize-space()='Save']"));
    private static final Locator PASSWORD_CONTINUE_BUTTON = new Locator("PASSWORD_CONTINUE_BUTTON", By.cssSelector("[data-testid='continue-modal-password-button']"));
    private static final Locator DELETE_ACCOUNT_OPTION = new Locator("DELETE_ACCOUNT_OPTION", By.xpath("//div[contains(@class, 'styles_dropdown_option')]//p[normalize-space(text())='Delete Account']"));
    private static final Locator DELETE_MODAL_TITLE = new Locator("DELETE_MODAL_TITLE", By.xpath("//h2[text()='Delete Account']"));
    private static final Locator DELETE_MODAL_WARNING = new Locator("DELETE_MODAL_WARNING",
            By.xpath("//*[@id='modal-root']//*[contains(normalize-space(),'will not be accessible anymore')]"));

    /** Status shown on member details when the account is a connected BCA cobrand cardholder. */
    public static final String BCA_COBRAND_CARD_CONNECTED = "Connected";

    /** Action text on the member log after a successful BCA cobrand registration. */
    public static final String ACCOUNT_REGISTRATION_SUCCESSFUL = "Account Registration Successful.";

    /** Member-log source written when BCA registers a cardholder via the entity registration API. */
    public static final String BCA_COBRAND_REGISTRATION_SOURCE = "Registration source: BCA_COBRAND - API";

    /** Action text on the member log after BCA maps an existing UNM account as a cobrand cardholder. */
    public static final String ACCOUNT_MAPPED_WITH_BCA_COBRAND_CARD = "Account is Mapped with BCA Cobrand Card";

    /** Warning copy under the Delete Account modal title. */
    public static final String DELETE_ACCOUNT_INACCESSIBILITY_WARNING =
            "This account will not be accessible anymore, and user cannot re-access old details of this account.";

    private static final String BCA_COBRAND_SOURCE_MARKER = "BCA_COBRAND";
    private static final int MEMBER_LOG_READY_ATTEMPTS = 8;
    private static final long MEMBER_LOG_RETRY_DELAY_MS = 2500L;

    public void unSuspendAccount(String reason, String password) {
        clickAction();
        clickUnsuspend();
        inputReason(reason);
        clickContinue();
        inputPassword(password);
        clickContinue();
    }

    public void clickAction() {
        driverTools.waitForElementToBeClickable(ACTION_BUTTON, 10).orElseThrow();
        driverTools.click(ACTION_BUTTON);
    }

    public void clickSuspend() {
        driverTools.waitForElementToAppear(SUSPEND_OPTION, 5).orElseThrow();
        driverTools.click(SUSPEND_OPTION);
    }

    public void clickUnsuspend() {
        driverTools.waitForElementToAppear(UNSUSPEND_OPTION, 5).orElseThrow();
        driverTools.click(UNSUSPEND_OPTION);
    }

    public void clickUpdateName() {
        driverTools.waitForElementToAppear(UPDATE_NAME_OPTION, 5).orElseThrow();
        driverTools.click(UPDATE_NAME_OPTION);
    }

    public void clickUpdateEmail() {
        driverTools.waitForElementToAppear(UPDATE_EMAIL_OPTION, 5).orElseThrow();
        driverTools.click(UPDATE_EMAIL_OPTION);
    }

    public void inputReason(String reason) {
        driverTools.waitForElementToAppear(SUSPENSION_NOTES_FIELD).orElseThrow();
        driverTools.sendKeys(SUSPENSION_NOTES_FIELD, reason);
    }

    public void inputNewEmail(String email) {
        driverTools.waitForElementToAppear(NEW_EMAIL_FIELD).orElseThrow();
        driverTools.clearInput(NEW_EMAIL_FIELD);
        driverTools.sendKeys(NEW_EMAIL_FIELD, email);
    }

    public void inputNewName(String name) {
        driverTools.waitForElementToAppear(NEW_NAME_FIELD).orElseThrow();
        driverTools.sendKeys(NEW_NAME_FIELD, name);
    }

    public void clickContinue() {
        driverTools.waitForElementToAppear(CONTINUE_BUTTON).orElseThrow();
        driverTools.click(CONTINUE_BUTTON);
    }

    public void inputPassword(String password) {
        driverTools.waitForElementToAppear(PASSWORD_FIELD).orElseThrow();
        driverTools.sendKeys(PASSWORD_FIELD, password);
    }

    public void verifyStatus(String expectedStatus) {
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 4).ifPresent(e -> driverTools.waitForElementToDisappear(e));
        expectedStatus = expectedStatus.toLowerCase();
        driverTools.waitForElementToAppear(MEMBER_STATUS).orElseThrow();
        String actualStatus = driverTools.getText(MEMBER_STATUS).orElse("").toLowerCase();
        Assertion.assertThat("Verify Member Status", actualStatus, is(expectedStatus));
    }

    public void verifyName(String expectedName) {
        driverTools.waitForElementToAppear(MEMBER_NAME).orElseThrow();
        String actualName = driverTools.getText(MEMBER_NAME).orElse("");
        Assertion.assertThat("Verify Member Name", actualName, is(expectedName));
    }

    public void verifyBcaCobrandCardStatus(String expectedStatus) {
        driverTools.waitForElementToAppear(BCA_COBRAND_CARD, 10).orElseThrow();
        String actualStatus = driverTools.getText(BCA_COBRAND_CARD).orElse("").trim();
        Assertion.assertThat("BCA Cobrand Card status", actualStatus, equalToIgnoringCase(expectedStatus));
    }

    /**
     * Asserts the account is not a connected BCA cobrand cardholder. The dashboard may omit the
     * BCA Cobrand Card row, or show a status other than {@link #BCA_COBRAND_CARD_CONNECTED}.
     */
    public void verifyBcaCobrandCardIsNotConnected() {
        if (driverTools.waitForElementToAppear(BCA_COBRAND_CARD, 5).isEmpty()) {
            logger.log("BCA Cobrand Card row is absent — account is not a cobrand cardholder");
            return;
        }
        String actualStatus = driverTools.getText(BCA_COBRAND_CARD).orElse("").trim();
        Assertion.assertThat("BCA Cobrand Card is not connected",
                actualStatus, not(equalToIgnoringCase(BCA_COBRAND_CARD_CONNECTED)));
    }

    /**
     * Asserts the Member Logs first row for this cardholder: registration succeeded and the source is
     * {@link #BCA_COBRAND_REGISTRATION_SOURCE}.
     */
    public void verifyBcaCobrandRegistrationLog(String email) {
        waitForMemberLogsFirstRow(
                row -> row.contains(ACCOUNT_REGISTRATION_SUCCESSFUL)
                        && row.contains(BCA_COBRAND_REGISTRATION_SOURCE)
                        && row.toLowerCase().contains(email.toLowerCase()),
                "Member log should show account registration succeeded from BCA cobrand API for " + email);
    }

    public void verifyRegistrationSourceIsNotBcaCobrand() {
        waitForMemberLogsFirstRow(
                row -> row.contains(ACCOUNT_REGISTRATION_SUCCESSFUL) && !row.contains(BCA_COBRAND_SOURCE_MARKER),
                "Member log should show account registration succeeded without a BCA cobrand source");
    }

    /**
     * Refreshes until the Member Logs first row shows {@link #ACCOUNT_MAPPED_WITH_BCA_COBRAND_CARD}.
     */
    public void verifyBcaCobrandMappingLog() {
        waitForMemberLogsFirstRow(
                row -> row.contains(ACCOUNT_MAPPED_WITH_BCA_COBRAND_CARD),
                "Member log should show the account was mapped with a BCA cobrand card");
    }

    private void waitForMemberLogsFirstRow(Predicate<String> rowIsReady, String expectation) {
        int[] attempt = {0};
        String lastRowText = RetryUtils.retryUntil(
                MEMBER_LOG_READY_ATTEMPTS,
                MEMBER_LOG_RETRY_DELAY_MS,
                () -> {
                    if (attempt[0]++ > 0) {
                        driverTools.refreshWebpage();
                        driverTools.waitForWebToLoad();
                    }
                    if (driverTools.waitForElementToAppear(MEMBER_LOGS_FIRST_DATA_ROW, 10).isEmpty()) {
                        return "";
                    }
                    return driverTools.getText(MEMBER_LOGS_FIRST_DATA_ROW).orElse("");
                },
                row -> row != null && !row.isBlank() && rowIsReady.test(row),
                "Member logs first row");
        if (lastRowText == null || !rowIsReady.test(lastRowText)) {
            Assertion.fail(expectation + " after " + MEMBER_LOG_READY_ATTEMPTS
                    + " attempts. Last row: " + lastRowText);
        }
    }

    public void verifySuccessSnackbar(String expectedSuccessMessage) {
        expectedSuccessMessage = expectedSuccessMessage.toLowerCase();

        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 10).orElseThrow();
        String actualSuccessMessage = driverTools.getText(SUCCESS_SNACKBAR).orElse("").toLowerCase();
        driverTools.waitForElementToDisappear(SUCCESS_SNACKBAR);
        
        Assertion.assertThat("Verify Success Message", actualSuccessMessage, containsStringIgnoringCase(expectedSuccessMessage));
    }

    public void clickUpdateDOB() {
        driverTools.waitForElementToBeClickable(UPDATE_DOB_OPTION, 5).orElseThrow();
        driverTools.click(UPDATE_DOB_OPTION);
    }

    public void selectDateOfBirth(String day) {
        driverTools.waitForElementToBeClickable(DOB_INPUT, 5).orElseThrow();
        driverTools.click(DOB_INPUT);
        Locator dayButton = new Locator("DAY_" + day, By.xpath("//button[not(@disabled)]/span[normalize-space()='" + day + "']"));
        driverTools.waitForElementToBeClickable(dayButton, 5).orElseThrow();
        driverTools.click(dayButton);
        driverTools.waitForElementToBeClickable(DATE_PICKER_SAVE_BUTTON, 5).orElseThrow();
        driverTools.click(DATE_PICKER_SAVE_BUTTON);
    }

    public void selectTodayDateOfBirth() {
        driverTools.waitForElementToBeClickable(DOB_INPUT, 5).orElseThrow();
        driverTools.click(DOB_INPUT);
        Locator todayButton = new Locator("TODAY_BUTTON", By.xpath("//small[text()='Today']/parent::button"));
        driverTools.waitForElementToBeClickable(todayButton, 5).orElseThrow();
        driverTools.click(todayButton);
        driverTools.waitForElementToBeClickable(DATE_PICKER_SAVE_BUTTON, 5).orElseThrow();
        driverTools.click(DATE_PICKER_SAVE_BUTTON);
    }

    public void confirmWithPassword(String password) {
        driverTools.waitForElementToAppear(PASSWORD_FIELD, 5).orElseThrow();
        driverTools.sendKeys(PASSWORD_FIELD, password);
        driverTools.waitForElementToBeClickable(PASSWORD_CONTINUE_BUTTON, 5).orElseThrow();
        driverTools.click(PASSWORD_CONTINUE_BUTTON);
    }

    public void verifyDOB(String expectedDOB) {
        boolean textPresent = driverTools.waitForTextToBePresent(MEMBER_DOB, expectedDOB, 10);
        Assertion.assertThat("Verify Date of Birth contains expected date", textPresent, is(true));
    }

    public void verifyEmail(String expectedEmail) {
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 5).ifPresent(e -> driverTools.waitForElementToDisappear(e));
        driverTools.waitForElementToAppear(MEMBER_EMAIL, 10).orElseThrow();
        boolean textMatched = driverTools.waitForTextToBePresent(MEMBER_EMAIL, expectedEmail, 10);
        if (!textMatched) {
            String actualEmail = driverTools.getText(MEMBER_EMAIL).orElse("").trim();
            Assertion.fail("Verify Member Email — expected: " + expectedEmail + ", but was: " + actualEmail);
        }
    }

    public void updateEmail(String newEmail, String reason, String password) {
        clickAction();
        clickUpdateEmail();
        inputReason(reason);
        inputNewEmail(newEmail);
        clickContinue();
        inputPassword(password);
        clickContinue();
    }

    public void verifyLogsActivity(String expectedLog) {
        String safeLog = expectedLog.replace("'", "\\'");
        Locator logEntry = new Locator("LOG_ENTRY_" + expectedLog, By.xpath("//tbody//td[contains(., '" + safeLog + "')]"));
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            if (attempt > 1) {
                driverTools.refreshWebpage();
                driverTools.waitForWebToLoad();
            }
            if (driverTools.waitForElementToAppear(logEntry, 10).isPresent()) {
                return;
            }
        }
        Assertion.fail("Verify Logs Activity: could not find '" + expectedLog + "' after " + maxAttempts + " attempts.");
    }

    public void clickRemovePhoneNumber() {
        driverTools.waitForElementToAppear(REMOVE_PHONE_OPTION, 5).orElseThrow();
        driverTools.click(REMOVE_PHONE_OPTION);
    }

    public void verifyPhoneNumberEmpty() {
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 4).ifPresent(e -> driverTools.waitForElementToDisappear(e));
        driverTools.refreshWebpage();
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToAppear(MEMBER_PHONE_INFO, 10).orElseThrow();
        String phoneText = driverTools.getText(MEMBER_PHONE_INFO).orElse("").replace("Phone:", "").trim();
        Assertion.assertThat("Phone number should be empty after removal", phoneText, is("-"));
    }

    public void verifyRemovePhoneAndUpdateEmailNotAvailable() {
        clickAction();

        boolean removePhoneDisabled = isMenuOptionDisabledOrAbsent(REMOVE_PHONE_OPTION);
        boolean updateEmailDisabled = isMenuOptionDisabledOrAbsent(UPDATE_EMAIL_OPTION);

        driverTools.click(ACTION_BUTTON);

        Assertion.assertThat("Remove Phone Number option should be disabled or absent", removePhoneDisabled, is(true));
        Assertion.assertThat("Update Email option should be disabled or absent", updateEmailDisabled, is(true));
    }

    private boolean isMenuOptionDisabledOrAbsent(Locator option) {
        return driverTools.waitForElementToAppear(option, 3)
                .map(el -> {
                    String cls = el.getAttribute("class");
                    String ariaDisabled = el.getAttribute("aria-disabled");
                    boolean classDisabled = cls != null && cls.toLowerCase().contains("disabled");
                    boolean ariaFlagDisabled = "true".equalsIgnoreCase(ariaDisabled);
                    return classDisabled || ariaFlagDisabled;
                })
                .orElse(true);
    }

    public void clickDeleteAccountOption() {
        driverTools.waitForElementToAppear(DELETE_ACCOUNT_OPTION, 5).orElseThrow();
        driverTools.click(DELETE_ACCOUNT_OPTION);
    }

    public void verifyModalAndConfirmDeletion(String deletionNotes) {
        driverTools.waitForElementToAppear(DELETE_MODAL_TITLE, 10).orElseThrow(
                () -> new NoSuchElementException("Delete Account modal did not appear")
        );
        driverTools.waitForElementToAppear(DELETE_MODAL_WARNING, 10).orElseThrow(
                () -> new NoSuchElementException("Delete Account inaccessibility warning did not appear")
        );
        String warning = driverTools.getText(DELETE_MODAL_WARNING).orElse("").trim();
        Assertion.assertThat("Delete Account modal shows the inaccessibility warning",
                warning, containsStringIgnoringCase(DELETE_ACCOUNT_INACCESSIBILITY_WARNING));
        driverTools.waitForElementToAppear(SUSPENSION_NOTES_FIELD, 10).orElseThrow(
                () -> new NoSuchElementException("Notes textarea not found")
        );
        driverTools.sendKeys(SUSPENSION_NOTES_FIELD, deletionNotes);
        driverTools.waitForElementToBeClickable(DELETE_MODAL_CONTINUE_BUTTON, 10).orElseThrow(
                () -> new NoSuchElementException("Continue button not found")
        );
        driverTools.clickWithJavascriptExecutor(DELETE_MODAL_CONTINUE_BUTTON);
        driverTools.waitForElementToAppear(PASSWORD_FIELD, 10).orElseThrow(
                () -> new NoSuchElementException(driverTools.isElementDisplayed(NOTES_REQUIRED_ERROR)
                        ? "Password confirmation did not appear after Continue: notes were rejected"
                        : "Password confirmation did not appear after Continue")
        );
    }

    public void fillPasswordAndConfirm(String password) {
        driverTools.waitForElementToAppear(PASSWORD_FIELD, 10).orElseThrow(
                () -> new NoSuchElementException("Password input field did not appear")
        );
        driverTools.sendKeys(PASSWORD_FIELD, password);
        driverTools.waitForElementToBeClickable(PASSWORD_CONTINUE_BUTTON, 10).orElseThrow(
                () -> new NoSuchElementException("Continue button not found on password modal")
        );
        driverTools.clickWithActions(PASSWORD_CONTINUE_BUTTON);
    }

    public void verifySnackbarMessageAndDismissal(String expectedMessage) {
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 10).orElseThrow(
                () -> new NoSuchElementException("Success snackbar did not appear in time")
        );
        String actualMessage = driverTools.getText(SUCCESS_SNACKBAR).orElse("").trim();
        Assertion.assertThat("Verify snackbar message", actualMessage, containsStringIgnoringCase(expectedMessage.trim()));
        boolean isDismissed = driverTools.waitForElementToDisappear(SUCCESS_SNACKBAR, 10);
        Assertion.assertThat("Verify the snackbar successfully dismissed itself", isDismissed, is(true));
    }
}
