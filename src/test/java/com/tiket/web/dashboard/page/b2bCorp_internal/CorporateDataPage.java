package com.tiket.web.dashboard.page.b2bCorp_internal;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.core.Is.is;

public class CorporateDataPage<T extends WebDriver> extends BasePage<T> {

    private static final Locator CORPORATE_NAME_INPUT = new Locator("CORPORATE_NAME_INPUT", new By.ByXPath("//input[@placeholder='Corporate name']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[normalize-space()='Search']"));
    private static final Locator SEE_DETAILS_BUTTON = new Locator("SEE_DETAILS_BUTTON", new By.ByXPath("//button[normalize-space()='See Details']"));
    private static final Locator DOWNLOAD_CSV = new Locator("DOWNLOAD_CSV", new By.ByXPath("//button[normalize-space()='Download Corporate Data List']"));
    private static final Locator LOG_IN_INDICATOR = new Locator("LOG_IN_INDICATOR", new By.ByXPath("//span[contains(@class, 'md:w-11 md:h-11 md:text-lg')]"));
    private static final Locator SELECT_DROP_DOWN = new Locator("SELECT_DROP_DOWN", new By.ByXPath("//div[contains(@class,'InputSearchAutoComplete_list')]"));
    private static final Locator ADD_CORPORATE = new Locator("ADD_CORPORATE", new By.ByXPath("//button[normalize-space()='Add New Corporate']"));
    private static final Locator CORP_NAME = new Locator("CORP_NAME", new By.ByXPath("//input[@id='corporate-name']"));
    private static final Locator PIC_NAME = new Locator("PIC_NAME", new By.ByXPath("//input[@id='pic-name']"));
    private static final Locator PIC_EMAIL = new Locator("PIC_EMAIL", new By.ByXPath("//input[@id='pic-email']"));
    private static final Locator PIC_NUMBER = new Locator("PIC_NUMBER", new By.ByXPath("//input[@id='mobile-number']"));
    private static final Locator NPWP = new Locator("NPWP", new By.ByXPath("//input[@id='npwp']"));
    private static final Locator BILLING_ADD = new Locator("BILLING_ADD", new By.ByXPath("//input[@id='billing-address']"));
    private static final Locator CORP_ADD = new Locator("CORP_ADD", new By.ByXPath("//input[@id='corporate-address']"));
    private static final Locator DROP_DOWN_BANK = new Locator("DROP_DOWN_BANK", new By.ByXPath("//input[@id='bank-name']"));
    private static final Locator BANK = new Locator("BANK", new By.ByXPath("(//div[contains(@class, 'AutoComplete_list_menu')])[1]"));
    private static final Locator ACC_NUMBER = new Locator("ACC_NUMBER", new By.ByXPath("//input[@id='account-number']"));
    private static final Locator CHECK = new Locator("CHECK", new By.ByXPath("(//button[contains(@class, 'styles_clickable_ico')])[4]"));
    private static final Locator CRED_LIMIT = new Locator("CRED_LIMIT", new By.ByXPath("//input[@id='credit-limit']"));
    private static final Locator THRESHHOLD = new Locator("THRESHHOLD", new By.ByXPath("//input[@id='threshold']"));
    private static final Locator NUMBER_OF_DAYS = new Locator("NUMBER_OF_DAYS", new By.ByXPath("//input[@id='no-of-days']"));
    private static final Locator SERV_FEE = new Locator("SERV_FEE", new By.ByXPath("//input[@id='service-fee']"));
    private static final Locator TAX_FEE = new Locator("TAX_FEE", new By.ByXPath("//input[@id='tax-rate']"));
    private static final Locator SAVE = new Locator("SAVE", new By.ByXPath("//button[normalize-space()='Save']"));
    private static final Locator SAVE_CHANGES_BTN = new Locator("SAVE_CHANGES_BTN", new By.ByXPath("//button//span[normalize-space(text())='Save changes']"));
    private static final Locator EDIT = new Locator("EDIT", new By.ByXPath("//button[normalize-space()='Edit']"));
    private static final Locator SCROLL = new Locator("SCROLL", new By.ByXPath("//input[@id='account-holder-name']"));
    private static final Locator CORPORATE_INFORMATION = new Locator("CORPORATE_INFORMATION", new By.ByXPath("//span[@title='Corporate Information']"));
    private static final Locator CORPORATE_DATA = new Locator("CORPORATE_DATA", new By.ByXPath("//span[@title='Corporate Data']"));
    private static final Locator REGISTERED_CORPORATE = new Locator("REGISTERED_CORPORATE", By.xpath("//span[@title='Registered Corporate']"));
    private static final Locator ENTITY_TYPE = new Locator("ENTITY_TYPE", new By.ByXPath("//div[contains(@class, 'cursor-pointer')]//span[normalize-space(text())='Entity Type']"));
    private static final Locator ENTITY_TYPE_FIRST_OPTION = new Locator("ENTITY_TYPE_FIRST_OPTION", new By.ByXPath("(//ul[contains(@class, 'overflow-y-auto') and contains(@class, 'visible')]//li[contains(@class, 'cursor-pointer')])[1]"));
    private static final Locator CORPORATE_TYPE = new Locator("CORPORATE_TYPE", new By.ByXPath("//input[@placeholder='Corporate Type']"));
    private static final Locator CORPORATE_TYPE_FIRST_OPTION = new Locator("CORPORATE_TYPE_FIRST_OPTION", new By.ByXPath("(//ul[contains(@class, 'overflow-y-auto') and contains(@class, 'visible')]//input[@type='radio'])[1]"));
    private static final Locator CORPORATE_TAGS = new Locator("CORPORATE_TAGS", new By.ByXPath("//input[@placeholder='Corporate Tags']"));
    private static final Locator CORPORATE_TAGS_FIRST_OPTION = new Locator("CORPORATE_TAGS_FIRST_OPTION", new By.ByXPath("(//div[contains(@class, 'overflow-y-auto')]//input[@type='checkbox'])[1]"));
    private static final Locator INDUSTRY = new Locator("INDUSTRY", new By.ByXPath("//div[contains(@class, 'cursor-pointer')]//span[normalize-space(text())='Industry']"));
    private static final Locator INDUSTRY_FIRST_OPTION = new Locator("INDUSTRY_FIRST_OPTION", new By.ByXPath("//ul[contains(@class, 'visible')]//li[normalize-space(text())='Insurance']"));
    private static final Locator MONTHLY_TRAVEL_BUDGET = new Locator("MONTHLY_TRAVEL_BUDGET", new By.ByXPath("//div[contains(@class, 'cursor-pointer')]//span[normalize-space(text())='Monthly Travel Budget']"));
    private static final Locator MONTHLY_TRAVEL_BUDGET_FIRST_OPTION = new Locator("MONTHLY_TRAVEL_BUDGET_FIRST_OPTION", new By.ByXPath("//ul[contains(@class, 'visible')]//li[normalize-space(text())='<150 Million IDR']"));
    private static final Locator OFFICE_ADDRESS = new Locator("OFFICE_ADDRESS", new By.ByXPath("//textarea[@id='address' and @placeholder='Address']"));
    private static final Locator OFFICE_ADDRESS_DETAIL = new Locator("OFFICE_ADDRESS_DETAIL", new By.ByXPath("//input[@id='address-details' and @placeholder='Address Details']"));
    private static final Locator FULL_NAME = new Locator("FULL_NAME", new By.ByXPath("//input[@id='full-name' and @placeholder='Full Name']"));
    private static final Locator POSITION = new Locator("POSITION", new By.ByXPath("//span[contains(text(), 'Position') and contains(@class, 'overflow-hidden')]"));
    private static final Locator POSITION_FIRST_OPTION = new Locator("POSITION_FIRST_OPTION", new By.ByXPath("//ul[contains(@class,'overflow-y-auto')]//li[1][contains(text(),'Director/Owner')]"));
    private static final Locator ACCOUNT_MANAGER = new Locator("ACCOUNT_MANAGER", new By.ByXPath("//input[@id='select-account-manager' and @placeholder='Select Account Manager']"));
    private static final Locator SELECT_ACCOUNT_EXECUTIVE = new Locator("SELECT_ACCOUNT_EXECUTIVE", new By.ByXPath("//input[@id='select-account-executive' and @placeholder='Select Account Executive']"));
    private static final Locator ACCOUNT_EXECUTIVE_FIRST_OPTION = new Locator("ACCOUNT_EXECUTIVE_FIRST_OPTION", new By.ByXPath("//span[text()='Accommodation Centralized']"));
    private static final Locator TIN = new Locator("TIN", new By.ByXPath("//input[@id='corporate-tax-identification-number-tin' and @placeholder='Corporate Tax Identification Number (TIN)']"));
    private static final Locator ACCOUNT_MANAGER_FIRST_OPTION = new Locator("ACCOUNT_MANAGER_FIRST_OPTION", new By.ByXPath("//span[text()='Accommodation Centralized']"));
    private static final Locator EMAIL_ADDRESS = new Locator("EMAIL_ADDRESS", new By.ByXPath("//input[@id='email-address']"));
    private static final Locator EMAIL_ADDRESS_FIRST_OPTION = new Locator("EMAIL_ADDRESS_FIRST_OPTION", new By.ByXPath("(//div[contains(@class, 'InputSearchAutoComplete_list_menu__')]//span)[1]"));
    private static final Locator UPDATED_EMAIL_ADDRESS = new Locator("UPDATED_EMAIL_ADDRESS", new By.ByXPath("//span[text()='Email Address']/following::span[1]"));
    private static final Locator SAVE_BTN = new Locator("SAVE_BTN", new By.ByXPath("(//button[normalize-space(text())='Save'])[2]"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_text_content')]"));
    private static final Locator CORPORATE_DOCUMENT = new Locator("CORPORATE_DOCUMENT", new By.ByXPath("//button[contains(@class,'Chip_chip') and .//span[text()='Corporate Documents']]"));
    private static final Locator LEGAL_ACTION_BTN = new Locator("LEGAL_ACTION_BTN", new By.ByXPath("(//table)[2]//*[local-name()='svg' and contains(@class,'cursor-pointer')]"));
    private static final Locator VIEW_OPTION = new Locator("VIEW_OPTION", new By.ByXPath("(//table)[2]//ul[contains(@class,'bg-white')]//li[normalize-space()='View']"));
    private static final Locator DOWNLOAD_OPTION = new Locator("DOWNLOAD_OPTION", new By.ByXPath("(//table)[2]//ul[contains(@class,'bg-white')]//li[normalize-space()='Download']"));
    private static final Locator RE_UPLOAD_OPTION = new Locator("RE_UPLOAD_OPTION", new By.ByXPath("(//table)[2]//ul[contains(@class,'bg-white')]//li[normalize-space()='Re-upload']"));
    private static final Locator VERSION_HISTORY_OPTION = new Locator("VERSION_HISTORY_OPTION", new By.ByXPath("(//ul[contains(@class,'bg-white')]//li[normalize-space()='Version History'])[1]"));
    private static final Locator PAYMENT_DETAILS = new Locator("PAYMENT_DETAILS", new By.ByXPath("//span[normalize-space()='Payment Details']"));
    private static final Locator PAYMENT_EDIT= new Locator("PAYMENT_EDIT", new By.ByXPath("(//button[normalize-space()='Edit'])[2]"));
    private static final Locator ACTIVE_PAYMENT_OPTIONS = new Locator("ACTIVE_PAYMENT_OPTIONS", new By.ByXPath("(//button[contains(@class, 'Chip_active')]/span)[2]"));
    private static final Locator CHANGE_PAYMENT_BTN = new Locator("CHANGE_PAYMENT_BTN", new By.ByXPath("//button//span[normalize-space(text())='Save']"));
    private static final Locator PAYMENT_CHANGE_SUCCESS_SNACKBAR = new Locator("PAYMENT_CHANGE_SUCCESS_SNACKBAR", By.xpath("//div[contains(@class, 'SnackbarWrapper_active')]"));
    private static final Locator INSTANT_PAYMENT_BTN = new Locator("INSTANT_PAYMENT_BTN", new By.ByXPath("//button[.//span[text()='Instant Payment']]"));
    private static final Locator INVOICE_PAYMENT_BTN = new Locator("INVOICE_PAYMENT_BTN", new By.ByXPath("//button[.//span[text()='Invoice']]"));
    private static final Locator PAYMENT_TERMS = new Locator("PAYMENT_TERMS", new By.ByXPath("//input[@id='terms-of-payment']"));
    private static final Locator INVOICE_THRESHOLD = new Locator("INVOICE_THRESHOLD", new By.ByXPath("//input[@id='invoice-threshold']"));
    private static final Locator EMPLOYEE_LIST = new Locator("EMPLOYEE_LIST", new By.ByXPath("//span[contains(@class, 'Text_weight_bold') and text()='Employee List']"));
    private static final Locator COMPANY_DETAILS = new Locator("COMPANY_DETAILS", new By.ByXPath("//span[text()='Company Details']"));
    private static final Locator PREMIUM_TIER = new Locator("PREMIUM_TIER", new By.ByXPath("//button[@id='benefit-plan-PREMIUM']"));


    // Registered Corporates List Page
    private static final Locator LEAD_NAMES = new Locator("LEAD_NAMES", By.xpath("//table/tbody/tr/td[1]/span"));
    private static final Locator EMPLOYEE_NAME_EMAIL = new Locator("EMPLOYEE_NAME_EMAIL", new By.ByXPath("//div[normalize-space(.)='Employee Name & Email']"));
    private static final Locator USER_ROLE = new Locator("USER_ROLE", new By.ByXPath("//div[normalize-space(.)='User Role']"));
    private static final Locator DEPARTMENT_PERMISSION = new Locator("DEPARTMENT_PERMISSION", new By.ByXPath("//div[normalize-space(.)='Department & Permission']"));
    private static final Locator GROUP_SEGMENT = new Locator("GROUP_SEGMENT", new By.ByXPath("//div[normalize-space(.)='Group' or normalize-space(.)='Group (Segment)']"));
    private static final Locator STATUS = new Locator("STATUS", new By.ByXPath("//div[normalize-space(.)='Status']"));
    private static final Locator CREATED_DATE = new Locator("CREATED_DATE", new By.ByXPath("(//div[normalize-space(.)='Created Date'])[3]"));
    private static final Locator UPDATE_DATE = new Locator("UPDATE_DATE", new By.ByXPath("//div[normalize-space(.)='Update Date']"));
    private static final Locator STATUS_FILTER_DROPDOWN = new Locator("STATUS_FILTER_DROPDOWN", By.xpath("//button[./span/span[text()='Status']]"));
    private static final Locator STATUS_FILTER_SELECTION = new Locator("STATUS_FILTER_SELECTION", By.xpath("(//div[@class='max-h-60 overflow-y-auto p-2'])[4]//span[contains(@class, 'text-[14px]')]"));
    private static final Locator STATUS_FILTER_SAVE_BUTTON = new Locator("STATUS_FILTER_SAVE_BUTTON", By.xpath("(//button[text()='Save'])[4]"));
    private static final Locator ACTION_BUTTON_AGREEMENT = new Locator("ACTION_BUTTON_AGREEMENT", By.xpath("(//table[contains(@class,'border-separate w-full')])[1]//td[2]//following-sibling::*//*[local-name()='svg']"));
    private static final Locator DELETE_AGREEMENT = new Locator("DELETE_AGREEMENT", By.xpath("(//li[text()='Delete'])[1]"));
    private static final Locator DELETE_POPUP = new Locator("DELETE_POPUP", By.xpath("//button//span[text()='Delete document']"));
    private static final Locator DELETE_SNACKBAR = new Locator("DELETE_SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_text_content')]"));
    private static final Locator ADD_SUPER_ADMIN_BUTTON = new Locator("ADD_SUPER_ADMIN_BUTTON", By.xpath("//button[.//span[normalize-space()='Add super administrator']]"));
    private static final Locator SUPER_ADMIN_SAVE_BUTTON = new Locator("SUPER_ADMIN_SAVE_BUTTON", By.xpath("//section[contains(@class,'Modal_modal_popup')]//button[.//span[normalize-space()='Save']]"));
    private static final Locator FIRST_EMPLOYEE_NAME = new Locator("FIRST_EMPLOYEE_NAME", By.xpath("(//table//tbody/tr)[1]/td[2]//button/span"));
    private static final Locator FIRST_EMPLOYEE_EMAIL = new Locator("FIRST_EMPLOYEE_EMAIL", By.xpath("(//table//tbody/tr)[1]/td[2]//p"));
    private static final Locator FIRST_EMPLOYEE_ROLE = new Locator("FIRST_EMPLOYEE_ROLE", By.xpath("(//table//tbody/tr)[1]/td[3]//span"));
    private static final Locator FIRST_EMPLOYEE_COPY_LINK_BUTTON = new Locator("FIRST_EMPLOYEE_COPY_LINK_BUTTON", By.xpath("(//table//tbody/tr)[1]//span[contains(@class,'cursor-pointer')][.//*[starts-with(@id,'copy-')]]"));
    private static final Locator REGISTER_PASSWORD_INPUT = new Locator("REGISTER_PASSWORD_INPUT", By.xpath("//input[@data-testid='txtPassword']"));
    private static final Locator REGISTER_SUBMIT_BUTTON = new Locator("REGISTER_SUBMIT_BUTTON", By.xpath("//button[@data-testid='btnSubmit']"));
    private static final Locator REGISTER_SUCCESS_TITLE = new Locator("REGISTER_SUCCESS_TITLE", By.xpath("//h1[contains(normalize-space(),'Congrats! Your account is all set')]"));
    private static final Locator REGISTER_SUCCESS_OK_BUTTON = new Locator("REGISTER_SUCCESS_OK_BUTTON", By.xpath("//main[contains(@class,'styles_success_register_container')]//button"));
    private static final Locator ADD_SUPER_ADMIN_POPUP_TITLE = new Locator("ADD_SUPER_ADMIN_POPUP_TITLE", By.xpath("//section[contains(@class,'Modal_modal_popup')]//h2[normalize-space()='Add super administrator']"));
    private static final Locator SUPER_ADMIN_EMPLOYEE_NAME = new Locator("SUPER_ADMIN_EMPLOYEE_NAME", By.id("employee-name"));
    private static final Locator SUPER_ADMIN_EMPLOYEE_EMAIL = new Locator("SUPER_ADMIN_EMPLOYEE_EMAIL", By.id("employee-email"));
    private static final Locator SUPER_ADMIN_PHONE_NUMBER = new Locator("SUPER_ADMIN_PHONE_NUMBER", By.id("phone-number"));
    private static final Locator SUPER_ADMIN_DEPARTMENT_FIELD = new Locator("SUPER_ADMIN_DEPARTMENT_FIELD", By.id("department"));
    private static final Locator SUPER_ADMIN_GROUP_FIELD = new Locator("SUPER_ADMIN_GROUP_FIELD", By.id("group"));
    private static final Locator SUPER_ADMIN_TRAVEL_POLICY_FIELD = new Locator("SUPER_ADMIN_TRAVEL_POLICY_FIELD", By.id("travel-policy"));



    public CorporateDataPage(Session<T> session) {
        super(session);
    }

    public boolean assertUserIsLoggedIn(String email) {
        driverTools.maximizeWindow();
        Locator LOGGED_IN_INDICATOR = new Locator("LOGGED_IN_INDICATOR", new By.ByXPath("//span[text()='" + email + "']"));
        return driverTools.isElementDisplayed(LOGGED_IN_INDICATOR);
    }

    public boolean checkUserIsLoggedIn() {
        driverTools.maximizeWindow();
        driverTools.waitForElementToAppear(LOG_IN_INDICATOR);
        driverTools.click(LOG_IN_INDICATOR);
        Locator LOGGED_OUT_INDICATOR = new Locator("LOGGED_OUT_INDICATOR", new By.ByXPath("//span[text()='Log Out']"));
        driverTools.waitForElementToAppear(LOGGED_OUT_INDICATOR);
        return driverTools.isElementDisplayed(LOGGED_OUT_INDICATOR);
    }

    public void inputSearch(String search) throws InterruptedException {
        driverTools.waitForElementToPresence(CORPORATE_NAME_INPUT);
        driverTools.click(CORPORATE_NAME_INPUT);
        driverTools.clearInput(CORPORATE_NAME_INPUT);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(CORPORATE_NAME_INPUT,search);
        driverTools.waitForElementToAppear(SELECT_DROP_DOWN,3);
        driverTools.click(SELECT_DROP_DOWN);
        Thread.sleep(1000);

    }

    public void clickSearch() {
        driverTools.click(SEARCH_BUTTON);
    }

    public boolean checkIfCorporateExists(String corporateName) {
        Locator CORPORATE_NAME_IN_TABLE = new Locator("CORPORATE_NAME_IN_TABLE", new By.ByXPath("//*[text()='" + corporateName + "']"));
        driverTools.waitForElementToAppear(CORPORATE_NAME_IN_TABLE, 5);
        return driverTools.isElementDisplayed(CORPORATE_NAME_IN_TABLE);
    }

    public void downloadCSV() throws InterruptedException {
        driverTools.clickWithJavascriptExecutor(DOWNLOAD_CSV);
        Thread.sleep(1500);
    }

    public boolean checkNewEmployeeCSV(String employeeName, List<List<String>> csvContent) {
        boolean isExist = false;

        for (int i = 0; i < csvContent.size(); i++) {
            List<String> csvRow = csvContent.get(i);
            logger.log("Row" + i + ": " + csvRow.toString());
            if (csvRow.get(1).equals(employeeName)) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    public void addCorporate(String name) {
        driverTools.maximizeWindow();
        driverTools.waitForElementToAppear(ADD_CORPORATE);
        driverTools.clickWithJavascriptExecutor(ADD_CORPORATE);
        driverTools.waitForElementToAppear(PIC_NAME);
        driverTools.sendKeys(CORP_NAME, name);
        driverTools.sendKeys(PIC_NAME, "TESTING");
        driverTools.sendKeys(PIC_EMAIL, "Testing@redmail.com");
        driverTools.sendKeys(PIC_NUMBER, "626262626262");
        driverTools.sendKeys(CORP_ADD, "Street view london");
        driverTools.sendKeys(NPWP, "121212121212121");
        driverTools.sendKeys(BILLING_ADD, "101 Park street");
        driverTools.scrollIntoWebView(SCROLL);
        driverTools.clickWithJavascriptExecutor(DROP_DOWN_BANK);
        driverTools.clickWithJavascriptExecutor(BANK);
        driverTools.sendKeys(ACC_NUMBER, "1234567890");
        driverTools.clickWithJavascriptExecutor(CHECK);
        driverTools.sendKeys(CRED_LIMIT, "5000000");
        driverTools.sendKeys(THRESHHOLD, "20");
        driverTools.sendKeys(NUMBER_OF_DAYS, "5");
        driverTools.sendKeys(SERV_FEE, "20");
        driverTools.sendKeys(TAX_FEE, "30");
        driverTools.clickWithJavascriptExecutor(SAVE);

    }

    public static String name() {
        String Name = CoreFunctions.randomName();
        return Name;
    }

    public void clickCorporateInformation() {
        driverTools.click(CORPORATE_INFORMATION);
    }

    public void clickCorporateData() {
        driverTools.waitForElementToAppear(CORPORATE_DATA, 5);
        driverTools.clickWithJavascriptExecutor(CORPORATE_DATA);
    }

    public void clickRegisteredCorporate() {
        driverTools.waitForElementToAppear(REGISTERED_CORPORATE, 5);
        driverTools.click(REGISTERED_CORPORATE);
    }

    public void clickSeeDetailsBtn() {
        driverTools.waitForElementToAppear(SEE_DETAILS_BUTTON, 5);
        driverTools.scrollToElement(SEE_DETAILS_BUTTON);
        driverTools.click(SEE_DETAILS_BUTTON);
        driverTools.switchToNewTab();
    }

    public void clickEditBtn() {
        driverTools.click(EDIT);
    }

    public void fillCompanyDetails(String officeAddress, String addressDetail, String fullName, String corporateName) {

        if (driver.findElement(CORP_NAME.by()).getAttribute("value").isEmpty()) {
            driverTools.sendKeys(CORP_NAME, corporateName);

            driverTools.waitForElementToAppear(ENTITY_TYPE);
            driverTools.click(ENTITY_TYPE);
            driverTools.click(ENTITY_TYPE_FIRST_OPTION);

            driverTools.waitForElementToAppear(CORPORATE_TYPE);
            driverTools.click(CORPORATE_TYPE);
            driverTools.clickWithJavascriptExecutor(CORPORATE_TYPE_FIRST_OPTION);

            driverTools.waitForElementToAppear(INDUSTRY);
            driverTools.scrollToElement(INDUSTRY);
            driverTools.click(INDUSTRY);
            driverTools.clickWithJavascriptExecutor(INDUSTRY_FIRST_OPTION);

            driverTools.waitForElementToAppear(MONTHLY_TRAVEL_BUDGET);
            driverTools.click(MONTHLY_TRAVEL_BUDGET);
            driverTools.clickWithJavascriptExecutor(MONTHLY_TRAVEL_BUDGET_FIRST_OPTION);

            driverTools.waitForElementToAppear(CORPORATE_TAGS);
            driverTools.click(CORPORATE_TAGS);
            driverTools.clickWithJavascriptExecutor(CORPORATE_TAGS_FIRST_OPTION);

            driverTools.waitForElementToAppear(OFFICE_ADDRESS);
            driverTools.scrollToElement(OFFICE_ADDRESS);
            driverTools.click(OFFICE_ADDRESS);
            driverTools.sendKeys(OFFICE_ADDRESS, officeAddress);

            driverTools.waitForElementToAppear(OFFICE_ADDRESS_DETAIL);
            driverTools.click(OFFICE_ADDRESS_DETAIL);
            driverTools.sendKeys(OFFICE_ADDRESS_DETAIL, addressDetail);

            driverTools.waitForElementToAppear(FULL_NAME);
            driverTools.click(FULL_NAME);
            driverTools.sendKeys(FULL_NAME, fullName);

            driverTools.waitForElementToAppear(POSITION);
            driverTools.click(POSITION);
            driverTools.clickWithJavascriptExecutor(POSITION_FIRST_OPTION);

            driverTools.waitForElementToAppear(ACCOUNT_MANAGER);
            driverTools.click(ACCOUNT_MANAGER);
            driverTools.clickWithJavascriptExecutor(ACCOUNT_MANAGER_FIRST_OPTION);

            driverTools.waitForElementToAppear(SELECT_ACCOUNT_EXECUTIVE);
            driverTools.click(SELECT_ACCOUNT_EXECUTIVE);
            driverTools.clickWithJavascriptExecutor(ACCOUNT_EXECUTIVE_FIRST_OPTION);

            driverTools.waitForElementToAppear(TIN);
            driverTools.click(TIN);
            driverTools.sendKeys(TIN, String.valueOf(CoreFunctions.generateRandomInt(16)));
        }
    }

    public void fillCorporatePICDetails(String corporatePICName1, String corporatePICName2) throws InterruptedException {
        driverTools.waitForElementToAppear(SEARCH_BUTTON, 10);
        driverTools.waitForElementToAppear(EMAIL_ADDRESS);
        driverTools.scrollToElement(EMAIL_ADDRESS);
        String emailAddress = driverTools.getText(EMAIL_ADDRESS).orElseThrow();
        driverTools.clearInput(EMAIL_ADDRESS);
        if (emailAddress.equals(corporatePICName1)) {
            driverTools.sendKeys(EMAIL_ADDRESS, corporatePICName2);
        } else {
            driverTools.sendKeys(EMAIL_ADDRESS, corporatePICName1);
        }
        driverTools.clickWithJavascriptExecutor(EMAIL_ADDRESS_FIRST_OPTION);
    }

    public void clickSaveChangesBtn() {
        driverTools.waitForElementToAppear(SAVE_CHANGES_BTN, 5);
        driverTools.scrollToElement(SAVE_CHANGES_BTN);
        driverTools.clickWithJavascriptExecutor(SAVE_CHANGES_BTN);
        driverTools.waitForElementToAppear(SAVE_BTN, 5);
        driverTools.click(SAVE_BTN);
    }

    public boolean isCorporatePICUpdated(String expectedSnakbarText) {
        driverTools.waitForElementToAppear(SEARCH_BUTTON, 5);
        driverTools.waitForElementToAppear(EMAIL_ADDRESS);
        String emailAddress = driverTools.getText(EMAIL_ADDRESS).orElseThrow();
        clickSaveChangesBtn();
        Assertion.assertThat("Verify the snackbar text", isSnackbarAppeared(expectedSnakbarText), is(true));
        logger.log("Updated Email Address: " + emailAddress);
        driverTools.waitForElementToAppear(UPDATED_EMAIL_ADDRESS);
        driverTools.scrollToElement(UPDATED_EMAIL_ADDRESS);
        String updatedEmailAddress = driverTools.getText(UPDATED_EMAIL_ADDRESS).orElseThrow();
        logger.log("Email Address after update: " + updatedEmailAddress);
        return updatedEmailAddress.contains(emailAddress);
    }

    public boolean isSnackbarAppeared(String expectedSnackbarText) {
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 1);
        String snackbarText = driverTools.getText(SUCCESS_SNACKBAR).orElseThrow();
        logger.log("Snackbar text: " + snackbarText);
        return driverTools.isElementPresent(SUCCESS_SNACKBAR) && snackbarText.contains(expectedSnackbarText);
    }

    public void clickRandomLeadNames() {
        List<WebElement> rows = driverTools.waitForElementsToAppear(LEAD_NAMES).orElse(null);
        if (rows == null || rows.isEmpty()) throw new NoSuchElementException("There are no Lead Names to click");
        int selectedRow = CoreFunctions.generateRandomInt(0, rows.size()-1);
        driverTools.clickElementByCoordinateDWeb(rows.get(selectedRow));
    }

    public void setStatusFilter(String status) {
        driverTools.waitForElementToAppear(STATUS_FILTER_DROPDOWN, 3);
        driverTools.click(STATUS_FILTER_DROPDOWN);
        List<WebElement> selections = driverTools.waitForElementsToAppear(STATUS_FILTER_SELECTION).orElse(null);
        if (selections == null) throw new NoSuchElementException("There are no selection for Lead status");
        for (WebElement selection : selections) {
            if (driverTools.getText(selection).equalsIgnoreCase(status)) {
                driverTools.scrollIntoWebView(selection);
                driverTools.click(selection);
            }
        }
        driverTools.clickElementByCoordinateDWeb(STATUS_FILTER_SAVE_BUTTON);
    }

    public void clickCorporateDocument(){
        driverTools.waitForElementToDisappear(PAYMENT_CHANGE_SUCCESS_SNACKBAR);
        driverTools.waitForElementToAppear(CORPORATE_DOCUMENT, 5);
        driverTools.clickWithJavascriptExecutor(CORPORATE_DOCUMENT);
    }

    public void clickActionButtonOnLegalDocument() {
        driverTools.waitForElementToAppear(LEGAL_ACTION_BTN, 5);
        driverTools.scrollToElement(LEGAL_ACTION_BTN);
        driverTools.click(LEGAL_ACTION_BTN);
    }

    public boolean isLegalDocumentOptionsDisplayed() {
        driverTools.waitForElementToAppear(VIEW_OPTION, 5);
        driverTools.scrollToElement(VIEW_OPTION);
        driverTools.scrollToElement(VERSION_HISTORY_OPTION);
        return driverTools.isElementDisplayed(VIEW_OPTION) &&
                driverTools.isElementDisplayed(DOWNLOAD_OPTION) &&
                driverTools.isElementDisplayed(RE_UPLOAD_OPTION) &&
                driverTools.isElementDisplayed(VERSION_HISTORY_OPTION);
    }

    public boolean isPaymentDetailsPresent(){
        driverTools.waitForElementToAppear(PAYMENT_DETAILS,10);

        return driverTools.isElementPresent(PAYMENT_DETAILS);
    }

    public void clickPaymentEdit() {
        driverTools.dummyWait(5);
        driverTools.waitForElementToAppear(PAYMENT_EDIT, 5);
        driverTools.click(PAYMENT_EDIT);
    }

    public void openPaymentOptions(String fee, String limit, String terms) {
        driverTools.dummyWait(5);

        driverTools.waitForElementToAppear(ACTIVE_PAYMENT_OPTIONS, 5);
        String activePaymentOption = driverTools.getText(ACTIVE_PAYMENT_OPTIONS).orElseThrow();

        if (activePaymentOption.equalsIgnoreCase("Invoice")){
            openInstantPaymentOptions(fee);
        }
        else if (activePaymentOption.equalsIgnoreCase("Instant Payment")){
            openInvoicePaymentOptions(limit, terms, fee);
        }
    }

    public void openInstantPaymentOptions(String fee) {
        driverTools.click(INSTANT_PAYMENT_BTN);
        driverTools.waitForElementToAppear(PREMIUM_TIER,10);
        driverTools.click(PREMIUM_TIER);
        fillFee(fee);
    }

    public void openInvoicePaymentOptions(String limit, String terms, String fee) {
        driverTools.click(INVOICE_PAYMENT_BTN);

        driverTools.waitForElementToAppear(CRED_LIMIT, 10);
        driverTools.sendKeys(CRED_LIMIT, limit);

        driverTools.waitForElementToAppear(PAYMENT_TERMS, 10);
        driverTools.sendKeys(PAYMENT_TERMS, terms);

        driverTools.waitForElementToAppear(INVOICE_THRESHOLD,10);
        driverTools.sendKeys(INVOICE_THRESHOLD, fee);

        fillFee(fee);
    }

    public void fillFee(String fee){
        driverTools.waitForElementToAppear(SERV_FEE,10);
        driverTools.sendKeys(SERV_FEE, fee);

        driverTools.waitForElementToAppear(TAX_FEE,10);
        driverTools.sendKeys(TAX_FEE, fee);
    }

    public void clickPaymentSaveButton(){
        driverTools.clickWithJavascriptExecutor(SAVE_CHANGES_BTN);

        driverTools.waitForElementToAppear(CHANGE_PAYMENT_BTN,10);
        driverTools.click(CHANGE_PAYMENT_BTN);
    }

    public boolean isPaymentChangeSnackBarDisplayed() {
        driverTools.waitForElementToAppear(PAYMENT_CHANGE_SUCCESS_SNACKBAR, 5);
        return driverTools.isElementPresent(PAYMENT_CHANGE_SUCCESS_SNACKBAR);
    }

    public boolean isE_agreementGenerated(String corporateName) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formattedDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String todayDate = today.format(formattedDate);

        Locator rowLocator = new Locator("LAST_ROW_WITH_NAME_AND_DATE", By.xpath("(//table[contains(@class,'border-separate w-full')])[1]//tr[last()]/td[2]"));

        driverTools.waitForElementToAppear(rowLocator,5);
        String text=driverTools.getText(rowLocator).orElseThrow();
        logger.log("Agreement generated name : " + text);
        logger.log("Today's date : " + todayDate);
        return text.contains(todayDate);
    }

    public void clickEmployeeList(){
        driverTools.waitForElementToAppear(EMPLOYEE_LIST, 5);
        driverTools.click(EMPLOYEE_LIST);
    }
    public boolean isEmployeeNameEmailPresent() {
        return driverTools.isElementPresent(EMPLOYEE_NAME_EMAIL);
    }

    public boolean isUserRolePresent() {
        return driverTools.isElementPresent(USER_ROLE);
    }

    public boolean isDepartmentPermissionPresent() {
        return driverTools.isElementPresent(DEPARTMENT_PERMISSION);
    }

    public boolean isGroupSegmentPresent() {
        return driverTools.isElementPresent(GROUP_SEGMENT);
    }

    public boolean isStatusPresent() {
        return driverTools.isElementPresent(STATUS);
    }

    public boolean isCreatedDatePresent() {
        return driverTools.isElementPresent(CREATED_DATE);
    }

    public boolean isUpdateDatePresent() {
        return driverTools.isElementPresent(UPDATE_DATE);
    }
  
    public boolean isCompanyDetailsPresent() {
        driverTools.waitForElementToAppear(COMPANY_DETAILS,5);
        return driverTools.isElementDisplayed(COMPANY_DETAILS);

    }

    public String deleteDocument(){
        driverTools.waitForElementToDisappear(PAYMENT_CHANGE_SUCCESS_SNACKBAR);

        driverTools.waitForElementToAppear(ACTION_BUTTON_AGREEMENT);
        WebElement element=driverTools.element(ACTION_BUTTON_AGREEMENT).orElseThrow();
        driverTools.clickSvgOrNonHtmlElements(element);

        driverTools.click(DELETE_AGREEMENT);
        driverTools.waitForElementToAppear(DELETE_POPUP,2);
        driverTools.click(DELETE_POPUP);

        driverTools.waitForElementToAppear(DELETE_SNACKBAR);
        return driverTools.getText(DELETE_SNACKBAR).orElseThrow();
    }

    public boolean isAddSuperAdminButtonPresent() {
        driverTools.waitForElementToAppear(ADD_SUPER_ADMIN_BUTTON, 5);
        return driverTools.isElementDisplayed(ADD_SUPER_ADMIN_BUTTON);
    }

    public void clickAddSuperAdminButton() {
        driverTools.click(ADD_SUPER_ADMIN_BUTTON);
    }

    public boolean isAddSuperAdminPopupDisplayed() {
        driverTools.waitForElementToAppear(ADD_SUPER_ADMIN_POPUP_TITLE, 5);
        return driverTools.isElementDisplayed(ADD_SUPER_ADMIN_POPUP_TITLE);
    }

    public boolean isSuperAdminNamePresent() {
        driverTools.waitForElementToAppear(SUPER_ADMIN_EMPLOYEE_NAME, 5);
        return driverTools.isElementDisplayed(SUPER_ADMIN_EMPLOYEE_NAME);
    }

    public void fillSuperAdminName(String value) {
        driverTools.sendKeys(SUPER_ADMIN_EMPLOYEE_NAME, value);
    }

    public boolean isSuperAdminEmailPresent() {
        driverTools.waitForElementToAppear(SUPER_ADMIN_EMPLOYEE_EMAIL, 5);
        return driverTools.isElementDisplayed(SUPER_ADMIN_EMPLOYEE_EMAIL);
    }

    public void fillSuperAdminEmail(String value) {
        driverTools.sendKeys(SUPER_ADMIN_EMPLOYEE_EMAIL, value);
    }

    public boolean isSuperAdminPhoneNumberPresent() {
        driverTools.waitForElementToAppear(SUPER_ADMIN_PHONE_NUMBER, 5);
        return driverTools.isElementDisplayed(SUPER_ADMIN_PHONE_NUMBER);
    }

    public void fillSuperAdminPhoneNumber(String value) {
        driverTools.sendKeys(SUPER_ADMIN_PHONE_NUMBER, value);
    }

    public boolean isSuperAdminDepartmentFieldPresent() {
        driverTools.waitForElementToAppear(SUPER_ADMIN_DEPARTMENT_FIELD, 5);
        return driverTools.isElementDisplayed(SUPER_ADMIN_DEPARTMENT_FIELD);
    }

    public boolean isSuperAdminGroupFieldPresent() {
        driverTools.waitForElementToAppear(SUPER_ADMIN_GROUP_FIELD, 5);
        return driverTools.isElementDisplayed(SUPER_ADMIN_GROUP_FIELD);
    }

    public boolean isSuperAdminTravelPolicyFieldPresent() {
        driverTools.waitForElementToAppear(SUPER_ADMIN_TRAVEL_POLICY_FIELD, 5);
        return driverTools.isElementDisplayed(SUPER_ADMIN_TRAVEL_POLICY_FIELD);
    }

    public void clickSaveSuperAdmin() {
        driverTools.click(SUPER_ADMIN_SAVE_BUTTON);
    }

    public String getFirstEmployeeName() {
        driverTools.waitForElementToAppear(FIRST_EMPLOYEE_NAME, 5);
        return driverTools.getText(FIRST_EMPLOYEE_NAME).orElseThrow();
    }

    public String getFirstEmployeeEmail() {
        return driverTools.getText(FIRST_EMPLOYEE_EMAIL).orElseThrow();
    }

    public String getFirstEmployeeRole() {
        return driverTools.getText(FIRST_EMPLOYEE_ROLE).orElseThrow();
    }

    public String copyFirstEmployeeRegistrationLink() {
        driverTools.waitForElementToAppear(FIRST_EMPLOYEE_COPY_LINK_BUTTON, 5);
        driverTools.prepareBrowserClipboardCapture();
        driverTools.click(FIRST_EMPLOYEE_COPY_LINK_BUTTON);
        return driverTools.getTextFromClipboardBrowser();
    }

    public void openUrl(String url) {
        driverTools.get(url);
    }

    public boolean isRegisterPasswordFieldPresent() {
        driverTools.waitForElementToAppear(REGISTER_PASSWORD_INPUT, 10);
        return driverTools.isElementDisplayed(REGISTER_PASSWORD_INPUT);
    }

    public void fillRegisterPassword(String password) {
        driverTools.sendKeys(REGISTER_PASSWORD_INPUT, password);
    }

    public void clickRegister() {
        driverTools.clickWithJavascriptExecutor(REGISTER_SUBMIT_BUTTON);
    }

    public boolean isRegisterSuccessTitleDisplayed() {
        driverTools.waitForElementToAppear(REGISTER_SUCCESS_TITLE, 10);
        return driverTools.isElementDisplayed(REGISTER_SUCCESS_TITLE);
    }

    public void clickRegisterSuccessOk() {
        driverTools.clickWithJavascriptExecutor(REGISTER_SUCCESS_OK_BUTTON);
    }
}
