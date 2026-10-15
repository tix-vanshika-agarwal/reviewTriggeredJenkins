package com.tiket.web.dashboard.page.member_dashboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSyntaxException;
import com.tiket.api.intools.*;
import com.tiket.core.CoreFunctions;
import com.tiket.dbclient.TiketMysqlDBManager;
import com.tiket.dto.MonthlyReportIntoolsDTO;
import com.tiket.dto.SummaryReportListIntoolsDTO;
import com.tiket.dto.SummaryReportDetailIntoolsDTO;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.intools.B2BDailyDetail;
import com.tiket.pojo.intools.B2CDailyDetail;
import com.tiket.pojo.intools.B2CDailyDetailOrder;
import com.tiket.pojo.intools.B2BResellerDetail;
import com.tiket.pojo.intools.MonthlyReportDetail;
import com.tiket.pojo.intools.SummaryReportList;
import com.tiket.pojo.intools.SummaryReportDetail;
import com.tiket.pojo.intools.TopUpDetail;
import com.tiket.pojo.intools.ClearLockTransaction;
import com.tiket.pojo.intools.KafkaPublishFlight1stOrderDetail;
import com.tiket.pojo.intools.KafkaPublishFlight2ndOrderDetail;
import com.tiket.pojo.intools.KafkaPublishFlight3rdOrderDetail;
import com.tiket.pojo.intools.KafkaPublishTTD1stOrderDetail;
import com.tiket.pojo.intools.KafkaPublishTTD2ndOrderDetail;
import com.tiket.pojo.intools.KafkaPublishTTD3rdOrderDetail;
import com.tiket.pojo.intools.KafkaPublishFerryOrderDetail;
import com.tiket.pojo.intools.KafkaPublishBusOrderDetail;
import com.tiket.pojo.intools.DeleteOrderId;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import io.appium.java_client.AppiumBy;
import nl.jiankai.mapper.ResultSetMapperFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.time.Instant;
import java.io.*;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.is;

public class FinanceDashboardPage<T extends WebDriver> extends BasePage<T> {
    public FinanceDashboardPage(Session<T> session) {
        super(session);
    }

