package com.tiket.web.dWeb.page.payment;

import com.mongodb.client.FindIterable;
import com.tiket.api.accommodation.hotel.HotelPendingRetrySummaryNotifier;
import com.tiket.core.CoreFunctions;
import com.tiket.dbclient.TiketMongoDBManager;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class PaymentPage<T extends WebDriver> extends BasePage<T> {

    private static final Locator PAYMENT_PAGE_CHECK= new Locator("PAYMENT_PAGE_CHECK", AppiumBy.xpath("//div[@data-testid='payment-method']"));
    private static final Locator CONTINUE_PAYMENT_CONTAINER = new Locator("CONTINUE_PAYMENT_CONTAINER", AppiumBy.xpath("//div[contains(@class,'Countdown_countdown')]"));
    private static final Locator SELECT_PAYMENT_METHOD = new Locator("SELECT_PAYMENT_METHOD", new By.ByXPath("//*[text()='Select Payment Method' or text()='Pilih metode pembayaran']"));
    private static final Locator PAYMENT_BUTTON = new Locator("PAYMENT_BUTTON", By.cssSelector("[data-testid='submit_button']"));
    private static final Locator CONTINUE_TO_PAYMENT_POPUP_BUTTON = new Locator("CONTINUE_TO_PAYMENT_POPUP_BUTTON", By.xpath("//div[contains(@class, 'Figure_footer')]//button[contains(@class, 'Button_variant_primary__')]"));
    private static final Locator TRANSACTION_FEE = new Locator("TRANSACTION_FEE", new By.ByXPath("(//span[text()='Transaction fee'])[2]"));
    private static final Locator CONVENIENCE_FEE = new Locator("CONVENIENCE_FEE", By.xpath("(//span[contains(@class,'content_title') and (contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'convenience fee') or contains(.,'Biaya platform') or contains(.,'Biaya Pemesanan'))])[2]"));
    private static final Locator PAYMENT_DRAWER_HEADING = new Locator("PAYMENT_DRAWER_HEADING", new By.ByXPath("//h2[contains(@class,'Text')]"));
    private static final Locator PAYMENT_SUCCCESSFUL_DIV_HEADING = new Locator("PAYMENT_SUCCCESSFUL_DIV_HEADING", new By.ByXPath("//div[contains(@class,'SuccessV2_animation_container')]"));
    private static final Locator ORDER_ID_CONTAINER = new Locator("ORDER_ID_CONTAINER", new By.ByXPath("//p[contains(text(), 'Order ID:')]"));
    private static final Locator PRICE_SUMMARY_CONTAINER = new Locator("PRICE_SUMMARY_CONTAINER", new By.ByXPath("(//div[@data-testid='payment_summary'] | //div[contains(@class,'PriceSummary_price_summary')] | //div[contains(@class,'payment_bottom_sticky_col')] | //span[contains(@class,'PriceSummary_final_price')]/ancestor::div[1])[2]"));
    private static final Locator FINAL_PRICE = new Locator("FINAL_PRICE", new By.ByXPath("//span[contains(@class , 'PriceSummary_final_price')]"));
    private static final Locator SELECTED_PAYMENT = new Locator("SELECTED_PAYMENT", new By.ByXPath("//div[contains(@class,'PaymentTile_seleted_payment_text')]//span[1]"));
    private static final Locator CONTINUE_TO_PAYMENT = new Locator("CONTINUE_TO_PAYMENT", By.xpath("//button[.//span[normalize-space()='Continue to payment']]"));
    private static final Locator PAYMENT_INSTRUCTION_TEXT = new Locator("PAYMENT_INSTRUCTION_TEXT", new By.ByXPath("//p[text()='Payment Instructions']"));
    private static final Locator UNIQUE_CODE = new Locator("UNIQUE_CODE", new By.ByXPath("(//div[contains(@class,'PriceDetail')]//span[contains(text(), '- IDR')])[2]"));
    private static final Locator UNIQUE_CODE_TEXT = new Locator("UNIQUE_CODE_TEXT", new By.ByXPath("(//span[text()='Unique code'])[2]"));
    private static final Locator CLOSE_DRAWER = new Locator("CLOSE_DRAWER", new By.ByXPath("//button[@data-testid='modal-close-button']"));
    private static final Locator CREDIT_CARD_NUMBER = new Locator("CREDIT_CARD_NUMBER", new By.ByName("cardNumber"));
    private static final Locator CREDIT_CARD_MONTH_YEAR = new Locator("CREDIT_CARD_MONTH_YEAR", new By.ByName("cardExpired"));
    private static final Locator CREDIT_CARD_CVV = new Locator("CREDIT_CARD_CVV", new By.ByName("cardCVV"));
    private static final Locator CREDIT_CARD_HOLDER_NAME = new Locator("CREDIT_CARD_HOLDER_NAME", new By.ByXPath("//input[@name='cardHolderName']"));
    private static final Locator AMOUNT_ON_2C2P_PAGE = new Locator("AMOUNT_ON_2C2P_PAGE", new By.ByXPath("(//div[@class='date value'])[2]"));
    private static final Locator OTP_FIELD_ON_2C2P_PAGE = new Locator("OTP_FIELD_ON_2C2P_PAGE", new By.ByXPath("//input[contains(@class,'card-textbox')]"));
    private static final Locator SUBMIT_BUTTON_ON_2C2P_PAGE = new Locator("SUBMIT_BUTTON_ON_2C2P_PAGE", new By.ByXPath("//button[contains(text(),'Submit')]"));
    private static final Locator KREDIVO_FORM = new Locator("KREDIVO_FORM", new By.ByXPath("//form[contains(@class,'ant-form')]"));
    private static final Locator BIBLI_PAYLATER_ELEMENT = new Locator("BIBLI_PAYLATER_ELEMENT", new By.ByXPath("//span[text()='Blibli Tiket PayLater']/ancestor::li"));
    private static final Locator BIBLI_PAYLATER_UNAVAILABLE_TEXT = new Locator("BIBLI_PAYLATER_UNAVAILABLE_TEXT", new By.ByXPath("//div[contains(@class,'unavailable_message')]//p"));
    private static final Locator FORBIDDEN_PAGE = new Locator("FORBIDDEN_PAGE", new By.ByXPath("//h1[text()='403 Forbidden']"));
    private static final Locator SEE_PROMOS_VOUCHERS = new Locator("SEE_PROMOS_VOUCHERS", new By.ByXPath("//div[contains(@class, 'PromoDisplay_main_container')]//input[@type='radio'] | //div[@data-testid='promo-container']//input[@type='radio']"));
    private static final Locator CREDIT_DEBIT_CARD_RADIO = new Locator("CREDIT_DEBIT_CARD_RADIO", new By.ByXPath("//div[contains(@class,'PaymentTile_selected_payment_tile')][.//*[contains(text(),'Credit/Debit Card') or contains(text(),'Kartu Kredit/Debit')]]//input[@type='radio']"));
    private static final Locator APPLY_BUTTON = new Locator("APPLY_BUTTON", new By.ByXPath("//button[contains(@class,'PromoInputForm_apply_button')]"));
    private static final Locator PROMO_INPUT_FIELD = new Locator("PROMO_INPUT_FIELD", new By.ByXPath("//input[@id='enter-your-code']"));
    private static final Locator PROMO_VALIDATION_MESSAGE = new Locator("PROMO_VALIDATION_MESSAGE", new By.ByXPath("//form[contains(@class,'PromoInputForm_discount_code_input')]//span[contains(@class,'Text_variant_alert')]"));
    private static final Locator PROMO_INSTANT_APPLIED_TEXT = new Locator("PROMO_INSTANT_APPLIED_TEXT", new By.ByXPath("//div[contains(@class,'PromoDisplay_text_wrapper')]//span"));
    private static final Locator PROMO_INSTANT_APPLIED_TEXT_CODE = new Locator("PROMO_INSTANT_APPLIED_TEXT_CODE", new By.ByXPath("//div[contains(@class,'PromoDisplay_text_wrapper')]//p"));
    private static final Locator PAYMENT_METHOD_RECOMMENDATION = new Locator("PAYMENT_METHOD_RECOMMENDATION", By.xpath("//*[contains(@class, 'Recommended_recomendation')]"));
    private static final Locator SEE_ALL_PAYMENT_METHOD = new Locator("SEE_ALL_PAYMENT_METHOD", By.xpath("//*[contains(@class, 'PaymentMethod_see_all_group')]"));
    private static final Locator SEE_ALL_BUTTON = new Locator("SEE_ALL_BUTTON", By.xpath("//*[@data-testid='seeall']"));
    private static final Locator SELECT_PAYMENT_METHOD_MODAL_TITLE = new Locator("SELECT_PAYMENT_METHOD_MODAL_TITLE", By.xpath("//section[contains(@class,'Modal_modal_popup')]//h2[normalize-space()='Select Payment Method' or normalize-space()='Pilih Metode Pembayaran']"));
    private static final Locator FIRST_ENABLED_PAYMENT_METHOD_IN_MODAL = new Locator("FIRST_ENABLED_PAYMENT_METHOD_IN_MODAL", By.xpath("(//section[contains(@class,'Modal_modal_popup')]//ul[contains(@class,'PaymentGroup_groups')]/li[not(contains(@class,'disabled'))])[1]"));
    private static final Locator TOTAL_PRICE_TEXT =new Locator("TOTAL_PRICE_TEXT", new By.ByXPath("//span[contains(@class, 'PriceSummary_final_price')]"));
    private static final Locator SEE_ORDER_LIST_BUTTON = new Locator("SEE_ORDER_LIST_BUTTON", By.xpath("//a[contains(@href, 'yourorder') and (.//span[text()='See the order list'] or .//span[text()='Lihat daftar pesanan'])]"));
    private static final Locator TIKET_POINTS = new Locator("TIKET_POINTS", By.xpath("//div[contains(@class,'TiketPoints_desktop_points_radio')]//span[contains(@class,'Radio_checkbox')]"));
    private static final Locator TIKET_POINTS_APPLIED_TEXT = new Locator("TIKET_POINTS_APPLIED_TEXT", By.xpath("//div[contains(@class,'TiketPoints_points_text')]//span[contains(@class,'Text_text')]"));
    private static final Locator BLIBLI_POINTS = new Locator("BLIBLI_POINTS", new By.ByXPath("(//span[text()='Blibli Tiket Points'])[2]"));
    private static final Locator BLIBLI_POINTS_EARNED = new Locator("BLIBLI_POINTS_EARNED", new By.ByXPath("//div[contains(@class,'Text_text') and contains(.,'Blibli Tiket Points')]//b"));
    private static final Locator CORPORATE_SUCCESSFULLY_PLACED_ORDER_TITLE_POPUP = new Locator("CORPORATE_SUCCESSFULLY_PLACED_ORDER_TITLE_POPUP", AppiumBy.xpath("//div[contains(@class,'CorporateBillingSuccess_caption')]//h3"));
    private static final Locator ERROR_MESSAGE_NON_IDR_ENG = new Locator("ERROR_MESSAGE_NON_IDR_ENG", By.xpath("//h2[contains(@class,'Figure_title__') and text()='This product is only available in IDR']"));
    private static final Locator ERROR_MESSAGE_NON_IDR_BAHASA = new Locator("ERROR_MESSAGE_NON_IDR_BAHAASA", By.xpath("//h2[contains(@class,'Figure_title__Waulm') and text()='Maaf ya, ada sedikit gangguan']"));
    private static final Locator TOTAL_PRICE_CHEVRON = new Locator("TOTAL_PRICE_CHEVRON", By.xpath("//*[contains(@class, 'PriceSummary_more_icon')]"));
    private static final Locator DISCOUNT_PRICE_DETAIL = new Locator("DISCOUNT_PRICE_DETAIL", By.xpath("(//*[contains(@class, 'content_title')]//following-sibling::span[contains(text(),'-')])[last()]"));
    private static final Locator TOTAL_PRICE_DETAIL = new Locator("TOTAL_PRICE_DETAIL", By.xpath("(//*[contains(@class, 'PriceDetailV4_total')]//span)[last()]"));
    private static final Locator TOTAL_PRICE_DETAIL_MWEB = new Locator("TOTAL_PRICE_DETAIL_MWEB", By.xpath("(//div[contains(@class, 'PriceDetailV4_total')]//span[last()])[1]"));
    private static final Locator FIRST_PAYMENT = new Locator("FIRST_PAYMENT", By.xpath("(//input[@type='radio' and @name='selected-payment'])[1]"));
    private static final Locator CHANGE_PAYMENT_BUTTON = new Locator("CHANGE_PAYMENT_BUTTON", By.xpath("//button[text()='Change payment' or text()='Ganti pembayaran']"));
    private static final Locator CHANGE_PAYMENT_METHOD_BUTTON = new Locator("CHANGE_PAYMENT_METHOD_BUTTON", By.xpath("//button[text()='Change Payment Method' or text()='Ganti metode pembayaran']"));
    private static final Locator ELIGIBLE_PROMO = new Locator("ELIGIBLE_PROMO", new By.ByXPath("//div[contains(@class, 'EligiblePromos_list')]"));
    private static final Locator NON_ELIGIBLE_PROMO = new Locator("NON_ELIGIBLE_PROMO", new By.ByXPath("//div[contains(@class, 'NonEligiblePromo_list')]"));
    private static final Locator UNAPPLY_BUTTON = new Locator("UNAPPLY_BUTTON", new By.ByXPath("//svg[@data-testid='unapply-promo']"));
    private static final Locator REMOVE_PROMO_BUTTON = new Locator("REMOVE_PROMO_BUTTON", new By.ByXPath("//button[text()='Remove Promo' or text()='Hapus Promo']"));
    private static final Locator CONTINUE_WITH_POINTS = new Locator("CONTINUE_WITH_POINTS", new By.ByXPath("//button[text()='Continue with points' or text()='Lanjut dengan poin']"));
    private static final Locator CONTINUE_WITHOUT_PROMO = new Locator("CONTINUE_WITHOUT_PROMO", new By.ByXPath("//button[text()='Continue Without Promo' or text()='Lanjut Tanpa Promo']"));
    private static final Locator SEE_ALL_METHOD = new Locator("SEE_ALL_METHOD", new By.ByXPath("//div[@data-testid='seeall']"));
    private static final Locator ERROR_MSG_NOT_APPLICABLE_PROMO = new Locator("ERROR_MSG_NOT_APPLICABLE_PROMO", new By.ByXPath("//div[contains(@class, 'PromoCodeInputForm_error_message_container')]//span"));
    private static final Locator LPG_LABEL_PAYMENT_PAGE = new Locator("LPG_LABEL_PAYMENT_PAGE", new By.ByXPath("//img[contains(@alt, 'Lowest Price Guarantee') or contains(@alt, 'Jaminan Harga Termurah')]//ancestor::div[contains(@class, 'LpgBanner_label_container')]\n"));
    private static final Locator ORDER_ID_TEXT = new Locator("ORDER_ID_TEXT", new By.ByXPath("//p[contains(text(), 'Order ID:')]"));
    private static final Locator VIEW_ALL_PAYMENT = new Locator("VIEW_ALL_PAYMENT", By.xpath("//div[contains(@class,'PaymentMethod_see_all_group') and @data-testid='seeall' and (span[text()='See all' or text()='Lihat semua'])]"));
    private static final Locator VIEW_CARD = new Locator("VIEW_ALL_PAYMENT", By.xpath("(//div[contains(@class, 'PaymentTile_selected_payment_installment_sub_container')])[3]"));
    private static final Locator CVCV = new Locator("CVV", By.xpath("//input[@id='cvccvv']"));
    private static final Locator B2B_POPUP = new Locator("B2B_POPUP", By.xpath("//h2[text()='This product is only available in IDR'] or //h2[text()='Produk ini hanya tersedia dalam IDR']"));
    private static final Locator CORPORATE_BILLING_INSUFFICIENT_BALANCE_POPUP = new Locator("CORPORATE_BILLING_INSUFFICIENT_BALANCE_POPUP", By.xpath("//div[contains(@class,'Dialog_pop_up')][.//h2[contains(@class,'Figure_title')]]"));
    private static final Locator CORPORATE_BILLING_INSUFFICIENT_BALANCE_TITLE = new Locator("CORPORATE_BILLING_INSUFFICIENT_BALANCE_TITLE", By.xpath("//div[contains(@class,'Dialog_pop_up')]//h2[contains(@class,'Figure_title')]"));
    private static final Locator CORPORATE_BILLING_INSUFFICIENT_BALANCE_DESCRIPTION = new Locator("CORPORATE_BILLING_INSUFFICIENT_BALANCE_DESCRIPTION", By.xpath("//div[contains(@class,'Dialog_pop_up')]//p[contains(@class,'CorporateError')]"));
    private static final Locator CORPORATE_BILLING_INSUFFICIENT_BALANCE_OK_BUTTON = new Locator("CORPORATE_BILLING_INSUFFICIENT_BALANCE_OK_BUTTON", By.xpath("//div[contains(@class,'Dialog_pop_up')]//button[contains(@class,'Button_variant_primary')][.//span[normalize-space()='Ok, got it' or normalize-space()='Oke, mengerti' or normalize-space()='Baik, mengerti' or contains(normalize-space(),'got it') or contains(normalize-space(),'mengerti')]]"));
    private static final Locator CORPORATE_BILLING_PAYMENT_TILE = new Locator("CORPORATE_BILLING_PAYMENT_TILE", By.xpath(
            "//*[contains(@class,'PaymentTile') or contains(@class,'PaymentGroup') or @data-testid='payment-method']"
                    + "//*[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'corporate billing')]"));
    private static final Locator PAY_WITH_CORPORATE_BILLING_BUTTON = new Locator("PAY_WITH_CORPORATE_BILLING_BUTTON", By.xpath(
            "//button[contains(@class,'SubmitPayment_payment')][.//span[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'corporate billing')]]"));
    private static final Locator INACTIVE_EMPLOYEE_VALIDATION_MESSAGE = new Locator("INACTIVE_EMPLOYEE_VALIDATION_MESSAGE", By.xpath(
            "//*[self::p or self::span or self::div][contains(@class,'Text') or contains(@class,'Snackbar') or contains(@class,'helper')]"
                    + "[(contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'employee')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'karyawan'))"
                    + " and (contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'inactive')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'removed')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'tidak aktif')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'dihapus'))]"));
    private static final Locator CORPORATE_SUSPENDED_BOOKING_POPUP = new Locator("CORPORATE_SUSPENDED_BOOKING_POPUP", By.xpath(
            "//div[contains(@class,'Dialog_pop_up')][.//h2[contains(@class,'Figure_title')][contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'unable to create a corporate booking')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'tidak dapat membuat pemesanan korporat')]]"));
    private static final Locator CORPORATE_SUSPENDED_BOOKING_TITLE = new Locator("CORPORATE_SUSPENDED_BOOKING_TITLE", By.xpath(
            "//div[contains(@class,'Dialog_pop_up')][.//h2[contains(@class,'Figure_title')][contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'unable to create a corporate booking')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'tidak dapat membuat pemesanan korporat')]]//h2[contains(@class,'Figure_title')]"));
    private static final Locator CORPORATE_SUSPENDED_BOOKING_DESCRIPTION = new Locator("CORPORATE_SUSPENDED_BOOKING_DESCRIPTION", By.xpath(
            "//div[contains(@class,'Dialog_pop_up')][.//h2[contains(@class,'Figure_title')][contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'unable to create a corporate booking') or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'tidak dapat membuat pemesanan korporat')]]//p[contains(@class,'CorporateErrorDialog')]"));
    private static final Locator CORPORATE_SUSPENDED_BOOKING_OK_BUTTON = new Locator("CORPORATE_SUSPENDED_BOOKING_OK_BUTTON", By.xpath(
            "//div[contains(@class,'Dialog_pop_up')][.//h2[contains(@class,'Figure_title')][contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'unable to create a corporate booking')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'tidak dapat membuat pemesanan korporat')]]//button[contains(@class,'Button_variant_primary')][.//span[normalize-space()='Ok, got it' or normalize-space()='Oke, mengerti' or normalize-space()='Baik, mengerti' or contains(normalize-space(),'got it') or contains(normalize-space(),'mengerti')]]"));
    private static final Locator CORPORATE_INACTIVE_SUSPENDED_VALIDATION_MESSAGE = new Locator("CORPORATE_INACTIVE_SUSPENDED_VALIDATION_MESSAGE", By.xpath(
            "//*[self::p or self::span or self::div][contains(@class,'Text') or contains(@class,'Snackbar') or contains(@class,'helper')]"
                    + "[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'corporate')"
                    + " and (contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'inactive')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'suspended')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'tidak aktif'))]"));
    private static final Locator ADD_ON_SECTION = new Locator("ADD_ON_SECTION", By.xpath("//div[contains(@class,'AddOnSection_addon_section_container')]"));

    private static final Locator CONVENIENCE_FEE_SPAN = new Locator("CONVENIENCE_FEE_SPAN", By.xpath("//div[contains(@class, 'BottomSheet')]//div[contains(@class, 'PriceDetail')]/span[contains(text(),'Convenience fee') or contains(text(), 'Biaya platform')]"));
    private static final Locator AKULAKU_BTN_LOGIN_ACCOUNT = new Locator("AKULAKU_BTN_LOGIN_ACCOUNT", By.xpath("//a[.//span[normalize-space()='Login Akun & Bayar']]"));
    private static final Locator AKULAKU_FIELD_PHONE = new Locator("AKULAKU_FIELD_PHONE", By.name("phoneNumber"));
    private static final Locator AKULAKU_FIELD_PASSWORD = new Locator("AKULAKU_FIELD_PASSWORD", By.name("password"));
    private static final Locator AKULAKU_BTN_LOGIN = new Locator("AKULAKU_BTN_LOGIN", By.xpath("//button[.//span[normalize-space()='Login']]"));
    private static final Locator AKULAKU_BTN_CONFIRM_PAYMENT = new Locator("AKULAKU_BTN_CONFIRM_PAYMENT", By.xpath("//button[.//span[normalize-space()='Konfirmasi Pembayaran']]"));
    private static final Locator AKULAKU_TXT_PAYMENT_SUCCESS = new Locator("AKULAKU_TXT_PAYMENT_SUCCESS", By.xpath("//p[normalize-space()='Pembayaran berhasil']"));
    private static final Locator AKULAKU_BTN_BACK_TO_SHOP = new Locator("AKULAKU_BTN_BACK_TO_SHOP", By.xpath("//button[.//span[normalize-space()='Kembali Belanja']]"));
    private static final Locator KREDIVO_INSTALLMENT_INPUT = new Locator("KREDIVO_INSTALLMENT_INPUT", By.xpath("//input[@id='select-installment']"));
    private static final Locator KREDIVO_INSTALLMENT_3_PERCENT = new Locator("KREDIVO_INSTALLMENT_3_PERCENT", By.xpath("//span[contains(@class,'Radio_icon')]"));
    private static final Locator KREDIVO_FIELD_PHONE = new Locator("KREDIVO_FIELD_PHONE", By.id("phone"));
    private static final Locator KREDIVO_FIELD_PIN = new Locator("KREDIVO_FIELD_PIN", By.id("pin"));
    private static final Locator KREDIVO_BTN_MASUK = new Locator("KREDIVO_BTN_MASUK", By.xpath("//button[@type='submit' and normalize-space()='Masuk']"));
    private static final Locator KREDIVO_BTN_LANJUTKAN = new Locator("KREDIVO_BTN_LANJUTKAN", By.xpath("//button[normalize-space()='Lanjutkan']"));
    private static final Locator KREDIVO_DIV_MASKED_NUMBER = new Locator("KREDIVO_DIV_MASKED_NUMBER", By.xpath("//div[contains(text(),'****')]"));
    private static final Locator KREDIVO_OTP_FIELD_1 = new Locator("KREDIVO_OTP_FIELD_1", By.xpath("(//input[@inputmode='numeric' and @maxlength='1'])[1]"));
    private static final Locator KREDIVO_OTP_FIELD_2 = new Locator("KREDIVO_OTP_FIELD_2", By.xpath("(//input[@inputmode='numeric' and @maxlength='1'])[2]"));
    private static final Locator KREDIVO_OTP_FIELD_3 = new Locator("KREDIVO_OTP_FIELD_3", By.xpath("(//input[@inputmode='numeric' and @maxlength='1'])[3]"));
    private static final Locator KREDIVO_OTP_FIELD_4 = new Locator("KREDIVO_OTP_FIELD_4", By.xpath("(//input[@inputmode='numeric' and @maxlength='1'])[4]"));
    private static final Locator KREDIVO_BTN_VERIFIKASI = new Locator("KREDIVO_BTN_VERIFIKASI", By.xpath("//button[normalize-space()='Verifikasi']"));
    private static final Locator KREDIVO_BTN_LANJUT_TANPA_KREDISHIELD = new Locator("KREDIVO_BTN_LANJUT_TANPA_KREDISHIELD", By.xpath("//button[normalize-space()='Lanjut tanpa KrediShield']"));
    private static final Locator KREDIVO_BTN_SETUJU = new Locator("KREDIVO_BTN_SETUJU", By.xpath("//button[normalize-space()='Setuju']"));
    private static final Locator KREDIVO_BTN_KEMBALI_KE_TIKET = new Locator("KREDIVO_BTN_KEMBALI_KE_TIKET", By.xpath("//button[normalize-space()='Kembali ke Tiket']"));
    private static final Locator DANA_BTN_PAY = new Locator("DANA_BTN_PAY", By.xpath("//button[contains(@class,'btn-pay')]"));
    private static final Locator DANA_BTN_CLOSE = new Locator("DANA_BTN_CLOSE", By.xpath("//button[contains(@class,'btn-close')]"));
    private static final Locator GOPAY_BTN_PAY = new Locator("GOPAY_BTN_PAY", By.xpath("//button[@data-testid='pay-button']"));
    private static final Locator GOPAY_PIN_INPUT = new Locator("GOPAY_PIN_INPUT", By.xpath("//input[@data-testid='pin-input-field']"));
    private static final Locator GOPAY_BTN_BACK_TO_MERCHANT = new Locator("GOPAY_BTN_BACK_TO_MERCHANT", By.xpath("//a[@data-testid='back-to-merchant-button']"));
    private static final Locator BNI_SNAP_FIELD_VA = new Locator("BNI_SNAP_FIELD_VA", By.id("bankchannelsimulatorform-virtual_account"));
    private static final Locator BNI_SNAP_BTN_SEARCH = new Locator("BNI_SNAP_BTN_SEARCH", By.id("searchva"));
    private static final Locator BNI_SNAP_FIELD_AMOUNT = new Locator("BNI_SNAP_FIELD_AMOUNT", By.id("bankchannelsimulatorform-total_paid"));
    private static final Locator BNI_SNAP_BTN_FLAG = new Locator("BNI_SNAP_BTN_FLAG", By.xpath("//button[@class='btn btn-success flag-button']"));
    private static final Locator BNI_SNAP_TXT_SUCCESS = new Locator("BNI_SNAP_TXT_SUCCESS", By.xpath("//p[contains(text(),'Transaction success')]"));
    private static final Locator BNI_SNAP_TXT_NOT_FOUND = new Locator("BNI_SNAP_TXT_NOT_FOUND", By.xpath("//p[normalize-space()='Data Not Found']"));
    private static final Locator IFORTE_SNAP_FIELD_VA = new Locator("IFORTE_SNAP_FIELD_VA", By.xpath("//input[@id='virtualAccountNo']"));
    private static final Locator IFORTE_SNAP_SELECT_CHANNEL = new Locator("IFORTE_SNAP_SELECT_CHANNEL", By.xpath("//span[normalize-space()='Select Payment Channel']"));
    private static final Locator BSI_SNAP_OPTION_BSI = new Locator("BSI_SNAP_OPTION_BSI", By.xpath("//div[normalize-space()='BSI']"));
    private static final Locator PERMATA_SNAP_OPTION_PERMATA = new Locator("PERMATA_SNAP_OPTION_PERMATA", By.xpath("//div[normalize-space()='PERMATA']"));
    private static final Locator BRI_SNAP_OPTION_BRI = new Locator("BRI_SNAP_OPTION_BRI", By.xpath("//div[normalize-space()='BRI']"));
    private static final Locator IFORTE_SNAP_BTN_SEARCH = new Locator("IFORTE_SNAP_BTN_SEARCH", By.xpath("//button[normalize-space()='Search']"));
    private static final Locator IFORTE_SNAP_BTN_PAY_VA = new Locator("IFORTE_SNAP_BTN_PAY_VA", By.xpath("//button[normalize-space()='Pay VA']"));
    private static final Locator IFORTE_SNAP_TXT_SUCCESS = new Locator("IFORTE_SNAP_TXT_SUCCESS", By.xpath("//p[normalize-space()='Dummy Payment Success!']"));
    private static final Locator PAYMENT_SUMMARY_TRIGGER = new Locator("PAYMENT_SUMMARY_TRIGGER", By.xpath("//div[contains(@class, 'PriceSummary_final_price_wrapper')] | //span[@data-testid='price_summary_final_price']"));
    private static final Locator PRICE_DETAILS_MODAL_TITLE = new Locator("PRICE_DETAILS_MODAL_TITLE", By.xpath("//section[contains(@class, 'Modal_modal_popup')]//h2[normalize-space()='Price Details' or normalize-space()='Rincian Harga']"));
    private static final Locator BUS_CLASS_ACCORDION_BTN = new Locator("BUS_CLASS_ACCORDION_BTN", By.xpath("//div[contains(@class, 'ModalBody_content')]//div[contains(@class, 'accordion_button') and @role='button']"));
    private static final Locator MODAL_TAX_PRICE = new Locator("MODAL_TAX_PRICE", By.xpath("//div[contains(@class, 'ModalBody_content')]//span[contains(text(), 'Tax')]/following-sibling::span"));
    private static final Locator MODAL_TOTAL_PRICE = new Locator("MODAL_TOTAL_PRICE", By.xpath("//div[contains(@class, 'ModalBody_content')]//span[text()='Total']/following-sibling::span"));
    private static final Locator STICKY_FOOTER_TOTAL_PRICE = new Locator("STICKY_FOOTER_TOTAL_PRICE", By.xpath("//span[@data-testid='price_summary_final_price']"));
    private static final Locator SELECT_PAYMENT_METHOD(String paymentMethod){
        return new Locator("SELECT_PAYMENT_METHOD", By.xpath(String.format("(//*[contains(@class,'PaymentGroup_groups')]//*[text()='%s'])[last()]",paymentMethod)));
    }
    
    private static Locator PAYMENT_METHOD_GROUP(String paymentGroup) {
        String xpath = String.format("//div[contains(@class,'Group')]//*[%s]", buildPaymentGroupTextCondition(paymentGroup));
        return new Locator("INSTANT_PAYMENT_METHOD_GROUP", By.xpath(xpath));
    }

    private static String buildPaymentGroupTextCondition(String paymentGroup) {
        String[] groups = paymentGroup.split("/");
        if (groups.length == 1) {
            return String.format("contains(text(),'%s')", paymentGroup.trim());
        }

        StringBuilder condition = new StringBuilder();
        for (String group : groups) {
            String trimmedGroup = group.trim();
            if (trimmedGroup.isEmpty()) {
                continue;
            }
            if (condition.length() > 0) {
                condition.append(" or ");
            }
            condition.append(String.format("contains(text(),'%s')", trimmedGroup));
        }
        return condition.toString();
    }

    public PaymentPage(Session<T> session) {
        super(session);
    }

    public String getOrderIdText(){
        WebElement orderIdText = driverTools.waitForElementToAppear(ORDER_ID_TEXT).orElseThrow();
        return driverTools.getText(orderIdText);
    }

    public void goToPaymentMethodsGroup(String paymentGroup){
        driverTools.waitForElementToAppear(PAYMENT_DRAWER_HEADING);
        driverTools.waitForElementToAppear(PAYMENT_METHOD_GROUP("Transfer Virtual Account"));
        driverTools.scrollIntoWebView(PAYMENT_METHOD_GROUP(paymentGroup));
        driverTools.waitForElementToAppear(PAYMENT_METHOD_GROUP(paymentGroup));
        driverTools.clickWithJavascriptExecutor(PAYMENT_METHOD_GROUP(paymentGroup));
    }

    public void goToPaymentMethodsGroupSakuku(String paymentGroup){
        driverTools.waitForElementToAppear(PAYMENT_DRAWER_HEADING);
        driverTools.waitForElementToAppear(PAYMENT_METHOD_GROUP("Sakuku"));
        driverTools.scrollIntoWebView(PAYMENT_METHOD_GROUP(paymentGroup));
        driverTools.waitForElementToAppear(PAYMENT_METHOD_GROUP(paymentGroup));
    }

    public void selectPaymentMethod(String paymentMethod){
        WebElement element = driverTools.waitForElementToAppear(SELECT_PAYMENT_METHOD(paymentMethod)).orElseThrow();
        driverTools.scrollIntoWebView(element);
        driverTools.clickWithJavascriptExecutor(SELECT_PAYMENT_METHOD(paymentMethod));
        logger.log("payment method "+paymentMethod+" selected");
        isPaymentPageLoaded();
    }

    public void selectPaymentMethodMWeb(String paymentMethod, String paymentType){
        driverTools.waitForElementToAppear(VIEW_ALL_PAYMENT).ifPresent(driverTools::clickWithJavascriptExecutor);
        WebElement paymentTypeElement = driverTools.waitForElementToAppear(getPaymentType(paymentType)).orElseThrow();
        driverTools.scrollIntoWebView(paymentTypeElement);
        driverTools.clickWithJavascriptExecutor(paymentTypeElement);
        WebElement element = driverTools.waitForElementToAppear(getPaymentMethodMWweb(paymentMethod)).orElseThrow();
        driverTools.scrollIntoWebView(element);
        driverTools.clickWithJavascriptExecutor(element);
        isPaymentPageLoaded();
    }   

    public Locator getPaymentType(String paymentType) { 
        return new Locator("PAYMENT_HEADER_CATALOG", By.xpath("//p[contains(@class,'Groups_title') and normalize-space(text())='" + paymentType + "']"));
    }

    public Locator getPaymentMethodMWweb(String paymentMethod) {
        return new Locator("PAYMENT_METHOD_MWEB", By.xpath("//div[contains(@class,'Groups_flex_row')]//p[contains(@class,'Text_text') and normalize-space(text())='" + paymentMethod + "']"));
    }

    public void clickOnSelectPaymentMethod() {
        boolean recommendationExist = driverTools.waitForElementToAppear(PAYMENT_METHOD_RECOMMENDATION, 30).isPresent();
        if (recommendationExist) {
            driverTools.clickWithJavascriptExecutor(SEE_ALL_PAYMENT_METHOD);
        } else {
            driverTools.clickWithJavascriptExecutor(SELECT_PAYMENT_METHOD);
        }
    }

    public void verifySeeAllButtonHasPointerCursor() {
        WebElement seeAll = driverTools.waitForElementToAppear(SEE_ALL_BUTTON, 10).orElseThrow(() -> new NoSuchElementException("See all button not found on payment page"));
        driverTools.scrollElementToCenter(seeAll);
        driverTools.hoverToElement(seeAll);
        Assertion.assertThat("Verify See all button shows pointer cursor on hover", seeAll.getCssValue("cursor"), is("pointer"));
    }

    public void clickSeeAllButton() {
        driverTools.clickWithJavascriptExecutor(SEE_ALL_BUTTON);
        driverTools.waitForElementToAppear(SELECT_PAYMENT_METHOD_MODAL_TITLE, 10).orElseThrow(() -> new NoSuchElementException("Select Payment Method modal did not open"));
    }

    public void verifyPaymentMethodHoverShowsPointerCursorAndBackground(String expectedHexColor) {
        WebElement item = driverTools.waitForElementToAppear(FIRST_ENABLED_PAYMENT_METHOD_IN_MODAL, 10).orElseThrow(() -> new NoSuchElementException("Payment method not found in Select Payment Method modal"));
        String backgroundBeforeHover = item.getCssValue("background-color");
        driverTools.hoverToElement(item);
        String backgroundOnHover = item.getCssValue("background-color");
        Assertion.assertThat("Verify payment method shows pointer cursor on hover", item.getCssValue("cursor"), is("pointer"));
        Assertion.assertThat("Verify payment method background changes on hover", backgroundOnHover, not(backgroundBeforeHover));
        String actualHexColor = Color.fromString(backgroundOnHover).asHex();
        Assertion.assertThat("Verify payment method background color on hover", actualHexColor.toLowerCase(), is(Color.fromString(expectedHexColor).asHex().toLowerCase()));
    }

    public boolean isPaymentPageLoaded() {
        driverTools.waitForElementToAppear(CONTINUE_PAYMENT_CONTAINER);
        driverTools.waitForElementToAppear(FINAL_PRICE);
        boolean loaded = (driverTools.getTitle().contains("Continue") || driverTools.getTitle().contains("Lanjutkan")) &&
                driverTools.waitForElementToAppear(CONTINUE_PAYMENT_CONTAINER).isPresent();
        if (!loaded) {
            logger.softWarning("isPaymentPageLoaded returned false. title='" + driverTools.getTitle()
                    + "', currentURL='" + driverTools.getCurrentURL() + "'");
        }
        return loaded;
    }

    public String clickPayAndGetNextPageUrl() {
        clickOnPaymentButton();
        new WebDriverWait(driver, Duration.ofSeconds(60)).until(d -> d.getCurrentUrl().contains("/complete"));
        String nextPageUrl = driver.getCurrentUrl();
        logger.log("Next page url after pay: " + nextPageUrl);
        return nextPageUrl;
    }

    public void clickOnPaymentButton(){
        driverTools.waitForElementToAppear(PAYMENT_BUTTON);
        driverTools.waitForElementToBeClickable(PAYMENT_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public void clickContinueToPaymentPopUp() {
        if (driverTools.waitForElementToAppear(CONTINUE_TO_PAYMENT_POPUP_BUTTON).isPresent()) {
            driverTools.clickWithJavascriptExecutor(CONTINUE_TO_PAYMENT_POPUP_BUTTON);
        }
    }

    public boolean isPaymentSuccessfulVisible(){
        driverTools.waitForElementToAppear(PAYMENT_SUCCCESSFUL_DIV_HEADING,120);
        boolean visible = driverTools.isElementDisplayed(PAYMENT_SUCCCESSFUL_DIV_HEADING);
        if (visible) {
            HotelPendingRetrySummaryNotifier.afterWebPaymentSuccess(session, apiTools, logger);
        }
        return visible;
    }

    public String getOrderId(){
        driverTools.waitForElementToAppear(ORDER_ID_CONTAINER);
        WebElement orderIdTextElement = driverTools.waitForElementToAppear(ORDER_ID_CONTAINER,30).orElseThrow();
        String orderIdText = driverTools.getText(orderIdTextElement);
        int colonIndex = orderIdText.indexOf(":");
        String orderId = orderIdText.substring(colonIndex + 1).trim();
        logger.log("orderId "+orderId);
        return orderId;
    }

    public int getEarnedPoints(){
        WebElement pointsElement = driverTools.waitForElementToAppear(BLIBLI_POINTS_EARNED, 30).orElseThrow();
        String pointsText = driverTools.getText(pointsElement).replaceAll("[^\\d]", "");
        int points = pointsText.isEmpty() ? 0 : Integer.parseInt(pointsText);
        logger.log("earnedPoints " + points);
        return points;
    }

    public boolean isLPGLabelPresentOnPaymentPage() {
        return driverTools.waitForElementsToAppear(LPG_LABEL_PAYMENT_PAGE).isPresent();
    }

    public String getSelectedPayment(){
        WebElement selectedPaymentTextElement = driverTools.waitForElementToAppear(SELECTED_PAYMENT).orElseThrow();
        String selectedPaymentText = driverTools.getText(selectedPaymentTextElement);
        logger.log("selectedPaymentText "+selectedPaymentText);
        return selectedPaymentText;
    }

    public boolean isErrorMessageDisplayed(){
        boolean isEnglishMessageDisplayed = driverTools.waitForElementToAppear(ERROR_MESSAGE_NON_IDR_ENG).isPresent();
        boolean isBahasaMessageDisplayed = driverTools.waitForElementToAppear(ERROR_MESSAGE_NON_IDR_BAHASA).isPresent();
        if (isEnglishMessageDisplayed) {
            return driverTools.isElementDisplayed(ERROR_MESSAGE_NON_IDR_ENG);
        } else if (isBahasaMessageDisplayed) {
            return driverTools.isElementDisplayed(ERROR_MESSAGE_NON_IDR_BAHASA);
        } else {
            logger.log("No error message is displayed");
            return false;
        }
    }

    public String getPaymentAmountOn2c2p(){
        WebElement paymentAmountOn2c2pElement = driverTools.waitForElementToAppear(AMOUNT_ON_2C2P_PAGE).orElseThrow();
        String paymentAmountOn2c2p = driverTools.getText(paymentAmountOn2c2pElement);
        logger.log("paymentAmountOn2c2p "+paymentAmountOn2c2p);
        String finalPriceWithoutComma = paymentAmountOn2c2p.replaceAll(",", "");
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(finalPriceWithoutComma);
        if (matcher.find()) {
            String finalPrice = matcher.group(0);
            logger.log("finalPrice "+finalPrice);
            return finalPrice;
        } else {
            return "";
        }
    }

  public void clickContinueToPayment(){
    driverTools.waitForElementToAppear(CONTINUE_TO_PAYMENT).ifPresent(driverTools::clickWithJavascriptExecutor);
  }

    public String getFinalPrice() {
        driverTools.waitForElementToAppear(FINAL_PRICE).orElseThrow();
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(d -> {
                    String finalPriceText = driverTools.getText(FINAL_PRICE).orElse("");
                    String finalPriceFormatted = finalPriceText.replaceAll("[^\\d]", "");
                    return !finalPriceFormatted.isEmpty() && Integer.parseInt(finalPriceFormatted) > 0;
                });
        String finalPriceText = driverTools.getText(FINAL_PRICE).orElseThrow();
        String finalPriceFormatted = finalPriceText.replaceAll("[^\\d]", "");
        logger.log("finalPrice " + finalPriceFormatted);
        return finalPriceFormatted;
    }

    public String totalPricePaymentPage(){
        WebElement finalPriceTextElement = driverTools.waitForElementToAppear(FINAL_PRICE).orElseThrow();
        String finalPriceText = driverTools.getText(finalPriceTextElement);
        logger.log("finalPriceText -> " + finalPriceText);
        return finalPriceText;
    }

    public double totalPricePaymentPageHandleFreePayment(){
        WebElement finalPriceTextElement = driverTools.waitForElementToAppear(FINAL_PRICE).orElseThrow();
        String finalPriceText = driverTools.getText(finalPriceTextElement);
        if(finalPriceText.toLowerCase().contains("free"))
            finalPriceText = "0";
        double finalPrice = CoreFunctions.convertPriceSafely(finalPriceText);
        logger.log("finalPriceText -> " + finalPrice);
        return finalPrice;
    }


    public Map<String, String> getPaymentTransactionDetails(String configType, String dataBase, String collectionName, String queryString) {
      try {
        Thread.sleep(3000);
        logger.log("wait 3s to buffer latest status");
      }
      catch (Exception e){
        e.getMessage();
      }
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

            return paymentInfo;
        }
        return null;
    }

    public String queryString(String orderID){
        logger.log("orderID "+orderID);
        return "{referenceId:\"" + orderID + "\"}";
    }

    private static String mainSitePaymentUrl(String origin, String orderId, String orderHash) {
        return origin + "/en-sg/payment?order_id=" + orderId + "&order_hash=" + orderHash;
    }

    public void goToPaymentPage(String orderId , String orderHash){
        Deeplink deepLink = new Deeplink("ORDER_DETAIL_PAGE_"+orderId,
                mainSitePaymentUrl("https://gatotkaca.tiket.com", orderId, orderHash),
                mainSitePaymentUrl("https://preprod.tiket.com", orderId, orderHash),
                mainSitePaymentUrl("https://www.tiket.com", orderId, orderHash));

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

    public boolean isPaymentConfirmationPageLoaded(){
        driverTools.waitForElementToAppear(PAYMENT_INSTRUCTION_TEXT);
        return driverTools.isElementDisplayed(PAYMENT_INSTRUCTION_TEXT) && driver.getCurrentUrl().contains("confirm");
    }

    public void openPriceSummary() {
        driverTools.waitForElementToAppear(PRICE_SUMMARY_CONTAINER).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public String getUniqueCode(){
        WebElement uniqueCodeElement = driverTools.waitForElementToAppear(UNIQUE_CODE).orElseThrow();
        String uniqueCodeText = driverTools.getText(uniqueCodeElement);
        String uniqueCode = uniqueCodeText.replaceAll("[^\\d]", "");
        logger.log("uniqueCode "+uniqueCode);
        return uniqueCode;
    }

    public void closeDrawer(){
        driverTools.waitForElementToAppear(CLOSE_DRAWER).ifPresent(driverTools::click);
    }

    public boolean isUniqueCodeTextDisplayed(){
        driverTools.waitForElementToAppear(UNIQUE_CODE_TEXT);
        return driverTools.isElementDisplayed(UNIQUE_CODE_TEXT);
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

    public boolean isTransactionFeesDisplayed(){
        driverTools.waitForElementToAppear(TRANSACTION_FEE);
        return driverTools.isElementDisplayed(TRANSACTION_FEE);
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

    public boolean is2c2pConfirmationPageLoaded(){
        return driver.getCurrentUrl().contains("demo-emvacs.2c2p.com");
    }

    public boolean isKredivoPageLoaded(){
        driverTools.waitForElementToAppear(KREDIVO_FORM);
        return driver.getCurrentUrl().contains("pay.kredivo.com");
    }

    public boolean isOctoClicksPageLoaded(){
        driverTools.waitForElementToAppear(FORBIDDEN_PAGE);
        return driver.getCurrentUrl().contains("pg-uat.e2pay.co.id");
    }

    public void clickOnSubmitButton(){
        driverTools.waitForElementToAppear(SUBMIT_BUTTON_ON_2C2P_PAGE).ifPresent(driverTools::click);
    }

    public void inputOtpOn2C2PPage(String otp){
        logger.log("otp "+otp);
        driverTools.waitForElementToAppear(OTP_FIELD_ON_2C2P_PAGE);
        driverTools.sendKeys(OTP_FIELD_ON_2C2P_PAGE, otp);
    }

    public boolean isTiketPayLaterDisabled() {
        WebElement tiketPayLaterElement = driverTools.waitForElementToAppear(BIBLI_PAYLATER_ELEMENT).orElseThrow();
        boolean isDisabled = tiketPayLaterElement.getAttribute("class").contains("disabled");
        logger.log("Tiket PayLater is disabled: " + isDisabled);
        return isDisabled;
    }

    public String getPayLaterText(){
        WebElement paylaterTextElement = driverTools.waitForElementToAppear(BIBLI_PAYLATER_UNAVAILABLE_TEXT).orElseThrow();
        String paylaterText = driverTools.getText(paylaterTextElement);
        logger.log("paylaterText "+paylaterText);
        return paylaterText;
    }

    public boolean isPaymentMethodVisible(String paymentMethod){
        Optional<WebElement> paymentMethodLocator = driverTools.waitForElementToAppear(SELECT_PAYMENT_METHOD(paymentMethod),3);
        return !paymentMethodLocator.isPresent();
    }

    public void selectCreditDebitCardRadio() {
        WebElement radio = driverTools.waitForElementToPresence(CREDIT_DEBIT_CARD_RADIO, 20).orElseThrow(() -> new NoSuchElementException("Credit/Debit Card radio not found on payment page"));
        driverTools.scrollIntoWebView(radio);
        driverTools.clickWithJavascriptExecutor(radio);
        logger.log("Credit/Debit Card payment tile selected");
    }

    public void fillCardDetails(String cardNumber, String expiry, String cvv, String cardHolderName) {
        inputCardNumber(cardNumber);
        inputCardMonth(expiry);
        inputCardCvv(cvv);
        inputCardHolderName(cardHolderName);
    }

    public void applyPromoFromSuggestionList(String promoCode) {
        Locator applyButton = new Locator("APPLY_PROMO_" + promoCode, new By.ByXPath("//button[@data-testid='btnApply-" + promoCode + "']"));
        WebElement button = driverTools.waitForElementToPresence(applyButton, 20).orElseThrow(() -> new NoSuchElementException("Promo " + promoCode + " not found in promo suggestion list"));
        driverTools.scrollIntoWebView(button);
        driverTools.clickWithJavascriptExecutor(button);
        logger.log("Promo applied from suggestion list: " + promoCode);
    }

    public void clickPromoSuggestion() {
        driverTools.clickWithJavascriptExecutor(SEE_PROMOS_VOUCHERS);
    }

    public void inputPromoInInputField(String value){
        driverTools.waitForElementToAppear(PROMO_INPUT_FIELD);
        driverTools.sendKeys(PROMO_INPUT_FIELD, value);
    }

    public boolean isValidationCorrect(String expectedValidationMsg) {
        driverTools.waitForElementToAppear(PROMO_VALIDATION_MESSAGE);
        String actualValidationText = driverTools.getText(PROMO_VALIDATION_MESSAGE).get();
        logger.log("actual validation text -> " + actualValidationText);
        logger.log("expected validation text -> " + expectedValidationMsg);
        return actualValidationText.equals(expectedValidationMsg);
    }

    public void clickApplyButton() {
        driverTools.waitForElementToAppear(APPLY_BUTTON).ifPresent(driverTools::click);
    }

    public boolean isPromoInstantApplied(String expectedPromoApplied, String expectedPromoAppliedCode){
        driverTools.waitForElementToAppear(PROMO_INSTANT_APPLIED_TEXT, 10).orElseThrow();
        driverTools.waitForElementToAppear(PROMO_INSTANT_APPLIED_TEXT_CODE, 10).orElseThrow();
        String actualPromoApplied = driverTools.getText(PROMO_INSTANT_APPLIED_TEXT).get();
        String actualPromoAppliedCode = driverTools.getText(PROMO_INSTANT_APPLIED_TEXT_CODE).get();
        return actualPromoApplied.contains(expectedPromoApplied) && actualPromoAppliedCode.equals(expectedPromoAppliedCode);
    }

    public boolean verifyTotalPaymentPriceInIDR() {
        List<WebElement> priceList = driverTools.elements(TOTAL_PRICE_TEXT).orElseThrow();
        for (WebElement priceElement : priceList) {
            String priceText = driverTools.getText(priceElement);
            if (!priceText.contains("IDR")) {
                return false;
            }
        }
        return true;
    }

    public void clickSeeOrderListButton() {
        driverTools.waitForElementToAppear(SEE_ORDER_LIST_BUTTON, 30);
        WebElement seeOrderListButton = driverTools.waitForElementToAppear(SEE_ORDER_LIST_BUTTON, 30).orElseThrow();
        driverTools.scrollIntoWebView(seeOrderListButton);
        driverTools.clickWithJavascriptExecutor(seeOrderListButton);
        HotelPendingRetrySummaryNotifier.afterNavigateToYourOrders(session, apiTools, logger);
    }

    public void clickOnTiketPoints() {
        WebElement tiketPointButton = driverTools.waitForElementToAppear(TIKET_POINTS).orElseThrow();
        driverTools.clickWithJavascriptExecutor(tiketPointButton);
    }

    public boolean getTiketPointsText(){
        WebElement tiketPointsTextElement = driverTools.waitForElementToAppear(TIKET_POINTS_APPLIED_TEXT).orElseThrow();
        String tiketPointsText = driverTools.getText(tiketPointsTextElement);
        logger.log(tiketPointsText);
        return tiketPointsText.contains("Save IDR");
    }

    public boolean isTiketPointsDisplayed(){
        driverTools.waitForElementToAppear(BLIBLI_POINTS);
        return driverTools.isElementDisplayed(BLIBLI_POINTS);
    }

    public boolean isCorporateSuccessfullyPlacedOrderPopup(){
        logger.log("corporate successfully placed order popup");
        driverTools.waitForElementToAppear(CORPORATE_SUCCESSFULLY_PLACED_ORDER_TITLE_POPUP,15);
        boolean isTitleDisplayed = driverTools.isElementDisplayed(CORPORATE_SUCCESSFULLY_PLACED_ORDER_TITLE_POPUP);
        logger.log("is title displayed correctly: " + isTitleDisplayed);
        String descEn = "Admin account holders will receive e-tickets right away. If you are a traveler, please wait for the administrator to approve your order.";
        String descId = "Untuk pemegang akun admin, e-tiket akan segera diterbitkan. Jika kamu pelaku perjalanan, tunggu pesananmu disetujui oleh admin.";
        boolean isDescDisplayed = driverTools.isElementDisplayed(getDescLocatorCorporateSuccessfullyOrder(descEn,descId));
        logger.log("is desc displayed correctly: " + isDescDisplayed);
        return isTitleDisplayed && isDescDisplayed;
    }

    private Locator getDescLocatorCorporateSuccessfullyOrder(String textEn,String textId){
        return new Locator("CORPORATE_SUCCESSFULLY_PLACED_ORDER_DESC_POPUP",AppiumBy.xpath("//div[contains(@class,'CorporateBillingSuccess_caption')]//p[contains(normalize-space(.),\""+textEn+"\") or contains(normalize-space(.),\""+textId+"\")]"));
    }

    public void clickTotalPriceChevron() {
        driverTools.waitForElementToAppear(TOTAL_PRICE_CHEVRON);
        driverTools.click(TOTAL_PRICE_CHEVRON);
    }

    public String getDiscountPrice() {
        driverTools.click(TOTAL_PRICE_CHEVRON);
        driverTools.waitForElementToAppear(DISCOUNT_PRICE_DETAIL);
        return driverTools.getText(DISCOUNT_PRICE_DETAIL).orElseThrow();
    }

    public String getTotalPriceDetail() {
        driverTools.click(TOTAL_PRICE_CHEVRON);
        driverTools.waitForElementToAppear(TOTAL_PRICE_DETAIL);
        return driverTools.getText(TOTAL_PRICE_DETAIL).orElseThrow();
    }

    public String getTotalPriceDetailMweb() {
        driverTools.click(TOTAL_PRICE_CHEVRON);
        driverTools.waitForElementToAppear(TOTAL_PRICE_DETAIL_MWEB);
        return driverTools.getText(TOTAL_PRICE_DETAIL_MWEB).orElseThrow();
    }

    public boolean validateExistPlatformFee() {
        driverTools.waitForElementToPresence(CONVENIENCE_FEE_SPAN);
        return !driver.findElements(CONVENIENCE_FEE_SPAN.by()).isEmpty();
    }

    public void selectFirstPaymentMethod(){
        driverTools.waitForElementToAppear(FIRST_PAYMENT);
        driverTools.waitForElementToBeClickable(FIRST_PAYMENT);
        driverTools.click(FIRST_PAYMENT);
    }

    public void clickChangePaymentMethod() {
        driverTools.waitForElementToAppear(CHANGE_PAYMENT_METHOD_BUTTON);
        driverTools.waitForElementToBeClickable(CHANGE_PAYMENT_METHOD_BUTTON);
        driverTools.click(CHANGE_PAYMENT_METHOD_BUTTON);
    }

    public boolean verifyEligiblePromo() {
        driverTools.waitForElementToAppear(ELIGIBLE_PROMO);
        return driverTools.isElementDisplayed(ELIGIBLE_PROMO);
    }

    public boolean verifyNonEligiblePromo() {
        driverTools.waitForElementToAppear(NON_ELIGIBLE_PROMO);
        return driverTools.isElementDisplayed(NON_ELIGIBLE_PROMO);
    }

    public void clickUnApplyButton() {
        driverTools.waitForElementToAppear(UNAPPLY_BUTTON).ifPresent(driverTools::click);
    }

    public void clickRemovePromo() {
        driverTools.waitForElementToAppear(REMOVE_PROMO_BUTTON).ifPresent(driverTools::click);
    }

    public boolean isPromoInstantUnApplied(){
        driverTools.waitForElementToAppear(SEE_PROMOS_VOUCHERS);
        return driverTools.isElementDisplayed(SEE_PROMOS_VOUCHERS);
    }

    public void clickContinueWithPoints(){
        driverTools.waitForElementToAppear(CONTINUE_WITH_POINTS).ifPresent(driverTools::click);
    }
    public void clickContinueWithoutPromo(){
        driverTools.waitForElementToAppear(CONTINUE_WITHOUT_PROMO).ifPresent(driverTools::click);
    }

    public void clickOnSeeAll(){
        driverTools.waitForElementToAppear(SEE_ALL_METHOD);
        driverTools.clickWithJavascriptExecutor(SEE_ALL_METHOD);
    }

    public boolean verifyErrorMsg(String errorMsgEN, String errorMsgID) {
        boolean isErrorMessageDisplayed = driverTools.waitForElementToAppear(ERROR_MSG_NOT_APPLICABLE_PROMO).isPresent();
        if (isErrorMessageDisplayed) {
            String errorMsg = String.valueOf(driverTools.getText(ERROR_MSG_NOT_APPLICABLE_PROMO));
            return (errorMsg.contains(errorMsgEN) || errorMsg.contains(errorMsgID));
        } else {
            logger.log("No error message is displayed");
            return false;
        }
    }

    public boolean isFreePaymentOrRefund(String reschedulePrice){
        boolean isPriceNegative = reschedulePrice.contains("-");
        boolean isPriceZero = CoreFunctions.convertPrice(reschedulePrice) == 0;
        return isPriceNegative || isPriceZero;
    }
    public boolean isPaymentPagepresent(){
       return driverTools.waitForElementToAppear(PAYMENT_PAGE_CHECK).isPresent();
    }

    public void selectCCOption(){
        driverTools.waitForElementToAppear(VIEW_CARD);
        driverTools.click(VIEW_CARD);

        driverTools.click(CVCV);
        driverTools.sendKeys(CVCV,"123");

    }

    public boolean isB2bPopupVisible(){
        driverTools.waitForElementToAppear(B2B_POPUP,5);
        return driverTools.isElementDisplayed(B2B_POPUP);

    }

    public boolean isCorporateBillingInsufficientBalancePopupDisplayed() {
        driverTools.waitForElementToAppear(CORPORATE_BILLING_INSUFFICIENT_BALANCE_POPUP, 15);
        return driverTools.isElementDisplayed(CORPORATE_BILLING_INSUFFICIENT_BALANCE_POPUP);
    }

    public String getCorporateBillingInsufficientBalancePopupTitle() {
        WebElement titleElement = driverTools.waitForElementToAppear(CORPORATE_BILLING_INSUFFICIENT_BALANCE_TITLE).orElseThrow();
        String title = driverTools.getText(titleElement);
        logger.log("Corporate billing insufficient balance popup title: " + title);
        return title;
    }

    public String getCorporateBillingInsufficientBalancePopupDescription() {
        WebElement descriptionElement = driverTools.waitForElementToAppear(CORPORATE_BILLING_INSUFFICIENT_BALANCE_DESCRIPTION).orElseThrow();
        String description = driverTools.getText(descriptionElement);
        logger.log("Corporate billing insufficient balance popup description: " + description);
        return description;
    }

    public boolean isCorporateBillingInsufficientBalanceOkButtonDisplayed() {
        driverTools.waitForElementToAppear(CORPORATE_BILLING_INSUFFICIENT_BALANCE_OK_BUTTON, 10);
        return driverTools.isElementDisplayed(CORPORATE_BILLING_INSUFFICIENT_BALANCE_OK_BUTTON);
    }

    public void clickCorporateBillingInsufficientBalanceOkButton() {
        driverTools.waitForElementToAppear(CORPORATE_BILLING_INSUFFICIENT_BALANCE_OK_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CORPORATE_BILLING_INSUFFICIENT_BALANCE_OK_BUTTON);
        driverTools.waitForElementToDisappear(CORPORATE_BILLING_INSUFFICIENT_BALANCE_POPUP, 10);
    }

    public boolean isCorporateBillingInsufficientBalancePopupDismissed() {
        return driverTools.waitForElementToDisappear(CORPORATE_BILLING_INSUFFICIENT_BALANCE_POPUP, 10);
    }

    public boolean isCorporateBillingPaymentOptionDisplayed() {
        return driverTools.waitForElementToAppear(CORPORATE_BILLING_PAYMENT_TILE, 5).isPresent();
    }

    public boolean isPayWithCorporateBillingButtonDisplayed() {
        return driverTools.waitForElementToAppear(PAY_WITH_CORPORATE_BILLING_BUTTON, 5).isPresent();
    }

    public boolean isInactiveEmployeeValidationDisplayed() {
        driverTools.waitForElementToAppear(INACTIVE_EMPLOYEE_VALIDATION_MESSAGE, 15);
        return driverTools.isElementDisplayed(INACTIVE_EMPLOYEE_VALIDATION_MESSAGE);
    }

    public String getInactiveEmployeeValidationText() {
        WebElement validationElement = driverTools.waitForElementToAppear(INACTIVE_EMPLOYEE_VALIDATION_MESSAGE).orElseThrow();
        String validationText = driverTools.getText(validationElement);
        logger.log("Inactive employee validation text: " + validationText);
        return validationText;
    }

    public boolean isCorporateSuspendedBookingPopupDisplayed() {
        driverTools.waitForElementToAppear(CORPORATE_SUSPENDED_BOOKING_POPUP, 15);
        return driverTools.isElementDisplayed(CORPORATE_SUSPENDED_BOOKING_POPUP);
    }

    public String getCorporateSuspendedBookingPopupTitle() {
        WebElement titleElement = driverTools.waitForElementToAppear(CORPORATE_SUSPENDED_BOOKING_TITLE).orElseThrow();
        String title = driverTools.getText(titleElement);
        logger.log("Corporate suspended booking popup title: " + title);
        return title;
    }

    public String getCorporateSuspendedBookingPopupDescription() {
        WebElement descriptionElement = driverTools.waitForElementToAppear(CORPORATE_SUSPENDED_BOOKING_DESCRIPTION).orElseThrow();
        String description = driverTools.getText(descriptionElement);
        logger.log("Corporate suspended booking popup description: " + description);
        return description;
    }

    public boolean isCorporateSuspendedBookingOkButtonDisplayed() {
        driverTools.waitForElementToAppear(CORPORATE_SUSPENDED_BOOKING_OK_BUTTON, 10);
        return driverTools.isElementDisplayed(CORPORATE_SUSPENDED_BOOKING_OK_BUTTON);
    }

    public void clickCorporateSuspendedBookingOkButton() {
        driverTools.waitForElementToAppear(CORPORATE_SUSPENDED_BOOKING_OK_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CORPORATE_SUSPENDED_BOOKING_OK_BUTTON);
        driverTools.waitForElementToDisappear(CORPORATE_SUSPENDED_BOOKING_POPUP, 10);
    }

    public boolean isCorporateSuspendedBookingPopupDismissed() {
        return driverTools.waitForElementToDisappear(CORPORATE_SUSPENDED_BOOKING_POPUP, 10);
    }

    public boolean isCorporateInactiveOrSuspendedValidationDisplayed() {
        driverTools.waitForElementToAppear(CORPORATE_INACTIVE_SUSPENDED_VALIDATION_MESSAGE, 15);
        return driverTools.isElementDisplayed(CORPORATE_INACTIVE_SUSPENDED_VALIDATION_MESSAGE);
    }

    public String getCorporateInactiveOrSuspendedValidationText() {
        WebElement validationElement = driverTools.waitForElementToAppear(CORPORATE_INACTIVE_SUSPENDED_VALIDATION_MESSAGE).orElseThrow();
        String validationText = driverTools.getText(validationElement);
        logger.log("Corporate inactive or suspended validation text: " + validationText);
        return validationText;
    }

    public void selectKredivoInstallment() {
        driverTools.waitForElementToBeClickable(KREDIVO_INSTALLMENT_INPUT, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(KREDIVO_INSTALLMENT_3_PERCENT, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public boolean payKredivoSimulator() {
        try {
            Optional<WebElement> phoneField = driverTools.waitForElementToAppear(KREDIVO_FIELD_PHONE, 10);
            if (phoneField.isEmpty()) {
                logger.log("Kredivo simulator: phone field not found");
                return false;
            }
            phoneField.ifPresent(el -> {
                driverTools.clear(el);
                driverTools.sendKeys(el, "89638337953");
            });
            driverTools.waitForElementToAppear(KREDIVO_FIELD_PIN, 10).ifPresent(el -> {
                driverTools.clear(el);
                driverTools.sendKeys(el, "131212");
            });
            driverTools.waitForElementToAppear(KREDIVO_BTN_MASUK, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToAppear(KREDIVO_BTN_LANJUTKAN, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToAppear(KREDIVO_DIV_MASKED_NUMBER, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToAppear(KREDIVO_OTP_FIELD_1, 10).ifPresent(el -> driverTools.sendKeys(el, "4"));
            driverTools.waitForElementToAppear(KREDIVO_OTP_FIELD_2, 10).ifPresent(el -> driverTools.sendKeys(el, "5"));
            driverTools.waitForElementToAppear(KREDIVO_OTP_FIELD_3, 10).ifPresent(el -> driverTools.sendKeys(el, "6"));
            driverTools.waitForElementToAppear(KREDIVO_OTP_FIELD_4, 10).ifPresent(el -> driverTools.sendKeys(el, "7"));
            driverTools.waitForElementToAppear(KREDIVO_BTN_VERIFIKASI, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToAppear(KREDIVO_BTN_LANJUT_TANPA_KREDISHIELD, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToAppear(KREDIVO_BTN_SETUJU, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToAppear(KREDIVO_BTN_KEMBALI_KE_TIKET, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            return true;
        } catch (Exception e) {
            logger.log("Kredivo simulator failed: " + e.getMessage());
            return false;
        }
    }

    public boolean payAkulakuSimulator() {
        try {
            Optional<WebElement> loginBtn = driverTools.waitForElementToAppear(AKULAKU_BTN_LOGIN_ACCOUNT, 10);
            if (loginBtn.isEmpty()) {
                logger.log("Akulaku simulator: login button not found");
                return false;
            }
            loginBtn.ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.switchToNewTab();
            driverTools.waitForElementToAppear(AKULAKU_FIELD_PHONE, 10).ifPresent(el -> {
                driverTools.clear(el);
                driverTools.clickWithJavascriptExecutor(el);
                driverTools.sendKeys(el, "080708000135");
            });
            driverTools.waitForElementToAppear(AKULAKU_FIELD_PASSWORD, 10).ifPresent(el -> {
                driverTools.clear(el);
                driverTools.clickWithJavascriptExecutor(el);
                driverTools.sendKeys(el, "a123456789");
            });
            driverTools.waitForElementToAppear(AKULAKU_BTN_LOGIN, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToAppear(AKULAKU_BTN_CONFIRM_PAYMENT, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToAppear(AKULAKU_BTN_CONFIRM_PAYMENT, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToAppear(AKULAKU_BTN_BACK_TO_SHOP, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            return true;
        } catch (Exception e) {
            logger.log("Akulaku simulator failed: " + e.getMessage());
            return false;
        }
    }

    public boolean payGopaySimulator() {
        try {
            Optional<WebElement> payBtn = driverTools.waitForElementToAppear(GOPAY_BTN_PAY, 10);
            if (payBtn.isEmpty()) {
                logger.log("Gopay simulator: pay button not found");
                return false;
            }
            payBtn.ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToAppear(GOPAY_PIN_INPUT, 10).ifPresent(el -> {
                driverTools.clear(el);
                driverTools.sendKeys(el, "112233");
            });
            driverTools.waitForElementToAppear(GOPAY_BTN_BACK_TO_MERCHANT, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            return true;
        } catch (Exception e) {
            logger.log("Gopay simulator failed: " + e.getMessage());
            return false;
        }
    }

    public boolean payDanaSimulator() {
        try {
            Optional<WebElement> payBtn = driverTools.waitForElementToAppear(DANA_BTN_PAY, 10);
            if (payBtn.isEmpty()) {
                logger.log("DANA simulator: pay button not found");
                return false;
            }
            payBtn.ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToAppear(DANA_BTN_CLOSE,10).ifPresent(driverTools::clickWithJavascriptExecutor);
            return true;
        } catch (Exception e) {
            logger.log("DANA simulator failed: " + e.getMessage());
            return false;
        }
    }

    public boolean isAddOnPresent() throws InterruptedException {
        openPriceSummary();
        boolean isAddOnPresent = driverTools.waitForElementToAppear(ADD_ON_SECTION, 5).isPresent();
        closeDrawer();
        return isAddOnPresent;
    }

    public boolean payBsiSnapVaSimulator(String virtualAccountNo, long totalAmount) {
        try {
            driverTools.get("https://athena.ifortepay.id/virtual-account/snap");
            Optional<WebElement> vaField = driverTools.waitForElementToAppear(IFORTE_SNAP_FIELD_VA, 10);
            if (vaField.isEmpty()) {
                logger.log("BSI Snap VA simulator: VA input field not found");
                return false;
            }
            vaField.ifPresent(el -> {
                driverTools.clear(el);
                driverTools.sendKeys(el, virtualAccountNo);
            });
            driverTools.waitForElementToAppear(IFORTE_SNAP_SELECT_CHANNEL, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToAppear(BSI_SNAP_OPTION_BSI, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToAppear(IFORTE_SNAP_BTN_SEARCH, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            Locator vaNumberDisplay = new Locator("BSI_SNAP_VA_NUMBER", By.xpath("//p[normalize-space()='" + virtualAccountNo + "']"));
            boolean vaVisible = driverTools.waitForElementToAppear(vaNumberDisplay, 10).isPresent();
            if (!vaVisible) {
                logger.log("BSI Snap VA simulator: VA number not found after search");
                return false;
            }
            driverTools.waitForElementToAppear(IFORTE_SNAP_BTN_PAY_VA, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            boolean success = driverTools.waitForElementToAppear(IFORTE_SNAP_TXT_SUCCESS, 10).isPresent();
            if (!success) {
                logger.log("BSI Snap VA simulator: payment success text not found");
            }
            return success;
        } catch (Exception e) {
            logger.log("BSI Snap VA simulator failed: " + e.getMessage());
            return false;
        }
    }

    public boolean payPermataSnapVaSimulator(String virtualAccountNo, long totalAmount) {
        try {
            driverTools.get("https://athena.ifortepay.id/virtual-account/snap");
            Optional<WebElement> vaField = driverTools.waitForElementToAppear(IFORTE_SNAP_FIELD_VA, 10);
            if (vaField.isEmpty()) {
                logger.log("Permata Snap VA simulator: VA input field not found");
                return false;
            }
            vaField.ifPresent(el -> {
                driverTools.clear(el);
                driverTools.sendKeys(el, virtualAccountNo);
            });
            driverTools.waitForElementToAppear(IFORTE_SNAP_SELECT_CHANNEL, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToAppear(PERMATA_SNAP_OPTION_PERMATA, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToAppear(IFORTE_SNAP_BTN_SEARCH, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            Locator vaNumberDisplay = new Locator("PERMATA_SNAP_VA_NUMBER", By.xpath("//p[normalize-space()='" + virtualAccountNo + "']"));
            boolean vaVisible = driverTools.waitForElementToAppear(vaNumberDisplay, 10).isPresent();
            if (!vaVisible) {
                logger.log("Permata Snap VA simulator: VA number not found after search");
                return false;
            }
            driverTools.waitForElementToAppear(IFORTE_SNAP_BTN_PAY_VA, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            boolean success = driverTools.waitForElementToAppear(IFORTE_SNAP_TXT_SUCCESS, 10).isPresent();
            if (!success) {
                logger.log("Permata Snap VA simulator: payment success text not found");
            }
            return success;
        } catch (Exception e) {
            logger.log("Permata Snap VA simulator failed: " + e.getMessage());
            return false;
        }
    }

    public boolean payBriSnapVaSimulator(String virtualAccountNo, long totalAmount) {
        try {
            driverTools.get("https://athena.ifortepay.id/virtual-account/snap");
            Optional<WebElement> vaField = driverTools.waitForElementToAppear(IFORTE_SNAP_FIELD_VA, 10);
            if (vaField.isEmpty()) {
                logger.log("BRI Snap VA simulator: VA input field not found");
                return false;
            }
            vaField.ifPresent(el -> {
                driverTools.clear(el);
                driverTools.sendKeys(el, virtualAccountNo);
            });
            driverTools.waitForElementToAppear(IFORTE_SNAP_SELECT_CHANNEL, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToAppear(BRI_SNAP_OPTION_BRI, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToAppear(IFORTE_SNAP_BTN_SEARCH, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            Locator vaNumberDisplay = new Locator("BRI_SNAP_VA_NUMBER", By.xpath("//p[normalize-space()='" + virtualAccountNo + "']"));
            boolean vaVisible = driverTools.waitForElementToAppear(vaNumberDisplay, 10).isPresent();
            if (!vaVisible) {
                logger.log("BRI Snap VA simulator: VA number not found after search");
                return false;
            }
            driverTools.waitForElementToAppear(IFORTE_SNAP_BTN_PAY_VA, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            boolean success = driverTools.waitForElementToAppear(IFORTE_SNAP_TXT_SUCCESS, 10).isPresent();
            if (!success) {
                logger.log("BRI Snap VA simulator: payment success text not found");
            }
            return success;
        } catch (Exception e) {
            logger.log("BRI Snap VA simulator failed: " + e.getMessage());
            return false;
        }
    }

    public boolean payBniSnapVaSimulator(String virtualAccountNo, long amount) {
        try {
            driverTools.get("https://portalbeta-v3.bni-ecollection.com/partner/simulator/bank-channel-simulator/index");
            Optional<WebElement> vaField = driverTools.waitForElementToAppear(BNI_SNAP_FIELD_VA, 10);
            if (vaField.isEmpty()) {
                logger.log("BNI Snap VA simulator: VA input field not found");
                return false;
            }
            vaField.ifPresent(el -> {
                driverTools.clear(el);
                driverTools.sendKeys(el, virtualAccountNo);
            });
            driverTools.click(BNI_SNAP_BTN_SEARCH);
            driverTools.waitForElementToAppear(BNI_SNAP_FIELD_AMOUNT, 10).ifPresent(el -> {
                driverTools.clear(el);
                driverTools.sendKeys(el, String.valueOf(amount));
            });
            driverTools.click(BNI_SNAP_BTN_FLAG);
            boolean success = driverTools.waitForElementToAppear(BNI_SNAP_TXT_SUCCESS, 10).isPresent();
            if (!success) {
                logger.log("BNI Snap VA simulator: payment not successful. notFound=" +
                    driverTools.waitForElementToAppear(BNI_SNAP_TXT_NOT_FOUND, 5).isPresent());
            }
            return success;
        } catch (Exception e) {
            logger.log("BNI Snap VA simulator failed: " + e.getMessage());
            return false;
        }
    }

    public void verifyBottomPriceDetailsMatchSummary(Map<String, String> summary) {
        String expectedTotal = summary.get("Total to pay");
        String actualStickyFooterPrice = driverTools.getText(STICKY_FOOTER_TOTAL_PRICE).orElseThrow(() -> new NoSuchElementException("Sticky footer total price not found"));
        Assertion.assertThat("Verify Total Payment on the sticky bottom bar matches the summary", actualStickyFooterPrice, is(expectedTotal));
        WebElement summaryTrigger = driverTools.waitForElementToBeClickable(PAYMENT_SUMMARY_TRIGGER, 5).orElseThrow(() -> new NoSuchElementException("Payment summary trigger not found"));
        driverTools.scrollElementToCenter(summaryTrigger);
        driverTools.clickWithJavascriptExecutor(summaryTrigger);
        driverTools.waitForElementToAppear(PRICE_DETAILS_MODAL_TITLE, 5).orElseThrow(() -> new NoSuchElementException("Price Details modal did not appear"));
        WebElement accordionBtn = driverTools.waitForElementToBeClickable(BUS_CLASS_ACCORDION_BTN, 5).orElseThrow(() -> new NoSuchElementException("Accordion button for class/tier not found"));
        String isExpanded = accordionBtn.getAttribute("class");
        if (isExpanded != null && !isExpanded.contains("opened")) {
            driverTools.clickWithJavascriptExecutor(accordionBtn);
        }
        String actualTax = driverTools.getText(MODAL_TAX_PRICE).orElseThrow(() -> new NoSuchElementException("Tax price not found in Price Details modal"));
        String actualTotal = driverTools.getText(MODAL_TOTAL_PRICE).orElseThrow(() -> new NoSuchElementException("Total price not found in Price Details modal"));
        String expectedTax = summary.get("Tax & other fees");
        Assertion.assertThat("Verify Total Price matches between Booking form and Payment page", actualTotal, is(expectedTotal));
        Assertion.assertThat("Verify Tax matches matches between Booking form and Payment page", actualTax, is(expectedTax));
    }
}