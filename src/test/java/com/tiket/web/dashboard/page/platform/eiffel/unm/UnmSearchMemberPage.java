package com.tiket.web.dashboard.page.platform.eiffel.unm;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.web.WebPage;
import com.tiket.testbase.Assertion;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UnmSearchMemberPage<T extends WebDriver> extends WebPage<T> {
    public UnmSearchMemberPage(Session<T> session) {
        super(session);
    }

    private String searchBy;

    private static final Locator SEARCH_MEMBER_CONTAINER = new Locator("SEARCH_MEMBER_CONTAINER", By.xpath("//div[contains(@class, 'styles_search_member_page_container')]"));
    private static final Locator SEARCH_BY_DROPDOWN = new Locator("SEARCH_BY_DROPDOWN", By.id("search-by"));
    private static final Locator SEARCH_BY_PHONE = new Locator("SEARCH_BY_PHONE", By.cssSelector("[data-testid='option-single-0']"));
    private static final Locator SEARCH_BY_EMAIL = new Locator("SEARCH_BY_EMAIL", By.cssSelector("[data-testid='option-single-1']"));
    private static final Locator SEARCH_EMAIL_FIELD = new Locator("SEARCH_EMAIL_FIELD", By.id("sb-text-input"));
    private static final Locator SEARCH_PHONE_FIELD = new Locator("SEARCH_PHONE_FIELD", By.id("phone-number"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.cssSelector("[data-testid='button-search-filter']"));
    private static final Locator DATA_NOT_FOUND = new Locator("DATA_NOT_FOUND", By.cssSelector("[data-testid='member-search-result-empty']"));
    private static final Locator TABLE_LIST = new Locator("TABLE_LIST", By.cssSelector("[data-testid='table-list']"));
    private static final Locator TABLE_ROW = new Locator("TABLE_ROW", By.xpath("//tbody/tr"));
    private static final Locator VIEW_BUTTONS = new Locator("VIEW_BUTTONS", By.xpath("//p[normalize-space()='View']"));

    public void searchMember(String by, String value) {
        this.searchBy = by.toLowerCase();
        if (!this.searchBy.equals("email") && !this.searchBy.equals("phone")) {
            Assertion.fail("Search by " + by + " is not supported. Please use 'email' or 'phone'.");
        }

        driverTools.waitForElementToAppear(SEARCH_MEMBER_CONTAINER).orElseThrow();
        String currentSearchBy = getSearchByDropdownValue();

        if (!currentSearchBy.equals(this.searchBy)) {
            driverTools.click(SEARCH_BY_DROPDOWN);
            driverTools.waitForElementToAppear(SEARCH_BY_EMAIL).orElseThrow();
            selectSearchByDropdownValue(this.searchBy);
        }

        if (this.searchBy.equals("email")) {
            driverTools.click(SEARCH_EMAIL_FIELD);
            driverTools.clearInput(SEARCH_EMAIL_FIELD);
            driverTools.sendKeys(SEARCH_EMAIL_FIELD, value);
        } else if (this.searchBy.equals("phone")) {
            driverTools.click(SEARCH_PHONE_FIELD);
            driverTools.clearInput(SEARCH_PHONE_FIELD);
            driverTools.sendKeys(SEARCH_PHONE_FIELD, value);
        }

        driverTools.click(SEARCH_BUTTON);
        boolean tableFound = driverTools.waitForElementToAppear(TABLE_LIST, 10).isPresent();
        if (!tableFound) {
            boolean emptyFound = driverTools.waitForElementToAppear(DATA_NOT_FOUND, 5).isPresent();
            if (!emptyFound) {
                Assertion.fail("Search did not return results or empty state within timeout for: " + value);
            }
        }
    }

    private String getSearchByDropdownValue() {
        return driverTools.getValue(SEARCH_BY_DROPDOWN).replaceAll(" ", "").toLowerCase();
    }

    private void selectSearchByDropdownValue(String value) {
        if (value.equals("email")) {
            driverTools.click(SEARCH_BY_EMAIL);
        } else if (value.equals("phone")) {
            driverTools.click(SEARCH_BY_PHONE);
        } else {
            Assertion.fail("Search by " + this.searchBy + " is not supported. Please use 'email' or 'phone'.");
        }
    }

    public void verifyFirstResultRow(String value) {
        if (!isDataFound()) {
            Assertion.fail("No data found for " + this.searchBy + " with value: " + value);
        }

        WebElement tableList = driverTools.element(TABLE_LIST).orElseThrow();
        List<WebElement> rows = driverTools.elements(TABLE_ROW, tableList).orElseThrow();
        if (rows.size() == 0) {
            Assertion.fail("No data found for " + this.searchBy + " with value: " + value);
        }

        WebElement firstRow = rows.get(0);
        List<WebElement> cells = driverTools.elements(new Locator("TD", By.tagName("td")), firstRow).orElseThrow();

        String actualUnmId = driverTools.getText(cells.get(0));
        String actualName = driverTools.getText(cells.get(1));
        String actualPhone = driverTools.getText(cells.get(2)).replaceAll(" ", "");
        String actualEmail = driverTools.getText(cells.get(3)).trim().toLowerCase();
        String actualStatus = driverTools.getText(cells.get(4));

        Assertion.assertThat("Verify UNM ID", actualUnmId, is(notNullValue()));
        Assertion.assertThat("Verify Name", actualName, is(notNullValue()));
        Assertion.assertThat("Verify Email", actualEmail, this.searchBy.equals("email") ? is(value) : is(notNullValue()));
        Assertion.assertThat("Verify Phone", actualPhone, this.searchBy.equals("phone") ? is(value) : is(notNullValue()));
        Assertion.assertThat("Verify Status", actualStatus, is(notNullValue()));
    }

    public boolean isDataFound() {
        return !driverTools.isElementDisplayed(DATA_NOT_FOUND);
    }

    public void clickViewFirstResult() {
        if (!isDataFound()) {
            Assertion.fail("Cannot click View: No data found.");
        }
        driverTools.waitForElementToAppear(VIEW_BUTTONS, 5).orElseThrow();
        driverTools.waitForElementToBeClickable(VIEW_BUTTONS, 5).orElseThrow();
        driverTools.click(VIEW_BUTTONS);
    }

    public void verifyAccountStatus(String value, String expectedStatus) {
        if (!isDataFound()) {
            Assertion.fail("No data found for " + this.searchBy + " with value: " + value);
        }
        WebElement tableList = driverTools.element(TABLE_LIST).orElseThrow();
        List<WebElement> rows = driverTools.elements(TABLE_ROW, tableList).orElseThrow();
        if (rows.isEmpty()) {
            Assertion.fail("No data found for " + this.searchBy + " with value: " + value);
        }
        WebElement firstRow = rows.get(0);
        List<WebElement> cells = driverTools.elements(new Locator("TD", By.tagName("td")), firstRow).orElseThrow();
        if (cells.size() <= 4) {
            Assertion.fail("Unexpected table column count (" + cells.size() + ") when searching by " + this.searchBy + " with value: " + value);
        }
        String actualStatus = driverTools.getText(cells.get(4));
        Assertion.assertThat("Verify Status. Expected: "+expectedStatus+" Actual: "+ actualStatus, actualStatus, is(expectedStatus));
    }
}
