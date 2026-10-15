package com.tiket.web.page.platform.payment;
import static com.tiket.testbase.Configs.ENVIRONMENT;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import org.apache.commons.lang3.NotImplementedException;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mongodb.client.FindIterable;
import com.tiket.api.accommodation.hotel.HotelPendingRetrySummaryNotifier;
import com.tiket.api.payment.DefaultPgConfigApi;
import com.tiket.api.payment.UpdatePgConfigApi;
import com.tiket.model.api.ApiResponse;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.dbclient.TiketMongoDBManager;
import com.tiket.model.Deeplink;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.flight.bookingForm.FlightBookingFormPageWeb;

import io.appium.java_client.AppiumBy;

public abstract class PaymentPageWeb<T extends WebDriver> extends WebPage<T> {

    public PaymentPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> PaymentPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new PaymentPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new PaymentPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public String getCurrentGiftCardDeliveryDateTime() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime now = LocalDateTime.now(zoneId);

        if (Configs.PLATFORM == Platform.DWEB) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy • HH:mm", Locale.ENGLISH);
            return now.format(formatter);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale.ENGLISH);
            return now.format(formatter);
        }
    }
    private static final Locator REFUND_MODAL_TITLE = new Locator("REFUND_MODAL_TITLE", By.xpath("//p[contains(@class, 'styles_title') and contains(text(), 'Refund & Reschedule Info')]"));
    private static final Locator REFUND_INFO_IFRAME = new Locator("REFUND_INFO_IFRAME", By.xpath("//div[contains(@class, 'styles_iframe_container')]//iframe"));
    private static final Locator REFUND_100_TEXT = new Locator("REFUND_100_TEXT", By.xpath("//h6[contains(@class, 'info-100p-title')]"));
    private static final Locator ACTIVE_PAYMENT_TIMELINE = new Locator("ACTIVE_PAYMENT_TIMELINE", By.xpath("//div[contains(@class, 'tab-content') and contains(@class, 'active')] | //div[contains(@class,'Tabs_tab_panel') and not(@hidden)]"));
    private static final Locator ORDER_SUMMARY_DETAILS_LINK = new Locator("ORDER_SUMMARY_DETAILS_LINK", new By.ByXPath("//span[contains(@class, 'RefundReschedule_pointer') and (contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'detail') or contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'rincian'))]"));
    private static final Locator AIRWALLEX_IFRAME = new Locator("AIRWALLEX_IFRAME", AppiumBy.xpath("//*[@id='issuer-iframe']"));
    private static final Locator PAYMENT_PAGE_CHECK= new Locator("PAYMENT_PAGE_CHECK", AppiumBy.xpath("//div[@data-testid='payment-method']"));
    private static final Locator CONTINUE_PAYMENT_CONTAINER = new Locator("CONTINUE_PAYMENT_CONTAINER", AppiumBy.xpath("//div[contains(@class,'Countdown_countdown')]"));
    private static final Locator FINAL_PRICE = new Locator("FINAL_PRICE", new By.ByXPath("//span[contains(@class, 'PriceSummary_final_price__')]"));
    private static final Locator ORDER_ID_CONTAINER = new Locator("ORDER_ID_CONTAINER", new By.ByXPath("//*[contains(text(), 'Order ID:')]"));
    private static final Locator PAYMENT_METHOD_RECOMMENDATION = new Locator("PAYMENT_METHOD_RECOMMENDATION", By.xpath("//*[contains(@class, 'Recommended_recomendation')]"));
    private static final Locator CONTINUE_TO_PAYMENT_BUTTON = new Locator("CONTINUE_TO_PAYMENT_BUTTON", By.xpath("//button[contains(@class,'Button_variant_primary')][.//span[contains(normalize-space(.),'Continue to payment') or contains(normalize-space(.),'Lanjut Bayar')]]"));
    private static final Locator SEE_ALL_PAYMENT_METHOD = new Locator("SEE_ALL_PAYMENT_METHOD", By.xpath("//div[@data-testid='seeall']"));
    private static final Locator SELECT_PAYMENT_METHOD = new Locator("SELECT_PAYMENT_METHOD", new By.ByXPath("//*[text()='Select Payment Method' or text()='Pilih metode pembayaran']"));
    private static final Locator SELECTED_PAYMENT = new Locator("SELECTED_PAYMENT", new By.ByXPath("//div[contains(@class,'PaymentTile_seleted_payment_text')]//span[1]"));
    private static final Locator PAYMENT_BUTTON = new Locator("PAYMENT_BUTTON", new By.ByXPath("//button[contains(@class, 'SubmitPayment_payment')]"));
    private static final Locator PAYMENT_INSTRUCTION_TEXT = new Locator("PAYMENT_INSTRUCTION_TEXT", new By.ByXPath("//p[text()='Payment Instructions']"));
    private static final Locator PROMO_INPUT_FIELD = new Locator("PROMO_INPUT_FIELD", new By.ByXPath("//input[@name='promo-input' or contains(@id,'enter-your-code')]"));
    private static final Locator APPLY_BUTTON = new Locator("APPLY_BUTTON", new By.ByXPath("//button[contains(@class,'PromoCodeInputForm_apply_button')]"));
    private static final Locator ERROR_MSG_NOT_APPLICABLE_PROMO = new Locator("ERROR_MSG_NOT_APPLICABLE_PROMO", new By.ByXPath("//div[contains(@class, 'styles_helper_text_container')]//span"));
    private static final Locator CLOSE_DRAWER = new Locator("CLOSE_DRAWER", new By.ByXPath("//button[contains(@class,'BottomSheet_close_button')]"));
    public static final Locator PAYMENT_DRAWER_HEADING = new Locator("PAYMENT_DRAWER_HEADING", new By.ByXPath("//h2[contains(@class,'Text')]"));
    private static final Locator ELIGIBLE_PROMO = new Locator("ELIGIBLE_PROMO", new By.ByXPath("//div[contains(@class, 'EligiblePromos_list')]//div[contains(@class,'SuggestionItem_suggestion_item')]"));
    private static final Locator CHANGE_PAYMENT_BUTTON = new Locator("CHANGE_PAYMENT_BUTTON", By.xpath("//button[text()='Change payment' or text()='Ganti pembayaran']"));
    private static final Locator CHANGE_PAYMENT_METHOD_BUTTON = new Locator("CHANGE_PAYMENT_METHOD_BUTTON", By.xpath("//*[text()='Change Payment Method' or text()='Ganti metode pembayaran']"));
    private static final Locator PROMO_INSTANT_APPLIED_TEXT = new Locator("PROMO_INSTANT_APPLIED_TEXT", new By.ByXPath("//div[contains(@class,'AppliedPromoText')]//p"));
    private static final Locator PROMO_INSTANT_APPLIED_TEXT_CODE = new Locator("PROMO_INSTANT_APPLIED_TEXT_CODE", new By.ByXPath("//div[contains(@class,'PromoDisplay_text_wrapper')]//p"));
    private static final Locator PROMO_DISPLAY_CONTAINER = new Locator("PROMO_DISPLAY_CONTAINER", new By.ByXPath("//div[contains(@class,'PromoDisplay_main_container')]"));
    private static final Locator PROMO_DISPLAY_CTA = new Locator("PROMO_DISPLAY_CTA", new By.ByXPath("//div[@data-testid='promoDisplayCta']"));
    private static final Locator SLASH_PRICE = new Locator("SLASH_PRICE", new By.ByXPath("//span[contains(@class,'PriceSummary_slash_price')]"));
    private static final Locator PRICE_SUMMARY_DISCOUNT = new Locator("PRICE_SUMMARY_DISCOUNT", new By.ByXPath("//div[contains(@class,'PriceSummary_discount_wrapper')]//span"));
    private static final Locator PROMO_CANNOT_BE_USED = new Locator("PROMO_CANNOT_BE_USED", new By.ByXPath("//h2[text()='Promo cannot be used' or text()='Promo tidak bisa dipakai']"));
    private static final Locator PRICE_SUMMARY_CONTAINER = new Locator("PRICE_SUMMARY_SVG", new By.ByXPath("(//div[@data-testid='payment_summary'] | //div[contains(@class,'PriceSummary_price_summary')] | //div[contains(@class,'payment_bottom_sticky_col')] | //span[contains(@class,'PriceSummary_final_price')]/ancestor::div[1])[2]"));
    private static final Locator UNIQUE_CODE_TEXT = new Locator("UNIQUE_CODE_TEXT", new By.ByXPath("(//span[text()='Unique code'])[1]"));
    private static final Locator UNIQUE_CODE = new Locator("UNIQUE_CODE", new By.ByXPath("(//div[contains(@class,'PriceDetail')]//span[contains(text(), '- IDR')])[1]"));
    private static final Locator CREDIT_CARD_NUMBER = new Locator("CREDIT_CARD_NUMBER", new By.ByXPath("//input[@id='creditdebit-card-number' or @name='cardNumber']"));
    private static final Locator CREDIT_CARD_MONTH_YEAR = new Locator("CREDIT_CARD_MONTH_YEAR", new By.ByXPath("//input[@id='mmyy']"));
    private static final Locator CREDIT_CARD_CVV = new Locator("CREDIT_CARD_CVV", new By.ByXPath("//input[@id='cvccvv']"));
    private static final Locator CREDIT_CARD_HOLDER_NAME = new Locator("CREDIT_CARD_HOLDER_NAME", new By.ByXPath("//input[@id='cardholder-name' or @name='cardHolderName']"));
    private static final Locator BIBLI_PAYLATER_ELEMENT = new Locator("BIBLI_PAYLATER_ELEMENT", new By.ByXPath("//*[self::span or self::p][normalize-space(text())='Blibli Tiket PayLater']/ancestor::li[1]"));
    private static final Locator FORBIDDEN_PAGE = new Locator("FORBIDDEN_PAGE", new By.ByXPath("//h1[text()='403 Forbidden']"));
    private static final Locator KREDIVO_PAGE = new Locator("KREDIVO_PAGE", By.xpath("//*[contains(.,'Kredivo')]"));
    private static final Locator BEST_SUITED_PROMO = new Locator("BEST_SUITED_PROMO", new By.ByXPath("//div[contains(@class, 'BestSuitedPromoBanner_best_suited_promo_box')]"));
    private static final Locator TIKET_POINTS_RADIO = new Locator("TIKET_POINTS_RADIO", By.xpath("//input[@data-testid='btnApplyTiketPoints']"));
    private static final Locator REFUND_DEPOSIT_PAYMENT_METHOD = new Locator("REFUND_DEPOSIT_PAYMENT_METHOD", By.xpath("//*[contains(text(),'Deposit Refund') or contains(text(),'Refund Deposit')]"));
    private static final Locator CHECK_TNC = new Locator("CHECK_TNC", new By.ByXPath("//p[contains(text(),'S&K') or contains(text(),'T&C')]"));
    private static final Locator TNC_APPLY_BUTTON = new Locator("TNC_APPLY_BUTTON", new By.ByXPath("//footer//button"));
    private static final Locator OVO_ELEMENT = new Locator("OVO_ELEMENT", new By.ByXPath("//*[self::span or self::p][normalize-space(text())='OVO']/ancestor::li[1]"));
    private static final Locator AMOUNT_ON_2C2P_PAGE = new Locator("AMOUNT_ON_2C2P_PAGE", new By.ByXPath("(//div[@class='date value'])[2]"));
    private static final Locator OTP_FIELD_ON_2C2P_PAGE = new Locator("OTP_FIELD_ON_2C2P_PAGE", new By.ByXPath("//input[contains(@class,'card-textbox')]"));
    private static final Locator SUBMIT_BUTTON_ON_2C2P_PAGE = new Locator("SUBMIT_BUTTON_ON_2C2P_PAGE", new By.ByXPath("//button[contains(text(),'Submit')]"));
    private static final Locator OTP_FIELD_XENDIT = new Locator("OTP_FIELD_XENDIT", new By.ByXPath("//*[contains(@class,'input-field')]"));
    private static final Locator SUBMIT_BUTTON_XENDIT = new Locator("SUBMIT_BUTTON_XENDIT", new By.ByXPath("//*[contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'submit')]"));
    private static final Locator OTP_FIELD_BCA = new Locator("OTP_FIELD_BCA", new By.ByXPath("//*[@id='txnOTP']"));
    private static final Locator OTP_VALUE_BCA = new Locator("OTP_VALUE_BCA", new By.ByXPath("//*[@id='lblTestModeOTP']"));
    private static final Locator SUBMIT_BUTTON_BCA = new Locator("SUBMIT_BUTTON_BCA", new By.ByXPath("//*[@id='btnProceed']"));
    private static final Locator COMPLETE_BUTTON = new Locator("COMPLETE_BUTTON", new By.ByXPath("//button[contains(text(),'Complete')]"));
    private static final Locator SELECT_CREDIT_CARD = new Locator("SELECT_CREDIT_CARD", new By.ByXPath("(//*[normalize-space()='Add Card'])[2]"));
    private static final Locator POINTS_ON_LANDING_PAGE = new Locator("POINTS_ON_LANDING_PAGE", new By.ByXPath("//div[contains(@class,'v4_tiket_point')]//b"));
    private static final Locator POINTS_ON_CONFIRMATION_PAGE = new Locator("POINTS_ON_CONFIRMATION_PAGE", new By.ByXPath("//div[contains(@class,'confirm_tiket')]//b"));
    private static final Locator FINAL_PRICE_IDR = new Locator("FINAL_PRICE_IDR", new By.ByXPath("//span[contains(@class , 'PriceSummary_final_price') and contains(text(), 'IDR')]"));
    private static final Locator UNAPPLY_BUTTON = new Locator("UNAPPLY_BUTTON", new By.ByXPath("//*[local-name()='svg' and @data-testid='unapply-promo']"));
    private static final Locator REMOVE_PROMO_BUTTON = new Locator("REMOVE_PROMO_BUTTON", new By.ByXPath("//button[text()='Remove Promo' or text()='Hapus Promo']"));
    private static final Locator VA_NUMBER = new Locator("VA_NUMBER", new By.ByXPath("//h3[contains(@class,'Text')]"));
    private static final Locator CHECK_PAYMENT_STATUS_BUTTON = new Locator("CHECK_PAYMENT_STATUS_BUTTON", new By.ByXPath("//*[contains(@class, 'Button_icon')]"));
    private static final Locator SEE_ORDER_LIST_BUTTON = new Locator("SEE_ORDER_LIST_BUTTON", By.xpath(
            "//button[contains(@class,'Button_variant_primary')][.//span[normalize-space()='See Order List' or normalize-space()='See the order list' or normalize-space()='Lihat daftar pesanan' or normalize-space()='Lihat Daftar Pesanan']]"
                    + "|//a[contains(@href,'yourorder')][.//span[normalize-space()='See Order List' or normalize-space()='See the order list' or normalize-space()='Lihat daftar pesanan' or normalize-space()='Lihat Daftar Pesanan']]"));
    private static final Locator SEE_ORDER_LIST_BUTTON_AFTER_SUCCESS = new Locator("SEE_ORDER_LIST_BUTTON_AFTER_SUCCESS", By.xpath("//*[self::a or self::button or @role='button' or @role='link']" + "[.//span[normalize-space()='See the order list' or normalize-space()='Lihat daftar pesanan']]"));
    private static final Locator CHECK_PAYMENT_STATUS = new Locator("CHECK_PAYMENT_STATUS", new By.ByXPath("//button[.//span[normalize-space()='Check the payment status' or normalize-space()='Cek status pembayaran']]"));
    private static final Locator LOG_IN_NOW_BANNER = new Locator("LOG_IN_NOW_BANNER", new By.ByXPath("//*[contains(text(),'Log in')]"));
    private static final Locator LOG_IN_NOW = new Locator("LOG_IN_NOW", new By.ByXPath("//div[contains(@class,'Dialog')]//button//span[text()='Log in now' or text()='Log in sekarang']"));
    private static final Locator LOG_IN_BUTTON = new Locator("LOG_IN_BUTTON", new By.ByXPath("//button[text()='Log in']"));
    
    private static final Locator PROMO_APPLIED = new Locator("PROMO_APPLIED", new By.ByXPath("//div[contains(@class, 'PromoDisplay_text_wrapper__')]//p[contains(@class, 'Text_text__')] | //div[contains(@class,'AppliedPromoText')]//p"));
    private static final Locator SEE_ALL_METHOD = new Locator("SEE_ALL_METHOD", new By.ByXPath("(//span[contains(@class, 'Text_variant_activity')])[2]"));
    private static final Locator LOGIN_BOX = new Locator("LOGIN_BOX", new By.ByXPath("//*[contains(@class,'LoginBox')]"));
    private static final Locator BRI_VIRTUAL_ACCOUNT = new Locator("BRI_VIRTUAL_ACCOUNT", new By.ByXPath("//span[text()='BRI Virtual Account']"));
    private static final Locator CONTINUE_WITHOUT_PROMO = new Locator("CONTINUE_WITHOUT_PROMO", new By.ByXPath("//button[text()='Continue Without Promo' or text()='Lanjut Tanpa Promo']"));
    private static final Locator TIKET_POINTS = new Locator("TIKET_POINTS", By.xpath("//div[contains(@class,'TiketPoints_desktop_points_radio')]//span[contains(@class,'Radio_checkbox')]"));
    private static final Locator CONTINUE_WITH_POINTS = new Locator("CONTINUE_WITH_POINTS", new By.ByXPath("//button[text()='Continue with points' or text()='Lanjut dengan poin']"));
    private static final Locator PROMO_VALIDATION_MESSAGE = new Locator("PROMO_VALIDATION_MESSAGE", new By.ByXPath("//form[contains(@class,'PromoInputForm_discount_code_input')]//span[contains(@class,'Text_variant_alert')]"));
    private static final Locator CONTINUE_TO_PAYMENT = new Locator("CONTINUE_TO_PAYMENT", new By.ByXPath("//button[.//span[normalize-space()='Continue to payment']]"));
    private static final Locator ORDER_FREE = new Locator("ORDER_FREE", new By.ByXPath("//span[text()='Free' and contains(@class, 'PriceSummary_final_price')]"));
    private static final Locator ORDER_DETAILS_BUTTON = new Locator("ORDER_DETAILS_BUTTON", By.xpath("//button[contains(@class,'OrderSummaryItem_container')]"));
    private static final Locator ORDER_DETAILS_CLOSE_BUTTON = new Locator("ORDER_DETAILS_CLOSE_BUTTON", By.xpath("//button[@data-testid='modal-close-button' or contains(@class,'_close_')]"));
    private static final Locator ORDER_SUMMARY_CONTAINER = new Locator("ORDER_SUMMARY_CONTAINER", By.xpath("//div[contains(@class,'OrderSummaryItem')]"));
    private static final Locator ORDER_SUMMARY_LOGO = new Locator("ORDER_SUMMARY_LOGO", By.xpath("//div[contains(@class,'OrderSummaryItem')]//img"));
    private static final Locator ORDER_SUMMARY_PRODUCT_NAME = new Locator("ORDER_SUMMARY_PRODUCT_NAME", By.xpath("//span[contains(@class,'OrderSummaryItem_summary_title')]/div"));
    private static final Locator ORDER_SUMMARY_TITLE = new Locator("ORDER_SUMMARY_TITLE", By.xpath("//h2[text()='Order Summary']"));
    private static final Locator ORDER_SUMMARY_FLIGHT_CARDS = new Locator("ORDER_SUMMARY_FLIGHT_CARDS", By.cssSelector("[data-testid^='btnOrderSummaryItem-']"));
    private static final Locator ORDER_SUMMARY_FLIGHT_COUNT_LABEL = new Locator("ORDER_SUMMARY_FLIGHT_COUNT_LABEL", By.xpath("//div[contains(@class,'NewOrderSummarySection_summary_item_container')]//div[contains(@class,'OrderSummaryItem_summary_label')]"));
    private static final Locator ORDER_SUMMARY_LIST_MODAL = new Locator("ORDER_SUMMARY_LIST_MODAL", By.xpath("//section[contains(@class,'Modal_modal_popup_small__')]"));
    private static final Locator ORDER_SUMMARY_LIST_MODAL_CLOSE = new Locator("ORDER_SUMMARY_LIST_MODAL_CLOSE", By.xpath("//section[contains(@class,'Modal_modal_popup_small__')]//button[@data-testid='modal-close-button']"));
    private static final Locator FLIGHT_DETAIL_MODAL = new Locator("FLIGHT_DETAIL_MODAL", By.xpath("//section[contains(@class,'Modal_modal_popup_medium__')]"));
    private static final Locator FLIGHT_DETAIL_MODAL_CLOSE = new Locator("FLIGHT_DETAIL_MODAL_CLOSE", By.xpath("//section[contains(@class,'Modal_modal_popup_medium__')]//button[@data-testid='modal-close-button']"));
    private static final Locator FLIGHT_DETAIL_TITLE = new Locator("FLIGHT_DETAIL_TITLE", By.xpath("//section[contains(@class,'Modal_modal_popup_medium__')]//h2[contains(@class,'detailV4_header_title__')]"));
    private static final Locator ORDER_ID_ABOVE_ORDER_DETAILS = new Locator("ORDER_ID_ABOVE_ORDER_DETAILS", By.xpath("//p[starts-with(normalize-space(),'Order ID:')]"));
    private static final Locator PASSENGER_NAME = new Locator("PASSENGER_NAME", By.xpath("//h4[contains(@class,'TrainPassengersDetail_passenger_name') and contains(@class,'Text_text') and contains(@class,'Text_size_b2') and contains(@class,'Text_weight_bold')]"));
    private static final Locator DEPARTURE_TIME = new Locator("DEPARTURE_TIME", By.xpath("(//h4[contains(@class,'Text_text') and contains(@class,'Text_size_b2') and contains(@class,'Text_weight_bold') and contains(text(), ':')])[1]"));
    private static final Locator ARRIVAL_TIME = new Locator("ARRIVAL_TIME", By.xpath("(//h4[contains(@class,'Text_text') and contains(@class,'Text_size_b2') and contains(@class,'Text_weight_bold') and contains(text(), ':')])[2]"));
    private static final Locator ERROR_POP_UP_MSG_POP_UP = new Locator("ERROR_POP_UP_MSG_POP_UP", new By.ByXPath("//section[contains(@class, 'Figure_figure')]"));
    private static final Locator ERROR_POP_UP_MSG_DEBIT_BIN = new Locator("ERROR_POP_UP_MSG_DEBIT_BIN", new By.ByXPath("//section[contains(@class, 'Figure_figure')]//h2"));
    private static final Locator TRAIN_PASSENGER_NAME = new Locator("TRAIN_PASSENGER_NAME", new By.ByXPath("(//div[contains(@class,'PassengersAndFacilities_detail_header')]//h4[contains(@class,'Text_text')])[1] | (//h4[contains(@class,'TrainPassengersDetail')])[1]"));
    private static final Locator SELECT_INDOMARET = new Locator("SELECT_INDOMARET", new By.ByXPath("//li[.//p[normalize-space(text())='Indomaret'] or .//span[normalize-space(text())='Indomaret']]"));
    private static final Locator CONFIRM_CHANGE_PAYMENT_METHOD = new Locator("CONFIRM_CHANGE_PAYMENT_METHOD", new By.ByXPath("(//*[text()='Change Payment Method' or text()='Ganti metode pembayaran'])[2]"));
    private static final Locator PAYMENT_PAGE = new Locator("PAYMENT_PAGE", new By.ByXPath("//div[@data-testid='payment-method']"));
    private static final Locator PAYMENT_PAGE_CHECKIN = new Locator("CHECKIN_DATE", new By.ByXPath("(//div[contains(@class,'NewOrderSummarySection_summary_item_container')]//div[2]//span[1])"));
    private static final Locator PAYMENT_PAGE_NIGHT = new Locator("NIGHT_STAY", new By.ByXPath("(//div[contains(@class,'NewOrderSummarySection_summary_item_container')]//div[2]//span[3])"));
    private static final Locator PAYMENT_LOGO_ON_PAYMENT_CONFIRMATION_PAGE = new Locator("PAYMENT_LOGO_ON_PAYMENT_CONFIRMATION_PAGE", new By.ByXPath("//img[@alt='logo']"));
    private static final Locator HOW_TO_PAY_SECTION = new Locator("HOW_TO_PAY_SECTION", new By.ByXPath("//div[contains(@class, 'Card_card_container') and contains(@class, 'HowToPay') or contains(@class, 'Card_variant')]"));
    private static final Locator PERMATA_VA_TITLE = new Locator("PERMATA_VA_TITLE", new By.ByXPath("//p[contains(text(), 'Permata Virtual Account')]"));
    private static final Locator INSURANCE_APPLIED = new Locator("INSURANCE_APPLIED", new By.ByXPath("//*[@id=\"modal-root\"]//span[normalize-space(.)=\"Add-ons\"]"));
    private static final Locator BSI_VA_TITLE = new Locator("BSI_VA_TITLE", new By.ByXPath("//p[contains(text(), 'BSI Virtual Account')]"));
    private static final Locator LPG_LABEL_PAYMENT_PAGE = new Locator("LPG_LABEL_PAYMENT_PAGE", new By.ByXPath("//img[contains(@alt, 'Lowest Price Guarantee') or contains(@alt, 'Jaminan Harga Termurah')]//ancestor::div[contains(@class, 'LpgBanner_label_container')]"));
    public static final Locator INSTALLMENT_CONTAINER = new Locator("INSTALLMENT_CONTAINER", new By.ByXPath("//input[@id='installment']"));
    protected static final Locator SELECT_INSTALLMENT_DROPDOWN = new Locator("SELECT_INSTALLMENT_DROPDOWN", By.id("select-installment"));
    protected static final Locator INSTALLMENT_DROPDOWN_FIRST_OPTION = new Locator("INSTALLMENT_DROPDOWN_FIRST_OPTION", By.xpath("//div[@id='frmPaymentMethodInstallment']//div[@data-testid='collapse-container' and @aria-hidden='false']//span[contains(@class,'Radio_checkbox')][.//input[contains(@id,'installment-radio')]]"));
    protected static final Locator INSTALLMENT_INTEREST_ZERO_LABEL = new Locator("INSTALLMENT_INTEREST_ZERO_LABEL",
            By.xpath("//ul[contains(@class,'styles_installment_dropdown')]"
                    + "//p[contains(@class,'InstallmentOption_tag')][contains(.,'0%')]"));
    protected static final Locator INSTALLMENT_FIRST_PAYMENT_DUE_LABEL = new Locator("INSTALLMENT_FIRST_PAYMENT_DUE_LABEL",
            By.xpath("//span[normalize-space()='First payment due on']"));
    private static final Locator BOOKING_EARNING_POINTS = new Locator("BOOKING_EARNING_POINTS", By.xpath("//span[contains(@class,'PaymentDetail_point_wrapper') and (contains(., 'point') or contains(., 'poin'))]"));
    private static final Locator PAYMENT_EARNING_POINTS = new Locator("PAYMENT_EARNING_POINTS", By.xpath("//div[contains(@class,'Text_text') and contains(.,'Blibli Tiket Points')]"));
    private static final Locator CONVENIENCE_FEE = new Locator("CONVENIENCE_FEE", By.xpath("(//span[contains(@class,'content_title') and (contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'convenience fee') or contains(.,'Biaya platform') or contains(.,'Biaya Pemesanan'))])[2]"));
    private static final Locator SELECT_SAKUKU_PAYMENT = new Locator("SELECT_SAKUKU_PAYMENT", By.xpath("//span[normalize-space(text())='Sakuku']/ancestor::div[contains(@class,'PaymentTile_selected_payment_tile')]//input[@type='radio']"));
    /**
     * Recommended / tile Credit-Debit radio: same pattern as {@link #SELECT_SAKUKU_PAYMENT}. Avoid {@code @value='0'} (order varies).
     * {@code Groups_title} + {@code following::input} often fails (header not ancestor of tile; use tile + label text instead).
     */
    private static final Locator SELECT_CREDIT_DEBIT_CARD_TILE_RADIO = new Locator("SELECT_CREDIT_DEBIT_CARD_TILE_RADIO", By.xpath(
            "//div[contains(@class,'PaymentTile_selected_payment_tile')][.//*[contains(.,'Credit/Debit Card') or contains(.,'Kartu Kredit/Debit') or contains(.,'Kartu Kredit / Kartu Debit') or contains(.,'Credit Card / Debit Card') or contains(.,'CreditCard')]]"
                    + "//input[@type='radio' and @name='selected-payment']"));
    public static final Locator DOWNPAY_BUTTON = new Locator("DOWNPAY_BUTTON", By.xpath("//button[@data-testid='downpay-button']"));
    private static final Locator DOWNPAY_INFO_BANNER = new Locator("DOWNPAY_INFO_BANNER", By.xpath("//div[contains(@class,'DownPayInfoBanner_wrapper')]"));
    private static final Locator DOWNPAY_NEXT_BILL_DUE_DATE = new Locator("DOWNPAY_NEXT_BILL_DUE_DATE", By.xpath("//div[contains(@class,'Step_title_content_container')][.//p[contains(normalize-space(.),'Pay next bill')]]//span[contains(@class,'Step_title') and contains(normalize-space(.),'Before')]"));
    private static final Locator DOWNPAY_CONFIRMATION_DUE_DATE = new Locator("DOWNPAY_CONFIRMATION_DUE_DATE", By.xpath("//div[contains(@class,'DownPayInfoBanner_content_wrapper')]/div/span[count(b)=2]"));
    private static final Locator DOWNPAY_MODAL = new Locator("DOWNPAY_MODAL", By.xpath("//div[contains(@class,'DownPayConfirmation_body')]"));
    private static final Locator DOWNPAY_CURRENT_BILL = new Locator("DOWNPAY_CURRENT_BILL", By.xpath("//li[contains(@class,'Step_step')]//p[contains(@class,'DownPayBillsNudge_downpay_bills')]"));
    private static final Locator DOWNPAY_DUE_DATE = new Locator("DOWNPAY_DUE_DATE", By.xpath("//li[contains(@class,'Step_step')]//span[contains(@class,'Step_title') and contains(text(),'Before')]"));
    private static final Locator NAVIGATE_BACK_CONFIRMATION_POPUP = new Locator("NAVIGATE_BACK_CONFIRMATION_POPUP", new By.ByXPath("//div[@id='modal-root']//div[contains(@class,'Dialog')]//div[contains(@class,'button_container')]//button[contains(@class,'Button_variant_secondary')]"));
    private static final Locator CREDIT_RECOMMENDED_PAYMENT = new Locator("CREDIT_RECOMMENDED_PAYMENT", By.xpath("(//input[@name='selected-payment'])[2]"));
    private static final Locator CVCV = new Locator("CVCV", By.xpath("//input[@id='cvccvv']"));
    private static final Locator PRICE_DETAILS=new Locator("PRICE_DETAILS",By.xpath("(//div[contains(@class,'PriceDetailV4_accordion_item')]//span[contains(@class,'PriceDetailV4_accordion_header')])[1]"));
    private static final Locator INSURANCE_PRICE=new Locator("INSURANCE_PRICE",By.xpath("(//div[span[text()='Add-ons']]//span[contains(@class,'Text_text') and contains(text(),'IDR')])[1]"));
    private static final Locator PAY_IN_FULL_BUTTON = new Locator("PAY_IN_FULL_BUTTON", new By.ByXPath("//button[@data-testid='submit_button']//span[contains(text(),'Pay in full') or contains(text(),'Bayar penuh')]"));
    private static final Locator PAYMENT_SUCCESSFUL_HEADER = new Locator("PAYMENT_SUCCESSFUL_HEADER", new By.ByXPath("//h1[contains(@class,'DesktopSuccessNavigation_desktop_header') and (contains(text(),'Payment Successful') or contains(text(),'Pembayaran Berhasil'))]"));
    private static final Locator TERMS_AND_CONDITION_CHECKBOX = new Locator("TERMS_AND_CONDITION_CHECKBOX", By.xpath("//input[@data-testid='checklist-tnc']"));
    private static final Locator SUBMIT_REFUND_BUTTON = new Locator("SUBMIT_REFUND_BUTTON", By.xpath("//button[@data-testid='continue-submit-refund']"));
    private static final Locator RESCHEDULE_REQUEST_SUBMITTED_TEXT = new Locator("RESCHEDULE_REQUEST_SUBMITTED_TEXT", By.xpath("//h2[contains(@class,'Figure_title') and contains(text(),'Reschedule request submitted') or contains(text(),'Pengajuan reschedule berhasil')]"));
    private static final Locator XENDIT_EMULATOR_SUBMIT_BUTTON = new Locator("XENDIT_EMULATOR_SUBMIT_BUTTON", By.xpath("//input[@id='acssubmit']"));

    private static final Locator COUNTDOWN_TIMER = new Locator("COUNTDOWN_TIMER", new By.ByXPath("//div[contains(@class, 'Countdown_countdown_timer')]"));
    private static final Locator SECONDS_TIMER = new Locator("SECONDS_TIMER", new By.ByXPath("//li[contains(@class, 'Countdown_time')][3]"));
    private static final Locator DUE_DATE_CONFIRMATION = new Locator("DUE_DATE_CONFIRMATION", new By.ByXPath("//span[contains(@class, 'highEmphasis') and contains(@class, 'size_b3')]"));
    private static final Locator DANA_PAY_BUTTON = new Locator("DANA_PAY_BUTTON", By.xpath("//button[contains(text(), 'PAY')]"));
    private static final Locator VIRTUAL_ACCOUNT_NUMBER_SECTION = new Locator("VIRTUAL_ACCOUNT_NUMBER_SECTION", By.xpath("//div[contains(@class, 'AccountInfo_number_box')]//h3"));
    private static final Locator PROMO_CODE = new Locator("PROMO_CODE",AppiumBy.xpath("//div[contains(@class, 'PromoDisplay_content_wrapper__')]//p"));
    private static final Locator LOGIN_NUDGE_POSITIVE_TEXT = new Locator("LOGIN_NUDGE_POSITIVE_TEXT", By.xpath("//span[contains(@class,'Text_variant_positive')]"));
    private static final Locator SEE_ALL_PAYMENT_NUDGE = new Locator("SEE_ALL_PAYMENT_NUDGE", By.xpath("//div[contains(@class,'PaymentMethod_see_all_group') and @data-testid='seeall']"));
    private static final Locator LOGIN_NUDGE_WRAPPER = new Locator("LOGIN_NUDGE_WRAPPER", By.xpath("//div[contains(@class,'PaymentGroup_benefit_nudge_wrapper')]"));
    private static final Locator PAYMENT_GROUP_CLOSE_BUTTON = new Locator("PAYMENT_GROUP_CLOSE_BUTTON", By.xpath("//button[contains(@class, 'close')]"));
    private static final Locator REFUND_RESCHEDULE_INFO = new Locator("REFUND_RESCHEDULE_INFO", By.xpath("//div[contains(@class,'CardWithNudge_header')]//h3[contains(@class,'CardWithNudge_card_title')]"));
    private static final Locator PROMO_CONTAINER = new Locator("PROMO_CONTAINER", new By.ByXPath("//div[@data-testid='btnApplyUnapplyPromo']"));
    private static final Locator BUTTON_UNAPPLY_PROMO = new Locator("BUTTON_UNAPPLY_PROMO", new By.ByXPath("//button[@data-testid='btnRemovePromo']"));
    private static final Locator DOWNPAY_CONFIRM_BUTTON = new Locator("DOWNPAY_CONFIRM_BUTTON", By.xpath("//div[contains(@class,'DownPayConfirmation')]//button[contains(@class,'Button_variant_primary')]"));

    protected static String[] splitPaymentGroupLabels(String paymentGroup) {
        if (paymentGroup == null || paymentGroup.isBlank()) {
            return new String[]{paymentGroup};
        }
        if (paymentGroup.contains("/")) {
            String[] labels = paymentGroup.split("/", 2);
            return new String[]{labels[0].trim(), labels[1].trim()};
        }
        return new String[]{paymentGroup.trim()};
    }

    static final Locator getIndexEligiblePromo(int index) {
        return new Locator("FIRST_ELIGIBLE_PROMO", By.xpath(String.format(
            "//div[contains(@class, 'PromoSuggestionCard_suggestion_card')]["
                + (index + 1) + "]")));
    }
    private static final Locator getIndexNonEligiblePromo(int index) {
        return new Locator("FIRST_NON_ELIGIBLE_PROMO", By.xpath(String.format("//div[contains(@class, 'NonEligiblePromo_list')]//div[contains(@class,'SuggestionItem_suggestion_item')][" +(index+1)+"]")));
    }
    private static final Locator SELECT_PAYMENT_METHOD(String paymentMethod) {
        return new Locator("SELECT_PAYMENT_METHOD",
                By.xpath(String.format("//li[descendant::*[normalize-space()='%s']]", paymentMethod))
        );
    }
    private static final Locator getChangePaymentPromoLocFromSuggestion(String promoCodeName) {
        return new Locator("CHANGE_PAYMENT_PROMO_LOCATOR_FROM_SUGGESTION", By.xpath(String.format("//span[text()='%s']//parent::span//parent::div//following-sibling::button", promoCodeName)));
    }
    private static final Locator getPromoTNCByCode(String promoCodeName) {
        return new Locator("PROMO_TNC_BY_CODE", By.xpath(String.format(
            "//span[text()='%s']/ancestor::div[contains(@class,'SuggestionItem_main_container')]//p[text()='T&C' or text()='S&K']", promoCodeName)));
    }
    private static final Locator getSuggestionPromoByCode(String promoCodeName) {
        return new Locator("SUGGESTION_PROMO_BY_CODE", By.xpath(String.format(
            "//div[contains(@class,'SuggestionItem_suggestion_item')]//span[normalize-space()='%s']", promoCodeName)));
    }
    private static final Locator FIRST_ELIGIBLE_PROMO_APPLY_BUTTON = new Locator("FIRST_ELIGIBLE_PROMO_APPLY_BUTTON", By.xpath(
            "(//div[contains(@class,'PromoSuggestionCard_suggestion_card')])[1]//button"
                + " | (//div[contains(@class,'EligiblePromos_list')]//div[contains(@class,'SuggestionItem_suggestion_item')])[1]//button"));

    public abstract void maximize();
    public abstract void clickPromoSuggestion();
    public abstract void goToPaymentMethodsGroup(String paymentGroup);
    public abstract void clickClosePromoSuggestion();
    public abstract void selectPaymentMethod(String paymentMethod);
    public abstract boolean isPromoInstantUnApplied();
    public abstract void clickOnFirstPaymentMethod();
    public abstract void selectPaymentMethodCCTokenize(String paymentMethod);
    public abstract void changeTenureToAnother();
    public abstract void changeTenure();
    public abstract String getInitialPrice();
    public abstract void clickOnPaymentGroup(String paymentGroup);
    public abstract void clickClosePaymentMethodGroup();

    public void clickClosePaymentGroup() {
        driverTools.waitForElementToAppear(PAYMENT_GROUP_CLOSE_BUTTON).ifPresent(driverTools::click);
    }

    public Map<String, String> getCardFieldValues() {
        Map<String, String> values = new HashMap<>();
        values.put("cardNumber",     driverTools.getValue(CREDIT_CARD_NUMBER));
        values.put("cardMonth",      driverTools.getValue(CREDIT_CARD_MONTH_YEAR));
        values.put("cvv",            driverTools.getValue(CREDIT_CARD_CVV));
        values.put("cardHolderName", driverTools.getValue(CREDIT_CARD_HOLDER_NAME));
        
        return values;
    }

    public boolean isCardFieldsEmpty() {
        return getCardFieldValues().values().stream()
                .allMatch(v -> v == null || v.trim().isEmpty());
    }
    
    /**
     * Consumer web payment lives on the main tiket host with locale prefix (not payment.* /next/v4).
     */
    private static String mainSitePaymentUrl(String origin, String orderId, String orderHash) {
        return origin + "/en-sg/payment?order_id=" + orderId + "&order_hash=" + orderHash;
    }

    private void navigateToPaymentPage(String orderId, String orderHash, String gkUrl, String preprodUrl, String prodUrl) {
        Deeplink deepLink = new Deeplink("ORDER_DETAIL_PAGE_"+orderId, gkUrl, preprodUrl, prodUrl);

        switch (Configs.ENVIRONMENT){
            case PROD:
                driverTools.get(deepLink.prodDeeplink());
                break;
            case PREPROD:
                driverTools.get(deepLink.preprodDeeplink());
                break;
            case GK:
                driverTools.get(deepLink.gkDeeplink());
                break;
        }
    }

    /** Navigates to consumer web payment on the main tiket host (locale-prefixed path). */
    public void goToPaymentPage(String orderId, String orderHash) {
        navigateToPaymentPage(orderId, orderHash,
                mainSitePaymentUrl("https://gatotkaca.tiket.com", orderId, orderHash),
                mainSitePaymentUrl("https://preprod.tiket.com", orderId, orderHash),
                mainSitePaymentUrl("https://www.tiket.com", orderId, orderHash));
    }

    /** Same destination as {@link #goToPaymentPage}; kept for callers that explicitly use the direct main-site URL. */
    public void goToPaymentPageDirect(String orderId, String orderHash) {
        goToPaymentPage(orderId, orderHash);
    }

    public String getBookingDate(){
        WebElement date = driverTools.waitForElementToAppear(PAYMENT_PAGE_CHECKIN).orElseThrow();
        return driverTools.getText(date);
    }

    public String getBookingNights(){
        WebElement night = driverTools.waitForElementToAppear(PAYMENT_PAGE_NIGHT).orElseThrow();
        return driverTools.getText(night);
    }

    public boolean isPaymentConfirmationPageLoaded(){
        driverTools.waitForElementToAppear(PAYMENT_INSTRUCTION_TEXT,15);
        return driver.getCurrentUrl().contains("confirm");
    }

    public boolean isPaymentPageLoaded() {
        driverTools.waitForElementToAppear(CONTINUE_PAYMENT_CONTAINER);
        driverTools.waitForElementToAppear(FINAL_PRICE);
        return (driverTools.getTitle().contains("Continue") || driverTools.getTitle().contains("Lanjutkan")) &&
                driverTools.waitForElementToAppear(CONTINUE_PAYMENT_CONTAINER).isPresent();
    }

    public boolean isOnPaymentSuccessPage() {
        String title = driverTools.getTitle();
        return title.contains("Payment Successful") || title.contains("Pembayaran Berhasil");
    }

    public boolean isOctoClicksPageLoaded(){
        driverTools.waitForElementToAppear(FORBIDDEN_PAGE);
        return driver.getCurrentUrl().contains("pg-uat.e2pay.co.id");
    }

    public boolean isKredivoPageLoaded(){
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(d -> driverTools.getCurrentURL().toLowerCase().contains("kredivo.com"));
        } catch (TimeoutException e) {
            logger.softWarning("Kredivo URL not loaded, current URL: " + driverTools.getCurrentURL());
            return false;
        }
        logger.log("Kredivo URL: " + driverTools.getCurrentURL());
        Optional<List<WebElement>> kredivoElements = driverTools.waitForElementsToAppear(KREDIVO_PAGE, 15);
        return kredivoElements.map(elements -> elements.stream().anyMatch(WebElement::isDisplayed)).orElse(false);
    }

    public String getFinalPrice() {
        driverTools.waitForElementToAppear(FINAL_PRICE).orElseThrow();
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .pollingEvery(Duration.ofMillis(500))
                .until(d -> {
                    String text = driverTools.getText(FINAL_PRICE).orElse("");
                    if (text.isBlank() || text.toLowerCase().contains("free") || text.toLowerCase().contains("gratis")) {
                        return false;
                    }
                    try {
                        return CoreFunctions.convertPriceSafely(text) > 0;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                });
        String finalPriceText = driverTools.getText(FINAL_PRICE).orElseThrow();
        String finalPriceFormatted = String.valueOf(CoreFunctions.convertPriceSafely(finalPriceText));
        logger.log("finalPrice " + finalPriceFormatted);
        return finalPriceFormatted;
    }

    public String getOrderId(){
        driverTools.waitForElementToPresence(CONTINUE_TO_PAYMENT_BUTTON, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
        WebElement orderIdTextElement = driverTools.waitForElementToAppear(ORDER_ID_CONTAINER,30).orElseThrow();
        String orderIdText = driverTools.getText(orderIdTextElement);
        int colonIndex = orderIdText.indexOf(":");
        String orderId = orderIdText.substring(colonIndex + 1).trim();
        logger.log("orderId "+orderId);
        return orderId;
    }

    public String getAccessToken() {
        return driverTools.getCookieValue("session_access_token");
    }

    public void clickOnSelectPaymentMethod() {
        driverTools.waitForElementToAppear(SEE_ALL_PAYMENT_METHOD).ifPresent(driverTools::click);
    }

    public boolean isSeeAllPaymentMethodDisplayed() {
        return driverTools.waitForElementToAppear(SEE_ALL_PAYMENT_METHOD, 5).isPresent()
                && driverTools.isElementDisplayed(SEE_ALL_PAYMENT_METHOD);
    }

    public abstract boolean isPaymentSuccessfulVisible();

    public String queryString(String orderID){
        logger.log("orderID "+orderID);
        return "{referenceId:\"" + orderID + "\"}";
    }

    public Map<String, String> getPaymentTransactionDetails(String configType, String dataBase, String collectionName, String queryString) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB(configType, dataBase, collectionName, queryString);
        for (Document document : result) {
            Map<String, String> paymentInfo = new HashMap<>();
            Integer uniqueCode = document.getInteger("uniqueCode");
            if (uniqueCode != null) {
                String uniqueCodeString = String.valueOf(Math.abs(uniqueCode));
                paymentInfo.put("uniqueCode", uniqueCodeString);
            }
            paymentInfo.put("paymentStatus", document.getString("paymentStatus"));
            paymentInfo.put("paymentAmount", String.valueOf(document.getDouble("paymentAmount")));
            Document creditCardFields = document.get("creditCardFields", Document.class);
            paymentInfo.put("cardHolderNumber",
                    creditCardFields != null ? creditCardFields.getString("cardHolderNumber") : null);

            return paymentInfo;
        }
        return new HashMap<>();
    }

    public void logConvenienceFeeIfPresent() {
        if (isConvenienceFeeDisplayed()) {
            logger.log("Convenience fee is available");
        } else {
            logger.log("Convenience fee is not available - optional check skipped");
        }
    }

    private boolean isConvenienceFeeDisplayed() {
        return driverTools.waitForElementToAppear(CONVENIENCE_FEE, 5).isPresent();
    }

    public String getSelectedPayment(){
        WebElement selectedPaymentTextElement = driverTools.waitForElementToAppear(SELECTED_PAYMENT).orElseThrow();
        String selectedPaymentText = driverTools.getText(selectedPaymentTextElement);
        logger.log("selectedPaymentText "+selectedPaymentText);
        return selectedPaymentText;
    }

    public void clickOnPaymentButton(){
        driverTools.waitForElementToAppear(PAYMENT_BUTTON);
        driverTools.waitForElementToBeClickable(PAYMENT_BUTTON);
        driverTools.waitForElementToAppear(PAYMENT_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(CONTINUE_TO_PAYMENT, 3);
        if (driverTools.isElementDisplayed(CONTINUE_TO_PAYMENT)) {
            driverTools.click(CONTINUE_TO_PAYMENT);
        }
    }

    public void inputPromoInInputField(String value){
        driverTools.waitForElementToAppear(PROMO_INPUT_FIELD, 10);
        driverTools.click(PROMO_INPUT_FIELD);
        driverTools.sendKeys(PROMO_INPUT_FIELD, value);
    }

    public void promoInputOrScroll(String value){
        driverTools.waitForElementToAppear(PROMO_INPUT_FIELD, 10);
        if(value == null || value.isBlank()){
            driverTools.swipeToElement(LOG_IN_BUTTON, Swipe.UP,7);
            driverTools.clickWithJavascriptExecutor(LOG_IN_BUTTON);
        }else{
            driverTools.click(PROMO_INPUT_FIELD);
            driverTools.sendKeys(PROMO_INPUT_FIELD, value);
            driverTools.waitForElementToBeClickable(APPLY_BUTTON);
            driverTools.clickWithJavascriptExecutor(APPLY_BUTTON);
        }
    }

    public void clickApplyButton() {
        driverTools.waitForElementToBeClickable(APPLY_BUTTON);
        driverTools.clickWithJavascriptExecutor(APPLY_BUTTON);
    }

    public void applyPromoWithSpecificDiscountCode(String value) {
        driverTools.click(PROMO_CONTAINER);
        inputPromoInInputField(value);
        clickApplyButton();
    }

    public void unapplyPromoFromPromoSheet() {
        driverTools.click(BUTTON_UNAPPLY_PROMO);
    }

    public boolean isPromoAppliedInPromoSheet() {
        return driverTools.waitForElementToPresence(BUTTON_UNAPPLY_PROMO).isPresent();
    }

    public void clickLogInNow() {
        driverTools.waitForElementToAppear(LOG_IN_NOW);
        driverTools.clickWithJavascriptExecutor(LOG_IN_NOW);
    }

    public boolean promoIsApplied(){
        driverTools.waitForElementToPresence(PROMO_APPLIED);
        return driverTools.isElementDisplayed(PROMO_APPLIED);
    }

    public void applyAnyPromo(String promoCode) {
        clickPromoSuggestion();
        Assertion.assertThat("Eligible promo list should be displayed on payment page",
                verifyEligiblePromo(), is(true));

        if (promoCode != null && !promoCode.isBlank()
                && !promoCode.equalsIgnoreCase("any")
                && !promoCode.equalsIgnoreCase("first")) {
            inputPromoInInputField(promoCode);
            clickApplyButton();
        } else {
            driverTools.scrollIntoWebView(getIndexEligiblePromo(0));
            WebElement applyButton = driverTools.waitForElementToBeClickable(FIRST_ELIGIBLE_PROMO_APPLY_BUTTON, 10)
                    .orElseThrow(() -> new IllegalStateException("No eligible promo apply button found"));
            driverTools.clickWithJavascriptExecutor(applyButton);
        }

        Assertion.assertThat("Promo should be applied on payment page", promoIsApplied(), is(true));
    }

    public boolean verifyErrorMsg(String errorMsgEN, String errorMsgID) {
        boolean isErrorMessageDisplayed = driverTools.waitForElementToAppear(ERROR_MSG_NOT_APPLICABLE_PROMO).isPresent();
        if (isErrorMessageDisplayed) {
            String errorMsg = String.valueOf(driverTools.getText(ERROR_MSG_NOT_APPLICABLE_PROMO));
            String actual = CoreFunctions.normalizeNonBreakingSpaceText(errorMsg).toLowerCase(Locale.ROOT);
            String expectedEN = CoreFunctions.normalizeNonBreakingSpaceText(errorMsgEN).toLowerCase(Locale.ROOT);
            String expectedID = CoreFunctions.normalizeNonBreakingSpaceText(errorMsgID).toLowerCase(Locale.ROOT);
            logger.log("verifyErrorMsg -> actual=[" + errorMsg + "] (len " + (errorMsg == null ? 0 : errorMsg.length())
                    + "), expectedEN=[" + errorMsgEN + "], expectedID=[" + errorMsgID + "]");
            logger.log("verifyErrorMsg (normalized) -> actual=[" + actual + "], expectedEN=[" + expectedEN
                    + "], expectedID=[" + expectedID + "]");
            return (!expectedEN.isEmpty() && actual.contains(expectedEN))
                    || (!expectedID.isEmpty() && actual.contains(expectedID));
        } else {
            logger.log("No error message is displayed");
            return false;
        }
    }

    public void openPriceSummary(){
        driverTools.waitForElementToAppear(PRICE_SUMMARY_CONTAINER).ifPresent(driverTools::click);
    }

    public void closeDrawer(){
        driverTools.waitForElementToAppear(CLOSE_DRAWER).ifPresent(driverTools::click);
    }

    public abstract Locator getTransactionFeeLocator();

    public boolean isTransactionFeesDisplayed(){
        driverTools.waitForElementToAppear(getTransactionFeeLocator());
        return driverTools.isElementDisplayed(getTransactionFeeLocator());
    }

    public boolean isUniqueCodeTextDisplayed(){
        driverTools.waitForElementToAppear(UNIQUE_CODE_TEXT);
        return driverTools.isElementDisplayed(UNIQUE_CODE_TEXT);
    }


    public String getUniqueCode(){
        WebElement uniqueCodeElement = driverTools.waitForElementToAppear(UNIQUE_CODE).orElseThrow();
        String uniqueCodeText = driverTools.getText(uniqueCodeElement);
        String uniqueCode = uniqueCodeText.replaceAll("[^\\d]", "");
        logger.log("uniqueCode "+uniqueCode);
        return uniqueCode;
    }

    public void inputCardNumber(String cardNumber) {
        driverTools.waitForElementToAppear(CREDIT_CARD_NUMBER, 5);
        driverTools.clear(CREDIT_CARD_NUMBER);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(CREDIT_CARD_NUMBER, cardNumber);
        
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(d -> {
                String currentValue = driverTools.getValue(CREDIT_CARD_NUMBER);
                String normalized = currentValue.replaceAll("[\\s-]", "");
                String expectedNormalized = cardNumber.replaceAll("[\\s-]", "");
                return normalized.equals(expectedNormalized);
            });
    }

    public void inputCardMonth(String cardMonth){
        logger.log("cardMonth " + cardMonth);
        driverTools.waitForElementToAppear(CREDIT_CARD_MONTH_YEAR, 10).orElseThrow();
        driverTools.click(CREDIT_CARD_MONTH_YEAR);
        driverTools.clear(CREDIT_CARD_MONTH_YEAR);

        String digits = cardMonth == null ? "" : cardMonth.replaceAll("[^0-9]", "");
        String valueToSet;
        if (digits.length() == 4) {
            valueToSet = digits.substring(0, 2) + "/" + digits.substring(2);
        } else {
            valueToSet = cardMonth;
        }

        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(CREDIT_CARD_MONTH_YEAR, valueToSet);

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> {
                    String currentValue = driverTools.getValue(CREDIT_CARD_MONTH_YEAR);
                    String currentDigits = currentValue == null ? "" : currentValue.replaceAll("[^0-9]", "");
                    return !digits.isEmpty() && currentDigits.equals(digits);
                });
    }
    public void inputCardCvv(String cvv){
        logger.log("cvv " + cvv);
        driverTools.waitForElementToAppear(CREDIT_CARD_CVV, 10).orElseThrow();
        driverTools.click(CREDIT_CARD_CVV);
        driverTools.clear(CREDIT_CARD_CVV);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(CREDIT_CARD_CVV, cvv);

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> {
                    String currentValue = driverTools.getValue(CREDIT_CARD_CVV);
                    return currentValue != null && currentValue.equals(cvv);
                });
    }

    public void inputCardHolderName(String cardHolderName){
        logger.log("cardHolderName " + cardHolderName);
        driverTools.waitForElementToAppear(CREDIT_CARD_HOLDER_NAME, 10).orElseThrow();
        driverTools.click(CREDIT_CARD_HOLDER_NAME);
        driverTools.clear(CREDIT_CARD_HOLDER_NAME);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(CREDIT_CARD_HOLDER_NAME, cardHolderName);

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> {
                    String currentValue = driverTools.getValue(CREDIT_CARD_HOLDER_NAME);
                    return currentValue != null && currentValue.equals(cardHolderName);
                });
    }

    public boolean verifyNonEligiblePromo() {
        driverTools.waitForElementToAppear(getIndexNonEligiblePromo(0));
        return driverTools.isElementDisplayed(getIndexNonEligiblePromo(0));
    }

    public boolean verifyEligiblePromo() {
        driverTools.waitForElementToAppear(getIndexEligiblePromo(0));
        return driverTools.isElementDisplayed(getIndexEligiblePromo(0));
    }

    public void clickChangePaymentMethodButton() {
        driverTools.waitForElementToAppear(CHANGE_PAYMENT_METHOD_BUTTON);
        driverTools.click(CHANGE_PAYMENT_METHOD_BUTTON);
    }

    public boolean isPromoInstantApplied(String expectedPromoAppliedText){
        boolean textPresent = driverTools.waitForTextToBePresent(PROMO_INSTANT_APPLIED_TEXT, expectedPromoAppliedText);
        return textPresent;
    }

    public boolean isPromoDisplayVisible() {
        driverTools.waitForElementToAppear(PROMO_DISPLAY_CONTAINER);
        driverTools.waitForElementToAppear(PROMO_DISPLAY_CTA);
        driverTools.waitForElementToAppear(UNAPPLY_BUTTON);
        return driverTools.isElementDisplayed(PROMO_DISPLAY_CONTAINER)
                && driverTools.isElementDisplayed(PROMO_DISPLAY_CTA)
                && driverTools.isElementDisplayed(UNAPPLY_BUTTON);
    }

    public String getPromoSavedAmount() {
        WebElement savedAmountElement = driverTools.waitForElementToAppear(PROMO_INSTANT_APPLIED_TEXT).orElseThrow();
        String savedAmountText = driverTools.getText(savedAmountElement);
        logger.log("Promo saved amount text: " + savedAmountText);
        return String.valueOf(CoreFunctions.convertPriceSafely(savedAmountText));
    }

    public int getAppliedPromoPointsFromBanner() {
        isPromoDisplayVisible();
        String bannerText = driverTools.getText(PROMO_INSTANT_APPLIED_TEXT).orElseThrow();
        int points = (int) CoreFunctions.convertPriceSafely(bannerText);
        logger.log("Applied promo points from banner: " + points + " (text: " + bannerText + ")");
        return points;
    }

    public String getAppliedPromoCodeFromBanner() {
        isPromoDisplayVisible();
        String promoCode = driverTools.getText(PROMO_INSTANT_APPLIED_TEXT_CODE).orElseThrow();
        logger.log("Applied promo code from banner: " + promoCode);
        return promoCode;
    }

    public String getSlashPrice() {
        WebElement slashPriceElement = driverTools.waitForElementToAppear(SLASH_PRICE).orElseThrow();
        String slashPriceText = driverTools.getText(slashPriceElement);
        logger.log("Slash price text: " + slashPriceText);
        return String.valueOf(CoreFunctions.convertPriceSafely(slashPriceText));
    }

    public String getPriceSummaryDiscountAmount() {
        WebElement discountElement = driverTools.waitForElementToAppear(PRICE_SUMMARY_DISCOUNT).orElseThrow();
        String discountText = driverTools.getText(discountElement);
        logger.log("Price summary discount text: " + discountText);
        return String.valueOf(CoreFunctions.convertPriceSafely(discountText));
    }

    public void verifyPromoDiscountPriceCalculation(String priceBeforePromo) {
        double beforePromo = CoreFunctions.convertPriceSafely(priceBeforePromo);
        double promoDiscount = CoreFunctions.convertPriceSafely(getPromoSavedAmount());
        double slashPrice = CoreFunctions.convertPriceSafely(getSlashPrice());
        double finalPrice = CoreFunctions.convertPriceSafely(getFinalPrice());
        double priceSummaryDiscount = CoreFunctions.convertPriceSafely(getPriceSummaryDiscountAmount());

        logger.log(String.format(
                "Promo price check: before=%s, slash=%s, final=%s, promoDiscount=%s, summaryDiscount=%s",
                beforePromo, slashPrice, finalPrice, promoDiscount, priceSummaryDiscount));

        Assertion.assertThat("Slash price should match price before promo", slashPrice, is(beforePromo));
        Assertion.assertThat("Price before promo minus final price should equal promo discount",
                beforePromo - finalPrice, is(promoDiscount));
        Assertion.assertThat("Price summary discount should match promo discount",
                priceSummaryDiscount, is(promoDiscount));
    }

    public boolean isPromoVisibleInSuggestion(String promoCodeName) {
        boolean visible = driverTools.waitForElementToAppear(getSuggestionPromoByCode(promoCodeName), 10).isPresent();
        logger.log("Promo '" + promoCodeName + "' visible in suggestion: " + visible);
        return visible;
    }

    public boolean verifyBestSuitedPromo() {
        driverTools.waitForElementToAppear(BEST_SUITED_PROMO);
        return driverTools.isElementDisplayed(BEST_SUITED_PROMO);
    }

    public void clickPayWithTiketPoints() {
        driverTools.element(TIKET_POINTS_RADIO, 5).orElseThrow();
        driverTools.clickWithJavascriptExecutor(TIKET_POINTS_RADIO);
    }

    public void selectRefund(){
        boolean isRefundDeposit = driverTools.waitForElementToAppear(REFUND_DEPOSIT_PAYMENT_METHOD).isPresent();
        if(isRefundDeposit){
            driverTools.scrollIntoWebView(REFUND_DEPOSIT_PAYMENT_METHOD);
            driverTools.clickWithJavascriptExecutor(REFUND_DEPOSIT_PAYMENT_METHOD);
        }else{
            clickOnSelectPaymentMethod();
            clickOnSeeAll();
            selectPaymentMethod("Sakuku");
        }
    }

    public void clickChangePayment(String promoCodeName) {
        Locator promoLocator = getChangePaymentPromoLocFromSuggestion(promoCodeName);
        driverTools.scrollIntoWebView(promoLocator);
        driverTools.waitForElementToAppear(promoLocator);
        driverTools.waitForElementToBeClickable(promoLocator);
        driverTools.clickWithJavascriptExecutor(promoLocator);
    }
    public void clickChangePayment() {
        driverTools.waitForElementToAppear(CHANGE_PAYMENT_BUTTON);
        driverTools.waitForElementToBeClickable(CHANGE_PAYMENT_BUTTON);
        driverTools.click(CHANGE_PAYMENT_BUTTON);
    }

    public void clickChangePaymentMethod() {
        driverTools.waitForElementToAppear(CHANGE_PAYMENT_METHOD_BUTTON);
        driverTools.waitForElementToBeClickable(CHANGE_PAYMENT_METHOD_BUTTON);
        driverTools.click(CHANGE_PAYMENT_METHOD_BUTTON);
        driverTools.waitForElementToDisappear(CHANGE_PAYMENT_METHOD_BUTTON);
        driverTools.waitForElementToDisappear(PROMO_INPUT_FIELD);
    }
    public boolean isPaymentPagepresent(){
        driverTools.waitForElementToPresence(PAYMENT_PAGE_CHECK,40);
        if(!driverTools.isElementDisplayed(PAYMENT_PAGE_CHECK)){
            FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
            flightBookingFormPageWeb.clickGotItInBaggagePopup();
        }
        return driverTools.waitForElementToAppear(PAYMENT_PAGE_CHECK).isPresent();
    }

    public void clickCheckTNC() {
        driverTools.waitForElementToAppear(CHECK_TNC);
        driverTools.click(CHECK_TNC);
    }

    public void scrollToPromoAndClickTNC(String promoCodeName) {
        Locator promoTNC = getPromoTNCByCode(promoCodeName);
        driverTools.scrollIntoWebView(promoTNC);
        driverTools.waitForElementToAppear(promoTNC);
        driverTools.waitForElementToBeClickable(promoTNC);
        driverTools.clickWithJavascriptExecutor(promoTNC);
    }

    public void clickTNCApplyButton() {
        driverTools.waitForElementToAppear(TNC_APPLY_BUTTON);
        driverTools.waitForElementToBeClickable(TNC_APPLY_BUTTON);
        driverTools.click(TNC_APPLY_BUTTON);
    }
    public boolean isOVODisabled() {
        WebElement tiketOvoElement = driverTools.waitForElementToAppear(OVO_ELEMENT).orElseThrow();
        boolean isDisabled = tiketOvoElement.getAttribute("class").contains("disabled");
        logger.log("Tiket OVO is disabled: " + isDisabled);
        return isDisabled;
    }

    public boolean isPaymentMethodVisible(String paymentMethod){
        Optional<WebElement> paymentMethodLocator = driverTools.waitForElementToAppear(SELECT_PAYMENT_METHOD(paymentMethod),10);
        return paymentMethodLocator.isPresent();
    }
    
    public void clickOnSubmitButton(){
        driverTools.waitForElementToAppear(SUBMIT_BUTTON_ON_2C2P_PAGE).ifPresent(driverTools::click);
    }

    public void clickOnCompleteButton(){
        driverTools.waitForElementToAppear(COMPLETE_BUTTON, 20).ifPresent(driverTools::click);
        driverTools.refreshWebpage();
    }

    public void selectCreditCard(){
        WebElement cardElement = driverTools.waitForElementToAppear(SELECT_CREDIT_CARD).orElseThrow();
        driverTools.clickWithJavascriptExecutor(cardElement);
    }


    public void handle2C2PCallbackURL(int limitSecond) {
        while (limitSecond-- > 0) {
            String currentUrl = driverTools.getCurrentURL();
            if (currentUrl.contains("gatotkaca") && ENVIRONMENT.equals(Environment.PREPROD)) {
                driverTools.get(currentUrl.replace("-gatotkaca", ".preprod"));
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.log(e.getLocalizedMessage());
            }
        }
    }

    public void handle3DSPage(int limitSecond) {
        String[] pgNames = {"2c2p", "xendit", "umundus", "airwallex"};
        String currentPg = "";
        while (limitSecond-- > 0) {
            String currentUrl = driverTools.getCurrentURL();
            for (String string : pgNames) {
                if (currentUrl.contains(string)) {
                    currentPg = string;
                    break;
                }
            }

            if (!currentPg.isEmpty()) break;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.log(e.getLocalizedMessage());
            }
        }

        switch (currentPg) {
            case "2c2p":
                driverTools.waitForElementToPresence(OTP_FIELD_ON_2C2P_PAGE, 10);
                driverTools.sendKeysWithJsExecutor(OTP_FIELD_ON_2C2P_PAGE, "123456");
                driverTools.click(SUBMIT_BUTTON_ON_2C2P_PAGE);
                handle2C2PCallbackURL(10); // just handling for preprod
                break;
            case "xendit":
                driverTools.waitForElementToAppear(XENDIT_EMULATOR_SUBMIT_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
                break;
            case "airwallex":
                WebElement iframe = driverTools.waitForElementToAppear(AIRWALLEX_IFRAME).orElseThrow();
                driverTools.switchToIframe(iframe);
                driverTools.waitForElementToPresence(OTP_FIELD_XENDIT, 10);
                driverTools.sendKeysKeyboard(OTP_FIELD_XENDIT, "1234");
                driverTools.waitForElementToPresence(SUBMIT_BUTTON_XENDIT, 10);
                driverTools.clickWithJavascriptExecutor(SUBMIT_BUTTON_XENDIT);
                driverTools.switchBackFromIframe();
                break;
            case "umundus": //BCA PG
                String otp = driverTools.getText(driverTools.waitForElementToAppear(OTP_VALUE_BCA, 10).orElseThrow());
                driverTools.sendKeysWithJsExecutor(OTP_FIELD_BCA, otp);
                driverTools.click(SUBMIT_BUTTON_BCA);
                break;
            default:
                if (!driverTools.getCurrentURL().contains("tiket.com") && !driverTools.getCurrentURL().contains("complete")) {
                    throw new RuntimeException("PG not yet handled for: "+currentPg);
                } else {
                    logger.log("3DS page skipped. direct to success or failed payment");
                }
        }
    }

    public abstract String getTextOnLoginNudge();

    public boolean isLoginNudgePositiveTextVisible() {
        return driverTools.waitForElementToAppear(LOGIN_NUDGE_POSITIVE_TEXT, 5).isPresent();
    }

    public void clickSeeAllPaymentMethodNudge() {
        WebElement seeAllButton = driverTools.waitForElementToBeClickable(SEE_ALL_PAYMENT_NUDGE, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(seeAllButton);
    }

    public boolean isLoginNudgeWrapperVisible() {
        return driverTools.waitForElementToAppear(LOGIN_NUDGE_WRAPPER, 10).isPresent();
    }

    public boolean isPriceCurrencyInIDR() {
        WebElement paymentPagePriceElement = driverTools.waitForElementToAppear(FINAL_PRICE_IDR).orElseThrow();
        String paymentPagePriceValue = driverTools.getText(paymentPagePriceElement);
        logger.log("Payment page final price: " + paymentPagePriceValue);
        String paymentPagePriceCurrency = CoreFunctions.getSectionOfString(paymentPagePriceValue, " ", 0).trim();
        logger.log("Payment page price currency: " + paymentPagePriceCurrency);
        return paymentPagePriceCurrency.equalsIgnoreCase("IDR");
    }

    public String getCurrency() {
        WebElement finalPriceElement = driverTools.waitForElementToAppear(FINAL_PRICE).orElseThrow();
        String finalPriceText = driverTools.getText(finalPriceElement);
        logger.log("Payment page final price with currency: " + finalPriceText);
        String currency = CoreFunctions.getSectionOfString(finalPriceText, " ", 0).trim();
        logger.log("Payment page currency: " + currency);
        return currency;
    }

    public void clickUnApplyButton() {
        driverTools.waitForElementToAppear(UNAPPLY_BUTTON).ifPresent(driverTools::click);
    }

    public void clickRemovePromo() {
        driverTools.waitForElementToAppear(REMOVE_PROMO_BUTTON).ifPresent(driverTools::click);
    }

    public boolean isUserOnPaymentConfirmationPage() {
        driverTools.waitForElementToAppear(CHECK_PAYMENT_STATUS_BUTTON).orElseThrow();
        driverTools.waitForElementToAppear(SEE_ORDER_LIST_BUTTON).orElseThrow();

        boolean isCheckPaymentStatusButtonDisplayed = driverTools.isElementDisplayed(CHECK_PAYMENT_STATUS_BUTTON);
        boolean isSeeOrderListButtonDisplayed = driverTools.isElementDisplayed(SEE_ORDER_LIST_BUTTON);

        return isCheckPaymentStatusButtonDisplayed && isSeeOrderListButtonDisplayed;
    }

    public boolean isSeeOrderListButtonDisplayed() {
        driverTools.waitForElementToAppear(SEE_ORDER_LIST_BUTTON, 20);
        return driverTools.isElementDisplayed(SEE_ORDER_LIST_BUTTON);
    }

    public boolean isVaNumberDigitsOnly() {
        WebElement vaNumberElement = driverTools.waitForElementToAppear(VA_NUMBER).orElseThrow();
        String vaNumberText = driverTools.getText(vaNumberElement).trim();
        String vaNumberWithoutSpaces = vaNumberText.replaceAll("\\s", "");

        logger.log("VA Number: " + vaNumberWithoutSpaces);

        boolean isVaNumberOnlyContainsDigits = Pattern.matches("\\d+", vaNumberWithoutSpaces);
        logger.log("Is VA number only contains digits: " + isVaNumberOnlyContainsDigits);

        return isVaNumberOnlyContainsDigits;
    }
    public void clickOnSeeAll(){
        driverTools.waitForElementToAppear(SEE_ALL_METHOD);
        driverTools.clickWithJavascriptExecutor(SEE_ALL_METHOD);
    }
    public void clickSeeOrderListButton() {
        WebElement seeOrderListButton = driverTools.waitForElementToAppear(SEE_ORDER_LIST_BUTTON, 30).orElseThrow();
        driverTools.scrollIntoWebView(seeOrderListButton);
        seeOrderListButton = driverTools.waitForElementToBeClickable(SEE_ORDER_LIST_BUTTON, 30).orElseThrow();
        try {
            seeOrderListButton.click();
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(seeOrderListButton);
        }
        driverTools.waitForElementToDisappear(SEE_ORDER_LIST_BUTTON, 10);
        HotelPendingRetrySummaryNotifier.afterNavigateToYourOrders(session, apiTools, logger);
    }
    public void clickSeeOrderListButtonAfterSuccess() {
        WebElement seeOrderListButton = driverTools.waitForElementToBeClickable(SEE_ORDER_LIST_BUTTON_AFTER_SUCCESS, 30).orElseThrow();
        try {
            seeOrderListButton.click();
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(seeOrderListButton);
        }
        driverTools.waitForElementToDisappear(SEE_ORDER_LIST_BUTTON_AFTER_SUCCESS, 10);
        HotelPendingRetrySummaryNotifier.afterNavigateToYourOrders(session, apiTools, logger);
    }
    public void clickOnTiketPoints() {
        WebElement tiketPointButton = driverTools.waitForElementToAppear(TIKET_POINTS).orElseThrow();
        driverTools.clickWithJavascriptExecutor(tiketPointButton);
    }

    public void clickDiffPaymentMethod(){
        driverTools.waitForElementToAppear(BRI_VIRTUAL_ACCOUNT);
        driverTools.clickWithJavascriptExecutor(BRI_VIRTUAL_ACCOUNT);
    }

    public void clickContinueWithoutPromo() {
        driverTools.waitForElementToAppear(CONTINUE_WITHOUT_PROMO).ifPresent(driverTools::click);
    }

    public void clickContinueWithPoints(){
        driverTools.waitForElementToAppear(CONTINUE_WITH_POINTS).ifPresent(driverTools::click);
    }

    public boolean isValidationCorrect(String expectedValidationMsg) {
        driverTools.waitForElementToAppear(PROMO_VALIDATION_MESSAGE);
        String actualValidationText = driverTools.getText(PROMO_VALIDATION_MESSAGE).get();
        logger.log("actual validation text -> " + actualValidationText);
        logger.log("expected validation text -> " + expectedValidationMsg);
        return actualValidationText.equals(expectedValidationMsg);
    }

    public boolean isOrderFree(){
        driverTools.waitForElementToAppear(ORDER_FREE);
        return driverTools.isElementPresent(ORDER_FREE);
    }

    public void openOrderDetails(){
        driverTools.waitForElementToAppear(ORDER_DETAILS_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public void closeOrderDetails(){
        driverTools.waitForElementToAppear(ORDER_DETAILS_CLOSE_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public boolean isOrderSummaryLogoVisible() {
        driverTools.waitForElementToAppear(ORDER_SUMMARY_CONTAINER);
        return driverTools.isElementDisplayed(ORDER_SUMMARY_LOGO);
    }

    public String getOrderSummaryProduct() {
        driverTools.waitForElementToAppear(ORDER_SUMMARY_CONTAINER);
        WebElement productNameElement = driverTools.waitForElementToAppear(ORDER_SUMMARY_PRODUCT_NAME).orElseThrow();
        return driverTools.getText(productNameElement);
    }

    public void verifyOrderSummaryDetails() {
        driverTools.waitForElementToAppear(ORDER_SUMMARY_CONTAINER);

        Assertion.assertThat("Order summary logo should be visible", isOrderSummaryLogoVisible(), is(true));

        String productText = getOrderSummaryProduct();
        Assertion.assertThat("Order summary product name should not be empty",
            productText != null && !productText.isEmpty(), is(true));

        openOrderDetails();
        driverTools.waitForElementToAppear(ORDER_SUMMARY_TITLE);

        driverTools.waitForElementToAppear(ORDER_ID_ABOVE_ORDER_DETAILS).orElseThrow();

        closeOrderDetails();

        Assertion.assertThat("Order details popup/bottomsheet should be closed",
            driverTools.waitForElementToDisappear(ORDER_SUMMARY_TITLE, 5), is(true));
    }

    private Locator orderSummaryFlightCard(int index) {
        return new Locator("ORDER_SUMMARY_FLIGHT_CARD_" + index, By.cssSelector("[data-testid='btnOrderSummaryItem-" + index + "']"));
    }

    public void ensureOrderSummaryListOpen() {
        if (driverTools.isElementDisplayed(ORDER_SUMMARY_LIST_MODAL)) {
            return;
        }
        openOrderDetails();
        driverTools.waitForElementToAppear(ORDER_SUMMARY_LIST_MODAL, 15).orElseThrow();
    }

    public void closeOrderSummaryList() {
        driverTools.waitForElementToAppear(ORDER_SUMMARY_LIST_MODAL_CLOSE, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public int getOrderSummaryFlightCardCount() {
        ensureOrderSummaryListOpen();
        driverTools.waitForElementToAppear(ORDER_SUMMARY_FLIGHT_CARDS, 15);
        return driverTools.elements(ORDER_SUMMARY_FLIGHT_CARDS).orElse(emptyList()).size();
    }

    public String getOrderSummaryFlightCountLabel() {
        driverTools.waitForElementToAppear(ORDER_SUMMARY_FLIGHT_COUNT_LABEL, 15);
        return driverTools.getText(ORDER_SUMMARY_FLIGHT_COUNT_LABEL).orElse("");
    }

    public String getOrderSummaryIconSource() {
        driverTools.waitForElementToAppear(ORDER_SUMMARY_LOGO, 15);
        return driverTools.getValueAttribute(ORDER_SUMMARY_LOGO, "src");
    }

    public void openFlightCard(int index) {
        ensureOrderSummaryListOpen();
        driverTools.waitForElementToAppear(orderSummaryFlightCard(index), 15).orElseThrow();
        driverTools.clickWithJavascriptExecutor(orderSummaryFlightCard(index));
        driverTools.waitForElementToAppear(FLIGHT_DETAIL_MODAL, 15).orElseThrow();
    }

    public String getFlightDetailTitle() {
        driverTools.waitForElementToAppear(FLIGHT_DETAIL_TITLE, 15);
        return driverTools.getText(FLIGHT_DETAIL_TITLE).orElse("");
    }

    public void closeFlightCardDetail() {
        driverTools.waitForElementToAppear(FLIGHT_DETAIL_MODAL_CLOSE, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToDisappear(FLIGHT_DETAIL_MODAL, 10);
    }

    public void verifyMultiCityOrderSummary(int expectedFlightCount, String expectedTitle, String expectedIconFragment) {
        driverTools.waitForElementToAppear(ORDER_SUMMARY_CONTAINER, 20);

        Assertion.assertThat("Order summary title should be the multi-city product name",
                getOrderSummaryProduct(), containsStringIgnoringCase(expectedTitle));

        Assertion.assertThat("Order summary icon should be visible", isOrderSummaryLogoVisible(), is(true));
        Assertion.assertThat("Order summary icon should be the flight icon",
                getOrderSummaryIconSource(), containsString(expectedIconFragment));

        Assertion.assertThat("Order summary card should show the number of flights ordered",
                getOrderSummaryFlightCountLabel(), containsString(String.valueOf(expectedFlightCount)));

        ensureOrderSummaryListOpen();
        Assertion.assertThat("Order summary card should be clickable and open the Order Summary popup",
                driverTools.isElementDisplayed(ORDER_SUMMARY_LIST_MODAL), is(true));

        Assertion.assertThat("Number of flight cards should match the number of flights ordered",
                getOrderSummaryFlightCardCount(), is(expectedFlightCount));

        for (int i = 0; i < expectedFlightCount; i++) {
            openFlightCard(i);
            Assertion.assertThat("Flight card " + (i + 1) + " should open its detail popup",
                    getFlightDetailTitle(), containsString(String.valueOf(i + 1)));
            closeFlightCardDetail();
        }
        closeOrderSummaryList();
    }

    public String getAndVerifyOrderSummaryProduct() {
        verifyOrderSummaryDetails();
        return getOrderSummaryProduct();
    }

    public String getDepartureTime(){
        driverTools.waitForElementToAppear(DEPARTURE_TIME);
        String departureTimeText = driverTools.getText(DEPARTURE_TIME).orElseThrow();
        departureTimeText = departureTimeText.replaceAll(".*?(\\d{2}:\\d{2}).*", "$1");
        return departureTimeText;
    }

    public String getArrivalTime(){
        driverTools.waitForElementToAppear(ARRIVAL_TIME);
        String arrivalTimeText = driverTools.getText(ARRIVAL_TIME).orElseThrow();
        arrivalTimeText = arrivalTimeText.replaceAll(".*?(\\d{2}:\\d{2}).*", "$1");
        return arrivalTimeText;
    }

    public String verifyErrorPopUpMsgFunct(String errorMsgEN){
        driverTools.waitForElementToAppear(ERROR_POP_UP_MSG_POP_UP).orElseThrow();
        String errorMsg = driverTools.getText(ERROR_POP_UP_MSG_DEBIT_BIN).orElseThrow();
        Assertion.assertThat("Verify promo doesn't support payment method error message.", errorMsg, is(errorMsgEN));
        return errorMsg;
    }

    public String getTrainPassengerNameOnOrderSummary() {
        WebElement passengerNameElement = driverTools.waitForElementToAppear(TRAIN_PASSENGER_NAME).orElseThrow();
        String passengerNameText = driverTools.getText(passengerNameElement).trim();
        String passengerName = passengerNameText.replaceAll("(?i)^\\s*\\d*\\.?\\s*(mr|mrs|ms|tuan|nyonya|nona)\\.?\\s+", "").trim();

        logger.log("Passenger name on order summary: " + passengerName);

        return passengerName;
    }

    public void selectIndoMaret(){
        WebElement cardElement = driverTools.waitForElementToAppear(SELECT_INDOMARET).orElseThrow();
        driverTools.click(cardElement);
    }

    public boolean isChangePaymentButtonVisible(){
        driverTools.waitForElementToAppear(CHANGE_PAYMENT_METHOD_BUTTON);
        return driverTools.isElementDisplayed(CHANGE_PAYMENT_METHOD_BUTTON);
    }

    public void setConfirmChangePaymentMethod(){
        driverTools.waitForElementToAppear(CONFIRM_CHANGE_PAYMENT_METHOD);
        driverTools.clickWithJavascriptExecutor(CONFIRM_CHANGE_PAYMENT_METHOD);
    }

    public boolean isPaymentPageVisible(){
        driverTools.waitForElementToAppear(PAYMENT_PAGE) ;
        return driverTools.isElementDisplayed(PAYMENT_PAGE) ;
    }

    public boolean isPaymentLogoOnPaymentConfirmationPageVisible(){
        driverTools.waitForElementToAppear(PAYMENT_LOGO_ON_PAYMENT_CONFIRMATION_PAGE);
        return driverTools.isElementDisplayed(PAYMENT_LOGO_ON_PAYMENT_CONFIRMATION_PAGE);
    }

    public boolean isVANumberDisplayed() {
        driverTools.waitForElementToAppear(VA_NUMBER);
        return driverTools.isElementDisplayed(VA_NUMBER);
    }

    public boolean isHowToPaySectionDisplayed() {
        driverTools.waitForElementToAppear(HOW_TO_PAY_SECTION);
        return driverTools.isElementDisplayed(HOW_TO_PAY_SECTION);
    }

    public boolean isPaymentPermataVAonPaymentConfirmationPage(){
        driverTools.waitForElementToAppear(PERMATA_VA_TITLE);
        return driverTools.isElementDisplayed(PERMATA_VA_TITLE);
    }


    public boolean isInsuranceApplied(){
        openPriceSummary();
        driverTools.waitForElementToAppear(INSURANCE_APPLIED);
        boolean isDisplayed = driverTools.isElementPresent(INSURANCE_APPLIED);
        closeDrawer();
        return isDisplayed;
    }

    public boolean isPaymentBSIVAonPaymentConfirmationPage(){
        driverTools.waitForElementToAppear(BSI_VA_TITLE);
        return driverTools.isElementDisplayed(BSI_VA_TITLE);
    }

    public boolean isLPGLabelPresentOnPaymentPage() {
        return driverTools.waitForElementsToAppear(LPG_LABEL_PAYMENT_PAGE).isPresent();
    }
    public abstract void selectInstallment();

    protected static Locator installmentTenureOption(String tenure) {
        return new Locator("INSTALLMENT_TENURE_OPTION_" + tenure,
                By.xpath("//input[@name='installmentOptions' and @value='\"" + tenure + "\"']/ancestor::label"));
    }

    /**
     * Opens the installment dropdown and selects the option for the given tenure (months),
     * e.g. "6" picks "6 x IDR ...", "0" picks "Full Payment".
     */
    public void selectInstallmentByTenure(String tenure) {
        driverTools.waitForElementToAppear(SELECT_INSTALLMENT_DROPDOWN).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SELECT_INSTALLMENT_DROPDOWN);
        Locator option = installmentTenureOption(tenure.trim());
        driverTools.waitForElementToAppear(option, 10).orElseThrow(
                () -> new IllegalStateException("Installment option for tenure " + tenure + " not found"));
        driverTools.clickWithJavascriptExecutor(option);
    }

    protected boolean isPaylaterInstallmentDropdownDisplayed() {
        return driverTools.waitForElementToAppear(SELECT_INSTALLMENT_DROPDOWN, 5).isPresent();
    }

    protected void selectPaylaterInstallment() {
        driverTools.waitForElementToAppear(SELECT_INSTALLMENT_DROPDOWN).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SELECT_INSTALLMENT_DROPDOWN);
        WebElement installmentOption = driverTools.waitForElementToAppear(INSTALLMENT_DROPDOWN_FIRST_OPTION, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(installmentOption);
    }

    public void openPayLaterInstallment() {
        driverTools.waitForElementToAppear(SELECT_INSTALLMENT_DROPDOWN).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SELECT_INSTALLMENT_DROPDOWN);
        driverTools.waitForElementToAppear(INSTALLMENT_FIRST_PAYMENT_DUE_LABEL, 10).orElseThrow();
    }

    public void closePayLaterInstallment() {
        driverTools.waitForElementToAppear(SELECT_INSTALLMENT_DROPDOWN).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SELECT_INSTALLMENT_DROPDOWN);
        Assertion.assertThat("Installment bottom sheet is closed",
                driverTools.waitForElementToDisappear(INSTALLMENT_FIRST_PAYMENT_DUE_LABEL), is(true));
    }

    public boolean isInstallmentInterestZeroLabelDisplayed() {
        return driverTools.waitForElementToAppear(INSTALLMENT_INTEREST_ZERO_LABEL, 5).isPresent();
    }

    public boolean isEarningPointsVisibleOnBookingPage(){
        return isEarningPointsVisible(BOOKING_EARNING_POINTS);
    }

    public String getEarningPointsFromBookingPage(){
        return extractPointsCount(BOOKING_EARNING_POINTS);
    }

    public boolean isEarningPointsVisibleOnPaymentPage(){
        return isEarningPointsVisible(PAYMENT_EARNING_POINTS);
    }

    public String getEarningPointsFromPaymentPage(){
        return extractPointsCount(PAYMENT_EARNING_POINTS);
    }

    public boolean isEarningPointsVisible(Locator locator){
        driverTools.waitForElementToAppear(locator, 10);
        return driverTools.isElementPresent(locator);
    }

    public String extractPointsCount(Locator locator){
        WebElement earningPointsElement = driverTools.waitForElementToAppear(locator).orElseThrow();
        String earningPointsText = driverTools.getText(earningPointsElement);
        logger.log("earningPointsText: " + earningPointsText);
        String pointsCount = earningPointsText.replaceAll("[^\\d]", "");
        logger.log("Extracted points count: " + pointsCount);
        return pointsCount;
    }

    public boolean verifyEarningPointsMatch(String bookingPoints, String paymentPoints){
        logger.log("Booking points: " + bookingPoints + ", Payment points: " + paymentPoints);
        return bookingPoints.equals(paymentPoints);
    }

    public void selectSakukuPaymentMethod(){
        driverTools.waitForElementToAppear(SELECT_SAKUKU_PAYMENT);
        driverTools.clickWithJavascriptExecutor(SELECT_SAKUKU_PAYMENT);
    }

    public boolean isDownPayBtnVisibleAndClickable (){
        driverTools.waitForElementToAppear(DOWNPAY_BUTTON);
        return driverTools.isElementPresent(DOWNPAY_BUTTON) && driverTools.isElementClickable(DOWNPAY_BUTTON,1);
    }

    public void clickDownPayButton(){
        driverTools.waitForElementToAppear(DOWNPAY_BUTTON);
        driverTools.clickWithJavascriptExecutor(DOWNPAY_BUTTON);
    }

    public boolean isDownPayInfoBannerVisible(){
        return driverTools.waitForElementToAppear(DOWNPAY_INFO_BANNER, 10).isPresent();
    }

    public boolean isDownPayNextBillDueDateVisible(){
        return driverTools.waitForElementToAppear(DOWNPAY_NEXT_BILL_DUE_DATE, 10).isPresent();
    }

    public boolean isDownPayDueDateOnConfirmationPageVisible(){
        return driverTools.waitForElementToAppear(DOWNPAY_CONFIRMATION_DUE_DATE, 10).isPresent();
    }

    public boolean isDownPayModalVisible(){
        driverTools.waitForElementToAppear(DOWNPAY_MODAL);
        return driverTools.isElementPresent(DOWNPAY_MODAL);
    }

    public boolean isDownPayCurrentBillVisible(){
        driverTools.waitForElementToAppear(DOWNPAY_CURRENT_BILL);
        return driverTools.isElementPresent(DOWNPAY_CURRENT_BILL);
    }

    public boolean isDownPayDueDateVisible(){
        driverTools.waitForElementToAppear(DOWNPAY_DUE_DATE);
        return driverTools.isElementPresent(DOWNPAY_DUE_DATE);
    }

    public void navigateBack() {
        driverTools.back();
    }

    public void handleNavigateBackConfirmationPopup() {
        if (driverTools.waitForElementToAppear(NAVIGATE_BACK_CONFIRMATION_POPUP, 3).isPresent()) {
            driverTools.clickWithJavascriptExecutor(NAVIGATE_BACK_CONFIRMATION_POPUP);
            driverTools.waitForElementToDisappear(NAVIGATE_BACK_CONFIRMATION_POPUP, 5);
        }
    }

    public void clickDanaWebviewPayButton(){
        driverTools.waitForElementToAppear(DANA_PAY_BUTTON);
        driverTools.clickWithJavascriptExecutor(DANA_PAY_BUTTON);
    }

    public void selectRecommendedCreditPayment() {
        driverTools.waitForElementToAppear(CREDIT_RECOMMENDED_PAYMENT);
        driverTools.clickWithJavascriptExecutor(CREDIT_RECOMMENDED_PAYMENT);
        driverTools.click(CVCV);
        driverTools.sendKeys(CVCV,"123");
    }

    /**
     * Selects Credit/Debit on the payment tile (mpayment / web). Prefer over index {@code [2]} or {@code @value='0'}.
     */
    public void selectCreditDebitCardTileRadio() {
        driverTools.waitForElementToAppear(SELECT_CREDIT_DEBIT_CARD_TILE_RADIO, 15);
        driverTools.scrollIntoWebView(SELECT_CREDIT_DEBIT_CARD_TILE_RADIO);
        driverTools.waitForElementToBeClickable(SELECT_CREDIT_DEBIT_CARD_TILE_RADIO, 10);
        driverTools.clickWithJavascriptExecutor(SELECT_CREDIT_DEBIT_CARD_TILE_RADIO);
    }

    public void waitAfterPayment() {
        driverTools.dummyWait(30);
    }

    public boolean checkFinalPrice(int price){
        String finalPrice=getFinalPrice();
        String priceOnBookingForm=String.valueOf(price).replaceAll("[^0-9]", "");
        return finalPrice.contains(priceOnBookingForm);
    }

    public void openPriceDetails(){
        driverTools.waitForElementToAppear(PRICE_DETAILS);
        driverTools.clickWithJavascriptExecutor(PRICE_DETAILS);
    }

    public boolean checkInsurancePrice(String price){
        WebElement webElement = driverTools.waitForElementToAppear(INSURANCE_PRICE).orElseThrow();
        String insurancePrice= driverTools.getText(webElement).replaceAll("[^0-9]", "");
        return price.contains(insurancePrice);
    }
    public void clickPayInFullButton(){
        driverTools.waitForElementToAppear(PAY_IN_FULL_BUTTON);
        driverTools.clickWithJavascriptExecutor(PAY_IN_FULL_BUTTON);
    }

    public boolean isPaymentSuccessfulHeaderVisible(){
        driverTools.waitForElementToAppear(PAYMENT_SUCCESSFUL_HEADER, 30);
        boolean visible = driverTools.isElementDisplayed(PAYMENT_SUCCESSFUL_HEADER);
        if (visible) {
            HotelPendingRetrySummaryNotifier.afterWebPaymentSuccess(session, apiTools, logger);
        }
        return visible;
    }

    public void clickTermsAndConditionCheckbox(){
        driverTools.waitForElementToAppear(TERMS_AND_CONDITION_CHECKBOX);
        driverTools.clickWithJavascriptExecutor(TERMS_AND_CONDITION_CHECKBOX);
    }

    public void clickSubmitRefundButton(){
        driverTools.waitForElementToAppear(SUBMIT_REFUND_BUTTON);
        driverTools.clickWithJavascriptExecutor(SUBMIT_REFUND_BUTTON);
    }

    public boolean isRescheduleRequestSubmittedVisible(){
        driverTools.waitForElementToAppear(RESCHEDULE_REQUEST_SUBMITTED_TEXT, 30);
        return driverTools.isElementPresent(RESCHEDULE_REQUEST_SUBMITTED_TEXT);
    }

    public boolean isCountdownTimerVisible(){
        driverTools.waitForElementToAppear(COUNTDOWN_TIMER);
        return driverTools.isElementDisplayed(COUNTDOWN_TIMER);
    }

    public boolean isCountdownRunning(){

        WebElement secondsTimer = driverTools.waitForElementToAppear(SECONDS_TIMER).orElseThrow();
        String initialSecond = driverTools.getText(secondsTimer);
        int initialValue = Integer.parseInt(driverTools.getText(secondsTimer));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        try {
            wait.until(ExpectedConditions.not(
                    ExpectedConditions.textToBe(SECONDS_TIMER.by(), initialSecond)
            ));
        } catch (Exception e) {
            return false;
        }

        WebElement updatedElement = driverTools.waitForElementToAppear(SECONDS_TIMER).orElseThrow();
        int newValue = Integer.parseInt(driverTools.getText(updatedElement));

        if (initialValue == 0 && newValue == 59) {
            return true;
        }

        return newValue < initialValue;
    }

    public boolean isDueDateConfirmationVisible() {
        driverTools.waitForElementToAppear(DUE_DATE_CONFIRMATION);
        return driverTools.isElementDisplayed(DUE_DATE_CONFIRMATION);
    }

    public boolean isVirtualAccountInfoVisible() {
        driverTools.waitForElementToAppear(VIRTUAL_ACCOUNT_NUMBER_SECTION);
        return driverTools.isElementDisplayed(VIRTUAL_ACCOUNT_NUMBER_SECTION);
    }

    public Boolean checkPromo(String code) {
        WebElement webElement=driverTools.waitForElementToAppear(PROMO_CODE).orElseThrow();
        String promoCode=webElement.getText();
        return code.equals(promoCode);
    }

    public void routeCardPaymentToXendit() {
        String cookieHeader = getBrowserCookieHeader();
        String paymentGateway = "xendit";

        UpdatePgConfigApi updatePgConfigApi = new UpdatePgConfigApi(
                new UpdatePgConfigApi.Inputs(cookieHeader, paymentGateway, true));
        ApiResponse updatePgResponse = updatePgConfigApi.hitApi(apiTools);
        logger.log("Update PG config response: " + updatePgResponse.getBody());
        Assertion.assertThat("Verify update PG config API status code", updatePgResponse.getStatusCode(), is(200));

        DefaultPgConfigApi defaultPgConfigApi = new DefaultPgConfigApi(
                new DefaultPgConfigApi.Inputs(cookieHeader, paymentGateway));
        ApiResponse defaultPgResponse = defaultPgConfigApi.hitApi(apiTools);
        logger.log("Default PG config response: " + defaultPgResponse.getBody());
        Assertion.assertThat("Verify default PG config API status code", defaultPgResponse.getStatusCode(), is(200));
    }

    private String getBrowserCookieHeader() {
        StringBuilder cookieHeader = new StringBuilder();
        for (Cookie cookie : driver.manage().getCookies()) {
            if (!cookieHeader.isEmpty()) {
                cookieHeader.append("; ");
            }
            cookieHeader.append(cookie.getName()).append("=").append(cookie.getValue());
        }
        return cookieHeader.toString();
    }

    public boolean isLoginBannerVisible() {
        return driverTools.waitForElementToAppear(LOG_IN_NOW_BANNER, 5).isPresent() &&
                driverTools.isElementDisplayed(LOG_IN_NOW_BANNER);
    }

    public void clickLoginBanner() {
        driverTools.waitForElementToAppear(LOG_IN_NOW_BANNER);
        driverTools.clickWithJavascriptExecutor(LOG_IN_NOW_BANNER);
    }
    public void clickCheckPaymentStatus() {
        if (driverTools.waitForElementToPresence(CHECK_PAYMENT_STATUS, 30).isPresent()) {
            driverTools.click(CHECK_PAYMENT_STATUS);
        }
    }

    public String getRefundRescheduleInfo() {
        driverTools.waitForElementToAppear(REFUND_RESCHEDULE_INFO, 5);
        return driverTools.getText(
                driverTools.element(REFUND_RESCHEDULE_INFO).orElseThrow()
        ).trim();
    }

    public void openRRPolicies(){
        logger.log("Opening order summary to check Refund policies");
        if (!driverTools.isElementDisplayed(ORDER_SUMMARY_DETAILS_LINK)) {
            openOrderDetails();
        }
        driverTools.waitForElementToAppear(ORDER_SUMMARY_DETAILS_LINK);
        driverTools.clickWithJavascriptExecutor(ORDER_SUMMARY_DETAILS_LINK);
    }

    public void verifyRefundRescheduleModalOpened() {
        WebElement modalTitle = driverTools.waitForElementToAppear(REFUND_MODAL_TITLE).orElseThrow(
                () -> new NoSuchElementException("Refund & Reschedule Info modal was not found.")
        );
        Assertion.assertThat("Verify Refund & Reschedule modal is displayed", modalTitle.isDisplayed(), is(true));
    }

    public String verifyRefund100PercentPresent() {

        // 1. Find the iframe and switch context to it
        WebElement iframe = driverTools.waitForElementToAppear(REFUND_INFO_IFRAME).orElseThrow(
                () -> new NoSuchElementException("Refund info iframe was not found.")
        );
        driverTools.switchToIframe(iframe);

        try {
            // 2. Wait for and verify the text inside the iframe
            WebElement refundText = driverTools.waitForElementToAppear(REFUND_100_TEXT).orElseThrow(
                    () -> new NoSuchElementException("The '100%' text was not found inside the iframe.")
            );
            String text = driverTools.getText(refundText);
            logger.log(text);
           return text;

        } finally {
            // 3. ALWAYS switch back to the main page, even if the assertion above fails
            driverTools.switchBackFromIframe();
        }

    }

    public boolean verifyRefundPolicyMatches(List<String> expectedKeywords) {
        logger.log("Checking Refund policies on Payment Page");
        if (driverTools.elements(REFUND_INFO_IFRAME).isEmpty() || !driverTools.isElementDisplayed(REFUND_INFO_IFRAME)) {
            if (!driverTools.isElementDisplayed(ORDER_SUMMARY_DETAILS_LINK)) {
                openOrderDetails();
            }
            driverTools.waitForElementToAppear(ORDER_SUMMARY_DETAILS_LINK);
            driverTools.clickWithJavascriptExecutor(ORDER_SUMMARY_DETAILS_LINK);
        }

        WebElement iframe = driverTools.waitForElementToAppear(REFUND_INFO_IFRAME).orElseThrow();
        driverTools.switchToIframe(iframe);
        try {
            Locator refundTab = new Locator("REFUND_TAB_IFRAME", By.xpath("//*[contains(text(),'Refund') or contains(text(),'Pengembalian')][self::span or self::button or self::div or self::a]"));

            WebElement refundTabElement = driverTools.waitForElementToAppear(refundTab, 5)
                    .orElseThrow(() -> new NoSuchElementException("Refund tab not found in iframe on Payment page"));

            driverTools.clickWithJavascriptExecutor(refundTabElement);
            driverTools.dummyWait(1);

            String iframeText = driverTools.isElementPresent(ACTIVE_PAYMENT_TIMELINE)
                    ? driverTools.getText(driverTools.element(ACTIVE_PAYMENT_TIMELINE).orElseThrow())
                    : driverTools.getText(driverTools.element(new Locator("IFRAME_BODY", By.tagName("body"))).orElseThrow());

            return driverTools.verifyTextContainsKeywords(iframeText, expectedKeywords);
        } finally {
            driverTools.switchBackFromIframe();
        }
    }

    public boolean verifyReschedulePolicyMatches(List<String> expectedKeywords) {
        logger.log("Checking Reschedule policies on Payment Page");
        if (driverTools.elements(REFUND_INFO_IFRAME).isEmpty() || !driverTools.isElementDisplayed(REFUND_INFO_IFRAME)) {
            if (!driverTools.isElementDisplayed(ORDER_SUMMARY_DETAILS_LINK)) {
                openOrderDetails();
            }
            driverTools.waitForElementToAppear(ORDER_SUMMARY_DETAILS_LINK);
            driverTools.clickWithJavascriptExecutor(ORDER_SUMMARY_DETAILS_LINK);
        }

        WebElement iframe = driverTools.waitForElementToAppear(REFUND_INFO_IFRAME).orElseThrow();
        driverTools.switchToIframe(iframe);
        try {
            Locator rescheduleTab = new Locator("RESCHEDULE_TAB_IFRAME", By.xpath("//*[contains(text(),'Reschedule') or contains(text(),'Jadwal') or contains(text(),'Ubah Jadwal')][self::span or self::button or self::div or self::a]"));

            WebElement rescheduleTabElement = driverTools.waitForElementToAppear(rescheduleTab, 5)
                    .orElseThrow(() -> new NoSuchElementException("Reschedule tab not found in iframe on Payment page"));

            driverTools.clickWithJavascriptExecutor(rescheduleTabElement);
            driverTools.dummyWait(1);

            String iframeText = driverTools.isElementPresent(ACTIVE_PAYMENT_TIMELINE)
                    ? driverTools.getText(driverTools.element(ACTIVE_PAYMENT_TIMELINE).orElseThrow())
                    : driverTools.getText(driverTools.element(new Locator("IFRAME_BODY", By.tagName("body"))).orElseThrow());

            return driverTools.verifyTextContainsKeywords(iframeText, expectedKeywords);
        } finally {
            driverTools.switchBackFromIframe();
        }
    }

     public void clickDownPayConfirmInModal() {
        driverTools.waitForElementToAppear(DOWNPAY_CONFIRM_BUTTON);
        driverTools.clickWithJavascriptExecutor(DOWNPAY_CONFIRM_BUTTON);
        driverTools.waitForElementToDisappear(DOWNPAY_MODAL, 10);
    }

    public boolean isPriceSummaryVisible() {
        driverTools.waitForElementToAppear(FINAL_PRICE);
        return driverTools.isElementDisplayed(FINAL_PRICE);
    }

}

