package com.tiket.web.dashboard.page.platform.sphinx.insurance.variable;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class VariableDashboardPageWeb<T extends WebDriver> extends WebPage<T> {

    public VariableDashboardPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> VariableDashboardPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new VariableDashboardPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new VariableDashboardPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Platform not supported");
        }
    }

    private static final Locator VARIABLE_PAGE = new Locator("VARIABLE_PAGE", By.xpath("//*[@id='sidebar-dashboard']//a[contains(@href,'variable')]"));
    private static final Locator CREATE_NEW_VARIABLE_BUTTON = new Locator("CREATE_NEW_VARIABLE_BUTTON", By.xpath("//a[contains(@href,'/admin/insurance/variable/create')]"));
    private static final Locator VARIABLE_NAME_INPUT = new Locator("VARIABLE_NAME_INPUT", By.xpath("//input[@data-testid='variable-name-input']"));
    private static final Locator VARIABLE_PARAMETER_INPUT = new Locator("VARIABLE_PARAMETER_INPUT", By.xpath("//input[@data-testid='variable-param-input']"));
    private static final Locator USAGE_DROPDOWN = new Locator("USAGE_DROPDOWN", By.xpath("//input[@id='usage' and @placeholder='Usage' and @readonly]"));
    private static final Locator INPUT_TYPE_DROPDOWN = new Locator("INPUT_TYPE_DROPDOWN", By.xpath("//input[@id='input-type' and @placeholder='Input Type']"));
    private static final Locator DATA_TYPE_DROPDOWN = new Locator("DATA_TYPE_DROPDOWN", By.xpath("//input[@data-testid='input-select-variable-data-type-input']"));
    private static final Locator ARITHMETIC_DROPDOWN = new Locator("ARITHMETIC_DROPDOWN", By.xpath("//input[@data-testid='input-select-variable-allowedArithmetics-input' and @id='allowed-arithmetics']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[normalize-space()='Submit Variable']"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_snackbar') and contains(., 'successfully added')]"));
    private static final Locator RANDOM_CLICK = new Locator("RANDOM_CLICK", By.xpath("//button[contains(@class, 'Chip_chip') and contains(@class, 'Chip_active')]//span[contains(normalize-space(.), '(∌) Not Contains')]"));
    private static final Locator ACTION_BUTTON = new Locator("ACTION_BUTTON", By.xpath("(//input[@data-testid='menu-action'])[1]"));
    private static final Locator DELETE_OPTION = new Locator("DELETE_OPTION", By.xpath("//a[contains(@href, 'action=delete')]//span[@data-testid='button-delete' and normalize-space()='Delete']"));
    private static final Locator DELETE_CONFIRMATION = new Locator("DELETE_CONFIRMATION", By.xpath("//button[contains(@class, 'Button_variant_primary__9T6Qr') and normalize-space()='Delete']"));
    private static final Locator TOGGLE_BUTTON = new Locator("TOGGLE_BUTTON", By.xpath("(//div[contains(@class,'Toggle_toggle__hHR11') and contains(@class,'Toggle_toggle_small__W_1BC')])[1]"));
    private static final Locator ACTIVE_BUTTON= new Locator("ACTIVE_BUTTON", By.xpath("//button[contains(@class, 'Button_variant_primary') and normalize-space()='Active']"));
    private static final Locator INACTIVE_BUTTON = new Locator("INACTIVE_BUTTON", By.xpath("//button[contains(@class, 'Button_variant_primary') and normalize-space()='Inactive']"));
    private static final Locator INACTIVE_SNACKBAR = new Locator("INACTIVE_SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_text_content') and contains(text(), 'inactivated the variable')]"));

    public boolean verifyVariablePageIsOpened(){
        driverTools.waitForElementToAppear(VARIABLE_PAGE,10);
        return driverTools.isElementPresent(VARIABLE_PAGE);
    }

    public void clickCreateNewVariableButton(){
        driverTools.clickWithJavascriptExecutor(CREATE_NEW_VARIABLE_BUTTON);
    }

    public void inputVariableName(String variableName) {
        driverTools.waitForElementToAppear(VARIABLE_NAME_INPUT, 10);
        driverTools.sendKeys(VARIABLE_NAME_INPUT, variableName);
    }

    public void inputVariableParameter(String parameter) {
        driverTools.waitForElementToAppear(VARIABLE_PARAMETER_INPUT,10);
        driverTools.sendKeys(VARIABLE_PARAMETER_INPUT, parameter);
    }

    public void selectVariableUsage(String usage) {
        driverTools.click(USAGE_DROPDOWN);
        Locator USAGE_OPTION = new Locator("USAGE_OPTION",
                By.xpath("//div[contains(@class,'SelectItem_select_item_content')]//span[normalize-space()='" + usage + "']"));
        driverTools.scrollToElement(USAGE_OPTION);
        driverTools.click(USAGE_OPTION);
    }

    public void selectVariableInputType(String inputType) {
        driverTools.click(INPUT_TYPE_DROPDOWN);
        Locator TEXT_INPUT_OPTION = new Locator("TEXT_INPUT_OPTION",
                By.xpath("//div[@data-testid='input-select-item-variable-input-type-input']//span[normalize-space()='" + inputType + "']"));
        driverTools.scrollToElement(TEXT_INPUT_OPTION);
        driverTools.click(TEXT_INPUT_OPTION);
    }

    public void selectVariableDataType(String dataType) {
        driverTools.click(DATA_TYPE_DROPDOWN);
        Locator STRING_DATA_TYPE_OPTION = new Locator("STRING_DATA_TYPE_OPTION",By.xpath("//div[@data-testid='input-select-item-variable-data-type-input']//span[normalize-space()='" + dataType + "']"));

        driverTools.scrollToElement(STRING_DATA_TYPE_OPTION);
        driverTools.click(STRING_DATA_TYPE_OPTION);
    }

    public void selectVariableArithmeticType(String arithmeticOption) {
        driverTools.click(ARITHMETIC_DROPDOWN);
        Locator ALL_ARITHMETIC_OPTION = new Locator("ALL_ARITHMETIC_OPTION",
                By.xpath("//div[@data-testid='input-select-item-variable-allowedArithmetics-input']//span[normalize-space()='" + arithmeticOption + "']"));
        driverTools.scrollToElement(ALL_ARITHMETIC_OPTION);
        driverTools.click(ALL_ARITHMETIC_OPTION);
        driverTools.click(RANDOM_CLICK);
    }

    public void clickSubmitButton() {
        driverTools.waitForElementToAppear(SUBMIT_BUTTON,10);
        driverTools.click(SUBMIT_BUTTON);
    }

    public boolean verifySuccessSnackbarIsDisplayed() {
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 10);
        return driverTools.isElementDisplayed(SUCCESS_SNACKBAR);
    }

    public boolean isNewVariableCreated(String variableName) {
        Locator NEW_VARIABLE = new Locator("NEW_VARIABLE", By.xpath("//tr[contains(@class, 'Table_table_tr') and .//td[normalize-space()='" + variableName + "']]"));

        driverTools.waitForElementToAppear(NEW_VARIABLE, 10);
        return driverTools.isElementPresent(NEW_VARIABLE);
    }

    public void resetStateToDefault(){
        driverTools.click(ACTION_BUTTON);

        driverTools.waitForElementToAppear(DELETE_OPTION,10);
        driverTools.click(DELETE_OPTION);

        driverTools.waitForElementToAppear(DELETE_CONFIRMATION,10);
        driverTools.clickWithJavascriptExecutor(DELETE_CONFIRMATION);
    }

    public void setToggle(Locator toggleButton) {
        driverTools.waitForElementToAppear(TOGGLE_BUTTON,10);
        driverTools.click(TOGGLE_BUTTON);

        driverTools.waitForElementToAppear(toggleButton, 10);
        driverTools.click(toggleButton);
    }

    public void makeToggleButtonON(){
        setToggle(ACTIVE_BUTTON);
    }

    public void makeToggleButtonOFF(){
        setToggle(INACTIVE_BUTTON);
    }

    public boolean isToggleButtonOFF() {
        driverTools.waitForElementToAppear(INACTIVE_SNACKBAR, 10);
        return driverTools.isElementDisplayed(INACTIVE_SNACKBAR);
    }
}
