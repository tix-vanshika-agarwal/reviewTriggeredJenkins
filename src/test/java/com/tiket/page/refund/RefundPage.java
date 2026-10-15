package com.tiket.page.refund;

import com.tiket.api.auth.POSTGetAdminAccessTokenDashboardApi;
import com.tiket.api.refund.GETRefundAbleStatus;
import com.tiket.api.refund.GetRefundStatusApi;
import com.tiket.api.refund.PostRefundForceSubmitApi;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.pojo.flight.FlightTicketDetail;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.pojo.refund.RefundStatusApiResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hamcrest.core.Is;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.page.payment.PaymentPageAndroid.LOADING_STATE;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

public abstract class RefundPage <T extends WebDriver> extends BasePage<T> {

    private static final Locator TOTAL_REFUND_AMOUNT = new Locator("TOTAL_REFUND_AMOUNT", AppiumBy.xpath("//*[@data-testid='point-amount']//span"));
    private static final Locator TIKET_POINT_ICON = new Locator("TIKET_POINT_ICON", AppiumBy.xpath("//*[@class='refund-tiket-points']//img[@class='points-large mobile']"));
    private static final Locator PRICE_DETAIL_DROPDOWN_ARROW = new Locator("PRICE_DETAIL_DROPDOWN_ARROW", AppiumBy.xpath("//*[contains(@class,'refund-total-payment')]//following-sibling::*[@class='tix-icon v4']"));
    private static final Locator CLOSE_PRICE_DETAIL_BTN = new Locator("CLOSE_PRICE_DETAIL_BTN", AppiumBy.xpath("//*[contains(@class,'bs-icon-close-wrapper') or contains(@class,'close_button') or contains(@class,'modal_close')]"));
    private static final Locator PRICE_LOCATOR = new Locator("PRICE_LOCATOR", AppiumBy.xpath("//*[contains(@class,'refund-payment-section')]//following-sibling::*[last()]"));
    private static final Locator SELECT_REASON_SECTION = new Locator("SELECT_REASON_SECTION", AppiumBy.xpath("//*[contains(@class,'styles_reason')]"));
    private static final Locator SELECT_REASON_DROPDOWN = new Locator("SELECT_REASON_DROPDOWN", AppiumBy.xpath("//*[@data-testid='card-choose-reason']"));
    private static final Locator REFUND_REASON = new Locator("REFUND_REASON", AppiumBy.xpath("//*[@data-testid='refund-reason-radio-group']//*[contains(@class,'styles_radio_wrapper')]//*[text()]"));
    private static final Locator CHOSEN_REASON = new Locator("CHOSEN_REASON", AppiumBy.xpath("(//*[contains(@class,'styles_radio_wrapper')])[1]"));
    private static final Locator SAVE_REASON = new Locator("SAVE_REASON", AppiumBy.xpath("//*[contains(@class,'select_reason_wrapper')]//following-sibling::button"));
    private static final Locator REFUND_BTN_WEBVIEW = new Locator("PROCESS_REFUND_BTN_WEBVIEW", AppiumBy.xpath("//*[@class='next-button-container']//*[@type='button'] | //*[contains(@class,'Button_variant_primary')]"));
    private static final Locator REFUND_METHOD_NAME_NON_BLIPAY = new Locator("REFUND_METHOD_NAME_NON_BLIPAY", AppiumBy.xpath("//*[contains(@class,'refund_method_name')]//span | //*[contains(@class,'RefundMethodItem_header')]//span"));
    private static final Locator REFUND_METHOD_BLIBLIPAY_ICON = new Locator("REFUND_METHOD_BLIBLIPAY_ICON", AppiumBy.xpath("//*[contains(@class,'refund_method_name')]//*[contains(@class,'blipay_title')]"));
    private static final Locator REFUND_METHOD_NAME = new Locator("REFUND_METHOD_NAME", AppiumBy.xpath("//*[contains(@class,'refund_method_name')]"));
    private static final Locator SELECTED_REFUND_METHOD_NAME = new Locator("SELECTED_REFUND_METHOD_NAME", AppiumBy.xpath("//*[contains(@class,'RefundMethodItem_header_information') or contains(@class,'RefundMethodItem_bank_account_details')]//*[text()]"));
    private static final Locator BLIBLIPAY_ICON = new Locator("BLIBLIPAY_ICON", AppiumBy.xpath("//*[ contains(@class,'styles_blipay_text')]"));
    private static final Locator REFUND_AMOUNT = new Locator("REFUND_AMOUNT", AppiumBy.xpath("//*[contains(@class,'refund-amount')] | //p[contains(@class,'total_refund')] | //*[@data-testid='icon-chevron']//preceding-sibling::span"));
    private static final Locator REFUND_FEE = new Locator("REFUND_FEE", AppiumBy.xpath("//*[contains(@class,'estimation')]//*[contains(text(),'Refund Fee') or contains(text(),'Biaya Refund')]/following-sibling::*[contains(@class,'text')]"));
    private static final Locator REFUND_RECEIVE = new Locator("REFUND_RECEIVE", AppiumBy.xpath("//*[contains(@class,'refund_receive')]//following-sibling::*[contains(@class,'Text_text')]"));
    private static final Locator REFUND_ESTIMATION_AMOUNT = new Locator("REFUND_ESTIMATION_AMOUNT", AppiumBy.xpath("//*[contains(@class,'total_refund')] | //*[contains(@class,'styles_type_price_breakdown')]/*[contains(@class,'styles_amount')]"));
    private static final Locator TERM_AND_CONDITION_CHECKBOX_WEBVIEW1 = new Locator("TERM_AND_CONDITION_CHECKBOX_WEBVIEW1", AppiumBy.xpath("//*[contains(@class,'Check_checkbox')]"));
    private static final Locator TERM_AND_CONDITION_CHECKBOX_WEBVIEW2 = new Locator("TERM_AND_CONDITION_CHECKBOX_WEBVIEW2", AppiumBy.xpath("//*[contains(@class,'Check_check_')]"));
    private static final Locator CHECKBOX_REFUND = new Locator("CHECKBOX_REFUND", AppiumBy.xpath("//*[contains(@class,'Check_custom_checkbox')]"));
    private static final Locator SUBMIT_REFUND = new Locator("SUBMIT_REFUND", AppiumBy.xpath("//*[(contains(@class,'Button_size_large') or @data-testid='continue-submit-refund')]//*[contains(text(),'Refund')]"));
    private static final Locator SEE_ORDER_LIST_BUTTON = new Locator("SEE_ORDER_LIST_BUTTON", AppiumBy.xpath("//*[contains(@class,'Button_size_large') and not(contains(text(),'Refund'))]"));
    private static final Locator POPUP_PROCESS_REFUND_BTN_WEBVIEW = new Locator("POPUP_PROCESS_REFUND_BTN_WEBVIEW", AppiumBy.xpath("(//*[contains(@class,'Button_variant_primary') and  not(contains(@class,'Button_state_disabled'))  ])[2]"));
    private static final Locator OTP_FEILD_WEBVIEW = new Locator("OTP_FEILD_WEBVIEW", AppiumBy.xpath("//div[@class='otp-code-field focus']//div//input"));
    private static final Locator REFUND_SUBMISSION_POPUP_MSG_WEBVIEW = new Locator("REFUND_SUBMISSION_POPUP_MSG_WEBVIEW", AppiumBy.xpath("//*[contains(text(),'Pengajuan refund berhasil') or contains(text(),'Success submit the request')]"));
    private static final Locator VIEW_REFUND_DETAIL_BTN = new Locator("VIEW_REFUND_DETAIL_BTN", AppiumBy.xpath("//*[@class='dialog-popup']//button | //*[contains(@class,'dialog')]//*[contains(@class,'Button_variant_primary')]"));
    private static final Locator ORDER_ID_REFUND_DETAIL = new Locator("ORDER_ID_REFUND_DETAIL", AppiumBy.xpath("(//div[contains(@class,'styles_refund_id')]//span)[2]"));
    private static final Locator ESTIMATED_TOTAL_PAYMENT_BTN = new Locator("ESTIMATED_TOTAL_PAYMENT_BTN", AppiumBy.xpath("(//*[@class='tix-icon v4'])[last()] | //*[contains(@class,'chevron')] | //*[@data-testid='icon-chevron']"));
    private static final Locator TOTAL_ESTIMATED_REFUND = new Locator("TOTAL_ESTIMATED_REFUND", AppiumBy.xpath("//*[contains(@class,'refund_total_payment')]//following-sibling::*[contains(@class,'total_wrapper')]//span"));
    private static final Locator PROMO_AMOUNT_DETAIL = new Locator("PROMO_AMOUNT_DETAIL", AppiumBy.xpath("//*[@class='refund-payment-section']//*[contains(text(),'Promo') or contains(text(),'promo')]//following-sibling::*[contains(text(),'-')]"));
    private static final Locator CHANGE_REFUND_METHOD = new Locator("CHANGE_REFUND_METHOD", AppiumBy.xpath("//*[contains(@class,'RefundMethodItem_change_button')]"));
    private static final Locator BANK_TRANSFER_METHOD = new Locator("BANK_TRANSFER_METHOD", AppiumBy.xpath("//*[contains(@class,'BottomSheet')]//*[contains(@class,'RefundMethodItem_header')]//*[text()='Bank Transfer']"));
    private static final Locator BLIPAY_METHOD = new Locator("BLIPAY_METHOD", AppiumBy.xpath("//*[contains(@class,'RefundMethodItem_body')]//*[contains(text(),'BlibliPay')]"));
    private static final Locator BLIBLI_POINTS_METHOD = new Locator("BLIBLI_POINTS_METHOD", AppiumBy.xpath("//*[normalize-space()='Blibli Tiket Points']"));
    private static final Locator CONTINUE_SELECTED_REFUND_METHOD = new Locator("CONTINUE_SELECTED_REFUND_METHOD", AppiumBy.xpath("//*[contains(@class,'BottomSheet')]//button[contains(@class,'large')]"));
    private static final Locator SELECTED_REFUND_METHOD = new Locator("SELECTED_REFUND_METHOD", AppiumBy.xpath("(//*[contains(@class,'refund_method_name')])"));
    private static final Locator PRICE_BREAKDOWN = new Locator("PRICE_BREAKDOWN", AppiumBy.xpath("//*[@class='refund-payment-section']//p[last()] | //*[contains(@class,'styles_amount_details_item')]/p"));
    private static final Locator TOTAL_ESTIMATED_REFUND_DETAILS = new Locator("TOTAL_ESTIMATED_REFUND_DETAILS", AppiumBy.xpath("//*[contains(@class,'bottom_sheet')]//*[@class='refund-total-section']//div | //*[contains(@class,'styles_total_refund')]/span"));
    private static final Locator REFUND_ID = new Locator("REFUND_ID", AppiumBy.xpath("(//div[contains(@class,'refund_id')]//span)[2]"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("//*[@data-testid='icon-back']"));
    private static final Locator PRODUCT_NAME = new Locator("PRODUCT_NAME", AppiumBy.xpath("(//*[contains(@class,'card_container')]//span[contains(@class,'text')])[1]"));
    private static final Locator PACKAGE_NAME = new Locator("PACKAGE_NAME", AppiumBy.xpath("//*[contains(@class,'styles_middle_content')]//span[1]"));
    private static final Locator BOOK_DATE = new Locator("BOOK_DATE", AppiumBy.xpath("//*[contains(@class,'styles_lower_content')]//span"));
    private static final Locator BOOKED_PRODUCT_NAME = new Locator("BOOKED_PRODUCT_NAME", AppiumBy.xpath("//*[@class='order-title']//span"));
    private static final Locator REFUND_STATUS = new Locator("REFUND_STATUS", AppiumBy.xpath("//li[.//*[contains(@class,'active')]]//span[contains(@class,'Step_title')]"));
    private static final Locator ESTIMATED_RECEIVE_DATE = new Locator("ESTIMATED_RECEIVE_DATE", AppiumBy.xpath("(//li[.//*[contains(@class,'active')]]//div[contains(@class,'date_wrapper')]//span)[1]"));
    private static final Locator HOTEL_REFUND_ESTIMATED_DATE_PROGRESS = new Locator("HOTEL_REFUND_ESTIMATED_DATE_PROGRESS", AppiumBy.xpath("(//div[@class='content border-green']//div[@class='content-info']//div[@class='info-date']//div)[1]"));
    private static final Locator HOTEL_NAME_ORDER = new Locator("HOTEL_NAME_ORDER", AppiumBy.xpath("//*[@class='b2 mobile high-emphasis bold hover tix-font v4']"));
    private static final Locator HOTEL_REFUND_STATUS_PROGRESS = new Locator("HOTEL_REFUND_STATUS_PROGRESS", AppiumBy.xpath("//*[contains(@class,'info-title') and (contains(text(),'Verified by hotel') or contains(text(), 'Diverifikasi hotel'))]"));
    private static final Locator HOTEL_REFUND_ESTIMATED_DATE_DETAIL = new Locator("HOTEL_REFUND_ESTIMATED_DATE_DETAIL", AppiumBy.xpath("//*[@class='refund-estimation-date mobile high-emphasis bold hover tix-font v4']"));
    private static final Locator HOTEL_REFUND_ESTIMATED_PAYMENT_DETAIL = new Locator("HOTEL_REFUND_ESTIMATED_PAYMENT_DETAIL", AppiumBy.xpath("//*[contains(@class,'refund-amount')]"));
    private static final Locator CONTINUE = new Locator("CONTINUE_REFUND", AppiumBy.xpath("//div[contains(@class, 'styles_button_wrapper__')]/button[contains(@class, 'Button_button__')]"));
    private static final Locator CONFIRM_REFUND = new Locator("CONFIRM_REFUND", AppiumBy.xpath("//*[contains(@class,'footer')]//*[contains(@class,'Button_variant_primary')]"));
    private static final Locator CONTINUE_REFUND_POPUP = new Locator("CONTINUE_REFUND_POPUP", AppiumBy.xpath("(//*[contains(@class,'btn_continue_refund')])[1]"));
    private static final Locator GO_TO_ORDER_LIST_PAGE = new Locator("GO_TO_ORDER_LIST_PAGE", AppiumBy.xpath("//*[contains(@class,'Dialog_pop_up') or contains(@class,'Figure_footer')]//button[contains(@class,'Button_button')][.//span[contains(normalize-space(),'Orders') or contains(normalize-space(),'Pesanan')]]"));
    private static final Locator REFUND_ESTIMATIONS_DATES = new Locator("REFUND_ESTIMATIONS_DATES", AppiumBy.xpath("//*[contains(@class,'styles_estimation')]/*[contains(@class,'Text_variant')]"));
    private static final Locator ROOM_DETAILS_HOTEL_NAME = new Locator("HOTEL_NAME_REFUND", AppiumBy.xpath("//*[contains(@class,'card_container')]//*[contains(@class,'title_wrapper')]"));
    private static final Locator ROOM_DETAILS_CHECK_IN = new Locator("ROOM_DETAILS_CHECK_IN", AppiumBy.xpath("//*[text()='Check-in']//following-sibling::*"));
    private static final Locator ROOM_DETAILS_CHECK_OUT = new Locator("ROOM_DETAILS_CHECK_OUT", AppiumBy.xpath("//*[text()='Check-out']//following-sibling::*"));
    private static final Locator ROOM_DETAILS_NIGHT = new Locator("ROOM_DETAILS_NIGHT", AppiumBy.xpath("//*[contains(@class,'addons_detail') and not(contains(@class,'wrapper'))]//span[not(contains(text(),'x'))]"));
    private static final Locator ROOM_DETAILS_ROOM_NAME = new Locator("ROOM_DETAILS_ROOM_NAME", AppiumBy.xpath("//*[contains(@class,'addons_detail') and not(contains(@class,'wrapper'))]//span[contains(text(),'x')]"));
    private static final Locator ROOM_DETAILS_GUEST = new Locator("ROOM_DETAILS_GUEST", AppiumBy.xpath("(//*[contains(@class,'addons_detail_wrapper')]//span)[last()]"));
    private static final Locator REFUND_AMOUNT_PROCESS_PAGE = new Locator("REFUND_AMOUNT_PROCESS_PAGE", AppiumBy.xpath("(//*[contains(@class,'amount')])[2]"));
    private static final Locator REFUND_REASON_ESTIMATED_PAGE = new Locator("REFUND_REASON_ESTIMATED_PAGE", AppiumBy.xpath("//div[contains(@class, 'styles_reason_wrapper__')]/*[contains(@class, 'Text_size_b2')]"));
    private static final Locator BLIPAY_ICON = new Locator("BLIPAY_ICON", AppiumBy.xpath("(//*[contains(@class,'styles_blipay_text')])"));
    private static final Locator REFUND_REGISTERED_PHONE = new Locator("REFUND_REGISTERED_PHONE", AppiumBy.xpath("(//*[contains(@class,'styles_registered_number')])/span"));
    private static final Locator TIMER_COUNTDOWN = new Locator("TIMER_COUNTDOWN", AppiumBy.xpath("//ul[contains(@class,'Countdown_countdown')]"));
    private static final Locator TERM_AND_CONDITION_CHECKBOX = new Locator("TERM_AND_CONDITION_CHECKBOX", AppiumBy.xpath("//*[contains(@class,'wrapper')]//label[contains(@class,'check')]"));
    private static final Locator REFUND_BTN = new Locator("REFUND_BTN", AppiumBy.xpath("//button[contains(@data-testid,'continue-submit-refund')]"));
    private static final Locator CANCEL_REFUND_BUTTON = new Locator("CANCEL_REFUND_BUTTON", AppiumBy.xpath("//*[contains(@class,'dialog')]//*[contains(@class,'Button_variant_primary')]"));
    private static final Locator ESTIMATIONS_FOR_DIFFERENT_DATES = new Locator("ESTIMATIONS_FOR_DIFFERENT_DATES", AppiumBy.xpath("//*[contains(@class,'other_dates_estimation')]"));
    private static final Locator ESTIMATION_PAGE_PRICE = new Locator("ESTIMATION_PAGE_PRICE", AppiumBy.xpath("//*[contains(@class,'styles_amount')]"));
    private static final Locator TOTAL_PRICE_ESTIMATION_PAGE = new Locator("TOTAL_PRICE_ESTIMATION_PAGE", AppiumBy.xpath("(//*[contains(@class,'styles_total')])[2]/*[contains(@class,'Text') and not(contains(text(),'-'))]"));
    private static final Locator AIRLINE_NAME = new Locator("AIRLINE_NAME", AppiumBy.xpath("//div[contains(@class,'styles_airline_wrapper')]//following-sibling::*"));
    private static final Locator DEPARTURE_AIRPORT = new Locator("DEPARTURE_AIRPORT", AppiumBy.xpath("//div[contains(@class,'styles_flight_route')]//p[1]"));
    private static final Locator ARRIVAL_AIRPORT = new Locator("ARRIVAL_AIRPORT", AppiumBy.xpath("//div[contains(@class,'styles_flight_route')]//p[2]"));
    private static final Locator CHECKBOX_PASSENGER = new Locator("CHECKBOX_PASSENGER", AppiumBy.xpath("//*[@data-testid='check-passenger']"));
    private static final Locator SPECIFIC_REASON_DROPDOWN = new Locator("SPECIFIC_REASON_DROPDOWN", AppiumBy.xpath("//*[contains(@class,'styles_clickable_icon')]"));
    private static final Locator PRICE_POSITIVE = new Locator("PRICE_POSITIVE", AppiumBy.xpath("//div[not(@class)]/*[contains(@class,'styles_amount_details_item')]/p"));
    private static final Locator PRICE_NEGATIVE = new Locator("PRICE_NEGATIVE", AppiumBy.xpath("//div[contains(@class,'styles_negative_breakdown')]/*[contains(@class,'styles_amount_details_item')]/p"));
    private static final Locator CHECKBOX_TNC = new Locator("CHECKBOX_TNC", AppiumBy.xpath("//*[@data-testid='checklist-tnc']"));
    private static final Locator BACK_BUTTON_DETAIL = new Locator("BACK_BUTTON_DETAIL", AppiumBy.xpath("//*[@data-testid='icon-back']"));
    private static final Locator REFUND_HEADER_TITLE = new Locator("REFUND_HEADER_TITLE", AppiumBy.xpath("//*[contains(@data-testid,'title-header') and (contains(text(),'Refund'))]"));
    private static final Locator CANCEL_REFUND_REQUEST_TITLE_POP_UP = new Locator("CANCEL_REFUND_REQUEST_TITLE_POP_UP", AppiumBy.xpath("//*[contains(@class,'Figure_title') and (contains(text(),'Cancel refund request') or contains(text(),'Batalkan pengajuan refund?'))]"));
    private static final Locator CANCEL_BTN_CONFIRMATION_POP_UP = new Locator("CANCEL_BTN_CONFIRMATION_POP_UP", AppiumBy.xpath("//button[contains(@class,'Button_button') and .//*[contains(normalize-space(.),'Cancel Refund') or contains(normalize-space(.),'Batalkan Refund')]]"));
    private static final Locator RE_CHECK_BTN_CONFIRMATION_POP_UP = new Locator("RE_CHECK_BTN_CONFIRMATION_POP_UP", AppiumBy.xpath("//*[contains(@class,'Figure_secondary') and (contains(text(),'Re-check') or contains(text(),'Cek Ulang'))]"));
    private static final Locator CROSS_BTN_REFUND_PAGE = new Locator("CROSS_BTN_REFUND_PAGE", AppiumBy.xpath("//*[contains(@class,'BottomSheet_close_button')]"));
    private static final Locator SELECTED_REASON_REFUND_PAGE_1 = new Locator("SELECTED_REASON_REFUND_PAGE_1", AppiumBy.xpath("//*[contains(@class,'styles_reason')]//*[contains(@class,'Text_text')]"));
    private static final Locator ESTIMATION_REFUND_DETAIL_CHEVRON = new Locator("ESTIMATION_REFUND_DETAIL_CHEVRON", AppiumBy.xpath("//*[contains(@data-testid,'icon-chevron')]"));
    private static final Locator ESTIMATION_ON_OTHER_DATE_TITLE = new Locator("ESTIMATION_ON_OTHER_DATE_TITLE", AppiumBy.xpath("//*[contains(@class,'BottomSheet_body_title') and (contains(text(),'Estimated Refund Amount') or contains(text(),'Estimasi Jumlah Refund'))]"));
    private static final Locator HELP_CENTER_GUIDE = new Locator("HELP_CENTER_GUIDE", AppiumBy.xpath("//*[contains(@class,'styles_refund_guide')]"));
    private static final Locator HELP_CENTER_GUIDE_ON_GUIDE_PAGE_TITLE = new Locator("HELP_CENTER_GUIDE", AppiumBy.xpath("//*[contains(@class,'header_title') and (contains(text(),'Help Center') or contains(text(),'Pusat Bantuan'))]"));
    private static final Locator HELP_CENTER_GUIDE_ON_GUIDE_PAGE_BACK_BTN = new Locator("HELP_CENTER_GUIDE_ON_GUIDE_PAGE_BACK_BTN", AppiumBy.xpath("//*[contains(@data-testid,'btnBack')]"));
    private static final Locator IMPORTANT_INFO_BTN = new Locator("IMPORTANT_INFO_BTN", AppiumBy.xpath("//*[contains(@data-testid,'card-important-info')]"));
    private static final Locator IMPORTANT_INFO_TITLE_POP_UP = new Locator("IMPORTANT_INFO_TITLE_POP_UP", AppiumBy.xpath("//*[contains(@class,'BottomSheet_body_title') and (contains(text(),'Important info') or contains(text(),'Info penting'))]"));
    private static final Locator TERMS_AND_CONDITIONS = new Locator("TERMS_AND_CONDITIONS", AppiumBy.xpath("//*[@data-testid='tnc-text']"));
    private static final Locator TERMS_AND_CONDITIONS_TITLE_POP_UP = new Locator("TERMS_AND_CONDITIONS_TITLE_POP_UP", AppiumBy.xpath("//*[contains(@class,'BottomSheet_body_title') and (contains(text(),'Terms & Conditions') or contains(text(),'Syarat & Ketentuan'))]"));
    private static final Locator SUBMIT_TO_REFUND_DISABLED_BTN = new Locator("SUBMIT_TO_REFUND_DISABLED_BTN", AppiumBy.xpath("//*[contains(@data-testid,'continue-submit-refund') and contains(@class,'Button_state_disabled')]"));
    private static final Locator REFUND_CONFIRMATION_TITLE_POP_UP = new Locator("REFUND_CONFIRMATION_TITLE_POP_UP", AppiumBy.xpath("//*[contains(@class,'Figure_title') and (contains(text(),'Refund Confirmation') or contains(text(),'Konfirmasi Refund'))]"));
    private static final Locator RE_CHECK_BTN_REFUND_CONFIRMATION_POP_UP = new Locator("RE_CHECK_BTN_REFUND_CONFIRMATION_POP_UP", AppiumBy.xpath("//*[contains(@class,'Figure_secondary') and (contains(text(),'Re-Check') or contains(text(),'Cek Ulang'))]"));
    private static final Locator EDIT_BANK_ACCOUNT = new Locator("EDIT_BANK_ACCOUNT", AppiumBy.className("RefundMethodItem_bank_account_wrapper__pcKIC"));
    private static final Locator ADD_BUTTON_BANK_ACCOUNT = new Locator("ADD_BUTTON_BANK_ACCOUNT", AppiumBy.className("SelectBankAccount_icon__2SQsf"));
    private static final Locator ACCOUNT_NUMBER = new Locator("ACCOUNT_NUMBER", AppiumBy.id("account-number"));
    private static final Locator BANK_NAME = new Locator("BANK_NAME", AppiumBy.id("bank-name"));
    private static final Locator INPUT_BANK_NAME = new Locator("INPUT_BANK_NAME", AppiumBy.xpath("//*[contains(@class,'SearchBox')]//input"));
    private static final Locator CHECK_BANK_ACCOUNT = new Locator("CHECK_BANK_ACCOUNT", AppiumBy.xpath("//p[contains(@class,'Text_variant_activity')]"));
    private static final Locator OTP_INPUT_CHECK_BANK_ACCOUNT = new Locator("OTP_INPUT_CHECK_BANK_ACCOUNT", AppiumBy.xpath("//*[contains(@class,'AddBankAccount_otp_input')]//input"));
    private static final Locator WRONG_OTP_TEXT = new Locator("OTP_WRONG", AppiumBy.xpath("//*[contains(@class,'AddBankAccount_error')]"));
    private static final Locator ACCOUNT_HOLDER_NAME = new Locator("ACCOUNT_HOLDER_NAME", AppiumBy.id("account-holder-name"));
    private static final Locator CLOSE_OTP_BOTTOMSHEET = new Locator("CLOSE_BTN", AppiumBy.xpath("(//*[contains(@class,'BottomSheet_close_button')])[1]"));
    private static final Locator RESCHEDULE_NUDGE = new Locator("RESCHEDULE_NUDGE", AppiumBy.xpath("//*[contains(@class,'styles_reschedule_nudge')]"));
    private static final Locator RESCHEDULE_NUDGE_TITLE = new Locator("RESCHEDULE_NUDGE_TITLE", AppiumBy.xpath("//*[contains(@class,'styles_title') and (contains(text(),'reschedule?') or contains(text(),'refund'))]"));
    private static final Locator RESCHEDULE_NUDGE_BACK = new Locator("RESCHEDULE_NUDGE_BACK", AppiumBy.xpath("//*[contains(@class,'styles_back_button')]"));
    private static final Locator RESCHEDULE_NUDGE_REFUND_BTN = new Locator("RESCHEDULE_NUDGE_REFUND_BTN", AppiumBy.xpath("//*[contains(@class,'styles_btn_continue_refund') and contains(@class,'Button_variant_secondary')]"));
    private static final Locator RESCHEDULE_NUDGE_RESCHEDULE_BTN = new Locator("RESCHEDULE_NUDGE_RESCHEDULE_BTN", AppiumBy.xpath("//*[contains(@class,'styles_btn_continue_refund') and contains(@class,'Button_variant_primary')]"));
    private static final Locator REFUND_DIALOG_POPUP = new Locator("REFUND_DIALOG_POPUP", AppiumBy.xpath("//*[contains(@class,'Figure_title')]"));
    private static final Locator REFUND_FLOW_FIRST_CONTINUE_BTN = new Locator("REFUND_FLOW_FIRST_CONTINUE_BTN", AppiumBy.xpath("(//button[contains(@class,'Button_variant_primary')][.//span[contains(normalize-space(),'Continue') or contains(normalize-space(),'Lanjutkan')]])[1]"));
    private static final Locator REFUND_SUBMIT_REQUEST_PRIMARY_BTN = new Locator("REFUND_SUBMIT_REQUEST_PRIMARY_BTN", AppiumBy.xpath("//button[@data-testid='continue-submit-refund']"));
    private static final Locator REFUND_PROCESS_REFUND_PRIMARY_BTN = new Locator("REFUND_PROCESS_REFUND_PRIMARY_BTN", AppiumBy.xpath("//button[contains(@class,'Button_variant_primary')][.//span[contains(.,'Process Refund') or contains(.,'Proses')]]"));
    private static final Locator REFUND_VIEW_REFUND_DETAIL_BTN = new Locator("REFUND_VIEW_REFUND_DETAIL_BTN", AppiumBy.xpath("//button[contains(@class,'Button_variant_primary')][.//span[contains(.,'View Refund Detail') or contains(.,'Detail Refund') or contains(.,'Lihat Detail')]]"));
    private static final Locator REFUND_COMPLETED_STEP_TITLE = new Locator("REFUND_COMPLETED_STEP_TITLE", AppiumBy.xpath("//span[contains(@class,'Step_title')][contains(.,'Refund has completed') or contains(.,'selesai') or contains(.,'completed')]"));
    private static final Locator CHANGE_METHOD_BUTTON = new Locator("CHANGE_METHOD_BUTTON", AppiumBy.xpath("//div[@role='button']//p[normalize-space()='Change method' or normalize-space()='Ganti metode']"));
    private static final Locator TIKET_POINT_RADIO = new Locator("TIKET_POINT_RADIO", AppiumBy.xpath("//label[.//input[@name='selectRefundMethod' and @value='\"tix_point\"']]"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.xpath("//button[.//span[normalize-space()='Continue' or normalize-space()='Lanjut']]"));

    public RefundPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> RefundPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new RefundPageAndroid<>(session);
            }
            case IOS -> {
                return new RefundPageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public String getHotelName() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("refunds");
        String hotelName = driverTools.getText(ROOM_DETAILS_HOTEL_NAME).orElseThrow();
        driverTools.switchToNative();
        return hotelName;
    }

    public String getCheckInDate() {
        driverTools.switchToWebViewUrl("refunds");
        String date = driverTools.getText(ROOM_DETAILS_CHECK_IN).orElseThrow();
        driverTools.switchToNative();
        return date;
    }

    public String getCheckOutDate() {
        driverTools.switchToWebViewUrl("refunds");
        String date = driverTools.getText(ROOM_DETAILS_CHECK_OUT).orElseThrow();
        driverTools.switchToNative();
        return date;
    }

    public int getTotalNight() {
        driverTools.switchToWebViewUrl("refunds");
        String[] nights = driverTools.getText(ROOM_DETAILS_NIGHT).orElseThrow().split(" ");
        driverTools.switchToNative();
        return Integer.parseInt(nights[0]);
    }

    public String getRoomName() {
        driverTools.switchToWebViewUrl("refunds");
        String roomName = driverTools.getText(ROOM_DETAILS_ROOM_NAME).orElseThrow();
        driverTools.switchToNative();
        return roomName;
    }

    public int getTotalGuest() {
        driverTools.switchToWebViewUrl("refunds");
        String[] guests = driverTools.getText(ROOM_DETAILS_GUEST).orElseThrow().split(" ");
        driverTools.switchToNative();
        return Integer.parseInt(guests[0]);
    }

    public String getRefundProductName() {
        driverTools.waitForElementToDisappear(LOADING_STATE, 30);
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("refund", 5);
        driverTools.waitForElementToAppear(PRODUCT_NAME).orElseThrow();
        String productName = driverTools.getText(PRODUCT_NAME).orElseThrow();
        driverTools.switchToNative();
        return productName;
    }

    public String getRefundStatus() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("refunds");
        List<WebElement> refundStatusItems = driverTools.elements(REFUND_STATUS).orElseThrow();
        String refundStatus = driverTools.getText(refundStatusItems.get(refundStatusItems.size() - 1));
        driverTools.switchToNative();
        return refundStatus;
    }

