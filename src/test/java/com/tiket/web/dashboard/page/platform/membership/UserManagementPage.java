package com.tiket.web.dashboard.page.platform.membership;

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
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;

public class UserManagementPage<T extends WebDriver> extends BasePage<T> {
    public UserManagementPage(Session<T> session) {
        super(session);
    }

    ILogger logger = BaseTest.mainLogger.get();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

    private final String URL_USER_MANAGEMENT = "/admin/member/v2/user-list";
    private final Locator USER_MANAGEMENT_LINK = new Locator("USER_MANAGEMENT_LINK", By.xpath("//a[text()='USER MANAGEMENT']"));
    private final Locator PAGE_TITLE = new Locator("PAGE_TITLE", By.xpath("//span[contains(@class, 'Text_variant_highEmphasis')]"));
    private final Locator USER_TABLE = new Locator("USER_TABLE", By.xpath("//table[contains(@class, 'TabUserDasboard_styles_table')]"));
    private final Locator ADD_NEW_USER_BTN = new Locator("ADD_USER_BTN", By.xpath("//button[text()='Add New User']"));
    private final Locator ADD_USER_CONTAINER = new Locator("ADD_USER_CONTAINER", By.xpath("//div[contains(@class, 'styles_add_user_dashboard_page')]/div[contains(@class, 'Card_card_container')]"));
    private final Locator USERNAME_FIELD = new Locator("USERNAME_FIELD", By.id("username"));
    private final Locator SEARCH_FIELD = new Locator("SEARCH_FIELD", By.xpath("//input[@data-testid='search-keyword']"));
    private final Locator FNAME_FIELD = new Locator("FNAME_FIELD", By.id("first-name"));
    private final Locator LNAME_FIELD = new Locator("LNAME_FIELD", By.id("last-name"));
    private final Locator CATEGORIES_CONTAINER = new Locator("CATEGORIES_CONTAINER", By.id("sb-text-input-Categories"));
    private final Locator SEARCH_BY_CONTAINER = new Locator("SEARCH_BY_CONTAINER", By.xpath("//div[contains(@class, 'styles_option_container')]"));
    private final Locator CATEGORIES_LIST = new Locator("CATEGORIES_LIST", By.cssSelector(".list__menu.css-26l3qy-menu"));
    private final Locator ADD_USER_BTN = new Locator("ADD_USER_BTN", By.xpath("//button[text()='Add User']"));
    private final Locator SNACKBAR_SUCCESS = new Locator("SNACKBAR_SUCCESS", By.xpath("//div[contains(@class, 'SnackbarsPositioner_snackbars_positioner')]//div[contains(@class, 'Snackbar_type_general')]"));
    private final Locator SEARCH_BY_DROPDOWN = new Locator("SEARCH_BY_DROPDOWN", By.id("sb-text-input-Select Filter Type"));
    private final Locator TABLE_HEADER = new Locator("TABLE_HEADER", By.xpath("//table[contains(@class, 'TabUserDasboard')]/thead/tr/th"));
    private final Locator TABLE_ROW = new Locator("TABLE_ROW", By.xpath("//table[contains(@class, 'TabUserDasboard')]/tbody/tr"));
    private final Locator SUSPEND_BUTTON = new Locator("SUSPEND_BUTTON", By.xpath("//span[@data-testid='action-Deactivate']"));
    private final Locator REACTIVATE_BTN = new Locator("REACTIVATE_BTN", By.xpath("//span[@data-testid='action-Reactivate']"));
    private final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", By.xpath("//span[contains(@class, 'TabUserDasboard_styles_button_action') and normalize-space() = 'Delete']"));
    private final Locator CONFIRMATION_MODAL = new Locator("SUSPEND_CONFIRMATION_MODAL", By.xpath("//section[contains(@class, 'Modal_modal_popup')]"));
    private final Locator YES_BUTTON = new Locator("YES_BUTTON", By.xpath("//button[.='Yes']"));
    private final Locator PASSWORD_CONFIRMATION = new Locator("PASSWORD_CONFIRMATION", By.xpath("//input[@id='password']"));
    private final Locator PASSWORD_CONFIRMATION_BUTTON = new Locator("PASSWORD_CONFIRMATION_BUTTON", By.xpath("//button[contains(@class, 'Button_variant_primary') and normalize-space()='Confirm']"));
    private final Locator SEARCH_BY_DROP_2 = new Locator("SEARCH_BY_DROP_2", By.className("css-g1d714-ValueContainer"));
    private final Locator CHANGE_PASSWORD_LINK = new Locator("CHANGE_PASSWORD_LINK", By.xpath("//a[text()='Change Password']"));
    private final Locator CHANGE_PASSWORD_CONTAINER = new Locator("CHANGE_PASSWORD_CONTAINER", By.cssSelector(".landing-desktop.change-password-container"));
    private final Locator CHANGE_TYPE_DIV = new Locator("CHANGE_TYPE_DIV", By.cssSelector(".list__single-value.css-1uccc91-singleValue"));
    private final Locator CHANGE_PW_BTN = new Locator("CHANGE_PW_BTN", By.xpath("//button[text()='Change Password']"));
    private final Locator PASSWORD_INPUT = new Locator("PASSWORD_INPUT", By.name("password"));
    private final Locator CONFIRM_PASSWORD_INPUT = new Locator("CONFIRM_PASSWORD_INPUT", By.name("confirmPassword"));
    private final Locator INHOUSE_CATEGORY = new Locator("INHOUSE_CATEGORY", By.xpath("(//div[@data-testid='option-box'])[1]"));
    private final Locator OUTSOURCE_CATEGORY = new Locator("OUTSOURCE_CATEGORY", By.xpath("(//div[@data-testid='option-box'])[2]"));
    private final Locator SEARCH_BY_USERID = new Locator("SEARCH_BY_USERID", By.xpath("//div[@data-testid = 'option-box' and contains(., 'User ID')]"));
    private final Locator SEARCH_BY_EMAIL = new Locator("SEARCH_BY_EMAIL", By.xpath("//div[@data-testid = 'option-box' and contains(., 'User Name')]"));
    private final Locator CHANGE_BY_AUTOMATIC = new Locator("CHANGE_BY_AUTOMATIC", By.id("react-select-select-password-option-0"));
    private final Locator CHANGE_BY_MANUAL = new Locator("CHANGE_BY_MANUAL", By.id("react-select-select-password-option-1"));
    private final Locator MANAGE_ROLE = new Locator("MANAGE_ROLE", By.xpath("//span[text()='Manage Role']/parent::a"));
    private final Locator ROLE_PRIV_FORM = new Locator("ROLE_PRIV_FORM", By.xpath("//div[contains(@class, 'styles_add_user_dashboard_page')]"));
    private final Locator GROUP_NAME = new Locator("GROUP_NAME", By.name("sb-text-input-Group"));
    private final Locator GROUP_ELEMENT = new Locator("GROUP_ELEMENT", By.xpath("//input[@name='sb-text-input-Group']/parent::div"));
    private final Locator GROUP_LIST = new Locator("GROUP_LIST", By.xpath("//div[contains(@class, 'styles_option_container')]"));
    private final Locator ROLE_AREA = new Locator("ROLE_AREA", By.xpath("(//div[contains(@class, 'input_select__control')])[1]"));
    private final Locator PRIV_AREA = new Locator("PRIV_AREA", By.xpath("(//div[contains(@class, 'input_select__control')])[2]"));
    private final Locator ROLE_FIELD = new Locator("ROLE_FIELD", By.id("react-select-2-input"));
    private final Locator PRIV_FIELD = new Locator("PRIV_FIELD", By.id("react-select-3-input"));
    private final Locator ROLE_NAME_CONTAINER = new Locator("ROLE_NAME_CONTAINER", By.xpath("..//div"));
    private final Locator X_BUTTON_ROLE_PRIV = new Locator("X_BUTTON_ROLE_PRIV", By.xpath("following-sibling::div"));
    private final Locator ROLE_PRIV_LIST = new Locator("ROLE_PRIV_LIST", By.cssSelector(".css-26l3qy-menu"));
    private final Locator ROLE_PRIV_LIST_CONTAINER = new Locator("ROLE_PRIV_LIST_CONTAINER", By.cssSelector(".css-11unzgr"));
    private final Locator UPDATE_ROLE_PRIV_BTN = new Locator("UPDATE_ROLE_PRIV_BTN", By.xpath("//button[normalize-space()='Update Roles & Privileges']"));

