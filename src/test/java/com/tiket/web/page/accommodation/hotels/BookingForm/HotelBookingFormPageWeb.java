package com.tiket.web.page.accommodation.hotels.BookingForm;

import com.tiket.appium.PopupObserver;
import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.tiket.core.CoreFunctions.assertPriceEquals;
import static com.tiket.core.CoreFunctions.convertPrice;
import static com.tiket.core.CoreFunctions.doubleRound;
import static com.tiket.core.CoreFunctions.extractPriceAfterMarkers;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.Is.is;

public abstract class HotelBookingFormPageWeb<T extends WebDriver> extends WebPage<T> {
    private static final Locator LOGIN_BANNER = new Locator("LOGIN_BANNER", new By.ByXPath("//*[@data-testid='login-banner']//div[contains(@class,'click')]"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", new By.ByXPath("//span[text()='Continue' or text()='Lanjutkan']"));
    private static final Locator SELECT_PAYMENT_BTN = new Locator("SELECT_PAYMENT_BTN", new By.ByXPath("//*[contains(@class,'PriceSummarySection_complete_booking_button')]"));
    private static final Locator CONTINUE_BUTTON_TEXT = new Locator("CONTINUE_BUTTON_TEXT", new By.ByXPath("//span[contains(@class, 'Button_truncation') and (text()='Continue' or text()='Lanjutkan')]"));
    public static final Locator CONTACT_SALUTATION = new Locator("CONTACT_SALUTATION", new By.ByXPath("(//*[contains(@data-testid, 'contact-details-form')])//label[contains(@class, 'Radio_radio')]"));
    private static final Locator CONTACT_FIRST_SALUTATION = new Locator("CONTACT_FIRST_SALUTATION", new By.ByXPath("(//*[@data-testid='contact-details-form']//label[contains(@class,'Radio_radio')])[1]"));
    public static final Locator FULL_NAME_INPUT = new Locator("FULL_NAME_INPUT", new By.ByXPath("//input[@id='full-name-according-to-id']"));
    public static final Locator PHONE_NUMBER_INPUT = new Locator("PHONE_NUMBER_INPUT", new By.ByXPath("//input[@id='mobile-number']"));
    public static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", new By.ByXPath("//input[@id='email-address']"));
    public static final Locator EMAIL_DISABLED = new Locator("EMAIL_DISABLED", new By.ByXPath("(//*[contains(@class, 'styles_state_disabled')])[2]"));
    private static final Locator CONTACT_CARD = new Locator("CONTACT_CARD", new By.ByXPath("//h3[contains(@class, 'Text_size_h3')]"));
    private static final Locator ENTER_GUEST_NAME_BUTTON = new Locator("ENTER_GUEST_NAME_BUTTON", new By.ByXPath("(//div[contains(@class, 'FormItem_room_guest_row')]//span)[2]"));
    private static final Locator INPUT_GUEST_NAME = new Locator("INPUT_GUEST_NAME", new By.ByXPath("(//input[contains(@data-testid, 'guest-form-full-name-text-input')])[1]"));
    protected static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[contains(@class,'Button_button') and .//span[normalize-space()='Save' or normalize-space()='Simpan']]"));
    private static final Locator CONTINUE = new Locator("CONTINUE", new By.ByXPath("//button[contains(@class,'PriceFooter_continue_button')]//span[text()='Continue' or text()='Lanjutkan']"));
    protected static final Locator SAVE_GUEST_BUTTON = new Locator("SAVE_GUEST_BUTTON", new By.ByXPath("//footer[contains(@class, 'Modal_modal_footer')]//button"));
    protected static final Locator SPECIAL_REQUEST_BUTTON = new Locator("SPECIAL_REQUEST_BUTTON", new By.ByXPath("//span[contains(@class, 'special')]"));
    private static final Locator SPECIAL_REQUEST_CHIPS = new Locator("SPECIAL_REQUEST_CHIPS", new By.ByXPath("//*[@data-testid='special-request-chips-container']/*[contains(@class,'PopupSpecialRequestForm') and count(*) = 1]"));
    private static final Locator SPECIAL_REQUEST_SAVE = new Locator("SPECIAL_REQUEST_SAVE", new By.ByXPath("//button[contains(@class,'Button_variant_primary') and contains(@class,'Button_size_large') and (span[contains(text(),'Simpan')] or span[contains(text(),'Save')])]"));
    private static final Locator ADDONS_NAME = new Locator("ADDONS_NAME", new By.ByXPath("//*/*[contains(@class,'Header_title_text')]"));
    private static final Locator ADDONS_PRICE_PER_UNIT = new Locator("ADDONS_PRICE_PER_UNIT", new By.ByXPath("(//*/*[contains(@class,'Header_price_text')]//span)[1]"));
    private static final Locator ADDONS_BUTTON = new Locator("ADDONS_BUTTON", new By.ByXPath("//*[@data-testid='add-on-card-item-container']//button"));
    private static final Locator ADDONS_RADIO_BUTTON = new Locator("ADDONS_RADIO_BUTTON", new By.ByXPath("//span[contains(@class, 'Check_custom_checkbox')]"));
    private static final Locator ADDONS_INC_BUTTON = new Locator("ADDONS_INC_BUTTON", new By.ByXPath("//button[contains(@class, 'QuantityEditor_operation_button')][2]"));
    private static final Locator ADDONS_SAVE_BUTTON = new Locator("ADDONS_SAVE_BUTTON", new By.ByXPath("//div[contains(@class, 'Footer_footer')]//button"));
    private static final Locator FINAL_TOTAL_PRICE = new Locator("FINAL_TOTAL_PRICE", new By.ByXPath("//*[contains(@class, 'PriceFooter_final_price_text')]"));
    private static final Locator CROSS_TOTAL_PRICE = new Locator("CROSS_TOTAL_PRICE", new By.ByXPath("//*[contains(@class, 'Text_diagonal_strikethrough')]"));
    private static final Locator TOTAL_PRICE_LABEL = new Locator("TOTAL_PRICE_LABEL", new By.ByXPath("//*[contains(@class,'PriceFooter_price_row')]//*[contains(text(),'Total') and (contains(text(),'after taxes') or contains(text(),'setelah pajak'))]"));
    private static final Locator INSURANCE_NAME = new Locator("INSURANCE_NAME", new By.ByXPath("//*[contains(@class,'InsuranceTabContent_title_text')]/*[contains(@class,'Text')]"));
    private static final Locator INSURANCE_BUTTON = new Locator("INSURANCE_BUTTON", new By.ByXPath("//div[contains(@class, 'insurance_tab')]//button"));
    private static final Locator INSURANCE_PRICE = new Locator("INSURANCE_PRICE", new By.ByXPath("//div[contains(@class, 'InsuranceTabContent_footer_text_price')]//*[contains(@class,'Text')]"));
    private static final Locator INSURANCE_GIFT_VOUCHER_SECTION = new Locator("INSURANCE_GIFT_VOUCHER_SECTION", new By.ByXPath("//div[contains(@class,'insurance_tab')]/ancestor::*[.//*[contains(@class,'GiftVoucherClaimWidget')]][1]//*[contains(@class,'GiftVoucherClaimWidget')]"));
    private static final Locator INSURANCE_GIFT_VOUCHER_CLAIM_BTN = new Locator("INSURANCE_GIFT_VOUCHER_CLAIM_BTN", new By.ByXPath("(//div[contains(@class,'insurance_tab')]/ancestor::*[.//*[contains(@class,'GiftVoucherClaimWidget')]][1]//button[contains(@class,'GiftVoucherCard_claim_button') and not(contains(@class,'Button_state_disabled'))])[2]"));
    private static final Locator INSURANCE_GIFT_VOUCHER_CLAIMED_BTN = new Locator("INSURANCE_GIFT_VOUCHER_CLAIMED_BTN", new By.ByXPath("( //div[contains(@class,'insurance_tab')]/ancestor::*[.//*[contains(@class,'GiftVoucherClaimWidget')]][1]//button[contains(@class,'GiftVoucherCard_claim_button') and contains(@class,'Button_state_disabled')][.//span[normalize-space()='Claimed' or normalize-space()='Diklaim']])[2]"));
    private static final Locator GIFT_VOUCHER_CLAIMED_SNACKBAR = new Locator("GIFT_VOUCHER_CLAIMED_SNACKBAR", new By.ByXPath("//div[contains(@class,'Snackbar_text_content_') and (contains(text(), 'Nice! Vouchers will go to your Voucher Box after payment of this transaction.') or contains(text(), 'Mantul! Voucher akan masuk ke Voucher Box-mu setelah pembayaran transaksi ini.'))]"));
    private static final Locator PICK_PAY_METHOD_BUTTON = new Locator("PICK_PAY_METHOD_BUTTON", new By.ByXPath("//*[@data-testid='pick-when-to-pay-pay-now']"));
    public static final Locator SELECT_PAYMENT_BUTTON = new Locator("SELECT_PAYMENT_BUTTON", new By.ByXPath("//div[contains(@class,'action_section')]//button[contains(@class,'Button_variant_primary')] | //button[contains(@class,'PriceSummarySection_complete_booking_button')] | //button[.//span[normalize-space()='Select Payment' or normalize-space()='Pilih Pembayaran' or normalize-space()='Continue to payment' or normalize-space()='Lanjut ke pembayaran']]"));
    private static final Locator PAYMENT_PAGE_SUBMIT_BUTTON = new Locator("PAYMENT_PAGE_SUBMIT_BUTTON", By.cssSelector("[data-testid='submit_button']"));
    private static final Locator PAYMENT_METHOD_SECTION = new Locator("PAYMENT_METHOD_SECTION", By.cssSelector("[data-testid='payment-method']"));
    private static final Locator GUEST_STAY_DETAILS_CARD = new Locator("GUEST_STAY_DETAILS_CARD", new By.ByXPath("//div[contains(@class, 'StayDetails')]"));
    private static final Locator BOOKING_FORM_HOTEL_NAME = new Locator("BOOKING_FORM_HOTEL_NAME", new By.ByXPath("//*[@data-testid='item-name']"));
    private static final Locator SEE_OTHER_ROOMS = new Locator("SEE_OTHER_ROOMS", new By.ByXPath("//div[contains(@class, 'Figure_footer')]//button[1]"));
    private static final Locator SPECIAL_REQUEST_TEXT = new Locator("SPECIAL_REQUEST_TEXT", new By.ByXPath("//*/*[contains(@class,'FormItem_special_request_text')]"));
    private static final Locator SPECIAL_REQUEST_TEXTAREA = new Locator("SPECIAL_REQUEST_TEXTAREA", new By.ByXPath("//textarea"));
    private static final Locator GUEST_NAME = new Locator("GUEST_NAME", new By.ByXPath("//span[contains(@class, 'guest_name')]"));
    private static final Locator BOOKING_PRICE = new Locator("BOOKING_PRICE", new By.ByXPath("//p[contains(@class,'PriceFooter_final_price_text')]"));
    private static final Locator BOOKING_DATES = new Locator("BOOKING_DATES", new By.ByXPath("//p[contains(@data-testid, 'item-date')]"));
    private static final Locator BOOKING_ROOM_NIGHT = new Locator("BOOKING_DATES", new By.ByXPath("//p[contains(@data-testid, 'item-room-night')]"));
    private static final Locator CLOSE_MODAL = new Locator("BOOKING_DATES",new By.ByXPath("//button[contains(@class, 'BottomSheet_close_button')]"));
    private static final Locator BOOK_NAME_BUTTON = new Locator("BOOKING_DATES", new By.ByXPath("(//span[contains(@class, 'guest_name')])[2]"));
    private static final Locator INPUT_BOOK_NAME = new Locator("BOOKING_DATES", new By.ByXPath("(//input[contains(@data-testid, 'guest-form-full-name-text-input')])[2]"));
    private static final Locator ROOM_NAME = new Locator("ROOM_NAME", new By.ByXPath("//h4[contains(@class, 'FormSubheading_title_text') and contains(@class, 'Text_variant_highEmphasis')]"));
    private static final Locator B2B_MULTIGUEST_ERROR = new Locator("B2B_MULTIGUEST_ERROR", new By.ByXPath("//button[text()='Select 1 room type' or text()='Pilih 1 tipe kamar']"));
    private static final Locator B2B_EMAIL_MULTIGUEST_ERROR = new Locator("B2B_EMAIL_MULTIGUEST_ERROR", new By.ByXPath("//h2[contains(normalize-space(text()),'This booking does not apply to corporate accounts') or contains(normalize-space(text()),'Pemesanan ini tidak berlaku untuk akun korporat')]"));
    private static final Locator FULLY_BOOKED_NOTIF = new Locator("HOTEL_FULLY_BOOKED_NOTIF", By.xpath("//h2[contains(@class,'Figure_title') and (contains(text(),'fully booked') or contains(text(),'sudah penuh'))]"));
    private static final Locator HOTEL_INSURANCE_PAGE = new Locator("HOTEL_INSURANCE_PAGE", By.xpath("//span[contains(@class,'SegmentedControl_label') and contains(text(),'Premium')]"));
    private static final Locator HOTEL_ADD_ONS_PAGE = new Locator("HOTEL_ADD_ONS_PAGE", By.xpath("//h3[contains(@class,'Text_size_h3') and (contains(text(),'Add-ons') or contains(text(),'Fasilitas Ekstra'))]"));
    public static final Locator ROOM_NAME_IN_PRICE_DETAIL = new Locator("ROOM_NAME_IN_PRICE_DETAIL", By.xpath("(//div[contains(@class,'PriceSummary_sub_accordion_header')]//span)[2]"));
    public static final Locator TOTAL_PRICE_IN_PRICE_DETAIL = new Locator("TOTAL_PRICE_IN_PRICE_DETAIL", By.xpath("//span[contains(@class, 'PriceDetail_price_text') and contains(@class, 'Text_weight_bold') ]"));
    private static final Locator PRICE_PDP = new Locator("PRICE_PDP", By.xpath("//p[contains(@class, 'PriceFooter_final_price_text')]"));
    private static final Locator PRICE_SUMMARY_TITLE = new Locator("PRICE_SUMMARY_TITLE", new By.ByXPath("//*[contains(@class, 'PriceSummary_accordion_title_section')]"));
    private static final Locator CHEVRON_PRICE = new Locator("CHEVRON_PRICE", By.xpath("//*[contains(@class, 'PriceSummary_sub_accordion_content_wrapper')]"));
    private static final Locator RADIO_BUTTON_MR = new Locator("RADIO_BUTTON_MR", By.xpath("(//input[contains(@class, 'Radio_input')][@name='title'])[1]"));
    private static final Locator DROPDOWN_AUTHON = new Locator("DROPDOWN_AUTHON", By.xpath("//svg[contains(@class, 'DropdownFormDisplay_chevron')][1]"));
    private static final Locator DROPDOWN_CHEVRON = new Locator("DROPDOWN_CHEVRON", By.xpath("//svg[contains(@class, 'DropdownFormDisplay_chevron')][2]"));
    private static final Locator DROPDOWN_OPTION_OPT1 = new Locator("DROPDOWN_OPTION_OPT1", By.cssSelector("div[data-testid='option-single-0']"));
    private static final Locator ZGLY_AUTO_OPTION = new Locator("ZGLY_AUTO_OPTION", By.cssSelector("div[data-testid='option-single-0']"));
    private static final Locator POP_UP_PAYMENT = new Locator("POP_UP_PAYMENT", new By.ByXPath("//button[normalize-space(text())='Go to the payment page']"));
    private static final Locator POP_UP_CONTINUE_BOOKING = new Locator("POP_UP_CONTINUE_BOOKING", new By.ByXPath("//*[contains(@class,'Dialog_pop_up')]//button[contains(@class,'Button_accessibility_button')]"));
    private static final Locator POP_UP_SEE_CHANGES_AND_CONTINUE = new Locator("POP_UP_SEE_CHANGES_AND_CONTINUE", new By.ByXPath("//button[contains(@class,'Button_accessibility_button')][.//span[normalize-space()='See changes & continue booking' or normalize-space()='Lihat perubahan & lanjut pesan']]"));
    private static final Locator POP_UP_CONTINUE_BOOKING_TEXT = new Locator("POP_UP_CONTINUE_BOOKING_TEXT", new By.ByXPath("//button[contains(@class,'Button_accessibility_button')][.//span[normalize-space()='Continue booking' or normalize-space()='Lanjutkan pemesanan']]"));
    /** Rate / cancellation-policy change dialog: the "See other rooms" secondary button that returns to the room list. */
    private static final Locator POP_UP_SEE_OTHER_ROOMS = new Locator("POP_UP_SEE_OTHER_ROOMS", new By.ByXPath("//button[contains(@class,'Button_variant_secondary')][.//span[normalize-space()='See other rooms' or normalize-space()='Lihat kamar lain']]"));
    private static final Locator NAVIGATE_BACK_CONFIRMATION_POPUP = new Locator("NAVIGATE_BACK_CONFIRMATION_POPUP", new By.ByXPath("//div[@id='modal-root']//div[contains(@class,'Dialog')]//div[contains(@class,'button_container')]//button[contains(@class,'Button_variant_secondary')]"));
    /** Booking form step 1: rate plan change dialog (Dialog). */
    private static final Locator RATE_PLAN_CHANGE_DIALOG_PRIMARY_BTN = new Locator("RATE_PLAN_CHANGE_DIALOG_PRIMARY_BTN", new By.ByXPath(
            "//div[contains(@class,'Dialog_dialog')]//button[contains(@class,'Button_variant_primary') and contains(@class,'Button_size_large')]"));
    private static final Locator RATE_PLAN_CHANGE_DIALOG = new Locator("RATE_PLAN_CHANGE_DIALOG", new By.ByXPath("//div[contains(@class,'Dialog_dialog')]"));
    /** Booking form step 1: body of any rate / cancellation-policy change dialog (used to read its text). */
    private static final Locator RATE_CHANGE_DIALOG_BODY = new Locator("RATE_CHANGE_DIALOG_BODY", new By.ByXPath("//div[contains(@class,'Dialog_pop_up') or contains(@class,'Dialog_dialog')]"));
    /** Booking form step 1: price-change snackbar (e.g. "The total price has changed from IDR ... to IDR ..."). */
    private static final Locator PRICE_CHANGE_SNACKBAR = new Locator("PRICE_CHANGE_SNACKBAR", new By.ByXPath(
            "//div[contains(@class,'Snackbar_snackbar')][.//div[contains(@class,'Snackbar_text_content') and (contains(.,'price has changed') or contains(.,'harga') )]]"));
    private static final Locator PRICE_CHANGE_SNACKBAR_TEXT = new Locator("PRICE_CHANGE_SNACKBAR_TEXT", new By.ByXPath(
            "//div[contains(@class,'Snackbar_snackbar')]//div[contains(@class,'Snackbar_text_content')]"));
    private static final Locator PRICE_CHANGE_SNACKBAR_OK_BTN = new Locator("PRICE_CHANGE_SNACKBAR_OK_BTN", new By.ByXPath(
            "//div[contains(@class,'Snackbar_snackbar')]//button[contains(@class,'Snackbar_action_button')]"));
    /** Booking form step 2: combination changes footer modal. */
    private static final Locator COMBINATION_CHANGES_FOOTER_PRIMARY_BTN = new Locator("COMBINATION_CHANGES_FOOTER_PRIMARY_BTN", new By.ByXPath(
            "//footer[contains(@class,'Modal_modal_footer')]//div[contains(@class,'CombinationChangesPopup_combination_changes_footer_modal')]//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator ADDON_PRICE = new Locator("ADDON_PRICE", By.xpath("//span[normalize-space()='Add-ons']/ancestor::div[@class='undefined']//div[contains(@class,'PriceSummary_sub_accordion_item_level_3')]//div[last()]//span"));
    private static final Locator TOTAL_ADDON_PRICE = new Locator("TOTAL_ADDON_PRICE", By.xpath("//p[@data-testid='final-total-amount-text']/following::span[1]"));
    private static final Locator SELECT_CHEVRON_BUTTON = new Locator("SELECT_CHEVRON_BUTTON", new By.ByXPath("(//div[contains(@class,'group_field_body_chevron_container')])[2]"));
    private static final Locator CHEVRON_INFO = new Locator("CHEVRON_INFO", new By.ByXPath("//h3[@data-testid='reservation-info-hotel-name-text']"));
    private static final Locator ENTER_GUEST_NAME=new Locator("ENTER_GUEST_NAME",new By.ByXPath("//span[contains(@class, 'FormItem_guest_name_text')]"));
    private static final Locator GUEST_NAME_POPUP=new Locator("GUEST_NAME_POPUP",new By.ByXPath("//input[@data-testid='guest-form-full-name-text-input']"));
    private static final Locator INSURANCE_PRICE_DETAIL=new Locator("INSURANCE_PRICE_DETAIL",new By.ByXPath("//div[contains(@class,'PriceSummary_sub_accordion_nested_content')]  //div[contains(@class,'PriceSummary_sub_accordion_header')]  /div[last()]//span"));
    private static final Locator PRICE_ON_FOOTER=new Locator("PRICE_ON_FOOTER",new By.ByXPath("//div[contains(@class,'PriceSummary_total_price_container')]/span[last()]"));
    private static final Locator GO_TO_PAYMENT_PAGE=new Locator("GO_TO_PAYMENT_PAGE",new By.ByXPath("//button[contains(@class,'PriceSummarySection_complete_booking_button')]"));
    private static final Locator REVIEW_CONTINUE_TO_PAYMENT_BUTTON = new Locator("REVIEW_CONTINUE_TO_PAYMENT_BUTTON", By.xpath(
            "//button[contains(@class,'rr___Button-module__variant_primary')][.//span[normalize-space()='Continue to payment'"
                    + " or normalize-space()='Lanjut ke pembayaran' or normalize-space()='Lanjut ke Pembayaran']]"));
    private static final Locator ROOM_COUNT_TEXT = new Locator("ROOM_COUNT_TEXT", By.xpath("//*[@data-testid='item-room-night']"));
    private static final Locator SUBTOTAL_OPEN_ICON = new Locator("SUBTOTAL_OPEN_ICON", new By.ByXPath("//span[normalize-space()='Subtotal']"));
    private static final Locator ROOM_OPEN_ICON = new Locator("ROOM_OPEN_ICON", new By.ByXPath("//span[contains(normalize-space(),'Room Type') or contains(normalize-space(),'Tipe Kamar')]"));
    private static final Locator SELECT_PAYMENT = new Locator("SELECT_PAYMENT", new By.ByXPath("//button//span[text()='Select Payment' or text()='Pilih Pembayaran']"));
    private static final Locator PRICE_SUMMARY_SUBTOTAL = new Locator("PRICE_SUMMARY_SUBTOTAL", new By.ByXPath("(//div[@data-testid='price-summary-container'])[1]//span[text()='Subtotal']/ancestor::div[contains(@class, 'accordion_header')]"));
    private static final Locator ROOM_PRICE_DETAILS = new Locator("ROOM_PRICE_DETAILS", new By.ByXPath("//*[(contains(.,' kamar') or contains(.,' room')) and contains(@class,'PriceSummary_text_with_dot_container')]/parent::*/following-sibling::*"));
    // Stay Details locators
    private static final Locator STAY_DETAILS_CHEVRON = new Locator("STAY_DETAILS_CHEVRON", new By.ByXPath("//div[contains(@class, 'GroupFieldBodyComponents_group_field_body_chevron_container')]"));
    private static final Locator STAY_DETAILS_BOTTOM_SHEET_TITLE = new Locator("STAY_DETAILS_BOTTOM_SHEET_TITLE", new By.ByXPath("//h2[contains(@class, 'BottomSheet_body_title') and (text()='Stay Details' or text()='Detail Menginap')]"));
    private static final Locator STAY_DETAILS_HOTEL_NAME = new Locator("STAY_DETAILS_HOTEL_NAME", new By.ByXPath("//h3[@data-testid='reservation-info-hotel-name-text']"));
    private static final Locator STAY_DETAILS_ROOM_NAME = new Locator("STAY_DETAILS_ROOM_NAME", new By.ByXPath("//div[@data-testid='additional-info-container']//p[contains(@class, 'AdditionalInfo_title')]"));
    private static final Locator STAY_DETAILS_ROOM_COUNT = new Locator("STAY_DETAILS_ROOM_COUNT", new By.ByXPath("//div[@data-testid='number-of-room-info-content']//p"));
    private static final Locator STAY_DETAILS_NIGHT_COUNT = new Locator("STAY_DETAILS_NIGHT_COUNT", new By.ByXPath("//div[@data-testid='number-of-night-info-content']//p"));
    private static final Locator STAY_DETAILS_GUEST_COUNT = new Locator("STAY_DETAILS_GUEST_COUNT", new By.ByXPath("//div[@data-testid='number-of-guests-info-content']//p"));
    private static final Locator STAY_DETAILS_CANCELLATION_POLICY_CONTAINER = new Locator("STAY_DETAILS_CANCELLATION_POLICY_CONTAINER", new By.ByXPath("//div[@data-testid='cancellation-stay-policies-container']"));
    private static final Locator BOTTOM_SHEET_CLOSE_BUTTON = new Locator("BOTTOM_SHEET_CLOSE_BUTTON", new By.ByXPath("//button[contains(@class, 'BottomSheet_close_button') or @data-testid='modal-close-button']"));

    // Special Request form locators (for booking page)
    private static final Locator SPECIAL_REQUEST_FORM_CONTAINER = new Locator("SPECIAL_REQUEST_FORM_CONTAINER", new By.ByXPath("//div[@data-testid='special-request-chips-container']"));
    private static final Locator SPECIAL_REQUEST_FIRST_CHIP = new Locator("SPECIAL_REQUEST_FIRST_CHIP", new By.ByXPath("(//div[@data-testid='special-request-chips-container']//button[not(.//span[contains(@class, 'Chip_icon_right')])])[1]"));
    private static final Locator SPECIAL_REQUEST_SECOND_CHIP = new Locator("SPECIAL_REQUEST_SECOND_CHIP", new By.ByXPath("(//div[@data-testid='special-request-chips-container']//button[not(.//span[contains(@class, 'Chip_icon_right')])])[2]"));
    private static final Locator SPECIAL_REQUEST_OTHER_TEXTAREA = new Locator("SPECIAL_REQUEST_OTHER_TEXTAREA", new By.ByXPath("//textarea[@id='add-other-requests']"));

    private static final Locator PRICE_FOOTER_CONTINUE_BUTTON = new Locator("PRICE_FOOTER_CONTINUE_BUTTON", new By.ByXPath("//button[contains(@class, 'PriceFooter_continue_button')]"));
    private static final Locator PRICE_SUMMARY_CONTAINER = new Locator("PRICE_SUMMARY_CONTAINER", new By.ByXPath("//div[contains(@class, 'PriceSummarySection_price_summary_section_container')]"));
    public static final Locator PRICE_SUMMARY_TOTAL = new Locator("PRICE_SUMMARY_TOTAL", new By.ByXPath("//div[contains(@class, 'PriceSummary_total_price_container')]//span[contains(@class, 'Text_weight_bold')][2]"));
    private static final Locator PRICE_SUMMARY_DISCOUNT_LABEL = new Locator("PRICE_SUMMARY_DISCOUNT_LABEL", new By.ByXPath("(//div[@data-testid='price-summary-container'])[1]//span[text()='Discount' or text()='Diskon']"));
    private static final Locator PROMO_MODAL_OPEN = new Locator("PROMO_MODAL_OPEN", AppiumBy.xpath("//*[contains(@class, 'PromoCodeBanner_promo_footer')]"));
    private static final Locator PROMO_CODE_INPUT = new Locator("PROMO_CODE_INPUT", AppiumBy.xpath("//*[@data-testid='promo-code-search-input']"));
    private static final Locator PROMO_SECTION = new Locator("PROMO_SECTION", AppiumBy.xpath("//*[contains(@class,'PromoCodeBanner_promo_header')]"));
    private static final Locator SUITABLE_PROMO = new Locator("SUITABLE_PROMO", AppiumBy.xpath("//*[contains(@class,'PromoCodeBanner_promo_text')]//*[contains(@class,'Text_weight_regular')]"));
    private static final Locator PROMO_BUTTON = new Locator("PROMO_BUTTON", AppiumBy.xpath("//button[.//span[text()='Apply' or text()='Pakai']]"));
    private static final Locator PROMO_PRICE = new Locator("PROMO_PRICE",AppiumBy.xpath("//p[   contains(@class,'Text_text__') and   contains(@class,'Text_size_b2__') and   contains(@class,'Text_weight_semibold__') ]"));
    private static final Locator DISCOUNT_MODAL =new Locator("DISCOUNT_MODAL",AppiumBy.xpath("//span[normalize-space(text())='Discount' or normalize-space(text())='Diskon']")) ;
    private static final Locator PROMO_VALUE_DISCOUNT = new Locator("PROMO_VALUE_DISCOUNT",AppiumBy.xpath("(//span[contains(@class,'Text_variant_positive__')])[2]"));
    private static final Locator TOTAL_PRICE_FORM2 = new Locator("TOTAL_PRICE_FORM2",AppiumBy.xpath("(//div[contains(@class, 'PriceSummary_total_price_container__')]//span[contains(@class, 'Text_variant_highEmphasis__') and contains(@class, 'Text_weight_bold__')])[2]"));
    private static final Locator PROMO_FIELD = new Locator("PROMO_FIELD",AppiumBy.xpath("//input[@data-testid='promo-code-search-input']"));
    private static final Locator REQUIRED_PROMO = new Locator("REQUIRED_PROMO",AppiumBy.xpath("(//span[text()='Apply' or text()='Pakai'])[2]"));
    private static final Locator PROMO_AREA = PROMO_MODAL_OPEN;
    private static final Locator APPLY_BUTTON = PROMO_BUTTON;
    private static final Locator PAYING_AMOUNT_TEXT_GREEN = new Locator("PAYING_AMOUNT_TEXT_GREEN", new By.ByXPath("//span[@data-testid='benefit-banner-description']"));
    private static final Locator PAY_NOW_PRICE = new Locator("PAY_NOW_PRICE", new By.ByXPath("//*[contains(@class,'PropertyFee_property_fee_title_container')]/*[last()]"));
    private static final Locator PAY_UPON_CHECK_IN_PRICE = new Locator("PAY_UPON_CHECK_IN_PRICE", new By.ByXPath("//*[contains(@class,'PriceSummary_accordion_header')]/*[last()]/*"));
    private static final Locator PROPERTY_FEE_DISCLAIMER = new Locator("PROPERTY_FEE_DISCLAIMER", new By.ByXPath("(//*[@data-testid='property-fee-container']/*[last()]/*/*/*)[last()]"));
    private static final Locator BOOKING_FORM_PAGE2_PAY_UPON_CHECK_IN = new Locator("BOOKING_FORM_PAGE2_PAY_UPON_CHECK_IN", new By.ByXPath("(//div[contains(@class, 'PriceSummary_accordion_header')]/*)[last()]"));
    private static final Locator BOOKING_FORM_PAGE2_ESTIMATED_TOTAL = new Locator("BOOKING_FORM_PAGE2_ESTIMATED_TOTAL", new By.ByXPath("//div[contains(@class,'PriceSummarySection_property_fee_disclaimer_container')]/*[last()]/*/*"));
    private static final String[] ESTIMATED_TOTAL_MARKERS = {"Estimated total:", "Perkiraan total:"};
    private static final Locator FLEXIBLE_PAYMENT_BTN = new Locator("FLEXIBLE_PAYMENT_BTN",new By.ByXPath("//input[@data-testid='pick-when-to-pay-pay-now']"));
    private static final Locator PAY_ZERO_IDR_AMOUNT_BUTTON = new Locator("PAY_ZERO_IDR_AMOUNT_BUTTON",new By.ByXPath("//button[contains(@class,'PriceSummarySection_complete_booking_button')]"));

    private static final Locator CUSTOM_CHECKIN_BOX=new Locator("CUSTOM_CHECKIN_BOX", new By.ByXPath("//span[contains(@class, 'Check_custom_checkbox')]"));
    private static final Locator CONFIRM_RESCHEDULE_REQUEST=new Locator("CONFIRM_RESCHEDULE_REQUEST", new By.ByXPath("//span[contains(text(), 'Request reschedule')]"));

    // Dynamic locator methods for price summary (currency-dependent)
    private Locator getPriceSummarySubtotalLocator(String currency) {
        return new Locator("PRICE_SUMMARY_SUBTOTAL", new By.ByXPath("(//div[@data-testid='price-summary-container'])[1]//span[text()='Subtotal']/ancestor::div[contains(@class, 'accordion_header')]//span[contains(text(), '" + currency + "')]"));
    }

    private Locator getExtraFacilityPriceDetails(String facilityName) {
        return new Locator("EXTRA_FACILITY_PRICE_DETAILS", new By.ByXPath("//*[contains(.,'" + facilityName + "') and contains(@class,'PriceSummary_text_with_dot_container')]/parent::*/following-sibling::*"));
    }

    private Locator getPriceSummaryOtherFeesLocator(String currency) {
        return new Locator("PRICE_SUMMARY_OTHER_FEES", new By.ByXPath("(//div[@data-testid='price-summary-container'])[1]//span[text()='Other Fees' or text()='Biaya Lainnya']/ancestor::div[contains(@class, 'accordion_header')]//span[contains(text(), '" + currency + "')]"));
    }

    private Locator getPriceSummaryDiscountLocator(String currency) {
        return new Locator("PRICE_SUMMARY_DISCOUNT", new By.ByXPath("(//div[@data-testid='price-summary-container'])[1]//span[text()='Discount' or text()='Diskon']/ancestor::div[contains(@class, 'accordion_header')]//span[contains(@class, 'Text_variant_positive') and contains(text(), '" + currency + "')]"));
    }

    // Complementary Items Page Locators
    private static final Locator COMPLEMENTARY_STEPPER = new Locator("COMPLEMENTARY_STEPPER",
            By.xpath("//div[contains(@class, 'Stepper_stepper_item') and contains(@class, 'Stepper_active')]//div[contains(@class, 'Stepper_label') and (contains(text(), 'Complementary') or contains(text(), 'Pelengkap'))]"));
    private static final Locator COMPLEMENTARY_STEPPER_ITEM = new Locator("COMPLEMENTARY_STEPPER_ITEM",
            By.xpath("//div[@data-testid='stepper-item-1']//div[contains(@class, 'Stepper_active')]"));
    private static final Locator ADDONS_CONTAINER = new Locator("ADDONS_CONTAINER",
            By.xpath("//div[@data-testid='add-ons-container']"));
    private static final Locator ADDONS_HEADER = new Locator("ADDONS_HEADER",
            By.xpath("//div[@data-testid='add-ons-container']//h3[contains(text(), 'Add-ons') or contains(text(), 'Fasilitas')]"));

    private Locator getInputEmail(String username) {
        return new Locator("INPUT_EMAIL", new By.ByXPath("//*[@name='emailAddress' and @value='"+username+"']"));
    }
    public HotelBookingFormPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> HotelBookingFormPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new HotelBookingFormPageDWeb<>(session);
            }
            case AMWEB, IMWEB -> {
                return new HotelBookingFormPageMWeb<>(session);
            }
            default -> throw new UnsupportedOperationException("Platform not supported");
        }
    }

    public abstract void fillGuestDetailsCombinedRandom(int guests);
    public abstract void fillSalutations(String salutation);
    public abstract int getTotalPriceWithoutRounding();

    public void clickLoginBanner() {
        driverTools.waitForElementToAppear(LOGIN_BANNER).orElseThrow();
        driverTools.clickWithJavascriptExecutor(LOGIN_BANNER);
    }

    public void clickRadioButtonMr() {
        driverTools.waitForElementToAppear(RADIO_BUTTON_MR).orElseThrow();
        driverTools.click(RADIO_BUTTON_MR);
    }

    public abstract void continueToPaymentPage();

    public void continueToSecondPartThenPaymentPage() {
        driverTools.dummyWait(3);
        driverTools.waitForElementToAppear(CONTINUE);
        driverTools.clickWithJavascriptExecutor(CONTINUE);
        driverTools.waitForElementToDisappear(CONTINUE, 15);

        if (driverTools.waitForElementToPresence(PICK_PAY_METHOD_BUTTON).isPresent()) {
            driverTools.clickWithJavascriptExecutor(PICK_PAY_METHOD_BUTTON);
        }

        if(driverTools.waitForElementToPresence(SELECT_PAYMENT_BTN).isPresent()){
            driverTools.clickWithJavascriptExecutor(SELECT_PAYMENT_BTN);
            driverTools.waitForElementToDisappear(SELECT_PAYMENT_BTN);
        }
    }

    public void continueToPayment() {
        driverTools.waitForElementToPresence(CONTINUE).ifPresent(driverTools::clickWithJavascriptExecutor);
        if(driverTools.waitForElementToPresence(FLEXIBLE_PAYMENT_BTN,3).isPresent()){
            driverTools.clickWithJavascriptExecutor(FLEXIBLE_PAYMENT_BTN);
        }
        driverTools.waitForElementToAppear(GO_TO_PAYMENT_PAGE);
        driverTools.clickWithJavascriptExecutor(GO_TO_PAYMENT_PAGE);
        if (driverTools.isElementDisplayed(CONTINUE)) {
            driverTools.clickWithJavascriptExecutor(CONTINUE);
        }
    }

    /**
     * Proceeds from booking form step 1 through review to the payment page:
     * 1. Continue (booking details footer)
     * 2. Continue to payment (price summary on booking form step 2)
     * 3. Continue to payment (review / interim page), when shown
     */
    public void continueThroughBookingFormToPayment() {
        dismissRatePlanChangePopupsIfPresent();
        driverTools.scrollIntoWebView(PRICE_FOOTER_CONTINUE_BUTTON);
        clickContinueBookingFormButton(PRICE_FOOTER_CONTINUE_BUTTON);

        dismissRatePlanChangePopupsIfPresent();
        detectAndDismissRateChangePopup();
        clickContinueBookingFormButton(GO_TO_PAYMENT_PAGE);

        dismissRatePlanChangePopupsIfPresent();
        detectAndDismissRateChangePopup();
    }

    private void clickContinueBookingFormButton(Locator locator) {
        driverTools.waitForElementToAppear(locator, 15).orElseThrow();
        driverTools.scrollIntoWebView(locator);
        driverTools.clickWithJavascriptExecutor(locator);
        driverTools.waitForElementToDisappear(locator, 15);
    }


    public void continueTo2NdPartThenPaymentPage() {
        driverTools.dummyWait(3);
        driverTools.waitForElementToAppear(CONTINUE);
        driverTools.clickWithJavascriptExecutor(CONTINUE);
        driverTools.waitForElementToDisappear(CONTINUE, 15);
        dismissRatePlanChangePopupsIfPresent();

        if(driverTools.waitForElementToPresence(PICK_PAY_METHOD_BUTTON).isPresent()){
            driverTools.clickWithJavascriptExecutor(PICK_PAY_METHOD_BUTTON);
        }
        if(driverTools.waitForElementToPresence(SELECT_PAYMENT_BTN).isPresent()){
            driverTools.clickWithJavascriptExecutor(SELECT_PAYMENT_BTN);
            driverTools.waitForElementToDisappear(SELECT_PAYMENT_BTN);
        }
        dismissRatePlanChangePopupsIfPresent();
    }

    public void fillOtherDetails(){
        driverTools.waitForElementToAppear(DROPDOWN_AUTHON);
        driverTools.clickWithJavascriptExecutor(DROPDOWN_AUTHON);
        driverTools.click(DROPDOWN_OPTION_OPT1);
        driverTools.waitForElementToAppear(DROPDOWN_CHEVRON);
        driverTools.click(DROPDOWN_CHEVRON);
        driverTools.click(ZGLY_AUTO_OPTION);
        if(driverTools.waitForElementToAppear(CONTINUE_BUTTON,3).isPresent()){
            driverTools.scrollIntoWebView(CONTINUE_BUTTON);
            driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON);
        }
    }

    public boolean isAddonThereOnBookingForm() {
        driverTools.waitForElementToPresence(CONTINUE_BUTTON).orElseThrow();
        String buttonText = driverTools.getText(CONTINUE_BUTTON).orElseThrow();
        if(buttonText.contains("Select Payment") || buttonText.contains("Pilih Pembayaran")){
            return false;
        }
        return true;
    }

    public abstract void fillContactDetail(String fullName, String phoneNumber, String emailId);

    /**
     * Fills contact title, full name, and phone number only when those fields are empty.
     * Email is skipped because it is pre-filled and disabled for logged-in users.
     */
    public void fillContactDetailIfEmpty(String fullName, String phoneNumber) {
        driverTools.waitForElementToAppear(CONTACT_CARD, 5);
        selectFirstContactSalutation();

        WebElement fullNameInput = driverTools.waitForElementToAppear(FULL_NAME_INPUT).orElseThrow();
        String currentName = driverTools.getValue(fullNameInput);
        if (currentName == null || currentName.trim().isEmpty()) {
            driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(FULL_NAME_INPUT, fullName);
        }

        WebElement phoneNumberInput = driverTools.waitForElementToAppear(PHONE_NUMBER_INPUT).orElseThrow();
        String currentPhone = driverTools.getValue(phoneNumberInput);
        if (currentPhone == null || currentPhone.trim().isEmpty()) {
            driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(PHONE_NUMBER_INPUT, phoneNumber);
        }
    }

    public void selectFirstContactSalutation() {
        driverTools.waitForElementToAppear(CONTACT_FIRST_SALUTATION, 5);
        driverTools.scrollIntoWebView(CONTACT_FIRST_SALUTATION);
        driverTools.clickWithJavascriptExecutor(CONTACT_FIRST_SALUTATION);
    }



    public abstract boolean isContactForm();

    public boolean isGuestStayForm(){
        driverTools.waitForElementToAppear(GUEST_STAY_DETAILS_CARD, 5);
        return driverTools.isElementDisplayed(GUEST_STAY_DETAILS_CARD);
    }

    public boolean isSpecialRequest(){
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_BUTTON, 5);
        return driverTools.isElementDisplayed(SPECIAL_REQUEST_BUTTON);
    }

