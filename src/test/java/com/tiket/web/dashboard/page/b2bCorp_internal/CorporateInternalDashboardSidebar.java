package com.tiket.web.dashboard.page.b2bCorp_internal;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.List;

import static org.hamcrest.Matchers.is;

public class CorporateInternalDashboardSidebar<T extends WebDriver> extends BasePage<T> {
    private static final Locator CORPORATE_INFORMATION = new Locator("CORPORATE_INFORMATION", new By.ByXPath("//span[text()='Corporate Information']"));
    private static final Locator REGISTERED_CORPORATE = new Locator("REGISTERED_CORPORATE", new By.ByXPath("//span[@title='Registered Corporate']"));
    private static final Locator CORPORATE_PAYMENT_PREFERENCE = new Locator("CORPORATE_PAYMENT_PREFERENCE", new By.ByXPath("//span[@title='Corporate Payment Preference']"));
    private static final Locator DEPOSIT = new Locator("DEPOSIT", new By.ByXPath("//span[@title='Deposit']"));
    private static final Locator REQUEST_TOPUP = new Locator("REQUEST_TOPUP", new By.ByXPath("//button[normalize-space()='Request Top-Up']"));
    private static final Locator DROPDOWN_CORP_NAME = new Locator("DROPDOWN_CORP_NAME", new By.ByXPath("//*[@id='corporate-name' and contains(@class,'styles_has_clickable')]"));
    private static final Locator  CORPORATE_DEAL= new Locator("CORPORATE_DEAL", new By.ByXPath("//span[@title='Corporate Deal']"));
    private static final Locator  CLICK_CHA= new Locator("CLICK_CHA", new By.ByXPath("//span[@title='Garuda CHA Codes']"));
    private static final Locator  ACTIVE_NUM= new Locator("ACTIVE_NUM", new By.ByXPath("//td[normalize-space()='1']"));
    private static final Locator  INACTIVE= new Locator("INACTIVE", new By.ByXPath("//span[contains(text(),'Inactive')]"));
    private static final Locator  DETAILS= new Locator("DETAILS", new By.ByXPath("//button[normalize-space()='Details']"));
    private static final Locator  APPROVE= new Locator("APPROVE", new By.ByXPath("//button[normalize-space()='Approve']"));
    private static final Locator  CONTINUE= new Locator("CONTINUE", new By.ByXPath("//button[text()='Yes, continue']"));
    private static final Locator SELECT_CORPORATE = new Locator("SELECT_CORPORATE", new By.ByXPath("//div[contains(@role,'presentation')]//div[1]//span[1]//span[27]"));
    private static final Locator CLICK_SUBMIT = new Locator("CLICK_SUBMIT", new By.ByXPath("//button[normalize-space()='Submit']"));
    private static final Locator AMOUNT_FIELD = new Locator("AMOUNT_FIELD", new By.ByXPath("//input[@id='requested-amount']"));
    private static final Locator UPLOAD_PROOF = new Locator("UPLOAD_PROOF", new By.ByXPath("//input[@type='file']"));
    private static final Locator SUBMIT = new Locator("SUBMIT", new By.ByXPath("//button[normalize-space()='Submit']"));
    private static final Locator CORPORATE_DEPOSIT_REQUEST_1 = new Locator("CORPORATE_DEPOSIT_REQUEST_1", new By.ByXPath("//span[contains(@class,'transition-all') and text()='Corporate Deposit Request']"));
    private static final Locator CORPORATE_DEPOSIT_REQUEST_2 = new Locator("CORPORATE_DEPOSIT_REQUEST_2", new By.ByXPath("//span[contains(@class,'w-full') and text()='Corporate Deposit Request']"));
    private static final Locator ACTIVE_PAGE = new Locator("ACTIVE_PAGE", new By.ByXPath("//*[text()='Active']"));
    private static final Locator ACTION_DOTS = new Locator("ACTION_DOTS", new By.ByXPath("//*[local-name()='svg' and contains(@class, 'cursor-pointer')]"));
    private static final Locator ACTION_ACTIVATE = new Locator("ACTION_ACTIVATE", new By.ByXPath("//*[text()='Activate']"));
    private static final Locator CHA_NAME = new Locator("CHA_NAME", new By.ByXPath("(//table//tr[1])//td[2]"));
    private static final Locator ACTIVATE_BUTTON = new Locator("ACTIVATE_BUTTON", new By.ByXPath("//button//span[text()='Activate']"));
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[@class='account-card']"));
    private static final Locator CODE_SEARCHBAR = new Locator("CODE_SEARCHBAR", By.id("enter-the-code"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button//span[text()='Search']"));
    private static final Locator ACTIVE_CHA_NAME = new Locator("ACTIVE_CHA_NAME", new By.ByXPath("(//table//tr[5])//td[2]"));
    private static final Locator ACTIVE_ACTION_DOTS = new Locator("ACTIVE_ACTION_DOTS", new By.ByXPath("(//*[name()='svg'][@class='cursor-pointer'])[1]"));
    private static final Locator ACTION_DEACTIVATE = new Locator("ACTION_DEACTIVATE", new By.ByXPath("(//*[text()='Deactivate'])[5]"));
    private static final Locator DEACTIVATE_BUTTON = new Locator("DEACTIVATE_BUTTON", new By.ByXPath("//button//span[text()='Deactivate']"));
    private static final Locator DOCUMENT_SETTING = new Locator("DOCUMENT_SETTING", new By.ByXPath("//span[contains(text(), 'Document Setting')]"));
    private static final Locator TIKET_PLUS_FOR_CORPORATE = new Locator("TIKET_PLUS_FOR_CORPORATE", new By.ByXPath("//span[text()='tiketPlus for Corporate']"));
    private static final Locator QUOTATION_LIST = new Locator("QUOTATION_LIST", new By.ByXPath("//span[text()='Quotation List']"));
    private static final Locator REQUEST_LIST = new Locator("REQUEST_LIST", new By.ByXPath("//span[text()='Request List']"));
    private static final Locator INVOICE_AND_BILLING = new Locator("INVOICE_AND_BILLING", By.xpath("//button[.//span[contains(@title,'Billing')]]"));
    private static final Locator BOOKING_MANAGEMENT = new Locator("BOOKING_MANAGEMENT", new By.ByXPath("//span[@title='Booking Management']"));

    public CorporateInternalDashboardSidebar(Session<T> session) {
        super(session);
    }

    public void expandCorporateInformation() {
        driverTools.maximizeWindow();
        driverTools.waitForElementToAppear(CORPORATE_INFORMATION);
        driverTools.clickWithJavascriptExecutor(CORPORATE_INFORMATION);
        driverTools.waitForElementToAppear(REGISTERED_CORPORATE);
        if(!driverTools.isElementDisplayed(REGISTERED_CORPORATE)){
            driverTools.clickWithJavascriptExecutor(CORPORATE_INFORMATION);
        }
    }

    public void openRegisteredCorporate() {
        driverTools.clickWithJavascriptExecutor(REGISTERED_CORPORATE);
    }

    public void expandCorporatePaymentPreference(){
        driverTools.maximizeWindow();
        driverTools.click(CORPORATE_PAYMENT_PREFERENCE);
        driverTools.waitForElementToAppear(DEPOSIT);
        driverTools.click(DEPOSIT);
    }
    public void expandCorporateDepositRequest(){
        driverTools.maximizeWindow();
        driverTools.waitForElementToAppear(CORPORATE_DEPOSIT_REQUEST_1);
        driverTools.clickWithJavascriptExecutor(CORPORATE_DEPOSIT_REQUEST_1);
        driverTools.waitForElementToAppear(CORPORATE_DEPOSIT_REQUEST_2);
        driverTools.clickWithJavascriptExecutor(CORPORATE_DEPOSIT_REQUEST_2);
    }

    public void depositFlow(){
        driverTools.maximizeWindow();
        driverTools.waitForElementToAppear(REQUEST_TOPUP);
        driverTools.click(REQUEST_TOPUP);
        driverTools.waitForElementToAppear(DROPDOWN_CORP_NAME);
        driverTools.click(DROPDOWN_CORP_NAME);
        driverTools.waitForElementToAppear(SELECT_CORPORATE);
        driverTools.click(SELECT_CORPORATE);
        driverTools.click(CLICK_SUBMIT);
    }

    public void approveRequest(){
        driverTools.waitForElementToAppear(DETAILS);
        if(driverTools.isElementDisplayed(DETAILS)) {
            driverTools.click(DETAILS);
            driverTools.waitForElementToAppear(APPROVE);
            driverTools.click(APPROVE);
            driverTools.waitForElementToAppear(CONTINUE);
            driverTools.click(CONTINUE);
        }
    }
    public void enterDetails(String amount, String filePath){
        driverTools.waitForElementToAppear(AMOUNT_FIELD);
        driverTools.click(AMOUNT_FIELD);
        driverTools.sendKeys(AMOUNT_FIELD, amount);
        File data = new File(filePath);
        driverTools.scrollIntoWebView(UPLOAD_PROOF);
        driverTools.sendKeys(UPLOAD_PROOF, data.getAbsolutePath());
        driverTools.clickWithJavascriptExecutor(SUBMIT);
    }
    public void expandCorporateDeal()
    {
        driverTools.waitForElementToAppear(CORPORATE_DEAL);
        driverTools.click(CORPORATE_DEAL);
        driverTools.waitForElementToAppear(CLICK_CHA);
        driverTools.click(CLICK_CHA);
    }
    public void checkInactiveCode(){
        driverTools.waitForElementToAppear(INACTIVE);
        driverTools.click(INACTIVE);
    }

    public boolean isPageOpened() {
        driverTools.waitForElementToAppear(ACTIVE_NUM,5);
        return  driverTools.isElementDisplayed(ACTIVE_NUM);
    }

    public String activateCode(){
        driverTools.waitForElementToAppear(ACTION_DOTS);
        driverTools.click(ACTION_DOTS);
        String code=driverTools.getText(CHA_NAME).orElseThrow();
        driverTools.clickWithJavascriptExecutor(ACTION_ACTIVATE);
        driverTools.clickWithJavascriptExecutor(ACTIVATE_BUTTON);
        return code;
    }

    private Locator getCHAcodeLocator(String code){
        return new Locator("CHA_CODE", new By.ByXPath("//*[text()='"+code+"']"));
    }
    public boolean isCodePresent(String text){
        driverTools.waitForElementToAppear(ACTIVE_PAGE,2);
        Locator codeLocator=getCHAcodeLocator(text);
        driverTools.waitForElementToAppear(codeLocator,2);
        return driverTools.isElementDisplayed(codeLocator);
    }

    public void gotoActivePage(){
        driverTools.click(ACTIVE_PAGE);
    }

    public boolean isB2BLoggedIn() {
        driverTools.waitForElementToAppear(USERNAME_GREETING, 3);
        return driverTools.isElementDisplayed(USERNAME_GREETING);
    }

    public void searchCode(String code){
        driverTools.waitForElementsToAppear(CODE_SEARCHBAR);
        driverTools.sendKeys(CODE_SEARCHBAR, code);
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
    }

    public String deactivateCode(){
        driverTools.waitForElementToAppear(ACTIVE_ACTION_DOTS);
        driverTools.click(ACTIVE_ACTION_DOTS);
        String code=driverTools.getText(ACTIVE_CHA_NAME).orElseThrow();
        driverTools.clickWithJavascriptExecutor(ACTION_DEACTIVATE);
        driverTools.clickWithJavascriptExecutor(DEACTIVATE_BUTTON);
        return code;
    }

    public void goToDocumentSetting(){
        driverTools.waitForElementToAppear(CORPORATE_INFORMATION);
        driverTools.clickWithJavascriptExecutor(CORPORATE_INFORMATION);
        driverTools.waitForElementToAppear(DOCUMENT_SETTING);
        driverTools.clickWithJavascriptExecutor(DOCUMENT_SETTING);
        driverTools.waitForElementToAppear(REQUEST_TOPUP,3);
    }

    public void expandTiketPlusForCorporate() {
        driverTools.maximizeWindow();
        driverTools.waitForElementToAppear(TIKET_PLUS_FOR_CORPORATE);
        driverTools.clickWithJavascriptExecutor(TIKET_PLUS_FOR_CORPORATE);
        driverTools.waitForElementToAppear(QUOTATION_LIST);
        if (!driverTools.isElementDisplayed(QUOTATION_LIST)) {
            driverTools.clickWithJavascriptExecutor(TIKET_PLUS_FOR_CORPORATE);
        }
    }

    public void openQuotationList() {
        driverTools.waitForElementToBeClickable(QUOTATION_LIST);
        driverTools.clickWithJavascriptExecutor(QUOTATION_LIST);
    }

    public void openRequestList() {
        driverTools.waitForElementToBeClickable(REQUEST_LIST);
        Assertion.assertThat("Verify Request List option is visible",driverTools.isElementDisplayed(REQUEST_LIST),is(true));
        driverTools.clickWithJavascriptExecutor(REQUEST_LIST);
    }

    private static Locator getChaLocatorByHeading(String code){
        return new Locator("CLICK_%s_CHA".formatted(code), new By.ByXPath("//span[@title='%s CHA Codes']".formatted(code)));
    }

    public void expandCorporateDealAndClickCHACodeByHeading(String code){
        driverTools.waitForElementToAppear(CORPORATE_DEAL);
        driverTools.click(CORPORATE_DEAL);

        Locator codeLocator = getChaLocatorByHeading(code);
        driverTools.waitForElementToAppear(codeLocator);
        driverTools.click(codeLocator);
    }

    private static final Locator TABLE_HEADINGS = new Locator("TABLE_HEADINGS", By.xpath("//table//thead//th"));

    private static Locator getTableColumnLocator(int index){
        return new Locator("TABLE_COLUMN_" + index, By.xpath("//table//tbody//tr//td[%d]".formatted(index)));
    }

    public int getTableHeadingIndex(String heading) {
        driverTools.waitForElementToAppear(TABLE_HEADINGS);
        List<WebElement> headings = driverTools.elements(TABLE_HEADINGS).orElseThrow(() -> new IllegalStateException("No headings found"));
        if (headings.isEmpty()) {
            return -1;
        }

        for (int i = 0; i < headings.size(); i++) {
            String text = headings.get(i).getText().trim();
            if (text.isEmpty()) {
                text = headings.get(i).getAttribute("textContent").trim();
            }
            if (text.equalsIgnoreCase(heading)) {
                return i;
            }
        }

        return -1;
    }

    public String getTableValueByHeading(String heading) {
        int index = getTableHeadingIndex(heading);
        if (index == -1) {
            logger.log("Heading: %s not found returning empty value".formatted(heading));
            return "";
        }

        Locator COLUMN_VALUES = getTableColumnLocator(index + 1);
        driverTools.waitForElementToAppear(COLUMN_VALUES);
        int retry = 5;
        while (retry-- > 0 && driverTools.getAttributeTextContent(COLUMN_VALUES).orElseThrow().trim().isEmpty()){
            driverTools.dummyWait(1);
        }

        String text =  driverTools.getText(COLUMN_VALUES).orElseThrow().trim();
        if (text.isEmpty()) {
            text = driverTools.getAttributeTextContent(COLUMN_VALUES).orElseThrow().trim();
        }

        logger.log("Got (%s) value for %s".formatted(text, heading));
        return text;
    }

    public void goToInvoiceAndBilling() {
        driverTools.waitForElementToAppear(INVOICE_AND_BILLING, 10);
        try {
            driverTools.click(INVOICE_AND_BILLING);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(INVOICE_AND_BILLING);
        }
    }

    public void openBookingManagement() {
        driverTools.waitForElementToBeClickable(BOOKING_MANAGEMENT);
        driverTools.clickWithJavascriptExecutor(BOOKING_MANAGEMENT);
    }
}