    private WebElement getGroupName(String group) {
        final Locator GROUP_NAME = new Locator(group.toUpperCase(), By.xpath("//div[normalize-space(text())='" + group.toLowerCase() + "']"));
        return driverTools.element(GROUP_NAME, driver.findElement(GROUP_LIST.by())).orElseThrow();
    }

    public void goToUserManagementPage() {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(sheetData.get("username"), sheetData.get("password"));
        memberLoginPage.goToPage(URL_USER_MANAGEMENT);
        driverTools.waitForElementToAppear(ADD_USER_CONTAINER, 3);
        if (!driverTools.getCurrentURL().contains(URL_USER_MANAGEMENT) && !driverTools.isElementDisplayed(ADD_USER_CONTAINER)) {
            memberLoginPage.goToPage(URL_USER_MANAGEMENT);
        }
        driverTools.waitForElementToAppear(ADD_USER_CONTAINER, 3);
        Assertion.assertThat("Verify URL is correct", driverTools.getCurrentURL(), is(containsStringIgnoringCase(URL_USER_MANAGEMENT)));
        Assertion.assertThat("Verify page title is: " + sheetData.get("titlePage"), driverTools.getText(PAGE_TITLE).orElse(""), is(sheetData.get("titlePage")));
        Assertion.assertThat("Verify user list table is present", driverTools.isElementDisplayed(USER_TABLE), is(true));
    }

