package com.tiket.web.dashboard.page.platform.membership;

import com.mysql.cj.exceptions.AssertionFailedException;
import com.tiket.core.CoreFunctions;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleManagementPage<T extends WebDriver> extends BasePage<T> {
    public RoleManagementPage(Session<T> session) {
        super(session);
    }

    @Setter
    @Getter
    Map<String, String> sheetData;

    private final String ROLE_MANAGEMENT_URL = "/admin/member/v2/role-list?page=1&tab=role";
    private final Locator PAGE_TITLE = new Locator("PAGE_TITLE", By.xpath("//div[contains(@class,'Card_separator')]/span"));
    private final Locator ADD_NEW_ROLE_BUTTON = new Locator("ADD_ROLE_BUTTON", By.xpath("//button[contains(@class, 'Button_variant_primary') and text()='Add New Role']"));
    private final Locator ADD_ROLE_BUTTON = new Locator("ADD_ROLE_BUTTON", By.xpath("//button[contains(@class, 'Button_variant_primary') or text()='Add Role']"));
    private final Locator ID_FIELD = new Locator("ID_FIELD", By.id("id"));
    private final Locator NAME_FIELD = new Locator("NAME_FIELD", By.id("name"));
    private final Locator DESCRIPTION_FIELD = new Locator("DESCRIPTION_FIELD", By.id("description"));
    private final Locator SNACKBAR_SUCCESS = new Locator("SNACKBAR_SUCCESS", By.xpath("//div[contains(@class,'Snackbar_text_content')]"));
    private final Locator SEARCH_FIELD = new Locator("SEARCH_FIELD", By.id("search"));
    private final Locator TABLE_BODY_LIST = new Locator("TABLE_BODY_LIST", By.className("user-item"));
    private final Locator RESULT_ELEMENT = new Locator("RESULT_ELEMENT", By.tagName("td"));
    private final Locator DELETE_ROLE_ELEMENT = new Locator("DELETE_ROLE_ELEMENT", By.xpath("//span[contains(@class,'Text_variant_activity') and text()='Delete']"));
    private final Locator CONFIRMATION_POP_UP = new Locator("CONFIRMATION_POP_UP", By.xpath("//section[contains(@class,'Modal_modal_popup')]"));
    private final Locator CONFIRMATION_YES_BUTTON = new Locator("CONFIRMATION_YES_BUTTON", By.xpath("//button[contains(@class,'Button_variant_primary') and text()='Yes']"));

    public void goToRoleManagementPage(String username, String password) {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.goToPage(ROLE_MANAGEMENT_URL);

        driverTools.waitForElementToAppear(PAGE_TITLE, 3);
        if (!driver.getCurrentUrl().contains(ROLE_MANAGEMENT_URL) || !driverTools.isElementDisplayed(PAGE_TITLE)) {
            memberLoginPage.goToPage(ROLE_MANAGEMENT_URL);
        }

        logger.log("Verifying page...");
        final String pageTitle = driverTools.getText(PAGE_TITLE).orElseThrow(() -> new RuntimeException("Page title not found or unable to find the element \"" + PAGE_TITLE.by()));
        if (!pageTitle.equals(sheetData.get("titlePage"))) {
            throw new AssertionFailedException("Page title is not as expected, expected: " + sheetData.get("titlePage") + ", but got: " + pageTitle);
        } else {
            logger.log("Verified.");
        }
    }

    public void clickAddNewRoleButton() {
        driverTools.click(ADD_NEW_ROLE_BUTTON);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(driver ->
                driverTools.getText(PAGE_TITLE).map(text -> text.equals(sheetData.get("titleForm"))).orElseThrow(() -> new RuntimeException("Page title not found or unable to find the element: " + PAGE_TITLE.by()))
        );
    }

    public void inputAllFormsAndSubmit() {
        final int id = Math.toIntExact(CoreFunctions.generateRandomInt(8));
        final String name = sheetData.get("prefix") + CoreFunctions.generateRandomString(10);
        final String description = "Created by automation test";
        sheetData.put("id", String.valueOf(id));
        sheetData.put("name", name);
        sheetData.put("description", description);

        driverTools.deleteUsingBackspace(ID_FIELD);
        driverTools.sendKeys(ID_FIELD, String.valueOf(id));
        driverTools.sendKeys(NAME_FIELD, name);
        driverTools.sendKeys(DESCRIPTION_FIELD, description);
        driverTools.click(ADD_ROLE_BUTTON);

        driverTools.waitForElementToAppear(SNACKBAR_SUCCESS);
    }

    public Map<?, ?> getSnackbarData() {
        Map<String, Object> snackbarData = new HashMap<>();
        if (!driverTools.isElementDisplayed(SNACKBAR_SUCCESS)) {
            throw new AssertionFailedException("Snackbar success not displayed");
        } else {
            snackbarData.put("visibility", true);
        }

        final String snackbarText = driverTools.getText(SNACKBAR_SUCCESS).orElse("");
        snackbarData.put("snackbarText", snackbarText);

        driverTools.waitForElementToDisappear(SNACKBAR_SUCCESS);
        return snackbarData;
    }

    public Map<String, String> getCreatedRoleData() {
        Map<String, String> roleData = new HashMap<>();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driverTools.sendKeys(SEARCH_FIELD, sheetData.get("id") + Keys.RETURN);
        wait.until(driver -> driverTools.elements(TABLE_BODY_LIST).orElseThrow().size() == 1);

        List<WebElement> resultElem = driverTools.elements(RESULT_ELEMENT, driverTools.element(TABLE_BODY_LIST).orElseThrow()).orElseThrow();
        if (resultElem.size() < 3) {
            throw new AssertionFailedException("Expected at least 3 elements in resultElem, but found: " + resultElem.size());
        }
        roleData.put("id", String.valueOf(driverTools.getText(resultElem.get(0))));
        roleData.put("name", String.valueOf(driverTools.getText(resultElem.get(1))));
        roleData.put("description", String.valueOf(driverTools.getText(resultElem.get(2))));

        return roleData;
    }

    public void deleteRole() {
        List<WebElement> resultElem = driverTools.elements(RESULT_ELEMENT, driverTools.element(TABLE_BODY_LIST).orElseThrow()).orElseThrow();
        if (!driverTools.getText(resultElem.get(0)).equals(sheetData.get("id"))) {
            driverTools.sendKeys(SEARCH_FIELD, sheetData.get("id") + Keys.RETURN);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(driver -> driverTools.elements(TABLE_BODY_LIST).orElseThrow().size() == 1);
        }

        driverTools.click(DELETE_ROLE_ELEMENT);
        driverTools.waitForElementToAppear(CONFIRMATION_POP_UP);
        driverTools.click(CONFIRMATION_YES_BUTTON);
        driverTools.waitForElementToAppear(SNACKBAR_SUCCESS);
    }
}
