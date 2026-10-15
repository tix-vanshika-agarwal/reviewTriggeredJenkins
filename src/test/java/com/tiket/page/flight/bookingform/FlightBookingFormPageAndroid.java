package com.tiket.page.flight.bookingform;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.flight.*;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tiket.core.CoreFunctions.convertPrice;
import static com.tiket.core.CoreFunctions.doubleRound;
import static com.tiket.page.payment.PaymentPageAndroid.ORDER_ID;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.core.Is.is;

public class FlightBookingFormPageAndroid<T extends WebDriver> extends FlightBookingFormPage<T> {

    public static final Locator SELECT_TICKET_OLD = new Locator("SELECT_TICKET_OLD", AppiumBy.xpath("//*[contains(@resource-id, \"tds_button_view\") and (@text=\"Select\" or @text='Pilih')]"));
    public static final Locator LOGIN_BANNER = new Locator("LOGIN_BANNER", AppiumBy.xpath("//*[contains(@resource-id,'banner_login') or contains(@resource-id,'tv_login_banner')]"));
    public static final Locator TRIP_DETAIl = new Locator("TRIP_DETAIl", AppiumBy.id("card_pdp"));
    public static final Locator DEPARTURE_DATE = new Locator("DEPARTURE_DATE", AppiumBy.id("tv_flight_departure_date_time"));
    public static final Locator DEPARTURE_AIRPORT = new Locator("DEPARTURE_AIRPORT", AppiumBy.id("tv_flight_departure_airport_name"));
    public static final Locator ARRIVAL_DATE = new Locator("ARRIVAL_DATE", AppiumBy.xpath("(//*[contains(@resource-id,'tv_date_duration') or contains(@resource-id,'tv_flight_arrival_date_time')])[1]"));
    public static final Locator ARRIVAL_AIRPORT = new Locator("ARRIVAL_AIRPORT", AppiumBy.id("tv_flight_arrival_airport_name"));
    public static final Locator TRIP_DETAIl_BACK = new Locator("TRIP_DETAIl_BACK", AppiumBy.id("iv_flight_detail_page_close"));
    public static final Locator CONTACT_DETAIL = new Locator("CONTACT_DETAIL", AppiumBy.id("card_contact_details"));
    public static final Locator FILL_CONTACT_DETAILS = new Locator("FILL_CONTACT_DETAILS", AppiumBy.id("tv_fill_contact"));
    public static final Locator LOAD_ERROR_RETRY = new Locator("LOAD_ERROR_RETRY", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (@text='Reload' or @text='Muat ulang' or @text='Coba lagi' or @text='Try again')] | //android.widget.Button[@text='Reload' or @text='Muat ulang' or @text='Coba lagi' or @text='Try again'] | //*[@clickable='true' and (@text='Reload' or @text='Muat ulang' or @text='Coba lagi' or @text='Try again')]"));
    public static final Locator PASSENGER_ROW = new Locator("PASSENGER_ROW", AppiumBy.xpath("//*[contains(@resource-id,'items_container')]//*[contains(@resource-id,'item_row')]"));
    public static final Locator CONTACT_BOTTOM_SHEET = new Locator("CONTACT_BOTTOM_SHEET", AppiumBy.id("crf_bottom_sheet"));
    public static final Locator CONTACT_FORM_SAVE = new Locator("CONTACT_FORM_SAVE", AppiumBy.xpath("//*[contains(@resource-id,'crf_bottom_sheet')]//*[contains(@resource-id,'btn_flight_bottom_button')]//*[contains(@resource-id,'tds_button_view')]"));
    public static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON", AppiumBy.xpath("//*[contains(@resource-id, 'iv_close') or contains(@resource-id,'iv_flight_edit_passenger_close')]"));
    public static final Locator CLOSE_BUTTON_INSURANCE = new Locator("CLOSE_BUTTON_INSURANCE", AppiumBy.id("iv_close"));
    public static final Locator PHONE = new Locator("PHONE", AppiumBy.xpath("//*[contains(@resource-id,'tf_flight_booking_dynamic_phone_tds')]//*[contains(@resource-id,'tds_box_input_text')]"));
    public static final Locator TITLE_RADIO = new Locator("TITLE_RADIO", AppiumBy.xpath("//*[contains(@resource-id,'ll_flight_radio_group')]//android.widget.TextView"));
    public static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", AppiumBy.id("btn_flight_bottom_button"));
    public static final Locator PASSENGER_FORM = new Locator("PASSENGER_FORM", AppiumBy.id("rv_form_fields"));
    public static final Locator PASSENGER_FORM_SHEET = new Locator("PASSENGER_FORM_SHEET", AppiumBy.xpath("//*[contains(@resource-id,'crf_bottom_sheet')][.//*[contains(@resource-id,'tv_title') and (contains(@text,'Passenger Form') or contains(@text,'Formulir Penumpang') or contains(@text,'Data Penumpang'))]]"));
    public static final Locator PASSENGER_FORM_DYNAMIC = new Locator("PASSENGER_FORM_DYNAMIC", AppiumBy.xpath("//*[contains(@resource-id,'crf_bottom_sheet')]//*[contains(@resource-id,'view_flight_dynamic_form')]"));
    // Union of PASSENGER_FORM | PASSENGER_FORM_SHEET | PASSENGER_FORM_DYNAMIC so "is a passenger form open?"
    // is a SINGLE wait instead of three stacked waitForElementToAppear timeouts (major speed-up: the
    // "form never opens" case for already-complete saved passengers previously cost ~10s each).
    public static final Locator ANY_PASSENGER_FORM = new Locator("ANY_PASSENGER_FORM", AppiumBy.xpath("//*[contains(@resource-id,'rv_form_fields')] | //*[contains(@resource-id,'crf_bottom_sheet')][.//*[contains(@resource-id,'tv_title') and (contains(@text,'Passenger Form') or contains(@text,'Formulir Penumpang') or contains(@text,'Data Penumpang'))]] | //*[contains(@resource-id,'crf_bottom_sheet')]//*[contains(@resource-id,'view_flight_dynamic_form')]"));
    public static final Locator PASSENGER_FORM_SAVE = new Locator("PASSENGER_FORM_SAVE", AppiumBy.xpath("//*[contains(@resource-id,'btn_save')]//*[contains(@resource-id,'tds_button_view')]"));
    public static final Locator PASSENGER_FORM_BACK = new Locator("PASSENGER_FORM_BACK", AppiumBy.id("tds_icon_navigation_toolbar"));
    public static final Locator PASSENGER_REVIEW_SHEET = new Locator("PASSENGER_REVIEW_SHEET", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Review Passenger') or contains(@text,'Tinjau Data Penumpang'))]"));
    public static final Locator PASSENGER_REVIEW_CONFIRM = new Locator("PASSENGER_REVIEW_CONFIRM", AppiumBy.xpath("//*[contains(@resource-id,'crf_bottom_sheet')][.//*[contains(@resource-id,'tv_title') and (contains(@text,'Review Passenger') or contains(@text,'Tinjau Data Penumpang'))]]//*[contains(@resource-id,'btn_action_bottom_primary')]//*[contains(@resource-id,'tds_button_view')]"));
    public static final Locator PASSENGER_REVIEW_CONFIRM_TEXT = new Locator("PASSENGER_REVIEW_CONFIRM_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'crf_bottom_sheet')]//*[contains(@resource-id,'tds_button_view') and (contains(@text,\"It's correct\") or contains(@text,'Sudah benar') or contains(@text,'Next Passenger') or contains(@text,'Penumpang Berikutnya'))]"));
    public static final Locator PASSENGER_PROFILE = new Locator("PASSENGER_PROFILE", AppiumBy.xpath("//*[contains(@resource-id,'group_field_passenger_selection') or contains(@resource-id,'items_container')]//*[contains(@resource-id,'title_text') and not(contains(@text,'Passenger')) and not(contains(@text,'Penumpang'))]"));
    // --- Login / saved-passenger mode (mirrors iOS) ---
    // Logged-in users pick from a saved passenger list (checkboxes/profile rows) instead of the guest "Passenger N" forms.
    public static final Locator
            SAVED_PASSENGER_CHECKBOX_ANY = new Locator("SAVED_PASSENGER_CHECKBOX_ANY", AppiumBy.xpath("//*[contains(@resource-id,'group_field_passenger_selection') or contains(@resource-id,'items_container')]//*[contains(@resource-id,'area_selection_control') or contains(@resource-id,'fl_check') or contains(@resource-id,'iv_checkbox') or contains(@resource-id,'rb_selected')]"));
    public static final Locator SAVED_PASSENGER_NAME = new Locator("SAVED_PASSENGER_NAME", AppiumBy.xpath("//*[contains(@resource-id,'group_field_passenger_selection') or contains(@resource-id,'items_container')]//*[contains(@resource-id,'title_text') and not(contains(@text,'Passenger')) and not(contains(@text,'Penumpang'))]"));
    public static final Locator VIEW_OR_ADD_PASSENGERS = new Locator("VIEW_OR_ADD_PASSENGERS", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'add passengers') or contains(@text,'tambah penumpang') or contains(@text,'View or add') or contains(@text,'Lihat penumpang') or contains(@text,'Tambah Penumpang'))]"));
    // Empty passenger slots on the (login) booking form that still need to be filled manually.
    public static final Locator EMPTY_PASSENGER_SLOT = new Locator("EMPTY_PASSENGER_SLOT", AppiumBy.xpath("(//*[contains(@resource-id,'item_row') or contains(@resource-id,'title_text')][(contains(@text,'Passenger ') or contains(@text,'Penumpang ')) and (contains(@text,'(Adult)') or contains(@text,'(Child)') or contains(@text,'(Infant)') or contains(@text,'(Dewasa)') or contains(@text,'(Anak)') or contains(@text,'(Bayi)'))])[1]"));
    // Age hints shown inside an open passenger form, used to infer the passenger type for the DOB.
    public static final Locator FORM_TYPE_CHILD = new Locator("FORM_TYPE_CHILD", AppiumBy.xpath("//*[contains(@text,'Child Passenger') or contains(@text,'Penumpang Anak') or contains(@text,'age 2') or contains(@text,'usia 2')]"));
    public static final Locator FORM_TYPE_INFANT = new Locator("FORM_TYPE_INFANT", AppiumBy.xpath("//*[contains(@text,'Infant passenger') or contains(@text,'Penumpang bayi') or contains(@text,'0 months') or contains(@text,'Infant Passenger') or contains(@text,'Penumpang Bayi') or contains(@text,'age 0') or contains(@text,'usia 0')]"));
    public static final Locator PASSENGER_FORM_CLEAR = new Locator("PASSENGER_FORM_CLEAR", AppiumBy.xpath("//*[contains(@resource-id,'crf_bottom_sheet') or contains(@resource-id,'rv_form_fields')]/ancestor-or-self::*//*[contains(@resource-id,'btn_action_top') or contains(@resource-id,'btn_clear')]//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Clear') or contains(@text,'Hapus'))] | //*[contains(@resource-id,'btn_action_top')]//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Clear') or contains(@text,'Hapus'))]"));
    public static final Locator PASSENGER_FORM_SAVE_TEXT = new Locator("PASSENGER_FORM_SAVE_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'btn_save')]//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Save') or contains(@text,'Simpan'))] | //*[contains(@resource-id,'crf_bottom_sheet')]//*[contains(@resource-id,'btn_flight_bottom_button')]//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Save') or contains(@text,'Simpan'))]"));
    public static final Locator PASSENGER_FORM_SHEET_CLOSE = new Locator("PASSENGER_FORM_SHEET_CLOSE", AppiumBy.xpath("//*[contains(@resource-id,'crf_bottom_sheet')][.//*[contains(@resource-id,'tv_title') and (contains(@text,'Passenger Form') or contains(@text,'Formulir Penumpang') or contains(@text,'Data Penumpang'))]]//*[contains(@resource-id,'iv_close')]"));
    public static final Locator LEAVE_BOOKING_DIALOG = new Locator("LEAVE_BOOKING_DIALOG", AppiumBy.xpath("//*[contains(@resource-id,'tds_txt_header_info_view') and (contains(@text,'check other flights') or contains(@text,'Cek penerbangan') or contains(@text,'other flights'))]"));
    public static final Locator COMPLETE_BOOKING_BUTTON = new Locator("COMPLETE_BOOKING_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_btn_primary_info_view')]//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Complete') or contains(@text,'Selesaikan') or contains(@text,'Lanjut'))]"));
    public static final Locator GIVEN_NAMES = new Locator("GIVEN_NAMES", AppiumBy.xpath("//*[contains(@resource-id,'rv_form_fields') or contains(@resource-id,'view_flight_dynamic_form')]//*[contains(@resource-id,'tds_box_input_text') and (contains(@hint,'Given Names') or contains(@hint,'Nama Depan') or contains(@hint,'First'))]"));
    public static final Locator SURNAME = new Locator("SURNAME", AppiumBy.xpath("//*[contains(@resource-id,'rv_form_fields') or contains(@resource-id,'view_flight_dynamic_form')]//*[contains(@resource-id,'tds_box_input_text') and (contains(@hint,'Surname') or contains(@hint,'Last Name') or contains(@hint,'Nama Belakang'))]"));
    public static final Locator SAME_AS_CONTACT_DETAIL_SWITCH = new Locator("SAME_AS_CONTACT_DETAIL_SWITCH", AppiumBy.id("tds_toggle_group"));
    public static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'btn_flight_price_and_button') or contains(@resource-id,'flight_price_and_button_continue_to_payment')]//*[contains(@resource-id,'tds_button_view') and (@text='Continue' or @text='Lanjutkan')]"));
    public static final Locator BOOKING_PAGE_ONE_INDICATOR = new Locator("BOOKING_PAGE_ONE_INDICATOR", AppiumBy.xpath("//*[contains(@resource-id,'text_page_indicator') and (@text='1/2' or contains(@text,'1/2'))]"));
    public static final Locator BOOKING_PAGE_TWO_INDICATOR = new Locator("BOOKING_PAGE_TWO_INDICATOR", AppiumBy.xpath("//*[contains(@resource-id,'text_page_indicator') and contains(@text,'2/2')]"));
    /** Red helper under passenger selection when quota is not met / incomplete cached pax cannot proceed. */
    public static final Locator PASSENGER_SELECTION_HELPER_ERROR = new Locator("PASSENGER_SELECTION_HELPER_ERROR", AppiumBy.xpath("//*[contains(@resource-id,'group_field_passenger_selection')]//*[contains(@resource-id,'helper_text') and (contains(@text,'Select 1 adult') or contains(@text,'Select 1 Adult') or contains(@text,'Pilih 1 dewasa') or contains(@text,'Pilih 1 Dewasa'))]"));
    public static final Locator CACHED_PASSENGER_EDIT = new Locator("CACHED_PASSENGER_EDIT", AppiumBy.xpath("(//*[contains(@resource-id,'group_field_passenger_selection')]//*[contains(@resource-id,'item_row')][.//*[contains(@resource-id,'edit_icon')]])[1]"));
    public static final Locator BAGGAGE_ADDON = new Locator("BAGGAGE_ADDON", AppiumBy.xpath("//*[contains(@resource-id,'tv_addon_title') and (contains(@text,'Bagasi') or contains(@text,'Baggage'))]//following-sibling::*//*[contains(@resource-id,'iv_add') or contains(@resource-id,'tv_add')] | //*[contains(@resource-id,'tv_add_checked_baggage') and (contains(@text,'Add') or contains(@text,'Tambah'))]"));
    public static final Locator BAGGAGE_ALLOWANCE_SECTION = new Locator("BAGGAGE_ALLOWANCE_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'tv_baggage_allowance_title') and (contains(@text,'Baggage Allowance') or contains(@text,'Alokasi Bagasi') or contains(@text,'Kuota Bagasi'))] | //*[@class='android.widget.TextView' and (contains(@text,'Baggage Allowance') or contains(@text,'Alokasi Bagasi') or contains(@text,'Kuota Bagasi'))]"));
    /** Baggage Protection / Insurance row on the new booking form (title text varies by locale/build). */
    public static final Locator BAGGAGE_INSURANCE_PROTECTION = new Locator("BAGGAGE_INSURANCE_PROTECTION", AppiumBy.xpath("//*[contains(@resource-id,'cl_protection')][.//*[contains(@resource-id,'tv_protection_title') and (contains(@text,'Baggage Insurance') or contains(@text,'Asuransi Bagasi') or contains(@text,'Baggage Protection') or contains(@text,'Proteksi Bagasi'))]]"));
    public static final Locator BAGGAGE_INSURANCE_CHECKBOX = new Locator("BAGGAGE_INSURANCE_CHECKBOX", AppiumBy.xpath("//*[contains(@resource-id,'cl_protection')][.//*[contains(@resource-id,'tv_protection_title') and (contains(@text,'Baggage Insurance') or contains(@text,'Asuransi Bagasi') or contains(@text,'Baggage Protection') or contains(@text,'Proteksi Bagasi'))]]//*[contains(@resource-id,'fl_check')]"));
    public static final Locator BAGGAGE_INSURANCE_CHECKED = new Locator("BAGGAGE_INSURANCE_CHECKED", AppiumBy.xpath("//*[contains(@resource-id,'cl_protection')][.//*[contains(@resource-id,'tv_protection_title') and (contains(@text,'Baggage Insurance') or contains(@text,'Asuransi Bagasi') or contains(@text,'Baggage Protection') or contains(@text,'Proteksi Bagasi'))]]//*[contains(@resource-id,'iv_check')]"));
    public static final Locator ADD_CHECKED_BAGGAGE_BUTTON = new Locator("ADD_CHECKED_BAGGAGE_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tv_add_checked_baggage') and (contains(@text,'Add') or contains(@text,'Tambah'))] | (//*[contains(@text,'Checked baggage') or contains(@text,'Checked Baggage') or contains(@text,'Bagasi tercatat')]/following::*[contains(@text,'Add') or contains(@text,'Tambah')])[1]"));
    public static final Locator BOOK_EXTRA_BAGGAGE_PAGE = new Locator("BOOK_EXTRA_BAGGAGE_PAGE", AppiumBy.xpath("//*[@class='android.widget.TextView' and (@text='Book Extra Baggage' or @text='Pesan Bagasi Extra' or contains(@text,'Extra Baggage') or contains(@text,'Bagasi Extra'))]"));
    public static final Locator BAGGAGE_DECLINE_OPTION = new Locator("BAGGAGE_DECLINE_OPTION", AppiumBy.xpath("//*[@class='android.widget.TextView' and (contains(@text,'No Thanks') or contains(@text,'No thanks') or contains(@text,'Tidak mau') or @text='0 Kg' or @text='0 KG')]"));
    /** One passenger block on Book Extra Baggage (used to count pax dynamically). */
    public static final Locator BAGGAGE_PASSENGER_SECTIONS = new Locator("BAGGAGE_PASSENGER_SECTIONS", AppiumBy.xpath("//*[contains(@resource-id,'tv_pax_number')]"));
    public static final Locator BAGGAGE_SAVE_BUTTON = new Locator("BAGGAGE_SAVE_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Save') or contains(@text,'Simpan'))]"));
    public static final Locator DOB_PICKER = new Locator("DOB_PICKER", AppiumBy.xpath("(//*[contains(@resource-id,'rv_form_fields') or contains(@resource-id,'view_flight_dynamic_form')]//*[contains(@resource-id,'tf_flight_booking_dynamic_date')][.//*[contains(@hint,'Date of Birth') or contains(@hint,'Tanggal Lahir')]])[1]"));
    public static final Locator DOB_SUBMIT = new Locator("DOB_SUBMIT", AppiumBy.id("tds_btn_submit_picker"));
    public static final Locator NATIONALITY = new Locator("NATIONALITY", AppiumBy.id("tf_flight_booking_dynamic_option_autocomplete"));
    public static final Locator NIK_PASSPORT_NUMBER = new Locator("NIK_PASSPORT_NUMBER", AppiumBy.xpath("//*[contains(@resource-id,'view_flight_dynamic_form')]//*[contains(@resource-id,'tds_box_input_text') and (contains(@hint,'NIK/Passport') or contains(@hint,'NIK/Paspor') or contains(@hint,'No. Paspor'))]"));
    public static final Locator ID = new Locator("ID", AppiumBy.xpath("//*[contains(@resource-id,'view_flight_dynamic_form')]//*[contains(@resource-id,'tds_box_input_text') and (contains(@hint,'NIK') or contains(@hint,'Paspor') or contains(@hint,'Passport') or contains(@hint,'KTP')) and not(contains(@hint,'Name') or contains(@hint,'Nama'))]"));
    public static final Locator BAGGAGE_ROUTE = new Locator("BAGGAGE_ROUTE", AppiumBy.id("cl_btn"));
    public static final Locator PRICE_CHEVRON = new Locator("PRICE_CHEVRON", AppiumBy.id("iv_chevron"));
    public static final Locator BAGGAGE_ADDON_PRICE = new Locator("BAGGAGE_ADDON_PRICE", AppiumBy.id("tv_subtotal_price"));
    public static final Locator SAVE_ADDON_BUTTON = new Locator("SAVE_ADDON_BUTTON", AppiumBy.id("tds_button_view"));
    public static final Locator CONTINUE = new Locator("CONTINUE", AppiumBy.xpath("//*[contains(@resource-id, 'tds_button_view')]"));
    public static final Locator FINISH_ADD_BAGGAGE_BUTTON = new Locator("FINISH_ADD_BAGGAGE_BUTTON", AppiumBy.xpath("//*[contains(@resource-id, 'tds_button_view') and (contains(@text, 'Baggage') or contains(@text, 'Bagasi'))]"));
    public static final Locator CONTINUE_PAYMENT_BUTTON = new Locator("CONTINUE_PAYMENT_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'btn_continue_to_payment')]//*[contains(@resource-id,'tds_button_view')] | //*[contains(@resource-id,'btn_flight_price_and_button')]//*[contains(@resource-id,'tds_button_view')]"));
    public static final Locator TRAVEL_REQUIREMENT_BANNER = new Locator("TRAVEL_REQUIREMENT_BANNER", AppiumBy.id("warning_banner"));
    public static final Locator TRAVEL_REQUIREMENT_CLOSE_BUTTON = new Locator("TRAVEL_REQUIREMENT_CLOSE_PAGE", AppiumBy.className("android.widget.ImageButton"));
    public static final Locator TRAVEL_REQUIREMENT_WEBVIEW = new Locator("TRAVEL_REQUIREMENT_CLOSE_PAGE", AppiumBy.xpath("//*[@class='info-page']"));
    public static final Locator PRICE_DETAIL_ARROW = new Locator("PRICE_DETAIL_ARROW", AppiumBy.id("iv_flight_price_and_button_arrow"));
    public static final Locator PRICE_DETAIL_PAY_NOW_CHEVRON = new Locator("PRICE_DETAIL_PAY_NOW_CHEVRON", AppiumBy.id("iv_pay_now_chevron"));
    public static final Locator PRICE_BREAKDOWN_TITLE = new Locator("PRICE_BREAKDOWN_TITLE", AppiumBy.id("tv_flight_booking_price_breakdown_title"));
    private static final String EXTRA_PROTECTION_LIST_XPATH = "//*[contains(@resource-id,'rv_flight_extra_protection')]/android.view.ViewGroup";
    public static final Locator EXTRA_PROTECTION_LIST = new Locator("EXTRA_PROTECTION_LIST", AppiumBy.id("rv_flight_extra_protection"));
    public static final Locator EXTRA_PROTECTION_PRE_ADDED_CHECK = new Locator("EXTRA_PROTECTION_PRE_ADDED_CHECK", AppiumBy.xpath("//*[contains(@resource-id,'rv_flight_extra_protection')]//*[contains(@resource-id,'iv_check')]"));
    public static final Locator EXTRA_PROTECTION_UNSELECTED_CHECK = new Locator("EXTRA_PROTECTION_UNSELECTED_CHECK", AppiumBy.xpath("//*[contains(@resource-id,'rv_flight_extra_protection')]//*[contains(@resource-id,'fl_check')][not(.//*[contains(@resource-id,'iv_check')])]"));
    public static final Locator PRICE_BREAKDOWN = new Locator("PRICE_BREAKDOWN", AppiumBy.xpath("//*[@class='android.widget.TextView' and contains(@resource-id, 'tv_flight_price_breakdown_detail_price')]"));
    public static final Locator PRICE_BREAKDOWN_LINE_ITEMS = new Locator("PRICE_BREAKDOWN_LINE_ITEMS", AppiumBy.xpath("//*[contains(@resource-id,'rv_flight_booking_price_breakdown')]//*[@class='android.widget.TextView' and (contains(@resource-id,'tv_pax_price') or contains(@resource-id,'tv_sub_row_price') or contains(@resource-id,'tv_discount_item_price'))]"));
    public static final Locator PRICE_BREAKDOWN_SUBTOTAL = new Locator("PRICE_BREAKDOWN_SUBTOTAL", AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_price_breakdown_detail_name') and (contains(@text,'Subtotal') or contains(@text,'Sub total') or contains(@text,'Sub-total'))]/following-sibling::*[contains(@resource-id,'tv_flight_price_breakdown_detail_price')] | //*[contains(@resource-id,'tv_flight_price_breakdown_detail_name') and (contains(@text,'Subtotal') or contains(@text,'Sub total') or contains(@text,'Sub-total'))]/parent::*//*[contains(@resource-id,'tv_flight_price_breakdown_detail_price')]"));
    public static final Locator CONFIRM_PAYMENT_BUTTON = new Locator("CONFIRM_PAYMENT_BUTTON", AppiumBy.id("tds_btn_primary_info_view"));
    public static final Locator BAGGAGE_ALLOWANCE = new Locator("BAGGAGE_ALLOWANCE", AppiumBy.xpath("//*[contains(@resource-id, 'tds_button_view')]"));
    public static final Locator SKIP_FULL_PROTECTION = new Locator("SKIP_FULL_PROTECTION", AppiumBy.xpath("//*[@data-testid='popup-button-secondary']"));
    /** Legacy skip CTA only — avoid loose contains(@text,'No'|'don') which match Done / unrelated copy. */
    public static final Locator SKIP_FULL_PROTECTION_NATIVE = new Locator("SKIP_FULL_PROTECTION", AppiumBy.xpath("//android.widget.Button[contains(@text,'Nggak') or contains(@text,\"don't\") or contains(@text,\"Don’t\") or starts-with(@text,'No,') or @text='No']"));
    /**
     * Flight Disruption Protection webview — EN: "Without protection" / ID: "Tanpa proteksi"
     * (see flightskipinsurancepage.xml + Indo PDP upsell). Scoped to WebView; exact label texts.
     */
    private static final Locator WITHOUT_PROTECTION_OPTION = new Locator("WITHOUT_PROTECTION_OPTION", AppiumBy.xpath("//android.webkit.WebView//*[@text='Without protection' or @text='Tanpa proteksi' or @text='Tanpa perlindungan']"));
    /** Radio sibling of Without / Tanpa proteksi row (label View is not clickable). */
    private static final Locator WITHOUT_PROTECTION_RADIO = new Locator("WITHOUT_PROTECTION_RADIO", AppiumBy.xpath("//android.webkit.WebView//*[@text='Without protection' or @text='Tanpa proteksi' or @text='Tanpa perlindungan']/parent::*/following-sibling::*[1]//android.widget.RadioButton"));
    /** EN: Continue without protection / ID: Lanjut tanpa proteksi (after selecting Tanpa proteksi). */
    private static final Locator CONTINUE_WITHOUT_PROTECTION_BTN = new Locator("CONTINUE_WITHOUT_PROTECTION_BTN", AppiumBy.xpath("//android.webkit.WebView//android.widget.Button[contains(@text,'Continue without') or contains(@text,'Lanjut tanpa') or contains(@text,'Lanjutkan tanpa') or @text='Continue without protection' or @text='Lanjut tanpa proteksi' or @text='Lanjutkan tanpa proteksi']"));
    /** WebView continue CTA after Without / Tanpa select — never "with/dengan proteksi". */
    private static final Locator CONTINUE_AFTER_SKIP_PROTECTION = new Locator("CONTINUE_AFTER_SKIP_PROTECTION", AppiumBy.xpath("//android.webkit.WebView//android.widget.Button[(starts-with(@text,'Continue') or starts-with(@text,'Lanjut')) and not(contains(@text,'with protection')) and not(contains(@text,'dengan proteksi')) and not(contains(@text,'dengan perlindungan'))]"));
    /**
     * Full-name input on the flight booking form (passenger details / contact details).
     * Multi-branch locator so it survives the form's two render modes:
     *   1) Empty / fresh form  ⇒ rendered as 'tds_box_autocomplete_input_text' (autocomplete suggestions enabled).
     *   2) Pre-filled / round-trip / saved-traveller flow ⇒ rendered as a plain 'box_input_text'
     *      EditText (no autocomplete), often shown below pre-selected Title/Phone fields.
     * All branches are scoped to the contact-form container ('view_flight_dynamic_form_bottom_sheet'
     * or 'tf_flight_booking_dynamic_text') and require the field to be the one whose hint/label
     * contains 'Nama'/'Name', so phone/email/passport inputs cannot accidentally satisfy it.
     */
    public static final Locator FULL_NAME = new Locator("FULL_NAME", AppiumBy.xpath(
            // Passenger Form bottom sheet — single full-name field (new variant).
            "(//*[contains(@resource-id,'view_flight_dynamic_form')]//*[contains(@resource-id,'tds_box_input_text') and (contains(@hint,'Full Name According to ID/Passport') or contains(@hint,'Nama Lengkap Sesuai KTP/Paspor') or contains(@hint,'Nama Lengkap Sesuai') or contains(@hint,'Nama Lengkap') or contains(@hint,'Full Name'))])[1]"
                    // Original autocomplete-input id (most stable when the autocomplete renders).
                    + " | (//*[contains(@resource-id,'tds_box_autocomplete_input_text')])[1]"
                    // Plain text-input rendered when the field is pre-filled / saved-traveller mode,
                    // scoped to a dynamic-form wrapper that also contains a 'Nama'/'Name' hint or label.
                    + " | (//*[contains(@resource-id,'view_flight_dynamic_form_bottom_sheet')"
                    + " or contains(@resource-id,'view_flight_dynamic_form')"
                    + " or contains(@resource-id,'tf_flight_booking_dynamic_text')]"
                    + "[.//*[contains(@hint,'Nama Lengkap') or contains(@hint,'Full Name')"
                    + " or contains(@hint,'Nama') or contains(@hint,'Name')"
                    + " or contains(@text,'Nama Lengkap') or contains(@text,'Full Name')]]"
                    + "//*[contains(@resource-id,'tds_box_input_text')"
                    + " or contains(@resource-id,'box_autocomplete_input_text')])[1]"
                    // Direct hint match on an EditText/AutoCompleteTextView inside the booking-form area.
                    + " | (//*[(self::android.widget.EditText or self::android.widget.AutoCompleteTextView)"
                    + " and (contains(@hint,'Nama Lengkap') or contains(@hint,'Full Name')"
                    + " or contains(@hint,'Nama') or contains(@hint,'Name'))])[1]"));
    public static final Locator PASSENGER_FULL_NAME = new Locator("PASSENGER_FULL_NAME", AppiumBy.xpath("//*[contains(@resource-id,'view_flight_dynamic_form')]//*[contains(@resource-id,'tds_box_input_text') and (contains(@hint,'Full Name According to ID/Passport') or contains(@hint,'Nama Lengkap Sesuai KTP/Paspor') or contains(@hint,'Nama Lengkap Sesuai') or contains(@hint,'Nama Lengkap') or contains(@hint,'Full Name'))]"));
    public static final Locator EMAIL = new Locator("EMAIL", AppiumBy.xpath("//*[contains(@resource-id,'view_flight_dynamic_form') or contains(@resource-id,'view_flight_dynamic_form_bottom_sheet')]//*[contains(@resource-id,'tf_flight_booking_dynamic_text')]//*[contains(@resource-id,'tds_box_input_text') and (contains(@hint,'Email') or contains(@hint,'email') or contains(@text,'Email') or contains(@text,'email'))]"));
    private static final Locator NO_SURNAME_CHECKBOX = new Locator("NO_SURNAME_CHECKBOX", AppiumBy.xpath("//*[contains(@resource-id,'cb_flight_booking_dynamic_text_with_checkbox')]//*[contains(@resource-id,'fl_check')]"));
    private static final Locator NO_SURNAME_CHECKED = new Locator("NO_SURNAME_CHECKED", AppiumBy.xpath("//*[contains(@resource-id,'cb_flight_booking_dynamic_text_with_checkbox')]//*[contains(@resource-id,'fl_check')]//*[contains(@resource-id,'iv_check')]"));
    public static final Locator PASSPORT = new Locator("PASSPORT", AppiumBy.xpath("//*[contains(@resource-id,'rv_form_fields') or contains(@resource-id,'view_flight_dynamic_form')]//*[contains(@resource-id,'tds_box_input_text') and (contains(@hint,'Passport') or contains(@hint,'Paspor'))]"));
    public static final Locator PASSPORT_DATE = new Locator("PASSPORT_DATE", AppiumBy.xpath("//*[contains(@resource-id,'rv_form_fields') or contains(@resource-id,'view_flight_dynamic_form')]//*[contains(@resource-id,'tds_box_input_text') and (contains(@hint,'Issued') or contains(@hint,'Pengeluaran'))]"));
    public static final Locator PASSPORT_EXPIRE = new Locator("PASSPORT_EXPIRE", AppiumBy.xpath("//*[contains(@resource-id,'rv_form_fields') or contains(@resource-id,'view_flight_dynamic_form')]//*[contains(@resource-id,'tds_box_input_text') and (contains(@hint,'Expiry') or contains(@hint,'Habis Berlaku'))]"));
    public static final Locator PASSPORT_COUNTRY = new Locator("PASSPORT_COUNTRY", AppiumBy.xpath("//*[contains(@resource-id,'rv_form_fields') or contains(@resource-id,'view_flight_dynamic_form')]//*[contains(@resource-id,'tds_box_input_text') and (contains(@hint,'Issuing Country') or contains(@hint,'Negara Penerbit') or contains(@hint,'Negara'))]"));
    public static final Locator LAST_NAME = new Locator("LAST_NAME", AppiumBy.xpath("((//*[contains(@resource-id,'view_flight_dynamic_form_bottom_sheet')])[1]//*[contains(@resource-id,'tds_box_input_text')])[1]"));
    public static final Locator ADD_FULL_PROTECTION = new Locator("ADD_FULL_PROTECTION", AppiumBy.xpath("//*[contains(@text,'Perlindungan Penuh') or contains(@text,'Full Protection')]//following-sibling::*[contains(@resource-id,'button')]"));
    public static final Locator FULL_PROTECTION_PRICE = new Locator("FULL_PROTECTION_PRICE", AppiumBy.xpath("//*[contains(@text,'Perlindungan Penuh') or contains(@text,'Full Protection')]//following-sibling::*[contains(@resource-id,'tv_price')]"));
    public static final Locator ADD_BAGGAGE_INSURANCE = new Locator("ADD_BAGGAGE_INSURANCE", AppiumBy.xpath("//*[contains(@text,'Asuransi Bagasi') or contains(@text,'Baggage Insurance') or contains(@text,'Baggage Protection') or contains(@text,'Proteksi Bagasi')]//following-sibling::*[contains(@resource-id,'button')]"));
    public static final Locator BAGGAGE_INSURANCE_PRICE = new Locator("BAGGAGE_INSURANCE_PRICE", AppiumBy.xpath("//*[contains(@text,'Asuransi Bagasi') or contains(@text,'Baggage Insurance') or contains(@text,'Baggage Protection') or contains(@text,'Proteksi Bagasi')]//following-sibling::*[contains(@resource-id,'tv_price')]"));
    public static final Locator PRICE_BREAKDOWN_CITY = new Locator("PRICE_BREAKDOWN_CITY", AppiumBy.id("tv_flight_price_breakdown_origin_city"));
    public static final Locator FLIGHT_PAX_SECTION = new Locator("FLIGHT_PAX_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'cl_flight_section_root')][.//*[contains(@resource-id,'tv_pax_label')]]"));
    public static final Locator MEALS_ADDON_TITLE = new Locator("MEALS_ADDON_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_addon_title') and (contains(@text,'Meals') or contains(@text,'Makanan'))]"));
    // Booking page: each add-on card has title + tv_add (Add/Tambah). Scope Add under the Meals/Makanan card.
    public static final Locator MEALS_ADDON = new Locator("MEALS_ADDON", AppiumBy.xpath("//*[contains(@resource-id,'cv_card_reskin_add_ons') or contains(@resource-id,'fl_card_reskin_add_ons')][.//*[contains(@resource-id,'tv_addon_title') and (contains(@text,'Meals') or contains(@text,'Makanan'))]]//*[contains(@resource-id,'tv_add') and (contains(@text,'Add') or contains(@text,'Tambah') or contains(@text,'Change') or contains(@text,'Ubah'))]"));
    // Legacy meal list used tv_add; new "Book Meals"/"Pesan Makanan" page uses tds_button_view Add/Tambah (toggles to Cancel/Batalkan).
    public static final Locator MEAL_ADD_ITEM = new Locator("MEAL_ADD_ITEM", AppiumBy.xpath("(//*[contains(@resource-id,'tv_add') and (contains(@text,'Tambah') or contains(@text,'Add'))])[1]"));
    public static final Locator MEAL_ADD_BUTTON = new Locator("MEAL_ADD_BUTTON", AppiumBy.xpath("(//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Add') or contains(@text,'Tambah'))])[1]"));
    public static final Locator MEAL_ALREADY_SELECTED = new Locator("MEAL_ALREADY_SELECTED", AppiumBy.xpath("(//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Cancel') or contains(@text,'Batalkan'))])[1]"));
    public static final Locator MEAL_ADD_BUTTON_ENABLED = new Locator("MEAL_ADD_BUTTON_ENABLED", AppiumBy.xpath("(//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Add') or contains(@text,'Tambah')) and @enabled='true'])[1]"));
    public static final Locator MEAL_ADD_BUTTON_DISABLED = new Locator("MEAL_ADD_BUTTON_DISABLED", AppiumBy.xpath("(//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Add') or contains(@text,'Tambah')) and @enabled='false'])[1]"));
    public static final Locator MEAL_SELECT_BUTTON = new Locator("MEAL_SELECT_BUTTON", AppiumBy.xpath("(//*[contains(@resource-id,'tds_button_view') and not(contains(@text,'Save')) and not(contains(@text,'Simpan')) and not(contains(@text,'Batalkan')) and not(contains(@text,'Cancel'))])[1]"));
    public static final Locator ADDON_SAVE_BUTTON = new Locator("ADDON_SAVE_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Save') or contains(@text,'Simpan'))]"));
    public static final Locator MEAL_SAVE_BUTTON = ADDON_SAVE_BUTTON;
    public static final Locator MEAL_TOTAL_VALUE = new Locator("MEAL_TOTAL_VALUE", AppiumBy.id("tv_select_meal_pax_total_value"));
    public static final Locator BOOK_MEALS_PAGE = new Locator("BOOK_MEALS_PAGE", AppiumBy.xpath("//*[contains(@resource-id,'tds_header_navigation_appbar') and (contains(@text,'Book Meals') or contains(@text,'Pesan Makanan'))]"));
    // RT Book Meals page: route tabs (e.g. CGK - DPS, DPS - CGK). Last tab is return.
    public static final Locator MEAL_RETURN_ROUTE_TAB = new Locator("MEAL_RETURN_ROUTE_TAB", AppiumBy.xpath("(//*[contains(@resource-id,'tab_layout_routes')]//*[contains(@resource-id,'tds_title_tab_layout')])[last()]"));
    public static final Locator YEAR_PICKER_WHEEL = new Locator("YEAR_PICKER_WHEEL", AppiumBy.xpath("(//*[contains(@resource-id,'tds_rv_right_picker')])[last()]"));
    public static final Locator CURRENT_YEAR_PICKER_WHEEL = new Locator("CURRENT_YEAR_PICKER_WHEEL", AppiumBy.xpath("((//*[contains(@resource-id,'tds_rv_right_picker')])[last()]//*[contains(@resource-id,'tds_body_picker')])[2]"));
    public static final Locator SEAT_ADDON_TITLE = new Locator("SEAT_ADDON_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_addon_title') and (contains(@text,'Seat') or contains(@text,'Kursi'))]"));
    public static final Locator SEAT_ADDON = new Locator("SEAT_ADDON", AppiumBy.xpath("//*[contains(@resource-id,'cv_card_reskin_add_ons') or contains(@resource-id,'fl_card_reskin_add_ons')][.//*[contains(@resource-id,'tv_addon_title') and (contains(@text,'Seat') or contains(@text,'Kursi'))]]//*[contains(@resource-id,'tv_add') and (contains(@text,'Add') or contains(@text,'Tambah') or contains(@text,'Change') or contains(@text,'Ubah'))]"));
    public static final Locator SEAT_SAVE_BUTTON = ADDON_SAVE_BUTTON;
    public static final Locator EMPTY_SEAT = new Locator("EMPTY_SEAT", AppiumBy.id("v_seat"));
    public static final Locator SEAT_CONTAINER = new Locator("SEAT_CONTAINER",AppiumBy.id("seat_map"));
    public static final Locator SEAT_TOTAL_VALUE = new Locator("SEAT_TOTAL_VALUE",AppiumBy.id("tv_select_seat_map_total_value"));
    public static final Locator ADD_COVID_19_INSURANCE = new Locator("COVID_19_INSURANCE", AppiumBy.xpath("//*[contains(@text,'Asuransi Covid-19') or contains(@text,'Covid-19 Insurance')]//following-sibling::*[contains(@resource-id,'button')]"));
    public static final Locator COVID_19_INSURANCE_PRICE = new Locator("COVID_19_INSURANCE_PRICE", AppiumBy.xpath("//*[contains(@text,'Asuransi Covid-19') or contains(@text,'Covid-19 Insurance')]//following-sibling::*[contains(@resource-id,'tv_price')]"));
    public static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", AppiumBy.xpath("//*[@class='android.view.ViewGroup' and (.//*[contains(@text,'Pay now') or contains(@text,'Pay Now')] or .//*[contains(@text,'Bayar sekarang')])]//android.widget.TextView[contains(@resource-id, 'tv_flight_price_breakdown_detail_price')]"));
    public static final Locator COVID19_ADDON = new Locator("COVID19_ADDON", AppiumBy.xpath("//*[contains(@resource-id,'tv_addon_title') and (contains(@text,'Tes COVID-19') or contains(@text,'COVID-19 Test'))]//following-sibling::*[contains(@resource-id,'action')]"));
    public static final Locator COVID19_CHECKBOX_AGREE_BUTTON = new Locator("COVID19_CHECKBOX_AGREE_BUTTON",AppiumBy.id("fl_check"));
    public static final Locator COVID19_TOTAL_VALUE = new Locator("COVID19_TOTAL_VALUE",AppiumBy.xpath("(//*[contains(@resource-id,'tv_price')])[last()]"));
    public static final Locator PRICE_CHANGE = new Locator("PRICE_CHANGE",AppiumBy.xpath("//*[contains(@resource-id,'tds_info_view_dialog') or contains(@resource-id,'tds_btn_primary_info_view')]//*[contains(@resource-id,'button')]"));
    public static final Locator DISCOUNT_TAG = new Locator("DISCOUNT_TAG", AppiumBy.xpath("//*[contains(@resource-id,'breakdown_detail_name') and (contains(@text,'Discount') or contains(@text,'Diskon'))] | //*[contains(@resource-id,'tv_discount_label') or contains(@resource-id,'tv_discount_item_label')]"));
    public static final Locator BENEFIT_SECTION = new Locator("BENEFIT_SECTION", AppiumBy.xpath("//*[contains(@name,'What You') or contains(@name,'Yang akan')]"));
    public static final Locator CAMPAIGN_OR_LOYALTY = new Locator("CAMPAIGN_OR_LOYALTY", AppiumBy.xpath("//*[contains(@name,'Diskon') or contains(@name,'Discount)' or @name='Harga Gledek']"));
    public static final Locator CANCEL_INSURANCE = new Locator("CANCEL_INSURANCE", AppiumBy.xpath("(//*[contains(@resource-id,'card_flight_extra_protection')]//*[contains(@resource-id,'tds_button_view') and ( contains(@text,'Cancel') or contains(@text,'Batalkan') ) ])"));
    public static final Locator PRICE_DETAIL_100P_REFUND = new Locator("PRICE_DETAIL_100P_REFUND", AppiumBy.xpath("(//*[contains(@resource-id,'tv_sub_row_label') and (contains(@text,'100%') or contains(@text,'refund 100% '))])"));
    public static final Locator YELLOW_BANNER_BOOKING_PAGE_2 = new Locator("YELLOW_BANNER_BOOKING_PAGE_2",AppiumBy.id("tds_content_notification_banner"));
    public static final Locator YELLOW_BANNER_BOOKING_PAGE_2_ICON = new Locator("YELLOW_BANNER_BOOKING_PAGE_2_ICON",AppiumBy.id("tds_icon_notification_banner"));
    public static final Locator TRIP_DETAIL_DATE_CARD = new Locator("TRIP_DETAIL_DATE_CARD", AppiumBy.id("tv_depart_info"));
    public static final Locator TRIP_DETAIL_RETURN_INFO = new Locator("TRIP_DETAIL_RETURN_INFO", AppiumBy.id("tv_return_info"));
    public static final Locator TRIP_DETAIL_PARENT= new Locator("TRIP_DETAIL_PARENT",AppiumBy.id("rv_multi_card"));
    public static final Locator ADDONS_AMOUNT_BAGGAGE= new Locator("ADDONS_AMOUNT_BAGGAGE",AppiumBy.id("tv_passenger_addons_value"));
    public static final Locator CANCEL_BTN_EXTRA_PROTECTION= new Locator("CANCEL_BTN_EXTRA_PROTECTION",AppiumBy.xpath("(//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Cancel') or contains(@text,'Batalkan'))])[1]"));
    public static final Locator ANCILLARIES_NOT_AVAILABLE_DEPART = new Locator("ANCILLARIES_NOT_AVAILABLE_DEPART",AppiumBy.xpath("//*[(contains(@text,'Return') or contains(@text,'Pulang'))]//parent::*//preceding-sibling::*//*[contains(@resource-id,'tv_addons_unavailable')]"));
    public static final Locator ANCILLARIES_NOT_AVAILABLE_RETURN = new Locator("ANCILLARIES_NOT_AVAILABLE_DEPART",AppiumBy.xpath("//*[(contains(@text,'Return') or contains(@text,'Pulang'))]//parent::*//following-sibling::*//*[contains(@resource-id,'tv_addons_unavailable')]"));
    public static final Locator ANCILLARIES_NOT_AVAILABLE_DEPART_BAGGAGE = new Locator("ANCILLARIES_NOT_AVAILABLE_DEPART_BAGGAGE",AppiumBy.xpath("//*[contains(@resource-id,'space')]//following-sibling::android.widget.TextView//preceding-sibling::*[contains(@resource-id,'cv_schedule_route')]//*[contains(@resource-id,'tv_addons_unavailable')]"));
    public static final Locator ANCILLARIES_NOT_AVAILABLE_RETURN_BAGGAGE = new Locator("ANCILLARIES_NOT_AVAILABLE_RETURN_BAGGAGE",AppiumBy.xpath("//*[contains(@resource-id,'space')]//following-sibling::android.widget.TextView//following-sibling::*[contains(@resource-id,'cv_schedule_route')]//*[contains(@resource-id,'tv_addons_unavailable')]"));
    public static final Locator ANCILLARIES_SELECT_DEPART = new Locator("ANCILLARIES_SELECT_DEPART",AppiumBy.xpath("//*[(contains(@text,'Return') or contains(@text,'Pulang'))]//parent::*//preceding-sibling::*//*[contains(@resource-id,'cl_btn')]//*[contains(@resource-id,'tv_btn_change') and (contains(@text,'Pilih') or contains(@text,'Select'))]"));
    public static final Locator ANCILLARIES_SELECT_RETURN = new Locator("ANCILLARIES_SELECT_DEPART",AppiumBy.xpath("//*[(contains(@text,'Return') or contains(@text,'Pulang'))]//parent::*//following-sibling::*//*[contains(@resource-id,'cl_btn')]//*[contains(@resource-id,'tv_btn_change') and (contains(@text,'Pilih') or contains(@text,'Select'))]"));
    public static final Locator ANCILLARIES_SELECT_DEPART_BAGGAGE = new Locator("ANCILLARIES_SELECT_DEPART_BAGGAGE",AppiumBy.xpath("//*[contains(@resource-id,'space')]//following-sibling::android.widget.TextView//preceding-sibling::*[contains(@resource-id,'cv_schedule_route')]//*[contains(@resource-id,'tv_btn_change') and (contains(@text,'Pilih') or contains(@text,'Select'))]"));
    public static final Locator ANCILLARIES_SELECT_RETURN_BAGGAGE = new Locator("ANCILLARIES_SELECT_RETURN_BAGGAGE",AppiumBy.xpath("//*[contains(@resource-id,'space')]//following-sibling::android.widget.TextView//following-sibling::*[contains(@resource-id,'cv_schedule_route')]//*[contains(@resource-id,'tv_btn_change') and (contains(@text,'Pilih') or contains(@text,'Select'))]"));
    public static final Locator TRIP_DETAIL_TIME = new Locator("TRIP_DETAIL_TIME",AppiumBy.id("tv_hour"));
    private static final Locator CLOSE_PRICE_DETAILS = new Locator("CLOSE_PRICE_DETAILS", AppiumBy.id("iv_flight_booking_price_breakdown_close"));
    private static final Locator PROMO_BANNER = new Locator("PROMO_BANNER", AppiumBy.xpath("//*[contains(@resource-id,'tds_banner')]//*[contains(@text,' promo regulations') or contains(@text,'promo dapat')]"));
    private static final Locator PROMO_BANNER_CHEVRON_2ND_PAGE = new Locator("PROMO_BANNER_CHEVRON_2ND_PAGE", AppiumBy.xpath("//*[contains(@resource-id,'cl_root')]"));
    private static final Locator REMOVE_PROMO = new Locator("REMOVE_PROMO", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Remove') or contains(@text,'Batalkan'))]"));
    private static final Locator CONNECTING_NUDGE = new Locator("CONNECTING_NUDGE", AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_nudges') and contains(@text, 'Proteksi Transit Mandiri') or contains(@text, 'Self Transfer Protection')]"));
    private static final Locator CONNECTING_FLIGHT_PROTECTION_NUDGE = new Locator("CONNECTING_FLIGHT_PROTECTION_NUDGE", AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_nudges') and (contains(@text,'Protected by Connecting Flight Protection') or contains(@text,'Terlindungi oleh Connecting Flight Protection'))]"));
    private static final Locator AIRLINE_ICON = new Locator("AIRLINE_ICON", AppiumBy.id("iv_airline_logo"));
    private static final Locator MORE_BENEFIT = new Locator("MORE_BENEFIT", AppiumBy.xpath("//*[contains(@resource-id,'tv_description') and (contains(@text,'More benefit') or contains(@text,'Lihat benefit lainnya'))]"));
    private static final Locator INSURANCE_DETAIL_TITLE = new Locator("INSURANCE_DETAIL_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_base_bottom_sheet_protection_title')]"));
    private static final Locator INSURANCE_DETAIL_COVERAGE_HEADER = new Locator("INSURANCE_DETAIL_COVERAGE_HEADER", AppiumBy.id("tv_coverage_header"));
    private static final Locator INSURANCE_DETAIL_COMPENSATION_HEADER = new Locator("INSURANCE_DETAIL_COMPENSATION_HEADER", AppiumBy.id("tv_compensation_header"));
    private static final Locator INSURANCE_DETAIL_COVERAGE = new Locator("INSURANCE_DETAIL_COVERAGE", AppiumBy.id("tv_coverage"));
    private static final Locator INSURANCE_DETAIL_COMPENSATION = new Locator("INSURANCE_DETAIL_COMPENSATION", AppiumBy.id("tv_compensation"));
    private static final Locator YELLOW_BANNER_CONNECTING_FLIGHT_PROTECTION = new Locator("YELLOW_BANNER_CONNECTING_FLIGHT_PROTECTION", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_notification_banner') and (contains(@text,'You chose connecting flights') or contains(@text,'Info penerbangan multimaskapai'))]"));
    public static final Locator AIRPORT_TRANSFER_DETAIL = new Locator("AIRPORT_TRANSFER_DETAIL", AppiumBy.xpath("//*[contains(@resource-id, 'tv_title') and (contains(@text,'Free Airport Transfer') or contains(@text,'Gratis Airport Transfer'))]"));
    public static final Locator ANTIGEN_TEST_DETAIL = new Locator("ANTIGEN_TEST_DETAIL", AppiumBy.xpath("//*[contains(@resource-id, 'tv_title') and contains(@text, 'PCR/Antigen')]"));
    public static final Locator WHAT_YOU_GET = new Locator("WHAT_YOU_GET", AppiumBy.xpath("//*[@class='android.widget.LinearLayout' and ./*[contains(@resource-id, 'rv_flight_booking_what_will_you_get')]]"));
    public static final Locator PASSPORT_POPUP = new Locator("PASSPORT_POPUP", AppiumBy.xpath("//*[contains(@resource-id, 'tds_txt_body_info_view')]"));
    public static final Locator CHECK_PASSENGER_DETAILS = new Locator("CHECK_PASSENGER_DETAILS", AppiumBy.xpath("//*[contains(@resource-id, 'tds_button_view') and contains(@text, 'Cek detail penumpang') or contains(@text, 'Check Again')]"));
    public static final Locator PASSPORT_BANNER = new Locator("PASSPORT_BANNER", AppiumBy.xpath("//*[contains(@resource-id,\"iv_icon\")]"));
    public static final Locator OPEN_PASSENGER_DETAILS = new Locator("OPEN_PASSENGER_DETAILS", AppiumBy.xpath("//*[contains(@resource-id,'edit_icon')]"));
    public static final Locator CLOSE_PASSENGER_DETAILS = new Locator("CLOSE_PASSENGER_DETAILS", AppiumBy.xpath("//*[contains(@resource-id, 'iv_flight_edit_passenger_close')] | //*[contains(@resource-id,'crf_bottom_sheet')][.//*[contains(@resource-id,'tv_title') and (contains(@text,'Passenger Form') or contains(@text,'Formulir Penumpang') or contains(@text,'Data Penumpang'))]]//*[contains(@resource-id,'iv_close')]"));
    public static final Locator CHECK_PASSENGER = new Locator("CHECK_PASSENGER", AppiumBy.xpath("//*[contains(@resource-id, 'tds_button_view') and (contains(@text, 'Check Again') or contains(@text, 'Check passenger & baggage details') or contains(@text, 'Cek detail penumpang & bagasi'))]"));
    public static final Locator GREEN_BANNER_UNDER_CONTINUE_BTN = new Locator("GREEN_BANNER_UNDER_CONTINUE_BTN", AppiumBy.id("tv_title_cashback"));
    public static final Locator CASHBACK_TEXT = new Locator("CASHBACK_TEXT", AppiumBy.xpath("//*[contains(@text, 'Cashback')]"));
    public static final Locator DEPARTURE_TAB = new Locator("DEPARTURE_TAB", AppiumBy.xpath("(//*[contains(@resource-id, 'cl_flight_detail_area')])[1]"));
    public static final Locator CLICK_DETAILS = new Locator("CLICK_DETAILS", AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_pdp_refund_reschedule_detail')]"));
    public static final Locator DEPARTURE_FLIGHT = new Locator("DEPARTURE_FLIGHT", AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_body') and contains(@text, 'Departure Flight') or contains(@text, 'Penerbangan Pergi')]"));
    public static final Locator REFUND_PERCENTAGE = new Locator("REFUND_PERCENTAGE", AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_penalty_info_title') and contains(@text, 'refund')]"));
    public static final Locator CLOSE_TAB = new Locator("CLOSE_TAB", AppiumBy.xpath("//*[contains(@resource-id, 'iv_flight_policies_page_close')]"));
    public static final Locator RESCHEDULE_CHIP = new Locator("RESCHEDULE_CHIP", AppiumBy.xpath("//*[contains(@resource-id, 'tds_title_tab_layout') and contains(@text,'Reschedule')]"));
    public static final Locator RESCHEDULE_PERCENTAGE = new Locator("RESCHEDULE_PERCENTAGE", AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_penalty_info_title') and contains(@text, 'reschedule')]"));
    public static final Locator RETURN_TAB = new Locator("RETURN_TAB", AppiumBy.xpath("(//*[contains(@resource-id, 'cl_flight_detail_area')])[2]"));
    public static final Locator WHOLE_TAB = new Locator("WHOLE_TAB", AppiumBy.xpath("//*[contains(@resource-id, 'rv_multi_card')]"));
    public static final Locator CLOSE_TAB_AGAIN = new Locator("CLOSE_TAB_AGAIN", AppiumBy.xpath("//*[contains(@resource-id, 'iv_flight_detail_page_close')]"));
    public static final Locator RETURN_FLIGHT = new Locator("RETURN_FLIGHT", AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_body') and contains(@text, 'Return Flight') or contains(@text, 'Penerbangan Pulang')]"));
    public static final Locator REFUND = new Locator("REFUND", AppiumBy.xpath("(//*[contains(@resource-id,'tv_flight_penalty_info_not_available_title') and (contains(normalize-space(@text),'Refund') or contains(normalize-space(@text),'refund'))])[1]"));
    public static final Locator RESCHEDULE = new Locator("RESCHEDULE", AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_penalty_info_not_available_title') and (contains(normalize-space(@text),'Reschedule') or contains(normalize-space(@text),'reschedule'))]"));
    public static final Locator BAGGAGE_POPUP = new Locator("BAGGAGE_POPUP", AppiumBy.xpath("//*[contains(@resource-id, 'tds_button_view')]"));
    public static final Locator CASHBACK = new Locator("CASHBACK", AppiumBy.xpath("//*[contains(@resource-id,'tv_cashback_label') and contains(@text,'Cashback')] | //*[contains(@resource-id,'tv_flight_price_breakdown_detail_name') and contains(@text,'Cashback')]"));
    public static final Locator CASHBACK_PRICE_VALUE = new Locator("CASHBACK_PRICE_VALUE", AppiumBy.xpath(
            "(//*[contains(@resource-id,'tv_cashback_total')])[1]"
                    + " | (//*[contains(@resource-id,'tv_flight_price_breakdown_detail_name') and contains(@text,'Cashback')]"
                    + "/following-sibling::*[contains(@resource-id,'tv_flight_price_breakdown_detail_price')])[1]"
                    + " | (//*[contains(@resource-id,'tv_flight_price_breakdown_detail_name') and contains(@text,'Cashback')]"
                    + "/parent::*//*[contains(@resource-id,'tv_flight_price_breakdown_detail_price')])[1]"));
    public static final Locator TOTAL_AFTER_CASHBACK_PRICE = new Locator("TOTAL_AFTER_CASHBACK_PRICE", AppiumBy.xpath(
            "(//*[contains(@resource-id, 'tv_flight_price_breakdown_detail_name') and (contains(@text, 'Total after cashback') or contains(@text, 'Total setelah cashback'))]"
                    + "/following-sibling::*[contains(@resource-id, 'tv_flight_price_breakdown_detail_price')])[1]"
                    + " | (//*[contains(@resource-id, 'tv_flight_price_breakdown_detail_name') and (contains(@text, 'Total after cashback') or contains(@text, 'Total setelah cashback'))]"
                    + "/parent::*//*[contains(@resource-id, 'tv_flight_price_breakdown_detail_price')])[1]"));
    public static final Locator BAGGAGE_OPTIONS = new Locator("BAGGAGE_OPTIONS", AppiumBy.xpath("//*[(contains(@resource-id,'tv_value_weight') and contains(@text,'KG')) or (contains(@resource-id,'tv_weight') and contains(@text,'kg')) or (contains(@text,'+') and contains(translate(@text,'KG','kg'),'kg'))]"));
    public static final Locator CONTINUE_POPUP = new Locator("CONTINUE_POPUP", AppiumBy.xpath("//*[contains(@resource-id, 'tds_button_view') and (contains(@text,'Continue') or contains(@text,'Lanjutkan'))]"));
    public static final Locator PASSPORT_NUMBER = new Locator("PASSPORT_NUMBER", AppiumBy.xpath("//*[contains(@resource-id,'rv_form_fields') or contains(@resource-id,'view_flight_dynamic_form')]//*[contains(@resource-id,'tds_box_input_text') and (contains(@hint,'Passport') or contains(@hint,'Paspor'))]"));
    private static final Locator ERROR_POPUP = new Locator("ERROR_POPUP", AppiumBy.xpath("//*[contains(@label,'Sorry') or contains(@label,'Maaf')]"));
    private static final Locator CONTENT_LOADING_DIALOG = new Locator("CONTENT_LOADING_DIALOG", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_content_loading_dialog')]"));

    public FlightBookingFormPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public boolean isTripDetailCorrect(String tripType, String departureDate, String departureAirport, String arrivalDate, String arrivalAirport) {
        skipFullProtection();
        driverTools.waitForElementToAppear(TRIP_DETAIl);
        List<WebElement> tripDetails = driverTools.elements(TRIP_DETAIl).orElseThrow();

        if (tripType.equals("depart")) {
            driverTools.click(tripDetails.get(0));
        } else if (tripType.equals("return")) {
            driverTools.click(tripDetails.get(1));
        }

        logger.log("Verifying trip detail same as on search result");
        boolean correctDepartureDate = driverTools.getText(DEPARTURE_DATE).orElseThrow().equals(departureDate);
        boolean correctDepartureAirport = driverTools.getText(DEPARTURE_AIRPORT).orElseThrow().equals(departureAirport);
        boolean correctArrivalDate = driverTools.getText(ARRIVAL_DATE).orElseThrow().equals(arrivalDate);
        boolean correctArrivalAirport = driverTools.getText(ARRIVAL_AIRPORT).orElseThrow().equals(arrivalAirport);

        driverTools.click(TRIP_DETAIl_BACK);
        driverTools.waitForElementToAppear(TRIP_DETAIl);
        
        return correctDepartureDate && correctDepartureAirport && correctArrivalDate && correctArrivalAirport;
    }

    @Override
    public boolean checkPassportPopup(){
        driverTools.swipeToElement(CONTINUE_PAYMENT_BUTTON,Swipe.UP,15);
        logger.log("Continue to payment");
        driverTools.click(CONTINUE_PAYMENT_BUTTON);
        boolean result = driverTools.isElementDisplayed(PASSPORT_POPUP) && driverTools.isElementDisplayed(CHECK_PASSENGER_DETAILS);
        if (result) {
            logger.log("Click on passenger details");
            driverTools.click(CHECK_PASSENGER_DETAILS);
        }
        return result;
    }

    @Override
    public boolean checkPassportBanner(){
        logger.log("Opening passenger detail form");
        driverTools.click(OPEN_PASSENGER_DETAILS);
        driverTools.waitForElementToAppear(PASSPORT_BANNER, 20);
        boolean result = driverTools.isElementDisplayed(PASSPORT_BANNER);
        driverTools.click(driverTools.isElementDisplayed(PASSENGER_FORM) ? PASSENGER_FORM_BACK : CLOSE_PASSENGER_DETAILS);
        return result;
    }

    @Override
    public boolean isExistingPassengerDisplayed() {
        return driverTools.waitForElementToAppear(PASSENGER_PROFILE, 5).isPresent()
                || driverTools.isElementDisplayed(SAVED_PASSENGER_NAME);
    }

    @Override
    public void selectCachedPassengerWithoutCompleting(String passengerName) {
        String methodName = "selectCachedPassengerWithoutCompleting";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForElementToAppear(CONTINUE_BUTTON, 10);
            dismissOpenBottomSheetIfPresent();
            if (isSavedPassengerMode() || isExistingPassengerDisplayed()) {
                selectCachedProfileWithoutCompleting(passengerName);
                return;
            }
            selectCachedPassengerViaAutocompleteWithoutCompleting(passengerName);
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void selectAnySavedPassenger() {
        int index = 1;
        driverTools.waitForElementToAppear(CONTINUE_BUTTON, 10);
        dismissOpenBottomSheetIfPresent();
        Locator firstPassenger = savedPassengerNameAt(index);
        driverTools.swipeToElement(firstPassenger, Swipe.UP, 3)
                .orElseThrow(() -> new NoSuchElementException("No saved passenger found on booking form"));
        String passengerName = driverTools.getText(firstPassenger).orElse("");
        logger.log("Selecting saved passenger: " + passengerName);
        tapSavedPassengerCheckbox(savedPassengerCheckboxAt(index));
    }

    @Override
    public boolean areIssuingDateAndCountryEmpty() {
        logger.log("Opening passenger form to verify issuing date/country are empty");
        if (!isAnyPassengerFormOpen()) {
            if (driverTools.waitForElementToAppear(CACHED_PASSENGER_EDIT, 3).isPresent()) {
                driverTools.click(CACHED_PASSENGER_EDIT);
            } else if (driverTools.waitForElementToAppear(OPEN_PASSENGER_DETAILS, 3).isPresent()) {
                driverTools.click(OPEN_PASSENGER_DETAILS);
            } else if (driverTools.isElementDisplayed(PASSENGER_PROFILE) || driverTools.isElementDisplayed(SAVED_PASSENGER_NAME)) {
                driverTools.click(savedPassengerNameAt(1));
            } else {
                openPassengerFromList(1);
            }
            waitForDynamicFormToOpen();
        }
        driverTools.swipeToElement(PASSPORT_DATE, Swipe.UP, 5);
        boolean issuingDateEmpty = isPassportFieldEmpty(PASSPORT_DATE, "issued", "pengeluaran", "date", "tanggal");
        driverTools.swipeToElement(PASSPORT_COUNTRY, Swipe.UP, 4);
        boolean issuingCountryEmpty = isPassportFieldEmpty(PASSPORT_COUNTRY, "issuing country", "negara penerbit");
        logger.log("Issuing date empty=" + issuingDateEmpty + ", issuing country empty=" + issuingCountryEmpty);
        dismissOpenPassengerFormIfBlocking();
        return issuingDateEmpty && issuingCountryEmpty;
    }

    @Override
    public boolean isBlockedByPassengerSelectionValidation() {
        logger.log("Tap Continue on BF page 1 and expect passenger selection helper error");
        WebElement continueBtn = driverTools.swipeToElement(CONTINUE_BUTTON, Swipe.UP, 5)
                .orElseGet(() -> driverTools.waitForElementToAppear(CONTINUE_BUTTON, 5).orElseThrow());
        driverTools.click(continueBtn);
        boolean helperError = driverTools.waitForElementToAppear(PASSENGER_SELECTION_HELPER_ERROR, 5).isPresent()
                || driverTools.isElementDisplayed(PASSENGER_SELECTION_HELPER_ERROR);
        boolean stillOnPageOne = driverTools.isElementDisplayed(BOOKING_PAGE_ONE_INDICATOR)
                && !driverTools.isElementDisplayed(BOOKING_PAGE_TWO_INDICATOR);
        logger.log("Passenger selection helper error=" + helperError + ", still on page 1/2=" + stillOnPageOne);
        return helperError && stillOnPageOne;
    }

    /**
     * Selects a cached/saved profile by name and closes any auto-opened incomplete form without filling
     * issuing date / issuing country.
     */
    private void selectCachedProfileWithoutCompleting(String passengerName) {
        String target = normalizePassengerName(passengerName);
        int total = driverTools.elements(SAVED_PASSENGER_NAME).map(List::size).orElse(0);
        for (int i = 1; i <= total; i++) {
            Locator nameAt = savedPassengerNameAt(i);
            driverTools.swipeToElement(nameAt, Swipe.UP, 3);
            String labelName = normalizePassengerName(driverTools.getText(nameAt).orElse(""));
            if (labelName.isEmpty() || (!labelName.contains(target) && !target.contains(labelName))) {
                continue;
            }
            // Guest cache UI uses area_selection_control / item_row (no fl_check/iv_check).
            tapSavedPassengerCheckbox(savedPassengerCheckboxAt(i));
            if (driverTools.waitForElementToAppear(savedPassengerCheckboxSelectedAt(i), 2).isEmpty()) {
                driverTools.element(savedPassengerRowAt(i)).ifPresent(driverTools::click);
            }
            if (waitForAnyPassengerForm(3)) {
                logger.log("Incomplete passenger form auto-opened for cached pax; closing without completing issuing fields");
                dismissOpenPassengerFormIfBlocking();
            }
            return;
        }
        throw new NoSuchElementException("Cached passenger not found on booking form: " + passengerName);
    }

    private String normalizePassengerName(String name) {
        if (name == null) {
            return "";
        }
        return name.trim()
                .replaceAll("(?i)^(mrs\\.?|mr\\.?|ms\\.?|miss|mrs|mr)\\s+", "")
                .replaceAll("\\s*\\([^)]*\\)\\s*$", "")
                .trim()
                .toLowerCase();
    }

    /**
     * Guest flow: open Passenger 1, trigger name autocomplete from app cache, select suggestion, save
     * without filling international issuing fields.
     */
    private void selectCachedPassengerViaAutocompleteWithoutCompleting(String passengerName) {
        openPassengerFromList(1);
        clearPassengerFormIfShown();
        String query = passengerName == null ? "" : passengerName.trim().split("\\s+")[0];
        Locator nameField = getFullNameFieldLocator();
        if (!fillTextField(nameField, query)) {
            throw new NoSuchElementException("Name field not available to select cached passenger: " + passengerName);
        }
        Locator suggestion = passengerNameSuggestion(passengerName);
        WebElement suggestionEl = driverTools.waitForElementToAppear(suggestion, 8)
                .or(() -> driverTools.waitForElementToAppear(passengerNameSuggestion(query), 3))
                .orElseThrow(() -> new NoSuchElementException("Cached passenger suggestion not shown for: " + passengerName));
        driverTools.click(suggestionEl);
        if (driverTools.isElementDisplayed(TITLE_RADIO)) {
            fillTitle();
        }
        Locator saveButton = getActiveFormSaveButton();
        WebElement saveBtn = driverTools.element(saveButton)
                .filter(WebElement::isDisplayed)
                .orElseGet(() -> driverTools.swipeToElement(saveButton, Swipe.UP, 2).orElseThrow());
        driverTools.click(saveBtn);
        confirmPassengerReviewIfShown();
        if (isAnyPassengerFormOpen()) {
            logger.log("Passenger form still open after selecting cached pax (validation may be showing)");
        }
    }

    private Locator passengerNameSuggestion(String passengerName) {
        String safe = passengerName == null ? "" : passengerName.trim();
        return new Locator("PASSENGER_NAME_SUGGESTION", AppiumBy.xpath(
                "//*[contains(@resource-id,'tds_body_list') or contains(@resource-id,'item_title') or contains(@resource-id,'tv_title') or contains(@resource-id,'title_text')]"
                        + "[contains(@text,'" + safe + "')]"
                        + " | //*[@class='android.widget.TextView' and contains(@text,'" + safe + "')"
                        + " and not(contains(@resource-id,'tds_box_input_text')) and not(contains(@resource-id,'tds_box_autocomplete_input_text'))]"));
    }

    /**
     * Fills one passenger detail form and commits it (mirrors iOS {@code passengerDomestic}).
     * The form is assumed to be already open (opened from the passenger list, or auto-opened by
     * the previous "Next passenger" confirmation).
     */
    private String passengerDomestic(int dobRange, String airline) {
        logger.log("Fill domestic passenger data");
        waitForDynamicFormToOpen();
        String name = usesSplitNameFields() ? fillPassengerNames(airline) : fillName(airline);
        fillTitle();
        if (driverTools.isElementDisplayed(DOB_PICKER)) {
            fillDob(dobRange);
        }
        if (driverTools.isElementDisplayed(NATIONALITY)) {
            fillNationality();
        }
        if (driverTools.isElementDisplayed(NIK_PASSPORT_NUMBER) || driverTools.isElementDisplayed(ID)) {
            fillId();
        }
        submitPassenger("domestic");
        return name;
    }

    /**
     * Fills one international passenger detail form and commits it (mirrors iOS {@code passengerInternational}).
     */
    private String passengerInternational(int dobRange, String airline) {
        logger.log("Fill international passenger data");
        waitForDynamicFormToOpen();
        clearPassengerFormIfShown();
        String name = fillPassengerNames(airline);
        fillTitle();
        if (driverTools.isElementDisplayed(DOB_PICKER)) {
            fillDob(dobRange);
        }
        if (driverTools.isElementDisplayed(NATIONALITY)) {
            fillNationality();
        }
        fillInternationalIdentityInfo();
        submitPassenger("international");
        return name;
    }

    private void fillInternationalIdentityInfo() {
        driverTools.swipeToElement(PASSPORT_NUMBER, Swipe.UP, 4);
        if (driverTools.waitForElementToPresence(PASSPORT_NUMBER).isPresent()){
            fillPassportNumber();
            fillPassportDateField(PASSPORT_DATE, "before");
            fillPassportDateField(PASSPORT_EXPIRE, "next");
            fillIssuingCountry();
        }
    }

    public String chooseDomesticOrInternational(String flightRoute, int dobRange, String airline) {
        String name = "";
        if (flightRoute.equals("domestic")) {
            name += passengerDomestic(dobRange, airline);
        } else if (flightRoute.equals("international")) {
            name += passengerInternational(dobRange, airline);
        }
        return name;
    }

    /**
     * Commits the current passenger form and advances the flow (mirrors iOS {@code submitPassenger}).
     * <p>New guest UI: the bottom action button is "Next passenger" for every passenger except the last
     * (which shows "Save"). After tapping it, a name-review bottom sheet ("Review Passenger" +
     * "It's correct"/"Next passenger") may appear; confirming it auto-opens the next passenger's form,
     * which {@link #processPassenger} then fills without reopening it from the passenger list. If the
     * form stays open after saving (missing identity data), the remaining fields are filled and saved again.
     */
    private void submitPassenger(String flightRoute) {
        Locator saveButton = getActiveFormSaveButton();
        WebElement saveBtn = driverTools.element(saveButton)
                .filter(WebElement::isDisplayed)
                .orElseGet(() -> driverTools.swipeToElement(saveButton, Swipe.UP, 2).orElseThrow());
        driverTools.click(saveBtn);
        if (!waitForPassengerFormToClose(15)) {
            logger.log("Passenger form still visible after save, filling remaining identity fields");
            if (flightRoute.equals("international")) {
                fillInternationalIdentityInfo();
            }
            saveBtn = driverTools.element(getActiveFormSaveButton())
                    .filter(WebElement::isDisplayed)
                    .orElseGet(() -> driverTools.swipeToElement(getActiveFormSaveButton(), Swipe.UP, 2).orElseThrow());
            driverTools.click(saveBtn);
            waitForPassengerFormToClose(15);
        }
        confirmPassengerReviewIfShown();
    }

    /**
     * Fills a single passenger (mirrors iOS {@code processPassenger}).
     * <p>New guest UI: confirming a passenger with "Next passenger" auto-opens the next passenger's
     * form (it may take a moment to animate in). Only the first passenger is opened from the list;
     * for later passengers, wait for the auto-opened form and fill it directly. If it never appears
     * (older builds that return to the list), fall back to opening from the passenger list.
     */
    private void processPassenger(int index, String flightRoute, int dobRange, String airline, List<String> passengerNames) {
        boolean formAlreadyOpen = index > 1 && waitForAnyPassengerForm(8);
        if (!formAlreadyOpen) {
            openPassengerFromList(index);
        }
        String passengerName = chooseDomesticOrInternational(flightRoute, dobRange, airline);
        passengerNames.add(passengerName);
    }

    /**
     * Opens the Nth passenger's form from the passenger list (mirrors iOS {@code openPassengerFromList}).
     * The row may be scrolled off-screen after the previous passenger was saved, so {@code element()}
     * (which does not scroll) can miss it; swipe the row into view before clicking. Any leftover dialog,
     * review sheet or previously open form is dismissed first.
     */
    private void openPassengerFromList(int index) {
        dismissLeaveBookingDialogIfShown();
        confirmPassengerReviewIfShown();
        dismissLeaveBookingDialogIfShown();
        dismissOpenPassengerFormIfBlocking();
        Optional<WebElement> row = driverTools.element(getPassengerLocator(index)).filter(WebElement::isDisplayed);
        if (row.isEmpty()) {
            row = driverTools.swipeToElement(getPassengerLocator(index), Swipe.UP, 5);
        }
        if (row.isEmpty()) {
            row = driverTools.swipeToElement(getPassengerLocatorID(index), Swipe.UP, 5);
        }
        driverTools.click(row.orElseThrow(() -> new NoSuchElementException("Passenger selection not found for index " + index)));
        waitForDynamicFormToOpen();
    }

    private void dismissOpenPassengerFormIfBlocking() {
        if (!isAnyPassengerFormOpen()) {
            return;
        }
        logger.log("Closing open passenger form before selecting next passenger");
        if (driverTools.isElementDisplayed(PASSENGER_FORM_SHEET_CLOSE)) {
            driverTools.click(PASSENGER_FORM_SHEET_CLOSE);
        } else if (driverTools.isElementDisplayed(PASSENGER_FORM_BACK)) {
            driverTools.click(PASSENGER_FORM_BACK);
        } else {
            driverTools.click(CLOSE_BUTTON);
        }
        waitForPassengerFormToClose(10);
    }

    /**
     * Login users book from a saved passenger list (checkboxes / profile rows) rather than the guest
     * "Passenger N" forms (mirrors iOS {@code isSavedPassengerMode}). If the guest "Passenger 1" row
     * is present this is the guest flow.
     */
    private boolean isSavedPassengerMode() {
        if (driverTools.isElementDisplayed(getPassengerLocator(1))) {
            return false;
        }
        return driverTools.isElementDisplayed(SAVED_PASSENGER_CHECKBOX_ANY)
                || driverTools.isElementDisplayed(VIEW_OR_ADD_PASSENGERS)
                || driverTools.isElementDisplayed(PASSENGER_PROFILE);
    }

    /**
     * Selects the required passengers from the saved list by tapping their checkboxes, matching the
     * requested passenger types (Adult / Child / Infant) (mirrors iOS {@code selectSavedPassengers}).
     * Immediately after a passenger is selected, if its form auto-opens with missing identity info the
     * data is completed (form filled + review bottom sheet confirmed) before the next passenger is
     * selected. Passengers already selected that are not needed are de-selected.
     */
    private List<String> selectSavedPassengers(int adult, int child, int infant, String flightRoute) {
        List<String> names = new ArrayList<>();
        // Remaining slots per type: index 0 = adult, 1 = child, 2 = infant.
        int[] remaining = {adult, child, infant};

        int total = driverTools.elements(SAVED_PASSENGER_NAME).map(List::size).orElse(0);
        for (int i = 1; i <= total; i++) {
            Locator nameAt = savedPassengerNameAt(i);
            driverTools.swipeToElement(nameAt, Swipe.UP, 3);
            String label = driverTools.getText(nameAt).orElse("");
            if (label.isEmpty()) {
                continue;
            }

            int type = (label.contains("(Infant)") || label.contains("(Bayi)")) ? 2
                    : (label.contains("(Child)") || label.contains("(Anak)")) ? 1 : 0;
            boolean selected = driverTools.isElementDisplayed(savedPassengerCheckboxSelectedAt(i));

            if (remaining[type] > 0) {
                if (!selected) {
                    ensureSavedPassengerSelected(i);
                }
                fillSavedPassengerIfIncomplete(label, flightRoute);
                remaining[type]--;
                names.add(label);
            } else if (selected) {
                tapSavedPassengerCheckbox(savedPassengerCheckboxAt(i));
            }
        }

        return names;
    }

    /**
     * Selects via clickable {@code area_selection_control}; retries via {@code item_row} if checkmark
     * does not appear ({@code fl_check} alone is not clickable).
     */
    private void ensureSavedPassengerSelected(int index) {
        tapSavedPassengerCheckbox(savedPassengerCheckboxAt(index));
        if (driverTools.waitForElementToAppear(savedPassengerCheckboxSelectedAt(index), 3).isPresent()) {
            return;
        }
        logger.log("Retry selecting saved passenger via item_row at index " + index);
        driverTools.element(savedPassengerRowAt(index)).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(savedPassengerCheckboxSelectedAt(index), 3);
    }

    /**
     * When a saved passenger with missing identity info is selected, tapping its checkbox auto-opens
     * the passenger form (mirrors iOS {@code fillSavedPassengerIfIncomplete}). If that form opens, fill
     * the missing data, save it and confirm the review bottom sheet so the passenger is completed before
     * the next one is selected. If no form opens, the passenger was already complete.
     */
    private void fillSavedPassengerIfIncomplete(String passengerName, String flightRoute) {
        // Short probe: when a saved passenger is incomplete its form auto-opens near-instantly; if it does
        // not appear quickly the passenger is already complete, so don't burn a long timeout per passenger.
        if (!waitForAnyPassengerForm(2)) {
            return;
        }
        logger.log("Completing missing identity info for saved passenger: " + passengerName);
        fillIdentityInfoLogin(flightRoute);
        submitPassenger(flightRoute);
    }

    /** Taps a saved-passenger checkbox/radio directly by presence (mirrors iOS {@code tapSavedPassengerCheckbox}). */
    private void tapSavedPassengerCheckbox(Locator checkbox) {
        driverTools.element(checkbox).ifPresent(driverTools::click);
    }

    /** Fills only the missing identity fields on a saved-passenger form (mirrors iOS {@code fillIdentityInfoLogin}). */
    private void fillIdentityInfoLogin(String flightRoute) {
        if (driverTools.isElementDisplayed(SURNAME)) {
            driverTools.clear(SURNAME);
            driverTools.sendKeys(SURNAME, CoreFunctions.randomLastName());
            driverTools.hideKeyboard(1);
        }
        if (driverTools.isElementDisplayed(NIK_PASSPORT_NUMBER) || driverTools.isElementDisplayed(ID)) {
            fillId();
        }
        if (driverTools.isElementDisplayed(NATIONALITY)) {
            fillNationality();
        }
        if (flightRoute.equals("international")) {
            fillInternationalIdentityInfo();
        }
    }

    /**
     * Fills the empty "Passenger N" slots that aren't covered by saved passengers (login flow, mirrors
     * iOS {@code fillEmptyPassengerSlots}). Each slot opens a full passenger form; confirming with "Next
     * passenger" auto-opens the next one, so the loop fills whatever form is open and only opens a slot
     * from the list when none is. The passenger type (for DOB) is read from the open form's age hint.
     */
    private void fillEmptyPassengerSlots(String flightRoute, String airline, List<String> names) {
        // Fast exit for the pure saved-passenger (cache) case: no form open and no empty "Passenger N"
        // slot present means there is nothing to fill here, so skip the whole loop (avoids the repeated
        // EMPTY_PASSENGER_SLOT swipe-searches that dominated the log).
        if (!isAnyPassengerFormOpen() && driverTools.waitForElementToPresence(EMPTY_PASSENGER_SLOT, 1).isEmpty()) {
            return;
        }
        for (int guard = 0; guard < 12; guard++) {
            boolean formOpen = waitForAnyPassengerForm(2);
            if (!formOpen) {
                if (driverTools.swipeToElement(EMPTY_PASSENGER_SLOT, Swipe.UP, 4).isEmpty()) {
                    break; // no more empty passenger slots to fill
                }
                driverTools.click(EMPTY_PASSENGER_SLOT);
                if (!waitForAnyPassengerForm(8)) {
                    break;
                }
            }
            int dobRange = getDobRangeFromOpenForm();
            String name = chooseDomesticOrInternational(flightRoute, dobRange, airline);
            if (name != null && !name.isBlank()) {
                names.add(name);
            }
        }
    }

    /** Infers the passenger type of the currently open form from its age hint, for the DOB (mirrors iOS). */
    private int getDobRangeFromOpenForm() {
        if (driverTools.isElementDisplayed(FORM_TYPE_INFANT)) {
            return PassengerCategory.INFANT.getAge();
        }
        if (driverTools.isElementDisplayed(FORM_TYPE_CHILD)) {
            return PassengerCategory.CHILD.getAge();
        }
        return PassengerCategory.ADULT.getAge();
    }

    private Locator getPassengerLocatorID(int index) {
        return new Locator("Passenger Locator ID", AppiumBy.xpath("(//*[contains(@resource-id,'group_field_passenger_selection')]//*[contains(@resource-id,'title_text') and (contains(@text,'Passenger') or contains(@text,'Penumpang'))])[" + index + "]"));
    }

    private Locator savedPassengerNameAt(int index) {
        return new Locator("SAVED_PASSENGER_NAME_" + index, AppiumBy.xpath("(//*[contains(@resource-id,'group_field_passenger_selection') or contains(@resource-id,'items_container')]//*[contains(@resource-id,'title_text') and not(contains(@text,'Passenger')) and not(contains(@text,'Penumpang'))])[" + index + "]"));
    }

    /** Clickable selection hit-area — one per passenger row ({@code fl_check} itself is not clickable). */
    private Locator savedPassengerCheckboxAt(int index) {
        return new Locator("SAVED_PASSENGER_CHECKBOX_" + index, AppiumBy.xpath(
                "(//*[contains(@resource-id,'items_container')]//*[contains(@resource-id,'area_selection_control')])[" + index + "]"));
    }

    private Locator savedPassengerRowAt(int index) {
        return new Locator("SAVED_PASSENGER_ROW_" + index, AppiumBy.xpath(
                "(//*[contains(@resource-id,'items_container')]//*[contains(@resource-id,'item_row')])[" + index + "]"));
    }

    /** Selected state: exactly one {@code fl_check} per row; selected when it contains {@code iv_check}. */
    private Locator savedPassengerCheckboxSelectedAt(int index) {
        return new Locator("SAVED_PASSENGER_CHECKBOX_SELECTED_" + index, AppiumBy.xpath(
                "(//*[contains(@resource-id,'items_container')]//*[contains(@resource-id,'fl_check')])[" + index + "]"
                        + "[.//*[contains(@resource-id,'iv_check')]]"));
    }

    private boolean waitForAnyPassengerForm(int timeoutSeconds) {
        return driverTools.waitForElementToAppear(ANY_PASSENGER_FORM, timeoutSeconds).isPresent();
    }

    private void clearPassengerFormIfShown() {
        if (driverTools.waitForElementToAppear(PASSENGER_FORM_CLEAR, 5).isPresent()) {
            logger.log("Clearing pre-filled passenger profile data");
            driverTools.click(PASSENGER_FORM_CLEAR);
            driverTools.dummyWait(1);
        }
    }

    private void dismissLeaveBookingDialogIfShown() {
        if (driverTools.waitForElementToAppear(LEAVE_BOOKING_DIALOG, 2).isPresent()) {
            driverTools.click(COMPLETE_BOOKING_BUTTON);
            driverTools.waitForElementToDisappear(LEAVE_BOOKING_DIALOG, 10);
        }
    }

    private void confirmPassengerReviewIfShown() {
        if (driverTools.waitForElementToAppear(PASSENGER_REVIEW_SHEET, 5).isPresent()) {
            clickPassengerReviewConfirm();
            driverTools.waitForElementToDisappear(PASSENGER_REVIEW_SHEET, 15);
            dismissLeaveBookingDialogIfShown();
        }
    }

    private boolean isAnyPassengerFormOpen() {
        return driverTools.isElementDisplayed(ANY_PASSENGER_FORM);
    }

    private boolean usesSplitNameFields() {
        return driverTools.isElementDisplayed(PASSENGER_FORM) && driverTools.isElementDisplayed(GIVEN_NAMES);
    }

    private boolean waitForPassengerFormToClose(int timeoutSeconds) {
        if (driverTools.isElementDisplayed(PASSENGER_FORM_SHEET)) {
            return driverTools.waitForElementToDisappear(PASSENGER_FORM_SHEET, timeoutSeconds);
        }
        if (driverTools.isElementDisplayed(PASSENGER_FORM)) {
            return driverTools.waitForElementToDisappear(PASSENGER_FORM, timeoutSeconds);
        }
        if (driverTools.isElementDisplayed(PASSENGER_FORM_DYNAMIC)) {
            return driverTools.waitForElementToDisappear(PASSENGER_FORM_DYNAMIC, timeoutSeconds);
        }
        return !isAnyPassengerFormOpen();
    }

    private Locator getFullNameFieldLocator() {
        if (driverTools.isElementDisplayed(PASSENGER_FULL_NAME)) {
            return PASSENGER_FULL_NAME;
        }
        return FULL_NAME;
    }

    private void clickPassengerReviewConfirm() {
        if (driverTools.isElementDisplayed(PASSENGER_REVIEW_CONFIRM)) {
            driverTools.click(PASSENGER_REVIEW_CONFIRM);
        } else {
            driverTools.click(PASSENGER_REVIEW_CONFIRM_TEXT);
        }
    }

    private Locator getActiveFormSaveButton() {
        if (driverTools.isElementDisplayed(PASSENGER_FORM_SAVE_TEXT)) {
            return PASSENGER_FORM_SAVE_TEXT;
        }
        if (isAnyPassengerFormOpen() || driverTools.isElementDisplayed(PASSENGER_FORM_SAVE)) {
            return PASSENGER_FORM_SAVE;
        }
        if (driverTools.isElementDisplayed(CONTACT_BOTTOM_SHEET) || driverTools.isElementDisplayed(CONTACT_FORM_SAVE)) {
            return CONTACT_FORM_SAVE;
        }
        return SAVE_BUTTON;
    }

    @Override
    public List<String> fillPassengerDetail(int adult, int child, int infant, String flightRoute, String airline) {
        String methodName = "fillPassengerDetail";
        long startTime = CoreFunctions.startTiming(methodName);
        // The booking form animates/spins/loads continuously, so UiAutomator2 never reports "idle" and
        // every command pays the full 10s waitForIdleTimeout. Disable it for the whole fill (restored in
        // finally) to cut per-command latency from ~10s to <1s. Scoped to the fill so the rest of the suite
        // keeps the default idle behaviour.
        long previousIdleTimeout = driverTools.getWaitForIdleTimeout();
        driverTools.setWaitForIdleTimeout(0);
        try {
            List<String> passengerNames = new ArrayList<>();

            driverTools.waitForElementToAppear(CONTINUE_BUTTON, 10);
            dismissOpenBottomSheetIfPresent();

            // Login/saved-passenger UI: some passengers may be pre-filled saved profiles (selected via
            // checkbox, sometimes still missing info), while the rest are empty "Passenger N" slots that
            // must be filled manually. Fill the empty slots, then select/complete the saved ones.
            if (isSavedPassengerMode()) {
                fillEmptyPassengerSlots(flightRoute, airline, passengerNames);
                passengerNames.addAll(selectSavedPassengers(adult, child, infant, flightRoute));
                return passengerNames;
            }

            int currentIndex = 1;

            for (int i = 1; i <= adult; i++) {
                logger.log("Filling detail for passenger(adult) " + i);
                processPassenger(currentIndex, flightRoute, PassengerCategory.ADULT.getAge(), airline, passengerNames);
                currentIndex++;
            }

            if (child != 0) {
                for (int i = 1; i <= child; i++) {
                    logger.log("Filling detail for passenger(Child) " + currentIndex);
                    processPassenger(currentIndex, flightRoute, PassengerCategory.CHILD.getAge(), airline, passengerNames);
                    currentIndex++;
                }
            }

            if (infant != 0) {
                for (int i = 1; i <= infant; i++) {
                    logger.log("Filling detail for passenger(Infant) " + currentIndex);
                    processPassenger(currentIndex, flightRoute, PassengerCategory.INFANT.getAge(), airline, passengerNames);
                    currentIndex++;
                }
            }

            return passengerNames;
        } finally {
            driverTools.setWaitForIdleTimeout(previousIdleTimeout);
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    private String fillPassengerNames(String airline) {
        String firstName = CoreFunctions.randomNameWithoutTest();
        String lastName = CoreFunctions.randomLastName();
        if (airline.toLowerCase().contains("citilink") || airline.toLowerCase().contains("garuda")) {
            lastName = " Testing";
        }
        fillTextField(GIVEN_NAMES, firstName);
        fillTextField(SURNAME, lastName);
        return firstName + " " + lastName;
    }

    /**
     * Types {@code value} into a text field with a SINGLE element lookup, then reuses that element for
     * click/clear/sendKeys. On the heavy booking form every locator lookup triggers a full UI-tree xpath
     * scan (several seconds each, and doubled on preprod via the wait multiplier), and {@code click(Locator)}
     * re-locates the element on every clickable poll — re-finding per action was the dominant cost behind
     * the very slow passenger fill. Falls back to a single locator-based retry if the cached element goes
     * stale. Returns {@code false} when the field cannot be brought on-screen.
     */
    private boolean fillTextField(Locator field, String value) {
        WebElement element = driverTools.swipeToElement(field, Swipe.UP, 5)
                .or(() -> driverTools.swipeToElement(field, Swipe.DOWN, 3))
                .orElse(null);
        if (element == null) {
            logger.warn("Field not visible after scroll attempts; skipping: " + field);
            return false;
        }
        try {
            driverTools.click(element);
            driverTools.clear(element);
            driverTools.sendKeys(element, value);
        } catch (StaleElementReferenceException stale) {
            logger.log("Field element went stale, re-locating once: " + field);
            driverTools.click(field);
            driverTools.clear(field);
            driverTools.sendKeys(field, value);
        }
        driverTools.hideKeyboard(1);
        return true;
    }

    private String fillFullName (String airline) {
        if (driverTools.isElementDisplayed(GIVEN_NAMES)) {
            return fillPassengerNames(airline);
        }
        String name = CoreFunctions.randomNameWithoutTest();
        String lastName = CoreFunctions.randomLastName();
        if(airline.toLowerCase().contains("citilink") || airline.toLowerCase().contains("garuda")){
            lastName = " Testing";
        }
        if (!ensureFullNameVisible()) {
            logger.warn("FULL_NAME field not visible after scroll attempts; skipping fillFullName");
            return name + lastName;
        }
        Locator nameField = getFullNameFieldLocator();
        driverTools.click(nameField);
        driverTools.clear(nameField);
        driverTools.sendKeys(nameField, name);
        driverTools.hideKeyboard(1);

        return name + lastName;
    }

    @Override
    public void fillContactDetail (String phone, String email, String loginType,String airline) {
        if (loginType.equals("guest")) {
            // Same idle-wait problem as the passenger fill: disable it for the contact form so each field
            // interaction is fast, then restore the previous value.
            long previousIdleTimeout = driverTools.getWaitForIdleTimeout();
            driverTools.setWaitForIdleTimeout(0);
            try {
                openContactDetailForm();
                fillName(airline);
                fillTitle();
                fillPhone(phone);
                fillEmail(email);
                saveContactForm();
            } finally {
                driverTools.setWaitForIdleTimeout(previousIdleTimeout);
            }
        }
    }

    private void openContactDetailForm() {
        if (driverTools.isElementDisplayed(CONTACT_BOTTOM_SHEET)) {
            return;
        }
        if (driverTools.waitForElementToAppear(CONTACT_DETAIL, 3).isEmpty()) {
            driverTools.swipeToElement(CONTACT_DETAIL, Swipe.UP, 8);
        }
        driverTools.click(CONTACT_DETAIL);
        driverTools.waitForElementToAppear(CONTACT_BOTTOM_SHEET, 15).orElseThrow();
    }

    private void saveContactForm() {
        driverTools.click(CONTACT_FORM_SAVE);
        if (!driverTools.waitForElementToDisappear(CONTACT_BOTTOM_SHEET, 15)) {
            driverTools.click(CLOSE_BUTTON);
            driverTools.waitForElementToDisappear(CONTACT_BOTTOM_SHEET, 10);
        }
    }

    private void dismissOpenBottomSheetIfPresent() {
        dismissLeaveBookingDialogIfShown();
        if (driverTools.isElementDisplayed(PASSENGER_REVIEW_SHEET)) {
            confirmPassengerReviewIfShown();
            return;
        }
        if (driverTools.isElementDisplayed(CONTACT_BOTTOM_SHEET)
                && driverTools.waitForElementToAppear(CONTACT_FORM_SAVE, 2).isPresent()) {
            driverTools.click(CLOSE_BUTTON);
            driverTools.waitForElementToDisappear(CONTACT_BOTTOM_SHEET, 10);
        }
    }

    private void saveDynamicForm() {
        driverTools.click(getActiveFormSaveButton());
        driverTools.waitForElementToDisappear(CONTACT_BOTTOM_SHEET, 15);
    }

    private void waitForDynamicFormToOpen() {
        // Common case: wait on the union locator (covers PASSENGER_FORM / SHEET / DYNAMIC) first.
        if (driverTools.waitForElementToAppear(ANY_PASSENGER_FORM, 10).isPresent()) {
            return;
        }
        // The contact/close variants can render slightly AFTER ANY_PASSENGER_FORM resolves, so give them a
        // real wait (not an instantaneous displayed-check) before failing — otherwise a UI that is still
        // transitioning correctly would fall through and throw on CONTACT_FORM_SAVE. The || short-circuits,
        // so these extra waits only cost time on the genuine timeout/error path.
        if (driverTools.waitForElementToAppear(CLOSE_PASSENGER_DETAILS, 5).isPresent()
                || driverTools.waitForElementToAppear(CONTACT_BOTTOM_SHEET, 5).isPresent()) {
            return;
        }
        driverTools.waitForElementToAppear(CONTACT_FORM_SAVE, 5).orElseThrow();
    }

    private void fillPhone (String phone) {
        // Locate once and reuse; pressKey(WebElement) clicks the element itself, so no separate click.
        WebElement element = driverTools.swipeToElement(PHONE, Swipe.UP, 3).orElse(null);
        if (element == null) {
            logger.warn("Phone field not visible after scroll attempts; skipping fillPhone");
            return;
        }
        driverTools.clear(element);
        driverTools.pressKey(element, phone);
        driverTools.hideKeyboard(1);
    }

    private void fillEmail (String email) {
        fillTextField(EMAIL, email);
    }

    private void fillTitle () {
        driverTools.waitForElementToPresence(TITLE_RADIO, 15);
        List<WebElement> titleRadio = driverTools.elements(TITLE_RADIO).orElseThrow();
        if (titleRadio.isEmpty()) {
            throw new IllegalStateException("Title radio options not found on contact/passenger form");
        }
        driverTools.click(titleRadio.get(CoreFunctions.generateRandomInt(0, titleRadio.size() - 1)));
    }

    private String fillName (String airline) {
        String name = CoreFunctions.randomNameWithoutTest();
        if(airline.toLowerCase().contains("citilink") || airline.toLowerCase().contains("garuda")){
            name += " Testing";
        }
        if (!fillTextField(getFullNameFieldLocator(), name)) {
            logger.warn("FULL_NAME field not visible after scroll attempts; skipping fillName");
        }
        return name;
    }

    /**
     * Make sure the full-name input is in the viewport before we try to click/type.
     * Round-trip / pre-filled forms render the field below the visible area, and a bare
     * .click() on a non-displayed element either no-ops or hits the wrong widget.
     * Returns false when the field is not present at all (e.g. saved-traveller mode that
     * hides the editable input entirely) so callers can skip cleanly.
     */
    private boolean ensureFullNameVisible() {
        return ensureFullNameVisible(getFullNameFieldLocator());
    }

    private boolean ensureFullNameVisible(Locator nameField) {
        if (driverTools.isElementDisplayed(nameField)) {
            return true;
        }
        if (driverTools.swipeToElement(nameField, Swipe.UP, 5).isPresent()) {
            return true;
        }
        if (driverTools.swipeToElement(nameField, Swipe.DOWN, 3).isPresent()) {
            return true;
        }
        return driverTools.isElementDisplayed(nameField);
    }

    private void fillDob () {
        driverTools.click(DOB_PICKER);
        driverTools.click(DOB_SUBMIT);
    }

    private void fillDob ( int roll) {
        driverTools.click(DOB_PICKER);
        String currentDate = CoreFunctions.getCurrentLongDate();
        int currentYear = Integer.parseInt(CoreFunctions.getSectionOfString(currentDate," ",2));
        WebElement yearWheel = driverTools.waitForElementToPresence(YEAR_PICKER_WHEEL).orElseThrow();
        WebElement currentYearElement = driverTools.waitForElementToPresence(CURRENT_YEAR_PICKER_WHEEL).orElseThrow();
        int currentYearOnDob = Integer.parseInt(driverTools.getText(currentYearElement));
        logger.log("currentYear :" + currentYearOnDob);
        int rangeDOB = currentYear - roll;
        String direction = "before";
        int distance = currentYearOnDob - rangeDOB;

        if(distance < 0){
            direction = "next";
            distance *= -1;
        }
        logger.log("direction : " + direction);
        driverTools.scrollPickerWheel(yearWheel, distance, direction);
        driverTools.click(DOB_SUBMIT);
    }

    private void fillId () {
        String id = String.valueOf(CoreFunctions.randomInt(3));
        Locator idField = driverTools.isElementDisplayed(NIK_PASSPORT_NUMBER) ? NIK_PASSPORT_NUMBER : ID;
        fillTextField(idField, "327202200202" + id + "1");
    }

    private void fillNationality () {
        driverTools.click(NATIONALITY);
        driverTools.click(getCountryLocator("Indonesia"));
    }

    private void verifyCurrentContact (String phone) {
        if (driverTools.waitForElementToAppear(CONTACT_DETAIL, 5).isPresent()) {
            openContactDetailForm();

            String phoneValue = driverTools.getText(PHONE).orElseThrow();
            fillTitle();

            if (phoneValue.length() < 5) {
                driverTools.pressKey(PHONE, phone);
            }

            saveContactForm();
        }
    }

    private void toggleSameAsContact (String flightRoute,String airline) {
        WebElement sameAsContact = driverTools.waitForElementToAppear(SAME_AS_CONTACT_DETAIL_SWITCH,15).orElseThrow();
        driverTools.click(sameAsContact);
        waitForDynamicFormToOpen();
        if (flightRoute.equals("domestic")) {
            fillName(airline);
            if (driverTools.isElementDisplayed(DOB_PICKER)) {
                fillDob();
            }
            if (driverTools.isElementDisplayed(NIK_PASSPORT_NUMBER) || driverTools.isElementDisplayed(ID)) {
                fillId();
            }
            if (driverTools.isElementDisplayed(NATIONALITY)) {
                fillNationality();
            }
        } else if (flightRoute.equals("international")) {
            if (driverTools.isElementDisplayed(DOB_PICKER)) {
                fillDob();
            }
            if (driverTools.isElementDisplayed(NATIONALITY)) {
                fillNationality();
            }
            if(driverTools.isElementDisplayed(PASSPORT)){
                fillPassport();
            }
        }
        WebElement saveBtn = driverTools.swipeToElement(getActiveFormSaveButton(), Swipe.UP, 2).orElseThrow();
        driverTools.click(saveBtn);
    }

    private void fillPassportNumber () {
        driverTools.waitForElementToPresence(PASSPORT_NUMBER);
        String currentValue = driverTools.getText(PASSPORT_NUMBER).orElseThrow();
        if (currentValue.isBlank() || currentValue.toLowerCase().contains("passport") || currentValue.toLowerCase().contains("paspor")
                || CoreFunctions.getIntFromString(currentValue).isEmpty()) {
            String passportNumber = String.valueOf(CoreFunctions.randomInt(9));
            driverTools.clear(PASSPORT_NUMBER);
            driverTools.click(PASSPORT_NUMBER);
            driverTools.sendKeys(PASSPORT_NUMBER, passportNumber);
        }
    }

    private void fillPassport () {
        fillInternationalIdentityInfo();
    }

    private void fillPassportDateField(Locator dateField, String scrollDirection) {
        driverTools.swipeToElement(dateField, Swipe.UP, 4);
        if (!isPassportFieldEmpty(dateField, "issued", "expiry", "habis", "pengeluaran", "date", "tanggal")) {
            return;
        }
        driverTools.click(dateField);
        WebElement yearWheel = driverTools.waitForElementToPresence(YEAR_PICKER_WHEEL).orElseThrow();
        driverTools.scrollPickerWheel(yearWheel, 3, scrollDirection);
        driverTools.click(DOB_SUBMIT);
    }

    private void fillIssuingCountry() {
        driverTools.swipeToElement(PASSPORT_COUNTRY, Swipe.UP, 4);
        if (!isPassportFieldEmpty(PASSPORT_COUNTRY, "issuing country", "negara")) {
            return;
        }
        driverTools.click(PASSPORT_COUNTRY);
        driverTools.click(getCountryLocator("Indonesia"));
    }

    private boolean isPassportFieldEmpty(Locator field, String... placeholders) {
        if (!driverTools.isElementDisplayed(field)) {
            driverTools.swipeToElement(field, Swipe.UP, 3);
        }
        if (!driverTools.isElementDisplayed(field)) {
            return false;
        }
        String value = driverTools.getText(field).orElse("");
        if (value.isBlank()) {
            return true;
        }
        String lower = value.toLowerCase();
        for (String placeholder : placeholders) {
            if (lower.contains(placeholder.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private enum PassengerCategory {
        ADULT(25),
        CHILD(4),
        INFANT(1);

        private final int age;

        PassengerCategory(int age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }
    }

    private Locator getPassengerLocator(int index) {
        return new Locator("Passenger Locator", AppiumBy.xpath(
                "//*[contains(@resource-id,'title_text') and (contains(@text,'Passenger " + index + "') or contains(@text,'Penumpang " + index + "'))]"));
    }

    private Locator getCountryLocator (String countryName) {
        countryName = CoreFunctions.titleize(countryName);
        return new Locator("COUNTRY", AppiumBy.xpath("//*[contains(@resource-id,'tds_country_code_title_text') and contains(@text,'" + countryName + "')]"));
    }

    private Locator selectButton ( int index) {
        return new Locator("Select button inside extra addon", AppiumBy.xpath("(//*[contains(@resource-id,'zl_seat_map')]//*[contains(@resource-id,'v_seat') and not(contains(@resource-id,'tv'))])[" + index + "]"));
    }

    private Locator selectButtonAddOn ( int index) {
        return new Locator("Select button", AppiumBy.xpath("(//*[contains(@resource-id, 'tv_btn_change') and (contains(@text,'Select') or contains(@text,'Pilih'))])[" + index + "]"));
    }

    private Locator getPassengerForMealButton ( int index) {
        return new Locator("Passenger Change Meal Button", AppiumBy.xpath("(//*[contains(@resource-id,'cl_meal_pax_passenger_item')])[" + index + "]"));
    }

    private Locator getMealCheckBox ( int index) {
        return new Locator("Meal CheckBox", AppiumBy.xpath("(//*[contains(@resource-id,'fl_check')])[" + index + "]"));
    }

    private Locator getCovid19RadioBox ( int index) {
        return new Locator("Covid19 RadioBox", AppiumBy.xpath("(//*[contains(@resource-id,'rb_selected')])[" + index + "]"));
    }

    private Locator getCovid19RadioBoxText ( int index) {
        return new Locator("Covid19 RadioBox Text", AppiumBy.xpath("(//*[contains(@resource-id,'tv_title')])[" + index + "]"));
    }

    @Override
    public void continueToSecondHalf () {
        String methodName = "continueToSecondHalf";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.swipeToElement(CONTINUE_BUTTON, Swipe.UP, 5).orElseThrow();
            driverTools.click(CONTINUE_BUTTON);
            waitForBookingPageTwo();
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    private void waitForBookingPageTwo() {
        for (int attempt = 0; attempt < 30; attempt++) {
            completeBundledMealOrSeatPagesIfShown();
            if (driverTools.waitForElementToPresence(BOOK_MEALS_PAGE, 1).isPresent()) {
                logger.log("Book Meals shown after Continue — closing without selecting meals");
                driverTools.waitForElementToAppear(MEAL_SAVE_BUTTON, 10).ifPresent(driverTools::click);
                driverTools.waitForElementToDisappear(BOOK_MEALS_PAGE, 15);
                continue;
            }
            if (driverTools.waitForElementToPresence(SEAT_CONTAINER, 1).isPresent()
                    || driverTools.waitForElementToPresence(EMPTY_SEAT, 1).isPresent()) {
                logger.log("Seat map shown after Continue — selecting free seat");
                selectBundledSeatAndSave();
                continue;
            }
            if (driverTools.waitForElementToPresence(BOOKING_PAGE_TWO_INDICATOR, 1).isPresent()
                    || driverTools.waitForElementToPresence(BAGGAGE_ADDON, 1).isPresent()
                    || driverTools.waitForElementToPresence(BAGGAGE_ALLOWANCE_SECTION, 1).isPresent()
                    || driverTools.waitForElementToPresence(ADD_CHECKED_BAGGAGE_BUTTON, 1).isPresent()
                    || driverTools.waitForElementToPresence(EXTRA_PROTECTION_LIST, 1).isPresent()
                    || driverTools.waitForElementToPresence(getCompoundExtraProtectionTitleLocator(1), 1).isPresent()
                    || driverTools.waitForElementToPresence(getExtraProtectionLocatorTitle(1), 1).isPresent()
                    || driverTools.waitForElementToPresence(MEALS_ADDON_TITLE, 1).isPresent()
                    || driverTools.waitForElementToPresence(SEAT_ADDON_TITLE, 1).isPresent()) {
                logger.log("Booking form page 2 is loaded");
                return;
            }
            driverTools.dummyWait(1);
        }
        throw new NoSuchElementException("Booking form page 2 did not load after clicking Continue");
    }

    /**
     * When depart/return fare includes free meals or seats, Continue from booking part 1 can open
     * Book Meals / seat map before page 2. Complete those screens so waitForBookingPageTwo can proceed.
     */
    private void completeBundledMealOrSeatPagesIfShown() {
        if (driverTools.waitForElementToPresence(BOOK_MEALS_PAGE, 1).isPresent()) {
            logger.log("Book Meals page shown after Continue — selecting free meal(s) and saving");
            selectMeal(1);
            if (driverTools.waitForElementToAppear(MEAL_RETURN_ROUTE_TAB, 2).isPresent()) {
                driverTools.click(MEAL_RETURN_ROUTE_TAB);
                selectMeal(1);
            }
            driverTools.swipeToElement(MEAL_SAVE_BUTTON, Swipe.UP, 5);
            driverTools.waitForElementToAppear(MEAL_SAVE_BUTTON, 15).orElseThrow();
            driverTools.click(MEAL_SAVE_BUTTON);
            driverTools.waitForElementToDisappear(BOOK_MEALS_PAGE, 20);
        }
        if (driverTools.waitForElementToPresence(SEAT_CONTAINER, 2).isPresent()
                || driverTools.waitForElementToPresence(EMPTY_SEAT, 2).isPresent()) {
            logger.log("Seat map shown after Continue — selecting seat(s) and saving");
            selectBundledSeatAndSave();
        }
    }

    private void selectBundledSeatAndSave() {
        if (driverTools.waitForElementToAppear(EMPTY_SEAT, 8).isPresent()) {
            List<WebElement> seats = driverTools.elements(EMPTY_SEAT).orElseThrow();
            if (seats.size() > 2) {
                Locator emptySeat = getEmptySeatLocator(CoreFunctions.generateRandomInt(2, seats.size() - 1));
                driverTools.swipeToElement(emptySeat, Swipe.UP, 3);
                driverTools.click(emptySeat);
            } else {
                driverTools.click(EMPTY_SEAT);
            }
        }
        if (driverTools.waitForElementToAppear(ADDON_SAVE_BUTTON, 10).isPresent()) {
            driverTools.click(ADDON_SAVE_BUTTON);
        }
    }

    /** Keeps tapping enabled meal Add until max; asserts Add is then disabled / unavailable. */
    private int selectMealsUntilMaximumOnCurrentPage(int noOfPassenger) {
        int totalAdded = 0;
        for (int i = 1; i <= noOfPassenger; i++) {
            Locator changePassengerBtn = getPassengerForMealButton(i);
            if (driverTools.swipeToElement(changePassengerBtn, Swipe.LEFT, 4).isPresent()
                    || driverTools.waitForElementToAppear(changePassengerBtn, 2).isPresent()) {
                driverTools.click(changePassengerBtn);
            }
            int addedForPax = 0;
            while (driverTools.waitForElementToAppear(MEAL_ADD_BUTTON_ENABLED, 3).isPresent() && addedForPax < 10) {
                driverTools.click(MEAL_ADD_BUTTON_ENABLED);
                addedForPax++;
                totalAdded++;
            }
            boolean addDisabled = driverTools.waitForElementToPresence(MEAL_ADD_BUTTON_DISABLED, 3).isPresent()
                    || driverTools.waitForElementToAppear(MEAL_ADD_BUTTON_ENABLED, 2).isEmpty();
            Assertion.assertThat("Meal Add must be disabled after reaching maximum for passenger " + i, addDisabled, is(true));
            logger.log("Meals added for passenger " + i + ": " + addedForPax);
        }
        driverTools.swipeToElement(MEAL_SAVE_BUTTON, Swipe.UP, 5);
        driverTools.waitForElementToAppear(MEAL_SAVE_BUTTON, 10).orElseThrow();
        driverTools.click(MEAL_SAVE_BUTTON);
        driverTools.waitForElementToDisappear(BOOK_MEALS_PAGE, 15);
        return totalAdded;
    }

    @Override
    public int selectMealsUntilMaximumFromAddons(int adultNumber, int childNumber) {
        if (!driverTools.waitForElementToPresence(BOOK_MEALS_PAGE, 2).isPresent()) {
            openMealsAddon();
            driverTools.waitForElementToAppear(BOOK_MEALS_PAGE, 10).orElseThrow();
        }
        return selectMealsUntilMaximumOnCurrentPage(adultNumber + childNumber);
    }

    @Override
    public String selectFreeSeatFromAddons(PriceDetail priceDetail) {
        openSeatsAddon();
        int pax = priceDetail.adult + priceDetail.child;
        String seatPrice;
        if (driverTools.waitForElementToAppear(ANCILLARIES_SELECT_DEPART, 5).isPresent()) {
            for (WebElement selectBtn : driverTools.elements(ANCILLARIES_SELECT_DEPART).orElseThrow()) {
                driverTools.click(selectBtn);
                seatPrice = selectSeatForPassengers(pax);
                Assertion.assertThat("Free seat total should be present", !seatPrice.isBlank(), is(true));
                return seatPrice;
            }
        }
        seatPrice = selectSeatForPassengers(pax);
        Assertion.assertThat("Free seat total should be present", !seatPrice.isBlank(), is(true));
        if (driverTools.waitForElementToAppear(ADDON_SAVE_BUTTON, 5).isPresent()) {
            driverTools.click(ADDON_SAVE_BUTTON);
        }
        return seatPrice;
    }

    @Override
    public boolean labelsOnBookingPageCorrect() {
        driverTools.swipeToElement(ANTIGEN_TEST_DETAIL, Swipe.UP,10);
        boolean isAirportTransferDetail = driverTools.waitForElementToAppear(AIRPORT_TRANSFER_DETAIL).isPresent();
        Assertion.assertThat("Airport Transfer Detail is present on booking page ", isAirportTransferDetail, is(true));
        boolean isAntigenTestDetail = driverTools.waitForElementToAppear(ANTIGEN_TEST_DETAIL).isPresent();
        Assertion.assertThat("Antigen Test Detail is present on booking page ", isAntigenTestDetail, is(true));
        return isAirportTransferDetail && isAntigenTestDetail;
    }

    private void selectMeal () {
        selectMeal(1);
    }

    private void selectMeal (int mealsPerPassenger) {
        for (int m = 1; m <= mealsPerPassenger; m++) {
            // New Book Meals UI: enabled Add/Tambah on each meal row (becomes Cancel/Batalkan after tap).
            if (driverTools.waitForElementToAppear(MEAL_ADD_BUTTON, 5).isPresent()) {
                driverTools.click(MEAL_ADD_BUTTON);
                continue;
            }
            if (driverTools.waitForElementToAppear(MEAL_ALREADY_SELECTED, 2).isPresent()) {
                logger.log("Meal already selected for this passenger");
                continue;
            }
            // Legacy meal list.
            if (driverTools.waitForElementToAppear(MEAL_ADD_ITEM, 5).isPresent()) {
                driverTools.click(MEAL_ADD_ITEM);
                continue;
            }
            if (driverTools.waitForElementToAppear(MEAL_SELECT_BUTTON, 5).isPresent()) {
                driverTools.click(MEAL_SELECT_BUTTON);
                continue;
            }
            Locator mealCheckBox = getMealCheckBox(m);
            driverTools.click(mealCheckBox);
        }
    }

    private String selectMealForPassengers ( int noOfPassenger) {
        return selectMealForPassengers(noOfPassenger, 1);
    }

    private String selectMealForPassengers ( int noOfPassenger, int mealsPerPassenger) {
        for (int i = 1; i <= noOfPassenger; i++) {
            Locator changePassengerBtn = getPassengerForMealButton(i);
            // Book Meals keeps cl_meal_pax_passenger_item in a horizontal list — swipe into view when needed.
            if (driverTools.swipeToElement(changePassengerBtn, Swipe.LEFT, 4).isPresent()
                    || driverTools.waitForElementToAppear(changePassengerBtn, 3).isPresent()) {
                driverTools.click(changePassengerBtn);
            } else {
                throw new AssertionError("Meal passenger item not found at index " + i);
            }
            selectMeal(mealsPerPassenger);
        }

        driverTools.waitForElementToAppear(MEAL_TOTAL_VALUE);
        String price = driverTools.getText(MEAL_TOTAL_VALUE).orElseThrow();
        logger.log("Price per" + price);
        Assertion.assertThat("Meal price is non empty", !price.isEmpty(), is(true));
        return price;
    }

    private void openMealsAddon() {
        driverTools.swipeToElement(MEALS_ADDON_TITLE, Swipe.UP, 3);
        driverTools.swipeToElement(MEALS_ADDON, Swipe.UP, 3);
        driverTools.click(MEALS_ADDON);
    }

    /** Selects return route on Book Meals (last route tab) or legacy Return/Pulang Select when present. */
    private void selectReturnMealSegment() {
        if (driverTools.waitForElementToAppear(MEAL_RETURN_ROUTE_TAB, 5).isPresent()) {
            driverTools.click(MEAL_RETURN_ROUTE_TAB);
            return;
        }
        if (driverTools.waitForElementToAppear(ANCILLARIES_SELECT_RETURN, 5).isPresent()) {
            driverTools.click(ANCILLARIES_SELECT_RETURN);
            return;
        }
        throw new AssertionError("Return meal segment control not found (route tab / Return Select)");
    }

    private void openSeatsAddon() {
        driverTools.swipeToElement(SEAT_ADDON_TITLE, Swipe.UP, 3);
        driverTools.swipeToElement(SEAT_ADDON, Swipe.UP, 3);
        driverTools.click(SEAT_ADDON);
    }

    @Override
    public String addMeals(int adultNumber, int childNumber, String currency){
        openMealsAddon();
        String mealPrice = selectMealForPassengers(adultNumber + childNumber);

        driverTools.waitForElementToAppear(MEAL_SAVE_BUTTON);
        driverTools.click(MEAL_SAVE_BUTTON);

        String currencyMeal = mealPrice.replaceAll("[^a-zA-Z]", "");
        logger.log("Meal currency: "+ currencyMeal);
        Assertion.assertThat("Meal currency is correct", currencyMeal.equalsIgnoreCase(currency), is(true));

        return mealPrice;
    }

    private void checkForAncillariesAvailability(){
        if(driverTools.isElementDisplayed(ANCILLARIES_NOT_AVAILABLE_DEPART)){
            logger.warn("meal not available for depart");
        }
        if(driverTools.isElementDisplayed(ANCILLARIES_NOT_AVAILABLE_RETURN)){
            logger.warn("meal not available for return");
        }
    }

    private void checkForAncillariesAvailabilityBaggage(){
        if(driverTools.isElementDisplayed(ANCILLARIES_NOT_AVAILABLE_DEPART_BAGGAGE)){
            logger.warn("meal not available for depart");
        }
        if(driverTools.isElementDisplayed(ANCILLARIES_NOT_AVAILABLE_RETURN_BAGGAGE)){
            logger.warn("meal not available for return");
        }
    }

    @Override
    public FlightMealsDetail addMealsRoundTrip(int adultNumber, int childNumber, String currency){
        FlightMealsDetail flightMealsDetail = new FlightMealsDetail();
        openMealsAddon();
        driverTools.waitForElementToAppear(ANCILLARIES_SELECT_DEPART);
        checkForAncillariesAvailability();

        logger.log("select meal for depart");
        List<WebElement>  selectBtnsDepart = driverTools.elements(ANCILLARIES_SELECT_DEPART).orElseThrow();
        double mealPriceDepart = 0;
        for(WebElement selectBtn : selectBtnsDepart){
            driverTools.click(selectBtn);
            String tempMealPrice = selectMealForPassengers(adultNumber + childNumber);
            mealPriceDepart += convertPrice(tempMealPrice);
            String currencyMeal = tempMealPrice.replaceAll("[^a-zA-Z]", "");
            logger.log("Meal currency Depart: "+ currencyMeal);
            Assertion.assertThat("Meal currency is correct", currency.equalsIgnoreCase(currencyMeal) , is(true));
        }
        flightMealsDetail.mealPriceDepart = currency + " " + (int) mealPriceDepart;
        flightMealsDetail.totalPaxDepart = selectBtnsDepart.size() * (adultNumber + childNumber);
        logger.log("total meal price Depart : " + flightMealsDetail.mealPriceDepart);
        logger.log("total X pax Depart : " + flightMealsDetail.totalPaxDepart);

        logger.log("select meal for return");
        List<WebElement>  selectBtnsReturn = driverTools.elements(ANCILLARIES_SELECT_RETURN).orElseThrow();
        double mealPriceReturn = 0;
        for(WebElement selectBtn : selectBtnsReturn){
            driverTools.click(selectBtn);
            String tempMealPrice = selectMealForPassengers(adultNumber + childNumber);
            mealPriceReturn += convertPrice(tempMealPrice);
            String currencyMeal = tempMealPrice.replaceAll("[^a-zA-Z]", "");
            logger.log("Meal currency Return: "+ currencyMeal);
            Assertion.assertThat("Meal currency is correct", currency.equalsIgnoreCase(currencyMeal) , is(true));
        }
        flightMealsDetail.mealPriceReturn = currency + " " + (int) mealPriceReturn;
        flightMealsDetail.totalPaxReturn = selectBtnsReturn.size() * (adultNumber + childNumber);
        logger.log("total meal price Return : " + flightMealsDetail.mealPriceReturn);
        logger.log("total X pax Return : " + flightMealsDetail.totalPaxReturn);

        driverTools.waitForElementToAppear(MEAL_SAVE_BUTTON);
        driverTools.click(MEAL_SAVE_BUTTON);
        return flightMealsDetail;
    }

    @Override
    public FlightMealsDetail addMealsOneWay(int adultNumber, int childNumber, String currency){
        FlightMealsDetail flightMealsDetail = new FlightMealsDetail();
        openMealsAddon();
        driverTools.waitForElementToAppear(ANCILLARIES_SELECT_DEPART);
        checkForAncillariesAvailability();

        logger.log("select meal for depart");
        List<WebElement>  selectBtnsDepart = driverTools.elements(ANCILLARIES_SELECT_DEPART).orElseThrow();
        double mealPriceDepart = 0;
        for(WebElement selectBtn : selectBtnsDepart){
            driverTools.click(selectBtn);
            String tempMealPrice = selectMealForPassengers(adultNumber + childNumber);
            mealPriceDepart += convertPrice(tempMealPrice);
            String currencyMeal = tempMealPrice.replaceAll("[^a-zA-Z]", "");
            logger.log("Meal currency Depart: "+ currencyMeal);
            Assertion.assertThat("Meal currency is correct", currency.equalsIgnoreCase(currencyMeal) , is(true));
        }
        flightMealsDetail.mealPriceDepart = currency + " " + (int) mealPriceDepart;
        flightMealsDetail.totalPaxDepart = selectBtnsDepart.size() * (adultNumber + childNumber);
        logger.log("total meal price Depart : " + flightMealsDetail.mealPriceDepart);
        logger.log("total X pax Depart : " + flightMealsDetail.totalPaxDepart);

        driverTools.waitForElementToAppear(ADDON_SAVE_BUTTON);
        driverTools.click(ADDON_SAVE_BUTTON);
        return flightMealsDetail;
    }

    @Override
    public FlightMealsDetail addMealsReturnOnly(int adultNumber, int childNumber, String currency, int mealsPerPassenger){
        FlightMealsDetail flightMealsDetail = new FlightMealsDetail();
        openMealsAddon();
        driverTools.waitForElementToAppear(BOOK_MEALS_PAGE, 5);
        checkForAncillariesAvailability();

        logger.log("select meal for return only, meals per passenger: " + mealsPerPassenger);
        // New UI: Book Meals route tabs (allow slow load). Legacy: only if Return/Pulang Select is present.
        if (driverTools.waitForElementToAppear(MEAL_RETURN_ROUTE_TAB, 8).isPresent()
                || driverTools.isElementDisplayed(BOOK_MEALS_PAGE)) {
            selectReturnMealSegment();
            String mealPrice = selectMealForPassengers(adultNumber + childNumber, mealsPerPassenger);
            String currencyMeal = mealPrice.replaceAll("[^a-zA-Z]", "");
            logger.log("Meal currency Return: " + currencyMeal);
            Assertion.assertThat("Meal currency is correct", currency.equalsIgnoreCase(currencyMeal), is(true));
            flightMealsDetail.mealPriceReturn = mealPrice;
            flightMealsDetail.totalPaxReturn = adultNumber + childNumber;
        } else if (driverTools.waitForElementToAppear(ANCILLARIES_SELECT_RETURN, 5).isPresent()) {
            List<WebElement> selectBtnsReturn = driverTools.elements(ANCILLARIES_SELECT_RETURN).orElseThrow();
            double mealPriceReturn = 0;
            for (WebElement selectBtn : selectBtnsReturn) {
                driverTools.click(selectBtn);
                String tempMealPrice = selectMealForPassengers(adultNumber + childNumber, mealsPerPassenger);
                mealPriceReturn += convertPrice(tempMealPrice);
                String currencyMeal = tempMealPrice.replaceAll("[^a-zA-Z]", "");
                logger.log("Meal currency Return: " + currencyMeal);
                Assertion.assertThat("Meal currency is correct", currency.equalsIgnoreCase(currencyMeal), is(true));
            }
            flightMealsDetail.mealPriceReturn = currency + " " + (int) mealPriceReturn;
            flightMealsDetail.totalPaxReturn = selectBtnsReturn.size() * (adultNumber + childNumber);
        } else {
            throw new AssertionError("Return meal segment control not found (route tab / Return Select)");
        }
        logger.log("total meal price Return : " + flightMealsDetail.mealPriceReturn);
        logger.log("total X pax Return : " + flightMealsDetail.totalPaxReturn);

        driverTools.waitForElementToAppear(ADDON_SAVE_BUTTON);
        driverTools.click(ADDON_SAVE_BUTTON);
        return flightMealsDetail;
    }

    @Override
    public String selectFreeMealsFromAddons(int adultNumber, int childNumber) {
        openMealsAddon();
        driverTools.waitForElementToAppear(BOOK_MEALS_PAGE, 8);
        checkForAncillariesAvailability();
        // Free meal is on return fare — prefer return tab; also select depart if Add is still available.
        if (driverTools.waitForElementToAppear(MEAL_RETURN_ROUTE_TAB, 5).isPresent()
                || driverTools.waitForElementToAppear(ANCILLARIES_SELECT_RETURN, 3).isPresent()) {
            selectReturnMealSegment();
        }
        String mealPrice = selectMealForPassengers(adultNumber + childNumber);
        logger.log("Free meal total from addons: " + mealPrice);
        Assertion.assertThat("Free meal total should be present", !mealPrice.isBlank(), is(true));
        driverTools.waitForElementToAppear(ADDON_SAVE_BUTTON, 10).orElseThrow();
        driverTools.click(ADDON_SAVE_BUTTON);
        driverTools.waitForElementToDisappear(BOOK_MEALS_PAGE, 15);
        return mealPrice;
    }

    @Override
    public String selectFreeSeatsFromAddons(PriceDetail priceDetail) {
        openSeatsAddon();
        checkForAncillariesAvailability();
        int pax = priceDetail.adult + priceDetail.child;
        String seatPrice = "";
        if (driverTools.waitForElementToAppear(ANCILLARIES_SELECT_RETURN, 5).isPresent()) {
            logger.log("Selecting free seat on return from addons");
            for (WebElement selectBtn : driverTools.elements(ANCILLARIES_SELECT_RETURN).orElseThrow()) {
                driverTools.click(selectBtn);
                seatPrice = selectSeatForPassengers(pax);
            }
        } else if (driverTools.waitForElementToAppear(ANCILLARIES_SELECT_DEPART, 5).isPresent()) {
            logger.log("Selecting free seat on depart from addons");
            for (WebElement selectBtn : driverTools.elements(ANCILLARIES_SELECT_DEPART).orElseThrow()) {
                driverTools.click(selectBtn);
                seatPrice = selectSeatForPassengers(pax);
            }
        } else {
            seatPrice = selectSeatForPassengers(pax);
        }
        logger.log("Free seat total from addons: " + seatPrice);
        Assertion.assertThat("Free seat total should be present", !seatPrice.isBlank(), is(true));
        if (driverTools.waitForElementToAppear(ADDON_SAVE_BUTTON, 5).isPresent()) {
            driverTools.click(ADDON_SAVE_BUTTON);
        }
        return seatPrice;
    }

    @Override
    public boolean checkInsurance () {
        return false;
    } // FIXME: Implementation needed

    @Override
    public int addBaggage () {
        openBaggageSelectionPage();
        int baggageAmount = getBaggage();
        logger.log("Verify have No Thanks / 0 KG option or weight chips");
        boolean isBaggage = driverTools.isElementDisplayed(BAGGAGE_DECLINE_OPTION)
                || driverTools.isElementDisplayed(getBaggageOptionLocator(0))
                || driverTools.isElementDisplayed(getBaggageOptionLocator(baggageAmount));
        Assertion.assertThat("Does baggage have No Thanks, 0 KG, or weight chips", isBaggage, is(true));

        selectBaggageWeightForAllPassengers(baggageAmount);
        return baggageAmount;
    }

    /**
     * Opens the baggage weight picker. Prefers the new "Baggage Allowance" section (+ Add on checked
     * baggage → Book Extra Baggage), then falls back to the legacy addon cell.
     */
    private void openBaggageSelectionPage() {
        if (driverTools.isElementDisplayed(BAGGAGE_DECLINE_OPTION) || driverTools.isElementDisplayed(BAGGAGE_OPTIONS)) {
            logger.log("Baggage options already visible (inline)");
            return;
        }
        if (isBaggageAllowanceSectionVisible()) {
            logger.log("Baggage Allowance section found");
            uncheckBaggageInsuranceIfSelected();
            if (isAddCheckedBaggageButtonVisible()) {
                logger.log("Add checked baggage button found, opening baggage page");
                driverTools.click(ADD_CHECKED_BAGGAGE_BUTTON);
                driverTools.waitForElementToAppear(BOOK_EXTRA_BAGGAGE_PAGE, 5);
                driverTools.waitForElementToAppear(BAGGAGE_OPTIONS, 15)
                        .or(() -> driverTools.waitForElementToAppear(BAGGAGE_DECLINE_OPTION, 5))
                        .orElseThrow();
                return;
            }
            logger.log("Add button not present, selecting baggage inline on allowance section");
            return;
        }
        logger.log("Opening baggage via legacy addon section");
        driverTools.swipeToElement(BAGGAGE_ADDON, Swipe.UP, 3).orElseThrow();
        driverTools.click(BAGGAGE_ADDON);
        driverTools.waitForElementToAppear(BAGGAGE_ROUTE, 5).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(BAGGAGE_OPTIONS, 15)
                .or(() -> driverTools.waitForElementToAppear(BAGGAGE_DECLINE_OPTION, 5))
                .orElseThrow();
    }

    private boolean isBaggageAllowanceSectionVisible() {
        return driverTools.isElementDisplayed(BAGGAGE_ALLOWANCE_SECTION)
                || driverTools.swipeToElement(BAGGAGE_ALLOWANCE_SECTION, Swipe.UP, 8).isPresent();
    }

    private boolean isAddCheckedBaggageButtonVisible() {
        return driverTools.isElementDisplayed(ADD_CHECKED_BAGGAGE_BUTTON)
                || driverTools.swipeToElement(ADD_CHECKED_BAGGAGE_BUTTON, Swipe.UP, 3).isPresent();
    }

    private void uncheckBaggageInsuranceIfSelected() {
        driverTools.swipeToElement(BAGGAGE_INSURANCE_PROTECTION, Swipe.UP, 3);
        if (driverTools.waitForElementToPresence(BAGGAGE_INSURANCE_CHECKED, 3).isPresent()) {
            logger.log("Unchecking pre-selected baggage protection/insurance");
            driverTools.click(BAGGAGE_INSURANCE_CHECKBOX);
        } else {
            logger.log("Baggage protection/insurance is not checked, skipping uncheck");
        }
    }

    /** Selects the given weight for every passenger block on Book Extra Baggage (dynamic pax count). */
    private void selectBaggageWeightForAllPassengers(int weight) {
        int paxSections = driverTools.elements(BAGGAGE_PASSENGER_SECTIONS).map(List::size).orElse(0);
        if (paxSections <= 1) {
            logger.log("Picking " + weight + " KG baggage option");
            driverTools.click(getBaggageOptionLocator(weight));
            return;
        }
        logger.log("Picking " + weight + " KG for " + paxSections + " passengers");
        for (int i = 1; i <= paxSections; i++) {
            logger.log("passenger : " + i);
            selectBaggageWeightForPassenger(weight, i);
        }
    }

    private void selectBaggageWeightForPassenger(int weight, int passengerIndex) {
        Locator indexedLocator = getBaggageOptionLocator(weight, passengerIndex);
        if (driverTools.waitForElementToPresence(indexedLocator, 2).isPresent()) {
            driverTools.click(indexedLocator);
        } else {
            driverTools.click(getBaggageOptionLocator(weight));
        }
    }

    private void saveBaggageSelection() {
        if (driverTools.isElementDisplayed(BAGGAGE_SAVE_BUTTON)) {
            logger.log("Saving baggage selection");
            driverTools.click(BAGGAGE_SAVE_BUTTON);
            return;
        }
        logger.log("Save baggage button not present, skipping save");
        if (driverTools.isElementDisplayed(SAVE_ADDON_BUTTON)) {
            driverTools.back();
            driverTools.click(SAVE_ADDON_BUTTON);
        }
    }

    @Override
    public int getBaggage() {
        List<WebElement> baggage = driverTools.elements(BAGGAGE_OPTIONS).orElseGet(ArrayList::new);
        if (baggage.isEmpty()) {
            driverTools.swipeToElement(BAGGAGE_OPTIONS, Swipe.UP, 3);
            baggage = driverTools.elements(BAGGAGE_OPTIONS).orElseGet(ArrayList::new);
        }
        List<WebElement> uniqueBaggage = new ArrayList<>();
        Set<String> seenTexts = new LinkedHashSet<>();

        for (WebElement element : baggage) {
            String text = driverTools.getText(element);
            if (!seenTexts.contains(text)) {
                seenTexts.add(text);
                uniqueBaggage.add(element);
            }
        }

        logger.log("Baggage options list (after removing duplicates):");
        for (int i = 0; i < uniqueBaggage.size(); i++) {
            String text = driverTools.getText(uniqueBaggage.get(i));
            logger.log("  [" + i + "] " + text);
        }

        int minWeight = Integer.MAX_VALUE;
        Pattern pattern = Pattern.compile("(\\d+)");
        for (WebElement element : uniqueBaggage) {
            String text = driverTools.getText(element);
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                int weight = Integer.parseInt(matcher.group(1));
                if (weight > 0 && weight < minWeight) {
                    minWeight = weight;
                }
            }
        }
        if (minWeight == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("No baggage option with weight > 0 found");
        }
        logger.log("Selected smallest baggage weight after zero: " + minWeight + " kg");
        return minWeight;
    }

    @Override
    public boolean isAddOnPresent(String addOn, String direction){
        if (driverTools.isElementDisplayed(getAddOnLocator(addOn))) return true;
        if (direction.equals("up")) {
            driverTools.swipeToElement(getAddOnLocator(addOn), Swipe.UP, 10);
            return driverTools.isElementDisplayed(getAddOnLocator(addOn));
        } else if (direction.equals("down")) {
            driverTools.swipeToElement(getAddOnLocator(addOn), Swipe.DOWN, 10);
            return driverTools.isElementDisplayed(getAddOnLocator(addOn));
        }
        return false;
    }

    private Locator getAddOnLocator (String addOn){
        switch (addOn) {
            case "Baggage" -> {
                // New booking form shows "Baggage Allowance" / "Checked baggage" instead of a bare "Baggage" title.
                return new Locator("ADD_ON_" + addOn, AppiumBy.xpath("//*[contains(@resource-id,'tv_addon_title') and (@text='Baggage' or @text='Bagasi' or contains(@text,'Baggage') or contains(@text,'Bagasi'))] | //*[contains(@resource-id,'tv_baggage_allowance_title') or (contains(@text,'Baggage Allowance') or contains(@text,'Alokasi Bagasi') or contains(@text,'Kuota Bagasi') or contains(@text,'Checked baggage') or contains(@text,'Bagasi tercatat'))] | //*[contains(@resource-id,'tv_add_checked_baggage')]"));
            }
            default -> throw new NotImplementedException("Unimplemented add on type!");
        }
    }


    @Override
    public void goBackToAddOnSection(){
        driverTools.swipeToElement(CONTINUE_PAYMENT_BUTTON,Swipe.UP,15);
        logger.log("Continue to payment");
        driverTools.click(CONTINUE_PAYMENT_BUTTON);
        logger.log("Checking the passenger details");
        Assertion.assertThat("Check Passenger Details button is displayed", driverTools.isElementDisplayed(CHECK_PASSENGER), is(true));
        driverTools.click(CHECK_PASSENGER);
    }

    @Override
    public int addBaggage (PriceDetail priceDetail) {

        int totalPassenger = priceDetail.adult + priceDetail.child;

        openBaggageSelectionPage();

        int baggageAmount = getBaggage();

        logger.log("Verify have No Thanks / 0 KG option or weight chips");
        boolean isBaggage = driverTools.isElementDisplayed(BAGGAGE_DECLINE_OPTION)
                || driverTools.isElementDisplayed(getBaggageOptionLocator(0))
                || driverTools.isElementDisplayed(getBaggageOptionLocator(baggageAmount));
        Assertion.assertThat("Does baggage have No Thanks, 0 KG, or weight chips", isBaggage, is(true));

        logger.log("Picking " + baggageAmount + " KG baggage option");
        for (int i = 1; i <= totalPassenger; i++) {
            logger.log("passenger : " + i);
            selectBaggageWeightForPassenger(baggageAmount, i);
        }
        return baggageAmount;
    }

    public String[] addBaggageAndGetPrice(int baggageAmount,PriceDetail priceDetail){
        int totalPassenger = priceDetail.adult + priceDetail.child;
        String[] baggageDetail = new String[2];
        logger.log("Verify have 0 KG option");
        boolean isBaggage = driverTools.isElementDisplayed(getBaggageOptionLocator(0));
        isBaggage = isBaggage | driverTools.isElementDisplayed(getBaggageOptionLocator(baggageAmount));
        Assertion.assertThat("Does baggage have 0 KG or 5 KG option", isBaggage, is(true));

        logger.log("Picking " + baggageAmount + " KG baggage option");
        for (int i=1;i<=totalPassenger;i++){
            logger.log("passenger : " + i);
            selectBaggageWeightForPassenger(baggageAmount, i);
        }
        baggageDetail[0] = getBaggagePrice(baggageAmount);
        baggageDetail[1] = String.valueOf(getBaggageAmount(baggageAmount,priceDetail));
        saveBaggageSelection();
        return baggageDetail;
    }

    @Override
    public FlightBaggageDetail addBaggageRoundTrip (int baggageAmount, PriceDetail priceDetail,String currency) {
        FlightBaggageDetail flightBaggageDetail = new FlightBaggageDetail();
        driverTools.swipeToElement(BAGGAGE_ADDON, Swipe.UP, 3);
        driverTools.click(BAGGAGE_ADDON);
        driverTools.waitForElementToAppear(ANCILLARIES_SELECT_DEPART_BAGGAGE);
        checkForAncillariesAvailabilityBaggage();

        logger.log("select baggage for depart");
        List<WebElement>  selectBtnsDepart = driverTools.elements(ANCILLARIES_SELECT_DEPART_BAGGAGE).orElseThrow();
        double baggagePriceDepart = 0;
        double amountDepart = 0;
        for(WebElement selectBtn : selectBtnsDepart){
            driverTools.click(selectBtn);
            String[] tempBaggageDetail = addBaggageAndGetPrice(baggageAmount,priceDetail);
            baggagePriceDepart += convertPrice(tempBaggageDetail[0]);
            logger.log("price inside: " + convertPrice(tempBaggageDetail[0]));
            amountDepart += CoreFunctions.convertPriceSafely(tempBaggageDetail[1]);
            String currencyBaggage = tempBaggageDetail[0].replaceAll("[^a-zA-Z]", "");
            logger.log("Baggage currency Depart: "+ currencyBaggage);
            Assertion.assertThat("Baggage currency is correct", currency.equalsIgnoreCase(currencyBaggage) , is(true));
        }
        logger.log("price depart: " + baggagePriceDepart);
        flightBaggageDetail.baggagePriceDepart = currency + " " + (int) baggagePriceDepart;
        flightBaggageDetail.baggageAmountDepart = (int) amountDepart;
        logger.log("total baggage price Depart : " + flightBaggageDetail.baggagePriceDepart);
        logger.log("total baggage amount Depart: " + flightBaggageDetail.baggageAmountDepart);

        logger.log("select seat for return");
        List<WebElement>  selectBtnsReturn = driverTools.elements(ANCILLARIES_SELECT_RETURN_BAGGAGE).orElseThrow();
        double baggagePriceReturn = 0;
        double amountReturn = 0;
        for(WebElement selectBtn : selectBtnsReturn){
            driverTools.click(selectBtn);
            String[] tempBaggageDetail = addBaggageAndGetPrice(baggageAmount,priceDetail);
            baggagePriceReturn += convertPrice(tempBaggageDetail[0]);
            amountReturn += convertPrice(tempBaggageDetail[1]);
            String currencyBaggage = tempBaggageDetail[0].replaceAll("[^a-zA-Z]", "");
            logger.log("Baggage currency Return: "+ currencyBaggage);
            Assertion.assertThat("Baggage currency is correct", currency.equalsIgnoreCase(currencyBaggage) , is(true));
        }
        flightBaggageDetail.baggagePriceReturn = currency + " " + (int) baggagePriceReturn;
        flightBaggageDetail.baggageAmountReturn = (int) amountReturn;
        logger.log("total baggage price Depart : " + flightBaggageDetail.baggagePriceReturn);
        logger.log("total baggage amount Depart: " + flightBaggageDetail.baggageAmountReturn);

        driverTools.waitForElementToAppear(FINISH_ADD_BAGGAGE_BUTTON);
        driverTools.click(FINISH_ADD_BAGGAGE_BUTTON);
        return flightBaggageDetail;
    }

    @Override
    public FlightBaggageDetail addBaggageOneWay(int baggageAmount, PriceDetail priceDetail,String currency) {
        FlightBaggageDetail flightBaggageDetail = new FlightBaggageDetail();
        driverTools.swipeToElement(BAGGAGE_ADDON, Swipe.UP, 3);
        driverTools.click(BAGGAGE_ADDON);
        driverTools.waitForElementToAppear(ANCILLARIES_SELECT_DEPART_BAGGAGE);
        checkForAncillariesAvailabilityBaggage();

        logger.log("select baggage for depart");
        List<WebElement>  selectBtnsDepart = driverTools.elements(ANCILLARIES_SELECT_DEPART_BAGGAGE).orElseThrow();
        double baggagePriceDepart = 0;
        double amountDepart = 0;
        for(WebElement selectBtn : selectBtnsDepart){
            driverTools.click(selectBtn);
            String[] tempBaggageDetail = addBaggageAndGetPrice(baggageAmount,priceDetail);
            baggagePriceDepart += convertPrice(tempBaggageDetail[0]);
            logger.log("price inside: " + convertPrice(tempBaggageDetail[0]));
            amountDepart += CoreFunctions.convertPriceSafely(tempBaggageDetail[1]);
            String currencyBaggage = tempBaggageDetail[0].replaceAll("[^a-zA-Z]", "");
            logger.log("Baggage currency Depart: "+ currencyBaggage);
            Assertion.assertThat("Baggage currency is correct", currency.equalsIgnoreCase(currencyBaggage) , is(true));
        }
        logger.log("price depart: " + baggagePriceDepart);
        flightBaggageDetail.baggagePriceDepart = currency + " " + (int) baggagePriceDepart;
        flightBaggageDetail.baggageAmountDepart = (int) amountDepart;
        logger.log("total baggage price Depart : " + flightBaggageDetail.baggagePriceDepart);
        logger.log("total baggage amount Depart: " + flightBaggageDetail.baggageAmountDepart);

        driverTools.waitForElementToAppear(FINISH_ADD_BAGGAGE_BUTTON);
        driverTools.click(FINISH_ADD_BAGGAGE_BUTTON);
        return flightBaggageDetail;
    }

    private String getCovid19TestPrice() {
        driverTools.click(COVID19_CHECKBOX_AGREE_BUTTON);
        WebElement  covid19TotalValue = driverTools.waitForElementToAppear(COVID19_TOTAL_VALUE).orElseThrow();
        String price = driverTools.getText(covid19TotalValue);
        logger.log("Covid19 Test Price "+ price);
        driverTools.click(SAVE_ADDON_BUTTON);
        Assertion.assertThat("Seat price is non empty",!price.isEmpty() , is(true));
        return price;
    }

    @Override
    public ArrayList<String> addCovidTest () {
        driverTools.swipeToElement(COVID19_ADDON, Swipe.UP, 3);
        driverTools.click(COVID19_ADDON);

        Locator selectMeal = selectButton(1);
        driverTools.click(selectMeal);

        Locator selectRadioBtn = getCovid19RadioBox(2);
        driverTools.click(selectRadioBtn);

        WebElement  covid19TestCompany = driverTools.waitForElementToAppear(getCovid19RadioBoxText(2)).orElseThrow();
        String covid19TestCompanyName = driverTools.getText(covid19TestCompany);

        String getPrice=getCovid19TestPrice();
        ArrayList<String> covidTestInfo = new ArrayList<String>();
        covidTestInfo.add(covid19TestCompanyName);
        covidTestInfo.add(getPrice);

        WebElement saveAddOnButton = driverTools.swipeToElement(SAVE_ADDON_BUTTON,Swipe.UP,5).orElseThrow();
        driverTools.click(saveAddOnButton);
        return covidTestInfo;
    }

    @Override
    public String getBaggagePrice (int baggageAmount) {
        Locator priceLocator = getBaggagePriceByWeightLocator(baggageAmount);
        driverTools.swipeToElement(priceLocator, Swipe.UP, 3);
        if (driverTools.waitForElementToPresence(priceLocator, 3).isPresent()) {
            String price = driverTools.getText(priceLocator).orElseThrow();
            logger.log("Baggage price for " + baggageAmount + " kg: " + price);
            Assertion.assertThat("Baggage price is non empty", !price.isEmpty(), is(true));
            return price;
        }
        logger.log("Sibling price not found for " + baggageAmount + " kg, falling back to legacy baggage price");
        return getBaggagePriceLegacy();
    }

    private String getBaggagePriceLegacy() {
        if (driverTools.isElementDisplayed(PRICE_CHEVRON)) {
            driverTools.click(PRICE_CHEVRON);
        }
        String price = driverTools.getText(BAGGAGE_ADDON_PRICE).orElseThrow();
        Assertion.assertThat("Baggage price is non empty", !price.isEmpty(), is(true));
        return price;
    }

    @Override
    public int getBaggageAmount(int baggageAmount,PriceDetail priceDetail){
        driverTools.swipeToElement(ADDONS_AMOUNT_BAGGAGE, Swipe.UP, 1);
        List<WebElement> amount = driverTools.elements(ADDONS_AMOUNT_BAGGAGE).orElseThrow();
        ArrayList<Integer> arrayPrice = new ArrayList<Integer>();

        for (WebElement element : amount) {
            String text = driverTools.getText(element);
            try {
                int price = (int) CoreFunctions.convertPriceSafely(text);
                arrayPrice.add(price);
            } catch (NumberFormatException ignored) {
            }
        }

        int finalBaggageAmount = 0;
        for (int i : arrayPrice) {
            finalBaggageAmount += i;
        }
        logger.log("baggage amount: " + finalBaggageAmount);
        return finalBaggageAmount;
    }

    @Override
    public void saveAddBaggage () {
        if (driverTools.isElementDisplayed(BAGGAGE_SAVE_BUTTON)) {
            logger.log("Saving baggage selection");
            driverTools.click(BAGGAGE_SAVE_BUTTON);
            return;
        }
    }

    private Locator getBaggageOptionLocator ( int weight){
        // New UI: "+20 kg" via tv_weight; legacy: "20 Kg"/"20 KG" via tv_value_weight.
        return new Locator("BAGGAGE " + weight + " KG", AppiumBy.xpath("//*[(contains(@resource-id,'tv_value_weight') and (@text='" + weight + " Kg' or @text='" + weight + " KG')) or (contains(@resource-id,'tv_weight') and (contains(@text,'+" + weight + "') or @text='+" + weight + " kg' or @text='+" + weight + " Kg')) or (@text='+" + weight + " kg' or @text='+" + weight + " Kg' or (contains(@text,'+" + weight + "') and contains(translate(@text,'KG','kg'),'kg')))][1]"));
    }

    private Locator getBaggageOptionLocator ( int weight,int index){
        // Prefer passenger-scoped chips under tv_pax_number, then nth weight chip on screen.
        return new Locator("BAGGAGE " + weight + " KG PAX " + index, AppiumBy.xpath("((//*[contains(@resource-id,'tv_pax_number')])[" + index + "]//following-sibling::*//*[(contains(@resource-id,'tv_value_weight') and (@text='" + weight + " Kg' or @text='" + weight + " KG')) or (contains(@resource-id,'tv_weight') and (contains(@text,'+" + weight + "') or @text='+" + weight + " kg')) or (@text='+" + weight + " kg' or @text='+" + weight + " Kg')]) | (//*[(contains(@resource-id,'tv_weight') and (contains(@text,'+" + weight + "') or @text='+" + weight + " kg')) or (@text='+" + weight + " kg' or @text='+" + weight + " Kg') or (contains(@text,'+" + weight + "') and contains(translate(@text,'KG','kg'),'kg'))])[" + index + "])[1]"));
    }

    private Locator getBaggagePriceByWeightLocator(int weight) {
        return new Locator("BAGGAGE_PRICE_" + weight + " KG", AppiumBy.xpath("//*[contains(@resource-id,'tv_weight') and (contains(@text,'+" + weight + "') or contains(@text,'" + weight + " kg') or contains(@text,'" + weight + " KG'))]/following-sibling::*[contains(@resource-id,'tv_price')]"));
    }

    @Override
    public boolean isTravelRequirementInfoPagePresent () {
        logger.log("Verifying travel requirements infopage");
        driverTools.swipeToElement(CONTINUE_PAYMENT_BUTTON, Swipe.UP, 10);
        driverTools.click(TRAVEL_REQUIREMENT_BANNER);
        driverTools.waitForElementToAppear(TRAVEL_REQUIREMENT_CLOSE_BUTTON);

        driverTools.switchToWebView();
        boolean infoPage = driverTools.waitForElementToAppear(TRAVEL_REQUIREMENT_WEBVIEW, 10).isPresent();

        driverTools.switchToNative();
        driverTools.click(TRAVEL_REQUIREMENT_CLOSE_BUTTON);
        return infoPage;
    }

    private boolean isCurrencyCorrect(String currency){
        Locator totalPrice = getTotalPriceLocator(currency);
        driverTools.swipeToElement(totalPrice, Swipe.UP, 2);
        String totalPriceText = driverTools.getText(totalPrice).orElseThrow();
        String currencyTotal = totalPriceText.replaceAll("[^a-zA-Z]", "");
        logger.log("Currency in total price: "+currencyTotal);
        return  currency.equalsIgnoreCase(currencyTotal);
    }

    @Override
    public boolean isPriceBreakdownCorrect (PriceDetail priceDetail, String currency) {
        logger.log("Verifying price detail");

        boolean ticketPriceBreakdown = verifyTicketPrice(priceDetail);
        logger.log("Ticket price breakdown correct: " + ticketPriceBreakdown);

        boolean priceCorrect = isPriceSumCorrect(currency);
        logger.log("Total price breakdown correct: " + priceCorrect);

        boolean extraPriceBreakdown = verifyExtraPrice(priceDetail);
        logger.log("Extra price breakdown correct: " + extraPriceBreakdown);

        boolean currencyCorrect = isCurrencyCorrect(currency);
        logger.log("Total currency correct: " + currencyCorrect);

        closePriceDetailIfOpen();
        return ticketPriceBreakdown && extraPriceBreakdown && priceCorrect;
    }

    private boolean verifyExtraPrice (PriceDetail priceDetail) {
        boolean baggageBreakdown = isBreakdownCorrect("baggage depart", priceDetail.baggageDepartAmount, priceDetail.baggageDepartPrice);
        boolean fullProtectionBreakdown = isBreakdownCorrect("full protection", priceDetail.adult + priceDetail.child, priceDetail.fullProtectionPrice);
        boolean fullBaggageInsuranceBreakdown = isBreakdownCorrect("baggage insurance", priceDetail.adult + priceDetail.child, priceDetail.baggageInsurancePrice);
        boolean covid19InsuranceBreakdown = isBreakdownCorrect("covid19 insurance", priceDetail.adult + priceDetail.child, priceDetail.covid19InsurancePrice);

        // mealPrice is already the total; amount=1 avoids pax/portion multiplier mismatch in isBreakdownCorrect.
        boolean mealBreakdown = isBreakdownCorrect("meal breakdown", 1, priceDetail.mealPrice);
        boolean seatDepartBreakdown = isBreakdownCorrect("seat depart",priceDetail.adult+priceDetail.child,priceDetail.seatDepartPrice);
        boolean covid19TestBreakdown = isBreakdownCorrect("covid19 Test",priceDetail.adult + priceDetail.child + priceDetail.infant,priceDetail.covid19TestPrice,priceDetail.covid19TestPriceText);
        boolean extraProtectionBreakdown = isBreakdownCorrect("Extra Protection",priceDetail.adult + priceDetail.child ,priceDetail.extraProtectionPrice,priceDetail.extraProtectionTitle);
        boolean discountPriceBreakdown = isBreakdownCorrect("discount",1,priceDetail.discountPrice);

        boolean mealBreakDownRoundTrip = true;
        if(priceDetail.flightMealsDetail != null){
            mealBreakDownRoundTrip = isBreakdownCorrectRoundTrip("meal", priceDetail.flightMealsDetail.totalPaxDepart, priceDetail.flightMealsDetail.mealPriceDepart, priceDetail.flightMealsDetail.totalPaxReturn, priceDetail.flightMealsDetail.mealPriceReturn);
        }
        boolean baggageBreakDownRoundTrip = true;
        if(priceDetail.flightBaggageDetail != null){
            baggageBreakDownRoundTrip = isBreakdownCorrectRoundTrip("baggage",priceDetail.flightBaggageDetail.baggageAmountDepart,priceDetail.flightBaggageDetail.baggagePriceDepart,priceDetail.flightBaggageDetail.baggageAmountReturn,priceDetail.flightBaggageDetail.baggagePriceReturn);
        }
        boolean seatBreakDownRoundTrip = true;
        if(priceDetail.flightSeatDetails != null){
            seatBreakDownRoundTrip = isBreakdownCorrectRoundTrip("seat",priceDetail.flightSeatDetails.totalPaxDepart,priceDetail.flightSeatDetails.seatPriceDepart,priceDetail.flightSeatDetails.totalPaxReturn,priceDetail.flightSeatDetails.seatPriceReturn);
        }

        return baggageBreakdown && fullBaggageInsuranceBreakdown && fullProtectionBreakdown && mealBreakdown && seatDepartBreakdown && covid19InsuranceBreakdown && covid19TestBreakdown && extraProtectionBreakdown && discountPriceBreakdown && mealBreakDownRoundTrip && baggageBreakDownRoundTrip && seatBreakDownRoundTrip;
    }

    private boolean isBreakdownCorrectRoundTrip (String type,int totalDepart,String totalPriceDepart,int totalReturn,String totalPriceReturn) {
        //total depart and return are either total baggage or total pax for meal and seat
        boolean departBreakdown = true,returnBreakdown = true;
        if(totalDepart > 0)
            departBreakdown = isBreakdownCorrect(type + " depart",totalDepart, totalPriceDepart);
        if(totalReturn > 0)
            returnBreakdown = isBreakdownCorrect(type + " return",totalReturn, totalPriceReturn);
        logger.log("depart breakdown " + type + " : " + departBreakdown);
        logger.log("return breakdown " + type + " : " + returnBreakdown);
        return departBreakdown && returnBreakdown;

    }

    private boolean verifyTicketPrice (PriceDetail priceDetail) {
        boolean adultBreakdown, childBreakdown, infantBreakdown;
        if (priceDetail.tripType.equals("oneway")) {
            adultBreakdown = isBreakdownCorrect("adult", priceDetail.adult, priceDetail.adultDepartPrice);
            childBreakdown = isBreakdownCorrect("child", priceDetail.child, priceDetail.childDepartPrice);
            infantBreakdown = isBreakdownCorrect("infant", priceDetail.infant, priceDetail.infantDepartPrice);
        } else if (priceDetail.tripType.equals("roundtrip")) {
            driverTools.waitForElementToAppear(FLIGHT_PAX_SECTION, 10);
            List<WebElement> flightSections = driverTools.elements(FLIGHT_PAX_SECTION).orElse(Collections.emptyList());
            if (flightSections.isEmpty()) {
                driverTools.waitForElementToAppear(PRICE_BREAKDOWN_CITY, 5);
                flightSections = driverTools.elements(PRICE_BREAKDOWN_CITY).orElse(Collections.emptyList());
            }
            if (flightSections.size() <= 1) {
                adultBreakdown = isBreakdownCorrect("adult", priceDetail.adult, priceDetail.adultDepartPrice, priceDetail.adultReturnPrice);
                childBreakdown = isBreakdownCorrect("child", priceDetail.child, priceDetail.childDepartPrice, priceDetail.childReturnPrice);
                infantBreakdown = isBreakdownCorrect("infant", priceDetail.infant, priceDetail.infantDepartPrice, priceDetail.infantReturnPrice);
            } else {
                adultBreakdown = isBreakdownCorrect("adult", priceDetail.adult, priceDetail.adultReturnPrice);
                childBreakdown = isBreakdownCorrect("child", priceDetail.child, priceDetail.childReturnPrice);
                infantBreakdown = isBreakdownCorrect("infant", priceDetail.infant, priceDetail.infantReturnPrice);
            }
        } else {
            throw new NotImplementedException("not implemented trip type");
        }
        return adultBreakdown && childBreakdown && infantBreakdown;
    }

    private boolean isBreakdownCorrect (String type, int amount, String expected) {
        logger.log("amount: " + amount);
        logger.log("expected amount: " + expected);
        if (expected != null) {
            double price = CoreFunctions.getIntFromString(expected).orElseThrow();
            double priceBookingForm = CoreFunctions.getIntFromString(getPrice(type, amount)).orElseThrow();
            if (type.equals("baggage insurance") || type.equals("full protection") || type.equals("covid19 insurance")) {
                price = price * amount;
            }
            logger.log(type + " price " + price);
            logger.log(type + " price on booking form " + priceBookingForm);
            return priceBookingForm == price;
        } else {
            return true;
        }
    }

    private boolean isBreakdownCorrect (String type,int amount, String expectedDepart, String other) {
       if (expectedDepart != null) {
            if (type.equals("covid19 Test") || type.equals("Extra Protection")) {
                String actualPriceText = getPrice(type, amount, other);
                if (actualPriceText == null) {
                    logger.log(type + " price row not found in breakdown");
                    return false;
                }
                double price = convertPrice(expectedDepart);
                double priceBookingForm = convertPrice(actualPriceText);
                if (type.equals("Extra Protection")) {
                    price = price * amount;
                }
                logger.log(type + " price Depart " + price);
                logger.log(type + " price booking form " + priceBookingForm);
                return priceBookingForm == price;
            } else {
                String actualPriceText = getPrice(type, amount);
                if (actualPriceText == null) {
                    logger.log(type + " price row not found in breakdown");
                    return false;
                }
                double priceDepart = convertPrice(expectedDepart);
                double priceReturn = convertPrice(other);
                double priceBookingForm = convertPrice(actualPriceText);
                logger.log(type + " price Depart " + priceDepart);
                logger.log(type + " price Return " + priceReturn);
                logger.log(type + " price booking form " + priceBookingForm);
                return priceBookingForm == priceDepart + priceReturn;
            }
        } else {
            return true;
        }
    }

    private boolean isPriceSumCorrect(String currency) {
        Locator totalPriceLocator = getTotalPriceLocator(currency);
        driverTools.swipeToElement(totalPriceLocator, Swipe.UP, 5);
        double actualTotalPrice = convertPrice(driverTools.getText(totalPriceLocator).orElseThrow());
        logger.log("Actual total price: " + actualTotalPrice);

        ArrayList<Double> arrayPrice = new ArrayList<>();
        boolean longBreakdown = false;
        int totalSwipe = 2;

        if (hasNewPriceBreakdownLineItems()) {
            logger.log("Collecting price breakdown from line items (tv_pax_price / tv_sub_row_price)");
            collectBreakdownLineItemPrices(arrayPrice, totalPriceLocator, totalSwipe);
            longBreakdown = arrayPrice.isEmpty();
        } else {
            logger.log("Collecting price breakdown from legacy detail prices");
            longBreakdown = collectLegacyBreakdownDetailPrices(currency, arrayPrice, totalPriceLocator, totalSwipe);
        }

        double sum = 0;
        for (double val : arrayPrice) {
            sum += val;
        }
        logger.log("Sum before rounding: " + sum);
        sum = doubleRound(sum);
        logger.log("Sum after rounding: " + sum);

        Assertion.assertThat("Total price breakdown is correct", sum, is(actualTotalPrice));

        if (longBreakdown) {
            driverTools.swipeDown(totalSwipe);
        }

        return true;
    }

    private boolean hasNewPriceBreakdownLineItems() {
        Locator newUiLineItems = new Locator("NEW_PRICE_BREAKDOWN_LINE_ITEMS", AppiumBy.xpath(
                "//*[contains(@resource-id,'tv_pax_price') or contains(@resource-id,'tv_sub_row_price') or contains(@resource-id,'tv_discount_item_price')]"));
        return driverTools.elements(newUiLineItems)
                .map(elements -> !elements.isEmpty())
                .orElse(false);
    }

    private void collectBreakdownLineItemPrices(ArrayList<Double> arrayPrice, Locator totalPriceLocator, int totalSwipe) {
        driverTools.waitForElementToAppear(PRICE_BREAKDOWN_LINE_ITEMS, 10);

        // Collect line items in document order. Identical fares (e.g. Adult and Child on the
        // same leg) are legitimate separate rows, so we must NOT de-duplicate by value.
        List<String> firstPass = readBreakdownLineItemTexts();
        List<String> orderedTexts = new ArrayList<>(firstPass);

        // For long breakdowns the total is off-screen: swipe to it and read again. The second
        // read overlaps the first, so drop the overlapping prefix instead of double counting.
        if (!driverTools.isElementDisplayed(totalPriceLocator)) {
            driverTools.swipeToElement(totalPriceLocator, Swipe.UP, totalSwipe);
            List<String> secondPass = readBreakdownLineItemTexts();
            int overlap = overlapCount(firstPass, secondPass);
            for (int i = overlap; i < secondPass.size(); i++) {
                orderedTexts.add(secondPass.get(i));
            }
        }

        for (String priceText : orderedTexts) {
            addPriceTextIfCountable(priceText, arrayPrice);
        }
    }

    private List<String> readBreakdownLineItemTexts() {
        List<WebElement> lineItems = driverTools.elements(PRICE_BREAKDOWN_LINE_ITEMS).orElse(Collections.emptyList());
        List<String> texts = new ArrayList<>();
        for (WebElement lineItem : lineItems) {
            texts.add(driverTools.getText(lineItem));
        }
        return texts;
    }

    /**
     * Returns the length of the longest suffix of {@code first} that equals a prefix of
     * {@code second}. This is the number of rows visible in both reads after a swipe, so the
     * caller can skip that many leading rows of the second read to avoid double counting.
     */
    private int overlapCount(List<String> first, List<String> second) {
        int max = Math.min(first.size(), second.size());
        for (int k = max; k > 0; k--) {
            boolean match = true;
            for (int i = 0; i < k; i++) {
                if (!Objects.equals(first.get(first.size() - k + i), second.get(i))) {
                    match = false;
                    break;
                }
            }
            if (match) {
                return k;
            }
        }
        return 0;
    }

    private boolean collectLegacyBreakdownDetailPrices(String currency, ArrayList<Double> arrayPrice, Locator totalPriceLocator, int totalSwipe) {
        driverTools.waitForElementToAppear(PRICE_BREAKDOWN, 20);
        List<WebElement> prices = driverTools.elements(PRICE_BREAKDOWN).orElseThrow();
        int totalPrices = prices.size();
        boolean longBreakdown = false;
        logger.log("Initial size: " + totalPrices);

        for (int i = 0; i < totalPrices;) {
            addPriceTextIfCountable(driverTools.getText(prices.get(i)), arrayPrice);

            i++;
            if (i == totalPrices && !driverTools.isElementDisplayed(totalPriceLocator)) {
                longBreakdown = true;
                String lastPrice = driverTools.getText(prices.get(totalPrices - 1));
                driverTools.swipeToElement(totalPriceLocator, Swipe.UP, totalSwipe);
                prices = driverTools.elements(nextPrice(lastPrice, currency)).orElseThrow();
                totalPrices = prices.size();
                i = 0;
            }
        }

        removeSummaryPricesFromBreakdown(arrayPrice, currency);
        return longBreakdown;
    }

    private void addPriceTextIfCountable(String priceText, ArrayList<Double> arrayPrice) {
        if (priceText == null) {
            return;
        }
        String normalized = priceText.trim().toLowerCase();
        if (normalized.contains("points") || normalized.contains("poin")
                || normalized.contains("included") || normalized.contains("termasuk")
                || normalized.contains("free") || normalized.contains("gratis")) {
            logger.log("Skipping non-price item: " + priceText);
            return;
        }
        if (normalized.matches(".*\\d.*")) {
            double result = convertPrice(priceText.trim());
            arrayPrice.add(result);
            logger.log("Added breakdown price: " + result);
        } else {
            logger.log("Skipping non-numeric value: " + priceText);
        }
    }

    private void removeSummaryPricesFromBreakdown(ArrayList<Double> arrayPrice, String currency) {
        Locator totalPriceLocator = getTotalPriceLocator(currency);
        double actualTotalPrice = convertPrice(driverTools.getText(totalPriceLocator).orElseThrow());

        if (arrayPrice.contains(actualTotalPrice)) {
            arrayPrice.remove(actualTotalPrice);
            logger.log("Removed total price from breakdown list to avoid double counting: " + actualTotalPrice);
        }
        if (driverTools.isElementDisplayed(PRICE_BREAKDOWN_SUBTOTAL)) {
            String subtotalText = driverTools.getText(PRICE_BREAKDOWN_SUBTOTAL).orElse(null);
            if (subtotalText != null && subtotalText.trim().matches(".*\\d.*")) {
                double subtotalValue = convertPrice(subtotalText.trim());
                if (arrayPrice.remove(Double.valueOf(subtotalValue))) {
                    logger.log("Removed subtotal from breakdown list to avoid double counting: " + subtotalValue);
                }
            }
        }
        if (driverTools.isElementDisplayed(TOTAL_AFTER_CASHBACK_PRICE)) {
            String totalAfterCashbackText = driverTools.getText(TOTAL_AFTER_CASHBACK_PRICE).orElse(null);
            if (totalAfterCashbackText != null && totalAfterCashbackText.trim().matches(".*\\d.*")) {
                double totalAfterCashbackValue = convertPrice(totalAfterCashbackText.trim());
                if (arrayPrice.remove(Double.valueOf(totalAfterCashbackValue))) {
                    logger.log("Removed 'Total after cashback' from breakdown list to avoid double counting: " + totalAfterCashbackValue);
                }
            }
        }
        if (driverTools.isElementDisplayed(CASHBACK)) {
            String cashbackPriceText = driverTools.getText(CASHBACK_PRICE_VALUE).orElse(null);
            if (cashbackPriceText != null && !cashbackPriceText.trim().isEmpty()) {
                String normalized = cashbackPriceText.trim().toLowerCase();
                boolean isNonMonetary = normalized.contains("points") || normalized.contains("poin")
                        || normalized.contains("included") || normalized.contains("termasuk")
                        || !normalized.matches(".*\\d.*");
                if (!isNonMonetary) {
                    double cashbackValue = convertPrice(cashbackPriceText);
                    if (arrayPrice.remove(Double.valueOf(cashbackValue))) {
                        logger.log("Removed cashback from breakdown list to avoid double counting: " + cashbackValue);
                    }
                }
            }
        }
    }

    private static Locator nextPrice(String firstPrice, String currency) {
        return new Locator("NEXT_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_price_breakdown_detail_price') and @text='" + firstPrice + "']//following::*[contains(@resource-id,'iv_flight_price_breakdown_detail_bullet')]//following-sibling::* [contains(@resource-id,'tv_flight_price_breakdown_detail_price') and contains(@text,'" + currency + "')]"));
    }

    private String getPrice (String type,int amount) {
        Locator locator = getPriceBreakdownLocator(type, amount);
        driverTools.swipeToElement(locator, Swipe.UP, 3);
        List<WebElement> priceElements = driverTools.elements(locator).orElse(Collections.emptyList());
        if (priceElements.isEmpty() && driverTools.waitForElementToPresence(locator, 3).isPresent()) {
            priceElements = driverTools.elements(locator).orElse(Collections.emptyList());
        }
        if (priceElements.isEmpty()) {
            return null;
        }
        if (priceElements.size() == 1) {
            String price = driverTools.getText(priceElements.get(0));
            logger.log(type + " price:  " + price);
            return price;
        }
        double total = 0;
        for (WebElement priceElement : priceElements) {
            total += convertPrice(driverTools.getText(priceElement));
        }
        logger.log(type + " combined price: " + total);
        return String.valueOf((long) total);
    }

    private String getPrice (String type,int amount,String other) {
        Locator locator = getPriceBreakdownLocator(type, amount, other);
        driverTools.swipeToElement(locator, Swipe.UP, 3);
        if (driverTools.waitForElementToPresence(locator, 5).isEmpty()) {
            return null;
        }
        String price = driverTools.getText(locator).orElse(null);
        logger.log(type + " price:  " + price);
        return price;
    }

    private Locator getBreakdownRowPriceLocator(String labelText) {
        return new Locator("Price breakdown row", AppiumBy.xpath(
                "(//*[contains(@resource-id,'tv_sub_row_label') and contains(@text,'" + labelText + "')]"
                        + "/following-sibling::*[contains(@resource-id,'tv_sub_row_price')]"
                        + " | //*[contains(@resource-id,'tv_pax_label') and contains(@text,'" + labelText + "')]"
                        + "/following-sibling::*[contains(@resource-id,'tv_pax_price')]"
                        + " | //*[contains(@resource-id,'tv_discount_item_label') and contains(@text,'" + labelText + "')]"
                        + "/following-sibling::*[contains(@resource-id,'tv_discount_item_price')]"
                        + " | //*[contains(@resource-id,'breakdown_detail_name') and contains(@text,'" + labelText + "')]"
                        + "/following-sibling::*[contains(@resource-id,'breakdown_detail_price')]"
                        + " | //*[contains(@resource-id,'tv_flight_price_breakdown_detail_name') and contains(@text,'" + labelText + "')]"
                        + "/following-sibling::*[contains(@resource-id,'tv_flight_price_breakdown_detail_price')])[1]"));
    }

    private Locator getPriceBreakdownLocator (String type,int amount,String other) {
        String cleanedOther = other;
        if (other.toLowerCase().contains("extra protection")) {
            cleanedOther = other.replaceAll("(?i)\\s*extra\\s+protection\\s*$", "").trim();
        }
        return getBreakdownRowPriceLocator(cleanedOther + " (x" + amount + ")");
    }

    private Locator getTotalPriceLocator(String currency) {
        return new Locator("Total price", AppiumBy.xpath("//*[@class='android.view.ViewGroup' and (./*[contains(@text,'Bayar sekarang') or contains(@text,'Pay now') or contains(@text,'Pay Now')])]//*[contains(@resource-id, 'tv_flight_price_breakdown_detail_price') and contains(@text, '" + currency + "')]"));
    }

    private Locator getPriceBreakdownLocator (String type,int amount) {
        String typeId, typeEng;
        switch (type.toLowerCase()) {
            case "adult":
            case "dewasa": {
                typeId = "Dewasa";
                typeEng = "Adult";
                break;
            }
            case "child":
            case "anak": {
                typeId = "Anak";
                typeEng = "Child";
                break;
            }
            case "infant":
            case "bayi": {
                typeId = "Bayi";
                typeEng = "Infant";
                break;
            }
            case "meal depart":
            case "meal breakdown":
            case "makanan depart": {
                // Android EN: "Meal (xN)"; iOS EN: "Meals (xN)"; ID: "Makanan (xN)". N = portions.
                return new Locator("Price breakdown Meal", AppiumBy.xpath("(//*[contains(@resource-id,'tv_sub_row_label') and (starts-with(@text,'Meals') or starts-with(@text,'Meal') or starts-with(@text,'Makanan'))]/following-sibling::*[contains(@resource-id,'tv_sub_row_price')] | //*[contains(@resource-id,'breakdown_detail_name') and (starts-with(@text,'Meals') or starts-with(@text,'Meal') or starts-with(@text,'Makanan'))]/following-sibling::*[contains(@resource-id,'breakdown_detail_price')])[1]"));
            }
            case "meal return":
            case "makanan return": {
                return new Locator("Price breakdown Meal return", AppiumBy.xpath("((//*[contains(@resource-id,'tv_sub_row_label') and (starts-with(@text,'Meals') or starts-with(@text,'Meal') or starts-with(@text,'Makanan'))]/following-sibling::*[contains(@resource-id,'tv_sub_row_price')] | //*[contains(@resource-id,'breakdown_detail_name') and (starts-with(@text,'Meals') or starts-with(@text,'Meal') or starts-with(@text,'Makanan'))]/following-sibling::*[contains(@resource-id,'breakdown_detail_price')])[2]"));
            }
            case "baggage depart":
            case "bagasi depart": {
                return getBreakdownRowPriceLocator("Baggage (" + amount + "kg)");
            }
            case "baggage return":
            case "bagasi return": {
                return new Locator("Price breakdown baggage return", AppiumBy.xpath(
                        "((//*[contains(@resource-id,'tv_sub_row_label') and (contains(@text,'Baggage (" + amount + "kg)') or contains(@text,'Bagasi (" + amount + "kg)') )]"
                                + "/following-sibling::*[contains(@resource-id,'tv_sub_row_price')]"
                                + " | //*[contains(@resource-id,'breakdown_detail_name') and (contains(@text,'Baggage (" + amount + "kg)') or contains(@text,'Bagasi (" + amount + "kg)') )]"
                                + "/following-sibling::*[contains(@resource-id,'breakdown_detail_price')])[2]"));
            }
            case "full protection":
            case "perlindungan penuh": {
                return new Locator("Price breakdown full protection", AppiumBy.xpath(
                        "(//*[contains(@resource-id,'tv_sub_row_label') and (contains(@text,'Full Protection (x" + amount + ")') or contains(@text,'Perlindungan Penuh (x" + amount + ")'))]"
                                + "/following-sibling::*[contains(@resource-id,'tv_sub_row_price')]"
                                + " | //*[contains(@resource-id,'breakdown_detail_name') and (contains(@text,'Full Protection (x" + amount + ")') or contains(@text,'Perlindungan Penuh (x" + amount + ")'))]"
                                + "/following-sibling::*[contains(@resource-id,'breakdown_detail_price')])[1]"));
            }
            case "baggage insurance":
            case "asuransi bagasi": {
                return new Locator("Price breakdown baggage insurance", AppiumBy.xpath(
                        "(//*[contains(@resource-id,'tv_sub_row_label') and (contains(@text,'Baggage Insurance (x" + amount + ")') or contains(@text,'Asuransi Bagasi (x" + amount + ")'))]"
                                + "/following-sibling::*[contains(@resource-id,'tv_sub_row_price')]"
                                + " | //*[contains(@resource-id,'breakdown_detail_name') and (contains(@text,'Baggage Insurance (x" + amount + ")') or contains(@text,'Asuransi Bagasi (x" + amount + ")'))]"
                                + "/following-sibling::*[contains(@resource-id,'breakdown_detail_price')])[1]"));
            }
            case "covid19 insurance":
            case "asuransi covid-19": {
                return new Locator("Price breakdown covid19 insurance", AppiumBy.xpath(
                        "(//*[contains(@resource-id,'tv_sub_row_label') and (contains(@text,'Covid-19 Insurance (x" + amount + ")') or contains(@text,'Asuransi Covid-19 (x" + amount + ")'))]"
                                + "/following-sibling::*[contains(@resource-id,'tv_sub_row_price')]"
                                + " | //*[contains(@resource-id,'breakdown_detail_name') and (contains(@text,'Covid-19 Insurance (x" + amount + ")') or contains(@text,'Asuransi Covid-19 (x" + amount + ")'))]"
                                + "/following-sibling::*[contains(@resource-id,'breakdown_detail_price')])[1]"));
            }
            case "seat depart": {
                return getBreakdownRowPriceLocator("Seat (x" + amount + ")");
            }
            case "seat return": {
                return new Locator("Price breakdown seat return", AppiumBy.xpath(
                        "((//*[contains(@resource-id,'tv_sub_row_label') and (contains(@text,'Seat (x" + amount + ")') or contains(@text,'Kursi (x" + amount + ")'))]"
                                + "/following-sibling::*[contains(@resource-id,'tv_sub_row_price')]"
                                + " | //*[contains(@resource-id,'tv_flight_price_breakdown_detail_name') and (contains(@text,'Seat (x" + amount + ")') or contains(@text,'Kursi (x" + amount + ")'))]"
                                + "/following-sibling::*[contains(@resource-id,'tv_flight_price_breakdown_detail_price')]"
                                + " | //*[contains(@resource-id,'breakdown_detail_name') and (contains(@text,'Seat (x" + amount + ")') or contains(@text,'Kursi (x" + amount + ")'))]"
                                + "/following-sibling::*[contains(@resource-id,'breakdown_detail_price')])[2]"));
            }
            case "discount":{
                return getBreakdownRowPriceLocator("Discount");
            }
            case  "harga gledek":{
                return new Locator(type.toUpperCase() + " breakdown", AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_price_breakdown_detail_name') and (contains(@text,'Harga Gledek') or contains(@text,'Discount') or contains(@text,'Diskon'))]//following-sibling::*[contains(@resource-id,'tv_flight_price_breakdown_detail_price')]"));
            }
            case "connecting flight protection":
                return new Locator(type.toUpperCase() + " breakdown", AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_price_breakdown_detail_name') and ((contains(@text,'Proteksi Transfer Mandiri') or contains(@text,'Connecting Flight Protection') or contains(@text,'Self Transfer Protection')) and contains(@text,'(x" + amount + ")'))]//following-sibling::*[contains(@resource-id,'tv_flight_price_breakdown_detail_price')]"));
            default:
                throw new NotImplementedException("Unimplemented passenger type!");
        }
        return new Locator("Price breakdown pax", AppiumBy.xpath(
                "(//*[contains(@resource-id,'tv_pax_label') and (contains(@text,'" + typeEng + " (x" + amount + ")') or contains(@text,'" + typeId + " (x" + amount + ")'))]"
                        + "/following-sibling::*[contains(@resource-id,'tv_pax_price')]"
                        + " | //*[contains(@resource-id,'breakdown_detail_name') and (contains(@text,'" + typeEng + " (x" + amount + ")') or contains(@text,'" + typeId + " (x" + amount + ")'))]"
                        + "/following-sibling::*[contains(@resource-id,'breakdown_detail_price')])[1]"));
    }

    @Override
    public void continueToPayment () {
        String methodName = "continueToPayment";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.swipeToElement(CONTINUE_PAYMENT_BUTTON,Swipe.UP,15);
            logger.log("Continue to payment");
            driverTools.click(CONTINUE_PAYMENT_BUTTON);
            logger.log("Confirm payment");
            driverTools.waitForElementToAppear(CONFIRM_PAYMENT_BUTTON);
            driverTools.click(CONFIRM_PAYMENT_BUTTON);
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }
    public void errorHandling(){
        if (Configs.ENVIRONMENT == Environment.PROD) {
            return;
        }
        if(driverTools.isElementDisplayed(ERROR_POPUP)) {
            Assertion.fail("There is error on booking to payment page step, date and time at "+ CoreFunctions.getCurrentDateTime());
        }
    }
    @Override
    public void baggageAllowancePopup() {
        String methodName = "baggageAllowancePopup";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            for (int i = 0; i < 30; i++) {
                if (driverTools.isElementDisplayed(BAGGAGE_ALLOWANCE)) {
                    errorHandling();
                    driverTools.click(BAGGAGE_ALLOWANCE);
                    return;
                }
                if (driverTools.isElementDisplayed(ORDER_ID)) {
                    return;
                }
                driverTools.getPageSource();
                if (driverTools.waitForElementToAppear(BAGGAGE_ALLOWANCE, 2).isPresent()) {
                    errorHandling();
                    driverTools.click(BAGGAGE_ALLOWANCE);
                    return;
                }
                if (driverTools.waitForElementToAppear(ORDER_ID, 1).isPresent()) {
                    return;
                }
            }
        } finally {
            errorHandling();
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    // TODO: verify on-device — best-guess locator for the "No, thank you" button on the baggage offer add-ons page.
    private static final Locator NO_THANKS_BAGGAGE_OFFER = new Locator("NO_THANKS_BAGGAGE_OFFER",
            AppiumBy.xpath("//*[contains(@text,'No, thank you') or contains(@text,'No, thanks') or contains(@text,'Nggak, deh') or contains(@text,'Tidak, terima kasih') or contains(@text,'Lewati')]"));

    @Override
    public void declineBaggageOffer() {
        String methodName = "declineBaggageOffer";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForElementToAppear(NO_THANKS_BAGGAGE_OFFER, 10).ifPresent(driverTools::click);
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void skipFullProtection () {
        String methodName = "skipFullProtection";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForElementToDisappear(CONTINUE_PAYMENT_BUTTON);
            if(driverTools.waitForElementToAppear(ORDER_ID,6).isPresent()){
                return;
            }
            boolean orderIdMissing = driverTools.waitForElementToAppear(ORDER_ID,5).isEmpty();
            boolean skipProtectionVisible = driverTools.isElementDisplayed(SKIP_FULL_PROTECTION_NATIVE);

            if(orderIdMissing && skipProtectionVisible) {
                driverTools.waitForWebview();
                driverTools.switchToWebViewUrl("insurance-popup", 3);
                driverTools.waitForElementToAppear(SKIP_FULL_PROTECTION).ifPresent(driverTools::clickWithJavascriptExecutor);
                driverTools.switchToNative();

                if(driverTools.waitForElementToAppear(ORDER_ID,3).isPresent())
                    return;
            }
            driverTools.waitForElementToAppear(PRICE_CHANGE,8).ifPresent(driverTools::click);
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void skipFullProtectionPrp () {
        // Prefer new Disruption Protection UI (EN/ID), then legacy "No / Nggak / don't…"
        if (driverTools.waitForElementToAppear(WITHOUT_PROTECTION_OPTION, 8).isPresent()) {
            logger.log("[skipFullProtectionPrp] Disruption Protection page — selecting Without protection / Tanpa proteksi");
            if (driverTools.waitForElementToAppear(WITHOUT_PROTECTION_RADIO, 5).isPresent()) {
                driverTools.clickElementByCoordinate(WITHOUT_PROTECTION_RADIO);
            } else {
                driverTools.clickElementByCoordinate(WITHOUT_PROTECTION_OPTION);
            }
            if (driverTools.waitForElementToAppear(CONTINUE_WITHOUT_PROTECTION_BTN, 8).isPresent()) {
                logger.log("[skipFullProtectionPrp] clicking Continue without / Lanjut tanpa proteksi");
                driverTools.click(CONTINUE_WITHOUT_PROTECTION_BTN);
                return;
            }
            if (driverTools.waitForElementToAppear(CONTINUE_AFTER_SKIP_PROTECTION, 5).isPresent()) {
                logger.log("[skipFullProtectionPrp] clicking continue CTA after Without / Tanpa select");
                driverTools.click(CONTINUE_AFTER_SKIP_PROTECTION);
                return;
            }
            logger.warn("[skipFullProtectionPrp] Without-protection continue CTA not found — not clicking 'Continue with protection' / 'Lanjut dengan proteksi'");
            return;
        }
        if (driverTools.waitForElementToAppear(SKIP_FULL_PROTECTION_NATIVE, 5).isPresent()) {
            logger.log("[skipFullProtectionPrp] legacy skip button — clicking");
            driverTools.click(SKIP_FULL_PROTECTION_NATIVE);
            return;
        }
        logger.log("[skipFullProtectionPrp] no protection interstitial found — nothing to skip");
    }

    @Override
    public boolean isBookingFormPageDisplayed() {
        return driverTools.waitForElementToPresence(TRIP_DETAIl, 15).isPresent()
                || driverTools.waitForElementToPresence(CONTACT_DETAIL, 15).isPresent()
                || driverTools.waitForElementToPresence(FILL_CONTACT_DETAILS, 5).isPresent();
    }

    @Override
    public String addFullProtection () {
        logger.log("Adding full protection");
        if (driverTools.swipeToElement(ADD_FULL_PROTECTION, Swipe.UP, 4).isPresent()) {
            driverTools.click(ADD_FULL_PROTECTION);
            return driverTools.getText(FULL_PROTECTION_PRICE).orElseThrow();
        } else {
            return null;
        }
    }

    @Override
    public String addBaggageInsurance() {
        logger.log("Adding baggage insurance");
        if (driverTools.swipeToElement(ADD_BAGGAGE_INSURANCE, Swipe.UP, 4).isPresent()) {
            driverTools.click(ADD_BAGGAGE_INSURANCE);
            return driverTools.getText(BAGGAGE_INSURANCE_PRICE).orElseThrow();
        } else {
            return null;
        }
    }

    @Override
    public String addCovid19Insurance() {
        logger.log("Adding covid19 insurance");
        if (driverTools.swipeToElement(ADD_COVID_19_INSURANCE, Swipe.UP, 4).isPresent()) {
            driverTools.click(ADD_COVID_19_INSURANCE);
            return driverTools.getText(COVID_19_INSURANCE_PRICE).orElseThrow();
        } else {
            return null;
        }
    }

    private Locator getExtraProtectionLocatorTitle ( int index) {
        return new Locator("Extra Protection Title", AppiumBy.xpath("(//*[contains(@resource-id,'card_flight_extra_protection')]//*[contains(@resource-id,'tv_title')])[" + index + "]"));
    }

    private Locator getExtraProtectionLocatorPrice ( int index) {
        return new Locator("Extra Protection Price", AppiumBy.xpath("(//*[contains(@resource-id,'card_flight_extra_protection')]//*[contains(@resource-id,'tv_price')])[" + index + "]"));
    }

    private Locator getExtraProtectionLocatorAddBtn ( int index) {
        return new Locator("Extra Protection Add button", AppiumBy.xpath("(//*[contains(@resource-id,'card_flight_extra_protection')]//*[contains(@resource-id,'tds_button_view')])["+index+"]"));
    }

    private Locator getCompoundExtraProtectionTitleLocator(int index) {
        return new Locator("COMPOUND_EXTRA_PROTECTION_TITLE_" + index, AppiumBy.xpath("(//*[contains(@resource-id,'iv_card_compound')]//following-sibling::*[contains(@resource-id,'tv_title')])[" + index + "]"));
    }

    private Locator getCompoundExtraProtectionPriceLocator(int index) {
        return new Locator("COMPOUND_EXTRA_PROTECTION_PRICE_" + index, AppiumBy.xpath("(//*[contains(@resource-id,'iv_card_compound')]//following-sibling::*[contains(@resource-id,'tv_title')])[" + index + "]//following-sibling::*[contains(@resource-id,'tv_base_price') or contains(@resource-id,'tv_price')][1]"));
    }

    private Locator getCompoundExtraProtectionAddBtnLocator(int index) {
        return new Locator("COMPOUND_EXTRA_PROTECTION_ADD_BTN_" + index, AppiumBy.xpath("(//*[contains(@resource-id,'iv_card_compound')]//following-sibling::*[contains(@resource-id,'tv_title')])[" + index + "]//following-sibling::*//*[contains(@resource-id,'tds_button_view')]"));
    }

    private Locator getRvExtraProtectionTitleLocator(int index) {
        return new Locator("EXTRA_PROTECTION_TITLE_" + index, AppiumBy.xpath("(" + EXTRA_PROTECTION_LIST_XPATH + ")[" + index + "]//*[contains(@resource-id,'tv_protection_title')]"));
    }

    private Locator getRvExtraProtectionPriceLocator(int index) {
        return new Locator("EXTRA_PROTECTION_PRICE_" + index, AppiumBy.xpath("(" + EXTRA_PROTECTION_LIST_XPATH + ")[" + index + "]//*[contains(@resource-id,'ll_price_text_component')]//*[contains(@resource-id,'tv_flight_body')][1]"));
    }

    private Locator getRvExtraProtectionCheckboxLocator(int index) {
        return new Locator("EXTRA_PROTECTION_CHECKBOX_" + index, AppiumBy.xpath("(" + EXTRA_PROTECTION_LIST_XPATH + ")[" + index + "]//*[contains(@resource-id,'cb_protection')]"));
    }

    private Locator getRvExtraProtectionSelectedCheckLocator(int index) {
        return new Locator("EXTRA_PROTECTION_SELECTED_CHECK_" + index, AppiumBy.xpath("(" + EXTRA_PROTECTION_LIST_XPATH + ")[" + index + "]//*[contains(@resource-id,'iv_check')]"));
    }

    private void removePreAddedExtraProtections() {
        for (int attempt = 0; attempt < 10; attempt++) {
            List<WebElement> preAddedChecks = driverTools.elements(EXTRA_PROTECTION_PRE_ADDED_CHECK).orElse(Collections.emptyList());
            if (preAddedChecks.isEmpty()) {
                logger.log("No pre-added extra protection checkboxes found");
                return;
            }
            logger.log("Found " + preAddedChecks.size() + " pre-added extra protection checkbox(es), removing next one");
            WebElement preAddedCheck = driverTools.swipeToElement(EXTRA_PROTECTION_PRE_ADDED_CHECK, Swipe.UP, 5).orElseThrow();
            driverTools.click(preAddedCheck);
            driverTools.dummyWait(1);
        }
    }

    private ArrayList<String> addExtraProtectionViaCheckbox(int index) {
        removePreAddedExtraProtections();

        WebElement checkElement = driverTools.swipeToElement(getRvExtraProtectionCheckboxLocator(index), Swipe.UP, 8).orElseThrow(
                () -> new NoSuchElementException("Extra protection checkbox not found at index " + index));

        String title = driverTools.getText(getRvExtraProtectionTitleLocator(index)).orElseThrow();
        String price = driverTools.getText(getRvExtraProtectionPriceLocator(index)).orElseThrow();

        if (driverTools.waitForElementToPresence(getRvExtraProtectionSelectedCheckLocator(index), 2).isEmpty()) {
            driverTools.click(checkElement);
        }

        logger.log(title + " is Added with Price of " + price);
        ArrayList<String> protectionInfo = new ArrayList<>();
        protectionInfo.add(title.trim());
        protectionInfo.add(price.trim());
        return protectionInfo;
    }

    @Override
    public ArrayList<String> addExtraProtection(int index) {
        waitForBookingPageTwo();

        if (driverTools.waitForElementToPresence(EXTRA_PROTECTION_LIST, 5).isPresent()) {
            logger.log("Extra protection list UI is found");
            return addExtraProtectionViaCheckbox(index);
        }

        ArrayList<String> protectionInfo = new ArrayList<>();
        String title;
        String price;

        if (driverTools.swipeToElement(getExtraProtectionLocatorTitle(index), Swipe.UP, 5).isPresent()
                && driverTools.waitForElementToAppear(getExtraProtectionLocatorTitle(index), 5).isPresent()) {
            logger.log("Legacy extra protection card is found");
            WebElement extraProtectionLocatorTitle = driverTools.waitForElementToAppear(getExtraProtectionLocatorTitle(index)).orElseThrow();
            title = driverTools.getText(extraProtectionLocatorTitle);

            Locator addBtn = getExtraProtectionLocatorAddBtn(index);
            WebElement addBtnElement = driverTools.swipeToElement(addBtn, Swipe.UP, 2).orElseThrow();
            String btnText = driverTools.getText(addBtnElement);
            if ("Cancel".equals(btnText) || "Batalkan".equals(btnText)) {
                driverTools.click(addBtnElement);
            }

            WebElement extraProtectionLocatorPrice = driverTools.swipeToElement(getExtraProtectionLocatorPrice(index), Swipe.UP, 2).orElseThrow();
            price = driverTools.getText(extraProtectionLocatorPrice);
            driverTools.click(addBtn);
        } else if (driverTools.swipeToElement(getCompoundExtraProtectionTitleLocator(index), Swipe.UP, 8).isPresent()
                && driverTools.waitForElementToAppear(getCompoundExtraProtectionTitleLocator(index), 5).isPresent()) {
            logger.log("Compound card extra protection is found");
            WebElement titleElement = driverTools.waitForElementToAppear(getCompoundExtraProtectionTitleLocator(index)).orElseThrow();
            title = driverTools.getText(titleElement);

            WebElement addBtnElement = driverTools.waitForElementToPresence(getCompoundExtraProtectionAddBtnLocator(index)).orElseThrow();
            String btnText = driverTools.getText(addBtnElement);
            if (btnText.contains("Cancel") || btnText.contains("Batalkan")) {
                driverTools.click(addBtnElement);
            }
            driverTools.click(getCompoundExtraProtectionAddBtnLocator(index));

            WebElement priceElement = driverTools.waitForElementToPresence(getCompoundExtraProtectionPriceLocator(index)).orElseThrow();
            price = driverTools.getText(priceElement);
        } else {
            throw new NoSuchElementException("Extra protection card not found on booking form page 2");
        }

        logger.log(title + " is Added with Price of " + price);
        protectionInfo.add(title);
        protectionInfo.add(price);
        return protectionInfo;
    }

    @Override
    public void removeExtraProtection(){
        if (driverTools.waitForElementToPresence(EXTRA_PROTECTION_LIST, 5).isPresent()) {
            removePreAddedExtraProtections();
            return;
        }
        Locator addBtn = getExtraProtectionLocatorAddBtn(2);
        Optional<WebElement> addBtnElement = driverTools.swipeToElement(addBtn,Swipe.UP,4);
        if (addBtnElement.isEmpty()) {
            logger.log("Extra protection add/cancel button not found — already removed");
            return;
        }
        String btnText = driverTools.getText(addBtnElement.get());
        if("Cancel".equals(btnText) || "Batalkan".equals(btnText) )
            driverTools.click(addBtnElement.get());
    }

    @Override
    public void removeExtraProtectionAll(){
        for(int i=1;i<=4;i++){
            driverTools.swipeToElement(CANCEL_BTN_EXTRA_PROTECTION,Swipe.UP,2);
            if(driverTools.isElementDisplayed(CANCEL_BTN_EXTRA_PROTECTION)){
                driverTools.click(CANCEL_BTN_EXTRA_PROTECTION);
            }
        }
    }

    @Override
    public void openPriceDetail(){
        if (driverTools.swipeToElement(PRICE_DETAIL_PAY_NOW_CHEVRON, Swipe.UP, 10).isPresent()) {
            driverTools.click(PRICE_DETAIL_PAY_NOW_CHEVRON);
        } else {
            driverTools.swipeToElement(PRICE_DETAIL_ARROW, Swipe.UP, 15);
            driverTools.click(PRICE_DETAIL_ARROW);
        }
        driverTools.waitForElementToAppear(PRICE_BREAKDOWN_TITLE, 15).orElseThrow(
                () -> new NoSuchElementException("Price breakdown did not open"));
    }

    private void closePriceDetailIfOpen() {
        if (driverTools.isElementDisplayed(PRICE_BREAKDOWN_TITLE)) {
            driverTools.click(CLOSE_PRICE_DETAILS);
            driverTools.waitForElementToDisappear(PRICE_BREAKDOWN_TITLE);
        }
    }

    private String selectSeatForPassengers(int noOfPassenger){
        String price;
        for (int i = 0; i < noOfPassenger; i++){
            boolean swipeUp = true;
            driverTools.waitForElementToPresence(CONTINUE_POPUP, 3).ifPresent(driverTools::click);

            for(int tries = 0; tries<5; tries++){
                boolean isSeatAvailable = driverTools.waitForElementToAppear(EMPTY_SEAT,5).isPresent();

                for (int j = 0; j < 2 && !isSeatAvailable; j++) {

                    logger.log("Swiping vertically to find empty seat");

                    if(swipeUp){driverTools.swipeUp(SEAT_CONTAINER);}
                    else{driverTools.swipeDown(SEAT_CONTAINER);}

                    isSeatAvailable =driverTools.waitForElementToAppear(EMPTY_SEAT,5).isPresent();
                    driverTools.waitForElementToPresence(CONTINUE_POPUP, 3).ifPresent(driverTools::click);
                }

                if (isSeatAvailable) {
                    List<WebElement> e = driverTools.elements(EMPTY_SEAT).orElseThrow();
                    Locator emptySeat = getEmptySeatLocator(CoreFunctions.generateRandomInt(2,e.size()-1));
                    driverTools.swipeToElement(emptySeat,Swipe.UP,3);
                    driverTools.click(emptySeat);
                    driverTools.waitForElementToPresence(CONTINUE_POPUP, 3).ifPresent(driverTools::click);
                    if(driverTools.isElementDisplayed(ADDON_SAVE_BUTTON))
                        break;
                }
                swipeUp = !swipeUp;
                driverTools.waitForElementToPresence(CONTINUE_POPUP, 3).ifPresent(driverTools::click);
            }
        }
        WebElement seatTotalValue = driverTools.waitForElementToAppear(SEAT_TOTAL_VALUE).orElseThrow();
        price = driverTools.getText(seatTotalValue);
        logger.log("Seat Price " + price);
        if(driverTools.isElementDisplayed(ADDON_SAVE_BUTTON)){
            driverTools.click(ADDON_SAVE_BUTTON);
        }
        Assertion.assertThat("Seat price is non empty",!price.isEmpty() , is(true));
        return price;
    }

    private Locator getEmptySeatLocator(int index){
        index += 1;
        logger.log("index of empty seat to select: " + index);
        return new Locator("EMPTY_SEAT_LOCATOR", AppiumBy.xpath("(//*[contains(@resource-id,'v_seat')])[" + index + "]"));
    }

    @SneakyThrows
    public String addSeat(PriceDetail priceDetail, String currency) {
        openSeatsAddon();
        String seatPrice = selectSeatForPassengers(priceDetail.adult + priceDetail.child);
        String currencySeat = seatPrice.replaceAll("[^a-zA-Z]", "");
        logger.log("Seat currency: "+ currencySeat);
        Assertion.assertThat("Seat currency is correct", currency.equalsIgnoreCase(currencySeat) , is(true));

        return seatPrice;
    }

   /**
     * Clicks on the seat add-on button.
     * This method first finds the seat add-on button, then performs a click action on it.
     */
    public void clickAddOnSeat(){
        openSeatsAddon();
    }

    /**
     * Clicks on the choose seat button.
     * This method first finds the choose seat button, then performs a click action on it.
     */
    public void clickChooseSeat(){
        Locator selectSeatBtn = selectButton(1);
        WebElement selectSeat = driverTools.waitForElementToAppear(selectSeatBtn,20).orElseThrow();
        driverTools.click(selectSeat);
    }

    /**
     * Checks if the Quiet Zone banner is displayed.
     * @return false as the implementation is pending.
     */
    @Override
    public boolean isQuiteZoneBannerDisplayed(){
        //fixme implementation pending
        return false;
    }

    /**
     * Adds a seat in the Quiet Zone.
     * @param priceDetail the price details of the flight.
     * @param currency the currency of the flight price.
     * @return null as the implementation is pending.
     */
    @Override
    public String addQuietZoneSeat(PriceDetail priceDetail,String currency){
        //fixme implementation pending
        return null;
    }

    public FlightSeatDetails addSeatRoundTrip(PriceDetail priceDetail, String currency) {
        FlightSeatDetails flightSeatDetails = new FlightSeatDetails();
        openSeatsAddon();
        driverTools.waitForElementToAppear(ANCILLARIES_SELECT_DEPART);
        checkForAncillariesAvailability();

        logger.log("select seat for depart");
        List<WebElement>  selectBtnsDepart = driverTools.elements(ANCILLARIES_SELECT_DEPART).orElseThrow();
        double seatPriceDepart = 0;
        for(WebElement selectBtn : selectBtnsDepart){
            driverTools.click(selectBtn);
            String tempSeatPrice = selectSeatForPassengers(priceDetail.adult + priceDetail.child);
            seatPriceDepart += convertPrice(tempSeatPrice);
            String currencySeat = tempSeatPrice.replaceAll("[^a-zA-Z]", "");
            logger.log("Seat currency Depart: "+ currencySeat);
            Assertion.assertThat("Seat currency is correct", currency.equalsIgnoreCase(currencySeat) , is(true));
        }
        flightSeatDetails.seatPriceDepart = currency + " " + (int) seatPriceDepart;
        flightSeatDetails.totalPaxDepart = selectBtnsDepart.size() * (priceDetail.adult + priceDetail.child);
        logger.log("total seat price Depart : " + flightSeatDetails.seatPriceDepart);
        logger.log("total X pax Depart : " + flightSeatDetails.totalPaxDepart);

        logger.log("select seat for return");
        List<WebElement>  selectBtnsReturn = driverTools.elements(ANCILLARIES_SELECT_RETURN).orElseThrow();
        double seatPriceReturn = 0;
        for(WebElement selectBtn : selectBtnsReturn){
            driverTools.click(selectBtn);
            String tempSeatPrice = selectSeatForPassengers(priceDetail.adult + priceDetail.child);
            seatPriceReturn += convertPrice(tempSeatPrice);
            String currencySeat = tempSeatPrice.replaceAll("[^a-zA-Z]", "");
            logger.log("Seat currency Return: "+ currencySeat);
            Assertion.assertThat("Seat currency is correct", currency.equalsIgnoreCase(currencySeat) , is(true));
        }
        flightSeatDetails.seatPriceReturn = currency + " " + (int) seatPriceReturn;
        flightSeatDetails.totalPaxReturn = selectBtnsReturn.size() * (priceDetail.adult + priceDetail.child);
        logger.log("total seat price Return : " + flightSeatDetails.seatPriceReturn);
        logger.log("total X pax Return : " + flightSeatDetails.totalPaxReturn);

        WebElement saveAddonButton = driverTools.waitForElementToAppear(ADDON_SAVE_BUTTON).orElseThrow();
        driverTools.click(saveAddonButton);
        return flightSeatDetails;
    }

    public FlightSeatDetails addSeatOneWay(PriceDetail priceDetail, String currency) {
        FlightSeatDetails flightSeatDetails = new FlightSeatDetails();
        openSeatsAddon();
        driverTools.waitForElementToAppear(ANCILLARIES_SELECT_DEPART);
        checkForAncillariesAvailability();

        logger.log("select seat for depart");
        List<WebElement>  selectBtnsDepart = driverTools.elements(ANCILLARIES_SELECT_DEPART).orElseThrow();
        double seatPriceDepart = 0;
        for(WebElement selectBtn : selectBtnsDepart){
            driverTools.click(selectBtn);
            String tempSeatPrice = selectSeatForPassengers(priceDetail.adult + priceDetail.child);
            seatPriceDepart += convertPrice(tempSeatPrice);
            String currencySeat = tempSeatPrice.replaceAll("[^a-zA-Z]", "");
            logger.log("Seat currency Depart: "+ currencySeat);
            Assertion.assertThat("Seat currency is correct", currency.equalsIgnoreCase(currencySeat) , is(true));
        }
        flightSeatDetails.seatPriceDepart = currency + " " + (int) seatPriceDepart;
        flightSeatDetails.totalPaxDepart = selectBtnsDepart.size() * (priceDetail.adult + priceDetail.child);
        logger.log("total seat price Depart : " + flightSeatDetails.seatPriceDepart);
        logger.log("total X pax Depart : " + flightSeatDetails.totalPaxDepart);

        WebElement saveAddonButton = driverTools.waitForElementToAppear(ADDON_SAVE_BUTTON).orElseThrow();
        driverTools.click(saveAddonButton);
        return flightSeatDetails;
    }

    @Override
    public boolean isWhatYouGetCorrect(String label){
        driverTools.swipeToElement(getWhatYouGetLocator(label), Swipe.UP, 10);
        if (!driverTools.isElementDisplayed(getWhatYouGetLocator(label)))driverTools.swipeToElement(getWhatYouGetLocator(label), Swipe.DOWN, 5);
        return driverTools.waitForElementToPresence(getWhatYouGetLocator(label), 20).isPresent();
    }

    @Override
    public boolean isWhatYouGetCorrectVI(){
        return driverTools.waitForElementToPresence(WHAT_YOU_GET, 20).isPresent();
    }

    private Locator getWhatYouGetLocator(String label){
        switch (label.toLowerCase()) {
            case "platinum", "diamond" -> {
                return new Locator("WHAT YOU GET :" + label.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'what_will_you_get')]//*[contains(@text,'Benefit') and contains(@text,'Elite') and contains(@text,'" + label + "')]"));
            }
            case "harga gledek","100% refund","free reschedule" -> {
                return new Locator("WHAT YOU GET :" + label.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'rv_flight_booking_what_will_you_get')]//*[contains(@resource-id,'tv_title') and contains(@text,'" + label + "')]"));
            }
            case "connecting flight protection", "self transfer protection", "proteksi transfer mandiri" -> {
                return new Locator("WHAT YOU GET :" + label.toUpperCase(), AppiumBy.xpath("(//*[contains(@resource-id,'rv_flight_booking_what_will_you_get')]//*[contains(@resource-id,'tv_title') or contains(@text,'" + label + "') or contains(@text,'self transfer protection') or contains(@text,'proteksi transfer mandiri')])[1]"));
            }
            default -> throw new NotImplementedException("Unimplemented passenger type!");
        }
    }

    @Override
    public boolean isDiscountPriceCorrect(double discount, String discType){
        driverTools.swipeToElement(getDiscountPrice(discType), Swipe.UP, 5).orElseThrow();
        String discountPrice = driverTools.getText(getDiscountPrice(discType)).orElseThrow();
        double price = convertPrice(discountPrice);
        logger.log("Discount price booking form: "+price);
        logger.log("Discount price search result: "+discount);
        driverTools.click(CLOSE_PRICE_DETAILS);
        openPriceDetail();
        return price == discount;
    }

    private Locator getDiscountPrice(String loyaltyType){
        return new Locator("discount Locator",AppiumBy.xpath("//*[contains(@text,'Elite') and contains(@text,'"+loyaltyType+"') and (contains(@text,'Discount') or contains(@text,'Diskon'))]/following-sibling::*"));
    }
    private Locator getDiscountPriceReturn(String loyaltyType){
        return new Locator("discount Locator",AppiumBy.xpath("(//*[contains(@text,'Elite') and contains(@text,'"+loyaltyType+"') and (contains(@text,'Discount') or contains(@text,'Diskon'))]/following-sibling::*)[last()]"));
    }

    @Override
    public boolean isDiscountTagDisplayed(){
       return driverTools.isElementDisplayed(DISCOUNT_TAG);
    }

    @Override
    public boolean isDiscountPriceRTCorrect(double discount,double discountRT, String discType){
        String discountPrice = driverTools.getText(getDiscountPrice(discType)).orElseThrow();
        double price = convertPrice(discountPrice);
        double priceFromSRP = discount + discountRT;
        logger.log("Discount price booking form depart: "+price);
        logger.log("Discount price search result depart: "+discount);
        logger.log("Discount price search result return: "+discountRT);
        logger.log("Discount price search result: " + priceFromSRP);

        return price == priceFromSRP || price == priceFromSRP - 1 || price == priceFromSRP + 1;
    }

    private Locator getCampaignLabelLocatorPriceDetail(String label){
        switch (label.toLowerCase()){
            case "harga gledek":{
                return new Locator(label.toUpperCase() + " LABEL", AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_price_breakdown_detail_name') and (contains(@text,'Harga Gledek') or contains(@text,'Discount') or contains(@text,'Diskon'))]"));
            }
            default:
                throw new NotImplementedException("Unimplemented campaign label type!");
        }
    }

    private boolean verifyCampaignLabelAndPriceOnPriceDetail(String label,PriceDetail priceDetail){
        driverTools.swipeToElement(getCampaignLabelLocatorPriceDetail(label), Swipe.UP, 1);
        boolean verifyLabel = driverTools.waitForElementToPresence(getCampaignLabelLocatorPriceDetail(label)).isPresent();

        // For "Harga Gledek" the SRP shows the TOTAL savings vs the regular fare (e.g. "Save IDR
        // 278,076"). On the booking-form price-detail popup that same total is split across two
        // rows: the Discount/Harga Gledek line (e.g. "-IDR 275,707") and the Tix Points Cashback
        // line (e.g. "Get 2,369 points", equivalent to IDR 2,369). So when verifying the special-
        // campaign price we must sum discount + cashback from the breakdown before comparing to
        // the SRP value. A ±1 IDR tolerance accounts for any rounding diff between the two views.
        boolean verifyPrice;
        if ("harga gledek".equalsIgnoreCase(label) && priceDetail.specialCampaignPrice != null) {
            logger.log(label + " - raw SRP specialCampaignPrice input: '" + priceDetail.specialCampaignPrice + "'");
            int srpSavings = CoreFunctions.getIntFromString(priceDetail.specialCampaignPrice)
                    .orElseThrow(() -> new IllegalStateException(
                            "Could not parse SRP specialCampaignPrice as integer (got '"
                                    + priceDetail.specialCampaignPrice
                                    + "'). The SPECIAL_CAMPAIGN_SAVINGS_PRICE locator likely matched a non-monetary element"
                                    + " on the SRP (e.g. a 'Save more with the best promo' banner). Tighten the locator."));
            int discountOnly = CoreFunctions.getIntFromString(getPrice(label, 1)).orElseThrow();
            int cashbackValue = 0;
            if (driverTools.isElementDisplayed(CASHBACK)) {
                String cashbackText = driverTools.getText(CASHBACK_PRICE_VALUE).orElse("");
                cashbackValue = CoreFunctions.getIntFromString(cashbackText).orElse(0);
                logger.log(label + " - cashback row text: '" + cashbackText + "' => value: " + cashbackValue);
            } else {
                logger.log(label + " - no Cashback row rendered; treating cashback as 0");
            }
            int bookingFormCombined = discountOnly + cashbackValue;
            logger.log(label + " - SRP savings: " + srpSavings
                    + " | booking-form discount: " + discountOnly
                    + " | booking-form cashback: " + cashbackValue
                    + " | combined: " + bookingFormCombined);
            verifyPrice = Math.abs(bookingFormCombined - srpSavings) <= 1;
        } else {
            verifyPrice = isBreakdownCorrect(label, 1, priceDetail.specialCampaignPrice);
        }

        logger.log("label verified: " + verifyLabel);
        logger.log("price verified: " + verifyPrice);
        return verifyLabel && verifyPrice ;
    }

    @Override
    public boolean isAdjustmentPresentInBooking (){
        driverTools.swipeToElement(CANCEL_INSURANCE,Swipe.UP,3).ifPresent(driverTools::click);
        driverTools.swipeToElement(BENEFIT_SECTION, Swipe.UP,7);
        boolean campaignOrLoyaltyShow = driverTools.isElementDisplayed(CAMPAIGN_OR_LOYALTY);
        return campaignOrLoyaltyShow;
    }

    private boolean verify100pRefundOnPriceDetail(){
        driverTools.waitForElementToAppear(PRICE_DETAIL_100P_REFUND);
        List<WebElement> refund100P = driverTools.elements(PRICE_DETAIL_100P_REFUND).orElseThrow();
        boolean is100PRefundPresent = refund100P.size() >= 1;
        return is100PRefundPresent;
    }

    @Override
    public boolean offerBreakdown(String label,PriceDetail priceDetail){
        return switch (label.toLowerCase()) {
            case "harga gledek" -> verifyCampaignLabelAndPriceOnPriceDetail(label, priceDetail);
            case "100% refund" -> verify100pRefundOnPriceDetail();
            default -> throw new NotImplementedException("Unimplemented offer label type!");
        };
    }

    @Override
    public boolean isTotalPriceWithoutDiscount(int adultPassenger){
        String price = driverTools.getText(TOTAL_PRICE).orElseThrow();
        double totalPrice = convertPrice(price);
        logger.log("Total Price: " + totalPrice);
        String priceAdult = driverTools.getText(getPriceBreakdownLocator("adult",adultPassenger)).orElseThrow();
        double totalPriceAdult = convertPrice(priceAdult);
        logger.log("Adult Price: " + totalPriceAdult);
        driverTools.back();
        return totalPrice == totalPriceAdult;
    }

    @Override
    public void isOnBookingPage(){
        for (int i = 0; i < 4; i++) {
            dismissLoadErrorIfShown();
            if (driverTools.waitForElementToAppear(TRIP_DETAIl, 8).isPresent()
                    || driverTools.waitForElementToAppear(CONTACT_DETAIL, 3).isPresent()
                    || driverTools.waitForElementToAppear(FILL_CONTACT_DETAILS, 2).isPresent()) {
                return;
            }
        }
        driverTools.waitForElementToAppear(TRIP_DETAIl, 5).orElseThrow();
    }

    private void dismissLoadErrorIfShown() {
        if (!driverTools.isElementDisplayed(LOAD_ERROR_RETRY)) {
            return;
        }
        for (int i = 0; i < 3 && driverTools.waitForElementToAppear(LOAD_ERROR_RETRY, 2).isPresent(); i++) {
            logger.log("Load error overlay shown — tapping Reload / Coba lagi");
            driverTools.click(LOAD_ERROR_RETRY);
        }
    }

    private Locator getContactDetail(String text,String type){
        return new Locator("Locator " + type.toUpperCase(),AppiumBy.xpath("//*[contains(@text,'"+text+"')]"));
    }

    @Override
    public boolean verifyChangeEmail(String email){
        driverTools.click(CONTACT_DETAIL);
        boolean isEmailCorrect = driverTools.isElementDisplayed(getContactDetail(email,"email"));
        logger.log("is email correct after change: " + isEmailCorrect);
        return isEmailCorrect;
    }

    @Override
    public boolean isYellowBannerBookingPage2Displayed(){
        WebElement yellowBannerElement = driverTools.swipeToElement(YELLOW_BANNER_BOOKING_PAGE_2,Swipe.UP,15).orElseThrow();
        boolean isYellowBannerDisplayed = driverTools.isElementDisplayed(yellowBannerElement);
        logger.log("yellow banner 2 displayed : " + isYellowBannerDisplayed);

        boolean isYellowBannerIconDisplayed = driverTools.isElementDisplayed(YELLOW_BANNER_BOOKING_PAGE_2_ICON);
        logger.log("yellow banner 2 displayed : " + isYellowBannerIconDisplayed);
        return isYellowBannerDisplayed && isYellowBannerIconDisplayed;
    }

    @Override
    public void clickYellowBannerBookingPage2(){
        driverTools.click(YELLOW_BANNER_BOOKING_PAGE_2);
    }

    @Override
    public void openLoginPage() {
        driverTools.waitForElementToPresence(LOGIN_BANNER, 5).ifPresent(driverTools::click);
    }

    @Override
    public boolean isLoggedIn() {
        return driverTools.waitForElementToDisappear(EMAIL,5) && driverTools.waitForElementToDisappear(LOGIN_BANNER, 5);
    }

    private String extractDateFromTripInfo(String tripInfo) {
        return tripInfo.split(" • ")[0].trim();
    }

    private String extractDateTimeFromTripInfo(String tripInfo) {
        String[] parts = tripInfo.split(" • ");
        if (parts.length >= 2) {
            return parts[0].trim() + " • " + parts[1].trim();
        }
        return tripInfo.trim();
    }

    private boolean verifyDate(String date){
        // Fri, 31 Jul
        String dateOnly = date.contains(" • ") ? extractDateFromTripInfo(date) : date;
        String regex = "^\\p{Upper}{1}\\p{Lower}{2}, \\d{1,2} \\p{Upper}{1}\\p{Lower}{2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dateOnly);
        return matcher.matches();
    }

    @Override
    public void verifyDateFormat(String tripType){
        String dateDepart = null;
        try {
            WebElement dateDepartElement = driverTools.waitForElementToAppear(TRIP_DETAIL_DATE_CARD).orElseThrow();
            dateDepart = extractDateFromTripInfo(driverTools.getText(dateDepartElement));
        } catch (Exception e) {
            logger.log("Retrying to fetch departure date due to exception: " + e.getMessage());
            WebElement dateDepartElement = driverTools.waitForElementToAppear(TRIP_DETAIL_DATE_CARD).orElseThrow();
            dateDepart = extractDateFromTripInfo(driverTools.getText(dateDepartElement));
        }

        logger.log("date depart: " + dateDepart);
        Assertion.assertThat("is date format correct depart", verifyDate(dateDepart), is(true));

        if (tripType.toLowerCase().contains("roundtrip")) {
            WebElement dateReturnElement = driverTools.waitForElementToAppear(TRIP_DETAIL_RETURN_INFO).orElseThrow();
            String dateReturn = extractDateFromTripInfo(driverTools.getText(dateReturnElement));
            logger.log("date return: " + dateReturn);
            Assertion.assertThat("is date format correct return", verifyDate(dateReturn), is(true));
        }
    }

    @Override
    public List<String> getFlightDateTime(String tripType) {
        List<String> flightDatetime = new ArrayList<String>();
        driverTools.waitForElementToAppear(TRIP_DETAIL_DATE_CARD).orElseThrow();
        String departDateTime = extractDateTimeFromTripInfo(driverTools.getText(TRIP_DETAIL_DATE_CARD).orElseThrow());
        flightDatetime.add(departDateTime);
        driverTools.click(TRIP_DETAIl);
        String departureFlightArrivalTime = driverTools.getText(ARRIVAL_DATE).orElseThrow();
        flightDatetime.add(departureFlightArrivalTime);
        logger.log("(Flight booking page) date depart: " + flightDatetime.get(0) + " arrival: " + flightDatetime.get(1));
        driverTools.click(TRIP_DETAIl_BACK);
        if (tripType.toLowerCase().contains("roundtrip")){
            WebElement returnInfoElement = driverTools.waitForElementToAppear(TRIP_DETAIL_RETURN_INFO).orElseThrow();
            String returnDateTime = extractDateTimeFromTripInfo(driverTools.getText(returnInfoElement));
            flightDatetime.add(returnDateTime);
            driverTools.clickElementByCoordinate(returnInfoElement);
            String returnFlightArrivalTime = driverTools.getText(ARRIVAL_DATE).orElseThrow();
            flightDatetime.add(returnFlightArrivalTime);
            driverTools.click(TRIP_DETAIl_BACK);
            logger.log("(Flight booking page) date return: " + flightDatetime.get(2) + " arrival: " + flightDatetime.get(3));
        }
        return flightDatetime;
    }

    @Override
    public boolean verifyPromoInfinitySection(){
        return driverTools.isElementDisplayed(PROMO_BANNER);
    }

    @Override
    public boolean verifyPromoInfinitySection2ndPage(){
        return driverTools.isElementDisplayed(PROMO_BANNER_CHEVRON_2ND_PAGE);
    }

    @Override
    public void swipeToPromoInfinityPart(){
        driverTools.swipeToElement(PROMO_BANNER_CHEVRON_2ND_PAGE,Swipe.UP,12).orElseThrow();
    }

    @Override
    public void openPromoInfinity(){
        driverTools.click(PROMO_BANNER_CHEVRON_2ND_PAGE);
    }

    @Override
    public void removePromo(){
        driverTools.click(REMOVE_PROMO);
    }

    @Override
    public PromoData applyPromoInfinityRandomAndGetData(int index){
        PromoData promoData = new PromoData();
        promoData.promoCode = driverTools.getText(getPromoCodeByIndex(index)).orElseThrow().split(" ")[1];
        String price = driverTools.getText(getPromoPriceByIndex(index)).orElseThrow();
        promoData.promoDiscountPrice = CoreFunctions.convertPrice(price);

        if(price.contains("Save") || price.contains("Hemat")){
            promoData.isSaveAmount = true;
        }else {
            promoData.isSaveAmount = false;
        }
        driverTools.click(getPromoCodeByIndex(index));
        return promoData;
    }

    private Locator getPromoCodeByIndex(int index){
        return new Locator("PROMO_CODE",AppiumBy.xpath("(//*[contains(@resource-id,'tv_promo_code')])["+index+"]"));
    }

    private Locator getPromoPriceByIndex(int index){
        return new Locator("PROMO_PRICE",AppiumBy.xpath("(//*[contains(@resource-id,'label_primary')]//android.widget.TextView)["+index+"]"));
    }

    @Override
    public boolean isPromoCodeAndAmountShown(PromoData promoData){
        boolean isPromoCodeShown = false;
        driverTools.swipeToElement(CONTINUE_PAYMENT_BUTTON,Swipe.UP,3);
        int i=0;
        String price ="";
        while(i<5){
            WebElement promoCode = driverTools.waitForElementToAppear(getPromoCodeAndAmountBannerLocator(promoData.promoCode)).orElseThrow();
            logger.log("isPromoCodeShown: " + isPromoCodeShown);
            price = driverTools.getText(promoCode);
            if(price.contains(promoData.promoCode)){
                isPromoCodeShown = true;
                break;
            }
            i++;
        }

        Assertion.assertThat("is promo code shown ",isPromoCodeShown,is(true));

        String amount = price.replaceAll(".*\\(([^)]+)\\).*", "$1").trim();
        amount = amount.replaceAll("IDR|\\+|-|\\s|\\.", "");
        double amountConvert = CoreFunctions.convertPrice(amount);
        logger.log("amount excepted: " + promoData.promoDiscountPrice + " amount actual: " + amountConvert);

        Assertion.assertThat("is amount same as promo",promoData.promoDiscountPrice,is(amountConvert));
        return true;
    }

    private Locator getPromoCodeAndAmountBannerLocator(String promoCode){
        return new Locator("PROMO_CODE_AMOUNT",AppiumBy.xpath("//*[contains(@resource-id,'tds_label')]//android.widget.TextView[contains(@text,'"+promoCode+"')]"));
    }

    @Override
    public boolean isPromoCodeAndAmountDisplayedInPriceDetail(PromoData promoData){
        WebElement promoAmount = driverTools.waitForElementToAppear(PromoCodeAndRelatedAmountlocator(promoData.promoCode)).orElseThrow();
        String price = driverTools.getText(promoAmount).replace("-","");
        double priceActual = CoreFunctions.convertPrice(price);
        logger.log("price excepted: " + promoData.promoDiscountPrice + " price Actual: " + priceActual);

        Assertion.assertThat("is price same in price detail",promoData.promoDiscountPrice,is(priceActual));
        return true;
    }

    private Locator PromoCodeAndRelatedAmountlocator(String promoCode){
        return new Locator("PROMO_CODE_AND_AMOUNT",AppiumBy.xpath("//*[contains(@resource-id,'tv_discount_item_label') and contains(@text,'"+promoCode+"')]//following-sibling::*[contains(@resource-id,'tv_discount_item_price')]"));
    }

    @Override
    public boolean isConnectingFlightProtectionNudgeDisplayed(){
        boolean isConnectingFlightProtectionNudgeDisplayed = driverTools.isElementDisplayed(CONNECTING_FLIGHT_PROTECTION_NUDGE) || driverTools.isElementDisplayed(CONNECTING_NUDGE);
        logger.log("isConnectingFlightProtectionNudgeDisplayed: " + isConnectingFlightProtectionNudgeDisplayed);
        List<WebElement> airlineLogoElement = driverTools.elements(AIRLINE_ICON).orElseThrow();
        boolean isAirlineIconSize2 = airlineLogoElement.size() == 2;
        logger.log("isAirlineIconSize2: " + isAirlineIconSize2);
        return isConnectingFlightProtectionNudgeDisplayed && isAirlineIconSize2;
    }

    @Override
    public boolean verifyMoreBenefit(){
        WebElement element = driverTools.waitForElementToAppear(MORE_BENEFIT).orElseThrow();
        for (int where=1;where<=5;where++){
            driverTools.clickSpecificPart(element,where);
            driverTools.waitForElementToAppear(CLOSE_BUTTON, 20);
            if(driverTools.isElementDisplayed(CLOSE_BUTTON))
                break;
        }

        driverTools.waitForElementToPresence(CLOSE_BUTTON_INSURANCE);
        Assertion.assertThat("is insurance detail title displayed",driverTools.isElementDisplayed(INSURANCE_DETAIL_TITLE),is(true));
        Assertion.assertThat("is coverage header displayed",driverTools.isElementDisplayed(INSURANCE_DETAIL_COVERAGE_HEADER),is(true));
        Assertion.assertThat("is compensation header displayed",driverTools.isElementDisplayed(INSURANCE_DETAIL_COMPENSATION_HEADER),is(true));
        Assertion.assertThat("is coverage element should displayed",driverTools.isElementDisplayed(INSURANCE_DETAIL_COVERAGE),is(true));
        Assertion.assertThat("is compensation element should displayed",driverTools.isElementDisplayed(INSURANCE_DETAIL_COMPENSATION),is(true));
        driverTools.click(CLOSE_BUTTON_INSURANCE);
        return true;
    }

    @Override
    public boolean verifyWarningMassage(){
        driverTools.swipeToElement(YELLOW_BANNER_CONNECTING_FLIGHT_PROTECTION,Swipe.UP,4);
        return driverTools.isElementDisplayed(YELLOW_BANNER_CONNECTING_FLIGHT_PROTECTION);
    }

    @Override
    public boolean connectingFlightProtectionDisplayedOnPriceDetail(int passengerCount,String currency){
        WebElement connectingFlightProtectionLabelOnPriceDetail = driverTools.waitForElementToAppear(getPriceBreakdownLocator("Connecting Flight Protection",passengerCount)).orElseThrow();
        boolean isElementDisplayed = driverTools.isElementDisplayed(connectingFlightProtectionLabelOnPriceDetail);
        logger.log("connectingFlightProtectionLabelOnPriceDetailDisplayed: " + isElementDisplayed);

        boolean isCurrencyExpected = driverTools.getText(connectingFlightProtectionLabelOnPriceDetail).contains(currency);
        logger.log("isCurrencyExpected: " + isCurrencyExpected);
        return isElementDisplayed && isCurrencyExpected;
    }

    @Override
    public boolean isGreenBannerVisible(){
        driverTools.swipeUp(15);
        return driverTools.isElementDisplayed(GREEN_BANNER_UNDER_CONTINUE_BTN);
    }

    @Override
    public boolean isCashbackVisible(){
        boolean result = driverTools.swipeToElement(CASHBACK_TEXT,Swipe.UP,10).isPresent();
        driverTools.back();
        return result;
    }

    @Override
    public boolean verifyDepartureInfoBookingPage(){
        driverTools.click(DEPARTURE_TAB);
        logger.log("Clicked on details tab");
        driverTools.click(CLICK_DETAILS);
        Assertion.assertThat("Departure flight details are present", driverTools.isElementDisplayed(DEPARTURE_FLIGHT), is(true));
        boolean res1 = driverTools.isElementDisplayed(REFUND_PERCENTAGE) || driverTools.isElementDisplayed(REFUND);
        Assertion.assertThat("Refund info present for departure flight", true, either(is(driverTools.isElementDisplayed(REFUND_PERCENTAGE))).or(is(driverTools.isElementDisplayed(REFUND))));
        driverTools.click(RESCHEDULE_CHIP);

        Assertion.assertThat("Departure flight details are present", driverTools.isElementDisplayed(DEPARTURE_FLIGHT), is(true));
        logger.log("Departure reschedule percentage information is displayed");
        boolean res2 = driverTools.isElementDisplayed(RESCHEDULE_PERCENTAGE) || driverTools.isElementDisplayed(RESCHEDULE);
        Assertion.assertThat("Reschedule info present for departure flight", true, either(is(driverTools.isElementDisplayed(RESCHEDULE_PERCENTAGE))).or(is(driverTools.isElementDisplayed(RESCHEDULE))));
        driverTools.click(CLOSE_TAB);
        driverTools.click(CLOSE_TAB_AGAIN);
        return res1 && res2;
    }

    @Override
    public boolean verifyReturnInfoBookingPage(){
        driverTools.swipeLeftUntilFound(WHOLE_TAB, RETURN_TAB, 5);
        driverTools.click(RETURN_TAB);
        logger.log("Clicked on details tab");
        driverTools.click(CLICK_DETAILS);
        Assertion.assertThat("Return flight details are present", driverTools.isElementDisplayed(RETURN_FLIGHT), is(true));
        boolean res1 = driverTools.isElementDisplayed(REFUND_PERCENTAGE) || driverTools.isElementDisplayed(REFUND);
        Assertion.assertThat("Refund info present for return flight", true, either(is(driverTools.isElementDisplayed(REFUND_PERCENTAGE))).or(is(driverTools.isElementDisplayed(REFUND))));

        driverTools.click(RESCHEDULE_CHIP);
        Assertion.assertThat("Return flight details are present", driverTools.isElementDisplayed(RETURN_FLIGHT), is(true));
        boolean res2 = driverTools.isElementDisplayed(RESCHEDULE_PERCENTAGE) || driverTools.isElementDisplayed(RESCHEDULE);
        Assertion.assertThat("Reschedule info present for return flight", true, either(is(driverTools.isElementDisplayed(RESCHEDULE_PERCENTAGE))).or(is(driverTools.isElementDisplayed(RESCHEDULE))));
        driverTools.click(CLOSE_TAB);
        driverTools.click(CLOSE_TAB_AGAIN);
        return res1 && res2;
    }

    @Override
    public void otherOrderStillWaitingPopUp(){
        driverTools.waitForElementToPresence(CONTINUE_PAYMENT_BUTTON, 5).ifPresent(driverTools::click);
    }

    /**
     * Waits for the native content loading dialog (e.g. shown after continue to payment / interim page)
     * to disappear. Must be called from the native context.
     */
    @Override 
    public void waitForLoadingDialogToDisappear() {
        if (driverTools.waitForElementToAppear(CONTENT_LOADING_DIALOG, 5).isEmpty()) {
            logger.log("Content loading dialog not shown");
            return;
        }
        boolean isDisappeared = driverTools.waitForElementToDisappear(CONTENT_LOADING_DIALOG);
        logger.log("Content loading dialog disappeared: " + isDisappeared);
    }
}
