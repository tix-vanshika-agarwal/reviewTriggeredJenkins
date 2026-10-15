package com.tiket.web.dashboard.page.accommodation.internal_supplier.user_management;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class InternalSupplierUserManagementPage<T extends WebDriver> extends WebPage<T> {

    private static final Locator USER_MANAGEMENT_MENU = new Locator("USER_MANAGEMENT_MENU",
            By.xpath("//button[.//span[@data-testid='nav-label' and normalize-space()='User Management']]"));
    private static final Locator USER_MANAGEMENT_PAGE_TITLE = new Locator("USER_MANAGEMENT_PAGE_TITLE",
            By.xpath("//span[@id='title-page-id' and normalize-space()='User Management']"));
    private static final Locator USER_LIST_TABLE = new Locator("USER_LIST_TABLE", By.xpath("//table//tbody"));
    private static final Locator INVITE_USERS_BUTTON = new Locator("INVITE_USERS_BUTTON",
            By.xpath("//button[contains(normalize-space(),'Invite users')]"));
    private static final Locator INVITE_MODAL_TITLE = new Locator("INVITE_MODAL_TITLE",
            By.xpath("//h2[normalize-space()='Invite user']"));
    private static final Locator INVITE_FIRST_NAME_INPUT = new Locator("INVITE_FIRST_NAME_INPUT",
            By.xpath("//input[@data-testid='test-id-form-first-name']"));
    private static final Locator INVITE_LAST_NAME_INPUT = new Locator("INVITE_LAST_NAME_INPUT",
            By.xpath("//input[@data-testid='test-id-form-last-name']"));
    private static final Locator INVITE_EMAIL_INPUT = new Locator("INVITE_EMAIL_INPUT",
            By.xpath("//input[@data-testid='test-id-form-email']"));
    private static final Locator PAGE_ACCESS_DROPDOWN = new Locator("PAGE_ACCESS_DROPDOWN",
            By.xpath("//input[@data-testid='test-id-form-vendor-filter']"));
    private static final Locator INVITE_ROLE_DROPDOWN = new Locator("INVITE_ROLE_DROPDOWN",
            By.xpath("//input[@data-testid='test-role-id-form']"));
    private static final Locator SEND_INVITATION_BUTTON = new Locator("SEND_INVITATION_BUTTON",
            By.xpath("//button[@data-testid='id-fu-button-submit']"));
    private static final Locator CONFIRM_SEND_INVITATION_BUTTON = new Locator("CONFIRM_SEND_INVITATION_BUTTON",
            By.xpath("//button[@data-testid='submit-form-user']"));
    private static final Locator SNACKBAR_MESSAGE = new Locator("SNACKBAR_MESSAGE",
            By.xpath("//div[contains(@class,'Snackbar_text_content')]"));
    private static final Locator CONFIRM_POPUP_BUTTON = new Locator("CONFIRM_POPUP_BUTTON",
            By.xpath("//div[contains(@class,'Dialog_pop_up')]//button[normalize-space()='Continue' or normalize-space()='Confirm' or normalize-space()='Yes' or normalize-space()='Resend invitation' or normalize-space()='Cancel invitation']"));

    public InternalSupplierUserManagementPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> InternalSupplierUserManagementPage<?> getInstance(Session<T> session) {
        return switch (Configs.PLATFORM) {
            case DWEB -> new InternalSupplierUserManagementPageDWeb<>(session);
            case AMWEB, IMWEB, ANDROID, IOS -> new InternalSupplierUserManagementPageMWeb<>(session);
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        };
    }

    public void goToUserManagementPage() {
        driverTools.waitForElementToAppear(USER_MANAGEMENT_MENU, 15).orElseThrow();
        driverTools.waitForElementToBeClickable(USER_MANAGEMENT_MENU, 10).orElseThrow();
        driverTools.clickElementByCoordinateDWeb(USER_MANAGEMENT_MENU);
        driverTools.waitForElementToAppear(USER_MANAGEMENT_PAGE_TITLE, 15);
    }

    public boolean isUserManagementPageLoaded() {
        return driverTools.waitForElementToAppear(USER_MANAGEMENT_PAGE_TITLE, 15).isPresent();
    }

    public boolean isUserListTableDisplayed() {
        return driverTools.waitForElementToAppear(USER_LIST_TABLE, 15).isPresent();
    }

    public void clickInviteUsers() {
        driverTools.waitForElementToBeClickable(INVITE_USERS_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(INVITE_USERS_BUTTON);
        driverTools.waitForElementToAppear(INVITE_MODAL_TITLE, 10);
        driverTools.waitForElementToAppear(INVITE_FIRST_NAME_INPUT, 10);
    }

    /**
     * Fills an invite-modal input. The normal clear (which clicks first) can be intercepted when the modal inputs
     * overlap on smaller CI viewports, so on interception fall back to focusing via JavaScript and clearing with key presses, without a click.
     */
    private void fillInviteInput(Locator locator, String value) {
        driverTools.waitForElementToAppear(locator, 10).orElseThrow();
        driverTools.scrollToElement(locator);
        try {
            driverTools.clearInput(locator);
            driverTools.sendKeys(locator, value);
        } catch (ElementClickInterceptedException e) {
            WebElement input = driverTools.element(locator).orElseThrow();
            // Focus without a click, then clear with real key presses (select-all + delete) so the React
            // onChange fires; setting value through JS would not update React's state.
            driverTools.focusWithJavascriptExecutor(input);
            driverTools.clearInput(input);
            input.sendKeys(value);
        }
    }

    public void fillInviteUserForm(String firstName, String lastName, String email, String pageAccess, String role) {
        fillInviteInput(INVITE_FIRST_NAME_INPUT, firstName);
        fillInviteInput(INVITE_LAST_NAME_INPUT, lastName);
        fillInviteInput(INVITE_EMAIL_INPUT, email);
        selectReadonlyDropdown(PAGE_ACCESS_DROPDOWN, pageAccess);
        selectReadonlyDropdown(INVITE_ROLE_DROPDOWN, role);
    }

    public void submitInviteUserForm() {
        driverTools.waitForElementToBeClickable(SEND_INVITATION_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEND_INVITATION_BUTTON);
        driverTools.waitForElementToBeClickable(CONFIRM_SEND_INVITATION_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CONFIRM_SEND_INVITATION_BUTTON);
        driverTools.waitForElementToDisappear(INVITE_MODAL_TITLE, 15);
    }

    public void filterUserListByAccessTypeAndRole(String accessType, String role) {
        selectFilterRoleDropdown(role);
    }

    public void updateEditUserForm(String firstName, String lastName, String role) {
        driverTools.waitForElementToPresence(INVITE_FIRST_NAME_INPUT);
        driverTools.clearInput(INVITE_FIRST_NAME_INPUT);
        driverTools.sendKeys(INVITE_FIRST_NAME_INPUT, firstName);
        driverTools.clearInput(INVITE_LAST_NAME_INPUT);
        driverTools.sendKeys(INVITE_LAST_NAME_INPUT, lastName);
        selectReadonlyDropdown(INVITE_ROLE_DROPDOWN, role);
    }

    public void submitEditUserForm() {
        submitInviteUserForm();
    }

    public void clickActionOnUser(String userIdentifier) {
        Locator actionButton = new Locator("ACTION_BUTTON_" + userIdentifier, By.xpath(String.format(
                "//tr[contains(., '%s')]//button[.//span[normalize-space()='Action'] or normalize-space()='Action']",
                userIdentifier)));
        driverTools.waitForElementToBeClickable(actionButton, 10).orElseThrow();
        driverTools.scrollElementToCenter(actionButton);
        driverTools.clickWithJavascriptExecutor(actionButton);

    }

    public void clickEditUserFromTableRow(String userIdentifier) {
        openRowActionMenu(userIdentifier);
        clickRowActionMenuItem("Edit");
    }

    public void clickResendInvitationFromTableRow(String userIdentifier) {
        openRowActionMenu(userIdentifier);
        clickRowActionMenuItem("Resend invitation");
    }

    public void clickRemoveUserFromTableRow(String userIdentifier) {
        openRowActionMenu(userIdentifier);
        clickRowActionMenuItem("Cancel invitation");
    }

    private void openRowActionMenu(String userIdentifier) {
        Locator actionTrigger = new Locator("ROW_ACTION_TRIGGER_" + userIdentifier, By.xpath(String.format(
                "//tr[contains(., '%s')]//div[@data-testid='dropdown-action-trigger']",
                userIdentifier)));
        driverTools.waitForElementToBeClickable(actionTrigger, 10).orElseThrow();
        driverTools.scrollElementToCenter(actionTrigger);
        driverTools.clickWithJavascriptExecutor(actionTrigger);
    }

    private void clickRowActionMenuItem(String menuLabel) {
        Locator menuOption = new Locator("ROW_MENU_" + menuLabel.replace(" ", "_"), By.xpath(String.format(
                "//div[@data-testid='dropdown-item'][.//span[normalize-space()='%s']]",
                menuLabel)));
        driverTools.waitForElementToBeClickable(menuOption, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(menuOption);

    }

    public void confirmActionPopup() {
        driverTools.waitForElementToAppear(CONFIRM_POPUP_BUTTON, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToDisappear(CONFIRM_POPUP_BUTTON, 10);

    }

    public void refreshPage(int times) {
        for (int i = 0; i < times; i++) {
            driverTools.refreshWebpage();
        }
        driverTools.waitForElementToAppear(USER_MANAGEMENT_PAGE_TITLE, 15);
    }

    public boolean isUserPresentInTable(String text) {
        Locator userRow = new Locator("USER_ROW_" + text, By.xpath("//tbody//tr[contains(., '" + text + "')]"));
        return driverTools.waitForElementToAppear(userRow, 10).isPresent();
    }

    public boolean isUserNotPresentInTable(String text) {
        Locator userRow = new Locator("USER_ROW_" + text, By.xpath("//tbody//tr[contains(., '" + text + "')]"));
        return driverTools.waitForElementToDisappear(userRow, 10);
    }

    public boolean verifySnackbarMessage(String expectedMessage) {
        driverTools.waitForElementToAppear(SNACKBAR_MESSAGE, 10);
        String actualMessage = driverTools.getText(SNACKBAR_MESSAGE).orElse("");
        logger.log("Snackbar message: " + actualMessage);
        return actualMessage.contains(expectedMessage);
    }

    private void selectFilterRoleDropdown(String role) {
        Locator roleFilterDropdown = filterDropdown("Role");
        WebElement dropdownElement = driverTools.waitForElementToAppear(roleFilterDropdown, 10).orElseThrow();
        driverTools.scrollElementToCenter(dropdownElement);
        openDropdown(dropdownElement, roleFilterDropdown);

        Locator roleOption = filterRoleListOption(role);
        WebElement optionElement = driverTools.waitForElementToAppear(roleOption, 10).orElseThrow();
        driverTools.scrollElementToCenter(optionElement);
        driverTools.clickWithJavascriptExecutor(roleOption);

    }

    private static Locator filterRoleListOption(String role) {
        return new Locator("FILTER_ROLE_OPTION_" + role, By.xpath(String.format(
                "//li[starts-with(@data-testid,'dropdown-test-id')]//div[@data-testid='role-display']//span[normalize-space()='%s']",
                role)));
    }

    private static Locator filterDropdown(String label) {
        return new Locator("FILTER_DROPDOWN_" + label.toUpperCase().replace(" ", "_"), By.xpath(String.format(
                "//label[normalize-space()='%1$s']/preceding-sibling::input"
                        + " | //label[normalize-space()='%1$s']/ancestor::div[contains(@class,'input_inner_wrapper')]//input",
                label)));
    }

    private void selectReadonlyDropdown(Locator dropdown, String option) {
        WebElement dropdownElement = driverTools.waitForElementToAppear(dropdown, 10).orElseThrow();
        driverTools.scrollElementToCenter(dropdownElement);
        openDropdown(dropdownElement, dropdown);

        Locator optionLocator = dropdownListOption(dropdown, option);
        WebElement optionElement = driverTools.waitForElementToAppear(optionLocator, 10).orElseThrow();
        driverTools.scrollElementToCenter(optionElement);
        driverTools.clickWithJavascriptExecutor(optionLocator);


        String selectedValue = driverTools.getValue(dropdown);
        if (selectedValue == null || selectedValue.isBlank()) {
            selectedValue = driverTools.waitForElementToAppear(dropdown, 5).orElseThrow().getAttribute("value");
        }
        logger.log(String.format("Dropdown [%s] value after select: %s (expected: %s)", dropdown.name(), selectedValue, option));
    }

    private void openDropdown(WebElement dropdownElement, Locator dropdown) {
        driverTools.waitForElementToBeClickable(dropdown, 10);
        try {
            WebElement chevron = dropdownElement.findElement(By.xpath(
                    "./ancestor::div[contains(@class,'input_inner_wrapper') or contains(@class,'styles_input_inner_wrapper')]"
                            + "//div[contains(@class,'DropdownFormDisplay_icon_container')]"));
            driverTools.clickWithJavascriptExecutor(chevron);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(dropdown);
        }
    }

    private static Locator dropdownListOption(Locator dropdown, String option) {
        String testIdPrefix = dropdownTestIdPrefix(dropdown.name());
        if (testIdPrefix != null) {
            return new Locator("DROPDOWN_OPTION_" + option, By.xpath(String.format(
                    "//li[starts-with(@data-testid,'%s')]//span[translate(normalize-space(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='%s']",
                    testIdPrefix, option.toLowerCase())));
        }
        String scope = dropdownScope(dropdown);
        return new Locator("DROPDOWN_OPTION_" + option, By.xpath(String.format(
                "%s//li[.//span[normalize-space()='%1$s']] | %s//li[normalize-space()='%1$s']",
                scope, option, scope, option)));
    }

    private static String dropdownScope(Locator dropdown) {
        if ("PAGE_ACCESS_DROPDOWN".equals(dropdown.name()) || "INVITE_ROLE_DROPDOWN".equals(dropdown.name())) {
            return "//div[contains(@class,'Dialog') and .//h2[normalize-space()='Invite user']]";
        }
        return "";
    }

    private static String dropdownTestIdPrefix(String dropdownName) {
        return switch (dropdownName) {
            case "PAGE_ACCESS_DROPDOWN" -> "test-id-form-vendor-filter";
            case "INVITE_ROLE_DROPDOWN" -> "test-role-id-form";
            default -> null;
        };
    }
}