    public void clickAddNewUser() {
        driverTools.click(ADD_NEW_USER_BTN);
        driverTools.waitForElementToAppear(ADD_USER_CONTAINER);
        driverTools.waitForElementToAppear(USERNAME_FIELD);
        driverTools.waitForElementToAppear(FNAME_FIELD);
        driverTools.waitForElementToAppear(LNAME_FIELD);
        driverTools.waitForElementToAppear(CATEGORIES_CONTAINER);

        Assertion.assertThat("Verify add new user forms are present", driverTools.isElementDisplayed(ADD_USER_CONTAINER), is(true));
        Assertion.assertThat("Verify first name field is present", driverTools.isElementDisplayed(ADD_USER_CONTAINER), is(true));
        Assertion.assertThat("Verify last name field is present", driverTools.isElementDisplayed(ADD_USER_CONTAINER), is(true));
        Assertion.assertThat("Verify categories field is present", driverTools.isElementDisplayed(ADD_USER_CONTAINER), is(true));
    }

    private void createDummyUser() {
        final String name = CoreFunctions.randomNameWithoutTest();
        final String email = sheetData.get("adminEmail") + name + "@tiket.com";
        sheetData.put("adminEmail", email.toLowerCase());
        sheetData.put("adminFName", name);
        sheetData.put("adminLName", CoreFunctions.randomLastName());
    }

    public void inputAllAdminFields() {
        createDummyUser();
        driverTools.sendKeys(USERNAME_FIELD, sheetData.get("adminEmail"));
        driverTools.sendKeys(FNAME_FIELD, sheetData.get("adminFName"));
        driverTools.sendKeys(LNAME_FIELD, sheetData.get("adminLName"));
        setUserCategories(sheetData.get("category"));
        driverTools.click(ADD_USER_BTN);
    }

