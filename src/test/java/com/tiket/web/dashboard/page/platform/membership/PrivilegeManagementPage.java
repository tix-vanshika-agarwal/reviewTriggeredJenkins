package com.tiket.web.dashboard.page.platform.membership;

import com.github.javafaker.Faker;
import com.tiket.core.CoreFunctions;
import com.tiket.logging.ILogger;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.hamcrest.Matchers.*;


public class PrivilegeManagementPage<A extends WebDriver> extends BasePage<A> {
    public PrivilegeManagementPage(Session<A> session) {
        super(session);
    }

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    ILogger logger = BaseTest.mainLogger.get();

    @Setter
    @Getter
    Map<String, String> sheetData = new HashMap<>();
    @Getter
    Map<String, String> privData = new HashMap<>();

    private final String PRIVILEGE_MANAGEMENT_PATH = "/admin/member/v2/priv-list?page=1&tab=priv";
    private final String HOME_URL = "/admin/member/v2";
    private final Locator PAGE_TITLE = new Locator("PAGE_TITLE", By.xpath("//span[contains(@class, 'Text_variant_highEmphasis')]"));
    private final Locator TR = new Locator("TR", By.cssSelector("tr.user-item"));
    private final Locator TD = new Locator("TD", By.tagName("td"));
    private final Locator ADD_NEW_PRIV_BTN = new Locator("ADD_NEW_PRIV_BUTTON", By.xpath("//button[normalize-space()='Add New Privilege']"));
    private final Locator ADD_PRIV_FORM = new Locator("ADD_PRIV_FORM", By.xpath("//div[contains(@class, 'styles_set_priv_page')]"));
    private final Locator INPUT_ID = new Locator("INPUT_ID", By.id("id"));
    private final Locator INPUT_NAME = new Locator("INPUT_NAME", By.id("name"));
    private final Locator INPUT_DESCRIPTION = new Locator("INPUT_DESCRIPTION", By.id("description"));
    private final Locator INPUT_URL = new Locator("INPUT_URL", By.id("url"));
    private final Locator PRIV_STATUS = new Locator("PRIV_STATUS", By.id("sb-text-input-Privilege Status"));
    private final Locator PRIV_STATUS_CONTAINER = new Locator("PRIV_STATUS_CONTAINER", By.xpath("//input[@id='sb-text-input-Privilege Status']/parent::div"));
    private final Locator PRIV_STATUS_LIST = new Locator("PRIV_STATUS_LIST", By.xpath("//div[contains(@class, 'styles_option_container')]"));
    private final Locator PRIV_ACTIVE = new Locator("PRIV_ACTIVE", By.xpath("(//div[@data-testid='option-box'])[1]"));
    private final Locator PRIV_INACTIVE = new Locator("PRIV_INACTIVE", By.xpath("(//div[@data-testid='option-box'])[2]"));
    private final Locator ADD_PRIVILEGE_BTN = new Locator("ADD_PRIVILEGE_BTN", By.xpath("//button[normalize-space()='Add Privilege']"));
    private final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//div[contains(@class, 'SnackbarsPositioner_snackbars_positioner')]//div[contains(@class, 'Snackbar_type_general')]"));
    private final Locator SEARCH_BY_TEXT = new Locator("SEARCH_BY_TEXT", By.id("sb-text-input-Select Filter Type"));
    private final Locator SEARCH_BY_CONTAINER = new Locator("SEARCH_BY_CONTAINER", By.xpath("//input[@id='sb-text-input-Select Filter Type']/parent::div"));
    private final Locator SEARCH_BY_LIST = new Locator("SEARCH_BY_LIST", By.xpath("//div[contains(@class, 'styles_option_container')]"));
    private final Locator SEARCH_BY_ID = new Locator("SEARCH_BY_ID", By.xpath("(//div[@data-testid='option-box'])[1]"));
    private final Locator SEARCH_BY_NAME = new Locator("SEARCH_BY_NAME", By.xpath("(//div[@data-testid='option-box'])[2]"));
    private final Locator SEARCH_BOX = new Locator("SEARCH_BOX", By.id("search"));
    private final Locator TR_HEAD = new Locator("TR_HEAD", By.xpath("//thead/tr"));
    private final Locator TH = new Locator("TH", By.tagName("th"));
    private final Locator DELETE_BTN = new Locator("DELETE_BTN", By.xpath("//span[contains(@class, 'Text_variant_activity') and contains(text(), 'Delete')]"));
    private final Locator CONFIRMATION_MODAL = new Locator("CONFIRMATION_MODAL", By.xpath("//section[contains(@class, 'Modal_modal_popup')]"));
    private final Locator YES_BTN = new Locator("YES_BTN", By.xpath("//button[normalize-space()='Yes']"));


