package com.tiket.page.accommodation.hotel.bookingformpage;

import com.tiket.page.common.booking_form.CommonBookingFormPage;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.AddOnsType;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.payment.PaymentPageIOS;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.page.accommodation.hotel.roomdetailpage.RoomDetailPageIOS.ADD_ROOM_BUTTON;
import static com.tiket.page.accommodation.hotel.roomlistpage.RoomListPage.selectedRoomName;
import static com.tiket.page.accommodation.hotel.roomlistpage.RoomListPageIOS.BACK_BUTTON;
import static com.tiket.page.accommodation.hotel.roomlistpage.RoomListPageIOS.ROOM_FLEXIBLE_PAYMENT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

public class BookingFormIOS <T extends WebDriver> extends BookingFormPage<T> {

    public BookingFormIOS(Session<T> session) {
        super(session);
    }

    private static final Locator EMAIL_CONTACT_V3 = new Locator("EMAIL_CONTACT_V3", AppiumBy.id("email"));
    public static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", AppiumBy.id("hotel_name"));
    public static final Locator ROOM_NAME = new Locator("ROOM_NAME", AppiumBy.id("room_name"));
    public static final Locator CHECKIN_DATE = new Locator("CHECKIN_DATE", AppiumBy.id("check_in_out_date"));
    public static final Locator NIGHT_DURATION = new Locator("NIGHT_DURATION", AppiumBy.id("total_night_rooms"));
    public static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", AppiumBy.id("totalPriceText"));
    public static final Locator TOTAL_TO_PAY = new Locator("TOTAL_TO_PAY", AppiumBy.iOSClassChain("**/*[`name == 'totalView'`]/*[`name == 'rightText'`]"));
    private static final Locator GET_REFUND_TEXT = new Locator("GET_REFUND_TEXT", AppiumBy.xpath("//*[@name='totalPriceTableViewCell']//XCUIElementTypeStaticText[not(@label='GRATIS' or @label='FREE')]"));
    public static final Locator CROSSED_PRICE = new Locator("CROSS_PRICE", AppiumBy.id("total_cross_price"));
    /** Part 1 sticky footer CTA under {@code page1PriceFooterView}. */
    public static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.xpath("//*[@name='page1PriceFooterView']//*[@name='button']"));
    /** Legacy bare footer Other (pre–page1PriceFooterView builds). */
    private static final Locator CONTINUE_BUTTON_LEGACY = new Locator("CONTINUE_BUTTON_LEGACY", AppiumBy.xpath("//XCUIElementTypeOther[@name='button']"));
    /**
     * Part 2 (Tinjauan / Review) marker EN/ID.
     * Indo screens: Rincian Harga, Tinjauan; EN: Add-ons, Extra Protection, Price Summary.
     */
    private static final Locator BOOKING_FORM_PART2_MARKER = new Locator("BOOKING_FORM_PART2_MARKER", AppiumBy.xpath("//*[@name='Add-ons' or @label='Add-ons' or @name='Add-on' or @label='Add-on' or contains(@name,'Fasilitas tambahan') or contains(@label,'Fasilitas tambahan') or @name='Extra Protection' or @label='Extra Protection' or @name='Perlindungan Ekstra' or @label='Perlindungan Ekstra' or @name='Price Summary' or @label='Price Summary' or @name='Ringkasan Harga' or @label='Ringkasan Harga' or @name='Rincian Harga' or @label='Rincian Harga' or @name='Review' or @label='Review' or @name='Tinjauan' or @label='Tinjauan' or @name='Tinjau' or @label='Tinjau']"));
    /**
     * Part 2 CTA EN/ID (often below the fold until swiped).
     * EN: Continue to payment | ID: Lanjut bayar (prod Indo UI), also Lanjutkan ke pembayaran.
     */
    private static final Locator CONTINUE_TO_PAYMENT_CTA = new Locator("CONTINUE_TO_PAYMENT_CTA", AppiumBy.xpath("//XCUIElementTypeButton[translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='continue to payment' or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'continue to payment') or translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='lanjut bayar' or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lanjut bayar') or translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='lanjutkan ke pembayaran' or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lanjutkan ke pembayaran') or translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='lanjut ke pembayaran' or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lanjut ke pembayaran') or translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='lanjutkan pembayaran' or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lanjutkan pembayaran')]"));
    /** Older copy-based continue (non-Button / value) EN/ID. */
    private static final Locator CONTINUE_PAYMENT_BY_VALUE = new Locator("CONTINUE_PAYMENT_BY_VALUE", AppiumBy.xpath("//*[translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='continue to payment' or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='continue to payment' or translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='lanjut bayar' or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='lanjut bayar' or translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='lanjutkan ke pembayaran' or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='lanjutkan ke pembayaran' or translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='lanjut ke pembayaran' or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='lanjut ke pembayaran']"));
    private static final Locator CONTINUE_PAYMENT_PAGE2_FOOTER = new Locator("CONTINUE_PAYMENT_PAGE2_FOOTER", AppiumBy.xpath("//*[@name='page2PriceFooterView']//*[@name='button']"));
    /**
     * New builds: sticky footer may stay under page1PriceFooterView on Review, or use Other{@code name=button}
     * with contains-label (not exact XCUIElementTypeButton match).
     */
    private static final Locator CONTINUE_TO_PAYMENT_CTA_CONTAINS = new Locator("CONTINUE_TO_PAYMENT_CTA_CONTAINS", AppiumBy.xpath("//*[@name='button' or @type='XCUIElementTypeButton'][contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'continue to payment') or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'continue to payment') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lanjut bayar') or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lanjut bayar') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lanjutkan ke pembayaran') or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lanjutkan ke pembayaran') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lanjut ke pembayaran') or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lanjut ke pembayaran') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'select payment') or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'select payment') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'pilih pembayaran') or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'pilih pembayaran')]"));
    private static final Locator CONTINUE_PAYMENT_FOOTER_ANY = new Locator("CONTINUE_PAYMENT_FOOTER_ANY", AppiumBy.xpath("//*[@name='page2PriceFooterView' or @name='page1PriceFooterView']//*[@name='button']"));
    private static final Locator PAY_NOW_BUTTON = new Locator("PAY_NOW_BUTTON", AppiumBy.xpath("//*[contains(@name,'pay_now_btn') and @visible='true']"));
    private static final Locator PAY_NOW_BUTTON_SELECTED = new Locator("PAY_NOW_BUTTON_SELECTED", AppiumBy.xpath("//*[@name='pay_now_btn (selected)' and @visible='true']"));
    /** Alias for callers expecting CONTINUE_PAYMENT_BUTTON — points at part-1 footer. */
    public static final Locator CONTINUE_PAYMENT_BUTTON = CONTINUE_BUTTON;
    private static final Locator DOWNPAYMENT_CONTENT = new Locator("DOWNPAYMENT_CONTENT", AppiumBy.xpath("//*[@name='contentView']/*/*[@name='titleLabel' and (contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'downpayment') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'tiket dp'))]"));
    private static final Locator CONFIRM_BOOKING_CONTINUE_BUTTON = new Locator("CONFIRM_BOOKING_CONTINUE_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='continue' or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='lanjut']"));
    private static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON", AppiumBy.name("tds ic cross big"));
    private static final Locator PAY_BY_BUTTON = new Locator("PAY_BY_BUTTON", AppiumBy.xpath("//*[contains(@name,'pay_by_btn')]"));
    public static final Locator CONTACT_MRS = new Locator("CONTACT_MRS", AppiumBy.xpath("//*[contains(@name,'TVCell')]//*[contains(@name, 'radioButton') and @label='rb_nyonya']"));
    public static final Locator CONTACT_MS = new Locator("CONTACT_MS", AppiumBy.xpath("//*[contains(@name,'TVCell')]//*[contains(@name, 'radioButton') and @label='rb_nona']"));
    public static final Locator CONTACT_MR = new Locator("CONTACT_MR", AppiumBy.xpath("//*[contains(@name,'TVCell')]//*[contains(@name, 'radioButton') and @label='rb_tuan']"));
    public static final Locator CONTACT_NAME = new Locator("CONTACT_NAME", AppiumBy.id("fullname_field"));
    public static final Locator CONTACT_NUMBER = new Locator("CONTACT_NUMBER", AppiumBy.id("countryCode"));
    public static final Locator CONTACT_EMAIL = new Locator("CONTACT_EMAIL", AppiumBy.xpath("//*[@name='email_field' and @visible='true']"));
    public static final Locator GUEST_DETAIL = new Locator("GUEST_DETAIL", AppiumBy.xpath("//XCUIElementTypeCell[@name='TVCell<TACMBookingGuestDetailView>']//XCUIElementTypeButton[@name='tds ic edit']"));
    public static final Locator GUEST_DETAIL_MRS = new Locator("GUEST_DETAIL_MRS", AppiumBy.xpath("//*[contains(@label,'Kamar') or contains(@label,'Room')][last()]//following-sibling::XCUIElementTypeOther//*[contains(@name, 'radioButton') and @label='rb_nyonya']"));
    public static final Locator GUEST_DETAIL_MS = new Locator("GUEST_DETAIL_MS", AppiumBy.xpath("//*[contains(@label,'Kamar') or contains(@label,'Room')][last()]//following-sibling::XCUIElementTypeOther//*[contains(@name, 'radioButton') and @label='rb_nona']"));
    public static final Locator GUEST_DETAIL_MR = new Locator("GUEST_DETAIL_MR", AppiumBy.xpath("//*[contains(@label,'Kamar') or contains(@label,'Room')][last()]//following-sibling::XCUIElementTypeOther//*[contains(@name, 'radioButton') and @label='rb_tuan']"));
    public static final Locator GUEST_DETAIL_NAME = new Locator("GUEST_DETAIL_NAME", AppiumBy.id("guest_fullname_field"));
    public static final Locator GUEST_DETAIL_SAVE_BUTTON = new Locator("GUEST_DETAIL_SAVE_BUTTON", AppiumBy.name("save_button"));
    public static final Locator GUEST_DETAIL_EDIT_BUTTON = new Locator("GUEST_DETAIL_EDIT_BUTTON", AppiumBy.id("guest_name_button"));
    /** Classic Select Payment button (some builds / post–bottom-sheet). */
    public static final Locator SELECT_PAYMENT_BUTTON = new Locator("SELECT_PAYMENT_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'pilih pembayaran') or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'select payment') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'pilih pembayaran') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'select payment')]"));
    private static final Locator SELECT_PAYMENT_PAGE2_FOOTER = new Locator("SELECT_PAYMENT_PAGE2_FOOTER", AppiumBy.xpath("//*[@name='page2PriceFooterView']//*[@name='button']"));
    private static final Locator EXTRA_PROTECTION_PREMIUM_TAB = new Locator("EXTRA_PROTECTION_PREMIUM_TAB", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='extra_protection_btn'])[2]"));
    private static final Locator EXTRA_PROTECTION_STANDARD_TAB = new Locator("EXTRA_PROTECTION_STANDARD_TAB", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='extra_protection_btn'])[1]"));
    private static final Locator EXTRA_PROTECTION_PRICE = new Locator("EXTRA_PROTECTION_PRICE_STANDARD_TAB", AppiumBy.xpath("(//*[@name='extra_protection_price'])[last()]"));
    private static final Locator EXTRA_PROTECTION_PRICE_MO = new Locator("EXTRA_PROTECTION_PRICE_MO", AppiumBy.xpath("//*[contains(@name,'TVCell<TDSCardCompoundView>') and contains(@label,'/pax') and ./*[@value='Premium' or contains(@value,'Standar')]]"));
    private static final Locator EXTRA_PROTECTION_ADD_BUTTON = new Locator("EXTRA_PROTECTION_ADD_BUTTON", AppiumBy.xpath("(//*[@name='add_protection_btn'])[last()]"));
    private static final Locator EXTRA_PROTECTION_CANCEL_BUTTON = new Locator("EXTRA_PROTECTION_CANCEL_BUTTON", AppiumBy.xpath("(//*[@name='cancel_protection_btn'])[last()]"));
    public static final Locator DISCOUNT_FLAG = new Locator("DISCOUNT_FLAG", AppiumBy.xpath("//XCUIElementTypeCell[@name='TVCell<TACMInsuranceSectionView>+TACMInsuranceSectionView-0']//XCUIElementTypeStaticText[contains(@name,'%')]"));
    public static final Locator TOTAL_PRICE_DETAIL_ARROW_BUTTON = new Locator("TOTAL_PRICE_DETAIL_ARROW_BUTTON", AppiumBy.id("tds_ic_chevron_down"));
    public static final Locator SUBTOTAL_BUTTON = new Locator("SUBTOTAL_BUTTON", AppiumBy.iOSClassChain("**/*[`name == 'titleView'`]/*[`label == 'Subtotal'`]"));
    public static final Locator EXTRA_PROTECTION_ON_TOTAL_PRICE_DETAIL = new Locator("EXTRA_PROTECTION_ON_TOTAL_PRICE_DETAIL", AppiumBy.iOSClassChain("**/*[`name == 'TVCell<TACMAccordionPriceDetailCardView>' AND label CONTAINS 'Perlindungan' OR label CONTAINS 'Protection'`]"));
    public static final Locator SEARCH_OTHER_ROOM_BUTTON = new Locator("SEARCH_OTHER_ROOM_BUTTON", AppiumBy.iOSClassChain("**/*[`label == 'See other rooms' OR label == 'Lihat kamar lain'`]"));
    public static final Locator SUBMIT_RESCHEDULE_BOOK_BUTTON = new Locator("SUBMIT_RESCHEDULE_BOOK_BUTTON", AppiumBy.name("continueToPaymentButton"));
    public static final Locator AGREE_TNC_CHECK = new Locator("AGREE_TNC_CHECK", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'mengerti dan menyetujui') or contains(@name,'agree') or contains(@label,'mengerti dan menyetujui') or contains(@label,'agree')]/parent::*/XCUIElementTypeSwitch"));
    public static final Locator PROPOSE_RESCHEDULE_BUTTON = new Locator("PROPOSE_RESCHEDULE_BUTTON", AppiumBy.name("alertAction0"));
    private static final Locator BOOKING_SUMMARY_CONTAINER = new Locator("BOOKING_SUMMARY_CONTAINER", AppiumBy.name("TVCell<TACMMultiBFStayDetailView>"));
    public static final Locator ROOM_NAME_TEXT = new Locator("ROOM_NAME_TEXT", AppiumBy.id("room_name"));
    private static final Locator ROOM_TYPE_TAB = new Locator("ROOM_TYPE_TAB", AppiumBy.iOSClassChain("**/*[`name CONTAINS 'room_type_tab'`]"));
    public static final Locator CHECKOUT_TEXT = new Locator("CHECKOUT_TEXT", AppiumBy.name("checkout_date"));
    public static final Locator CHECKIN_TIME_TEXT = new Locator("CHECKIN_TIME_TEXT", AppiumBy.name("checkin_time"));
    public static final Locator ROOM_GUEST_TEXT = new Locator("ROOM_GUEST_TEXT", AppiumBy.xpath("//*[@name='TVCell<TACMMultiBFStayDetailView>']//XCUIElementTypeStaticText[contains(@label,' /')]"));
    public static final Locator ROOM_BED_TEXT = new Locator("ROOM_BED_TEXT", AppiumBy.xpath("//*[@name='TVCell<TACMMultiBFStayDetailView>']//XCUIElementTypeStaticText[contains(@label,' /')]//following-sibling::XCUIElementTypeStaticText[1]"));
    public static final Locator CANCELLATION_TEXT = new Locator("CANCELLATION_TEXT", AppiumBy.xpath("//XCUIElementTypeCell[@name='TVCell<TACMMultiBFStayDetailView>']//XCUIElementTypeStaticText[contains(@label, 'refund') or contains(@label, 'reschedule')]"));
    private static final Locator EXTRA_ADDON_PRICE = new Locator("EXTRA_ADDON_PRICE", AppiumBy.id("add_ons_price"));
    private static final Locator EXTRA_FACILITY_ADD_BUTTON = new Locator("EXTRA_FACILITY_ADD_BUTTON", AppiumBy.id("select_button"));
    private static final Locator ROOM_ADD_ONS_CHECKBOX = new Locator("ROOM_ADD_ONS_CHECKBOX", AppiumBy.id("checkbox"));
    private static final Locator ADDON_CATEGORY = new Locator("ADDON_CATEGORY", AppiumBy.xpath("//*[@name='add_ons (selected)']//XCUIElementTypeStaticText"));
    private static final Locator ADDON_NAME = new Locator("ADDON_NAME", AppiumBy.id("add_ons_name"));
    private static final Locator ADDON_CONTAINER = new Locator("ADDON_CONTAINER", AppiumBy.id("TVCell<TDSCarouselView>"));
    private static final Locator ADDON_CATEGORY_SELECTED = new Locator("ADDON_CATEGORY_SELECTED", AppiumBy.xpath("//*[@name='add_ons (selected)']//XCUIElementTypeStaticText"));
    private static final Locator ADDON_CATEGORY_POPUP = new Locator("ADDON_CATEGORY_POPUP", AppiumBy.xpath("(//*[@name='ic cancel grey']//following::XCUIElementTypeStaticText[@visible='true'])[1]"));
    private static final Locator ADDON_NAME_POPUP = new Locator("ADDON_NAME_POPUP", AppiumBy.xpath("//*[@name='TVCell<TACMMultiBFAddonsItemView>']//*[@name='add_ons_name']"));
    private static final Locator ADDON_PRICE_POPUP = new Locator("ADDON_PRICE_POPUP", AppiumBy.xpath("//*[@name='TVCell<TACMMultiBFAddonsItemView>']//*[@name='add_ons_price']"));
    private static final Locator PAYING_AMOUNT_TEXT_GREEN = new Locator("PAYING_AMOUNT_TEXT_GREEN", AppiumBy.xpath("//*[contains(@label,'cashback points')]"));
    private static final Locator ADDON_TOTAL_PRICE_POPUP = new Locator("ADDON_TOTAL_PRICE_POPUP", AppiumBy.xpath("//*[contains(@label,'Total')]//preceding-sibling::XCUIElementTypeStaticText[1]"));
    private static final Locator ADDON_INCREMENT_BUTTON = new Locator("ADDON_INCREMENT_BUTTON", AppiumBy.xpath("//*[@name='add_btn' and @visible='true']"));
    private static final Locator ROOM_TYPE_PRICE_BREAKDOWN = new Locator("ROOM_TYPE_PRICE_BREAKDOWN", AppiumBy.xpath("//*[@name='TVCell<TDSLabel>']//XCUIElementTypeStaticText"));
    // guest detail container preview
    public static final Locator GUEST_DETAIL_NAME_CONTAINER_PREVIEW = new Locator("GUEST_DETAIL_NAME_CONTAINER_PREVIEW", AppiumBy.id("guest_name_button"));

    // contact detail container preview
    public static final Locator CONTACT_DETAIL_NAME = new Locator("CONTACT_DETAIL_NAME", AppiumBy.id("contact_details_name"));
    public static final Locator CONTACT_DETAIL_PHONE_NUMBER = new Locator("CONTACT_DETAIL_PHONE_NUMBER", AppiumBy.name("contact_details_phone_number"));
    public static final Locator CONTACT_DETAIL_EMAIL = new Locator("CONTACT_DETAIL_EMAIL", AppiumBy.name("contact_details_email"));
    public static final Locator EDIT_CONTACT_BUTTON = new Locator("EDIT_CONTACT_BUTTON", AppiumBy.xpath("//XCUIElementTypeCell[@name='TVCell<TACMMultiBFContactDetailView>']/XCUIElementTypeButton[@name='tds ic chevron right']"));
    public static final Locator FULL_NAME_INPUT = new Locator("FULL_NAME_INPUT", AppiumBy.name("fullname_field"));
    public static final Locator PHONE_NUMBER_INPUT = new Locator("PHONE_NUMBER_INPUT", AppiumBy.name("countryCode"));
    public static final Locator SAVE_BUTTON_V3 = new Locator("SAVE_BUTTON_V3", AppiumBy.xpath("//XCUIElementTypeButton[@label='Save' or @label='Simpan' or @label='SAVE' or @label='SIMPAN']"));
    public static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", AppiumBy.name("save_button"));
    public static final Locator MR_SALUTATION_CHECKBOX = new Locator("MR_SALUTATION_CHECKBOX", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Mr.' or @label='Tuan']"));
    public static final Locator MS_SALUTATION_CHECKBOX = new Locator("MS_SALUTATION_CHECKBOX", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Ms.' or @label='Nona']"));
    public static final Locator MRS_SALUTATION_CHECKBOX = new Locator("MRS_SALUTATION_CHECKBOX", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Mrs.' or @label='Nyonya']"));
    private static final Locator PRICE_SUMMARY_EXPAND_BUTTON = new Locator("PRICE_SUMMARY_EXPAND_BUTTON", AppiumBy.xpath("//*[@name='TVCell<TSHCPriceSummaryView>']//*[@name='titleView']//*[@name='leftTextChevronImage']"));
    public static final Locator PRICE_BREAKDOWN = new Locator("PRICE_BREAKDOWN", AppiumBy.xpath("//*[@name='leftText' and not(contains(@label,'check-in'))]//parent::*[@name='titleView']//following-sibling::*[@name='groupView']//*[@name='rightText' and @label]"));
    public static final Locator PRICE_BREAKDOWN_RESCHEDULE = new Locator("PRICE_BREAKDOWN_RESCHEDULE", AppiumBy.xpath("//XCUIElementTypeImage[@name='ic-cancel-grey']//following::XCUIElementTypeStaticText[(contains(@label,'.') or contains(@label,',')) and contains(@label,' ') and @visible='true']"));
    public static final Locator PRICE_DETAIL_CLOSE_BTN = new Locator("PRICE_DETAIL_CLOSE_BTN", AppiumBy.accessibilityId("ic cancel grey"));
    public static final Locator PRICE_DETAIL_CLOSE_BTN_RESCHEDULE = new Locator("PRICE_DETAIL_CLOSE_BTN_RESCHEDULE", AppiumBy.accessibilityId("ic-cancel-grey"));
    private static final Locator PAY_AT_HOTEL_TOTAL_PRICE = new Locator("PAY_AT_HOTEL_TOTAL_PRICE", AppiumBy.name("totalPriceText"));
    private static final Locator ROOM_GUEST_INFO = new Locator("ROOM_GUEST_INFO", AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`name == \"TVCell<TACMBookingGuestDetailFormView>\"`]/XCUIElementTypeTextField"));
    public static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", AppiumBy.xpath("//XCUIElementTypeCell[@name='TVCell<TDSBannerView>+loginBanner']/XCUIElementTypeOther[2]"));

    //popup
    public static final Locator PRICE_CHANGES_POPUP = new Locator("PRICE_CHANGES_POPUP", AppiumBy.xpath("//XCUIElementTypeStaticText[translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='harga baru saja diperbarui' or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='the price has been updated' or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='perlindungan ekstra sedang tidak tersedia' or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='extra protections are currently unavailable' or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='yay! the room you wanted just got cheaper' or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='hore! harga kamar yang kamu mau lagi turun']"));
    public static final Locator POLICY_CHANGES_POPUP = new Locator("POLICY_CHANGES_POPUP", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Detail kebijakan pembatalan berubah' or @label='Cancellation policy details have changed']"));
    public static final Locator POLICY_CHANGES_DESCRIPTION = new Locator("POLICY_CHANGES_DESCRIPTION", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Detail kebijakan pembatalan berubah' or @label='Cancellation policy details have changed']//following-sibling::XCUIElementTypeStaticText"));
    public static final Locator GO_PAYMENT_POLICY_CHANGES = new Locator("GO_PAYMENT_POLICY_CHANGES", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Detail kebijakan pembatalan berubah' or @label='Cancellation policy details have changed']//following-sibling::*//XCUIElementTypeButton"));
    private static final Locator PRICE_CHANGES_DESCRIPTION = new Locator("PRICE_CHANGES_DESCRIPTION", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Harga baru saja diperbarui' or @label='The price has been updated' or @label='Perlindungan Ekstra sedang tidak tersedia' or @label='Extra Protections are currently unavailable' or @label='Yay! The room you wanted just got cheaper' or @label='Hore! Harga kamar yang kamu mau lagi turun']//following-sibling::XCUIElementTypeStaticText"));
    private static final Locator GO_PAYMENT_PRICE_CHANGES = new Locator("GO_PAYMENT_PRICE_CHANGES", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Harga baru saja diperbarui' or @label='The price has been updated' or @label='Perlindungan Ekstra sedang tidak tersedia' or @label='Extra Protections are currently unavailable' or @label='Yay! The room you wanted just got cheaper' or @label='Hore! Harga kamar yang kamu mau lagi turun']//following-sibling::*//XCUIElementTypeButton"));
    private static final Locator RATE_PLAN_BOTTOM_SHEET = new Locator("RATE_PLAN_BOTTOM_SHEET", AppiumBy.xpath("//XCUIElementTypeButton[(contains(@label,'lanjut pesan') or contains(@label,'Lanjut pesan') or contains(@label,'Lanjutkan pemesanan') or contains(@label,'Continue booking') or contains(@label,'continue booking') or contains(@label,'Tetap pesan kamar ini') or contains(@label,'Book this room anyway')) and @visible='true']"));
    /**
     * Rate-plan / multi-change sheet. Exclude booking footer cashback {@code benefitBanner/containerView}.
     * Require a continue/price cue so unrelated {@code containerView} nodes are not clicked.
     */
    private static final Locator RATE_PLAN_MULTI_CHANGES = new Locator("RATE_PLAN_MULTI_CHANGES", AppiumBy.xpath("//*[@name='containerView' and not(ancestor::*[@name='benefitBanner']) and not(ancestor::*[@name='footerContainer']) and (.//XCUIElementTypeButton[contains(@label,'Continue booking') or contains(@label,'Lanjut pesan') or contains(@label,'Lanjutkan pemesanan') or contains(@label,'lanjut pesan')] or .//*[contains(@label,'Price') or contains(@label,'Harga')])]"));
    private static final Locator OK_BUTTON_PRICE_CHANGE = new Locator("OK_BUTTON_PRICE_CHANGE", AppiumBy.xpath("//*[@name='footerContainer']//following-sibling::*//XCUIElementTypeButton"));
    private static final Locator PRICE_SUMMARY_CASHBACK = new Locator("PRICE_SUMMARY_CASHBACK", AppiumBy.xpath("//*[@name='containerView']/*[@name='textLabel']"));
    //price detail element
    private static final Locator LOYALTY_BENEFIT_DISCOUNT_LABEL = new Locator("LOYALTY_BENEFIT_DISCOUNT_ON_PRICE_DETAIL", AppiumBy.xpath("//*[@name='other_price_title' and contains(@label,'tier')]"));
    private static final Locator OTHER_PRICE = new Locator("OTHER_PRICE", AppiumBy.id("other_price"));
    public static final Locator CANCEL_BUTTON = new Locator("CANCEL_BUTTON", AppiumBy.accessibilityId("ic cancel grey"));
    private static final Locator GET_REFUND_ESTIMATION = new Locator("GET_REFUND_ESTIMATION", AppiumBy.iOSClassChain("**/*[`name == 'totalPriceTableViewCell'`]/*[`label CONTAINS 'refund'`]"));
    private static final Locator ONE_ROOM_POPUP = new Locator("ONE_ROOM_POPUP", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Log in dan pesan 1 tipe kamar') or contains(@name,'Log in and book 1 room type')]"));
    private static final Locator MO_CORPORATE_POPUP = new Locator("MO_CORPORATE_POPUP", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label, \"This booking does not apply to corporate accounts\") or @label=\"Pemesanan ini tidak berlaku untuk akun corporate\"]"));
    private static final Locator RESCHEDULE_PRICE_UPDATE_POPUP_CONFIRMATION  = new Locator("RESCHEDULE_PRICE_UPDATE_POPUP_CONFIRMATION", AppiumBy.accessibilityId("alertAction0"));
    private static final Locator REFUND_ESTIMATION = new Locator("REFUND_ESTIMATION", AppiumBy.xpath("//*[contains(@name,'totalPriceTableViewCell')]//*[contains(@name,'refund')]"));
    private static final Locator ROOM_PRICE_TITLE = new Locator("PRICE_TITLE", AppiumBy.xpath("//*[@name='TVCell<TSHCPriceSummaryView>']//*[@name='groupView']/*[@name='leftText']"));
    private static final Locator TOTAL_PRICE_LABEL = new Locator("TOTAL_PRICE_LABEL", AppiumBy.xpath("//*[@label='Total (setelah pajak)' or @label='Total (after taxes)' or @label='Pay now (after taxes)' or @label='Bayar sekarang (setelah pajak)' or @label='Estimated total' or @label='Perkiraan total']"));
    private static final Locator GUEST_NAME_WARNING_TEXT = new Locator("GUEST_NAME_WARNING_TEXT", AppiumBy.xpath("//*[contains(@label,'Must consist') or contains(@label,'Harus terdiri')]"));
    private static final Locator RATE_PLAN_BOTTOM_SHEET_BOOKING_PAGE_PART_1 = new Locator("RATE_PLAN_BOTTOM_SHEET_BOOKING_PAGE_PART_1", AppiumBy.xpath("//*[contains(@label,'Must consist') or contains(@label,'Harus terdiri')]"));
    private static final Locator PAY_NOW_PRICE = new Locator("PAY_NOW_PRICE", AppiumBy.xpath("//*[( @name=\"Pay now\" or @name=\"Bayar sekarang\" ) and ( @label=\"Pay now\" or @label=\"Bayar sekarang\" )]/following-sibling::*[1]"));
    private static final Locator PAY_UPON_CHECK_IN_PRICE = new Locator("PAY_UPON_CHECK_IN_PRICE", AppiumBy.xpath("//*[@name=\"Pay upon check-in\" or @name=\"Bayar saat check-in\"]/following-sibling::*[1]"));
    private static final Locator PROPERTY_FEE_DISCLAIMER = new Locator("PROPERTY_FEE_DISCLAIMER", AppiumBy.xpath("//*[ (contains(@label,\"Estimated total\") or contains(@label,\"Perkiraan total\")) and (contains(@name,\"Estimated total\") or contains(@name,\"Perkiraan total\")) ]"));
    private static final Pattern PRICE_PATTERN = Pattern.compile("[A-Z]{2,4}\\s*[\\d.,]+");
    private static final Locator BOOKING_FORM_PAGE2_PAY_UPON_CHECK_IN = new Locator("BOOKING_FORM_PAGE2_PAY_UPON_CHECK_IN",
            AppiumBy.xpath("//*[@label=\"Bayar saat check-in\" or @label=\"Pay upon check-in\"]/following-sibling::*[last()]"));
    private static final Locator BOOKING_FORM_PAGE2_ESTIMATED_TOTAL = new Locator("BOOKING_FORM_PAGE2_ESTIMATED_TOTAL",
            AppiumBy.xpath("//*[contains(@label,\"Estimated total\") or contains(@label,\"Perkiraan total\")]"));
    private static final Locator TOTAL_PRICE_PRICE_BREAKDOWN = new Locator("TOTAL_PRICE_PRICE_BREAKDOWN", AppiumBy.xpath("//*[@label=\"Diskon Saleprise\" or @label=\"Pay now (after taxes)\"]/following-sibling::*[last()]"));
    private static final Locator RATE_PLAN_SNACK_BAR_TEXT = new Locator("RATE_PLAN_SNACK_BAR_TEXT", AppiumBy.name("snackbarMessage"));
    private static final Locator RATE_PLAN_SNACK_BAR_BTN = new Locator("RATE_PLAN_SNACK_BAR_BTN", AppiumBy.xpath("//*[@name='snackbarMessage']//following-sibling::*[@name='button']"));
    //promo related
    private static final Locator CHECK_MORE_PROMO = new Locator("CHECK_MORE_PROMO",AppiumBy.name("checkMorePromoLabel"));
    private static final Locator PROMO_TEXT_INPUT = new Locator("PROMO_TEXT_INPUT", AppiumBy.xpath("//*[@name='Enter your code' or @label='Enter your code' or @name='Masukkan kode Anda' or @label='Masukkan kode Anda']"));
    private static final Locator APPLY_PROMO_BUTTON = new Locator("APPLY_PROMO_BUTTON",AppiumBy.xpath("//*[@name='Apply' or @label='Apply' or @name='Terapkan' or @label='Terapkan']"));

    @Override
    public String getLocalPriceValue() {
        return ""; //FIXME: Pending implementation
    }

    @Override
    public String detectAndDismissPriceChangeSnackbar() {
        driverTools.waitForElementToAppear(RATE_PLAN_SNACK_BAR_TEXT, 5);
        String snackbarText = driverTools.getText(RATE_PLAN_SNACK_BAR_TEXT).orElseThrow();
        logger.log("Price-change snackbar detected on booking form: " + snackbarText);
        driverTools.waitForElementToPresence(RATE_PLAN_SNACK_BAR_BTN, 3).ifPresent(driverTools::click);
        return snackbarText;
    }

    @Override
    public String cancelRatePLanBottomSheetBookingPart1() {
        if (driverTools.waitForElementToPresence(OK_BUTTON_PRICE_CHANGE).isPresent()){
            driverTools.clickElementByCoordinate(OK_BUTTON_PRICE_CHANGE);
        }
        if(driverTools.swipeToElement(RATE_PLAN_BOTTOM_SHEET, Swipe.UP, 2).isPresent()){
            driverTools.click(RATE_PLAN_BOTTOM_SHEET);
        }
        if(driverTools.isElementDisplayed(CANCEL_BUTTON)){
            driverTools.clickElementByCoordinate(CANCEL_BUTTON);
        }
        if (driverTools.isElementDisplayed(RATE_PLAN_MULTI_CHANGES)) {
            driverTools.clickElementByCoordinate(RATE_PLAN_MULTI_CHANGES);
        }
        if(driverTools.isElementDisplayed(RATE_PLAN_BOTTOM_SHEET_BOOKING_PAGE_PART_1)){
            driverTools.clickElementByCoordinate(RATE_PLAN_BOTTOM_SHEET_BOOKING_PAGE_PART_1);
        }
        return "";
    }

    @Override
    public String getLocalPriceOnPriceDetails() {
        return ""; //FIXME: Pending implementation
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

        Locator salutation = selectedSalutationLocator("contact");
        driverTools.swipeToElement(salutation, Swipe.DOWN, 3);
        driverTools.swipeToElement(salutation, Swipe.UP, 3);
        driverTools.click(salutation);
        driverTools.sendKeys(CONTACT_NAME, contactName);
        driverTools.hideKeyboard(1);
        driverTools.sendKeys(CONTACT_NUMBER, phoneNumber);
        driverTools.hideKeyboard(1);
        driverTools.sendKeys(CONTACT_EMAIL, email);
        driverTools.hideKeyboard(1);
    }

    @Override
    public void fillContactOrderDetail(String email) {
        String contactName = randomName();
        String phoneNumber = "8123" + randomInt(7);

        driverTools.swipeToElement(selectedSalutationLocator("contact"), Swipe.DOWN, 3);
        driverTools.swipeToElement(selectedSalutationLocator("contact"), Swipe.UP, 3);
        driverTools.click(selectedSalutationLocator("contact"));
        driverTools.sendKeys(CONTACT_NAME, contactName);
        driverTools.hideKeyboard(1);
        driverTools.sendKeys(CONTACT_NUMBER, phoneNumber);
        driverTools.hideKeyboard(1);
        driverTools.sendKeys(CONTACT_EMAIL, email);
        driverTools.hideKeyboard(1);
    }

    private String guestName = "";
    @Override
    public String fillGuestDetail(boolean checkRoomName) {
        String methodName = "fillGuestDetail";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            openGuestDetails(checkRoomName);
            guestName = submitGuestData();

            return guestName;
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public String verifyGuestDetailNameSize(int numberOfNameChar){
        driverTools.clear(getFullNameInputLocator(0));
        String randomName = randomName(numberOfNameChar);
        driverTools.sendKeys(getFullNameInputLocator(0), randomName);
        driverTools.click(SAVE_BUTTON);
        if(numberOfNameChar >=3 && numberOfNameChar <=50 ){
            logger.log("Guest name with "+ numberOfNameChar + " characters is accepted");
            return randomName;
        }
        WebElement warningText = driverTools.waitForElementToAppear(GUEST_NAME_WARNING_TEXT).orElseThrow();
        String warning = driverTools.getText(warningText);
        logger.log("warningText -> " + warning);
        return warning.trim().replace("-", "–");
    }

    @Override
    public String verifyGuestDetailDefaultNationality(String expectedNationality){
        WebElement nationalityElement = driverTools.waitForElementToPresence(getNationalityGuestDetailLocator(expectedNationality)).orElseThrow();
        String nationality = driverTools.getText(nationalityElement);
        logger.log("nationality -> " + nationality);
        return nationality.trim();
    }

    private Locator getNationalityGuestDetailLocator(String country){
        return new Locator("NATIONALITY",AppiumBy.xpath("//XCUIElementTypeTextField[contains(@value,'"+country+"')]"));
    }

    @Override
    public void openGuestDetailForm() {
        driverTools.swipeToElement(GUEST_DETAIL_EDIT_BUTTON, Swipe.UP, 5);
        driverTools.click(GUEST_DETAIL_EDIT_BUTTON);
    }

    @Override
    public String getNameOnGuestDetail(String expectedName){
        WebElement guestNameElement = driverTools.waitForElementToPresence(getNameLocatorOnGuestDetail(expectedName)).orElseThrow();
        String guestName = CoreFunctions.getNameOnGuestDetail(driverTools.getText(guestNameElement));
        logger.log("guest name -> " + guestName);
        return guestName.trim();
    }

    private Locator getNameLocatorOnGuestDetail(String expectedName){
        return new Locator("GUEST_NAME_GUEST_DETAIL", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'guest_name_button') and contains(@label,'"+expectedName+"')]"));
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
            String selectedRoomAttribute = driverTools.getValueAttribute(roomElement, "name");
            Assertion.assertThat("Room " + i + " is selected", selectedRoomAttribute, containsString("selected"));
            Assertion.assertThat("is default nationality is { " + expectedNationality +" } for room1",verifyGuestDetailDefaultNationality(expectedNationality),equalToIgnoringCase(expectedNationality));

            driverTools.clear(getFullNameInputLocator(0));
            driverTools.sendKeys(getFullNameInputLocator(0), guestNameList.get(i - 1));
            logger.log("index " + i + " lastRoom " + lastRoom);
            if (i == lastRoom-1) {
                logger.log("change room to next room: " + (i + 1));
                driverTools.click(roomLocator(i + 1));
            }
        }
        driverTools.click(SAVE_BUTTON);
    }

    private Locator roomLocator(int index) {
        return new Locator("ROOM_NAME_" + index, AppiumBy.xpath("//*[contains(@name,'room_type_tab') and contains(@label,'"+index+"')]"));
    }

    @Override
    public boolean selectSuggestedNameAndVerifyNationality(String expectedName, String expectedNationality){
        driverTools.clear(getFullNameInputLocator(0));
        driverTools.sendKeys(getFullNameInputLocator(0),expectedName.substring(0,3));
        WebElement suggestedName = driverTools.waitForElementToPresence(getGuestNameSuggestionLocator(expectedName)).orElseThrow();
        driverTools.click(suggestedName);

        WebElement suggestedNameElement = getFullNameInputLocator(0);
        String actualName = driverTools.getText(suggestedNameElement);
        logger.log("actualName -> " + actualName + " expectedName -> " + expectedName);
        Assertion.assertThat("is selected name is { " + expectedName +" }",actualName,equalToIgnoringCase(expectedName));

        WebElement nationalityElement = driverTools.waitForElementToPresence(getNationalityGuestDetailLocator(expectedNationality)).orElseThrow();
        String actualNationality = driverTools.getText(nationalityElement);
        logger.log("actualNationality -> " + actualNationality + " expectedNationality -> " + expectedNationality);
        Assertion.assertThat("is default nationality is { " + expectedNationality +" } for room1",actualNationality,equalToIgnoringCase(expectedNationality));
        return expectedNationality.equalsIgnoreCase(actualNationality);
    }

    private Locator getGuestNameSuggestionLocator(String expectedName){
        return new Locator("GUEST_NAME_SUGGESTION", AppiumBy.xpath("//*[contains(@name,'"+expectedName+"')]"));
    }

    private void openGuestDetails(boolean checkRoomName) {
        driverTools.swipeToElement(GUEST_DETAIL_EDIT_BUTTON, Swipe.UP, 5);
        if (checkRoomName) replaceRoomName();
        driverTools.click(GUEST_DETAIL_EDIT_BUTTON);
    }

    private void replaceRoomName() {
        if (!selectedRoomName.isEmpty()) {
            if (!selectedRoomName.get(1).equals(getRoomNameBookingForm())) {
                logger.log("selectedRoomName.size() -> " + selectedRoomName.size());
                for (int i = 0; i < selectedRoomName.size(); i++) {
                    List<WebElement> roomNameList = driverTools.elements(ROOM_NAME_TEXT).orElseThrow();
                    String roomName = driverTools.getText(roomNameList.get(i));
                    String[] roomNameSplit = roomName.split(" · ");
                    logger.log("loop i -> " + i);
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
        if (driverTools.waitForElementToPresence(ROOM_TYPE_TAB).isPresent()) {
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
        driverTools.waitForElementToPresence(GUEST_DETAIL_NAME).orElseThrow();
        int roomCount = driverTools.elements(GUEST_DETAIL_NAME).orElseThrow().size() + 1;
        logger.log("There is '" + (roomCount - 1) + "' guest info should be filled");

        for (int index = 1; index < roomCount; index++) {
            guestName = randomName() + " " + randomName();
            if(driverTools.isElementDisplayed(GUEST_DETAIL_MR)){
                WebElement salutation = driverTools.waitForElementToAppear(getSalutationRadioLocator(index - 1, generateRandomInt(0, 2))).orElseThrow();
                driverTools.click(salutation);
            }
            
            WebElement fullNameTextFieldElement = getFullNameInputLocator(index - 1);
            driverTools.clear(fullNameTextFieldElement);
            driverTools.sendKeys(fullNameTextFieldElement, guestName);
            driverTools.hideKeyboard(1);
        }
        return guestName;
    }

    private WebElement getFullNameInputLocator(int index) {
        return driverTools.elements(GUEST_DETAIL_NAME).orElseThrow().get(index);
    }

    private WebElement getSalutationRadioLocator(int index, int generateRandomInt) {
        switch (generateRandomInt) {
            case 0:
                return driverTools.elements(GUEST_DETAIL_MR).orElseThrow().get(index);
            case 1:
                return driverTools.elements(GUEST_DETAIL_MRS).orElseThrow().get(index);
            case 2:
                return driverTools.elements(GUEST_DETAIL_MS).orElseThrow().get(index);
            default:
                throw new NoSuchMethodError("There is no '" + generateRandomInt + "' case");
        }
    }

    @Override
    public List<String> getGuestNameList() {
        List<String> guestNameList = driverTools.getTexts(GUEST_DETAIL_NAME_CONTAINER_PREVIEW);
        guestNameList = guestNameList.stream().map(name -> name.split(" : ",2)[1]).collect(Collectors.toList());
        logger.log("guestNameList on the booking form: "+guestNameList);
        return guestNameList;
    }

    @Override
    public void submitRescheduleBookingForm() {
        driverTools.swipeToElement(AGREE_TNC_CHECK, Swipe.UP, 4).orElseThrow().click();

        driverTools.swipeToElement(SUBMIT_RESCHEDULE_BOOK_BUTTON,Swipe.UP,3);
        driverTools.swipeUp();
        driverTools.clickElementByCoordinate(SUBMIT_RESCHEDULE_BOOK_BUTTON);
        driverTools.clickElementByCoordinate(PROPOSE_RESCHEDULE_BUTTON);
        boolean isPricechangeAppear = driverTools.waitForElementToAppear(RESCHEDULE_PRICE_UPDATE_POPUP_CONFIRMATION,5).isPresent();
        if(isPricechangeAppear){
            driverTools.click(RESCHEDULE_PRICE_UPDATE_POPUP_CONFIRMATION);
        }
    }

    @Override
    public String getHotelNameBookingForm() {
        WebElement hotelNameText;
        hotelNameText = driverTools.swipeToPresentedElement(HOTEL_NAME, Swipe.UP, 5).orElseThrow();
        String hotelName = driverTools.getText(hotelNameText);
        hotelName = removeTrailingOrStaringSpace(hotelName);
        logger.log("Hotel Name Booking Form: " + hotelName);
        return hotelName;
    }

    @Override
    public String getHotelRoomNameBookingForm() {
        WebElement hotelRoomNameText;
        driverTools.waitForElementToAppear(ROOM_NAME);
        hotelRoomNameText = driverTools.swipeToElement(ROOM_NAME, Swipe.UP, 5).orElseThrow();
        String hotelRoomName = driverTools.getText(hotelRoomNameText);
        logger.log("Hotel Room Name Booking Form: " + hotelRoomName);
        return hotelRoomName;
    }

    @Override
    public boolean checkChildGuest() {
        return false; //FIXME: Pending implementation
    }

    @Override
    public boolean checkChildMeal() {
        return false; //FIXME: Pending implementation
    }

    @Override
    public String getHotelRoomCountBookingForm() {
        String roomCountText = driverTools.getText(ROOM_NAME_TEXT).orElseThrow();
        String[] roomCountSplit = roomCountText.split(" · ");
        String hotelRoomCount = roomCountSplit[0].replaceAll("[^0-9]", "");
        if (hotelRoomCount.isEmpty()) hotelRoomCount = "1";
        logger.log("Hotel Room Count Booking Form: " + hotelRoomCount);
        return hotelRoomCount;
    }

    @Override
    public void openLoginPage() {
        driverTools.click(LOGIN_BUTTON);
    }

    @Override
    public String getCheckinBookingForm() {
        WebElement checkInDateElement;
        checkInDateElement = driverTools.swipeToElement(CHECKIN_DATE, Swipe.UP, 5).orElseThrow();
        String[] dateRawArray = driverTools.getText(checkInDateElement).split(" ");

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
        WebElement nightDurationElement = driverTools.waitForElementToAppear(NIGHT_DURATION).orElseThrow();
        String nightDuration = driverTools.getText(nightDurationElement);
        String[] nightDurationSplit = nightDuration.split(" · ");
        nightDuration = nightDurationSplit[0]
                .replace(" Nights", "")
                .replace(" Night", "")
                .replace(" Malam", "");
        return nightDuration;
    }

    @Override
    public String getTotalPriceBookingForm() {
        WebElement totalPrice = driverTools.waitForElementToAppear(TOTAL_PRICE).orElseThrow();
        return driverTools.getValueAttribute(totalPrice, "label");
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
    public void addInsuranceOnBookingFormPage2(String estimatedTotal) {
        driverTools.swipeToElement(EXTRA_PROTECTION_ADD_BUTTON, Swipe.DOWN, 5);
        if (driverTools.waitForElementToAppear(EXTRA_PROTECTION_ADD_BUTTON).isPresent()) {
            driverTools.click(EXTRA_PROTECTION_ADD_BUTTON);
        } else {
            logger.log("Extra protection add button not found, skipping insurance addition");
        }
        verifyEstimatedTotalUnchangedAfterInsuranceOrAddOns(estimatedTotal);
    }

    public void verifyEstimatedTotalUnchangedAfterInsuranceOrAddOns(String estimatedTotalBeforeInsurance) {
        String estimatedTotalAfter = getEstimatedTotalFromBookingFormPage2();
        logger.log("estimated total before insurance -> " + estimatedTotalBeforeInsurance);
        logger.log("estimated total after insurance -> " + estimatedTotalAfter);
        assertPriceEquals("Estimated total unchanged after adding insurance on booking form page 2",
                estimatedTotalAfter, estimatedTotalBeforeInsurance);
    }

    @Override
    public String getTotalToPayBookingForm() {
        WebElement totalToPayElement = driverTools.swipeToElement(TOTAL_TO_PAY, Swipe.UP, 5).orElseThrow();
        return driverTools.getText(totalToPayElement);
    }

    @Override
    public String getTotalRefundPriceBookingForm() {
        driverTools.waitForElementToAppear(GET_REFUND_TEXT);
        driverTools.swipeToElement(GET_REFUND_TEXT, Swipe.UP, 5);
        String getRefundEstimation = driverTools.getValueAttribute(GET_REFUND_TEXT, "label")
                .replace("Get refund ", "")
                .replace("Dapat refund ", "");
        logger.log("getRefundEstimation-> " + getRefundEstimation);
        return getRefundEstimation;
    }

    @Override
    public String getTotalPriceInPriceDetail() {
        WebElement totalPrice = driverTools.swipeToElement(TOTAL_TO_PAY, Swipe.UP, 5).orElseThrow();
        return driverTools.getText(totalPrice);
    }

    @Override
    public String getContactNameDetail() {
        return driverTools.getText(CONTACT_DETAIL_NAME).orElseThrow();
    }

    @Override
    public String getContactPhoneNumberDetail() {
        driverTools.swipeToElement(CONTACT_DETAIL_PHONE_NUMBER, Swipe.DOWN, 2).orElseThrow();
        return driverTools.getText(CONTACT_DETAIL_PHONE_NUMBER).orElseThrow();
    }

    @Override
    public String getGuestNameDetail() {
        String guestName = driverTools.getText(GUEST_DETAIL_NAME_CONTAINER_PREVIEW).orElseThrow();
        String[] guestNameSplit = guestName.split(" : ");
        guestName = guestNameSplit[1];
        logger.log("guestName -> " + guestName);
        return guestName;
    }

    /**
     * Booking Details (part 1) → Review (part 2) → payment.
     * <ol>
     *   <li>Always tap sticky footer <b>Continue</b> on part 1</li>
     *   <li>On part 2, swipe to the end and tap <b>Continue to payment</b></li>
     * </ol>
     * Bottom sheet after that is optional (handled by price/rate-plan helpers).
     */
    @Override
    public void continueToPaymentPage() {
        String methodName = "continueToPaymentPage";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            clickBookingFormPart1Continue();
            clickBookingFormPart2ContinueToPayment();
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    /** Part 1: always click sticky footer Continue (do not mid-screen swipe). */
    private void clickBookingFormPart1Continue() {
        Locator part1;
        if (driverTools.waitForElementToAppear(CONTINUE_BUTTON, 8).isPresent()) {
            part1 = CONTINUE_BUTTON;
        } else if (driverTools.waitForElementToAppear(CONTINUE_BUTTON_LEGACY, 5).isPresent()) {
            part1 = CONTINUE_BUTTON_LEGACY;
        } else {
            logger.log("Booking form part-1 Continue footer not found — must click Continue before Review or checking the Booking form part-2");
            return;
        }
        logger.log("[continueToPaymentPage] part 1 — always tapping sticky Continue");
        driverTools.clickElementByCoordinate(part1);
        // Wait until Review / part 2 is loaded
        if (driverTools.waitForElementToPresence(BOOKING_FORM_PART2_MARKER, 15).isEmpty()
                && driverTools.waitForElementToPresence(CONTINUE_TO_PAYMENT_CTA, 8).isEmpty()) {
            throw new RuntimeException("Booking form part 2 (Review) did not load after part-1 Continue");
        }
        logger.log("[continueToPaymentPage] part 2 (Review) loaded");
    }

    @Override
    public boolean isBookingFormPart2Loaded() {
        return driverTools.waitForElementToPresence(BOOKING_FORM_PART2_MARKER).isPresent();
    }

    private void clickPayNowButton() {
        driverTools.swipeToElement(PAY_NOW_BUTTON, Swipe.UP, 5);
        if (driverTools.isElementDisplayed(PAY_NOW_BUTTON)) driverTools.clickElementByCoordinate(PAY_NOW_BUTTON);
        if (driverTools.isElementDisplayed(PAY_NOW_BUTTON) && !driverTools.isElementDisplayed(PAY_NOW_BUTTON_SELECTED)) driverTools.click(PAY_NOW_BUTTON);
    }

    /** Part 2: swipe to end of Review, then click Continue to payment. */
    private void clickBookingFormPart2ContinueToPayment() {
        clickPayNowButton();
        Locator part2 = resolvePart2ContinueToPaymentCta();
        logger.log("[continueToPaymentPage] part 2 — swipe to end / Continue to payment: " + part2);
        // Swipe list to the end until CTA is on-screen (visible=false in dump until scrolled).
        driverTools.swipeUp(2);
        if (driverTools.swipeToPresentedElement(part2, Swipe.UP, 15).isEmpty()
                && driverTools.swipeToElement(part2, Swipe.UP, 10).isEmpty()) {
            throw new RuntimeException("Part-2 Continue to payment not found after swiping to end");
        }
        driverTools.swipeToFullyVisibleElement(part2, Swipe.UP, 10);
        driverTools.isElementDisplayed(part2);
        driverTools.clickElementByCoordinate(part2);
        logger.log("[continueToPaymentPage] part 2 — Continue to payment clicked (bottom sheet optional)");
    }

    private Locator resolvePart2ContinueToPaymentCta() {
        // Preference order unchanged; single shared wait instead of stacked per-locator timeouts.
        Locator[] candidates = {
                CONTINUE_TO_PAYMENT_CTA,
                CONTINUE_PAYMENT_PAGE2_FOOTER,
                SUBMIT_RESCHEDULE_BOOK_BUTTON,
                CONTINUE_PAYMENT_BY_VALUE,
                SELECT_PAYMENT_BUTTON,
                SELECT_PAYMENT_PAGE2_FOOTER,
                CONTINUE_TO_PAYMENT_CTA_CONTAINS,
                CONTINUE_PAYMENT_FOOTER_ANY
        };
        Optional<Locator> found = driverTools.waitForAnyLocator(8, candidates);
        if (found.isPresent()) {
            return found.get();
        }
        // CTA may only appear in hierarchy after a swipe on long Review pages —
        // swipe then instant-probe all candidates (avoid stacked swipeToElement waits).
        for (int i = 0; i < 8; i++) {
            driverTools.swipeUp(1);
            found = driverTools.firstPresentLocator(candidates);
            if (found.isPresent()) {
                return found.get();
            }
        }
        throw new RuntimeException("Part-2 Continue to payment CTA not found");
    }

    @Override
    public void continueToAddOnPage() {
        // Part-1 only (legacy caller name) — sticky footer, no mid-screen swipe
        if (driverTools.waitForElementToAppear(CONTINUE_BUTTON, 5).isPresent()) {
            driverTools.clickElementByCoordinate(CONTINUE_BUTTON);
            return;
        }
        if (driverTools.waitForElementToAppear(CONTINUE_BUTTON_LEGACY, 5).isPresent()) {
            driverTools.clickElementByCoordinate(CONTINUE_BUTTON_LEGACY);
            return;
        }
        driverTools.click(CONTINUE_BUTTON);
    }

    @Override
    public void submitBooking() {
        // Prefer explicit Continue to payment / Select Payment CTA
        if (driverTools.waitForElementToPresence(CONTINUE_TO_PAYMENT_CTA, 5).isPresent()
                || driverTools.swipeToElement(CONTINUE_TO_PAYMENT_CTA, Swipe.UP, 6).isPresent()) {
            driverTools.swipeToPresentedElement(CONTINUE_TO_PAYMENT_CTA, Swipe.UP, 8);
            driverTools.clickElementByCoordinate(CONTINUE_TO_PAYMENT_CTA);
            return;
        }
        if (driverTools.waitForElementToAppear(SELECT_PAYMENT_BUTTON, 8).isPresent()) {
            driverTools.click(SELECT_PAYMENT_BUTTON);
            return;
        }
        if (driverTools.waitForElementToAppear(SELECT_PAYMENT_PAGE2_FOOTER, 5).isPresent()) {
            driverTools.clickElementByCoordinate(SELECT_PAYMENT_PAGE2_FOOTER);
            return;
        }
        driverTools.click(SELECT_PAYMENT_BUTTON);
    }

    @Override
    public void chooseProtection(String protectionType) {
        if (protectionType.equals("premium")) {
            driverTools.swipeToElement(EXTRA_PROTECTION_PREMIUM_TAB, Swipe.UP, 5).orElseThrow();
            driverTools.swipeUp();
            if (driverTools.isElementDisplayed(EXTRA_PROTECTION_CANCEL_BUTTON))
                driverTools.click(EXTRA_PROTECTION_CANCEL_BUTTON);
            WebElement premiumTab = driverTools.waitForElementToPresence(EXTRA_PROTECTION_PREMIUM_TAB).orElseThrow();
            driverTools.click(premiumTab);
        } else if (protectionType.equals("standard")) {
            driverTools.swipeToElement(EXTRA_PROTECTION_STANDARD_TAB, Swipe.UP, 5).orElseThrow();
            driverTools.swipeUp();
            if (driverTools.isElementDisplayed(EXTRA_PROTECTION_CANCEL_BUTTON))
                driverTools.click(EXTRA_PROTECTION_CANCEL_BUTTON);
            WebElement standardTab = driverTools.waitForElementToPresence(EXTRA_PROTECTION_STANDARD_TAB).orElseThrow();
            driverTools.click(standardTab);
        } else {
            logger.fail("wrong protectionType ");
        }
    }

    @Override
    public void addProtection() {
        driverTools.swipeToElement(EXTRA_PROTECTION_ADD_BUTTON, Swipe.DOWN, 5).orElseThrow();
        driverTools.click(EXTRA_PROTECTION_ADD_BUTTON);
    }

    @Override
    public boolean isAddonThereOnBookingForm() {
        return driverTools.isElementDisplayed(EXTRA_ADDON_PRICE);
    }

    @Override
    public void openPriceDetail() {
        driverTools.waitForElementToPresence(TOTAL_PRICE_DETAIL_ARROW_BUTTON);
        driverTools.click(TOTAL_PRICE_DETAIL_ARROW_BUTTON);
    }

    @Override
    public void openSubtotal() {
        driverTools.click(SUBTOTAL_BUTTON);
    }

    @Override
    public boolean isProtectionAdded(String protectionPrice) {
        return driverTools.waitForElementToPresence(extraProtectionOnTotalPriceDetail(protectionPrice)).isPresent();
    }

    private Locator extraProtectionOnTotalPriceDetail(String price) {
        return new Locator("EXTRA_PROTECTION_ON_TOTAL_PRICE_DETAIL", AppiumBy.xpath("//*[contains(@label,'Perlindungan') or contains(@label,'Protection')]//following-sibling::*[@label='" + price + "']"));
    }

    @Override
    public String getProtectionPrice() {
        String price;
        if (driverTools.isElementDisplayed(EXTRA_PROTECTION_PRICE_MO)) {
            String section = driverTools.getText(EXTRA_PROTECTION_PRICE_MO).orElseThrow();
            String[] sectionSplit = section.split(", ");
            int index = findIndexContains(sectionSplit, "pax");
            price = sectionSplit[index];
            logger.log("price-> " + price);
        } else{
            driverTools.waitForElementToPresence(EXTRA_PROTECTION_PRICE);
            price = driverTools.getText(EXTRA_PROTECTION_PRICE).orElseThrow();
        }
        return price;
    }

    private Locator getExtraProtectionFinalPrice(int index) {
        return new Locator("EXTRA_PROTECTION_PRICE", AppiumBy.xpath("//XCUIElementTypeCell[@name='TVCell<TACMInsuranceSectionView>+TACMInsuranceSectionView-0']//XCUIElementTypeStaticText[contains(@name,'IDR')][" + index + "]"));
    }

    private Locator selectedSalutationLocator(String form_type) {
        //generate random salutation
        if (form_type.equals("contact")) {
            Locator[] salutationList = {CONTACT_MR, CONTACT_MRS, CONTACT_MS};
            return getRandomObject(salutationList);
        } else if (form_type.equals("guest_detail")) {
            Locator[] salutationList = {GUEST_DETAIL_MR, GUEST_DETAIL_MRS, GUEST_DETAIL_MS};
            return getRandomObject(salutationList);
        }
        return null;
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
        driverTools.swipeToElement(EDIT_CONTACT_BUTTON, Swipe.DOWN, 5).orElseThrow();
        driverTools.click(EDIT_CONTACT_BUTTON);
        driverTools.click(randomSalutationLocator());
        driverTools.clear(FULL_NAME_INPUT);
        driverTools.sendKeys(FULL_NAME_INPUT, randomName());
        driverTools.clear(PHONE_NUMBER_INPUT);
        driverTools.sendKeys(PHONE_NUMBER_INPUT, "8123" + randomInt(6));
        driverTools.waitForElementToPresence(SAVE_BUTTON);
        driverTools.click(SAVE_BUTTON);
    }

    @Override
    public void selectRandomSalutationIfMissing() {
        String contactName = getContactNameDetail();
        if (!SALUTATION_PATTERN.matcher(contactName).find()) {
            logger.log("Contact name '" + contactName + "' has no salutation, selecting a random one");
            driverTools.click(CONTACT_DETAIL_NAME);
            driverTools.click(randomSalutationLocator());
            driverTools.waitForElementToPresence(SAVE_BUTTON);
            driverTools.click(SAVE_BUTTON);
        }
    }

    @Override
    public boolean isContactAndGuestDetailsFilled() {
        driverTools.waitForElementToPresence(CONTACT_DETAIL_NAME).orElseThrow();

        boolean isContactDetailNameFilled = driverTools.isElementDisplayed(CONTACT_DETAIL_NAME);
        Assertion.assertThat("Assert if contact detail name is displayed and filled", isContactDetailNameFilled, is(true));
        logger.log("Contact Detail Name = '" + driverTools.getText(CONTACT_DETAIL_NAME) + "'");

        boolean isContactDetailEmailFilled = driverTools.isElementDisplayed(CONTACT_DETAIL_EMAIL);
        Assertion.assertThat("Assert if contact detail email is displayed and filled", isContactDetailEmailFilled, is(true));
        logger.log("Contact Detail Email = '" + driverTools.getText(CONTACT_DETAIL_EMAIL) + "'");

        boolean isContactDetailPhoneNumberFilled = driverTools.isElementDisplayed(CONTACT_DETAIL_PHONE_NUMBER);
        Assertion.assertThat("Assert if contact detail phone number is displayed and filled", isContactDetailPhoneNumberFilled, is(true));
        logger.log("Contact Detail Phone Number = '" + driverTools.getText(CONTACT_DETAIL_PHONE_NUMBER) + "'");

        driverTools.swipeToElement(GUEST_DETAIL_EDIT_BUTTON, Swipe.UP, 5);

        boolean isGuestDetailNameFilled = driverTools.isElementDisplayed(GUEST_DETAIL_NAME_CONTAINER_PREVIEW);
        Assertion.assertThat("Assert if guest detail name is displayed and filled", isGuestDetailNameFilled, is(true));
        logger.log("Guest Detail Name = '" + driverTools.getText(GUEST_DETAIL_NAME_CONTAINER_PREVIEW) + "'");

        return true;
    }

    @Override
    public String priceChangePopUpText() {
        WebElement priceChangePopup = driverTools.waitForElementToAppear(PRICE_CHANGES_POPUP).orElseThrow();
        return driverTools.getText(priceChangePopup);
    }

    @Override
    public void clickPriceChangeGoPayment() {
        driverTools.click(GO_PAYMENT_PRICE_CHANGES);
    }

    public String PriceChangesPopUp() {
        String methodName = "PriceChangesPopUp";
        long startTime = CoreFunctions.startTiming(methodName);
        CommonBookingFormPage.getInstance(session).waitProcessingBooking();
        try {
            String priceChanges = "";
            if (driverTools.waitForElementToPresence(PRICE_CHANGES_POPUP).isPresent()) {
                priceChanges = driverTools.getText(PRICE_CHANGES_DESCRIPTION).orElseThrow();
                String popupTitle = driverTools.getText(PRICE_CHANGES_POPUP).orElseThrow();
                String regex = "\\b[A-Z]{3} \\d{1,3}(?:[.,]\\d{3})*(?:[.,]\\d{2})?\\b|\\.|\\s";
                if(popupTitle.toLowerCase().contains("yay") || popupTitle.toLowerCase().contains("hore")){

                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(priceChanges);
                    ArrayList<String> matches = new ArrayList<>();
                    while (matcher.find()) {
                        matches.add(matcher.group());
                    }

                    String newPrice ="";
                    String oldPrice="";
                    if(popupTitle.toLowerCase().contains("yay")){
                        newPrice = matches.get(0);
                        oldPrice = matches.get(1);
                    } else{
                        newPrice = matches.get(1);
                        oldPrice = matches.get(0);
                    }
                    logger.log("Old Price: "+oldPrice);
                    logger.log("New Price: "+newPrice);
                    priceChanges = newPrice;
                    driverTools.click(GO_PAYMENT_PRICE_CHANGES);
                } else{
                    priceChanges = priceChanges.replace(",", " ");
                    logger.log("priceChanges -> " + priceChanges);
                    String language = "id";
                    if (priceChanges.contains("to ")) language = "en";
                    String[] priceChangesSplit = (language.equals("id")) ?  priceChanges.split("jadi") : priceChanges.split("to ");
                    logger.log("priceChangesSplit -> " + Arrays.toString(priceChangesSplit));

                    priceChanges = priceChangesSplit[1];
                    priceChanges = priceChanges.replace(",","");
                    priceChanges = priceChanges.replace(".","");
                    logger.log("priceChanges after replace -> " + priceChanges);

                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(priceChanges);

                    ArrayList<String> matches = new ArrayList<>();
                    while (matcher.find()) {
                        matches.add(matcher.group());
                        logger.log("matcher.group() -> " + matcher.group());
                    }
                    logger.log("matches.get(0) -> " + matches.get(0));
                    priceChanges = matches.get(0);

                    logger.log("priceChanges -> " + priceChanges);
                    driverTools.click(GO_PAYMENT_PRICE_CHANGES);
                }
            } else {
                if (driverTools.waitForElementToAppear(POLICY_CHANGES_POPUP).isPresent()) {
                    String policyChangeDesc = driverTools.getText(POLICY_CHANGES_DESCRIPTION).orElseThrow();
                    logger.log("policyChangeDesc -> " + policyChangeDesc);
                    driverTools.click(GO_PAYMENT_POLICY_CHANGES);
                }
                if (driverTools.isElementDisplayed(RATE_PLAN_BOTTOM_SHEET)) {
                    driverTools.click(RATE_PLAN_BOTTOM_SHEET);
                }
                if (driverTools.isElementDisplayed(RATE_PLAN_MULTI_CHANGES)) {
                    driverTools.click(RATE_PLAN_MULTI_CHANGES);
                }
            }
            return priceChanges;
        } finally {
            BookingFormPage<T> bookingFormPage = BookingFormPage.getInstance(session);
            bookingFormPage.closeRatePlanBottomSheet();
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void closeRatePlanBottomSheet(){
        String methodName = "closeRatePlanBottomSheet";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            if(driverTools.waitForElementToPresence(PaymentPageIOS.ORDER_ID,3).isPresent()){
                return;
            }
            driverTools.waitForElementToPresence(RATE_PLAN_BOTTOM_SHEET).ifPresent(driverTools::click);
        }finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public boolean isContactAndGuestDetailsChanged(String contactName, String contactPhoneNumber, String guestName) {
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

    private Locator randomSalutationLocator() {
        Locator[] salutationLocatorList = {CONTACT_MR, CONTACT_MRS, CONTACT_MS};
        return getRandomObject(salutationLocatorList);
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
        String checkInDate = driverTools.getText(CHECKIN_DATE).orElseThrow();
        logger.log("Check In Date Booking Form: " + checkInDate);
        return checkInDate;
    }

    @Override
    public String getCheckOutDateBookingForm() {
        WebElement checkInDateElement = driverTools.waitForElementToAppear(CHECKIN_DATE).orElseThrow();
        String[] dateRawArray = driverTools.getText(checkInDateElement).split(" ");
        if (dateRawArray[6].length() == 2) {
            String[] dateNumSplit = dateRawArray[6].split("");
            if(dateNumSplit[0].equals("0")){
                dateRawArray[6] = dateNumSplit[1];
            }
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
        driverTools.waitForElementToPresence(PRICE_BREAKDOWN);
        List<WebElement> expandButtons = driverTools.elements(PRICE_SUMMARY_EXPAND_BUTTON).orElseThrow();
        for (WebElement expandButton : expandButtons) {
            driverTools.clickElementByCoordinate(expandButton);
        }
        List<WebElement> prices = driverTools.elements(PRICE_BREAKDOWN).orElseThrow();
        ArrayList<Double> arrayPrice = new ArrayList<Double>();

        for (WebElement element : prices) {
            String text = driverTools.getText(element);
            try {
                double price = convertPrice(text);
                arrayPrice.add(price);
            } catch (NumberFormatException message) {
                logger.log(message.getMessage());
            }
        }
        logger.log("arrayPrice-> " + arrayPrice);

        double sum = 0;
        for (double i : arrayPrice) {
            sum += i;
        }

        String totalPriceBreakdown = driverTools.getValueAttribute(TOTAL_TO_PAY, "label");
        double totalPricePriceDetail = convertPrice(totalPriceBreakdown);
        double actualPrice = safeDivide(sum,1);
        double totalPriceFromBookingPage = convertPrice(totalPayment);
        logger.log("Total price from price detail:  " + totalPricePriceDetail);
        logger.log("Total price got:  " + actualPrice);
        logger.log("Total price from the booking page: " + totalPriceFromBookingPage);
        return totalPricePriceDetail == actualPrice && totalPriceFromBookingPage == actualPrice;
    }

    @Override
    public void closePriceDetail() {
        if(driverTools.isElementDisplayed(PRICE_DETAIL_CLOSE_BTN)){
            driverTools.click(PRICE_DETAIL_CLOSE_BTN);
        }else{
            driverTools.click(PRICE_DETAIL_CLOSE_BTN_RESCHEDULE);
        }
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
        String roomBed = driverTools.getText(ROOM_BED_TEXT).orElseThrow();
        logger.log("Room Bed Booking Form: " + roomBed);
        return roomBed;
    }

    @Override
    public String getCancellationBookingForm() {
        String cancellation = driverTools.getText(CANCELLATION_TEXT).orElseThrow();
        logger.log("Room Cancellation Booking Form: " + cancellation);
        return cancellation;
    }

    @Override
    public void openBookingDetailPage() {
        cancelRatePLanBottomSheetBookingPart1();
        driverTools.swipeToElement(ROOM_NAME_TEXT,Swipe.UP,2);
        driverTools.click(ROOM_NAME_TEXT);
    }

    @Override
    public boolean isBookingFormShown(boolean loginStatus) {
        cancelRatePLanBottomSheetBookingPart1();
        WebElement bookingSummaryContainer = driverTools.waitForElementToAppear(BOOKING_SUMMARY_CONTAINER, 10).orElseThrow();
        return driverTools.isElementDisplayed(bookingSummaryContainer);
    }

    @Override
    public void clickExtraFacilityAddOn() {
        driverTools.swipeToElement(EXTRA_FACILITY_ADD_BUTTON, Swipe.DOWN, 2).orElseThrow();
        driverTools.click(driverTools.onScreenElement(EXTRA_FACILITY_ADD_BUTTON));
        driverTools.swipeToElement(ROOM_ADD_ONS_CHECKBOX, Swipe.UP, 2).orElseThrow();
        driverTools.click(ROOM_ADD_ONS_CHECKBOX);
        driverTools.swipeToElement(ADD_ROOM_BUTTON, Swipe.UP, 2).orElseThrow();
        driverTools.click(ADD_ROOM_BUTTON); 
        driverTools.click(SAVE_BUTTON_V3);
    }

    private static Locator ADDON_SELECT_BUTTON(String addOnType) {
        String[] addOnTypeLang = splitLanguageData(addOnType, "-");
        return new Locator("ADDON_SELECT_BUTTON_" + addOnType.toUpperCase().replace("/",""), AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'" + addOnTypeLang[0] + "') or contains(@label,'" + addOnTypeLang[1] + "')]//following-sibling::*[@name='select_button']"));
    }

    private static Locator ADDON_CATEGORY_AFTER(String addOnCategoryName) {
        return new Locator("ADDON_CATEGORY_AFTER_" + addOnCategoryName.toUpperCase(), AppiumBy.xpath("//*[contains(@name,'add_ons')]//XCUIElementTypeStaticText[contains(@label,'" + addOnCategoryName + "')]//following::*[contains(@name,'add_ons')]//XCUIElementTypeStaticText"));
    }

    private static Locator ROOM_ADD_ONS_CHECKBOX(String roomName, int index) {
        return new Locator("ROOM_ADD_ONS_CHECKBOX_" + roomName.toUpperCase() + "_" + index, AppiumBy.xpath("(//*[@name='TVCell<TACMAddonsTitleCheckboxView>' and @label='" + roomName + "']//*[contains(@name,'checkbox')])[" + index + "]"));
    }

    private static Locator ROOM_ADD_ONS_CHECKBOX(String roomName) {
        return new Locator("ROOM_ADD_ONS_CHECKBOX_" + roomName.toUpperCase(), AppiumBy.xpath("//*[@name='TVCell<TACMAddonsTitleCheckboxView>' and @label='" + roomName + "']//*[contains(@name,'checkbox')]"));
    }

    private static Locator INCREMENT_BUTTON(String roomName, int index) {
        return new Locator("INCREMENT_BUTTON_" + roomName.toUpperCase() + "_" + index, AppiumBy.xpath("(//*[@name='TVCell<TACMAddonsTitleCheckboxView>' and @label='" + roomName + "'])[" + index + "]//following-sibling::*[@name='TVCell<TACMAddonsNightStepperView>']//*[@name='add_btn']"));
    }

    private static Locator INCREMENT_BUTTON(String roomName) {
        return new Locator("INCREMENT_BUTTON_" + roomName.toUpperCase(), AppiumBy.xpath("//*[@name='TVCell<TACMAddonsTitleCheckboxView>' and @label='" + roomName + "']//following-sibling::*[@name='TVCell<TACMAddonsNightStepperView>']//*[@name='add_btn']"));
    }

    private static Locator ROOM_NIGHT_ADD_ONS_CHECKBOX(String roomName, int index) {
        return new Locator("ROOM_NIGHT_ADD_ONS_CHECKBOX_" + roomName.toUpperCase(), AppiumBy.xpath("(//*[@name='TVAtomicCell<BoldLabel<Body1>>' and @label='" + roomName + "'])[" + index + "]//following-sibling::*[@name='TVCell<TACMAddonsTitleCheckboxView>']//*[contains(@name,'checkbox')]"));
    }

    private static Locator ROOM_NIGHT_ADD_ONS_CHECKBOX(String roomName) {
        return new Locator("ROOM_NIGHT_ADD_ONS_CHECKBOX_" + roomName.toUpperCase(), AppiumBy.xpath("//*[@name='TVAtomicCell<BoldLabel<Body1>>' and @label='" + roomName + "']//following-sibling::*[@name='TVCell<TACMAddonsTitleCheckboxView>']//*[contains(@name,'checkbox') and @visible='true']"));
    }

    private static Locator NIGHT_INCREMENT_BUTTON(String roomName, int index) {
        return new Locator("NIGHT_INCREMENT_BUTTON_" + roomName.toUpperCase(), AppiumBy.xpath("(//*[@name='TVAtomicCell<BoldLabel<Body1>>' and @label='" + roomName + "'])[" + index + "]//following-sibling::*[@name='TVCell<TACMAddonsNightStepperView>']//*[@name='add_btn']"));
    }

    private static Locator NIGHT_INCREMENT_BUTTON(String roomName) {
        return new Locator("NIGHT_INCREMENT_BUTTON_" + roomName.toUpperCase(), AppiumBy.xpath("//*[@name='TVAtomicCell<BoldLabel<Body1>>' and @label='" + roomName + "']//following-sibling::*[@name='TVCell<TACMAddonsNightStepperView>']//*[@name='add_btn' and @visible='true']"));
    }

    @Override
    public HashMap<Object, Object> clickExtraFacilityAddOn(int itemPerRoom, AddOnsType addOnsType, HashMap<Integer, String> selectedRoomName) {
        String addOnType = addOnsType.equals(AddOnsType.ITEM) ? "/item" : "/room-/kamar";

        int maxAttempt = 2;
        int i = 0;
        while (!driverTools.isElementDisplayed(ROOM_ADD_ONS_CHECKBOX) && i < maxAttempt) {
            selectItem(maxAttempt, ADDON_SELECT_BUTTON(addOnType), ADDON_CONTAINER);
            if (driverTools.isElementDisplayed(ROOM_ADD_ONS_CHECKBOX)) break;
            String selectedCategory = driverTools.getText(ADDON_CATEGORY_SELECTED).orElseThrow();
            selectItem(maxAttempt, ADDON_CATEGORY_AFTER(selectedCategory), ADDON_CATEGORY);
            i++;
        }

        String category = driverTools.getText(ADDON_CATEGORY_POPUP).orElseThrow();
        String name = driverTools.getText(ADDON_NAME_POPUP).orElseThrow();
        String singlePrice = driverTools.getText(ADDON_PRICE_POPUP).orElseThrow();

        int totalRoomType = selectedRoomName.size();
        logger.log("totalRoomType -> " + totalRoomType);

        int totalAddOnsItem = addItemOnEachRoom(addOnsType, itemPerRoom, selectedRoomName);
        double expectedTotalPrice = convertPrice(singlePrice) * totalAddOnsItem;
        String actualTotalPrice = driverTools.getText(ADDON_TOTAL_PRICE_POPUP).orElseThrow();
        Assertion.assertThat("user can add the add ons of each night of room type", convertPrice(actualTotalPrice), is(expectedTotalPrice));
        driverTools.click(SAVE_BUTTON_V3);

        HashMap<Object, Object> addOns = new HashMap<>();
        addOns.put(AddOnsType.NAME, name);
        addOns.put(AddOnsType.CATEGORY, category);
        addOns.put(AddOnsType.SINGLE_PRICE, singlePrice);
        addOns.put(AddOnsType.TOTAL_ROOM_TYPE, totalRoomType);
        addOns.put(AddOnsType.TOTAL_ADDONS_ITEM, totalAddOnsItem);
        addOns.put(AddOnsType.TOTAL_PRICE, actualTotalPrice);
        logger.log("addOns -> " + addOns);

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
            if (driverTools.isElementDisplayed(ADDON_INCREMENT_BUTTON)) driverTools.swipeByDistance(ADDON_INCREMENT_BUTTON, Swipe.UP, 1);
            driverTools.swipeToElement(incrementButtonLocator, Swipe.UP, 2).orElseThrow();
            driverTools.clickElementByCoordinate(incrementButtonLocator);
            totalAddOns++;
        }
        return totalAddOns;
    }

    private static Locator ADDONS_NAME_PRICE_BREAKDOWN(int roomTypeIndex) {
        return new Locator("ADDONS_NAME_PRICE_BREAKDOWN_TYPE_" + roomTypeIndex, AppiumBy.xpath("(//*[@label='Fasilitas Ekstra' or @label='Add-ons']//following-sibling::XCUIElementTypeStaticText[contains(@label,'(') and contains(@label,')')])[" + roomTypeIndex + "]"));
    }

    private static Locator ADDONS_PRICE_DETAILS_BREAKDOWN(int roomTypeIndex) {
        return new Locator("ADDONS_PRICE_DETAILS_BREAKDOWN_TYPE_" + roomTypeIndex, AppiumBy.xpath("(//*[@label='Fasilitas Ekstra' or @label='Add-ons']//following-sibling::XCUIElementTypeStaticText[contains(@label,'(') and contains(@label,')')])[" + roomTypeIndex + "]//preceding-sibling::XCUIElementTypeStaticText[1]"));
    }

    @Override
    public boolean checkAddOnPriceDetail(int totalRoomType, String addOnsName, String singlePrice, int itemPerRoom) {
        driverTools.waitForElementToAppear(ROOM_TYPE_PRICE_BREAKDOWN).orElseThrow();
        Assertion.assertThat("Total room type should be equal with the total selected on the room list", driverTools.elements(ROOM_TYPE_PRICE_BREAKDOWN).orElseThrow().size(), is(totalRoomType));
        for (int i = 1; i <= totalRoomType; i++) {
            WebElement addOns = driverTools.swipeToElement(ADDONS_NAME_PRICE_BREAKDOWN(i), Swipe.UP, 2).orElseThrow();
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
        return driverTools.getText(EXTRA_ADDON_PRICE).orElseThrow();
    }

    @Override
    public String getAddonName() {
        return driverTools.getText(ADDON_CATEGORY).orElseThrow();
    }

    @Override
    public String getAddonTitle() {
        return driverTools.getText(ADDON_NAME).orElseThrow();
    }

    private Locator getAddonPriceLocator(String addOnCategory) {
        return new Locator("ADDON_PRICE_PRICE_DETAIL", AppiumBy.xpath("//*[@name='leftText' and contains(@label,'" + addOnCategory + "')]//following-sibling::*[@name='rightText' and @label]"));
    }

    @Override
    public Boolean MatchAddOnPriceDetail(Double addOnPrice, String addOnCategory) {
        double addOnPriceDetail = convertPrice(driverTools.getText(getAddonPriceLocator(addOnCategory)).orElseThrow());
        logger.log("Addon Price on Booking Form = " + addOnPrice);
        logger.log("Addon Price on Price Detail = " + addOnPriceDetail);
        return (addOnPriceDetail == addOnPrice);
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
    public String getTotalPriceBookingFormPage2() {
        clickPayNowButton();
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

    @Override
    public String getTotalPricePayAtHotelBookingForm() {
        return driverTools.getText(PAY_AT_HOTEL_TOTAL_PRICE).orElseThrow();
    }

    @Override
    public String loyaltyBenefitValueText() {
        //clicking and opening price detail
        driverTools.waitForElementToAppear(TOTAL_PRICE_DETAIL_ARROW_BUTTON).orElseThrow();
        driverTools.click(TOTAL_PRICE_DETAIL_ARROW_BUTTON);
        logger.log("Price detail is opened");

        WebElement loyaltyBenefitDiscount = driverTools.waitForElementToPresence(LOYALTY_BENEFIT_DISCOUNT_LABEL).orElseThrow();
        String yValue = driverTools.getValueAttribute(loyaltyBenefitDiscount,"rect");

        logger.log("yValue: "+yValue);

        yValue = CoreFunctions.getDataFromJsonString(yValue,"y");

        logger.log("y: "+yValue);

        List<WebElement> otherPrices = driverTools.elements(OTHER_PRICE).orElseThrow();
        String discount ="";
        for(WebElement element : otherPrices){
            if(CoreFunctions.getDataFromJsonString(driverTools.getValueAttribute(element,"rect"),"y").equals(yValue)){
                discount=driverTools.getText(element);
                break;
            }
        }

        if(discount.equals("")){
            throw new NoSuchElementException("Discount not found");
        }

        discount = discount.replace(" ","");

        logger.log("discount -> " + discount);
        //closing price detail
        driverTools.click(CANCEL_BUTTON);
        logger.log("Price detail is closed");

        return discount;
    }

    @Override
    public boolean isLoggedIn() {
        return (driverTools.waitForElementToDisappear(EMAIL_CONTACT_V3, 5) || driverTools.waitForElementToDisappear(CONTACT_EMAIL, 5))
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
        Locator ADDON_CATEGORY_PRICE_DETAIL = new Locator("ADDON_CATEGORY_PRICE_DETAIL", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'"+addOnName+"') and contains(@label,'(x')]"));
        return getIntFromString(driverTools.getText(ADDON_CATEGORY_PRICE_DETAIL).orElseThrow()).orElseThrow();
    }
    
    @Override
    public String getEstimationRefund() {
        String estimatedRefund = driverTools.getText(REFUND_ESTIMATION).orElseThrow();
        logger.log("estimated refund :  " + estimatedRefund);
        return estimatedRefund;
    }

    @Override
    public List<String> getPriceDetailPrices(String totalPayment) {
        String currency = totalPayment.split(" ")[0];
        Locator PRICE_BREAKDOWN = new Locator("PRICE_BREAKDOWN", AppiumBy.xpath("//*[@name='leftText' and not(contains(@label,'check-in'))]//parent::*[@name='titleView']//following-sibling::*[@name='groupView']//*[@name='rightText' and contains(@label,'"+currency+"')]"));
        driverTools.swipeToElement(PRICE_BREAKDOWN, Swipe.DOWN, 4).orElseThrow();
        List<String> priceItems = new ArrayList<>(driverTools.getTexts(PRICE_BREAKDOWN));
        Assertion.assertThat("Prices exist", priceItems, is(not(empty())));
        priceItems.add(driverTools.getText(TOTAL_TO_PAY).orElseThrow());
        logger.log("prices: "+priceItems);
        return priceItems;
    }

    @Override
    public List<String> getPriceDetailPricesReschedule() {
        List<String> priceItems = new ArrayList<>(driverTools.getTexts(PRICE_BREAKDOWN_RESCHEDULE));
        Assertion.assertThat("Prices exist", priceItems, is(not(empty())));
        logger.log("prices: "+priceItems);
        return priceItems;
    }

    private static final Locator SPECIAL_REQUEST = new Locator("SPECIAL_REQUEST", AppiumBy.name("special_request"));
    private static final Locator ADD_OTHER_REQUEST = new Locator("ADD_OTHER_REQUEST", AppiumBy.name("add_other_request"));
    private static final Locator TOTAL_PRICE_TITLE = new Locator("TOTAL_PRICE_TITLE", AppiumBy.name("totalPriceTitleText"));
    private static final Locator GIFT_VOUCHER_CLAIM_BUTTON = new Locator("GIFT_VOUCHER_CLAIM_BUTTON",
            AppiumBy.xpath("//*[@name='TVCell<TSHCVoucherCardViewV2>']/XCUIElementTypeButton[@enabled='true']"));
    private static final Locator GIFT_VOUCHER_CLAIM_SNACKBAR = new Locator("GIFT_VOUCHER_CLAIM_SNACKBAR",
            AppiumBy.xpath("//*[@name='snackbarMessage' and (contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'vouchers will go to your voucher box') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'voucher akan masuk ke voucher box') or contains(translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'vouchers will go to your voucher box') or contains(translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'voucher akan masuk ke voucher box'))]"));
    private static final Locator CONTINUE_PAYMENT_BUTTON_PAGE2 = new Locator("CONTINUE_PAYMENT_BUTTON_PAGE2",
            AppiumBy.xpath("//*[@name='TVCell<TACMButtonViewComponent>']/XCUIElementTypeButton"));

    @Override
    public void fillSpecialRequest(String request) {
        driverTools.swipeToElement(SPECIAL_REQUEST, Swipe.UP, 5).orElseThrow();
        driverTools.click(SPECIAL_REQUEST);
        driverTools.waitForElementToAppear(ADD_OTHER_REQUEST).orElseThrow();
        driverTools.sendKeys(ADD_OTHER_REQUEST, request);
        driverTools.click(SAVE_BUTTON);
    }

    @Override
    public boolean isPayUponCheckInLabelAndPriceDisplayed() {
        String title = driverTools.getText(TOTAL_PRICE_TITLE).orElse("").toLowerCase();
        boolean labelOk = title.contains("pay upon check-in") || title.contains("bayar saat check-in");
        String price = driverTools.getText(TOTAL_PRICE).orElse("").trim();
        boolean priceOk = !price.isEmpty();
        logger.log("totalPriceTitleText='" + title + "', totalPriceText='" + price + "'");
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
        driverTools.swipeToElement(PAY_BY_BUTTON, Swipe.UP, 6).orElseThrow();
        String name = Optional.ofNullable(driverTools.getValueAttribute(PAY_BY_BUTTON, "name")).orElse("");
        logger.log("Pay by button name -> " + name);
        return name.toLowerCase().contains("selected");
    }

    @Override
    public void clickDownPaymentContentCloseButton() {
        driverTools.waitForElementToAppear(CLOSE_BUTTON).orElseThrow();
        driverTools.click(CLOSE_BUTTON);
        driverTools.waitForElementToDisappear(CLOSE_BUTTON, 10);
    }

    /** Continue to payment CTA, same button used by getContinueToPaymentButtonText(). */
    private Locator resolveContinueToPaymentButton() {
        if (driverTools.swipeToElement(CONTINUE_PAYMENT_BUTTON_PAGE2, Swipe.UP, 8).isEmpty()) {
            driverTools.swipeToElement(CONTINUE_TO_PAYMENT_CTA, Swipe.UP, 5);
        }
        return driverTools.waitForElementToPresence(CONTINUE_PAYMENT_BUTTON_PAGE2, 5).isPresent()
                ? CONTINUE_PAYMENT_BUTTON_PAGE2 : resolvePart2ContinueToPaymentCta();
    }

    @Override
    public void clickContinueToPaymentButton() {
        Locator cta = resolveContinueToPaymentButton();
        String text = getContinueToPaymentButtonText(cta);
        driverTools.click(cta);
        logger.log("Clicked continue to payment button -> " + cta);
        if (isPayZeroTodayText(text)) {
            logger.log("Pay IDR 0 today, confirm booking on the bottom sheet");
            driverTools.waitForElementToAppear(CONFIRM_BOOKING_CONTINUE_BUTTON, 10).orElseThrow();
            driverTools.click(CONFIRM_BOOKING_CONTINUE_BUTTON);
        }
    }

    @Override
    public String getContinueToPaymentButtonText() {
        return getContinueToPaymentButtonText(resolveContinueToPaymentButton());
    }

    private String getContinueToPaymentButtonText(Locator cta) {
        String text = driverTools.getText(cta).orElse("");
        if (text.isBlank()) {
            text = Optional.ofNullable(driverTools.getValueAttribute(cta, "label")).orElse("");
        }
        logger.log("Continue to payment button text -> " + text);
        return text;
    }

    @Override
    public boolean isPriceDetailRescheduleBookingFormCorrect(String totalPayment) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isPriceDetailRescheduleBookingFormCorrect'");
    }

    @Override
    public List<String> getPriceTitles() {
        getTotalToPayBookingForm();
        openSubtotal();
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
        List<WebElement> roomTabs = driverTools.elements(ROOM_TYPE_TAB).orElseThrow();
        int numberOfRooms = roomTabs.size();
        if(numberOfRooms == 0){
            numberOfRooms =1;
        }

        List<String> names = new ArrayList<>();
        logger.log("number of rooms: "+numberOfRooms);
        for(int j = 0; j < numberOfRooms; j++) {
            if(j>0){
                roomTabs = driverTools.elements(ROOM_TYPE_TAB).orElseThrow();
                driverTools.click(roomTabs.get(j));
            }

            Assertion.assertThat("Salutation checkbox not present",driverTools.isElementDisplayed(GUEST_DETAIL_MR),is(false));

            List<WebElement> nameFields = driverTools.elements(GUEST_DETAIL_NAME).orElseThrow();
            int numberOfGuest = nameFields.size();

            for (int i = 0; i < numberOfGuest; i++) {
                if(i>0){
                    nameFields = driverTools.elements(GUEST_DETAIL_NAME).orElseThrow();
                }
                driverTools.clear(nameFields.get(i));
                String name = randomName();
                names.add(name);
                driverTools.sendKeys(nameFields.get(i), name);
                driverTools.hideKeyboard();
                driverTools.swipeUp();
            }
        }
        driverTools.click(SAVE_BUTTON);
        List<String> namesBookingForm = driverTools.getTexts(GUEST_DETAIL_EDIT_BUTTON).stream()
                .map(name -> name.split(":")[1].trim())
                .collect(Collectors.toList());
        Assertion.assertThat("Names are inputted correctly",names,is(namesBookingForm));
        return true;
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

    @Override
    public void cancelProtection() {
        driverTools.swipeToElement(EXTRA_PROTECTION_CANCEL_BUTTON, Swipe.UP, 5).orElseThrow();
        driverTools.click(EXTRA_PROTECTION_CANCEL_BUTTON);
    }

    @Override
    public void addPromo(String promo){
        clickPayNowButton();
        driverTools.swipeToElement(CHECK_MORE_PROMO,Swipe.UP,3);
        driverTools.click(CHECK_MORE_PROMO);
        driverTools.sendKeys(PROMO_TEXT_INPUT,promo);
        driverTools.click(APPLY_PROMO_BUTTON);
    }

}
