package com.tiket.web.page.platform.sphinx.refund;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.testbase.Assertion;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.google.gson.Gson;
import com.tiket.api.refund.GETRefundAbleStatus;
import com.tiket.api.refund.POSTDashboardRefundsCalculate;
import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.model.Exception.ApiChangeException;
import com.tiket.model.Exception.ApiEnvironmentException;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.pojo.refund.DashboardRefundsCalculateResponse;
import com.tiket.testbase.Configs;

import org.openqa.selenium.WebElement;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public abstract class RefundPageWeb<T extends WebDriver> extends WebPage<T> {
    private static final Locator SELECT_REASON_CARD = new Locator("SELECT_REASON_CARD", By.xpath("//*[@data-testid='card-choose-reason']"));
    private static final Locator REFUND_REASON = new Locator("SELECT_REASON_CARD", By.xpath("//div[contains(@class,'styles_reason')]//span"));
    private static final Locator REFUND_BANNER_PROGRESS = new Locator("REFUND_BANNER_PROGRESS", By.xpath("//*[contains(@class,'progress_step_list')]//li//*[@data-testid='step-circle'][contains(@class,'circle_active')]"));
    private static final Locator REFUND_BANNER_ORDERID = new Locator("REFUND_BANNER_ORDERID", By.xpath("//*[contains(text(),'Order ID')]"));
    private static final Locator ORDER_DETAIL_REFUND = new Locator("ORDER_DETAIL_REFUND", By.xpath("//*[contains(@class,'detail_wrapper')]"));
    private static final Locator BACK_POP_UP_CANCEL_REFUND = new Locator("BACK_POP_UP_CANCEL_REFUND", By.xpath("//button[contains(text(),'Batalkan Refund') or contains(text(),'Cancel Refund')]"));
    private static final Locator BACK_BUTTON_NUDGE = new Locator("BACK_BUTTON_NUDGE", By.xpath("//div[contains(@class,'styles_back_button')]"));
    private static final Locator CONTINUE_REFUND_BUTTON_NUDGE = new Locator("CONTINUE_REFUND_BUTTON_NUDGE", By.xpath(
            "//button[@data-testid='nudge-button-0']"
                    + " | //button[contains(@class,'styles_btn_continue_refund') and contains(@class,'Button_variant_secondary')]"
                    + " | //button[.//span[normalize-space()='Continue to refund' or normalize-space()='Lanjutkan ke refund']]"));
    private static final Locator RESCHEDULE_BUTTON_NUDGE = new Locator("RESCHEDULE_BUTTON_NUDGE", By.xpath("//button[contains(text(),'Reschedule now') or contains(text(),'Reschedule sekarang')]"));
    private static final Locator CONTINUE_REFUND = new Locator("CONTINUE_REFUND", By.xpath("//*[contains(@class,'Button_variant_primary')]"));
    private static final Locator SUBMIT_REFUND = new Locator("SUBMIT_REFUND", By.xpath("//*[@data-testid='continue-submit-refund']"));
    private static final Locator PROCESS_REFUND = new Locator("PROCESS_REFUND", By.xpath("//*[contains(@class,'Figure_footer')]//button[contains(@class,'primary')]"));
    private static final Locator GO_TO_ORDERS = new Locator("GO_TO_ORDERS", By.xpath("//*[contains(@class,'Figure_footer')]//button[1]"));
    private static final Locator REASON_RADIO_LIST = new Locator("REASON_RADIO_LIST", By.xpath("//*[contains(@class,'Radio_radio')]"));
    private static final Locator REASON_RADIO_WRAPPER = new Locator("REASON_RADIO_WRAPPER", By.xpath("//*[contains(@class,'radio_wrapper')]"));
    private static final Locator ESTIMATION_OTHER_DATES = new Locator("ESTIMATION_OTHER_DATES", By.xpath("//*[@data-testid='card-other-dates']"));
    private static final Locator ESTIMATION_REFUND_RECEIVE = new Locator("ESTIMATION_REFUND_RECEIVE", By.xpath("//*[contains(@class,'styles_card_estimation')]//*[contains(text(),'Refund you') or contains(text(),'Refund diterima')]//parent::*//following-sibling::span"));
    private static final Locator ESTIMATION_REFUND_FEE = new Locator("ESTIMATION_REFUND_FEE", By.xpath("//*[contains(@class,'styles_card_estimation')]//*[contains(text(),'Refund Fee') or contains(text(),'Biaya Refund')]//following-sibling::span"));
    private static final Locator ESTIMATION_REFUND_TOTAL = new Locator("ESTIMATION_REFUND_TOTAL", By.xpath("//*[@data-testid='total-amount']//span"));
    private static final Locator ESTIMATION_BREAKDOWN_ICON = new Locator("ESTIMATION_BREAKDOWN_ICON", By.xpath("//*[@data-testid='total-amount']//*[@data-testid='icon-chevron']"));
    private static final Locator CARD_SUBMISSION_GUIDE = new Locator("CARD_SUBMISSION_GUIDE", By.xpath("//*[@data-testid='card-refund-guide']"));
    private static final Locator CARD_IMPORTANT_INFO = new Locator("CARD_SUBMISSION_GUIDE", By.xpath("//*[@data-testid='card-important-info']"));
    private static final Locator DYNAMIC_REFUND_INFO_CONTENT = new Locator("DYNAMIC_REFUND_INFO_CONTENT", By.xpath("//*[@data-testid='dynamic-refund-info-content']"));
    private static final Locator REASON_WRAPPER = new Locator("REASON_WRAPPER", By.xpath("//*[contains(@class,'reason_wrapper')]"));
    private static final Locator REFUND_METHOD_OPTION = new Locator("REFUND_METHOD_OPTION", By.xpath("//*[contains(@class,'RefundMethodItem_container')]"));
    private static final Locator REFUND_METHOD_BANK_NUMBER = new Locator("REFUND_METHOD_BANK_NUMBER", By.xpath("//*[contains(@class,'RefundMethodItem_bank_account_detail')]//*[contains(@class,'Text_weight')]"));
    private static final Locator REFUND_METHOD_BANK_NAME = new Locator("REFUND_METHOD_BANK_NAME", By.xpath("//*[contains(@class,'RefundMethodItem_bank_account_detail')]//*[contains(@class,'lowEmphasis')]"));
    private static final Locator REFUND_METHOD_BANK_ACCOUNT_NAME = new Locator("REFUND_METHOD_BANK_ACCOUNT_NAME", By.xpath("//*[contains(@class,'RefundMethodItem_bank_account_detail')]//*[contains(@class,'highEmphasis')]"));
    private static final Locator CHANGE_REFUND_METHOD = new Locator("CHANGE_REFUND_METHOD", By.xpath("//*[contains(@class,'RefundMethodItem_change_button')]"));
    private static final Locator SELECT_REFUND_METHOD_OPTIONS = new Locator("SELECT_REFUND_METHOD_OPTIONS", By.xpath("//div[contains(@class,'SelectRefundMethod_options_wrapper__')]"));
    private static final Locator SELECT_REFUND_METHOD_CONTINUE = new Locator("SELECT_REFUND_METHOD_CONTINUE", By.xpath("//div[contains(@class,'SelectRefundMethod_button_wrapper')]//button"));
    private static final Locator EDIT_REFUND_METHOD = new Locator("EDIT_REFUND_METHOD", By.xpath("//*[contains(@class,'RefundMethodItem_icon_edit')]"));
    private static final Locator SAVED_BANK_ACCOUNT = new Locator("SAVED_BANK_ACCOUNT", By.xpath("//*[contains(@class,'SelectBankAccount_text')]"));
    private static final Locator ADD_BANK_ACCOUNT = new Locator("ADD_BANK_ACCOUNT", By.xpath("//*[contains(@class,'RefundMethodItem_actions')]//*[contains(text(),'Tambah Rekening Bank') or contains(text(),'Add Bank Account')]"));
    private static final Locator INPUT_BANK_NAME = new Locator("INPUT_BANK_NAME", By.xpath("//*[contains(@class,'AddBankAccount')]//*[@id='bank-name' or @id='nama-bank']"));
    private static final Locator INPUT_ACCOUNT_NUMBER = new Locator("INPUT_ACCOUNT_NUMBER", By.xpath("//*[contains(@class,'AddBankAccount')]//*[@id='account-number' or @id='nomor-rekening']"));
    private static final Locator INPUT_ACCOUNT_NAME = new Locator("INPUT_ACCOUNT_NAME", By.xpath("//*[contains(@class,'AddBankAccount')]//*[@id='account-holder-name' or @id='nama-pemilik-rekening']"));
    private static final Locator VALID_ACCOUNT_NAME_CHECKMARK = new Locator("VALID_ACCOUNT_NAME_CHECKMARK", By.xpath("//*[contains(@class,'AddBankAccount')]//*[contains(@class,'styles_clickable_icon')]"));
    private static final Locator CHECK_BANK_ACCOUNT = new Locator("CHECK_BANK_ACCOUNT", By.xpath("//*[contains(@class,'AddBankAccount')]//*[contains(text(),'Check bank account') or contains(text(),'Cek akun bank')]"));
    private static final Locator CHECK_BANK_OTP = new Locator("CHECK_BANK_OTP", By.xpath("//*[contains(@class,'AddBankAccount_otp')]//input"));
    private static final Locator ADD_BANK_SAVE_BUTTON = new Locator("ADD_BANK_SAVE_BUTTON", new By.ByXPath("//span[normalize-space()='Save' or normalize-space()='Simpan']"));
    private static final Locator CHECKBOX_SAVE_BANK = new Locator("CHECKBOX_SAVE_BANK", new By.ByXPath("//*[contains(@class,'AddBankAccount')]//*[contains(@class,'Check_icon')]"));
    private static final Locator TNC_BUTTON = new Locator("TNC_BUTTON", By.xpath("//*[@data-testid='tnc-text']"));
    private static final Locator TNC_CONTENT = new Locator("TNC_CONTENT", By.xpath("//*[@data-testid='tnc']"));
    private static final Locator TNC_CHECKLIST = new Locator("TNC_CHECKLIST", By.xpath("//*[@data-testid='checklist-tnc']/parent::*"));
    private static final Locator SUCCESS_REFUND_FIGURE_CONTENT = new Locator("SUCCESS_REFUND_FIGURE_CONTENT", By.xpath("//*[contains(@class,'Figure_content')]"));
    private static final Locator CONTENT_LOADER = new Locator("CONTENT_LOADER", By.xpath("//*[contains(@class,'ContentLoader')]"));
    private static final Locator ESTIMATION_OTHER_DATES_PROGRESS = new Locator("ESTIMATION_OTHER_DATES_PROGRESS", By.xpath("//*[contains(@class,'ProgressStepVerticalDots_step')]"));
    private static final Locator RESCHEDULE_NUDGE_WRAPPER = new Locator("RESCHEDULE_NUDGE_WRAPPER", By.xpath(
            "//*[contains(@class,'styles_reschedule_nudge_wrapper') or contains(@class,'reschedule_nudge_wrapper')]"));
    private static final Locator BREAKDOWN_REFUND_METHOD_MULTI = new Locator("BREAKDOWN_REFUND_METHOD_MULTI", By.xpath("//*[contains(@class,'refund_method_section')]//span[1]"));
    private static final Locator BREAKDOWN_REFUND_METHOD_SINGLE = new Locator("BREAKDOWN_REFUND_METHOD_SINGLE", By.xpath("//*[contains(@class,'total_refund')]//*[contains(@class,'title')]"));
    private static final Locator CONTINUE_TO_REFUND = new Locator("CONTINUE_REFUND", By.xpath(
            "//button[@data-testid='nudge-button-0']"
                    + " | //button[contains(@class,'styles_btn_continue_refund') and contains(@class,'Button_variant_secondary')]"
                    + " | //button[.//span[normalize-space()='Continue to refund' or normalize-space()='Lanjutkan ke refund']]"));
    private static final Locator REFUND_PERSONAL_REASON = new Locator("REFUND_PERSONAL_REASON", By.xpath("//*[contains(@class,'radio_wrapper')]//span[contains(text(),'Personal Reason') or contains(text(),'Alasan Pribadi')]"));
    private static final Locator CHOOSE_SPECIFIC_REASON = new Locator("CHOOSE_SPECIFIC_REASON", By.xpath("//button[contains(@class,'styles_clickable_icon')]"));
    private static final Locator SPECIFIC_REASON_CHANGE_OF_MIND = new Locator("SPECIFIC_REASON_CHANGE_OF_MIND", By.xpath("//*[contains(@class,'Radio_children')]//*[ contains(text(),'Change of Mind') or contains(text(),'Berubah Pikiran')]"));
    private static final Locator SPECIFIC_REASON_SICKNESS = new Locator("SPECIFIC_REASON_SICKNESS", By.xpath("//*[contains(@class,'Radio_children')]//*[ contains(text(),'Sickness') or contains(text(),'Sakit')]"));
    private static final Locator SELECT_ROOM_FOR_REFUND = new Locator("SELECT_ROOM_FOR_REFUND", By.xpath("//label[.//input[@name='chosenOrderDetail']]"));
    private static final Locator ROOM_FOR_REFUND = new Locator("ROOM_FOR_REFUND", By.xpath("(//label[.//input[@name='chosenOrderDetail']])[1]"));
    private static final Locator START_REFUND_BUTTON = new Locator("START_REFUND_BUTTON", By.xpath("//button[(contains(.,'Start the refund process') or contains(.,'Mulai proses refund'))]"));
    private static final Locator CONTINUE_TO_NEXT_PAGE = new Locator("CONTINUE_TO_NEXT_PAGE", By.xpath("//button[contains(.,'Lanjutkan') or contains(.,'Continue')]"));
    private static final Locator REFUND_CHARGED_AMOUNT = new Locator("REFUND_CHARGED_AMOUNT", By.xpath("//p[normalize-space()='Cancellation fee' or normalize-space()='Biaya pembatalan']/ancestor::div[contains(@class,'styles_amount_details_item')]//p[contains(@class,'Text_variant_alert')]"));
    private static final Locator REFUND_ROOM_NAME = new Locator("REFUND_ROOM_NAME", By.xpath("//div[contains(@class,'ChooseMultiOrderItem_title_and_radio')][.//span[contains(text(),'refunded') or contains(text(),'di-refund')]]//div[contains(@class,'ChooseMultiOrderItem_title')]//span[contains(@class,'Text_weight_bold')]"));

    // Specific for Flight Refund
    private static final Locator FLIGHT_DETAILS_TITLE = new Locator("FLIGHT_DETAILS_TITLE", By.xpath("//p[contains(@class, 'styles_order_detail_title')]"));
    private static final Locator AIRLINE_LOGO = new Locator("AIRLINE_LOGO", By.xpath("//img[contains(@class,'styles_airline_logo')]"));
    private static final Locator FLIGHT_NAME_AND_NUMBER = new Locator("FLIGHT_NAME_AND_NUMBER", By.xpath("//div[contains(@class, 'styles_flight__')]/span"));
    private static final Locator FLIGHT_DESTINATION_FROM = new Locator("FLIGHT_DESTINATION_FROM", By.xpath("//div[contains(@class, 'styles_flight_route')]/p[1]"));
    private static final Locator FLIGHT_DESTINATION_TO = new Locator("FLIGHT_DESTINATION_TO", By.xpath("//div[contains(@class, 'styles_flight_route')]/p[2]"));
    private static final Locator FLIGHT_SCHEDULED_DATE = new Locator("FLIGHT_SCHEDULED_DATE", By.xpath("//div[contains(@class, 'styles_flight_date')]/p[1]"));
    private static final Locator FLIGHT_SCHEDULED_TIME = new Locator("FLIGHT_SCHEDULED_TIME", By.xpath("//div[contains(@class, 'styles_flight_date')]/p[2]"));
    private static final Locator PASSENGER_CHECKBOX = new Locator("PASSENGER_CHECKBOX", By.xpath("//input[@data-testid='check-passenger']"));
    private static final Locator RELATED_ORDER_POPUP = new Locator("RELATED_ORDER_POPUP", By.xpath("//section[contains(@class,'Modal_modal_popup')][.//*[contains(text(),'Related Order') or contains(text(),'Pesanan Terkait')]]"));
    private static final Locator VIEW_REFUND_DETAIL = new Locator("VIEW_REFUND_DETAIL", By.xpath("//*[contains(@class,'Figure_footer')]//button[.//*[contains(text(),'View Refund Detail') or contains(text(),'Lihat Detail Refund')]]"));
    private static final Locator SPECIFIC_REASON_DROPDOWN = new Locator("SPECIFIC_REASON_DROPDOWN", By.xpath("//div[contains(@class, 'styles_input_inner_wrapper')]/input"));
    private static final Locator SPECIFIC_REASON_SELECTION = new Locator("SPECIFIC_REASON_SELECTION", By.xpath("//label/span/input"));
    private static final Locator CONFIRMATION_REFUND_POPUP_LOCATOR = new Locator("CONFIRMATION_REFUND_POPUP_LOCATOR", By.xpath("(//button[contains(@class,'Button_variant_primary')])[2]"));
    private static final Locator B2B_PROCESS_TO_REFUND = new Locator("B2B_PROCESS_TO_REFUND", By.xpath("//div[contains(@class,'dialog')]//button[contains(@class,'Button_variant_primary')]"));


    protected abstract Locator headerBackRefund();

    private static Locator getRefundReasonRadio(String reasonName) {
        return new Locator("REASON_RADIO_" + reasonName.toUpperCase(), By.xpath("//*[contains(@class,'Radio_radio')]//*[contains(text(),'" + reasonName + "')]"));
    }

    private static Locator getAmountBreakdown(String currency) {
        return new Locator("AMOUNT_BREAKDOWN", By.xpath("//div[contains(@class,'Breakdown') or contains(@class,'breakdown')]//p[contains(@class,'Text_text') and starts-with(text(),'" + currency + "')]"));
    }

    private static Locator getFeeBreakdown(String currency) {
        return new Locator("AMOUNT_FEE_BREAKDOWN", By.xpath("//*[contains(@class,'amount_details_item')]//*[contains(@class,'variant_alert')][contains(text(),'" + currency + "')]"));
    }

    private static Locator getTotalRefundBreakdown(String currency) {
        return new Locator("TOTAL_BREAKDOWN", By.xpath("//*[contains(@class,'total_refund') or contains(@class,'refund_method_section')]//span[contains(@class,'Text_weight_bold') and contains(text(),'"+currency+"')]"));
    }

    private static Locator getButtonByText(String text) {
        return new Locator("BUTTON_" + text.toUpperCase(), By.xpath("//button[contains(@class,'Button_variant_primary')]"));
    }

    private static Locator getPrimaryButtonByText(String textButton) {
        return new Locator("BUTTON_" + textButton.toUpperCase(), By.xpath("//button[contains(@class,'Button_variant_primary') and .//span[normalize-space()='"+ textButton+ "']]"));
    }

    private static Locator getButtonByText(String textEn, String textId) {
        return new Locator("BUTTON_" + textEn.toUpperCase(), By.xpath("//*[contains(@class,'button') or contains(@class, 'Button')][contains(text(),'" + textEn + "') or contains(text(),'" + textId + "')]"));
    }

    private static Locator changeRefundMethodOption(String method) {
        return new Locator("CHANGE_REFUND_METHOD_OPTION", By.xpath("//div[contains(@class, 'SelectRefundMethod_options_wrapper__')]/div[.//*[contains(normalize-space(.), normalize-space('" + method + "'))]]//span[./input]"));
    }

    private static Locator getBankNameOnList(String bankName) {
        return new Locator("BANK_NAME_" + bankName, By.xpath("//*[contains(@class,'AddBankAccount_bank_list')]//*[contains(text(),'" + bankName + "')]"));
    }

    private Locator estimationRegularPrice(String currency) {
        return new Locator("ESTIMATION_REGULAR_PRICE", AppiumBy.xpath("//*[contains(@class,'styles_estimation')]//*[contains(@class,'Text') and (contains(text(),'" + currency + "') and not(contains(text(),'" + currency + " -')))]"));
    }

    private Locator estimationTotalPrice(String currency) {
        return new Locator("ESTIMATION_TOTAL_PRICE", AppiumBy.xpath("//*[contains(@class,'styles_total')]//*[contains(@class,'Text') and (contains(text(),'" + currency + "') and not(contains(text(),'" + currency + " -')))]"));
    }
    public abstract Locator getPopUpBody();
    public abstract Locator getClosePopupButton();
    
    public RefundPageWeb(Session<T> session) {
        super(session);
    }
    
    public static <T extends WebDriver> RefundPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new RefundPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new RefundPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public interface CheckRefundAbleStatus {
        boolean check(MapVerticalWithOrderId.TitleAndOrderId orderData);
    }

    public CheckRefundAbleStatus checkRefundAbleStatus (String username,String accountId){
        return (orderData) ->{
            GETRefundAbleStatus.Inputs input = new GETRefundAbleStatus.Inputs(username,orderData.orderId,orderData.orderDetailId,accountId);
            GETRefundAbleStatus getRefundAbleStatus = new GETRefundAbleStatus(input);
            ApiResponse response;
            try {
                response = getRefundAbleStatus.hitApi(apiTools);
                JSONObject obj = new JSONObject(response.getBody());
                return obj.getJSONObject("data").getJSONObject("refundable").getBoolean("value");
            } catch (JSONException | ApiChangeException | ApiEnvironmentException e ) {
                logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            }
            return false;
        };
    }

    public DashboardRefundsCalculateResponse hitPOSTDashboardCalculate(String orderId, String orderDetailId, String reasonId, String amount, String forceBank, List<Integer> items) throws InterruptedException, ExecutionException, JSONException {
        POSTDashboardRefundsCalculate.Inputs inputs = new POSTDashboardRefundsCalculate.Inputs(orderId, orderDetailId, reasonId, amount, forceBank, items);
        POSTDashboardRefundsCalculate postDashboardRefundsCalculate = new POSTDashboardRefundsCalculate(inputs);
        ApiResponse response = postDashboardRefundsCalculate.hitApi(apiTools);
        JSONObject data = new JSONObject(response.getBody()).getJSONObject("data");
        Gson gson = new Gson();
        return gson.fromJson(data.toString(), DashboardRefundsCalculateResponse.class);
    }

    public String getRefundMethodKey(String orderId, String orderDetailId, String forceBank) {
        List<Integer> items = new ArrayList<Integer>();
        try {
            DashboardRefundsCalculateResponse calculateResponse = hitPOSTDashboardCalculate(orderId, orderDetailId, "customer_request", "20000", forceBank, items);
            DashboardRefundsCalculateResponse.RefundMethods firstMethod = firstRefundMethod(calculateResponse);
            if (firstMethod == null) {
                logger.log("No refund method returned for orderId=" + orderId + " (forceBank=" + forceBank + "), skipping");
                return "";
            }
            String method = firstMethod.methodKey;
            if ("tix_point".equalsIgnoreCase(method)) {
                calculateResponse = hitPOSTDashboardCalculate(orderId, orderDetailId, "customer_request", "15000", forceBank, items);
                firstMethod = firstRefundMethod(calculateResponse);
                if (firstMethod == null) {
                    logger.log("No refund method returned on tix_point retry for orderId=" + orderId + ", skipping");
                    return "";
                }
                method = firstMethod.methodKey;
            }
            if ("pay_later".equalsIgnoreCase(method)) {
                if (firstMethod.merchantDetails == null || firstMethod.merchantDetails.merchantName == null) {
                    logger.log("pay_later method missing merchantDetails for orderId=" + orderId + ", skipping");
                    return "";
                }
                method = firstMethod.merchantDetails.merchantName;
            }
            logger.log("got refund method key: "+method);
            return method;
        } catch (InterruptedException | ExecutionException | JSONException | ApiChangeException | ApiEnvironmentException e) {
            logger.log("Error occurred: "+e.getLocalizedMessage());
        }
        return "";
    }

    private DashboardRefundsCalculateResponse.RefundMethods firstRefundMethod(DashboardRefundsCalculateResponse response) {
        if (response == null) {
            return null;
        }
        if (response.refundMethods != null && !response.refundMethods.isEmpty()) {
            return response.refundMethods.get(0);
        }
        if (response.groupedRefundMethods != null && !response.groupedRefundMethods.isEmpty()) {
            DashboardRefundsCalculateResponse.GroupedRefundMethods grouped = response.groupedRefundMethods.get(0);
            DashboardRefundsCalculateResponse.RefundMethods adapted = new DashboardRefundsCalculateResponse.RefundMethods();
            adapted.methodKey = grouped.methodKey;
            adapted.merchantDetails = grouped.merchantDetails;
            return adapted;
        }
        return null;
    }

    public String getOrderIdOnRefundBanner() {
        driverTools.waitForElementToAppear(REFUND_BANNER_ORDERID);
        try {
            return driverTools.getText(REFUND_BANNER_ORDERID).orElseThrow();
        } catch (Exception e) {
            logger.log("Order ID on refund banner not found, trying to wait for it to appear");
            driverTools.waitForElementToPresence(REFUND_BANNER_ORDERID).orElseThrow();
            return driverTools.getText(REFUND_BANNER_ORDERID).orElseThrow();
        }
    }

    public void skipRescheduleNudgeIfShown() {
        Optional<WebElement> rescheduleNudge = driverTools.waitForElementToAppear(RESCHEDULE_NUDGE_WRAPPER, 4);
        if (rescheduleNudge.isPresent()) {
            driverTools.click(CONTINUE_REFUND_BUTTON_NUDGE);
            driverTools.waitForElementToAppear(ORDER_DETAIL_REFUND, 3);
        }
    }

    public void backFromRescheduleNudge() {
        driverTools.waitForElementToAppear(RESCHEDULE_NUDGE_WRAPPER, 4);
        driverTools.click(BACK_BUTTON_NUDGE);
        Optional<WebElement> popUpCancelRefund = driverTools.waitForElementToAppear(BACK_POP_UP_CANCEL_REFUND, 4);
        if (popUpCancelRefund.isPresent()){
            driverTools.clickWithJavascriptExecutor(BACK_POP_UP_CANCEL_REFUND);
        }
    }

    public void continueRefundFromRescheduleNudge() {
        driverTools.waitForElementToAppear(RESCHEDULE_NUDGE_WRAPPER, 4);
        driverTools.click(CONTINUE_REFUND_BUTTON_NUDGE);
        driverTools.waitForElementToAppear(ORDER_DETAIL_REFUND, 3);
    }

    public void clickRescheduleOnNudges() {
        driverTools.waitForElementToAppear(RESCHEDULE_NUDGE_WRAPPER, 4);
        driverTools.click(RESCHEDULE_BUTTON_NUDGE);
    }

    public int getActiveProgress() {
        WebElement progress = driverTools.waitForElementToAppear(REFUND_BANNER_PROGRESS).orElseThrow();
        return Integer.parseInt(driverTools.getText(progress));
    }

    public String getOrderDetail() {
        return driverTools.getText(ORDER_DETAIL_REFUND).orElseThrow();
    }

    public void selectRefundReason(String reasonName) {
        driverTools.scrollIntoWebView(SELECT_REASON_CARD);
        driverTools.click(SELECT_REASON_CARD);
        WebElement reasonRadio = driverTools.waitForElementToAppear(getRefundReasonRadio(reasonName)).orElseThrow();
        driverTools.click(reasonRadio);
    }

    public String selectRandomRefundReason() {
        driverTools.waitForElementsToAppear(SELECT_REASON_CARD);
        driverTools.scrollIntoWebView(SELECT_REASON_CARD);
        driverTools.clickWithJavascriptExecutor(SELECT_REASON_CARD);
        driverTools.waitForElementsToAppear(getPopUpBody());
        driverTools.scrollIntoWebView(getPopUpBody());
        driverTools.clickWithJavascriptExecutor(getPopUpBody());
        List<WebElement> reasonList = driverTools.waitForElementsToAppear(REASON_RADIO_LIST).orElseThrow();
        int num = CoreFunctions.generateRandomInt(0, reasonList.size()-1);
        driverTools.waitForElementToBeClickable(reasonList.get(num));
        driverTools.clickWithJavascriptExecutor(reasonList.get(num));
        WebElement reason = driverTools.waitForElementToAppear(REFUND_REASON).orElseThrow();
        return driverTools.getText(reason);
    }

    public void selectRefundReasonPersonalReasonFlight() {
        driverTools.waitForElementsToAppear(SELECT_REASON_CARD);
        driverTools.scrollIntoWebView(SELECT_REASON_CARD);
        driverTools.clickWithJavascriptExecutor(SELECT_REASON_CARD);
        driverTools.waitForElementsToAppear(getPopUpBody());
        try {
            driverTools.scrollIntoWebView(getPopUpBody());
            driverTools.clickWithJavascriptExecutor(getPopUpBody());
        }catch (Exception e){
            logger.log("Modal header not found");
        }
        driverTools.waitForElementToBeClickable(REFUND_PERSONAL_REASON);
        driverTools.clickWithJavascriptExecutor(REFUND_PERSONAL_REASON);
    }

    public void chooseSpecificReason() {
        driverTools.waitForElementsToAppear(CHOOSE_SPECIFIC_REASON);
        driverTools.scrollIntoWebView(CHOOSE_SPECIFIC_REASON);
        driverTools.clickWithJavascriptExecutor(CHOOSE_SPECIFIC_REASON);
        driverTools.waitForElementsToAppear(getPopUpBody());
        try {
            driverTools.scrollIntoWebView(getPopUpBody());
            driverTools.clickWithJavascriptExecutor(getPopUpBody());
        }catch (Exception e){
            logger.log("Modal header not found");
        }

        int num = CoreFunctions.generateRandomInt(0, 2);
        if(num == 1){
            driverTools.waitForElementToBeClickable(SPECIFIC_REASON_CHANGE_OF_MIND);
            driverTools.clickWithJavascriptExecutor(SPECIFIC_REASON_CHANGE_OF_MIND);
        }else {
            driverTools.waitForElementToBeClickable(SPECIFIC_REASON_SICKNESS);
            driverTools.clickWithJavascriptExecutor(SPECIFIC_REASON_SICKNESS);
        }
    }
    
    public boolean verifyRefundEstimation(String currency) {
        driverTools.waitForElementToPresence(estimationRegularPrice(currency));
        List<WebElement> regularPrice = driverTools.elements(estimationRegularPrice(currency)).orElseThrow();
        double sumCalculated = 0;
        for(WebElement element:regularPrice){
            String price = driverTools.getText(element);
            if(price.contains(currency)){
                sumCalculated += CoreFunctions.convertPriceSafely(price);
            }
        }
        WebElement totalPriceElement = driverTools.waitForElementToAppear(estimationTotalPrice(currency)).orElseThrow();
        String totalPrice = driverTools.getText(totalPriceElement);
        double totalPriceActual = CoreFunctions.convertPriceSafely(totalPrice);
        logger.log("sumCalculated: " + sumCalculated);
        logger.log("totalPriceActual: " + totalPriceActual);
        return sumCalculated == totalPriceActual;
    }

    public double getTotalRefundPrice(String currency){
        WebElement totalPriceElement = driverTools.waitForElementToAppear(estimationTotalPrice(currency)).orElseThrow();
        String totalPrice = driverTools.getText(totalPriceElement);
        double totalPriceActual = CoreFunctions.convertPriceSafely(totalPrice);
        logger.log("totalPriceActual: " + totalPriceActual);
        return totalPriceActual;
    }
    
    public boolean isShownEstimationOtherDate() {
        Optional<WebElement> estimation = driverTools.waitForElementToAppear(ESTIMATION_OTHER_DATES, 2);
        return estimation.isPresent();
    }

    public void clickEstimationOtherDates() {
        if (isShownEstimationOtherDate()) {
            driverTools.click(ESTIMATION_OTHER_DATES);
        } else {
            Assertion.fail("Estimation other dates not shown");
        }
    }

    public int countProgressEstimationOtherDates() {
        List<WebElement> listEstimation = driverTools.waitForElementsToAppear(ESTIMATION_OTHER_DATES_PROGRESS).orElseThrow();
        return listEstimation.size();
    }

    public void waitForEstimationAmountAppear(String timeOutSec) {
        long waitTime = System.currentTimeMillis() + (Long.parseLong(timeOutSec) * 1000);
        WebElement element = null;
        while (System.currentTimeMillis() < waitTime) {
            element = driverTools.waitForElementToAppear(ESTIMATION_REFUND_TOTAL).orElseThrow();
            if (!driverTools.getValueAttribute(element, "class").contains("disabled")) {
                logger.log("Estimation amount appear");
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }
    }

    public String getRefundYoullReceive() {
        WebElement amountElement = driverTools.waitForElementToAppear(ESTIMATION_REFUND_RECEIVE).orElseThrow();
        return driverTools.getText(amountElement);
    }

    public String getRefundFee() {
        WebElement amountElement = driverTools.waitForElementToAppear(ESTIMATION_REFUND_FEE).orElseThrow();
        return driverTools.getText(amountElement);
    }

    public String getRefundTotal() throws InterruptedException {
        int retries = 5;
        while (retries > 0) {
            WebElement amountElement = driverTools.waitForElementToAppear(ESTIMATION_REFUND_TOTAL, 20).orElseThrow();
            String amountText = driverTools.getText(amountElement);
            logger.log("Extracted refund amt: " + amountText);

            if (amountText.matches(".*\\d.*")) {
                return amountText.replaceAll("[^0-9]", "");
            }

            logger.log("Amount not extracted successfully, retrying... (" + retries + " retries left)");
            retries--;
            Thread.sleep(1000);
        }

        throw new RuntimeException("Failed to extract refund amount after multiple attempts");
    }

    public String getRefundTotalAndCurrency() {
        WebElement amountElement = driverTools.waitForElementToAppear(ESTIMATION_REFUND_TOTAL).orElseThrow();
        return driverTools.getText(amountElement);
    }

    public List<String> getRefundAmountBreakdown(String currency) {
        List<WebElement> amount = driverTools.waitForElementsToAppear(getAmountBreakdown(currency), 10).orElseThrow();
        List<String> amounts = new ArrayList<>();
        for (WebElement we : amount) {
            amounts.add(driverTools.getText(we));
        }
        return amounts;
    }

    public Optional<List<String>> getRefundFeeBreakdown(String currency) {
        Optional<List<WebElement>> amount = driverTools.waitForElementsToAppear(getFeeBreakdown(currency), 2);
        List<String> amounts = new ArrayList<>();
        if (amount.isPresent()) {
            for (WebElement we : amount.get()) {
                amounts.add(driverTools.getText(we));
            }
            return Optional.of(amounts);
        } else {
            return Optional.empty();
        }
    }

    public List<String> getRefundTotalBreakdown(String currency) {
        List<WebElement> amount = driverTools.waitForElementsToAppear(getTotalRefundBreakdown(currency), 2).orElseThrow();
        List<String> amounts = new ArrayList<>();
        for (WebElement we : amount) {
            amounts.add(driverTools.getText(we));
        }
        return amounts;
    }

    public String getRefundTotalBreakdownSum(String currency) {
        List<WebElement> amount = driverTools.waitForElementsToAppear(getTotalRefundBreakdown(currency), 10).orElseThrow();
        long sums = 0;
        for (WebElement we : amount) {
            String text = driverTools.getText(we);
            if (text == null || text.trim().isEmpty()) {
                text = we.getAttribute("textContent");
            }
            if (text == null || text.replaceAll("[^0-9-]", "").isEmpty()) {
                logger.log("Skipping refund breakdown element with no numeric text: " + text);
                continue;
            }
            sums = sums + CoreFunctions.removeText(text);
        }
        return String.valueOf(sums);
    }

    public void openRefundBreakdown() {
        driverTools.click(ESTIMATION_BREAKDOWN_ICON);
        driverTools.waitForElementToBeClickable(getClosePopupButton());
    }
    
    public void closeRefundBreakdown() {
        driverTools.click(getClosePopupButton());
    }

    public boolean isRefundGuideShown() {
        return driverTools.isElementDisplayed(CARD_SUBMISSION_GUIDE);
    }

    public boolean isRefundPageOpened() {
        return driverTools.isElementDisplayed(REFUND_REASON);
    }

    public void openRefundImportantInfo() {
        driverTools.click(CARD_IMPORTANT_INFO);
    }

    public String getModalHeaderText() {
        WebElement header = driverTools.waitForElementToAppear(getPopUpBody()).orElseThrow();
        return driverTools.getText(header);
    }

    public String getImportantInfoContent() {
        WebElement content = driverTools.waitForElementToAppear(DYNAMIC_REFUND_INFO_CONTENT).orElseThrow();
        return driverTools.getText(content);
    }

    public void clickButton(String button) {
        driverTools.waitForElementToBeClickable(getButtonByText(button), 20);
        driverTools.clickWithJavascriptExecutor(getButtonByText(button));
    }

    public void clickPrimaryButton(String textButton) {
        driverTools.waitForElementToBeClickable(getPrimaryButtonByText(textButton), 20);
        driverTools.clickWithJavascriptExecutor(getPrimaryButtonByText(textButton));
    }

    public void clickConfirmationRefundPopup() {
        driverTools.waitForElementToAppear(CONFIRMATION_REFUND_POPUP_LOCATOR,5);
        driverTools.click(CONFIRMATION_REFUND_POPUP_LOCATOR);
    }

    public void SuccessSubmitRefundPopup() {
        driverTools.waitForElementToAppear(CONFIRMATION_REFUND_POPUP_LOCATOR,5);
        driverTools.click(CONFIRMATION_REFUND_POPUP_LOCATOR);
    }

    public void clickButton(String buttonEn, String buttonId) {
        driverTools.waitForElementToAppear(getButtonByText(buttonEn, buttonId), 20);
        driverTools.clickWithJavascriptExecutor(getButtonByText(buttonId, buttonEn));
    }

    public String getReasonWrapper() {
        WebElement reason = driverTools.waitForElementToAppear(REASON_WRAPPER).orElseThrow();
        driverTools.scrollIntoWebView(reason);
        return driverTools.getText(reason);
    }

    public String getRefundMethod() {
        return driverTools.waitForElementsToAppear(REFUND_METHOD_OPTION)
                .orElseThrow()
                .stream()
                .map(driverTools::getText)
                .collect(Collectors.joining());
    }

    public String getRefundMethodOnBreakdown() {
        return driverTools.waitForElementToAppear(BREAKDOWN_REFUND_METHOD_SINGLE, 3)
                .map(driverTools::getText)
                .orElseGet(() -> driverTools.waitForElementsToAppear(BREAKDOWN_REFUND_METHOD_MULTI)
                        .orElseThrow()
                        .stream()
                        .map(driverTools::getText)
                        .collect(Collectors.joining()));
    }

    public String getRefundBankAccountNumber() {
        WebElement number = driverTools.waitForElementToAppear(REFUND_METHOD_BANK_NUMBER).orElseThrow();
        return driverTools.getText(number);
    }

    public String getRefundBankAccountName() {
        WebElement account = driverTools.waitForElementToAppear(REFUND_METHOD_BANK_ACCOUNT_NAME).orElseThrow();
        return driverTools.getText(account);
    }

    public String getRefundBankName() {
        WebElement bank = driverTools.waitForElementToAppear(REFUND_METHOD_BANK_NAME).orElseThrow();
        return driverTools.getText(bank);
    }

    public void changeRefundMethod(String refundMethod) {
        boolean isPresence = driverTools.waitForElementToAppear(CHANGE_REFUND_METHOD).isPresent();
        if (isPresence) {
            driverTools.click(CHANGE_REFUND_METHOD);
            driverTools.scrollIntoWebView(getPopUpBody());
            driverTools.clickElementByCoordinateDWeb(changeRefundMethodOption(refundMethod));
            driverTools.click(getButtonByText("Continue", "Lanjut"));
            waitForContentLoaderDisappear();
            driverTools.scrollIntoWebView(REASON_WRAPPER);
        }
    }

    public void clickEditBankInfo() {
        driverTools.scrollElementToCenter(EDIT_REFUND_METHOD);
        driverTools.click(EDIT_REFUND_METHOD);
    }

    public String selectSavedBankAccount() {
        driverTools.scrollIntoWebView(getPopUpBody());
        driverTools.click(getPopUpBody());
        List<WebElement> savedBanks = driverTools.waitForElementsToAppear(SAVED_BANK_ACCOUNT).orElseThrow();
        WebElement selectBank = savedBanks.get(CoreFunctions.generateRandomInt(0, savedBanks.size()-1));
        driverTools.click(selectBank);
        String selectedBank = driverTools.getText(selectBank);
        driverTools.click(getButtonByText("Save", "Simpan"));
        driverTools.waitForElementToDisappear(getPopUpBody());
        return selectedBank;
    }

    public String inputNewBankAccount(String bankName, String accountNumber, String otp) {
        driverTools.waitForElementToBeVisible(ADD_BANK_ACCOUNT);
        driverTools.clickWithJavascriptExecutor(ADD_BANK_ACCOUNT);
        driverTools.waitForElementToDisappear(getClosePopupButton());
        selectBankName(bankName);
        driverTools.click(INPUT_ACCOUNT_NUMBER);
        driverTools.sendKeys(INPUT_ACCOUNT_NUMBER, accountNumber);
        driverTools.clickWithJavascriptExecutor(CHECK_BANK_ACCOUNT);
        handleInputBankOTP(otp);
        driverTools.waitForElementToAppear(VALID_ACCOUNT_NAME_CHECKMARK);
        String accountName = driverTools.getValue(INPUT_ACCOUNT_NAME);
        driverTools.clickWithJavascriptExecutor(CHECKBOX_SAVE_BANK);
        driverTools.clickWithJavascriptExecutor(ADD_BANK_SAVE_BUTTON);
        return accountName;
    }

    public void handleInputBankOTP(String otp) {
        driverTools.waitForElementToAppear(getClosePopupButton());
        driverTools.click(CHECK_BANK_OTP);
        driverTools.sendKeys(CHECK_BANK_OTP, otp);
        driverTools.waitForElementToDisappear(getClosePopupButton());
    }

    public void selectBankName(String bankName) {
        driverTools.click(INPUT_BANK_NAME);
        WebElement bankNameElement = driverTools.waitForElementToAppear(getBankNameOnList(bankName)).orElseThrow();
        driverTools.scrollElementToCenter(bankNameElement);
        driverTools.click(bankNameElement);
    }

    public void openTnc() {
        driverTools.click(TNC_BUTTON);
    }

    public void clickChecklistTnc() {
        driverTools.waitForElementToPresence(TNC_CHECKLIST);
        driverTools.clickWithJavascriptExecutor(TNC_CHECKLIST);
        driverTools.dummyWait(3);
    }

    public String getTncContent() {
        WebElement content = driverTools.waitForElementToAppear(TNC_CONTENT).orElseThrow();
        return driverTools.getText(content);
    }

    public boolean isDisabledSubmitButton() {
        Optional<WebElement> button = driverTools.waitForElementToBeClickable(getButtonByText("Submit"), 1);
        return button.isEmpty();
    }

    public String getSuccessRefundFigureContent() {
        WebElement content = driverTools.waitForElementToAppear(SUCCESS_REFUND_FIGURE_CONTENT).orElseThrow();
        return driverTools.getText(content);
    }

    public void waitRefundSecondPageLoaded() {
        driverTools.waitForElementToAppear(getButtonByText("Submit"));
        waitForContentLoaderDisappear();
    }

    public void clickBackFromRefund() {
        driverTools.click(headerBackRefund());
        driverTools.waitForElementsToAppear(BACK_POP_UP_CANCEL_REFUND);
        driverTools.click(BACK_POP_UP_CANCEL_REFUND);
        waitForContentLoaderDisappear();
    }

    public void waitForContentLoaderDisappear() {
        driverTools.waitForElementToDisappear(CONTENT_LOADER);
    }

    public void waitForSubmitRefundLoader() {
        driverTools.waitForElementToAppear(ESTIMATION_OTHER_DATES_PROGRESS, 2);
        driverTools.waitForElementToDisappear(ESTIMATION_OTHER_DATES_PROGRESS);
    }

    public void continueRefund(){
        driverTools.clickWithJavascriptExecutor(CONTINUE_REFUND);
        driverTools.waitForElementToDisappear(CONTINUE_REFUND);
    }

    public void clickSubmitRefund(){
        driverTools.waitForElementToBeClickable(SUBMIT_REFUND);
        driverTools.clickWithJavascriptExecutor(SUBMIT_REFUND);
        driverTools.waitForElementToAppear(PROCESS_REFUND, 10);
    }

    public void clickProcessRefund(){
        driverTools.scrollIntoWebView(PROCESS_REFUND);
        driverTools.clickWithJavascriptExecutor(PROCESS_REFUND);
        driverTools.dummyWait(5);
    }

    public void clickGoToOrders(){
        driverTools.waitForElementToAppear(GO_TO_ORDERS);
        driverTools.clickWithJavascriptExecutor(GO_TO_ORDERS);
    }

    public void continueToRefund(){
        if (driverTools.waitForElementToPresence(RESCHEDULE_NUDGE_WRAPPER, 10).isPresent()) {
            driverTools.waitForElementToAppear(CONTINUE_TO_REFUND);
            driverTools.clickWithJavascriptExecutor(CONTINUE_TO_REFUND);
        }
    }

    public void followRefundMethodIfRequired(boolean isPaymentFree) {
        if(isPaymentFree){
            clickChecklistTnc();
            clickSubmitRefund();
            clickProcessRefund();
            clickGoToOrders();
        }
    }

    public boolean isNudgeShow() {
        boolean isNudgeShow = driverTools.waitForElementsToAppear(RESCHEDULE_NUDGE_WRAPPER).isPresent();
        logger.log("Is reschedule nudges showing: "+isNudgeShow);
        return isNudgeShow;
    }

    public void openAutoRefundURL(String requestHash) {
        String deeplink = "";
        if (Configs.ENVIRONMENT.equals(Environment.PREPROD)) {
            deeplink = "https://preprod.tiket.com/my-refund/v4/refunds/" + requestHash + "/request";
        } else if (Configs.ENVIRONMENT.equals(Environment.PROD)) {
            deeplink = "https://www.tiket.com/my-refund/v4/refunds/" + requestHash + "/request";
        } else if (Configs.ENVIRONMENT.equals(Environment.GK)) {
            deeplink = "https://gatotkaca.tiket.com/my-refund/v4/refunds/" + requestHash + "/request";
        }
        driverTools.get(deeplink);
    }

    public void openRefundTrackerURL(String requestHash) {
        String deeplink = "";
        if (Configs.ENVIRONMENT.equals(Environment.PREPROD)) {
            deeplink = "https://preprod.tiket.com/my-refund/v4/refunds/" + requestHash + "/track";
        } else if (Configs.ENVIRONMENT.equals(Environment.PROD)) {
            deeplink = "https://www.tiket.com/my-refund/v4/refunds/" + requestHash + "/track";
        } else if (Configs.ENVIRONMENT.equals(Environment.GK)) {
            deeplink = "https://gatotkaca.tiket.com/my-refund/v4/refunds/" + requestHash + "/track";
        }
        driverTools.get(deeplink);
    }

    public void selectRoomForRefund(int roomIndex) {
        continueToRefund();
        roomIndex = roomIndex - 1;
        driverTools.waitForElementToAppear(SELECT_ROOM_FOR_REFUND);
        List<WebElement> roomButtons = driverTools.elements(SELECT_ROOM_FOR_REFUND).orElseThrow();
        if (roomIndex < 0 || roomIndex >= roomButtons.size()) {
            throw new IndexOutOfBoundsException("Room index out of bounds: " + roomIndex + ". Available rooms: " + roomButtons.size());
        }
        driverTools.clickWithJavascriptExecutor(roomButtons.get(roomIndex));
        driverTools.waitForElementToAppear(START_REFUND_BUTTON);
        driverTools.clickWithJavascriptExecutor(START_REFUND_BUTTON);
    }


    public void verifyRefundedRoom(String expectedRoomName) {
        WebElement roomNameElement = driverTools.waitForElementToAppear(REFUND_ROOM_NAME, 5).orElseThrow();
        
        String actualRoomName = driverTools.getText(roomNameElement).trim();
        logger.log("Refunded room name from card: " + actualRoomName);
        
        Assertion.assertThat("Refunded room name should match expected",
                actualRoomName, containsString(expectedRoomName));
    }

    public void continueToNextPage(){
        driverTools.clickWithJavascriptExecutor(CONTINUE_TO_NEXT_PAGE);
    }

    public void verifyFlightDetails() {
        Assertion.assertThat("Verify Flight Details Title is shown", driverTools.scrollUntilElementFound(FLIGHT_DETAILS_TITLE).orElseThrow().getText(), in(Arrays.asList("Flight Details", "Detail Penerbangan")));
        Assertion.assertThat("Verify Airline Logo is shown", driverTools.waitForElementsToAppear(AIRLINE_LOGO).isPresent(), is(true));
        Assertion.assertThat("Verify Flight Name and Number is shown", driverTools.waitForElementToAppear(FLIGHT_NAME_AND_NUMBER).isPresent(), is(true));
        Assertion.assertThat("Verify Flight Departure is shown", driverTools.getText(FLIGHT_DESTINATION_FROM), notNullValue());
        Assertion.assertThat("Verify Flight Destination is shown", driverTools.getText(FLIGHT_DESTINATION_TO), notNullValue());
        Assertion.assertThat("Verify Flight Date is shown", driverTools.getText(FLIGHT_SCHEDULED_DATE), notNullValue());
        Assertion.assertThat("Verify Flight Time is shown", driverTools.getText(FLIGHT_SCHEDULED_TIME), notNullValue());
    }

    public void verifyFlightDetails(Map<String, String> flight) {
        Assertion.assertThat("Verify Flight Details Title is shown", driverTools.scrollUntilElementFound(FLIGHT_DETAILS_TITLE).orElseThrow().getText(), in(Arrays.asList("Flight Details", "Detail Penerbangan")));
        Assertion.assertThat("Verify Airline Logo is shown", driverTools.waitForElementsToAppear(AIRLINE_LOGO).isPresent(), is(true));

        String nameAndNumber = textOf(FLIGHT_NAME_AND_NUMBER);
        Assertion.assertThat("Airline name matches the order", nameAndNumber, containsStringIgnoringCase(flight.get("airlinesName")));
        Assertion.assertThat("Flight number matches the order", nameAndNumber, containsString(flight.get("flightNumber")));

        String from = textOf(FLIGHT_DESTINATION_FROM);
        Assertion.assertThat("Departure city matches the order", from, containsStringIgnoringCase(flight.get("departureCityName")));
        Assertion.assertThat("Departure airport matches the order", from, containsString(flight.get("departureCity")));

        String to = textOf(FLIGHT_DESTINATION_TO);
        Assertion.assertThat("Arrival city matches the order", to, containsStringIgnoringCase(flight.get("arrivalCityName")));
        Assertion.assertThat("Arrival airport matches the order", to, containsString(flight.get("arrivalCity")));

        Assertion.assertThat("Flight date matches the order", textOf(FLIGHT_SCHEDULED_DATE), containsString(expectedFlightDate(flight.get("flightDate"))));
        Assertion.assertThat("Departure time matches the order", textOf(FLIGHT_SCHEDULED_TIME), is(expectedDepartureTime(flight.get("departureTime"), flight.get("departureTz"))));
    }

    private String textOf(Locator locator) {
        return driverTools.getText(driverTools.waitForElementToAppear(locator).orElseThrow());
    }

    private String expectedFlightDate(String flightDate) {
        return LocalDate.parse(flightDate).format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH));
    }

    private String expectedDepartureTime(String departureTime, String departureTz) {
        return LocalDateTime.parse(departureTime.replace(" ", "T"))
                .plusMinutes(Math.round(Double.parseDouble(departureTz) * 60))
                .format(DateTimeFormatter.ofPattern("HH.mm"));
    }

    public String getRefundedOrderDetailId() {
        String url = driverTools.getCurrentURL();
        Matcher matcher = Pattern.compile("/refunds/\\d+/(\\d+)").matcher(url);
        if (!matcher.find()) {
            Assertion.fail("The refund URL does not name an orderDetailId: " + url);
        }
        logger.log("Refund is requested for orderDetailId " + matcher.group(1));
        return matcher.group(1);
    }

    public void clickViewRefundDetail() {
        driverTools.waitForElementToBeClickable(VIEW_REFUND_DETAIL);
        driverTools.clickWithJavascriptExecutor(VIEW_REFUND_DETAIL);
    }

    public void closeRelatedOrderPopupIfShown() {
        driverTools.waitForElementToAppear(RELATED_ORDER_POPUP, 5).ifPresent(popUp -> {
            logger.log("Related Order popup is shown, a multi-leg order is refunded one order at a time");
            driverTools.clickWithJavascriptExecutor(getClosePopupButton());
            driverTools.waitForElementToDisappear(RELATED_ORDER_POPUP, 5);
        });
    }

    public int[] checkAllPassengerCheckbox() {
        List<WebElement> elements = driverTools.waitForElementsToAppear(PASSENGER_CHECKBOX).orElseThrow();
        int alreadyChecked = 0;
        for (WebElement element : elements) {
            if (element.isSelected()) {
                alreadyChecked++;
            } else {
                driverTools.clickWithJavascriptExecutor(element);
            }
        }
        logger.log("Passenger checkbox(es): " + elements.size() + " total, " + alreadyChecked + " already checked before this");
        return IntStream.range(0, elements.size()).toArray();
    }

    public void checkPassengerCheckbox(int[] passengerIndex) {
        List<WebElement> elements = driverTools.waitForElementsToAppear(PASSENGER_CHECKBOX).orElseThrow();
        for (int i : passengerIndex) {
            driverTools.clickWithJavascriptExecutor(elements.get(i));
        }
    }

    public void selectSpecificReason(int[] passengerIndex) {
        List<WebElement> elements = driverTools.waitForElementsToAppear(SPECIFIC_REASON_DROPDOWN).orElseThrow();
        for (int i : passengerIndex) {
            driverTools.scrollElementToCenter(elements.get(i));
            driverTools.clickWithJavascriptExecutor(elements.get(i));
            List<WebElement> selections = driverTools.waitForElementsToAppear(SPECIFIC_REASON_SELECTION).orElseThrow();
            driverTools.clickWithJavascriptExecutor(selections.get(CoreFunctions.generateRandomInt(0, selections.size()-1)));
        }
        driverTools.dummyWait(4);
    }

    public String getRefundFeeBreakdownSum(){
        driverTools.waitForElementToAppear(REFUND_CHARGED_AMOUNT);
        WebElement feeElement = driverTools.waitForElementToAppear(REFUND_CHARGED_AMOUNT).orElseThrow();
        String feeText = driverTools.getText(feeElement);
        logger.log("Refund fee Charged text: " + feeText);
        return feeText.replaceAll("[^0-9]", "");
    }

    public void ProcessToRefundB2B(){
        driverTools.waitForElementToAppear(B2B_PROCESS_TO_REFUND,3);
        driverTools.clickWithJavascriptExecutor(B2B_PROCESS_TO_REFUND);
    }
}
