package com.tiket.web.dashboard.page.order;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tiket.core.CoreFunctions.generateRandomInt;

public class AdminCareDetailPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator TITLE_ORDER_DETAIL = new Locator("TITLE_ORDER_DETAIL", new By.ByXPath("//*[@data-testid='title-order-detail']"));
    private static final Locator USER_INFO_LIST = new Locator("USER_INFO_LIST", new By.ByXPath("//*[@data-testid='account-info']//span[contains(@class,'pr-[8px]')]"));
    private static final Locator LOADING_STATE = new Locator("LOADING_STATE", new By.ByXPath("//*[contains(@class,'loading_indicator_container')]"));
    private static final Locator ORDER_DETAIL_CARD = new Locator("ORDER_DETAIL_CARD", new By.ByXPath("//div[@id='order-detail-0']"));
    private static final Locator ORDER_DETAIL_MULTI = new Locator("ORDER_DETAIL_MULTI", new By.ByXPath("//span[text()='Order Detail ID']/following-sibling::div/span"));
    private static final Locator ORDER_DETAIL_SINGLE = new Locator("ORDER_DETAIL_SINGLE", new By.ByXPath("//*[@data-testid='label-order-id']"));
    private static final Locator ORDER_DETAIL_MULTI_OLD = new Locator("ORDER_DETAIL_MULTI_OLD", new By.ByXPath("//p[@class='trip-type'][contains(text(),'Order Detail ID')]"));
    private static final Locator MENU_REFUND = new Locator("MENU_REFUND", new By.ByXPath("//span[contains(@class,'styles_menuLabel')][normalize-space()='Refund']"));
    private static final Locator ORDER_AMOUNT = new Locator("ORDER_AMOUNT",
            new By.ByXPath("//div[contains(text(),'Invoice')]/following-sibling::div[contains(@class,'item-detail')]"));
    private static final Locator DISCOUNT_TYPE = new Locator("DISCOUNT_TYPE",
            new By.ByXPath("//div[contains(@class,'discount')]//span[contains(text(),'Type')]/following-sibling::span"));
    private static final Locator DISCOUNT_AMOUNT = new Locator("DISCOUNT_AMOUNT",
            new By.ByXPath("//div[contains(@class,'discount')]//span[contains(text(),'Worth')]/following-sibling::span"));
    private static final Locator SPECIAL_REFUND_BUTTON = new Locator("SPECIAL_REFUND_BUTTON",
            new By.ByXPath("//div[contains(@class,'refund-cancellation')]//span[contains(text(),'Special Refund')]"));
    private static final Locator EREFUND_BY_CS_BUTTON = new Locator("EREFUND_BY_CS_BUTTON", By.xpath("//*[@data-testid='refundable-content']//*[contains(text(),'e-Refund by CS')]"));
    private static final Locator EREFUND_BY_CS_BUTTON_ALTERNATE = new Locator("EREFUND_BY_CS_BUTTON_ALTERNATE", By.xpath("//span[normalize-space(.)='E-Refund by CS']/ancestor-or-self::*[contains(@class,'iconRefundText') or self::button][1] | //span[normalize-space(.)='E-Refund by CS']"));
    private static final Locator PRICE_DETAIL_BUTTON = new Locator("PRICE_DETAIL_BUTTON",
            new By.ByXPath("//button[@data-testid='transaction-priceDetails'] | //button[contains(normalize-space(.), 'PRICE DETAIL')] | //button[contains(normalize-space(.), 'Price Details')]"));
    private static final String PRICE_DETAILS_MODAL_XPATH = "//section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Price Details']]";
    private static final Locator PRICE_DETAILS_MODAL = new Locator("PRICE_DETAILS_MODAL", By.xpath(PRICE_DETAILS_MODAL_XPATH));
    private static final Locator PRICE_DETAILS_MODAL_CLOSE = new Locator("PRICE_DETAILS_MODAL_CLOSE",
            By.xpath(PRICE_DETAILS_MODAL_XPATH + "//button[@data-testid='modal-close-button']"));
    private static final Locator TRANSACTION_AMOUNT_SECTION = new Locator("TRANSACTION_AMOUNT_SECTION",
            new By.ByXPath("//*[@data-testid='dynamic-transaction'][.//*[normalize-space()='Amount']]"));
    private static final Locator PRICE_DETAIL_HOTEL_ORDER_AMOUNT_SECTION = new Locator("PRICE_DETAIL_HOTEL_ORDER_AMOUNT_SECTION",
            new By.ByXPath("//*[@data-testid='order-detail']//*[@data-testid='dynamic-transaction'][.//*[normalize-space()='Amount']]"));
    private static final Locator PRICE_SUMMARY_FARE = new Locator("PRICE_SUMMARY_FARE",
            new By.ByXPath("//div[contains(@class,'price-detail-wrapper')]"));
    private static final Locator FARE_LIST_PRICE_DETAIL = new Locator("FARE_LIST_PRICE_DETAIL",
            new By.ByXPath(".//div[@data-testid='collapse-container']"));
    private static final Locator OTHER_FEES_PRICE_DETAIL = new Locator("OTHER_FEES_PRICE_DETAIL",
            new By.ByXPath("//div[contains(@class,'price-detail-wrapper')]//li[span[normalize-space()='Other Fees']]/ul"));
    private static final Locator TOTAL_PAYMENT_PRICE_DETAIL = new Locator("TOTAL_PAYMENT_PRICE_DETAIL",
            new By.ByXPath("//div[contains(@class,'price-detail-wrapper')]//div[contains(@class,'inline-flex')][span[normalize-space()='Total']]"));
    private static final Locator ADD_NOTE_BUTTON = new Locator("ADD_NOTE_BUTTON",
            new By.ByXPath("//button[.//span[normalize-space()='Add Note']]"));
    private static final Locator ADD_NOTE_MODAL = new Locator("ADD_NOTE_MODAL",
            new By.ByXPath("//form[@data-testid='addnote-form']"));
    private static final Locator ADD_NOTE_CATEGORY_DROPDOWN = new Locator("ADD_NOTE_CATEGORY_DROPDOWN",
            new By.ByXPath("//form[@data-testid='addnote-form']//input[@data-testid='add-note-type']"));
    private static final Locator ADD_NOTE_CATEGORY_DROPDOWN_VALUES = new Locator("ADD_NOTE_CATEGORY_DROPDOWN_VALUES",
            new By.ByXPath("//*[contains(@data-testid,'value-dropdown-option')] | //div[contains(@id,'dropdown-options')]//*[contains(@class,'dropdown-option')]"));
    private static final Locator ADD_NOTE_TEXT_AREA = new Locator("ADD_NOTE_TEXT_AREA",
            new By.ByXPath("//textarea[@data-testid='add-note-description']"));
    private static final Locator ADD_NOTE_SUBMIT_BUTTON = new Locator("ADD_NOTE_SUBMIT_BUTTON",
            new By.ByXPath("//form[@data-testid='addnote-form']//button[contains(normalize-space(),'Submit')]"));
    private static final Locator FIRST_LATEST_ACTIVITIES_DESCRIPTION = new Locator("FIRST_LATEST_ACTIVITIES_DESCRIPTION",
            new By.ByXPath("(//td[@data-testid='log-activity'])[1]/following-sibling::td[1]//span[contains(@class,'whitespace-pre-wrap')]"));
    private static final Locator FIRST_LATEST_ACTIVITIES_DATE = new Locator("FIRST_LATEST_ACTIVITIES_DATE",
            new By.ByXPath("(//td[@data-testid='log-activity'])[1]/following-sibling::td[2]//span"));
    private static final Locator GREEN_SUCCESS_BAR = new Locator("GREEN_SUCCESS_BAR",
            new By.ByXPath("//div[contains(@class,'snackbar')]//div[contains(text(), 'successfully')]"));
    private static final Locator ADD_NOTE_SUCCESS_SNACKBAR = new Locator("ADD_NOTE_SUCCESS_SNACKBAR",
            new By.ByXPath("//div[contains(.,'You have successfully added note')]"));
    private static final Locator RESEND_VOUCHER_SUCCESS_SNACKBAR = new Locator("RESEND_VOUCHER_SUCCESS_SNACKBAR",
            new By.ByXPath("//div[contains(.,'You have successfully resend voucher')]"));
    private static final Locator FIRST_REFUND_POPUP_CONTENT = new Locator("FIRST_REFUND_POPUP_CONTENT", By.xpath("//*[@data-testid='popup-refund']//*[contains(@class,'popup-contents')]/li"));
    private static final Locator REFUND_POPUP_ROOM_OPTION = new Locator("REFUND_POPUP_ROOM_OPTION",
            By.xpath("//*[@data-testid='popup-refund']//ul[@role='radiogroup']/li"
                    + " | //*[@data-testid='popup-refund']//*[contains(@class,'refundPopupContents')]/li"));
    private static final Locator REFUND_POPUP_OK_BUTTON = new Locator ("REFUND_POPUP_OK_BUTTON", By.xpath("//*[@data-testid='popup-refund']//button[text()='OK']"));
    private static final Locator CHOOSE_ROOM_OK_BUTTON = new Locator("CHOOSE_ROOM_OK_BUTTON",
            By.xpath("//*[@data-testid='popup-refund']//button[contains(@class,'Button_variant_primary') and normalize-space(.)='OK']"
                    + " | //*[@data-testid='popup-refund']//button[normalize-space(.)='OK']"));
    private static final Locator CHOOSE_ROOM_CLOSE_BUTTON = new Locator("CHOOSE_ROOM_CLOSE_BUTTON",
            By.xpath("//section[contains(@class,'Modal_modal_popup_small') and .//*[contains(normalize-space(),'Choose a room to refund')]]//button[@data-testid='modal-close-button']"));
    private static final Locator REFUND_FORM_MODAL = new Locator("REFUND_FORM_MODAL", new By.ByXPath("//div[contains(@class,'modal-refund-form')]"));
    private static final Locator REFUND_FORM_REASON_DROPDOWN = new Locator("REFUND_FORM_REASON_DROPDOWN", By.xpath("//*[@id='reason-for-refund']"));
    private static final Locator REFUND_FORM_ESTIMATION_SEE_DETAILS = new Locator("REFUND_FORM_ESTIMATION_SEE_DETAILS", By.xpath("//*[contains(text(),'Total Estimation Amount')]/following-sibling::*//*[contains(text(),'See Details')]"));
    private static final Locator REFUND_FORM_ESTIMATION_DETAILS = new Locator("REFUND_FORM_ESTIMATION_DETAILS",
            new By.ByXPath("//section[contains(@id,'refund-estimation')]//div[contains(@class,'card-collapsable')]//div[contains(@class,'text-sm')]"));
    private static final Locator COLLAPSED_OPEN = new Locator("COLLAPSED_OPEN", By.xpath("//*[contains(text(),'Estimated Refund')]"));
    private static final Locator IS_FORCE_TO_BANK_TRANSFER_CHECKBOX = new Locator("IS_FORCE_TO_BANK_TRANSFER_CHECKBOX", By.xpath("//*[contains(@class,'force_bank')]//*[contains(@class,'check_')]"));
    private static final Locator REFUND_FORM_REFUND_METHOD = new Locator ("REFUND_FORM_REFUND_METHOD", By.xpath("//*[contains(@class,'method_container')]//*[contains(text(),'Refund Method')]/following-sibling::*[contains(@class,'bold')]"));
    private static final Locator REFUND_FORM_PAYLATER_NAME = new Locator ("REFUND_FORM_PAYLATER_NAME", new By.ByXPath("//li[contains(text(),'PayLater Name')]//span"));
    private static final Locator INPUT_ESTIMATION_REFUND = new Locator("INPUT_ESTIMATION_REFUND", new By.ByXPath("//*[contains(@class,'currency-input')]//input"));
    private static final Locator EMPTY_REFUND_HISTORY_TABLE = new Locator("EMPTY_REFUND_HISTORY_TABLE",
            new By.ByXPath("//table[contains(@class,'log-refund-table')]//td[contains(@class,'empty-row')]"));
    private static final Locator REQUEST_REFUND_BUTTON = new Locator("REQUEST_REFUND_BUTTON", By.xpath("//*[contains(text(),'Request Refund')]"));
    private static final Locator BANK_NAME_DROPDOWN = new Locator("BANK_NAME_DROPDOWN", By.xpath("//*[contains(@class,'add_bank')]//*[@id='bank-name']"));
    private static final Locator BANK_NAME_SEARCH = new Locator("BANK_NAME_SEARCH", By.xpath("//*[contains(@class,'search_box')]//label"));
    private static final Locator BANK_NAME_LIST = new Locator("BANK_NAME_LIST", By.xpath("//*[contains(@class,'list_item')]"));
    private static final Locator ACCOUNT_NUMBER_FIELD = new Locator("ACCOUNT_NUMBER_FIELD", By.xpath("//*[contains(@class,'add_bank')]//*[@id='account-number']"));
    private static final Locator CHECK_BUTTON = new Locator("CHECK_BUTTON", By.xpath("//*[contains(@class,'add_bank')]//*[contains(text(),'Check')]"));
    private static final Locator ACCOUNT_HOLDER_NAME = new Locator("ACCOUNT_HOLDER_NAME", By.xpath("//*[contains(@class,'add_bank')]//*[@id='account-holder-name']"));
    private static final Locator SMART_PAY_BUTTON = new Locator("SMART_PAY_BUTTON", By.xpath("//button[normalize-space(.)='Smart Pay']"));
    private static final Locator REFUND_METHOD_DROPDOWN = new Locator("REFUND_METHOD_DROPDOWN",
            By.xpath("//*[@id='select-method']"
                    + " | //*[contains(@class,'DropdownForDisplay_dropdown_form_display_input_wrapper')]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON",
            By.xpath("//*[contains(@class,'SelectBankAccount_button_wrapper')]//button[normalize-space(.)='Save']"
                    + " | //button[contains(@class,'Button_variant_primary') and normalize-space(.)='Save']"));
    private static final Locator SAVED_BANK_ACCOUNT = new Locator("SAVED_BANK_ACCOUNT", By.xpath("//*[contains(@class,'option_smart_pay')]"));
    private static final Locator REFUND_ESTIMATION_DETAILS = new Locator("REFUND_ESTIMATION_DETAILS",
            new By.ByXPath("//section[contains(@id,'refund-estimation')]//ul"));
    private static final Locator REFUND_ESTIMATION_TOTAL = new Locator("REFUND_ESTIMATION_TOTAL",
            new By.ByXPath("//section[contains(@id,'refund-estimation')]//p[@class='primary-refund-method']"));
    private static final Locator REFUND_FORM_BLIBLIPAY_PN = new Locator ("REFUND_FORM_BLIBLIPAY_PN", By.xpath("//*[contains(@class,'method_container')]//*[contains(text(),'Phone Number')]/following-sibling::*[contains(@class,'bold')]"));
    private static final Locator PHONE_NUMBER = new Locator("PHONE_NUMBER", new By.ByXPath("//span[contains(@class,'break-all') and starts-with(normalize-space(),'+')]"));
    private static final Locator BOOKING_CODE_VALUE = new Locator("BOOKING_CODE_VALUE_LOCATOR", new By.ByXPath("(//span[contains(@class, 'outline-0') and contains(@class, '_weight_bold')])[2]"));
    private static final Locator NAME_LOCATOR = new Locator("NAME_IN_ACCOUNT_DETAILS", new By.ByXPath("//*[@data-testid='account-info']//span[contains(@class,'block')][1]"));
    private static final Locator EMAIL_LOCATOR = new Locator("EMAIL_IN_ACCOUNT_DETAILS", new By.ByXPath("//*[@data-testid='account-info']//span[contains(@class,'pr-[8px]')][1]"));
    private static final Locator ISSUE_STATUS_LOCATOR = new Locator("ISSUE_STATUS_ORDER_DETAIL", new By.ByXPath("//*[@data-testid='label-order-id']"));
    private static final Locator ACTION_MENU_LOCATOR = new Locator("ACTION_MENU_LOCATOR", new By.ByXPath("//*[@data-testid='order-detail']"));
    private static final Locator GUEST_DETAILS_TAB_HOTEL = new Locator("GUEST_DETAILS_TAB_HOTEL",
            new By.ByXPath("//*[@data-testid='order-detail']//button[contains(@class,'_tab_')][.//span[normalize-space()='Guest Details' or normalize-space()='Passenger Detail']]"));
    private static final Locator GUEST_DETAILS_TAB_HOMES = new Locator("GUEST_DETAILS_TAB_HOMES",
            new By.ByXPath("(//*[text()='Guest Details'])[1]"));
    private static final Locator GUEST_DETAILS_TAB_AIRPORT_TRANSFER = GUEST_DETAILS_TAB_HOTEL;
    private static final Locator PASSENGER_DETAIL_TAB_BUS = new Locator("PASSENGER_DETAIL_TAB_BUS",
            new By.ByXPath("//*[@data-testid='order-detail']//*[@data-testid='detail-tab']//button[contains(@class,'Tabs_tab')][.//span[normalize-space()='Passenger Details']]"));
    private static final Locator PASSENGER_DETAIL_TAB_FLIGHT = new Locator("PASSENGER_DETAIL_TAB_FLIGHT",
            new By.ByXPath("//*[@data-testid='order-detail']//*[@data-testid='detail-tab']//button[contains(@class,'Tabs_tab')][.//span[normalize-space()='Passenger Detail' or normalize-space()='Passenger Details']]"));
    private static final Locator SCHEDULED_FLIGHT_LOCATOR = new Locator("SCHEDULED_FLIGHT_LOCATOR", new By.ByXPath("//*[@data-testid='order-detail']//span[contains(normalize-space(.), '•') and contains(normalize-space(.), 'Operated by')]"));
    private static final Locator SCHEDULED_HOTEL_LOCATOR = new Locator("SCHEDULED_HOTEL_LOCATOR", new By.ByXPath("//*[@data-testid='order-detail']//span[contains(@class,'mb-[8px]') and contains(@class,'inline-block')]"));
    private static final Locator NAME_PASSENGER_DETAIL_LOCATOR = new Locator( "NAME_PASSENGER_DETAIL_LOCATOR", new By.ByXPath("(//*[text()='Full Name'])/following-sibling::*[1]"));
    private static final Locator NAME_PASSENGER_DETAIL_HOTEL_LOCATOR = new Locator( "NAME_PASSENGER_DETAIL_HOTEL_LOCATOR", new By.ByXPath("//*[text()='Guest Details']/following-sibling::*//li//span")); 
    private static final Locator GREEN_BAR_LOCATOR = new Locator( "GREEN_BAR_LOCATOR", new By.ByXPath("(//*[@class='flash '])"));
    private static final Locator ORDER_DETAIL_ID_LOCATOR = new Locator( "ORDER_DETAIL_ID_LOCATOR", new By.ByXPath("(//*[text()='Order Detail ID'])[2]/following-sibling::*[1]"));
    private static final Locator ORDER_DETAIL_ID_HOTEL_LOCATOR = new Locator( "ORDER_DETAIL_ID_HOTEL_LOCATOR", new By.ByXPath("(//span[contains(@class,'outline-0')])[1]"));
    private static final Locator PRICE_DETAIL_LEGACY_INLINE = new Locator("PRICE_DETAIL_LEGACY_INLINE", new By.ByXPath("(//div[@class='item-detail'])[7]"));
    private static final Locator PRICE_DETAIL_FLIGHT_INVOICE_AMOUNT = new Locator("PRICE_DETAIL_FLIGHT_INVOICE_AMOUNT",
            new By.ByXPath("//*[@data-testid='order-detail']//*[@data-testid='dynamic-transaction']//span[normalize-space()='Invoice']/following-sibling::span[1]"));
    private static final Locator PRICE_DETAIL_FLIGHT_INVOICE_AMOUNT_HIGH_EMPHASIS = new Locator("PRICE_DETAIL_FLIGHT_INVOICE_AMOUNT_HIGH_EMPHASIS",
            new By.ByXPath("//*[@data-testid='order-detail']//*[@data-testid='dynamic-transaction']//span[normalize-space()='Invoice']/following-sibling::span[contains(@class,'highEmphasis')]"));
    private static final Locator PAYMENT_METHOD_LOCATOR = new Locator("PAYMENT_METHOD_LOCATOR",
            new By.ByXPath("//*[@data-testid='dynamic-transaction'][.//*[normalize-space()='Amount']]//div[contains(@class,'item-detail')]/span[2]"));
    private static final Locator PAYMENT_METHOD_LOCATOR_HOMES = new Locator("PAYMENT_METHOD_LOCATOR_HOMES",
            new By.ByXPath("//*[@data-testid='order-detail']//div[contains(@class,'Tabs_tab_panel') and not(@hidden)]//*[@data-testid='dynamic-transaction'][.//span[normalize-space()='Amount']]//span[contains(normalize-space(),'Paid Amount')]/following-sibling::span//div[contains(@class,'item-detail')]/span[2]"));
    private static final Locator PAYMENT_METHOD_LOCATOR_FLIGHT = new Locator("PAYMENT_METHOD_LOCATOR_FLIGHT",
            new By.ByXPath("//*[@data-testid='order-detail']//div[contains(@class,'Tabs_tab_panel') and not(@hidden)]//*[@data-testid='dynamic-transaction'][.//span[normalize-space()='Amount']]//div[contains(@class,'item-detail')]/span[2]"));
    private static final Locator SCHEDULED_TRAIN_LOCATOR = new Locator("SCHEDULED_TRAIN_LOCATOR", new By.ByXPath("//*[text()='Train Name']/following-sibling::*[1]"));
    private static final Locator SCHEDULED_CAR_LOCATOR = new Locator("SCHEDULED_CAR_LOCATOR", new By.ByXPath("//span[following-sibling::div[contains(@class,'copy-wrapper')]//textarea[contains(.,'Car Model')]]"));
    private static final Locator SCHEDULED_AIRPORT_TRANSFER_CAR_LOCATOR = new Locator("SCHEDULED_AIRPORT_TRANSFER_CAR_LOCATOR",
            new By.ByXPath("//*[@data-testid='order-detail-airport_transfer']//div[contains(@class,'justify-between')]/span[1]"));
    private static final Locator SCHEDULED_BUS_LOCATOR = new Locator("SCHEDULED_BUS_LOCATOR", new By.ByXPath("//*[text()='Bus Name and Class']/following-sibling::*[1]"));
    private static final Locator NAME_PASSENGER_DETAIL_LOCATOR_TRAIN = new Locator("NAME_PASSENGER_DETAIL_LOCATOR_TRAIN",
            new By.ByXPath("(//*[@data-testid='passenger-detail']//span[contains(@class,'leading-[20px]')])[1]"));
    private static final Locator NAME_PASSENGER_DETAIL_LOCATOR_AT = new Locator("NAME_PASSENGER_DETAIL_LOCATOR_AT", new By.ByXPath("//*[text()='Full Name']/following-sibling::*"));
    private static final Locator NAME_PASSENGER_DETAIL_LOCATOR_CAR = new Locator("NAME_PASSENGER_DETAIL_LOCATOR_CAR", new By.ByXPath("(//*[text()='Traveller Info']/following-sibling::*[1]//div[1])[3]"));
    private static final Locator NAME_PASSENGER_DETAIL_LOCATOR_FERRY = new Locator("NAME_PASSENGER_DETAIL_LOCATOR_FERRY", new By.ByXPath("//*[@class='flex items-center']//div"));
    private static final Locator ORDER_DETAIL_ID_CAR_LOCATOR = new Locator("ORDER_DETAIL_ID_CAR_LOCATOR", new By.ByXPath("//*[@data-testid='label-order-id']"));
    private static final Locator VIEW_E_TICKET_VOUCHER_LOCATOR = new Locator("VIEW_E_TICKET_VOUCHER_LOCATOR", new By.ByXPath("//*[text()='View Voucher']"));
    private static final Locator RESEND_VOUCHER_LOCATOR = new Locator("RESEND_VOUCHER_LOCATOR", new By.ByXPath("//*[text()='Resend Voucher']"));
    private static final Locator RECIPIENT_TYPE_DROPDOWN_LOCATOR = new Locator("RECIPIENT_TYPE_DROPDOWN_LOCATOR",
            new By.ByXPath("//*[normalize-space()='Recipient Type']/following::div[contains(@class,'icon_container')][1]"));
    private static final Locator CHOOSE_LANGUAGE_DROPDOWN_LOCATOR = new Locator("CHOOSE_LANGUAGE_DROPDOWN_LOCATOR",
            new By.ByXPath("//*[normalize-space()='Choose Language']/following::div[contains(@class,'icon_container')][1]"));
    private static final Locator CUSTOMER_EMAIL_LOCATOR = new Locator("CUSTOMER_EMAIL_LOCATOR", new By.ByXPath("//*[text()='Customer email']"));
    private static final Locator OTHER_EMAIL_LOCATOR = new Locator("OTHER_EMAIL_LOCATOR", new By.ByXPath("//*[text()='Other Email']"));
    private static final Locator ENGLISH_LOCATOR = new Locator("ENGLISH_LOCATOR", new By.ByXPath("//*[text()='English']"));
    private static final Locator SUBMIT_BUTTON_LOCATOR = new Locator("SUBMIT_BUTTON_LOCATOR", new By.ByXPath("//*[text()='Submit']"));
    private static final Locator RESEND_VOUCHER_SUBMIT_BUTTON = new Locator("RESEND_VOUCHER_SUBMIT_BUTTON",
            new By.ByXPath("//button[@data-testid='submit-btn' and normalize-space()='Resend Voucher']"));
    private static final Locator ORDER_DETAIL_ID_TRAIN_LOCATOR = new Locator("ORDER_DETAIL_ID_LOCATOR", new By.ByXPath("(//span[contains(@class,'outline-0')])[2]"));
    private static final Locator GUEST_DETAIL_LOCATOR = new Locator("GUEST_DETAIL_LOCATOR", new By.ByXPath("(//*[text()='Guest Details'])"));
    private static final Locator VISITOR_NAME_LOCATOR = new Locator("VISITOR_NAME_LOCATOR", new By.ByXPath("(//*[text()='Visitor Name'])"));
    private static final Locator ORDER_DETAIL_ID_TTD_LOCATOR = new Locator("ORDER_DETAIL_ID_TTD_LOCATOR", new By.ByXPath("//*[@data-testid='label-order-id']"));
    private static final Locator VISITOR_FULL_NAME_TTD = new Locator("VISITOR_FULL_NAME_TTD",
            new By.ByXPath("//*[@data-testid='contact-details']//span[normalize-space()='Full Name']/following-sibling::span"));
    private static final Locator ORDER_DETAIL_ID_BUS_LOCATOR = new Locator("ORDER_DETAIL_ID_BUS_LOCATOR", new By.ByXPath("//*[text()='Order Detail ID']/following-sibling::*//span"));
    private static final Locator VENDOR_EMAIL_LOCATOR = new Locator("VENDOR_EMAIL_LOCATOR", new By.ByXPath("//*[text()='Vendor']"));
    private static final Locator EMAIL_FIELD_LOCATOR = new Locator("EMAIL_FIELD_LOCATOR", new By.ByXPath("//*[text()='Email']/preceding-sibling::*[1]"));
    private static final Locator VIEW_RECEIPT_LOCATOR = new Locator("VIEW_RECEIPT_LOCATOR", new By.ByXPath("//*[contains(text(),'View Receipt')]"));
    private static final Locator RESEND_RECEIPT_LOCATOR = new Locator("RESEND_RECEIPT_LOCATOR", new By.ByXPath("//*[text()='Resend Receipt']"));
    private static final Locator RESEND_RECEIPT_SUBMIT_LOCATOR = new Locator("RESEND_RECEIPT_SUBMIT_LOCATOR", new By.ByXPath("//button[@data-testid='submit-btn'][.//span[normalize-space()='Resend Receipt']]"));
    private static final Locator LOG_HISTORY_LOCATOR = new Locator("LOG_HISTORY_LOCATOR", new By.ByXPath("//*[text()='Log History']"));
    private static final Locator EDIT_BOOKING_LOCATOR = new Locator("EDIT_BOOKING_LOCATOR", new By.ByXPath("//button[@data-testid='button-EDIT_BOOKING']"));
    private static final Locator EDIT_ROOM_RADIO_BUTTON_LOCATOR = new Locator("EDIT_ROOM_RADIO_BUTTON_LOCATOR", new By.ByXPath("//div[@data-testid='popup-edit-booking']"));
    private static final Locator SUBMIT_LOCATOR = new Locator("SUBMIT_LOCATOR", new By.ByXPath("//*[text()='Submit']"));
    private static final Locator INSURANCE_SECTION_LOCATOR = new Locator("INSURANCE_SECTION_LOCATOR", new By.ByXPath("//span[normalize-space()='Extra Protections']"));
    private static final Locator MORE_IN_INSURANCE_SECTION_LOCATOR = new Locator("MORE_IN_INSURANCE_SECTION_LOCATOR", new By.ByXPath("//*[@data-testid='more-btn']"));
    private static final Locator SEE_COVERAGE_AND_VALUE_LOCATOR = new Locator("SEE_COVERAGE_AND_VALUE_LOCATOR", new By.ByXPath("//*[text()='See Coverage & Value']"));
    private static final Locator SEE_PROTECTION_DETAIL_LOCATOR = new Locator("SEE_PROTECTION_DETAIL_LOCATOR", new By.ByXPath("//*[text()='See Protection Details']"));
    private static final Locator RESEND_POLICY_LOCATOR = new Locator("RESEND_POLICY_LOCATOR", new By.ByXPath("//*[text()='Resend Policy']"));
    private static final Locator EMAIL_IN_RESEND_POLICY = new Locator("EMAIL_IN_RESEND_POLICY", new By.ByXPath("//input[@data-testid='resend-form-email']"));
    private static final Locator RESEND_BUTTON_LOCATOR_POLICY = new Locator("RESEND_BUTTON_LOCATOR_POLICY", new By.ByXPath("//*[text()='Resend']"));
    private static final Locator RESEND_BUTTON_LOCATOR = new Locator("RESEND_BUTTON_LOCATOR", new By.ByXPath("//*[text()='Resend']"));
    private static final Locator ORDER_DETAIL_REFUND_FORM = new Locator("ORDER_DETAIL_REFUND_FORM", new By.ByXPath("//section[@id='order-detail']//ul"));
    private static final Locator ORDER_DETAIL_LIST_REFUND_FORM = new Locator("ORDER_DETAIL_LIST_REFUND_FORM", new By.ByXPath("//section[@id='order-detail']//ul//li"));
    private static final Locator REFUND_100P_LOGO_REFUND_FORM = new Locator("REFUND_100P_LOGO_REFUND_FORM", new By.ByXPath("//section[@id='order-detail']//ul/li/img"));
    private static final Locator DETAIL_FLIGHT_FR_DEPART = new Locator("DETAIL_FLIGHT_FR_DEPART", new By.ByXPath("//section[@id='order-detail']//li[text()='Depart']"));
    private static final Locator DETAIL_FLIGHT_FR_RETURN = new Locator("DETAIL_FLIGHT_FR_RETURN", new By.ByXPath("//section[@id='order-detail']//li[text()='Return']"));
    private static final Locator PRIMARY_REFUND_AMOUNT = new Locator("PRIMARY_REFUND_AMOUNT", new By.ByXPath("//*[contains(text(),'Total Estimation Refund')]/following-sibling::*"));
    private static final Locator TNC_BUTTON_REFUND_FORM = new Locator("TNC_BUTTON_REFUND_FORM", new By.ByXPath("//span[normalize-space()='Terms and Conditions' and contains(@class,'font-bold')]"));
    private static final Locator TNC_BUTTON_SFR = new Locator("TNC_BUTTON_SFR", new By.ByXPath("//span[text()='Terms and Conditions']"));
    private static final Locator TNC_DESC_REFUND_FORM = new Locator("TNC_DESC_REFUND_FORM", new By.ByXPath("//div[contains(@class, 'content')]//h2"));
    private static final Locator TNC_TOGGLE_CLOSE = new Locator("TNC_TOGGLE_CLOSE", new By.ByXPath("//section[contains(@class,'Modal_modal_popup_small') and .//h2[contains(normalize-space(),'Terms & Conditions')]]//button[@data-testid='modal-close-button']"));
    private static final Locator FLASH_SUCCESS_SUBMIT_REFUND = new Locator("FLASH_SUCCESS_SUBMIT_REFUND", new By.ByXPath("//div[contains(@class,'Snackbar_type_success')]"));
    private static final Locator FLASH_ERROR_SUBMIT_REFUND = new Locator("FLASH_ERROR_SUBMIT_REFUND", new By.ByXPath("//div[contains(@class,'flash error')]"));
    private static final Locator SUBMIT_REQUEST_REFUND_BUTTON = new Locator("SUBMIT_REQUEST_REFUND_BUTTON", new By.ByXPath("//button[normalize-space()='Submit Request']"));
    private static final Locator NOTE_SUBMIT_REFUND = new Locator("NOTE_SUBMIT_REFUND", new By.ByXPath("//textarea[@id='enter-note']"));
    private static final Locator TTD_VALIDITY_DATE_LABEL = new Locator("TTD_VALIDITY_DATE_LABEL", new By.ByXPath("//span[normalize-space(.)='Validity Date' and contains(@class,'lowEmphasis')]"));
    private static final Locator TTD_VALIDITY_DATE = new Locator("TTD_VALIDITY_DATE", new By.ByXPath("(//span[normalize-space(.)='Validity Date'])[1]/following::span[contains(@class,'highEmphasis')][1]"));
    private static final Locator CAR_START_DATE = new Locator("CAR_START_DATE", new By.ByXPath("//div[contains(text(),'Start Date')]/following-sibling::div"));
    private static final Locator CAR_VENDOR = new Locator("CAR_VENDOR", new By.ByXPath("//div[contains(text(),'Vendor')]/following-sibling::div//p"));
    private static final Locator AIRLINE_NAME = new Locator("AIRLINE_NAME", new By.ByXPath("//ul[contains(@class,'list-none leading-[20px]')]/li[1]"));
    private static final Locator PASSENGER_REFUND_FORM = new Locator("PASSENGER_REFUND_FORM",new By.ByXPath("//fieldset[contains(@class,'grid-cols-2')]//label[contains(@class,'Check_check')]" + " | //div[contains(@class,'content-passenger')]"));
    private static final Locator FLIGHT_TRIP_TYPE = new Locator("FLIGHT_TRIP_TYPE",new By.ByXPath("(//span[contains(@class,'items-center') and contains(@class,'Text_variant_highEmphasis') and contains(@class,'Text_weight_bold')])[1]"));
    private static final Locator RELATED_ORDERS = new Locator("RELATED_ORDERS", new By.ByXPath("//*[contains(text(),'Related Orders')]/following-sibling::*//a[contains(@href,'/admin/care')]"));
    private static final Locator STATUS_LOCATOR = new Locator("STATUS_LOCATOR", new By.ByXPath("//div[@data-testid='label-status']"));
    private static final Locator BACK_TO_ORDER_LIST_LOCATOR = new Locator("BACK_TO_ORDER_LIST_LOCATOR", new By.ByXPath("//div[@class='back-button']"));
    private static final Locator SFR_CALCULATE_BUTTON = new Locator("SFR_CALCULATE_BUTTON", new By.ByXPath("//button[text()='Calculate']"));
    private static final Locator REFUND_FORM_SKELETON = new Locator("REFUND_FORM_SKELETON", By.xpath("//*[contains(@class,'placeholder_wrapper')]"));
    private static final Locator VERTICAL_DROPDOWN_CONTAINER = new Locator("VERTICAL_DROPDOWN_CONTAINER", new By.ByXPath("//div[@role='button' and .//span[normalize-space(text())='Vertical']]"));
    private static final Locator ALL_ROWS_VERTICAL_VALUE = new Locator("ALL_ROWS_VERTICAL_VALUE", new By.ByXPath("//tbody/tr/td[4]//p"));
    private static final Locator DROPDOWN_SEARCH_INPUT = new Locator("DROPDOWN_SEARCH_INPUT", new By.ByXPath("//div[.//svg[contains(@class,'lucide-search')]]//input | //input[contains(@placeholder,'Search')]"));
    private static Locator getVerticalCheckboxLocator(String name) {
        return new Locator("CHECKBOX_" + name.toUpperCase().replace(" ", "_"),
                By.xpath(String.format("//div[@role='option' and (@data-value='%s' or .//span[text()='%s'])]//label", name, name)));
    }

    private static final String EREFUND_MODAL_XPATH = "//section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='e-Refund by CS']]";
    private static final Locator EREFUND_MODAL = new Locator("EREFUND_MODAL", By.xpath(EREFUND_MODAL_XPATH));
    private static final Locator EREFUND_REASON_INPUT = new Locator("EREFUND_REASON_INPUT", By.xpath(EREFUND_MODAL_XPATH + "//input[@id='reason-for-refund']"));
    private static final Locator EREFUND_ESTIMATION_SECTION = new Locator("EREFUND_ESTIMATION_SECTION",
            By.xpath(EREFUND_MODAL_XPATH + "//p[normalize-space()='Total Estimation Amount']/ancestor::div[contains(@class,'rounded-[8px]')][1]"));
    private static final Locator EREFUND_SEE_DETAILS = new Locator("EREFUND_SEE_DETAILS",
            By.xpath(EREFUND_MODAL_XPATH + "//div[@role='button'][.//p[normalize-space()='See Details']]"));
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";

    private Locator dynamicOrderId(String orderId) {
        return new Locator("DYNAMIC_ORDER_ID",
                new By.ByXPath(String.format("//*[@data-testid='label-order-id' and contains(normalize-space(.), '%s')]", orderId)));
    }

    private Locator dynamicRefundMethodOption(String methodName) {
        return new Locator("REFUND_METHOD_OPTION_" + methodName.toUpperCase().replace(' ', '_'),
                new By.ByXPath(String.format(
                        "//*[contains(@class,'RefundMethodItemPanel_method_item') and @role='button' and .//*[normalize-space(text())='%s']]",
                        methodName)));
    }
    private Locator tableIssuedStatus(String column) {
        if (!"Order Detail ID".equals(column) && !"Order Details ID".equals(column)) {
            throw new IllegalArgumentException("tableIssuedStatus only supports Order Detail ID or Order Details ID, got: " + column);
        }
        return new Locator(
                "TABLE_ISSUED_STATUS",
                new By.ByXPath(
                        "(//td[@data-testid='rebooking-table'])[1]"
                        + " | //*[@data-testid='label-order-id']/following::table[contains(@class,'table-general')][1]"
                        + "//tbody/tr[1]/td[1]"));
    }
    private Locator tableTicketDetailTodo(String column) {
        return new Locator("TABLE_TICKET_DETAIL_TODO", 
        By.xpath("//*[contains(@class,'data-list-tables')]//tr[1]//td[position()=count(//*[contains(@class,'data-list-tables')]//th/span[text()='"+column+"']/parent::*/preceding-sibling::th)+1]"));
    }
    private Locator priceSummaryFareCollapsed(int index) {
        return new Locator("PRICE_SUMMARY_FARE_COLLAPSED",
                new By.ByXPath(String.format("(//div[@data-testid='price-segments'])[%d]", index)));
    }
    private Locator addNoteCategoryDropdownValue(String value) {
        return new Locator("ADD_NOTE_CATEGORY_DROPDOWN_VALUE",
                new By.ByXPath(String.format(
                        "//*[contains(@data-testid,'value-dropdown-option')][contains(normalize-space(.), '%s')] | "
                                + "//div[contains(@class,'dropdown-option')][contains(normalize-space(.), '%s')]",
                        value, value)));
    }
    private Locator getRefundFormReasonByIndex(String index) {
        return new Locator("REFUND_FORM_REASON_"+index, By.xpath("//*[contains(@data-testid,'value-dropdown-option-"+index+"')]"));
    }
    private Locator getRefundFormEstimationDetails(int section) {
        return new Locator("REFUND_FORM_ESTIMATION_DETAILS_SECTION",
                new By.ByXPath(String.format("//section[contains(@id,'refund-estimation')]//div[contains(@class,'card-collapsable')]//div[contains(@class,'text-sm')][%d]", section)));
    }
    private Locator getRefundFormMultipleEstimationDetails(int section) {
        return new Locator("REFUND_FORM_MULTIPLE_ESTIMATION_DETAILS_SECTION",
                new By.ByXPath(String.format("//section[contains(@id,'refund-estimation')]//div[contains(@class,'card-collapsable')]//div[contains(@class,'text-sm')][%d]//li//p[1]", section)));
    }
    private Locator getLocatorHotelDate(String type) {
        return new Locator("HOTEL_DATE", new By.ByXPath("//span[contains(text(),'"+type+"')]/following-sibling::div[contains(@class,'subtitle-wrapper')]//span[normalize-space()!=''][1]"));
    }
    private Locator getPassengerCheckBox(int index) {
        return new Locator("PASSENGER_CHECKBOX",
                new By.ByXPath(String.format("(//label[contains(@class,'Check_check_')]//span[contains(@class,'Check_custom_checkbox')])[%d]"
                        + " | //div[contains(@class,'content-passenger')][%d]//span[contains(@class,'check-mark')]", index, index)));
    }
    private Locator getPassengerDropdownReason(int index) {
        return new Locator("PASSENGER_DROPDOWN_REASON",
                new By.ByXPath(String.format("(//fieldset[contains(@class,'grid-cols-2')]/div[contains(@class,'gap-2')])[%d]//input[@id='reason-for-refund']"
                        + " | //div[contains(@class,'content-passenger')][%d]//div[contains(@id,'passenger-refund-reason')]", index, index)));
    }
    private Locator getPassengerDropdownReasonValues(int index) {
        return new Locator("PASSENGER_DROPDOWN_REASON_VALUES",
                new By.ByXPath(String.format("//*[contains(@data-testid,'value-dropdown-option')]"
                        + " | //div[contains(@class,'content-passenger')][%d]//div[contains(@class,'container-options')]//label", index)));
    }
    private Locator getPassengerDropdownReasonValueAt(int paxIndex, int optionIndex) {
        return new Locator("PASSENGER_DROPDOWN_REASON_VALUE_AT_" + optionIndex,
                new By.ByXPath(String.format("(//*[contains(@data-testid,'value-dropdown-option')] | //div[contains(@class,'content-passenger')][%d]//div[contains(@class,'container-options')]//label)[%d]", paxIndex, optionIndex)));
    }
    private Locator refundFeesChargedCalculation(String breakdownItem) {
        return new Locator("REFUND_FEES_CHARGED_CALCULATION", By.xpath("//*[@data-testid='collapse-container'][@aria-hidden='false']//*[contains(text(),'Fees Charged')]/following-sibling::*//*[contains(text(),'"+breakdownItem+"')]/following-sibling::*"));
    }
    private Locator refundPaymentCalculation(String breakdownItem) {
        return new Locator("REFUND_PAYMENT_CALCULATION", By.xpath("//*[@data-testid='collapse-container'][@aria-hidden='false']//*[contains(text(),'Payment')]/following-sibling::*//*[contains(text(),'"+breakdownItem+"')]/following-sibling::*"));
    }
    private Locator estimatedRefundCalculation(String refundMethod) {
        return new Locator("ESTIMATED_REFUND_CALCULATION",
                By.xpath("(//*[normalize-space(text())='"+refundMethod+"']/ancestor::li[1]//p[contains(@class,'items-center')])[1]"
                        + " | //*[@data-testid='collapse-container'][@aria-hidden='false']//*[contains(text(),'Estimated Refund')]/following-sibling::*//*[contains(text(),'"+refundMethod+"')]/following-sibling::*"));
    }
    private Locator refundHistoryDiscountColumnValue(String discountType) {
        return new Locator("REFUND_HISTORY_DISCOUNT_COLUMN_VALUE",
                new By.ByXPath(String.format("//table[contains(@class,'log-refund-table')]//td//div[contains(text(),'Created')]/parent::td/preceding-sibling::td[2]//div[contains(text(),'%s')]", discountType)));
    }
    private static final Locator logHistoryStatusLocator(String type) {
        return new Locator("DYNAMIC_LOG_HISTORY_STATUS_LOCATOR", new By.ByXPath("//*[text()='"+ type +"']"));
    }
    private Locator getAirportName(String trip) {
        return new Locator("AIRPORT_NAME",
                new By.ByXPath("(//span[contains(text(),'Airport of "+trip+"')]/following::span[contains(@class,'Text_variant_highEmphasis')])[1]"
                        + " | //span[contains(text(),'Airport of "+trip+"')]/following-sibling::span/span[1]"));
    }
    private Locator getFlightTripDateTime(String trip) {
        return new Locator("TRIP_DATE_TIME", new By.ByXPath("//span[contains(text(),'"+trip+" Time')]/following-sibling::span/span"));
    }
    private Locator getSpecialReasonRefund(String name) {
        return new Locator("SPECIAL_REASON_"+name.toUpperCase(), new By.ByXPath("//*[contains(@data-testid,'dropdown-option')]//*[contains(text(),'"+name+"')]"));
    }
    private Locator getSFRPriceBreakdownItem(String item) {
        return new Locator("SFR_ITEM_"+item.toUpperCase(), new By.ByXPath("//p[contains(text(),'"+item+"')]/following-sibling::p"));
    }
    private Locator getFRPriceBreakdowns(String item) {
        return new Locator("FR_BREAKDOWN_"+item.toUpperCase(), By.xpath("//*[normalize-space(text())='"+item+"']/following-sibling::ul/li/descendant::*[not(*) and normalize-space()!=''][last()]"));
    };
    private Locator scheduledTtdLocator(String category) {
        String categoryLabel = CoreFunctions.titleize(category.replace('_', ' ').toLowerCase());
        return new Locator("SCHEDULED_TTD_LOCATOR",
                By.xpath("//span[normalize-space()='" + categoryLabel + " Detail']/parent::*"));
    }
    private Locator getTabByName(String tabName) {
        return new Locator("TAB_"+tabName.toUpperCase(), By.xpath("//*[contains(@class,'_tab_list')]//*[contains(text(),'"+tabName+"')]"));
    }
    private final Pattern currencyPattern = Pattern.compile("\\b([A-Z]{3})\\b");
    private static String url100pLogo = "https://s-gk.tiket.photos/t/01E25EBZS3W0FY9GTG6C42E1SE/original/tix-insurance-core/2023/06/08/e8edae11-7ce2-44a8-844f-7f220ff9598a-1686215364955-4b3c5f34bd97eca0946b0cb48cc2b75d.png";
    Set<String> multiDetailVerticals = new HashSet<>(Arrays.asList("Flight", "Hotel", "Homes", "Train", "Todo"));
    Set<String> singleDetailVerticals = new HashSet<>(Arrays.asList("Car Rental", "Airport Transfer"));
    private static final Map<String, String> mapDiscountType = Map.of(
            "PROMOCODE", "Promo code",
            "GIFTCARD", "Gift Voucher",
            "TIKETPOINT", "Blibli Tiket Points"
    );
    private static final String digitPattern = "[^\\d-]";

    public AdminCareDetailPage(Session<T> session) {
        super(session);
    }

    public boolean isUserInAdminCareDetailPage(){
        driverTools.waitForElementToAppear(TITLE_ORDER_DETAIL);
        return driverTools.isElementDisplayed(TITLE_ORDER_DETAIL);
    }

    public String getUserInfoAccount(int index){ //0 for email, 1 for number
        driverTools.waitForElementToAppear(USER_INFO_LIST);
        List<WebElement> userInfo = driverTools.elements(USER_INFO_LIST).orElseThrow();
        String userAccount = driverTools.getText(userInfo.get(index)).split("\n")[0];
        return userAccount;
    }

    public String getOrderDetailByVertical(String vertical) {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        String orderDetail = "";
        String[] parts = {""};
        if (multiDetailVerticals.contains(vertical)) {
            List<WebElement> orderDetails;
            if (driverTools.isElementDisplayed(ORDER_DETAIL_CARD)) {
                driverTools.scrollIntoWebView(ORDER_DETAIL_CARD);
                orderDetails = driverTools.elements(ORDER_DETAIL_MULTI).get();
            }else {
                driverTools.scrollIntoWebView(ORDER_DETAIL_SINGLE);
                orderDetails = driverTools.elements(ORDER_DETAIL_MULTI_OLD).get();
            }
            orderDetail = driverTools.getText(orderDetails.get(0));
            parts = orderDetail.split("\\D+");
        } else if (singleDetailVerticals.contains(vertical)) {
            orderDetail = driverTools.getText(ORDER_DETAIL_SINGLE).get();
            parts = orderDetail.split("#|-");
        }
        orderDetail = parts[parts.length-1].trim();
        logger.log("got value Order Detail ID: "+orderDetail);
        return orderDetail;
    }

    public String getOrderId(){
        driverTools.waitForElementToDisappear(LOADING_STATE);
        WebElement orderData = driverTools.waitForElementToAppear(ORDER_DETAIL_SINGLE).orElseThrow();
        String orderDataString = driverTools.getText(orderData);

        Pattern regexPattern = Pattern.compile("Order ID #\\s*(\\d+)");
        Matcher matcher = regexPattern.matcher(orderDataString);
        if (matcher.find()) {
            orderDataString = matcher.group(1);
        }
        
        logger.log("Get Order Id -> "+orderDataString);
        return orderDataString;
    }

    public String getDataTableIssuedStatus(String columnName) {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        return driverTools.getText(tableIssuedStatus(columnName)).orElseThrow();
    }

    public String getHotelDate (String type) {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        driverTools.waitForElementToAppear(getLocatorHotelDate(type));
        String hotelDate = driverTools.getText(getLocatorHotelDate(type)).orElseThrow();
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("E, dd MMM yyyy");
            LocalDate date = LocalDate.parse(hotelDate, inputFormatter);

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("E, d MMM yyyy");
            String formattedDate = outputFormatter.format(date);
            logger.log(type+" date: "+formattedDate);
            return formattedDate;
        } catch (DateTimeParseException e) {
            logger.log(e.getLocalizedMessage());
            return null;
        }
    }

    public String getValidityDateTodo() {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        driverTools.waitForElementToAppear(TTD_VALIDITY_DATE_LABEL, 3)
                .orElseThrow(() -> new NoSuchElementException("Validity Date label not found on Order Details page"));
        String validityDate = driverTools.waitForElementToAppear(TTD_VALIDITY_DATE, 15)
                .map(el -> el.getText())
                .orElseThrow(() -> new NoSuchElementException("Validity Date value not found on Order Details page"));
        logger.log("Validity Date from order -> "+validityDate);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("E, d MMM yyyy");

        String startPart = validityDate.contains(" - ") ? validityDate.split(" - ", 2)[0].trim() : validityDate.trim();

        for (String pattern : new String[] { "E, dd MMM yyyy", "E, d MMM yyyy" }) {
            try {
                LocalDate date = LocalDate.parse(startPart, DateTimeFormatter.ofPattern(pattern));
                String formattedDate = outputFormatter.format(date);
                logger.log("Validity date formatted -> "+formattedDate);
                return formattedDate;
            } catch (DateTimeParseException ignored) {
            }
        }
        logger.log("Unable to parse validity date: '"+validityDate+"', returning raw value");
        return validityDate;
    }
    
    public String getCarStartDate() {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        String carStartDate = driverTools.getText(CAR_START_DATE).orElseThrow();
        String carStartDateFormatted = carStartDate.split("\n")[0];
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("E, d MMMM yyyy");
            LocalDate date = LocalDate.parse(carStartDateFormatted, inputFormatter);

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("E, d MMM yyyy");
            String formattedDate = outputFormatter.format(date);
            logger.log("Car Rental start date -> "+formattedDate);
            return formattedDate;
        } catch (DateTimeParseException e) {
            logger.log(e.getLocalizedMessage());
            return null;
        }
    }

    public String getCarVendor() {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        String carVendor = driverTools.getText(CAR_VENDOR).orElseThrow();
        logger.log("Car Rental start date -> "+carVendor);
        return carVendor;
    }

    public String getAirlineName() {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        String airlineName = driverTools.getText(AIRLINE_NAME).orElseThrow();
        int index = airlineName.indexOf('(');
        if (index != -1) {
            return airlineName.substring(0, index).trim();
        }
        return airlineName;
    }
    
    public String getFlightAirpotCode(String trip) {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        String airportName = driverTools.getText(getAirportName(trip)).orElseThrow();
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(airportName);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return airportName;
    }

    public String getFlightDateOrTime(String trip, String type) {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        List<WebElement> flightDateTimeList = driverTools.elements(getFlightTripDateTime(trip)).orElseThrow();
        String flightDateTime = "";
        if (flightDateTimeList.size() > 1) {
            switch (type) {
                case "date":
                    flightDateTime = driverTools.getText(flightDateTimeList.get(0));
                    try {
                        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("E, dd MMM yyyy");
                        LocalDate date = LocalDate.parse(flightDateTime, inputFormatter);
            
                        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("E, d MMM yyyy");
                        String formattedDate = outputFormatter.format(date);
                        logger.log("Flight date formatted -> "+formattedDate);
                        return formattedDate;
                    } catch (DateTimeParseException e) {
                        logger.log(e.getLocalizedMessage());
                        return flightDateTime;
                    }
                case "time":
                    flightDateTime = driverTools.getText(flightDateTimeList.get(1));
                    return flightDateTime;
            }
            logger.fail(type+" not matched any case");
        }
        logger.fail("Date Time List element <= 1. size -> "+flightDateTimeList.size());
        return flightDateTime;
    }
    
    public void selectTripType(String trip) {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        WebElement tripHeader = driverTools.waitForElementToAppear(FLIGHT_TRIP_TYPE).orElseThrow();
        driverTools.scrollIntoWebView(tripHeader);
        String flightTrip = driverTools.getText(tripHeader);
        if (!flightTrip.contains(trip)) {
            driverTools.click(RELATED_ORDERS);
        }
    }

    public void goToRefundPage(){
        WebElement menuRefund = driverTools.waitForElementToAppear(MENU_REFUND).orElseThrow();
        driverTools.scrollIntoWebView(menuRefund);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(menuRefund);
    }

    public boolean isOrderIdCorrect(String orderId){
        driverTools.waitForElementToAppear(dynamicOrderId(orderId)).orElseThrow();
        return driverTools.getText(dynamicOrderId(orderId)).get().contains(orderId);
    }

    public void clickTransactionInvoiceTab() {
        Locator tabLocator = getTabByName("Transaction/Invoice");
        for (int attempt = 0; attempt < 3; attempt++) {
            driverTools.waitForElementToDisappear(LOADING_STATE, 5);
            WebElement tab = driverTools.waitForElementToBeClickable(tabLocator, 10)
                    .orElseThrow(() -> new TimeoutException("Timeout waiting for " + tabLocator + " to be clickable"));
            driverTools.scrollIntoWebView(tab);
            try {
                driverTools.click(tabLocator);
            } catch (ElementClickInterceptedException e) {
                logger.log("Transaction/Invoice tab click intercepted, using JS click: " + e.getMessage());
                driverTools.clickWithJavascriptExecutor(tabLocator);
            }
            if (isTransactionInvoicePanelVisible()) {
                return;
            }
            // Selenium reports click success even when React does not switch the tab — force JS click
            logger.log("Transaction/Invoice panel not visible after click, forcing JS click. Attempt: " + (attempt + 1));
            tab = driverTools.waitForElementToAppear(tabLocator, 5)
                    .orElseThrow(() -> new NoSuchElementException(tabLocator + " not present"));
            driverTools.scrollIntoWebView(tab);
            driverTools.clickWithJavascriptExecutor(tabLocator);
            if (isTransactionInvoicePanelVisible()) {
                return;
            }
            logger.log("Transaction/Invoice tab did not open. Attempt: " + (attempt + 1));
        }
        throw new NoSuchElementException("Transaction/Invoice tab did not open after 3 attempts");
    }

    private boolean isTransactionInvoicePanelVisible() {
        return driverTools.waitForElementToAppear(TRANSACTION_AMOUNT_SECTION, 5).isPresent()
                || driverTools.waitForElementToAppear(PRICE_DETAIL_BUTTON, 2).isPresent()
                || driverTools.waitForElementToAppear(PRICE_DETAIL_LEGACY_INLINE, 2).isPresent()
                || driverTools.waitForElementToAppear(ORDER_AMOUNT, 2).isPresent();
    }

    public void clickTransactionInvoiceTabFlight() {
        driverTools.waitForElementToAppear(getTabByName("Transaction/Invoice"));
        driver.navigate().refresh();
        driverTools.waitForElementToAppear(getTabByName("Transaction/Invoice")).orElseThrow();
        driverTools.scrollIntoWebView(getTabByName("Transaction/Invoice"));
        WebElement tab = driverTools.waitForElementToBeClickable(getTabByName("Transaction/Invoice")).orElseThrow();
        try {
            driverTools.click(tab);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(tab);
        }
        driverTools.waitForElementToAppear(PRICE_DETAIL_FLIGHT_INVOICE_AMOUNT, 8).orElseThrow();
    }

    public void clickRefundCancelTab(){
        Locator refundCancelTab = getTabByName("Refund/Cancel");
        WebElement tab = driverTools.waitForElementToAppear(refundCancelTab)
                .orElseThrow(() -> new NoSuchElementException(refundCancelTab + " Button not present"));
        driverTools.waitForElementToDisappear(LOADING_STATE, 5);
        driverTools.scrollToElement(tab);
        driverTools.waitForElementToBeClickable(refundCancelTab, 10)
                .orElseThrow(() -> new TimeoutException("Timeout waiting for " + refundCancelTab + " to be clickable"));
        try {
            driverTools.click(tab);
        } catch (ElementClickInterceptedException e) {
            logger.log("Refund/Cancel tab click intercepted, retrying with JS click: " + e.getMessage());
            driverTools.clickWithJavascriptExecutor(tab);
        }
    }

    public void clickPriceDetailButton() {
        driverTools.waitForElementToAppear(PRICE_DETAIL_BUTTON);
        driverTools.waitForElementToBeClickable(PRICE_DETAIL_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(PRICE_DETAIL_BUTTON);
    }

    /** Price of each pax per journey in the open Price Details modal, e.g. {"Jakarta → Bali": {"Adults (x2)": 3319980, "Infant (x1)": 103506}}. */
    public Map<String, Map<String, Long>> getPriceDetailsPerJourney() {
        Map<String, Map<String, Long>> pricePerJourney = new LinkedHashMap<>();
        for (WebElement journey : priceDetailsScope().findElements(By.xpath(".//div[starts-with(@id,'price-') and @id!='price-segments']"))) {
            String title = journey.findElement(By.xpath("./span[1]")).getAttribute("textContent").trim();
            pricePerJourney.put(title, readPriceRows(journey, ".//div[contains(@class,'inline-flex') and count(span)=2]"));
        }
        return pricePerJourney;
    }

    /** Rows under "Price Deduction" in the open Price Details modal, e.g. {"Discount": 100000, "Promo code": 1331812}. */
    public Map<String, Long> getPriceDeductions() {
        return readPriceRows(priceDetailsScope(), ".//span[normalize-space()='Price Deduction']/following-sibling::ul//div[contains(@class,'inline-flex')]");
    }

    /** The active fare tab (Publish Fare) when the modal has tabs, otherwise the whole modal. */
    private WebElement priceDetailsScope() {
        WebElement modal = driverTools.waitForElementToAppear(PRICE_DETAILS_MODAL).orElseThrow();
        List<WebElement> activePanel = modal.findElements(By.xpath(".//div[contains(@class,'Tabs_tab_panel_active')]"));
        return activePanel.isEmpty() ? modal : activePanel.get(0);
    }

    public void closePriceDetails() {
        driverTools.click(PRICE_DETAILS_MODAL_CLOSE);
        driverTools.waitForElementToDisappear(PRICE_DETAILS_MODAL, 5);
    }

    private Map<String, Long> readPriceRows(WebElement scope, String rowsXpath) {
        Map<String, Long> rows = new LinkedHashMap<>();
        for (WebElement row : scope.findElements(By.xpath(rowsXpath))) {
            String label = row.findElement(By.xpath("./span[1]")).getAttribute("textContent").trim();
            String amount = row.findElement(By.xpath("./span[2]")).getAttribute("textContent").replaceAll("\\D", "");
            rows.put(label, Long.parseLong(amount));
        }
        return rows;
    }

    public void clickSpecialRefundButton() {
        driverTools.waitForElementToAppear(SPECIAL_REFUND_BUTTON).ifPresentOrElse(driverTools::click,
                ()-> {throw new NoSuchElementException("Button not present");}
        );
    }

    public void clickERefundByCsButton() {
        driverTools.waitForElementToAppear(EREFUND_BY_CS_BUTTON).ifPresentOrElse(driverTools::scrollToElement, () -> {throw new NoSuchElementException(EREFUND_BY_CS_BUTTON+" Button not present");});
        driverTools.click(EREFUND_BY_CS_BUTTON);
        if (driverTools.waitForElementToPresence(FIRST_REFUND_POPUP_CONTENT, 2).isPresent()) {
            clickFirstItemToRefund();
            clickRefundPopUpOkButton();
        }
    }

    public void clickAlternateERefundByCsButton() {
        driverTools.waitForElementToAppear(EREFUND_BY_CS_BUTTON_ALTERNATE).ifPresentOrElse(driverTools::scrollToElement, () -> {throw new NoSuchElementException(EREFUND_BY_CS_BUTTON_ALTERNATE+" Button not present");});
        driverTools.click(EREFUND_BY_CS_BUTTON_ALTERNATE);
        if (driverTools.waitForElementToPresence(FIRST_REFUND_POPUP_CONTENT, 2).isPresent()) {
            clickFirstItemToRefund();
            clickRefundPopUpOkButton();
        }
    }


    public void selectRefundReason(String reason) {
        driverTools.waitForElementToAppear(EREFUND_MODAL).orElseThrow(() -> new NoSuchElementException(EREFUND_MODAL + " not present"));
        driverTools.click(EREFUND_REASON_INPUT);
        Locator option = new Locator("EREFUND_REASON_" + reason.toUpperCase(),
                By.xpath("//div[@data-testid='dropdown-options-container']//div[contains(@class,'dropdown-option')][normalize-space()='" + reason + "']"));
        driverTools.waitForElementToAppear(option)
                .orElseThrow(() -> new NoSuchElementException("Refund reason '" + reason + "' not present"));
        driverTools.click(option);
    }

    public void selectRefundPassenger(String passengerName) {
        Locator passenger = new Locator("EREFUND_PASSENGER_" + passengerName.toUpperCase(), By.xpath(EREFUND_MODAL_XPATH
                + "//fieldset//label[.//b[translate(normalize-space(),'" + UPPER + "','" + LOWER + "')='" + passengerName.trim().toLowerCase() + "']]"));
        WebElement label = driverTools.waitForElementToAppear(passenger)
                .orElseThrow(() -> new NoSuchElementException("Refund passenger '" + passengerName + "' not present"));
        WebElement checkbox = label.findElement(By.xpath(".//input[@type='checkbox']"));
        if (checkbox.isSelected()) return;
        try {
            driverTools.click(label);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(label);
        }
        if (!checkbox.isSelected()) driverTools.clickWithJavascriptExecutor(checkbox);
    }

    public void expandRefundEstimationDetails() {
        WebElement section = driverTools.waitForElementToAppear(EREFUND_ESTIMATION_SECTION)
                .orElseThrow(() -> new NoSuchElementException(EREFUND_ESTIMATION_SECTION + " not present"));
        driverTools.scrollToElement(section);
        WebElement seeDetails = driverTools.waitForElementToAppear(EREFUND_SEE_DETAILS, 20)
                .orElseThrow(() -> new NoSuchElementException(EREFUND_SEE_DETAILS + " not present"));
        if (section.findElements(By.xpath(".//p[normalize-space()='Fees Charged']")).isEmpty()) {
            try {
                driverTools.click(seeDetails);
            } catch (ElementClickInterceptedException e) {
                driverTools.clickWithJavascriptExecutor(seeDetails);
            }
        }
        driverTools.waitForElementToAppear(new Locator("EREFUND_FEES_CHARGED",
                By.xpath(EREFUND_MODAL_XPATH + "//p[normalize-space()='Fees Charged']"))).orElseThrow();
    }

    @SneakyThrows
    public Map<String, Long> getRefundEstimationDetails(Map<String, Long> previous) {
        Map<String, Long> last = null;
        for (int attempt = 0; attempt < 15; attempt++) {
            Map<String, Long> current = readRefundEstimationRows();
            boolean stable = current.equals(last);
            boolean changed = previous == null || !current.equals(previous);
            if (stable && changed && !current.isEmpty()) return current;
            last = current;
            Thread.sleep(1000);
        }
        return last;
    }

    private Map<String, Long> readRefundEstimationRows() {
        WebElement section = driverTools.waitForElementToAppear(EREFUND_ESTIMATION_SECTION).orElseThrow();
        Map<String, Long> rows = new LinkedHashMap<>();
        for (WebElement row : section.findElements(By.xpath(".//li//div[contains(@class,'justify-between')][count(p)=2]"))) {
            String label = row.findElement(By.xpath("./p[1]")).getAttribute("textContent").trim();
            String amount = row.findElement(By.xpath("./p[2]")).getAttribute("textContent").replaceAll("\\D", "");
            if (!amount.isEmpty()) rows.put(label, Long.parseLong(amount));
        }
        return rows;
    }

    /** Passenger names of a round trip order, in the order they are booked and later picked in the refund form. */
    public record RefundPassengers(String[] adultNames, String[] infantNames) {
        public int adultCount() {
            return adultNames.length;
        }

        public int infantCount() {
            return infantNames.length;
        }
    }

    public RefundPassengers generateRefundPassengers(int adult, int infant) {
        return new RefundPassengers(generatePassengerNames(adult), generatePassengerNames(infant));
    }

    private String[] generatePassengerNames(int count) {
        String[] names = new String[count];
        for (int i = 0; i < count; i++) names[i] = CoreFunctions.randomNameWithoutTest() + " Testing";
        return names;
    }

    /** Sum of every fare row of every journey in {@link #getPriceDetailsPerJourney()}. */
    public long getFareTotal(Map<String, Map<String, Long>> pricePerJourney) {
        return pricePerJourney.values().stream().flatMap(journey -> journey.values().stream()).mapToLong(Long::longValue).sum();
    }

    /** Fare of one pax of the given type ("Adult"/"Infant") from a journey's rows, e.g. {"Adults (x2)": 3319980} gives 1659990. */
    public double getFarePerPax(Map<String, Long> journeyFares, String paxType) {
        for (Map.Entry<String, Long> fare : journeyFares.entrySet()) {
            if (!fare.getKey().toLowerCase().startsWith(paxType.toLowerCase())) continue;
            String count = fare.getKey().replaceAll("\\D", "");
            return fare.getValue() / (count.isEmpty() ? 1.0 : Double.parseDouble(count));
        }
        return 0;
    }

    /**
     * Picks 1..adultCount adults (each with up to as many infants selected) in the open refund form and verifies the
     * "Promo code" deduction of the refund breakdown follows
     * ((adults x adult fare) + (infants x infant fare)) / fare total x promo deduction.
     */
    public void verifyPromoDeductionPerSelectedPassengers(RefundPassengers passengers, Map<String, Long> journeyFares, long fareTotal, long promoDeduction) {
        double adultFare = getFarePerPax(journeyFares, "Adult");
        double infantFare = getFarePerPax(journeyFares, "Infant");
        int infant = passengers.infantCount();
        Map<String, Long> previousBreakdown = null;
        for (int selectedAdults = 1; selectedAdults <= passengers.adultCount(); selectedAdults++) {
            int selectedInfants = Math.min(selectedAdults, infant);
            String selected = selectedAdults + " adult" + (infant > 0 ? " & " + selectedInfants + " infant" : "");
            logger.step("Pick " + selected + " and verify the promo deduction in the refund breakdown");
            selectRefundPassenger(passengers.adultNames()[selectedAdults - 1]);
            expandRefundEstimationDetails();
            Map<String, Long> breakdown = getRefundEstimationDetails(previousBreakdown);
            double expectedPromo = (selectedAdults * adultFare + selectedInfants * infantFare) / fareTotal * promoDeduction;
            logger.log("Refund breakdown for " + selected + " -> " + breakdown);
            logger.log(String.format("Formula: ((%d x adult %.2f) + (%d x infant %.2f)) / fare total %d x promo %d = expected promo deduction %.2f, actual %d",
                    selectedAdults, adultFare, selectedInfants, infantFare, fareTotal, promoDeduction, expectedPromo, breakdown.get("Promo code")));
            Assertion.assertThat("Promo deduction for " + selected + " follows the formula", (double) breakdown.get("Promo code"), org.hamcrest.Matchers.closeTo(expectedPromo, 1.0));
            previousBreakdown = breakdown;
        }
    }

    public boolean isRefundFormShown() {
        return driverTools.waitForElementToPresence(REFUND_FORM_MODAL, 4).isPresent();
    }

    public int getOrderAmount() {
        WebElement orderAmount = driverTools.waitForElementToAppear(ORDER_AMOUNT).orElseThrow();
        String text = orderAmount.getText().replaceAll(digitPattern, "");
        return Integer.parseInt(text);
    }

    public boolean checkCurrencyPriceDetails(String targetCurrency){
        ArrayList<String> currencyList = getCurrencyPriceDetails();
        return currencyList.stream().allMatch(targetCurrency::equals);
    }

    private ArrayList<String> getCurrencyPriceDetails() {
        WebElement priceSummary = driverTools.waitForElementToAppear(PRICE_SUMMARY_FARE).orElseThrow();
        Optional<WebElement> otherFees = driverTools.waitForElementToAppear(OTHER_FEES_PRICE_DETAIL);
        WebElement totalPayment = driverTools.waitForElementToAppear(TOTAL_PAYMENT_PRICE_DETAIL).orElseThrow();
        ArrayList<String> currencies = new ArrayList<>();
        List<WebElement> fareLists = priceSummary.findElements(FARE_LIST_PRICE_DETAIL.by());
        if (!fareLists.isEmpty()) {
            for (WebElement fareList : fareLists) {
                String fare = fareList.getText();
                Matcher fareMatcher = currencyPattern.matcher(fare);

                while (fareMatcher.find()) {
                    String currencyCode = fareMatcher.group(1);
                    currencies.add(currencyCode);
                }
            }
        }
        if (otherFees.isPresent()) {
            String fee = otherFees.get().getText();
            Matcher feeMatcher = currencyPattern.matcher(fee);
            while (feeMatcher.find()) {
                String currencyCode = feeMatcher.group(1);
                currencies.add(currencyCode);
            }
        }
        String total = totalPayment.getText();
        Matcher totalMatcher = currencyPattern.matcher(total);
        if (totalMatcher.find()) {
            String currencyCode = totalMatcher.group(1);
            currencies.add(currencyCode);
        }
        return currencies;
    }

    public void clickExpandCollapsePriceDetail() throws InterruptedException {
        Optional<WebElement> priceSummary = driverTools.waitForElementToAppear(PRICE_SUMMARY_FARE);
        if (priceSummary.isPresent()) {
            List<WebElement> fareLists = priceSummary.get().findElements(FARE_LIST_PRICE_DETAIL.by());
            if (fareLists.size() > 1) {
                for (int i = 1; i < fareLists.size(); i++) {
                    driverTools.click(priceSummaryFareCollapsed(i+1));
                    Thread.sleep(500);
                }
            }
        }
    }

    public void clickAddNoteButton() {
        WebElement addNoteButton = driverTools.waitForElementToAppear(ADD_NOTE_BUTTON).orElseThrow();
        driverTools.scrollIntoWebView(addNoteButton);
        driverTools.waitForElementToBeClickable(ADD_NOTE_BUTTON).orElseThrow();
        try {
            driverTools.click(addNoteButton);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(addNoteButton);
        }
        driverTools.waitForElementToAppear(ADD_NOTE_MODAL, 8).orElseThrow();
    }

    public boolean isAddNoteModalAppear() {
        driverTools.waitForElementToAppear(ADD_NOTE_MODAL).orElseThrow();
        return driverTools.isElementDisplayed(ADD_NOTE_MODAL);
    }

    public void selectAddNoteCategoryDropdown() {
        driverTools.waitForElementToAppear(ADD_NOTE_CATEGORY_DROPDOWN).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(ADD_NOTE_MODAL).orElseThrow();
        List<WebElement> listOfAddNoteCategoryValues = driverTools.elementsUntilExist(ADD_NOTE_CATEGORY_DROPDOWN_VALUES)
                .orElseThrow(() -> new NoSuchElementException("Add note category options not found"));
        int randomIndex = generateRandomInt(0, listOfAddNoteCategoryValues.size() - 1);
        String text = driverTools.getText(listOfAddNoteCategoryValues.get(randomIndex));
        driverTools.click(addNoteCategoryDropdownValue(text));
    }

    public void addTextNote(String text) {
        driverTools.waitForElementToAppear(ADD_NOTE_TEXT_AREA).ifPresent(driverTools::click);
        driverTools.sendKeys(ADD_NOTE_TEXT_AREA, text);
    }

    public void clickSubmitNote() {
        driverTools.waitForElementToAppear(ADD_NOTE_SUBMIT_BUTTON).ifPresent(driverTools::click);
    }

    public boolean isAddNoteSuccessSnackbarAppear() {
        driverTools.waitForElementToAppear(ADD_NOTE_SUCCESS_SNACKBAR).orElseThrow();
        return driverTools.isElementDisplayed(ADD_NOTE_SUCCESS_SNACKBAR);
    }

    public boolean isResendVoucherSuccessSnackbarAppear() {
        driverTools.waitForElementToAppear(RESEND_VOUCHER_SUCCESS_SNACKBAR).orElseThrow();
        return driverTools.isElementDisplayed(RESEND_VOUCHER_SUCCESS_SNACKBAR);
    }

    public boolean checkResendVoucherSuccessMessage(String message) {
        WebElement snackbar = driverTools.waitForElementToAppear(RESEND_VOUCHER_SUCCESS_SNACKBAR, 3).orElseThrow();
        return driverTools.getText(snackbar).contains(message);
    }

    public boolean validateLatestActivitiesDescription(String expectedDescription) {
        driverTools.waitForElementToAppear(FIRST_LATEST_ACTIVITIES_DESCRIPTION).orElseThrow();
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        return (driverTools.getText(FIRST_LATEST_ACTIVITIES_DESCRIPTION).get().contains(expectedDescription)
                && driverTools.getText(FIRST_LATEST_ACTIVITIES_DATE).get().contains(formattedDate));
    }

    public void clickFirstItemToRefund() {
        driverTools.waitForElementToAppear(FIRST_REFUND_POPUP_CONTENT).ifPresentOrElse(driverTools::click, ()-> {throw new NoSuchElementException(FIRST_REFUND_POPUP_CONTENT+" Button not present");});
    }

    public void clickRefundPopUpOkButton() {
        driverTools.waitForElementToAppear(REFUND_POPUP_OK_BUTTON).ifPresentOrElse(driverTools::click, ()-> {throw new NoSuchElementException(REFUND_POPUP_OK_BUTTON+" Button not present");});
        driverTools.waitForElementToDisappear(LOADING_STATE);
        driverTools.waitForElementToAppear(REFUND_FORM_SKELETON, 2).ifPresentOrElse(driverTools::waitForElementToDisappear, () -> {logger.log(REFUND_FORM_SKELETON+" Element not present");});
    }

    public void selectRefundPopupRoom(int index) {
        driverTools.waitForElementToAppear(REFUND_POPUP_ROOM_OPTION)
                .orElseThrow(() -> new NoSuchElementException(REFUND_POPUP_ROOM_OPTION + " Room options not present"));
        List<WebElement> options = driverTools.elements(REFUND_POPUP_ROOM_OPTION)
                .orElseThrow(() -> new NoSuchElementException(REFUND_POPUP_ROOM_OPTION + " Room options not present"));
        if (index < 1 || index > options.size()) {
            throw new IndexOutOfBoundsException("Refund popup room index " + index
                    + " out of range (found " + options.size() + " option(s))");
        }
        driverTools.click(options.get(index - 1));
    }

    public void clickChooseRoomOkButton() {
        driverTools.waitForElementToAppear(CHOOSE_ROOM_OK_BUTTON).ifPresentOrElse(driverTools::click,
                () -> {throw new NoSuchElementException(CHOOSE_ROOM_OK_BUTTON + " Button not present");});
        driverTools.waitForElementToDisappear(LOADING_STATE);
        driverTools.waitForElementToAppear(REFUND_FORM_SKELETON, 2).ifPresentOrElse(driverTools::waitForElementToDisappear,
                () -> logger.log(REFUND_FORM_SKELETON + " Element not present"));
    }

    public void closeChooseRoomPopupIfPresent() {
        driverTools.waitForElementToAppear(CHOOSE_ROOM_CLOSE_BUTTON, 2).ifPresentOrElse(closeBtn -> {
            driverTools.click(closeBtn);
            logger.log("Closed 'Choose a room to refund' popup");
        }, () -> logger.log("'Choose a room to refund' popup not present, skipping close"));
    }

    public void waitForRefundFormToDisappear() {
        driverTools.waitForElementToDisappear(REFUND_FORM_MODAL);
    }

    public void selectRoomAndConfirmRefundPopup(int index) {
        selectRefundPopupRoom(index);
        clickChooseRoomOkButton();
    }

    public void selectRoomAndConfirmRefundPopup() {
        selectRoomAndConfirmRefundPopup(1);
    }

    public void selectRefundReasonValue(int index) {
        driverTools.waitForElementToAppear(REFUND_FORM_REASON_DROPDOWN, 3).ifPresentOrElse(driverTools::click, ()-> {throw new NoSuchElementException(REFUND_FORM_REASON_DROPDOWN+" Element not present");});
        driverTools.waitForElementToAppear(getRefundFormReasonByIndex(String.valueOf(index)), 3).ifPresentOrElse(driverTools::click, ()-> {throw new NoSuchElementException(getRefundFormReasonByIndex(String.valueOf(index))+" Element not present");});
        driverTools.waitForElementToDisappear(REFUND_FORM_SKELETON);
    }

    public void selectRefundReasonByName(String reasonName) {
        driverTools.click(driverTools.waitForElementToAppear(REFUND_FORM_REASON_DROPDOWN, 3).orElseThrow());
        driverTools.scrollElementToCenter(getSpecialReasonRefund(reasonName));
        driverTools.click(getSpecialReasonRefund(reasonName));
        driverTools.waitForElementToDisappear(REFUND_FORM_SKELETON);
    }

    public void inputEstimationTotalRefund(int amount) {
        WebElement inputField = driverTools.waitForElementToAppear(INPUT_ESTIMATION_REFUND).orElseThrow();
        int amountInField;
        try {
            amountInField = Integer.parseInt(driverTools.getValue(inputField).replaceAll(digitPattern, ""));
        } catch (NumberFormatException e) {
            amountInField = 0;
        }
        driverTools.scrollIntoWebView(inputField);
        for(int i=0; i<Integer.toString(amountInField).length(); i++) {
            inputField.sendKeys(Keys.BACK_SPACE);
        }
        driverTools.sendKeys(inputField, String.valueOf(amount));
        driverTools.click(SFR_CALCULATE_BUTTON);
        driverTools.waitForElementToDisappear(LOADING_STATE);
    }

    public void clickSeeDetailsEstimationAmount() {
        driverTools.waitForElementToDisappear(REFUND_FORM_SKELETON);
        driverTools.waitForElementToAppear(REFUND_FORM_ESTIMATION_SEE_DETAILS).ifPresentOrElse(driverTools::click,
                ()-> {throw new NoSuchElementException(REFUND_FORM_ESTIMATION_SEE_DETAILS+" Element not present");}
        );
        driverTools.waitForElementToAppear(COLLAPSED_OPEN);
        driverTools.dummyWait(2);
    }

    private int getRefundFormEstimationSize() {
        List<WebElement> estimationDetails = driverTools.elements(REFUND_FORM_ESTIMATION_DETAILS).orElseThrow();
        return estimationDetails.size();
    }

    public void scrollToRefundMethod() {
        driverTools.waitForElementToAppear(REFUND_FORM_REFUND_METHOD, 3).ifPresentOrElse(driverTools::scrollElementToCenter, () -> {throw new NoSuchElementException(REFUND_FORM_REFUND_METHOD+" Element not present");});
    }

    public boolean isUniqueCodeCalculated() {
        int estimationSize = getRefundFormEstimationSize();
        if (estimationSize > 1) {
            Locator section = getRefundFormEstimationDetails(estimationSize-1);
            String text = driverTools.getText(section).get();
            return text.contains("Unique Code");
        } else {
            return false;
        }
    }

    public boolean isEstimatedRefundMethodCorrect(String refundMethod) {
        int estimationSize = getRefundFormEstimationSize();
        Locator section = getRefundFormEstimationDetails(estimationSize);
        String text = driverTools.getText(section).get();
        return text.contains(refundMethod);
    }

    public boolean isEstimatedRefundMethodCorrect(ArrayList<String> refundMethods) {
        int estimationSize = getRefundFormEstimationSize();
        List<WebElement> section = driverTools.elements(getRefundFormMultipleEstimationDetails(estimationSize)).orElseThrow();
        for (WebElement e : section) {
            String text = e.getText();
            for (int i = 0; i < refundMethods.size(); i++) {
                if (text.contains(refundMethods.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isRefundMethodCorrect(String refundMethod) {
        WebElement refundFormRefundMethod = driverTools.waitForElementToAppear(REFUND_FORM_REFUND_METHOD).orElseThrow();
        String text = refundFormRefundMethod.getText();
        return text.contains(refundMethod);
    }

    public boolean isPaylaterNameCorrect(String payLaterName) {
        WebElement formPayLaterName = driverTools.waitForElementToAppear(REFUND_FORM_PAYLATER_NAME).orElseThrow();
        return driverTools.getText(formPayLaterName).contains(payLaterName);
    }
    
    public ArrayList<String> getRefundMethodsRefundForm() {
        List<WebElement> estimationDetails = driverTools.elements(REFUND_FORM_REFUND_METHOD).orElseThrow();
        ArrayList<String> refundMethods = new ArrayList<>();
        for (WebElement e : estimationDetails) {
            refundMethods.add(driverTools.getText(e));
        }
        return refundMethods;
    }

    public boolean isRefundMethodCorrect(ArrayList<String> refundMethods) {
        List<WebElement> estimationDetails = driverTools.elements(REFUND_FORM_REFUND_METHOD).orElseThrow();
        for (WebElement e : estimationDetails) {
            String text = e.getText();
            for (int i = 0; i < refundMethods.size(); i++) {
                if (text.contains(refundMethods.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getBliblipayPhoneNumber() {
        return driverTools.waitForElementToAppear(REFUND_FORM_BLIBLIPAY_PN).map(driverTools::getText)
                .orElseGet(() -> {
                    logger.log("BlibliPay Phone Number not found");
                    return "";
                });
    }

    public void clickSpecialCaseCheckBox() {
        driverTools.waitForElementToAppear(IS_FORCE_TO_BANK_TRANSFER_CHECKBOX).ifPresentOrElse(driverTools::click, ()-> {throw new NoSuchElementException(IS_FORCE_TO_BANK_TRANSFER_CHECKBOX+" Element not present");});
        driverTools.waitForElementToAppear(REFUND_FORM_SKELETON).ifPresentOrElse(driverTools::waitForElementToDisappear, ()-> {logger.log(REFUND_FORM_SKELETON+" Element not present");});
        driverTools.waitForElementToAppear(REFUND_FORM_REFUND_METHOD, 3);
    }

    public boolean verifyOrderReviewRefundFormHotel(
        String orderName,
        String roomName,
        int adult,
        int room,
        String checkIn,
        String checkOut,
        int maxRetryWait
    ) throws InterruptedException{
        //wait order detail
        List<WebElement> listOrderDetail = driverTools.elements(ORDER_DETAIL_LIST_REFUND_FORM).orElseThrow();
        int counter = 1;
        boolean orderReview = true;
        while (counter <= maxRetryWait) {
            listOrderDetail = driverTools.elements(ORDER_DETAIL_LIST_REFUND_FORM).orElseThrow();
            if (!listOrderDetail.get(1).getText().equals("-")) {
                logger.log("Order Review shown");
                break;
            }
            counter++;
            Thread.sleep(500);
        }
        logger.log("counter: "+counter);
        if (counter > maxRetryWait) {
            orderReview = false;
            throw new NoSuchElementException("Order Detail Review not shown properly");
        }

        //verify order review
        String orderDetail = driverTools.getText(ORDER_DETAIL_REFUND_FORM).get();
        String guestRoomCount = adult+" Guest(s), "+room+" Room(s)";
        ArrayList<String> expectedOrderReview = new ArrayList<String>(Arrays.asList(orderName, roomName, guestRoomCount, checkIn, checkOut));
        for (String ex : expectedOrderReview) {
            logger.log("Verifying "+ex+" in Order Review");
            if (!orderDetail.contains(ex)) {
                orderReview = false;
                logger.log(ex+" is not expected");
                break;
            }
        }
        return orderReview;
    }

    public boolean verifyOrderReviewRefundFormTodo(String productName, String productCategory, int ticketQty, String validityDate, int maxRetryWait) throws InterruptedException {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        if (!driverTools.isElementDisplayed(REFUND_FORM_MODAL)) return false;
        List<WebElement> listOrderDetail = driverTools.elements(ORDER_DETAIL_LIST_REFUND_FORM).orElseThrow();
        int counter = 1;
        boolean orderReview = true;
        // wait order detail shown
        while (counter <= maxRetryWait) {
            listOrderDetail = driverTools.elements(ORDER_DETAIL_LIST_REFUND_FORM).orElseThrow();
            logger.log(listOrderDetail.get(1).getText());
            if (!listOrderDetail.get(1).getText().equals("-")) {
                logger.log("Order Review shown");
                break;
            }
            counter++;
            Thread.sleep(500);
        }
        logger.log("counter: "+counter);
        if (counter > maxRetryWait) {
            orderReview = false;
            throw new NoSuchElementException("Order Detail Review not shown properly");
        }
        
        //verify order review
        String orderDetail = driverTools.getText(ORDER_DETAIL_REFUND_FORM).get();
        String ticketQtyString = String.valueOf(ticketQty)+" Ticket(s)";
        ArrayList<String> expectedOrderReview = new ArrayList<String>(Arrays.asList(productName, productCategory, ticketQtyString, validityDate));
        for (String ex : expectedOrderReview) {
            logger.log("Verifying "+ex+" in Order Review");
            if (!orderDetail.contains(ex)) {
                orderReview = false;
                logger.log(ex+" is not expected");
                break;
            }
        }
        return orderReview;
    }

    public boolean verifyOrderReviewRefundFormCar(
        String carName, String carVendor, String carRentalLocation, String carStartDate, 
        String pickupTime, int carCount, int rentalDuration, int maxRetryWait
        ) throws InterruptedException {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        if (!driverTools.isElementDisplayed(REFUND_FORM_MODAL)) return false;
        List<WebElement> listOrderDetail = driverTools.elements(ORDER_DETAIL_LIST_REFUND_FORM).orElseThrow();
        int counter = 1;
        boolean orderReview = true;
        //wait order detail shown
        while (counter <= maxRetryWait) {
            listOrderDetail = driverTools.elements(ORDER_DETAIL_LIST_REFUND_FORM).orElseThrow();
            if (!listOrderDetail.get(1).getText().equals("-")) {
                logger.log("Order Review shown");
                break;
            }
            counter++;
            Thread.sleep(500);
        }
        logger.log("counter: "+counter);
        if (counter > maxRetryWait) {
            orderReview = false;
            throw new NoSuchElementException("Order Detail Review not shown properly");
        }
        //verify order review
        String orderDetail = driverTools.getText(ORDER_DETAIL_REFUND_FORM).get();
        String carCountString = String.valueOf(carCount)+" Car(s)";
        String rentalDurationString = String.valueOf(rentalDuration)+" Day(s)";
        ArrayList<String> expectedOrderReview = new ArrayList<String>(Arrays.asList(carName, carVendor, carRentalLocation, 
                                                    carStartDate, pickupTime, carCountString, rentalDurationString));
        for (String ex : expectedOrderReview) {
            logger.log("Verifying "+ex+" in Order Review");
            if (!orderDetail.contains(ex)) {
                orderReview = false;
                logger.log(ex+" is not expected");
                break;
            }
        }
        return orderReview;
    }

    public boolean verifyOrderReviewRefundFormFlight(
        String orderDetailId, String airlineName, String airportDepartCode, 
        String airportArrivalCode, String flightDepartureDate, String flightDepartureTime,
        boolean isOneHundredRefund, boolean isSmartTrip
    ) {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        if (!driverTools.isElementDisplayed(REFUND_FORM_MODAL)) return false;
        Optional<WebElement> depart = driverTools.waitForElementToAppear(DETAIL_FLIGHT_FR_DEPART);
        if (depart.isEmpty()) { // wait for order review shown
            logger.log("Order Detail Review not shown properly");
            return false;
        }
        if (isSmartTrip) { // check Return trip for Smarttrip
            if (!driverTools.isElementDisplayed(DETAIL_FLIGHT_FR_RETURN)) return false;
        }
        String orderDetail = driverTools.getText(ORDER_DETAIL_REFUND_FORM).get();
        ArrayList<String> expectedOrderReview = new ArrayList<String>(Arrays.asList(orderDetailId, airlineName,
                                airportDepartCode, airportArrivalCode, flightDepartureDate, flightDepartureTime));
        for (String ex : expectedOrderReview) { // verify order review
            logger.log("Verifying "+ex+" in Order Review");
            if (!orderDetail.contains(ex)) {
                logger.log(ex+" is not expected");
                return false;
            }
        }
        if (isOneHundredRefund) { // checking 100p Refund logo
            WebElement logo100p = driverTools.element(REFUND_100P_LOGO_REFUND_FORM).orElseThrow();
            String srcLogo = driverTools.getValueAttribute(logo100p, "src");
            if (!srcLogo.equals(url100pLogo)) {
                logger.log("incorrect 100p Refund logo -> "+srcLogo);
                return false;
            }
        }
        return true;
    }

    public String getTotalRefundAmount() {
        return driverTools.getText(PRIMARY_REFUND_AMOUNT).orElseThrow();
    }

    public String getEstimatedRefundAmountPerMethod(String refundMethod) {
        return driverTools.getText(estimatedRefundCalculation(refundMethod)).orElseThrow().replaceAll(digitPattern, "");
    }

    public boolean verifyRefundAmountPerMethod(BigInteger issuedTotalPayment, String currency, String refundMethod) {
        String amountRefundMethod = driverTools.getText(estimatedRefundCalculation(refundMethod)).orElseThrow();
        String digitsOnly = amountRefundMethod.replaceAll("\\D", "");
        if (digitsOnly.isEmpty()) {
            logger.log("No numeric amount in Refund Method text: "+amountRefundMethod);
            return false;
        }
        BigInteger actualAmount = new BigInteger(digitsOnly);
        boolean currencyMatches = amountRefundMethod.contains(currency);
        boolean amountMatches = actualAmount.equals(issuedTotalPayment);

        if (!currencyMatches || !amountMatches) {
            logger.log("Incorrect Total Refund Amount: expected "+currency+" "+issuedTotalPayment+", got "+amountRefundMethod);
            return false;
        }
        return true;
    }

    public boolean openTNCRefundForm(boolean isSFR) {
        Optional<WebElement> tnc = isSFR ? driverTools.element(TNC_BUTTON_SFR) : driverTools.element(TNC_BUTTON_REFUND_FORM);
        if (tnc.isEmpty()) {
            Assertion.fail("TnC button not shown");
        }
        driverTools.click(tnc.get());
        Optional<WebElement> tncDesc = driverTools.waitForElementToAppear(TNC_DESC_REFUND_FORM);
        if (tncDesc.isEmpty()) {
            Assertion.fail("TnC description not shown");
        }
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(TNC_TOGGLE_CLOSE);
        return true;
    }

    public void submitRefundForm(String note) {
        driverTools.click(REQUEST_REFUND_BUTTON);
        WebElement requestBtn = driverTools.waitForElementToAppear(SUBMIT_REQUEST_REFUND_BUTTON).orElseThrow();
        String attrDisabled = driverTools.getValueAttribute(requestBtn, "disabled");
        logger.log("attrDisabled value: "+attrDisabled);

        WebElement noteTextarea = driverTools.waitForElementToAppear(NOTE_SUBMIT_REFUND).orElseThrow();
        driverTools.sendKeys(noteTextarea, note);
        driverTools.click(requestBtn);
        logger.log("Refund Note: "+note);
    }

    public boolean verifyFlashSuccessSubmitRefund() {
        Optional<WebElement> flashSuccessSubmit = driverTools.waitForElementToAppear(FLASH_SUCCESS_SUBMIT_REFUND);
        Optional<WebElement> flashErrorSubmit = driverTools.waitForElementToAppear(FLASH_ERROR_SUBMIT_REFUND, 2);
        if (flashErrorSubmit.isPresent() && flashSuccessSubmit.isEmpty()) {
            logger.log("Failed to submit refund");
            return false;
        }
        return true;
    }

    public String showFlashMessage(boolean isFlashSuccess) {
        Optional<WebElement> flash = Optional.empty();
        flash = isFlashSuccess ? driverTools.waitForElementToAppear(FLASH_SUCCESS_SUBMIT_REFUND) : driverTools.waitForElementToAppear(FLASH_ERROR_SUBMIT_REFUND);
        if (flash.isEmpty()) {
            Assertion.fail("Flash alert not shown");
        }
        return driverTools.getText(flash.get());
    }
    
    public void selectReasonFirstTwoPassengers() {
        int[] indexes = {3, 1};
        for (int index : indexes) {
            Locator checkbox = getPassengerCheckBox(index);
            Locator dropdown = getPassengerDropdownReason(index);
            Locator dropdownValues = getPassengerDropdownReasonValues(index);
            Locator firstDropdownValue = getPassengerDropdownReasonValueAt(index, 1);
            driverTools.click(checkbox);
            driverTools.waitForElementToAppear(LOADING_STATE).ifPresentOrElse(driverTools::waitForElementToDisappear,
                    ()-> {throw new NoSuchElementException("Loading state not appeared");}
            );
            driverTools.waitForElementToAppear(dropdown).ifPresent(driverTools::click);
            driverTools.waitForElementToAppear(dropdownValues);
            driverTools.click(firstDropdownValue);
            driverTools.waitForElementToDisappear(LOADING_STATE);
        }
    }

    public void selectPax(int paxNum) {
        WebElement paxCheckbox = driverTools.waitForElementToAppear(getPassengerCheckBox(paxNum)).orElseThrow();
        driverTools.click(paxCheckbox);
        driverTools.waitForElementToDisappear(LOADING_STATE);
    }
    
    public void selectPaxAndSpecificReason(int paxNum) {
        selectPax(paxNum);
        driverTools.click(getPassengerDropdownReason(paxNum));
        driverTools.waitForElementToAppear(getPassengerDropdownReasonValues(paxNum));
        driverTools.click(getPassengerDropdownReasonValueAt(paxNum, 1));
    }

    public void selectAllPaxAndReason(boolean selectSpecificReason) {
        List<WebElement> passengerList = driverTools.elements(PASSENGER_REFUND_FORM).orElseThrow();
        for (int i = passengerList.size(); i >= 1; i--){
            if (selectSpecificReason) {
                selectPaxAndSpecificReason(i);
            } else {
                selectPax(i);
            }
        }
    }

    public boolean isInstallmentServiceFeeIncluded() {
        int estimationSize = getRefundFormEstimationSize();
        if (estimationSize > 1) {
            Locator section = getRefundFormEstimationDetails(estimationSize-1);
            String text = driverTools.getText(section).get();
            return text.contains("Service Fee") || text.contains("Service Charge");
        } else {
            return false;
        }
    }

    public String getDiscountType() {
        return driverTools.getText(DISCOUNT_TYPE).orElseThrow();
    }

    public int getDiscountValue() {
        return Integer.parseInt(driverTools.getText(DISCOUNT_AMOUNT).orElseThrow().replaceAll(digitPattern, ""));
    }

    public int getRefundDiscountDeduction(String discountType) {
        return Integer.parseInt(driverTools.getText(refundFeesChargedCalculation(mapDiscountType.get(discountType))).orElseThrow().replaceAll(digitPattern, ""));
    }

    public int getRefundFromAirline() {
        return Integer.parseInt(driverTools.getText(refundPaymentCalculation("Refund from airline")).orElseThrow().replaceAll(digitPattern, ""));
    }

    public int getTicketFee() {
        int fee;
        try {
            fee = Integer.parseInt(driverTools.getText(refundPaymentCalculation("Ticket Fee")).orElseThrow().replaceAll(digitPattern, ""));
        } catch (java.util.NoSuchElementException | StaleElementReferenceException e) {
            fee = Integer.parseInt(driverTools.getText(refundFeesChargedCalculation("Ticket Fee")).orElseThrow().replaceAll(digitPattern, ""));
        }
        return fee;
    }

    public int getRefundFee() {
        return Integer.parseInt(driverTools.getText(refundFeesChargedCalculation("Refund Fee")).orElseThrow().replaceAll(digitPattern, ""));
    }

    public int getSFRPriceBreakdown(String item) {
        return Integer.parseInt(driverTools.getText(getSFRPriceBreakdownItem(item)).orElseThrow().replaceAll(digitPattern, ""));
    }

    public int getFRPriceBreakdown(String item) {
        int total = 0;
        try {
            List<WebElement> breakdowns = driverTools.waitForElementsToAppear(getFRPriceBreakdowns(item), 2).orElseThrow();
            for (WebElement element : breakdowns) {
                total += Integer.parseInt(driverTools.getText(element).replaceAll(digitPattern, ""));
            }
            logger.log("Got total amount: "+total+" for price breakdown: "+item);
        } catch (java.util.NoSuchElementException e) {
            logger.log(e.getLocalizedMessage());
        }
        return total;
    }
    
    public int getBankFee() {
        return Integer.parseInt(driverTools.getText(refundFeesChargedCalculation("Bank Fee")).orElseThrow().replaceAll(digitPattern, ""));
    }

    public boolean isElementInFeesChargedPresent(String value) {
        return driverTools.isElementDisplayed(refundFeesChargedCalculation(value));
    }

    public boolean verifyBankFee(int bankFee, boolean isOneHundredRefund) {
        if (isOneHundredRefund) {
            return !isElementInFeesChargedPresent("Bank Fee");
        }
        return (bankFee == getBankFee());
    }
    
    public int getRefundHistoryTixPoint() {
        if (!isFirstRefund()) {
            List<WebElement> elements = driverTools.elements(refundHistoryDiscountColumnValue("tix point")).orElseThrow();
            int totalTixPointsRefunded = 0;
            for (WebElement element : elements) {
                totalTixPointsRefunded += Integer.parseInt(driverTools.getText(element).replaceAll(digitPattern, ""));
            }
            return totalTixPointsRefunded;
        } else {
            return 0;
        }
    }

    public boolean isFirstRefund() {
        return driverTools.isElementDisplayed(EMPTY_REFUND_HISTORY_TABLE);
    }

    public String getDiscountTypeMapped(String value) {return mapDiscountType.get(value);}

    public void scrollToRequestRefundButton() {
        driverTools.waitForElementToAppear(REQUEST_REFUND_BUTTON).ifPresent(driverTools::scrollIntoWebView);
    }
    
    public boolean isRequestRefundButtonEnabled() {
        return driverTools.isElementEnabled(REQUEST_REFUND_BUTTON);
    }
    
    public void selectBankName(String bankName) {
        driverTools.click(BANK_NAME_DROPDOWN);
        driverTools.click(BANK_NAME_SEARCH);
        driverTools.sendKeys(BANK_NAME_SEARCH, bankName);
        driverTools.click(BANK_NAME_LIST);
    }

    public void inputAccountNumber(String bankAccountNumber) {
        driverTools.click(ACCOUNT_NUMBER_FIELD);
        driverTools.sendKeys(ACCOUNT_NUMBER_FIELD, bankAccountNumber);
        driverTools.click(CHECK_BUTTON);
        driverTools.waitForElementToDisappear(LOADING_STATE);
    }

    public String getBankAccountHolderName() {
        return driverTools.waitForElementToAppear(ACCOUNT_HOLDER_NAME, 3).map(driverTools::getValue)
                .orElseGet(() -> {
                    logger.log("Account holder name not found");
                    return "";
                });
    }

    public void clickSmartPayButton() {
        driverTools.waitForElementToAppear(SMART_PAY_BUTTON).ifPresentOrElse(driverTools::click, () -> {throw new NoSuchElementException(SMART_PAY_BUTTON + " Button not present");});
    }

    public void openRefundMethodDropdown() {
        driverTools.waitForElementToAppear(REFUND_METHOD_DROPDOWN).ifPresentOrElse(driverTools::click,
                () -> {throw new NoSuchElementException(REFUND_METHOD_DROPDOWN + " Dropdown not present");});
    }

    public void selectRefundMethodOption(String methodName) {
        openRefundMethodDropdown();
        Locator option = dynamicRefundMethodOption(methodName);
        driverTools.waitForElementToAppear(option).ifPresentOrElse(driverTools::click,
                () -> {throw new NoSuchElementException(option + " Option not present");});
        driverTools.waitForElementToAppear(REFUND_FORM_SKELETON, 2).ifPresentOrElse(driverTools::waitForElementToDisappear,
                () -> logger.log(REFUND_FORM_SKELETON + " Element not present"));
    }

    public void clickSaveButton() {
        driverTools.click(SAVE_BUTTON);
    }

    public void clickFirstSavedAccount() {
        driverTools.waitForElementToAppear(SAVED_BANK_ACCOUNT).ifPresentOrElse(el -> {
            driverTools.scrollToElement(el);
            driverTools.clickOrFallbackToClickWithJavascriptExecutor(el);
        }, () -> {throw new NoSuchElementException(SAVED_BANK_ACCOUNT + " not present. user don't have saved bank");});
    }

    public int getPaymentCalculation() {
        int refundFromAirline = getRefundFromAirline();
        int ticketFee = getTicketFee();
        return refundFromAirline - ticketFee;
    }

    public boolean isRefundMethodCalculationPartialTiketPointsCorrect(String refundMethod, String discountType, int discountValue, int refundHistoryTixPoints) {
        int paymentCalculation = getPaymentCalculation();
        int tixPointsRemaining = discountValue - refundHistoryTixPoints;
        if (refundHistoryTixPoints < discountValue) {
            int deduction = getRefundDiscountDeduction(discountType);
            String additionalRefundMethod = getDiscountTypeMapped(discountType);
            if (tixPointsRemaining < paymentCalculation) {
                return deduction == tixPointsRemaining && isEstimatedRefundMethodCorrect(refundMethod) && isEstimatedRefundMethodCorrect(additionalRefundMethod) && isRefundMethodCorrect(new ArrayList<>(Arrays.asList(refundMethod, additionalRefundMethod)));
            } else {
                return deduction == paymentCalculation && isEstimatedRefundMethodCorrect(refundMethod) && isEstimatedRefundMethodCorrect(additionalRefundMethod) && isRefundMethodCorrect(new ArrayList<>(Arrays.asList(refundMethod, additionalRefundMethod)));
            }
        } else {
            return !isElementInFeesChargedPresent(discountType) && isEstimatedRefundMethodCorrect(refundMethod) && isRefundMethodCorrect(refundMethod);
        }
    }

    public boolean checkCurrencyRefundEstimation(String targetCurrency){
        ArrayList<String> currencyList = getCurrencyEstimationDetails();
        return currencyList.stream().allMatch(targetCurrency::equals);
    }

    private ArrayList<String> getCurrencyEstimationDetails() {
        List<WebElement> priceLists = driverTools.elements(REFUND_ESTIMATION_DETAILS).orElseThrow();
        ArrayList<String> currencies = new ArrayList<>();
        if (!priceLists.isEmpty()) {
            for (WebElement priceList : priceLists) {
                String priceListText = priceList.getText();
                Matcher fareMatcher = currencyPattern.matcher(priceListText);

                while (fareMatcher.find()) {
                    String currencyCode = fareMatcher.group(1);
                    currencies.add(currencyCode);
                }
            }
        }
        String total = driverTools.getText(REFUND_ESTIMATION_TOTAL).orElseThrow();
        Matcher totalMatcher = currencyPattern.matcher(total);
        if (totalMatcher.find()) {
            String currencyCode = totalMatcher.group(1);
            currencies.add(currencyCode);
        }
        return currencies;
    }

    public boolean verifyPhoneNumber(String number) {
        WebElement phoneNumber = driverTools.waitForElementToAppear(PHONE_NUMBER,3).orElseThrow();
        return driverTools.getText(phoneNumber).contains(number);
    }

    public boolean verifyBookingCode(String code) {
        WebElement bookingCode = driverTools.waitForElementToAppear(BOOKING_CODE_VALUE,3).orElseThrow();
        return driverTools.getText(bookingCode).contains(code);
    }

    public boolean verifyName(String name) {
        WebElement names = driverTools.waitForElementToAppear(NAME_LOCATOR,3).orElseThrow();
        return driverTools.getText(names).contains(name);
    }

    public boolean verifyEmail(String email) {
        WebElement emails = driverTools.waitForElementToAppear(EMAIL_LOCATOR,3).orElseThrow();
        return driverTools.getText(emails).contains(email);
    }

    public boolean verifyIssueStatus(String orderId) {
        WebElement issueStatus = driverTools.waitForElementToAppear(ISSUE_STATUS_LOCATOR,3).orElseThrow();
        return driverTools.getText(issueStatus).contains(orderId);
    }

    public boolean verifySeeActionMenu() {
        WebElement seeActionMenu = driverTools.waitForElementToAppear(ACTION_MENU_LOCATOR, 3).orElseThrow();
        return driverTools.isElementDisplayed(seeActionMenu);
    }

    public boolean checkGreenBar() throws InterruptedException {
        WebElement greenBar = driverTools.waitForElementToAppear(GREEN_BAR_LOCATOR,3).orElseThrow();
        return driverTools.isElementDisplayed(greenBar);
    }

    public boolean verifyScheduledFlight(String flightName) {
        WebElement scheduledFlight = driverTools.waitForElementToAppear(SCHEDULED_FLIGHT_LOCATOR, 3).orElseThrow();
        return driverTools.getText(scheduledFlight).contains(flightName);
    }

    public boolean verifyScheduledHotel(String hotelName) {
        WebElement scheduledHotel = driverTools.waitForElementToAppear(SCHEDULED_HOTEL_LOCATOR).orElseThrow();
        return driverTools.getText(scheduledHotel).contains(hotelName);
    }

    public boolean verifyOrderDetailID(String orderDetailID) {
        WebElement orderDetailIDs = driverTools.waitForElementToAppear(ORDER_DETAIL_ID_LOCATOR, 3).orElseThrow();
        return driverTools.getText(orderDetailIDs).contains(orderDetailID);
    }

    public boolean verifyHotelOrderDetailID(String orderDetailID) {
        WebElement hotelOrderDetailID = driverTools.waitForElementToAppear(ORDER_DETAIL_ID_HOTEL_LOCATOR, 3).orElseThrow();
        return driverTools.getText(hotelOrderDetailID).contains(orderDetailID);
    }

    public boolean verifyNamePassengerDetail(String name) {
        WebElement namePassengerDetail = driverTools.waitForElementToAppear(NAME_PASSENGER_DETAIL_LOCATOR, 3).orElseThrow();
        return driverTools.getText(namePassengerDetail).contains(name);
    }

    public boolean verifyNamePassengerDetailHotel(String name) {
        WebElement namePassengerDetailHotel = driverTools.waitForElementToAppear(NAME_PASSENGER_DETAIL_HOTEL_LOCATOR, 3).orElseThrow();
        return driverTools.getText(namePassengerDetailHotel).contains(name);
    }

    public boolean verifyPriceDetail(String price) {
        if (driverTools.waitForElementToAppear(TRANSACTION_AMOUNT_SECTION, 3).isEmpty()
                && driverTools.waitForElementToAppear(PRICE_DETAIL_BUTTON, 2).isPresent()) {
            clickPriceDetailButton();
        }
        Optional<WebElement> amountSection = driverTools.waitForElementToAppear(TRANSACTION_AMOUNT_SECTION, 5);
        if (amountSection.isPresent()) {
            return hotelOrderPriceMatches(driverTools.getText(amountSection.get()), price);
        }
        WebElement legacy = driverTools.waitForElementToAppear(PRICE_DETAIL_LEGACY_INLINE, 3).orElseThrow();
        return hotelOrderPriceMatches(driverTools.getText(legacy), price);
    }

    public boolean verifyPriceDetailFlight(String price) {
        WebElement invoiceAmount = driverTools.waitForElementToAppear(PRICE_DETAIL_FLIGHT_INVOICE_AMOUNT, 8).orElseThrow();
        String text = driverTools.getText(invoiceAmount).replace('\u00a0', ' ');
        return hotelOrderPriceMatches(text, price);
    }

    public boolean verifyPriceDetailFlightHighEmphasis(String price) {
        WebElement invoiceAmount = driverTools.waitForElementToAppear(PRICE_DETAIL_FLIGHT_INVOICE_AMOUNT_HIGH_EMPHASIS, 8).orElseThrow();
        driverTools.scrollIntoWebView(invoiceAmount);
        String text = driverTools.getText(invoiceAmount).replace('\u00a0', ' ').trim();
        if (text.isEmpty()) {
            String textContent = invoiceAmount.getAttribute("textContent");
            text = textContent == null ? "" : textContent.replace('\u00a0', ' ').trim();
        }
        return hotelOrderPriceMatches(text, price);
    }

    public boolean verifyPriceDetailHotel(String price) {
        WebElement amountSection = driverTools.waitForElementToAppear(PRICE_DETAIL_HOTEL_ORDER_AMOUNT_SECTION, 8).orElseThrow();
        return hotelOrderPriceMatches(driverTools.getText(amountSection), price);
    }

    private static boolean hotelOrderPriceMatches(String uiText, String expectedPrice) {
        if (expectedPrice == null || expectedPrice.isBlank()) {
            return false;
        }
        if (uiText.contains(expectedPrice)) {
            return true;
        }
        String expectedDigits = expectedPrice.replaceAll("\\D", "");
        if (expectedDigits.isEmpty()) {
            return false;
        }
        return uiText.replaceAll("\\D", "").contains(expectedDigits);
    }

    public boolean verifyPaymentMethod(String pMethod) {
        WebElement paymentMethod = driverTools.waitForElementToAppear(PAYMENT_METHOD_LOCATOR, 5).orElseThrow();
        return driverTools.getText(paymentMethod).contains(pMethod);
    }

    public boolean verifyPaymentMethodHomes(String pMethod) {
        WebElement paymentMethod = driverTools.waitForElementToAppear(PAYMENT_METHOD_LOCATOR_HOMES, 10).orElseThrow();
        driverTools.scrollIntoWebView(paymentMethod);
        String text = driverTools.getText(paymentMethod).replace('\u00a0', ' ').trim();
        if (text.isEmpty()) {
            String textContent = paymentMethod.getAttribute("textContent");
            text = textContent == null ? "" : textContent.replace('\u00a0', ' ').trim();
        }
        String normalizedText = text.replace('\u2013', '-').replace('\u2014', '-');
        String normalizedExpected = pMethod == null ? "" : pMethod.replace('\u2013', '-').replace('\u2014', '-').trim();
        return normalizedText.contains(normalizedExpected);
    }

    public boolean verifyPaymentMethodFlight(String pMethod) {
        WebElement paymentMethod = driverTools.waitForElementToAppear(PAYMENT_METHOD_LOCATOR_FLIGHT, 10).orElseThrow();
        driverTools.scrollIntoWebView(paymentMethod);
        String text = driverTools.getText(paymentMethod).replace('\u00a0', ' ').trim();
        if (text.isEmpty()) {
            String textContent = paymentMethod.getAttribute("textContent");
            text = textContent == null ? "" : textContent.replace('\u00a0', ' ').trim();
        }
        return text.contains(pMethod);
    }

    public void clickPassengerDetail() {
        WebElement passengerDetail = driverTools.waitForElementToAppear(getTabByName("Passenger Detail"), 3).orElseThrow();
        driverTools.click(passengerDetail);
    }

    public void clickPassengerDetailFlight() {
        driverTools.waitForElementToDisappear(LOADING_STATE, 5);
        WebElement tab = driverTools.waitForElementToBeClickable(PASSENGER_DETAIL_TAB_FLIGHT, 10).orElseThrow();
        driverTools.scrollIntoWebView(tab);
        try {
            driverTools.click(tab);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(PASSENGER_DETAIL_TAB_FLIGHT);
        }
    }

    public void clickPassengerDetailHotel() {
        WebElement tab = driverTools.waitForElementToAppear(GUEST_DETAILS_TAB_HOTEL, 6).orElseThrow();
        driverTools.click(tab);
    }

    public void clickGuestDetailsTabHomes() {
        WebElement tab = driverTools.waitForElementToAppear(GUEST_DETAILS_TAB_HOMES, 6).orElseThrow();
        driverTools.click(tab);
    }

    public void clickGuestDetailsTabAirportTransfer() {
        driverTools.waitForElementToDisappear(LOADING_STATE, 5);
        driverTools.scrollIntoWebView(GUEST_DETAILS_TAB_AIRPORT_TRANSFER);
        WebElement tab = driverTools.waitForElementToBeClickable(GUEST_DETAILS_TAB_AIRPORT_TRANSFER, 10).orElseThrow();
        try {
            driverTools.click(tab);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(GUEST_DETAILS_TAB_AIRPORT_TRANSFER);
        }
    }

    public boolean verifyScheduledTrain(String TrainName) {
        WebElement scheduledTrain = driverTools.waitForElementToAppear(SCHEDULED_TRAIN_LOCATOR, 3).orElseThrow();
        return driverTools.getText(scheduledTrain).contains(TrainName);
    }

    public boolean verifyScheduledCar(String carName) {
        WebElement scheduledCar = driverTools.waitForElementToAppear(SCHEDULED_CAR_LOCATOR, 3).orElseThrow();
        return driverTools.getText(scheduledCar).contains(carName);
    }

    public boolean verifyScheduledCarAirportTransfer(String carName) {
        WebElement scheduledCar = driverTools.waitForElementToAppear(SCHEDULED_AIRPORT_TRANSFER_CAR_LOCATOR, 3).orElseThrow();
        return driverTools.getText(scheduledCar).contains(carName);
    }

    public boolean verifyScheduledBus(String busName) {
        WebElement scheduledBus = driverTools.waitForElementToAppear(SCHEDULED_BUS_LOCATOR, 3).orElseThrow();
        return driverTools.getText(scheduledBus).contains(busName);
    }

    public boolean verifyNamePassengerDetailTrain(String name) {
        WebElement namePassengerDetailTrain = driverTools.waitForElementToAppear(NAME_PASSENGER_DETAIL_LOCATOR_TRAIN, 3).orElseThrow();
        return driverTools.getText(namePassengerDetailTrain).contains(name);
    }

    public boolean verifyNamePassengerDetailFerry(String name) {
        WebElement namePassengerDetailFerry = driverTools.waitForElementToAppear(NAME_PASSENGER_DETAIL_LOCATOR_FERRY, 3).orElseThrow();
        return driverTools.getText(namePassengerDetailFerry).contains(name);
    }

    public boolean verifyCarOrderDetailID(String orderDetailID) {
        WebElement carOrderDetailID = driverTools.waitForElementToAppear(ORDER_DETAIL_ID_CAR_LOCATOR, 3).orElseThrow();
        return driverTools.getText(carOrderDetailID).contains(orderDetailID);
    }

    public void clickViewETicketButton() {
        WebElement viewETicketButton = driverTools.waitForElementToAppear(VIEW_E_TICKET_VOUCHER_LOCATOR).orElseThrow();
        driverTools.scrollIntoWebView(VIEW_E_TICKET_VOUCHER_LOCATOR);
        driverTools.waitForElementToBeClickable(VIEW_E_TICKET_VOUCHER_LOCATOR).orElseThrow();
        try {
            driverTools.click(viewETicketButton);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(viewETicketButton);
        }
    }

    public boolean isNewTabOpened(String expectedUrl) throws InterruptedException {
        String mainWindowHandle = driver.getWindowHandle();
        clickViewETicketButton();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        String actualUrl = driverTools.getCurrentURL();
        boolean urlMatches = actualUrl.contains(expectedUrl);
        closeCurrentTab(mainWindowHandle);
        driver.switchTo().window(mainWindowHandle);
        return urlMatches;
    }

    public void clickResendVoucher() {
        WebElement resendVoucher = driverTools.waitForElementToAppear(RESEND_VOUCHER_LOCATOR).orElseThrow();
        driverTools.scrollIntoWebView(RESEND_VOUCHER_LOCATOR);
        driverTools.waitForElementToBeClickable(RESEND_VOUCHER_LOCATOR).orElseThrow();
        try {
            driverTools.click(resendVoucher);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(resendVoucher);
        }
    }

    public void selectRecipientTypeDropdown() {
        WebElement recipientTypeDropdown = driverTools.waitForElementToAppear(RECIPIENT_TYPE_DROPDOWN_LOCATOR).orElseThrow();
        driverTools.waitForElementToBeClickable(recipientTypeDropdown).orElseThrow();
        driverTools.clickWithJavascriptExecutor(recipientTypeDropdown);
    }

    public void selectCustomerEmailInDropdown() {
        WebElement customerEmailInDropdown = driverTools.waitForElementToAppear(CUSTOMER_EMAIL_LOCATOR).orElseThrow();
        driverTools.click(customerEmailInDropdown);
    }

    public void selectChooseLanguageDropdown() {
        WebElement chooseLanguageDropdown = driverTools.waitForElementToAppear(CHOOSE_LANGUAGE_DROPDOWN_LOCATOR).orElseThrow();
        driverTools.click(chooseLanguageDropdown);
    }

    public void selectEnglishInDropdown() {
        WebElement englishInDropdown = driverTools.waitForElementToAppear(ENGLISH_LOCATOR).orElseThrow();
        driverTools.click(englishInDropdown);
    }

    public void clickSubmitButton() {
        WebElement submitButton = driverTools.waitForElementToAppear(SUBMIT_BUTTON_LOCATOR).orElseThrow();
        driverTools.click(submitButton);
    }

    public void clickResendVoucherSubmitButton() {
        WebElement submitButton = driverTools.waitForElementToAppear(RESEND_VOUCHER_SUBMIT_BUTTON).orElseThrow();
        driverTools.click(submitButton);
    }

    public void clickGuestDetail() {
        WebElement guestDetail = driverTools.waitForElementToAppear(GUEST_DETAIL_LOCATOR).orElseThrow();
        driverTools.click(guestDetail);
    }

    public void clickPassengerDetailTab() {
        WebElement passengerDetailTab = driverTools.waitForElementToAppear(getTabByName("Passenger Detail")).orElseThrow();
        driverTools.click(passengerDetailTab);
    }

    public void clickPassengerDetailTabBus() {
        driverTools.waitForElementToDisappear(LOADING_STATE, 5);
        WebElement tab = driverTools.waitForElementToBeClickable(PASSENGER_DETAIL_TAB_BUS, 10).orElseThrow();
        driverTools.scrollIntoWebView(tab);
        try {
            driverTools.click(tab);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(PASSENGER_DETAIL_TAB_BUS);
        }
    }

    public void clickVisitorName() {
        WebElement visitorName = driverTools.waitForElementToAppear(VISITOR_NAME_LOCATOR).orElseThrow();
        driverTools.click(visitorName);
    }

    public boolean verifyTrainOrderDetailID(String orderDetailID) {
        WebElement trainOrderDetailID = driverTools.waitForElementToAppear(ORDER_DETAIL_ID_TRAIN_LOCATOR, 3).orElseThrow();
        return driverTools.getText(trainOrderDetailID).contains(orderDetailID);
    }

    public String verifyScheduledTTD(String category) {
        WebElement scheduledTTD = driverTools.waitForElementToAppear(scheduledTtdLocator(category), 3).orElseThrow();
        return driverTools.getText(scheduledTTD);
    }

    public boolean verifyOrderDetailIDTTD(String ID) {
        WebElement orderDetailIDTTD = driverTools.waitForElementToAppear(ORDER_DETAIL_ID_TTD_LOCATOR, 3).orElseThrow();
        return driverTools.getText(orderDetailIDTTD).contains(ID);
    }

    public boolean verifyVisitorNameTTD(String name) {
        WebElement visitorNameTTD = driverTools.waitForElementToAppear(VISITOR_FULL_NAME_TTD, 3).orElseThrow();
        return driverTools.getText(visitorNameTTD).contains(name);
    }

    public boolean verifyOrderDetailIDBus(String id) {
        WebElement orderDetailIDBus = driverTools.waitForElementToAppear(ORDER_DETAIL_ID_BUS_LOCATOR, 3).orElseThrow();
        return driverTools.getText(orderDetailIDBus).contains(id);
    }

    public boolean checkSuccessMessage(String message) {
        WebElement successMessage = driverTools.waitForElementToAppear(GREEN_SUCCESS_BAR, 3).orElseThrow();
        return driverTools.getText(successMessage).contains(message);
    }

    public boolean verifyNamePassengerDetailAT(String name) {
        WebElement namePassengerDetailAT = driverTools.waitForElementToAppear(NAME_PASSENGER_DETAIL_LOCATOR_AT, 3).orElseThrow();
        return driverTools.getText(namePassengerDetailAT).contains(name);
    }

    public boolean verifyNamePassengerDetailCar(String name) {
        WebElement namePassengerDetailCar = driverTools.waitForElementToAppear(NAME_PASSENGER_DETAIL_LOCATOR_CAR, 3).orElseThrow();
        return driverTools.getText(namePassengerDetailCar).contains(name);
    }

    public void selectVendorInDropdown() {
        WebElement vendorEmail = driverTools.waitForElementToAppear(VENDOR_EMAIL_LOCATOR,3).orElseThrow();
        driverTools.click(vendorEmail);
    }

    public void selectOtherEmailInDropdown() {
        WebElement otherEmail = driverTools.waitForElementToAppear(OTHER_EMAIL_LOCATOR,3).orElseThrow();
        driverTools.click(otherEmail);
    }

    public void sendEmailInEmailField(String value) {
        WebElement emailField = driverTools.waitForElementToAppear(EMAIL_FIELD_LOCATOR,3).orElseThrow();
        driverTools.click(emailField);
        driverTools.sendKeys(emailField, value);
    }

    public void clickViewReceiptButton() {
        WebElement viewReciept = driverTools.waitForElementToAppear(VIEW_RECEIPT_LOCATOR,3).orElseThrow();
        try {
            driverTools.click(viewReciept);
        } catch (ElementClickInterceptedException e) {
            logger.log("Click with Javascript Executor");
            driverTools.clickWithJavascriptExecutor(viewReciept);
        }
    }

    public boolean isNewTabOpenedViewReceipt(String expectedUrl) throws InterruptedException {
        String mainWindowHandle = driver.getWindowHandle();
        clickViewReceiptButton();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        String actualUrl = driverTools.getCurrentURL();
        boolean urlMatches = actualUrl.contains(expectedUrl);
        closeCurrentTab(mainWindowHandle);
        driver.switchTo().window(mainWindowHandle);
        return urlMatches;
    }

    public void clickResendReceipt() {
        WebElement resendReceipt = driverTools.waitForElementToAppear(RESEND_RECEIPT_LOCATOR,3).orElseThrow();
        try {
            driverTools.click(resendReceipt);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(resendReceipt);
        }
    }

    public void clickResendReceiptSubmit() {
        WebElement resendReceiptSubmit = driverTools.waitForElementToAppear(RESEND_RECEIPT_SUBMIT_LOCATOR, 3).orElseThrow();
        try {
            driverTools.click(resendReceiptSubmit);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(resendReceiptSubmit);
        }
    }

    public void clickLogHistory() {
        WebElement logHistroyButton = driverTools.waitForElementToAppear(LOG_HISTORY_LOCATOR,3).orElseThrow();
        try {
            driverTools.click(logHistroyButton);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(logHistroyButton);
        }
    }

    public boolean verifyStatus() {
        boolean statusLocator = driverTools.isElementDisplayed(LOG_HISTORY_LOCATOR);
        return statusLocator;
    }

    public void clickEditBooking() {
        WebElement editBookingButton = driverTools.waitForElementToAppear(EDIT_BOOKING_LOCATOR,3).orElseThrow();
        try {
            driverTools.click(editBookingButton);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(editBookingButton);
        }
    }

    public void clickRoomToEdit() {
        WebElement editRoomRadioButton = driverTools.waitForElementToAppear(EDIT_ROOM_RADIO_BUTTON_LOCATOR,3).orElseThrow();
        driverTools.click(editRoomRadioButton);
    }

    public void clickSubmit() {
        WebElement submitButton = driverTools.waitForElementToAppear(SUBMIT_LOCATOR,3).orElseThrow();
        driverTools.click(submitButton);
    }

    public String isHotelDashboardOpened() throws InterruptedException {
        String mainWindowHandle = driver.getWindowHandle();
        clickSubmit();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        String actualUrl = driverTools.getCurrentURL();
        Thread.sleep(1000);
        closeCurrentTab(mainWindowHandle);
        driver.switchTo().window(mainWindowHandle); 
        return actualUrl;
    }

    public void clickInsurance() {
        driverTools.waitForElementToAppear(INSURANCE_SECTION_LOCATOR, 3).orElseThrow();
        driverTools.scrollIntoWebView(INSURANCE_SECTION_LOCATOR);
        WebElement insuranceSection = driverTools.waitForElementToBeClickable(INSURANCE_SECTION_LOCATOR).orElseThrow();
        try {
            driverTools.click(insuranceSection);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(insuranceSection);
        }
    }

    public void clickMoreInInsuranceColunm() {
        WebElement moreButton = driverTools.waitForElementToAppear(MORE_IN_INSURANCE_SECTION_LOCATOR,3).orElseThrow();
        try {
            driverTools.click(moreButton);
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            driverTools.clickWithJavascriptExecutor(moreButton);
        }
    }

    public void clickSeeCoverageValue() {
        driverTools.waitForElementToAppear(SEE_COVERAGE_AND_VALUE_LOCATOR, 3).orElseThrow();
        driverTools.scrollIntoWebView(SEE_COVERAGE_AND_VALUE_LOCATOR);
        driverTools.clickWithJavascriptExecutor(SEE_COVERAGE_AND_VALUE_LOCATOR);
    }

    public boolean isNewTabOpenedSeeCoverageValue(String expectedUrl) throws InterruptedException {
        String mainWindowHandle = driver.getWindowHandle();
        clickSeeCoverageValue();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        String actualUrl = driverTools.getCurrentURL();
        boolean urlMatches = actualUrl.contains(expectedUrl);
        closeCurrentTab(mainWindowHandle);
        driver.switchTo().window(mainWindowHandle);
        return urlMatches;
    }

    public void clickSeeProtectionDetail() {
        driverTools.waitForElementToAppear(SEE_PROTECTION_DETAIL_LOCATOR, 3).orElseThrow();
        driverTools.scrollIntoWebView(SEE_PROTECTION_DETAIL_LOCATOR);
        driverTools.clickWithJavascriptExecutor(SEE_PROTECTION_DETAIL_LOCATOR);
    }

    public boolean isNewTabOpenedSeeProtectionDetail(String expectedUrl) throws InterruptedException {
        String mainWindowHandle = driver.getWindowHandle();
        clickSeeProtectionDetail();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        String actualUrl = driverTools.getCurrentURL();
        boolean urlMatches = actualUrl.contains(expectedUrl);
        closeCurrentTab(mainWindowHandle);
        driver.switchTo().window(mainWindowHandle);
        return urlMatches;
    }

    public void clickResendPolicy() {
        driverTools.waitForElementToAppear(RESEND_POLICY_LOCATOR, 3).orElseThrow();
        driverTools.scrollIntoWebView(RESEND_POLICY_LOCATOR);
        WebElement resendPolicyButton = driverTools.waitForElementToBeClickable(RESEND_POLICY_LOCATOR).orElseThrow();
        try {
            driverTools.click(resendPolicyButton);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(resendPolicyButton);
        }
    }

    public void inputEmailInResendPolicy(String input) {
        try {
            driverTools.click(EMAIL_IN_RESEND_POLICY);
        } catch (ElementClickInterceptedException e) {
            logger.log("Click intercepted, retrying with JS executor");
            driverTools.clickWithJavascriptExecutor(EMAIL_IN_RESEND_POLICY);
        }
        driverTools.sendKeys(EMAIL_IN_RESEND_POLICY, input);
    }

    public void clickResend() {
        WebElement resendButton = driverTools.waitForElementToAppear(RESEND_BUTTON_LOCATOR,3).orElseThrow();
        driverTools.click(resendButton);
    }

    public void clickResendPolicyButton() {
        WebElement resendButton = driverTools.waitForElementToAppear(RESEND_BUTTON_LOCATOR_POLICY, 3).orElseThrow();
        driverTools.click(resendButton);
    }

    public boolean checkStatus(String status1, String status2) throws InterruptedException {
        for (int attempt = 1; attempt <= 10; attempt++) {
            driverTools.waitForElementToDisappear(LOADING_STATE);
            Optional<WebElement> statusType = driverTools.waitForElementToAppear(STATUS_LOCATOR, 10);
            if (statusType.isPresent()) {
                String text = driverTools.getText(statusType.get()).replace('\u00a0', ' ').trim();
                if (matchesStatus(text, status1) || matchesStatus(text, status2)) {
                    return true;
                }
                if ("Waiting for payment".equalsIgnoreCase(text) && attempt < 10) {
                    driver.navigate().refresh();
                    driverTools.waitForElementToDisappear(LOADING_STATE);
                }
            }
            if (attempt < 10) {
                driverTools.dummyWait(1);
            }
        }
        return false;
    }

    private boolean matchesStatus(String actual, String expected) {
        if (expected == null || expected.isBlank()) {
            return false;
        }
        return normalizeStatus(actual).contains(normalizeStatus(expected));
    }

    private String normalizeStatus(String value) {
        return value.replace('\u00a0', ' ')
                .replace('\u2013', '-')
                .replace('\u2014', '-')
                .replace("-", "")
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase();
    }

    public void goBackToOrderList() {
        WebElement back = driverTools.waitForElementToAppear(BACK_TO_ORDER_LIST_LOCATOR,3).orElseThrow();
        driverTools.click(back);
    }

    public void closeCurrentTab(String mainWindowHandle) {
        String currentWindowHandle = driver.getWindowHandle(); 
        if (!currentWindowHandle.equals(mainWindowHandle)) {
            logger.log("current window -> "+currentWindowHandle+" is not same with main window -> "+mainWindowHandle+". closing current window");
            driver.close();
            logger.log("current window closed");
        }
    }
    public void selectVerticalFilter(String verticalName) {
        WebElement dropdown = driverTools.waitForElementToAppear(VERTICAL_DROPDOWN_CONTAINER, 10).orElseThrow(() ->
                new IllegalStateException("Vertical filter dropdown was not found on the page."));
        driverTools.clickWithJavascriptExecutor(dropdown);

        WebElement searchInput = driverTools.waitForElementToAppear(DROPDOWN_SEARCH_INPUT, 5).orElseThrow(() ->
                new IllegalStateException("Search input inside dropdown was not found."));
        driverTools.sendKeys(searchInput, verticalName);
        driverTools.dummyWait(1);

        Locator checkboxLocator = getVerticalCheckboxLocator(verticalName);
        WebElement checkbox = driverTools.waitForElementToAppear(checkboxLocator, 5).orElseThrow(() ->
                new IllegalStateException("Checkbox for vertical '" + verticalName + "' was not found after searching."));

        if (!checkbox.isSelected()) {
            driverTools.clickWithJavascriptExecutor(checkbox);
        }
    }
    public String getFirstRowVerticalValue() {
        List<WebElement> cells = driverTools.waitForElementsToAppear(ALL_ROWS_VERTICAL_VALUE, 10)
                .orElseThrow(() -> new IllegalStateException("Table row data was not loaded after filtering."));

        if (cells.isEmpty()) {
            throw new IllegalStateException("Table row data is empty.");
        }

        int rowsToCheck = Math.min(cells.size(), 5);

        for (int i = 0; i < rowsToCheck; i++) {
            String cellText = driverTools.getValue(cells.get(i)).trim();
            System.out.println("Row " + (i + 1) + " Vertical Value: " + cellText);
        }

        return driverTools.getText(cells.get(0)).trim();
    }
}
