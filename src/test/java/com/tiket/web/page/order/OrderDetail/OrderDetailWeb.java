package com.tiket.web.page.order.OrderDetail;

import com.mongodb.client.FindIterable;
import com.tiket.api.order.InternalOrderDetailApi;
import com.tiket.core.CoreFunctions;
import com.tiket.dbclient.TiketMongoDBManager;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.pojo.bus.BusSrpOneWayData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.security.auth.login.FailedLoginException;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.JavascriptExecutor;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
public abstract class OrderDetailWeb<T extends WebDriver> extends BasePage<T> {

    static HashMap<String,Boolean> sessionToCoachmark = new HashMap<String,Boolean>();
    public OrderDetailWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> OrderDetailWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new OrderDetailDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new OrderDetailMweb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }
    private static final Locator HIDE_BUTTON = new Locator("HIDE_BUTTON", By.xpath("//p[normalize-space()='Hide' or normalize-space()='Tutup']"));
    private static final Locator CONTINUE_TO_PAYMENT = new Locator("CONTINUE_TO_PAYMENT", new By.ByXPath("//span[contains(@class,'Button_truncation_not_applied') and (contains(text(), 'payment') or contains(text(), 'pembayaran'))]//parent::button"));
    private final static Locator OK_COACHMARK_BUTTON = new Locator("OK_COACHMARK_BUTTON", new By.ByXPath("//div[contains(@class,'coachmark')]//button[contains(text(),'OK') or contains(text(),'Oke')]"));
    private final static Locator SKIP_COACHMARK_BUTTON = new Locator("SKIP_COACHMARK_BUTTON", new By.ByXPath("//div[contains(@class,'coachmark')]//button[contains(text(),'Skip') or contains(text(),'Lewati')]"));
    private final static Locator DETAIL_RESERVATION_CARD = new Locator("DETAIL_RESERVATION_CARD", new By.ByXPath("//div[contains(@class,'DetailReservation_detail_reservation_card')]"));
    private static final Locator SEE_RESERVATION_DETAILS_BUTTON = new Locator("SEE_RESERVATION_DETAILS_BUTTON", By.xpath("//button[.//span[contains(normalize-space(.),'See reservation details') or contains(normalize-space(.),'Lihat detail reservasi')]]"));
    private static final Locator GUEST_NAME = new Locator("GUEST_NAME", By.xpath("//p[text()='Guest Details']/parent::div/following-sibling::ul/li/span"));
    private final static Locator CLOSE_MODAL_BUTTON = new Locator("CLOSE_MODAL_BUTTON", new By.ByXPath("//button[@data-testid='modal-close-button']"));
    private final static Locator ETICKET_MANAGE_ORDER_BUTTON = new Locator("ETICKET_MANAGE_ORDER_BUTTON", new By.ByXPath("//div[contains(@class,'ManageOrder_action')]//span[text()='E-ticket' or text()='E-tiket']"));
    private final static Locator SEE_ETICKET_FROM_HEADER_WEBVIEW = new Locator("SEE_ETICKET_FROM_HEADER_WEBVIEW", new By.ByXPath("//span[text()='Lihat' or text()='View']"));
    private final static Locator ORDER_ID_LOCATOR = new Locator("ORDER_ID_LOCATOR", new By.ByXPath("//p[contains(@class,'DetailReservation_order_id') or contains(@class,'styles_order_id')] | //span[contains (@class, 'styles_order_id')]"));
    private final static Locator ETICKET_CARD = new Locator("ETICKET_CARD", new By.ByXPath(
            "//*[contains(@class,'GenericETicket_ticket_card')]"
                    + " | //*[contains(@class,'ETicket_card')]"
                    + " | //*[contains(@class,'ETicket_wrapper')]"
                    + " | //*[contains(@class,'eticket_wrapper')]"));
    private final static Locator ROOM_DETAILS_BUTTON = new Locator("ROOM_DETAILS_BUTTON", new By.ByXPath("//*[contains(@class,'Button')]//*[contains(., 'Lihat detail kamar') or contains(., 'See room details')]"));
    private final static Locator ROOM_NAME_LOCATOR = new Locator("ROOM_NAME_LOCATOR", new By.ByXPath("//h3[@data-testid='hotel-title']"));
    private final static Locator HOTEL_NAME_LOCATOR = new Locator("HOTEL_NAME_LOCATOR", new By.ByXPath("//div[contains(@class,'Card')]//span[contains (@class, 'styles_title_text')]"));
    private final static Locator TOTAL_ROOM_LOCATOR = new Locator("TOTAL_ROOM_LOCATOR", new By.ByXPath("//p[@data-testid='hotel-title-subdes'][1]"));
    private final static Locator ROOM_TYPE_LABEL = new Locator("ROOM_TYPE_LABEL", new By.ByXPath("//div[contains(@class,'styles_room_label')]//div[contains(@class,'Label_truncation_lines')]"));
    private final static Locator TOTAL_NIGHT_LOCATOR = new Locator("TOTAL_NIGHT_LOCATOR", new By.ByXPath("//div[contains(@class,'styles_number_of_nights')]//p"));
    private final static Locator CALL_BUTTON = new Locator("CALL_BUTTON", new By.ByXPath("//span[text()='Call' or text()='Telepon']"));
    private final static Locator LOCATION_REDIRECTION_BUTTON = new Locator("LOCATION_REDIRECTION_BUTTON", new By.ByXPath("//div[contains(@class,'styles_action_location')]"));
    private final static Locator REFUND_RESCHEDULE_INFO_SECTION = new Locator("REFUND_RESCHEDULE_INFO_SECTION", new By.ByXPath(
            "//div[contains(@class,'RefundRescheduleInfo_container')]"
                    + " | //div[contains(@class,'RefundRescheduleInfo')][.//button[contains(@class,'RefundRescheduleInfo_button')]]"));
    private final static Locator REFUND_RESCHEDULE_INFO_BUTTON = new Locator("REFUND_RESCHEDULE_INFO_BUTTON", new By.ByXPath("//div[contains(@class,'RefundRescheduleInfo')]//span[text()='Details' or text()='Detail']"));
    private final static Locator REFUND_RESCHEDULE_INFO_HOTEL_MULTI_ROOM = new Locator("REFUND_RESCHEDULE_INFO_HOTEL_MULTI_ROOM", new By.ByXPath("//div[contains(@class,'RefundRescheduleInfo_container')]"));
    protected static final Locator REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON = new Locator("REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON", new By.ByXPath("//button[contains(@class,'RefundRescheduleInfo_button') and (.//span[contains(.,'Cancel order')] or .//span[contains(.,'Batalkan pesanan')])]"));
    private static final Locator REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON_CONTAINER = new Locator("REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON_CONTAINER", new By.ByXPath(
            "//div[contains(@class,'RefundRescheduleInfo_cancel_button')]//button[contains(@class,'RefundRescheduleInfo_button') and (.//span[contains(.,'Cancel order')] or .//span[contains(.,'Batalkan pesanan')])]"));
    private static final Locator CANCEL_ORDER_MODAL = new Locator("CANCEL_ORDER_MODAL", new By.ByXPath(
            "//section[contains(@class,'Modal_modal_popup')][.//div[contains(@class,'RefundRescheduleInfo_bottomsheet_wrapper')]]"));
    private static final Locator CANCEL_ORDER_MODAL_CHECKBOX_LABEL = new Locator("CANCEL_ORDER_MODAL_CHECKBOX_LABEL", new By.ByXPath(
            "//section[contains(@class,'Modal_modal_popup')]//div[contains(@class,'RefundRescheduleInfo_checkbox_points_wrapper')]//label[contains(@class,'Check_check') and .//input[contains(@class,'Check_checkbox')]]"));
    private static final Locator CANCEL_ORDER_MODAL_AGREE_BUTTON = new Locator("CANCEL_ORDER_MODAL_AGREE_BUTTON", new By.ByXPath(
            "//section[contains(@class,'Modal_modal_popup')]//div[contains(@class,'RefundRescheduleInfo_buttons_wrapper')]//button[contains(@class,'Button_variant_secondary') and .//span[contains(text(),'Agree') or contains(text(),'Setuju')]]"));
    private static final Locator CANCEL_ORDER_AGREEMENT_CHECKBOX = new Locator("CANCEL_ORDER_AGREEMENT_CHECKBOX", new By.ByXPath(
            "//input[contains(@class,'Check_checkbox') and @type='checkbox']"));
    private static final Locator CANCEL_ORDER_MODAL_AGREEMENT_LABEL = new Locator("CANCEL_ORDER_MODAL_AGREEMENT_LABEL", new By.ByXPath(
            "//*[contains(@class,'Modal') or contains(@class,'BottomSheet')]//label[.//input[contains(@class,'Check_checkbox') and @type='checkbox']]"));
    private static final Locator CANCEL_ORDER_AGREE_BUTTON = new Locator("CANCEL_ORDER_AGREE_BUTTON", new By.ByXPath(
            "(//*[contains(@class,'Modal') or contains(@class,'BottomSheet')]//button[contains(@class,'Button_accessibility_button')]//span[contains(text(),'Agree') or contains(text(),'Setuju')])[last()]"
                    + " | (//button[contains(@class,'Button_accessibility_button')]//span[contains(text(),'Agree') or contains(text(),'Setuju')])[last()]"));
    private static final Locator CANCEL_ORDER_MODAL_CANCEL_CONFIRM_BUTTON = new Locator("CANCEL_ORDER_MODAL_CANCEL_CONFIRM_BUTTON", new By.ByXPath(
            "//*[contains(@class,'Modal') or contains(@class,'BottomSheet')]//button[contains(@class,'RefundRescheduleInfo_button') and (.//span[contains(.,'Cancel order')] or .//span[contains(.,'Batalkan pesanan')])]"));
    protected static final Locator CANCEL_ORDER_BUTTON_POPUP = new Locator("CANCEL_ORDER_BUTTON_POPUP", new By.ByXPath("//button[contains(@class,'CancelFooter_cancel_button') and contains(@class,'Button_variant_danger')] | (//*[contains(@class,'Modal') or contains(@class,'BottomSheet')]//*[contains(@class,'RefundRescheduleInfo_buttons_wrapper')]/button)[last()]"));
    public static final Locator CANCEL_ORDER_CONFIRMATION = new Locator("CANCEL_ORDER_CONFIRMATION", new By.ByXPath("//*[@data-testid='cancel-footer']/button"));
    public static final Locator CANCEL_ORDER_CONFIRMATION_MODAL = new Locator("CANCEL_ORDER_CONFIRMATION_MODAL", new By.ByXPath("//*[contains(@class,'Dialog_body')]//button[contains(.,'Cancel') or contains(.,'Batal')]"));
    protected static final Locator CANCEL_ORDER_BUTTON_POPUP_CONFIRMATION = new Locator("CANCEL_ORDER_BUTTON_POPUP_CONFIRMATION", new By.ByXPath("//div[contains(@class,'Dialog_body')]//button[contains(@class,'Button_variant_secondary')] | //*[contains(@class,'Modal') or contains(@class,'BottomSheet')]//*[contains(@class,'RefundRescheduleInfo_button_list')]/button"));
    private static final Locator ORDER_DETAIL_CANCELED_BANNER_TITLE = new Locator("ORDER_DETAIL_CANCELED_BANNER_TITLE", new By.ByXPath("//p[contains(@class,'NotificationBanner_title') and (contains(.,'You have canceled this order') or contains(.,'Kamu membatalkan pesanan ini') or contains(.,'This order has been canceled') or contains(.,'Pesanan ini telah dibatalkan'))]"));
    private static final Locator OTHER_CANCEL_ORDER_CONFIRMATION_BUTTON = new Locator("OTHER_CANCEL_ORDER_CONFIRMATION_BUTTON", new By.ByXPath("(//*[contains(@class,'RefundRescheduleInfo_button_list')]/button)[1]"));
    public static final Locator DETAIL_RESERVATION_DELETE_ORDER_BUTTON = new Locator("DETAIL_RESERVATION_DELETE_ORDER_BUTTON", new By.ByXPath("//*[contains(@class,'Button_accessibility_button')]/*[contains(.,'Delete order') or contains(.,'Hapus pesanan') or contains(.,'Hapus')]"));
    private static final Locator DELETE_ORDER_CONFIRMATION_BUTTON = new Locator("DELETE_ORDER_CONFIRMATION_BUTTON", new By.ByXPath("//button[normalize-space()='Hapus' or normalize-space()='Delete']"));
    public static final Locator MAKE_SIMILAR_ORDER_BUTTON = new Locator("MAKE_SIMILAR_ORDER_BUTTON", new By.ByXPath("//div[contains(@class,'DetailReservation_cancel_order_buttons')]//button[.//span[contains(.,'Make similar order') or contains(.,'Make Similar Order') or contains(.,'Buat pesanan serupa') or contains(.,'Buat Pesanan Serupa')]] | //button[.//span[contains(.,'Make similar order') or contains(.,'Make Similar Order') or contains(.,'Buat pesanan serupa') or contains(.,'Buat Pesanan Serupa')]]"));
    private final static Locator REFUND_RESCHEDULE_INFO_PAGE = new Locator("REFUND_RESCHEDULE_INFO_PAGE", new By.ByXPath("//div[contains(@class,'styles_iframe_container')]//p[contains(@class,'styles_title')]"));
    private final static Locator REFUND_RESCHEDULE_BACK_BUTTON = new Locator("REFUND_RESCHEDULE_BACK_BUTTON", new By.ByXPath("//div[contains(@class,'detail_wrapper')]//span[text()='Back' or text()='Kembali']"));
    private final static Locator REFUND_RESCHEDULE_IFRAME = new Locator("REFUND_RESCHEDULE_IFRAME", new By.ByXPath("//div[contains(@class,'styles_iframe_container')]//iframe"));
    private final static Locator REFUND_NOT_SUPPORTED_TEXT = new Locator("REFUND_NOT_SUPPORTED_TEXT", new By.ByXPath("//*[contains(text(),'Refund not supported') or contains(text(),'Refund tidak didukung') or contains(text(),'not available') or contains(text(),'tidak tersedia') or contains(text(),'No refund') or contains(text(),'Tidak bisa refund')]"));
    public final static Locator HIDE_BANNER_BUTTON = new Locator("HIDE_BANNER_BUTTON", new By.ByXPath("//*[@id='page-module-collapse-inner']//*[contains(@class,'styles_curve_button')]"));
    private final static Locator LPG_BANNER = new Locator("LPG_BANNER", new By.ByXPath("//div[contains(@style,'https://s-light.tiket.photos/t/01E25EBZS3W0FY9GTG6C42E1SE')]/parent::div"));
    private final static Locator MANAGE_ORDER_SECTION = new Locator("MANAGE_ORDER_SECTION", new By.ByXPath("//div[contains(@class,'ManageOrder_wrapper')] | //div[contains(@class,'ManageOrder_action_buttons_wrapper')]"));
    private final static Locator REFUND_MANAGE_ORDER_BUTTON = new Locator("REFUND_MANAGE_ORDER_BUTTON", new By.ByXPath("//div[contains(@class,'ManageOrder')]//span[text()='Refund'] | //div[contains(@class,'ActionButton_action_title')]//span[text()='Refund']"));
    private final static Locator RESCHEDULE_MANAGE_ORDER_BUTTON = new Locator("RESCHEDULE_MANAGE_ORDER_BUTTON", new By.ByXPath("//div[contains(@class,'ManageOrder')]//span[text()='Reschedule']"));
    private final static Locator EDIT_GUEST_BUTTON = new Locator("EDIT_GUEST_BUTTON", new By.ByXPath("//div[contains(@class,'ManageOrder')]//span[text()='Ubah Pemesanan' or text()='Edit Booking']"));
    public final static Locator PAGE_MODULE_SECTION = new Locator("PAGE_MODULE_SECTION", new By.ByXPath("(//div[contains(@class,'ExtPageModuleSection_page_module_layout_container')])[1]"));
    private final static Locator CAR_DETAIL_CAR_TYPE = new Locator("CAR_DETAIL_CAR_TYPE", new By.ByXPath("//div[contains(@class,'styles_description')]//span[contains(@class, 'Text_weight_bold')]"));
    private final static Locator PICKUP_DROPOFF_PLACE = new Locator("PICKUP_DROPOFF_PLACE", new By.ByXPath("//div[contains(@class,'styles_trip_summary_id')]//p[contains(@class,'Text_weight_bold')]"));
   private final static Locator CAR_RENTAL_RESERVATION = new Locator("CAR_RENTAL_RESERVATION", new By.ByXPath("//div[contains(@class, 'styles_trip_summary')]//span[contains(@class, 'Text_size_b2')]"));
    private final static Locator CAR_RENTAL_DURATION = new Locator("CAR_RENTAL_DURATION", new By.ByXPath("//*[contains (@class, 'styles_note')]"));
    private final static Locator FACILITIES_SECTION = new Locator("FACILITIES_SECTION", new By.ByXPath("//div[contains(@class,'DetailReservation_detail_reservation_card')]//span[text()='Fasilitas' or text()='Facilities']"));
    private final static Locator VENDOR_SECTION = new Locator("VENDOR_SECTION", new By.ByXPath("//div[contains(@class,'ContactPartner_wrapper')]"));
    private final static Locator CONTACT_DETAILS_SECTION = new Locator("CONTACT_DETAILS_SECTION", new By.ByXPath("//div[contains(@class,'styles_contact_detail_wrapper')]"));
    private final static Locator CONTACT_DETAILS_DATA = new Locator("CONTACT_DETAILS_DATA", new By.ByXPath("//div[contains(@class,'styles_contact_detail_wrapper')]/child::div//span"));
    public final static Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", new By.ByXPath("//div[contains(@class,'TotalAmount_total_amount_and_label')]//span[contains(@class,'Text_weight_bold')] | //div[contains(@class,'TotalAmount_info_data')]/following-sibling::span"));
    private final static Locator DETAIL_RESERVATION_DETAIL_BUTTON = new Locator("DETAIL_RESERVATION_DETAIL_BUTTON", new By.ByXPath("//span[contains(@class,'DetailReservation_details_button')]"));
    private final static Locator DEPARTURE_ARRIVAL_NAME = new Locator("DEPARTURE_ARRIVAL_NAME", new By.ByXPath("//div[contains(@class,'DetailReservation_flight_subtitle')]"));
    private final static Locator DEPARTURE_ARRIVAL_DURATION = new Locator("DEPARTURE_ARRIVAL_DURATION", new By.ByXPath("//div[contains(@class,'DetailReservation_flight_duration')]"));
    private final static Locator TRAIN_NAME_CLASS_MODAL = new Locator("TRAIN_NAME_CLASS_MODAL", new By.ByXPath("//div[contains(@class,'DetailReservation_airline_info_wrapper')]"));
    private final static Locator PASSENGER_FACILITIES_SECTION = new Locator("PASSENGER_FACILITIES_SECTION", new By.ByXPath("//div[contains(@class,'Passenger_container')]"));
    private final static Locator PASSENGER_FACILITIES_LIST = new Locator("PASSENGER_FACILITIES_LIST", new By.ByXPath("//div[contains(@class,'passenger_list')]"));
    private final static Locator TTD_DATE_AND_NAME = new Locator("TTD_DATE_AND_NAME", new By.ByXPath("//div[contains(@class, 'DetailReservation_card')]//div[contains(@class, 'DetailReservation_text_wrapper')]"));
    public final static Locator CONTACT_DETAIL_RESERVATION_DATA = new Locator("CONTACT_DETAIL_RESERVATION_DATA", new By.ByXPath("//div[contains(@class, 'DetailReservation_extra_details')]//p[contains(@class, 'highEmphasis')]"));
    private final static Locator FERRY_BOOKING_CODE = new Locator("FERRY_BOOKING_CODE", new By.ByXPath("//div[contains(@class, 'ETicket_qr_title_code')]//span[contains(@class, 'highEmphasis')]"));
    private final static Locator FERRY_RESERVATION_CITY = new Locator("FERRY_RESERVATION_CITY", new By.ByXPath("//div[contains(@class, 'DetailReservation_route')]//*[contains(@class, 'Text_text')]"));
    // Both the e-ticket "heading" and the "View" action live inside the
    // green issuance banner that appears at the top of the order-detail
    // page once the order is ETICKET_ISSUED. The legacy locators anchored
    // on CSS-Module class names like `ETicket_generated_ticket` /
    // `Text_variant_positive` / `Text_variant_activity`, which the latest
    // dWeb redesign no longer emits. We therefore use a union XPath:
    //   1) the legacy class-anchored path (kept so older environments and
    //      train e-tickets still match), OR
    //   2) a text-anchored path that finds the "Your e-ticket is here" /
    //      "E-tiketmu di sini" heading in any container, then drills back
    //      down to the corresponding "View" / "Lihat" action element
    //      inside the closest common ancestor.
    private final static Locator YOUR_E_TICKET_IS_HERE_SPAN = new Locator(
            "YOUR_E_TICKET_IS_HERE_SPAN",
            By.xpath(
                    "//div[contains(@class,'ETicket_generated_ticket') "
                            + "or contains(@class,'styles_train_eticket')]"
                            + "/span[contains(@class,'Text_variant_positive')]"
                            + "|"
                            + "//*[normalize-space(text())='Your e-ticket is here' "
                            + "or normalize-space(text())='E-tiketmu di sini']"));
    private final static Locator VIEW_E_TICKET_BUTTON = new Locator(
            "VIEW_E_TICKET_BUTTON",
            By.xpath(
                    "//div[contains(@class,'ETicket_generated_ticket') "
                            + "or contains(@class,'styles_train_eticket')]"
                            + "/span[contains(@class,'Text_variant_activity')]"
                            + "|"
                            + "//*[normalize-space(text())='Your e-ticket is here' "
                            + "or normalize-space(text())='E-tiketmu di sini']"
                            + "/ancestor::div"
                            + "[descendant::*[normalize-space(text())='View' "
                            + "or normalize-space(text())='Lihat']][1]"
                            + "//*[normalize-space(text())='View' "
                            + "or normalize-space(text())='Lihat']"));
    private final static Locator QR_CODE = new Locator("QR_CODE", new By.ByXPath("(//*[contains(@class, 'CardBoardingPass_ticket_img')]//img)[1]"));
    private final static Locator QR_CODE_ALL = new Locator("QR_CODE_ALL", new By.ByXPath("//*[contains(@class, 'CardBoardingPass_ticket_img')]//img"));
    private final static Locator SEE_QR_CODE_BUTTON = new Locator("SEE_QR_CODE_BUTTON", new By.ByXPath("//button[contains(@class,'SingleButton_button')]"));
    private final static Locator CHANGE_TRAIN_SEAT_BUTTON = new Locator("CHANGE_TRAIN_SEAT_BUTTON", new By.ByXPath("//div[contains(@class,'ActionCard_action_card_wrapper')][.//span[text()='Change Passenger Seat' or text()='Ubah Kursi Penumpang']]"));
    public final static Locator MANAGE_ORDER_BUTTONS = new Locator("MANAGE_ORDER_BUTTONS", new By.ByXPath("//*[contains(@class, 'ManageOrder_action_buttons_wrapper')]"));
    public final static Locator E_TICKET_BUTTON = new Locator("E_TICKET_BUTTON", new By.ByXPath("//*[contains(@class, 'ManageOrder')]//*[normalize-space()='E-ticket' or normalize-space()='E-tiket']"));
    // The e-ticket "select language" UI is rendered as TWO modals in the DOM:
    //   * the mobile/responsive `BottomSheet` (data-testid="bottom-sheet-body")
    //   * the desktop `BaseModal`/`Modal_modal_popup`.
    // Only one is visible at any time depending on the viewport size, but both
    // share the inner `.ETicket_language_container` / `.ETicket_lang_title`
    // markup, so we anchor locators on those structural classes rather than on
    // a specific modal container.
    private final static Locator E_TICKET_POPUP = new Locator("E_TICKET_POPUP", new By.ByXPath(
            "//*[@data-testid='bottom-sheet-body' "
                    + "or contains(@class,'Modal_modal_popup') "
                    + "or contains(@class,'BaseModal_modal')]"));
    // Same dual-modal caveat as the language selectors below: both the mobile
    // BottomSheet and the desktop BaseModal render their own close button in
    // the DOM. The desktop modal is rendered AFTER the mobile one, so
    // `[last()]` resolves to the visible (clickable) close button on desktop
    // while still matching the only close button on mobile.
    private final static Locator E_TICKET_POPUP_CLOSE_BUTTON = new Locator("E_TICKET_POPUP_CLOSE_BUTTON", By.xpath(
            "(//button[contains(@class,'BottomSheet_close_button') "
                    + "or @data-testid='modal-close-button'])[last()]"));
    // Each language option is a `.ETicket_language_container` whose first
    // child `<span class='ETicket_lang_title…'>` carries the language name.
    // The language name itself ("English" / "Bahasa Indonesia") is the same
    // brand-string in every locale, so it's safe to disambiguate the two
    // options by the title text within the structural anchor.
    //
    // The container can be any of three CSS-Module-hashed classes depending
    // on which design-system primitive the modal is built on:
    //   * `bottom-sheet-body` data-testid - the legacy mobile BottomSheet
    //   * `Modal_modal_popup`              - the dWeb popup variant
    //   * `BaseModal_modal`                - the newer shared modal primitive
    // The mobile and desktop containers can co-exist in the DOM (only one
    // is CSS-visible). The desktop container is always rendered LAST, so
    // `[last()]` picks the visible option on desktop while still matching
    // the only option on mobile.
    private static final String E_TICKET_POPUP_CONTAINER_XPATH =
            "//*[@data-testid='bottom-sheet-body' "
                    + "or contains(@class,'Modal_modal_popup') "
                    + "or contains(@class,'BaseModal_modal')]";
    private final static Locator E_TICKET_POPUP_ENGLISH_SELECTION = new Locator("E_TICKET_POPUP_ENGLISH_SELECTION", By.xpath(
            "(" + E_TICKET_POPUP_CONTAINER_XPATH
                    + "//div[contains(@class,'ETicket_language_container')]"
                    + "[.//span[contains(@class,'ETicket_lang_title') and normalize-space()='English']])[last()]"));
    private final static Locator E_TICKET_POPUP_BAHASA_SELECTION = new Locator("E_TICKET_POPUP_BAHASA_SELECTION", By.xpath(
            "(" + E_TICKET_POPUP_CONTAINER_XPATH
                    + "//div[contains(@class,'ETicket_language_container')]"
                    + "[.//span[contains(@class,'ETicket_lang_title') and normalize-space()='Bahasa Indonesia']])[last()]"));
    public final static Locator RECEIPT_BUTTON = new Locator("RECEIPT_BUTTON", new By.ByXPath("//*[contains(@class, 'ManageOrder')]//*[text()='Receipt' or text()='Bukti Pembayaran']"));
    private static final Locator INSURANCE_CLAIM_CENTER_BUTTON = new Locator("INSURANCE_CLAIM_CENTER_BUTTON", By.xpath("//*[contains(@class,'SingleButton_button')]"));
    private static final Locator INSURANCE_PRODUCT_NAME = new Locator("INSURANCE_PRODUCT_NAME", By.xpath("//*[contains(@class,'ProductSummary_text_wrapper')]/*[1]"));
    private static final Locator SEE_POLICY_AND_COVERAGE_BUTTON = new Locator("SEE_POLICY_AND_COVERAGE_BUTTON", By.xpath("//*[contains(@class,'GenericReservationDetails_inner_container')]/button"));
    private static final Locator RELATED_ORDER_VERTICAL_NAME = new Locator("RELATED_ORDER_VERTICAL_NAME", By.xpath("//*[contains(@class,'VerticalCard_header_wrapper')]/*[contains(@class,'text')]"));
    private static final Locator RELATED_ORDER_ITEM = new Locator("RELATED_ORDER_ITEM", By.xpath("//*[@data-testid='order-item-container']"));
    private static final Locator ADDONS_POPUP = new Locator("ADDONS_POPUP", By.xpath("//*[contains(@class,'AddOns_container_popup')]"));
    private static final Locator PAGE_MODULE_VIEWS = new Locator("PAGE_MODULE_VIEWS", By.xpath("//*[contains(@data-testid,'pagemodule-view')]"));
    private static final Locator LOADING_INDICATOR = new Locator("LOADING_INDICATOR", By.xpath("//*[@data-testid='loading-indicator']"));
    private final static Locator ORDER_CHECK_IN_TIME = new Locator("ORDER_CHECK_IN_TIME", new By.ByXPath("(//*[contains(@class, 'DetailReservation_date_wrapper')])[1]"));
    private final static Locator ORDER_FERRY_CLASS = new Locator("ORDER_FERRY_CLASS", new By.ByXPath("//*[contains(@class, 'DetailReservation_vendor_ferry_details')]//*[contains(@class, 'Text_weight_bold')]"));
    private final static Locator ORDER_DEPARTURE_PORT = new Locator("ORDER_DEPARTURE_PORT", new By.ByXPath("(//*[contains(@class, 'ProgressStepVerticalDots_title')]//div[contains(@class, 'Text_weight_bold')])[1]"));
    private final static Locator ORDER_DESTINATION_PORT = new Locator("ORDER_DESTINATION_PORT", new By.ByXPath("(//*[contains(@class, 'ProgressStepVerticalDots_title')]//div[contains(@class, 'Text_weight_bold')])[2]"));
    private final static Locator ORDER_TOTAL_PASSENGER = new Locator("ORDER_TOTAL_PASSENGER", new By.ByXPath("//*[contains(@class, 'VPassenger_passenger_names')][last()]//*[contains(@class, 'VPassenger_facility_numbering')]"));
    private final static Locator WAITING_FOR_CONFIRMATION = new Locator("WAITING_FOR_CONFIRMATION", new By.ByXPath("(//*[contains(@class, 'WaitingStatus_margin')])"));
    private final static Locator AIRPORT_LOCATION_NAME_ORDER_DETAIL_PAGE = new Locator("AIRPORT_LOCATION_NAME_ORDER_DETAIL_PAGE", new By.ByXPath("(//div[contains(@class, 'ProgressStepVerticalDots_content')]//p)[3]"));
    private final static Locator DESTINATION_LOCATION_NAME_ORDER_DETAIL_PAGE = new Locator("DESTINATION_LOCATION_NAME_ORDER_DETAIL_PAGE", new By.ByXPath("(//div[contains(@class, 'ProgressStepVerticalDots_content')]//p)[1]"));
    private final static Locator RESCHEDULE_CARD = new Locator("RESCHEDULE_CARD", new By.ByXPath("//*[contains(@class,'NotificationBanner')]"));
    private final static Locator RESCHEDULE_CARD_BUTTON = new Locator("RESCHEDULE_CARD_BUTTON", new By.ByXPath("//*[contains(@class,'NotificationBanner_action_button')]"));
    private final static Locator NEW_SCHEDULE_CARD_BUTTON = new Locator("NEW_SCHEDULE_CARD_BUTTON", new By.ByXPath("//*[contains(@class,'variant_activity') and (text()='New schedule' or text()='Jadwal baru')]//parent::*[contains(@class,'styles_title')]"));
    private final static Locator CHECK_IN_OUT_DATE = new Locator("CHECK_IN_OUT_DATE", new By.ByXPath("//*[contains(@class,'check_in_out_value') and contains(@class,'b2')]"));
    private final static Locator FLIGHT_AIRLINE_NAME = new Locator("FLIGHT_AIRLINE_NAME", new By.ByXPath("//div[contains (@class, 'DetailReservation_flight_info')]//span[contains (@class, 'highEmphasis')]"));
    private final static Locator ORDER_BUS_OPERATOR_CLASS = new Locator("ORDER_BUS_OPERATOR_CLASS", new By.ByXPath("//*[contains(@class, 'DetailReservation_bus')]//*[contains(@class, 'Text_variant_highEmphasis')]"));
    private final static Locator ORDER_BUS_ORIGIN = new Locator("ORDER_BUS_ORIGIN", new By.ByXPath("(//*[contains(@class, 'DetailReservation_flight_subtitle') and contains(@class, 'Text_variant_highEmphasis')])[1]"));
    private final static Locator ORDER_BUS_DESTINATION = new Locator("ORDER_BUS_DESTINATION", new By.ByXPath("(//*[contains(@class, 'DetailReservation_flight_subtitle') and contains(@class, 'Text_variant_highEmphasis')])[2]"));
    public final static Locator PRICE_DETAIL_BUTTON = new Locator("PRICE_DETAIL_BUTTON", By.xpath("//div[contains (@class,'TotalAmount_price')]/span/following-sibling::*"));
    public final static Locator PRICE_DETAIL_FLIGHT_LOCATOR = new Locator("PRICE_DETAIL_FLIGHT_LOCATOR", By.xpath("//div[contains(@class,'TotalAmount_price')]//*[@data-testid='icon-chevron-down']"));
    public final static Locator HUNDREDREFUND_PRICE_BREAKDOWN = new Locator("HUNDREDREFUND_PRICE_BREAKDOWN", new By.ByXPath("//div[contains (@class, 'styles_price_types_wrapper')]//span[contains (@class, 'styles_margin_left')]"));
    private final static Locator HUNDREDREFUND_IMG_MANAGE_ORDER = new Locator("HUNDREDREFUND_IMG_MANAGE_ORDER", new By.ByXPath("//img[@alt='Refund']"));
    private final static Locator BUS_BOOKING_CODE = new Locator("BUS_BOOKING_CODE", new By.ByXPath("//div[contains (@class, 'styles_id_copy')]//span[contains (@class, 'highEmphasis')]"));
    private final static Locator TRAIN_BOOKING_CODE = new Locator("TRAIN_BOOKING_CODE", new By.ByXPath("//div[contains(@class, 'styles_booking_code_section')]//p[contains(@class, 'Text_weight_bold')]"));
    private final static Locator BUS_RESERVATION_DETAIL = new Locator("BUS_RESERVATION_DETAIL", new By.ByXPath("//div[contains (@class, 'DetailReservation_flight_subtitle')]"));
    private final static Locator EDIT_GUEST_POPUP = new Locator("EDIT_GUEST_BUTTON", new By.ByXPath("(//ul[contains(@class, 'EditBookingPopup')]//span[contains(@class,'edit_button')])[1]"));
    private final static Locator EDIT_SPECIAL_REQ_POPUP = new Locator("EDIT_SPECIAL_REQ_BUTTON", new By.ByXPath("(//ul[contains(@class, 'EditBookingPopup')]//span[contains(@class,'edit_button')])[2]"));
    private final static Locator SPECIAL_REQ_TEXT = new Locator("SPECIAL_REQ_TEXT", new By.ByXPath("(//*[(contains(text(),'Others') or contains(text(),'Lainnya'))])[1]"));
    private final static Locator ADD_ON_TEXT = new Locator("ADD_ON_TEXT", new By.ByXPath("//span[(normalize-space()='Add-ons')or(normalize-space()='Fasilitas Ekstra') ]/ancestor::div[contains(@class,'styles_content_section_wrapper')]//li[1]"));
    private static final Locator COUNTDOWN = new Locator("COUNTDOWN", new By.ByXPath("//*[contains(@class,'Countdown_time')]"));
    private static final Locator FLIGHT_DEPARTURE_DATE= new Locator("FLIGHT_DEPARTURE_DATE", new By.ByXPath("(//div[contains(@class,'styles_content_list')])[1]//span"));
    private static final Locator FLIGHT_RETURN_DATE= new Locator("FLIGHT_RETURN_DATE", new By.ByXPath("(//div[contains(@class,'styles_content_list')])[3]//span"));
    private static final Locator FLIGHT_ORDER_GROUP_DEPART_AIRLINE = new Locator("FLIGHT_ORDER_GROUP_DEPART_AIRLINE", new By.ByXPath("(//div[contains(@class, 'styles_content_list') and not(*[contains(text(), ',')])]//span)[1]"));
    private static final Locator FLIGHT_ORDER_GROUP_RETURN_AIRLINE = new Locator("FLIGHT_ORDER_GROUP_RETURN_AIRLINE", new By.ByXPath("(//div[contains(@class, 'styles_content_list') and not(*[contains(text(), ',')])]//span)[6]"));
    private static final Locator DEPART_AIRPORT_NAME_ORDER_GROUP = new Locator("DEPART_AIRPORT_NAME_ORDER_GROUP", new By.ByXPath("(//*[contains(@class,'group_card')]//*[contains(@class,'content_item')])[1]//div[3]//span[3]"));
    private static final Locator RETURN_AIRPORT_NAME_ORDER_GROUP = new Locator("RETURN_AIRPORT_NAME_ORDER_GROUP", new By.ByXPath("(//*[contains(@class,'group_card')]//*[contains(@class,'content_item')])[2]//div[3]//span[3]"));
    private static final Locator FLIGHT_ORDER_GROUP_TOTAL_PAYMENT = new Locator("FLIGHT_ORDER_GROUP_TOTAL_PAYMENT", new By.ByXPath("//*[contains(@class,'PaymentFooter_price')]//span"));
    private static final Locator FLIGHT_ORDER_GROUP_PAYMENT_DETAIL_CHEVRON = new Locator("FLIGHT_ORDER_GROUP_PAYMENT_DETAIL_CHEVRON", new By.ByXPath("//*[contains(@class,'PaymentFooter_price')]//*[name()='svg']"));
    private static final Locator PRICE_DETAIL_COMPONENTS = new Locator("PRICE_DETAIL_COMPONENTS",new By.ByXPath("//div[contains(@class,'price_flex')]//following-sibling::*"));
    private static final Locator PRICE_DETAIL_COMPONENTS_DISCOUNT_AND_OTHER_FEES = new Locator("PRICE_DETAIL_COMPONENTS_DISCOUNT_AND_OTHER_FEES",new By.ByXPath("//*[contains(@class,'price_text')]"));
    private static final Locator PRICE_DETAIL_TOTAL_PRICE_VALUE = new Locator("PRICE_DETAIL_TOTAL_PRICE_VALUE",new By.ByXPath("(//span[contains(text(),'Total')]//following-sibling::*)[2]"));
    private static final Locator CLOSE_PRICE_DETAIL_WEBVIEW = new Locator("CLOSE_PRICE_DETAIL_BUS", new By.ByXPath("//button[contains(@class,'Modal_modal_close')]"));
    private static final Locator FLIGHT_ORDER_GROUP_DEPART_CHEVRON = new Locator("FLIGHT_ORDER_GROUP_DEPART_CHEVRON", new By.ByXPath("//*[contains(@class,'styles_action_header_container') and *[(contains(text(),'Pergi') or contains(text(),'Depart'))]]"));
    private static final Locator FLIGHT_DEPARTURE_DATE_DETAIL= new Locator("FLIGHT_DEPARTURE_DATE_DETAIL", new By.ByXPath("(//div[contains(@class,'DetailReservation_flight_subtitle')])[1]//preceding-sibling::*"));
    private static final Locator FLIGHT_ARRIVAL_DATE_DETAIL= new Locator("FLIGHT_ARRIVAL_DATE_DETAIL", new By.ByXPath("(//div[contains(@class,'DetailReservation_flight_subtitle')])[2]//preceding-sibling::*"));
    private static final Locator DEPART_AIRPORT_NAME_ORDER_DETAIL= new Locator("DEPART_AIRPORT_NAME", new By.ByXPath("(//div[contains(@class,'DetailReservation_flight_subtitle')])[1]"));
    private static final Locator RETURN_AIRPORT_NAME_ORDER_DETAIL= new Locator("RETURN_AIRPORT_NAME", new By.ByXPath("(//div[contains(@class,'DetailReservation_flight_subtitle')])[2]"));
    private static final Locator BACK_BUTTON= new Locator("BACK_BUTTON", new By.ByXPath("//span[contains(@class,'detail_icon_back')]"));
    private static final Locator FLIGHT_ORDER_GROUP_RETURN_CHEVRON = new Locator("FLIGHT_ORDER_GROUP_RETURN_CHEVRON", new By.ByXPath("//*[contains(@class,'styles_action_header_container') and *[(contains(text(),'Pulang') or contains(text(),'Return'))]]"));
    private static final Locator TOTAL_PAYMENT_PRICE_TOGGLE = new Locator("TOTAL_PAYMENT_PRICE_TOGGLE", new By.ByXPath("//div[contains(@class, 'TotalAmount_price')]//*[@data-testid='icon-chevron-down']"));
    private static final Locator COMPLETE_THE_PAYMENT_BUTTON = new Locator("COMPLETE_THE_PAYMENT_BUTTON", new By.ByXPath("//button[.//span[normalize-space()='Complete the payment']]"));
    private static final Locator COMPLETE_PAYMENT_FROM_ORDER_GROUP = new Locator("COMPLETE_PAYMENT_FROM_ORDER_GROUP", new By.ByXPath("//*[contains(@class,'PaymentFooter')]//button"));
    private static final Locator ORDER_ID_LOCATOR_FLIGHT = new Locator("ORDER_ID_LOCATOR_FLIGHT", new By.ByXPath("(//span[contains(@class,'OrderCard')])[1]")) ;
    private static final Locator CHOOSE_ROOM_HOTEL_MULTI_ORDER = new Locator("CHOOSE_ROOM_HOTEL_MULTI_ORDER", By.xpath("//span[contains(@class,'Radio_icon')]"));
    private static final Locator CONTINUE_REFUND_MULTI_ORDER = new Locator("CONTINUE_REFUND_MULTI_ORDER", By.xpath("//div[contains (@class, 'ChooseMultiOrderItem_action_button')]//button"));
    private static final Locator POP_UP_REFUND_RESCHEDULE = new Locator("POP_UP_REFUND_RESCHEDULE", By.xpath("//div[contains (@class, 'Dialog_pop_up')]"));
    private static final Locator POP_UP_REFUND_RESCHEDULE_BUTTON = new Locator("POP_UP_REFUND_RESCHEDULE_BUTTON", By.xpath("//div[contains (@class, 'Dialog_pop_up')]//button"));
    private static final Locator CAR_PICKUP_LOCATION = new Locator("CAR_PICKUP_LOCATION", By.xpath("(//div[contains(@class, 'styles_text_wrapper')]//span[not(contains(@class, 'style'))])[1]"));
    private static final Locator CAR_DROP_OFF_LOCATION = new Locator("CAR_DROP_OFF_LOCATION", By.xpath("(//div[contains(@class, 'styles_text_wrapper')]//span[not(contains(@class, 'style'))])[2]"));
    private static final Locator DEPART_WHOOSH_NAME = new Locator("DEPART_WHOOSH_NAME", By.xpath("//span[contains(@class, 'Text_text__') and contains(text(), 'Whoosh')]"));
    private static final Locator DEPARTURE_ORIGIN_STATION = new Locator("DEPARTURE_ORIGIN_STATION", By.xpath("(//div[contains(@class, 'DetailReservation_flight_subtitle')])[1]"));
    private static final Locator DEPARTURE_DESTINATION_STATION = new Locator("DEPARTURE_DESTINATION_STATION", By.xpath("(//div[contains(@class, 'DetailReservation_flight_subtitle')])[2]"));
    public static final Locator DEPARTURE_TRAIN = new Locator("DEPARTURE_TRAIN", AppiumBy.xpath("(//div[contains(@class, 'Text_weight_bold') and contains(text(), ':')])[1]"));
    public static final Locator ARRIVAL_TRAIN = new Locator("ARRIVAL_TRAIN", AppiumBy.xpath("(//div[contains(@class, 'Text_weight_bold') and contains(text(), ':')])[2]"));
    public static final Locator PASSENGER_NAME = new Locator("PASSENGER_NAME", AppiumBy.xpath("(//div[contains(@class, 'styles_train_detail_passengers')]//span[contains(@class, 'Text_weight_bold')])[1]"));
    public final static Locator CONTACT_DETAIL_RESERVATION_BUTTON = new Locator("CONTACT_DETAIL_RESERVATION_BUTTON", new By.ByXPath("//span[contains(@class, 'DetailReservation_action_button')]"));
    private final static Locator SUMMARY_DETAIL_BUTTON = new Locator("SUMMARY_DETAIL_BUTTON", new By.ByXPath("//div[contains(@class,'DetailReservation_detail_reservation_card')]//div[contains(@class,'styles_action')]//span"));
    private final static Locator PICKUP_DROPOFF_TITLE_MODAL = new Locator("PICKUP_DROP_TITLE_MODAL", new By.ByXPath("//div[contains(@class,'styles_progress_step_contents')]//p[contains(@class,'Text_size_b2')]"));
    private final static Locator PICKUP_DROPOFF_TEXT_MODAL = new Locator("PICKUP_DROP_TEXT_MODAL", new By.ByXPath("//div[contains(@class,'styles_additional_content_popup_wrapper')]//p[contains(@class,'Text_size_b3')]"));
    public final static Locator COMPLETE_THE_PAYMENT = new Locator("COMPLETE_THE_PAYMENT", new By.ByXPath("(//button[contains(@class, 'Button_variant_primary')])[1]"));
    private final static Locator REDEEM_YOUR_E_TICKET_BUTTON = new Locator("REDEEM_YOUR_E_TICKET_BUTTON", By.xpath("//button[contains(@class, 'ETicket_redeem_button')]"));
    public final static Locator SEE_REDEMPTION_STATUS_BUTTON = new Locator("SEE_REDEMPTION_STATUS_BUTTON", new By.ByXPath("//div[contains(@class,'ETicket_redeem_buttons')]/button[contains(@class,'secondary')]"));
    public final static Locator PASSENGER_DETAIL_BTN = new Locator("PASSENGER_DETAIL_BTN", new By.ByXPath("//span[@data-testid='open-bottomsheet' and text()='Details']"));
    public final static Locator DEP_DATE = new Locator("DEP_DATE", new By.ByXPath("(//div[contains(@class, 'Text_variant_highEmphasis') and contains(@class, 'Text_weight_bold') and contains(@class, 'Text_size_b2')])[1]"));
    public final static Locator TRAIN_BAR_CODE = new Locator("TRAIN_BAR_CODE", new By.ByXPath("(//img[contains(@class, 'styles_barcode')])"));
    public final static Locator ORDER_DETAIL_WRAPPER = new Locator("ORDER_DETAIL_WRAPPER", new By.ByXPath("//div[contains(@class, 'order_detail_wrapper')]"));
    public final static Locator FINAL_PRICE = new Locator("FINAL_PRICE", new By.ByXPath("//div[contains(@class, 'PaymentFooter')]//span[contains(@class , 'highEmphasis')]"));
    private static final Locator SHARE_ETICKET_BUTTON = new Locator("SHARE_ETICKET_BUTTON", By.xpath("//div[contains(@class,'styles_wrapper')]//*[name()='svg']"));
    private static final Locator EMAIL_ADDRESS_FIELD = new Locator("EMAIL_ADDRESS_FIELD", By.xpath("//input[@type='email']"));
    private static final Locator SHARE_NOW_BUTTON = new Locator("SHARE_NOW_BUTTON", By.xpath("//button[contains(@class,'ticket_share_button')]"));
    private static final Locator SNACKBAR_SHARE_ETICKET = new Locator("SNACKBAR_SHARE_ETICKET", By.xpath("//div[contains(@class,'Snackbar_text_content')]"));
    private static final Locator EDIT_BOOKING_BUTTON = new Locator("EDIT_BOOKING_BUTTON", By.xpath("(//*[contains(@class,'ActionButton_action_title_')])[3]"));
    private static final Locator CLICK_ON_EDIT_GUEST_BUTTON = new Locator("CLICK_ON_EDIT_GUEST_BUTTON", By.xpath("(//li//span[contains(@class,'EditBookingPopup_edit_button')])[1]"));
    private static final Locator CLICK_ON_EDIT_GUEST_BUTTON_2 = new Locator("CLICK_ON_EDIT_GUEST_BUTTON_2", By.xpath("(//li//span[contains(@class,'EditBookingPopup_edit_button')])[2]"));
    private static final Locator CLICK_ON_PENCIL_ICON = new Locator("CLICK_ON_PENCIL_ICON", By.xpath("(//*[contains(@class,'RoomTypeCard_icon')])[1]"));
    private static final Locator CLICK_ON_PENCIL_ICON_2 = new Locator("CLICK_ON_PENCIL_ICON", By.xpath("(//*[contains(@class,'RoomTypeCard_icon')])[2]"));
    private static final Locator CLICK_INPUT_TO_REVISE_NAME = new Locator("CLICK_INPUT_TO_REVISE_NAME", By.xpath("(//input[contains(@id,'full-name-according-to-id')])"));
    private static final Locator CLICK_INPUT_TO_REVISE_SPECIAL_REQUEST = new Locator("CLICK_INPUT_TO_REVISE_SPECIAL_REQUEST", By.xpath("(//textarea[contains(@id,'add-other-requests')])"));
    private static final Locator SAVE_AFTER_REVISE = new Locator("SAVE_AFTER_REVISE", By.xpath("(//button[contains(@class,'Button_button_')])"));
    private static final Locator CONFIRM_AFTER_SAVE_REVISE = new Locator("CONFIRM_AFTER_SAVE_REVISE", By.xpath("((//div[contains(@class,'Figure_footer')])//button)[2]"));
    private static final Locator OTP_TEXTFIELD1 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("(//input[contains(@class, 'otp_input')])[1]"));
    private static final Locator OTP_TEXTFIELD2 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("(//input[contains(@class, 'otp_input')])[2]"));
    private static final Locator OTP_TEXTFIELD3 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("(//input[contains(@class, 'otp_input')])[3]"));
    private static final Locator OTP_TEXTFIELD4 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("(//input[contains(@class, 'otp_input')])[4]"));
    private static final Locator SUCCESSFULLY_REVISED = new Locator("SUCCESSFULLY_REVISED", new By.ByXPath("(//*[contains(@class, 'SuccessReviseFeedback_image_wrapper')])"));
    private static final Locator ORDER_DETAIL_PACKAGE_NAME = new Locator("ORDER_DETAIL_PACKAGE_NAME", AppiumBy.xpath("//*[contains(@class,'DetailReservation_header_wrapper')]/span[contains(@class,'Text_text')]"));
    private static final Locator SPECIAL_REQUEST = new Locator("SPECIAL_REQUEST", new By.ByXPath("//*[contains(@class,'reservation_detail_list')]//*[text()='Special Request' or text()='Permintaan Khusus']/parent::*/following-sibling::*/*/span"));
    private static final Locator ROOM_GUEST_COUNT = new Locator("ROOM_GUEST_COUNT", AppiumBy.xpath("//span[(contains(text(), 'guest') and contains(text(), 'adult')) or (contains(text(), 'tamu') and contains(text(), 'dewasa'))]"));
    private static final Locator ORDER_UPDATE_SECTION = new Locator("ORDER_UPDATE_SECTION", By.xpath("//div[contains(@class, 'styles_update_view__')]"));
    private static final Locator ORDER_UPDATE_TITLE = new Locator("ORDER_UPDATE_TITLE", By.xpath("//span[contains(@class,'styles_title_text')]"));
    private static final Locator ORDER_UPDATE_STATUS = new Locator("ORDER_UPDATE_STATUS", By.xpath("//span[contains(@class, 'styles_section_status__')]"));
    private static final Locator ORDER_UPDATE_SEE_DETAILS = new Locator("ORDER_UPDATE_SEE_DETAILS", By.xpath("//span[contains(@class, 'styles_title_button__')]"));
    private static final Locator ORDER_UPDATE_ROOM_DESCRIPTION = new Locator("ORDER_UPDATE_ROOM_DESCRIPTION", By.xpath("//div[contains(@class, 'styles_update_view__')]//div[contains(@class, 'styles_descriptions_wrapper')]//ul/li[2]//span"));
    private final static Locator TAB_YOUR_ITINERARIES = new Locator("TAB_YOUR_ITINERARIES", By.xpath("//button[.//span[contains(@class,'SegmentedControl_label') and (text()='Your Itineraries' or text()='Itinerari Kamu')]]"));
    private final static Locator TAB_ACTIVE_ORDERS = new Locator("TAB_ACTIVE_ORDERS", By.xpath("//button[.//span[contains(@class,'SegmentedControl_label') and (text()='Active Orders' or text()='Pesanan Aktif')]]"));
    private final static Locator TRIP_HEADER_HEADING = new Locator("TRIP_HEADER_HEADING", By.xpath("//*[@data-testid='itinerary-header-heading']"));
    private final static Locator TRIP_ORDER_CARD = new Locator("TRIP_ORDER_CARD", By.xpath("//*[@data-testid='trip-order-card']"));
    private final static Locator TTD_E_VOUCHER_TITLE = new Locator("TTD_E_VOUCHER_TITLE", By.xpath("//div[contains(@class,'EVoucherATF_product_title')]"));
    private final static Locator E_TICKET_LANGUAGE_ENGLISH = new Locator("E_TICKET_LANGUAGE_ENGLISH", By.xpath("//p[contains(@class,'List_title') and text()='English']"));
    private final static Locator E_TICKET_BACK_BUTTON = new Locator("E_TICKET_BACK_BUTTON", By.xpath("//div[@data-testid='desktop-headline-button'] | //span[contains(@class,'Headline_icon_back')]"));
    private final static Locator CROSS_SELL_BANNER = new Locator("CROSS_SELL_BANNER", By.xpath("//*[@data-testid='trip-banner-card']")); // Low Profile
    private final static Locator HIGH_PROFILE_CROSS_SELL = new Locator("HIGH_PROFILE_CROSS_SELL", By.xpath("//*[@data-testid='trip-page-module']")); // High Profile
    private final static Locator SEE_ALL_ORDERS_BUTTON = new Locator("SEE_ALL_ORDERS_BUTTON", By.xpath("//button[@data-testid='trip-list-button']"));
    private final static Locator REFUND_NOT_AVAILABLE_POPUP = new Locator("REFUND_NOT_AVAILABLE_POPUP", new By.ByXPath("//div[contains(@class,'Dialog_pop_up')]"));
    private final static Locator REFUND_NOT_AVAILABLE_TITLE = new Locator("REFUND_NOT_AVAILABLE_TITLE", new By.ByXPath("//h2[contains(@class,'Figure_title') and (contains(text(),'Refund is not available') or contains(text(),'Refund tidak tersedia'))]"));
    private final static Locator REFUND_NOT_AVAILABLE_DESCRIPTION = new Locator("REFUND_NOT_AVAILABLE_DESCRIPTION", new By.ByXPath("//p[contains(@class,'Figure_description') and (contains(text(),'Refund not allowed') or contains(text(),'Tidak bisa refund'))]"));
    private final static Locator REFUND_NOT_AVAILABLE_RESCHEDULE_BUTTON = new Locator("REFUND_NOT_AVAILABLE_RESCHEDULE_BUTTON", new By.ByXPath("//div[contains(@class,'Dialog_pop_up')]//button[contains(@class,'Button_variant_primary')]//span[text()='Reschedule' or text()='Jadwal ulang']"));
    private final static Locator REFUND_NOT_AVAILABLE_BACK_BUTTON = new Locator("REFUND_NOT_AVAILABLE_BACK_BUTTON", new By.ByXPath("//div[contains(@class,'Dialog_pop_up')]//button[contains(@class,'Button_variant_secondary')]//span[text()='Back' or text()='Kembali']"));
    private final static Locator ORDER_DETAIL_ROOM_NAME = new Locator("ORDER_DETAIL_ROOM_NAME", new By.ByXPath("//span[contains(@class, 'Text_variant_highEmphasis') and contains(@class, 'Text_size_b2') and contains(text(), ' x ')]"));

    private final static Locator MANAGE_ORDER = new Locator("MANAGE_ORDER", new By.ByXPath("//button[.//span[normalize-space()='Manage Order' or normalize-space()='Atur Pesanan']]"));
    private final static Locator MANAGE_ORDER_FLOATING_BTN = new Locator("MANAGE_ORDER_FLOATING_BTN", new By.ByXPath("//button[contains(@class,'FloatingButton') and .//span[normalize-space()='Manage Order' or normalize-space()='Atur Pesanan']]"));

    private final static Locator BOOK_AGAIN_OR_REFUND_BUTTON = new Locator("BOOK_AGAIN_OR_REFUND_BUTTON", By.xpath("//button[contains(@class, 'AlertInfo_action_button')]"));
    private final static Locator HIDE_EXCLUSION_TRAVEL_DEAL_BANNER = new Locator("HIDE_EXCLUSION_TRAVEL_DEAL_BANNER", By.xpath("//div[contains(@class,'styles_collapse_content_fixed')]//div[contains(@class,'styles_container')]//button/div[contains(@class,'styles_text_container')]//p"));

    public String getOrderDetailRoomName() {
        WebElement roomNameElement = driverTools.waitForElementToAppear(ORDER_DETAIL_ROOM_NAME).orElseThrow();
        String fullText = driverTools.getText(roomNameElement);
        // Remove the "1 x " or "2 x " prefix to get just the room name
        return fullText.replaceFirst("^\\d+\\s*x\\s*", "").trim();
    }

    public String getPassengerName(){
        WebElement passengerForm = driverTools.waitForElementToAppear(PASSENGER_NAME).orElseThrow();
        String fullName = passengerForm.getText().trim();
        String nameOnly = fullName.replaceAll("(?i)^\\s*\\d*\\.?\\s*(mr|mrs|ms|tuan|nyonya|nona)\\.?\\s+", "").trim();
        return nameOnly;
    }
    private Locator getETicketElement(String language) {
        return new Locator("ETICKET_LANGUAGE", By.xpath(String.format("//div[contains(@class, 'List_center_side')]//p[contains(text(), '%s')]", language)));
    }

    private static Locator getHotelNameLocator(String hotelName) {
        return new Locator("HOTEL_NAME_LOCATOR",
                new By.ByXPath("//span[contains(@class, 'styles_title_text') and text()='" + hotelName + "']"));
    }

    private static Locator getRoomNameLocator(String roomName) {
        return new Locator("HOTEL_NAME_LOCATOR", new By.ByXPath(String.format("//*[contains(@class,'styles_detail_list')]//*[contains(text(),'%s')]", roomName)));
    }

    public boolean isRoomNameDisplayed(String roomName) {
        return driverTools.waitForElementToPresence(getRoomNameLocator(roomName)).isPresent();
    }

    public JSONObject getInternalOrderDetail(String orderID) throws InterruptedException, ExecutionException {
        InternalOrderDetailApi.Inputs input = new InternalOrderDetailApi.Inputs(orderID);
        InternalOrderDetailApi internalOrderDetailApi = new InternalOrderDetailApi(input);
        ApiResponse response = internalOrderDetailApi.hitApi(apiTools);
        logger.log("response: " + response.getBody());
        return new JSONObject(response.getBody());
    }

    public Map<String, String> getVerticalData(String orderId, String orderDetailId) throws InterruptedException, ExecutionException {
        JSONArray orderDetails = getInternalOrderDetail(orderId).getJSONObject("data").getJSONArray("orderDetails");
        for (int i = 0; i < orderDetails.length(); i++) {
            JSONObject detail = orderDetails.getJSONObject(i);
            if (orderDetailId.equals(String.valueOf(detail.get("orderDetailId")))) {
                Map<String, String> dataMap = new HashMap<>();
                JSONObject verticalData = detail.getJSONObject("vertical");
                Iterator<String> keys = verticalData.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    Object value = verticalData.get(key);
                    dataMap.put(key, value instanceof String ? (String) value : String.valueOf(value));
                }
                logger.log("vertical of orderDetailId " + orderDetailId + " -> " + dataMap);
                return dataMap;
            }
        }
        Assertion.fail("Order " + orderId + " has no orderDetailId " + orderDetailId);
        return Map.of();
    }

    public Map<String, String> getVerticalData(String orderId) throws InterruptedException, ExecutionException {
        JSONObject verticalData = getInternalOrderDetail(orderId)
                .getJSONObject("data")
                .getJSONArray("orderDetails")
                .getJSONObject(0)
                .getJSONObject("vertical");

        Map<String, String> dataMap = new HashMap<>();
        Iterator<String> keys = verticalData.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = verticalData.get(key);

            if (value instanceof JSONObject) {
                dataMap.put(key, value.toString());
            } else if (value instanceof JSONArray) {
                dataMap.put(key, value.toString());
            } else if (value instanceof String) {
                dataMap.put(key, (String) value);
            } else {
                dataMap.put(key, String.valueOf(value));
            }
        }
        logger.log("dataMap -> " + dataMap);
        return dataMap;
    }

    public void continueToPayment() {
        driverTools.waitForElementToPresence(CONTINUE_TO_PAYMENT);
        driverTools.click(CONTINUE_TO_PAYMENT);
    }

    public JSONObject getVerticalDataObject(String orderId) throws JSONException, InterruptedException, ExecutionException {
        return getInternalOrderDetail(orderId)
                .getJSONObject("data")
                .getJSONArray("orderDetails")
                .getJSONObject(0)
                .getJSONObject("vertical");
    }

    public String getTotalAmount(String orderId) throws ExecutionException, InterruptedException {
        try {
            return String.valueOf(getInternalOrderDetail(orderId)
                    .getJSONObject("data")
                    .getInt("totalAmount"));
        } catch (JSONException e) {
            return String.valueOf(getInternalOrderDetail(orderId)
                    .getJSONObject("data")
                    .getInt("price"));
        }
    }

    public String getTotalAmountMultiCurrency(String orderId) throws ExecutionException, InterruptedException {
        try {
            return String.valueOf(getInternalOrderDetail(orderId)
                    .getJSONObject("data")
                    .getFloat("totalAmount"));
        } catch (JSONException e) {
            return String.valueOf(getInternalOrderDetail(orderId)
                    .getJSONObject("data")
                    .getFloat("price"));
        }
    }

    public String getCustomerCurrencyData(String orderId) throws ExecutionException, InterruptedException {
        return String.valueOf(getInternalOrderDetail(orderId)
                .getJSONObject("data")
                .getString("customerCurrency"));
    }

    public String getCarType(String orderId) throws ExecutionException, InterruptedException {
        String fleet = getVerticalData(orderId).get("fleet");
        JSONObject fleetJson = new JSONObject(fleet);
        return fleetJson.getString("fleetName");
    }

    public String getSalutation(String orderId) throws ExecutionException, InterruptedException {
        String salutation = getInternalOrderDetail(orderId)
                .getJSONObject("data")
                .getJSONArray("travelers")
                .getJSONObject(0)
                .getString("salutation");
        return salutation;
    }

    public String getGuestName(String orderId) throws ExecutionException, InterruptedException {
        String guestName = getInternalOrderDetail(orderId)
                .getJSONObject("data")
                .getJSONArray("travelers")
                .getJSONObject(0)
                .getString("fullName");
        return guestName;
    }

    public String getOrderStatus(String orderId) throws ExecutionException, InterruptedException {
        String orderStatus = getInternalOrderDetail(orderId)
                .getJSONObject("data")
                .getString("orderStatus");
        return orderStatus;
    }

    public List<String> getAllGuestNames(String orderId) throws ExecutionException, InterruptedException {
        List<String> guestNames = new ArrayList<>();
        org.json.JSONArray travelers = getInternalOrderDetail(orderId)
                .getJSONObject("data")
                .getJSONArray("travelers");
        for (int i = 0; i < travelers.length(); i++) {
            String guestName = travelers.getJSONObject(i).getString("fullName");
            guestNames.add(guestName);
        }
        return guestNames;
    }

    public String getContactName(String orderId) throws ExecutionException, InterruptedException {
        String contactName = getInternalOrderDetail(orderId)
                .getJSONObject("data")
                .getJSONObject("contact")
                .getString("contactName");
        return contactName;
    }

    public String getPhoneNumber(String orderId) throws ExecutionException, InterruptedException {
        String phoneNumber = getInternalOrderDetail(orderId)
                .getJSONObject("data")
                .getJSONObject("contact")
                .getString("phoneNumber");
        return phoneNumber;
    }

    public String getContactEmail(String orderId) throws ExecutionException, InterruptedException {
        String contactEmail = getInternalOrderDetail(orderId)
                .getJSONObject("data")
                .getJSONObject("contact")
                .getString("email");
        return contactEmail;
    }

    public String constructTrainOrigin(String orderId)
            throws ExecutionException, InterruptedException {
        return getVerticalData(orderId).get("departureStation") + " (" + getVerticalData(orderId).get("departureStationCode") + ") - " + getVerticalData(orderId).get("departureStationCityName");
    }

    public String constructTrainDestination(String orderId)
            throws ExecutionException, InterruptedException {
        return getVerticalData(orderId).get("arrivalStation") + " (" + getVerticalData(orderId).get("arrivalStationCode") + ") - " + getVerticalData(orderId).get("arrivalStationCityName");
    }

    /**
     * Dismisses the exclusive travel deals overlay ("Hide" / "Tutup") when it is covering the order detail page.
     */
    private void hideTravelDealBannerIfPresent() {
        if (driverTools.waitForElementToBeClickable(HIDE_BUTTON, 3).isPresent()) {
            driverTools.click(HIDE_BUTTON);
            driverTools.waitForElementToDisappear(HIDE_BUTTON, 3);
        }
    }

    public void skipCoachmark() {
        driverTools.waitForElementToPresence(HIDE_EXCLUSION_TRAVEL_DEAL_BANNER, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
        if(sessionToCoachmark.get(session.sessionId()) != null && sessionToCoachmark.get(session.sessionId())) {
            return;
        }
        Optional<WebElement> skipButton = driverTools.waitForElementToAppear(SKIP_COACHMARK_BUTTON,3);
        Optional<WebElement> okButton = driverTools.waitForElementToAppear(OK_COACHMARK_BUTTON,3);
        skipButton.ifPresent(element -> {
            if (driverTools.element(SKIP_COACHMARK_BUTTON).isPresent()) {
                driverTools.clickWithJavascriptExecutor(SKIP_COACHMARK_BUTTON);
                driverTools.waitForElementToDisappear(SKIP_COACHMARK_BUTTON);
                sessionToCoachmark.put(session.sessionId(), true);
            }
        });
        okButton.ifPresent(element -> {
            if (driverTools.element(OK_COACHMARK_BUTTON).isPresent()) {
                driverTools.clickWithJavascriptExecutor(OK_COACHMARK_BUTTON);
                driverTools.waitForElementToDisappear(OK_COACHMARK_BUTTON);
                sessionToCoachmark.put(session.sessionId(), true);
            } else {
                logger.log("No coachmark button spotted");
            }
        });
        driverTools.refreshWebpage();
        hideTravelDealBannerIfPresent();
    }

    public void clickEticketFromHeader() {
        driverTools.waitForElementToPresence(HIDE_BUTTON, 2).ifPresent(driverTools::click);
        WebElement eTicketHeaderButton = driverTools.waitForElementToAppear(SEE_ETICKET_FROM_HEADER_WEBVIEW).orElseThrow();
        driverTools.click(eTicketHeaderButton);
    }

    public boolean checkETicketLanguage(String language) {
        WebElement eTicketLanguageButton = driverTools.waitForElementToAppear(getETicketElement(language)).orElseThrow();
        return driverTools.isElementDisplayed(eTicketLanguageButton);
    }

    public void clickToManageOrderEticket() {
        WebElement orderWrapper = driverTools.waitForElementToAppear(ORDER_DETAIL_WRAPPER).orElseThrow();
        driverTools.scrollInsideElement(orderWrapper);
        driverTools.waitForElementToAppear(ETICKET_MANAGE_ORDER_BUTTON);
        driverTools.click(ETICKET_MANAGE_ORDER_BUTTON);
    }

    public void closeEticketPopUp() {
        driverTools.waitForElementToPresence(CLOSE_MODAL_BUTTON).ifPresent(driverTools::click);
        if(!driverTools.isElementDisplayed(ORDER_ID_LOCATOR)){
            driverTools.waitForElementToPresence(CLOSE_MODAL_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
        }
    }

    public boolean isNewTabOpened(String expectedUrl, int timeOutMiliSec) throws InterruptedException {
        String mainWindowHandle = driver.getWindowHandle();
        driverTools.waitForElementToBeClickable(getETicketElement("English")).orElseThrow();
        try {
            driverTools.click(getETicketElement("English"));
        } catch (ElementClickInterceptedException e) {
            WebElement seeEticket = driverTools.elements(getETicketElement("English")).orElseThrow().get(1);
            driverTools.click(seeEticket);
        }
        long startTime = Instant.now().toEpochMilli();
        long endTime = startTime + timeOutMiliSec;
        Set<String> windowList;
        while (Instant.now().toEpochMilli() < endTime) {
            windowList = driver.getWindowHandles();
            if (windowList.size() > 1) {
                break;
            }
        }
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        String actualUrl = driver.getCurrentUrl();
        boolean urlMatches = actualUrl.contains(expectedUrl);
        driver.close();
        driver.switchTo().window(mainWindowHandle);
        return urlMatches;
    }

    public String getCurrentURL() {
        return driverTools.getCurrentURL();
    }

    public abstract String getOrderId();

    public abstract String getOrderIdAt();

    public abstract String getGiftCardOrderId();

    public abstract boolean isGiftCardReceiptButtonDisplayed();

    public abstract boolean isGiftCardNameDisplayed(String giftCardName);

    public abstract boolean isGiftCardSenderDisplayed(String senderName);

    public abstract boolean isGiftCardRecipientDisplayed(String recipientName);

    public abstract String getGiftCardDeliveryDateTime();

    public boolean isETicketCardDisplayed() {
        for (int attempt = 1; attempt <= 5; attempt++) {
            if (driverTools.waitForElementToAppear(ETICKET_CARD, 10).isPresent()) {
                return true;
            }
            if (attempt < 5) {
                logger.log("E-ticket card not visible, refreshing (attempt " + attempt + "/5)");
                driverTools.refreshWebpage();
            }
        }
        return false;
    }

    public boolean isWaitingForConfirmationAppeared() {
        driverTools.waitForElementToPresence(WAITING_FOR_CONFIRMATION);
        return driverTools.isElementDisplayed(WAITING_FOR_CONFIRMATION);
    }

    public boolean isHotelNameDisplayed(String hotelName) {
        logger.log("hotel name >" + hotelName );
        return driverTools.waitForElementToAppear(getHotelNameLocator(hotelName), 20).isPresent();
    }

    public String getHotelName(String hotelName) {
        logger.log("hotel name >" + hotelName );
        WebElement hotelNameElement = driverTools.waitForElementToAppear(getHotelNameLocator(hotelName), 20).orElseThrow();
        return driverTools.getText(hotelNameElement);
    }

    public String verifyHotelName() {
        driverTools.waitForElementToPresence(HOTEL_NAME_LOCATOR);
        return driverTools.getText(HOTEL_NAME_LOCATOR).get();
    }

    public String roomNameDisplayed() {
    driverTools.scrollIntoWebView(DETAIL_RESERVATION_CARD);
    driverTools.click(ROOM_DETAILS_BUTTON);
    driverTools.waitForElementToAppear(ROOM_NAME_LOCATOR).ifPresent(driverTools::scrollIntoWebView);
    String roomName = driverTools.getText(ROOM_NAME_LOCATOR).get();
    closeModal();
    return roomName;
    }

    public abstract void closeModal();

    public int getTotalNight() {
        return Integer.parseInt(driverTools.getText(TOTAL_NIGHT_LOCATOR).get());
    }

    public int getTotalRoom() {
    driverTools.click(ROOM_DETAILS_BUTTON);
    driverTools.waitForElementToAppear(TOTAL_ROOM_LOCATOR).ifPresent(driverTools::scrollIntoWebView);
    int totalRoom = Integer.parseInt(driverTools.getText(TOTAL_ROOM_LOCATOR).get().split(" ")[0]);
    closeModal();
    return totalRoom;
    }


    public int getRoomTypeCount() {
        driverTools.waitForElementToAppear(ROOM_TYPE_LABEL).orElseThrow();
        List<WebElement> roomTypeLabels = driverTools.elements(ROOM_TYPE_LABEL).orElseThrow();
        return roomTypeLabels.size();
    }

    public int getTotalChild(String orderId) throws InterruptedException, ExecutionException {
        try {
            JSONObject orderDetailResponse = getInternalOrderDetail(orderId);
            JSONObject data = orderDetailResponse.getJSONObject("data");
            JSONArray orderDetails = data.getJSONArray("orderDetails");

            int totalChildren = 0;
            for (int i = 0; i < orderDetails.length(); i++) {
                JSONObject orderDetail = orderDetails.getJSONObject(i);
                if (orderDetail.has("vertical")) {
                    JSONObject verticalData = orderDetail.getJSONObject("vertical");
                    if (verticalData.has("bookingChild")) {
                        totalChildren += verticalData.getInt("bookingChild");
                    }
                }
            }
            return totalChildren;
        } catch (JSONException e) {
            logger.log("Error getting total child from API for orderId: " + orderId + " - " + e.getMessage());
        }
        return 0;
    }

    /**
     * Logs a non-fatal order-creation failure so callers can continue a multi-order setup (e.g. one
     * vertical's booking failing shouldn't stop the others from being attempted) without resorting to
     * {@code System.out}/raw logger calls inside the test class itself.
     */
    public void logOrderCreationFailure(String productLabel, Exception err) {
        logger.log("Error creating " + productLabel + " order: " + err.getMessage());
    }

    public boolean isCallButtonDisplayed() {
        driverTools.waitForElementsToAppear(CALL_BUTTON);
        return driverTools.element(CALL_BUTTON).isPresent();
    }

    public boolean verifyLocationRedirectionButton() {
        WebElement orderWrapper = driverTools.waitForElementToAppear(ORDER_DETAIL_WRAPPER).orElseThrow();
        driverTools.scrollInsideElement(orderWrapper);
        driverTools.waitForElementToAppear(LOCATION_REDIRECTION_BUTTON, 10).orElseThrow();
        driverTools.waitForElementToPresence(LOCATION_REDIRECTION_BUTTON, 10);
        return driverTools.element(LOCATION_REDIRECTION_BUTTON).isPresent();
    }

    public boolean verifyRefundAndRescheduleInfo() {
        driverTools.waitForElementToPresence(LOCATION_REDIRECTION_BUTTON, 20);
        driverTools.scrollToElement(REFUND_RESCHEDULE_INFO_SECTION);
        driverTools.waitForElementToPresence(REFUND_RESCHEDULE_INFO_SECTION, 30);
        return driverTools.element(REFUND_RESCHEDULE_INFO_SECTION).isPresent() || driverTools.element(REFUND_RESCHEDULE_INFO_BUTTON).isPresent();
    }

    public boolean verifyRefundAndRescheduleInfoHotelMultiRoom() {
        driverTools.waitForElementToAppear(REFUND_RESCHEDULE_INFO_HOTEL_MULTI_ROOM, 30).orElseThrow();
        driverTools.scrollToElement(REFUND_RESCHEDULE_INFO_HOTEL_MULTI_ROOM);
        driverTools.waitForElementToPresence(REFUND_RESCHEDULE_INFO_HOTEL_MULTI_ROOM, 30);
        return driverTools.isElementDisplayed(REFUND_RESCHEDULE_INFO_HOTEL_MULTI_ROOM);
    }

    public void clickRefundRescheduleDetailsButton() {
        driverTools.waitForElementToAppear(REFUND_RESCHEDULE_INFO_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(REFUND_RESCHEDULE_INFO_BUTTON);
    }

    public boolean isRefundRescheduleInfoPageOpened() {
        driverTools.waitForElementToAppear(REFUND_RESCHEDULE_INFO_PAGE, 10);
        return driverTools.isElementDisplayed(REFUND_RESCHEDULE_INFO_PAGE);
    }

    public boolean isRefundNotSupported() {
        try {
            WebElement iframe = driverTools.waitForElementToAppear(REFUND_RESCHEDULE_IFRAME, 10).orElse(null);
            if (iframe != null) {
                driverTools.switchToIframe(iframe);
                boolean isNotSupported = driverTools.waitForElementToAppear(REFUND_NOT_SUPPORTED_TEXT, 10).isPresent();
                driverTools.switchBackFromIframe();
                return isNotSupported;
            }
        } catch (Exception e) {
            logger.log("Error checking refund not supported: " + e.getMessage());
            driverTools.switchBackFromIframe();
        }
        return false;
    }

    public void clickRefundRescheduleBackButton() {
        driverTools.waitForElementToAppear(REFUND_RESCHEDULE_BACK_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(REFUND_RESCHEDULE_BACK_BUTTON);
    }

    /**
     * Opens Manage Order cancel flow and confirms cancellation.
     * Confirmation UI differs by platform — implemented in
     * {@link OrderDetailDweb} (popup) and {@link OrderDetailMweb} (cancel footer).
     */
    public abstract void clickCancelOrderButton();

    public void cancelOrderFromRefundRescheduleSection() {
        driverTools.waitForElementToAppear(ORDER_DETAIL_WRAPPER).orElseThrow();
        driverTools.scrollUntilElementFound(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON, 600, 2, 8);
        try {
            driverTools.scrollIntoWebView(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON);
        } catch (Exception e) {
            logger.log("scrollIntoWebView RefundRescheduleInfo cancel: " + e.getMessage());
            driverTools.scrollUntilElementFound(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON, 600, 2, 8);
        }
        driverTools.waitForElementToAppear(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON, 20).orElseThrow();
        driverTools.scrollIntoWebView(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON);
        driverTools.clickWithJavascriptExecutor(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON);

        clickCancelOrderAgreementCheckboxesIfPresent();
        confirmCancelOrderIfRequired();
    }

    /**
     * Cancels an unpaid order from the order-detail Refund/Reschedule section:
     * 1. Clicks the page-level "Cancel order" button inside {@code RefundRescheduleInfo_cancel_button}.
     * 2. In the modal, checks every agreement checkbox.
     * 3. Clicks the modal "Agree" button to confirm cancellation.
     */
    public void cancelOrderViaRefundRescheduleModal() {
        driverTools.waitForElementToAppear(ORDER_DETAIL_WRAPPER).orElseThrow();
        driverTools.scrollUntilElementFound(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON_CONTAINER, 600, 2, 8);
        driverTools.waitForElementToAppear(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON_CONTAINER, 20).orElseThrow();
        driverTools.scrollIntoWebView(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON_CONTAINER);
        driverTools.clickWithJavascriptExecutor(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON_CONTAINER);
        logger.log("Clicked Cancel order button on order detail page");

        driverTools.waitForElementToAppear(CANCEL_ORDER_MODAL, 15).orElseThrow();
        List<WebElement> agreementLabels = driverTools
                .waitForPresenceElementsList(CANCEL_ORDER_MODAL_CHECKBOX_LABEL, 10)
                .orElseThrow();
        logger.log("Found " + agreementLabels.size() + " cancel agreement checkbox(es) in modal");
        for (int i = 0; i < agreementLabels.size(); i++) {
            WebElement label = agreementLabels.get(i);
            driverTools.scrollIntoWebView(label);
            driverTools.clickWithJavascriptExecutor(label);
            logger.log("Checked cancel agreement checkbox " + (i + 1) + " of " + agreementLabels.size());
        }

        driverTools.waitForElementToAppear(CANCEL_ORDER_MODAL_AGREE_BUTTON, 10).orElseThrow();
        driverTools.scrollIntoWebView(CANCEL_ORDER_MODAL_AGREE_BUTTON);
        driverTools.clickWithJavascriptExecutor(CANCEL_ORDER_MODAL_AGREE_BUTTON);
        logger.log("Clicked Agree button to confirm order cancellation");
    }

    private void clickCancelOrderAgreementCheckboxesIfPresent() {
        List<WebElement> agreementLabels = driverTools
                .waitForPresenceElementsList(CANCEL_ORDER_MODAL_AGREEMENT_LABEL, 8)
                .orElse(Collections.emptyList());
        if (agreementLabels.isEmpty()) {
            logger.log("No cancel-order agreement checkboxes found in modal; skipping consent step");
            return;
        }

        logger.log("Found " + agreementLabels.size() + " cancel agreement checkbox(es) in modal");
        for (int i = 0; i < agreementLabels.size(); i++) {
            WebElement label = agreementLabels.get(i);
            driverTools.scrollIntoWebView(label);
            driverTools.clickWithJavascriptExecutor(label);
            logger.log("Checked cancel agreement checkbox " + (i + 1));
        }

        driverTools.waitForElementToAppear(CANCEL_ORDER_AGREE_BUTTON, 10).orElseThrow();
        driverTools.scrollIntoWebView(CANCEL_ORDER_AGREE_BUTTON);
        driverTools.clickWithJavascriptExecutor(CANCEL_ORDER_AGREE_BUTTON);
        logger.log("Clicked agree button to confirm cancellation terms");
    }

    private void confirmCancelOrderIfRequired() {
        if (isCanceledOrderBannerVisible(3)) {
            logger.log("Canceled order banner already displayed after agreeing to cancellation terms");
            return;
        }

        for (int attempt = 1; attempt <= 10; attempt++) {
            if (isCanceledOrderBannerVisible(0)) {
                logger.log("Canceled order banner displayed after agree (attempt " + attempt + ")");
                return;
            }

            if (clickCancelOrderConfirmationIfDisplayed()) {
                logger.log("Clicked cancel-order confirmation on attempt " + attempt);
                break;
            }

            logger.log("Waiting for cancel-order confirmation UI, attempt " + attempt + " of 10");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        clickCancelOrderConfirmationIfDisplayed();
        isCanceledOrderBannerVisible(15);
    }

    public boolean isCanceledOrderBannerVisible(int timeoutSeconds) {
        if (timeoutSeconds <= 0) {
            return driverTools.isElementDisplayed(ORDER_DETAIL_CANCELED_BANNER_TITLE);
        }
        return driverTools.waitForElementToAppear(ORDER_DETAIL_CANCELED_BANNER_TITLE, timeoutSeconds).isPresent();
    }

    private boolean clickCancelOrderConfirmationIfDisplayed() {
        if (driverTools.isElementDisplayed(CANCEL_ORDER_MODAL_CANCEL_CONFIRM_BUTTON)) {
            driverTools.scrollIntoWebView(CANCEL_ORDER_MODAL_CANCEL_CONFIRM_BUTTON);
            driverTools.clickWithJavascriptExecutor(CANCEL_ORDER_MODAL_CANCEL_CONFIRM_BUTTON);
            return true;
        }
        if (driverTools.isElementDisplayed(CANCEL_ORDER_BUTTON_POPUP)) {
            driverTools.scrollIntoWebView(CANCEL_ORDER_BUTTON_POPUP);
            driverTools.clickWithJavascriptExecutor(CANCEL_ORDER_BUTTON_POPUP);
            return true;
        }
        if (driverTools.isElementDisplayed(CANCEL_ORDER_BUTTON_POPUP_CONFIRMATION)) {
            driverTools.clickWithJavascriptExecutor(CANCEL_ORDER_BUTTON_POPUP_CONFIRMATION);
            return true;
        }
        if (driverTools.isElementDisplayed(OTHER_CANCEL_ORDER_CONFIRMATION_BUTTON)) {
            driverTools.clickWithJavascriptExecutor(OTHER_CANCEL_ORDER_CONFIRMATION_BUTTON);
            return true;
        }
        return false;
    }

    public boolean isCanceledOrderNotificationBannerDisplayed() {
         driverTools.waitForElementToAppear(ORDER_DETAIL_CANCELED_BANNER_TITLE);
         driverTools.refreshWebpage();
         if (!driverTools.isElementPresent(ORDER_DETAIL_CANCELED_BANNER_TITLE)) {
             driverTools.dummyWait(3);
             driverTools.refreshWebpage();
         }
         return driverTools.isElementPresent(ORDER_DETAIL_CANCELED_BANNER_TITLE);
    }

    /**
     * Retry-aware variant: refreshes the order detail page up to {@code retry} times waiting for
     * the canceled-order banner. If still not visible, reopens the order detail via
     * {@link OrderListWeb#openActiveTicketViaURL(String)} and retries another {@code retry} times
     * to recover from stale cached content right after a cancellation.
     *
     * @param orderId order id used to reopen the order detail page on the recovery pass
     * @param retry   number of attempts per pass (refresh pass and reopen pass)
     * @return {@code true} if the canceled banner becomes visible within the attempts
     */
    public boolean isCanceledOrderNotificationBannerDisplayed(String orderId, int retry) throws InterruptedException {
        for (int i = 0; i < retry; i++) {
            driverTools.refreshWebpage();
            if (driverTools.waitForElementToAppear(ORDER_DETAIL_CANCELED_BANNER_TITLE).isPresent()
                    && driverTools.isElementPresent(ORDER_DETAIL_CANCELED_BANNER_TITLE)) {
                logger.log("Canceled order banner displayed on attempt " + (i + 1) + " of " + retry);
                return true;
            }
            logger.log("Canceled order banner not found, attempt " + (i + 1) + " of " + retry);
            Thread.sleep(3000);
        }

        logger.log("Canceled order banner not found after " + retry + " attempts");
        logger.log("try to reopen the order detail page to refresh the content");
        OrderListWeb<T> orderListPage = OrderListWeb.getInstance(session);
        for (int i = 0; i < retry; i++) {
            orderListPage.openActiveTicketViaURL(orderId);
            if (driverTools.waitForElementToAppear(ORDER_DETAIL_CANCELED_BANNER_TITLE).isPresent()
                    && driverTools.isElementPresent(ORDER_DETAIL_CANCELED_BANNER_TITLE)) {
                logger.log("Canceled order banner displayed after reopen, attempt " + (i + 1) + " of " + retry);
                return true;
            }
            logger.log("Canceled order banner still not found after reopen, attempt " + (i + 1) + " of " + retry);
            Thread.sleep(3000);
        }
        return false;
    }


    public void clickDeleteOrderOnCanceledOrderDetail() {
        driverTools.waitForElementToAppear(DETAIL_RESERVATION_DELETE_ORDER_BUTTON);
        driverTools.clickWithJavascriptExecutor(DETAIL_RESERVATION_DELETE_ORDER_BUTTON);

        driverTools.waitForElementToAppear(DELETE_ORDER_CONFIRMATION_BUTTON);
        driverTools.clickWithJavascriptExecutor(DELETE_ORDER_CONFIRMATION_BUTTON);
    }

    /**
     * Presence check for the PAH-style "Cancel order" button in the Refund/Reschedule
     * section (below the hotel card / Manage Order). Scrolls the page because the button
     * is typically below the fold and may lazy-render. Returns {@code false} when the
     * order is already canceled or the button is otherwise not found.
     */
    public boolean isCancelOrderButtonVisible() {
        hideTravelDealBannerIfPresent();
        driverTools.waitForElementToAppear(ORDER_DETAIL_WRAPPER, 10);
        if (driverTools.waitForElementToPresence(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON, 2).isPresent()) {
            return driverTools.isElementDisplayed(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON);
        }
        return driverTools.scrollUntilElementFound(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON, 600, 1, 8).isPresent();
    }

    /**
     * Fast presence check for the "Delete order" ({@code Hapus pesanan}) button shown on a
     * canceled order detail. Expected to be {@code true} only after a successful cancellation.
     */
    public boolean isDeleteOrderButtonVisible() {
        return driverTools.waitForElementToAppear(DETAIL_RESERVATION_DELETE_ORDER_BUTTON, 10).isPresent();
    }

    /**
     * Fast presence check for the "Make similar order" ({@code Buat pesanan serupa}) button
     * shown on a canceled order detail. Expected to be {@code true} only after a successful
     * cancellation, together with {@link #isDeleteOrderButtonVisible()}.
     */
    public boolean isMakeSimilarOrderButtonVisible() {
        return driverTools.waitForElementToAppear(MAKE_SIMILAR_ORDER_BUTTON, 10).isPresent();
    }

    /**
     * Fast presence check for the "Refund" action under the Manage Order section. Unlike
     * {@link #isRefundButtonDisplayed()} this does not refresh the page — useful for
     * negative assertions such as "PAH order must only expose cancel".
     */
    public boolean isRefundManageOrderButtonPresent() {
        return driverTools.waitForElementToAppear(REFUND_MANAGE_ORDER_BUTTON, 5).isPresent();
    }

    /**
     * Fast presence check for the "Reschedule" action under the Manage Order section.
     * Used together with {@link #isRefundManageOrderButtonPresent()} for negative assertions.
     */
    public boolean isRescheduleManageOrderButtonPresent() {
        return driverTools.waitForElementToAppear(RESCHEDULE_MANAGE_ORDER_BUTTON, 5).isPresent();
    }

    public boolean verifyLpgBanner() {
        return driverTools.element(LPG_BANNER).isPresent();
    }

    public boolean isRefundButtonDisplayed() {
        int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            if (driverTools.waitForElementToAppear(REFUND_MANAGE_ORDER_BUTTON, 30).isPresent()) {
                try {
                    driverTools.scrollIntoWebView(REFUND_MANAGE_ORDER_BUTTON);
                } catch (DriverToolsException driverToolsException) {
                    driverTools.scrollToElement(REFUND_MANAGE_ORDER_BUTTON);
                }
                return driverTools.element(REFUND_MANAGE_ORDER_BUTTON).isPresent();
            }

            attempt++;
            if (attempt < maxRetries) {
                logger.log("REFUND_MANAGE_ORDER_BUTTON not found. Refreshing page (attempt " + attempt + " of " + maxRetries + ")");
                driverTools.refreshWebpage();
                try {
                    Thread.sleep(2000); // Wait for page to reload
                } catch (InterruptedException e) {
                    logger.log("Sleep interrupted: " + e.getMessage());
                }
            }

        }

        logger.log("REFUND_MANAGE_ORDER_BUTTON not found after " + maxRetries + " refresh attempts");
        return driverTools.element(REFUND_MANAGE_ORDER_BUTTON).isPresent();

    }

    public void clickRefundButton() {
        driverTools.waitForElementToPresence(MANAGE_ORDER_SECTION);
        driverTools.clickWithJavascriptExecutor(REFUND_MANAGE_ORDER_BUTTON);
    }

    public void clickRefundButtonTTD() {
        boolean manageOrderVisible = false;
        for (int attempt = 1; attempt <= 5; attempt++) {
            if (driverTools.waitForElementToAppear(MANAGE_ORDER_FLOATING_BTN, 5).isPresent()) {
                manageOrderVisible = true;
                break;
            }
            if (attempt < 5) {
                logger.log("Manage Order button not visible, refreshing (attempt " + attempt + "/5)");
                driverTools.refreshWebpage();
            }
        }
        if (!manageOrderVisible) {
            driverTools.waitForElementToAppear(MANAGE_ORDER_FLOATING_BTN, 30).orElseThrow();
        }
        driverTools.clickWithJavascriptExecutor(MANAGE_ORDER_FLOATING_BTN);
        driverTools.waitForElementToAppear(REFUND_MANAGE_ORDER_BUTTON, 30).orElseThrow();
        driverTools.clickWithJavascriptExecutor(REFUND_MANAGE_ORDER_BUTTON);
    }

    public void clickRescheduleButton(){
        driverTools.waitForElementToPresence(RESCHEDULE_MANAGE_ORDER_BUTTON);
        driverTools.scrollUntilElementFound(MANAGE_ORDER_SECTION);

        for (int attempt = 1; attempt <= 3; attempt++) {
            driverTools.clickWithJavascriptExecutor(RESCHEDULE_MANAGE_ORDER_BUTTON);
            if (driverTools.waitForElementToDisappear(RESCHEDULE_MANAGE_ORDER_BUTTON, 5)) {
                logger.log("Reschedule button disappeared after click (attempt " + attempt + ")");
                return;
            }
            logger.log("Reschedule button still present, retrying click (attempt " + attempt + "/3)");
        }
        logger.softWarning("Reschedule button still present after 10 click attempts");
    }

    public boolean isRescheduleButtonDisplayed() {
        return driverTools.element(RESCHEDULE_MANAGE_ORDER_BUTTON).isPresent();
    }

    public boolean verifyEditGuestDetail() {
        return driverTools.element(EDIT_GUEST_BUTTON).isPresent();
    }

    public boolean verifyEticketManageOrder() {
        return driverTools.element(ETICKET_MANAGE_ORDER_BUTTON).isPresent();
    }

    public abstract boolean verifyPageModule();

    public String verifyAtCarDetailSection() {
        return driverTools.getText(CAR_DETAIL_CAR_TYPE).get();
    }

    public boolean checkAirportTransferDetail(String pickupPlace, String dropPlace) {
        driverTools.scrollIntoWebView(DETAIL_RESERVATION_CARD);
        List<WebElement> elements = driverTools.elements(PICKUP_DROPOFF_PLACE).get();
        return ((driverTools.getText(elements.get(0)).equalsIgnoreCase(pickupPlace)) &&
                (driverTools.getText(elements.get(1)).equalsIgnoreCase(dropPlace))
        );
    }

    public boolean checkCarRentalDetail(String pickupPlace, String duration) {
        driverTools.scrollIntoWebView(DETAIL_RESERVATION_CARD);
        List<WebElement> elements = driverTools.elements(CAR_RENTAL_RESERVATION).get();
        WebElement carDurationElement = driverTools.element(CAR_RENTAL_DURATION).get();

        boolean isPickupPlaceCorrect = driverTools.getText(elements.get(2)).equalsIgnoreCase(pickupPlace);
        logger.log("isPickupPlaceCorrect: " + isPickupPlaceCorrect);

        boolean isDurationCorrect = driverTools.getText(carDurationElement).contains(String.valueOf(duration));
        logger.log("isDurationCorrect: " + isDurationCorrect);

        return isPickupPlaceCorrect && isDurationCorrect;
    }

    public boolean verifyAtFacilitiesDetailPresent() {
        return driverTools.element(FACILITIES_SECTION).isPresent();
    }

    public boolean verifyAtVendorSectionPresent() {
        driverTools.scrollIntoWebView(VENDOR_SECTION);
        return driverTools.element(VENDOR_SECTION).isPresent();
    }

    public boolean verifyAtContactDetail(String salutation, String guestName, String phoneNumber) {
        try {
            driverTools.scrollIntoWebView(CONTACT_DETAILS_SECTION);
        } catch (Exception e) {
            logger.log("Native scroll failed, falling back to JS scroll into CONTACT_DETAILS_SECTION");
            driverTools.scrollToElement(CONTACT_DETAILS_SECTION);
        }
    
        String contactName = "";
        String phoneData = "";
        int maxAttempts = 10;
        int attempt = 0;
    
        while ((contactName.isEmpty() || phoneData.isEmpty()) && attempt < maxAttempts) {
            if (attempt > 0) {
                try {
                    driverTools.dummyWait(1);
                } catch (Exception e) {
                    logger.log("Error during wait: " + e.getMessage());
                }
            }
    
            Optional<List<WebElement>> elementsOptional = driverTools.elements(CONTACT_DETAILS_DATA);
            if (elementsOptional.isPresent() && elementsOptional.get().size() >= 2) {
                List<WebElement> elements = elementsOptional.get();
                contactName = driverTools.getText(elements.get(0));
                phoneData = driverTools.getText(elements.get(1));
            } else {
                logger.log("Insufficient elements found on attempt " + (attempt + 1) + ". Elements size: " 
                        + (elementsOptional.map(List::size).orElse(0)));
            }
            attempt++;
        }
    
        String expectedName = (salutation + " " + guestName).trim();
        logger.log("Contact Detail Check -> Expected Name: '" + expectedName + "', Actual Name: '" + contactName + "'");
        logger.log("Contact Detail Check -> Expected Phone: '" + phoneNumber + "', Actual Phone: '" + phoneData + "'");
    
        return contactName.equalsIgnoreCase(expectedName) && phoneData.equals(phoneNumber);
    }

    public abstract String getVerticalTotalPrice();

    public String getFinalPrice() {
        WebElement finalPriceTextElement = driverTools.scrollUntilElementFound(TOTAL_PRICE, 600, 2, 10).orElseThrow();
        String finalPriceText = driverTools.getText(finalPriceTextElement);
        String finalPriceFormatted = finalPriceText.replaceAll("[^\\d.]", "");
        String[] parts = finalPriceFormatted.split("\\.");
        if (parts.length > 2) {
            finalPriceFormatted = String.join(".", Arrays.copyOf(parts, 2));
        }
        logger.log("finalPrice " + finalPriceFormatted);
        return finalPriceFormatted;
    }

    public void clickManageOrder(){
        driverTools.waitForElementToAppear(MANAGE_ORDER);
        for (int attempt = 0; attempt < 2; attempt++) {
            if (!driverTools.isElementDisplayed(MANAGE_ORDER)) {
                driverTools.refreshWebpage();
                driverTools.waitForElementToAppear(MANAGE_ORDER);
            }
        }
        driverTools.scrollToElement(MANAGE_ORDER);
        driverTools.clickWithJavascriptExecutor(MANAGE_ORDER);
    }

    public void verifyReceiptButton(){
        boolean receiptButton = driverTools.waitForElementToAppear(RECEIPT_BUTTON).isPresent();
        Assertion.assertThat("Verify receipt button is visible on order detail page", receiptButton, is(true));
        try {
            driverTools.click(RECEIPT_BUTTON);
        } catch (Exception e) {
            Assertion.assertThat("Verify receipt button is clickable on order detail page", receiptButton, is(true));
        }
    }


    public void goToPreviousOrderBanner() {
        driverTools.scrollUntilElementFound(RESCHEDULE_CARD_BUTTON);
    }

    public void clickPreviousOrder() {
        try {
            driverTools.click(RESCHEDULE_CARD_BUTTON);
        } catch (Exception e) {
            logger.log("Clicking with javascript executor due to: " + e.getMessage());
            driverTools.clickWithJavascriptExecutor(RESCHEDULE_CARD_BUTTON);
        }
    }

    public boolean isNewScheduleButtonDisplayed() {
        return driverTools.waitForElementToAppear(NEW_SCHEDULE_CARD_BUTTON).isPresent();
    }

    public String isTrainDetailCorrect() {
        driverTools.scrollIntoWebView(DETAIL_RESERVATION_CARD);

        List<WebElement> elements = driverTools.elements(DEPARTURE_ARRIVAL_NAME).get();
        String departureName = elements.get(0).getText();
        String arrivalName = elements.get(1).getText();
        return departureName + arrivalName;
    }

    public boolean isTrainDepartureArrivalDurationExists() {
        return driverTools.element(DEPARTURE_ARRIVAL_DURATION).isPresent();
    }

    public boolean isTrainNameAndClassExists() {
        driverTools.click(DETAIL_RESERVATION_DETAIL_BUTTON);
        boolean result = driverTools.waitForElementToAppear(TRAIN_NAME_CLASS_MODAL).isPresent();
        driverTools.click(CLOSE_MODAL_BUTTON);
        return result;
    }

    public boolean passengerAndFacilitiesExists() {
        driverTools.scrollIntoWebView(PASSENGER_FACILITIES_SECTION);
        return driverTools.element(PASSENGER_FACILITIES_LIST).isPresent();
    }

    public boolean verifyIssuedETicket() {
        for (int i = 0; i < 10; i++) {
            if (!driverTools.isElementDisplayed(VIEW_E_TICKET_BUTTON)) {
                logger.log("View e-ticket button not found. Refreshing page (attempt " + i + " of 10)");
                driverTools.refreshWebpage();
                driverTools.dummyWait(2);
            } else {
                break;
            }
        }
        return driverTools.isElementDisplayed(VIEW_E_TICKET_BUTTON);
    }

    public void clickViewETicketButton() {
        hideTravelDealBannerIfPresent();
        driverTools.waitForElementToAppear(VIEW_E_TICKET_BUTTON);
        driverTools.click(VIEW_E_TICKET_BUTTON);
    }

    public void closeETicketPopup() {
        driverTools.click(E_TICKET_POPUP_CLOSE_BUTTON);
    }

    public boolean verifyETicketModalContainsEnglishAndBahasaOption() {
        driverTools.waitForElementToAppear(E_TICKET_POPUP);
        return (driverTools.waitForElementToAppear(E_TICKET_POPUP_ENGLISH_SELECTION).isPresent() &&
                driverTools.waitForElementToAppear(E_TICKET_POPUP_BAHASA_SELECTION).isPresent()) ||
                (driverTools.isElementDisplayed(OrderDetailDweb.languageOption("English")) &&
                driverTools.isElementDisplayed(OrderDetailDweb.languageOption("Bahasa Indonesia")));
    }

    /**
     * Clicks the requested language inside the e-ticket bottom-sheet modal,
     * waits for the PDF tab to open, captures its URL, closes that tab and
     * returns focus to the order-detail tab.
     *
     * @param language "English" or "Bahasa Indonesia"
     * @return the PDF URL surfaced in the new tab; empty string when no new tab opened in time
     */
    public String openETicketAndCapturePdfUrl(String language) {
        String mainWindowHandle = driver.getWindowHandle();
        int existingHandles = driverTools.getWindowHandles().size();
        if (driverTools.waitForElementToAppear(OrderDetailDweb.languageOption(language)).isPresent()) {
            driverTools.clickWithJavascriptExecutor(OrderDetailDweb.languageOption(language));
        } else {
            Locator target = "Bahasa Indonesia".equalsIgnoreCase(language)
                    ? E_TICKET_POPUP_BAHASA_SELECTION
                    : E_TICKET_POPUP_ENGLISH_SELECTION;
            driverTools.click(target);
        }

        String capturedUrl = "";
        try {
            driverTools.waitForNewTabAndSwitchToLatest(existingHandles);
            driverTools.waitForWebToLoad();
            capturedUrl = driverTools.getCurrentURL();
            driverTools.closeCurrentTab();
        } catch (TimeoutException ignored) {
            // No new tab opened within the configured wait window; nothing to capture.
        }

        driverTools.switchToWindow(mainWindowHandle);
        return capturedUrl == null ? "" : capturedUrl;
    }
    public boolean verifyManageOrderETicketShown() {
        driverTools.scrollIntoWebView(E_TICKET_BUTTON);
        return driverTools.waitForElementToAppear(E_TICKET_BUTTON).isPresent();
    }
    public boolean verifyYourETicketIsHereSpanShown() {
        return driverTools.waitForElementToPresence(YOUR_E_TICKET_IS_HERE_SPAN).isPresent();
    }

    @SneakyThrows
    public boolean verifyIssuedETicket(String orderId) {
        boolean viewETicketButton = driverTools.waitForElementToAppear(VIEW_E_TICKET_BUTTON).isPresent();
        if (!viewETicketButton) {
            // Backend hadn't issued the e-ticket yet (or the order-detail
            // page hadn't finished rendering). Re-open the detail page a
            // few times and re-check. We use waitForElementToAppear instead
            // of isElementDisplayed so each iteration gives the page a
            // chance to load - the previous instant-check loop burnt all
            // 10 attempts in under a second and never actually waited for
            // the issuance banner to render.
            OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
            for (int i = 0; i < 10; i++) {
                orderListPage.openActiveTicketViaURL(orderId);
                viewETicketButton = driverTools.waitForElementToAppear(VIEW_E_TICKET_BUTTON).isPresent();
                if (viewETicketButton) break;
            }
        }
        Assertion.assertThat("Verify view e-ticket button is visible on order detail page", viewETicketButton, is(true));
        return true;
    }

    public void verifyIssuedQRCode() {
        boolean qrCode = driverTools.waitForElementToAppear(QR_CODE).isPresent();
        Assertion.assertThat("Verify QR code is visible on order detail page", qrCode, is(true));
    }

    /**
     * Clicks a "See QR code" / "See e-boarding pass" / "Online check-in" entry point
     * if it is rendered on the order detail page. On Whoosh & Train issued orders the
     * boarding pass / QR is sometimes behind such a CTA, sometimes inlined.
     *
     * @return {@code true} if the CTA was visible and clicked, {@code false} otherwise.
     */
    public boolean clickSeeQRCodeIfPresent() {
        if (driverTools.waitForElementToAppear(SEE_QR_CODE_BUTTON, 5).isPresent()) {
            driverTools.scrollIntoWebView(SEE_QR_CODE_BUTTON);
            driverTools.clickWithJavascriptExecutor(SEE_QR_CODE_BUTTON);
            return true;
        }
        return false;
    }

    /**
     * Verifies that at least {@code expectedCount} QR code images are displayed
     * (one per non-infant passenger). Infants do not get a boarding QR.
     */
    public void verifyQRCodesForPassengers(int expectedCount) {
        driverTools.waitForElementToAppear(QR_CODE);
        List<WebElement> qrCodes = driverTools.elements(QR_CODE_ALL).orElse(Collections.emptyList());
        Assertion.assertThat(
                "Verify at least " + expectedCount + " QR codes are visible (one per non-infant passenger)",
                qrCodes.size() >= expectedCount,
                is(true));
    }

    /**
     * Clicks the "Change seat" CTA on a Train order detail page (typically rendered for
     * orders still in the "Waiting for Payments" state).
     *
     * @return {@code true} if the CTA was visible and clicked, {@code false} otherwise.
     */
    public boolean clickChangeTrainSeatIfPresent() {
        if (driverTools.waitForElementToAppear(CHANGE_TRAIN_SEAT_BUTTON, 5).isPresent()) {
            driverTools.scrollIntoWebView(CHANGE_TRAIN_SEAT_BUTTON);
            driverTools.clickWithJavascriptExecutor(CHANGE_TRAIN_SEAT_BUTTON);
            return true;
        }
        return false;
    }

    public boolean isChangeTrainSeatButtonDisplayed() {
        return driverTools.waitForElementToAppear(CHANGE_TRAIN_SEAT_BUTTON, 5).isPresent();
    }

    public void verifyShareETicketAndReceiptButton(String fileName, String timeOut) {
        driverTools.scrollIntoWebView(MANAGE_ORDER_BUTTONS);

        boolean eTicketButton = driverTools.waitForElementToAppear(E_TICKET_BUTTON).isPresent();
        Assertion.assertThat("Verify e-ticket button is visible on order detail page", eTicketButton, is(true));

        driverTools.clickWithJavascriptExecutor(E_TICKET_BUTTON);
        boolean eTicketPopup = driverTools.waitForElementToAppear(E_TICKET_POPUP).isPresent();
        Assertion.assertThat("Verify e-ticket button is clickable on order detail page", eTicketPopup, is(true));
        driverTools.clickWithJavascriptExecutor(CLOSE_MODAL_BUTTON);

        boolean receiptButton = driverTools.waitForElementToAppear(RECEIPT_BUTTON).isPresent();
        Assertion.assertThat("Verify receipt button is visible on order detail page", receiptButton, is(true));

        if (Configs.PLATFORM == Platform.DWEB) {
            driverTools.clickWithJavascriptExecutor(RECEIPT_BUTTON);
            Assertion.assertThat("Verify receipt button is clickable on order detail page", verifyDownloadFile(fileName, timeOut), is(true));
        }
    }

    public boolean verifyDownloadFile(String fileName, String timeOut) {
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        logger.log("Download dir:" + downloadDir);
        boolean fileDownloaded = false;

        long startTime = Instant.now().toEpochMilli();
        long waitTime = startTime + Integer.parseInt(timeOut);

        while (Instant.now().toEpochMilli() < waitTime) {
            File[] listFiles = new File(downloadDir).listFiles();
            for (File file : listFiles) {
                if (file.getName().contains(fileName)) {
                    String[] listFilesName = new File(downloadDir).list();
                    for (int i = 0; i < listFilesName.length; i++) {
                    }
                    fileDownloaded = true;
                    file.delete();
                    break;
                }
            }
            if (fileDownloaded) break;
        }
        return fileDownloaded;
    }

    public void verifyFerryOrderPassengerDetail(String ferryClass, String departurePort, String destinationPort, int totalPassenger) {
        WebElement element = driverTools.waitForElementToAppear(ORDER_CHECK_IN_TIME).orElseThrow();
        driverTools.scrollIntoWebView(element);

        String orderFerryClass = driverTools.waitForElementToAppear(ORDER_FERRY_CLASS).orElseThrow().getText();
        Assertion.assertThat("Verify ferry class is correct on order detail page", orderFerryClass, is(ferryClass));

        String orderDeparturePort = driverTools.waitForElementToAppear(ORDER_DEPARTURE_PORT).orElseThrow().getText();
        Assertion.assertThat("Verify departure port is correct on order detail page", orderDeparturePort, is(departurePort));

        String orderDestinationPort = driverTools.waitForElementToAppear(ORDER_DESTINATION_PORT).orElseThrow().getText();
        Assertion.assertThat("Verify destination port is correct on order detail page", orderDestinationPort, is(destinationPort));

        String orderTotalPassenger = driverTools.waitForElementToAppear(ORDER_TOTAL_PASSENGER).orElseThrow().getText();
        Assertion.assertThat("Verify total passenger is correct on order detail page", orderTotalPassenger.replace(".", ""), is(Integer.toString(totalPassenger)));
    }

    public boolean isTTDValidityAndContactNameExist() {
        List<WebElement> elements = driverTools.elements(TTD_DATE_AND_NAME).get();
        boolean validityDate = driverTools.waitForElementToAppear(elements.get(0)).isPresent();
        boolean contactInfo = driverTools.waitForElementToAppear(elements.get(1)).isPresent();
        return  validityDate && contactInfo;
    }

    public abstract String verifyContactNameAtReservation();

    public String verifyPhoneNumberAtReservation(){
        List<WebElement> dataElements = driverTools.waitForElementsToAppear(CONTACT_DETAIL_RESERVATION_DATA).get();
        return driverTools.getText(dataElements.get(1));
    }

    public abstract String verifyContactEmailAtReservation();

    public boolean verifyFerryReservationDetails(String origin, String destination) throws InterruptedException {
        driverTools.scrollIntoWebView(FERRY_RESERVATION_CITY);

        List<WebElement> cityElements = driverTools.waitForElementsToAppear(FERRY_RESERVATION_CITY).get();

        boolean isDepartureCityCorrect = driverTools.getText(cityElements.get(0)).equalsIgnoreCase(origin);
        logger.log("isDepartureCityCorrect: " + isDepartureCityCorrect);

        boolean isArrivalCityCorrect = driverTools.getText(cityElements.get(3)).equalsIgnoreCase(destination);
        logger.log("isArrivalCityCorrect: " + isArrivalCityCorrect);

        return isDepartureCityCorrect && isArrivalCityCorrect;
    }

    public String verifyDepatureCityFerry(){
        driverTools.scrollIntoWebView(FERRY_RESERVATION_CITY);
        List<WebElement> cityElements = driverTools.waitForElementsToAppear(FERRY_RESERVATION_CITY).get();
        return driverTools.getText(cityElements.get(0));
    }

    public String verifyArrivalCityFerry(){
        List<WebElement> cityElements = driverTools.waitForElementsToAppear(FERRY_RESERVATION_CITY).get();
        return driverTools.getText(cityElements.get(3));
    }

    public String verifyBookingCodeFerry(){
        return  driverTools.getText(FERRY_BOOKING_CODE).get();
    }

    public String verifyBookingCodeBus(){
        driverTools.waitForElementToAppear(BUS_BOOKING_CODE);
        return  driverTools.getText(BUS_BOOKING_CODE).get();
    }
    public String verifyBookingCodeTrain(){
        return  driverTools.getText(TRAIN_BOOKING_CODE).get();
    }

    public String getDepartureArrivalTimeTrain(String type){
        WebElement dateForm;
        if(type == "departure"){
            dateForm = driverTools.element(DEPARTURE_TRAIN).orElseThrow();
            return driverTools.getText(dateForm).split("-")[1].trim();
        } else{
            dateForm = driverTools.element(ARRIVAL_TRAIN).orElseThrow();
            return driverTools.getText(dateForm).split("-")[1].trim();
        }
    }

    public boolean verifyValidityTextFromEVoucher(String validityPeriod) throws IOException, InterruptedException {
        String validityPeriod1 = validityPeriod.split(" - ")[1];
        String validityPeriod2 = validityPeriod.split(" - ")[0];
        String pdfText = driverTools.getPdfContent("voucher");
        return pdfText.contains(validityPeriod1) && pdfText.contains(validityPeriod2) ;
    }

    public String checkForAirportLocationName(){
        driverTools.waitForElementToAppear(AIRPORT_LOCATION_NAME_ORDER_DETAIL_PAGE).orElseThrow();
        return driverTools.getText(AIRPORT_LOCATION_NAME_ORDER_DETAIL_PAGE).orElseThrow();
    }
    public String checkForDestinationLocationName(){
        driverTools.waitForElementToAppear(DESTINATION_LOCATION_NAME_ORDER_DETAIL_PAGE).orElseThrow();
        return driverTools.getText(DESTINATION_LOCATION_NAME_ORDER_DETAIL_PAGE).orElseThrow();
    }

    public String checkForAirportLocationNameTransferOut(){
        driverTools.waitForElementToAppear(DESTINATION_LOCATION_NAME_ORDER_DETAIL_PAGE).orElseThrow();
        return driverTools.getText(DESTINATION_LOCATION_NAME_ORDER_DETAIL_PAGE).orElseThrow();
    }
    public String checkForDestinationLocationNameTransferOut(){
        driverTools.waitForElementToAppear(AIRPORT_LOCATION_NAME_ORDER_DETAIL_PAGE).orElseThrow();
        return driverTools.getText(AIRPORT_LOCATION_NAME_ORDER_DETAIL_PAGE).orElseThrow();
    }

    public void refreshTheWebpage(){
        driverTools.refreshWebpage();
    }

    public String getSupplierOrderIdFromDb(String config, String database, String collectionName, String query) {
        String supplierOrderId = null;
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB(config, database, collectionName, query);
        for (Document document : result) {
            supplierOrderId = document.getString("supplierOrderId");
            if (supplierOrderId != null) break;
        }
        logger.log("supplierOrderId: " + supplierOrderId);
        TiketMongoDBManager.closeMongoDBConnections();
        return supplierOrderId;
    }


    public boolean verifyTextFromEVoucher(String text) throws IOException, InterruptedException {
        String pdfText = driverTools.getPdfContent("voucher");
        return pdfText.contains(text);
    }

    /**
     * Reads the product/package title shown on the e-voucher for the current line item.
     * <p>DWEB: reads from the e-voucher tab DOM (varargs ignored).</p>
     * <p>MWEB/AMWEB/IMWEB: loads PDF text and returns whichever {@code candidateProductNames} appears in that PDF
     * (longest match wins if multiple substrings match), so callers can compare to expected names with {@code containsInAnyOrder}.</p>
     *
     * @param candidateProductNames expected titles to resolve against PDF text (required for non-DWEB)
     */
    public String verifyTextFromEVoucherDwebAndMweb(String... candidateProductNames) throws IOException, InterruptedException {
        if (Configs.PLATFORM == Platform.DWEB) {
            driverTools.tabSwitch("/e-voucher");
            // Robust locator: original product_title selector plus DOM-stable fallbacks
            // (data-testid and PackageDetail/PackageName classes used in newer e-voucher templates).
            Locator bundlingTextLocator = new Locator("BUNDLING_TEXT", new By.ByXPath(
                    "//*[contains(@class,'product_title')]//following-sibling::*[contains(@class,'Text_text')]"
                            + " | //*[@data-testid='product-title']//following-sibling::*[contains(@class,'Text_text')]"
                            + " | //*[contains(@class,'PackageDetail_product_title') or contains(@class,'EVoucher_product_title')]//following-sibling::*[contains(@class,'Text_text')]"
                            + " | //*[contains(@class,'PackageName')]"));
            driverTools.waitForElementToPresence(bundlingTextLocator, 10);
            Optional<WebElement> elementOpt = driverTools.element(bundlingTextLocator);
            if (elementOpt.isEmpty()) {
                logger.softWarning("verifyTextFromEVoucherDwebAndMweb: bundling text locator not found on e-voucher tab. URL=" + driverTools.getCurrentURL() + ", title=" + driverTools.getTitle());
                throw new NoSuchElementException("Could not find bundling product title on e-voucher tab");
            }
            return driverTools.getText(elementOpt.orElseThrow());
        }
        if (candidateProductNames == null || candidateProductNames.length == 0) {
            throw new IllegalArgumentException(
                    "candidateProductNames is required for non-DWEB platforms so PDF text can be matched to an expected product title");
        }
        String pdfText = driverTools.getPdfContent("voucher");
        return resolveProductTitleContainedInPdf(pdfText, candidateProductNames);
    }

    /**
     * Picks the longest candidate string contained in {@code pdfText} to reduce false positives from short substrings.
     * Throws {@link IOException} with a PDF-specific message when no candidate is found.
     */
    private static String resolveProductTitleContainedInPdf(String pdfText, String... candidates) throws IOException {
        String best = null;
        int bestLen = -1;
        if (pdfText != null && candidates != null) {
            for (String c : candidates) {
                if (c == null || c.isBlank()) {
                    continue;
                }
                if (pdfText.contains(c) && c.length() > bestLen) {
                    best = c;
                    bestLen = c.length();
                }
            }
        }
        if (best == null) {
            String snippet = pdfText == null ? "<null>"
                    : (pdfText.length() > 400 ? pdfText.substring(0, 400) + "…" : pdfText);
            throw new IOException("E-voucher PDF did not contain any of the expected product names "
                    + Arrays.toString(candidates) + ". Text snippet: " + snippet.replaceAll("\\s+", " ").trim());
        }
        return best;
    }

    public boolean isRescheduleCardDisplayed(){
        return driverTools.waitForElementToPresence(RESCHEDULE_CARD).isPresent();
    }

    public String getDate() {
        List<String> date = driverTools.getTexts(CHECK_IN_OUT_DATE);
        String[] date1Array = date.get(0).trim().split(" ");
        String date1 = date1Array[0] + " " + date1Array[1];
        String[] date2Array = date.get(1).trim().split(" ");
        String date2 = date2Array[0] + " " + date2Array[1];
        return date1 + " - " + date2;
    }

    public String getStartDate() {
        List<String> date = driverTools.getTexts(CHECK_IN_OUT_DATE);
        return date.get(0).trim().split(" ")[1];
    }

    public String getAirlineNameFlight(){
        WebElement airlineName = driverTools.waitForElementToAppear(FLIGHT_AIRLINE_NAME).orElseThrow();
        return airlineName.getText();
    }

    public String verifyDepartureAirportName(){
        List<WebElement> cityElements = driverTools.waitForElementsToAppear(DEPARTURE_ARRIVAL_NAME).get();
        return driverTools.getText(cityElements.get(0));
    }

    public String verifyArrivalAirportName(){
        List<WebElement> cityElements = driverTools.waitForElementsToAppear(DEPARTURE_ARRIVAL_NAME).get();
        return driverTools.getText(cityElements.get(1));
    }

    public void verifyBusOrderDetail(BusSrpOneWayData busSrpData) {
        WebElement element = driverTools.waitForElementToAppear(ORDER_BUS_OPERATOR_CLASS).orElseThrow();
        driverTools.scrollIntoWebView(element);

        String operatorClass = driverTools.waitForElementToAppear(ORDER_BUS_OPERATOR_CLASS).orElseThrow().getText();
        String[] operatorClassArr = operatorClass.split(" - ");
        Assertion.assertThat("Verify bus operator is correct on order detail page", operatorClassArr[0], is(busSrpData.operatorName));
        Assertion.assertThat("Verify bus class is correct on order detail page", operatorClassArr[1], is(busSrpData.busClass.split(" ")[0]));

        String origin = driverTools.waitForElementToAppear(ORDER_BUS_ORIGIN).orElseThrow().getText();
        Assertion.assertThat("Verify bus origin is correct on order detail page", origin, is(busSrpData.origin));

        String destination = driverTools.waitForElementToAppear(ORDER_BUS_DESTINATION).orElseThrow().getText();
        Assertion.assertThat("Verify bus destination is correct on order detail page", destination, is(busSrpData.destination));
    }

    public String getCustomerCurrency(){
        WebElement customerCurrency = driverTools.scrollUntilElementFound(TOTAL_PRICE).orElseThrow();
        return driverTools.getText(customerCurrency).split(" ")[0];
    }

    public void clickShowPriceDetailButton() {
        driverTools.scrollDown(1000);
        WebElement element = driverTools.waitForElementToPresence(PRICE_DETAIL_BUTTON).orElseThrow();
        driverTools.clickSvgOrNonHtmlElements(element);
    }

    public abstract boolean isConvenienceFeeExist();

    public abstract boolean verify100pRefundPriceDetail();

    public boolean verify100pRefundManageOrder(){
        driverTools.scrollIntoWebView(MANAGE_ORDER_SECTION);
        return driverTools.waitForElementToAppear(HUNDREDREFUND_IMG_MANAGE_ORDER).isPresent();
    }

    public String verifyBusDepartureTerminal(){
        driverTools.scrollIntoWebView(DETAIL_RESERVATION_CARD);
        List<WebElement> bookingElements = driverTools.waitForElementsToAppear(BUS_RESERVATION_DETAIL).get();
        return driverTools.getText(bookingElements.get(0));
    }

    public String verifyBusArrivalTerminal(){
        driverTools.scrollIntoWebView(DETAIL_RESERVATION_CARD);
        List<WebElement> bookingElements = driverTools.waitForElementsToAppear(BUS_RESERVATION_DETAIL).get();
        return driverTools.getText(bookingElements.get(2));
    }

    public void clickEditBooking(){
        driverTools.waitForElementToAppear(EDIT_GUEST_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_GUEST_BUTTON);
    }

    public void clickEditPopUpGuest(){
        driverTools.waitForElementToAppear(EDIT_GUEST_POPUP);
        driverTools.click(EDIT_GUEST_POPUP);
    }

    public void clickEditPopUpSplReq(){
        driverTools.waitForElementToAppear(EDIT_SPECIAL_REQ_POPUP);
        driverTools.click(EDIT_SPECIAL_REQ_POPUP);
    }

    public String getSpecialReq(){
        try {
            driverTools.waitForElementToAppear(SPECIAL_REQ_TEXT);
            return driverTools.getText(SPECIAL_REQ_TEXT).orElseThrow();
        }catch (Exception e){
            return "No Special Request";
        }
    }

    public String getAddOnText(){
        try {
            driverTools.waitForElementToAppear(ADD_ON_TEXT);
            return driverTools.getText(ADD_ON_TEXT).orElseThrow();
        }catch (Exception e){
            return "No Add On";
        }
    }

    public boolean isAddOnPresent(String addOnsName) {
        return driverTools.isElementDisplayed(getAddOnsLocator(addOnsName));
    }

    private Locator getAddOnsLocator(String addOnsName) {
        return new Locator("ADDONS_NAME", By.xpath("//*[(normalize-space()='Add-ons')or(normalize-space()='Fasilitas Ekstra')]/following-sibling::*/*/*[contains(.,'" + addOnsName + "')]"));
    }

    public boolean isInsurancePresent(String insuranceName) {
        driverTools.scrollIntoWebView(getInsuranceLocator(insuranceName));
        return driverTools.isElementDisplayed(getInsuranceLocator(insuranceName));
    }

    private Locator getInsuranceLocator(String insuranceName) {
        return new Locator("INSURANCE_NAME", By.xpath("//div[contains(@class, 'styles_protection_content')]//p[contains(@class, 'Text_weight_bold') and contains(text(), '" + insuranceName + "')]"));
    }

    public boolean verifyCompletePaymentExpiryTime(){
        driverTools.switchToWebViewUrl("yourorder");
        List<String> expiryTimeComponents = driverTools.getTexts(COUNTDOWN);
        logger.log("Total elements found : " + expiryTimeComponents.size());
        Assertion.assertThat("Verify hour is in present",expiryTimeComponents.get(0), is(notNullValue()));
        Assertion.assertThat("Verify minutes is in present", expiryTimeComponents.get(1), is(notNullValue()));
        Assertion.assertThat("Verify seconds is in present", expiryTimeComponents.get(2), is(notNullValue()));
        driverTools.switchToNative();
        return true;
    }

    public String getDepartureDate(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("order");
        List<String> departureDateComponents = driverTools.getTexts(FLIGHT_DEPARTURE_DATE);
        logger.log("Flight departure date: " + departureDateComponents.get(0));
        logger.log("Flight departure date time: " + departureDateComponents.get(2));
        String depatureDate = String.join(" ",departureDateComponents).trim();
        driverTools.switchToNative();
        return depatureDate;
    }

    public String getReturnDate(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("order");
        List<String> returnDateComponents = driverTools.getTexts(FLIGHT_RETURN_DATE);
        logger.log("Flight departure date: " + returnDateComponents.get(0));
        logger.log("Flight departure date time: " + returnDateComponents.get(2));
        String returnDate = String.join(" ",returnDateComponents).trim();
        driverTools.switchToNative();
        return returnDate;
    }

    public String getOrderGroupDepartureAirline(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("order");
        String departureAirline = driverTools.getText(FLIGHT_ORDER_GROUP_DEPART_AIRLINE).orElseThrow();
        driverTools.switchToNative();
        return departureAirline;
    }

    public String getOrderGroupReturnAirline(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("order");
        String returnAirline = driverTools.getText(FLIGHT_ORDER_GROUP_RETURN_AIRLINE).orElseThrow();
        return returnAirline;
    }

    public String geDepartAirportOrderGroup(){
        driverTools.switchToWebView();
        String destinationAirportDetail = driverTools.getText(DEPART_AIRPORT_NAME_ORDER_GROUP).orElseThrow();
        driverTools.switchToNative();
        return destinationAirportDetail;
    }

    public String getReturnAirportOrderGroup(){
        driverTools.switchToWebView();
        String destinationAirportDetail = driverTools.getText(RETURN_AIRPORT_NAME_ORDER_GROUP).orElseThrow();
        driverTools.switchToNative();
        return destinationAirportDetail;
    }

    public String getTotalPaymentInFlightPreviewPage(){
        driverTools.switchToWebViewUrl("yourorder");
        String orderText = driverTools.getText(FLIGHT_ORDER_GROUP_TOTAL_PAYMENT).orElseThrow();
        driverTools.switchToNative();
        return orderText;
    }

    public void openFlightGroupPriceDetail(){
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.click(FLIGHT_ORDER_GROUP_PAYMENT_DETAIL_CHEVRON);
        driverTools.switchToNative();
    }

    public boolean verifyFlightPriceDetailBreakDown(String expectedTotalPrice){
        driverTools.switchToWebView();
        driverTools.switchToWebViewUrl("yourorder");
        List<String> priceComponentsInPriceDetail = driverTools.getTexts(PRICE_DETAIL_COMPONENTS);
        List<String> priceComponentsOtherFees = driverTools.getTexts(PRICE_DETAIL_COMPONENTS_DISCOUNT_AND_OTHER_FEES);
        List<String> allPriceComponents = new ArrayList<>();
        allPriceComponents.addAll(priceComponentsInPriceDetail);
        allPriceComponents.addAll(priceComponentsOtherFees);
        logger.log("number of price component present : " + priceComponentsInPriceDetail.size());
        String totalPrice = driverTools.getText(PRICE_DETAIL_TOTAL_PRICE_VALUE).orElseThrow();
        Assertion.assertThat("Verify total price is in IDR", totalPrice, containsString("IDR"));
        List<Double> priceComponentsNumbersValue = allPriceComponents.stream()
                .filter(n -> n.matches(".*[0-9]+.*"))
                .map(n -> CoreFunctions.convertPriceSafely(n)).toList();

        double sumOfPriceDetail = CoreFunctions.sumUpDouble(priceComponentsNumbersValue);
        Assertion.assertThat("Verify total price is the as sum of price breakdown", CoreFunctions.convertPriceSafely(totalPrice),
                is(sumOfPriceDetail));
        Assertion.assertThat("Verify total price is the same as price in payment page", CoreFunctions.convertPriceSafely(totalPrice), is(CoreFunctions.convertPriceSafely(expectedTotalPrice)));
        driverTools.click(CLOSE_PRICE_DETAIL_WEBVIEW);
        driverTools.switchToNative();
        return true;
    }

    public void clickFlightDepartOrderDetail(){
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.click(FLIGHT_ORDER_GROUP_DEPART_CHEVRON);
        driverTools.switchToNative();
    }

    public String getDepartureDateOrderDetail(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("detail");
        String departureDate = driverTools.getText(FLIGHT_DEPARTURE_DATE_DETAIL).orElseThrow();
        logger.log("Flight departure date: " + departureDate);
        driverTools.switchToNative();
        return departureDate;
    }

    public String getArrivalDateOrderDetail(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("detail");
        String departureDate = driverTools.getText(FLIGHT_ARRIVAL_DATE_DETAIL).orElseThrow();
        logger.log("Flight arrival date: " + departureDate);
        driverTools.switchToNative();
        return departureDate;
    }

    public String getOriginAirportOrderDetail(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("detail");
        String originAirportDetail = driverTools.getText(DEPART_AIRPORT_NAME_ORDER_DETAIL).orElseThrow();
        driverTools.switchToNative();
        return originAirportDetail;
    }

    public String getDestinationAirportOrderDetail(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("detail");
        String destinationAirportDetail = driverTools.getText(RETURN_AIRPORT_NAME_ORDER_DETAIL).orElseThrow();
        driverTools.switchToNative();
        return destinationAirportDetail;
    }

    public void clickBack() {
        driverTools.click(BACK_BUTTON);
    }

    public void clickFlightReturnOrderDetail(){
        //entry from order group page
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.click(FLIGHT_ORDER_GROUP_RETURN_CHEVRON);
    }

    public void clickTotalPayment() {
        driverTools.waitForElementToAppear(TOTAL_PAYMENT_PRICE_TOGGLE, 5).orElseThrow();
        driverTools.scrollElementToCenter(TOTAL_PAYMENT_PRICE_TOGGLE);
        driverTools.clickWithActions(TOTAL_PAYMENT_PRICE_TOGGLE);
    }

    public void clickCompleteThePayment() {
        driverTools.waitForElementToAppear(COMPLETE_THE_PAYMENT_BUTTON);
        driverTools.clickWithJavascriptExecutor(COMPLETE_THE_PAYMENT_BUTTON);
    }

    public void completePaymentFromOrderGroupPaymentPage(){
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.click(COMPLETE_PAYMENT_FROM_ORDER_GROUP);
    }

    public String getOrderIdFlight() {
        driverTools.waitForElementToPresence(ORDER_ID_LOCATOR_FLIGHT);
        driverTools.scrollIntoWebView(ORDER_ID_LOCATOR_FLIGHT);
        WebElement element = driverTools.waitForElementToAppear(ORDER_ID_LOCATOR_FLIGHT).orElseThrow();
        return driverTools.getText(element).split(" ")[2];
    }

    public boolean isGuestNameDisplayedOrderDetails(String guestName) {
        return driverTools.waitForElementToAppear(getGuestNameLocatorOrderDetails(guestName)).isPresent();
    }

    private Locator getGuestNameLocatorOrderDetails(String guestName) {
        return new Locator("GUEST_NAME_" + guestName, new By.ByXPath("//*[contains(@class,'card_container')]//*[text()='Guest Details' or text()='Detail Tamu']/parent::*/following-sibling::*/li[contains(.,'" + guestName + "')]"));
    }

    public boolean checkSpecialRequest(String specialRequest) {
        String separatorBookingForm = ",";
        if (specialRequest.contains(separatorBookingForm)) {
            String[] specialRequestSplit = specialRequest.split(separatorBookingForm);
            for (int i = 0; i < specialRequestSplit.length; i++) {
                logger.log("Checking index: " + i);
                // Pass the raw trimmed token through unchanged.
                // isSpecialRequestDisplayedOrderDetails() does a case-insensitive
                // XPath match, so capitalize() is unnecessary here and would
                // mangle mixed-case values such as "AutoTest-<hex>" ->
                // "Autotest-<hex>" without adding any matching power.
                String token = specialRequestSplit[i].trim();
                Assertion.assertThat(
                        "Verify special request \"" + token + "\" is displayed on order details page",
                        isSpecialRequestDisplayedOrderDetails(token), is(true));
            }
            return driverTools.isElementDisplayed(SPECIAL_REQUEST);
        }
        return isSpecialRequestDisplayedOrderDetails(specialRequest.trim());
    }

    public boolean isSpecialRequestDisplayedOrderDetails(String specialRequest) {
        // Do NOT pre-capitalize: CoreFunctions.capitalize() lowercases every
        // character after the first one, which mangles mixed-case values such
        // as "AutoTest-<hex>". Match the booked value verbatim, and let the
        // XPath itself do a case-insensitive contains() so callers can still
        // pass values in whatever case they like.
        return driverTools.waitForElementToAppear(getSpecialRequestLocatorOrderDetails(specialRequest)).isPresent();
    }

    private Locator getSpecialRequestLocatorOrderDetails(String specialRequest) {
        // Walk from the "Special Request" / "Permintaan Khusus" label down to
        // a sibling span that contains the booked value. We use:
        //   * normalize-space(text())='...'  - the label has no surrounding
        //     whitespace in current builds but this stays safe across future
        //     Next.js formatting changes.
        //   * /parent::*/following-sibling::*  - matches the section value
        //     wrapper regardless of intermediate divs (preserved from the
        //     original locator so the existing call sites in HotelsTest
        //     stay green).
        //   * //span (descendant) instead of /*/span - the value can be one
        //     or two levels deep depending on whether the section renders
        //     a bullet list (`<li><div><span>...</span></div></li>`) or a
        //     flat list (`<li><span>...</span></li>`).
        //   * translate(...)  - case-insensitive contains() so booking values
        //     like "AutoTest-..." or "abc123 Revised" match the UI text
        //     regardless of how either side is cased.
        //
        // Special-request values are user input and may contain apostrophes
        // (e.g. "Driver's lunch needed"), so the search literal is built via
        // CoreFunctions.toXPathLiteral() to keep the resulting XPath valid.
        //
        // The XPath uses translate(...) over ASCII A-Z to fold case; mirror
        // that with Locale.ROOT on the Java side so we never disagree with
        // the XPath fold under locale-sensitive JVMs (e.g. Turkish dotted/
        // dotless I).
        String lowercaseSearchLiteral = CoreFunctions.toXPathLiteral(
                specialRequest.toLowerCase(java.util.Locale.ROOT));
        return new Locator("SPECIAL_REQUEST_" + specialRequest, new By.ByXPath(
                "//*[contains(@class,'reservation_detail_list')]"
                        + "//*[normalize-space(text())='Special Request' "
                        + "or normalize-space(text())='Permintaan Khusus']"
                        + "/parent::*/following-sibling::*"
                        + "//span[contains(translate(normalize-space(.),"
                        + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),"
                        + lowercaseSearchLiteral + ")]"));
    }

    public void clickSeeRoomDetails() {
        try {
            driverTools.click(ROOM_DETAILS_BUTTON);
        } catch (Exception e) {
            logger.log("Scroll and Clicking with javascript executor due to: " + e.getMessage());
            driverTools.scrollIntoWebView(ROOM_DETAILS_BUTTON);
            driverTools.clickWithJavascriptExecutor(ROOM_DETAILS_BUTTON);
        }
    }

    public void clickSeeRoomDetails(String roomName, int index) {
        driverTools.waitForElementsToAppear(getSeeRoomDetailsLocator(roomName)).orElseThrow();
        List<WebElement> seeRoomDetailsList = driverTools.elements(getSeeRoomDetailsLocator(roomName)).orElseThrow();
        int totalSeeRoomDetails = seeRoomDetailsList.size();
        logger.log("Total see room details button found for room \"" + roomName + "\": " + totalSeeRoomDetails);
        WebElement seeRoomDetailsButton;
        if (totalSeeRoomDetails > 1) {
            logger.log("Selecting see room details button at index: " + index);
            seeRoomDetailsButton = seeRoomDetailsList.get(index);
        } else {
            logger.log("Only one see room details button found, selecting the first one.");
            seeRoomDetailsButton = seeRoomDetailsList.get(0);
        }
        try {
            driverTools.click(seeRoomDetailsButton);
        } catch (Exception e) {
            logger.log("Scroll and Clicking with javascript executor due to: " + e.getMessage());
            driverTools.scrollIntoWebView(seeRoomDetailsButton);
            driverTools.clickWithJavascriptExecutor(seeRoomDetailsButton);
        }
    }

    private Locator getSeeRoomDetailsLocator(String roomName) {
        return new Locator("SEE_ROOM_DETAILS_" + roomName, new By.ByXPath("//*[contains(.,'" + roomName + "')]//parent::*[contains(@class,'accordion_button')]//following-sibling::*[@data-testid='collapse-container']//*[contains(@class,'hotel_room_action_button')]//*[contains(., 'Lihat detail kamar') or contains(., 'See room details')]"));
    }

    public boolean isGuestNameDisplayedRoomDetails(String guestName) {
        return driverTools.waitForElementToAppear(getGuestNameLocatorRoomDetails(guestName)).isPresent();
    }

    private Locator getGuestNameLocatorRoomDetails(String guestName) {
        return new Locator("GUEST_NAME_" + guestName, new By.ByXPath("//*[contains(@class,'tab_panel_inner_container')]//*[text()='Guest Details' or text()='Detail Tamu']/parent::*/following-sibling::*/li[contains(.,'" + guestName + "')]"));
    }

    public boolean isSpecialRequestDisplayedRoomDetails(String specialRequest) {
        return driverTools.waitForElementToAppear(getSpecialRequestLocatorRoomDetails(specialRequest)).isPresent();
    }

    private Locator getSpecialRequestLocatorRoomDetails(String specialRequest) {
        return new Locator("SPECIAL_REQUEST_" + specialRequest, new By.ByXPath("//*[contains(@class,'tab_panel_inner_container')]//*[text()='Special Request' or text()='Permintaan Khusus']/parent::*/following-sibling::*/li[contains(.,'" + specialRequest + "')]"));
    }

    public void clickEditGuestName(String guestName) {
        driverTools.waitForElementToAppear(getEditGuestNameLocator(guestName)).orElseThrow();
        driverTools.click(getEditGuestNameLocator(guestName));
    }

    private Locator getEditGuestNameLocator(String guestName) {
        return new Locator("EDIT_GUEST_NAME_" + guestName, new By.ByXPath("//*[contains(@class,'tab_panel_inner_container')]//li[contains(.,'" + guestName + "')]/parent::*/preceding-sibling::*/*[text()='Edit' or text()='Ubah']"));
    }

    public void clickEditSpecialRequest(String specialRequest) {
        driverTools.waitForElementToAppear(getEditSpecialRequestLocator(specialRequest)).orElseThrow();
        driverTools.click(getEditSpecialRequestLocator(specialRequest));
    }

    private Locator getEditSpecialRequestLocator(String specialRequest) {
        return new Locator("EDIT_SPECIAL_REQUEST_" + specialRequest, new By.ByXPath("//*[contains(@class,'tab_panel_inner_container')]//li[contains(.,'" + specialRequest + "')]/parent::*/preceding-sibling::*/*[text()='Edit' or text()='Ubah']"));
    }

    public void clickReviseGuestName(String guestName) {
        driverTools.waitForElementToAppear(getReviseGuestNameLocator(guestName)).orElseThrow();
        driverTools.click(getReviseGuestNameLocator(guestName));
    }

    private Locator getReviseGuestNameLocator(String guestName) {
        return new Locator("REVISE_GUEST_NAME_" + guestName, new By.ByXPath("//*[contains(@class,'room_type_details_content') and contains(.,'" + guestName + "')]//following-sibling::*[@data-testid='revise-booking-edit-button']"));
    }

    public void clickReviseSpecialRequest(String specialRequest) {
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        homePageWeb.continueWithoutApp(5);
        driverTools.waitForElementToAppear(getReviseSpecialRequestLocator(specialRequest)).orElseThrow();
        driverTools.click(getReviseSpecialRequestLocator(specialRequest));
    }

    private Locator getReviseSpecialRequestLocator(String specialRequest) {
        return new Locator("REVISE_SPECIAL_REQUEST_" + specialRequest, new By.ByXPath("//*[contains(@class,'room_type_details_content') and contains(.,'" + specialRequest + "')]//following-sibling::*[@data-testid='revise-booking-edit-button']"));
    }

    private Locator getRoomNameRadioButtonLocator(String roomName) {
        return new Locator("ROOM_NAME_" + roomName, new By.ByXPath("//*[contains(@class,'ChooseMultiOrderItem')]//*[contains(text(),'" + roomName + "')]"));
    }

    public void clickRescheduleMultiOrderRoom(String roomName, int index) {
        driverTools.waitForElementToAppear(getRoomNameRadioButtonLocator(roomName)).orElseThrow();
        List<WebElement> roomNameRadioButtonList = driverTools.elements(getRoomNameRadioButtonLocator(roomName)).orElseThrow();
        int totalRadioButton = roomNameRadioButtonList.size();
        logger.log("Total room name radio button found for room \"" + roomName + "\": " + totalRadioButton);
        WebElement roomNameRadioButton;
        if (totalRadioButton > 1) {
            logger.log("Selecting room name radio button at index: " + index);
            roomNameRadioButton = roomNameRadioButtonList.get(index);
        } else {
            logger.log("Only one room name radio button found, selecting the first one.");
            roomNameRadioButton = roomNameRadioButtonList.get(0);
        }
        driverTools.click(roomNameRadioButton);
        driverTools.click(CONTINUE_REFUND_MULTI_ORDER);
    }

    private Locator getRoomRescheduledBannerLocator(String roomName) {
        return new Locator("ROOM_RESCHEDULED_BANNER_" + roomName, new By.ByXPath("//*[contains(text(),'" + roomName + "')]//ancestor::*[contains(@class,'card_container')]//*[contains(@class,'NotificationBanner_title')]"));
    }

    public boolean isRoomRescheduledBannerDisplayed(String roomName) {
        return driverTools.waitForElementToAppear(getRoomRescheduledBannerLocator(roomName)).isPresent();
    }

    public void clickRefundMultiOrderRoom(){
        List<WebElement> totalRoom = driverTools.waitForElementsToAppear(CHOOSE_ROOM_HOTEL_MULTI_ORDER).get();
        logger.log("Total room: " + totalRoom.size());
        driverTools.click(totalRoom.get(0));
        driverTools.click(CONTINUE_REFUND_MULTI_ORDER);
    }

    public boolean isPopUpNonRefundableShown(){
        return driverTools.waitForElementToAppear(POP_UP_REFUND_RESCHEDULE).isPresent();
    }

    public void closeRefundReschedulePopUp(){
        driverTools.click(POP_UP_REFUND_RESCHEDULE_BUTTON);
    }

    public String checkForCarRentalPickUpLocation(){
        driverTools.waitForElementToAppear(CAR_PICKUP_LOCATION).orElseThrow();
        return driverTools.getText(CAR_PICKUP_LOCATION).orElseThrow();
    }
    public String checkForCarRentalDropOffLocation(){
        driverTools.waitForElementToAppear(CAR_DROP_OFF_LOCATION).orElseThrow();
        return driverTools.getText(CAR_DROP_OFF_LOCATION).orElseThrow();
    }

    public boolean verifyAtSummaryDetail(String pickupPlace, String dropPlace) {

        String pickupTitle = "";
        String pickupText = "";
        String dropOffTitle = "";
        String dropOffText = "";
        int maxAttempts = 10;
        int attempt = 0;

        driverTools.click(SUMMARY_DETAIL_BUTTON);
        while (pickupTitle.isEmpty() || pickupText.isEmpty() || dropOffTitle.isEmpty() || dropOffText.isEmpty() && attempt < maxAttempts) {
            List<WebElement> titleElements = driverTools.elements(PICKUP_DROPOFF_TITLE_MODAL).get();
            List<WebElement> textElements = driverTools.elements(PICKUP_DROPOFF_TEXT_MODAL).get();
            if (titleElements.size() >= 2 && textElements.size() >= 2) {
                pickupTitle = driverTools.getText(titleElements.get(0));
                pickupText = driverTools.getText(textElements.get(0));
                dropOffTitle = driverTools.getText(titleElements.get(1));
                dropOffText = driverTools.getText(textElements.get(1));
            } else {
                logger.log("Insufficient elements found. Title elements: " + titleElements.size() + ", Text elements: " + textElements.size());
            }
            attempt++;
        }

        String pickupPlaceData = pickupTitle + ", " + pickupText;
        String dropPlaceData = dropOffTitle + ", " + dropOffText;
        boolean boolPickUpPlace = pickupPlaceData.equalsIgnoreCase(pickupPlace);
        Assertion.assertThat("Check pick up place", pickupPlaceData.equalsIgnoreCase(pickupPlace), is(true));
        boolean boolDropOffPlace = dropPlaceData.equalsIgnoreCase(dropPlace);
        Assertion.assertThat("Check drop off place", dropPlaceData.equalsIgnoreCase(dropPlace), is(true));
        return boolPickUpPlace && boolDropOffPlace;
    }

    public void openRedemptionStatus() {
        driverTools.click(SEE_REDEMPTION_STATUS_BUTTON);
    }

    public String getTicketId(int index) {
        driverTools.dummyWait(1);
        return driverTools.getText(getTicketIdLocatorByIndex(index)).orElseThrow().split(": ")[1];
    }

    private Locator getTicketIdLocatorByIndex(int index) {
        return new Locator("TICKET_ID_"+index, new By.ByXPath("(//div[contains(@class,'ETicket_individual_ticket_wrapper')]/span)[" + index + "]"));
    }
    public boolean isEticketDisplayed(){
        driverTools.waitForElementToAppear(ETICKET_CARD);
        WebElement eTicketHeaderButton = driverTools.waitForElementToAppear(ETICKET_CARD).orElseThrow();
        return driverTools.isElementDisplayed(eTicketHeaderButton);
    }

    public void checkDepartureTrainDetails(String departureWhoosh, String originStation, String destinationStation) {
        driverTools.waitForElementToAppear(DEPART_WHOOSH_NAME);
        String depWhooshOnOrderDetailPage = driverTools.getText(DEPART_WHOOSH_NAME).orElseThrow();

        driverTools.waitForElementToAppear(DEPARTURE_ORIGIN_STATION);
        String departureOriginStationOnODP = driverTools.getText(DEPARTURE_ORIGIN_STATION).orElseThrow();

        String departureDestinationStationOnODP = driverTools.getText(DEPARTURE_DESTINATION_STATION).orElseThrow();

        assertThat("Departure whoosh does not match", depWhooshOnOrderDetailPage, containsString(departureWhoosh));
        assertThat("Origin station does not match", departureOriginStationOnODP, containsString(originStation));
        assertThat("Destination station does not match", departureDestinationStationOnODP, containsString(destinationStation));
    }

    public abstract void checkReturnTrainDetails(String returnWhoosh, String destinationStation, String originStation);

    public abstract boolean verifyManageOrderContainer();

    public abstract List<String> verifyAdultName(int adultCount, int infantCount);

    public abstract List<String> verifyInfantName(int adultCount, int infantCount);

    public abstract List<String> verifyPassengerIDs(int adultCount, int infantCount);

    public String verifyDepartureDate() {
        driverTools.scrollIntoWebViewMWeb(DEP_DATE);
        driverTools.waitForElementToAppear(DEP_DATE);

        String fullText = driverTools.getText(DEP_DATE).orElseThrow();
        String datePart = fullText.split("-")[0].trim();

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("E, dd MMM yyyy", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(datePart, inputFormatter);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("E, d MMM yyyy", Locale.ENGLISH);
        return date.format(outputFormatter);
    }

    public boolean trainBarCode() {
        driverTools.waitForElementToAppear(TRAIN_BAR_CODE);
        return driverTools.isElementDisplayed(TRAIN_BAR_CODE);
    }

    public String getFinalPriceOrderDetail() {
        WebElement finalPriceTextElement = driverTools.waitForElementToAppear(FINAL_PRICE).orElseThrow();
        String finalPriceText = driverTools.getText(finalPriceTextElement);
        String finalPriceFormatted = String.valueOf(CoreFunctions.convertPriceSafely(finalPriceText));
        logger.log("Final price formatted: " + finalPriceFormatted);
        return finalPriceFormatted;
    }

    public void clickShareEticket(){
        driverTools.waitForElementToAppear(SHARE_ETICKET_BUTTON);
        driverTools.click(SHARE_ETICKET_BUTTON);
    }

    public boolean shareEticketWithEmail(String email){
        driverTools.sendKeys(EMAIL_ADDRESS_FIELD, email);
        driverTools.hideKeyboard();
        driverTools.clickWithJavascriptExecutor(SHARE_NOW_BUTTON);
        driverTools.dummyWait(3);
        return driverTools.waitForElementToPresence(SNACKBAR_SHARE_ETICKET).isPresent();
    }

    public abstract void  refundButtonInweb();

    public void clickOnEditBooking() {
        String revise_name = CoreFunctions.randomName()+" TestAutomation";

        driverTools.scrollUntilElementFound(EDIT_BOOKING_BUTTON, 700, 2, 5);
        driverTools.clickWithJavascriptExecutor(EDIT_BOOKING_BUTTON);

        driverTools.waitForElementToAppear(CLICK_ON_EDIT_GUEST_BUTTON);
        driverTools.clickWithJavascriptExecutor(CLICK_ON_EDIT_GUEST_BUTTON);

        driverTools.waitForElementToAppear(CLICK_INPUT_TO_REVISE_NAME);
        driverTools.clickWithJavascriptExecutor(CLICK_INPUT_TO_REVISE_NAME);
        driverTools.clearInput(CLICK_INPUT_TO_REVISE_NAME);
        driverTools.sendKeys(CLICK_INPUT_TO_REVISE_NAME, revise_name);

        driverTools.waitForElementToAppear(SAVE_AFTER_REVISE);
        driverTools.clickWithJavascriptExecutor(SAVE_AFTER_REVISE);

        driverTools.waitForElementToAppear(CONFIRM_AFTER_SAVE_REVISE);
        driverTools.click(CONFIRM_AFTER_SAVE_REVISE);
    }

    public String getGuestNameFromUI() {
        WebElement guestNameElement = driverTools.waitForElementToAppear(GUEST_NAME).orElseThrow();
        String rawText = driverTools.getText(guestNameElement);

        return rawText.replaceAll("^\\d+\\.\\s*", "").trim();
    }


    public void verifyGuestNameEditBooking(String newName, String revisedBookingOtp, int retry) throws FailedLoginException, InterruptedException {
        driverTools.scrollUntilElementFound(EDIT_BOOKING_BUTTON, 700, 2, 5);
        driverTools.clickWithJavascriptExecutor(EDIT_BOOKING_BUTTON);

        driverTools.waitForElementToAppear(CLICK_ON_EDIT_GUEST_BUTTON);
        driverTools.clickWithJavascriptExecutor(CLICK_ON_EDIT_GUEST_BUTTON);

        driverTools.waitForElementToAppear(CLICK_INPUT_TO_REVISE_NAME);
        driverTools.clickWithJavascriptExecutor(CLICK_INPUT_TO_REVISE_NAME);
        driverTools.clearInput(CLICK_INPUT_TO_REVISE_NAME);
        driverTools.sendKeys(CLICK_INPUT_TO_REVISE_NAME, newName);

        driverTools.waitForElementToAppear(SAVE_AFTER_REVISE);
        driverTools.clickWithJavascriptExecutor(SAVE_AFTER_REVISE);

        driverTools.waitForElementToAppear(CONFIRM_AFTER_SAVE_REVISE);
        driverTools.click(CONFIRM_AFTER_SAVE_REVISE);

        if (driverTools.waitForElementToAppear(OTP_TEXTFIELD1, 15).isEmpty()) {
            throw new DriverToolsException("OTP fields did not appear after confirming revise booking");
        }

        inputOTPCode(revisedBookingOtp, retry);
    }

    public void verifyCrossMark() {
        driverTools.scrollUntilElementFound(EDIT_BOOKING_BUTTON, 700, 2, 5);
        driverTools.clickWithJavascriptExecutor(EDIT_BOOKING_BUTTON);

        driverTools.waitForElementToBeClickable(CLOSE_MODAL_BUTTON, 5).orElseThrow();
        driverTools.click(CLOSE_MODAL_BUTTON);

        Assertion.assertThat(
                "Edit Booking modal should be closed after clicking the close icon",
                driverTools.waitForElementToDisappear(CLOSE_MODAL_BUTTON, 5),
                is(true)
        );
    }

    public void clickSeeReservationDetail(){
        driverTools.waitForElementToAppear(SEE_RESERVATION_DETAILS_BUTTON);
        driverTools.clickWithJavascriptExecutor(SEE_RESERVATION_DETAILS_BUTTON);
    }

    public void verifyRevisedGuestName(String revisedName) {
        String actualRevisedName = getGuestNameFromUI();
        Assertion.assertThat(
                "Verify guest name is successfully updated on the UI",
                actualRevisedName,
                is(revisedName)
        );
    }

    public void clickOnEditBookingForSpecialRequest() {
        String revise_special_request = CoreFunctions.randomName()+" TestAutomation";

        driverTools.scrollUntilElementFound(EDIT_BOOKING_BUTTON, 700, 2, 5);
        driverTools.clickWithJavascriptExecutor(EDIT_BOOKING_BUTTON);

        driverTools.waitForElementToAppear(CLICK_ON_EDIT_GUEST_BUTTON_2);
        driverTools.clickWithJavascriptExecutor(CLICK_ON_EDIT_GUEST_BUTTON_2);

        driverTools.waitForElementToAppear(CLICK_INPUT_TO_REVISE_SPECIAL_REQUEST);
        driverTools.clickWithJavascriptExecutor(CLICK_INPUT_TO_REVISE_SPECIAL_REQUEST);
        driverTools.clearInput(CLICK_INPUT_TO_REVISE_SPECIAL_REQUEST);
        driverTools.sendKeys(CLICK_INPUT_TO_REVISE_SPECIAL_REQUEST, revise_special_request);

        driverTools.waitForElementToAppear(SAVE_AFTER_REVISE);
        driverTools.clickWithJavascriptExecutor(SAVE_AFTER_REVISE);

        driverTools.waitForElementToAppear(CONFIRM_AFTER_SAVE_REVISE);
        driverTools.click(CONFIRM_AFTER_SAVE_REVISE);
    }

    public void inputOTPCode(String otpRevisedBooking, int retryCounter) throws InterruptedException, FailedLoginException {
        if (driverTools.isElementDisplayed(OTP_TEXTFIELD1)) {
            driverTools.waitForElementToBeClickable(OTP_TEXTFIELD1, 3).orElseThrow();
            driverTools.sendKeys(OTP_TEXTFIELD1, otpRevisedBooking.substring(0,1));
            driverTools.sendKeys(OTP_TEXTFIELD2, otpRevisedBooking.substring(1,2));
            driverTools.sendKeys(OTP_TEXTFIELD3, otpRevisedBooking.substring(2,3));
            driverTools.sendKeys(OTP_TEXTFIELD4, otpRevisedBooking.substring(3));
        }else {
            retryCounter++;
            Thread.sleep(3000);
            inputOTPCode(otpRevisedBooking, retryCounter);
        }
    }

    public boolean validateCompletePaymentButtonExist() {
        return driverTools.isElementPresent(COMPLETE_THE_PAYMENT);
    }

    public boolean successFullRevisedname(){
        driverTools.waitForElementToAppear(SUCCESSFULLY_REVISED);
        return driverTools.isElementDisplayed(SUCCESSFULLY_REVISED);

    }

    public abstract String switchTabAndgetURL();

    public boolean validateRedeemAndSeeRedemptionStatusButtonExistAndClickable() {
        return driverTools.isElementPresent(REDEEM_YOUR_E_TICKET_BUTTON) && driverTools.isElementPresent(SEE_REDEMPTION_STATUS_BUTTON)
                && driverTools.isElementClickable(REDEEM_YOUR_E_TICKET_BUTTON, 3) && driverTools.isElementClickable(SEE_REDEMPTION_STATUS_BUTTON, 3);
    }

    public boolean isCorrectOrderIssued(String packageName){
        driverTools.waitForElementToAppear(ORDER_DETAIL_PACKAGE_NAME);
        String actualPackageName = driverTools.getText(ORDER_DETAIL_PACKAGE_NAME).orElse("");
        logger.log("Expected package: " + packageName + ", Actual package: " + actualPackageName);
        return actualPackageName.contains(packageName);

    }

    public String getGuestNameMO(String roomName) {
        return getGuestNameMO(roomName, 1);
    }

    public String getGuestNameMO(String roomName, int guestIndex) {
        Locator guestName = getGuestNamesByRoomName(roomName, guestIndex);
        WebElement nameElement = driverTools.waitForElementToAppear(guestName).orElseThrow();
        String nameUI = driverTools.getText(nameElement).replaceFirst("^\\d+\\.\\s*", "").trim();
        logger.log("Guest " + guestIndex + " name on UI: " + nameUI);
        return nameUI;
    }

    private static Locator getGuestNamesByRoomName(String roomName) {
        return getGuestNamesByRoomName(roomName, 1);
    }

    private static Locator getGuestNamesByRoomName(String roomName, int guestIndex) {
        return new Locator("GUEST_NAMES_FOR_ROOM", new By.ByXPath("(//div[contains(@class, 'styles_room_name__')]//span[contains(text(), '" + roomName + "')]/ancestor::div[contains(@class, 'styles_accordion_button')]/following-sibling::div[contains(@class, 'collapse')]//ul[contains(@class, 'styles_content_list')]//li)[" + guestIndex + "]"));
    }

    public void verifyAllGuestNameBasedOnRoomName(Map<String,List<String>> roomNameToGuestNameMap){
        List<Map.Entry<String, List<String>>> roomEntries = new ArrayList<>(roomNameToGuestNameMap.entrySet());

        for (Map.Entry<String, List<String>> roomEntry : roomEntries) {
            String roomName = roomEntry.getKey();
            List<String> guestsInRoom = roomEntry.getValue();

            // Verify each guest in this room
            for (int i = 0; i < guestsInRoom.size(); i++) {
                String expectedGuestName = guestsInRoom.get(i);
                String actualGuestNameFromUI = getGuestNameMO(roomName, i + 1); // XPath is 1-indexed

                Assertion.assertThat(
                        "Guest '" + expectedGuestName + "' in room '" + roomName + "' is displayed correctly",
                        actualGuestNameFromUI.toLowerCase(),
                        containsString(expectedGuestName.toLowerCase())
                );

                logger.log("Verified guest " + (i+1) + ": " + expectedGuestName + " in room: " + roomName);
            }
        }
    }

    public void verifyOrderUpdate() {
        driverTools.waitForElementToAppear(ORDER_UPDATE_SECTION);
        Assertion.assertThat("Refund order update is shown", driverTools.getText(ORDER_UPDATE_TITLE).orElseThrow(), is("Refund"));
        Assertion.assertThat("Refund order update status is shown", driverTools.getText(ORDER_UPDATE_STATUS).orElseThrow(), in(Arrays.asList("In process", "Diproses", "Completed", "Selesai")));
    }


    public void verifyRefundedRoomInOrderUpdate(String expectedRoomName) {
        driverTools.waitForElementToAppear(ORDER_UPDATE_SECTION);
        String actualRoomName = driverTools.getText(ORDER_UPDATE_ROOM_DESCRIPTION).orElse("");
        logger.log("Expected refunded room: " + expectedRoomName);
        logger.log("Actual refunded room in Order Update: " + actualRoomName);
        Assertion.assertThat("Refunded room name should be displayed in Order Update section", actualRoomName.toLowerCase(), containsString(expectedRoomName.toLowerCase()));

    }

    public void goToRefundDetails() {
        driverTools.waitForElementToAppear(ORDER_UPDATE_SEE_DETAILS).ifPresent(WebElement::click);
    }

    public boolean isRefundNotAvailablePopupDisplayed() {
        driverTools.waitForElementToAppear(REFUND_NOT_AVAILABLE_POPUP, 10);
        return driverTools.isElementDisplayed(REFUND_NOT_AVAILABLE_POPUP);
    }

    public boolean waitForRefundNotAvailablePopupToDisappear() {
        return driverTools.waitForElementToDisappear(REFUND_NOT_AVAILABLE_POPUP, 10);
    }

    public boolean isRefundNotAvailableTitleDisplayed() {
        return driverTools.isElementDisplayed(REFUND_NOT_AVAILABLE_TITLE);
    }

    public boolean isRefundNotAvailableDescriptionDisplayed() {
        return driverTools.isElementDisplayed(REFUND_NOT_AVAILABLE_DESCRIPTION);
    }

    public String getRefundNotAvailableDescriptionText() {
        driverTools.waitForElementToAppear(REFUND_NOT_AVAILABLE_DESCRIPTION, 10);
        return driverTools.getText(REFUND_NOT_AVAILABLE_DESCRIPTION).orElse("");
    }

    public boolean isRefundNotAvailableRescheduleButtonDisplayed() {
        return driverTools.isElementDisplayed(REFUND_NOT_AVAILABLE_RESCHEDULE_BUTTON);
    }

    public void clickRefundNotAvailableBackButton() {
        driverTools.waitForElementToAppear(REFUND_NOT_AVAILABLE_BACK_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(REFUND_NOT_AVAILABLE_BACK_BUTTON);
    }

    /**
     * Parses guest text from UI and extracts total guests, adults, and children
     * Supports both English and Indonesian formats
     * Examples: "2 guests (1 adult 1 child)", "2 tamu (1 dewasa 1 anak)", "1 guest (1 adult)"
     *
     * @param roomGuestText The text from UI
     * @return Map containing "totalGuests", "adults", "children"
     */
    private Map<String, Integer> parseGuestDetails(String roomGuestText) {
        Map<String, Integer> guestMap = new HashMap<>();

        logger.log("Parsing guest text: " + roomGuestText);

        try {
            // Extract all numbers from the text
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(roomGuestText);

            List<Integer> numbers = new ArrayList<>();
            while (matcher.find()) {
                numbers.add(Integer.parseInt(matcher.group()));
            }

            if (numbers.size() >= 2) {
                // Format: "2 guests (1 adult 1 child)" or "2 tamu (1 dewasa 1 anak)"
                guestMap.put("totalGuests", numbers.get(0));
                guestMap.put("adults", numbers.get(1));
                guestMap.put("children", numbers.size() >= 3 ? numbers.get(2) : 0);
            } else if (numbers.size() == 1) {
                // Format: Single guest, assume adult
                guestMap.put("totalGuests", numbers.get(0));
                guestMap.put("adults", numbers.get(0));
                guestMap.put("children", 0);
            } else {
                // No numbers found
                logger.log("Could not parse guest details from text: " + roomGuestText);
                guestMap.put("totalGuests", 0);
                guestMap.put("adults", 0);
                guestMap.put("children", 0);
            }

            logger.log("Parsed guest details - Total: " + guestMap.get("totalGuests") +
                    ", Adults: " + guestMap.get("adults") +
                    ", Children: " + guestMap.get("children"));

        } catch (Exception e) {
            logger.log("Error parsing guest details: " + e.getMessage());
            guestMap.put("totalGuests", 0);
            guestMap.put("adults", 0);
            guestMap.put("children", 0);
        }

        return guestMap;
    }

    public Map<String,Integer> getGuestDetailUI() {
        WebElement roomGuestContainer = driverTools.waitForElementToAppear(ROOM_GUEST_COUNT).orElseThrow();
        // 2 tamu (1 dewasa 1 anak) or 2 guests (1 adult 1 child) or 1 guest (1 adult) or .....
        String roomGuestText =  driverTools.getText(roomGuestContainer);
        logger.log("Room guest text from UI: " + roomGuestText);
        return parseGuestDetails(roomGuestText);
    }

    public abstract void clickBookAgainOrRefund();

    public void clickReceiptButton() {
        driverTools.scrollIntoWebView(RECEIPT_BUTTON);
        driverTools.waitForElementToPresence(RECEIPT_BUTTON, 20);
        driverTools.clickWithJavascriptExecutor(RECEIPT_BUTTON);
        logger.log("Clicked on Receipt button");
    }

    public abstract void openETicket();
    public abstract void openReceipt();
    public abstract void verifyCurrencyOnReceipt(String expectedCurrency);
    public abstract void verifyOrderIDOnReceipt(String expectedOrderID);

    public void verifyCurrencyOnOrderDetail(String expectedCurrency) {
        String actualCurrency = getCustomerCurrency();
        logger.log("Expected currency: " + expectedCurrency + ", Actual currency: " + actualCurrency);
        Assertion.assertThat("Currency on order detail should match expected currency", actualCurrency, is(expectedCurrency));
    }

    public String getPDFContentFile(){
        driverTools.switchToNative();
        OrderDetailPage<?> orderDetailPageApp = OrderDetailPage.getInstance(session);
        String data = orderDetailPageApp.getPDFContentFile();
        logger.log("PDF content: " + data);
        driverTools.switchToWebView();
        return data;
    }

    public String getFullBlackoutDate(String startBlackoutDate, String endBlackoutDate) {
        return CoreFunctions.getFullBlackoutDate(startBlackoutDate, endBlackoutDate);
    }

    public boolean isClaimCenterButtonDisplayed() {
        return driverTools.waitForElementToAppear(INSURANCE_CLAIM_CENTER_BUTTON).isPresent();
    }

    public String clickClaimCenterButtonAndGetUrl() {
        return clickAndGetRedirectUrl(INSURANCE_CLAIM_CENTER_BUTTON, true);
    }

    public String getInsuranceProductName() {
        WebElement element = driverTools.waitForElementToAppear(INSURANCE_PRODUCT_NAME).orElseThrow();
        return driverTools.getText(element).trim();
    }

    public boolean isSeePolicyAndCoverageButtonDisplayed() {
        return driverTools.waitForElementToAppear(SEE_POLICY_AND_COVERAGE_BUTTON).isPresent();
    }

    public String clickSeePolicyAndCoverageButtonAndGetUrl() {
        return clickAndGetRedirectUrl(SEE_POLICY_AND_COVERAGE_BUTTON, true);
    }

    public String getRelatedOrderVerticalName() {
        WebElement element = driverTools.waitForElementToAppear(RELATED_ORDER_VERTICAL_NAME).orElseThrow();
        return driverTools.getText(element).trim();
    }

    public void clickRelatedOrder() {
        driverTools.scrollIntoWebView(RELATED_ORDER_ITEM);
        driverTools.waitForElementToBeClickable(RELATED_ORDER_ITEM).orElseThrow();
        int handlesBefore = driver.getWindowHandles().size();
        driverTools.clickWithJavascriptExecutor(RELATED_ORDER_ITEM);
        driverTools.waitForWebToLoad();
        if (driver.getWindowHandles().size() > handlesBefore) {
            driverTools.switchToLatestTab();
        }
        driverTools.waitForElementToAppear(LOADING_INDICATOR);
        driverTools.waitForElementToDisappear(LOADING_INDICATOR);
        driverTools.waitForWebToLoad();
        hideTravelDealBannerIfPresent();
    }

    public boolean isAddOnsPopupDisplayed() {
        return driverTools.waitForElementToAppear(ADDONS_POPUP, 10).isPresent();
    }

    public boolean isReceiptButtonDisplayed() {
        driverTools.scrollIntoWebView(RECEIPT_BUTTON);
        return driverTools.waitForElementToAppear(RECEIPT_BUTTON).isPresent();
    }

    public int getPageModuleViewCount() {
        driverTools.scrollToBottomOfPage();
        return driverTools.elements(PAGE_MODULE_VIEWS).map(List::size).orElse(0);
    }

    private String clickAndGetRedirectUrl(Locator locator, boolean closeTabAfterRedirect) {
        String originalHandle = driver.getWindowHandle();
        int handlesBefore = driver.getWindowHandles().size();
        driverTools.scrollIntoWebView(locator);
        driverTools.waitForElementToBeClickable(locator).orElseThrow();
        driverTools.clickWithJavascriptExecutor(locator);
        driverTools.waitForWebToLoad();

        if (driver.getWindowHandles().size() > handlesBefore) {
            String url = driverTools.tabSwitch("insurance-claim");
            if (url.isEmpty()) {
                url = driverTools.tabSwitch("yourorder/detail");
            }
            if (url.isEmpty()) {
                url = driverTools.getCurrentURL();
            }
            if (closeTabAfterRedirect) {
                driverTools.closeCurrentTab();
                driverTools.switchToWindow(originalHandle);
            }
            return url;
        }
        driverTools.waitForElementToAppear(LOADING_INDICATOR);
        driverTools.waitForElementToDisappear(LOADING_INDICATOR);
        driverTools.waitForWebToLoad();
        return driverTools.getCurrentURL();
    }

    public void clickYourItinerariesTab() {
        driverTools.waitForElementToAppear(TAB_YOUR_ITINERARIES, 10);
        driverTools.clickWithJavascriptExecutor(TAB_YOUR_ITINERARIES);
    }

    public void clickActiveOrdersTab() {
        driverTools.waitForElementToAppear(TAB_ACTIVE_ORDERS, 10);
        driverTools.clickWithJavascriptExecutor(TAB_ACTIVE_ORDERS);
    }

    public boolean isYourItinerariesTabActive() {
        WebElement button = driverTools.waitForElementToAppear(TAB_YOUR_ITINERARIES).orElseThrow();
        String className = button.getAttribute("class");
        return className != null && className.contains("highEmphasis");
    }

    public void toggleTripHeader() {
        driverTools.waitForElementToAppear(TRIP_HEADER_HEADING, 10);
        driverTools.clickWithJavascriptExecutor(TRIP_HEADER_HEADING);
    }

    public boolean isTripSectionExpanded() {
        WebElement header = driverTools.waitForElementToAppear(TRIP_HEADER_HEADING).orElseThrow();
        String expanded = header.getAttribute("aria-expanded");
        return "true".equalsIgnoreCase(expanded);
    }

    public void ensureTripSectionExpanded() {
        if (!isTripSectionExpanded()) {
            toggleTripHeader();
        }
    }

    /**
     * Verifies if a trip order card with the given heading/title exists within the expanded trip group.
     *
     * @param titleKeyword The expected card title (e.g., Hotel Name, TTD Product Name, or Train Station/Vendor)
     * @return true if a card heading contains the titleKeyword, false otherwise
     */
    public boolean isOrderCardPresentInTripView(String titleKeyword) {
        ensureTripSectionExpanded();
        driverTools.dummyWait(10);
        List<WebElement> cards = driverTools.elements(TRIP_ORDER_CARD).orElse(Collections.emptyList());
        for (WebElement card : cards) {
            try {
                WebElement titleElement = card.findElement(By.xpath(".//*[contains(@class,'TripOrderCard_title')]"));
                logger.log("title element " + titleElement);

                String actualTitle = titleElement.getText();
                logger.log("actual  title " + actualTitle);
                if (actualTitle != null && actualTitle.toLowerCase().contains(titleKeyword.toLowerCase())) {
                    logger.log("Found matching trip order card heading: " + actualTitle);
                    return true;
                }
            } catch (Exception ignored) {
                // Ignore cards without title elements
            }
        }
        logger.log("Trip order card heading containing '" + titleKeyword + "' was not found in trip view.");
        return false;
    }

    private WebElement getTripCardByTitleKeyword(String titleKeyword) {
        ensureTripSectionExpanded();
        List<WebElement> cards = driverTools.elements(TRIP_ORDER_CARD).orElse(Collections.emptyList());
        for (WebElement card : cards) {
            try {
                WebElement titleElement = card.findElement(By.xpath(".//*[contains(@class,'TripOrderCard_title')]"));
                if (titleElement.getText().toLowerCase().contains(titleKeyword.toLowerCase())) {
                    return card;
                }
            } catch (Exception ignored) {}
        }
        throw new NoSuchElementException("Trip order card containing title '" + titleKeyword + "' was not found.");
    }

    public boolean verifyTripCardData(String titleKeyword, String expectedSubtitleSubstring, String expectedBeneficiarySubstring) {
        WebElement card = getTripCardByTitleKeyword(titleKeyword);

        String title = card.findElement(By.xpath(".//*[contains(@class,'TripOrderCard_title')]")).getText();
        String subtitle = card.findElement(By.xpath(".//*[contains(@class,'TripOrderCard_subtitle')]")).getText();
        String beneficiary = card.findElement(By.xpath(".//*[contains(@class,'TripOrderCard_beneficiary_names')]")).getText();

        boolean isTitleMatch = title.toLowerCase().contains(titleKeyword.toLowerCase());
        boolean isSubtitleMatch = true;

        logger.log("Verifying Card -> UI Title: '" + title + "' | UI Subtitle: '" + subtitle + "' | UI Beneficiary: '" + beneficiary + "'");
        logger.log("Verifying expected details  -> UI Title: '" + titleKeyword + "' | UI Subtitle: '" + expectedSubtitleSubstring + "' | UI Beneficiary: '" + expectedBeneficiarySubstring + "'");

        if (expectedSubtitleSubstring != null && !expectedSubtitleSubstring.isEmpty()) {
            if (expectedSubtitleSubstring.matches("\\d{4}-\\d{2}-\\d{2}")) {
                LocalDate date = LocalDate.parse(expectedSubtitleSubstring);
                String day = String.valueOf(date.getDayOfMonth());
                String monthLong = date.format(DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH));
                String monthShort = date.format(DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH));
                String year = String.valueOf(date.getYear());

                boolean hasDay = subtitle.contains(day);
                boolean hasMonth = subtitle.contains(monthLong) || subtitle.contains(monthShort);
                boolean hasYear = subtitle.contains(year);

                isSubtitleMatch = hasDay && hasMonth && hasYear;

                // Detailed logging for date matching
                logger.log(String.format("Date Match Breakdown -> Expected: %s | Day(%s): %b | Month(%s/%s): %b | Year(%s): %b",
                        expectedSubtitleSubstring, day, hasDay, monthLong, monthShort, hasMonth, year, hasYear));
            } else {
                isSubtitleMatch = subtitle.toLowerCase().contains(expectedSubtitleSubstring.toLowerCase());
                logger.log("Substring Match -> Expected: '" + expectedSubtitleSubstring + "' | Found: " + isSubtitleMatch);
            }
        }

        boolean isBeneficiaryMatch = expectedBeneficiarySubstring == null || beneficiary.toLowerCase().contains(expectedBeneficiarySubstring.toLowerCase());

        logger.log("Final Match Results -> Title: " + isTitleMatch + " | Subtitle: " + isSubtitleMatch + " | Beneficiary: " + isBeneficiaryMatch);

        return isTitleMatch && isSubtitleMatch && isBeneficiaryMatch;
    }

    public void clickCardViewETicketByTitle(String titleKeyword) {
        WebElement card = getTripCardByTitleKeyword(titleKeyword);
        WebElement eTicketBtn = card.findElement(By.xpath(".//*[contains(@class,'TripOrderActions_label') and (text()='View e-ticket' or text()='Lihat e-tiket')]/ancestor::div[@role='button']"));
        driverTools.scrollIntoWebView(eTicketBtn);
        eTicketBtn.click();
    }

    public void clickTripOrderCard(String titleKeyword) {
        WebElement card = getTripCardByTitleKeyword(titleKeyword);
        WebElement pressableSummary = card.findElement(By.xpath(".//*[contains(@class,'TripOrderCard_summary')]"));
        driverTools.scrollIntoWebView(pressableSummary);
        pressableSummary.click();
    }

    public void clickSeeAllOrdersInTripView() {
        driverTools.scrollIntoWebView(SEE_ALL_ORDERS_BUTTON);
        driverTools.waitForElementToBeClickable(SEE_ALL_ORDERS_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEE_ALL_ORDERS_BUTTON);
    }

    public boolean isLowProfileCrossSellBannerDisplayed() {
        ensureTripSectionExpanded();
        return driverTools.waitForElementToAppear(CROSS_SELL_BANNER, 5).isPresent();
    }

    public boolean isHighProfileCrossSellBannerDisplayed() {
        ensureTripSectionExpanded();
        return driverTools.waitForElementToAppear(HIGH_PROFILE_CROSS_SELL, 5).isPresent();
    }

    public boolean isTrainOrderCardPresentInTripView(String stationKeyword) {
        ensureTripSectionExpanded();
        List<WebElement> cards = driverTools.elements(TRIP_ORDER_CARD).orElse(Collections.emptyList());
        for (WebElement card : cards) {
            try {
                // Train locations are in the timeline texts, so checking the full card text is safe
                if (card.getText().toLowerCase().contains(stationKeyword.toLowerCase())) {
                    logger.log("Found matching train order card containing station: " + stationKeyword);
                    return true;
                }
            } catch (Exception ignored) {
            }
        }
        logger.log("Train order card containing '" + stationKeyword + "' was not found in trip view.");
        return false;
    }

    /**
     * Verifies the TTD e-ticket page opened in the same tab, then navigates back to Itineraries.
     */
    public boolean verifyTTDETicketAndGoBack() {
        boolean isTitleDisplayed = driverTools.waitForElementToAppear(TTD_E_VOUCHER_TITLE, 15).isPresent();
        driver.navigate().back(); // Go back to the Trip View/Itineraries page
        driverTools.waitForWebToLoad();
        return isTitleDisplayed;
    }

    /**
     * Verifies an e-ticket (Train/Hotel) opened in a new tab, closes it, and switches back.
     */
    public boolean verifyETicketInNewTabAndClose() {
        String mainWindowHandle = driver.getWindowHandle();
        int existingHandles = driver.getWindowHandles().size();

        try {
            driverTools.waitForNewTabAndSwitchToLatest(existingHandles);
        } catch (Exception e) {
            logger.log("Failed to switch to new tab: " + e.getMessage());
        }

        boolean isNewTabOpened = driver.getWindowHandles().size() > existingHandles;

        if (isNewTabOpened) {
            driverTools.waitForWebToLoad();
            logger.log("E-Ticket successfully opened in new tab. URL: " + driver.getCurrentUrl());
            driverTools.closeCurrentTab();
            driverTools.switchToWindow(mainWindowHandle);
        }

        return isNewTabOpened;
    }

    /**
     * Clicks "English" on the Hotel E-Ticket language selector page.
     */
    public void selectEnglishLanguageForHotelETicket() {
        driverTools.waitForElementToAppear(E_TICKET_LANGUAGE_ENGLISH, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(E_TICKET_LANGUAGE_ENGLISH);
    }

    /**
     * Clicks the "Back" button on the Hotel E-Ticket language selector page to return to Itineraries.
     */
    public void clickBackFromLanguageSelector() {
        driverTools.waitForElementToAppear(E_TICKET_BACK_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(E_TICKET_BACK_BUTTON);
        driverTools.waitForWebToLoad();
    }

    /**
     * Helper to retrieve the Train card element using the origin/destination city instead of title.
     */
    private WebElement getTrainTripCardByStation(String stationKeyword) {
        ensureTripSectionExpanded();
        List<WebElement> cards = driverTools.elements(TRIP_ORDER_CARD).orElse(Collections.emptyList());
        for (WebElement card : cards) {
            try {
                if (card.getText().toLowerCase().contains(stationKeyword.toLowerCase())) {
                    return card;
                }
            } catch (Exception ignored) {}
        }
        throw new NoSuchElementException("Train order card containing station '" + stationKeyword + "' was not found.");
    }

    /**
     * Verifies train card details. Uses the origin location to find the card.
     */
    public boolean verifyTrainTripCardData(String expectedOriginLocation, String expectedDestLocation, String expectedBeneficiarySubstring) {
        WebElement card = getTrainTripCardByStation(expectedOriginLocation);

        String title = card.findElement(By.xpath(".//*[contains(@class,'TripOrderCard_title')]")).getText();
        String beneficiary = card.findElement(By.xpath(".//*[contains(@class,'TripOrderCard_beneficiary_names')]")).getText();

        List<WebElement> timelineSubtitles = card.findElements(By.xpath(".//*[contains(@class,'TripOrderTimeline_step_subtitle')]"));
        List<WebElement> timelineTitles = card.findElements(By.xpath(".//*[contains(@class,'TripOrderTimeline_step_title')]"));
        List<WebElement> timelineTimes = card.findElements(By.xpath(".//*[contains(@class,'TripOrderTimeline_step_time')]"));

        String originLocation = timelineSubtitles.size() >= 1 ? timelineSubtitles.get(0).getText() : "";
        String destLocation = timelineSubtitles.size() >= 2 ? timelineSubtitles.get(1).getText() : "";
        String originCity = timelineTitles.size() >= 1 ? timelineTitles.get(0).getText() : "";
        String destCity = timelineTitles.size() >= 2 ? timelineTitles.get(1).getText() : "";
        String originTime = timelineTimes.size() >= 1 ? timelineTimes.get(0).getText() : "";
        String destTime = timelineTimes.size() >= 2 ? timelineTimes.get(1).getText() : "";

        logger.log("Verifying Train Card -> Title: " + title + " | Origin: " + originCity + " - " + originLocation + " (" + originTime + ") | Dest: " + destCity + " - " + destLocation + " (" + destTime + ") | Beneficiary: " + beneficiary);
        logger.log("Verifying expected Card -> Title: " + title + " | Origin: " + originCity + " - " + expectedOriginLocation + " (" + originTime + ") | Dest: " + destCity + " - " + expectedDestLocation + " (" + destTime + ") | Beneficiary: " + expectedBeneficiarySubstring);

        boolean isOriginLocMatch = expectedOriginLocation == null || card.getText().toLowerCase().contains(expectedOriginLocation.toLowerCase());
        boolean isDestLocMatch = expectedDestLocation == null || card.getText().toLowerCase().contains(expectedDestLocation.toLowerCase());
        boolean isBeneficiaryMatch = expectedBeneficiarySubstring == null || beneficiary.toLowerCase().contains(expectedBeneficiarySubstring.toLowerCase());

        return isOriginLocMatch && isDestLocMatch && isBeneficiaryMatch && !originTime.isEmpty() && !destTime.isEmpty();
    }

    /**
     * Clicks "View E-Ticket" specifically for the Train Card using the station keyword.
     */
    public void clickCardViewETicketByStation(String stationKeyword) {
        WebElement card = getTrainTripCardByStation(stationKeyword);
        WebElement eTicketBtn = card.findElement(By.xpath(".//*[contains(@class,'TripOrderActions_label') and (text()='View e-ticket' or text()='Lihat e-tiket')]/ancestor::div[@role='button']"));
        driverTools.scrollIntoWebView(eTicketBtn);
        eTicketBtn.click();
    }

    public void hidePromoBannerIfPresent() {
        driverTools.waitForElementToAppear(OrderDetailWeb.HIDE_BANNER_BUTTON, 5).ifPresent(driverTools::click);
    }

}