    public boolean isSpecialRequestPopup(){
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_CHIPS, 5);
        return driverTools.isElementDisplayed(SPECIAL_REQUEST_CHIPS);
    }

    protected Locator getGuestNameButtonLocator(int guestIndex) {
        return new Locator("ENTER_GUEST_NAME_BUTTON_" + guestIndex,
                new By.ByXPath("(((//div[contains(@class, 'FormItem_room_guest_row')])["+guestIndex+"])//span)[2]"));
    }

    protected Locator getGuestNameInputLocator(int guestIndex) {
        return new Locator("INPUT_GUEST_NAME_" + guestIndex,
                new By.ByXPath("(//input[contains(@data-testid, 'guest-form-full-name-text-input')])[" + guestIndex + "]"));
    }
    public Locator getSpecialRequestLocator(String requestText) {
        return new Locator("SPECIAL_REQUEST_CHIPS" + requestText,
                new By.ByXPath("//div[contains(@data-testid, 'special-request-chips-container')]//span[contains(text(), '" + requestText + "')]"));
    }

    public abstract void fillGuestDetails(int guestIndex, String name, boolean isGuest, boolean isSpecialRequest, String chips);

    public void fillGuestDetails(int guestIndex, String name, boolean isGuest) {
        if (isGuest) {
            Locator guestNameButton = getGuestNameButtonLocator(guestIndex);
            Locator guestNameInput = getGuestNameInputLocator(guestIndex);

            driverTools.waitForElementToAppear(guestNameButton, 5);
            driverTools.clickWithJavascriptExecutor(guestNameButton);

            WebElement fullNameInput = driverTools.waitForElementToAppear(guestNameInput).orElseThrow();
            if (!driverTools.getValue(fullNameInput).equals(name)) {
                driverTools.clearInput(fullNameInput);
                driverTools.sendKeys(fullNameInput, name);

                driverTools.waitForElementToAppear(SAVE_BUTTON, 5);
                driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
            }
        }
    }

    public abstract List<String> fillMultiGuestDetails(int totalGuest);

    public void addSpecialRequestOptions() {
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_BUTTON, 5);
        Assertion.assertThat("Special request button should be visible ", driverTools.isElementDisplayed(SPECIAL_REQUEST_BUTTON), is(true));
        driverTools.clickWithJavascriptExecutor(SPECIAL_REQUEST_BUTTON);

        driverTools.waitForElementToAppear(SPECIAL_REQUEST_CHIPS, 5);
        Assertion.assertThat("Special request Pop Container should be visible ", driverTools.isElementDisplayed(SPECIAL_REQUEST_CHIPS), is(true));
        driverTools.clickWithJavascriptExecutor(SPECIAL_REQUEST_CHIPS);
        driverTools.click(SPECIAL_REQUEST_SAVE);
    }

    public void continueToNextPage() {
        dismissRatePlanChangePopupsIfPresent();
        driverTools.waitForElementToAppear(CONTINUE_BUTTON);
        driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON);
    }

    public void detectAndDismissRateChangePopup() {
        PopupObserver.inject(driver, List.of(POP_UP_SEE_CHANGES_AND_CONTINUE, POP_UP_CONTINUE_BOOKING, RATE_PLAN_CHANGE_DIALOG_PRIMARY_BTN, COMBINATION_CHANGES_FOOTER_PRIMARY_BTN));
    }

    /**
     * Detects the price-change <b>snackbar</b> shown on booking form step 1
     * (e.g. "The total price has changed from IDR 1,323,108 to IDR 2,425,698."), captures its text and
     * dismisses it via the OK button. This is the snackbar variant to {@link #detectAndDismissRateChangePopup()}
     * (which handles the dialog).
     *
     * @return the snackbar text when present, otherwise an empty string.
     */
    public String detectAndDismissPriceChangeSnackbar() {
        driverTools.waitForElementToAppear(PRICE_CHANGE_SNACKBAR_TEXT);
        String snackbarText = driverTools.getText(PRICE_CHANGE_SNACKBAR_TEXT).orElseThrow();
        logger.log("Price-change snackbar detected on booking form: " + snackbarText);
        driverTools.waitForElementToPresence(PRICE_CHANGE_SNACKBAR_OK_BTN, 3).ifPresent(driverTools::clickWithJavascriptExecutor);
        return snackbarText;
    }

    /** Returns the price-change snackbar text on booking form step 1, or an empty string if not shown. */