    private static final Locator FINANCE_DASHBOARD_LOGO = new Locator("FINANCE_DASHBOARD_LOGO", new By.ByXPath("//span[contains(text(),'Finance Dashboard')]"));
    private static final Locator FINDER_MENU = new Locator("FINDER_MENU", By.xpath("//span[normalize-space()='Finder']"));
    private static final Locator SEARCH_TRANSACTIONS_MENU = new Locator("SEARCH_TRANSACTIONS_MENU", By.xpath("//span[normalize-space()='Search Transactions']"));
    private static final Locator SEARCH_BY_INPUT = new Locator("SEARCH_BY_INPUT", By.xpath("//input[@data-testid='select-input' and @id='search-by']"));
    private static final Locator SEARCH_DATA_TEXTAREA = new Locator("SEARCH_DATA_TEXTAREA", By.xpath("//textarea[@data-testid='search-data']"));
    private static final Locator ACCORDION_MENU = new Locator("ACCORDION_MENU", new By.ByXPath("//span[contains(text(),'Commission Dashboard')]"));
    private static final Locator SALES_MENU = new Locator("SALES_MENU", By.xpath("//div[@role='button' and .//span[normalize-space()='Sales']]"));
    private static final Locator SALES_ORDER_MENU = new Locator("SALES_ORDER_MENU", By.xpath("//div[normalize-space()='Sales Order']"));
    private static final Locator SALES_ORDER_FILTER = new Locator("SALES_ORDER_FILTER", By.xpath("//th[@data-field='orderId']//button"));
    private static final Locator SALES_ORDER_ID_SEARCH_INPUT = new Locator("SALES_ORDER_ID_SEARCH_INPUT", By.xpath("//input[@aria-label='Search order ID']"));
    private static final Locator SALES_ORDER_ID_CHECKBOX = new Locator("SALES_ORDER_ID_CHECKBOX", By.xpath("//input[@type='checkbox' and contains(@class,'TableColumnFilterOrderId_checkbox')]"));
    private static final Locator SALES_ORDER_APPLY_BUTTON = new Locator("SALES_ORDER_APPLY_BUTTON", By.xpath("//button[normalize-space()='Apply']"));
    private static final Locator SALES_ORDER_RESULT_ORDER_ID = new Locator("SALES_ORDER_RESULT_ORDER_ID", By.xpath("//tr/td[2]"));
    private static final Locator SALES_ORDER_RESULT_SOURCE = new Locator("SALES_ORDER_RESULT_SOURCE", By.xpath("//tr/td[6]"));
    private static final Locator SALES_ORDER_RESULT_PRICE = new Locator("SALES_ORDER_RESULT_PRICE", By.xpath("//tr/td[11]"));
    private static final Locator SALES_ORDER_RESULT_ROW = new Locator("SALES_ORDER_RESULT_ROW", By.xpath("//tbody[@data-testid='table:list-body']/tr[1]"));
    private static final Locator SALES_ORDER_DETAIL_REFUND_LINK = new Locator("SALES_ORDER_DETAIL_REFUND_LINK", By.xpath("//h4[normalize-space()='Sales Refund']/ancestor::div[contains(@class,'related_doc_card')]//button[.//span[normalize-space()='1000009088']]"));
    private static final Locator SALES_REFUND_DETAIL_REFUND_ID = new Locator("SALES_REFUND_DETAIL_REFUND_ID", By.xpath("//span[contains(@class,'SalesRefundDetailCard_detail_header_value__')]"));
    private static final Locator SALES_REFUND_DETAIL_ORDER_ID = new Locator("SALES_REFUND_DETAIL_ORDER_ID", By.xpath("//div[contains(@class,'SalesRefundDetailCard_detail_row__')][.//span[normalize-space()='Order ID']]//span[contains(@class,'SalesRefundDetailCard_detail_value__')]"));
    private static final Locator FLIGHT_DOMESTIC_MENU = new Locator("FLIGHT_DOMESTIC_MENU", new By.ByXPath("//div[contains(text(),'Flight (Domestic)')]"));
    private static final Locator FLIGHT_INTERNATIONAL_MENU = new Locator("FLIGHT_INTERNATIONAL_MENU", new By.ByXPath("//div[contains(text(),'Flight (International)')]"));
    private static final Locator VENDOR_NAME_DROPDOWN = new Locator("VENDOR_NAME_DROPDOWN", By.xpath("//input[@id='vendor-name']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[normalize-space()='Search']"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", By.xpath("//button[normalize-space()='Reset Filter']"));
    private static final Locator SEARCH_BUTTON_RECON_REPORTS = new Locator("SEARCH_BUTTON_RECON_REPORTS", By.xpath("//*[@data-testid='filter-search']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[normalize-space()='Save' or normalize-space()='Simpan']"));
    private static final Locator MORE_BUTTON = new Locator("MORE_BUTTON", new By.ByXPath("//div[@id='dropdown-form-0']"));
    private static final Locator FIRST_ROW_VENDOR_NAME = new Locator("FIRST_ROW_VENDOR_NAME", By.xpath("//tbody[@data-testid='table:list-body']/tr[1]/td[@data-field='vendor-name']/div"));
    private static final Locator SEARCH_BY_DROPDOWN = new Locator("SEARCH_BY_DROPDOWN", By.xpath("//input[@id='search-by']"));
    private static final Locator TX_ROW1_ORDER_ID = new Locator("TX_ROW1_ORDER_ID", By.xpath("//tbody[@data-testid='table:list-body']/tr[1]/td[4]"));
    private static final Locator TX_ROW2_ORDER_ID = new Locator("TX_ROW2_ORDER_ID", By.xpath("//tbody[@data-testid='table:list-body']/tr[2]/td[4]"));
    private static final Locator TX_ROW1_PRODUCT_NAME = new Locator("TX_ROW1_PRODUCT_NAME", By.xpath("//tbody[@data-testid='table:list-body']/tr[1]/td[23]"));
    private static final Locator TX_ROW2_PRODUCT_NAME = new Locator("TX_ROW2_PRODUCT_NAME", By.xpath("//tbody[@data-testid='table:list-body']/tr[2]/td[23]"));
    private static final Locator TX_ROW1_COUNT_OF_ADULT = new Locator("TX_ROW1_COUNT_OF_ADULT", By.xpath("//tbody[@data-testid='table:list-body']/tr[1]/td[30]"));
    private static final Locator TX_ROW1_BASIC_PRICE_BAGGAGE = new Locator("TX_ROW1_BASIC_PRICE_BAGGAGE", By.xpath("//tbody[@data-testid='table:list-body']/tr[1]/td[46]"));
    private static final Locator TX_ROW1_BASIC_PRICE_MEALS = new Locator("TX_ROW1_BASIC_PRICE_MEALS", By.xpath("//tbody[@data-testid='table:list-body']/tr[1]/td[47]"));
    private static final Locator TX_ROW1_BASIC_PRICE_SEAT = new Locator("TX_ROW1_BASIC_PRICE_SEAT", By.xpath("//tbody[@data-testid='table:list-body']/tr[1]/td[48]"));
    private static final Locator TX_ROW1_CUSTOMER_CURRENCY = new Locator("TX_ROW1_CUSTOMER_CURRENCY", By.xpath("//tbody[@data-testid='table:list-body']/tr[1]/td[89]"));
    private static final Locator TX_ROW1_VENDOR_CURRENCY = new Locator("TX_ROW1_VENDOR_CURRENCY", By.xpath("//tbody[@data-testid='table:list-body']/tr[1]/td[100]"));
    private static final Locator DOWNLOAD_BUTTON = new Locator("DOWNLOAD_BUTTON", By.xpath("//button[@data-testid='button-download']"));
    private static final Locator DOWNLOAD_SNACKBAR_TEXT = new Locator("DOWNLOAD_SNACKBAR_TEXT", By.xpath("//div[contains(@class,'Snackbar_snackbar')]//div[contains(@class,'Snackbar_text_content')]"));
    private static final Locator TRANSACTION_SEARCH_INPUT = new Locator("TRANSACTION_SEARCH_INPUT", By.xpath("//input[@data-testid='search-input']"));
    private static final Locator DATE_RANGE_START_CALENDAR = new Locator("DATE_RANGE_START_CALENDAR", By.xpath("//input[@id='start-date']"));
    private static final Locator DATE_RANGE_END_CALENDAR = new Locator("DATE_RANGE_END_CALENDAR", By.xpath("(//button[contains(@class,'styles_clickable_icon')][.//*[local-name()='path'][starts-with(@d,'M16 4.996')]])[2]"));
    private static final Locator TOP_UP_BUTTON = new Locator("TOP_UP_BUTTON", new By.ByXPath("//span[contains(text(),'Top Up')]"));
    private static final Locator TRANSACTIONS_BUTTON = new Locator("TRANSACTIONS_BUTTON", new By.ByXPath("//span[contains(text(),'Transactions')]"));
    private static final Locator ADD_TRANSACTION_BUTTON = new Locator("ADD_TRANSACTION_BUTTON", new By.ByXPath("//button[contains(text(),'Add transaction')]"));
    private static final Locator ADD_TRANSACTION_BUTTON_MODAL = new Locator("ADD_TRANSACTION_BUTTON_MODAL", new By.ByXPath("//footer//button[contains(text(),'Add transaction')]"));
    private static final Locator TOP_UP_HEADING = new Locator("TOP_UP_HEADING", new By.ByXPath("//h2[contains(text(),'Top up')]"));
    private static final Locator TOP_UP_AMOUNT_INPUT = new Locator("TOP_UP_AMOUNT_INPUT", By.name("amount"));
    private static final Locator PAYMENT_TIMESTAMP_INPUT = new Locator("PAYMENT_TIMESTAMP_INPUT", By.xpath("//input[@id='payment-timestamp']"));
    private static final Locator DESCRIPTION_TEXTAREA = new Locator("DESCRIPTION_TEXTAREA", new By.ByXPath("//textarea[@name='description']"));
    private static final Locator UPLOAD_FILE_INPUT = new Locator("UPLOAD_FILE_INPUT", new By.ByXPath("//input[@data-testid='input-file']"));
    private static final Locator SUBMIT_TOP_UP_BUTTON = new Locator("SUBMIT_TOP_UP_BUTTON", new By.ByXPath("//button[contains(text(),'Submit')]"));
    private static final Locator CANCEL_BUTTON = new Locator("CANCEL_BUTTON", new By.ByXPath("//button[contains(text(),'Cancel')]"));
    private static final Locator SNACKBAR_SUCCESS_MESSAGE = new Locator("SNACKBAR_SUCCESS_MESSAGE", new By.ByXPath("//div[contains(text(),'Successfully added a top-up. Refresh for balance deposit updates.')]"));
    private static final Locator DEPOSIT_BALANCE = new Locator("DEPOSIT_BALANCE", new By.ByXPath("//td[@data-field='deposit-balance']"));
    private static final Locator SOURCE_FILTER = new Locator("SOURCE_FILTER", new By.ByXPath("//span[contains(text(),'Source')]"));
    private static final Locator MANUAL_SOURCE = new Locator("MANUAL_SOURCE", new By.ByXPath("//span[contains(text(),'Manual')]"));
    private static final Locator DAILY_TAB = new Locator("DAILY_TAB", new By.ByXPath("//span[contains(text(),'Daily')]"));
    private static final Locator MONTHLY_TAB = new Locator("MONTHLY_TAB", new By.ByXPath("//span[contains(text(),'Monthly')]"));
    private static final Locator SUMMARY_REPORT_LIST_TAB = new Locator("SUMMARY_REPORT_LIST_TAB", new By.ByXPath("//button[contains(text(),'Summary Report')]"));
    private static final Locator SUMMARY_REPORT_DETAIL = new Locator("SUMMARY_REPORT_DETAIL", new By.ByXPath("(//span[contains(text(),'See Details')])[1]"));
    private static final Locator SELECT_MONTH_DROPDOWN = new Locator("SELECT_MONTH_DROPDOWN", new By.ById("select-month"));
    private static final Locator SELECT_YEAR_DROPDOWN = new Locator("SELECT_YEAR_DROPDOWN", new By.ById("select-year"));
    private static final Locator DOWNLOAD_CSV_SUMMARY_REPORT_DETAIL = new Locator("DOWNLOAD_CSV_SUMMARY_REPORT_DETAIL", new By.ByXPath("//button[contains(text(),'Download CSV')]"));
    private static final Locator SNACKBAR_DOWNLOAD_CSV_SUMMARY_REPORT_DETAIL_SUCCESS = new Locator("SNACKBAR_DOWNLOAD_CSV_SUMMARY_REPORT_DETAIL_SUCCESS", new By.ByXPath("//div[contains(text(),'Successfully download summary report')]"));
    private static final Locator VENDOR_ID = new Locator("VENDOR_ID", new By.ByXPath("//td[@data-field='vendor-id']"));
    private static final Locator ENTITY = new Locator("ENTITY", new By.ByXPath("//td[@data-field='entity-id']"));
    private static final Locator SETTLEMENT_TYPE = new Locator("SETTLEMENT_TYPE", new By.ByXPath("//td[@data-field='settlement-type']"));
    private static final Locator VENDOR_CURRENCY = new Locator("VENDOR_CURRENCY", new By.ByXPath("//td[@data-field='vendor-currency']"));
    private static final Locator BULK_UPLOAD_OPTION = new Locator("BULK_UPLOAD_OPTION", new By.ByXPath("//button[contains(text(),'Bulk Upload')]"));
    private static final Locator SNACKBAR_SUCCESS_MESSAGE_BULK_UPLOAD = new Locator("SNACKBAR_SUCCESS_MESSAGE_BULK_UPLOAD", new By.ByXPath("//div[contains(text(),'Upload Successful!')]"));
    private static final Locator ROWS_PER_PAGE = new Locator("ROWS_PER_PAGE", new By.ByXPath("//input[@data-testid='select-input' and contains(@class, 'styles_input____Fnk styles_has_clickable_icon__v8mvj')']"));
    private static final Locator PAYMENT_MENU = new Locator("PAYMENT_MENU", new By.ByXPath("//span[contains(text(),'Payment')]"));
    private static final Locator B2B_AFFILIATE_DROPDOWN_MENU = new Locator("B2B_AFFILIATE_DROPDOWN_MENU", new By.ByXPath("//span[contains(text(),'B2B Affiliate')]"));
    private static final Locator REQUEST_DROPDOWN_MENU = new Locator("REQUEST_DROPDOWN_MENU", new By.ByXPath("//span[contains(text(),'Request')]"));
    private static final Locator B2B_RESELLER_MENU = new Locator("B2B_RESELLER_MENU", new By.ByXPath("(//div[contains(text(),'Reseller')])[1]"));
    private static final Locator REQUEST_RESELLER_MENU = new Locator("REQUEST_RESELLER_MENU", new By.ByXPath("(//div[contains(text(),'Reseller')])[2]"));
    private static final Locator RESELLER_NAME_DROPDOWN = new Locator("RESELLER_NAME_DROPDOWN", By.name("Reseller Name"));
    private static final Locator REQUEST_HISTORY_TAB = new Locator("REQUEST_HISTORY_TAB", new By.ByXPath("//button[contains(text(),'Request History')]"));
    private static final Locator RESELLER_ID = new Locator("RESELLER_ID", new By.ByXPath("//td[@data-field='reseller-id']"));
    private static final Locator TYPE_OF_TRX_DROPDOWN = new Locator("TYPE_OF_TRX_DROPDOWN", new By.ById("type-of-transaction"));
    private static final Locator TRX_AMOUNT_INPUT = new Locator("TRX_AMOUNT_INPUT", By.xpath("//input[@data-testid='amount-error']"));
    private static final Locator SUB_RESELLER_NAME_INPUT = new Locator("SUB_RESELLER_NAME_INPUT", new By.ById("subreseller-name-optional"));
    private static final Locator ORDER_ID_INPUT = new Locator("ORDER_ID_INPUT", new By.ByXPath("//label[normalize-space()='Order ID']"));
    private static final Locator ORDER_ID_INPUT_DATA = new Locator("ORDER_ID_INPUT_DATA", new By.ByXPath("//input[@id='order-id'] | //input[@id='payment-id']"));
    private static final Locator SEARCH_BY_PAYMENT_ID = new Locator("SEARCH_BY_PAYMENT_ID", By.xpath("//input[@id='search-by' and @value='Payment ID']"));
    private static final Locator REMARKS_INPUT = new Locator("REMARKS_INPUT", new By.ById("remarks"));
    private static final Locator APPROVE_BUTTON_MODAL = new Locator("APPROVE_BUTTON_MODAL", new By.ByXPath("//footer//button[contains(text(),'Approve')]"));
    private static final Locator APPROVE_B2B_PENDING_REQUEST = new Locator("APPROVE_B2B_PENDING_REQUEST", new By.ByXPath("//button[contains(text(),'Approve')]"));
    private static final Locator BANK_NAME_INPUT = new Locator("BANK_NAME_INPUT", new By.ById("bank-name"));
    private static final Locator TRANSACTION_ID_INPUT = new Locator("TRANSACTION_ID_INPUT", new By.ById("transaction-id"));
    private static final Locator COMM_DASHBOARD_MENU = new Locator("COMM_DASHBOARD_MENU", By.xpath("//span[text()='Commission Dashboard']"));
    private static final String INTOOLS_PAGE_URL = "/admin/intools";
    private static final Locator BANK_STATEMENT_UPLOAD_MENU = new Locator("BANK_STATEMENT_UPLOAD_MENU", new By.ByXPath("//div[contains(text(),'Bank Statement Upload')]"));
    private static final Locator RECONCILIATION_MENU = new Locator("RECONCILIATION_MENU", By.xpath("//span[normalize-space()='Reconciliation']"));
    private static final Locator RECON_UPLOADS_SUBMENU = new Locator("RECON_UPLOADS_SUBMENU", By.xpath("//div[normalize-space()='Uploads']"));
    private static final Locator RECON_UPLOADS_TABLE_HEADINGS = new Locator("RECON_UPLOADS_TABLE_HEADINGS", By.xpath("//table[@data-testid='table:list']//thead//th"));
    private static final Locator RECON_UPLOADS_FIRST_ROW_VIEW_BUTTON = new Locator("RECON_UPLOADS_FIRST_ROW_VIEW_BUTTON", By.xpath("(//table[@data-testid='table:list']//tbody//tr)[1]//button[normalize-space()='View']"));
    private static final Locator UPLOAD_DETAIL_INFO_GRID = new Locator("UPLOAD_DETAIL_INFO_GRID", By.xpath("//div[contains(@class,'reconciliation_upload_detail_info_grid')]"));
    private static final Locator RECON_UPLOADS_UPLOADED_BY_FILTER = new Locator("RECON_UPLOADS_UPLOADED_BY_FILTER", By.xpath("//button[@aria-label='Filter by Uploaded By']"));
    private static final Locator RECON_UPLOADS_UPLOADED_BY_FILTER_ACTIVE_DOT = new Locator("RECON_UPLOADS_UPLOADED_BY_FILTER_ACTIVE_DOT", By.xpath("//th[.//button[@aria-label='Filter by Uploaded By']]//span[contains(@class,'filter_active_dot')]"));
    private static final Locator RECON_UPLOADS_FILTER_SEARCH_INPUT = new Locator("RECON_UPLOADS_FILTER_SEARCH_INPUT", By.xpath("//input[@aria-label='Search uploaded by']"));
    private static final Locator RECON_UPLOADS_FILTER_APPLY_BUTTON = new Locator("RECON_UPLOADS_FILTER_APPLY_BUTTON", By.xpath("//button[contains(@class,'TableColumnFilterOrderId_apply_button')]"));
    private static final Locator DOWNLOAD_TEMPLATE_BUTTON = new Locator("DOWNLOAD_TEMPLATE_BUTTON", new By.ByXPath("//button[contains(text(),'Download Template')]"));
    private static final Locator PAYMENT_METHOD_DROPDOWN = new Locator("PAYMENT_METHOD_DROPDOWN", new By.ById("payment-method"));
    private static final Locator CSV_FILE_INPUT = new Locator("CSV_FILE_INPUT", new By.ById("csv-reader-recon-upload"));
    private static final Locator UPLOAD_BUTTON = new Locator("UPLOAD_BUTTON", new By.ByXPath("//button[contains(text(),'Upload')]"));
    private static final Locator CONFIRM_BUTTON = new Locator("CONFIRM_BUTTON", new By.ByXPath("//button[normalize-space()='Confirm']"));
    private static final Locator CONFIRM = new Locator("CONFIRM_BUTTON", new By.ByXPath("//button[normalize-space()='CONFIRM']"));
    private static final Locator CLOSE_BUTTON_MODAL = new Locator("CLOSE_BUTTON_MODAL", By.xpath("//button[contains(@class, 'Modal_modal_close')]"));
    private static final Locator CONFIRM_BUTTON_MODAL = new Locator("CONFIRM_BUTTON_MODAL", new By.ByXPath("//footer//button[contains(text(),'Confirm')]"));
    private static final Locator OK_UPLOADED_BUTTON = new Locator("OK_UPLOADED_BUTTON", new By.ByXPath("//button[contains(text(),'Ok, got it')]"));
    private static final Locator BANK_PAYMENT_RECON_MENU = new Locator("BANK_PAYMENT_RECON_MENU", new By.ByXPath("//div[contains(text(),'Bank Payment Recon')]"));
    private static final Locator ORDER_ID_INVOICE_INPUT = new Locator("ORDER_ID_INVOICE_INPUT", new By.ByXPath("//div[contains(@class, 'ReconcileCheckFilterInvoice')]//input[@id='search-by']"));
    private static final Locator ORDER_ID_INVOICE_OPTION = new Locator("ORDER_ID_INVOICE_OPTION", By.xpath("(//div[@data-testid='the-popoverlist']//label[normalize-space()='Order ID'])[2]"));
    private static final Locator ORDER_ID_VALUE_INPUT = new Locator("ORDER_ID_VALUE_INPUT", By.id("order-id"));
    private static final Locator ORDER_ID_SETTLEMENT_INPUT = new Locator("ORDER_ID_SETTLEMENT_INPUT", new By.ByXPath("(//input[@id='enter-order-id'])[1]"));
    private static final Locator SEARCH_INVOICE_BUTTON = new Locator("SEARCH_INVOICE_BUTTON", new By.ByClassName("ReconcileCheckFilterInvoice_search_button__oT2XK"));
    private static final Locator SEARCH_ORDER_ID_INPUT = new Locator("SEARCH_ORDER_ID_INPUT", By.xpath("(//button[text()='Search'])[1]"));
    private static final Locator SETTLEMENT_CHECKBOX = new Locator("SETTLEMENT_CHECKBOX", By.xpath("(//label[@class='Check_check__GziHG']//input[@type='checkbox'])[1]"));
    private static final Locator INVOICE_CHECKBOX = new Locator("INVOICE_CHECKBOX", By.xpath("(//label[@class='Check_check__GziHG']//input[@type='checkbox'])[2]"));
    private static final Locator CHOOSE_CURRENCY_DROPDOWN = new Locator("CHOOSE_CURRENCY_DROPDOWN", By.xpath("//input[@id='choose-currency']"));
    private static final Locator USD_CURRENCY_OPTION = new Locator("USD_CURRENCY_OPTION", By.xpath("//div[@class='popoverlist_list']//label[text()='USD']"));
    private static final Locator DETAILS_BUTTON = new Locator("DETAILS_BUTTON", new By.ByXPath("(//p[contains(text(),'Details')])[1]"));
    private static final Locator EXTRA_DETAILS = new Locator ("EXTRA_DETAILS", By.xpath("(//div[contains(@class,'DropdownHistoryAction_icon_more__52Wpq')])[1]"));
    private static final Locator REVOKED_ACTION = new Locator("REVOKED_ACTION", By.xpath("//div[contains(@class,'DropdownHistoryAction_list_action__ycvZ4')]/p[normalize-space()='Revoked']"));
    private static final Locator YES_REVOKE_BUTTON = new Locator("YES_REVOKE_BUTTON", By.xpath("//button[normalize-space()='Yes, revoke']"));
    private static final Locator EXPORT_REPORT_BUTTON = new Locator("EXPORT_REPORT_BUTTON", By.xpath("//p[text()='Export Report']"));
    private static final Locator uploadDateLoc = new Locator("uploadDateLoc", By.xpath("//p[contains(text(), 'Upload Date')]/following::p[1]"));
    private static final Locator amountLoc = new Locator("amountLoc", By.xpath("//tbody[@data-testid='table:list-body']/tr/td[3]/div"));
    private static final Locator uploadByLoc = new Locator("uploadByLoc", By.xpath("//p[contains(text(), 'Upload By')]/following::p[1]"));
    private static final Locator uploadStatusLoc = new Locator("uploadStatusLoc", By.xpath("//p[contains(text(), 'Upload Status')]/following::div[contains(@class, 'Label_color_green__')]"));
    private static final Locator fileNameLoc = new Locator("fileNameLoc", By.xpath("//p[contains(text(), 'File Name')]/following::p[1]"));
    private static final Locator reconLoc = new Locator("reconLoc", By.xpath("//tbody[@data-testid='table:list-body']/tr/td[3]/div/div"));
    private static final Locator totalUploadedDataLoc = new Locator("totalUploadedDataLoc", By.xpath("//p[contains(text(), 'Total Uploaded Data')]/following::p[1]"));
    private static final Locator orderIdLoc = new Locator("orderIdLoc", By.xpath("//tbody[@data-testid='table:list-body']/tr/td[2]"));
    private static final Locator totalUploadedAmountLoc = new Locator("totalUploadedAmountLoc", By.xpath("//p[contains(text(), 'Total Uploaded Amount')]/following::p[1]//div"));
    private static final Locator orderIDLoc = new Locator("orderIDLoc", By.xpath("//tbody[@data-testid='table:list-body']/tr/td[6]"));
    private static final Locator statusLoc = new Locator("statusLoc", By.xpath("//tbody[@data-testid='table:list-body']/tr/td[9]"));
    private static final Locator BANK_RECON_REPORTS_MENU = new Locator("BANK_RECON_REPORTS_MENU", new By.ByXPath("//div[contains(text(),'Bank Recon Reports')]"));
    private static final Locator CHOOSE_START_DATE = new Locator("CHOOSE_START_DATE", By.id("choose-start-date"));
    private static final Locator totalAutomatchedDataLoc = new Locator("totalAutomatchedDataLoc", By.xpath("//p[contains(text(), 'Total Auto-matched Data')]/following::p[1]"));
    private static final Locator totalUnmatchedDataLoc = new Locator("totalUnmatchedDataLoc", By.xpath("//p[contains(text(), 'Total Unmatched Data')]/following::p[1]"));
    private static final Locator customerPriceLoc = new Locator("customerPriceLoc", By.xpath("//tbody[@data-testid='table:list-body']/tr/td[4]"));
    private static final Locator createdDateLoc = new Locator("createdDateLoc", By.xpath("//tbody[@data-testid='table:list-body']/tr/td[9]"));
    private static final Locator reconciliationMethodLoc = new Locator("reconciliationMethodLoc", By.xpath("//tbody[@data-testid='table:list-body']/tr/td[18]"));
    private static final Locator reconciledLoc = new Locator("reconciledLoc", By.xpath("//tbody[@data-testid='table:list-body']/tr/td[19]"));
    private static final Locator PAYMENT_METHOD = new Locator("PAYMENT_METHOD", By.xpath("//tbody[@data-testid='table:list-body']/tr/td[6]"));
    private static final Locator VENDOR_ID_AUTOCOMPLETE = new Locator("VENDOR_ID_AUTOCOMPLETE", new By.ById("vendor-id"));
    private static final Locator VENDOR_ID_CHECKLIST = new Locator("VENDOR_ID_CHECKLIST", new By.ByXPath("(//span[contains(@class, 'Check_custom_checkbox')])[2]"));
    private static final Locator VENDOR_ID_SAVE = new Locator("VENDOR_ID_SAVE", By.xpath("//button[contains(text(), 'Save')]"));
    private static final Locator B2C_DAILY_VERTICAL_TITLE_FLIGHT_INTERNATIONAL = new Locator("B2C_DAILY_VERTICAL_TITLE_FLIGHT_INTERNATIONAL", By.xpath("//div[contains(text(),'Flight International')]"));
    private static final Locator VENDOR_LISTING_B2C_SEARCH = new Locator("VENDOR_LISTING_B2C_SEARCH", By.xpath("//button[@data-testid='search-submit']"));
    private static final Locator B2C_TTD_MENU = new Locator("B2C_TTD_MENU", new By.ByXPath("//div[contains(text(),'Things To Do')]"));
    private static final Locator B2C_DAILY_VERTICAL_TITLE_TTD = new Locator("B2C_DAILY_VERTICAL_TITLE_TTD", By.xpath("//div[contains(text(),'Things To Do')]"));
    private static final Locator B2C_DAILY_VERTICAL_TITLE_FLIGHT_DOMESTIC = new Locator("B2C_DAILY_VERTICAL_TITLE_FLIGHT_DOMESTIC", By.xpath("//div[contains(text(),'Flight Domestic')]"));
    private static final Locator B2C_FERRY_MENU = new Locator("B2C_BUS_MENU", new By.ByXPath("//div[contains(text(),'Ferry')]"));
    private static final Locator B2C_DAILY_VERTICAL_TITLE_FERRY = new Locator("B2C_DAILY_VERTICAL_TITLE_BUS", By.xpath("//div[contains(text(),'Ferry')]"));
    private static final Locator B2C_BUS_MENU = new Locator("B2C_BUS_MENU", new By.ByXPath("//div[contains(text(),'Bus & Shuttle')]"));
    private static final Locator B2C_DAILY_VERTICAL_TITLE_BUS = new Locator("B2C_DAILY_VERTICAL_TITLE_BUS", By.xpath("//div[contains(text(),'Bus & Shuttle')]"));
    private static final Locator EDIT_CONFIGURATION_BUTTON = new Locator("EDIT_CONFIGURATION_BUTTON", new By.ByXPath("//span[contains(text(),'Edit')]"));
    private static final Locator EDIT_CONFIGURATION_SAVE_BUTTON = new Locator("EDIT_CONFIGURATION_SAVE_BUTTON", By.cssSelector("button[data-testid='edit-commission-submit']"));
    private static final Locator SNACKBAR_EDIT_CONFIGURATION_PAGE_SUCCESS = new Locator("SNACKBAR_EDIT_CONFIGURATION_PAGE_SUCCESS", new By.ByXPath("//div[contains(text(),'Successfully edited a commission configuration.')]"));
    private static final Locator VAT_INPUT_EDIT_CONFIGURATION_PAGE = new Locator("VAT_INPUT_EDIT_CONFIGURATION_PAGE", By.name("tax_vat"));
    private static final Locator WHT_INPUT_EDIT_CONFIGURATION_PAGE = new Locator("WHT_INPUT_EDIT_CONFIGURATION_PAGE", By.name("withholding_tax"));
    private static final Locator HEADER_ACCOUNT = new Locator("HEADER_ACCOUNT", By.xpath("//nav"));
    private static final Locator LOGOUT_BUTTON = new Locator("LOGOUT_BUTTON", By.xpath("//div[./span[text()='Log out']]"));
    private static final Locator SETTLEMENT_DROPDOWN_MENU = new Locator("SETTLEMENT_DROPDOWN_MENU", By.xpath("//span[normalize-space()='Settlement' or normalize-space()='Penyelesaian']/ancestor::div[@role='button']"));
    private static final Locator VCC_OPTION = new Locator("VCC_OPTION", By.xpath("//div[normalize-space()='VCC']"));
    private static final Locator ADD_NEW_VCC_OPTION = new Locator("ADD_NEW_VCC_OPTION", By.xpath("//button[contains(text(),'Add virtual credit card') or contains(text(),'Tambah kartu kredit virtual')]"));
    private static final Locator VCC_PAGE_HEADING = new Locator("VCC_PAGE_HEADING", By.xpath("//h3[@data-testid='title-title']"));
    private static final Locator PURCHASE_TYPE = new Locator("PURCHASE_TYPE", By.xpath("(//input[@id='purchase-type'])[1]"));
    private static final Locator PURCHASE_TYPE_SELECTOR = new Locator("PURCHASE_TYPE_SELECTOR", By.xpath("//label[normalize-space()='B2B']/ancestor::div[contains(@class,'popoverlist_list')]"));
    private static final Locator NAME_ON_CARD = new Locator("NAME_ON_CARD", By.xpath("//input[@id='name-on-card']"));
    private static final Locator EXPIRY_DATE = new Locator("EXPIRY_DATE", By.xpath("//input[@id='expiry-date']"));
    private static final Locator EXPIRY_DATE_SELECTOR = new Locator("EXPIRY_DATE_SELECTOR", By.xpath("(//div[contains(@class,'popoverlist_menu') and contains(@class,'popoverlist_slidedown_enter_active')]//div[contains(@class,'popoverlist_list')]//label)[1]"));
    private static final Locator ISSUED_CARD_TYPE = new Locator("ISSUED_CARD_TYPE", By.xpath("//input[@id='issued-card-type']"));
    private static final Locator ISSUED_CARD_TYPE_SELECTOR = new Locator("ISSUED_CARD_TYPE_SELECTOR", By.xpath("//label[normalize-space()='Master Card']/ancestor::div[contains(@class,'popoverlist_list')]"));
    private static final Locator CREDIT_LIMIT = new Locator("CREDIT_LIMIT", By.xpath("//input[@id='credit-limit']"));
    private static final Locator CURRENCY_TYPE = new Locator("CURRENCY_TYPE", By.xpath("//input[@id='currency-type']"));
    private static final Locator CURRENCY_TYPE_SELECTOR = new Locator("CURRENCY_TYPE_SELECTOR", By.xpath("//label[normalize-space()='Billing']/ancestor::div[contains(@class,'popoverlist_list')]"));
    private static final Locator MAXIMUM_TRANSACTIONS_ALLOWED = new Locator("MAXIMUM_TRANSACTIONS_ALLOWED", By.xpath("//input[@id='maximum-transactions-allowed']"));
    private static final Locator PERIOD = new Locator("PERIOD", By.xpath("//input[@id='period']"));
    private static final Locator PERIOD_SELECTOR = new Locator("PERIOD_SELECTOR", By.xpath("//label[normalize-space()='Monthly']/ancestor::div[contains(@class,'popoverlist_list')]"));
    private static final Locator VCC_LISTING_PAGE_HEADER = new Locator("VCC_LISTING_PAGE_HEADER", By.xpath("//h3[normalize-space()='Virtual Credit Card']"));
    private static final Locator POPUP = new Locator("POPUP", By.xpath("//div[contains(@class,'LoadingWithIllustration_card__wUteG')]"));
    private static final Locator FIRST_VCC_CREDIT_LIMIT = new Locator("FIRST_VCC_CREDIT_LIMIT", By.xpath("//table[@data-testid='table:list']//tbody//tr[1]/td[12]"));
    private static final Locator FIRST_VCC_MORE_BUTTON = new Locator("FIRST_VCC_MORE_BUTTON", By.xpath("(//input[@id='more'])[1]"));
    private static final Locator VIEW_BALANCE = new Locator("VIEW_BALANCE", By.xpath("//p[normalize-space()='View']/ancestor::div[contains(@class,'VCCActionButton_list_action')]"));
    private static final Locator VCC_DETAILS_PAGE_HEADER = new Locator("VCC_DETAILS_PAGE_HEADER", By.xpath("//span[contains(@class,'Text_weight_bold') and normalize-space()='VCC Detail Page']"));
    private static final Locator EDIT_VCC_BTN = new Locator("EDIT_VCC_BTN", By.xpath("//button[normalize-space()='Edit' or normalize-space()='Sunting']"));
    private static final Locator SAVE_MODAL = new Locator("SAVE_MODAL", By.xpath("//section[contains(@class,'Modal_modal_popup')]//button [contains(@class,'Button_variant_primary') and  (normalize-space()='Save' or normalize-space()='Simpan')]"));
    private static final Locator CARD_NUMBER = new Locator("CARD_NUMBER", By.xpath("//table[@data-testid='table:list']//tbody//tr[1]/td[5]"));
    private static final Locator SINGLE_SLAB = new Locator("SINGLE_SLAB", By.xpath("//span[normalize-space()='Single Slab']"));
    private static final Locator TIER_1_COMMISSION_TEXT = new Locator("TIER_1_COMMISSION_OPTION", By.xpath("//span[contains(text(),'Tier Commission') or contains(text(),'Komisi Tier')]"));
    private static final Locator MULTI_SLAB = new Locator("MULTI_SLAB", By.xpath("//span[normalize-space()='Multi Slab']"));
    private static final Locator ADD_TIER_BUTTON = new Locator("ADD_TIER_BUTTON", By.xpath("//button[@data-testid='tier-commission-add']"));
    private static final Locator TOGGLE_BTN = new Locator("TOGGLE_BTN", By.xpath("//div[contains(@class,'Toggle_toggle')]"));
    private static final Locator DELETE_LOCATOR = new Locator("DELETE_LOCATOR", By.xpath("(//span[contains(text(),'Delete tier') or contains(text(),'Hapus tier')])[1]"));
    private static final Locator PREVIOUS_MONTH_BUTTON = new Locator("PREVIOUS_MONTH_BUTTON",By.xpath("//button[contains(@class, 'CalendarDesktop_prev_button')]"));
    private static final Locator START_DATE_INPUT = new Locator("START_DATE_INPUT",By.xpath("//input[@data-testid='input-daterange-start-default']"));
    private static final Locator VIEW_LIST_OF_TRANSACTIONS_BY = new Locator("VIEW_LIST_OF_TRANSACTIONS_BY", By.xpath("//div[contains(normalize-space(text()), 'View List of Transactions by:')]"));
    private static final Locator TRAIN_MENU = new Locator("TRAIN_MENU", new By.ByXPath("//div[contains(text(),'Train')]"));
    private static final Locator ACCOMMODATION_THIRD_PARTY_MENU = new Locator("ACCOMMODATION_THIRD_PARTY_MENU", By.xpath("//div[contains(text(),'Accommodation (Third Party)')]"));
    private static final Locator ACCOMMODATION_NATIVE_MENU = new Locator("ACCOMMODATION_NATIVE_MENU", By.xpath("//div[contains(text(),'Accommodation (Native)')]"));
    private static final Locator ACCOMM_CUSTOMER_CURRENCY_ROW1 = new Locator("ACCOMM_CUSTOMER_CURRENCY_ROW1", By.xpath("//tbody[@data-testid='table:list-body']/tr[1]/td[106]"));
    private static final Locator ACCOMM_VENDOR_CURRENCY_ROW1 = new Locator("ACCOMM_VENDOR_CURRENCY_ROW1", By.xpath("//tbody[@data-testid='table:list-body']/tr[1]/td[110]"));
    private static final Locator ACCOMM_ADDON_DETAILS_ROW1 = new Locator("ACCOMM_ADDON_DETAILS_ROW1", By.xpath("//tbody[@data-testid='table:list-body']/tr[1]/td[32]"));
    private static final Locator HEADER_ACCOMMODATION_THIRD_PARTY = new Locator("HEADER_ACCOMMODATION_THIRD_PARTY", By.xpath("//h3[contains(text(),'Third-Party Accommodation')]"));
    private static final Locator CREDIT_MEMO_SUBMENU = new Locator("CREDIT_MEMO_SUBMENU", new By.ByXPath("//div[text()='Credit Memo']"));
    private TopUpDetail topUpDetail;
    private Locator PAYMENT_DATE_LOCATOR;
    private WebElement PROOF_OF_TRANSFER;
    private String dateTime;
    private boolean isCurrentDate;
    private String vendorId;
    private String entity;
    private String settlementType;
    private String vendorCurrency;
    private List<TopUpDetail> topUpDetailList = new ArrayList<>();

    public void logout() {
        driverTools.waitForElementToAppear(HEADER_ACCOUNT, 2);
        driverTools.click(HEADER_ACCOUNT);
        driverTools.click(LOGOUT_BUTTON);
    }

    public void goToFinanceDashboard(String username, String password) {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.goToPage(INTOOLS_PAGE_URL);
    }

    public void goToFinanceDashboardViaUI(String username, String password) {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.goToPage(INTOOLS_PAGE_URL);
        memberLoginPage.adminLogin(username, password);
    }

    private static Locator getReconUploadsFirstRowCellByIndex(int columnIndex) {
        return new Locator("RECON_UPLOADS_FIRST_ROW_CELL", By.xpath("(//table[@data-testid='table:list']//tbody//tr)[1]/td[" + columnIndex + "]"));
    }

    private static Locator getUploadDetailValueLocatorByLabel(String label) {
        return new Locator("UPLOAD_DETAIL_VALUE", By.xpath("//div[contains(@class,'reconciliation_upload_detail_info_item')][.//span[normalize-space()='" + label + "']]//strong"));
    }

    public void goToReconciliationMenu() {
        driverTools.waitForElementToAppear(RECONCILIATION_MENU);
        driverTools.click(RECONCILIATION_MENU);
    }

    public void goToReconciliationUploadsMenu() {
        driverTools.waitForElementToAppear(RECON_UPLOADS_SUBMENU);
        driverTools.click(RECON_UPLOADS_SUBMENU);
    }

    public void clickFirstRowViewButton() {
        driverTools.waitForElementToAppear(RECON_UPLOADS_FIRST_ROW_VIEW_BUTTON);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(RECON_UPLOADS_FIRST_ROW_VIEW_BUTTON);
    }

    public boolean isUploadDetailPageDisplayed() {
        driverTools.waitForElementToAppear(UPLOAD_DETAIL_INFO_GRID);
        return driverTools.isElementDisplayed(UPLOAD_DETAIL_INFO_GRID);
    }

    private int getReconUploadsHeadingIndex(String heading) {
        driverTools.waitForElementToAppear(RECON_UPLOADS_TABLE_HEADINGS);
        List<WebElement> headings = driverTools.elements(RECON_UPLOADS_TABLE_HEADINGS).orElseThrow(() -> new IllegalStateException("No headings found on Uploads table"));
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

    public String getReconUploadsTableValueByHeading(String heading) {
        try {
            int index = getReconUploadsHeadingIndex(heading);
            if (index == -1) {
                throw new IllegalStateException("heading is not found on the table");
            }
            Locator cell = getReconUploadsFirstRowCellByIndex(index + 1);
            driverTools.waitForElementToAppear(cell);
            String text = driverTools.getText(cell).orElse("").trim();
            if (text.isEmpty()) {
                text = driverTools.getAttributeTextContent(cell).orElse("").trim();
            }
            if (text.isEmpty()) {
                throw new IllegalStateException("value is empty");
            }
            logger.log("Got %s value for heading: %s".formatted(text, heading));
            return text;
        } catch (Throwable e) {
            Assertion.skip("Cannot get %s from Uploads table, skipping: %s".formatted(heading, e.getMessage()));
            return "";
        }
    }

    public Map<String, String> getReconUploadsFirstRowData(String... headings) {
        Map<String, String> rowData = new LinkedHashMap<>();
        for (String heading : headings) {
            rowData.put(heading, getReconUploadsTableValueByHeading(heading));
        }
        return rowData;
    }

    private static Locator getUploadsFilterOptionCheckboxByValue(String value) {
        return new Locator("UPLOADS_FILTER_OPTION_CHECKBOX", By.xpath("//label[contains(@class,'TableColumnFilterOrderId_option_row')][.//span[normalize-space()='" + value + "']]//input[@type='checkbox']"));
    }

    public void clickUploadedByFilter() {
        driverTools.waitForElementToAppear(RECON_UPLOADS_UPLOADED_BY_FILTER);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(RECON_UPLOADS_UPLOADED_BY_FILTER);    
    }

    public void searchUploadedByFilter(String uploadedBy) {
        driverTools.waitForElementToAppear(RECON_UPLOADS_FILTER_SEARCH_INPUT);
        driverTools.sendKeys(RECON_UPLOADS_FILTER_SEARCH_INPUT, uploadedBy);
    }

    public void selectUploadsFilterOptionByValue(String value) {
        try {
            Locator checkbox = getUploadsFilterOptionCheckboxByValue(value);
            driverTools.waitForElementToAppear(checkbox);
            driverTools.clickOrFallbackToClickWithJavascriptExecutor(checkbox);
        } catch (Throwable e) {
            Assertion.skip("Cannot select %s on Uploads filter, skipping: %s".formatted(value, e.getMessage()));
        }
    }

    public void clickUploadsFilterApplyButton() {
        driverTools.waitForElementToAppear(RECON_UPLOADS_FILTER_APPLY_BUTTON);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(RECON_UPLOADS_FILTER_APPLY_BUTTON);
    }

    public boolean isUploadedByFilterActiveDotDisplayed() {
        return driverTools.waitForElementToAppear(RECON_UPLOADS_UPLOADED_BY_FILTER_ACTIVE_DOT, 10).isPresent();
    }

    public List<String> getReconUploadsColumnValuesByHeading(String heading) {
        List<String> values = new ArrayList<>();
        int index = getReconUploadsHeadingIndex(heading);
        if (index == -1) {
            logger.log("Heading: %s is not found on Uploads table".formatted(heading));
            return values;
        }
        Locator columnValues = new Locator("RECON_UPLOADS_COLUMN_VALUES", By.xpath("//table[@data-testid='table:list']//tbody//tr/td[%d]".formatted(index + 1)));
        driverTools.waitForElementToAppear(columnValues);
        for (WebElement element : driverTools.elements(columnValues).orElse(new ArrayList<>())) {
            String text = element.getText().trim();
            if (text.isEmpty()) {
                text = element.getAttribute("textContent").trim();
            }
            values.add(text);
        }
        logger.log("Got %d value(s) for heading: %s".formatted(values.size(), heading));
        return values;
    }

    public String getUploadDetailValueByLabel(String label) {
        Locator value = getUploadDetailValueLocatorByLabel(label);
        driverTools.waitForElementToAppear(value);
        String text = driverTools.getText(value).orElse("").trim();
        if (text.isEmpty()) {
            text = driverTools.getAttributeTextContent(value).orElse("").trim();
        }
        logger.log("Got %s value for label: %s on upload detail page".formatted(text, label));
        return text;
    }

    public void goToBankStatementUplopadMenu() {
        driverTools.click(BANK_STATEMENT_UPLOAD_MENU);
    }

    public void clickDownloadTemplateButton() {
        WebElement exportCSV = driverTools.waitForElementToAppear(DOWNLOAD_TEMPLATE_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(exportCSV);
    }

    public boolean verifyDownloadedExportCSV(String fileNameTemplateRecon, String timeOutMiliSec) {
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        logger.log("Download folder: " + downloadDir);
        logger.log("expected filename: " + fileNameTemplateRecon);
        boolean fileDownloaded = false;

        long startTime = Instant.now().toEpochMilli();
        logger.log("Start time: " + startTime);
        long waitTime = startTime + Integer.parseInt(timeOutMiliSec);
        logger.log("Wait time: " + waitTime);

        while (Instant.now().toEpochMilli() < waitTime) {
            File[] listFiles = new File(downloadDir).listFiles();
            for (File file : listFiles) {
                if (file.getName().equals(fileNameTemplateRecon)) {
                    String[] listFilesName = new File(downloadDir).list(); //logging purpose
                    for (int i = 0; i < listFilesName.length; i++) {
                        logger.log("File name on dir: " + listFilesName[i]);
                    }
                    fileDownloaded = true;
                    file.delete();
                    break;
                }
            }
            if (fileDownloaded) break;
        }

        return fileDownloaded;
    }

    private static final Locator getMonthByDropdown(String month) {
        return new Locator("VALUE_SELECT_MONTHLY_LOCATOR", new By.ByXPath("//*[@class='Text_text__DSnue Text_size_b3__6n_9j' and text()='" + month + "']"));
    }

    private static final Locator getYearByDropdown(String year) {
        return new Locator("VALUE_SELECT_MONTHLY_LOCATOR", new By.ByXPath("//*[@class='Text_text__DSnue Text_size_b3__6n_9j' and text()='" + year + "']"));
    }

    private static final Locator clickDownloadCSVinSummaryDetail(String year) {
        return new Locator("DOWNLOAD_CSV_SUMMARY_DETAIL_LOCATOR", new By.ByXPath("//*[@class='Text_text__DSnue Text_size_b3__6n_9j' and text()='" + year + "']"));
    }

    public boolean isFinanceDashboardLoaded() {
        driverTools.waitForElementToAppear(FINANCE_DASHBOARD_LOGO, 30);
        return driverTools.isElementDisplayed(FINANCE_DASHBOARD_LOGO);
    }

    public void openCommissionDashboardAccordionMenu() {
        driverTools.click(ACCORDION_MENU);
    }

    public void clickFinderMenu() {
        driverTools.click(FINDER_MENU);
    }

    public void clickSearchTransactionsMenu() {
        driverTools.click(SEARCH_TRANSACTIONS_MENU);
    }

    public void clickSearchByInput() {
        driverTools.click(SEARCH_BY_INPUT);
    }

    public void enterSearchData(String searchData) {
        driverTools.sendKeys(SEARCH_DATA_TEXTAREA, searchData);
    }

    public void clickSalesMenu() {
        driverTools.click(SALES_MENU);
    }

    public void clickSalesOrderMenu() {
        driverTools.click(SALES_ORDER_MENU);
    }

    public void clickSalesOrderFilter() {
        driverTools.click(SALES_ORDER_FILTER);
    }

    public void enterSalesOrderIdSearch(String orderId) {
        driverTools.waitForElementToPresence(SALES_ORDER_ID_SEARCH_INPUT);
        driverTools.sendKeys(SALES_ORDER_ID_SEARCH_INPUT, orderId);
    }

    public void clickSalesOrderIdCheckbox() {
        driverTools.waitForElementToAppear(SALES_ORDER_ID_CHECKBOX);
        driverTools.click(SALES_ORDER_ID_CHECKBOX);
    }

    public void clickSalesOrderApplyButton() {
        driverTools.click(SALES_ORDER_APPLY_BUTTON);
    }

    public String getSalesOrderResultOrderId(String orderId) {
        driverTools.waitForTextToBePresent(SALES_ORDER_RESULT_ORDER_ID, orderId);
        return driverTools.getText(SALES_ORDER_RESULT_ORDER_ID).orElse("");
    }

    public String getSalesOrderResultSource(String source) {
        driverTools.waitForTextToBePresent(SALES_ORDER_RESULT_SOURCE, source);
        return driverTools.getText(SALES_ORDER_RESULT_SOURCE).orElse("");
    }

    public String getSalesOrderResultPrice() {
        driverTools.waitForElementToAppear(SALES_ORDER_RESULT_PRICE);
        return driverTools.getText(SALES_ORDER_RESULT_PRICE).orElse(null);
    }

    public void clickSalesOrderResultRow() {
        driverTools.waitForElementToAppear(SALES_ORDER_RESULT_ROW);
        driverTools.click(SALES_ORDER_RESULT_ROW);
    }

    public String getSalesOrderDetailRefundId() {
        driverTools.waitForElementToAppear(SALES_ORDER_DETAIL_REFUND_LINK);
        return driverTools.getText(SALES_ORDER_DETAIL_REFUND_LINK).orElse(null);
    }

    public void clickSalesOrderDetailRefundLink() {
        int handleCountBeforeClick = driverTools.getWindowHandles().size();
        driverTools.click(SALES_ORDER_DETAIL_REFUND_LINK);
        driverTools.waitForNewTabAndSwitchToLatest(handleCountBeforeClick);
    }

    public String getCurrentUrl() {
        return driverTools.getCurrentURL();
    }

    public String getSalesRefundDetailRefundId() {
        driverTools.waitForElementToAppear(SALES_REFUND_DETAIL_REFUND_ID);
        return driverTools.getText(SALES_REFUND_DETAIL_REFUND_ID).orElse(null);
    }

    public String getSalesRefundDetailOrderId() {
        driverTools.waitForElementToAppear(SALES_REFUND_DETAIL_ORDER_ID);
        return driverTools.getText(SALES_REFUND_DETAIL_ORDER_ID).orElse(null);
    }

    public void goToFlightDomesticMenu() {
        if (!driverTools.isElementDisplayed(FLIGHT_DOMESTIC_MENU)) {
            driverTools.click(COMM_DASHBOARD_MENU);
        }
        driverTools.click(FLIGHT_DOMESTIC_MENU);
    }

    public void goToFlightInternationalMenu() {
        driverTools.click(FLIGHT_INTERNATIONAL_MENU);
    }

    public void goToTrainMenu() {
        driverTools.click(TRAIN_MENU);
    }

    public Locator getSearchByOption(String searchByLabel) {
        return new Locator("SEARCH_BY_OPTION", new By.ByXPath(String.format("//label[normalize-space()='%s']", searchByLabel)));
    }

    public void clickSearchByOption(String searchByLabel) {
        driverTools.click(getSearchByOption(searchByLabel));
    }

    public Locator getVendorNameOption(String vendorName) {
        return new Locator("VENDOR_NAME_OPTION", new By.ByXPath(String.format("//div[contains(text(), '%s')]", vendorName)));
    }

    public void searchVendorName(String vendorName) {
        driverTools.click(VENDOR_NAME_DROPDOWN);
        Locator VENDOR_NAME_OPTION = getVendorNameOption(vendorName);
        driverTools.dummyWait(5);
        driverTools.clickWithJavascriptExecutor(VENDOR_NAME_OPTION);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public String getFirstRowVendorName() {
        return getFirstRowVendorName(null);
    }

    public String getFirstRowVendorName(String expectedVendorName) {
        driverTools.waitForElementToAppear(FIRST_ROW_VENDOR_NAME);
        if (expectedVendorName != null && !expectedVendorName.isEmpty()) {
            driverTools.waitForTextToBePresent(FIRST_ROW_VENDOR_NAME, expectedVendorName);
        }
        return driverTools.getText(FIRST_ROW_VENDOR_NAME).orElse("");
    }

    public String getTxRow1OrderId(String orderId) {
        driverTools.waitForTextToBePresent(TX_ROW2_ORDER_ID, orderId);
        return driverTools.getText(TX_ROW2_ORDER_ID).orElse("");
    }

    public String getTxRow2OrderId(String orderId) {
        driverTools.waitForTextToBePresent(TX_ROW1_ORDER_ID, orderId);
        return driverTools.getText(TX_ROW1_ORDER_ID).orElse("");
    }

    public String getTxRow1ProductName(String productName) {
        driverTools.waitForTextToBePresent(TX_ROW1_PRODUCT_NAME, productName);
        return driverTools.getText(TX_ROW1_PRODUCT_NAME).orElse("");
    }

    public String getTxRow2ProductName(String productName) {
        driverTools.waitForTextToBePresent(TX_ROW2_PRODUCT_NAME, productName);
        return driverTools.getText(TX_ROW2_PRODUCT_NAME).orElse("");
    }

    public String getTxRow1CountOfAdult() {
        driverTools.waitForElementToAppear(TX_ROW1_COUNT_OF_ADULT);
        return driverTools.getText(TX_ROW1_COUNT_OF_ADULT).orElse("");
    }

    public String getTxRow1BasicPriceBaggage() {
        driverTools.waitForElementToAppear(TX_ROW1_BASIC_PRICE_BAGGAGE);
        return driverTools.getText(TX_ROW1_BASIC_PRICE_BAGGAGE).orElse("");
    }

    public String getTxRow1BasicPriceMeals() {
        driverTools.waitForElementToAppear(TX_ROW1_BASIC_PRICE_MEALS);
        return driverTools.getText(TX_ROW1_BASIC_PRICE_MEALS).orElse("");
    }

    public String getTxRow1BasicPriceSeat() {
        driverTools.waitForElementToAppear(TX_ROW1_BASIC_PRICE_SEAT);
        return driverTools.getText(TX_ROW1_BASIC_PRICE_SEAT).orElse("");
    }

    public String getTxRow1CustomerCurrency() {
        driverTools.waitForElementToAppear(TX_ROW1_CUSTOMER_CURRENCY);
        return driverTools.getText(TX_ROW1_CUSTOMER_CURRENCY).orElse("");
    }

    public String getTxRow1VendorCurrency() {
        driverTools.waitForElementToAppear(TX_ROW1_VENDOR_CURRENCY);
        return driverTools.getText(TX_ROW1_VENDOR_CURRENCY).orElse("");
    }

    public void clickDownloadButton() {
        driverTools.waitForElementToAppear(DOWNLOAD_BUTTON);
        driverTools.clickWithJavascriptExecutor(DOWNLOAD_BUTTON);
    }

    public String getDownloadSnackbarText() {
        driverTools.waitForElementToPresence(DOWNLOAD_SNACKBAR_TEXT);
        return driverTools.getText(DOWNLOAD_SNACKBAR_TEXT).orElse("");
    }

    public void selectSearchByOption(String optionText) {
        driverTools.waitForElementToPresence(SEARCH_BY_DROPDOWN);
        driverTools.click(SEARCH_BY_DROPDOWN);
        Locator searchByOption = new Locator("SEARCH_BY_OPTION",
                By.xpath(String.format("//div[contains(@class,'popoverlist_list')]/label[contains(text(),'%s')]", optionText)));
        driverTools.waitForElementToAppear(searchByOption);
        driverTools.click(searchByOption);
    }

    private static Locator calendarDayLocator(String day) {
        return new Locator("CALENDAR_DAY_" + day,
                By.xpath(String.format("(//span[contains(@class,'Day_day_number__rQ1Bc') and text()='%s'])[1]", day)));
    }

    public void enterTransactionSearchOrderId(String orderId) {
        driverTools.waitForElementToAppear(TRANSACTION_SEARCH_INPUT);
        driverTools.sendKeys(TRANSACTION_SEARCH_INPUT, orderId);
    }

    public void clickStartDateCalendar() {
        driverTools.waitForElementToAppear(DATE_RANGE_START_CALENDAR);
        driverTools.click(DATE_RANGE_START_CALENDAR);
    }

    public void clickEndDateCalendar() {
        driverTools.waitForElementToPresence(DATE_RANGE_END_CALENDAR);
        driverTools.clickWithJavascriptExecutor(DATE_RANGE_END_CALENDAR);
    }

    public void selectCalendarDay(String day) {
        driverTools.dummyWait(1);
        Locator dayLocator = calendarDayLocator(day);
        driverTools.waitForElementToAppear(dayLocator);
        driverTools.click(dayLocator);
    }

    public void selectCalendarDay(String monthYear, String day) {
        Locator monthLocator = new Locator("CALENDAR_MONTH", By.xpath(String.format("//h2[normalize-space()='%s']", monthYear)));
        driverTools.waitForElementToAppear(PREVIOUS_MONTH_BUTTON, 5);
        while (!driverTools.isElementDisplayed(monthLocator)) {
            driverTools.clickWithJavascriptExecutor(PREVIOUS_MONTH_BUTTON);
            driverTools.waitForElementToAppear(monthLocator, 1);
        }
        Locator dayLocator = new Locator("CALENDAR_DAY", By.xpath(String.format("//*[contains(@class,'CalendarDesktop_month')][.//h2[normalize-space()='%s']]//span[contains(@class,'Day_day_number') and normalize-space()='%s']", monthYear, day)));
        for (int i = 0; i < 2; i++) {
            driverTools.waitForElementToAppear(dayLocator);
            driverTools.waitForElementToBeClickable(dayLocator);
            driverTools.clickWithJavascriptExecutor(dayLocator);
        }
    }

    public void searchVendorNameV2(String vendorName) {
        driverTools.click(VENDOR_NAME_DROPDOWN);
        Locator VENDOR_NAME_OPTION = getVendorNameOption(vendorName);
        driverTools.waitForElementToAppear(VENDOR_NAME_OPTION, 5);
        driverTools.scrollElementToCenter(VENDOR_NAME_OPTION);
        driverTools.clickWithJavascriptExecutor(VENDOR_NAME_OPTION);
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.waitForElementToBeClickable(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public void clickSearchButton() {
        driverTools.waitForElementToAppear(SEARCH_BUTTON);
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
    }

    public void clickResetFilterButton() {
        driverTools.waitForElementToAppear(RESET_FILTER_BUTTON);
        driverTools.clickWithJavascriptExecutor(RESET_FILTER_BUTTON);
    }

    public void clickSearchButtonReconReports() {
        driverTools.waitForElementToAppear(SEARCH_BUTTON_RECON_REPORTS);
        driverTools.click(SEARCH_BUTTON_RECON_REPORTS);
    }

    public void clickMoreButton() {
        try {
            driverTools.waitForElementToBeClickable(MORE_BUTTON);
            driverTools.click(MORE_BUTTON);
        } catch (ElementClickInterceptedException e) {
            clickMoreButton();
        }
    }

    public void clickMoreButtonByVendorName(String vendorName) {
        String xpathExpression = String.format("//tr[.//td[@data-field='vendor-name']//div[normalize-space(.)='%s']]//div[contains(@id, 'dropdown-form')]", vendorName);           
        Locator dynamicMoreButton = new Locator("DYNAMIC_MORE_BUTTON", By.xpath(xpathExpression));
        driverTools.waitForElementToBeClickable(dynamicMoreButton);
        driverTools.click(dynamicMoreButton);
    }

    public Locator getVendorRowByVendorId(String vendorId) {
        return new Locator("VENDOR_ROW_BY_VENDOR_ID", By.xpath(String.format("//tr[.//td[@data-field='vendor-id'][normalize-space(.)='%s']]", vendorId)));
    }

    public void hoverToVendorRowByVendorId(String vendorId) {
        Locator vendorRow = getVendorRowByVendorId(vendorId);
        driverTools.waitForElementToAppear(vendorRow);
        driverTools.hoverToElement(vendorRow);
    }

    public String getSettlementTypeByVendorId(String vendorId) {
        Locator settlementTypeCell = new Locator("SETTLEMENT_TYPE_BY_VENDOR_ID", By.xpath(String.format("//tr[.//td[@data-field='vendor-id'][normalize-space(.)='%s']]//td[@data-field='settlement-type']", vendorId)));
        driverTools.waitForElementToAppear(settlementTypeCell);
        return driverTools.getText(settlementTypeCell).orElse("").trim();
    }

    public void clickMoreButtonByVendorId(String vendorId) {
        Locator dynamicMoreButton = new Locator("DYNAMIC_MORE_BUTTON_BY_VENDOR_ID", By.xpath(String.format("//tr[.//td[@data-field='vendor-id'][normalize-space(.)='%s']]//div[contains(@id, 'dropdown-form')]", vendorId)));
        driverTools.waitForElementToBeClickable(dynamicMoreButton);
        driverTools.click(dynamicMoreButton);
    }

    public void clickTransactionsButton() {
        driverTools.waitForElementToAppear(TRANSACTIONS_BUTTON);
        driverTools.scrollToElement(TRANSACTIONS_BUTTON);
        driverTools.click(TRANSACTIONS_BUTTON);
    }

    public void clickAddTransactionsButton() {
        driverTools.waitForElementToAppear(ADD_TRANSACTION_BUTTON);
        driverTools.click(ADD_TRANSACTION_BUTTON);
    }

    public void clickAddTransactionsButtonModal() {
        driverTools.waitForElementToAppear(ADD_TRANSACTION_BUTTON_MODAL);
        driverTools.click(ADD_TRANSACTION_BUTTON_MODAL);
    }

    public void listOfTransactionFromFile(String file, String vendorName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");


                TopUpDetail topUpDetail = new TopUpDetail(fields, entity, vendorId, vendorCurrency, settlementType, vendorName);
                logger.log("top up detail " + topUpDetail.subVendorName);
                topUpDetailList.add(topUpDetail);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public Boolean listOfTransactionFromCFD() {
        int temp = topUpDetailList.size() - 1;
        for (int i = 1; i <= topUpDetailList.size(); i++) {
            Locator locator = new Locator("locator", new By.ByXPath(String.format("//tr[@class='daily_verticalTable__8kG2i'][%d]", i)));
            driverTools.scrollIntoWebView(locator);
            List<WebElement> elements = driver.findElement(locator.by()).findElements(By.tagName("td"));
            List<String> detailsTrx = new ArrayList<>();
            for (WebElement td : elements) {
                detailsTrx.add(td.getText());
            }
            topUpDetail = new TopUpDetail(detailsTrx);
            TopUpDetail detail = topUpDetailList.get(temp--);
            Boolean checkEntity = detail.entity.equalsIgnoreCase(topUpDetail.entity);
            logger.log("This checkEntity: " + checkEntity + " This actual: " + topUpDetail.entity + " This expected: " + detail.entity);
            Boolean checkVendorName = detail.vendorName.equalsIgnoreCase(topUpDetail.vendorName);
            logger.log("This checkVendorName " + checkVendorName + " This actual: " + topUpDetail.vendorName + " This expected: " + detail.vendorName);
            Boolean checkSubVenName = detail.subVendorName.equalsIgnoreCase(topUpDetail.subVendorName);
            logger.log("This checkSubVenName " + checkSubVenName + " This actual: " + topUpDetail.subVendorName + " This expected: " + detail.subVendorName);
            Boolean checkSource = detail.source.equalsIgnoreCase(topUpDetail.source);
            logger.log("This checkSource " + checkSource + " This actual: " + topUpDetail.source + " This expected: " + detail.source);
            Boolean checkSettlementType = checkSettlementType();
            logger.log("This checkSettlementType " + checkSettlementType + " This actual: " + topUpDetail.settlementType + " This expected: " + detail.settlementType);
            Boolean checkVendorCurrency = detail.vendorCurrency.equalsIgnoreCase(topUpDetail.vendorCurrency);
            logger.log("This vendorCurrency " + checkVendorCurrency + " This actual: " + topUpDetail.vendorCurrency + " This expected: " + detail.vendorCurrency);
            int actualTrxAmount = (int) CoreFunctions.convertPriceSafely(topUpDetail.trxpAmount);
            Boolean checkTrxAmount = (Integer.parseInt(detail.trxpAmount) == actualTrxAmount);
            logger.log("This checkTrxAmount " + checkTrxAmount + " This actual: " + actualTrxAmount + " This expected: " + detail.trxpAmount);
            Boolean checkTypeOfAdj = detail.typeOfAdjustment.equalsIgnoreCase(topUpDetail.typeOfAdjustment);
            logger.log("This typeOfAdjustment " + checkTypeOfAdj + " This actual: " + topUpDetail.typeOfAdjustment + " This expected: " + detail.typeOfAdjustment);
            Boolean checkTypeOfTrx = detail.typeOfTransaction.equalsIgnoreCase(topUpDetail.typeOfTransaction);
            logger.log("This checkTypeOfTrx " + checkTypeOfTrx + " This actual: " + topUpDetail.typeOfTransaction + " This expected: " + detail.typeOfTransaction);
            Boolean checkOrderId = detail.orderId.equalsIgnoreCase(topUpDetail.orderId);
            logger.log("This checkOrderId " + checkOrderId + " This actual: " + topUpDetail.orderId + " This expected: " + detail.orderId);
            Boolean checkStartDepositBalance = checkStartDepositBalance();
            logger.log("This checkStartDepositBalance " + topUpDetail.startDepositBalance + " This boolean " + checkStartDepositBalance);
            Boolean checkEndDepositBalance = checkEndDepositBalance();
            logger.log("This checkEndDepositBalance " + topUpDetail.endDepositBalance + " This boolean " + checkEndDepositBalance);
            Boolean calculateDepositBalance = calculateDepositBalance((int) CoreFunctions.removeText(topUpDetail.trxpAmount));
            logger.log("This calculateDepositBalance " + topUpDetail.endDepositBalance + " This boolean " + calculateDepositBalance);
            boolean checkAll = (checkEntity && checkVendorName && checkSubVenName && checkSource && checkSettlementType && checkVendorCurrency
                    && checkTypeOfTrx && checkTypeOfAdj && checkOrderId && checkStartDepositBalance && checkEndDepositBalance
                    && calculateDepositBalance);
            if (!checkAll) {
                return false;
            }
        }
        return true;
    }

    public void clickTopUpButton() {
        driverTools.waitForElementToAppear(TOP_UP_BUTTON);
        driverTools.click(TOP_UP_BUTTON);
    }

    public void clickSubmitTopUpButton() {
        driverTools.waitForElementToAppear(SUBMIT_TOP_UP_BUTTON);
        driverTools.clickWithJavascriptExecutor(SUBMIT_TOP_UP_BUTTON);
    }

    public void clickSourceFilter() {
        driverTools.click(SOURCE_FILTER);
    }

    public void searchBySourceManual() {
        driverTools.click(MANUAL_SOURCE);
        driverTools.click(SAVE_BUTTON);
    }

    public boolean isTopUpSubmitted() {
        driverTools.waitForElementToAppear(SNACKBAR_SUCCESS_MESSAGE);
        return driverTools.isElementDisplayed(SNACKBAR_SUCCESS_MESSAGE);
    }

    public boolean isTopUpModalLoaded() {
        driverTools.waitForElementToAppear(TOP_UP_HEADING, 30);
        return driverTools.isElementDisplayed(TOP_UP_HEADING);
    }

    public boolean isDailyTabLoaded() {
        driverTools.waitForElementToAppear(DAILY_TAB, 30);
        return driverTools.isElementDisplayed(DAILY_TAB);
    }

    public void setVendorId() {
        driverTools.waitForElementToAppear(VENDOR_ID);
        vendorId = driver.findElement(VENDOR_ID.by()).getText();
    }

    public void setEntity() {
        driverTools.waitForElementToAppear(ENTITY);
        entity = driverTools.getText(ENTITY).orElse("");
    }

    public void setSettlementType() {
        driverTools.waitForElementToAppear(SETTLEMENT_TYPE);
        settlementType = driverTools.getText(SETTLEMENT_TYPE).orElse("");
    }

    public void setVendorCurrency() {
        driverTools.waitForElementToAppear(VENDOR_CURRENCY);
        vendorCurrency = driverTools.getText(VENDOR_CURRENCY).orElse("");
    }

    public void setTopUpAmountInput(String topUpAmount) {
        driverTools.waitForElementToAppear(TOP_UP_AMOUNT_INPUT);
        driverTools.scrollIntoWebView(TOP_UP_AMOUNT_INPUT);
        driverTools.clickWithJavascriptExecutor(TOP_UP_AMOUNT_INPUT);
        driverTools.sendKeys(TOP_UP_AMOUNT_INPUT, topUpAmount);
    }

    public void setDescriptionTextarea(String description) {
        driverTools.scrollIntoWebView(DESCRIPTION_TEXTAREA);
        driverTools.click(DESCRIPTION_TEXTAREA);
        driverTools.sendKeys(DESCRIPTION_TEXTAREA, description);
    }

    public void uploadTransferProof(List<String> files) {
        driverTools.scrollIntoWebView(UPLOAD_FILE_INPUT);
        for (String file : files) {
            File readyFile = new File(file);
            file = readyFile.getAbsolutePath();
            driverTools.sendKeys(UPLOAD_FILE_INPUT, file);
        }
    }

    public void uploadTopUpImage(String file) {
        Path path = Paths.get(file).toAbsolutePath();
        file = path.toString();
        driverTools.sendKeys(UPLOAD_FILE_INPUT, file);
    }

    public void setPaymentTimestampInput(String paymentTimestamp) {
        driverTools.click(PAYMENT_TIMESTAMP_INPUT);
        driverTools.clickWithJavascriptExecutor(getDateLocator(paymentTimestamp));
        driverTools.waitForElementToDisappear(PAYMENT_TIMESTAMP_INPUT, 1);
    }

    public Locator getDateLocator(String date) {

        String year = CoreFunctions.getSectionOfString(date, "/", 0);
        String month = CoreFunctions.getSectionOfString(date, "/", 1);
        String day = CoreFunctions.getSectionOfString(date, "/", 2);

        if (day.startsWith("0") && day.length() == 2) {
            day = day.substring(1);
        }

        String monthId = CoreFunctions.getMonthName(Integer.parseInt(month), "id");
        String monthEn = CoreFunctions.getMonthName(Integer.parseInt(month), "en");
        String locatorTextId = day + " " + monthId + " " + year;
        String locatorTextEng = day + " " + monthEn + " " + year;

        String currentDate = CoreFunctions.getCurrentDateDD_MM_YYYY();
        String dateConvert = CoreFunctions.convertDateFormat(date, "yyyy/MM/dd", "dd-MM-yyyy");
        if (currentDate.equals(dateConvert)) {
            isCurrentDate = true;
        }

        logger.log("date locator : " + locatorTextId);
        return new Locator(locatorTextId, AppiumBy.xpath("//*[contains(@aria-label,'" + locatorTextEng + "') or contains(@aria-label,'" + locatorTextId + "')]"));
    }

    public boolean calculateDepositBalance() {
        int startDepositBalance = (int) CoreFunctions.removeText((topUpDetail.startDepositBalance));
        int endDepositBalance = (int) CoreFunctions.removeText(topUpDetail.endDepositBalance);
        int topUpAmount = (int) CoreFunctions.removeText(topUpDetail.topUpAmount);
        return (startDepositBalance + topUpAmount == endDepositBalance);
    }

    public boolean calculateDepositBalance(int amount) {
        int startDepositBalance = (int) CoreFunctions.removeText((topUpDetail.startDepositBalance));
        int endDepositBalance = (int) CoreFunctions.removeText(topUpDetail.endDepositBalance);
        return (startDepositBalance + amount == endDepositBalance);
    }


    public void locateTheTopUp() {
        PAYMENT_DATE_LOCATOR = locatePaymentDate(dateTime);
        driverTools.scrollIntoWebView(PAYMENT_DATE_LOCATOR);
    }

    public Locator locatePaymentDate(String date) {
        return new Locator("PAYMENT_DATE", new By.ByXPath(String.format("//td[contains(text(), '%s')]", date)));
    }

    public void extractDetails() {
        WebElement tdEl = driver.findElement(PAYMENT_DATE_LOCATOR.by());
        WebElement trElement = (WebElement) ((JavascriptExecutor) driver).executeScript("return arguments[0].parentNode;", tdEl);

        // Find all td elements within the parent tr
        List<WebElement> tdElements = trElement.findElements(By.tagName("td"));

        List<String> detailsTopUp = new ArrayList<>();
        // Iterate through each td element and print its text
        for (WebElement td : tdElements) {
            if (td.getText().equalsIgnoreCase("View")) {
                PROOF_OF_TRANSFER = td.findElement(By.xpath("//button[@data-testid='view image']"));
            }
            detailsTopUp.add(td.getText());
        }
        logger.log("detailsTopUp:\n" + detailsTopUp);
        topUpDetail = new TopUpDetail(detailsTopUp);
    }


    public boolean checkEntity() {
        return topUpDetail.entity.equals(entity);
    }

    public boolean checkUID() {
        return !topUpDetail.uid.isEmpty();
    }

    public boolean checkVendorName(String vendorName) {
        logger.log("vendorName 1: " + vendorName);
        logger.log("vendorName 2: " + topUpDetail.vendorName);
        return topUpDetail.vendorName.equals(vendorName);
    }

    public boolean checkVendorID() {
        return topUpDetail.vendorId.equalsIgnoreCase(vendorId);

    }

    public boolean checkSource() {
        return topUpDetail.source.equals("Manual");
    }

    public boolean checkSettlementType() {
        return topUpDetail.settlementType.equals(settlementType);
    }

    public boolean checkVendorCurrency() {
        return topUpDetail.vendorCurrency.equals(vendorCurrency);
    }

    public boolean checkStartDepositBalance() {
        return !topUpDetail.startDepositBalance.isEmpty();
    }

    public boolean checkEndDepositBalance() {
        return !topUpDetail.endDepositBalance.isEmpty();
    }

    public boolean checkTopUpAmount(String topUpAmount) {
        int actual = (int) CoreFunctions.convertPriceSafely(topUpDetail.topUpAmount);
        int expected = Integer.parseInt(topUpAmount);
        return actual == expected;
    }

    public boolean checkTypeOfAdjustment() {
        return topUpDetail.typeOfAdjustment.equals("Addition");
    }

    public boolean checkTypeofTransaction() {
        return topUpDetail.typeOfTransaction.equals("Top Up");
    }

    public boolean checkDescription(String description) {
        return topUpDetail.description.equals(description);
    }

    public void clickBulkUploadOption() {
        driverTools.waitForElementToAppear(BULK_UPLOAD_OPTION);
        driverTools.clickWithJavascriptExecutor(BULK_UPLOAD_OPTION);
    }

    public void uploadBulkFile(String fileName) {
        WebElement input = driverTools.waitForElementToPresence(UPLOAD_FILE_INPUT).orElseThrow();
        String path = System.getProperty("user.dir") + "/src/test/resources/dweb/intools/";
        driverTools.sendKeys(input, path + fileName);
        String basePath = System.getProperty("user.dir") + "/src/test/resources/dweb/intools/";
        String effectivePath;
        if (fileName.startsWith(basePath) || fileName.startsWith("src/test/resources/dweb/intools/")) {
            effectivePath = fileName;
        } else {
            effectivePath = basePath + fileName;
        }
        driverTools.sendKeys(input, effectivePath);

    }

    public boolean isBulkUploadSubmitted() {
        driverTools.waitForElementToAppear(SNACKBAR_SUCCESS_MESSAGE_BULK_UPLOAD);
        return driverTools.isElementDisplayed(SNACKBAR_SUCCESS_MESSAGE_BULK_UPLOAD);
    }

    public void clickMonthlyTab() {
        driverTools.waitForElementToBeClickable(MONTHLY_TAB);
        driverTools.click(MONTHLY_TAB);
    }

    public MonthlyReportIntoolsDTO getMonthlyDataInDB(String softblockId, String startDate, String endDate) throws Exception {
        String sqlQuery = "SELECT " +
                "COALESCE(CAST(ROUND(SUM(IFNULL(json_extract(multi_order_details, '$[0].quantity'),0) + IFNULL(json_extract(multi_order_details, '$[1].quantity'),0)),2) AS DOUBLE), 0.0) AS Quantity, " +
                "COALESCE(CAST(ROUND(SUM(IFNULL(json_extract(multi_order_details, '$[0].publishedFare'),0) + IFNULL(json_extract(multi_order_details, '$[1].publishedFare'),0)),2) AS DOUBLE), 0.0) AS Revenue, " +
                "COALESCE(CAST(ROUND(SUM(IFNULL(json_extract(multi_order_details, '$[0].vatValue'),0) + IFNULL(json_extract(multi_order_details, '$[1].vatValue'),0)),2) AS DOUBLE), 0.0) AS VAT, " +
                "COALESCE(CAST(ROUND(SUM(IFNULL(json_extract(multi_order_details, '$[0].withholdingTaxValue'),0) + IFNULL(json_extract(multi_order_details, '$[1].withholdingTaxValue'),0)),2) AS DOUBLE), 0.0) AS WHT, " +
                "COALESCE(CAST(" +
                "  ROUND(SUM(IFNULL(json_extract(multi_order_details, '$[0].markupTotal'),0) + IFNULL(json_extract(multi_order_details, '$[1].markupTotal'),0)),2) + " +
                "  ROUND(SUM(IFNULL(json_extract(multi_order_details, '$[0].commissionTotal'),0) + IFNULL(json_extract(multi_order_details, '$[1].commissionTotal'),0)),2) + " +
                "  ROUND(SUM(IFNULL(json_extract(multi_order_details, '$[0].dynamicPricingRevenue'),0) + IFNULL(json_extract(multi_order_details, '$[1].dynamicPricingRevenue'),0)),2)" +
                " AS DOUBLE), 0.0) AS CommissionAndMarkup, " +
                "COALESCE(CAST(ROUND(SUM(IFNULL(json_extract(multi_order_details, '$[0].basicPrice'),0) + IFNULL(json_extract(multi_order_details, '$[1].basicPrice'),0)),2) AS DOUBLE), 0.0) AS VendorShare, " +
                "COALESCE(CAST(" +
                "  ROUND(SUM(IFNULL(gift_voucher,0)),2) + " +
                "  ROUND(SUM(IFNULL(promocode_value,0)),2) + " +
                "  ROUND(SUM(IFNULL(tiket_point,0)),2) + " +
                "  ROUND(SUM(IFNULL(json_extract(multi_order_details, '$[0].tiketSubsidyCampaignValue'),0) + IFNULL(json_extract(multi_order_details, '$[1].tiketSubsidyCampaignValue'),0)),2) + " +
                "  ROUND(SUM(IFNULL(json_extract(multi_order_details, '$[0].tiketSubsidyLoyaltyValue'),0) + IFNULL(json_extract(multi_order_details, '$[1].tiketSubsidyLoyaltyValue'),0)),2) + " +
                "  ROUND(SUM(IFNULL(json_extract(multi_order_details, '$[0].dynamicPricingCost'),0) + IFNULL(json_extract(multi_order_details, '$[1].dynamicPricingCost'),0)),2)" +
                " AS DOUBLE), 0.0) AS DirectCost, " +
                "COALESCE(CAST(ROUND(SUM(IFNULL(json_extract(vertical_specific, '$[0].profit'),0) + IFNULL(json_extract(vertical_specific, '$[1].profit'),0)),2) AS DOUBLE), 0.0) AS Profit " +
                "FROM tix_deposit.softblock_transaction " +
                "WHERE softblock_id = '" + softblockId + "' " +
                "AND order_date >= '" + startDate + " 17:00:00' " +
                "AND order_date < '" + endDate + " 17:00:00' " +
                "AND JSON_EXTRACT(vertical_specific, '$.flightType') = '\"international\"'";

        var db = TiketMysqlDBManager.getInstance("monthly");

        logger.log("Executing query: " + sqlQuery);

        var dbExecute = ResultSetMapperFactory.getResultSetMapperIdentity().map(
                db.executeQuery(sqlQuery), MonthlyReportIntoolsDTO.class);

        if (dbExecute == null || dbExecute.isEmpty()) {
            String msg = "Data not found in Database! softblockId=" + softblockId
                    + " startDate=" + startDate
                    + " endDate=" + endDate;
            logger.fail(msg);
            throw new Exception(msg);
        }

        MonthlyReportIntoolsDTO result = dbExecute.get(0);
        logger.log("Data found in Database : \n" + result.toString());
        logger.log("ini data quantity " + result.getQuantity());
        return result;
    }

    public JSONObject getMonthlyReportDetailApi(String date, String softblockId) throws InterruptedException, ExecutionException {
        MonthlyReportDetailApi.Inputs input = new MonthlyReportDetailApi.Inputs(date, softblockId);
        MonthlyReportDetailApi monthlyReportDetailApi = new MonthlyReportDetailApi(input);
        ApiResponse response = monthlyReportDetailApi.hitApi(apiTools);
        return new JSONObject(response.getBody());
    }

    public MonthlyReportDetail getMonthlyReportDetail(String date, String softblockId) throws JsonSyntaxException, InterruptedException, ExecutionException {
        Gson gson = new Gson();
        MonthlyReportDetail response = gson.fromJson(this.getMonthlyReportDetailApi(date, softblockId).toString(), MonthlyReportDetail.class);
        logger.log("Success getting the monthly report detail");
        logger.log("This is response data " + response.data.toString());
        return response;
    }

    public Boolean verifyEqual(Object actual, Object expected) {
        logger.log("This is actual value " + actual);
        logger.log("This is expected value " + expected);
        return Objects.equals(actual, expected);
    }

    public void selectMonthOption(String month) {
        driverTools.click(SELECT_MONTH_DROPDOWN);
        Locator locator = new Locator("locator", By.xpath(String.format("//label[contains(text(), '%s')]", month)));
        driverTools.scrollIntoWebView(locator);
        driverTools.waitForElementToBeClickable(locator, 5);
        try {
            driverTools.clickWithJavascriptExecutor(locator);
        } catch (ElementClickInterceptedException e) {
            selectMonthOption(month);
        }
    }

    public void clickDropdownSelectMonth() {
        WebElement dropdownSelectMonth = driverTools.waitForElementToAppear(SELECT_MONTH_DROPDOWN, 3).orElseThrow();
        driverTools.click(dropdownSelectMonth);
    }

    public void clickMonth(String month) {
        driverTools.click(getMonthByDropdown(month));
    }

    public void clickDropdownSelectYear() {
        WebElement dropdownSelectYear = driverTools.waitForElementToAppear(SELECT_YEAR_DROPDOWN, 3).orElseThrow();
        driverTools.click(dropdownSelectYear);
    }

    public void clickYear(String year) {
        driverTools.click(getYearByDropdown(year));
    }

    public void clickSummaryReportListTab() {
        driverTools.waitForElementToBeClickable(SUMMARY_REPORT_LIST_TAB);
        driverTools.click(SUMMARY_REPORT_LIST_TAB);
    }

    public void clickSummaryReportDetail() {
        driverTools.waitForElementToBeClickable(SUMMARY_REPORT_DETAIL);
        driverTools.click(SUMMARY_REPORT_DETAIL);
    }

    public Boolean verifyEqualString(String actual, String expected) {
        logger.log("This is actual value " + actual);
        logger.log("This is expected value " + expected);
        return Objects.equals(actual, expected);
    }

    public Boolean verifyContainString(String actual, String expected) {
        logger.log("This is actual value " + actual);
        logger.log("This is expected value " + expected);
        return expected.contains(actual);
    }

    public Boolean verifyEqualLong(Long actual, Long expected) {
        logger.log("This is actual value " + actual);
        logger.log("This is expected value " + expected);
        return Objects.equals(actual, expected);
    }

    public SummaryReportListIntoolsDTO getSummaryReportListDataInDB(String flightType, String startDate, String endDate) throws Exception {
        String sqlQuery = "SELECT /*+ MAX_EXECUTION_TIME(30000) */ " +
                "filtered.entity_id, " +
                "JSON_UNQUOTE(json_extract(filtered.multi_order_details, '$[0].vendorCurrency')) AS Currency, " +
                "COUNT(DISTINCT filtered.vendor_id) AS Total_Number_of_Vendors, " +
                "ROUND(SUM(IFNULL(json_extract(filtered.multi_order_details, '$[0].publishedFare'),0) + IFNULL(json_extract(filtered.multi_order_details, '$[1].publishedFare'), 0)), 2) AS Total_Revenue, " +
                "ROUND(SUM(IFNULL(json_extract(filtered.multi_order_details, '$[0].quantity'),0) + IFNULL(json_extract(filtered.multi_order_details, '$[1].quantity'), 0)), 2) AS Number_of_Orders, " +
                "ROUND(SUM(" +
                "  IFNULL(json_extract(filtered.multi_order_details, '$[0].commissionTotal'), 0) + IFNULL(json_extract(filtered.multi_order_details, '$[1].commissionTotal'), 0) + IFNULL(json_extract(filtered.multi_order_details, '$[2].commissionTotal'), 0) + " +
                "  IFNULL(json_extract(filtered.multi_order_details, '$[0].markupTotal'), 0) + IFNULL(json_extract(filtered.multi_order_details, '$[1].markupTotal'), 0) + IFNULL(json_extract(filtered.multi_order_details, '$[2].markupTotal'), 0) + " +
                "  IFNULL(json_extract(filtered.multi_order_details, '$[0].dynamicPricingRevenue'), 0) + IFNULL(json_extract(filtered.multi_order_details, '$[1].dynamicPricingRevenue'), 0) + IFNULL(json_extract(filtered.multi_order_details, '$[2].dynamicPricingRevenue'), 0)" +
                "), 2) AS Total_Margin, " +
                "ROUND(SUM(IFNULL(json_extract(filtered.vendor_price, '$[0]'),0) + IFNULL(json_extract(filtered.vendor_price, '$[1]'), 0) + IFNULL(json_extract(filtered.vendor_price, '$[2]'), 0)), 2) AS Base_Price, " +
                "CONVERT(ROUND(SUM(IFNULL(filtered.tiket_point,0)), 2), DOUBLE) AS Tiket_Point_Value, " +
                "CONVERT(ROUND(SUM(IFNULL(filtered.gift_voucher,0)), 2), DOUBLE) AS Gift_Voucher, " +
                "CONVERT(ROUND(SUM(IFNULL(filtered.promocode_value,0)), 2), DOUBLE) AS Promo_Value, " +
                "CONVERT(ROUND(SUM(IFNULL(filtered.convenience_fee,0)), 2), DOUBLE) AS Platform_Fee, " +
                "ROUND(SUM(" +
                "  IFNULL(filtered.tiket_point,0) + IFNULL(filtered.gift_voucher,0) + IFNULL(filtered.promocode_value,0) + " +
                "  IFNULL(json_extract(filtered.multi_order_details, '$[0].tiketSubsidyLoyaltyValue'), 0) + IFNULL(json_extract(filtered.multi_order_details, '$[1].tiketSubsidyLoyaltyValue'), 0) + IFNULL(json_extract(filtered.multi_order_details, '$[2].tiketSubsidyLoyaltyValue'), 0) + " +
                "  IFNULL(json_extract(filtered.multi_order_details, '$[0].tiketSubsidyCampaignValue'), 0) + IFNULL(json_extract(filtered.multi_order_details, '$[1].tiketSubsidyCampaignValue'), 0) + IFNULL(json_extract(filtered.multi_order_details, '$[2].tiketSubsidyCampaignValue'), 0) + " +
                "  IFNULL(json_extract(filtered.multi_order_details, '$[0].dynamicPricingCost'), 0) + IFNULL(json_extract(filtered.multi_order_details, '$[1].dynamicPricingCost'), 0) + IFNULL(json_extract(filtered.multi_order_details, '$[2].dynamicPricingCost'), 0)" +
                "), 2) AS Direct_Costs, " +
                "ROUND(SUM(IFNULL(json_extract(filtered.vertical_specific, '$[0].profit'), 0)), 2) AS Profit " +
                "FROM ( " +
                "  SELECT " +
                "    entity_id, vendor_id, multi_order_details, vendor_price, " +
                "    vertical_specific, tiket_point, gift_voucher, promocode_value, convenience_fee " +
                "  FROM tix_deposit.softblock_transaction " +
                "  WHERE vertical = 'flight' " +
                "  AND order_date >= '" + startDate + " 17:00:00' " +
                "  AND order_date < '" + endDate + " 17:00:00' " +
                ") AS filtered " +
                "WHERE JSON_UNQUOTE(JSON_EXTRACT(filtered.vertical_specific, '$.flightType')) = '" + flightType + "' " +
                "AND JSON_UNQUOTE(json_extract(filtered.multi_order_details, '$[0].source')) = 'Automated' " +
                "GROUP BY filtered.entity_id, Currency " +
                "ORDER BY filtered.entity_id, Currency";

        var db = TiketMysqlDBManager.getInstance("monthly");

        List<SummaryReportListIntoolsDTO> dbExecute;
        try {
            dbExecute = ResultSetMapperFactory.getResultSetMapperIdentity().map(
                    db.executeQuery(sqlQuery), SummaryReportListIntoolsDTO.class);
        } catch (SQLException e) {
            String msg = "Database query failed: " + e.getMessage();
            logger.fail(msg);
            throw new Exception(msg, e);
        }

        if (dbExecute == null || dbExecute.isEmpty()) {
            String msg = "Data not found in Database!";
            logger.fail(msg);
            throw new Exception(msg);
        }

        SummaryReportListIntoolsDTO result = dbExecute.get(0);
        logger.log("Data found in Database : \n" + result.toString());
        logger.log("Entity is " + result.getEntity_id());
        return result;
    }

    public JSONObject getSummaryReportListApi(String flightType, String year, String months) throws InterruptedException, ExecutionException {
        SummaryReportListApi.Inputs input = new SummaryReportListApi.Inputs(flightType, year, months);
        SummaryReportListApi SummaryReportListApi = new SummaryReportListApi(input);
        ApiResponse response = SummaryReportListApi.hitApi(apiTools);
        return new JSONObject(response.getBody());
    }

    public SummaryReportList getSummaryReportList(String flightType, String year, String months) throws JsonSyntaxException, InterruptedException, ExecutionException {
        Gson gson = new Gson();
        SummaryReportList response = gson.fromJson(this.getSummaryReportListApi(flightType, year, months).toString(), SummaryReportList.class);
        logger.log("Success getting the Summary Report List");
        logger.log("This is response data " + response.data.toString());
        return response;
    }

    public Map<String, Object> getEntriesSummaryReportList() {
        Map<String, Object> extractedValues = new HashMap<>();

        Locator entityLoc = new Locator("entityLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[1]/td[2]"));
        extractedValues.put("Entity", driverTools.getText(entityLoc).get());

        Locator currencyListLoc = new Locator("currency_list", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[1]/td[3]"));
        extractedValues.put("Currency", driverTools.getText(currencyListLoc).get());

        Locator totalNumberOfVendorsLoc = new Locator("totalNumberOfVendorsLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[1]/td[4]"));
        extractedValues.put("Total Number Of Vendors", driverTools.getText(totalNumberOfVendorsLoc).get());

        Locator totalRevenueLoc = new Locator("totalRevenueLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[1]/td[5]"));
        extractedValues.put("Total Revenue", driverTools.getText(totalRevenueLoc).get());

        Locator numberOfOrdersLoc = new Locator("numberOfOrdersLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[1]/td[6]"));
        extractedValues.put("Number of Orders", driverTools.getText(numberOfOrdersLoc).get());

        Locator totalMarginLoc = new Locator("totalMarginLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[1]/td[7]"));
        extractedValues.put("Total Margin", driverTools.getText(totalMarginLoc).get());

        Locator basePriceLoc = new Locator("basePriceLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[1]/td[8]"));
        extractedValues.put("Base Price", driverTools.getText(basePriceLoc).get());

        Locator tiketPointLoc = new Locator("tiketPointLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[1]/td[9]"));
        extractedValues.put("Tiket Point Value", driverTools.getText(tiketPointLoc).get());

        Locator giftVoucherLoc = new Locator("giftVoucherLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[1]/td[10]"));
        extractedValues.put("Gift Voucher", driverTools.getText(giftVoucherLoc).get());

        Locator promoValueLoc = new Locator("promoValueLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[1]/td[11]"));
        extractedValues.put("Promo Value", driverTools.getText(promoValueLoc).get());

        Locator platformFeeLoc = new Locator("platformFeeLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[1]/td[12]"));
        extractedValues.put("Platform Fee", driverTools.getText(platformFeeLoc).get());

