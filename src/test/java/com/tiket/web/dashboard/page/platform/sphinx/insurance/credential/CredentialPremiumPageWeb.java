package com.tiket.web.dashboard.page.platform.sphinx.insurance.credential;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;

public class CredentialPremiumPageWeb<T extends WebDriver> extends WebPage<T> {

    public CredentialPremiumPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CredentialPremiumPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CredentialPremiumPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CredentialPremiumPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Platform not supported");
        }
    }

    // Search and Navigation Locators
    private static final Locator INSURANCE_GROUP_FILTER_CONTAINER = new Locator("INSURANCE_GROUP_FILTER_CONTAINER", By.xpath("//div[@class='styles_container__rxZ45'][.//input[@id='insurance-group']]"));
    private static final Locator INSURANCE_GROUP_FILTER_INPUT = new Locator("INSURANCE_GROUP_FILTER_INPUT", By.xpath("//input[@id='insurance-group']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[@data-testid='credential-btn-filter-submit']"));
    private static final Locator CHEVRON_EXPAND = new Locator("CHEVRON_EXPAND", By.xpath("(//div[contains(@class, 'credential_action_group_list')]//div[contains(@class, 'Tooltip_tooltip')][last()])[2]"));
    private static final Locator MORE_BUTTON = new Locator("MORE_BUTTON", By.xpath("(//input[@data-testid='menu-action'])[1]"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.xpath("//span[@data-testid='button-edit']"));
    private static final Locator ADD_NEW_PREMIUM_BUTTON = new Locator("ADD_NEW_PREMIUM_BUTTON", By.xpath("//button[@data-testid='button-add-new-premium']"));
    
    // Premium Form Mandatory Fields
    private static final Locator PREMIUM_NAME_FIELD = new Locator("PREMIUM_NAME_FIELD", By.xpath("//input[@data-testid='premium-name']"));
    private static final Locator FORMULA_DROPDOWN = new Locator("FORMULA_DROPDOWN", By.xpath("//input[@data-testid='input-select-premium-formula']"));
    private static final Locator CURRENCY_DROPDOWN = new Locator("CURRENCY_DROPDOWN", By.xpath("//input[@data-testid='input-select-premium-currency']"));
    private static final Locator UOM_DROPDOWN = new Locator("UOM_DROPDOWN", By.xpath("//input[@data-testid='input-select-premium-uom']"));
    private static final Locator UOM_DISCOUNT_DROPDOWN = new Locator("UOM_DISCOUNT_DROPDOWN", By.xpath("//input[@placeholder='UoM Discount']"));
    private static final Locator BROKER_FEE_FIELD = new Locator("BROKER_FEE_FIELD", By.xpath("//input[@data-testid='premium-broker-fee']"));
    private static final Locator COMMISSION_FEE_FIELD = new Locator("COMMISSION_FEE_FIELD", By.xpath("//input[@data-testid='premium-commission-fee']"));
    private static final Locator ADMIN_FEE_FIELD = new Locator("ADMIN_FEE_FIELD", By.xpath("//input[@data-testid='premium-admin-fee']"));
    private static final Locator UOM_DISCOUNT_AMOUNT_FIELD = new Locator("UOM_DISCOUNT_AMOUNT_FIELD", By.xpath("//input[@data-testid='premium-discount']"));
    // Form Action Buttons
    private static final Locator NEXT_BUTTON = new Locator("NEXT_BUTTON", By.xpath("//button[@data-testid='button-submit' or contains(text(), 'Next')]"));
    
    // Group Rules Section Locators
    private static final Locator GROUP_RULE_NAME_FIELD = new Locator("GROUP_RULE_NAME_FIELD", By.xpath("//input[contains(@data-testid, 'group-rule-name')]"));
    private static final Locator VERTICAL_PRODUCT_DROPDOWN = new Locator("VERTICAL_PRODUCT_DROPDOWN", By.xpath("//input[contains(@data-testid, 'input-select-group-rule-vertical')]"));
    private static final Locator VARIABLE_NAME_DROPDOWN = new Locator("VARIABLE_NAME_DROPDOWN", By.xpath("//input[@data-testid='input-select-group-rule-item-rule-variable-0']"));
    private static final Locator OPERATOR_DROPDOWN = new Locator("OPERATOR_DROPDOWN", By.xpath("//input[@data-testid='input-select-group-rule-item-rule-operator-0']"));
    private static final Locator VALUE_FIELD = new Locator("VALUE_FIELD", By.xpath("//input[@id='value']"));
    
    // Dynamic Option XPath Template
    private static final String OPTION_XPATH_TEMPLATE = "//span[contains(text(), '%s')]";
    private static final String PREMIUM_NAME_XPATH_TEMPLATE = "//div[contains(@class, 'premium') or contains(@class, 'name')]//td[contains(text(), '%s')]";

    // Helper method to create dynamic option locators
    private Locator getDynamicOptionLocator(String optionText) {
        return new Locator("DYNAMIC_OPTION", By.xpath(String.format(OPTION_XPATH_TEMPLATE, optionText)));
    }

    private Locator getDynamicPremiumNameLocator(String premiumName) {
        return new Locator("PREMIUM_NAME", By.xpath(String.format(PREMIUM_NAME_XPATH_TEMPLATE, premiumName)));
    }

    // Upload Status Popup Locators
    private static final Locator UPLOAD_STATUS_POPUP = new Locator("UPLOAD_STATUS_POPUP", By.xpath("//div[contains(@class, 'modal') or contains(@class, 'popup')]//h2[contains(text(), 'Upload Status')]"));
    private static final Locator UPLOAD_COMPLETED_STATUS = new Locator("UPLOAD_COMPLETED_STATUS", By.xpath("//span[contains(text(), 'Completed')]"));
    private static final Locator CONFIRM_BUTTON = new Locator("CONFIRM_BUTTON", By.xpath("//button[normalize-space(text())='Confirm']"));
    private static final Locator PROGRESS_BAR_LOADING = new Locator("PROGRESS_BAR_LOADING", By.xpath("//div[contains(@class, 'PremiumUploadProcess_premium_upload_process_header_loading_bar_process')]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[@data-testid='credential-submit-button']"));
    // Try multiple fallback locators for delete button
    private static final Locator DELETE_LAST_PREMIUM_BUTTON = new Locator("DELETE_LAST_PREMIUM_BUTTON", By.xpath("(//*[contains(@data-testid,'credential-delete')])[last()]"));
    private static final Locator CONFIRM_DELETE_BUTTON = new Locator("CONFIRM_DELETE_BUTTON", By.xpath("//button[contains(text(), 'Delete') or contains(text(), 'Confirm')]"));

    public void filterByInsuranceName(String insuranceName) {
        driverTools.waitForElementToAppear(INSURANCE_GROUP_FILTER_CONTAINER, 10);
        driverTools.clickWithJavascriptExecutor(INSURANCE_GROUP_FILTER_CONTAINER);
        driverTools.dummyWait(1);
        driverTools.waitForElementToAppear(INSURANCE_GROUP_FILTER_INPUT, 5);
        driverTools.clear(INSURANCE_GROUP_FILTER_INPUT);
        driverTools.sendKeys(INSURANCE_GROUP_FILTER_INPUT, insuranceName);
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
        driverTools.dummyWait(3);
    }

    public void clickChevronExpand() {
        driverTools.dummyWait(3);
        driverTools.scrollToElement(CHEVRON_EXPAND);
        driverTools.click(CHEVRON_EXPAND);
        driverTools.dummyWait(2);
    }

    public void clickMoreButton() {
        driverTools.clickWithJavascriptExecutor(MORE_BUTTON);
        driverTools.dummyWait(2);
    }

    public void clickEditButton() {
        driverTools.waitForElementToAppear(EDIT_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(EDIT_BUTTON);
        driverTools.dummyWait(1);
    }

    public boolean isOnEditPage() {
        driverTools.dummyWait(3);
        return driver.getCurrentUrl().contains("edit") || 
               driverTools.isElementPresent(ADD_NEW_PREMIUM_BUTTON);
    }

    public void clickAddNewPremium() {
        driverTools.waitForElementToAppear(ADD_NEW_PREMIUM_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(ADD_NEW_PREMIUM_BUTTON);
        driverTools.dummyWait(1);
    }

    public void inputPremiumName(String premiumName) {
        driverTools.waitForElementToAppear(PREMIUM_NAME_FIELD);
        driverTools.sendKeys(PREMIUM_NAME_FIELD, premiumName);
    }

    // Generic helper methods to reduce code duplication
    private void selectFromDropdown(Locator dropdown, String option) {
        driverTools.scrollToElement(dropdown);
        driverTools.click(dropdown);
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(getDynamicOptionLocator(option));
        driverTools.dummyWait(1);
    }

    private void inputToField(Locator field, String value) {
        driverTools.scrollToElement(field);
        driverTools.clear(field);
        driverTools.sendKeys(field, value);
        driverTools.dummyWait(1);
    }

    private void selectFromDropdownWithJS(Locator dropdown, String option) {
        driverTools.clickWithJavascriptExecutor(dropdown);
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(getDynamicOptionLocator(option));
        driverTools.dummyWait(1);
    }

    public void selectFormula(String formulaOption) {
        selectFromDropdown(FORMULA_DROPDOWN, formulaOption);
    }

    public void selectCurrency(String currencyOption) {
        selectFromDropdown(CURRENCY_DROPDOWN, currencyOption);
    }

    public void selectUoM(String uomOption) {
        selectFromDropdown(UOM_DROPDOWN, uomOption);
    }

    public void selectUoMDiscount(String uomDiscountOption) {
        selectFromDropdown(UOM_DISCOUNT_DROPDOWN, uomDiscountOption);
    }

    public void inputBrokerFee(String brokerFee) {
        inputToField(BROKER_FEE_FIELD, brokerFee);
    }

    public void inputCommissionFee(String commissionFee) {
        inputToField(COMMISSION_FEE_FIELD, commissionFee);
    }

    public void inputAdminFee(String adminFee) {
        inputToField(ADMIN_FEE_FIELD, adminFee);
    }

    public void inputUoMDiscountAmount(String uomDiscountAmount) {
        inputToField(UOM_DISCOUNT_AMOUNT_FIELD, uomDiscountAmount);
    }

    public void clickNextButton() {
        driverTools.dummyWait(3);
        driverTools.scrollToElement(NEXT_BUTTON);
        driverTools.clickWithJavascriptExecutor(NEXT_BUTTON);
        driverTools.dummyWait(1);
    }

    public void fillAllMandatoryFields(String premiumName, String formula, String currency, String uom, String uomDiscount, String uomDiscountAmount, String brokerFee, String commissionFee, String adminFee) {
        inputPremiumName(premiumName);
        selectFormula(formula);
        selectCurrency(currency);
        selectUoM(uom);
        selectUoMDiscount(uomDiscount);
        inputUoMDiscountAmount(uomDiscountAmount);
        inputBrokerFee(brokerFee);
        inputCommissionFee(commissionFee);
        inputAdminFee(adminFee);
    }
    
    // Group Rules Methods
    public void inputGroupRuleName(String groupRuleName) {
        driverTools.waitForElementToAppear(GROUP_RULE_NAME_FIELD, 5);
        driverTools.scrollToElement(GROUP_RULE_NAME_FIELD);
        driverTools.clear(GROUP_RULE_NAME_FIELD);
        driverTools.sendKeys(GROUP_RULE_NAME_FIELD, groupRuleName);
        driverTools.dummyWait(1);
    }
    
    public void selectVerticalProduct(String verticalProduct) {
        selectFromDropdownWithJS(VERTICAL_PRODUCT_DROPDOWN, verticalProduct);
    }

    public void selectVariableName(String variableName) {
        selectFromDropdownWithJS(VARIABLE_NAME_DROPDOWN, variableName);
    }

    public void selectOperator(String operator) {
        selectFromDropdownWithJS(OPERATOR_DROPDOWN, operator);
    }
    
    public void inputValue(String value) {
        driverTools.waitForElementToAppear(VALUE_FIELD);
        driverTools.clickWithJavascriptExecutor(VALUE_FIELD);
        driverTools.dummyWait(1);
        driverTools.sendKeys(VALUE_FIELD, value);
        driverTools.dummyWait(3);
    }
    
    public void fillGroupRules(String groupRuleName, String verticalProduct, String variableName, String operator, String value) {
        inputGroupRuleName(groupRuleName);
        selectVerticalProduct(verticalProduct);
        selectVariableName(variableName);
        selectOperator(operator);
        inputValue(value);
    }

    public boolean isUploadStatusPopupDisplayed() {
        driverTools.dummyWait(2);
        return driverTools.isElementPresent(UPLOAD_STATUS_POPUP) || 
               driverTools.isElementPresent(UPLOAD_COMPLETED_STATUS);
    }

    public boolean isUploadCompleted() {
        driverTools.dummyWait(3);
        return driverTools.isElementPresent(UPLOAD_COMPLETED_STATUS);
    }

    public void waitForPageLoad() {
        driverTools.dummyWait(5);
    }

    public void clickConfirmButton() {
        driverTools.waitForElementToAppear(CONFIRM_BUTTON, 10);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.clickWithJavascriptExecutor(CONFIRM_BUTTON);
        driverTools.dummyWait(1);
    }

    public void clickSaveButton() {
        driverTools.dummyWait(3);
        driverTools.waitForElementToAppear(SAVE_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
        driverTools.dummyWait(1);
    }

    public boolean isPremiumCreated(String premiumName) {
        driverTools.dummyWait(3);
        return driverTools.isElementPresent(getDynamicPremiumNameLocator(premiumName));
    }

    public void deletePremium() {
        if (driverTools.isElementPresent(DELETE_LAST_PREMIUM_BUTTON)) {
            WebElement deleteBtn = driverTools.element(DELETE_LAST_PREMIUM_BUTTON).orElseThrow();
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));",
                    deleteBtn);
            driverTools.dummyWait(2);
            driverTools.clickWithJavascriptExecutor(CONFIRM_DELETE_BUTTON);
            driverTools.dummyWait(3);
        } else {
            throw new RuntimeException("Could not find delete button for last premium");
        }
    }

    public boolean verifyPremiumDeleted(String premiumName) {
        driverTools.dummyWait(2);
        return !driverTools.isElementPresent(getDynamicPremiumNameLocator(premiumName));
    }
    
}
