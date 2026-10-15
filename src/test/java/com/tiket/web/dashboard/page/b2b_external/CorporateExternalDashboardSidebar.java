package com.tiket.web.dashboard.page.b2b_external;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;

import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import com.tiket.testbase.Configs;
import org.openqa.selenium.WebDriver;
import java.io.File;

public class CorporateExternalDashboardSidebar <T extends WebDriver> extends BasePage<T> {
    private static final Locator COMPANY_INFORMATION =  new Locator("COMPANY_INFORMATION", new By.ByXPath("//*[text()='Company Information']"));
    private static final Locator CORPORATE_HEADER =  new Locator("CORPORATE_HEADER", new By.ByXPath("//*[text()='Corporate Information']"));
    private static final Locator CORPORATE_SUBHEADER =  new Locator("CORPORATE_SUBHEADER", new By.ByXPath("//*[text()='General Information']"));
    private static final Locator B2C_FLOW_INITIATION =  new Locator("B2C_FLOW_INITIATION", new By.ByXPath("//button[normalize-space()='Go To tiket.com page']"));
    private static final Locator SCROLL =  new Locator("SCROLL", new By.ByXPath("//div[contains(@class,'font-bold ml-')]"));
    private static final Locator REFUND_LIST =  new Locator("REFUND_LIST", new By.ByXPath("//span[@title='Refund List']"));
    private static final Locator HEADER_REFUND_REQUEST =  new Locator("HEADER_REFUND_REQUEST", new By.ByXPath("//span[contains(text(), 'Refund Request')]"));
    private static final Locator RESCHEDULE_LIST =  new Locator("RESCHEDULE_LIST", new By.ByXPath("//span[@title='Reschedule List']"));
    private static final Locator HEADER_RESCHEDULE_LIST =  new Locator("HEADER_RESCHEDULE_LIST", new By.ByXPath("//h1[normalize-space()='Reschedule List']"));
    private static final Locator RESCHEDULE_REQUEST =  new Locator("HEADER_RESCHEDULE_REQUEST", new By.ByXPath("//span[contains(text(),'Reschedule Request')]"));
    private static final Locator TRAVEL_POLICY =  new Locator("TRAVEL_POLICY", new By.ByXPath("//span[@title='Travel Policy']"));
    private static final Locator HEADER_ADD_POLICY =  new Locator("HEADER_ADD_POLICY", new By.ByXPath("//button[normalize-space()='Add policy']"));
    private static final Locator COMPANYINFORMATION =  new Locator("COMPANY_INFORMATION", new By.ByXPath("//span[@title='Company Information']"));
    private static final Locator ADD_CODE =  new Locator("CDC", new By.ByXPath("//button[normalize-space()='Add a new code'] | //button[contains(text(), 'Add a New Code')]"));
    private static final Locator PPIH =  new Locator("HEADER_RESCHEDULE_REQUEST", new By.ByXPath("//div[contains(@class,'w-full mt-') and text()='PPIH']"));
    private static final Locator SELECT_AIRLINE_DROPDOWN =  new Locator("SELECT_AIRLINE_DROPDOWN", new By.ByXPath("//div[@class='relative bg-white']"));
    private static final Locator SELECT_AIRLINE =  new Locator("SELECT_AIRLINE", new By.ByXPath("//li[normalize-space()='GARUDA INDONESIA']"));
    private static final Locator ENTER_CODE =  new Locator("ENTER_CODE", new By.ByXPath("//input[@id='enter-the-code']"));
    private static final Locator SAVE =  new Locator("SAVE", new By.ByXPath("//button[normalize-space()='Save']"));
    private static final Locator TRAVEL_PLAN =  new Locator("TRAVEL_PLAN", new By.ByXPath("(//*[contains(text(), 'Travel Plan')])[1]"));
    private static final Locator YOUR_TRAVEL_PLAN =  new Locator("YOUR_TRAVEL_PLAN", new By.ByXPath("//span[text()='My Travel Plan']"));
    private static final Locator TRAVEL_PLAN_REQUEST =  new Locator("TRAVEL_PLAN_REQUEST", new By.ByXPath("//*[contains(text(), 'Travel Plan Request')]"));
    private static final Locator TEAM_MANAGEMENT =  new Locator("TEAM_MANAGEMENT", new By.ByXPath("//span[contains(text(), 'Team Management')]"));
    private static final Locator DEPARTMENT =  new Locator("DEPARTMENT", new By.ByXPath("//span[contains(text(), 'Department')]"));
    private static final Locator GROUP =  new Locator("GROUP", new By.ByXPath("//span[contains(text(), 'Groups')]"));
    private static final Locator SETTINGS_SIDEBAR =  new Locator("SETTINGS_SIDEBAR", new By.ByXPath("(//span[contains(text(), 'Settings')])[1]"));
    private static final Locator CUSTOM_FIELD_SIDEBAR =  new Locator("CUSTOM_FIELD_SIDEBAR", new By.ByXPath("//span[contains(text(), 'Custom')]"));
    private static final Locator COST_CENTER_SIDEBAR = new Locator("COST_CENTER_SIDEBAR", By.xpath("//span[contains(text(), 'Cost Center')]"));
    private static final Locator CORPORATE_DEAL_AND_AIRLINE = new Locator("CORPORATE_DEAL_AND_AIRLINE", By.xpath("//td[contains(@class,'ProductTypes_table_data')]//img[@alt='corporate deal']/ancestor::td//span[normalize-space(text())='Garuda Indonesia']"));
    private static final Locator BOOKING_LIST_MENU = new Locator("BOOKING_LIST_MENU", By.xpath("//span[@title='Booking List' or @title='Daftar Pemesanan' or normalize-space(text())='Booking List' or normalize-space(text())='Daftar Pemesanan']"));
    private static final Locator DEPOSIT_TOP_UP = new Locator("DEPOSIT_TOP_UP", By.xpath("//span[@title='Deposit Top Up']"));
    private static final Locator REQUEST_ID_INPUT = new Locator("REQUEST_ID_INPUT", By.id("request-id"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[contains(@class, 'Button_variant_secondary')]//span[normalize-space(text())='Search']"));
    private static final Locator REQUEST_ID_CELL = new Locator("REQUEST_ID_CELL", By.xpath("//tbody/tr[1]/td[2]"));
    private static final Locator COMPANY_SETTINGS_BUTTON = new Locator("COMPANY_SETTINGS_BUTTON", By.xpath("//button[.//span[contains(text(), 'Company Settings')]]"));
    private static final Locator SETTINGS_BUTTON = new Locator("SETTINGS_BUTTON", By.xpath("//button[.//span[text()='Settings']]"));
    private static final Locator RESCHEDULE_AUTO_APPROVE_MESSAGE = new Locator("RESCHEDULE_AUTO_APPROVE_MESSAGE", By.xpath("//span[contains(., 'Reschedule requests don') and contains(., 'require approval (auto approve')]"));
    private static final Locator REFUND_AUTO_APPROVE_MESSAGE = new Locator("REFUND_AUTO_APPROVE_MESSAGE", By.xpath("//span[contains(., 'Refund requests don') and contains(., 'require approval (auto approve')]"));
    private static final Locator REQUEST_TOP_UP_BUTTON = new Locator("REQUEST_TOP_UP_BUTTON", By.xpath("//button[contains(text(),'Request top-up')]"));
    private static final Locator IDR_REQUESTED_AMOUNT_INPUT = new Locator("IDR_REQUESTED_AMOUNT_INPUT", By.id("idr-requested-amount"));
    private static final Locator BROWSE_FILE_BTN = new Locator("BROWSE_FILE_BTN", By.xpath("//input[@type='file']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[normalize-space()='Submit' or normalize-space()='Ajukan']"));
    private static final Locator REQUEST_SUBMITTED_SNACKBAR = new Locator("REQUEST_SUBMITTED_SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_text_content') and (" + "text()='The request has been submitted' or " + "text()='Permintaan telah dikirim')]"));
    private static final Locator ACCOUNT_BUTTON = new Locator("ACCOUNT_BUTTON", By.xpath("//button[.//span[normalize-space(text())='Account' or normalize-space(text())='Akun']]"));
    private static final Locator ACCOUNT_PAGE_SPAN = new Locator("ACCOUNT_PAGE_SPAN", By.xpath("//span[contains(@class, 'account_span__') and (normalize-space(text())='account page' or normalize-space(text())='halaman akun')]"));
    private static final Locator CHANGE_PASSWORD_BUTTON = new Locator("CHANGE_PASSWORD_BUTTON", By.xpath("//div[@data-testid='settings-changepassword' and contains(@class, 'SettingsItem_item__')]"));
    private static final Locator CURRENT_PASSWORD_INPUT = new Locator("CURRENT_PASSWORD_INPUT", By.xpath("//input[@id='current-password' and @type='password']"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", By.xpath("//button[contains(@class, 'AccountVerification_submit') and normalize-space(text())='Continue']"));
    private static final Locator NEW_PASSWORD_INPUT = new Locator("NEW_PASSWORD_INPUT", By.id("new-password"));
    private static final Locator PASSWORD_UPDATE_SNACKBAR = new Locator("PASSWORD_UPDATE_SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_text_content') and (contains(text(), 'successfully updated') or contains(text(), 'berhasil diperbarui'))]"));
    private static final Locator CONFIRM_BUTTON = new Locator("CONFIRM_BUTTON", By.xpath("//button[normalize-space()='Confirm' or normalize-space()='Konfirmasi']"));
    private static final Locator EDIT_TRAVEL_POLICY = new Locator("EDIT_TRAVEL_POLICY", By.xpath("(//button[normalize-space()='Edit' and contains(@class, 'Button_variant_tertiary')])[2]"));
    public static final Locator TRAVEL_POLICY_RADIO = new Locator("TRAVEL_POLICY_RADIO", By.xpath("//input[@name='TRAVEL_POLICY_COMPLY_SEARCH' and @value='false']/following-sibling::span"));
    public static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[normalize-space(text())='Save changes' and contains(@class, 'Button_variant_primary')]"));
    public static final Locator SUCCESS_SNACKBAR = new Locator("TRAVEL_POLICY_SUCCESS_SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_text_content') and contains(text(), 'updated the travel policy')]"));
    public static final Locator EDIT_REFUND = new Locator("EDIT_REFUND", By.xpath("(//button[normalize-space()='Edit' and contains(@class, 'Button_variant_tertiary')])[3]"));
    public static final Locator REFUND_RADIO = new Locator("REFUND_RADIO", By.xpath("//input[@name='REFUND_AUTO_APPROVE' and @value='true']/following-sibling::span"));
    public static final Locator EDIT_TRAVEL_PLAN = new Locator("EDIT_TRAVEL_PLAN", By.xpath("(//button[contains(@class, 'Button_variant_tertiary') and contains(., 'Edit')])[1]"));
    private static final Locator CLOSE_POP_UP = new Locator("CLOSE_POP_UP", new By.ByXPath("//button[contains(@class, 'Button-module') and (normalize-space(text())='Close' or normalize-space(text())='Tutup')]"));
    public static final Locator TRAVEL_PLAN_RADIO_OFF = new Locator("TRAVEL_PLAN_RADIO_OFF", By.xpath("//input[@name='BOOKING_REQUIRED_TRAVEL_PLAN' and @type='radio' and @value='false']"));
    public static final Locator SAVE_TRAVEL_PLAN = new Locator("SAVE_TRAVEL_PLAN", By.xpath("//button[normalize-space(text())='Save' and contains(@class, 'Button_variant_primary')]"));
    public static final Locator TRAVEL_PLAN_SUCCESS_SNACKBAR = new Locator("TRAVEL_PLAN_SUCCESS_SNACKBAR", By.xpath("(//div[contains(@class, 'Snackbar_text_content') and contains(text(), 'updated the travel plan')])[1]"));
    public static final Locator TRAVEL_PLAN_RADIO_ON = new Locator("TRAVEL_PLAN_RADIO_ON", By.xpath("//input[@name='BOOKING_REQUIRED_TRAVEL_PLAN' and @type='radio' and @value='true']"));
    private static final Locator OPEN_BUTTON=new Locator("OPEN_BUTTON", new By.ByXPath("(//button[contains(text(), 'Open')])[1]"));
    private static final Locator TRANSACTION_LIST=new Locator("TRANSACTION_LIST", new By.ByXPath("//span[contains(text(),'Transaction')]"));
    private static final Locator INVOICE_AND_BILLING = new Locator("INVOICE_AND_BILLING", By.xpath("//span[@title='Invoices & Billing' or @title='Invoices and Billing' or normalize-space(text())='Invoices & Billing' or normalize-space(text())='Invoices and Billing']"));
    private static final Locator REFUND_N_RESCHEDULE = new Locator("REFUND_N_RESCHEDULE", By.xpath("//span[text()='Refund & reschedule']"));
    private static final Locator SHOW_ONE_RESULT = new Locator("REFUND_N_RESCHEDULE", By.xpath("//span[text()='Showing 1 to 1 of 1']"));
    private static final Locator SETTINGS = new Locator("SETTINGS", By.xpath("//*[text()='Settings']"));
    private static final Locator FINANCE = new Locator("FINANCE", By.xpath("//span[text()='Finance']"));

    public CorporateExternalDashboardSidebar(Session<T> session) {super(session);}

    private static boolean policyEdit;
    private static boolean refundEdit;
    public void goToCorporateInformationPage(){
        driverTools.waitForElementToAppear(SETTINGS);
        driverTools.click(SETTINGS);

        driverTools.waitForElementToAppear(COMPANY_INFORMATION);
        driverTools.click(COMPANY_INFORMATION);
        driverTools.waitForElementToAppear(CORPORATE_HEADER, 5).orElseThrow();
        driverTools.waitForElementToAppear(CORPORATE_SUBHEADER, 5).orElseThrow();
    }

    public void goToB2C(){
        driverTools.maximizeWindow();
        driverTools.waitForElementToAppear(B2C_FLOW_INITIATION);
        driverTools.click(B2C_FLOW_INITIATION);
    }

    public void goToRefundList(){
        driverTools.maximizeWindow();
        driverTools.waitForElementToAppear(REFUND_LIST);
        driverTools.click(REFUND_LIST);
        driverTools.waitForElementToAppear(HEADER_REFUND_REQUEST,30).orElseThrow();
    }

    public void goToRescheduleList()
    {
        driverTools.maximizeWindow();
        driverTools.waitForElementToAppear(RESCHEDULE_LIST);
        driverTools.clickWithJavascriptExecutor(RESCHEDULE_LIST);
        driverTools.waitForElementToAppear(HEADER_RESCHEDULE_LIST,30).orElseThrow();
        driverTools.waitForElementToAppear(RESCHEDULE_REQUEST,30).orElseThrow();
    }

    public void goToTravelPolicy()
    {
        driverTools.maximizeWindow();
        driverTools.waitForElementToAppear(TRAVEL_POLICY);
        driverTools.clickWithJavascriptExecutor(TRAVEL_POLICY);
        driverTools.waitForElementToAppear(HEADER_ADD_POLICY,30).orElseThrow();
    }

    public void goToCorporateInformation(){
        driverTools.maximizeWindow();
        driverTools.waitForElementToPresence(COMPANYINFORMATION);
        driverTools.clickWithJavascriptExecutor(COMPANYINFORMATION);
        driverTools.waitForElementToAppear(PPIH);
        driverTools.isElementDisplayed(PPIH);
        if(!driverTools.isElementDisplayed(PPIH)){
            driverTools.scrollToElement(ADD_CODE);
            driverTools.clickWithJavascriptExecutor(ADD_CODE);
            driverTools.clickWithJavascriptExecutor(SELECT_AIRLINE_DROPDOWN);
            driverTools.clickWithJavascriptExecutor(SELECT_AIRLINE);
            driverTools.sendKeys(ENTER_CODE,"PPIH");
            driverTools.waitForElementToPresence(SAVE);
            driverTools.clickWithJavascriptExecutor(SAVE);
        }
    }

    public void clickTravelPlanSidebar(){
        driverTools.waitForElementToAppear(TRAVEL_PLAN);
        driverTools.click(TRAVEL_PLAN);
    }

    public void clickTravelPlanRequest(){
        driverTools.waitForElementToAppear(TRAVEL_PLAN_REQUEST);
        driverTools.click(TRAVEL_PLAN_REQUEST);
    }

    public void clickOpenButton(){
        driverTools.waitForElementToAppear(OPEN_BUTTON);
        driverTools.clickWithJavascriptExecutor(OPEN_BUTTON);
    }

    public void openTravelPlanPage(){
        clickTravelPlanSidebar();
        driverTools.waitForElementToAppear(YOUR_TRAVEL_PLAN);
        driverTools.click(YOUR_TRAVEL_PLAN);
    }

    public boolean isTeamManagementVisible(){
        driverTools.waitForElementToAppear(TEAM_MANAGEMENT);
        return driverTools.isElementDisplayed(TEAM_MANAGEMENT);
    }

    public void goToDepartment(){
        driverTools.click(TEAM_MANAGEMENT);
        driverTools.waitForElementToAppear(DEPARTMENT);
        driverTools.click(DEPARTMENT);
    }

    public void goToGroup(){
        driverTools.click(TEAM_MANAGEMENT);
        driverTools.waitForElementToAppear(GROUP);
        driverTools.click(GROUP);
    }

    public void GoToPolicyNonAdmin(){
        driverTools.waitForElementToAppear(TRAVEL_POLICY);
        driverTools.clickWithJavascriptExecutor(TRAVEL_POLICY);
    }

    public void clickOnSettings(){
        driverTools.waitForElementToAppear(SETTINGS_SIDEBAR);
        driverTools.click(SETTINGS_SIDEBAR);
    }

    public boolean isCustomFieldOptionPresent(){
        driverTools.waitForElementToAppear(CUSTOM_FIELD_SIDEBAR);
        return driverTools.isElementDisplayed(CUSTOM_FIELD_SIDEBAR);
    }

    public void goToTravelerCustomField(){
        driverTools.waitForElementToAppear(CUSTOM_FIELD_SIDEBAR);
        driverTools.clickWithJavascriptExecutor(CUSTOM_FIELD_SIDEBAR);
    }

    public void goToCostCenter() {
        driverTools.waitForElementToAppear(COST_CENTER_SIDEBAR);
        driverTools.clickWithJavascriptExecutor(COST_CENTER_SIDEBAR);
    }

    public void goToBookingListMenu() {
        driverTools.waitForElementToAppear(BOOKING_LIST_MENU);
        driverTools.click(BOOKING_LIST_MENU);
    }

    public boolean isCorporateDealGarudaVisible() {
        driverTools.waitForElementToAppear(CORPORATE_DEAL_AND_AIRLINE);
        driverTools.scrollToElement(CORPORATE_DEAL_AND_AIRLINE);
        return driverTools.isElementDisplayed(CORPORATE_DEAL_AND_AIRLINE);
    }

    public void goToDepositHistory() {
        driverTools.waitForElementToAppear(FINANCE);
        driverTools.click(FINANCE);

        driverTools.waitForElementToAppear(DEPOSIT_TOP_UP);
        driverTools.click(DEPOSIT_TOP_UP);
    }

    public void searchByRequestId(String requestId) {
        driverTools.waitForElementToAppear(REQUEST_ID_INPUT);
        driverTools.sendKeys(REQUEST_ID_INPUT, requestId);
        driverTools.waitForElementToAppear(SEARCH_BUTTON);
        driverTools.click(SEARCH_BUTTON);
    }

    public boolean isRequestIdDisplayed(String requestId) {
        driverTools.waitForElementToAppear(SHOW_ONE_RESULT);
        if(driverTools.isElementDisplayed(SHOW_ONE_RESULT)){
            driverTools.waitForElementToAppear(REQUEST_ID_CELL);
            return (driverTools.getText(REQUEST_ID_CELL)).toString().contains(requestId);
        }
      return false;
    }

    public void goToCompanySettings() {
        driverTools.waitForElementToAppear(SETTINGS_BUTTON);
        driverTools.scrollToElement(SETTINGS_BUTTON);
        driverTools.clickWithJavascriptExecutor(SETTINGS_BUTTON);
        driverTools.waitForElementToAppear(COMPANY_SETTINGS_BUTTON);
        driverTools.click(COMPANY_SETTINGS_BUTTON);
    }

    public boolean isRescheduleAutoApproveMessageDisplayed() {
        driverTools.waitForElementToAppear(RESCHEDULE_AUTO_APPROVE_MESSAGE);
        return driverTools.isElementDisplayed(RESCHEDULE_AUTO_APPROVE_MESSAGE);
    }

    public boolean isRefundAutoApproveMessageDisplayed() {
        driverTools.waitForElementToAppear(REFUND_AUTO_APPROVE_MESSAGE);
        return driverTools.isElementDisplayed(REFUND_AUTO_APPROVE_MESSAGE);
    }

    public void createRequestDepositTopUp(String amount, String filePath) {
        File data = new File(filePath);

        driverTools.waitForElementToAppear(REQUEST_TOP_UP_BUTTON);
        driverTools.click(REQUEST_TOP_UP_BUTTON);
        driverTools.waitForElementToAppear(IDR_REQUESTED_AMOUNT_INPUT);
        driverTools.sendKeys(IDR_REQUESTED_AMOUNT_INPUT, amount);
        driverTools.waitForElementToAppear(BROWSE_FILE_BTN);
        driverTools.sendKeys(BROWSE_FILE_BTN, data.getAbsolutePath());
        driverTools.scrollToElement(SUBMIT_BUTTON);
        driverTools.waitForElementToAppear(SEARCH_BUTTON);
        driverTools.clickWithJavascriptExecutor(SUBMIT_BUTTON);
    }

    public boolean isRequestSubmittedSnackbarDisplayed() {
        driverTools.waitForElementToAppear(REQUEST_SUBMITTED_SNACKBAR);
        return driverTools.isElementDisplayed(REQUEST_SUBMITTED_SNACKBAR);
    }

    public void goToAccountPage() {
        driverTools.waitForElementToAppear(ACCOUNT_BUTTON);
        driverTools.scrollToElement(ACCOUNT_BUTTON);
        driverTools.clickWithJavascriptExecutor(ACCOUNT_BUTTON);
        driverTools.waitForElementToAppear(ACCOUNT_PAGE_SPAN);
        driverTools.click(ACCOUNT_PAGE_SPAN);
    }

    public void changePassword(String currentPassword, String newPassword) {
        driverTools.switchToWindowContainingUrl("bliblitiket.com/account");
        driverTools.waitForElementToAppear(CHANGE_PASSWORD_BUTTON);
        driverTools.click(CHANGE_PASSWORD_BUTTON);
        driverTools.waitForElementToAppear(CURRENT_PASSWORD_INPUT);
        driverTools.sendKeys(CURRENT_PASSWORD_INPUT, currentPassword);
        driverTools.waitForElementToAppear(CONTINUE_BUTTON);
        driverTools.click(CONTINUE_BUTTON);
        driverTools.waitForElementToAppear(NEW_PASSWORD_INPUT);
        driverTools.sendKeys(NEW_PASSWORD_INPUT, newPassword);
        driverTools.click(CONFIRM_BUTTON);
    }

    public boolean isPasswordUpdatedSnackbarDisplayed() {
        driverTools.waitForElementToAppear(PASSWORD_UPDATE_SNACKBAR);
        return driverTools.isElementDisplayed(PASSWORD_UPDATE_SNACKBAR);
    }

    public boolean isTravelPolicyEditable(){
        driverTools.waitForElementToAppear(EDIT_TRAVEL_POLICY,10);
        driverTools.click(EDIT_TRAVEL_POLICY);

        driverTools.waitForElementToAppear(TRAVEL_POLICY_RADIO,10);
        driverTools.click(TRAVEL_POLICY_RADIO);

        driverTools.waitForElementToAppear(SAVE_BUTTON,10);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);

        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 10);
        if(driverTools.isElementDisplayed(SUCCESS_SNACKBAR)) {
            policyEdit = true;
            return true;
        }
        else {
            policyEdit = false;
            return false;
        }
    }

    public boolean isRefundEditable() {
        driverTools.waitForElementToAppear(EDIT_REFUND,10);
        driverTools.click(EDIT_REFUND);

        driverTools.waitForElementToAppear(REFUND_RADIO,10);
        driverTools.click(REFUND_RADIO);

        driverTools.waitForElementToAppear(SAVE_BUTTON,10);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);

        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 10);
        if(driverTools.isElementDisplayed(SUCCESS_SNACKBAR)){
            refundEdit = true;
            return true;
        }
        else {
            refundEdit = false;
            return false;
        }
    }

    public boolean isSettingsSectionAccessByRole(){
        if(!(policyEdit && refundEdit)){
            logger.log("Settings section is not accessible. Ensure credentials are of Admin or Super Admin.");
            return false;
        }
        return true;
    }

    public void editTravelPlan(){
        driverTools.waitForElementToAppear(EDIT_TRAVEL_PLAN,10);
        driverTools.click(EDIT_TRAVEL_PLAN);
    }

    public void clickTravelPlanRadioToOFF() {
        driverTools.waitForElementToBeClickable(TRAVEL_PLAN_RADIO_OFF,10);
        driverTools.clickWithJavascriptExecutor(TRAVEL_PLAN_RADIO_OFF);

        driverTools.clickWithJavascriptExecutor(SAVE_TRAVEL_PLAN);
    }

    public boolean isTravelPlanSuccessSnackbarDisplayed() {
        driverTools.waitForElementToAppear(TRAVEL_PLAN_SUCCESS_SNACKBAR, 10);
        return driverTools.isElementDisplayed(TRAVEL_PLAN_SUCCESS_SNACKBAR);
    }

    public void clickTravelPlanRadioToON(){
        driverTools.waitForElementToBeClickable(TRAVEL_PLAN_RADIO_ON,10);
        driverTools.clickWithJavascriptExecutor(TRAVEL_PLAN_RADIO_ON);

        driverTools.clickWithJavascriptExecutor(SAVE_TRAVEL_PLAN);
    }
    
    public void goToCorporateDashboardPage(){
        driverTools.maximizeWindow();
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://corporate.gatotkaca.tiket.com/");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://corporate.preprod.tiket.com/");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driverTools.get("https://corporate.tiket.com/");
        }
        driverTools.waitForElementToAppear(CLOSE_POP_UP, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public void goToTransactionList(){
        driverTools.waitForElementToAppear(TRANSACTION_LIST);
        driverTools.click(TRANSACTION_LIST);
    }

    public void goToInvoiceAndBilling() {
        driverTools.waitForElementToAppear(INVOICE_AND_BILLING);
        driverTools.scrollToElement(INVOICE_AND_BILLING);
        driverTools.clickWithJavascriptExecutor(INVOICE_AND_BILLING);
    }
    public void goToTravelPolicyPage(){
        driverTools.maximizeWindow();
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://corporate.gatotkaca.tiket.com/travel-policy");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://corporate.preprod.tiket.com/travel-policy");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driverTools.get("https://corporate.tiket.com/travel-policy");
        }
    }

    public void clickCorporateInformation(){
        driverTools.waitForElementToPresence(COMPANYINFORMATION);
        driverTools.clickWithJavascriptExecutor(COMPANYINFORMATION);
    }

    public void goToRefundAndRescheduleTab(){
        driverTools.waitForElementToAppear(REFUND_N_RESCHEDULE);
        driverTools.click(REFUND_N_RESCHEDULE);
    }
}