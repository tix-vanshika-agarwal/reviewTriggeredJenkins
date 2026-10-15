package com.tiket.page.payment;

import com.google.gson.Gson;
import com.tiket.api.payment.PaymentCheckerApi;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.*;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.home.HomePage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.pojo.bus.BusBookingPageData;
import com.tiket.pojo.flight.PriceDetail;
import com.tiket.pojo.flight.PromoData;
import com.tiket.pojo.order.GeneralVerticalInternalOrderDetail;
import com.tiket.pojo.payment.PaymentCheckerResponse;
import com.tiket.pojo.train.TrainData;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.tiket.core.CoreFunctions.removeLeadingZeroFromDate;
import static com.tiket.core.CoreFunctions.removeText;
import static com.tiket.core.CoreFunctions.replaceShortMonth;
import static com.tiket.page.login.LoginPage.LOADING;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;


public abstract class PaymentPage <T extends WebDriver> extends BasePage<T> {

    private static final Locator SELECTED_PAYMENT_TEXT = new Locator("SELECTED_PAYMENT_TEXT", AppiumBy.xpath("//*[contains(@class,'PaymentTile_seleted')]//span[string-length(text()) > 0]"));
    private static final Locator PRODUCT_NAME_TEXT = new Locator("PRODUCT_NAME_TEXT", AppiumBy.xpath("//*[contains(@class,'OrderSummaryItem_summary_title')]//span"));
    private static final Locator DATE_DURATION_TEXT = new Locator("DATE_DURATION_TEXT", AppiumBy.xpath("//*[contains(@class,'OrderSummaryItem_summary_bottom_container') or contains(@class,'OrderSummary_subtitle_container')]//span[1]"));
    private static final Locator NIGHT_DURATION_TEXT = new Locator("NIGHT_DURATION_TEXT", AppiumBy.xpath("//*[contains(@class,'OrderSummaryItem_summary_bottom_container') or contains(@class,'OrderSummary_subtitle_container')]//span[last()]"));
    private static final Locator CARD_NUMBER = new Locator("CARD_NUMBER", AppiumBy.xpath("//input[@id='creditdebit-card-number' or @id='nomor-kartu-kreditdebit']"));
    private static final Locator EXPIRY = new Locator("EXPIRY", AppiumBy.xpath("//input[@id='mmyy']"));
    private static final Locator CVV = new Locator("CVV", AppiumBy.xpath("//input[@id='cvccvv']"));
    private static final Locator CARD_NAME = new Locator("CARD_NAME", AppiumBy.xpath("//input[@id='cardholder-name' or @id='nama-pemilik-kartu']"));
    private static final Locator POSTAL_CODE = new Locator("POSTAL_CODE", AppiumBy.xpath("//input[@name='avsFields.postalCode']"));
    private static final Locator BILLING_ADDRESS = new Locator("BILLING_ADDRESS", AppiumBy.xpath("//input[@name='cardBillingAddress']"));
    private static final Locator SUBMIT_CC = new Locator("SUBMIT_CC", AppiumBy.xpath("//*[contains(@class,'button-submit')]"));
    private static final Locator SUBMIT_CC_NEXTJS = new Locator("SUBMIT_CC_NEXTJS", AppiumBy.xpath("//button[contains(@type,'submit')]"));
    private static final Locator SUBMIT_CC_NEW = new Locator("SUBMIT_CC_NEW", AppiumBy.xpath("//button[contains(@class,'SubmitPayment')]"));
    private static final Locator CARD_NUMBER_V3 = new Locator("CARD_NUMBER_V3", AppiumBy.xpath("//*[contains(@name,'cardNumber')]"));
    private static final Locator EXPIRY_V3 = new Locator("EXPIRY_V3", AppiumBy.xpath("//*[contains(@data-rules,'expiredCard') or contains(@name,'cardExpired')]"));
    private static final Locator CVV_V3 = new Locator("CVV_V3", AppiumBy.xpath("//*[contains(@name,'cardCvvNumber') or contains(@name,'cardCVV')]"));
    private static final Locator CARD_NAME_V3 = new Locator("CARD_NAME_V3", AppiumBy.xpath("//*[contains(@name,'cardHolderName')]"));
    private static final Locator SUBMIT_CC_V3 = new Locator("SUBMIT_CC_V3", AppiumBy.xpath("//*[@class='payment-button-next-step']"));
    private static final Locator IFRAME_AIRWALLEX = new Locator("IFRAME_AIRWALLEX", AppiumBy.cssSelector("#issuer-iframe"));
    private static final Locator OTP_AIRWALLEX_INPUT = new Locator("OTP_AIRWALLEX_INPUT", AppiumBy.cssSelector("#challengeDataEntry"));
    private static final Locator SUBMIT_AIRWALLEX = new Locator("SUBMIT_AIRWALLEX", AppiumBy.cssSelector("#submit"));
    private static final Locator SUBMIT = new Locator("SUBMIT", AppiumBy.xpath("//*[@type='submit']"));
    public static final Locator IFRAME_STRIPE_1 = new Locator("IFRAME_STRIPE_1", AppiumBy.xpath("//iframe[contains(@name,'StripeFrame')]"));
    public static final Locator IFRAME_STRIPE_2 = new Locator("IFRAME_STRIPE_2", AppiumBy.xpath("//*[@id='challengeFrame']"));
    private static final Locator SUBMIT_STRIPE = new Locator("SUBMIT_STRIPE", AppiumBy.xpath("//*[@id='test-source-authorize-3ds']"));
    private static final Locator OTP_2C2P_INPUT = new Locator("OTP_2C2P_INPUT", AppiumBy.xpath("//*[@name='challengeDataEntry']"));
    private static final Locator SUBMIT_2C2P = new Locator("SUBMIT_2C2P", AppiumBy.xpath("//*[@class='acs-challenge-form-actions']//*[@class='acs-challenge-btn proceed']"));
    private static final Locator PRICE_LOCATOR_NFT = new Locator("PRICE_LOCATOR_NFT", AppiumBy.xpath("//*[contains(@class,'bottom_sheet')]//*[contains(@class,'horizontal')]//preceding::*[contains(@class,'price_group')]//*[2] | //*[contains(@class,'bottom')]//*[contains(@class,'PriceDetailV4_price_list_item')]/div/*[2]"));
    private static final Locator PRICE_LOCATOR = new Locator("PRICE_LOCATOR", AppiumBy.xpath("//*[contains(@class,'children_wrapper')]//*[contains(@class,'PriceDetailV4_price_list')]//*[contains(@class,'PriceDetailV4_price_list_item__')]//*[contains(text(),',') or contains(text(),'.') or contains(@class,'Text_size') and not(contains(@class,'price_list_title')) and not(contains(@class,'content_title'))] | //*[contains(@class,'bottom_sheet')]//*[contains(@class,'PriceDetails_price_group') and not(contains(@class,'PriceDetails_total_payment'))]/*[2]"));
    private static final Locator PRICE_LOCATOR_NO_OTHER_FEES = new Locator("PRICE_LOCATOR_NO_OTHER_FEES", AppiumBy.xpath("//*[contains(@class,'PriceDetailV4_accordion_panel')]//*[contains(@class,'Text_text') and contains(text(),'IDR')]"));
    private static final Locator TOTAL_PRICE_FROM_PRICE_DETAIL = new Locator("TOTAL_PRICE_FROM_PRICE_DETAIL", AppiumBy.xpath("((//*[contains(@class,'PriceDetailV4_total_section')]//*[contains(@class,'PriceDetailV4_total')])//span[contains(text(),',') or contains(text(),'.')])[1] | //*[contains(@class,'bottom_sheet')]//*[contains(@class,'PriceDetails_total_payment')]/*[2]"));
    private static final Locator TOTAL_PRICE_NFT_FROM_PRICE_DETAIL = new Locator("TOTAL_PRICE_NFT_FROM_PRICE_DETAIL", AppiumBy.xpath("(//*[contains(@class,'bottom_sheet')]//preceding::*[contains(@class,'price_group')]//*[2])[last()]"));
    private static final Locator TRIP_TITLE = new Locator("TRIP_TITLE", AppiumBy.xpath("//*[@data-testid='bottom-sheet-body']//*[contains(@class,'PriceDetailV4_price_title')]"));
    private static final Locator TRIP_HEADER = new Locator("TRIP_HEADER", AppiumBy.xpath("//*[@data-testid='bottom-sheet-body']//*[contains(@class,'PriceDetailV4_accordion_header')]"));
    private static final Locator PRICE_DETAIL_V4_CHEV_BTN = new Locator("PRICE_DETAIL_V4_CHEV_BTN", AppiumBy.xpath("//span[contains(@class,'PriceDetailV4_accordion_header')]//following-sibling::*[contains(@class,'icon_inverted')]"));
    private static final Locator PRICE_DETAIL_SEGMENT_BUTTON = new Locator("PRICE_DETAIL_SEGMENT_BUTTON", AppiumBy.xpath("//*[contains(@class,'bottom')]//*[contains(@class,'PriceDetailV4_price_segment')]"));
    private static final Locator DEPART_ARRIVAL_STATION_TEXT_PRICE_DETAIL = new Locator("DEPART_ARRIVAL_STATION_TEXT_PRICE_DETAIL", AppiumBy.xpath("(//*[contains(@class,'PriceDetailV4_price_title')])[1]"));
    public static final Locator SEE_ORDER_LIST_BUTTON_WEBVIEW = new Locator("SEE_ORDER_LIST_BUTTON_WEBVIEW", AppiumBy.xpath("//*[contains(text(), 'See Order List') or contains(text(), 'Lihat Daftar Pesanan') or contains(text(), 'See order list') or contains(text(), 'Lihat daftar pesanan')]"));
    public static final Locator IFRAME_INFO = new Locator("IFRAME_INFO", AppiumBy.xpath("//iframe[contains(@src,'refund-reschedule-nudges')]"));
    private static final Locator CLOSE_BTN_PRICE_DETAIL = new Locator("CLOSE_BTN_PRICE_DETAIL", AppiumBy.xpath("(//*[@data-testid='bottom-sheet-close-handle'])[last()] | //*[contains(@class,'close_button') or contains(@class,'BottomSheet_close_button') or @data-testid='modal-close-button']"));
    private static final Locator BOTTOMSHEET_CONTAINER = new Locator("BOTTOMSHEET_CONTAINER", AppiumBy.xpath("//*[@data-testid='bottom-sheet-body']"));
    private static final Locator PRICE_DETAIL_BOTTOM_SHEET = new Locator("PRICE_DETAIL_BOTTOM_SHEET", AppiumBy.xpath("//*[@data-testid='bottomsheet_testing' or contains(@class,'BottomSheet_body') or contains(@class,'bottom_sheet')][.//*[contains(text(),'Price Details') or contains(text(),'Detail Harga') or contains(text(),'Rincian Harga')] or .//*[contains(@class,'PriceDetailV4')]] | //*[contains(@class,'Modal_modal_popup')][.//*[contains(text(),'Price Details') or contains(text(),'Detail Harga')]]"));
    private static final Locator CONTINUE_TO_PAYMENT_BUTTON = new Locator("CONTINUE_TO_PAYMENT_BUTTON", AppiumBy.xpath("//*[contains(@type,'button') and contains(text(), 'Lanjut ke Pembayaran') or contains(text(), 'Continue to Payment') or contains(text(), 'Lanjut ke pembayaran') or contains(text(), 'Continue to payment')]"));
    private static final Locator LOYALTY_DISCOUNT = new Locator("LOYALTY_DISCOUNT", AppiumBy.xpath("//*[contains(@class,'bottom_sheet')]//*[contains(@class,'PriceDetailV4_price_list_item')]//span[contains(text(),'Tier')]//following-sibling::span[last()]"));
    private static final Locator PRODUCT_NAME_TITLE = new Locator("PRODUCT_NAME_TITLE", AppiumBy.xpath("//div[contains(@class,'OrderSummaryItem_summary_title')]//div"));
    private static final Locator TOTAL_ROOM_HOTEL = new Locator("TOTAL_ROOM_HOTEL", AppiumBy.xpath("//*[contains(@class,'OrderSummaryItem_summary_bottom_container')]//div"));
    private static final Locator TOTAL_GUEST_HOTEL_DETAIL = new Locator("TOTAL_GUEST_HOTEL_DETAIL", AppiumBy.xpath("//span[contains(text(),'Guest') or contains(text(), 'Tamu')]//following-sibling::ol//li"));
    private static final Locator VALIDITY_PERIOD = new Locator("VALIDITY_PERIOD", AppiumBy.xpath("//*[contains(@class,'BottomSheet_body')]//*[contains(text(),'Valid') or contains(text(),'Berlaku')]/following-sibling::*[1]/child::*[1]"));
    private static final Locator SUBMIT_PAYMENT_BUTTON = new Locator("SUBMIT_PAYMENT_BUTTON", AppiumBy.xpath("//*[contains(@class,'SubmitPayment_pay_now')]"));
    private static final Locator PREVIOUS_BOOKING_POPUP = new Locator("PREVIOUS_BOOKING_POPUP", AppiumBy.xpath("//*[contains(text(),'Lanjut ke Pembayaran') or contains(text(),'Continue to Payment') or contains(text(),'Lanjut ke pembayaran') or contains(text(),'Continue to payment')]"));
    private static final Locator PRICE_DETAIL_ARROW_BUTTON = new Locator("PRICE_DETAIL_ARROW_BUTTON", AppiumBy.xpath("//span[contains(@class, 'PriceSummary_final_price')]/following-sibling::*"));
    private static final Locator DISCOUNT_PRICE_DETAIL = new Locator("DISCOUNT_PRICE_DETAIL", AppiumBy.xpath("(//*[contains(@class, 'content_title')]//following-sibling::span[contains(text(),'-')])[1]"));
    private static final Locator PAYMENT_PAGE_LOGIN_BTN = new Locator("PAYMENT_PAGE_LOGIN_BTN", AppiumBy.xpath("//div[contains(@class,'LoginBox')]//*[ contains(text(),'Log in')]"));
    private static final Locator PRICE_DETAIL_100P_REFUND_PAYMENT_PAGE = new Locator("PRICE_DETAIL_100P_REFUND_PAYMENT_PAGE", AppiumBy.xpath("(//*[contains(@class, 'price_list_title') and contains(text(),'tiket 100% Refund')])[1]"));
    private static final Locator PAY_WITH_TIKET_POINT_TOGGLE = new Locator("PAY_WITH_TIKET_POINT_TOGGLE",AppiumBy.xpath("//*[contains(@class,'toggle_small')]"));
    private static final Locator PAY_WITH_TIKET_POINT_CHECKBOX = new Locator("PAY_WITH_TIKET_POINT_CHECKBOX", AppiumBy.xpath("//*[contains(@class,'tiket_points')]//*[contains(@class,'checkbox')]"));
    private static final Locator PAY_WITH_TIKET_POINT_OTP_CONFIRMATION = new Locator("PAY_WITH_TIKET_POINT_OTP_CONFIRMATION", AppiumBy.xpath("//*[contains(@class,'otp_input')]//input"));
    private static final Locator TIKET_POINTS_VALUE = new Locator("TIKET_POINTS_VALUE",AppiumBy.xpath("//*[@data-testid='txtTixpointsLabel']//span"));
    private static final Locator TIKET_POINTS_CAP_INFO_ICON = new Locator("TIKET_POINTS_CAP_INFO_ICON", AppiumBy.xpath("//*[@data-testid='icTixpointTooltip']"));
    private static final Locator TIKET_POINTS_CAP_TOOLTIP = new Locator("TIKET_POINTS_CAP_TOOLTIP", AppiumBy.xpath("//*[contains(@class,'Tooltip_container')]//p"));
    private static final Locator CREDIT_CARD_PAYMENT_METHOD = new Locator("CREDIT_CARD_PAYMENT_METHOD", AppiumBy.xpath("//*[contains(@class,'Groups_title') and (contains(text(),'Kartu Kredit/Debit') or contains(text(),'Credit/Debit Card') or contains(text(),'Kartu Kredit / Kartu Debit') or contains(text(),'Credit Card / Debit Card') or contains(text(),'CreditCard'))]"));
    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", AppiumBy.xpath("//span[contains(@class,'PriceSummary_final_price')]"));
    private static final Locator TOTAL_PRICE_FREE = new Locator("TOTAL_PRICE_FREE", AppiumBy.xpath("//span[contains(@class,'PriceSummary_final_price') and (text()='GRATIS' or text()='FREE')]"));
    private static final Locator PAYMENT_SUCCESSFUL = new Locator("PAYMENT_SUCCESSFUL", AppiumBy.xpath("//*[contains(@class,'SuccessV2_popup_mobile_title') or contains(@class,'Button_variant_primary')]"));
    private static final Locator LOADING_STATE = new Locator("LOADING_STATE", AppiumBy.id("loading"));
    public static final Locator SEE_ALL_PAYMENT_METHOD_BUTTON_NEXTJS = new Locator("CHANGE_PAYMENT_METHOD_BUTTON_NEXTJS", AppiumBy.xpath("//*[contains(@class,'PaymentMethod_see_all_group')]//*"));
    // Variant-independent page-ready signals. Both are emitted by the Next.js
    // payment page in BOTH the multi-method (variant A) and single-method
    // inline-form (variant B) layouts, so they're safe to wait on before any
    // variant-specific decision.
    public static final Locator PAYMENT_PAGE_METHOD_CONTAINER = new Locator("PAYMENT_PAGE_METHOD_CONTAINER", AppiumBy.xpath("//*[@data-testid='payment-method']"));
    public static final Locator PAYMENT_PAGE_SUBMIT_BUTTON = new Locator("PAYMENT_PAGE_SUBMIT_BUTTON", AppiumBy.xpath("//*[@data-testid='submit_button']"));
    public static final Locator SELECT_PAYMENT_METHOD = new Locator("SELECT_PAYMENT_METHOD", AppiumBy.xpath("//*[contains(@class,'styles_title')]/following-sibling::*"));
    public static final Locator CHANGE_PAYMENT_METHOD = new Locator("CHANGE_PAYMENT_METHOD", AppiumBy.xpath("//*[contains(@class,'change_button')]"));
    private static final Locator SAVE_CARD_DETAIL = new Locator("SAVE_CARD_DETAIL", AppiumBy.xpath("//*[contains(@type,'submit') and contains(text(),'Simpan Kartu') or contains(text(),'Save Card')]"));
    private static final Locator INSTALLMENTS = new Locator("INSTALLMENTS", AppiumBy.xpath("(//div[contains(@class,'BottomSheet')]//span[contains(@class,'Radio_checkbox')])"));
    private static final Locator INSTALLMENTS_DROPDOWN = new Locator("INSTALLMENTS_DROPDOWN", AppiumBy.xpath("//*[contains(@class,'DropdownFormDisplay_chevron')]"));
    private static final Locator BTPL_INSTALLMENT_DROPDOWN = new Locator("BTPL_INSTALLMENT_DROPDOWN", AppiumBy.xpath("//*[@id='select-installment']"));
    private static final Locator SAVE_INSTALLMENTS_BUTTON= new Locator("SAVE_INSTALLMENTS_BUTTON", AppiumBy.xpath("(//*[contains(@class,'KV-nSG_full_width') or contains(text(),'Save') or contains(text(),'Simpan')])[last()]"));
    private static final Locator INSTALLMENTS_DETAIL  = new Locator("INSTALLMENTS_DETAIL", AppiumBy.xpath("//*[contains(@class,'installment_wrapper')]//*[contains(@class,'DropdownFormDisplay_input')]"));
    private static final Locator INSTALLMENT_INTEREST_LABEL = new Locator("INSTALLMENT_INTEREST_LABEL", AppiumBy.xpath("//div[contains(@class,'BottomSheet')]//*[contains(text(),'Bunga 0%') or contains(text(),'Interest 0%')]"));
    private static final Locator INSTALLMENT_FEE_TEXT = new Locator("INSTALLMENT_FEE_TEXT", AppiumBy.xpath("//div[contains(@class,'BottomSheet')]//*[contains(text(),'PayLater')]"));
    private static final Locator ZERO_INTEREST_TENOR_ROW = new Locator("ZERO_INTEREST_TENOR_ROW", AppiumBy.xpath("(//div[contains(@class,'BottomSheet')]//*[contains(text(),'Bunga 0%') or contains(text(),'Interest 0%')])[1]/ancestor::*[count(.//span[contains(@class,'Radio_checkbox')])=1][last()]"));
    private static final Locator ZERO_INTEREST_TENOR_ROW_FEE_TEXT = new Locator("ZERO_INTEREST_TENOR_ROW_FEE_TEXT", AppiumBy.xpath("(//div[contains(@class,'BottomSheet')]//*[contains(text(),'Bunga 0%') or contains(text(),'Interest 0%')])[1]/ancestor::*[count(.//span[contains(@class,'Radio_checkbox')])=1][last()]//*[contains(text(),'PayLater')]"));
    private static final Locator ZERO_INTEREST_TENOR_RADIO = new Locator("ZERO_INTEREST_TENOR_RADIO", AppiumBy.xpath("(//div[contains(@class,'BottomSheet')]//*[contains(text(),'Bunga 0%') or contains(text(),'Interest 0%')])[1]/ancestor::*[count(.//span[contains(@class,'Radio_checkbox')])=1][last()]//span[contains(@class,'Radio_checkbox')]"));

