package com.tiket.web.dashboard.page.platform.membership;

import com.google.common.collect.Iterables;
import com.tiket.logging.ILogger;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;

public class TableMemberPage <T extends WebDriver> extends BasePage<T> {
    public TableMemberPage(Session<T> session) {
        super(session);
    }
    ILogger logger = BaseTest.mainLogger.get();

    @Setter @Getter
    Map<String, String> sheetData = new HashMap<>();

    private final String TABLE_MEMBER_PATH = "admin/member/table";
    private final Locator TABLE_MEMBER_CONTAINER = new Locator("TABLE_MEMBER_CONTAINER", By.className("member-list-desktop"));
    private final Locator TITLE_PAGE = new Locator("TITLE_PAGE", By.cssSelector("span.title"));
    private final Locator MENU_TITLE = new Locator("MENU_TITLE", By.cssSelector("span.card-title"));
    private final Locator SEARCH_FIELD = new Locator("SEARCH_FIELD", By.name("search"));
    private final Locator SEARCH_BTN = new Locator("SEARCH_BTN", By.cssSelector("button.btn-field.btn-search.btn.btn-secondary"));
    private final Locator ROW_DATA = new Locator("ROW_DATA", By.cssSelector("tr.user-item"));
    private final Locator TD = new Locator("TD", By.xpath("//td[contains(@class, 'table-body')]"));
    private final Locator REACTIVATE_BTN = new Locator("REACTIVATE_BTN", By.xpath("//button[contains(@class, 'btn-reactivate')]"));
    private final Locator DEACTIVATE_BTN = new Locator("DEACTIVATE_BTN", By.xpath("//button[contains(@class, 'btn-deactivate')]"));
    private final Locator NOTE_INPUT = new Locator("NOTE_INPUT", By.name("note"));
    private final Locator CONFIRMATION_MODAL = new Locator("CONFIRMATION_MODAL", By.cssSelector("div.modal-content.medium.modal-confirmation"));
    private final Locator OK_BTN = new Locator("OK_BTN", By.xpath("//button[normalize-space()='OK']"));
    private final Locator SNACKBAR_SUCCESS = new Locator("SNACKBAR_SUCCESS", By.cssSelector(".snackbar.success"));


    public void goToTableMember(String username, String password) {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.goToPage(TABLE_MEMBER_PATH);

        driverTools.waitForElementToAppear(TABLE_MEMBER_CONTAINER, 3);
        if(!driverTools.getCurrentURL().contains(TABLE_MEMBER_PATH) && !driverTools.isElementDisplayed(TABLE_MEMBER_CONTAINER)) {
            memberLoginPage.goToPage(TABLE_MEMBER_PATH);
        }
    }

    public void verifyTableMemberLoaded() {
        final String url = driverTools.getCurrentURL();
        final String titlePage = driverTools.getText(TITLE_PAGE).orElse("");
        final String menuTitle = driverTools.getText(MENU_TITLE).orElse("");

        Assertion.assertThat("Verify url is containing '" + TABLE_MEMBER_PATH + "'", url, is(containsString(TABLE_MEMBER_PATH)));
        Assertion.assertThat("Verify title page is correct", titlePage, is(sheetData.get("titlePage").replace("#", "/")));
        Assertion.assertThat("Verify menu title is correct", menuTitle, is(sheetData.get("menuTitle")));
    }

    public void searchUserByEmail(String email) {
        driverTools.sendKeys(SEARCH_FIELD, email);
        driverTools.click(SEARCH_BTN);
        driverTools.waitForElementToAppear(ROW_DATA);

        List<WebElement> dataElement = driverTools.elements(TD, driver.findElement(ROW_DATA.by())).orElseThrow();
        if (dataElement.size() > 2) {
            String[] texts = dataElement.get(2).getText().split("\n");
            Assertion.assertThat("Verify that email in the table match with '" + sheetData.get("userEmail") + "'", texts[0], is(sheetData.get("userEmail")));
        } else {
            logger.fail("Expected at least 3 elements in dataElement, but found " + dataElement.size());
            throw new IllegalStateException("Not enough elements in dataElement");
        }
    }

    public void deactivateUser() {
        List<WebElement> dataElement = driverTools.elements(TD, driver.findElement(ROW_DATA.by())).orElseThrow();
        WebElement buttonReactivate = driverTools.element(REACTIVATE_BTN, Iterables.getLast(dataElement)).orElse(null);
        boolean isUserDeactivated = buttonReactivate != null;

        if(isUserDeactivated) {
            logger.log("Account is already deactivated. Reactivating...");
            driverTools.sendKeys(NOTE_INPUT, "Automation - Reactivate");
            driverTools.click(buttonReactivate);
            driverTools.waitForElementToAppear(CONFIRMATION_MODAL);
            driverTools.click(OK_BTN);
            driverTools.waitForElementToAppear(SNACKBAR_SUCCESS);
            driverTools.waitForElementToDisappear(SNACKBAR_SUCCESS);
            logger.log("Done reactivating!");
        }

        WebElement buttonDeactivate = driverTools.element(DEACTIVATE_BTN, Iterables.getLast(dataElement)).orElseThrow();
        driverTools.sendKeys(NOTE_INPUT, "Automation - Deactivate");
        driverTools.click(buttonDeactivate);
        driverTools.waitForElementToAppear(CONFIRMATION_MODAL);
        driverTools.click(OK_BTN);
    }

    public void verifySuccessSnackbar(String message) {
        driverTools.waitForElementToAppear(SNACKBAR_SUCCESS);
        final String successMsg = driverTools.getText(SNACKBAR_SUCCESS).orElse("");
        final boolean snackBarAppeared = driverTools.isElementDisplayed(SNACKBAR_SUCCESS);
        driverTools.waitForElementToDisappear(SNACKBAR_SUCCESS);

        Assertion.assertThat("Verify success snack bar is appeared", snackBarAppeared, is(true));
        Assertion.assertThat("Verify success snack bar message", successMsg, is(message));
    }

    public void verifyUserData() {
        List<WebElement> dataElement = driverTools.elements(TD, driver.findElement(ROW_DATA.by())).orElseThrow();
        String[] texts = dataElement.get(2).getText().split("\n");

        final String email = texts[0];
        final String date = texts[1].split(":")[1];
        final String activity = texts[2].split(":")[1];
        final String doneBy = texts[3].split(":")[1];
        final String detail = texts[4].split(":")[1];


        Assertion.assertThat("Verify email deactivated is '" + sheetData.get("userEmail") + "'", email, is(sheetData.get("userEmail")));
        Assertion.assertThat("Verify date & time deactivation is not null", date, is(notNullValue()));
        Assertion.assertThat("Verify activity is '" + sheetData.get("activity") + "'", activity, is(sheetData.get("activity")));
        Assertion.assertThat("Verify deactivation is done by '" + sheetData.get("username") + "'", doneBy, is(sheetData.get("username")));
        Assertion.assertThat("Verify detail is '" + sheetData.get("note") + "'", detail, is(sheetData.get("note")));
    }
}
