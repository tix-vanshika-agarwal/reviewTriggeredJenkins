package com.tiket.web.dashboard.page.platform.eiffel.unm;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.web.WebPage;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;

/**
 * Page object for the UNM Dashboard "Role Based Access Control" (RBAC) menu
 * (Settings &gt; Role Based Access Control, at {@code /admin/rbac}).
 * Covers adding a role with access and verifying the assigned access via the
 * "View Access" action. Mirrors the modal/table patterns used by
 * {@link UnmDashboardUserPage}.
 */
public class UnmRbacPage<T extends WebDriver> extends WebPage<T> {

    private static final int DEFAULT_TIMEOUT = 10;
    private static final int SHORT_TIMEOUT = 5;
    private static final int LONG_TIMEOUT = 15;

    private static final Locator SETTINGS_MENU = new Locator("SETTINGS_MENU", By.xpath("//p[normalize-space()='Settings']"));
    private static final Locator RBAC_MENU = new Locator("RBAC_MENU", By.xpath("//a[@href='/admin/rbac']"));
    private static final Locator PAGE_HEADING = new Locator("PAGE_HEADING", By.xpath("//h3[normalize-space()='Role Based Access Control']"));

    private static final Locator ADD_ROLE_BUTTON = new Locator("ADD_ROLE_BUTTON", By.cssSelector("[data-testid='add-role-button']"));
    private static final Locator ROLE_NAME_INPUT = new Locator("ROLE_NAME_INPUT", By.cssSelector("[data-testid='role-name-text-input']"));
    private static final Locator ROLE_DESC_INPUT = new Locator("ROLE_DESC_INPUT", By.cssSelector("[data-testid='role-description-text-input']"));
    private static final Locator ACCESS_DROPDOWN = new Locator("ACCESS_DROPDOWN", By.cssSelector("[data-testid='dropdown-multiple-access']"));
    private static final Locator ACCESS_OPTION = new Locator("ACCESS_OPTION", By.cssSelector("[data-testid^='option-multiple-']"));
    private static final Locator ADD_SUBMIT_BUTTON = new Locator("ADD_SUBMIT_BUTTON", By.cssSelector("[data-testid='add-edit-role-button']"));

    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//div[contains(@class, 'snackbar') and contains(@class, 'type_success')]//div[contains(@class, 'text_content')]"));

    private static final Locator SHOWING_TEXT = new Locator("SHOWING_TEXT", By.xpath("//p[contains(normalize-space(), 'Showing') and contains(normalize-space(), 'of')]"));
    private static final Locator PAGE_NUMBER_BUTTONS = new Locator("PAGE_NUMBER_BUTTONS", By.xpath("//button[normalize-space()!='' and translate(normalize-space(), '0123456789', '')='']"));

    private static final Locator VIEW_ACCESS_OPTION = new Locator("VIEW_ACCESS_OPTION", By.xpath("//p[normalize-space()='View Access']"));
    private static final Locator VIEW_ACCESS_HEADING = new Locator("VIEW_ACCESS_HEADING", By.xpath("//h2[normalize-space()='View Role Access']"));
    private static final Locator VIEW_ACCESS_CONTAINER = new Locator("VIEW_ACCESS_CONTAINER", By.xpath("//div[contains(@class, 'view_access')]"));

    public UnmRbacPage(Session<T> session) {
        super(session);
    }

    public void goToRbacMenu() {
        if (!driverTools.waitForElementToAppear(RBAC_MENU, SHORT_TIMEOUT).isPresent()) {
            driverTools.click(SETTINGS_MENU);
            driverTools.waitForElementToAppear(RBAC_MENU, SHORT_TIMEOUT);
        }
        driverTools.click(RBAC_MENU);
        if (!driverTools.waitForElementToAppear(PAGE_HEADING, DEFAULT_TIMEOUT).isPresent()) {
            driverTools.clickWithJavascriptExecutor(RBAC_MENU);
        }
        driverTools.waitForElementToAppear(PAGE_HEADING, DEFAULT_TIMEOUT).orElseThrow();
        driverTools.waitForElementToAppear(ADD_ROLE_BUTTON, DEFAULT_TIMEOUT).orElseThrow();
    }

    public void clickAddRole() {
        driverTools.click(ADD_ROLE_BUTTON);
        driverTools.waitForElementToAppear(ROLE_NAME_INPUT, DEFAULT_TIMEOUT).orElseThrow();
    }

    public void inputRoleDetails(String roleName, String roleDescription) {
        driverTools.waitForElementToAppear(ROLE_NAME_INPUT, DEFAULT_TIMEOUT);
        driverTools.clearInput(ROLE_NAME_INPUT);
        driverTools.sendKeys(ROLE_NAME_INPUT, roleName);
        driverTools.clearInput(ROLE_DESC_INPUT);
        driverTools.sendKeys(ROLE_DESC_INPUT, roleDescription);
    }