    private static final Locator REFUND_BANNER = new Locator("REFUND_BANNER", AppiumBy.xpath("(//*[contains(@id,'installment-radio') and @value='true']/preceding::*[contains(@class,'installment-notes')]/*[contains(@class,'emphasis hover')])[last()]"));
    private static final Locator PRICE_DETAIL = new Locator("PRICE_DETAIL", AppiumBy.xpath("//div[contains(@class,'PriceSummary_final_price_wrapper')]//*[local-name()='svg' and contains(@class,'PriceSummary_more_icon')]"));
    private static final Locator SERVICE_CHARGE_PRICE_DETAIL = new Locator("SERVICE_CHARGE_PRICE_DETAIL", AppiumBy.xpath("//*[contains(text(),'Biaya layanan') or contains(text(),'Service charge')]//following-sibling::*"));
    public static final Locator ACCOUNT_NUMBER_CLIPBOARD = new Locator("ACCOUNT_NUMBER_CLIPBOARD", AppiumBy.xpath("(//*[contains(@class, 'AccountInfo_number')]//*[contains(@class, 'AccountInfo_icon_copy')])[1]"));
    public static final Locator TOTAL_PRICE_CLIPBOARD = new Locator("TOTAL_PRICE_CLIPBOARD", AppiumBy.xpath("(//*[contains(@class, 'AccountInfo_number')]//*[contains(@class, 'AccountInfo_icon_copy')])[last()]"));
    public static final Locator UNIQUE_CODE = new Locator("UNIQUE_CODE", AppiumBy.xpath("//*[contains(text(), 'Unique code') or contains(text(), 'Kode unik')]"));
    private static final Locator CANT_COMBINE_PROMO_LABEL = new Locator("CANT_COMBINE_PROMO_LABEL", AppiumBy.xpath("(//*[contains(@class,'TiketPoints_tiket_points_main_container')]//span[contains(@class,'text') and not(contains(@class,'children'))])[last()]"));
    private static final Locator DEPOSIT_AMOUNT = new Locator("DEPOSIT_AMOUNT", AppiumBy.xpath("//*[contains(@class,'PaymentTile_selected_payment_info')]"));
    public static final Locator BLIBLIPAY_BALANCE_PAYMENT_LANDING = new Locator("BLIBLIPAY_BALANCE_PAYMENT_LANDING", AppiumBy.xpath("//*[contains(@class,'SelectedPayment_selected_payment_container')]//*[contains(@class,'PaymentTile_selected_payment_info')]"));
    public static final Locator BLIBLIPAY_BALANCE_PAYMENT_GROUP = new Locator("BLIBLIPAY_BALANCE_PAYMENT_GROUP", AppiumBy.xpath("//*[contains(@class,'Groups_title_wrapper')]/following-sibling::p"));
    private static final Locator USE_ANOTHER_CARD = new Locator("USE_ANOTHER_CARD", AppiumBy.xpath("//*[contains(@class,'SavedCards_add_credit_card')] | //*[contains(@class,'SavedCards_cc_wrapper')][last()]"));
    private static final Locator PROMO_SECTION = new Locator("PROMO_SECTION", AppiumBy.xpath("//*[@data-testid='btnApplyUnapplyPromo' or contains(@class,'PromoCard_container') or contains(@class,'PromoDisplay')]"));
    private static final Locator PROMO_SECTION_CODE = new Locator("PROMO_SECTION_CODE", AppiumBy.xpath("//*[@data-testid='btnApplyUnapplyPromo']//p | //*[contains(@class,'PromoCard_container')]//p | //*[contains(@class,'PromoDisplay')]//p"));
    private static final Locator PROMO_SECTION_TEXT = new Locator("PROMO_SECTION_TEXT", AppiumBy.xpath("//*[contains(@class,'ApplyPromoText_promo_text') or contains(@class,'PromoDisplay')]//span"));
    private static final Locator SUGGESTED_PROMO = new Locator("SUGGESTED_PROMO", AppiumBy.xpath("//*[contains(@class,'SuggestionItem')]//*[contains(@class,'text') and contains(text(),'Code') or  contains(text(),'Kode')]"));
    private static final Locator SUGGESTED_PROMO_CODE = new Locator("SUGGESTED_PROMO_CODE", AppiumBy.xpath("//*[contains(@class,'SuggestionItem_code_wrapper')]//*[contains(@class,'Text')]"));
    private static final Locator SUGGESTED_GIFT_VOUCHER = new Locator("SUGGESTED_GIFT_VOUCHER", AppiumBy.xpath("//*[contains(@class,'EligiblePromos_list')]//*[contains(@class,'card_container') and .//*[contains(text(),'Gift Voucher')] and .//*[contains(text(),'Hemat') or contains(text(),'Save')]]"));
    private static final Locator SUGGESTED_GIFT_VOCHER_CODE = new Locator("SUGGESTED_GIFT_VOCHER_CODE", AppiumBy.xpath("//*[contains(@class,'EligiblePromos_list')]//*[contains(@class,'card_container') and .//*[contains(text(),'Gift Voucher')] and .//*[contains(text(),'Hemat') or contains(text(),'Save')]]//*[contains(@class,'SuggestionItem_code')]"));
    private static final Locator SUGGESTED_PROMO_DISCOUNT_AMOUNT = new Locator("SUGGESTED_PROMO_DISCOUNT_AMOUNT", AppiumBy.xpath("//*[contains(@class,'EligiblePromos_list')]//*[contains(@class,'SuggestionItem_label_container')]//*[contains(text(),'Hemat') or contains(text(),'Save')]"));
    private static final Locator SUGGESTED_GIFT_VOUCHER_DISCOUNT_AMOUNT = new Locator("SUGGESTED_GIFT_VOUCHER_DISCOUNT_AMOUNT", AppiumBy.xpath("//*[contains(@class,'EligiblePromos_list')]//*[contains(@class,'card_container') and .//*[contains(text(),'Gift Voucher')]]//*[contains(text(),'Hemat') or contains(text(),'Save')]"));
    private static final Locator APPLIED_PROMO_TEXT = new Locator("APPLIED_PROMO_TEXT", AppiumBy.xpath("//*[contains(@class,'ApplyPromoText_promo_text') or contains(@class,'PromoDisplay_text_wrapper')]"));
    private static final Locator DISCOUNT_AMOUNT = new Locator("DISCOUNT_AMOUNT",AppiumBy.xpath("//*[contains(@class,'PriceSummary_discount_wrapper')]//span"));
    private static final Locator CLOSE_PAYMENT_GROUP = new Locator("CLOSE_PAYMENT_GROUP", AppiumBy.xpath("//*[contains(@class,'_bottom_sheet') and not(contains(@class,'scrolled'))]//button[contains(@class,'_close_button')]"));
    private static final Locator PROMO_CODE_BUTTON = new Locator("PROMO_CODE_BUTTON", AppiumBy.xpath("//*[@data-testid='btnApplyUnapplyPromo' or contains(@class,'PromoCard_clickable') or contains(@class,'PromoDisplay')]"));
    private static final Locator PROMO_CODE_INPUT = new Locator("PROMO_CODE_INPUT", AppiumBy.xpath("//input[@id='enter-your-code' or @id='masukkan-kodemu']"));
    private static final Locator APPLY_PROMOCODE_BUTTON = new Locator("APPLY_PROMOCODE_BUTTON", AppiumBy.xpath("(//*[contains(@class,'apply_button')])[1]"));
    private static final Locator SLASH_PRICE_BEFORE = new Locator("SLASH_PRICE_BEFORE", AppiumBy.xpath("//*[contains(@class, 'PriceSummary_slash_price')]"));
    private static final Locator PROMO_DISCOUNT_PERCENTAGE = new Locator("PROMO_DISCOUNT_PERCENTAGE", AppiumBy.xpath("//*[contains(@class, 'PriceSummary_slash_price')]/following-sibling::span"));
    private static final Locator PROMO_DISCOUNT_TOTAL = new Locator("PROMO_DISCOUNT_TOTAL", AppiumBy.xpath("//*[contains(@class, 'PriceSummary_discount')]"));
    private static final Locator PROMO_DISCOUNT_AMOUNT_ON_PROMO_SECTION = new Locator("PROMO_DISCOUNT_AMOUNT_ON_PROMO_SECTION", AppiumBy.xpath("//*[@data-testid='btnApplyUnapplyPromo']//*[contains(@class,'AppliedPromoText_text')] | //*[contains(@class,'PromoDisplay_text')]//span"));
    private static final Locator UNAPPLY_PROMO_BTN = new Locator("UNAPPLY_PROMO_BTN", AppiumBy.xpath("//*[@data-testid='unapply-promo'] | (//div[contains(@class,'PromoDisplay_content') or contains(@class,'PromoCard_container')]//*[name()='svg']/*[name()='g']//*[name()='use'])[2]"));
    private static final Locator CONFIRM_UNAPPLY_PROMO_BTN = new Locator("CONFIRM_UNAPPLY_PROMO_BTN", AppiumBy.xpath("//span[contains(text(),'Hapus') or contains(text(),'Remove')]//ancestor::button | //button[contains(text(),'Remove Promo') or contains(text(),'Hapus Promo')]"));
    private static final Locator DISCOUNT_AMOUNT_PRICE_DETAIL = new Locator("DISCOUNT_AMOUNT_PRICE_DETAIL", AppiumBy.xpath("//*[contains(@class,'bottom_sheet')]//*[contains(@class,'PriceDetailV4_price_list_item')]//span[contains(text(),'Promo code') or contains(text(),'Kode promo')]//following-sibling::span[last()]"));
    private static final Locator CC_LOADING_INDICATOR = new Locator("CC_LOADING_INDICATOR", AppiumBy.xpath("//svg[contains(@class,'loading')]"));
    private static final Locator NON_IDR_POPUP = new Locator("NON_IDR_POPUP", AppiumBy.xpath("//*[contains(@class,'content')]//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'this product is only available in idr') or contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'produk ini hanya bisa dipesan dengan mata uang idr')]"));
    private static final Locator NON_IDR_POPUP_NATIVE = new Locator("NON_IDR_POPUP_NATIVE", AppiumBy.xpath("//*[contains(translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'this product is only available in idr') or contains(translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'produk ini hanya bisa dipesan dengan mata uang idr')]"));
    private static final Locator SEAT_BUS = new Locator("NON_IDR_POPUP_NATIVE", AppiumBy.xpath("(//*[contains(@class,'styles_seating_arrangement')]//p[contains(@class,'weight_regular')])"));
    private static final Locator SEE_SEAT_BUS = new Locator("SEE_SEAT_BUS", AppiumBy.xpath("(//*[contains(@class,'styles_seat')]//p[contains(@class,'text')])[1]//following-sibling::*"));
    private static final Locator REFUND_DEPOSIT_PAYMENT_METHOD = new Locator("REFUND_DEPOSIT_PAYMENT_METHOD", AppiumBy.xpath("//*[contains(text(),'Deposit Refund') or contains(text(),'Refund Deposit')]"));
    private static final Locator DISCOUNT_PROMO_ICON = new Locator("DISCOUNT_PROMO_ICON", AppiumBy.xpath("//*[@data-testid='btnApplyUnapplyPromo']//*[contains(@class,'ApplyPromoText_icon')] | //*[contains(@class,'PromoDisplay_discount_icon')]"));
    private static final Locator CORPORATE_SUCCESSFULLY_PLACED_ORDER_TITLE_POPUP = new Locator("CORPORATE_SUCCESSFULLY_PLACED_ORDER_TITLE_POPUP", AppiumBy.xpath("//div[contains(@class,'CorporateBillingSuccess_caption')]//h3"));
    private static final Locator CORPORATE_SUCCESSFUL_PAYMENT_TITLE_POPUP = new Locator("CORPORATE_SUCCESSFUL_PAYMENT_TITLE_POPUP", AppiumBy.xpath("//h1[contains(@class,'SuccessV2_popup_mobile_title')]"));
    public static final Locator THANKYOU_PAGE_TEXT = new Locator("THANK_YOU_TEXT", AppiumBy.xpath("//h1[contains(@class,'SuccessV2_popup_mobile_title__')]"));
    public static final Locator SEE_ORDER_LIST_BUTTON = new Locator("SEE_ORDER_LIST_BUTTON", AppiumBy.xpath("//a[contains(@class,'Button_variant_primary__') and contains(@href,'yourorder')]"));
    protected static final Locator SEE_ORDER_LIST_BUTTON_NATIVE_ANDROID_IOS = new Locator("SEE_ORDER_LIST_BUTTON_NATIVE_ANDROID_IOS", AppiumBy.xpath("//*[contains(@text, 'See Order List') or contains(@label, 'See Order List') or contains(@text, 'Lihat daftar pesanan') or contains(@label, 'Lihat daftar pesanan') or contains(@text, 'See order list') or contains(@label, 'See order list') or contains(@text, 'Lihat Daftar Pesanan') or contains(@label, 'Lihat Daftar Pesanan')]"));
    private static final Locator PAYMENT_CONFIRMED = new Locator("PAYMENT_CONFIRMED", AppiumBy.xpath("//*[contains(text(),'Pembayaran Berhasil') or contains(text(),'Payment Success')]"));
    private static final Locator PROMO_CODE_TEXT = new Locator("PROMO_CODE_TEXT", AppiumBy.xpath("//*[@data-testid='btnApplyUnapplyPromo']//p | //*[contains(@class,'PromoCard_container')]//p | //*[contains(@class,'PromoDisplay')]//*[contains(@class,'PromoDisplay_text_wrapper')]//p"));
  private static final Locator BTPL_PROMO_SAVED_BANNER = new Locator("BTPL_PROMO_SAVED_BANNER", AppiumBy.xpath("//*[contains(.,'You saved') or contains(.,'Kamu hemat')]"));
    private static final Locator BTPL_REMOVE_PROMO_BUTTON = new Locator("BTPL_REMOVE_PROMO_BUTTON", AppiumBy.xpath("//*[@aria-label='Remove promo']"));
    private static final Locator PROMO_DISCOUNT_PRICE= new Locator("PROMO_DISCOUNT_PRICE", AppiumBy.xpath("//*[@data-testid='btnApplyUnapplyPromo']//*[contains(@class,'ApplyPromoText_promo_text')] | //*[contains(@class,'PromoDisplay')]//*[contains(@class,'PromoDisplay_text_wrapper')]//span"));
    private static final Locator PROMO_TERMCONDITION = new Locator("TERM_AND_CONDITION", AppiumBy.xpath("//*[contains(@data-testid,'btnManualPromoApplyTnc')]"));
    private static final Locator TERM_AND_CONDITION_BOTTOM_SHEET = new Locator("TERM_AND_CONDITION_BOTTOM_SHEET", AppiumBy.xpath("//*[contains(@data-testid,'bottom-sheet-header')]//*[contains(@class,'BottomSheet_header_title') and (contains(text(),'Details') or contains(text(),'Detail'))]"));
    private static final Locator PROMO_ERROR = new Locator("PROMO_CODE_ERROR", AppiumBy.xpath("//*[contains(@class,'helper_text')]//*[contains(@class,'variant_alert')]"));
    private static final Locator AVB_ERROR = new Locator("AVB_ERROR", AppiumBy.xpath("//*[contains(@class,'figure')]//*[contains(@class,'title')]"));
    private static final Locator AVB_ERROR_SEE_ALL_PROMO = new Locator("AVB_ERROR_SEE_ALL_PROMO", AppiumBy.xpath("//*[contains(@class,'figure')]//*[contains(@class,'button')]"));
    private static final Locator PROMO_ELIGIBLE_LIST = new Locator("PROMO_ELIGIBLE_LIST", AppiumBy.xpath("//*[contains(@class,'EligiblePromos_list')]"));

