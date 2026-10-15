package com.tiket.web.dashboard.page.pricing.SubsidyBudgeting;

import java.time.Duration;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.*;

import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import static com.tiket.core.CoreFunctions.log;

public class SubsidyBudgetingPage<T extends WebDriver> extends BasePage<T> {
    public SubsidyBudgetingPage(Session<T> session) {
        super(session);
    }

    private static final Locator SUBSIDY_BUDGETING_SIDEBAR_BUTTON = new Locator("SUBSIDY_BUDGETING_SIDEBAR_BUTTON",
            new By.ByXPath("//span[@title='Subsidy Budgeting']"));
    private static final Locator SUBSIDY_BUDGETING_TITLE = new Locator("SUBSIDY_BUDGETING_TITLE",
            new By.ByXPath("//span[@title='Subsidy Budgeting']"));
    private static final Locator SHOW_FILTER_BUTTON = new Locator("SHOW_FILTER_BUTTON",
            new By.ByXPath("//button[normalize-space()='Show Filter']"));
    private static final Locator STATUS_INPUT_FIELD = new Locator("STATUS_INPUT_FIELD",
            By.xpath("//input[@id='status']"));
    private static final Locator VERTICAL_INPUT_FIELD = new Locator("VERTICAL_INPUT_FIELD",
            By.xpath("//input[@id='vertical--distribution-channel']"));
    private static final Locator SEARCH_BY_BUDGET_NAME = new Locator("SEARCH_BY_BUDGET_NAME",
            By.xpath("//input[@id='search-by-budget-name']"));
    private static final Locator ACTIVE_OPTION = new Locator("ACTIVE_OPTION",
            new By.ByXPath("//span[normalize-space()='ACTIVE']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON",
            new By.ByXPath("//button[normalize-space()='Search']"));
    private static final Locator BUDGET_NAME_FROM_RESULT_TABLE = new Locator("BUDGET_NAME_FROM_RESULT_TABLE",
            new By.ByXPath("//div[text()='Active']"));
    private static final Locator BUDGET_ACTIVE_STATUS_LABEL = new Locator("BUDGET_ACTIVE_STATUS_LABEL",
            new By.ByXPath("//div[contains(@class, 'Label_label') and text()='Active']"));
    private static final Locator SUBSIDY_BUDGETING_FILTER_BY_VERTICAL_BUTTON = new Locator(
            "SUBSIDY_BUDGETING_FILTER_BY_VERTICAL_BUTTON", new By.ByXPath("//input[@id='All']"));
    private static final Locator SUBSIDY_BUDGETING_SELECT_ALL_VERTICAL_OPTIONS = new Locator(
            "SUBSIDY_BUDGETING_SELECT_ALL_VERTICAL_OPTIONS", new By.ByXPath("(//span[contains(text(),'All')])[2]"));
    private static final Locator SUBSIDY_BUDGETING_SELECT_SPECIFIC_VERTICAL_OPTIONS = new Locator(
            "SUBSIDY_BUDGETING_SELECT_SPECIFIC_VERTICAL_OPTIONS",
            new By.ByXPath("//span[contains(@class,'Check')]//span[text()='FLIGHT']"));
    private static final Locator FILTER_SUBSIDY_BUDGET_FLIGHT_VERTICAL = new Locator(
            "FILTER_SUBSIDY_BUDGET_FLIGHT_VERTICAL", new By.ByXPath("//span[contains(text(),'FLIGHT')]"));
    private static final Locator INACTIVE_BUDGET_NAME_FROM_RESULT_TABLE = new Locator("INACTIVE_BUDGET_NAME_FROM_RESULT_TABLE",
            new By.ByXPath("//div[text()='Inactive']"));
    private static final Locator RESET_FILTER_BUTTON_ON_RESULT_PAGE = new Locator("RESET_FILTER_BUTTON_ON_RESULT_PAGE",
            new By.ByXPath("//button[normalize-space()='Reset Filter']"));
    private static final Locator SETTINGS_BUTTON_FOR_FIRST_BUDGET = new Locator("SETTINGS_BUTTON_FOR_FIRST_BUDGET",
            new By.ByXPath("(//span[normalize-space()='Settings'])[1]"));
    private static final Locator EDIT_BUTTON_FOR_FIRST_BUDGET = new Locator("EDIT_BUTTON_FOR_FIRST_BUDGET",
            new By.ByXPath("(//span[normalize-space()='Edit'])[1]"));
    private static final Locator UPDATE_SUBSIDY_BUDGETING_PAGE = new Locator("UPDATE_SUBSIDY_BUDGETING_PAGE",
            new By.ByXPath("//span[normalize-space()='Update Subsidy Budget']"));
    private static final Locator FIRST_INACTIVE_BUDGET_CHECKBOX = new Locator("FIRST_INACTIVE_BUDGET_CHECKBOX",
            new By.ByXPath("(//span[contains(@class,'Check_custom_checkbox')])[14]"));
    private static final Locator FIRST_BUDGET_CHECKBOX = new Locator("FIRST_BUDGET_CHECKBOX",
            new By.ByXPath("//tbody/tr[1]/td[1]/label[1]/span[1]"));
    private static final Locator THIRD_ACTIVE_BUDGET_CHECKBOX = new Locator("THIRD_ACTIVE_BUDGET_CHECKBOX",
            new By.ByXPath("//tbody/tr[3]/td[1]/label[1]/span[1]"));
    private static final Locator SECOND_BUDGET_CHECKBOX = new Locator("SECOND_BUDGET_CHECKBOX",
            new By.ByXPath("//tbody/tr[2]/td[1]/label[1]/span[1]"));
    private static final Locator RADIO_BUTTON_FOR_ACTIVE_OPTION = new Locator("RADIO_BUTTON_FOR_ACTIVE_OPTION",
            new By.ByXPath("//span[normalize-space()='Active']"));
    private static final Locator YES_BUTTON_ON_POP_UP = new Locator("YES_BUTTON_ON_POP_UP",
            new By.ByXPath("//button[normalize-space()='Yes']"));
    private static final Locator POPUP_MESSAGE = new Locator("POPUP_MESSAGE",
            new By.ByXPath("//div[contains(@class, 'Snackbar_snackbar')]"));
    private static final Locator RADIO_BUTTON_FOR_INACTIVE_OPTION = new Locator("RADIO_BUTTON_FOR_INACTIVE_OPTION",
            new By.ByXPath("(//span[contains(@class, 'Radio_checkbox')])[2]"));
    private static final Locator ALL_OPTION = new Locator("ALL_OPTION",
            new By.ByXPath("(//span[contains(@class,'Check_custom_checkbox')])[1]"));
    private static final Locator RESET_FILTER_BUTTON_ON_FILTER_PAGE = new Locator("RESET_FILTER_BUTTON_ON_FILTER_PAGE",
            new By.ByXPath("//button[normalize-space()='Reset filter']"));
    private static final Locator MULTIPLE_DAY_PERIOD_VALUE = new Locator("MULTIPLE_DAY_PERIOD_VALUE",
            new By.ByXPath("//td[@data-testid='table-cell-0-period']/span"));
    private static final Locator TIKET_BUDGET_VALUE = new Locator("TIKET_BUDGET_VALUE",
            new By.ByXPath("//td[contains(@data-testid,'remainingTiket')]"));
    private static final Locator VENDOR_BUDGET_VALUE = new Locator("VENDOR_BUDGET_VALUE",
            new By.ByXPath("//td[contains(@data-testid,'remainingVendor')]"));
    private static final Locator SELECT_CLOSE_BUTTON = new Locator("SELECT_CLOSE_BUTTON",
            new By.ByXPath("//button[contains(@class, 'modal_close')]"));
    private static final Locator SUBSIDY_BUDGET_STATUS_FILTER = new Locator("SUBSIDY_BUDGET_STATUS_FILTER",
            new By.ByXPath("(//div[contains(@class, 'styles_input_inner_wrapper')])[4]"));
    private static final Locator SUBSIDY_BUDGET_ACTIVE_STATUS_FILTER = new Locator(
            "SUBSIDY_BUDGET_ACTIVE_STATUS_FILTER", new By.ByXPath("//span[text()='ACTIVE']"));
    private static final Locator SUBSIDY_BUDGET_SELECT_RULE_NAME_FILTER = new Locator(
            "SUBSIDY_BUDGET_SELECT_RULE_NAME_FILTER", new By.ByXPath("//input[@id='search-by-budget-name']"));
    private static final Locator SUBSIDY_BUDGET_FILTER_BUDGET_ACTIVE = new Locator(
            "SUBSIDY_BUDGET_FILTER_BUDGET_ACTIVE", new By.ByXPath("//tbody/tr[1]//div[normalize-space()='Active']"));
    private static final Locator RULES_SECTION = new Locator("RULES_SECTION",
            new By.ByXPath("//div[@class='mt-4 rounded-md border']"));
    private static final Locator FIRST_RULE_CHECKBOX = new Locator("FIRST_RULE_CHECKBOX",
            new By.ByXPath("(//input[@type='checkbox'])[2]"));
    private static final Locator SECOND_RULE_CHECKBOX = new Locator("SECOND_RULE_CHECKBOX",
            new By.ByXPath("(//input[@type='checkbox'])[4]"));
    private static final Locator THIRD_RULE_CHECKBOX = new Locator("THIRD_RULE_CHECKBOX",
            new By.ByXPath("(//input[@type='checkbox'])[5]"));
    private static final Locator FOURTH_RULE_CHECKBOX = new Locator("FOURTH_RULE_CHECKBOX",
            new By.ByXPath("(//input[@type='checkbox'])[6]"));
    private static final Locator SECOND_RULE_CHECKBOXV2 = new Locator("SECOND_RULE_CHECKBOXV2",
            new By.ByXPath("(//input[@type='checkbox'])[3]"));
    private static final Locator SAVE_BUTTON_UPDATE_PAGE = new Locator("SAVE_BUTTON_UPDATE_PAGE", new By.ByXPath(
            "//button[@type='submit'][contains(@class, 'Button_variant_primary')][normalize-space()='Save']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON",
            By.xpath("//button[@type='submit' and normalize-space()='Save']"));
    private static final Locator SEARCH_BY_RULE_NAME = new Locator("SEARCH_BY_RULE_NAME",
            By.xpath("//input[@id='search-by-rule-name']"));
    private static final Locator SEARCH_RULE_BUTTON = new Locator("SEARCH_RULE_BUTTON",
            By.xpath("//button[contains(@class, 'Button_variant_secondary') and normalize-space()='Search']"));
    private static final Locator UPDATE_SUCCESS_MESSAGE = new Locator("UPDATE_SUCCESS_MESSAGE", new By.ByXPath(
            "//div[contains(@class,'Snackbar_snackbar')] | //div[contains(text(),'successfully')] | //div[contains(text(),'Success')]"));
    private static final Locator ADD_NEW_BUDGET_BUTTON = new Locator("ADD_NEW_BUDGET_BUTTON",
            By.xpath("//button[normalize-space()='Add new budget']"));
    private static final Locator BUDGET_NAME_INPUT = new Locator("BUDGET_NAME_INPUT",
            By.xpath("//input[@id='budget-name']"));
    private static final Locator BUDGET_DATE_TYPE_DROPDOWN = new Locator("BUDGET_DATE_TYPE_DROPDOWN",
            By.xpath("//input[@id='budget-date-type']"));
    private static final Locator BUDGET_DATE_TYPE_WITHOUT_PERIOD = new Locator("BUDGET_DATE_TYPE_WITHOUT_PERIOD",
            By.xpath("//div[@id='dropdown-options-limitType-1' and normalize-space()='Without period']"));
    private static final Locator VERTICAL_DROPDOWN = new Locator("VERTICAL_DROPDOWN",
            By.xpath("//input[@placeholder='Vertical / Distribution Channel']"));
    private static final Locator DROPDOWN_CONTAINER = new Locator("DROPDOWN_CONTAINER",
            By.xpath("(//div[contains(@class,'fixed z-100')]//div[contains(@class,'display-scrollbar')])[2]"));
    private static final Locator TRANSACTION_QUOTA_RADIO = new Locator("TRANSACTION_QUOTA_RADIO",
            By.xpath("//label[span[normalize-space()='Transaction Quota']]"));
    private static final Locator QUOTA_INPUT = new Locator("QUOTA_INPUT", By.xpath("//input[@id='quota']"));
    private static final Locator CONFIRM_SAVE_BUTTON = new Locator("CONFIRM_SAVE_BUTTON",
            By.xpath("//button[.//span[@data-testid='confirm-submit-button']]"));
    private static final Locator SNACKBAR_MESSAGE = new Locator("SNACKBAR_MESSAGE",
            By.xpath("//div[contains(@class,'Snackbar_text_content')]"));
    private static final Locator FIRST_ROW_DATA = new Locator("FIRST_ROW_DATA", By.xpath("//table/tbody/tr[1]"));
    private static final Locator ERROR_MESSAGE = new Locator("ERROR_MESSAGE",
            By.xpath("//div[contains(@class,'styles_helper_text_container')]//span"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON",
            By.xpath("//div[contains(@data-testid,'Delete')]"));
    private static final Locator RULE_1_CHECKBOX = new Locator("RULE_1_CHECKBOX",
            By.xpath("//label[.//span[contains(@class,'highEmphasis')]]//input[@type='checkbox']"));
    private static final Locator RULE_SEARCH_INPUT = new Locator("RULE_SEARCH_INPUT",
            By.xpath("//input[@id='search-by-rule-name']"));
    private static final Locator RULE_SEARCH_BUTTON = new Locator("RULE_SEARCH_BUTTON",
            By.xpath("//input[@id='search-by-rule-name']/following::button[contains(@class,'Button_variant_secondary')][normalize-space()='Search'][1]"));
    private static final Locator DELETE_ALL_RULES_BUTTON = new Locator("DELETE_ALL_RULES_BUTTON", By.xpath(
            "//button[@type='button' and contains(@class,'Button_size_small') and normalize-space(text())='Delete all']"));
    private static final Locator DELETE_MODAL_HEADER = new Locator("DELETE_MODAL_HEADER", 
            By.xpath("//span[normalize-space()='Delete this subsidy budget?']"));      
    private static final Locator CONFIRM_DELETE_BUTTON_IN_MODAL = new Locator("CONFIRM_DELETE_BUTTON_IN_MODAL",
            By.xpath("//span[normalize-space()='Delete this subsidy budget?']/parent::div//button[normalize-space()='Delete']"));
    private static final Locator ERROR_MODAL_HEADER = new Locator("ERROR_MODAL_HEADER", 
            By.xpath("//h2[normalize-space()='Budget with active status cannot be deleted']"));
    private static final Locator MODAL_CLOSE_BUTTON = new Locator("MODAL_CLOSE_BUTTON", 
            By.xpath("//button[@data-testid='modal-close-button']"));
   private static final Locator VERTICAL_DROPDOWN_CONTAINER = new Locator("VERTICAL_DROPDOWN_CONTAINER", By.xpath("//div[contains(@class, 'display-scrollbar') and .//span[text()='FLIGHT']]"));

    private static final Locator CONFIRM_DELETE_BUTTON = new Locator("CONFIRM_DELETE_BUTTON",
            By.xpath("//button[normalize-space()='Delete']"));

    // CV type
    private static final Locator CURRENCY_VALUE_RADIO = new Locator("CURRENCY_VALUE_RADIO",
            By.xpath("//label[span[normalize-space()='Currency Value']]"));
    private static final Locator TIKET_BUDGET_INPUT = new Locator("TIKET_BUDGET_INPUT",
            By.xpath("//input[@id='tiket-budget']"));
    private static final Locator VENDOR_BUDGET_INPUT = new Locator("VENDOR_BUDGET_INPUT",
            By.xpath("//input[@id='vendor-budget']"));
    private static final Locator CURRENCY_DROPDOWN_INPUT = new Locator("CURRENCY_DROPDOWN_INPUT",
            By.xpath("//input[@id='currency']"));

    // Date range
    private static final Locator BUDGET_DATE_TYPE_WITH_PERIOD = new Locator("BUDGET_DATE_TYPE_WITH_PERIOD",
            By.xpath("//div[@id='dropdown-options-limitType-0' and normalize-space()='With period']"));
    private static final Locator START_DATE_INPUT = new Locator("START_DATE_INPUT",
            By.xpath("//input[@id='start-date']"));
    private static final Locator END_DATE_INPUT = new Locator("END_DATE_INPUT",
            By.xpath("//input[@id='end-date']"));
    private static final Locator DATE_RANGE_ERROR = new Locator("DATE_RANGE_ERROR",
            By.xpath("//span[contains(@class,'styles_helper_text') and (contains(normalize-space(),'date') or contains(normalize-space(),'period'))]"));

    // Status on create form
    private static final Locator FORM_STATUS_INACTIVE_RADIO = new Locator("FORM_STATUS_INACTIVE_RADIO",
            By.xpath("//label[.//span[normalize-space()='Inactive']]//span[contains(@class,'Radio')]"));
    private static final Locator FORM_STATUS_ACTIVE_RADIO = new Locator("FORM_STATUS_ACTIVE_RADIO",
            By.xpath("//div[contains(@class, 'Toggle_toggle') and contains(@class, 'Toggle_toggle_large')]"));

    // Tracking page
    private static final Locator TRACKING_BUTTON_IN_SETTINGS = new Locator("TRACKING_BUTTON_IN_SETTINGS",
            By.xpath("(//span[normalize-space()='Tracking'])[1]"));
    private static final Locator TRACKING_PAGE_HEADER = new Locator("TRACKING_PAGE_HEADER",
            By.xpath("//*[contains(normalize-space(),'Budget Tracking') or contains(normalize-space(),'Tracking Detail')]"));
    private static final Locator PER_DAY_TRACKING_TABLE = new Locator("PER_DAY_TRACKING_TABLE",
            By.xpath("//table[.//th[contains(normalize-space(),'Date')]]"));
    private static final Locator TIKET_BUDGET_USAGE_COLUMN = new Locator("TIKET_BUDGET_USAGE_COLUMN",
            By.xpath("//th[contains(normalize-space(),'Tiket')]"));
    private static final Locator VENDOR_BUDGET_USAGE_COLUMN = new Locator("VENDOR_BUDGET_USAGE_COLUMN",
            By.xpath("//th[contains(normalize-space(),'Vendor')]"));
    private static final Locator DOWNLOAD_CSV_BUTTON = new Locator("DOWNLOAD_CSV_BUTTON",
            By.xpath("//button[contains(normalize-space(),'Download')]"));

    // Edit page restricted fields
    private static final Locator VERTICAL_FIELD_ON_EDIT = new Locator("VERTICAL_FIELD_ON_EDIT",
            By.xpath("//input[@id='vertical--distribution-channel']"));
    private static final Locator BUDGET_TYPE_RADIO_ON_EDIT = new Locator("BUDGET_TYPE_RADIO_ON_EDIT",
            By.xpath("//label[.//span[normalize-space()='Transaction Quota']]"));

    private static Locator getCurrencyOption(String currency) {
        return new Locator("CURRENCY_OPTION_" + currency,
                By.xpath(String.format("//div[starts-with(@id,'autocomplete-options-currency-') and @title='%s']", currency)));
    }

    private static Locator getSubsidyBudgetName(String budgetName) {
        return new Locator("SEARCH_BUDGET_NAME_FROM_RESULT_TABLE",
                By.xpath(String.format("//td[contains(@data-testid,'budgetName')][text()='%s']", budgetName)));
    }

    private static final Locator SETTINGS_LOCATOR = new Locator("SETTINGS_LOCATOR", By.xpath(
            "//span[normalize-space()='Settings']"));

    private static final Locator CLICK_EDIT = new Locator("CLICK_EDIT", By.xpath(
            "//span[normalize-space()='Edit']"));

    private static final Locator SUBSIDY_BUDGETING = new Locator("SUBSIDY_BUDGETING", By.xpath(
            "//span[normalize-space()='Subsidy Budgeting']"));

    public void clickSubsidyBudgetingSidebarButton() {
        driverTools.waitForElementToAppear(SUBSIDY_BUDGETING_SIDEBAR_BUTTON);
        driverTools.click(SUBSIDY_BUDGETING_SIDEBAR_BUTTON);
    }

    public void clickSubsidyBudgetingStatusFilter() {
        driverTools.waitForElementToAppear(SUBSIDY_BUDGET_STATUS_FILTER);
        driverTools.click(SUBSIDY_BUDGET_STATUS_FILTER);
    }

    public void clickSubsidyBudgetRuleNameField(String ruleName) {
        driverTools.waitForElementToAppear(SUBSIDY_BUDGET_SELECT_RULE_NAME_FILTER);
        driverTools.click(SUBSIDY_BUDGET_SELECT_RULE_NAME_FILTER);
        driverTools.sendKeys(SUBSIDY_BUDGET_SELECT_RULE_NAME_FILTER, ruleName);
    }

    public void clickSubsidyBudgetActiveStatusCheckbox() {
        driverTools.waitForElementToAppear(SUBSIDY_BUDGET_ACTIVE_STATUS_FILTER, 10).orElseThrow();
        driverTools.click(SUBSIDY_BUDGET_ACTIVE_STATUS_FILTER);
    }

    public boolean isSubsidyBudgetingPageLoaded() {
        driverTools.waitForElementToAppear(SUBSIDY_BUDGETING_TITLE);
        return driverTools.isElementDisplayed(SUBSIDY_BUDGETING_TITLE);
    }

    public void clickOnShowFilterButton() {
        Optional<WebElement> button = driverTools.waitForElementToAppear(SHOW_FILTER_BUTTON, 10);
        if (button.isPresent()) {
            driverTools.click(button.get());
        }
    }

    public void clickStatusInputField() {
        driverTools.waitForElementToAppear(STATUS_INPUT_FIELD);
        driverTools.click(STATUS_INPUT_FIELD);
    }

    public void clickVerticalInputField() {
        driverTools.waitForElementToAppear(VERTICAL_INPUT_FIELD);
        driverTools.click(VERTICAL_INPUT_FIELD);
    }

    private static Locator getVerticalOptionLocator(String verticalName) {
        return new Locator("VERTICAL_OPTION_" + verticalName,
                By.xpath(String.format("//span[contains(@class,'Check')]//span[text()='%s']", verticalName)));
    }

    public void selectVerticalAndCloseDropdown(String verticalName) {
        driverTools.waitForElementToAppear(VERTICAL_INPUT_FIELD);
        driverTools.click(VERTICAL_INPUT_FIELD);
        clickSelectAllVertical();
        Locator option = getVerticalOptionLocator(verticalName);
        driverTools.waitForElementToAppear(option, 10).orElseThrow();
        driverTools.click(option);
        driverTools.sendKeys(VERTICAL_INPUT_FIELD, Keys.ESCAPE.toString());
    }

    public void selectActiveOption() {
        driverTools.waitForElementToAppear(ACTIVE_OPTION);
        driverTools.click(ACTIVE_OPTION);
        driverTools.click(SEARCH_BY_BUDGET_NAME);
    }

    public void clickSearchButton() {
        driverTools.waitForElementToAppear(SEARCH_BUTTON);
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
    }

    public boolean verifySearchResult(String budgetName) {
        WebElement textElement = driverTools.waitForElementToAppear(BUDGET_NAME_FROM_RESULT_TABLE,
                10).orElseThrow();
        String text = driverTools.getText(textElement);
        return text.contains(budgetName);
    }

    public boolean verifyInactiveSearchResult() {
        driverTools.waitForElementToAppear(INACTIVE_BUDGET_NAME_FROM_RESULT_TABLE, 10).orElseThrow();
        return driverTools.isElementDisplayed(INACTIVE_BUDGET_NAME_FROM_RESULT_TABLE);
    }

    public boolean verifySubsidyBudgetingDashboardTitle(String budgetName) {
        WebElement textElement = driverTools.waitForElementToAppear(BUDGET_NAME_FROM_RESULT_TABLE, 10).orElseThrow();
        String text = driverTools.getText(textElement);
        return text.contains(budgetName);
    }

    public void clickSelectAllVertical() {
        List<WebElement> elements = driverTools.elements(SUBSIDY_BUDGETING_SELECT_ALL_VERTICAL_OPTIONS).get();
        if (!elements.isEmpty()) {
            driverTools.click(SUBSIDY_BUDGETING_SELECT_ALL_VERTICAL_OPTIONS);
        } else {
            throw new NoSuchElementException("The 'Select All' vertical option was not found.");
        }
    }

    public void searchByByBudgetName(String budgetName) {
        driverTools.waitForElementToAppear(SEARCH_BY_BUDGET_NAME);
        driverTools.click(SEARCH_BY_BUDGET_NAME);
        driverTools.sendKeys(SEARCH_BY_BUDGET_NAME, budgetName);
    }

    public boolean verifySearchBudgedNameResult(String budgetName) {
        WebElement textElement = driverTools.waitForElementToAppear(getSubsidyBudgetName(budgetName), 10).orElseThrow();
        String text = driverTools.getText(textElement);
        return text.contains(budgetName);
    }

    public void clickOnSearchBudgetName() {
        driverTools.waitForElementToAppear(SEARCH_BY_BUDGET_NAME);
        driverTools.click(SEARCH_BY_BUDGET_NAME);
    }

    public void enterInvalidBudgetName(String invalidBudgetName) {
        WebElement budgetNameInput = driverTools.waitForElementToAppear(SEARCH_BY_BUDGET_NAME).orElseThrow();
        budgetNameInput.clear();
        budgetNameInput.sendKeys(invalidBudgetName);
    }

    public void clickSubsidyBudgetFilterByVertical() {
        // driverTools.waitForElementToAppear(SHOW_FILTER_BUTTON);
        clickOnShowFilterButton();
        driverTools.waitForElementToAppear(SUBSIDY_BUDGETING_FILTER_BY_VERTICAL_BUTTON);
        driverTools.click(SUBSIDY_BUDGETING_FILTER_BY_VERTICAL_BUTTON);
        clickSelectAllVertical();
        driverTools.click(SUBSIDY_BUDGETING_SELECT_SPECIFIC_VERTICAL_OPTIONS);
        driverTools.click(SEARCH_BUTTON);

    }

    public boolean verifyNoResultResetFilterOption() {
        driverTools.waitForElementToAppear(RESET_FILTER_BUTTON_ON_RESULT_PAGE);
        return driverTools.isElementDisplayed(RESET_FILTER_BUTTON_ON_RESULT_PAGE);

    }

    public boolean verifyFilterResult(String verticalName) {
        Locator verticalResult = new Locator("VERTICAL_RESULT_" + verticalName,
                By.xpath(String.format("//span[contains(text(),'%s')]", verticalName)));
        driverTools.waitForElementToAppear(verticalResult, 10).orElseThrow();
        String text = driverTools.getText(verticalResult).orElse("");
        return text.contains(verticalName);
    }

    public void clickOnSettingsButton() {
        driverTools.waitForElementToAppear(SETTINGS_BUTTON_FOR_FIRST_BUDGET);
        driverTools.scrollToElement(SETTINGS_BUTTON_FOR_FIRST_BUDGET);
        driverTools.click(SETTINGS_BUTTON_FOR_FIRST_BUDGET);
    }

    public void clickOnEditButton() {
        driverTools.waitForElementToAppear(EDIT_BUTTON_FOR_FIRST_BUDGET);
        driverTools.click(EDIT_BUTTON_FOR_FIRST_BUDGET);
    }

    public boolean isUpdateSubsidyBudgetingPageLoaded() {
        driverTools.waitForElementToAppear(UPDATE_SUBSIDY_BUDGETING_PAGE);
        return driverTools.isElementDisplayed(UPDATE_SUBSIDY_BUDGETING_PAGE);
    }

    public void selectSecondInactiveBudget() {
        driverTools.waitForElementToAppear(SECOND_BUDGET_CHECKBOX, 10).orElseThrow();
        driverTools.click(SECOND_BUDGET_CHECKBOX);
    }

    public void clickRadioButtonOfActiveOption() {
        driverTools.scrollToElement(RADIO_BUTTON_FOR_ACTIVE_OPTION);
        driverTools.waitForElementToAppear(RADIO_BUTTON_FOR_ACTIVE_OPTION);
        driverTools.click(RADIO_BUTTON_FOR_ACTIVE_OPTION);
    }

    public void clickYesButton() {
        driverTools.waitForElementToAppear(YES_BUTTON_ON_POP_UP);
        driverTools.clickWithJavascriptExecutor(YES_BUTTON_ON_POP_UP);
    }

    public boolean verifyThePopUpMessage(String message, String message2) {
        WebElement textElement = driverTools.waitForElementToAppear(POPUP_MESSAGE).orElseThrow();
        String text = driverTools.getText(textElement);
        return text.contains(message) || text.contains(message2);
    }

    public boolean verifyThePopUpRevertMessage(String revertMessage1, String revertMessage2) {
        WebElement textElement = driverTools.waitForElementToAppear(POPUP_MESSAGE).orElseThrow();
        String text = driverTools.getText(textElement);
        return text.contains(revertMessage1) || text.contains(revertMessage2);
    }

    public void clickTheResetFilterButton() {
        driverTools.scrollToElement(RESET_FILTER_BUTTON_ON_FILTER_PAGE);
        driverTools.waitForElementToAppear(RESET_FILTER_BUTTON_ON_FILTER_PAGE);
        driverTools.click(RESET_FILTER_BUTTON_ON_FILTER_PAGE);
    }

    public void selectFirstActiveBud() {
        driverTools.waitForElementToAppear(FIRST_BUDGET_CHECKBOX);
        driverTools.scrollToElement(FIRST_BUDGET_CHECKBOX);
        driverTools.click(FIRST_BUDGET_CHECKBOX);
    }

    public void selectThirdActiveBud() {
        driverTools.waitForElementToAppear(THIRD_ACTIVE_BUDGET_CHECKBOX, 10).orElseThrow();
        driverTools.scrollToElement(THIRD_ACTIVE_BUDGET_CHECKBOX);
        driverTools.click(THIRD_ACTIVE_BUDGET_CHECKBOX);
    }

    public void closePopupIfPresent() {
        List<WebElement> closeButtons = driver.findElements(SELECT_CLOSE_BUTTON.by());
        if (!closeButtons.isEmpty()) {
            driverTools.waitForElementToBeClickable(SELECT_CLOSE_BUTTON);
            driverTools.click(SELECT_CLOSE_BUTTON);
        }
    }

    public void clickRadioButtonOfInActiveOption() {
        driverTools.scrollToElement(RADIO_BUTTON_FOR_INACTIVE_OPTION);
        driverTools.waitForElementToAppear(RADIO_BUTTON_FOR_INACTIVE_OPTION);
        driverTools.click(RADIO_BUTTON_FOR_INACTIVE_OPTION);
    }

    public void selectInactiveOption() {
        driverTools.maximizeWindow();
        driverTools.waitForElementToAppear(ALL_OPTION);
        driverTools.click(ALL_OPTION);
        driverTools.click(ACTIVE_OPTION);
        driverTools.click(SEARCH_BY_BUDGET_NAME);
    }

    public boolean verifyMultipleDayPeriodValue(String day) {
        WebElement textElement = driverTools.waitForElementToAppear(MULTIPLE_DAY_PERIOD_VALUE).orElseThrow();
        String text = driverTools.getText(textElement);
        return text.contains(day);
    }

    public boolean verifyFilterActiveBudgetStatus() {
        driverTools.waitForElementToAppear(BUDGET_NAME_FROM_RESULT_TABLE, 10).orElseThrow();
        driverTools.scrollToElement(BUDGET_NAME_FROM_RESULT_TABLE);
        return driverTools.isElementDisplayed(BUDGET_NAME_FROM_RESULT_TABLE);
    }

    public boolean verifyBudgetActiveStatusInTable() {
        driverTools.waitForElementToAppear(BUDGET_ACTIVE_STATUS_LABEL, 20).orElseThrow();
        driverTools.scrollToElement(BUDGET_ACTIVE_STATUS_LABEL);
        return driverTools.isElementDisplayed(BUDGET_ACTIVE_STATUS_LABEL);
    }

    public boolean verifyTiketBudVendorBudIsGreaterThanZero() {
        WebElement tiketBudget = driverTools.waitForElementToAppear(TIKET_BUDGET_VALUE).orElseThrow();
        WebElement vendorBudget = driverTools.waitForElementToAppear(VENDOR_BUDGET_VALUE).orElseThrow();

        String tiketBudgetValue = driverTools.getText(tiketBudget);
        String vendorBudgetValue = driverTools.getText(vendorBudget);

        int tiketAmount = parseCurrencyToInt(tiketBudgetValue);
        int vendorAmount = parseCurrencyToInt(vendorBudgetValue);

        return tiketAmount > 0 && vendorAmount > 0;
    }

    private int parseCurrencyToInt(String currencyText) {
        if (currencyText == null)
            return 0;
        String cleaned = currencyText.replace("IDR", "").replace(",", "").trim();
        try {
            return Integer.parseInt(cleaned);
        } catch (NumberFormatException e) {
            System.err.println("Failed to parse currency text: " + currencyText + ". Returning 0 as default. Error: "
                    + e.getMessage());
            return 0;
        }
    }

    public void wrapperFunction() {
        clickStatusInputField();
        selectInactiveOption();
        clickSearchButton();
        selectSecondInactiveBudget();
        clickRadioButtonOfActiveOption();
        clickYesButton();
    }

    public boolean isRulesSectionDisplayed() {
        driverTools.waitForElementToAppear(RULES_SECTION, 10);
        return driverTools.isElementDisplayed(RULES_SECTION);
    }

    public void selectFirstRule() {
        driverTools.waitForElementToAppear(FIRST_RULE_CHECKBOX, 2);
        driverTools.clickWithJavascriptExecutor(FIRST_RULE_CHECKBOX);
    }

    public void searchByRuleName(String ruleName) {
        driverTools.waitForElementToAppear(SEARCH_BY_RULE_NAME, 5).orElseThrow();
        driverTools.click(SEARCH_BY_RULE_NAME);
        driverTools.sendKeys(SEARCH_BY_RULE_NAME, ruleName);
        driverTools.waitForElementToAppear(SEARCH_RULE_BUTTON, 5).orElseThrow();
        driverTools.click(SEARCH_RULE_BUTTON);
    }

    public void selectSecondRule() {
        driverTools.waitForElementToAppear(SECOND_RULE_CHECKBOX, 2);
        driverTools.clickWithJavascriptExecutor(SECOND_RULE_CHECKBOX);
    }

    public void selectThirdRule() {
        driverTools.waitForElementToAppear(THIRD_RULE_CHECKBOX, 2);
        driverTools.clickWithJavascriptExecutor(THIRD_RULE_CHECKBOX);
    }

    public void selectFourthRule() {
        driverTools.waitForElementToAppear(FOURTH_RULE_CHECKBOX, 2);
        driverTools.clickWithJavascriptExecutor(FOURTH_RULE_CHECKBOX);
    }

    public void selectSecondRuleV2() {
        driverTools.waitForElementToAppear(SECOND_RULE_CHECKBOXV2, 2);
        driverTools.clickWithJavascriptExecutor(SECOND_RULE_CHECKBOXV2);
    }

    public void selectMultipleRules() {
        selectFirstRule();
        driverTools.dummyWait(1);
        selectSecondRule();
        driverTools.dummyWait(1);
        selectThirdRule();
        driverTools.dummyWait(1);
        selectFourthRule();
        driverTools.dummyWait(1);
    }

    public void clickSaveButtonOnUpdatePage() {
        driverTools.waitForElementToAppear(SAVE_BUTTON_UPDATE_PAGE, 2);
        driverTools.scrollToElement(SAVE_BUTTON_UPDATE_PAGE);
        driverTools.click(SAVE_BUTTON_UPDATE_PAGE);
        driverTools.waitForElementToAppear(CONFIRM_SAVE_BUTTON);
        if (driverTools.isElementDisplayed(CONFIRM_SAVE_BUTTON)) {
            driverTools.click(CONFIRM_SAVE_BUTTON);
        }
    }

    public boolean verifyUpdateSuccessMessage() {
        return driverTools.waitForElementToAppear(UPDATE_SUCCESS_MESSAGE, 10).isPresent();
    }

    public boolean verifyMultipleRulesSelected() {
        int selectedCount = getSelectedRulesCount();
        return selectedCount > 1;
    }

    public int getSelectedRulesCount() {
        driverTools.dummyWait(2);

        return (int) driverTools.getDriver()
                .findElements(By.xpath("//button[contains(@class,'Chip_size_default')]"))
                .stream()
                .filter(WebElement::isDisplayed)
                .count();
    }

    public void goToSettingsAndClickEdit(){
        driverTools.click(SUBSIDY_BUDGETING);
        driverTools.waitForElementToAppear(SETTINGS_LOCATOR);
        driverTools.click(SETTINGS_LOCATOR);
        driverTools.click(CLICK_EDIT);
    }

    public boolean verifyMandatoryFieldsPrefilled() {
        boolean budgetNameFilled = false;
        boolean quotaFilled = false;
        boolean verticalFilled = false;

        try {
            WebElement budgetNameInput = driverTools.waitForElementToAppear(
                    new Locator("BUDGET_NAME_INPUT", By.xpath("//input[@id='budget-name']")), 5).orElse(null);
            if (budgetNameInput != null && !budgetNameInput.getAttribute("value").isEmpty()) {
                budgetNameFilled = true;
            }

            WebElement quotaInput = driverTools.waitForElementToAppear(
                    new Locator("QUOTA_INPUT", By.xpath("//input[@id='quota']")), 5).orElse(null);
            if (quotaInput != null && !quotaInput.getAttribute("value").isEmpty()) {
                quotaFilled = true;
            }

            WebElement verticalInput = driverTools.waitForElementToAppear(
                    new Locator("VERTICAL_INPUT", By.xpath("//input[@id='vertical--distribution-channel']")), 5)
                    .orElse(null);
            if (verticalInput != null && !verticalInput.getAttribute("value").isEmpty()) {
                verticalFilled = true;
            }
        } catch (Exception e) {
            return false;
        }

        return budgetNameFilled && quotaFilled && verticalFilled;
    }

    public boolean verifyUpdatedRulesInEditPage() {
        goToSettingsAndClickEdit();
        driverTools.waitForElementToAppear(UPDATE_SUBSIDY_BUDGETING_PAGE, 10);

        return verifyMultipleRulesSelected();
    }

    public void goDirectlyToEditPage(String budgetId) {
        String editUrl = "";
        switch (Configs.ENVIRONMENT) {
            case GK:
                editUrl = "https://gatotkaca.tiket.com/admin/pricing-dashboard/subsidy-budgeting/update/" + budgetId;
                break;
            case PREPROD:
                editUrl = "https://preprod.tiket.com/admin/pricing-dashboard/subsidy-budgeting/update/" + budgetId;
                break;
            case PROD:
                editUrl = "https://www.tiket.com/admin/pricing-dashboard/subsidy-budgeting/update/" + budgetId;
        }
        driverTools.get(editUrl);
        driverTools.waitForWebToLoad();
        driverTools.dummyWait(3);
    }

    public boolean verifySubsidyBudgetUpdateComplete() {
        boolean successMessage = verifyUpdateSuccessMessage();
        boolean rulesSelected = verifyMultipleRulesSelected();
        return successMessage || rulesSelected;
    }

    public void uncheckAllSelectedRules() {
        driverTools.dummyWait(1);
        List<WebElement> checkboxes = driverTools.getDriver().findElements(
                By.xpath("//li[.//input[@type='checkbox' and @checked]]//span[contains(@class,'Text_size_b3__MWY7D')]"));

        for (WebElement checkbox : checkboxes) {
            try {
                if (checkbox.isSelected()) {
                    driverTools.scrollToElement(checkbox);
                    driverTools.clickWithJavascriptExecutor(checkbox);
                    driverTools.dummyWait(1);
                }
            } catch (Exception e) {
                log("Failed to uncheck checkbox: " + e.getMessage());
            }
        }
        driverTools.dummyWait(1);
    }

    public void cleanupAfterTest() {
        try {
            if (isRulesSectionDisplayed()) {
                uncheckAllSelectedRules();
            }
        } catch (Exception e) {
            log("Warning: Could not cleanup rule state: " + e.getMessage());
        }
    }

    public void clickAddNewBudgetButton() {
        driverTools.waitForElementToAppear(ADD_NEW_BUDGET_BUTTON);
        driverTools.click(ADD_NEW_BUDGET_BUTTON);
    }

    public void inputBudgetName(String budgetName) {
        driverTools.waitForElementToAppear(BUDGET_NAME_INPUT);
        driverTools.click(BUDGET_NAME_INPUT);
        driverTools.sendKeys(BUDGET_NAME_INPUT, budgetName);
    }

    public void selectBudgetDateTypeWithoutPeriod() {
        driverTools.waitForElementToAppear(BUDGET_DATE_TYPE_DROPDOWN);
        driverTools.click(BUDGET_DATE_TYPE_DROPDOWN);
        driverTools.waitForElementToAppear(BUDGET_DATE_TYPE_WITHOUT_PERIOD);
        driverTools.click(BUDGET_DATE_TYPE_WITHOUT_PERIOD);
    }

    private static Locator getVerticalOption(String vertical) {
        return new Locator("VERTICAL_OPTION_" + vertical,
                By.xpath(String.format("//div[starts-with(@id,'dropdown-options-vertical-') and @data-value='%s']", vertical)));
    }

    public void selectVertical(String vertical) {
        Locator option = getVerticalOption(vertical);
        driverTools.waitForElementToAppear(VERTICAL_DROPDOWN);
        driverTools.click(VERTICAL_DROPDOWN);
        driverTools.scrollUntilVisibleInElement(DROPDOWN_CONTAINER, option, 300, 10);
        driverTools.clickWithJavascriptExecutor(option);
    }

    public void selectTransactionQuotaRadio() {
        driverTools.waitForElementToAppear(TRANSACTION_QUOTA_RADIO);
        driverTools.clickWithJavascriptExecutor(TRANSACTION_QUOTA_RADIO);
    }

    public void inputQuota(String quota) {
        driverTools.waitForElementToAppear(QUOTA_INPUT);
        driverTools.click(QUOTA_INPUT);
        driverTools.sendKeys(QUOTA_INPUT, quota);
    }

    public void clickSaveButton() {
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.scrollToElement(SAVE_BUTTON);
        driverTools.click(SAVE_BUTTON);
    }

    public void clickConfirmSaveButton() {
        driverTools.waitForElementToAppear(CONFIRM_SAVE_BUTTON, 10);
        driverTools.click(CONFIRM_SAVE_BUTTON);
    }

    public void clickOnBudgetRule1() {
        driverTools.waitForElementToAppear(RULE_1_CHECKBOX);
        driverTools.clickWithJavascriptExecutor(RULE_1_CHECKBOX);
    }

    public void searchRuleByName(String ruleName) {
        driverTools.waitForElementToAppear(RULE_SEARCH_INPUT);
        driverTools.click(RULE_SEARCH_INPUT);
        driverTools.sendKeys(RULE_SEARCH_INPUT, ruleName);
        driverTools.waitForElementToAppear(RULE_SEARCH_BUTTON);
        driverTools.click(RULE_SEARCH_BUTTON);
    }

    public void clickOnDeleteAllRulesButton() {
        driverTools.waitForElementToAppear(DELETE_ALL_RULES_BUTTON);
        driverTools.click(DELETE_ALL_RULES_BUTTON);
    }

    public boolean isDeleteAllButtonDisplayed() {
        return driverTools.isElementDisplayed(DELETE_ALL_RULES_BUTTON);
    }

    public void createNewBudget(String budgetName, String quota, String vertical, String currency) {
        clickAddNewBudgetButton();
        inputBudgetName(budgetName);
        selectBudgetDateTypeWithoutPeriod();
        selectVertical(vertical);
        selectTransactionQuotaRadio();
        inputQuota(quota);
        selectCurrencyFromDropdown(currency);
    }

    public void clickDeleteButton() {
        driverTools.waitForElementToAppear(DELETE_BUTTON);
        driverTools.clickWithJavascriptExecutor(DELETE_BUTTON);
    }

    public void confirmDeleteButton() {
        driverTools.waitForElementToAppear(CONFIRM_DELETE_BUTTON);
        driverTools.click(CONFIRM_DELETE_BUTTON);
    }

    public boolean verifySnackbarMessage(String expectedMessage) {
        driverTools.waitForElementToAppear(SNACKBAR_MESSAGE);
        String actualMessage = driverTools.getText(SNACKBAR_MESSAGE).toString();
        logger.log("Snackbar message: " + actualMessage);
        if (expectedMessage == null || expectedMessage.isEmpty()) {
            return !actualMessage.isEmpty();
        }
        return actualMessage.contains(expectedMessage);
    }

    public boolean verifyNewBudgetInTable(String budgetName) {
        driverTools.waitForElementToAppear(FIRST_ROW_DATA);
        driverTools.dummyWait(5);
        String rowText = driverTools.getText(FIRST_ROW_DATA).toString();
        logger.log("row text: " + rowText);
        return rowText.contains(budgetName);
    }

    public boolean verifyErrorMessage(String errorMessage) {
        driverTools.waitForElementToAppear(ERROR_MESSAGE);
        String errorText = driverTools.getText(ERROR_MESSAGE).toString();
        logger.log("Error Message: " + errorText);
        return errorText.contains(errorMessage);
    }
  
    public void selectCurrencyValueRadio() {
        driverTools.waitForElementToAppear(CURRENCY_VALUE_RADIO);
        driverTools.clickWithJavascriptExecutor(CURRENCY_VALUE_RADIO);
    }

    public void inputTiketBudgetAmount(String amount) {
        driverTools.waitForElementToAppear(TIKET_BUDGET_INPUT);
        driverTools.click(TIKET_BUDGET_INPUT);
        driverTools.sendKeys(TIKET_BUDGET_INPUT, amount);
    }

    public void inputVendorBudgetAmount(String amount) {
        driverTools.waitForElementToAppear(VENDOR_BUDGET_INPUT);
        driverTools.click(VENDOR_BUDGET_INPUT);
        driverTools.sendKeys(VENDOR_BUDGET_INPUT, amount);
    }

    public void selectCurrencyFromDropdown(String currency) {
        if ("IDR".equalsIgnoreCase(currency)) return;
        driverTools.waitForElementToAppear(CURRENCY_DROPDOWN_INPUT);
        driverTools.sendKeys(CURRENCY_DROPDOWN_INPUT, currency);
        Locator option = getCurrencyOption(currency);
        driverTools.waitForElementToAppear(option, 10)
                .orElseThrow(() -> new NoSuchElementException("Currency option not found: " + currency));
        driverTools.clickWithJavascriptExecutor(option);
    }

    public void createNewCVBudget(String budgetName, String tiketAmount, String vendorAmount, String vertical, String currency) {
        clickAddNewBudgetButton();
        inputBudgetName(budgetName);
        selectBudgetDateTypeWithoutPeriod();
        selectVertical(vertical);
        selectCurrencyValueRadio();
        selectCurrencyFromDropdown(currency);
        inputTiketBudgetAmount(tiketAmount);
        inputVendorBudgetAmount(vendorAmount);
    }

    public void selectBudgetDateTypeWithPeriod() {
        driverTools.waitForElementToAppear(BUDGET_DATE_TYPE_DROPDOWN);
        driverTools.click(BUDGET_DATE_TYPE_DROPDOWN);
        driverTools.waitForElementToAppear(BUDGET_DATE_TYPE_WITH_PERIOD);
        driverTools.click(BUDGET_DATE_TYPE_WITH_PERIOD);
    }

    public void inputStartDate(String date) {
        driverTools.waitForElementToAppear(START_DATE_INPUT);
        driverTools.click(START_DATE_INPUT);
        driverTools.sendKeys(START_DATE_INPUT, date);
    }

    public void inputEndDate(String date) {
        driverTools.waitForElementToAppear(END_DATE_INPUT);
        driverTools.click(END_DATE_INPUT);
        driverTools.sendKeys(END_DATE_INPUT, date);
        driverTools.sendKeys(END_DATE_INPUT, Keys.TAB.toString());
    }

    public void createNewTQBudgetWithPeriod(String budgetName, String quota, String startDate, String endDate, String vertical, String currency) {
        clickAddNewBudgetButton();
        inputBudgetName(budgetName);
        selectBudgetDateTypeWithPeriod();
        inputStartDate(startDate);
        inputEndDate(endDate);
        selectVertical(vertical);
        selectTransactionQuotaRadio();
        inputQuota(quota);
        selectCurrencyFromDropdown(currency);
    }

    public boolean verifyDateRangeErrorDisplayed() {
        return driverTools.waitForElementToAppear(DATE_RANGE_ERROR, 5).isPresent()
                || driverTools.waitForElementToAppear(ERROR_MESSAGE, 5).isPresent();
    }

    public void selectFormStatusInactive() {
        driverTools.waitForElementToAppear(FORM_STATUS_INACTIVE_RADIO);
        driverTools.clickWithJavascriptExecutor(FORM_STATUS_INACTIVE_RADIO);
    }

    public void selectFormStatus() {
        driverTools.waitForElementToAppear(FORM_STATUS_ACTIVE_RADIO);
        driverTools.clickWithJavascriptExecutor(FORM_STATUS_ACTIVE_RADIO);
    }

    public void clickTrackingButtonInSettings() {
        driverTools.waitForElementToAppear(TRACKING_BUTTON_IN_SETTINGS);
        driverTools.click(TRACKING_BUTTON_IN_SETTINGS);
    }

    public boolean isTrackingPageLoaded() {
        return driverTools.waitForElementToAppear(TRACKING_PAGE_HEADER, 10).isPresent();
    }

    public boolean verifyPerDayTrackingTableDisplayed() {
        return driverTools.waitForElementToAppear(PER_DAY_TRACKING_TABLE, 10).isPresent();
    }

    public boolean verifyTiketAndVendorBudgetColumnsDisplayed() {
        boolean tiket = driverTools.waitForElementToAppear(TIKET_BUDGET_USAGE_COLUMN, 5).isPresent();
        boolean vendor = driverTools.waitForElementToAppear(VENDOR_BUDGET_USAGE_COLUMN, 5).isPresent();
        return tiket && vendor;
    }

    public boolean isDownloadCSVButtonDisplayed() {
        return driverTools.waitForElementToAppear(DOWNLOAD_CSV_BUTTON, 5).isPresent();
    }

    public void clickDownloadCSVButton() {
        driverTools.waitForElementToAppear(DOWNLOAD_CSV_BUTTON);
        driverTools.click(DOWNLOAD_CSV_BUTTON);
    }

    public boolean isVerticalFieldDisabledOnEditPage() {
        WebElement el = driverTools.waitForElementToAppear(VERTICAL_FIELD_ON_EDIT, 5).orElse(null);
        if (el == null) return false;
        return el.getAttribute("disabled") != null
                || el.getAttribute("readonly") != null
                || "true".equals(el.getAttribute("aria-disabled"))
                || "none".equals(el.getCssValue("pointer-events"));
    }

    public boolean isBudgetTypeFieldDisabledOnEditPage() {
        WebElement el = driverTools.waitForElementToAppear(BUDGET_TYPE_RADIO_ON_EDIT, 5).orElse(null);
        if (el == null) return false;
        String pointerEvents = el.getCssValue("pointer-events");
        String cls = el.getAttribute("class");
        return "none".equals(pointerEvents) || (cls != null && cls.contains("disabled"));
    }
  
    public void clickConfirmDeleteButton() {
        driverTools.waitForElementToAppear(CONFIRM_DELETE_BUTTON_IN_MODAL, 10);
        driverTools.clickWithJavascriptExecutor(CONFIRM_DELETE_BUTTON_IN_MODAL);
        driverTools.waitForElementToAbsence(CONFIRM_DELETE_BUTTON_IN_MODAL, 5);
    }

    public boolean isDeletionRestrictionErrorDisplayed() {
        driverTools.waitForElementToAppear(ERROR_MODAL_HEADER, 5);
        return driverTools.isElementDisplayed(ERROR_MODAL_HEADER);
    }

    public void clickDismissErrorModal() {
        driverTools.waitForElementToBeClickable(MODAL_CLOSE_BUTTON, 5);
        driverTools.click(MODAL_CLOSE_BUTTON);
        driverTools.waitForElementToAbsence(ERROR_MODAL_HEADER, 5);
    }
    
    public boolean isDeleteModalDisplayed() {
        driverTools.waitForElementToAppear(DELETE_MODAL_HEADER, 5);
        return driverTools.isElementDisplayed(DELETE_MODAL_HEADER);
    }

    public void selectVerticalAndCloseDropdownScrollable(String verticalName) {
        driverTools.waitForElementToAppear(VERTICAL_INPUT_FIELD);
        driverTools.click(VERTICAL_INPUT_FIELD);
        clickSelectAllVertical();
        
        Locator option = getVerticalOptionLocator(verticalName);
        
        driverTools.scrollUntilVisibleInElement(VERTICAL_DROPDOWN_CONTAINER, option, 200, 15);
        
        driverTools.click(option);
        driverTools.sendKeys(VERTICAL_INPUT_FIELD, Keys.ESCAPE.toString());
    }

    public void selectTwoRules() {
        selectFirstRule();
        selectSecondRuleV2();
    }

}