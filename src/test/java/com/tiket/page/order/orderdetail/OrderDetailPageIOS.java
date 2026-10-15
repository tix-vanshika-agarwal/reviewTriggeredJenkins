package com.tiket.page.order.orderdetail;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.refund.RefundPage;
import com.tiket.pojo.train.TrainData;
import com.tiket.testbase.Assertion;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.tiket.testbase.Configs;
import com.tiket.model.Environment;
import java.util.stream.Stream;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.page.accommodation.hotel.landingpage.LandingPageIOS.OK_POPUP;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import org.hamcrest.core.Every;

public class OrderDetailPageIOS<T extends WebDriver> extends OrderDetailPage<T> {
    public OrderDetailPageIOS(Session<T> session) {
        super(session);
    }

    private static final Locator NEXT_BUTTON = new Locator("NEXT_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='Next' or @label='Next' or @name='Berikutnya' or @label='Berikutnya']"));
    private static final Locator DONE_BUTTON = new Locator("DONE_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='Done' or @label='Done' or @name='Selesai' or @label='Selesai']"));
    private static final Locator BACK_BUTTON_CC = new Locator("BACK_BUTTON_CC", AppiumBy.xpath("//XCUIElementTypeButton[@name='back' or @label='back' or @name='Kembali' or @label='Kembali']"));
    private static final Locator IMP_INFO_BACK_BUTTON = new Locator("IMP_INFO_BACK_BUTTON", AppiumBy.xpath("//*[@name='back' and @type='XCUIElementTypeButton']"));
    private static final Locator ETICKET_LABEL = new Locator("ETICKET_LABEL", AppiumBy.xpath("//*[@label='View' or @label='Lihat']//preceding-sibling::XCUIElementTypeStaticText|//*[contains(@name,'ic-information-general')]//following-sibling::XCUIElementTypeStaticText"));
    private static final Locator SEE_ETICKET_BUTTON = new Locator("SEE_ETICKET_BUTTON", AppiumBy.xpath("//*[@label='View' or @label='Lihat' or @label='See']"));
    private static final Locator MANAGE_ORDER_SEE_ETICKET = new Locator("MANAGE_ORDER_SEE_ETICKET", AppiumBy.xpath("//XCUIElementTypeButton[following-sibling::XCUIElementTypeImage[@name='ic-chevron-right'] and following-sibling::*[contains(@label,'E-tike') or contains(@label,'E-ticket')]]"));
    private static final Locator PDF_CONTENT = new Locator("PDF_CONTENT", AppiumBy.xpath("//XCUIElementTypeOther[@name='E-voucher' or @name='E-ticket' or @label='E-voucher' or @label='E-ticket'] | //XCUIElementTypeTextView[contains(@label,'tiket') or contains(@value,'Order ID')]"));
    private static final Locator PDF_CONTENT_TEXTS = new Locator("PDF_CONTENT_TEXTS", AppiumBy.xpath("//XCUIElementTypeOther[@name='E-voucher' or @name='E-ticket' or @label='E-voucher' or @label='E-ticket']//XCUIElementTypeStaticText"));
    private static final Locator ORDER_ID_PDF = new Locator("ORDER_ID_PDF", AppiumBy.xpath("//*[contains(@label,'Order ID')]"));
    private static final Locator PICKUP = new Locator("PICKUP", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='Place'])[1]"));
    private static final Locator DROPOFF = new Locator("DROPOFF", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='Place'])[2]"));
    private static final Locator RESCHEDULE_BUTTON = new Locator("RESCHEDULE_BUTTON", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Reschedule']/preceding-sibling::XCUIElementTypeButton[@visible='true']"));
    private static final Locator REFUND_RESCHEDULE_DELETE_ORDER_BUTTON = new Locator("REFUND_RESCHEDULE_DELETE_ORDER_BUTTON", AppiumBy.name("Cancel Order Button"));
    private static final Locator CANCEL_ORDER_BUTTON_NATIVE = new Locator("CANCEL_ORDER_BUTTON_NATIVE", AppiumBy.xpath("//*[@name='Cancel Order Button' or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='cancel order' or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='batalkan pesanan']"));
    private static final Locator DELETE_ORDER_BUTTON_NATIVE = new Locator("DELETE_ORDER_BUTTON_NATIVE", AppiumBy.xpath("//*[contains(@label,'Delete order') or contains(@label,'Hapus pesanan') or @name='Delete Order Button' or @label='Delete' or @label='Hapus']"));
    private static final Locator MAKE_SIMILAR_ORDER_BUTTON_NATIVE = new Locator("MAKE_SIMILAR_ORDER_BUTTON_NATIVE", AppiumBy.xpath("//*[contains(@label,'Make similar order') or contains(@label,'Make Similar Order') or contains(@label,'Buat pesanan serupa') or contains(@label,'Buat Pesanan Serupa') or @name='Make Similar Order Button']"));
    private static final Locator CANCEL_ORDER_CHECKBOX_NATIVE = new Locator("CANCEL_ORDER_CHECKBOX_NATIVE", AppiumBy.xpath("//*[@type='XCUIElementTypeOther' and @height>0 and ./parent::*[@type='XCUIElementTypeOther' and ((./preceding-sibling::* | ./following-sibling::*)[contains(@name,'E-ti') or contains(@name,'voucher') or contains(@name,'Tiket Points')]) and ./parent::*[@type='XCUIElementTypeOther']]]"));
    private static final Locator CANCEL_ORDER_AGREE_BUTTON_NATIVE = new Locator("CANCEL_ORDER_AGREE_BUTTON_NATIVE", AppiumBy.xpath("//*[translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='setuju' or translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='agree' and @type='XCUIElementTypeButton']"));
    private static final Locator SUBMIT_CANCEL_ORDER = new Locator("SUBMIT_CANCEL_ORDER", AppiumBy.xpath("//*[@name='alert']//following-sibling::*//XCUIElementTypeButton[translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='cancel order' or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='batalin pesanan']"));
    public static final Locator ORDER_ID = new Locator("ORDER_ID", AppiumBy.xpath("//*[@name='order_id']"));
    public static final Locator ORDER_ID_XPATH = new Locator("ORDER_ID", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'Order ID ')]"));
    // New iOS UI surfaces the per-itinerary order detail id (e.g. 1900742246) in a
    // dedicated static-text element named "itinerary_id_number"; the parent order id
    // is not rendered natively on this screen, so tests that need to verify the
    // displayed identifier compare against this value instead.
    public static final Locator ORDER_DETAIL_ID_VALUE = new Locator("ORDER_DETAIL_ID_VALUE", AppiumBy.name("itinerary_id_number"));
    private static final Locator RESCHEDULE_CARD = new Locator("RESCHEDULE_CARD", AppiumBy.xpath("(//*[(contains(@label,'previous order') or contains(@label,'pesanan sebelumnya')) and @visible='true'])[1]"));
    private static final Locator NEW_SCHEDULE_BUTTON = new Locator("NEW_SCHEDULE_BUTTON", AppiumBy.xpath("(//*[(contains(@label,'New Schedule') or contains(@label,'New schedule') or contains(@label,'jadwal baru') or contains(@label,'Jadwal baru')) and @visible='true'])[1]"));
    private static final Locator CHECKIN_DATE = new Locator("CHECKIN_DATE", AppiumBy.name("Check-in date"));
    private static final Locator CHECKOUT_DATE = new Locator("CHECKOUT_DATE", AppiumBy.name("Check-out date"));
    private static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", AppiumBy.name("Hotel Name"));
    private static final Locator ORDER_DETAIL_NAME = new Locator("ORDER_DETAIL_NAME", AppiumBy.name("Room Name"));
    private static final Locator LANG_POPUP_ENGLISH = new Locator("LANG_POPUP_ENGLISH", AppiumBy.xpath("//*[@name='eng_lang_button' and @label='English' and @type='XCUIElementTypeButton']"));
    private static final Locator LANG_POPUP_INDO = new Locator("LANG_POPUP_INDO", AppiumBy.xpath("//*[@type='XCUIElementTypeOther' and ./*[@name='indo_lang_button']]"));
    private static final Locator SKIP_INFORMATION_BUTTON = new Locator("SKIP_INFORMATION_BUTTON", AppiumBy.xpath("//XCUIElementTypeStaticText[@value='Lewati' or @value='Skip']"));
    private static final Locator SKIP_OK_BUTTON = new Locator("SKIP_OK_BUTTON", AppiumBy.name("skip_button"));
    private static final Locator EDIT_BOOKING_BUTTON = new Locator("EDIT_BOOKING_BUTTON", AppiumBy.xpath("//*[contains(@label,'Guest') or contains(@label,'Tamu')]//following-sibling::*[@label='Edit' or @label='Ubah']"));
    private static final Locator EDIT_GUEST_NAME_POPUP = new Locator("EDIT_GUEST_NAME_POPUP", AppiumBy.xpath("//XCUIElementTypeImage[@name='icRightArrow']/preceding-sibling::XCUIElementTypeStaticText[contains(@label,'Guest Name') or contains(@label,'Nama Tamu')]/preceding-sibling::XCUIElementTypeButton"));
    private static final Locator TOTAL_ADULT = new Locator("TOTAL_ADULT", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Room Name' and (contains(@value, 'tamu ') or contains(@value, 'guest '))]"));
    private static final Locator TOTAL_NIGHT = new Locator("TOTAL_NIGHT", AppiumBy.id("Number of nights"));
    private static final Locator TOTAL_ROOM = new Locator("TOTAL_ROOM", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Room Name' and contains(@value, ' x ')]"));
    private static final Locator GUEST_FULL_NAME = new Locator("GUEST_FULL_NAME", AppiumBy.id("Guest Name"));
    private static final Locator GUEST_FULL_NAME_XPATH = new Locator("GUEST_FULL_NAME", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Guest Name' and @visible='true']"));
    private static final Locator GUEST_DETAIL_FULL_NAME = new Locator("GUEST_DETAIL_FULL_NAME", AppiumBy.xpath("((//XCUIElementTypeStaticText[@label='Contact Info' or @label='Info Pemesan'])[last()]//following::XCUIElementTypeStaticText[@label])[1]"));
    private static final Locator CLOSE_GUEST_DETAIL = new Locator("CLOSE_GUEST_DETAIL", AppiumBy.name("ic cancel grey"));
    private static final Locator PRODUCT_NAME = new Locator("PRODUCT_NAME", AppiumBy.id("product_name"));
    private static final Locator MAIN_ETICKET = new Locator("MAIN_ETICKET", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@value,'E-tiketmu') or contains(@value,'e-ticket')]"));
    private static final Locator ETICKET_BUTTON = new Locator("ETICKET_BUTTON", AppiumBy.accessibilityId("see_eticket_button"));
    private static final Locator TOTAL_PAYMENT = new Locator("TOTAL_PAYMENT", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Total Payment' and not(@label='Total Payment')]"));
    private static final Locator RESCHEDULE_BANNER = new Locator("RESCHEDULE_BANNER", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Di-reschedule') or contains(@label,'Rescheduled') or contains(@label,'di-reschedule') or contains(@label,'rescheduled')]"));
    private static final Locator ORDER_DETAIL_HEADER_TITLE = new Locator("ORDER_DETAIL_HEADER_TITLE", AppiumBy.accessibilityId("product_name"));
    private static final Locator CAR_RENTAL_LOCATION_TEXT = new Locator("CAR_RENTAL_LOCATION_TEXT", AppiumBy.xpath("//XCUIElementTypeOther[3]//XCUIElementTypeOther//XCUIElementTypeOther[1]//XCUIElementTypeStaticText[2]"));
    private static final Locator CAR_RENTAL_DRIVER_OPTION_TEXT = new Locator("CAR_RENTAL_DRIVER_OPTION_TEXT", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@value, 'with driver') or contains(@value, 'dengan sopir')]"));
    private static final Locator CAR_RENTAL_PICKUP_TIME_TEXT = new Locator("CAR_RENTAL_PICKUP_TIME_TEXT", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@value, ' - ')]"));
    private static final Locator CAR_RENTAL_PASSENGER_FULL_NAME_TEXT = new Locator("CAR_RENTAL_PASSENGER_FULL_NAME_TEXT", AppiumBy.xpath("//XCUIElementTypeOther[7]//XCUIElementTypeStaticText[1][not(contains(@value, 'Detail'))]"));
    private static final Locator CAR_RENTAL_PASSENGER_PHONE_NUMBER_TEXT = new Locator("CAR_RENTAL_PASSENGER_PHONE_NUMBER_TEXT", AppiumBy.xpath("//XCUIElementTypeOther[7]//XCUIElementTypeStaticText[2]"));
    private static final Locator REFUND_AND_RESCHEDULE_INFO = new Locator("REFUND_AND_RESCHEDULE_INFO", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Refund & Reschedule')]"));
    private static final Locator REFUND_AND_RESCHEDULE_INFO_TEXT = new Locator("REFUND_AND_RESCHEDULE_INFO_TEXT", AppiumBy.xpath("//XCUIElementTypeButton[@name='ic cancel grey']//following::XCUIElementTypeStaticText[@label]"));
    private static final Locator REFUND_AND_RESCHEDULE_DETAIL_BUTTON = new Locator("REFUND_AND_RESCHEDULE_DETAIL_BUTTON", AppiumBy.name("Detail Button"));
    private static final Locator REFUND_VALUE = new Locator("REFUND_VALUE", AppiumBy.xpath("(//XCUIElementTypeStaticText[translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='biaya dikenakan' or translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='charge fee' or translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='policy' or translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='kebijakan']/parent::*/parent::*/following-sibling::*//XCUIElementTypeStaticText[contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'idr') or contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'free') or contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'gratis') or contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '%')])[1]"));
    private static final Locator REFUND_AND_RESCHEDULE_CLOSE_BUTTON = new Locator("REFUND_AND_RESCHEDULE_CLOSE_BUTTON", AppiumBy.xpath("//*[@name='back']"));
    private static final Locator REFUND_BUTTON = new Locator("REFUND_BUTTON", AppiumBy.xpath("//*[@label='Refund']//preceding-sibling::XCUIElementTypeButton"));
    private static final Locator START_REFUND_BUTTON = new Locator("START_REFUND_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'Mulai') or contains(@label,'Start')]"));
    // Native iOS "Enjoying TiketPreprod? Tap a star to rate it on the App Store." popup.
    // It can fire after deeplink navigation lands on order detail (the home-page dismiss
    // does not always cover this entry path). Same locator used by HomePageIOS.
    private static final Locator CLOSE_GIVE_RATING_POPUP = new Locator("CLOSE_GIVE_RATING_POPUP", AppiumBy.xpath("//XCUIElementTypeButton[@label=\"Not Now\"]"));
    private static final Locator TTD_TOTAL_PAYMENT = new Locator("TTD_TOTAL_PAYMENT", AppiumBy.xpath("//*[@name='Total Payment' and not(@label='Total Payment')]"));
    private static final Locator TOTAL_PRICE_DETAILS = new Locator("TOTAL_PRICE_DETAILS", AppiumBy.xpath("//*[@label='Total']/parent::*/following-sibling::*/XCUIElementTypeStaticText"));
    private static final Locator TTD_PRODUCT_TITLE = new Locator("TTD_PRODUCT_TITLE", AppiumBy.id("TTD Name"));
    private static final Locator TTD_VALIDITY_PERIOD = new Locator("TTD_VALIDITY_PERIOD", AppiumBy.xpath("//*[@label='Validity Period' or @label='Masa Berlaku']//following-sibling::*//*[@name='Unidentified']"));
    private static final Locator GUEST_NAME_EDIT_PAGE = new Locator("GUEST_NAME_EDIT_PAGE", AppiumBy.xpath("//XCUIElementTypeImage[@name='icEdit']/preceding-sibling::XCUIElementTypeStaticText"));
    private static final Locator EDIT_GUEST_NAME_EDIT_PAGE = new Locator("EDIT_GUEST_NAME_EDIT_PAGE", AppiumBy.xpath("//XCUIElementTypeImage[@name='icEdit']"));
    private static final Locator GUEST_NAME_TEXTFIELD_EDIT_PAGE = new Locator("EDIT_GUEST_NAME_EDIT_PAGE", AppiumBy.accessibilityId("fullName"));
    private static final Locator SAVE_BUTTON_FORM_EDIT_PAGE = new Locator("SAVE_BUTTON_FORM_EDIT_PAGE", AppiumBy.xpath("(//XCUIElementTypeButton[@label='SAVE' or @label='SIMPAN'])[2]"));
    private static final Locator SAVE_BUTTON_EDIT_PAGE = new Locator("SAVE_BUTTON_EDIT_PAGE", AppiumBy.xpath("(//XCUIElementTypeButton[@label='SAVE' or @label='SIMPAN'])[1]"));
    private static final Locator SAVE_ALERT_POPUP_EDIT_PAGE = new Locator("SAVE_ALERT_POPUP_EDIT_PAGE", AppiumBy.xpath("//XCUIElementTypeButton[@name='alertAction1']"));
    private static final Locator GUEST_NAME_TRAIN_PASSENGER_DETAIL = new Locator("GUEST_NAME_TRAIN_PASSENGER_DETAIL", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@name,'1. ')])[last()]"));
    private static final Locator GUEST_AND_PASSENGER_DETAIL_BTN = new Locator("GUEST_AND_PASSENGER_DETAIL_BTN", AppiumBy.xpath("(//*[contains(@label,'Passengers & Facilities') or contains(@label,'Penumpang & Fasilitas')]//parent::*//parent::*)[1]//following-sibling::*//XCUIElementTypeButton[@label='Lihat semua' or @label='See all']"));
    private static final Locator ARROW_TRAIN_PASSENGER_DETAIL = new Locator("ARROW_TRAIN_PASSENGER_DETAIL", AppiumBy.accessibilityId("icArrowDownUp"));
    private static final Locator SEAT_TRAIN_PASSENGER_DETAIL = new Locator("SEAT_TRAIN_PASSENGER_DETAIL", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'Seat') or contains(@name,'Kursi')]//following-sibling::*//XCUIElementTypeStaticText"));
    private static final Locator TOTAL_PAYMENT_TRAIN = new Locator("TOTAL_PAYMENT_TRAIN", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Total Payment']"));
    private static final Locator REFUND_DETAIL_BUTTON = new Locator("REFUND_DETAIL_BUTTON", AppiumBy.xpath("//*[translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='refund']/parent::*/following-sibling::*[contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'detail')]"));
    private static final Locator DATE_TIME_TEXT = new Locator("DATE_TIME_TEXT", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@label,'•') and contains(@label,'-') and contains(@label,':')])[last()]"));
    private static final Locator CHAT_BTN = new Locator("CHAT_BTN", AppiumBy.id("chat_now button"));
    private static final Locator CANT_RESCHEDULE_POPUP = new Locator("CANT_RESCHEDULE_POPUP", AppiumBy.xpath("//XCUIElementTypeAlert//*[contains(@label,'Tidak bisa reschedule') or contains(@label,'Reschedule not allowed')]"));
    private static final Locator OK_BUTTON = new Locator("OK_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Oke') or contains(@name,'OK')]"));
    private static final Locator REFUND_BANNER_INFO = new Locator("REFUND_BANNER_INFO", AppiumBy.xpath("(//*[contains(@label,'Detail')]//preceding-sibling::*[contains(@label,'Refund')]//parent::XCUIElementTypeOther)[1]"));
    private static final Locator REFUND_NOT_AVAILABLE = new Locator("REFUND_NOT_AVAILABLE", AppiumBy.xpath("(//*[@label='Refund is not available' or @label='Refund tidak tersedia' or @label='Order Cannot Be Refunded' or @label='Order Tidak Dapat Di Refund'])[last()]"));
    private static final Locator SEE_MORE_DETAILS_BUTTON = new Locator("SEE_MORE_DETAILS_BUTTON", AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label == 'See room details' OR label == 'Lihat detail kamar'`]"));
    private static final Locator GUEST_DETAILS_PAGE = new Locator("GUEST_DETAILS_PAGE", AppiumBy.xpath("//*[@label='Detail Tamu' or @label='Guest Details']"));
    private static final Locator FULL_NAME_FIELD = new Locator("FULL_NAME_FIELD", AppiumBy.className("XCUIElementTypeTextField"));
    private static final Locator MR_SALUTATION_CHECKBOX = new Locator("MR_SALUTATION_CHECKBOX", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Mr.' or @label='Tuan']"));
    private static final Locator MS_SALUTATION_CHECKBOX = new Locator("MS_SALUTATION_CHECKBOX", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Ms.' or @label='Nona']"));
    private static final Locator MRS_SALUTATION_CHECKBOX = new Locator("MRS_SALUTATION_CHECKBOX", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Mrs.' or @label='Nyonya']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@label='Save' or @label='Simpan' or @label='SAVE' or @label='SIMPAN']"));
    private static final Locator SUBMIT_CONFIRM = new Locator("SUBMIT_CONFIRM", AppiumBy.xpath("//XCUIElementTypeButton[@label='Ya' or @label='Yes']"));
    private static final Locator VERIFY_PHONE_NUMBER_FIELD = new Locator("VERIFY_PHONE_NUMBER_FIELD", AppiumBy.xpath("//*[contains(@label,'OTP')]//parent::*//following-sibling::XCUIElementTypeTextField[not(contains(@value,'kode OTP baru') or contains(@value,'new OTP code'))]"));
    private static final Locator SEE_RESERVATION_DETAIL_BUTTON = new Locator("SEE_RESERVATION_DETAIL_BUTTON", AppiumBy.iOSClassChain("**/*[`label == 'Lihat detail reservasi' OR label == 'See reservation details'`]"));
    private static final Locator BUS_DEPARTURE_NAME = new Locator("BUS_DEPARTURE_NAME", AppiumBy.xpath("(//XCUIElementTypeImage/following-sibling::XCUIElementTypeStaticText)[1]"));
    private static final Locator BUS_DEPARTURE_DATE = new Locator("BUS_DEPARTURE_DATE", AppiumBy.xpath("((//XCUIElementTypeImage/following-sibling::XCUIElementTypeOther)[1]/XCUIElementTypeOther[1]//XCUIElementTypeStaticText)[1]"));
    private static final Locator BUS_RETURN_DATE = new Locator("BUS_RETURN_DATE", AppiumBy.xpath("((//XCUIElementTypeImage/following-sibling::XCUIElementTypeOther)[1]/XCUIElementTypeOther[1]//XCUIElementTypeStaticText)[1]"));
    private static final Locator BUS_FACILITY = new Locator("BUS_FACILITY", AppiumBy.name("Unidentified"));
    private static final Locator BUS_PASSENGER_DETAIL = new Locator("BUS_PASSENGER_DETAIL", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label, 'Mr.') or contains(@label, 'Mrs') or contains(@label, 'Ms') or contains(@label, 'Tuan') or contains(@label, 'Nyonya') or contains(@label, 'Nona')]"));
    private static final Locator BUS_SEAT_NO_DETAIL = new Locator("BUS_SEAT_NO_DETAIL", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Kursi') or contains(@label,'Seat')]"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("//XCUIElementTypeOther[@name='navigation']/XCUIElementTypeOther/XCUIElementTypeOther[1]/XCUIElementTypeButton"));
    private static final Locator MAIN_ETICKET_NATIVE = new Locator("MAIN_ETICKET_NATIVE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label, \"Your e-ticket is here\") or contains(@label, \"E-tiketmu\")]"));
    private static final Locator FLIGHT_DEPARTURE_DATE = new Locator("FLIGHT_DEPARTURE_DATE", AppiumBy.xpath("//*[contains(@class,'styles_action_header_container') and div[contains(text(),'Depart') or contains(text(),'Pergi')]]//following-sibling::*[contains(@class, 'content_list')][1]//span"));
    private static final Locator FLIGHT_DEPARTURE_DATE_NATIVE = new Locator("FLIGHT_DEPARTURE_DATE_NATIVE", AppiumBy.xpath("(//*[contains(@label,':') and contains(@label,',')])[1]"));
    private static final Locator BUS_SEE_TRIP_DETAIL_BTN = new Locator("BUS_SEE_TRIP_DETAIL_BTN", AppiumBy.xpath("//*[(@label='Lihat detail perjalanan' or @label='See trip details') and @visible='true']"));
    private static final Locator BUS_DEPARTURE_NAME_TRIP_DETAIL = new Locator("BUS_DEPARTURE_NAME_TRIP_DETAIL", AppiumBy.xpath("(//*[@label='Trip Details' or @label='Detail Perjalanan'])[last()]"));
    private static final Locator BUS_DEPARTURE_LOCATIONS_TRIP_DETAIL = new Locator("BUS_DEPARTURE_LOCATIONS_TRIP_DETAIL", AppiumBy.xpath("//*[@label='Route' or @label='Rute']/following::XCUIElementTypeOther[1]/XCUIElementTypeOther/XCUIElementTypeStaticText[1]"));
    private static final Locator BUS_FACILITIES_TRIP_DETAIL = new Locator("BUS_FACILITIES_TRIP_DETAIL", AppiumBy.xpath("(//*[@label='Facilities' or @label='Fasilitas']/following-sibling::XCUIElementTypeOther)[last()]//XCUIElementTypeStaticText"));
    private static final Locator REFUND_STATUS_IN_PROCESS = new Locator("REFUND_STATUS_IN_PROCESS", AppiumBy.xpath("//*[contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'in process') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'diproses')]"));
    private static final Locator FLIGHT_RETURN_DATE = new Locator("FLIGHT_RETURN_DATE", AppiumBy.xpath("//*[contains(@class,'styles_action_header_container') and div[contains(text(),'Return') or contains(text(),'Pulang')]]//following-sibling::*[contains(@class, 'content_list')][1]//span"));
    private static final Locator PRICE_DETAIL_CHEVRON = new Locator("PRICE_DETAIL_CHEVRON", AppiumBy.xpath("//*[(@name='Expand Button' or @name='ic chevron') and @visible='true']"));
    private static final Locator PRICE_DETAIL_TEXTS = new Locator("PRICE_DETAIL_TEXTS", AppiumBy.xpath("(//XCUIElementTypeScrollView)[2]//XCUIElementTypeStaticText"));
    private static final Locator BOTTOMSHEET_CLOSE_BUTTON = new Locator("BOTTOMSHEET_CLOSE_BUTTON", AppiumBy.name("ic cancel grey"));
    private static final Locator RECEIPT_BUTTON = new Locator("RECEIPT_BUTTON", AppiumBy.xpath("//XCUIElementTypeOther[./XCUIElementTypeImage and ./*[contains(@label,'Bukti') or contains(@label,'Receipt')]]/XCUIElementTypeButton[@visible='true']"));
    private static final Locator NATIVE_SHARE_POPUP = new Locator("NATIVE_SHARE_POPUP", AppiumBy.xpath("//XCUIElementTypeButton[@label='Close']"));
    private static final Locator SHARE_RECEIPT_BUTTON = new Locator("SHARE_RECEIPT_BUTTON", AppiumBy.xpath("//*[./XCUIElementTypeStaticText[@name='Total Payment' and not(@label='Total Payment')]]/*[contains(@label,'Bagikan') or contains(@label,'Share')]"));
    private static final Locator OPEN_CHANGE_SEAT_SELECTION = new Locator("OPEN_CHANGE_SEAT_SELECTION", AppiumBy.xpath("//XCUIElementTypeButton[@name='CHANGE' or @name='UBAH']"));
    private static final Locator TRAIN_EMPTY_SEATS_IN_CHANGE_SEAT_SELECTION = new Locator("TRAIN_EMPTY_SEATS_IN_CHANGE_SEAT_SELECTION", AppiumBy.xpath("//XCUIElementTypeOther[@name='seat_available']"));
    private static final Locator CONFIRM_NEW_TRAIN_SEAT = new Locator("CONFIRM_NEW_TRAIN_SEAT", AppiumBy.xpath("//XCUIElementTypeButton[@name='Save' or @name='Simpan']"));
    private static final Locator CLOSE_TRAIN_PASSENGER_DETAIL = new Locator("CLOSE_TRAIN_PASSENGER_DETAIL", AppiumBy.accessibilityId("ic cancel grey"));
    private static final Locator CHANGE_TRAIN_SEAT_BTN = new Locator("CHANGE_TRAIN_SEAT_BTN", AppiumBy.xpath("//*[*[*[contains(@label,'Ganti Nomor Kursi') or contains(@label,'Change Passenger Seat')]]]"));
    private static final Locator TRAIN_SEAT_FROM_CHANGE_TRAIN_SEAT_MENU = new Locator("TRAIN_SEAT_FROM_CHANGE_TRAIN_SEAT_MENU", AppiumBy.xpath("//XCUIElementTypeCollectionView//XCUIElementTypeStaticText"));
    private static final Locator CONTINUE_TO_PAYMENT_FROM_CHANGE_SEAT = new Locator("CONTINUE_TO_PAYMENT_FROM_CHANGE_SEAT", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'LANJUTKAN') or contains(@label,'CONTINUE')]"));
    private static final Locator CONFIRM_CONTINUE_PAYMENT_TRAIN_CHANGE_SEAT = new Locator("CONFIRM_CONTINUE_PAYMENT_TRAIN_CHANGE_SEAT", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'Ya, lanjutkan') or contains(@label,'Yes, continue')]"));
    private static final Locator TRAIN_SEAT_SELECTION_CONTAINER = new Locator("TRAIN_SEAT_SELECTION_CONTAINER", AppiumBy.xpath("//XCUIElementTypeOther[XCUIElementTypePageIndicator]"));
    private static final Locator SHARE_ETICKET_FROM_HEADER = new Locator("SHARE_ETICKET_FROM_HEADER", AppiumBy.xpath("(//*[@name='back']/following-sibling::*//XCUIElementTypeOther)[last()-1]"));
    private static final Locator CHANGE_LANGUAGE_CONTAINER = new Locator("CHANGE_LANGUAGE_CONTAINER", AppiumBy.xpath("//*[contains(@class,'ETicket_language_container')]"));
    private static final Locator CLOSE_ETICKE_BTM_SHEET_WEBVIEW = new Locator("CLOSE_ETICKE_BTM_SHEET", AppiumBy.xpath("//header[contains(@class,'BottomSheet_header')]//*[@aria-label='Close' or @aria-label='Tutup']"));
    private static final Locator CLOSE_NATIVE_SHARE_BTM_SHEET = new Locator("CLOSE_NATIVE_SHARE_BTM_SHEET", AppiumBy.xpath("//XCUIElementTypeButton[@name='Tutup' or @name='Close']"));
    private static final Locator PRICE_DETAIL_TICKET_ONLY = new Locator("PRICE_DETAIL_TICKET_ONLY", AppiumBy.xpath("//XCUIElementTypeImage[@name='icArrowDownUp']/parent::*/parent::*/XCUIElementTypeOther[2]//*[contains(@label,'IDR')]"));
    private static final Locator CLOSE_PRICE_DETAIL = new Locator("CLOSE_PRICE_DETAIL", AppiumBy.accessibilityId("ic cancel grey"));
    private static final Locator ROOM_NAME_INFO = new Locator("ROOM_NAME_INFO", AppiumBy.name("Room Name"));
    private static final Locator ROOM_INFO = new Locator("ROOM_INFO", AppiumBy.xpath("//*[@name='Room Name' and contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'guest') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'tamu')]"));
    private static final Locator REFUND_RESCHEDULE_TAB_BUTTON = new Locator("REFUND_RESCHEDULE_TAB_BUTTON", AppiumBy.xpath("(//*[@label='Refund & Reschedule'])[last()]"));
    private static final Locator REFUND_REQUEST_BUTTON = new Locator("REFUND_REQUEST_BUTTON", AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label == 'Ajukan refund' OR label == 'Request refund'`]"));
    private static final Locator ROOM_TYPE = new Locator("ROOM_TYPE", AppiumBy.iOSClassChain("**/*[`name == 'labelText'`]/XCUIElementTypeStaticText"));
    private static final Locator REFUND_HEADER = new Locator("REFUND_HEADER", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Refund') and (contains(@label,'passenger') or contains(@label,'penumpang'))]"));
    private static final Locator PAYMENT_RECEIPT = new Locator("PAYMENT_RECEIPT", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name, 'Bukti Pembayaran') or contains(@name, 'Receipt')]"));
    private static final Locator ADD_TO_CALENDAR = new Locator("ADD_TO_CALENDAR", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name, 'Tambahkan ke Kalender') or contains(@name, 'Add To Calendar')]"));
    //AT Order detail
    private static final Locator AT_CAR_TYPE_NAME = new Locator("AT_CAR_TYPE_NAME", AppiumBy.xpath("//*[@name='Car Transfer Name']"));
    private static final Locator AT_CAR_DRIVE_CONTACT = new Locator("AT_DRIVER_CONTACT", AppiumBy.xpath("//*[@name='Contact vendor' or @name='Hubungi vendor']"));
    private static final Locator AT_TRIP_DETAIL_ENTRY = new Locator("AT_TRIP_DETAIL_ENTRY", AppiumBy.accessibilityId("Details"));
    private static final Locator AT_FACILITIES_DETAIL_ENTRY = new Locator("AT_FACILITIES_DETAIL_ENTRY", AppiumBy.xpath("//*[@name='Facilities']//following-sibling::*[@name='Details']"));
    private static final Locator AT_TRIP_DETAIL_PICKUP = new Locator("AT_TRIP_DETAIL_PICKUP", AppiumBy.xpath("(//*[contains(@name,'Header') and (contains(@label,'Titik penjemputan') or contains(@label,'Pickup point'))]//following-sibling::*//*[contains(@name,'Place')])[1]"));
    private static final Locator AT_TRIP_DETAIL_DESTINATION = new Locator("AT_TRIP_DETAIL_DESTINATION", AppiumBy.xpath("(//*[contains(@name,'Header') and (contains(@label,'Titik tujuan') or contains(@label,'Drop-off point'))]//following-sibling::*//*[contains(@name,'Place')])[1]"));
    private static final Locator AT_FACILITIES_COMPONENT = new Locator("AT_FACILITIES_COMPONENT", AppiumBy.xpath("//*[*[@name='ic cancel grey']]/..//following-sibling::*//XCUIElementTypeScrollView//XCUIElementTypeStaticText"));
    private static final Locator AT_CONTACT_DETAIL = new Locator("AT_CONTACT_DETAIL", AppiumBy.name("contact_name"));
    private static final Locator AT_CONTACT_DETAIL_PHONE_NUMBER = new Locator("AT_CONTACT_DETAIL_PHONE_NUMBER", AppiumBy.name("contact_phone"));
    private static final Locator AT_CLOSE_DETAIL = new Locator("AT_CLOSE_DETAIL", AppiumBy.name("ic cancel grey"));
    private static final Locator REFUND_HOTEL_STATUS = new Locator("REFUND_HOTEL_STATUS", AppiumBy.xpath("//*[contains(@label,'Refund hotel') or contains(@label,'Refunding hotel')]"));

    //ferry order detail
    private static final Locator BARCODE_FERRY = new Locator("BARCODE_FERRY", AppiumBy.xpath("//*[@type='XCUIElementTypeImage' and ./following-sibling::*[@name='Image Title']]"));
    private static final Locator VIEW_ETICKET_BTMSHEET = new Locator("VIEW_ETICKET_BTMSHEET", AppiumBy.xpath("//*[contains(@name, 'E-tiket Ferry (Express)') or contains(@name, 'E-ticket Ferry (Express)')and @type='XCUIElementTypeOther']"));
    private static final Locator CLOSE_BTMSHEET = new Locator("CLOSE_BTMSHEET", AppiumBy.xpath("//*[@name='back' and @type='XCUIElementTypeButton']"));
    private static final Locator SHARE_RECIPT_ = new Locator("SHARE_RECIPT", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Share receipt' or @name='Bagikan bukti bayar']"));
    //Car rental order detail
    private static final Locator CAR_RENTAL_PRODUCT_NAME = new Locator("CAR_RENTAL_PRODUCT_NAME", AppiumBy.accessibilityId("Car Rental Name"));
    private static final Locator CAR_RENTAL_RENTAL_TYPE = new Locator("CAR_RENTAL_RENTAL_TYPE", AppiumBy.xpath("(//XCUIElementTypeOther[@name='driver_option'])[2]//XCUIElementTypeStaticText"));
    private static final Locator CONTACT_DRIVER_EXIST = new Locator("CONTACT_DRIVER_EXIST", AppiumBy.id("(//*[contains(@label,'Hubungi partner') or contains(@label,'Contact partner')]//following-sibling::*)[1]"));
    private static final Locator RENTAL_DETAIL_TITLE = new Locator("RENTAL_DETAIL_TITLE", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Detail Sewa' or @label='Rental Details']"));
    private static final Locator CAR_RENTAL_PICK_UP_LOCATION = new Locator("CAR_RENTAL_PICK_UP_LOCATION", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='pickup_time'])[1]//following-sibling:: XCUIElementTypeStaticText"));
    private static final Locator CAR_RENTAL_DROP_OFF_LOCATION = new Locator("CAR_RENTAL_DROP_OFF_LOCATION", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='pickup_time'])[2]//following-sibling:: XCUIElementTypeStaticText"));
    private static final Locator CAR_RENTAL_PICK_UP_DATE = new Locator("CAR_RENTAL_PICK_UP_DATE", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`name == 'pickup_time'`][1]"));
    private static final Locator CAR_RENTAL_DROP_OFF_DATE = new Locator("CAR_RENTAL_DROP_OFF_DATE", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`name == 'pickup_time'`][2]"));
    private static final Locator CAR_RENTAL_DURATION = new Locator("CAR_RENTAL_DURATION", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='pickup_time'])[1]//following-sibling:: XCUIElementTypeOther//XCUIElementTypeStaticText"));
    private static final Locator CR_REFUND_AND_RESCHEDULE_INFO_SECTION = new Locator("CR_REFUND_AND_RESCHEDULE_INFO_SECTION", AppiumBy.accessibilityId("Detail Button"));
    private static final Locator CR_RR_DETAIL_BTN = new Locator("CR_RR_DETAIL_BTN", AppiumBy.accessibilityId("Detail Button"));
    private static final Locator RR_INFO_BTM_SHEET = new Locator("RR_INFO_BTM_SHEET", AppiumBy.xpath("//XCUIElementTypeStaticText[@name=\"Refund & Reschedule\"]"));
    private static final Locator LOGIN_CHALLENGE_BTN = new Locator("LOGIN_CHALLENGE_BTN", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'Log in')]"));
    private static final Locator LOGIN_CHALLENGE_CLOSE_BTN = new Locator("LOGIN_CHALLENGE_CLOSE_BTN", AppiumBy.xpath("//XCUIElementTypeButton[@name='Kembali' or @name='Back']"));
    private static final Locator CR_ORDDER_DETAIL_TOTAL_PRICE = new Locator("CR_ORDER_DETAIL_TOTAL_PRICE", AppiumBy.xpath("//XCUIElementTypeStaticText[@name=\"Total\"]/parent::XCUIElementTypeOther/following-sibling::XCUIElementTypeOther//XCUIElementTypeStaticText[contains(@name,\"IDR\")]"));
    private static final Locator CR_PRICE_DETAIL_COMPONENTS = new Locator("CR_PRICE_DETAIL_COMPONENTS", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Price']/following-sibling::XCUIElementTypeOther//XCUIElementTypeStaticText[contains(@label, 'IDR')]"));
    private static final Locator CR_OTHER_FEES = new Locator("CR_OTHER_FEES", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label, 'Biaya Lainnya') or contains(@label, 'Other Fees') or contains(@label, 'Add-on')]/following-sibling::XCUIElementTypeOther//XCUIElementTypeStaticText[contains(@label, 'IDR')]"));
    private static final Locator CR_SCROLL_DOWN_FLOATING_BTN = new Locator("CR_SCROLL_DOWN_FLOATING_BTN", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@name, \"Manage Order\") or contains(@name, \"Atur Pesanan\")])[2]"));

    private static final Locator CR_CLOSE_PRICE_DETAIL = new Locator("CR_CLOSE_PRICE_DETAIL", AppiumBy.accessibilityId("ic cancel grey"));
    private static final Locator HELP_CENTER_CARD = new Locator("HELP_CENTER_CARD", AppiumBy.xpath("//*[contains(@label, 'Customer Care')]"));
    private static final Locator WAITING_PAYMENT_EXPIRY_TIME = new Locator("WAITING_PAYMENT_EXPIRY_TIME", AppiumBy.xpath("//*[*[contains(@label,'Selesaikan sebelum') or contains(@label,'Complete before')]]/parent::*//following-sibling::*//XCUIElementTypeStaticText[number(substring(@value,1,2))>=0]"));
    private static final Locator BACK_BUTTON_PDF = new Locator("BACK_BUTTON_PDF", AppiumBy.name("back"));
    private static final Locator BACK_BUTTON_ETICKET = new Locator("BACK_BUTTON_PDF", AppiumBy.xpath("//XCUIElementTypeOther[@name='E-voucher' or @name='E-ticket']/XCUIElementTypeImage[1]"));

    //flight order detail
    private static final Locator FLIGHT_DEPARTURE_DATE_DETAIL = new Locator("FLIGHT_DEPARTURE_DATE_DETAIL", AppiumBy.xpath("((//*[*[contains(@label,'Detail')]])[1]/*[*[*[XCUIElementTypeStaticText]]]//XCUIElementTypeStaticText)[1]"));
    private static final Locator FLIGHT_ARRIVAL_DATE_DETAIL = new Locator("FLIGHT_ARRIVAL_DATE_DETAIL", AppiumBy.xpath("((//*[*[contains(@label,'Detail')]])[1]/*[*[*[XCUIElementTypeStaticText]]]//XCUIElementTypeStaticText)[4]"));
    private static final Locator DEPART_AIRPORT_NAME_ORDER_DETAIL = new Locator("DEPART_AIRPORT_NAME", AppiumBy.xpath("((//*[*[contains(@label,'Detail')]])[1]/*[*[*[XCUIElementTypeStaticText]]]//XCUIElementTypeStaticText)[2]"));
    private static final Locator RETURN_AIRPORT_NAME_ORDER_DETAIL = new Locator("RETURN_AIRPORT_NAME", AppiumBy.xpath("((//*[*[contains(@label,'Detail')]])[1]/*[*[*[XCUIElementTypeStaticText]]]//XCUIElementTypeStaticText)[5]"));
    private static final Locator PASSENGER_NAMES = new Locator("PASSENGER_NAMES", AppiumBy.xpath("//*[*[contains(@label,'Penumpang') or contains(@label,'Passenger')]]/parent::*//following-sibling::XCUIElementTypeOther//XCUIElementTypeStaticText[number(substring(@label,1,2))]"));
    private static final Locator PRICE_DETAIL_COMPONENTS = new Locator("PRICE_DETAIL_COMPONENTS", AppiumBy.xpath("//*[@label='Price' or @label='Harga']/following-sibling::XCUIElementTypeOther/*[last()]/XCUIElementTypeOther[last()]/XCUIElementTypeStaticText"));
    private static final Locator PRICE_DETAIL_DISCOUNT_COMPONENTS = new Locator("PRICE_DETAIL_DISCOUNT_COMPONENTS", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name, \"Discount\") or contains(@name, \"Diskon\")]/parent::* /following-sibling::*[last()]/XCUIElementTypeStaticText"));
    private static final Locator ORDER_DETAIL_ID_COPY = new Locator("ORDER_DETAIL_ID_COPY", AppiumBy.name("ic copy"));
    private static final Locator ORDER_DETAIL_ID = new Locator("ORDER_DETAIL_ID", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,\"Order Detail ID:\")]"));
    private static final Locator CALL_BUTTON = new Locator("CALL_BUTTON", AppiumBy.name("call_phone button"));
    private static final Locator ORDER_DETAIL_ID_COPIED = new Locator("ORDER_DETAIL_ID_COPIED", AppiumBy.xpath("//XCUIElementTypeStaticText[@name=\"Order Detail ID berhasil disalin.\" or @name=\"Order Detail ID has been copied.\"]\n"));
    private static final Locator EDIT_SPECIAL_REQUEST = new Locator("EDIT_SPECIAL_REQUEST", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name=\"Ubah\" or @name=\"Edit\"])[2]"));
    private static final Locator SPECIAL_REQUEST_PAGE = new Locator("SPECIAL_REQUEST_PAGE", AppiumBy.xpath("//XCUIElementTypeStaticText[@name=\"Change the Special Request\" or @name=\"Ubah Permintaan Khusus\"]\n"));
    private static final Locator SPECIAL_REQUEST_CHANGE = new Locator("SPECIAL_REQUEST_CHNGE", AppiumBy.xpath("//XCUIElementTypeOther[@name=\"Revise Booking\"]/XCUIElementTypeImage[2]"));
    private static final Locator SPECIAL_REQUEST_TEXTFIELD = new Locator("SPECIAL_REQUEST_TEXTFIELD", AppiumBy.xpath("//XCUIElementTypeOther[@name=\"Revise Booking\"]/XCUIElementTypeTextView"));
    private static final Locator SAVE_SPECIAL_REQUEST = new Locator("SAVE_SPECIAL_REQUEST", AppiumBy.xpath("//XCUIElementTypeButton[@name=\"Save\" or @name=\"Simpan\"]"));
    private static final Locator YES_SPECIAL_REQUEST = new Locator("YES_SPECIAL_REQUEST", AppiumBy.xpath("//XCUIElementTypeButton[@name=\"Ya\" or @name=\"Yes\"]\n"));
    private static final Locator OTP_FEILD = new Locator("OTP_FEILD", AppiumBy.className("XCUIElementTypeTextField"));
    private static final Locator SPECIAL_REQUEST_CHANGED_BANNER = new Locator("SPECIAL_REQUEST_CHANGED_BANNER", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`name == \"Permintaan khusus berhasil diubah\" OR name == \"The special request has been changed\"`]"));
    private static final Locator SEE_RESERVATION_DETAILS = new Locator("SEE_RESERVATION_DETAILS", AppiumBy.xpath("//XCUIElementTypeButton[@name=\"Lihat detail reservasi\" or @name=\"See reservation details\"]"));
    private static final Locator HOTEL_MAP_BTN = new Locator("HOTEL_MAP_BTN", AppiumBy.accessibilityId("direction button"));
    private static final Locator REFUND_RESCHEDULE_BTMSHEET = new Locator("REFUND_RESCHEDULE_BTMSHEET", AppiumBy.xpath("(//XCUIElementTypeStaticText[@label=\"Info Refund & Reschedule\" or @label=\"Refund & Reschedule Info\"])[last()]"));
    private static final Locator PAGE_MODULE = new Locator("PAGE_MODULE", AppiumBy.accessibilityId("page_module_description"));
    private static final Locator EDIT_GUESTS_DETAILS = new Locator("EDIT_GUESTS_DETAILS", AppiumBy.xpath("//XCUIElementTypeStaticText[@name=\"Ubah Pemesanan\" or @name=\"Edit Booking\"]/parent::*"));
    private static final Locator LPG_BANNER = new Locator("LPG_BANNER", AppiumBy.accessibilityId("title"));
    private static final Locator FULL_PROTECTION_SUBMIT_NEW_CLAIM = new Locator("FULL_PROTECTION_SUBMIT_NEW_CLAIM", AppiumBy.xpath("///XCUIElementTypeStaticText[@name=\"Perlindungan Menginap\" or @name=\"Stay Protection\"]/parent::*"));
    private static final Locator IMPORTANT_INFO_CARD = new Locator("IMPORTANT_INFO_CARD", AppiumBy.name("Important Info description"));
    private static final Locator TTD_PACKAGE_NAME = new Locator("TTD_PACKAGE_NAME", AppiumBy.xpath("(//*[*[XCUIElementTypeStaticText[@label='Detail Paket' or @label='Package Details']]]/following-sibling::XCUIElementTypeStaticText)[1]"));
    private static final Locator TTD_GUEST_DETAIL_BUTTON = new Locator("TTD_GUEST_DETAIL_BUTTON", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Contact Info' or @label='Info Pemesan']//following-sibling::XCUIElementTypeStaticText[@label='Details' or @label='Detail']"));
    private static final Locator TTD_INSURANCE = new Locator("TTD_INSURANCE", AppiumBy.xpath("(//XCUIElementTypeStaticText[@label='Additional Order' or @label='Pesanan Tambahan']//following::XCUIElementTypeStaticText[@label])[1]"));
    private static final Locator TTD_ORDER_DETAIL = new Locator("TTD_ORDER_DETAIL", AppiumBy.id("Details button"));
    private static final Locator CONTINUE_PAYMENT_BUTTON = new Locator("CONTINUE_PAYMENT_BUTTON", AppiumBy.id("Complete payment button"));
    private static final Locator FIRST_ROOM_POLICY = new Locator("FIRST_ROOM_POLICY", AppiumBy.xpath("//XCUIElementTypeStaticText[@label and following::XCUIElementTypeStaticText[@label='Room Type 2' or @label='Tipe Kamar 2'] and not(following::XCUIElementTypeStaticText[@label='Room Type 1' or @label='Tipe Kamar 1'])]"));
    private static final Locator SECOND_ROOM_POLICY = new Locator("SECOND_ROOM_POLICY", AppiumBy.xpath("//XCUIElementTypeStaticText[@label and not(following::XCUIElementTypeStaticText[@label='Room Type 2' or @label='Tipe Kamar 2']) and following::XCUIElementTypeStaticText[@label='Other Important Info' or @label='Info Penting Lainnya']]"));
    private static final Locator SECOND_ROOM_POLICY_CHEVRON = new Locator("SECOND_ROOM_POLICY_CHEVRON", AppiumBy.xpath("(//XCUIElementTypeImage[@name='icArrowDownUp'])[2]"));
    private static final Locator ROOM_TO_REFUNDS_OPTIONS = new Locator("ROOM_TO_REFUND_OPTIONS", AppiumBy.xpath("//*[contains(@name,'radioButton')]"));
    private static final Locator WHOOSH_TRAIN_NAME = new Locator("WHOOSH_TRAIN_NAME", AppiumBy.xpath("//*[contains(@label,'Whoosh') and @type='XCUIElementTypeStaticText']"));
    private static final Locator WHOOSH_TRAIN_DEPARTURE_TIME = new Locator("WHOOSH_TRAIN_DEPARTURE_TIME", AppiumBy.xpath("//*[contains(@label,',') and contains(@label,' - ') and @type='XCUIElementTypeStaticText']"));
    private static final Locator WHOOSH_TRAIN_ORIGIN_STATION = new Locator("WHOOSH_TRAIN_ORIGIN_STATION", AppiumBy.xpath("(//*[@type='XCUIElementTypeStaticText' and contains(@label,'(ID')])[1]"));
    private static final Locator WHOOSH_TRAIN_DESTINATION_STATION = new Locator("WHOOSH_TRAIN_DESTINATION_STATION", AppiumBy.xpath("(//*[@type='XCUIElementTypeStaticText' and contains(@label,'(ID')])[2]"));
    private static final Locator WHOOSH_DURATION_DIRECT_INFO = new Locator("WHOOSH_DURATION_DIRECT_INFO", AppiumBy.xpath("//*[contains(@label,'Direct') or contains(@label,'Transit')]"));
    private static final Locator WHOOSH_PASSENGER_AND_FACILITIES_TITLE = new Locator("WHOOSH_PASSENGER_AND_FACILITIES_TITLE", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and @label='Passengers & Facilities']"));
    private static final Locator WHOOSH_PASSENGER_SEAT = new Locator("WHOOSH_PASSENGER_SEAT", AppiumBy.xpath("//*[contains(@label,' / ')]"));
    private static final Locator ORDER_ID_TOP = new Locator("ORDER_ID_TOP", AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND (label BEGINSWITH 'Order ID:' OR label BEGINSWITH 'ID Pesanan:')"));
    private static final Locator HIDE_POPUP = new Locator("HIDE_POPUP", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and (@label='Hide' or @label='Tutup' or @name='Hide' or @name='Tutup')]"));
    //Train

    private static final Locator TRAIN_DEPARTURE_TIME = new Locator("TRAIN_DEPARTURE_TIME", AppiumBy.xpath("(//XCUIElementTypeScrollView/XCUIElementTypeOther[1]/XCUIElementTypeOther[3]//XCUIElementTypeOther[./XCUIElementTypeImage]/XCUIElementTypeOther//XCUIElementTypeStaticText[1])[1]"));
    private static final Locator TRAIN_ORIGIN_STATION = new Locator("TRAIN_ORIGIN_STATION", AppiumBy.xpath("(//XCUIElementTypeScrollView/XCUIElementTypeOther[1]/XCUIElementTypeOther[3]//XCUIElementTypeOther[./XCUIElementTypeImage]/XCUIElementTypeOther//XCUIElementTypeStaticText[2])[1]"));
    private static final Locator TRAIN_DESTINATION_STATION = new Locator("TRAIN_DESTINATION_STATION", AppiumBy.xpath("(//XCUIElementTypeScrollView/XCUIElementTypeOther[1]/XCUIElementTypeOther[3]//XCUIElementTypeOther[./XCUIElementTypeImage]/XCUIElementTypeOther//XCUIElementTypeStaticText[2])[2]"));
    private static final Locator TRAIN_NAME_CLASS = new Locator("TRAIN_NAME_CLASS", AppiumBy.xpath("//XCUIElementTypeScrollView/XCUIElementTypeOther[1]/XCUIElementTypeOther[3]//XCUIElementTypeOther[./XCUIElementTypeImage]/XCUIElementTypeStaticText[last()]"));
    private static final Locator SEE_BOARDING_PASS = new Locator("SEE_BOARDING_PASS", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Print e-boarding pass' or @label='Cetak e-boarding pass' or @label='See e-boarding pass' or @label='Lihat e-boarding pass']"));
    /**
     * PNR value on order detail: a {@code XCUIElementTypeStaticText} immediately after the
     * "Booking code (PNR)" / "Kode booking" label (sibling in the same row, not a nested descendant).
     */
    private static final Locator BOOKING_CODE_ORDER_DETAIL = new Locator("BOOKING_CODE_ORDER_DETAIL", AppiumBy.xpath("//*[@name='Booking code' or @name='Kode booking']/following::XCUIElementTypeStaticText[1]"));
    private static final Locator GET_BOARDING_PASS_BTN = new Locator("GET_BOARDING_PASS_BTN", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'Get boarding pass') or contains(@label,'Dapatkan boarding pass')]"));
    /**
     * Copy button on the native online-checkin screen (enabled after navigation from order detail).
     */
    private static final Locator COPY_BOOKING_CODE_CHECK_IN_PAGE = new Locator("COPY_BOOKING_CODE_CHECK_IN_PAGE", AppiumBy.xpath(
            "//XCUIElementTypeButton[@label='Copy' or @label='Salin']"));
    /**
     * PNR on online-checkin: {@code XCUIElementTypeStaticText} inside the {@code XCUIElementTypeOther}
     * immediately before the Copy button (e.g. label {@code ESK33A}), exposed via the embedded
     * {@code XCUIElementTypeWebView} tree while still in {@code NATIVE_APP} context.
     */
    private static final Locator BOOKING_CODE_CHECK_IN_IOS = new Locator("BOOKING_CODE_CHECK_IN_IOS", AppiumBy.xpath(
            "//XCUIElementTypeButton[@label='Copy' or @label='Salin']"
                    + "/preceding-sibling::*[1]//XCUIElementTypeStaticText"));
    /**
     * Continue CTA (e.g. {@code name="Continue to check-in"}); enabled after Copy is tapped.
     */
    private static final Locator CHECK_IN_BTN = new Locator("CHECK_IN_BTN", AppiumBy.xpath(
            "//XCUIElementTypeButton[@label='Continue to check-in' or @label='Lanjut check-in']"));
    /**
     * Snackbar/toast shown briefly (~3–4s) right after Copy on the online-checkin screen.
     */
    private static final Locator BOOKING_CODE_COPIED_SNACKBAR = new Locator("BOOKING_CODE_COPIED_SNACKBAR", AppiumBy.xpath(
            "//XCUIElementTypeStaticText["
                    + "contains(@name,'Booking code') and (contains(@name,'copied') or contains(@name,'disalin'))"
                    + " or contains(@name,'Kode booking berhasil disalin')"
                    + " or contains(@label,'Booking code succesfully copied')"
                    + " or contains(@label,'Booking code successfully copied')"
                    + " or contains(@label,'Booking code has been copied')"
                    + " or contains(@label,'Kode booking berhasil disalin')"
                    + " or contains(@value,'Booking code succesfully copied')"
                    + " or contains(@value,'Booking code successfully copied')"
                    + " or contains(@value,'Kode booking berhasil disalin')"
                    + "]"));
    private static final Locator ONLINE_CHECKIN_PAGE_TITLE = new Locator("ONLINE_CHECKIN_PAGE_TITLE", AppiumBy.xpath(
            "//XCUIElementTypeStaticText[@label='Online Check-in' or @label='Check-in online']"));
    /**
     * Native Identification step after Continue (not a separate webview URL).
     */
    private static final Locator IDENTIFICATION_PAGE_TITLE = new Locator("IDENTIFICATION_PAGE_TITLE", AppiumBy.xpath(
            "//*[@type='XCUIElementTypeStaticText' and @label='Identification']"));

    private static final Locator REDEEM_BUTTON = new Locator("REDEEM_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Redeem') or contains(@name,'Tukar')]"));

    private static final Locator STANDALONE_SECTION = new Locator("STANDALONE_SECTION", AppiumBy.xpath("//*[contains(@label,'Add protection for a worry-free trip') or contains(@label,'Lindungi perjalananmu biar tenang')]"));
    private static final Locator STANDALONE_ADD_BTN = new Locator("STANDALONE_ADD_BTN", AppiumBy.xpath("//XCUIElementTypeButton[(contains(@name,'Add') or contains(@name,'Tambahkan')) and contains(@visible,'true')]"));
    private static final Locator STANDALONE_PRICE_INSURANCE = new Locator("STANDALONE_PRICE_INSURANCE", AppiumBy.xpath("//*[contains(@label,'/pax')]//preceding-sibling::*[contains(@label,'IDR') and contains(@visible,'true')]"));
    private static final Locator CONTACT_CALL_BUTTON = new Locator("CONTACT_CALL_BUTTON", AppiumBy.name("call_phone button"));
    private static final Locator WHATSAPP_BUTTON = new Locator("WHATSAPP_BUTTON", AppiumBy.name("whatsapp button"));
    private static final Locator LOGIN_NOW_BUTTON = new Locator("LOGIN_NOW_BUTTON", AppiumBy.xpath("//*[@name='Log in now' and @type='XCUIElementTypeButton']"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.xpath("//*[@name='Continue' and @type='XCUIElementTypeButton']"));
    private static final Locator BOOKING_CODE = new Locator("BOOKING_CODE", AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND name MATCHES '^[A-Z]{2}[0-9]+$'"));
    private static final Locator COPY_BOOKING_CODE = new Locator("COPY_BOOKING_CODE", AppiumBy.xpath("//*[@name='ic copy' and @type='XCUIElementTypeButton']"));
    private static final Locator IMPORTANT_INFO_DETAILS = new Locator("IMPORTANT_INFO_DETAILS", AppiumBy.xpath("//*[contains(@name,'Info Penting') or contains(@name,'Important Info Title')]//following-sibling::*[(@name='Details' or @label='Detail') and @type='XCUIElementTypeStaticText']"));
    private static final Locator TRAIN_NAME = new Locator("TRAIN_NAME", AppiumBy.xpath("//*[contains(@name,'Whoosh') and @type='XCUIElementTypeStaticText']"));
    private static final Locator TRAIN_ROUTE = new Locator("TRAIN_ROUTE", AppiumBy.xpath("//*[contains(@name,' - ') and @type='XCUIElementTypeStaticText']"));
    private static final Locator TRAIN_DATE = new Locator("TRAIN_DATE", AppiumBy.xpath("//*[contains(@name,'Jul') or contains(@name,'Jan') or contains(@name,'Feb') or contains(@name,'Mar') or contains(@name,'Apr') or contains(@name,'May') or contains(@name,'Jun') or contains(@name,'Aug') or contains(@name,'Sep') or contains(@name,'Oct') or contains(@name,'Nov') or contains(@name,'Dec')]"));
    private static final Locator DURATION_DIRECT_INFO = new Locator("DURATION_DIRECT_INFO", AppiumBy.xpath("//*[contains(@name,'Direct') or contains(@name,'Transit') or contains(@name,'h')]"));
    private static final Locator PASSENGER_AND_FACILITIES_TITLE = new Locator("PASSENGER_AND_FACILITIES_TITLE", AppiumBy.xpath("//*[@name='Passengers & Facilities' and @type='XCUIElementTypeStaticText']"));
    private static final Locator PASSENGER_NAME = new Locator("PASSENGER_NAME", AppiumBy.xpath("//*[contains(@name,'Ms.') or contains(@name,'Mr.') or contains(@name,'Mrs.')]"));
    private static final Locator PASSENGER_SEAT = new Locator("PASSENGER_SEAT", AppiumBy.xpath("//*[contains(@name,'/') and @type='XCUIElementTypeStaticText']"));
    // WebView page root often reports isDisplayed=false on iOS (tall node / invisible ancestor); assert via visible child instead.
    private static final Locator HELP_CENTER = new Locator("HELP_CENTER", AppiumBy.xpath("//*[@name='bn_grid' and @type='XCUIElementTypeOther']"));
    private static final Locator HELP_CENTER_PAGE = new Locator("HELP_CENTER_PAGE", AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == 'Help Center | tiket.com' OR name == 'Pusat Bantuan | tiket.com'`]/XCUIElementTypeImage[`name == 'Tiket'`]"));
    private static final Locator HELP_CENTER_BACK_BUTTON = new Locator("HELP_CENTER_BACK_BUTTON", AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == 'Help Center | tiket.com' OR name == 'Pusat Bantuan | tiket.com'`]/XCUIElementTypeButton[1]"));
    // EN: Need further help? Contact Customer Care | ID: Masih butuh bantuan? Hubungi Customer Care
    private static final Locator CONTACT_CUSTOMER_CARE = new Locator("CONTACT_CUSTOMER_CARE", AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND (name == 'Need further help? Contact Customer Care' OR name == 'Masih butuh bantuan? Hubungi Customer Care' OR name CONTAINS 'Contact Customer Care' OR name CONTAINS 'Hubungi Customer Care')"));
    // Contact Us webview title often fails isDisplayed() on iOS; assert presence instead.
    private static final Locator CONTACT_US_TITLE = new Locator("CONTACT_US_TITLE", AppiumBy.iOSNsPredicateString("(type == 'XCUIElementTypeStaticText' OR type == 'XCUIElementTypeOther') AND (name == 'Contact Us' OR name == 'Hubungi Kami' OR name CONTAINS 'Contact Us' OR name CONTAINS 'Hubungi Kami')"));
    private static final Locator SKIP_BUTTON = new Locator("SKIP_BUTTON", AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeButton' AND (name == 'Skip' OR name == 'Lewati') AND visible == 1"));

    private static final Locator SUBMIT_CLAIM_BUTTON = new Locator("SUBMIT_CLAIM_BUTTON", AppiumBy.xpath("//*[contains(@name,'Submit claim')]"));
    private static final Locator LPG_PAGE_TITLE = new Locator("LPG_PAGE_TITLE", AppiumBy.xpath("//*[contains(@name,'Lowest Price Guarantee')]"));
    private static final Locator READ_BEFORE_CLAIM = new Locator("READ_BEFORE_CLAIM", AppiumBy.xpath("//*[@name=\"Read Before Claim\"]"));
    private static final Locator READ_BEFORE_CLAIM_BOTTOM_SHEET = new Locator("READ_BEFORE_CLAIM_BOTTOM_SHEET", AppiumBy.xpath("//*[contains(@name,'Read Before Claim')]"));
    private static final Locator FINAL_PRICE_INPUT = new Locator("FINAL_PRICE_INPUT", AppiumBy.xpath("//*[@name=\"finalPrice\"]"));
    private static final Locator URL_INPUT = new Locator("URL_INPUT", AppiumBy.xpath("//*[@name=\"urlOtherProduct\"]"));
    private static final Locator TNC_CHECKBOX = new Locator("TNC_CHECKBOX", AppiumBy.xpath("//*[@name=\"checkbox\"]"));
    private static final Locator CLAIM_NOW_BUTTON = new Locator("CLAIM_NOW_BUTTON", AppiumBy.xpath("//*[@name=\"Claim now\"]"));
    private static final Locator CLOSE_ICON = new Locator("CLOSE_ICON", AppiumBy.xpath("(//*[contains(@name,'ic cancel grey')])[2]"));
    private static final Locator CROSS_ICON = new Locator("CROSS_ICON", AppiumBy.xpath("//*[contains(@name,'tds ic oval cross')]"));
    private static final Locator HIGH_PRICE_ERROR_MSG = new Locator("HIGH_PRICE_ERROR_MSG", AppiumBy.xpath("//*[@name=\"Must be lower than your total payment on tiket.com\"]"));
    private static final Locator SEE_GUIDELINE_LINK = new Locator("SEE_GUIDELINE_LINK", AppiumBy.xpath("(//*[contains(@name,'See guideline')])[2]"));
    private static final Locator SCREENSHOT_GUIDELINE_TITLE = new Locator("SCREENSHOT_GUIDELINE_TITLE", AppiumBy.xpath("(//*[contains(@name,'Screenshot Guideline')])"));
    private static final Locator POPUP_CLAIM_NOW_BUTTON = new Locator("POPUP_CLAIM_NOW_BUTTON", AppiumBy.xpath("(//*[@name=\"Claim now\" and @type=\"XCUIElementTypeStaticText\"])[2]"));
    private static final Locator UPLOAD_BUTTON = new Locator("UPLOAD_BUTTON", AppiumBy.xpath("//*[contains(@name,'tds ic add image')]"));
    private static final Locator FIRST_IMAGE_THUMBNAIL = new Locator("FIRST_IMAGE_THUMBNAIL", AppiumBy.xpath("(//*[@name[contains(.,'Photo')]])[8]"));
    private static final Locator WAITING_FOR_APPROVAL_STATUS = new Locator("WAITING_FOR_APPROVAL_STATUS", AppiumBy.xpath("//*[contains(@name,'Waiting for approval')]"));
    private static final Locator POPUP_SUCCESS_CANCEL = new Locator("POPUP_SUCCESS_CANCEL", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'pesanan berhasil dibatalkan dan dipindahkan ke riwayat pesanan.') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'your order was canceled successfully and moved to the order history.')]"));
    private static final Locator ORDER_STATUS_NOTIFICATION_BANNER = new Locator("ORDER_STATUS_NOTIFICATION_BANNER", AppiumBy.xpath("//*[@name='banner_container']//following-sibling::*[@name='tds_ic_alert_high']//following-sibling::*[translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='this order has been canceled' or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='pesanan ini telah dibatalkan']"));

    @Override 
    public String getTotalPaymentPah(String orderId) {
        return getTotalPayment();
    }


    @Override
    public String getRoomName(String orderId) {
        String roomName = driverTools.getText(ROOM_NAME_INFO).orElseThrow();
        roomName = roomName.split(" x ")[1];
        logger.log("Room Name Booking Detail: " + roomName);
        return roomName;
    }

    @Override
    public void clickRedeemButton() {
        WebElement redeemButton = driverTools.swipeToPresentedElement(REDEEM_BUTTON, Swipe.DOWN, 7).orElseThrow();
        driverTools.click(redeemButton);
    }

    @Override
    public String getOrderStatus(String orderId, int retry) {
        driverTools.switchToNative();
        String status = "";
        for (int i = 0; i < retry; i++) {
            driverTools.swipeDown(2);
            Optional<WebElement> banner = driverTools.waitForElementToPresence(ORDER_STATUS_NOTIFICATION_BANNER);
            status = driverTools.getText(banner.get());
            if (status != null && !status.isEmpty()) {
                break;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return status;
    }

    @Override
    public String getRefundInfo(int index) {
        Locator refundPolicyLocator;
        if (index == 1)
            refundPolicyLocator = FIRST_ROOM_POLICY;
        else if (index == 2) {
            driverTools.clickElementByCoordinate(SECOND_ROOM_POLICY_CHEVRON);
            refundPolicyLocator = SECOND_ROOM_POLICY;
        } else {
            throw new NotImplementedException("not implented room index");
        }

        List<String> refundPolicies = driverTools.getTexts(refundPolicyLocator);

        String refundPoliciesString = String.join(" ", refundPolicies);
        logger.log("Refund policies: " + refundPoliciesString);
        return refundPoliciesString;
    }

    @Override
    public String getRoomName(int index) {
        Locator roomNameLocator = getRoomLocator(index);
        String roomName = driverTools.getText(roomNameLocator).orElseThrow();
        logger.log("Room name: " + roomName);
        return roomName;
    }

    private Locator getRoomLocator(int index) {
        return new Locator("room " + index, AppiumBy.xpath("(//XCUIElementTypeStaticText[@label='Tipe Kamar " + index + "' or @label='Room Type " + index + "']//following::XCUIElementTypeStaticText[contains(@label,'Malam)') or contains(@label,'Night)')]/preceding-sibling::XCUIElementTypeStaticText)[1]"));
    }

    @Override
    public int getRoomAmount() {
        String[] amount = driverTools.getText(ROOM_NAME_INFO).orElseThrow().split(" ");
        return Integer.parseInt(amount[0]);
    }

    @Override
    public int getRommGuestAmount() {
        String[] amount = driverTools.getText(ROOM_INFO).orElseThrow().split(" ");
        return Integer.parseInt(amount[0]);
    }

    @Override
    public void seeRoomDetails(int index) {
        driverTools.swipeToElement(seeRoomDetailsButton(index), Swipe.UP, 5);
        driverTools.click(seeRoomDetailsButton(index));
    }

    private Locator seeRoomDetailsButton(int index) {
        return new Locator("SEE_ROOM_DETAILS_" + index, AppiumBy.xpath("(//XCUIElementTypeButton[@label='See room details' or @label='Lihat detail kamar'])[" + index + "]"));
    }

    @Override
    public void openTabRefundRescheduleDetailRoom() {
        driverTools.clickElementByCoordinate(REFUND_RESCHEDULE_TAB_BUTTON);
    }

    @Override
    public void openRefundRequestDetailRoom() {
        driverTools.swipeToElement(REFUND_REQUEST_BUTTON, Swipe.UP, 3);
        driverTools.click(REFUND_REQUEST_BUTTON);
    }

    @Override
    public String getRoomStatus(String roomName) {
        driverTools.swipeToElement(roomStatus(roomName), Swipe.UP, 10);
        boolean roomStatusElement = driverTools.isElementDisplayed(roomStatus(roomName));
        return roomStatusElement ? "Processing refund" : "Room Status Not Found";
    }

    private Locator roomStatus(String roomName) {
        return new Locator("ROOM_STATUS", AppiumBy.xpath("(//*[@label='" + roomName + "']//following::XCUIElementTypeButton[@label='Check refund page' or @label='Cek halaman refund'])[1]//preceding::*[@name='banner_container'][1]"));
    }

    @Override
    public void seeMoreDetails() {
        driverTools.swipeToElement(SEE_MORE_DETAILS_BUTTON, Swipe.UP, 5).orElseThrow();
        driverTools.click(SEE_MORE_DETAILS_BUTTON);
    }

    @Override
    public boolean refundBannerInfo() {
        return driverTools.swipeToElement(REFUND_BANNER_INFO, Swipe.DOWN, 10).isPresent();
    }

    @Override
    public boolean refundNotAvailable() {
        return driverTools.waitForElementToPresence(REFUND_NOT_AVAILABLE).isPresent();
    }

    @Override
    public void clickReschedule() {
        if (driverTools.isElementDisplayed(HIDE_POPUP)) {
            driverTools.click(HIDE_POPUP);
        }
        driverTools.swipeToElement(RESCHEDULE_BUTTON, Swipe.UP, 10).orElseThrow();
        driverTools.click(RESCHEDULE_BUTTON);
    }

    @Override
    public void clickReschedule(String orderId) {
        clickReschedule();
    }

    @Override
    public boolean isInfoPopupDisplayed() {
        return driverTools.waitForElementToPresence(CANT_RESCHEDULE_POPUP).isPresent();
    }

    @Override
    public void closeInfoPopup() {
        if (driverTools.isElementDisplayed(SKIP_OK_BUTTON))
            driverTools.click(SKIP_OK_BUTTON);
        else
            driverTools.click(OK_BUTTON);
    }

    @Override
    public void goToPreviousOrderBanner() {
        driverTools.swipeDown(5);
        driverTools.swipeToElement(RESCHEDULE_CARD, Swipe.DOWN, 8).orElseThrow();
    }

    @Override
    public void clickPreviousOrder() {
        WebElement rescheduleBannerElement = driverTools.swipeToElement(RESCHEDULE_CARD, Swipe.DOWN, 6).orElseThrow();
        // Banner label groups two lines; centre tap hits intro copy ("Use the e-ticket…"), not the link line.
        driverTools.clickElementFractionWithinBounds(rescheduleBannerElement, 0.5d, 0.78d);
    }

    @Override
    public void clickPreviousOrder(String orderId) {
        clickPreviousOrder();
    }

    @Override
    public boolean isPreviousOrderBannerDisplayed(String orderId) {
        if (driverTools.waitForElementToAppear(OK_POPUP).isPresent()) driverTools.click(OK_POPUP);
        driverTools.swipeToElement(RESCHEDULE_CARD, Swipe.DOWN, 5).orElseThrow();
        return driverTools.waitForElementToAppear(RESCHEDULE_CARD).isPresent();
    }

    @Override
    public boolean isNewScheduleButtonDisplayed(String orderId) {
        if (driverTools.waitForElementToAppear(OK_POPUP).isPresent()) driverTools.click(OK_POPUP);
        driverTools.swipeToElement(NEW_SCHEDULE_BUTTON, Swipe.DOWN, 5).orElseThrow();
        return driverTools.waitForElementToAppear(NEW_SCHEDULE_BUTTON).isPresent();
    }

    @Override
    public void clickNewScheduleButton(String orderId) {
        driverTools.swipeToElement(NEW_SCHEDULE_BUTTON, Swipe.DOWN, 5).orElseThrow();
        driverTools.click(NEW_SCHEDULE_BUTTON);
    }

    @Override
    public boolean isRescheduleCardDisplayed(String orderId) {
        WebElement rescheduleCard = driverTools.swipeToElement(RESCHEDULE_CARD, Swipe.DOWN, 5).orElseThrow();
        return driverTools.waitForElementToAppear(rescheduleCard, 10).isPresent();
    }

    @Override
    public boolean isHotelNameDisplayed(String query) {
        WebElement hotelNameElement = driverTools.waitForElementToAppear(PRODUCT_NAME, 5).orElseThrow();
        return hotelNameElement.isDisplayed();
    }

    @Override
    public boolean isHotelNameDisplayed(String hotelName, String orderId) {
        return isHotelNameDisplayed(hotelName);
    }

    @Override
    public String getHotelName(String orderId) {
        WebElement hotelName = driverTools.waitForElementToPresence(HOTEL_NAME).orElseThrow();
        return driverTools.getValueAttribute(hotelName, "label");
    }

    @Override
    public boolean isRoomNameDisplayed(String roomName) {
        WebElement roomNameTextElement = driverTools.swipeToElement(ORDER_DETAIL_NAME, Swipe.UP, 3).orElseThrow();
        String fullRoomName = driverTools.getValueAttribute(roomNameTextElement, "value");
        Pattern pattern = Pattern.compile("\\broom\\b\\s+(.*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fullRoomName);
        String strippedRoomName = matcher.find() ? matcher.group(1) : "";
        matcher = pattern.matcher(roomName);
        roomName = matcher.find() ? matcher.group(1) : "";
        logger.log("data Name = '" + roomName + "'\n displayed Name = '" + strippedRoomName + "'");
        return strippedRoomName.contains(roomName);
    }

    /**
     * Returns the parent order id (e.g. {@code "1900291994"}) from the native iOS tree.
     * Tries the two old-UI variants in order:
     * <ol>
     *   <li>A single static text named {@code "Order ID 1234567890"}.</li>
     *   <li>The bare {@code "Order ID"} element whose {@code value} attribute carries
     *       the id (older builds).</li>
     * </ol>
     * On the new iOS UI the native tree only renders the bare {@code "Order ID"} label
     * with no numeric id attached and the webview can't be reliably entered from this
     * page, so we fail fast with a descriptive error message that captures whatever the
     * native tree did expose, to make debugging easy.
     */
    @Override
    public String getOrderID() {
        // Old UI variant 1: static text whose name contains "Order ID " (with trailing
        // space) followed by the actual ID. The trailing space guarantees we don't
        // false-match the bare "Order ID" label that also exists on the new UI.
        if (driverTools.isElementDisplayed(HIDE_POPUP)) {
            driverTools.click(HIDE_POPUP);
        }
        Optional<String> oldXpathText = driverTools.waitForElementToAppear(ORDER_ID_XPATH, 5)
                .map(driverTools::getText);
        if (oldXpathText.isPresent()) {
            return extractOrderID(oldXpathText.get());
        }
        // Old UI variant 2: name-based locator. Read the value attribute and only
        // accept it if it actually contains a digit; on the new UI the value is just
        // the literal "Order ID" label, which means we couldn't get the id natively.
        Optional<WebElement> orderIDElement = driverTools.waitForElementToAppear(ORDER_ID, 5);
        String capturedText = "N/A";
        String capturedValue = "N/A";
        if (orderIDElement.isPresent()) {
            String value = driverTools.getValueAttribute(orderIDElement.get(), "value");
            String trimmed = value == null ? "" : value.trim();
            if (!trimmed.isEmpty() && trimmed.chars().anyMatch(Character::isDigit)) {
                return extractOrderID(trimmed);
            }
            // Capture both attributes once so the error below carries enough evidence
            // to tell whether we're on the new UI (label-only) or the element really
            // is missing — without paying the cost twice on the success path above.
            capturedText = String.valueOf(driverTools.getText(orderIDElement.get()));
            capturedValue = String.valueOf(value);
        }
        throw new RuntimeException("Could not extract a numeric Order ID from the native iOS tree; "
                + "ORDER_ID_XPATH did not match, and ORDER_ID element text was '"
                + capturedText + "' and value attribute was '" + capturedValue
                + "'. The new iOS UI exposes only the bare 'Order ID' label natively.");
    }

    /**
     * Returns the per-itinerary {@code orderDetailId} (e.g. {@code "1900742246"}) as
     * shown on the new iOS UI's e-ticket card. Pairs with
     * {@code OrderListPage#getOrderDetailIdByOrderId(String)} on the test side so the
     * expected value comes from the same internal-order API used by the deeplink.
     */
    @Override
    public String getOrderDetailID() {
        WebElement detailIdElement = driverTools.waitForElementToAppear(ORDER_DETAIL_ID_VALUE, 10).orElseThrow();
        String value = driverTools.getValueAttribute(detailIdElement, "value");
        if (value == null || value.isBlank()) {
            throw new RuntimeException("Order Detail ID element returned empty value");
        }
        return value.trim();
    }

    /**
     * Pulls the numeric order ID out of a raw display string. Handles
     * {@code "Order ID 1234567890"}, {@code "Order ID  1234567890"} (double space), and
     * a bare {@code "1234567890"}. Throws when the string is empty or carries no
     * digits in its trailing token (e.g. the new-UI bare {@code "Order ID"} label) so
     * the test fails loudly instead of asserting against {@code "ID"}.
     */
    private static String extractOrderID(String text) {
        if (text == null || text.isBlank()) {
            throw new RuntimeException("Order ID element returned empty value");
        }
        String trimmed = text.trim();
        String[] parts = trimmed.split("\\s+");
        String last = parts[parts.length - 1];
        if (!last.chars().anyMatch(Character::isDigit)) {
            throw new RuntimeException("Order ID element does not contain a numeric ID; "
                    + "raw value was '" + trimmed + "'");
        }
        return last;
    }

    @Override
    public String getCheckInDate() {
        return driverTools.getText(CHECKIN_DATE).orElseThrow();
    }

    @Override
    public String getStartDate(String orderId) {
        driverTools.swipeToElement(CHECKIN_DATE, Swipe.UP, 3);
        WebElement startDateTextElement = driverTools.swipeToElement(CHECKIN_DATE, Swipe.DOWN, 3).orElseThrow();
        String checkinDateText = driverTools.getValueAttribute(startDateTextElement, "value").split(" ")[1];
        logger.log("checkinDateText-> " + checkinDateText);
        return checkinDateText;
    }

    @Override
    public String getEndDate() {
        WebElement endDateTextElement = driverTools.waitForElementToAppear(CHECKOUT_DATE).orElseThrow();
        String endDate = driverTools.getValueAttribute(endDateTextElement, "label");
        logger.log("endDate-> " + endDate);
        return endDate;
    }

    @Override
    public String getCheckOutDate() {
        return driverTools.getText(CHECKOUT_DATE).orElseThrow();
    }

    @Override
    public String getTotalAdult() {
        WebElement totalAdultElement = driverTools.swipeToElement(TOTAL_ADULT, Swipe.UP, 2).orElseThrow();
        String total = driverTools.getText(totalAdultElement);
        //get the number only
        String[] preprocess = total.split(" ");
        total = preprocess[0];
        return total;
    }

    @Override
    public String getTotalNight(String orderId) {
        WebElement totalNightElement = driverTools.waitForElementToAppear(TOTAL_NIGHT, 5).orElseThrow();
        return driverTools.getText(totalNightElement);
    }

    @Override
    public String getTotalRoom() {
        WebElement totalRoomElement = driverTools.waitForElementToAppear(TOTAL_ROOM, 5).orElseThrow();
        String totalRoom = driverTools.getText(totalRoomElement);
        //get the number only
        String[] preprocess = totalRoom.split("x");
        totalRoom = preprocess[0];
        logger.log("totalRoom->" + totalRoom);
        return totalRoom;
    }

    @Override
    public String getGuestFullName() {
        WebElement guestFullNameElement = driverTools.waitForElementToAppear(GUEST_FULL_NAME, 5).orElseThrow();
        String name = driverTools.getText(guestFullNameElement);
        //remove the title
        String[] preprocess = name.split(" ", 2);
        name = preprocess[1];
        return name;
    }

    @Override
    public void openTTDGuestDetail(int order) {
        driverTools.click(getGuestDetailButton(order));
    }

    private Locator getGuestDetailButton(int order) {
        return new Locator("GUEST_DETAIL_" + order, AppiumBy.xpath("(//XCUIElementTypeOther[@name='Item button'])[" + order + "]"));
    }

    @Override
    public String getTTDGuestFullName() {
        WebElement guestDetailFullName = driverTools.waitForElementToAppear(GUEST_DETAIL_FULL_NAME).orElseThrow();
        return driverTools.getText(guestDetailFullName);
    }

    @Override
    public void closeTTDGuestDetail() {
        driverTools.waitForElementToAppear(CLOSE_GUEST_DETAIL).ifPresent(driverTools::click);
    }

    @Override
    public String getETicketLabel() {
        // The native "Enjoying TiketPreprod?" rate-the-app popup can land here when the
        // order detail page is opened via deeplink (the equivalent home-page dismiss in
        // HomePageIOS does not cover the deeplink entry path). Dismiss it before reading
        // the e-ticket label so the underlying static text is reachable.
        closeGiveRatingPopupIfPresent();
        return driverTools.getText(ETICKET_LABEL).orElseThrow();
    }

    /**
     * Best-effort dismissal of the iOS native "Enjoying TiketPreprod? Tap a star to rate
     * it on the App Store." popup. Mirrors the helper on {@code HomePageIOS} so any entry
     * point that lands on the order detail page (e.g. deeplink) can transparently absorb
     * the prompt without test-side noise. No-op when the popup is absent.
     */
    private void closeGiveRatingPopupIfPresent() {
        driverTools.waitForElementToAppear(CLOSE_GIVE_RATING_POPUP, 2).ifPresent(driverTools::click);
    }

    @Override
    public void clickSeeEticketFromManageOrder() {
        if (driverTools.waitForElementToAppear(OK_POPUP, 1).isPresent()) driverTools.click(OK_POPUP);
        driverTools.swipeToElement(MANAGE_ORDER_SEE_ETICKET, Swipe.UP, 8);
        driverTools.click(MANAGE_ORDER_SEE_ETICKET);
    }

    private Locator getETicketElement(String language) {
        if (!language.equals("Bahasa Indonesia")) {
            return new Locator("ETICKET_LANGUAGE", AppiumBy.xpath("//*[@label='English' or @label='Bahasa Inggris']"));
        } else {
            return new Locator("ETICKET_LANGUAGE", AppiumBy.xpath("//*[@label='Bahasa Indonesia']"));
        }
    }

    @Override
    public void clickETicketLanguage(String language) {
        if (language.equalsIgnoreCase("en") || language.equalsIgnoreCase("english")) {
            language = "English";
        } else {
            language = "Bahasa Indonesia";
        }
        if (driverTools.waitForElementToPresence(getETicketElement(language)).isPresent())
            driverTools.clickElementByCoordinate(getETicketElement(language));
    }

    @Override
    public boolean isETicketLanguageOptionVisible(String language) {
        String normalized = (language.equalsIgnoreCase("en") || language.equalsIgnoreCase("english"))
                ? "English"
                : "Bahasa Indonesia";
        return driverTools.waitForElementToAppear(getETicketElement(normalized), 10).isPresent();
    }

    @Override
    public void chooseDefaultAppPdf() {
        //FIXME: not implemented, currently only support for android
    }

    @Override
    public String getPDFContentFile() {
        driverTools.waitForElementToPresence(PDF_CONTENT, 30).orElseThrow();
        String pdfContent = String.join(" ", driverTools.getTexts(PDF_CONTENT_TEXTS));
        if (pdfContent.isEmpty()) {
            logger.log("PDF content is empty, fetching from an alternative PDF_CONTENT element");
            pdfContent = driverTools.getText(PDF_CONTENT).orElseThrow();
        }
        return pdfContent;
    }

    @Override
    public boolean isOrderRescheduledBannerExist() {
        WebElement reschduleBannner = driverTools.waitForElementToAppear(RESCHEDULE_BANNER).orElseThrow();
        return driverTools.isElementDisplayed(reschduleBannner);
    }

    public boolean isMainETiketCardDisplayed() {
        return driverTools.waitForElementToAppear(MAIN_ETICKET).orElseThrow().isDisplayed();
    }

    @Override
    public boolean isETiketAndReceiptCardDisplayed() {
        return driverTools.swipeToElement(ETICKET_BUTTON, Swipe.UP, 10).orElseThrow().isDisplayed();
    }

    @Override
    public String getFullOrderId() {
        WebElement orderId = driverTools.waitForElementToAppear(ORDER_ID, 10).orElseThrow();
        String orderIDText = driverTools.getText(orderId);
        return orderIDText;
    }

    @Override
    public boolean isTrainDetailCorrect(String originStation, String destinationStation, String totalPrice) {
        boolean isOriginStationShown = driverTools.swipeToElement(getStationLocator(originStation), Swipe.UP, 2).isPresent();
        boolean isDestinationStationShown = driverTools.swipeToElement(getStationLocator(destinationStation), Swipe.UP, 2).isPresent();

        logger.log("is origin same: " + isOriginStationShown);
        logger.log("is destination same: " + isDestinationStationShown);

        WebElement totalPriceElement = driverTools.swipeToElement(TOTAL_PAYMENT, Swipe.UP, 3).orElseThrow();
        String actualTotalPrice = driverTools.getText(totalPriceElement);
        int actualPrice = CoreFunctions.getIntFromString(actualTotalPrice).orElseThrow();
        int expectedPrice = CoreFunctions.getIntFromString(totalPrice).orElseThrow();
        logger.log("expected total price: " + expectedPrice);
        logger.log("actual total price: " + actualPrice);
        boolean isComparePrice = actualPrice == expectedPrice;

        logger.log("is price same: " + isComparePrice);

        return isDestinationStationShown && isOriginStationShown && isComparePrice;
    }

    private Locator getStationLocator(String stationCode) {
        return new Locator(stationCode, AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Details']//following::XCUIElementTypeStaticText[contains(@label,'" + stationCode + "') and @visible='true']"));
    }

    public String getCarRentalName() {
        String carRentalName = driverTools.getText(ORDER_DETAIL_HEADER_TITLE).orElseThrow();
        logger.log("Car Rental Name: " + carRentalName);
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
        driverTools.swipeToElement(CAR_RENTAL_PASSENGER_FULL_NAME_TEXT, Swipe.UP, 3).orElseThrow();
        String carRentalPassengerFullName = driverTools.getText(CAR_RENTAL_PASSENGER_FULL_NAME_TEXT).orElseThrow();
        logger.log("Car Rental Passenger Full Name: " + carRentalPassengerFullName);
        return carRentalPassengerFullName;
    }

    @Override
    public String getCarRentalPassengerPhoneNumber() {
        driverTools.swipeToElement(CAR_RENTAL_PASSENGER_PHONE_NUMBER_TEXT, Swipe.UP, 3).orElseThrow();
        String carRentalPassengerPhoneNumber = driverTools.getText(CAR_RENTAL_PASSENGER_PHONE_NUMBER_TEXT).orElseThrow();
        logger.log("Car Rental Passenger Phone Number: " + carRentalPassengerPhoneNumber);
        return carRentalPassengerPhoneNumber;
    }

    @Override
    public boolean isInOrderDetailPage() {
        return driverTools.waitForElementToAppear(ORDER_ID, 40).isPresent();
    }

    @Override
    public boolean isRescheduleDetailCorrect(String refundDetailDate) {
        return false;//FIXME
    }

    @Override
    public String getRefundDetailDate() {
        return "";//FIXME
    }

    @Override
    public void openRefundAndRescheduleInfo() {
        driverTools.waitForElementToAppear(ORDER_ID, 30);
        driverTools.swipeToElement(REFUND_AND_RESCHEDULE_INFO, Swipe.UP, 8).orElseThrow();
        driverTools.swipeToElement(REFUND_AND_RESCHEDULE_DETAIL_BUTTON, Swipe.UP, 3).ifPresent(driverTools::click);
    }

    @Override
    public String getOrderRefundPolicyValue(String totalPrice) {
        WebElement refundValueElement = driverTools.waitForElementToAppear(REFUND_VALUE, 10).orElseThrow();
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
        return refundValue;
    }

    @Override
    public void closeRefundAndRescheduleInfo() {
        driverTools.click(REFUND_AND_RESCHEDULE_CLOSE_BUTTON);
    }

    @Override
    public void clickRefundButton() {

        if(driverTools.isElementDisplayed(HIDE_POPUP)){
            driverTools.click(HIDE_POPUP);
        }
        driverTools.swipeToElement(REFUND_BUTTON, Swipe.UP, 10).orElseThrow();
        driverTools.clickElementByCoordinate(REFUND_BUTTON);
        if (driverTools.waitForElementToPresence(START_REFUND_BUTTON, 2).isPresent()) {
            driverTools.click(START_REFUND_BUTTON);
        }
        RefundPage<T> refundPage = RefundPage.getInstance(session);
        refundPage.continueToRefundPopup();
    }

    @Override
    public void clickAndOpenRescheduleNudgeButton() {
        WebElement refundBtn = driverTools.swipeToElement(REFUND_BUTTON, Swipe.UP, 10).orElseThrow();
        if (driverTools.isElementDisplayed(refundBtn)) {
            driverTools.waitForElementToAppear(REFUND_BUTTON).ifPresent(driverTools::click);
        } else {
            driverTools.swipeUp();
            driverTools.click(REFUND_BUTTON);
        }
        driverTools.waitForElementToDisappear(REFUND_BUTTON);
    }

    @Override
    public void clickRefundButtonWithoutSkip(String orderId) {
        driverTools.swipeToElement(REFUND_BUTTON, Swipe.UP, 10).orElseThrow();
        driverTools.swipeUp();
        driverTools.click(REFUND_BUTTON);
    }

    @Override
    public void goToTotalPayment() {
        driverTools.swipeToElement(TTD_TOTAL_PAYMENT, Swipe.UP, 10).orElseThrow();
    }

    @Override
    public String getTotalPayment() {
        return driverTools.getText(TTD_TOTAL_PAYMENT).orElseThrow();
    }

    @Override
    public Double getTotalPaymentPriceDetails() {
        return CoreFunctions.convertPrice(driverTools.getText(TOTAL_PRICE_DETAILS).orElseThrow());
    }

    @Override 
    public String getRefundInProcessAmount(String orderId) {
        return getTotalPaymentHotel();
    }

    @Override
    public String getTotalPaymentHotel() {
        driverTools.swipeToElement(TOTAL_PAYMENT, Swipe.UP, 10);
        return CoreFunctions.normalizePrice(driverTools.getText(TOTAL_PAYMENT).orElseThrow());
    }

    @Override
    public String getTTDProductTitle() {
        WebElement productTitle = driverTools.swipeToElement(TTD_PRODUCT_TITLE, Swipe.UP, 5).orElseThrow();
        return driverTools.getText(productTitle);
    }

    @Override
    public boolean isEditGuestNameButtonDisplayed() {
        // EDIT_BOOKING_BUTTON also matches the special request edit button, so 2 matches means
        // the guest name edit button is present alongside it; only 1 match means it's absent.
        driverTools.swipeToElement(EDIT_BOOKING_BUTTON, Swipe.DOWN, 5);
        int editButtonCount = driverTools.elements(EDIT_BOOKING_BUTTON).map(List::size).orElse(0);
        logger.log("the edit button count is " + editButtonCount);
        return editButtonCount >= 2;
    }

    @Override
    public void clickEditGuestName() {
        driverTools.swipeToElement(EDIT_BOOKING_BUTTON, Swipe.DOWN, 5);
        driverTools.swipeDown();
        driverTools.click(EDIT_BOOKING_BUTTON);
        if (driverTools.isElementDisplayed(EDIT_GUEST_NAME_POPUP)) driverTools.click(EDIT_GUEST_NAME_POPUP);
    }

    @Override
    public boolean checkRoomDetail(List<String> guestNames) {
        driverTools.waitForElementToPresence(GUEST_FULL_NAME_XPATH).orElseThrow();
        List<WebElement> guestNamesElement = driverTools.elements(GUEST_FULL_NAME_XPATH).orElseThrow();
        boolean isGuestNameEqual = false;
        int index = 0;
        for (WebElement guestNameElement : guestNamesElement) {
            String guestNameDisplayed = driverTools.getText(guestNameElement);
            String guestNameExpected = guestNames.get(index);

            isGuestNameEqual = guestNameDisplayed.contains(guestNameExpected);
            logger.log("Displayed Guest Name " + index + 1 + " : " + guestNameDisplayed + "\n" +
                    "Expected : " + guestNameExpected);

            if (!isGuestNameEqual) {
                break;
            } else {
                index++;
            }
        }
        return isGuestNameEqual;
    }

    @Override
    public boolean checkDetailsEditPage(String orderID, String hotelName, List<String> guestNames) {
        boolean oldVersion = driverTools.waitForElementToAppear(ORDER_ID_XPATH).isPresent();
        if (oldVersion) {
            String[] orderIdTextArray = driverTools.getText(ORDER_ID_XPATH).orElseThrow().split(" ");
            String orderIdText = orderIdTextArray[orderIdTextArray.length - 1];
            boolean isOrderIdEqual = orderIdText.equals(orderID);
            logger.log("Displayed Order ID : " + orderIdText + "\n" +
                    "Expected : " + orderID);

            boolean isHotelNameEqual = true;

            List<WebElement> guestNamesElement = driverTools.elements(GUEST_NAME_EDIT_PAGE).orElseThrow();
            boolean isGuestNameEqual = false;
            int index = 0;
            for (WebElement guestNameElement : guestNamesElement) {
                String guestNameDisplayed = driverTools.getText(guestNameElement);
                String guestNameExpected = guestNames.get(index);

                isGuestNameEqual = guestNameDisplayed.contains(guestNameExpected);
                logger.log("Displayed Guest Name " + index + 1 + " : " + guestNameDisplayed + "\n" +
                        "Expected : " + guestNameExpected);

                if (!isGuestNameEqual) {
                    break;
                } else {
                    index++;
                }
            }

            return isOrderIdEqual && isHotelNameEqual && isGuestNameEqual;
        } else {
            logger.softWarning("The new version is visible");
            return true;
        }
    }

    @Override
    public void editGuestName() {
        boolean oldVersion = driverTools.waitForElementToPresence(EDIT_GUEST_NAME_EDIT_PAGE).isPresent();
        if (oldVersion) {
            List<WebElement> editGuestNamesElement = driverTools.elements(EDIT_GUEST_NAME_EDIT_PAGE).orElseThrow();
            int guestCount = editGuestNamesElement.size();

            for (int i = 0; i < guestCount; i++) {
                driverTools.click(getEditGuestNameLocator(i + 1));
                driverTools.clear(GUEST_NAME_TEXTFIELD_EDIT_PAGE);
                driverTools.sendKeys(GUEST_NAME_TEXTFIELD_EDIT_PAGE, CoreFunctions.randomName() + " updated autguest");
                driverTools.click(SAVE_BUTTON_FORM_EDIT_PAGE);
            }

            driverTools.click(SAVE_BUTTON_EDIT_PAGE);
            driverTools.click(SAVE_ALERT_POPUP_EDIT_PAGE);
        } else {
            driverTools.waitForElementToPresence(GUEST_DETAILS_PAGE).orElseThrow();
            List<WebElement> fullNameField = driverTools.elements(FULL_NAME_FIELD).orElseThrow();
            for (int i = 0; i < fullNameField.size(); i++) {
                if (driverTools.waitForElementToAppear(randomSalutationLocator(), 5).isPresent()) {
                    List<WebElement> salutationButton = driverTools.elements(randomSalutationLocator()).orElseThrow();
                    driverTools.click(salutationButton.get(i));
                }
                fullNameField = driverTools.elements(FULL_NAME_FIELD).orElseThrow();
                driverTools.clear(fullNameField.get(i));
                driverTools.sendKeys(fullNameField.get(i), CoreFunctions.randomName() + " updated autguest");
                driverTools.hideKeyboard(1);
            }
            driverTools.click(SAVE_BUTTON);
            driverTools.click(SUBMIT_CONFIRM);
            driverTools.waitForElementToAppear(VERIFY_PHONE_NUMBER_FIELD).orElseThrow();
            List<WebElement> otpField = driverTools.elements(VERIFY_PHONE_NUMBER_FIELD).orElseThrow();
            int otpSize = otpField.size();
            logger.log("otpField get size: " + otpSize);
            for (int i = 0; i < otpSize; i++) {
                driverTools.sendKeysKeyboard(otpField.get(i), String.valueOf(i + 1));
            }
            driverTools.click(SEE_RESERVATION_DETAIL_BUTTON);
        }
    }

    private Locator randomSalutationLocator() {
        Locator[] salutationLocatorList = {MR_SALUTATION_CHECKBOX, MRS_SALUTATION_CHECKBOX, MS_SALUTATION_CHECKBOX};
        return getRandomObject(salutationLocatorList);
    }

    private Locator getEditGuestNameLocator(int i) {
        return new Locator("EDIT_GUEST_NAME_" + i, AppiumBy.xpath("(//XCUIElementTypeImage[@name='icEdit'])[" + i + "]"));
    }

    @Override
    public boolean isGuestNameUpdated() {
        boolean isGuestNameUpdated = false;
        driverTools.waitForElementToAppear(GUEST_FULL_NAME_XPATH, 10);
        List<WebElement> guestNameElements = driverTools.elements(GUEST_FULL_NAME_XPATH).orElseThrow();
        if (guestNameElements.size() == 0) {
            logger.fail("guestNameElements has 0 size");
            return false;
        }

        int index = 0;
        for (WebElement guestNameElement : guestNameElements) {
            String displayedGuestName = driverTools.getText(guestNameElement);
            isGuestNameUpdated = displayedGuestName.contains("updated");
            logger.log("Displayed Guest Name " + index + 1 + " : " + displayedGuestName);

            if (!isGuestNameUpdated) {
                break;
            } else {
                index++;
            }
        }

        return isGuestNameUpdated;
    }

    @Override
    public boolean findOnPDF(String textToFind) {
        Locator pdfContent = new Locator("PDF Content", AppiumBy.xpath("(//*[@name='QLPDFViewControllerViewAccessibilityIdentifier']//*[contains(@label,'" + textToFind + "')])[1]"));
        return driverTools.isElementDisplayed(pdfContent);
    }

    @Override
    public boolean verifyBusPriceDetailBreakDown() {
        driverTools.waitForPresenceElementsList(PRICE_DETAIL_COMPONENTS).orElseThrow();
        List<String> priceComponentsInPriceDetail = driverTools.getTexts(PRICE_DETAIL_COMPONENTS);
        logger.log("number of price component present : " + priceComponentsInPriceDetail.size());
        String totalPrice = driverTools.getText(TOTAL_PRICE_DETAILS).orElseThrow();
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
        driverTools.click(BOTTOMSHEET_CLOSE_BUTTON);
        return true;
    }


    @Override
    public String getFullBlackoutDate(String startBlackoutDate, String endBlackoutDate) {
        return CoreFunctions.getFullBlackoutDate(startBlackoutDate, endBlackoutDate);
    }

    private Locator getPassengerTypeTrainLocator(String type) {
        return new Locator("PASSENGER_TYPE_DETAIL_BTN", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'1. ')]//following-sibling::*//XCUIElementTypeStaticText[contains(@label,'" + type + "')]"));
    }

    @Override
    public void checkPassengerDetailsTrain(TrainData data) {
        logger.log("Passenger details");
        driverTools.swipeToElement(GUEST_AND_PASSENGER_DETAIL_BTN, Swipe.UP, 4).orElseThrow();
        driverTools.click(GUEST_AND_PASSENGER_DETAIL_BTN);

        String name = driverTools.getText(driverTools.element(GUEST_NAME_TRAIN_PASSENGER_DETAIL).orElseThrow());
        logger.log("Name order detail -> " + name);
        boolean isNameCorrect = name.equalsIgnoreCase(data.guestName);
        logger.log("is passenger name correct -> " + isNameCorrect);

        String seat = driverTools.getText(driverTools.element(SEAT_TRAIN_PASSENGER_DETAIL).orElseThrow());
        logger.log("Seat order detail -> " + seat);

        seat = extractSeatNumber(seat);
        boolean isSeatCorrect = seat.equalsIgnoreCase(extractSeatNumber(data.seat));
        Assertion.assertThat("Verify passenger seat order detail { " + seat + " } seat payment page {" + extractSeatNumber(data.seat) + " }", seat, containsString(extractSeatNumber(data.seat)));

        boolean isPassengerType = driverTools.isElementDisplayed(getPassengerTypeTrainLocator(data.guestType));
        logger.log("is passenger type correct -> " + isPassengerType);

        driverTools.click(CLOSE_GUEST_DETAIL);
        Assertion.assertThat("is passenger name correct", isNameCorrect, is(true));
        Assertion.assertThat("is passenger seat correct", isSeatCorrect, is(true));
        Assertion.assertThat("is passenger type correct", isPassengerType, is(true));
    }

    private Locator getDateAndTimeLocatorTrain(String station) {
        return new Locator("DATE_TIME_FOR_" + station, AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'" + station + "')]//preceding-sibling::XCUIElementTypeStaticText"));
    }

    private Locator getStationLocatorTrain(String station) {
        return new Locator("DATE_TIME_FOR_" + station, AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'" + station + "')]"));
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
        WebElement dateAndTimeOrigin = driverTools.swipeToElement(getDateAndTimeLocatorTrain(station), Swipe.UP, 3).orElseThrow();
        String date = removeDayOfWeek(driverTools.getText(dateAndTimeOrigin));
        return date;
    }

    private boolean checkDateTrainDetailTrain(TrainData data) {
        String dateAndTimeOrigin = getDateTimeTrain(data.departureStation.split(" ")[0]);
        logger.log("origin date and time -> " + dateAndTimeOrigin);
        String dateAndTimeDestination = getDateTimeTrain(data.arrivalStation.split(" ")[0]);
        logger.log("destination date and time -> " + dateAndTimeDestination);

        String dateAndTimeOriginFromPaymentPage = data.departureDate + " " + data.departYear + " - " + data.departureTime;
        logger.log("origin date and time from payment page -> " + dateAndTimeOriginFromPaymentPage);
        String dateAndTimeDestinationFromPaymentPage = data.arrivalDate + " " + data.arrivalYear + " - " + data.arrivalTime;
        logger.log("destination date and time from payment page -> " + dateAndTimeDestinationFromPaymentPage);

        Assertion.assertThat("Verify date time origin equal to date time origin in payment page", dateAndTimeOrigin, equalTo(dateAndTimeOriginFromPaymentPage));
        Assertion.assertThat("Verify date time destination equal to date time destination in payment page", dateAndTimeOrigin, equalTo(dateAndTimeOriginFromPaymentPage));
        return true;
    }

    private boolean checkStationDetailsTrain(TrainData data) {
        String originStation = driverTools.getText(driverTools.element(getStationLocator(data.departureStation.split(" ")[0])).orElseThrow());
        logger.log("origin station order detail-> " + originStation);
        String destinationStation = driverTools.getText(driverTools.element(getStationLocator(data.arrivalStation.split(" ")[0])).orElseThrow());
        logger.log("destination station order detail-> " + destinationStation);

        boolean isOriginStationCorrect = originStation.toLowerCase().contains(data.departureStation.toLowerCase());
        logger.log("origin station correct -> " + isOriginStationCorrect);
        boolean isDestinationStationCorrect = destinationStation.toLowerCase().contains(data.arrivalStation.toLowerCase());
        logger.log("destination station correct -> " + isDestinationStationCorrect);

        return isOriginStationCorrect && isDestinationStationCorrect;
    }

    @Override
    public boolean checkTrainDetails(TrainData data) {
        boolean isBookingCodeCorrect;
        if (Configs.ENVIRONMENT != Environment.PROD) {
            isBookingCodeCorrect = checkBookingCode();
        } else {
            logger.log("booking code is not checked in production environment");
            isBookingCodeCorrect = true;
        }
        boolean isDateCorrect = checkDateTrainDetailTrain(data);
        boolean isStationDetailCorrect = checkStationDetailsTrain(data);
        return isDateCorrect && isStationDetailCorrect && isBookingCodeCorrect;
    }

    private boolean checkBookingCode() {
        WebElement bookingCodeElement = driverTools.swipeToElement(BOOKING_CODE_ORDER_DETAIL, Swipe.UP, 4).orElseThrow();
        String bookingCode = driverTools.getText(bookingCodeElement);
        logger.log("booking code order detail -> " + bookingCode);
        boolean isBookingCodeCorrect = !bookingCode.isEmpty();
        logger.log("is booking code correct -> " + isBookingCodeCorrect);
        return isBookingCodeCorrect;
    }

    @Override
    public boolean verifyTotalPaymentTrain(String totalPrice) {
        WebElement totalPaymentElement = driverTools.swipeToElement(TOTAL_PAYMENT_TRAIN, Swipe.UP, 4).orElseThrow();
        double totalPaymentDetailPage = convertPrice(driverTools.getText(totalPaymentElement));
        logger.log("total Payment detail page : " + totalPaymentDetailPage);
        double totalPricePaymentPage = convertPrice(totalPrice);
        logger.log("total Payment payment page : " + totalPricePaymentPage);
        return totalPaymentDetailPage == totalPricePaymentPage;
    }

    @Override
    public boolean verifyTotalPaymentTrain(TrainData data, String totalPrice) {
        return verifyTotalPaymentTrain(totalPrice);
    }

    @Override
    public void goToRefundDetail(String orderId) {
        WebElement refundDetail = driverTools.swipeToElement(REFUND_DETAIL_BUTTON, Swipe.DOWN, 3).orElseThrow();
        driverTools.click(refundDetail);
    }

    @Override
    public String getTimeSlot() {
        return driverTools.getValueAttribute(DATE_TIME_TEXT, "label");
    }

    @Override
    public void clickChatBtn() {
        WebElement chatBtn = driverTools.waitForElementToAppear(CHAT_BTN).orElseThrow();
        driverTools.click(chatBtn);
    }

    @Override
    public boolean verifyBusDepartureSection() {
        String departureName = driverTools.getText(BUS_DEPARTURE_NAME).orElseThrow();
        Assertion.assertThat("Verify order departure bus name exist", departureName, not(blankOrNullString()));
        String departureDate = driverTools.getText(BUS_DEPARTURE_DATE).orElseThrow();
        Assertion.assertThat("Verify order departure date exist", departureDate, not(blankOrNullString()));
        String arrivalDate = driverTools.getText(BUS_RETURN_DATE).orElseThrow();
        Assertion.assertThat("Verify order return date exist", arrivalDate, not(blankOrNullString()));
        List<String> facility = driverTools.getTexts(BUS_FACILITY);
        Assertion.assertThat("Verify facilities present", facility, Every.everyItem(not(blankOrNullString())));
        return true;
    }

    @Override
    public void openBusTripDetail() {
        driverTools.swipeToElement(BUS_SEE_TRIP_DETAIL_BTN, Swipe.UP, 5).orElseThrow();
        driverTools.click(BUS_SEE_TRIP_DETAIL_BTN);
    }

    @Override
    public boolean verifyTripDetailPresent() {
        String departureName = driverTools.getText(BUS_DEPARTURE_NAME_TRIP_DETAIL).orElseThrow();
        Assertion.assertThat("Verify order departure bus name exist", departureName, not(blankOrNullString()));
        List<String> stopLocation = driverTools.getTexts(BUS_DEPARTURE_LOCATIONS_TRIP_DETAIL);
        Assertion.assertThat("Verify stop location present", stopLocation, Every.everyItem(not(blankOrNullString())));
        List<String> facility = driverTools.getTexts(BUS_FACILITIES_TRIP_DETAIL);
        Assertion.assertThat("Verify facilities present", facility, Every.everyItem(not(blankOrNullString())));
        driverTools.click(BOTTOMSHEET_CLOSE_BUTTON);
        return true;
    }

    public String getBusPassengerDetails() {
        driverTools.swipeToElement(BUS_PASSENGER_DETAIL, Swipe.UP, 5).orElseThrow();
        String[] passengerDetails = driverTools.elements(BUS_PASSENGER_DETAIL).orElseThrow().stream()
                .map(element -> element.getAttribute("label")).toArray(String[]::new);
        logger.log("Bus passenger details : " + String.join(", ", passengerDetails));
        return String.join(", ", passengerDetails);
    }

    @Override
    public String getBusDepartureDate(String orderID, String orderHash, String orderDetail) {
        driverTools.swipeToElement(BUS_DEPARTURE_DATE, Swipe.UP, 5).orElseThrow();
        return driverTools.getText(BUS_DEPARTURE_DATE).orElseThrow();
    }

    @Override
    public String getBusReturnDate(String orderID, String orderHash, String orderDetail) {
        driverTools.swipeToElement(BUS_RETURN_DATE, Swipe.UP, 5).orElseThrow();
        return driverTools.getText(BUS_RETURN_DATE).orElseThrow();
    }

    @Override
    public boolean isETiketAndReceiptCardDisplayedNative() {
        return driverTools.isElementDisplayed(MAIN_ETICKET_NATIVE);
    }

    @Override
    public Boolean checkPriceDetailIsIn(String currency) {
        goToTotalPayment();
        String ttdTotalPrice = driverTools.getText(TTD_TOTAL_PAYMENT).orElseThrow();

        if (!ttdTotalPrice.contains(currency)) {
            return false;
        }

        driverTools.scrollElementToCenter(PRICE_DETAIL_CHEVRON);
        driverTools.click(PRICE_DETAIL_CHEVRON);
        List<String> ttdPriceTextList = driverTools.getTexts(PRICE_DETAIL_TEXTS);

        driverTools.click(BOTTOMSHEET_CLOSE_BUTTON);

        logger.log("ttdPriceTextList -> " + ttdPriceTextList);
        for (String e : ttdPriceTextList) {
            if (e.matches(".*\\d.*.,") && !(e.contains(")"))) {
                if (!e.contains(currency)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public Boolean isShareReceiptButtonDisplayed() {
        return driverTools.isElementDisplayed(SHARE_RECEIPT_BUTTON);
    }

    @Override
    public Boolean isReceiptButtonWork(String currency) {
        driverTools.swipeToElement(SHARE_RECIPT_, Swipe.UP, 5);
        driverTools.click(SHARE_RECIPT_);
        driverTools.switchToNative();
        boolean result = driverTools.waitForElementToAppear(NATIVE_SHARE_POPUP).isPresent();
        driverTools.back();
        return result;
    }

    @Override
    public Boolean isRefundButtonDisplayed() {
        return driverTools.swipeToElement(REFUND_BUTTON, Swipe.UP, 3).isPresent();
    }

    public String getBusSeatNoDetail() {
        driverTools.swipeToElement(BUS_SEAT_NO_DETAIL, Swipe.UP, 7);
        String[] seatNumbersSecondPage = getSeatNumbersFromPage();
        String seatNo = String.join(", ", seatNumbersSecondPage);
        logger.log("Bus seat no detail: " + seatNo);
        return seatNo;
    }

    private String[] getSeatNumbersFromPage() {
        List<WebElement> busSeatNoDetailElements = driverTools.waitForPresenceElementsList(BUS_SEAT_NO_DETAIL).orElseThrow();
        return busSeatNoDetailElements.stream()
                .map(element -> element.getAttribute("label")).toArray(String[]::new);
    }

    @Override
    public boolean checkAddon(String addOnName, int addOnQty) {
        // The add-on name comes from the catalogue API and can contain
        // apostrophes (e.g. "Chef's Special"); always wrap it through
        // CoreFunctions.toXPathLiteral() so the generated XPath stays valid.
        String qtyAndName = CoreFunctions.toXPathLiteral(addOnQty + " " + addOnName);
        Locator addOnLocator = new Locator(addOnName, AppiumBy.xpath(
                "//*[contains(@label,'ons') or contains(@label,'Ekstra')]"
                        + "//following::*[contains(@label," + qtyAndName + ")]"));
        boolean isAddOnAvailable = driverTools.swipeToElement(addOnLocator, Swipe.UP, 5).isPresent();
        driverTools.swipeToElement(SEE_ETICKET_BUTTON, Swipe.DOWN, 5).orElseThrow();
        return isAddOnAvailable;
    }

    private static Locator ROOM_TYPE_NAME(int roomType) {
        return new Locator("ROOM_TYPE_NAME_" + roomType, AppiumBy.xpath("(//*[(contains(@label,' Room') or contains(@label,' Kamar')) and contains(@label,' • ')]//preceding-sibling::XCUIElementTypeStaticText)[" + roomType + "]"));
    }

    private static Locator ROOM_TYPE_ADDONS(int roomType) {
        return new Locator("ROOM_TYPE_ADDONS_" + roomType, AppiumBy.xpath("(//*[contains(@label,'Add-ons') or contains(@label,'Fasilitas Ekstra')]//following-sibling::XCUIElementTypeOther//XCUIElementTypeStaticText)[" + roomType + "]"));
    }

    @Override
    public boolean checkAddon(String addOnName, int itemPerRoom, HashMap<Integer, String> roomName) {
        int totalExpectedRoomType = roomName.size();
        driverTools.waitForElementToAppear(ROOM_TYPE);
        Assertion.assertThat("Total Room Type should be equal with the selected room list", driverTools.elements(ROOM_TYPE).orElseThrow().size(), is(totalExpectedRoomType));
        for (int i = 1; i <= totalExpectedRoomType; i++) {
            Assertion.assertThat(i + " Room Name should be contains the selected room list", driverTools.getText(ROOM_TYPE_NAME(i)).orElseThrow(), containsString(roomName.get(i)));
            Assertion.assertThat(i + " Addons should be contains the selected addons booking form", driverTools.getText(ROOM_TYPE_ADDONS(i)).orElseThrow(), containsString(itemPerRoom + " " + addOnName));
        }
        return true;
    }

    public String getTrainSeatData() {
        driverTools.click(GUEST_AND_PASSENGER_DETAIL_BTN);
        if (!driverTools.isElementDisplayed(SEAT_TRAIN_PASSENGER_DETAIL))
            driverTools.click(ARROW_TRAIN_PASSENGER_DETAIL);
        String seat = driverTools.getText(driverTools.element(SEAT_TRAIN_PASSENGER_DETAIL).orElseThrow());
        driverTools.click(CLOSE_TRAIN_PASSENGER_DETAIL);
        return seat;
    }

    @Override
    public void choosNewTrainSeatWagoon() {
        driverTools.click(OPEN_CHANGE_SEAT_SELECTION);
        driverTools.waitForElementToAppear(TRAIN_SEAT_SELECTION_CONTAINER, 10);
        driverTools.swipeByDistance(TRAIN_SEAT_SELECTION_CONTAINER, Swipe.LEFT, 0.75);
        boolean isEmptySeatPresent = driverTools.waitForElementToAppear(TRAIN_EMPTY_SEATS_IN_CHANGE_SEAT_SELECTION, 7).isPresent();
        if (!isEmptySeatPresent) driverTools.swipeByDistance(TRAIN_SEAT_SELECTION_CONTAINER, Swipe.LEFT, 0.75);
        List<WebElement> trainsSeatEleements = driverTools.elements(TRAIN_EMPTY_SEATS_IN_CHANGE_SEAT_SELECTION).orElseThrow();
        int lowerLimit = 0;
        if (trainsSeatEleements.size() > 1) lowerLimit = 1;
        int random = CoreFunctions.generateRandomInt(lowerLimit, trainsSeatEleements.size());
        driverTools.click(trainsSeatEleements.get(random));
        driverTools.click(CONFIRM_NEW_TRAIN_SEAT);
    }

    @Override
    public void clickChangeTrainSeat() {
        driverTools.swipeUp();
        driverTools.clickElementByCoordinate(CHANGE_TRAIN_SEAT_BTN);
    }

    @Override
    public String getTrainSeatFromConfirmationPage() {
        String seat = driverTools.getText(TRAIN_SEAT_FROM_CHANGE_TRAIN_SEAT_MENU).orElseThrow();
        logger.log("Train seat value in change seat menu : ");
        return seat;
    }

    public void continueToPaymentTrainChangeSeat() {
        driverTools.swipeToElement(CONTINUE_TO_PAYMENT_FROM_CHANGE_SEAT, Swipe.UP, 3);
        driverTools.click(CONTINUE_TO_PAYMENT_FROM_CHANGE_SEAT);
        driverTools.waitForElementToAppear(CONFIRM_CONTINUE_PAYMENT_TRAIN_CHANGE_SEAT, 5);
        driverTools.click(CONFIRM_CONTINUE_PAYMENT_TRAIN_CHANGE_SEAT);
    }

    @Override
    public void clickBackFromPdf() {
        driverTools.back();
        boolean isClosed = false;
        if (isPdfContentOpen()) {
            if (driverTools.isElementDisplayed(CLOSE_NATIVE_SHARE_BTM_SHEET)) {
                driverTools.click(CLOSE_NATIVE_SHARE_BTM_SHEET);
                isClosed = true;
            } else if (driverTools.waitForElementToPresence(ORDER_ID_PDF, 5).isPresent()) {
                if (driverTools.isElementDisplayed(BACK_BUTTON_ETICKET))
                    driverTools.clickElementByCoordinate(BACK_BUTTON_ETICKET);
                else
                    driverTools.clickElementByCoordinate(BACK_BUTTON_PDF);
                isClosed = true;
            }
        }
        if (!isClosed) {
            try {
                driverTools.switchToWebView();
                if (driverTools.isElementDisplayed(PDF_CONTENT) || driverTools.isElementDisplayed(CHANGE_LANGUAGE_CONTAINER)) {
                    driverTools.click(CLOSE_ETICKE_BTM_SHEET_WEBVIEW);
                }
                driverTools.switchToNative();
            } catch (Exception e) {
                logger.log("Not in webview");
            }
        }
    }

    @Override
    public String getTotalPrice() {
        driverTools.swipeToElement(TTD_TOTAL_PAYMENT, Swipe.UP, 10);
        return driverTools.getText(TTD_TOTAL_PAYMENT).orElseThrow();
    }

    private Locator getAddProductSectionLocator(String orderDetailSectionTitle) {
        return new Locator("ADD_OTHER_PRODUCT_SECITON", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='page_module_title' and contains(@label,'" + orderDetailSectionTitle + "')]"));
    }

    private Locator getAddProductBannerLocator(String orderDetailSectionTitle) {
        return new Locator("ADD_OTHER_PRODUCT_BANNER", AppiumBy.xpath("//*[@name='page_module_title' and contains(@label,'" + orderDetailSectionTitle + "')]/../../../..//following-sibling::*//*[contains(@name,'bn_grid')]"));
    }

    @Override
    public boolean verifyCrossSellRedirection(String orderDetailSectionTitle, List<String> expectedUrl) {
        Locator addProductSectionLocator = getAddProductSectionLocator(orderDetailSectionTitle);
        driverTools.swipeToElement(addProductSectionLocator, Swipe.UP, 3);
        driverTools.waitForElementToAppear(OK_BUTTON, 2).ifPresent(driverTools::click);
        driverTools.swipeToElement(addProductSectionLocator, Swipe.UP, 3);
        Assertion.assertThat("Verify add product section present", driverTools.isElementDisplayed(addProductSectionLocator), is(true));
        //Check chips
        Locator addProductBannerLocator = getAddProductBannerLocator(orderDetailSectionTitle);
        driverTools.swipeToElement(addProductBannerLocator, Swipe.UP, 2);
        List<WebElement> addProductBannerElement = driverTools.elements(addProductBannerLocator).orElseThrow();
        driverTools.click(addProductBannerElement.get(0));
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        String actualUrl = driverTools.getCurrentURL().replace("https://m.", "");
        String expectedUrlIndex0 = expectedUrl.get(0).replace("https://www.", "");
        driverTools.switchToNative();
        Assertion.assertThat("Verify url is match", actualUrl, containsString(expectedUrlIndex0));
        return true;
    }

    @Override
    public boolean isShareFileNameCorrect(String vertical, String orderId, String lang) {
        //click share button and then share btm sheet will appear
        vertical = vertical.substring(0, 1).toUpperCase() + vertical.substring(1);
        String fileName1 = "E-Receipt-" + vertical + "-ID-" + orderId;
        String fileName2 = "E-Receipt-" + vertical + "-EN-" + orderId;
        logger.log("expected filenames: " + fileName1 + ", " + fileName2);
        Locator fileNameLocator1 = new Locator("FILE_NAME1", AppiumBy.xpath("//*[contains(@label,'" + fileName1 + "')]"));
        Locator fileNameLocator2 = new Locator("FILE_NAME2", AppiumBy.xpath("//*[contains(@label,'" + fileName2 + "')]"));
        boolean isElementDsiplayed = driverTools.waitForAnyLocator(10, fileNameLocator1, fileNameLocator2).isPresent();
        if (!isElementDsiplayed) {
            String alternativeFileName1 = ("Receipt--EN-" + orderId).toLowerCase();
            String alternativeFileName2 = ("Receipt--ID-" + orderId).toLowerCase();
            logger.log("expected filename not found, checking alternative filename: " + alternativeFileName1 + " or " + alternativeFileName2);
            Locator alternativeFileNameLocator = new Locator("ALTERNATIVE_FILE_NAME", AppiumBy.xpath("//*[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + alternativeFileName1 + "') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + alternativeFileName2 + "')]"));
            isElementDsiplayed = driverTools.waitForElementToAppear(alternativeFileNameLocator, 5).isPresent();
        }
        return isElementDsiplayed;
    }

    @Override
    public void clickEticketFromHeader() {
        driverTools.swipeToElement(SEE_ETICKET_BUTTON, Swipe.DOWN, 10);
        driverTools.clickElementByCoordinate(SEE_ETICKET_BUTTON);
    }

    @Override
    public void clickEticketFromHeader(String orderId) { //Android only
        clickEticketFromHeader();
    }

    @Override
    public boolean isPdfContentOpen() {
        return driverTools.waitForElementToPresence(ORDER_ID_PDF, 7).isPresent();
    }

    @Override
    public boolean verifyShareEticketBtmSheet(String vertical, String lang, String orderID) {
        Locator fileNameLocator = new Locator("FILE_NAME_LOCATOR", AppiumBy.xpath("//*[contains(@label,'E-Voucher') and contains(@label,'"+orderID+"')]"));
        boolean isFileNameCorrect = driverTools.waitForElementToAppear(fileNameLocator,10).isPresent();
        driverTools.back();
        return isFileNameCorrect;
    }

    @Override
    public void closeShareReceiptBtmSheet() {
        driverTools.click(CLOSE_NATIVE_SHARE_BTM_SHEET);
    }

    @Override
    public boolean verifyAtContactDetail(String salutation, String name, String phoneNumber, String lang) {
        String actualName = driverTools.getText(AT_CONTACT_DETAIL).orElseThrow().trim();
        Assertion.assertThat("Verify contact detail is the same as use filled in booking page", actualName, containsString(name));
        String actualPhoneNumber = driverTools.getText(AT_CONTACT_DETAIL_PHONE_NUMBER).orElseThrow();
        Assertion.assertThat("Verify contact detail is the same as use filled in booking page", actualPhoneNumber, containsString(phoneNumber));
        return true;
    }

    @Override
    public boolean verifyAtCarDetailSection(String carType) {
        String carTypeName = driverTools.getText(AT_CAR_TYPE_NAME).orElseThrow();
        logger.log("Actual car type name : " + carTypeName);
        Assertion.assertThat("Verify car type name same as in booking page", carTypeName, equalTo(carType));
        String contactPhoneNumber = driverTools.getValueAttribute(AT_CAR_DRIVE_CONTACT, "label");
        Assertion.assertThat("Verify contact phone number is present", contactPhoneNumber, not(blankOrNullString()));
        return true;
    }

    @Override
    public boolean verifyOrderDetailHeader(String expectedTitle) {
        String orderDetailHeaderTitle = driverTools.getText(ORDER_DETAIL_HEADER_TITLE).orElseThrow();
        Assertion.assertThat("Verify order dtail header title", orderDetailHeaderTitle, containsString(expectedTitle));
        return true;
    }

    @Override
    public boolean veriyAtSummaryDetail(String origin, String destination) {
        driverTools.swipeToElement(AT_TRIP_DETAIL_ENTRY, Swipe.UP, 2);
        driverTools.click(AT_TRIP_DETAIL_ENTRY);
        String actualOrigin = driverTools.getText(AT_TRIP_DETAIL_PICKUP).orElseThrow();
        logger.log("Actual origin : " + actualOrigin);
        Assertion.assertThat("Verify orgigin is the same", actualOrigin, equalTo(origin));
        String actualDestination = driverTools.getText(AT_TRIP_DETAIL_DESTINATION).orElseThrow();
        logger.log("Actual destination : " + actualDestination);
        Assertion.assertThat("Verify destination is the same", actualDestination, equalTo(destination));
        driverTools.click(AT_CLOSE_DETAIL);
        return true;
    }

    @Override
    public boolean verifyAtFacilitiesDetailPresent() {
        final var facilities = new Locator("FACILITIES", AppiumBy.xpath("//*[@name='Facilities' or @name='Fasilitas']//following-sibling::*//XCUIElementTypeStaticText"));
        List<WebElement> elements = driverTools.elements(facilities).orElseThrow();
        Assertion.assertThat("Verify facilities is longer than 0", elements.size(), greaterThan(0));
        return true;
    }

    @Override
    public void refreshOrderDetailPage(int numberOfSwipe) {
        driverTools.swipeDown(numberOfSwipe);
    }

    @Override
    public boolean checkAirportTransferDetail(String pickupPlace, String dropPlace) {
        driverTools.swipeUp();
        boolean isPickupPlace = driverTools.getText(PICKUP).orElseThrow().equals(pickupPlace);
        boolean isDropPlace = driverTools.getText(DROPOFF).orElseThrow().equals(dropPlace);
        logger.log("is pickup Place same: " + isPickupPlace);
        logger.log("is drop place same: " + isDropPlace);
        return isPickupPlace && isDropPlace;
    }

    @Override
    public String getAirportTransferTotalPrice() {
        WebElement totalPriceElement = driverTools.swipeToElement(TOTAL_PAYMENT, Swipe.UP, 3).orElseThrow();
        String actualTotalPrice = driverTools.getText(totalPriceElement);
        logger.log("actual AT TotalPrice: " + actualTotalPrice);
        return actualTotalPrice;
    }

    @Override
    public Boolean verifyFerryCheckinDate(String BookingFormCheckIndate) {
        String dateWithoutOptional = BookingFormCheckIndate.replace("Optional[", "").replace("]", "");
        driverTools.swipeToElement(getFerryDetailsLocator(dateWithoutOptional), Swipe.DOWN, 4);
        driverTools.waitForElementToAppear(getFerryDetailsLocator(dateWithoutOptional), 30);
        return driverTools.isElementDisplayed(getFerryDetailsLocator(dateWithoutOptional));
    }

    @Override
    public Boolean verifyFerryRoute(String BookingFormRoute) {
        // Remove "Optional" wrapper if present
        BookingFormRoute = BookingFormRoute.replace("Optional[", "").replace("]", "");

        String[] routeParts = BookingFormRoute.split("[^a-zA-Z0-9]+");
        for (String routePart : routeParts) {
            if (!routePart.isEmpty()) {
                Locator locator = getFerryDetailsLocator(routePart.trim());
                if (!driverTools.isElementDisplayed(locator)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Boolean verifyFerryPassengerNames(List<String> bookingFormPassengerNames) {
        Map<String, String> salutationMap = new HashMap<>();
        salutationMap.put("Nyonya", "Ny.");
        salutationMap.put("Tuan", "Tn.");
        salutationMap.put("Nona", "Nn.");
        salutationMap.put("Tn.", "Tn.");  // Keep existing salutation same
        salutationMap.put("Mr.", "Mr.");
        salutationMap.put("Mrs.", "Mrs.");
        salutationMap.put("Ms.", "Ms.");
        for (String bookingFormPassengerName : bookingFormPassengerNames) {
            String cleanedName = bookingFormPassengerName.replace("Optional[", "").replace("]", "").trim();

            String[] parts = cleanedName.split("\\s+", 2); // Split on first whitespace
            if (parts.length < 2) {
                return false;
            }
            String formSalutation = parts[0];
            String formName = parts[1];
            // Map the booking form salutation to the order detail salutation
            String expectedSalutation = salutationMap.getOrDefault(formSalutation, formSalutation);
            // Construct the expected full name on the order detail page
            String expectedFullName = expectedSalutation + " " + formName;
            // Split the full name into individual components and verify each component
            String[] nameParts = expectedFullName.split("\\s+");
            for (String namePart : nameParts) {
                Locator locator = getFerryDetailsLocator(namePart);
                driverTools.swipeToElement(locator, Swipe.UP, 4);

                if (!driverTools.isElementDisplayed(locator)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Boolean verifyIssuedBarCode() {
        return driverTools.isElementDisplayed(BARCODE_FERRY);
    }

    @Override
    public Boolean verifyIssuedViewEticket(String lang) {
        driverTools.waitForElementToAppear(SEE_ETICKET_BUTTON, 40); //order to get e-ticket-issued
        driverTools.click(SEE_ETICKET_BUTTON);
        if (lang.equalsIgnoreCase("en")) {
            driverTools.waitForElementToAppear(LANG_POPUP_ENGLISH, 10).ifPresent(driverTools::click);
        } else {
            driverTools.waitForElementToAppear(LANG_POPUP_INDO, 10).ifPresent(driverTools::click);
        }
        driverTools.waitForElementToAppear(VIEW_ETICKET_BTMSHEET, 10);
        boolean verifyViewETkt = driverTools.isElementDisplayed(VIEW_ETICKET_BTMSHEET);
        driverTools.click(CLOSE_BTMSHEET);
        return verifyViewETkt;
    }

    @Override
    public Boolean verifyIssuedShareEticket() {
        driverTools.swipeToElement(SHARE_RECIPT_, Swipe.UP, 5);
        return driverTools.isElementDisplayed(SHARE_RECIPT_);
    }

    private Locator getFerryDetailsLocator(String checkInDate) {
        return new Locator("FERRY_DETAILS", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@label,'" + checkInDate + "')])[1]"));
    }


    public boolean verifyCarRentalProductDetail(String fleetName, String carRentalType, String appLang) {
        String carRentalTypeTranslated = "";
        switch (carRentalType) {
            case "with_driver":
                carRentalTypeTranslated = appLang.equalsIgnoreCase("en") ? "With Driver" : "dengan sopir";
                break;
            default:
                carRentalTypeTranslated = appLang.equalsIgnoreCase("en") ? "without driver" : "tanpa sopir";
                break;
        }

        String actualProductName = driverTools.getText(CAR_RENTAL_PRODUCT_NAME).orElseThrow();
        Assertion.assertThat("Verify product name is the same as previously booked", actualProductName, equalTo(fleetName));

        String actualCarRentalType = driverTools.getText(CAR_RENTAL_RENTAL_TYPE).orElseThrow();
        Assertion.assertThat("Verify car rental type", actualCarRentalType, equalToIgnoringCase(carRentalTypeTranslated));
        return true;
    }

    @Override
    public Double getTotalTicketPriceOnly() {
        driverTools.waitForElementToAppear(PRICE_DETAIL_TICKET_ONLY);
        List<String> ticketsPriceText = driverTools.getTexts(PRICE_DETAIL_TICKET_ONLY);

        return CoreFunctions.sumUpStringNumbers(ticketsPriceText);
    }

    @Override
    public void closePriceDetailNative() {
        driverTools.waitForElementToAppear(CLOSE_PRICE_DETAIL);
        driverTools.click(CLOSE_PRICE_DETAIL);
    }

    public void verifyRefundStatusTitleHotel() {
        boolean isRefundHotelDisplayed = driverTools.isElementDisplayed(REFUND_HOTEL_STATUS);
        Assertion.assertThat("is refund hotel status present", isRefundHotelDisplayed, is(true));
    }

    @Override
    public String getRefundInProcessStatus(String orderId) {
        return driverTools.getText(REFUND_STATUS_IN_PROCESS).orElseThrow();
    }

    public boolean verifyDriverDetailExist() {
        driverTools.swipeToPresentedElement(CONTACT_CALL_BUTTON, Swipe.UP, 5);
        boolean whatsappButton = driverTools.isElementDisplayed(WHATSAPP_BUTTON);
        boolean callButton = driverTools.isElementDisplayed(CONTACT_CALL_BUTTON);
        Assertion.assertThat("Verify contact options", either(is(whatsappButton)).or(is(callButton)), is(true));
        return true;
    }

    @Override
    public boolean verifyRentalDetail(String pickUpLocation, String dropOffLocation, String pickUpTime, String dropOffTime, String carRentalDuration) {
        driverTools.swipeToElement(RENTAL_DETAIL_TITLE, Swipe.UP, 2);
        driverTools.swipeUp(2);
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
        return true;
    }

    @Override
    public boolean verifyCarRrInfoSection() {
        boolean isRrInfoPresent = driverTools.swipeToElement(CR_REFUND_AND_RESCHEDULE_INFO_SECTION, Swipe.UP, 3).isPresent();
        Assertion.assertThat("Verify rental refund and reschedule present", isRrInfoPresent, is(true));
        driverTools.click(CR_RR_DETAIL_BTN);
        boolean isRrBtmSheetPresent = driverTools.waitForElementToPresence(RR_INFO_BTM_SHEET, 15).isPresent();
        Assertion.assertThat("Verify rental refund and reschedule btm sheet appear", isRrBtmSheetPresent, is(true));
        driverTools.click(AT_CLOSE_DETAIL);
        return true;
    }

    @Override
    public boolean isLoginChallengeShown() {
        boolean loginChanlengeShown = driverTools.waitForElementToAppear(LOGIN_CHALLENGE_BTN, 3).isPresent();
        driverTools.click(LOGIN_CHALLENGE_CLOSE_BTN);
        return loginChanlengeShown;
    }

    @Override
    public boolean openPriceDetailNative() {
        driverTools.swipeToPresentedElement(PRICE_DETAIL_CHEVRON, Swipe.UP, 15);
        driverTools.swipeUp();
        driverTools.click(PRICE_DETAIL_CHEVRON);
        return true;
    }

    @Override
    public boolean verifyCrPriceDetailComponentCorrect() {
        driverTools.waitForElementToPresence(CR_PRICE_DETAIL_COMPONENTS);
        List<String> priceComponents = driverTools.elements(CR_PRICE_DETAIL_COMPONENTS)
                .orElseThrow()
                .stream()
                .map(WebElement::getText)
                .toList();
        List<String> otherFees = driverTools.getTexts(CR_OTHER_FEES);
        logger.log("Extracted price components: " + priceComponents);
        logger.log("Extracted other fees: " + otherFees);
        List<String> allPriceComponents = Stream.concat(priceComponents.stream(), otherFees.stream())
                .collect(Collectors.toList());
        logger.log("All extracted price-related components: " + allPriceComponents);
        List<Double> priceValues = allPriceComponents.stream()
                .filter(n -> n.matches(".*[0-9]+.*")).map(CoreFunctions::convertPriceSafely).toList();
        logger.log("Converted numeric price values: " + priceValues);
        double sumOfPriceDetail = priceValues.stream().reduce(0.0, Double::sum);

        String actualTotalPrice = driverTools.getText(CR_ORDDER_DETAIL_TOTAL_PRICE).orElseThrow();
        logger.log("Calculated sum of price components: " + sumOfPriceDetail);
        logger.log("Actual total price from UI: " + actualTotalPrice);
        Assertion.assertThat("Verify total sum is the same",
                CoreFunctions.convertPriceSafely(actualTotalPrice),
                equalTo(sumOfPriceDetail));

        driverTools.click(CR_CLOSE_PRICE_DETAIL);
        return true;
    }

    private Locator hotelPriceDetailComponents(String currency) {
        return new Locator("PRICE_DETAIL_COMPONENTS", AppiumBy.xpath("//*[contains(@label,'Total')]//preceding::XCUIElementTypeStaticText[contains(@label,'" + currency + "') and not(@name='Total Payment')]"));
    }

    private Locator hotelPriceDetailTotal(String currency) {
        return new Locator("PRICE_DETAIL_TOTAL", AppiumBy.xpath("(//*[contains(@label,'Total')]//following::XCUIElementTypeStaticText[contains(@label,'" + currency + "')])[last()]"));
    }

    @Override
    public boolean verifyHotelPriceDetail(String currency) {
        driverTools.waitForElementToPresence(hotelPriceDetailComponents(currency));
        List<String> priceComponentsInPriceDetail = driverTools.getTexts(hotelPriceDetailComponents(currency));
        logger.log("number of price component present : " + priceComponentsInPriceDetail.size());
        String totalPrice = driverTools.getText(hotelPriceDetailTotal(currency)).orElseThrow();

        List<Double> priceComponentsNumbersValue = priceComponentsInPriceDetail.stream()
                .filter(n -> n.matches(".*[0-9]+.*"))
                .map(CoreFunctions::convertPriceSafely).toList(); // remove currency word from the value. Eg -> // IDR 5000 into 5000 (with data type double)

        double sumOfPriceDetail = CoreFunctions.sumUpDouble(priceComponentsNumbersValue); // sum price components

        Assertion.assertThat("Verify total sum is the same", CoreFunctions.convertPriceSafely(totalPrice), equalTo(Math.abs(sumOfPriceDetail))); // compare sumOfPriceDetail with actual value in ui
        driverTools.click(CLOSE_GUEST_DETAIL);
        return true;
    }

    @Override
    public boolean verifyHotelCashbackPriceDetail(String currency) {
        return verifyHotelPriceDetail(currency);
    }

    @Override
    public boolean verifyHelpCenterSectionPresent() {
        driverTools.swipeToElement(HELP_CENTER_CARD, Swipe.UP, 30);
        return driverTools.isElementDisplayed(HELP_CENTER_CARD);
    }

    @Override
    public boolean verifyCarManageOrder() {
        driverTools.swipeToPresentedElement(MAIN_ETICKET, Swipe.UP, 10);
        Assertion.assertThat("Verify payment receipt button is present", driverTools.waitForElementToPresence(PAYMENT_RECEIPT).isPresent(), is(true));
        Assertion.assertThat("Verify e-ticket button is present", driverTools.waitForElementToPresence(MAIN_ETICKET).isPresent(), is(true));
        Assertion.assertThat("Verify add to calendar button is present", driverTools.waitForElementToPresence(ADD_TO_CALENDAR).isPresent(), is(true));
        return true;
    }

    @Override
    public void clickShareEticketNative() {
        driverTools.waitForElementToAppear(SHARE_ETICKET_FROM_HEADER, 10).isPresent();
        driverTools.click(SHARE_ETICKET_FROM_HEADER);
    }

    @Override
    public void shareReceiptNative() {
        logger.log("not in webview");
        driverTools.swipeToElement(SHARE_RECEIPT_BUTTON, Swipe.UP, 5);
        driverTools.click(SHARE_RECEIPT_BUTTON);
    }

    public String getTTDValidityPeriod() {
        driverTools.waitForElementToAppear(TTD_VALIDITY_PERIOD);

        return driverTools.getValueAttribute(TTD_VALIDITY_PERIOD, "label");
    }

    @Override
    public boolean verifyCompletePaymentExpiryTime() {
        List<WebElement> expiryTimeComponents = driverTools.waitForElementsToAppear(WAITING_PAYMENT_EXPIRY_TIME).orElseThrow();
        logger.log("Total elements found : " + expiryTimeComponents.size());
        Assertion.assertThat("Verify hour is in present", expiryTimeComponents.get(0).isDisplayed(), is(true));
        Assertion.assertThat("Verify minutes is in present", expiryTimeComponents.get(1).isDisplayed(), is(true));
        Assertion.assertThat("Verify seconds is in present", expiryTimeComponents.get(2).isDisplayed(), is(true));
        return true;
    }

    @Override
    public String getDepartureDateOrderDetail() {
        driverTools.waitForElementToAppear(FLIGHT_DEPARTURE_DATE_DETAIL, 10).orElseThrow();
        return driverTools.getText(FLIGHT_DEPARTURE_DATE_DETAIL).orElseThrow();
    }

    @Override
    public String getArrivalDateOrderDetail() {
        return driverTools.getText(FLIGHT_ARRIVAL_DATE_DETAIL).orElseThrow();
    }

    @Override
    public String getOriginAirportOrderDetail() {
        String originAirportDetail = driverTools.getText(DEPART_AIRPORT_NAME_ORDER_DETAIL).orElseThrow();
        return originAirportDetail;
    }

    @Override
    public String getDestinationAirportOrderDetail() {
        String destinationAirportDetail = driverTools.getText(RETURN_AIRPORT_NAME_ORDER_DETAIL).orElseThrow();
        return destinationAirportDetail;
    }

    @Override
    public List<String> getFlightsPassengerList() {
        driverTools.swipeToElement(PASSENGER_NAMES, Swipe.UP, 3);
        List<String> passengerList = driverTools.getTexts(PASSENGER_NAMES);
        logger.log("DEBUG: " + passengerList.get(0));
        return passengerList;
    }

    @Override
    public boolean verifyTTDLoyaltyDeduction() {
        goToTotalPayment();
        driverTools.click(PRICE_DETAIL_CHEVRON);
        String discountElement = driverTools.getText(PRICE_DETAIL_DISCOUNT_COMPONENTS).orElseThrow();
        logger.log("Loyalty discount component present : " + discountElement);
        Assertion.assertThat("Loyalty discount component present", driverTools.isElementDisplayed(PRICE_DETAIL_DISCOUNT_COMPONENTS),
                is(true));
        List<WebElement> priceComponentsInPriceDetail = driverTools.elements(PRICE_DETAIL_COMPONENTS).orElseThrow();
        logger.log("Number of price components present: " + priceComponentsInPriceDetail.size());

        for (WebElement element : priceComponentsInPriceDetail) {
            logger.log("Element value: " + element.getText());
        }

        String totalPrice = driverTools.getText(TTD_TOTAL_PAYMENT).orElseThrow();

        List<Double> priceComponentsNumbersValue = priceComponentsInPriceDetail.stream()
                .map(WebElement::getText)
                .filter(n -> n.matches(".*[0-9]+.*"))
                .map(CoreFunctions::convertPriceSafely)
                .toList();
        double sumOfPriceDetail = CoreFunctions.sumUpDouble(priceComponentsNumbersValue);

        Assertion.assertThat("Verify total sum is the same", CoreFunctions.convertPriceSafely(totalPrice),
                equalTo(sumOfPriceDetail));
        driverTools.click(CLOSE_PRICE_DETAIL);
        driverTools.switchToNative();
        return true; // Assuming that if the assertion passes, we return true
    }

    @Override
    public List<String> getGuestDetailsName() {
        driverTools.swipeToElement(GUEST_FULL_NAME, Swipe.UP, 3);
        List<String> guestNameList = driverTools.getTexts(GUEST_FULL_NAME);
        guestNameList = guestNameList.stream().map(String::stripLeading).collect(Collectors.toList());
        logger.log("guestNameList on the order details: " + guestNameList);
        return guestNameList;
    }

    @Override
    public Boolean verifyReviseSpecialRequest() {
        driverTools.waitForElementToAppear(EDIT_SPECIAL_REQUEST);
        driverTools.click(EDIT_SPECIAL_REQUEST);
        driverTools.isElementDisplayed(SPECIAL_REQUEST_PAGE);
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_CHANGE).ifPresent(WebElement::click);
        driverTools.sendKeys(SPECIAL_REQUEST_TEXTFIELD, "Test special request SDET");
        driverTools.hideKeyboard();
        driverTools.click(SAVE_SPECIAL_REQUEST);
        driverTools.click(YES_SPECIAL_REQUEST);
        fillOtp();
        boolean specialRequestDone = driverTools.waitForElementToPresence(SPECIAL_REQUEST_CHANGED_BANNER).isPresent();
        driverTools.click(SEE_RESERVATION_DETAILS);
        return specialRequestDone;
    }

    private void fillOtp() {
        driverTools.waitForElementToAppear(OTP_FEILD);
        for (int i = 1; i < 5; i++) {
            List<WebElement> field = driverTools.elements(OTP_FEILD).orElseThrow();
            driverTools.click(field.get(i - 1));
            driverTools.sendKeys(field.get(i - 1), String.valueOf(i));
        }
    }

    @Override
    public Boolean verifyLocationRedirectionButton() {
        driverTools.waitForElementToAppear(ORDER_ID);
        driverTools.swipeToElement(HOTEL_MAP_BTN, Swipe.UP, 12).orElseThrow();
        return driverTools.isElementDisplayed(HOTEL_MAP_BTN);
    }

    @Override
    public Boolean verifyRefundAndRescheduleInfo() {
        openRefundAndRescheduleInfo();
        boolean refundRescheduleBtmSheet = driverTools.waitForElementToAppear(REFUND_RESCHEDULE_BTMSHEET, 10).isPresent();
        closeRefundAndRescheduleInfo();
        return refundRescheduleBtmSheet;
    }

    @Override
    public boolean isRescheduleButtonDisplayed() {
        return driverTools.isElementDisplayed(RESCHEDULE_BUTTON);
    }

    @Override
    public boolean isRescheduleButtonDisplayed(String orderId) { //String orderId for Android only
        driverTools.swipeToElement(RESCHEDULE_BUTTON, Swipe.UP, 7);
        return isRescheduleButtonDisplayed();
    }

    @Override
    public boolean isDeleteOrderButtonDisplayed(String orderId) { //String orderId for Android only}
        driverTools.swipeToElement(REFUND_RESCHEDULE_DELETE_ORDER_BUTTON, Swipe.UP, 7);
        return driverTools.isElementDisplayed(REFUND_RESCHEDULE_DELETE_ORDER_BUTTON);
    }

    @Override
    public void clickRefundRescheduleCancelOrderButton(String orderId) { //String orderId for Android only
        driverTools.swipeToElement(REFUND_RESCHEDULE_DELETE_ORDER_BUTTON, Swipe.UP, 7).orElseThrow();
        driverTools.click(REFUND_RESCHEDULE_DELETE_ORDER_BUTTON);
    }

    @Override
    public boolean isCancelOrderButtonVisible() {
        driverTools.switchToNative();
        driverTools.swipeToElement(CANCEL_ORDER_BUTTON_NATIVE, Swipe.UP, 5);
        return driverTools.waitForElementToAppear(CANCEL_ORDER_BUTTON_NATIVE, 5).isPresent();
    }

    @Override
    public boolean isDeleteOrderButtonVisible() {
        driverTools.switchToNative();
        driverTools.swipeToElement(DELETE_ORDER_BUTTON_NATIVE, Swipe.DOWN, 5);
        driverTools.swipeToElement(DELETE_ORDER_BUTTON_NATIVE, Swipe.UP, 5);
        return driverTools.waitForElementToAppear(DELETE_ORDER_BUTTON_NATIVE, 10).isPresent();
    }

    @Override
    public boolean isMakeSimilarOrderButtonVisible() {
        driverTools.switchToNative();
        driverTools.swipeToElement(MAKE_SIMILAR_ORDER_BUTTON_NATIVE, Swipe.DOWN, 5);
        driverTools.swipeToElement(MAKE_SIMILAR_ORDER_BUTTON_NATIVE, Swipe.UP, 5);
        return driverTools.waitForElementToAppear(MAKE_SIMILAR_ORDER_BUTTON_NATIVE, 10).isPresent();
    }

    @Override
    public void cancelOrder(String orderId) {
        driverTools.swipeToElement(CANCEL_ORDER_BUTTON_NATIVE, Swipe.UP, 7);
        driverTools.waitForElementToAppear(CANCEL_ORDER_BUTTON_NATIVE).orElseThrow();
        driverTools.click(CANCEL_ORDER_BUTTON_NATIVE);
        logger.log("Clicked native cancel order button");

        driverTools.waitForElementToAppear(CANCEL_ORDER_CHECKBOX_NATIVE).orElseThrow();
        List<WebElement> checkBoxes = driverTools.elements(CANCEL_ORDER_CHECKBOX_NATIVE).orElseThrow();
        logger.log("Found " + checkBoxes.size() + " agreement checkbox(es)");
        for (WebElement checkbox : checkBoxes) {
            driverTools.click(checkbox);
        }

        WebElement agreeBtn = driverTools.waitForElementToAppear(CANCEL_ORDER_AGREE_BUTTON_NATIVE).orElseThrow();
        driverTools.click(agreeBtn);
        logger.log("Clicked agree button to confirm cancellation");
    }

    @Override
    public boolean submitCancelOrder(String orderId) {
        driverTools.swipeToElement(CANCEL_ORDER_BUTTON_NATIVE, Swipe.UP, 7);
        driverTools.waitForElementToAppear(CANCEL_ORDER_BUTTON_NATIVE).orElseThrow();
        driverTools.click(CANCEL_ORDER_BUTTON_NATIVE);
        logger.log("Clicked native cancel order button");

        WebElement submit = driverTools.waitForElementToAppear(SUBMIT_CANCEL_ORDER).orElseThrow();
        driverTools.click(submit);
        logger.log("Clicked submit button to confirm cancellation");

        return driverTools.waitForElementToPresence(POPUP_SUCCESS_CANCEL).isPresent();
    }

    /**
     * PAH / Manage Order cancel: swipe + tap native Cancel, then confirm in webview
     * using the same footer/modal steps as {@code OrderDetailMweb#clickCancelOrderButton()}.
     */
    @Override
    public void clickCancelOrderButton(String orderId) {
        driverTools.switchToNative();
        driverTools.swipeToElement(CANCEL_ORDER_BUTTON_NATIVE, Swipe.UP, 7);
        driverTools.waitForElementToAppear(CANCEL_ORDER_BUTTON_NATIVE).orElseThrow();
        driverTools.click(CANCEL_ORDER_BUTTON_NATIVE);
        logger.log("Clicked native cancel order button");

        driverTools.switchToWebViewUrl("order-cancellation?orderId=" + orderId);
        driverTools.waitForElementToPresence(OrderDetailWeb.CANCEL_ORDER_CONFIRMATION).orElseThrow();
        driverTools.clickWithJavascriptExecutor(OrderDetailWeb.CANCEL_ORDER_CONFIRMATION);
        driverTools.click(OrderDetailWeb.CANCEL_ORDER_CONFIRMATION_MODAL);
        OrderDetailWeb.getInstance(session).isCanceledOrderBannerVisible(15);
        driverTools.switchToNative();
    }

    @Override
    public boolean verifyPageModule() {
        driverTools.swipeToElement(PAGE_MODULE, Swipe.UP, 3);
        return driverTools.isElementDisplayed(PAGE_MODULE);
    }

    @Override
    public boolean verifyEditGuestDetail() {
        return driverTools.isElementDisplayed(EDIT_GUESTS_DETAILS);
    }

    @Override
    public boolean clickCallBtn() {
        driverTools.swipeToElement(CALL_BUTTON, Swipe.UP, 5);
        return driverTools.isElementDisplayed(CALL_BUTTON);
    }

    @Override
    public boolean verifyOrderDetailIdComponent() {
        driverTools.swipeToElement(ORDER_DETAIL_ID, Swipe.DOWN, 10);
        boolean detailId = driverTools.waitForElementToPresence(ORDER_DETAIL_ID).isPresent();
        driverTools.click(ORDER_DETAIL_ID_COPY);
        boolean idCopiedPopup = driverTools.waitForElementToPresence(ORDER_DETAIL_ID_COPIED).isPresent();
        logger.log("Order detail ID component verification " + (detailId && idCopiedPopup ? "successful." : "failed."));
        return detailId && idCopiedPopup;
    }

    @Override
    public boolean verifyLpgBanner() {
        driverTools.swipeToElement(LPG_BANNER, Swipe.UP, 3);
        driverTools.swipeUp();
        driverTools.swipeUp();
        driverTools.swipeToElement(LPG_BANNER,Swipe.UP,10);
        return driverTools.isElementDisplayed(LPG_BANNER);
    }

    @Override
    public void hidePromoBannerIfPresent() {
if (driverTools.waitForElementToAppear(HIDE_POPUP, 3).isPresent()) {
            driverTools.click(HIDE_POPUP);
        }
    }


    @Override
    public boolean ButtonFullProtectionSubmitNewClaim() {
        driverTools.swipeToElement(FULL_PROTECTION_SUBMIT_NEW_CLAIM, Swipe.UP, 6);
        return driverTools.isElementDisplayed(FULL_PROTECTION_SUBMIT_NEW_CLAIM);
    }

    @Override
    public void clickSubmitClaimBtn() {
        WebElement submit=driverTools.swipeToElement(SUBMIT_CLAIM_BUTTON, Swipe.UP, 3).orElseThrow();
        driverTools.clickElementFractionWithinBounds(submit,0.1,0.5);
        driverTools.waitForElementToDisappear(SUBMIT_CLAIM_BUTTON,5);
        if(driverTools.isElementDisplayed(SUBMIT_CLAIM_BUTTON)){
            driverTools.clickElementByCoordinate(SUBMIT_CLAIM_BUTTON);
        }
    }

    @Override
    public boolean verifyLowestPriceGuaranteePage() {
        return driverTools.isElementDisplayed(LPG_PAGE_TITLE);
    }

    @Override
    public boolean openAndCloseReadBeforeClaim() {
        WebElement read = driverTools.waitForElementToAppear(READ_BEFORE_CLAIM, 3).orElseThrow();
        driverTools.clickElementFractionWithinBounds(read,0.25,0.5);
        boolean isBottomSheetDisplayed = driverTools.isElementDisplayed(READ_BEFORE_CLAIM_BOTTOM_SHEET);
        WebElement readClose = driverTools.waitForElementToAppear(CLOSE_ICON, 3).orElseThrow();
        driverTools.clickElementFractionWithinBounds(readClose,0.25,0.5);
        return isBottomSheetDisplayed;
    }

    @Override
    public void fillClaimDetails(String finalPrice, String url) {
        driverTools.waitForElementToAppear(FINAL_PRICE_INPUT);
        driverTools.click(FINAL_PRICE_INPUT);
        if(driverTools.isElementDisplayed(CROSS_ICON)){
            driverTools.click(CROSS_ICON);
        }
        driverTools.sendKeys(FINAL_PRICE_INPUT, finalPrice);
        driverTools.swipeToElement(URL_INPUT,Swipe.UP,3);
        driverTools.waitForElementToAppear(URL_INPUT);
        driverTools.click(URL_INPUT);
        if(driverTools.isElementDisplayed(CROSS_ICON)){
            driverTools.click(CROSS_ICON);
        }
        driverTools.sendKeys(URL_INPUT, url);
    }

    @Override
    public void checkTnC() {
        driverTools.swipeToElement(TNC_CHECKBOX, Swipe.UP, 5);
        driverTools.click(TNC_CHECKBOX);
        driverTools.click(CLAIM_NOW_BUTTON);
    }

    @Override
    public void clickClaimNow() {
        driverTools.waitForElementToAppear(POPUP_CLAIM_NOW_BUTTON, 3);
        driverTools.click(POPUP_CLAIM_NOW_BUTTON);
        driverTools.dummyWait(2);
    }

    @Override
    public void uploadScreenshot() {
        driverTools.switchToNative();
        WebElement upload= driverTools.waitForElementToPresence(UPLOAD_BUTTON,15).orElseThrow();
        driverTools.clickElementFractionWithinBounds(upload,0.1,0.1);
        driverTools.waitForElementToAppear(FIRST_IMAGE_THUMBNAIL, 10);
        driverTools.click(FIRST_IMAGE_THUMBNAIL);
    }

    @Override
    public boolean statusChange() {
        driverTools.swipeToElement(WAITING_FOR_APPROVAL_STATUS,Swipe.UP,3);
        boolean isStatusUpdated = driverTools.isElementPresent(WAITING_FOR_APPROVAL_STATUS);
        return isStatusUpdated;
    }

    @Override
    public boolean verifyHighPriceValidationError() {
        boolean isDisplay = driverTools.isElementDisplayed(HIGH_PRICE_ERROR_MSG);
        return isDisplay;
    }

    @Override
    public boolean verifySeeGuideline() {
        WebElement guide = driverTools.waitForElementToAppear(SEE_GUIDELINE_LINK,2).orElseThrow();
        driverTools.click(SEE_GUIDELINE_LINK);
        driverTools.dummyWait(2);
        driverTools.clickElementFractionWithinBounds(guide,0.1,0.1);
        boolean isGuidelineDisplayed = driverTools.isElementDisplayed(SCREENSHOT_GUIDELINE_TITLE);
        WebElement readClose = driverTools.waitForElementToAppear(CLOSE_ICON, 3).orElseThrow();
        driverTools.clickElementFractionWithinBounds(readClose,0.25,0.5);
        return isGuidelineDisplayed;
    }

    @Override
    public String getImportantInfo() {
        driverTools.swipeToPresentedElement(IMPORTANT_INFO_CARD, Swipe.UP, 5);
        return driverTools.getText(IMPORTANT_INFO_CARD).orElseThrow();
    }

    @Override
    public String getTTDPackageName() {
        driverTools.swipeToElement(TTD_PACKAGE_NAME, Swipe.UP, 3);
        return driverTools.getText(TTD_PACKAGE_NAME).orElseThrow();
    }

    @Override
    public void openTTDGuestDetail() {
        driverTools.swipeToElement(TTD_GUEST_DETAIL_BUTTON, Swipe.UP, 5);
        driverTools.click(TTD_GUEST_DETAIL_BUTTON);
        driverTools.waitForElementToAppear(CLOSE_GUEST_DETAIL);
    }

    @Override
    public String getInsuranceTTD() {
        driverTools.swipeToElement(TTD_INSURANCE, Swipe.UP, 5);
        return driverTools.getText(TTD_INSURANCE).orElseThrow();
    }

    @Override
    public void clickDetailTTD() {
        if (driverTools.isElementDisplayed(TTD_ORDER_DETAIL)) {
            driverTools.click(TTD_ORDER_DETAIL);
        } else {
            driverTools.swipeToElement(SEE_ETICKET_BUTTON, Swipe.DOWN, 5);
            driverTools.swipeToElement(TTD_ORDER_DETAIL, Swipe.UP, 5);
            driverTools.click(TTD_ORDER_DETAIL);
        }
    }

    @Override
    public void continueToPayment() {
        driverTools.click(CONTINUE_PAYMENT_BUTTON);
    }

    @Override
    public String getRefundAndRescheduleText() {
        List<String> refundPolicies = driverTools.getTexts(REFUND_AND_RESCHEDULE_INFO_TEXT);

        String refundPoliciesString = String.join(" ", refundPolicies);
        logger.log("Refund policies: " + refundPoliciesString);
        return refundPoliciesString;
    }

    @Override
    public boolean isCloseBottomSheetButtonPresent() {
        return driverTools.isElementDisplayed(BOTTOMSHEET_CLOSE_BUTTON);
    }

    private Locator getTrainPassengerNameLocator(int index) {
        return new Locator("TRAIN_PASSENGER_" + index, AppiumBy.xpath("(//XCUIElementTypeScrollView/XCUIElementTypeOther[1]/XCUIElementTypeOther[4]/XCUIElementTypeOther/XCUIElementTypeOther[" + (1 + index) + "]//XCUIElementTypeStaticText)[1]"));
    }

    @Override
    public String getDepartureDateTrain() {
        String result = driverTools.getValueAttribute(TRAIN_DEPARTURE_TIME, "label").replace(" - ", ", ");
        logger.log("Train departure time = " + result);

        return result;
    }
    public String getDepartureDateWhoosh() {
        String result = driverTools.getValueAttribute(WHOOSH_TRAIN_DEPARTURE_TIME, "label").replace(" - ", ", ");
        logger.log("Train departure time = " + result);

        return result;
    }


    @Override
    public String getOriginStationNameTrain() {
        String result = driverTools.getValueAttribute(TRAIN_ORIGIN_STATION, "label").split(" - ")[0];
        logger.log("Train origin station = " + result);

        return result;
    }

    @Override
    public String getDestinationStationNameTrain() {
        String result = driverTools.getValueAttribute(TRAIN_DESTINATION_STATION, "label").split(" - ")[0];
        logger.log("Train destination station = " + result);

        return result;
    }

    public String getOriginStationNameWhoosh() {
        String result = driverTools.getValueAttribute(WHOOSH_TRAIN_ORIGIN_STATION, "label").split(" - ")[0];
        logger.log("Train origin station = " + result);

        return result;
    }

    public String getDestinationStationNameWhoosh() {
        String result = driverTools.getValueAttribute(WHOOSH_TRAIN_DESTINATION_STATION, "label").split(" - ")[0];
        logger.log("Train destination station = " + result);

        return result;
    }

    @Override
    public String getClassNameTrain() {
        String result = driverTools.getValueAttribute(TRAIN_NAME_CLASS, "label").split(" - ")[1].split("\\(")[0];
        logger.log("Train class name = " + result);

        return result;
    }

    public void carRentalScrollToTop() {
        driverTools.swipeToElement(CAR_RENTAL_PRODUCT_NAME, Swipe.DOWN, 10).orElseThrow();
    }

    @Override
    public void carRentalClickScrollDownBtn() {
        driverTools.swipeDown();
        driverTools.clickElementByCoordinate(CR_SCROLL_DOWN_FLOATING_BTN);
    }

    @Override
    public boolean clickSeeBoardingPass() {
        boolean boardingPass = driverTools.waitForElementToPresence(SEE_BOARDING_PASS).isPresent();
        if (boardingPass) {
            WebElement seeBoardingPassElement = driverTools.waitForElementToPresence(SEE_BOARDING_PASS, 3).orElseThrow();
            driverTools.click(seeBoardingPassElement);
        }
        return boardingPass;
    }

    public void clickRoomToRefundByIndex(int index, String orderId) {
        List<WebElement> elements = driverTools.elements(ROOM_TO_REFUNDS_OPTIONS).orElseThrow();
        driverTools.click(elements.get(index));
    }

    @Override
    public void clickShareEticket() {
        clickShareEticketNative();
    }

    @Override
    public boolean openPriceDetail() {
        return openPriceDetailNative();
    }

    @Override
    public void closePriceDetail() {
        closePriceDetailNative();
    }

    @Override
    public void shareReceipt() {
        shareReceiptNative();
    }

    @Override
    public void openReceipt() {
        driverTools.swipeToElement(RECEIPT_BUTTON, Swipe.UP, 10);
        driverTools.swipeUp();
        driverTools.click(RECEIPT_BUTTON);
    }

    @Override
    public boolean verifyManageOrderSection() {
        return driverTools.swipeToElement(MANAGE_ORDER_SEE_ETICKET, Swipe.UP, 10).isPresent();
    }

    /**
     * iOS-specific override: the order-detail page is rendered with native
     * XCUI elements (not a webview), so the base implementation which switches
     * to a webview and waits on a JS atom hangs indefinitely. Read the native
     * page source instead and look for the special-request text directly.
     * The text is non-translated user input, so a case-insensitive substring
     * match against the XML is sufficient.
     */
    @Override
    public boolean containsSpecialRequest(String specialRequestText) {
        if (specialRequestText == null || specialRequestText.isEmpty()) {
            return false;
        }
        driverTools.swipeToElement(MANAGE_ORDER_SEE_ETICKET, Swipe.UP, 10);
        String pageSource = driverTools.getPageSource();
        if (pageSource == null) {
            logger.log("iOS containsSpecialRequest: page source was null");
            return false;
        }
        boolean found = pageSource.toLowerCase().contains(specialRequestText.toLowerCase());
        logger.log("iOS containsSpecialRequest('" + specialRequestText + "') -> " + found);
        return found;
    }

    @Override
    public String getRefundHeader() {
        return driverTools.getText(REFUND_HEADER).orElseThrow();
    }

    @Override
    public String getBookingCodeOrderDetail() {
        WebElement bookingCodeElement = driverTools.swipeToElement(BOOKING_CODE_ORDER_DETAIL, Swipe.UP, 4)
                .or(() -> driverTools.waitForElementToPresence(BOOKING_CODE_ORDER_DETAIL, 20))
                .orElseThrow();
        String bookingCode = driverTools.getText(bookingCodeElement).trim();
        logger.log("Booking code from order detail: " + bookingCode);
        return bookingCode;
    }

    @Override
    public String getBookingCodeOnCheckInPage() {
        // Order detail is native on iOS; online-checkin is also driven through the native
        // accessibility tree — do not switch to WEBVIEW (unlike Android/dWeb base flow).
        WebElement bookingCodeElement = driverTools.waitForElementToPresence(BOOKING_CODE_CHECK_IN_IOS, 20).orElseThrow();
        String bookingCode = driverTools.getText(bookingCodeElement).trim();
        logger.log("Booking code on check-in page: " + bookingCode);

        WebElement copyBtn = driverTools.waitForElementToBeClickable(COPY_BOOKING_CODE_CHECK_IN_PAGE, 20).orElseThrow();
        driverTools.clickElementByCoordinate(copyBtn);
        verifyBookingCodeCopiedSnackbar();
        return bookingCode;
    }

    /**
     * Polls rapidly for a short-lived toast/snackbar. Do not use {@link com.tiket.appium.DriverTools#waitForElementToPresence}
     * here — it wraps {@code ExpectedConditions.refreshed(...)}, which keeps waiting after the toast
     * disappears and misses transient UI that only lives ~3–4 seconds.
     */
    private boolean waitForTransientElement(Locator locator, int timeoutMs, int pollIntervalMs) {
        long deadline = System.currentTimeMillis() + timeoutMs;
        int attempt = 0;
        while (System.currentTimeMillis() < deadline) {
            attempt++;
            try {
                List<WebElement> matches = driverTools.elements(locator).orElse(List.of());
                for (WebElement match : matches) {
                    if (match.isDisplayed()) {
                        logger.log("Transient element captured on attempt " + attempt + ": "
                                + driverTools.getText(match));
                        return true;
                    }
                }
            } catch (Exception e) {
                logger.log("Transient-element poll attempt " + attempt + " for " + locator + ": " + e.getMessage());
            }
            try {
                Thread.sleep(pollIntervalMs);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    /**
     * Waits for the post-copy snackbar; gates the Continue CTA which only enables after a successful copy.
     */
    private void verifyBookingCodeCopiedSnackbar() {
        boolean snackbarShown = waitForTransientElement(BOOKING_CODE_COPIED_SNACKBAR, 6_000, 150);
        logger.log("Booking code copied snackbar displayed: " + snackbarShown);
        Assertion.assertThat("Booking code copied snackbar is displayed", snackbarShown, is(true));
    }

    @Override
    public void clickCheckInButton() {
        driverTools.waitForElementToPresence(GET_BOARDING_PASS_BTN);
        driverTools.click(GET_BOARDING_PASS_BTN);
        driverTools.waitForElementToPresence(BOOKING_CODE_CHECK_IN_IOS, 20);
    }

    @Override
    public void clickContinueToOnlineCheckIn() {
        // Continue is disabled until Copy succeeds (snackbar confirms copy on the prior step).
        WebElement continueBtn = driverTools.waitForElementToBeClickable(CHECK_IN_BTN, 30).orElseThrow();
        driverTools.click(continueBtn);
        driverTools.waitForElementToPresence(IDENTIFICATION_PAGE_TITLE, 25).orElseThrow();
        logger.log("Navigated to native Identification page after Continue");
    }

    @Override
    public String isOnlineCheckInPage() {
        // iOS navigates to a native Identification screen — not a webview URL containing "identification".
        if (driverTools.waitForElementToPresence(IDENTIFICATION_PAGE_TITLE, 5).isPresent()) {
            String pageLabel = driverTools.getText(IDENTIFICATION_PAGE_TITLE).orElse("Identification");
            logger.log("On native Identification page: " + pageLabel);
            return pageLabel;
        }
        if (driverTools.isElementDisplayed(ONLINE_CHECKIN_PAGE_TITLE)) {
            logger.log("Still on online-checkin page (Online Check-in title visible)");
            return "online-checkin";
        }
        logger.log("Page state after continue is neither Identification nor online-checkin title");
        return "";
    }

    @Override
    public boolean swipeToStandAloneSection() {
        driverTools.swipeToElement(STANDALONE_SECTION, Swipe.UP, 10).orElseThrow();
        boolean isStandAloneSectionDisplayed = driverTools.isElementDisplayed(STANDALONE_SECTION);
        boolean isAddBtnDisplayed = driverTools.isElementDisplayed(STANDALONE_ADD_BTN);
        return isStandAloneSectionDisplayed && isAddBtnDisplayed;
    }

    @Override
    public String getPriceOnStandAloneSection() {
        WebElement standAloneSectionPrice = driverTools.swipeToElement(STANDALONE_PRICE_INSURANCE, Swipe.UP, 5).orElseThrow();
        String price = driverTools.getText(standAloneSectionPrice);
        logger.log("price on standalone section: " + price);
        return price;
    }

    @Override
    public void clickAddOnStandAloneSection() {
        driverTools.swipeToElement(STANDALONE_SECTION, Swipe.UP, 1).orElseThrow();
        driverTools.click(STANDALONE_ADD_BTN);
    }

    @Override
    public boolean isRefundDetailButtonDisplayed() {
        return driverTools.waitForElementToPresence(REFUND_DETAIL_BUTTON).isPresent();
    }

    public void clickRefund() {
        try {
            driverTools.swipeToElement(REFUND_BUTTON, Swipe.UP, 5);

            if (driverTools.isElementDisplayed(REFUND_BUTTON)) {
                driverTools.click(REFUND_BUTTON);
            } else {
                logger.log("Refund button not found after scrolling. Skipping click.");
            }
        } catch (Exception e) {
            logger.log("Refund button not found after scrolling. Skipping click.");
        }
    }

    @Override
    public void clickLoginNow() {

        driverTools.waitForElementToAppear(LOGIN_NOW_BUTTON, 10).orElseThrow();
        driverTools.click(LOGIN_NOW_BUTTON);

        driverTools.waitForElementToAppear(CONTINUE_BUTTON, 10).orElseThrow();
        driverTools.click(CONTINUE_BUTTON);
    }
    @Override
    public String getBookingCode() {
        driverTools.switchToNative();
        if (driverTools.isElementDisplayed(HIDE_POPUP)) {
            driverTools.click(HIDE_POPUP);
        }
        String bookingCode = driverTools.getText(BOOKING_CODE).orElseThrow().trim();
        driverTools.click(COPY_BOOKING_CODE);
        return bookingCode;
    }
    @Override
    public String getOrderId() {
        String orderIdText = String.valueOf(driverTools.getText(ORDER_ID_TOP));

        int separator = orderIdText.indexOf(":");
        return separator >= 0
                ? orderIdText.substring(separator + 1).trim()
                : orderIdText.trim();
    }
    @Override
    public boolean verifyImportantInfo() {
        if (driverTools.isElementDisplayed(HIDE_POPUP)) {
            driverTools.click(HIDE_POPUP);
        }
        driverTools.swipeToElement(IMPORTANT_INFO_DETAILS, Swipe.UP, 5);
        driverTools.click(IMPORTANT_INFO_DETAILS);

        driverTools.switchToNative();

        boolean opened = driverTools.waitForElementToAppear(IMP_INFO_BACK_BUTTON).isPresent();

        if (opened) {
            driverTools.click(IMP_INFO_BACK_BUTTON);
        }

        return true;
    }
    @Override
    public boolean verifyDepartureTrainDetails(TrainData data) {

        driverTools.waitForElementToAppear(WHOOSH_TRAIN_NAME).orElseThrow();
        String actualTrain = driverTools.getText(WHOOSH_TRAIN_NAME).orElseThrow();

        String actualOrigin = getOriginStationNameWhoosh().replaceAll("\\s*\\([^)]*\\)$", "");
        String actualDestination = getDestinationStationNameWhoosh().replaceAll("\\s*\\([^)]*\\)$", "");

        Assertion.assertThat("Origin station should match", actualOrigin, is(data.departureStation));
        Assertion.assertThat("Destination station should match", actualDestination, is(data.arrivalStation));
        Assertion.assertThat("Departure date should match", getDepartureDateWhoosh(), containsString(data.departureDate));

        Assertion.assertThat("Train number should match", actualTrain, containsString(data.trainName.split("•")[0].trim()));
        driverTools.swipeToElement(WHOOSH_DURATION_DIRECT_INFO, Swipe.UP, 3);

        Assertion.assertThat(
                "Duration and trip type should be displayed",
                driverTools.getText(WHOOSH_DURATION_DIRECT_INFO).orElseThrow(),
                either(containsString("Direct")).or(containsString("Transit"))
        );

        return true;
    }
    @Override
    public boolean verifyPassengerAndFacilities(TrainData data) {

        driverTools.swipeToElement(WHOOSH_PASSENGER_AND_FACILITIES_TITLE, Swipe.UP, 5);

        Assertion.assertThat(
                "Passenger & Facilities section should be displayed",
                driverTools.isElementDisplayed(WHOOSH_PASSENGER_AND_FACILITIES_TITLE),
                is(true));

        String passengerName = driverTools.getText(PASSENGER_NAME).orElseThrow();

        Assertion.assertThat(
                "Passenger name should match",
                passengerName,
                containsString(data.guestList.get(0)));

        Assertion.assertThat(
                "Seat assignment should be displayed",
                driverTools.getText(WHOOSH_PASSENGER_SEAT).orElseThrow(),
                not(blankOrNullString()));

        return true;
    }

   @Override
    public boolean verifyHelpCenter() {
       if (driverTools.isElementDisplayed(HIDE_POPUP)) {
           driverTools.click(HIDE_POPUP);
       }

       driverTools.swipeToElement(HELP_CENTER, Swipe.UP, 8);
       driverTools.click(HELP_CENTER);
       // Coachmark can sit on top; dismiss first so Help Center content is interactable.
       driverTools.waitForElementToAppear(SKIP_BUTTON, 5).ifPresent(skip -> driverTools.click(SKIP_BUTTON));

       boolean opened = driverTools.waitForElementToAppear(HELP_CENTER_PAGE, 15).isPresent();
        driverTools.click(HELP_CENTER_BACK_BUTTON);

        return opened;
    }
    @Override
    public boolean verifyContactCustomerCare() {
        if (driverTools.isElementDisplayed(HIDE_POPUP)) {
            driverTools.click(HIDE_POPUP);
        }
        WebElement contactLink = driverTools.swipeToElement(CONTACT_CUSTOMER_CARE, Swipe.UP, 8).orElseThrow();
        // Manage Order / Atur Pesanan FAB covers the text center (dump: text y~723, FAB y~760 / x~111).
        // Tap the chat icon just left of the label so the FAB does not swallow the hit.
        tapContactCustomerCareIcon(contactLink);

        boolean opened = driverTools.waitForElementToPresence(CONTACT_US_TITLE, 15).isPresent();
        if (!opened) {
            // Fallback: WDA accessibility click on the labeled static text.
            driverTools.click(CONTACT_CUSTOMER_CARE);
            opened = driverTools.waitForElementToPresence(CONTACT_US_TITLE, 10).isPresent();
        }
        driverTools.waitForElementToAppear(NEXT_BUTTON, 3).ifPresent(next -> driverTools.click(NEXT_BUTTON));
        driverTools.waitForElementToAppear(DONE_BUTTON, 3).ifPresent(done -> driverTools.click(DONE_BUTTON));

        if (opened) {
            driverTools.click(BACK_BUTTON_CC);
        }

        return opened;
    }

    @Override
    public boolean verifyBusManageOrderSection() {
        final var ETICKET_ON_MANAGE_ORDER_SECTION = new Locator("ETICKET_ON_MANAGE_ORDER_SECTION", AppiumBy.xpath("//XCUIElementTypeButton[@name='E-ticket' or @name='E-tiket']"));
        final var RECEIPT_BUTTON = new Locator("RECEIPT_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='Receipt' or @name='Bukti Pembayaran']"));
        Assertion.assertThat("Verify eticket button present", driverTools.isElementDisplayed(ETICKET_ON_MANAGE_ORDER_SECTION), is(true));
        Assertion.assertThat("Verify receipt button present", driverTools.isElementDisplayed(RECEIPT_BUTTON), is(true));
        return true;
    }

    private void tapContactCustomerCareIcon(WebElement contactLabel) {
        Point location = contactLabel.getLocation();
        Dimension size = contactLabel.getSize();
        int iconX = Math.max(24, location.getX() - 20);
        int iconY = location.getY() + (size.getHeight() / 2);
        logger.log("Tapping Contact Customer Care icon at (" + iconX + ", " + iconY + ")");
        Map<String, Object> params = new HashMap<>();
        params.put("x", iconX);
        params.put("y", iconY);
        ((AppiumDriver) driverTools.getDriver()).executeScript("mobile: tap", params);
    }
}

