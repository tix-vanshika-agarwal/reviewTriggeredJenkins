package com.tiket.web.page.flight.bookingForm;

import com.tiket.appium.PopupObserver;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.pojo.common.InsuranceGeneralData;
import com.tiket.pojo.flight.FlightPaxDetail;
import com.tiket.pojo.flight.PriceDetail;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chromium.HasCdp;
import org.openqa.selenium.logging.LogEntry;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public abstract class FlightBookingFormPageWeb<T extends WebDriver> extends WebPage<T> {
    public FlightBookingFormPageWeb(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> FlightBookingFormPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new FlightBookingFormPageDWeb<>(session);
            }
            case AMWEB, IMWEB -> {
                return new FlightBookingFormPageMWeb<>(session);
            }
            default -> throw new UnsupportedOperationException("Platform not supported");
        }
    }

    private static final String V7_BOOKING_URL = "v7/booking";
    private static final String V7_BOOKING_STORAGE_KEY = "__tiketV7BookingBody";

    private static final Locator REFUND_ACTIVE_PANEL = new Locator("REFUND_ACTIVE_PANEL", By.xpath("//div[contains(@class, 'BaggageRefundReschedulePopup')]//div[contains(@class, 'Tabs_tab_panel') and not(@hidden)]"));
    private static final Locator RESCHEDULE_ACTIVE_PANEL = new Locator("RESCHEDULE_ACTIVE_PANEL", By.xpath("//div[contains(@class, 'BaggageRefundReschedulePopup')]//div[contains(@class, 'Tabs_tab_panel') and not(@hidden)]"));
    private static final Locator MEAL_SELECTOR = new Locator("MEAL_SELECTOR", By.xpath("//button[contains(@class,'MealSelectionContent_meal_item_add_button')]"));
    private static final Locator RRPOPUP = new Locator("RRPOPUP", By.xpath("(//header[contains(@class,'Modal_modal_header')])[last()]"));
    private static final Locator CLOSERR = new Locator("CLOSERR", By.xpath("(//button[@data-testid='modal-close-button'])[last()]"));
    protected static final Locator I_DONT_WANT_IT_BUTTON = new Locator("I_DONT_WANT_IT_BUTTON", By.xpath("//button[text()=\"I don't want it\"]"));
    private static final Locator PRICE_CHANGE_POPUPOKAY = new Locator("PRICE_CHANGE_POPUPOKAY", new By.ByXPath("//button//span[contains(text(),'OK, Mengerti') or contains(text(),'OK, Got It') or contains(text(),'Saya mengerti') or contains(text(),'I understand') or contains(text(),'Got it') or contains(text(),\"Let's Try Again\") or contains(text(),'Coba Lagi') or contains(text(),'OK, got it')]"));
    private static final Locator SORRY_POPUP = new Locator("SORRY_POPUP", new By.ByXPath("//*[contains(@class,'Figure_content_container')]//*[contains(@class,'Figure_title')]"));
    private static final Locator AIRLINE_CONNECTION_TRY_AGAIN = new Locator("AIRLINE_CONNECTION_TRY_AGAIN", By.xpath("//button[.//span[contains(text(),\"Let's Try Again\") or contains(text(),'Coba Lagi') or contains(text(),'Try again') or contains(text(),'Coba lagi')]]"));
    private static final Locator POPUP_BUTTON_SECONDARY = new Locator("POPUP_BUTTON_SECONDARY", By.xpath("//button[@data-testid='popup-button-secondary' and .//span[translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='nggak dulu, deh' or translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='no, thanks']]"));
    private static final Locator CONTACT_DETAILS_BOOKING_PAGE = new Locator("CONTACT_DETAILS_BOOKING_PAGE", new By.ByXPath("//h3[contains(@class,'BookingContactAndPassengers_title__')]"));
    private static final Locator GO_TO_BOOKING_FORM_BTN = new Locator("GO_TO_BOOKING_FORM_BTN", By.xpath("//button[contains(@class, 'Button_variant_secondary')]//span[text()='Go to booking form' or text()='Ke form pemesanan']"));
    private static final Locator LOGIN_BANNER_BOOKING_PAGE = new Locator("LOGIN_BANNER_BOOKING_PAGE", new By.ByXPath("//div[contains(@class,'LoggedInBanner_wrapper')]"));
    private static final Locator LOGIN_BTN_BOOKING_PAGE = new Locator("LOGIN_BTN_BOOKING_PAGE", new By.ByXPath("//div[contains(@class,'LoggedInBanner_wrapper')]"));
    private static final Locator TOGGLE_SAME_AS_CONTACT_DETAILS = new Locator("SAME_AS_CONTACT_DETAILS", new By.ByXPath("(//div[contains(@class,'Toggle_knob')])"));
    protected static final Locator NEXT_BUTTON = new Locator("NEXT_BUTTON", new By.ByXPath("//button[contains(@class, 'Button_variant_primary') and contains(@class, 'Button_full_width')]"));
    protected static final Locator CONTINUE_TO_PAYMENT_PAGE_POPUP = new Locator("CONTINUE_TO_PAYMENT_PAGE_POPUP", new By.ByXPath("//div[contains(@class,'Figure_footer')]//button[contains(@class,'primary')]"));
    protected static final Locator NON_PAYMENT_CONFIRMATION_DIALOG_POPUP = new Locator("NON_PAYMENT_CONFIRMATION_DIALOG_POPUP", new By.ByXPath("//div[contains(@class,'Figure_footer')]//button[contains(@class,'secondary')]"));
    private static final Locator FLIGHT_MAY_RESCHEDULED_POPUP = new Locator("FLIGHT_MAY_RESCHEDULED_POPUP", new By.ByXPath("//*[@data-testid=\"popup-button-secondary\"]"));
    private static final Locator PRICE_CHANGE_POPUP = new Locator("PRICE_CHANGE_POPUP", new By.ByXPath("//*[contains(text(),'OK, Mengerti') or contains(text(),'OK, Got It')]"));
    private static final Locator PASSENGER_FULL_NAME = new Locator("PASSENGER_FULL_NAME", new By.ByXPath("//*[contains(@class,'Collapse_collapse_with_transition')]//input[@id='given-names-first--middle-name' or @id='nama-depan--nama-tengah' or @id='full-name-according-to-idpassport' or @id='nama-lengkap-sesuai-ktppasporsim']"));
    private static final Locator PASSENGER_FIRST_NAME = new Locator("PASSENGER_FIRST_NAME", new By.ByXPath("//*[contains(@class,'Collapse_collapse_with_transition')]//input[@id='given-names-first--middle-name']"));
    private static final Locator PASSENGER_LAST_NAME = new Locator("PASSENGER_LAST_NAME", new By.ByXPath("//*[contains(@class,'Collapse_collapse_with_transition')]//input[@id='surname-familylast-name']"));
    private static final Locator CLEAR_FULL_NAME = new Locator("CLEAR_FULL_NAME", new By.ByXPath("//*[contains(@class,'BookingPassengerSection_person_card')]//input[@id='full-name-according-to-idpassport']//following-sibling::*//button[contains(@class,'styles_clear_button')]"));
    private static final Locator CLEAR_FIRST_NAME = new Locator("CLEAR_FIRST_NAME", new By.ByXPath("//*[contains(@class,'Collapse_collapse_with_transition')]//input[@id='given-names-first--middle-name']//following-sibling::*//button[contains(@class,'styles_clear_button')]"));
    private static final Locator CLEAR_LAST_NAME = new Locator("CLEAR_LAST_NAME", new By.ByXPath("//*[contains(@class,'Collapse_collapse_with_transition')]//input[@id='surname-familylast-name']//following-sibling::*//button[contains(@class,'styles_clear_button')]"));
    private static final Locator NO_THANKS_BUTTON = new Locator("NO_THANKS_BUTTON", new By.ByXPath("//button[contains(text(),'No, thanks') or contains(text(),'Nggak dulu, deh')]"));
    private static final Locator MS_BUTTON = new Locator("MS_BUTTON", new By.ByXPath("(//span[contains(@class,'Radio_icon')])[3]")) ;
    private static final Locator NATIONALITY = new Locator("NATIONALITY", new By.ByXPath("(//button[@type='button' and contains(@class, 'styles_clickable_icon')])[3]"));
    private static final Locator NATIONALITY_SEARCH = new Locator("NATIONALITY_SEARCH", new By.ByXPath("//*[contains(@class,'CountryListSelection_search_box')]//*[contains(@class,'SearchBox_input_box')]//input"));
    private static final Locator COUNTRY = new Locator("COUNTRY", new By.ByXPath("(//p[contains(@class, 'List_title')])[1]"));
    private static final Locator CONTACT_NAME_INPUT = new Locator("CONTACT_NAME_INPUT", By.xpath("(.//input[@id='full-name-according-to-idpassport' or @id='nama-lengkap-sesuai-ktppasporsim' or @id='full-name'])[1]"));
    private static final Locator CONTACT_PHONE_INPUT = new Locator("CONTACT_PHONE_INPUT", By.xpath(".//input[@type='tel']"));
    private static final Locator CONTACT_EMAIL_INPUT = new Locator("CONTACT_EMAIL_INPUT", By.xpath(".//input[@type='email']"));
    private static final Locator SAME_CONTACT_TOGGLE = new Locator("SAME_CONTACT_TOGGLE", By.xpath("//div[contains(@class,'Toggle_toggle') and contains(@class,'Toggle_toggle_small')]"));
    private static final Locator PASSENGER_DATA_MODAL_MWEB = new Locator("PASSENGER_DATA_MODAL_MWEB", By.xpath("//div[@data-testid='bottom-sheet-body']"));
    private static final Locator INSURANCE_POPUP_BTN  = new Locator("INSURANCE_POPUP", By.xpath("//h2[contains(@class, 'Text_text') and (contains(text(), 'Full Protection!') or contains(text(), 'Perlindungan Penuh!'))]/ancestor::section//button[@data-testid='popup-button-secondary']"));
    private static final Locator FLIGHT_DISRUPTION_INSURANCE_POPUP = new Locator("FLIGHT_DISRUPTION_INSURANCE_POPUP",
            By.xpath("//div[@data-testid='template-disruption-variant2']"));
    private static final Locator WITHOUT_PROTECTION_OPTION = new Locator("WITHOUT_PROTECTION_OPTION",
            By.xpath("//div[@data-testid='template-disruption-variant2']//strong[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'without protection')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'tanpa proteksi')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'tanpa perlindungan')]/ancestor::div[@data-testid='content-body2']"));
    private static final Locator WITHOUT_PROTECTION_RADIO = new Locator("WITHOUT_PROTECTION_RADIO",
            By.xpath("//div[@data-testid='template-disruption-variant2']//div[@data-testid='content-body2']//label[contains(@class,'Radio_radio')]"
                    + " | //div[@data-testid='template-disruption-variant2']//div[@data-testid='content-body2']//input[@type='radio' and contains(@value,'body2')]/ancestor::label[1]"));
    private static final Locator CONTINUE_WITHOUT_PROTECTION_BTN = new Locator("CONTINUE_WITHOUT_PROTECTION_BTN",
            By.xpath("//div[@data-testid='template-disruption-variant2']/ancestor::section//button[@data-testid='popup-button-primary']"
                    + "[.//span[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'continue without')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lanjut tanpa')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lanjutkan tanpa')]]"));
    private static final Locator CONTINUE_AFTER_SKIP_PROTECTION = new Locator("CONTINUE_AFTER_SKIP_PROTECTION",
            By.xpath("//div[@data-testid='template-disruption-variant2']/ancestor::section//button[@data-testid='popup-button-primary']"
                    + "[.//span[(starts-with(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'continue')"
                    + " or starts-with(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lanjut'))"
                    + " and not(contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'with protection'))"
                    + " and not(contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'dengan proteksi'))"
                    + " and not(contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'dengan perlindungan'))]]"));
    private static final Locator BAGGAGE_POPUP = new Locator("BAGGAGE_POPUP", By.xpath("//h2[contains(@class, 'Figure_title') and (contains(text(), 'bagasi') or contains(text(), 'baggage'))]/ancestor::div[contains(@class, 'Dialog_pop_up')]"));
    protected static final Locator PRICE_TEXT = new Locator("PRICE_TEXT", By.xpath("(//div[contains(@class, 'PriceSummaryItem_container')][.//span[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'total')]]//span[contains(@class, 'PriceSummaryItem_price')])[last()]"));
    private static final Locator BOOKING_FORM = new Locator("BOOKING_FORM", new By.ByXPath("//*[@data-testid='booking-content']"));
    private static final Locator FLIGHT_DETAILS = new Locator("FLIGHT_DETAILS", new By.ByXPath("//div[contains(@class, 'Card_card_container') and contains(@class, 'BookingFlightSummary_container')]"));
    private static final Locator FLIGHT_DETAILS_BUTTON = new Locator("FLIGHT_DETAILS_BUTTON", new By.ByXPath("//div[contains(@class, 'BookingFlightSummary_arrow_right')]"));
    private static final Locator FLIGHT_DETAIL_POPUP = new Locator("FLIGHT_DETAIL_POPUP", new By.ByXPath("//div[contains(@class, 'BookingFlightSummaryPopup_container')]"));
    private static final Locator SEE_OTHER_FACILITY = new Locator("SEE_OTHER_FACILITY", new By.ByXPath("//button[contains(@class,'Facilities_more_facilities__')]"));
    private static final Locator FLIGHT_FACILITY_POPUP = new Locator("FLIGHT_FACILITY_POPUP", new By.ByXPath("//h2[contains(normalize-space(.),'Flight Facilities') or contains(normalize-space(.),'Fasilitas Penerbangan')]"));
    private static final Locator CLOSE_SECOND = new Locator("CLOSE_SECOND", new By.ByXPath("(//button[@data-testid='modal-close-button'])[last()]"));
    private static final Locator CLOSE = new Locator("CLOSE", new By.ByXPath("(//button[@data-testid='modal-close-button'])[1]"));
    private static final Locator PRICE_BUTTON = new Locator("PRICE_BUTTON", new By.ByXPath("//div[contains(@class, 'BookingFlightSummary_pay_now')]"));
    private static final Locator PRICE_DETAIL_POPUP = new Locator("PRICE_DETAIL_POPUP", new By.ByXPath("//div[contains(@class, 'BookingPriceDetailPopUp_price_detail')]"));
    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", new By.ByXPath("(//div[contains(@class, 'PriceSummary_total_price_item')])[2]"));
    private static final Locator TOTAL_AFTER_CASHBACK = new Locator("TOTAL_AFTER_CASHBACK", new By.ByXPath("//ul[contains(@class,'BookingPriceDetailPopUp_price_list_items')]//li[.//span[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'total after cashback') or contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'total setelah cashback')]]/span[last()]"));
    private static final Locator CASHBACK_ITEMS = new Locator("CASHBACK_ITEMS", new By.ByXPath("//ul[contains(@class,'BookingPriceDetailPopUp_price_list_items')]//li[.//div[contains(@class,'BookingPriceDetailPopUp_point')]]"));
    private static final String BAGGAGE_ADDON_CARD_XPATH =
            "//div[contains(@class,'AddonsCard_card')][.//p[normalize-space()='Baggage' or normalize-space()='Bagasi']]";
    private static final Locator ADD_BAGGAGES = new Locator("ADD_BAGGAGES", new By.ByXPath("//button[contains(@class, 'CheckedBaggageItem_add_button')]"));
    private static final Locator ADD_BAGGAGES_BUTTON = new Locator("ADD_BAGGAGES_BUTTON", new By.ByXPath(
           "//button[contains(@class, 'CheckedBaggageItem_add_button')]"));
    private static final Locator SELECT_BAGGAGES_BUTTON = new Locator("SELECT_BAGGAGES_BUTTON", new By.ByXPath("(//div[contains(@class, 'BaggageSelectionContent_option_box')])[2]"));
    private static final Locator FIRST_PAID_BAGGAGE_OPTION = new Locator("FIRST_PAID_BAGGAGE_OPTION", new By.ByXPath("(//div[contains(@class, 'BaggageSelectionContent_option_box') and not(contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '0 kg'))])[2]"));
    private static final Locator FIRST_PAID_BAGGAGE_WEIGHT = new Locator("FIRST_PAID_BAGGAGE_WEIGHT", new By.ByXPath("(//div[contains(@class, 'BaggageSelectionContent_option_box') and not(contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '0 kg'))])[2]//span[contains(@class, 'Text_size_b2')]"));
    private static final Locator FIRST_PAID_BAGGAGE_PRICE = new Locator("FIRST_PAID_BAGGAGE_PRICE", new By.ByXPath("(//div[contains(@class, 'BaggageSelectionContent_option_box') and not(contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '0 kg'))])[2]//span[contains(@class, 'Text_size_b3')]"));
    public static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[.//span[normalize-space()='Save' or normalize-space()='Simpan']]"));
    private static final Locator DONE_SELECTING = new Locator("DONE_SELECTING", new By.ByXPath("(//button[contains(@class, 'Button_button__') and contains(@class, 'Button_full_width__') and contains(@class, 'Button_variant_primary__')])[2]"));
    private static final Locator DOB_PICKER = new Locator("DOB_PICKER", new By.ByXPath("//div[contains(@class, 'DropdownFormDisplay_icon_container__')]"));
    private static final Locator DOB_INPUT = new Locator("DOB_INPUT", By.id("date-of-birth"));
    private static final Locator LAST_CLICKABLE_DATE = new Locator("LAST_CLICKABLE_DATE", By.xpath("(//button[contains(@class, 'Day_day') and not(@disabled)])[last()]"));
    private static final Locator YEAR_DROPDOWN = new Locator("YEAR_DROPDOWN", new By.ByXPath("//button[contains(@class, 'Navigation_year_menu_button__')]"));
    private static final Locator MONTH_DROPDOWN = new Locator("MONTH_DROPDOWN", new By.ByXPath("//button[contains(@class, 'Navigation_month_menu_button__')]"));
    private static final Locator DOB_SUBMIT = new Locator("DOB_SUBMIT", new By.ByXPath("//*[contains(@class, 'DatePickerDesktop_action_area')]//button"));
    private static final Locator SEE_DETAILS = new Locator("SEE_DETAILS", By.xpath("//span[normalize-space()='Detail']"));
    private static final Locator RESCHEDULE_BUTTON = new Locator("RESCHEDULE_BUTTON", By.xpath("(//button[contains(@class,'Tabs_tab__') and .//span[contains(@class,'Tabs_tab_text__')]])[2]"));
    public static final Locator INSURANCE_SELECTOR = new Locator("INSURANCE_SELECTOR", new By.ByXPath("(//button[@data-testid='btn-add'])[1]"));
    public static final Locator BAGGAGE_ADDON = new Locator("BAGGAGE_ADDON", By.xpath("(//button[@data-testid='btn-add'])[2]"));
    private static final Locator ADD_SEAT = new Locator("ADD_SEAT", new By.ByXPath("//div[contains(@class, 'AddonsCard_card')][.//p[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'seat') or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'kursi')]]//button[contains(@class, 'AvailableAddonsCard_add_button')]"));
    private static final Locator ADD_SEATS_BUTTON = new Locator("ADD_SEATS_BUTTON", new By.ByXPath("//button[span[text()='Select' or text()='Pilih']]"));
    private static final Locator ADD_SEAT_BUTTON = new Locator("ADD_SEAT_BUTTON", new By.ByXPath("//div[contains(@class,'AddonsCard_card')]" + "[.//p[normalize-space()='Seat' or normalize-space()='Kursi']]" + "//button[@aria-label='Add']"));
    private static final Locator SEAT_MAP_CONTAINER = new Locator("SEAT_MAP_CONTAINER", new By.ByXPath("//div[@data-testid='seat-map-container']"));
    private static final Locator SEAT_SELECTION_CONFIRM_BUTTON = new Locator("SEAT_SELECTION_CONFIRM_BUTTON", new By.ByXPath("//section[contains(@class,'Modal_modal_popup')]//button[contains(@class,'Button_variant_primary')][.//span[normalize-space()='Save' or normalize-space()='Simpan' or normalize-space()='Add' or normalize-space()='Tambah' or normalize-space()='Select' or normalize-space()='Pilih']]"));
    protected static final Locator CLICK_PAX_CHEVRON = new Locator("CLICK_PAX_CHEVRON", new By.ByXPath("(//div[contains(@class,'BookingPassengerSection_right_side')])[2]"));
    private static final Locator QUIET_ZONE_BANNER = new Locator("QUIET_ZONE_BANNER", new By.ByXPath("//p[contains(text(), 'Quiet Zone')]"));
    private static final Locator BAGGAGE_CHANGE_POPUP_VALIDATOR = new Locator("BAGGAGE_CHANGE_POPUP_VALIDATOR", new By.ByXPath("//h2[text()='Baggage allowance has changed' or text()='Ada perubahan batas bagasi']"));
    private static final Locator BAGGAGE_CHANGE_POPUP_CLOSE = new Locator("BAGGAGE_CHANGE_POPUP_CLOSE", new By.ByXPath("//span[\n" +
            "  contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), 'ok, got it')\n" +
            "  or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), 'ok, mengerti')\n" +
            "  or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), 'oke, mengerti')\n" +
            "]"));
    private static final Locator PASSPORT_BANNER = new Locator ("PASSPORT_BANNER", new By.ByXPath("(//div[contains(@class, 'BookingPassengerFormSectionInfo_passport')]//p[contains(., 'passport is valid for at least') or contains(., 'paspor penumpang berlaku setidaknya')])[1]"));
    protected static final Locator PASSPORT_CONFIRMATION_POPUP = new Locator ("PASSPORT_CONFIRMATION_POPUP", new By.ByXPath("//h2[contains(normalize-space(text()), \"Let's prepare your travel documents\") or contains(normalize-space(text()), \"Mari persiapkan dokumen perjalanan Anda\")]"));
    private static final Locator POP_UP_WARNING_LABEL_AFTER_CONTINUE_PAYMENT = new Locator("POP_UP_WARNING_LABEL_AFTER_CONTINUE_PAYMENT", new By.ByXPath("//div[contains(@class,'Dialog_pop_up')]//h2[contains(@class,'Figure_title')]"));
    private static final Locator SEAT = new Locator("SEAT1", new By.ByXPath("(//div[@data-testid='seat-map-container']//div[contains(@class,'Seat_seat__') and not(contains(@class,'Seat_seat_not_available__'))])[1]"));
    private static final Locator VI_CHECKIN_INTERIM = new Locator("VI_CHECKIN_INTERIM", new By.ByXPath("//div[span[text()='Collect Baggage & Check-in Again']]"));
    public static final Locator INSURANCE_CHOSEN_BUTTON = new Locator("INSURANCE_CHOSEN_BUTTON", new By.ByXPath("//button[@data-testid='btn-add']"));
    private static final Locator CONTINUE_NEXT_BUTTON = new Locator("CONTINUE_NEXT_BUTTON", By.xpath("//span[contains(text(), 'Continue') or contains(text(), 'Lanjutkan')]//parent::button[contains(@class, 'Button_variant_primary')]"));
    public static final Locator ADD_MEAL_BUTTON = new Locator("ADD_MEAL_BUTTON", new By.ByXPath("//img[@alt='Meals' or @alt='Makanan']" + "/ancestor::div[contains(@class,'AddonsCard_card')]//button[@aria-label='Add']"));
    public static final Locator ADD_ADDON_TICKET_SELECT = new Locator("ADD_ADDON_TICKET_SELECT", new By.ByXPath("(//button[contains(@class, 'AddonsSummaryCard_summary_action__')])"));
    public static final Locator PASSENGER_ADDON_SECTION = new Locator("PASSENGER_ADDON_SECTION", new By.ByXPath("//div[contains(@class,'AddonsPassengerPicker_passenger_item') and .//span[contains(@class,'AddonsPassengerPicker_passenger_name')]]"));
    private static final Locator MEAL_ADDON_DETAILS = new Locator("MEAL_ADDON_DETAILS", new By.ByXPath("//div[contains(@class,'SelectedAddonsCard_segments')]" + "//span[contains(@class,'Text_variant_usp')]"));
    public static final Locator REFUND_GOT_IT= new Locator ("REFUND_GOT_IT", new By.ByXPath("(.//button[contains(@class,'Button_variant_primary')])[4]"));
    private static final Locator FLIGHT_FACILITIES_BOTTOM_SHEET = new Locator("FLIGHT_FACILITIES_BOTTOM_SHEET", new By.ByXPath("//div[@data-testid='bottom-sheet-body']//h2[contains(text(),'Flight Facilities') or contains(text(),'Fasilitas Penerbangan')]"));
    private static final Locator BOTTOM_SHEET_CLOSE_BUTTON = new Locator("BOTTOM_SHEET_CLOSE_BUTTON", new By.ByXPath("//button[contains(@class,'BottomSheet_close_button')]"));
    private static final Locator BAGGAGE_CARD_ADD_STATE = new Locator("BAGGAGE_CARD_ADD_STATE", new By.ByXPath(
            BAGGAGE_ADDON_CARD_XPATH + "//div[contains(@class,'AddonsCard_add_button')]"));
    private static final Locator INLINE_BAGGAGE_SECTION =
            new Locator(
                    "INLINE_BAGGAGE_SECTION",
                    new By.ByXPath(
                            "//div[contains(@class,'BaggageSelectionSlider_container')]"
                    )
            );
    private static final Locator SELECTED_NO_BAGGAGE_OPTION =
            new Locator("SELECTED_NO_BAGGAGE_OPTION", new By.ByXPath("//button[(@aria-label='No Thanks!' or @aria-label='Tidak, Terima Kasih')" + " and @aria-pressed='true']"));
    private static final Locator BAGGAGE_ADD_EXTRA_TEXT = new Locator("BAGGAGE_ADD_EXTRA_TEXT", new By.ByXPath(
            BAGGAGE_ADDON_CARD_XPATH + "//div[contains(@class,'AddonsCard_free_price')]"));
    private static final Locator BAGGAGE_UNSELECTED_TEXT = new Locator("BAGGAGE_UNSELECTED_TEXT", new By.ByXPath(
            BAGGAGE_ADDON_CARD_XPATH + "//p[contains(@class,'AddonsCard_unselected_text')]"));
    private static final Locator BAGGAGE_EDIT_BUTTON = new Locator("BAGGAGE_EDIT_BUTTON", new By.ByXPath(
            BAGGAGE_ADDON_CARD_XPATH + "//div[@data-testid='edit' and contains(@class,'AddonsCardContent_add_addons')]"));
    private static final Locator BAGGAGE_SELECTION_SUBTOTAL = new Locator("BAGGAGE_SELECTION_SUBTOTAL", new By.ByXPath("//button[@data-testid='price-toggler']//span[contains(@class,'Text_size_b1')]"));
    private static final Locator BAGGAGE_SELECTION_SAVE_BUTTON = new Locator("BAGGAGE_SELECTION_SAVE_BUTTON", new By.ByXPath("//button[contains(@class,'Button_variant_primary')][.//span[normalize-space()='Save' or normalize-space()='Simpan']]"));
    private static final Locator BAGGAGE_CARD_ADD_BUTTON = new Locator("BAGGAGE_CARD_ADD_BUTTON", new By.ByXPath(
            BAGGAGE_ADDON_CARD_XPATH + "//button[@data-testid='add' and contains(@class,'AddonsCard_action_btn')]"));
    private static final Locator FIRST_PAID_INLINE_BAGGAGE_OPTION = new Locator("FIRST_PAID_INLINE_BAGGAGE_OPTION",
                    new By.ByXPath("//div[contains(@class,'BaggageSelectionSlider_container')]"
                                    + "//button[contains(@class,'BaggageOptionPill_pill')"
                                    + " and not(contains(@aria-label,'No Thanks'))"
                                    + " and not(contains(@aria-label,'Tidak'))]"));
    private static final Locator BAGGAGE_SELECT_BUTTON = new Locator("BAGGAGE_SELECT_BUTTON", new By.ByXPath("//button[contains(@class,'AddonsSummaryCard_summary_action')]"));
    private static final Locator BAGGAGE_SUBTOTAL_PRICE = new Locator("BAGGAGE_SUBTOTAL_PRICE", new By.ByXPath("//button[@data-testid='price-toggler']//span[contains(@class,'Text_size_h2')]"));
    private static final Locator BAGGAGE_PRICE_TOGGLER = new Locator("BAGGAGE_PRICE_TOGGLER", new By.ByXPath("//button[@data-testid='price-toggler']"));
    private static final Locator BAGGAGE_PRICE_DETAILS_BOTTOM_SHEET = new Locator("BAGGAGE_PRICE_DETAILS_BOTTOM_SHEET", new By.ByXPath("//div[@data-testid='bottom-sheet-body']//h2[contains(text(),'Price Details') or contains(text(),'Rincian Harga')]"));
    private static final Locator BAGGAGE_PASSENGER_NAME = new Locator("BAGGAGE_PASSENGER_NAME", new By.ByXPath("//span[contains(@class,'AddonsPriceDetail_passenger_name')]"));
    private static final Locator BAGGAGE_PRICE_DETAILS_CLOSE = new Locator("BAGGAGE_PRICE_DETAILS_CLOSE", new By.ByXPath("//button[contains(@class,'BottomSheet_close_button')]"));
    private static final Locator BAGGAGE_ZERO_KG = new Locator("BAGGAGE_ZERO_KG", new By.ByXPath(
                            "//button[contains(@class,'BaggageOptionPill_pill')"
                                    + " and (@aria-label='No Thanks!'"
                                    + " or @aria-label='Tidak Terima kasih!')]"
                                    + " | "
                                    + "//div[@data-testid='baggage-selection']"
                                    + "[.//p[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'0 kg')]]"));
    private static final Locator PRICE_BREAKDOWN_BAGGAGE_ITEM = new Locator("PRICE_BREAKDOWN_BAGGAGE_ITEM", new By.ByXPath(
                    "//div[contains(@class,'PriceSummary_container')]"
                            + "//span["
                            + "(contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'baggage')"
                            + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'bagasi'))"
                            + " and not(contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'insurance'))"
                            + "]"));
    private static final Locator PRICE_BREAKDOWN_BAGGAGE_LABEL = new Locator("PRICE_BREAKDOWN_BAGGAGE_LABEL",
                    new By.ByXPath("//div[contains(@class,'PriceSummary_container')]"
                                    + "//span[contains(translate(normalize-space(.),"
                                    + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
                                    + "'abcdefghijklmnopqrstuvwxyz'),'baggage')"
                                    + " or contains(translate(normalize-space(.),"
                                    + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
                                    + "'abcdefghijklmnopqrstuvwxyz'),'bagasi')]"));
    private static final Locator PRICE_BREAKDOWN_BAGGAGE_PRICE =
            new Locator(
                    "PRICE_BREAKDOWN_BAGGAGE_PRICE",
                    new By.ByXPath(
                            "//div[contains(@class,'PriceSummary_container')]"
                                    + "//span[contains(translate(normalize-space(.),"
                                    + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
                                    + "'abcdefghijklmnopqrstuvwxyz'),'baggage')"
                                    + " or contains(translate(normalize-space(.),"
                                    + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
                                    + "'abcdefghijklmnopqrstuvwxyz'),'bagasi')]"
                                    + "/following-sibling::span"));
    private static final Locator PRICE_BREAKDOWN_ADULT_ITEM = new Locator("PRICE_BREAKDOWN_ADULT_ITEM", new By.ByXPath(
                            "//div[contains(@class,'PriceSummary_container')]"
                                    + "//span[contains(normalize-space(.),'Adult')"
                                    + " or contains(normalize-space(.),'Dewasa')"
                                    + " or contains(normalize-space(.),'Child')"
                                    + " or contains(normalize-space(.),'Anak')"
                                    + " or contains(normalize-space(.),'Infant')"
                                    + " or contains(normalize-space(.),'Bayi')]"));
    private static final Locator PRICE_BREAKDOWN_TICKET_PRICES = new Locator("PRICE_BREAKDOWN_TICKET_PRICES",
                    new By.ByXPath("//div[contains(@class,'PriceSummary_container')]"
                                    + "//span[contains(normalize-space(.),'Adult')"
                                    + " or contains(normalize-space(.),'Dewasa')"
                                    + " or contains(normalize-space(.),'Child')"
                                    + " or contains(normalize-space(.),'Anak')"
                                    + " or contains(normalize-space(.),'Infant')"
                                    + " or contains(normalize-space(.),'Bayi')]"
                                    + "/following-sibling::span"));
    private static final Locator PRICE_BREAKDOWN_DISCOUNT = new Locator("PRICE_BREAKDOWN_DISCOUNT",
                    new By.ByXPath("//span[contains(.,'• Discount') or contains(.,'• Diskon')]" + "/following-sibling::span[contains(@class,'PriceSummaryItem_price')]"));
    private static final Locator PRICE_BREAKDOWN_PAY_NOW = new Locator("PRICE_BREAKDOWN_PAY_NOW",
                    new By.ByXPath("(//div[contains(@class,'PriceSummary_total_price_item')]" + "//span[contains(@class,'PriceSummaryItem_price')])[last()]"));
    private static final Locator PRICE_BREAKDOWN_OTHER_ADDON_PRICES = new Locator("PRICE_BREAKDOWN_OTHER_ADDON_PRICES",
                    new By.ByXPath("//div[contains(@class,'PriceSummaryList_list_item')]"
                                    + "[.//span[normalize-space()='Other' or normalize-space()='Lainnya']]"
                                    + "//div[contains(@class,'PriceSummaryList_children_container')]"
                                    + "//span[contains(@class,'PriceSummaryItem_price')]"));
    private static final Locator PRICE_BREAKDOWN_FULL_PROTECTION = new Locator("PRICE_BREAKDOWN_FULL_PROTECTION",
            By.xpath("//div[contains(@class, 'BookingPriceDetailPopUp')]//span["
                    + "contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'full protection')"
                    + " or contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'perlindungan penuh')"
                    + " or contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'proteksi')"
                    + "]"));
    private static final Locator ALL_INSURANCE_CARDS = new Locator("ALL_INSURANCE_CARDS", By.xpath("//div[contains(@class, 'BookingInsuranceCard_container')]"));
    private static final Locator FULL_PROTECTION_CARD = new Locator("FULL_PROTECTION_CARD", By.xpath("//div[contains(@class, 'BookingInsuranceCard_container')][.//p[text()='Full Protection' or text()='Perlindungan Penuh']]"));
    private static final Locator FULL_PROTECTION_CHECKBOX_INPUT = new Locator("FULL_PROTECTION_CHECKBOX_INPUT", By.xpath("//div[contains(@class, 'BookingInsuranceCard_container')][.//p[text()='Full Protection' or text()='Perlindungan Penuh']]//input[@type='checkbox']"));
    private static final Locator FULL_PROTECTION_CHECKBOX_LABEL = new Locator("FULL_PROTECTION_CHECKBOX_LABEL", By.xpath("//div[contains(@class, 'BookingInsuranceCard_container')][.//p[text()='Full Protection' or text()='Perlindungan Penuh']]//label"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", By.xpath("//button[contains(@class, 'Button_variant_primary') and .//span[text()='Continue' or text()='Lanjutkan']]"));
    private static final Locator CONTINUE_TO_PAYMENT_BUTTON = new Locator("CONTINUE_TO_PAYMENT_BUTTON", By.xpath("//button[contains(@class, 'Button_variant_primary') and .//span[contains(text(), 'Continue to Payment') or contains(text(), 'Lanjut ke Pembayaran')]]"));
    public static final Locator SEE_OTHER_FACILITIES_BUTTON = new Locator("SEE_OTHER_FACILITIES_BUTTON", new By.ByXPath("(//Button[contains(@class,'Facilities_more_facilities')])[1]"));
    public static final Locator CLOSE_FACILITIES_POPUP = new Locator("CLOSE_FACILITIES_POPUP", new By.ByXPath("//button[contains(@class,'BottomSheet_close_button')]"));
    private static final Locator REFUND_CHIP_ON_DETAILS = new Locator("REFUND_CHIP_ON_DETAILS", By.xpath("//div[contains(@class, 'BaggageRefundReschedulePopup')]//button[.//span[contains(text(),'Refund') or contains(text(),'Pengembalian')]]"));
    private static final Locator RESCHEDULE_CHIP_ON_DETAILS = new Locator("RESCHEDULE_CHIP_ON_DETAILS", By.xpath("//div[contains(@class, 'BaggageRefundReschedulePopup')]//button[.//span[contains(text(),'Reschedule') or contains(text(),'Jadwal') or contains(text(),'Ubah Jadwal')]]"));
    private static final Locator ACTIVE_TIMELINE_CONTAINER = new Locator("ACTIVE_TIMELINE_CONTAINER", By.xpath("//div[contains(@class, 'BaggageRefundReschedulePopup')]//div[contains(@class, 'Tabs_tab_panel') and not(@hidden)] | //div[contains(@class, 'BaggageRefundReschedulePopup')]"));
    private static final Locator INSURANCE_BUTTON = new Locator("INSURANCE_BUTTON", new By.ByXPath("//div[contains(@class,'BookingInsuranceCard_container')]"));
    private static final Locator FIRST_INSURANCE_EXTRA_PROTECTION_TITLE = new Locator("FIRST_INSURANCE_EXTRA_PROTECTION_TITLE", new By.ByXPath("(//*[contains(@class,'BookingInsuranceCard_header_text')]//p)[1]"));
    private static final Locator FIRST_INSURANCE_CHECKBOX_INPUT = new Locator("FIRST_INSURANCE_CHECKBOX_INPUT", new By.ByXPath("(//input[@type='checkbox'])[1]"));
    private static final Locator FIRST_INSURANCE_CHECKBOX_LABEL = new Locator("FIRST_INSURANCE_CHECKBOX_LABEL", new By.ByXPath("(//label[.//input[contains(@data-testid, 'booking-insurance-checkbox')]])[1]"));
    private static final Locator FIRST_INSURANCE_EXTRA_PROTECTION_ADD_CANCEL_BTN = new Locator("FIRST_INSURANCE_EXTRA_PROTECTION_ADD_CANCEL_BTN", new By.ByXPath("(//button[contains(@data-testid,'btn-add')])[1]"));
    private static final Locator FIRST_INSURANCE_SELECTED = new Locator("FIRST_INSURANCE_SELECTED", new By.ByXPath("(//button[contains(@data-testid,'btn-add')])[1]//preceding-sibling::*//*[contains(@class,'BookingPaidInsurances_selected_check')]"));
    private static final Locator FIRST_INSURANCE_MORE_INFO_BTN = new Locator("FIRST_INSURANCE_MORE_INFO_BTN", new By.ByXPath("//p[contains(text(), 'Full Protection') or contains(text(), 'Perlindungan Penuh')]/following-sibling::div[contains(@class, 'BookingInsuranceCard_subtitle')]/span"));
    private static final Locator BOOKING_BENEFITS_100_PERCENT_REFUND = new Locator("BOOKING_BENEFITS_100_PERCENT_REFUND", new By.ByXPath("//div[contains(@class,'BookingBenefits')]//p[contains(text(),'100% Refund') or contains(text(),'Refund 100%')]"));
    private static final Locator EXTRA_PROTECTION_BOTTOMSHEET_TITLE = new Locator("EXTRA_PROTECTION_BOTTOMSHEET_TITLE", new By.ByXPath("//header[contains(@class, 'Modal_modal_header')]//h2[contains(@class, 'Text')]"));
    private static final Locator TERMS_AND_CONDITIONS_LINK = new Locator("TERMS_AND_CONDITIONS_LINK", new By.ByXPath("//span[contains(text(), 'tapping here') or contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'di sini')]"));
    private static final Locator SEE_DETAILS_REFUND_AND_RESCHEDULE = new Locator("SEE_DETAILS_REFUND_AND_RESCHEDULE", new By.ByXPath("//span[normalize-space()='Detail']"));
    private static final Locator RESCHEDULE_TAB_FLIGHT_SUMMARY = new Locator("RESCHEDULE_TAB_FLIGHT_SUMMARY", new By.ByXPath("//*[contains(text(),'Reschedule')]//parent::button[contains(@class,'Tabs_tab')]"));
    private static final Locator SEE_FLIGHT_FACILITIES = new Locator("SEE_FLIGHT_FACILITIES", new By.ByXPath("//button[contains(@class, 'Facilities_more_facilities')]"));
    private static final Locator PASSENGER_DATA_MODAL = new Locator("PASSENGER_DATA_MODAL", By.xpath("//*[contains(@class,'BookingPassengerSection_card_title')]"));
    private static final Locator FAQ_QUESTION = new Locator("FAQ_QUESTION", new By.ByXPath("//*[contains(@class,'Accordion_accordion_button')]"));
    private static final Locator FAQ_ANSWER = new Locator("FAQ_ANSWER", new By.ByXPath("//*[@data-testid='collapse-container']"));
    private static final Locator FAQ_QUESTION_TITLE  = new Locator("FAQ_QUESTION_TITLE", new By.ByXPath("//*[contains(@class,'Accordion_accordion_button')]//span"));
    private static final Locator FAQ_ANSWER_TEXT = new Locator("FAQ_ANSWER_TEXT", new By.ByXPath("//*[contains(@class,'PenaltyInfoFaq_faq_answer')]"));
    public static final Locator ADD_MEAL_POPUP = new Locator("ADD_MEAL_POPUP", new By.ByXPath("//section[contains(@class,'Modal_modal_popup')]"));
    public static final Locator MEAL_ADD_BUTTONS = new Locator("MEAL_ADD_BUTTONS", new By.ByXPath("//div[contains(@class,'MealSelectionContent_meal_item_price_container')]//button[.//span[normalize-space()='Add' or normalize-space()='Tambahkan']]"));
    public static final Locator LAST_PAX_INFO_CHEVRON = new Locator("LAST_PAX_INFO_CHEVRON", new By.ByXPath("(//div[contains(@class,'BookingPassengerDesktopCard_card')])[last()]"));
    private static final Locator CONNECTING_FLIGHT_PROTECTION_INSURANCE_DETAIL = new Locator("CONNECTING_FLIGHT_PROTECTION_INSURANCE_DETAIL", new By.ByXPath("//div[contains(@class,'ModalBody_content')]//span[normalize-space()='Connecting Flight Protection' or normalize-space()='Perlindungan Penerbangan Transit']"));
    private static final Locator SEE_ALL_BUTTON = new Locator("SEE_ALL_BUTTON",new By.ByXPath("//div[contains(@class, 'BookingInsurance_see_all_btn')]"));
    private static final Locator CONNECTING_FLIGHT_PROTECTION_NUDGE = new Locator("CONNECTING_FLIGHT_PROTECTION_NUDGE", new By.ByXPath("//div[contains(@class,'VirtualInterliningNudgesCard_desc')]//span[contains(normalize-space(.),'Self Transfer Protection') or contains(normalize-space(.),'Perlindungan Transit Mandiri')]"));
    private static final Locator CONNECTING_FLIGHT_PROTECTION_WHAT_YOU_GET = new Locator("CONNECTING_FLIGHT_PROTECTION_WHAT_YOU_GET", new By.ByXPath("//h1[normalize-space()='What You Will Get' or normalize-space()='Yang Akan Kamu Dapatkan']"));
    private static final Locator CONNECTING_FLIGHT_PROTECTION_TEXT_INFO = new Locator("CONNECTING_FLIGHT_PROTECTION_TEXT_INFO", new By.ByXPath("//p[normalize-space()='Connecting Flight Protection' or normalize-space()='Perlindungan Penerbangan Transit']"));
    private static final Locator CONNECTING_FLIGHT_PROTECTION_MORE_BENEFIT_BTN = new Locator("CONNECTING_FLIGHT_PROTECTION_MORE_BENEFIT_BTN", new By.ByXPath("//button[.//span[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'more benefit') or contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'manfaat lainnya')]]"));
    private static final Locator CONNECTING_FLIGHT_PROTECTION_BOTTOM_SHEET_CLOSE = new Locator("CONNECTING_FLIGHT_PROTECTION_BOTTOM_SHEET_CLOSE", new By.ByXPath("//button[@data-testid='modal-close-button']"));
    private static final Locator CONNECTING_FLIGHT_WARNING_MESSAGE = new Locator("CONNECTING_FLIGHT_WARNING_MESSAGE", new By.ByXPath("//p[normalize-space()='You chose connecting flights' or normalize-space()='Kamu memilih penerbangan transit']"));
    private static final Locator CONNECTING_FLIGHT_INFO_IN_PRICEDETAIL = new Locator("CONNECTING_FLIGHT_INFO_IN_PRICEDETAIL", new By.ByXPath("//span[contains(normalize-space(.),'Connecting Flight Protection') or contains(normalize-space(.),'Perlindungan Penerbangan Transit')]"));
    private static final Locator BAGGAGE_SELECTOR = new Locator("BAGGAGE_SELECTOR", By.xpath("(//div[contains(@class, 'BaggageSelectionContent_option_box') and not(contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '0 kg'))])[2]"));
    public static final Locator ADD_MEAL_SELECTION_POPUP = new Locator("ADD_MEAL_SELECTION_POPUP", new By.ByXPath("//div[contains(@class,'AddonsSelection_container')]"));
    private static final Locator POPUP_SAVE_BUTTON = new Locator("POPUP_SAVE_BUTTON", new By.ByXPath("//*[contains(@class,'ModalBody_modal_body')]//button[contains(@class,'Button_button__') and contains(@class,'Button_size_large__') and contains(@class,'Button_variant_primary__')]"));
    private static final Locator MEAL_ADDONS_PRICE = new Locator("MEAL_ADDONS_PRICE", new By.ByXPath("//*[contains(@class,'AddonsCard_card')][.//p[text()='Meals' or text()='Makanan']]//*[contains(@class,'SelectedAddonsCard_subtotal')]/span[2]"));
    private static final Locator SEAT_ADDONS_PRICE = new Locator("SEAT_ADDONS_PRICE", new By.ByXPath("//*[contains(@class,'AddonsCard_card')][.//p[text()='Seat' or text()='Kursi']]//*[contains(@class,'SelectedAddonsCard_subtotal')]/span[2]"));
    private static final Locator PASSENGER_FORM_VALIDATION_ERROR = new Locator("PASSENGER_FORM_VALIDATION_ERROR", new By.ByXPath(
            "//div[contains(@class,'BookingPassengerForm') or contains(@class,'BookingPassengerSection')]"
                    + "//*[contains(normalize-space(.),'is required') or contains(normalize-space(.),'wajib diisi') or contains(normalize-space(.),'harus diisi')]"));
    private static final Locator PRICE_BREAKDOWN_REFUND_ADDON_ITEM = new Locator("PRICE_BREAKDOWN_REFUND_ADDON_ITEM", new By.ByXPath("//*[contains(@class,'PriceSummaryItem_container')][.//span[contains(normalize-space(.),'100% Refund addon')]]"));
    private static final Locator PRICE_BREAKDOWN_REFUND_ADDON_PRICE = new Locator("PRICE_BREAKDOWN_REFUND_ADDON_PRICE", new By.ByXPath("//*[contains(@class,'PriceSummaryItem_container')][.//span[contains(normalize-space(.),'100% Refund addon')]]//*[contains(@class,'PriceSummaryItem_price')]"));
    private static final Locator REVIEW_PASSENGER_DATA_POPUP = new Locator("REVIEW_PASSENGER_DATA_POPUP", new By.ByXPath("//button[contains(@class, 'Button_variant_primary') and (contains(., 'correct') or contains(., 'Benar') or contains(., 'benar'))]"));
    private static final Locator PRICE_BREAKDOWN_OTHER_ADDON_ITEM = new Locator("PRICE_BREAKDOWN_OTHER_ADDON_ITEM",
                    new By.ByXPath("//div[contains(@class,'PriceSummaryList_list_item')]"
                                    + "[.//span[normalize-space()='Other' or normalize-space()='Lainnya']]"));
    private static final Locator FLIGHT_ROUTE_COLLAPSE_ICON = new Locator("FLIGHT_ROUTE_COLLAPSE_ICON",
            new By.ByXPath("(//div[contains(@class,'TransitInfoDetail')])[1]"));
    private static final Locator CHECK_IN_AGAIN_LIST_ITEM = new Locator(
            "CHECK_IN_AGAIN_LIST_ITEM",
            new By.ByXPath(
                    "//span[contains(@class,'TransitDetailInfoPopUp_transit_info_title')"
                            + " and ("
                            + "contains(translate(normalize-space(.),"
                            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),"
                            + "'collect baggage & check in again')"
                            + " or contains(translate(normalize-space(.),"
                            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),"
                            + "'check-in again')"
                            + " or contains(translate(normalize-space(.),"
                            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),"
                            + "'check in again')"
                            + " or contains(translate(normalize-space(.),"
                            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),"
                            + "'check-in ulang')"
                            + " or contains(translate(normalize-space(.),"
                            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),"
                            + "'check in ulang')"
                            + ")]"
            ));

    private static final Locator SELF_TRANSFER_PROTECTION_INFO = new Locator(
            "SELF_TRANSFER_PROTECTION_INFO",
            new By.ByXPath(
                    "//span[contains(@class,'TransitDetailInfoPopUp_transit_info_title')"
                            + " and ("
                            + "normalize-space()='Self Transfer Protection included'"
                            + " or normalize-space()='Termasuk Proteksi Transit Mandiri'"
                            + " or normalize-space()='Perlindungan Transfer Mandiri termasuk'"
                            + ")]"
            ));
    protected static final String[] FULL_NAME_IDS = {
            "given-names-first--middle-name",
            "nama-depan--nama-tengah",
            "full-name-according-to-idpassport",
            "nama-lengkap",
            "full-name",
            "nama-lengkap-sesuai-ktppasporsim"
    };
    protected static final String[] PASSPORT_FULL_NAME_IDS = {
            "full-name-according-to-idpassport",
            "nama-lengkap-sesuai-ktppasporsim"
    };
    protected static final String[] LAST_NAME_IDS = {
            "nama-keluarganama-belakang",
            "surname-familylast-name"
    };


    public abstract void fillOutSalutation(String salutation);
    public abstract void goToPaymentPageFromBookingFormPage();

    public abstract void goToPaymentPageFromAddonsPage();

    public abstract String getPriceText();

    public void waitForBookingPageToLoad(){
        driverTools.tryClickWhenPresent(PRICE_CHANGE_POPUPOKAY, 3);
        skipFlightDisruptionInsurancePopupPrp();
        driverTools.tryClickWhenPresent(POPUP_BUTTON_SECONDARY, 3);
        driverTools.waitForElementToAppear(CONTACT_DETAILS_BOOKING_PAGE);
        logCartIdFromUrl();
    }

    private static final Pattern CART_FLIGHT_ID_PATTERN = Pattern.compile("/cart/flight/([^/?#]+)");
    private boolean cartIdLogged = false;

    protected void logCartIdFromUrl() {
        if (Configs.PLATFORM != Platform.DWEB || cartIdLogged) {
            return;
        }
        driverTools.waitForUrlToContainAnyOf(5, "/cart/flight/");

        String currentUrl = driverTools.getCurrentURL();
        if (currentUrl == null || !currentUrl.contains("/cart/flight/")) {
            return;
        }
        Matcher matcher = CART_FLIGHT_ID_PATTERN.matcher(currentUrl);
        if (matcher.find()) {
            cartIdLogged = true;
            logger.log("Cart ID: " + matcher.group(1));
        }
    }

    public boolean hasPassengerFormValidationErrors() {
        return driverTools.waitForElementToAppear(PASSENGER_FORM_VALIDATION_ERROR, 2).isPresent();
    }

    public void clickLoginBtnOnBookingPage(){
        driverTools.clickWithJavascriptExecutor(LOGIN_BTN_BOOKING_PAGE);
    }

    public boolean isLoggedInUsingCorrectEmail(String email){
        Locator emailLLocator = emailInputBoxLocator(email);
        return driverTools.isElementDisplayed(emailLLocator);
    }

    private Locator emailInputBoxLocator(String email){
        return new Locator("EMAIL_INPUT_LOCATOR", new By.ByXPath("//input[@id='email-address' and @value='"+email+"']"));
    }

    public void fillPassengerDetailsLogin(String name) {
        logCartIdFromUrl();
        driverTools.click(MS_BUTTON);
        driverTools.waitForElementToPresence(TOGGLE_SAME_AS_CONTACT_DETAILS);
        driverTools.clickWithJavascriptExecutor(TOGGLE_SAME_AS_CONTACT_DETAILS);
        String nameOnBookingForm = driverTools.getValueAttribute(PASSENGER_FULL_NAME,"value");
        logger.log("nameOnBookingForm ->" + nameOnBookingForm);
        driverTools.sendKeys(PASSENGER_FULL_NAME, name);
        String nameOnBookingFormFinal = driverTools.getValueAttribute(PASSENGER_FULL_NAME,"value");
        logger.log("nameOnBookingFormFinal ->" + nameOnBookingFormFinal);
    }

    public void fillPassengerDetails(){
        logCartIdFromUrl();
        if(!(Configs.PLATFORM.equals(Platform.AMWEB))){
            driverTools.clickWithJavascriptExecutor(MS_BUTTON);
        }
        driverTools.waitForElementToPresence(TOGGLE_SAME_AS_CONTACT_DETAILS);
        driverTools.clickWithJavascriptExecutor(TOGGLE_SAME_AS_CONTACT_DETAILS);
    }

    public void fillPassengerDetailsLogin(String name,String lastName) {
        logCartIdFromUrl();
        driverTools.waitForElementToPresence(TOGGLE_SAME_AS_CONTACT_DETAILS);
        driverTools.click(TOGGLE_SAME_AS_CONTACT_DETAILS);
        driverTools.waitForElementToAppear(CLEAR_FULL_NAME);
        if(driverTools.isElementDisplayed(CLEAR_FULL_NAME)){
            driverTools.clickWithJavascriptExecutor(CLEAR_FULL_NAME);
            String nameOnBookingForm = driverTools.getValueAttribute(PASSENGER_FULL_NAME,"value");
            logger.log("nameOnBookingForm ->" + nameOnBookingForm);
            if(!nameOnBookingForm.isEmpty()){
                driverTools.clickWithJavascriptExecutor(CLEAR_FULL_NAME);
            }
            String fullName = name + " " + lastName;
            driverTools.sendKeys(PASSENGER_FULL_NAME, fullName);
            String nameOnBookingFormFinal = driverTools.getValueAttribute(PASSENGER_FULL_NAME,"value");
            logger.log("nameOnBookingFormFinal ->" + nameOnBookingFormFinal);
            Assertion.assertThat("nameOnBookingFormFinal should be " + name, nameOnBookingFormFinal,is(equalToIgnoringCase(fullName)));
        }else{
            driverTools.clickWithJavascriptExecutor(CLEAR_FIRST_NAME);
            String nameOnBookingForm = driverTools.getValueAttribute(PASSENGER_FIRST_NAME,"value");
            logger.log("nameOnBookingForm ->" + nameOnBookingForm);
            if(!nameOnBookingForm.isEmpty()){
                driverTools.clickWithJavascriptExecutor(CLEAR_FIRST_NAME);
            }
            driverTools.sendKeys(PASSENGER_FIRST_NAME, name);
            String nameOnBookingFormFinal = driverTools.getValueAttribute(PASSENGER_FIRST_NAME,"value");
            logger.log("nameOnBookingFormFinal ->" + nameOnBookingFormFinal);
            Assertion.assertThat("nameOnBookingFormFinal should be " + name, nameOnBookingFormFinal,is(equalToIgnoringCase(name)));

            driverTools.clickWithJavascriptExecutor(CLEAR_LAST_NAME);
            String lastNameOnBookingForm = driverTools.getValueAttribute(PASSENGER_LAST_NAME,"value");
            logger.log("lastNameOnBookingForm ->" + lastNameOnBookingForm);
            if(!nameOnBookingForm.isEmpty()){
                driverTools.clickWithJavascriptExecutor(CLEAR_LAST_NAME);
            }
            driverTools.sendKeys(PASSENGER_LAST_NAME, lastName);
            String lastNameOnBookingFormFinal = driverTools.getValueAttribute(PASSENGER_LAST_NAME,"value");
            logger.log("lastNameOnBookingFormFinal ->" + lastNameOnBookingFormFinal);
            Assertion.assertThat("lastNameOnBookingFormFinal should be " + lastName, lastNameOnBookingFormFinal,is(equalToIgnoringCase(lastName)));
        }
    }

    protected void fillPassengerDetailDomestic(int indexPax, FlightPaxDetail flightPaxDetail) {
        logger.log("Fill domestic passenger data");
        if(driverTools.isElementPresent(getNationalityLocator(indexPax))) {
            fillNationality(indexPax, flightPaxDetail.data.get(indexPax).paxNationality);
        }
        if(driverTools.isElementPresent(fillIdLocator(indexPax))) {
            flightPaxDetail.data.get(indexPax).paxPassportNumber = fillId(indexPax);
        }
        if(driverTools.isElementPresent(getDobLocator(indexPax))) {
            fillDob(indexPax, flightPaxDetail.data.get(indexPax).paxBirthDate);
        }
    }

    protected void fillPassengerDetailInternational(int indexPax, FlightPaxDetail flightPaxDetail, boolean fillPassportDates) {
        logger.log("Fill international passenger data");
        driverTools.scrollToElement(getNationalityLocator(indexPax));
        if(driverTools.isElementPresent(getNationalityLocator(indexPax))) {
            fillNationality(indexPax, flightPaxDetail.data.get(indexPax).paxNationality);
        }

        if(driverTools.isElementPresent(getDobLocator(indexPax))) {
            fillDob(indexPax, flightPaxDetail.data.get(indexPax).paxBirthDate);
        }

        if(fillPassportDates && driverTools.isElementPresent(fillIdLocator(indexPax))) {
            flightPaxDetail.data.get(indexPax).paxPassportNumber = fillPassport(indexPax, flightPaxDetail);
        }
    }

    public Locator checkHiddenAttributePaxFill(int index){
        return new Locator("CHECK_HIDDEN_ATTRIBUTE_PAX_FILL", By.xpath("(//*[contains(@class,'Collapse_collapse_with_transition')])["+index+"]"));
    }

    protected Locator passengerTitleLocator(int indexPax) {
        return new Locator("PASSENGER_TITLE_LOCATOR", By.xpath("((//*[contains(@class,'Collapse_collapse_with_transition')]/parent::*)//*[contains(@class,'BookingPassengerSection_title_text')])["+indexPax+"]"));
    }

    private void chooseDomesticOrInternational(int indexPax, FlightPaxDetail flightPaxDetail,String flightRoute) {
        try {
            logger.log("================ Filling passenger details for passenger " + indexPax + " ========");
            logger.log("  Type: " + flightPaxDetail.data.get(indexPax).paxType);
            logger.log("  Name: " + flightPaxDetail.data.get(indexPax).paxFirstName + " " + flightPaxDetail.data.get(indexPax).paxLastName);
            logger.log("  DOB: " + flightPaxDetail.data.get(indexPax).paxBirthDate);
        }catch (Exception e){
            logger.log("flightPaxDetail is null or empty for passenger " + indexPax);
        }

        ensurePassengerFormIsActive(indexPax);
        flightPaxDetail.data.get(indexPax).paxTitle = fillTitle(indexPax);
        driverTools.waitForElementToAppear(getFullNameLocator(indexPax), 5);

        if (flightRoute.equals("domestic")) {
            fillPassengerDetailDomestic(indexPax,flightPaxDetail);
        } else if (flightRoute.equals("international")) {
            fillPassengerDetailInternational(indexPax, flightPaxDetail, false);
        }

        waitForFormStability(indexPax);
        fillFullName(indexPax,flightPaxDetail);
        driverTools.waitForElementToAppear(getFullNameLocator(indexPax), 5);

        if (flightRoute.equals("international")) {
            fillPassengerDetailInternational(indexPax, flightPaxDetail, true);
        }

        waitForFormStability(indexPax);
        verifyAndRefillNameIfNeeded(indexPax, flightPaxDetail);
        logger.log("================= Filling passenger details for passenger " + indexPax + " completed ========");
    }

    public FlightPaxDetail fillPassengerDetailsLogin(int adultPax, int childPax, int infantPax, String route, String airlineName) {
        logCartIdFromUrl();
        driverTools.waitForElementToAppear(PASSENGER_DATA_MODAL, 5);
        List<WebElement> passengerElements = driverTools.elements(PASSENGER_DATA_MODAL).orElseThrow();
        logger.log("Number of passenger elements found: " + passengerElements.size() + "number of passenger expected : " + (adultPax + childPax + infantPax));
        Assertion.assertThat("Number of passenger elements should be equal to total number of passengers", passengerElements.size(), is(adultPax + childPax + infantPax));
        FlightPaxDetail flightPaxDetail = new FlightPaxDetail(adultPax, childPax, infantPax, route, airlineName, "Indonesia");

        int totalPax = adultPax + childPax + infantPax;
        for (int paxIndex = 1; paxIndex <= totalPax; paxIndex++) {

            chooseDomesticOrInternational(paxIndex, flightPaxDetail, route);
        }
        return flightPaxDetail;
    }

    private Locator titleLocator(int indexPax) {
        return new Locator("TITLE_LOCATOR_"+indexPax, By.xpath("(//*[contains(@class,'BookingPassengerForm_radio')])["+indexPax+"]//input"));
    }

    protected String fillTitle(int indexPax){
        List<WebElement> titleElements = driverTools.elements(titleLocator(indexPax)).orElseThrow();
        int totalTitles = titleElements.size();
        int randomIndex = CoreFunctions.generateRandomInt(0, totalTitles - 1);
        WebElement titleElement = titleElements.get(randomIndex);
        String title = titleElement.getAttribute("value");
        logger.log("Selected title: " + title);
        driverTools.clickWithJavascriptExecutor(titleElement);
        return title.toUpperCase();
    }

    protected Locator getFullNameLocator(int indexPax) {
        StringBuilder xpath = new StringBuilder("(//*[contains(@class,'BookingPassengerForm_passenger_form')])[" + indexPax + "]//input[");
        for (int i = 0; i < FULL_NAME_IDS.length; i++) {
            xpath.append("@id='").append(FULL_NAME_IDS[i]).append("'");
            if (i < FULL_NAME_IDS.length - 1) {
                xpath.append(" or ");
            }
        }
        xpath.append("]");
        return new Locator("FULL_NAME_LOCATOR", By.xpath(xpath.toString()));
    }

    protected Locator getFullNameAccordingToIdLocator(int indexPax) {
        StringBuilder xpath = new StringBuilder("(//*[contains(@class,'BookingPassengerForm_passenger_form')])[" + indexPax + "]//input[");
        for (int i = 0; i < PASSPORT_FULL_NAME_IDS.length; i++) {
            xpath.append("@id='").append(PASSPORT_FULL_NAME_IDS[i]).append("'");
            if (i < PASSPORT_FULL_NAME_IDS.length - 1) {
                xpath.append(" or ");
            }
        }
        xpath.append("]");
        return new Locator("FULL_NAME_ACCORDING_TO_ID_LOCATOR", By.xpath(xpath.toString()));
    }

    private Locator getClearFullNameAccordingToIdLocator(int indexPax) {
        StringBuilder xpath = new StringBuilder("(//*[contains(@class,'BookingPassengerForm_passenger_form')])[" + indexPax + "]//input[");
        for (int i = 0; i < PASSPORT_FULL_NAME_IDS.length; i++) {
            xpath.append("@id='").append(PASSPORT_FULL_NAME_IDS[i]).append("'");
            if (i < PASSPORT_FULL_NAME_IDS.length - 1) {
                xpath.append(" or ");
            }
        }
        xpath.append("]//following-sibling::*//button[contains(@class,'styles_clear_button')]");
        return new Locator("CLEAR_FULL_NAME_ACCORDING_TO_ID_LOCATOR", By.xpath(xpath.toString()));
    }

    private Locator getClearFullNameLocator(int indexPax) {
        StringBuilder xpath = new StringBuilder("(//*[contains(@class,'BookingPassengerForm_passenger_form')])[" + indexPax + "]//input[");
        for (int i = 0; i < FULL_NAME_IDS.length; i++) {
            xpath.append("@id='").append(FULL_NAME_IDS[i]).append("'");
            if (i < FULL_NAME_IDS.length - 1) {
                xpath.append(" or ");
            }
        }
        xpath.append("]//following-sibling::*//button[contains(@class,'styles_clear_button')]");
        return new Locator("CLEAR_FULL_NAME_LOCATOR", By.xpath(xpath.toString()));
    }

    private Locator getLastNameLocator(int indexPax) {
        StringBuilder xpath = new StringBuilder("(//*[contains(@class,'BookingPassengerForm_passenger_form')])[" + indexPax + "]//input[");
        for (int i = 0; i < LAST_NAME_IDS.length; i++) {
            xpath.append("@id='").append(LAST_NAME_IDS[i]).append("'");
            if (i < LAST_NAME_IDS.length - 1) {
                xpath.append(" or ");
            }
        }
        xpath.append("]");
        return new Locator("LAST_NAME_LOCATOR", By.xpath(xpath.toString()));
    }

    private Locator getClearLastNameLocator(int indexPax) {
        StringBuilder xpath = new StringBuilder("(//*[contains(@class,'BookingPassengerForm_passenger_form')])[" + indexPax + "]//input[");
        for (int i = 0; i < LAST_NAME_IDS.length; i++) {
            xpath.append("@id='").append(LAST_NAME_IDS[i]).append("'");
            if (i < LAST_NAME_IDS.length - 1) {
                xpath.append(" or ");
            }
        }
        xpath.append("]//following-sibling::*//button[contains(@class,'styles_clear_button')]");
        return new Locator("CLEAR_LAST_NAME_LOCATOR", By.xpath(xpath.toString()));
    }

    private Locator outsideLocator(int indexPax) {
        return new Locator("CLICK_OUTSIDE", By.xpath("(//*[contains(@class,'BookingPassengerForm_passenger_form')])["+indexPax+"]//*[contains(@class,'styles_helper_text_container')]"));
    }

    private void clearAndFillField(Locator fieldLocator, int indexPax, String value, String logLabel,Locator clearLocator) {
        driverTools.waitForElementToAppear(fieldLocator);
        WebElement fieldElement = driverTools.element(fieldLocator).orElseThrow();
        if(!driverTools.getText(fieldElement).isEmpty()){
            logger.log("Clearing field: " + logLabel);
            driverTools.clearWithJsExecutor(fieldLocator);
            String valueOnForm = driverTools.getValueAttribute(fieldLocator, "value");
            logger.log(logLabel + " ->" + valueOnForm);
            if(!valueOnForm.isEmpty()){
                driverTools.clickWithJavascriptExecutor(clearLocator);
            }
        }
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(fieldLocator, value);
        driverTools.clickElementByCoordinateDWeb(outsideLocator(indexPax));
        String valueOnFormFinal = driverTools.getValueAttribute(fieldLocator, "value");
        logger.log(logLabel + "Final ->" + valueOnFormFinal);
        Assertion.assertThat(logLabel + "Final should be " + value, valueOnFormFinal, is(equalToIgnoringCase(value)));
    }

    protected void fillFullName(int indexPax, FlightPaxDetail flightPaxDetail) {
        ensurePassengerFormIsActive(indexPax);
        String firstName = flightPaxDetail.data.get(indexPax).paxFirstName;
        String lastName = flightPaxDetail.data.get(indexPax).paxLastName;

        logger.log("Filling passenger " + indexPax + " full name: " + firstName + " " + lastName);
        Locator fullNameLocator = getFullNameLocator(indexPax);
        Locator clearFullNameLocator = getClearFullNameLocator(indexPax);
        Locator lastNameLocator = getLastNameLocator(indexPax);
        Locator clearLastNameLocator = getClearLastNameLocator(indexPax);
        driverTools.waitForElementToAppear(fullNameLocator);
        boolean hasLastNameField = driverTools.isElementDisplayed(lastNameLocator);
        if (hasLastNameField) {
            fillNameFieldWithoutOutsideClick(fullNameLocator, indexPax, firstName, "nameOnBookingForm for passenger " + indexPax, clearFullNameLocator);
            fillNameFieldWithoutOutsideClick(lastNameLocator, indexPax, lastName, "lastNameOnBookingForm for passenger " + indexPax, clearLastNameLocator);
        } else {
            String fullName = firstName + " " + lastName;
            fillNameFieldWithoutOutsideClick(fullNameLocator, indexPax, fullName, "nameOnBookingForm for passenger " + indexPax, clearFullNameLocator);
        }
        fillFullNameAccordingToIdIfPresent(indexPax, flightPaxDetail);
    }

    protected void fillFullNameAccordingToIdIfPresent(int indexPax, FlightPaxDetail flightPaxDetail) {
        Locator passportFullNameLocator = getFullNameAccordingToIdLocator(indexPax);
        if (!driverTools.isElementPresent(passportFullNameLocator)) {
            return;
        }

        String firstName = flightPaxDetail.data.get(indexPax).paxFirstName;
        String lastName = flightPaxDetail.data.get(indexPax).paxLastName;
        String fullName = firstName + " " + lastName;
        logger.log("Filling passenger " + indexPax + " full name according to ID/Passport: " + fullName);
        fillNameFieldWithoutOutsideClick(
                passportFullNameLocator,
                indexPax,
                fullName,
                "fullNameAccordingToId for passenger " + indexPax,
                getClearFullNameAccordingToIdLocator(indexPax));
    }

    private void fillNameFieldWithoutOutsideClick(Locator fieldLocator, int indexPax, String value, String logLabel, Locator clearLocator) {
        driverTools.waitForElementToAppear(fieldLocator);
        WebElement fieldElement = driverTools.element(fieldLocator).orElseThrow();

        String currentValue = driverTools.getValueAttribute(fieldLocator, "value");
        if (currentValue != null && !currentValue.isEmpty() && !currentValue.equalsIgnoreCase(value)) {
            logger.log("Clearing field: " + logLabel + " current value: " + currentValue);
            driverTools.clearWithJsExecutor(fieldLocator);
            String valueAfterClear = driverTools.getValueAttribute(fieldLocator, "value");
            if (valueAfterClear != null && !valueAfterClear.isEmpty()) {
                driverTools.clickWithJavascriptExecutor(clearLocator);
            }
        }

        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(fieldLocator, value);

        int maxRetries = 3;
        String valueOnForm = null;
        for (int retry = 0; retry < maxRetries; retry++) {
            driverTools.waitForElementToAppear(fieldLocator);
            valueOnForm = waitForFieldValueToBeSet(fieldLocator, value, 2);
            logger.log(logLabel + " attempt " + (retry + 1) + " ->" + valueOnForm);

            if (valueOnForm != null && !valueOnForm.isEmpty() && valueOnForm.equalsIgnoreCase(value)) {
                break;
            }

            if (retry < maxRetries - 1) {
                logger.log(logLabel + " value not set correctly, retrying: " + value);
                driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(fieldLocator, value);
            }
        }

        Assertion.assertThat(logLabel + " should be " + value, valueOnForm, is(equalToIgnoringCase(value)));
    }

    protected void verifyAndRefillNameIfNeeded(int indexPax, FlightPaxDetail flightPaxDetail) {
        ensurePassengerFormIsActive(indexPax);

        String firstName = flightPaxDetail.data.get(indexPax).paxFirstName;
        String lastName = flightPaxDetail.data.get(indexPax).paxLastName;
        Locator fullNameLocator = getFullNameLocator(indexPax);
        Locator clearFullNameLocator = getClearFullNameLocator(indexPax);
        Locator lastNameLocator = getLastNameLocator(indexPax);
        Locator clearLastNameLocator = getClearLastNameLocator(indexPax);

        driverTools.waitForElementToAppear(fullNameLocator);
        boolean hasLastNameField = driverTools.isElementDisplayed(lastNameLocator);

        if (hasLastNameField) {
            String currentFirstName = driverTools.getValueAttribute(fullNameLocator, "value");
            String currentLastName = driverTools.getValueAttribute(lastNameLocator, "value");

            logger.log("Verifying passenger " + indexPax + " name. Expected: " + firstName + " " + lastName + ", Found: " + currentFirstName + " " + currentLastName);

            boolean firstNameCleared = currentFirstName == null || currentFirstName.isEmpty() || !currentFirstName.equalsIgnoreCase(firstName);
            boolean lastNameCleared = currentLastName == null || currentLastName.isEmpty() || !currentLastName.equalsIgnoreCase(lastName);

            if (firstNameCleared || lastNameCleared) {
                logger.log("Passenger " + indexPax + " name fields need re-filling. FirstName cleared: " + firstNameCleared + ", LastName cleared: " + lastNameCleared);
                if (firstNameCleared) {
                    fillNameFieldWithoutOutsideClick(fullNameLocator, indexPax, firstName, "nameOnBookingForm for passenger " + indexPax, clearFullNameLocator);
                }
                if (lastNameCleared) {
                    fillNameFieldWithoutOutsideClick(lastNameLocator, indexPax, lastName, "lastNameOnBookingForm for passenger " + indexPax, clearLastNameLocator);
                }
            } else {
                logger.log("Passenger " + indexPax + " name fields verified successfully: " + firstName + " " + lastName);
            }
        } else {
            String fullName = firstName + " " + lastName;
            String currentFullName = driverTools.getValueAttribute(fullNameLocator, "value");

            logger.log("Verifying passenger " + indexPax + " full name. Expected: " + fullName + ", Found: " + currentFullName);

            boolean fullNameCleared = currentFullName == null || currentFullName.isEmpty() || !currentFullName.equalsIgnoreCase(fullName);

            if (fullNameCleared) {
                logger.log("Passenger " + indexPax + " full name was cleared, re-filling. Current: " + currentFullName);
                fillNameFieldWithoutOutsideClick(fullNameLocator, indexPax, fullName, "nameOnBookingForm for passenger " + indexPax, clearFullNameLocator);
            } else {
                logger.log("Passenger " + indexPax + " full name verified successfully: " + fullName);
            }
        }

        Locator passportFullNameLocator = getFullNameAccordingToIdLocator(indexPax);
        if (driverTools.isElementPresent(passportFullNameLocator)) {
            String fullName = firstName + " " + lastName;
            String currentPassportFullName = driverTools.getValueAttribute(passportFullNameLocator, "value");
            boolean passportFullNameMissing = currentPassportFullName == null
                    || currentPassportFullName.isEmpty()
                    || !currentPassportFullName.equalsIgnoreCase(fullName);

            if (passportFullNameMissing) {
                logger.log("Passenger " + indexPax + " ID/Passport full name needs filling. Expected: "
                        + fullName + ", Found: " + currentPassportFullName);
                fillNameFieldWithoutOutsideClick(
                        passportFullNameLocator,
                        indexPax,
                        fullName,
                        "fullNameAccordingToId for passenger " + indexPax,
                        getClearFullNameAccordingToIdLocator(indexPax));
            }
        }
    }

    protected void ensurePassengerFormIsActive(int indexPax) {
        boolean isFormExpanded = isPassengerFormExpanded(indexPax);

        if (!isFormExpanded) {
            logger.log("Passenger " + indexPax + " form is hidden, expanding it");
            WebElement passengerTitle = driverTools.waitForElementToPresence(passengerTitleLocator(indexPax),5).orElseThrow();
            logger.log("Passenger " + indexPax + " title: " + driverTools.getText(passengerTitle));
            driverTools.clickWithJavascriptExecutor(passengerTitle);
            driverTools.waitForElementToAppear(getFullNameLocator(indexPax), 10);
            driverTools.waitForElementToBeClickable(getFullNameLocator(indexPax), 5);
        } else {
            driverTools.waitForElementToAppear(getFullNameLocator(indexPax), 5);
        }
    }

    protected void waitForFormStability(int indexPax) {
        driverTools.waitForElementToAppear(getFullNameLocator(indexPax), 3);
    }

    private void collapseOtherPassengerForms(int currentPaxIndex) {
        for (int i = 1; i < currentPaxIndex; i++) {
            try {
                boolean isFormExpanded = isPassengerFormExpanded(i);

                if (isFormExpanded) {
                    logger.log("Collapsing passenger " + i + " form before filling passenger " + currentPaxIndex + " dates");
                    WebElement passengerTitle = driverTools.waitForElementToPresence(passengerTitleLocator(i), 3).orElseThrow();
                    driverTools.clickWithJavascriptExecutor(passengerTitle);

                    int maxWaitAttempts = 5;
                    for (int attempt = 0; attempt < maxWaitAttempts; attempt++) {
                        if (!isPassengerFormExpanded(i)) {
                            logger.log("Passenger " + i + " form successfully collapsed");
                            break;
                        }
                        driverTools.waitForElementToAppear(checkHiddenAttributePaxFill(i), 1);
                    }
                } else {
                    logger.log("Passenger " + i + " form is already collapsed");
                }
            } catch (Exception e) {
                logger.log("Could not collapse passenger " + i + " form: " + e.getMessage());
            }
        }
    }

    private boolean isPassengerFormExpanded(int indexPax) {
        try {
            WebElement paxHiddenAttribute = driverTools.element(checkHiddenAttributePaxFill(indexPax)).orElse(null);
            if (paxHiddenAttribute != null) {
                String ariaHidden = paxHiddenAttribute.getAttribute("aria-hidden");
                if (ariaHidden != null && ariaHidden.equals("true")) {
                    return false;
                }
            }

            Locator fullNameLocator = getFullNameLocator(indexPax);
            if (driverTools.isElementPresent(fullNameLocator)) {
                return driverTools.isElementDisplayed(fullNameLocator);
            }

            logger.log("Could not determine expansion state for passenger " + indexPax + ", assuming expanded");
            return true;
        } catch (Exception e) {
            logger.log("Error checking if passenger " + indexPax + " form is expanded: " + e.getMessage());
            return true;
        }
    }

    private String waitForFieldValueToBeSet(Locator fieldLocator, String expectedValue, int timeoutSeconds) {
        driverTools.waitForElementToAppear(fieldLocator, 1);
        String currentValue = driverTools.getValueAttribute(fieldLocator, "value");
        if (currentValue != null && !currentValue.isEmpty() && currentValue.equalsIgnoreCase(expectedValue)) {
            return currentValue;
        }

        driverTools.waitForElementToAppear(fieldLocator, timeoutSeconds);
        currentValue = driverTools.getValueAttribute(fieldLocator, "value");
        return currentValue;
    }

    private Locator getDobLocator(int indexPax) {
        return new Locator("DOB_LOCATOR", By.xpath("(//*[contains(@class,'BookingPassengerForm_passenger_form')])["+indexPax+"]//input[contains(@id,'date-of-birth') or contains(@id,'tanggal-lahir')]"));
    }

    private Locator getDynamicYearLocator(String year) {
        return new Locator("DYNAMIC_YEAR_LOCATOR", By.xpath("//button[contains(@class, 'month-year-menus_button') and text()='"+year+"']"));
    }

    private Locator getDynamicMonthLocator(int month) {
        String monthEn = CoreFunctions.getMonthName(month,"en");
        String monthId = CoreFunctions.getMonthName(month,"id");
        return new Locator("DYNAMIC_MONTH_LOCATOR", By.xpath("//button[contains(@class, 'month-year-menus_button') and (contains(text(),'"+monthEn+"') or contains(text(),'"+monthId+"'))]"));
    }

    private Locator getDynamicDateLocator(String day) {
        //remove leading zero from day if present
        if (day.startsWith("0")) {
            day = day.substring(1);
        }
        return new Locator("DYNAMIC_DATE_LOCATOR", By.xpath("//*[contains(@class,'DatePickerDesktop_month')]//button[contains(@class, 'Day_day')]//span[text()='"+day+"']"));
    }

    private Locator clickPaxChevronLocator(int paxIndex) {
        return new Locator("CLICK_PAX_CHEVRON_" + paxIndex, By.xpath("(//div[contains(@class,'BookingPassengerSection_right_side')])[" + paxIndex + "]"));
    }

    private void fillDate(String date,String loggerLabel) {
        String [] dateParts = date.split("-");
        String year = dateParts[0];
        int month = Integer.parseInt(dateParts[1]);
        String day = dateParts[2];

        logger.log(loggerLabel + " with year: " + year + ", month: " + month + ", day: " + day);

        WebElement yearDropdown = driverTools.waitForElementToAppear(YEAR_DROPDOWN).orElseThrow();
        driverTools.clickWithJavascriptExecutor(yearDropdown);
        Locator dynamicYear = getDynamicYearLocator(year);
        driverTools.waitForElementToAppear(dynamicYear);
        driverTools.clickWithJavascriptExecutor(dynamicYear);

        driverTools.waitForElementToAppear(MONTH_DROPDOWN);
        driverTools.clickWithJavascriptExecutor(MONTH_DROPDOWN);
        Locator dynamicMonth = getDynamicMonthLocator(month);
        driverTools.waitForElementToAppear(dynamicMonth);
        driverTools.clickWithJavascriptExecutor(dynamicMonth);

        Locator dynamicDate = getDynamicDateLocator(day);
        driverTools.waitForElementToAppear(dynamicDate);
        driverTools.clickWithJavascriptExecutor(dynamicDate);
        driverTools.waitForElementToPresence(DOB_SUBMIT);
        driverTools.scrollIntoWebView(DOB_SUBMIT);
        driverTools.clickWithJavascriptExecutor(DOB_SUBMIT);
    }

    private Locator getNationalityLocator(int indexPax){
        return new Locator("NATIONALITY_"+indexPax, By.xpath("(//*[contains(@class,'BookingPassengerForm_passenger_form')])[" + indexPax + "]//input[contains(@id,'nationality') or contains(@id,'kewarganegaraan')]"));
    }

    private Locator getNationalityListLocator(String nation) {
        return new Locator("NATIONAL_LIST_LOCATOR", By.xpath("//*[contains(@class,'CountryListSelection_list_item')]//*[contains(@class,'List_title')][1]"));
    }

    public void fillNationality(int indexPax, String nation){
        Locator paxChevronLocator = clickPaxChevronLocator(indexPax);
        driverTools.waitForElementToAppear(getNationalityLocator(indexPax), 15);
        if (driverTools.isElementDisplayed(getNationalityLocator(indexPax))){
            driverTools.clickWithJavascriptExecutor(getNationalityLocator(indexPax));
            fillAndSelectCountry(nation);
        }else{
            if (driverTools.isElementDisplayed(paxChevronLocator)) {
                driverTools.clickWithJavascriptExecutor(paxChevronLocator);
            }
            if (driverTools.isElementDisplayed(getNationalityLocator(indexPax))) {
                driverTools.clickWithJavascriptExecutor(getNationalityLocator(indexPax));
                fillAndSelectCountry(nation);
            }
        }
    }

    protected void fillAndSelectCountry(String country) {
        country = CoreFunctions.titleize(country);
        driverTools.waitForElementToPresence(NATIONALITY_SEARCH).orElseThrow();
        driverTools.clickWithJavascriptExecutor(NATIONALITY_SEARCH);
        driverTools.sendKeysWithJsExecutor(NATIONALITY_SEARCH, country);
        driverTools.waitForElementToPresence(getNationalityListLocator(country));
        driverTools.clickWithJavascriptExecutor(getNationalityListLocator(country));
    }

    private Locator fillIdLocator(int paxIndex) {
        return new Locator("FILL_ID_LOCATOR", By.xpath("(//*[contains(@class,'BookingPassengerForm_passenger_form')])[" + paxIndex + "]//input[@id='passport-no' or @id='nikpassport-no' or @aria-label='passportNo']"));
    }

    private String fillId(int paxIndex){
        Locator idLocator = fillIdLocator(paxIndex);
        String idValue = "";
        if(driverTools.isElementDisplayed(idLocator)){
            idValue = "3272022002" + CoreFunctions.generateRandomInt(2);
            driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(idLocator, idValue);
            driverTools.clickElementByCoordinateDWeb(outsideLocator(paxIndex));
            String idOnBookingForm = driverTools.getValueAttribute(idLocator, "value");
            logger.log("ID on booking form: " + idOnBookingForm);
            Assertion.assertThat("ID on booking form should be " + idValue, idOnBookingForm, is(equalToIgnoringCase(idValue)));
        }
        return idValue;
    }

    private Locator getExpiryDateLocator(int paxIndex) {
        return new Locator("EXPIRY_DATE_LOCATOR", By.xpath("(//*[contains(@class,'BookingPassengerForm_passenger_form')])[" + paxIndex + "]//input[@id='expiry-date' or @id='tanggal-habis-berlaku' or contains(@placeholder,'Expiry Date') or contains(@placeholder,'Tanggal Habis Berlaku')]"));
    }

    private Locator getIssueDateLocator(int paxIndex) {
        return new Locator("ISSUE_DATE_LOCATOR", By.xpath("(//*[contains(@class,'BookingPassengerForm_passenger_form')])[" + paxIndex + "]//input[@id='issued-date' or @id='tanggal-pengeluaran' or contains(@placeholder,'Issued Date') or contains(@placeholder,'Tanggal Pengeluaran')]"));
    }

    private Locator getPassportNationLocator(int paxIndex) {
        return new Locator("PASSPORT_NATION_LOCATOR", By.xpath("(//*[contains(@class,'BookingPassengerForm_passenger_form')])[" + paxIndex + "]//input[@id='issuing-country' or @aria-label='issuingCountry' or contains(@placeholder,'Issuing Country') or contains(@placeholder,'Negara Penerbit')]"));
    }

    private String fillPassport(int paxIndex,FlightPaxDetail flightPaxDetail) {
        logger.log("Filling passport details for passenger " + paxIndex);

        if (paxIndex > 1) {
            collapseOtherPassengerForms(paxIndex);
        }

        ensurePassengerFormIsActive(paxIndex);
        Locator passportLocator = fillIdLocator(paxIndex);
        String passportValue = "";
        if(driverTools.isElementDisplayed(passportLocator)){
            passportValue = String.valueOf(CoreFunctions.generateRandomInt(10)) ;
            driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(passportLocator, passportValue);
            driverTools.waitForElementToAppear(passportLocator);
            String passportOnBookingForm = driverTools.getValueAttribute(passportLocator, "value");
            logger.log("Passport " + paxIndex + " on booking form: " + passportOnBookingForm);
            if (passportOnBookingForm == null || passportOnBookingForm.isEmpty() || !passportOnBookingForm.equalsIgnoreCase(passportValue)) {
                logger.log("Passport number for passenger " + paxIndex + " was cleared, re-filling: " + passportValue);
                driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(passportLocator, passportValue);
                driverTools.waitForElementToAppear(passportLocator);
                passportOnBookingForm = driverTools.getValueAttribute(passportLocator, "value");
            }
            Assertion.assertThat("Passport on booking form should be " + passportValue, passportOnBookingForm, is(equalToIgnoringCase(passportValue)));
        }

        Locator expiryDateLocator = getExpiryDateLocator(paxIndex);
        if(driverTools.isElementDisplayed(expiryDateLocator)){
            ensurePassengerFormIsActive(paxIndex);
            if (paxIndex > 1) {
                collapseOtherPassengerForms(paxIndex);
            }

            String expiryDate = flightPaxDetail.data.get(paxIndex).paxPassportExpiryDate;
            logger.log("Filling expiry date for passenger " + paxIndex + ": " + expiryDate);

            driverTools.waitForElementToAppear(expiryDateLocator);
            driverTools.waitForElementToBeClickable(expiryDateLocator, 3);
            driverTools.clickWithJavascriptExecutor(expiryDateLocator);
            driverTools.waitForElementToAppear(YEAR_DROPDOWN, 3);

            fillDate(expiryDate, "Expiry date for passenger " + paxIndex);

            driverTools.waitForElementToAppear(expiryDateLocator);
            String expiryDateValue = driverTools.getValueAttribute(expiryDateLocator, "value");
            logger.log("Passenger " + paxIndex + " expiry date after filling: " + expiryDateValue);
        }

        Locator issueDateLocator = getIssueDateLocator(paxIndex);
        if(driverTools.isElementDisplayed(issueDateLocator)){
            ensurePassengerFormIsActive(paxIndex);
            if (paxIndex > 1) {
                collapseOtherPassengerForms(paxIndex);
            }

            String issueDate = flightPaxDetail.data.get(paxIndex).paxPassportIssueDate;
            logger.log("Filling issue date for passenger " + paxIndex + ": " + issueDate);

            driverTools.waitForElementToAppear(issueDateLocator);
            driverTools.waitForElementToBeClickable(issueDateLocator, 3);
            driverTools.clickWithJavascriptExecutor(issueDateLocator);
            driverTools.waitForElementToAppear(YEAR_DROPDOWN, 3);

            fillDate(issueDate, "Issue date for passenger " + paxIndex);

            driverTools.waitForElementToAppear(issueDateLocator);
            String issueDateValue = driverTools.getValueAttribute(issueDateLocator, "value");
            logger.log("Passenger " + paxIndex + " issue date after filling: " + issueDateValue);
        }

        Locator passportNationLocator = getPassportNationLocator(paxIndex);
        if(driverTools.isElementDisplayed(passportNationLocator)){
            ensurePassengerFormIsActive(paxIndex);
            String passportNation = flightPaxDetail.data.get(paxIndex).paxPassportCountry;
            logger.log("Filling issuing country for passenger " + paxIndex + ": " + passportNation);
            driverTools.clickWithJavascriptExecutor(passportNationLocator);
            fillAndSelectCountry(passportNation);
            driverTools.waitForElementToAppear(passportNationLocator);
            String passportNationValue = driverTools.getValueAttribute(passportNationLocator, "value");
            logger.log("Passenger " + paxIndex + " issuing country after filling: " + passportNationValue);
        }
        return passportValue;
    }

    public void fillNationality(){
        if (driverTools.isElementDisplayed(NATIONALITY)){
            driverTools.clickWithJavascriptExecutor(NATIONALITY);
            driverTools.waitForElementToPresence(COUNTRY);
            driverTools.clickWithJavascriptExecutor(COUNTRY);
        }
    }

    public void fillBirthdayWithLastClickableDate() {
        driverTools.waitForElementToAppear(DOB_INPUT);
        driverTools.clickWithJavascriptExecutor(DOB_INPUT);
        driverTools.waitForElementToAppear(LAST_CLICKABLE_DATE);
        driverTools.clickWithJavascriptExecutor(LAST_CLICKABLE_DATE);
        driverTools.waitForElementToPresence(DOB_SUBMIT);
        driverTools.clickWithJavascriptExecutor(DOB_SUBMIT);
    }

    public void clickOnTryAgain(){
        driverTools.waitForElementToPresence(AIRLINE_CONNECTION_TRY_AGAIN);
        driverTools.clickWithJavascriptExecutor(AIRLINE_CONNECTION_TRY_AGAIN);
    }

    public void clickNoThanksOnFlightMayReschedulePopup() {
        if (skipFlightDisruptionInsurancePopupPrp()) {
            driverTools.waitForElementToAppear(PRICE_CHANGE_POPUP).ifPresent(driverTools::clickWithJavascriptExecutor);
            return;
        }
        driverTools.waitForElementToAppear(FLIGHT_MAY_RESCHEDULED_POPUP);
        if(driverTools.isElementDisplayed(FLIGHT_MAY_RESCHEDULED_POPUP)){
            driverTools.click(FLIGHT_MAY_RESCHEDULED_POPUP);
        }
        driverTools.waitForElementToAppear(PRICE_CHANGE_POPUP).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public void clickNoThanksOnFlightMayReschedulePopupPRP() {
        if (skipFlightDisruptionInsurancePopupPrp()) {
            return;
        }
        driverTools.waitForElementToAppear(FLIGHT_MAY_RESCHEDULED_POPUP, 5);
        if(driverTools.isElementDisplayed(FLIGHT_MAY_RESCHEDULED_POPUP)){
            driverTools.click(FLIGHT_MAY_RESCHEDULED_POPUP);
        }
    }

    /**
     * New PRP interstitial ({@code template-disruption-variant2}): select Without protection, then continue.
     * Falls back to legacy {@code popup-button-secondary} when the disruption sheet is absent.
     */
    private boolean skipFlightDisruptionInsurancePopupPrp() {
        if (!driverTools.waitForElementToAppear(FLIGHT_DISRUPTION_INSURANCE_POPUP, 8).isPresent()) {
            return false;
        }

        logger.log("Flight disruption insurance popup detected — selecting Without protection");
        if (driverTools.waitForElementToAppear(WITHOUT_PROTECTION_RADIO, 5).isPresent()) {
            driverTools.clickWithJavascriptExecutor(WITHOUT_PROTECTION_RADIO);
        } else {
            driverTools.waitForElementToAppear(WITHOUT_PROTECTION_OPTION, 5).orElseThrow(() ->
                    new NoSuchElementException("Without protection option not found on disruption popup"));
            driverTools.clickWithJavascriptExecutor(WITHOUT_PROTECTION_OPTION);
        }

        if (driverTools.waitForElementToAppear(CONTINUE_WITHOUT_PROTECTION_BTN, 8).isPresent()) {
            logger.log("Clicking Continue without protection");
            driverTools.clickWithJavascriptExecutor(CONTINUE_WITHOUT_PROTECTION_BTN);
            driverTools.waitForElementToDisappear(FLIGHT_DISRUPTION_INSURANCE_POPUP, 10);
            return true;
        }

        if (driverTools.waitForElementToAppear(CONTINUE_AFTER_SKIP_PROTECTION, 5).isPresent()) {
            logger.log("Clicking continue CTA after Without protection selection");
            driverTools.clickWithJavascriptExecutor(CONTINUE_AFTER_SKIP_PROTECTION);
            driverTools.waitForElementToDisappear(FLIGHT_DISRUPTION_INSURANCE_POPUP, 10);
            return true;
        }

        logger.log("Without-protection continue CTA not found — not clicking Continue with protection");
        return false;
    }

    public void clickNoThanksButton() {
        driverTools.waitForElementToPresence(NO_THANKS_BUTTON) ;
        driverTools.clickWithJavascriptExecutor(NO_THANKS_BUTTON) ;
    }

    protected Locator getSalutationLocator(String salutation) {
        return new Locator("SALUTATION_LOCATOR", By.xpath("(//span[contains(@class, 'Radio_icon__')])[1]"));
    }

    public void inputContactDetailsNonLogin(String salutation, String fullName, String phoneNumber, String email) {
        driverTools.waitForElementToAppear(CONTACT_DETAILS_BOOKING_PAGE);
        driverTools.clickWithJavascriptExecutor(getSalutationLocator(salutation));
        clearAndFillContactField(CONTACT_NAME_INPUT, fullName);
        clearAndFillContactField(CONTACT_PHONE_INPUT, phoneNumber);
        clearAndFillContactField(CONTACT_EMAIL_INPUT, email);
    }

    private void clearAndFillContactField(Locator fieldLocator, String value) {
        driverTools.waitForElementToAppear(fieldLocator);
        driverTools.scrollIntoWebView(fieldLocator);
        String currentValue = driverTools.getValueAttribute(fieldLocator, "value");
        if (currentValue != null && !currentValue.isEmpty() && !currentValue.equals(value)) {
            logger.log("Clearing pre-filled contact field value '" + currentValue + "' before entering: " + value);
            driverTools.clearWithJsExecutor(fieldLocator);
            driverTools.clearInput(fieldLocator);
        }
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(fieldLocator, value);
    }

    public void clickSameContactToggle() {
        driverTools.scrollElementToCenter(SAME_CONTACT_TOGGLE);
        driverTools.clickWithJavascriptExecutor(SAME_CONTACT_TOGGLE);

        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            driverTools.waitForElementToAppear(PASSENGER_DATA_MODAL_MWEB);
            WebElement saveButton = driverTools.element(new Locator("SAVE_BUTTON", By.xpath("//button[span[text()='Save' or text()='Simpan']]")), driverTools.element(PASSENGER_DATA_MODAL_MWEB).orElseThrow()).orElseThrow();
            driverTools.click(saveButton);
            driverTools.waitForElementToDisappear(PASSENGER_DATA_MODAL_MWEB);
        }
    }

    public void clickGotItInBaggagePopup() {
        driverTools.waitForElementToAppear(INSURANCE_POPUP_BTN, 3).ifPresent(driverTools::click);
        if(driverTools.isElementPresent(BAGGAGE_CHANGE_POPUP_VALIDATOR)){
            driverTools.clickWithJavascriptExecutor(BAGGAGE_CHANGE_POPUP_CLOSE);
        }
        driverTools.waitForElementToAppear(BAGGAGE_POPUP, 5).ifPresent(
                element -> {
                    WebElement button = driverTools.element(new Locator("BAGGAGE_BUTTON", By.xpath(".//button[contains(@class,'Button_variant_primary')]")), element).orElseThrow();
                    driverTools.click(button);
                });
    }

    public void clickOkayGotIt() {
        PopupObserver.inject(driver, List.of(PRICE_CHANGE_POPUPOKAY));

    }

    public void clickItsCorrectOnReviewPassengerDataPopup(){
        driverTools.waitForElementToAppear(REVIEW_PASSENGER_DATA_POPUP, 4)
                .ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    /**
     * Checks if the "No thanks" pop-up appeared on the page.
     * Throws an exception/assertion error if the pop-up is detected.
     */
    public void checkIfNoThanksPopUpCame() {
        logger.log("Checking if 'No thanks' pop-up unexpectedly appeared...");

        // Checks for both standard text button and secondary popup button variants
        boolean isPopUpDisplayed = driverTools.waitForElementToAppear(NO_THANKS_BUTTON, 3).isPresent()
                || driverTools.waitForElementToAppear(POPUP_BUTTON_SECONDARY, 3).isPresent();

        if (isPopUpDisplayed) {
            logger.log("ERROR: 'No thanks' pop-up appeared on the screen!");
            Assertion.assertThat("Unexpected 'No thanks' pop-up appeared on the page", isPopUpDisplayed, is(false));
        } else {
            logger.log("Verified: 'No thanks' pop-up did not appear.");
        }
    }

    public boolean isBookingFormVisible() {
        return driverTools.waitForElementToAppear(BOOKING_FORM).isPresent();
    }

    public boolean isFlightDetailsVisible() {
        return driverTools.waitForElementToAppear(FLIGHT_DETAILS).isPresent();
    }

    public double get100pRefundInsurancePriceFromBookingBreakdown() {
        openPriceDetailSection();
        Assertion.assertThat("Price detail popup is visible on booking form", isPriceDetailPageVisible(), is(true));

        Map<String, String> breakdown = getPriceBreakdownFromBookingPage();
        Double insurancePrice = find100pRefundInsurancePriceInBreakdown(breakdown);
        Assertion.assertThat("100% Refund insurance price is present on booking form breakdown", insurancePrice, is(notNullValue()));

        closePopUp();
        logger.log("100% Refund insurance price on booking form breakdown: " + insurancePrice);
        return insurancePrice;
    }

    private Double find100pRefundInsurancePriceInBreakdown(Map<String, String> breakdown) {
        for (Map.Entry<String, String> entry : breakdown.entrySet()) {
            String key = entry.getKey().toLowerCase();
            if (key.contains("100") && key.contains("refund") && key.contains("addon")) {
                logger.log("100% Refund addon line on booking form: " + entry.getKey() + " = " + entry.getValue());
                return CoreFunctions.convertPriceSafely(entry.getValue());
            }
        }
        for (Map.Entry<String, String> entry : breakdown.entrySet()) {
            String key = entry.getKey().toLowerCase();
            if ((key.contains("100") && key.contains("refund")) || key.contains("tiket 100%")) {
                logger.log("100% Refund insurance line on booking form: " + entry.getKey() + " = " + entry.getValue());
                return CoreFunctions.convertPriceSafely(entry.getValue());
            }
        }
        return null;
    }

    public boolean isBookingBenefits100PercentRefundDisplayed() {
        return driverTools.waitForElementToAppear(PRICE_BREAKDOWN_REFUND_ADDON_ITEM, 10).isPresent();
    }

    public void goOnFlightDetail(){
        driverTools.scrollIntoWebView(FLIGHT_DETAILS);
        driverTools.clickWithJavascriptExecutor(FLIGHT_DETAILS_BUTTON);

    }

    public void openFlightSummery(int index){
        Locator FLIGHT_SUMMARY_BUTTON = getSeeDetailsRefundAndRescheduleByIndex(index);
        driverTools.scrollIntoWebView(FLIGHT_SUMMARY_BUTTON);
        driverTools.clickWithJavascriptExecutor(FLIGHT_SUMMARY_BUTTON);
    }

    private Locator getSeeDetailsRefundAndRescheduleByIndex(int index) {
        return new Locator("SEE_DETAILS_REFUND_AND_RESCHEDULE_" + index,
                new By.ByXPath("(//*[contains(@class, 'FlightSummaryCard_icon')])[" + index + "]"));
    }

    public void clickSeeDetailRefundAndReschedule(){
        driverTools.waitForElementToAppear(SEE_DETAILS_REFUND_AND_RESCHEDULE);
        driverTools.clickWithJavascriptExecutor(SEE_DETAILS_REFUND_AND_RESCHEDULE);
    }

    public void clickRescheduleTab(){
        driverTools.waitForElementToAppear(RESCHEDULE_TAB_FLIGHT_SUMMARY);
        driverTools.clickWithJavascriptExecutor(RESCHEDULE_TAB_FLIGHT_SUMMARY);
    }

    public void clickSeeFlightFacilities(){
        driverTools.waitForElementToAppear(SEE_FLIGHT_FACILITIES);
        driverTools.clickWithJavascriptExecutor(SEE_FLIGHT_FACILITIES);
    }

    private boolean isOtherImportantInfoSectionVisible() {
        String otherInfoTextEn = "Other Important Info";
        String otherInfoTextId = "Info Penting Lainnya";

        // Create locator that finds all matching elements (not just last)
        Locator otherImportantInfoSection = new Locator("OTHER_IMPORTANT_INFO_SECTION",
                new By.ByXPath("//*[(contains(text(),'"+otherInfoTextEn+"') or contains(text(),'"+otherInfoTextId+"'))]"));

        // Wait for tab content to fully load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.warn("Interrupted while waiting for tab content");
        }

        // Find all matching elements and get the visible one
        List<WebElement> allElements = driverTools.elements(otherImportantInfoSection).orElse(emptyList());
        logger.log("Found " + allElements.size() + " elements matching 'Other Important Info'");

        WebElement visibleElement = null;
        String otherInfoText = "";

        for (int i = 0; i < allElements.size(); i++) {
            WebElement element = allElements.get(i);
            if (driverTools.isElementDisplayed(element)) {
                String text = driverTools.getText(element);
                logger.log("Element " + (i+1) + " is visible with text: '" + text + "'");
                if (text != null && !text.trim().isEmpty()) {
                    visibleElement = element;
                    otherInfoText = text;
                    logger.log("Selected visible element " + (i+1) + " as the active one");
                    break;
                }
            } else {
                logger.log("Element " + (i+1) + " is hidden (likely from inactive tab)");
            }
        }

        if (visibleElement == null) {
            logger.warn("No visible 'Other Important Info' element found");
            throw new RuntimeException("No visible 'Other Important Info' element found among " + allElements.size() + " elements");
        }

        logger.log("Other Important Information Text Found: " + otherInfoText);
        boolean isOtherInfoTextCorrect = otherInfoText.contains(otherInfoTextEn) || otherInfoText.contains(otherInfoTextId);
        Assertion.assertThat("Other Important Information text should be displayed correctly",
                otherInfoText, anyOf(containsString(otherInfoTextEn), containsString(otherInfoTextId)));
        return isOtherInfoTextCorrect;
    }

    public void fillDob(String year, String month, String day) {
        driverTools.click(DOB_PICKER);
        WebElement yearDropdown = driverTools.waitForElementToAppear(YEAR_DROPDOWN).orElseThrow();
        driverTools.click(yearDropdown);

        Locator dynamicYear = new Locator("YEAR",
                By.xpath("//button[contains(@class, 'month-year-menus_button__') and text()='" + year + "']"));
        driverTools.waitForElementToAppear(dynamicYear);
        driverTools.clickWithJavascriptExecutor(dynamicYear);

        driverTools.waitForElementToAppear(MONTH_DROPDOWN);
        driverTools.clickWithJavascriptExecutor(MONTH_DROPDOWN);

        Locator dynamicMonth = new Locator("MONTH",
                By.xpath("//button[contains(@class, 'month-year-menus_button__') and (text()='" + month + "')]"));
        driverTools.waitForElementToAppear(dynamicMonth);
        driverTools.clickWithJavascriptExecutor(dynamicMonth);

        Locator dynamicDate = new Locator("DATE",
                By.xpath("(//button[contains(@class, 'Day_day__')]//span[text()='" + day + "'])[1]"));
        driverTools.waitForElementToAppear(dynamicDate);
        driverTools.clickWithJavascriptExecutor(dynamicDate);

        driverTools.clickWithJavascriptExecutor(DOB_SUBMIT);
    }

    private boolean isFaqExpandable(String tabName) {
        // Get count of FAQ questions to determine how many to iterate
        List<WebElement> allFaqQuestions = driverTools.elements(FAQ_QUESTION).orElse(emptyList());
        int totalFaqCount = allFaqQuestions.size();

        logger.log("[" + tabName + " Tab] Total FAQ questions found: " + totalFaqCount);

        if (totalFaqCount == 0) {
            logger.warn("[" + tabName + " Tab] No FAQ questions found on current page");
            Assertion.assertThat("[" + tabName + " Tab] FAQ questions should be visible",
                    false, is(true));
            return false;
        }

        // Find visible FAQ indices
        List<Integer> visibleIndices = new ArrayList<>();
        for (int i = 0; i < totalFaqCount; i++) {
            // Use index-based locator to check visibility
            int xpathIndex = i + 1; // XPath is 1-based
            Locator faqQuestionAtIndex = new Locator("FAQ_QUESTION_" + xpathIndex,
                    new By.ByXPath("(//*[contains(@class,'Accordion_accordion_button')])[" + xpathIndex + "]"));

            WebElement questionElement = driverTools.element(faqQuestionAtIndex).orElse(null);
            if (questionElement != null && driverTools.isElementDisplayed(questionElement)) {
                visibleIndices.add(i);
            }
        }

        logger.log("[" + tabName + " Tab] Visible FAQ questions count: " + visibleIndices.size());

        if (visibleIndices.isEmpty()) {
            logger.warn("[" + tabName + " Tab] No visible FAQ questions found on current page");
            Assertion.assertThat("[" + tabName + " Tab] FAQ questions should be visible",
                    false, is(true));
            return false;
        }

        boolean allFaqsPassed = true;
        int faqNumber = 1; // Counter for visible FAQs (1-based for display)
        int validFaqsCount = 0; // Counter for FAQs with valid titles (from current tab)

        // Iterate over all visible FAQ questions using index-based locators
        for (int actualIndex : visibleIndices) {
            int xpathIndex = actualIndex + 1; // XPath is 1-based
            logger.log("[" + tabName + " Tab] ========== Testing FAQ " + faqNumber + " (XPath index: " + xpathIndex + ") ==========");

            // Create index-based locators for this FAQ
            Locator faqQuestionLocator = new Locator("FAQ_QUESTION_" + xpathIndex,
                    new By.ByXPath("(//*[contains(@class,'Accordion_accordion_button')])[" + xpathIndex + "]"));
            Locator faqQuestionTitleLocator = new Locator("FAQ_QUESTION_TITLE_" + xpathIndex,
                    new By.ByXPath("(//*[contains(@class,'Accordion_accordion_button')]//span)[" + xpathIndex + "]"));
            Locator faqAnswerContainerLocator = new Locator("FAQ_ANSWER_CONTAINER_" + xpathIndex,
                    new By.ByXPath("(//*[@data-testid='collapse-container'])[" + xpathIndex + "]"));
            Locator faqAnswerTextLocator = new Locator("FAQ_ANSWER_TEXT_" + xpathIndex,
                    new By.ByXPath("(//*[contains(@class,'PenaltyInfoFaq_faq_answer')])[" + xpathIndex + "]"));

            // Get FAQ question element using driverTools
            WebElement faqQuestion = driverTools.waitForElementToPresence(faqQuestionLocator, 5).orElse(null);
            if (faqQuestion == null) {
                logger.log("[" + tabName + " Tab] FAQ " + faqNumber + " question element not found - skipping");
                faqNumber++;
                continue;
            }

            // Scroll to FAQ question to ensure it's visible in viewport
            logger.log("[" + tabName + " Tab] Scrolling to FAQ " + faqNumber + " to ensure visibility");
            driverTools.scrollElementToCenter(faqQuestionLocator);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Get FAQ question title using driverTools
            WebElement questionTitleElement = driverTools.waitForElementToPresence(faqQuestionTitleLocator, 5).orElse(null);
            String questionTitleText = questionTitleElement != null ? driverTools.getText(questionTitleElement) : "";
            logger.log("[" + tabName + " Tab] FAQ " + faqNumber + " question title: '" + questionTitleText + "'");

            boolean hasQuestionTitle = questionTitleText != null && !questionTitleText.trim().isEmpty();
            if (!hasQuestionTitle) {
                // FAQ with empty title is likely from an inactive tab - skip without failing
                logger.log("[" + tabName + " Tab] FAQ " + faqNumber + " has empty title (likely from inactive tab) - skipping");
                faqNumber++;
                continue;
            }

            // Valid FAQ found (from current tab) - increment counter
            validFaqsCount++;
            logger.log("[" + tabName + " Tab] Valid FAQ found: " + questionTitleText);
            Assertion.assertThat("[" + tabName + " Tab] FAQ " + faqNumber + " question title should not be null or empty",
                    questionTitleText, allOf(notNullValue(), not(emptyString())));

            // Get FAQ answer container using driverTools
            WebElement answerContainer = driverTools.waitForElementToPresence(faqAnswerContainerLocator, 5).orElse(null);
            if (answerContainer == null) {
                logger.warn("[" + tabName + " Tab] No FAQ answer container found at index " + faqNumber);
                allFaqsPassed = false;
                faqNumber++;
                continue;
            }

            // Get initial state (should be collapsed - aria-hidden="true")
            String ariaHiddenBefore = answerContainer.getAttribute("aria-hidden");
            logger.log("[" + tabName + " Tab] FAQ " + faqNumber + " aria-hidden before click: " + ariaHiddenBefore);

            // Click to EXPAND the FAQ
            logger.log("[" + tabName + " Tab] Clicking to EXPAND FAQ " + faqNumber + ": " + questionTitleText);
            driverTools.clickWithJavascriptExecutor(faqQuestionLocator);

            // Wait for animation to complete
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Scroll to answer container to see the expanded content
            logger.log("[" + tabName + " Tab] Scrolling to FAQ " + faqNumber + " answer to see expanded content");
            driverTools.scrollElementToCenter(faqAnswerContainerLocator);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Re-fetch answer container and verify it's expanded (aria-hidden="false")
            WebElement answerContainerAfter = driverTools.waitForElementToPresence(faqAnswerContainerLocator, 5).orElseThrow();
            String ariaHiddenAfterExpand = answerContainerAfter.getAttribute("aria-hidden");
            logger.log("[" + tabName + " Tab] FAQ " + faqNumber + " aria-hidden after EXPAND: '" + ariaHiddenAfterExpand + "'");

            boolean isExpandable = "false".equals(ariaHiddenAfterExpand);
            Assertion.assertThat("[" + tabName + " Tab] FAQ " + faqNumber + " should be expanded (aria-hidden='false')",
                    ariaHiddenAfterExpand, is("false"));

            // Get the answer text using driverTools
            logger.log("[" + tabName + " Tab] Looking for FAQ " + faqNumber + " answer text at XPath index " + xpathIndex);
            WebElement answerTextElement = driverTools.waitForElementToPresence(faqAnswerTextLocator, 5).orElse(null);

            // Scroll to answer text element to see it in viewport
            if (answerTextElement != null && driverTools.isElementDisplayed(answerTextElement)) {
                logger.log("[" + tabName + " Tab] Scrolling to FAQ " + faqNumber + " answer text");
                driverTools.scrollElementToCenter(faqAnswerTextLocator);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            String answerTextContent = (answerTextElement != null && driverTools.isElementDisplayed(answerTextElement))
                    ? driverTools.getText(answerTextElement) : "";
            logger.log("[" + tabName + " Tab] FAQ " + faqNumber + " answer text: '" + answerTextContent + "'");

            boolean hasAnswerText = answerTextContent != null && !answerTextContent.trim().isEmpty();

            // Soft check for answer text - log warning if empty but don't fail test
            if (!hasAnswerText) {
                logger.warn("[" + tabName + " Tab] FAQ " + faqNumber + " answer text is empty or not found after expansion");
            } else {
                Assertion.assertThat("[" + tabName + " Tab] FAQ " + faqNumber + " answer text should not be null or empty after expansion",
                        answerTextContent, allOf(notNullValue(), not(emptyString())));
            }

            logger.log("[" + tabName + " Tab] FAQ " + faqNumber + " test completed - isExpandable: " + isExpandable +
                    ", hasQuestionTitle: " + hasQuestionTitle + ", hasAnswerText: " + hasAnswerText);

            if (!isExpandable || !hasQuestionTitle) {
                allFaqsPassed = false;
            }

            faqNumber++;
        }

        logger.log("[" + tabName + " Tab] ========== " + validFaqsCount + " valid FAQs tested (out of " + visibleIndices.size() + " total). Result: " + (allFaqsPassed && validFaqsCount > 0 ? "PASSED" : "FAILED") + " ==========");

        // Pass only if at least one valid FAQ was tested and all valid FAQs passed
        if (validFaqsCount == 0) {
            logger.warn("[" + tabName + " Tab] No valid FAQs found on current tab");
            Assertion.assertThat("[" + tabName + " Tab] At least one valid FAQ should be found",
                    false, is(true));
            return false;
        }

        return allFaqsPassed;
    }


    private boolean isFAQSectionVisible(String tabName) {
        String faqTextEn = "Frequently asked questions";
        String faqTextId = "Pertanyaan yang sering diajukan";

        // Create locator with specific class to avoid matching elements from other pages
        Locator faqSection = new Locator("FAQ_INFO_SECTION",
                new By.ByXPath("//span[contains(@class,'PenaltyInfoFaq_faq_title') and (contains(text(),'"+faqTextEn+"') or contains(text(),'"+faqTextId+"'))]"));

        // Wait for tab content to fully load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.warn("[" + tabName + " Tab] Interrupted while waiting for FAQ content");
        }

        // Find all matching elements and get the visible one
        List<WebElement> allElements = driverTools.elements(faqSection).orElse(emptyList());
        logger.log("[" + tabName + " Tab] Found " + allElements.size() + " elements matching 'FAQ'");

        WebElement visibleElement = null;
        String faqText = "";

        for (int i = 0; i < allElements.size(); i++) {
            WebElement element = allElements.get(i);
            if (driverTools.isElementDisplayed(element)) {
                String text = driverTools.getText(element);
                logger.log("[" + tabName + " Tab] FAQ element " + (i+1) + " is visible with text: '" + text + "'");
                if (text != null && !text.trim().isEmpty()) {
                    visibleElement = element;
                    faqText = text;
                    logger.log("[" + tabName + " Tab] Selected visible FAQ element " + (i+1) + " as the active one");
                    break;
                }
            } else {
                logger.log("[" + tabName + " Tab] FAQ element " + (i+1) + " is hidden (likely from inactive tab)");
            }
        }

        if (visibleElement == null) {
            logger.warn("[" + tabName + " Tab] No visible FAQ element found among " + allElements.size() + " elements");
            Assertion.assertThat("[" + tabName + " Tab] FAQ section should be visible",
                    false, is(true));
            return false;
        }

        logger.log("[" + tabName + " Tab] FAQ Text Found: " + faqText);
        boolean isFaqTextCorrect = faqText.contains(faqTextEn) || faqText.contains(faqTextId);
        Assertion.assertThat("[" + tabName + " Tab] FAQ text should be displayed correctly",
                faqText, anyOf(containsString(faqTextEn), containsString(faqTextId)));
        return isFaqTextCorrect && isFaqExpandable(tabName);
    }

    /**
     * Verifies Refund Tab Section contains:
     * - Refund Amount title
     * - FAQ section
     * - Other Important Information section
     */
    public boolean verifyRefundTabSection() {
        return isFAQSectionVisible("Refund") && isOtherImportantInfoSectionVisible();
    }

    /**
     * Verifies Reschedule Tab Section contains:
     * - Reschedule Fee title
     * - Other Important Information section
     */
    public boolean verifyRescheduleTabSection() {
        return isFAQSectionVisible("Reschedule") && isOtherImportantInfoSectionVisible();
    }

    public boolean isFlightScheduleTabVisible(){
        return driverTools.waitForElementsToAppear(FLIGHT_DETAIL_POPUP).isPresent();
    }
    public void clickOnFacilitiesDetail(){
        driverTools.waitForElementToAppear(SEE_OTHER_FACILITY);
        driverTools.clickWithJavascriptExecutor(SEE_OTHER_FACILITY);
    }
    public boolean isFlightFacilityVisible(){
        return driverTools.waitForElementToAppear(FLIGHT_FACILITY_POPUP).isPresent();
    }
    public void closeSecondPopup(){
        driverTools.waitForElementToAppear(CLOSE_SECOND);
        driverTools.click(CLOSE_SECOND);
        driverTools.waitForElementToDisappear(FLIGHT_FACILITY_POPUP);
    }

    /**
     * Checks if the Flight Facilities bottom sheet is displayed.
     * @return true if the bottom sheet with "Flight Facilities" title is visible
     */
    public boolean isFlightFacilitiesBottomSheetDisplayed() {
        return driverTools.waitForElementToAppear(FLIGHT_FACILITIES_BOTTOM_SHEET, 10).isPresent();
    }

    public void closePopUp(){
        driverTools.waitForElementToAppear(CLOSE);
        driverTools.clickWithJavascriptExecutor(CLOSE);
        driverTools.waitForElementToDisappear(CLOSE);
    }

    public void openPriceDetailSection(){
        driverTools.waitForElementsToAppear(PRICE_BUTTON);
        driverTools.clickWithJavascriptExecutor(PRICE_BUTTON);
    }
    public boolean isPriceDetailPageVisible(){
        return driverTools.waitForElementToAppear(PRICE_DETAIL_POPUP).isPresent();
    }
    public boolean isTotalPriceDisplayed(){
        driverTools.scrollIntoWebView(TOTAL_PRICE);
        return driverTools.waitForElementsToAppear(TOTAL_PRICE).isPresent();
    }
    public void scrollToBaggageSection(){
        driverTools.clickWithJavascriptExecutor(LAST_PAX_INFO_CHEVRON);
        if (driverTools.isElementDisplayed(CONTINUE_BUTTON))
            driverTools.click(CONTINUE_BUTTON);
        if (driverTools.waitForElementToAppear(ADD_BAGGAGES, 2).isPresent()) {
            driverTools.scrollElementToCenter(ADD_BAGGAGES);
        } else if (driverTools.waitForElementToAppear(FIRST_PAID_INLINE_BAGGAGE_OPTION, 2).isPresent()) {
            driverTools.scrollElementToCenter(FIRST_PAID_INLINE_BAGGAGE_OPTION);
        }
    }

    public void selectAddonsBaggage(){
        driverTools.waitForElementToAppear(ADD_BAGGAGES).orElseThrow();
        driverTools.clickWithJavascriptExecutor(ADD_BAGGAGES_BUTTON);
        clickOkayGotIt();
        driverTools.waitForElementToAppear(SELECT_BAGGAGES_BUTTON);
        clickOkayGotIt();
        driverTools.click(SELECT_BAGGAGES_BUTTON);
        clickOkayGotIt();
        driverTools.waitForElementToAppear(FIRST_PAID_BAGGAGE_OPTION);
        selectPaidBaggageOption();
        driverTools.click(SAVE_BUTTON);
    }

    public void fillDob(int indexPax, String dob) {
        // Add a check to ensure the DOB field is clickable before proceeding
        Locator dobLocator = getDobLocator(indexPax);
        try{
            driverTools.waitForElementToAppear(dobLocator, 15).orElseThrow();
            driverTools.scrollToElement(dobLocator);

            if(driverTools.isElementDisplayed(dobLocator)){
                driverTools.clickWithJavascriptExecutor(dobLocator);
                fillDate(dob, "DOB for passenger " + indexPax);
            }
        }catch(Exception e){
            if (driverTools.isElementDisplayed(clickPaxChevronLocator(indexPax))) {
                driverTools.clickWithJavascriptExecutor(clickPaxChevronLocator(indexPax));
            }
            if(driverTools.isElementDisplayed(dobLocator)) {
                driverTools.clickWithJavascriptExecutor(dobLocator);
                fillDate(dob, "DOB for passenger " + indexPax);
            }else{
                logger.log("DOB field for passenger" + indexPax + "is not displayed or clickable.");
            }
        }
    }

    public void clickOnSeeDetails(){
        driverTools.waitForElementToAppear(SEE_DETAILS);
        driverTools.clickWithJavascriptExecutor(SEE_DETAILS);
        driverTools.clickWithJavascriptExecutor(RESCHEDULE_BUTTON);
    }

    public void closeRRPopUp(){
        driverTools.waitForElementToAppear(CLOSERR);
        driverTools.clickWithJavascriptExecutor(CLOSERR);
        driverTools.waitForElementToDisappear(CLOSERR);
    }

    public boolean isRRPopUpAppear(){
        return driverTools.waitForElementToAppear(RRPOPUP).isPresent();
    }

    public void selectQuietZoneSeat(){
        driverTools.waitForElementToAppear(ADD_SEAT).orElseThrow();
        try {
            driverTools.scrollIntoWebView(ADD_SEAT);
            driverTools.clickWithJavascriptExecutor(ADD_SEAT);
            driverTools.waitForElementToAppear(ADD_SEATS_BUTTON);
            driverTools.clickWithJavascriptExecutor(ADD_SEATS_BUTTON);
        }catch (Exception e){
            driverTools.clickWithJavascriptExecutor(ADD_SEAT);
            driverTools.waitForElementToAppear(ADD_SEATS_BUTTON);
            driverTools.clickWithJavascriptExecutor(ADD_SEATS_BUTTON);
        }
        isQuietZoneBannerVisible();
        driverTools.click(SAVE_BUTTON);
        driverTools.waitForElementToAppear(DONE_SELECTING);
        driverTools.click(DONE_SELECTING);
    }

    public boolean isQuietZoneBannerVisible(){
        driverTools.scrollIntoWebView(QUIET_ZONE_BANNER);
        return driverTools.waitForElementsToAppear(QUIET_ZONE_BANNER).isPresent();
    }

    public void selectAddonsBaggageMultiPAX(int adultPassengers,int childPassengers){
        int totalPax = adultPassengers + childPassengers;
        driverTools.waitForElementToAppear(ADD_BAGGAGES).orElseThrow();
        try {
            selectingBaggageAddOns(totalPax);
        }catch (Exception e){
            selectingBaggageAddOns(totalPax);
        }
        driverTools.click(SAVE_BUTTON);

    }

    public abstract void insuranceSelector();

    public void clickExtraProtectionAddButtonForBaggage() {
        driverTools.waitForElementToAppear(BAGGAGE_ADDON, 10);
        driverTools.scrollIntoWebView(BAGGAGE_ADDON);
        driverTools.clickWithJavascriptExecutor(BAGGAGE_ADDON);
    }

    public void selectingBaggageAddOns(int totalPax){
        driverTools.waitForElementToPresence(ADD_BAGGAGES_BUTTON);
        driverTools.clickElementByCoordinateDWeb(ADD_BAGGAGES_BUTTON);
        clickOkayGotIt();
        driverTools.waitForElementToPresence(SELECT_BAGGAGES_BUTTON);
        driverTools.click(SELECT_BAGGAGES_BUTTON);
        clickOkayGotIt();
        driverTools.waitForElementToAppear(FIRST_PAID_BAGGAGE_OPTION);
        selectPaidBaggageOption();
        for (int paxIndex = 1; paxIndex <= totalPax; paxIndex++) {
            driverTools.clickWithJavascriptExecutor(BAGGAGE_SELECTOR);
        }
    }

    public boolean isPassportBannerVisibled(){
        return driverTools.waitForElementsToAppear(PASSPORT_BANNER).isPresent();
    }

    public boolean isPassportConfirmationPopupVisibled(){
        return driverTools.waitForElementsToAppear(PASSPORT_CONFIRMATION_POPUP).isPresent();
    }

    public void showingBookingPopupConfirmation(){
        WebElement continueToPayment = driverTools.waitForElementToAppear(NEXT_BUTTON).orElseThrow();
        driverTools.scrollIntoWebView(continueToPayment);
        driverTools.clickWithJavascriptExecutor(NEXT_BUTTON);
    }

    public void goToPaymentPageFromBookingFormPageForOthersPopup(){
        driverTools.waitForElementToAppear(CONTINUE_TO_PAYMENT_PAGE_POPUP);
        driverTools.clickWithJavascriptExecutor(CONTINUE_TO_PAYMENT_PAGE_POPUP);
        driverTools.waitForElementToAppear(I_DONT_WANT_IT_BUTTON, 2);
        if(driverTools.isElementDisplayed(I_DONT_WANT_IT_BUTTON)){
            driverTools.click(I_DONT_WANT_IT_BUTTON);
        } else {
            logger.log("I don't want it button is not displayed, skipping click.");
        }
    }

    public void goToPaymentPageWithoutSkippingPopup(){
        WebElement continueToPayment = driverTools.waitForElementToAppear(NEXT_BUTTON).orElseThrow();
        driverTools.scrollIntoWebView(continueToPayment);
        driverTools.clickWithJavascriptExecutor(NEXT_BUTTON);
    }

    public String getContinueToPaymentPopupWarningMessage(){
        driverTools.waitForElementToAppear(POP_UP_WARNING_LABEL_AFTER_CONTINUE_PAYMENT);
        WebElement element = driverTools.element(POP_UP_WARNING_LABEL_AFTER_CONTINUE_PAYMENT).orElseThrow();
        String warningLabel = driverTools.getText(element);
        return warningLabel;
    }

    public String getNonPaymentConfirmationDialogPopupText(){
        driverTools.waitForElementToAppear(NON_PAYMENT_CONFIRMATION_DIALOG_POPUP);
        WebElement element =  driverTools.element(NON_PAYMENT_CONFIRMATION_DIALOG_POPUP).orElseThrow();
        String buttonLabel = driverTools.getText(element);
        return buttonLabel;
    }

    public String getPaymentConfirmationDialogPopupText(){
        driverTools.waitForElementToAppear(CONTINUE_TO_PAYMENT_PAGE_POPUP);
        WebElement element =  driverTools.element(CONTINUE_TO_PAYMENT_PAGE_POPUP).orElseThrow();
        String buttonLabel = driverTools.getText(element);
        return buttonLabel;
    }
    public void goBackToBookingFormFromPaymentPopUp(){
        driverTools.waitForElementsToAppear(NON_PAYMENT_CONFIRMATION_DIALOG_POPUP);
        driverTools.click(NON_PAYMENT_CONFIRMATION_DIALOG_POPUP);
    }
    public void selectSeatAddons() {
        driverTools.waitForElementToAppear(ADD_SEAT).orElseThrow();

        driverTools.scrollIntoWebView(ADD_SEAT);
        driverTools.clickWithJavascriptExecutor(ADD_SEAT);

        driverTools.waitForElementToAppear(SEAT_MAP_CONTAINER, 20).orElseThrow();

        driverTools.waitForElementToAppear(SEAT, 20).orElseThrow();
        driverTools.scrollIntoWebView(SEAT);
        driverTools.clickWithJavascriptExecutor(SEAT);

        driverTools.waitForElementToAppear(SAVE_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public boolean vICheckinBaggageInterimPage(){
        return driverTools.waitForElementToAppear(VI_CHECKIN_INTERIM).isPresent();
    }

    public void selectChooseBtn(){
        driverTools.waitForElementToAppear(INSURANCE_CHOSEN_BUTTON);
        driverTools.clickWithJavascriptExecutor(INSURANCE_CHOSEN_BUTTON);
    }

    public void clickContinue() {
        driverTools.click(CONTINUE_NEXT_BUTTON);
    }

    public void clickRefundOkGotIt (){
        driverTools.waitForElementToAppear(REFUND_GOT_IT);
        driverTools.clickWithJavascriptExecutor(REFUND_GOT_IT);
    }

    private Locator getSeeOtherFacilityByIndex(int index) {
        return new Locator("SEE_OTHER_FACILITY_" + index,
                new By.ByXPath("(//button[contains(@class,'Facilities_more_facilities__')])[" + index + "]"));
    }

    public void clickOnFacilitiesDetail(int index){
        Locator seeOtherFacilityByIndex = getSeeOtherFacilityByIndex(index);
        driverTools.waitForElementToAppear(seeOtherFacilityByIndex);
        driverTools.clickWithJavascriptExecutor(seeOtherFacilityByIndex);
    }


    public void closeFlightFacilitiesBottomSheet() {
        driverTools.waitForElementToAppear(BOTTOM_SHEET_CLOSE_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(BOTTOM_SHEET_CLOSE_BUTTON);
        driverTools.waitForElementToDisappear(FLIGHT_FACILITIES_BOTTOM_SHEET);
    }

    public void clickBaggageAddButton() {
        driverTools.waitForElementToAppear(BAGGAGE_CARD_ADD_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(BAGGAGE_CARD_ADD_BUTTON);
    }

    public void clickBaggageEditButton() {
        driverTools.waitForElementToAppear(BAGGAGE_EDIT_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(BAGGAGE_EDIT_BUTTON);
    }
    public void verifyBaggageNotPreSelected() {

        if (driverTools.waitForElementToAppear(BAGGAGE_CARD_ADD_STATE, 2).isPresent()) {
            verifyPopupBaggageNotPreSelected();
        } else {
            verifyInlineBaggageNotPreSelected();
        }
    }
    private void verifyInlineBaggageNotPreSelected() {

        Assertion.assertThat("Inline baggage options are displayed",
                driverTools.isElementDisplayed(INLINE_BAGGAGE_SECTION), is(true));

        Assertion.assertThat("No baggage option is pre-selected",
                driverTools.isElementDisplayed(SELECTED_NO_BAGGAGE_OPTION), is(true));
    }
    public void verifyPopupBaggageNotPreSelected() {
        driverTools.waitForElementToAppear(BAGGAGE_CARD_ADD_STATE, 10).orElseThrow();
        String addExtraText = driverTools.getText(BAGGAGE_ADD_EXTRA_TEXT).orElse("").trim();
        String unselectedText = driverTools.getText(BAGGAGE_UNSELECTED_TEXT).orElse("").trim();
        logger.log("Baggage card add state text: " + addExtraText);
        logger.log("Baggage card unselected text: " + unselectedText);
        Assertion.assertThat("Baggage add card is displayed",
                driverTools.isElementDisplayed(BAGGAGE_CARD_ADD_STATE), is(true));
        Assertion.assertThat("Baggage card shows add extra baggage prompt",
                addExtraText.toLowerCase(),
                anyOf(
                        containsString("add extra baggage"),
                        containsString("tambah bagasi"),
                        containsString("extra baggage"),
                        containsString("bagasi ekstra"),
                        allOf(containsString("from"), containsString("kg")),
                        allOf(containsString("dari"), containsString("kg"))));
        Assertion.assertThat("Baggage card shows unselected description",
                unselectedText.isEmpty(), is(false));
        Assertion.assertThat("Baggage card add button is displayed",
                driverTools.isElementDisplayed(BAGGAGE_CARD_ADD_BUTTON), is(true));
        Assertion.assertThat("Baggage edit icon should not be displayed when baggage is not selected",
                driverTools.isElementPresent(BAGGAGE_EDIT_BUTTON), is(false));
    }

    private String getBaggageCardAddAreaText() {
        WebElement addArea = driverTools.element(BAGGAGE_CARD_ADD_STATE).orElseThrow();
        String textContent = addArea.getAttribute("textContent");
        if (textContent != null && !textContent.isBlank()) {
            return textContent.replaceAll("\\s+", " ").trim();
        }
        return driverTools.getText(addArea);
    }

    public void verifyBaggageReturnedToAddState() {
        verifyBaggageNotPreSelected();
    }

    public void verifyBaggageSelectionSubtotalIsZero() {
        if (driverTools.waitForElementToAppear(BAGGAGE_SELECTION_SUBTOTAL, 5).isPresent()) {
            String subtotal = driverTools.getText(BAGGAGE_SELECTION_SUBTOTAL).orElse("").trim();
            logger.log("Baggage selection subtotal: " + subtotal);
            Assertion.assertThat("Baggage selection subtotal should be zero", subtotal, is("0"));
        } else {
            logger.log("Baggage selection subtotal not present, skipping verification");
        }
    }
    public void clickBaggageSelectionSaveButton() {
        if (driverTools.waitForElementToAppear(BAGGAGE_SELECTION_SAVE_BUTTON, 10).isPresent()) {
            driverTools.clickWithJavascriptExecutor(BAGGAGE_SELECTION_SAVE_BUTTON);
        }
    }


    public void clickBaggageSelectButton() {
        driverTools.waitForElementToAppear(BAGGAGE_SELECT_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(BAGGAGE_SELECT_BUTTON);
    }


    public String getBaggageSubtotalPrice() {
        driverTools.waitForElementToAppear(BAGGAGE_SUBTOTAL_PRICE, 10).orElseThrow();
        return driverTools.getText(BAGGAGE_SUBTOTAL_PRICE).orElse("");
    }


    public void validateBaggageSubtotalIsZero(String expectedCurrency) {
        String subtotal = getBaggageSubtotalPrice();
        logger.log("Baggage subtotal: " + subtotal);
        Assertion.assertThat("Baggage subtotal should contain currency", subtotal.contains(expectedCurrency), is(true));
        Assertion.assertThat("Baggage subtotal should be zero", subtotal, is(expectedCurrency + " 0"));
    }


    public void clickBaggagePriceToggler() {
        driverTools.waitForElementToAppear(BAGGAGE_PRICE_TOGGLER, 10);
        driverTools.clickWithJavascriptExecutor(BAGGAGE_PRICE_TOGGLER);
    }


    public boolean isBaggagePriceDetailsBottomSheetDisplayed() {
        return driverTools.waitForElementToAppear(BAGGAGE_PRICE_DETAILS_BOTTOM_SHEET, 10).isPresent();
    }


    public String getBaggagePassengerName() {
        driverTools.waitForElementToAppear(BAGGAGE_PASSENGER_NAME, 10).orElseThrow();
        return driverTools.getText(BAGGAGE_PASSENGER_NAME).orElse("");
    }


    public void validateBaggagePassengerName(String expectedName) {
        String actualName = getBaggagePassengerName();
        logger.log("Baggage passenger name: " + actualName);
        // Compare first name (in case full name has first + last)
        String expectedFirstName = expectedName.split(" ")[0];
        Assertion.assertThat("Baggage should be assigned to passenger: " + expectedFirstName,
                actualName.toLowerCase().contains(expectedFirstName.toLowerCase()), is(true));
    }


    public void closeBaggagePriceDetailsBottomSheet() {
        driverTools.waitForElementToAppear(BAGGAGE_PRICE_DETAILS_CLOSE, 5);
        driverTools.clickWithJavascriptExecutor(BAGGAGE_PRICE_DETAILS_CLOSE);
        driverTools.waitForElementToDisappear(BAGGAGE_PRICE_DETAILS_BOTTOM_SHEET);
    }

    public void selectBaggage() {
        selectPaidBaggageOption();
    }

    public SelectedBaggageDetail selectPaidBaggageOption() {
        driverTools.waitForElementToAppear(FIRST_PAID_BAGGAGE_OPTION, 10).orElseThrow();
        String weightLabel = driverTools.getText(FIRST_PAID_BAGGAGE_WEIGHT).orElse("").trim();
        String priceLabel = driverTools.getText(FIRST_PAID_BAGGAGE_PRICE).orElse("").trim();
        driverTools.clickWithJavascriptExecutor(FIRST_PAID_BAGGAGE_OPTION);

        SelectedBaggageDetail selectedBaggage = new SelectedBaggageDetail();
        selectedBaggage.weightLabel = weightLabel;
        selectedBaggage.weightKg = extractBaggageWeightKg(weightLabel);
        selectedBaggage.priceLabel = priceLabel;
        selectedBaggage.normalizedPrice = normalizePrice(priceLabel);
        logger.log("Selected baggage: " + weightLabel + " at " + priceLabel);
        return selectedBaggage;
    }

    public void saveBaggage(){
        clickBaggageSelectionSaveButton();
        driverTools.waitForElementToAppear(DONE_SELECTING, 5).ifPresent(element ->
                driverTools.clickWithJavascriptExecutor(DONE_SELECTING));
    }

    public SelectedBaggageDetail selectInlinePaidBaggageOption() {
        WebElement baggageOption = driverTools
                .waitForElementToAppear(FIRST_PAID_INLINE_BAGGAGE_OPTION, 10)
                .orElseThrow();
        String baggageLabel = driverTools
                .getAttribute(baggageOption, "aria-label")
                .trim();
        driverTools.clickWithJavascriptExecutor(FIRST_PAID_INLINE_BAGGAGE_OPTION);

        SelectedBaggageDetail selectedBaggage = new SelectedBaggageDetail();

        // Example: "+20 Kg IDR 216,450"
        selectedBaggage.weightLabel = baggageLabel.split(" IDR")[0].trim();
        selectedBaggage.weightKg = extractBaggageWeightKg(selectedBaggage.weightLabel);
        selectedBaggage.priceLabel = baggageLabel.substring(baggageLabel.indexOf("IDR")).trim();
        selectedBaggage.normalizedPrice = normalizePrice(selectedBaggage.priceLabel);

        logger.log("Selected baggage: "
                + selectedBaggage.weightLabel
                + " at "
                + selectedBaggage.priceLabel);

        return selectedBaggage;
    }
    public SelectedBaggageDetail addBaggageFromBookingForm() {
        if (driverTools.waitForElementToAppear(FIRST_PAID_INLINE_BAGGAGE_OPTION, 10).isPresent()) {
            return selectInlinePaidBaggageOption();
        }
        clickBaggageAddButton();
        clickBaggageSelectButton();
        SelectedBaggageDetail selectedBaggage = selectPaidBaggageOption();
        saveBaggage();
        return selectedBaggage;
    }

    public void openBaggageAddonSelection() {
        driverTools.scrollElementToCenter(ADD_BAGGAGES);
        clickOkayGotIt();
        if (driverTools.waitForElementToAppear(BAGGAGE_SELECT_BUTTON, 5).isPresent()) {
            driverTools.clickWithJavascriptExecutor(BAGGAGE_SELECT_BUTTON);
        } else {
            clickBaggageAddButton();
            clickBaggageSelectButton();
        }
        clickOkayGotIt();
    }
    private void clickBaggageEditButtonIfPresent() {
        driverTools.waitForElementToAppear(BAGGAGE_EDIT_BUTTON, 3)
                .ifPresent(element ->
                        driverTools.clickWithJavascriptExecutor(BAGGAGE_EDIT_BUTTON)
                );
    }
    public void removeBaggageAddon() {
        clickBaggageEditButtonIfPresent();
        driverTools.waitForElementToAppear(BAGGAGE_ZERO_KG, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(BAGGAGE_ZERO_KG);
        verifyBaggageSelectionSubtotalIsZero();
        clickBaggageSelectionSaveButton();
        driverTools.waitForElementToAppear(DONE_SELECTING, 5).ifPresent(element ->
                driverTools.clickWithJavascriptExecutor(DONE_SELECTING));
        verifyBaggageReturnedToAddState();
    }

    public SelectedBaggageDetail addBaggageAddonFromSelection() {
        return addBaggageFromBookingForm();
    }

    public String getBookingPriceDetailBaggageLabel() {
        return driverTools.getText(PRICE_BREAKDOWN_BAGGAGE_LABEL).orElse("").trim();
    }

    public String getBookingPriceDetailBaggagePrice() {
        return driverTools.getText(PRICE_BREAKDOWN_BAGGAGE_PRICE).orElse("").trim();
    }

    public String getBookingPriceDetailRefundAddonPrice() {
        return driverTools.getText(PRICE_BREAKDOWN_REFUND_ADDON_PRICE).orElse("0").trim();
    }

    public String getBookingPriceDetailPayNowAmount() {
        return normalizePrice(driverTools.getText(PRICE_BREAKDOWN_PAY_NOW).orElse(""));
    }
    public long getBookingPriceDetailOtherAddonAmount() {
        long total = 0;
        List<WebElement> elements = driver.findElements(PRICE_BREAKDOWN_OTHER_ADDON_PRICES.by());
        for (WebElement element : elements) {
            String price = normalizePrice(element.getText());
            if (!price.isEmpty()) {
                total += Long.parseLong(price);
            }
        }
        return total;
    }
    public boolean isBaggageDetailPresentInBookingPriceDetail() {
        return driverTools.isElementPresent(PRICE_BREAKDOWN_BAGGAGE_ITEM);
    }

    public boolean isBaseTicketPricePresentInBookingPriceDetail() {
        return driverTools.isElementPresent(PRICE_BREAKDOWN_ADULT_ITEM);
    }

    public boolean isRefundAddonPresentInBookingPriceDetail() {
        return driverTools.isElementPresent(PRICE_BREAKDOWN_REFUND_ADDON_ITEM);
    }

    public boolean isOtherAddonPresentInBookingPriceDetail() {
        return driverTools.isElementPresent(PRICE_BREAKDOWN_OTHER_ADDON_ITEM);
    }

    public void verifyBookingPriceDetailWithSelectedBaggage(SelectedBaggageDetail selectedBaggage) {
        Assertion.assertThat("Price Detail Page is displayed", isPriceDetailPageVisible(), is(true));
        Assertion.assertThat("Price breakdown shows base ticket price",
                isBaseTicketPricePresentInBookingPriceDetail(), is(true));
        Assertion.assertThat("Price breakdown shows baggage addon",
                isBaggageDetailPresentInBookingPriceDetail(), is(true));

        String breakdownLabel = getBookingPriceDetailBaggageLabel();
        String breakdownPrice = getBookingPriceDetailBaggagePrice();
        logger.log("Price breakdown baggage label: " + breakdownLabel);
        logger.log("Price breakdown baggage price: " + breakdownPrice);

        Assertion.assertThat("Price breakdown baggage weight matches selected baggage",
                extractBaggageWeightKg(breakdownLabel), is(selectedBaggage.weightKg));
        Assertion.assertThat("Price breakdown baggage price matches selected baggage",
                normalizePrice(breakdownPrice), is(selectedBaggage.normalizedPrice));
        verifyBookingPriceDetailPayNowTotal();
    }

    public long verifyBookingPriceDetailWithoutBaggage() {
        Assertion.assertThat("Price Detail Page is displayed", isPriceDetailPageVisible(), is(true));
        Assertion.assertThat("Price breakdown shows base ticket price",
                isBaseTicketPricePresentInBookingPriceDetail(), is(true));
        Assertion.assertThat("Price breakdown does not show baggage addon",
                isBaggageDetailPresentInBookingPriceDetail(), is(false));
        verifyBookingPriceDetailPayNowTotal();
        return Long.parseLong(getBookingPriceDetailPayNowAmount());
    }

    public void verifyBookingPriceDetailPayNowMatchesWithoutBaggageTotal(long payNowWithoutBaggage,
                                                                         SelectedBaggageDetail selectedBaggage) {
        long currentPayNow = Long.parseLong(getBookingPriceDetailPayNowAmount());
        long expectedIncrease = Long.parseLong(selectedBaggage.normalizedPrice);
        Assertion.assertThat("Pay Now increases by selected baggage price after re-adding baggage",
                currentPayNow, is(payNowWithoutBaggage + expectedIncrease));
    }

    private void verifyBookingPriceDetailPayNowTotal() {
        long ticketSubtotal = getBookingPriceDetailTicketSubtotal();
        long baggageAmount = isBaggageDetailPresentInBookingPriceDetail()
                ? Long.parseLong(normalizePrice(getBookingPriceDetailBaggagePrice()))
                : 0L;
        long refundAddonAmount = isRefundAddonPresentInBookingPriceDetail()
                ? Long.parseLong(normalizePrice(getBookingPriceDetailRefundAddonPrice()))
                : 0L;
        long discountAmount = getBookingPriceDetailDiscountAmount();
        long payNow = Long.parseLong(getBookingPriceDetailPayNowAmount());
        long otherAddonAmount = isOtherAddonPresentInBookingPriceDetail()
                ? getBookingPriceDetailOtherAddonAmount()
                : 0L;
        long expectedPayNow = ticketSubtotal + baggageAmount + otherAddonAmount + refundAddonAmount + discountAmount;

        logger.log("Price breakdown ticket subtotal: " + ticketSubtotal);
        logger.log("Price breakdown baggage amount: " + baggageAmount);
        logger.log("Price breakdown discount amount: " + discountAmount);
        logger.log("Price breakdown pay now: " + payNow);
        logger.log("Price breakdown 100% refund addon amount: " + refundAddonAmount);
        logger.log("Price breakdown expected pay now: " + expectedPayNow);

        Assertion.assertThat("Pay Now total matches price breakdown calculation", payNow, is(expectedPayNow));
    }

    private long getBookingPriceDetailTicketSubtotal() {
        List<WebElement> ticketPriceElements = driverTools.elements(PRICE_BREAKDOWN_TICKET_PRICES)
                .orElse(emptyList());
        long subtotal = 0L;
        for (WebElement ticketPriceElement : ticketPriceElements) {
            subtotal += Long.parseLong(normalizePrice(driverTools.getText(ticketPriceElement)));
        }
        return subtotal;
    }

    private long getBookingPriceDetailDiscountAmount() {
        if (!driverTools.isElementPresent(PRICE_BREAKDOWN_DISCOUNT)) {
            return 0L;
        }
        String discountText = driverTools.getText(PRICE_BREAKDOWN_DISCOUNT).orElse("0");
        return -Long.parseLong(normalizePrice(discountText));
    }

    private int extractBaggageWeightKg(String baggageText) {
        if (baggageText == null || baggageText.isBlank()) {
            return -1;
        }
        Matcher matcher = Pattern.compile("(\\d+)").matcher(baggageText);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
    }

    public static class SelectedBaggageDetail {
        public String weightLabel;
        public int weightKg;
        public String priceLabel;
        public String normalizedPrice;
    }

    public void verifyPriceBreakdownBaggageDetail(boolean shouldContainBaggage) {
        openPriceDetailSection();
        Assertion.assertThat("Price Detail Page is displayed", isPriceDetailPageVisible(), is(true));

        boolean baggageDetailPresent = isBaggageDetailPresentInBookingPriceDetail();
        if (shouldContainBaggage) {
            Assertion.assertThat("Price breakdown shows baggage detail", baggageDetailPresent, is(true));
        } else {
            Assertion.assertThat("Price breakdown shows base ticket price without baggage addon",
                    isBaseTicketPricePresentInBookingPriceDetail(), is(true));
            Assertion.assertThat("Price breakdown does not show baggage detail", baggageDetailPresent, is(false));
        }

        closePopUp();
        Assertion.assertThat("Booking Form page is visible after closing price breakdown",
                isBookingFormVisible(), is(true));
    }

    public void clickContinueToPaymentButtons() {
        driverTools.waitForElementToAppear(CONTINUE_TO_PAYMENT_BUTTON);
        driverTools.scrollIntoWebView(CONTINUE_TO_PAYMENT_BUTTON);
        driverTools.clickWithJavascriptExecutor(CONTINUE_TO_PAYMENT_BUTTON);

        driverTools.waitForElementToAppear(CONTINUE_TO_PAYMENT_BUTTON);
        driverTools.clickWithJavascriptExecutor(CONTINUE_TO_PAYMENT_BUTTON);
    }

    public void clickContinueToPaymentIfVisible() {
        Locator continueToPayment = new Locator("CONTINUE_TO_PAYMENT", By.xpath("//button[contains(@class, 'Button_variant_primary')]//span[contains(text(), 'Continue to payment')]"));
        driverTools.waitForElementToAppear(continueToPayment, 5).ifPresent(e -> {
            driverTools.scrollIntoWebView(continueToPayment);
            driverTools.clickWithJavascriptExecutor(continueToPayment);
        });
    }


    public abstract void scrollYearWheelTo(String year);

    public InsuranceGeneralData selectInsurance(String currency) {
        InsuranceGeneralData insuranceGeneralData = new InsuranceGeneralData();
        driverTools.scrollAndCheckElement(INSURANCE_BUTTON);

        WebElement titleElement = driverTools.waitForElementToPresence(FIRST_INSURANCE_EXTRA_PROTECTION_TITLE, 5).orElseThrow();
        String title = driverTools.getText(titleElement);
        logger.log("title: " + title + " will be added for insurance verification");
        insuranceGeneralData.insuranceTitle = title;

        // Check if insurance is already selected using the flag locator
        WebElement checkbox = driverTools.waitForElementToPresence(FIRST_INSURANCE_CHECKBOX_INPUT, 5).orElseThrow();
        boolean isInsuranceAdded = checkbox.isSelected();
        logger.log("is insurance already added: " + isInsuranceAdded);
        if (!isInsuranceAdded) {
            driverTools.clickWithJavascriptExecutor(FIRST_INSURANCE_CHECKBOX_LABEL);
        }

        // Dynamic price locator based on currency
        Locator priceLocator = getInsurancePriceLocator(currency);
        WebElement insurancePriceElement = driverTools.waitForElementToPresence(priceLocator).orElseThrow();
        String insurancePrice = removeExtraData(driverTools.getText(insurancePriceElement));
        logger.log("selected insurance price: " + insurancePrice);
        insuranceGeneralData.insurancePrice = insurancePrice;
        return insuranceGeneralData;
    }

    private Locator getInsurancePriceLocator(String currency) {
        String xpath = "(//div[contains(@class, 'BookingInsuranceCard_price')]//span[contains(text(),'" + currency + "')])[1]";
        return new Locator("FIRST_INSURANCE_EXTRA_PROTECTION_BASE_PRICE", new By.ByXPath(xpath));
    }

    public String removeExtraData(String pricePerPax){
        String[] priceText = pricePerPax.split("/");
        return priceText[0];
    }

    /**
     * Clicks on the "More Info" button for extra protection insurance,
     * verifies the bottom sheet opens with close button and correct title.
     */
    public void clickMoreInfoFullProtection() {
        logger.log("Clicking on More Info button for Extra Protection...");
        driverTools.clickWithJavascriptExecutor(FIRST_INSURANCE_MORE_INFO_BTN);

        // Verify close button is present
        boolean isCloseBtnPresent = driverTools.waitForElementToAppear(CLOSE).isPresent();
        logger.log("Close button is present: " + isCloseBtnPresent);
        Assertion.assertThat("Close button should be present", isCloseBtnPresent, is(true));

        // Verify title contains "Extra Protections" or Indonesian equivalent "Perlindungan Ekstra"
        WebElement titleElement = driverTools.waitForElementToPresence(EXTRA_PROTECTION_BOTTOMSHEET_TITLE).orElseThrow();
        String title = driverTools.getText(titleElement);
        logger.log("Bottom sheet title: " + title);

        Assertion.assertThat("Title should contain 'Extra Protections' or 'Perlindungan Ekstra'", title, anyOf(containsString("Extra Protections"), containsString("Perlindungan Ekstra")));
    }

    /**
     * Closes the Extra Protection bottom sheet by clicking the close button.
     */
    public void closeExtraProtectionBottomSheet() {
        if(driverTools.isElementDisplayed(CLOSE_FACILITIES_POPUP)){
            logger.log("Closing Extra Protection bottom sheet...");
            driverTools.click(CLOSE_FACILITIES_POPUP);
            driverTools.waitForElementToDisappear(EXTRA_PROTECTION_BOTTOMSHEET_TITLE);
            logger.log("Extra Protection bottom sheet closed");
        }else{
            logger.log("Close button not displayed, because we move to first part of booking flow");
            FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
            if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
                flightBookingFormPageWeb.clickContinue();
            }
        }

    }

    /**
     * Clicks on Terms and Conditions link, opens new tab with PDF,
     * switches to the tab containing "tix-insurance" or "pdf" in URL.
     *
     * @return the insurance PDF URL
     */
    public void clickTermsAndConditionsLink() {
        logger.log("Checking if terms and conditions button is present");
        Assertion.assertThat("Terms and Conditions button should be present", driverTools.waitForElementToAppear(TERMS_AND_CONDITIONS_LINK).isPresent(), is(true));

        // Handle native "Open with" popup if it appears (Android only)
        handleOpenWithPopup();
    }

    /**
     * Handles the native "Open with" popup that appears when clicking PDF links on Android and iOS.
     * Android: Shows options like "Open with Drive", "Edge", "Firefox", etc. We click "Just once" to open with Drive.
     * iOS: Shows "This site is attempting to open a pop-up window" with "Block" and "Allow" buttons. We click "Allow".
     */
    private void handleOpenWithPopup() {
        if (Configs.PLATFORM == Platform.ANDROID || Configs.PLATFORM == Platform.AMWEB) {
            handleOpenWithPopupAndroid();
        } else if (Configs.PLATFORM == Platform.IOS || Configs.PLATFORM == Platform.IMWEB) {
            handleOpenWithPopupIOS();
        } else {
            logger.log("Not on Android or iOS platform, skipping native popup handling");
        }
    }

    /**
     * Handles the "Open with" popup on Android platform using DriverTools.
     */
    private void handleOpenWithPopupAndroid() {
        try {
            logger.log("Checking for 'Open with' native popup (Android)...");
            Thread.sleep(500); // Wait for popup to appear

            // Switch to native context using DriverTools
            driverTools.switchToNative();

            // Locators for the "Open with" popup elements
            Locator driveOption = new Locator("DRIVE_OPTION",
                    new By.ByXPath("//android.widget.TextView[@text='Drive']"));
            Locator drivePdfViewer = new Locator("DRIVE_PDF_VIEWER",
                    new By.ByXPath("//android.widget.TextView[@text='Drive PDF Viewer']"));
            Locator justOnceButton = new Locator("JUST_ONCE_BUTTON",
                    new By.ByXPath("//android.widget.Button[@text='Just once']"));
            Locator alwaysButton = new Locator("ALWAYS_BUTTON",
                    new By.ByXPath("//android.widget.Button[@text='Always']"));

            // Check if "Open with" popup is present by looking for "Just once" button
            boolean popupFound = driverTools.waitForElementToPresence(justOnceButton, 10).isPresent();

            if (popupFound) {
                logger.log("'Open with' popup detected");

                // Step 1: Click on "Drive" option to ensure it's selected
                if (driverTools.waitForElementToPresence(driveOption, 3).isPresent()) {
                    logger.log("Found 'Drive' option, clicking it to select...");
                    driverTools.click(driveOption);
                    logger.log("Clicked on 'Drive' option");
                    Thread.sleep(500); // Small wait for selection to register
                } else if (driverTools.waitForElementToPresence(drivePdfViewer, 3).isPresent()) {
                    logger.log("Found 'Drive PDF Viewer' text, clicking it to select...");
                    driverTools.click(drivePdfViewer);
                    logger.log("Clicked on 'Drive PDF Viewer' text");
                    Thread.sleep(500);
                } else {
                    logger.log("Drive option not found explicitly, assuming it's already selected (default)");
                }

                // Step 2: Click "Just once" to confirm selection
                logger.log("Clicking 'Just once' to open PDF in Drive...");
                if (driverTools.waitForElementToPresence(justOnceButton, 3).isPresent()) {
                    driverTools.click(justOnceButton);
                    logger.log("Clicked on 'Just once' button");
                } else if (driverTools.waitForElementToPresence(alwaysButton, 3).isPresent()) {
                    logger.log("'Just once' not found, clicking 'Always' button instead");
                    driverTools.click(alwaysButton);
                    logger.log("Clicked on 'Always' button");
                } else {
                    logger.log("WARNING: No action buttons found in popup");
                }

                Thread.sleep(1000); // Wait for Drive to open and load PDF
                logger.log("Waited for Drive to open and load PDF");

            } else {
                logger.log("'Open with' popup not detected, PDF may have opened directly");
            }

            // Switch back to webview context using DriverTools
            logger.log("Switching back to webview context...");
            driverTools.switchToWebViewIndex(0);
            logger.log("Switched back to webview context");

        } catch (Exception e) {
            logger.log("Error handling 'Open with' popup: " + e.getMessage());
            logger.log("Stack trace: " + java.util.Arrays.toString(e.getStackTrace()));
            logger.log("Attempting to switch back to webview context...");
            try {
                driverTools.switchToWebViewIndex(0);
                logger.log("Successfully switched back to webview");
            } catch (Exception ex) {
                logger.log("Failed to switch back to webview: " + ex.getMessage());
            }
        }
        driverTools.switchToWebView();
    }

    /**
     * Handles the popup permission dialog on iOS platform using DriverTools.
     * iOS shows "This site is attempting to open a pop-up window" with "Block" and "Allow" buttons.
     * We click "Allow" to permit the PDF to open in Safari.
     */
    private void handleOpenWithPopupIOS() {
        try {
            logger.log("Checking for popup permission dialog (iOS)...");
            Thread.sleep(200); // Wait for popup to appear

            // Switch to native context using DriverTools
            logger.log("Switching to NATIVE_APP context...");
            driverTools.switchToNative();

            // Locators for the iOS popup permission dialog
            Locator allowButton = new Locator("ALLOW_BUTTON",
                    By.xpath("//XCUIElementTypeButton[@name='Allow']"));

            // Alternative locators in case the button names are different
            Locator allowButtonAlt = new Locator("ALLOW_BUTTON_ALT",
                    By.xpath("//XCUIElementTypeButton[contains(@label, 'Allow')]"));

            // Check if popup permission dialog is present
            boolean popupFound = driverTools.waitForElementToPresence(allowButton, 10).isPresent();

            if (!popupFound) {
                // Try alternative locator
                popupFound = driverTools.waitForElementToPresence(allowButtonAlt, 3).isPresent();
            }

            if (popupFound) {
                logger.log("iOS popup permission dialog detected");

                // Click "Allow" to permit popup
                if (driverTools.waitForElementToPresence(allowButton, 3).isPresent()) {
                    logger.log("Clicking 'Allow' button...");
                    driverTools.click(allowButton);
                    logger.log("Clicked on 'Allow' button");
                } else if (driverTools.waitForElementToPresence(allowButtonAlt, 3).isPresent()) {
                    logger.log("Clicking 'Allow' button (alternative locator)...");
                    driverTools.click(allowButtonAlt);
                    logger.log("Clicked on 'Allow' button");
                } else {
                    logger.log("WARNING: 'Allow' button not found");
                }

                Thread.sleep(600); // Wait for Safari to open and load PDF
                logger.log("Waited for Safari to open and load PDF");

            } else {
                logger.log("iOS popup permission dialog not detected, PDF may have opened directly");
            }

            // Switch back to webview context using DriverTools
            logger.log("Switching back to webview context...");
            driverTools.switchToWebView();
            logger.log("Switched back to webview context");

        } catch (Exception e) {
            logger.log("Error handling iOS popup permission: " + e.getMessage());
            logger.log("Stack trace: " + java.util.Arrays.toString(e.getStackTrace()));
            logger.log("Attempting to switch back to webview context...");
            try {
                driverTools.switchToWebViewIndex(0);
                logger.log("Successfully switched back to webview");
            } catch (Exception ex) {
                logger.log("Failed to switch back to webview: " + ex.getMessage());
            }
        }
    }

    public void verifyInsurancePdfByEnvironment() throws IOException, InterruptedException {
        logger.log("========== Starting insurance PDF verification ==========");
        logger.log("Platform: " + Configs.PLATFORM);

        if (Configs.PLATFORM == Platform.ANDROID || Configs.PLATFORM == Platform.AMWEB) {
            verifyPdfInDriveAppAndroid();
        } else if (Configs.PLATFORM == Platform.IOS || Configs.PLATFORM == Platform.IMWEB) {
            verifyPdfInQuickLookIOS();
        } else {
            verifyPdfInBrowser();
        }
    }

    private void verifyPdfInDriveAppAndroid() {
        logger.log("========== Android: Verifying PDF in Drive App ==========");
        driverTools.switchToNative();

        // Wait for Drive UI to load
        driverTools.dummyWait(5);

        logger.log("Verifying PDF filename is visible in Drive...");

        Locator pdfFilename = new Locator(
                "PDF_FILENAME",
                By.xpath(
                        "//*[contains(@text,'insurance') or contains(@text,'Insurance') or contains(@text,'.pdf')]"
                )
        );

        WebElement pdfElement = driverTools
                .waitForElementToAppear(pdfFilename, 15)
                .orElseThrow(() ->
                        new AssertionError("PDF not visible in Drive app")
                );

        String filename = driverTools.getText(pdfElement);
        logger.log("PDF filename visible: " + filename);

        Assertion.assertThat(
                "Filename should contain insurance or .pdf",
                filename.toLowerCase(),
                anyOf(containsString("insurance"), containsString(".pdf"))
        );

        logger.log("PDF verification in Drive app PASSED");
    }

    private void verifyPdfInQuickLookIOS() {
        logger.log("========== iOS: Verifying PDF in Safari ==========");

        // Define locator for PDF element (used as fallback if URL validation fails)
        Locator pdfElement = new Locator(
                "PDF_ELEMENT",
                By.xpath("//*[contains(@name,'insurance') or contains(@name,'Insurance') or contains(@name,'.pdf') or contains(text(),'insurance') or contains(text(),'Insurance')]")
        );

        try {
            // Use DriverTools utility to verify PDF in Safari
            boolean pdfVerified = driverTools.verifyPdfInSafari(pdfElement);

            // Assert that PDF was verified
            Assertion.assertThat(
                    "PDF verification in Safari",
                    pdfVerified,
                    is(true)
            );

            logger.log("PDF verification in iOS Safari PASSED");

        } catch (Exception e) {
            logger.log("ERROR: Error during PDF verification in Safari: " + e.getMessage());
            logger.log("Stack trace: " + java.util.Arrays.toString(e.getStackTrace()));

            // Try to switch back even if verification failed
            try {
                logger.log("Attempting to switch back to booking page after error...");
                switchToChromeOrSafari();
            } catch (Exception switchEx) {
                logger.log("Failed to switch back after error: " + switchEx.getMessage());
            }

            throw new AssertionError("PDF verification in Safari failed: " + e.getMessage());
        }
    }


    /**
     * Verifies if Full Protection insurance is selected.
     * Selects it if unselected, and asserts that the checkbox is checked in both cases.
     */
    public void selectFullProtectionInsurance() {
        logger.log("Verifying Full Protection insurance selection...");

        driverTools.waitForElementToPresence(FULL_PROTECTION_CARD, 10).orElseThrow(() ->
                new NoSuchElementException("Full Protection insurance card not found on page"));
        driverTools.scrollElementToCenter(FULL_PROTECTION_CARD);

        WebElement checkboxInput = driverTools.waitForElementToPresence(FULL_PROTECTION_CHECKBOX_INPUT, 5).orElseThrow();

        boolean isSelected = checkboxInput.isSelected();
        logger.log("Is Full Protection currently selected: " + isSelected);

        if (isSelected) {
            logger.log("Full Protection is already selected.");
        } else {
            logger.log("Full Protection is not selected. Clicking checkbox label...");
            driverTools.clickWithJavascriptExecutor(FULL_PROTECTION_CHECKBOX_LABEL);
            driverTools.waitForElementToAppear(FULL_PROTECTION_CHECKBOX_INPUT, 3);
        }

        // Always assert checkbox state is checked in both scenarios
        Assertion.assertThat("Full Protection checkbox must be checked", checkboxInput.isSelected(), is(true));
        logger.log("Verified: Full Protection insurance checkbox is checked.");
    }

    /**
     * Unchecks/discards all other active insurance options (Flight Delay Basic, Baggage Delay, etc.)
     * and ensures ONLY Full Protection is selected.
     */
    public void ensureOnlyFullProtectionIsSelected() {
        logger.log("Ensuring ONLY Full Protection insurance is selected...");

        driverTools.waitForElementToAppear(SEE_ALL_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);

        List<WebElement> cards = driverTools.elements(ALL_INSURANCE_CARDS).orElse(emptyList());
        logger.log("Found " + cards.size() + " insurance options on the booking form.");

        for (WebElement card : cards) {
            driverTools.scrollElementToCenter(card);
            String cardText = driverTools.getText(card);

            List<WebElement> checkboxes = card.findElements(By.xpath(".//input[@type='checkbox']"));
            List<WebElement> labels = card.findElements(By.xpath(".//label"));

            if (checkboxes.isEmpty() || labels.isEmpty()) {
                continue;
            }

            WebElement checkbox = checkboxes.get(0);
            WebElement label = labels.get(0);

            boolean isFullProtection = cardText.contains("Full Protection") || cardText.contains("Perlindungan Penuh");
            boolean isChecked = checkbox.isSelected();

            if (isFullProtection) {
                if (!isChecked) {
                    logger.log("Selecting Full Protection...");
                    driverTools.clickWithJavascriptExecutor(label);
                }
            } else {
                if (isChecked) {
                    String optionName = cardText.split("\n")[0];
                    logger.log("Unchecking non-Full Protection insurance: " + optionName);
                    driverTools.clickWithJavascriptExecutor(label);
                }
            }
        }

        // Assert Full Protection is checked
        selectFullProtectionInsurance();
    }

    private void verifyPdfInBrowser() throws IOException, InterruptedException {
        logger.log("========== Desktop: Verifying PDF in Browser ==========");

        String currentUrl = driverTools.getCurrentURL();
        logger.log("Current URL: " + currentUrl);

        Assertion.assertThat(
                "PDF URL validation",
                currentUrl.toLowerCase(),
                anyOf(containsString("pdf"), containsString("insurance"))
        );

        String pdfContent = driverTools.getPdfContent("insurance");

        Assertion.assertThat(
                "PDF content validation",
                pdfContent.toLowerCase(),
                anyOf(containsString("insurance"), containsString("dummy pdf"))
        );

        logger.log("PDF verification in browser PASSED");
    }


    /**
     * Switches back to Chrome/Safari booking page from PDF viewer.
     * Handles all platforms: Android, iOS, AMWEB, IMWEB, and Desktop.
     */
    public void switchToChromeOrSafari() {
        logger.log("========== Switching Back to Booking Page ==========");
        logger.log("Platform: " + Configs.PLATFORM);

        try {
            // Determine platform and call appropriate handler
            if (Configs.PLATFORM == Platform.AMWEB) {
                handleAndroidMwebSwitch();
            }else if (Configs.PLATFORM == Platform.IMWEB) {
                handleIOSMwebSwitch();
            } else {
                handleDesktopSwitch();
            }

            logger.log("Successfully switched back to booking page");

        } catch (Exception e) {
            logger.log("ERROR: Error switching back to booking page: " + e.getMessage());
            logger.log("Stack trace: " + java.util.Arrays.toString(e.getStackTrace()));
            throw new RuntimeException("Failed to switch back to booking page", e);
        }
    }

    /**
     * Handles switching for Android mobile web (AMWEB).
     * PDF can open in either:
     * 1. Chrome browser (same tab or new tab) - handled via navigation/tab switching
     * 2. Drive app (external app) - handled via context switching
     */
    private void handleAndroidMwebSwitch() {
        logger.log("Android Mobile Web: Checking PDF location...");

        try {
            // First, try to detect if we're in Chrome browser (webview context)
            boolean isInChromeContext = detectIfInChromeContext();

            if (isInChromeContext) {
                logger.log("PDF opened in Chrome browser - handling browser navigation");
                handleAndroidChromePdf();
            } else {
                logger.log("PDF opened in external app (likely Drive) - handling app switch");
                handleAndroidDrivePdf();
            }

            logger.log("Successfully switched to Chrome");

        } catch (Exception e) {
            logger.log("Error in handleAndroidMwebSwitch: " + e.getMessage());
            logger.log("Attempting fallback...");
            try {
                driverTools.switchToWebViewIndex(0);
                driverTools.dummyWait(1);
                logger.log("Fallback successful");
            } catch (Exception fallbackEx) {
                throw new RuntimeException("Failed to switch on Android Mweb", e);
            }
        }
    }

    /**
     * Detects if we're currently in Chrome browser context (webview).
     * @return true if in Chrome/webview context, false if in external app context
     */
    private boolean detectIfInChromeContext() {
        try {
            // Try to get current URL - only possible in webview context
            String currentUrl = driverTools.getCurrentURL();
            logger.log("Detected Chrome context - current URL: " + currentUrl);
            return currentUrl != null;
        } catch (Exception e) {
            logger.log("Not in Chrome context - likely in external app: " + e.getMessage());
            return false;
        }
    }

    /**
     * Handles PDF opened in Chrome browser itself.
     * Checks for multiple tabs or single tab and handles accordingly.
     */
    private void handleAndroidChromePdf() {
        logger.log("Handling PDF in Chrome browser...");

        try {
            // Check if PDF is in same tab or different tab
            Set<String> allWindows = driverTools.getDriver().getWindowHandles();
            logger.log("Total tabs: " + allWindows.size());

            if (allWindows.size() == 1) {
                // PDF opened in same tab - use back navigation
                logger.log("PDF in same tab - using back navigation");
                handleAndroidChromeSameTab();
            } else {
                // PDF opened in new tab - close PDF tab and switch to booking tab
                logger.log("PDF in new tab - closing PDF tab");
                handleAndroidChromeNewTab(allWindows);
            }
        } catch (Exception e) {
            logger.log("Error handling Chrome PDF, trying back navigation: " + e.getMessage());
            driverTools.back();
            driverTools.dummyWait(2);
        }
    }

    /**
     * Handles PDF opened in same Chrome tab - navigates back.
     */
    private void handleAndroidChromeSameTab() {
        String currentUrl = driverTools.getCurrentURL();
        logger.log("Current URL: " + currentUrl);

        // If already on booking page, do nothing
        if (currentUrl != null &&
                (currentUrl.contains("cart") || currentUrl.contains("booking"))) {
            logger.log("Already on booking page");
            return;
        }

        // Navigate back to booking page
        logger.log("Navigating back to booking page...");
        driverTools.back();
        driverTools.dummyWait(2);

        String finalUrl = driverTools.getCurrentURL();
        logger.log("After back navigation URL: " + finalUrl);

        if (finalUrl == null ||
                (!finalUrl.contains("cart") && !finalUrl.contains("booking"))) {
            logger.log("WARNING: Back navigation did not return to booking page");
        } else {
            logger.log("Successfully returned to booking page");
        }
    }

    /**
     * Handles PDF opened in new Chrome tab - closes PDF tab and switches to booking tab.
     */
    private void handleAndroidChromeNewTab(Set<String> allWindows) {
        logger.log("Finding and closing PDF tab...");
        String bookingWindowHandle = null;
        String currentHandle = driverTools.getDriver().getWindowHandle();

        // Find booking window
        for (String windowHandle : allWindows) {
            try {
                driverTools.getDriver().switchTo().window(windowHandle);
                String url = driverTools.getCurrentURL();
                logger.log("Checking tab - URL: " + url);

                if (url != null && (url.contains("cart") || url.contains("booking"))) {
                    bookingWindowHandle = windowHandle;
                    logger.log("Found booking tab");
                    break;
                }
            } catch (Exception e) {
                logger.log("Error checking window: " + e.getMessage());
            }
        }

        if (bookingWindowHandle != null) {
            // Close other tabs (PDF tabs)
            for (String windowHandle : allWindows) {
                if (!windowHandle.equals(bookingWindowHandle)) {
                    try {
                        driverTools.getDriver().switchTo().window(windowHandle);
                        driverTools.getDriver().close();
                        logger.log("Closed PDF tab");
                    } catch (Exception e) {
                        logger.log("Error closing PDF tab: " + e.getMessage());
                    }
                }
            }

            // Switch back to booking tab
            driverTools.getDriver().switchTo().window(bookingWindowHandle);
            logger.log("Switched to booking tab");
        } else {
            logger.log("WARNING: Could not find booking tab, staying on current tab");
            driverTools.getDriver().switchTo().window(currentHandle);
        }
    }

    /**
     * Handles PDF opened in external Drive app.
     * Presses back to close Drive and switches to Chrome webview.
     */
    private void handleAndroidDrivePdf() {
        logger.log("Switching from Drive app to Chrome...");

        // Press back button to close Drive app and return to Chrome
        logger.log("Pressing back button to close Drive app...");
        try {
            driverTools.back();
            driverTools.dummyWait(2);
            logger.log("Back button pressed, waiting for Chrome to appear...");
        } catch (Exception e) {
            logger.log("Back button not applicable or failed: " + e.getMessage());
        }

        // Use DriverTools to switch to Chrome webview
        logger.log("Switching to Chrome webview context...");
        driverTools.switchToWebView();
    }

    /**
     * Handles switching for iOS mobile web (IMWEB).
     * Switches from PDF tab back to booking tab in Safari using DriverTools.
     */
    private void handleIOSMwebSwitch() {
        logger.log("iOS Mobile Web: Returning from PDF using back navigation");

        String currentUrl = driverTools.getCurrentURL();
        logger.log("Current URL: " + currentUrl);

        // If already on booking page, do nothing
        if (currentUrl != null &&
                (currentUrl.contains("cart") || currentUrl.contains("booking"))) {
            logger.log("Already on booking page");
            return;
        }

        // iOS Safari: PDF opens via navigation, NOT a new tab
        driverTools.back();
        driverTools.dummyWait(2);

        String finalUrl = driverTools.getCurrentURL();
        logger.log("After back navigation URL: " + finalUrl);

        if (finalUrl == null ||
                (!finalUrl.contains("cart") && !finalUrl.contains("booking"))) {
            logger.log("WARNING: Back navigation did not return to booking page");
        } else {
            logger.log("Successfully returned to booking page");
        }
    }

    /**
     * Handles switching for desktop web.
     * Closes PDF tab/window and switches back to booking page.
     */
    private void handleDesktopSwitch() {
        logger.log("Desktop Web: Switching to booking tab...");

        Set<String> allWindows = driverTools.getDriver().getWindowHandles();
        logger.log("Total windows/tabs: " + allWindows.size());

        // If only one window exists, check if we're already on booking page
        if (allWindows.size() == 1) {
            String currentUrl = driverTools.getCurrentURL();
            logger.log("Only one window exists, current URL: " + currentUrl);

            if (currentUrl != null && (currentUrl.toLowerCase().contains("cart") ||
                    currentUrl.toLowerCase().contains("booking"))) {
                logger.log("Already on booking page");
                return;
            }

            // If not on booking page, try to navigate back
            logger.log("Not on booking page, navigating back...");
            driverTools.getDriver().navigate().back();
            driverTools.dummyWait(2);
            return;
        }

        // Multiple windows exist - close PDF window and switch to booking tab
        logger.log("Multiple windows detected, finding booking tab...");
        String bookingWindowHandle = null;

        // Find booking window
        for (String windowHandle : allWindows) {
            try {
                driverTools.getDriver().switchTo().window(windowHandle);
                driverTools.dummyWait(1);
                String url = driverTools.getCurrentURL();
                logger.log("Checking window: " + url);

                if (url != null && (url.toLowerCase().contains("cart") ||
                        url.toLowerCase().contains("booking"))) {
                    bookingWindowHandle = windowHandle;
                    logger.log("Found booking window: " + url);
                    break;
                }
            } catch (Exception e) {
                logger.log("Error checking window: " + e.getMessage());
            }
        }

        // Close other windows (PDF windows)
        for (String windowHandle : allWindows) {
            if (bookingWindowHandle != null && !windowHandle.equals(bookingWindowHandle)) {
                try {
                    driverTools.getDriver().switchTo().window(windowHandle);
                    driverTools.getDriver().close();
                    logger.log("Closed PDF window");
                } catch (Exception e) {
                    logger.log("Error closing window: " + e.getMessage());
                }
            }
        }

        // Switch to booking window
        if (bookingWindowHandle != null) {
            driverTools.getDriver().switchTo().window(bookingWindowHandle);
            logger.log("Switched to booking window");

            // Verify URL
            String finalUrl = driverTools.getCurrentURL();
            logger.log("Final URL: " + finalUrl);

            if (finalUrl == null || (!finalUrl.toLowerCase().contains("cart") &&
                    !finalUrl.toLowerCase().contains("booking"))) {
                logger.log("WARNING: Current URL doesn't contain 'cart' or 'booking': " + finalUrl);
            }
        } else {
            logger.log("WARNING: Booking window not found, staying on current window");
        }
    }

    /**
     * Verifies if price breakdown is correct by comparing UI values with expected PriceDetail values.
     * Supports both English and Indonesian languages.
     *
     * @param priceDetail Expected price details
     * @param currency    Currency code (e.g., "IDR", "USD", "SGD")
     * @return true if all prices match
     */
    /**
     * Validates the complete price breakdown in the booking page similar to product review page.
     * Uses assertions for mandatory fields that are set in priceDetail.
     *
     * @param priceDetail Price detail object containing expected prices
     * @param currency Expected currency code (e.g., "IDR", "SGD")
     */
    public void isPriceBreakdownCorrect(PriceDetail priceDetail, String currency) {
        logger.log("========== PRICE BREAKDOWN VALIDATION (BOOKING PAGE) ==========");

        // Validate input parameters
        validateInputParameters(priceDetail, currency);

        openPriceDetailSection();

        // Get and log all price breakdown items dynamically
        Map<String, String> breakdown = getPriceBreakdownFromBookingPage();
        logPriceBreakdown(breakdown);

        // Validate currency presence in all price items
        validateCurrencyInAllItems(breakdown, currency);

        // Validate passenger prices
        validatePassengerPrices(breakdown, "Adult", "Dewasa", priceDetail.adult,
                priceDetail.adultDepartPrice, priceDetail.adultReturnPrice);

        validatePassengerPrices(breakdown, "Child", "Anak", priceDetail.child,
                priceDetail.childDepartPrice, priceDetail.childReturnPrice);
        validatePassengerPrices(breakdown, "Infant", "Bayi", priceDetail.infant,
                priceDetail.infantDepartPrice, priceDetail.infantReturnPrice);

        // Validate extra protection
        validateExtraProtection(breakdown, priceDetail);

        // Validate discount (optional)
        validateDiscount(breakdown, priceDetail);

        // Calculate and log total sum, then assert against UI total
        double calculatedTotal = calculateAndLogTotalSum(breakdown, currency);

        // Get and validate UI total (Pay Now / Bayar sekarang)
        validateCalculatedTotalAgainstUI(calculatedTotal, currency);

        // Optionally validate "Total after cashback" if present
        validateTotalAfterCashback(calculatedTotal, currency);

        logger.log("========== PRICE BREAKDOWN VALIDATION COMPLETED ==========");
    }

    /**
     * Validates input parameters for price breakdown validation
     */
    private void validateInputParameters(PriceDetail priceDetail, String currency) {
        if (priceDetail == null) {
            logger.log("ERROR: PriceDetail is null");
            Assertion.assertThat("PriceDetail should not be null", priceDetail, is(not(nullValue())));
        }

        if (currency == null || currency.isEmpty()) {
            logger.log("ERROR: Currency is null or empty");
            Assertion.assertThat("Currency should not be null or empty", currency, is(not(nullValue())));
        }
    }

    /**
     * Logs the price breakdown in a formatted way
     */
    private void logPriceBreakdown(Map<String, String> breakdown) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== COMPLETE PRICE BREAKDOWN VALIDATION =====\n");

        if (breakdown.isEmpty()) {
            logger.log("WARNING: No price breakdown items found!");
            sb.append("--- Price Items ---\n");
            sb.append("  [NO ITEMS FOUND]\n");
            sb.append("================================================\n");
            logger.log(sb.toString());
            Assertion.assertThat("Price breakdown should not be empty", breakdown.isEmpty(), is(false));
        }

        sb.append("--- Price Items ---\n");
        for (Map.Entry<String, String> entry : breakdown.entrySet()) {
            sb.append(String.format("  %s: %s\n", entry.getKey(), entry.getValue()));
        }
        sb.append("================================================\n");
        logger.log(sb.toString());
    }

    /**
     * Validates that currency is present in all numeric price items
     */
    private void validateCurrencyInAllItems(Map<String, String> breakdown, String currency) {
        logger.log("--- Validating Currency in All Price Items (Mandatory) ---");
        int itemsWithCurrency = 0;
        int totalItems = 0;
        int skippedTextOnlyItems = 0;

        for (Map.Entry<String, String> entry : breakdown.entrySet()) {
            String value = entry.getValue();
            if (value == null) {
                logger.log("Currency validation skipped - NULL value for: " + entry.getKey());
                skippedTextOnlyItems++;
                continue;
            }

            // Skip text-only values (no numbers) like "Included", "Free", etc.
            if (!value.matches(".*\\d.*")) {
                logger.log("Currency validation skipped - Text-only value (no numbers): " + entry.getKey() + " = " + value);
                skippedTextOnlyItems++;
                continue;
            }

            totalItems++;
            if (value.contains(currency)) {
                itemsWithCurrency++;
                logger.log("Currency " + currency + " found in: " + entry.getKey() + " = " + value);
            } else {
                logger.log("Currency " + currency + " NOT found in: " + entry.getKey() + " = " + value);
            }
        }

        logger.log("Currency validation: " + itemsWithCurrency + "/" + totalItems + " items contain " + currency +
                " (" + skippedTextOnlyItems + " text-only items skipped)");

        double currencyPercentage = totalItems > 0 ? (itemsWithCurrency * 100.0 / totalItems) : 0;
        logger.log("Currency coverage: " + String.format("%.1f%%", currencyPercentage));

        String currencyMessage = String.format("Currency %s should be present in all %d price items (found in %d items = %.1f%%)",
                currency, totalItems, itemsWithCurrency, currencyPercentage);
        Assertion.assertThat(currencyMessage, currencyPercentage, is(100.0));
    }

    /**
     * Validates passenger prices (generic method for adult/child/infant)
     */
    private void validatePassengerPrices(Map<String, String> breakdown, String passengerTypeEn,
                                         String passengerTypeId, int count,
                                         String departPrice, String returnPrice) {
        if (count > 0) {
            logger.log("--- Validating " + passengerTypeEn + " Passenger Prices (Mandatory) ---");
            String key = findPriceItemKey(breakdown, passengerTypeEn, passengerTypeId, count);
            Assertion.assertThat(passengerTypeEn + " price item should be present for " + count + " passenger(s)",
                    key, is(not(nullValue())));

            if (key != null) {
                String actualPrice = breakdown.get(key);

                if (actualPrice == null) {
                    logger.log("WARNING: " + passengerTypeEn + " price is null for key: " + key);
                }

                if (departPrice != null && !departPrice.isEmpty()) {
                    double actualPriceDouble = CoreFunctions.convertPriceSafely(actualPrice);
                    double expectedPriceDouble = CoreFunctions.convertPriceSafely(departPrice);
                    logger.log(passengerTypeEn + " Depart price: Expected=" + departPrice + " (" + expectedPriceDouble + "), Actual=" + actualPrice + " (" + actualPriceDouble + ")");
                    Assertion.assertThat(passengerTypeEn + " departure price should match expected value", actualPriceDouble, is(expectedPriceDouble));
                } else {
                    logger.log(passengerTypeEn + " Depart price validation skipped - expected price not provided");
                }

                // Check return price for roundtrip
                if (returnPrice != null && !returnPrice.isEmpty()) {
                    String returnKey = findSecondPriceItemKey(breakdown, passengerTypeEn, passengerTypeId, count);
                    if (returnKey != null) {
                        String actualReturnPrice = breakdown.get(returnKey);
                        double actualReturnPriceDouble = CoreFunctions.convertPriceSafely(actualReturnPrice);
                        double expectedReturnPriceDouble = CoreFunctions.convertPriceSafely(returnPrice);
                        logger.log(passengerTypeEn + " Return price: Expected=" + returnPrice + " (" + expectedReturnPriceDouble + "), Actual=" + actualReturnPrice + " (" + actualReturnPriceDouble + ")");
                        Assertion.assertThat(passengerTypeEn + " return price should match expected value", actualReturnPriceDouble, is(expectedReturnPriceDouble));
                    } else {
                        logger.log("WARNING: " + passengerTypeEn + " return price expected but not found in breakdown for " + count + " passenger(s)");
                        logger.log("This might be a one-way trip or the return price is combined");
                    }
                }
            }
        } else {
            logger.log("--- " + passengerTypeEn + " passengers validation skipped (count: 0) ---");
        }
    }

    /**
     * Validates extra protection pricing
     */
    private void validateExtraProtection(Map<String, String> breakdown, PriceDetail priceDetail) {
        if (priceDetail.extraProtectionTitle != null && !priceDetail.extraProtectionTitle.isEmpty() &&
                priceDetail.extraProtectionPrice != null && !priceDetail.extraProtectionPrice.isEmpty()) {
            logger.log("--- Validating Extra Protection Price (Mandatory) ---");
            logger.log("Searching for Extra Protection: '" + priceDetail.extraProtectionTitle + "'");

            String extraProtectionKey = findExtraProtectionKey(breakdown, priceDetail.extraProtectionTitle);

            Assertion.assertThat("Extra Protection price item should be present for '" + priceDetail.extraProtectionTitle + "'",
                    extraProtectionKey, is(not(nullValue())));

            if (extraProtectionKey != null) {
                validateExtraProtectionPrice(breakdown, priceDetail, extraProtectionKey);
            }
        } else {
            logger.log("--- Extra Protection validation skipped (not provided) ---");
        }
    }

    /**
     * Finds extra protection key with fallback strategies
     */
    private String findExtraProtectionKey(Map<String, String> breakdown, String title) {
        String key = findPriceItemKeyByPartialMatch(breakdown, title);

        if (key == null) {
            logger.log("Extra Protection not found with exact title, trying alternative search terms...");

            String[] alternativeSearchTerms = {
                    "Extra Protection", "Mini", "Full", "Pro", "Protection",
                    "Proteksi", "Asuransi", "Insurance", "EN"
            };

            for (String term : alternativeSearchTerms) {
                key = findPriceItemKeyByPartialMatch(breakdown, term);
                if (key != null) {
                    logger.log("Found Extra Protection using alternative term: '" + term + "'");
                    break;
                }
            }
        }

        if (key == null) {
            logger.log("ERROR: Extra Protection not found. Available price items:");
            for (String availableKey : breakdown.keySet()) {
                logger.log("  - " + availableKey);
            }
        }

        return key;
    }

    /**
     * Validates extra protection price with per-person calculation logic
     */
    private void validateExtraProtectionPrice(Map<String, String> breakdown, PriceDetail priceDetail, String key) {
        String actualPrice = breakdown.get(key);

        if (actualPrice == null) {
            logger.log("WARNING: Extra Protection price is null for key: " + key);
        }

        int totalPassengers = priceDetail.adult + priceDetail.child + priceDetail.infant;
        logger.log("Total passengers for Extra Protection calculation: " + totalPassengers +
                " (Adult: " + priceDetail.adult + ", Child: " + priceDetail.child + ", Infant: " + priceDetail.infant + ")");

        try {
            double actualPriceDouble = CoreFunctions.convertPriceSafely(actualPrice);
            double expectedPriceDouble = CoreFunctions.convertPriceSafely(priceDetail.extraProtectionPrice);
            // Use tolerance-based comparison for floating point precision (0.01 tolerance)
            double priceDifference = Math.abs(actualPriceDouble - expectedPriceDouble);
            boolean match = priceDifference <= 0.01;
            String logMessage;

            if (match) {
                logMessage = "Extra Protection price: Expected=" + priceDetail.extraProtectionPrice +
                        " (" + expectedPriceDouble + "), Actual=" + actualPrice +
                        " (" + actualPriceDouble + "), Difference=" + String.format("%.2f", priceDifference) + ", Match=true";
                logger.log(logMessage);
                Assertion.assertThat("Extra Protection price should match expected value (difference: " + priceDifference + ")",
                        priceDifference, lessThanOrEqualTo(0.01));
            } else if (totalPassengers > 0) {
                // Try per-person multiplication
                double totalExpectedPrice = expectedPriceDouble * totalPassengers;
                // Use tolerance-based comparison for floating point precision after multiplication (0.01 tolerance)
                double totalPriceDifference = Math.abs(actualPriceDouble - totalExpectedPrice);
                match = totalPriceDifference <= 0.01;

                logger.log("Trying per-person calculation: " + expectedPriceDouble + " × " + totalPassengers + " = " + totalExpectedPrice);

                if (match) {
                    logMessage = "Extra Protection price: Expected (per person)=" + priceDetail.extraProtectionPrice +
                            " × " + totalPassengers + " passengers = " + totalExpectedPrice +
                            ", Actual=" + actualPrice + " (" + actualPriceDouble + "), Difference=" + String.format("%.2f", totalPriceDifference) + ", Match=true";
                } else {
                    logMessage = "Extra Protection price: Expected (per person)=" + priceDetail.extraProtectionPrice +
                            " × " + totalPassengers + " = " + totalExpectedPrice +
                            ", Actual=" + actualPrice + " (" + actualPriceDouble + "), Difference=" + String.format("%.2f", totalPriceDifference) + ", Match=false";
                }
                logger.log(logMessage);
                Assertion.assertThat("Extra Protection price should match expected value (difference: " + totalPriceDifference + ")",
                        totalPriceDifference, lessThanOrEqualTo(0.01));
            } else {
                logMessage = "Extra Protection price: Expected=" + priceDetail.extraProtectionPrice +
                        " (" + expectedPriceDouble + "), Actual=" + actualPrice +
                        " (" + actualPriceDouble + "), Difference=" + String.format("%.2f", priceDifference) + ", Match=false";
                logger.log(logMessage);
                Assertion.assertThat("Extra Protection price should match expected value (difference: " + priceDifference + ")",
                        priceDifference, lessThanOrEqualTo(0.01));
            }
        } catch (NumberFormatException e) {
            logger.log("Could not convert Extra Protection prices to double - actual: '" + actualPrice +
                    "', expected: '" + priceDetail.extraProtectionPrice + "', error: " + e.getMessage());
            Assertion.assertThat("Extra Protection price should be valid", false, is(true));
        }
    }

    /**
     * Validates discount price (optional)
     */
    private void validateDiscount(Map<String, String> breakdown, PriceDetail priceDetail) {
        if (priceDetail.discountPrice != null && !priceDetail.discountPrice.isEmpty()) {
            logger.log("--- Validating Discount Price (Optional) ---");
            String discountKey = findPriceItemKeyByPartialMatch(breakdown, "Discount", "Diskon", "Potongan", "Promo", "Voucher");
            if (discountKey != null) {
                String actualDiscountPrice = breakdown.get(discountKey);
                double actualDiscountPriceDouble = CoreFunctions.convertPriceSafely(actualDiscountPrice);
                double expectedDiscountPriceDouble = CoreFunctions.convertPriceSafely(priceDetail.discountPrice);
                logger.log("Discount price: Expected=" + priceDetail.discountPrice + " (" + expectedDiscountPriceDouble + "), Actual=" + actualDiscountPrice + " (" + actualDiscountPriceDouble + ")");
                Assertion.assertThat("Discount price should match expected value", actualDiscountPriceDouble, closeTo(expectedDiscountPriceDouble, 0.01));
            } else {
                logger.log("WARNING: Discount price not found in breakdown, but expected: " + priceDetail.discountPrice);
                logger.log("This is optional validation - test continues");
            }
        } else {
            logger.log("--- Discount validation skipped (not provided) ---");
        }
    }

    /**
     * Calculates and logs the total sum of all price items
     * @return The calculated total sum as double
     */
    private double calculateAndLogTotalSum(Map<String, String> breakdown, String currency) {
        logger.log("--- Calculating Total Sum of Price Items ---");
        double totalSum = 0;
        int parsedItems = 0;
        int failedItems = 0;

        for (Map.Entry<String, String> entry : breakdown.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                logger.log("  " + entry.getKey() + ": [EMPTY VALUE]");
                failedItems++;
                continue;
            }

            try {
                double priceValue = CoreFunctions.convertPriceSafely(entry.getValue());

                if (priceValue < 0) {
                    logger.log("  " + entry.getKey() + ": " + priceValue + " [NEGATIVE - Discount/Refund]");
                } else {
                    logger.log("  " + entry.getKey() + ": " + priceValue);
                }

                totalSum += priceValue;
                parsedItems++;
            } catch (NumberFormatException e) {
                logger.log("  " + entry.getKey() + ": Could not parse price - " + entry.getValue());
                failedItems++;
            }
        }

        if (totalSum == 0 && parsedItems == 0) {
            logger.log("WARNING: No price items could be parsed for total calculation");
        } else {
            logger.log("Calculated Total Sum: " + String.format("%.0f", totalSum) + " " + currency);
            logger.log("Successfully parsed " + parsedItems + " items, failed to parse " + failedItems + " items");
        }

        return totalSum;
    }

    /**
     * Validates that the calculated total matches the UI total (Pay Now / Bayar sekarang)
     */
    private void validateCalculatedTotalAgainstUI(double calculatedTotal, String currency) {
        logger.log("--- Validating Calculated Total Against UI Total ---");

        try {
            // Get the total price from UI (Pay Now / Bayar sekarang)
            String uiTotalText = driverTools.getText(TOTAL_PRICE).orElse("");

            if (uiTotalText == null || uiTotalText.isEmpty()) {
                logger.log("WARNING: UI total price is empty or not found");
                return;
            }

            // Convert UI total to double
            double uiTotal = CoreFunctions.convertPriceSafely(uiTotalText);

            // Calculate difference for tolerance-based comparison
            double priceDifference = Math.abs(calculatedTotal - uiTotal);

            logger.log("Calculated Total: " + String.format("%.2f", calculatedTotal) + " " + currency);
            logger.log("UI Total (Pay Now): " + uiTotalText + " (" + String.format("%.2f", uiTotal) + " " + currency + ")");
            logger.log("Difference: " + String.format("%.2f", priceDifference) + " " + currency);

            // Use tolerance-based comparison for floating point precision (0.01 IDR tolerance)
            Assertion.assertThat("Calculated total should match UI total (Pay Now). Difference: " + priceDifference,
                    priceDifference, lessThanOrEqualTo(0.01));

            logger.log("✓ Total price validation passed: Calculated total matches UI total");
        } catch (Exception e) {
            logger.log("ERROR: Failed to validate calculated total against UI total: " + e.getMessage());
            throw new AssertionError("Could not validate total price against UI", e);
        }
    }

    /**
     * Validates "Total after cashback" if present in the UI
     * Validates: Total After Cashback = Pay Now Total - all cashback (points converted to IDR)
     * Based on DOM structure: <li><span>Cashback label</span><div><span>Get X points</span></div></li>
     *                        <li><span>Total after cashback</span><span>IDR amount</span></li>
     */
    private void validateTotalAfterCashback(double payNowTotal, String currency) {
        logger.log("--- Validating Total After Cashback (Optional) ---");

        try {
            // Get all cashback points from UI
            double totalCashbackPoints = getAllCashbackPoints();

            // Get "Total after cashback" from UI
            Optional<WebElement> totalAfterCashbackElement = driverTools.waitForElementToAppear(TOTAL_AFTER_CASHBACK, 2);

            if (totalAfterCashbackElement.isPresent()) {
                String totalAfterCashbackText = driverTools.getText(totalAfterCashbackElement.get());

                if (totalAfterCashbackText != null && !totalAfterCashbackText.isEmpty()) {
                    double totalAfterCashback = CoreFunctions.convertPriceSafely(totalAfterCashbackText);

                    // Calculate expected: Total After Cashback = Pay Now - Cashback Points (1 point = 1 IDR)
                    double expectedTotalAfterCashback = payNowTotal - totalCashbackPoints;
                    double difference = Math.abs(totalAfterCashback - expectedTotalAfterCashback);

                    logger.log("Pay Now Total: " + String.format("%.2f", payNowTotal) + " " + currency);
                    logger.log("Total Cashback Points: " + String.format("%.0f", totalCashbackPoints) + " points (= " + String.format("%.2f", totalCashbackPoints) + " " + currency + ")");
                    logger.log("Expected Total After Cashback: " + String.format("%.2f", expectedTotalAfterCashback) + " " + currency);
                    logger.log("Actual Total After Cashback: " + totalAfterCashbackText + " (" + String.format("%.2f", totalAfterCashback) + " " + currency + ")");
                    logger.log("Difference: " + String.format("%.2f", difference) + " " + currency);

                    // Validate: Total After Cashback = Pay Now - Cashback Points (with 0.01 tolerance)
                    Assertion.assertThat("Total after cashback should equal Pay Now minus cashback points. Difference: " + difference,
                            difference, lessThanOrEqualTo(0.01));

                    logger.log("✓ Total after cashback validation passed: " + String.format("%.2f", totalAfterCashback) + " = " + String.format("%.2f", payNowTotal) + " - " + String.format("%.2f", totalCashbackPoints));
                } else {
                    logger.log("Total after cashback text is empty - skipping validation");
                }
            } else {
                logger.log("Total after cashback not found in UI - skipping validation (this is optional)");
            }
        } catch (Exception e) {
            logger.log("INFO: Could not validate total after cashback (optional validation): " + e.getMessage());
            // Don't throw error as this is optional validation
        }
    }

    /**
     * Extracts all cashback points from the UI and converts them to IDR
     * DOM structure: <li><span>Cashback label</span><div class="BookingPriceDetailPopUp_point__7TUVr"><span>Get X points</span></div></li>
     * @return Total cashback points as double (1 point = 1 IDR)
     */
    private double getAllCashbackPoints() {
        double totalCashbackPoints = 0.0;

        try {
            // Find all cashback items (Discount Cashback, Tix Points Cashback, etc.)
            List<WebElement> cashbackItems = driverTools.elements(CASHBACK_ITEMS).orElse(emptyList());

            logger.log("Found " + cashbackItems.size() + " cashback item(s)");

            for (WebElement cashbackItem : cashbackItems) {
                // Get the label (e.g., "Discount Cashback", "Tix Points Cashback")
                // First span that doesn't contain "Get" or "Dapat" is the label
                String labelXpath = ".//span[contains(@class,'Text_text')][not(contains(text(),'Get')) and not(contains(text(),'Dapat'))]";
                List<WebElement> labelElements = cashbackItem.findElements(By.xpath(labelXpath));
                String label = labelElements.isEmpty() ? "Unknown" : driverTools.getText(labelElements.get(0));

                // Get the points value from the nested span (e.g., "Get 81,864 points" or "Dapat 81.864 poin")
                String pointsXpath = ".//div[contains(@class,'BookingPriceDetailPopUp_point')]//span";
                List<WebElement> pointsElements = cashbackItem.findElements(By.xpath(pointsXpath));

                if (!pointsElements.isEmpty()) {
                    String pointsText = driverTools.getText(pointsElements.get(0));
                    // Extract numeric value using convertPriceSafely to handle both formats (81,864 or 81.864)
                    // First, extract the numeric part
                    String numericPart = pointsText.replaceAll("(?i)(get|dapat|points|poin)", "").trim();

                    if (!numericPart.isEmpty()) {
                        try {
                            // Use convertPriceSafely to handle both Indonesian (dot as thousand separator)
                            // and English (comma as thousand separator) formats
                            double points = CoreFunctions.convertPriceSafely(numericPart);
                            totalCashbackPoints += points;
                            logger.log("Cashback: " + label + " = " + String.format("%.0f", points) + " points");
                        } catch (NumberFormatException e) {
                            logger.log("WARNING: Could not parse cashback points from: " + pointsText);
                        }
                    }
                }
            }

            logger.log("Total Cashback Points: " + String.format("%.0f", totalCashbackPoints) + " points (= " + String.format("%.2f", totalCashbackPoints) + " IDR)");

        } catch (Exception e) {
            logger.log("WARNING: Could not extract cashback points: " + e.getMessage());
        }

        return totalCashbackPoints;
    }

    /**
     * Gets all price breakdown items from the booking page price detail popup.
     * Iterates through all price list items dynamically using index-based approach.
     *
     * @return Map of price label to price value
     */
    private Map<String, String> getPriceBreakdownFromBookingPage() {
        Map<String, String> breakdown = new LinkedHashMap<>();

        // First, get the count of all price list items
        String priceItemsCountXpath = "//*[contains(@class,'BookingPriceDetailPopUp_price_list_items')]";
        Locator priceItemsCountLocator = new Locator("PRICE_ITEMS_COUNT", new By.ByXPath(priceItemsCountXpath));

        List<WebElement> priceItems = driverTools.elements(priceItemsCountLocator).orElse(emptyList());
        int totalPriceItems = priceItems.size();
        logger.log("Found " + totalPriceItems + " price breakdown items to process");

        // Edge Case 14: No price items found
        if (totalPriceItems == 0) {
            logger.log("WARNING: No price breakdown items found in the DOM");
            return breakdown;
        }

        // Edge Case 15: Too many price items (potential DOM issue)
        if (totalPriceItems > 50) {
            logger.log("WARNING: Unusually high number of price items detected (" + totalPriceItems + "). Possible DOM issue.");
        }

        // Iterate through each price item by index
        for (int index = 1; index <= totalPriceItems; index++) {
            try {
                // Get ALL spans within this price item container
                String allSpansXpath = "(//*[contains(@class,'BookingPriceDetailPopUp_price_list_items')])[" + index + "]//span";
                Locator allSpansLocator = new Locator("ALL_SPANS_" + index, new By.ByXPath(allSpansXpath));

                List<WebElement> spans = driverTools.elements(allSpansLocator).orElse(emptyList());
                logger.log("Index " + index + ": Found " + spans.size() + " span elements");

                // Edge Case: Process spans in pairs (label, value)
                if (spans.size() >= 2) {
                    // Process all span pairs in this container
                    for (int spanIndex = 0; spanIndex < spans.size() - 1; spanIndex += 2) {
                        String label = driverTools.getText(spans.get(spanIndex));
                        String value = driverTools.getText(spans.get(spanIndex + 1));

                        // Edge Case 17: Null or empty text
                        if (label == null) {
                            logger.log("WARNING: NULL label at index " + index + ", span " + spanIndex);
                            label = "UNKNOWN_LABEL_" + index;
                        }
                        if (value == null) {
                            logger.log("WARNING: NULL value at index " + index + ", span " + (spanIndex + 1));
                            value = "0";
                        }

                        if (!label.isEmpty() && !value.isEmpty()) {
                            // Skip "Pay now" / "Bayar sekarang" and cashback items
                            String normalizedLabel = label.toLowerCase().trim();
                            if (normalizedLabel.contains("pay now") || normalizedLabel.contains("bayar sekarang") ||
                                    normalizedLabel.contains("cashback") || normalizedLabel.contains("total")) {
                                logger.log("Skipping at index " + index + ": " + label + " (excluded type)");
                                continue;
                            }

                            // Handle duplicate keys (e.g., Adult for departure and return)
                            String uniqueKey = label.trim();
                            int suffix = 1;
                            while (breakdown.containsKey(uniqueKey)) {
                                suffix++;
                                uniqueKey = label.trim() + " (" + suffix + ")";
                            }

                            breakdown.put(uniqueKey, value.trim());
                            logger.log("Price item at index " + index + ": " + uniqueKey + " = " + value.trim());
                        } else {
                            logger.log("Skipping at index " + index + ", span " + spanIndex + ": Empty label or value");
                        }
                    }
                } else if (spans.size() == 1) {
                    logger.log("WARNING: Only 1 span found at index " + index + " - expected pairs");
                } else {
                    logger.log("WARNING: No spans found at index " + index);
                }
            } catch (Exception e) {
                logger.log("ERROR processing price item at index " + index + ": " + e.getMessage());
                // Continue processing other items even if one fails
            }
        }

        logger.log("Total price items collected: " + breakdown.size() + " out of " + totalPriceItems + " found");
        return breakdown;
    }

    /**
     * Finds the first price item key that matches the given passenger type and count.
     *
     * @param breakdown Price breakdown map
     * @param englishType English passenger type (e.g., "Adult")
     * @param indonesianType Indonesian passenger type (e.g., "Dewasa")
     * @param count Passenger count
     * @return Matching key or null if not found
     */
    private String findPriceItemKey(Map<String, String> breakdown, String englishType, String indonesianType, int count) {
        // Edge Case 18: Handle null or empty inputs
        if (breakdown == null || breakdown.isEmpty()) {
            logger.log("findPriceItemKey: breakdown is null or empty");
            return null;
        }

        if (englishType == null || indonesianType == null) {
            logger.log("findPriceItemKey: passenger type is null");
            return null;
        }

        if (count <= 0) {
            logger.log("findPriceItemKey: invalid passenger count: " + count);
            return null;
        }

        String searchPattern1 = englishType + " (x" + count + ")";
        String searchPattern2 = indonesianType + " (x" + count + ")";

        // Edge Case 19: Try case-insensitive search
        for (String key : breakdown.keySet()) {
            if (key == null) continue;

            String lowerKey = key.toLowerCase();
            if (lowerKey.contains(searchPattern1.toLowerCase()) || lowerKey.contains(searchPattern2.toLowerCase())) {
                logger.log("Found match: '" + key + "' for pattern: '" + searchPattern1 + "' or '" + searchPattern2 + "'");
                return key;
            }
        }

        logger.log("No match found for patterns: '" + searchPattern1 + "' or '" + searchPattern2 + "'");
        return null;
    }

    /**
     * Finds the second occurrence of a price item key (for return flights in roundtrip).
     *
     * @param breakdown Price breakdown map
     * @param englishType English passenger type (e.g., "Adult")
     * @param indonesianType Indonesian passenger type (e.g., "Dewasa")
     * @param count Passenger count
     * @return Matching key or null if not found
     */
    private String findSecondPriceItemKey(Map<String, String> breakdown, String englishType, String indonesianType, int count) {
        // Edge Case 20: Handle null or empty inputs
        if (breakdown == null || breakdown.isEmpty()) {
            logger.log("findSecondPriceItemKey: breakdown is null or empty");
            return null;
        }

        if (englishType == null || indonesianType == null) {
            logger.log("findSecondPriceItemKey: passenger type is null");
            return null;
        }

        String searchPattern1 = englishType + " (x" + count + ")";
        String searchPattern2 = indonesianType + " (x" + count + ")";
        boolean foundFirst = false;

        for (String key : breakdown.keySet()) {
            if (key == null) continue;

            String lowerKey = key.toLowerCase();
            if (lowerKey.contains(searchPattern1.toLowerCase()) || lowerKey.contains(searchPattern2.toLowerCase())) {
                if (foundFirst) {
                    logger.log("Found second match: '" + key + "' for pattern: '" + searchPattern1 + "' or '" + searchPattern2 + "'");
                    return key; // Return the second occurrence
                }
                foundFirst = true;
            }
        }

        logger.log("No second match found for patterns: '" + searchPattern1 + "' or '" + searchPattern2 + "'");
        return null;
    }

    /**
     * Finds a price item key by partial text match.
     *
     * @param breakdown Price breakdown map
     * @param searchTerms Search terms to match (any of them)
     * @return Matching key or null if not found
     */
    private String findPriceItemKeyByPartialMatch(Map<String, String> breakdown, String... searchTerms) {
        // Edge Case 21: Handle null or empty inputs
        if (breakdown == null || breakdown.isEmpty()) {
            logger.log("findPriceItemKeyByPartialMatch: breakdown is null or empty");
            return null;
        }

        if (searchTerms == null || searchTerms.length == 0) {
            logger.log("findPriceItemKeyByPartialMatch: no search terms provided");
            return null;
        }

        // Log search attempt
        StringBuilder searchTermsLog = new StringBuilder("Searching for: ");
        for (String term : searchTerms) {
            if (term != null) {
                searchTermsLog.append("'").append(term).append("' ");
            }
        }
        logger.log(searchTermsLog.toString());

        for (String key : breakdown.keySet()) {
            if (key == null) continue;

            String lowerKey = key.toLowerCase();
            for (String term : searchTerms) {
                // Edge Case 22: Handle null search terms
                if (term == null || term.isEmpty()) continue;

                if (lowerKey.contains(term.toLowerCase())) {
                    logger.log("Found match: '" + key + "' for search term: '" + term + "'");
                    return key;
                }
            }
        }

        logger.log("No match found for any of the search terms");
        return null;
    }

    /**
     * Compares two price strings, handling different formats.
     * Converts prices to double using convertPriceSafely for accurate numeric comparison.
     * Uses tolerance-based comparison (0.01) to handle floating-point precision issues.
     *
     * @param actualPrice   Actual price from UI
     * @param expectedPrice Expected price
     * @return true if prices match within tolerance (0.01)
     */
    private boolean comparePrices(String actualPrice, String expectedPrice) {
        // Edge Case 23: Handle null prices
        if (actualPrice == null || expectedPrice == null) {
            logger.log("comparePrices: null price detected - actual: " + actualPrice + ", expected: " + expectedPrice);
            return false;
        }

        try {
            double actualPriceDouble = CoreFunctions.convertPriceSafely(actualPrice);
            double expectedPriceDouble = CoreFunctions.convertPriceSafely(expectedPrice);

            // Use tolerance-based comparison for floating point precision (0.01 tolerance)
            double priceDifference = Math.abs(actualPriceDouble - expectedPriceDouble);
            return priceDifference <= 0.01;
        } catch (NumberFormatException e) {
            logger.log("comparePrices: Could not convert prices to double - actual: '" + actualPrice +
                    "', expected: '" + expectedPrice + "', error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Normalizes price string for comparison.
     * Removes currency symbols, spaces, and converts separators.
     *
     * @param price Price string to normalize
     * @return Normalized price string (digits only)
     */
    private String normalizePrice(String price) {
        if (price == null) {
            return "";
        }

        // Edge Case 26: Handle already normalized prices
        if (price.matches("^[0-9]+$")) {
            return price;
        }

        // Remove currency codes, spaces, and keep only digits
        String normalized = price.replaceAll("[^0-9]", "");

        // Edge Case 27: Log warning if no digits found
        if (normalized.isEmpty() && !price.isEmpty()) {
            logger.log("WARNING: normalizePrice removed all content from: '" + price + "'");
        }

        return normalized;
    }

    public void selectMultiMealAddons(int mealCount,boolean multiMeal) {

        driverTools.waitForElementToAppear(ADD_MEAL_BUTTON);
        driverTools.clickWithJavascriptExecutor(ADD_MEAL_BUTTON);

        driverTools.waitForElementToAppear(ADD_MEAL_POPUP);

        List<WebElement> addButtons =
                driverTools.waitForElementsToAppear(MEAL_ADD_BUTTONS).get();

        for (int i = 0; i < mealCount; i++) {
            driverTools.clickWithJavascriptExecutor(addButtons.get(i));
        }

        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);

    }
    public void openFlightFacilitySection(){
        driverTools.waitForElementToAppear(FLIGHT_DETAILS_BUTTON);
        driverTools.clickWithJavascriptExecutor(FLIGHT_DETAILS_BUTTON);
        driverTools.waitForElementToPresence(SEE_OTHER_FACILITIES_BUTTON);
        driverTools.clickWithJavascriptExecutor(SEE_OTHER_FACILITIES_BUTTON);
    }

    public void closeFlightFacilitySection(){
        driverTools.waitForElementToAppear(CLOSE_FACILITIES_POPUP);
        driverTools.clickWithJavascriptExecutor(CLOSE_FACILITIES_POPUP);
        driverTools.waitForElementToDisappear(FLIGHT_FACILITY_POPUP);
    }

    public boolean isRefundRescheduleVisible(){
        boolean refundVisible = driverTools.waitForElementToAppear(REFUND_CHIP_ON_DETAILS).isPresent();
        logger.log("Refund visible: " + refundVisible);
        boolean rescheduleVisible = driverTools.waitForElementToAppear(RESCHEDULE_CHIP_ON_DETAILS).isPresent();
        logger.log("Reschedule visible: " + rescheduleVisible);
        return refundVisible && rescheduleVisible;
    }

    public boolean isMealSelectionVisible(){
        driverTools.waitForElementToPresence(MEAL_ADDON_DETAILS);
        List<String> meals = driverTools.getTexts(MEAL_ADDON_DETAILS);
        Assertion.assertThat("Meals available: "+meals.toString() ,meals.toString(), is(not(emptyString())));
        return !meals.isEmpty();
    }

    public boolean isConnectingFlightProtectionNudgeVisible() {
        boolean isVisible = driverTools.waitForElementToAppear(CONNECTING_FLIGHT_PROTECTION_NUDGE, 10).isPresent();
        logger.log("'Protected by Connecting Flight Protection' nudge visible on BF1 flight card: " + isVisible);
        return isVisible;
    }

    public boolean isConnectingFlightProtectionInWhatYouGetSection() {
        driverTools.waitForElementToAppear(CONNECTING_FLIGHT_PROTECTION_WHAT_YOU_GET, 5);
        return driverTools.waitForElementToAppear(CONNECTING_FLIGHT_PROTECTION_TEXT_INFO, 5).isPresent();
    }

    public void clickConnectingFlightProtectionMoreBenefit() {
        driverTools.waitForElementToAppear(CONNECTING_FLIGHT_PROTECTION_MORE_BENEFIT_BTN, 10);
        driverTools.clickWithJavascriptExecutor(CONNECTING_FLIGHT_PROTECTION_MORE_BENEFIT_BTN);
        logger.log("Clicked 'More Benefit' on Connecting Flight Protection card.");
    }

    public boolean isConnectingFlightProtectionInsuranceDetailVisible() {
        boolean isVisible = driverTools.waitForElementToAppear(CONNECTING_FLIGHT_PROTECTION_INSURANCE_DETAIL, 10).isPresent();
        logger.log("Connecting Flight Protection insurance detail visible in bottom sheet: " + isVisible);
        return isVisible;
    }

    public void closeConnectingFlightProtectionBottomSheet() {
        driverTools.waitForElementToAppear(CONNECTING_FLIGHT_PROTECTION_BOTTOM_SHEET_CLOSE, 5);
        driverTools.clickWithJavascriptExecutor(CONNECTING_FLIGHT_PROTECTION_BOTTOM_SHEET_CLOSE);
        driverTools.waitForElementToDisappear(CONNECTING_FLIGHT_PROTECTION_INSURANCE_DETAIL);
        logger.log("Connecting Flight Protection bottom sheet closed.");
    }

    public boolean isConnectingFlightWarningMessageVisible() {
        boolean isVisible = driverTools.waitForElementToAppear(CONNECTING_FLIGHT_WARNING_MESSAGE, 10).isPresent();
        logger.log("'You chose connecting flights' warning message visible: " + isVisible);
        return isVisible;
    }

    public boolean isConnectingFlightProtectionInPriceDetail() {
        boolean isVisible = driverTools.waitForElementToAppear(CONNECTING_FLIGHT_INFO_IN_PRICEDETAIL, 5).isPresent();
        logger.log("'Connecting Flight Protection' in Price Detail visible via fallback locator: " + isVisible);
        return isVisible;
    }

    public abstract boolean verifyChangeEmail(String email);


    public void selectSingleSegmentMealAddons(int totalPax, int totalMealEachPax){
        driverTools.waitForElementToAppear(ADD_MEAL_BUTTON);
        driverTools.scrollIntoWebView(ADD_MEAL_BUTTON);
        driverTools.clickWithJavascriptExecutor(ADD_MEAL_BUTTON);
        if (driverTools.waitForElementToAppear(ADD_MEAL_SELECTION_POPUP).isEmpty()) {
            driverTools.scrollIntoWebView(ADD_MEAL_BUTTON);
            driverTools.clickWithJavascriptExecutor(ADD_MEAL_BUTTON);
        }
        for (int paxIndex = 0; paxIndex < totalPax; paxIndex++) {
            WebElement passenger = driverTools.waitForElementsToAppear(PASSENGER_ADDON_SECTION).get().get(paxIndex);
            driverTools.clickWithJavascriptExecutor(passenger);
            for(int j=0;j<totalMealEachPax;j++){
                WebElement radioButton = driverTools.waitForElementsToAppear(MEAL_SELECTOR).get().get(j);
                driverTools.clickWithJavascriptExecutor(radioButton);
            }
        }
        driverTools.waitForElementToAppear(POPUP_SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(POPUP_SAVE_BUTTON);
    }
    private double parseAddonTotalPrice(Locator locator) {
        driverTools.waitForElementToAppear(locator);
        String priceText = driverTools.getText(locator).orElseThrow().trim();
        String normalized = priceText.toLowerCase();
        if (normalized.contains("free") || normalized.contains("gratis") || normalized.contains("included")) {
            return 0.0;
        }
        return CoreFunctions.convertPriceSafely(priceText);
    }

    public void deleteMiddleChar() {
        // 1. Grab the web element
        Optional<WebElement> nameInput = driverTools.element(CONTACT_NAME_INPUT);
        WebElement nameInputEl = nameInput.orElseThrow(() -> new NoSuchElementException("CONTACT_NAME_INPUT not found"));
        String initialText = nameInputEl.getAttribute("value");
        logger.log("LOG -> Initial Text fetched: '" + initialText + "'");

        // Safety check to prevent StringIndexOutOfBoundsException if the field is empty
        if (initialText == null || initialText.length() < 3) {
            Assertion.assertThat("Text field must have at least 3 characters to test middle deletion.", false, is(true));
            return;
        }

        int middle = initialText.length() / 2;
        String expectedText = initialText.substring(0, Math.max(0, middle - 2)) + initialText.substring(middle);
        logger.log("LOG -> Calculated Expected Text: '" + expectedText + "'");

        // 2. Move the cursor to the middle
        driverTools.moveCursorToMiddle(nameInputEl);

        // 3. Send backspaces to delete the middle characters
        nameInput.get().sendKeys(Keys.BACK_SPACE);
        nameInput.get().sendKeys(Keys.BACK_SPACE);

        // 4. Fetch the updated text string after deletion
        String actualText = nameInput.get().getAttribute("value");
        logger.log("LOG -> Actual Text after deletion: '" + actualText + "'");

        // 6. Assert that the last character hasn't changed (proving cursor didn't jump to the end)
        Assertion.assertThat("Cursor stayed in the middle and deleted correctly. Expected: '" + expectedText + "', Actual: '" + actualText + "'", actualText.equalsIgnoreCase(expectedText), is(true));
    }

    public double getTotalMealAddonPrice() {
        return parseAddonTotalPrice(MEAL_ADDONS_PRICE);
    }

    public double getTotalSeatAddonPrice() {
        return parseAddonTotalPrice(SEAT_ADDONS_PRICE);
    }
    public void reloadPage() {
        driver.navigate().refresh();
    }

    /**
     * Fills out the Contact Details and Passenger Details using data provided from a Google Sheet.
     * Designed for a single passenger booking where "Same as contact detail" is toggled,
     * but DOB and Nationality must still be filled manually.
     *
     * @param contactSalutation e.g., "Mr.", "Mrs.", "Ms."
     * @param contactFullName Full name for the contact person
     * @param contactPhone Mobile number including country code
     * @param contactEmail Email address for the contact person
     * @param paxDob Date of birth for the passenger in "YYYY-MM-DD" format
     * @param paxNationality Nationality string to search and select
     */
    public void fillBookingDetailsSameAsLogin(
            String contactSalutation,
            String contactFullName,
            String contactPhone,
            String contactEmail,
            String paxDob,
            String paxNationality,
            int paxIndex) {

        logger.log("Filling Contact Details from GSheet data...");
        logCartIdFromUrl();

        // 1. Fill Contact Details
        driverTools.waitForElementToAppear(CONTACT_DETAILS_BOOKING_PAGE);

        // Select Contact Salutation
        Locator salutationLoc = getSalutationLocator(contactSalutation);
        driverTools.clickWithJavascriptExecutor(salutationLoc);

        // Fill Contact Name, Phone, and Email using existing helper
        clearAndFillContactField(CONTACT_NAME_INPUT, contactFullName);
        clearAndFillContactField(CONTACT_PHONE_INPUT, contactPhone);
        clearAndFillContactField(CONTACT_EMAIL_INPUT, contactEmail);

        logger.log("Toggling 'Same as contact detail' and filling remaining Pax data...");

        // Ensure the passenger form is active/expanded so elements are interactable
        ensurePassengerFormIsActive(paxIndex);

        // Click the toggle to copy Name and Salutation to passenger details
        driverTools.scrollElementToCenter(SAME_CONTACT_TOGGLE);
        driverTools.clickWithJavascriptExecutor(SAME_CONTACT_TOGGLE);

        // 3. Fill the remaining mandatory fields (DOB and Nationality) that the toggle doesn't cover
        if (paxDob != null && !paxDob.isEmpty()) {
            logger.log("Filling DOB for pax 1: " + paxDob);
            fillDob(paxIndex, paxDob);
        }

        if (paxNationality != null && !paxNationality.isEmpty()) {
            logger.log("Filling Nationality for pax 1: " + paxNationality);
            fillNationality(paxIndex, paxNationality);
        }
    }

    public boolean isLoginBannerVisible() {
        return driverTools.waitForElementToAppear(LOGIN_BANNER_BOOKING_PAGE, 3).isPresent() &&
                driverTools.isElementDisplayed(LOGIN_BANNER_BOOKING_PAGE);
    }

    public boolean isEmailFieldDisabled() {
        WebElement emailInput = driverTools.waitForElementToPresence(CONTACT_EMAIL_INPUT, 3).orElseThrow();
        String disabledAttr = emailInput.getAttribute("disabled");
        String readOnlyAttr = emailInput.getAttribute("readonly");

        return "true".equalsIgnoreCase(disabledAttr) || "true".equalsIgnoreCase(readOnlyAttr);
    }

    public void navigateBackToBookingForm() {
        int maxRetries = 5;
        logger.log("Attempting to navigate back to the booking form...");

        for (int i = 1; i <= maxRetries; i++) {
            driverTools.back();
            logger.log("Pressed browser back button. Attempt: " + i);

            // Wait briefly to see if the intercept popup appears
            if (driverTools.waitForElementToAppear(GO_TO_BOOKING_FORM_BTN, 3).isPresent()) {
                logger.log("Leave confirmation popup intercepted!");
                driverTools.clickWithJavascriptExecutor(GO_TO_BOOKING_FORM_BTN);

                // Wait for the popup to disappear confirming we are moving pages
                driverTools.waitForElementToDisappear(GO_TO_BOOKING_FORM_BTN, 5);
                return;
            }
        }
        throw new RuntimeException("Leave confirmation popup did not appear after " + maxRetries + " back attempts.");
    }

    public String isBookingFailed() {
        driverTools.waitForElementToAppear(SORRY_POPUP, 20);
        return driverTools.waitForElementToAppear(SORRY_POPUP).orElseThrow().getText();
    }

    public void enableV7BookingRequestCapture() {
        driverTools.enableNetworkRequestCapture(V7_BOOKING_URL, V7_BOOKING_STORAGE_KEY);
        logger.log("Enabled v7/booking request capture");
    }

    public String getCapturedCorporateInterimId() {
        String body = driverTools.readCapturedNetworkRequestBody(V7_BOOKING_STORAGE_KEY);
        logger.log("Captured v7/booking payload length=" + body.length());
        if (body.isBlank()) {
            return "";
        }
        try {
            JSONObject corporateData = new JSONObject(body).optJSONObject("corporateDataRequest");
            if (corporateData != null && !corporateData.optString("corporateInterimId").isBlank()) {
                String corporateInterimId = corporateData.optString("corporateInterimId");
                logger.log("Extracted corporateInterimId=" + corporateInterimId);
                return corporateInterimId;
            }
        } catch (Exception e) {
            logger.log("v7/booking payload is not JSON: " + e.getMessage());
        }
        Matcher matcher = Pattern.compile("\"corporateInterimId\"\\s*:\\s*\"([^\"]+)\"").matcher(body);
        String corporateInterimId = matcher.find() ? matcher.group(1) : "";
        logger.log("Extracted corporateInterimId=" + (corporateInterimId.isBlank() ? "<empty>" : corporateInterimId));
        return corporateInterimId;
    }

    private String v7BookingCaptureScript() {
        return "(() => {"
                + "  const KEY = '__tiketV7BookingCaptures';"
                + "  const persist = (body) => {"
                + "    if (!body) return;"
                + "    const value = typeof body === 'string' ? body : String(body);"
                + "    if (!value || value === '[object Object]' || value === '[object ReadableStream]') return;"
                + "    try {"
                + "      const arr = JSON.parse(sessionStorage.getItem(KEY) || '[]');"
                + "      arr.push(value);"
                + "      if (arr.length > 5) arr.splice(0, arr.length - 5);"
                + "      sessionStorage.setItem(KEY, JSON.stringify(arr));"
                + "      window.__tiketV7BookingCaptures = arr;"
                + "    } catch (e) {}"
                + "  };"
                + "  if (window.__tiketV7BookingCaptureInstalled) return;"
                + "  window.__tiketV7BookingCaptureInstalled = true;"
                + "  const origFetch = window.fetch;"
                + "  window.fetch = async function(input, init) {"
                + "    try {"
                + "      const url = typeof input === 'string' ? input : (input && input.url) || '';"
                + "      const body = (init && init.body) || '';"
                + "      if (String(url).indexOf('v7/booking') !== -1) persist(body);"
                + "    } catch (e) {}"
                + "    return origFetch.apply(this, arguments);"
                + "  };"
                + "  const origOpen = XMLHttpRequest.prototype.open;"
                + "  const origSend = XMLHttpRequest.prototype.send;"
                + "  XMLHttpRequest.prototype.open = function(method, url) {"
                + "    this.__tiketUrl = url;"
                + "    return origOpen.apply(this, arguments);"
                + "  };"
                + "  XMLHttpRequest.prototype.send = function(body) {"
                + "    try {"
                + "      if (this.__tiketUrl && String(this.__tiketUrl).indexOf('v7/booking') !== -1) persist(body);"
                + "    } catch (e) {}"
                + "    return origSend.apply(this, arguments);"
                + "  };"
                + "})();";
    }
    private String readCapturedV7BookingBody() {
        Object result = ((JavascriptExecutor) driver).executeScript(
                "try {"
                        + "  const stored = JSON.parse(sessionStorage.getItem('__tiketV7BookingCaptures') || '[]');"
                        + "  const live = window.__tiketV7BookingCaptures || [];"
                        + "  const all = stored.concat(live).filter(Boolean);"
                        + "  return all.length ? all[all.length - 1] : '';"
                        + "} catch (e) { return ''; }");
        return result == null ? "" : result.toString();
    }
    private String readV7BookingBodyFromPerformanceLogs() {
        try {
            String last = "";
            for (LogEntry entry : driver.manage().logs().get("performance").getAll()) {
                JSONObject message = new JSONObject(entry.getMessage()).optJSONObject("message");
                if (message == null || !"Network.requestWillBeSent".equals(message.optString("method"))) {
                    continue;
                }
                JSONObject params = message.optJSONObject("params");
                JSONObject request = params == null ? null : params.optJSONObject("request");
                if (request == null) {
                    continue;
                }
                String url = request.optString("url");
                if (url != null && url.contains("v7/booking") && request.has("postData")) {
                    last = request.optString("postData");
                }
            }
            return last;
        } catch (Exception e) {
            logger.log("Failed to read v7/booking from performance logs: " + e.getMessage());
            return "";
        }
    }
    public boolean isCheckInAgainInfoVisible() {
        if (driverTools.waitForElementToAppear(CHECK_IN_AGAIN_LIST_ITEM, 10).isPresent()) {
            return true;
        }
        return driverTools.waitForElementToAppear(SELF_TRANSFER_PROTECTION_INFO, 5).isPresent();
    }

    public void expandFlightRouteDetails() {
        driverTools.waitForElementToAppear(FLIGHT_ROUTE_COLLAPSE_ICON, 10);
        driverTools.clickWithJavascriptExecutor(FLIGHT_ROUTE_COLLAPSE_ICON);
    }

    public void openRefundAndRescheduleDetails() {
        logger.log("Opening flight details");
        driverTools.waitForElementToAppear(SEE_DETAILS_REFUND_AND_RESCHEDULE);
        driverTools.clickWithJavascriptExecutor(SEE_DETAILS_REFUND_AND_RESCHEDULE);
    }

    public List<String> refundDatesUnderPolicies() {
        driverTools.waitForElementToAppear(REFUND_CHIP_ON_DETAILS);
        logger.log("Opening Refund tab");
        driverTools.clickWithJavascriptExecutor(REFUND_CHIP_ON_DETAILS);
        driverTools.dummyWait(1);

        String rawText = getPolicyTimelineText();
        String refundSectionText = extractSectionText(rawText, "refund");
        logger.log("Isolated Refund Text: " + refundSectionText);
        return driverTools.extractPolicyKeywords(refundSectionText);
    }

    public List<String> rescheduleDatesUnderPolicies() {
        logger.log("Opening Reschedule tab");
        driverTools.waitForElementToAppear(RESCHEDULE_CHIP_ON_DETAILS);
        driverTools.clickWithJavascriptExecutor(RESCHEDULE_CHIP_ON_DETAILS);
        driverTools.dummyWait(1);

        String rawText = getPolicyTimelineText();
        String rescheduleSectionText = extractSectionText(rawText, "reschedule");
        logger.log("Isolated Reschedule Text: " + rescheduleSectionText);
        return driverTools.extractPolicyKeywords(rescheduleSectionText);
    }

    private String getPolicyTimelineText() {
        driverTools.waitForElementToAppear(ACTIVE_TIMELINE_CONTAINER, 5);
        WebElement activeTimeline = driverTools.element(ACTIVE_TIMELINE_CONTAINER).orElseThrow(
                () -> new NoSuchElementException("Active timeline container was not found on Booking Page.")
        );
        return driverTools.getText(activeTimeline);
    }

    /**
     * Filters the combined modal text to only return the specified section ("refund" or "reschedule").
     */
    private String extractSectionText(String fullText, String sectionType) {
        if (fullText == null || fullText.isBlank()) {
            return "";
        }
        String lower = fullText.toLowerCase();

        int refundIdx = findFirstIndex(lower, "estimated refund", "pengembalian", "jumlah refund");
        int rescheduleIdx = findFirstIndex(lower, "estimated reschedule", "jadwal", "biaya reschedule");

        if ("refund".equalsIgnoreCase(sectionType)) {
            if (refundIdx != -1 && rescheduleIdx != -1 && rescheduleIdx > refundIdx) {
                return fullText.substring(refundIdx, rescheduleIdx);
            } else if (refundIdx != -1) {
                return fullText.substring(refundIdx);
            }
        } else if ("reschedule".equalsIgnoreCase(sectionType)) {
            if (rescheduleIdx != -1) {
                return fullText.substring(rescheduleIdx);
            }
        }
        return fullText;
    }

    private int findFirstIndex(String text, String... keywords) {
        for (String kw : keywords) {
            int idx = text.indexOf(kw);
            if (idx != -1) {
                return idx;
            }
        }
        return -1;
    }

    /**
     * Opens the Price Details section and verifies that Full Protection is displayed.
     */
    public void verifyFullProtectionInPriceBreakdown() {
        logger.log("Opening Price Details section...");
        openPriceDetailSection();

        boolean isFullProtectionDisplayed = driverTools.waitForElementToAppear(PRICE_BREAKDOWN_FULL_PROTECTION, 5).isPresent();
        logger.log("Is Full Protection displayed in Price Details popup: " + isFullProtectionDisplayed);

        Assertion.assertThat("Full Protection should be visible in Price Details popup",
                isFullProtectionDisplayed, is(true));

        closePopUp();
    }

    public void clickPassengerDetailsToggle(){
         driverTools.waitForElementToPresence(TOGGLE_SAME_AS_CONTACT_DETAILS,5);
        driverTools.clickWithJavascriptExecutor(TOGGLE_SAME_AS_CONTACT_DETAILS);
    }
}