    public void goToPrivilegeManagement(String username, String password) {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(sheetData.get("username"), sheetData.get("password"));
        memberLoginPage.goToPage(PRIVILEGE_MANAGEMENT_PATH);

        driverTools.waitForElementToAppear(PAGE_TITLE, 3);
        if (!driver.getCurrentUrl().contains(PRIVILEGE_MANAGEMENT_PATH) || !driverTools.isElementDisplayed(PAGE_TITLE)) {
            memberLoginPage.goToPage(PRIVILEGE_MANAGEMENT_PATH);
        }
    }

    public void verifyPrivilegeManagementPageLoaded() {
        final String url = driver.getCurrentUrl();
        final String pageTitle = driverTools.getText(PAGE_TITLE).orElse("");

        Assertion.assertThat("Verify url is correct", url, containsString(PRIVILEGE_MANAGEMENT_PATH));
        Assertion.assertThat("Verify page title is correct", pageTitle, equalToIgnoringCase(sheetData.get("titlePage")));
    }

    public void clickPrivilegeButton() {
        driverTools.waitForElementToAppear(TR);
        driverTools.click(ADD_NEW_PRIV_BTN);
        driverTools.waitForElementToAppear(ADD_PRIV_FORM);
    }

    public void inputAllFormsAndSubmit() {
        createDummyData();

        driverTools.deleteUsingBackspace(INPUT_ID);
        driverTools.sendKeys(INPUT_ID, sheetData.get("id"));
        driverTools.dummyWait(1);
        driverTools.sendKeys(INPUT_NAME, sheetData.get("name"));
        driverTools.dummyWait(1);
        driverTools.sendKeys(INPUT_DESCRIPTION, sheetData.get("description"));
        driverTools.dummyWait(1);
        driverTools.sendKeys(INPUT_URL, sheetData.get("url").replace("#", "/"));
        driverTools.dummyWait(1);

        final String privStats = driverTools.getText(PRIV_STATUS).orElse("");
        if (!privStats.equalsIgnoreCase(sheetData.get("status"))) {
            driverTools.scrollElementToCenter(PRIV_STATUS_CONTAINER);
            driverTools.click(PRIV_STATUS_CONTAINER);
            driverTools.waitForElementToAppear(PRIV_STATUS_LIST);

            switch (sheetData.get("status").toLowerCase()) {
                case "active" -> {
                    WebElement active = driverTools.element(PRIV_ACTIVE, driver.findElement(PRIV_STATUS_LIST.by())).orElseThrow();
                    driverTools.click(active);
                }
                case "inactive" -> {
                    WebElement inactive = driverTools.element(PRIV_INACTIVE, driver.findElement(PRIV_STATUS_LIST.by())).orElseThrow();
                    driverTools.click(inactive);
                }
                default -> throw new NotImplementedException(sheetData.get("status") + " not implemented yet");
            }
        }

        driverTools.scrollElementToCenter(ADD_PRIVILEGE_BTN);
        driverTools.click(ADD_PRIVILEGE_BTN);
    }

