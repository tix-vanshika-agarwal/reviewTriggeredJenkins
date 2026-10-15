package com.tiket.web.dashboard.page.platform.eiffel.unm;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.core.Is.is;

public class UnmDashboardUserPage<T extends WebDriver> extends BasePage<T> {

    private static final int DEFAULT_TIMEOUT = 10;
    private static final int SHORT_TIMEOUT = 5;
    private static final int MAX_RETRY = 3;

    public UnmDashboardUserPage(Session<T> session) {
        super(session);
    }

    private static final Locator DASHBOARD_USER_MENU = new Locator("DASHBOARD_USER_MENU", By.xpath("//a[@href='/admin/dashboard-user']"));
    private static final Locator ADD_USER_BUTTON = new Locator("ADD_USER_BUTTON", By.cssSelector("[data-testid='add-user-button']"));
    private static final Locator PLEASE_WAIT_LOADING = new Locator("PLEASE_WAIT_LOADING", By.xpath("//h2[normalize-space()='Please wait']"));
    private static final Locator FIRST_ROW_USER = new Locator("FIRST_ROW_USER", By.xpath("//tbody/tr[1]//td[1]"));
    private static final Locator FULL_NAME_INPUT = new Locator("FULL_NAME_INPUT", By.cssSelector("[data-testid='dashboard-user-name-input']"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", By.cssSelector("[data-testid='dashboard-user-email-input']"));
    private static final Locator ROLE_DROPDOWN = new Locator("ROLE_DROPDOWN", By.cssSelector("[data-testid='dropdown-select-role']"));
    private static final Locator ADD_BUTTON = new Locator("ADD_BUTTON", By.cssSelector("[data-testid='manage-user-submit-button']"));
    private static final Locator SEARCH_EMAIL_INPUT = new Locator("SEARCH_EMAIL_INPUT", By.cssSelector("[data-testid='input-text-keyword']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.cssSelector("[data-testid='search-button']"));
    private static final Locator ACTION_BUTTON = new Locator("ACTION_BUTTON", By.cssSelector("[data-testid='action-button-0']"));
    private static final Locator DELETE_USER_OPTION = new Locator("DELETE_USER_OPTION", By.xpath("//p[text()='Delete User']"));
    private static final Locator PASSWORD_INPUT = new Locator("PASSWORD_INPUT", By.cssSelector("[data-testid='input-password']"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", By.xpath("//button[text()='Continue']"));
    private static final Locator SNACKBAR = new Locator("SNACKBAR", By.xpath("//div[contains(@class, 'snackbar') and contains(@class, 'type_success')]"));

    public void goToDashboardUserMenu() {
        driverTools.click(DASHBOARD_USER_MENU);
        driverTools.waitForElementToDisappear(PLEASE_WAIT_LOADING, DEFAULT_TIMEOUT);
        driverTools.waitForElementToAppear(ADD_USER_BUTTON);
    }

    public void clickAddUser() {
        driverTools.waitForElementToAppear(ADD_USER_BUTTON, DEFAULT_TIMEOUT);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driverTools.click(ADD_USER_BUTTON);
    }

    public void inputUserDetails(String fullName, String email, String role) {
        boolean isVisible = false;
        for (int i = 0; i < MAX_RETRY; i++) {
            if (driverTools.waitForElementToAppear(FULL_NAME_INPUT, SHORT_TIMEOUT).isPresent()) {
                isVisible = true;
                break;
            }
            logger.log("Full Name input not visible, clicking Add User button again. Attempt: " + (i + 1));
            driverTools.click(ADD_USER_BUTTON);
            driverTools.dummyWait(1);
        }

        if (!isVisible) {
            Assertion.fail("Add User modal inputs did not appear after " + MAX_RETRY + " retries");
        }

        driverTools.clearInput(FULL_NAME_INPUT);
        driverTools.sendKeys(FULL_NAME_INPUT, fullName);
        driverTools.clearInput(EMAIL_INPUT);
        driverTools.sendKeys(EMAIL_INPUT, email);
        driverTools.click(ROLE_DROPDOWN);
        selectRoleOption(role);
    }

    private void selectRoleOption(String role) {
        Locator roleOption = new Locator("ROLE_OPTION_" + role, By.xpath("//p[contains(text(), '" + role + "')]"));
        driverTools.waitForElementToAppear(roleOption).orElseThrow().click();
    }

    public void clickAdd() {
        driverTools.click(ADD_BUTTON);
    }

    public void verifySuccessSnackbar(String expectedText) {
        driverTools.waitForElementToAppear(SNACKBAR, DEFAULT_TIMEOUT);
        String actualText = driverTools.getText(SNACKBAR).orElse("");
        Assertion.assertThat("Verify success snackbar text", actualText, is(expectedText));
        driverTools.waitForElementToDisappear(SNACKBAR, DEFAULT_TIMEOUT);
    }

    public void searchUser(String email) {
        driverTools.clearInput(SEARCH_EMAIL_INPUT);
        driverTools.sendKeys(SEARCH_EMAIL_INPUT, email);
        driverTools.click(SEARCH_BUTTON);
        driverTools.waitForElementToDisappear(PLEASE_WAIT_LOADING, DEFAULT_TIMEOUT);
        driverTools.waitForElementToAppear(FIRST_ROW_USER, SHORT_TIMEOUT);
    }

    public void deleteUser(String password) {
        driverTools.click(ACTION_BUTTON);
        driverTools.waitForElementToAppear(DELETE_USER_OPTION, SHORT_TIMEOUT);
        driverTools.click(DELETE_USER_OPTION);
        driverTools.waitForElementToAppear(PASSWORD_INPUT, SHORT_TIMEOUT);
        driverTools.sendKeys(PASSWORD_INPUT, password);
        driverTools.click(CONTINUE_BUTTON);
    }
}
