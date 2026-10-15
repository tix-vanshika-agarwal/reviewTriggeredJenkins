package com.tiket.web.dashboard.page.b2b_external;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hamcrest.Matchers.is;

public class TravelPolicyPage <T extends WebDriver> extends BasePage<T> {
    private static final Locator ADD_POLICY = new Locator("ADD_POLICY", new By.ByXPath("//button//span[contains(text(), 'Add policy')]"));
    private static final Locator CREATE_POLICY = new Locator("CREATE_POLICY", new By.ByXPath("//h2[contains(text(), 'Create travel')]"));
    private static final Locator FIRST_POLICY = new Locator("FIRST_POLICY", new By.ByXPath("//td//p[contains(@class, 'font-bold')]"));
    private static final Locator EDIT_POLICY = new Locator("EDIT_POLICY", new By.ByXPath("//span[text()='Edit' and contains(@class, 'cursor')]"));
    private static final Locator EDIT_FORM_HEADING = new Locator("EDIT_FORM_HEADING", new By.ByXPath("(//div[contains(@class, 'font-bold')])[1]"));
    private static final Locator YOUR_POLICY = new Locator("YOUR_POLICY", new By.ByXPath("//span[contains(text(), 'Your Travel Policy')]"));
    private static final Locator SETTING_BUTTON = new Locator("SETTING_BUTTON", new By.ByXPath("//*[local-name()='svg' and contains(@class, 'mr-2')]"));
    public static final Locator DELETE_POLICY = new Locator("DELETE_POLICY", new By.ByXPath("//div[contains(@class, 'cursor-pointer') and contains(., 'Delete policy')]"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON",By.xpath("//button[contains(@class, 'Button_variant_danger') and normalize-space()='Delete']"));
    private static final Locator DELETE_POLICY_SUCCESS_TOAST = new Locator("DELETE_POLICY_SUCCESS_TOAST", By.xpath("//div[contains(@class, 'SnackbarWrapper_snackbar_wrapper')]"));
    private static final Locator MANAGE_PRODUCTS = new Locator("MANAGE_PRODUCTS", new By.ByXPath("//div[contains(@class, 'cursor-pointer') and normalize-space()='Manage product']"));
    private static final Locator AIRPORT_CHECK_BOX = new Locator("AIRPORT_CHECK_BOX", new By.ByXPath("//span[text()='Airport Transfer']/ancestor::div[contains(@class,'justify-between')]//span[contains(@class,'Check_custom_checkbox')]"));
    private static final Locator TRAIN_CHECK_BOX = new Locator("TRAIN_CHECK_BOX", new By.ByXPath("//span[text()='Train']/ancestor::div[contains(@class,'justify-between')]//span[contains(@class,'Check_custom_checkbox')]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[normalize-space(text())='Save']"));
    private static final Locator MANAGE_SUCCESS_SNACKBAR = new Locator("MANAGE_SUCCESS_SNACKBAR", new By.ByXPath("//div[contains(@class, 'Snackbar_text_content') and contains(text(), 'successfully changed products')]"));
    private static final Locator POLICY_NAME_INPUT = new Locator("POLICY_NAME_INPUT", new By.ByXPath("//input[@placeholder='Enter the policy name']"));
    private static final Locator POLICY_ERROR_MESSAGE = new Locator("POLICY_ERROR_MESSAGE", new By.ByXPath("//div[contains(@class, 'helper_text_container') and contains(@class, 'state_danger')]"));
    public static final Locator NEXT_BUTTON = new Locator(
            "NEXT_BUTTON",
            By.xpath("//button[contains(@class, 'Button_variant_primary') and normalize-space(text())='Next']")
    );
    private static final Locator CHECK_HOTEL = new Locator("CHECK_HOTEL", new By.ByXPath("   (//label[contains(@class, 'Check')])[1]"));
    private static final Locator NEXT_DRAFT = new Locator("NEXT_DRAFT", new By.ByXPath("//button[contains(@class, 'Button_variant_primary')]"));
    private static final Locator SKIP_BUTTON = new Locator("SKIP_BUTTON", new By.ByXPath("//section//button[contains(@class, 'Button_variant_secondary')]"));
    private static final Locator SEARCH_POLICY = new Locator("SEARCH_POLICY", new By.ByXPath("//input[@id='search-policy']"));
    private static final Locator REFRESH = new Locator("REFRESH", new By.ByXPath("//button[normalize-space()='Refresh']"));

    public TravelPolicyPage(Session<T> session) {
        super(session);
    }

    public boolean isTravelPolicyPageOpened(){
        return driverTools.waitForElementToAppear(FIRST_POLICY).isPresent();
    }

    public void clickAddPolicy(){
        driverTools.click(ADD_POLICY);
    }

    public boolean isCreatePolicyPopup(){
        return driverTools.waitForElementToAppear(CREATE_POLICY).isPresent();
    }

    public Locator getLocator(String text) {
        return new Locator("DYNAMIC_TEXT_LOCATOR", new By.ByXPath("//thead//*[contains(text(), '" + text + "')]"));
    }

    public boolean isFieldPresent(String text){
        Locator FieldLocator=getLocator(text);
        return driverTools.waitForElementToAppear(FieldLocator).isPresent();
    }

    public void openPolicyDetail(){
        driverTools.waitForElementToAppear(FIRST_POLICY);
        driverTools.click(FIRST_POLICY);
    }

    public void clickEditPolicy(){
        driverTools.waitForElementToAppear(EDIT_POLICY);
        Assertion.assertThat("Edit button is Present on Travel Policy Detail Page",driverTools.isElementDisplayed(EDIT_POLICY),is(true));
        driverTools.click(EDIT_POLICY);
    }

    public boolean isEditPolicyFormPageOpened(){
        return driverTools.waitForElementToAppear(EDIT_FORM_HEADING).isPresent();
    }

    public boolean AddPolicyButtonNotAppeared(){
        return !driverTools.isElementDisplayed(ADD_POLICY);

    }

    public boolean YourTravelPolicyIsShown(){
        return driverTools.waitForElementToAppear(YOUR_POLICY).isPresent();
    }

    public void clickSettingButton() {
        driverTools.waitForElementToAppear(SETTING_BUTTON);
        WebElement ele = driver.findElement(SETTING_BUTTON.by());
        driverTools.clickSvgOrNonHtmlElements(ele);
    }

    public boolean isDeletePolicyOptionPresent(){
        return driverTools.waitForElementToAppear(DELETE_POLICY).isPresent();
    }

    public void selectDeletePolicy() {
        driverTools.clickWithJavascriptExecutor(DELETE_POLICY);
    }

    public void clickDeleteButton(){
        driverTools.waitForElementToAppear(DELETE_BUTTON, 10);
        driverTools.click(DELETE_BUTTON);
    }

    public boolean isDeletePolicyPopupVisible(){
        driverTools.waitForElementToAppear(DELETE_POLICY_SUCCESS_TOAST, 10);
        return driverTools.isElementDisplayed(DELETE_POLICY_SUCCESS_TOAST);
    }

    public void selectManageProducts(){
        driverTools.click(MANAGE_PRODUCTS);
    }

    public void selectVerticals(){
        driverTools.waitForElementToAppear(AIRPORT_CHECK_BOX,10);
        driverTools.waitForElementToAppear(TRAIN_CHECK_BOX, 10);
        driverTools.click(AIRPORT_CHECK_BOX);
        driverTools.click(TRAIN_CHECK_BOX);
    }

    public void clickSaveButton() {
        driverTools.waitForElementToAppear(SAVE_BUTTON,10);
        driverTools.click(SAVE_BUTTON);
    }

    public boolean isManagePopupVisible(){
        driverTools.waitForElementToAppear(MANAGE_SUCCESS_SNACKBAR, 50);
        return driverTools.isElementDisplayed(MANAGE_SUCCESS_SNACKBAR);
    }

    public void enterPolicyName(String policyName) {
        driverTools.waitForElementToAppear(POLICY_NAME_INPUT, 10);
        driverTools.sendKeys(POLICY_NAME_INPUT, policyName);
        driverTools.click(NEXT_BUTTON);
    }

    public boolean isErrorMessageDisplayed() {
        return driverTools.isElementDisplayed(POLICY_ERROR_MESSAGE);
    }

    public void addNewPolicy(String Name) throws InterruptedException {
        driverTools.waitForElementToAppear(POLICY_NAME_INPUT, 10);
        driverTools.sendKeys(POLICY_NAME_INPUT, Name);
        driverTools.click(NEXT_BUTTON);

        driverTools.click(CHECK_HOTEL);

        driverTools.click(NEXT_BUTTON);

        driverTools.click(NEXT_BUTTON);

        driverTools.waitForElementToAppear(NEXT_DRAFT,5);
        driverTools.click(NEXT_DRAFT);

        driverTools.click(SKIP_BUTTON);

        driverTools.waitForElementToAppear(REFRESH,5);
    }

    public void searchNewPolicy(String Name){
        driverTools.sendKeys(SEARCH_POLICY,Name);
        driverTools.waitForElementToAppear(REFRESH,5);
    }
}