public String getPriceChangeSnackbarText() {
    for (int i = 0; i < 5; i++) {
        String text = driverTools.waitForElementToPresence(PRICE_CHANGE_SNACKBAR_TEXT, 2)
                .map(WebElement::getText).orElse("");

        if (!text.trim().isEmpty()) {
            return text.trim();
        }
        logger.log("Price change snackbar is present but text is empty. Retrying...");
        // Allow time for snackbar text to fully render, as it may occasionally appear before the text is loaded.
        try {
            Thread.sleep(200);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return "";
        }
    }
    return "";
}

    /**
     * Returns the text of the rate / cancellation-policy change <b>dialog</b> (e.g. "The cancellation policy
     * has changed ...") shown on booking form step 1, or an empty string if not shown. Does not dismiss it;
     * call {@link #detectAndDismissRateChangePopup()} afterwards to continue.
     */
    public String getRateChangeDialogText() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            String text = driverTools.waitForElementToPresence(
                    RATE_CHANGE_DIALOG_BODY, 2
            ).map(WebElement::getText).orElse("");

            if (!text.trim().isEmpty()) {
                return text.trim();
            }
            logger.log("Rate change dialog is present but text is empty. Retrying...");
            Thread.sleep(1);
        }

        return "";
    }
    public void continueBookingAfterRateChange() {
        PopupObserver.inject(driver, List.of(POP_UP_CONTINUE_BOOKING_TEXT));
    }

    /**
     * On the rate / cancellation-policy change dialog, clicks the "See other rooms" option to return
     * to the room list instead of continuing the booking.
     */
    public void goBackToRoomList() {
        driverTools.waitForElementToAppear(POP_UP_SEE_OTHER_ROOMS, 10)
                .ifPresent(driverTools::clickWithJavascriptExecutor);
        logger.log("Clicked 'See other rooms' to go back to the room list");
    }

    /** If rate plan / combination change popups appear on booking form step 1, dismiss them before Continue. */
    public void dismissRatePlanChangePopupsIfPresent() {
        if (driverTools.waitForElementToAppear(RATE_PLAN_CHANGE_DIALOG_PRIMARY_BTN, 5).isPresent()) {
            driverTools.clickWithJavascriptExecutor(RATE_PLAN_CHANGE_DIALOG_PRIMARY_BTN);
            driverTools.waitForElementToDisappear(RATE_PLAN_CHANGE_DIALOG, 15);
        }
        driverTools.waitForElementToAppear(COMBINATION_CHANGES_FOOTER_PRIMARY_BTN, 5)
                .ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public void checkBookingPagePrice(String[] bookingPrices) {
        String finalPriceApi = bookingPrices[0];
        String basePriceApi = bookingPrices[1];
        Assertion.assertThat("Final price from API and Booking Form should be the same", getFinalTotalPrice(), is(finalPriceApi));
        if (!finalPriceApi.equals(basePriceApi)) {
            logger.log("Booking Page: There is crossed price for this hotel");
            Assertion.assertThat("Crossed price from API and Booking Form should be the same", getCrossTotalPrice(), is(basePriceApi));
        }
    }

    public void clickContinueButton() {
        driverTools.waitForElementToAppear(CONTINUE_BUTTON, 10);
        driverTools.scrollIntoWebView(CONTINUE_BUTTON);
        driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON);
    }


    public boolean isAddOnsSectionVisible() {
        return driverTools.waitForElementToAppear(ADDONS_CONTAINER, 10).isPresent()
                && driverTools.isElementDisplayed(ADDONS_HEADER);
    }

    public void clickSelectPaymentButton() {
        clickPickPayMethodButton();
        driverTools.waitForElementToAppear(SELECT_PAYMENT_BUTTON, 10);
        driverTools.scrollIntoWebView(SELECT_PAYMENT_BUTTON);
        driverTools.clickWithJavascriptExecutor(SELECT_PAYMENT_BUTTON);
    }


    public boolean isLabelTotalPrice() {
        driverTools.waitForElementToAppear(TOTAL_PRICE_LABEL).orElseThrow();
        return driverTools.isElementDisplayed(TOTAL_PRICE_LABEL);
    }

    public void clickPickPayMethodButton() {
        driverTools.waitForElementToAppear(PICK_PAY_METHOD_BUTTON, 5);
        try {
            driverTools.scrollIntoWebView(PICK_PAY_METHOD_BUTTON);
            driverTools.clickWithJavascriptExecutor(PICK_PAY_METHOD_BUTTON);
        } catch (Exception e) {
            logger.log("Pick pay method button not found, " + e.getMessage());
        }
    }

    public void selectPayment(){
        // continueTo2NdPartThenPaymentPage() may already land on payment; don't look for BF CTA there
        if (driverTools.waitForElementToPresence(PAYMENT_METHOD_SECTION, 3).isPresent()
                || driverTools.waitForElementToPresence(PAYMENT_PAGE_SUBMIT_BUTTON, 3).isPresent()) {
            logger.log("Already on payment page — skipping booking-form Select Payment click");
            return;
        }
        clickPickPayMethodButton();
        driverTools.waitForElementToPresence(SELECT_PAYMENT_BUTTON, 10).orElseThrow();
        driverTools.scrollIntoWebView(SELECT_PAYMENT_BUTTON);
        driverTools.clickWithJavascriptExecutor(SELECT_PAYMENT_BUTTON);
    }

    public abstract int getTotalPrice();

    private String getFinalTotalPrice() {
        WebElement webElement = driverTools.waitForElementToAppear(FINAL_TOTAL_PRICE, 40)
                .orElseThrow(() -> new NoSuchElementException(
                        "Timed out after 40 seconds waiting for FINAL_TOTAL_PRICE locator: " + FINAL_TOTAL_PRICE));
        return driverTools.getText(webElement);
    }

    private String getCrossTotalPrice() {
        WebElement webElement = driverTools.waitForElementToAppear(CROSS_TOTAL_PRICE).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getBookingHotelName(){
        WebElement webElement = driverTools.waitForElementToAppear(BOOKING_FORM_HOTEL_NAME).orElseThrow();
        return driverTools.getText(webElement);
    }

    public void chooseAddOnsAndInsurance(Boolean isProtection){
        driverTools.waitForElementToAppear(ADDONS_BUTTON,3);
        Assertion.assertThat("AddOns should be visible If available",driverTools.isElementDisplayed(ADDONS_BUTTON),is(true));
        driverTools.clickWithJavascriptExecutor(ADDONS_BUTTON);

        driverTools.waitForElementToAppear(ADDONS_RADIO_BUTTON,3);
        driverTools.clickWithJavascriptExecutor(ADDONS_RADIO_BUTTON);

        driverTools.waitForElementToAppear(ADDONS_INC_BUTTON,3);
        driverTools.clickWithJavascriptExecutor(ADDONS_INC_BUTTON);

        driverTools.waitForElementToAppear(ADDONS_SAVE_BUTTON,3);
        driverTools.clickWithJavascriptExecutor(ADDONS_SAVE_BUTTON);

        if(isProtection){
            driverTools.waitForElementToAppear(INSURANCE_BUTTON,6);
            Assertion.assertThat("Insurance like Stay Protection should be visible If available",driverTools.isElementDisplayed(INSURANCE_BUTTON),is(true));
            String ButtonText=driverTools.getText(INSURANCE_BUTTON).orElseThrow();
            logger.log("Button Text->"+ ButtonText);
            if(ButtonText.contains("Add")){
                driverTools.clickWithJavascriptExecutor(INSURANCE_BUTTON);
            }
        }

    }

    public List<String> chooseAddOns() {
        driverTools.waitForElementToAppear(ADDONS_BUTTON,3);
        Assertion.assertThat("AddOns should be visible If available",driverTools.isElementDisplayed(ADDONS_BUTTON),is(true));
        driverTools.clickWithJavascriptExecutor(ADDONS_BUTTON);

        driverTools.waitForElementToAppear(ADDONS_RADIO_BUTTON,3);
        driverTools.clickWithJavascriptExecutor(ADDONS_RADIO_BUTTON);

        driverTools.waitForElementToAppear(ADDONS_INC_BUTTON,3);
        driverTools.clickWithJavascriptExecutor(ADDONS_INC_BUTTON);

        String addonName = driverTools.getText(ADDONS_NAME).orElseThrow();
        String addonPrice = driverTools.getText(TOTAL_ADDON_PRICE).orElseThrow();

        driverTools.waitForElementToAppear(ADDONS_SAVE_BUTTON, 3);
        driverTools.clickWithJavascriptExecutor(ADDONS_SAVE_BUTTON);

        List<String> addonDetails = new ArrayList<>();
        addonDetails.add(addonName);
        addonDetails.add(addonPrice);
        logger.log("addonDetails-> "+ addonDetails);
        return addonDetails;
    }

    public List<String> chooseInsurance() {
        driverTools.waitForElementToAppear(INSURANCE_BUTTON, 6);
        Assertion.assertThat("Insurance should be visible If available", driverTools.isElementDisplayed(INSURANCE_BUTTON), is(true));
        String ButtonText=driverTools.getText(INSURANCE_BUTTON).orElseThrow();
        logger.log("Button Text->"+ ButtonText);
        if(ButtonText.toLowerCase().contains("add") || ButtonText.toLowerCase().contains("tambahkan")){
            driverTools.clickWithJavascriptExecutor(INSURANCE_BUTTON);
        }
        driverTools.isElementDisplayed(INSURANCE_PRICE);
        String insurancePrice = driverTools.getText(INSURANCE_PRICE).orElseThrow();
        String insuranceName = driverTools.getText(INSURANCE_NAME).orElseThrow();

        List<String> insuranceDetails = new ArrayList<>();
        insuranceDetails.add(insuranceName);
        insuranceDetails.add(insurancePrice);
        logger.log("insuranceDetails-> "+ insuranceDetails);
        return insuranceDetails;
    }

    public void clickSeeOtherRooms(){
        driverTools.clickWithJavascriptExecutor(SEE_OTHER_ROOMS);
    }

    public void enterSpecialRequest(String customReq){
        driverTools.clickWithJavascriptExecutor(SPECIAL_REQUEST_BUTTON);
        driverTools.waitForElementsToAppear(SPECIAL_REQUEST_TEXTAREA,5);
        driverTools.waitForElementToBeClickable(SPECIAL_REQUEST_TEXTAREA,5);

        driverTools.sendKeys(SPECIAL_REQUEST_TEXTAREA,customReq);
        driverTools.clickWithJavascriptExecutor(SPECIAL_REQUEST_SAVE);
    }

    public void enterSpecialRequestMO(String customReq , int count){
        for (int i = 0; i < count; i++) {
            WebElement specialRequestBtn = driverTools.waitForElementsToAppear(SPECIAL_REQUEST_BUTTON).get().get(i);
            driverTools.clickWithJavascriptExecutor(specialRequestBtn);
            driverTools.waitForElementsToAppear(SPECIAL_REQUEST_TEXTAREA,5);
            driverTools.waitForElementToBeClickable(SPECIAL_REQUEST_TEXTAREA,5);

            driverTools.sendKeys(SPECIAL_REQUEST_TEXTAREA,customReq);
            driverTools.clickWithJavascriptExecutor(SPECIAL_REQUEST_SAVE);
        }
    }

    public String getSpecialReqText(){
        WebElement webElement = driverTools.waitForElementToAppear(SPECIAL_REQUEST_TEXT).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getGuestName(){
        WebElement webElement = driverTools.waitForElementToAppear(GUEST_NAME).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getBookingPrice(){
        if (driverTools.waitForElementToPresence(POP_UP_SEE_CHANGES_AND_CONTINUE, 5).isPresent()) {
            driverTools.clickWithJavascriptExecutor(POP_UP_SEE_CHANGES_AND_CONTINUE);
            driverTools.waitForElementToPresence(POP_UP_CONTINUE_BOOKING_TEXT, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
        } else {
            driverTools.waitForElementToPresence(POP_UP_CONTINUE_BOOKING, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
        }
        WebElement webElement = driverTools.waitForElementToAppear(BOOKING_PRICE).orElseThrow();
        return driverTools.getText(webElement).replaceAll("[^0-9]", "");
    }

    public String getBookingDate(){
        WebElement webElement = driverTools.waitForElementToAppear(BOOKING_DATES).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getBookingRoomNight(){
        WebElement webElement = driverTools.waitForElementToAppear(BOOKING_ROOM_NIGHT).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getBookingCheckInDate() {
        return getBookingDate().split(" - ")[0].trim();
    }

    public String getBookingNightLabel() {
        return getBookingRoomNight().split("•")[0].trim();
    }

    private Locator getRoomPrice(int index) {
        return new Locator("ROOM_PRICE_BOOKING_" + index,
                new By.ByXPath("(//div[contains(@data-testid, 'price-per-night-item-container')]//span[contains(@class,'price_text')])[" + index + "]"));
    }

    public int getIndividualPrice(int index){
        Locator roomPrice = getRoomPrice(index);
        WebElement element = driverTools.waitForElementToAppear(roomPrice).orElseThrow();
        String price= driverTools.getText(element);
        return Integer.parseInt(price.split(" ")[1].replace(",", ""));
    }


    public abstract void openPriceModal();

    public void closePriceModal(){
        driverTools.clickWithJavascriptExecutor(CLOSE_MODAL);
    }

    public void fillGuestDifferentRatePlan(String name){
        driverTools.clickWithJavascriptExecutor(BOOK_NAME_BUTTON);
        driverTools.waitForElementToAppear(INPUT_BOOK_NAME);
        driverTools.sendKeys(INPUT_BOOK_NAME,name);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public boolean verifyValidationErrorForMultiGuestB2B() {
        return driverTools.waitForElementToPresence(B2B_MULTIGUEST_ERROR, 15).isPresent();
    }

    public boolean verifyEmailErrorWhenPutB2BEmailMultiGuest(){
        return driverTools.waitForElementToPresence(B2B_EMAIL_MULTIGUEST_ERROR, 15).isPresent();
    }

    public boolean isFullyBookedNotifPresent() {
        driverTools.waitForElementToAppear(FULLY_BOOKED_NOTIF);
        return driverTools.isElementDisplayed(FULLY_BOOKED_NOTIF);
    }

    public boolean isInsuranceOrAddonPageVisible() {
        driverTools.waitForElementToAppear(HOTEL_INSURANCE_PAGE);
        return driverTools.isElementDisplayed(HOTEL_INSURANCE_PAGE) || driverTools.isElementDisplayed(HOTEL_ADD_ONS_PAGE);
    }
    public String getHotelPriceDetail() {
        WebElement headingText = driverTools.waitForElementToAppear(PRICE_PDP).orElseThrow();
        return driverTools.getText(headingText);
    }

    public void openPriceSummary() {
        driverTools.waitForElementToAppear(PRICE_SUMMARY_TITLE);
        driverTools.scrollIntoWebView(PRICE_SUMMARY_TITLE);
        driverTools.clickWithJavascriptExecutor(PRICE_SUMMARY_TITLE);
    }

    public boolean isChevronButtonLoaded() {
        WebElement headingText = driverTools.waitForElementToAppear(CHEVRON_PRICE).orElseThrow();
        return driverTools.isElementDisplayed(CHEVRON_PRICE);
    }

    public String getBookingRoomName(){
        WebElement webElement=driverTools.waitForElementToAppear(ROOM_NAME).orElseThrow();
        return driverTools.getText(webElement);
    }

    public List<String> getRoomRatesIfCorrectCount(int noOfRoomExpected) {
        List<String> roomRates = new ArrayList<>();

        for (int i = 1; i <= noOfRoomExpected; i++) {
            Locator ROOM_RATES = new Locator("CONTACT_CARD", new By.ByXPath("(//h4[contains(@class,'FormSubheading_title_text')])[" + i + "]"));
            if (driverTools.isElementPresent(ROOM_RATES)) {
                String rateText = driverTools.getText(ROOM_RATES).orElseThrow();
                roomRates.add(rateText);
            } else {
                return Collections.emptyList();
            }
        }

        int exceedingRoom = noOfRoomExpected + 1;
        Locator ROOM_RATES_EXCEED = new Locator("CONTACT_CARD", new By.ByXPath("(//h4[contains(@class,'FormSubheading_title_text')])[" + exceedingRoom + "]"));
        if (driverTools.isElementPresent(ROOM_RATES_EXCEED)) {
            return Collections.emptyList();
        }

        return roomRates;
    }


    public abstract void openPriceBreakdownPopUp();
    public void openSubTotalPriceBreakdown() {
        driverTools.waitForElementToAppear(PRICE_SUMMARY_SUBTOTAL);
        driverTools.clickWithJavascriptExecutor(PRICE_SUMMARY_SUBTOTAL);
    }

    public String getRoomPriceSubtotal() {
        WebElement webElement = driverTools.waitForElementToAppear(ROOM_PRICE_DETAILS).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getFacilityPriceSubtotal(String facilityName) {
        Locator addOnsPriceDetails = getExtraFacilityPriceDetails(facilityName);
        WebElement webElement = driverTools.waitForElementToAppear(addOnsPriceDetails).orElseThrow();
        return driverTools.getText(webElement);
    }

    public List<String> getRoomRatesIfCorrectCountInPriceDetail(int noOfRoomExpected) {
        List<String> roomRates = new ArrayList<>();

        for (int i = 1; i <= noOfRoomExpected; i++) {
            Locator ROOM_NAME = new Locator("ROOM_NAME", new By.ByXPath("(//span[contains(@class, 'PriceDetail_room_name')])[" + i + "]"));
            if (driverTools.isElementPresent(ROOM_NAME)) {
                String rateText = driverTools.getText(ROOM_NAME).orElseThrow();
                roomRates.add(rateText);
            } else {
                return Collections.emptyList();
            }
        }

        int exceedingRoom = noOfRoomExpected + 1;
        Locator ROOM_RATES_EXCEED = new Locator("CONTACT_CARD", new By.ByXPath("(//h4[contains(@class,'FormSubheading_title_text')])[" + exceedingRoom + "]"));
        if (driverTools.isElementPresent(ROOM_RATES_EXCEED)) {
            return Collections.emptyList();
        }

        return roomRates;
    }

    public static boolean checkPriceSameFromRoomDetail(String roomPrice, String bookingPrice){
        return roomPrice.equalsIgnoreCase(bookingPrice);
    }

    public abstract int getTotalPriceInPriceDetail();

    public boolean isRoomRatesAreTheSame(List<String> roomRateBF, List<String> roomRatePriceDetail) {
        return  driverTools.areTwoStringArraysEqual(roomRateBF,roomRatePriceDetail);
    }

    public boolean isLoginSuccessful(String username) {
        boolean getInputEmailDisplayed = driverTools.isElementEnabled(getInputEmail(username));
        logger.log("Input email filed is disable: " + getInputEmailDisplayed);
        return  getInputEmailDisplayed;
    }

    public void clickOnPopUp() {
        driverTools.waitForElementToPresence(POP_UP_CONTINUE_BOOKING,5).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToPresence(POP_UP_PAYMENT).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public boolean isRoomAndAddOnPresent(HashMap<String,String> addOnDetails){
        driverTools.clickWithJavascriptExecutor(SUBTOTAL_OPEN_ICON);
        driverTools.clickWithJavascriptExecutor(ROOM_OPEN_ICON);
        WebElement roomNameElement = driverTools.waitForElementToAppear(ROOM_NAME_IN_PRICE_DETAIL).orElseThrow();
        List<String> roomName = driverTools.getTexts(roomNameElement);
        logger.log("Room Name in Price Detail: " + roomName.toString());
        Assertion.assertThat("Room name should be present in price detail",roomName.toString(),is(not("")));
        WebElement addOnPriceElement = driverTools.waitForElementToAppear(ADDON_PRICE).orElseThrow();
        List<String> addOnDetail = driverTools.getTexts(addOnPriceElement);
        logger.log("Addon  " + addOnDetail.toString());
        Assertion.assertThat("AddOn should be present in price detail",addOnDetail.toString(),containsString(addOnDetails.get("addonName")));
        return !roomName.isEmpty() && !addOnDetail.isEmpty();
    }

    public HashMap<String,String> chooseAddOnsMultiRoom(int count){
        driverTools.waitForElementToAppear(ADDONS_BUTTON,3);
        Assertion.assertThat("AddOns should be visible If available",driverTools.isElementDisplayed(ADDONS_BUTTON),is(true));
        driverTools.clickWithJavascriptExecutor(ADDONS_BUTTON);


        for(int i=0;i<count;i++){
            driverTools.waitForElementToAppear(ADDONS_RADIO_BUTTON,3);
            WebElement radioButton = driverTools.waitForElementsToAppear(ADDONS_RADIO_BUTTON).get().get(i);
            driverTools.clickWithJavascriptExecutor(radioButton);

            driverTools.waitForElementToAppear(ADDONS_INC_BUTTON,3);
            WebElement incButton = driverTools.waitForElementsToAppear(ADDONS_INC_BUTTON).get().get(i);
            driverTools.clickWithJavascriptExecutor(incButton);
        }
        String addonPrice=driverTools.getText(TOTAL_ADDON_PRICE).orElseThrow();
        addonPrice = addonPrice.replaceAll("[^0-9]", "");
        String addonName=driverTools.getText(ADDONS_NAME).orElseThrow();
        logger.log("Addon Name: "+ addonName);

        String addOnPRicePerUnit=driverTools.getText(ADDONS_PRICE_PER_UNIT).orElseThrow();
        addOnPRicePerUnit = addOnPRicePerUnit.replaceAll("[^0-9]", "");
        logger.log("AddOn Price Per Unit: "+ addOnPRicePerUnit);
        int totalAddOnPrice = Integer.parseInt(addOnPRicePerUnit) * count;
        logger.log("Total addon Price for "+ count + " room(s): " + addOnPRicePerUnit + " x " + count + " = " + totalAddOnPrice);

        Assertion.assertThat("Total Addon Price: "+ addonPrice,addonPrice,is(totalAddOnPrice+""));
        String numericPrice = addonPrice.replaceAll("[^0-9]", "");
        driverTools.waitForElementToAppear(ADDONS_SAVE_BUTTON,3);
        driverTools.clickWithJavascriptExecutor(ADDONS_SAVE_BUTTON);

        HashMap<String,String> addonDetails = new HashMap<>();
        addonDetails.put("addonName",addonName);
        addonDetails.put("addonPrice",numericPrice);
        addonDetails.put("addonPricePerUnit",addOnPRicePerUnit);
        return addonDetails;
    }

    public void clickHotelChevron() {
        driverTools.waitForElementsToAppear(SELECT_CHEVRON_BUTTON);
        driverTools.clickWithJavascriptExecutor(SELECT_CHEVRON_BUTTON);
    }

    public boolean isChevronButtonLoadedInfo() {
        driverTools.waitForElementToAppear(CHEVRON_INFO).orElseThrow();
        return driverTools.isElementDisplayed(CHEVRON_INFO);
    }

    public String getTotalRoomCount(){
        WebElement webElement=driverTools.waitForElementToAppear(ROOM_COUNT_TEXT).orElseThrow();
        return driverTools.getText(webElement);
    }


    public void clickStayDetailsChevron() {
        driverTools.waitForElementToAppear(STAY_DETAILS_CHEVRON, 10);
        driverTools.clickWithJavascriptExecutor(STAY_DETAILS_CHEVRON);
    }


    public boolean isStayDetailsBottomSheetOpened() {
        // First try to find the title element
        try {
            driverTools.waitForElementToAppear(STAY_DETAILS_BOTTOM_SHEET_TITLE, 5).orElseThrow();
            return driverTools.isElementDisplayed(STAY_DETAILS_BOTTOM_SHEET_TITLE);
        } catch (Exception e) {
            // If title not found, check for other elements that are definitely in the bottom sheet
            // This makes the method more resilient to locator changes
            logger.log("Stay Details bottom sheet title not found, checking alternative elements. Error: " + e.getMessage());
        }

        try {
            driverTools.waitForElementToAppear(STAY_DETAILS_HOTEL_NAME, 5).orElseThrow();
            return driverTools.isElementDisplayed(STAY_DETAILS_HOTEL_NAME);
        } catch (Exception e) {
            // Continue to next check
            logger.log("Stay Details hotel name element not found, trying next check. Error: " + e.getMessage());
        }

        try {
            driverTools.waitForElementToAppear(STAY_DETAILS_ROOM_COUNT, 3).orElseThrow();
            return driverTools.isElementDisplayed(STAY_DETAILS_ROOM_COUNT);
        } catch (Exception e) {
            // Continue to next check
            logger.log("Stay Details room count element not found, trying next check. Error: " + e.getMessage());
        }

        try {
            driverTools.waitForElementToAppear(BOTTOM_SHEET_CLOSE_BUTTON, 3).orElseThrow();
            return driverTools.isElementDisplayed(BOTTOM_SHEET_CLOSE_BUTTON);
        } catch (Exception e) {
            logger.log("Bottom sheet close button not found. Stay Details bottom sheet may not be opened. Error: " + e.getMessage());
            return false;
        }
    }


    public String getStayDetailsHotelName() {
        WebElement element = driverTools.waitForElementToAppear(STAY_DETAILS_HOTEL_NAME).orElseThrow();
        return driverTools.getText(element);
    }


    public boolean isStayDetailsHotelNameVisible() {
        driverTools.waitForElementToAppear(STAY_DETAILS_HOTEL_NAME, 10);
        return driverTools.isElementDisplayed(STAY_DETAILS_HOTEL_NAME);
    }


    public String getStayDetailsRoomName() {
        WebElement element = driverTools.waitForElementToAppear(STAY_DETAILS_ROOM_NAME).orElseThrow();
        return driverTools.getText(element);
    }


    public boolean isStayDetailsRoomNameVisible() {
        driverTools.waitForElementToAppear(STAY_DETAILS_ROOM_NAME, 10);
        return driverTools.isElementDisplayed(STAY_DETAILS_ROOM_NAME);
    }


    public String getStayDetailsRoomCount() {
        WebElement element = driverTools.waitForElementToAppear(STAY_DETAILS_ROOM_COUNT).orElseThrow();
        return driverTools.getText(element);
    }


    public boolean isStayDetailsRoomCountVisible() {
        driverTools.waitForElementToAppear(STAY_DETAILS_ROOM_COUNT, 10);
        return driverTools.isElementDisplayed(STAY_DETAILS_ROOM_COUNT);
    }


    public String getStayDetailsNightCount() {
        WebElement element = driverTools.waitForElementToAppear(STAY_DETAILS_NIGHT_COUNT).orElseThrow();
        return driverTools.getText(element);
    }


    public boolean isStayDetailsNightCountVisible() {
        driverTools.waitForElementToAppear(STAY_DETAILS_NIGHT_COUNT, 10);
        return driverTools.isElementDisplayed(STAY_DETAILS_NIGHT_COUNT);
    }


    public String getStayDetailsGuestCount() {
        WebElement element = driverTools.waitForElementToAppear(STAY_DETAILS_GUEST_COUNT).orElseThrow();
        return driverTools.getText(element);
    }


    public boolean isStayDetailsGuestCountVisible() {
        driverTools.waitForElementToAppear(STAY_DETAILS_GUEST_COUNT, 10);
        return driverTools.isElementDisplayed(STAY_DETAILS_GUEST_COUNT);
    }


    public boolean isStayDetailsCancellationPolicyVisible() {
        driverTools.waitForElementToAppear(STAY_DETAILS_CANCELLATION_POLICY_CONTAINER, 10);
        return driverTools.isElementDisplayed(STAY_DETAILS_CANCELLATION_POLICY_CONTAINER);
    }


    public void closeBottomSheet() {
        driverTools.waitForElementToAppear(BOTTOM_SHEET_CLOSE_BUTTON, 3);
        driverTools.clickWithJavascriptExecutor(BOTTOM_SHEET_CLOSE_BUTTON);
    }


    public boolean isBottomSheetClosed() {
        driverTools.waitForElementToDisappear(STAY_DETAILS_BOTTOM_SHEET_TITLE, 5);
        return !driverTools.isElementDisplayed(STAY_DETAILS_BOTTOM_SHEET_TITLE);
    }


    public void clickSpecialRequestFormButton() {
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(SPECIAL_REQUEST_BUTTON);
    }


    public boolean isSpecialRequestFormOpened() {
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_FORM_CONTAINER, 10);
        return driverTools.isElementDisplayed(SPECIAL_REQUEST_FORM_CONTAINER);
    }


    public void clickFirstSpecialRequestChip() {
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_FIRST_CHIP, 10);
        driverTools.clickWithJavascriptExecutor(SPECIAL_REQUEST_FIRST_CHIP);
    }


    public void clickSecondSpecialRequestChip() {
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_SECOND_CHIP, 10);
        driverTools.clickWithJavascriptExecutor(SPECIAL_REQUEST_SECOND_CHIP);
    }


    public void fillOtherRequestsTextarea(String text) {
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_OTHER_TEXTAREA, 10);
        driverTools.sendKeys(SPECIAL_REQUEST_OTHER_TEXTAREA, text);
    }


    public void clickSpecialRequestSaveFormButton() {
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_SAVE, 10);
        driverTools.clickWithJavascriptExecutor(SPECIAL_REQUEST_SAVE);
    }


    public void fillSpecialRequestForm(String otherRequestsText) {
        clickSpecialRequestFormButton();
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_FORM_CONTAINER, 10);
        clickFirstSpecialRequestChip();
        clickSecondSpecialRequestChip();
        fillOtherRequestsTextarea(otherRequestsText);
        clickSpecialRequestSaveFormButton();
    }


    public boolean isPriceSummaryVisible() {
        driverTools.swipeToElement(PRICE_SUMMARY_CONTAINER, Swipe.UP, 5);
        driverTools.waitForElementToAppear(PRICE_SUMMARY_CONTAINER, 10);
        return driverTools.isElementDisplayed(PRICE_SUMMARY_CONTAINER);
    }

    public String getPriceSummarySubtotal(String currency) {
        Locator subtotalLocator = getPriceSummarySubtotalLocator(currency);
        WebElement element = driverTools.waitForElementToAppear(subtotalLocator).orElseThrow();
        return driverTools.getText(element);
    }

    public String getPriceSummaryOtherFees(String currency) {
        Locator otherFeesLocator = getPriceSummaryOtherFeesLocator(currency);
        WebElement element = driverTools.waitForElementToAppear(otherFeesLocator).orElseThrow();
        return driverTools.getText(element);
    }

    public String getPriceSummaryDiscount(String currency) {
        try {
            Locator discountLocator = getPriceSummaryDiscountLocator(currency);
            WebElement element = driverTools.waitForElementToAppear(discountLocator, 3).orElse(null);
            if (element != null) {
                return driverTools.getText(element);
            }
        } catch (Exception e) {
            logger.log("Discount not present: " + e.getMessage());
        }
        return "";
    }

    public boolean isPriceSummaryDiscountPresent() {
        return driverTools.isElementPresent(PRICE_SUMMARY_DISCOUNT_LABEL);
    }


    public String getPriceSummaryTotal() {
        WebElement element = driverTools.waitForElementToAppear(PRICE_SUMMARY_TOTAL).orElseThrow();
        return driverTools.getText(element);
    }


    public void validatePriceSummaryCalculation(String currency) {
        String subtotalStr = getPriceSummarySubtotal(currency).replaceAll("[^0-9]", "");
        String otherFeesStr = getPriceSummaryOtherFees(currency).replaceAll("[^0-9]", "");
        String discountStr = getPriceSummaryDiscount(currency).replaceAll("[^0-9]", "");
        String totalStr = getPriceSummaryTotal().replaceAll("[^0-9]", "");

        long subtotal = Long.parseLong(subtotalStr);
        long otherFees = Long.parseLong(otherFeesStr);
        long discount = discountStr.isEmpty() ? 0 : Long.parseLong(discountStr);
        long total = Long.parseLong(totalStr);

        long calculatedTotal = subtotal + otherFees - discount;

        logger.log("Booking Form Price Summary: Subtotal=" + subtotal + ", Other Fees=" + otherFees + ", Discount=" + discount + ", Expected Total=" + calculatedTotal + ", Actual Total=" + total);

        Assertion.assertThat("Booking form price breakdown calculation is correct (Total = Subtotal + Other Fees - Discount)", calculatedTotal, is(total));
    }

    public void applyPromo(String promoCode) {
        driverTools.scrollIntoWebView(PROMO_MODAL_OPEN);
        driverTools.click(PROMO_MODAL_OPEN);

        driverTools.sendKeys(PROMO_CODE_INPUT, promoCode);
        driverTools.hideKeyboard();

        Locator promoCodeApplyButton = getPromoCodeApply(promoCode);
        driverTools.swipeToElement(promoCodeApplyButton, Swipe.UP, 10);
        driverTools.click(promoCodeApplyButton);
    }

    private Locator getPromoCodeApply(String promoCode) {
        return new Locator("PROMOCODE_"+promoCode, AppiumBy.xpath("//*[contains(@class, 'PromoDetails_promo_code') and text()='"+promoCode+"']//ancestor::*[contains(@class, 'PromoDetails_footer_row')]//following-sibling::*[contains(@class, 'PromoDetails_footer_button')]//button"));
    }

    public String getContactFullNameValue() {
        driverTools.scrollUpUntilElementFound(FULL_NAME_INPUT, 300, 5);
        WebElement fullNameInput = driverTools.waitForElementToAppear(FULL_NAME_INPUT).orElseThrow();
        return driverTools.getValue(fullNameInput);
    }

    public String getContactPhoneNumberValue() {
        WebElement phoneNumberInput = driverTools.waitForElementToAppear(PHONE_NUMBER_INPUT).orElseThrow();
        return driverTools.getValue(phoneNumberInput);
    }

    public String getContactEmailValue() {
        WebElement emailInput = driverTools.waitForElementToAppear(EMAIL_INPUT).orElseThrow();
        return driverTools.getValue(emailInput);
    }

    public String getGuestNameValue(int guestIndex) {
        Locator guestNameButton = getGuestNameButtonLocator(guestIndex);
        driverTools.waitForElementToAppear(guestNameButton, 5);
        driverTools.clickWithJavascriptExecutor(guestNameButton);

        Locator guestNameInput = getGuestNameInputLocator(guestIndex);
        WebElement guestNameElement = driverTools.waitForElementToAppear(guestNameInput, 10).orElseThrow();
        String value = driverTools.getValue(guestNameElement);

        driverTools.clickWithJavascriptExecutor(BOTTOM_SHEET_CLOSE_BUTTON);
        driverTools.waitForElementToDisappear(guestNameInput, 3);

        return value;
    }

    /**
     * Safely gets guest name value if guest stay form is visible.
     * Returns null if form is not visible or if guest name is not pre-filled.
     * This method handles exceptions internally.
     */
    public String getGuestNameValueSafely(int guestIndex) {
        if (!isGuestStayForm()) {
            return null;
        }
        try {
            return getGuestNameValue(guestIndex);
        } catch (Exception e) {
            // Guest name not pre-filled in profile
            logger.log("Guest " + guestIndex + " name not pre-filled in profile: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets contact details values after navigating back.
     * Returns a map with keys: "fullName", "phoneNumber", "email"
     */
    public Map<String, String> getContactDetailsAfterBack() {
        Map<String, String> contactDetails = new HashMap<>();
        contactDetails.put("fullName", getContactFullNameValue());
        contactDetails.put("phoneNumber", getContactPhoneNumberValue());
        contactDetails.put("email", getContactEmailValue());
        return contactDetails;
    }

    /**
     * Gets guest name value if number of adults is greater than 1.
     * Returns null if noOfAdults is 1 or less.
     */
    public String getGuestNameValueIfMultipleAdults(int guestIndex, int noOfAdults) {
        if (noOfAdults > 1) {
            return getGuestNameValue(guestIndex);
        }
        return null;
    }

    public void enterGuestDetails(String guestName,String num,String mail){
        WebElement name=driverTools.waitForElementToAppear(INPUT_GUEST_NAME,10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(name);
        driverTools.sendKeys(name,guestName);
        WebElement number=driverTools.waitForElementToAppear(PHONE_NUMBER_INPUT,10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(number);
        driverTools.sendKeys(number,num);
        WebElement email=driverTools.waitForElementToAppear(EMAIL_INPUT,10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(email);
        driverTools.sendKeys(email,mail);
    }

    public void setEnterGuestName(String guestName){
        driverTools.clickWithJavascriptExecutor(ENTER_GUEST_NAME);
        WebElement name=driverTools.waitForElementToAppear(GUEST_NAME_POPUP,10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(name);
        driverTools.sendKeys(name,guestName);
        driverTools.clickWithJavascriptExecutor(SAVE_GUEST_BUTTON);
    }

    public void addInsurance(){
        driverTools.waitForElementToAppear(INSURANCE_BUTTON,6).orElseThrow();
        Assertion.assertThat("Insurance like Stay Protection should be visible If available",driverTools.isElementDisplayed(INSURANCE_BUTTON),is(true));
        String buttonText=driverTools.getText(INSURANCE_BUTTON).orElseThrow();
        logger.log("Button Text->"+ buttonText);
        if(buttonText.contains("Add")){
            driverTools.clickWithJavascriptExecutor(INSURANCE_BUTTON);
        }
    }

    public String getInsurancePrice(){
        driverTools.waitForElementToAppear(INSURANCE_PRICE).orElseThrow();
        String price= String.valueOf(driverTools.getText(INSURANCE_PRICE));
        return price.replaceAll("[^0-9]", "");
    }


    public boolean checkInsurancePrice(String price){
        driverTools.waitForElementToAppear(INSURANCE_PRICE_DETAIL).orElseThrow();
        String insurancePrice=String.valueOf(driverTools.getText(INSURANCE_PRICE_DETAIL));
        String numeric = insurancePrice.replaceAll("[^0-9]", "");
        return price.equals(numeric);
    }

    public boolean checkPriceAfterInsurance(String beforePrice,String insurancePrice){
        String totalPrice=String.valueOf(driverTools.getText(PRICE_ON_FOOTER)).replaceAll("[^0-9]", "");
        int result = Integer.parseInt(beforePrice) + Integer.parseInt(insurancePrice);
        String resultPrice = String.valueOf(result);
        return totalPrice.contains(resultPrice);
    }

    public boolean isTotalPriceCorrect(String bookingPrice,String pdpPrice){
        String price=pdpPrice.replaceAll("[^0-9]", "");
        return bookingPrice.equals(price);
    }

    public abstract int getTotalPriceOnBookingPage1();

    public boolean checkBookingPrice(String roomPrice) {
        driverTools.scrollDown(20);
        String bookingPriceText = getBookingPrice();
        String normalizedBookingPrice = bookingPriceText.replaceAll("[^0-9]", "");
        String normalizedRoomPrice = roomPrice.replaceAll("[^0-9]", "");
        logger.log(bookingPriceText);
        return normalizedBookingPrice.equals(normalizedRoomPrice);
    }

    public Boolean isPromoSectionAvailable() {
        var promoSection = driverTools.waitForElementToAppear(PROMO_SECTION);
        return promoSection.isPresent();
    }

    public boolean checkMostSuitablePromo() {
        WebElement suitablePromo = driverTools.waitForElementToAppear(SUITABLE_PROMO).orElseThrow();
        String val = driverTools.getText(suitablePromo);
        return val.equals("Most suitable promo") || val.equals("Promo pilihan buat kamu");
    }

    public boolean checkMostSuitablePromoNotApplied(){
        return driverTools.isElementDisplayed(SUITABLE_PROMO);
    }

    public void addPromo() {
        driverTools.waitForElementToAppear(PROMO_BUTTON);
        driverTools.click(PROMO_BUTTON);
    }

    public Boolean checkPromoApplied() {
        boolean flag1=driverTools.waitForElementToAppear(SUITABLE_PROMO).isPresent();
        driverTools.waitForElementsToAppear(PROMO_PRICE).orElseThrow();
        String promoPrice=String.valueOf(driverTools.getText(PROMO_PRICE));
        int promo_Price = Integer.parseInt(
                promoPrice.replaceAll("[^0-9]", "")
        );
        logger.log("Price of applied promo"+promo_Price);
        openDiscountModal();
        driverTools.waitForElementToAppear(PROMO_VALUE_DISCOUNT);
        String promoValueFromDiscount=String.valueOf(driverTools.getText(PROMO_VALUE_DISCOUNT));
        int promoValue_Discount=Integer.parseInt(
                promoValueFromDiscount.replaceAll("[^0-9]", "")
        );
        logger.log("Price from discount modal"+promoValue_Discount);
       return promo_Price==promoValue_Discount && flag1;
    }

    public void openDiscountModal() {
        driverTools.waitForElementToAppear(DISCOUNT_MODAL);
        driverTools.clickWithJavascriptExecutor(DISCOUNT_MODAL);
    }

    public Boolean checkPriceAfterPromo(String roomPrice) {
        driverTools.waitForElementToAppear(PROMO_VALUE_DISCOUNT);
        String promoPrice=String.valueOf(driverTools.getText(PROMO_VALUE_DISCOUNT));
        int promo_Price = Integer.parseInt(
                promoPrice.replaceAll("[^0-9]", "")
        );
        logger.log(promoPrice);
        driverTools.waitForElementToAppear(TOTAL_PRICE_FORM2);
        String totalPrice=String.valueOf(driverTools.getText(TOTAL_PRICE_FORM2));
        int total_Price = Integer.parseInt(
                totalPrice.replaceAll("[^0-9]", "")
        );
        logger.log(totalPrice);
        String roomPriceDigits = roomPrice.replaceAll("[^0-9]", "");
        int roomPriceValue = Integer.parseInt(roomPriceDigits);
        return promo_Price+total_Price==roomPriceValue;
    }

    public void addPromoWithCode(String code) {
        driverTools.waitForElementToAppear(PROMO_FIELD).orElseThrow();
        driverTools.clickWithJavascriptExecutor(PROMO_FIELD);
        driverTools.sendKeys(PROMO_FIELD, code);
        driverTools.waitForElementToAppear(REQUIRED_PROMO).orElseThrow();
        driverTools.clickWithJavascriptExecutor(REQUIRED_PROMO);
    }

    public void clickCheckOtherPromoCodes() {
        driverTools.waitForElementToAppear(PROMO_MODAL_OPEN).orElseThrow();
        driverTools.clickWithJavascriptExecutor(PROMO_MODAL_OPEN);
    }


    public void clickPromoSection() {
        driverTools.scrollIntoWebView(PROMO_AREA);
        driverTools.waitForElementToAppear(PROMO_AREA).orElseThrow();
        driverTools.clickWithJavascriptExecutor(PROMO_AREA);
    }

    public void clickToAddPromo() {
        driverTools.scrollIntoWebView(APPLY_BUTTON);
        driverTools.waitForElementToAppear(APPLY_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(APPLY_BUTTON);
    }

    public boolean isPayingThisAmountTextInGreen() {
        return driverTools.waitForElementToPresence(PAYING_AMOUNT_TEXT_GREEN, 10).isPresent();
    }
public String getTotalToPay() {
    return driverTools.waitForElementToPresence(getTotalToPayLocator(), 10)
            .map(driverTools::getText)
            .orElseThrow(() -> new NoSuchElementException("Total to pay not found on booking form"));
}

    private Locator getTotalToPayLocator() {
        return new Locator(
                "TOTAL_TO_PAY", new By.ByXPath("//p[contains(@class,'PriceFooter_final_price_text')]"));
    }
    public void continueToAddOnPage() {
        continueToNextPage();
    }

    public void clickExtraFacilityAddOn() {
        driverTools.waitForElementToAppear(ADDONS_BUTTON, 10);
        driverTools.scrollIntoWebView(ADDONS_BUTTON);
        driverTools.clickWithJavascriptExecutor(ADDONS_BUTTON);
        driverTools.waitForElementToAppear(ADDONS_RADIO_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(ADDONS_RADIO_BUTTON);
        driverTools.waitForElementToAppear(ADDONS_INC_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(ADDONS_INC_BUTTON);
        driverTools.waitForElementToAppear(ADDONS_SAVE_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(ADDONS_SAVE_BUTTON);
    }

    public void addInsuranceOnBookingFormPage2(String estimatedTotal) {
        driverTools.waitForElementToAppear(INSURANCE_BUTTON, 6);
        if (driverTools.isElementDisplayed(INSURANCE_BUTTON)) {
            String buttonText = driverTools.getText(INSURANCE_BUTTON).orElse("");
            if (buttonText.contains("Add") || buttonText.contains("Tambah")) {
                driverTools.clickWithJavascriptExecutor(INSURANCE_BUTTON);
            }
            verifyEstimatedTotalUnchangedAfterAddOns(estimatedTotal);
        }
    }

    public String getPayNowPriceBookingForm() {
        driverTools.scrollIntoWebView(PAY_NOW_PRICE);
        WebElement payNowElement = driverTools.waitForElementToAppear(PAY_NOW_PRICE, 10).orElseThrow();
        String payNowPrice = driverTools.getText(payNowElement);
        logger.log("pay now price on booking form -> " + payNowPrice);
        return payNowPrice;
    }

    public String getPayUponCheckInPriceBookingForm() {
        driverTools.scrollIntoWebView(PAY_UPON_CHECK_IN_PRICE);
        WebElement payUponCheckInElement = driverTools.waitForElementToAppear(PAY_UPON_CHECK_IN_PRICE, 10).orElseThrow();
        String payUponCheckInPrice = driverTools.getText(payUponCheckInElement);
        logger.log("pay upon check-in price on booking form -> " + payUponCheckInPrice);
        return payUponCheckInPrice;
    }

    public String getEstimatedTotalFromBookingFormPage1() {
        driverTools.scrollIntoWebView(PROPERTY_FEE_DISCLAIMER);
        WebElement disclaimerElement = driverTools.waitForElementToAppear(PROPERTY_FEE_DISCLAIMER, 10).orElseThrow();
        String disclaimerText = driverTools.getText(disclaimerElement);
        logger.log("property fee disclaimer text -> " + disclaimerText);
        String estimatedTotal = extractPriceAfterMarkers(disclaimerText, ESTIMATED_TOTAL_MARKERS);
        logger.log("estimated total from booking form page 1 -> " + estimatedTotal);
        return estimatedTotal;
    }

    public String getTotalPriceBookingFormPage2() {
        String totalPrice = getPriceSummaryTotal();
        logger.log("total price on booking form page 2 -> " + totalPrice);
        return totalPrice;
    }

    public String getPayUponCheckInPriceBookingFormPage2() {
        driverTools.scrollIntoWebView(BOOKING_FORM_PAGE2_PAY_UPON_CHECK_IN);
        WebElement payUponCheckInElement = driverTools.waitForElementToAppear(BOOKING_FORM_PAGE2_PAY_UPON_CHECK_IN, 10).orElseThrow();
        String payUponCheckInPrice = driverTools.getText(payUponCheckInElement);
        logger.log("pay upon check-in price on booking form page 2 -> " + payUponCheckInPrice);
        return payUponCheckInPrice;
    }

    public String getEstimatedTotalFromBookingFormPage2() {
        driverTools.scrollIntoWebView(BOOKING_FORM_PAGE2_ESTIMATED_TOTAL);
        WebElement estimatedTotalElement = driverTools.waitForElementToAppear(BOOKING_FORM_PAGE2_ESTIMATED_TOTAL, 10).orElseThrow();
        String estimatedTotalText = driverTools.getText(estimatedTotalElement);
        logger.log("estimated total text on booking form page 2 -> " + estimatedTotalText);
        String estimatedTotal = extractPriceAfterMarkers(estimatedTotalText, ESTIMATED_TOTAL_MARKERS);
        logger.log("estimated total from booking form page 2 -> " + estimatedTotal);
        return estimatedTotal;
    }

    public void verifyPayNowPriceMatchesRoomPrice(String roomPrice) {
        String payNowPrice = getPayNowPriceBookingForm();
        logger.log("roomPrice from room list -> " + roomPrice);
        logger.log("payNowPrice on booking form -> " + payNowPrice);
        assertPriceEquals("Pay now price on booking form matches room list price", payNowPrice, roomPrice);
    }

    public void verifyEstimatedTotalIsCorrect(String roomPrice, String payUponCheckInPrice, String estimatedTotal) {
        double expected = doubleRound(convertPrice(payUponCheckInPrice) + convertPrice(roomPrice));
        double actual = convertPrice(estimatedTotal);
        logger.log("roomPrice -> " + roomPrice);
        logger.log("payUponCheckInPrice -> " + payUponCheckInPrice);
        logger.log("expected estimated total (pay upon check-in + room price) -> " + expected);
        logger.log("actual estimated total -> " + estimatedTotal + " (" + actual + ")");
        assertPriceEquals("Estimated total equals pay upon check-in plus room price", actual, expected);
    }

    public void verifyTotalPriceMatchesPriceOnBookingForm(String roomPrice) {
        String totalPricePage2 = getTotalPriceBookingFormPage2();
        logger.log("roomPrice from room list -> " + roomPrice);
        logger.log("total price on booking form page 2 -> " + totalPricePage2);
        assertPriceEquals("Total price on booking form page 2 matches room list price", totalPricePage2, roomPrice);
    }

    public void verifyEstimatedTotalIsCorrectOnBF2(String estimatedPage1, String estimatedPage2) {
        logger.log("estimated total booking form page 1 -> " + estimatedPage1);
        logger.log("estimated total booking form page 2 -> " + estimatedPage2);
        assertPriceEquals("Estimated total on booking form page 1 matches page 2", estimatedPage2, estimatedPage1);
    }

    public void verifyEstimatedTotalUnchangedAfterAddOns(String estimatedTotalBeforeAddOns) {
        String estimatedTotalAfter = getEstimatedTotalFromBookingFormPage2();
        logger.log("estimated total before add-ons/insurance -> " + estimatedTotalBeforeAddOns);
        logger.log("estimated total after add-ons/insurance -> " + estimatedTotalAfter);
        assertPriceEquals("Estimated total unchanged after adding add-ons or insurance on booking form page 2",
                estimatedTotalAfter, estimatedTotalBeforeAddOns);
    }

    public boolean isGiftVoucherSectionVisibleUnderInsurance() {
        driverTools.waitForElementToAppear(HOTEL_INSURANCE_PAGE, 10);
        driverTools.scrollIntoWebViewOptional(INSURANCE_GIFT_VOUCHER_SECTION);
        return driverTools.waitForElementToAppear(INSURANCE_GIFT_VOUCHER_SECTION, 10).isPresent();
    }

    public void claimGiftVoucherUnderInsurance() {
        driverTools.scrollIntoWebView(INSURANCE_GIFT_VOUCHER_CLAIM_BTN);
        driverTools.waitForElementToAppear(INSURANCE_GIFT_VOUCHER_CLAIM_BTN, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(INSURANCE_GIFT_VOUCHER_CLAIM_BTN);
    }

    public boolean isGiftVoucherClaimedUnderInsurance() {
        return driverTools.waitForElementToAppear(INSURANCE_GIFT_VOUCHER_CLAIMED_BTN, 10).isPresent();
    }

    public boolean isGiftVoucherClaimSnackbarVisible() {
        return driverTools.waitForElementToPresence(GIFT_VOUCHER_CLAIMED_SNACKBAR, 10).isPresent();
    }

    public void verifyGiftVoucherClaimUnderInsurance() {
        Assertion.assertThat("Gift voucher button should change to claimed state under insurance section",
                isGiftVoucherClaimedUnderInsurance(), is(true));
        Assertion.assertThat("Gift voucher claim snackbar should be shown",
                isGiftVoucherClaimSnackbarVisible(), is(true));
    }

    public void verifyGiftVoucherCannotBeUnclaimedUnderInsurance() {
        driverTools.scrollIntoWebViewOptional(INSURANCE_GIFT_VOUCHER_CLAIMED_BTN);
        WebElement claimedButton = driverTools.waitForElementToAppear(INSURANCE_GIFT_VOUCHER_CLAIMED_BTN, 10).orElseThrow();
        String buttonText = driverTools.getText(claimedButton).trim();
        boolean isClaimedLabel = "Claimed".equalsIgnoreCase(buttonText) || "Diklaim".equalsIgnoreCase(buttonText);
        Assertion.assertThat("Claimed voucher button text should be Claimed or Diklaim", isClaimedLabel, is(true));
        Assertion.assertThat("Claimed voucher button should be disabled",
                isClaimedGiftVoucherButtonDisabled(claimedButton), is(true));
        driverTools.clickWithJavascriptExecutor(INSURANCE_GIFT_VOUCHER_CLAIMED_BTN);
        Assertion.assertThat("Voucher should remain claimed after attempting to click the disabled button",
                isGiftVoucherClaimedUnderInsurance(), is(true));
        WebElement claimedButtonAfterClick = driverTools.waitForElementToAppear(INSURANCE_GIFT_VOUCHER_CLAIMED_BTN, 10).orElseThrow();
        Assertion.assertThat("Claimed voucher button should stay disabled after click attempt",
                isClaimedGiftVoucherButtonDisabled(claimedButtonAfterClick), is(true));
    }

    private boolean isClaimedGiftVoucherButtonDisabled(WebElement button) {
        String classAttr = button.getAttribute("class");
        return classAttr != null && classAttr.contains("Button_state_disabled");
    }

    public void clickPayZeroIdrNowButton() {
        driverTools.scrollIntoWebView(PAY_ZERO_IDR_AMOUNT_BUTTON);
        driverTools.waitForElementToAppear(PAY_ZERO_IDR_AMOUNT_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(PAY_ZERO_IDR_AMOUNT_BUTTON);
    }

    public void checkCustomCheckInBoxConditions(){
        driverTools.waitForElementToAppear(CUSTOM_CHECKIN_BOX, 10);
        driverTools.clickWithJavascriptExecutor(CUSTOM_CHECKIN_BOX);
    }

    public void clickConfirmRescheduleButton(){
        driverTools.waitForElementToAppear(CONFIRM_RESCHEDULE_REQUEST, 10);
        driverTools.clickWithJavascriptExecutor(CONFIRM_RESCHEDULE_REQUEST);
    }
}