    private static final Locator BOOK_DETAIL_CONTAINER = new Locator("BOOK_DETAIL_CONTAINER", AppiumBy.xpath("//div[contains(@class,'OrderSummaryItem')]"));
    private static final Locator CLOSE_CONTAINER = new Locator("CLOSE_CONTAINER", AppiumBy.xpath("//*[contains(@class,'close_button')]"));
    private static final Locator GUEST_DETAIL_NAME = new Locator("GUEST_DETAIL_NAME", AppiumBy.xpath("(//*[contains(@class,'SingleOrder_col')]/Span)"));
    private static final Locator DEPARTURE_DATE_PAYMENT_PAGE = new Locator("DEPARTURE_DATE_PAYMENT_PAGE", AppiumBy.xpath("(//*[contains(@class,'OrderSummaryItem_summary_bottom_container')]//*[contains(@class,'Text_text')])[1]"));
    private static final Locator GUEST_NAME_FOR_TRAIN = new Locator("GUEST_NAME_FOR_TRAIN", AppiumBy.xpath("//*[contains(@class,'PassengersAndFacilities_detail_content_container')]//*[contains(@class,'PassengersAndFacilities_detail_header')]/*[contains(@class,'text')]"));
    private static final Locator GUEST_TYPE_FOR_TRAIN = new Locator("GUEST_TYPE_FOR_TRAIN", AppiumBy.xpath("//*[contains(@class,'PassengersAndFacilities_detail_content_container')]//*[contains(@class,'PassengersAndFacilities_detail_header')]/*[contains(@class,'PassengersAndFacilities_label_badge')]/*[contains(@class,'text')]"));
    private static final Locator GUEST_BOOKING_FOR_TRAIN = new Locator("GUEST_BOOKING_FOR_TRAIN", AppiumBy.xpath("//*[contains(@class,'styles_train_detail')]//span[1]"));
    private static final Locator GUEST_SEAT_NO_FOR_TRAIN = new Locator("GUEST_SEAT_NO_FOR_TRAIN", AppiumBy.xpath("//*[contains(@class,'PassengersAndFacilities_detail_content_container')]//*[contains(@class,'PassengersAndFacilities_item_notes')]"));
    private static final Locator ORDER_DETAIL_TAB = new Locator("ORDER_DETAIL_TAB", AppiumBy.xpath("//*[contains(@class,'tab_text') and (contains(text(),'Detail Pesanan') or contains(text(),'Order Details'))]//parent::button[contains(@class,'tab')]"));
    private static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", AppiumBy.xpath("//*[contains(@class,'Hotel_title')]//*[contains(@class,'text') and contains(@class,'weight_regular')]"));
    private static final Locator CHECK_IN_DATE_DETAIL = new Locator("CHECK_IN_DATE_DETAIL", AppiumBy.xpath("//*[contains(@class,'checkin')]//span[2]"));
    private static final Locator NIGHT_DURATION_DETAIL = new Locator("NIGHT_DURATION_DETAIL", AppiumBy.xpath("//*[contains(@class,'duration')]//span"));
    private static final Locator PRICE_DETAIL_EXPAND = new Locator("PRICE_DETAIL_EXPAND", AppiumBy.xpath("(//*[contains(@class,'PriceDetailV4_accordion_item')]//*[contains(@class,'icon_inverted')])[1]"));
    private static final Locator PRICE_DETAIL_EXPAND_2 = new Locator("PRICE_DETAIL_EXPAND_2", AppiumBy.xpath("(//*[contains(@class,'PriceDetailV4_accordion_item')]//*[contains(@class,'icon_inverted')])[2]"));
    private static final Locator TOTAL_PRICE_PRICE_DETAIL = new Locator("TOTAL_PRICE_PRICE_DETAIL", AppiumBy.xpath("//*[contains(@class,'children_wrapper')]//*[contains(@class,'PriceDetailV4_total')]//*[contains(text(),',') or contains(text(),'.') or contains(text(),'Free') or contains(text(),'GRATIS')]"));
    private static final Locator LOGIN_FROM_POPUP_BTN = new Locator("LOGIN_FROM_POPUP_BTN", AppiumBy.xpath("//div[contains(@class, 'LoginBox_gradient_banner_container')]"));
    private static final Locator CLOSE_LOGIN_POPUP_BTN = new Locator("CLOSE_LOGIN_POPUP_BTN", AppiumBy.xpath("//button[contains(text(),'Log in')]"));
    private static final Locator PROMO_SUGGESTION_LIST = new Locator("PRMO_SUGGESTION_LIST", AppiumBy.xpath("//*[contains(@class,'EligiblePromos_list')]"));
    private static final Locator PSV3_CHANGE_METHOD_BTN = new Locator("PSV3_CHANGE_METHOD_BUTTON", AppiumBy.xpath("//div[contains(@class,'pop_up')]//button[contains(text(),'Change Payment Method') or contains(text(),'Ganti metode pembayaran')]"));
    private static final Locator FIRST_PAYMENT_CHECKBOX = new Locator("FIRST_PAYMENT_CHECKBOX", AppiumBy.xpath("(//div[contains(@class,'PaymentTile_selected_payment_checkbox')])[1]"));
    private static final Locator CREDIT_DEBIT_PAYMENT_TILE_RADIO = new Locator("CREDIT_DEBIT_PAYMENT_TILE_RADIO", AppiumBy.xpath(
            "//div[contains(@class,'PaymentTile_selected_payment_tile')][.//*[contains(.,'Credit/Debit Card') or contains(.,'Kartu Kredit/Debit') or contains(.,'Kartu Kredit / Kartu Debit') or contains(.,'Credit Card / Debit Card') or contains(.,'CreditCard')]]//input[@type='radio']"));
    private static final Locator CREDIT_DEBIT_CARD_FORM_INLINE = new Locator("CREDIT_DEBIT_CARD_FORM_INLINE", AppiumBy.xpath(
            "//form[contains(@class,'CardComponent_container')]//input[contains(@name,'cardNumber')]"));
    private static final Locator CREDIT_DEBIT_CHECKMARK_SELECTED = new Locator("CREDIT_DEBIT_CHECKMARK_SELECTED", AppiumBy.xpath(
            "//div[contains(@class,'PaymentTile_selected_payment_tile')][.//*[contains(.,'Credit/Debit Card') or contains(.,'Kartu Kredit/Debit') or contains(.,'Kartu Kredit / Kartu Debit') or contains(.,'Credit Card / Debit Card') or contains(.,'CreditCard')]]//div[contains(@class,'PaymentTile_selected_payment_checkbox')]//*[local-name()='svg' and .//*[@fill='#007bff']]"));
    private static final Locator SAKUKU_PAYMENT_TILE_RADIO = new Locator("SAKUKU_PAYMENT_TILE_RADIO", AppiumBy.xpath(
            "//div[contains(@class,'PaymentTile_selected_payment_tile')][.//*[contains(.,'Sakuku')]]//input[@type='radio']"));
    private static final Locator SAKUKU_CHECKMARK_SELECTED = new Locator("SAKUKU_CHECKMARK_SELECTED", AppiumBy.xpath(
            "//div[contains(@class,'PaymentTile_selected_payment_tile')][.//*[contains(.,'Sakuku')]]//div[contains(@class,'PaymentTile_selected_payment_checkbox')]//*[local-name()='svg']"));
    private static final Locator CHECK_BOX = new Locator("CHECK_BOX", AppiumBy.xpath("//*[contains(@class,'Check_custom_checkbox')]"));
    private static final Locator CONTINUE_REFUND_RESCHEDULE = new Locator("CONTINUE_REFUND_RESCHEDULE", AppiumBy.xpath("//*[contains(@data-testid,'continue-submit-refund')]"));
    private static final Locator GO_TO_ORDER_LIST = new Locator("GO_TO_ORDER_LIST", AppiumBy.xpath("//*[contains(@class,'Button_button') and (contains(text(),'Order') or contains(text(),'pesanan'))]"));
    private static final Locator EARN_TIKET_POINTS = new Locator("EARN_TIKET_POINTS", AppiumBy.xpath("//*[contains(@class,'v4_price_footer_container_sticky')]/child::*[3]"));
    private static final Locator TOTAL_PRICE_FIRST_PAGE = new Locator("TOTAL_PRICE_FIRST_PAGE", AppiumBy.xpath("//*[contains(@class,'PriceSummary_final_price') and not(contains(@class,'wrapper'))]"));
    private static final Locator ELIGIBLE_PROMO = new Locator("ELIGIBLE_PROMO", AppiumBy.xpath("//div[contains(@class, 'Card_card_container') and not(.//button[contains(text(), 'Ganti Pembayaran') or contains(text(), 'Change Method')]) and contains(., 'Voucher')]"));
    private static final Locator NOT_ELIGIBLE_PROMO = new Locator("NOT_ELIGIBLE_PROMO", AppiumBy.xpath("//div[contains(@class, 'Card_card_container')]//button[contains(text(), 'Ganti Pembayaran') or contains(text(), 'Change Method')]/ancestor::div[contains(@class, 'Card_card_container')]\n"));
    private static final Locator PROMO_NAME = new Locator("PROMO_NAME", AppiumBy.xpath(".//div[contains(@class, 'SuggestionItem_description_wrapper')]//p"));
    private static final Locator DISCOUNT_TEXT = new Locator("DISCOUNT_TEXT", AppiumBy.xpath(".//div[contains(@class, 'Label_label')]"));
    private static final Locator PROMO_CARD_DESCRIPTION = new Locator("PROMO_CARD_DESCRIPTION", AppiumBy.xpath(".//div[contains(@class, 'SuggestionItem_code')]//span[contains(@class, 'Text_weight')]"));
    private static final Locator NOT_ELIGIBLE_PROMO_PAYMENT_METHOD = new Locator("NOT_ELIGIBLE_PROMO_PAYMENT_METHOD", AppiumBy.xpath(".//p[contains(@class, 'Text_variant_alert')]"));
    private static final Locator PROMO_T_AND_C = new Locator("PROMO_T_AND_C", AppiumBy.xpath(".//p[contains(@class, 'SuggestionItem_tnc')]"));
    private static final Locator PROMO_VALIDITY = new Locator("PROMO_VALIDITY", AppiumBy.xpath(".//div[contains(@class, 'SuggestionItem_bottom')]/span"));
    private static final Locator PROMO_PAYMENT_METHOD = new Locator("PROMO_PAYMENT_METHOD", AppiumBy.xpath(".//p[contains(@class, 'Text_variant_lowEmphasis')]"));
    private static final Locator BEST_ELIGIBLE_PROMO = new Locator("BEST_ELIGIBLE_PROMO", AppiumBy.xpath(".//div[contains(@class, 'EligiblePromos_description')]//p[contains(@class, 'Text_weight_bold')]"));
    private static final Locator APPLY_PROMO = new Locator("APPLY_PROMO", AppiumBy.xpath("//button[contains(@class, 'EligiblePromos_apply_button')]"));
    private static final Locator CLOSE_PROMO = new Locator("CLOSE_PROMO", AppiumBy.xpath("//button[contains(@class, 'BottomSheet_close_button')]"));
    private static final Locator CONTINUE_WITHOUT_PROMO = new Locator("CONTINUE_WITHOUT_PROMO", AppiumBy.xpath("//button[contains(@class,'Button_variant_primary') and contains(text(),'Promo')]"));
    public static final Locator YES_CONTINUE_MEET_POLICY_BTN = new Locator("YES_CONTINUE_MEET_POLICY_BTN", AppiumBy.xpath("//*[contains(@class,'Button_variant_primary')]//*[contains(text(),'Yes,') or contains(text(),'Ya,')]"));
    public static final Locator ORDER_ID = new Locator("ORDER_ID", AppiumBy.xpath("//*[contains(@name, \"Order ID\")]"));
    public static final Locator ACCOUNT_NUMBER_TEXT_BOX = new Locator("ACCOUNT_NUMBER_TEXT_BOX", AppiumBy.xpath("//*[contains(@class,'AccountInfo_number_box')]//div//h3[contains(@class,'Text_text')]"));
    public static final Locator TOTAL_PRICE_BOX_TEXT_2ND_PAGE = new Locator("TOTAL_PRICE_BOX_TEXT_2ND_PAGE", AppiumBy.xpath("//*[contains(@class,'AccountInfo_number_box')]//span[contains(@class,'Text_text')]"));
    public static final Locator SELECTED_PAYMENT_TYPE_BCA_2ND_PAGE = new Locator("SELECTED_PAYMENT_TYPE_BCA_2ND_PAGE", AppiumBy.xpath("//*[contains(@class,'AccountInfo_title') and contains(text(),'BCA Virtual Account')]"));
    public static final Locator SUBMIT_NATIVE = new Locator("SUBMIT_NATIVE", AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`name IN {\"SUBMIT\", \"KIRIM\"`]"));
    public static final Locator ENTER_CODE = new Locator("ENTER_CODE", AppiumBy.iOSClassChain("**/XCUIElementTypeTextField[`value IN {\"Enter Code Here\", \"Masukkan Kode di Sini\"}`]"));
  public static final Locator CLOSE_BTN_PAYMENT_METHOD = new Locator("CLOSE_BTN_PAYMENT_METHOD", AppiumBy.xpath("(//*[@data-testid='bottom-sheet-close-handle'])[last()]"));
    public static final Locator COMPLETE = new Locator("COMPLETE", AppiumBy.xpath("//*[@type='XCUIElementTypeButton' and @label='COMPLETE']"));
    public static final Locator COMPLETE_ANDROID = new Locator("COMPLETE_ANDROID", AppiumBy.xpath("//*[@resource-id='test-source-authorize-3ds']"));
    public static final Locator OPEN_DETAILS = new Locator("OPEN_DETAILS", AppiumBy.xpath("(//div[@class='OrderSummaryItem_summary_title_container__EzAAx']//div)[1]"));
    public static final Locator DEPARTURE_FLIGHT = new Locator("DEPARTURE_FLIGHT", AppiumBy.xpath("//div[contains(@class,'OrderSummaryItem_summary_label__')]//div[text()='Departure' or text()='Keberangkatan']"));
    public static final Locator CLICK_DETAILS = new Locator("CLICK_DETAILS", AppiumBy.xpath("//span[contains(@class,'RefundReschedule_pointer__')]"));
    public static final Locator REFUND_INFO = new Locator("REFUND_INFO", AppiumBy.xpath("(//div[contains(@class,'box-timeline')]//h6[contains(normalize-space(.),'Refund')])[1]"));
    public static final Locator RESCHEDULE_TAB = new Locator("RESCHEDULE_TAB", AppiumBy.xpath("//div[@id='tab-navigation-reschedule']"));
    public static final Locator RESCHEDULE_INFO = new Locator("RESCHEDULE_INFO", AppiumBy.xpath("(//div[contains(@class,'box-timeline')]//h6[contains(normalize-space(.),'Reschedule')])[1]"));
    public static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON", AppiumBy.xpath("(//button[contains(@class,'BottomSheet_close_button__')])[3]"));
    public static final Locator CLOSE_BUTTON_AGAIN = new Locator("CLOSE_BUTTON_AGAIN", AppiumBy.xpath("(//button[contains(@class,'BottomSheet_close_button__')])[2]"));
    public static final Locator RETURN_FLIGHT = new Locator("RETURN_FLIGHT", AppiumBy.xpath("//div[contains(@class,'OrderSummaryItem_summary_label__')]//div[text()='Return' or text()='Kepulangan']"));
    public static final Locator REFUND = new Locator("REFUND", AppiumBy.xpath("//p[contains(normalize-space(),'Refund not allowed') or contains(normalize-space(),'Refund tidak diperbolehkan')]"));
    public static final Locator RESCHEDULE = new Locator("RESCHEDULE", AppiumBy.xpath("//p[contains(normalize-space(),'Reschedule not allowed') or contains(normalize-space(),'Reschedule tidak diperbolehkan')]"));
    public static final Locator REFUND_RESCHEDULE_NUDGE = new Locator("REFUND_RESCHEDULE_NUDGE", AppiumBy.xpath("//*[contains(@class,'CardWithNudge_header')]"));
    public static final Locator PAYMENT_METHODS = new Locator("PAYMENT_METHODS",AppiumBy.xpath("//p[contains(@class,'Groups_title')]"));

    String promoCode;
    String promoDiscountAmmount;

    public PaymentPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> PaymentPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new PaymentPageAndroid<>(session);}
            case IOS -> {return new PaymentPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void clickLoginFromNativePopup();

    @SneakyThrows
    public void openThirdPartyUrl(String url) {
        Deeplink deeplink = new Deeplink("Third Party URL", url, url, url);
        driverTools.deeplink(deeplink);
    }

    public String getSelectedPayment() {
        driverTools.switchToWebViewUrl("payment");
        String selectedPayment = driverTools.getText(SELECTED_PAYMENT_TEXT).orElseThrow();
        driverTools.switchToNative();
        return selectedPayment;
    }

    public abstract void checkCcOtp();
    public abstract String checkOrderID();

    // Downpay option on payment webview (see ui-dumps/paymentpageen.html, downpayoption.html, downpaybhasha.html)
    private static final Locator DOWNPAY_OPTION_BANNER = new Locator("DOWNPAY_OPTION_BANNER", AppiumBy.xpath("//*[contains(@class,'DownPayPaymentOption_content_wrapper') or contains(@class,'DownPayInfoBanner_wrapper')]"));
    private static final Locator DOWNPAY_INFO_BOTTOMSHEET = new Locator("DOWNPAY_INFO_BOTTOMSHEET", AppiumBy.xpath("//*[contains(@class,'DownPayPaymentInfo_title') or contains(@class,'DownPayPaymentInfo_content_wrapper')]"));
    private static final Locator DOWNPAY_INFO_CLOSE_BTN = new Locator("DOWNPAY_INFO_CLOSE_BTN", AppiumBy.xpath("//*[contains(@class,'DownPayPaymentInfo_dialog')]//*[@data-testid='bottom-sheet-header']//button"));
    private static final Locator DOWNPAY_PAY_UPFRONT_BTN = new Locator("DOWNPAY_PAY_UPFRONT_BTN", AppiumBy.xpath("//button[@data-testid='downpay-button'][.//*[contains(text(),'upfront') or contains(text(),'dulu') or contains(text(),'%')]]"));
    private static final Locator DOWNPAY_BTN_PERCENT_TEXT = new Locator("DOWNPAY_BTN_PERCENT_TEXT", AppiumBy.xpath("(//button[@data-testid='downpay-button']//*[contains(@class,'DownPayButton_button_text')])[1]"));
    private static final Locator DOWNPAY_BTN_AMOUNT_TEXT = new Locator("DOWNPAY_BTN_AMOUNT_TEXT", AppiumBy.xpath("(//button[@data-testid='downpay-button']//*[contains(@class,'DownPayButton_button_text')])[2]"));
    private static final Locator PRICE_SUMMARY_FINAL = new Locator("PRICE_SUMMARY_FINAL", AppiumBy.xpath("//*[@data-testid='price_summary_final_price'] | //span[contains(@class,'PriceSummary_final_price') and not(contains(@class,'wrapper'))]"));
    private static final Locator DOWNPAY_TODAY_AMOUNT = new Locator("DOWNPAY_TODAY_AMOUNT", AppiumBy.xpath("//span[contains(@class,'downpay_main_text')]"));
    private static final Locator DOWNPAY_FINAL_AMOUNT = new Locator("DOWNPAY_FINAL_AMOUNT", AppiumBy.xpath("//span[contains(@class,'downpay_bold_text') and (contains(text(),'IDR') or contains(text(),'Rp'))]"));
    private static final Locator DOWNPAY_ADDONS_BANNER = new Locator("DOWNPAY_ADDONS_BANNER", AppiumBy.xpath("//*[contains(@class,'DownPayPriceAddOnsBanner_wrapper')]"));
    private static final Locator SAKUKU_PAYMENT_TILE = new Locator("SAKUKU_PAYMENT_TILE", AppiumBy.xpath("//div[contains(@class,'PaymentTile_selected_payment_tile')][.//*[contains(.,'Sakuku')]]"));
    private static final Pattern DOWNPAY_PERCENT_PATTERN = Pattern.compile("(\\d+)\\s*%");

    /** Amounts captured from the Downpay info bottomsheet (today / final / optional upgrade fee). */
    public static final class DownPayBottomSheetAmounts {
        public final double todayAmount;
        public final double finalPayment;
        public final double upgradeFee;
        public final double bannerLockAmount;
        /** Sticky payment total captured at the same time as bottomsheet amounts. */
        public final double paymentTotal;

        public DownPayBottomSheetAmounts(double todayAmount, double finalPayment, double upgradeFee,
                                         double bannerLockAmount, double paymentTotal) {
            this.todayAmount = todayAmount;
            this.finalPayment = finalPayment;
            this.upgradeFee = upgradeFee;
            this.bannerLockAmount = bannerLockAmount;
            this.paymentTotal = paymentTotal;
        }
    }

    /** True when payment page header Order ID / payment method container is shown. */
    public boolean isOnPaymentPage() {
        String methodName = "isOnPaymentPage";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.switchToNative();
            Locator androidOrderId = new Locator("ORDER_ID_ANDROID", AppiumBy.xpath("//android.widget.TextView[contains(@text,'Order ID')]"));
            if (driverTools.waitForElementToAppear(ORDER_ID, 8).isPresent()
                    || driverTools.waitForElementToAppear(androidOrderId, 3).isPresent()) {
                return true;
            }
            driverTools.waitForWebview();
            driverTools.switchToWebViewUrl("payment");
            return driverTools.waitForElementToPresence(PAYMENT_PAGE_METHOD_CONTAINER, 15).isPresent();
        } finally {
            driverTools.switchToNative();
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    /** True when the Downpay info banner / option card is shown on the payment page. */
    public boolean isDownPayOptionDisplayed() {
        String methodName = "isDownPayOptionDisplayed";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForWebview();
            driverTools.switchToWebViewUrl("payment");
            driverTools.scrollIntoWebView(DOWNPAY_OPTION_BANNER);
            return driverTools.waitForElementToAppear(DOWNPAY_OPTION_BANNER, 15).isPresent();
        } finally {
            driverTools.switchToNative();
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    /** Opens the Downpay information bottomsheet via the option banner (See details / Lihat detailnya). */
    public void openDownPayOption() {
        String methodName = "openDownPayOption";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForWebview();
            driverTools.switchToWebViewUrl("payment");
            driverTools.scrollIntoWebView(DOWNPAY_OPTION_BANNER);
            driverTools.waitForElementToAppear(DOWNPAY_OPTION_BANNER, 15).orElseThrow();
            driverTools.clickWithJavascriptExecutor(DOWNPAY_OPTION_BANNER);
        } finally {
            driverTools.switchToNative();
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    /** True when the Downpay payment-info bottomsheet is displayed. */
    public boolean isDownPayInfoBottomSheetDisplayed() {
        String methodName = "isDownPayInfoBottomSheetDisplayed";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForWebview();
            driverTools.switchToWebViewUrl("payment");
            return driverTools.waitForElementToAppear(DOWNPAY_INFO_BOTTOMSHEET, 10).isPresent()
                    && driverTools.waitForElementToAppear(DOWNPAY_INFO_CLOSE_BTN, 5).isPresent();
        } finally {
            driverTools.switchToNative();
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    /** Closes the Downpay payment-info bottomsheet. */
    public void closeDownPayInfoBottomSheet() {
        String methodName = "closeDownPayInfoBottomSheet";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForWebview();
            driverTools.switchToWebViewUrl("payment");
            driverTools.waitForElementToAppear(DOWNPAY_INFO_CLOSE_BTN, 10).orElseThrow();
            driverTools.clickWithJavascriptExecutor(DOWNPAY_INFO_CLOSE_BTN);
            driverTools.waitForElementToDisappear(DOWNPAY_INFO_BOTTOMSHEET, 10);
        } finally {
            driverTools.switchToNative();
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    /**
     * True when the sticky Downpay CTA is shown and enabled, e.g. "Pay 25% upfront" / "Bayar 25% dulu".
     */
    public boolean isPayUpfrontDownPayButtonDisplayed() {
        String methodName = "isPayUpfrontDownPayButtonDisplayed";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForWebview();
            driverTools.switchToWebViewUrl("payment");
            Optional<WebElement> btn = driverTools.waitForElementToAppear(DOWNPAY_PAY_UPFRONT_BTN, 10);
            if (btn.isEmpty()) {
                return false;
            }
            WebElement button = btn.get();
            String text = button.getText();
            String disabledAttr = button.getAttribute("disabled");
            String classAttr = Optional.ofNullable(button.getAttribute("class")).orElse("");
            boolean isDisabled = (disabledAttr != null && !disabledAttr.isBlank() && !"false".equalsIgnoreCase(disabledAttr))
                    || classAttr.contains("disabled")
                    || !button.isEnabled();
            logger.log("Downpay button text: " + text + ", disabled: " + isDisabled);
            return !isDisabled
                    && text != null && text.contains("%")
                    && (text.toLowerCase(Locale.ROOT).contains("upfront")
                    || text.toLowerCase(Locale.ROOT).contains("dulu")
                    || text.toLowerCase(Locale.ROOT).contains("pay")
                    || text.toLowerCase(Locale.ROOT).contains("bayar"));
        } finally {
            driverTools.switchToNative();
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    /**
     * Captures Downpay bottomsheet amounts while the info sheet is open.
     * today / final from timeline; upgrade fee from add-ons banner when present; lock amount from option banner.
     * Also captures sticky payment total at the same moment to avoid timing drift after closing the sheet.
     */
    public DownPayBottomSheetAmounts getDownPayBottomSheetAmounts() {
        String methodName = "getDownPayBottomSheetAmounts";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForWebview();
            driverTools.switchToWebViewUrl("payment");
            driverTools.waitForElementToAppear(DOWNPAY_INFO_BOTTOMSHEET, 10).orElseThrow();

            String todayText = driverTools.getText(DOWNPAY_TODAY_AMOUNT).orElseThrow();
            double todayAmount = CoreFunctions.convertPriceSafely(todayText);

            double finalPayment = 0;
            Optional<WebElement> finalEl = driverTools.waitForElementToAppear(DOWNPAY_FINAL_AMOUNT, 5);
            if (finalEl.isPresent()) {
                finalPayment = CoreFunctions.convertPriceSafely(finalEl.get().getText());
            }

            double upgradeFee = 0;
            Optional<WebElement> addOns = driverTools.waitForElementToAppear(DOWNPAY_ADDONS_BANNER, 3);
            if (addOns.isPresent()) {
                String addOnsText = addOns.get().getText();
                String feeText = CoreFunctions.extractPriceAfterMarkers(addOnsText,
                        "additional fee of", "biaya tambahan sebesar", "additional fee", "biaya tambahan");
                upgradeFee = CoreFunctions.convertPriceSafely(feeText);
            }

            double bannerLockAmount = 0;
            Optional<WebElement> banner = driverTools.waitForElementToAppear(DOWNPAY_OPTION_BANNER, 3);
            if (banner.isPresent()) {
                Matcher priceMatcher = Pattern.compile("(?:IDR|Rp)\\s*[\\d.,]+").matcher(banner.get().getText());
                if (priceMatcher.find()) {
                    bannerLockAmount = CoreFunctions.convertPriceSafely(priceMatcher.group());
                }
            }

            String totalText = driverTools.getText(PRICE_SUMMARY_FINAL).orElseGet(() ->
                    driverTools.getText(TOTAL_PRICE).orElseThrow());
            double paymentTotal = CoreFunctions.convertPriceSafely(totalText);

            logger.log(String.format(Locale.ROOT,
                    "Downpay bottomsheet amounts — today: %.0f, final: %.0f, upgradeFee: %.0f, bannerLock: %.0f, paymentTotal: %.0f",
                    todayAmount, finalPayment, upgradeFee, bannerLockAmount, paymentTotal));
            return new DownPayBottomSheetAmounts(todayAmount, finalPayment, upgradeFee, bannerLockAmount, paymentTotal);
        } finally {
            driverTools.switchToNative();
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    /** Percent shown on the Downpay CTA, e.g. 25 from "Pay 25% upfront". */
    public int getDownPayButtonPercent() {
        String methodName = "getDownPayButtonPercent";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForWebview();
            driverTools.switchToWebViewUrl("payment");
            String percentText = driverTools.getText(DOWNPAY_BTN_PERCENT_TEXT).orElseThrow();
            Matcher percentMatcher = DOWNPAY_PERCENT_PATTERN.matcher(percentText);
            if (!percentMatcher.find()) {
                throw new AssertionError("No percent found on Downpay button text: " + percentText);
            }
            int percent = Integer.parseInt(percentMatcher.group(1));
            logger.log("Downpay button percent: " + percent);
            return percent;
        } finally {
            driverTools.switchToNative();
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    /** Amount shown on the Downpay CTA, e.g. IDR 1,267,718 under "Pay 25% upfront". */
    public double getDownPayButtonAmount() {
        String methodName = "getDownPayButtonAmount";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForWebview();
            driverTools.switchToWebViewUrl("payment");
            String amountText = driverTools.getText(DOWNPAY_BTN_AMOUNT_TEXT).orElseThrow();
            double amount = CoreFunctions.convertPriceSafely(amountText);
            logger.log("Downpay button amount: " + amount);
            return amount;
        } finally {
            driverTools.switchToNative();
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    /** Sticky payment summary total (`price_summary_final_price`). */
    public double getPaymentSummaryTotalAmount() {
        String methodName = "getPaymentSummaryTotalAmount";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForWebview();
            driverTools.switchToWebViewUrl("payment");
            String totalText = driverTools.getText(PRICE_SUMMARY_FINAL).orElseGet(() ->
                    driverTools.getText(TOTAL_PRICE).orElseThrow());
            double total = CoreFunctions.convertPriceSafely(totalText);
            logger.log("Payment summary total: " + total);
            return total;
        } finally {
            driverTools.switchToNative();
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    /** Ensures Sakuku tile is selected on the payment page (checkmark, radio, tile tap, or drawer). */
    public void selectSakukuPaymentMethod() {
        String methodName = "selectSakukuPaymentMethod";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForWebview();
            driverTools.switchToWebViewUrl("payment");
            driverTools.waitForElementToPresence(PAYMENT_PAGE_METHOD_CONTAINER, 20);

            if (driverTools.waitForElementToPresence(SAKUKU_CHECKMARK_SELECTED, 3).isPresent()) {
                logger.log("Sakuku tile already selected (SVG checkmark)");
                return;
            }
            if (selectPaymentTileRadio("Sakuku")) {
                return;
            }
            if (driverTools.waitForElementToPresence(SAKUKU_PAYMENT_TILE, 5).isPresent()) {
                driverTools.clickWithJavascriptExecutor(SAKUKU_PAYMENT_TILE);
                if (driverTools.waitForElementToPresence(SAKUKU_CHECKMARK_SELECTED, 5).isPresent()
                        || driverTools.waitForElementToPresence(SAKUKU_PAYMENT_TILE_RADIO, 3).isPresent()) {
                    return;
                }
            }
            driverTools.switchToNative();
            selectPaymentMethodFromTileOrDrawer("Sakuku");
        } finally {
            driverTools.switchToNative();
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    public boolean isPromoCloseButtonDisplayed() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForElementToAppear(CLOSE_PROMO);
        boolean isPresent = driverTools.isElementDisplayed(CLOSE_PROMO);
        driverTools.switchToNative();
        return isPresent;
    }

    public boolean isBestPromoDisplayed() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.scrollIntoWebView(BEST_ELIGIBLE_PROMO);
        driverTools.waitForElementToAppear(BEST_ELIGIBLE_PROMO);
        driverTools.waitForElementToAppear(APPLY_PROMO);
        boolean isDisplayed = driverTools.isElementDisplayed(BEST_ELIGIBLE_PROMO) && driverTools.isElementDisplayed(APPLY_PROMO);
        driverTools.switchToNative();

        return isDisplayed;
    }

    public void verifyFirstPromoCardDetails(Locator locator1, Locator locator2){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment", 3);
        driverTools.scrollIntoWebView(locator1);
        List<WebElement> promoCards = driverTools.elements(locator1).orElseThrow(() -> new RuntimeException("Promo cards not found"));

        if (!promoCards.isEmpty()) {
            WebElement firstPromoCard = promoCards.get(0);

            String promoName = driverTools.findElementInElement(firstPromoCard, PROMO_NAME.by()).getText();
            String discountText = driverTools.findElementInElement(firstPromoCard, DISCOUNT_TEXT.by()).getText();
            String description = driverTools.findElementInElement(firstPromoCard, PROMO_CARD_DESCRIPTION.by()).getText();
            String isValidPaymentMethodPresent = driverTools.findElementInElement(firstPromoCard, locator2.by()).getText();
            String isTermsAndConditionsPresent = driverTools.findElementInElement(firstPromoCard, PROMO_T_AND_C.by()).getText();
            String isValidityPresent = driverTools.findElementInElement(firstPromoCard, PROMO_VALIDITY.by()).getText();

            Assertion.assertThat("Promo name is present", promoName, Matchers.is(not(emptyString())));
            Assertion.assertThat("Discount is present", discountText, Matchers.is(not(emptyString())));
            Assertion.assertThat("Description is present", description, Matchers.is(not(emptyString())));
            Assertion.assertThat("Valid Payment Method is present", isValidPaymentMethodPresent, Matchers.is(not(emptyString())));
            Assertion.assertThat("Terms & Conditions are present", isTermsAndConditionsPresent, Matchers.is(not(emptyString())));
            Assertion.assertThat("Validity information is present", isValidityPresent, Matchers.is(not(emptyString())));

        } else {
            logger.log("No promo cards found.");
        }
        driverTools.switchToNative();

    }

    public boolean checkNotEligiblePromoCodesDisplayed() throws InterruptedException {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment", 3);
        driverTools.scroll(1, 20);
        driverTools.scrollIntoWebView(NOT_ELIGIBLE_PROMO);
        driverTools.waitForElementToAppear(NOT_ELIGIBLE_PROMO);
        if(driverTools.isElementDisplayed(NOT_ELIGIBLE_PROMO)){
            verifyFirstPromoCardDetails(NOT_ELIGIBLE_PROMO, NOT_ELIGIBLE_PROMO_PAYMENT_METHOD);
            return true;
        }
        return false;
    }

    public boolean checkEligiblePromoCodesDisplayed() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment", 3);
        driverTools.waitForElementToAppear(ELIGIBLE_PROMO);
        if(driverTools.isElementDisplayed(ELIGIBLE_PROMO)){
            verifyFirstPromoCardDetails(ELIGIBLE_PROMO, PROMO_PAYMENT_METHOD);
            return true;
        }
        return false;
    }
    public String getProductNamePayment() {
        String hotelName;
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment", 3);
        Optional<WebElement> productName = driverTools.waitForElementToAppear(PRODUCT_NAME_TEXT, 5);
        if (productName.isPresent()) {
            hotelName = driverTools.getText(productName.orElseThrow());
        } else {
            openPriceDetail();
            openOrderDetailTab();
            hotelName = getHotelName();
            closePriceDetail();
        }
        driverTools.switchToNative();
        return hotelName;
    }

    public String getCheckinPayment() {
        String checkinDate;
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment", 3);
        Optional<WebElement> checkinDateElement = driverTools.firstElement(DATE_DURATION_TEXT);
        if (checkinDateElement.isPresent()) {
            checkinDate = driverTools.getText(checkinDateElement.orElseThrow());
        } else {
            openPriceDetail();
            openOrderDetailTab();
            checkinDate = getCheckInDateDetail();
            closePriceDetail();
        }
        driverTools.switchToNative();
        if (checkinDate.toLowerCase().contains("ags")) checkinDate = checkinDate.replace("Ags", "Agu");
        return removeLeadingZeroFromDate(checkinDate);
    }

    public String getDurationPayment() {
        String nightDuration;
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment", 3);
        Optional<WebElement> nightDurationElement = driverTools.lastElement(NIGHT_DURATION_TEXT);
        if (nightDurationElement.isPresent()) {
            nightDuration = driverTools.getText(nightDurationElement.orElseThrow())
                    .replace(" Nights", "")
                    .replace(" Night", "")
                    .replace(" Malam", "");
        } else {
            openPriceDetail();
            openOrderDetailTab();
            nightDuration = getNightDurationDetail();
            closePriceDetail();
        }
        logger.log("nightDuration-> " + nightDuration);
        driverTools.switchToNative();
        return nightDuration;
    }

    public String getTotalPricePayment() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment", 3);
        WebElement totalPriceLocator = driverTools.waitForElementToAppear(TOTAL_PRICE, 10).orElseThrow();
        String totalPrice = driverTools.getText(totalPriceLocator);
        logger.log("totalPrice-> " + totalPrice);
        driverTools.switchToNative();
        return CoreFunctions.normalizePrice(totalPrice);
    }

    public String getGuestDetailName() {
        driverTools.switchToWebView();
        driverTools.waitForElementToAppear(GUEST_DETAIL_NAME).orElseThrow();
        String guestDetailName = driverTools.getText(GUEST_DETAIL_NAME).orElseThrow();
        driverTools.switchToNative();
        return guestDetailName;
    }
    public abstract void clickBackButton();
    public void clickBookDetail() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment", 2);
        driverTools.waitForElementToAppear(BOOK_DETAIL_CONTAINER).orElseThrow();
        driverTools.click(BOOK_DETAIL_CONTAINER);
        driverTools.switchToNative();
    }
    public abstract void clickRecommendedPaymentMethod();
    public String getTotalPrice(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForElementToDisappear(LOADING);
        WebElement priceElement = driverTools.waitForElementToAppear(TOTAL_PRICE,15).orElseThrow();
        String price = driverTools.getText(priceElement);
        for(int i = 0; i < 10; i++) {
            if(!price.equalsIgnoreCase("GRATIS") || !(price.equalsIgnoreCase("FREE"))) {
                break;
            }
            price = driverTools.getText(priceElement);
        }
        logger.log("total price in payment page : " + price);
        driverTools.switchToNative();
        return price;
    }

    public void clickPayButton() {
        driverTools.switchToWebViewUrl("payment");
        driverTools.scrollIntoWebView(SUBMIT_PAYMENT_BUTTON);
        driverTools.swipeUp(2);
        WebElement element = driverTools.waitForElementToAppear(SUBMIT_PAYMENT_BUTTON,10).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(element,3);
        driverTools.waitForElementToAppear(PREVIOUS_BOOKING_POPUP,10).ifPresent(driverTools::click);
        driverTools.switchToNative();
        if (Configs.PLATFORM == Platform.IOS)
            driverTools.waitForElementToPresence(COMPLETE, 40).ifPresent(driverTools::click);
        else
            driverTools.waitForElementToPresence(COMPLETE_ANDROID, 40).ifPresent(driverTools::click);
    }

    public void clickPayButtonWithoutWaitForPreviousBookingPopup() {
        driverTools.switchToWebViewUrl("payment");
        driverTools.swipeToElement(SUBMIT_PAYMENT_BUTTON,Swipe.UP,3);
        driverTools.swipeUp();
        WebElement element = driverTools.waitForElementToAppear(SUBMIT_PAYMENT_BUTTON,10).orElseThrow();
        driverTools.click(element);
        if(driverTools.isElementDisplayed(CORPORATE_SUCCESSFULLY_PLACED_ORDER_TITLE_POPUP)){
            driverTools.switchToNative();
            return;
        }
        driverTools.switchToNative();
    }

    public abstract void clickPayButtonCC();

    public abstract void allowCameraAccessIfPresent(); //FIXME: IMPLEMENTATION PENDING IOS
    
    public abstract void seeOrderList();
    public abstract void seeOrderList(String orderId);

    /**
     * Reschedule / refund flows: the success bottom sheet with the "See Order List" button
     * lives on a different webview depending on whether additional payment was required.
     *   - isFree == true  -> bottom sheet renders on the /my-refund/v4/refunds/&lt;orderId&gt;/... webview
     *   - isFree == false -> bottom sheet renders on the /payment?order_id=&lt;orderId&gt; webview
     * Both URLs contain &lt;orderId&gt;, so the plain seeOrderList(orderId) short-circuits on the
     * first match and lands on the wrong webview for free reschedules. Use this overload from
     * reschedule tests where the free/paid state is known.
     */
    public void seeOrderList(String orderId, boolean isFree) {
        if (isFree) {
            seeOrderList("refunds/" + orderId);
        } else {
            seeOrderList(orderId);
        }
    }
    public abstract boolean isThankYouPageShown();
    public abstract boolean isThankYouPageShown(String orderId);

    /**
     * Extracts the {@code order_id} query param from the thank you page URL, e.g.
     * {@code .../payment/zero_flexible_payment/complete?order_id=1900253250&...}.
     */
    public String getOrderIdFromThankYouPageUrl() {
        driverTools.switchToWebViewUrl("payment");
        String currentUrl = driverTools.getCurrentURL();
        driverTools.switchToNative();
        Matcher matcher = Pattern.compile("[?&]order_id=(\\d+)").matcher(currentUrl);
        Assertion.assertThat("Order id should be found on thank you page URL", matcher.find(), is(true));
        String orderId = matcher.group(1);
        logger.log("Order id from thank you page url -> " + orderId);
        return orderId;
    }

    public boolean checkSeeAllPaymentMethod() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        boolean result = driverTools.waitForElementsToAppear(SEE_ALL_PAYMENT_METHOD_BUTTON_NEXTJS, 20).isPresent();
        driverTools.switchToNative();
        return result;
    }

    public void openPaymentMethod(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        closePriceDetailSheetIfOpen();

        // ----- Step 1: Page-ready guard (variant-independent) -----
        // Before we try to decide which layout we're on, confirm the payment
        // page actually finished rendering. Otherwise a slow load would look
        // identical to a missing "See all" group (variant B) and we'd silently
        // pick the wrong code path.
        //
        // Three independent signals are checked:
        //   (a) URL contains "/payment" - already enforced by switchToWebViewUrl above.
        //   (b) data-testid="payment-method" container is mounted.
        //   (c) data-testid="submit_button" (Pay Now) is mounted - proves the
        //       page is interactive, not still on a skeleton/loader.
        //
        // (b) and (c) are emitted by both variant A (multi-method/drawer) and
        // variant B (single-method/inline form), so they're safe to gate on.
        boolean methodContainerReady =
                driverTools.waitForElementToPresence(PAYMENT_PAGE_METHOD_CONTAINER, 30).isPresent();
        boolean submitButtonReady = methodContainerReady
                && driverTools.waitForElementToPresence(PAYMENT_PAGE_SUBMIT_BUTTON, 10).isPresent();

        if (!methodContainerReady || !submitButtonReady) {
            driverTools.switchToNative();
            throw new RuntimeException(
                    "Payment page did not finish loading: "
                            + (methodContainerReady
                                    ? "Pay Now button (data-testid='submit_button') never rendered."
                                    : "Payment-method container (data-testid='payment-method') never rendered.")
                            + " Aborting before variant detection so we don't misclassify a slow page as variant B.");
        }
        logger.log("Payment page ready (method container + submit button mounted). Detecting layout variant...");

        // ----- Step 2: Variant discrimination -----
        // The presence of the "See all payment methods" group (not the currency)
        // tells us which payment-page layout the backend served:
        //
        //   Variant A - multi-method "drawer" layout
        //     The page lists several tiles + a "See all" expander. Any currency
        //     can hit this (IDR, SGD, USD, ...) when the order is eligible for
        //     multiple methods. We pre-select the first tile so the form is
        //     non-empty, then click "See all" to open the drawer for callers
        //     that want to pick a specific method.
        //
        //   Variant B - single recommended method, inline card form
        //     The page renders only the recommended tile (already auto-selected)
        //     with the card form mounted directly underneath; there is no
        //     "See all" group at all. Clicking the checkbox would TOGGLE the
        //     selection off, so we leave it alone and just return.
        //
        // The wait here is short (10s) because Step 1 already proved the page
        // is rendered - if "See all" were going to appear at all, it would be
        // there by now.
        Optional<WebElement> seeAll =
                driverTools.waitForElementToPresence(SEE_ALL_PAYMENT_METHOD_BUTTON_NEXTJS, 10);

        if (seeAll.isPresent()) {
            logger.log("Variant A detected (multi-method drawer layout).");
            if (driverTools.element(FIRST_PAYMENT_CHECKBOX).isPresent()) {
                driverTools.click(FIRST_PAYMENT_CHECKBOX);
            }
            driverTools.scrollIntoWebView(SEE_ALL_PAYMENT_METHOD_BUTTON_NEXTJS);
            driverTools.clickWithJavascriptExecutor(SEE_ALL_PAYMENT_METHOD_BUTTON_NEXTJS);
        } else {
            logger.log("Variant B detected (single recommended method, inline form). "
                    + "Tile is already auto-selected, no drawer to open.");
        }

        driverTools.switchToNative();
    }

    /**
     * Payment webview: clicks the Credit/Debit {@code PaymentTile} radio (mpayment next/v4).
     * Ensure the payment page is loaded; call {@link #openPaymentMethod()} first if the method drawer is not open.
     */
    public boolean selectCreditDebitPaymentTileRadio() {
        return selectPaymentTileRadio("Credit Card");
    }

    /**
     * Payment webview: clicks the recommended {@code PaymentTile} radio for the given method (mpayment next/v4).
     * Use the same labels as {@link #selectPaymentMethod(String)}, e.g. {@code "Credit Card"}, {@code "Sakuku"}.
     * Only finds tiles already visible on the first screen; use {@link #selectPaymentMethodFromTileOrDrawer(String)} if the method may be behind "See all".
     */
    public boolean selectPaymentTileRadio(String paymentMethod) {
        Locator radioLocator = locatorForPaymentTileRadio(paymentMethod);
        boolean clicked = false;
        try {
            driverTools.waitForWebview();
            driverTools.switchToWebViewUrl("payment");

            driverTools.waitForElementToPresence(PAYMENT_PAGE_METHOD_CONTAINER, 20);

            Optional<WebElement> radio = driverTools.waitForElementToPresence(radioLocator, 5);
            if (radio.isPresent()) {
                driverTools.clickWithJavascriptExecutor(radio.get());
                clicked = true;
            }
            return clicked;
        } finally {
            driverTools.switchToNative();
        }
    }

    /**
     * Selects the payment method: taps the tile radio when Sakuku / Credit Card is already shown upfront; otherwise opens "See all" and uses {@link #selectPaymentMethod(String)}.
     * <p>For non-IDR currencies the Credit/Debit Card tile is often the only method and is
     * pre-selected with an SVG checkmark (no radio button at all). In that case the card form
     * is already mounted inline — we detect this via the checkmark or the inline form and
     * return immediately instead of timing out looking for a radio that doesn't exist.</p>
     */
    public void selectPaymentMethodFromTileOrDrawer(String paymentMethod) {
        if (selectPaymentTileRadio(paymentMethod)) {
            return;
        }

        if (paymentMethod != null
                && paymentMethod.toLowerCase(Locale.ROOT).contains("credit")) {
            try {
                driverTools.waitForWebview();
                driverTools.switchToWebViewUrl("payment");

                boolean checkmarkSelected =
                        driverTools.waitForElementToPresence(CREDIT_DEBIT_CHECKMARK_SELECTED, 3).isPresent();
                if (checkmarkSelected) {
                    logger.log("Credit/Debit Card tile is already selected via SVG checkmark. "
                            + "Skipping payment method selection.");
                    return;
                }

                boolean cardFormAlreadyMounted =
                        driverTools.waitForElementToPresence(CREDIT_DEBIT_CARD_FORM_INLINE, 3).isPresent();
                if (cardFormAlreadyMounted) {
                    logger.log("Credit/Debit Card form is already mounted inline (non-IDR default selection). "
                            + "Skipping payment method selection.");
                    return;
                }
            } finally {
                driverTools.switchToNative();
            }
        }

        openPaymentMethod();
        selectPaymentMethod(paymentMethod);
    }

    private static Locator locatorForPaymentTileRadio(String paymentMethod) {
        if (paymentMethod != null && paymentMethod.toLowerCase(Locale.ROOT).contains("sakuku")) {
            return SAKUKU_PAYMENT_TILE_RADIO;
        }
        return CREDIT_DEBIT_PAYMENT_TILE_RADIO;
    }

    @SneakyThrows
    public String paymentChecker() {
        String submitPaidPayment = hitPaymentChecker();
        logger.log("submitPaidPayment-> " + submitPaidPayment);
        return submitPaidPayment;
    }

    private String hitPaymentChecker() throws ExecutionException, InterruptedException {
        PaymentCheckerApi paymentCheckerApi = new PaymentCheckerApi();
        ApiResponse apiResponse = paymentCheckerApi.hitApi(apiTools);
        Gson gson = new Gson();
        PaymentCheckerResponse paymentCheckerResponse = gson.fromJson(apiResponse.getBody(), PaymentCheckerResponse.class);
        return paymentCheckerResponse.message;
    }

    private void openPaymentMethodGroup(Locator paymentMethodGroup) {
        driverTools.swipeToElement(paymentMethodGroup, Swipe.UP, 3);
        driverTools.click(paymentMethodGroup);
    }

    private void openNoCardInstallment(Locator noCardInstallment) {
        WebElement webElement = driverTools.waitForElementToAppear(noCardInstallment, 15).orElseThrow();
        driverTools.click(webElement);
    }

    private void setPaymentMethodSub(String type) {
        Locator paymentMethodSub = getPaymentMethodTypeLocator(type);
        Optional<WebElement> paymentMethodSubElement = driverTools.waitForElementToAppear(paymentMethodSub,5);
        if(paymentMethodSubElement.isPresent()){
            driverTools.click(paymentMethodSub);
        }else{
            paymentMethodSubElement = driverTools.swipeToElement(paymentMethodSub, Swipe.UP, 3);
            driverTools.click(paymentMethodSubElement.orElseThrow());
        }
    }

    private static final Locator BTPL_PAYMENT_METHOD_SUB = new Locator("BTPL_PAYMENT_METHOD_SUB", AppiumBy.xpath("//*[contains(@class,'Groups_blipay_title_wrapper')]//*[contains(text(),'Blibli Tiket PayLater')]"));

    private void selectBtplPaymentSub() {
        driverTools.scrollIntoWebView(BTPL_PAYMENT_METHOD_SUB);
        driverTools.click(BTPL_PAYMENT_METHOD_SUB);
    }

    private Locator getPaymentMethodLocator(String paymentMethod) {
        return new Locator("PAYMENT_METHOD", AppiumBy.xpath("//p[contains(@class,'Groups_title')  and contains(text(),'"+paymentMethod+"')]"));
    }

    private Locator getPaymentMethodTypeLocator(String paymentMethod) {
        return new Locator("PAYMENT_METHOD_TYPE", AppiumBy.xpath("//*[contains(@class,'Groups_title')]//*[contains(text(),'"+ paymentMethod +"')]"));
    }

    public void deselectSavedCard() {
        driverTools.scrollIntoWebViewOptional(USE_ANOTHER_CARD);
        driverTools.waitForElementToAppear(USE_ANOTHER_CARD,4).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public boolean verifyPaymentMethodExistence(String type){
        logger.log(type + " is Opening!!");
        driverTools.switchToWebViewUrl("payment");
        switch (type) {
            case "GoPay":
                openPaymentMethodGroup(getPaymentMethodLocator("E-Wallet"));
                return isPaymentMethodActive(type);
            case "OVO":
                openPaymentMethodGroup(getPaymentMethodLocator("E-Wallet"));
                return isPaymentMethodActive(type);
            case "BRI Virtual Account":
            case "BCA Virtual Account":
                Locator virtualAccount = getPaymentMethodLocator("Virtual Account");
                openPaymentMethodGroup(virtualAccount);
                return isPaymentMethodActive(type);
            case "No Card Installment":
                Locator noCardInstallment = new Locator("PAYMENT_METHOD", AppiumBy.xpath("(//*[(contains(text(), 'Paylater') or contains(text(),'Cicilan Tanpa Kartu')) or (contains(text(), 'Paylater')  or contains(text(),'No Card Installment'))])[last()]"));
                openNoCardInstallment(noCardInstallment);
                return isPaymentMethodActive("Kredivo");
            case "ATM":
                Locator paymentMethodLocator = getPaymentMethodLocator(type);
                openPaymentMethodGroup(paymentMethodLocator);
                return isPaymentMethodActive(type);
            case "BCA Transfer":
                Locator paymentMethodLocators = getPaymentMethodLocator("Transfer");
                openPaymentMethodGroup(paymentMethodLocators);
                return isPaymentMethodActive(type);
            case "BlibliPay":
            case "blipay":
                openPaymentMethodGroup(getPaymentMethodLocator("E-Wallet"));
                return isPaymentMethodActive("BlibliPay");
            case "Sakuku":
                openPaymentMethodGroup(getPaymentMethodLocator("Instan"));
                return isPaymentMethodActive("Sakuku");
            default:
                driverTools.switchToNative();
                throw new NotImplementedException("Unimplemented payment method! " + type);
        }
    }

    private boolean isPaymentMethodActive(String type) {
        Locator paymentMethodSub = getPaymentMethodTypeLocator(type);
        boolean result;
        Optional<WebElement> paymentMethodSubElement = driverTools.waitForElementToAppear(paymentMethodSub,5);
        if(paymentMethodSubElement.isPresent()){
            result = driverTools.isElementDisplayed(paymentMethodSub);
        }else{
            paymentMethodSubElement = driverTools.swipeToElement(paymentMethodSub, Swipe.UP, 3);
            result = driverTools.isElementDisplayed(paymentMethodSubElement.orElseThrow());
        }
        driverTools.switchToNative();
        return result;
    }

    public void selectPaymentMethod(String type){
        logger.log(type + " is Opening!!");
        driverTools.switchToWebViewUrl("payment");
        switch (type) {
            case "E-Wallet":
                Locator eWallet = getPaymentMethodLocator(type);
                openPaymentMethodGroup(eWallet);
                break;
            case "GoPay":
                openPaymentMethodGroup(getPaymentMethodLocator("E-Wallet"));
                setPaymentMethodSub(type);
                break;
            case "OVO":
                openPaymentMethodGroup(getPaymentMethodLocator("E-Wallet"));
                setPaymentMethodSub(type);
                break;
            case "BRI Virtual Account":
            case "BCA Virtual Account":
                Locator virtualAccount = getPaymentMethodLocator("Virtual Account");
                openPaymentMethodGroup(virtualAccount);
                setPaymentMethodSub(type);
                break;
            case "Credit Card":
                // Variant A (drawer layout) renders a "Groups_title" Credit/Debit
                // tile inside the "See all payment methods" drawer; we must click
                // it to open the inline form. Variant B (single recommended
                // method) renders the credit-card tile auto-selected with the
                // card form already mounted inline, so the Groups_title element
                // doesn't exist at all - clicking would be unnecessary and the
                // long default wait would just stall the test.
                Optional<WebElement> creditCardGroup =
                        driverTools.waitForElementToAppear(CREDIT_CARD_PAYMENT_METHOD, 5);
                if (creditCardGroup.isPresent()) {
                    driverTools.clickWithJavascriptExecutor(CREDIT_CARD_PAYMENT_METHOD);
                    deselectSavedCard();
                    driverTools.waitForElementToAppear(CLOSE_PAYMENT_GROUP, 5).ifPresent(driverTools::click);
                } else {
                    logger.log("Credit Card group title not found - assuming Variant B "
                            + "(single recommended method, inline card form already mounted, "
                            + "tile auto-selected). Skipping drawer click.");
                    deselectSavedCard();
                }
                break;
            case "No Card Installment":
                Locator noCardInstallment = new Locator("PAYMENT_METHOD", AppiumBy.xpath("(//*[(contains(text(), 'Paylater') or contains(text(),'Cicilan Tanpa Kartu')) or (contains(text(), 'Paylater')  or contains(text(),'No Card Installment'))])[last()]"));
                openNoCardInstallment(noCardInstallment);
                setPaymentMethodSub("Kredivo");
                break;
            case "BTPL":
                 Locator btplGroup = new Locator("PAYMENT_METHOD", AppiumBy.xpath("(//*[(contains(text(), 'Paylater') or contains(text(),'Cicilan Tanpa Kartu')) or (contains(text(), 'Paylater')  or contains(text(),'No Card Installment'))])[last()]"));
                openNoCardInstallment(btplGroup);
                selectBtplPaymentSub();
                break;
            case "ATM":
                Locator paymentMethodLocator = getPaymentMethodLocator(type);
                openPaymentMethodGroup(paymentMethodLocator);
                setPaymentMethodSub(type);
                break;
            case "BCA Transfer":
                Locator paymentMethodLocators = getPaymentMethodLocator("Transfer");
                openPaymentMethodGroup(paymentMethodLocators);
                setPaymentMethodSub(type);
                break;
            case "BlibliPay":
            case "blipay":
                openPaymentMethodGroup(getPaymentMethodLocator("E-Wallet"));
                setPaymentMethodSub("BlibliPay");
                break;
            case "Sakuku":
                openPaymentMethodGroup(getPaymentMethodLocator("Instan"));
                setPaymentMethodSub("Sakuku");
                break;
            default:
                throw new NotImplementedException("Unimplemented payment method! " + type);
        }
        driverTools.switchToNative();
    }

    public void verifyInactivePaymentMethod(String paymentType) {
        driverTools.switchToWebViewUrl("payment");
        openPaymentMethodGroup(getPaymentMethodLocator("E-Wallet"));
        Locator loginText = loginTextForInactivePaymentMethod(paymentType);
        WebElement loginTextElement = driverTools.waitForElementToAppear(loginText, 10).orElseThrow();
        String isLoginText = driverTools.getText(loginTextElement);

        logger.log("looking for payment type ->" + paymentType);
        switch (paymentType) {
            case "OVO":
                Assertion.assertThat("Verify login text for inactive payment method", isLoginText,either(containsString("Log In to pay using OVO")).or(containsString("Log in untuk membayar dengan OVO")));
                break;
            default:
                Assertion.assertThat("Verify login text for inactive payment method", isLoginText,either(containsString("Log in to use this payment method")).or(containsString("Log in untuk menggunakan metode pembayaran ini")));
                break;
        }

        driverTools.clickWithJavascriptExecutor(CLOSE_BTN_PAYMENT_METHOD);
        driverTools.waitForElementToDisappear(CLOSE_BTN_PAYMENT_METHOD,4);
        driverTools.clickWithJavascriptExecutor(CLOSE_BTN_PAYMENT_METHOD);
        driverTools.waitForElementToDisappear(CLOSE_BTN_PAYMENT_METHOD,4);
        driverTools.switchToNative();
    }

    private Locator loginTextForInactivePaymentMethod(String paymentType) {
        return new Locator("LOGIN_TEXT_FOR_INACTIVE_PAYMENT_METHOD", AppiumBy.xpath("//*[contains(@class,'variant_disabled') and contains(text(),'"+paymentType+"')]/parent::*/following-sibling::*"));
    }

    public abstract boolean isOVOAsPaymentDisabledInGuest();

    public boolean isPromoCodeSectionExist() {
        driverTools.switchToWebViewUrl("payment");
        WebElement promoCodeSection = driverTools.waitForElementToAppear(PROMO_SECTION, 10).orElseThrow();
        boolean isExist = driverTools.isElementDisplayed(promoCodeSection);
        driverTools.switchToNative();

        return isExist;
    }

    public void seeAllPromoCodes() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForElementToPresence(PROMO_CODE_BUTTON, 20);
        driverTools.scrollIntoWebView(PROMO_CODE_BUTTON);
        driverTools.clickWithJavascriptExecutor(PROMO_CODE_BUTTON);
        driverTools.switchToNative();
    }

    public String getCrossedPriceBefore() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        WebElement crossedPriceBeforeElement = driverTools.waitForElementToPresence(SLASH_PRICE_BEFORE, 10).orElseThrow();
        String price = driverTools.getText(crossedPriceBeforeElement);
        driverTools.switchToWebView();
        return price;
    }

    public String getDiscountPercentage() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        String discount = driverTools.isElementDisplayed(PROMO_DISCOUNT_PERCENTAGE) ?
                driverTools.getText(PROMO_DISCOUNT_PERCENTAGE).orElseThrow() : driverTools.getText(PROMO_DISCOUNT_TOTAL).orElseThrow();
        driverTools.switchToNative();
        return discount;
    }

    public String getDiscountAmountOnPromoCodeSection(String currency) {
        driverTools.switchToWebViewUrl("payment");
        String discountAmount = driverTools.getText(PROMO_DISCOUNT_AMOUNT_ON_PROMO_SECTION).orElseThrow(); // Save IDR 76,500 and get 555 points after your trip is complete
        driverTools.switchToNative();
        discountAmount = discountAmount.split(currency + " ")[1];
        discountAmount = currency + " " + discountAmount.split(" ")[0];
        if (discountAmount.endsWith(".")) {
            discountAmount = discountAmount.replaceAll(".$", "");
        }
        logger.log("getDiscountAmountOnPromoCodeSection discountAmount-> " + discountAmount);
        return discountAmount;
    }

    public boolean isCantCombinePromoLabelExist() {
        driverTools.switchToWebViewUrl("payment");
        boolean result = driverTools.waitForElementToAppear(CANT_COMBINE_PROMO_LABEL).isPresent();
        driverTools.switchToNative();
        return result;
    }

    public void closePriceDetail() {
        driverTools.switchToWebViewUrl("payment");
        if (driverTools.waitForElementToAppear(CLOSE_BTN_PRICE_DETAIL).isPresent()) {
            driverTools.clickOrFallbackToClickWithJavascriptExecutor(CLOSE_BTN_PRICE_DETAIL);
            driverTools.switchToNative();
        } else {
            clickOutsideBottomsheet();
        }
    }

    /**
     * Reads Meals/Makanan line-item price from the open payment Price Detail sheet.
     * Expands accordion sections when needed. Uses {@code currency} in the price locator.
     */
    public double getMealPriceAmountFromPriceDetail(String currency) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        try {
            if (currency == null || currency.isBlank()) {
                throw new IllegalArgumentException("currency must not be blank for meal price detail lookup");
            }
            openPriceDetailSheetIfNeeded();
            expandAllPriceDetailAccordions();

            Locator mealPriceLocator = new Locator("MEAL_PRICE_PRICE_DETAIL", AppiumBy.xpath("(//*[@data-testid='bottomsheet_testing' or contains(@class,'bottom_sheet')]//li[contains(@class,'PriceDetailV4_price_list_item')][.//span[contains(text(),'Meal') or contains(text(),'Makanan')]]//span[contains(text(),'" + currency + "')])[1]"));
            String mealPriceText = driverTools.getText(mealPriceLocator).orElseThrow();
            logger.log("Meal price on payment price detail: " + mealPriceText);
            return CoreFunctions.convertPrice(mealPriceText);
        } finally {
            closePriceDetailSheetIfOpen();
            driverTools.switchToNative();
        }
    }

    /**
     * Returns true when payment price detail contains a line item matching any of {@code keywords}
     * (e.g. Baggage/Bagasi). Opens price detail once, expands RT legs, and always closes afterward.
     */
    public boolean isPriceDetailLineItemDisplayed(String... keywords) {
        return checkPriceDetailLineItemsDisplayed(keywords)[0];
    }

    /**
     * Opens price detail once and checks each keyword group (OR within a group), returning one
     * boolean per group in the same order as {@code keywordGroups}. Always closes the sheet.
     * Use this when asserting multiple addons so the sheet is not reopened between checks
     * (avoids click-intercepted failures).
     */
    public boolean[] checkPriceDetailLineItemsDisplayed(String[]... keywordGroups) {
        boolean[] results = new boolean[keywordGroups.length];
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        try {
            openPriceDetailSheetIfNeeded();
            expandAllPriceDetailAccordions();
            for (int i = 0; i < keywordGroups.length; i++) {
                results[i] = isLineItemPresentInOpenPriceDetail(keywordGroups[i]);
                logger.log("Price detail line item group [" + String.join(",", keywordGroups[i]) + "] found=" + results[i]);
            }
            return results;
        } finally {
            closePriceDetailSheetIfOpen();
            driverTools.switchToNative();
        }
    }

    private boolean isLineItemPresentInOpenPriceDetail(String... keywords) {
        for (String keyword : keywords) {
            String kw = keyword.toLowerCase();
            Locator lineItem = new Locator("PRICE_DETAIL_LINE_" + keyword, AppiumBy.xpath("//*[@data-testid='bottomsheet_testing' or contains(@class,'bottom_sheet') or contains(@class,'Modal_modal') or contains(@class,'PriceDetail')]//*[contains(@class,'PriceDetailV4_price_list_item') or contains(@class,'price_list_item')][.//*[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + kw + "')]] | //*[@data-testid='bottomsheet_testing' or contains(@class,'bottom_sheet') or contains(@class,'Modal_modal')]//*[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + kw + "')]"));
            if (driverTools.waitForElementToPresence(lineItem, 5).isPresent()) {
                return true;
            }
        }
        return false;
    }

    private void openPriceDetailSheetIfNeeded() {
        if (driverTools.waitForElementToPresence(PRICE_DETAIL_BOTTOM_SHEET, 2).isPresent()) {
            logger.log("Price detail bottom sheet already open");
            return;
        }
        driverTools.swipeToElement(PRICE_DETAIL_ARROW_BUTTON, Swipe.UP, 3).orElseThrow();
        try {
            driverTools.click(PRICE_DETAIL_ARROW_BUTTON);
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            logger.log("Price detail arrow click intercepted — using JS click");
            driverTools.clickWithJavascriptExecutor(PRICE_DETAIL_ARROW_BUTTON);
        }
        driverTools.waitForElementToPresence(PRICE_DETAIL_BOTTOM_SHEET, 10).orElseThrow();
    }

    /** Closes Price Details bottomsheet/modal so payment tiles remain clickable. */
    private void closePriceDetailSheetIfOpen() {
        if (driverTools.waitForElementToPresence(PRICE_DETAIL_BOTTOM_SHEET, 1).isEmpty()
                && driverTools.waitForElementToPresence(CLOSE_BTN_PRICE_DETAIL, 1).isEmpty()) {
            return;
        }
        logger.log("Closing price detail sheet");
        for (int i = 0; i < 3; i++) {
            if (driverTools.waitForElementToAppear(CLOSE_BTN_PRICE_DETAIL, 2).isEmpty()) {
                break;
            }
            driverTools.clickWithJavascriptExecutor(CLOSE_BTN_PRICE_DETAIL);
        }
        driverTools.waitForElementToDisappear(PRICE_DETAIL_BOTTOM_SHEET, 5);
    }

    /** Expands collapsed RT price-detail accordions so return-leg addons (e.g. free meal) are visible. */
    private void expandAllPriceDetailAccordions() {
        // icon_inverted marks collapsed (or expand) chevrons; after each expand the next leg becomes [1].
        for (int attempt = 0; attempt < 4; attempt++) {
            if (driverTools.waitForElementToAppear(PRICE_DETAIL_EXPAND, 2).isEmpty()) {
                break;
            }
            driverTools.clickWithJavascriptExecutor(PRICE_DETAIL_EXPAND);
        }
        driverTools.waitForElementToAppear(PRICE_DETAIL_EXPAND_2, 1).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(PRICE_DETAIL_V4_CHEV_BTN, 1).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public String getDiscountAmountOnPriceDetail() {
        driverTools.switchToWebViewUrl("payment");
        WebElement discountAmountElement = driverTools.waitForElementToAppear(DISCOUNT_AMOUNT_PRICE_DETAIL, 10).orElseThrow(); // "- IDR xxx.xxx"
        String discountAmount = driverTools.getText(discountAmountElement).substring(2); // "IDR xxx.xxx"
        driverTools.switchToNative();
        logger.log("getDiscountAmountOnPriceDetail discountAmount-> " + discountAmount);
        return discountAmount;
    }

    public boolean isCorrectPaymentMethodSelected(String paymentType){
        driverTools.switchToWebViewUrl("payment");
        Optional<WebElement> selectedPaymentMethod = driverTools.waitForElementToAppear(getSelectedPaymentMethodLocator(paymentType));
        boolean isCorrectPaymentMethodSelected = selectedPaymentMethod.isPresent();
        logger.log("is correct payment method selected " + isCorrectPaymentMethodSelected);
        driverTools.switchToNative();
        return isCorrectPaymentMethodSelected;
    }

    private Locator getSelectedPaymentMethodLocator(String paymentType) {
        return new Locator(paymentType, AppiumBy.xpath("//*[contains(@class,'PaymentTile_seleted_payment_text')]//*[contains(text(),'"+paymentType+"')]"));
    }

    public abstract boolean isCorrectPaymentMethodChosen(String paymentType);

    public void fillCardDetailPayment(String cardNumber, String expiry, String cvv, String cardName) {
        driverTools.switchToWebViewUrl("payment");

        boolean isIOSPlatform = Configs.PLATFORM == Platform.IOS;

        if (driverTools.waitForElementToAppear(CARD_NUMBER_V3, 5).isPresent()) {
            logger.log("Filling card details using V3 locators" + (isIOSPlatform ? " (iOS - JavaScript)" : ""));
            fillCardField(CARD_NUMBER_V3, cardNumber, isIOSPlatform);
            fillCardField(EXPIRY_V3, expiry, isIOSPlatform);
            fillCardField(CVV_V3, cvv, isIOSPlatform);
            fillCardField(CARD_NAME_V3, cardName, isIOSPlatform);
            driverTools.hideKeyboard(5);
        } else if (driverTools.waitForElementToAppear(CARD_NUMBER, 5).isPresent()) {
            logger.log("Filling card details using original locators" + (isIOSPlatform ? " (iOS - JavaScript)" : ""));
            fillCardField(CARD_NUMBER, cardNumber, isIOSPlatform);
            fillCardField(EXPIRY, expiry, isIOSPlatform);
            fillCardField(CVV, cvv, isIOSPlatform);
            fillCardField(CARD_NAME, cardName, isIOSPlatform);
            driverTools.hideKeyboard();
        }
        if (driverTools.isElementDisplayed(POSTAL_CODE)) {
            fillCardField(POSTAL_CODE, "12345", isIOSPlatform);
        }
        if (driverTools.isElementDisplayed(BILLING_ADDRESS)) {
            fillCardField(BILLING_ADDRESS, "123 Main St, Anytown, USA", isIOSPlatform);
        }
        boolean isLoadingIndiCatorDisplayed = driverTools.waitForElementToAppear(CC_LOADING_INDICATOR, 2).isPresent();
        if (isLoadingIndiCatorDisplayed) {
            driverTools.waitForElementToDisappear(CC_LOADING_INDICATOR, 10);
        }
        driverTools.switchToNative();
    }

    private void fillCardField(Locator locator, String value, boolean isIOSPlatform) {
        if (isIOSPlatform) {
            driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(locator, value);
        } else {
            driverTools.sendKeysKeyboard(locator, value);
        }
    }

    public boolean verifyCCdetails() {
        driverTools.switchToWebViewUrl("payment");
        driverTools.click(INSTALLMENTS_DROPDOWN);
        List<WebElement> installments = driverTools.elements(INSTALLMENTS).orElseThrow();
        logger.log("installment options " + installments.size());

        if (installments.size() > 1) {
            int index = CoreFunctions.generateRandomInt(2, installments.size()-1);
            logger.log(" index "+index);
            driverTools.click(installmentButton(index));
        } else {
            driverTools.click(installmentButton(1));
        }
        driverTools.click(SAVE_INSTALLMENTS_BUTTON);

        String installmentDetail = driverTools.getValue(INSTALLMENTS_DETAIL);
        double total = installmentBreakdown(installmentDetail);
        logger.log("Total: " + total);
        String finalPrice = driverTools.getText(TOTAL_PRICE).orElseThrow();
        String totalAsString = String.valueOf(total);
        Assertion.assertThat("Verify total price is the same ", CoreFunctions.convertPrice(finalPrice), equalTo(CoreFunctions.convertPrice(totalAsString)));
        return CoreFunctions.convertPrice(totalAsString) == CoreFunctions.convertPrice(finalPrice);
    }

    public void  setInstallments(int index) {
        driverTools.switchToWebViewUrl("payment");
        driverTools.click(INSTALLMENTS_DROPDOWN);
        List<WebElement> installments = driverTools.elements(INSTALLMENTS).orElseThrow();
        logger.log("installment options " + installments.size());

        if (installments.size() > 1) {
            logger.log(" index "+index);
            driverTools.click(installmentButton(index));
        }
        driverTools.click(SAVE_INSTALLMENTS_BUTTON);
    }

    private Locator installmentButton(int index){
        return new Locator("INSTALLMENT_BUTTON" + index, AppiumBy.xpath("(//div[contains(@class,'BottomSheet')]//span[contains(@class,'Radio_checkbox')])["+index+"]"));
    }

    public void openInstallmentSelection() {
        driverTools.switchToWebViewUrl("payment");
        driverTools.click(BTPL_INSTALLMENT_DROPDOWN);
    }

    public int getInstallmentTenorCount() {
        List<WebElement> installments = driverTools.elements(INSTALLMENTS).orElseThrow();
        logger.log("installment tenor options " + installments.size());
        if (installments.isEmpty()) {
            logger.fail("No installment tenor options found on BTPL installment selection bottom sheet");
        }
        return installments.size();
    }

    public int getInstallmentZeroInterestLabelCount() {
        int count = driverTools.elements(INSTALLMENT_INTEREST_LABEL).map(List::size).orElse(0);
        logger.log("'Bunga 0%' labels shown on installment sheet: " + count);
        return count;
    }

    public int getInstallmentFeeCount() {
        int count = driverTools.elements(INSTALLMENT_FEE_TEXT).map(List::size).orElse(0);
        logger.log("Tenors showing a PayLater fee: " + count);
        return count;
    }

    public boolean hasInstallmentTenorOptions() {
        boolean hasOptions = getInstallmentTenorCount() > 0;
        if (!hasOptions) {
            logger.fail("No installment tenor options are shown on the installment selection UI");
        }
        return hasOptions;
    }

    public boolean verifyAllInstallmentTenorsShowZeroInterestLabel() {
        int tenorCount = getInstallmentTenorCount();
        int feeCount = getInstallmentFeeCount();
        int expectedZeroInterestCount = tenorCount - feeCount;
        int zeroInterestLabelCount = getInstallmentZeroInterestLabelCount();
        boolean allEligibleShowZeroInterest = tenorCount > 0 && zeroInterestLabelCount == expectedZeroInterestCount;
        if (!allEligibleShowZeroInterest) {
            logger.fail("Expected " + expectedZeroInterestCount + " zero-fee tenor(s) to show 'Bunga 0%' label, but " + zeroInterestLabelCount + " did");
        }
        return allEligibleShowZeroInterest;
    }

    public boolean verifyNoInstallmentTenorShowsZeroInterestLabel() {
        int zeroInterestLabelCount = getInstallmentZeroInterestLabelCount();
        boolean noIncorrectLabel = zeroInterestLabelCount == 0;
        if (!noIncorrectLabel) {
            logger.fail("Bug PLAT-56785 regression: " + zeroInterestLabelCount
                    + " tenor(s) incorrectly show 'Bunga 0%' label without a 0%-interest promo applied");
        }
        return noIncorrectLabel;
    }

    public void selectInstallmentTenor(int index) {
        driverTools.click(installmentButton(index));
    }

    public boolean verifyExactlyOneInstallmentTenorShowsZeroInterestLabel() {
        int tenorCount = getInstallmentTenorCount();
        int feeCount = getInstallmentFeeCount();
        int zeroInterestLabelCount = getInstallmentZeroInterestLabelCount();
        if (zeroInterestLabelCount != 1) {
            logger.fail("Expected exactly 1 tenor to show the 'Bunga 0%' label but " + zeroInterestLabelCount + " did (tenors: " + tenorCount + ", tenors with PayLater fee: " + feeCount + ")");
            return false;
        }
        int labelledRowFeeCount = driverTools.elements(ZERO_INTEREST_TENOR_ROW_FEE_TEXT).map(List::size).orElse(0);
        if (labelledRowFeeCount > 0) {
            String rowText = driverTools.getText(ZERO_INTEREST_TENOR_ROW).orElse("");
            logger.fail("The tenor showing 'Bunga 0%' also charges a PayLater fee: " + rowText);
            return false;
        }
        if (feeCount != tenorCount - 1) {
            logger.fail("Expected every tenor except the labelled one to charge a PayLater fee, but " + feeCount + " of " + tenorCount + " tenors show a fee");
            return false;
        }
        return true;
    }

    public void selectZeroInterestInstallmentTenor() {
        logger.log("Selecting the tenor that shows the 'Bunga 0%' label");
        driverTools.click(ZERO_INTEREST_TENOR_RADIO);
    }

    public void closeInstallmentSelection() {
        driverTools.click(SAVE_INSTALLMENTS_BUTTON);
    }

    public boolean verifyBtplRedirect() {
        driverTools.waitForWebview();
        String currentUrl = driverTools.switchToWebViewUrl("indodana", 3);
        boolean isRedirected = currentUrl != null && currentUrl.toLowerCase().contains("indodana");
        if (isRedirected) {
            logger.log("Redirected to Indodana/BTPL webview: " + currentUrl);
        } else {
            logger.fail("Not redirected to Indodana/BTPL webview after clicking Pay Now. Current URL: " + currentUrl);
        }
        driverTools.switchToNative();
        return isRedirected;
    }

    public String getInstallmentDetail() {
        String detail = driverTools.getValue(INSTALLMENTS_DETAIL);
        if (detail == null || detail.isEmpty()) {
            logger.fail("Installment detail is empty after selecting a tenor - INSTALLMENTS_DETAIL locator returned nothing");
        }
        return detail;
    }

    public static double installmentBreakdown(String installmentDetail) {
        String[] parts = installmentDetail.split(" x ");
        String countString = parts[0];
        String amountString = parts[1].replace("IDR ", "").replace(",", "");
        int count = Integer.parseInt(countString);
        double amount = CoreFunctions.convertPriceSafely(amountString);
        return count * amount;
        }

    public void openPriceDetail() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment", 3);
        driverTools.swipeToElement(PRICE_DETAIL_ARROW_BUTTON, Swipe.UP, 3).orElseThrow();
        driverTools.click(PRICE_DETAIL_ARROW_BUTTON);
        driverTools.switchToNative();
    };

    public void openPriceDetailWebView() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment", 3);
        driverTools.swipeToElement(PRICE_DETAIL_ARROW_BUTTON, Swipe.UP, 3).orElseThrow();
        driverTools.click(PRICE_DETAIL_ARROW_BUTTON);
        driverTools.switchToNative();
    };

    public String getDiscountPrice() {
        driverTools.switchToWebViewUrl("payment", 3);
        driverTools.swipeToElement(PRICE_DETAIL_ARROW_BUTTON, Swipe.UP, 3).orElseThrow();
        driverTools.click(PRICE_DETAIL_ARROW_BUTTON);
        driverTools.waitForElementToAppear(DISCOUNT_PRICE_DETAIL);
        String result = driverTools.getText(DISCOUNT_PRICE_DETAIL).orElseThrow();
        driverTools.switchToNative();
        return result;
    }

    public abstract boolean priceBreakDown(String tiketPoint,String discount);
    public abstract String getAndToogleTiketPoint();
    public abstract boolean verifyTotalAmountSecondPage(String expectedPrice); //FIXME: IMPLEMENTATION PENDING IOS
    public abstract void clickChangePaymentMethodBtnSecondPage();//FIXME: IMPLEMENTATION PENDING IOS
    public abstract void deToogleTiketPoint();//FIXME: IMPLEMENTATION PENDING IOS

    public void applyPromoCode(String promoCode) {
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForElementToAppear(PROMO_CODE_INPUT, 30).orElseThrow();
        driverTools.click(PROMO_CODE_INPUT);
        driverTools.sendKeys(PROMO_CODE_INPUT, promoCode);
        driverTools.click(APPLY_PROMOCODE_BUTTON);
        driverTools.switchToNative();
    }

    public String getPromo(){
        driverTools.switchToWebView();
        WebElement promoCodeText = driverTools.waitForElementToAppear(PROMO_CODE_TEXT, 60).orElseThrow();
        String appliedPromoCode = driverTools.getText(promoCodeText);
        logger.log("appliedPromoCode: " + appliedPromoCode);
        driverTools.switchToNative();
        return appliedPromoCode;
    }

    public boolean isBtplPromoApplied(){
        driverTools.switchToWebViewUrl("payment");
        boolean applied = driverTools.waitForElementToAppear(BTPL_PROMO_SAVED_BANNER, 15).isPresent();
        driverTools.switchToNative();
        return applied;
    }

    public boolean isBtplPromoBannerNotShown(){
        driverTools.switchToWebViewUrl("payment");
        boolean notShown = driverTools.waitForElementToDisappear(BTPL_PROMO_SAVED_BANNER, 10);
        if (!notShown) {
            logger.fail("BTPL promo savings banner ('You saved') is shown although no promo is applied");
        }
        driverTools.switchToNative();
        return notShown;
    }

    public void unApplyBtplPromoCode(){
        driverTools.switchToWebViewUrl("payment");
        driverTools.click(BTPL_REMOVE_PROMO_BUTTON);
        driverTools.switchToNative();
    }

    public String getPriceFromPromo(){
        driverTools.switchToWebView();
        String appliedPromoCodePrice = driverTools.getText(PROMO_DISCOUNT_PRICE).orElseThrow();
        logger.log(appliedPromoCodePrice);
        String priceText = appliedPromoCodePrice.replaceAll("[^\\d.]", "");
        logger.log(priceText);
        driverTools.switchToNative();
        return priceText;
    }
    public abstract void selectPayLater();
    public abstract String getPayLaterLimitPopup();
    public abstract String getPayLaterLimitWebview();
    public abstract boolean checkOtp();

    public void selectRefund(){
        driverTools.switchToWebViewUrl("payment");
        boolean isRefundDeposit = driverTools.waitForElementToAppear(REFUND_DEPOSIT_PAYMENT_METHOD).isPresent();
        if(isRefundDeposit){
            driverTools.scrollIntoWebView(REFUND_DEPOSIT_PAYMENT_METHOD);
            driverTools.clickWithJavascriptExecutor(REFUND_DEPOSIT_PAYMENT_METHOD);
            clickPayButton();
            driverTools.switchToNative();
        }else{
            driverTools.switchToNative();
            openPaymentMethod();
            selectPaymentMethod("ATM");
            clickPayButton();
        }
    }

    public void selectRefundAndClickPay(){
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForElementToAppear(REFUND_DEPOSIT_PAYMENT_METHOD);
        driverTools.waitForElementToAppear(new Locator("CHECK_BOX",AppiumBy.xpath("//*[contains(@class,'selected_payment_checkbox')]"))).ifPresent(driverTools::click);
        driverTools.switchToNative();
        clickPayButton();
    }

    public abstract boolean isVaNumberShow();
    public abstract void addOVOAccount();
    public  abstract boolean verifyLinkageOvo();
    public abstract String getProductNamePayAtHotel();
    public abstract String getCheckinDatePayAtHotel();
    public abstract String getDurationPayAtHotel();
    public abstract String getTotalRoomPayAtHotel();
    public abstract String getTotalGuestPayAtHotel();
    public abstract String getTotalPricePayAtHotel();
    public abstract void clickOrderDetailPayAtHotel();
    public abstract void exitOrderDetailPayAtHotel();
    public abstract void clickPayAtHotelButton();
    public boolean isPaymentConfirmed(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        WebElement paymentConfirmedElement = driverTools.waitForElementToAppear(PAYMENT_CONFIRMED,10).orElseThrow();
        boolean result = driverTools.isElementDisplayed(paymentConfirmedElement);
        driverTools.switchToNative();
        return result;
    }
    public boolean isPaymentSuccessful() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");

        boolean isPaymentSuccessful = driverTools.waitForElementsToAppear(PAYMENT_SUCCESSFUL).isPresent();

        driverTools.switchToNative();

        return isPaymentSuccessful;
    }
    public String getTotalPriceFirstPage() {
        driverTools.switchToWebViewUrl("payment");
        WebElement totalPriceElement = driverTools.waitForElementToAppear(TOTAL_PRICE_FIRST_PAGE).orElseThrow();
        String totalPrice = driverTools.getText(totalPriceElement);
        logger.log("total price first payment page : " + totalPrice);
        driverTools.switchToNative();
        return totalPrice;
    }
    public boolean checkLoyaltyBenefitPriceDetail() {
        driverTools.switchToWebViewUrl("payment");
        boolean status = driverTools.isElementDisplayed(LOYALTY_DISCOUNT);
        driverTools.switchToNative();
        return status;
    }
    public abstract String getTotalPriceFromPriceDetail();

    public void openOrderDetailTab() {
        driverTools.switchToWebViewUrl("payment");
        driverTools.click(ORDER_DETAIL_TAB);
        driverTools.switchToNative();
    }

    public String getHotelName() {
        driverTools.switchToWebView();
        WebElement hotelNameElement = driverTools.waitForElementToAppear(HOTEL_NAME).orElseThrow();
        String hotelName = driverTools.getText(hotelNameElement);
        logger.log("hotelName on the Order Summary-> " + hotelName);
        driverTools.switchToNative();
        return hotelName;
    }

    public String getCheckInDateDetail() {
        driverTools.switchToWebView();
        WebElement checkInDateElement = driverTools.waitForElementToAppear(CHECK_IN_DATE_DETAIL).orElseThrow();
        String checkInDate = driverTools.getText(checkInDateElement);
        logger.log("checkInDate on the Order Summary-> " + checkInDate);
        driverTools.switchToNative();
        return removeLeadingZeroFromDate(replaceShortMonth(checkInDate, "Ag"));
    }

    public String getNightDurationDetail() {
        driverTools.switchToWebView();
        WebElement nightDurationElement = driverTools.waitForElementToAppear(NIGHT_DURATION_DETAIL).orElseThrow();
        String nightDuration = driverTools.getText(nightDurationElement).split(" ")[0];
        logger.log("nightDuration on the Order Summary-> " + nightDuration);
        driverTools.switchToNative();
        return nightDuration;
    }

    public void openGuestDetail(){
        driverTools.switchToWebViewUrl("payment");
        driverTools.click(BOOK_DETAIL_CONTAINER);
        driverTools.switchToNative();
    }

    public void closeGuestDetail(){
        driverTools.switchToWebViewUrl("payment");
        if (driverTools.isElementDisplayed(CLOSE_CONTAINER)) {
            driverTools.click(CLOSE_CONTAINER);
        } else {
            clickOutsideBottomsheet();
        }
        driverTools.switchToNative();
    }

    private Locator stationCodeLocator(int index){
        return new Locator("Origin", AppiumBy.xpath("(//span[contains(@class,'OrderSummaryItem_summary_title')]/div)["+index+"]"));
    }

    private String stationCode(String type){
        int index = switch (type.toLowerCase()) {
            case "origin", "destination" -> 1; // now both comes under one element only
            default -> 0;
        };
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForElementToAppear(stationCodeLocator(index),15);
        WebElement stationCodeElement = driverTools.element(stationCodeLocator(index)).orElseThrow();
        String stationCodeText = driverTools.getText(stationCodeElement);
        driverTools.switchToNative();
        return stationCodeText;
    }

    private String getDepartureDate(){
        driverTools.switchToWebViewUrl("payment");
        WebElement depElement = driverTools.waitForElementToAppear(DEPARTURE_DATE_PAYMENT_PAGE).orElseThrow();
        String date = driverTools.getText(depElement);
        driverTools.switchToNative();
        return date;
    }

    private Locator getStationName(String code){
        return new Locator("STATION_NAME_" + code.toUpperCase(),AppiumBy.xpath("//*[contains(@class,'styles_card_body')]//p[contains(text(),'"+code+"')]"));
    }

    private Locator getTimeTrainLocator(int index){
        return new Locator("TIME_LOCATOR_TRAIN",AppiumBy.xpath("(//*[contains(@class,'ProgressStepVerticalDots_title_content')]//div//h4[contains(text(),':')])["+index+"]"));
    }

    private String getTimeInfoTrain(String stationType){
        int index = 0;
        switch(stationType.toLowerCase()){
            case "origin": index=1;
                break;
            case "destination": index=2;
        }
        WebElement time = driverTools.element(getTimeTrainLocator(index)).orElseThrow();
        return driverTools.getText(time);
    }

    private Locator getDateTrainLocator(int index){
        return new Locator("DATE_LOCATOR_TRAIN",AppiumBy.xpath("(//*[contains(@class,'ProgressStepVerticalDots_title_content')]//div//h4[contains(text(),':')])["+index+"]"));
    }

    private String getDateInfoTrain(String stationType,String year){
        int index = 0;
        switch(stationType.toLowerCase()){
            case "origin": index=1;
                break;
            case "destination": index=2;
        }
        WebElement time = driverTools.element(getDateTrainLocator(index)).orElseThrow();
        return driverTools.getText(time) + " " + year;
    }

    private String formatDate(String date){
        String[] newDate = date.split(" ");
        StringBuilder convertedDate= new StringBuilder();
        for(String x:newDate){
            convertedDate.append(x);
            convertedDate.append(" ");
        }
        date = String.valueOf(convertedDate).substring(0,convertedDate.length()-1);
        return date;
    }

    private String convertSeatTrain(String seat){
        seat = seat.trim();
        return seat;
    }

    private TrainData getGuestInfo(String date, String originStationCode, String destinationStationCode) {
        TrainData data = new TrainData();
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");

        data.departureStation = originStationCode;
        logger.log("Origin station -> " + data.departureStation);
        data.arrivalStation = destinationStationCode;
        logger.log("Destination station -> " + data.arrivalStation);

        data.guestName = driverTools.getText(driverTools.element(GUEST_NAME_FOR_TRAIN).orElseThrow());
        logger.log("Guest Name -> " + data.guestName);
        data.guestType = driverTools.getText(driverTools.element(GUEST_TYPE_FOR_TRAIN).orElseThrow());
        logger.log("Guest Type -> " + data.guestType);

        data.seat = convertSeatTrain(driverTools.getText(driverTools.element(GUEST_SEAT_NO_FOR_TRAIN).orElseThrow()));
        logger.log("Seat No -> " + data.seat);

        // Fetch full date-time string
        String departureDateTime = getTimeInfoTrain("origin");  // Example: "Wed, 19 Feb 2025 - 05:00 2025"
        String arrivalDateTime = getTimeInfoTrain("destination"); // Example: "Wed, 19 Feb 2025 - 08:00 2025"

        // Extract date in "dd MMM" format and time separately
        String[] depParts = departureDateTime.split(" ");
        String[] arrParts = arrivalDateTime.split(" ");

        data.departureDate = depParts[1] + " " + depParts[2];  // "19 Feb"
        data.departYear = depParts[3];  // "2025"
        data.departureTime = departureDateTime.split(" - ")[1].split(" ")[0].trim();  // "05:00"

        data.arrivalDate = arrParts[1] + " " + arrParts[2];  // "19 Feb"
        data.arrivalYear = arrParts[3];  // "2025"
        data.arrivalTime = arrivalDateTime.split(" - ")[1].split(" ")[0].trim();  // "08:00"

        logger.log("Departure Date -> " + data.departureDate);
        logger.log("depart Year -> " + data.departYear);
        logger.log("Departure Time -> " + data.departureTime);
        logger.log("Arrival Date -> " + data.arrivalDate);
        logger.log("arrival Year -> " + data.arrivalYear);
        logger.log("Arrival Time -> " + data.arrivalTime);

        driverTools.switchToNative();
        return data;
    }

    public TrainData getTrainGuestInfoPaymentPage(){
        TrainData data;
        String StationCode = stationCode("destination"); // Get the station code
        StationCode = StationCode.replace("(", "").replace(")", ""); // Remove parentheses
        String[] stations = StationCode.split(" "); // Split by space

        String Origin = stations[0];       // First part is Origin
        String Destination = stations[1];   // Split StationCode into parts
        logger.log("Origin: " + Origin);
        logger.log("Destination: " + Destination);

        String date = getDepartureDate();
        openGuestDetail();
        data = getGuestInfo(date,Origin,Destination);
        closeGuestDetail();
        return data;
    }

    public String getGuestName(){
        driverTools.switchToWebView();
        driverTools.click(BOOK_DETAIL_CONTAINER);
        String guestDetailName = driverTools.getText(GUEST_DETAIL_NAME).orElseThrow();
        driverTools.click(CLOSE_CONTAINER);
        driverTools.switchToNative();
        return guestDetailName;
    }

    public void fillCCDetailRealIssued(String cardNumber, String expiry, String cvv, String cardName) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment", 3);

        if (driverTools.waitForElementToAppear(CARD_NUMBER_V3,5).isPresent()) {
            logger.log("Filling card details using V3 locators");
            driverTools.sendKeysKeyboard(CARD_NUMBER_V3, cardNumber);
            driverTools.sendKeysKeyboard(EXPIRY_V3, expiry);
            driverTools.sendKeysKeyboard(CVV_V3, cvv);
            driverTools.sendKeysKeyboard(CARD_NAME_V3, cardName);
            driverTools.hideKeyboard(5);
        } else if (driverTools.waitForElementToAppear(CARD_NUMBER,5).isPresent()) {
            logger.log("Filling card details using original locators");
            driverTools.sendKeysKeyboard(CARD_NUMBER, cardNumber);
            driverTools.sendKeysKeyboard(EXPIRY, expiry);
            driverTools.sendKeysKeyboard(CVV, cvv);
            driverTools.sendKeysKeyboard(CARD_NAME, cardName);
            driverTools.hideKeyboard();
        }
        if (driverTools.isElementDisplayed(POSTAL_CODE)) {
            driverTools.sendKeysKeyboard(POSTAL_CODE, "12345");
        }
        if (driverTools.isElementDisplayed(BILLING_ADDRESS)) {
            driverTools.sendKeys(BILLING_ADDRESS, "123 Main St, Anytown, USA");
        }
        boolean isLoadingIndiCatorDisplayed = driverTools.waitForElementToAppear(CC_LOADING_INDICATOR,2).isPresent();
        if(isLoadingIndiCatorDisplayed){
            driverTools.waitForElementToDisappear(CC_LOADING_INDICATOR,10);
        }
        driverTools.switchToNative();
    }

