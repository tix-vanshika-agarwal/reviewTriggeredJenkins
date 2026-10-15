package com.tiket.web.dashboard.page.platform.eiffel.lounge.user_management;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * User Management (lounge, V2) page object.
 * <p>
 * V2 notes: the edit/reset success snackbar is generic (it does not contain the username), so the presence
 * of the success toast is the success signal. The V2 Edit User popup currently exposes no "Reset Password"
 * button (QAAUT-44146), so {@link #clickResetPasswordButton()} will not resolve until that action's V2
 * location is confirmed.
 */
public class UserListPageWeb<T extends WebDriver> extends WebPage<T> {
    public UserListPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> UserListPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new UserListPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new UserListPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator USER_MANAGEMENT_PAGE_TITLE = new Locator("USER_MANAGEMENT_PAGE_TITLE", By.xpath("//h3[normalize-space()='User Management']"));
    private static final Locator ADD_NEW_USER_BUTTON = new Locator("ADD_NEW_USER_BUTTON", By.xpath("//button[normalize-space()='Create User']"));
    private static final Locator USER_SEARCH_BAR = new Locator("USER_SEARCH_BAR", By.cssSelector("input[class*='SearchBox_accessibility_search']"));
    private static final Locator USER_SEARCH_BAR_SEARCH_BUTTON = new Locator("USER_SEARCH_BAR_SEARCH_BUTTON", By.xpath("//button[normalize-space()='Search']"));
    private static final Locator RESET_PASSWORD_SUCCESS_TOAST = new Locator("RESET_PASSWORD_SUCCESS_TOAST", By.xpath("//div[contains(@class,'Snackbar_type_success')]"));
    private static final Locator EDIT_USER_SUCCESS_TOAST = new Locator("EDIT_USER_SUCCESS_TOAST", By.xpath("//div[contains(@class,'Snackbar_type_success')]"));

    private static final Locator CREATE_USER_POPUP = new Locator("CREATE_USER_POPUP", By.cssSelector("section[class*='Modal_modal_popup']"));
    private static final Locator USERNAME_INPUT = new Locator("USERNAME_INPUT", By.id("user-name"));
    private static final Locator ROLE_DROPDOWN = new Locator("ROLE_DROPDOWN", By.cssSelector("section[class*='Modal_modal_popup'] input[placeholder='Role']"));
    private static final Locator LOUNGE_NAME_INPUT = new Locator("LOUNGE_NAME_INPUT", By.cssSelector("section[class*='Modal_modal_popup'] input[placeholder='Search lounge name']"));
    private static final Locator AUTO_GENERATED_PASSWORD = new Locator("AUTO_GENERATED_PASSWORD", By.id("password"));
    private static final Locator CREATE_USER_POPUP_SAVE_BUTTON = new Locator("CREATE_USER_POPUP_SAVE_BUTTON", By.xpath("//section[contains(@class,'Modal_modal_popup')]//button[normalize-space()='Create User']"));
    private static final Locator EDIT_USER_POPUP = new Locator("EDIT_USER_POPUP", By.cssSelector("section[class*='Modal_modal_popup']"));
    private static final Locator USER_STATUS_TOGGLE = new Locator("USER_STATUS_TOGGLE", By.xpath("//p[normalize-space()='User Status']/following-sibling::div[contains(@class,'Toggle_container')]"));
    private static final Locator USER_STATUS_CHECKBOX = new Locator("USER_STATUS_CHECKBOX", By.xpath("//p[normalize-space()='User Status']/following-sibling::div[contains(@class,'Toggle_container')]//input[@type='checkbox']"));
    private static final Locator EDIT_USER_POPUP_SAVE_BUTTON = new Locator("EDIT_USER_POPUP_SAVE_BUTTON", By.xpath("//section[contains(@class,'Modal_modal_popup')]//button[normalize-space()='Update User']"));
    private static final Locator RESET_PASSWORD_BUTTON = new Locator("RESET_PASSWORD_BUTTON", By.xpath("//section[contains(@class,'Modal_modal_popup')]//button[normalize-space()='Reset Password']"));
    private static final Locator RESET_PASSWORD_CONFIRMATION_POPUP = new Locator("RESET_PASSWORD_CONFIRMATION_POPUP", By.cssSelector("section[class*='Modal_modal_popup']"));
    private static final Locator RESET_PASSWORD_CONFIRMATION_POPUP_CONFIRM = new Locator("RESET_PASSWORD_CONFIRMATION_POPUP_CONFIRM", By.xpath("//button[normalize-space()='Reset']"));
    private static final Locator LOADING_OVERLAY = new Locator("LOADING_OVERLAY", By.cssSelector("[data-testid='loading-container']"));

    private void waitForLoadingOverlayToDisappear() {
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 15);
    }

    public boolean isUserManagementPageOpened() {
        driverTools.waitForElementToAppear(USER_MANAGEMENT_PAGE_TITLE, 10);
        boolean isPageOpened = driverTools.isElementDisplayed(USER_MANAGEMENT_PAGE_TITLE);
        if (!isPageOpened) {
            logger.log("User Management page is not opened");
        }
        return isPageOpened;
    }

    public void clickAddNewUserButton() {
        waitForLoadingOverlayToDisappear();
        driverTools.waitForElementToAppear(ADD_NEW_USER_BUTTON, 10);
        driverTools.click(ADD_NEW_USER_BUTTON);
    }

    /**
     * Types the username into the freshly-opened Create User modal. The modal slides up and overshoots
     * (spring) briefly, so we wait for the field to stop moving before typing — otherwise the click lands on
     * stale coordinates and is intercepted by the modal wrapper.
     */
    public void inputUsernameToAddNewUser(String username) {
        driverTools.waitForElementToAppear(CREATE_USER_POPUP);
        driverTools.waitForElementToAppear(USERNAME_INPUT, 10);
        driverTools.waitForElementToSettle(USERNAME_INPUT, 5);
        driverTools.clearInput(USERNAME_INPUT);
        driverTools.sendKeys(USERNAME_INPUT, username);
    }

    /**
     * Selects a Role from the readonly custom dropdown. Uses a real coordinate click on the option so the
     * React selection registers, and retries opening the dropdown because opening it right after typing the
     * username can be swallowed by an async modal re-render and leave the field unselected.
     */
    public void inputRoleToAddNewUser(String role) {
        Locator option = new Locator("ROLE_OPTION_" + role,
                By.xpath("//div[@data-testid='option-box' and normalize-space()='" + role + "']"));
        driverTools.waitForElementToAppear(ROLE_DROPDOWN, 10);
        boolean opened = false;
        for (int attempt = 0; attempt < 3 && !opened; attempt++) {
            driverTools.clickElementByCoordinateDWeb(ROLE_DROPDOWN);
            opened = driverTools.waitForElementToAppear(option, 5).isPresent();
        }
        if (!opened) {
            throw new NoSuchElementException("Role option not found (or dropdown did not open) for role: " + role);
        }
        driverTools.scrollElementToCenter(option);
        driverTools.clickElementByCoordinateDWeb(option);
    }

    public void assignLoungeToAddNewUser(String[] lounges) {
        for (String lounge : lounges) {
            driverTools.clearInput(LOUNGE_NAME_INPUT);
            driverTools.sendKeys(LOUNGE_NAME_INPUT, lounge);
            Locator loc = new Locator("LOUNGE_SELECTION_" + lounge,
                    By.xpath("//label[contains(@class,'Check_check') and contains(.,'" + lounge + "')]"));
            driverTools.click(loc);
        }
    }

    public void clickSaveToAddNewUser() {
        driverTools.click(CREATE_USER_POPUP_SAVE_BUTTON);
        driverTools.dummyWait(2);
    }

    public void searchUser(String userName) {
        waitForLoadingOverlayToDisappear();
        driverTools.clickElementByCoordinateDWeb(USER_SEARCH_BAR);
        driverTools.clearInput(USER_SEARCH_BAR);
        driverTools.sendKeys(USER_SEARCH_BAR, userName);
        driverTools.clickElementByCoordinateDWeb(USER_SEARCH_BAR_SEARCH_BUTTON);
        driverTools.dummyWait(1);
        waitForLoadingOverlayToDisappear();
    }

    public boolean verifyUserExist(String userName) {
        searchUser(userName);

        int userNameIndex = getIndexTableColumn("User Name");

        WebElement e = driverTools.waitForElementToAppear(new Locator(
                "USER_ROW",
                By.xpath("//tr[normalize-space(./td[" + userNameIndex + "]) = '" + userName + "']")
        )).orElse(null);

        return e != null;
    }

    public boolean verifyUserStatus(String userName, String status) {
        searchUser(userName);

        int userNameIndex = getIndexTableColumn("User Name");
        int statusIndex = getIndexTableColumn("Status");

        WebElement e = driverTools.waitForElementToAppear(new Locator(
                "USER_ROW",
                By.xpath("//tr[normalize-space(./td[" + userNameIndex + "]) = '" + userName + "' and normalize-space(./td[" + statusIndex + "]) = '" + status + "']")
        )).orElse(null);

        return e != null;
    }

    /** Success is signalled by the generic V2 success snackbar (which does not contain the username). */
    public boolean verifyEditUserSuccessful() {
        return driverTools.waitForElementToAppear(EDIT_USER_SUCCESS_TOAST, 8).isPresent();
    }

    public void clickEditButtonForUserName(String userName) {
        searchUser(userName);
        int rowIndex = getRowIndexFromUsername(userName);
        driverTools.clickElementByCoordinateDWeb(new Locator("EDIT_BUTTON",
                By.xpath("//tbody/tr[" + rowIndex + "]//button[normalize-space()='Edit']")));
        driverTools.waitForElementToAppear(EDIT_USER_POPUP, 2);
        driverTools.dummyWait(3);
    }

    public void clickResetPasswordButton() {
        driverTools.click(RESET_PASSWORD_BUTTON);
    }

    public void confirmResetPasswordConfirmationPopup() {
        driverTools.waitForElementToAppear(RESET_PASSWORD_CONFIRMATION_POPUP, 2);
        driverTools.click(RESET_PASSWORD_CONFIRMATION_POPUP_CONFIRM);
    }

    /** Success is signalled by the generic V2 success snackbar (which does not contain the username). */
    public boolean verifyResetPasswordSuccess() {
        return driverTools.waitForElementToAppear(RESET_PASSWORD_SUCCESS_TOAST, 8).isPresent();
    }

    /**
     * Flips the User Status toggle, confirming the backing checkbox actually changed — a click on the
     * just-opened modal can be dropped and would otherwise save the unchanged status.
     */
    public void clickStatusToggle() {
        driverTools.toggleUntilStateChanges(USER_STATUS_TOGGLE, USER_STATUS_CHECKBOX, 3);
    }

    public void clickEditUserSaveButton() {
        driverTools.clickWithJavascriptExecutor(EDIT_USER_POPUP_SAVE_BUTTON);
        driverTools.dummyWait(1);
    }

    /**
     * Drives the user to {@code status} ("Active"/"Inactive"), opening edit + toggling + saving only when it
     * differs. Searches first so the grid and its headers are loaded before column/row indices are read (this
     * is also used as a precondition right after navigating, when the grid may not be rendered yet), and
     * waits for the status cell before reading it so a slow list doesn't yield a null/empty value.
     */
    public void changeUserStatus(String username, String status) {
        searchUser(username);

        int statusIndex = getIndexTableColumn("Status");

        int rowIndex = getRowIndexFromUsername(username);

        Locator statusCell = new Locator("USER_STATUS",
                By.xpath("//table/tbody/tr[" + rowIndex + "]/td[" + statusIndex + "]"));
        driverTools.waitForElementToAppear(statusCell, 10);
        String currentStatus = driverTools.getText(statusCell).map(String::trim).orElse(null);

        if (currentStatus == null) throw new NoSuchElementException("There are no status found");

        if (!currentStatus.equalsIgnoreCase(status)) {
            driverTools.clickElementByCoordinateDWeb(new Locator("EDIT_BUTTON",
                    By.xpath("//tbody/tr[" + rowIndex + "]//button[normalize-space()='Edit']")));
            driverTools.waitForElementToAppear(EDIT_USER_POPUP, 5);
            driverTools.waitForElementToAppear(USER_STATUS_TOGGLE, 5);
            driverTools.toggleUntilStateChanges(USER_STATUS_TOGGLE, USER_STATUS_CHECKBOX, 3);
            driverTools.click(EDIT_USER_POPUP_SAVE_BUTTON);
            driverTools.dummyWait(2);
        }
    }

    /** Role is a readonly input whose selected value lives in the {@code value} attribute (getText is empty). */
    public String getUserRole() {
        driverTools.waitForElementToAppear(ROLE_DROPDOWN, 5);
        return driverTools.getAttribute(ROLE_DROPDOWN.by(), "value");
    }

    /**
     * 1-based index of the row whose User Name cell equals {@code username}, matched on the cell's normalized
     * string value so V2's &lt;p&gt;-wrapped cells (and any surrounding whitespace) still match.
     */
    private int getRowIndexFromUsername(String username) {
        int usernameIndex = getIndexTableColumn("User Name");
        List<WebElement> rows = driverTools.getDriver().findElements(
                By.xpath("//tr[normalize-space(./td[" + usernameIndex + "])='" + username + "']/preceding-sibling::tr")
        );
        return rows.size() + 1;
    }

    private int getIndexTableColumn(String columnTitle) {
        List<WebElement> cols = driverTools.getDriver().findElements(
                By.xpath("//th[.='" + columnTitle + "']/preceding-sibling::th")
        );
        return cols.size() + 1;
    }

    public String generateAndInputUsernameWithTimestamp(String baseUsername) {
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String formattedUserName = baseUsername + timestamp + "@yopmail.com";

        inputUsernameToAddNewUser(formattedUserName);
        return formattedUserName;
    }

    public String getAutoGeneratedPassword() {
        driverTools.waitForElementToAppear(AUTO_GENERATED_PASSWORD, 10);
        String password = "";

        try {
            password = driverTools.getDriver().findElement(AUTO_GENERATED_PASSWORD.by()).getAttribute("value");
            if (password != null && !password.isEmpty()) {
                return password;
            }
        } catch (Exception e) {
            logger.log("Failed to get password via attribute method");
        }

        password = driverTools.getText(AUTO_GENERATED_PASSWORD).orElse("");
        return password;
    }

}