    public String getEstimatedReceiveDate(String language) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("refunds");

        String estimatedReceiveDate = driverTools.getText(ESTIMATED_RECEIVE_DATE).orElseThrow();
        if (estimatedReceiveDate.contains(": ")) {
            String[] estimatedReceiveDateSplit = estimatedReceiveDate.split(": ");
            estimatedReceiveDate = estimatedReceiveDateSplit[1];
        }
        if (estimatedReceiveDate.contains(", ")) {
            String[] estimatedReceiveDateSplit = estimatedReceiveDate.split(", ");
            estimatedReceiveDate = estimatedReceiveDateSplit[1];
        }

        String[] dateSplit = estimatedReceiveDate.split(" ");
        String monthShort = dateSplit[1];
        String monthLong;
        if (language.equals("en")) {
            monthLong = monthShortToLongEng(monthShort);
        } else {
            monthLong = monthShortToLongId(monthShort);
        }
        estimatedReceiveDate = estimatedReceiveDate.replace(monthShort, monthLong);

        logger.log("estimatedReceiveDate-> " + estimatedReceiveDate);
        driverTools.switchToNative();
        return estimatedReceiveDate;
    }

    public void clickBackButton() {
        driverTools.switchToWebViewUrl("my-refund/v4/refunds");
        driverTools.click(BACK_BUTTON);
        driverTools.switchToNative();
    }

    public abstract String chooseRefundReason();

    public abstract String choosePersonalReason();

    public String chooseRefundReasonWebView() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("refund", 5);
        WebElement selectReasonSection = driverTools.waitForElementToBeClickable(SELECT_REASON_SECTION, 10).orElseThrow();
        driverTools.click(selectReasonSection);

        int maxOption = driverTools.elements(REFUND_REASON).orElseThrow().size();
        String reason = chooseRefundReasonByIndex(CoreFunctions.generateRandomInt(1, maxOption));
        driverTools.switchToNative();
        logger.log("Reason: " + reason);
        return reason;
    }

    public String chooseRefundReasonWebView(int reasonIndex) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("refund", 5);
        WebElement selectReasonSection = driverTools.waitForElementToBeClickable(SELECT_REASON_SECTION, 10).orElseThrow();
        driverTools.click(selectReasonSection);

        String reason = chooseRefundReasonByIndex(reasonIndex);
        driverTools.switchToNative();
        logger.log("Reason: " + reason);
        return reason;
    }

    public String chooseRefundReasonWebView(String reasonTitle) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("refund", 5);
        WebElement selectReasonSection = driverTools.waitForElementToBeClickable(SELECT_REASON_DROPDOWN, 10).orElseThrow();
        driverTools.click(selectReasonSection);

        String reason = chooseRefundReasonByTitle(reasonTitle);
        driverTools.switchToNative();
        logger.log("Reason: " + reason);
        return reason;
    }

    public String chooseRefundReasonWebViewFlight() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("refund", 5);
        WebElement selectReasonSection = driverTools.waitForElementToBeClickable(SELECT_REASON_SECTION, 10).orElseThrow();
        driverTools.click(selectReasonSection);
        String reason = chooseRefundReasonByIndex(1);
        driverTools.switchToNative();
        logger.log("Reason: " + reason);
        return reason;
    }

    private String chooseRefundReasonByIndex(int index) {
        List<WebElement> reasonToBeSelected = driverTools.elements(REFUND_REASON).orElseThrow();
        String reason = driverTools.getText(reasonToBeSelected.get(index - 1));
        driverTools.clickWithJavascriptExecutor(reasonToBeSelected.get(index - 1));
        return reason;
    }

    private String chooseRefundReasonByTitle(String reasonTitle) {
        String reason;
        if (reasonTitle.equalsIgnoreCase("random")) {
            reason = chooseRefundReasonByIndex(CoreFunctions.generateRandomInt(1, driverTools.elements(REFUND_REASON).orElseThrow().size()));
        } else {
            String[] splitData = splitLanguageData(reasonTitle, "/");
            String reasonEn = splitData[0];
            String reasonId = splitData[1];
            reason = driverTools.getText(getReasonLocator(reasonEn, reasonId)).orElseThrow();
            driverTools.clickWithJavascriptExecutor(getReasonLocator(reasonEn, reasonId));
        }
        return reason;
    }

    private Locator getReasonLocator(int index) {
        return new Locator("REFUND_REASON", AppiumBy.xpath("(//*[contains(@class,'Radio_children')]//*[contains(@class,'styles_radio')]//*[contains(@class,'text')])[" + index + "]"));
    }

    private Locator getReasonLocator(String reasonEn, String reasonId) {
        return new Locator("REFUND_REASON", AppiumBy.xpath("//*[contains(@class,'Radio_children')]//*[contains(@class,'styles_radio')]//*[contains(@class,'text') and (contains(text(),'" + reasonEn + "') or contains(text(),'" + reasonId + "'))]"));
    }

    public abstract boolean isCorrectReasonShowing(String reason);

    public abstract boolean isEstimationShowing();

    public String getOrderIdRefundDetails() {
        driverTools.waitForElementToDisappear(LOADING_STATE, 15);
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.waitForElementToAppear(ORDER_ID_REFUND_DETAIL, 3);
        String refundIdTitleText = driverTools.getText(ORDER_ID_REFUND_DETAIL).orElseThrow();
        logger.log("Order ID on Refund Details: " + refundIdTitleText);
        driverTools.switchToNative();
        return refundIdTitleText;
    }

    public String getRefundHotelNameOrder() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.waitForElementToAppear(HOTEL_NAME_ORDER, 30).orElseThrow();
        String hotelNameOrder = driverTools.getText(HOTEL_NAME_ORDER).orElseThrow();
        logger.log("Hotel Name Order Section: " + hotelNameOrder);
        driverTools.switchToNative();
        return hotelNameOrder;
    }

    public String getRefundIdRefundDetails() {
        driverTools.switchToWebView();
        String refundId = driverTools.getText(REFUND_ID).orElseThrow();
        logger.log("Refund ID: " + refundId);
        driverTools.switchToNative();
        return refundId.trim();
    }

    public String getHotelRefundStatusProgress(String PreviousRefundStatus) {
        driverTools.switchToWebView();
        String statusProgress = driverTools.getText(getRefundStatusLocator(PreviousRefundStatus)).orElseThrow();
        logger.log("Refund Status Progress Section: " + statusProgress);
        driverTools.switchToNative();
        return statusProgress;
    }

    private Locator getRefundStatusLocator(String PreviousRefundStatus) {
        return new Locator("REFUND_STATUS", AppiumBy.xpath("//*[contains(@class,'info-title') and contains(text(),'" + PreviousRefundStatus + "')]"));
    }

    public String getHotelRefundEstimatedDateProgress() {
        driverTools.switchToWebView();
        String estimatedDateProgressText = driverTools.getText(HOTEL_REFUND_ESTIMATED_DATE_PROGRESS).orElseThrow();
        // sample estimated date progress text -> Estimation: 1 Jan 2023
        String[] estimatedDateProgressArray = estimatedDateProgressText.split(": ");
        String estimatedDateProgress = estimatedDateProgressArray[1];
        // sample estimatedDateProgress = 1 Jan 2023
        logger.log("Estimated Date Progress Section: " + estimatedDateProgress);
        driverTools.switchToNative();
        return estimatedDateProgress;
    }

    public String getHotelRefundEstimatedDateDetail() {
        driverTools.switchToWebView();
        String estimatedDateDetailText = driverTools.getText(HOTEL_REFUND_ESTIMATED_DATE_DETAIL).orElseThrow();
        // sample estimated date detail text -> 1 January 2023
        String[] estimatedDateDetailOld = estimatedDateDetailText.split(" ");
        // get first three letters of month (ex: January -> Jan)
        String month = estimatedDateDetailOld[1].substring(0, 3);
        String[] estimatedDateDetailNew = new String[]{estimatedDateDetailOld[0], month, estimatedDateDetailOld[2]};
        String estimatedDateDetail = String.join(" ", estimatedDateDetailNew);
        // sample estimatedDateDetail = 1 Jan 2023
        logger.log("Estimated Date Detail Section: " + estimatedDateDetail);
        driverTools.switchToNative();
        return estimatedDateDetail;
    }

    public String getHotelRefundEstimatedPaymentDetail() {
        driverTools.switchToWebView();
        String estimatedPaymentDetail = driverTools.getText(HOTEL_REFUND_ESTIMATED_PAYMENT_DETAIL).orElseThrow();
        logger.log("Estimated Payment Detail Section: " + estimatedPaymentDetail);
        driverTools.switchToNative();
        return estimatedPaymentDetail;
    }

    public void clickProcessRefundBtn() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.scrollIntoWebView(CONTINUE);
        driverTools.clickWithJavascriptExecutor(CONTINUE);
        if (driverTools.waitForElementToPresence(CONFIRM_REFUND, 5).isPresent()) {
            driverTools.click(CONFIRM_REFUND);
        }
        driverTools.switchToNative();
    }

    public void continueToRefundPopup() {
        driverTools.switchToWebViewUrl("my-refund");
        if (driverTools.waitForElementToPresence(CONTINUE_REFUND_POPUP, 3).isPresent()) {
            driverTools.clickWithJavascriptExecutor(CONTINUE_REFUND_POPUP);
            if (!driverTools.waitForElementToDisappear(CONTINUE_REFUND_POPUP, 3)) {
                driverTools.click(CONTINUE_REFUND_POPUP);
            }
        }

        driverTools.switchToNative();
    }

    public abstract boolean isFullRefund(String price);

    public abstract Optional<Integer> getTotalEstimatedRefund();

    public Double getTotalEstimatedRefundWebView() {
        driverTools.switchToWebViewUrl("refund");
        WebElement refundAmount = driverTools.swipeToElement(REFUND_AMOUNT, Swipe.UP, 5).orElseThrow();
        Double refundAmountLabel = CoreFunctions.convertPriceSafely(driverTools.getText(refundAmount));
        logger.log("refundAmountLabel-> " + refundAmountLabel);
        driverTools.switchToNative();
        return refundAmountLabel;
    }

    public String getEstimatedRefundReasonWebView() {
        driverTools.switchToWebViewUrl("my-refund");
        WebElement refundReason = driverTools.swipeToElement(REFUND_REASON_ESTIMATED_PAGE, Swipe.UP, 5).orElseThrow();
        String refundReasonText = (driverTools.getText(refundReason));
        driverTools.switchToNative();
        return refundReasonText;
    }

    public String getTheRefundCurrency() {
        driverTools.switchToWebView();
        WebElement refundAmount = driverTools.swipeToElement(REFUND_AMOUNT, Swipe.UP, 5).orElseThrow();
        String totalAmmount = driverTools.getText(refundAmount);
        logger.log("totalamount-> " + totalAmmount);
        String currency = driverTools.getText(refundAmount).split(" ")[0];
        logger.log("currency-> " + currency);
        driverTools.switchToNative();
        return currency;
    }

    public abstract String getOrderIdRefundPage();

    public boolean isUsingBlipay() {
        driverTools.switchToWebViewUrl("my-refund");
        boolean blipayIcon = driverTools.waitForElementToAppear(BLIPAY_ICON).isPresent();
        driverTools.switchToNative();
        return blipayIcon;
    }

    public void submitRefundCorporateBilling() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.swipeToElement(REFUND_BTN_WEBVIEW, Swipe.UP, 10).orElseThrow();
        List<Locator> termConditionCheckbox = new LinkedList<>();
        termConditionCheckbox.add(TERM_AND_CONDITION_CHECKBOX_WEBVIEW1);
        termConditionCheckbox.add(TERM_AND_CONDITION_CHECKBOX_WEBVIEW2);
        driverTools.click(termConditionCheckbox, driverTools::isElementDisplayed);
        driverTools.swipeUp();
        WebElement submitRefundBtn = driverTools.waitForElementToAppear(SUBMIT_REFUND).orElseThrow();
        driverTools.click(submitRefundBtn);

        WebElement submitBtnPopup = driverTools.waitForElementToAppear(POPUP_PROCESS_REFUND_BTN_WEBVIEW, 20).orElseThrow();
        driverTools.click(submitBtnPopup);
        driverTools.switchToNative();
    }
    public void submitRefundPoint()
    {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.swipeToElement(REFUND_BTN_WEBVIEW, Swipe.UP, 10).orElseThrow();
        if (driverTools.waitForElementToAppear(CHANGE_METHOD_BUTTON, 3).isPresent()) {
            driverTools.clickWithJavascriptExecutor(CHANGE_METHOD_BUTTON);
            driverTools.clickWithJavascriptExecutor(TIKET_POINT_RADIO);
            driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON);
        }
        driverTools.waitForElementToAppear(TERM_AND_CONDITION_CHECKBOX_WEBVIEW1, 10);
        List<Locator> termConditionCheckbox = new LinkedList<>();
        termConditionCheckbox.add(TERM_AND_CONDITION_CHECKBOX_WEBVIEW1);
        termConditionCheckbox.add(TERM_AND_CONDITION_CHECKBOX_WEBVIEW2);
        driverTools.click(termConditionCheckbox, driverTools::isElementDisplayed);
        driverTools.swipeUp();
        WebElement submitRefundBtn = driverTools.waitForElementToAppear(SUBMIT_REFUND).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SUBMIT_REFUND);

        WebElement submitBtnPopup = driverTools.waitForElementToAppear(POPUP_PROCESS_REFUND_BTN_WEBVIEW, 20).orElseThrow();
        driverTools.click(submitBtnPopup);
        driverTools.switchToNative();
    }

    public void submitRefund() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.swipeToElement(REFUND_BTN_WEBVIEW, Swipe.UP, 10).orElseThrow();
        List<Locator> termConditionCheckbox = new LinkedList<>();
        termConditionCheckbox.add(TERM_AND_CONDITION_CHECKBOX_WEBVIEW1);
        termConditionCheckbox.add(TERM_AND_CONDITION_CHECKBOX_WEBVIEW2);
        driverTools.click(termConditionCheckbox, driverTools::isElementDisplayed);
        driverTools.swipeUp();
        WebElement submitRefundBtn = driverTools.waitForElementToAppear(SUBMIT_REFUND).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SUBMIT_REFUND);

        WebElement submitBtnPopup = driverTools.waitForElementToAppear(POPUP_PROCESS_REFUND_BTN_WEBVIEW, 20).orElseThrow();
        driverTools.click(submitBtnPopup);
        driverTools.switchToNative();
    }

    public void submitRefundCashBackFreePayment() {
        driverTools.switchToWebViewUrl("refund");
        driverTools.click(CHECKBOX_REFUND);
        driverTools.swipeUp();
        driverTools.click(SUBMIT_REFUND);
        driverTools.waitForElementToDisappear(SUBMIT_REFUND);
        driverTools.switchToNative();
    }
    public String getRefundPointAmount() {
        driverTools.switchToWebViewUrl("refund");
        driverTools.waitForElementToAppear(TOTAL_REFUND_AMOUNT);
        driverTools.scrollIntoWebView(TOTAL_REFUND_AMOUNT);

        String points = driverTools.getText(TOTAL_REFUND_AMOUNT).orElseThrow();

        driverTools.switchToNative();
        return points;
    }

    public String getEstimatedRefund() {
        driverTools.switchToWebViewUrl("refund");
        driverTools.waitForElementToAppear(TOTAL_PRICE_ESTIMATION_PAGE);
        driverTools.scrollIntoWebView(TOTAL_PRICE_ESTIMATION_PAGE);
        String refundReceive = driverTools.getText(REFUND_RECEIVE).orElseThrow();
        String refundFee = driverTools.getText(REFUND_FEE).orElseThrow();
        String refundAmount = driverTools.getText(TOTAL_PRICE_ESTIMATION_PAGE).orElseThrow();
        String value = null;
        if (convertPriceSafely(refundFee.trim()) != 0) {
            value = refundAmount;
        } else {
            value = refundReceive;
        }
        driverTools.switchToNative();
        return value;
    }
    public String getEstimatedRefundPoint()
    {
        driverTools.switchToWebViewUrl("refund");
        driverTools.waitForElementToAppear(TOTAL_PRICE_ESTIMATION_PAGE);
        driverTools.scrollIntoWebView(TOTAL_PRICE_ESTIMATION_PAGE);
        String refundReceive = driverTools.getText(REFUND_RECEIVE).orElseThrow();
        refundReceive = refundReceive.replaceAll("[^0-9,\\.]", "").trim();
        driverTools.switchToNative();
        return refundReceive;
    }


    private void fillOtp() {
        if (driverTools.waitForElementToAppear(OTP_FEILD_WEBVIEW).isPresent()) {
            driverTools.sendKeys(OTP_FEILD_WEBVIEW, "1234");
        } else {
            logger.log("There is no OTP refund");
        }
    }

    public void submitRefundReschedule() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.waitForElementToPresence(TIMER_COUNTDOWN, 15);
        driverTools.scrollIntoWebView(TERM_AND_CONDITION_CHECKBOX);
        driverTools.clickWithJavascriptExecutor(TERM_AND_CONDITION_CHECKBOX);
        driverTools.scrollIntoWebView(REFUND_BTN);
        driverTools.clickWithJavascriptExecutor(REFUND_BTN);
        driverTools.switchToNative();
    }

    public boolean checkRefundSubmission() {
        driverTools.switchToWebViewUrl("my-refund");
        WebElement refundSubmissionMsg = driverTools.waitForElementToAppear(REFUND_SUBMISSION_POPUP_MSG_WEBVIEW, 15).orElseThrow();
        boolean isRefundSuccessful = driverTools.isElementDisplayed(refundSubmissionMsg);
        driverTools.switchToNative();
        logger.log("Refund Submission Successful: " + isRefundSuccessful);
        return isRefundSuccessful;
    }

    public abstract boolean checkAllOrderId(String orderId1, String orderId2, String orderId3);

    public abstract void seeOrderList();

    public abstract void clickContinueButton();

    public boolean checkRefundMethodTixPoints() {
        driverTools.switchToWebView();
        boolean tiketIcon = driverTools.waitForElementToAppear(TIKET_POINT_ICON).isPresent();
        driverTools.switchToNative();
        return tiketIcon;
    }

    public String getRefundMethod() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("refund");
        driverTools.swipeUp(3);
        Optional<WebElement> refundMethodName = driverTools.element(REFUND_METHOD_NAME_NON_BLIPAY);
        if (refundMethodName.isPresent()) {
            return driverTools.getText(refundMethodName.get());
        }
        String refundMethod = "";
        if (driverTools.element(REFUND_METHOD_BLIBLIPAY_ICON).isPresent()) refundMethod = "blipay";
        driverTools.switchToNative();
        return refundMethod;
    }

    private Locator getPriceDetailLocator(String name) {
        return new Locator(name.toUpperCase().replace(" ", "_"), AppiumBy.xpath("//*[contains(@class,'refund-payment-section')]//*[contains(text(),'" + name + "')]//following-sibling::*[last()]"));
    }

    public boolean isTiketPointAndTotalPriceCorrect(int tiketPoints, int totalPrice) {
        driverTools.switchToWebView();
        driverTools.click(PRICE_DETAIL_DROPDOWN_ARROW);
        driverTools.waitForElementToAppear(PRICE_LOCATOR).orElseThrow();

        List<WebElement> prices = driverTools.elements(PRICE_LOCATOR).orElseThrow();
        ArrayList<Integer> arrayPrice = new ArrayList<Integer>();
        for (WebElement element : prices) {
            String text = driverTools.getText(element);
            try {
                int price = (int) CoreFunctions.convertPrice(text);
                logger.log("price got: " + price);
                arrayPrice.add(price);
            } catch (NumberFormatException ignored) {
                logger.log(ignored.getMessage());
            }
        }

        int sum = 0;
        for (double i : arrayPrice) {
            if (i > 0)
                sum += i;
        }
        int actualPrice = sum - tiketPoints;
        logger.log("actual price: " + actualPrice);
        logger.log("expected price: " + totalPrice);

        boolean isTotalPrice = actualPrice == totalPrice;

        String actualTiketPoints = driverTools.getText(getPriceDetailLocator("Tiket Points")).orElseThrow();
        int actualTiketPoint = CoreFunctions.getIntFromString(actualTiketPoints).orElseThrow();
        logger.log("actual Tiket Points: " + actualTiketPoint);
        logger.log("expected Tiket Points: " + tiketPoints);

        driverTools.click(CLOSE_PRICE_DETAIL_BTN);
        driverTools.switchToNative();
        boolean isTiketPriceCorrect = actualTiketPoint == tiketPoints;
        return isTiketPriceCorrect && isTotalPrice;
    }

    public void viewRefundDetailPage() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.click(VIEW_REFUND_DETAIL_BTN);
        driverTools.switchToNative();
    }

    public String getSubmitTotalEstimatedRefund() {
        driverTools.switchToWebViewUrl("my-refund");
        String totalEstimatedRefund = driverTools.getText(TOTAL_ESTIMATED_REFUND).orElseThrow();
        driverTools.switchToNative();
        return totalEstimatedRefund;
    }

    public void openEstimatedRefundDetail() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.waitForElementToAppear(ESTIMATED_TOTAL_PAYMENT_BTN);
        driverTools.click(ESTIMATED_TOTAL_PAYMENT_BTN);
        driverTools.switchToNative();
    }

    public double calculatePriceBreakdown() {
        double result = 0;
        ArrayList<Double> arrayPrice = new ArrayList<>();

        driverTools.switchToWebViewUrl("my-refund");
        List<WebElement> prices = driverTools.elements(PRICE_BREAKDOWN).orElseThrow();
        logger.log("total price breakdown-> " + prices.size());
        for (WebElement element : prices) {
            String text = driverTools.getText(element);
            double price = CoreFunctions.convertPrice(text);
            logger.log("price-> " + price);
            arrayPrice.add(price);
        }
        for (double i : arrayPrice) {
            result += i;
        }
        driverTools.switchToNative();

        logger.log("result-> " + result);
        return result;
    }

    private Locator getCancelationFeeLocator(String currency) {
        return new Locator("CANCELLATION_FEE_PRICE_BREAK_DOWN", AppiumBy.xpath("//*[contains(@class,'styles_amount_label')]//p[contains(text(),'Cancellation fee') or contains(text(),'Biaya Pembatalan')]//parent::*//following-sibling::p[contains(text(),'" + currency + "')]"));
    }

    public String getCancellationPrice(String currency) {
        driverTools.switchToWebViewUrl("my-refund");
        WebElement cancellationFeeElement = driverTools.waitForElementToAppear(getCancelationFeeLocator(currency), 5).orElseThrow();
        String cancellationFeeText = driverTools.getText(cancellationFeeElement).replace("-", "");
        logger.log("Cancellation Fee: " + cancellationFeeText);
        driverTools.switchToNative();
        return cancellationFeeText;
    }

    public boolean isPriceBreakdownAppear() {
        driverTools.switchToWebViewUrl("my-refund");
        boolean status = driverTools.waitForElementToAppear(PRICE_BREAKDOWN, 2).isPresent();
        driverTools.switchToNative();
        return status;
    }

    public String getTotalEstimatedRefundDetails() {
        driverTools.switchToWebView();
        Optional<String> totalRefundOptional = driverTools.getText(TOTAL_ESTIMATED_REFUND_DETAILS);
        String totalRefund = totalRefundOptional.isPresent() ? totalRefundOptional.get() : "";
        driverTools.switchToNative();
        totalRefund = String.valueOf(CoreFunctions.convertPriceSafely(totalRefund));
        logger.log("Total Estimated Refund Details: " + totalRefund);
        return totalRefund;
    }


    public void closeTheRefundPaymentDetails() {
        driverTools.switchToWebViewUrl("my-refund");
        try {
            driverTools.click(CLOSE_PRICE_DETAIL_BTN);
            return;
        } catch (Exception e) {
            logger.log("Failed to closing refund payment details, error: " + e.getMessage());
        }
        driverTools.switchToNative();
        logger.log("Back from the device screen");
        driverTools.back();
    }

    public String getPromoAmountDetail() {
        driverTools.switchToWebView();
        String promoAmount = driverTools.getText(PROMO_AMOUNT_DETAIL).orElseThrow();
        driverTools.switchToNative();
        return promoAmount.replace("-", "");
    }

    public void changeRefundMethodToBankTransfer() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.click(CHANGE_REFUND_METHOD);
        driverTools.click(BANK_TRANSFER_METHOD);
        driverTools.click(CONTINUE_SELECTED_REFUND_METHOD);
        driverTools.switchToNative();
    }

    public boolean checkRefundAmmountWithBankTransfer() {
        driverTools.switchToWebView();
        driverTools.click(PRICE_DETAIL_DROPDOWN_ARROW);
        String bankFee = driverTools.getText(getPriceDetailLocator("Bank")).orElseThrow();
        boolean isBankFeeCorrect = (bankFee.equals("-IDR 5.000") || bankFee.equals("-IDR 5,000"));
        driverTools.switchToNative();
        return isBankFeeCorrect;
    }

    public String selectedRefundMethod() {
        driverTools.switchToWebView();
        driverTools.waitForElementToAppear(SELECTED_REFUND_METHOD);
        String refundMethod = driverTools.getText(SELECTED_REFUND_METHOD).orElseThrow();
        logger.log("Selected refund method: " + refundMethod);
        driverTools.switchToNative();
        return refundMethod;
    }

    public String getCompleteHotelRefundDate() {
        driverTools.switchToWebView();
        String estimatedDateProgressText = driverTools.getText(HOTEL_REFUND_ESTIMATED_DATE_PROGRESS).orElseThrow();
        String[] estimatedDateProgress = estimatedDateProgressText.split("[,:]");
        logger.log("Estimated Date Progress Section: " + estimatedDateProgress[1]);
        driverTools.switchToNative();
        return estimatedDateProgress[1];
    }

    public String getRefundPackageName() {
        driverTools.switchToWebViewUrl("refund", 5);
        String result = driverTools.getText(PACKAGE_NAME).orElseThrow();
        driverTools.switchToNative();

        return result;
    }

    public String getBookedDate() {
        driverTools.switchToWebViewUrl("refund");
        String result = driverTools.getText(BOOK_DATE).orElseThrow();
        driverTools.switchToNative();

        return result;
    }

    public interface CheckRefundAbleStatus {
        boolean check(MapVerticalWithOrderId.TitleAndOrderId orderData);
    }

    public interface CheckRefundAbleStatus2 {
        boolean check(String orderId, String orderDetailId);
    }

    public CheckRefundAbleStatus2 checkRefundAbleStatus2(String username, String accountId) {
        return (orderId, orderDetailId) -> {
            GETRefundAbleStatus.Inputs input = new GETRefundAbleStatus.Inputs(username, orderId, orderDetailId, accountId);
            GETRefundAbleStatus getRefundAbleStatus = new GETRefundAbleStatus(input);
            ApiResponse response;
            try {
                response = getRefundAbleStatus.hitApi(apiTools);
                JSONObject obj = new JSONObject(response.getBody());
                return obj.getJSONObject("data").getJSONObject("refundable").getBoolean("value");
            } catch (JSONException e) {
                logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            }
            return false;
        };
    }

    public CheckRefundAbleStatus checkRefundAbleStatus(String username, String accountId) {
        return (orderData) -> {
            GETRefundAbleStatus.Inputs input = new GETRefundAbleStatus.Inputs(username, orderData.orderId, orderData.orderDetailId, accountId);
            GETRefundAbleStatus getRefundAbleStatus = new GETRefundAbleStatus(input);
            ApiResponse response;
            try {
                response = getRefundAbleStatus.hitApi(apiTools);
                JSONObject obj = new JSONObject(response.getBody());
                return obj.getJSONObject("data").getJSONObject("refundable").getBoolean("value");
            } catch (JSONException e) {
                logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            }
            return false;
        };
    }

    public MapVerticalWithOrderId getRefundAbleOrder(MapVerticalWithOrderId orderData, String username, String accountId, int limit) {
        CheckRefundAbleStatus filterFunction = checkRefundAbleStatus(username, accountId);
        //filter Car
        orderData.carRental = orderData.carRental.stream().filter(d -> filterFunction.check(d)).limit(limit).toList();
        //filter Todo
        orderData.ttd = orderData.ttd.stream().filter(d -> filterFunction.check(d)).limit(limit).toList();
        //filter Hotel
        orderData.hotel = orderData.hotel.stream().filter(d -> filterFunction.check(d)).limit(limit).toList();
        //filter Homes
        orderData.homes = orderData.homes.stream().filter(d -> filterFunction.check(d)).limit(limit).toList();
        logger.log("DEBUG: Hotel Length after filter" + orderData.hotel.size());
        logger.log("DEBUG: TTD Length after filter" + orderData.ttd.size());
        logger.log("DEBUG: CARR Length after filter" + orderData.ttd.size());

        if (orderData.airportTransfer.size() == 0 && orderData.hotel.size() == 0 && orderData.ttd.size() == 0 && orderData.carRental.size() == 0 && orderData.homes.size() == 0) {
            orderData.isEmpty = true;
        }
        return orderData;
    }


    public MapVerticalWithOrderId getRefundAbleOrderByVertical(String verticalName, MapVerticalWithOrderId orderData, String username, String accountId) {
        logger.log("DEBUG: Length before filter" + orderData.ttd.size());
        CheckRefundAbleStatus filterFunction = checkRefundAbleStatus(username, accountId);
        switch (verticalName.toLowerCase()) {
            case "car":
                orderData.carRental = orderData.carRental.stream().filter(d -> filterFunction.check(d)).toList();
                break;
            case "ttd":
                orderData.ttd = orderData.ttd.stream().filter(d -> filterFunction.check(d)).toList();
                break;
            case "hotel":
                orderData.hotel = orderData.hotel.stream().filter(d -> filterFunction.check(d)).toList();
                break;
            case "homes":
                orderData.homes = orderData.homes.stream().filter(d -> filterFunction.check(d)).toList();
                break;
            case "airportransfer":
                orderData.airportTransfer = orderData.airportTransfer.stream().filter(d -> filterFunction.check(d)).toList();
                break;
            default:
                logger.log("Invalid value for filtering refundable order");
                break;
        }
        logger.log("DEBUG: Length after filter" + orderData.ttd.size());
        if (orderData.airportTransfer.size() == 0 && orderData.hotel.size() == 0 && orderData.ttd.size() == 0 && orderData.carRental.size() == 0) {
            orderData.isEmpty = true;
        }
        return orderData;
    }

    public void clickGoToOrderListBtnOnRefundPage() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.click(GO_TO_ORDER_LIST_PAGE);
        driverTools.switchToNative();
    }

    public void waitForRefundPopUp() {
        //wait is just so we can be sure request is reflected in the corporate dashboard
        driverTools.waitForElementToAppear(GO_TO_ORDER_LIST_PAGE, 5);
    }

    public List<String> getEstimationDatePrices() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("refund");
        List<String> priceItems = new ArrayList<>(driverTools.getTexts(REFUND_ESTIMATIONS_DATES));
        Assertion.assertThat("Prices exist", priceItems, is(not(empty())));
        priceItems.replaceAll(price -> CoreFunctions.normalizePrice(price));
        logger.log("Prices: " + priceItems);
        driverTools.switchToNative();
        return priceItems;
    }

    public boolean checkRefundFee() {
        driverTools.switchToWebViewUrl("my-refund");
        boolean isRefundFee = false;
        if (driverTools.waitForElementToAppear(REFUND_FEE).isPresent()) {
            String refundFee = driverTools.getText(REFUND_FEE).orElse("").trim();
            isRefundFee = convertPriceSafely(refundFee) != 0;
            logger.log("Refund Fee text: " + refundFee + ", is non-zero: " + isRefundFee);
        } else {
            logger.log("Is Refund Fee Present: false");
        }
        driverTools.switchToNative();
        return isRefundFee;
    }

    public String getTotalRefund() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.waitForElementToPresence(REFUND_AMOUNT);
        driverTools.scrollIntoWebView(REFUND_AMOUNT);
        String refund = CoreFunctions.normalizePrice(driverTools.getText(REFUND_AMOUNT).orElseThrow());
        logger.log("Refund total: " + refund);
        driverTools.switchToNative();
        return refund;
    }
    public String getFinalRefund() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.waitForElementToPresence(TOTAL_REFUND_AMOUNT);
        driverTools.scrollIntoWebView(TOTAL_REFUND_AMOUNT);
        String refund = driverTools.getText(TOTAL_REFUND_AMOUNT).orElseThrow();
        refund = refund.replaceAll("[^0-9,\\.]", "").trim();
        logger.log("Refund total: " + refund);
        driverTools.switchToNative();
        return refund;
    }


    public List<String> getRefundDetailPrices() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("my-refund");
        List<String> priceItems = new ArrayList<>(driverTools.getTexts(PRICE_BREAKDOWN));
        String totalEstimatedRefund = driverTools.getText(TOTAL_ESTIMATED_REFUND_DETAILS).orElseThrow();
        totalEstimatedRefund = totalEstimatedRefund.replaceAll("[^A-Za-z0-9,\\.\\s]", "").replaceAll("\\s+", " ").trim();
        priceItems.add(totalEstimatedRefund);
        Assertion.assertThat("Prices exist", priceItems, is(not(empty())));
        logger.log("Prices: " + priceItems);
        driverTools.switchToNative();
        return priceItems;
    }

    public String getTotalEstimatedRefundAmountProcessPage() {
        driverTools.switchToWebViewUrl("my-refund");
        String refundAmount = driverTools.getText(REFUND_AMOUNT_PROCESS_PAGE).orElseThrow();
        driverTools.switchToNative();
        return refundAmount;
    }


    public List<String> getRefundMethodNames() {
        driverTools.switchToWebViewUrl("my-refund");
        List<WebElement> refundMethodElements = driverTools.elements(REFUND_METHOD_NAME).orElseThrow();

        Set<String> refundMethodNames = refundMethodElements.stream()
                .map(element -> {
                    String refundMethod = element.getText().replace("+\n", "").trim();
                    if ((refundMethod.equalsIgnoreCase("new") || refundMethod.equalsIgnoreCase("baru") || refundMethod.isEmpty()) && driverTools.element(BLIBLIPAY_ICON).isPresent()) {
                        return "blipay";
                    } else if (refundMethod.equalsIgnoreCase("Credit/Debit Card")) {
                        return "Credit card";
                    } else {
                        return refundMethod;
                    }
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));

        StringBuilder logMessage = new StringBuilder("Refund Method Names: ");
        for (String name : refundMethodNames) {
            logMessage.append(name).append(", ");
        }
        logger.log(logMessage.toString());

        driverTools.switchToNative();
        return new ArrayList<>(refundMethodNames);
    }


    public boolean isRegisteredPhoneExist(String bliPayPhone) {
        driverTools.switchToWebViewUrl("my-refund");
        String isRegisteredPhoneExist = driverTools.getText(REFUND_REGISTERED_PHONE).orElseThrow();
        driverTools.switchToNative();
        return isRegisteredPhoneExist.equals(bliPayPhone);
    }

    public void changeRefundMethodToBliPay() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.click(CHANGE_REFUND_METHOD);
        driverTools.click(BLIPAY_METHOD);
        driverTools.click(CONTINUE_SELECTED_REFUND_METHOD);
        driverTools.switchToNative();
    }

    public boolean isInRefundPage() {
        driverTools.switchToWebViewUrl("refund");
        boolean isInRefundPage = driverTools.waitForElementToAppear(SELECT_REASON_SECTION).isPresent();
        logger.log("Is refund estimation showing: " + isInRefundPage);
        driverTools.switchToNative();
        return isInRefundPage;
    }

    public void confirmBack() {
        driverTools.switchToWebViewUrl("refund");
        driverTools.click(CANCEL_REFUND_BUTTON);
        driverTools.switchToNative();
    }

    private String getAdminAccessTokenDashboardAuth() throws ExecutionException, InterruptedException {
        POSTGetAdminAccessTokenDashboardApi postGetAdminAccessTokenDashboardApi = new POSTGetAdminAccessTokenDashboardApi();
        ApiResponse response = postGetAdminAccessTokenDashboardApi.hitApi(apiTools);
        try {
            String isSuccessAuth = new JSONArray(response.getBody()).getJSONObject(0).getJSONObject("data").getJSONObject("loginUser").get("message").toString();
            logger.log("res : " + isSuccessAuth);
            if ("SUCCESS".equals(isSuccessAuth)) {
                Map<String, ArrayList<Object>> header = response.getHeaderParams();
                ArrayList<Object> listSetCookies = header.get("set-cookie");
                String accessToken = listSetCookies.get(0).toString().contains("session_access_token") ? listSetCookies.get(0).toString() : listSetCookies.get(1).toString();
                logger.log("token : " + accessToken);
                int start = accessToken.indexOf('=') + 1;
                int end = accessToken.indexOf(';');
                accessToken = accessToken.substring(start, end);
                logger.log("session_access_token= " + accessToken);
                return accessToken;
            }
            return null;
        } catch (Exception e) {
            logger.warn("failed getting admin access token, when trying to delete account");
            return null;
        }

    }

    @SneakyThrows
    public void approveEndToEndRefundFromAdminCareDashboard(String refundRequestId, String orderId) {
        String accessToken = "Bearer " + getAdminAccessTokenDashboardAuth();
        log(accessToken);

        GetRefundStatusApi.Inputs inputs = new GetRefundStatusApi.Inputs(accessToken, refundRequestId);
        GetRefundStatusApi getRefundStatusApi = new GetRefundStatusApi(inputs);
        ApiResponse response = getRefundStatusApi.hitApi(apiTools);
        log(response.getBody());

        RefundStatusApiResponse refundStatusApiResponse = RefundStatusApiResponse.parse(response.getBody());
        String lastStatus = refundStatusApiResponse.data.refunds.get(0).lastStatus;
        Assertion.assertThat("Refund status is correct", lastStatus, is(equalTo("on_process_cs")));

        int paymentMethodId = refundStatusApiResponse.data.refunds.get(0).groupedPaymentMethods.get(0).id;

        log("Payment id" + paymentMethodId);

        PostRefundForceSubmitApi.Inputs inputs1 = new PostRefundForceSubmitApi.Inputs(accessToken, lastStatus, orderId, refundRequestId, paymentMethodId);
        PostRefundForceSubmitApi postRefundForceSubmitApi = new PostRefundForceSubmitApi(inputs1);
        ApiResponse response1 = postRefundForceSubmitApi.hitApi(apiTools);
        log(response1.getBody());

        Assertion.assertThat("Refund status is correct", response1.getBody(), is(notNullValue()));
    }

    public void clickCheckEstimationsForDifferentDates() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.swipeToElement(ESTIMATIONS_FOR_DIFFERENT_DATES, Swipe.UP, 4);
        driverTools.click(ESTIMATIONS_FOR_DIFFERENT_DATES);
        driverTools.switchToNative();
    }

    public boolean estimationPageCurrencyIsAsExpected(String currency) {
        driverTools.switchToWebViewUrl("my-refund");
        List<WebElement> prices = driverTools.elements(ESTIMATION_PAGE_PRICE).orElseThrow();
        int size = prices.size();
        for (WebElement i : prices) {
            String price = driverTools.getText(i);
            logger.log(price);
            Assertion.assertThat("Price contains " + currency, price.contains(currency), is(true));
            size--;
        }
        driverTools.click(CROSS_BTN_REFUND_PAGE);
        driverTools.switchToNative();
        if (size == 0) {
            return true;
        }
        return false;
    }

    public boolean checkTotalEstimationPriceCurrency(String currency) {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.swipeToElement(TOTAL_PRICE_ESTIMATION_PAGE, Swipe.UP, 4);
        String price = driverTools.getText(TOTAL_PRICE_ESTIMATION_PAGE).orElseThrow();
        logger.log(price);
        Assertion.assertThat("Price contains " + currency, price.contains(currency), is(true));
        driverTools.switchToNative();
        return price.contains(currency);
    }

    public boolean verifyFlightOrder(FlightTicketDetail ticketDetail) {
        driverTools.switchToWebViewUrl("my-refund");
        String airlineRefund = driverTools.getText(AIRLINE_NAME).orElseThrow();
        Assertion.assertThat("Airline name is correct", airlineRefund, containsStringIgnoringCase(ticketDetail.flightAirline));

        String departureAirportRefund = driverTools.getText(DEPARTURE_AIRPORT).orElseThrow();
        Assertion.assertThat("Departure airport is correct", departureAirportRefund, containsStringIgnoringCase(ticketDetail.flightDepartAirport));

        String arrivalAirportRefund = driverTools.getText(ARRIVAL_AIRPORT).orElseThrow();
        Assertion.assertThat("Departure airport is correct", arrivalAirportRefund, containsStringIgnoringCase(ticketDetail.flightArrivalAirport));
        driverTools.switchToNative();
        return true;
    }

    public void selectPassenger(int passengerCount) {
        driverTools.switchToWebViewUrl("my-refund");
        List<WebElement> checkBoxes = driverTools.elements(CHECKBOX_PASSENGER).orElseThrow();
        int numberOfCheckBoxes = checkBoxes.size();
        for (int index = 0; index < numberOfCheckBoxes; index++) {
            checkBoxes = driverTools.elements(CHECKBOX_PASSENGER).orElseThrow();
            driverTools.clickWithJavascriptExecutor(checkBoxes.get(index));
        }
        for (int index = 0; index < passengerCount; index++) {
            checkBoxes = driverTools.elements(CHECKBOX_PASSENGER).orElseThrow();
            driverTools.clickWithJavascriptExecutor(checkBoxes.get(index));
        }
        driverTools.switchToNative();
    }

    public void chooseReasonPerPax(int pax) {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.waitForElementToPresence(SPECIFIC_REASON_DROPDOWN, 10).orElseThrow();
        for (int i = 0; i < pax; i++) {
            List<WebElement> dropdowns = driverTools.elements(SPECIFIC_REASON_DROPDOWN).orElseThrow();
            logger.log("Dropdowns size: " + dropdowns.size());
            driverTools.click(dropdowns.get(i));
            chooseRefundReasonByIndex(1);
        }
        driverTools.switchToNative();
    }

    public boolean verifyPriceBreakDown() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.waitForElementToPresence(PRICE_POSITIVE, 10).orElseThrow();
        List<WebElement> positivePrices = driverTools.elements(PRICE_POSITIVE).orElseThrow();
        double totalPositive = 0;
        for (WebElement i : positivePrices) {
            String price = driverTools.getText(i);
            totalPositive += CoreFunctions.convertPriceSafely(price);
        }
        logger.log("Total positive price: " + totalPositive);

        double totalNegative = 0;
        List<WebElement> negativePrices = driverTools.elements(PRICE_NEGATIVE).orElseThrow();
        for (WebElement i : negativePrices) {
            String price = driverTools.getText(i);
            totalPositive += CoreFunctions.convertPriceSafely(price);
        }
        logger.log("Total negative price: " + totalNegative);

        double total = totalPositive + totalNegative;
        logger.log("Total price from breakdown calculation: " + total);

        String totalPriceString = driverTools.getText(TOTAL_ESTIMATED_REFUND_DETAILS).orElseThrow();
        double totalPrice = CoreFunctions.convertPriceSafely(totalPriceString);
        logger.log("Total price: " + totalPrice);
        driverTools.switchToNative();
        return total == totalPrice;
    }

    public void clickCheckBoxTnC() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.clickWithJavascriptExecutor(CHECKBOX_TNC);
        driverTools.switchToNative();
    }

    public void clickSubmitRefund() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.click(SUBMIT_REFUND);
        driverTools.click(CONFIRM_REFUND);
        driverTools.switchToNative();
    }

    public void clickBackButtonDetail() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.click(BACK_BUTTON_DETAIL);
        driverTools.switchToNative();
    }

    public boolean isHeaderRefundDisplayed() {
        driverTools.switchToWebViewUrl("my-refund");
        boolean refundHeader = driverTools.isElementDisplayed(REFUND_HEADER_TITLE);
        logger.log("refund header : " + refundHeader);
        driverTools.switchToNative();
        return refundHeader;
    }

    public boolean backBtnDisplayedOnHeader() {
        driverTools.switchToWebViewUrl("my-refund");
        boolean refundHeaderBackBtn = driverTools.isElementDisplayed(BACK_BUTTON_DETAIL);
        logger.log("refund header : " + refundHeaderBackBtn);
        driverTools.switchToNative();
        return refundHeaderBackBtn;
    }

    public boolean checkBackBtnFunctionality() {
        driverTools.switchToWebViewUrl("my-refund");
        String urlOnRefundPage = driverTools.getCurrentURL();
        Assertion.assertThat("should contains refund text", urlOnRefundPage.toLowerCase(), is(containsString("refund")));

        boolean isRecheckBtnDisplayed = driverTools.isElementDisplayed(RE_CHECK_BTN_CONFIRMATION_POP_UP);
        Assertion.assertThat("isRecheckBtnDisplayed", isRecheckBtnDisplayed, is(true));

        driverTools.click(RE_CHECK_BTN_CONFIRMATION_POP_UP);
        driverTools.switchToNative();

        Assertion.assertThat("is refund titled header displayed on refund page", isHeaderRefundDisplayed(), Is.is(true));
        Assertion.assertThat("is back btn displayed on header", backBtnDisplayedOnHeader(), Is.is(true));

        clickBackButtonDetail();

        driverTools.switchToWebViewUrl("my-refund");
        boolean isCancelRefundPopupTitleDisplayed = driverTools.isElementDisplayed(CANCEL_REFUND_REQUEST_TITLE_POP_UP);
        Assertion.assertThat("isCancelRefundPopupTitleDisplayed", isCancelRefundPopupTitleDisplayed, is(true));

        driverTools.click(CANCEL_BTN_CONFIRMATION_POP_UP);
        driverTools.switchToNative();
        OrderDetailPage<T> orderDetailPage = OrderDetailPage.getInstance(session);
        orderDetailPage.clickRefundButton();

        driverTools.switchToWebViewUrl("my-refund");
        Assertion.assertThat("is refund titled header displayed on refund page", isHeaderRefundDisplayed(), Is.is(true));
        driverTools.switchToNative();
        return true;
    }

    public boolean verifySelectReasonFunctionality() {
        driverTools.switchToWebViewUrl("my-refund");
        WebElement selectReasonSection = driverTools.waitForElementToBeClickable(SELECT_REASON_SECTION, 10).orElseThrow();
        driverTools.click(selectReasonSection);

        boolean isCloseBtnDisplayed = driverTools.isElementDisplayed(CROSS_BTN_REFUND_PAGE);
        Assertion.assertThat("isCloseBtnDisplayed", isCloseBtnDisplayed, is(true));

        driverTools.click(CROSS_BTN_REFUND_PAGE);

        boolean isSelectReasonDisplayed = driverTools.isElementDisplayed(SELECT_REASON_SECTION);
        Assertion.assertThat("isSelectReasonDisplayed", isSelectReasonDisplayed, is(true));

        driverTools.switchToNative();

        return isCloseBtnDisplayed && isSelectReasonDisplayed;
    }

    public String getSelectedReason() {
        driverTools.switchToWebViewUrl("my-refund");
        WebElement selectedReasonRefundPageElement = driverTools.waitForElementToAppear(SELECTED_REASON_REFUND_PAGE_1).orElseThrow();
        String selectedReasonRefundPage = driverTools.getText(selectedReasonRefundPageElement);
        logger.log("selectedReasonRefundPage: " + selectedReasonRefundPage);
        driverTools.switchToNative();
        return selectedReasonRefundPage;
    }

    public boolean verifyCalculationForEstimation(String currency) {
        driverTools.switchToWebViewUrl("my-refund");
        waitUntilEstimationRegularPriceLoaded(currency);
        List<WebElement> regularPrice = driverTools.elements(estimationRegularPrice(currency)).orElseThrow();
        double sumCalculated = 0;
        for (WebElement element : regularPrice) {
            String price = driverTools.getText(element);
            if (price.contains(currency)) {
                sumCalculated += CoreFunctions.convertPriceSafely(price);
            }
        }
        WebElement totalPriceElement = driverTools.waitForElementToAppear(estimationTotalPrice(currency)).orElseThrow();
        String totalPrice = driverTools.getText(totalPriceElement);
        double totalPriceActual = CoreFunctions.convertPriceSafely(totalPrice);
        logger.log("sumCalculated: " + sumCalculated);
        logger.log("totalPriceActual: " + totalPriceActual);
        driverTools.switchToNative();
        return sumCalculated == totalPriceActual;
    }

    /**
     * Waits until estimation price texts are loaded (not placeholders like "IDR -" / "USD -").
     */
    @SneakyThrows
    private void waitUntilEstimationRegularPriceLoaded(String currency) {
        Locator locator = estimationRegularPrice(currency);
        driverTools.waitForElementToAppear(locator).orElseThrow();
        String placeholder = currency + " -";
        for (int attempt = 1; attempt <= 15; attempt++) {
            List<WebElement> prices = driverTools.elements(locator).orElse(Collections.emptyList());
            boolean stillPlaceholder = prices.stream()
                    .map(e -> driverTools.getText(e).trim())
                    .filter(text -> text.contains(currency))
                    .anyMatch(text -> text.equals(placeholder) || text.matches(currency + "\\s*-\\s*"));
            boolean hasNumericPrice = prices.stream()
                    .map(e -> driverTools.getText(e).trim())
                    .anyMatch(text -> text.contains(currency) && text.matches(".*\\d.*"));
            if (!stillPlaceholder && hasNumericPrice) {
                logger.log("Estimation regular price loaded after attempt " + attempt);
                return;
            }
            logger.log("Estimation regular price still placeholder (e.g. '" + placeholder + "'), attempt " + attempt);
            sleep(1000);
        }
        throw new AssertionError("Estimation regular price still showing placeholder like '" + placeholder + "' after wait");
    }

    private Locator estimationRegularPrice(String currency) {
        return new Locator("ESTIMATION_REGULAR_PRICE", AppiumBy.xpath("//*[contains(@class,'styles_estimation')]//*[contains(@class,'Text') and contains(text(),'" + currency + "')]"));
    }

    private Locator estimationTotalPrice(String currency) {
        return new Locator("ESTIMATION_TOTAL_PRICE", AppiumBy.xpath("//*[contains(@class,'styles_total')]//*[contains(@class,'Text') and contains(text(),'" + currency + "')]"));
    }

    public void openEstimationDetailPopup() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.click(ESTIMATION_REFUND_DETAIL_CHEVRON);
        driverTools.switchToNative();
    }

    public boolean verifyCalculationForEstimationPopup(String currency) {
        driverTools.switchToWebViewUrl("my-refund");
        List<WebElement> regularPrice = driverTools.elements(estimationRegularPricePopup(currency)).orElseThrow();
        double sumCalculated = 0;
        for (WebElement element : regularPrice) {
            String price = driverTools.getText(element);
            if (price.contains(currency)) {
                sumCalculated += CoreFunctions.convertPriceSafely(price);
            }
        }
        WebElement totalPriceElement = driverTools.waitForElementToAppear(estimationTotalPricePopup(currency)).orElseThrow();
        String totalPrice = driverTools.getText(totalPriceElement);
        double totalPriceActual = CoreFunctions.convertPriceSafely(totalPrice);
        logger.log("sumCalculated: " + sumCalculated);
        logger.log("totalPriceActual: " + totalPriceActual);
        driverTools.switchToNative();
        return sumCalculated == totalPriceActual;
    }

    private Locator estimationRegularPricePopup(String currency) {
        return new Locator("ESTIMATION_REGULAR_PRICE", AppiumBy.xpath("//*[contains(@class,'styles_amount_details_item')]//*[contains(@class,'Text') and contains(text(),'" + currency + "')]"));
    }

    private Locator estimationTotalPricePopup(String currency) {
        return new Locator("ESTIMATION_TOTAL_PRICE", AppiumBy.xpath("//*[contains(@class,'styles_total_refund')]//*[contains(@class,'Text') and contains(text(),'" + currency + "')]"));
    }

    public boolean closeEstimationDetailPopup() {
        driverTools.switchToWebViewUrl("my-refund");
        boolean isCloseBtnDisplayed1 = driverTools.isElementDisplayed(CROSS_BTN_REFUND_PAGE);
        logger.log("close btn display: " + isCloseBtnDisplayed1);
        driverTools.click(CROSS_BTN_REFUND_PAGE);

        logger.log("verify to close estimation detail popup");
        boolean isCloseBtnDisplayed2 = driverTools.isElementDisplayed(CROSS_BTN_REFUND_PAGE);
        logger.log("close btn display: " + isCloseBtnDisplayed2);

        Assertion.assertThat("clicking the close btn closes the stimation detail popup", isCloseBtnDisplayed2, is(false));
        driverTools.switchToNative();
        return true;
    }

    public String getTodayRefundAmount(String currency) {
        driverTools.switchToWebViewUrl("my-refund");
        WebElement todayRefundAmountElement = driverTools.waitForElementToAppear(todayRefundAmountLocator(currency)).orElseThrow();
        String todayRefundAmount = driverTools.getText(todayRefundAmountElement);
        logger.log("todayRefundAmount: " + todayRefundAmount);
        driverTools.switchToNative();
        return todayRefundAmount;
    }

    private Locator todayRefundAmountLocator(String currency) {
        return new Locator("TODAY_REFUND_AMOUNT", AppiumBy.xpath("//*[contains(@class,'styles_refund_receive')]//following-sibling::*[contains(text(),'" + currency + "')]"));
    }

    public boolean verifyEstimationOnOtherDateFunctionality(String todayRefundAmount) {
        driverTools.switchToWebViewUrl("my-refund");
        Assertion.assertThat("estimation on other date title displayed", driverTools.isElementDisplayed(ESTIMATION_ON_OTHER_DATE_TITLE), is(true));
        Assertion.assertThat("estimation on other date price of today refund amount is displayed", driverTools.isElementDisplayed(todayRefundAmountOnOtherDateEstimationPopupLocator(todayRefundAmount)), is(true));
        driverTools.switchToNative();
        return true;
    }

    private Locator todayRefundAmountOnOtherDateEstimationPopupLocator(String todayRefundAmount) {
        return new Locator("TODAY_REFUND_AMOUNT_OTHER_DATE_ESTIMATION", AppiumBy.xpath("//*[contains(@class,'Label_label') and (contains(text(),'estimation') or contains(text(),'Estimasi hari ini'))]//following-sibling::*[contains(text(),'" + todayRefundAmount + "')]"));
    }

    public boolean openAndVerifyHelpCenterGuide() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.swipeToElement(HELP_CENTER_GUIDE, Swipe.UP, 3);
        driverTools.click(HELP_CENTER_GUIDE);
        driverTools.switchToNative();

        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("help-center");
        Assertion.assertThat("is help center title displayed", driverTools.isElementDisplayed(HELP_CENTER_GUIDE_ON_GUIDE_PAGE_TITLE), is(true));

        driverTools.click(HELP_CENTER_GUIDE_ON_GUIDE_PAGE_BACK_BTN);
        driverTools.switchToNative();

        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("my-refund");
        Assertion.assertThat("is on refund page", driverTools.isElementDisplayed(HELP_CENTER_GUIDE), is(true));
        driverTools.switchToNative();
        return true;
    }

    public boolean openAndVerifyImportantInfoFunctionality() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.swipeToElement(IMPORTANT_INFO_BTN, Swipe.UP, 3);
        driverTools.click(IMPORTANT_INFO_BTN);

        Assertion.assertThat("is important info title displayed on popup", driverTools.isElementDisplayed(IMPORTANT_INFO_TITLE_POP_UP), is(true));
        driverTools.switchToNative();
        closeEstimationDetailPopup();
        return true;
    }

    public boolean isOnRefundPage2() {
        driverTools.switchToWebViewUrl("my-refund");
        boolean isTnCCheckBoxDisplayed = driverTools.waitForElementToPresence(CHECKBOX_TNC).isPresent();
        logger.log("isTnCCheckBoxDisplayed: " + isTnCCheckBoxDisplayed);
        driverTools.switchToNative();
        return isTnCCheckBoxDisplayed;
    }

    public boolean goBackToRefundPage1AndVerifyData(String refundReason) {
        clickBackButtonDetail();
        driverTools.switchToWebViewUrl("my-refund");
        Assertion.assertThat("tnc will disappear", driverTools.isElementDisplayed(CHECKBOX_TNC), is(false));
        Assertion.assertThat("important info will appear", driverTools.isElementDisplayed(IMPORTANT_INFO_BTN), is(true));
        Assertion.assertThat("refund reason should be selected", driverTools.isElementDisplayed(refundReasonSelectedLocator(refundReason)), is(true));
        driverTools.switchToNative();
        return true;
    }

    private Locator refundReasonSelectedLocator(String refundReasonSelected) {
        return new Locator("REFUND_REASON_SELECTED", AppiumBy.xpath("//*[contains(@class,'styles_reason')]//*[contains(@class,'Text_text') and contains(text(),'" + refundReasonSelected + "')]"));
    }

    public boolean openTermAndConditionsAndVerifyFunctionality() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.swipeToElement(TERMS_AND_CONDITIONS, Swipe.UP, 4);
        driverTools.click(TERMS_AND_CONDITIONS);
        Assertion.assertThat("is Terms and conditions title displayed", driverTools.waitForElementToPresence(TERMS_AND_CONDITIONS_TITLE_POP_UP, 10).isPresent(), is(true));
        driverTools.click(CROSS_BTN_REFUND_PAGE);
        Assertion.assertThat("is Terms and conditions title disappeared", driverTools.isElementDisplayed(TERMS_AND_CONDITIONS_TITLE_POP_UP), is(false));
        driverTools.switchToNative();
        return true;
    }

    public boolean verifyCheckboxFunctionality() {
        driverTools.switchToWebViewUrl("my-refund");
        Assertion.assertThat("is submit to refund request disabled", driverTools.isElementDisplayed(SUBMIT_TO_REFUND_DISABLED_BTN), is(true));
        driverTools.clickWithJavascriptExecutor(CHECKBOX_TNC);
        Assertion.assertThat("is submit to refund request enable", driverTools.isElementDisplayed(SUBMIT_TO_REFUND_DISABLED_BTN), is(false));
        driverTools.switchToNative();
        return true;
    }

    public boolean verifyReCheckBtn() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.click(SUBMIT_REFUND);
        Assertion.assertThat("is refund confirmation title displayed", driverTools.isElementDisplayed(REFUND_CONFIRMATION_TITLE_POP_UP), is(true));
        driverTools.click(RE_CHECK_BTN_REFUND_CONFIRMATION_POP_UP);
        Assertion.assertThat("is refund confirmation title disappeared", driverTools.isElementDisplayed(REFUND_CONFIRMATION_TITLE_POP_UP), is(false));
        driverTools.switchToNative();
        return true;
    }

    public String getCurrentMethod() {
        driverTools.switchToWebViewUrl("my-refund");
        String currentMethod = driverTools.getText(SELECTED_REFUND_METHOD_NAME).orElseThrow();
        logger.log("currentMethod: " + currentMethod);
        driverTools.switchToNative();
        return currentMethod.trim();
    }

    public void clickEditBankAccount() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.click(EDIT_BANK_ACCOUNT);
        driverTools.switchToNative();
    }

    public void addBankAccountDetails(String accountNumber, String bankName) {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.click(ADD_BUTTON_BANK_ACCOUNT);
        driverTools.sendKeys(ACCOUNT_NUMBER, accountNumber);
        driverTools.click(BANK_NAME);
        driverTools.sendKeys(INPUT_BANK_NAME, bankName);
        driverTools.click(bankTransferLocator(bankName));
        driverTools.switchToNative();
    }

    private Locator bankTransferLocator(String bankName) {
        return new Locator("BANK_TRANSFER_NAME", AppiumBy.xpath("//span[contains(text(),'" + bankName + "')]"));
    }

    public void clickCheckBankAccount() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.click(CHECK_BANK_ACCOUNT);
        driverTools.switchToNative();
    }

    public void inputOTPWhileAddingBankAccount(String otp) {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.waitForElementToAppear(OTP_INPUT_CHECK_BANK_ACCOUNT, 20).orElseThrow();
        driverTools.sendKeys(OTP_INPUT_CHECK_BANK_ACCOUNT, otp);
        driverTools.switchToNative();
    }

    public boolean isOTPWrong() {
        driverTools.switchToWebViewUrl("my-refund");
        boolean isDisplayed = driverTools.isElementDisplayed(WRONG_OTP_TEXT);
        driverTools.click(CLOSE_OTP_BOTTOMSHEET);
        driverTools.switchToNative();
        return isDisplayed;
    }

    public String accountHolderName() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.waitForElementToAppear(ACCOUNT_HOLDER_NAME);
        String name = driverTools.getValue(ACCOUNT_HOLDER_NAME);
        driverTools.switchToNative();
        return name;
    }

    public abstract boolean checkRefundReviewPage();

    public boolean verifyRescheduleNudge() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.waitForElementToAppear(RESCHEDULE_NUDGE_BACK);

        boolean rescheduleNudge = driverTools.waitForElementToPresence(RESCHEDULE_NUDGE).isPresent();
        logger.log("rescheduleNudge : " + rescheduleNudge);
        Assertion.assertThat("is rescheduleNudge popup displayed ", rescheduleNudge, is(true));

        boolean rescheduleNudgeTitle = driverTools.waitForElementToPresence(RESCHEDULE_NUDGE_TITLE).isPresent();
        logger.log("rescheduleNudgeTitle : " + rescheduleNudgeTitle);
        Assertion.assertThat("is rescheduleNudgeTitle displayed ", rescheduleNudgeTitle, is(true));

        boolean rescheduleNudgeRefundBtn = driverTools.waitForElementToPresence(RESCHEDULE_NUDGE_REFUND_BTN).isPresent();
        logger.log("rescheduleNudgeRefundBtn : " + rescheduleNudgeRefundBtn);
        Assertion.assertThat("is rescheduleNudgeRefundBtn displayed ", rescheduleNudgeRefundBtn, is(true));

        boolean rescheduleNudgeRescheduleBtn = driverTools.waitForElementToPresence(RESCHEDULE_NUDGE_RESCHEDULE_BTN).isPresent();
        logger.log("rescheduleNudgeRescheduleBtn : " + rescheduleNudgeRescheduleBtn);
        Assertion.assertThat("is rescheduleNudgeRescheduleBtn displayed ", rescheduleNudgeRescheduleBtn, is(true));

        driverTools.switchToNative();
        return rescheduleNudge && rescheduleNudgeTitle && rescheduleNudgeRefundBtn && rescheduleNudgeRescheduleBtn;
    }

    public boolean verifyBackBtn() {
        driverTools.switchToWebViewUrl("my-refund");
        boolean rescheduleNudgeBackBtn = driverTools.waitForElementToPresence(RESCHEDULE_NUDGE_BACK).isPresent();
        logger.log("rescheduleNudgeBackBtn : " + rescheduleNudgeBackBtn);
        Assertion.assertThat("is rescheduleNudgeBackBtn popup displayed ", rescheduleNudgeBackBtn, is(true));

        driverTools.clickWithJavascriptExecutor(RESCHEDULE_NUDGE_BACK);
        driverTools.switchToNative();

        //is on order detail page
        OrderDetailPage<T> orderDetailPage = OrderDetailPage.getInstance(session);
        orderDetailPage.clickAndOpenRescheduleNudgeButton();

        Assertion.assertThat("is on reschedule nudge open", verifyRescheduleNudge(), is(true));

        return rescheduleNudgeBackBtn;
    }

    public boolean verifyRefundBtn() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.clickWithJavascriptExecutor(RESCHEDULE_NUDGE_REFUND_BTN);

        Assertion.assertThat("isHeaderRefundDisplayed", isHeaderRefundDisplayed(), is(true));
        driverTools.switchToNative();

        clickBackButton();
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.clickWithJavascriptExecutor(CANCEL_BTN_CONFIRMATION_POP_UP);
        driverTools.switchToNative();

        //is on order detail page
        OrderDetailPage<T> orderDetailPage = OrderDetailPage.getInstance(session);
        orderDetailPage.clickAndOpenRescheduleNudgeButton();

        Assertion.assertThat("is on reschedule nudge open", verifyRescheduleNudge(), is(true));
        return true;
    }

    public void clickRescheduleBtnOnRescheduleNudge() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.clickWithJavascriptExecutor(RESCHEDULE_NUDGE_RESCHEDULE_BTN);
        driverTools.switchToNative();
    }

    public String getRefundDialogText() {
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.waitForElementToPresence(REFUND_DIALOG_POPUP, 10);
        String refundDialogText = driverTools.getText(REFUND_DIALOG_POPUP).orElseThrow();
        logger.log("refundDialogText : " + refundDialogText);
        driverTools.switchToNative();
        return refundDialogText;
    }

    public void completeRefundSubmissionAfterEstimationVerification() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("refund", 10);
        driverTools.scrollIntoWebView(REFUND_FLOW_FIRST_CONTINUE_BTN);
        driverTools.swipeUp(5);
        driverTools.clickWithJavascriptExecutor(driverTools.waitForElementToAppear(REFUND_FLOW_FIRST_CONTINUE_BTN, 20).orElseThrow(() -> new NoSuchElementException("Continue button not shown within timeout")));
        driverTools.scrollIntoWebView(CHECKBOX_TNC);
        driverTools.swipeUp(5);
        driverTools.clickWithJavascriptExecutor(driverTools.waitForElementToAppear(CHECKBOX_TNC, 15).orElseThrow(() -> new NoSuchElementException("T&C checkbox not shown within timeout")));
        driverTools.scrollIntoWebView(REFUND_SUBMIT_REQUEST_PRIMARY_BTN);
        driverTools.clickWithJavascriptExecutor(driverTools.waitForElementToBeClickable(REFUND_SUBMIT_REQUEST_PRIMARY_BTN, 20).orElseThrow(() -> new NoSuchElementException("Submit refund request button not clickable within timeout")));
        driverTools.clickWithJavascriptExecutor(driverTools.waitForElementToAppear(REFUND_PROCESS_REFUND_PRIMARY_BTN, 20).orElseThrow(() -> new NoSuchElementException("Process refund button not shown within timeout")));
        driverTools.clickWithJavascriptExecutor(driverTools.waitForElementToAppear(REFUND_VIEW_REFUND_DETAIL_BTN, 20).orElseThrow(() -> new NoSuchElementException("View refund detail button not shown within timeout")));
        driverTools.waitForElementToAppear(REFUND_COMPLETED_STEP_TITLE, 40).orElseThrow(() -> new NoSuchElementException("Refund completed step not shown within timeout"));
        driverTools.switchToNative();
    }

    public boolean isRefundCompletedStepDisplayed() {       
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("refund");
        boolean displayed = driverTools.isElementDisplayed(REFUND_COMPLETED_STEP_TITLE);
        driverTools.switchToNative();
        return displayed;
        
    }
    public abstract void getRefundDetails();

    public abstract boolean isRefundSentToTiketCheckDisplayed();

    public abstract boolean isRefundReviewedByTiketCheckDisplayed();

    public abstract boolean isRefundVerifiedByTiketCheckDisplayed();

    public void changeRefundMethodToBlibliPoints() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("my-refund");
        try {
            driverTools.click(CHANGE_REFUND_METHOD);
            driverTools.click(BLIBLI_POINTS_METHOD);
            driverTools.click(CONTINUE_SELECTED_REFUND_METHOD);
        } finally {
            driverTools.switchToNative();
        }
    }

}