    public void verifySuccessSnackBar() {
        driverTools.waitForElementToAppear(SNACKBAR_SUCCESS);
        logger.log("snackBar element = " + driverTools.getAttribute(SNACKBAR_SUCCESS.by(), "outerHTML"));
        logger.log("inner snackBar element = " + driverTools.getAttribute(SNACKBAR_SUCCESS.by(), "innerHTML"));
        final boolean isSuccessSnackbarAppeared = driverTools.isElementDisplayed(SNACKBAR_SUCCESS);
        final String actualSuccessMsg = driverTools.getText(SNACKBAR_SUCCESS).orElse("");
        driverTools.waitForElementToDisappear(SNACKBAR_SUCCESS);

        Assertion.assertThat("Verify success Snack Bar is present", isSuccessSnackbarAppeared, is(true));
        Assertion.assertThat("Verify snack Bar text is correct", actualSuccessMsg, is(sheetData.get("successMsg")));
    }

    public void verifyNewAdminData() throws InterruptedException {
        if (!driverTools.getText(PAGE_TITLE).orElse("").equals(sheetData.get("titlePage"))) {
            driverTools.click(USER_MANAGEMENT_LINK);
        }

        searchAdminByEmail(sheetData.get("adminEmail"));

        Map<String, String> data = getDataAfterSearch();
        Assertion.assertThat("Verify admin user id is created", data.get("User ID"), is(notNullValue()));
        Assertion.assertThat("Verify admin email is correct", data.get("User Name"), is(sheetData.get("adminEmail")));
        Assertion.assertThat("Verify admin type is correct", data.get("Admin Type"), is(sheetData.get("category").equalsIgnoreCase("inhouse") ? "INTERNAL" : "EXTERNAL"));
        Assertion.assertThat("Verify admin categories is correct", data.get("Categories").toLowerCase(), is(sheetData.get("category")));
        Assertion.assertThat("Verify admin status is active", data.get("User Status").toLowerCase(), is("active"));
    }

