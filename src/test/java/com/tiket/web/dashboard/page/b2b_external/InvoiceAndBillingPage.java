package com.tiket.web.dashboard.page.b2b_external;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;

import java.io.File;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class InvoiceAndBillingPage<T extends WebDriver> extends BasePage<T> {

    private static final Locator INVOICE_BILLING_HEADER = new Locator("INVOICE_BILLING_HEADER", By.xpath("//span[contains(normalize-space(.), 'Invoice & Billing')]"));
    private static final Locator VIEW_INVOICE_BUTTON = new Locator("VIEW_INVOICE_BUTTON", By.xpath("(//button[ normalize-space(.)='View' or normalize-space(.)='Lihat' ])[1]"));
    private static final Locator PAY_INVOICE_BUTTON = new Locator("PAY_INVOICE_BUTTON", By.xpath("//button[normalize-space(.)='Pay invoice' or normalize-space(.)='Bayar invoice']"));
    private static final Locator INVOICE_ID = new Locator("INVOICE_ID", By.xpath("(//span[normalize-space(.)='Overdue']/ancestor::div[contains(@class,'items-center')]//span[1])[1]"));
    private static final Locator ORDER_ID_ON_PAYMENT = new Locator("ORDER_ID_ON_PAYMENT", By.xpath("//span[contains(normalize-space(.),'Order ID:') or contains(normalize-space(.),'ID Pesanan:')]"));
    private static final Locator TOTAL_AMOUNT_DUE = new Locator("TOTAL_AMOUNT_DUE", By.xpath("//p[normalize-space(text())='Total amount due']/following-sibling::p"));
    private static final Locator PAYMENT_PAGE_AMOUNT = new Locator("PAYMENT_PAGE_AMOUNT", By.xpath("//*[self::span or self::p][contains(@class,'PriceSummary_final_price') or contains(@class,'final_price')]"));
    private static final Locator INVOICE_NUMBER = new Locator("INVOICE_NUMBER", By.xpath("//button//span[text()='Mark as Paid' or text()='Pay invoice']/ancestor::div/preceding-sibling::div/span[1]"));
    private static final Locator INVOICE_STATUS = new Locator("INVOICE_STATUS", By.xpath("//button//span[text()='Mark as Paid' or text()='Pay invoice']/ancestor::div/preceding-sibling::div/div//span"));
    private static final Locator MARK_AS_PAID_BUTTON = new Locator("MARK_AS_PAID_BUTTON", By.xpath("//button//span[text()='Mark as Paid']"));
    private static final Locator COMPANY_NAME_HEADER = new Locator("COMPANY_NAME_HEADER", By.xpath("//span[text()='Invoice Date']/ancestor::div[3]/span[1]"));
    private static final Locator INVOICE_DATE_VALUE = new Locator("INVOICE_DATE_VALUE", By.xpath("//span[text()='Invoice Date']/following-sibling::span"));
    private static final Locator LOG_HISTORY_HEADER = new Locator("LOG_HISTORY_HEADER", By.xpath("//span[normalize-space()='Log History']"));

    private static final Locator MARK_AS_PAID_BUTTON_DIALOG = new Locator("MARK_AS_PAID_BUTTON_DIALOG", By.xpath("//button[normalize-space()='Mark as Paid']"));
    private static final Locator PAYMENT_METHOD_DROPDOWN = new Locator("PAYMENT_METHOD_DROPDOWN", By.xpath("//div[contains(@class,'cursor-pointer') and .//span[normalize-space()='Payment Method']]"));
    private static final Locator CONFIRMATION_CHECKBOX = new Locator("CONFIRMATION_CHECKBOX", By.xpath("//span[contains(@class,'custom_checkbox')]"));
    private static final Locator SUBMIT_MARK_AS_PAID = new Locator("SUBMIT_MARK_AS_PAID", By.xpath("//button[contains(normalize-space(.),'Mark as paid')]"));
    private static final Locator PAYMENT_DATE_FIELD = new Locator("PAYMENT_DATE_FIELD", By.xpath("//input[@id='payment-date']"));
    private static final Locator CALENDAR_SAVE_BUTTON = new Locator("CALENDAR_SAVE_BUTTON", By.xpath("//button[normalize-space()='Save' or normalize-space()='Simpan']"));
    private static final Locator SELECTED_PAYMENT_DATE_VALUE = new Locator("SELECTED_PAYMENT_DATE_VALUE", By.xpath("//input[@id='payment-date']"));
    private static final Locator OPTION = new Locator("PAYMENT_METHOD_OPTION_DYNAMIC", By.xpath("//li[normalize-space()='Credit Card']"));
    private static final Locator FIRSTAVAILABLEDATE = new Locator("FIRST_AVAILABLE_DATE", By.xpath("//button[contains(@class,'Day_day') and .//small[normalize-space()='Hari Ini' or normalize-space()='Today']]"));
    private static final Locator UPLOAD_FILE_INPUT = new Locator("UPLOAD_FILE_INPUT", By.xpath("//input[@type='file']"));
    private static final Locator INVOICE_STATUS_UPDATE = new Locator("INVOICE_STATUS_UPDATE", By.xpath("//span[contains(normalize-space(.),'Payment') or contains(normalize-space(.),'Paid') or contains(normalize-space(.),'Unpaid')]"));
    private static final Locator TOTAL_AMOUNT_DUE_VALUE_UPDATE = new Locator("TOTAL_AMOUNT_DUE_VALUE_UPDATE", By.xpath("//p[normalize-space()='Total amount due']/following-sibling::p"));
    private static final Locator LOADER = new Locator("PROGRESS_BAR",By.xpath("//div[contains(@class,'ProgressBar_progress_wrapper')]"));
    private static final Locator INVOICEID = new Locator("INVOICE_ID_DYNAMIC", By.xpath("//span[contains(@class,'Text_weight_bold') and contains(text(),'-')]"));
    private static final Locator VIEW_BUTTON = new Locator("VIEW_BUTTON", By.xpath("//button[normalize-space()='View']"));
    private static final Locator NOTES_VALUE = new Locator("NOTES_VALUE", By.xpath("//span[normalize-space()='Notes']/following-sibling::span"));
    private static final Locator TERMS_AND_CONDITIONS_VALUE = new Locator("TERMS_AND_CONDITIONS_VALUE", By.xpath("//span[normalize-space()='Terms & Conditions']/following-sibling::span"));


    public InvoiceAndBillingPage(Session<T> session) {
        super(session);
    }

    public boolean isInvoiceAndBillingPageOpened() {
        driverTools.waitForElementToAppear(INVOICE_BILLING_HEADER);
        return driverTools.isElementDisplayed(INVOICE_BILLING_HEADER);
    }

    public void clickViewInvoice() {
        driverTools.waitForElementToAppear(VIEW_INVOICE_BUTTON);
        driverTools.click(VIEW_INVOICE_BUTTON);
    }

    public String getInvoiceId() {
        driverTools.waitForElementToAppear(INVOICE_ID);
        return driverTools.getText(INVOICE_ID).orElseThrow();
    }

    public String getTotalAmountDue() {
        driverTools.waitForElementToAppear(TOTAL_AMOUNT_DUE, 30);
        return driverTools.getText(TOTAL_AMOUNT_DUE).orElseThrow();
    }

    public String getAmountOnPaymentPage() {
        driverTools.waitForElementToAppear(PAYMENT_PAGE_AMOUNT, 30);
        return driverTools.getText(PAYMENT_PAGE_AMOUNT).orElseThrow();
    }

    public boolean isSameAmountDisplayedOnPaymentPage(String invoiceAmount) {
        String normalised = invoiceAmount.replaceAll("[.,\\s]", "");
        String paymentAmount = getAmountOnPaymentPage().replaceAll("[.,\\s]", "");
        logger.log("Invoice amount (normalised): " + normalised + " | Payment page amount (normalised): " + paymentAmount);
        return paymentAmount.contains(normalised);
    }

    public void clickPayInvoice() {
        driverTools.waitForElementToAppear(PAY_INVOICE_BUTTON);
        int tabCountBefore = driverTools.getWindowHandles().size();
        driverTools.click(PAY_INVOICE_BUTTON);
        driverTools.waitForNewTabAndSwitchToLatest(tabCountBefore);
    }

    public boolean isOrderIdPresentOnPaymentPage() {
        driverTools.waitForElementToAppear(ORDER_ID_ON_PAYMENT, 30);
        return driverTools.isElementPresent(ORDER_ID_ON_PAYMENT);
    }

    public boolean isInvoiceIdPresentOnPaymentPage(String invoiceId) {
        Locator invoiceIdOnPayment = new Locator("INVOICE_ID_ON_PAYMENT",
                By.xpath("//span[contains(@class,'OrderSummaryItem_summary_title') and normalize-space(.)='" + invoiceId + "']"));
        driverTools.waitForElementToAppear(invoiceIdOnPayment, 30);
        return driverTools.isElementPresent(invoiceIdOnPayment);
    }

    public boolean isInvoiceDetailPageOpened() {
        try {
            Locator itemDetailsTitle = new Locator("ITEM_DETAILS_TITLE", By.xpath("//span[text()='Item Details']"));
            driverTools.waitForElementToPresence(itemDetailsTitle, 15);
            return driverTools.getDriver().findElement(itemDetailsTitle.by()).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private static final Locator INVOICE_NUMBER_INPUT = new Locator("INVOICE_NUMBER_INPUT", By.xpath("//input[@type='text' and @placeholder='Invoice Number']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[normalize-space(.)='Search']"));

    public void searchInvoiceNumber(String invoiceNumber) {
        driverTools.waitForElementToPresence(INVOICE_NUMBER_INPUT);
        driverTools.clearInput(INVOICE_NUMBER_INPUT);
        driverTools.sendKeys(INVOICE_NUMBER_INPUT, invoiceNumber);
        driverTools.click(SEARCH_BUTTON);
    }
    public String getInvoiceNumber() {
        driverTools.waitForElementToPresence(INVOICE_NUMBER, 15);
        return driverTools.getText(INVOICE_NUMBER).orElseThrow(() -> new IllegalStateException("Invoice number not found"));
    }

    public String getInvoiceStatus() {
        driverTools.waitForElementToPresence(INVOICE_STATUS, 15);
        return driverTools.getText(INVOICE_STATUS).orElseThrow(() -> new IllegalStateException("Invoice status not found"));
    }

    public boolean isMarkAsPaidButtonDisplayed() {
        driverTools.waitForElementToAppear(MARK_AS_PAID_BUTTON);
        return driverTools.isElementDisplayed(MARK_AS_PAID_BUTTON);
    }

    public boolean isPayButtonDisplayed() {
        driverTools.waitForElementToAppear(PAY_INVOICE_BUTTON);
        return driverTools.isElementDisplayed(PAY_INVOICE_BUTTON);
    }

    public String getCompanyName() {
        return driverTools.getText(COMPANY_NAME_HEADER).orElseThrow(() -> new IllegalStateException("Company name not found"));
    }

    public String getInvoiceDate() {
        return driverTools.getText(INVOICE_DATE_VALUE).orElseThrow(() -> new IllegalStateException("Invoice date value not found"));
    }

    public int getItemDetailsColumnIndex(String columnTitle) {
        Locator targetHeader = new Locator("TARGET_HEADER", By.xpath(String.format("//th[normalize-space()='%s']", columnTitle)));
        driverTools.waitForElementToPresence(targetHeader, 15);
        
        List<WebElement> headers = driverTools.getDriver().findElements(By.xpath("//thead//tr//th"));
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().trim().equalsIgnoreCase(columnTitle)) {
                return i + 1;
            }
        }
        throw new NoSuchElementException("Column not found: " + columnTitle);
    }

    public boolean isItemDisplayed(String itemName) {
        Locator itemRow = new Locator("ITEM_ROW", By.xpath("//tbody/tr[td[normalize-space()='" + itemName + "']]"));
        driverTools.waitForElementToPresence(itemRow, 15);
        return driverTools.isElementPresent(itemRow);
    }

    public String getItemDetailCellValue(String itemName, String columnTitle) {
        int itemColIndex = getItemDetailsColumnIndex("Item Name");
        int targetColIndex = getItemDetailsColumnIndex(columnTitle);
        Locator cellLocator = new Locator("DYNAMIC_CELL", By.xpath("//tbody/tr[td[" + itemColIndex + "][normalize-space()='" + itemName + "']]/td[" + targetColIndex + "]"));
        return driverTools.getText(cellLocator).orElseThrow(() -> new IllegalStateException("Cell value not found for item: " + itemName + ", column: " + columnTitle));
    }

    public String getTotalAmountDueValue() {
        return driverTools.getText(TOTAL_AMOUNT_DUE).orElseThrow(() -> new IllegalStateException("Total amount due value not found")).trim();
    }

    public boolean isDocumentPresent(String docName) {
        try {
            Locator docSpan = new Locator("DOC_SPAN", By.xpath(String.format("//span[normalize-space()='%s']", docName)));
            return driverTools.getDriver().findElement(docSpan.by()).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isLogHistoryDisplayed() {
        try {
            return driverTools.getDriver().findElement(LOG_HISTORY_HEADER.by()).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickMarkAsPaid() {
        driverTools.waitForElementToAppear(MARK_AS_PAID_BUTTON_DIALOG, 10);
        driverTools.click(MARK_AS_PAID_BUTTON_DIALOG);
    }

    public void selectPaymentMethod() {
        driverTools.waitForElementToAppear(PAYMENT_METHOD_DROPDOWN, 10);
        driverTools.click(PAYMENT_METHOD_DROPDOWN);
        driverTools.waitForElementToAppear(OPTION, 10);
        driverTools.click(OPTION);
    }

    public void selectPaymentDate() {
        driverTools.waitForElementToAppear(PAYMENT_DATE_FIELD, 5);
        driverTools.click(PAYMENT_DATE_FIELD);
        driverTools.waitForElementToAppear(FIRSTAVAILABLEDATE, 5);
        driverTools.click(FIRSTAVAILABLEDATE);
        driverTools.scrollToElement(CALENDAR_SAVE_BUTTON);
        driverTools.waitForElementToAppear(CALENDAR_SAVE_BUTTON, 5);
        driverTools.click(CALENDAR_SAVE_BUTTON);
        driverTools.waitForElementToAppear(SELECTED_PAYMENT_DATE_VALUE, 5);
    }

    public void uploadSingleFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("File not found at path: " + filePath);
        }
        driverTools.waitForElementToAppear(UPLOAD_FILE_INPUT, 2);
        driverTools.sendKeys(UPLOAD_FILE_INPUT, file.getAbsolutePath());
    }

    public void acceptConfirmationCheckbox() {
        driverTools.waitForElementToAppear(CONFIRMATION_CHECKBOX, 5);
        driverTools.click(CONFIRMATION_CHECKBOX);
    }

    public String getInvoiceStatusUpdate() {
        driverTools.dummyWait(5);
        driverTools.waitForElementToAppear(INVOICE_STATUS_UPDATE, 10);
        return driverTools.getText(INVOICE_STATUS_UPDATE).orElseThrow(() -> new IllegalStateException("Invoice status not found"));
    }

    public String getInvoiceIdUpdate() {
        driverTools.waitForElementToAppear(INVOICEID, 10);
        return driverTools.getText(INVOICEID).orElseThrow(() -> new IllegalStateException("Invoice ID not found"));
    }

    public String getTotalAmountDueUpdate() {
        driverTools.waitForElementToAppear(TOTAL_AMOUNT_DUE_VALUE_UPDATE, 10);
        return driverTools.getText(TOTAL_AMOUNT_DUE_VALUE_UPDATE).orElseThrow(() -> new IllegalStateException("Total amount due not found"));
    }
    public void clickOnInvoice(){
        driverTools.waitForElementToAppear(VIEW_BUTTON);
        driverTools.click(VIEW_BUTTON);
    }

    public void clickMarkAsPaidSubmit() {
        driverTools.waitForElementToDisappear(LOADER,10);
        driverTools.waitForElementToAppear(SUBMIT_MARK_AS_PAID, 5);
        driverTools.click(SUBMIT_MARK_AS_PAID);
    }

    public String getNotes() {
        driverTools.waitForElementToAppear(NOTES_VALUE, 10);
        return driverTools.getText(NOTES_VALUE)
                .orElseThrow(() -> new IllegalStateException("Notes not found"));
    }

    public String getTnC() {
        driverTools.waitForElementToAppear(TERMS_AND_CONDITIONS_VALUE, 10);
        return driverTools.getText(TERMS_AND_CONDITIONS_VALUE)
                .orElseThrow(() -> new IllegalStateException("Terms & Conditions not found"));
    }
}
