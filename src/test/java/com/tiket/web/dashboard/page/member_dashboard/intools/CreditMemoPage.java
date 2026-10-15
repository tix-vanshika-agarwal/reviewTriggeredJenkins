package com.tiket.web.dashboard.page.member_dashboard.intools;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CreditMemoPage<T extends WebDriver> extends BasePage<T> {

    public CreditMemoPage(Session<T> session) {
        super(session);
    }

    private static final Locator NEW_CREDIT_MEMO_BUTTON = new Locator("NEW_CREDIT_MEMO_BUTTON", By.xpath("//button//span[text()='Add Credit Memo']"));
    private static final Locator ADD_CREDIT_MEMO_HEADING = new Locator("ADD_CREDIT_MEMO_HEADING", By.xpath("//h2[text()='Add Credit memo']"));
    private static final Locator SOURCE_INPUT = new Locator("SOURCE_INPUT", By.xpath("//input[@data-testid='credit-memo-create-source']"));
    private static final Locator SOURCE_OPTION = new Locator("SOURCE_OPTION", By.xpath("//div[contains(@class,'pop')]//*[contains(text(), 'Purchase Order')]"));
    private static final Locator SOURCE_ID_INPUT = new Locator("SOURCE_ID_INPUT", By.xpath("//input[@data-testid='credit-memo-create-source-id']"));
    private static final Locator SOURCE_ID_OPTION = new Locator("SOURCE_ID_OPTION", By.xpath("(//div[@id='sourceId']//div[contains(@class,'popoverlist_list')])[1]"));
    private static final Locator TYPE_INPUT = new Locator("TYPE_INPUT", By.xpath("//input[@data-testid='credit-memo-create-type']"));
    private static final Locator TYPE_OPTION = new Locator("TYPE_OPTION", By.xpath("(//div[@id='type']//div[contains(@class,'popoverlist_list')])[1]"));
    private static final Locator AMOUNT_INPUT = new Locator("AMOUNT_INPUT", By.xpath("//input[@data-testid='credit-memo-create-amount']"));
    private static final Locator CREATE_BUTTON = new Locator("CREATE_BUTTON", By.xpath("//button[@form='credit-memo-create-form']"));
    private static final Locator SNACKBAR_TEXT = new Locator("SNACKBAR_TEXT", By.xpath("//div[contains(@class, 'Snackbar_text_content')]"));
    private static final Locator CREATED_ROW_DATE_TIME = new Locator("CREATED_ROW_DATE_TIME", By.xpath("(//table//tr[td])[1]/td[7]"));
    private static final Locator CREATED_ROW_PRICE = new Locator("CREATED_ROW_PRICE", By.xpath("(//table//tr[td])[1]/td[9]"));
    private static final Locator ROW_ACTION_MORE = new Locator("ROW_ACTION_MORE", By.xpath("(//button[contains(@class,'supplierId__action_more')])[1]"));
    private static final Locator EDIT_ACTION = new Locator("EDIT_ACTION", By.xpath("//button[@role='menuitem']//span[text()='Edit']"));
    private static final Locator CONFIRM_UPDATE_HEADING = new Locator("CONFIRM_UPDATE_HEADING", By.xpath("//h2[text()='Confirm Credit Memo Update']"));
    private static final Locator UPDATE_REASON_TEXTAREA = new Locator("UPDATE_REASON_TEXTAREA", By.xpath("//textarea[@name='updateReason']"));
    private static final Locator CONFIRM_UPDATE_BUTTON = new Locator("CONFIRM_UPDATE_BUTTON", By.xpath("//button[normalize-space(text())='Confirm Update']"));
    private static final Locator EDIT_CREDIT_MEMO_HEADING = new Locator("EDIT_CREDIT_MEMO_HEADING", By.xpath("//h2[text()='Edit Credit Memo']"));
    private static final Locator VOID_BUTTON = new Locator("VOID_BUTTON", By.xpath("//button[text()='Void']"));
    private static final Locator VOID_HEADING = new Locator("VOID_HEADING", By.xpath("//h2[text()='Void Credit Memo?']"));
    private static final Locator VOID_REASON_TEXTAREA = new Locator("VOID_REASON_TEXTAREA", By.xpath("//textarea[@name='voidReason']"));
    private static final Locator CONFIRM_VOID_BUTTON = new Locator("CONFIRM_VOID_BUTTON", By.xpath("//button[normalize-space(text())='Confirm Void']"));
    private static final Locator FIRST_ROW_STATUS_CANCELLED = new Locator("FIRST_ROW_STATUS_CANCELLED", By.xpath("(//table//tr[td])[1]//*[contains(text(),'Cancelled')]"));
    private static final Locator STATUS_FILTER = new Locator("STATUS_FILTER", By.xpath("//span[normalize-space()='Status']//button"));
    private static final Locator STATUS_AVAILABLE_OPTION = new Locator("STATUS_AVAILABLE_OPTION", By.xpath("//div[contains(@class,'Dropdown_options_list_')]//div[text()='Available']"));
    private static final Locator APPLY_FILTER_BUTTON = new Locator("APPLY_FILTER_BUTTON", By.xpath("//button[text()='Apply']"));
    private static final Locator FIRST_ROW_STATUS_AVAILABLE = new Locator("FIRST_ROW_STATUS_AVAILABLE", By.xpath("(//table//tr[td])[1]//*[contains(text(),'Available')]"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", By.xpath("//button[text()='Reset']"));

    public void clickNewCreditMemoButton() {
        driverTools.waitForElementToAppear(NEW_CREDIT_MEMO_BUTTON);
        driverTools.click(NEW_CREDIT_MEMO_BUTTON);
    }

    public boolean isAddCreditMemoPopupDisplayed() {
        driverTools.waitForElementToAppear(ADD_CREDIT_MEMO_HEADING);
        return driverTools.isElementDisplayed(ADD_CREDIT_MEMO_HEADING);
    }

    public void selectSource() {
        driverTools.click(SOURCE_INPUT);
        driverTools.waitForElementToAppear(SOURCE_OPTION);
        driverTools.click(SOURCE_OPTION);
    }

    public void selectSourceId() {
        driverTools.click(SOURCE_ID_INPUT);
        driverTools.waitForElementToAppear(SOURCE_ID_OPTION);
        driverTools.click(SOURCE_ID_OPTION);
    }

    public void selectType() {
        driverTools.click(TYPE_INPUT);
        driverTools.waitForElementToAppear(TYPE_OPTION);
        driverTools.click(TYPE_OPTION);
    }

    public void enterAmount(String amount) {
        driverTools.click(AMOUNT_INPUT);
        driverTools.sendKeys(AMOUNT_INPUT, amount);
    }

    public void clickCreateButton() {
        driverTools.click(CREATE_BUTTON);
    }

    public String getSnackbarText() {
        driverTools.waitForElementToAppear(SNACKBAR_TEXT);
        return driverTools.getText(SNACKBAR_TEXT).orElse("");
    }

    public String getCreatedRowDateTime() {
        driverTools.waitForElementToAppear(CREATED_ROW_DATE_TIME);
        return driverTools.getText(CREATED_ROW_DATE_TIME).orElse("").trim();
    }

    public String getCreatedRowPrice() {
        driverTools.waitForElementToAppear(CREATED_ROW_PRICE);
        return driverTools.getText(CREATED_ROW_PRICE).orElse("").trim();
    }

    public void clickRowActionMore() {
        driverTools.scrollIntoWebView(ROW_ACTION_MORE);
        driverTools.clickWithJavascriptExecutor(ROW_ACTION_MORE);
    }

    public void clickEditAction() {
        driverTools.waitForElementToAppear(EDIT_ACTION);
        driverTools.click(EDIT_ACTION);
    }

    public void updateAmount(String amount) {
        driverTools.click(AMOUNT_INPUT);
        driverTools.clearInput(AMOUNT_INPUT);
        driverTools.sendKeys(AMOUNT_INPUT, amount);
    }

    public boolean isConfirmUpdatePopupDisplayed() {
        driverTools.waitForElementToAppear(CONFIRM_UPDATE_HEADING);
        return driverTools.isElementDisplayed(CONFIRM_UPDATE_HEADING);
    }

    public void enterUpdateReason(String reason) {
        driverTools.sendKeys(UPDATE_REASON_TEXTAREA, reason);
    }

    public void clickConfirmUpdateButton() {
        driverTools.click(CONFIRM_UPDATE_BUTTON);
    }

    public boolean isEditCreditMemoPopupDisplayed() {
        driverTools.waitForElementToAppear(EDIT_CREDIT_MEMO_HEADING);
        return driverTools.isElementDisplayed(EDIT_CREDIT_MEMO_HEADING);
    }

    public void clickVoidButton() {
        driverTools.waitForElementToAppear(VOID_BUTTON);
        driverTools.click(VOID_BUTTON);
    }

    public boolean isVoidPopupDisplayed() {
        driverTools.waitForElementToAppear(VOID_HEADING);
        return driverTools.isElementDisplayed(VOID_HEADING);
    }

    public void enterVoidReason(String reason) {
        driverTools.sendKeys(VOID_REASON_TEXTAREA, reason);
    }

    public void clickConfirmVoidButton() {
        driverTools.click(CONFIRM_VOID_BUTTON);
    }

    public boolean isFirstRowStatusCancelled() {
        driverTools.waitForElementToAppear(FIRST_ROW_STATUS_CANCELLED);
        return driverTools.isElementDisplayed(FIRST_ROW_STATUS_CANCELLED);
    }

    public void filterByStatusAvailable() {
        driverTools.click(STATUS_FILTER);
        driverTools.waitForElementToAppear(STATUS_AVAILABLE_OPTION);
        driverTools.click(STATUS_AVAILABLE_OPTION);
        driverTools.click(APPLY_FILTER_BUTTON);
    }

    public boolean isFirstRowStatusAvailable() {
        driverTools.waitForElementToAppear(FIRST_ROW_STATUS_AVAILABLE);
        return driverTools.isElementDisplayed(FIRST_ROW_STATUS_AVAILABLE);
    }

    public void resetStatusFilter() {
        driverTools.click(STATUS_FILTER);
        driverTools.waitForElementToAppear(RESET_FILTER_BUTTON);
        driverTools.click(RESET_FILTER_BUTTON);
        driverTools.click(APPLY_FILTER_BUTTON);
    }

}
