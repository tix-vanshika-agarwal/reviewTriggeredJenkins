package com.tiket.web.dashboard.page.b2bCorp_internal;

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
import java.util.Locale;
import java.util.stream.Collectors;

public class TiketPlusForCorporateInternal<T extends WebDriver> extends BasePage<T> {

    public TiketPlusForCorporateInternal(Session<T> session) {
        super(session);
    }

    private static final Locator QUOTATION_LIST_HEADER = new Locator("QUOTATION_LIST_HEADER", new By.ByXPath("(//span[text()='Quotation List'])[last()]"));
    private static final Locator NEW_QUOTATION_BUTTON = new Locator("NEW_QUOTATION_BUTTON", new By.ByXPath("//button//span[text()='New quotation']"));
    private static final Locator CREATE_NEW_QUOTATION_PAGE = new Locator("CREATE_NEW_QUOTATION_PAGE", new By.ByXPath("//span[text()='Create New Quotation']"));
    private static final Locator NEXT_QUOTATION_DETAILS = new Locator("NEXT_QUOTATION_DETAILS", new By.ByXPath("//button//span[text()='Next: Quotation Details']"));
    private static final Locator NEXT_VENDOR_PAYMENT = new Locator("NEXT_VENDOR_PAYMENT", new By.ByXPath("//button//span[text()='Next: Vendor Payment']"));
    private static final Locator NEXT_REVIEW_AND_SUBMIT = new Locator("NEXT_REVIEW_AND_SUBMIT", new By.ByXPath("//button//span[text()='Next: Review & Submit']"));
    private static final Locator SUBMIT_TO_LEAD_BUTTON = new Locator("SUBMIT_TO_LEAD_BUTTON", new By.ByXPath("//button//span[text()='Submit to lead']"));
    private static final Locator SUBMIT_QUOTATION_BUTTON = new Locator("SUBMIT_QUOTATION_BUTTON", new By.ByXPath("//button//span[text()='Submit quotation']"));

    private static final Locator CORPORATE_CLIENT_NAME_INPUT = new Locator("CORPORATE_CLIENT_NAME_INPUT", By.xpath("//input[@id='corporate-name']"));
    private static final Locator PIC_EMPLOYEE_NAME_INPUT = new Locator("PIC_EMPLOYEE_NAME_INPUT", By.xpath("//input[@id='employee-name']"));
    private static final Locator NEW_QUOTATION_CURRENT_TAB_NAME = new Locator("NEW_QUOTATION_CURRENT_TAB_NAME", By.xpath("//span[text()='Create New Quotation']//following-sibling::div//div[contains(@class,'bg-[#E7F2FF]')]//span"));
    private static final Locator QUOTATION_DETAILS_START_DATE = new Locator("QUOTATION_DETAILS_START_DATE", By.xpath("//input[@id='order-start-date']"));
    private static final Locator QUOTATION_DETAILS_END_DATE = new Locator("QUOTATION_DETAILS_END_DATE", By.xpath("//input[@id='order-end-date']"));

    private static final Locator FIRST_CARD_OPTION = new Locator("FIRST_CARD_OPTION", By.xpath("(//div[contains(@class,'card')]//ul//li)[1]"));
    private static final Locator FIRST_DIV_OPTION = new Locator("FIRST_DIV_OPTION", By.xpath("(//div[contains(@class,'overflow-auto')])[2]//div[1]"));
    private static final Locator CURRENCY_INPUT = new Locator("CURRENCY_INPUT", By.xpath("//input[@id='currency']"));
    private static final Locator PRODUCT_CATEGORY_INPUT = new Locator("PRODUCT_CATEGORY_INPUT", By.xpath("//input[@id='product-category']"));
    private static final Locator REGION_HOTEL_INPUT = new Locator("REGION_HOTEL_INPUT", By.xpath("//input[@id='region-hotel']"));
    private static final Locator REVENUE_CATEGORY_INPUT = new Locator("REVENUE_CATEGORY_INPUT", By.xpath("//input[@id='revenue-category']"));
    private static final Locator PAYMENT_DUE_DATE = new Locator("PAYMENT_DUE_DATE", By.xpath("//input[@id='payment-due-date']"));
    private static final Locator VENDOR_NAME_INPUT = new Locator("VENDOR_NAME_INPUT", By.xpath("//input[@id='vendor-name']"));
    private static final Locator PAYMENT_TYPE_INPUT = new Locator("PAYMENT_TYPE_INPUT", By.xpath("//input[@id='payment-type']"));
    private static final Locator PRODUCT_PROVIDER_INPUT = new Locator("PRODUCT_PROVIDER_INPUT", By.xpath("//input[@id='product-provider']"));
    private static final Locator VENDOR_BANK_NAME_INPUT = new Locator("VENDOR_BANK_NAME_INPUT", By.xpath("//input[@id='vendor-bank-name']"));
    private static final Locator ACCOUNT_NUMBER = new Locator("ACCOUNT_NUMBER", By.xpath("//input[@id='bank-account-number']"));
    private static final Locator MODAL_HEADING = new Locator("MODAL_HEADING", By.xpath("(//div[contains(@class,'Modal')]//span)[1]"));
    private static final Locator MODAL_DESCRIPTION = new Locator("MODAL_DESCRIPTION", By.xpath("(//div[contains(@class,'Modal')]//span)[2]"));
    private static final Locator MODAL_SUBMIT_BUTTON = new Locator("MODAL_SUBMIT_BUTTON", By.xpath("//div[contains(@class,'Modal')]//button[contains(normalize-space(.),'Submit')]"));
    private static final Locator ACCOUNT_VERIFIED_TEXT = new Locator("ACCOUNT_VERIFIED_TEXT", By.xpath("//span[contains(normalize-space(.),'account verified successfully')]"));
    private static final Locator CHECK_ACCOUNT_BUTTON = new Locator("CHECK_ACCOUNT_BUTTON", By.xpath("//button[normalize-space(.)='Check account']"));
    private static final Locator RFP_NUMBER_INPUT = new Locator("RFP_NUMBER_INPUT", By.xpath("//input[@id='rfp-number']"));
    private static final Locator ITEM_AMOUNT_INPUT = new Locator("ITEM_AMOUNT_INPUT", By.xpath("//input[@id='net-amount']"));
    private static final Locator ITEM_QUANTITY_INPUT = new Locator("ITEM_QUANTITY_INPUT", By.xpath("//input[@id='qty']"));
    private static final Locator ITEM_TAX_INPUT = new Locator("ITEM_TAX_INPUT", By.xpath("//input[@id='tax']"));
    private static final Locator QUOTATION_NUMBER_INPUT = new Locator("QUOTATION_NUMBER_INPUT", By.xpath("//input[@id='quotation-number']"));
    private static final Locator ITEM_NAME_INPUT = new Locator("ITEM_NAME_INPUT", By.xpath("//input[@id='item-name']"));
    private static final Locator ITEM_DESCRIPTION_INPUT = new Locator("ITEM_DESCRIPTION_INPUT", By.xpath("//input[@id='description']"));