    private Map<String, String> getDataAfterSearch() {
        driverTools.waitForElementToAppear(TABLE_ROW);
        Map<String, String> result = new HashMap<>();
        List<WebElement> headers = driverTools.elements(TABLE_HEADER).orElseThrow();
        List<WebElement> rows = driverTools.elements(TABLE_ROW).orElseThrow();

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.xpath("./td"));
            for (int i = 0; i < cells.size(); i++) {
                String headerText = driverTools.getText(headers.get(i));
                String cellText = driverTools.getText(cells.get(i)).trim();
                result.put(headerText, cellText.isEmpty() ? "" : cellText);
            }
        }

        return result;
    }

    private void searchAdminByEmail(String email) throws InterruptedException {
        driverTools.click(SEARCH_BY_DROPDOWN);
        driverTools.waitForElementToAppear(SEARCH_BY_EMAIL, 2).orElseThrow();
        driverTools.click(SEARCH_BY_EMAIL);
        driverTools.sendKeys(SEARCH_FIELD, email);
        driverTools.sendKeys(SEARCH_FIELD, String.valueOf(Keys.RETURN));
        Thread.sleep(1000);
    }

    public void deleteNewAdmin(String email) throws InterruptedException {
        List<WebElement> tableResult = driverTools.elements(TABLE_ROW).orElseThrow();
        if (tableResult.size() != 1) {
            searchAdminByEmail(email);
        }

        suspendUser();
        driverTools.waitForElementToAppear(SNACKBAR_SUCCESS);
        driverTools.waitForElementToDisappear(SNACKBAR_SUCCESS);
        logger.log("wait until delete button active");
        try {
            wait.until((ExpectedCondition<Boolean>) driver -> {
                String classes = driverTools.getValueAttribute(DELETE_BUTTON, "class");
                return !classes.contains("disabled");
            });
        } catch (TimeoutException e) {
            logger.log("Delete button still disabled after wait, refreshing page...");
            driverTools.refreshWebpage();
            searchAdminByEmail(email);
            new WebDriverWait(driver, Duration.ofSeconds(10)).until((ExpectedCondition<Boolean>) driver -> {
                String classes = driverTools.getValueAttribute(DELETE_BUTTON, "class");
                return !classes.contains("disabled");
            });
        }
        logger.log("end waiting");

        driverTools.click(DELETE_BUTTON);
        driverTools.waitForElementToPresence(CONFIRMATION_MODAL);
        final WebElement yesButton = driverTools.element(YES_BUTTON, driver.findElement(CONFIRMATION_MODAL.by())).orElseThrow();
        driverTools.clickWithJavascriptExecutor(yesButton);
        confirmPassword(sheetData.get("password"));
        driverTools.waitForElementToAppear(SNACKBAR_SUCCESS);
        driverTools.waitForElementToDisappear(SNACKBAR_SUCCESS);
        tableResult = driverTools.elements(TABLE_ROW).orElse(new ArrayList<>());
        Assertion.assertThat("Verify result is 0", tableResult.size(), is(0));
    }

    private void suspendUser() {
        driverTools.click(SUSPEND_BUTTON);
        driverTools.waitForElementToPresence(CONFIRMATION_MODAL);
        final WebElement yesButton = driverTools.element(YES_BUTTON, driver.findElement(CONFIRMATION_MODAL.by())).orElseThrow();
        driverTools.clickWithJavascriptExecutor(yesButton);
        confirmPassword(sheetData.get("password"));
    }

    private void reactivateUser() {
        driverTools.click(REACTIVATE_BTN);
        driverTools.waitForElementsToAppear(CONFIRMATION_MODAL).orElseThrow();
        final WebElement yesButton = driverTools.element(YES_BUTTON, driver.findElement(CONFIRMATION_MODAL.by())).orElseThrow();
        driverTools.clickWithJavascriptExecutor(yesButton);
        confirmPassword(sheetData.get("password"));
    }

    public void searchUserByType(String type, String value) {
        driverTools.waitForElementToAppear(SEARCH_BY_DROPDOWN, 1);
        String currentSearchBy = driverTools.getValue(SEARCH_BY_DROPDOWN).replaceAll(" ", "").toLowerCase();
        if (currentSearchBy.equalsIgnoreCase("User Name")) {
            currentSearchBy = "email";
        }
        if (!currentSearchBy.equalsIgnoreCase(type)) {
            driverTools.click(SEARCH_BY_DROPDOWN);
            List<WebElement> elements = driverTools.elements(SEARCH_BY_EMAIL).orElseThrow();
            logger.log("elements = " + elements.size() + "\n" + elements);
            driverTools.waitForElementToAppear(SEARCH_BY_EMAIL, 3).orElseThrow();
            if (type.equalsIgnoreCase("userid")) {
                sheetData.put("searchBy", "userid");
                driverTools.click(SEARCH_BY_USERID);
            } else if (type.equalsIgnoreCase("email")) {
                sheetData.put("searchBy", "email");
                driverTools.click(SEARCH_BY_EMAIL);
            } else {
                Assertion.fail("search by " + type + " is not yet available");
            }
        } else {
            sheetData.put("searchBy", "userid");
        }

        List<WebElement> totalRows = driverTools.elements(TABLE_ROW).orElseThrow();
        driverTools.deleteUsingBackspace(SEARCH_FIELD);
        driverTools.sendKeys(SEARCH_FIELD, value + Keys.RETURN);
        wait.pollingEvery(Duration.ofSeconds(1)).until(driver -> {
            List<WebElement> newRows = driverTools.elements(TABLE_ROW).orElse(new ArrayList<>());
            return newRows.size() < totalRows.size();
        });
    }

    public void verifyAfterSearchUser() {
        List<Map<String, String>> result = new ArrayList<>();

        List<WebElement> headers = driverTools.elements(TABLE_HEADER).orElseThrow();
        List<WebElement> rows = driverTools.elements(TABLE_ROW).orElseThrow();

        if (rows.size() > 2) {
            Assertion.fail("Search result should only return 1 or 2 rows, but got " + rows.size());
        }

        for (WebElement row : rows) {
            Map<String, String> cellData = new HashMap<>();
            List<WebElement> cells = row.findElements(By.xpath("./td"));

            for (int i = 0; i < cells.size(); i++) {
                String data = driverTools.getText(cells.get(i));
                String header = driverTools.getText(headers.get(i));
                cellData.put(header, data.isEmpty() ? "" : data);
            }
            result.add(cellData);
        }

        if (rows.size() == 2) {
            int roleSize1 = result.get(0).get("Role Name").split(",").length;
            int roleSize2 = result.get(1).get("Role Name").split(",").length;

            Assertion.assertThat("Verify that userId in row 1 & 2 are the same", result.get(0).get("User ID"), is(result.get(1).get("User ID")));
            Assertion.assertThat("Verify that Email in row 1 & 2 are the same", result.get(0).get("User Name"), is(result.get(1).get("User Name")));
            Assertion.assertThat("Verify that total Role in row 1 & 2 are the same", roleSize1, is(roleSize2));
        }

        if (sheetData.get("searchBy").equals("userid")) {
            Assertion.assertThat("Verify user id is as requested", result.get(0).get("User ID"), is(sheetData.get("userId")));
        } else {
            Assertion.assertThat("Verify user id is present", result.get(0).get("User ID"), is(notNullValue()));
        }

        if (sheetData.get("searchBy").equals("email")) {
            Assertion.assertThat("Verify Email is as requested", result.get(0).get("User Name"), is(sheetData.get("email")));
        } else {
            Assertion.assertThat("Verify Email is present", result.get(0).get("User Name"), is(notNullValue()));
        }
    }

    public void clickChangePassword() {
        driverTools.click(CHANGE_PASSWORD_LINK);
        driverTools.waitForElementToAppear(CHANGE_PASSWORD_CONTAINER);
    }

    public void chooseChangePasswordBy(String by) {
        final String type = driverTools.getText(CHANGE_TYPE_DIV).orElse("");
        if (!type.equalsIgnoreCase(by)) {
            driverTools.click(CHANGE_TYPE_DIV);
            driverTools.waitForElementToAppear(CATEGORIES_LIST);

            if (by.equalsIgnoreCase("automatic")) {
                driverTools.click(CHANGE_BY_AUTOMATIC);
            } else if (by.equalsIgnoreCase("manual")) {
                driverTools.click(CHANGE_BY_MANUAL);
            } else {
                Assertion.fail("change password by " + by + " is not yet available");
            }

        }
        if (by.equalsIgnoreCase("manual")) {
            driverTools.waitForElementToAppear(PASSWORD_INPUT);
        } else {
            driverTools.click(CHANGE_PW_BTN);
        }
    }

    public void verifySuccessChangePassword() {
        driverTools.waitForElementToAppear(SNACKBAR_SUCCESS);
        final boolean isSuccessSnackbarAppeared = driverTools.isElementDisplayed(SNACKBAR_SUCCESS);
        final String actualSuccessMsg = driverTools.getText(SNACKBAR_SUCCESS).orElse("");
        driverTools.waitForElementToDisappear(SNACKBAR_SUCCESS);

        Assertion.assertThat("Verify success snackbar is appeared", isSuccessSnackbarAppeared, is(true));
        Assertion.assertThat("Verify success message is correct", actualSuccessMsg, is(sheetData.get("successMsg")));
    }

    public void adminSuspendUser() {
        String suspendBtnTxt = driverTools.getText(SUSPEND_BUTTON).orElse("");
        if (suspendBtnTxt.isEmpty()) {
            suspendBtnTxt = driverTools.getText(REACTIVATE_BTN).orElseThrow();
        }
        if (suspendBtnTxt.equalsIgnoreCase("reactivate")) {
            logger.log("user is already deactivated. reactivating...");
            reactivateUser();
            driverTools.waitForElementToAppear(SNACKBAR_SUCCESS);
            driverTools.waitForElementToDisappear(SNACKBAR_SUCCESS);
            logger.log("done reactivating.");
        }
        wait.until(driver -> !driverTools.getText(SUSPEND_BUTTON).orElse("").equalsIgnoreCase("unsuspend"));
        logger.log("Deactivating...");
        suspendUser();
        logger.log("Deactivated!");
    }

    public void verifySuccessSuspend() {
        driverTools.waitForElementToAppear(SNACKBAR_SUCCESS);
        final boolean snackBarAppeared = driverTools.isElementDisplayed(SNACKBAR_SUCCESS);
        final String actualSuccessMsg = driverTools.getText(SNACKBAR_SUCCESS).orElseThrow();
        driverTools.waitForElementToDisappear(SNACKBAR_SUCCESS);

        Assertion.assertThat("Verify success snackbar appears", snackBarAppeared, is(true));
        Assertion.assertThat("Verify success message is correct", actualSuccessMsg, is(sheetData.get("successMsg")));
    }

    public void clickManageRole() {
        driverTools.click(MANAGE_ROLE);
    }

    public void updateRolePriv(String[] roles, String[] privs) throws InterruptedException {
        driverTools.waitForElementToAppear(ROLE_PRIV_FORM, 3);
        final String group = driverTools.getValue(GROUP_NAME);
        final String category = driverTools.getValue(CATEGORIES_CONTAINER);

        if (group.isEmpty()) {
            setGroup("administrators");
        }

        if (category.isEmpty()) {
            setUserCategories("inhouse");
        }

        for (String role : roles) {
            checkAndDeleteExistingRole(role);
        }

        for (String priv : privs) {
            checkAndDeleteExistingPriv(priv);
        }

        addRoles(roles);
        addPrivs(privs);

        scrollElementToCenter(UPDATE_ROLE_PRIV_BTN);
        driverTools.click(UPDATE_ROLE_PRIV_BTN);
        confirmPassword(sheetData.get("password"));
    }

    private void addRoles(String[] roles) {
        logger.log("Adding '" + roles.length + "' roles...");
        WebElement roleField = driverTools.element(ROLE_FIELD, driver.findElement(ROLE_AREA.by())).orElseThrow();
        driverTools.scrollToElement(roleField);
        driverTools.clickWithJavascriptExecutor(roleField);
        for (String role : roles) {
            driverTools.sendKeys(roleField, role + Keys.RETURN);
        }
        driverTools.sendKeys(roleField, String.valueOf(Keys.ESCAPE));
        dummyWait(3);
    }

    public void verifySuccessUpdateRolePriv() {
        driverTools.waitForElementToAppear(SNACKBAR_SUCCESS);
        final boolean snackBarAppeared = driverTools.isElementDisplayed(SNACKBAR_SUCCESS);
        final String actualSuccessMsg = driverTools.getText(SNACKBAR_SUCCESS).orElseThrow();
        driverTools.waitForElementToDisappear(SNACKBAR_SUCCESS);

        Assertion.assertThat("Verify success snackbar appears", snackBarAppeared, is(true));
        Assertion.assertThat("Verify success message is correct", actualSuccessMsg, is(sheetData.get("successMsg")));
    }

    private void addPrivs(String[] privs) {
        logger.log("Adding '" + privs.length + "' privileges...");
        WebElement privField = driverTools.element(PRIV_FIELD, driver.findElement(PRIV_AREA.by())).orElseThrow();
        scrollElementToCenter(privField);

        for (String priv : privs) {
            driverTools.sendKeys(privField, priv + Keys.RETURN);
        }
        driverTools.sendKeys(privField, String.valueOf(Keys.ESCAPE));
        dummyWait(3);
    }

    private void checkAndDeleteExistingRole(String role) {
        final Locator roleLoc = new Locator(role.toUpperCase() + "_LOC", By.xpath("//div[@aria-label='Remove " + role + "']"));
        WebElement roleElement = null;
        boolean isRoleExist = false;
        try {
            roleElement = driverTools.element(roleLoc, driver.findElement(ROLE_AREA.by())).orElseThrow();
            isRoleExist = driverTools.isElementDisplayed(roleElement);
        } catch (NoSuchElementException ignored) {
        }

        if (isRoleExist) {
            driverTools.click(roleElement);
        } else {
            logger.log(role + " does not exist");
        }
    }

    private void checkAndDeleteExistingPriv(String priv) {
        final Locator privLoc = new Locator(priv.toUpperCase() + "_LOC", By.xpath("//div[@aria-label='Remove " + priv + "']"));
        WebElement privElement = null;
        boolean isPrivExist = false;
        try {
            privElement = driverTools.element(privLoc, driver.findElement(PRIV_AREA.by())).orElseThrow();
            isPrivExist = driverTools.isElementDisplayed(privElement);
        } catch (NoSuchElementException ignored) {
        }

        if (isPrivExist) {
            driverTools.click(privElement);
        } else {
            logger.log(priv + " does not exist");
        }
    }

    private void setGroup(String group) {
        driverTools.click(GROUP_ELEMENT);
        driverTools.waitForElementToAppear(GROUP_LIST);
        driverTools.click(getGroupName(group));
    }

    private void setUserCategories(String category) {
        driverTools.click(CATEGORIES_CONTAINER);
        driverTools.waitForElementToAppear(INHOUSE_CATEGORY, 1);
        if (category.equalsIgnoreCase("inhouse")) {
            driverTools.click(INHOUSE_CATEGORY);
            driverTools.waitForElementToDisappear(INHOUSE_CATEGORY);
        } else if (category.equalsIgnoreCase("outsource")) {
            driverTools.click(OUTSOURCE_CATEGORY);
            driverTools.waitForElementToDisappear(OUTSOURCE_CATEGORY);
        } else {
            Assertion.fail(category + " is not yet supported");
        }
    }

    private void typingOnKeyboard(String text, WebElement element) {
        element.click();
        logger.log("Typing '" + text + "' into '" + element + "'...");
        Actions actions = new Actions(driver);
        for (char c : text.toCharArray()) {
            actions.sendKeys(String.valueOf(c)).perform();
        }
        logger.log("Typed '" + text + "' into '" + element + "' successfully.");
        dummyWait(2);
    }

    private void scrollElementToCenter(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        dummyWait(1);
    }

    private void scrollElementToCenter(Locator locator) {
        WebElement elem = driverTools.element(locator).orElseThrow();
        scrollElementToCenter(elem);
    }

    private void dummyWait(int seconds) {
        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            wait2.until(driver -> false);
        } catch (TimeoutException ignored) {
        }
    }

    public void confirmPassword(String password) {
        driverTools.waitForElementsToAppear(PASSWORD_CONFIRMATION, 6).orElseThrow();
        driverTools.waitForElementToBeClickable(PASSWORD_CONFIRMATION).orElseThrow();
        driverTools.sendKeys(PASSWORD_CONFIRMATION, password);
        driverTools.click(PASSWORD_CONFIRMATION_BUTTON);
        driverTools.waitForElementToDisappear(PASSWORD_CONFIRMATION_BUTTON);
    }

    public void tryToDeleteAdmin(String email) {
        try {
            BaseTest.mainLogger.get().step("And admin delete the newly-created admin");
            deleteNewAdmin(sheetData.get("adminEmail"));
        } catch (Exception e) {
            BaseTest.mainLogger.get().log("Error deleting admin: " + e.getMessage());
            BaseTest.mainLogger.get()
                    .log("Might wanna delete admin email: '" + sheetData.get("adminEmail") + "' manually.");
        }
    }

    @Setter
    @Getter
    Map<String, String> sheetData = new HashMap<>();
}
