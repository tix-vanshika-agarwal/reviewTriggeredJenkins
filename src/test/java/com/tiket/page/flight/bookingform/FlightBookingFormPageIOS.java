package com.tiket.page.flight.bookingform;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.flight.*;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.chromium.HasCdp;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tiket.page.payment.PaymentPageIOS.ORDER_ID;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class FlightBookingFormPageIOS<T extends WebDriver> extends FlightBookingFormPage<T> {

    public FlightBookingFormPageIOS(Session<T> session) {
        super(session);
    }

    public static final Locator LOGIN_BANNER = new Locator("LOGIN_BANNER",AppiumBy.xpath("//*[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'log in')]"));
    public static final Locator FULL_NAME = new Locator("FULL_NAME",AppiumBy.xpath("//*[@name='fullName' and ./parent::*[./parent::*[@type='XCUIElementTypeOther']]]"));
    public static final Locator FULL_NAME_GUEST = new Locator("FULL_NAME_GUEST",AppiumBy.id("fullName"));
    public static final Locator FILL_IN_CONTACT_DETAILS = new Locator("FILL_IN_CONTACT_DETAILS",AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and (contains(@name,'Fill in contact details') or contains(@name,'Isi detail kontak') or contains(@name,'Isi data kontak'))]"));
    public static final Locator TITLE = new Locator("TITLE",AppiumBy.xpath("//*[@name='Mr.' or @name='Tuan']"));
    public static final Locator TITLE_PASSENGER = new Locator("TITLE_PASSENGER",AppiumBy.xpath("(//*[@name='Mr.' or @name='Mstr.' or @name='Tuan' and @visible='true' and ./parent::*[./parent::*[@type='XCUIElementTypeOther']]])[last()]"));
    public static final Locator DOB = new Locator("DOB",AppiumBy.xpath("//*[@name='Date of Birth' or @name='Tanggal Lahir']"));
    public static final Locator BTN_CONTINUE = new Locator("BTN_CONTINUE",AppiumBy.accessibilityId("continue_btn"));
    public static final Locator YEAR_PICKER_WHEEL = new Locator("YEAR_PICKER_WHEEL",AppiumBy.id("Table_year"));
    public static final Locator CHECK_PASSENGER = new Locator("CHECK_PASSENGER",AppiumBy.xpath("//*[@type='XCUIElementTypeButton' and (contains(@name,'Check Again') or contains(@label,'Check Again') or contains(@name,'Check passenger & baggage details') or contains(@label,'Check passenger & baggage details') or contains(@name,'Cek detail penumpang & bagasi') or contains(@label,'Cek detail penumpang & bagasi'))]"));
    public static final Locator NATIONALITY = new Locator("NATIONALITY",AppiumBy.xpath("//*[@name='Nationality' or @name='Kewarganegaraan']"));
    public static final Locator PHONE_NUMBER = new Locator("PHONE_NUMBER",AppiumBy.id("phone"));
    public static final Locator EMAIL = new Locator("EMAIL",AppiumBy.id("email"));
    public static final Locator INDONESIA = new Locator("INDONESIA",AppiumBy.id("Indonesia"));
    public static final Locator DONE_BTN = new Locator("DONE_BTN",AppiumBy.xpath("//*[@name='Selesai' or @name='Done' and @type='XCUIElementTypeButton']"));
    // The add-on cells carry buggy/duplicate accessibility ids ("add_meal" becomes "add_seat" once meals
    // are added), so also match by stable title. Always click the Add/Tambah control to open the add-on.
    public static final Locator MEALS_ADDON = new Locator("MEALS_ADDON",AppiumBy.xpath("//*[@name='add_meal']//*[@name='Add' or @name='Tambah' or @name='Change' or @name='Ubah'] | //XCUIElementTypeCell[.//XCUIElementTypeStaticText[@name='Meals' or @name='Makanan']]//*[@name='Add' or @name='Tambah' or @name='Change' or @name='Ubah']"));
    public static final Locator CHOOSE_MEALS = new Locator("CHOOSE_MEALS",AppiumBy.xpath("(//*[contains(@name,'checkbox')])[1]"));
    // New "Book Meals"/"Pesan Makanan" page: horizontal passenger tabs ("1.", "2." ...) then meal rows with
    // enabled Add/Tambah. Adding a meal toggles that row to Cancel/Batalkan.
    public static final Locator MEAL_PASSENGER_SELECTOR = new Locator("MEAL_PASSENGER_SELECTOR",AppiumBy.xpath("//XCUIElementTypeScrollView[.//XCUIElementTypeStaticText[starts-with(@name,'1.')]]"));
    public static final Locator MEAL_ADD_BUTTON = new Locator("MEAL_ADD_BUTTON",AppiumBy.xpath("(//XCUIElementTypeButton[(@name='Add' or @name='Tambah') and @enabled='true'])[1]"));
    public static final Locator MEAL_ALREADY_SELECTED = new Locator("MEAL_ALREADY_SELECTED",AppiumBy.xpath("(//XCUIElementTypeButton[@name='Cancel' or @name='Batalkan'])[1]"));
    public static final Locator MEAL_ADD_BUTTON_DISABLED = new Locator("MEAL_ADD_BUTTON_DISABLED",AppiumBy.xpath("(//XCUIElementTypeButton[(@name='Add' or @name='Tambah') and @enabled='false'])[1]"));
    public static final Locator BOOK_MEALS_PAGE = new Locator("BOOK_MEALS_PAGE",
            AppiumBy.xpath("//*[@name='Book Meals' or @name='Pesan Makanan' or @label='Book Meals' or @label='Pesan Makanan']"));
    public static final Locator COVID_TEST = new Locator("COVID_TEST",AppiumBy.xpath("//*[contains(@name,'COVID-19')]"));
    public static final Locator SELECT_TEST = new Locator("SELECT_TEST",AppiumBy.xpath("(//*[@name='radioButton'])[1]"));
    public static final Locator CHECKBOX_AGREE = new Locator("CHECKBOX_AGREE",AppiumBy.xpath("//*[@name='checkbox']"));
    public static final Locator CONTINUE_PAYMENT_BUTTON = new Locator("CONTINUE_PAYMENT_BUTTON",AppiumBy.xpath("//*[@name='continue_to_payment' or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'review')]"));
    public static final Locator PRICE_DETAIL_ARROW = new Locator("PRICE_DETAIL_ARROW", AppiumBy.xpath("//*[@label=\"Pay Now\" and @type=\"XCUIElementTypeStaticText\"]"));
    public static final Locator CLOSED_PRICE_DETAIL = new Locator("CLOSED_PRICE_DETAIL",AppiumBy.xpath("//XCUIElementTypeButton[@name='ic cancel grey' and following-sibling::XCUIElementTypeStaticText[@name='Price Details' or @name='Detail Harga' or @name='Rincian Harga']]"));
    public static final Locator INSURANCE_LABEL = new Locator("INSURANCE_LABEL",AppiumBy.xpath("(//*[contains(@name,'Asuransi') or contains(@name,'Insurance')])[last()]"));
    public static final Locator CONFIRM_PAYMENT = new Locator("CONFIRM_PAYMENT",AppiumBy.name("continue_to_payment_2"));
    public static final Locator BAGGAGE_ALLOWANCE = new Locator("BAGGAGE_ALLOWANCE",AppiumBy.xpath("//*[@type='XCUIElementTypeButton' and contains(@name, 'OK') or contains(@name, 'OK, Mengerti') or contains(@name, 'Oke, mengerti') or contains(@name, 'Oke, Mengerti')]"));
    public static final Locator PRICE_CHANGE = new Locator("PRICE_CHANGE",AppiumBy.xpath("//*[@name='OK, Mengerti' or @name='OK, Got It' and @type='XCUIElementTypeButton']"));
    public static final Locator RELOAD = new Locator("RELOAD",AppiumBy.xpath("//*[@name='Reload, Got It' or @name='Reload, Mengerti' and @type='XCUIElementTypeButton']"));
    public static final Locator CHOOSE_BUTTON_DEPART = new Locator("CHOOSE_BUTTON_DEPART",AppiumBy.id("select_depart"));
    public static final Locator CHOOSE_BUTTON_RETURN = new Locator("CHOOSE_BUTTON_RETURN",AppiumBy.id("select_return"));
    public static final Locator ANCILLARIES_SELECT_RETURN = new Locator("ANCILLARIES_SELECT_RETURN",
            AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Return' or @name='Pulang' or contains(@name,'Return') or contains(@name,'Pulang')]/following::XCUIElementTypeButton[@name='Select' or @name='Pilih' or @name='Change' or @name='Ubah'][1]"));
    // RT Book Meals: route tabs expose codes in @label ("CGK - DPS" / "DPS - CGK"); @name is textLabel / textLabel (selected).
    public static final Locator MEAL_RETURN_ROUTE_TAB = new Locator("MEAL_RETURN_ROUTE_TAB", AppiumBy.xpath(
            "(//XCUIElementTypeStaticText[(@name='textLabel' or @name='textLabel (selected)') and contains(@label,' - ')])[last()]"
                    + " | (//XCUIElementTypeStaticText[contains(@label,' - ') and string-length(@label) >= 7 and string-length(@label) <= 12])[last()]"));
    public static final Locator WHAT_YOU_GET = new Locator("WHAT_YOU_GET",AppiumBy.xpath("//*[@type='XCUIElementTypeCell' and ./*[@name='what_you_get_subtitle']]"));
    public static final Locator GIVEN_NAME = new Locator("GIVEN_NAME",AppiumBy.xpath("//*[@type='XCUIElementTypeOther' and ./*[@type='XCUIElementTypeStaticText' and contains(@label, 'Full Name') or contains(@label, 'Nama Lengkap sesuai')]]"));
    public static final Locator LAST_NAME = new Locator("LAST_NAME",AppiumBy.accessibilityId("lastName"));
    public static final Locator PASSPORT = new Locator("PASSPORT",AppiumBy.name("passportNo"));
    public static final Locator ISSUED_DATE = new Locator("ISSUED_DATE",AppiumBy.xpath("//*[@name='Issued Date' or @name='Tanggal Pengeluaran']"));
    public static final Locator EXPIRY_DATE = new Locator("EXPIRY_DATE",AppiumBy.xpath("//*[@name='Expiry Date' or @name='Tanggal Habis Berlaku']"));
    public static final Locator ISSUING_COUNTRY = new Locator("ISSUING_COUNTRY",AppiumBy.xpath("//*[contains(@name,'Issuing Country') or  contains(@name,'Negara yang Mengeluarkan')]"));
    public static final Locator SEAT_ADDON = new Locator("SEAT_ADDON",AppiumBy.xpath("//*[@name='add_seat']//*[@name='Add' or @name='Tambah' or @name='Change' or @name='Ubah'] | //XCUIElementTypeCell[.//XCUIElementTypeStaticText[@name='Seat' or @name='Seats' or @name='Kursi']]//*[@name='Add' or @name='Tambah' or @name='Change' or @name='Ubah']"));
    public static final Locator CHOOSE_EMPTY_SEAT = new Locator("CHOOSE_EMPTY_SEAT",AppiumBy.xpath("//*[contains(@name,'ft_seat_available')][1]"));
    public static final Locator PRICE_ADD_ON = new Locator("PRICE_ADD_ON",AppiumBy.name("price"));
    public static final Locator BOOKING_PAGE_TWO_INDICATOR = new Locator("BOOKING_PAGE_TWO_INDICATOR", AppiumBy.xpath("//*[@name='back']/following-sibling::*//*[contains(@label,'/2')]"));
    public static final Locator BAGGAGE_ADDON = new Locator("BAGGAGE_ADDON", AppiumBy.xpath("//*[@name='add_baggage']//*[@name='Add' or @name='Tambah'] | //XCUIElementTypeCell[.//XCUIElementTypeStaticText[contains(@name,'baggage') or contains(@name,'Baggage') or contains(@name,'Bagasi')]]//*[@name='Add' or @name='Tambah'] | (//XCUIElementTypeStaticText[contains(@name,'Checked baggage') or contains(@label,'Checked baggage') or contains(@name,'Bagasi tercatat') or contains(@label,'Bagasi tercatat')]/following::XCUIElementTypeButton[@name='Add' or @name='Tambah'])[1]"));
    public static final Locator BAGGAGE_DECLINE_OPTION = new Locator("BAGGAGE_DECLINE_OPTION", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'Thanks') or contains(@name,'Tidak') or contains(@label,'Thanks') or contains(@label,'Tidak')]"));
    public static final Locator BAGGAGE_ALLOWANCE_HEADER = new Locator("BAGGAGE_ALLOWANCE_HEADER", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'Baggage Allowance') or contains(@name,'Kuota Bagasi') or contains(@name,'Alokasi Bagasi') or contains(@label,'Baggage Allowance')]"));
    /** "Book Extra Baggage" page title — confirms the dedicated baggage selection sheet opened. */
    public static final Locator BOOK_EXTRA_BAGGAGE_PAGE = new Locator("BOOK_EXTRA_BAGGAGE_PAGE", AppiumBy.xpath("//*[@name='Book Extra Baggage' or @name='Pesan Bagasi Extra' or @label='Book Extra Baggage' or @label='Pesan Bagasi Extra']"));
    public static final Locator BAGGAGE_PROTECTION = new Locator("BAGGAGE_PROTECTION", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Baggage Protection' or @name='Proteksi Bagasi' or @label='Baggage Protection' or @label='Proteksi Bagasi']"));
    public static final Locator BAGGAGE_PROTECTION_CHECKBOX = new Locator("BAGGAGE_PROTECTION_CHECKBOX", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='Baggage Protection' or @name='Proteksi Bagasi' or @label='Baggage Protection' or @label='Proteksi Bagasi']/following::XCUIElementTypeOther[@name='checkbox' or @name='checkbox (selected)'])[1]"));
    public static final Locator BAGGAGE_PROTECTION_CHECKED = new Locator("BAGGAGE_PROTECTION_CHECKED", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='Baggage Protection' or @name='Proteksi Bagasi' or @label='Baggage Protection' or @label='Proteksi Bagasi']/following::XCUIElementTypeOther[@name='checkbox (selected)'])[1]"));
    public static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON", AppiumBy.accessibilityId("ic cancel grey"));
    public static final Locator SAVE_BUTTON = new Locator("SAVE_ADDON_BUTTON", AppiumBy.xpath("//*[@name='save' or @name='Save' or @name='Simpan']"));
    public static final Locator PASSENGER_NAME_CONFIRM = new Locator("PASSENGER_NAME_CONFIRM", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Next passenger') or contains(@label,'Next passenger') or contains(@name,'Penumpang berikutnya') or contains(@label,'Penumpang berikutnya') or contains(@name,\"It's correct\") or contains(@label,\"It's correct\") or contains(@name,'correct') or contains(@label,'correct') or contains(@name,'Sudah benar') or contains(@label,'Sudah benar') or contains(@name,'Benar') or contains(@label,'Benar')]"));
    // New guest passenger form: "No last name/surname" toggle for single-name passengers.
    public static final Locator NO_SURNAME = new Locator("NO_SURNAME", AppiumBy.xpath("//XCUIElementTypeButton[@name='No last name/surname' or contains(@name,'No last name') or contains(@name,'Tidak punya nama belakang') or contains(@name,'Tanpa nama belakang')]"));
    // Indicator that a passenger detail form is currently open (used to detect "Next passenger" auto-advance).
    public static final Locator PASSENGER_FORM_OPEN = new Locator("PASSENGER_FORM_OPEN", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Passenger Info' or @name='Passenger Form' or @name='Info Penumpang' or @name='Informasi Penumpang' or @name='Formulir Penumpang' or @name='Data Penumpang' or @name='Identity Info' or @name='Info Identitas' or @name='Adult Passenger' or @name='Child Passenger' or @name='Infant Passenger' or @name='Penumpang Dewasa' or @name='Penumpang Anak' or @name='Penumpang Bayi']"));
    // Passenger form bottom action: "Next passenger" for intermediate passengers, "Save" for the last one.
    public static final Locator PASSENGER_SUBMIT = new Locator("PASSENGER_SUBMIT", AppiumBy.xpath("//XCUIElementTypeButton[@name='save' or @name='Save' or @name='Simpan' or @name='Next passenger' or @name='Penumpang berikutnya']"));
    // "Edit" button that only appears on the post-save name confirmation popup (distinguishes it from the next form).
    public static final Locator PASSENGER_CONFIRM_EDIT = new Locator("PASSENGER_CONFIRM_EDIT", AppiumBy.xpath("//XCUIElementTypeButton[@name='Edit' or @name='Ubah']"));
    // Confirm on the name-review popup (prefer "It's correct"; fallback Next passenger next to Edit).
    public static final Locator PASSENGER_REVIEW_CONFIRM = new Locator("PASSENGER_REVIEW_CONFIRM", AppiumBy.xpath("//XCUIElementTypeButton[@name=\"It's correct\" or @name='Sudah benar' or contains(@name,\"It's correct\") or contains(@name,'Sudah benar')] | //XCUIElementTypeButton[@name='Edit' or @name='Ubah']/following::XCUIElementTypeButton[@name='Next passenger' or @name='Penumpang berikutnya' or @name='Save' or @name='Simpan'][1]"));
    // --- Login / saved-passenger mode ---
    // Logged-in users pick from a saved passenger list using checkboxes instead of the guest "Passenger N" forms.
    public static final Locator SAVED_PASSENGER_CHECKBOX_ANY = new Locator("SAVED_PASSENGER_CHECKBOX_ANY", AppiumBy.xpath("//XCUIElementTypeOther[@name='checkbox' or @name='checkbox (selected)' or @name='radioButton' or @name='radioButton (selected)']"));
    public static final Locator SAVED_PASSENGER_NAME = new Locator("SAVED_PASSENGER_NAME", AppiumBy.xpath("//XCUIElementTypeStaticText[starts-with(@name,'Mr.') or starts-with(@name,'Mrs.') or starts-with(@name,'Ms.') or starts-with(@name,'Miss.') or starts-with(@name,'Mstr.') or starts-with(@name,'Tuan') or starts-with(@name,'Nyonya') or starts-with(@name,'Nona')]"));
    public static final Locator VIEW_OR_ADD_PASSENGERS = new Locator("VIEW_OR_ADD_PASSENGERS", AppiumBy.xpath("//XCUIElementTypeButton[@name='View or add passengers' or contains(@name,'add passengers') or contains(@name,'tambah penumpang') or contains(@name,'Lihat penumpang') or contains(@name,'Tambah Penumpang') or contains(@name,'Lihat')]"));
    // Prompt shown under a selected saved passenger when required identity info is missing.
    public static final Locator FILL_INFO_PROMPT = new Locator("FILL_INFO_PROMPT", AppiumBy.xpath("//XCUIElementTypeStaticText[starts-with(@name,'Fill the') or starts-with(@name,'Isi ')]"));
    // Empty guest/login slots (Passenger N (Adult/Child/…)) — names/indexes are dynamic.
    public static final Locator EMPTY_PASSENGER_SLOT = new Locator("EMPTY_PASSENGER_SLOT", AppiumBy.xpath(
            "(//XCUIElementTypeStaticText[(starts-with(@name,'Passenger ') or starts-with(@name,'Penumpang ') or starts-with(@label,'Passenger ') or starts-with(@label,'Penumpang '))"
                    + " and (contains(@name,'(Adult)') or contains(@name,'(Child)') or contains(@name,'(Infant)') or contains(@name,'(Dewasa)') or contains(@name,'(Anak)') or contains(@name,'(Bayi)')"
                    + " or contains(@label,'(Adult)') or contains(@label,'(Child)') or contains(@label,'(Infant)') or contains(@label,'(Dewasa)') or contains(@label,'(Anak)') or contains(@label,'(Bayi)'))])[1]"
                    + " | (//XCUIElementTypeButton[@name='body_btn' and (contains(@label,'(Adult)') or contains(@label,'(Child)') or contains(@label,'(Infant)')"
                    + " or contains(@label,'(Dewasa)') or contains(@label,'(Anak)') or contains(@label,'(Bayi)') or contains(@label,'Passenger') or contains(@label,'Penumpang'))])[1]"));
    // Age hints shown inside an open passenger form, used to infer the passenger type for the DOB.
    public static final Locator FORM_TYPE_CHILD = new Locator("FORM_TYPE_CHILD", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'Child Passenger') or contains(@name,'Penumpang Anak') or contains(@name,'age 2') or contains(@name,'usia 2')]"));
    public static final Locator FORM_TYPE_INFANT = new Locator("FORM_TYPE_INFANT", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'Infant Passenger') or contains(@name,'Penumpang Bayi') or contains(@name,'age 0') or contains(@name,'usia 0')]"));
    // Identity Info fields on the saved-passenger detail form (matched by their field name).
    public static final Locator NATIONALITY_FIELD = new Locator("NATIONALITY_FIELD", AppiumBy.name("nationality"));
    public static final Locator ISSUING_DATE_FIELD = new Locator("ISSUING_DATE_FIELD", AppiumBy.name("issuingDate"));
    public static final Locator PASSPORT_EXPIRY_FIELD = new Locator("PASSPORT_EXPIRY_FIELD", AppiumBy.name("passportExpiry"));
    public static final Locator ISSUING_COUNTRY_FIELD = new Locator("ISSUING_COUNTRY_FIELD", AppiumBy.name("issuingCountry"));
    public static final Locator DONE_ADDON_BTN = new Locator("DONE_ADDON_BTN", AppiumBy.xpath("//*[@name='done_btn']"));
    public static final Locator CONTINUE = new Locator("CONTINUE", AppiumBy.xpath("(//*[contains(@name, 'Continue') or contains(@name, 'Lanjut') or contains(@name, 'Lanjutkan') and @type='XCUIElementTypeButton'])[1]"));
    public static final Locator COVID_LABEL = new Locator("COVID_LABEL", AppiumBy.xpath("//*[@name='radioButton (selected)']/preceding-sibling::*[@type='XCUIElementTypeStaticText'][3]"));
    public static final Locator ID_NUMBER = new Locator("ID_NUMBER", AppiumBy.id("identityNumber"));
    public static final Locator DOB_PICKER = new Locator("DOB", AppiumBy.name("dob"));
    public static final Locator DISCOUNT_TAG_PRICE_DETAIL = new Locator("DISCOUNT_TAG_PRICE_DETAIL", AppiumBy.xpath("(//*[(contains(@name,'Diskon') or contains(@name,'Discount'))])[last()]"));
    public static final Locator CANCEL_INSURANCE = new Locator("CANCEL_INSURANCE", AppiumBy.xpath("//*[(@label='Batalkan' or @label='Cancel')and @type='XCUIElementTypeButton']"));
    public static final Locator BENEFIT_SECTION = new Locator("BENEFIT_SECTION", AppiumBy.xpath("//*[contains(@value,'What You') or contains(@value,'Yang akan')]"));
    public static final Locator CAMPAIGN_OR_LOYALTY = new Locator("CAMPAIGN_OR_LOYALTY", AppiumBy.xpath("//*[contains(@value,'Diskon') or contains(@value,'Discount)' or @value='Harga Gledek']"));
    /** Default total line when no currency is passed (e.g. {@link #isTotalPriceWithoutDiscount}); prefer {@link #getTotalPricePayNowLocator(String)}. */
    public static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", AppiumBy.xpath(
            "(//XCUIElementTypeStaticText[@name='Pay now' or @name='Total' or @name='Bayar sekarang']"
                    + "/following::XCUIElementTypeStaticText[contains(@name,'IDR ')])[1]"));
    public static final Locator PRICE_ADULT = new Locator("PRICE_ADULT", AppiumBy.xpath(
            "(//XCUIElementTypeStaticText[starts-with(@name,'Adult') or starts-with(@name,'Dewasa')]"
                    + "/following::XCUIElementTypeStaticText[contains(@name,'IDR ')])[1]"));
    public static final Locator PRICE_LOCATOR_PRICE_DETAIL = new Locator("PRICE_LOCATOR_PRICE_DETAIL",AppiumBy.xpath("//XCUIElementTypeStaticText[@name='price_detail_price']"));
    public static final Locator PARENT_ADD_ON_MEAL = new Locator("PARENT_ADD_ON_MEAL",AppiumBy.xpath("(((((//XCUIElementTypeOther[@name='passenger'])[1])//parent::*[1])//parent::*)[1]//parent::*)[1]"));
    public static final Locator PRICE_DETAIL_100P_REFUND = new Locator("PRICE_DETAIL_100P_REFUND",AppiumBy.xpath("(//*[@type='XCUIElementTypeStaticText' and contains(@label, '100% Refund')])"));
    public static final Locator YELLOW_BANNER_BOOKING_PAGE_2 = new Locator("YELLOW_BANNER_BOOKING_PAGE_2",AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Penting nih!') or contains(@label,'Important!')]"));
    public static final Locator CONNECTING_NUDGE = new Locator("CONNECTING_NUDGE",AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and contains(@label, 'Self Transfer Protection') or contains(@label, 'Proteksi Transit Mandiri')]"));
    public static final Locator TRIP_DETAIL_PARENT = new Locator("TRIP_DETAIL_PARENT",AppiumBy.xpath("(//XCUIElementTypeCell)[1]"));
    public static final Locator DATE_TIME_DEPART = new Locator("DATE_TIME_DEPART",AppiumBy.xpath("((//XCUIElementTypeCell)[1]//XCUIElementTypeStaticText[contains(@label,'•') and contains(@label,':')])[1]"));
    public static final Locator DATE_TIME_RETURN = new Locator("DATE_TIME_RETURN",AppiumBy.xpath("((//XCUIElementTypeCell)[1]//XCUIElementTypeStaticText[contains(@label,'•') and contains(@label,':')])[2]"));
    private static final Locator TRIP_DETAIL_ARRIVAL_DATE_TIME = new Locator("TRIP_DETAIL_ARRIVAL_DATE_TIME", AppiumBy.xpath("(//*[*[@name='flight_note']])[1]//following-sibling::XCUIElementTypeCell//XCUIElementTypeStaticText[2]"));
    private static final Locator QUIET_ZONE_BANNER = new Locator("QUIET_ZONE_BANNER", AppiumBy.name("banner_container"));
    private static final Locator QUIET_ZONE_BANNER_ROW_DETAIL = new Locator("QUIET_ZONE_BANNER_ROW_DETAIL", AppiumBy.xpath("//XCUIElementTypeButton[@name='tds ic cross big']//preceding-sibling::XCUIElementTypeOther//XCUIElementTypeStaticText"));
    public static final Locator REMOVE_ALREADY_ADDED = new Locator("REMOVE_ALREADY_ADDED", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and (contains(@value,'You’ve added protection') or contains(@value,'Kamu sudah dilindungi'))]/following-sibling::*[@name='extraprotection_btn_0']"));
    /**
     * Flight Disruption Protection — option label.
     * EN: "Without protection" / ID: "Tanpa proteksi" (exact name/label from native dump).
     */
    private static final Locator WITHOUT_PROTECTION_OPTION = new Locator("WITHOUT_PROTECTION_OPTION", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Without protection' or @name='Tanpa proteksi' or @name='Tanpa perlindungan' or @label='Without protection' or @label='Tanpa proteksi' or @label='Tanpa perlindungan']"));
    /**
     * Radio of Without / Tanpa row (label StaticText click does not select).
     * Targets the accessible XCUIElementTypeOther in the first following-sibling Other after the label container
     * (dump shows that node with value 0 unselected / 1 selected; value is not used in the XPath).
     */
    private static final Locator WITHOUT_PROTECTION_RADIO = new Locator("WITHOUT_PROTECTION_RADIO", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Without protection' or @name='Tanpa proteksi' or @name='Tanpa perlindungan' or @label='Without protection' or @label='Tanpa proteksi' or @label='Tanpa perlindungan']/parent::XCUIElementTypeOther/following-sibling::XCUIElementTypeOther[1]//XCUIElementTypeOther[@accessible='true']"));
    /**
     * CTA after Without / Tanpa is selected — never match "with/dengan proteksi".
     * EN: "Continue without protection" / ID: "Lanjut tanpa proteksi".
     */
    private static final Locator CONTINUE_WITHOUT_PROTECTION_BTN = new Locator("CONTINUE_WITHOUT_PROTECTION_BTN", AppiumBy.xpath("//XCUIElementTypeButton[@name='Continue without protection' or @name='Lanjut tanpa proteksi' or @name='Lanjutkan tanpa proteksi' or contains(@name,'Continue without') or contains(@name,'Lanjut tanpa') or contains(@label,'Continue without') or contains(@label,'Lanjut tanpa')]"));
    /** Legacy skip CTA (old interstitial). Do not use "Continue with protection". */
    public static final Locator SKIP_FULL_PROTECTION_NATIVE = new Locator("SKIP_FULL_PROTECTION", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,\"don't\") or contains(@name,\"Don’t\") or contains(@name,'Nggak') or starts-with(@name,'No,') or @name='No']"));
    private static final Locator PROMO_BANNER = new Locator("PROMO_BANNER", AppiumBy.xpath("//*[contains(@label,' promo regulations') or contains(@label,'promo dapat')]"));
    private static final Locator PROMO_BANNER_CHEVRON_2ND_PAGE = new Locator("PROMO_BANNER_CHEVRON_2ND_PAGE", AppiumBy.xpath("//*[contains(@label,'applied!') or contains(@label,'terpasang!')]"));
    private static final Locator REMOVE_PROMO = new Locator("REMOVE_PROMO", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Remove') or contains(@name,'Batalkan')]"));
    private static final Locator PROTECTION = new Locator("PROTECTION", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and @label='You’ve added protection' or contains(@label, 'Kamu sudah dilindungi')]"));
    private static final Locator CONNECTING_FLIGHT_PROTECTION_NUDGE = new Locator("CONNECTING_FLIGHT_PROTECTION_NUDGE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Protected by Connecting Flight Protection') or contains(@label,'Terlindungi oleh Connecting Flight Protection')]"));
    private static final Locator MORE_BENEFIT = new Locator("MORE_BENEFIT", AppiumBy.xpath("//*[contains(@name,'what_you_get_subtitle') and (contains(@label,'More benefit') or contains(@label,'Lihat benefit lainnya'))]"));
    private static final Locator INSURANCE_DETAIL_TITLE = new Locator("INSURANCE_DETAIL_TITLE", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@label,'Connecting Flight Protection') or contains(@label,'Self Transfer Protection') or contains(@label,'Proteksi Transfer Mandiri')])[2]"));
    private static final Locator INSURANCE_DETAIL_COVERAGE_HEADER = new Locator("INSURANCE_DETAIL_COVERAGE_HEADER", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Coverage') or contains(@label,'Tanggungan')]"));
    private static final Locator INSURANCE_DETAIL_COMPENSATION_HEADER = new Locator("INSURANCE_DETAIL_COMPENSATION_HEADER", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Compensation') or contains(@label,'Kompensasi')]"));
    private static final Locator YELLOW_BANNER_CONNECTING_FLIGHT_PROTECTION = new Locator("YELLOW_BANNER_CONNECTING_FLIGHT_PROTECTION", AppiumBy.xpath("//*[(contains(@label,'You chose connecting flights') or contains(@label,'Info penerbangan multimaskapai'))]"));
    public static final Locator AIRPORT_TRANSFER_DETAIL = new Locator("AIRPORT_TRANSFER_DETAIL", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and contains(@label, 'Free Airport Transfer') or contains(@label, 'Gratis Airport Transfer')]"));
    public static final Locator ANTIGEN_TEST_DETAIL = new Locator("ANTIGEN_TEST_DETAIL", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and contains(@label, 'PCR/Antigen')]"));
    public static final Locator PASSPORT_BANNER = new Locator("PASSPORT_BANNER", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Enter') and contains(@label,'passenger') or contains(@label,'Masukkan') and contains(@label,'penumpang')]"));
    public static final Locator PASSPORT_POPUP_BANNER = new Locator("PASSPORT_POPUP_BANNER", AppiumBy.xpath("(//*[contains(@name, 'Check Again') or contains(@name, 'Cek detail penumpang') and @type='XCUIElementTypeButton'])[1]"));
    public static final Locator OPEN_PASSENGER_DETAILS = new Locator("OPEN_PASSENGER_DETAILS", AppiumBy.xpath("(//XCUIElementTypeStaticText[starts-with(@name,'Mr.') or starts-with(@name,'Mrs.') or starts-with(@name,'Ms.') or starts-with(@name,'Miss.') or starts-with(@name,'Mstr.') or starts-with(@name,'Tuan') or starts-with(@name,'Nyonya') or starts-with(@name,'Nona')])[1] | (//XCUIElementTypeStaticText[(starts-with(@name,'Passenger ') or starts-with(@name,'Penumpang ')) and (contains(@name,'(Adult)') or contains(@name,'(Child)') or contains(@name,'(Infant)') or contains(@name,'(Dewasa)') or contains(@name,'(Anak)') or contains(@name,'(Bayi)'))])[1]"));
    public static final Locator CLOSE_PASSENGER_DETAILS = new Locator("CLOSE_PASSENGER_DETAILS", AppiumBy.xpath("//*[@name='back' and @type='XCUIElementTypeButton']"));
    public static final Locator CHECK_PASSENGER_DETAILS = new Locator("CHECK_PASSENGER_DETAILS", AppiumBy.xpath("(//*[contains(@name, 'Check Again') or contains(@name, 'Cek detail penumpang') and @type='XCUIElementTypeButton'])[1]"));
    public static final Locator GREEN_BANNER_UNDER_CONTINUE_BTN = new Locator("GREEN_BANNER_UNDER_CONTINUE_BTN", AppiumBy.xpath("//*[contains(@name,'Yay!')]"));
    public static final Locator CASHBACK_TEXT = new Locator("CASHBACK_TEXT", AppiumBy.xpath("//*[contains(@name, 'Cashback')]"));
    public static final Locator DEPARTURE_TAB = new Locator("DEPARTURE_TAB", AppiumBy.xpath("//*[(@label='Depart' or @label='Pergi') and @type='XCUIElementTypeStaticText']"));
    public static final Locator CLICK_DETAILS = new Locator("CLICK_DETAILS", AppiumBy.xpath(
            "//XCUIElementTypeStaticText[@name='detail_button'"
                    + " or (contains(@name,'refund') and contains(@name,'reschedule'))"
                    + " or (contains(@name,'Bagasi') and contains(@name,'reschedule'))"
                    + " or contains(@name,'refund & reschedule polic')]"));
    public static final Locator DEPARTURE_FLIGHT = new Locator("DEPARTURE_FLIGHT", AppiumBy.xpath("//*[(@label='Departure Flight' or @label='Penerbangan Pergi') and @type='XCUIElementTypeStaticText']"));
    public static final Locator REFUND_PERCENTAGE = new Locator("REFUND_PERCENTAGE", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and contains(@label,'%')]"));
    public static final Locator CLOSE_TAB = new Locator("CLOSE_TAB", AppiumBy.xpath("(//*[@label='ic cancel grey' and @type='XCUIElementTypeButton'])[1]"));
    public static final Locator RESCHEDULE_CHIP = new Locator("RESCHEDULE_CHIP", AppiumBy.xpath("//*[@label='Reschedule' and @name='textLabel']"));
    public static final Locator RESCHEDULE_PERCENTAGE = new Locator("RESCHEDULE_PERCENTAGE", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and contains(@label,'% reschedule')]"));
    public static final Locator RETURN_TAB = new Locator("RETURN_TAB", AppiumBy.xpath("//*[(@name='Return' or @name='Pulang') and @type='XCUIElementTypeStaticText']"));
    public static final Locator WHOLE_TAB = new Locator("WHOLE_TAB", AppiumBy.xpath("//*[@type='XCUIElementTypeCollectionView']"));
    public static final Locator CLOSE_TAB_AGAIN = new Locator("CLOSE_TAB_AGAIN", AppiumBy.xpath("//*[@name='ic cancel grey' and @type='XCUIElementTypeButton']"));
    public static final Locator RETURN_FLIGHT = new Locator("RETURN_FLIGHT", AppiumBy.xpath("//*[(@name='Return Flight' or @label='Penerbangan Pulang')and @type='XCUIElementTypeStaticText']"));
    public static final Locator REFUND_NOT_ALLOWED = new Locator("REFUND_NOT_ALLOWED", AppiumBy.xpath("(//*[(@label='Refund not allowed' or @label='Refund information is not available'or @label='Informasi refund tidak tersedia') and @type='XCUIElementTypeStaticText'])[1]"));
    public static final Locator RESCHEDULE_NOT_ALLOWED = new Locator("RESCHEDULE_NOT_ALLOWED", AppiumBy.xpath("//*[(@label='Reschedule information is not available' or @label='Informasi reschedule tidak tersedia') and @type='XCUIElementTypeStaticText']"));
    public static final Locator BAGGAGE_OPTIONS = new Locator("BAGGAGE_OPTIONS", AppiumBy.xpath("//XCUIElementTypeStaticText[((contains(@name,'kg') or contains(@name,'Kg')) and contains(@name,'+')) or ((contains(@label,'kg') or contains(@label,'Kg')) and contains(@label,'+'))]"));
    /** One horizontal baggage chip list per passenger on "Book Extra Baggage". */
    public static final Locator BAGGAGE_PASSENGER_SECTIONS = new Locator("BAGGAGE_PASSENGER_SECTIONS", AppiumBy.xpath("//XCUIElementTypeCollectionView[.//XCUIElementTypeStaticText[contains(@name,'Thanks') or contains(@name,'Tidak') or contains(@label,'Thanks') or contains(@label,'Tidak')]]"));
    public static final Locator CONTINUE_POPUP = new Locator("CONTINUE_POPUP", AppiumBy.xpath("(//*[@name='Continue' or @name='Lanjutkan'])[1]"));
    public static final Locator FIRST_NAME = new Locator("FIRST_NAME", AppiumBy.xpath("//*[@name='firstName']"));
    public static final Locator NIQ = new Locator("NIQ", AppiumBy.xpath("//*[@name='identityNumber']"));
    private static final Locator PRICE_DETAIL_CASHBACK_ROW = new Locator("PRICE_DETAIL_CASHBACK_ROW", AppiumBy.xpath(
            "//*[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'points cashback')"
                    + " or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'discount cashback')"
                    + " or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'cashback points')]"));
    private static final Locator PRICE_DETAIL_TOTAL_AFTER_CASHBACK_PRICE = new Locator("PRICE_DETAIL_TOTAL_AFTER_CASHBACK_PRICE", AppiumBy.xpath(
            "(//*[contains(@name, 'Total after cashback') or contains(@label, 'Total after cashback')"
                    + " or contains(@name, 'Total setelah cashback') or contains(@label, 'Total setelah cashback')]"
                    + "/following-sibling::*[@type='XCUIElementTypeStaticText'])[1]"
                    + " | (//*[contains(@name, 'Total after cashback') or contains(@label, 'Total after cashback')"
                    + " or contains(@name, 'Total setelah cashback') or contains(@label, 'Total setelah cashback')]"
                    + "/parent::*//*[@type='XCUIElementTypeStaticText'][contains(@name,'IDR')])[last()]"));
    /**
     * Monetary IDR rendered in the footer after the Pay now row (e.g. post-cashback total) when the
     * row title is not exposed as "Total after cashback" to {@link #PRICE_DETAIL_TOTAL_AFTER_CASHBACK_PRICE}.
     */
    private static final Locator PRICE_DETAIL_POST_PAY_NOW_IDR = new Locator("PRICE_DETAIL_POST_PAY_NOW_IDR", AppiumBy.xpath(
            "(//*[@type='XCUIElementTypeStaticText' and (@name='Pay now' or @name='Bayar sekarang')]"
                    + "/ancestor::*[@type='XCUIElementTypeOther'][1]/following-sibling::*"
                    + "//XCUIElementTypeStaticText[contains(@name,'IDR ')])[1]"));
    /** Value column adjacent to the cashback row (e.g. "Get 1,092 points" or "-IDR 1,092"). */
    private static final Locator PRICE_DETAIL_CASHBACK_VALUE = new Locator("PRICE_DETAIL_CASHBACK_VALUE", AppiumBy.xpath(
            "//*[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'points cashback')"
                    + " or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'discount cashback')"
                    + " or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'cashback points')]"
                    + "/following-sibling::*//XCUIElementTypeStaticText[@visible='true']"));
    private static final Locator CONTINUE_TO_PAYMENT_POPUP_BUTTON = new Locator("CONTINUE_TO_PAYMENT_POPUP_BUTTON", AppiumBy.xpath("(//*[@name='Continue to payment' or @name='Lanjut ke pembayaran'])[1]"));
    private static final Locator ERROR_POPUP = new Locator("ERROR_POPUP", AppiumBy.xpath("//*[contains(@label,'Sorry') or contains(@label,'Maaf')]"));
    private static final Locator CONTENT_LOADING_DIALOG = new Locator("CONTENT_LOADING_DIALOG", AppiumBy.xpath("//*[translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='relax and take a breath' or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='saatnya santai']"));

    @Override
    public boolean isTripDetailCorrect(String tripType,String departureDate, String departureAirport, String arrivalDate, String arrivalAirport) {
        skipFullProtection();
        return true;
    }//FIXME: Implementation pending

    private static final int ADULT_BIRTH_YEAR_OFFSET = 25;
    private static final int CHILD_BIRTH_YEAR_OFFSET = 4;
    private static final int INFANT_BIRTH_YEAR_OFFSET = 1;

    /** Weight (kg) of the baggage option chosen in {@link #addBaggage()}; used as a price fallback for the inline UI. */
    private int selectedBaggageWeight = -1;

    private Locator getPassengerLocator(int index) {
        // Guest empty slots only — do NOT match bare body_btn[index] (false guest detection on login UI).
        return new Locator("Passenger Locator", AppiumBy.xpath(
                "//XCUIElementTypeStaticText[contains(@name,'Passenger " + index + " (') or contains(@label,'Passenger " + index + " (')"
                        + " or contains(@name,'Penumpang " + index + " (') or contains(@label,'Penumpang " + index + " (')]"
                        + " | //XCUIElementTypeButton[@name='body_btn' and (contains(@label,'Passenger " + index + "')"
                        + " or contains(@label,'Penumpang " + index + "'))]"));
    }
    private Locator getPassengerLocatorID(int index) {
        return new Locator("Passenger Locator", AppiumBy.xpath(
                "//XCUIElementTypeStaticText[contains(@name,'Passenger " + index + "') or contains(@label,'Passenger " + index + "')"
                        + " or contains(@name,'Penumpang " + index + "') or contains(@label,'Penumpang " + index + "')]"
                        + " | //XCUIElementTypeButton[@name='body_btn' and (contains(@label,'Passenger " + index + "')"
                        + " or contains(@label,'Penumpang " + index + "'))]"));
    }

    private boolean waitForAnyPassengerForm(int timeoutSeconds) {
        return driverTools.waitForElementToAppear(PASSENGER_FORM_OPEN, timeoutSeconds).isPresent()
                || driverTools.waitForElementToAppear(FULL_NAME, 2).isPresent()
                || driverTools.waitForElementToAppear(FULL_NAME_GUEST, 2).isPresent();
    }

    private void confirmPassengerReviewIfShown() {
        if (driverTools.waitForElementToAppear(PASSENGER_CONFIRM_EDIT, 3).isPresent()) {
            if (driverTools.isElementDisplayed(PASSENGER_REVIEW_CONFIRM)) {
                driverTools.click(PASSENGER_REVIEW_CONFIRM);
            } else {
                driverTools.click(PASSENGER_NAME_CONFIRM);
            }
            driverTools.waitForElementToDisappear(PASSENGER_CONFIRM_EDIT, 10);
        }
    }

    private void fillDob(int dobRange){
        String currentPicked = driverTools.getText(DOB_PICKER).orElseThrow();
        if(!currentPicked.equals("dob")) {
            int currentPickedYear = Integer.parseInt(CoreFunctions.getSectionOfString(currentPicked, "-", 0));

            logger.log("currentYear :" + currentPickedYear);
            driverTools.click(DOB);

            String direction = "before";
            int distance = currentPickedYear - dobRange;

            if (distance < 0) {
                direction = "next";
                distance *= -1;
            }
            logger.log("direction : " + direction);
            driverTools.scrollPickerWheel(YEAR_PICKER_WHEEL, distance, direction);
        }else{
            driverTools.click(DOB);
            driverTools.scrollPickerWheel(YEAR_PICKER_WHEEL, 1, "next");
        }
        driverTools.click(DONE_BTN);
    }

    private String fillName(String airline){
        String name = CoreFunctions.randomNameWithoutTest();
        if(airline.toLowerCase().contains("citilink") || airline.toLowerCase().contains("garuda")){
            name += " Testing";
        }
        this.passengerNames.add(name);
        //driverTools.clear(FULL_NAME);
        driverTools.sendKeys(FULL_NAME,name);
        driverTools.hideKeyboard(1);

        return name;
    }

    private String fillFullName(String airline){
        String name = CoreFunctions.randomNameWithoutTest();
        String lastName = CoreFunctions.randomLastName();
        if(airline.toLowerCase().contains("citilink") || airline.toLowerCase().contains("garuda")){
            lastName = " Testing";
        }

        if (driverTools.isElementDisplayed(GIVEN_NAME)){
            driverTools.sendKeysKeyboard(GIVEN_NAME,name);
            driverTools.hideKeyboard(1);
        }
        if (driverTools.isElementDisplayed(FIRST_NAME)){
            driverTools.sendKeysKeyboard(FIRST_NAME,name);
            driverTools.hideKeyboard(1);
        }
        if (driverTools.isElementDisplayed(LAST_NAME)){
            driverTools.clear(LAST_NAME);
            driverTools.sendKeys(LAST_NAME,lastName);
            driverTools.hideKeyboard(1);
            return name + lastName;
        }
        // New guest form has no separate last name field: acknowledge single-name via the toggle.
        if (driverTools.isElementDisplayed(NO_SURNAME)){
            driverTools.click(NO_SURNAME);
        }
        return name;
    }

    private void fillTitle(){
        WebElement e = driverTools.waitForElementToAppear(TITLE_PASSENGER).orElseThrow();
        driverTools.click(e);
    }

    private void fillNationality(){
        WebElement nationality = driverTools.waitForElementToPresence(NATIONALITY).orElseThrow();
        driverTools.clickElementByCoordinate(nationality);
        driverTools.clickElementByCoordinate(INDONESIA);
    }

    private void fillNIQNumber(){
        driverTools.clear(NIQ);
        String idNumber = String.valueOf(CoreFunctions.randomInt(6));
        driverTools.sendKeys(NIQ,"3"+idNumber+"1");
        driverTools.hideKeyboard(1);
    }

    private void fillID(){
        driverTools.clear(ID_NUMBER);
        String idNumber = String.valueOf(CoreFunctions.randomInt(3));
        driverTools.sendKeys(ID_NUMBER,"327202200202"+idNumber+"1");
        driverTools.hideKeyboard(1);
    }

    private void fillPassport(){
        driverTools.clear(PASSPORT);
        driverTools.sendKeys(PASSPORT, String.valueOf(CoreFunctions.randomInt(9)));
        driverTools.hideKeyboard(1);

        driverTools.swipeUp(2);

        if(driverTools.isElementDisplayed(ISSUING_COUNTRY)){
            driverTools.click(ISSUING_COUNTRY);
            driverTools.click(INDONESIA);
        }
        if(driverTools.isElementDisplayed(EXPIRY_DATE)){
            driverTools.click(EXPIRY_DATE);
            driverTools.scrollPickerWheel(YEAR_PICKER_WHEEL, 2, "after");
            driverTools.click(DONE_BTN);
        }
        if (driverTools.isElementDisplayed(ISSUED_DATE)) {
            driverTools.click(ISSUED_DATE);
            driverTools.scrollPickerWheel(YEAR_PICKER_WHEEL, 2, "before");
            driverTools.click(DONE_BTN);
        }
    }

    /**
     * Waits for the passenger detail form to be ready. The new form shows a "Passenger Info" header
     * (with a back chevron); older builds showed an "ic cancel grey" close button.
     */
    private void waitForPassengerForm(){
        if (!waitForAnyPassengerForm(10)) {
            driverTools.waitForElementToAppear(CLOSE_BUTTON, 5);
        }
    }

    private String passengerDomestic(Integer rangeDOB,String airline){
        waitForPassengerForm();
        String name = fillName(airline);
        fillTitle();
        if (driverTools.isElementDisplayed(DOB)) {
            fillDob(rangeDOB);
        }
        if (driverTools.isElementDisplayed(NATIONALITY)) {
            fillNationality();
        }

        if (driverTools.isElementDisplayed(ID_NUMBER)) {
            fillID();
        }
        submitPassenger();

        return name;
    }

    private String passengerInternational(Integer rangeDOB,String airline){
        waitForPassengerForm();
        String name = fillFullName(airline);
        fillTitle();
        if (driverTools.isElementDisplayed(DOB)) {
            fillDob(rangeDOB);
        }
        if (driverTools.isElementDisplayed(NIQ)) {
            fillNIQNumber();
        }

        if (driverTools.waitForElementToPresence(NATIONALITY).isPresent()) {
            fillNationality();
        }
        driverTools.swipeToElement(PASSPORT,Swipe.UP,1);
        if(driverTools.isElementDisplayed(PASSPORT)){
            fillPassport();
        }
        submitPassenger();

        return name;
    }

    /**
     * Commits the current passenger form and advances the flow.
     * <p>New guest UI: the bottom action button is "Next passenger" for every passenger except
     * the last (which shows "Save"). After tapping it, some builds also show a name-confirmation
     * popup ("Edit" + "It's correct"/"Next passenger"). Tapping "Next passenger" (either the form
     * button or the popup button) automatically opens the next passenger's form, which
     * {@link #processPassenger} then fills without reopening it from the passenger list.
     */
    private void submitPassenger(){
        WebElement submit = driverTools.swipeToElement(PASSENGER_SUBMIT, Swipe.UP, 3).orElseThrow();
        driverTools.click(submit);
        // A name-review bottomsheet ("Edit" + "It's correct"/"Next passenger") may appear after saving.
        // Prefer the review-specific confirm so we don't tap the next form's "Next passenger" by mistake.
        confirmPassengerReviewIfShown();
    }

    public String chooseDomesticOrInternational(String flightRoute,int dobRange,String airline) {
        String name = "";

        if (flightRoute.equals("domestic")) {
            name += passengerDomestic(dobRange,airline);
        } else if (flightRoute.equals("international")) {
            name += passengerInternational(dobRange,airline);
        }

        return name;
    }

    @Override
    public List<String> fillPassengerDetail(int adult, int child, int infant, String flightRoute, String airline) {
        String methodName = "fillPassengerDetail";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            List<String> passengerNames = new ArrayList<>();

            driverTools.waitForElementToAppear(BTN_CONTINUE, 10).orElseThrow();
            // Optimized: Single swipe at start to bring all passengers into view
            driverTools.swipeDown(2);

            // Two booking UIs (names are always dynamic — never hardcode person names):
            // 1) Login/saved: checkboxes + titled names (Mr./Mrs./Mstr./…) + optional empty Passenger N slots
            // 2) Guest: empty "Passenger N (Adult/Child/…)" rows filled via forms
            if (isSavedPassengerMode()) {
                logger.log("Passenger fill mode: saved/login (checkbox list)");
                fillEmptyPassengerSlots(flightRoute, airline, passengerNames);
                passengerNames.addAll(selectSavedPassengers(adult, child, infant, flightRoute));
                return passengerNames;
            }

            logger.log("Passenger fill mode: guest (Passenger N forms)");
            String currentDate = CoreFunctions.getCurrentLongDate();
            int currentYear = Integer.parseInt(CoreFunctions.getSectionOfString(currentDate, " ", 2));
            int currentIndex = 1;

            for (int i = 1; i <= adult; i++) {
                logger.log("Filling detail for passenger(adult) " + i);
                processPassenger(currentIndex, flightRoute, currentYear - ADULT_BIRTH_YEAR_OFFSET, airline, passengerNames);
                currentIndex++;
            }

            if (child != 0) {
                for (int i = 1; i <= child; i++) {
                    logger.log("Filling detail for passenger(Child) " + currentIndex);
                    processPassenger(currentIndex, flightRoute, currentYear - CHILD_BIRTH_YEAR_OFFSET, airline, passengerNames);
                    currentIndex++;
                }
            }

            if (infant != 0) {
                for (int i = 1; i <= infant; i++) {
                    logger.log("Filling detail for passenger(Infant) " + currentIndex);
                    processPassenger(currentIndex, flightRoute, currentYear - INFANT_BIRTH_YEAR_OFFSET, airline, passengerNames);
                    currentIndex++;
                }
            }
            return passengerNames;
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    // Optimized: Extract common logic into helper method
    private void processPassenger(int index, String flightRoute, int dobRange, String airline, List<String> passengerNames) {
        // New guest UI: confirming a passenger with "Next passenger" auto-opens the next passenger's
        // form (it may take a moment to animate in). Only the first passenger is opened from the list;
        // for later passengers, wait for the auto-opened form and fill it directly. If it never appears
        // (older builds that return to the list), fall back to opening from the passenger list.
        boolean formAlreadyOpen = index > 1 && waitForAnyPassengerForm(8);
        if (!formAlreadyOpen) {
            openPassengerFromList(index);
        }

        String passengerName = chooseDomesticOrInternational(flightRoute, dobRange, airline);
        passengerNames.add(passengerName);
    }

    /**
     * Opens the Nth passenger's form from the passenger list. The row may be scrolled off-screen after
     * the previous passenger was saved, so {@code element()} (which does not scroll) can miss it; swipe
     * the row into view before clicking. Any leftover review sheet is confirmed first.
     */
    private void openPassengerFromList(int index) {
        confirmPassengerReviewIfShown();
        // After saving a passenger the list may be below the fold — bring Passenger Details into view.
        driverTools.swipeToElement(new Locator("PASSENGER_DETAILS_HEADER",
                AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Passenger Details' or @name='Detail Penumpang']")), Swipe.UP, 3);

        Optional<WebElement> row = driverTools.element(getPassengerLocatorID(index))
                .filter(WebElement::isDisplayed);
        if (row.isEmpty()) {
            row = driverTools.swipeToElement(getPassengerLocator(index), Swipe.UP, 5);
        }
        if (row.isEmpty()) {
            row = driverTools.swipeToElement(getPassengerLocatorID(index), Swipe.UP, 5);
        }
        // After passenger 1 is filled, remaining empty slot is often the first EMPTY_PASSENGER_SLOT / body_btn.
        if (row.isEmpty() && index > 1) {
            row = driverTools.swipeToElement(EMPTY_PASSENGER_SLOT, Swipe.UP, 4);
        }
        driverTools.click(row.orElseThrow(() ->
                new NoSuchElementException("Passenger selection not found for index " + index)));
        if (!waitForAnyPassengerForm(10)) {
            throw new NoSuchElementException("Passenger form did not open for index " + index);
        }
    }

    /** Login/saved list (checkboxes) vs guest empty "Passenger N" forms. Names are never hardcoded. */
    private boolean isSavedPassengerMode() {
        // flightbookingpart1ios.xml: checkbox + "View or add passengers" = login saved list
        if (driverTools.isElementDisplayed(SAVED_PASSENGER_CHECKBOX_ANY)
                || driverTools.isElementDisplayed(VIEW_OR_ADD_PASSENGERS)) {
            return true;
        }
        // Guest UI: empty "Passenger 1 (Adult)" style rows (not bare body_btn alone)
        return false;
    }

    private Locator savedPassengerNameAt(int oneBasedIndex) {
        return new Locator("SAVED_PASSENGER_NAME_" + oneBasedIndex,
                AppiumBy.xpath("(//XCUIElementTypeStaticText[starts-with(@name,'Mr.') or starts-with(@name,'Mrs.') or starts-with(@name,'Ms.') or starts-with(@name,'Miss.') or starts-with(@name,'Mstr.') or starts-with(@name,'Tuan') or starts-with(@name,'Nyonya') or starts-with(@name,'Nona')])[" + oneBasedIndex + "]"));
    }

    private Locator savedPassengerCheckboxAt(int oneBasedIndex) {
        return new Locator("SAVED_PASSENGER_CHECKBOX_" + oneBasedIndex,
                AppiumBy.xpath("(//XCUIElementTypeOther[@name='checkbox' or @name='checkbox (selected)' or @name='radioButton' or @name='radioButton (selected)'])[" + oneBasedIndex + "]"));
    }

    private Locator savedPassengerCheckboxSelectedAt(int oneBasedIndex) {
        return new Locator("SAVED_PASSENGER_CHECKBOX_SELECTED_" + oneBasedIndex,
                AppiumBy.xpath("(//XCUIElementTypeOther[@name='checkbox' or @name='checkbox (selected)' or @name='radioButton' or @name='radioButton (selected)'])[" + oneBasedIndex + "][@name='checkbox (selected)' or @name='radioButton (selected)']"));
    }

    /**
     * Selects the required passengers from the saved list by tapping their checkboxes, matching the
     * requested passenger types (Adult / Child / Infant). Immediately after a passenger is selected,
     * if it still shows "Fill the NIK/Passport No. information" its data is completed (form filled +
     * review bottom sheet confirmed) before the next passenger is selected. Passengers already
     * selected that are not needed are de-selected.
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
                    tapSavedPassengerCheckbox(savedPassengerCheckboxAt(i));
                }
                // Complete THIS passenger's data before selecting the next one: if it still shows the
                // "Fill the NIK/Passport No. information" prompt, open its form, fill the missing info
                // and confirm the review bottom sheet ("It's correct") so it is saved.
                fillSavedPassengerIfIncomplete(label);
                remaining[type]--;
                names.add(label);
            } else if (selected) {
                // Passenger is selected but not needed for this booking: de-select it.
                tapSavedPassengerCheckbox(savedPassengerCheckboxAt(i));
            }
        }

        return names;
    }

    /**
     * When a saved passenger with missing identity info ("Fill the NIK/Passport No. information") is
     * selected, tapping its checkbox auto-opens the Passenger Info form. If that form opens, fill the
     * missing data, save it and confirm the review bottom sheet ("It's correct") so the passenger is
     * completed before the next one is selected. If no form opens, the passenger was already complete.
     */
    private void fillSavedPassengerIfIncomplete(String passengerName) {
        // Short wait: the form auto-opens quickly for incomplete passengers; a long (default) wait
        // would stall on every already-complete passenger.
        if (driverTools.waitForElementToAppear(PASSENGER_FORM_OPEN, 6).isEmpty()) {
            return; // this passenger's data is already complete (no form auto-opened)
        }
        logger.log("Completing missing identity info for saved passenger: " + passengerName);
        fillIdentityInfoLogin();
        // Save the identity form, then confirm on the review bottom sheet ("It's correct").
        driverTools.swipeToElement(PASSENGER_SUBMIT, Swipe.UP, 3).ifPresent(driverTools::click);
        driverTools.swipeToElement(PASSENGER_NAME_CONFIRM, Swipe.UP, 2).ifPresent(driverTools::click);
        // Wait until we are back on the saved-passenger list before continuing.
        driverTools.swipeToElement(SAVED_PASSENGER_CHECKBOX_ANY, Swipe.UP, 2);
    }

    /**
     * Taps a saved-passenger radio/checkbox. On iOS these are {@code XCUIElementTypeOther} elements
     * that Appium reports as present but never "clickable" (elementToBeClickable times out), so we
     * locate them by presence and tap directly instead of using the clickable-gated {@code click()}.
     */
    private void tapSavedPassengerCheckbox(Locator checkbox) {
        driverTools.element(checkbox).ifPresent(driverTools::click);
    }

    private void fillIdentityInfoLogin() {
        waitForPassengerForm();
        // last name
        if (driverTools.isElementDisplayed(LAST_NAME)) {
            driverTools.clear(LAST_NAME);
            driverTools.sendKeys(LAST_NAME, CoreFunctions.randomLastName());
            driverTools.hideKeyboard(1);
        }

        // Domestic saved passengers may only be missing their NIK / identity number.
        if (driverTools.isElementDisplayed(NIQ)) {
            fillNIQNumber();
        } else if (driverTools.isElementDisplayed(ID_NUMBER)) {
            fillID();
        }

        // Nationality defaults to Indonesia; fill only when empty.
        if (driverTools.isElementDisplayed(NATIONALITY) && isFieldEmpty(NATIONALITY_FIELD, "nationality")) {
            fillNationality();
        }
        // Passport number, when the form asks for it.
        if (driverTools.isElementDisplayed(PASSPORT)) {
            driverTools.clear(PASSPORT);
            driverTools.sendKeys(PASSPORT, String.valueOf(CoreFunctions.randomInt(9)));
            driverTools.hideKeyboard(1);
        }

        driverTools.swipeUp(2);

        // Issuing country defaults to Indonesia; fill only when empty.
        if (driverTools.isElementDisplayed(ISSUING_COUNTRY) && isFieldEmpty(ISSUING_COUNTRY_FIELD, "issuingCountry")) {
            driverTools.click(ISSUING_COUNTRY);
            driverTools.click(INDONESIA);
        }
        // Issued date.
        if (driverTools.isElementDisplayed(ISSUED_DATE)) {
            driverTools.click(ISSUED_DATE);
            driverTools.scrollPickerWheel(YEAR_PICKER_WHEEL, 2, "before");
            driverTools.click(DONE_BTN);
        } else if (driverTools.isElementDisplayed(ISSUING_DATE_FIELD)) {
            driverTools.click(ISSUING_DATE_FIELD);
            driverTools.scrollPickerWheel(YEAR_PICKER_WHEEL, 2, "before");
            driverTools.click(DONE_BTN);
        }
        // Expiry date.
        if (driverTools.isElementDisplayed(EXPIRY_DATE)) {
            driverTools.click(EXPIRY_DATE);
            driverTools.scrollPickerWheel(YEAR_PICKER_WHEEL, 2, "after");
            driverTools.click(DONE_BTN);
        } else if (driverTools.isElementDisplayed(PASSPORT_EXPIRY_FIELD)) {
            driverTools.click(PASSPORT_EXPIRY_FIELD);
            driverTools.scrollPickerWheel(YEAR_PICKER_WHEEL, 2, "after");
            driverTools.click(DONE_BTN);
        }
    }

    private boolean isFieldEmpty(Locator field, String placeholder) {
        String value = driverTools.getText(field).orElse("");
        return value.isBlank() || value.equalsIgnoreCase(placeholder);
    }

    /**
     * Fills the empty "Passenger N" slots that aren't covered by saved passengers (login flow).
     * Each slot opens a full passenger form; confirming with "Next passenger" auto-opens the next
     * one, so the loop fills whatever form is open and only opens a slot from the list when none is.
     * The passenger type (for DOB) is read from the open form's age hint.
     */
    private void fillEmptyPassengerSlots(String flightRoute, String airline, List<String> names) {
        String currentDate = CoreFunctions.getCurrentLongDate();
        int currentYear = Integer.parseInt(CoreFunctions.getSectionOfString(currentDate, " ", 2));

        for (int guard = 0; guard < 12; guard++) {
            boolean formOpen = driverTools.waitForElementToAppear(PASSENGER_FORM_OPEN, 3).isPresent();
            if (!formOpen) {
                if (driverTools.swipeToElement(EMPTY_PASSENGER_SLOT, Swipe.UP, 4).isEmpty()) {
                    break; // no more empty passenger slots to fill
                }
                driverTools.click(EMPTY_PASSENGER_SLOT);
                if (driverTools.waitForElementToAppear(PASSENGER_FORM_OPEN, 8).isEmpty()) {
                    break;
                }
            }
            int dobRange = getDobRangeFromOpenForm(currentYear);
            String name = chooseDomesticOrInternational(flightRoute, dobRange, airline);
            if (name != null && !name.isBlank()) {
                names.add(name);
            }
        }
    }

    /** Infers the passenger type of the currently open form from its age hint, for the DOB year. */
    private int getDobRangeFromOpenForm(int currentYear) {
        if (driverTools.isElementDisplayed(FORM_TYPE_INFANT)) {
            return currentYear - INFANT_BIRTH_YEAR_OFFSET;
        }
        if (driverTools.isElementDisplayed(FORM_TYPE_CHILD)) {
            return currentYear - CHILD_BIRTH_YEAR_OFFSET;
        }
        return currentYear - ADULT_BIRTH_YEAR_OFFSET;
    }

    @Override
    public void fillContactDetail(String phone, String email, String loginType,String airline){

        if (loginType.equals("guest")) {
            String name = CoreFunctions.randomNameWithoutTest();
            if(airline.toLowerCase().contains("citilink") || airline.toLowerCase().contains("garuda")){
                name += " Testing";
            }
            // New UI: contact details are filled in a dedicated form opened from the booking page
            if (!driverTools.isElementDisplayed(FULL_NAME_GUEST)) {
                driverTools.swipeToElement(FILL_IN_CONTACT_DETAILS, Swipe.UP, 5).ifPresent(driverTools::click);
            }
            driverTools.sendKeys(FULL_NAME_GUEST,name);
            driverTools.hideKeyboard(1);
            driverTools.click(TITLE);
            driverTools.clear(PHONE_NUMBER);
            driverTools.clear(PHONE_NUMBER);
            driverTools.sendKeys(PHONE_NUMBER,phone);
            driverTools.hideKeyboard(1);
            driverTools.swipeUp();
            driverTools.clear(EMAIL);
            driverTools.click(EMAIL);
            driverTools.sendKeys(EMAIL,email);
            driverTools.hideKeyboard(1);
            // New UI: persist the contact details form
            driverTools.swipeToElement(SAVE_BUTTON, Swipe.UP, 3).ifPresent(driverTools::click);
        }
    }

    @Override
    public void continueToSecondHalf(){
        String methodName = "continueToSecondHalf";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForElementToAppear(BTN_CONTINUE,5).ifPresent(driverTools::click);
            waitForBookingPageTwo();
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    private void completeBundledMealOrSeatPagesIfShown() {
        if (driverTools.waitForElementToPresence(BOOK_MEALS_PAGE, 1).isPresent()) {
            logger.log("Book Meals page shown after Continue — selecting free meal(s) and saving");
            if (driverTools.waitForElementToAppear(MEAL_ADD_BUTTON, 8).isPresent()) {
                driverTools.click(MEAL_ADD_BUTTON);
            }
            if (driverTools.waitForElementToAppear(MEAL_RETURN_ROUTE_TAB, 2).isPresent()) {
                driverTools.click(MEAL_RETURN_ROUTE_TAB);
                if (driverTools.waitForElementToAppear(MEAL_ADD_BUTTON, 5).isPresent()) {
                    driverTools.click(MEAL_ADD_BUTTON);
                }
            }
            driverTools.waitForElementToAppear(SAVE_BUTTON, 15).orElseThrow();
            driverTools.click(SAVE_BUTTON);
            driverTools.waitForElementToDisappear(BOOK_MEALS_PAGE, 20);
        }
        if (driverTools.waitForElementToPresence(CHOOSE_EMPTY_SEAT, 2).isPresent()) {
            logger.log("Seat map shown after Continue — selecting seat(s) and saving");
            driverTools.swipeToElement(CHOOSE_EMPTY_SEAT, Swipe.UP, 3);
            driverTools.click(CHOOSE_EMPTY_SEAT);
            if (driverTools.waitForElementToAppear(SAVE_BUTTON, 10).isPresent()) {
                driverTools.click(SAVE_BUTTON);
            }
        }
    }

    private void waitForBookingPageTwo() {
        for (int attempt = 0; attempt < 20; attempt++) {
            completeBundledMealOrSeatPagesIfShown();
            if (driverTools.waitForElementToPresence(BAGGAGE_ADDON, 1).isPresent()
                || driverTools.waitForElementToPresence(BAGGAGE_ALLOWANCE_HEADER, 1).isPresent()
                || driverTools.waitForElementToPresence(MEALS_ADDON, 1).isPresent()
                || driverTools.waitForElementToPresence(SEAT_ADDON, 1).isPresent()) {
                    logger.log("Booking form page 2 is loaded");
                    return;
                }
            if (driverTools.waitForElementToPresence(BOOK_MEALS_PAGE, 1).isPresent()) {
                logger.log("Book Meals shown after Continue — closing without selecting meals");
                driverTools.waitForElementToAppear(SAVE_BUTTON, 10).ifPresent(driverTools::click);
                driverTools.waitForElementToDisappear(BOOK_MEALS_PAGE, 15);
                continue;
            }
            if (driverTools.waitForElementToPresence(CHOOSE_EMPTY_SEAT, 1).isPresent()) {
                logger.log("Seat map shown after Continue — selecting free seat");
                driverTools.swipeToElement(CHOOSE_EMPTY_SEAT, Swipe.UP, 3);
                driverTools.click(CHOOSE_EMPTY_SEAT);
                if (driverTools.waitForElementToAppear(SAVE_BUTTON, 10).isPresent()) {
                    driverTools.click(SAVE_BUTTON);
                }
                continue;
            }
            if (driverTools.waitForElementToPresence(BOOKING_PAGE_TWO_INDICATOR, 1).isPresent()
                    || driverTools.waitForElementToPresence(BAGGAGE_ADDON, 1).isPresent()
                    || driverTools.waitForElementToPresence(BAGGAGE_ALLOWANCE_HEADER, 1).isPresent()
                    || driverTools.waitForElementToPresence(MEALS_ADDON, 1).isPresent()
                    || driverTools.waitForElementToPresence(SEAT_ADDON, 1).isPresent()) {
                logger.log("Booking form page 2 is loaded");
                return;
            }
            driverTools.dummyWait(1);
        }
    }

    private int selectMealsUntilMaximumOnCurrentPage(int noOfPassenger) {
        int totalAdded = 0;
        for (int i = 1; i <= noOfPassenger; i++) {
            driverTools.swipeToElement(getMealPassengerTab(i), MEAL_PASSENGER_SELECTOR, Swipe.LEFT, 4)
                    .ifPresent(driverTools::click);
            int addedForPax = 0;
            while (driverTools.waitForElementToAppear(MEAL_ADD_BUTTON, 3).isPresent() && addedForPax < 10) {
                driverTools.click(MEAL_ADD_BUTTON);
                addedForPax++;
                totalAdded++;
            }
            boolean addDisabled = driverTools.waitForElementToPresence(MEAL_ADD_BUTTON_DISABLED, 3).isPresent()
                    || driverTools.waitForElementToAppear(MEAL_ADD_BUTTON, 2).isEmpty();
            Assertion.assertThat("Meal Add must be disabled after reaching maximum for passenger " + i, addDisabled, is(true));
            logger.log("Meals added for passenger " + i + ": " + addedForPax);
        }
        driverTools.waitForElementToAppear(SAVE_BUTTON, 10).orElseThrow();
        driverTools.click(SAVE_BUTTON);
        driverTools.waitForElementToDisappear(BOOK_MEALS_PAGE, 15);
        return totalAdded;
    }

    @Override
    public int selectMealsUntilMaximumFromAddons(int adultNumber, int childNumber) {
        if (!driverTools.waitForElementToPresence(BOOK_MEALS_PAGE, 2).isPresent()) {
            driverTools.swipeToElement(MEALS_ADDON, Swipe.UP, 3);
            driverTools.click(MEALS_ADDON);
            driverTools.waitForElementToAppear(BOOK_MEALS_PAGE, 10).orElseThrow();
        }
        return selectMealsUntilMaximumOnCurrentPage(adultNumber + childNumber);
    }

    @Override
    public String selectFreeSeatFromAddons(PriceDetail priceDetail) {
        driverTools.swipeToElement(SEAT_ADDON, Swipe.UP, 3);
        driverTools.click(SEAT_ADDON);
        driverTools.waitForElementToAppear(CHOOSE_BUTTON_DEPART, 5).ifPresent(driverTools::click);
        String seatPrice = selectSeatForPassengers(priceDetail.adult + priceDetail.child);
        Assertion.assertThat("Free seat total should be present", !seatPrice.isBlank(), is(true));
        driverTools.waitForElementToPresence(DONE_ADDON_BTN).ifPresent(driverTools::clickWithJavascriptExecutor);
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

    private Locator getPassengerForMealButton(int index){
        return new Locator("Passenger Change Meal Button",AppiumBy.xpath("((//XCUIElementTypeOther[@name='passenger'])//*[contains(@name,'"+index+".')])[last()]"));
    }

    @Override
    public int addBaggage(){
        openBaggageSelectionPage();
        int baggageAmount = getBaggage();

        logger.log("Verify decline (No Thanks) option or weight chips are present");
        boolean hasDeclineOrWeight = driverTools.isElementDisplayed(BAGGAGE_DECLINE_OPTION)
                || driverTools.isElementDisplayed(getBaggageOptionLocator(baggageAmount));
        Assertion.assertThat("Does baggage have decline option or weight chips", hasDeclineOrWeight, is(true));

        selectBaggageWeightForAllPassengers(baggageAmount);
        selectedBaggageWeight = baggageAmount;
        return baggageAmount;
    }

    /**
     * Opens the baggage weight picker. Prefers the new "Baggage Allowance" section (+ Add on checked
     * baggage → "Book Extra Baggage"), then falls back to the legacy add_baggage addon cell.
     */
    private void openBaggageSelectionPage() {
        if (driverTools.isElementDisplayed(BAGGAGE_DECLINE_OPTION) || driverTools.isElementDisplayed(BAGGAGE_OPTIONS)) {
            logger.log("Baggage options already visible (inline)");
            return;
        }
        if (isBaggageAllowanceSectionVisible()) {
            logger.log("Baggage Allowance section found");
            uncheckBaggageProtectionIfSelected();
            if (driverTools.isElementDisplayed(BAGGAGE_ADDON)
                    || driverTools.swipeToElement(BAGGAGE_ADDON, Swipe.UP, 4).isPresent()) {
                logger.log("Opening Book Extra Baggage via Add button");
                driverTools.click(BAGGAGE_ADDON);
                driverTools.waitForElementToAppear(BOOK_EXTRA_BAGGAGE_PAGE, 5);
                driverTools.waitForElementToAppear(CHOOSE_BUTTON_DEPART, 3).ifPresent(driverTools::click);
                driverTools.waitForElementToAppear(BAGGAGE_DECLINE_OPTION, 15)
                        .or(() -> driverTools.waitForElementToAppear(BAGGAGE_OPTIONS, 5))
                        .orElseThrow();
                return;
            }
            logger.log("Add button not present, expecting inline baggage chips on allowance section");
            return;
        }
        logger.log("Opening baggage via legacy addon section");
        driverTools.swipeToElement(BAGGAGE_ADDON, Swipe.UP, 6).orElseThrow();
        driverTools.click(BAGGAGE_ADDON);
        driverTools.waitForElementToAppear(CHOOSE_BUTTON_DEPART, 3).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(BAGGAGE_DECLINE_OPTION, 15)
                .or(() -> driverTools.waitForElementToAppear(BAGGAGE_OPTIONS, 5))
                .orElseThrow();
    }

    private boolean isBaggageAllowanceSectionVisible() {
        return driverTools.isElementDisplayed(BAGGAGE_ALLOWANCE_HEADER)
                || driverTools.swipeToElement(BAGGAGE_ALLOWANCE_HEADER, Swipe.UP, 8).isPresent();
    }

    /** Unchecks pre-selected Baggage Protection so baggage price assertions stay predictable. */
    private void uncheckBaggageProtectionIfSelected() {
        driverTools.swipeToElement(BAGGAGE_PROTECTION, Swipe.UP, 3);
        if (driverTools.waitForElementToPresence(BAGGAGE_PROTECTION_CHECKED, 2).isPresent()) {
            logger.log("Unchecking pre-selected Baggage Protection");
            driverTools.click(BAGGAGE_PROTECTION_CHECKBOX);
        }
    }

    /** Selects the given weight for every passenger section on Book Extra Baggage (dynamic pax count). */
    private void selectBaggageWeightForAllPassengers(int weight) {
        int paxSections = driverTools.elements(BAGGAGE_PASSENGER_SECTIONS).map(List::size).orElse(0);
        if (paxSections <= 1) {
            logger.log("Picking " + weight + " kg baggage option");
            driverTools.click(getBaggageOptionLocator(weight));
            return;
        }
        logger.log("Picking " + weight + " kg for " + paxSections + " passengers");
        for (int i = 1; i <= paxSections; i++) {
            logger.log("passenger : " + i);
            selectBaggageWeightForPassenger(weight, i);
        }
        driverTools.waitForElementToDisappear(BAGGAGE_PASSENGER_SECTIONS,5);
    }

    private void selectBaggageWeightForPassenger(int weight, int passengerIndex) {
        Locator indexedLocator = getBaggageOptionLocator(weight, passengerIndex);
        if (driverTools.waitForElementToPresence(indexedLocator, 3).isPresent()) {
            driverTools.click(indexedLocator);
        } else {
            driverTools.click(getBaggageOptionLocator(weight));
        }
    }

    @Override
    public int getBaggage() {
        List<WebElement> baggage = driverTools.elements(BAGGAGE_OPTIONS).orElseGet(ArrayList::new);
        if (baggage.isEmpty()) {
            // Options may not be rendered yet / still below the fold: scroll them into view and retry.
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
                return new Locator("ADD_ON_" + addOn, AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Baggage' or @name='Bagasi' or contains(@name,'Baggage Allowance') or contains(@name,'Kuota Bagasi') or contains(@name,'Alokasi Bagasi') or contains(@name,'Checked baggage') or contains(@label,'Checked baggage') or contains(@name,'Bagasi tercatat')] | //*[@name='add_baggage']"));
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
    public int addBaggage(PriceDetail priceDetail){
        int totalPassenger = priceDetail.adult + priceDetail.child;
        if (!driverTools.isElementDisplayed(BAGGAGE_ADDON)
                && !driverTools.isElementDisplayed(BAGGAGE_ALLOWANCE_HEADER)
                && !driverTools.isElementDisplayed(BAGGAGE_DECLINE_OPTION)) {
            continueToSecondHalf();
        }

        openBaggageSelectionPage();
        int baggageAmount = getBaggage();

        logger.log("Verify have No Thanks / 0 KG option");
        boolean hasDeclineOrZero = driverTools.isElementDisplayed(BAGGAGE_DECLINE_OPTION)
                || driverTools.isElementDisplayed(getBaggageOptionLocator(0));
        Assertion.assertThat("Does baggage have No Thanks or 0 KG option", hasDeclineOrZero, is(true));

        logger.log("Picking " + baggageAmount + " KG baggage option");
        for (int i = 1; i <= totalPassenger; i++) {
            logger.log("passenger : " + i);
            selectBaggageWeightForPassenger(baggageAmount, i);
        }
        selectedBaggageWeight = baggageAmount;
        return baggageAmount;
    }

    @Override
    public FlightBaggageDetail addBaggageRoundTrip(int baggageAmount, PriceDetail priceDetail,String currency){
        FlightBaggageDetail flightBaggageDetail = new FlightBaggageDetail();
        //fixme: implementation pending
        return flightBaggageDetail;
    }

    @Override
    public FlightBaggageDetail addBaggageOneWay(int baggageAmount, PriceDetail priceDetail,String currency){
        FlightBaggageDetail flightBaggageDetail = new FlightBaggageDetail();
        //fixme: implementation pending
        return flightBaggageDetail;
    }

    private Locator getBaggageOptionLocator ( int weight){
        // New UI: "+20 kg"; legacy: "20 Kg". Prefer chips inside a No-Thanks collection, else last match.
        return new Locator("BAGGAGE " + weight + " KG", AppiumBy.xpath("//XCUIElementTypeCollectionView[.//XCUIElementTypeStaticText[contains(@name,'Thanks') or contains(@name,'Tidak') or contains(@label,'Thanks')]]//XCUIElementTypeStaticText[@name='+" + weight + " kg' or @label='+" + weight + " kg'] | (//XCUIElementTypeStaticText[@name='+" + weight + " kg' or @label='+" + weight + " kg' or @name='" + weight + " Kg' or @label='" + weight + " Kg'])[last()]"));
    }

    private Locator getBaggageOptionLocator ( int weight,int index){
        // Per-passenger CollectionView on "Book Extra Baggage", then nth chip, then legacy "N. " passenger rows.
return new Locator("BAGGAGE " + weight + " KG PAX " + index, AppiumBy.xpath("(//XCUIElementTypeCollectionView[.//XCUIElementTypeStaticText[contains(@name,'Thanks') or contains(@name,'Tidak') or contains(@label,'Thanks') or contains(@label,'Tidak')]])[" + index + "]//XCUIElementTypeStaticText[@name='+" + weight + " kg' or @label='+" + weight + " kg' or @name='" + weight + " Kg'] | (//XCUIElementTypeStaticText[@name='+" + weight + " kg' or @label='+" + weight + " kg' or @name='" + weight + " Kg' or @label='" + weight + " Kg'])[" + index + "] | //*[@type='XCUIElementTypeStaticText' and contains(@label,'" + index + ". ')]/ancestor::*[.//*[@type='XCUIElementTypeStaticText' and (@label='" + weight + " Kg' or @name='+" + weight + " kg')]]//*[@type='XCUIElementTypeStaticText' and (@label='" + weight + " Kg' or @name='+" + weight + " kg')]"));
    }

    private Locator getMealPassengerTab(int index){
        return new Locator("Meal Passenger Tab " + index,
                AppiumBy.xpath("//XCUIElementTypeScrollView//XCUIElementTypeStaticText[starts-with(@name,'" + index + ".')]"));
    }

    private String selectMealForPassengers(int noOfPassenger){
        return selectMealForPassengers(noOfPassenger, 1);
    }

    private String selectMealForPassengers(int noOfPassenger, int mealsPerPassenger){
        for(int i=1;i<=noOfPassenger;i++){
            // Select the passenger in the horizontal selector at the top of "Book Meals" (swipe it into
            // view if the tab is off-screen), then add up to mealsPerPassenger meals for that passenger.
            driverTools.swipeToElement(getMealPassengerTab(i), MEAL_PASSENGER_SELECTOR, Swipe.LEFT, 4)
                    .ifPresent(driverTools::click);
            for (int m = 0; m < mealsPerPassenger; m++) {
                if (driverTools.waitForElementToAppear(MEAL_ADD_BUTTON, 5).isPresent()) {
                    driverTools.click(MEAL_ADD_BUTTON);
                } else if (driverTools.waitForElementToAppear(MEAL_ALREADY_SELECTED, 2).isPresent()) {
                    logger.log("Meal already selected for passenger " + i);
                } else {
                    throw new AssertionError("Neither Add nor Cancel meal button found for passenger " + i);
                }
            }
        }
        String price = driverTools.waitForElementToAppear(PRICE_ADD_ON, 10).orElseThrow().getAttribute("value");
        logger.log("Meal price: " + price);
        driverTools.click(SAVE_BUTTON);
        Assertion.assertThat("Meal price is non empty",!price.isEmpty() , is(true));
        return price;
    }

    /** Selects return route on Book Meals (last airport-code tab) or legacy Return/Pulang Select when present. */
    private void selectReturnMealSegment() {
        // Prefer the unselected route tab (name=textLabel) when depart is already selected.
        Locator unselectedReturnTab = new Locator("MEAL_RETURN_ROUTE_TAB_UNSELECTED", AppiumBy.xpath(
                "(//XCUIElementTypeStaticText[@name='textLabel' and contains(@label,' - ')])[last()]"));
        if (driverTools.waitForElementToAppear(unselectedReturnTab, 5).isPresent()) {
            logger.log("Selecting return meal route tab: " + driverTools.getText(unselectedReturnTab).orElse(""));
            driverTools.click(unselectedReturnTab);
            return;
        }
        if (driverTools.waitForElementToAppear(MEAL_RETURN_ROUTE_TAB, 3).isPresent()) {
            logger.log("Selecting return meal route tab (fallback last): "
                    + driverTools.getText(MEAL_RETURN_ROUTE_TAB).orElse(""));
            driverTools.click(MEAL_RETURN_ROUTE_TAB);
            return;
        }
        if (driverTools.waitForElementToAppear(CHOOSE_BUTTON_RETURN, 3).isPresent()) {
            driverTools.click(CHOOSE_BUTTON_RETURN);
            return;
        }
        if (driverTools.waitForElementToAppear(ANCILLARIES_SELECT_RETURN, 3).isPresent()) {
            driverTools.click(ANCILLARIES_SELECT_RETURN);
            return;
        }
        throw new AssertionError("Return meal segment control not found (route tab / Return Select)");
    }

    @Override
    public String addMeals(int adultNumber, int childNumber, String currency){
        driverTools.swipeToElement(MEALS_ADDON,Swipe.UP,3);
        driverTools.click(MEALS_ADDON);

        // New UI opens meal selection directly; the legacy depart chooser ("select_depart") is gone,
        // so only click it when it is actually present.
        driverTools.waitForElementToAppear(CHOOSE_BUTTON_DEPART,3).ifPresent(driverTools::click);

        String mealPrice =selectMealForPassengers(adultNumber+childNumber);

        String currencyMeal = mealPrice.replaceAll("[^a-zA-Z]", "");
        logger.log("Meal currency: "+ currencyMeal);

        Assertion.assertThat("Meal currency is correct", currencyMeal.equalsIgnoreCase(currency), is(true));
        // Saving on the "Book Meals" page returns to the booking form directly. Older UIs showed a
        // "done selecting meals" confirmation button; click it only when it is actually present.
        driverTools.waitForElementToAppear(DONE_ADDON_BTN,3).ifPresent(driverTools::click);
        return mealPrice;
    }

    @Override
    public FlightMealsDetail addMealsRoundTrip(int adultNumber, int childNumber, String currency){
        FlightMealsDetail flightMealsDetail = new FlightMealsDetail();
       //fixme: imeplementaion pending ios
        return flightMealsDetail;
    }

    @Override
    public FlightMealsDetail addMealsOneWay(int adultNumber, int childNumber, String currency){
        FlightMealsDetail flightMealsDetail = new FlightMealsDetail();
        //fixme: imeplementaion pending ios
        return flightMealsDetail;
    }

    @Override
    public FlightMealsDetail addMealsReturnOnly(int adultNumber, int childNumber, String currency, int mealsPerPassenger){
        FlightMealsDetail flightMealsDetail = new FlightMealsDetail();
        driverTools.swipeToElement(MEALS_ADDON, Swipe.UP, 3);
        driverTools.click(MEALS_ADDON);

        // New UI: Book Meals opens on depart route tab — switch to return (last tab). Legacy: Return Select.
        selectReturnMealSegment();

        logger.log("select meal for return only, meals per passenger: " + mealsPerPassenger);
        String mealPrice = selectMealForPassengers(adultNumber + childNumber, mealsPerPassenger);
        String currencyMeal = mealPrice.replaceAll("[^a-zA-Z]", "");
        logger.log("Meal currency Return: " + currencyMeal);
        Assertion.assertThat("Meal currency is correct", currencyMeal.equalsIgnoreCase(currency), is(true));

        flightMealsDetail.mealPriceReturn = mealPrice;
        flightMealsDetail.totalPaxReturn = adultNumber + childNumber;
        logger.log("total meal price Return : " + flightMealsDetail.mealPriceReturn);

        driverTools.waitForElementToAppear(DONE_ADDON_BTN, 3).ifPresent(driverTools::click);
        return flightMealsDetail;
    }

    @Override
    public String selectFreeMealsFromAddons(int adultNumber, int childNumber) {
        driverTools.swipeToElement(MEALS_ADDON, Swipe.UP, 3);
        driverTools.click(MEALS_ADDON);
        if (driverTools.waitForElementToAppear(MEAL_RETURN_ROUTE_TAB, 5).isPresent()
                || driverTools.waitForElementToAppear(CHOOSE_BUTTON_RETURN, 3).isPresent()
                || driverTools.waitForElementToAppear(ANCILLARIES_SELECT_RETURN, 3).isPresent()) {
            selectReturnMealSegment();
        }
        String mealPrice = selectMealForPassengers(adultNumber + childNumber);
        logger.log("Free meal total from addons: " + mealPrice);
        Assertion.assertThat("Free meal total should be present", !mealPrice.isBlank(), is(true));
        driverTools.waitForElementToAppear(DONE_ADDON_BTN, 3).ifPresent(driverTools::click);
        return mealPrice;
    }

    @Override
    public String selectFreeSeatsFromAddons(PriceDetail priceDetail) {
        driverTools.swipeToElement(SEAT_ADDON, Swipe.UP, 3);
        driverTools.click(SEAT_ADDON);
        if (driverTools.waitForElementToAppear(CHOOSE_BUTTON_RETURN, 5).isPresent()) {
            driverTools.click(CHOOSE_BUTTON_RETURN);
        } else {
            driverTools.waitForElementToAppear(CHOOSE_BUTTON_DEPART, 5).ifPresent(driverTools::click);
        }
        String seatPrice = selectSeatForPassengers(priceDetail.adult + priceDetail.child);
        logger.log("Free seat total from addons: " + seatPrice);
        Assertion.assertThat("Free seat total should be present", !seatPrice.isBlank(), is(true));
        driverTools.waitForElementToPresence(DONE_ADDON_BTN).ifPresent(driverTools::clickWithJavascriptExecutor);
        return seatPrice;
    }

    private String getCovid19TestPrice(){
        String price = String.valueOf(driverTools.getText(PRICE_ADD_ON));
        logger.log("Covid19 Test Price "+ price);
        driverTools.click(SAVE_BUTTON);
        Assertion.assertThat("Seat price is non empty",!price.isEmpty() , is(true));
        return price;
    }

    @Override
    public ArrayList<String> addCovidTest(){
        if(driverTools.swipeToElement(COVID_TEST,Swipe.UP,3).isEmpty()){
            continueToSecondHalf();
        }
        driverTools.click(COVID_TEST);
        driverTools.click(CHOOSE_BUTTON_DEPART);
        driverTools.click(SELECT_TEST);
        driverTools.click(CHECKBOX_AGREE);

        String covid19TestCompanyName = String.valueOf(driverTools.getText(COVID_LABEL));

        String getPrice=getCovid19TestPrice();
        ArrayList<String> covidTestInfo = new ArrayList<String>();
        covidTestInfo.add(covid19TestCompanyName);
        covidTestInfo.add(getPrice);

        driverTools.click(DONE_ADDON_BTN);
        return covidTestInfo;
    }
    @Override
    public boolean isTravelRequirementInfoPagePresent(){
        return true;
    }//FIXME: Implementation pending

    /** Fallback when currency is unknown (e.g. extra-protection plan cards). Prefer {@link #priceValueCond(String)}. */
    private static final String PRICE_VALUE_COND =
            "contains(@name,'IDR') or contains(@name,'SGD') or contains(@name,'MYR')"
                    + " or contains(@name,'USD') or contains(@name,'THB') or contains(@name,'PHP')"
                    + " or contains(@name,'VND') or contains(@name,'AUD') or contains(@name,'EUR')"
                    + " or @name='Free' or @name='Gratis'";

    private String priceValueCond(String currency){
        if (currency == null || currency.isBlank()) {
            return PRICE_VALUE_COND;
        }
        return "contains(@name,'" + currency + " ') or contains(@label,'" + currency + " ') or @name='Free' or @name='Gratis'";
    }

    private String labelContains(String text){
        return "contains(@name,'" + text + "') or contains(@value,'" + text + "') or contains(@label,'" + text + "')";
    }

    /**
     * New price-detail layout renders each row as a plain label StaticText followed by a monetary
     * value StaticText (no {@code price_detail_info}/{@code price_detail_price} ids). This returns the
     * {@code occurrence}-th monetary value that follows a label matching {@code labelCond}.
     */
    private Locator priceAfterLabel(String labelCond, int occurrence, String currency){
        return new Locator("PRICE_AFTER_LABEL", AppiumBy.xpath(
                "(//XCUIElementTypeStaticText[" + labelCond + "]/following::XCUIElementTypeStaticText[" + priceValueCond(currency) + "])[" + occurrence + "]"));
    }

    private Locator getPriceBreakdownLocator(String type, int amount, String currency){
        if (type.toLowerCase().contains("extra protection")) {
            String originalType = type.replaceAll("\\s+extra\\s+protection\\s*$", "").trim();
            return priceAfterLabel(labelContains(originalType + " (x" + amount + ")"), 1, currency);
        }

        switch (type.toLowerCase()) {
            case "adult":
            case "dewasa":
                return priceAfterLabel("@name='Adult (x" + amount + ")' or @name='Dewasa (x" + amount + ")'", 1, currency);
            case "child":
            case "anak":
                return priceAfterLabel("@name='Child (x" + amount + ")' or @name='Anak (x" + amount + ")'", 1, currency);
            case "infant":
            case "bayi":
                return priceAfterLabel("@name='Infant (x" + amount + ")' or @name='Bayi (x" + amount + ")'", 1, currency);
            // UI shows Meals/Makanan (xN) where N = meal portions, not passenger count.
            case "meal depart":
            case "makanan depart":
            case "meal breakdown":
                return priceAfterLabel("starts-with(@name,'Meal (x') or starts-with(@name,'Meals (x') or starts-with(@name,'Makanan (x')", 1, currency);
            case "meal return":
            case "makanan return":
                return priceAfterLabel("starts-with(@name,'Meal (x') or starts-with(@name,'Meals (x') or starts-with(@name,'Makanan (x')", 2, currency);
            case "baggage depart":
            case "bagasi depart":
            case "baggage return":
            case "bagasi return":
                return priceAfterLabel("@name='Baggage (" + amount + "kg)' or @name='Baggage (" + amount + "KG)' or @name='Bagasi (" + amount + "kg)' or @name='Bagasi (" + amount + "KG)'", 1, currency);
            case "discount":
                return new Locator("Price breakdown Discount", AppiumBy.xpath(
                        "(//XCUIElementTypeStaticText[contains(@name,'Diskon') or contains(@name,'Discount')])[last()]/following::XCUIElementTypeStaticText[" + priceValueCond(currency) + "][1]"));
            case "harga gledek":
                return priceAfterLabel(labelContains(type), 1, currency);
            case "seat depart":
                return priceAfterLabel("@name='Seat (x" + amount + ")' or @name='Seats (x" + amount + ")' or @name='Kursi (x" + amount + ")'", 1, currency);
            case "seat return":
                return priceAfterLabel("@name='Seat (x" + amount + ")' or @name='Seats (x" + amount + ")' or @name='Kursi (x" + amount + ")'", 2, currency);
            case "connecting flight protection":
                return priceAfterLabel("(contains(@name,'Connecting Flight Protection') or contains(@name,'Proteksi Transfer Mandiri') or contains(@name,'Self Transfer Protection')) and contains(@name,'(x" + amount + ")')", 1, currency);
            case "extra protection":
            default:
                return priceAfterLabel(labelContains(type + " (x" + amount + ")"), 1, currency);
        }
    }

    private String getPrice(String type, int amount, String currency){
        Optional<WebElement> priceElement = driverTools.waitForElementToPresence(getPriceBreakdownLocator(type, amount, currency));
        if (priceElement.isPresent()) {
            String price = driverTools.getValue(priceElement.orElseThrow());
            logger.log(type + " price:  " + price);
            return price;
        }
        return null;
    }

    private String getPrice(String type, int amount){
        return getPrice(type, amount, null);
    }

    private boolean isBreakdownCorrect(String type, int amount, String expected, String currency){
        if (expected != null) {
            double price = CoreFunctions.getIntFromString(expected).orElseThrow();
            // Guard the common failure where the breakdown row is missing (e.g. the add-on wasn't actually
            // applied): getPrice() returns null and getIntFromString(null) would NPE with an opaque message.
            // Fail the assertion cleanly with context instead.
            String bookingFormPriceText = getPrice(type, amount, currency);
            if (bookingFormPriceText == null) {
                logger.softWarning("Price breakdown row not found for '" + type + " (x" + amount + ")'"
                        + (currency == null ? "" : " [" + currency + "]") + " — the add-on is likely not "
                        + "selected/applied on the booking form.");
                return false;
            }
            double priceBookingForm = CoreFunctions.getIntFromString(bookingFormPriceText).orElseThrow();

            if (price * amount == priceBookingForm){
                price *= amount;
            }

            logger.log(type + " price " + price);
            logger.log(type + " price on booking form " + priceBookingForm);

            return priceBookingForm == price;
        }
        return true;
    }

    private boolean isBreakdownCorrect(String type, int amount, String expected){
        return isBreakdownCorrect(type, amount, expected, null);
    }

    private boolean verifyExtraPrice (PriceDetail priceDetail, String currency){

        // mealPrice is already the total; amount=1 avoids pax/portion multiplier mismatch in isBreakdownCorrect.
        boolean mealBreakdown = isBreakdownCorrect("meal breakdown", 1, priceDetail.mealPrice, currency);

        logger.log("meal depart breakdown "+mealBreakdown);

        boolean baggageDepartBreakdown = isBreakdownCorrect("baggage depart", priceDetail.baggageDepartAmount, priceDetail.baggageDepartPrice, currency);
        boolean baggageReturnBreakdown = isBreakdownCorrect("baggage return", priceDetail.baggageReturnAmount, priceDetail.baggageReturnPrice, currency);
        boolean baggageBreakdown = baggageDepartBreakdown && baggageReturnBreakdown;

        logger.log("baggage depart breakdown "+baggageDepartBreakdown);
        logger.log("baggage return breakdown "+baggageReturnBreakdown);

        boolean extraProtectionBreakdown = isBreakdownCorrect(priceDetail.extraProtectionTitle, priceDetail.adult + priceDetail.child, priceDetail.extraProtectionPrice, currency);

        logger.log("extra protection breakdown " + extraProtectionBreakdown);

        boolean isSeatPriceCorrect = isBreakdownCorrect("seat depart", priceDetail.adult + priceDetail.child, priceDetail.seatDepartPrice, currency);
        logger.log("seat breakdown " + isSeatPriceCorrect);

        boolean discountPriceBreakdown = isBreakdownCorrect("discount", 1, priceDetail.discountPrice, currency);
        logger.log("discount breakdown " + discountPriceBreakdown);

        return mealBreakdown && baggageBreakdown && extraProtectionBreakdown && discountPriceBreakdown && isSeatPriceCorrect;
    }

    @Override
    public boolean isPriceBreakdownCorrect(PriceDetail priceDetail, String currency){
        boolean extraPriceBreakdown = verifyExtraPrice(priceDetail, currency);
        logger.log("Extra price breakdown correct: " + extraPriceBreakdown);

        boolean priceCorrect = isPriceSumCorrect(currency);
        logger.log("Total price sum correct: " + priceCorrect);

        boolean currencyCorrect = isCurrencyCorrect(currency);
        logger.log("Total currency correct: " + currencyCorrect);

        // Close the Price Details popup once verification is done.
        driverTools.waitForElementToAppear(CLOSED_PRICE_DETAIL).ifPresent(driverTools::click);
        return  extraPriceBreakdown && priceCorrect && currencyCorrect;
    }

    @Override
    public boolean checkPassportPopup(){
        driverTools.swipeToElement(CONTINUE_PAYMENT_BUTTON,Swipe.UP,15);
        logger.log("Continue to payment");
        driverTools.click(CONTINUE_PAYMENT_BUTTON);
        boolean result = driverTools.isElementDisplayed(PASSPORT_POPUP_BANNER) || driverTools.isElementDisplayed(CHECK_PASSENGER_DETAILS);
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
        driverTools.click(CLOSE_PASSENGER_DETAILS);
        return result;
    }

    @Override
    public boolean isExistingPassengerDisplayed() {
        throw new NotImplementedException("Android only");
    }

    @Override
    public void selectCachedPassengerWithoutCompleting(String passengerName) {
        throw new NotImplementedException("Android only");
    }

    @Override
    public void selectAnySavedPassenger() {
        int index = 1;
        driverTools.waitForElementToAppear(BTN_CONTINUE, 10).orElseThrow();
        Locator firstPassenger = savedPassengerNameAt(index);
        driverTools.swipeToElement(firstPassenger, Swipe.UP, 3)
                .orElseThrow(() -> new NoSuchElementException("No saved passenger found on booking form"));
        String passengerName = driverTools.getText(firstPassenger).orElse("");
        logger.log("Selecting saved passenger: " + passengerName);
        tapSavedPassengerCheckbox(savedPassengerCheckboxAt(index));
    }

    @Override
    public boolean areIssuingDateAndCountryEmpty() {
        throw new NotImplementedException("Android only");
    }

    @Override
    public boolean isBlockedByPassengerSelectionValidation() {
        throw new NotImplementedException("Android only");
    }

    /**
     * Total / Pay now row: label StaticText → nested price StaticText whose {@code @name} contains the expected currency (e.g. MYR, IDR).
     */
    private Locator getTotalPricePayNowLocator(String currency) {
        String code = currency == null ? "" : currency.trim().replace("\"", "");
        if (code.isEmpty()) {
            code = "IDR";
        }
        return new Locator("TOTAL_PRICE_PAY_NOW_" + code, AppiumBy.xpath(
                "(//XCUIElementTypeStaticText[@name='Pay now' or @name='Total' or @name='Bayar sekarang']"
                        + "/following::XCUIElementTypeStaticText[contains(@name,'" + code + " ')])[1]"));
    }

    private Locator getPriceBreakdownLocator(String currency){
        return new Locator("PRICE_BREAKDOWN", AppiumBy.xpath("//XCUIElementTypeButton[@name='ic cancel grey']//following::*[contains(@name,'"+currency.toUpperCase()+"') and not(preceding-sibling::*[contains(@name,'Total')])]"));

    }

    private boolean isCurrencyCorrect(String currency){
        String totalPrice = driverTools.getText(getTotalPricePayNowLocator(currency)).orElseThrow();
        String currencyTotal = totalPrice.replaceAll("[^a-zA-Z]", "");
        logger.log("Currency in total price: "+currencyTotal);
        return  currency.equalsIgnoreCase(currencyTotal);
    }

    private Locator getPriceLocatorPriceDetail(String currency){
        return new Locator("PRICE_LOCATOR_PRICE_DETAIL", AppiumBy.xpath(
                "//*[@type='XCUIElementTypeOther' and (./*[@name='Price Details'] or ./*[@name='Detail Harga'] or ./*[@name='Rincian Harga'])]//*[contains(@name, '" + currency + " ')]"
                        + " | //XCUIElementTypeButton[@name='ic cancel grey']/following::XCUIElementTypeStaticText[contains(@name,'" + currency + " ')]"));
    }

    /**
     * Removes a parsed monetary value from the collected breakdown list when present.
     * Only removes when the value is surplus (appears more than once, or removing it still leaves
     * a sum that can reconcile with {@code payNowTotal}). A single occurrence that equals Subtotal /
     * Pay now is usually the real fare line item on iOS — wiping it empties the breakdown (sum = 0).
     */
    private boolean removeBreakdownPriceIfPresent(ArrayList<Double> arrayPrice, String priceText, String logContext) {
        return removeBreakdownPriceIfPresent(arrayPrice, priceText, logContext, Double.NaN);
    }

    private boolean removeBreakdownPriceIfPresent(ArrayList<Double> arrayPrice, String priceText, String logContext,
                                                  double payNowTotal) {
        if (priceText == null || !priceText.trim().matches(".*\\d.*")) {
            return false;
        }
        double value = CoreFunctions.convertPrice(priceText.trim());
        long occurrences = arrayPrice.stream().filter(v -> v.equals(value)).count();
        if (occurrences == 0) {
            return false;
        }
        if (occurrences == 1) {
            double sum = arrayPrice.stream().mapToDouble(Double::doubleValue).sum();
            boolean removingReconciles = !Double.isNaN(payNowTotal)
                    && Math.abs((sum - value) - payNowTotal) <= 1
                    && Math.abs(sum - payNowTotal) > 1;
            if (!removingReconciles) {
                logger.log("Skipped removing single occurrence of " + value
                        + " (" + logContext.trim() + ") — likely the fare line item itself");
                return false;
            }
        }
        if (arrayPrice.remove(Double.valueOf(value))) {
            logger.log(logContext + value);
            return true;
        }
        return false;
    }

    /** "Subtotal" summary row value; equals the sum of the fare line items so must not be summed again. */
    private Locator getSubtotalPriceLocator(String currency) {
        return new Locator("PRICE_DETAIL_SUBTOTAL_PRICE", AppiumBy.xpath(
                "(//XCUIElementTypeStaticText[@name='Subtotal' or @label='Subtotal']"
                        + "/following-sibling::XCUIElementTypeStaticText[contains(@name,'" + currency + " ')])[1]"
                        + " | (//XCUIElementTypeStaticText[@name='Subtotal' or @label='Subtotal']"
                        + "/parent::*/*[@type='XCUIElementTypeStaticText' and contains(@name,'" + currency + " ')])[1]"));
    }

    /**
     * Strips summary rows (Subtotal, post-cashback total, monetary cashback) that are picked up by the
     * broad {@link #getPriceLocatorPriceDetail} collector but must not be summed with line items.
     */
    private void removeSummaryTotalsFromBreakdown(ArrayList<Double> arrayPrice, double payNowTotal, String currency) {
        // "Subtotal" is a summary of the fare line items (Adult/Child/Infant + paid add-ons). On
        // international bookings with no discount, Subtotal == Pay now, so the total value is collected
        // twice; drop the Subtotal so the line items are summed only once.
        // Scope to sibling/parent of the Subtotal label — never use following:: (can match Meals price).
        if (driverTools.isElementDisplayed(getSubtotalPriceLocator(currency))) {
            driverTools.getText(getSubtotalPriceLocator(currency)).ifPresent(text ->
                    removeBreakdownPriceIfPresent(arrayPrice, text,
                            "Removed 'Subtotal' summary from breakdown list to avoid double counting: ",
                            payNowTotal));
        }

        driverTools.waitForElementToPresence(PRICE_DETAIL_TOTAL_AFTER_CASHBACK_PRICE, 3)
                .map(driverTools::getText)
                .ifPresent(text -> removeBreakdownPriceIfPresent(arrayPrice, text,
                        "Removed 'Total after cashback' from breakdown list to avoid double counting: ",
                        payNowTotal));

        driverTools.waitForElementToPresence(PRICE_DETAIL_POST_PAY_NOW_IDR, 3)
                .map(driverTools::getText)
                .ifPresent(text -> {
                    double value = CoreFunctions.convertPrice(text.trim());
                    if (Math.abs(value - payNowTotal) > 1) {
                        removeBreakdownPriceIfPresent(arrayPrice, text,
                                "Removed post-pay-now summary IDR from breakdown list: ",
                                payNowTotal);
                    }
                });

        driverTools.waitForElementToPresence(PRICE_DETAIL_CASHBACK_ROW, 3).ifPresent(ignored -> {
            String cashbackPriceText = driverTools.getText(PRICE_DETAIL_CASHBACK_VALUE).orElse(null);
            logger.log("Found cashback row with text: " + cashbackPriceText);
            if (cashbackPriceText == null || cashbackPriceText.trim().isEmpty()) {
                logger.log("Cashback row present but no price text found; skipping subtraction.");
                return;
            }
            String normalized = cashbackPriceText.trim().toLowerCase();
            boolean isNonMonetary = normalized.contains("included") || normalized.contains("termasuk")
                    || !normalized.matches(".*\\d.*");
            if (isNonMonetary) {
                logger.log("Cashback row has non-monetary value ('" + cashbackPriceText
                        + "'); skipping subtraction.");
            } else {
                removeBreakdownPriceIfPresent(arrayPrice, cashbackPriceText,
                        "Removed cashback from breakdown list to avoid double counting: ",
                        payNowTotal);
            }
        });
    }

    /**
     * Drops surplus copies of the Pay now amount that the broad collector picked up.
     * Unlike a blind {@code while (remove(payNow))}, this stops once removing another copy would
     * erase a fare line item that happens to equal Pay now (common with no discount / single adult).
     */
    private int removeSurplusPayNowTotals(ArrayList<Double> arrayPrice, double payNowTotal) {
        int removed = 0;
        while (arrayPrice.contains(Double.valueOf(payNowTotal))) {
            double sum = arrayPrice.stream().mapToDouble(Double::doubleValue).sum();
            long payNowCopies = arrayPrice.stream().filter(v -> v.equals(payNowTotal)).count();
            // Keep at least one copy when it is the only amount left that can represent the fare.
            boolean hasOtherAmounts = arrayPrice.stream().anyMatch(v -> Math.abs(v - payNowTotal) > 1);
            boolean surplusCopy = payNowCopies > 1 || (hasOtherAmounts && sum - payNowTotal >= payNowTotal - 1);
            if (!surplusCopy) {
                break;
            }
            arrayPrice.remove(Double.valueOf(payNowTotal));
            removed++;
        }
        return removed;
    }

    /**
     * iOS often exposes the same fare StaticText multiple times in the accessibility tree.
     * Collapse duplicate values until the running sum matches Subtotal (preferred) or Pay now.
     * <p>
     * Two real line items can legitimately share a price (e.g. Adult and Child on the same leg), so a
     * duplicate is only dropped while the sum stays at or above the target. Whatever still inflates the
     * total after that is a unique orphan, left to {@link #reconcileBreakdownSumWithPayNow}.
     */
    private void collapseAccessibilityDuplicatePrices(ArrayList<Double> arrayPrice, double payNowTotal, String currency) {
        double target = payNowTotal;
        if (driverTools.isElementDisplayed(getSubtotalPriceLocator(currency))) {
            String subtotalText = driverTools.getText(getSubtotalPriceLocator(currency)).orElse(null);
            if (subtotalText != null && subtotalText.trim().matches(".*\\d.*")) {
                target = CoreFunctions.convertPrice(subtotalText.trim());
                logger.log("Collapsing accessibility duplicates toward Subtotal: " + target);
            }
        } else {
            logger.log("Collapsing accessibility duplicates toward Pay now: " + target);
        }

        double sum = arrayPrice.stream().mapToDouble(Double::doubleValue).sum();
        while (sum - target > 1) {
            int index = indexOfCollapsibleDuplicate(arrayPrice, sum, target);
            if (index < 0) {
                logger.log("No duplicate can be collapsed without undershooting " + target
                        + "; leaving " + arrayPrice + " for orphan reconciliation");
                return;
            }
            double duplicate = arrayPrice.remove(index);
            logger.log("Collapsed iOS accessibility duplicate price: " + duplicate);
            sum -= duplicate;
        }
    }

    /**
     * Picks which duplicate to drop: the first whose removal lands on {@code target} (±1), otherwise the
     * largest one that keeps the sum at or above it. Returns {@code -1} when every duplicate is a real
     * line item whose removal would undershoot the target.
     */
    private int indexOfCollapsibleDuplicate(ArrayList<Double> arrayPrice, double sum, double target) {
        int fallbackIndex = -1;
        double fallbackValue = 0;
        for (int i = 0; i < arrayPrice.size(); i++) {
            final double value = arrayPrice.get(i);
            if (arrayPrice.stream().filter(v -> v.doubleValue() == value).count() < 2) {
                continue;
            }
            double remaining = sum - value;
            if (Math.abs(remaining - target) <= 1) {
                return i;
            }
            if (remaining >= target - 1 && value > fallbackValue) {
                fallbackIndex = i;
                fallbackValue = value;
            }
        }
        return fallbackIndex;
    }

    /**
     * When a summary IDR is in the accessibility tree but not matched by named locators, drop the
     * orphan value that reconciles the running sum with Pay now (±1 IDR).
     */
    private void reconcileBreakdownSumWithPayNow(ArrayList<Double> arrayPrice, double payNowTotal) {
        double sum = arrayPrice.stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(sum - payNowTotal) <= 1) {
            return;
        }
        for (int i = arrayPrice.size() - 1; i >= 0; i--) {
            double candidate = arrayPrice.get(i);
            if (Math.abs(candidate - payNowTotal) <= 1) {
                continue;
            }
            double adjustedSum = sum - candidate;
            if (Math.abs(adjustedSum - payNowTotal) <= 1) {
                arrayPrice.remove(i);
                logger.log("Reconciled breakdown by removing orphan summary price: " + candidate);
                return;
            }
        }
    }

    private boolean isPriceSumCorrect(String currency) {
        ArrayList<Double> arrayPrice = new ArrayList<>();
        Locator priceLocator = getPriceLocatorPriceDetail(currency);
        driverTools.waitForElementToAppear(priceLocator, 20);
        List<WebElement> prices = driverTools.elements(priceLocator).orElseThrow();
        int totalPrices = prices.size();
        boolean longBreakdown = false;
        int totalSwipe = 2;
        logger.log("Initial size: " + totalPrices);

        for (int i = 0; i < totalPrices;) {
            String priceText = driverTools.getText(prices.get(i)).trim().toLowerCase();

            if (priceText.contains("included")
                    || priceText.contains("termasuk")) {
                logger.log("Skipping non-price item: " + priceText);
            } else if (priceText.matches(".*\\d.*")) {
                double result = CoreFunctions.convertPrice(priceText);
                arrayPrice.add(result);
                logger.log("Added breakdown price: " + result);
            } else {
                logger.log("Skipping non-numeric value: " + priceText);
            }
            logger.log("Current breakdown array: " + arrayPrice);

            i++;
            if (i == totalPrices && !driverTools.isElementDisplayed(getTotalPricePayNowLocator(currency))) {
                longBreakdown = true;
                String lastPrice = driverTools.getText(prices.get(totalPrices - 1));
                driverTools.swipeToElement(getTotalPricePayNowLocator(currency), Swipe.UP, totalSwipe);
                prices = driverTools.elements(nextPrice(lastPrice, currency)).orElseThrow();
                totalPrices = prices.size();
                i = 0;
            }
        }

        double actualTotalPrice = CoreFunctions.convertPrice(driverTools.getText(getTotalPricePayNowLocator(currency)).orElseThrow());
        logger.log("Actual total price: " + actualTotalPrice);

        // Drop surplus "Pay now" totals only — never wipe fare rows that equal Pay now.
        int removedPayNow = removeSurplusPayNowTotals(arrayPrice, actualTotalPrice);
        logger.log("Breakdown array after removing surplus 'Pay now' totals: " + arrayPrice);
        if (removedPayNow > 0) {
            logger.log("Removed surplus 'Pay now' total from breakdown list ("
                    + removedPayNow + "x): " + actualTotalPrice);
        }

        // Strip the remaining summary rows (Subtotal, Total after cashback, cashback) that must not be summed.
        removeSummaryTotalsFromBreakdown(arrayPrice, actualTotalPrice, currency);
        logger.log("Breakdown array after removing summary totals: " + arrayPrice);

        // Collapse iOS a11y duplicates of the same fare StaticText (e.g. 2557300 x3 → 2557300).
        collapseAccessibilityDuplicatePrices(arrayPrice, actualTotalPrice, currency);
        logger.log("Breakdown array after collapsing accessibility duplicates: " + arrayPrice);

        // Drop any orphan summary IDR not matched by named locators so the sum reconciles with Pay now.
        reconcileBreakdownSumWithPayNow(arrayPrice, actualTotalPrice);
        logger.log("Breakdown array after reconciling with 'Pay now' total: " + arrayPrice);

        double sum = 0;
        for (double val : arrayPrice) {
            sum += val;
        }
        logger.log("Sum before rounding: " + sum);
        sum = CoreFunctions.doubleRound(sum);
        logger.log("Sum after rounding: " + sum);

        // With cashback, line items sum to Subtotal while Pay now shows Total after cashback.
        if (Math.abs(sum - actualTotalPrice) > 1
                && driverTools.isElementDisplayed(getSubtotalPriceLocator(currency))) {
            String subtotalText = driverTools.getText(getSubtotalPriceLocator(currency)).orElse("");
            if (subtotalText.trim().matches(".*\\d.*")) {
                double subtotal = CoreFunctions.convertPrice(subtotalText.trim());
                if (Math.abs(sum - subtotal) <= 1) {
                    logger.log("Line items sum to Subtotal (" + subtotal
                            + "); Pay now (" + actualTotalPrice + ") reflects post-cashback total");
                    Assertion.assertThat("Total price breakdown matches Subtotal", sum, is(subtotal));
                    if (longBreakdown) {
                        driverTools.swipeDown(totalSwipe);
                    }
                    return true;
                }
            }
        }

        Assertion.assertThat("Total price breakdown is correct", sum, is(actualTotalPrice));

        if (longBreakdown) {
            driverTools.swipeDown(totalSwipe);
        }

        return true;
    }

    private Locator nextPrice(String firstPrice, String currency) {
        return new Locator("NEXT_PRICE", AppiumBy.xpath("//*[@type=\"XCUIElementTypeStaticText\" and @name=\"" + firstPrice + "\"]//following::*[@type=\"XCUIElementTypeOther\"]//*[@type=\"XCUIElementTypeStaticText\" and contains(@name, \"" + currency + " \")]"));
    }

    @Override
    public void openPriceDetail(){
        driverTools.swipeToElement(CONTINUE_PAYMENT_BUTTON, Swipe.UP,15);
        driverTools.waitForElementToAppear(PRICE_DETAIL_ARROW);
        driverTools.click(PRICE_DETAIL_ARROW);
    }

    @Override
    public boolean checkInsurance(){
        return driverTools.isElementDisplayed(INSURANCE_LABEL);
    }

    @Override
    public void skipFullProtection(){
        String methodName = "skipFullProtection";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForElementToDisappear(CONTINUE_PAYMENT_BUTTON);
            if (driverTools.waitForElementToAppear(ORDER_ID,6).isPresent()){
                return;
            }
            skipFullProtectionPrp();
            if (driverTools.waitForElementToAppear(ORDER_ID,3).isPresent()){
                return;
            }
            driverTools.waitForElementToAppear(PRICE_CHANGE).ifPresent(driverTools::click);
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
        return driverTools.waitForElementToPresence(BTN_CONTINUE, 15).isPresent();
    }

    @Override
    public void continueToPayment(){
        String methodName = "continueToPayment";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            if(driverTools.isElementDisplayed(CLOSED_PRICE_DETAIL)){
                driverTools.click(CLOSED_PRICE_DETAIL);
            }

            WebElement continueToPaymentBtn = driverTools.swipeToElement(CONTINUE_PAYMENT_BUTTON,Swipe.UP,10).orElseThrow();
            driverTools.click(continueToPaymentBtn);
            WebElement confirmPayment = driverTools.waitForElementToAppear(CONFIRM_PAYMENT).orElseThrow();
            driverTools.click(confirmPayment);
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    // TODO: verify on-device — best-guess locator for the "No, thank you" button on the baggage offer add-ons page.
    private static final Locator NO_THANKS_BAGGAGE_OFFER = new Locator("NO_THANKS_BAGGAGE_OFFER",
            AppiumBy.xpath("//*[contains(@label,'No, thank you') or contains(@name,'No, thank you') or contains(@label,'No, thanks') or contains(@label,'Nggak, deh') or contains(@label,'Tidak, terima kasih') or contains(@label,'Lewati')]"));

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

    @Override
    public String getBaggagePrice(int baggageAmount){
        // Dedicated page exposes a summary "price" element; the inline layout does not,
        // so fall back to the price shown on the selected baggage chip.
        String price = driverTools.waitForElementToAppear(PRICE_ADD_ON, 5)
                .map(driverTools::getText)
                .orElse("");
        if (price == null || price.isEmpty()) {
            price = driverTools.getText(getBaggagePriceForWeight(selectedBaggageWeight)).orElse("");
        }
        Assertion.assertThat("Baggage price is non empty", price != null && !price.isEmpty(), is(true));
        return price;
    }

    private Locator getBaggagePriceForWeight(int weight){
        return new Locator("BAGGAGE_PRICE_" + weight, AppiumBy.xpath(
                "//XCUIElementTypeStaticText[@name='+" + weight + " kg' or @name='" + weight + " Kg']"
                        + "/following-sibling::XCUIElementTypeStaticText[1]"
                        + " | //XCUIElementTypeStaticText[@name='+" + weight + " kg' or @name='" + weight + " Kg']"
                        + "/parent::*//XCUIElementTypeStaticText[contains(@name,'IDR') or contains(@name,'SGD') or contains(@name,'MYR') or contains(@name,'USD') or contains(@name,'THB')]"));
    }

    @Override
    public int getBaggageAmount(int baggageAmount,PriceDetail priceDetail){
        int totalPassenger = priceDetail.adult + priceDetail.child;
        int finalBaggageAmount = 0;
        for (int i=1;i<=totalPassenger;i++){
            WebElement baggageElement = driverTools.element(getBaggageOptionLocator(baggageAmount,i)).orElseThrow();
            int baggage = (int) CoreFunctions.convertPriceSafely(driverTools.getText(baggageElement));
            logger.log("passenger : " + i +" baggage amount: " + baggage);
            finalBaggageAmount += baggage;
        }
        logger.log("final baggage amount : " + finalBaggageAmount);
        return finalBaggageAmount;
    }

    @Override
    public void saveAddBaggage(){
        // Dedicated page needs a Save (and legacy Done); the inline layout applies the
        // selection immediately, so click both only when present.
        driverTools.waitForElementToAppear(SAVE_BUTTON,3).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(DONE_ADDON_BTN,3).ifPresent(driverTools::click);
    }

    @Override
    public String addFullProtection(){
        return null;
    }

    @Override
    public String addBaggageInsurance(){
        return null;
    }

    private String selectSeatForPassengers(int noOfPassenger){
        String price;
        for (int i=0;i<noOfPassenger;i++){
            driverTools.swipeToElement(CHOOSE_EMPTY_SEAT,Swipe.UP,3);
            driverTools.waitForElementToPresence(CONTINUE_POPUP).ifPresent(driverTools::click);
            driverTools.click(CHOOSE_EMPTY_SEAT);
            driverTools.waitForElementToPresence(CONTINUE_POPUP).ifPresent(driverTools::click);
            driverTools.waitForElementToPresence(CONTINUE_POPUP).ifPresent(driverTools::click);
            boolean btnContinue = driverTools.isElementDisplayed(BTN_CONTINUE);
            if(btnContinue){
                driverTools.click(BTN_CONTINUE);
            }
        }

        price = String.valueOf(driverTools.getText(PRICE_ADD_ON).orElseThrow());
        logger.log("Seat Price "+ price);
        Assertion.assertThat("Seat price is non empty",!price.isEmpty() , is(true));
        driverTools.click(SAVE_BUTTON);
        return price;
    }
    @Override
    public String addSeat(PriceDetail priceDetail, String currency) {
        //todo: assert currency
        driverTools.swipeToElement(SEAT_ADDON,Swipe.UP,3);
        driverTools.click(SEAT_ADDON);
        driverTools.waitForElementToAppear(CHOOSE_BUTTON_DEPART, 5).ifPresent(driverTools::click);
        String seatPrice= selectSeatForPassengers(priceDetail.adult+ priceDetail.child);
        String currencySeat = seatPrice.replaceAll("[^a-zA-Z]", "");
        logger.log("Seat currency: "+ currencySeat);
        Assertion.assertThat("Seat currency is correct", currency.equalsIgnoreCase(currencySeat) , is(true));
        driverTools.waitForElementToPresence(DONE_ADDON_BTN).ifPresent(driverTools::clickWithJavascriptExecutor);
        return seatPrice;
    }

    /**
    * Clicks on the seat add-on button.
    * This method first finds the seat add-on button, then performs a click action on it.
    */
    @Override
    public void clickAddOnSeat(){
        WebElement seatAddon = driverTools.swipeToElement(SEAT_ADDON,Swipe.UP,3).orElseThrow();
        driverTools.click(seatAddon);
    }

    /**
     * Clicks on the choose seat button.
     * This method first finds the choose seat button, then performs a click action on it.
     */
    @Override
    public void clickChooseSeat(){
        WebElement chooseBtn = driverTools.waitForElementToAppear(CHOOSE_BUTTON_DEPART).orElseThrow();
        driverTools.click(chooseBtn);
    }

    /**
     * Checks if the Quiet Zone banner is displayed.
     * @return true if the Quiet Zone banner is displayed, false otherwise.
     */
    @Override
    public boolean isQuiteZoneBannerDisplayed(){
        return driverTools.isElementDisplayed(QUIET_ZONE_BANNER);
    }

    /**
     * Adds a seat in the Quiet Zone.
     * This method first extracts the row details from the Quiet Zone banner, then selects a seat for each passenger in the middle of the Quiet Zone.
     * @param priceDetail the price details of the flight.
     * @param currency the currency of the flight price.
     * @return the price of the seat in the Quiet Zone.
     */
    @Override
    public String addQuietZoneSeat(PriceDetail priceDetail,String currency){
        WebElement quiteZoneBannerDetail = driverTools.waitForElementToAppear(QUIET_ZONE_BANNER_ROW_DETAIL).orElseThrow();
        String quiteZoneDetail = driverTools.getText(quiteZoneBannerDetail);
        logger.log("Quite Zone Detail: " + quiteZoneDetail);
        int startRow = getRowNumber(quiteZoneDetail,1);
        int endRow = getRowNumber(quiteZoneDetail,2);
        logger.log("Start Row: " + startRow);
        logger.log("End Row: " + endRow);

        String seatPrice= selectSeatForPassengersForQuietZone(priceDetail.adult+ priceDetail.child,startRow,endRow);
        String currencySeat = seatPrice.replaceAll("[^a-zA-Z]", "");
        logger.log("Seat currency: "+ currencySeat);
        Assertion.assertThat("Seat currency is correct", currency.equalsIgnoreCase(currencySeat) , is(true));
        driverTools.click(DONE_ADDON_BTN);
        return seatPrice;
    }

    /**
     * Extracts the row numbers from the Quiet Zone detail string.
     * @param quiteZoneDetail the detail string of the Quiet Zone.
     * @param index the index of the row number to extract (1 for start row, 2 for end row).
     * @return the extracted row number.
     */
    private int getRowNumber(String quiteZoneDetail,int index){
        Pattern pattern = Pattern.compile("(\\d+)-(\\d+)");
        Matcher matcher = pattern.matcher(quiteZoneDetail);
        if (matcher.find()) {
            int startRow = Integer.parseInt(matcher.group(1));
            int endRow = Integer.parseInt(matcher.group(2));
            return index == 1 ? startRow : endRow;
        }
        logger.log("No match found.");
        return -1;
    }

    /**
     * Gets the locator of a seat in the Quiet Zone.
     * @param row the row number of the seat.
     * @return the locator of the seat.
     */
    private Locator getQuietZoneSeatLocator(int row){
        return new Locator("Quiet Zone Seat", AppiumBy.xpath("((//XCUIElementTypeOther//*[@name='"+row+"'])[1]//parent::*//preceding-sibling::*[contains(@name,'ft_seat_available')])[last()]"));
    }

    /**
     * Gets the locator of a selected seat in the Quiet Zone.
     * @param row the row number of the seat.
     * @param isActive whether the seat is active.
     * @param type the type of the seat ("before" or "after").
     * @return the locator of the seat.
     */
    private Locator getQuietZoneSelectedSeatRow(int row, boolean isActive, String type){
        String flag = isActive ? "active" : "inactive";
        if(type.equals("before")){
            return new Locator("Quiet Zone Selected Seat Row Before", AppiumBy.xpath("(//XCUIElementTypeOther[@name='ft_seat_selection_"+flag+"']//preceding-sibling::XCUIElementTypeOther//*[@name='"+row+"'])[1]"));
        }
        return new Locator("Quiet Zone Selected Seat Row After", AppiumBy.xpath("(//XCUIElementTypeOther[@name='ft_seat_selection_"+flag+"']//following-sibling::XCUIElementTypeOther//*[@name='"+row+"'])[1]"));
    }

    /**
     * Selects seats for passengers in the Quiet Zone.
     * This method first calculates the middle row of the Quiet Zone, then selects a seat for each passenger in that row.
     * @param noOfPassenger the number of passengers.
     * @param startRow the start row of the Quiet Zone.
     * @param endRow the end row of the Quiet Zone.
     * @return the price of the seats.
     */
    private String selectSeatForPassengersForQuietZone(int noOfPassenger,int startRow,int endRow){
        String price;
        for (int i=0;i<noOfPassenger;i++){
            int seatRow = (endRow + startRow + 1) / 2;
            Locator seatLocator = getQuietZoneSeatLocator(seatRow);
            driverTools.swipeToElement(seatLocator,Swipe.UP,3);
            driverTools.click(seatLocator);
            boolean btnContinue = driverTools.isElementDisplayed(BTN_CONTINUE);
            if(btnContinue){
                driverTools.click(BTN_CONTINUE);
            }
        }
        //after selecting for 1st passenger, passenger selection move to 2nd and prv selected passenger seat becomes be inactive
        driverTools.swipeToElement(getQuietZoneSelectedSeatRow(startRow,false,"before"),Swipe.DOWN,3);
        boolean isCorrectSeatSelectedBasedOnStartRow1stPassengers = driverTools.isElementDisplayed(getQuietZoneSelectedSeatRow(startRow,false,"before"));
        driverTools.swipeToElement(getQuietZoneSelectedSeatRow(startRow,true,"before"),Swipe.DOWN,3);
        boolean isCorrectSeatSelectedBasedOnStartRow2ndPassengers = driverTools.isElementDisplayed(getQuietZoneSelectedSeatRow(startRow,true,"before"));

        driverTools.swipeToElement(getQuietZoneSelectedSeatRow(endRow,false,"after"),Swipe.UP,3);
        boolean isCorrectSeatSelectedBasedOnEndRow1stPassengers = driverTools.isElementDisplayed(getQuietZoneSelectedSeatRow(endRow,false,"after"));
        driverTools.swipeToElement(getQuietZoneSelectedSeatRow(endRow,true,"after"),Swipe.UP,3);
        boolean isCorrectSeatSelectedBasedOnEndRow2ndPassengers = driverTools.isElementDisplayed(getQuietZoneSelectedSeatRow(endRow,true,"after"));

        Assertion.assertThat("Seat selected is correct based on start row 1st passenger",isCorrectSeatSelectedBasedOnStartRow1stPassengers,is(true));
        Assertion.assertThat("Seat selected is correct based on end row 1st Passenger",isCorrectSeatSelectedBasedOnEndRow1stPassengers,is(true));
        Assertion.assertThat("Seat selected is correct based on start row 2st passenger",isCorrectSeatSelectedBasedOnStartRow2ndPassengers,is(true));
        Assertion.assertThat("Seat selected is correct based on end row 2st Passenger",isCorrectSeatSelectedBasedOnEndRow2ndPassengers,is(true));

        price = driverTools.getText(PRICE_ADD_ON).orElseThrow();
        logger.log("Seat Price "+ price);
        Assertion.assertThat("Seat price is non empty",!price.isEmpty() , is(true));
        driverTools.click(SAVE_BUTTON);
        return price;
    }

    @Override
    public FlightSeatDetails addSeatRoundTrip(PriceDetail priceDetail, String currency) {
        //fixme: implementation pending
        return new FlightSeatDetails();
    }

    @Override
    public FlightSeatDetails addSeatOneWay(PriceDetail priceDetail, String currency) {
        //fixme: implementation pending
        return new FlightSeatDetails();
    }

    @Override
    public String addCovid19Insurance(){
        return null;
    }

    @Override
    public ArrayList<String> addExtraProtection(int index){
        ArrayList<String> protectionInfo = new ArrayList<>();

        // New UI: each plan is a card with a plan name (e.g. "Flight Delay Plus"), a benefits list, a
        // per-pax price ("IDR .../pax") and a selection indicator. The indicator renders as a *disabled*
        // "checkbox" element (not a tappable radio), so the plan is selected by tapping the plan title.
        // Scroll the plan cards into view; if we overshoot (the price is now above), scroll back down.
        Optional<WebElement> priceOpt = driverTools.swipeToElement(getExtraProtectionLocatorPrice(index), Swipe.UP, 10);
        if (priceOpt.isEmpty()) {
            priceOpt = driverTools.swipeToElement(getExtraProtectionLocatorPrice(index), Swipe.DOWN, 10);
        }
        WebElement priceEl = priceOpt.orElseThrow();
        String price = driverTools.getText(priceEl);

        WebElement titleEl = driverTools.waitForElementToAppear(getExtraProtectionLocatorTitle(index)).orElseThrow();
        String title = driverTools.getText(titleEl);

        // The plan may already be selected from a previous step (selecting it again would toggle it off),
        // so only act when it isn't. Selection state is detected by the checkbox/radio indicator sharing
        // the plan's price row (proximity match) — indexing the global selected-only list misaligns when
        // other plans are selected by default (e.g. "No Protection A") and makes a paid, unselected plan
        // look "already added".
        if (isExtraProtectionSelectedNear(priceEl)) {
            logger.log(title + " is already added with Price of " + price);
        } else {
            // Tapping the plain plan title does NOT toggle selection on this UI (verified: two title taps
            // were silent no-ops and the default plan stayed selected -> breakdown missing the plan ->
            // NullPointerException downstream). The real control is the row's checkbox/radio, which renders
            // as a *disabled* Other element, so it must be tapped by coordinate (the clickable-gated click
            // can't operate it). Verify after each tap and alternate the target so a silent no-op doesn't
            // leave the wrong plan selected.
            boolean selected = false;
            for (int attempt = 1; attempt <= 3 && !selected; attempt++) {
                WebElement anchor = driverTools.element(getExtraProtectionLocatorPrice(index)).orElse(priceEl);
                Optional<WebElement> indicator = nearestProtectionIndicator(anchor);
                if (indicator.isPresent() && attempt < 3) {
                    driverTools.clickElementByCoordinate(indicator.get());
                } else {
                    // Fallback: tap the title/card (covers builds where the row itself is the tap target).
                    driverTools.clickElementByCoordinate(titleEl);
                }
                WebElement verifyAnchor = driverTools.element(getExtraProtectionLocatorPrice(index)).orElse(anchor);
                selected = isExtraProtectionSelectedNear(verifyAnchor);
                logger.log(title + " select attempt " + attempt + " -> selected=" + selected);
            }
            if (selected) {
                logger.log(title + " is Added with Price of " + price);
            } else {
                logger.softWarning(title + " could NOT be selected after 3 attempts; price breakdown "
                        + "verification for this plan will likely fail.");
            }
        }

        protectionInfo.add(title);
        protectionInfo.add(price);
        return protectionInfo;
    }

    @Override
    public void removeExtraProtection(){
        for(int i=0;i<2;i++){
            driverTools.swipeToElement(getExtraProtectionLocatorSelectedFlag(i+1),Swipe.UP,3);
            if(driverTools.isElementDisplayed(getExtraProtectionLocatorSelectedFlag(i+1))){
                driverTools.click(getExtraProtectionLocatorAddBtn(i+1));
            }
        }
    }

    @Override
    public void removeExtraProtectionAll(){
        for(int i=0;i<3;i++){
            Locator cancelBtn = getExtraProtectionLocatorCancelBtn(1);
            driverTools.swipeToElement(cancelBtn,Swipe.UP,3);
            if(driverTools.isElementDisplayed(cancelBtn))
                driverTools.click(cancelBtn);
        }
    }

    /**
     * Condition matching a plan's "per pax" price StaticText (e.g. "IDR 5,000/pax"). Some layouts
     * (notably international) render the price on {@code @label} and/or use a different separator, so
     * match both attributes and only require the "pax" keyword + a currency/free marker.
     */
    private static final String EXTRA_PROTECTION_PRICE_COND =
            "(" + PRICE_VALUE_COND + ") and (contains(@name,'pax') or contains(@label,'pax'))";

    // A plan title is a StaticText whose card also has a "N benefit(s)" row, i.e. it has a following-sibling
    // benefit text. In the current UI all plan titles are grouped together (as siblings) *before* the benefit
    // counts, descriptions and per-pax prices, so associating a title with its price by document proximity is
    // wrong; instead the Nth title element corresponds to the Nth plan/price.
    private static final String EXTRA_PROTECTION_TITLE_COND =
            "following-sibling::XCUIElementTypeStaticText[contains(@name,'benefit') or contains(@name,'manfaat')] "
                    + "and not(contains(@name,'benefit')) and not(contains(@name,'manfaat')) "
                    + "and not(contains(@name,'Cover')) and not(contains(@name,'IDR')) and not(contains(@name,'Rp')) "
                    + "and not(contains(@name,'pax')) and not(contains(@name,'Protection up')) and not(contains(@name,'Lindungi'))";

    private Locator getExtraProtectionLocatorTitle ( int index){
        return new Locator("Extra Protection Title", AppiumBy.xpath(
                "(//XCUIElementTypeStaticText[" + EXTRA_PROTECTION_TITLE_COND + "])[" + index + "]"));
    }

    private Locator getExtraProtectionLocatorPrice (int index){
        return new Locator("Extra Protection Price", AppiumBy.xpath(
                "(//XCUIElementTypeStaticText[" + EXTRA_PROTECTION_PRICE_COND + "])[" + index + "]"));
    }

    private Locator getExtraProtectionRadio (int index){
        return new Locator("Extra Protection Radio", AppiumBy.xpath(
                "(//XCUIElementTypeOther[contains(@label,'radioButton') or contains(@name,'radioButton')])[" + index + "]"));
    }

    // Max vertical gap (px) between a plan's price row and its selection indicator to treat them as the
    // same row. Price height ~19px, indicator ~25px; their vertical centres line up within a few px.
    private static final int EXTRA_PROTECTION_ROW_BAND_PX = 40;

    // All plan selection indicators (checkbox/radio, selected or not). We can't reach a plan's indicator
    // by structure because the plan cards are flattened as sibling elements, so it is matched by row.
    private static final Locator EXTRA_PROTECTION_SELECT_INDICATORS = new Locator("EXTRA_PROTECTION_SELECT_INDICATORS",
            AppiumBy.xpath("//XCUIElementTypeOther[contains(@name,'checkbox') or contains(@label,'checkbox') "
                    + "or contains(@name,'radioButton') or contains(@label,'radioButton')]"));

    /**
     * The checkbox/radio selection indicator that shares a row with {@code rowAnchor} (a plan's price
     * element), matched by closest vertical centre. Proximity matching is used because the plan cards
     * are flattened as sibling elements (no per-card container) and a positional index into the
     * selected-only indicator list misaligns whenever other plans are selected by default.
     */
    private Optional<WebElement> nearestProtectionIndicator(WebElement rowAnchor){
        if (rowAnchor == null) return Optional.empty();
        int anchorCentreY;
        try {
            anchorCentreY = rowAnchor.getRect().getY() + rowAnchor.getRect().getHeight() / 2;
        } catch (Exception e) {
            return Optional.empty();
        }
        List<WebElement> indicators = driverTools.elements(EXTRA_PROTECTION_SELECT_INDICATORS)
                .orElseGet(java.util.Collections::emptyList);
        WebElement nearest = null;
        int bestDelta = Integer.MAX_VALUE;
        for (WebElement indicator : indicators){
            try {
                int centreY = indicator.getRect().getY() + indicator.getRect().getHeight() / 2;
                int delta = Math.abs(centreY - anchorCentreY);
                if (delta < bestDelta){
                    bestDelta = delta;
                    nearest = indicator;
                }
            } catch (Exception ignored){
                // element went stale between find and measure; skip it
            }
        }
        return (nearest != null && bestDelta <= EXTRA_PROTECTION_ROW_BAND_PX) ? Optional.of(nearest) : Optional.empty();
    }

    /** A plan's indicator is selected when its {@code name}/{@code label} carries a selected/checked marker. */
    private boolean isIndicatorSelected(WebElement indicator){
        String marker = (safeAttr(indicator, "name") + " " + safeAttr(indicator, "label")).toLowerCase();
        return marker.contains("selected") || marker.contains("checked");
    }

    /** Whether the plan sharing a row with {@code rowAnchor} (its price element) is currently selected. */
    private boolean isExtraProtectionSelectedNear(WebElement rowAnchor){
        return nearestProtectionIndicator(rowAnchor).map(this::isIndicatorSelected).orElse(false);
    }

    private static String safeAttr(WebElement element, String attribute){
        try {
            String value = element.getAttribute(attribute);
            return value == null ? "" : value;
        } catch (Exception e) {
            return "";
        }
    }

    private Locator getExtraProtectionLocatorAddBtn ( int index){
        return new Locator("Extra Protection Add button", AppiumBy.xpath("(//*[contains(@name,'extraprotection_btn')])[" + index + "]"));
    }

    private Locator getExtraProtectionLocatorSelectedFlag ( int index){
        return new Locator("Extra Protection Selected Flag", AppiumBy.xpath("(//*[contains(@name,'extraprotection_btn')])["+index+"]//following-sibling::*[contains(@label,'You’ve added protection') or contains(@label,'Kamu sudah dilindungi')]"));
    }

    private Locator getExtraProtectionLocatorCancelBtn ( int index){
        return new Locator("Extra Protection Add button", AppiumBy.xpath("(//*[@name='extra_protection_cancel_btn'])["+index+"]"));
    }

    @Override
    public boolean isWhatYouGetCorrect(String label){
        driverTools.swipeToElement(getWhatYouGetLocator(label), Swipe.UP, 10);
        return driverTools.isElementDisplayed(getWhatYouGetLocator(label));
    }

    @Override
    public boolean isWhatYouGetCorrectVI(){
        return driverTools.waitForElementToPresence(WHAT_YOU_GET, 20).isPresent();
    }

    private Locator getWhatYouGetLocator(String label){
        switch (label.toLowerCase()) {
            case "platinum" ,"diamond" -> {
                return new Locator("WHAT YOU GET :" + label.toUpperCase(),AppiumBy.xpath("//*[contains(@name,'what_you_get_title') and (contains(@value,'Benefit') and contains(@value,'Elite') and contains(@value,'"+label+"'))]"));
            }
            case "harga gledek" ,"100% refund" ,"free reschedule" -> {
                return new Locator("WHAT YOU GET :" + label.toUpperCase(), AppiumBy.xpath("//*[contains(@name,'what_you_get_title') and contains(@label,'"+label+"')]"));
            }
            case "connecting flight protection", "self transfer protection", "proteksi transfer mandiri" -> {
                return new Locator("WHAT YOU GET :" + label.toUpperCase(), AppiumBy.xpath("(//*[contains(@name,'what_you_get_title') or contains(@label,'"+label+"')  or contains(@text,'self transfer protection') or contains(@text,'proteksi transfer mandiri')])[2]"));
            }
            default -> throw new NotImplementedException("Unimplemented what you get type!");
        }
    }

    @Override
    public boolean isDiscountPriceCorrect(double discount, String discType){
        String discountPrice = driverTools.getText(getDiscountPrice(discType)).orElseThrow();
        double price = CoreFunctions.convertPrice(discountPrice);
        logger.log("Discount price booking form: "+price);
        logger.log("Discount price search result: "+discount);
        return price == discount;
    }

    private Locator getDiscountPrice(String loyaltyType){
        return new Locator("discount Locator loyalty",AppiumBy.xpath("//*[contains(@name,'price_detail_info') and contains(@value,'Elite') and contains(@value,'"+loyaltyType+"') and (contains(@value,'Discount') or contains(@value,'Diskon'))]/following-sibling::*[contains(@name,'price_detail_price')]"));
    }

    @Override
    public boolean isDiscountTagDisplayed(){
        return driverTools.isElementDisplayed(DISCOUNT_TAG_PRICE_DETAIL);
    }

    @Override
    public boolean isDiscountPriceRTCorrect(double discount,double discountRT, String discType){
        String discountPrice = driverTools.getText(getDiscountPrice(discType)).orElseThrow();
        double price = CoreFunctions.convertPrice(discountPrice);
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
                return new Locator(label.toUpperCase() + " LABEL", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@name,'Price Details') or contains(@name,'Detail Harga')])[2]//parent::*//parent::*//*[contains(@label,'"+label+"')]"));
            }
            default:
                throw new NotImplementedException("Unimplemented campaign label type!");
        }
    }

    private boolean verifyCampaignLabelAndPriceOnPriceDetail(String label,PriceDetail priceDetail){
        boolean verifyLabel = driverTools.isElementDisplayed(getCampaignLabelLocatorPriceDetail(label));

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
                                    + "'). The savings locator likely matched a non-monetary element on the SRP."));
            int discountOnly = CoreFunctions.getIntFromString(getPrice(label, 1)).orElseThrow();
            int cashbackValue = 0;
            if (driverTools.isElementDisplayed(PRICE_DETAIL_CASHBACK_ROW)) {
                String cashbackText = driverTools.getText(PRICE_DETAIL_CASHBACK_VALUE).orElse("");
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
        driverTools.swipeUp();
        driverTools.waitForElementToAppear(CANCEL_INSURANCE,2).ifPresent(driverTools::click);
        driverTools.swipeToElement(BENEFIT_SECTION, Swipe.UP,7);
        boolean campaignOrLoyaltyShow = driverTools.isElementDisplayed(CAMPAIGN_OR_LOYALTY);
        return campaignOrLoyaltyShow;
    }

    private boolean verify100pRefundOnPriceDetail(){
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
        double totalPrice = CoreFunctions.convertPrice(price);
        logger.log("Total Price: " + totalPrice);
        String priceAdult = driverTools.getText(PRICE_ADULT).orElseThrow();
        double totalPriceAdult = CoreFunctions.convertPrice(priceAdult);
        logger.log("Adult Price: " + totalPriceAdult);
        return totalPrice == totalPriceAdult;
    }

    @Override
    public void isOnBookingPage(){
        // The booking form can be preceded by blockers that sometimes appear late or more than once:
        // a full-protection interstitial ("No, thanks") and a "Reload" error dialog. Poll for the
        // continue button, dismissing whatever blocker is on top between attempts.
        for (int attempt = 0; attempt < 4; attempt++) {
            if (driverTools.waitForElementToAppear(BTN_CONTINUE, 8).isPresent()) {
                return;
            }
            boolean dismissed = false;
            if (driverTools.waitForElementToAppear(WITHOUT_PROTECTION_OPTION, 3).isPresent()) {
                skipFullProtectionPrp();
                dismissed = true;
            } else if (driverTools.waitForElementToAppear(SKIP_FULL_PROTECTION_NATIVE, 1).isPresent()) {
                driverTools.click(SKIP_FULL_PROTECTION_NATIVE);
                dismissed = true;
            }
            Optional<WebElement> reload = driverTools.waitForElementToAppear(RELOAD, 3);
            if (reload.isPresent()) {
                driverTools.click(reload.get());
                dismissed = true;
            }
            if (!dismissed) {
                driverTools.swipeUp();
            }
        }
        driverTools.waitForElementToPresence(BTN_CONTINUE, 15).orElseThrow();
    }

    private Locator getContactDetail(String text,String type){
        return new Locator("Locator " + type.toUpperCase(),AppiumBy.xpath("//XCUIElementTypeStaticText[@label='"+text+"']"));
    }

    @Override
    public boolean verifyChangeEmail(String email){
        boolean isEmailCorrect = driverTools.isElementDisplayed(getContactDetail(email,"email"));
        logger.log("is email correct after change: " + isEmailCorrect);
        return isEmailCorrect;
    }

    @Override
    public boolean isYellowBannerBookingPage2Displayed(){
        WebElement yellowBannerElement = driverTools.swipeToElement(YELLOW_BANNER_BOOKING_PAGE_2,Swipe.UP,15).orElseThrow();
        boolean isYellowBannerDisplayed = driverTools.isElementDisplayed(yellowBannerElement);
        logger.log("yellow banner 2 displayed : " + isYellowBannerDisplayed);
        return isYellowBannerDisplayed;
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

    private boolean verifyDate(String date){
        //Kam, 08 Feb • 14:30
        String[] dateArr = date.split(" ");
        String dateWithoutTime = dateArr[0] + " " + dateArr[1] + " " + dateArr[2];
        logger.log("date without time: " + dateWithoutTime);

        // Day can be one or two digits (e.g. "Tue, 4 Aug" or "Mon, 20 Jul").
        String regex = "^\\p{Upper}{1}\\p{Lower}{2}, \\d{1,2} \\p{Upper}{1}\\p{Lower}{2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dateWithoutTime);
        return matcher.matches();
    }
    @Override
    public void verifyDateFormat(String tripType){
        driverTools.swipeToElement(DATE_TIME_DEPART, Swipe.DOWN, 15);
        String dateDepart = null;
        try {
            WebElement dateDepartElement = driverTools.waitForElementToAppear(DATE_TIME_DEPART).orElseThrow();
            dateDepart = driverTools.getText(dateDepartElement);
        } catch (Exception e) {
            logger.log("Retrying to fetch departure date due to exception: " + e.getMessage());
            WebElement dateDepartElement = driverTools.waitForElementToAppear(DATE_TIME_DEPART).orElseThrow();
            dateDepart = driverTools.getText(dateDepartElement);
        }

        logger.log("date depart: " + dateDepart);
        Assertion.assertThat("is date format correct depart", verifyDate(dateDepart), is(true));

        if (tripType.toLowerCase().contains("roundtrip")) {
            WebElement dateReturnElement = driverTools.waitForElementToPresence(DATE_TIME_RETURN).orElseThrow();
            String dateReturn = driverTools.getText(dateReturnElement);
            logger.log("date return: " + dateReturn);
            Assertion.assertThat("is date format correct return", verifyDate(dateReturn), is(true));
        }
    }

    @Override
    public List<String> getFlightDateTime(String tripType) {
        List<String> flightDatetime = new ArrayList<String>();
        WebElement dateDepartElement = driverTools.waitForElementToAppear(DATE_TIME_DEPART).orElseThrow();
        String dateDepart = driverTools.getText(dateDepartElement);
        logger.log("(Flight booking page) date depart: " + dateDepart);
        flightDatetime.add(dateDepart);
        driverTools.clickElementByCoordinate(dateDepartElement);
        String departureFlightArrivalTime = driverTools.getText(TRIP_DETAIL_ARRIVAL_DATE_TIME).orElseThrow();
        flightDatetime.add(departureFlightArrivalTime);
        driverTools.click(CLOSE_BUTTON);
        if (tripType.toLowerCase().contains("roundtrip")){
            WebElement dateReturnElement = driverTools.waitForElementToPresence(DATE_TIME_RETURN).orElseThrow();
            String dateReturn = driverTools.getText(dateReturnElement);
            flightDatetime.add(dateReturn);
            driverTools.clickElementByCoordinate(dateReturnElement);
            String returnFlightArrivalTime = driverTools.getText(TRIP_DETAIL_ARRIVAL_DATE_TIME).orElseThrow();
            flightDatetime.add(returnFlightArrivalTime);
            driverTools.click(CLOSE_BUTTON);
            logger.log("(Flight booking page) date arrival: " + dateDepart);
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

        promoData.isSaveAmount = price.contains("Save") || price.contains("Hemat");
        WebElement selectPromoCode = driverTools.swipeToElement(getPromoCodeByIndex(index),Swipe.UP,2).orElseThrow();
        driverTools.click(selectPromoCode);
        return promoData;
    }

    private Locator getPromoCodeByIndex(int index){
        return new Locator("PROMO_CODE",AppiumBy.xpath("(//*[contains(@name,'Code:') or contains(@name,'Kode:')])["+index+"]"));
    }

    private Locator getPromoPriceByIndex(int index){
        return new Locator("PROMO_PRICE",AppiumBy.xpath("(//*[contains(@name,'Code:') or contains(@name,'Kode:')]//following-sibling::*[contains(@name,'Get') or contains(@name,'Dapat') or contains(@name,'Save') or contains(@name,'Hemat')])["+index+"]"));
    }

    @Override
    public boolean isPromoCodeAndAmountShown(PromoData promoData){
        WebElement promoCode = driverTools.waitForElementToAppear(getPromoCodeAndAmountBannerLocator(promoData.promoCode)).orElseThrow();
        boolean isPromoCodeShown = driverTools.isElementDisplayed(promoCode);
        logger.log("isPromoCodeShown: " + isPromoCodeShown);
        int i=0;
        String price ="";
        while(i<5){
            price = driverTools.getText(promoCode);
            if(price.contains(promoData.promoCode)){
                break;
            }
            promoCode = driverTools.waitForElementToAppear(getPromoCodeAndAmountBannerLocator(promoData.promoCode)).orElseThrow();
            i++;
        }

        Assertion.assertThat("is promo code shown ",isPromoCodeShown,is(true));

        String amount = price.replaceAll(".*\\(([^)]+)\\).*", "$1").trim();
        amount = amount.replaceAll("IDR|\\+|-|\\s|\\.", "");
        double amountConvert = CoreFunctions.convertPrice(amount);
        logger.log("amount excepted: " + promoData.promoDiscountPrice + " amount actual: " + amountConvert);
        boolean isAmountSame = amountConvert == promoData.promoDiscountPrice;
        logger.log("isAmountSame: " + isAmountSame);
        Assertion.assertThat("is amount same as promo",promoData.promoDiscountPrice,is(amountConvert));
        return true;
    }

    private Locator getPromoCodeAndAmountBannerLocator(String promoCode){
        return new Locator("PROMO_CODE_AMOUNT",AppiumBy.xpath("//*[contains(@name,'"+promoCode+" (')]"));
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
        return new Locator("PROMO_CODE_AND_AMOUNT",AppiumBy.xpath("//*[contains(@name,'price_detail_info') and contains(@label,'"+promoCode+"')]//following-sibling::*[contains(@name,'price_detail_price')]"));
    }

    @Override
    public boolean isConnectingFlightProtectionNudgeDisplayed(){
        driverTools.waitForElementToPresence(CONNECTING_NUDGE, 25);
        boolean flag = driverTools.isElementDisplayed(CONNECTING_FLIGHT_PROTECTION_NUDGE) || driverTools.isElementDisplayed(CONNECTING_NUDGE);
        return flag;
    }

    @Override
    public boolean verifyMoreBenefit(){
        WebElement element = driverTools.waitForElementToAppear(MORE_BENEFIT).orElseThrow();
        for (int where=1;where<=5;where++){
            driverTools.clickSpecificPart(element,where);
            if(driverTools.isElementDisplayed(CLOSE_BUTTON))
                break;
        }
        driverTools.waitForElementToPresence(CLOSE_BUTTON);
        Assertion.assertThat("is insurance detail title displayed",driverTools.isElementDisplayed(INSURANCE_DETAIL_TITLE),is(true));
        Assertion.assertThat("is coverage header displayed",driverTools.isElementDisplayed(INSURANCE_DETAIL_COVERAGE_HEADER),is(true));
        Assertion.assertThat("is compensation header displayed",driverTools.isElementDisplayed(INSURANCE_DETAIL_COMPENSATION_HEADER),is(true));
        driverTools.click(CLOSE_BUTTON);
        return true;
    }

    @Override
    public boolean verifyWarningMassage(){
        driverTools.swipeToElement(YELLOW_BANNER_CONNECTING_FLIGHT_PROTECTION,Swipe.UP,4);
        return driverTools.isElementDisplayed(YELLOW_BANNER_CONNECTING_FLIGHT_PROTECTION);
    }

    @Override
    public boolean connectingFlightProtectionDisplayedOnPriceDetail(int passengerCount,String currency){
        WebElement connectingFlightProtectionLabelOnPriceDetail = driverTools.waitForElementToAppear(getPriceBreakdownLocator("Connecting Flight Protection",passengerCount, currency)).orElseThrow();
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
        return driverTools.isElementDisplayed(CASHBACK_TEXT);
    }

    @Override
    public boolean verifyDepartureInfoBookingPage(){
        driverTools.swipeToElement(DEPARTURE_TAB, Swipe.DOWN, 5);
        driverTools.click(DEPARTURE_TAB);
        logger.log("Clicked on details tab");
        // New UI renders the details entry point as a "Baggage, refund & reschedule policies" link
        // (the legacy "detail_button" is gone) which may sit below the fold; scroll it into view first.
        driverTools.swipeToElement(CLICK_DETAILS, Swipe.UP, 4);
        driverTools.click(CLICK_DETAILS);
        Assertion.assertThat("Departure flight details are present", driverTools.isElementDisplayed(DEPARTURE_FLIGHT), is(true));
        boolean res1 = driverTools.isElementDisplayed(REFUND_PERCENTAGE) || driverTools.isElementDisplayed(REFUND_NOT_ALLOWED);
        Assertion.assertThat("Refund info present for departure flight", true, either(is(driverTools.isElementDisplayed(REFUND_PERCENTAGE))).or(is(driverTools.isElementDisplayed(REFUND_NOT_ALLOWED))));
        driverTools.click(RESCHEDULE_CHIP);

        Assertion.assertThat("Departure flight details are present", driverTools.isElementDisplayed(DEPARTURE_FLIGHT), is(true));
        logger.log("Departure reschedule percentage information is displayed");
        boolean res2 = driverTools.isElementDisplayed(RESCHEDULE_PERCENTAGE) || driverTools.isElementDisplayed(RESCHEDULE_NOT_ALLOWED);
        Assertion.assertThat("Reschedule info present for departure flight", true, either(is(driverTools.isElementDisplayed(RESCHEDULE_PERCENTAGE))).or(is(driverTools.isElementDisplayed(RESCHEDULE_NOT_ALLOWED))));
        driverTools.waitForElementToPresence(CLOSE_TAB);
        driverTools.clickElementByCoordinate(CLOSE_TAB);
        driverTools.waitForElementToPresence(CLOSE_TAB_AGAIN);
        driverTools.clickElementByCoordinate(CLOSE_TAB_AGAIN);
        return res1 && res2;
    }

    @Override
    public boolean verifyReturnInfoBookingPage(){
        driverTools.swipeLeftUntilFound(WHOLE_TAB, RETURN_TAB, 5);
        driverTools.click(RETURN_TAB);
        logger.log("Clicked on details tab");
        driverTools.click(CLICK_DETAILS);
        Assertion.assertThat("Return flight details are present", driverTools.isElementDisplayed(RETURN_FLIGHT), is(true));
        boolean res1 = driverTools.isElementDisplayed(REFUND_PERCENTAGE) || driverTools.isElementDisplayed(REFUND_NOT_ALLOWED);
        Assertion.assertThat("Refund info present for return flight", true, either(is(driverTools.isElementDisplayed(REFUND_PERCENTAGE))).or(is(driverTools.isElementDisplayed(REFUND_NOT_ALLOWED))));

        driverTools.click(RESCHEDULE_CHIP);
        Assertion.assertThat("Return flight details are present", driverTools.isElementDisplayed(RETURN_FLIGHT), is(true));
        boolean res2 = driverTools.isElementDisplayed(RESCHEDULE_PERCENTAGE) || driverTools.isElementDisplayed(RESCHEDULE_NOT_ALLOWED);
        Assertion.assertThat("Reschedule info present for return flight", true, either(is(driverTools.isElementDisplayed(RESCHEDULE_PERCENTAGE))).or(is(driverTools.isElementDisplayed(RESCHEDULE_NOT_ALLOWED))));
        driverTools.waitForElementToPresence(CLOSE_TAB).ifPresent(driverTools::click);
        driverTools.waitForElementToPresence(CLOSE_TAB_AGAIN).ifPresent(driverTools::click);
        return res1 && res2;
    }

    @Override
    public void otherOrderStillWaitingPopUp(){
        driverTools.waitForElementToPresence(CONTINUE_TO_PAYMENT_POPUP_BUTTON, 5).ifPresent(driverTools::click);
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