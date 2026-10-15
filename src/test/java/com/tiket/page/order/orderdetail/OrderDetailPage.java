package com.tiket.page.order.orderdetail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tiket.api.order.GETPageModuleOrderDetail;
import com.tiket.api.order.InternalOrderDetailApi;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.payment.PaymentPageAndroid;
import com.tiket.page.login.LoginPage;
import com.tiket.pojo.order.HotelInternalOrderDetail;
import com.tiket.pojo.order.OrderDetailPageModule;
import com.tiket.pojo.order.OrderDetailPageModule.BannerContents;
import com.tiket.pojo.train.CarTransferInternalOrderDetail;
import com.tiket.pojo.train.FerryInternalOrderDetail;
import com.tiket.pojo.train.TrainData;
import com.tiket.pojo.train.TrainInternalOrderDetail;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.core.Every;
import org.json.JSONObject;
import org.openqa.selenium.NoSuchContextException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static com.tiket.core.CoreFunctions.convertPrice;
import static com.tiket.core.CoreFunctions.findIndex;
import static com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPageIOS.LOADING_STATE;
import static com.tiket.page.order.orderdetail.OrderDetailPageAndroid.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public abstract class OrderDetailPage<T extends WebDriver> extends BasePage<T> {

    public OrderDetailPage(Session<T> session) {
        super(session);
    }

    private static final Locator TOTAL_PAYMENT_PAH = new Locator("TOTAL_PAYMENT_PAH", AppiumBy.xpath("//*[contains(@class,'card_container')]//*[contains(@class,'total_amount')]"));
    private static final Locator HIDE_BUTTON = new Locator("HIDE_BUTTON", AppiumBy.xpath("//*[normalize-space()='Hide' or normalize-space()='Tutup']"));
    public static final Locator BOTTOM_SHEET_HANDLE = new Locator("BOTTOM_SHEET_HANDLE", AppiumBy.xpath("//*[@data-testid='bottom-sheet-handle']"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("(//*[@type='XCUIElementTypeImage' and @accessible='true'])[1]"));
    private static final Locator REFUND_REASON_INPUT = new Locator("REFUND_REASON_INPUT", AppiumBy.xpath("//*[@class='reasons-container']//*[contains(@class,'btn-area')]"));
    private static final Locator REFUND_REASONS = new Locator("REFUND_REASONS", AppiumBy.xpath("//*[@class='bs-popup']//*[@role='checkbox']"));
    private static final Locator SUBMIT_REFUND_REASON_BUTTON = new Locator("SUBMIT_REFUND_REASON_BUTTON", AppiumBy.xpath("//*[@class='bs-popup']//*[@type='button']"));
    private static final Locator TOTAL_REFUND_ESTIMATION = new Locator("TOTAL_REFUND_ESTIMATION", AppiumBy.xpath("//*[@class='matrix-body'][1]//*[@class='b2 mobile high-emphasis hover tix-font v4'][2]"));
    private static final Locator DETAILS_BTN_TRAIN_PASSENGER_DETAILS_WEBVIEW = new Locator("DETAILS_BTN_TRAIN_PASSENGER_DETAILS_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'EntryPoint_generic_facilities_wrapper')]//*[contains(@class,'ActionButton')]/*[contains(@class,'Button')]"));
    private static final Locator GUEST_NAME_TRAIN_PASSENGER_DETAIL_WEBVIEW = new Locator("GUEST_NAME_TRAIN_PASSENGER_DETAIL_WEBVIEW", AppiumBy.xpath("//*[@data-testid='bottom-sheet-body']//*[contains(@class,'DetailsAccordion_facility_title')]"));
    private static final Locator GUEST_TYPE_TRAIN_PASSENGER_DETAIL_WEBVIEW = new Locator("GUEST_TYPE_TRAIN_PASSENGER_DETAIL_WEBVIEW", AppiumBy.xpath("//*[@data-testid='bottom-sheet-body']//*[contains(@class,'DetailsAccordion_facility_label')]//*[contains(@class,'Label_truncation_lines')]"));
    private static final Locator ARROW_TRAIN_PASSENGER_DETAIL_WEBVIEW = new Locator("ARROW_TRAIN_PASSENGER_DETAIL_WEBVIEW", AppiumBy.xpath("//div[contains(@class,'BottomSheet')]//div[contains(@class,'Accordion_accordion_button')]"));
    private static final Locator SEAT_TRAIN_PASSENGER_DETAIL_WEBVIEW = new Locator("SEAT_TRAIN_PASSENGER_DETAIL_WEBVIEW", AppiumBy.xpath("//*[@data-testid='collapse-container']//*[contains(@class,'DetailsAccordion_facility_data_column')]/*[not(contains(@class,'icon'))]/*[contains(@class,'Text')]"));
    private static final Locator CLOSE_GUEST_DETAIL_WEBVIEW = new Locator("CLOSE_GUEST_DETAIL_WEBVIEW", AppiumBy.xpath("//span[normalize-space()='Departure Train']"));
    private static final Locator TOTAL_PAYMENT_TRAIN_WEBVIEW = new Locator("TOTAL_PAYMENT_TRAIN_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'TotalAmount_total_amount_and_label') or contains(@class,'PaymentFooter_price')]//span"));
    private static final Locator BOOKING_CODE_WEBVIEW = new Locator("BOOKING_CODE_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'styles_booking_code__')]//p"));
    private static final Locator DEPARTURE_DATE = new Locator("FLIGHT_DEPARTURE_DATE", AppiumBy.xpath("(//span[normalize-space(.)='Departure' or normalize-space(.)='Pergi']/ancestor::div[contains(@class,'DetailReservation_border_ticket')][1]//div[contains(@class,'DetailReservation_content_flight')])[1]//div[contains(@class,'Text_text') and contains(@class,'Text_size_b2') and contains(@class,'Text_weight_bold')]"));
    private static final Locator RETURN_DATE = new Locator("FLIGHT_RETURN_DATE", AppiumBy.xpath("(//span[normalize-space(.)='Return' or normalize-space(.)='Pulang']/ancestor::div[contains(@class,'DetailReservation_border_ticket')][1]//div[contains(@class,'DetailReservation_content_flight')])[1]//div[contains(@class,'Text_text') and contains(@class,'Text_size_b2') and contains(@class,'Text_weight_bold')]"));
    private static final Locator BACK_BUTTON_WEBVIEW = new Locator("BACK_BUTTON_WEBVIEW", AppiumBy.xpath("//*[@data-testid='back_button']"));
    private static final Locator GUEST_DETAILS_NAME_WEBVIEW = new Locator("GUEST_DETAILS_NAME_WEBVIEW", AppiumBy.xpath("//*[text()='Detail Tamu' or text()='Guest Details']//parent::*[contains(@class,'detail_list') or contains(@class,'content_section_header')]//following-sibling::*[contains(@class,'detail_list') or contains(@class,'content_list')]//li"));
    private static final Locator SPECIAL_REQUEST_WEBVIEW = new Locator("SPECIAL_REQUEST_WEBVIEW", AppiumBy.xpath("//*[text()='Permintaan Khusus' or text()='Special Request']//parent::*[contains(@class,'detail_list') or contains(@class,'content_section_header')]//following-sibling::*[contains(@class,'detail_list') or contains(@class,'content_list')]"));
    private static final Locator TOTAL_PAYMENT_WEBVIEW = new Locator("TOTAL_PAYMENT_WEBVIEW", AppiumBy.xpath("(//*[contains(@class,'TotalAmount_total')]//span)[last()]"));
    private static final Locator CHANGE_TRAIN_SEAT_BTN = new Locator("CHANGE_TRAIN_SEAT_BTN", AppiumBy.xpath("//div[contains(@class,'styles_action_button') and span]"));
    private static final Locator REFUND_AND_RESCHEDULE_INFO_TEXT = new Locator("REFUND_AND_RESCHEDULE_INFO_TEXT", AppiumBy.xpath("//*[contains(@class,'BottomSheet_wrapper')]//following::*[text()]"));
    //slick-current will help locate current open tab '//div[contains(@class,'slick-current')]'
    private static final Locator TRAIN_EMPTY_SEATS_IN_CHANGE_SEAT_SELECTION = new Locator(
            "TRAIN_EMPTY_SEATS_IN_CHANGE_SEAT_SELECTION", AppiumBy.xpath(
            "//div[contains(@class,'slick-current')]//div[@class='seat-box']//div[contains(@class,'seat') and not(contains(@class,'not-available')) and not(contains(@class,'selected'))]"));
    private static final Locator CONFIRM_NEW_TRAIN_SEATS = new Locator("CONFIRM_NEW_TRAIN_SEATS", AppiumBy.xpath("//div[contains(@class,'apply-button-container')]"));
    private static final Locator TRAIN_SEAT_FROM_CHANGE_TRAIN_SEAT_MENU = new Locator("TRAIN_SEAT_FROM_CHANGE_TRAIN_SEAT_MENU", AppiumBy.cssSelector(".passenger-seat"));
    private static final Locator OPEN_CHANGE_SEAT_SELECTION = new Locator("OPEN_CHANGE_SEAT_SELECTION", AppiumBy.xpath("//p[contains(@class,'passenger-card-header-change')]"));
    private static final Locator CONTINUE_TO_PAYMENT_FROM_CHANGE_SEAT = new Locator("CONTINUE_TO_PAYMENT_FROM_CHANGE_SEAT", AppiumBy.xpath("//button[contains(@class,'v3-btn-yellow')]"));
    private static final Locator CLOSE_TRAIN_ORDER_DETAIL_BTM_SHEET = new Locator("CLOSE_TRAIN_ORDER_DETAI_BTM_SHEET", AppiumBy.xpath("//header[contains(@class,'BottomSheet_header')]//button"));
    private static final Locator TRAIN_PRICE_IN_CHANGE_SEAT_MENU = new Locator("TOTAL_PRICE_IN_CHANGE_SEAT_MENU", AppiumBy.xpath("//p[@class='total-price']//span"));
    private static final Locator REFUND_INFO = new Locator("REFUND_INFO", AppiumBy.xpath("    //*[contains(@class,'styles_update_view')]//*[contains(@class,'styles_title_text') and contains(text(),'Refund')]"));
    private static final Locator RECEIPT_BUTTON = new Locator("RECEIPT_BUTTON", AppiumBy.xpath("//*[contains(@class,'ManageOrder_action_buttons')]//*[contains(text(),'Bukti') or contains(text(),'Receipt')]"));
    // Updated 2026-05: the Manage Order section was redesigned. The old class names
    // (ManageOrder_content_info / ManageOrder_refund_tiket_icon) are gone; the
    // E-ticket button is now an ActionButton with a child <span> whose text is
    // "E-ticket" or "E-tiket". CSS-module class suffixes (e.g. "__hVmVR") are
    // build-hash-dependent so we match by class prefix.
    private static final Locator ETICKET_ON_MANAGE_ORDER_SECTION = new Locator(
            "ETICKET_ON_MANAGE_ORDER_SECTION",
            AppiumBy.xpath(
                    "//div[contains(@class,'ActionButton_action_button_wrapper')]"
                            + "[.//span[normalize-space(text())='E-ticket' or normalize-space(text())='E-tiket']]"));
    private static final Locator ADD_TO_CALENDAR_ON_MANAGE_ORDER_SECTION = new Locator("ADD_TO_CALENDAR_ON_MANAGE_ORDER_SECTION", AppiumBy.xpath("//*[contains(@class,'ManageOrder_action_buttons_wrapper')]//*[contains(text(),'Kalender') or contains(text(),'Calendar')]"));
    private static final Locator RR_INFO_SECTION = new Locator("RR_INFO_SECTION", AppiumBy.xpath("//*[contains(@class,'RefundRescheduleInfo_wrapper')]"));
    private static final Locator SHARE_RECEIPT_BUTTON = new Locator("SHARE_RECEIPT_BUTTON", AppiumBy.xpath("//*[contains(@class,'TotalAmount_share_reciept_button')]"));
    private static final Locator SHARE_ETICKET_WEBVIEW = new Locator("SHARE_ETICKET_WEBVIEW", AppiumBy.xpath("//*[@data-testid='navbar_action_SHARE_LINK_ETICKET' or @alt='SHARE_ETICKET']"));
    private static final Locator SHARE_ETICKET_BTM_SHEET = new Locator("SHARE_ETICKET_BTM_SHEET", AppiumBy.xpath("//div[contains(@class,'BottomSheet_body')]"));
    private static final Locator SHARE_ETICKET_BTM_SHEET_EMAIL_FIELD = new Locator("SHARE_ETICKET_BTM_SHEET_EMAIL_FIELD", AppiumBy.xpath("//input[@type='email' and @value='']"));
    private static final Locator SHARE_ETICKET_ADD_OTHER_RECEIPIENT = new Locator("SHARE_ETICKET_ADD_OTHER_RECEIPIENT", AppiumBy.xpath("//section[contains(@class,'BottomSheet')]//span[contains(text(),'Tambah') or contains(text(),'Add')]"));
    private static final Locator SHARE_ETICKET_CONFRIM_BTN = new Locator("SHARE_ETICKET_CONFIRM_BTN", AppiumBy.xpath("//section[contains(@class,'BottomSheet')]//button[contains(@class,'share')]"));
    private static final Locator HELP_CENTER_CARD = new Locator("HELP_CENTER_CARD", AppiumBy.xpath("//*[contains(@class,'ContactCustomerCare_container')]"));
    private static final Locator ORDER_DETAIL_CHEVRTON = new Locator("ORDER_DETAIL_CHEVRTON", AppiumBy.xpath("//div[contains(@class,'TotalAmount_price')]//*[contains(@class,'icon')]"));
    private static final Locator PRICE_DETAIL_COMPONENTS = new Locator("PRICE_DETAIL_COMPONENTS", AppiumBy.xpath("//div[contains(@class,'BottomSheet_body')]//div[contains(@class,'price_flex')]//following-sibling::*"));
    private static final Locator PRICE_DETAIL_COMPONENTS_FERRY = new Locator("PRICE_DETAIL_COMPONENTS_FERRY", AppiumBy.xpath("//div[contains(@class,'BottomSheet_body')]//div[contains(@class,'passenger') or contains(@class,'styles_accordion_title')]/span[last()]"));
    private static final Locator OTHERS_FEE = new Locator("OTHERS_FEE", AppiumBy.xpath("//div[contains(@class,'BottomSheet_body')]//span[contains(@class,'price_text') and preceding-sibling::*[contains(@class,'deduction')]]"));
    private static final Locator ROOM_TYPE_CHECKBOX = new Locator("ROOM_TYPE_CHECKBOX", AppiumBy.xpath("//*[contains(@class,'Radio_icon')]"));
    private static final Locator SUBMIT_ROOM_TYPE = new Locator("SUBMIT_ROOM_TYPE", AppiumBy.xpath("//*[contains(@class,'BottomSheet')]//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator RESCHEDULE_CARD_WEBVIEW = new Locator("RESCHEDULE_CARD_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'notification_banner')]"));
    private static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", AppiumBy.xpath("(//*[contains(@class, 'styles_title_text__')])[last()]"));
    private static final Locator CHECKIN_DATE_WEBVIEW = new Locator("CHECKIN_DATE_WEBVIEW", AppiumBy.xpath("//*[text()='Check-in']//following-sibling::*[contains(@class,'check_in_out_value')]"));
    private static final Locator TOTAL_NIGHT = new Locator("TOTAL_NIGHT", AppiumBy.xpath("//*[contains(@class, 'styles_number_of_nights')]/child::*[last()]"));
    private static final Locator NEW_SCHEDULE_WEBVIEW = new Locator("NEW_SCHEDULE_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'update_view_header') or contains(@class,'NotificationBanner_body')]"));
    private static final Locator REFUND_DETAIL_BUTTON = new Locator("REFUND_DETAIL_BUTTON", AppiumBy.xpath("//*[contains(@class,'title_text') and contains(text(),'Refund')]//following::*[contains(@class,'title_button') and contains(text(),'Detail')]"));
    private static final Locator ROOM_NAME_INFO = new Locator("ROOM_NAME_INFO", AppiumBy.xpath("//*[text()='Room' or text()='Kamar']//parent::*//following-sibling::*//*//span"));
    private static final Locator PREVIOUS_ORDER_BANNER_WEBVIEW = new Locator("PREVIOUS_ORDER_BANNER_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'card_header')]//*[contains(text(),'New Schedule') or contains(text(),'New schedule') or contains(text(),'jadwal baru') or contains(text(),'Order Update') or contains(text(),'Update Pesanan')]"));
    private static final Locator BACK_BUTTON_REFUND_RESCHEDULE_INFO = new Locator("BACK_BUTTON_REFUND_RESCHEDULE_INFO", AppiumBy.xpath("(//*[contains(@class,'detail_order_detail_wrapper')]//header//*)[1]"));
    //below is locator for prices outside card container
    private static final Locator PRICE_DETAIL_COMPONENTS_DISCOUNT_AND_OTHER_FEES = new Locator("PRICE_DETAIL_COMPONENTS_DISCOUNT_AND_OTHER_FEES", AppiumBy.xpath("//*[contains(@class,'price_text')]"));
    private static final Locator PRICE_DETAIL_TOTAL_PRICE_VALUE = new Locator("PRICE_DETAIL_TOTAL_PRICE_VALUE", AppiumBy.xpath("//div[contains(@class,'BottomSheet_body')]//span[contains(text(),'Total')]//following-sibling::*"));
    private static final Locator CASHBACK_AMOUNT = new Locator("CASHBACK_AMOUNT", AppiumBy.xpath("//*[contains(@class,'total_amount')]//following-sibling::*[contains(@class,'notes')]//*"));
    private static final Locator CLOSE_PRICE_DETAIL_WEBVIEW = new Locator("CLOSE_PRICE_DETAIL_BUS", AppiumBy.xpath("//button[contains(@class,'BottomSheet_close_button')]"));
    private static final Locator BUS_DEPARTURE_NAME = new Locator("BUS_DEPARTURE_NAME", AppiumBy.xpath("//div[contains(@class,'DetailReservation')]//div[contains(@class,'name')]"));
    private static final Locator BUS_DEPARTURE_DATE = new Locator("BUS_DEPARTURE_DATE", AppiumBy.xpath("(//div[contains(@class,'DetailReservation')]//div[contains(@class,'bold')])[1]"));
    private static final Locator BUS_RETURN_DATE = new Locator("BUS_RETURN_DATE", AppiumBy.xpath("(//div[contains(@class,'DetailReservation')]//div[contains(@class,'bold')])[2]"));
    private static final Locator BUS_FACILITY = new Locator("BUS_FACILITY", AppiumBy.xpath("//div[contains(@class,'DetailReservation_facility')]//span"));
    private static final Locator BUS_SEE_TRIP_DETAIL_BTN = new Locator("BUS_SEE_TRIP_DETAIL_BTN", AppiumBy.xpath("//div[@data-testid='see-trip-detail-button']//button"));

    private static final Locator BUS_DEPARTURE_NAME_TRIP_DETAIL = new Locator("BUS_DEPARTURE_NAME_TRIP_DETAIL", AppiumBy.xpath("//div[contains(@class,'BottomSheet_body')]//div[contains(@class,'DetailReservation_trip_info')]"));
    private static final Locator BUS_DEPARTURE_LOCATIONS_TRIP_DETAIL = new Locator("BUS_DEPARTURE_LOCATION_TRIP_DETAIL", AppiumBy.xpath("//div[contains(@class,'BottomSheet_body')]//div[contains(@class,'DetailReservation_stop_info')]"));
    private static final Locator BUS_FACILITIES_TRIP_DETAIL = new Locator("BUS_FACILITIES_TRIP_DETAIL", AppiumBy.xpath("//div[contains(@class,'BottomSheet_body')]//span[contains(@class,'DetailReservation_facility_title')]"));
    private static final Locator BUS_SEE_TRIP_DETAIL_CLOSE_BTN = new Locator("BUS_SEE_TRIP_DETAIL_CLOSE_BTN", AppiumBy.xpath("//*[contains(@class,'BottomSheet_close_button')]"));

    private static final Locator PRICE_DETAIL_TICKET_ONLY = new Locator("PRICE_DETAIL_TICKET_ONLY", AppiumBy.xpath("//*[contains(@class,'BottomSheet_bottom_sheet')]//*[contains(@class,'styles_accordion_title')]/*[2]"));
    private static final Locator REFUND_HOTEL_STATUS = new Locator("REFUND_HOTEL_STATUS", AppiumBy.xpath("//*[contains(@class,'styles_title_text') and (contains(text(),'Refund hotel') or contains(text(),'Refunding hotel'))]"));

    private static final Locator TTD_VALIDITY_PERIOD = new Locator("TTD_VALIDITY_PERIOD", AppiumBy.xpath("(//*[contains(text(),'Masa Berlaku') or contains(text(),'Validity Period')]//following::*[contains(@class,'DetailReservation_text_wrapper')]//p)[1]"));
    private static final Locator DEPART_AIRPORT_NAME_ORDER_GROUP = new Locator("DEPART_AIRPORT_NAME_ORDER_GROUP", AppiumBy.xpath("(//*[contains(@class,'group_card')]//*[contains(@class,'content_item')])[1]//div[3]//span[3]"));
    private static final Locator RETURN_AIRPORT_NAME_ORDER_GROUP = new Locator("RETURN_AIRPORT_NAME_ORDER_GROUP", AppiumBy.xpath("(//*[contains(@class,'group_card')]//*[contains(@class,'content_item')])[2]//div[3]//span[3]"));
    private static final Locator FLIGHT_DEPARTURE_DATE_DETAIL = new Locator("FLIGHT_DEPARTURE_DATE_DETAIL", AppiumBy.xpath("(//div[contains(@class,'DetailReservation_flight_subtitle')])[1]//preceding-sibling::*"));
    private static final Locator FLIGHT_ARRIVAL_DATE_DETAIL = new Locator("FLIGHT_ARRIVAL_DATE_DETAIL", AppiumBy.xpath("(//div[contains(@class,'DetailReservation_flight_subtitle')])[2]//preceding-sibling::*"));
    private static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON", AppiumBy.xpath("//button[contains(@class,'BottomSheet_close_button')]"));

    //flight order detail
    private static final Locator FLIGHT_ORDER_GROUP_TOTAL_PAYMENT = new Locator("FLIGHT_ORDER_GROUP_TOTAL_PAYMENT", AppiumBy.xpath("//*[contains(@class,'PaymentFooter_price')]//span"));
    private static final Locator FLIGHT_ORDER_GROUP_PAYMENT_DETAIL_CHEVRON = new Locator("FLIGHT_ORDER_GROUP_PAYMENT_DETAIL_CHEVRON", AppiumBy.xpath("//*[contains(@class,'PaymentFooter_price')]//*[name()='svg']"));
    private static final Locator FLIGHT_ORDER_GROUP_DEPART_CHEVRON = new Locator("FLIGHT_ORDER_GROUP_DEPART_CHEVRON", AppiumBy.xpath("//*[contains(@class,'styles_action_header_container') and *[(contains(text(),'Pergi') or contains(text(),'Depart'))]]"));
    private static final Locator FLIGHT_ORDER_GROUP_RETURN_CHEVRON = new Locator("FLIGHT_ORDER_GROUP_RETURN_CHEVRON", AppiumBy.xpath("//*[contains(@class,'styles_action_header_container') and *[(contains(text(),'Pulang') or contains(text(),'Return'))]]"));
    private static final Locator DEPART_AIRPORT_NAME_ORDER_DETAIL = new Locator("DEPART_AIRPORT_NAME", AppiumBy.xpath("(//div[contains(@class,'DetailReservation_flight_subtitle')])[1]"));
    private static final Locator RETURN_AIRPORT_NAME_ORDER_DETAIL = new Locator("RETURN_AIRPORT_NAME", AppiumBy.xpath("(//div[contains(@class,'DetailReservation_flight_subtitle')])[2]"));
    private static final Locator FLIGHT_ORDER_GROUP_DEPART_AIRLINE = new Locator("FLIGHT_ORDER_GROUP_DEPART_AIRLINE", AppiumBy.xpath("(//div[contains(@class, 'styles_content_list') and not(*[contains(text(), ',')])]//span)[1]"));
    private static final Locator FLIGHT_ORDER_GROUP_RETURN_AIRLINE = new Locator("FLIGHT_ORDER_GROUP_RETURN_AIRLINE", AppiumBy.xpath("(//div[contains(@class, 'styles_content_list') and not(*[contains(text(), ',')])]//span)[1]"));
    private static final Locator COMPLETE_PAYMENT_FROM_ORDER_GROUP = new Locator("COMPLETE_PAYMENT_FROM_ORDER_GROUP", AppiumBy.xpath("//*[contains(@class,'PaymentFooter')]//button"));
    private static final Locator COUNTDOWN = new Locator("COUNTDOWN", AppiumBy.xpath("//*[contains(@class,'Countdown_time')]"));
    private static final Locator IMPORTANT_INFO_CARD = new Locator("IMPORTANT_INFO_CARD", AppiumBy.xpath("//*[contains(@class,'ImportantInfo_important_info')]//*[text()]"));
    private static final Locator TTD_PACKAGE_NAME = new Locator("TTD_PACKAGE_NAME", AppiumBy.xpath("//*[contains(@class,'DetailReservation_subtitle')]//following-sibling::*[contains(@class,'text')]"));
    private static final Locator TTD_GUEST_DETAIL_BUTTON = new Locator("TTD_GUEST_DETAIL_BUTTON", AppiumBy.xpath("(//*[contains(@class,'DetailReservation_action_button')])[last()]"));
    private static final Locator TTD_INSURANCE = new Locator("TTD_INSURANCE", AppiumBy.xpath("(//*[contains(@class,'styles_protection_content')]//*[text()])[1]"));
    private static final Locator TTD_ORDER_DETAIL = new Locator("TTD_ORDER_DETAIL", AppiumBy.xpath("//*[contains(@class,'TTDInfo_detail_link')]"));
    private static final Locator TTD_GUEST_DETAIL_FULL_NAME = new Locator("TTD_GUEST_DETAIL_FULL_NAME", AppiumBy.xpath("(//*[contains(@class,'DetailReservation_extra_details')])[1]"));
    private static final Locator TTD_CLOSE_GUEST_DETAIL = new Locator("CLOSE_GUEST_DETAIL", AppiumBy.xpath("//button[contains(@class,'BottomSheet_close_button')]"));
    private static final Locator TTD_ORDER_DETAIL_ID = new Locator("TTD_ORDER_DETAIL_ID", AppiumBy.xpath("//*[contains(@class, 'DetailReservation_action_wrapper')]//span"));
    private static final Locator SEE_ETIKCET_WEBVIEW = new Locator("SEE_FERRY_ETIKCET", AppiumBy.xpath("//*[contains(@class,'ETicket_view_coachmark')]"));
    private static final Locator SEE_BOARDING_PASS_BTN = new Locator("SEE_BOARDING_PASS_BTN", AppiumBy.xpath("//button[contains(@class,'SingleButton_button')]//*[contains(text(),'pass')]"));

    private static final Locator PASSENGERS_AND_FACILITIES_HEADING = new Locator("PASSENGERS_AND_FACILITIES_HEADING", AppiumBy.xpath("//h2[normalize-space()='Passengers & Facilities']"));

    //train order detail
    public static final Locator HEADER = new Locator("HEADER", AppiumBy.xpath("//header"));
    private static final Locator PASSENGERS_AND_FACILITIES_SECTION = new Locator("PASSENGERS_AND_FACILITIES_SECTION", AppiumBy.xpath("//*[contains(@class,'Passenger_container')]"));
    private static final Locator PASSENGER_DETAIL = new Locator("PASSENGER_DETAIL", AppiumBy.xpath("//*[contains(@class,'styles_train_detail_passengers')]"));
    private static final Locator TRAIN_DETAILS = new Locator("TRAIN_DETAILS", AppiumBy.xpath("//*[contains(@class,'DetailReservation_flight_name_info')]"));
    private static final Locator TRAIN_DETAIL_DURATION = new Locator("TRAIN_DETAIL_DURATION", AppiumBy.xpath("//*[contains(@class,'DetailReservation_flight_duration')]"));
    private static final Locator TRAIN_DETAIL_CONTENT = new Locator("TRAIN_DETAIL_CONTENT", AppiumBy.xpath("//*[contains(@class,'DetailReservation_content_flight')]]"));
    private static final Locator RESCHEDULE_ROOMS_SELECTION = new Locator("RESCHEDULE_ROOMS_SELECTION", AppiumBy.xpath("(//*[contains(@class,'BottomSheet_wrapper')]//*[contains(@class,'Radio_checkbox')])"));
    private static final Locator CONFIRM_RESCHEDULE_HOTEL_MULTI_ORDER = new Locator("CONFIRM_RESCHEDULE_HOTEL_MULTI_ORDER", AppiumBy.xpath("//div[contains(@class,'BottomSheet_bottom_sheet')]//button[contains(@class,'primary')]"));
    private static final Locator SECOND_ROOM_POLICY_CHEVRON = new Locator("SECOND_ROOM_POLICY_CHEVRON", AppiumBy.xpath("(//*[contains(@class,'Accordion_icon_inverted')])[1]"));
    private static final Locator TRAIN_DEPARTURE_TIME = new Locator("TRAIN_DEPARTURE_TIME", AppiumBy.xpath("(//*[contains(@class,'DetailReservation_flight_subtitle')])[1]/preceding-sibling::*"));
    private static final Locator TRAIN_ORIGIN_STATION = new Locator("TRAIN_ORIGIN_STATION", AppiumBy.xpath("(//*[contains(@class,'DetailReservation_flight_subtitle')])[1]"));
    private static final Locator TRAIN_DESTINATION_STATION = new Locator("TRAIN_DESTINATION_STATION", AppiumBy.xpath("(//*[contains(@class,'DetailReservation_flight_subtitle')])[2]"));
    private static final Locator TRAIN_NAME_CLASS = new Locator("TRAIN_NAME_CLASS", AppiumBy.xpath("//*[contains(@class,'DetailReservation_flight_name_info')]/span[1]"));
    private static final Locator SELECT_ALL_PASSENGERS_CHECKBOX = new Locator("SELECT_ALL_PASSENGERS_CHECKBOX", AppiumBy.xpath("(//*[contains(@class,'Check_custom_checkbox')])[last()]"));
    private static final Locator PRINT_BOARDING_PASS = new Locator("PRINT_BOARDING_PASS", AppiumBy.xpath("//*[contains(@class,'SubmissionFooter_footer_button')]"));
    private static final Locator BARCODE_BOARDING_PASS = new Locator("BARCODE_BOARDING_PASS", AppiumBy.xpath("//*[contains(@class,'CardBoardingPass_ticket_img')]//img"));
    private static final Locator TRAIN_OR_WHOOSH_NAME = new Locator("TRAIN_OR_WHOOSH_NAME", AppiumBy.xpath("//*[contains(@class,'CardBoardingPass_title')]"));
    private static final Locator WAGON_AND_SEAT_DETAIL = new Locator("WAGON_AND_SEAT_DETAIL", AppiumBy.xpath("//*[contains(@class,'CardBoardingPass_passenger_info')]//*[contains(text(),'/')]"));
    private static final Locator ARRIVAL_TIME_BOARDING_PASS = new Locator("ARRIVAL_TIME_BOARDING_PASS", AppiumBy.xpath("//*[contains(@class,'CardBoardingPass_time')]"));

    //ferry
    private static final Locator FERRY_ORDER_GROUP_TOTAL_PAYMENT = new Locator("FERRY_ORDER_GROUP_TOTAL_PAYMENT", AppiumBy.xpath("//*[contains(@class,'TotalAmount_total_amount_and_label')]//span"));

    //car
    private static final Locator CAR_RENTAL_PRODUCT_NAME = new Locator("CAR_RENTAL_PRODUCT_NAME", AppiumBy.xpath("//*[contains(@class,'styles_description_wrapper')]//*[contains(@class,'bold')]"));
    private static final Locator CAR_RENTAL_RENTAL_TYPE = new Locator("CAR_RENTAL_RENTAL_TYPE", AppiumBy.xpath("(//*[contains(@class,'styles_icon_contents_wrapper')]//*[text()])[2]"));
    private static final Locator CONTACT_DRIVER = new Locator("CONTACT_DRIVER", AppiumBy.xpath("//*[contains(@class,'ContactPartner_action_button')]"));
    private static final Locator CAR_RENTAL_PICK_UP_LOCATION = new Locator("CAR_RENTAL_PICK_UP_LOCATION", AppiumBy.xpath("(//*[contains(@class,'styles_content_item_wrapper')]//*[contains(@class,'styles_detail_content_wrapper')])[1]//*[text()][3]"));
    private static final Locator CAR_RENTAL_DROP_OFF_LOCATION = new Locator("CONTACT_DRIVER", AppiumBy.xpath("(//*[contains(@class,'styles_content_item_wrapper')]//*[contains(@class,'styles_detail_content_wrapper')])[2]//*[text()][3]"));
    private static final Locator CAR_RENTAL_PICK_UP_DATE = new Locator("CAR_RENTAL_PICK_UP_DATE", AppiumBy.xpath("(//*[contains(@class,'styles_content_item_wrapper')]//*[contains(@class,'styles_detail_content_wrapper')])[1]//*[text()][2]"));
    private static final Locator CAR_RENTAL_DROP_OFF_DATE = new Locator("CAR_RENTAL_DROP_OFF_DATE", AppiumBy.xpath("(//*[contains(@class,'styles_content_item_wrapper')]//*[contains(@class,'styles_detail_content_wrapper')])[2]//*[text()][2]"));
    private static final Locator CAR_RENTAL_DURATION = new Locator("CAR_RENTAL_DURATION", AppiumBy.xpath("(//*[contains(@class,'styles_content_item_wrapper')]//*[contains(@class,'styles_detail_content_wrapper')])[1]//*[text()][4]"));
    private static final Locator CR_REFUND_AND_RESCHEDULE_INFO_SECTION = new Locator("CR_REFUND_AND_RESCHEDULE_INFO_SECTION", AppiumBy.xpath("//*[contains(@class,'RefundRescheduleInfo_container')]"));
    private static final Locator CR_RR_DETAIL_BTN = new Locator("CR_RR_DETAIL_BTN", AppiumBy.xpath("//*[contains(@class,'RefundRescheduleInfo_more_details')]"));
    private static final Locator RR_INFO_BTM_SHEET = new Locator("RR_INFO_BTM_SHEET", AppiumBy.xpath("//*[contains(@class,'styles_full_iframe_container')]"));
    private static final Locator AT_CLOSE_DETAIL = new Locator("AT_CLOSE_DETAIL", AppiumBy.xpath("(//*[contains(@class,'styles_header_wrapper')]//*)[2]"));
    private static final Locator REFUND_BUTTON = new Locator("REFUND_BUTTON", AppiumBy.xpath("//div[contains(@class,'ManageOrder_refund_tiket_icon__')]//span[contains(@class, 'text')]//*[contains(text(), 'Refund')] | //*[contains(@class,'ManageOrder_content_info') and .//*[contains(@class,'ManageOrder_refund_tiket_icon')]/*[contains(text(),'Refund')]]"));
    private static final Locator MANAGE_ORDER_BUTTON = new Locator("MANAGE_ORDER_BUTTON", AppiumBy.xpath("//button[contains(@class, 'FloatingButton')]"));
    private static final Locator REDEEM_BUTTON = new Locator("REDEEM_BUTTON", AppiumBy.xpath("//*[contains(text(),'Redeem') or contains(text(),'Tukar')]"));
    private static final Locator MANAGE_PASSES_BUTTON = new Locator("MANAGE_PASSES_BUTTON", AppiumBy.xpath("//span[contains(text(), 'Upcoming Visits') or contains(text(), 'Kunjungan Mendatangmu')]/ancestor::div[@data-testid='manage_pass_button']"));
    private static final Locator ETICKET_QRCODE = new Locator("ETICKET_QRCODE", AppiumBy.xpath("//*[contains(@class, 'EVoucher') and contains(@class, 'qr_code')]"));
    private static final Locator EDIT_BOOKING_BUTTON = new Locator("EDIT_BOOKING_BUTTON", AppiumBy.xpath("//*[contains(@class,'content_section_header')]//*[contains(text(),'Tamu') or contains(text(),'Guest')]//following-sibling::*[contains(@class,'action_button') and (contains(text(),'Ubah') or contains(text(),'Edit'))]"));

    //standalone section
    private static final Locator STANDALONE_SECTION = new Locator("STANDALONE_SECTION", AppiumBy.xpath("//*[contains(@class,'ProtectionSection_title')]"));
    private static final Locator STANDALONE_ADD_BTN = new Locator("STANDALONE_ADD_BTN", AppiumBy.xpath("(//*[contains(@data-testid,'protection-card:add-button')])[1]"));
    private static final Locator STANDALONE_PRICE_INSURANCE = new Locator("STANDALONE_PRICE_INSURANCE", AppiumBy.xpath("(//*[contains(@class,'ProtectionCard_action')]//*[contains(@class,'Text_variant_price')])[1]"));

    //hotel
    private static final Locator HOTEL_GUEST_NAME = new Locator("HOTEL_GUEST_NAME", AppiumBy.xpath("//*[@name='Guest Name']"));

    //cancel order (webview) — same selectors as dWeb OrderDetailWeb; Android OD is webview-backed
    private static final Locator CANCEL_ORDER_BUTTON_WEBVIEW = new Locator("CANCEL_ORDER_BUTTON_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'RefundRescheduleInfo_button') or contains(@class,'CancelFooter_cancel_button')]//span[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'cancel order') or contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'batalkan pesanan')]"));
    private static final Locator CANCEL_ORDER_AGREEMENT_CHECKBOX = new Locator("CANCEL_ORDER_AGREEMENT_CHECKBOX", AppiumBy.xpath("//input[contains(@class,'Check_checkbox') and @type='checkbox']"));
    private static final Locator CANCEL_ORDER_AGREE_BUTTON = new Locator("CANCEL_ORDER_AGREE_BUTTON", AppiumBy.xpath("//button[contains(@class,'Button_accessibility_button')]//span[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'agree') or contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'setuju')]"));
    private static final Locator SUBMIT_CANCEL_ORDER = new Locator("SUBMIT_CANCEL_ORDER", AppiumBy.xpath("//*[contains(@class,'Figure_footer')]/button[contains(@class,'Figure_secondary')]"));
    private static final Locator ORDER_STATUS_NOTIFICATION_BANNER = new Locator("ORDER_STATUS_NOTIFICATION_BANNER", AppiumBy.xpath("//*[contains(@class,'NotificationBanner_title')]"));

    public static <T extends WebDriver> OrderDetailPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID, AMWEB -> {
                return new OrderDetailPageAndroid<>(session);
            }
            case IOS, IMWEB -> {
                return new OrderDetailPageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public String getTotalPaymentPah(String orderId) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderId);
        driverTools.scrollIntoWebView(TOTAL_PAYMENT_PAH);
        String totalPayment = driverTools.getText(TOTAL_PAYMENT_PAH).orElseThrow();
        driverTools.switchToNative();
        return totalPayment;
    }

    /**
     * Refund status text (e.g. "In process"/"Diproses") shown on the order detail page while a
     * failed-issued booking is being auto-refunded.
     * Android's app webview is a real, switchable Chrome DOM, so this just switches into it and
     * reuses {@link OrderListWeb#getRefundStatus()}'s already-verified web locator instead of
     * duplicating it; iOS overrides this with its own @label-based implementation since its
     * embedded webview doesn't expose a real DOM the same way (see {@code REFUND_HOTEL_STATUS}).
     */
    public String getRefundInProcessStatus(String orderId) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderId);
        String status = OrderListWeb.getInstance(session).getRefundStatus();
        driverTools.switchToNative();
        return status;
    }

    /**
     * Refund amount shown on the order detail page while a failed-issued booking is being
     * auto-refunded. Reuses {@link OrderListWeb#getTotalAmount()} the same way as
     * {@link #getRefundInProcessStatus(String)} above (Android only).
     */
    public String getRefundInProcessAmount(String orderId) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderId);
        String amount = OrderListWeb.getInstance(session).getTotalAmount();
        driverTools.switchToNative();
        return amount;
    }

    public void goToManagePasses(String orderId) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl(orderId);
        driverTools.scrollIntoWebView(MANAGE_PASSES_BUTTON);
        driverTools.clickWithJavascriptExecutor(MANAGE_PASSES_BUTTON);
        driverTools.switchToNative();
    }

    public void goToManagePasses() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder/detail");
        driverTools.scrollIntoWebView(MANAGE_PASSES_BUTTON);
        driverTools.clickWithJavascriptExecutor(MANAGE_PASSES_BUTTON);
        driverTools.switchToNative();
    }

    public void clickRedeemButton() {
        driverTools.switchToWebViewUrl("yourorder/detail");
        driverTools.scrollIntoWebView(REDEEM_BUTTON);
        driverTools.clickWithJavascriptExecutor(REDEEM_BUTTON);
        driverTools.switchToNative();
    }

    public void selectPassengerCheckBoxes() {
        driverTools.switchToWebView();
        driverTools.waitForElementToAppear(SELECT_ALL_PASSENGERS_CHECKBOX);
        driverTools.click(SELECT_ALL_PASSENGERS_CHECKBOX);
        driverTools.click(PRINT_BOARDING_PASS);
        driverTools.switchToNative();
    }

    public boolean verifyBoardingPassDetails(TrainData dataFromBookingPage) {
        driverTools.switchToWebViewUrl("kereta-api/online-check-in/boarding-pass");
        driverTools.waitForElementToPresence(BARCODE_BOARDING_PASS);
        int numberOfGuests = dataFromBookingPage.guestList.size();
        logger.log("Number of guests: " + numberOfGuests);

        //name and id
        for (int i = 0; i < dataFromBookingPage.guestList.size(); i++) {
            logger.log("Verifying boarding pass details for guest " + (i + 1) + " of " + numberOfGuests);
            String expectedPassengerName = dataFromBookingPage.guestList.get(i);
            String expectedId = dataFromBookingPage.idNumberList.get(i);

            // Clean guest name by removing salutations and unwanted characters
            String cleanedGuestName = expectedPassengerName.replaceAll("(?i)\\b(Mr|Mrs|Ms|Tuan|Nyonya|Nona)\\.?\\s*", "").trim();
            cleanedGuestName = cleanedGuestName.replaceAll("\\s+", " ");

            logger.log("Original Passenger Name: " + expectedPassengerName);
            logger.log("Cleaned Passenger Name: " + cleanedGuestName);
            // Boarding passes sit in a horizontal carousel, so cards after the first start off-screen.
            WebElement passengerNameElement = driverTools.waitForElementToAppearScrollingHorizontally(TrainPassengerName(cleanedGuestName), Swipe.RIGHT, 5);
            String actualPassengerNameText = driverTools.getText(passengerNameElement).trim();
            logger.log("Actual Passenger Name on Boarding Pass: " + actualPassengerNameText + " | Expected Passenger Name: " + cleanedGuestName);
            Assertion.assertThat("Verify Passenger Name on Boarding Pass matches expected", actualPassengerNameText.toLowerCase(), containsString(cleanedGuestName.toLowerCase()));

            WebElement idElement = driverTools.waitForElementToAppearScrollingHorizontally(TrainPassengerName(expectedId), Swipe.RIGHT, 5);
            String actualIdText = driverTools.getText(idElement).trim();
            logger.log("Actual ID on Boarding Pass: " + actualIdText + " | Expected ID: " + expectedId);
            Assertion.assertThat("Verify ID on Boarding Pass matches expected", actualIdText.toLowerCase(), containsString(expectedId.toLowerCase()));
        }

        // Remove everything after '•' from the train name
        String dynamicTrainName = dataFromBookingPage.trainName;
        String expectedTrainName = dynamicTrainName.contains("•")
                ? dynamicTrainName.substring(0, dynamicTrainName.indexOf("•")).trim()
                : dynamicTrainName;
        logger.log("Expected Train Name: " + expectedTrainName);

        //Train or Whoosh name
        WebElement trainNameElement = driverTools.waitForElementToAppearScrollingHorizontally(TRAIN_OR_WHOOSH_NAME, Swipe.LEFT, 5);
        String actualTrainNameText = driverTools.getText(trainNameElement).trim();
        logger.log("Actual Train Name on Boarding Pass: " + actualTrainNameText + " | Expected Train Name: " + expectedTrainName);
        Assertion.assertThat("Verify Train Name on Boarding Pass matches expected", actualTrainNameText.toLowerCase(), containsString(expectedTrainName.toLowerCase()));

        //Arrival time
        WebElement arrivalTimeElement = driverTools.waitForElementToPresence(ARRIVAL_TIME_BOARDING_PASS, 5).orElseThrow();
        String actualArrivalTimeText = driverTools.getText(arrivalTimeElement).trim();
        logger.log("Actual Arrival Time on Boarding Pass: " + actualArrivalTimeText + " | Expected Arrival Time: " + dataFromBookingPage.arrivalTime);
        Assertion.assertThat("Verify Arrival Time on Boarding Pass matches expected", actualArrivalTimeText.toLowerCase(), containsString(dataFromBookingPage.arrivalTime.toLowerCase()));

        //coach and seat number
        WebElement wagonAndSeatElement = driverTools.waitForElementToPresence(WAGON_AND_SEAT_DETAIL, 5).orElseThrow();
        String actualWagonAndSeatText = driverTools.getText(wagonAndSeatElement).trim();
        logger.log("Actual Wagon and Seat on Boarding Pass: " + actualWagonAndSeatText);
        String[] expectedNormalizedSeats = extractAndNormalizeSeat(dataFromBookingPage.seat);
        logger.log("Expected Wagon and Seat Options: " + Arrays.toString(expectedNormalizedSeats) + " actual is " + actualWagonAndSeatText);
        Assertion.assertThat("Verify Wagon and Seat on Boarding Pass matches expected", actualWagonAndSeatText, anyOf(containsString(expectedNormalizedSeats[0]), containsString(expectedNormalizedSeats[1])));

        //boarding pass
        List<WebElement> barcodes = driverTools.waitForPresenceElementsList(BARCODE_BOARDING_PASS).orElseThrow();
        Assertion.assertThat("Verify Barcodes are present on each boarding pass", barcodes.size(), is(dataFromBookingPage.adult));
        driverTools.switchToNative();

        return true;
    }

    /**
     * Extracts and normalizes the seat information from the input string.
     * The input can be in formats like "Kereta A - 12" or "Kereta B / 15".
     * The output is an array containing two normalized strings:
     * [0] Indonesian format: "Kereta {coach} / Kursi {seatNumber}"
     * [1] English format: "Coach {coach} / Seat {seatNumber}"
     *
     * @param input the input seat string
     * @return an array of normalized seat strings in Indonesian and English
     */
    public String[] extractAndNormalizeSeat(String input) {
        String[] result = new String[2];

        // Split by "-" or "/"
        String[] parts;
        if (input.contains("-")) {
            parts = input.split("-");
        } else if (input.contains("/")) {
            parts = input.split("/");
        } else {
            // fallback: no separator found
            result[0] = input.trim();
            result[1] = input.trim();
            return result;
        }

        // Trim spaces
        String left = parts[0].trim();
        String seatNumber = parts[1].trim();

        // Extract coach number (last token of left part)
        String[] leftTokens = left.split(" ");
        String coachNumber = leftTokens[leftTokens.length - 1];

        result[0] = "Kereta " + coachNumber + " / Kursi " + seatNumber;  // Indonesian
        result[1] = "Coach " + coachNumber + " / Seat " + seatNumber; // English
        logger.log("Normalized Seat Options: " + Arrays.toString(result));
        return result;
    }


    private static Locator TrainPassengerName(String name) {
        String nameLower = name == null ? "" : name.toLowerCase();
        return new Locator("PASSENGER_NAME_OR_ID" + name, AppiumBy.xpath("//*[contains(@class,'CardBoardingPass_passenger_info')]//div[contains(@class,'Text_text') and contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + nameLower + "')]"));
    }

    public String getRoomName(String orderId) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderId);
        String roomName = driverTools.getText(ROOM_NAME_INFO).orElseThrow();
        roomName = roomName.split(" x ")[1];
        logger.log("Room Name Booking Detail: " + roomName);
        driverTools.switchToNative();
        return roomName;
    }

    public abstract String getRefundInfo(int index);

    public abstract String getRoomName(int index);

    public abstract int getRoomAmount();

    public abstract int getRommGuestAmount();

    public abstract void seeRoomDetails(int index);

    public abstract void openTabRefundRescheduleDetailRoom();

    public abstract void openRefundRequestDetailRoom();

    public abstract String getRoomStatus(String roomName);

    public abstract void seeMoreDetails();

    public abstract boolean refundBannerInfo();

    public abstract boolean refundNotAvailable();

    @Deprecated
    public abstract void clickReschedule();

    public abstract void clickReschedule(String orderId);

    public abstract boolean isInfoPopupDisplayed();

    public abstract void closeInfoPopup();

    public abstract void goToPreviousOrderBanner();

    public abstract void clickPreviousOrder();

    public abstract void clickPreviousOrder(String orderId);

    public boolean isPreviousOrderBannerDisplayed(String orderId) {
        driverTools.waitForElementToAppear(PaymentPageAndroid.LOADING_STATE);
        driverTools.switchToWebViewUrl(orderId);
        boolean result = driverTools.waitForElementToAppear(PREVIOUS_ORDER_BANNER_WEBVIEW).isPresent();
        driverTools.switchToNative();
        return result;
    }

    public boolean isNewScheduleButtonDisplayed(String orderId) {
        driverTools.waitForElementToAppear(PaymentPageAndroid.LOADING_STATE);
        driverTools.switchToWebViewUrl(orderId);
        boolean result = driverTools.waitForElementToAppear(NEW_SCHEDULE_WEBVIEW).isPresent();
        driverTools.switchToNative();
        if (!result && Configs.PLATFORM == Platform.ANDROID)
            result = driverTools.isElementDisplayed(NEW_SCHEDULE_NATIVE);
        return result;
    }

    public abstract void clickNewScheduleButton(String orderId);

    public boolean isRescheduleCardDisplayed(String orderId) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderId);
        boolean result = driverTools.waitForElementToAppear(RESCHEDULE_CARD_WEBVIEW).isPresent();
        driverTools.switchToNative();
        return result;
    }

    public abstract boolean isHotelNameDisplayed(String hotelName);

    public abstract boolean isHotelNameDisplayed(String hotelName, String orderId);

    public String getHotelName(String orderId) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderId);
        WebElement hotelName = driverTools.waitForElementToPresence(HOTEL_NAME).orElseThrow();
        String hotelNameText = driverTools.getText(hotelName);
        driverTools.switchToNative();
        return hotelNameText;
    }

    /**
     * Cancels the order identified by {@code orderId}. The flow (all in webview):
     * 1. clicks the "Cancel order" / "Batalkan pesanan" button on the refund/reschedule info section;
     * 2. selects every agreement checkbox presented on the consent screen;
     * 3. clicks the "Agree" / "Setuju" button to confirm the cancellation.
     * After the cancellation completes the driver context is switched back to native.
     */
    public void cancelOrder(String orderId) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderId);

        driverTools.scrollIntoWebView(CANCEL_ORDER_BUTTON_WEBVIEW);
        driverTools.click(CANCEL_ORDER_BUTTON_WEBVIEW);
        logger.log("Clicked cancel order button");

        List<WebElement> checkboxes = driverTools.waitForPresenceElementsList(CANCEL_ORDER_AGREEMENT_CHECKBOX).orElseThrow();
        logger.log("Found " + checkboxes.size() + " agreement checkbox(es)");
        for (int i = 0; i < checkboxes.size() ; i++) {
            driverTools.clickWithJavascriptExecutor(checkboxes.get(i));
            logger.log("Checked agreement checkbox " + (i + 1));
        }

        driverTools.scrollIntoWebView(CANCEL_ORDER_AGREE_BUTTON);
        driverTools.click(CANCEL_ORDER_AGREE_BUTTON);
        logger.log("Clicked agree button to confirm cancellation");

        driverTools.switchToNative();
    }

    public boolean submitCancelOrder(String orderId) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderId);

        driverTools.scrollIntoWebView(CANCEL_ORDER_BUTTON_WEBVIEW);
        driverTools.click(CANCEL_ORDER_BUTTON_WEBVIEW);
        logger.log("Clicked cancel order button");

        driverTools.scrollIntoWebView(SUBMIT_CANCEL_ORDER);
        driverTools.click(SUBMIT_CANCEL_ORDER);
        logger.log("Clicked submit button to confirm cancellation");
        
        boolean isSnackbarDisplayed = driverTools.waitForElementToPresence(SNACKBAR_TEXT).isPresent();
        driverTools.switchToNative();
        return isSnackbarDisplayed;
    }

    /**
     * Cancels the order from Manage Order via webview
     * ({@link OrderDetailWeb#clickCancelOrderButton()} — mWeb path for app).
     * iOS overrides with native Cancel CTA then webview confirmation.
     */
    public void clickCancelOrderButton(String orderId) {
        driverTools.switchToWebViewUrl("order-cancellation?orderId=" + orderId);
        OrderDetailWeb.getInstance(session).clickCancelOrderButton();
        driverTools.switchToNative();
    }

    /**
     * Reads the current order status banner text (e.g. "You have canceled this order" /
     * "Kamu membatalkan pesanan ini") from the order detail webview.
     * Switches the driver back to native before returning.
     */
    public String getOrderStatus(String orderId, int retry) throws InterruptedException {
        String status = "";
        for (int i = 0; i < retry; i++) {
            driverTools.swipeDown(2);
            driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderId);
            try {
                WebElement banner = driverTools.waitForElementToPresence(ORDER_STATUS_NOTIFICATION_BANNER).orElseThrow();
                status = driverTools.getText(banner);
                logger.log("Order status banner text: " + status);
            } catch (NoSuchElementException e) {
                logger.log("Order status banner not found, attempt " + (i + 1) + " of " + retry);
            }
            driverTools.switchToNative();
            if (status != null && !status.isEmpty()) {
                break;
            }
            Thread.sleep(3000);
        }

        driverTools.switchToNative();
        if (status == null || status.isEmpty()) {
            logger.log("Order status banner not found after " + retry + " attempts");
            logger.log("try to reopen the order detail page to refresh the content");
            for (int i = 0; i < retry; i++) {
                this.clickBack();
                OrderListPage<T> orderListPage = OrderListPage.getInstance(session);
                orderListPage.openOrderDetailViaDeeplink(orderId);
                driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderId);
                try {
                    WebElement banner = driverTools.waitForElementToPresence(ORDER_STATUS_NOTIFICATION_BANNER).orElseThrow();
                    status = driverTools.getText(banner);
                    logger.log("Order status banner text: " + status);
                } catch (NoSuchElementException e) {
                    logger.log("Order status banner still not found, attempt " + (i + 1) + " of " + retry);
                }
                driverTools.switchToNative();
                if (status != null && !status.isEmpty()) {
                    break;
                }
                Thread.sleep(3000);
            }
        }
        return status;
    }

    /**
     * Fast presence check for Manage Order / Refund-reschedule "Cancel order"
     * ({@code Batalkan pesanan}). Caller must already be in the OD webview
     * (e.g. via {@code webViewPage.switchToWebViewUrl("yourorder/detail")}).
     * iOS overrides with native.
     */
    public boolean isCancelOrderButtonVisible() {
        driverTools.waitForElementToAppear(OrderDetailWeb.HIDE_BANNER_BUTTON, 5).ifPresent(driverTools::click);
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=");
        return driverTools.waitForElementToAppear(CANCEL_ORDER_BUTTON_WEBVIEW).isPresent();
    }

    /**
     * Fast presence check for post-cancellation "Delete order" ({@code Hapus pesanan}).
     * Caller must already be in the OD webview. iOS overrides with native.
     * Distinct from {@link #isDeleteOrderButtonDisplayed(String)}, which targets the
     * RefundRescheduleInfo button family (and may match Cancel before cancellation).
     */
    public boolean isDeleteOrderButtonVisible() {
        return driverTools.waitForElementToAppear(OrderDetailWeb.DETAIL_RESERVATION_DELETE_ORDER_BUTTON, 10).isPresent();
    }

    /**
     * Fast presence check for post-cancellation "Make similar order"
     * ({@code Buat pesanan serupa}). Caller must already be in the OD webview.
     * iOS overrides with native.
     */
    public boolean isMakeSimilarOrderButtonVisible() {
        return driverTools.waitForElementToAppear(OrderDetailWeb.MAKE_SIMILAR_ORDER_BUTTON, 10).isPresent();
    }

    public abstract boolean isRoomNameDisplayed(String roomName);

    public abstract String getOrderID();

    /**
     * Returns the per-itinerary {@code orderDetailId} as displayed on the order detail
     * page. Implemented per-platform: legacy platforms only surfaced the parent
     * {@code orderId} via {@link #getOrderID()}, so historically callers used that. New
     * UIs (e.g. iOS new UI's {@code itinerary_id_number} static text) expose the
     * itinerary-level id directly and platform subclasses return it here. Subclasses
     * with no such element should throw a clear {@link UnsupportedOperationException}.
     */
    public abstract String getOrderDetailID();

    public abstract String getCheckInDate();

    public String getStartDate(String orderId) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderId);
        driverTools.swipeToElement(CHECKIN_DATE_WEBVIEW, Swipe.UP, 3);
        String checkinDateText = driverTools.getText(CHECKIN_DATE_WEBVIEW).orElseThrow().split(" ")[1].replaceAll("[^0-9]", "");
        logger.log("checkinDateText -> " + checkinDateText);
        driverTools.switchToNative();
        return checkinDateText;
    }

    public abstract String getEndDate();

    public abstract String getCheckOutDate();

    public abstract String getTotalAdult();

    public String getTotalNight(String orderId) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderId);
        String totalNightText = driverTools.getText(TOTAL_NIGHT).orElseThrow();
        driverTools.switchToNative();
        return totalNightText;
    }

    public abstract String getTotalRoom();

    public abstract String getGuestFullName();

    public abstract void openTTDGuestDetail(int order);

    public abstract String getETicketLabel();

    public abstract void clickSeeEticketFromManageOrder();

    public abstract void clickETicketLanguage(String language);

    public abstract boolean isETicketLanguageOptionVisible(String language);

    public abstract void chooseDefaultAppPdf();

    public abstract void clickEticketFromHeader();

    public abstract void clickEticketFromHeader(String orderId);

    public abstract String getPDFContentFile();

    public abstract boolean isPdfContentOpen();

    public abstract boolean isOrderRescheduledBannerExist();

    public abstract boolean isMainETiketCardDisplayed();

    public abstract boolean isETiketAndReceiptCardDisplayed();

    public abstract String getFullOrderId();

    public abstract boolean checkAirportTransferDetail(String pickupPlace, String dropPlace);

    public abstract String getAirportTransferTotalPrice();

    public abstract boolean isTrainDetailCorrect(String originStation, String destinationStation, String totalPrice);

    public abstract String getCarRentalName();

    public abstract String getCarRentalLocation();

    public abstract String getCarRentalDriverOption();

    public abstract String getCarRentalPickupTime();

    public abstract String getCarRentalPassengerFullName();

    public abstract String getCarRentalPassengerPhoneNumber();

    public abstract boolean isInOrderDetailPage();

    public abstract boolean verifyCrossSellRedirection(String orderDetailSectionTitle, List<String> expectedUrl);

    public abstract String getTotalPrice();

    public abstract boolean verifyShareEticketBtmSheet(String vertical, String lang, String newParam);

    public abstract void clickShareEticketNative();

    public abstract void clickRoomToRefundByIndex(int index, String orderId);

    public void clickShareEticket() {
        driverTools.switchToWebViewUrl("detail");
        driverTools.scrollIntoWebView(SHARE_ETICKET_WEBVIEW);
        driverTools.waitForElementToPresence(SHARE_ETICKET_WEBVIEW).ifPresent(driverTools::click);
        driverTools.switchToNative();
    }

    public abstract boolean verifyAtContactDetail(String salutation, String name, String phoneNumber, String lang);

    public abstract boolean verifyAtCarDetailSection(String carType);

    public abstract boolean verifyOrderDetailHeader(String expectedTitle);

    public abstract boolean veriyAtSummaryDetail(String origin, String destination);

    public abstract boolean verifyAtFacilitiesDetailPresent();

    public abstract void refreshOrderDetailPage(int numberOfSwipe);

    public abstract boolean verifyCarRentalProductDetail(String fleetName, String carRentalType, String appLang);

    public boolean verifyCarRentalProductDetailWebview(String fleetName, String carRentalType, String appLang) {
        driverTools.switchToWebViewUrl("detail");
        String carRentalTypeTranslated = "";
        switch (carRentalType.toLowerCase()) {
            case "with_driver":
                carRentalTypeTranslated = appLang.equals("en") ? "with driver" : "dengan sopir";
                break;
            default:
                carRentalTypeTranslated = appLang.equals("en") ? "without driver" : "tanpa sopir";
                break;
        }

        String actualProductName = driverTools.getText(CAR_RENTAL_PRODUCT_NAME).orElseThrow().trim();
        Assertion.assertThat("Verify product name is the same as previously booked", actualProductName, equalTo(fleetName));

        String actualCarRentalType = driverTools.getText(CAR_RENTAL_RENTAL_TYPE).orElseThrow();
        Assertion.assertThat("Verify car rental type", actualCarRentalType, equalToIgnoringCase(carRentalTypeTranslated));

        driverTools.switchToNative();
        return true;
    }

    public abstract boolean verifyDriverDetailExist();

    public boolean verifyDriverDetailExistWebview() {
        driverTools.switchToWebViewUrl("detail");
        String driverContactPhone = driverTools.getText(CONTACT_DRIVER).orElseThrow();
        Assertion.assertThat("Verify contact phone present", driverContactPhone, not(blankOrNullString()));
        driverTools.switchToNative();
        return true;
    }

    public abstract boolean verifyRentalDetail(String pickUpLocation, String dropOffLocation, String pickUpTime, String dropOffTime, String rentalDuration);

    public boolean verifyRentalDetailWebview(String pickUpLocation, String dropOffLocation, String pickUpTime, String dropOffTime, String carRentalDuration) {
        driverTools.switchToWebViewUrl("detail");
        String actualPickUpLocation = driverTools.getText(CAR_RENTAL_PICK_UP_LOCATION).orElseThrow();
        Assertion.assertThat("Verify pick-up location", actualPickUpLocation, containsString(pickUpLocation));
        String actualDropOffLocation = driverTools.getText(CAR_RENTAL_DROP_OFF_LOCATION).orElseThrow();
        Assertion.assertThat("Verify drop-off location", actualDropOffLocation, containsString(dropOffLocation));
        String actualPickUpDate = driverTools.getText(CAR_RENTAL_PICK_UP_DATE).orElseThrow();
        String actualDropOffDate = driverTools.getText(CAR_RENTAL_DROP_OFF_DATE).orElseThrow();
        Assertion.assertThat("Verify pick up date", actualPickUpDate, containsString(pickUpTime));
        Assertion.assertThat("Verify drop off date", actualDropOffDate, containsString(dropOffTime));
        String actualCarRentalDuration = driverTools.getText(CAR_RENTAL_DURATION).orElseThrow();
        Assertion.assertThat("Verify car rental duration", actualCarRentalDuration, containsString(carRentalDuration));
        driverTools.switchToNative();
        return true;
    }

    public abstract boolean verifyCarRrInfoSection();

    public boolean verifyCarRrInfoSectionWebview() {
        driverTools.switchToWebViewUrl("detail");
        boolean isRrInfoPresent = driverTools.swipeToElement(CR_REFUND_AND_RESCHEDULE_INFO_SECTION, Swipe.UP, 3).isPresent();
        Assertion.assertThat("Verify rental refund and reschedule present", isRrInfoPresent, is(true));
        driverTools.click(CR_RR_DETAIL_BTN);
        boolean isRrBtmSheetPresent = driverTools.isElementDisplayed(RR_INFO_BTM_SHEET);
        Assertion.assertThat("Verify rental refund and reschedule btm sheet appear", isRrBtmSheetPresent, is(true));
        driverTools.click(AT_CLOSE_DETAIL);
        driverTools.switchToNative();
        return true;
    }

    public abstract void carRentalScrollToTop();

    public abstract void carRentalClickScrollDownBtn();

    public boolean clickSeeBoardingPass() {
        driverTools.switchToWebViewUrl("yourorder/detail");
        boolean isSeeBoardingPassBtnPresent = driverTools.swipeToElement(SEE_BOARDING_PASS_BTN, Swipe.UP, 3).isPresent();
        if (isSeeBoardingPassBtnPresent) {
            driverTools.scrollIntoWebView(SEE_BOARDING_PASS_BTN);
            driverTools.clickWithJavascriptExecutor(SEE_BOARDING_PASS_BTN);
        }
        driverTools.switchToNative();
        return isSeeBoardingPassBtnPresent;
    }

    public boolean verifyRefundRescheduleSectionPresent(){

        return true;
    }

    public abstract boolean openPriceDetailNative();

    public abstract boolean verifyCrPriceDetailComponentCorrect();

    public abstract boolean verifyHotelPriceDetail(String currency);

    public abstract boolean verifyHotelCashbackPriceDetail(String currency);

    public abstract boolean verifyCarManageOrder();

    public boolean verifyCarManageOrderWebview() {
        driverTools.switchToWebViewUrl("detail");
        Assertion.assertThat("Verify eticket button present", driverTools.isElementDisplayed(ETICKET_ON_MANAGE_ORDER_SECTION), is(true));
        Assertion.assertThat("Verify receipt button present", driverTools.isElementDisplayed(RECEIPT_BUTTON), is(true));
        Assertion.assertThat("Verify refund button present", driverTools.isElementDisplayed(REFUND_BUTTON), is(true));
        driverTools.switchToNative();
        return true;
    }

    public abstract List<String> getFlightsPassengerList();

    public abstract String getRefundHeader();

    public String getRefundHeaderWebview() {
        driverTools.switchToWebViewUrl("yourorder");
        String refundHeader = driverTools.getText(REFUND_INFO).orElseThrow();
        driverTools.switchToNative();
        return refundHeader;
    }

    public boolean verifyCompletePaymentExpiryTime() {
        driverTools.switchToWebViewUrl("yourorder");
        List<String> expiryTimeComponents = driverTools.getTexts(COUNTDOWN);
        logger.log("Total elements found : " + expiryTimeComponents.size());
        Assertion.assertThat("Verify hour is in present", expiryTimeComponents.get(0), is(notNullValue()));
        Assertion.assertThat("Verify minutes is in present", expiryTimeComponents.get(1), is(notNullValue()));
        Assertion.assertThat("Verify seconds is in present", expiryTimeComponents.get(2), is(notNullValue()));
        driverTools.switchToNative();
        return true;
    }

    public abstract boolean verifyOrderDetailIdComponent();

    public void clickFlightDepartOrderDetail() {
        //entry from order group page
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.click(FLIGHT_ORDER_GROUP_DEPART_CHEVRON);
        driverTools.switchToNative();
    }

    public void clickFlightReturnOrderDetail() {
        //entry from order group page
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.click(FLIGHT_ORDER_GROUP_RETURN_CHEVRON);
        driverTools.switchToNative();
    }

    public String getTotalPaymentInFlightPreviewPage() {
        driverTools.switchToWebViewUrl("yourorder");
        String orderText = driverTools.getText(FLIGHT_ORDER_GROUP_TOTAL_PAYMENT).orElseThrow();
        driverTools.switchToNative();
        return orderText;
    }

    public String getTotalAmountFerry() {
        driverTools.switchToWebViewUrl("yourorder");
        String orderText = driverTools.getText(FLIGHT_ORDER_GROUP_TOTAL_PAYMENT).orElseThrow();
        driverTools.switchToNative();
        return orderText;
    }

    public String getTrainSeatData() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder/detail");
        driverTools.waitForElementToAppear(DETAILS_BTN_TRAIN_PASSENGER_DETAILS_WEBVIEW, 5);
        driverTools.click(DETAILS_BTN_TRAIN_PASSENGER_DETAILS_WEBVIEW);
        String seat = driverTools.getText(driverTools.waitForElementToAppear(SEAT_TRAIN_PASSENGER_DETAIL_WEBVIEW).orElseThrow());
        logger.log("Seat name: " + seat);
        driverTools.click(CLOSE_TRAIN_ORDER_DETAIL_BTM_SHEET);
        driverTools.switchToNative();
        return seat;
    }

    ;

    public String getOrderGroupDepartureAirline() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("order");
        String departureAirline = driverTools.getText(FLIGHT_ORDER_GROUP_DEPART_AIRLINE).orElseThrow();
        driverTools.switchToNative();
        return departureAirline;
    }

    public String getOrderGroupReturnAirline() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("order");
        String returnAirline = driverTools.getText(FLIGHT_ORDER_GROUP_RETURN_AIRLINE).orElseThrow();
        return returnAirline;
    }

    public String getDepartureDate() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("order");
        List<String> departureDateComponents = driverTools.getTexts(DEPARTURE_DATE);
        logger.log("Flight departure date: " + departureDateComponents.get(0));
        logger.log("Flight departure date time: " + departureDateComponents.get(1));
        String depatureDate = String.join(" ", departureDateComponents).trim();
        driverTools.switchToNative();
        return depatureDate;
    }

    public String getDepartureDateOrderDetail() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("detail");
        String departureDate = driverTools.getText(FLIGHT_DEPARTURE_DATE_DETAIL).orElseThrow();
        logger.log("Flight departure date: " + departureDate);
        driverTools.switchToNative();
        return departureDate;
    }

    public String getArrivalDateOrderDetail() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("detail");
        String departureDate = driverTools.getText(FLIGHT_ARRIVAL_DATE_DETAIL).orElseThrow();
        logger.log("Flight arrival date: " + departureDate);
        driverTools.switchToNative();
        return departureDate;
    }

    public String getReturnDate() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("order");
        List<String> returnDateComponents = driverTools.getTexts(RETURN_DATE);
        logger.log("Flight departure date: " + returnDateComponents.get(0));
        logger.log("Flight departure date time: " + returnDateComponents.get(1));
        String returnDate = String.join(" ", returnDateComponents).trim();
        driverTools.switchToNative();
        return returnDate;
    }

    public void completePaymentFromOrderGroupPaymentPage() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.click(COMPLETE_PAYMENT_FROM_ORDER_GROUP);
        driverTools.switchToNative();
    }

    public void openFlightGroupPriceDetail() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.click(FLIGHT_ORDER_GROUP_PAYMENT_DETAIL_CHEVRON);
        driverTools.switchToNative();
    }

    public boolean verifyFlightPriceDetailBreakDown(String expectedTotalPrice) {
        driverTools.switchToWebView();
        driverTools.switchToWebViewUrl("yourorder");
        List<String> priceComponentsInPriceDetail = driverTools.getTexts(PRICE_DETAIL_COMPONENTS);
        List<String> priceComponentsOtherFees = driverTools.getTexts(PRICE_DETAIL_COMPONENTS_DISCOUNT_AND_OTHER_FEES);
        List<String> allPriceComponents = new ArrayList<>();
        allPriceComponents.addAll(priceComponentsInPriceDetail);
        allPriceComponents.addAll(priceComponentsOtherFees);
        logger.log("number of price component present : " + priceComponentsInPriceDetail.size());
        String totalPrice = driverTools.getText(PRICE_DETAIL_TOTAL_PRICE_VALUE).orElseThrow();
        // assertion price components should be in IDR
        Assertion.assertThat("Verify price components are in idr", priceComponentsInPriceDetail,
                Every.everyItem(anyOf(containsString("IDR"), equalTo("Termasuk"), equalTo("Included"))));
        Assertion.assertThat("Verify total price is in IDR", totalPrice, containsString("IDR"));

        // assertion for total price same as manual sum calculation
        List<Double> priceComponentsNumbersValue = allPriceComponents.stream()
                .filter(n -> n.matches(".*[0-9]+.*"))
                .map(n -> CoreFunctions.convertPriceSafely(n)).toList();
                // remove currency word from the value. Eg ->
                // IDR 5000 into 5000 (with data type double)

        // sum price components
        double sumOfPriceDetail = CoreFunctions.sumUpDouble(priceComponentsNumbersValue);
        // compare sumOfPriceDetail with actual value in ui
        Assertion.assertThat("Verify total price is the as sum of price breakdown", CoreFunctions.convertPriceSafely(totalPrice),
                equalTo(sumOfPriceDetail));
        Assertion.assertThat("Verify total price is the same as price in payment page", CoreFunctions.convertPriceSafely(totalPrice), equalTo(CoreFunctions.convertPriceSafely(expectedTotalPrice)));
        driverTools.click(CLOSE_PRICE_DETAIL_WEBVIEW);
        driverTools.switchToNative();
        return true;
    }

    public String getOriginAirportOrderDetail() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("detail");
        String originAirportDetail = driverTools.getText(DEPART_AIRPORT_NAME_ORDER_DETAIL).orElseThrow();
        driverTools.switchToNative();
        return originAirportDetail;
    }

    public String getDestinationAirportOrderDetail() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("detail");
        String destinationAirportDetail = driverTools.getText(RETURN_AIRPORT_NAME_ORDER_DETAIL).orElseThrow();
        driverTools.switchToNative();
        return destinationAirportDetail;
    }

    public String getReturnAirportOrderGroup(){
        driverTools.switchToWebView();
        String destinationAirportDetail = driverTools.getText(RETURN_AIRPORT_NAME_ORDER_GROUP).orElseThrow();
        driverTools.switchToNative();
        return destinationAirportDetail;
    }

    public String geDepartAirportOrderGroup() {
        driverTools.switchToWebView();
        String destinationAirportDetail = driverTools.getText(DEPART_AIRPORT_NAME_ORDER_GROUP).orElseThrow();
        driverTools.switchToNative();
        return destinationAirportDetail;
    }

    public abstract boolean isRescheduleDetailCorrect(String refundDetailDate);// FIXME: Implementation pending IOS

    public abstract String getRefundDetailDate();// FIXME: Implementation pending IOS

    public abstract void openRefundAndRescheduleInfo();

    public abstract String getOrderRefundPolicyValue(String totalPrice);

    public String getOrderRefundPolicyValueWebView(String totalPrice) {
        driverTools.switchToWebViewUrl("refund-reschedule-info");
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

    public abstract void closeRefundAndRescheduleInfo();

    public void closeRefundAndRescheduleInfoWebView() {
        driverTools.switchToWebViewUrl("yourorder");
        if (driverTools.waitForElementToAppear(BACK_BUTTON_REFUND_RESCHEDULE_INFO, 5).isPresent()) {
            driverTools.click(BACK_BUTTON_REFUND_RESCHEDULE_INFO);
        } else {
            driverTools.switchToNative();
            closeRefundAndRescheduleInfo();
        }
        driverTools.switchToNative();
    }

    public abstract void clickRefundButton();

    public abstract void clickRefundButtonWithoutSkip(String orderId);

    public abstract void clickAndOpenRescheduleNudgeButton();

    public abstract boolean findOnPDF(String textToFind);

    public abstract void goToRefundDetail(String orderId);

    public abstract boolean isRefundDetailButtonDisplayed();

    public abstract String getTimeSlot();

    public abstract void clickChatBtn();

    public abstract boolean clickCallBtn();

    public abstract boolean isLoginChallengeShown();

    public abstract void choosNewTrainSeatWagoon();

    protected void waitForTrainSeatPageToLoad() {
        //train order detail -> change seat page
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("/yourorder/detail");
        boolean notLoaded = driverTools.getText(TRAIN_PRICE_IN_CHANGE_SEAT_MENU).orElseThrow().equals("IDR 0");
        int counter = 0;
        while (notLoaded && counter < 5) {
            notLoaded = driverTools.getText(TRAIN_PRICE_IN_CHANGE_SEAT_MENU).orElseThrow().equals("IDR 0");
            counter = counter + 1;
        }
        driverTools.switchToNative();
    }

    protected void openTrainChangeSeatSelection() {
        //train order detail -> change seat page -> open seat option
        driverTools.switchToWebViewUrl("order/train_change_seat");
        driverTools.click(OPEN_CHANGE_SEAT_SELECTION);
        driverTools.switchToNative();
    }

    protected void selectRandomTrainSeat() {
        //train order detail -> change seat page -> open seat option -> select random seat
        driverTools.switchToWebView();
        driverTools.waitForElementToAppear(TRAIN_EMPTY_SEATS_IN_CHANGE_SEAT_SELECTION, 7).orElseThrow();
        List<WebElement> trainsSeatEleements = driverTools.elements(TRAIN_EMPTY_SEATS_IN_CHANGE_SEAT_SELECTION).orElseThrow();
        driverTools.click(trainsSeatEleements.get(0));
        driverTools.click(CONFIRM_NEW_TRAIN_SEATS);
        driverTools.switchToNative();
    }

    public String getTrainSeatFromConfirmationPage() {
        //train order detail -> change seat page
        driverTools.switchToWebView();
        String seat = driverTools.getText(TRAIN_SEAT_FROM_CHANGE_TRAIN_SEAT_MENU).orElseThrow();
        driverTools.switchToNative();
        return seat;
    }

    public void clickChangeTrainSeat() {
        //train order detail page
        driverTools.switchToWebView();
        driverTools.swipeToElement(CHANGE_TRAIN_SEAT_BTN, Swipe.UP, 2).orElseThrow();
        driverTools.swipeUp();
        driverTools.click(CHANGE_TRAIN_SEAT_BTN);
        driverTools.switchToNative();
    }

    public void continueToPaymentTrainChangeSeat() {
        //train order detail -> change seat page
        driverTools.switchToWebView();
        driverTools.click(CONTINUE_TO_PAYMENT_FROM_CHANGE_SEAT);
        driverTools.switchToNative();
    }

    public JSONObject getInternalOrderDetail(String orderID) throws InterruptedException, ExecutionException{
        InternalOrderDetailApi.Inputs input = new InternalOrderDetailApi.Inputs(orderID);
        InternalOrderDetailApi internalOrderDetailApi = new InternalOrderDetailApi(input);
        ApiResponse response = internalOrderDetailApi.hitApi(apiTools);
        return new JSONObject(response.getBody());
    }

    public String getOrderCurrency(String orderId) throws InterruptedException, ExecutionException {
        JSONObject response = this.getInternalOrderDetail(orderId);
        return response.getJSONObject("data").getString("customerCurrency");
    }

    @SneakyThrows
    public HotelInternalOrderDetail getHotelInternalOrderData(String orderId) {
        Gson gson = new Gson();
        HotelInternalOrderDetail response = gson.fromJson(this.getInternalOrderDetail(orderId).toString(), HotelInternalOrderDetail.class);
        return response;
    }

    public TrainInternalOrderDetail getTrainInternalOrderData(String orderId) throws JsonSyntaxException, InterruptedException, ExecutionException {
        Gson gson = new Gson();
        TrainInternalOrderDetail response = gson.fromJson(this.getInternalOrderDetail(orderId).toString(), TrainInternalOrderDetail.class);
        return response;
    }

    public CarTransferInternalOrderDetail getCarTransferInternalOrderdata(String orderId) throws JsonSyntaxException, InterruptedException, ExecutionException {
        Gson gson = new Gson();
        CarTransferInternalOrderDetail response = gson.fromJson(this.getInternalOrderDetail(orderId).toString(), CarTransferInternalOrderDetail.class);
        return response;
    }

    public FerryInternalOrderDetail getFerryInternalOrderDetail(String orderId) throws JsonSyntaxException, InterruptedException, ExecutionException {
        Gson gson = new Gson();
        FerryInternalOrderDetail response = gson.fromJson(this.getInternalOrderDetail(orderId).toString(), FerryInternalOrderDetail.class);
        return response;
    }

     /**
     * @param orderId
     * @return String
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public String getHotelOrderNameFromInternalOrder(HotelInternalOrderDetail response, String orderId) {
        for (HotelInternalOrderDetail.OrderDetails orderDetail : response.data.orderDetails) {
            if (orderDetail.type.equals("tixhotel") || orderDetail.type.equals("hotel_homes")) {
                return orderDetail.orderName;
            }
        }
        throw new RuntimeException("Cannot find ordername in order detail with orderID : " + orderId);
    }

    /**
     * @param orderId
     * @return String
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public String getHotelItineraryFromInternalOrder(HotelInternalOrderDetail response, String orderId) throws InterruptedException, ExecutionException {
        for (HotelInternalOrderDetail.OrderDetails orderDetail : response.data.orderDetails) {
            if (orderDetail.type.equals("tixhotel")) {
                return orderDetail.vertical.hotelItineraryNumber;
            }
        }
        throw new RuntimeException("Cannot find internaryNumber in order detail with orderID : " + orderId);
    }

    /**
     * @param orderId
     * @return String
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public String getHotelTimeZoneFromInternalOrder(HotelInternalOrderDetail response, String orderId) throws InterruptedException, ExecutionException {
        for (HotelInternalOrderDetail.OrderDetails orderDetail : response.data.orderDetails) {
            if (orderDetail.type.equals("tixhotel")) {
                return orderDetail.startTimestampTimezone;
            }
        }
        throw new RuntimeException("Cannot find timezone in order detail with orderID : " + orderId);
    }

    public void chooseAndSubmitRefundReason() {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        driverTools.waitForElementToPresence(REFUND_REASON_INPUT, 20).orElseThrow();
        driverTools.click(REFUND_REASON_INPUT);

        // choose reason refund reason randomly
        driverTools.waitForElementToAppear(REFUND_REASONS, 10);
        List<WebElement> reasons = driverTools.elements(REFUND_REASONS).orElseThrow();
        WebElement[] reason = reasons.toArray(new WebElement[reasons.size()]);
        driverTools.click(CoreFunctions.getRandomObject(reason));

        WebElement submitRefundReasonButton = driverTools.swipeToElement(SUBMIT_REFUND_REASON_BUTTON, Swipe.UP, 3).orElseThrow();
        driverTools.click(submitRefundReasonButton);
        driverTools.switchToNative();
    }

    public String getTotalRefundEstimation() {
        driverTools.switchToWebView();
        driverTools.waitForElementToPresence(TOTAL_REFUND_ESTIMATION).orElseThrow();
        String totalRefundEstimation = driverTools.getText(TOTAL_REFUND_ESTIMATION).orElseThrow();
        driverTools.switchToNative();

        return totalRefundEstimation;
    }

    public abstract void goToTotalPayment();

    public abstract String getTotalPayment();

    public abstract Double getTotalPaymentPriceDetails();

    public abstract String getTotalPaymentHotel();

    public abstract String getTTDProductTitle();

    /** @return true if the edit guest name button is present/displayed, so the caller can skip the edit flow when it's absent. */
    public abstract boolean isEditGuestNameButtonDisplayed();

    public abstract void clickEditGuestName();

    public abstract boolean checkRoomDetail(List<String> guestNames);

    public abstract boolean checkDetailsEditPage(String orderID, String hotelName, List<String> guestNames);

    public abstract void editGuestName();

    public abstract boolean isGuestNameUpdated();

    public abstract String getFullBlackoutDate(String startBlackoutDate, String endBlackoutDate);

    public abstract void closeShareReceiptBtmSheet();

    private Locator getDateAndTimeLocatorTrain(String station) {
        return new Locator("DATE_TIME_FOR_" + station, AppiumBy.xpath("//*[contains(@class,'DetailReservation_content_flight')]//*[contains(text(),'" + station + "')]//preceding-sibling::*"));
    }

    private Locator getStationLocatorTrain(String station) {
        return new Locator("DATE_TIME_FOR_" + station, AppiumBy.xpath("//*[contains(@class,'DetailReservation_content_flight')]//*[contains(text(),'" + station + "')]"));
    }

    public void chooseRoomType(String targetUrl) {
        try {
            driverTools.switchToWebViewUrl(targetUrl);
            if (driverTools.waitForElementToPresence(ROOM_TYPE_CHECKBOX, 5).isPresent()) {
                driverTools.clickWithJavascriptExecutor(ROOM_TYPE_CHECKBOX);
                driverTools.clickWithJavascriptExecutor(SUBMIT_ROOM_TYPE);
            } else {
                logger.log("Room type is not available");
            }
        } catch (NoSuchContextException e) {
            logger.log("No WebView context found.");
        } finally {
            driverTools.switchToNative();
        }
    }

    private String removeDayOfWeek(String date) {
        return date.replaceFirst("^.*?,\\s*", "");
    }

    private String convertStation(String station) {
        station += ' ';
        String ans = "";
        for (int i = 0; i < station.length(); i++) {
            if (station.charAt(i) == '(')
                break;
            ans += station.charAt(i);
            ;
        }
        return ans.substring(0, ans.length() - 1);
    }

    private String getDateTimeTrain(String station) {
        WebElement dateAndTimeOrigin = driverTools.waitForElementToAppear(getDateAndTimeLocatorTrain(station)).orElseThrow();
        String date = removeDayOfWeek(driverTools.getText(dateAndTimeOrigin));
        return date;
    }

    private boolean checkDateTrainDetailTrain(TrainData data) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + data.orderId);
        String dateAndTimeOrigin = getDateTimeTrain(data.departureStation.split(" ")[0]);
        logger.log("origin date and time -> " + dateAndTimeOrigin);
        String dateAndTimeDestination = getDateTimeTrain(data.arrivalStation.split(" ")[0]);
        logger.log("destination date and time -> " + dateAndTimeDestination);

        String dateAndTimeOriginFromPaymentPage = data.departureDate + " " + data.departYear + " - " + data.departureTime;
        logger.log("origin date and time from payment page -> " + dateAndTimeOriginFromPaymentPage);
        String dateAndTimeDestinationFromPaymentPage = data.arrivalDate + " " + data.arrivalYear + " - " + data.arrivalTime;
        logger.log("destination date and time from payment page -> " + dateAndTimeDestinationFromPaymentPage);

        boolean isDateAndTimeOriginCorrect = dateAndTimeOrigin.equals(dateAndTimeOriginFromPaymentPage);
        logger.log("origin date and time correct -> " + isDateAndTimeOriginCorrect);
        boolean isDateAndTimeDestinationCorrect = dateAndTimeDestination.equals(dateAndTimeDestinationFromPaymentPage);
        logger.log("destination date and time correct -> " + isDateAndTimeDestinationCorrect);

        driverTools.switchToNative();
        return isDateAndTimeOriginCorrect && isDateAndTimeDestinationCorrect;
    }

    public String formatDate(String date) {
        String[] dateSplit = date.split(" ");
        String monthFormatted = CoreFunctions.monthShortToShortIdToEng(dateSplit[1]);
        if (monthFormatted != null) {
            dateSplit[1] = monthFormatted;
            return String.join(" ", dateSplit);
        } else {
            return date;
        }
    }

    private boolean checkStationDetailsTrain(TrainData data) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + data.orderId);
        String originStation = driverTools.getText(driverTools.element(getStationLocatorTrain(data.departureStation.split(" ")[0])).orElseThrow());
        logger.log("origin station order detail-> " + originStation);
        logger.log("origin station from payment page -> " + data.departureStation);
        String destinationStation = driverTools.getText(driverTools.element(getStationLocatorTrain(data.arrivalStation.split(" ")[0])).orElseThrow());
        logger.log("destination station order detail-> " + destinationStation);
        logger.log("destination station from payment page -> " + data.arrivalStation);


        boolean isOriginStationCorrect = originStation.toLowerCase().contains(data.departureStation.toLowerCase());
        logger.log("origin station correct -> " + isOriginStationCorrect);
        boolean isDestinationStationCorrect = destinationStation.toLowerCase().contains(data.arrivalStation.toLowerCase());
        logger.log("destination station correct -> " + isDestinationStationCorrect);
        driverTools.switchToNative();
        return isOriginStationCorrect && isDestinationStationCorrect;
    }

    private boolean checkBookingCode(TrainData data) {
        boolean isBookingCodeCorrect = true;
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + data.orderId);
        if (!(Configs.ENVIRONMENT == Environment.PROD)) {
            String bookingCode = driverTools.getText(BOOKING_CODE_WEBVIEW).orElseThrow();
            logger.log("booking code order detail -> " + bookingCode);
            isBookingCodeCorrect = !bookingCode.isEmpty();
            logger.log("is booking code correct -> " + isBookingCodeCorrect);
        }
        driverTools.switchToNative();
        return isBookingCodeCorrect;
    }

    public boolean checkTrainDetails(TrainData data) {
        boolean isDateCorrect = checkDateTrainDetailTrain(data);
        boolean isStationDetailCorrect = checkStationDetailsTrain(data);
        boolean isBookingCodeCorrect;
        if (Configs.ENVIRONMENT != Environment.PROD) {
            isBookingCodeCorrect = checkBookingCode(data);
        } else {
            logger.log("booking code is not checked in production environment");
            isBookingCodeCorrect = true;
        }
        Assertion.assertThat("is date correct ", isDateCorrect, is(true));
        Assertion.assertThat("is station detail correct", isStationDetailCorrect, is(true));
        return isDateCorrect && isStationDetailCorrect && isBookingCodeCorrect;
    }

    public String convertSeatCode(String seat) {
        String[] seatArray = seat.split(" ");
        switch (seatArray[0].toLowerCase()) {
            case "economy", "ekonomi", "eko" -> seatArray[0] = "EKO";
            case "executive", "eksekutif", "eks" -> seatArray[0] = "EKS";
            case "business", "bisnis", "bis" -> seatArray[0] = "BIS";
            case "premium", "premiere", "pre" -> seatArray[0] = "PRE";
            default -> throw new NotImplementedException("Unimplemented seat type type!");
        }
        seat = "";
        for (String x : seatArray) {
            seat += x;
            seat += " ";
        }
        return seat.substring(0, seat.length() - 1);
    }

    public String convertSeatCodeWithLang(String seat, String lang) {
        String[] seatArray = seat.split(" ");

        if (lang.equals("id")) {
            switch (seatArray[0].toLowerCase()) {
                case "economy", "ekonomi" -> seatArray[0] = "EKO";
                case "executive", "eksekutif" -> seatArray[0] = "EKS";
                case "business", "bisnis" -> seatArray[0] = "BIS";
                default -> throw new NotImplementedException("Unimplemented seat type type!");
            }
        } else {
            switch (seatArray[0].toLowerCase()) {
                case "economy", "ekonomi" -> seatArray[0] = "ECO";
                case "executive", "eksekutif" -> seatArray[0] = "EXC";
                case "business", "bisnis" -> seatArray[0] = "BUS";
                default -> throw new NotImplementedException("Unimplemented seat type type!");
            }
        }

        seat = "";
        for (String x : seatArray) {
            seat += x;
            seat += " ";
        }
        return seat.substring(0, seat.length() - 1);
    }

    public String convertSeatCodeSpecial(String seat) {
        String[] seatArray = seat.split(" ");
        switch (seatArray[0].toLowerCase()) {
            case "economy", "ekonomi" -> seatArray[0] = "PRE";
            case "executive", "eksekutif" -> seatArray[0] = "PRI";
            default -> throw new NotImplementedException("Unimplemented seat type type!");
        }
        seat = "";
        for (String x : seatArray) {
            seat += x;
            seat += " ";
        }
        return seat.substring(0, seat.length() - 1);
    }

    public void checkPassengerDetailsTrain(TrainData data) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + data.orderId);
        logger.log("Opening passenger details");

        driverTools.scrollIntoWebView(DETAILS_BTN_TRAIN_PASSENGER_DETAILS_WEBVIEW);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(DETAILS_BTN_TRAIN_PASSENGER_DETAILS_WEBVIEW);

        String name = getPassengerName();
        String seat = getPassengerSeat(data.seat);
        boolean isPassengerTypeCorrect = isPassengerTypeDisplayed();

        // Close detail view
        if (driverTools.isElementDisplayed(CLOSE_GUEST_DETAIL_WEBVIEW)) {
            driverTools.click(CLOSE_GUEST_DETAIL_WEBVIEW);
        } else {
            logger.log("Close guest detail button not found");
            driverTools.back();
        }

        // Assertions
        Assertion.assertThat("Verify passenger name", name, containsString(data.guestName));
        Assertion.assertThat("Verify passenger seat", seat, containsString(extractSeatNumber(data.seat)));
        Assertion.assertThat("Verify passenger type", isPassengerTypeCorrect, is(true));

        driverTools.switchToNative();
    }

    private String getPassengerName() {
        WebElement nameEl = driverTools.waitForElementToAppear(GUEST_NAME_TRAIN_PASSENGER_DETAIL_WEBVIEW)
                .orElseThrow(() -> new RuntimeException("Passenger name element not found"));
        String name = driverTools.getText(nameEl);
        logger.log("Passenger name in order detail: " + name);
        return name;
    }

    private String getPassengerSeat(String seat) {
        WebElement seatElement = driverTools.waitForElementToAppear(SEAT_TRAIN_PASSENGER_DETAIL_WEBVIEW).orElseThrow();
        String seatText = driverTools.getText(seatElement);
        logger.log("Passenger seat in order detail: " + seatText);
        seatText = extractSeatNumber(seatText);
        logger.log("Extracted seat number: " + seatText);
        return seatText;
    }

    public String extractSeatNumber(String seat) {
        // Find first digit and take the substring from their till end
        Matcher matcher = Pattern.compile("(\\d+\\s*[-/]?\\s*\\d*[A-Z]?)").matcher(seat);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return seat.trim(); // fallback
    }

    private boolean isPassengerTypeDisplayed() {
        boolean displayed = driverTools.isElementDisplayed(GUEST_TYPE_TRAIN_PASSENGER_DETAIL_WEBVIEW);
        logger.log("Is passenger type displayed? -> " + displayed);
        return displayed;
    }

    public boolean verifyTotalPaymentTrain(String totalPrice) {
        driverTools.switchToWebView();
        WebElement totalPaymentElement = driverTools.swipeToElement(TOTAL_PAYMENT_TRAIN_WEBVIEW, Swipe.UP, 4).orElseThrow();
        double totalPaymentDetailPage = CoreFunctions.convertPrice(driverTools.getText(totalPaymentElement));
        logger.log("total Payment detail page : " + totalPaymentDetailPage);
        double totalPricePaymentPage = CoreFunctions.convertPrice(totalPrice);
        logger.log("total Payment payment page : " + totalPricePaymentPage);
        driverTools.switchToNative();
        return totalPaymentDetailPage == totalPricePaymentPage;
    }

    public boolean verifyTotalPaymentTrain(TrainData data, String totalPrice) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + data.orderId);
        WebElement totalPaymentElement = driverTools.swipeToElement(TOTAL_PAYMENT_TRAIN_WEBVIEW, Swipe.UP, 4).orElseThrow();
        double totalPaymentDetailPage = CoreFunctions.convertPrice(driverTools.getText(totalPaymentElement));
        logger.log("total Payment detail page : " + totalPaymentDetailPage);
        double totalPricePaymentPage = CoreFunctions.convertPrice(totalPrice);
        logger.log("total Payment payment page : " + totalPricePaymentPage);
        driverTools.switchToNative();
        return totalPaymentDetailPage == totalPricePaymentPage;
    }

    public abstract String getBusSeatNoDetail();

    public abstract String getBusPassengerDetails();

    public void goToDepartureDetail() {
        driverTools.switchToWebViewUrl("yourorder/group");
        driverTools.click(DEPARTURE_DATE);
        driverTools.switchToNative();
    }

    public void goToReturnDetail() {
        driverTools.switchToWebViewUrl("yourorder/group");
        driverTools.click(RETURN_DATE);
        driverTools.switchToNative();
    }

    public String getBusDepartureDate(String orderID, String orderHash, String orderDetail) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderID + "&orderHash=" + orderHash + "&orderDetailId=" + orderDetail);
        logger.log("url -> " + driverTools.getCurrentURL());
        driverTools.waitForElementToAppear(DEPARTURE_DATE, 25);
        String departureDate = driverTools.getText(DEPARTURE_DATE).orElseThrow();
        logger.log("Departure Date : " + departureDate);
        driverTools.switchToNative();
        return departureDate;
    }

    public String getBusReturnDate(String orderID, String orderHash, String orderDetail) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderID + "&orderHash=" + orderHash + "&orderDetailId=" + orderDetail);
        logger.log("url -> " + driverTools.getCurrentURL());
        driverTools.waitForElementToAppear(RETURN_DATE, 25);
        String returnDate = driverTools.getText(RETURN_DATE).orElseThrow();
        logger.log("Return Date : " + returnDate);
        driverTools.switchToNative();
        return returnDate;
    }

    public abstract boolean isETiketAndReceiptCardDisplayedNative();
    public void clickBackNew()
    {
        if (Configs.PLATFORM == Platform.IOS) {
            driverTools.switchToNative();
            driverTools.click(BACK_BUTTON);
        } else {
            driverTools.waitForWebview();
            driverTools.switchToWebViewUrl("yourorder");
            driverTools.back();
            driverTools.switchToNative();
        }
    }

    public void clickBack() {
        if (Configs.PLATFORM == Platform.IOS) {
            driverTools.switchToNative();
            driverTools.click(BACK_BUTTON);
        } else {
            driverTools.waitForWebview();
            driverTools.switchToWebViewUrl("yourorder");
            driverTools.back();
            driverTools.switchToNative();
        }
    }

    public List<String> getGuestDetailsName() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder");
        List<String> guestNameList = new ArrayList<>();
        driverTools.swipeToElement(GUEST_DETAILS_NAME_WEBVIEW, Swipe.UP, 3);
        List<WebElement> guestNameElements = driverTools.elements(GUEST_DETAILS_NAME_WEBVIEW).orElseThrow();
        logger.log("totalGuest order details -> " + guestNameElements.size());
        for (WebElement guestNameElement : guestNameElements) {
            String[] guestName = guestNameElement.getText().split("\\.");
            guestNameList.add(guestName[1].trim());
        }
        driverTools.switchToNative();
        logger.log("guestNameList-> " + guestNameList);
        return guestNameList;
    }

    public String getSpecialRequest() {
        driverTools.switchToWebView();
        String request = driverTools.getText(SPECIAL_REQUEST_WEBVIEW).orElseThrow();
        logger.log("Special request: " + request);
        return request;
    }

    /**
     * Cross-platform check that the order-detail page is displaying the given
     * special-request text. Android delegates to the webview-based
     * {@link #getSpecialRequest()}; iOS overrides this in
     * {@code OrderDetailPageIOS} to read from the native page source, because
     * the order-detail page on iOS is native and the webview-based reader
     * hangs waiting for a JS atom that never resolves.
     */
    public boolean containsSpecialRequest(String specialRequestText) {
        if (specialRequestText == null || specialRequestText.isEmpty()) {
            return false;
        }
        String actual = getSpecialRequest();
        Assertion.assertThat("Special request contains the special request text", actual.toLowerCase(Locale.ROOT), containsString(specialRequestText.toLowerCase(Locale.ROOT)));
        // Locale.ROOT keeps the case fold ASCII-equivalent so we don't get
        // surprised by locale-specific mappings (e.g. Turkish dotted/dotless I)
        // when the JVM default locale changes across CI environments.
        return actual != null
                && actual.toLowerCase(Locale.ROOT)
                .contains(specialRequestText.toLowerCase(Locale.ROOT));
    }

    public String getTotalPaymentWebView() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("orderId", 3);
        driverTools.swipeToElement(TOTAL_PAYMENT_WEBVIEW, Swipe.UP, 10);
        String totalPayment = driverTools.getText(TOTAL_PAYMENT_WEBVIEW).orElseThrow();
        driverTools.switchToNative();
        return CoreFunctions.normalizePrice(totalPayment);
    }

    public abstract Boolean checkPriceDetailIsIn(String currency);

    public abstract Boolean isShareReceiptButtonDisplayed();

    public abstract Boolean isReceiptButtonWork(String currency);

    public abstract Boolean isRefundButtonDisplayed();

    public boolean checkAddon(String addOnName,int addOnQty){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder");
        // The add-on name comes from the catalogue API and can contain
        // apostrophes (e.g. "Chef's Special"); always wrap it through
        // CoreFunctions.toXPathLiteral() so the generated XPath stays valid.
        String qtyAndName = CoreFunctions.toXPathLiteral(addOnQty + " " + addOnName);
        // Primary locator: anchored to the "Add-ons" / "Ekstra" section header.
        Locator addOnLocator = new Locator(addOnName, AppiumBy.xpath(
                "//*[contains(text(),'ons') or contains(text(),'Ekstra')]"
                        + "//following::*[contains(text()," + qtyAndName + ")]"));
        boolean isAddOnAvailable = driverTools.isElementDisplayed(addOnLocator);
        // Fallback: the redesigned page sometimes renders the add-on row
        // without the section header that the primary locator anchored on, so
        // match the literal "<qty> <addOnName>" text anywhere in the webview.
        if (!isAddOnAvailable) {
            Locator fallbackLocator = new Locator(addOnName + "_FALLBACK",
                    AppiumBy.xpath("//*[normalize-space(text())=" + qtyAndName
                            + " or contains(normalize-space(.), " + qtyAndName + ")]"));
            isAddOnAvailable = driverTools.isElementDisplayed(fallbackLocator);
        }
        driverTools.switchToNative();
        return isAddOnAvailable;
    }

    private static final Locator ROOM_TYPE = new Locator("ROOM_TYPE", AppiumBy.xpath("//*[contains(@class,'label_button')]"));

    private static Locator ROOM_TYPE_NAME(int roomType) {
        return new Locator("ROOM_TYPE_NAME_" + roomType, AppiumBy.xpath("//*[contains(@class,'label') and contains(text(),'" + roomType + "')]//parent::*//parent::*//parent::*//parent::*//parent::*[contains(@class,'hotel_reservation_v2_accordion')]//*[contains(@class,'room_name') and not(contains(@class,'wrapper'))]"));
    }

    private static Locator ROOM_TYPE_ADDONS(int roomType) {
        return new Locator("ROOM_TYPE_ADDONS_" + roomType, AppiumBy.xpath("//*[contains(@class,'label') and contains(text(),'" + roomType + "')]//parent::*//parent::*//parent::*//parent::*//parent::*[contains(@class,'hotel_reservation_v2_accordion')]//*[contains(text(),'ons') or contains(text(),'Ekstra')]//parent::*//following-sibling::ul"));
    }

    public boolean checkAddon(String addOnName, int itemPerRoom, HashMap<Integer, String> roomName) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("yourorder");
        int totalExpectedRoomType = roomName.size();
        logger.log("roomNames ->" + roomName);
        driverTools.waitForElementToAppear(ROOM_TYPE).orElseThrow();
        Assertion.assertThat("Total Room Type should be equal with the selected room list", driverTools.elements(ROOM_TYPE).orElseThrow().size(), is(totalExpectedRoomType));
        for (int i = 1; i <= totalExpectedRoomType; i++) {
            String roomNameString = roomName.get(i);
            int actualIndex = 1;

            for (int j = 1; j <= totalExpectedRoomType; j++) {
                String actualRoomName = driverTools.getText(driverTools.waitForElementToAppear(ROOM_TYPE_NAME(j)).orElseThrow());
                logger.log("index " + j + " " + actualRoomName + "and we are looking for " + roomNameString);
                if (actualRoomName.contains(roomNameString)) {
                    actualIndex = j;
                }
            }

            logger.log("index found: " + actualIndex);

            Assertion.assertThat(i + " Room Name should be contains the selected room list", driverTools.getText(driverTools.waitForElementToAppear(ROOM_TYPE_NAME(actualIndex)).orElseThrow()), containsString(roomNameString));
            Assertion.assertThat(i + " Addons should be contains the selected addons booking form", driverTools.getText(driverTools.waitForElementToAppear(ROOM_TYPE_ADDONS(actualIndex)).orElseThrow()), containsString(itemPerRoom + " " + addOnName));
        }
        driverTools.switchToNative();
        return true;
    }

    public boolean checkETicket(String pdfContent, String addOnName, int itemPerRoom, HashMap<Integer, String> roomName) {
        for (int i = 1; i <= roomName.size(); i++)
            Assertion.assertThat("E-Voucher should contain " + roomName.get(i), pdfContent, containsString(roomName.get(i)));
        Assertion.assertThat("E-Voucher should contain " + itemPerRoom + " " + addOnName, pdfContent, containsString(itemPerRoom + " " + addOnName));
        return true;
    }

    public boolean isRefundInfoDisplayed(String orderId) {
        driverTools.switchToWebViewUrl(orderId);
        boolean isRefundInfoPresent = driverTools.isElementDisplayed(REFUND_INFO);
        driverTools.switchToNative();
        return isRefundInfoPresent;
    }

    public void openReceipt() {
        driverTools.switchToWebViewUrl("yourorder");
        if (driverTools.isElementPresent(MANAGE_ORDER_BUTTON)) driverTools.click(MANAGE_ORDER_BUTTON);
        driverTools.scrollIntoWebView(RECEIPT_BUTTON);
        driverTools.scrollElementToCenter(RECEIPT_BUTTON);
        driverTools.click(RECEIPT_BUTTON);
        chooseDefaultAppPdf();
        driverTools.switchToNative();
    }

    public abstract void shareReceiptNative();

    public void shareReceipt() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.scrollIntoWebView(SHARE_RECEIPT_BUTTON);
        driverTools.scrollElementToCenter(SHARE_RECEIPT_BUTTON);
        driverTools.click(SHARE_RECEIPT_BUTTON);
        driverTools.waitForElementToAppear(LoginPage.LOADING_STATE);
        driverTools.waitForElementToDisappear(LoginPage.LOADING_STATE);
        driverTools.switchToNative();
    }

    public abstract boolean isShareFileNameCorrect(String vertical, String orderId, String lang);

    public abstract void clickBackFromPdf();

    public OrderDetailPageModule getPageModuleOrderDetail(String accessToken, String orderId, String orderHash, String language) throws InterruptedException, ExecutionException {
        String orderCurrency = getOrderCurrency(orderId);
        GETPageModuleOrderDetail.Inputs inputs = new GETPageModuleOrderDetail.Inputs(accessToken, orderId, orderHash, language, orderCurrency);
        GETPageModuleOrderDetail getPageModuleOrderDetail = new GETPageModuleOrderDetail(inputs);
        ApiResponse response = getPageModuleOrderDetail.hitApi(apiTools);
        Gson gson = new Gson();
        OrderDetailPageModule orderDetailPageModule = gson.fromJson(response.getBody(), OrderDetailPageModule.class);
        return orderDetailPageModule;
    }

    @SneakyThrows
    public List<String> getPageModuleSectionUrl(String accessToken, String orderId, String orderHash, String language) throws InterruptedException, ExecutionException {
        String title = null;
        boolean isImageWithChipsFound = false;
        OrderDetailPageModule orderDetailPageModule = getPageModuleOrderDetail(accessToken, orderId, orderHash, language);
        logger.log("orderDetailPageModule -> " + new ObjectMapper().writeValueAsString(orderDetailPageModule));
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < orderDetailPageModule.data.size(); i++) {
            OrderDetailPageModule.PageModules pageModule = orderDetailPageModule.data.get(i);

            if(pageModule.templateCode.equals("IMAGE_WITH_CHIPS")) {
                title = pageModule.moduleDetail.title;
                isImageWithChipsFound = true;
            } else {
                continue;
            }

            ArrayList<BannerContents> bannerContents = null;
            if (pageModule.dynamicContents != null) {
                findBanners:
                for (OrderDetailPageModule.DynamicContent dynamicContent : pageModule.dynamicContents) {
                    if (dynamicContent == null || dynamicContent.chips == null) {
                        continue;
                    }
                    for (OrderDetailPageModule.Chip chip : dynamicContent.chips) {
                        if (chip != null && chip.bannerContents != null && !chip.bannerContents.isEmpty()) {
                            bannerContents = chip.bannerContents;
                            break findBanners;
                        }
                    }
                }
            }
            if (bannerContents == null) {
                continue;
            }
            String headingTitle =
                    pageModule.moduleDetail != null && pageModule.moduleDetail.heading != null && pageModule.moduleDetail.heading.title != null
                            ? pageModule.moduleDetail.heading.title
                            : "";
            logger.log("headingTitle: " + headingTitle);
            for (int x = 0;
                 x < bannerContents.size()
                         && headingTitle.length() > 0
                         && headingTitle.contains(title);
                 x++) {
                urls.add(bannerContents.get(x).clickUrl);
            }
        }
        if(!isImageWithChipsFound) {
            logger.log("No IMAGE_WITH_CHIPS module found in order detail page module");
        }
        return urls;
    }


    @SneakyThrows
    public String getPageModuleSectionTitle(String accessToken, String orderId, String orderHash, String language) {
        String title = null;
        OrderDetailPageModule orderDetailPageModule = getPageModuleOrderDetail(accessToken, orderId, orderHash, language);
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < orderDetailPageModule.data.size(); i++) {
            OrderDetailPageModule.PageModules pageModule = orderDetailPageModule.data.get(i);
            if(pageModule.templateCode.equals("IMAGE_WITH_CHIPS")) {
                return pageModule.moduleDetail.title;
            }
        }
        return title;
    }

    private Locator generateShareEticketEmaiLocator(String email) {
        return new Locator("EMAIL_" + email, AppiumBy.xpath("//input[@type='email' and @value='" + email + "']"));
    }


    protected boolean verifyShareEticketBtmSheetFromWebview() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.waitForElementToAppear(SHARE_ETICKET_BTM_SHEET, 2);
        //generate random email
        IntStream.range(0, 3).forEach(
                index -> {
                    String recipients = CoreFunctions.randomEmail();
                    driverTools.click(SHARE_ETICKET_BTM_SHEET_EMAIL_FIELD);
                    driverTools.sendKeys(SHARE_ETICKET_BTM_SHEET_EMAIL_FIELD, recipients);
                    if (index != (3 - 1)) {
                        driverTools.click(SHARE_ETICKET_ADD_OTHER_RECEIPIENT);
                    }
                    Assertion.assertThat("Verify email " + recipients + " present in ui", driverTools.waitForElementToAppear(generateShareEticketEmaiLocator(recipients)).isPresent(), is(true));
                }
        );

        driverTools.click(SHARE_ETICKET_CONFRIM_BTN);
        boolean isBtmSheetClose = driverTools.waitForElementToDisappear(SHARE_ETICKET_BTM_SHEET, 5);
        driverTools.switchToNative();
        Assertion.assertThat("Verify bottom sheet clsoed", isBtmSheetClose, is(true));
        return true;
    }

    public boolean verifyBusManageOrderSection() {
        driverTools.switchToWebViewUrl("order");
        driverTools.scrollIntoWebView(ADD_TO_CALENDAR_ON_MANAGE_ORDER_SECTION);
        Assertion.assertThat("Verify eticket button present", driverTools.isElementDisplayed(ETICKET_ON_MANAGE_ORDER_SECTION), is(true));
        Assertion.assertThat("Verify receipt button present", driverTools.isElementDisplayed(RECEIPT_BUTTON), is(true));
        driverTools.switchToNative();
        return true;
    }

    public boolean verifyFerryManageOrderSection() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.scrollIntoWebView(ADD_TO_CALENDAR_ON_MANAGE_ORDER_SECTION);
        Assertion.assertThat("Verify add to calender button present", driverTools.isElementDisplayed(ADD_TO_CALENDAR_ON_MANAGE_ORDER_SECTION), is(true));
        Assertion.assertThat("Verify eticket button present", driverTools.isElementDisplayed(ETICKET_ON_MANAGE_ORDER_SECTION), is(true));
        Assertion.assertThat("Verify receipt button present", driverTools.isElementDisplayed(RECEIPT_BUTTON), is(true));
        driverTools.switchToNative();
        return true;
    }

    public boolean verifyFerryRrInfoSection() {
        driverTools.switchToWebViewUrl("yourorder");
        boolean isRrInfoSectionPresent = driverTools.swipeToElement(RR_INFO_SECTION, Swipe.UP, 5).isPresent();
        driverTools.switchToNative();
        return isRrInfoSectionPresent;
    }

    public boolean verifyHelpCenterSectionPresentWebview() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.scrollIntoWebView(HELP_CENTER_CARD);
        boolean isHelpCenterCardDislayed = driverTools.isElementDisplayed(HELP_CENTER_CARD);
        Assertion.assertThat("Verify help center section present", isHelpCenterCardDislayed, is(true));
        driverTools.switchToNative();
        return true;
    }


    public abstract boolean verifyHelpCenterSectionPresent();

    public boolean openPriceDetail() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.swipeToElement(ORDER_DETAIL_CHEVRTON, Swipe.UP, 2);
        driverTools.click(ORDER_DETAIL_CHEVRTON);
        driverTools.switchToNative();
        return true;
    }

    public boolean openPriceDetail(String orderId) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderId);
        driverTools.swipeToElement(ORDER_DETAIL_CHEVRTON, Swipe.UP, 2);
        driverTools.click(ORDER_DETAIL_CHEVRTON);
        driverTools.switchToNative();
        return true;
    }

    public String getTotalPriceOrderDetailPage() {
        driverTools.switchToWebViewUrl("yourorder");
        String totalPrice = driverTools.getText(FERRY_ORDER_GROUP_TOTAL_PAYMENT).orElseThrow();
        logger.log("total price in order detail page: " + totalPrice);
        driverTools.switchToNative();
        return totalPrice;
    }

    public boolean verifyBusPriceDetailBreakDown() {
        driverTools.switchToWebViewUrl("yourorder");
        List<String> priceComponentsInPriceDetail = driverTools.getTexts(PRICE_DETAIL_COMPONENTS);
        logger.log("number of price component present : " + priceComponentsInPriceDetail.size());
        String totalPrice = driverTools.getText(PRICE_DETAIL_TOTAL_PRICE_VALUE).orElseThrow();
        // assertion price components should be in IDR
        Assertion.assertThat("Verify price components are in idr", priceComponentsInPriceDetail,
                Every.everyItem(anyOf(containsString("IDR"), equalTo("Termasuk"), equalTo("Included"))));
        Assertion.assertThat("Verify total price is in IDR", totalPrice, containsString("IDR"));

        // assertion for total price same as manual sum calculation
        List<Double> priceComponentsNumbersValue = priceComponentsInPriceDetail.stream()
                .filter(n -> n.matches(".*[0-9]+.*"))
                .map(n -> CoreFunctions.convertPriceSafely(n)).toList();// remove currency word from the value. Eg ->
        // IDR 5000 into 5000 (with data type double)
        // sum price components
        double sumOfPriceDetail = CoreFunctions.sumUpDouble(priceComponentsNumbersValue);
        // compare sumOfPriceDetail with actual value in ui
        Assertion.assertThat("Verify total sum is the same", CoreFunctions.convertPriceSafely(totalPrice),
                closeTo(sumOfPriceDetail, sumOfPriceDetail * 0.01));
        if(driverTools.isElementDisplayed(CLOSE_PRICE_DETAIL_WEBVIEW)) {
            driverTools.click(CLOSE_PRICE_DETAIL_WEBVIEW);
        } else {
            driverTools.back();
        }
        driverTools.switchToNative();
        return true;
    }

    public String getTotalPricePriceDetail() {
        driverTools.switchToWebViewUrl("yourorder");
        String totalPrice = driverTools.getText(PRICE_DETAIL_TOTAL_PRICE_VALUE).orElseThrow();
        logger.log("total price in price detail : " + totalPrice);
        driverTools.switchToNative();
        return totalPrice;
    }

    public boolean verifyFerryPriceDetailBreakDown() {
        driverTools.switchToWebViewUrl("yourorder");
        List<String> priceComponentsInPriceDetail = driverTools.getTexts(PRICE_DETAIL_COMPONENTS_FERRY);
        List<String> othersFeePriceDetail = driverTools.getTexts(OTHERS_FEE);
        logger.log("number of price component present : " + priceComponentsInPriceDetail.size());
        String totalPrice = driverTools.getText(PRICE_DETAIL_TOTAL_PRICE_VALUE).orElseThrow();
        // assertion price components should be in IDR
        Assertion.assertThat("Verify price components are in idr", priceComponentsInPriceDetail,
                Every.everyItem(anyOf(containsString("IDR"), equalTo("Termasuk"), equalTo("Included"))));
        Assertion.assertThat("Verify total price is in IDR", totalPrice, containsString("IDR"));

        // assertion for total price same as manual sum calculation
        List<Double> priceComponentsNumbersValue = priceComponentsInPriceDetail.stream()
                .filter(n -> n.matches(".*[0-9]+.*"))
                .map(n -> CoreFunctions.convertPriceSafely(n)).toList();// remove currency word from the value. Eg ->
        // IDR 5000 into 5000 (with data type double)
        // calculate deduction
        List<Double> othersFeeValue = othersFeePriceDetail.stream()
                .filter(n -> n.matches(".*[0-9]+.*"))
                .map(n -> CoreFunctions.convertPriceSafely(n)).toList();// remove currency word from the value. Eg ->
        // IDR 5000 into 5000 (with data type double)
        // sum price components
        double sumOfPriceDetail = CoreFunctions.sumUpDouble(priceComponentsNumbersValue);
        sumOfPriceDetail = sumOfPriceDetail + CoreFunctions.sumUpDouble(othersFeeValue);

        // compare sumOfPriceDetail with actual value in ui
        Assertion.assertThat("Verify total sum is the same", CoreFunctions.convertPriceSafely(totalPrice),
                equalTo(sumOfPriceDetail));
        driverTools.click(CLOSE_PRICE_DETAIL_WEBVIEW);
        driverTools.switchToNative();
        return true;
    }

    public boolean verifyCashbackPriceDetailBreakDown() {
        driverTools.switchToWebViewUrl("yourorder");
        List<String> priceComponentsInPriceDetail = driverTools.getTexts(PRICE_DETAIL_COMPONENTS_FERRY);
        List<String> othersFeePriceDetail = driverTools.getTexts(OTHERS_FEE);
        logger.log("number of price component present : " + priceComponentsInPriceDetail.size());
        String totalPrice = driverTools.getText(CASHBACK_AMOUNT).orElseThrow();
        // assertion price components should be in IDR
        Assertion.assertThat("Verify price components are in idr", priceComponentsInPriceDetail,
                Every.everyItem(anyOf(containsString("IDR"), equalTo("Termasuk"), equalTo("Included"))));
        Assertion.assertThat("Verify total cashback is in IDR", totalPrice, containsString("IDR"));

        // assertion for total price same as manual sum calculation
        List<Double> priceComponentsNumbersValue = priceComponentsInPriceDetail.stream()
                .filter(n -> n.matches(".*[0-9]+.*"))
                .map(CoreFunctions::convertPriceSafely).toList();   // remove currency word from the value. Eg ->
        // IDR 5000 into 5000 (with data type double)
        // calculate deduction
        List<Double> othersFeeValue = othersFeePriceDetail.stream()
                .filter(n -> n.matches(".*[0-9]+.*"))
                .map(CoreFunctions::convertPriceSafely).toList();  // remove currency word from the value. Eg ->
        // IDR 5000 into 5000 (with data type double)
        // sum price components
        double sumOfPriceDetail = CoreFunctions.sumUpDouble(priceComponentsNumbersValue);
        sumOfPriceDetail = sumOfPriceDetail + CoreFunctions.sumUpDouble(othersFeeValue);

        // compare sumOfPriceDetail with actual value in ui
        Assertion.assertThat("Verify total cashback is the same", CoreFunctions.convertPriceSafely(totalPrice),
                equalTo(Math.abs(sumOfPriceDetail)));
        driverTools.click(CLOSE_PRICE_DETAIL_WEBVIEW);
        driverTools.switchToNative();
        return true;
    }

    public boolean verifyBusDepartureSection() {
        driverTools.switchToWebViewUrl("order");
        String departureName = driverTools.getText(BUS_DEPARTURE_NAME).orElseThrow();
        Assertion.assertThat("Verify order departure bus name exist", departureName, not(blankOrNullString()));
        String departureDate = driverTools.getText(BUS_DEPARTURE_DATE).orElseThrow();
        Assertion.assertThat("Verify order departure date exist", departureDate, not(blankOrNullString()));
        String arrivalDate = driverTools.getText(BUS_RETURN_DATE).orElseThrow();
        Assertion.assertThat("Verify order return date exist", arrivalDate, not(blankOrNullString()));
        List<String> facility = driverTools.getTexts(BUS_FACILITY);
        Assertion.assertThat("Verify facilities present", facility, Every.everyItem(not(blankOrNullString())));
        driverTools.switchToNative();
        return true;
    }

    public void openBusTripDetail() {
        driverTools.switchToWebViewUrl("order");
        driverTools.scrollIntoWebView(BUS_SEE_TRIP_DETAIL_BTN);
        driverTools.clickWithJavascriptExecutor(BUS_SEE_TRIP_DETAIL_BTN);
        driverTools.switchToNative();
    }

    public boolean verifyTripDetailPresent() {
        driverTools.switchToWebViewUrl("order");
        String departureName = driverTools.getText(BUS_DEPARTURE_NAME_TRIP_DETAIL).orElseThrow();
        Assertion.assertThat("Verify order departure bus name exist", departureName, not(blankOrNullString()));
        List<String> stopLocation = driverTools.getTexts(BUS_DEPARTURE_LOCATIONS_TRIP_DETAIL);
        Assertion.assertThat("Verify stop location present", stopLocation, Every.everyItem(not(blankOrNullString())));
        List<String> facility = driverTools.getTexts(BUS_FACILITIES_TRIP_DETAIL);
        Assertion.assertThat("Verify facilities present", facility, Every.everyItem(not(blankOrNullString())));
        if(driverTools.isElementDisplayed(BUS_SEE_TRIP_DETAIL_CLOSE_BTN)) {
            driverTools.click(BUS_SEE_TRIP_DETAIL_CLOSE_BTN);
        } else {
            driverTools.back();
        }
        driverTools.switchToNative();
        return true;
    }

    public abstract void closePriceDetailNative();

    public void closePriceDetail() {
        driverTools.switchToWebViewUrl("detail?orderId");
        driverTools.swipeDown(0.9);
        driverTools.swipeDown(0.9);
        driverTools.switchToNative();
    }

    public void closePriceDetail(String orderId) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderId);
        driverTools.waitForElementToAppear(CLOSE_PRICE_DETAIL_WEBVIEW);
        driverTools.click(CLOSE_PRICE_DETAIL_WEBVIEW);
        driverTools.switchToNative();
    }

    public Double getTotalTicketPriceOnly() {
        driverTools.switchToWebViewUrl("detail?orderId");
        driverTools.waitForElementToAppear(PRICE_DETAIL_TICKET_ONLY);
        List<String> ticketsPriceText = driverTools.getTexts(PRICE_DETAIL_TICKET_ONLY);

        return CoreFunctions.sumUpStringNumbers(ticketsPriceText);
    }

    public Double getTotalTicketPriceOnly(String orderId) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderId);
        driverTools.waitForElementToAppear(PRICE_DETAIL_TICKET_ONLY);
        List<String> ticketsPriceText = driverTools.getTexts(PRICE_DETAIL_TICKET_ONLY);

        return CoreFunctions.sumUpStringNumbers(ticketsPriceText);
    }

    public void verifyRefundStatusTitleHotel() {
        driverTools.switchToWebView();
        boolean isRefundHotelDisplayed = driverTools.isElementDisplayed(REFUND_HOTEL_STATUS);
        Assertion.assertThat("is refund hotel status present", isRefundHotelDisplayed, is(true));
        driverTools.switchToNative();
    }

    public abstract Boolean verifyFerryCheckinDate(String BookingFormCheckIndate);

    public abstract Boolean verifyFerryRoute(String BookingFormRoute);

    public abstract Boolean verifyFerryPassengerNames(List<String> bookingFormPassengerNames);

    public abstract Boolean verifyIssuedBarCode();

    public abstract Boolean verifyIssuedViewEticket(String language);

    public abstract Boolean verifyIssuedShareEticket();

    public String getTTDValidityPeriod() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("detail?orderId");
        driverTools.waitForElementToAppear(TTD_VALIDITY_PERIOD);
        String result = driverTools.getText(TTD_VALIDITY_PERIOD).orElseThrow();
        driverTools.switchToNative();

        return result;
    }

    public abstract boolean verifyTTDLoyaltyDeduction();

    public abstract Boolean verifyReviseSpecialRequest();

    public abstract Boolean verifyLocationRedirectionButton();

    public abstract Boolean verifyRefundAndRescheduleInfo();

    public abstract boolean isRescheduleButtonDisplayed();

    public boolean isRescheduleButtonDisplayed(String orderId) {
        driverTools.switchToWebViewUrl(orderId);
        boolean isRescheduleButtonPresent = driverTools.isElementDisplayed(RESCHEDULE_BUTTON);
        driverTools.switchToNative();
        return isRescheduleButtonPresent;
    }

    public boolean isDeleteOrderButtonDisplayed(String orderId) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderId);
        driverTools.scrollUntilElementFound(REFUND_RESCHEDULE_DELETE_ORDER_BUTTON);
        boolean isDeleteOrderButtonPresent = driverTools.isElementDisplayed(REFUND_RESCHEDULE_DELETE_ORDER_BUTTON);
        driverTools.switchToNative();
        return isDeleteOrderButtonPresent;
    }

    /**
     * Clicks the "Cancel order" button on the Refund &amp; Reschedule info section.
     * iOS overrides with native.
     */
    public void clickRefundRescheduleCancelOrderButton(String orderId) {
        driverTools.switchToWebViewUrl("yourorder/detail?orderId=" + orderId);
        driverTools.scrollUntilElementFound(REFUND_RESCHEDULE_DELETE_ORDER_BUTTON);
        WebElement button = driverTools.waitForElementToAppear(REFUND_RESCHEDULE_DELETE_ORDER_BUTTON).orElseThrow();
        driverTools.scrollIntoWebViewCenter(button);
        driverTools.click(button);
        driverTools.switchToNative();
    }

    public abstract boolean verifyPageModule();

    public abstract boolean verifyEditGuestDetail();

    public abstract boolean verifyLpgBanner();

    public abstract boolean statusChange();
    public abstract void hidePromoBannerIfPresent();
    public abstract boolean ButtonFullProtectionSubmitNewClaim();
    public abstract void clickSubmitClaimBtn();
    public abstract boolean verifyLowestPriceGuaranteePage();
    public abstract boolean openAndCloseReadBeforeClaim();
    public abstract void fillClaimDetails(String finalPrice, String url);
    public abstract void uploadScreenshot();
    public abstract void checkTnC();
    public abstract void clickClaimNow();
    public abstract boolean verifyHighPriceValidationError();
    public abstract boolean verifySeeGuideline();

    public boolean verifyManageOrderSection() {
        driverTools.switchToWebView();
        driverTools.scrollIntoWebView(ETICKET_ON_MANAGE_ORDER_SECTION);
        boolean isEticketPresent = driverTools.isElementPresent(ETICKET_ON_MANAGE_ORDER_SECTION);
        driverTools.switchToNative();
        return isEticketPresent;
    }

    public boolean verifyManageOrderSection(String orderId) {
        driverTools.switchToWebViewUrl(orderId);
        // The new ManageOrder DOM lives inside a visibility:hidden wrapper on
        // first paint, which can make scrollIntoView throw. We still want the
        // DOM-presence check to run, so don't let the scroll failure mask it.
        try {
            driverTools.scrollIntoWebView(ETICKET_ON_MANAGE_ORDER_SECTION);
        } catch (Exception scrollEx) {
            logger.log("scrollIntoWebView for E-ticket failed (continuing to DOM-presence check): "
                    + scrollEx.getMessage());
        }
        boolean isEticketPresent = driverTools.isElementPresent(ETICKET_ON_MANAGE_ORDER_SECTION);
        driverTools.switchToNative();
        return isEticketPresent;
    }

    public abstract String getImportantInfo();

    public String getImportantInfoWebview() {
        driverTools.switchToWebViewUrl("yourorder");
        String importantInfoText = driverTools.getText(IMPORTANT_INFO_CARD).orElseThrow();
        driverTools.switchToNative();
        return importantInfoText;
    }

    public abstract String getTTDPackageName();

    public String getTTDPackageNameWebview() {
        driverTools.switchToWebViewUrl("yourorder");
        String packageName = driverTools.getText(TTD_PACKAGE_NAME).orElseThrow();
        driverTools.switchToNative();
        return packageName;
    }

    public abstract void openTTDGuestDetail();

    public void openTTDGuestDetailWebview() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.clickWithJavascriptExecutor(TTD_GUEST_DETAIL_BUTTON);
        driverTools.switchToNative();
    }

    public abstract String getInsuranceTTD();

    public String getInsuranceTTDWebview() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.scrollIntoWebView(TTD_INSURANCE);
        String insurance = driverTools.getText(TTD_INSURANCE).orElseThrow();
        driverTools.switchToNative();
        return insurance;
    }

    public abstract void clickDetailTTD();

    public void clickDetailTTDWebview() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.clickWithJavascriptExecutor(TTD_ORDER_DETAIL);
        driverTools.switchToNative();
    }

    public abstract String getTTDGuestFullName();

    public String getTTDGuestFullNameWebview() {
        driverTools.switchToWebViewUrl("yourorder");
        String guestDetailFullName = driverTools.getText(TTD_GUEST_DETAIL_FULL_NAME).orElseThrow();
        driverTools.switchToNative();
        return guestDetailFullName;
    }

    public abstract void closeTTDGuestDetail();

    public void closeTTDGuestDetailWebview() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.click(TTD_CLOSE_GUEST_DETAIL);
        driverTools.switchToNative();
    }

    public abstract void continueToPayment();

    public boolean passengerAndFacilitiesExists() {
        driverTools.switchToWebView();
        driverTools.swipeToElement(PASSENGERS_AND_FACILITIES_SECTION, Swipe.UP, 3);
        boolean isPassengerAndFacilitiesSectionPresent = driverTools.isElementDisplayed(PASSENGERS_AND_FACILITIES_SECTION);
        boolean isFacilitiesPresent = driverTools.isElementDisplayed(PASSENGER_DETAIL);
        driverTools.switchToNative();
        return isPassengerAndFacilitiesSectionPresent && isFacilitiesPresent;
    }

    public boolean isTrainNameAndClassExists() {
        driverTools.switchToWebView();
        boolean isTrainNameAndClassPresent = driverTools.isElementDisplayed(TRAIN_DETAILS);
        driverTools.switchToNative();
        return isTrainNameAndClassPresent;
    }

    public boolean isTrainDepartureArrivalDurationExists() {
        driverTools.switchToWebView();
        boolean isDurationPresent = driverTools.isElementDisplayed(TRAIN_DETAIL_DURATION);
        boolean isTrainDepartureAndArrivalPresent = driverTools.isElementDisplayed(TRAIN_DETAIL_CONTENT);
        driverTools.switchToNative();
        return isTrainDepartureAndArrivalPresent && isDurationPresent;
    }

    public abstract String getRefundAndRescheduleText();

    public String getRefundAndRescheduleTextWebview() {
        driverTools.switchToWebView();
        List<String> refundPolicies = driverTools.getTexts(REFUND_AND_RESCHEDULE_INFO_TEXT);
        String refundPoliciesString = String.join(" ", refundPolicies);
        logger.log("Refund policies: " + refundPoliciesString);
        driverTools.switchToNative();
        return refundPoliciesString;
    }

    public abstract boolean isCloseBottomSheetButtonPresent();

    public boolean isCloseBottomSheetButtonPresentWebview() {
        driverTools.switchToWebView();
        boolean isCloseButtonPresent = driverTools.isElementDisplayed(CLOSE_BUTTON);
        driverTools.switchToNative();
        return isCloseButtonPresent;
    }

    public boolean isWebviewChooseRoomToReschedBtmSheetDislayed() {
        driverTools.switchToWebViewUrl("yourorder");
        boolean isRescheduleBtmSheetDisplayed = driverTools.isElementDisplayed(RESCHEDULE_ROOMS_SELECTION);
        driverTools.switchToNative();
        return isRescheduleBtmSheetDisplayed;
    }

    public void webviewChooseRoomToRescheduleByIndex(int index, String orderId) {
        driverTools.switchToWebViewUrl(orderId);
        driverTools.waitForElementToAppear(RESCHEDULE_ROOMS_SELECTION);
        driverTools.click(RESCHEDULE_ROOMS_SELECTION);
        driverTools.waitForElementToAppear(RESCHEDULE_ROOMS_SELECTION);
        List<WebElement> rooms = driverTools.elements(RESCHEDULE_ROOMS_SELECTION).orElseThrow();
        driverTools.click(rooms.get((index)));
        driverTools.switchToNative();
    }

    public void webviewConfirmRescheduleMo() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.clickWithJavascriptExecutor(CONFIRM_RESCHEDULE_HOTEL_MULTI_ORDER);
        driverTools.switchToNative();
    }

    public void webviewConfirmRescheduleMo(String orderId) {
        driverTools.switchToWebViewUrl(orderId);
        driverTools.clickWithJavascriptExecutor(CONFIRM_RESCHEDULE_HOTEL_MULTI_ORDER);
        driverTools.switchToNative();
    }

    public String getRoomNameWebview(int index) {
        driverTools.switchToWebViewUrl("yourorder");
        Locator roomNameLocator = getRoomLocator(index);
        String roomName = driverTools.getText(roomNameLocator).orElseThrow();
        logger.log("Room name: " + roomName);
        driverTools.switchToNative();
        return roomName;
    }

    private Locator getRoomLocator(int index) {
        return new Locator("room " + index, AppiumBy.xpath("(//*[contains(@class,'styles_room_name')]/span)[" + index + "]"));
    }

    public String getRefundInfoWebview(int index) {
        driverTools.switchToWebViewUrl("yourorder");
        Locator refundPolicyLocator = getRefundInfoLocator(index);
        if (index == 2) {
            driverTools.click(SECOND_ROOM_POLICY_CHEVRON);
        }

        List<String> refundPolicies = driverTools.getTexts(refundPolicyLocator);

        String refundPoliciesString = String.join(" ", refundPolicies);
        logger.log("Refund policies: " + refundPoliciesString);
        driverTools.switchToNative();
        return refundPoliciesString;
    }

    private Locator getRefundInfoLocator(int index) {
        return new Locator("refund info " + index, AppiumBy.xpath("(//*[contains(@data-testid,'room-item-title-text')])[" + index + "]"));
    }

    public String getGuestFullNameHotel() {
        driverTools.swipeToElement(HOTEL_GUEST_NAME, Swipe.UP, 3);
        String result = driverTools.getText(HOTEL_GUEST_NAME).orElseThrow();
        logger.log("Hotel guest full name = " + result);
        return result;
    }

    private Locator getTrainPassengerNameLocator(int index) {
        return new Locator("TRAIN_PASSENGER_" + index, AppiumBy.xpath("(//*[contains(@class,'styles_train_detail_passengers')]/span)[" + index + "]"));
    }

    public String getDepartureDateTrain() {
        driverTools.switchToWebViewUrl("yourorder");
        String result = driverTools.getText(TRAIN_DEPARTURE_TIME).orElseThrow().replace(" - ", ", ");
        logger.log("Train departure time = " + result);
        driverTools.switchToNative();

        return result;
    }

    public String getOriginStationNameTrain() {
        driverTools.switchToWebViewUrl("yourorder");
        String result = driverTools.getText(TRAIN_ORIGIN_STATION).orElseThrow().split(" - ")[0];
        logger.log("Train origin station = " + result);
        driverTools.switchToNative();

        return result;
    }

    public String getDestinationStationNameTrain() {
        driverTools.switchToWebViewUrl("yourorder");
        String result = driverTools.getText(TRAIN_DESTINATION_STATION).orElseThrow().split(" - ")[0];
        logger.log("Train destination station = " + result);
        driverTools.switchToNative();

        return result;
    }

    public String getClassNameTrain() {
        driverTools.switchToWebViewUrl("yourorder");
        String result = driverTools.getText(TRAIN_NAME_CLASS).orElseThrow().split(" - ")[1].split("\\(")[0];
        logger.log("Train class name = " + result);
        driverTools.switchToNative();

        return result;
    }

    public boolean verifyTTDOrderDetailIDComponent() {
        driverTools.switchToWebView();
        driverTools.swipeToElement(TTD_ORDER_DETAIL_ID, Swipe.UP, 3);
        boolean isTTDOrderDetailIDPresent = driverTools.waitForElementToPresence(TTD_ORDER_DETAIL_ID)
                .map(WebElement::getText)
                .map(Objects::nonNull)
                .orElse(false);
        driverTools.switchToNative();
        return isTTDOrderDetailIDPresent;
    }

    public boolean verifyEticketSectionWebView() {
        driverTools.switchToWebViewUrl("yourorder");
        boolean isEticketSectionPresent = driverTools.isElementDisplayed(SEE_ETIKCET_WEBVIEW);
        driverTools.switchToNative();
        return isEticketSectionPresent;
    }

    public boolean verifyEticketQRCode() {
        driverTools.switchToWebView();
        boolean isEticketSectionPresent = driverTools.isElementDisplayed(ETICKET_QRCODE);
        driverTools.switchToNative();
        return isEticketSectionPresent;
    }

    public boolean verifyIsOnOrderDetailPage() {
        driverTools.switchToWebViewUrl("yourorder");
        String urlOrderDetail = driverTools.getCurrentURL();
        Assertion.assertThat("is url contains yourorder", urlOrderDetail, is(containsString("yourorder")));
        driverTools.switchToNative();
        return true;
    }

    public boolean verifyIsOnOrderDetailPageCheck() {
        if(Configs.PLATFORM==Platform.IOS) {
            return true;
        }
        else {
            driverTools.switchToWebViewUrl("yourorder");
            if (driverTools.isElementDisplayed(HIDE_BUTTON)) {
                driverTools.click(HIDE_BUTTON);
            }
            String urlOrderDetail = driverTools.getCurrentURL();
            Assertion.assertThat("is url contains yourorder", urlOrderDetail, is(containsString("yourorder")));
            driverTools.switchToNative();
            return true;
        }
    }
    public boolean matchPriceOfReschedule(String totalPriceBeforeReschedule, String totalPriceReschedule, String totalPriceAfterReschedule) {
        double totalPriceBeforeRescheduleDouble = CoreFunctions.convertPriceSafely(totalPriceBeforeReschedule);
        logger.log("totalPriceBeforeRescheduleDouble: " + totalPriceBeforeRescheduleDouble);
        double totalPriceRescheduleDouble = CoreFunctions.convertPriceSafely(totalPriceReschedule);
        logger.log("totalPriceRescheduleDouble: " + totalPriceRescheduleDouble);
        double totalPriceAfterRescheduleDouble = CoreFunctions.convertPriceSafely(totalPriceAfterReschedule);
        logger.log("totalPriceAfterRescheduleDouble: " + totalPriceAfterRescheduleDouble);

        return totalPriceBeforeRescheduleDouble + totalPriceRescheduleDouble == totalPriceAfterRescheduleDouble;
    }

    //flight check-in order locators
    private static final Locator BOOKING_CODE_ORDER_DETAIL = new Locator("BOOKING_CODE_ORDER_DETAIL", AppiumBy.xpath("//*[contains(@class,'styles_copy_icon')]//*[@data-testid='copy_button']"));
    private static final Locator GET_BOARDING_PASS_BTN = new Locator("GET_BOARDING_PASS_BTN", AppiumBy.xpath("//*[contains(@class,'SingleButton_button')]//*[contains(text(),'Get boarding pass') or contains(text(),'Dapatkan boarding pass')]"));
    //online-checkin page
    private static final Locator COPY_BOOKING_CODE_CHECK_IN_PAGE = new Locator("COPY_BOOKING_CODE_CHECK_IN_PAGE", AppiumBy.xpath("(//button[@data-clipboard-text and string-length(normalize-space(@data-clipboard-text))>0])[1]"));
    private static final Locator CHECK_IN_BTN = new Locator("CHECK_IN_BTN", AppiumBy.xpath("//button[not(@disabled)     and not(contains(@class,'Button_state_disabled'))     and contains(@class,'Button_variant_primary')     and (         .//span[normalize-space(text())='Continue to check-in'] or .//span[normalize-space(text())='Lanjutkan check-in']     )]"));
    /**
     * Direct DOM-attribute carrier of the booking code on the copy button itself
     * ({@code <button data-clipboard-text="FO75XU" ...>Copy</button>}).
     * Reading this attribute is preferred over the click-then-read-clipboard flow
     * because some Appium servers / device farms (notably LambdaTest's UiAutomator2
     * v2.5.4 on Android 14) do not implement the {@code setClipboard}/{@code getClipboard}
     * mobile commands and the legacy flow throws/returns empty there.
     */
    private static final Locator SNACKBAR_TEXT = new Locator("SNACKBAR_TEXT", AppiumBy.xpath("//*[contains(@class,'Snackbar_text_content')]"));
    private static final Locator BOOKING_CODE_COPY_BUTTON_DCT = new Locator("BOOKING_CODE_COPY_BUTTON_DCT", AppiumBy.xpath("(//button[@data-clipboard-text and string-length(normalize-space(@data-clipboard-text))>0])[1]"));

    public String getBookingCodeOrderDetail() {
        driverTools.switchToWebViewUrl("yourorder");
        String bookingCode = readBookingCodeFromCopyButton(BOOKING_CODE_ORDER_DETAIL);
        logger.log("Booking code: " + bookingCode);
        driverTools.switchToNative();
        return bookingCode;
    }

    public void clickCheckInButton() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.waitForElementToPresence(GET_BOARDING_PASS_BTN);
        driverTools.click(GET_BOARDING_PASS_BTN);
        // Click triggers a client-side route change from /yourorder/detail to /flights/online-checkin.
        // The URL change is async (Next.js fetches the new route chunk), so we MUST block until the
        // new URL is observable before returning to native. Without this, the next call
        // (getBookingCodeOnCheckInPage) races the navigation and reads from the stale page or NATIVE_APP.
        // Reuse switchToWebViewUrl as a URL poller with a generous retry budget (~15 attempts ~= 15s+).
        String settledUrl = driverTools.switchToWebViewUrl("online-checkin", 15);
        if (settledUrl == null || settledUrl.isBlank()) {
            logger.softWarning("[clickCheckInButton] online-checkin URL not observed within budget; downstream finds may fail");
        } else {
            logger.log("[clickCheckInButton] navigation settled at: " + settledUrl);
        }
        driverTools.switchToNative();
    }

    public String getBookingCodeOnCheckInPage() {
        // By the time we get here, clickCheckInButton has already confirmed the online-checkin URL is
        // reachable. Re-switching is fast (the right window already shows the right URL) and serves as
        // a defensive guard if any other caller invokes this method directly.
        String settledUrl = driverTools.switchToWebViewUrl("online-checkin", 15);
        if (settledUrl == null || settledUrl.isBlank()) {
            logger.softWarning("[getBookingCodeOnCheckInPage] could not switch to online-checkin webview");
        }
        // Even after the webview context flips, the SSR DOM may be re-hydrating; wait briefly so React
        // doesn't detach/replace the button between findElement attempts.
        driverTools.dummyWait(5);
        String bookingCode = readBookingCodeFromCopyButton(COPY_BOOKING_CODE_CHECK_IN_PAGE);
        logger.log("Booking code: " + bookingCode);
        driverTools.switchToNative();
        return bookingCode;
    }

    /**
     * Read the booking code by preferring the {@code data-clipboard-text} attribute on
     * the copy button (works on every Appium server, no clipboard permission required).
     * Falls back to the legacy clear-clipboard → click → get-clipboard-text flow when
     * the attribute is absent on older builds, so this change is fully backwards-compatible.
     *
     * @param fallbackCopyButton legacy copy-button locator to click when attribute read fails
     * @return booking code, or {@code ""} if neither path produced a value
     */
    private String readBookingCodeFromCopyButton(Locator fallbackCopyButton) {
        // Use a real wait (not a single-shot findElement) so a slow-to-mount DOM doesn't push
        // us into the broken-on-some-farms clipboard fallback. The wait is "safe" (returns
        // Optional.empty instead of throwing) so pages that genuinely lack the attribute
        // still proceed to the legacy clipboard click flow.
        Optional<WebElement> dctButton = driverTools.waitForElementToPresenceSafe(BOOKING_CODE_COPY_BUTTON_DCT, 8);
        if (dctButton.isPresent()) {
            String code = driverTools.getAttribute(dctButton.get(), "data-clipboard-text");
            if (code != null && !code.isBlank()) {
                // Trigger the copy gesture so React-driven side effects fire. On the online-checkin
                // page this gates the "Continue to check-in" CTA: clipboard.js / execCommand('copy')
                // checks event.isTrusted, and Selenium's WebElement.click() produces a SYNTHETIC
                // event (isTrusted=false) which the page treats as "no user gesture" - the CTA
                // stays disabled. clickElementByCoordinate() drives the click via Appium's W3C
                // PointerInput TOUCH sequence, which is delivered as a trusted native-input event
                // and satisfies the gesture check. The clipboard READ is still skipped (we already
                // have the code from the attribute); only the trusted-gesture is needed.
                try {
                    driverTools.clickElementByCoordinate(dctButton.get());
                    logger.log("Tapped copy button via PointerInput.TOUCH (trusted gesture; drives downstream CTA enablement)");
                } catch (Exception clickEx) {
                    logger.softWarning("Trusted tap on copy button failed (continuing): " + clickEx.getMessage());
                }
                logger.log("Read booking code from data-clipboard-text attribute (skipped clipboard flow)");
                return code.trim();
            }
        }
        logger.log("data-clipboard-text not available on this page; falling back to clipboard-copy flow.");
        driverTools.clearClipboard();
        driverTools.click(fallbackCopyButton);
        String clipboardCode = driverTools.getClipboardText();
        return clipboardCode == null ? "" : clipboardCode.trim();
    }

    public void clickContinueToOnlineCheckIn() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("online-checkin");
        driverTools.dummyWait(5);
        driverTools.waitForElementToPresence(CHECK_IN_BTN);
        driverTools.click(CHECK_IN_BTN);
        driverTools.waitForElementToDisappear(CHECK_IN_BTN);
        driverTools.switchToNative();
    }

    public String isOnlineCheckInPage() {
        driverTools.switchToWebViewUrl("identification");
        String url = driverTools.getCurrentURL();
        logger.log("is redirected to different page: " + url);
        driverTools.switchToNative();
        Assertion.assertThat("redirected to some other page", url, not(emptyOrNullString()));
        return url;
    }

    public boolean swipeToStandAloneSection() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.scrollToElement(STANDALONE_SECTION);
        boolean isStandAloneSectionDisplayed = driverTools.isElementDisplayed(STANDALONE_SECTION);
        boolean isAddBtnDisplayed = driverTools.isElementDisplayed(STANDALONE_ADD_BTN);
        driverTools.switchToNative();
        return isStandAloneSectionDisplayed && isAddBtnDisplayed;
    }

    public String getPriceOnStandAloneSection() {
        driverTools.switchToWebViewUrl("yourorder");
        WebElement standAloneSectionPrice = driverTools.swipeToElement(STANDALONE_PRICE_INSURANCE, Swipe.UP, 5).orElseThrow();
        String price = driverTools.getText(standAloneSectionPrice);
        logger.log("price on standalone section: " + price);
        driverTools.switchToNative();
        return price;
    }

    public void clickAddOnStandAloneSection() {
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.scrollToElement(STANDALONE_SECTION);
        driverTools.clickWithJavascriptExecutor(STANDALONE_ADD_BTN);
        driverTools.switchToNative();
    }

    public void switchToNative() {
        driverTools.switchToNative();
    }

    public boolean verifyEditGuestDetailDisabled() {
        driverTools.switchToWebViewUrl("yourorder");
        boolean notVisible = !driverTools.isElementDisplayed(EDIT_BOOKING_BUTTON);
        driverTools.switchToNative();
        return notVisible;
    }

    public abstract void clickRefund();

    public abstract void clickLoginNow();

    public abstract String getBookingCode();

    public abstract boolean verifyImportantInfo();

    public abstract boolean verifyDepartureTrainDetails(TrainData data);

    public abstract boolean verifyPassengerAndFacilities(TrainData data);

    public abstract boolean verifyHelpCenter();

    public abstract boolean verifyContactCustomerCare();

    public abstract String getOrderId();
}