    public void fillCCDetailRealIssued(String cardNumber, String expiry, String cvv, String cardName, String postalCode, String billingAddress) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment", 3);

        if (driverTools.waitForElementToAppear(CARD_NUMBER_V3,5).isPresent()) {
            logger.log("Filling card details using V3 locators");
            driverTools.sendKeysKeyboard(CARD_NUMBER_V3, cardNumber);
            driverTools.sendKeysKeyboard(EXPIRY_V3, expiry);
            driverTools.sendKeysKeyboard(CVV_V3, cvv);
            driverTools.sendKeysKeyboard(CARD_NAME_V3, cardName);
            driverTools.hideKeyboard(5);
        } else if (driverTools.waitForElementToAppear(CARD_NUMBER,5).isPresent()) {
            logger.log("Filling card details using original locators");
            driverTools.sendKeysKeyboard(CARD_NUMBER, cardNumber);
            driverTools.sendKeysKeyboard(EXPIRY, expiry);
            driverTools.sendKeysKeyboard(CVV, cvv);
            driverTools.sendKeysKeyboard(CARD_NAME, cardName);
            driverTools.hideKeyboard();
        }
        if (driverTools.isElementDisplayed(POSTAL_CODE)) {
            driverTools.sendKeysKeyboard(POSTAL_CODE, postalCode);
        }
        if (driverTools.isElementDisplayed(BILLING_ADDRESS)) {
            driverTools.sendKeysKeyboard(BILLING_ADDRESS, billingAddress);
        }
        boolean isLoadingIndiCatorDisplayed = driverTools.waitForElementToAppear(CC_LOADING_INDICATOR,2).isPresent();
        if(isLoadingIndiCatorDisplayed){
            driverTools.waitForElementToDisappear(CC_LOADING_INDICATOR,10);
        }
        driverTools.switchToNative();
    }

    public void fillCCDetailRealIssued(String cardNumber, String expiry, String cvv, String cardName, String orderId) {
        if (Configs.PLATFORM == Platform.IOS){
            fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
            return;
        }
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl(orderId);
        if (driverTools.waitForElementToAppear(CARD_NUMBER_V3,5).isPresent()) {
            driverTools.click(CARD_NUMBER_V3);
            driverTools.sendKeysKeyboard(CARD_NUMBER_V3, cardNumber);
            driverTools.click(EXPIRY_V3);
            driverTools.sendKeysKeyboard(EXPIRY_V3, expiry);
            driverTools.sendKeysKeyboard(CVV_V3, cvv);
            driverTools.sendKeysKeyboard(CARD_NAME_V3, cardName);
            driverTools.hideKeyboard(5);
        } else if (driverTools.waitForElementToAppear(CARD_NUMBER,5).isPresent()) {
            driverTools.sendKeys(CARD_NUMBER, cardNumber);
            driverTools.sendKeys(EXPIRY, expiry);
            driverTools.sendKeys(CVV, cvv);
            driverTools.sendKeys(CARD_NAME, cardName);
            driverTools.hideKeyboard();
        }
        if (driverTools.isElementDisplayed(POSTAL_CODE)) {
            driverTools.sendKeys(POSTAL_CODE, "12345");
        }
        if (driverTools.isElementDisplayed(BILLING_ADDRESS)) {
            driverTools.sendKeys(BILLING_ADDRESS, "123 Main St, Anytown, USA");
        }
        boolean isLoadingIndiCatorDisplayed = driverTools.waitForElementToAppear(CC_LOADING_INDICATOR,2).isPresent();
        if(isLoadingIndiCatorDisplayed){
            driverTools.waitForElementToDisappear(CC_LOADING_INDICATOR);
        }
        driverTools.switchToNative();
    }

    public void verifyCreditCard3ds() {
        // Some cards / rails succeed without a 3DS challenge. Avoid multi-minute acs/stripe/2c2p
        // webview probes when thank-you is already (or quickly) available.
        if (isPaymentAlreadySuccessfulQuick(4)) {
            logger.log("3DS not required — payment success already shown; skipping challenge handling");
            driverTools.switchToNative();
            return;
        }

        // Native OTP sheet (short wait — default presence wait is 10s×env and dominates when absent)
        if (driverTools.waitForElementToPresence(ENTER_CODE, 3).isPresent()) {
            driverTools.sendKeys(ENTER_CODE, "1234");
            driverTools.click(SUBMIT_NATIVE);
            if (isPaymentAlreadySuccessfulQuick(5)) {
                driverTools.switchToNative();
                return;
            }
        }

        // Do not early-return when waitForWebview() is false: a slow 3DS context can still appear
        // during switchToWebViewUrl (previous behaviour ignored the boolean and continued).
        if (!driverTools.waitForWebview()) {
            logger.log("No 3DS webview yet after initial wait — still probing acs/stripe/2c2p briefly");
        }

        // Airwallex sandbox 3DS challenge (OTP 1234), rendered inside the issuer iframe
        String airwallexUrl = driverTools.switchToWebViewUrl("airwallex", 1);
        if (airwallexUrl != null && airwallexUrl.contains("airwallex")) {
            boolean inIframe = false;
            if (!driverTools.waitForElementToAppear(OTP_AIRWALLEX_INPUT, 3).isPresent()) {
                try {
                    driverTools.switchToIframe(driverTools.element(IFRAME_AIRWALLEX).orElseThrow());
                    inIframe = true;
                } catch (Exception e) {
                    logger.log("Failed switching to Airwallex iFrame: " + e.getMessage());
                }
            }
            if (driverTools.waitForElementToAppear(OTP_AIRWALLEX_INPUT, 5).isPresent()) {
                driverTools.click(OTP_AIRWALLEX_INPUT);
                driverTools.sendKeys(OTP_AIRWALLEX_INPUT, "1234");
                driverTools.click(SUBMIT_AIRWALLEX);
                if (inIframe) {
                    driverTools.switchBackFromIframe();
                }
                driverTools.switchToNative();
                return;
            }
            if (inIframe) {
                driverTools.switchBackFromIframe();
            }
        }

        // maxTry=1: each missing provider used to retry ~3× across all contexts (minutes each).
        String currentUrl = driverTools.switchToWebViewUrl("acs", 1);
        if (currentUrl != null && currentUrl.contains("acs")
                && driverTools.waitForElementToAppear(SUBMIT, 5).isPresent()) {
            driverTools.click(SUBMIT);
            driverTools.switchToNative();
            return;
        }

        if (isPaymentAlreadySuccessfulQuick(3)) {
            driverTools.switchToNative();
            return;
        }

        currentUrl = driverTools.switchToWebViewUrl("stripe", 1);
        if (currentUrl != null && currentUrl.contains("stripe")
                && driverTools.waitForElementToPresence(IFRAME_STRIPE_1, 5).isPresent()) {
            try {
                driverTools.switchToIframe(driverTools.element(IFRAME_STRIPE_1).orElseThrow());
                driverTools.switchToIframe(driverTools.element(IFRAME_STRIPE_2).orElseThrow());
            } catch (Exception e) {
                logger.log("Failed switching to iFrame: " + e.getMessage());
                if (Configs.PLATFORM == Platform.IOS) {
                    submitStripeNative();
                    return;
                }
            }
            if (driverTools.waitForElementToAppear(SUBMIT_STRIPE, 5).isPresent()) {
                driverTools.clickWithJavascriptExecutor(SUBMIT_STRIPE);
                driverTools.switchBackFromIframe();
                driverTools.switchBackFromIframe();
                driverTools.switchToNative();
                return;
            }
            submitStripeNative();
            return;
        }

        if (isPaymentAlreadySuccessfulQuick(3)) {
            driverTools.switchToNative();
            return;
        }

        currentUrl = driverTools.switchToWebViewUrl("2c2p", 1);
        if (currentUrl != null && currentUrl.contains("2c2p")
                && driverTools.waitForElementToAppear(OTP_2C2P_INPUT, 5).isPresent()) {
            driverTools.click(OTP_2C2P_INPUT);
            driverTools.sendKeys(OTP_2C2P_INPUT, "123456");
            driverTools.click(SUBMIT_2C2P);
            driverTools.switchToNative();
            return;
        }

        logger.log("No acs/stripe/2c2p 3DS challenge found — assuming payment continued without it");
        driverTools.switchToNative();
    }

    /**
     * Fast probe for thank-you / success UI so callers can skip 3DS when payment already finished.
     * Uses short webview + presence waits only (does not use the 60s thank-you waiter).
     * Always returns to native so callers can safely look for native 3DS sheets next.
     */
    private boolean isPaymentAlreadySuccessfulQuick(int seconds) {
        try {
            String url = driverTools.switchToWebViewUrl("payment", 1);
            if (url != null && !url.isBlank() && url.contains("payment")) {
                if (driverTools.waitForElementToPresence(THANKYOU_PAGE_TEXT, seconds).isPresent()) {
                    logger.log("Payment success (thank-you) detected on payment webview");
                    return true;
                }
            }
            // Success may already be the active webview without "payment" in the URL probe target.
            if (driverTools.waitForElementToPresence(THANKYOU_PAGE_TEXT, Math.min(seconds, 2)).isPresent()) {
                logger.log("Payment success (thank-you) detected on current webview");
                return true;
            }
        } catch (Exception e) {
            logger.log("Quick payment-success check skipped: " + e.getMessage());
        } finally {
            try {
                driverTools.switchToNative();
            } catch (Exception ignored) {
                logger.log("switchToNative after quick success check failed: " + ignored.getMessage());
            }
        }
        return false;
    }

    public abstract void submitStripeNative();
    
    public  boolean priceBreakDown(){
        driverTools.switchToWebViewUrl("payment");
        if(driverTools.waitForElementToAppear(PRICE_DETAIL_V4_CHEV_BTN).isPresent()) {
            driverTools.click(PRICE_DETAIL_V4_CHEV_BTN);
        }
        List<WebElement> prices;
        if(driverTools.waitForElementToAppear(PRICE_LOCATOR).isPresent()){
            prices = driverTools.elements(PRICE_LOCATOR).orElseThrow();
        }else{
            prices = driverTools.elements(PRICE_LOCATOR_NFT).orElseThrow();
        }
        ArrayList<Long> arrayPrice = new ArrayList<>();
        for (WebElement element : prices) {
            String text = driverTools.getText(element);
            logger.log("price got: " + text);
            boolean isContainNumber = text.matches(".*[0-9]+.*");
            if(isContainNumber) {
                if (text.isEmpty()) text = "0";
                long price = removeText(text);
                logger.log("price got: " + price);
                arrayPrice.add(price);
            }
        }

        long actualTotalPrice = 0;
        for (long i : arrayPrice) {
            actualTotalPrice += i;
        }

        String totalPriceText;
        if(driverTools.isElementDisplayed(TOTAL_PRICE_FROM_PRICE_DETAIL)){
            totalPriceText = driverTools.getText(TOTAL_PRICE_FROM_PRICE_DETAIL).orElseThrow();
        }else{
            totalPriceText = driverTools.getText(TOTAL_PRICE_NFT_FROM_PRICE_DETAIL).orElseThrow();
        }
        long expectedTotalPrice = removeText(totalPriceText);
        logger.log("actual total: " + actualTotalPrice);
        logger.log("expected total: " + expectedTotalPrice);

        Assertion.assertThat("Total price summary should be equal total price label and should be more than 0", actualTotalPrice, allOf(equalTo(expectedTotalPrice), greaterThan(0L)));
        closePriceDetailSheetIfOpen();
        driverTools.switchToNative();
        return true;
    }

    private void clickOutsideBottomsheet() {
        logger.log("Close button of price detail is not found, click outside the bottom sheet to close it");
        Double bottomSheetTopRatio = driverTools.getBottomSheetTopRatio(BOTTOMSHEET_CONTAINER);
        driverTools.switchToNative();
        if (bottomSheetTopRatio != null) {
            driverTools.clickAboveBottomSheet(bottomSheetTopRatio);
        }
    }

    public String getDepartAndArrivalStationPriceDetail(){
        driverTools.switchToWebViewUrl("payment");
        WebElement departArrivalElement = driverTools.waitForElementToAppear(DEPART_ARRIVAL_STATION_TEXT_PRICE_DETAIL).orElseThrow();
        String departArrivalText = driverTools.getText(departArrivalElement);
        logger.log("Depart - Arrival station on the Price Detail-> " + departArrivalText);
        driverTools.switchToNative();
        return departArrivalText;
    }

    public void checkNotifyPreviousOrderHasNotBeenCompleted() {
        driverTools.waitForElementToAppear(CONTINUE_TO_PAYMENT_BUTTON, 5).ifPresent(driverTools::click);
    }

    public String getLoyaltyDiscountAmount(){
        driverTools.switchToWebView();

        WebElement loyaltyDiscount = driverTools.waitForElementToAppear(LOYALTY_DISCOUNT).orElseThrow();
        String loyaltyDiscountAmount = driverTools.getText(loyaltyDiscount);
        loyaltyDiscountAmount = loyaltyDiscountAmount.replace(" ","");
        logger.log("Discount amount "+loyaltyDiscountAmount);

        driverTools.switchToNative();

        return loyaltyDiscountAmount;
    }

    public String getProductName() {
        driverTools.switchToWebView();
        driverTools.waitForElementToDisappear(LOADING);
        WebElement productName = driverTools.waitForElementToPresence(PRODUCT_NAME_TITLE).orElseThrow();
        String name = driverTools.getText(productName);
        logger.log("Product Name-> " + name);
        driverTools.switchToNative();
        return name;
    }

    private Locator getProductNameByIndex(int index){
        return new Locator("PRODUCT_NAME_TITLE", AppiumBy.xpath("(//p[contains(@class,'order_title')]//span)["+index+"]"));
    }

    public String getProductNameFlight(String tripType) {
        driverTools.switchToWebViewUrl("payment");
        WebElement name1Element = driverTools.waitForElementToPresence(getProductNameByIndex(1)).orElseThrow();
        WebElement name2Element = driverTools.waitForElementToPresence(getProductNameByIndex(2)).orElseThrow();
        String name1 = driverTools.getText(name1Element);
        String name2 = driverTools.getText(name2Element);
        logger.log("Product Name 1-> " + name1);
        logger.log("Product Name 1->" + name2);
        String productName = name1 + " - " + name2;
        logger.log("Product Name->" + productName);
        driverTools.switchToNative();
        return productName;
    }

    public String getTotalRoomHotel() {
        driverTools.switchToWebView();
        WebElement totalRoom = driverTools.waitForElementToPresence(TOTAL_ROOM_HOTEL).orElseThrow();
        String total = driverTools.getText(totalRoom);
        logger.log("Total Room-> " + total);
        driverTools.switchToNative();
        return total;
    }

    public String getTotalGuestHotelDetail() {
        driverTools.switchToWebView();
        List<WebElement> totalGuest = driverTools.elements(TOTAL_GUEST_HOTEL_DETAIL).orElseThrow();
        String total = String.valueOf(totalGuest.size());
        logger.log("Total Guest-> " + total);
        driverTools.switchToNative();
        return total;
    }

    public void verifyDetailsTrainAndPassenger(TrainAndWhooshGeneralData generalData) {
        verifyTrainTimings(generalData);
        verifySeatDetails(generalData);
        verifyPassengerName(generalData);
        verifyTotalPayment(generalData);
        verifyOriginDestinationCity(generalData);
        verifyStationCodesPriceDetail(generalData);
    }

    private void verifyTrainTimings(TrainAndWhooshGeneralData data) {
        TrainData booking = data.trainDataBookingPage;
        TrainData payment = data.trainDataPaymentPage;

        logger.log("departure date booking: " + booking.departureDate);
        logger.log("departure date payment: " + payment.departureDate);
        Assertion.assertThat("departure date match", payment.departureDate, containsString(booking.departureDate));

        logger.log("departure time booking: " + booking.departureTime);
        logger.log("departure time payment: " + payment.departureTime);
        Assertion.assertThat("departure time match", payment.departureTime, is(booking.departureTime));

        logger.log("arrival date booking: " + booking.arrivalDate);
        logger.log("arrival date payment: " + payment.arrivalDate);
        Assertion.assertThat("arrival date match", payment.arrivalDate, containsString(booking.arrivalDate));

        logger.log("arrival time booking: " + booking.arrivalTime);
        logger.log("arrival time payment: " + payment.arrivalTime);
        Assertion.assertThat("arrival time match", payment.arrivalTime, containsString(booking.arrivalTime));
    }

    private void verifySeatDetails(TrainAndWhooshGeneralData data) {
        TrainData booking = data.trainDataBookingPage;
        TrainData payment = data.trainDataPaymentPage;

        logger.log("seat booking: " + booking.seat);
        logger.log("seat payment: " + payment.seat);

        // Extract only seat numbers (like "1 - 20B" or "1 / 20B")
        String bookingSeatNum = extractSeatNumber(booking.seat);
        String paymentSeatNum = extractSeatNumber(payment.seat);

        logger.log("booking seat num: " + bookingSeatNum);
        logger.log("payment seat num: " + paymentSeatNum);

        Assertion.assertThat("seat number match",
                paymentSeatNum.replace("-", "/").trim(),
                equalToIgnoringCase(bookingSeatNum.replace("-", "/").trim()));
    }

    private String extractSeatNumber(String seat) {
        // Find first digit and take the substring from their till end
        Matcher matcher = Pattern.compile("(\\d+\\s*[-/]?\\s*\\d*[A-Z]?)").matcher(seat);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return seat.trim(); // fallback
    }

    private void verifyPassengerName(TrainAndWhooshGeneralData data) {
        TrainData booking = data.trainDataBookingPage;
        TrainData payment = data.trainDataPaymentPage;

        logger.log("name booking: " + booking.guestName);
        logger.log("name payment: " + payment.guestName);

        String bookingName = CoreFunctions.removeTitleFromFullName(booking.guestName).trim();
        String paymentName = payment.guestName.trim();
        Assertion.assertThat("name match", paymentName, containsStringIgnoringCase(bookingName));
    }

    private void verifyTotalPayment(TrainAndWhooshGeneralData data) {
        logger.log("total booking: " + data.totalPaymentBookingPage);
        logger.log("total payment: " + data.totalPaymentPaymentPage);

        Assertion.assertThat("currency should be same", data.totalPaymentPaymentPage.split(" ")[0], is(data.totalPaymentBookingPage.split(" ")[0]));
        Assertion.assertThat("total payment match",CoreFunctions.convertPriceSafely(data.totalPaymentPaymentPage), is(CoreFunctions.convertPriceSafely(data.totalPaymentBookingPage)));
    }

    private void verifyOriginDestinationCity(TrainAndWhooshGeneralData data) {
        String originCity = data.originCityName;
        String destinationCity = data.destinationCityName;
        TrainData payment = data.trainDataPaymentPage;

        logger.log("origin city booking: " + originCity);
        logger.log("origin city payment: " + payment.departureStation);

        logger.log("destination city booking: " + destinationCity);
        logger.log("destination city payment: " + payment.arrivalStation);

        Assertion.assertThat("origin city match with paymentPage", payment.departureStation.toLowerCase(), containsString(originCity.toLowerCase()));
        Assertion.assertThat("destination city match", payment.arrivalStation.toLowerCase(), containsString(destinationCity.toLowerCase()));
    }

    private void verifyStationCodesPriceDetail(TrainAndWhooshGeneralData data) {
        TrainData booking = data.trainDataBookingPage;
        String[] departArriveData = getDepartAndArrivalStationPriceDetailSplit(data.departAndArrivalStationPriceDetail);
        String departStationPaymentPage = departArriveData[0];
        String arriveStationPaymentPage = departArriveData[1];

        logger.log("origin station booking: " + booking.departureStation);
        logger.log("origin station payment: " + departStationPaymentPage);

        logger.log("destination station booking: " + booking.arrivalStation);
        logger.log("destination station payment: " + arriveStationPaymentPage);

        Assertion.assertThat("origin station match" , departStationPaymentPage, containsString(booking.departureStation));
        Assertion.assertThat("destination station match", arriveStationPaymentPage, containsString(booking.arrivalStation));
    }

    /**
     * Extracts departure and arrival station names from the price detail text.
     * Example text: "Padalarang → Halim"
     *
     * @return String array of size 2 → [0]=Departure, [1]=Arrival
     */
    private String[] getDepartAndArrivalStationPriceDetailSplit(String departArrival) {
        if (departArrival == null || !departArrival.contains("→")) {
            logger.log("Invalid or missing station text: " + departArrival);
            return new String[] {"", ""};
        }

        String[] parts = departArrival.split("→", 2);
        return new String[] {
                parts[0].trim(),
                parts.length > 1 ? parts[1].trim() : ""
        };
    }

    public String getValidityPeriod() {
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForElementToAppear(VALIDITY_PERIOD);
        String text = driverTools.getText(VALIDITY_PERIOD).orElseThrow();
        driverTools.switchToNative();
        return text;
    }

    public void openOrderDetail() {
        driverTools.switchToWebViewUrl("payment");
        Optional<WebElement> bookDetailContainer = driverTools.waitForElementToPresence(BOOK_DETAIL_CONTAINER);
        if (bookDetailContainer.isPresent()) {
            driverTools.click(bookDetailContainer.orElseThrow());
        } else {
            openPriceDetail();
            openOrderDetailTab();
        }
        driverTools.switchToNative();
    }

    public void openLoginPage(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        WebElement loginPageBtn = driverTools.waitForElementToAppear(PAYMENT_PAGE_LOGIN_BTN).orElseThrow();
        driverTools.click(loginPageBtn);
        driverTools.switchToNative();
    }

    public boolean isLoginContainerDisplayed(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForElementToDisappear(PAYMENT_PAGE_LOGIN_BTN, 30);
        boolean loginContainer = driverTools.isElementDisplayed(PAYMENT_PAGE_LOGIN_BTN);
        driverTools.switchToNative();
        return loginContainer;
    }

    public boolean offerBreakdown(){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        if(driverTools.isElementDisplayed(REFUND_BANNER)){
            driverTools.click(PRICE_DETAIL);
        }
        driverTools.waitForElementToPresence(PRICE_DETAIL_EXPAND).ifPresent(driverTools::click);
        List<WebElement> refund100P = driverTools.elements(PRICE_DETAIL_100P_REFUND_PAYMENT_PAGE).orElseThrow();
        boolean is100PRefundSizeCorrect = refund100P.size() >= 1;
        logger.log("size of 100p : " + refund100P.size());
        driverTools.switchToNative();
        return is100PRefundSizeCorrect;
    }

    /**
     * Checks if the correct number of seats are selected in the payment page.
     * It waits for the webview, switches to the webview with URL "payment", and checks if the seat locator element is displayed.
     * If the seat locator element is displayed, it clicks on the PRICE_DETAIL_EXPAND element.
     * Finally, it checks again if the seat locator element is displayed and returns the result.
     *
     * @param priceDetail the PriceDetail object containing the number of adults and children.
     * @return true if the correct number of seats are selected, false otherwise.
     */
    public void isCorrectNoOfSeatSelected(PriceDetail priceDetail){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        if(!driverTools.isElementDisplayed(getSeatLocator(priceDetail.adult + priceDetail.child))) {
            driverTools.click(PRICE_DETAIL_EXPAND);
        }
        Assertion.assertThat("Is selected seat passenger number",driverTools.isElementDisplayed(getSeatLocator(priceDetail.adult + priceDetail.child)),is(true));
    }

    /**
     * Returns a Locator object for the seat element in the webview.
     * The Locator object is created with the xpath of the seat element, which contains the number of seats.
     *
     * @param amount the number of seats.
     * @return a Locator object for the seat element.
     */
    private Locator getSeatLocator(int amount){
        return new Locator("SEAT_LOCATOR",AppiumBy.xpath("//*[contains(@class, 'price_list_title') and contains(text(),'Kursi (x"+amount+")') or contains(text(),'Seat (x"+amount+")')]"));
    }

    public void payWithTiketPoint(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.swipeUp(4);
        WebElement tiketPoint = driverTools.waitForElementToAppear(PAY_WITH_TIKET_POINT_TOGGLE,2).isPresent() ?
                driverTools.element(PAY_WITH_TIKET_POINT_TOGGLE).orElseThrow() : driverTools.element(PAY_WITH_TIKET_POINT_CHECKBOX).orElseThrow();
        driverTools.click(tiketPoint);
        //otp turn off
        // boolean driverTools.waitForElementToAppear(PAY_WITH_TIKET_POINT_OTP_CONFIRMATION,10).isPresent();
        // driverTools.sendKeys(PAY_WITH_TIKET_POINT_OTP_CONFIRMATION, "1234");
        driverTools.switchToNative();
    }

    public int getRedeemableTiketPointsOnPaymentPage() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.scrollIntoWebView(TIKET_POINTS_VALUE);
        String rawValue = driverTools.waitForElementToAppear(TIKET_POINTS_VALUE, 10)
                .map(driverTools::getText)
                .orElseThrow(() -> new AssertionError("Redeemable tix points value not shown on payment page"));
        driverTools.switchToNative();
        return (int) CoreFunctions.convertPriceSafely(rawValue.replaceAll("[^0-9]", ""));
    }

    public void verifyTiketPointsCapInfo(String expectedSnippet) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.scrollIntoWebView(TIKET_POINTS_CAP_INFO_ICON);
        WebElement infoIcon = driverTools.waitForElementToAppear(TIKET_POINTS_CAP_INFO_ICON)
                .orElseThrow(() -> new AssertionError("Tix points cap info icon not found on payment page"));
        driverTools.click(infoIcon);
        String tooltipText = driverTools.waitForElementToAppear(TIKET_POINTS_CAP_TOOLTIP, 5)
                .map(driverTools::getText)
                .orElseThrow(() -> new AssertionError("Tix points cap info tooltip not shown after hover/click"));
        logger.log("Tix points cap tooltip: " + tooltipText);
        Assertion.assertThat("Tix points cap info tooltip should mention cap rule",
                expectedSnippet, containsStringIgnoringCase(tooltipText));
        driverTools.switchToNative();
    }

    /**
     * Verifies the displayed redeemable cap, toggles tix points, and asserts only up to {@code capPercentage}% is applied.
     *
     * @return points value (IDR) applied from tix points
     */
    public int verifyAndApplyCappedTiketPoints(int totalPrice, int capPercentage) {
        int maxUsablePoints = (totalPrice * capPercentage) / 100;
        int displayedRedeemable = getRedeemableTiketPointsOnPaymentPage();
        logger.log("Displayed redeemable tix points: " + displayedRedeemable + ", maxUsablePoints: " + maxUsablePoints);
        Assertion.assertThat("Only capped tix points should be redeemable on payment page",
                displayedRedeemable, lessThanOrEqualTo(maxUsablePoints + 1));

        int priceBeforePoints = (int) CoreFunctions.convertPrice(getTotalPrice());
        payWithTiketPoint();
        Assertion.assertThat("Order should not become free when using capped tix points",
                checkTotalPriceChangesToFree(), is(false));

        int priceAfterPoints = (int) CoreFunctions.convertPrice(getTotalPrice());
        int pointsUsed = priceBeforePoints - priceAfterPoints;
        logger.log("priceBeforePoints: " + priceBeforePoints + ", priceAfterPoints: " + priceAfterPoints
                + ", pointsUsed: " + pointsUsed);
        Assertion.assertThat("Tix points used should not exceed cap percentage",
                pointsUsed, lessThanOrEqualTo(maxUsablePoints + 1));
        Assertion.assertThat("Remaining price should require another payment method",
                priceAfterPoints, greaterThan(0));
        return pointsUsed;
    }

    /**
     * Call api that issued all orders that are using atm payment method (jatis)
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void postAutoPaidOrder() throws InterruptedException, ExecutionException{
        PaymentCheckerApi paymentCheckerApi = new PaymentCheckerApi();
        ApiResponse apiResponse = paymentCheckerApi.hitApi(apiTools);
        Assertion.assertThat("issued payment api success",apiResponse.getStatusCode(),is(200));
        Thread.sleep(2000);
    }

    public boolean verifyKredivoRedirect(){
        driverTools.switchToWebView();
         driverTools.waitForElementToDisappear(SEE_ALL_PAYMENT_METHOD_BUTTON_NEXTJS,30);
        driverTools.back();
        driverTools.switchToNative();
        return !driverTools.isElementDisplayed(SEE_ALL_PAYMENT_METHOD_BUTTON_NEXTJS);
    }
  
    public boolean checkTotalPriceChangesToFree(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForElementToAppear(TOTAL_PRICE_FREE);
        String price = driverTools.getText(TOTAL_PRICE).orElseThrow();
        driverTools.switchToNative();
        return price.equalsIgnoreCase("free") || price.equalsIgnoreCase("gratis");
    }

    public void openUrl(String url) throws InterruptedException, ExecutionException{
        driverTools.mAppOpenUrl(url);
        driverTools.waitForElementToAppear(SEE_ORDER_LIST_BUTTON_NATIVE_ANDROID_IOS,7);
        HomePage<T> homePage = HomePage.getInstance(session);
        homePage.goToHomeDeeplink();
    }

    public boolean checkAccountNumberSnackbar(){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        boolean AccountNumber = driverTools.isElementDisplayed(ACCOUNT_NUMBER_CLIPBOARD);
        driverTools.click(ACCOUNT_NUMBER_CLIPBOARD);
        driverTools.switchToNative();
        return AccountNumber;
    }

    public boolean checkPriceNumberSnackbar(){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        boolean AccountNumber = driverTools.isElementDisplayed(TOTAL_PRICE_CLIPBOARD);
        driverTools.click(TOTAL_PRICE_CLIPBOARD);
        return (AccountNumber);
    }

    public boolean checkUniqueCodePriceDetail(){
        openPriceDetail();
        driverTools.switchToWebView();
       boolean isUniqueCodeVisible = driverTools.isElementDisplayed(UNIQUE_CODE);
        driverTools.switchToNative();
        closePriceDetail();
        return isUniqueCodeVisible;
    }

    public boolean isBlibliPayBalanceValid(String pageType) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");

        WebElement element = pageType.equalsIgnoreCase("payment-group") ? driverTools.element(BLIBLIPAY_BALANCE_PAYMENT_GROUP).orElseThrow() : driverTools.element(BLIBLIPAY_BALANCE_PAYMENT_LANDING).orElseThrow();
        String elementText = element.getText();
        boolean elementDisplayed = element.isDisplayed();
        logger.log("BLIBLIPAY_BALANCE elementDisplayed : " + elementDisplayed);
        boolean isTextValid = elementText.contains("Saldo") || elementText.contains("Balance");
        logger.log("BLIBLIPAY_BALANCE elementText BLIBLIPAY_BALANCE : " + elementText);

        driverTools.switchToNative();

        return elementDisplayed && isTextValid;
      }

      public String getDepositAmount() {
          driverTools.switchToWebViewUrl("payment");
        WebElement depositAmountElement = driverTools.waitForElementToAppear(DEPOSIT_AMOUNT).orElseThrow();
        String depositAmount = driverTools.getText(depositAmountElement)
                .replace("(Deposit: ", "")
                .replace(")", "");
        logger.log("Deposit Amount-> " + depositAmount);
        driverTools.switchToNative();
        return depositAmount;
      }

    //getting override in android
    public String getTotalPriceCurrency(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForElementToAppear(TOTAL_PRICE,15);
        String totalPrice = driverTools.getText(TOTAL_PRICE).orElseThrow();
        driverTools.switchToNative();
        String totalPriceCurrency = totalPrice.split(" ")[0];
        return totalPriceCurrency;
    }

    public void applyPromoSuggestion() {
        driverTools.switchToWebViewUrl("payment");

        driverTools.waitForElementToAppear(SUGGESTED_PROMO);
        List<WebElement> element = driverTools.elements(SUGGESTED_PROMO).orElseThrow();
        logger.log("Number of suggested promo elements found: " + element.size());
        WebElement promoCards = element.get(0);
        WebElement promoCodes = driverTools.waitForElementToAppear(SUGGESTED_PROMO_CODE).orElseThrow();
        promoCode = driverTools.getText(promoCodes);
        List<WebElement> discountAmount = driverTools.elements(SUGGESTED_PROMO_DISCOUNT_AMOUNT).orElseThrow();
        logger.log("Number of discount amounts found: " + discountAmount.size());
        for (WebElement discountElement : discountAmount) {
            logger.log("Discount amount: " + discountElement.getText());
        }
        if (!discountAmount.isEmpty()) {
            String[] preProcessDiscountAmmount = driverTools.getText(discountAmount.get(0)).replaceAll("!", "").split(" ");
            logger.log("Pre-processed discount amount: " + Arrays.toString(preProcessDiscountAmmount));
            promoDiscountAmmount = preProcessDiscountAmmount[preProcessDiscountAmmount.length - 2] + " " + preProcessDiscountAmmount[preProcessDiscountAmmount.length - 1];
        } else {
            logger.log("No discount amounts found.");
        }

        driverTools.clickElementByCoordinate(SUGGESTED_PROMO);

        driverTools.waitForElementToAppear(APPLIED_PROMO_TEXT);
        driverTools.switchToNative();
    }


    public void applyGiftVoucherSuggestion() {
        driverTools.switchToWebViewUrl("payment");
        WebElement promoElements = driverTools.swipeToElement(SUGGESTED_GIFT_VOUCHER,Swipe.UP,5).orElseThrow();
        WebElement promoCodes = driverTools.waitForElementToAppear(SUGGESTED_GIFT_VOCHER_CODE).orElseThrow();
        promoCode = driverTools.getText(promoCodes).split(" ")[1];
        //throw other wording to get the code only
        WebElement discountAmount = driverTools.element(SUGGESTED_GIFT_VOUCHER_DISCOUNT_AMOUNT).orElseThrow();
        String[] preProcessDiscountAmmount = driverTools.getText(discountAmount).replaceAll("!", "").split(" ");
        promoDiscountAmmount = preProcessDiscountAmmount[preProcessDiscountAmmount.length-2] + " " + preProcessDiscountAmmount[preProcessDiscountAmmount.length-1];
        driverTools.clickWithJavascriptExecutor(promoElements);
        driverTools.waitForElementToAppear(APPLIED_PROMO_TEXT);
        driverTools.switchToNative();

    }

    public List<String> getPriceDetailNft(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        if(driverTools.waitForElementToAppear(PRICE_DETAIL_SEGMENT_BUTTON).isPresent()) {
            driverTools.click(PRICE_DETAIL_SEGMENT_BUTTON);
        }
        driverTools.waitForElementToAppear(PRICE_LOCATOR_NFT,3);
        List<String> priceItems = new ArrayList<>(driverTools.getTexts(PRICE_LOCATOR_NFT));
        logger.log("prices: "+priceItems);
        Assertion.assertThat("Prices exist", !priceItems.isEmpty(),is(true));
        String totalPrice = driverTools.getText(TOTAL_PRICE_NFT_FROM_PRICE_DETAIL).orElseThrow();
        priceItems.add(totalPrice);
        logger.log("Prices: " + priceItems);
        driverTools.switchToNative();
        return priceItems;
    }

    public boolean getTripTitle(boolean isRoundTrip, String originStation, String destinationStation) {
        boolean isTripTitleDisplayed = driverTools.waitForElementToPresence(TRIP_TITLE).isPresent();
        List<WebElement> tripTitle = driverTools.elements(TRIP_TITLE).orElseThrow();
        int tripTitleSize = tripTitle.size();
        if (isRoundTrip) {
            Assertion.assertThat("Trip title is displayed as round trip", tripTitleSize, is(2));
            Assertion.assertThat("Departure trip title is displayed as expected", driverTools.getText(tripTitle.get(0)), containsStringIgnoringCase(originStation + " → " + destinationStation));
            Assertion.assertThat("Return trip title is displayed as expected", driverTools.getText(tripTitle.get(1)), containsStringIgnoringCase(destinationStation + " → " + originStation));
        } else {
            Assertion.assertThat("Trip title is displayed as one way", tripTitleSize, is(1));
            Assertion.assertThat("Trip title is displayed as expected", driverTools.getText(TRIP_TITLE).orElseThrow(), containsStringIgnoringCase(originStation + " → " + destinationStation));
        }
        return isTripTitleDisplayed;
    }

    public boolean getTripHeader(boolean isRoundTrip) {
        boolean isTripHeaderDisplayed = driverTools.waitForElementToPresence(TRIP_HEADER).isPresent();
        List<WebElement> tripHeader = driverTools.elements(TRIP_HEADER).orElseThrow();
        int tripHeaderSize = tripHeader.size();
        if (isRoundTrip) {
            Assertion.assertThat("Trip header is displayed as round trip", tripHeaderSize, is(2));
            Assertion.assertThat("Departure trip header is not empty", driverTools.getText(tripHeader.get(0)), not(""));
            Assertion.assertThat("Return trip header is not empty", driverTools.getText(tripHeader.get(1)), not(""));
        } else {
            Assertion.assertThat("Trip header is displayed as one way", tripHeaderSize, is(1));
            Assertion.assertThat("Trip header is not empty", driverTools.getText(TRIP_HEADER).orElse(""), not(""));
        }
        return isTripHeaderDisplayed;
    }

    public List<String> getPriceDetail(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        if(driverTools.isElementDisplayed(PRICE_DETAIL_V4_CHEV_BTN))
            driverTools.click(PRICE_DETAIL_V4_CHEV_BTN);
        driverTools.waitForElementToAppear(PRICE_LOCATOR);
        List<String> priceItems = new ArrayList<>(driverTools.getTexts(PRICE_LOCATOR));
        logger.log("prices component: "+priceItems);
        Assertion.assertThat("Prices exist", !priceItems.isEmpty(),is(true));
        String totalPrice = driverTools.getText(TOTAL_PRICE_FROM_PRICE_DETAIL).orElseThrow();
        priceItems.add(totalPrice);
        logger.log("Prices: " + priceItems);
        driverTools.switchToNative();
        return priceItems;
    }

    public void openPaymentGroup(String type) {
        driverTools.switchToWebViewUrl("payment");
        switch (type.toLowerCase()) {
            case "e-wallet":
                openPaymentMethodGroup(getPaymentMethodLocator("E-Wallet"));
                break;
            case "va":
            case "virtual account":
                openPaymentMethodGroup(getPaymentMethodLocator("Virtual Account"));
                break;
            case "debit card":
            case "credit card":
            case "cc":
                // Same Variant A vs Variant B handling as in selectPaymentMethod:
                // the "Groups_title" tile only exists in the multi-method drawer
                // layout. In the single-method inline-form layout the credit-card
                // form is already mounted, so no click is needed.
                Optional<WebElement> ccGroupTile =
                        driverTools.waitForElementToAppear(CREDIT_CARD_PAYMENT_METHOD, 5);
                if (ccGroupTile.isPresent()) {
                    driverTools.click(CREDIT_CARD_PAYMENT_METHOD);
                } else {
                    logger.log("Credit Card group title not found in openPaymentGroup - "
                            + "assuming Variant B (inline card form already mounted). "
                            + "Skipping click.");
                }
                break;
            case "no card installment":
                Locator noCardInstallment = new Locator("PAYMENT_METHOD", AppiumBy.xpath("(//*[contains(text(), 'Paylater / Cicilan Tanpa Kartu') or contains(text(), 'Paylater / Cardless Installments')])[last()]"));
                openNoCardInstallment(noCardInstallment);
                break;
            case "atm":
                openPaymentMethodGroup(getPaymentMethodLocator("ATM"));
                break;
            case "transfer":
                openPaymentMethodGroup(getPaymentMethodLocator("Transfer"));
                break;
            default:
                throw new NotImplementedException("Unimplemented payment group '" + type + "'");
        }
        driverTools.switchToNative();
    }

    public void closePaymentGroup() {
        driverTools.switchToWebViewUrl("payment");
        driverTools.click(CLOSE_PAYMENT_GROUP);
        driverTools.switchToNative();
    }

    public boolean corporateNonIdrBookingPopUP() {
        if (driverTools.isElementDisplayed(NON_IDR_POPUP_NATIVE)) {
            return true;
        } else {
            driverTools.switchToWebViewUrl("payment");
            driverTools.waitForElementToAppear(NON_IDR_POPUP);
            boolean isPopUpAppeared = driverTools.isElementDisplayed(NON_IDR_POPUP);
            driverTools.switchToNative();
            return isPopUpAppeared;
        }
    }

    private Locator getLocatorViaText(String text){
        return new Locator("LOCATOR_VIA_TEXT_" + text,AppiumBy.xpath("//*[contains(text(),'"+text+"')][1]"));
    }

    public void verifyPaymentPageForBus(BusBookingPageData busBookingPageData){
        String totalPricePaymentPage = getTotalPrice();
        logger.log("total price payment page: " + totalPricePaymentPage);
        logger.log("total price booking page: " + busBookingPageData.totalPrice);
        Assertion.assertThat("is price on payment page same as booking page",busBookingPageData.totalPrice.equals(totalPricePaymentPage),is(true));

        String datePaymentPage = getDepartureDate();
        logger.log("date payment page: " + datePaymentPage);
        logger.log("date booking page: " + busBookingPageData.departDate);
        Assertion.assertThat("is date on payment page same as booking page",busBookingPageData.departDate.equals(datePaymentPage),is(true));
        openGuestDetail();

        driverTools.switchToWebViewUrl("payment");
        String busOperatorAndClass = busBookingPageData.operatorName + " - " + busBookingPageData.busClass;
        boolean isBusOperatorAndClassDisplayed = driverTools.isElementDisplayed(getLocatorViaText(busOperatorAndClass));
        logger.log("bus operator and bus class : " + busOperatorAndClass + "displayed : " + isBusOperatorAndClassDisplayed);
        Assertion.assertThat("is bus operator and bus class same as booking page",isBusOperatorAndClassDisplayed,is(true));

        boolean isPassengerNameDisplayed = driverTools.isElementDisplayed(getLocatorViaText(busBookingPageData.passengerName.split(" ")[1]));
        logger.log("passenger name : " + busBookingPageData.passengerName + "displayed : " + isPassengerNameDisplayed);
        Assertion.assertThat("is passenger name same as booking page",isPassengerNameDisplayed,is(true));

        if(!driverTools.isElementDisplayed(SEAT_BUS)){
            driverTools.click(SEE_SEAT_BUS);
        }

        WebElement seatElement = driverTools.waitForElementToAppear(SEAT_BUS).orElseThrow();
        String seat = driverTools.getText(seatElement);
        logger.log("seat on payment page: " + seat);
        boolean isSeatCorrect = busBookingPageData.seatDetail.contains(seat);
        logger.log("Seat on booking page : " + busBookingPageData.seatDetail + "displayed : " + isSeatCorrect);
        Assertion.assertThat("is passenger seat same as booking page",isPassengerNameDisplayed,is(true));
        driverTools.switchToNative();
    }

    public boolean isCCSelectedAsPaymentMethod(){
        driverTools.switchToWebViewUrl("payment");
        boolean isCCFieldDisplayedV3 = driverTools.isElementDisplayed(CARD_NUMBER_V3);
        boolean isCCFieldDisplayed = driverTools.isElementDisplayed(CARD_NAME);
        logger.log("is v3 cc field displayed: " + isCCFieldDisplayedV3);
        logger.log("is cc field displayed: " + isCCFieldDisplayed);
        driverTools.switchToNative();
        return isCCFieldDisplayedV3 | isCCFieldDisplayed;
    }

    private Locator getDescLocatorCorporateSuccessfullyOrder(String textEn,String textId){
        return new Locator("CORPORATE_SUCCESSFULLY_PLACED_ORDER_DESC_POPUP",AppiumBy.xpath("//div[contains(@class,'CorporateBillingSuccess_caption')]//p[contains(normalize-space(.),\""+textEn+"\") or contains(normalize-space(.),\""+textId+"\")]"));
    }

    private Locator getDescLocatorCorporateSuccessfulPayment(String textEn,String textId){
        return new Locator("CORPORATE_SUCCESSFUL_PAYMENT_DESC_POPUP",AppiumBy.xpath("//div[contains(@class,'MobileSuccessNavigation_container')]//span[contains(normalize-space(.),\""+textEn+"\") or contains(normalize-space(.),\""+textId+"\")]"));
    }

    public boolean isCorporateSuccessfullyPlacedOrderPopup(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        logger.log("corporate successfully placed order popup");
        driverTools.waitForElementToAppear(CORPORATE_SUCCESSFULLY_PLACED_ORDER_TITLE_POPUP,30).orElseThrow();
        boolean isTitleDisplayed = driverTools.isElementDisplayed(CORPORATE_SUCCESSFULLY_PLACED_ORDER_TITLE_POPUP);
        logger.log("is title displayed correctly: " + isTitleDisplayed);
        String descEn = "Admin account holders will receive e-tickets right away. If you are a traveler, please wait for the administrator to approve your order.";
        String descId = "Untuk pemegang akun admin, e-tiket akan segera diterbitkan. Jika kamu pelaku perjalanan, tunggu pesananmu disetujui oleh admin.";
        boolean isDescDisplayed = driverTools.isElementDisplayed(getDescLocatorCorporateSuccessfullyOrder(descEn,descId));
        logger.log("is desc displayed correctly: " + isDescDisplayed);
        driverTools.switchToNative();
        return isTitleDisplayed && isDescDisplayed;
    }

    public boolean isCorporatePaymentSuccessPopupDisplayed(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        logger.log("corporate successful payment popup");
        driverTools.waitForElementToAppear(CORPORATE_SUCCESSFUL_PAYMENT_TITLE_POPUP,30).orElseThrow();
        boolean isTitleDisplayed = driverTools.isElementDisplayed(CORPORATE_SUCCESSFUL_PAYMENT_TITLE_POPUP);
        logger.log("is title displayed correctly: " + isTitleDisplayed);
        String descEn = "The e-ticket will be sent to your email and saved on the Your Orders page.";
        String descId = "E-tiket akan dikirim ke email-mu dan tersimpan di halaman Your Orders.";
        boolean isDescDisplayed = driverTools.isElementDisplayed(getDescLocatorCorporateSuccessfulPayment(descEn,descId));
        logger.log("is desc displayed correctly: " + isDescDisplayed);
        driverTools.switchToNative();
        return isTitleDisplayed && isDescDisplayed;
    }

    /**
     * Lands on the Your Orders list after a successful payment / corporate booking flow.
     *
     * <p>Implementation note: we navigate via the {@code /yourorder} deeplink rather than
     * clicking the webview {@code <a href="/yourorder">} "See Order List" button. On iOS,
     * the anchor click triggers a full webview navigation that unloads the Appium click
     * atom mid-flight, producing a 120s {@code "Did not get any response for atom execution"}
     * WebKit timeout even when the on-device navigation itself succeeds. The deeplink
     * performs the same navigation natively and skips the atom round-trip entirely.
     */
    public void SeeOrderListAfterPaymentSuccess(){
        try {
            driverTools.switchToNative();
        } catch (Exception ignored) {
            // already in NATIVE_APP context, or no webview to leave — safe to continue
        }
        Deeplink yourOrder = new Deeplink("YourOrderList",
                "tiket://gatotkaca.tiket.com/yourorder",
                "tiket://preprod.tiket.com/yourorder",
                "tiket://www.tiket.com/yourorder");
        driverTools.deeplink(yourOrder);
        driverTools.waitForElementToAppear(LOADING_STATE);
        driverTools.swipeDown();
    }

    private boolean isPriceSumCorrect (Locator priceLocator) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");

        driverTools.waitForElementToAppear(PRICE_DETAIL_EXPAND).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(PRICE_DETAIL_EXPAND_2).ifPresent(driverTools::click);

        driverTools.swipeToElement(TOTAL_PRICE, Swipe.UP, 1);
        List<WebElement> prices = driverTools.elements(priceLocator).orElseThrow();
        ArrayList<Double> arrayPrice = new ArrayList<Double>();

        for (WebElement element : prices) {
            String text = driverTools.getText(element);
            try {
                double price = CoreFunctions.convertPrice(text);
                arrayPrice.add(price);
            } catch (NumberFormatException ignored) {
            }
        }

        double sum = 0;
        for (double i : arrayPrice) {
            sum += i;
        }
        WebElement totalPriceElement = driverTools.waitForElementToAppear(TOTAL_PRICE_PRICE_DETAIL).orElseThrow();
        String totalPriceText = driverTools.getText(totalPriceElement);
        if(totalPriceText.toLowerCase().contains("free") || totalPriceText.toLowerCase().contains("gratis")){
            logger.log("Total price got:  " + sum);
            driverTools.switchToNative();
            return sum <= 0 ;
        }
        double totalPrice = CoreFunctions.convertPrice(totalPriceText);
        logger.log("Total price text:  " + totalPrice);
        logger.log("Total price got:  " + sum);
        driverTools.switchToNative();

        return totalPrice == sum;
    }

    public String getTotalPricePriceDetail(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        WebElement totalPriceElement = driverTools.waitForElementToAppear(TOTAL_PRICE_PRICE_DETAIL).orElseThrow();
        String totalPriceText = driverTools.getText(totalPriceElement);
        logger.log("total price: " + totalPriceText);
        driverTools.switchToNative();
        return totalPriceText;
    }

    public boolean isPriceBreakdownCorrect(){
        logger.log("Verifying price detail");

        boolean priceCorrect = isPriceSumCorrect(PRICE_LOCATOR);
        logger.log("Total price breakdown correct: " + priceCorrect);

        return priceCorrect;
    }

    public void clickTermAndConditionPromo() {
        driverTools.switchToWebViewUrl("payment");
        driverTools.clickWithJavascriptExecutor(PROMO_TERMCONDITION);
        driverTools.switchToNative();
    }

    public boolean isErrorMessageCorrect(String errorEN, String errorIN) {
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForWebToLoad();
        String text = driverTools.getText(PROMO_ERROR).orElseThrow();
        driverTools.switchToNative();
        return text.contains(errorEN) || text.contains(errorIN);
    }

    public boolean isPromoTncBottomSheetShown() {
        driverTools.switchToWebViewUrl("payment");
        boolean result = driverTools.isElementDisplayed(TERM_AND_CONDITION_BOTTOM_SHEET);
        driverTools.switchToNative();
        return result;
    }

    public boolean isAVBErrorAppears() {
        driverTools.switchToWebView();
        boolean result = driverTools.isElementDisplayed(AVB_ERROR);
        driverTools.switchToNative();
        return result;
    }

    public void clickSeeAllPromoAvBError() {
        driverTools.switchToWebView();
        driverTools.click(AVB_ERROR_SEE_ALL_PROMO);
        driverTools.switchToNative();
    }

    public boolean isOnPromoSuggestionList(){
        driverTools.switchToWebView();
        boolean result = driverTools.waitForElementToPresence(PROMO_ELIGIBLE_LIST).isPresent();
        driverTools.switchToNative();
        return result;
    }
    public String getAppliedPromoCode(){
        driverTools.switchToWebViewUrl("payment");
        driverTools.swipeToElement(PROMO_SECTION,Swipe.UP,4);
        String appliedPromoCode = driverTools.getText(PROMO_SECTION_CODE).orElseThrow();
        driverTools.switchToNative();
        return appliedPromoCode;
    }

    public String getPromoText(){
        driverTools.switchToWebViewUrl("payment");
        driverTools.swipeToElement(PROMO_SECTION,Swipe.UP,3).orElseThrow();
        String appliedPromoText = driverTools.getText(PROMO_SECTION_TEXT).orElseThrow();
        driverTools.switchToNative();
        return appliedPromoText;
    }

    public String getDiscountAmmount(){
        driverTools.switchToWebViewUrl("payment");
        WebElement element = driverTools.swipeToElement(DISCOUNT_AMOUNT, Swipe.UP, 2).orElseThrow();
        String discontAmmount = driverTools.getText(element);
        driverTools.switchToNative();
        return discontAmmount;
    }

    public String getSelectedPromoCode(){
        return promoCode;
    }

    public String getSelectedPromoDiscount(){
        return promoDiscountAmmount;
    }

    public void unApplyPromoCode(){
        driverTools.switchToWebViewUrl("payment");
        driverTools.scrollIntoWebView(PROMO_SECTION);
        driverTools.click(UNAPPLY_PROMO_BTN);
        driverTools.click(CONFIRM_UNAPPLY_PROMO_BTN);
        driverTools.switchToNative();
    }

    public boolean verifyPromoUnApply(){
        driverTools.switchToWebViewUrl("payment");
        Assertion.assertThat("Verify crossed price not present", driverTools.isElementDisplayed(SLASH_PRICE_BEFORE), is(false));
        Assertion.assertThat("verify promo code in promo section is empty", driverTools.isElementDisplayed(PROMO_SECTION_CODE), is(false));
        Assertion.assertThat("Verify discount price is not present", driverTools.isElementDisplayed(DISCOUNT_AMOUNT), is(false));
        driverTools.switchToNative();
        return true;
    }

    public boolean isDiscountIconPresent(){
        driverTools.switchToWebViewUrl("payment");
        boolean isDiscountIconPresent = driverTools.isElementDisplayed(DISCOUNT_PROMO_ICON);
        driverTools.switchToNative();
        return isDiscountIconPresent;
    }

    public boolean isLoginChallengePopupDisplayed(){
        driverTools.switchToWebViewUrl("payment");
        boolean isLoginChallengePopupDisplayed = driverTools.isElementDisplayed(LOGIN_FROM_POPUP_BTN);
        driverTools.switchToNative();
        return isLoginChallengePopupDisplayed;
    }

    public void clickLoginFromPopup(){
        driverTools.switchToWebViewUrl("payment");
        driverTools.clickWithJavascriptExecutor(LOGIN_FROM_POPUP_BTN);
        driverTools.switchToNative();
    }

    public void dismissLoginPopup(){
        driverTools.switchToWebViewUrl("payment");
        driverTools.click(CLOSE_LOGIN_POPUP_BTN);
        driverTools.switchToNative();
    }

    private Locator getPromoCardLocator(String promoCode){
        return new Locator("PROMO_CARD", AppiumBy.xpath("//span[contains(@class, 'Text_text') and text()='" + promoCode + "']"));
    }

    private Locator getPromoNudgesLabelLocator(String nudges){
        return new Locator("PROMO_NUDGES_LABEL_"+nudges+"",AppiumBy.xpath("//*[contains(@class,'SuggestionItem_truncate_alert') and contains(text(),'"+nudges+"')]"));
    }

    public void scrollToPromoCardInPromoList(String promocode){
        driverTools.switchToWebViewUrl("payment");
        Locator PROMO_CARD = getPromoCardLocator(promocode);
        Optional<WebElement> webElement = driverTools.waitForElementToPresence(PROMO_CARD, 5);
        if(webElement.isEmpty()) {
            driverTools.scrollIntoWebView(PROMO_CARD);
        }
        driverTools.switchToNative();
    }

    public void clickPromoCardInPromoList(String promocode){
        driverTools.switchToWebViewUrl("payment");
        Locator PROMO_CARD = getPromoCardLocator(promocode);
        if (!driverTools.isElementPresent(PROMO_CARD))
            driverTools.scrollIntoWebView(PROMO_CARD);
        driverTools.click(PROMO_CARD);
        driverTools.switchToNative();
    }

    public boolean checkNudgesLabelInPromoCardIsShown(String nudges){
        driverTools.switchToWebViewUrl("payment");
        boolean isNudgesShown = driverTools.isElementDisplayed(getPromoNudgesLabelLocator(nudges));
        driverTools.switchToNative();
        return isNudgesShown;
    }


    private Locator getPromoChangeMethodLocator(String promoCode){
        return new Locator("PROMO_CHANGE_METHOD_"+promoCode+"",AppiumBy.xpath("//*[contains(@class,'SuggestionItem_bottom_container') and  .//*[contains(text(),'"+promoCode+"')]]//button"));
    }

    public void clickChangeMethodForInEligiblePromo(String code){
        driverTools.switchToWebViewUrl("payment");
        driverTools.click(getPromoChangeMethodLocator(code));
        driverTools.switchToNative();
    }

    public boolean verifyPsv3Shown(){
        driverTools.switchToWebViewUrl("payment");
        boolean isPsV3Displayed = driverTools.isElementDisplayed(PSV3_CHANGE_METHOD_BTN);
        driverTools.switchToNative();
        return isPsV3Displayed;
    }

    public void clickPsv3ChangePaymentMethod(){
        driverTools.switchToWebViewUrl("payment");
        driverTools.clickWithJavascriptExecutor(PSV3_CHANGE_METHOD_BTN);
        driverTools.switchToNative();
    }

    public String getPriceWithoutOtherFees() {
        driverTools.switchToWebViewUrl("payment");

        // Expand price accordion if needed
        driverTools.waitForElementToAppear(PRICE_DETAIL_V4_CHEV_BTN)
                .ifPresent(driverTools::click);

        // Fetch only visible price elements
        List<WebElement> visiblePrices = driverTools.elements(PRICE_LOCATOR_NO_OTHER_FEES)
                .orElseThrow(() -> new RuntimeException("No price elements found"))
                .stream()
                .filter(WebElement::isDisplayed)
                .collect(Collectors.toList());

        if (visiblePrices.isEmpty()) {
            throw new RuntimeException("No visible price elements found");
        }

        // Extract numeric part including decimals and sum
        double total = 0.0;
        for (WebElement priceEl : visiblePrices) {
            String priceText = driverTools.getText(priceEl);
            if (!priceText.isEmpty()) {
                double value = CoreFunctions.convertPriceSafely(priceText);
                total += value;
                logger.log("Found price: " + value);
            }
        }

        // Format: show decimals only if needed
        DecimalFormat df = new DecimalFormat("#,##0.##"); // Two decimals max
        String formattedPrice = "IDR " + df.format(total);

        logger.log("Total visible price (without other fees): " + formattedPrice);
        return formattedPrice;
    }

    public boolean isInPaymentPage(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        boolean isInPaymentPage = driverTools.waitForElementToAppear(SUBMIT_PAYMENT_BUTTON,30).isPresent();
        driverTools.switchToNative();
        return isInPaymentPage;
    }

    public void clickOnCheckMarkAndClickContinue(boolean isFree){
        driverTools.waitForWebview();
        if(isFree){
            driverTools.switchToWebViewUrl("my-refund");
            logger.log("URL: " + driverTools.getCurrentURL());
            driverTools.swipeToElement(CHECK_BOX,Swipe.UP,3);
            driverTools.click(CHECK_BOX);
            driverTools.swipeUp();
            driverTools.click(CONTINUE_REFUND_RESCHEDULE);
            driverTools.switchToNative();
        }
    }

    public boolean isEarnTiketPointsDisplayed() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");

        boolean result = driverTools.element(EARN_TIKET_POINTS).isPresent();

        driverTools.switchToNative();

        return result;
    }
    public void openWaitingPaymentPage(String orderId){
        OrderListPage<T> orderListPage = OrderListPage.getInstance(session);
        JSONObject response = orderListPage.getInternalOrderData(orderId);
        GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = orderListPage.getVerticalInternalOrderDtata(response);
        String orderHash = generalVerticalInternalOrderDetail.data.orderHash;
        this.openWaitingPaymentPage(orderId,orderHash);
    }

    public abstract void openWaitingPaymentPage(String orderId,String orderHash);

    public boolean isFreePaymentOrRefund(String reschedulePrice){
        boolean isPriceNegetive = reschedulePrice.contains("-");
        boolean isPriceZero = CoreFunctions.convertPrice(reschedulePrice) == 0;
        return isPriceNegetive || isPriceZero;
    }

    public void waitForPaymentPageToLoad(int waitingTime){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForElementToPresence(PRODUCT_NAME_TEXT,waitingTime);
        driverTools.waitForElementToPresence(FIRST_PAYMENT_CHECKBOX,waitingTime);
        driverTools.switchToNative();
    }

    public boolean isPromoCodeAppliedShown(PromoData promoData){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        boolean isPromoBannerWithPromoCodeShown = driverTools.isElementDisplayed(getPromoBannerWithPromoCodeLocator(promoData.promoCode));
        logger.log("isPromoBannerWithPromoCodeShown: " + isPromoBannerWithPromoCodeShown);
        driverTools.switchToNative();
        return isPromoBannerWithPromoCodeShown;
    }

    private Locator getPromoBannerWithPromoCodeLocator(String promoCode){
        return new Locator("PROM_BANNER_WITH_PROMO_CODE",AppiumBy.xpath("//*[contains(@class,'AppliedPromoText_text_content')]//*[contains(@class,'Text_text') and contains(text(),'IDR')]"));
    }

    public void clickContinueWithoutPromo(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.click(CONTINUE_WITHOUT_PROMO);
        driverTools.switchToNative();
    }

    public void continueIfBottomSheetPresent() {
        try {
            driverTools.switchToWebViewUrl("payment");
            if (driverTools.waitForElementToPresence(YES_CONTINUE_MEET_POLICY_BTN).isPresent())
                driverTools.click(YES_CONTINUE_MEET_POLICY_BTN);
            driverTools.switchToNative();
        } catch(Exception e) {
            logger.log("no bottomsheet is appear: " + e);
        }
    }

    public void clickYesContinue(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.click(YES_CONTINUE_MEET_POLICY_BTN);
        driverTools.switchToNative();
    }

    public String getAccountInfoNumber(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForElementToPresence(SELECTED_PAYMENT_TYPE_BCA_2ND_PAGE);
        WebElement accountNumber = driverTools.element(ACCOUNT_NUMBER_TEXT_BOX).orElseThrow();
        String accountNumberText = driverTools.getText(accountNumber);
        logger.log("Account Number: " + accountNumberText);
        driverTools.switchToNative();
        return accountNumberText;
    }

    public String totalPriceToPay(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForElementToAppear(TOTAL_PRICE_BOX_TEXT_2ND_PAGE);
        String totalPrice = driverTools.getText(TOTAL_PRICE_BOX_TEXT_2ND_PAGE).orElseThrow();
        logger.log("Total Price to Pay: " + totalPrice);
        driverTools.switchToNative();
        return totalPrice;
    }

    public abstract void fillOvoPin(String ovoPin);

    @SneakyThrows
    public boolean verifyDepartureInfo() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToAppear(OPEN_DETAILS);
        driverTools.clickWithJavascriptExecutor(OPEN_DETAILS);
        driverTools.clickWithJavascriptExecutor(DEPARTURE_FLIGHT);
        driverTools.scroll(1, 30);
        driverTools.scrollIntoWebView(CLICK_DETAILS);
        driverTools.clickWithJavascriptExecutor(CLICK_DETAILS);

        driverTools.switchToIframe(driverTools.element(IFRAME_INFO).orElseThrow());
        driverTools.swipeToElement(REFUND_INFO, Swipe.UP, 5);
        boolean res1 = driverTools.isElementDisplayed(REFUND_INFO) || driverTools.isElementDisplayed(REFUND);
        Assertion.assertThat("Refund info present for departure flight", true, either(is(driverTools.isElementDisplayed(REFUND_INFO))).or(is(driverTools.isElementDisplayed(REFUND))));
        logger.log("Refund info displayed for departure flight: " + res1);

        driverTools.waitForElementToBeClickable(RESCHEDULE_TAB);
        driverTools.click(RESCHEDULE_TAB);
        boolean res2 = driverTools.isElementDisplayed(RESCHEDULE_INFO) || driverTools.isElementDisplayed(RESCHEDULE);
        Assertion.assertThat("Reschedule info present for departure flight", true, either(is(driverTools.isElementDisplayed(RESCHEDULE_INFO))).or(is(driverTools.isElementDisplayed(RESCHEDULE))));
        driverTools.switchBackFromIframe();
        driverTools.click(CLOSE_BUTTON);
        driverTools.click(CLOSE_BUTTON_AGAIN);
        driverTools.switchToNative();
        return res1 && res2;
    }

    @SneakyThrows
    public boolean verifyReturnInfo() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToAppear(OPEN_DETAILS);
        driverTools.clickWithJavascriptExecutor(OPEN_DETAILS);
        driverTools.clickWithJavascriptExecutor(RETURN_FLIGHT);
        driverTools.scroll(1, 30);
        driverTools.scrollIntoWebView(CLICK_DETAILS);
        driverTools.clickWithJavascriptExecutor(CLICK_DETAILS);

        driverTools.switchToIframe(driverTools.element(IFRAME_INFO).orElseThrow());
        driverTools.swipeToElement(REFUND_INFO, Swipe.UP, 5);
        boolean res1 = driverTools.isElementDisplayed(REFUND_INFO) || driverTools.isElementDisplayed(REFUND);
        logger.log("Refund info displayed for return flight: " + res1);
        driverTools.waitForElementToBeClickable(RESCHEDULE_TAB);
        driverTools.click(RESCHEDULE_TAB);
        boolean res2 = driverTools.isElementDisplayed(RESCHEDULE_INFO) || driverTools.isElementDisplayed(RESCHEDULE);
        driverTools.switchBackFromIframe();
        driverTools.click(CLOSE_BUTTON);
        driverTools.click(CLOSE_BUTTON_AGAIN);
        driverTools.switchToNative();
        return res1 && res2;

    }

    public boolean verifyRefundRescheduleNudgeOnPaymentPage(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForWebToLoad();
        boolean isRefundNudgeDisplayed = driverTools.waitForElementsToAppear(REFUND_RESCHEDULE_NUDGE).isPresent();
        driverTools.switchToNative();
        return isRefundNudgeDisplayed;
    }

    public void verifyPaymentMethods(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForWebToLoad();
        driverTools.waitForElementsToAppear(PAYMENT_METHODS);
        List<String> methods = driverTools.getTexts(PAYMENT_METHODS);
        logger.log("Methods : "+methods);
        Assertion.assertThat("Payment Methods are visible ", methods,not(empty()));
        driverTools.switchToNative();
    }

}
