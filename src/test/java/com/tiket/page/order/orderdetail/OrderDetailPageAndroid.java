package com.tiket.page.order.orderdetail;

import com.tiket.appium.DriverTools.SideOfElement;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.refund.RefundPage;
import com.tiket.pojo.train.TrainData;
import com.tiket.testbase.Assertion;
import com.tiket.web.page.common.eTicket.ETicketWeb;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.NoSuchContextException;
import io.appium.java_client.android.AndroidDriver;
import lombok.SneakyThrows;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.page.payment.PaymentPageAndroid.LOADING_STATE;
import static com.tiket.page.train.searchpage.TrainSearchPageAndroid.TRAIN_NAME;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;

public class OrderDetailPageAndroid<T extends WebDriver> extends OrderDetailPage<T> {

    private String capturedPdfUrl;

    public OrderDetailPageAndroid(Session<T> session) {
        super(session);
    }

    public static final Locator PDF_APP_DRIVE = new Locator("PDF_APP_DRIVE", AppiumBy.xpath("//*[contains(@resource-id,'android:id/title') and contains(@text,'Open with Drive')]"));
    public static final Locator PDF_APP_VIEWER = new Locator("PDF_APP_VIEWER", AppiumBy.xpath("//*[contains(@resource-id,'android:id/text') and contains(@text,'PDF Viewer')]"));
    public static final Locator ALWAYS_APP = new Locator("ALWAYS_APP", AppiumBy.xpath("//*[@resource-id='android:id/button_always']"));
    private static final Locator RESCHEDULE_CARD_WEBVIEW = new Locator("RESCHEDULE_CARD_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'notification_banner')]"));
    private static final Locator ORDER_ID = new Locator("ORDER_ID", AppiumBy.xpath("//*[@class=\"android.widget.TextView\" and starts-with(@text, \"Order ID:\")]"));
    private static final Locator CHECKIN_DATE_WEBVIEW = new Locator("CHECKIN_DATE_WEBVIEW", AppiumBy.xpath("//*[text()='Check-in']//following-sibling::*[contains(@class,'check_in_out_value')]"));
    private static final Locator CHECKOUT_DATE_WEBVIEW = new Locator("CHECKOUT_DATE_WEBVIEW", AppiumBy.xpath("//*[text()='Check-out']//following-sibling::*[contains(@class,'check_in_out_value')]"));
    private static final Locator PREVIOUS_ORDER_BUTTON_WEBVIEW = new Locator("PREVIOUS_ORDER_BUTTON_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'NotificationBanner_action_button')]"));
    private static final Locator ETICKET_LABEL = new Locator("ETICKET_LABEL", AppiumBy.xpath("//*[contains(@class,'styles_eticket_header_title')] | (//*[contains(@class,'ETicket_generated')]//span)[1] | (//*[contains(@class,'WaitingStatus_container')]//p)[1]"));
    private static final Locator SEE_ETICKET_BUTTON = new Locator("SEE_ETICKET_BUTTON", AppiumBy.xpath("//*[contains(@class,'HOTEL_ETICKET_DETAIL')]//following-sibling::span[last()]"));
    //todo fix
    private static final Locator SEE_ETICKET_FROM_HEADER_WEBVIEW = new Locator("SEE_ETICKET_FROM_HEADER_WEBVIEW", AppiumBy.xpath("(//div[contains(translate(@class,'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'eticket_wrapper')]//span[contains(text(),'Lihat') or contains(text(),'See') or contains(text(),'View')])[1]"));
    private static final Locator FIREFOX_OPTION = new Locator("FIREFOX_OPTION", AppiumBy.xpath("//*[contains(@text,'Firefox')]"));
    private static final Locator SEE_ETICKET_FROM_HEADER_NATIVE = new Locator("SEE_ETICKET_FROM_HEADER_NATIVE", AppiumBy.xpath("//*[contains(@resource-id,'cv_card_divider')]//*[contains(@resource-id,'action')]"));
    private static final Locator CHANGE_LANGUAGE_CONTAINER = new Locator("CHANGE_LANGUAGE_CONTAINER", AppiumBy.xpath("//*[contains(@class,'ETicket_language_container') or contains(@class,'EVoucherContent_language_list')]"));
    private static final Locator PDF_CONTENT = new Locator("PDF_CONTENT", AppiumBy.xpath("//*[contains(@resource-id,'pdf_view') or contains(@resource-id,'viewerContainer')]//*[@content-desc][1]"));
    private static final Locator RESCHEDULE_BANNER = new Locator("RESCHEDULE_BANNER", AppiumBy.xpath("//*[contains(@text,'Rescheduling hotel')]"));
    private static final Locator PREVIOUS_ORDER_BANNER = new Locator("PREVIOUS_ORDER_BANNER", AppiumBy.xpath("//*[contains(@resource-id,'b_banner')]"));
    private static final Locator PREVIOUS_ORDER_BANNER_WEBVIEW = new Locator("PREVIOUS_ORDER_BANNER_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'card_header')]//*[contains(text(),'New Schedule') or contains(text(),'jadwal baru')]"));
    private static final Locator SKIP_INFORMATION_BUTTON = new Locator("SKIP_INFORMATION_BUTTON", AppiumBy.xpath("//*[@text='Skip' or @text='Lewati']|//*[contains(@resource-id,'tds_link_coach_mark')]"));
    private static final Locator SKIP_INFORMATION_BUTTON_WEBVIEW = new Locator("SKIP_INFORMATION_BUTTON_VIEW", AppiumBy.xpath("//*[contains(@class,'tix-coachmark-button-secondary')] | //*[contains(@class,'Coachmark_coachmark_skip_btn')] | //*[contains(@class,'coachmark_done_btn')]"));
    private static final Locator TOTAL_AMOUNT_AT = new Locator("TOTAL_AMOUNT_AT",AppiumBy.xpath("//div[contains(@class,'TotalAmount_total_amount_and_label')]//span"));
    private static final Locator TRAIN_TOTAL_PRICE_WEBVIEW = new Locator("TRAIN_TOTAL_PRICE_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'total_amount')]/*[contains(@class,'text')]"));
    private static final Locator TRAIN_MAIN_E_TICKET_WEBVIEW = new Locator("TRAIN_MAIN_E_TICKET_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'train_eticket_content')]//img"));
    private static final Locator TRAIN_MANAGE_ORDER_WEBVIEW = new Locator("TRAIN_MANAGE_ORDER_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'manage_order_wrapper')]"));
    private static final Locator ORDER_DETAIL_HEADER_TITLE = new Locator("ORDER_DETAIL_HEADER_TITLE", AppiumBy.xpath("//header[contains(@class,'styles_header')]/div/span"));
    private static final Locator CAR_RENTAL_LOCATION_TEXT = new Locator("CAR_RENTAL_LOCATION_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'tv_description') and not(contains(@text, '0'))]"));
    private static final Locator CAR_RENTAL_DRIVER_OPTION_TEXT = new Locator("CAR_RENTAL_DRIVER_OPTION_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'tv_text') and (contains(@text, 'driver') or contains(@text, 'sopir'))]"));
    private static final Locator CAR_RENTAL_PICKUP_TIME_TEXT = new Locator("CAR_RENTAL_PICKUP_TIME_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'tv_note')]//parent::*//*[contains(@resource-id,'tv_subtitle')]"));
    private static final Locator CAR_RENTAL_PASSENGER_FULL_NAME_TEXT = new Locator("CAR_RENTAL_PASSENGER_FULL_NAME_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Contact Details') or contains(@text,'Detail Pemesan'))]//parent::*//*[contains(@resource-id,'tv_name')]"));
    private static final Locator CAR_RENTAL_PASSENGER_PHONE_NUMBER_TEXT = new Locator("CAR_RENTAL_PASSENGER_PHONE_NUMBER_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'tv_phone')]"));
    private static final Locator NATIVE_SHARE_RECEIPT_BTN = new Locator("NATIVE_SHARE_RECEIPT_BTN", AppiumBy.xpath("//*[contains(@resource-id,'tv_action') and (contains(@text,'Bagikan') or contains(@text,'Share'))]"));
    private static final Locator RECEIPT_BUTTON_NATIVE = new Locator("RECEIPT_BUTTON_NATIVE", AppiumBy.xpath("//*[contains(@resource-id,'view_item') and *[contains(@text,'Receipt') or contains(@text,'Bukti')]]"));

    private static final Locator REFUND_DETAIL_DATE = new Locator("REFUND_DETAIL_DATE", AppiumBy.xpath("//*[(contains(@resource-id,'tv_text_left')) and not(contains(@text,'Submission Date')) and not(contains(@text,'Tanggal Pengajuan'))]"));
    private static final Locator REFUND_DESCRIPTION = new Locator("REFUND_DESCRIPTION", AppiumBy.xpath("//*[contains(@resource-id,'tv_description')]"));
    public static final Locator ORDER_DETAIL_ORDER_ID = new Locator("ORDER_DETAIL_ORDER_ID", AppiumBy.id("tds_sub_header_navigation_appbar"));
    private static final Locator REFUND_VALUE = new Locator("REFUND_VALUE", AppiumBy.xpath("(//*[contains(@resource-id, 'cl_container')])[2]//*[contains(@resource-id, 'tv_text_right')]"));
    static final Locator REFUND_RESCHEDULE_IFRAME = new Locator("REFUND_RESCHEDULE_IFRAME", AppiumBy.cssSelector("iframe[src*='refund-reschedule-info']"));
    static final Locator REFUND_VALUE_WEBVIEW = new Locator("REFUND_VALUE_WEBVIEW", AppiumBy.xpath("(//*[contains(@data-testid,'amount-description-text')])[1]"));
    public static final Locator MANAGE_ORDER_BUTTON = new Locator("MANAGE_ORDER_BUTTON", AppiumBy.xpath("//*[contains(@class,'FloatingButton_extended_btn')]"));
    public static final Locator RESCHEDULE_BUTTON = new Locator("RESCHEDULE_BUTTON", AppiumBy.xpath("//div[contains(@class,'ActionButton_action_title')][span[normalize-space()='Reschedule' or normalize-space()='Ubah Jadwal']]"));
    public static final Locator MANAGE_ORDER_SECTION = new Locator("MANAGE_ORDER_SECTION", AppiumBy.xpath("//*[contains(@class,'ManageOrder_manage_order_wrapper') or contains(@class,'ManageOrder_action_buttons_wrapper')]"));
    public static final Locator REFUND_RESCHEDULE_DELETE_ORDER_BUTTON = new Locator("REFUND_RESCHEDULE_DELETE_ORDER_BUTTON", AppiumBy.xpath("//*[contains(@class,'RefundRescheduleInfo_button')]"));
    private static final Locator REFUND_BUTTON = new Locator("REFUND_BUTTON", AppiumBy.xpath("//*[contains(@class,'ActionButton_action_title')]//*[contains(text(),'Refund')]"));
    private static final Locator REFUND_BUTTON_NATIVE = new Locator("REFUND_BUTTON_NATIVE", AppiumBy.xpath("//*[contains(@resource-id,'view_item') and *[contains(@text,'Refund')]]"));
    private static final Locator TTD_TOTAL_PAYMENT = new Locator("TTD_TOTAL_PAYMENT", AppiumBy.xpath("//*[contains(@class, 'TotalAmount_total_amount_and_label')]//*[contains(@class, 'Text_text')]"));
    private static final Locator TTD_PRODUCT_TITLE = new Locator("TTD_PRODUCT_TITLE", AppiumBy.xpath("//*[contains(@class,'TTDInfo_detail_info')]//div[contains(@class,'text')]"));
    private static final Locator ORDER_ID_EDIT_PAGE = new Locator("ORDER_ID_EDIT_PAGE", AppiumBy.xpath("//*[contains(@resource-id,'tv_toolbar_description')]"));
    private static final Locator HOTEL_NAME_EDIT_PAGE = new Locator("HOTEL_NAME_EDIT_PAGE", AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_detail_name')]"));
    private static final Locator SAVE_GUEST_NAME = new Locator("SAVE_GUEST_NAME", AppiumBy.xpath("//*[contains(@resource-id,'tv_save_contact_details')]"));
    private static final Locator SAVE_GUEST_NAME_REVISE = new Locator("SAVE_GUEST_NAME_REVISE", AppiumBy.xpath("//*[contains(@resource-id,'btn_save_revise')]"));
    private static final Locator SAVE_GUEST_NAME_POPUP = new Locator("SAVE_GUEST_NAME_POPUP", AppiumBy.xpath("//*[contains(@resource-id,'tv_bottom_button')]"));
    private static final Locator EDIT_GUEST_FIELD = new Locator("EDIT_GUEST_FIELD", AppiumBy.xpath("(//*[contains(@resource-id,'tv_edit_title')])[1]"));
    private static final Locator EDIT_GUEST_FIELD_POPUP = new Locator("EDIT_GUEST_FIELD_POPUP", AppiumBy.xpath("(//*[contains(@resource-id,'et_form')])[1]"));
    private static final Locator FIND_ON_PDF = new Locator("FIND_ON_PDF", AppiumBy.xpath("//android.widget.TextView[contains(@resource-id,'action_find')]"));
    private static final Locator PDF_SEARCH_BOX = new Locator("PDF_SEARCH_BOX", AppiumBy.xpath("//android.widget.EditText[contains(@resource-id,'find_query_box')]"));
    private static final Locator PDF_SEARCH_STATUS = new Locator("PDF_SEARCH_STATUS", AppiumBy.xpath("//android.widget.TextView[contains(@resource-id,'match_status_textview')]"));
    private static final Locator CHECKOUT_DATE = new Locator("CHECKOUT_DATE", AppiumBy.id("tv_checkout_date"));
    protected static final Locator NEW_SCHEDULE_NATIVE = new Locator("NEW_SCHEDULE_NATIVE", AppiumBy.xpath("//*[@text='Order Update' or @text='Update Pesanan']"));
    private static final Locator NEW_SCHEDULE_WEBVIEW = new Locator("NEW_SCHEDULE_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'update_view_header') or contains(@class,'NotificationBanner_body')]"));
    private static final Locator NEW_SCHEDULE_BUTTON_WEBVIEW = new Locator("NEW_SCHEDULE_BUTTON_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'Card_card_container')]//*[contains(@class,'action_title') or contains(@class,'title_button')]"));
    protected static final Locator REFUND_DETAIL_BUTTON_NATIVE = new Locator("REFUND_DETAIL_BUTTON_NATIVE", AppiumBy.xpath("//*[@text='Refund']//following-sibling::*[contains(@text,'Detail')]"));
    private static final Locator REFUND_DETAIL_BUTTON = new Locator("REFUND_DETAIL_BUTTON", AppiumBy.xpath("//*[contains(@class,'title_text') and contains(text(),'Refund')]//following::*[contains(@class,'title_button') and contains(text(),'Detail')]"));
    private static final Locator CHAT_BTN = new Locator("CHAT_BTN", AppiumBy.xpath("//div[span[contains(text(),'Chat')]]"));
    private static final Locator INFO_POPUP = new Locator("INFO_POPUP", AppiumBy.id("tds_info_view_dialog"));
    private static final Locator OK_BUTTON = new Locator("OK_BUTTON", AppiumBy.xpath("//*[contains(@text,'Ok')]"));
    private static final Locator DATE_TIME_TEXT = new Locator("DATE_TIME_TEXT", AppiumBy.xpath("(//*[contains(@class,'DetailReservation_detail_reservation_card')]//*[contains(@class,'DetailReservation_package_details')])//p[contains(text(),' - ') or contains(text(),':')]"));
    private static final Locator REFUND_BANNER_INFO = new Locator("REFUND_BANNER_INFO", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'Refund')]//parent::*[contains(@resource-id,'view_item')]//parent::android.widget.LinearLayout//parent::*[contains(@resource-id,'rv_container')]"));
    private static final Locator REFUND_NOT_AVAILABLE = new Locator("REFUND_NOT_AVAILABLE", AppiumBy.xpath("//*[contains(@resource-id,'tds_txt_header_info_view') and (@text='Refund is not available' or @text='Refund tidak tersedia' or @text='Order Cannot Be Refunded' or @text='Order Tidak Dapat Di Refund')]"));
    private static final Locator TOTAL_ADULT = new Locator("TOTAL_ADULT", AppiumBy.xpath("//*[contains(@class, 'styles_list_icon')]/following-sibling::*[contains(text(), 'guest') or contains(text(), 'tamu')]"));
    private static final Locator TOTAL_NIGHT = new Locator("TOTAL_NIGHT", AppiumBy.xpath("//*[contains(@class, 'styles_number_of_nights')]/child::*[last()]"));
    private static final Locator TOTAL_ROOM = new Locator("TOTAL_ROOM", AppiumBy.xpath("(//*[contains(@class, 'styles_detail_list')]//*[contains(@class, 'styles_list_icon')]/following-sibling::*)[1]"));
    private static final Locator MAIN_ETICKET = new Locator("MAIN_ETICKET", AppiumBy.xpath("//*[contains(@class, 'ETicket_wrapper')]"));
    private static final Locator ETICKET_BUTTON = new Locator("ETICKET_BUTTON", AppiumBy.xpath("//*[contains(@class, 'ETicket_generated_ticket__')]"));
    private static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", AppiumBy.xpath("(//*[contains(@class, 'styles_title_text__')])[last()]"));
    private static final Locator ORDER_ID_WEBVIEW = new Locator("ORDER_ID_WEBVIEW", AppiumBy.xpath("(//*[contains(@class, 'text') and contains(text(),'Order ID')])[1]"));
    private static final Locator ORDER_DETAIL_ID_WEBVIEW = new Locator("ORDER_DETAIL_ID_WEBVIEW", AppiumBy.xpath("//span[contains(text(),'Order Detail ID') or contains(text(),'ID Detail Pesanan')]/following-sibling::div[contains(@class,'styles_id_copy_icon')]/span[1]"));
    private static final Locator GUEST_FULL_NAME = new Locator("GUEST_FULL_NAME", AppiumBy.xpath("    //*[contains(@class, 'styles_detail')]//*[(contains(text(), 'Mr') or contains(text(), 'Tuan') or contains(text(), 'Mrs') or contains(text(), 'Nyonya') or contains(text(), 'Ms') or contains(text(), 'Nona'))]"));
    private static final Locator BUS_PASSENGER_DETAIL= new Locator("BUS_PASSENGER_DETAIL", AppiumBy.xpath("//span[contains(@class,'VPassenger_passenger_name') and contains(@class,'Text_text')]"));
    private static final Locator BUS_SEAT_NO_DETAIL= new Locator("BUS_SEAT_NO_DETAIL", AppiumBy.xpath("//*[contains(@class,'VPassenger_margin') and contains(@class,'Text_text')]"));
    private static final Locator MAIN_ETICKET_NATIVE = new Locator("MAIN_ETICKET_NATIVE", AppiumBy.xpath("//*[contains(@resource-id, 'tv_title') and contains(@text,'e-ticket') or contains(@text,'E-tiketmu')]"));
    private static final Locator CONFIRM_CONTINUE_PAYMENT_TRAIN_CHANGE_SEAT = new Locator("CONFIRM_CONTINUE_PAYMENT_TRAIN_CHANGE_SEAT", AppiumBy.xpath("//android.widget.Button[contains(@text,'Lanjutkan') or contains(@text,'Continue')]"));
    private static final Locator TRAIN_SEAT_SELECTION_CONTAINER = new Locator("TRAIN_SEAT_SELECTION_CONTAINER", AppiumBy.id("linear_web_view"));
    private static final Locator ROOM_NAME_INFO = new Locator("ROOM_NAME_INFO", AppiumBy.xpath("//*[@data-testid='hotel-title']"));
    private static final Locator ROOM_AMOUNT_INFO = new Locator("ROOM_AMOUNT_INFO", AppiumBy.xpath("//*[contains(@class,'list_icon')]/following-sibling::*[contains(text(),'Room') or contains(text(),'Kamar')]"));
    private static final Locator ROOM_GUEST_INFO = new Locator("ROOM_GUEST_INFO", AppiumBy.xpath("//*[contains(@class,'list_icon')]/following-sibling::*[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'guest') or contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'tamu')]"));
    private static final Locator REFUND_RESCHEDULE_TAB_BUTTON = new Locator("REFUND_RESCHEDULE_TAB_BUTTON", AppiumBy.xpath("//*[@data-testid='detail-tabs']//*[text()='Refund & Reschedule']"));
    private static final Locator REFUND_REQUEST_BUTTON = new Locator("REFUND_REQUEST_BUTTON", AppiumBy.xpath("//*[contains(@class,'button') and (text()='Ajukan refund' or text()='Request refund')]"));
    private static final Locator LANG_POPUP_ENGLISH = new Locator("LANG_POPUP_ENGLISH", AppiumBy.xpath("(//div[contains(@class, 'ETicket_language_container')]//span[contains(@class, 'ETicket_lang_title') and text()='English'])[1]"));
    private static final Locator LANG_POPUP_INDO = new Locator("LANG_POPUP_INDO", AppiumBy.xpath("(//span[text()='Bahasa Indonesia']/ancestor::div[contains(@class,'ETicket_language_container')]//span[text()='View'])[1]"));
    private static final Locator FIREFOX = new Locator("FIREFOX", AppiumBy.xpath("//*[@class='android.widget.LinearLayout' and ./preceding-sibling::*[@class='android.widget.LinearLayout']]"));
    private static final Locator FIREFOX_2 = new Locator("FIREFOX_2", AppiumBy.xpath("(//*[@class='android.widget.LinearLayout' and ./preceding-sibling::*[@class='android.widget.LinearLayout']])[2]"));
    private static final Locator JUST_ONCE_BTN = new Locator("JUST_ONCE_BTN", AppiumBy.xpath("//*[@resource-id='android:id/button_once']"));

    //TTD Order Detail
    private static final Locator TTD_TOTAL_PRICE= new Locator("TTD_TOTAL_PRICE", AppiumBy.xpath("//*[contains(@class,'total_amount') or contains(@class,'PaymentFooter_price')]/*[contains(@class,'text')]"));
    private static final Locator TTD_TOTAL_PRICE_CHEVRON= new Locator("TTD_TOTAL_PRICE_CHEVRON", AppiumBy.xpath("//*[contains(@class,'TotalAmount_margin_icon') or contains(@class,'PaymentFooter_margin_icon')]"));
    private static final Locator TTD_ALL_PRICE_DETAIL_TEXT= new Locator("TTD_ALL_PRICE_DETAIL_TEXT", AppiumBy.xpath("//*[contains(@class,'BottomSheet_body')]//*[contains(@class,'styles_accordion_title')]/*[2] | //*[contains(@class,'BottomSheet_body')]//*[contains(@class,'styles_discount_types')]/*[2] | //*[contains(@class,'BottomSheet_body')]//*[contains(@class,'styles_total_amount')]/*[2]"));
    private static final Locator BOTTOMSHEET_CLOSE_BUTTON= new Locator("BOTTOMSHEET_CLOSE_BUTTON", AppiumBy.xpath("//p[normalize-space(.)='Refund & Reschedule Info']/preceding-sibling::*[local-name()='svg'][1]"));
    private static final Locator BOTTOMSHEET_TOTAL_PRICE= new Locator("BOTTOMSHEET_TOTAL_PRICE", AppiumBy.xpath("//*[contains(@class,'BottomSheet_body')]//*[contains(@class,'styles_total_amount')]/*[2]"));
    private static final Locator SHARE_RECEIPT_BUTTON= new Locator("SHARE_RECEIPT_BUTTON", AppiumBy.xpath("//*[contains(@class,'TotalAmount_share_reciept_button')]"));
    private static final Locator RECEIPT_BUTTON= new Locator("RECEIPT_BUTTON", AppiumBy.xpath("//span[contains(@class, 'TotalAmount_share_reciept_button')]"));
    private static final Locator PDF_VIEWER_TEXT= new Locator("PDF_VIEWER_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'com.android.intentresolver')]"));
    private static final Locator SHARE_ETICKET_FROM_HEADER = new Locator("SHARE_ETICKET_FROM_HEADER", AppiumBy.id("tds_icon_appbar"));
    private static final Locator CLOSE_ETICKET_BTM_SHEET_WEBVIEW = new Locator("CLOSE_ETICKET_BTM_SHEET", AppiumBy.xpath("//header[contains(@class,'BottomSheet_header')]//*[@aria-label='Close' or @aria-label='Tutup']"));
    private static final Locator NATIVE_BTM_SHEET_OUTER_LAYOUT = new Locator("NATIVE_BTM_SHEET_OUTER_LAYOUT", AppiumBy.xpath("(//android.widget.FrameLayout)[1]"));
    private static final Locator HIDE_BUTTON = new Locator("HIDE_BUTTON", AppiumBy.xpath("//*[normalize-space()='Hide' or normalize-space()='Tutup']"));
    //AT order detail
    private static final Locator AT_CAR_TYPE_NAME = new Locator("AT_CAR_TYPE_NAME",AppiumBy.xpath("//div['styles_airport_transfer']/div/div/span[contains(@class,'styles_title_text')]"));
    private static final Locator AT_CAR_DRIVE_CONTACT = new Locator("AT_DRIVER_CONTACT", AppiumBy.xpath("//button[contains(@class,'ContactPartner_contact_vendor_button')]"));
    private static final Locator AT_TRIP_DETAIL_ENTRY = new Locator("AT_TRIP_DETAIL_ENTRY", AppiumBy.xpath("//div[contains(@class,'DetailReservation_detail_reservation_card')]//div[contains(@class,'styles_action_wrapper')]"));
    private static final Locator AT_FACILITIES_DETAIL_ENTRY = new Locator("AT_FACILITIES_DETAIL_ENTRY", AppiumBy.xpath("//*[contains(@text,'Fasilitas') or contains(@text,'Facility')]//following-sibling::*[contains(@resource-id,'tv_action')  and contains(@text,'Detail')]"));
    private static final Locator AT_TRIP_DETAIL_PICKUP = new Locator("AT_TRIP_DETAIL_PICKUP", AppiumBy.xpath("(//div[@data-testid='bottom-sheet-body']//p)[2]"));
    private static final Locator AT_TRIP_DETAIL_DESTINATION = new Locator("AT_TRIP_DETAIL_DESTINATION", AppiumBy.xpath("(//div[@data-testid='bottom-sheet-body']//p)[5]"));
    private static final Locator AT_FACILITIES_COMPONENT = new Locator("AT_FACILITIES_COMPONENT",AppiumBy.xpath("//androidx.recyclerview.widget.RecyclerView//android.widget.TextView"));
    private static final Locator AT_CONTACT_DETAIL =  new Locator("AT_CONTACT_DETAIL", AppiumBy.xpath("//div[contains(@class,'styles_contact_detail_wrapper')]//span"));
    private static final Locator AT_CLOSE_DETAIL = new Locator("AT_CLOSE_DETAIL", AppiumBy.id("iv_close"));
    private static final Locator SEE_MORE_DETAILS_BUTTON = new Locator("SEE_MORE_DETAILS_BUTTON", AppiumBy.xpath("//*[contains(@class,'styles_hotel_reservation_footer')]//button"));
    private static final Locator GUEST_NAME_DETAILS = new Locator("GUEST_NAME_DETAILS", AppiumBy.xpath("//*[contains(@class,'number_list')]//li"));
    private static final Locator GUEST_NAME_ORDER = new Locator("GUEST_NAME", AppiumBy.xpath("//*[text()='Detail Tamu' or text()='Guest Details']//parent::*[contains(@class,'detail_list')]//following-sibling::*[contains(@class,'detail_list')]//li"));
    private static final Locator EDIT_BOOKING_BUTTON = new Locator("EDIT_BOOKING_BUTTON", AppiumBy.xpath("//*[contains(@class,'content_section_header')]//*[contains(text(),'Tamu') or contains(text(),'Guest')]//following-sibling::*[contains(@class,'action_button') and (contains(text(),'Ubah') or contains(text(),'Edit'))]"));
    private static final Locator GUEST_DETAILS_PAGE = new Locator("GUEST_DETAILS_PAGE", AppiumBy.xpath("//*[contains(@class,'HeadlineMobile_container')]//*[contains(@class,'HeadlineMobile_title')]"));
    private static final Locator FULL_NAME_FIELD = new Locator("FULL_NAME_FIELD", AppiumBy.xpath("//*[@id='nama-lengkap-sesuai-identitas' or @id='full-name-according-to-id']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", AppiumBy.xpath("//*[contains(@class,'GuestDetailsForm_footer')]//button"));
    private static final Locator SUBMIT_CONFIRM = new Locator("SUBMIT_CONFIRM", AppiumBy.xpath("//*[contains(@class,'Dialog_body')]//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator VERIFY_PHONE_NUMBER_FIELD = new Locator("VERIFY_PHONE_NUMBER_FIELD", AppiumBy.xpath("//*[contains(@class,'OtpInput')]//input"));
    private static final Locator SEE_RESERVATION_DETAIL_BUTTON = new Locator("SEE_RESERVATION_DETAIL_BUTTON", AppiumBy.xpath("//*[contains(@class,'SuccessReviseFeedback')]//*[contains(@class,'SuccessReviseFeedback')]//button"));
    private static final Locator MR_SALUTATION_CHECKBOX = new Locator("MR_SALUTATION_CHECKBOX", AppiumBy.xpath("//*[contains(@class,'Radio_checkbox')]//*[@value='\"Mr\"']//parent::span"));
    private static final Locator MS_SALUTATION_CHECKBOX = new Locator("MS_SALUTATION_CHECKBOX", AppiumBy.xpath("//*[contains(@class,'Radio_checkbox')]//*[@value='\"Mrs\"']//parent::span"));
    private static final Locator MRS_SALUTATION_CHECKBOX = new Locator("MRS_SALUTATION_CHECKBOX", AppiumBy.xpath("//*[contains(@class,'Radio_checkbox')]//*[@value='\"Ms\"']//parent::span"));
    private final String updateName = "updated";

    //FERRY Oeder detial
    private static final Locator SEE_FERRY_ETIKCET =  new Locator("SEE_FERRY_ETIKCET", AppiumBy.xpath("//*[@class='android.widget.TextView' and @text='View']"));
    private static final Locator VIEW_ETICKET_BTMSHEET =  new Locator("VIEW_ETICKET_BTMSHEET", AppiumBy.xpath("///*[@class='android.view.View' and (contains(@text, 'E-ticket Ferry (Express)') or contains(@text, 'E-tiket Ferry (Express)'))]"));
    private static final Locator CLOSE_BTMSHEET =  new Locator("CLOSE_BTMSHEET", AppiumBy.xpath("//*[contains(@class,'BottomSheet_close_button__')]"));
    private static final Locator FERRY_BARCODE =  new Locator("FERRY_BARCODE", AppiumBy.xpath("(//*[@class='android.view.View'])[47]"));
    private static final Locator SHARE_RECIPT_ =  new Locator("SHARE_RECIPT_", AppiumBy.xpath("//span[contains(@class, 'TotalAmount_share_reciept_button')]"));


    //Car rental order detail
    private static final Locator CAR_RENTAL_PRODUCT_NAME = new Locator("CAR_RENTAL_PRODUCT_NAME", AppiumBy.xpath("//*[*[contains(@resource-id,'rv_details')]]//*[contains(@resource-id,'tv_title')]"));
    private static final Locator CAR_RENTAL_RENTAL_TYPE = new Locator("CAR_RENTAL_RENTAL_TYPE", AppiumBy.xpath("(//*[contains(@resource-id,'rv_details')]//*[contains(@resource-id,'tv_text')])[2]"));
    private static final Locator CONTACT_DRIVER_EXIST = new Locator("CONTACT_DRIVER_EXIST", AppiumBy.id("tv_driver_phone"));
    private static final Locator RENTAL_DETAIL_TITLE = new Locator("RENTAL_DETAIL_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_text') and (@text='Detail Sewa' or @text='Rental Detail')]"));
    private static final Locator CAR_RENTAL_PICK_UP_LOCATION = new Locator("CAR_RENTAL_PICK_UP_LOCATION", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Jemput') or contains(@text,'Pick-up'))]//parent::*//*[contains(@resource-id,'tv_description')]"));
    private static final Locator CAR_RENTAL_DROP_OFF_LOCATION = new Locator("CAR_RENTAL_DROP_OFF_LOCATION", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Antar') or contains(@text,'Drop-off'))]//parent::*//*[contains(@resource-id,'tv_description')]"));
    private static final Locator CAR_RENTAL_PICK_UP_DATE = new Locator("CAR_RENTAL_PICK_UP_DATE", AppiumBy.xpath("//*[contains(@text,'Jemput') or contains(@text,'Pick-up')]//following-sibling::*[contains(@resource-id,'tv_subtitle')]"));
    private static final Locator CAR_RENTAL_DROP_OFF_DATE = new Locator("CAR_RENTAL_DROP_OFF_DATE", AppiumBy.xpath("//*[contains(@text,'Antar') or contains(@text,'Drop-off')]//following-sibling::*[contains(@resource-id,'tv_subtitle')]"));
    private static final Locator CAR_RENTAL_DURATION = new Locator("CAR_RENTAL_DURATION", AppiumBy.id("tv_note"));
    private static final Locator CR_REFUND_AND_RESCHEDULE_INFO_SECTION = new Locator("CR_REFUND_AND_RESCHEDULE_INFO_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'cl_container') and *[contains(@text,'Refund & Reschedule')]]"));
    private static final Locator CR_RR_DETAIL_BTN = new Locator("CR_RR_DETAIL_BTN",AppiumBy.xpath("//*[contains(@resource-id,'cl_container') and *[contains(@text,'Refund & Reschedule')]]//*[contains(@text,'Detail')]"));
    private static final Locator RR_INFO_BTM_SHEET = new Locator("RR_INFO_BTM_SHEET", AppiumBy.xpath("//*[contains(@resource-id,'action_bar_root')]//*[contains(@text,'Info Refund & Reschedule')]"));
    private static final Locator LOGIN_CHALLENGE_BTN = new Locator("LOGIN_CHALLENGE_BTN", AppiumBy.xpath("//*[contains(@text,'Log in sekarang') or contains(@text,'Log in now')]"));
    private static final Locator LOGIN_CHALLENGE_CLOSE_BTN = new Locator("LOGIN_CHALLENGE_CLOSE_BTN", AppiumBy.xpath("//*[contains(@text,'Kembali') or contains(@text,'Back')]"));
    private static final Locator CR_ORDDER_DETAIL_TOTAL_PRICE = new Locator("CR_ORDER_DETAIL_TOTAL_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'tv_total_payment')]//following-sibling::*[contains(@resource-id,'tv_price')] | //*[contains(@resource-id,'tv_title') and contains(@text,'Total')]//following-sibling::*"));
    private static final Locator CR_PRICE_DETAIL_COMPONENTS = new Locator("CR_PRICE_DETAIL_COMPONENTS", AppiumBy.xpath("//*[contains(@resource-id,'tv_price') and not(preceding-sibling::*[contains(@resource-id,'tv_total_payment')])] | //*[contains(@resource-id,'tv_value') and not(preceding-sibling::*[contains(@text,'Total')])]"));
    private static final Locator ORDER_DETAIL_NATIVE_CHEVRON = new Locator("ORDER_DETAIL_NATIVE_CHEVERON", AppiumBy.xpath("//*[contains(@resource-id,'view_item') and *[contains(@text,'Total')]]//*[contains(@resource-id,'iv_chevron')]"));
    private static final Locator CR_CLOSE_PRICE_DETAIL = new Locator("CR_CLOSE_PRICE_DETAIL", AppiumBy.id("iv_close"));
    private static final Locator CR_SCROLL_DOWN_FLOATING_BTN = new Locator("CR_SCROLL_DOWN_FLOATING_BTN", AppiumBy.id("tds_extended_fab_group"));

    private static final Locator HELP_CENTER_CARD = new Locator("HELP_CENTER_CARD",AppiumBy.id("tds_content_notification_banner"));
    private static final Locator ETICKET_ON_MANAGE_ORDER_SECTION_WEBVIEW = new Locator("ETICKET_ON_MANAGE_ORDER_SECTION_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'ActionButton_action_title')]/*[text()='E-ticket' or text()='E-tiket']"));
    private static final Locator SKIP_ROOM_DETAIL_HOTEL = new Locator("SKIP_ROOM_DETAIL_HOTEL", AppiumBy.xpath("//button[contains(@class,'Coachmark_coachmark_skip_btn')]"));
    private static final Locator PRICE_DETAIL_DISCOUNT_COMPONENTS = new Locator("PRICE_DETAIL_DISCOUNT_COMPONENTS", AppiumBy.xpath("//div[contains(@class, 'styles_discount_types')]//*[contains(@class,'price_text')]"));
    private static final Locator ORDER_DETAIL_ID_COPY = new Locator("ORDER_DETAIL_ID_COPY", AppiumBy.xpath("//*[contains(@class,'copy_button')]"));
    private static final Locator ORDER_DETAIL_ID = new Locator("ORDER_DETAIL_ID", AppiumBy.xpath("//*[contains(@class, 'order_id')]//span"));
    private static final Locator CALL_BUTTON = new Locator("CALL_BUTTON", AppiumBy.xpath("(//*[contains(@class, 'styles_action_text') and contains(text(),'Telepon') or contains(text(),'Call')])[last()]"));
    private static final Locator ORDER_DETAIL_ID_COPIED = new Locator("ORDER_DETAIL_ID_COPIED", AppiumBy.xpath("//*[contains(text(),'Order Detail ID berhasil disalin.') or contains(text(),'Order Detail ID has been copied.')]"));
    private static final Locator HOTEL_MAP_BTN = new Locator("HOTEL_MAP_BTN", AppiumBy.xpath("(//*[contains(@class, 'styles_action_location')]//*[(contains(text(),'Petunjuk Arah') or contains(text(),'Direction'))])[last()]"));
    private static final Locator REFUND_RESCHEDULE_BTMSHEET = new Locator("REFUND_RESCHEDULE_BTMSHEET", AppiumBy.xpath("//*[contains(@class, 'BottomSheet_body_title_' ) and contains(text(),'Info Refund & Reschedule') or contains(text(),'Refund & Reschedule Info')]"));
    private static final Locator PAGE_MODULE = new Locator("PAGE_MODULE", AppiumBy.xpath("//div[contains(@class,'page_module_')]"));
    private static final Locator LPG_BANNER = new Locator("LPG_BANNER", AppiumBy.xpath("//div[contains(@class,'TotalAmount_lpg_wrapper')]"));
    private static final Locator EDIT_GUESTS_DETAILS = new Locator("EDIT_GUESTS_DETAILS", AppiumBy.xpath("//div[contains(@class,'ManageOrder')]//*[contains(text(),'Ubah Pemesanan') or contains(text(),'Edit Booking')]"));
    private static final Locator REFUND_RESCHEDULE_DETAILS = new Locator("REFUND_RESCHEDULE_DETAILS", AppiumBy.xpath("//*[contains(@class, 'RefundRescheduleInfo_more_details')]"));
    private static final Locator EDIT_SPECIAL_REQUEST = new Locator("EDIT_SPECIAL_REQUEST", AppiumBy.xpath("(//*[contains(@class, 'styles_action_button__')])[last()]"));
    private static final Locator SPECIAL_REQUEST_CHANGE = new Locator("SPECIAL_REQUEST_CHANGE", AppiumBy.xpath("(//*[contains(@class, 'RoomTypeCard_icon_')])[last()]"));
    private static final Locator SPECIAL_REQUEST_TEXTFIELD = new Locator("SPECIAL_REQUEST_TEXTFIELD", AppiumBy.xpath("//div[contains(@class, 'styles_input_inner_wrapper__')]/textarea[@id='isi-permintaan-lain']"));
    private static final Locator SAVE_SPECIAL_REQUEST = new Locator("SAVE_SPECIAL_REQUEST", AppiumBy.xpath("//*[contains(@class,'SpecialRequestForm_save_button_wrapper_')]//button"));
    private static final Locator YES_SPECIAL_REQUEST = new Locator("YES_SPECIAL_REQUEST", AppiumBy.xpath("(//*[contains(@class, 'RoomTypeCard_icon__')])[last()]//*[contains(@class,'Button_button__') and contains(text(),'Ya') or contains(text(),'Yes')]"));
    private static final Locator OTP_FEILD = new Locator("OTP_FEILD", AppiumBy.xpath("//*[contains(@class,'OtpInput_otp_input__')]"));
    private static final Locator SPECIAL_REQUEST_CHANGED_BANNER = new Locator("SPECIAL_REQUEST_CHANGED_BANNER", AppiumBy.xpath("//*[contains(@class, 'SuccessReviseFeedback_image_wrapper__')]"));
    private static final Locator SEE_RESERVATION_DETAILS = new Locator("SEE_RESERVATION_DETAILS", AppiumBy.xpath("//*[contains(@class, 'Button_button_')]"));
    private static final Locator PASSENGER_NAMES = new Locator("PASSENGER_NAMES", AppiumBy.xpath("//*[contains(@class,'passenger_names')]"));
    private static final Locator FULL_PROTECTION_SUBMIT_NEW_CLAIM = new Locator("FULL_PROTECTION_SUBMIT_NEW_CLAIM", AppiumBy.xpath("(//*[contains(@class, 'styles_protection_content')])[last()]"));
    private static final Locator HOTEL_RESERVATION_TITLE = new Locator("HOTEL_RESERVATION_TITLE", AppiumBy.xpath("//*[contains(@class,'hotel_reservation_v2_wrapper_title')]"));
    private static final Locator SEE_BOARDING_PASS = new Locator("SEE_BOARDING_PASS", AppiumBy.xpath("//android.widget.Button[@text=\"Print e-boarding pass\" or @text=\"Cetak e-boarding pass\"]\n"));
    private static final Locator ETICKET_LANGUAGE_EN = new Locator("ETICKET_LANGUAGE_EN", AppiumBy.xpath("(//*[contains(@class,'ChooseETicketLangPopup') or contains(@class,'ETicket_language_container')]//span[contains(text(),'English') or contains(text(),'Bahasa Inggris')])[1]"));
    private static final Locator ETICKET_LANGUAGE_ID = new Locator("ETICKET_LANGUAGE_ID", AppiumBy.xpath("(//*[contains(@class,'ChooseETicketLangPopup') or contains(@class,'ETicket_language_container')]//span[contains(text(),'Indonesia') or contains(text(),'Bahasa Indonesia')])[1]"));
    private static final Locator REFUND_BUTTON_WHOOSH =new Locator("REFUND_BUTTON", AppiumBy.xpath("//*[contains(@class,'detail_order_detail_wrapper')]//span[normalize-space()='Refund']"));
    private static final Locator LOGIN_NOW_BUTTON = new Locator("LOGIN_NOW_BUTTON", AppiumBy.xpath("//button[.//span[normalize-space()='Log in now']]"));
    private static final Locator IMPORTANT_INFO_DETAILS = new Locator("IMPORTANT_INFO_DETAILS", AppiumBy.xpath("//span[@data-testid='action-text' and normalize-space()='Details']"));
    private static final Locator IMPORTANT_INFO_BACK = new Locator("IMPORTANT_INFO_BACK", AppiumBy.xpath("//*[@class='android.widget.ImageButton' and @clickable='true' and @package='com.tiket.gits.preprod']"));
    private static final Locator ORDER_DETAIL = new Locator("ORDER_DETAIL", AppiumBy.xpath("//*[contains(text(),'Order ID:')]"));
    private static final Locator CONTACT_CUSTOMER_CARE = new Locator("CONTACT_CUSTOMER_CARE", AppiumBy.xpath("//*[normalize-space()='Contact Customer Care']"));
    private static final Locator CONTACT_US_TITLE = new Locator("CONTACT_US_TITLE", AppiumBy.xpath("//h1[@data-testid='title-section' and normalize-space()='Contact Us']"));
    private static final Locator HELP_CENTER = new Locator("HELP_CENTER", AppiumBy.xpath("//a[contains(@href,'help-center') and @aria-label]"));
    private static final Locator HELP_CENTER_BACK_BUTTON = new Locator("HELP_CENTER_BACK_BUTTON", AppiumBy.xpath("//button[contains(@class,'HelpCenterBanner_back_button')]"));
    private static final Locator BOOKING_CODE = new Locator("BOOKING_CODE", AppiumBy.xpath("//div[contains(@class,'styles_booking_code')]//p"));
    private static final Locator COPY_BOOKING_CODE = new Locator("COPY_BOOKING_CODE", AppiumBy.xpath("//*[@data-testid='copy-button']"));
    private static final Locator DURATION_DIRECT_INFO = new Locator("DURATION_DIRECT_INFO", AppiumBy.xpath("//div[contains(@class,'DetailReservation_flight_duration')]"));
    public static final Locator ORDER_ID_TOP = new Locator("ORDER_ID_TOP", AppiumBy.xpath("//span[contains(text(),'Order ID:') or contains(text(),'ID Pesanan:')]"));
    private static final Locator PASSENGERS_AND_FACILITIES_SECTION = new Locator("PASSENGERS_AND_FACILITIES_SECTION", AppiumBy.xpath("//h2[normalize-space()='Passengers & Facilities']"));
    private static final Locator PASSENGER_NAME = new Locator("PASSENGER_NAME", AppiumBy.xpath("//*[contains(@class,'PaxFacilityGroup_detail_contents_title')]"));
    private static final Locator PASSENGER_SEAT = new Locator("PASSENGER_SEAT", AppiumBy.xpath("//*[contains(@class,'FacilityDetails_facility_title')]"));
    private static final Locator PASSENGER_FACILITIES = new Locator("PASSENGER_FACILITIES", AppiumBy.xpath("//*[contains(@class,'FacilityDetails_facility_icons')]"));
    private static final Locator SUBMIT_CLAIM_BUTTON = new Locator("SUBMIT_CLAIM_BUTTON", AppiumBy.xpath("//p[text()='Submit claim']"));
    private static final Locator LPG_PAGE_TITLE = new Locator("LPG_PAGE_TITLE", AppiumBy.xpath("//*[@text=\"Lowest Price Guarantee\"]"));
    private static final Locator READ_BEFORE_CLAIM = new Locator("READ_BEFORE_CLAIM", AppiumBy.xpath("//*[@text=\"Read Before Claim\"]"));
    private static final Locator READ_BEFORE_CLAIM_BOTTOM_SHEET = new Locator("READ_BEFORE_CLAIM_BOTTOM_SHEET", AppiumBy.xpath("//*[contains(@text, 'Read Before Claim')]"));
    private static final Locator FINAL_PRICE_INPUT = new Locator("FINAL_PRICE_INPUT", AppiumBy.xpath("//android.widget.EditText[@resource-id='final-price']"));
    private static final Locator URL_INPUT = new Locator("URL_INPUT", AppiumBy.xpath("//android.widget.EditText[@resource-id='url']"));
    private static final Locator UPLOAD_BUTTON = new Locator("UPLOAD_BUTTON", AppiumBy.xpath("//android.widget.Button[@resource-id='upload']"));
    private static final Locator HAMBURGER_MENU = new Locator("HAMBURGER_MENU", AppiumBy.xpath("//*[@class='android.widget.ImageButton']"));
    private static final Locator IMAGES_MENU_ITEM = new Locator("IMAGES_MENU_ITEM", AppiumBy.xpath("//*[@text='Images']"));
    private static final Locator IMAGES_FOLDER = new Locator("IMAGES_FOLDER", AppiumBy.xpath("//*[@resource-id='android:id/title' and @text='images']"));
    private static final Locator FIRST_IMAGE_THUMBNAIL = new Locator("FIRST_IMAGE_THUMBNAIL", AppiumBy.xpath("(//*[@class='android.widget.ImageView' and @resource-id='com.google.android.documentsui:id/icon_thumb'])[1]"));
    private static final Locator TNC_CHECKBOX = new Locator("TNC_CHECKBOX", AppiumBy.xpath("//*[@class=\"android.view.View\" and ./*[@class=\"android.widget.CheckBox\"]]"));
    private static final Locator CLAIM_NOW_BUTTON = new Locator("CLAIM_NOW_BUTTON", AppiumBy.xpath("//*[@text=\"Claim now\"]"));
    private static final Locator HIGH_PRICE_ERROR_MSG = new Locator("HIGH_PRICE_ERROR_MSG", AppiumBy.xpath("//*[@text=\"Must be lower than your total payment on tiket.com\"]"));
    private static final Locator SEE_GUIDELINE_LINK = new Locator("SEE_GUIDELINE_LINK", AppiumBy.xpath("//*[@text=\"See guideline\"]"));
    private static final Locator SCREENSHOT_GUIDELINE_TITLE = new Locator("SCREENSHOT_GUIDELINE_TITLE", AppiumBy.xpath("//*[contains(@text, 'screenshot')]"));
    private static final Locator HIDE_PROMO_BANNER_BUTTON = new Locator("HIDE_PROMO_BANNER_BUTTON", AppiumBy.xpath("//p[text()='Hide' or text()='Tutup']"));
    private static final Locator POPUP_CLAIM_NOW_BUTTON = new Locator("POPUP_CLAIM_NOW_BUTTON", AppiumBy.xpath("//*[@text=\"Claim now\"]"));
    private static final Locator WAITING_FOR_APPROVAL_STATUS = new Locator("WAITING_FOR_APPROVAL_STATUS", AppiumBy.xpath("//*[contains(text(), 'Waiting for approval') or contains(@text, 'Waiting for approval')]"));

    @Override
    public int getRoomAmount() {
        driverTools.switchToWebView();
        String[] amount = driverTools.getText(ROOM_AMOUNT_INFO).orElseThrow().split(" ");
        driverTools.switchToNative();
        return Integer.parseInt(amount[0]);
    }

    @Override
    public int getRommGuestAmount() {
        driverTools.switchToWebViewUrl("yourorder");
        String[] amount = driverTools.getText(ROOM_GUEST_INFO).orElseThrow().split(" ");
        driverTools.switchToNative();
        return Integer.parseInt(amount[0]);
    }

    @Override
    public void seeRoomDetails(int index) {
        driverTools.switchToWebView();
        driverTools.swipeToElement(seeRoomDetailsButton(index), Swipe.UP, 5);
        driverTools.click(seeRoomDetailsButton(index));
        driverTools.switchToNative();
    }

    private Locator seeRoomDetailsButton(int index) {
        //increment index since xpath index start from 1
        return new Locator("SEE_ROOM_DETAILS_" + index, AppiumBy.xpath("(//*[contains(@class,'HOTEL_ROOM_DETAIL')]//*)[" + (index+1) + "]"));
    }

    @Override
    public void openTabRefundRescheduleDetailRoom() {
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        driverTools.click(REFUND_RESCHEDULE_TAB_BUTTON);
        driverTools.switchToNative();
    }

    @Override
    public void openRefundRequestDetailRoom() {
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        driverTools.swipeToElement(REFUND_REQUEST_BUTTON, Swipe.UP, 3);
        driverTools.click(REFUND_REQUEST_BUTTON);
        driverTools.switchToNative();
    }

    @Override
    public String getRoomStatus(String roomName) {
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        String roomStatus = driverTools.getText(roomStatus(roomName)).orElseThrow();
        driverTools.switchToNative();
        return roomStatus;
    }

    private Locator roomStatus(String roomName) {
        return new Locator("ROOM_STATUS", AppiumBy.xpath("//*[contains(@class,'room_name')]//span[text()='" + roomName + "']//following::*[contains(@class,'NotificationBanner_title')]"));
    }

    @Override
    public void seeMoreDetails() {
        String targetUrl = "yourorder";
        driverTools.switchToWebViewUrl(targetUrl);
        driverTools.swipeToElement(SEE_MORE_DETAILS_BUTTON, Swipe.UP, 5).orElseThrow();
        driverTools.click(SEE_MORE_DETAILS_BUTTON);
        driverTools.switchToNative();
    }

    @Override
    public boolean refundBannerInfo() {
        return driverTools.waitForElementToAppear(REFUND_BANNER_INFO).isPresent();
    }

    @Override
    public boolean refundNotAvailable() {
        return driverTools.waitForElementToAppear(REFUND_NOT_AVAILABLE).isPresent();
    }

    private void dismissCrossSellOverlayIfPresent() {
        if (driverTools.isElementDisplayed(HIDE_BUTTON)) {
            logger.log("Dismissing cross-sell overlay via Hide button");
            try {
                driverTools.clickWithJavascriptExecutor(HIDE_BUTTON);
            } catch (Exception e) {
                logger.log("JS click on Hide failed, falling back to regular click: " + e.getMessage());
                driverTools.click(HIDE_BUTTON);
            }
        }
    }

    @Override
    public void clickReschedule() {
        String targetUrl = "yourorder";
        driverTools.switchToWebViewUrl(targetUrl);
        dismissCrossSellOverlayIfPresent();
        List<Locator> titles = new ArrayList<Locator>();
        titles.add(HOTEL_RESERVATION_TITLE);
        driverTools.click(titles,driverTools::isElementDisplayed);
        if (driverTools.isElementDisplayed(MANAGE_ORDER_BUTTON)) {
            dismissCrossSellOverlayIfPresent();
            driverTools.click(MANAGE_ORDER_BUTTON);
        }
        driverTools.waitForElementToAppear(MANAGE_ORDER_SECTION, 10);
        clickRescheduleActionAndWaitForNavigation(targetUrl);
    }


    @Override
    public void clickReschedule(String orderId) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl(orderId, 3);
        if (driverTools.isElementDisplayed(HIDE_BUTTON)) {
            driverTools.click(HIDE_BUTTON);
        }
        dismissCrossSellOverlayIfPresent();
        List<Locator> titles = new ArrayList<Locator>();
        titles.add(HOTEL_RESERVATION_TITLE);
        driverTools.click(titles,driverTools::isElementDisplayed);
        if (driverTools.isElementDisplayed(MANAGE_ORDER_BUTTON)) {
            dismissCrossSellOverlayIfPresent();
            driverTools.click(MANAGE_ORDER_BUTTON);
        }
        driverTools.swipeToElement(RESCHEDULE_BUTTON, Swipe.UP, 5);
        if (driverTools.element(RESCHEDULE_BUTTON).isEmpty()) driverTools.scrollIntoWebView(RESCHEDULE_BUTTON);
        driverTools.scrollElementToCenter(RESCHEDULE_BUTTON);
        driverTools.click(RESCHEDULE_BUTTON);
        driverTools.waitForElementToAppear(MANAGE_ORDER_SECTION, 10);
        if (driverTools.element(RESCHEDULE_BUTTON).isEmpty()) {
            driverTools.swipeToElement(RESCHEDULE_BUTTON, Swipe.UP, 10);
            driverTools.clickWithJavascriptExecutor(RESCHEDULE_BUTTON);
        }
    }

    /**
     * Reschedule is a React ActionButton — JS el.click() often does not navigate.
     * Use Actions (real mouse events), handle optional room-type sheet, then wait for reschedule URL.
     */
    private void clickRescheduleActionAndWaitForNavigation(String roomTypeContextUrl) {
        driverTools.scrollIntoWebView(RESCHEDULE_BUTTON);
        try {
            driverTools.clickWithActions(RESCHEDULE_BUTTON);
        } catch (Exception e) {
            logger.log("Actions click on Reschedule failed, falling back to regular click: " + e.getMessage());
            driverTools.clickElementByCoordinate(RESCHEDULE_BUTTON);
        }
        // Optional room-type bottom sheet can appear before / during navigation
        chooseRoomType(roomTypeContextUrl);
        driverTools.waitForWebview();
        String rescheduleUrl = driverTools.switchToWebViewUrl("reschedule", 10);
        logger.log("Reschedule page URL after click: " + rescheduleUrl);
        driverTools.switchToNative();
    }

    @Override
    public boolean isInfoPopupDisplayed() {
        return driverTools.waitForElementToPresence(INFO_POPUP).isPresent();
    }

    @Override
    public void closeInfoPopup() {
        driverTools.click(OK_BUTTON);
    }

    @Override
    public void goToPreviousOrderBanner() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.swipeDown(5);
        driverTools.swipeToElement(PREVIOUS_ORDER_BUTTON_WEBVIEW, Swipe.DOWN, 8).orElseThrow();
        driverTools.switchToNative();
    }

    @Override
    public void clickPreviousOrder() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.click(PREVIOUS_ORDER_BUTTON_WEBVIEW);
        driverTools.switchToNative();
    }

    @Override
    public void clickPreviousOrder(String orderId) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId="+orderId);
        driverTools.scrollIntoWebView(PREVIOUS_ORDER_BUTTON_WEBVIEW);
        driverTools.clickWithJavascriptExecutor(PREVIOUS_ORDER_BUTTON_WEBVIEW);
        driverTools.switchToNative();
    }

    @Override
    public void clickNewScheduleButton(String orderId) {
        driverTools.switchToWebViewUrl(orderId);
        driverTools.swipeToElement(NEW_SCHEDULE_BUTTON_WEBVIEW, Swipe.DOWN, 5);
        driverTools.clickWithJavascriptExecutor(NEW_SCHEDULE_BUTTON_WEBVIEW);
        driverTools.switchToNative();
    }

    @Override
    public String getCheckInDate() {
        driverTools.switchToWebView();
        String date = driverTools.getText(CHECKIN_DATE_WEBVIEW).orElseThrow();
        driverTools.switchToNative();
        return date;
    }

    @Override
    public String getEndDate() {
        WebElement endDateTextElement = driverTools.waitForElementToAppear(CHECKOUT_DATE).orElseThrow();
        String endDate = driverTools.getValueAttribute(endDateTextElement,"text");
        logger.log("endDate-> " + endDate);
        return endDate;    }

    @Override
    public String getCheckOutDate() {
        driverTools.switchToWebView();
        String date = driverTools.getText(CHECKOUT_DATE_WEBVIEW).orElseThrow();
        driverTools.switchToNative();
        return date;
    }

    private Locator getMenuLocator(String menu) {
        String[] menuTextArray = menu.split("/");
        return new Locator("ORDER_DETAIL_MENU", AppiumBy.xpath("//android.widget.FrameLayout//*[contains(@resource-id,'tv_title') and (@text='"+menuTextArray[0]+"' or @text='"+menuTextArray[1]+"')]"));
    }

    @Override
    public void openTTDGuestDetail(int order) {
        WebElement guestDetail= driverTools.waitForElementToAppear(getGuestDetailButton(order)).orElseThrow();
        driverTools.click(guestDetail);
    }

    private Locator getGuestDetailButton(int order) {
        return new Locator("GUEST_DETAIL_"+order, AppiumBy.xpath("//*[contains(@resource-id,'cv_card')]//*[contains(@resource-id,'iv_icon')]"));
    }

    @Override
    public String getTTDGuestFullName() {
        return getTTDGuestFullNameWebview();
    }

    @Override
    public void closeTTDGuestDetail() {
        try {
            closeTTDGuestDetailWebview();
        }catch (NoSuchElementException e){
            driverTools.back();
        }
    }

    @Override
    public String getETicketLabel() {
        driverTools.switchToWebViewUrl("yourorder");
        String eTicketLabel = driverTools.getText(ETICKET_LABEL).orElseThrow();
        driverTools.switchToNative();
        return eTicketLabel;
    }

    @Override
    public void clickSeeEticketFromManageOrder() {
        try{
            driverTools.switchToWebViewUrl("order");
            driverTools.scrollIntoWebView(ETICKET_ON_MANAGE_ORDER_SECTION_WEBVIEW);
            driverTools.clickWithJavascriptExecutor(ETICKET_ON_MANAGE_ORDER_SECTION_WEBVIEW);
            driverTools.switchToNative();
            return;
        }catch(Exception e){
            logger.log("Webview not found. Exceotion-> " + e.getMessage());
        }
        //in android only AT and car rental are native
        driverTools.swipeToElement(SEE_ETICKET_FROM_HEADER_NATIVE, Swipe.UP, 8);
        driverTools.click(SEE_ETICKET_FROM_HEADER_NATIVE);
    }

    @Override
    public void clickETicketLanguage(String language) {
        driverTools.switchToWebViewUrl("orderId");
        Locator languageLocator = resolveETicketLanguageLocator(language);
        driverTools.waitForElementToPresence(languageLocator);
        String pageSource = driverTools.getPageSource();
        logger.log("E-ticket language page DOM preview: " + pageSource.substring(0, Math.min(pageSource.length(), 800)));

        if (driverTools.isElementDisplayed(CHANGE_LANGUAGE_CONTAINER)) {
            clickETicketLanguageNewUi(language);
        } else {
            driverTools.click(languageLocator);
            if (driverTools.isElementDisplayed(languageLocator)) {
                driverTools.click(languageLocator);
            }
            driverTools.switchToNative();
            waitAfterLanguageSelection();
            chooseDefaultAppPdf();
        }
    }

    private void clickETicketLanguageNewUi(String language) {
        Locator languageOption = getETicketLanguageLocator(language);
        driverTools.waitForElementToPresence(languageOption);
        driverTools.clickWithJavascriptExecutor(languageOption);
        driverTools.waitForElementToDisappear(languageOption, 1);
        if(driverTools.isElementDisplayed(languageOption)) {
            try {
                driverTools.click(languageOption);
            } catch (ElementClickInterceptedException e) {
                logger.softWarning("click intercepted");
            }
        }
        driverTools.switchToNative();
        waitForNativePdfViewer();
    }

    private void waitForNativePdfViewer() {
        logger.log("Waiting for native PDF viewer after language selection");
        for (int i = 0; i < 15; i++) {
            handlePdfAppChooserIfNeeded();
            String pdfUrl = tryResolvePdfUrlFromWebView();
            if (pdfUrl != null) {
                capturedPdfUrl = pdfUrl;
                logger.log("PDF URL captured from webview: " + pdfUrl);
                driverTools.switchToNative();
                return;
            }
            if (driverTools.waitForElementToAppear(PDF_CONTENT, 1).isPresent()) {
                logger.log("Native PDF content detected");
                driverTools.dummyWait(3);
                return;
            }
            driverTools.dummyWait(1);
        }
        logger.log("Native PDF content not detected within timeout");
    }

    private void handlePdfAppChooserIfNeeded() {
        driverTools.waitForElementToPresence(LOADING_STATE, 2);
        driverTools.waitForElementToDisappear(LOADING_STATE);
        if (driverTools.waitForElementToAppear(JUST_ONCE_BTN).isEmpty()) {
            return;
        }
        if (driverTools.isElementDisplayed(PDF_APP_VIEWER)) {
            driverTools.click(PDF_APP_VIEWER);
        } else if (driverTools.isElementDisplayed(PDF_APP_DRIVE)) {
            driverTools.click(PDF_APP_DRIVE);
        }
        confirmPdfAppSelection();
    }

    private String tryResolvePdfUrlFromWebView() {
        try {
            AndroidDriver androidDriver = (AndroidDriver) driver;
            for (String context : androidDriver.getContextHandles()) {
                if (!context.contains("WEBVIEW_com")) {
                    continue;
                }
                androidDriver.context(context);
                try {
                    for (String window : androidDriver.getWindowHandles()) {
                        androidDriver.switchTo().window(window);
                        String url = androidDriver.getCurrentUrl();
                        if (url != null && url.contains(".pdf")) {
                            return url;
                        }
                    }
                } catch (Exception ignored) {
                    Object href = androidDriver.executeScript("return window.location.href");
                    if (href != null && href.toString().contains(".pdf")) {
                        return href.toString();
                    }
                }
            }
        } catch (Exception e) {
            logger.log("PDF URL webview lookup skipped: " + e.getMessage());
        } finally {
            driverTools.switchToNative();
        }
        return null;
    }

    private void waitAfterLanguageSelection() {
        driverTools.waitForElementToDisappear(CHANGE_LANGUAGE_CONTAINER, 15);
        logger.log("Waiting for PDF viewer to open after language selection");
        driverTools.dummyWait(3);
    }

    @Override
    public boolean isETicketLanguageOptionVisible(String language) {
        driverTools.switchToWebViewUrl("orderId");
        Locator target = resolveETicketLanguageLocator(language);
        boolean visible = driverTools.waitForElementToAppear(target, 10).isPresent();
        driverTools.switchToNative();
        return visible;
    }

    private Locator resolveETicketLanguageLocator(String language) {
        if (driverTools.waitForElementToAppear(CHANGE_LANGUAGE_CONTAINER, 5).isPresent()) {
            return getETicketLanguageLocator(language);
        }
        return (language.equalsIgnoreCase("en") || language.equalsIgnoreCase("english"))
                ? ETICKET_LANGUAGE_EN
                : ETICKET_LANGUAGE_ID;
    }

    private Locator getETicketLanguageLocator(String language) {
        String languageText = (language.equalsIgnoreCase("en") || language.equalsIgnoreCase("english"))
                ? "English"
                : "Bahasa Indonesia";
        return new Locator("ETICKET_LANGUAGE", AppiumBy.xpath(String.format("//*[contains(@class,'title') and text()='%s']", languageText)));
    }

    @Override
    public void chooseDefaultAppPdf() {
        driverTools.switchToNative();
        handlePdfAppChooserIfNeeded();
        driverTools.switchToNative();
    }

    private void confirmPdfAppSelection() {
        driverTools.dummyWait(1);
        if (driverTools.waitForElementToAppear(JUST_ONCE_BTN, 8).isPresent()) {
            driverTools.click(JUST_ONCE_BTN);
        } else if (driverTools.isElementDisplayed(ALWAYS_APP)) {
            driverTools.click(ALWAYS_APP);
        }
        driverTools.dummyWait(3);
    }

    @Override
    public String getPDFContentFile() {
        driverTools.switchToNative();
        if (driverTools.waitForElementToAppear(PDF_CONTENT, 5).isPresent()) {
            String pdfContent = driverTools.waitForElementToAppear(PDF_CONTENT, 30).orElseThrow().getAttribute("content-desc");
            return normalizePdfContent(pdfContent);
        }
        String pdfUrl = capturedPdfUrl != null ? capturedPdfUrl : tryResolvePdfUrlFromWebView();
        if (pdfUrl != null && pdfUrl.contains(".pdf")) {
            logger.log("Reading PDF from captured URL: " + pdfUrl);
            ETicketWeb<?> eTicketWeb = ETicketWeb.getInstance(session);
            return normalizePdfContent(eTicketWeb.pdfContent(pdfUrl));
        }
        driverTools.switchToNative();
        throw new RuntimeException("PDF content not found");
    }

    private String normalizePdfContent(String pdfContent) {
        byte[] utf8Bytes = pdfContent.getBytes(StandardCharsets.UTF_8);
        String encodedString = new String(utf8Bytes, StandardCharsets.UTF_8);
        encodedString = encodedString.replaceAll("\\n", " ").replaceAll("[^\\x00-\\x7F]", "").trim();
        logger.log("PDF content -> " + encodedString);
        return encodedString;
    }

    @Override
    public boolean isOrderRescheduledBannerExist() {
        WebElement reschduleBannner = driverTools.waitForElementToAppear(RESCHEDULE_BANNER).orElseThrow();
        return driverTools.isElementDisplayed(reschduleBannner);
    }

    @Override
    public String getFullOrderId() {
        WebElement orderId = driverTools.waitForElementToAppear(ORDER_ID,10).orElseThrow();
        String orderIDText = driverTools.getText(orderId);
        return orderIDText;
    }

    private Locator getLocatorsForATplaceVerification(String place) {
        return new Locator(place,AppiumBy.xpath("//div[contains(@class,'styles_trip_summary_id')]//p[contains(text(),'" + place + "')]"));
    }

    @Override
    public boolean isTrainDetailCorrect(String originStation, String destinationStation, String totalPrice) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder/detail");

        if(driverTools.waitForElementToAppear(SKIP_INFORMATION_BUTTON_WEBVIEW).isPresent()) {
            driverTools.click(SKIP_INFORMATION_BUTTON_WEBVIEW);
        }

        boolean isMainETicketShown = driverTools.isElementDisplayed(TRAIN_MAIN_E_TICKET_WEBVIEW);
        boolean isOriginStationShown = driverTools.isElementDisplayed(getStationLocator(originStation));
        boolean isDestinationStationShown = driverTools.isElementDisplayed(getStationLocator(destinationStation));

        logger.log("is e-ticket shown: " + isMainETicketShown);
        logger.log("is origin same: " + isOriginStationShown);
        logger.log("is destination same: " + isDestinationStationShown);

        WebElement totalPriceElement = driverTools.swipeToElement(TRAIN_TOTAL_PRICE_WEBVIEW, Swipe.UP, 3).orElseThrow();
        String actualTotalPrice = driverTools.getText(totalPriceElement);
        int actualPrice = CoreFunctions.getIntFromString(actualTotalPrice).orElseThrow();
        int expectedPrice = CoreFunctions.getIntFromString(totalPrice).orElseThrow();
        logger.log("expected total price: " + expectedPrice);
        logger.log("actual total price: " + actualPrice);
        boolean isComparePrice = actualPrice == expectedPrice;
        logger.log("is price same: " + isComparePrice);

        WebElement manageOrderElement = driverTools.swipeToElement(TRAIN_MANAGE_ORDER_WEBVIEW, Swipe.UP, 3).orElseThrow();
        boolean isManageOrderShown = driverTools.isElementDisplayed(manageOrderElement);
        logger.log("is manage order shown: " + isManageOrderShown);

        driverTools.switchToNative();

        return isMainETicketShown && isOriginStationShown && isDestinationStationShown && isComparePrice && isManageOrderShown;
    }

    private Locator getStationLocator(String stationCode) {
        return new Locator(stationCode, AppiumBy.xpath("//*[contains(@class,'DetailReservation') and contains(text(),'"+stationCode+"')]"));
    }

    public String getCarRentalName() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder");
        String carRentalName = driverTools.getText(ORDER_DETAIL_HEADER_TITLE).orElseThrow();
        logger.log("Car Rental Name: " + carRentalName);
        driverTools.switchToNative();
        return carRentalName;
    }

    @Override
    public String getCarRentalLocation() {
        String carRentalLocation = driverTools.getText(CAR_RENTAL_LOCATION_TEXT).orElseThrow();
        logger.log("Car Rental Location: " + carRentalLocation);
        return carRentalLocation;
    }

    @Override
    public String getCarRentalDriverOption() {
        String carRentalDriverOption = driverTools.getText(CAR_RENTAL_DRIVER_OPTION_TEXT).orElseThrow();
        if (carRentalDriverOption.equals("dengan sopir")) {
            carRentalDriverOption = "with driver";
        } else if (carRentalDriverOption.equals("tanpa sopir")) {
            carRentalDriverOption = "without driver";
        }
        logger.log("Car Rental Driver Option: " + carRentalDriverOption);
        return carRentalDriverOption;
    }

    @Override
    public String getCarRentalPickupTime() {
        String carRentalPickupTimeText = driverTools.getText(CAR_RENTAL_PICKUP_TIME_TEXT).orElseThrow();
        String[] carRentalPickupTimeArray = carRentalPickupTimeText.split(" - ");
        String carRentalPickupTime = carRentalPickupTimeArray[1];
        logger.log("Car Rental Pickup Time: " + carRentalPickupTime);
        return carRentalPickupTime;
    }

    @Override
    public String getCarRentalPassengerFullName() {
        driverTools.swipeToElement(CAR_RENTAL_PASSENGER_FULL_NAME_TEXT,Swipe.UP,3).orElseThrow();
        String carRentalPassengerFullName = driverTools.getText(CAR_RENTAL_PASSENGER_FULL_NAME_TEXT).orElseThrow();
        logger.log("Car Rental Passenger Full Name: " + carRentalPassengerFullName);
        return carRentalPassengerFullName;
    }

    @Override
    public String getCarRentalPassengerPhoneNumber() {
        driverTools.swipeToElement(CAR_RENTAL_PASSENGER_PHONE_NUMBER_TEXT,Swipe.UP,3).orElseThrow();
        String carRentalPassengerPhoneNumber = driverTools.getText(CAR_RENTAL_PASSENGER_PHONE_NUMBER_TEXT).orElseThrow();
        logger.log("Car Rental Passenger Phone Number: " + carRentalPassengerPhoneNumber);
        return carRentalPassengerPhoneNumber;
    }

    @Override
    public boolean isInOrderDetailPage() {
        return driverTools.waitForElementToAppear(ORDER_ID,40).isPresent();
    }

    @Override
    public String getRefundDetailDate() {
        String refundDescription = driverTools.getText(REFUND_DETAIL_DATE).orElseThrow();
        return refundDescription;
    }
    @Override
    public boolean isRescheduleDetailCorrect(String refundDetailDate) {
        String refundDescription = driverTools.getText(REFUND_DESCRIPTION).orElseThrow();
        String[] refundDetailDateText = refundDetailDate.split(" ");
        refundDetailDateText[1] = CoreFunctions.monthEngToId(refundDetailDateText[1]);
        String refundDetailDateIN = refundDetailDateText[0] + " " + refundDetailDateText[1] + " " + refundDetailDateText[2];
        logger.log(refundDetailDateIN);
        return refundDescription.contains(refundDetailDate) | refundDescription.contains(refundDetailDateIN);
    }

    @Override
    public void openRefundAndRescheduleInfo() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.scrollToElement(REFUND_RESCHEDULE_DETAILS);
        driverTools.click(REFUND_RESCHEDULE_DETAILS);
        driverTools.switchToNative();
    }

    @Override
    public String getOrderRefundPolicyValue(String totalPrice) {
        driverTools.switchToWebViewUrl("yourorder");
        WebElement iFrameElement = driverTools.waitForElementToAppear(REFUND_RESCHEDULE_IFRAME).orElseThrow();
        driverTools.switchToIframe(iFrameElement);
        WebElement refundValueElement = driverTools.waitForElementToAppear(REFUND_VALUE_WEBVIEW).orElseThrow();
        String refundValue = driverTools.getText(refundValueElement).toLowerCase();
        if (refundValue.contains("gratis") || refundValue.contains("free")) refundValue = "IDR 0";
        if (refundValue.contains("%")) {
            String[] split = refundValue.split(" ");
            int currencyIndex = findIndex(split, "%");
            refundValue = split[currencyIndex + 1].replace("(", "");
            double total = Math.round(convertPrice(totalPrice));
            double percentage = convertPrice(refundValue);
            double estimation = total * percentage / 100;
            refundValue = String.valueOf(estimation).replace(".0", "");
        }
        logger.log("refundValue -> " + refundValue);
        driverTools.switchBackFromIframe();
        driverTools.switchToNative();
        return refundValue;
    }

    @Override
    public void closeRefundAndRescheduleInfo() {
        driverTools.switchToWebView();
        driverTools.click(BOTTOMSHEET_CLOSE_BUTTON);
        driverTools.switchToNative();
    }

    @Override
    public void clickRefundButton() {
        String targetUrl = "orderId";
        String currentUrl = "";
        try{
            currentUrl = driverTools.switchToWebViewUrl(targetUrl, 3);
        }catch(io.appium.java_client.NoSuchContextException e) {
            logger.log("No webview context found in this order detail");
        }

        if(driverTools.isElementDisplayed(HIDE_BUTTON)){
            driverTools.click(HIDE_BUTTON);
        }
        if (currentUrl.equals("")) {
            driverTools.switchToNative();
            driverTools.swipeToElement(REFUND_BUTTON_NATIVE, Swipe.UP, 10).orElseThrow();
            driverTools.click(REFUND_BUTTON_NATIVE);
        }else{
            driverTools.scrollIntoWebViewMWeb(REFUND_BUTTON);
            driverTools.click(REFUND_BUTTON);
            driverTools.switchToNative();
        }
        chooseRoomType(targetUrl);
        RefundPage<T> refundPage = RefundPage.getInstance(session);
        refundPage.continueToRefundPopup();
    }

    @Override
    public void clickRefundButtonWithoutSkip(String orderID) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderID);
        driverTools.clickWithJavascriptExecutor(REFUND_BUTTON);
        driverTools.switchToNative();
    }

        public void clickAndOpenRescheduleNudgeButton() {
        String targetUrl = "orderId";
        String currentUrl = "";
        try{
            currentUrl = driverTools.switchToWebViewUrl(targetUrl, 3);
        }catch(io.appium.java_client.NoSuchContextException e) {
            logger.log("No webview context found in this order detail");
        }
        if (currentUrl.isEmpty()) {
            driverTools.switchToNative();
            driverTools.swipeToElement(REFUND_BUTTON_NATIVE, Swipe.UP, 10).orElseThrow();
            driverTools.click(REFUND_BUTTON_NATIVE);
            driverTools.waitForElementToDisappear(REFUND_BUTTON_NATIVE);
        }else{
            WebElement refundButton = driverTools.swipeToElement(REFUND_BUTTON, Swipe.UP, 6).orElseThrow();
            driverTools.click(refundButton);
            driverTools.waitForElementToDisappear(REFUND_BUTTON);
            driverTools.switchToNative();
        }
    }

    @Override
    public void goToTotalPayment() {
        driverTools.swipeToElement(TTD_TOTAL_PAYMENT, Swipe.UP, 5).orElseThrow();
    }

    @Override
    public String getTotalPayment(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.swipeToFullyVisibleElement(TTD_TOTAL_PAYMENT, Swipe.UP, 10).orElseThrow();
        if(driverTools.waitForElementToAppear(TTD_TOTAL_PAYMENT, 5).isPresent()){
            String result = driverTools.getText(TTD_TOTAL_PAYMENT).orElseThrow();
            driverTools.switchToNative();
            return result;
        }
        driverTools.switchToNative();
        return getTotalPaymentInFlightPreviewPage();
    }

    @Override
    public Double getTotalPaymentPriceDetails() {
        return 0.0; // Only for iOS
    }

    @Override
    public String getTotalPaymentHotel() {
        return getTotalPaymentWebView();
    }

    @Override
    public String getTTDProductTitle(){
        driverTools.switchToWebViewUrl("yourorder");
        WebElement productTitle = driverTools.swipeToElement(TTD_PRODUCT_TITLE, Swipe.UP,4).orElseThrow();

        if(driverTools.getText(productTitle).split("\n").length > 1){
            String productTitleText = driverTools.getText(productTitle).split("\n")[0];
            driverTools.switchToNative();
            return productTitleText;
        }

        String productTitleText = driverTools.getText(productTitle);
        driverTools.switchToNative();
        return productTitleText;
    }
    
    @Override
    public boolean isEditGuestNameButtonDisplayed() {
        driverTools.switchToWebViewUrl("yourorder");
        boolean isDisplayed = driverTools.swipeToElement(EDIT_BOOKING_BUTTON, Swipe.DOWN, 5).isPresent();
        driverTools.switchToNative();
        return isDisplayed;
    }

    @Override
    public void clickEditGuestName(){
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.swipeToElement(EDIT_BOOKING_BUTTON, Swipe.DOWN, 5);
        driverTools.click(EDIT_BOOKING_BUTTON);
        driverTools.switchToNative();
    }
    private Locator checkGuestNameEditPage(String guestName) {
        return new Locator("Guest_Name", AppiumBy.xpath(""));
    }

    @Override
    public boolean checkRoomDetail(List<String> guestNames) {
        driverTools.switchToWebView();
        driverTools.waitForElementToPresence(GUEST_NAME_DETAILS).orElseThrow();
        List<WebElement> guestNamesElement = driverTools.elements(GUEST_NAME_DETAILS).orElseThrow();
        int index = 0;
        for (WebElement guestNameElement : guestNamesElement) {
            String guestNameDisplayed = driverTools.getText(guestNameElement);
            String guestNameExpected = guestNames.get(index);
            Assertion.assertThat("Guest name should be contains: " + guestNameExpected, guestNameDisplayed, is(containsString(guestNameExpected)));
            index++;
        }
        driverTools.switchToNative();
        return true;
    }

    @Override
    public boolean checkDetailsEditPage(String orderID, String hotelName, List<String> guestNames) {
        boolean oldVersion = driverTools.waitForElementToAppear(ORDER_ID_EDIT_PAGE).isPresent();
        if (oldVersion) {
            WebElement orderIDTextElement = driverTools.waitForElementToAppear(ORDER_ID_EDIT_PAGE, 10).orElseThrow();
            String orderIDText = driverTools.getText(orderIDTextElement).split(" ")[2];
            String hotelNameEditPage = driverTools.getText(HOTEL_NAME_EDIT_PAGE).orElseThrow();
            return orderID.equals(orderIDText) && hotelNameEditPage.equals(hotelName) && driverTools.isElementDisplayed(EDIT_GUEST_FIELD);
        } else {
            logger.softWarning("The new version is visible");
            return true;
        }
    }
    @Override
    public void editGuestName(){
        boolean oldVersion = driverTools.waitForElementToPresence(EDIT_GUEST_FIELD).isPresent();
        if (oldVersion) {
            driverTools.click(EDIT_GUEST_FIELD);

            driverTools.click(EDIT_GUEST_FIELD_POPUP);
            driverTools.sendKeys(EDIT_GUEST_FIELD_POPUP, updateName);

            driverTools.click(SAVE_GUEST_NAME);

            driverTools.click(SAVE_GUEST_NAME_REVISE);

            driverTools.click(SAVE_GUEST_NAME_POPUP);
        } else {
            driverTools.switchToWebViewUrl("revise?form");
            driverTools.waitForElementToPresence(GUEST_DETAILS_PAGE);
            List<WebElement> fullNameField = driverTools.elements(FULL_NAME_FIELD).orElseThrow();
            for (int i = 0; i < fullNameField.size(); i++) {
                if (driverTools.waitForElementToAppear(randomSalutationLocator()).isPresent()) {
                    List<WebElement> salutationButton = driverTools.elements(randomSalutationLocator()).orElseThrow();
                    driverTools.click(salutationButton.get(i));
                }
                fullNameField = driverTools.elements(FULL_NAME_FIELD).orElseThrow();
                driverTools.click(fullNameField.get(i));
                driverTools.clearInput(fullNameField.get(i));
                driverTools.sendKeys(fullNameField.get(i), CoreFunctions.randomName() + " " + updateName + " autguest");
                driverTools.hideKeyboard();
            }
            driverTools.click(SAVE_BUTTON);
            driverTools.click(SUBMIT_CONFIRM);
            List<WebElement> otpField = driverTools.elements(VERIFY_PHONE_NUMBER_FIELD).orElseThrow();
            for (int i = 0; i < otpField.size(); i++) {
                driverTools.sendKeys(otpField.get(i), String.valueOf(i + 1));
            }
            driverTools.click(SEE_RESERVATION_DETAIL_BUTTON);
            driverTools.switchToNative();
        }
    }

    private Locator randomSalutationLocator() {
        Locator[] salutationLocatorList = {MR_SALUTATION_CHECKBOX, MRS_SALUTATION_CHECKBOX, MS_SALUTATION_CHECKBOX};
        return getRandomObject(salutationLocatorList);
    }

    @Override
    public boolean isGuestNameUpdated(){
        driverTools.switchToWebView();
        List<WebElement> guestNameElements;
        if (driverTools.waitForElementToAppear(GUEST_NAME_DETAILS).isPresent()) {
            guestNameElements = driverTools.elements(GUEST_NAME_DETAILS).orElseThrow();
        } else {
            guestNameElements = driverTools.elements(GUEST_NAME_ORDER).orElseThrow();
        }

        for (WebElement guestNameElement : guestNameElements) {
            String displayedGuestName = driverTools.getText(guestNameElement);
            Assertion.assertThat("Guest name should be contains \"" + updateName + "\" as a updated name", displayedGuestName, is(containsString(updateName)));
        }

        driverTools.switchToNative();
        return true;
    }

    @Override
    public boolean findOnPDF(String textToFind){
        driverTools.click(FIND_ON_PDF);
        driverTools.sendKeys(PDF_SEARCH_BOX,textToFind);
        boolean isTextAvailable = !driverTools.getText(PDF_SEARCH_STATUS).orElseThrow().equals("0 of 0");
        driverTools.back();
        if(!driverTools.isElementDisplayed(FIND_ON_PDF)){
            driverTools.back();
        }
        return isTextAvailable;
    }

    @Override
    public String getFullBlackoutDate(String startBlackoutDate, String endBlackoutDate){
        return CoreFunctions.getFullBlackoutDate(startBlackoutDate, endBlackoutDate);
    }

    @Override
    public String getTimeSlot() {
        driverTools.switchToWebViewUrl("yourorder");
        String dateTimeText = driverTools.getText(DATE_TIME_TEXT).orElseThrow();
        String[] splitString = dateTimeText.split(" • ");
        dateTimeText = splitString[splitString.length - 1];
        logger.log("dateTimeText after removing the date -> " + dateTimeText);
        Matcher matcher;
        if (dateTimeText.contains("-")) {
            Pattern timePattern = Pattern.compile("\\d{2}:\\d{2} - \\d{2}:\\d{2}");
            matcher = timePattern.matcher(dateTimeText);
        } else {
            Pattern timePattern = Pattern.compile("\\d{2}:\\d{2}");
            matcher = timePattern.matcher(dateTimeText);
        }
        if (matcher.find()) {
            return "time slot selected : " + matcher.group();
        } else {
            throw new RuntimeException("Time slot not found in the text: " + dateTimeText);
        }
    }

    @Override
    public void clickChatBtn(){//webview on android only. Ios still native
        driverTools.switchToWebViewUrl("detail");
        driverTools.waitForElementToAppear(SKIP_ROOM_DETAIL_HOTEL,3).ifPresent(driverTools::click);
        WebElement chatBtn = driverTools.waitForElementToAppear(CHAT_BTN).orElseThrow();
        driverTools.click(chatBtn);
        driverTools.switchToNative();  
    }

    public boolean isHotelNameDisplayed(String hotelName) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder");
        boolean hotelNameView = driverTools.isElementDisplayed(getHotelNameLocator(hotelName));
        driverTools.switchToNative();
        return hotelNameView;
    }

    @Override
    public boolean isHotelNameDisplayed(String hotelName, String orderId) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl(orderId);
        boolean hotelNameView = driverTools.isElementDisplayed(getHotelNameLocator(hotelName));
        driverTools.switchToNative();
        return hotelNameView;
    }

    private Locator getHotelNameLocator(String hotelName) {
        return new Locator("DISPLAYED_ROOM_NAME", AppiumBy.xpath("//*[contains(@class, 'styles_title_text__') and contains(text(), '"+hotelName+"') or contains(text(), '"+hotelName.toLowerCase()+"')]"));
    }

    public boolean isMainETiketCardDisplayed() {
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        driverTools.waitForElementToAppear(MAIN_ETICKET, 30).orElseThrow();
        boolean hotelEticket = driverTools.isElementDisplayed(MAIN_ETICKET);
        driverTools.switchToNative();
        return hotelEticket;
    }

    public String getOrderID() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder");

        WebElement orderIDTextElement = driverTools.waitForElementToAppear(ORDER_ID_WEBVIEW,10).orElseThrow();
        String orderIDText = driverTools.getText(orderIDTextElement);
        if(orderIDText.equals("Order ID")){
            driverTools.switchToNative();
            driverTools.switchToWebViewUrl("yourorder");
            orderIDText = driverTools.getText(ORDER_ID_WEBVIEW).orElseThrow();
        }
        orderIDText = orderIDText.split(" ")[2];
        driverTools.switchToNative();
        return orderIDText;
    }

    /**
     * Returns the per-itinerary {@code orderDetailId} (e.g. {@code "1900692290"}) shown
     * next to the "Order Detail ID:" label in the e-ticket card of the order detail
     * webview. Android renders both ids on this page (parent {@code orderId} via
     * {@link #getOrderID()}, detail id here), so each method targets its own anchor
     * without poaching the other's match. Wrapped in try/finally so the native context
     * is restored even if the webview lookup throws.
     */
    @Override
    public String getOrderDetailID() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder");
        try {
            WebElement detailIdElement = driverTools.waitForElementToAppear(ORDER_DETAIL_ID_WEBVIEW, 10)
                    .orElseThrow(() -> new RuntimeException("Order Detail ID element not found in Android webview"));
            String value = driverTools.getText(detailIdElement);
            if (value == null || value.isBlank()) {
                throw new RuntimeException("Order Detail ID element returned empty value");
            }
            return value.trim();
        } finally {
            driverTools.switchToNative();
        }
    }

    public boolean isETiketAndReceiptCardDisplayed() {
        driverTools.switchToWebView();
        boolean isETiketAndReceiptCardDisplayed = driverTools.swipeToElement(ETICKET_BUTTON,Swipe.UP,10).orElseThrow().isDisplayed();
        driverTools.switchToNative();
        return isETiketAndReceiptCardDisplayed;
    }

    public boolean isRoomNameDisplayed(String roomName) {
        driverTools.switchToWebView();
        Locator roomNameTextLocator = getRoomNameLocator(roomName);
        driverTools.swipeToElement(roomNameTextLocator, Swipe.UP, 5);
        boolean isRoomNameDisplayed = driverTools.isElementDisplayed(roomNameTextLocator);
        driverTools.switchToNative();
        return isRoomNameDisplayed;
    }

    private Locator getRoomNameLocator(String roomName) {
        return new Locator("DISPLAYED_ROOM_NAME", AppiumBy.xpath("//*[contains(@class,'styles_detail')]//*[contains(text(),'"+roomName+"')]"));
    }

    public String getTotalRoom() {
        driverTools.switchToWebViewUrl("yourorder");
        String totalRoom = driverTools.getText(TOTAL_ROOM).orElseThrow();
        // get the number only
        String[] preprocess = totalRoom.split(" ");
        totalRoom = preprocess[0];
        driverTools.switchToNative();
        return totalRoom;
    }

    public String getGuestFullName() {
        driverTools.switchToWebViewUrl("yourorder");
        String name = driverTools.getText(GUEST_FULL_NAME).orElseThrow();
        // remove the title
        String[] preprocess = name.split(" ", 2);
        name = preprocess[1];
        driverTools.switchToNative();
        return name;
    }

    public String getTotalAdult() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.scrollIntoWebView(TOTAL_ADULT);
        String total = driverTools.getText(TOTAL_ADULT).orElseThrow();
        // get the number only
        String[] preprocess =  total.split(" ");
        total = preprocess[0];
        driverTools.switchToNative();
        return total;
    }

    public String getBusPassengerDetails(){
        driverTools.switchToWebViewUrl("yourorder/v4/detail");
        driverTools.waitForElementToAppear(BUS_PASSENGER_DETAIL,10);
        String[] passengerDetails = driverTools.elements(BUS_PASSENGER_DETAIL).orElseThrow().stream().map(WebElement::getText).toArray(String[]::new);
        String result = String.join(", ", passengerDetails);
        logger.log("Passenger detail -> " + result);
        driverTools.switchToNative();
        return result;
    }

    public String getBusSeatNoDetail() {
        driverTools.switchToWebViewUrl("yourorder/v4/detail");
        String[] seatNoDetail = driverTools.elements(BUS_SEAT_NO_DETAIL).orElseThrow().stream().map(WebElement::getText).toArray(String[]::new);
        String seatNo = String.join(", ", seatNoDetail);
        logger.log("Bus seat no detail : " + seatNo);
        driverTools.switchToNative();
        return seatNo;
    }
    public boolean isETiketAndReceiptCardDisplayedNative() {
        return driverTools.isElementDisplayed(MAIN_ETICKET_NATIVE);
    }

    // Current usage is only for TTD Order Detail which is webview
    @Override
    public Boolean checkPriceDetailIsIn(String currency) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("detail?orderId");

        driverTools.scrollIntoWebView(TTD_TOTAL_PRICE);
        String ttdTotalPrice = driverTools.getText(TTD_TOTAL_PRICE).orElseThrow();
        driverTools.clickElementByCoordinateDWeb(TTD_TOTAL_PRICE_CHEVRON);
        List<String> ttdPriceTextList = driverTools.getTexts(TTD_ALL_PRICE_DETAIL_TEXT);
        if(!ttdTotalPrice.contains(currency)) {
            return false;
        }

        if (driverTools.isElementDisplayed(BOTTOMSHEET_CLOSE_BUTTON)) {
            driverTools.click(BOTTOMSHEET_CLOSE_BUTTON);
        } else {
            driverTools.back();
        }

        logger.log("ttdPriceTextList -> " + ttdPriceTextList);
        for (String e : ttdPriceTextList) {
            if(e.matches(".*\\d.*")) {
                if(!e.contains(currency)) {
                    driverTools.switchToNative();
                    return false;
                }
            }
        }

        driverTools.switchToNative();
        return true;
    }

    @Override
    public Boolean isShareReceiptButtonDisplayed() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("detail?orderId");
        boolean result = driverTools.element(SHARE_RECEIPT_BUTTON).isPresent();
        driverTools.switchToNative();
        return result;
    }

    @Override
    public Boolean isReceiptButtonWork(String currency) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("detail?orderId");

        driverTools.scrollElementToCenter(RECEIPT_BUTTON);
        driverTools.waitForElementToAppear(RECEIPT_BUTTON).ifPresent(driverTools::click);

        driverTools.switchToNative();

        List<WebElement> webElements = driverTools.waitForElementsToAppear(PDF_VIEWER_TEXT, 3).orElseThrow();
        boolean result = !webElements.isEmpty();
        driverTools.back();

        return result;
    }

    @Override
    public Boolean isRefundButtonDisplayed() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("detail?orderId");
        boolean result = driverTools.element(REFUND_BUTTON).isPresent();
        driverTools.switchToNative();
        return result;
    }
    @Override
    public void continueToPaymentTrainChangeSeat(){
        super.continueToPaymentTrainChangeSeat();
        driverTools.waitForElementToAppear(CONFIRM_CONTINUE_PAYMENT_TRAIN_CHANGE_SEAT,5);
        driverTools.click(CONFIRM_CONTINUE_PAYMENT_TRAIN_CHANGE_SEAT);
    }

    @SneakyThrows
    @Override
    public void choosNewTrainSeatWagoon(){
        super.waitForTrainSeatPageToLoad(); //webview load
        super.openTrainChangeSeatSelection();
        driverTools.swipeByDistance(TRAIN_SEAT_SELECTION_CONTAINER,Swipe.LEFT,0.75);
        super.selectRandomTrainSeat();
    }

    @Override
    public void clickBackFromPdf(){
        driverTools.back();
        try{
            driverTools.switchToWebViewUrl("order");
            if(driverTools.isElementDisplayed(PDF_CONTENT) || driverTools.isElementDisplayed(CHANGE_LANGUAGE_CONTAINER)){
                driverTools.clickOrFallbackToClickWithJavascriptExecutor(CLOSE_ETICKET_BTM_SHEET_WEBVIEW);;
            }
        }catch(Exception e){
            logger.log("Not in webview");
        } finally {
            driverTools.switchToNative();
        }
    }

    @Override
    public String getTotalPrice() {
        driverTools.switchToWebViewUrl("detail?orderId");
        driverTools.swipeToPresentedElement(TTD_TOTAL_PRICE, Swipe.UP, 10);
        String result = driverTools.getText(TTD_TOTAL_PRICE).orElseThrow();
        driverTools.switchToNative();
        return result;
    }

    private Locator getAddProductSectionLocator(String orderDetailSectionTitle){
        return new Locator("ADD_OTHER_PRODUCT_SECITON", AppiumBy.xpath("//*[contains(@class,'Layout_title') and contains(text(),'"+orderDetailSectionTitle+"')]"));
    }

    private Locator getAddProductBannerLocator(String orderDetailSectionTitle){
        return new Locator("ADD_OTHER_PRODUCT_BANNER", AppiumBy.xpath("//*[contains(@class,'Layout_title') and contains(text(),'"+orderDetailSectionTitle+"')]/following::a[contains(@class,'CardWithTextOverlay_card')]"));
    }

    @Override
    public boolean verifyCrossSellRedirection(String orderDetailSectionTitle,List<String> expectedUrl) {
        driverTools.switchToWebViewUrl("yourorder/detail");
        Locator addProductSectionLocator = getAddProductSectionLocator(orderDetailSectionTitle);
        driverTools.scrollIntoWebView(addProductSectionLocator);
        Assertion.assertThat("Verify add product section present", driverTools.isElementDisplayed(addProductSectionLocator), is(true));
        //Check chips
        Locator addProductBannerLocator = getAddProductBannerLocator(orderDetailSectionTitle);
        driverTools.scrollIntoWebView(addProductBannerLocator);
        List<WebElement> addProductBannerElement = driverTools.elements(addProductBannerLocator).orElseThrow();
        driverTools.click(addProductBannerElement.get(0));
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        String actualUrl = driverTools.getCurrentURL().replace("https://m.","");
        String expectedUrlIndex0 = expectedUrl.get(0).replace("https://www.","");
        Assertion.assertThat("Verify url is match", actualUrl, containsString(expectedUrlIndex0));
        driverTools.switchToNative();
        return actualUrl.contains(expectedUrlIndex0);
    }

    @Override
    public boolean isShareFileNameCorrect(String vertical, String orderId, String lang) {
        //click share button and then share btm sheet will appear
        vertical = vertical.substring(0, 1).toUpperCase() + vertical.substring(1);
        String fileName = "E-Receipt-"+vertical+"-ID-"+orderId+".pdf";
        String fileName2 = "E-Receipt-"+vertical+"-"+orderId+"-ID.pdf";
        String fileName3 = "E-Receipt-"+vertical+"-EN-"+orderId+".pdf";
        String fileName4 = "E-Receipt-"+vertical+"-"+orderId+"-EN.pdf";
        Locator fileNameLocator = new Locator("FILE_NAME", AppiumBy.xpath("//*[contains(@text,'"+fileName+"') or contains(@text,'"+fileName2+"') or contains(@text,'" + fileName3 + "') or contains(@text,'" + fileName4 + "')]"));
        if (vertical.equalsIgnoreCase("bus")) {
            fileNameLocator = new Locator("FILE_NAME", AppiumBy.xpath("//*[contains(@text,'E-Receipt') and (contains(@text,'-ID-') or contains(@text,'-EN-')) and contains(@text,'"+orderId+"')]"));
        }
        return driverTools.waitForElementToAppear(fileNameLocator,5).isPresent();
    }

    @Override
    public void clickEticketFromHeader() {
        try{
            driverTools.switchToWebViewUrl("yourorder");
            driverTools.scrollElementToCenter(SEE_ETICKET_FROM_HEADER_WEBVIEW);
            try {
                driverTools.click(SEE_ETICKET_FROM_HEADER_WEBVIEW);
            } catch (Exception e) {
                logger.log("Failed to click See Eticket from header in webview. Exception -> " + e.getMessage());
                driverTools.scrollDown(100);
                driverTools.clickElementByCoordinate(SEE_ETICKET_FROM_HEADER_WEBVIEW);
            }
            if (!driverTools.waitForElementToAppear(CHANGE_LANGUAGE_CONTAINER, 5).isPresent()
                    && !driverTools.getCurrentURL().contains("order-e-voucher")) {
                driverTools.switchToNative();
                chooseDefaultAppPdf();
                return;
            }
            driverTools.switchToNative();
            return;
        }catch(Exception e){
            logger.log("Webview not found. Exception -> " + e.getMessage());
        }
        //in android only AT and car rental are native
        if (driverTools.isElementDisplayed(SEE_ETICKET_FROM_HEADER_NATIVE)) {
            driverTools.click(SEE_ETICKET_FROM_HEADER_NATIVE);
        }
        driverTools.switchToNative();
    }

    @Override
    public void clickEticketFromHeader(String orderId) {
        driverTools.switchToWebViewUrl(orderId);
        driverTools.click(SEE_ETICKET_FROM_HEADER_WEBVIEW);
        if (driverTools.isElementDisplayed(SEE_ETICKET_FROM_HEADER_WEBVIEW))
            driverTools.clickWithJavascriptExecutor(SEE_ETICKET_FROM_HEADER_WEBVIEW);
        driverTools.switchToNative();
    }

    @Override
    public boolean isPdfContentOpen() {
        return driverTools.waitForElementToPresence(PDF_CONTENT,5).isPresent();
    }

    @Override
    public boolean verifyShareEticketBtmSheet(String vertical, String lang, String orderID) {
        //need to open eticket first -> clickShareEticket()
        switch (vertical.toLowerCase()) {
            case "hotel","ttd","train","bus","airport_transfer","flight","homes","ferry","car":
                return super.verifyShareEticketBtmSheetFromWebview();
            default:
                Locator fileNameLocator = new Locator("FILE_NAME_LOCATOR", AppiumBy.xpath("//*[contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'e-voucher-"+vertical.toLowerCase()+"-"+lang.toLowerCase()+"-"+orderID+"')]"));
                boolean isFileNameCorrect = driverTools.waitForElementToAppear(fileNameLocator,10).isPresent();
                driverTools.back();
                return isFileNameCorrect;
        }
    }

    @Override
    public void closeShareReceiptBtmSheet() {
        driverTools.clickSidePartOfElement(NATIVE_BTM_SHEET_OUTER_LAYOUT,SideOfElement.topmiddle);
    }

    @Override
    public boolean verifyAtContactDetail(String salutation,String name,String phoneNumber,String lang) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.scrollIntoWebView(AT_CONTACT_DETAIL);
        List<String> contactDetailTexts = driverTools.getTexts(AT_CONTACT_DETAIL);
        String atContactDetail = contactDetailTexts.get(1);
        String phoneNumberText = contactDetailTexts.get(2);
        Assertion.assertThat("Verify contact detail is the same as use filled in booking page", atContactDetail, containsString(name));
        Assertion.assertThat("Verify phone number is the same as use filled in booking page", phoneNumberText, containsString(phoneNumber));
        driverTools.switchToNative();
        return true;
    }

    @Override
    public boolean verifyAtCarDetailSection(String carType) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder");
        String carTypeName = driverTools.getText(AT_CAR_TYPE_NAME).orElseThrow();
        logger.log("Actual car type name : " + carTypeName );
        Assertion.assertThat("Verify car type name same as in booking page", carTypeName, equalTo(carType));
        String contactPhoneNumber = driverTools.getText(AT_CAR_DRIVE_CONTACT).orElseThrow();
        Assertion.assertThat("Verify contact phone numbe is present", contactPhoneNumber, not(blankOrNullString()));
        driverTools.switchToNative();
        return true;
    }

    @Override
    public boolean verifyOrderDetailHeader(String expectedTitle) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder");
        String orderDetailHeaderTitle = driverTools.getText(ORDER_DETAIL_HEADER_TITLE).orElseThrow();
        Assertion.assertThat("Verify order detail header title", orderDetailHeaderTitle, containsString(expectedTitle));
        driverTools.switchToNative();
        return true;
    }

    @Override
    public boolean veriyAtSummaryDetail(String origin, String destination) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.scrollIntoWebView(AT_TRIP_DETAIL_ENTRY);
        driverTools.scrollElementToCenter(AT_TRIP_DETAIL_ENTRY);
        driverTools.click(AT_TRIP_DETAIL_ENTRY);
        String actualOrigin = driverTools.getText(AT_TRIP_DETAIL_PICKUP).orElseThrow();
        logger.log("Actual origin : " + actualOrigin);
        Assertion.assertThat("Verify orgigin is the same", actualOrigin, equalTo(origin));
        String actualDestination = driverTools.getText(AT_TRIP_DETAIL_DESTINATION).orElseThrow();
        logger.log("Actual destination : " + actualDestination);
        Assertion.assertThat("Verify destination is the same", actualDestination, equalTo(destination));
        driverTools.back();
        driverTools.switchToNative();
        return true;
    }

    @Override
    public boolean verifyAtFacilitiesDetailPresent() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder");
        final Locator FACILITIES = new Locator("FACILITIES", AppiumBy.xpath("//span[text()='Facilities' or text()='Fasilitas']/../following-sibling::*//span[contains(@class,'styles_title')]"));
        driverTools.scrollIntoWebView(FACILITIES);
        List<String> facilities = driverTools.getTexts(FACILITIES);
        Assertion.assertThat("Verify facilities is longger than 0", facilities.size(), greaterThan(0));
        driverTools.switchToNative();
        return true;
    }

    @Override
    public void refreshOrderDetailPage(int numberOfSwipe) {
        driverTools.swipeDown(numberOfSwipe);
    }

    @Override
    public boolean checkAirportTransferDetail(String pickupPlace, String dropPlace) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder");
        logger.log("Expected pickup place : " + pickupPlace);
        logger.log("Expected drop place : " + dropPlace);
        driverTools.swipeUp();
        boolean isPickupPlace = driverTools.isElementDisplayed(getLocatorsForATplaceVerification(pickupPlace));
        boolean isDropPlace = driverTools.isElementDisplayed(getLocatorsForATplaceVerification(dropPlace));
        logger.log("is pickup Place same: " + isPickupPlace);
        logger.log("is drop place same: " + isDropPlace);
        driverTools.switchToNative();
        return isPickupPlace && isDropPlace;
    }

    @Override
    public String getAirportTransferTotalPrice() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.scrollIntoWebView(TOTAL_AMOUNT_AT);
        WebElement totalPriceElement = driverTools.element(TOTAL_AMOUNT_AT).orElseThrow();
        String actualTotalPrice = driverTools.getText(totalPriceElement);
        logger.log("actual TotalPrice: " + actualTotalPrice);
        driverTools.switchToNative();
        return actualTotalPrice;
    }

    private Locator getFerryDetailsLocator(String checkInDate) {
        checkInDate = checkInDate.replace("Optional[", "").replace("]", "");
        return new Locator("FERRY_DETAILS", AppiumBy.xpath("//*[contains(@class,'DetailReservation')]/p[contains(@class,'Text_text') and contains(text(),'" + checkInDate + "')]"));
    }

    @Override
    public Boolean verifyFerryCheckinDate(String BookingFormCheckIndate) {
        driverTools.switchToWebViewUrl("yourorder");
        boolean date = driverTools.waitForElementToPresence(getFerryDetailsLocator(BookingFormCheckIndate),15).isPresent();
        driverTools.switchToNative();
        return date;
    }

    @Override
    public Boolean verifyFerryRoute(String bookingFormRoute) {
        driverTools.switchToWebViewUrl("yourorder");
        bookingFormRoute = bookingFormRoute.replace("Optional[", "").replace("]", "");
        String[] routeParts = bookingFormRoute.split("[^a-zA-Z0-9]+");
        for (String routePart : routeParts) {
            if (!routePart.isEmpty()) {
                Locator locator = getFerryDetailsLocator(routePart.trim());
                if (!driverTools.isElementDisplayed(locator)) {
                    return false;
                }
            }
        }
        driverTools.switchToNative();
        return true;
    }

    @Override
    public boolean verifyCarRentalProductDetail(String fleetName, String carRentalType,String appLang) {
        return verifyCarRentalProductDetailWebview(fleetName, carRentalType, appLang);
    }

    @Override
    public Boolean verifyFerryPassengerNames(List<String> bookingFormPassengerNames) {
        driverTools.switchToWebViewUrl("yourorder");
        Map<String, String> salutationMap = new HashMap<>();
        salutationMap.put("Nyonya", "Ny.");
        salutationMap.put("Tuan", "Tn.");
        salutationMap.put("Nona", "Nn.");
        salutationMap.put("Tn.", "Tn.");
        salutationMap.put("Mr.", "Mr.");
        salutationMap.put("Mrs.", "Mrs.");
        salutationMap.put("Ms.", "Ms.");
        for (String bookingFormPassengerName : bookingFormPassengerNames) {
            String cleanedName = bookingFormPassengerName.replace("Optional[", "").replace("]", "").trim();

            String[] parts = cleanedName.split("\\s+", 2);
            if (parts.length < 2) {
                return false;
            }
            String formSalutation = parts[0];
            String formName = parts[1];
            String expectedSalutation = salutationMap.getOrDefault(formSalutation, formSalutation);
            // Construct the expected full name on the order detail page
            String expectedFullName = expectedSalutation + " " + formName;
            // Split the full name into individual components and verify each component
            String[] nameParts = expectedFullName.split("\\s+");
            for (String namePart : nameParts) {
                Locator locator = getFerryPassengerNameLocator(namePart);
                driverTools.swipeToElement(locator, Swipe.UP, 4);
                if (!driverTools.isElementDisplayed(locator)) {
                    driverTools.switchToNative();
                    return false; // If any part of the name is not displayed, return false
                }
            }
        }
        driverTools.switchToNative();
        return true;
    }


    @Override
    public Boolean verifyIssuedBarCode() {
        driverTools.switchToWebViewUrl("yourorder");
        boolean verifyBarCode = driverTools.isElementDisplayed(FERRY_BARCODE);
        driverTools.switchToNative();
        return verifyBarCode;
    }

    @SneakyThrows
    @Override
    public Boolean verifyIssuedViewEticket(String lang) {
        driverTools.waitForElementToAppear(SEE_FERRY_ETIKCET,40);
        driverTools.click(SEE_FERRY_ETIKCET);
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder");
        if (lang.equalsIgnoreCase("en")) {
            driverTools.waitForElementToAppear(LANG_POPUP_ENGLISH, 10).ifPresent(driverTools::click);
        } else {
            driverTools.waitForElementToAppear(LANG_POPUP_INDO, 10).ifPresent(driverTools::click);
        }
        driverTools.switchToNative();
        driverTools.waitForElementToAppear(FIREFOX).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(FIREFOX_2).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(JUST_ONCE_BTN, 10).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(VIEW_ETICKET_BTMSHEET, 20);
        boolean verifyViewETkt = driverTools.isElementDisplayed(VIEW_ETICKET_BTMSHEET);
        driverTools.back();
        return verifyViewETkt;
    }

    @Override
    public Boolean verifyIssuedShareEticket() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.swipeToElement(SHARE_RECIPT_,Swipe.UP,5);
        boolean verifyShareDetails = driverTools.isElementDisplayed(SHARE_RECIPT_);
        driverTools.switchToNative();
        return verifyShareDetails;
    }

    @Override
    public boolean verifyTTDLoyaltyDeduction() {
        driverTools.switchToWebViewUrl("detail?orderId");
        driverTools.scrollIntoWebView(TTD_TOTAL_PRICE);
        driverTools.clickElementByCoordinateDWeb(TTD_TOTAL_PRICE_CHEVRON);
        String discountElement = driverTools.getText(PRICE_DETAIL_DISCOUNT_COMPONENTS).orElseThrow();
        logger.log("Loyalty discount component present : " + discountElement);
        Assertion.assertThat("Loyalty discount component present", driverTools.isElementDisplayed(PRICE_DETAIL_DISCOUNT_COMPONENTS),
                is(true));
        List<WebElement> priceComponentsInPriceDetail = driverTools.elements(TTD_ALL_PRICE_DETAIL_TEXT).orElseThrow();
        logger.log("number of price component present : " + priceComponentsInPriceDetail.size());
        String totalPrice = driverTools.getText(BOTTOMSHEET_TOTAL_PRICE).orElseThrow();

        List<Double> priceComponentsNumbersValue = priceComponentsInPriceDetail.stream()
                .map(WebElement::getText)
                .filter(n -> n.matches(".*[0-9]+.*"))
                .map(CoreFunctions::convertPriceSafely)
                .toList();
        double sumOfPriceDetail = CoreFunctions.sumUpDouble(priceComponentsNumbersValue);
        sumOfPriceDetail /= 2;

        Assertion.assertThat("Verify total sum is the same", CoreFunctions.convertPriceSafely(totalPrice),
                equalTo(sumOfPriceDetail));
        driverTools.click(CLOSE_BTMSHEET);
        driverTools.switchToNative();

        return true; // Assuming that if the assertion passes, we return true
    }

    private Locator getFerryPassengerNameLocator(String name) {
        // Remove "Optional" wrapper if present
        name = name.replace("Optional[", "").replace("]", "");
        return new Locator("FERRY_DETAILS", AppiumBy.xpath("(//*[contains(text(),'" + name + "')])[1]"));
    }
    public boolean verifyDriverDetailExist() {
        return verifyDriverDetailExistWebview();
    }

    @Override
    public boolean verifyRentalDetail(String pickUpLocation, String dropOffLocation,String pickUpTime,String dropOffTime,String carRentalDuration) {
        return verifyRentalDetailWebview(pickUpLocation, dropOffLocation, pickUpTime, dropOffTime, carRentalDuration);
    }

    @Override
    public boolean verifyCarRrInfoSection() {
        return verifyCarRrInfoSectionWebview();
    }

    @Override
    public boolean isLoginChallengeShown() {
        boolean loginChanlengeShown = driverTools.waitForElementToAppear(LOGIN_CHALLENGE_BTN,3).isPresent();
        driverTools.click(LOGIN_CHALLENGE_CLOSE_BTN);
        return loginChanlengeShown;
    }

    @Override
    public boolean openPriceDetailNative() {
        driverTools.swipeToElement(ORDER_DETAIL_NATIVE_CHEVRON, Swipe.UP, 15);
        driverTools.swipeUp();
        driverTools.click(ORDER_DETAIL_NATIVE_CHEVRON);
        return true;
    }

    @Override
    public boolean verifyCrPriceDetailComponentCorrect() {
        return verifyFerryPriceDetailBreakDown();
    }

    @Override
    public boolean verifyHotelPriceDetail(String currency) { //currency for ios only
        return verifyFerryPriceDetailBreakDown();
    }

    @Override
    public boolean verifyHotelCashbackPriceDetail(String currency) {
        return verifyCashbackPriceDetailBreakDown();
    }

    @Override
    public boolean verifyHelpCenterSectionPresent(){
        return verifyHelpCenterSectionPresentWebview();
    }

    @Override
    public boolean verifyCarManageOrder() {
        return verifyCarManageOrderWebview();
    }

    @Override
    public void clickShareEticketNative() {
        driverTools.click(SHARE_ETICKET_FROM_HEADER);
    }

    @Override
    public void shareReceiptNative() {
        driverTools.swipeToElement(NATIVE_SHARE_RECEIPT_BTN, Swipe.UP, 3);
        driverTools.click(NATIVE_SHARE_RECEIPT_BTN);
    }

    @Override
    public List<String> getFlightsPassengerList() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("detail");
        List<String> passengerList = driverTools.getTexts(PASSENGER_NAMES);
        logger.log("DEBUG: " + passengerList.get(0));
        driverTools.switchToNative();
        return passengerList;
    }

    @Override
    public boolean verifyOrderDetailIdComponent() {
        driverTools.switchToWebViewUrl("yourorder/detail?");
        boolean detailId = driverTools.isElementDisplayed(ORDER_DETAIL_ID);
        driverTools.scrollElementToCenter(ORDER_DETAIL_ID_COPY);
        driverTools.click(ORDER_DETAIL_ID_COPY);
        boolean idCopiedPopup = driverTools.isElementDisplayed(ORDER_DETAIL_ID_COPIED);
        logger.log("Order detail ID component verification " + (detailId && idCopiedPopup ? "successful." : "failed."));
        driverTools.switchToNative();
        return detailId && idCopiedPopup;
    }

    @Override
    public Boolean verifyReviseSpecialRequest() {
        driverTools.waitForElementToAppear(EDIT_SPECIAL_REQUEST);
        driverTools.click(EDIT_SPECIAL_REQUEST);
        driverTools.isElementDisplayed(SPECIAL_REQUEST_CHANGE);
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_CHANGE).ifPresent(WebElement::click);
        driverTools.sendKeys(SPECIAL_REQUEST_TEXTFIELD,"Test special request SDET");
        driverTools.hideKeyboard();
        driverTools.click(SAVE_SPECIAL_REQUEST);
        driverTools.click(YES_SPECIAL_REQUEST);
        fillOtp();
        boolean specialRequestDone = driverTools.isElementDisplayed(SPECIAL_REQUEST_CHANGED_BANNER);
        driverTools.click(SEE_RESERVATION_DETAILS);
        return specialRequestDone;
    }

    private void fillOtp() {
        WebElement otpField = driverTools.waitForElementToAppear(OTP_FEILD).orElseThrow();
        otpField.sendKeys("1234");
    }

    @Override
    public Boolean verifyLocationRedirectionButton() {
        driverTools.switchToWebViewUrl("yourorder/detail?");
        driverTools.waitForElementToAppear(ORDER_ID);
        driverTools.swipeToElement(HOTEL_MAP_BTN, Swipe.UP, 12).orElseThrow();
        boolean isButtonDisplayed = driverTools.isElementDisplayed(HOTEL_MAP_BTN);
        driverTools.switchToNative();
        return isButtonDisplayed;
    }

    @Override
    public Boolean verifyRefundAndRescheduleInfo() {

        driverTools.switchToWebView();
        driverTools.swipeToElement(REFUND_RESCHEDULE_DETAILS, Swipe.UP, 10);
        driverTools.click(REFUND_RESCHEDULE_DETAILS);
        boolean refundRescheduleBtmSheet = driverTools.isElementDisplayed(REFUND_RESCHEDULE_BTMSHEET);
        driverTools.back();
        driverTools.switchToNative();
        return refundRescheduleBtmSheet;
    }

    @Override
    public boolean isRescheduleButtonDisplayed() {
        driverTools.switchToWebViewUrl("yourorder/detail?");
        boolean rescheduleButton = driverTools.isElementDisplayed(RESCHEDULE_BUTTON);
        driverTools.switchToNative();
        return rescheduleButton;
    }

    @Override
    public boolean verifyPageModule() {
        driverTools.switchToWebViewUrl("yourorder/detail?");
        boolean pageModule = driverTools.isElementDisplayed(PAGE_MODULE);
        driverTools.switchToNative();
        return pageModule;
    }

    @Override
    public boolean verifyEditGuestDetail() {
        driverTools.switchToWebViewUrl("yourorder/detail?");
        boolean editGuestDetail = driverTools.isElementDisplayed(EDIT_GUESTS_DETAILS);
        driverTools.switchToNative();
        return editGuestDetail;
    }

    @Override
    public boolean verifyLpgBanner() {
        driverTools.swipeUp();
        driverTools.swipeUp();
        driverTools.switchToWebViewUrl("yourorder/detail?");
        driverTools.swipeToElement(LPG_BANNER, Swipe.UP, 5);
        boolean lpgBanner = driverTools.isElementDisplayed(LPG_BANNER);
        driverTools.switchToNative();
        return lpgBanner;
    }

    @Override
    public boolean statusChange() {
        driverTools.dummyWait(5);
        driverTools.switchToWebViewUrl("yourorder/detail?");
        driverTools.swipeToElement(WAITING_FOR_APPROVAL_STATUS, Swipe.UP, 3);
        boolean isStatusUpdated = driverTools.isElementDisplayed(WAITING_FOR_APPROVAL_STATUS);
        driverTools.switchToNative();
        return isStatusUpdated;
    }

    @Override
    public void hidePromoBannerIfPresent() {
        driverTools.switchToWebViewUrl("yourorder/detail?");
        if (driverTools.isElementDisplayed(HIDE_PROMO_BANNER_BUTTON)) {
            driverTools.click(HIDE_PROMO_BANNER_BUTTON);
        }
        driverTools.switchToNative();
    }

    @Override
    public void clickSubmitClaimBtn() {
        driverTools.switchToWebViewUrl("yourorder/detail?");
        driverTools.swipeToElement(SUBMIT_CLAIM_BUTTON, Swipe.UP, 5);
        driverTools.clickWithJavascriptExecutor(SUBMIT_CLAIM_BUTTON);
        driverTools.switchToNative();
    }

    @Override
    public boolean verifyLowestPriceGuaranteePage() {
        driverTools.waitForElementToAppear(LPG_PAGE_TITLE,5);
        return driverTools.isElementDisplayed(LPG_PAGE_TITLE);
    }

    @Override
    public boolean openAndCloseReadBeforeClaim() {
        driverTools.click(READ_BEFORE_CLAIM);
        boolean isBottomSheetDisplayed = driverTools.isElementDisplayed(READ_BEFORE_CLAIM_BOTTOM_SHEET);
        driverTools.click(LPG_PAGE_TITLE);
        return isBottomSheetDisplayed;
    }

    @Override
    public void fillClaimDetails(String finalPrice, String url) {
        driverTools.sendKeys(FINAL_PRICE_INPUT, finalPrice);
        driverTools.sendKeys(URL_INPUT, url);
        checkTnC();
    }

    @Override
    public void uploadScreenshot() {
        logger.log("Starting native Android image upload flow.");
        // 1. Scroll to and click the main upload button
        driverTools.swipeToElement(UPLOAD_BUTTON, Swipe.UP, 2);
        driverTools.waitForElementToAppear(UPLOAD_BUTTON, 2);
        driverTools.click(UPLOAD_BUTTON);
        logger.log("Clicked main upload button.");
        // 2. Wait for the Android file picker and click the Hamburger menu (triple lines)
        driverTools.waitForElementToAppear(HAMBURGER_MENU, 7);
        driverTools.click(HAMBURGER_MENU);
        logger.log("Clicked hamburger menu.");
        // 3. Click on "Images" in the side drawer
        driverTools.waitForElementToAppear(IMAGES_MENU_ITEM, 7);
        driverTools.click(IMAGES_MENU_ITEM);
        logger.log("Selected 'Images' from the drawer.");
        // 4. Click on the "images" folder
        driverTools.waitForElementToAppear(IMAGES_FOLDER, 7);
        driverTools.click(IMAGES_FOLDER);
        logger.log("Opened the 'images' folder.");
        // 5. Click on the first image thumbnail
        driverTools.waitForElementToAppear(FIRST_IMAGE_THUMBNAIL, 7);
        driverTools.click(FIRST_IMAGE_THUMBNAIL);
        logger.log("Successfully selected the first image thumbnail.");
    }

    @Override
    public void checkTnC() {
        driverTools.swipeToElement(TNC_CHECKBOX, Swipe.UP, 2);
        driverTools.click(TNC_CHECKBOX);
        driverTools.click(CLAIM_NOW_BUTTON);
    }

    @Override
    public void clickClaimNow() {
        WebElement claim = driverTools.waitForElementToAppear(POPUP_CLAIM_NOW_BUTTON, 3).orElseThrow();
        driverTools.clickElementFractionWithinBounds(claim,0.75,0.5);
    }

    @Override
    public boolean verifyHighPriceValidationError() {
        return driverTools.isElementDisplayed(HIGH_PRICE_ERROR_MSG);
    }

    @Override
    public boolean verifySeeGuideline() {
        driverTools.click(SEE_GUIDELINE_LINK);
        boolean isGuidelineDisplayed = driverTools.isElementPresent(SCREENSHOT_GUIDELINE_TITLE);
        driverTools.click(LPG_PAGE_TITLE);
        return isGuidelineDisplayed;
    }


    @Override
    public boolean ButtonFullProtectionSubmitNewClaim() {
        driverTools.switchToWebViewUrl("yourorder/detail?");
        driverTools.swipeToElement(FULL_PROTECTION_SUBMIT_NEW_CLAIM,Swipe.UP,6);
        boolean fullProtectionSubmitNewClaim = driverTools.isElementDisplayed(FULL_PROTECTION_SUBMIT_NEW_CLAIM);
        driverTools.switchToNative();
        return fullProtectionSubmitNewClaim;
    }

    @Override
    public boolean clickCallBtn() {
        driverTools.switchToWebViewUrl("yourorder/detail?");
        boolean callButton = driverTools.isElementDisplayed(CALL_BUTTON);
        driverTools.switchToNative();
        return callButton;
    }

    @Override
    public String getImportantInfo(){
        return getImportantInfoWebview();
    }

    @Override
    public String getTTDPackageName(){
        return getTTDPackageNameWebview();
    }

    @Override
    public void openTTDGuestDetail(){
        openTTDGuestDetailWebview();
    }

    @Override
    public String getInsuranceTTD(){
        return getInsuranceTTDWebview();
    }

    @Override
    public void clickDetailTTD(){
        clickDetailTTDWebview();
    }

    @Override
    public void continueToPayment(){
        completePaymentFromOrderGroupPaymentPage();
    }

    @Override
    public String getRefundAndRescheduleText(){
        return getRefundAndRescheduleTextWebview();
    }

    @Override
    public boolean isCloseBottomSheetButtonPresent(){
        return isCloseBottomSheetButtonPresentWebview();
    }

    @Override
    public String getRoomName(int index){
        return getRoomNameWebview(index);
    }

    @Override
    public String getRefundInfo(int index) {
        return getRefundInfoWebview(index);
    }

    public void carRentalScrollToTop() {
        driverTools.swipeToElement(CAR_RENTAL_PRODUCT_NAME, Swipe.DOWN, 10).orElseThrow();
    }

    @Override
    public void carRentalClickScrollDownBtn() {
        driverTools.click(CR_SCROLL_DOWN_FLOATING_BTN);
    }

    @Override
    public void closePriceDetailNative() {
        driverTools.click(AT_CLOSE_DETAIL);
    }

    public void clickRoomToRefundByIndex(int index, String orderId) {
        this.webviewChooseRoomToRescheduleByIndex(index, orderId);
    }

    @Override
    public String getRefundHeader(){
        return getRefundHeaderWebview();
    }

    @Override
    public void goToRefundDetail(String orderId){
        driverTools.waitForElementToAppear(REFUND_DETAIL_BUTTON_NATIVE);
        driverTools.click(REFUND_DETAIL_BUTTON_NATIVE);
        driverTools.waitForElementToDisappear(REFUND_DETAIL_BUTTON_NATIVE);
    }

    @Override
    public boolean isRefundDetailButtonDisplayed(){
        return driverTools.waitForElementsToAppear(REFUND_DETAIL_BUTTON_NATIVE).isPresent();
    }

    @Override
    public void clickRefund() {

        driverTools.switchToWebViewUrl("yourorder/detail");

        driverTools.swipeToElement(REFUND_BUTTON_WHOOSH, Swipe.UP, 3);

        if (!driverTools.isElementDisplayed(REFUND_BUTTON_WHOOSH)) {
            driverTools.switchToNative();
            throw new NoSuchElementException("Refund button is not displayed");
        }

        driverTools.clickWithJavascriptExecutor(REFUND_BUTTON_WHOOSH);
        driverTools.switchToNative();
    }

    @Override
    public void clickLoginNow() {

        driverTools.switchToWebViewUrl("yourorder/detail");
        driverTools.waitForElementToAppear(LOGIN_NOW_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(LOGIN_NOW_BUTTON);
        driverTools.switchToNative();
    }
    @Override
    public String getBookingCode() {

        driverTools.switchToWebViewUrl("yourorder/detail");
        if (driverTools.isElementDisplayed(HIDE_BUTTON)) {
            driverTools.click(HIDE_BUTTON);
        }
        driverTools.click(COPY_BOOKING_CODE);

        String clipboard = driverTools.getClipboardText().trim();

        Assertion.assertThat(
                "Booking code should be copied successfully",
                clipboard.isEmpty(),
                is(false));

        return clipboard;
    }@Override
    public boolean verifyImportantInfo() {
        driverTools.switchToWebViewUrl("yourorder");
        if (driverTools.isElementDisplayed(HIDE_BUTTON)) {
            driverTools.click(HIDE_BUTTON);
        }
        driverTools.swipeToElement(IMPORTANT_INFO_DETAILS, Swipe.UP, 5);
        driverTools.click(IMPORTANT_INFO_DETAILS);

        driverTools.switchToNative();

        boolean opened = driverTools.isElementDisplayed(IMPORTANT_INFO_BACK);

        driverTools.click(IMPORTANT_INFO_BACK);

        return opened;
    }
    @Override
    public boolean verifyHelpCenter() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder");
        if (driverTools.isElementDisplayed(HIDE_BUTTON)) {
            driverTools.click(HIDE_BUTTON);
        }
        driverTools.swipeToElement(HELP_CENTER, Swipe.UP, 8);
        driverTools.click(HELP_CENTER);

        boolean opened = driverTools.isElementDisplayed(HELP_CENTER_BACK_BUTTON);

        driverTools.click(HELP_CENTER_BACK_BUTTON);
        driverTools.switchToNative();

        return opened;
    }

    @Override
    public boolean verifyContactCustomerCare() {
        driverTools.switchToWebViewUrl("yourorder");
        if (driverTools.isElementDisplayed(HIDE_BUTTON)) {
            driverTools.click(HIDE_BUTTON);
        }
        driverTools.swipeToElement(CONTACT_CUSTOMER_CARE, Swipe.UP, 8);
        driverTools.click(CONTACT_CUSTOMER_CARE);

        driverTools.back();

        driverTools.switchToNative();

        return true;
    }
    @Override
    public String getOrderId() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder");

        String orderIdText = String.valueOf(driverTools.getText(ORDER_ID_TOP));

        int separator = orderIdText.indexOf(":");
        return separator >= 0
                ? orderIdText.substring(separator + 1).trim()
                : orderIdText.trim();
    }
    @Override
    public boolean verifyDepartureTrainDetails(TrainData data) {

        driverTools.switchToWebViewUrl("yourorder/detail");
        driverTools.waitForElementToPresence(TRAIN_NAME, 20).orElseThrow();
        String actualOrigin = getOriginStationNameTrain().replaceAll("\\s*\\([^)]*\\)$", "");
        String actualDestination = getDestinationStationNameTrain().replaceAll("\\s*\\([^)]*\\)$", "");

        Assertion.assertThat("Origin station should match", actualOrigin, is(data.departureStation));
        Assertion.assertThat("Destination station should match", actualDestination, is(data.arrivalStation));
        Assertion.assertThat("Departure date should match", getDepartureDateTrain(), containsString(data.departureDate));

        driverTools.switchToWebViewUrl("yourorder/detail");
        driverTools.waitForElementToPresence(TRAIN_NAME, 20).orElseThrow();

        Assertion.assertThat("Train name should match", driverTools.getText(TRAIN_NAME).orElseThrow(), containsString(data.trainName));

        driverTools.swipeToElement(DURATION_DIRECT_INFO, Swipe.UP, 3);
        Assertion.assertThat("Duration and trip type should be displayed", driverTools.getText(DURATION_DIRECT_INFO).orElseThrow(), either(containsString("Direct")).or(containsString("Transit")));

        return true;
    }
    @Override
    public boolean verifyPassengerAndFacilities(TrainData data) {

        driverTools.switchToWebViewUrl("yourorder/detail");
        driverTools.swipeToElement(PASSENGERS_AND_FACILITIES_SECTION, Swipe.UP, 5);

        Assertion.assertThat("Passengers & Facilities section should be displayed", driverTools.isElementDisplayed(PASSENGERS_AND_FACILITIES_SECTION), is(true));

        Assertion.assertThat("Passenger name should be displayed",
                driverTools.isElementDisplayed(PASSENGER_NAME), is(true));

        Assertion.assertThat("Seat assignment should be displayed", driverTools.isElementDisplayed(PASSENGER_SEAT), is(true));

        Assertion.assertThat("Facilities should be displayed", driverTools.isElementDisplayed(PASSENGER_FACILITIES), is(true));

        driverTools.switchToNative();
        return true;
    }

}
