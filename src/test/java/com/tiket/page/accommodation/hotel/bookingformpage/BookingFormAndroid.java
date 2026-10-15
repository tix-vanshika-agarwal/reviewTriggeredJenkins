package com.tiket.page.accommodation.hotel.bookingformpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.AddOnsType;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.payment.PaymentPageAndroid;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.page.accommodation.hotel.detailpage.DetailPageAndroid.CLOSE_POPUP_BUTTON;
import static com.tiket.page.accommodation.hotel.detailpage.DetailPageAndroid.INCREMENT_BUTTON;
import static com.tiket.page.accommodation.hotel.roomlistpage.RoomListPage.selectedRoomName;
import static com.tiket.page.accommodation.hotel.roomlistpage.RoomListPageAndroid.ROOM_FLEXIBLE_PAYMENT;
import static com.tiket.page.common.booking_form.BookingFormPageAndroid.*;
import static com.tiket.page.setting.SettingPageAndroid.SNACKBAR_TEXT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

public class BookingFormAndroid <T extends WebDriver> extends BookingFormPage<T> {

    public BookingFormAndroid(Session<T> session) {
        super(session);
    }

    private static final Locator HEADER_TITLE = new Locator("HEADER_TITLE", AppiumBy.id("tds_header_navigation_appbar"));
    private static final Locator BOOKING_FORM_V3 = new Locator("BOOKING_FORM_V3", AppiumBy.id("rv_checkout"));
    private static final Locator HOTEL_NAME_TEXT_V3 = new Locator("HOTEL_NAME_TEXT_V3", AppiumBy.id("tv_hotel_details_name"));
    private static final Locator HOTEL_ROOM_NAME_TEXT_V3 = new Locator("HOTEL_ROOM_NAME_TEXT_V3", AppiumBy.id("tv_hotel_room_name"));
    private static final Locator CHECKIN_TEXT_V3 = new Locator("CHECKIN_TEXT_V3", AppiumBy.id("tv_hotel_checkin_date"));
    private static final Locator NIGHT_DURATION_TEXT_V3 = new Locator("NIGHT_DURATION_TEXT_V3", AppiumBy.id("tv_hotel_details_night"));
    private static final Locator EDIT_CONTACT_DETAIL_BUTTON_V3 = new Locator("EDIT_CONTACT_DETAIL_BUTTON_V3", AppiumBy.id("iv_edit_person_details"));
    private static final Locator NAME_CONTACT_FIELD_V3 = new Locator("NAME_CONTACT_FIELD_V3", AppiumBy.xpath("//*[contains(@resource-id,'et_form') and (@text='Nama' or @text='Name')]"));
    private static final Locator TITLE_CONTACT_BUTTON_V3 = new Locator("TITLE_CONTACT_BUTTON_V3", AppiumBy.xpath("//*[contains(@resource-id,'et_form') and (@text='Titel' or @text='Title')]"));
    private static final Locator TITLE_CONTACT_BUTTON_V3_DROPDOWN = new Locator("TITLE_CONTACT_BUTTON_V3_DROPDOWN", AppiumBy.xpath("(//*[contains(@resource-id,'iv_arrow_down_form')])[1]"));
    private static final Locator TITLE_CHECKBOX_V3 = new Locator("TITLE_CHECKBOX_V3", AppiumBy.id("tv_recycler_view_item_radio_button_title"));
    private static final Locator PHONE_NUMBER_CONTACT_V3 = new Locator("PHONE_NUMBER_CONTACT_V3", AppiumBy.xpath("//*[contains(@resource-id,'et_form') and (@text='Nomor Telepon' or @text='Phone number')]"));
    private static final Locator FILLED_NAME_CONTACT_FIELD_V3 = new Locator("NAME_CONTACT_FIELD_V3", AppiumBy.xpath("//*[contains(@resource-id,'et_form') and (contains(@text,'test'))]"));
    private static final Locator FILLED_PHONE_NUMBER_CONTACT_V3 = new Locator("PHONE_NUMBER_CONTACT_V3", AppiumBy.xpath("//*[contains(@resource-id,'et_form') and (contains(@text,'8123'))]"));
    private static final Locator FILLED_EMAIL_CONTACT_V3 = new Locator("EMAIL_CONTACT_V3", AppiumBy.xpath("//*[contains(@resource-id,'et_form') and (contains(@text,'-aut@tiket.com'))]"));
    private static final Locator TOTAL_TO_PAY = new Locator("TOTAL_TO_PAY", AppiumBy.xpath("//*[contains(@resource-id,'price_summary_view')]/*[contains(@resource-id,'parent')]/*[contains(@resource-id,'tv_right')]"));
    private static final Locator PRICE_SUMMARY_CONTAINER = new Locator("PRICE_SUMMARY_CONTAINER", AppiumBy.xpath("//*[contains(@resource-id,'price_summary_container')]"));
    private static final Locator EMAIL_CONTACT_V3 = new Locator("EMAIL_CONTACT_V3", AppiumBy.xpath("//*[contains(@resource-id,'et_form') and (contains(@text,'Email'))]"));
    private static final Locator HOTEL_NAME_TEXT = new Locator("HOTEL_NAME_TEXT", AppiumBy.id("tv_hotel_name"));
    private static final Locator CHECKIN_TEXT = new Locator("CHECKIN_TEXT", AppiumBy.id("tv_stay_date"));
    private static final Locator NIGHT_DURATION_TEXT = new Locator("NIGHT_DURATION_TEXT", AppiumBy.id("tv_night_room"));
    private static final Locator TOTAL_PRICE_TEXT = new Locator("TOTAL_PRICE_TEXT", AppiumBy.id("tv_total_price_value"));
    private static final Locator PAY_NOW_PRICE = new Locator("PAY_NOW_PRICE", AppiumBy.id("tv_pay_now_price"));
    private static final Locator PAY_UPON_CHECK_IN_PRICE = new Locator("PAY_UPON_CHECK_IN_PRICE", AppiumBy.id("tv_pay_upon_check_in_price"));
    private static final Locator PROPERTY_FEE_DISCLAIMER = new Locator("PROPERTY_FEE_DISCLAIMER", AppiumBy.id("tv_disclaimer"));
    private static final Locator BOOKING_FORM_PAGE2_PAY_UPON_CHECK_IN = new Locator("BOOKING_FORM_PAGE2_PAY_UPON_CHECK_IN",
            AppiumBy.xpath("(//*[contains(@class,'android.widget.TextView') and contains(@resource-id,'tv_right')])[last()]"));
    private static final Locator BOOKING_FORM_PAGE2_ESTIMATED_TOTAL = new Locator("BOOKING_FORM_PAGE2_ESTIMATED_TOTAL",
            AppiumBy.xpath("//*[contains(@resource-id,'parent') and (contains(@text,'Estimated total') or contains(@text,'Perkiraan total'))]"));
    private static final Locator INSURANCE_ADD_BUTTON = new Locator("INSURANCE_ADD_BUTTON",
            AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (@text='Add' or @text='tambhakan' or @text='Tambahkan')]"));
    private static final Locator GET_REFUND_TEXT = new Locator("GET_REFUND_TEXT", AppiumBy.id("tv_total_refund"));
    private static final Locator MR_SALUTATION_CHECKBOX = new Locator("MR_SALUTATION_CHECKBOX", AppiumBy.id("rb_salutation_mr"));
    private static final Locator MRS_SALUTATION_CHECKBOX = new Locator("MRS_SALUTATION_CHECKBOX", AppiumBy.id("rb_salutation_mrs"));
    private static final Locator MS_SALUTATION_CHECKBOX = new Locator("MS_SALUTATION_CHECKBOX", AppiumBy.id("rb_salutation_ms"));
    private static final Locator FULL_NAME_INPUT = new Locator("FULL_NAME_INPUT", AppiumBy.xpath("//*[contains(@resource-id,'view_smart_contact')]//*[contains(@resource-id,'tds_box_autocomplete_input_text')]"));
    private static final Locator PHONE_NUMBER_INPUT = new Locator("PHONE_NUMBER_INPUT", AppiumBy.xpath("//*[contains(@resource-id,'input_phone')]//*[contains(@resource-id,'tds_box_input_text')]"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", AppiumBy.xpath("//*[contains(@resource-id,'input_email')]//*[contains(@resource-id,'tds_box_input_text')]"));
    private static final Locator EDIT_GUEST_BUTTON = new Locator("EDIT_GUEST_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tv_empty_guest') or (contains(@text,'Enter the guest name here') or contains(@text,'Masukkan nama tamu di sini'))]"));
    private static final Locator GUEST_NAME_LIST = new Locator("GUEST_NAME_LIST", AppiumBy.xpath("//*[contains(@resource-id,'tv_guest') and not(contains(@resource-id,'tv_guest_details')) and string-length(@text) > 0 and not(contains(@resource-id,'tv_special_request'))]"));
    private static final Locator SPECIAL_REQUEST_TAB = new Locator("SPECIAL_REQUEST_TAB", AppiumBy.id("tv_special_request"));
    private static final Locator SPECIAL_REQUEST_TEXTFIELD = new Locator("SPECIAL_REQUEST_TEXTFIELD", AppiumBy.xpath("//*[contains(@resource-id,'text_special_request')]//android.widget.EditText"));
    private static final Locator EDIT_CONTACT_BUTTON = new Locator("EDIT_CONTACT_BUTTON", AppiumBy.id("iv_edit_contact"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'btn_save')]//*[contains(@resource-id,'tds_btn') or contains(@resource-id,'tds_button_view')]"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'btn_continue')]//*[contains(@resource-id,'tds_button_view')]"));
    private static final Locator DOWNPAYMENT_CONTENT = new Locator("DOWNPAYMENT_CONTENT", AppiumBy.xpath("//*[contains(@resource-id,'tv_popup_title') and (contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'downpayment') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'tiket dp'))]"));
    private static final Locator PAY_BY_BUTTON = new Locator("PAY_BY_BUTTON", AppiumBy.id("radio_price_lock"));
    private static final Locator PAY_BY_BUTTON_SELECTED = new Locator("PAY_BY_BUTTON_SELECTED", AppiumBy.xpath("//*[contains(@resource-id,'radio_price_lock') and count(*) > 1]"));
    private static final Locator CONFIRM_BOOKING_CONTINUE_BUTTON = new Locator("CONFIRM_BOOKING_CONTINUE_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='continue' or translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='lanjut']"));
    private static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON", AppiumBy.id("tds_close_transparent"));
    private static final Locator PAY_NOW_BUTTON = new Locator("PAY_NOW_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'radio_pay_now')]"));
    private static final Locator SELECT_PAYMENT_BUTTON = new Locator("SELECT_PAYMENT_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'ll_parent')]//*[contains(@resource-id,'tv_primary')]"));
    private static final Locator SELECT_PAYMENT_SUB_TEXT = new Locator("SELECT_PAYMENT_SUB_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'ll_parent')]//*[contains(@resource-id,'tv_secondary')]"));
    private static final Locator EXTRA_PROTECTION_TAB_SECTION = new Locator("EXTRA_PROTECTION_TAB_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'cv_compound_content')]//*[contains(@resource-id, 'tds_segmented_control_button_title')]"));
    private static final Locator EXTRA_PROTECTION_CANCEL = new Locator("EXTRA_PROTECTION_CANCEL", AppiumBy.xpath("(//*[contains(@resource-id, 'tds_button_view') and contains(@text,'Cancel')or contains(@text,'Batalkan')])[last()]"));
    private static final Locator EXTRA_PROTECTION_PREMIUM_TAB = new Locator("EXTRA_PROTECTION_PREMIUM_TAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_segmented_control_button_title') and @text='Premium']"));
    private static final Locator EXTRA_PROTECTION_STANDARD_TAB = new Locator("EXTRA_PROTECTION_STANDARD_TAB", AppiumBy.xpath("//*[contains(@resource-id, 'tds_segmented_control_button_title') and contains(@text,'Standar')]"));
    private static final Locator EXTRA_PROTECTION_ADD_BUTTON = new Locator("EXTRA_PROTECTION_ADD_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'segmented_control_view_holder')]//following-sibling::*[contains(@resource-id,'card_compound_button')]//*[contains(@resource-id, 'tds_button_view')]"));
    private static final Locator TOTAL_PRICE_DETAIL_ARROW_BUTTON = new Locator("TOTAL_PRICE_DETAIL_ARROW_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'iv_chevron_bottom') or contains(@resource-id,'iv_arrow_down')]"));
    private static final Locator SUBTOTAL_BUTTON = new Locator("SUBTOTAL_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'price_summary_view')]//*[@text='Subtotal']"));
    private static final Locator EXTRA_PROTECTION_ON_TOTAL_PRICE_DETAIL = new Locator("EXTRA_PROTECTION_ON_TOTAL_PRICE_DETAIL", AppiumBy.xpath("(//*[contains(@resource-id, 'tv_left') and (contains(@text, 'Protection') or contains(@text, 'Perlindungan'))]//following-sibling::*[contains(@resource-id,'tv_right')])[last()]"));
    private static final Locator EXTRA_PROTECTION_PRICE = new Locator("EXTRA_PROTECTION_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'segmented_control_view_holder')]//following-sibling::*[contains(@resource-id,'price_tv_container')]//*[contains(@resource-id, 'tv_price')]"));
    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.id("tds_icon_navigation_toolbar_group"));
    private static final Locator SEARCH_OTHER_ROOM_BUTTON = new Locator("SEARCH_OTHER_ROOM_BUTTON", AppiumBy.xpath("//*[contains(@resource-id, 'tds_btn_secondary_info_view')]//*[contains(@resource-id, 'tds_button_view')]"));
    private static final Locator CONTACT_DETAIL_NAME = new Locator("CONTACT_DETAIL_NAME", AppiumBy.id("tv_contact_name"));
    private static final Locator CONTACT_DETAIL_EMAIL = new Locator("CONTACT_DETAIL_EMAIL", AppiumBy.id("tv_contact_email"));
    private static final Locator CONTACT_DETAIL_PHONENUMBER = new Locator("CONTACT_DETAIL_PHONENUMBER", AppiumBy.id("tv_contact_phone"));
    private static final Locator GUEST_DETAIL_NAME = new Locator("GUEST_DETAIL_NAME", AppiumBy.xpath("//*[contains(@resource-id,'rv_guests')]//*[contains(@resource-id,'tv_guest')]"));
    private static final Locator SUBMIT_RESCHEDULE_BOOK_BUTTON = new Locator("SUBMIT_RESCHEDULE_BOOK_BUTTON", AppiumBy.id("b_payment"));
    private static final Locator AGREE_TNC_CHECK = new Locator("AGREE_TNC_CHECK", AppiumBy.id("cb_reschedule_tnc"));
    private static final Locator PROPOSE_RESCHEDULE_BUTTON = new Locator("PROPOSE_RESCHEDULE_BUTTON", AppiumBy.id("tv_top_button"));
    private static final Locator BOOK_FORM_VIEW = new Locator("BOOK_FORM_VIEW", AppiumBy.xpath("//*[contains(@resource-id,'view_root_checkout')]/child::*[contains(@resource-id,'cl_container')]"));
    private static final Locator PRICE_CHANGE_POPUP = new Locator("PRICE_CHANGE_POPUP", AppiumBy.xpath("//*[contains(@resource-id,'tds_txt_header_info_view') and (contains(@text,'updated') or contains(@text,'diperbarui') or contains(@text,'turun') or contains(@text,'cheaper'))]"));
    private static final Locator POLICY_CHANGE_POPUP = new Locator("POLICY_CHANGE_POPUP", AppiumBy.xpath("//*[contains(@resource-id,'tds_txt_header_info_view') and (contains(@text,'changed') or contains(@text,'berubah'))]"));
    private static final Locator PRICE_CHANGE_DESCRIPTION = new Locator("PRICE_CHANGE_DESCRIPTION", AppiumBy.id("tds_txt_body_info_view"));
    private static final Locator PRICE_CHANGE_GO_PAYMENT = new Locator("PRICE_CHANGE_GO_PAYMENT", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'continue booking') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lanjut pesan') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'go to the payment page') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'ke halaman pembayaran'))]"));
    private static final Locator RATE_PLAN_BOTTOM_SHEET = new Locator("RATE_PLAN_BOTTOM_SHEET", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Lanjut pesan') or contains(@text,'Continue booking') or contains(@text,'Lanjutkan pemesanan'))]"));
    private static final Locator RATE_PLAN_RATE_CHANGES= new Locator("RATE_PLAN_RATE_CHANGES", AppiumBy.xpath("//*[contains(@resource-id,'rv_room_changes')]//*[contains(@resource-id,'tv_title') and (contains(@text,'Price') or contains(@text,'Harga'))]"));
    private static final Locator RATE_PLAN_BOTTOM_SHEET_BOOKING_PAGE_PART_1 = new Locator("RATE_PLAN_BOTTOM_SHEET_BOOKING_PAGE_PART_1", AppiumBy.xpath("(//*[contains(@resource-id,'tds_main_info_container_view')]//*[contains(@resource-id,'tds_button_view')])[2]"));
    private static final Locator RATE_PLAN_SNACK_BAR_TEXT = new Locator("RATE_PLAN_SNACK_BAR_TEXT", AppiumBy.id("tds_snackbar_text"));
    private static final Locator RATE_PLAN_SNACK_BAR_BTN = new Locator("RATE_PLAN_SNACK_BAR_BTN", AppiumBy.id("tds_snackbar_action"));
    private static final Pattern PRICE_PATTERN = Pattern.compile("[A-Z]{2,4}\\s*[\\d.,]+");
    private static final Locator PRICE_BREAKDOWN = new Locator("PRICE_BREAKDOWN", AppiumBy.xpath("//*[contains(@resource-id,'tv_left') and not(contains(@text,'check-in'))]//following-sibling::*[contains(@resource-id,'tv_right') and not(contains(@resource-id,'bot'))]"));
    /** All price rows (value column) in price summary, excluding the bottom total (tv_right_bot). Used for display log. */
    private static final Locator PRICE_SUMMARY_ALL_RIGHTS = new Locator("PRICE_SUMMARY_ALL_RIGHTS", AppiumBy.xpath("//*[contains(@resource-id,'price_summary_view')]//*[contains(@resource-id,'tv_right') and not(contains(@resource-id,'bot'))]"));
    /** Bottom total value in price breakdown (Pay now / after taxes). */
    private static final Locator TOTAL_PRICE_PRICE_BREAKDOWN = new Locator("TOTAL_PRICE_PRICE_BREAKDOWN", AppiumBy.id("tv_right_bot"));
    private static final Locator TOTAL_PRICE_LABEL_BREAKDOWN = new Locator("TOTAL_PRICE_LABEL_BREAKDOWN", AppiumBy.id("tv_left_bot"));
    private static final Locator ROOM_TYPE_PRICE_BREAKDOWN = new Locator("ROOM_TYPE_PRICE_BREAKDOWN", AppiumBy.xpath("//*[contains(@resource-id,'tv_room_name')]//preceding-sibling::*[contains(@class,'FrameLayout')]//*[contains(@class,'LinearLayout')]//*[contains(@class,'TextView')]"));
    private static final Locator PRICE_SUMMARY_CASHBACK = new Locator("PRICE_SUMMARY_CASHBACK", AppiumBy.id("tv_text"));
    private static final Locator PRICE_DETAIL_CLOSE_BTN = new Locator("PRICE_DETAIL_CLOSE_BTN", AppiumBy.xpath("//*[contains(@resource-id,'iv_close') or contains(@resource-id,'v_toolbar_left_button')]"));
    private static final Locator ROOM_NAME_TEXT = new Locator("ROOM_NAME_TEXT", AppiumBy.id("tv_room_type"));
    private static final Locator CHILD_GUEST_COUNT = new Locator("CHILD_GUEST_COUNT", By.xpath("//*[contains(@resource-id,'tv_facility') and (contains(@text,'Child') or contains(@text,'Anak')) and not(contains(@text,'('))]"));
    private static final Locator CHILD_MEAL_COUNT = new Locator("CHILD_MEAL_COUNT", AppiumBy.xpath("//*[contains(@resource-id,'tv_facility') and( contains(@text,'Child') or contains(@text,'Anak') ) and contains(@text,'(')]"));
    private static final Locator ROOM_DETAIL_BUTTON = new Locator("ROOM_DETAIL_BUTTON", AppiumBy.id("iv_room_detail"));
    private static final Locator PAYING_AMOUNT_TEXT_GREEN = new Locator("PAYING_AMOUNT_TEXT_GREEN", AppiumBy.xpath("//*[contains(@text,'cashback')]"));
    private static final Locator CHECKOUT_TEXT = new Locator("CHECKOUT_TEXT", AppiumBy.id("tv_date_check_out"));
    private static final Locator CHECKIN_TIME_TEXT = new Locator("CHECKIN_TIME_TEXT", AppiumBy.id("tv_time_check_in"));
    private static final Locator ROOM_GUEST_TEXT = new Locator("ROOM_GUEST_TEXT", AppiumBy.xpath("(//*[contains(@resource-id, 'tv_facility')])[1]"));
    private static final Locator ROOM_BED_TEXT = new Locator("ROOM_BED_TEXT", AppiumBy.xpath("(//*[contains(@resource-id, 'tv_facility')])[2]"));
    private static final Locator CANCELLATION_TEXT = new Locator("CANCELLATION_TEXT", AppiumBy.id("tv_refund_policy_summary"));
    private static final Locator BOOKING_SUMMARY_CONTAINER_NON_LOGIN = new Locator("BOOKING_SUMMARY_CONTAINER_NON_LOGIN", AppiumBy.id("view_non_login_user_contact_details_container"));
    private static final Locator BOOKING_SUMMARY_CONTAINER_LOGIN = new Locator("BOOKING_SUMMARY_CONTAINER_LOGIN", AppiumBy.id("view_login_user_contact_details"));
    private static final Locator ADDON_PRICE_CARD = new Locator("ADDON_PRICE_CARD", AppiumBy.xpath("//*[contains(@resource-id, 'rv_add_ons')]//*[contains(@resource-id, 'tv_price')]"));
    private static final Locator ADDON_SELECT_BUTTON = new Locator("ADDON_SELECT_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'rv_add_ons')]//*[contains(@resource-id,'tds_button_view')]"));
    private static final Locator ROOM_ADD_ONS_CHECKBOX = new Locator("ROOM_ADD_ONS_CHECKBOX", AppiumBy.xpath("//*[contains(@resource-id,'crf_bottom_sheet')]//*[contains(@resource-id,'rv_room')]//*[contains(@resource-id,'fl_check')]"));
    private static final Locator ADDON_NAME_CARD = new Locator("ADDON_NAME_CARD", AppiumBy.xpath("//*[contains(@resource-id, 'rv_add_ons')]//*[contains(@resource-id, 'tv_title')]"));
    private static final Locator ADDON_CONTAINER = new Locator("ADDON_CONTAINER", AppiumBy.xpath("//*[contains(@resource-id,'rv_add_ons')]//*[contains(@class,'ViewGroup')]"));
    private static final Locator ADDON_CATEGORY = new Locator("ADDON_CATEGORY", AppiumBy.xpath("//*[contains(@resource-id, 'rv_category')]//*[contains(@resource-id, 'tds_chips_text')]"));
    private static final Locator ADDON_CATEGORY_SELECTED = new Locator("ADDON_CATEGORY_SELECTED", AppiumBy.xpath("//*[contains(@resource-id, 'rv_category')]//*[contains(@resource-id, 'tds_chips_text') and @selected='true']"));
    private static final Locator ADDON_NAME_POPUP = new Locator("ADDON_NAME_POPUP", AppiumBy.id("tv_title"));
    private static final Locator ADDON_PRICE_POPUP = new Locator("ADDON_PRICE_POPUP", AppiumBy.id("tv_price"));
    private static final Locator ADDON_TOTAL_PRICE_POPUP = new Locator("ADDON_TOTAL_PRICE_POPUP", AppiumBy.id("tv_total_price"));
    private static final Locator ROOM_TYPE_TAB = new Locator("ROOM_TYPE_TAB", AppiumBy.id("tds_title_tab_layout"));
    private static final Locator ROOM_GUEST_INFO = new Locator("ROOM_GUEST_INFO", AppiumBy.xpath("//*[contains(@resource-id,'v_guest_form')]//*[contains(@resource-id,'tv_room_name')]"));
    private static final Locator PAY_AT_HOTEL_TOTAL_PRICE = new Locator("PAY_AT_HOTEL_TOTAL_PRICE", AppiumBy.id("tv_total_price_value"));
    private static final Locator LOYALTY_BENEFIT_DISCOUNT_ON_PRICE_DETAIL = new Locator("LOYALTY_BENEFIT_DISCOUNT_ON_PRICE_DETAIL", AppiumBy.xpath("//*[(contains(@resource-id,'tv_price_detail_name') or contains(@resource-id,'tv_title'))and contains(@text, 'tier') or contains(@text, 'Tier')]//following-sibling::*[contains(@resource-id,'tv_price_detail_value') or contains(@resource-id,'tv_price')]"));
    private static final Locator CANCEL_BUTTON = new Locator("CANCEL_BUTTON", AppiumBy.id("iv_close"));
    public static final Locator LOADING_ICON = new Locator("LOADING_ICON", By.id("animation_view_dot"));
    public static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", By.id("tds_link_action_text"));
    public static final Locator ROOM_NAME = new Locator("ROOM_NAME", By.id("tv_room_type"));
    public static final Locator ONE_ROOM_POPUP = new Locator("ONE_ROOM_POPUP", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Log in and book 1 room type') or contains(@text,'Log in dan pesan 1 tipe kamar'))]"));
    public static final Locator MO_CORPORATE_POPUP = new Locator("MO_CORPORATE_POPUP", AppiumBy.xpath("//*[contains(@resource-id, 'tds_txt_header_info_view') and contains(@text,'This booking does not apply to corporate accounts') or contains(@text,'Pemesanan ini tidak berlaku untuk akun corporate')]"));
    private static final Locator LOCAL_PRICE_LABEL = new Locator("LOCAL_PRICE_LABEL", AppiumBy.id("tv_approximate_local_price_value"));
    private static final Locator LOCAL_PRICE_DETAIL_LABEL = new Locator("LOCAL_PRICE_DETAIL_LABEL", AppiumBy.id("tv_local_price_value"));
    private static final Locator REFUND_ESTIMATION = new Locator("REFUND_ESTIMATION", AppiumBy.xpath("//*[contains(@text,'totalPriceTableViewCell')]//*[contains(@text,'refund')]"));
    private static final Locator REFUND_LABEL_DETAIL = new Locator("REFUND_LABEL_DETAIL", AppiumBy.id("tv_get_refund"));
    private static final Locator COUNTRY_CODE_DROPDOWN = new Locator("COUNTRY_CODE_DROPDOWN", AppiumBy.xpath("//*[contains(@resource-id,'et_form') and (contains(@text,'Kode') or contains(@text,'Code'))]"));
    private static final Locator FIRST_COUNTRY_CODE_OPTION = new Locator("FIRST_COUNTRY_CODE_OPTION",AppiumBy.xpath("(//*[contains(@resource-id,'rv_country_auto_complete')]//*[contains(@resource-id,'cl_item_group')])[1]"));
    private static final Locator SAVE_CONTACT_DETAIL = new Locator("SAVE_CONTACT_DETAIL", AppiumBy.id("btn_save_contact_details"));
    private static final Locator PRICE_BREAKDOWN_RESCHEDULE_BOOKING_FORM = new Locator("PRICE_BREAKDOWN_RESCHEDULE_BOOKING_FORM", AppiumBy.xpath("//*[contains(@resource-id,'tv_price') and ./preceding-sibling::*[contains(@resource-id,'tv_bullet')]]"));
    private static final Locator TOTAL_PRICE_BREAKDOWN_RESCHEDULE_BOOKING_FORM = new Locator("TOTAL_PRICE_BREAKDOWN_RESCHEDULE_BOOKING_FORM", AppiumBy.xpath("(//*[contains(@resource-id,'tv_total')]//following-sibling::*[contains(@resource-id,'tv_price')])[last()]"));
    private static final Locator TOTAL_PRICE_BREAKDOWN_WITHOUT_SUBSTRACTION_RESCHEDULE_BOOKING_FORM = new Locator("TOTAL_PRICE_BREAKDOWN_WITHOUT_SUBSTRACTION_RESCHEDULE_BOOKING_FORM", AppiumBy.xpath("//*[ *[contains(@resource-id,'tv_total')] and following-sibling::*[*[contains(@resource-id,'tv_title')]]]//following-sibling::*[contains(@resource-id,'tv_price')]"));
    private static final Locator TOTAL_PRICE_BREAKDOWN_CURRENT_BALANCE_RESCHEDULE_BOOKING_FORM = new Locator("TOTAL_PRICE_BREAKDOWN_CURRENT_BALANCE_RESCHEDULE_BOOKING_FORM", AppiumBy.xpath("//*[ *[contains(@resource-id,'tv_title')] and preceding-sibling::*[*[contains(@resource-id,'tv_total')]]]//*[contains(@resource-id,'tv_price')]"));
    private static final Locator GOT_REFUND_WORDING = new Locator("GOT_REFUND_WORDING", AppiumBy.id("tv_get_refund"));
    private static final Locator ROOM_PRICE_TITLE = new Locator("ROOM_PRICE_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_left') and contains(@text,' x ')]"));
    public static final Locator GUEST_DETAIL_EDIT_BUTTON = new Locator("GUEST_DETAIL_EDIT_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tv_empty_guest')] | //*[contains(@resource-id,'tv_guest') and not(contains(@resource-id,'_details'))]"));
    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", AppiumBy.id("price_module_final_price"));
    private static final Locator CROSSED_PRICE = new Locator("CROSSED_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'tv_crossed_price') or contains(@resource-id,'tv_total_price_slash_value')]"));
    private static final Locator TOTAL_PRICE_LABEL = new Locator("TOTAL_PRICE_LABEL", AppiumBy.xpath("//*[contains(@resource-id,'tv_total_label') and @text='Total (setelah pajak)' or @text='Total (after taxes)']"));
    private static final Locator PRICE_AFTER_TAXES = new Locator("PRICE_AFTER_TAXES", AppiumBy.id("price_module_description"));
    private static final Locator GUEST_NAME_WARNING_TEXT = new Locator("GUEST_NAME_WARNING_TEXT", AppiumBy.id("tds_box_helper_text"));
    private static final Locator NATIONALITY_GUEST_DETAIL = new Locator("NATIONALITY_GUEST_DETAIL", AppiumBy.id("tds_box_input_text"));
    private static final Locator PRICE_DETAIL_SUB_TOTAL_EXPAND = new Locator("PRICE_DETAIL_SUB_TOTAL_EXPAND", AppiumBy.xpath("//*[contains(@resource-id,'tv_left') and contains(@text,'Subtotal')]//following-sibling::*[contains(@resource-id,'iv_chevron')]"));
    private static final Locator CHECK_MORE_PROMO = new Locator("CHECK_MORE_PROMO",AppiumBy.id("ll_container"));
    private static final Locator PROMO_TEXT_INPUT = new Locator("PROMO_TEXT_INPUT", AppiumBy.id("tds_box_input_text"));
    private static final Locator APPLY_PROMO_BUTTON = new Locator("APPLY_PROMO_BUTTON",AppiumBy.id("tds_button_view"));


    private Locator selectedSalutation() {
        Locator[] salutationList = {MR_SALUTATION_CHECKBOX, MRS_SALUTATION_CHECKBOX, MS_SALUTATION_CHECKBOX};
        return getRandomObject(salutationList);
    }

    private WebElement selectedSalutationV3() {
        driverTools.waitForElementToAppear(TITLE_CHECKBOX_V3).orElseThrow();
        List<WebElement> titleCheckbox = driverTools.elements(TITLE_CHECKBOX_V3).orElseThrow();
        int totalTitle = titleCheckbox.size();
        logger.log("totalTitle: " + totalTitle);
        int randomList = generateRandomInt(0, totalTitle - 1);
        logger.log("randomList: " + randomList);
        logger.log("WebElement: " + titleCheckbox.get(randomList));
        return titleCheckbox.get(randomList);
    }

    @Override
    public String getLocalPriceValue() {
        WebElement localPrice = driverTools.waitForElementToAppear(LOCAL_PRICE_LABEL).orElseThrow();
        return localPrice.getText();
    }

    private String extractEstimatedTotalPrice(String text) {
        String[] markers = {"Estimated total:", "Perkiraan total:"};
        for (String marker : markers) {
            int index = text.indexOf(marker);
            if (index >= 0) {
                String afterMarker = text.substring(index + marker.length()).trim();
                Matcher matcher = PRICE_PATTERN.matcher(afterMarker);
                if (matcher.find()) {
                    return matcher.group().trim();
                }
            }
        }
        throw new AssertionError("Estimated total price not found in text: " + text);
    }

    @Override
    public String detectAndDismissPriceChangeSnackbar() {
        driverTools.waitForElementToAppear(RATE_PLAN_SNACK_BAR_TEXT, 5);
        String snackbarText = driverTools.getText(RATE_PLAN_SNACK_BAR_TEXT).orElseThrow();
        logger.log("Price-change snackbar detected on booking form: " + snackbarText);
        driverTools.waitForElementToPresence(RATE_PLAN_SNACK_BAR_BTN, 3).ifPresent(driverTools::click);
        return snackbarText;
    }

    /**
     * Handles rate plan cancellation when price changes during booking.
     * <p>
     * If the bottom sheet is shown, cancels via the rate change UI and extracts the new (to) price.
     * If a snack bar is shown instead, extracts the new price from the snack bar and dismisses it.
     * Both flows support multiple currencies (IDR, SGD, USD, etc.).
     *
     * @return the new price after the rate change (e.g. "SGD 840,5"), or empty string if not found
     */
    @Override
    public String cancelRatePLanBottomSheetBookingPart1() {
        String toPrice = "";
        driverTools.waitForElementToAppear(RATE_PLAN_BOTTOM_SHEET_BOOKING_PAGE_PART_1, 5);
        driverTools.swipeToElement(RATE_PLAN_BOTTOM_SHEET_BOOKING_PAGE_PART_1, Swipe.UP, 2);
        driverTools.swipeUp();
        if (driverTools.waitForElementToPresence(RATE_PLAN_BOTTOM_SHEET_BOOKING_PAGE_PART_1).isPresent()) {
            driverTools.click(RATE_PLAN_BOTTOM_SHEET_BOOKING_PAGE_PART_1);
            driverTools.waitForElementToPresence(RATE_PLAN_BOTTOM_SHEET, 5);
            if (driverTools.waitForElementToPresence(RATE_PLAN_RATE_CHANGES, 3).isPresent()) {
                String rateChangeText = driverTools.getText(RATE_PLAN_RATE_CHANGES).orElseThrow();
                logger.log("Rate change text: " + rateChangeText);
                List<String> prices = extractPrices(rateChangeText);
                toPrice = prices.size() >= 2 ? prices.get(prices.size() - 1) : "";
            }
            driverTools.waitForElementToPresence(RATE_PLAN_BOTTOM_SHEET, 3).ifPresent(driverTools::click);
        } else if (driverTools.waitForElementToPresence(RATE_PLAN_SNACK_BAR_TEXT).isPresent()) {
            String snackBarText = driverTools.getText(RATE_PLAN_SNACK_BAR_TEXT).orElseThrow();
            logger.log("Snack bar text: " + snackBarText);
            List<String> prices = extractPrices(snackBarText);
            toPrice = prices.size() >= 2 ? prices.get(prices.size() - 1) : "";
            driverTools.click(RATE_PLAN_SNACK_BAR_BTN);
        }
        return toPrice;
    }

    @Override
    public String getLocalPriceOnPriceDetails() {
        WebElement localPrice = driverTools.waitForElementToAppear(LOCAL_PRICE_DETAIL_LABEL).orElseThrow();
        return localPrice.getText();
    }

    @Override
    public void fillContactOrderDetail() {
        String contactName = "";
        String email = "";
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
        contactName = "preprod mailinator";
        email = "promopreprod@mailinator.com";
        }
        else {
            contactName = randomName()+"test";
            email = randomName() + "-aut@tiket.com";
        }
        String phoneNumber = "8123" + randomInt(7);

        if (driverTools.waitForElementToAppear(BOOKING_FORM_V3, 5).isPresent()) {
            WebElement editContactDetailButton = driverTools.swipeToElement(EDIT_CONTACT_DETAIL_BUTTON_V3, Swipe.UP, 5).orElseThrow();
            driverTools.click(editContactDetailButton);
            clearFilledContactDetails();
            WebElement nameContactField = driverTools.waitForElementToAppear(NAME_CONTACT_FIELD_V3).orElseThrow();
            driverTools.sendKeys(nameContactField, contactName);
            if(driverTools.isElementDisplayed(TITLE_CONTACT_BUTTON_V3)){
                driverTools.click(TITLE_CONTACT_BUTTON_V3_DROPDOWN);
                driverTools.click(selectedSalutationV3());
            }
            WebElement phoneNumberField = driverTools.waitForElementToAppear(PHONE_NUMBER_CONTACT_V3).orElseThrow();
            driverTools.sendKeys(phoneNumberField, phoneNumber);
            driverTools.sendKeys(EMAIL_CONTACT_V3, email);
            driverTools.click(SAVE_CONTACT_V3);
        } else {
            driverTools.swipeToElement(EMAIL_INPUT, Swipe.DOWN, 3).orElseThrow();
            driverTools.swipeToElement(EMAIL_INPUT, Swipe.UP, 3).orElseThrow();
            driverTools.swipeToElement(selectedSalutation(), Swipe.DOWN, 1);
            driverTools.click(selectedSalutation());
            driverTools.sendKeys(FULL_NAME_INPUT, contactName);
            driverTools.sendKeysKeyboard(PHONE_NUMBER_INPUT, phoneNumber);
            driverTools.sendKeys(EMAIL_INPUT, email);
            driverTools.hideKeyboardForce();
        }
    }

    @Override
    public void fillContactOrderDetail(String  email) {
        String contactName = randomName()+"test";
        String phoneNumber = "8123" + randomInt(7);

        if (driverTools.waitForElementToAppear(BOOKING_FORM_V3, 5).isPresent()) {
            WebElement editContactDetailButton = driverTools.swipeToElement(EDIT_CONTACT_DETAIL_BUTTON_V3, Swipe.UP, 5).orElseThrow();
            driverTools.click(editContactDetailButton);
            clearFilledContactDetails();
            WebElement nameContactField = driverTools.waitForElementToAppear(NAME_CONTACT_FIELD_V3).orElseThrow();
            driverTools.sendKeys(nameContactField, contactName);
            if(driverTools.isElementDisplayed(TITLE_CONTACT_BUTTON_V3)){
                driverTools.click(TITLE_CONTACT_BUTTON_V3_DROPDOWN);
                driverTools.click(selectedSalutationV3());
            }
            WebElement phoneNumberField = driverTools.waitForElementToAppear(PHONE_NUMBER_CONTACT_V3).orElseThrow();
            driverTools.sendKeys(phoneNumberField, phoneNumber);
            driverTools.sendKeys(EMAIL_CONTACT_V3, email);
            driverTools.click(SAVE_CONTACT_V3);
        } else {
            driverTools.swipeToElement(EMAIL_INPUT, Swipe.UP, 5).orElseThrow();
            driverTools.swipeToElement(selectedSalutation(), Swipe.DOWN, 1);
            driverTools.click(selectedSalutation());
            driverTools.sendKeys(FULL_NAME_INPUT, contactName);
            driverTools.sendKeysKeyboard(PHONE_NUMBER_INPUT, phoneNumber);
            driverTools.sendKeys(EMAIL_INPUT, email);
            driverTools.hideKeyboardForce();
        }
    }

    private void clearFilledContactDetails() {
        if (driverTools.isElementDisplayed(FILLED_NAME_CONTACT_FIELD_V3)) {
            driverTools.clear(FILLED_EMAIL_CONTACT_V3);
            driverTools.clear(FILLED_PHONE_NUMBER_CONTACT_V3);
            driverTools.clear(FILLED_NAME_CONTACT_FIELD_V3);
        }
    }

    private String guestName = "";
    @Override
    public String fillGuestDetail(boolean checkRoomName) {
        String methodName = "fillGuestDetail";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            if (driverTools.waitForElementToAppear(BOOKING_FORM_V3, 5).isPresent()) {
                WebElement switchButton = driverTools.swipeToElement(CONTACT_SWITCH_V3, Swipe.UP, 2).orElseThrow();
                driverTools.click(switchButton);
                WebElement saveButtonGuest = driverTools.waitForElementToAppear(SAVE_BUTTON_GUEST_PASSENGER).orElseThrow();
                driverTools.click(saveButtonGuest);
            } else {
                openGuestDetails(checkRoomName);
                guestName = submitGuestData();
            }

            return guestName;
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    private void openGuestDetails(boolean checkRoomName) {
        driverTools.swipeUp();
        driverTools.swipeToElement(EDIT_GUEST_BUTTON, Swipe.UP, 3);
        if (checkRoomName) replaceRoomName();
        if (driverTools.isElementDisplayed(EDIT_GUEST_BUTTON)) {
            driverTools.click(EDIT_GUEST_BUTTON);
        } else {
            driverTools.hideKeyboard();
            driverTools.click(GUEST_NAME_LIST);
        }
    }

    @Override
    public void openGuestDetailForm(){
        driverTools.swipeToElement(EDIT_GUEST_BUTTON, Swipe.UP, 3);
        if (driverTools.isElementDisplayed(EDIT_GUEST_BUTTON)) {
            driverTools.click(EDIT_GUEST_BUTTON);
        } else {
            driverTools.hideKeyboard();
            driverTools.click(GUEST_NAME_LIST);
        }
    }

    @Override
    public String verifyGuestDetailNameSize(int numberOfNameChar){
        driverTools.clear(getFullNameInputLocator(1));
        String randomName = randomName(numberOfNameChar);
        driverTools.sendKeys(getFullNameInputLocator(1), randomName);
        driverTools.click(SAVE_BUTTON);
        if(numberOfNameChar >=3 && numberOfNameChar <=50 ){
            logger.log("Guest name with "+ numberOfNameChar + " characters is accepted");
            return randomName;
        }
        WebElement warningText = driverTools.waitForElementToAppear(GUEST_NAME_WARNING_TEXT).orElseThrow();
        String warning = driverTools.getText(warningText);
        logger.log("warningText -> " + warning);
        return warning.trim();
    }

    @Override
    public String verifyGuestDetailDefaultNationality(String expectedNationality){
        WebElement nationalityElement = driverTools.waitForElementToAppear(NATIONALITY_GUEST_DETAIL).orElseThrow();
        String nationality = driverTools.getText(nationalityElement);
        logger.log("nationality -> " + nationality);
        return nationality.trim();
    }

    @Override
    public String getNameOnGuestDetail(String expectedName){
        driverTools.swipeToElement(getNameLocatorOnGuestDetail(expectedName),Swipe.UP,2);
        WebElement guestNameElement = driverTools.waitForElementToAppear(getNameLocatorOnGuestDetail(expectedName)).orElseThrow();
        String guestName = CoreFunctions.getNameOnGuestDetail(driverTools.getText(guestNameElement));
        logger.log("guest name -> " + guestName);
        return guestName.trim();
    }

    private Locator getNameLocatorOnGuestDetail(String expectedName){
        return new Locator("GUEST_NAME_GUEST_DETAIL_MO", AppiumBy.xpath("//android.widget.TextView[contains(@text,'"+expectedName+"')]"));
    }

    @Override
    public ArrayList<String> verifyGuestDetailNameSizeMO(int numberOfRoom,String expectedNationality) {
        ArrayList<String> guestNameList = generateGuestName(numberOfRoom, true);

        // Fill guest names (invalid case)
        fillGuestNamesForRooms(numberOfRoom, guestNameList, expectedNationality,numberOfRoom);

        // Check warning
        WebElement warningText = driverTools.waitForElementToAppear(GUEST_NAME_WARNING_TEXT).orElseThrow();
        logger.log("warningText -> " + driverTools.getText(warningText));

        // Fill correct guest names to proceed
        ArrayList<String> correctedGuestNameList = correctTheGuestNameList(guestNameList);
        fillGuestNamesForRooms(numberOfRoom, correctedGuestNameList,expectedNationality,numberOfRoom-1);

        return correctedGuestNameList;
    }

    private void fillGuestNamesForRooms(int numberOfRoom, ArrayList<String> guestNameList ,String expectedNationality,int lastRoom) {
        logger.log("Filling guest names for " + numberOfRoom + " rooms: " + guestNameList + ", expectedNationality: " + expectedNationality + ", lastRoom: " + lastRoom);
        for (int i = 1; i <= lastRoom; i++) {
            Locator roomLocator = roomLocator(i);
            WebElement roomElement = driverTools.waitForElementToAppear(roomLocator).orElseThrow();
            String selectedRoomAttribute = driverTools.getValueAttribute(roomElement, "selected");
            Assertion.assertThat("Room " + i + " is selected", selectedRoomAttribute, is("true"));
            Assertion.assertThat("is default nationality is { " + expectedNationality +" } for room {" + i + " }" ,verifyGuestDetailDefaultNationality("Indonesia"),equalToIgnoringCase(expectedNationality));

            driverTools.clear(getFullNameInputLocator(1));
            driverTools.sendKeys(getFullNameInputLocator(1), guestNameList.get(i - 1));
            logger.log("index " + i + " lastRoom " + lastRoom);
            if (i == lastRoom-1) {
                logger.log("change room to next room: " + (i + 1));
                driverTools.click(roomLocator(i + 1));
            }
        }
        driverTools.click(SAVE_BUTTON);
    }

    private Locator roomLocator(int index) {
        return new Locator("ROOM_NAME_" + index, AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tab_layout') and contains(@text,'"+index+"')]"));
    }

    @Override
    public boolean selectSuggestedNameAndVerifyNationality(String expectedName, String expectedNationality){
        driverTools.clear(getFullNameInputLocator(1));
        driverTools.sendKeysKeyboard(getFullNameInputLocator(1), expectedName.substring(0,3));
        WebElement suggestedName = driverTools.waitForElementToPresence(getGuestNameSuggestionLocator(expectedName)).orElseThrow();
        driverTools.click(suggestedName);

        WebElement selectedNameElement = driverTools.waitForElementToPresence(getFullNameInputLocator(1),5).orElseThrow();
        String actualName = driverTools.getText(selectedNameElement);
        logger.log("actualName -> " + actualName + " expectedName -> " + expectedName);
        Assertion.assertThat("is selected name is { " + expectedName +" }",actualName,equalToIgnoringCase(expectedName));

        String actualNationality = verifyGuestDetailDefaultNationality(expectedNationality);
        logger.log("actualNationality -> " + actualNationality + " expectedNationality -> " + expectedNationality);
        Assertion.assertThat("is default nationality is { " + expectedNationality +" } for room1",actualNationality,equalToIgnoringCase(expectedNationality));
        return expectedNationality.equalsIgnoreCase(actualNationality);
    }

    private Locator getGuestNameSuggestionLocator(String expectedName){
        return new Locator("GUEST_NAME_SUGGESTION", AppiumBy.xpath("//*[contains(@resource-id,'tv_auto_complete') and contains(@text,'"+expectedName+"')]"));
    }


    private void replaceRoomName() {
        if (!selectedRoomName.isEmpty()) {
            logger.log("replaceRoomName selectedRoomName -> " + selectedRoomName);
            if (!selectedRoomName.get(1).equals(getRoomNameBookingForm())) {
                for (int i = 0; i < selectedRoomName.size(); i++) {
                    List<WebElement> roomNameList = driverTools.elements(ROOM_NAME_TEXT).orElseThrow();
                    String roomName = driverTools.getText(roomNameList.get(i));
                    String[] roomNameSplit = roomName.split(" • ");
                    selectedRoomName.replace(i + 1, roomNameSplit[1]);
                }
                logger.log("new selectedRoomName: " + selectedRoomName);
            } else {
                logger.log("selectedRoomName is same with first room name on booking form");
            }
        } else {
            logger.log("selectedRoomName is empty");
        }
    }

    private String submitGuestData() {
        if (driverTools.waitForElementToAppear(ROOM_TYPE_TAB).isPresent()) {
            List<WebElement> roomType = driverTools.elements(ROOM_TYPE_TAB).orElseThrow();
            int totalRoomType = roomType.size();
            logger.log("totalRoomType booking form -> " + totalRoomType);
            for (int i = 0; i < totalRoomType; i++) {
                roomType.get(i).click();
                guestName = editGuestData();
            }
        } else {
            guestName = editGuestData();
        }
        driverTools.click(SAVE_BUTTON);
        return guestName;
    }

    private String editGuestData() {
        driverTools.waitForElementToAppear(ROOM_GUEST_INFO);
        List<WebElement> rooms = driverTools.elements(ROOM_GUEST_INFO).orElseThrow();
        int roomCount = rooms.size();
        logger.log("There is '" + (roomCount) + "' guest info should be filled");
        driverTools.waitForElementToAppear(FULL_NAME_INPUT);

        for (int index = 0; index < roomCount; index++) {
            guestName = randomName();

            if (driverTools.isElementDisplayed(MR_SALUTATION_CHECKBOX)
                    || driverTools.isElementDisplayed(MRS_SALUTATION_CHECKBOX)
                    || driverTools.isElementDisplayed(MS_SALUTATION_CHECKBOX))
                driverTools.click(getSalutationRadioLocator(index, generateRandomInt(0, 2)));
            driverTools.swipeToElement(getFullNameInputLocator(index+1), Swipe.UP, 3).orElseThrow();
            driverTools.sendKeys(getFullNameInputLocator(index+1), guestName + " " + randomName());
        }
        return guestName;
    }

    @Override
    public List<String> getGuestNameList() {
        driverTools.swipeDown(4);
        driverTools.swipeUp(1);
        driverTools.waitForElementToAppear(GUEST_NAME_LIST);
        List<WebElement> guestNameElements = driverTools.elements(GUEST_NAME_LIST).orElseThrow();
        logger.log("totalGuest booking form -> " + guestNameElements.size());
        List<String> guestNameList = new ArrayList<>();
        for (WebElement guestNameElement : guestNameElements) {
            String guestTitle = driverTools.getText(guestNameElement);
            String normalizeGuestName = guestTitle.replace(guestTitle, convertTitle(guestTitle, "en"));
            guestNameList.add(normalizeGuestName);
        }
        logger.log("guestNameList-> " + guestNameList);
        return guestNameList;
    }


    private Locator getFullNameInputLocator(int index) {
        return new Locator("FULL_NAME_INPUT_" + index, AppiumBy.xpath("(//*[contains(@resource-id,'view_smart_contact')]//*[contains(@resource-id,'tds_box_autocomplete_input_text')])[" + index + "]"));
    }

    private WebElement getSalutationRadioLocator(int index, int generateRandomInt) {
        switch (generateRandomInt) {
            case 0:
                return driverTools.elements(MR_SALUTATION_CHECKBOX).orElseThrow().get(index);
            case 1:
                return driverTools.elements(MRS_SALUTATION_CHECKBOX).orElseThrow().get(index);
            case 2:
                return driverTools.elements(MS_SALUTATION_CHECKBOX).orElseThrow().get(index);
            default:
                throw new NoSuchMethodError("There is no '" + generateRandomInt + "' case");
        }
    }

    @Override
    public void submitRescheduleBookingForm() {
        driverTools.waitForElementToAppear(LOADING_ICON);
        driverTools.waitForElementToDisappear(LOADING_ICON);

        WebElement submitBookingButton = driverTools.swipeToElement(SUBMIT_RESCHEDULE_BOOK_BUTTON, Swipe.UP, 5).orElseThrow();
        driverTools.swipeUp();
        WebElement agreeCheckList = driverTools.waitForElementToBeClickable(AGREE_TNC_CHECK).orElseThrow();
        driverTools.click(agreeCheckList);
        driverTools.click(submitBookingButton);
        driverTools.click(PROPOSE_RESCHEDULE_BUTTON);

        driverTools.waitForElementToAppear(LOADING_ICON);
        driverTools.waitForElementToDisappear(LOADING_ICON);
    }

    @Override
    public String getHotelNameBookingForm() {
        WebElement hotelNameText;
        if (driverTools.waitForElementToAppear(BOOKING_FORM_V3, 5).isPresent()) {
            hotelNameText = driverTools.element(HOTEL_NAME_TEXT_V3).orElseThrow();
        } else {
            driverTools.swipeToElement(HOTEL_NAME_TEXT,Swipe.UP,4);
            driverTools.swipeToElement(HOTEL_NAME_TEXT,Swipe.DOWN,2);
            hotelNameText = driverTools.waitForElementToPresence(HOTEL_NAME_TEXT).orElseThrow();
        }
        String hotelName = driverTools.getText(hotelNameText);
        logger.log("Hotel Name Booking Form: " + hotelName);
        return hotelName;
    }

    @Override
    public String getHotelRoomNameBookingForm() {
        WebElement hotelRoomNameText;
        if (driverTools.isElementDisplayed(BOOKING_FORM_V3)) {
            hotelRoomNameText = driverTools.element(HOTEL_ROOM_NAME_TEXT_V3).orElseThrow();
        } else {
            hotelRoomNameText = driverTools.swipeToElement(ROOM_NAME, Swipe.UP, 5).orElseThrow();
        }
        String hotelRoomName = driverTools.getText(hotelRoomNameText);
        logger.log("Hotel Room Name Booking Form: " + hotelRoomName);
        return hotelRoomName;
    }

    @Override
    public boolean checkChildGuest() {
        driverTools.waitForElementToAppear(CHILD_GUEST_COUNT);
        driverTools.swipeToElement(CHILD_GUEST_COUNT, Swipe.UP, 3);
        return driverTools.isElementDisplayed(CHILD_GUEST_COUNT);
    }

    @Override
    public boolean checkChildMeal() {
        driverTools.waitForElementToAppear(CHILD_MEAL_COUNT);
        driverTools.swipeToElement(CHILD_MEAL_COUNT, Swipe.UP, 3);
        return driverTools.isElementDisplayed(CHILD_MEAL_COUNT);
    }

    @Override
    public String getHotelRoomCountBookingForm() {
        WebElement hotelRoomCountText;
        String hotelRoomCount;
        if (driverTools.isElementDisplayed(BOOKING_FORM_V3)) {
            hotelRoomCountText = driverTools.element(HOTEL_ROOM_NAME_TEXT_V3).orElseThrow();
            hotelRoomCount = driverTools.getText(hotelRoomCountText);
        } else {
            hotelRoomCountText = driverTools.swipeToElement(NIGHT_DURATION_TEXT, Swipe.UP, 5).orElseThrow();
            hotelRoomCount = driverTools.getText(hotelRoomCountText);
            String[] roomCountSplit = hotelRoomCount.split(" • ");
            hotelRoomCount = roomCountSplit[0].replaceAll("[^0-9]", "");
            logger.log("Hotel Room Count Booking Form: " + hotelRoomCount);
        }
        return hotelRoomCount;
    }

    @Override
    public String getCheckinBookingForm() {
        WebElement checkInDateElement;
        if (driverTools.isElementDisplayed(BOOKING_FORM_V3)) {
            checkInDateElement = driverTools.element(CHECKIN_TEXT_V3).orElseThrow();
        } else {
            checkInDateElement = driverTools.swipeToElement(CHECKIN_TEXT, Swipe.UP, 5).orElseThrow();
        }
        String[] dateRawArray = driverTools.getText(checkInDateElement).split(" ");
        if (dateRawArray[1].length() == 1) {
            dateRawArray[1] = "0" + dateRawArray[1];
        }
        String checkInMonth = dateRawArray[2];
        if (checkInMonth.length() > 3) {
            dateRawArray[2] = checkInMonth.substring(0, 3);
        }
        String checkInDate = String.join(" ", dateRawArray);
        String[] dateSplit = checkInDate.split(" - ");
        String checkIn = dateSplit[0];
        String[] checkInSplit = checkIn.split(" ");
        String checkInYearOld = checkInSplit[3];
        String checkInYear = convertDateFormat(checkInYearOld, "yy", "yyyy");
        checkInDate =  removeLeadingZeroFromDate(checkInSplit[0] + " " + checkInSplit[1] + " " + checkInSplit[2] + " " + checkInYear);
        logger.log("checkInDate -> " + checkInDate);
        return checkInDate;
    }

    @Override
    public String getDurationBookingForm() {
        WebElement nightDurationElement;
        String nightDuration;
        if (driverTools.isElementDisplayed(BOOKING_FORM_V3)) {
            nightDurationElement = driverTools.element(NIGHT_DURATION_TEXT_V3).orElseThrow();
            String[] durationSplit = driverTools.getText(nightDurationElement).split(" ");
            nightDuration = durationSplit[0];
        } else {
            nightDurationElement = driverTools.waitForElementToAppear(NIGHT_DURATION_TEXT).orElseThrow();
            nightDuration = driverTools.getText(nightDurationElement);
            String[] nightDurationSplit = nightDuration.split(" • ");
            nightDuration = nightDurationSplit[0].replaceAll("[^0-9]", "");
            logger.log("night Duration-> " + nightDuration);
        }
        logger.log(nightDuration);
        return nightDuration;
    }

    @Override
    public String getTotalPriceBookingForm() {
        if(driverTools.waitForElementToPresence(TOTAL_PRICE_TEXT).isEmpty()){
            driverTools.swipeToElement(CONTACT_DETAIL_NAME,Swipe.DOWN,2);
            driverTools.waitForElementToPresence(CONTACT_DETAIL_NAME);
            driverTools.swipeToElement(TOTAL_PRICE_TEXT, Swipe.UP, 5);
        }
        WebElement totalPriceElement = driverTools.waitForElementToAppear(TOTAL_PRICE_TEXT).orElseThrow();
        String totalPrice = driverTools.getText(totalPriceElement);
        logger.log("total price booking form : " + totalPrice);
        return totalPrice;
    }

    @Override
    public String getTotalPriceBeforeCashback() {
        WebElement totalPriceBeforeCashback = driverTools.waitForElementToAppear(PRICE_SUMMARY_CASHBACK).orElseThrow();
        String text = driverTools.getText(totalPriceBeforeCashback);
        List<String> prices = extractPrices(text);
        if (prices.isEmpty()) {
            throw new AssertionError("Cashback price not found in text: " + text);
        }
        String price = prices.get(0);
        logger.log("total price before cashback : " + price);
        return price;
    }

    @Override
    public String getPayNowPriceBookingForm() {
        WebElement payNowElement = driverTools.waitForElementToAppear(PAY_NOW_PRICE)
                .or(() -> driverTools.swipeToElement(PAY_NOW_PRICE, Swipe.UP, 5))
                .orElseThrow();
        String payNowPrice = driverTools.getText(payNowElement);
        logger.log("pay now price on booking form -> " + payNowPrice);
        return payNowPrice;
    }

    @Override
    public String getPayUponCheckInPriceBookingForm() {
        WebElement payUponCheckInElement = driverTools.waitForElementToAppear(PAY_UPON_CHECK_IN_PRICE)
                .or(() -> driverTools.swipeToElement(PAY_UPON_CHECK_IN_PRICE, Swipe.UP, 5))
                .orElseThrow();
        String payUponCheckInPrice = driverTools.getText(payUponCheckInElement);
        logger.log("pay upon check-in price on booking form -> " + payUponCheckInPrice);
        return payUponCheckInPrice;
    }

    @Override
    public String getEstimatedTotalFromBookingFormPage1() {
        WebElement disclaimerElement = driverTools.waitForElementToAppear(PROPERTY_FEE_DISCLAIMER)
                .or(() -> driverTools.swipeToElement(PROPERTY_FEE_DISCLAIMER, Swipe.UP, 5))
                .orElseThrow();
        String disclaimerText = driverTools.getText(disclaimerElement);
        logger.log("property fee disclaimer text -> " + disclaimerText);
        String estimatedTotal = extractEstimatedTotalPrice(disclaimerText);
        logger.log("estimated total from booking form page 1 -> " + estimatedTotal);
        return estimatedTotal;
    }

    @Override
    public String getTotalPriceBookingFormPage2() {
        WebElement totalPriceElement = driverTools.waitForElementToAppear(TOTAL_PRICE_PRICE_BREAKDOWN)
                .or(() -> driverTools.swipeToElement(TOTAL_PRICE_PRICE_BREAKDOWN, Swipe.UP, 5))
                .orElseThrow();
        String totalPrice = driverTools.getText(totalPriceElement);
        logger.log("total price on booking form page 2 -> " + totalPrice);
        return totalPrice;
    }

    @Override
    public String getPayUponCheckInPriceBookingFormPage2() {
        WebElement payUponCheckInElement = driverTools.waitForElementToAppear(BOOKING_FORM_PAGE2_PAY_UPON_CHECK_IN)
                .or(() -> driverTools.swipeToElement(BOOKING_FORM_PAGE2_PAY_UPON_CHECK_IN, Swipe.UP, 5))
                .orElseThrow();
        String payUponCheckInPrice = driverTools.getText(payUponCheckInElement);
        logger.log("pay upon check-in price on booking form page 2 -> " + payUponCheckInPrice);
        return payUponCheckInPrice;
    }

    @Override
    public String getEstimatedTotalFromBookingFormPage2() {
        WebElement estimatedTotalElement = driverTools.waitForElementToAppear(BOOKING_FORM_PAGE2_ESTIMATED_TOTAL)
                .or(() -> driverTools.swipeToElement(BOOKING_FORM_PAGE2_ESTIMATED_TOTAL, Swipe.UP, 5))
                .orElseThrow();
        String estimatedTotalText = driverTools.getText(estimatedTotalElement);
        logger.log("estimated total text on booking form page 2 -> " + estimatedTotalText);
        String estimatedTotal = extractEstimatedTotalPrice(estimatedTotalText);
        logger.log("estimated total from booking form page 2 -> " + estimatedTotal);
        return estimatedTotal;
    }


    public void verifyEstimatedTotalUnchangedAfterInsuranceOrAddOns(String estimatedTotalBeforeInsurance) {
        String estimatedTotalAfter = getEstimatedTotalFromBookingFormPage2();
        logger.log("estimated total before insurance -> " + estimatedTotalBeforeInsurance);
        logger.log("estimated total after insurance -> " + estimatedTotalAfter);
        assertPriceEquals("Estimated total unchanged after adding insurance on booking form page 2",
                estimatedTotalAfter, estimatedTotalBeforeInsurance);
    }

    @Override
    public void addInsuranceOnBookingFormPage2(String estimatedTotal) {
        if(driverTools.isElementDisplayed(INSURANCE_ADD_BUTTON)){
        WebElement addInsuranceButton = driverTools.waitForElementToAppear(INSURANCE_ADD_BUTTON)

                .or(() -> driverTools.swipeToElement(INSURANCE_ADD_BUTTON, Swipe.UP, 5))
                .orElseThrow();
        driverTools.click(addInsuranceButton);
        verifyEstimatedTotalUnchangedAfterInsuranceOrAddOns(estimatedTotal);
        }
    }

    @Override
    public String getTotalToPayBookingForm() {
        StringBuilder log = new StringBuilder("[Booking form] ");
        WebElement totalToPayElement = driverTools.swipeToElement(TOTAL_TO_PAY, Swipe.UP, 5).orElseThrow();
        String total = driverTools.getText(totalToPayElement);
        log.append("Reading total to pay from price summary: ").append(total);
        logger.log(log.toString());
        return total;
    }

    @Override
    public String getTotalRefundPriceBookingForm() {
        driverTools.waitForElementToAppear(GET_REFUND_TEXT);
        driverTools.swipeToElement(GET_REFUND_TEXT, Swipe.UP, 5);
        String getRefundEstimation = driverTools.getValueAttribute(GET_REFUND_TEXT, "text")
                .replace("Get refund ", "")
                .replace("Dapat refund ", "");
        logger.log("getRefundEstimation-> " + getRefundEstimation);
        return getRefundEstimation;
    }

    @Override
    public String getTotalPriceInPriceDetail() {
        WebElement totalPrice = driverTools.swipeToElement(TOTAL_PRICE_PRICE_BREAKDOWN, PRICE_SUMMARY_CONTAINER, Swipe.UP, 5).orElseThrow();
        return driverTools.getText(totalPrice);
    }

    @Override
    public String getContactNameDetail() {
        driverTools.swipeToElement(CONTACT_DETAIL_NAME , Swipe.DOWN, 2);
        driverTools.waitForElementToPresence(CONTACT_DETAIL_NAME);
        return driverTools.getText(CONTACT_DETAIL_NAME).orElseThrow();
    }

    @Override
    public String getContactPhoneNumberDetail() {
        return driverTools.getText(CONTACT_DETAIL_PHONENUMBER).orElseThrow();
    }

    @Override
    public String getGuestNameDetail() {
        driverTools.swipeToElement(GUEST_DETAIL_NAME , Swipe.UP, 5);
        return driverTools.getText(GUEST_DETAIL_NAME).orElseThrow();
    }


    @Override
    public void continueToPaymentPage() {
        String methodName = "continueToPaymentPage";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            if (driverTools.waitForElementToAppear(BOOKING_FORM_V3, 5).isPresent()) {
                WebElement continuePayment = driverTools.swipeToElement(SUBMIT_RESCHEDULE_BOOK_BUTTON, Swipe.UP, 5).orElseThrow();
                driverTools.click(continuePayment);
            } else {
                driverTools.waitForElementToAppear(CONTINUE_BUTTON, 10).ifPresent(driverTools::click);
                if (driverTools.isElementDisplayed(CONTINUE_BUTTON)) driverTools.clickElementByCoordinate(CONTINUE_BUTTON);
                clickPayNowButton();
                driverTools.swipeToElement(SELECT_PAYMENT_BUTTON,Swipe.UP,6).orElseThrow();
                driverTools.click(SELECT_PAYMENT_BUTTON);
            }
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public boolean isBookingFormPart2Loaded() {
        return driverTools.swipeToElement(PRICE_SUMMARY_CONTAINER, Swipe.UP, 5).isPresent();
    }

    private void clickPayNowButton() {
        driverTools.swipeToElement(PAY_NOW_BUTTON, Swipe.UP, 5);
        driverTools.waitForElementToAppear(PAY_NOW_BUTTON, 10).ifPresent(driverTools::click);
    }

    @Override
    public void continueToAddOnPage() {
        driverTools.click(CONTINUE_BUTTON);
    }

    @Override
    public void submitBooking() {
        driverTools.click(SELECT_PAYMENT_BUTTON);
    }

    @Override
    public void chooseProtection(String protectionType) {
        if (driverTools.waitForElementToAppear(EXTRA_PROTECTION_TAB_SECTION).isEmpty()) driverTools.swipeUp();
        driverTools.swipeToElement(EXTRA_PROTECTION_TAB_SECTION, Swipe.UP, 3).orElseThrow();
        if (driverTools.waitForElementToAppear(EXTRA_PROTECTION_CANCEL).isEmpty()) driverTools.swipeUp();
        if (driverTools.isElementDisplayed(EXTRA_PROTECTION_CANCEL)) driverTools.click(EXTRA_PROTECTION_CANCEL);
        if (protectionType.equals("premium")) {
            driverTools.swipeToElement(EXTRA_PROTECTION_PREMIUM_TAB, Swipe.DOWN, 2).orElseThrow();
            driverTools.click(EXTRA_PROTECTION_PREMIUM_TAB);
        } else if (protectionType.equals("standard")) {
            driverTools.swipeToElement(EXTRA_PROTECTION_STANDARD_TAB, Swipe.DOWN, 2).orElseThrow();
            driverTools.click(EXTRA_PROTECTION_STANDARD_TAB);
        } else {
            logger.fail("wrong protectionType ");
        }
    }

    @Override
    public void addProtection() {
        driverTools.swipeToElement(EXTRA_PROTECTION_ADD_BUTTON, Swipe.UP, 2).orElseThrow();
        driverTools.click(EXTRA_PROTECTION_ADD_BUTTON);
    }

    @Override
    public boolean isAddonThereOnBookingForm() {
        return driverTools.isElementDisplayed(EXTRA_PROTECTION_ADD_BUTTON);
    }

    @Override
    public void openPriceDetail() {
        driverTools.waitForElementToDisappear(SNACKBAR_TEXT);
        driverTools.click(TOTAL_PRICE_DETAIL_ARROW_BUTTON);
    }

    @Override
    public void openSubtotal() {
        StringBuilder log = new StringBuilder("[Booking form] ");
        log.append("Opening price breakdown by clicking Subtotal.");
        logger.log(log.toString());
        driverTools.click(SUBTOTAL_BUTTON);
    }

    @Override
    public boolean isProtectionAdded(String protectionPrice) {
        driverTools.isElementDisplayed(EXTRA_PROTECTION_ON_TOTAL_PRICE_DETAIL);
        String priceOnPriceDetail = driverTools.getText(EXTRA_PROTECTION_ON_TOTAL_PRICE_DETAIL).orElseThrow();
        Assertion.assertThat("Protection price on price detail is same as selected protection price",
                priceOnPriceDetail, is(protectionPrice));
        return priceOnPriceDetail.equals(protectionPrice);
    }

    @Override
    public String getProtectionPrice() {
        driverTools.swipeToElement(EXTRA_PROTECTION_PRICE, Swipe.UP, 3).orElseThrow();
        return driverTools.getText(EXTRA_PROTECTION_PRICE).orElseThrow();
    }

    @Override
    public void clickBackButton() {
        driverTools.click(BACK_BUTTON);
    }

    @Override
    public void clickCancelButton() {
        driverTools.click(CANCEL_BUTTON);
    }

    @Override
    public void clickSearchOtherRoom() {
        driverTools.click(SEARCH_OTHER_ROOM_BUTTON);
    }

    @Override
    public void editContactOrderDetail() {
        driverTools.click(EDIT_CONTACT_BUTTON);
        driverTools.click(selectedSalutation());
        driverTools.sendKeys(FULL_NAME_INPUT, randomName());
        driverTools.clearInput(PHONE_NUMBER_INPUT);
        driverTools.sendKeysKeyboard(PHONE_NUMBER_INPUT, "8123" + randomInt(6));
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.click(SAVE_BUTTON);
    }

    @Override
    public void selectRandomSalutationIfMissing() {
        String contactName = getContactNameDetail();
        if (!SALUTATION_PATTERN.matcher(contactName).find()) {
            logger.log("Contact name '" + contactName + "' has no salutation, selecting a random one");
            driverTools.click(CONTACT_DETAIL_NAME);
            driverTools.click(selectedSalutation());
            driverTools.waitForElementToAppear(SAVE_BUTTON);
            driverTools.click(SAVE_BUTTON);
        }
    }

    @Override
    public boolean isContactAndGuestDetailsFilled() {
        driverTools.waitForElementToAppear(CONTACT_DETAIL_NAME);
        driverTools.swipeToElement(CONTACT_DETAIL_NAME, Swipe.DOWN, 2);
        driverTools.waitForElementToPresence(CONTACT_DETAIL_NAME);
        boolean isContactDetailNameFilled = driverTools.isElementDisplayed(CONTACT_DETAIL_NAME);
        Assertion.assertThat("Assert if contact detail name is displayed and filled", isContactDetailNameFilled, is(true));
        logger.log("Contact Detail Name = '" + driverTools.getText(CONTACT_DETAIL_NAME) + "'");

        boolean isContactDetailEmailFilled = driverTools.isElementDisplayed(CONTACT_DETAIL_EMAIL);
        Assertion.assertThat("Assert if contact detail email is displayed and filled", isContactDetailEmailFilled, is(true));
        logger.log("Contact Detail Email = '" + driverTools.getText(CONTACT_DETAIL_EMAIL) + "'");

        boolean isContactDetailPhoneNumberFilled = driverTools.isElementDisplayed(CONTACT_DETAIL_PHONENUMBER);
        Assertion.assertThat("Assert if contact detail phone number is displayed and filled", isContactDetailPhoneNumberFilled, is(true));
        logger.log("Contact Detail Phone Number = '" + driverTools.getText(CONTACT_DETAIL_PHONENUMBER) + "'");

        driverTools.swipeToElement(GUEST_DETAIL_NAME, Swipe.UP, 5);
        boolean isGuestDetailNameFilled = driverTools.isElementDisplayed(GUEST_DETAIL_NAME);
        Assertion.assertThat("Assert if guest detail name is displayed and filled", isGuestDetailNameFilled, is(true));
        logger.log("Guest Detail Name = '" + driverTools.getText(GUEST_DETAIL_NAME) + "'");
        return true;
    }

    @Override
    public boolean isContactAndGuestDetailsChanged(String contactName, String contactPhoneNumber, String guestName) {
        driverTools.swipeDown(2);
        boolean isContactDetailNameChanged = !getContactNameDetail().equalsIgnoreCase(contactName);
        logger.log("Contact name | new data '" + getContactNameDetail() + "', old data '" + contactName + "'");
        Assertion.assertThat("Assert if contact detail name is changed", isContactDetailNameChanged, is(true));

        boolean isContactDetailPhoneNumberChanged = !getContactPhoneNumberDetail().equalsIgnoreCase(contactPhoneNumber);
        logger.log("Contact phone number | new data '" + getContactPhoneNumberDetail() + "', old data '" + contactPhoneNumber + "'");
        Assertion.assertThat("Assert if contact detail phone number is changed", isContactDetailPhoneNumberChanged, is(true));

        boolean isGuestNameChanged = !getGuestNameDetail().equalsIgnoreCase(guestName);
        logger.log("Guest name | new data '" + getGuestNameDetail() + "', old data '" + guestName + "'");
        Assertion.assertThat("Assert if guest detail name is changed", isGuestNameChanged, is(true));

        return true;
    }

    @Override
    public String priceChangePopUpText() {
        WebElement priceChangePopup = driverTools.waitForElementToAppear(PRICE_CHANGE_POPUP).orElseThrow();
        return driverTools.getText(priceChangePopup);
    }

    @Override
    public void clickPriceChangeGoPayment() {
        driverTools.click(PRICE_CHANGE_GO_PAYMENT);
    }

    @Override
    public String PriceChangesPopUp() {
        String methodName = "PriceChangesPopUp";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            String priceChangeDesc = "";
            boolean priceChangeUp = false;
            String language = "id";

            if (driverTools.waitForElementToAppear(PRICE_CHANGE_POPUP).isPresent()) {
                String priceChangeType = driverTools.getText(PRICE_CHANGE_POPUP).orElseThrow();
                if (priceChangeType.contains("updated") || priceChangeType.contains("diperbarui")) priceChangeUp = true;
                logger.log("priceChangeUp -> " + priceChangeUp);

                priceChangeDesc = driverTools.getText(PRICE_CHANGE_DESCRIPTION).orElseThrow();
                if (priceChangeDesc.contains(" to ")) language = "en";

                if (priceChangeUp && language.equals("id")) {
                    String[] priceChangeSplit = priceChangeDesc.split(" jadi ");
                    logger.log("priceChangeSplit -> " + Arrays.toString(priceChangeSplit));
                    priceChangeDesc = priceChangeSplit[1];
                } else if (priceChangeUp) {
                    String[] priceChangeSplit = priceChangeDesc.split(" to ");
                    logger.log("priceChangeSplit -> " + Arrays.toString(priceChangeSplit));
                    priceChangeDesc = priceChangeSplit[1];
                } else if (language.equals("id")) {
                    String[] priceChangeSplit = priceChangeDesc.split(" jadi ");
                    logger.log("priceChangeSplit -> " + Arrays.toString(priceChangeSplit));
                    priceChangeDesc = priceChangeSplit[1];
                } else {
                    String[] priceChangeSplit = priceChangeDesc.split(" instead of ");
                    logger.log("priceChangeSplit -> " + Arrays.toString(priceChangeSplit));
                    priceChangeDesc = priceChangeSplit[0];
                }
                String removeText = priceChangeDesc.replaceAll("[^0-9-]","");
                String currency = priceChangeDesc.split(" ")[0];
                priceChangeDesc = String.join(" ", currency, thousandSeparator(removeText, "."));

                logger.log("priceChanges -> " + priceChangeDesc);
                driverTools.click(PRICE_CHANGE_GO_PAYMENT);
            }
            if (driverTools.waitForElementToAppear(POLICY_CHANGE_POPUP).isPresent()) {
                String policyChangeDesc = driverTools.getText(PRICE_CHANGE_DESCRIPTION).orElseThrow();
                logger.log("policyChangeDesc -> " + policyChangeDesc);
                driverTools.click(PRICE_CHANGE_GO_PAYMENT);
            }
            return priceChangeDesc;
        } finally {
            this.closeRatePlanBottomSheet();
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void closeRatePlanBottomSheet(){
        String methodName = "closeRatePlanBottomSheet";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            if(driverTools.waitForElementToPresence(PaymentPageAndroid.ORDER_ID,3).isPresent()){
                return;
            }
            driverTools.waitForElementToPresence(RATE_PLAN_BOTTOM_SHEET).ifPresent(driverTools::click);
        }finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public String getRoomNameBookingForm() {
        driverTools.swipeToElement(ROOM_NAME_TEXT, Swipe.UP, 5).orElseThrow();
        String roomName = driverTools.getText(ROOM_NAME_TEXT).orElseThrow();
        if (roomName.contains(" • ")) {
            roomName = roomName.split(" • ")[1];
        }
        logger.log("Room Name Booking Form: " + roomName);
        return roomName;
    }

    @Override
    public String getCheckInDateBookingForm() {
        String checkInDate = driverTools.getText(CHECKIN_TEXT).orElseThrow();
        logger.log("Check In Date Booking Form: " + checkInDate);
        return checkInDate;
    }

    @Override
    public String getCheckOutDateBookingForm() {
        WebElement checkInDateElement = driverTools.waitForElementToAppear(CHECKIN_TEXT).orElseThrow();
        String[] dateRawArray = driverTools.getText(checkInDateElement).split(" ");
        if (dateRawArray[6].length() == 1) {
            dateRawArray[6] = "0" + dateRawArray[6];
        }
        String checkInMonth = dateRawArray[7];
        if (checkInMonth.length() > 3) {
            dateRawArray[7] = checkInMonth.substring(0, 3);
        }
        String checkOutDate = String.join(" ", dateRawArray);
        String[] dateSplit = checkOutDate.split(" - ");
        String checkOut = dateSplit[1];
        String[] checkOutSplit = checkOut.split(" ");
        logger.log("checkOutSplit -> " + Arrays.toString(checkOutSplit));
        String checkOutMonth = checkOutSplit[2];
        String checkOutYearOld = checkOutSplit[3];
        String checkOutYear = convertDateFormat(checkOutYearOld, "yy", "yyyy");
        checkOutDate = checkOut.replace(checkOutMonth + " " + checkOutYearOld, checkOutMonth + " " + checkOutYear);
        logger.log("checkOutDate -> " + checkOutDate);
        return checkOutDate;
    }

    @Override
    public String getCheckInTimeBookingForm() {
        String checkInTime = driverTools.getText(CHECKIN_TIME_TEXT).orElseThrow();
        logger.log("Check In Time Booking Form: " + checkInTime);
        return checkInTime;
    }

    @Override
    public boolean isPriceDetailCorrect(String totalPayment) {
        driverTools.swipeToElement(TOTAL_PRICE_PRICE_BREAKDOWN, PRICE_SUMMARY_CONTAINER, Swipe.UP, 1);
        driverTools.waitForElementToAppear(PRICE_BREAKDOWN, 10);

        // Build display-style log (as shown on screen, any currency)
        StringBuilder log = new StringBuilder("PRICE SUMMARY (as displayed):");
        List<WebElement> allPriceRights = driverTools.elements(PRICE_SUMMARY_ALL_RIGHTS).orElse(List.of());
        for (WebElement rightEl : allPriceRights) {
            String value = "";
            try {
                value = driverTools.getText(rightEl);
                if (value != null) value = value.trim();
                else value = "";
            } catch (Exception ignored) {
            }
            String label = "";
            try {
                WebElement leftEl = rightEl.findElement(By.xpath("./preceding-sibling::*[contains(@resource-id,'tv_left')]"));
                String l = driverTools.getText(leftEl);
                label = l != null ? l.trim() : "";
            } catch (Exception ignored) {
            }
            log.append("\n  ").append(label.isEmpty() ? "(item)" : label).append(": ").append(value);
        }
        try {
            WebElement payNowLabelEl = driverTools.waitForElementToAppear(TOTAL_PRICE_LABEL_BREAKDOWN).orElse(null);
            WebElement payNowValueEl = driverTools.waitForElementToAppear(TOTAL_PRICE_PRICE_BREAKDOWN).orElse(null);
            if (payNowLabelEl != null && payNowValueEl != null) {
                String payNowLabel = driverTools.getText(payNowLabelEl);
                String payNowValue = driverTools.getText(payNowValueEl);
                log.append("\n  ").append(payNowLabel != null ? payNowLabel.trim() : "").append(": ").append(payNowValue != null ? payNowValue.trim() : "");
            }
        } catch (Exception ignored) {
        }
        logger.log(log.toString());

        // Sum and assertions (unchanged): use PRICE_BREAKDOWN (excludes Pay at check-in row)
        List<WebElement> prices = driverTools.elements(PRICE_BREAKDOWN).orElseThrow();
        ArrayList<Double> arrayPrice = new ArrayList<>();
        for (WebElement element : prices) {
            String text = driverTools.getText(element);
            if (text == null) text = "";
            try {
                double price = convertPrice(text);
                arrayPrice.add(price);
            } catch (NumberFormatException message) {
                logger.log(message.getMessage());
            }
        }

        double sumLineItems = 0;
        for (double p : arrayPrice) {
            sumLineItems += p;
        }

        WebElement totalPriceElement = driverTools.waitForElementToAppear(TOTAL_PRICE_PRICE_BREAKDOWN).orElseThrow();
        String totalPriceText = driverTools.getText(totalPriceElement);
        if (totalPriceText == null) totalPriceText = "";
        double totalPricePriceDetail = convertPriceSafely(totalPriceText);
        double totalPriceFromBookingPage = convertPriceSafely(totalPayment);

        logger.log("[Price detail verification] Breakdown total (bottom)=" + totalPricePriceDetail + ", booking form total=" + totalPriceFromBookingPage + ", sum(line items)=" + sumLineItems);

        // Assert: total on booking form matches total in price breakdown
        Assertion.assertThat("Total on booking form should match total in price breakdown", totalPriceFromBookingPage, is(closeTo(totalPricePriceDetail, 0.01)));
        // When breakdown has discounts (negative line items), sum of line items may not equal final total
        boolean hasDiscount = arrayPrice.stream().anyMatch(p -> p < 0);
        if (!hasDiscount) {
            Assertion.assertThat("Sum of breakdown line items should match breakdown total", totalPricePriceDetail, is(closeTo(sumLineItems, 0.01)));
        } else {
            logger.log("[Price detail verification] Breakdown contains discount(s); sum of line items (" + sumLineItems + ") may differ from final total (" + totalPricePriceDetail + ").");
        }
        return true;
    }

    @Override
    public void closePriceDetail() {
        driverTools.click(PRICE_DETAIL_CLOSE_BTN);
    }

    @Override
    public String getRoomGuestBookingForm() {
        String roomGuestText = driverTools.getText(ROOM_GUEST_TEXT).orElseThrow();
        String[] roomGuestArray = roomGuestText.split(" ");
        String roomGuest = roomGuestArray[1];
        logger.log("Room Guest Booking Form: " + roomGuest);
        return roomGuest;
    }

    @Override
    public String getRoomBedBookingForm() {
        driverTools.swipeToElement(ROOM_BED_TEXT, Swipe.UP, 2);
        String roomBed = driverTools.getText(ROOM_BED_TEXT).orElseThrow();
        logger.log("Room Bed Booking Form: " + roomBed);
        return roomBed;
    }

    @Override
    public String getCancellationBookingForm() {
        driverTools.swipeToElement(CANCELLATION_TEXT, Swipe.UP, 2);
        String cancellation = driverTools.getText(CANCELLATION_TEXT).orElseThrow();
        logger.log("Room Cancellation Booking Form: " + cancellation);
        return cancellation;
    }

    @Override
    public void openBookingDetailPage() {
        driverTools.swipeToElement(ROOM_DETAIL_BUTTON,Swipe.UP,3);
        driverTools.click(ROOM_DETAIL_BUTTON);
    }

    @Override
    public boolean isBookingFormShown(boolean loginStatus) {
        if(loginStatus) {
            WebElement bookingSummaryContainer = driverTools.swipeToElement(BOOKING_SUMMARY_CONTAINER_LOGIN, Swipe.DOWN, 5).orElseThrow();
            return driverTools.isElementDisplayed(bookingSummaryContainer);
        }
        WebElement bookingSummaryContainer = driverTools.swipeToElement(BOOKING_SUMMARY_CONTAINER_NON_LOGIN, Swipe.DOWN, 5).orElseThrow();
        return driverTools.isElementDisplayed(bookingSummaryContainer);
    }

    @Override
    public void clickExtraFacilityAddOn() {
        StringBuilder log = new StringBuilder("[Add-on] ");
        log.append("Scrolling to add-on select button, then opening first add-on. ");
        driverTools.swipeToElement(ADDON_SELECT_BUTTON, Swipe.DOWN, 4);
        driverTools.click(ADDON_SELECT_BUTTON);
        log.append("Selecting room checkbox, incrementing quantity by 1, saving. ");
        driverTools.click(ROOM_ADD_ONS_CHECKBOX);
        driverTools.swipeToElement(INCREMENT_BUTTON, Swipe.UP, 2);
        driverTools.click(INCREMENT_BUTTON);
        driverTools.click(SAVE_BUTTON);
        logger.log(log.toString());
    }

    private static Locator ADDON_SELECT_BUTTON(String addOnType) {
        String[] addOnTypeLang = splitLanguageData(addOnType, "-");
        return new Locator("ADDON_SELECT_BUTTON_" + addOnType.toUpperCase().replace("/",""), AppiumBy.xpath("//*[contains(@resource-id,'rv_add_ons')]//*[contains(@resource-id,'tv_type') and (contains(@text,'" + addOnTypeLang[0] + "') or contains(@text,'" + addOnTypeLang[1] + "'))]//following-sibling::*[contains(@resource-id,'btn_add')]"));
    }

    private static Locator ADDON_CATEGORY_AFTER(String addOnCategoryName) {
        return new Locator("ADDON_CATEGORY_AFTER_" + addOnCategoryName.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id, 'rv_category')]//*[contains(@resource-id, 'tds_chips_text') and contains(@text,'" + addOnCategoryName + "')]//following::*[contains(@resource-id, 'tds_chips_text')]"));
    }

    private static Locator ROOM_ADD_ONS_CHECKBOX(String roomName) {
        return new Locator("ROOM_ADD_ONS_CHECKBOX_" + roomName.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'crf_bottom_sheet')]//*[contains(@resource-id,'rv_room')]//*[contains(@resource-id,'tv_label') and @text='" + roomName + "']//parent::*[contains(@class,'ViewGroup') and count(*) <= 2]//*[contains(@resource-id,'cb_room')]"));
    }

    private static Locator ROOM_ADD_ONS_CHECKBOX(String roomName, int index) {
        return new Locator("ROOM_ADD_ONS_CHECKBOX_" + roomName.toUpperCase() + "_" + index, AppiumBy.xpath("(//*[contains(@resource-id,'crf_bottom_sheet')]//*[contains(@resource-id,'rv_room')]//*[contains(@resource-id,'tv_label') and @text='" + roomName + "']//parent::*[contains(@class,'ViewGroup')]//*[contains(@resource-id,'cb_room')])[" + index + "]"));
    }

    private static Locator ROOM_NIGHT_ADD_ONS_CHECKBOX(String roomName) {
        return new Locator("ROOM_NIGHT_ADD_ONS_CHECKBOX_" + roomName.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'crf_bottom_sheet')]//*[contains(@resource-id,'rv_room')]//*[contains(@resource-id,'tv_room') and @text='" + roomName + "']//following::*[contains(@resource-id,'cb_room')]"));
    }

    private static Locator ROOM_NIGHT_ADD_ONS_CHECKBOX(String roomName, int index) {
        return new Locator("ROOM_NIGHT_ADD_ONS_CHECKBOX_" + roomName.toUpperCase(), AppiumBy.xpath("(//*[contains(@resource-id,'crf_bottom_sheet')]//*[contains(@resource-id,'rv_room')]//*[contains(@resource-id,'tv_room') and @text='" + roomName + "'])[" + index + "]//following::*[contains(@resource-id,'cb_room')]"));
    }

    private static Locator INCREMENT_BUTTON(String roomName, int index) {
        return new Locator("INCREMENT_BUTTON_" + roomName.toUpperCase() + "_" + index, AppiumBy.xpath("((//*[contains(@resource-id,'tv_label') and @text='" + roomName + "']//following-sibling::*[contains(@resource-id,'rv_quantity_editor')])[" + index + "]//*[contains(@class,'ViewGroup')]//*[contains(@resource-id,'btn_increment')])[1]"));
    }

    private static Locator INCREMENT_BUTTON(String roomName) {
        return new Locator("INCREMENT_BUTTON_" + roomName.toUpperCase(), AppiumBy.xpath("(//*[contains(@resource-id,'tv_label') and @text='" + roomName + "']//following-sibling::*[contains(@resource-id,'rv_quantity_editor')]//*[contains(@class,'ViewGroup')]//*[contains(@resource-id,'btn_increment')])[1]"));
    }

    private static Locator NIGHT_INCREMENT_BUTTON(String roomName) {
        return new Locator("NIGHT_INCREMENT_BUTTON_" + roomName.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'crf_bottom_sheet')]//*[contains(@resource-id,'rv_room')]//*[contains(@resource-id,'tv_room') and @text='" + roomName + "']//following::*[contains(@resource-id,'rv_quantity_editor')]//*[contains(@class,'ViewGroup')]//*[contains(@resource-id,'btn_increment')]"));
    }

    private static Locator NIGHT_INCREMENT_BUTTON(String roomName, int index) {
        return new Locator("NIGHT_INCREMENT_BUTTON_" + roomName.toUpperCase(), AppiumBy.xpath("(//*[contains(@resource-id,'crf_bottom_sheet')]//*[contains(@resource-id,'rv_room')]//*[contains(@resource-id,'tv_room') and @text='" + roomName + "'])[" + index + "]//following::*[contains(@resource-id,'rv_quantity_editor')]//*[contains(@class,'ViewGroup')]//*[contains(@resource-id,'btn_increment')]"));
    }

    @Override
    public HashMap<Object, Object> clickExtraFacilityAddOn(int itemPerRoom, AddOnsType addOnsType, HashMap<Integer, String> selectedRoomName) {
        StringBuilder log = new StringBuilder("[Add-on multi-room] ");
        log.append("Type=").append(addOnsType).append(", itemPerRoom=").append(itemPerRoom)
           .append(", rooms=").append(selectedRoomName.size()).append(" ").append(selectedRoomName).append(". ");
        logger.log(log.toString());

        String addOnType = addOnsType.equals(AddOnsType.ITEM) ? "/item" : "/room-/kamar";

        int maxAttempt = 2;
        int i = 0;
        while (!driverTools.isElementDisplayed(ROOM_ADD_ONS_CHECKBOX) && i < maxAttempt) {
            log = new StringBuilder("[Add-on multi-room] ");
            log.append("Opening add-on selector (attempt ").append(i + 1).append("), scrolling to category if needed. ");
            logger.log(log.toString());
            selectItem(maxAttempt, ADDON_SELECT_BUTTON(addOnType), ADDON_CONTAINER);
            if (driverTools.waitForElementToPresence(ROOM_ADD_ONS_CHECKBOX).isPresent()) break;
            String selectedCategory = driverTools.getText(ADDON_CATEGORY_SELECTED).orElseThrow();
            selectItem(maxAttempt, ADDON_CATEGORY_AFTER(selectedCategory), ADDON_CATEGORY);
            i++;
        }

        String addOnName = driverTools.getText(ADDON_NAME_POPUP).orElseThrow();
        String singlePrice = driverTools.getText(ADDON_PRICE_POPUP).orElseThrow();

        int totalRoomType = selectedRoomName.size();
        log = new StringBuilder("[Add-on multi-room] ");
        log.append("Add-on name='").append(addOnName).append("', single price=").append(singlePrice)
           .append(", applying to ").append(totalRoomType).append(" room(s). ");
        logger.log(log.toString());

        int totalAddOnsItem = addItemOnEachRoom(addOnsType, itemPerRoom, selectedRoomName);
        double expectedTotalPrice = convertPrice(singlePrice) * totalAddOnsItem;
        String actualTotalPrice = driverTools.getText(ADDON_TOTAL_PRICE_POPUP).orElseThrow();
        Assertion.assertThat("user can add the add ons of each night of room type", convertPrice(actualTotalPrice), is(expectedTotalPrice));
        driverTools.click(SAVE_BUTTON);

        HashMap<Object, Object> addOns = new HashMap<>();
        addOns.put(AddOnsType.NAME, addOnName);
        addOns.put(AddOnsType.SINGLE_PRICE, singlePrice);
        addOns.put(AddOnsType.TOTAL_ROOM_TYPE, totalRoomType);
        addOns.put(AddOnsType.TOTAL_ADDONS_ITEM, totalAddOnsItem);
        addOns.put(AddOnsType.TOTAL_PRICE, actualTotalPrice);
        log = new StringBuilder("[Add-on multi-room] ");
        log.append("Saved. Total add-on items=").append(totalAddOnsItem).append(", total price=").append(actualTotalPrice);
        logger.log(log.toString());

        return addOns;
    }

    private int addItemOnEachRoom(AddOnsType addOnsType, int itemPerRoom, HashMap<Integer, String> roomNameData) {
        int roomIndex = 1;
        String currentRoomName = "";
        int totalAddOns = 0;

        for (String roomName : roomNameData.values()) {
            if (addOnsType.equals(AddOnsType.ITEM)) {
                if (roomName.equals(currentRoomName)) {
                    totalAddOns += selectItems(itemPerRoom, ROOM_ADD_ONS_CHECKBOX(roomName, roomIndex), INCREMENT_BUTTON(roomName, roomIndex));
                } else {
                    totalAddOns += selectItems(itemPerRoom, ROOM_ADD_ONS_CHECKBOX(roomName), INCREMENT_BUTTON(roomName));
                }
            } else {
                if (roomName.equals(currentRoomName)) {
                    totalAddOns += selectItems(itemPerRoom, ROOM_NIGHT_ADD_ONS_CHECKBOX(roomName, roomIndex), NIGHT_INCREMENT_BUTTON(roomName, roomIndex));
                } else {
                    totalAddOns += selectItems(itemPerRoom, ROOM_NIGHT_ADD_ONS_CHECKBOX(roomName), NIGHT_INCREMENT_BUTTON(roomName));
                }
            }

            currentRoomName = roomName;
            roomIndex++;
        }

        logger.log("totalAddOns -> " + totalAddOns);
        return totalAddOns;
    }

    private int selectItems(int itemPerRoom, Locator checkBoxLocator, Locator incrementButtonLocator) {
        int totalAddOns = 0;
        driverTools.swipeToElement(checkBoxLocator, Swipe.UP, 2).orElseThrow();
        driverTools.clickElementByCoordinate(checkBoxLocator);
        for (int i = 0; i < itemPerRoom; i++) {
            driverTools.swipeToElement(incrementButtonLocator, Swipe.UP, 2).orElseThrow();
            driverTools.clickElementByCoordinate(incrementButtonLocator);
            totalAddOns++;
        }
        return totalAddOns;
    }

    private static Locator ADDONS_NAME_PRICE_BREAKDOWN(int roomTypeIndex) {
        return new Locator("ADDONS_NAME_PRICE_BREAKDOWN_TYPE_" + roomTypeIndex, AppiumBy.xpath("//*[contains(@resource-id,'tv_room_name')]//preceding-sibling::*[contains(@class,'FrameLayout')]//*[contains(@class,'LinearLayout')]//*[contains(@class,'TextView') and contains(@text,'" + roomTypeIndex + "')]//following::*[contains(@resource-id,'v_price_add_ons')]//*[contains(@resource-id,'v_bullet')]//following-sibling::*[contains(@resource-id,'tv_title')]"));
    }

    private static Locator ADDONS_PRICE_DETAILS_BREAKDOWN(int roomTypeIndex) {
        return new Locator("ADDONS_PRICE_DETAILS_BREAKDOWN_TYPE_" + roomTypeIndex, AppiumBy.xpath("//*[contains(@resource-id,'tv_room_name')]//preceding-sibling::*[contains(@class,'FrameLayout')]//*[contains(@class,'LinearLayout')]//*[contains(@class,'TextView') and contains(@text,'" + roomTypeIndex + "')]//following::*[contains(@resource-id,'v_price_add_ons')]//*[contains(@resource-id,'v_bullet')]//following-sibling::*[contains(@resource-id,'tv_price')]"));
    }

    @Override
    public boolean checkAddOnPriceDetail(int totalRoomType, String addOnsName, String singlePrice, int itemPerRoom) {
        driverTools.waitForElementToAppear(ROOM_TYPE_PRICE_BREAKDOWN).orElseThrow();
        Assertion.assertThat("Total room type should be equal with the total selected on the room list", driverTools.elements(ROOM_TYPE_PRICE_BREAKDOWN).orElseThrow().size(), is(totalRoomType));
        for (int i = 1; i <= totalRoomType; i++) {
            WebElement addOns = driverTools.swipeToElement(ADDONS_NAME_PRICE_BREAKDOWN(i), PRICE_SUMMARY_CONTAINER, Swipe.UP, 2).orElseThrow();
            String addOnsNameText = driverTools.getText(addOns);
            Assertion.assertThat(i + " Add-ons name should be equal with the selected add-ons", addOnsNameText, is(addOnsName + " (x" + itemPerRoom + ")"));
            String addOnsPriceText = driverTools.getText(ADDONS_PRICE_DETAILS_BREAKDOWN(i)).orElseThrow();
            if (itemPerRoom == 1) {
                Assertion.assertThat(i + " Add-ons price should be equal with the selected add-ons", addOnsPriceText, is(singlePrice));
            } else {
                Assertion.assertThat(i + " Add-ons price should be equal with the selected add-ons", convertPrice(addOnsPriceText), is(convertPrice(singlePrice) * itemPerRoom));
            }
        }
        return true;
    }

    @Override
    public String getAddOnPrice() {
        return driverTools.getText(ADDON_PRICE_CARD).orElseThrow();
    }

    @Override
    public String getAddonName() {
        driverTools.swipeToElement(ADDON_NAME_CARD, Swipe.DOWN, 2);
        return driverTools.getText(ADDON_NAME_CARD).orElseThrow();
    }

    @Override
    public String getAddonTitle() {
        return getAddonName();
    }

    @Override
    public Boolean MatchAddOnPriceDetail(Double addOnPrice, String addOnCategory) {
        Locator ADDON_CATEGORY_PRICE_DETAIL = new Locator("ADDON_CATEGORY_PRICE_DETAIL", AppiumBy.xpath("//*[contains(@resource-id, 'price_summary_view')]//*[contains(@resource-id, 'tv_left') and contains(@text,'" + addOnCategory + "')]"));
        Locator ADDON_PRICE_PRICE_DETAIL = new Locator("ADDON_PRICE_PRICE_DETAIL", AppiumBy.xpath("//*[contains(@resource-id, 'price_summary_view')]//*[contains(@resource-id, 'tv_left') and contains(@text,'" + addOnCategory + "')]//following-sibling::*[contains(@resource-id, 'tv_right')]"));
        double addOnPriceDetail = convertPrice(driverTools.getText(ADDON_PRICE_PRICE_DETAIL).orElseThrow());
        Assertion.assertThat("Addon category should be displayed on price detail", driverTools.isElementDisplayed(ADDON_CATEGORY_PRICE_DETAIL), is(true));
        Assertion.assertThat("Addon price detail should match the addOnPrice", addOnPriceDetail, is(addOnPrice));
        logger.log("Addon Price on Booking Form = " + addOnPrice);
        logger.log("Addon Price on Price Detail = " + addOnPriceDetail);
        return driverTools.isElementDisplayed(ADDON_CATEGORY_PRICE_DETAIL) && (addOnPriceDetail == addOnPrice);
    }

    @Override
    public String getTotalPricePayAtHotelBookingForm() {
        return driverTools.getText(PAY_AT_HOTEL_TOTAL_PRICE).orElseThrow();
    }

    @Override
    public String loyaltyBenefitValueText() {
        //clicking and opening price detail
        WebElement priceDetailArrow = driverTools.waitForElementToAppear(TOTAL_PRICE_DETAIL_ARROW_BUTTON).orElseThrow();
        driverTools.click(priceDetailArrow);
        logger.log("Price detail is opened");

        WebElement loyaltyBenefitDiscount = driverTools.waitForElementToAppear(LOYALTY_BENEFIT_DISCOUNT_ON_PRICE_DETAIL).orElseThrow();
        String discount = driverTools.getText(loyaltyBenefitDiscount);
        discount = discount.replace(" ","");
        logger.log("***************Total discount amount is **************: " + discount);

        //closing price detail
        driverTools.click(CANCEL_BUTTON);
        logger.log("Price detail is closed");

        return discount;

    }

    @Override
    public void openLoginPage() {
        driverTools.swipeToElement(LOGIN_BUTTON,Swipe.DOWN,3).orElseThrow();
        driverTools.click(LOGIN_BUTTON);
    }

    @Override
    public boolean isLoggedIn() {
        return (driverTools.waitForElementToDisappear(EMAIL_INPUT,5) || driverTools.waitForElementToDisappear(EMAIL_CONTACT_V3,5))
                && driverTools.waitForElementToDisappear(LOGIN_BUTTON, 5);
    }

    @Override
    public boolean canNotBookMultiOrderCorporatePopUp() {
        driverTools.waitForElementToAppear(ONE_ROOM_POPUP);
        return driverTools.isElementDisplayed(ONE_ROOM_POPUP);
    }

    @Override
    public boolean canNotBookMultiOrderTravellerPopUp() {
        driverTools.waitForElementToAppear(MO_CORPORATE_POPUP);
        return driverTools.isElementDisplayed(MO_CORPORATE_POPUP);
    }

    @Override
    public int getAddOnQty(String addOnName){
        Locator ADDON_CATEGORY_PRICE_DETAIL = new Locator("ADDON_CATEGORY_PRICE_DETAIL", AppiumBy.xpath("//*[contains(@resource-id, 'v_price_add_ons')]//*[contains(@resource-id, 'tv_title') and contains(@text,'" + addOnName + "')]"));
        return getIntFromString(driverTools.getText(ADDON_CATEGORY_PRICE_DETAIL).orElseThrow()).orElseThrow();
    }

    @Override
    public String getEstimationRefund() {
        driverTools.waitForElementToAppear(REFUND_ESTIMATION);
        String estimatedRefund = driverTools.getText(REFUND_ESTIMATION).orElseThrow();
        logger.log("estimated refund :  " + estimatedRefund);
        return estimatedRefund;
    }

    @Override
    public List<String> getPriceDetailPrices(String totalPayment) {
        driverTools.waitForElementToAppear(PRICE_BREAKDOWN).orElseThrow();
        List<String> priceItems = new ArrayList<>(driverTools.getTexts(PRICE_BREAKDOWN));
        Assertion.assertThat("Prices exist", priceItems, is(not(empty())));
        logger.log("prices: "+priceItems);
        priceItems.removeIf(str -> !str.contains(" ") || str.contains("Termasuk"));
        return priceItems;
    }

    @Override
    public List<String> getPriceDetailPricesReschedule() {
        List<String> priceItems = new ArrayList<>(driverTools.getTexts(PRICE_BREAKDOWN));
        Assertion.assertThat("Prices exist", priceItems, is(not(empty())));
        logger.log("prices: "+priceItems);
        priceItems.removeIf(str -> str.contains("GRATIS") || str.contains("FREE"));
        if(driverTools.isElementDisplayed(REFUND_LABEL_DETAIL)){
            priceItems.add(driverTools.getText(REFUND_LABEL_DETAIL).orElseThrow());
        }
        return priceItems;
    }

    private static final Locator PAY_UPON_CHECK_IN_LABEL = new Locator("PAY_UPON_CHECK_IN_LABEL",
            AppiumBy.xpath("//*[contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'pay upon check-in') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'bayar saat check-in')]"));
    private static final Locator GIFT_VOUCHER_CLAIM_BUTTON = new Locator("GIFT_VOUCHER_CLAIM_BUTTON",
            AppiumBy.xpath("//*[contains(@resource-id,'voucher') or contains(@resource-id,'gift')]//*[@clickable='true' and @enabled='true' and (contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'claim') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'klaim')) and not(contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'claimed')) and not(contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'diklaim'))]"));
    private static final Locator GIFT_VOUCHER_CLAIM_SNACKBAR = new Locator("GIFT_VOUCHER_CLAIM_SNACKBAR",
            AppiumBy.xpath("//*[contains(@resource-id,'tds_snackbar_text') and (contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'vouchers will go to your voucher box') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'voucher akan masuk ke voucher box'))]"));

    @Override
    public void fillSpecialRequest(String request){
        driverTools.click(SPECIAL_REQUEST_TAB);
        driverTools.sendKeys(SPECIAL_REQUEST_TEXTFIELD,request);
        driverTools.click(SAVE_BUTTON);
    }

    @Override
    public boolean isPayUponCheckInLabelAndPriceDisplayed() {
        boolean labelOk = driverTools.swipeToElement(PAY_UPON_CHECK_IN_LABEL, Swipe.UP, 3).isPresent()
                || driverTools.isElementDisplayed(PAY_UPON_CHECK_IN_LABEL);
        String price = driverTools.getText(PAY_AT_HOTEL_TOTAL_PRICE)
                .or(() -> driverTools.getText(PAY_UPON_CHECK_IN_PRICE))
                .orElse("").trim();
        boolean priceOk = !price.isEmpty();
        logger.log("Pay upon check-in label present=" + labelOk + ", price='" + price + "'");
        return labelOk && priceOk;
    }

    @Override
    public void claimGiftVoucherOnBookingFormPage2() {
        driverTools.swipeToElement(GIFT_VOUCHER_CLAIM_BUTTON, Swipe.UP, 8).orElseThrow();
        driverTools.click(GIFT_VOUCHER_CLAIM_BUTTON);
        Assertion.assertThat("Gift voucher claim button should disappear after claim",
                driverTools.waitForElementToDisappear(GIFT_VOUCHER_CLAIM_BUTTON, 10), is(true));
        driverTools.waitForElementToAppear(GIFT_VOUCHER_CLAIM_SNACKBAR, 10);
        driverTools.waitForElementToDisappear(GIFT_VOUCHER_CLAIM_SNACKBAR, 15);
    }

    @Override
    public boolean isFlexiblePaymentLabelDisplayed() {
        return driverTools.swipeToElement(ROOM_FLEXIBLE_PAYMENT, Swipe.UP, 5).isPresent();
    }

    @Override
    public void clickFlexiblePaymentSection() {
        driverTools.swipeToElement(ROOM_FLEXIBLE_PAYMENT, Swipe.UP, 5).orElseThrow();
        driverTools.click(ROOM_FLEXIBLE_PAYMENT);
    }

    @Override
    public boolean isDownPaymentContentDisplayed() {
        return driverTools.waitForElementToAppear(DOWNPAYMENT_CONTENT, 10).isPresent();
    }

    @Override
    public boolean isPayByButtonSelected() {
        driverTools.swipeToElement(PAY_BY_BUTTON, Swipe.UP, 7).orElseThrow();
        driverTools.swipeUp();
        return driverTools.isElementDisplayed(PAY_BY_BUTTON_SELECTED);
    }

    @Override
    public void clickDownPaymentContentCloseButton() {
        driverTools.waitForElementToAppear(CLOSE_BUTTON).orElseThrow();
        driverTools.click(CLOSE_BUTTON);
        driverTools.waitForElementToDisappear(CLOSE_BUTTON, 10);
    }

    @Override
    public void clickContinueToPaymentButton() {
        String text = getContinueToPaymentButtonText();
        driverTools.click(SELECT_PAYMENT_BUTTON);
        logger.log("Clicked continue to payment button");
        if (isPayZeroTodayText(text)) {
            logger.log("Pay IDR 0 today, confirm booking on the bottom sheet");
            driverTools.waitForElementToAppear(CONFIRM_BOOKING_CONTINUE_BUTTON, 10).orElseThrow();
            driverTools.click(CONFIRM_BOOKING_CONTINUE_BUTTON);
        }
    }

    @Override
    public String getContinueToPaymentButtonText() {
        driverTools.swipeToElement(SELECT_PAYMENT_BUTTON, Swipe.UP, 8).orElseThrow();
        String text = driverTools.getText(SELECT_PAYMENT_BUTTON).orElseThrow();
        logger.log("Continue to payment button text -> " + text);
        return text == null ? "" : text;
    }

    @Override
    public boolean isPriceDetailRescheduleBookingFormCorrect(String totalPayment) {
        driverTools.swipeToElement(TOTAL_PRICE_BREAKDOWN_RESCHEDULE_BOOKING_FORM, Swipe.UP, 1);
        driverTools.waitForElementToAppear(PRICE_BREAKDOWN_RESCHEDULE_BOOKING_FORM,10);
        List<WebElement> prices = driverTools.elements(PRICE_BREAKDOWN_RESCHEDULE_BOOKING_FORM).orElseThrow();
        ArrayList<Double> arrayPrice = new ArrayList<>();

        for (WebElement element : prices) {
            String text = driverTools.getText(element);
            try {
                double price = CoreFunctions.convertPriceSafely(text);
                logger.log("DEBUG text : " + text);
                logger.log("DEBUG price : " + price);
                arrayPrice.add(price);
            } catch (NumberFormatException message) {
                logger.log("Number format exception : " + message.getMessage());
            }
        }

        double actualPrice = 0;
        for (double i : arrayPrice) {
            actualPrice += i;
        }
        logger.log("DEBUG sum actual price: " + actualPrice);
        double totalPricePriceDetailWithoutSubstraction = CoreFunctions.convertPriceSafely(driverTools.getText(TOTAL_PRICE_BREAKDOWN_WITHOUT_SUBSTRACTION_RESCHEDULE_BOOKING_FORM).orElseThrow());
        String totalPriceAfterSubstraction = driverTools.getText(TOTAL_PRICE_BREAKDOWN_RESCHEDULE_BOOKING_FORM).orElseThrow();
        String currentBalance = driverTools.getText(TOTAL_PRICE_BREAKDOWN_CURRENT_BALANCE_RESCHEDULE_BOOKING_FORM).orElseThrow(); //-IDR xxxx
        logger.log("Current balance : " + CoreFunctions.convertPriceSafely(currentBalance));
        logger.log("Total price from price detail without substraction:  " + totalPricePriceDetailWithoutSubstraction);
        logger.log("Total price got:  " + actualPrice);
        if(!totalPayment.equalsIgnoreCase("FREE") && !totalPayment.equalsIgnoreCase("GRATIS")){
            double totalPriceFromBookingPage = CoreFunctions.convertPriceSafely(totalPayment);
            logger.log("Total price from the booking page: " + totalPriceFromBookingPage);
            Assertion.assertThat("Total price from the booking form should be equal with the breakdown total price",String.valueOf(CoreFunctions.convertPriceSafely(totalPriceAfterSubstraction)),is(String.valueOf(totalPriceFromBookingPage)));
            Assertion.assertThat("Verify that total price substract by current balance equal to total price ",(actualPrice+CoreFunctions.convertPriceSafely(currentBalance)), is(CoreFunctions.convertPriceSafely(totalPriceAfterSubstraction)));
        }else{
            Assertion.assertThat("Total price from the booking form should be equal with the breakdown price",String.valueOf(totalPriceAfterSubstraction),is(totalPayment));
            if(driverTools.isElementDisplayed(GOT_REFUND_WORDING)){
                String refundValue = driverTools.getText(GOT_REFUND_WORDING).orElseThrow();
                Assertion.assertThat("Verify that total price substract by current balance equal to total price (0) ",String.valueOf(actualPrice+CoreFunctions.convertPriceSafely(currentBalance)), is(String.valueOf(CoreFunctions.convertPriceSafely(refundValue)*-1)));
            } else Assertion.assertThat("Verify that total price substract by current balance equal to total price (0) ",String.valueOf(actualPrice+CoreFunctions.convertPriceSafely(currentBalance)), is(String.valueOf(0.0)));
        }
        Assertion.assertThat("Total Price should be correct",String.valueOf(actualPrice),is(String.valueOf(totalPricePriceDetailWithoutSubstraction)));

        return true;
    }

    @Override
    public List<String> getPriceTitles() {
        if(!driverTools.isElementDisplayed(ROOM_PRICE_TITLE)){
            driverTools.click(PRICE_DETAIL_SUB_TOTAL_EXPAND);
        }
        driverTools.waitForElementToPresence(ROOM_PRICE_TITLE);
        List<String> priceTitles = driverTools.getTexts(ROOM_PRICE_TITLE);
        Assertion.assertThat("Prices exist", priceTitles, is(not(empty())));
        logger.log("Price titles: "+priceTitles);
        return priceTitles;
    }

    @Override
    public boolean fillGuestDetailNoSalutation(){
        driverTools.swipeToElement(GUEST_DETAIL_EDIT_BUTTON, Swipe.UP, 5);
        driverTools.click(GUEST_DETAIL_EDIT_BUTTON);
        driverTools.waitForElementToPresence(CLOSE_POPUP_BUTTON);
        List<WebElement> roomTabs = driverTools.waitForPresenceElementsList(ROOM_TYPE_TAB).orElseThrow();
        int numberOfRooms = roomTabs.size();
        logger.log("numberOfRooms -> " + numberOfRooms);
        if(numberOfRooms == 0){
            numberOfRooms = 1;
        }

        List<String> names = new ArrayList<>();
        logger.log("number of rooms: "+numberOfRooms);
        for(int j = 0; j < numberOfRooms; j++) {
            if(j>0){
                roomTabs = driverTools.elements(ROOM_TYPE_TAB).orElseThrow();
                driverTools.click(roomTabs.get(j));
            }

            Assertion.assertThat("Salutation checkbox not present",driverTools.isElementDisplayed(MR_SALUTATION_CHECKBOX),is(false));

            List<WebElement> nameFields = driverTools.elements(FULL_NAME_INPUT).orElseThrow();
            int numberOfGuest = nameFields.size();

            for (int i = 0; i < numberOfGuest; i++) {
                if(i>0){
                    nameFields = driverTools.elements(FULL_NAME_INPUT).orElseThrow();
                }
                driverTools.clear(nameFields.get(i));
                String name = randomName();
                names.add(name);
                driverTools.sendKeysKeyboard(nameFields.get(i), name);
                driverTools.hideKeyboard();
                driverTools.swipeUp();
            }
        }
        driverTools.click(SAVE_BUTTON);
        driverTools.swipeUp();
        List<String> namesBookingForm = driverTools.getTexts(GUEST_DETAIL_EDIT_BUTTON).stream()
        .map(name -> {
            String[] parts = name.split(":");
            return parts.length > 1 ? parts[1].trim() : parts[0].trim();
        })
        .collect(Collectors.toList());
        Assertion.assertThat("Names are inputted correctly",names,is(namesBookingForm));
        return true;
    }

    @Override
    public void cancelProtection() {
        driverTools.swipeToElement(EXTRA_PROTECTION_CANCEL, Swipe.UP, 5).orElseThrow();
        driverTools.click(EXTRA_PROTECTION_CANCEL);
    }

    @Override
    public String getCrossedPriceBookingForm() {
        return driverTools.getText(CROSSED_PRICE).orElseThrow();
    }

    @Override
    public boolean isLabelTotalPrice(){
        return driverTools.waitForElementToPresence(TOTAL_PRICE_LABEL).isPresent();
    }

    @Override
    public void addPromo(String promo){
        clickPayNowButton();
        driverTools.swipeToElement(CHECK_MORE_PROMO,Swipe.UP,3);
        driverTools.click(CHECK_MORE_PROMO);
        driverTools.sendKeys(PROMO_TEXT_INPUT,promo);
        driverTools.click(APPLY_PROMO_BUTTON);
    }

    @Override
    public boolean isPayingThisAmountTextInGreen() {
        driverTools.waitForElementsToAppear(PAYING_AMOUNT_TEXT_GREEN);
        WebElement payingAmount = driverTools.swipeToElement(PAYING_AMOUNT_TEXT_GREEN, Swipe.UP, 5).orElse(null);
        if (payingAmount == null) {
            logger.softWarning("'You will be paying this amount' nudge not found on Booking Form");
            return false;
        }
        String payingAmountText = driverTools.getText(payingAmount);
        logger.log("payingAmountText -> " + payingAmountText);
        return !payingAmountText.isEmpty();
    }

 }