    private static final Locator INVOICE_BILLING_HEADER = new Locator("INVOICE_BILLING_HEADER", By.xpath("//span[@title='Invoices & Billing']"));
    private static final Locator CREATE_NEW_INVOICE_BUTTON = new Locator("CREATE_NEW_INVOICE_BUTTON", By.xpath("//span[text()='Create new invoice']"));
    private static final Locator CORPORATE_DROPDOWN = new Locator("CORPORATE_DROPDOWN", By.xpath("//input[contains(@id,'select-corporate')]"));
    private static final Locator SYSTEM_ORDER_DROPDOWN = new Locator("SYSTEM_ORDER_DROPDOWN", By.xpath("//input[@id='invoice-type']"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", By.xpath("//button[normalize-space()='Continue']"));
    private static final Locator GENERATED_INVOICE_ID = new Locator("GENERATED_INVOICE_ID", By.xpath("//span[contains(normalize-space(),'GTNCORP-')]"));
    private static final Locator STATUS = new Locator("STATUS", By.xpath("//div[contains(@class,'Label_label')]"));
    private static final Locator SNACKBAR = new Locator("SNACKBAR", By.xpath("//div[contains(@class,'Snackbar_text_content')]"));
    private static final Locator ADD_ORDER_BUTTON = new Locator("ADD_ORDER_BUTTON", By.xpath("//span[contains(text(), 'Add')]"));
    private static final Locator DONE_BUTTON = new Locator("DONE_BUTTON", By.xpath("//button[normalize-space()='Done']"));
    private static final Locator REVIEW_PAGE_HEADER = new Locator("REVIEW_PAGE_HEADER", By.xpath("//span[contains(normalize-space(),'Review')]"));
    private static final Locator TOTAL_AMOUNT_DUE = new Locator("TOTAL_AMOUNT_DUE", By.xpath("//span[@id='total-due']"));
    private static final Locator PUBLISH_INVOICE_BUTTON = new Locator("PUBLISH_INVOICE_BUTTON", By.xpath("//button[normalize-space()='Publish invoice']"));
    private static final Locator PUBLISH_AND_SEND_BUTTON = new Locator("PUBLISH_AND_SEND_BUTTON",By.xpath("//button[normalize-space()='Publish & send']"));
    private static final Locator CHECKBOX = new Locator("ORDER_CHECKBOX", By.xpath("(//tbody//span[contains(@class,'custom_checkbox')])[1]"));
    private static final Locator FAILED_ISSUED_ORDER = new Locator("FAILED_ISSUED_ORDER", By.xpath("//tbody//tr[.//*[normalize-space()='Failed']]"));
    private static final Locator SYSTEM_ORDER = new Locator("SYSTEM_ORDER_OPTION", By.xpath("//li[normalize-space()='System Order' or contains(@value,'SYSTEM_ORDER')]"));
    private static final Locator NOTES_TEXTAREA = new Locator("NOTES_TEXTAREA", By.xpath("//textarea[@placeholder='Notes']"));
    private static final Locator TERMS_AND_CONDITIONS_TEXTAREA = new Locator("TERMS_AND_CONDITIONS_TEXTAREA", By.xpath("//textarea[@placeholder='Terms & Conditions']"));

    private static final Locator INPUT_ORDER_ID = new Locator("INPUT_ORDER_ID", By.xpath("//div[contains(@class,'modal')]//input[contains(@inputmode, 'numeric')]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//span[contains(.,'Search')]"));
    private static final Locator SEARCH_ORDER_IN_ROW = new Locator("SEARCH_ORDER_IN_ROW", By.xpath("(//div[contains(@class,'modal')]//tr//td)[2]"));
    private static final Locator FIRST_ORDER_ID_ROW = new Locator("FIRST_ORDER_ID_ROW", By.xpath("(//div[contains(@class,'modal')]//tbody//tr[1]//td)[2]"));

    private static final Locator Action_button = new Locator("Action_button", By.xpath("//table//tr[1]//td[last()]//div[contains(@class,'inline')]"));
    private static final Locator INVOICE_NUMBER_SEARCH_INPUT = new Locator("INVOICE_NUMBER_SEARCH_INPUT", By.xpath("//input[@type='text' and @placeholder='Invoice Number']"));
    private static final Locator INVOICE_NUMBER_SEARCH_BUTTON = new Locator("INVOICE_NUMBER_SEARCH_BUTTON", By.xpath("//button[normalize-space(.)='Search']"));
    private static final Locator VIEW_BUTTON = new Locator("VIEW_BUTTON", By.xpath("//button[normalize-space(.)='View'] | //li[normalize-space(.)='View'] | //span[normalize-space(.)='View']"));
    private static final Locator MORE_ACTION_BUTTON = new Locator("MORE_ACTION_BUTTON", By.xpath("//span[text()='More Action']"));
    private static final Locator CANCEL_INVOICE_OPTION = new Locator("CANCEL_INVOICE_OPTION", By.xpath("//p[text()='Cancel Invoice']"));
    private static final Locator CANCEL_INVOICE_CHECKBOX = new Locator("CANCEL_INVOICE_CHECKBOX", By.xpath("//span[contains(@class,'checkbox')]"));
    private static final Locator CANCEL_INVOICE_CONFIRM_BUTTON = new Locator("CANCEL_INVOICE_CONFIRM_BUTTON", By.xpath("//button//span[text()='Cancel invoice']"));

    private static final Locator REQUEST_NUMBER_INPUT = new Locator("REQUEST_NUMBER_INPUT", By.xpath("//input[@placeholder='Request Number']"));
    private static final Locator REQUEST_ACTION_DOTS = new Locator("REQUEST_ACTION_DOTS", By.xpath("(//table//tbody//tr[1]//td[last()]//*[local-name()='svg' and contains(@class, 'cursor-pointer')])[1]"));
    private static final Locator START_PROGRESS_OPTION = new Locator("START_PROGRESS_OPTION", By.xpath("//li[.//span[contains(text(), 'Start Progress') or contains(text(), 'Start progress')]]"));
    private static final Locator CONFIRM_START_PROGRESS_BUTTON = new Locator("CONFIRM_START_PROGRESS_BUTTON", By.xpath("//button//span[text()='Start progress' and contains(@class, 'Button_truncation')] | (//button//span[text()='Start progress'])[last()]"));
    private static final Locator SNACKBAR_CONTAINER = new Locator("SNACKBAR_CONTAINER", By.xpath("//div[contains(@class, 'SnackbarsPositioner')]"));
    private static final Locator VIEW_DETAILS_OPTION = new Locator("VIEW_DETAILS_OPTION", By.xpath("//li[.//span[contains(text(), 'View Details') or contains(text(), 'View details')]]"));
    private static final Locator CREATE_QUOTATION_BUTTON = new Locator("CREATE_QUOTATION_BUTTON", By.xpath("//span[text()='Create Quotation']"));
    private static final Locator BOOKING_MANAGEMENT_PAGE_HEADER = new Locator("BOOKING_MANAGEMENT_PAGE_HEADER", By.xpath("//span[text()='Booking Management' and contains(@class, 'Text_weight_bold')]"));
    private static final Locator BOOKING_MANAGEMENT_TABLE_HEADERS = new Locator("BOOKING_MANAGEMENT_TABLE_HEADERS", By.xpath("//table//thead//th"));
    private static final Locator BOOKING_MANAGEMENT_TABLE_ROWS = new Locator("BOOKING_MANAGEMENT_TABLE_ROWS", By.xpath("//table//tbody//tr"));




    public boolean isQuotationListPageOpened() {
        driverTools.waitForElementToAppear(QUOTATION_LIST_HEADER);
        return driverTools.isElementPresent(QUOTATION_LIST_HEADER);
    }

    public void clickNewQuotationButton() {
        driverTools.waitForElementToBeClickable(NEW_QUOTATION_BUTTON);
        driverTools.clickWithJavascriptExecutor(NEW_QUOTATION_BUTTON);
    }

    public Boolean isCreateNewQuotationPageOpened() {
        return driverTools.waitForElementToAppear(CREATE_NEW_QUOTATION_PAGE).isPresent();
    }
    public String getNewQuotationCurrentTabName() {
        driverTools.dummyWait(2);
        return driverTools.getText(NEW_QUOTATION_CURRENT_TAB_NAME).orElseThrow();
    }

    public void selectCorporateClientName(String corporateClientName) {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(CORPORATE_CLIENT_NAME_INPUT);
        driverTools.dummyWait(1);

        String optionXpath = "//div[contains(@class,'list_menu')]//span[normalize-space(.)='%s']".formatted(corporateClientName);
        Locator searchResultOption = new Locator("Dropdown Option: " + corporateClientName, By.xpath(optionXpath));
        driverTools.waitForElementsToAppear(searchResultOption);
        driverTools.click(searchResultOption);
    }

    public void selectPicEmployeeName(String picEmployeeName) {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(PIC_EMPLOYEE_NAME_INPUT);
        driverTools.dummyWait(1);

        String optionXpath = "//div[contains(@class,'list_menu')]//span[normalize-space(.)='%s']".formatted(picEmployeeName);
        Locator searchResultOption = new Locator("Dropdown Option: " + picEmployeeName, By.xpath(optionXpath));
        driverTools.waitForElementsToAppear(searchResultOption);
        driverTools.click(searchResultOption);
    }

    public void clickNextQuotationDetailsButton() {
        driverTools.waitForElementToBeClickable(NEXT_QUOTATION_DETAILS);
        driverTools.clickWithJavascriptExecutor(NEXT_QUOTATION_DETAILS);
    }

    public String getCorporateClientName() {
        return driverTools.getValue(CORPORATE_CLIENT_NAME_INPUT);
    }
    public String getPicEmployeeName() {
        return driverTools.getValue(PIC_EMPLOYEE_NAME_INPUT);
    }

    public String getQuotationDetailsClientName() {
        Locator CLIENT_NAME = new Locator("CLIENT_NAME", By.xpath("//span[normalize-space(.)='Quote Overview']//following-sibling::div[1]//div[2]//span[2]"));
        return driverTools.getText(CLIENT_NAME).orElseThrow();
    }
    public String getQuotationDetailsPicEmployeeName() {
        Locator PIC_NAME = new Locator("PIC_NAME", By.xpath("//span[normalize-space(.)='Quote Overview']//following-sibling::div//div[3]//span[2]"));
        return driverTools.getText(PIC_NAME).orElseThrow();
    }
    public String getQuotationDetailsQuotationNumber() {
        String quotationNumber = driverTools.getValue(QUOTATION_NUMBER_INPUT);
        logger.log("Quotation Number: " + quotationNumber);
        return quotationNumber;
    }

    public void setQuotationDetailsItemName(String itemName) {
        driverTools.scrollElementToCenter(ITEM_NAME_INPUT);
        driverTools.clearInput(ITEM_NAME_INPUT);
        driverTools.sendKeys(ITEM_NAME_INPUT, itemName);
    }
    public void setQuotationDetailsItemDescription(String itemDescription) {
        driverTools.clearInput(ITEM_DESCRIPTION_INPUT);
        driverTools.sendKeys(ITEM_DESCRIPTION_INPUT, itemDescription);
    }
    public void setQuotationDetailsItemAmount(String itemAmount) {
        driverTools.clearInput(ITEM_AMOUNT_INPUT);
        driverTools.sendKeys(ITEM_AMOUNT_INPUT, itemAmount);
    }
    public void setQuotationDetailsItemQuantity(String itemQuantity) {
        driverTools.clearInput(ITEM_QUANTITY_INPUT);
        driverTools.sendKeys(ITEM_QUANTITY_INPUT, itemQuantity);
    }

    public String getQuotationDetailsItemTax() {
        String tax = driverTools.getValue(ITEM_TAX_INPUT).replaceAll("[^0-9]", "").trim();
        logger.log("Tax => " + tax);
        return tax;
    }

    public String getQuotationDetailsTotalAmount() {
        Locator TOTAL_AMOUNT = new Locator("TOTAL_AMOUNT", By.xpath("//span[normalize-space(.)='Total amount']/ancestor::div[1]/following-sibling::div[1]/span"));
        return driverTools.getText(TOTAL_AMOUNT).orElseThrow().replaceAll("[^0-9]", "").trim();
    }

    public String getPaymentDueDate() {
        LocalDate firstOfNextMonth = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        DateTimeFormatter returnFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US);
        return firstOfNextMonth.format(returnFormatter);
    }

    public String setStartDate(String dateStr, int dateOffset) {
        driverTools.waitForElementToBeClickable(QUOTATION_DETAILS_START_DATE);
        driverTools.click(QUOTATION_DETAILS_START_DATE);

        Locator NEXT_MONTH_BUTTON = new Locator("NEXT_MONTH_BUTTON", By.xpath("(//div[contains(@class, 'DatePicker')]//*[name()='svg' and contains(@class, 'right')])[2]"));
        driverTools.waitForElementToBeClickable(NEXT_MONTH_BUTTON);
        driverTools.click(NEXT_MONTH_BUTTON);

        DateTimeFormatter inputParser = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US);
        LocalDate paymentDueDate = LocalDate.parse(dateStr.trim(), inputParser);

        LocalDate startDate = paymentDueDate.plusDays(dateOffset);
        DateTimeFormatter ariaFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy EEEE", Locale.US);
        String targetAriaLabel = startDate.format(ariaFormatter);

        String dateTileXpath = "(//span[@aria-label='%s'])[1]".formatted(targetAriaLabel);
        Locator dateTile = new Locator("Start Date Tile: " + targetAriaLabel, By.xpath(dateTileXpath));

        driverTools.dummyWait(1);
        driverTools.waitForElementToBeClickable(dateTile);
        driverTools.click(dateTile);

        DateTimeFormatter returnFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US);
        String startDateFormatted = startDate.format(returnFormatter);
        logger.log("Start Date : " + startDateFormatted);
        return startDateFormatted;
    }

    public String setEndDate(String startDateStr, int dateOffset) {
        driverTools.waitForElementToBeClickable(QUOTATION_DETAILS_END_DATE);
        driverTools.click(QUOTATION_DETAILS_END_DATE);

        Locator NEXT_MONTH_BUTTON = new Locator("NEXT_MONTH_BUTTON", By.xpath("(//div[contains(@class, 'DatePicker')]//*[name()='svg' and contains(@class, 'right')])[3]"));
        driverTools.waitForElementToBeClickable(NEXT_MONTH_BUTTON);
        driverTools.click(NEXT_MONTH_BUTTON);

        DateTimeFormatter inputParser = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US);
        LocalDate startDate = LocalDate.parse(startDateStr.trim(), inputParser);

        LocalDate endDate = startDate.plusDays(dateOffset);
        DateTimeFormatter ariaFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy EEEE", Locale.US);
        String targetAriaLabel = endDate.format(ariaFormatter);

        String dateTileXpath = "(//span[@aria-label='%s'])[2]".formatted(targetAriaLabel);
        Locator dateTile = new Locator("End Date Tile: " + targetAriaLabel, By.xpath(dateTileXpath));

        driverTools.dummyWait(1);
        driverTools.waitForElementToBeClickable(dateTile);
        driverTools.click(dateTile);

        String endDateFormatted = endDate.format(inputParser);
        logger.log("End Date : " + endDateFormatted);
        return endDateFormatted;
    }

    public void clickNextVendorPaymentButton() {
        driverTools.waitForElementToBeClickable(NEXT_VENDOR_PAYMENT);
        driverTools.clickWithJavascriptExecutor(NEXT_VENDOR_PAYMENT);
    }

    public void selectVendorPaymentInventorySource(String inventorySource) {
        Locator INVENTORY_SOURCE_INPUT = new Locator("INVENTORY_SOURCE_INPUT", By.xpath("//input[contains(@class,'Radio') and @value='\"%s\"']".formatted(inventorySource)));
        driverTools.waitForElementToPresence(INVENTORY_SOURCE_INPUT);
        driverTools.clickWithJavascriptExecutor(INVENTORY_SOURCE_INPUT);
    }

    public boolean getVendorPaymentInventorySource(String inventorySource) {
        Locator INVENTORY_SOURCE_INPUT = new Locator("INVENTORY_SOURCE_INPUT", By.xpath("//input[contains(@class,'Radio') and @value='\"%s\"']".formatted(inventorySource)));
        return driverTools.isElementSelected(INVENTORY_SOURCE_INPUT);
    }

    public String getVendorPaymentRfpNumber() {
        String rfpNumber = driverTools.getValue(RFP_NUMBER_INPUT).trim();
        logger.log("RFP Number : " + rfpNumber);
        return rfpNumber;
    }

    public void selectVendorPaymentCurrency() {
        driverTools.waitForElementToAppear(CURRENCY_INPUT);
        driverTools.click(CURRENCY_INPUT);
        driverTools.click(FIRST_CARD_OPTION);
    }
    public void selectVendorPaymentProductCategory() {
        driverTools.waitForElementToAppear(PRODUCT_CATEGORY_INPUT);
        driverTools.click(PRODUCT_CATEGORY_INPUT);
        driverTools.click(FIRST_CARD_OPTION);
    }
    public void selectVendorPaymentRegionalHotel() {
        driverTools.waitForElementToAppear(REGION_HOTEL_INPUT);
        driverTools.click(REGION_HOTEL_INPUT);
        driverTools.click(FIRST_DIV_OPTION);
    }
    public void selectVendorPaymentRevenueCategory() {
        driverTools.waitForElementToAppear(REVENUE_CATEGORY_INPUT);
        driverTools.click(REVENUE_CATEGORY_INPUT);
        driverTools.click(FIRST_CARD_OPTION);
    }
    public String selectVendorPaymentPaymentDueDate(String paymentDueDateStr) {
        driverTools.waitForElementToBeClickable(PAYMENT_DUE_DATE);
        driverTools.click(PAYMENT_DUE_DATE);

        Locator NEXT_MONTH_BUTTON = new Locator("NEXT_MONTH_BUTTON", By.xpath("//div[contains(@class, 'DatePicker')]//*[name()='svg' and contains(@class, 'right')]"));
        driverTools.click(NEXT_MONTH_BUTTON);

        DateTimeFormatter inputParser = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US);
        LocalDate paymentDueDate = LocalDate.parse(paymentDueDateStr.trim(), inputParser);

        DateTimeFormatter ariaFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy EEEE", Locale.US);
        String targetAriaLabel = paymentDueDate.format(ariaFormatter);

        String dateTileXpath = "//span[@aria-label='%s']".formatted(targetAriaLabel);
        Locator dateTile = new Locator("Payment Due Date Tile: " + targetAriaLabel, By.xpath(dateTileXpath));
        Locator BACK_BTN = new Locator("", By.xpath("(//input[@id='payment-due-date']//following-sibling::div)[last()]//button"));

        driverTools.click(dateTile);
        driverTools.click(BACK_BTN);

        String paymentDueDateFormatted = paymentDueDate.format(inputParser);
        logger.log("Payment Due Date : " + paymentDueDateFormatted);
        return paymentDueDateFormatted;
    }
    public void selectVendorPaymentVendorName() {
        driverTools.waitForElementToAppear(VENDOR_NAME_INPUT);
        driverTools.clickWithJavascriptExecutor(VENDOR_NAME_INPUT);
        driverTools.clickWithJavascriptExecutor(FIRST_DIV_OPTION);
    }
    public void selectVendorPaymentProductProvider() {
        driverTools.waitForElementToAppear(PRODUCT_PROVIDER_INPUT);
        driverTools.clickWithJavascriptExecutor(PRODUCT_PROVIDER_INPUT);
        driverTools.clickWithJavascriptExecutor(FIRST_DIV_OPTION);
    }
    public void selectVendorPaymentPaymentType() {
        driverTools.waitForElementToAppear(PAYMENT_TYPE_INPUT);
        driverTools.clickWithJavascriptExecutor(PAYMENT_TYPE_INPUT);
        driverTools.clickWithJavascriptExecutor(FIRST_CARD_OPTION);
    }
    public void selectVendorPaymentVendorBankName(String bankName) {
        driverTools.waitForElementToAppear(VENDOR_BANK_NAME_INPUT);
        driverTools.clickWithJavascriptExecutor(VENDOR_BANK_NAME_INPUT);
        Locator OPTION_NAME = new Locator("OPTION_NAME", By.xpath("//div[contains(@class,'list_menu') and normalize-space(.)='%s']".formatted(bankName)));
        driverTools.click(OPTION_NAME);
    }

    public void selectVendorPaymentFirstVendorBankNameForAmSupervisor() {
        driverTools.waitForElementToAppear(VENDOR_BANK_NAME_INPUT);
        driverTools.clickWithJavascriptExecutor(VENDOR_BANK_NAME_INPUT);
        Locator FIRST_BANK_NAME_OPTION = new Locator("FIRST_BANK_NAME_OPTION", By.xpath("(//div[contains(@class,'list_menu')])[1]"));
        driverTools.waitForElementToBeClickable(FIRST_BANK_NAME_OPTION);
        driverTools.click(FIRST_BANK_NAME_OPTION);
    }
    public void setVendorPaymentBankAccountNumber(String bankAccountNumber) {
        driverTools.waitForElementToAppear(ACCOUNT_NUMBER);
        driverTools.clearInput(ACCOUNT_NUMBER);
        driverTools.sendKeys(ACCOUNT_NUMBER, bankAccountNumber);
    }
    public void clickVendorPaymentCheckAccount() {
        driverTools.waitForElementToBeClickable(CHECK_ACCOUNT_BUTTON);
        driverTools.clickWithJavascriptExecutor(CHECK_ACCOUNT_BUTTON);
    }
    public String getVendorPaymentAccountVerifyText() {
        driverTools.waitForElementToAppear(ACCOUNT_VERIFIED_TEXT);
        return driverTools.getText(ACCOUNT_VERIFIED_TEXT).orElseThrow(() -> new RuntimeException("Account Verification text not found"));
    }

    public void clickNextReviewAndSubmitButton() {
        driverTools.waitForElementToBeClickable(NEXT_REVIEW_AND_SUBMIT);
        driverTools.clickWithJavascriptExecutor(NEXT_REVIEW_AND_SUBMIT);
    }

    /**
     * Live evidence (repeated preprod runs) showed the plain "Next: Review & Submit" click
     * intermittently fails to actually switch the wizard tab away from Vendor Payment - the
     * click is dispatched (no error), but the Review & Submit tab's content/buttons never
     * render, so a later wait for a Review & Submit page element times out. Re-clicking
     * "Next: Review & Submit" a few times, checking each time whether the Review & Submit
     * page has actually loaded, reliably gets past this race instead of failing outright.
     */
    public void clickNextReviewAndSubmitButtonForAmSupervisor() {
        int maxAttempts = 5;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            waitForButtonTextToBeEnabled("Next: Review & Submit", 15);
            driverTools.waitForElementToBeClickable(NEXT_REVIEW_AND_SUBMIT);
            driverTools.clickWithJavascriptExecutor(NEXT_REVIEW_AND_SUBMIT);
            if (driverTools.waitForElementToAppear(SUBMIT_QUOTATION_BUTTON, 20).isPresent()) {
                return;
            }
            logger.log("Review & Submit page did not load after attempt " + attempt + " to click 'Next: Review & Submit'; retrying.");
        }
        throw new com.tiket.model.Exception.DriverToolsException(
                "Failed to navigate to Review & Submit page after " + maxAttempts + " attempts to click 'Next: Review & Submit'");
    }

    // The "Next: Review & Submit" button can be transiently disabled (client-side validation
    // re-check) right when we try to click it, silently swallowing the click. Poll its real
    // disabled state via JS before each click attempt instead of relying solely on Selenium's
    // elementToBeClickable, which does not reliably reflect this component's disabled styling.
    private void waitForButtonTextToBeEnabled(String buttonText, int timeoutSeconds) {
        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L;
        do {
            Object disabled = ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "var b = Array.from(document.querySelectorAll('button')).find(x => x.innerText.trim()===arguments[0]); return b ? b.disabled : true;",
                    buttonText);
            if (Boolean.FALSE.equals(disabled)) {
                return;
            }
            driverTools.dummyWait(1);
        } while (System.currentTimeMillis() < deadline);
        logger.log("Button '" + buttonText + "' still reports disabled after " + timeoutSeconds + "s wait; proceeding anyway.");
    }

    public String getReviewAndSubmitDetailByName(String name) {
        Locator DETAIL_TEXT = new Locator("DETAIL_TEXT", By.xpath("//span[contains(normalize-space(.),'%s')]/following-sibling::*[self::span or self::div]".formatted(name)));
        driverTools.waitForElementToPresence(DETAIL_TEXT);

        List<WebElement> elements = driver.findElements(DETAIL_TEXT.by());
        return elements.stream()
                .map(element -> element.getText().trim())
                .filter(text -> !text.isEmpty())
                .collect(Collectors.joining(", "));
    }

    public String getReviewAndSubmitDetailByHeadingAndName(String heading, String name) {
        Locator DETAIL_TEXT = new Locator("DETAIL_TEXT", By.xpath("//span[contains(normalize-space(.),'%s')]//following-sibling::div//span[contains(normalize-space(.),'%s')]/following-sibling::*[self::span or self::div]".formatted(heading, name)));
        driverTools.waitForElementToPresence(DETAIL_TEXT);

        List<WebElement> elements = driver.findElements(DETAIL_TEXT.by());
        return elements.stream()
                .map(element -> element.getText().trim())
                .filter(text -> !text.isEmpty())
                .collect(Collectors.joining(", "));
    }

    public String getReviewAndSubmitTableDetailByHeading(String tableName, String headingName, boolean isNumeric) {
        Locator headersLocator = new Locator("Table Headers", By.xpath("//span[contains(normalize-space(.),'%s')]/ancestor::div[1]/following-sibling::div[1]//table/thead/tr/th".formatted(tableName)));
        driverTools.waitForElementToPresence(headersLocator);
        List<WebElement> headers = driver.findElements(headersLocator.by());

        int columnIndex = -1;
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().trim().equalsIgnoreCase(headingName.trim())) {
                columnIndex = i + 1;
                break;
            }
        }

        if (columnIndex == -1) {
            throw new RuntimeException("Specified table heading column '" + headingName + "' was not found.");
        }

        String cellXpath = "(//span[contains(normalize-space(.),'%s')]/ancestor::div[1]/following-sibling::div[1]//table/tbody/tr[1]/td)[%d]".formatted(tableName, columnIndex);
        Locator targetCellLocator = new Locator("First Row Cell Index " + columnIndex, By.xpath(cellXpath));

        driverTools.waitForElementToPresence(targetCellLocator);
        WebElement cellElement = driver.findElements(targetCellLocator.by()).get(0);
        String rawText = cellElement.getText().trim();

        if (isNumeric) {
            return rawText.replaceAll("[^0-9.]", "").trim();
        }

        return rawText;
    }

    public String getReviewAndSubmitTotalAmountDue() {
        Locator TOTAL_AMOUNT_DUE = new Locator("", By.xpath("//span[contains(normalize-space(.),'Total amount due')]/ancestor::div[1]/following-sibling::div[1]/span"));
        return driverTools.getText(TOTAL_AMOUNT_DUE).orElseThrow(() -> new RuntimeException("Total amount due not found"));
    }

    public String getTodayDateFormatted() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.US);
        return today.format(formatter);
    }

    public void clickSubmitToLeadButton() {
        driverTools.waitForElementToBeClickable(SUBMIT_TO_LEAD_BUTTON);
        driverTools.clickWithJavascriptExecutor(SUBMIT_TO_LEAD_BUTTON);
    }

    public void clickSubmitQuotationButton() {
        driverTools.waitForElementToBeClickable(SUBMIT_QUOTATION_BUTTON);
        driverTools.clickWithJavascriptExecutor(SUBMIT_QUOTATION_BUTTON);
    }

    public Boolean isModalLoaded() {
        return driverTools.waitForElementToAppear(MODAL_HEADING).isPresent();
    }

    public String getModalHeading() {
        return driverTools.getText(MODAL_HEADING).orElseThrow(() -> new IllegalStateException("Modal Heading not found"));
    }

    public String getModalDescription() {
        return driverTools.getText(MODAL_DESCRIPTION).orElseThrow(() -> new IllegalStateException("Modal Description not found"));
    }

    public void clickModalSubmitButton(){
        driverTools.waitForElementToBeClickable(MODAL_SUBMIT_BUTTON);
        driverTools.click(MODAL_SUBMIT_BUTTON);
    }

    public boolean isInvoiceAndBillingPageOpened() {
        driverTools.waitForElementToAppear(INVOICE_BILLING_HEADER);
        return driverTools.isElementDisplayed(INVOICE_BILLING_HEADER);
    }

    public void clickCreateNewInvoice() {
        driverTools.waitForElementToBeClickable(CREATE_NEW_INVOICE_BUTTON);
        driverTools.clickWithJavascriptExecutor(CREATE_NEW_INVOICE_BUTTON);
    }

    public void selectCorporate(String company) {
        driverTools.waitForElementToAppear(CORPORATE_DROPDOWN);
        driverTools.click(CORPORATE_DROPDOWN);
        driverTools.sendKeys(CORPORATE_DROPDOWN,company);
        Locator CORPORATE = new Locator("CORPORATE_OPTION", By.xpath("//div[contains(@class,'InputSearchAutoComplete_list_menu') and contains(.,'"+ company+ "')]"));
        driverTools.waitForElementToAppear(CORPORATE);
        driverTools.click(CORPORATE);
    }

    public void selectSystemOrderInvoiceType() {
        driverTools.waitForElementToAppear(SYSTEM_ORDER_DROPDOWN);
        driverTools.click(SYSTEM_ORDER_DROPDOWN);
        driverTools.dummyWait(2);
        driverTools.waitForElementToAppear(SYSTEM_ORDER);
        driverTools.click(SYSTEM_ORDER);
    }

    public void clickContinue() {
        driverTools.waitForElementToBeClickable(CONTINUE_BUTTON);
        driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON);
    }

    public String getInvoiceNumber() {
        driverTools.waitForElementToAppear(GENERATED_INVOICE_ID);
        return driverTools.getText(GENERATED_INVOICE_ID).orElseThrow();
    }

    public void addOrder() {
        driverTools.waitForElementToAppear(ADD_ORDER_BUTTON);
        driverTools.scrollIntoWebView(ADD_ORDER_BUTTON);
        driverTools.dummyWait(1);
        driverTools.click(ADD_ORDER_BUTTON);
        driverTools.waitForElementToAppear(CHECKBOX);
        driverTools.scrollIntoWebView(CHECKBOX);
        driverTools.waitForElementToBeClickable(CHECKBOX);
        driverTools.click(CHECKBOX);
        driverTools.dummyWait(1);
        driverTools.click(DONE_BUTTON);
    }

    public void clickAddOrder() {
        driverTools.waitForElementToAppear(ADD_ORDER_BUTTON);
        driverTools.clickWithJavascriptExecutor(ADD_ORDER_BUTTON);
    }

    public String addFirstOrderAndGetOrderId() {
        driverTools.waitForElementToAppear(ADD_ORDER_BUTTON);
        driverTools.scrollIntoWebView(ADD_ORDER_BUTTON);
        driverTools.dummyWait(1);
        driverTools.click(ADD_ORDER_BUTTON);

        driverTools.waitForElementToAppear(CHECKBOX);
        driverTools.scrollIntoWebView(CHECKBOX);

        driverTools.waitForElementToAppear(FIRST_ORDER_ID_ROW);
        String firstOrderId = driverTools.getText(FIRST_ORDER_ID_ROW).orElse("").trim();
        logger.log("First order ID selected for invoice: " + firstOrderId);

        driverTools.waitForElementToBeClickable(CHECKBOX);
        driverTools.click(CHECKBOX);
        driverTools.click(DONE_BUTTON);

        return firstOrderId;
    }

    public boolean isFailedIssuedOrderPresent() {
        driverTools.waitForElementToAppear(CHECKBOX);
        return driverTools.isElementPresent(FAILED_ISSUED_ORDER);
    }

    public boolean searchAndVerifyOrderInRow(String orderId) throws InterruptedException {
        driverTools.waitForElementToPresence(INPUT_ORDER_ID);
        driverTools.click(INPUT_ORDER_ID);
        driverTools.clearInput(INPUT_ORDER_ID);

       driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(INPUT_ORDER_ID,orderId);
        driverTools.waitForElementToBeClickable(SEARCH_BUTTON);
        driverTools.click(SEARCH_BUTTON);

        driverTools.dummyWait(1);

        driverTools.waitForElementToAppear(SEARCH_ORDER_IN_ROW,1);
        String orderIdInRow = driverTools.getText(SEARCH_ORDER_IN_ROW).orElse("");

        logger.log("Order ID in row: " + orderIdInRow);

        return orderIdInRow.contains(orderId);
    }

    public boolean isReviewInvoicePageOpened() {
        driverTools.waitForElementToAppear(REVIEW_PAGE_HEADER);
        return driverTools.isElementDisplayed(REVIEW_PAGE_HEADER);
    }

    public String getInvoiceStatus() {
        driverTools.waitForElementToAppear(SNACKBAR, 5).ifPresent(e -> driverTools.waitForElementToDisappear(e));
        String text = driverTools.getText(STATUS)
                .orElseThrow(() -> new RuntimeException("Invoice status not found"));
        if (text.trim().isEmpty()) {
            throw new RuntimeException("Invoice status is empty");
        }
        return text;
    }

    public String getTotalAmount(){
        driverTools.waitForElementToAppear(TOTAL_AMOUNT_DUE);
        String text = driverTools.getText(TOTAL_AMOUNT_DUE)
                .orElseThrow(() -> new RuntimeException("Total amount not found"));
        if (text.trim().isEmpty()) {
            throw new RuntimeException("Total amount is empty");
        }
        return text;
    }

    public void clickPublishInvoice(){
        driverTools.waitForElementToBeClickable(PUBLISH_INVOICE_BUTTON);
        driverTools.clickWithJavascriptExecutor(PUBLISH_INVOICE_BUTTON);
    }

    public void clickPublishAndSend(){
        driverTools.waitForElementToBeClickable(PUBLISH_AND_SEND_BUTTON);
        driverTools.clickWithJavascriptExecutor(PUBLISH_AND_SEND_BUTTON);
    }

    public void enterNotes(String notes) {
        driverTools.waitForElementToAppear(NOTES_TEXTAREA, 10);
        driverTools.clickWithJavascriptExecutor(NOTES_TEXTAREA);
        driverTools.dummyWait(1);
        driverTools.sendKeys(NOTES_TEXTAREA, notes);
    }

    public void enterTermsAndConditions(String termsAndConditions) {
        driverTools.waitForElementToAppear(TERMS_AND_CONDITIONS_TEXTAREA, 10);
        driverTools.clickWithJavascriptExecutor(TERMS_AND_CONDITIONS_TEXTAREA); 
        driverTools.dummyWait(1);
        driverTools.sendKeys(TERMS_AND_CONDITIONS_TEXTAREA, termsAndConditions);
    }

    public void searchOrderInModal(String orderId) {
        driverTools.waitForElementToPresence(INPUT_ORDER_ID);
        driverTools.click(INPUT_ORDER_ID);
        driverTools.clearInput(INPUT_ORDER_ID);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(INPUT_ORDER_ID, orderId);
        driverTools.waitForElementToBeClickable(SEARCH_BUTTON);
        driverTools.click(SEARCH_BUTTON);
    }

    public boolean isOrderDetailIdPresentInRow(String orderDetailId) {
        Locator orderDetailIdCell = new Locator("ORDER_DETAIL_ID_CELL",
                By.xpath("//div[contains(@class,'modal')]//tbody//tr//td[contains(normalize-space(.),'" + orderDetailId + "')]"));
        boolean present = driverTools.waitForElementToAppear(orderDetailIdCell, 3).isPresent();
        logger.log("OrderDetailId " + orderDetailId + " present in row: " + present);
        return present;
    }

    public void goBack() {
        driverTools.back();
    }

    public void searchInvoiceNumber(String invoiceNumber) {
        driverTools.waitForElementToPresence(INVOICE_NUMBER_SEARCH_INPUT);
        driverTools.clearInput(INVOICE_NUMBER_SEARCH_INPUT);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(INVOICE_NUMBER_SEARCH_INPUT, invoiceNumber);
        driverTools.click(INVOICE_NUMBER_SEARCH_BUTTON);
    }

    public void clickActionButton() {
        driverTools.waitForElementToBeClickable(Action_button);
        driverTools.clickWithJavascriptExecutor(Action_button);
    }

    public void clickView() {
        driverTools.waitForElementToBeClickable(VIEW_BUTTON);
        driverTools.click(VIEW_BUTTON);
    }

    public void clickMoreAction() {
        driverTools.waitForElementToBeClickable(MORE_ACTION_BUTTON);
        driverTools.click(MORE_ACTION_BUTTON);
    }

    public void clickCancelInvoiceOption() {
        driverTools.waitForElementToBeClickable(CANCEL_INVOICE_OPTION);
        driverTools.click(CANCEL_INVOICE_OPTION);
    }

    public void acceptCancelInvoiceCheckbox() {
        driverTools.waitForElementToAppear(CANCEL_INVOICE_CHECKBOX);
        driverTools.clickWithJavascriptExecutor(CANCEL_INVOICE_CHECKBOX);
    }

    public void confirmCancelInvoice() {
        driverTools.waitForElementToBeClickable(CANCEL_INVOICE_CONFIRM_BUTTON);
        driverTools.click(CANCEL_INVOICE_CONFIRM_BUTTON);
    }

    public void searchByRequestNumber(String requestNumber) {
        driverTools.waitForElementToBeClickable(REQUEST_NUMBER_INPUT);
        driverTools.clearInput(REQUEST_NUMBER_INPUT);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(REQUEST_NUMBER_INPUT, requestNumber);
        driverTools.dummyWait(3); // Wait for the table to filter
    }

    public boolean isRequestNumberVisibleInTable(String requestNumber) {
        Locator requestRow = new Locator("REQUEST_ROW_" + requestNumber,
                By.xpath("//table//tbody//tr//td[contains(normalize-space(.), '" + requestNumber + "')]"));
        return driverTools.waitForElementToAppear(requestRow, 5).isPresent();
    }

    public void clickRequestActionDots() {
        driverTools.waitForElementToBeClickable(REQUEST_ACTION_DOTS);
        try {
            driverTools.click(REQUEST_ACTION_DOTS);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(REQUEST_ACTION_DOTS);
        }
        driverTools.dummyWait(2);
    }

    public void clickStartProgressOption() {
        driverTools.waitForElementToPresence(START_PROGRESS_OPTION, 10);
        driverTools.clickWithJavascriptExecutor(START_PROGRESS_OPTION);
    }

    public void confirmStartProgress() {
        driverTools.waitForElementToBeClickable(CONFIRM_START_PROGRESS_BUTTON);
        driverTools.clickWithJavascriptExecutor(CONFIRM_START_PROGRESS_BUTTON);
    }

    public void verifySnackbarAndWaitUntilDismissed(String expectedMessage) {
        driverTools.waitForElementToAppear(SNACKBAR_CONTAINER).orElseThrow();
        driverTools.dummyWait(1); // Wait for snackbar text to render

        String actualMessage = driverTools.waitForElementToAppear(SNACKBAR_CONTAINER).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Snackbar Message. Expected: " + expectedMessage + " Actual: " + actualMessage, actualMessage, org.hamcrest.Matchers.containsString(expectedMessage));

        driverTools.waitForElementToDisappear(SNACKBAR_CONTAINER);
    }

    public void clickViewDetailsOption() {
        driverTools.waitForElementToPresence(VIEW_DETAILS_OPTION, 10);
        driverTools.clickWithJavascriptExecutor(VIEW_DETAILS_OPTION);
        driverTools.dummyWait(4); // View Details ka naya page load hone ke liye wait
    }

    public void clickCreateQuotation() {
        driverTools.waitForElementToBeClickable(CREATE_QUOTATION_BUTTON);
        driverTools.clickWithJavascriptExecutor(CREATE_QUOTATION_BUTTON);
        driverTools.dummyWait(2); // Wait for the 'Basic Information' page to load
    }

    /**
     * The Quotation Details calendar renders several DOM copies of the same day tile for a given
     * aria-label at once (confirmed via live DOM capture: e.g. 3 spans with
     * aria-label="30 September 2026 Wednesday" existed simultaneously - 2 with
     * {@code visibility:hidden; pointer-events:none} and 1 actually {@code visibility:visible}).
     * A plain positional xpath index like {@code (//span[@aria-label='...'])[1]} is therefore
     * unreliable: which position holds the visible copy varies between renders, so hardcoding an
     * index can and did bind to a hidden duplicate, which then can never become clickable and
     * times out. This scans all matches and returns the one that is actually displayed.
     */
    private WebElement waitForVisibleDateTile(String ariaLabel, int timeoutSeconds) {
        By by = By.xpath("//span[@aria-label='" + ariaLabel + "']");
        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L;
        do {
            List<WebElement> matches = driver.findElements(by);
            for (WebElement candidate : matches) {
                try {
                    if (candidate.isDisplayed()) {
                        return candidate;
                    }
                } catch (org.openqa.selenium.StaleElementReferenceException e) {
                    // Element re-rendered mid-scan; fall through and retry on the next poll.
                }
            }
            driverTools.dummyWait(1);
        } while (System.currentTimeMillis() < deadline);

        throw new java.util.NoSuchElementException("No visible calendar tile found for aria-label: " + ariaLabel);
    }

    public String selectQuotationStartDateForAmSupervisor(String dateStr, int dateOffset) {
        driverTools.waitForElementToBeClickable(QUOTATION_DETAILS_START_DATE);
        driverTools.click(QUOTATION_DETAILS_START_DATE);
        driverTools.dummyWait(1);

        DateTimeFormatter inputParser = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US);
        LocalDate baseDate = LocalDate.parse(dateStr.trim(), inputParser);
        LocalDate startDate = baseDate.plusDays(dateOffset);

        DateTimeFormatter ariaFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy EEEE", Locale.US);
        String targetAriaLabel = startDate.format(ariaFormatter);

        Locator NEXT_MONTH_BUTTON = new Locator("NEXT_MONTH_BUTTON", By.xpath("(//div[contains(@class, 'DatePicker')]//*[name()='svg' and contains(@class, 'right')])[2]"));
        int monthsToAdvance = (startDate.getYear() * 12 + startDate.getMonthValue())
                - (LocalDate.now().getYear() * 12 + LocalDate.now().getMonthValue());
        for (int i = 0; i < monthsToAdvance; i++) {
            driverTools.waitForElementToBeClickable(NEXT_MONTH_BUTTON);
            driverTools.click(NEXT_MONTH_BUTTON);
        }

        WebElement dateTile = waitForVisibleDateTile(targetAriaLabel, 10);
        driverTools.waitForElementToBeClickable(dateTile);
        driverTools.click(dateTile);

        String startDateFormatted = startDate.format(inputParser);
        logger.log("Start Date : " + startDateFormatted);
        return startDateFormatted;
    }

    public String selectQuotationEndDateForAmSupervisor(String startDateStr, int dateOffset) {
        driverTools.waitForElementToBeClickable(QUOTATION_DETAILS_END_DATE);
        driverTools.click(QUOTATION_DETAILS_END_DATE);
        driverTools.dummyWait(1);

        DateTimeFormatter inputParser = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US);
        LocalDate startDate = LocalDate.parse(startDateStr.trim(), inputParser);
        LocalDate endDate = startDate.plusDays(dateOffset);

        DateTimeFormatter ariaFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy EEEE", Locale.US);
        String targetAriaLabel = endDate.format(ariaFormatter);

        Locator NEXT_MONTH_BUTTON = new Locator("NEXT_MONTH_BUTTON", By.xpath("(//div[contains(@class, 'DatePicker')]//*[name()='svg' and contains(@class, 'right')])[3]"));
        int monthsToAdvance = (endDate.getYear() * 12 + endDate.getMonthValue())
                - (LocalDate.now().getYear() * 12 + LocalDate.now().getMonthValue());
        for (int i = 0; i < monthsToAdvance; i++) {
            driverTools.waitForElementToBeClickable(NEXT_MONTH_BUTTON);
            driverTools.click(NEXT_MONTH_BUTTON);
        }

        WebElement dateTile = waitForVisibleDateTile(targetAriaLabel, 10);
        driverTools.waitForElementToBeClickable(dateTile);
        driverTools.click(dateTile);

        String endDateFormatted = endDate.format(inputParser);
        logger.log("End Date : " + endDateFormatted);
        return endDateFormatted;
    }

    public String selectVendorPaymentPaymentDueDateForAmSupervisor(String paymentDueDateStr) {
        driverTools.waitForElementToBeClickable(PAYMENT_DUE_DATE);
        driverTools.click(PAYMENT_DUE_DATE);
        driverTools.dummyWait(1);

        DateTimeFormatter inputParser = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US);
        LocalDate paymentDueDate = LocalDate.parse(paymentDueDateStr.trim(), inputParser);

        DateTimeFormatter ariaFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy EEEE", Locale.US);
        String targetAriaLabel = paymentDueDate.format(ariaFormatter);

        Locator NEXT_MONTH_BUTTON = new Locator("NEXT_MONTH_BUTTON", By.xpath("//div[contains(@class, 'DatePicker')]//*[name()='svg' and contains(@class, 'right')]"));
        int monthsToAdvance = (paymentDueDate.getYear() * 12 + paymentDueDate.getMonthValue())
                - (LocalDate.now().getYear() * 12 + LocalDate.now().getMonthValue());
        for (int i = 0; i < monthsToAdvance; i++) {
            driverTools.waitForElementToBeClickable(NEXT_MONTH_BUTTON);
            driverTools.click(NEXT_MONTH_BUTTON);
        }

        WebElement dateTile = waitForVisibleDateTile(targetAriaLabel, 10);
        driverTools.waitForElementToBeClickable(dateTile);
        driverTools.click(dateTile);

        Locator BACK_BTN = new Locator("", By.xpath("(//input[@id='payment-due-date']//following-sibling::div)[last()]//button"));
        driverTools.click(BACK_BTN);

        String paymentDueDateFormatted = paymentDueDate.format(inputParser);
        logger.log("Payment Due Date : " + paymentDueDateFormatted);
        return paymentDueDateFormatted;
    }

    public void clickQuotationListActionDotsByRequestNumber(String requestNumber) {
        Locator actionDots = new Locator("QUOTATION_LIST_ACTION_DOTS_" + requestNumber,
                By.xpath("//table//tbody//tr[td[contains(normalize-space(.), '%s')]]//td[last()]//svg".formatted(requestNumber)));
        driverTools.waitForElementToBeClickable(actionDots);
        driverTools.click(actionDots);
    }

    public void clickSeeQuotationOption() {
        Locator seeQuotationBtn = new Locator("SEE_QUOTATION_OPTION",
                By.xpath("//li[contains(@class, 'cursor-pointer') and contains(., 'See quotation')]"));
        driverTools.waitForElementToBeClickable(seeQuotationBtn);
        driverTools.click(seeQuotationBtn);
    }

    // ==========================================
    // LOCATORS & METHODS FOR QUOTATION DETAILS PAGE
    // ==========================================

    public String getReceiverName() {
        Locator receiverNameLoc = new Locator("RECEIVER_NAME",
                By.xpath("//span[normalize-space(text())='Receiver']/following-sibling::span"));

        driverTools.waitForElementToAppear(receiverNameLoc);
        return driverTools.getText(receiverNameLoc).orElse("").trim();
    }

    public String getReceiverEmail() {
        Locator receiverEmailLoc = new Locator("RECEIVER_EMAIL",
                By.xpath("//span[normalize-space(text())='Email']/following-sibling::span"));

        driverTools.waitForElementToAppear(receiverEmailLoc);
        return driverTools.getText(receiverEmailLoc).orElse("").trim();
    }

    public String getReceiverRequestNumber() {
        Locator receiverReqNumLoc = new Locator("RECEIVER_REQUEST_NUMBER",
                By.xpath("//span[normalize-space(text())='Request Number']/following-sibling::span"));

        driverTools.waitForElementToAppear(receiverReqNumLoc);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return driverTools.getText(receiverReqNumLoc).orElse("").replace("-", "").trim();
    }
    public String getQuotationItemName() {
        Locator itemNameLoc = new Locator("QUOTATION_ITEM_NAME",
                By.xpath("//th[.//span[text()='Item Name']]/ancestor::table[1]//tbody/tr[1]/td[1]"));
        driverTools.waitForElementToAppear(itemNameLoc);
        return driverTools.getText(itemNameLoc).orElse("").trim();
    }

    public String getQuotationItemDescription() {
        Locator itemDescLoc = new Locator("QUOTATION_ITEM_DESC",
                By.xpath("//th[.//span[text()='Description']]/ancestor::table[1]//tbody/tr[1]/td[2]"));
        return driverTools.getText(itemDescLoc).orElse("").trim();
    }

    public String getQuotationTotalAmount() {
        Locator totalAmountLoc = new Locator("QUOTATION_TOTAL_AMOUNT",
                By.xpath("//tr[td[contains(normalize-space(.), 'Total Amount')]]/td[last()]"));
        return driverTools.getText(totalAmountLoc).orElse("").trim();
    }

    public boolean isQuotationCreatedLogVisible() {
        Locator createdLogLoc = new Locator("ACTIVITY_LOG_CREATED",
                By.xpath("//span[text()='Quotation created']"));
        return driverTools.isElementVisible(createdLogLoc);
    }

    public boolean isQuotationSubmittedLogVisible() {
        Locator submittedLogLoc = new Locator("ACTIVITY_LOG_SUBMITTED",
                By.xpath("//span[text()='Quotation submitted']"));
        return driverTools.isElementVisible(submittedLogLoc);
    }

    public boolean isBookingManagementPageOpened() {
        driverTools.dummyWait(2);
        return driverTools.waitForElementToAppear(BOOKING_MANAGEMENT_PAGE_HEADER).isPresent();
    }

    public boolean verifyBookingManagementColumns() {
        driverTools.waitForElementToPresence(BOOKING_MANAGEMENT_TABLE_HEADERS);

        List<String> expectedColumns = List.of(
                "Corporate Name",
                "Quote Number",
                "Order Status",
                "Order ID",
                "Order Detail ID",
                "Item Name",
                "Quantity",
                "Description",
                "Order Start Date",
                "Order End Date",
                "Payment Method"
        );

        boolean allColumnsPresent = true;

        for (String columnName : expectedColumns) {
            Locator colLocator = new Locator("COLUMN_" + columnName.replaceAll("\\s+", "_").toUpperCase(),
                    By.xpath("//table//thead//th[contains(normalize-space(.), '" + columnName + "')]"));

            if (!driverTools.waitForElementToAppear(colLocator, 5).isPresent()) {
                logger.log("Validation Failed: Column '" + columnName + "' is missing from the Booking Management table.");
                allColumnsPresent = false;
            }
        }

        return allColumnsPresent;
    }

    public boolean verifyOrderDetailsAndQuoteNumberFormat(String expectedQuotePrefix) {
        Locator rowLocator = new Locator("ORDER_DETAILS_ROW_WITH_PREFIX",
                By.xpath("//table//tbody//tr[count(td) >= 11 and contains(td[2], '" + expectedQuotePrefix + "')]"));
        return driverTools.waitForElementToAppear(rowLocator, 5).isPresent();
    }
}