    public void verifySuccessSnackbar() {
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR);
        final String successMsg = driverTools.getText(SUCCESS_SNACKBAR).orElse("");
        final boolean isSnackbarAppeared = driverTools.isElementDisplayed(SUCCESS_SNACKBAR);
        driverTools.waitForElementToDisappear(SUCCESS_SNACKBAR);

        Assertion.assertThat("Verify success snackbar appeared", isSnackbarAppeared, is(true));
        Assertion.assertThat("Verify success snackbar message", successMsg, is(sheetData.get("successMsg")));
    }

    public void verifyPrivDataCreated() {
        driverTools.waitForElementToAppear(TR, 3);

        List<WebElement> rows = driverTools.elements(TR).orElseThrow();
        List<WebElement> td = driverTools.elements(TD, rows.get(0)).orElseThrow();
        final String currentId = driverTools.getText(td.get(0));


        searchPrivBy("ID");
        boolean searchBoxEmpty = driverTools.getValue(SEARCH_BOX).isEmpty();
        if (!searchBoxEmpty) {
            driverTools.sendKeys(SEARCH_BOX, Keys.SHIFT + "a");
            driverTools.sendKeys(SEARCH_BOX, String.valueOf(Keys.BACK_SPACE));
        }
        driverTools.sendKeys(SEARCH_BOX, sheetData.get("id") + Keys.RETURN);
        wait.until(driver -> {
            List<WebElement> currentRows = driverTools.elements(TR).orElseThrow();
            List<WebElement> currentTd = driverTools.elements(TD, currentRows.get(0)).orElseThrow();
            final String afterId = driverTools.getText(currentTd.get(0));
            return !Objects.equals(currentId, afterId);
        });

//        Map<String, String> actualData = new HashMap<>();
        List<WebElement> tableHeads = driverTools.elements(TH, driverTools.element(TR_HEAD).orElseThrow()).orElseThrow();

        rows = driverTools.elements(TR).orElseThrow();
        if (rows.size() > 1) {
            Assertion.fail("Search result by Id cannot be more than 1");
        }
        td = driverTools.elements(TD, rows.get(0)).orElseThrow();
        for (int i = 0; i < td.size(); i++) {
            privData.put(driverTools.getText(tableHeads.get(i)).toLowerCase(), driverTools.getText(td.get(i)));
        }
    }

    public void searchPrivBy(String by) {
        final String currentSearchBy = driverTools.getValue(SEARCH_BY_TEXT);
        if (!currentSearchBy.contains(by)) {
            driverTools.click(SEARCH_BY_CONTAINER);
            driverTools.waitForElementToAppear(SEARCH_BY_LIST);
            switch (by.toLowerCase()) {
                case "id" -> driverTools.click(SEARCH_BY_ID);
                case "name" -> driverTools.click(SEARCH_BY_NAME);
                default -> throw new NotImplementedException(by + " is not implemented yet.");
            }
        } else {
            logger.log("Already Search By: " + by.toUpperCase());
        }

    }

    public void deletePriv() {
        try {
            driverTools.click(DELETE_BTN);
            driverTools.waitForElementToAppear(CONFIRMATION_MODAL);
            driverTools.click(YES_BTN);
            UserManagementPage<?> userManagementPage = new UserManagementPage<>(session);
            userManagementPage.confirmPassword(sheetData.get("password"));
            driverTools.waitForElementToAppear(SUCCESS_SNACKBAR);
            driverTools.waitForElementToDisappear(SUCCESS_SNACKBAR);
        } catch (Exception e) {
            logger.log("Delete privilege error with message: " + e.getMessage());
            logger.log("Might wanna delete privilege id: '" + sheetData.get("id") + "' manually.");
        }
    }


    private void createDummyData() {
        Faker faker = new Faker();
        final long id = CoreFunctions.generateRandomInt(7);
        final String name = sheetData.get("prefix") + CoreFunctions.generateRandomString(10);
        final String description = faker.lorem().sentence(20);
        sheetData.put("id", String.valueOf(id));
        sheetData.put("name", name);
        sheetData.put("description", description);
    }
}