        Locator directCostLoc = new Locator("directCostLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[1]/td[13]"));
        extractedValues.put("Direct Cost", driverTools.getText(directCostLoc).get());

        Locator profitLoc = new Locator("profitLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[1]/td[14]"));
        extractedValues.put("Profit", driverTools.getText(profitLoc).get());

        return extractedValues;
    }

    public Map<String, Object> getEntriesSummaryReportDetail() {
        Map<String, Object> extractedValues = new HashMap<>();

        Locator vendorNameLoc = new Locator("vendorNameLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[3]/td[2]"));
        extractedValues.put("Vendor Name", driverTools.getText(vendorNameLoc).get());

        Locator currencyDetailLoc = new Locator("currencyDetailLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[3]/td[3]"));
        extractedValues.put("Currency", driverTools.getText(currencyDetailLoc).get());

        Locator totalQuantitySoldLoc = new Locator("totalQuantitySoldLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[3]/td[4]"));
        extractedValues.put("Total Quantity Sold", driverTools.getText(totalQuantitySoldLoc).get());

        Locator revenueFromOrdersLoc = new Locator("revenueFromOrdersLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[3]/td[5]"));
        extractedValues.put("Revenue From Orders", driverTools.getText(revenueFromOrdersLoc).get());

        Locator additionalMarginLoc = new Locator("additionalMarginLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[3]/td[6]"));
        extractedValues.put("Additional Margin", driverTools.getText(additionalMarginLoc).get());

        Locator additionalCommissionLoc = new Locator("additionalCommissionLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[3]/td[7]"));
        extractedValues.put("Additional Commission", driverTools.getText(additionalCommissionLoc).get());

        Locator vatLoc = new Locator("vatLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[3]/td[8]"));
        extractedValues.put("Total Value Added Tax (VAT)", driverTools.getText(vatLoc).get());

        Locator whtLoc = new Locator("whtLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[3]/td[9]"));
        extractedValues.put("Total Withholding Tax", driverTools.getText(whtLoc).get());

        Locator topupVendorLoc = new Locator("topupVendorLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[3]/td[10]"));
        extractedValues.put("Top Up Vendor", driverTools.getText(topupVendorLoc).get());

        Locator totalCommissionLoc = new Locator("totalCommissionLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[3]/td[11]"));
        extractedValues.put("Total Commission", driverTools.getText(totalCommissionLoc).get());

        Locator totalVendorShareLoc = new Locator("totalVendorShareLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[3]/td[12]"));
        extractedValues.put("Total Vendor Share", driverTools.getText(totalVendorShareLoc).get());

        Locator directCostDetailLoc = new Locator("directCostDetailLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[3]/td[13]"));
        extractedValues.put("Direct Cost", driverTools.getText(directCostDetailLoc).get());

        Locator profitDetailLoc = new Locator("profitDetailLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[3]/td[14]"));
        extractedValues.put("Profit", driverTools.getText(profitDetailLoc).get());

        Locator settlementTypeLoc = new Locator("settlementTypeLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[3]/td[15]"));
        extractedValues.put("Settlement Type", driverTools.getText(settlementTypeLoc).get());

        Locator currentDepositBalanceLoc = new Locator("currentDepositBalanceLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[3]/td[16]"));
        extractedValues.put("Current Deposit Balance", driverTools.getText(currentDepositBalanceLoc).get());

        Locator startDepositBalanceLoc = new Locator("startDepositBalanceLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[3]/td[17]"));
        extractedValues.put("Start Deposit Balance", driverTools.getText(startDepositBalanceLoc).get());

        Locator endDepositBalanceLoc = new Locator("endDepositBalanceLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[3]/td[18]"));
        extractedValues.put("End Deposit Balance", driverTools.getText(endDepositBalanceLoc).get());

        return extractedValues;
    }

    public SummaryReportDetailIntoolsDTO getSummaryReportDetailDataInDB(String flightType, String startDate, String endDate, String entity2, String vendorCurrency, String dateMonth) throws Exception {
        String sqlQuery = "SELECT \n" +
                "vendor_name as Vendor_Name,\n" +
                "JSON_UNQUOTE(json_extract(a.multi_order_details, '$[0].vendorCurrency')) as Currency,\n" +
                "json_extract(result, '$[0].totalQuantitySold') as Total_Quantity_Sold,\n" +
                "ROUND(json_extract(result, '$[0].revenueFromOrders'),2) as Revenue_From_Orders,\n" +
                "ROUND(json_extract(result, '$[0].additionalMargin'),2) as Additional_Margin,\n" +
                "ROUND(json_extract(result, '$[0].additionalCommission'),2) as Additional_Commission,\n" +
                "ROUND(json_extract(result, '$[0].totalVat'),2) as VAT,\n" +
                "ROUND(json_extract(result, '$[0].totalWht'),2) as WHT,\n" +
                "ROUND(json_extract(result, '$[0].topUpAmountFromVendor'),2) as Top_Up_Amount_From_Vendor,\n" +
                "ROUND(json_extract(result, '$[0].totalCommissionAndMarkUp'),2) as Total_Commission_Earned_For_The_Month,\n" +
                "ROUND(json_extract(result, '$[0].totalVendorShare'),2) as Vendor_Share,\n" +
                "ROUND(json_extract(result, '$[0].directCosts'),2) as Direct_Costs,\n" +
                "ROUND(json_extract(result, '$[0].profit'),2) as Profit,\n" +
                "b.settlement_type as Settlement_Type,\n" +
                "ROUND(json_extract(result, '$[0].currentDepositBalance'),2) as Current_Deposit_Balance,\n" +
                "ROUND(json_extract(result, '$[0].startDepositBalance'),2) as Start_Deposit_Balance,\n" +
                "ROUND(json_extract(result, '$[0].endDepositBalance'),2) as End_Deposit_Balance\n" +
                "FROM tix_deposit.softblock_transaction a\n" +
                "LEFT JOIN tix_deposit.softblock_deposit b on b.id = a.softblock_id\n" +
                "LEFT JOIN tix_deposit.monthly_report c on c.softblock_id = b.id\n" +
                "WHERE a.vertical = 'flight' AND order_date >= '" + startDate + " 17:00:00' AND order_date < '" + endDate + " 17:00:00' AND JSON_EXTRACT(vertical_specific, '$.flightType') ='" + flightType + "' AND entity_id ='" + entity2 + "' AND b.vendor_currency ='" + vendorCurrency + "' AND json_extract(multi_order_details , '$[0].source') = 'Automated' AND c.date ='" + dateMonth + "' GROUP BY b.vendor_id\n" +
                "ORDER BY vendor_name";
        var db = TiketMysqlDBManager.getInstance("monthly");
        var dbExecute = ResultSetMapperFactory.getResultSetMapperIdentity().map(db.executeQuery(sqlQuery),
                SummaryReportDetailIntoolsDTO.class);
        SummaryReportDetailIntoolsDTO result;
        if (dbExecute.stream().count() < 1) {
            String msg = "Data not found in Database!";
            logger.fail(msg);
            throw new Exception(msg);
        } else {
            result = dbExecute.get(2);
        }
        logger.log("Data found in Database : \n" + result.toString());
        logger.log("Vendor Name is " + result.getVendor_Name());
        return result;
    }

    public JSONObject getSummaryReportDetailApi(String flightType, String entity2, String vendorCurrency, String year, String months, String page, String limit) throws InterruptedException, ExecutionException {
        SummaryReportDetailApi.Inputs input = new SummaryReportDetailApi.Inputs(flightType, entity2, vendorCurrency, year, months, page, limit);
        SummaryReportDetailApi SummaryReportDetailApi = new SummaryReportDetailApi(input);
        ApiResponse response = SummaryReportDetailApi.hitApi(apiTools);
        return new JSONObject(response.getBody());
    }

    public SummaryReportDetail getSummaryReportDetail(String flightType, String entity2, String vendorCurrency, String year, String months, String page, String limit) throws JsonSyntaxException, InterruptedException, ExecutionException {
        Gson gson = new Gson();
        SummaryReportDetail response = gson.fromJson(this.getSummaryReportDetailApi(flightType, entity2, vendorCurrency, year, months, page, limit).toString(), SummaryReportDetail.class);
        logger.log("Success getting the Summary Report List");
        logger.log("This is response data " + response.data.toString());
        return response;
    }

    public void selectYearOption(String year) {
        driverTools.waitForElementToBeClickable(SELECT_YEAR_DROPDOWN);
        driverTools.click(SELECT_YEAR_DROPDOWN);
        Locator locator = new Locator("locator", By.xpath(String.format("//label[contains(text(), '2025')]", year)));
        driverTools.scrollIntoWebView(locator);
        driverTools.waitForElementToBeClickable(locator, 5);
        try {
            driverTools.clickWithJavascriptExecutor(locator);
        } catch (ElementClickInterceptedException e) {
            selectYearOption(year);
        }
    }

    private Map<String, String> getOrderIdAndAmount() throws ExecutionException, InterruptedException {
        Map<String, String> orderIdAndAmount = new HashMap<>();
        SearchMutationApi searchMutationApi = new SearchMutationApi();
        ApiResponse response = searchMutationApi.hitApi(apiTools);

        JSONObject responseJson = new JSONObject(response.getBody());
        JSONArray data = null;

        try {
            data = responseJson.getJSONArray("data");
        } catch (Exception e) {
            Assertion.skip("Couldn't find data in search mutation API");
        }

        assert data != null;
        JSONObject firstData = data.getJSONObject(0);
        orderIdAndAmount.put("orderId", String.valueOf(firstData.getInt("orderId")));
        orderIdAndAmount.put("amount", String.valueOf(firstData.getInt("amount")));

        logger.log("Success getting data.\n" + "orderId: "
                + orderIdAndAmount.get("orderId")
                + "\namount: " + orderIdAndAmount.get("amount"));

        return orderIdAndAmount;
    }

    public void createUploadedFile(String originFilePath, String updatedFilePath) throws IOException, ExecutionException, InterruptedException {
        Map<String, String> orderIdAndAmount = getOrderIdAndAmount();
        String orderId = orderIdAndAmount.get("orderId");
        String amount = orderIdAndAmount.get("amount");

        try (BufferedReader reader = new BufferedReader(new FileReader(originFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(updatedFilePath))) {

            String value;
            while ((value = reader.readLine()) != null) {
                value = value.replace("[ORDER_ID]", orderId);
                value = value.replace("[AMOUNT]", amount);
                writer.write(value);
                writer.newLine();
            }

            logger.log("Success Write CSV to " + updatedFilePath);
        } catch (Exception e) {
            Assertion.skip("Failed updating CSV file.");
        }
    }

    public void showUpdatedCsvFile(String path) {
        try (BufferedReader read = new BufferedReader(new FileReader(path))) {
            String value;
            while ((value = read.readLine()) != null) {
                logger.log(value);
                System.out.println("Value csv ini:\n" + value);
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToPaymentMenu() {
        driverTools.click(PAYMENT_MENU);
    }

    public String randomOrderIDGenerator() {
        String orderID = String.valueOf(CoreFunctions.randomInt(10));
        logger.log("Generated order ID: " + orderID);
        return orderID;
    }

    public void clickB2BAffiliateDropdownMenu() {
        driverTools.click(B2B_AFFILIATE_DROPDOWN_MENU);
    }

    public void goToB2BAResellerMenu() {
        driverTools.click(B2B_RESELLER_MENU);
    }

    public void clickRequestHistoryTab() {
        driverTools.waitForElementToBeClickable(REQUEST_HISTORY_TAB);
        driverTools.click(REQUEST_HISTORY_TAB);
    }

    public void goToB2BDailyReport(int configId) {
        String B2B_DAILY_PAGE_URL = "https://gatotkaca.tiket.com/admin/intools/b2b-affiliate/reseller/" + configId + "/transactions/daily?reportType=sales";
        driverTools.get(B2B_DAILY_PAGE_URL);
    }

    public void searchResellerName(String resellerName) {
        driverTools.click(RESELLER_NAME_DROPDOWN);
        Locator RESELLER_NAME_OPTION = getVendorNameOption(resellerName);
        driverTools.click(RESELLER_NAME_OPTION);
        driverTools.click(SAVE_BUTTON);
    }

    public B2BResellerDetail getB2BResellerDetail(String resellerName) throws JsonSyntaxException, InterruptedException, ExecutionException {
        Gson gson = new Gson();
        B2BResellerDetail response = gson.fromJson(this.getB2BResellerDetailApi(resellerName).toString(), B2BResellerDetail.class);
        logger.log("Success getting the b2b reseller detail");
        logger.log("This is response data " + response.data.toString());
        return response;
    }

    public JSONObject getB2BResellerDetailApi(String resellerName) throws InterruptedException, ExecutionException {
        B2BResellerDetailApi.Inputs input = new B2BResellerDetailApi.Inputs(resellerName);
        B2BResellerDetailApi b2BResellerDetailApi = new B2BResellerDetailApi(input);
        ApiResponse response = b2BResellerDetailApi.hitApi(apiTools);
        return new JSONObject(response.getBody());
    }

    public B2BDailyDetail getB2BDailyDetail(String resellerName, String searchDateBy) throws JsonSyntaxException, InterruptedException, ExecutionException {
        Gson gson = new Gson();
        B2BDailyDetail response = gson.fromJson(this.getB2BDailyDetailApi(resellerName, searchDateBy).toString(), B2BDailyDetail.class);
        logger.log("Success getting the b2b daily detail");
        logger.log("This is response data " + response.data.toString());
        return response;
    }

    public JSONObject getB2BDailyDetailApi(String resellerName, String searchDateBy) throws InterruptedException, ExecutionException {
        B2BDailyDetailApi.Inputs input = new B2BDailyDetailApi.Inputs(resellerName, searchDateBy);
        B2BDailyDetailApi b2BDailyDetailApi = new B2BDailyDetailApi(input);
        ApiResponse response = b2BDailyDetailApi.hitApi(apiTools);
        return new JSONObject(response.getBody());
    }

    public B2BDailyDetail getB2BDailyDetailGateway(String searchDateBy, String startDate, String endDate, String resellerId,
                                                  String orderId) throws JsonSyntaxException, InterruptedException, ExecutionException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(int.class, (JsonDeserializer<Integer>) (json, type, context) -> (int) Math.round(json.getAsDouble()))
                .create();
        B2BDailyDetail response = gson.fromJson(this.getB2BDailyDetailGatewayApi(searchDateBy, startDate, endDate, resellerId, orderId).toString(), B2BDailyDetail.class);
        return response;
    }

    public JSONObject getB2BDailyDetailGatewayApi(String searchDateBy, String startDate, String endDate, String resellerId,
                                                String orderId) throws InterruptedException, ExecutionException {
        String accessToken = driverTools.getCookieValue("session_access_token");
        B2BDailyDetailGatewayApi.Inputs input = new B2BDailyDetailGatewayApi.Inputs(
                searchDateBy, startDate, endDate, resellerId, orderId, accessToken, 1, 10, "-created_at", "order_id");
        B2BDailyDetailGatewayApi b2BDailyDetailGatewayApi = new B2BDailyDetailGatewayApi(input);
        ApiResponse response = b2BDailyDetailGatewayApi.hitApi(apiTools);
        return new JSONObject(response.getBody());
    }

    public B2CDailyDetail getB2CDailyDetail(String vertical, String softblockId, String searchDateBy) throws JsonSyntaxException, InterruptedException, ExecutionException {
        Gson gson = new Gson();
        B2CDailyDetail response = gson.fromJson(this.getB2CDailyDetailApi(vertical, softblockId, searchDateBy).toString(), B2CDailyDetail.class);
        logger.log("Success getting the b2c daily report");
        logger.log("This is response daily report" + response.data.toString());
        return response;
    }

    public JSONObject getB2CDailyDetailApi(String vertical, String softblockId, String searchDateBy) throws InterruptedException, ExecutionException {
        B2CDailyDetailApi.Inputs input = new B2CDailyDetailApi.Inputs(vertical, softblockId, searchDateBy);
        B2CDailyDetailApi b2CDailyDetailApi = new B2CDailyDetailApi(input);
        ApiResponse response = b2CDailyDetailApi.hitApi(apiTools);
        return new JSONObject(response.getBody());
    }

    public void setBankNameInput(String bankName) {
        driverTools.waitForElementToAppear(BANK_NAME_INPUT);
        driverTools.click(BANK_NAME_INPUT);
        Locator locator = new Locator("locator", By.xpath(String.format("//label[contains(text(), '%s')]", bankName)));
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);
    }

    public void setTransactionIdInput(String trxId) {
        driverTools.clickWithJavascriptExecutor(TRANSACTION_ID_INPUT);
        driverTools.sendKeys(TRANSACTION_ID_INPUT, trxId);
    }

    public void clickSubmitTopUpButtonB2B() {
        driverTools.click(SUBMIT_TOP_UP_BUTTON);
    }

    public void clickRequestDropdownMenu() {
        driverTools.click(REQUEST_DROPDOWN_MENU);
    }

    public void clickResellerMenu() {
        driverTools.click(REQUEST_RESELLER_MENU);
    }

    public Map<String, Object> getEntriesRequestHistory() {
        Map<String, Object> extractedValues = new HashMap<>();

        Locator approvedByLoc = new Locator("approvedByLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[1]/td[9]"));
        extractedValues.put("Approved By", driverTools.getText(approvedByLoc).get());

        Locator remarksLoc = new Locator("remarksLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[1]/td[15]"));
        extractedValues.put("Remarks", driverTools.getText(remarksLoc).get());

        Locator statusLoc = new Locator("statusLoc", By.xpath("(//tbody[@data-testid='table:list-body']/tr)[1]/td[16]"));
        extractedValues.put("Status", driverTools.getText(statusLoc).get());

        return extractedValues;
    }

    public Boolean verifyContains(String actual, String expected) {
        logger.log("This is actual value " + actual);
        logger.log("This is expected value " + expected);
        return expected.contains(actual);
    }

    public void clickApproveButton() {
        driverTools.waitForElementToBeClickable(APPROVE_B2B_PENDING_REQUEST);
        driverTools.click(APPROVE_B2B_PENDING_REQUEST);
    }

    public void setRemarkInput(String remarks) {
        driverTools.click(REMARKS_INPUT);
        driverTools.sendKeys(REMARKS_INPUT, remarks);
    }

    public void clickApproveButtonModal() {
        driverTools.waitForElementToBeClickable(APPROVE_BUTTON_MODAL);
        driverTools.click(APPROVE_BUTTON_MODAL);
    }

    public Boolean calculateBySettlementType(String settlementType, String type, double amount, double startDepositBalance, double endDepositBalance,
                                             double startCreditLineBalance, double endCreditLineBalance, double creditLineUtil) {
        if (settlementType.equalsIgnoreCase("ADVANCE_DEPOSIT")) {
            logger.log("Start Deposit Balance: " + startDepositBalance);
            logger.log("Top Up Amount: " + amount);
            logger.log("End Deposit Balance: " + endDepositBalance);
            return (startDepositBalance + amount) == endDepositBalance;
        } else if (settlementType.equalsIgnoreCase("ADVANCE_DEPOSIT_CREDIT_LINE")) {
            if (type.equalsIgnoreCase("Deduction")) {
                if (startDepositBalance != 0) {
                    logger.log("This is startDeposit " + startDepositBalance);
                    logger.log("This is endDeposit " + endDepositBalance);
                    logger.log("This is endCreditLine " + endCreditLineBalance);
                    return ((startDepositBalance - amount) == endDepositBalance) && (startCreditLineBalance == endCreditLineBalance);
                } else {
                    return (startCreditLineBalance - amount == endCreditLineBalance) && (creditLineUtil != 0);
                }
            } else {
                logger.log("This is startDeposit " + startDepositBalance);
                logger.log("This is endDeposit " + endDepositBalance);
                logger.log("This is endCreditLine " + endCreditLineBalance);
                logger.log("This is topUpAmount " + amount);
                return (startDepositBalance + amount) == endDepositBalance;
            }
        }
        return false;
    }

    public double calculateAddition(String settlementType, double topUpAmount, double startDepositBalance) {
        if (settlementType.equalsIgnoreCase("ADVANCE_DEPOSIT")) {
            logger.log("Start Deposit Balance: " + startDepositBalance);
            logger.log("Deposit Deduction Amount: " + topUpAmount);
            double endDepositBalance = startDepositBalance + topUpAmount;
            BigDecimal roundedEndDepositBalance = BigDecimal.valueOf(endDepositBalance).setScale(2, RoundingMode.HALF_UP);
            logger.log("End Deposit Balance (Rounded): " + roundedEndDepositBalance);
            return roundedEndDepositBalance.doubleValue();
        }
        return 0.0;
    }

    public void clickDownloadCSVinSummaryDetail() {
        WebElement exportCSV = driverTools.waitForElementToAppear(DOWNLOAD_CSV_SUMMARY_REPORT_DETAIL).orElseThrow();
        driverTools.click(exportCSV);
    }

    public boolean isSummaryDetailDownloaded() {
        driverTools.waitForElementToAppear(SNACKBAR_DOWNLOAD_CSV_SUMMARY_REPORT_DETAIL_SUCCESS);
        return driverTools.isElementDisplayed(SNACKBAR_DOWNLOAD_CSV_SUMMARY_REPORT_DETAIL_SUCCESS);
    }

    public boolean verifyDownloadedSummaryDetailCSV(String fileNameSummaryDetail, String timeOutMiliSec) {
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        logger.log("Download folder: " + downloadDir);
        logger.log("expected filename: " + fileNameSummaryDetail);
        boolean fileDownloaded = false;

        long startTime = Instant.now().toEpochMilli();
        logger.log("Start time: " + startTime);
        long waitTime = startTime + Integer.parseInt(timeOutMiliSec);
        logger.log("Wait time: " + waitTime);

        while (Instant.now().toEpochMilli() < waitTime) {
            File[] listFiles = new File(downloadDir).listFiles();
            for (File file : listFiles) {
                if (file.getName().equals(fileNameSummaryDetail)) {
                    String[] listFilesName = new File(downloadDir).list(); //logging purpose
                    for (int i = 0; i < listFilesName.length; i++) {
                        logger.log("File name on dir: " + listFilesName[i]);
                    }
                    fileDownloaded = true;
                    file.delete();
                    break;
                }
            }
            if (fileDownloaded) break;
        }

        return fileDownloaded;
    }

    public void clickPaymentMethodDropdown() {
        driverTools.waitForElementToAppear(PAYMENT_METHOD_DROPDOWN);
        driverTools.waitForElementToBeClickable(PAYMENT_METHOD_DROPDOWN);
        driverTools.click(PAYMENT_METHOD_DROPDOWN);
    }

    public void choosePaymentMethod(String paymentMethod) {
        Locator locator = getPaymentMethodLocator(paymentMethod);
        driverTools.waitForElementToBeClickable(locator);
        driverTools.click(locator);
    }

    public Locator getPaymentMethodLocator(String paymentMethod) {
        return new Locator("locator", By.xpath(String.format("//label[contains(text(), '%s')]", paymentMethod)));
    }

    public void uploadCsvFile(String file) {
        Path path = Paths.get(file).toAbsolutePath();
        file = path.toString();
        driverTools.sendKeys(CSV_FILE_INPUT, file);
    }

    public void clickUploadButton() {
        driverTools.click(UPLOAD_BUTTON);
    }

    public Optional<List<WebElement>> valuesLocator() {
        return driverTools.elements(new Locator("locator", By.tagName("td")));
    }

    public int getPreviewAmount(List<WebElement> cells) {
        return (int) CoreFunctions.convertPriceSafely(cells.stream()
                .map(WebElement::getText)
                .filter(text -> text.trim().matches("[\\d,.]+\\.\\d{2}"))
                .findFirst()
                .orElse("0"));
    }

    public String[] valuesBankStatementFromFile(String file) {
        String[] values = new String[0];
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                values = line.split(",");
                logger.log("values " + Arrays.toString(values));
            }
            logger.log("value size: " + values.length);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return values;
    }

    public void clickConfirmButton() {
        driverTools.scrollToElement(CONFIRM_BUTTON);
        driverTools.click(CONFIRM_BUTTON);
    }

    public void clickConfirmButtonModal() {
        driverTools.click(CONFIRM_BUTTON_MODAL);
    }

    public void clickOkButton() {
        driverTools.waitForElementToAppear(OK_UPLOADED_BUTTON);
        driverTools.click(OK_UPLOADED_BUTTON);
    }

    public void clickDetailsButton() {

        driverTools.waitForElementToAppear(DETAILS_BUTTON, 10);
        driverTools.waitForElementToBeClickable(DETAILS_BUTTON, 25);
        driverTools.clickWithJavascriptExecutor(DETAILS_BUTTON);
    }

    public Map<String, String> getValuesDetails() {
        Map<String, String> extractedValues = new HashMap<>();
        driverTools.waitForElementToAppear(uploadDateLoc);
        extractedValues.put("Upload Date", driverTools.getText(uploadDateLoc).get());

        driverTools.waitForElementToAppear(uploadByLoc);
        extractedValues.put("Upload By", driverTools.getText(uploadByLoc).get());

        driverTools.waitForElementToAppear(uploadStatusLoc);
        extractedValues.put("Upload Status", driverTools.getText(uploadStatusLoc).get());
         return extractedValues;
    }

    public Boolean checkDate(String actualDate, String expectedDate) {
        String year = CoreFunctions.getSectionOfString(actualDate, "/", 0);
        String month = CoreFunctions.getSectionOfString(actualDate, "/", 1);
        String day = CoreFunctions.getSectionOfString(actualDate, "/", 2);
        String monthEn = CoreFunctions.getMonthName(Integer.parseInt(month), "en");
        String monthEnShort = CoreFunctions.monthLongToShortEn(monthEn);
        logger.log("This is actual value " + actualDate);
        logger.log("This is expected value " + expectedDate);
        return expectedDate.contains(day) && expectedDate.contains(monthEnShort) && expectedDate.contains(year);
    }

    public void clickCloseButtonModal() {
        driverTools.waitForElementToAppear(CLOSE_BUTTON_MODAL);
        driverTools.click(CLOSE_BUTTON_MODAL);
    }

    public void closePopupIfPresent() {
        driverTools.waitForElementToAppear(CLOSE_BUTTON_MODAL, 2);
        if (driverTools.isElementDisplayed(CLOSE_BUTTON_MODAL)) {
            driverTools.click(CLOSE_BUTTON_MODAL);
        }
    }

    public void goToBankPaymentReconMenu() {
        driverTools.click(BANK_PAYMENT_RECON_MENU);
    }

    public void setOrderIDInvoiceInput(String orderId) {
        driverTools.click(ORDER_ID_INVOICE_INPUT);
        driverTools.waitForElementToAppear(ORDER_ID_INVOICE_OPTION);
        driverTools.click(ORDER_ID_INVOICE_OPTION);
        driverTools.waitForElementToAppear(ORDER_ID_VALUE_INPUT);
        driverTools.clearInput(ORDER_ID_VALUE_INPUT);
        driverTools.sendKeys(ORDER_ID_VALUE_INPUT, orderId);
        }

    public void setOrderIdSettlementInput(String orderId){
        driverTools.clearInput(ORDER_ID_SETTLEMENT_INPUT);
        driverTools.click(ORDER_ID_SETTLEMENT_INPUT);
        driverTools.sendKeys(ORDER_ID_SETTLEMENT_INPUT, orderId);
    }

    public void clickSearchInvoiceButton() {
        driverTools.waitForElementToAppear(SEARCH_INVOICE_BUTTON);
        driverTools.click(SEARCH_INVOICE_BUTTON);
    }

    public void searchSettlementButton() {
        driverTools.waitForElementToAppear(SEARCH_ORDER_ID_INPUT);
        driverTools.click(SEARCH_ORDER_ID_INPUT);
    }

    public Map<String, String> getValuesInvoice() {
        Map<String, String> extractedValues = new HashMap<>();

        driverTools.waitForElementToAppear(amountLoc);
        extractedValues.put("Amount", driverTools.getText(amountLoc).get());

        driverTools.waitForElementToAppear(reconLoc);
        extractedValues.put("Recon", driverTools.isElementDisplayed(reconLoc) ? driverTools.getText(reconLoc).get() : null);

        driverTools.waitForElementToAppear(orderIDLoc);
        extractedValues.put("Order ID", driverTools.getText(orderIDLoc).get());

        driverTools.waitForElementToAppear(statusLoc);
        extractedValues.put("Status", driverTools.getText(statusLoc).get());
        return extractedValues;
    }

    public void goToBankReconReportsMenu() {
        driverTools.click(BANK_RECON_REPORTS_MENU);
    }

    public void selectReconReportDate(String monthYear, String day) {
        driverTools.waitForElementToAppear(CHOOSE_START_DATE);
        driverTools.click(CHOOSE_START_DATE);
        selectCalendarDay(monthYear, day);
    }

    public void clickSearchByPaymentId() {
        driverTools.waitForElementToAppear(SEARCH_BY_PAYMENT_ID);
        driverTools.click(SEARCH_BY_PAYMENT_ID);
    }

    public void orderIdClickReconReport() {
        driverTools.waitForElementToAppear(ORDER_ID_INPUT);
        driverTools.clickWithJavascriptExecutor(ORDER_ID_INPUT);
    }

    public void setOrderIDInput(String orderId) {
        driverTools.clearInput(ORDER_ID_INPUT_DATA);
        driverTools.waitForElementToAppear(ORDER_ID_INPUT);
        driverTools.clickWithJavascriptExecutor(ORDER_ID_INPUT);
        driverTools.sendKeys(ORDER_ID_INPUT_DATA, orderId);
      
    }

    public void setOrderIDInputForReconReport(String orderId) {
        driverTools.clearInput(ORDER_ID_INPUT_DATA);
        driverTools.sendKeys(ORDER_ID_INPUT_DATA, orderId);
      
    }

    public Map<String, String> getValuesReconReport() {
        Map<String, String> extractedValues = new HashMap<>();

        driverTools.waitForElementToAppear(orderIdLoc);
        extractedValues.put("Order ID", driverTools.getText(orderIdLoc).get());

        driverTools.waitForElementToAppear(customerPriceLoc);
        extractedValues.put("Customer Price", driverTools.getText(customerPriceLoc).get());

        driverTools.waitForElementToAppear(createdDateLoc);
        extractedValues.put("Created Date", driverTools.getText(createdDateLoc).get());

        driverTools.waitForElementToAppear(reconciliationMethodLoc);
        extractedValues.put("Reconciliation Method", driverTools.getText(reconciliationMethodLoc).get());

        driverTools.waitForElementToAppear(reconciledLoc);
        extractedValues.put("Reconciled By", driverTools.getText(reconciledLoc).get());

        driverTools.waitForElementToAppear(PAYMENT_METHOD);
        extractedValues.put("Payment Method", driverTools.getText(PAYMENT_METHOD).get());

        return extractedValues;
    }

    public B2CDailyDetailOrder getB2CDailyDetailOrder(String vertical, String softblockId, String searchDateBy, String startDate, String endDate) throws JsonSyntaxException, InterruptedException, ExecutionException {
        Gson gson = new Gson();
        B2CDailyDetailOrder response = gson.fromJson(this.getB2CDailyDetailOrderApi(vertical, softblockId, searchDateBy, startDate, endDate).toString(), B2CDailyDetailOrder.class);
        logger.log("Success getting the b2c daily report");
        logger.log("This is response daily report" + response.data.toString());
        return response;
    }

    public JSONObject getB2CDailyDetailOrderApi(String vertical, String softblockId, String searchDateBy, String startDate, String endDate) throws InterruptedException, ExecutionException {
        B2CDailyDetailOrderApi.Inputs input = new B2CDailyDetailOrderApi.Inputs(vertical, softblockId, searchDateBy, startDate, endDate);
        B2CDailyDetailOrderApi b2CDailyDetailOrderApi = new B2CDailyDetailOrderApi(input);
        ApiResponse response = b2CDailyDetailOrderApi.hitApi(apiTools);
        return new JSONObject(response.getBody());
    }

    public double calculateB2COrder(String settlementType, double settlementAmountInVendorCurrency, double startDepositBalance) {
        if (settlementType.equalsIgnoreCase("ADVANCE_DEPOSIT")) {
            logger.log("Start Deposit Balance: " + startDepositBalance);
            logger.log("Deposit Deduction Amount: " + settlementAmountInVendorCurrency);
            double endDepositBalance = Math.abs((startDepositBalance - settlementAmountInVendorCurrency));
            BigDecimal roundedEndDepositBalance = BigDecimal.valueOf(endDepositBalance).setScale(2, BigDecimal.ROUND_HALF_UP);
            logger.log("End Deposit Balance (Rounded): " + roundedEndDepositBalance);
            return roundedEndDepositBalance.doubleValue();
        }
        return 0.0;
    }

    public DeleteOrderId deleteOrderId(String softblockId, String orderId) throws JsonSyntaxException {
        Gson gson = new Gson();
        DeleteOrderId response = gson.fromJson(this.deleteOrderIdApi(softblockId, orderId).toString(), DeleteOrderId.class);
        logger.log("Success Delete Order ID");
        logger.log("This is response data with code: " + response.code.toString());
        return response;
    }

    public JSONObject deleteOrderIdApi(String softblockId, String orderId) {
        DeleteOrderIdApi.Inputs input = new DeleteOrderIdApi.Inputs(softblockId, orderId);
        DeleteOrderIdApi deleteOrderIdApi = new DeleteOrderIdApi(input);
        ApiResponse response = deleteOrderIdApi.hitApi(apiTools);
        return new JSONObject(response.getBody());

    }

    public JSONObject deleteOrderIdApiB2b(String configId, String orderId) {
        DeleteOrderIdApiB2B.Inputs input = new DeleteOrderIdApiB2B.Inputs(configId, orderId);
        DeleteOrderIdApiB2B deleteOrderIdApi = new DeleteOrderIdApiB2B(input);
        ApiResponse response = deleteOrderIdApi.hitApi(apiTools);
        return new JSONObject(response.getBody());
    }

    public JSONObject ClearLockTransactionApi() {
        ClearLockTransactionApi clearLockTransactionApi = new ClearLockTransactionApi();
        ApiResponse response = clearLockTransactionApi.hitApi(apiTools);
        return new JSONObject(response.getBody());
    }

    public ClearLockTransaction getClearLockTransaction() throws JsonSyntaxException, InterruptedException, ExecutionException {
        Gson gson = new Gson();
        ClearLockTransaction response = gson.fromJson(this.ClearLockTransactionApi().toString(), ClearLockTransaction.class);
        logger.log("Success hit api clear lock transaction");
        logger.log("This is response data with code: " + response.code.toString());
        return response;
    }

    public KafkaPublishFlight1stOrderDetail kafkaPublishFlight1stOrderDetail(String kafkaBrokers, String kafkaPort, String kafkaTopic) throws JsonSyntaxException {
        Gson gson = new Gson();
        KafkaPublishFlight1stOrderDetail response = gson.fromJson(this.kafkaPublishFlight1stOrderDetailApi(kafkaBrokers, kafkaPort, kafkaTopic).toString(), KafkaPublishFlight1stOrderDetail.class);
        logger.log("Success Publish Kafka Message");
        if (response.code != null && !response.code.isEmpty()) {
            logger.log("This is response data " + response.code);
        } else {
            logger.log("Response code is empty or null");
        }
        return response;
    }

    public JSONObject kafkaPublishFlight1stOrderDetailApi(String kafkaBrokers, String kafkaPort, String kafkaTopic) {
        KafkaPublishFlight1stOrderDetailApi.Inputs input = new KafkaPublishFlight1stOrderDetailApi.Inputs(kafkaBrokers, kafkaPort, kafkaTopic);
        KafkaPublishFlight1stOrderDetailApi kafkaPublishFlight1stOrderDetailApi = new KafkaPublishFlight1stOrderDetailApi(input);
        kafkaPublishFlight1stOrderDetailApi.hitApi(apiTools);
        return new JSONObject();
    }

    public KafkaPublishFlight2ndOrderDetail kafkaPublishFlight2ndOrderDetail(String kafkaBrokers, String kafkaPort, String kafkaTopic) throws JsonSyntaxException {
        Gson gson = new Gson();
        KafkaPublishFlight2ndOrderDetail response = gson.fromJson(this.kafkaPublishFlight2ndOrderDetailApi(kafkaBrokers, kafkaPort, kafkaTopic).toString(), KafkaPublishFlight2ndOrderDetail.class);
        logger.log("Success Publish Kafka Message");
        if (response.code != null && !response.code.isEmpty()) {
            logger.log("This is response data " + response.code);
        } else {
            logger.log("Response code is empty or null");
        }
        return response;
    }

    public JSONObject kafkaPublishFlight2ndOrderDetailApi(String kafkaBrokers, String kafkaPort, String kafkaTopic) {
        KafkaPublishFlight2ndOrderDetailApi.Inputs input = new KafkaPublishFlight2ndOrderDetailApi.Inputs(kafkaBrokers, kafkaPort, kafkaTopic);
        KafkaPublishFlight2ndOrderDetailApi kafkaPublishFlight2ndOrderDetailApi = new KafkaPublishFlight2ndOrderDetailApi(input);
        kafkaPublishFlight2ndOrderDetailApi.hitApi(apiTools);
        return new JSONObject();
    }

    public void searchVendorId(String vendorId1) {
        driverTools.waitForElementToAppear(VENDOR_ID_AUTOCOMPLETE);
        driverTools.clickWithJavascriptExecutor(VENDOR_ID_AUTOCOMPLETE);
        driverTools.sendKeys(VENDOR_ID_AUTOCOMPLETE, vendorId1);
        driverTools.waitForElementToAppear(VENDOR_ID_CHECKLIST);
        driverTools.clickWithJavascriptExecutor(VENDOR_ID_CHECKLIST);
        driverTools.clickWithJavascriptExecutor(VENDOR_ID_SAVE);
    }

    public void clickSearchButtonVendorListingB2C() {
        driverTools.waitForElementToAppear(VENDOR_LISTING_B2C_SEARCH);
        driverTools.clickWithJavascriptExecutor(VENDOR_LISTING_B2C_SEARCH);
    }

    public String getDailyVerticalTitleOfFlightInternational() {
        driverTools.waitForElementToAppear(B2C_DAILY_VERTICAL_TITLE_FLIGHT_INTERNATIONAL);
        Locator titleLocFlightInternational = new Locator("B2C_DAILY_VERTICAL_TITLE_FLIGHT_INTERNATIONAL", By.xpath("//div[contains(text(),'Flight International')]"));
        return driverTools.getText(titleLocFlightInternational).orElse("");
    }

    public KafkaPublishTTD1stOrderDetail kafkaPublishTTD1stOrderDetail(String kafkaBrokers, String kafkaPort, String kafkaTopic) throws JsonSyntaxException {
        Gson gson = new Gson();
        KafkaPublishTTD1stOrderDetail response = gson.fromJson(this.kafkaPublishTTD1stOrderDetailApi(kafkaBrokers, kafkaPort, kafkaTopic).toString(), KafkaPublishTTD1stOrderDetail.class);
        logger.log("Success Publish Kafka Message");
        if (response.code != null && !response.code.isEmpty()) {
            logger.log("This is response data " + response.code);
        } else {
            logger.log("Response code is empty or null");
        }
        return response;
    }

    public JSONObject kafkaPublishTTD1stOrderDetailApi(String kafkaBrokers, String kafkaPort, String kafkaTopic) {
        KafkaPublishTTD1stOrderDetailApi.Inputs input = new KafkaPublishTTD1stOrderDetailApi.Inputs(kafkaBrokers, kafkaPort, kafkaTopic);
        KafkaPublishTTD1stOrderDetailApi kafkaPublishTTD1stOrderDetailApi = new KafkaPublishTTD1stOrderDetailApi(input);
        kafkaPublishTTD1stOrderDetailApi.hitApi(apiTools);
        return new JSONObject();
    }

    public KafkaPublishTTD2ndOrderDetail kafkaPublishTTD2ndOrderDetail(String kafkaBrokers, String kafkaPort, String kafkaTopic) throws JsonSyntaxException {
        Gson gson = new Gson();
        KafkaPublishTTD2ndOrderDetail response = gson.fromJson(this.kafkaPublishTTD2ndOrderDetailApi(kafkaBrokers, kafkaPort, kafkaTopic).toString(), KafkaPublishTTD2ndOrderDetail.class);
        logger.log("Success Publish Kafka Message");
        if (response.code != null && !response.code.isEmpty()) {
            logger.log("This is response data " + response.code);
        } else {
            logger.log("Response code is empty or null");
        }
        return response;
    }

    public JSONObject kafkaPublishTTD2ndOrderDetailApi(String kafkaBrokers, String kafkaPort, String kafkaTopic) {
        KafkaPublishTTD2ndOrderDetailApi.Inputs input = new KafkaPublishTTD2ndOrderDetailApi.Inputs(kafkaBrokers, kafkaPort, kafkaTopic);
        KafkaPublishTTD2ndOrderDetailApi kafkaPublishTTD2ndOrderDetailApi = new KafkaPublishTTD2ndOrderDetailApi(input);
        kafkaPublishTTD2ndOrderDetailApi.hitApi(apiTools);
        return new JSONObject();
    }

    public void goToTTDMenu() {
        driverTools.click(B2C_TTD_MENU);
    }

    public String getDailyVerticalTitleOfTTD() {
        driverTools.waitForElementToAppear(B2C_DAILY_VERTICAL_TITLE_TTD);
        Locator titleLocTTD = new Locator("B2C_DAILY_VERTICAL_TITLE_TTD", By.xpath("//div[contains(text(),'Things To Do')]"));
        return driverTools.getText(titleLocTTD).orElse("");
    }

    public KafkaPublishTTD3rdOrderDetail kafkaPublishTTD3rdOrderDetail(String kafkaBrokers, String kafkaPort, String kafkaTopic) throws JsonSyntaxException {
        Gson gson = new Gson();
        KafkaPublishTTD3rdOrderDetail response = gson.fromJson(this.kafkaPublishTTD3rdOrderDetailApi(kafkaBrokers, kafkaPort, kafkaTopic).toString(), KafkaPublishTTD3rdOrderDetail.class);
        logger.log("Success Publish Kafka Message");
        if (response.code != null && !response.code.isEmpty()) {
            logger.log("This is response data " + response.code);
        } else {
            logger.log("Response code is empty or null");
        }
        return response;
    }

    public JSONObject kafkaPublishTTD3rdOrderDetailApi(String kafkaBrokers, String kafkaPort, String kafkaTopic) {
        KafkaPublishTTD3rdOrderDetailApi.Inputs input = new KafkaPublishTTD3rdOrderDetailApi.Inputs(kafkaBrokers, kafkaPort, kafkaTopic);
        KafkaPublishTTD3rdOrderDetailApi kafkaPublishTTD3rdOrderDetailApi = new KafkaPublishTTD3rdOrderDetailApi(input);
        kafkaPublishTTD3rdOrderDetailApi.hitApi(apiTools);
        return new JSONObject();
    }

    public KafkaPublishFlight3rdOrderDetail kafkaPublishFlight3rdOrderDetail(String kafkaBrokers, String kafkaPort, String kafkaTopic) throws JsonSyntaxException {
        Gson gson = new Gson();
        KafkaPublishFlight3rdOrderDetail response = gson.fromJson(this.kafkaPublishFlight3rdOrderDetailApi(kafkaBrokers, kafkaPort, kafkaTopic).toString(), KafkaPublishFlight3rdOrderDetail.class);
        logger.log("Success Publish Kafka Message");
        if (response.code != null && !response.code.isEmpty()) {
            logger.log("This is response data " + response.code);
        } else {
            logger.log("Response code is empty or null");
        }
        return response;
    }

    public JSONObject kafkaPublishFlight3rdOrderDetailApi(String kafkaBrokers, String kafkaPort, String kafkaTopic) {
        KafkaPublishFlight3rdOrderDetailApi.Inputs input = new KafkaPublishFlight3rdOrderDetailApi.Inputs(kafkaBrokers, kafkaPort, kafkaTopic);
        KafkaPublishFlight3rdOrderDetailApi kafkaPublishFlight3rdOrderDetailApi = new KafkaPublishFlight3rdOrderDetailApi(input);
        kafkaPublishFlight3rdOrderDetailApi.hitApi(apiTools);
        return new JSONObject();
    }

    public String getDailyVerticalTitleOfDomestic() {
        driverTools.waitForElementToAppear(B2C_DAILY_VERTICAL_TITLE_FLIGHT_DOMESTIC);
        Locator titleLocFlightDomestic = new Locator("B2C_DAILY_VERTICAL_TITLE_FLIGHT_DOMESTIC", By.xpath("//div[contains(text(),'Flight Domestic')]"));
        return driverTools.getText(titleLocFlightDomestic).orElse("");
    }

    public void goToFlightDomesticMenu1() {
        driverTools.click(FLIGHT_DOMESTIC_MENU);
    }

    public KafkaPublishFerryOrderDetail kafkaPublishFerryOrderDetail(String kafkaBrokers, String kafkaPort, String kafkaTopic) throws JsonSyntaxException {
        Gson gson = new Gson();
        KafkaPublishFerryOrderDetail response = gson.fromJson(this.kafkaPublishFerryOrderDetailApi(kafkaBrokers, kafkaPort, kafkaTopic).toString(), KafkaPublishFerryOrderDetail.class);
        logger.log("Success Publish Kafka Message");
        if (response.code != null && !response.code.isEmpty()) {
            logger.log("This is response data " + response.code);
        } else {
            logger.log("Response code is empty or null");
        }
        return response;
    }

    public JSONObject kafkaPublishFerryOrderDetailApi(String kafkaBrokers, String kafkaPort, String kafkaTopic) {
        KafkaPublishFerryOrderDetailApi.Inputs input = new KafkaPublishFerryOrderDetailApi.Inputs(kafkaBrokers, kafkaPort, kafkaTopic);
        KafkaPublishFerryOrderDetailApi kafkaPublishFerryOrderDetailApi = new KafkaPublishFerryOrderDetailApi(input);
        String apiResponse = kafkaPublishFerryOrderDetailApi.hitApi(apiTools).getBody();
        try {
            return new JSONObject(apiResponse);
        } catch (Exception e) {
            logger.log("Error parsing API response to JSONObject: " + e.getMessage());
            return new JSONObject();
        }
    }

    public String getDailyVerticalTitleOfFerry() {
        driverTools.waitForElementToAppear(B2C_DAILY_VERTICAL_TITLE_FERRY);
        Locator titleLocFerry = new Locator("B2C_DAILY_VERTICAL_TITLE_FERRY", By.xpath("//div[contains(text(),'Ferry')]"));
        return driverTools.getText(titleLocFerry).orElse("");
    }

    public void goToFerryMenu() {
        driverTools.click(B2C_FERRY_MENU);
    }

    public KafkaPublishBusOrderDetail kafkaPublishBusOrderDetail(String kafkaBrokers, String kafkaTopic, String kafkaPort) throws JsonSyntaxException {
        Gson gson = new Gson();
        KafkaPublishBusOrderDetail response = gson.fromJson(this.kafkaPublishBusOrderDetailApi(kafkaBrokers, kafkaTopic, kafkaPort).toString(), KafkaPublishBusOrderDetail.class);
        logger.log("Success Publish Kafka Message");
        if (response.code != null && !response.code.isEmpty()) {
            logger.log("This is response data " + response.code);
        } else {
            logger.log("Response code is empty or null");
        }
        return response;
    }

    public JSONObject kafkaPublishBusOrderDetailApi(String kafkaBrokers, String kafkaTopic, String kafkaPort) {
        KafkaPublishBusOrderDetailApi.Inputs input = new KafkaPublishBusOrderDetailApi.Inputs(kafkaBrokers, kafkaTopic, kafkaPort);
        KafkaPublishBusOrderDetailApi kafkaPublishBusOrderDetailApi = new KafkaPublishBusOrderDetailApi(input);
        String apiResponse = kafkaPublishBusOrderDetailApi.hitApi(apiTools).getBody();
        try {
            return new JSONObject(apiResponse);
        } catch (Exception e) {
            logger.log("Error parsing API response to JSONObject: " + e.getMessage());
            return new JSONObject();
        }
    }

    public String getDailyVerticalTitleOfBus() {
        driverTools.waitForElementToAppear(B2C_DAILY_VERTICAL_TITLE_BUS);
        return driverTools.getText(B2C_DAILY_VERTICAL_TITLE_BUS).orElse("");
    }

    public void goToBusMenu() {
        driverTools.click(B2C_BUS_MENU);
    }

    public boolean isEditConfigurationPageSuccess() {
        driverTools.waitForElementToAppear(SNACKBAR_EDIT_CONFIGURATION_PAGE_SUCCESS);
        return driverTools.isElementDisplayed(SNACKBAR_EDIT_CONFIGURATION_PAGE_SUCCESS);
    }


    public void clickEditConfigurationButton() {
        driverTools.waitForElementToAppear(EDIT_CONFIGURATION_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_CONFIGURATION_BUTTON);
    }

    public void clickEditConfigurationSaveButton() {
        driverTools.waitForElementToAppear(EDIT_CONFIGURATION_SAVE_BUTTON);
        driverTools.click(EDIT_CONFIGURATION_SAVE_BUTTON);
    }


    public void inputVATEditConfigurationPage(String vat) {
        driverTools.waitForElementToAppear(VAT_INPUT_EDIT_CONFIGURATION_PAGE);
        driverTools.click(VAT_INPUT_EDIT_CONFIGURATION_PAGE);
        driverTools.clearInput(driverTools.element(VAT_INPUT_EDIT_CONFIGURATION_PAGE).orElseThrow());
        driverTools.sendKeys(VAT_INPUT_EDIT_CONFIGURATION_PAGE, vat);
    }

    public void inputWHTEditConfigurationPage(String wht) {
        driverTools.waitForElementToAppear(WHT_INPUT_EDIT_CONFIGURATION_PAGE);
        driverTools.click(WHT_INPUT_EDIT_CONFIGURATION_PAGE);
        driverTools.clearInput(driverTools.element(WHT_INPUT_EDIT_CONFIGURATION_PAGE).orElseThrow());
        driverTools.sendKeys(WHT_INPUT_EDIT_CONFIGURATION_PAGE, wht);
    }

    public void blurActiveElement() {
        ((JavascriptExecutor) driver).executeScript("document.activeElement?.blur();");
    }

    public void reconcile(){
        driverTools.click(EXTRA_DETAILS);
        driverTools.waitForElementToAppear(REVOKED_ACTION);
        driverTools.click(REVOKED_ACTION);
        driverTools.waitForElementToAppear(YES_REVOKE_BUTTON);
        driverTools.click(YES_REVOKE_BUTTON);
    }

    public void exportReport(){
        driverTools.waitForElementToAppear(EXPORT_REPORT_BUTTON);
        driverTools.click(EXPORT_REPORT_BUTTON);
    }

    public void selectCheckBox(){
        driverTools.waitForElementToAppear(SETTLEMENT_CHECKBOX);
        driverTools.clickWithJavascriptExecutor(SETTLEMENT_CHECKBOX);
        driverTools.clickWithJavascriptExecutor(INVOICE_CHECKBOX);
    }

    public void confirm(){
        driverTools.waitForElementToAppear(CONFIRM);
        driverTools.click(CONFIRM);
        driverTools.dummyWait(1);
    }

    public void selectUSD(){
        driverTools.waitForElementToAppear(CHOOSE_CURRENCY_DROPDOWN);
        driverTools.click(CHOOSE_CURRENCY_DROPDOWN);
        driverTools.dummyWait(1);
        driverTools.click(USD_CURRENCY_OPTION);
    }

    public void selectVCCOption() {
        driverTools.waitForElementToAppear(SETTLEMENT_DROPDOWN_MENU);
        driverTools.click(SETTLEMENT_DROPDOWN_MENU);

        driverTools.waitForElementToAppear(VCC_OPTION);
        driverTools.click(VCC_OPTION);
    }

    public void addNewVCC() {
        driverTools.waitForElementToAppear(ADD_NEW_VCC_OPTION);
        driverTools.clickWithJavascriptExecutor(ADD_NEW_VCC_OPTION);
    }

    public boolean isAddNewVCCPageLoaded(){
        driverTools.waitForElementToDisappear(POPUP);
        driverTools.refreshWebpage();
        return driverTools.isElementDisplayed(VCC_PAGE_HEADING);
    }

    public void fillNewVCCInformation(String purchaseType, String nameOnCard, String issuedCardType) {
        driverTools.waitForElementToAppear(PURCHASE_TYPE);
        driverTools.clickWithJavascriptExecutor(PURCHASE_TYPE);
        driverTools.sendKeys(PURCHASE_TYPE,purchaseType);
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.waitForElementToPresence(PURCHASE_TYPE_SELECTOR,5);
        driverTools.clickElementByCoordinateDWeb(PURCHASE_TYPE_SELECTOR);
        logger.log("This is purchase type " + driverTools.getValue(PURCHASE_TYPE));

        driverTools.waitForElementToDisappear(POPUP);
        driverTools.waitForElementToAppear(NAME_ON_CARD);
        driverTools.clickWithJavascriptExecutor(NAME_ON_CARD);
        driverTools.sendKeys(NAME_ON_CARD,nameOnCard);
        logger.log("This is name on card " + driverTools.getValue(NAME_ON_CARD));

        driverTools.waitForElementToAppear(EXPIRY_DATE);
        driverTools.clickWithJavascriptExecutor(EXPIRY_DATE);
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.waitForElementToPresence(EXPIRY_DATE_SELECTOR,5);
        driverTools.clickElementByCoordinateDWeb(EXPIRY_DATE_SELECTOR);
        logger.log("This is expiry date " + driverTools.getValue(EXPIRY_DATE));

        driverTools.waitForElementToAppear(ISSUED_CARD_TYPE);
        driverTools.clickWithJavascriptExecutor(ISSUED_CARD_TYPE);
        driverTools.sendKeys(ISSUED_CARD_TYPE,issuedCardType);
        driverTools.waitForElementToAppear(SAVE_MODAL);
        driverTools.waitForElementToPresence(ISSUED_CARD_TYPE_SELECTOR,5);
        driverTools.clickElementByCoordinateDWeb(ISSUED_CARD_TYPE_SELECTOR);
        logger.log("This is issued card type " + driverTools.getValue(ISSUED_CARD_TYPE));
    }

    public void fillNewVCCDetails(String creditLimit, String currencyType, String maximumTransactionsAllowed, String period) {
        driverTools.waitForElementToAppear(CREDIT_LIMIT);
        driverTools.clickWithJavascriptExecutor(CREDIT_LIMIT);
        driverTools.sendKeys(CREDIT_LIMIT,creditLimit);
        logger.log("This is credit limit " + driverTools.getValue(CREDIT_LIMIT));

        driverTools.waitForElementToAppear(CURRENCY_TYPE);
        driverTools.clickWithJavascriptExecutor(CURRENCY_TYPE);
        driverTools.sendKeys(CURRENCY_TYPE,currencyType);
        driverTools.waitForElementToAppear(SAVE_MODAL);
        driverTools.waitForElementToPresence(CURRENCY_TYPE_SELECTOR,8);

        driverTools.clickElementByCoordinateDWeb(CURRENCY_TYPE_SELECTOR);
        logger.log("This is currency type selector" + driverTools.getValue(CURRENCY_TYPE_SELECTOR));

        driverTools.waitForElementToAppear(MAXIMUM_TRANSACTIONS_ALLOWED);
        driverTools.clickWithJavascriptExecutor(MAXIMUM_TRANSACTIONS_ALLOWED);
        driverTools.sendKeys(MAXIMUM_TRANSACTIONS_ALLOWED,maximumTransactionsAllowed);
        logger.log("This is maximum transactions allowed " + driverTools.getValue(MAXIMUM_TRANSACTIONS_ALLOWED));

        driverTools.waitForElementToAppear(PERIOD);
        driverTools.clickWithJavascriptExecutor(PERIOD);
        driverTools.sendKeys(PERIOD,period);
        driverTools.waitForElementToAppear(SAVE_MODAL);
        driverTools.waitForElementToPresence(PERIOD_SELECTOR,8);

        driverTools.clickElementByCoordinateDWeb(PERIOD_SELECTOR);
        logger.log("This is period " + driverTools.getValue(PERIOD));
    }

    public void saveVCC(){
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public boolean isVCCListingPageLoaded() {
        driverTools.waitForElementToAppear(VCC_LISTING_PAGE_HEADER);
        return driverTools.isElementDisplayed(VCC_LISTING_PAGE_HEADER);
    }

    public boolean getCreditLimitOfFirstVCCInList(String creditLimit){
        driverTools.scrollToElement(FIRST_VCC_CREDIT_LIMIT);
        driverTools.waitForElementToAppear(FIRST_VCC_CREDIT_LIMIT);
        String credit = driverTools.getText(FIRST_VCC_CREDIT_LIMIT).orElse("");
        logger.log("This is credit limit of first VCC in list " + credit);
        String normalizedCredit = credit.replaceAll("[.,]", "");
        return normalizedCredit.equals(creditLimit);
    }

    public void viewFirstVCCInList(){
        driverTools.waitForElementToAppear(FIRST_VCC_MORE_BUTTON);
        driverTools.click(FIRST_VCC_MORE_BUTTON);
        driverTools.waitForElementToAppear(VIEW_BALANCE);
        driverTools.click(VIEW_BALANCE);
    }

    public boolean isVCCDetailsPageLoaded() {
        driverTools.waitForElementToDisappear(POPUP);
        driverTools.waitForElementToAppear(VCC_DETAILS_PAGE_HEADER);
        return driverTools.isElementDisplayed(VCC_DETAILS_PAGE_HEADER);
    }

    public void clickEditButtonInViewVCCPage(){
        driverTools.waitForElementToAppear(SAVE_MODAL);
        driverTools.waitForElementToAppear(EDIT_VCC_BTN);
        driverTools.click(EDIT_VCC_BTN);
    }

    public void updateCreditLimitInEditVCCPage(String updatedCreditLimit){
        driverTools.waitForElementToAppear(CREDIT_LIMIT);
        driverTools.clickWithJavascriptExecutor(CREDIT_LIMIT);
        driverTools.waitForElementToAppear(SAVE_MODAL);
        driverTools.clearInput(driverTools.element(CREDIT_LIMIT).orElseThrow());
        driverTools.waitForElementToAppear(SAVE_MODAL);
        driverTools.sendKeys(CREDIT_LIMIT, updatedCreditLimit);
        logger.log("This is updated credit limit " + driverTools.getValue(CREDIT_LIMIT));
    }

    public void saveVCCAfterUpdation(){
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.click(SAVE_BUTTON);
        driverTools.waitForElementToAppear(SAVE_MODAL);
        driverTools.click(SAVE_MODAL);
    }

    public String getCardNumberOfFirstVCCInList(){
        driverTools.waitForElementToAppear(CARD_NUMBER);
        String cardNumber = driverTools.getText(CARD_NUMBER).orElse("");
        logger.log("This is card number of first VCC in list " + cardNumber);
        return cardNumber;
    }

    public boolean getUpdatedCreditLimitOfFirstVCCInList(String cardNumber, String updatedCreditLimit){

        int index = 1 ;

        for ( int i = 1 ; i <= 10 ; i++ ) {
            Locator cardNumberLoc = getCardNumber(i);
            driverTools.waitForElementToAppear(cardNumberLoc);
            String currentCardNumber = driverTools.getText(cardNumberLoc).orElse("");
            logger.log("This is current card number of first VCC in list " + currentCardNumber);
            if(currentCardNumber.equals(cardNumber)){
                index = i ;
                logger.log("Card number matches the expected card number: " + cardNumber);
                break;
            } else {
                logger.log("Card number does not match the expected card number: " + cardNumber + ". Retrying... (" + i + "/10)");
                if (i == 10) {
                    logger.log("Failed to find the expected card number after 10 attempts. Exiting the method.");
                    return false;
                }
            }
        }

        Locator exactCreditLimit = getExactCreditLimit(index);
        driverTools.scrollToElement(exactCreditLimit);
        driverTools.waitForElementToAppear(exactCreditLimit);
        String credit = driverTools.getText(exactCreditLimit).orElse("");
        logger.log("This is updated credit limit of first VCC in list " + credit);
        String normalizedCredit = credit.replaceAll("[.,]", "");
        if(normalizedCredit.equals(updatedCreditLimit)){
            logger.log("The credit limit has been successfully updated to " + updatedCreditLimit);
            return true;
        } else {
            logger.log("The credit limit update failed. Expected: " + updatedCreditLimit + ", but got: " + normalizedCredit);
            return false ;
        }
    }

    private Locator getExactCreditLimit(int index){
        return new Locator("EXACT_VCC_CREDIT_LIMIT", By.xpath("//table[@data-testid='table:list']//tbody//tr["+index+"]/td[12]"));
    }

    private Locator getCardNumber(int index) {
        return new Locator("CARD_NUMBER", By.xpath("//table[@data-testid='table:list']//tbody//tr["+index+"]/td[5]"));
    }

    public boolean isVendorNameVisible(String vendorName) {
        Locator vendorNameTableLocator = new Locator("VENDOR_NAME_TABLE", new By.ByXPath(String.format("//td[@data-field='vendor-name']//div[normalize-space(.)='%s']", vendorName)));
        driverTools.waitForElementToAppear(vendorNameTableLocator, 10);
        return driverTools.isElementDisplayed(vendorNameTableLocator);
    }

    public boolean isOrderDetailIdVisible(String orderDetailId) {
        Locator orderDetailIdLocator = new Locator("ORDER_DETAIL_ID_TABLE", new By.ByXPath(String.format("//table//td[normalize-space()='%s']", orderDetailId)));
        driverTools.waitForElementToAppear(orderDetailIdLocator, 10);
        return driverTools.isElementDisplayed(orderDetailIdLocator);
    }

    public Locator getStartDateLocator(String formattedDate) {
        return new Locator("START_DATE", By.xpath(String.format("//span[@aria-label='%s']/ancestor::button", formattedDate)));
    }

    public Locator getEndDateLocator(String formattedDate) {
        return new Locator("END_DATE", By.xpath(String.format("//span[@aria-label='%s']/ancestor::button", formattedDate)));
    }

    public void searchDateBy(String startDate, String endDate) {
        driverTools.waitForElementToAppear(START_DATE_INPUT, 5);
        driverTools.clickWithJavascriptExecutor(START_DATE_INPUT);
        goToPreviousMonthUntilDateVisible(startDate, 12);
        driverTools.clickWithJavascriptExecutor(getStartDateLocator(startDate));
        goToPreviousMonthUntilDateVisible(endDate, 12);
        driverTools.clickWithJavascriptExecutor(getEndDateLocator(endDate));
    }

    public void goToPreviousMonthUntilDateVisible(String formattedDate, int maxAttempts) {
        Locator dateLocator = new Locator("DATE_LOCATOR", By.xpath(String.format("//span[@aria-label='%s']/ancestor::button", formattedDate)));
        driverTools.waitForElementToAppear(PREVIOUS_MONTH_BUTTON, 5);
        for (int i = 0; i < maxAttempts; i++) {
            if (driverTools.isElementDisplayed(dateLocator)) {
                return;
            }
            driverTools.waitForElementToAppear(PREVIOUS_MONTH_BUTTON, 5);
            driverTools.clickWithJavascriptExecutor(PREVIOUS_MONTH_BUTTON);
            driverTools.waitForElementToAppear(dateLocator, 1);
        }
    }

    public int getTransactionRowCount() {
        driverTools.dummyWait(5);
        Locator rowsLoc = new Locator("TABLE_ROWS", By.xpath("//table[@data-testid='table:list']//tbody/tr"));
        return driverTools.waitForElementsToAppear(rowsLoc, 5).orElse(new ArrayList<>()).size();
    }

    public boolean verifyDeductionIsCorrect(String startBal, String dedAmount, String endBal) {
        String cleanStart = startBal.replaceAll("[^0-9.]", "");
        String cleanDed = dedAmount.replaceAll("[^0-9.]", "");
        String cleanEnd = endBal.replaceAll("[^0-9.]", "");

        BigDecimal startBalDec = new BigDecimal(cleanStart);
        BigDecimal dedAmountDec = new BigDecimal(cleanDed);
        BigDecimal endBalDec = new BigDecimal(cleanEnd);

        BigDecimal calculatedEndBal = startBalDec.subtract(dedAmountDec);

        return calculatedEndBal.compareTo(endBalDec) == 0;
    }

    public boolean isViewListOfTransactionsByLoaded() {
        return driverTools.waitForElementToAppear(VIEW_LIST_OF_TRANSACTIONS_BY, 5).isPresent();
    }

    public String getDepositDeductionAmountColumnValue(int index) {
        Locator depositDeductionAmountColumnValue = new Locator("DEPOSIT_DEDUCTION_AMOUNT_COLUMN",By.xpath("//table[@data-testid='table:list']//tbody/tr["+index+"]/td[144]"));
        return driverTools.getText(depositDeductionAmountColumnValue).orElse("");
    }

    public String getStartDepositBalanceColumnValue(int index) {
        Locator startDepositBalanceColumnValue = new Locator("START_DEPOSIT_BALANCE_COLUMN",By.xpath("//table[@data-testid='table:list']//tbody/tr["+index+"]/td[145]"));
        return driverTools.getText(startDepositBalanceColumnValue).orElse("");
    }

    public String getEndDepositBalanceColumnValue(int index) {
        Locator endDepositBalanceColumnValue = new Locator("END_DEPOSIT_BALANCE_COLUMN",By.xpath("//table[@data-testid='table:list']//tbody/tr["+index+"]/td[146]"));
        return driverTools.getText(endDepositBalanceColumnValue).orElse("");
    }
    public String getOrderDetailId(String rowIndex, String columnIndex) {
        Locator orderDetailIdDeparture = new Locator("ORDER_DETAIL_ID_DEPARTURE", By.xpath("//table[@data-testid='table:list']//tbody/tr[" + rowIndex + "]/td[" + columnIndex + "]"));
        return driverTools.getText(orderDetailIdDeparture).orElse("");
    }
    public String getCountOfAdult(String rowIndex, String columnIndex) {
        Locator countOfAdultLocator = new Locator("COUNT_OF_ADULT", By.xpath("//table[@data-testid='table:list']//tbody/tr[" + rowIndex + "]/td[" + columnIndex + "]"));
        return driverTools.getText(countOfAdultLocator).orElse("");
    }

    public int getTransactionColumnIndexByName(String columnName) {
        Locator headersLocator = new Locator("TRANSACTION_TABLE_HEADERS", By.xpath("//table[@data-testid='table:list']//thead/tr[last()]/th[not(contains(translate(@style,' ',''),'display:none'))]"));
        driverTools.waitForElementToAppear(headersLocator);
        List<WebElement> headers = driver.findElements(headersLocator.by());
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().trim().equalsIgnoreCase(columnName)) {
                logger.log("Column '" + columnName + "' resolved to visible column index " + (i + 1) + " of " + headers.size());
                return i + 1;
            }
        }
        throw new IllegalArgumentException("Column '" + columnName + "' was not found in the transaction table.");
    }

    public String getTransactionColumnValueByName(String rowIndex, String columnName) {
        Locator transactionCell = new Locator("TRANSACTION_CELL", By.xpath("//table[@data-testid='table:list']//tbody/tr[" + rowIndex + "]/td[" + getTransactionColumnIndexByName(columnName) + "]"));
        return driverTools.getText(transactionCell).orElse("").trim();
    }

    public String getTransactionColumnValueByName(String rowIndex, String columnName, String expectedText) {
        Locator transactionCell = new Locator("TRANSACTION_CELL", By.xpath("//table[@data-testid='table:list']//tbody/tr[" + rowIndex + "]/td[" + getTransactionColumnIndexByName(columnName) + "]"));
        driverTools.waitForTextToBePresent(transactionCell, expectedText);
        return driverTools.getText(transactionCell).orElse("").trim();
    }

    public void clickSingleSlab() {
        driverTools.waitForElementToAppear(SINGLE_SLAB);
        driverTools.click(SINGLE_SLAB);
    }

    public boolean isTierOneCommissionAppears() {
        driverTools.waitForElementToAppear(TIER_1_COMMISSION_TEXT);
        String tierOneCommission = driverTools.getText(TIER_1_COMMISSION_TEXT).orElse("").trim();
        logger.log("This is tier one commission value: " + tierOneCommission);
        return tierOneCommission.contains("Tier Commission 1") || tierOneCommission.contains("Komisi Tier 1");
    }

    public void clickMultiSlab() {
        driverTools.waitForElementToAppear(MULTI_SLAB);
        driverTools.click(MULTI_SLAB);
    }

    public void clickAddTier() {
        driverTools.waitForElementToAppear(ADD_TIER_BUTTON);
        driverTools.click(ADD_TIER_BUTTON);
    }

    public void fillTierCommission(int tier, String minSeatSold, String maxSeatSold, String commission) {
        fillTierField(getTierMinSeatsLocator(tier), minSeatSold);
        fillTierField(getTierMaxSeatsLocator(tier), maxSeatSold);
        fillTierField(getTierCommissionLocator(tier), commission);
    }

    private void fillTierField(Locator locator, String value) {
        driverTools.waitForElementToAppear(locator);
        driverTools.scrollElementToCenter(locator);
        driverTools.clickWithJavascriptExecutor(locator);
        driverTools.clearInput(locator);
        driverTools.sendKeys(locator, value);
    }

    private Locator getTierMinSeatsLocator(int tier) {
        return new Locator("TIER_MIN_SEATS_" + tier,
                By.xpath("(//input[@id='min-seats-sold'])[" + tier + "]"));
    }

    private Locator getTierMaxSeatsLocator(int tier) {
        return new Locator("TIER_MAX_SEATS_" + tier,
                By.xpath("(//input[@id='max-seats-sold'])[" + tier + "]"));
    }

    private Locator getTierCommissionLocator(int tier) {
        return new Locator("TIER_COMMISSION_" + tier,
                By.xpath("(//input[@id='commission'])[" + tier + "]"));
    }

    public void clickSave() {
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public void assertTierCommission(int tier, String expectedMin, String expectedMax, String expectedCommission) {
        driverTools.waitForElementToAppear(getTierMinSeatsLocator(tier));
        String actualMin = driverTools.getAttribute(getTierMinSeatsLocator(tier).by(), "value").trim();
        driverTools.waitForElementToAppear(getTierMaxSeatsLocator(tier));
        String actualMax = driverTools.getAttribute(getTierMaxSeatsLocator(tier).by(), "value").trim();
        driverTools.waitForElementToAppear(getTierCommissionLocator(tier));
        String actualCommission = driverTools.getAttribute(getTierCommissionLocator(tier).by(), "value").trim();
        logger.log("This is actual min seat sold for tier " + tier + ": " + actualMin);
        logger.log("This is actual max seat sold for tier " + tier + ": " + actualMax);
        logger.log("This is actual commission for tier " + tier + ": " + actualCommission);
        Assertion.assertThat("Verifies Min Seats Sold", actualMin, is(expectedMin));
        Assertion.assertThat("Verifies Max Seats Sold", actualMax, is(expectedMax));
        Assertion.assertThat("Verifies Commission", actualCommission, is(expectedCommission));
    }

    public void clickToggleBtn() {
        driverTools.waitForElementToAppear(TOGGLE_BTN);
        driverTools.clickWithJavascriptExecutor(TOGGLE_BTN);
    }

    public void deleteTiers(int numberOfTiers) {
        int remainingTiers = numberOfTiers;
        while (remainingTiers > 1) {
            driverTools.waitForElementToAppear(DELETE_LOCATOR);
            driverTools.clickWithJavascriptExecutor(DELETE_LOCATOR);
            remainingTiers--;
            logger.log("Deleted a tier. Remaining tiers: " + remainingTiers);
        }
    }

    public int getOrderIdCountFromLatestDownloadedCSV(String fileNamePattern, String orderId) {
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        File dir = new File(downloadDir);

        long deadlineMs = Instant.now().toEpochMilli() + 30_000;
        File targetFile = null;

        while (Instant.now().toEpochMilli() < deadlineMs && targetFile == null) {
            File[] listFiles = dir.listFiles();
            if (listFiles != null) {
                for (File file : listFiles) {
                    if (file.getName().contains(fileNamePattern) && file.getName().endsWith(".csv")) {
                        if (targetFile == null || file.lastModified() > targetFile.lastModified()) {
                            targetFile = file;
                        }
                    }
                }
            }
            if (targetFile == null) {
                driverTools.dummyWait(1);
            }
        }
        if (targetFile == null || !targetFile.exists()) {
            throw new RuntimeException("Latest CSV file with pattern '" + fileNamePattern + "' not found");
        }

        int count = 0;
        List<List<String>> csvData = CoreFunctions.getCSVContent(targetFile);

        if (targetFile.exists()) {
            logger.log("Deleting the latest CSV file: " + targetFile.getAbsolutePath());
            targetFile.delete();
        }

        for (int i = 1; i < csvData.size(); i++) {
            List<String> row = csvData.get(i);
            if (row.size() > 3 && row.get(3).trim().equals(orderId)) {
                count++;
            }
        }

        return count;
    }
    
    
    public void goToAccommodationThirdPartyMenu() {
        driverTools.waitForElementToAppear(ACCOMMODATION_THIRD_PARTY_MENU);
        driverTools.clickWithJavascriptExecutor(ACCOMMODATION_THIRD_PARTY_MENU);
    }
    public String getAccommodationRow1CustomerCurrency() {
        driverTools.waitForElementToAppear(ACCOMM_CUSTOMER_CURRENCY_ROW1);
        return driverTools.getText(ACCOMM_CUSTOMER_CURRENCY_ROW1).orElse("").trim();
    }
    public String getAccommodationRow1VendorCurrency() {
        driverTools.waitForElementToAppear(ACCOMM_VENDOR_CURRENCY_ROW1);
        return driverTools.getText(ACCOMM_VENDOR_CURRENCY_ROW1).orElse("").trim();
    }
    public String getAccommodationRow1AddOnDetails() {
        driverTools.waitForElementToAppear(ACCOMM_ADDON_DETAILS_ROW1);
        return driverTools.getText(ACCOMM_ADDON_DETAILS_ROW1).orElse("").trim();
    }

    public boolean isAccommodationThirdPartyPageLoaded() {
        driverTools.waitForElementToAppear(HEADER_ACCOMMODATION_THIRD_PARTY);
        driverTools.scrollToElement(HEADER_ACCOMMODATION_THIRD_PARTY);
        return driverTools.isElementDisplayed(HEADER_ACCOMMODATION_THIRD_PARTY);
    }

    public void goToAccommodationNativeMenu() {
        driverTools.waitForElementToAppear(ACCOMMODATION_NATIVE_MENU);
        driverTools.clickWithJavascriptExecutor(ACCOMMODATION_NATIVE_MENU);
    }

    public void goToCreditMemoMenu() {
        driverTools.waitForElementToAppear(SETTLEMENT_DROPDOWN_MENU);
        driverTools.click(SETTLEMENT_DROPDOWN_MENU);
        driverTools.waitForElementToAppear(CREDIT_MEMO_SUBMENU);
        driverTools.click(CREDIT_MEMO_SUBMENU);
    }

    public void goToSupplierCreditMemo(String supplierPath) {
        String currentUrl = driverTools.getCurrentURL();
        driverTools.get(currentUrl + supplierPath);
    }
}