    /**
     * Opens the Access dropdown, selects {@code count} distinct random access
     * options, then closes the dropdown so the Add button is clickable.
     *
     * @return the list of access display names that were selected
     */
    public List<String> selectRandomAccess(int count) {
        driverTools.click(ACCESS_DROPDOWN);
        driverTools.waitForElementsToAppear(ACCESS_OPTION, DEFAULT_TIMEOUT).orElseThrow();

        List<String> distinctNames = new ArrayList<>(new LinkedHashSet<>(driverTools.getTexts(ACCESS_OPTION)));
        distinctNames.removeIf(name -> name == null || name.isBlank());

        if (distinctNames.size() < count) {
            Assertion.fail("Not enough access options to select. Required: " + count + ", available: " + distinctNames.size());
        }

        Collections.shuffle(distinctNames);
        List<String> selected = new ArrayList<>(distinctNames.subList(0, count));

        for (String name : selected) {
            Locator optionLabel = new Locator("ACCESS_OPTION_" + name,
                    By.xpath("//div[starts-with(@data-testid, 'option-multiple-')][normalize-space()='" + name + "']//label"));
            driverTools.click(optionLabel);
        }

        closeAccessDropdown();
        return selected;
    }

    private void closeAccessDropdown() {
        driverTools.click(ACCESS_DROPDOWN);
        driverTools.waitForElementToDisappear(ACCESS_OPTION, SHORT_TIMEOUT);
    }

    public void clickAdd() {
        driverTools.click(ADD_SUBMIT_BUTTON);
    }

    public void verifySuccessSnackbar(String expectedText) {
        Optional<WebElement> snackbar = driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, LONG_TIMEOUT);
        Assertion.assertThat("Success snackbar is visible", snackbar.isPresent(), is(true));

        String actualText = snackbar.map(WebElement::getText).orElse("").trim();
        Assertion.assertThat("Success snackbar text matches", actualText, is(expectedText));
    }

    public void goToLastPage() {
        driverTools.waitForElementToAppear(SHOWING_TEXT, DEFAULT_TIMEOUT);

        int lastPage = driverTools.getTexts(PAGE_NUMBER_BUTTONS).stream()
                .filter(text -> text.matches("\\d+"))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(1);

        if (lastPage > 1) {
            String showingBefore = driverTools.getText(SHOWING_TEXT).orElse("");
            Locator lastPageButton = new Locator("PAGE_" + lastPage, By.xpath("//button[normalize-space()='" + lastPage + "']"));
            driverTools.click(lastPageButton);
            waitForShowingTextToChange(showingBefore);
        }
    }

    private void waitForShowingTextToChange(String previousText) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))
                    .until(d -> !driverTools.getText(SHOWING_TEXT).orElse("").equals(previousText));
        } catch (TimeoutException e) {
            logger.softWarning("Pagination footer text did not change after navigating to the last page; proceeding.");
        }
    }

    public void openViewAccess(String roleName) {
        Locator roleActionButton = new Locator("ROLE_ACTION_" + roleName,
                By.xpath("//td[contains(@class, 'styles_column_role')]//span[normalize-space()='" + roleName
                        + "']/ancestor::tr[1]//*[starts-with(@data-testid, 'action-button-')]"));

        boolean found = driverTools.waitForElementToAppear(roleActionButton, DEFAULT_TIMEOUT).isPresent();
        Assertion.assertThat("Newly created role '" + roleName + "' is found in the role list", found, is(true));

        driverTools.click(roleActionButton);
        driverTools.waitForElementToAppear(VIEW_ACCESS_OPTION, SHORT_TIMEOUT).orElseThrow();
        driverTools.click(VIEW_ACCESS_OPTION);
        driverTools.waitForElementToAppear(VIEW_ACCESS_HEADING, DEFAULT_TIMEOUT).orElseThrow();
    }

    public void verifyRoleAccess(String roleName, List<String> expectedAccess) {
        driverTools.waitForElementToAppear(VIEW_ACCESS_HEADING, DEFAULT_TIMEOUT).orElseThrow();

        String summary = driverTools.getText(VIEW_ACCESS_CONTAINER).orElse("");
        Assertion.assertThat("View Access modal shows the role name", summary.contains(roleName), is(true));
        Assertion.assertThat("View Access modal shows the correct access count",
                summary.contains(expectedAccess.size() + " access"), is(true));

        for (String access : expectedAccess) {
            Locator accessCell = new Locator("ACCESS_CELL_" + access,
                    By.xpath("//div[contains(@class, 'view_access')]//td[normalize-space()='" + access + "']"));
            boolean present = driverTools.waitForElementToAppear(accessCell, SHORT_TIMEOUT).isPresent();
            Assertion.assertThat("Access '" + access + "' is assigned to the role", present, is(true));
        }
    }
}
