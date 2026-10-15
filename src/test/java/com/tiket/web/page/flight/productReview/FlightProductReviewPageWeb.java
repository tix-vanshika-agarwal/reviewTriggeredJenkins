package com.tiket.web.page.flight.productReview;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.flight.PriceDetail;
import com.tiket.pojo.ttd.SupplierId;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.flight.bookingForm.FlightBookingFormPageWeb;
import com.tiket.web.page.flight.landingpage.FlightLandingPageDWeb;
import com.tiket.web.page.flight.landingpage.FlightLandingPageMWeb;
import com.tiket.web.page.flight.landingpage.FlightLandingPageWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.tiket.testbase.Assertion;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;

public abstract class FlightProductReviewPageWeb <T extends WebDriver> extends WebPage<T> {
    public FlightProductReviewPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FlightProductReviewPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new FlightProductReviewPageDWeb<>(session);
            }
            case AMWEB, IMWEB -> {
                return new FlightProductReviewPageMWeb<>(session);
            }
            default -> throw new UnsupportedOperationException("Platform not supported");
        }
    }
    private static final String PRP_FLIGHT_DETAIL_URL = "prp/flightDetail";
    private static final String PRP_FLIGHT_DETAIL_STORAGE_KEY = "__tiketPrpFlightDetail";
    private static final Locator FARE_CARD_WRAPPER = new Locator("FARE_CARD_WRAPPER", new By.ByXPath("//div[contains(@class,'FareCard_box')]"));
    private static final Locator NEXT_FARE_BUTTON = new Locator("NEXT_FARE_BUTTON", new By.ByXPath("//*[contains(@class,'FareCardSectionDesktop_slider_nav_next')]"));
    private static final Locator PREV_FARE_BUTTON = new Locator("PREV_FARE_BUTTON", new By.ByXPath("//*[contains(@class,'FareCardSectionDesktop_slider_nav_prev')]"));
    private static final Locator FACILITY_POPUP = new Locator("FACILITY_POPUP", new By.ByXPath("//div[contains(@class,'ModalBody_content')]"));
    private static final Locator SEE_OTHER_FACILITIES = new Locator("SEE_OTHER_FACILITITES", new By.ByXPath("(//span[contains(@class,'FareCard_see_detail__')])[1]"));
    private static final Locator SEE_OTHER_FACILITIES_RR = new Locator("SEE_OTHER_FACILITITES_RR", new By.ByXPath("//span[contains(@class,'FareCard_other_info_class') and contains(normalize-space(.),'Refund Reschedule')]/ancestor::div[contains(@class,'FareCard')][1]//span[contains(@class,'FareCard_see_detail')]"));
    private static final Locator DEPARTURE_CHECK = new Locator("DEPARTURE_CHECK", new By.ByXPath("(//div[contains(@class, 'Card_card_container')])[1]"));
    private static final Locator RETURN_CHECK = new Locator("RETURN_CHECK", new By.ByXPath("(//div[contains(@class, 'Card_card_container')])[2]"));
    private static final Locator PRODUCT_REVIEW_PAGE_CHECK = new Locator("PRODUCT_REVIEW_PAGE_CHECK", new By.ByXPath("//div[contains(@class,'review_card___')]//div[contains(@class,'Card_card_container__')]"));
    private static final Locator SELECT_BTN_REGULAR = new Locator("SELECT_BTN_REGULAR", new By.ByXPath("(//button[contains(@class,'FareCard_select_btn')])[1]"));
    private static final Locator NOT_MEET_TRAVEL_POLICY_POPUP = new Locator("NOT_MEET_TRAVEL_POLICY_POPUP", new By.ByXPath("//*[contains(@class,'Figure_title') and (contains(text(),'This order doesn’t meet the travel policy') or contains(text(),'Pesanan ini tidak sesuai dengan kebijakan perjalanan perusahaan') )]"));
    private static final Locator NOT_MEET_TRAVEL_POLICY_POPUP_NO_CANCEL = new Locator("NOT_MEET_TRAVEL_POLICY_POPUP_NO_CANCEL", new By.ByXPath("//*[contains(@class,'Figure_secondary') and (contains(text(),'No, cancel') or contains(text(),'Tidak, batalkan') )]"));
    private static final Locator NOT_MEET_TRAVEL_POLICY_POPUP_YES_CONTINUE = new Locator("NOT_MEET_TRAVEL_POLICY_POPUP_YES_CONTINUE", new By.ByXPath("//*[contains(@class,'Button_variant_primary') and (contains(text(),'Yes, continue') or contains(text(),'Ya, lanjutkan') )]"));
    private static final Locator CLOSE = new Locator("CLOSE", new By.ByXPath("//button[@data-testid='modal-close-button']"));
    private static final Locator TOTAL_AMOUNT = new Locator("TOTAL_AMOUNT", By.xpath("//p[contains(@class,'FareCardDetailFooter_total_amount')]"));
    private static final Locator ADULT_PRICE = new Locator("ADULT_PRICE", By.xpath("(//div[contains(@class,'PriceDetail_fare_component')][.//div[contains(@class,'PriceDetail_fare_label')]])[1]//div[contains(@class,'PriceDetail_fare_amount')]//span[normalize-space()]"));
    private static final Locator CHILD_PRICE = new Locator("CHILD_PRICE", By.xpath("(//div[contains(@class,'PriceDetail_fare_component')])[2]//div[contains(@class,'PriceDetail_fare_amount')]"));
    private static final Locator INFANT_PRICE = new Locator("INFANT_PRICE", By.xpath("(//div[contains(@class,'PriceDetail_fare_component')])[3]//div[contains(@class,'PriceDetail_fare_amount')]"));
    private static final Locator FARE_PRICE = new Locator("FARE_PRICE", new By.ByXPath("//p[contains(@class,'FareCardPricing_text_label') and contains(@class,'Text_variant_price')]"));
    private static final Locator DETAILS = new Locator("DETAILS", new By.ByXPath("(//span[text()='See details' or text()='Lihat Detail'])[1]"));
    private static final Locator PENALTY_POPUP = new Locator("PENALTY_POPUP", new By.ByXPath("//section[contains(@class,\"Modal_modal_popup\")]\n"));
    private static final Locator REFUND_TAB = new Locator("REFUND_TAB", By.xpath(
            "//button[contains(@class,'Tabs_tab')]//span[normalize-space()='Refund' or normalize-space()='Pengembalian dana' or normalize-space()='Pengembalian Dana']/parent::button"));
    private static final Locator RESCHEDULE_TAB = new Locator("RESCHEDULE_TAB", By.xpath(
            "//button[contains(@class,'Tabs_tab')]//span[normalize-space()='Reschedule' or normalize-space()='Ubah Jadwal' or normalize-space()='Penjadwalan Ulang']/parent::button"));
    private static final Locator PRICE_DETAIL_TAB = new Locator("PRICE_DETAIL_TAB", By.xpath(
            "//button[contains(@class,'Tabs_tab')]//span[normalize-space()='Price Detail' or normalize-space()='Price Details' or normalize-space()='Rincian Harga' or normalize-space()='Detail Harga']/parent::button"));
    private static final Locator RESCHEDULE_POPUP = new Locator("RESCHEDULE_POPUP", new By.ByXPath("(//div[contains(@class, 'PenaltyInfo_wrapper__')])[3]"));
    private static final Locator PRICE_DETAIL_POPUP = new Locator("PRICE_DETAIL_POPUP", new By.ByXPath("//div[contains(@class, 'PriceDetail_price_detail')]"));
    private static final Locator DEPARTURE_ARRIVAL_AIRPORT = new Locator("DEPARTURE_ARRIVAL_AIRPORT", new By.ByXPath("//span[contains(@class,'ProductReviewPageHeader_detail_departure_arrival')]"));
    private static final Locator FLIGHT_ROUTE_DETAILS_CONTAINER = new Locator("FLIGHT_ROUTE_DETAILS_CONTAINER", new By.ByXPath("//div[contains(@class, 'review_card')]//div[contains(@class, 'Container_container')]"));
    private static final Locator ORDER_POLICY = new Locator("ORDER_POLICY", new By.ByXPath("//button[contains(@class,'Button_variant_primary') and contains(@class,'Button_full_width')]"));
    private static final Locator FLIGHT_DETAIL_SECTION = new Locator("FLIGHT_DETAIL_SECTION", new By.ByXPath("//section[contains(@class,'FlightDetailCard_flight_detail_card')]"));
    private static final Locator FLIGHT_DEPARTURE_TIME = new Locator("FLIGHT_DEPARTURE_TIME", new By.ByXPath(".//div[contains(@class,'RouteCard_date_time_departure')]//span[contains(@class,'Text_weight_bold')] | .//div[contains(@class,'RouteCardWrapper_route_time')]/p"));
    private static final Locator FLIGHT_ARRIVAL_TIME = new Locator("FLIGHT_ARRIVAL_TIME", new By.ByXPath(".//div[contains(@class,'RouteCard_date_time_arrival')]//span[contains(@class,'Text_weight_bold')] | .//div[contains(@class,'RouteCardWrapper_time_arrival')]/p"));
    private static final Locator SELECT_BTN_DOWNPAY = new Locator("SELECT_BTN_DOWNPAY", new By.ByXPath("(//button[contains(@class,'FareCard_select_btn')])[2]"));
    private static final Locator ALL_PRICE_VALUES = new Locator("ALL_PRICE_VALUES", new By.ByXPath("//div[contains(@class,'PriceDetail_price_item')]//span[contains(@class,'PriceDetail_label') and not(contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'cashback')) and not(contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'pay now')) and not(contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'bayar sekarang'))]//following-sibling::span"));
    private static final Locator REVIEW_AND_PAY_BUTTON = new Locator("REVIEW_AND_PAY_BUTTON", By.xpath("//button[contains(@class,'BookingFooter_continue_button')][.//span[normalize-space()='Review & pay']]"));
    private static final Locator ITS_CORRECT_BUTTON = new Locator("ITS_CORRECT_BUTTON", By.xpath("//button[contains(@class,'Button_variant_primary')][.//span[normalize-space()=\"It's correct\"]]"));
    public static final Locator ONLY_ON_TIKET_NUDGE = new Locator("ONLY_ON_TIKET_NUDGE", new By.ByXPath("//span[contains(@class,'FareCard_other_info_class') and contains(normalize-space(),'Refund Reschedule DP')]"));
    private static final Locator NO_THANKS_BUTTON = new Locator("NO_THANKS_BUTTON", new By.ByXPath("//button[@data-testid='popup-button-secondary']"));
    private static final Locator RESCHEDULE_FLIGHT = new Locator("RESCHEDULE_FLIGHT", By.xpath("(//p//span[contains(normalize-space(text()),'Reschedule')])[1]"));
    private static final Locator FLIGHT_ROUTE_COLLAPSE_ICON = new Locator("FLIGHT_ROUTE_COLLAPSE_ICON",
            new By.ByXPath("(//div[contains(@class,'TransitInfoDetail')])[1]"));
    /**
     * Bullet list rows shown under the transit time block, e.g.
     * EN: "Collect baggage & check-in again", "Move terminal from Terminal 1 to Domestic"
     * ID: "Ambil bagasi & check-in ulang",   "Pindah terminal dari Terminal 1 ke Domestik"
     */
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
    /**
     * Click target for the "Self transfer ... in &lt;city&gt;" / "Self transfer ... di &lt;kota&gt;" header.
     * Clicking opens the {@code TransitDetailInfoPopUp} modal.
     */
    private static final Locator SELF_TRANSFER_TITLE = new Locator("SELF_TRANSFER_TITLE",
            new By.ByXPath("//div[contains(@class,'TransitInfoDetail_transit_info_detail_title')]"));
    /**
     * Title row inside the self-transfer popup that confirms protection coverage.
     * EN: "Self Transfer Protection included" / ID: "Termasuk Proteksi Transit Mandiri".
     */
    private static final Locator SELF_TRANSFER_PROTECTION_TITLE = new Locator("SELF_TRANSFER_PROTECTION_TITLE",
            new By.ByXPath("//span[contains(@class,'TransitDetailInfoPopUp_transit_info_title')"
                    + " and (contains(text(),'Self Transfer Protection included')"
                    + " or contains(text(),'Termasuk Proteksi Transit Mandiri'))]"));
    /**
     * Flight number line after expanding the first route segment (e.g. "JT - 123" / "8B-152").
     * Prefer AircraftHeader; fall back to any low-emphasis line in the route/review area that parses as a flight number.
     */
    private static final Locator FLIGHT_NUMBER_LINE_IN_REVIEW = new Locator("FLIGHT_NUMBER_LINE_IN_REVIEW",
            new By.ByXPath("(//div[contains(@class,'AircraftHeader_detail')]//p[contains(@class,'Text_variant_lowEmphasis')])[1]"
                    + " | (//div[contains(@class,'review_card')]//p[contains(@class,'Text_variant_lowEmphasis')][contains(.,'-')])[1]"));
    private static final Locator REVIEW_CARD_FLIGHT_NUMBER_CANDIDATES = new Locator("REVIEW_CARD_FLIGHT_NUMBER_CANDIDATES",
            new By.ByXPath("//div[contains(@class,'review_card')]//p[contains(@class,'Text_variant_lowEmphasis')]"));
    private static final Locator SECOND_NUMBER_LINE_IN_REVIEW = new Locator("SECOND_NUMBER_LINE_IN_REVIEW", new By.ByXPath("(//p[contains(@class,'Text_text__') and contains(@class,'Text_variant_lowEmphasis__') and contains(@class,'Text_size_b3__')])[2]"));


    private static final Locator FIRST_SEE_DETAILS_TRAVEL_ROUTE = new Locator("FIRST_SEE_DETAILS_TRAVEL_ROUTE", new By.ByXPath("(//button[contains(@class,'TravelRoute_more_facilities') and (contains(text(),'See details') or contains(text(),'Lihat detail'))])[1]"));
    private static final Locator SECOND_SEE_DETAILS_TRAVEL_ROUTE = new Locator("SECOND_SEE_DETAILS_TRAVEL_ROUTE", new By.ByXPath("(//button[contains(@class,'TravelRoute_more_facilities') and (contains(text(),'See details') or contains(text(),'Lihat detail'))])[2]"));
    private static final Locator FLIGHT_FACILITIES_BOTTOM_SHEET = new Locator("FLIGHT_FACILITIES_BOTTOM_SHEET", new By.ByXPath("//div[@data-testid='bottom-sheet-body']//h2[contains(text(),'Flight Facilities') or contains(text(),'Fasilitas Penerbangan')]"));
    private static final Locator BOTTOM_SHEET_CLOSE_BUTTON = new Locator("BOTTOM_SHEET_CLOSE_BUTTON", new By.ByXPath("//button[contains(@class,'BottomSheet_close_button')]"));
    private static final Locator OTHER_IMPORTANT_INFO_SECTION = new Locator("OTHER_IMPORTANT_INFO_SECTION", new By.ByXPath(
            "//span[contains(@class,'ImportantInfoAndFaq_info_title') and (contains(text(),'Other Important Info') or contains(text(),'Info Penting Lainnya'))]"
                    + " | //span[contains(@class,'PenaltyInfoFaq_info_title') and (contains(text(),'Other Important Info') or contains(text(),'Info Penting Lainnya'))]"));
    private static final Locator RESCHEDULE_CHIP_BUTTON = new Locator("RESCHEDULE_CHIP_BUTTON", new By.ByXPath("//div[contains(@class,'FareCardDetailBottomSheet_tab_chip')]//button[contains(@class,'Chip_chip')]//span[contains(text(),'Reschedule') or contains(text(),'Ubah Jadwal')]/parent::button"));
    private static final Locator REFUND_CHIP_BUTTON = new Locator("REFUND_CHIP_BUTTON", new By.ByXPath("//div[contains(@class,'FareCardDetailBottomSheet_tab_chip')]//button[contains(@class,'Chip_chip')]//span[contains(text(),'Refund')]/parent::button"));
    private static final Locator PRICE_DETAILS_HEADER = new Locator("PRICE_DETAILS_HEADER", new By.ByXPath("//div[contains(@class,'PriceDetail_price_detail')]//span[contains(text(),'Price Details') or contains(text(),'Rincian Harga')]"));
    private static final Locator PRICE_DETAILS_PAY_NOW = new Locator("PRICE_DETAILS_PAY_NOW", new By.ByXPath("//div[contains(@class,'PriceDetail_price_item')]//span[contains(text(),'Pay now') or contains(text(),'Bayar sekarang')]/following-sibling::span"));
    private static final Locator PRICE_DETAILS_DISCOUNT = new Locator("PRICE_DETAILS_DISCOUNT", new By.ByXPath("//div[contains(@class,'PriceDetail_price_item')]//span[contains(text(),'Discount') or contains(text(),'Diskon')]/following-sibling::span"));
    private static final Locator PRICE_DETAILS_HARGA_GLEDEK = new Locator("PRICE_DETAILS_HARGA_GLEDEK", new By.ByXPath("//div[contains(@class,'PriceDetail_price_item')]//span[contains(text(),'Harga Gledek')]/following-sibling::span"));
    private static final Locator PRICE_DETAILS_ADULT_PRICE = new Locator("PRICE_DETAILS_ADULT_PRICE", new By.ByXPath("//div[contains(@class,'PriceDetail_price_item')]//span[contains(text(),'Adult')]/following-sibling::span"));
    private static final Locator PRICE_DETAILS_CHILD_PRICE = new Locator("PRICE_DETAILS_CHILD_PRICE", new By.ByXPath("//div[contains(@class,'PriceDetail_price_item')]//span[contains(text(),'Child') or contains(text(),'Anak')]/following-sibling::span"));
    private static final Locator PRICE_DETAILS_INFANT_PRICE = new Locator("PRICE_DETAILS_INFANT_PRICE", new By.ByXPath("//div[contains(@class,'PriceDetail_price_item')]//span[contains(text(),'Infant') or contains(text(),'Bayi')]/following-sibling::span"));
    private static final Locator PRICE_DETAILS_TAX = new Locator("PRICE_DETAILS_TAX", new By.ByXPath("//div[contains(@class,'PriceDetail_price_item')]//span[contains(text(),'Tax') or contains(text(),'Pajak')]/following-sibling::span"));
    private static final Locator PRICE_DETAILS_SERVICE_FEE = new Locator("PRICE_DETAILS_SERVICE_FEE", new By.ByXPath("//div[contains(@class,'PriceDetail_price_item')]//span[contains(text(),'Service') or contains(text(),'Layanan')]/following-sibling::span"));
    private static final Locator PRICE_DETAILS_ALL_ITEMS = new Locator("PRICE_DETAILS_ALL_ITEMS", new By.ByXPath("//div[contains(@class,'PriceDetail_price_item')]"));
    private static final Locator FLIGHT_ROUTE_CARD = new Locator("FLIGHT_ROUTE_CARD",
            new By.ByXPath("//div[contains(@class,'FlightRouteCard_collapse_icon')]"
                    + " | //div[contains(@class,'AirlinesAndFacilitiesDesktop_main_row')]"));
    private static final Locator FLIGHT_FACILITIES_BUTTON = new Locator("FLIGHT_FACILITIES_BUTTON", new By.ByXPath("//button[contains(@class,'TravelRoute_more_facilities')]"));
    private static final Locator FLIGHT_FACILITIES_SECTION = new Locator("FLIGHT_FACILITIES_SECTION", new By.ByXPath("//div[contains(@class,'FacilitiesBottomSheet_facilities_box')]"));
    private static final Locator FLIGHT_FACILITY_CLOSE_BUTTON = new Locator("FLIGHT_FACILITY_CLOSE_BUTTON", new By.ByXPath("//button[contains(@class,'BottomSheet_close_button')]"));
    private static final Locator REFUND_SECTION = new Locator("REFUND_SECTION", new By.ByXPath(
            "//span[contains(@class,'RefundAndReschedule_estimation_title') and contains(translate(normalize-space(.),'REFUND','refund'),'refund')]"
                    + " | //div[contains(@class,'RefundAndReschedule_timeline')]//*[contains(translate(normalize-space(.),'REFUND','refund'),'refund')]"
                    + " | //span[contains(@class,'PenaltyInfo_title_info') and contains(.,'Refund')]"
                    + " | //*[contains(@class,'PenaltyInfo') and (contains(normalize-space(.),'Estimated refund amount') or contains(normalize-space(.),'Get refund') or contains(normalize-space(.),'Perkiraan jumlah refund') or contains(normalize-space(.),'Dapat refund'))]"
                    + " | //div[contains(@class,'FareCardDetailBottomSheet')]//*[contains(normalize-space(.),'Get refund') or contains(normalize-space(.),'Estimated refund')]"));
    private static final Locator RESCHEDULE_BUTTON_CHIP = new Locator("RESCHEDULE_BUTTON_CHIP", new By.ByXPath(
            "//div[contains(@class,'FareCardDetailBottomSheet_tab_chip')]//button[.//span[normalize-space()='Reschedule' or normalize-space()='Ubah Jadwal']]"));
    private static final Locator RESCHEDULE_SECTION = new Locator("RESCHEDULE_SECTION", new By.ByXPath(
            "//span[contains(@class,'RefundAndReschedule_estimation_title') and contains(translate(normalize-space(.),'RESCHEDULE','reschedule'),'reschedule')]"
                    + " | //div[contains(@class,'RefundAndReschedule_timeline')]//*[contains(translate(normalize-space(.),'RESCHEDULE','reschedule'),'reschedule')]"
                    + " | //span[contains(@class,'PenaltyInfo_title_info') and contains(.,'Reschedule')]"
                    + " | //*[contains(@class,'PenaltyInfo') and (contains(normalize-space(.),'Estimated reschedule') or contains(normalize-space(.),'reschedule fee') or contains(normalize-space(.),'Biaya reschedule') or contains(normalize-space(.),'Get reschedule'))]"
                    + " | //div[contains(@class,'FareCardDetailBottomSheet')]//*[contains(normalize-space(.),'reschedule fee') or contains(normalize-space(.),'Estimated reschedule')]"));
    private static final Locator FAQ_INFO_SECTION = new Locator("FAQ_INFO_SECTION", new By.ByXPath(
            "//div[contains(@class,'ImportantInfoAndFaq')]//span[normalize-space()='Frequently asked questions' or normalize-space()='Pertanyaan yang sering diajukan']"
                    + " | //div[contains(@class,'ImportantInfoAndFaq')]//div[contains(@class,'faq_faq_list')]"
                    + " | //span[contains(@class,'PenaltyInfoFaq_faq_title') and (contains(text(),'Frequently asked questions') or contains(text(),'Pertanyaan yang sering diajukan'))]"));
    // Bottom sheet footer total amount (Final price at bottom - "IDR 1.775.133")
    private static final Locator BOTTOM_SHEET_TOTAL_AMOUNT = new Locator("BOTTOM_SHEET_TOTAL_AMOUNT", new By.ByXPath("//*[contains(@class,'FareCardDetailBottomSheet_total_amount')]"));
    // Cashback labels (EN: Flight cashback, Transaction cashback / ID: Cashback penerbangan, Cashback transaksi)
    private static final String CASHBACK_LABELS_XPATH = "//*[contains(@class,'PriceDetail_label') and contains(translate(text(),'CASHBACK','cashback'),'cashback') and not(contains(translate(text(),'TOTAL SETELAH AFTER','total setelah after'),'total'))]";
    // Cashback amounts (the points/values like "Dapat 21.349 poin")
    private static final String CASHBACK_AMOUNTS_XPATH = "//*[contains(@class,'PriceDetail_label') and contains(translate(text(),'CASHBACK','cashback'),'cashback') and not(contains(translate(text(),'TOTAL','total'),'total'))]//following-sibling::*//span";
    // Total after cashback amount (EN: Total after cashback / ID: Total setelah cashback)
    private static final String TOTAL_AFTER_CASHBACK_XPATH = "//*[contains(@class,'PriceDetail_label') and contains(translate(text(),'CASHBACK','cashback'),'cashback') and (contains(translate(text(),'TOTAL SETELAH AFTER','total setelah after'),'total'))]//following-sibling::span";
    private static final Locator FAQ_SECTION = new Locator("FAQ_SECTION", new By.ByXPath("(//span[normalize-space()='Frequently asked questions' or normalize-space()='Pertanyaan yang sering diajukan'])[2]"));
    private static final Locator FAQ_QUESTION = new Locator("FAQ_QUESTION", new By.ByXPath("(//div[contains(@class,'faq_faq_list')]//span[contains(@class,'faq_faq_question')])[3]"));
    private static final Locator FAQ_ANSWER = new Locator("FAQ_ANSWER", new By.ByXPath("(//span[contains(@class,'faq_faq_answer')])[3]"));
    private static final Locator STEPPER_JOURNEY_ROUND_TRIP = new Locator("STEPPER_JOURNEY_ROUND_TRIP", By.xpath("//div[contains(@class,'StepperJourney_stepper_journey__')]"));
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
    private static final Locator FULL_REFUND_OPTION = new Locator("FULL_REFUND_OPTION", By.xpath("(//div[contains(@class,'Label_label')]//div[contains(normalize-space(),'Refund')])[1]"));
    private static final Locator ARROW = new Locator("ARROW", By.xpath("(//*[@data-testid='accordion-icon'])[3]"));
    private static final String REFUND_100P_FULL_REFUND_COPY_XPATH =
            "contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'get full refund')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'per full refund')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'pengembalian dana penuh')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'pengembalian penuh')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'dapat refund penuh')";
    private static final String REFUND_100P_FARE_CARD_ADDON_PRICE_XPATH =
            "(//div[contains(@class,'FareCard_additional_price')]//span[contains(@class,'Text_variant_alert')][contains(normalize-space(.),'+')])[1]";
    private static final String REFUND_100P_FARE_CARD_ADDON_BLOCK_XPATH =
            "(//div[contains(@class,'FareCard_additional_price')][.//span[contains(@class,'Text_variant_alert')][contains(normalize-space(.),'+')]])[1]";
    private static final String REFUND_100P_FARE_CARD_MATCH =
            "//div[contains(@class,'FareCard_box')]"
                    + "[.//span[contains(@class,'FareCard_other_info_class')]"
                    + "[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'tiket 100% refund')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'100% refund')"
                    + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'refund 100%')"
                    + " or contains(normalize-space(.),'Refund Reschedule')]]"
                    + "[.//div[contains(@class,'FareCard_additional_price')]//span[contains(@class,'Text_variant_alert')][contains(normalize-space(.),'+')]]"
                    + "[.//span[" + REFUND_100P_FULL_REFUND_COPY_XPATH + "]]";
    private static final String REFUND_100P_FARE_CARD_XPATH = "(" + REFUND_100P_FARE_CARD_MATCH + ")[1]";
    private static final Locator REFUND_100P_FARE_CARD = new Locator("REFUND_100P_FARE_CARD", new By.ByXPath(REFUND_100P_FARE_CARD_XPATH));
    private static final Locator REFUND_100P_FARE_CARD_FOOTER_LABEL = new Locator("REFUND_100P_FARE_CARD_FOOTER_LABEL",
            new By.ByXPath(REFUND_100P_FARE_CARD_XPATH + "//span[contains(@class,'FareCard_other_info_class')]"));
    private static final Locator REFUND_100P_SEE_DETAILS = new Locator("REFUND_100P_SEE_DETAILS",
            new By.ByXPath(REFUND_100P_FARE_CARD_XPATH + "//span[contains(@class,'FareCard_see_detail')]"));
    private static final Locator SELECT_BTN_100P_REFUND = new Locator("SELECT_BTN_100P_REFUND",
            new By.ByXPath(REFUND_100P_FARE_CARD_XPATH + "//button[contains(@class,'FareCard_select_btn')]"));
    private static final Locator REFUND_100P_FARE_CARD_ADDON_PRICE = new Locator("REFUND_100P_FARE_CARD_ADDON_PRICE",
            new By.ByXPath(REFUND_100P_FARE_CARD_ADDON_PRICE_XPATH));
    private static final Locator REFUND_100P_FARE_CARD_ADDON_DESCRIPTION = new Locator("REFUND_100P_FARE_CARD_ADDON_DESCRIPTION",
            new By.ByXPath(REFUND_100P_FARE_CARD_ADDON_BLOCK_XPATH + "//span[contains(@class,'Text_variant_lowEmphasis')]"));
    private static final Locator REFUND_100P_POLICY_TIMELINE = new Locator("REFUND_100P_POLICY_TIMELINE", new By.ByXPath(
            "//div[contains(@class,'RefundAndReschedule_timeline')]"
                    + "//p[contains(@class,'RefundAndReschedule_timeline_item_right')]"
                    + "[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'get refund 100%')]"));
    private static final String FARE_CARD_POPUP_PRICE_COMPONENTS_XPATH =
            "//div[contains(@class,'Tabs_tab_panel_active')]//div[contains(@class,'FareCardDetailPopUp_price_detail')]"
                    + "//div[contains(@class,'PriceDetail_fare_component')]";
    private static final String REFUND_100P_BREAKDOWN_COMPONENT_XPATH =
            "//div[contains(@class,'Tabs_tab_panel_active')]//div[contains(@class,'FareCardDetailPopUp_price_detail')]"
                    + "//div[contains(@class,'PriceDetail_fare_component')]"
                    + "[.//div[contains(@class,'PriceDetail_fare_label')]//span"
                    + "[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'100% refund')"
                    + " and contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'addon')]]";
    private static final Locator REFUND_100P_BREAKDOWN_LABEL = new Locator("REFUND_100P_BREAKDOWN_LABEL",
            new By.ByXPath(REFUND_100P_BREAKDOWN_COMPONENT_XPATH + "//div[contains(@class,'PriceDetail_fare_label')]//span"));

    private Locator getRefund100pBreakdownAmountLocator(String currency) {
        return new Locator("REFUND_100P_BREAKDOWN_AMOUNT",
                new By.ByXPath(REFUND_100P_BREAKDOWN_COMPONENT_XPATH
                        + "//div[contains(@class,'PriceDetail_fare_amount')]//span[contains(normalize-space(.),'"
                        + currency + "')]"));
    }
    private static final Locator SELECT_TICKET_100P_REFUND_POPUP = new Locator("SELECT_TICKET_100P_REFUND_POPUP",
            By.xpath("//footer[contains(@class,'Modal_modal_footer')]//button[contains(@class,'Button_variant_primary')]"
                    + "[.//span[normalize-space()='Select Ticket' or normalize-space()='Pilih Tiket']]"));

    public abstract void continueToBookingForm();

    public void clickSelectBtnRegular(){
        try {
            driverTools.waitForElementToPresence(SELECT_BTN_REGULAR);
            driverTools.clickWithJavascriptExecutor(SELECT_BTN_REGULAR);
            if (driverTools.isElementPresent(STEPPER_JOURNEY_ROUND_TRIP)) {
                driverTools.waitForElementToPresence(SELECT_BTN_REGULAR);
                driverTools.scrollIntoWebView(SELECT_BTN_REGULAR);
                driverTools.clickWithJavascriptExecutor(SELECT_BTN_REGULAR);
            }
        }catch(Exception e){
            driverTools.clickWithJavascriptExecutor(SELECT_BTN_REGULAR);
        }
    }

    public void clickYesContinue(){
        driverTools.waitForElementToAppear(ORDER_POLICY, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public boolean isNotMeetTravelPolicyPopupDisplayed(){
        driverTools.waitForElementToPresence(NOT_MEET_TRAVEL_POLICY_POPUP);
        boolean isNotMeetTravelPolicyPopupDisplayed = driverTools.waitForElementToPresence(NOT_MEET_TRAVEL_POLICY_POPUP).isPresent();
        logger.log("is not meet travel policy popup displayed : " + isNotMeetTravelPolicyPopupDisplayed);

        boolean isNoCancelBtnDisplayed = driverTools.waitForElementToPresence(NOT_MEET_TRAVEL_POLICY_POPUP_NO_CANCEL).isPresent();
        logger.log("is no cancel button displayed : " + isNoCancelBtnDisplayed);

        boolean isYesContinueBtnDisplayed = driverTools.waitForElementToPresence(NOT_MEET_TRAVEL_POLICY_POPUP_YES_CONTINUE).isPresent();
        logger.log("is yes continue button displayed : " + isYesContinueBtnDisplayed);
        return isNotMeetTravelPolicyPopupDisplayed && isNoCancelBtnDisplayed && isYesContinueBtnDisplayed;
    }
    public boolean isPresent(){
        return driverTools.waitForElementToAppear(PRODUCT_REVIEW_PAGE_CHECK).isPresent();

    }
    public boolean isDepartureScheduleVisible(){
        return driverTools.waitForElementToAppear(DEPARTURE_CHECK).isPresent();

    }
    public boolean isReturnScheduleVisible() {
        return driverTools.waitForElementToAppear(RETURN_CHECK).isPresent();
    }
    public void clickSeeOtherFacilities(){
        driverTools.waitForElementToAppear(SEE_OTHER_FACILITIES);
        driverTools.click(SEE_OTHER_FACILITIES);
    }

    public void clickSeeOtherFacilitiesRR(){
        driverTools.waitForElementToAppear(SEE_OTHER_FACILITIES_RR);
        driverTools.click(SEE_OTHER_FACILITIES_RR);
    }

    public boolean isFacilitiesPopupVisible(){
        return driverTools.waitForElementToAppear(FACILITY_POPUP).isPresent();

    }
    public void closePopUp(){
        driverTools.waitForElementToAppear(CLOSE);
        driverTools.clickWithJavascriptExecutor(CLOSE);
        driverTools.waitForElementToDisappear(CLOSE);
    }
    public void clickSeeDetailsUnderEconomyCard(){
        driverTools.waitForElementsToAppear(DETAILS);
        driverTools.scrollIntoWebView(DETAILS);
        driverTools.clickWithJavascriptExecutor(DETAILS);
    }

    public String getPriceOnFareCard(){
        String priceText = driverTools.getText(
                driverTools.element(FARE_PRICE).orElseThrow()
        );
        return String.format(Locale.ROOT, "%.2f", CoreFunctions.convertPriceSafely(priceText));
    }

    public boolean isPenaltyInfoVisible(){
        return driverTools.waitForElementsToAppear(PENALTY_POPUP).isPresent();
    }

    public void clickPenaltyInfoTabRefund(){
        driverTools.waitForElementToAppear(REFUND_TAB);
        driverTools.clickWithJavascriptExecutor(REFUND_TAB);
    }

    public void clickPenaltyInfoTabReschedule(){
        driverTools.waitForElementToAppear(RESCHEDULE_TAB);
        driverTools.clickWithJavascriptExecutor(RESCHEDULE_TAB);

    }
    public void clickPenaltyInfoTabPriceDetail(){
        driverTools.waitForElementToAppear(PRICE_DETAIL_TAB);
        driverTools.clickWithJavascriptExecutor(PRICE_DETAIL_TAB);

    }
    public boolean isPriceDetailInfoDisplayed(){
        return driverTools.waitForElementsToAppear(PRICE_DETAIL_POPUP).isPresent();
    }

    public Map<String, String> getDepartureOrArrivalAirport() {
        WebElement span = driverTools.element(DEPARTURE_ARRIVAL_AIRPORT).orElseThrow();
        String[] airports = span.getText().trim().split("\\s+");
        if (airports.length < 2) {
            throw new IllegalStateException("Expected at least two airports in text: '" + span.getText() + "'");
        }
        Map<String, String> airportMap = new HashMap<>() {{
            put("departure", airports[0]);
            put("arrival", airports[1]);
        }};
        return airportMap;
    }

    public Map<String, List<String>> getFlightSchedule() {
        Map<String, List<String>> flightTimes = new HashMap<>();
        List<String> departureTimes = new ArrayList<>();
        List<String> arrivalTimes = new ArrayList<>();

        List<WebElement> cards = driverTools.elements(FLIGHT_DETAIL_SECTION).orElse(null);
        if (cards == null || cards.isEmpty()) {
            cards = driverTools.elements(FLIGHT_ROUTE_DETAILS_CONTAINER).orElseThrow();
        }

        for (WebElement card : cards) {
            List<WebElement> depElements = driverTools.elements(FLIGHT_DEPARTURE_TIME, card)
                    .orElseThrow();
            List<WebElement> arrElements = driverTools.elements(FLIGHT_ARRIVAL_TIME, card)
                    .orElseThrow();

            for (WebElement dep : depElements) {
                departureTimes.add(dep.getText().trim().split("\n")[0]);
            }
            for (WebElement arr : arrElements) {
                arrivalTimes.add(arr.getText().trim().split("\n")[0]);
            }
        }

        flightTimes.put("departure", departureTimes);
        flightTimes.put("arrival", arrivalTimes);

        return flightTimes;
    }

    public boolean isFlightHaveDownPay() {
        driverTools.waitForElementToAppear(ONLY_ON_TIKET_NUDGE);
        return driverTools.isElementPresent(ONLY_ON_TIKET_NUDGE);
    }

    public void clickDownPaySelectBtn(){
        driverTools.waitForElementToAppear(SELECT_BTN_DOWNPAY);
        driverTools.clickWithJavascriptExecutor(SELECT_BTN_DOWNPAY);
    }

    public void clickNoThanksBtn() {
        driverTools.waitForElementToAppear(NO_THANKS_BUTTON);
        driverTools.waitForElementToPresence(NO_THANKS_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public void clickReviewAndPayBtn() {
        driverTools.waitForElementToAppear(REVIEW_AND_PAY_BUTTON);
        driverTools.clickWithJavascriptExecutor(REVIEW_AND_PAY_BUTTON);
    }

    public void clickItsCorrectBtn() {
        driverTools.waitForElementToAppear(ITS_CORRECT_BUTTON);
        driverTools.clickWithJavascriptExecutor(ITS_CORRECT_BUTTON);
    }

    public boolean isRescheduleAvailable(){
        driverTools.waitForElementToAppear(RESCHEDULE_FLIGHT);
        return driverTools.isElementPresent(RESCHEDULE_FLIGHT);
    }

    public void expandFlightRouteDetails() {
        driverTools.waitForElementToAppear(FLIGHT_ROUTE_COLLAPSE_ICON, 10);
        driverTools.clickWithJavascriptExecutor(FLIGHT_ROUTE_COLLAPSE_ICON);
    }

    public String expandFirstFlightRouteAndGetAirlineCode() {
        return getAirlineCodeForRoute(1);
    }

    public String expandSecondFlightRouteAndGetAirlineCode() {
        return getAirlineCodeForRoute(2);
    }

    /**
     * Returns the airline code (e.g. "JT", "IU") for the {@code routeIndex}th flight
     * segment on the product-review page (1-based).
     * <p>
     * Strategy:
     * <ol>
     *     <li>Try to read it from the visible airline row in plain sight - on the new
     *     DOM the row already shows "Lion Air JT748" / "Super Air Jet IU706", so no
     *     expansion is required.</li>
     *     <li>Only if that fails (legacy DOM, or the airline name span is missing),
     *     click the chevron for that segment to expand the route detail and read the
     *     dedicated flight-number line.</li>
     * </ol>
     */
    private String getAirlineCodeForRoute(int routeIndex) {
        Optional<String> visibleCode = readAirlineCodeFromAirlineRow(routeIndex);
        if (visibleCode.isPresent()) {
            logger.log("Airline code (in plain sight) from product review row[" + routeIndex + "]: " + visibleCode.get());
            return visibleCode.get();
        }

        logger.log("Airline code not in plain sight for row[" + routeIndex + "]; expanding the route detail.");
        Locator chevron = getFlightFacilitiesButtonByIndex(routeIndex);
        if (driverTools.waitForElementToAppear(chevron, 10).isPresent()) {
            driverTools.scrollIntoWebView(chevron);
            driverTools.clickWithJavascriptExecutor(chevron);
            if (!waitForFlightNumberLineVisible(4)) {
                logger.log("Flight number line not visible after chevron[" + routeIndex + "] click; retrying toggle.");
                driverTools.clickWithJavascriptExecutor(chevron);
            }
        }
        String line = readFirstFlightNumberLineForMockValidation();
        String airlineCode = parseAirlineCodeFromFlightNumberLine(line);
        logger.log("Airline code (after expand) from product review row[" + routeIndex + "]: " + airlineCode);
        return airlineCode;
    }

    /**
     * Reads the inline airline + flight-number text (e.g. "Lion Air JT748") from the
     * Nth airline row on the product-review page, then parses out the airline code.
     * Returns {@link Optional#empty()} if either the row is missing or its text does
     * not contain a recognisable code, so callers can fall back to expanding the
     * route detail.
     */
    private Optional<String> readAirlineCodeFromAirlineRow(int routeIndex) {
        Locator airlineNameSpan = new Locator("AIRLINE_ROW_FLIGHT_NUMBER_SPAN_" + routeIndex,
                By.xpath("(//div[contains(@class,'AirlinesAndFacilities_airlines_content')])[" + routeIndex + "]"));

        Optional<WebElement> rowEl = driverTools.waitForElementToAppear(airlineNameSpan, 5);
        if (rowEl.isEmpty()) {
            return Optional.empty();
        }
        Optional<String> rowText = driverTools.getText(airlineNameSpan);
        if (rowText.isEmpty() || rowText.get().isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(parseAirlineCodeFromFlightNumberLine(rowText.get()));
        } catch (IllegalStateException e) {
            logger.log("Airline row[" + routeIndex + "] text '" + rowText.get()
                    + "' did not contain a recognisable airline code.");
            return Optional.empty();
        }
    }

    private boolean waitForFlightNumberLineVisible(int seconds) {
        return driverTools.waitForElementToAppear(FLIGHT_NUMBER_LINE_IN_REVIEW, seconds).isPresent()
                || driverTools.waitForElementToAppear(REVIEW_CARD_FLIGHT_NUMBER_CANDIDATES, seconds).isPresent();
    }

    /**
     * Reads the first line under the review card that matches the flight-number pattern (airline code + digits).
     */
    private String readFirstFlightNumberLineForMockValidation() {
        driverTools.waitForElementToAppear(FLIGHT_NUMBER_LINE_IN_REVIEW, 20);
        Optional<String> primary = driverTools.getText(FLIGHT_NUMBER_LINE_IN_REVIEW);
        if (primary.isPresent()) {
            try {
                parseAirlineCodeFromFlightNumberLine(primary.get());
                return primary.get();
            } catch (IllegalStateException ignored) {
                logger.log("Primary flight line did not parse as flight number; scanning review_card candidates.");
            }
        }
        List<WebElement> candidates = driverTools.elements(REVIEW_CARD_FLIGHT_NUMBER_CANDIDATES).orElse(List.of());
        for (WebElement p : candidates) {
            String text = p.getText();
            if (text == null || text.isBlank()) {
                continue;
            }
            try {
                parseAirlineCodeFromFlightNumberLine(text);
                return text;
            } catch (IllegalStateException ignored) {
                // try next candidate
            }
        }
        throw new IllegalStateException("Could not find a parsable flight number line after expanding route.");
    }

    /**
     * Parses an IATA airline code (2 alphanumerics, at least one letter) from a free-form
     * flight-number line. Handles every separator the product-review page is known to
     * emit:
     * <ul>
     *     <li><b>New inline format</b> (no expansion): {@code "Lion Air JT748"},
     *         {@code "Super Air Jet IU706"}, {@code "Citilink QG800"}.</li>
     *     <li><b>Older expanded format with dash</b>: {@code "JT - 748"},
     *         {@code "JT-748"}, {@code "8B-152"}.</li>
     *     <li><b>Older expanded format with whitespace only</b>: {@code "JT 748"},
     *         {@code "JT  748"}.</li>
     * </ul>
     * <p>
     * Aircraft model numbers like {@code "737-900ER"}, {@code "Airbus A320"} or
     * {@code "Boeing B737"} must <strong>not</strong> be misidentified as airline
     * codes. To prevent this, the parser:
     * <ul>
     *     <li>scans <em>all</em> matches in the line and prefers a code whose 2
     *     characters are both letters (the dominant Indonesian airline-code shape:
     *     JT, IU, QG, GA, ID, SJ, ...);</li>
     *     <li>only accepts a mixed letter+digit code (e.g. {@code "8B"},
     *     {@code "9W"}) when an explicit separator (whitespace or dash) sits
     *     between the code and the digit suffix - this keeps {@code "A320"} and
     *     {@code "B737"} from being parsed as {@code "A3 + 20"} / {@code "B7 + 37"};</li>
     *     <li>always rejects pure-digit prefixes like {@code "737"}.</li>
     * </ul>
     */
    private static String parseAirlineCodeFromFlightNumberLine(String line) {
        String normalized = line.replace('\u00a0', ' ').trim();

        // Capture group 1 = candidate airline code (2 letters, letter+digit, or digit+letter).
        // Capture group 2 = the separator between the code and the digit-suffix; it may be
        //                   empty (inline like "JT748"), pure whitespace ("JT 748"), or a
        //                   dash with optional surrounding whitespace ("JT-748", "JT - 748").
        // Capture group 3 = the 2-4 digit flight number.
        // Mixed letter+digit codes ("8B", "9W") are only accepted when an explicit
        // separator is present, otherwise aircraft model strings such as "A320" or
        // "B737" would falsely match as "A3 + 20" / "B7 + 37".
        Pattern flightNumberPattern = Pattern.compile(
                "\\b([A-Za-z]{2}|[A-Za-z]\\d|\\d[A-Za-z])(\\s*-\\s*|\\s+|)(\\d{2,4})\\b");
        Matcher matcher = flightNumberPattern.matcher(normalized);

        String letterOnlyMatch = null;
        String mixedMatch = null;
        while (matcher.find()) {
            String code = matcher.group(1);
            String separator = matcher.group(2);
            boolean hasSeparator = !separator.isEmpty();
            boolean isLetterOnly = code.chars().allMatch(Character::isLetter);
            if (isLetterOnly) {
                if (letterOnlyMatch == null) {
                    letterOnlyMatch = code.toUpperCase(Locale.ROOT);
                }
            } else if (hasSeparator) {
                if (mixedMatch == null) {
                    mixedMatch = code.toUpperCase(Locale.ROOT);
                }
            }
        }

        if (letterOnlyMatch != null) {
            return letterOnlyMatch;
        }
        if (mixedMatch != null) {
            return mixedMatch;
        }

        // Final fallback: 3-character ICAO-style codes can appear in older content
        // ("AAA - 123"). Require a dash so we never collide with pure model numbers.
        Matcher icao = Pattern.compile("\\b([A-Za-z0-9]{3})\\s*-\\s*(\\d{1,4})\\b").matcher(normalized);
        while (icao.find()) {
            String code = icao.group(1);
            if (code.chars().anyMatch(Character::isLetter)) {
                return code.toUpperCase(Locale.ROOT);
            }
        }
        throw new IllegalStateException("Could not parse airline code from product review line: " + line);
    }

    public void clickFirstTravelRouteSeeDetails() {
        driverTools.waitForElementToAppear(FIRST_SEE_DETAILS_TRAVEL_ROUTE, 10);
        driverTools.scrollIntoWebView(FIRST_SEE_DETAILS_TRAVEL_ROUTE);
        driverTools.clickWithJavascriptExecutor(FIRST_SEE_DETAILS_TRAVEL_ROUTE);
    }


    public void clickSecondTravelRouteSeeDetails() {
        driverTools.waitForElementToAppear(SECOND_SEE_DETAILS_TRAVEL_ROUTE, 10);
        driverTools.scrollIntoWebView(SECOND_SEE_DETAILS_TRAVEL_ROUTE);
        driverTools.clickWithJavascriptExecutor(SECOND_SEE_DETAILS_TRAVEL_ROUTE);
    }


    public boolean isFlightFacilitiesBottomSheetDisplayed() {
        return driverTools.waitForElementToAppear(FLIGHT_FACILITIES_BOTTOM_SHEET, 10).isPresent();
    }


    public void closeBottomSheet() {
        driverTools.waitForElementToAppear(BOTTOM_SHEET_CLOSE_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(BOTTOM_SHEET_CLOSE_BUTTON);
        driverTools.waitForElementToDisappear(FLIGHT_FACILITIES_BOTTOM_SHEET);
    }


    public boolean isOtherImportantInfoSectionVisible() {
        driverTools.scrollIntoWebView(OTHER_IMPORTANT_INFO_SECTION);
        return driverTools.waitForElementToAppear(OTHER_IMPORTANT_INFO_SECTION, 10).isPresent();
    }


    public void clickRescheduleChipButton() {
        driverTools.waitForElementToAppear(RESCHEDULE_CHIP_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(RESCHEDULE_CHIP_BUTTON);
    }


    public boolean isPriceDetailsSectionVisible() {
        return driverTools.waitForElementToAppear(PRICE_DETAILS_HEADER, 10).isPresent();
    }


    public String getPayNowPrice() {
        driverTools.waitForElementToAppear(PRICE_DETAILS_PAY_NOW, 10).orElseThrow();
        return driverTools.getText(PRICE_DETAILS_PAY_NOW).orElse("");
    }


    public String getDiscountPrice() {
        if (driverTools.waitForElementToAppear(PRICE_DETAILS_DISCOUNT, 5).isPresent()) {
            return driverTools.getText(PRICE_DETAILS_DISCOUNT).orElse("");
        }
        return "";
    }


    public boolean isDiscountPresent() {
        return driverTools.waitForElementToAppear(PRICE_DETAILS_DISCOUNT, 5).isPresent();
    }


    public String getHargaGledekDiscount() {
        if (driverTools.waitForElementToAppear(PRICE_DETAILS_HARGA_GLEDEK, 5).isPresent()) {
            return driverTools.getText(PRICE_DETAILS_HARGA_GLEDEK).orElse("");
        }
        return "";
    }


    public boolean isHargaGledekDiscountPresent() {
        return driverTools.waitForElementToAppear(PRICE_DETAILS_HARGA_GLEDEK, 5).isPresent();
    }


    public String getAdultPrice() {
        driverTools.waitForElementToAppear(PRICE_DETAILS_ADULT_PRICE, 10).orElseThrow();
        return driverTools.getText(PRICE_DETAILS_ADULT_PRICE).orElse("");
    }

    public String getAdultPriceBreakdown() {
        clickPenaltyInfoTabPriceDetail();
        driverTools.waitForElementToAppear(ADULT_PRICE, 10).orElseThrow();
        String price = driverTools.getText(driverTools.element(ADULT_PRICE).orElseThrow());
        return price.replaceAll("[^0-9.]", "");
    }

    public String getTotalPriceOnFareCard(){
        String priceText = driverTools.getText(
                driverTools.element(TOTAL_AMOUNT).orElseThrow()
        );
        return priceText.replaceAll("[^0-9.]", "");
    }

    public String checkTotalPriceBreakdown(){
        clickPenaltyInfoTabPriceDetail();
        double adultPrice = Double.parseDouble(driverTools.getText(driverTools.element(ADULT_PRICE).orElseThrow()).replaceAll("[^0-9.]", ""));
        double childPrice = Double.parseDouble(driverTools.getText(driverTools.element(CHILD_PRICE).orElseThrow()).replaceAll("[^0-9.]", ""));
        double infantPrice = Double.parseDouble(driverTools.getText(driverTools.element(INFANT_PRICE).orElseThrow()).replaceAll("[^0-9.]", ""));
        double total = adultPrice + childPrice + infantPrice;
        driverTools.clickWithJavascriptExecutor(CLOSE);
        return String.format("%.2f", total);
    }

    public boolean validateCurrencyInPrice(String price, String expectedCurrency) {
        return price.contains(expectedCurrency);
    }


    public String getChildPrice() {
        if (driverTools.waitForElementToAppear(PRICE_DETAILS_CHILD_PRICE, 3).isPresent()) {
            return driverTools.getText(PRICE_DETAILS_CHILD_PRICE).orElse("");
        }
        return "";
    }


    public String getInfantPrice() {
        if (driverTools.waitForElementToAppear(PRICE_DETAILS_INFANT_PRICE, 3).isPresent()) {
            return driverTools.getText(PRICE_DETAILS_INFANT_PRICE).orElse("");
        }
        return "";
    }


    public String getTaxPrice() {
        if (driverTools.waitForElementToAppear(PRICE_DETAILS_TAX, 3).isPresent()) {
            return driverTools.getText(PRICE_DETAILS_TAX).orElse("");
        }
        return "";
    }


    public String getServiceFee() {
        if (driverTools.waitForElementToAppear(PRICE_DETAILS_SERVICE_FEE, 3).isPresent()) {
            return driverTools.getText(PRICE_DETAILS_SERVICE_FEE).orElse("");
        }
        return "";
    }


    public double extractNumericValue(String priceString) {
        if (priceString == null || priceString.isEmpty()) {
            return 0.0;
        }

        try {
            // convertPriceSafely already handles negative values by preserving the minus sign
            // So we don't need to manually check for "-" and negate
            return CoreFunctions.convertPriceSafely(priceString);
        } catch (NumberFormatException e) {
            logger.log("Failed to parse price: " + priceString + ", error: " + e.getMessage());
            return 0.0;
        }
    }

    public void openFlightFacilitiesSectionViaChevron(int index) {
        Locator FLIGHT_FACILITIES_BUTTON_DYNAMIC = getFlightFacilitiesButtonByIndex(index);
        driverTools.waitForElementToAppear(FLIGHT_FACILITIES_BUTTON_DYNAMIC, 10);
        driverTools.clickWithJavascriptExecutor(FLIGHT_FACILITIES_BUTTON_DYNAMIC);
    }

    private Locator getFlightFacilitiesButtonByIndex(int index) {
        // Each candidate scopes the [index] to a single class family so document
        // order can never count rows and chevrons together (which would shift
        // positions when both DOM versions render side-by-side). Probe in
        // priority order: new desktop DOM first, then the two legacy class
        // names. The first family that actually exists on the page wins.
        Locator newDomRow = new Locator("FLIGHT_FACILITIES_BUTTON_NEW_ROW_" + index,
                By.xpath("(//div[contains(@class,'AirlinesAndFacilitiesDesktop_main_row')])[" + index + "]"));
        if (driverTools.isElementPresent(newDomRow)) {
            return newDomRow;
        }
        Locator legacyCollapseIcon = new Locator("FLIGHT_FACILITIES_BUTTON_LEGACY_COLLAPSE_" + index,
                By.xpath("(//div[contains(@class,'review_card')]//div[contains(@class,'FlightRouteCard_collapse_icon')]"
                        + " | //div[contains(@class,'FlightRouteCard_collapse_icon')])[" + index + "]"));
        if (driverTools.isElementPresent(legacyCollapseIcon)) {
            return legacyCollapseIcon;
        }
        return new Locator("FLIGHT_FACILITIES_BUTTON_LEGACY_CHEVRON_" + index,
                By.xpath("(//div[contains(@class,'review_card')]//div[contains(@class,'AnimatedChevronIcon_icon_chevron')]"
                        + " | //div[contains(@class,'AnimatedChevronIcon_icon_chevron')])[" + index + "]"));
    }

    public boolean isFAQSectionVisible() {
        driverTools.scrollIntoWebView(FAQ_INFO_SECTION);
        return driverTools.waitForElementToAppear(FAQ_INFO_SECTION, 10).isPresent();
    }

    /**
     * Verifies Refund Tab Section contains:
     * - Refund section
     * - FAQ section
     * - Other Important Information section
     */
    public boolean verifyRefundTabSection() {
        return isFAQSectionVisible() && isOtherImportantInfoSectionVisible();
    }

    /**
     * Verifies Reschedule Tab Section contains:
     * - Reschedule section
     * - Other Important Information section
     */
    public boolean verifyRescheduleTabSection() {
        return isRescheduleSectionVisible() && isOtherImportantInfoSectionVisible();
    }

    /**
     * Gets all price breakdown details as a map.
     * Handles roundtrip flights (multiple passenger prices) and extra protection.
     * @return Map with price component names as keys and their values
     */
    public Map<String, Double> getPriceBreakdown(String currency) {
        Map<String, Double> breakdown = new LinkedHashMap<>();

        // Get ALL price items dynamically - handles roundtrip and all add-ons
        // Locator for all price labels (excludes cashback and pay now)
        String allPriceLabelsXpath = "//div[contains(@class,'PriceDetail_price_item')]//span[contains(@class,'PriceDetail_label') and not(contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'cashback')) and not(contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'pay now')) and not(contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'bayar sekarang'))]";
        String allPriceValuesXpath = "//div[contains(@class,'PriceDetail_price_item')]//span[contains(@class,'PriceDetail_label') and not(contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'cashback')) and not(contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'pay now')) and not(contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'bayar sekarang'))]//following-sibling::span";

        Locator labelsLocator = new Locator("ALL_PRICE_LABELS", new By.ByXPath(allPriceLabelsXpath));
        Locator valuesLocator = new Locator("ALL_PRICE_VALUES", new By.ByXPath(allPriceValuesXpath));

        java.util.List<WebElement> labels = driverTools.elements(labelsLocator).orElse(java.util.Collections.emptyList());
        java.util.List<WebElement> values = driverTools.elements(valuesLocator).orElse(java.util.Collections.emptyList());

        logger.log("Found " + labels.size() + " price labels and " + values.size() + " price values");

        int itemIndex = 1;
        int minSize = Math.min(labels.size(), values.size());
        for (int i = 0; i < minSize; i++) {
            String label = driverTools.getText(labels.get(i));
            String value = driverTools.getText(values.get(i));

            if (label == null || value == null || label.isEmpty() || value.isEmpty()) {
                continue;
            }

            // Skip if already a key or if it's pay now / cashback
            String normalizedLabel = label.toLowerCase();
            if (normalizedLabel.contains("cashback") || normalizedLabel.contains("pay now") || normalizedLabel.contains("bayar sekarang")) {
                continue;
            }

            double numericValue = extractNumericValue(value);

            // Handle discount/deduction (negative values - shown in green with minus sign)
            boolean isDiscount = normalizedLabel.contains("discount") || normalizedLabel.contains("diskon") ||
                                normalizedLabel.contains("deduction") || normalizedLabel.contains("potongan") ||
                                normalizedLabel.contains("gledek") || value.contains("-");

            if (isDiscount && numericValue > 0) {
                numericValue = -numericValue; // Make discount negative
            }

            // Create unique key for multiple items with same label (e.g., Adult for departure and return)
            String uniqueKey = label;
            if (breakdown.containsKey(label)) {
                uniqueKey = label + " (" + itemIndex + ")";
                itemIndex++;
            }

            breakdown.put(uniqueKey, numericValue);
            logger.log("Price item: " + uniqueKey + " = " + value + " (" + numericValue + ")");
        }

        if (breakdown.isEmpty()) {
            breakdown.putAll(getFareCardDetailPopUpPriceBreakdown(currency));
        }

        // If dynamic approach didn't find items, fallback to individual getters
        if (breakdown.isEmpty()) {
            logger.log("Fallback to individual price getters");

            // Get adult price(s) - all occurrences
            getAllPricesByType("Adult", "Dewasa", breakdown);

            // Get child price(s) - all occurrences
            getAllPricesByType("Child", "Anak", breakdown);

            // Get infant price(s) - all occurrences
            getAllPricesByType("Infant", "Bayi", breakdown);

            // Get extra protection / insurance prices
            getAllPricesByType("Protection", "Proteksi", breakdown);
            getAllPricesByType("Insurance", "Asuransi", breakdown);

            // Get tax
            String tax = getTaxPrice();
            if (!tax.isEmpty()) {
                breakdown.put("Tax", extractNumericValue(tax));
                logger.log("Tax: " + tax + " = " + extractNumericValue(tax));
            }

            // Get service fee
            String serviceFee = getServiceFee();
            if (!serviceFee.isEmpty()) {
                breakdown.put("Service Fee", extractNumericValue(serviceFee));
                logger.log("Service Fee: " + serviceFee + " = " + extractNumericValue(serviceFee));
            }

            // Get discount (negative value)
            String discount = getDiscountPrice();
            if (!discount.isEmpty()) {
                double discountValue = extractNumericValue(discount);
                breakdown.put("Discount", discountValue > 0 ? -discountValue : discountValue);
                logger.log("Discount: " + discount + " = " + breakdown.get("Discount"));
            }
        }

        return breakdown;
    }

    /**
     * Gets all prices for a specific type (handles multiple occurrences for roundtrip).
     * @param typeEng English label
     * @param typeId Indonesian label
     * @param breakdown Map to add prices to
     */
    private void getAllPricesByType(String typeEng, String typeId, Map<String, Double> breakdown) {
        String xpath = "//div[contains(@class,'PriceDetail_price_item')]//span[contains(text(),'" + typeEng + "') or contains(text(),'" + typeId + "')]/following-sibling::span";
        Locator locator = new Locator("PRICE_" + typeEng.toUpperCase(), new By.ByXPath(xpath));

        java.util.List<WebElement> priceElements = driverTools.elements(locator).orElse(java.util.Collections.emptyList());

        int index = 1;
        for (WebElement element : priceElements) {
            String priceText = driverTools.getText(element);
            if (priceText != null && !priceText.isEmpty()) {
                double value = extractNumericValue(priceText);
                String key = priceElements.size() > 1 ? typeEng + " (" + index + ")" : typeEng;
                breakdown.put(key, value);
                logger.log(key + ": " + priceText + " = " + value);
                index++;
            }
        }
    }

    /**
     * Validates price breakdown calculation by comparing sum of components with total.
     * Includes verification of:
     * - Price breakdown items (Adult, Child, Infant, Tax, Discount, etc.)
     * - Pay Now amount (Bayar sekarang)
     * - Cashback items (Flight cashback, Transaction cashback)
     * - Total after cashback
     * - Bottom sheet total matches Total after cashback (±1 IDR tolerance)
     *
     * @param expectedCurrency the expected currency code (e.g., "IDR", "SGD")
     */
    public void validatePriceBreakdownAndTotal(PriceDetail priceDetail, String expectedCurrency) {
        // Validate required fields
        if (priceDetail == null) {
            throw new IllegalArgumentException("PriceDetail cannot be null");
        }

        if (priceDetail.adult < 1) {
            throw new IllegalArgumentException("At least 1 adult passenger is required for booking. Got: " + priceDetail.adult);
        }

        if (priceDetail.child < 0) {
            throw new IllegalArgumentException("Child count cannot be negative. Got: " + priceDetail.child);
        }

        if (priceDetail.infant < 0) {
            throw new IllegalArgumentException("Infant count cannot be negative. Got: " + priceDetail.infant);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n===== COMPLETE PRICE BREAKDOWN VALIDATION =====\n");
        sb.append("Passengers: Adult=").append(priceDetail.adult)
          .append(", Child=").append(priceDetail.child)
          .append(", Infant=").append(priceDetail.infant).append("\n");

        // 1. Get and log all price breakdown items
        Map<String, Double> breakdown = getPriceBreakdown(expectedCurrency);
        double calculatedTotal = 0.0;

        // Edge case: Empty breakdown
        if (breakdown.isEmpty()) {
            logger.log("WARNING: Price breakdown is empty - no price items found");
            sb.append("--- Price Items ---\n");
            sb.append("  [NO ITEMS FOUND]\n");
        } else {
            sb.append("--- Price Items ---\n");
            for (Map.Entry<String, Double> entry : breakdown.entrySet()) {
                double value = entry.getValue();
                // Edge case: Check for NaN or Infinity
                if (Double.isNaN(value) || Double.isInfinite(value)) {
                    logger.log("WARNING: Invalid price value detected: " + entry.getKey() + " = " + value);
                    value = 0.0; // Use 0 for invalid values
                    entry.setValue(0.0); // Update the map entry to maintain consistency
                }
                sb.append(String.format("  %s: %,.2f\n", entry.getKey(), value));
                calculatedTotal += value;
            }
        }

        // Edge case: Zero total
        if (calculatedTotal == 0.0 && !breakdown.isEmpty()) {
            logger.log("WARNING: Calculated total is 0.0 but breakdown is not empty - possible issue with price extraction");
        }

        // 2. Get Pay Now total
        String payNowPriceStr = getPayNowPrice();
        double payNowTotal = extractNumericValue(payNowPriceStr);

        // Edge case: Invalid Pay Now total
        if (Double.isNaN(payNowTotal) || Double.isInfinite(payNowTotal)) {
            logger.log("ERROR: Pay Now total is invalid: " + payNowTotal);
            payNowTotal = 0.0;
        }

        // Edge case: Zero Pay Now total
        if (payNowTotal == 0.0) {
            logger.log("WARNING: Pay Now total is 0.0 - this might indicate an issue");
        }

        sb.append("  --------------------------\n");
        sb.append(String.format("  Calculated Total: %,.2f\n", calculatedTotal));
        sb.append(String.format("  Pay Now (Bayar sekarang): %,.2f\n", payNowTotal));

        // 3. Get Cashback items
        sb.append("--- Cashback Items ---\n");
        Map<String, String> cashbackItems = getCashbackItems();
        int flightCashbackCount = 0;
        int transactionCashbackCount = 0;

        for (Map.Entry<String, String> entry : cashbackItems.entrySet()) {
            String label = entry.getKey().toLowerCase();
            String value = entry.getValue();
            sb.append(String.format("  %s: %s\n", entry.getKey(), value));

            if (label.contains("flight") || label.contains("penerbangan")) {
                flightCashbackCount++;
            } else if (label.contains("transaction") || label.contains("transaksi")) {
                transactionCashbackCount++;
            }
        }

        sb.append(String.format("  Found %d Flight cashback item(s) and %d Transaction cashback item(s)\n",
            flightCashbackCount, transactionCashbackCount));

        // 4. Get Total after cashback
        String totalAfterCashbackStr = getTotalAfterCashback();
        double totalAfterCashback = extractNumericValue(totalAfterCashbackStr);
        sb.append(String.format("  Total after cashback: %,.2f\n", totalAfterCashback));

        // 5. Get Bottom sheet total
        String bottomSheetTotalStr = getBottomSheetTotalAmount();
        double bottomSheetTotal = extractNumericValue(bottomSheetTotalStr);
        sb.append(String.format("  Bottom Sheet Total: %,.2f\n", bottomSheetTotal));

        // 6. Calculate difference for rounding check
        double bottomSheetDifference = Math.abs(totalAfterCashback - bottomSheetTotal);
        sb.append(String.format("  Difference (Total vs Bottom Sheet): %.2f (±1 allowed)\n", bottomSheetDifference));

        sb.append("================================================\n");
        logger.log(sb.toString());

        // Assertions

        // Validate currency in all price items
        validateCurrencyInPriceItems(expectedCurrency);

        // Validate currency in Pay Now
        Assertion.assertThat("Pay now price should contain expected currency: " + expectedCurrency,
                payNowPriceStr.contains(expectedCurrency), is(true));

        // Validate discount currency if present
        if (isDiscountPresent()) {
            String discountPrice = getDiscountPrice();
            Assertion.assertThat("Discount should contain expected currency: " + expectedCurrency,
                    discountPrice.contains(expectedCurrency), is(true));
        }

        // Validate price breakdown sum equals Pay Now total
        // Use tolerance-based comparison for floating point precision (0.01 IDR tolerance)
        double priceDifference = Math.abs(calculatedTotal - payNowTotal);
        Assertion.assertThat("Price breakdown sum should equal Pay Now total (difference: " + priceDifference + ")",
                priceDifference, lessThanOrEqualTo(0.01));

        // Validate passenger prices presence based on counts
        validatePassengerPricesPresence(breakdown, priceDetail);

        // Validate Cashback items are present (at least one type should be present)
        boolean hasCashback = flightCashbackCount > 0 || transactionCashbackCount > 0;
        Assertion.assertThat("At least one cashback type (Flight or Transaction) should be displayed", hasCashback, is(true));

        // Log cashback counts for debugging
        if (flightCashbackCount > 0) {
            logger.log("Flight cashback entries found: " + flightCashbackCount);
        }
        if (transactionCashbackCount > 0) {
            logger.log("Transaction cashback entries found: " + transactionCashbackCount);
        }

        // Validate Total after cashback is displayed
        Assertion.assertThat("Total after cashback should be displayed", totalAfterCashbackStr, is(not(nullValue())));

        // Validate Bottom sheet total matches Total after cashback (±1 IDR rounding tolerance)
        Assertion.assertThat("Bottom sheet total should match Total after cashback (±1 IDR tolerance)",
                bottomSheetDifference, lessThanOrEqualTo(1.0));
    }

    /**
     * Validates that currency is present in all numeric price items
     */
    private void validateCurrencyInPriceItems(String currency) {
        logger.log("--- Validating Currency in All Price Items (Mandatory) ---");
        int itemsWithCurrency = 0;
        int totalItems = 0;
        int skippedTextOnlyItems = 0;

        // Get price items from UI (with currency symbols)
        List<WebElement> values = driverTools.elements(ALL_PRICE_VALUES).orElse(Collections.emptyList());

        for (WebElement valueElement : values) {
            String value = driverTools.getText(valueElement);
            if (value == null || value.isEmpty()) {
                logger.log("Currency validation skipped - NULL or empty value");
                skippedTextOnlyItems++;
                continue;
            }

            // Skip text-only values (no numbers) like "Included", "Free", etc.
            if (!value.matches(".*\\d.*")) {
                logger.log("Currency validation skipped - Text-only value (no numbers): " + value);
                skippedTextOnlyItems++;
                continue;
            }

            totalItems++;
            if (value.contains(currency)) {
                itemsWithCurrency++;
                logger.log("Currency " + currency + " found in: " + value);
            } else {
                logger.log("Currency " + currency + " NOT found in: " + value);
            }
        }

        logger.log("Currency validation: " + itemsWithCurrency + "/" + totalItems + " items contain " + currency +
                  " (" + skippedTextOnlyItems + " text-only items skipped)");

        // Handle edge case: If all items are text-only (e.g., "Free", "Included"), skip currency validation
        if (totalItems == 0) {
            logger.log("WARNING: All price items are text-only (no numeric values found). Currency validation skipped.");
            logger.log("This is acceptable if all price items are non-numeric (e.g., 'Free', 'Included').");
            return;
        }

        double currencyPercentage = (itemsWithCurrency * 100.0 / totalItems);
        logger.log("Currency coverage: " + String.format("%.1f%%", currencyPercentage));

        String currencyMessage = String.format("Currency %s should be present in all %d price items (found in %d items = %.1f%%)",
                                               currency, totalItems, itemsWithCurrency, currencyPercentage);
        Assertion.assertThat(currencyMessage, currencyPercentage, is(100.0));
    }

    /**
     * Validates that passenger prices are present in breakdown based on passenger counts.
     * Only validates passenger types with count > 0.
     * Adult count must always be >= 1 (at least 1 adult required for booking).
     * Skips validation for child and infant if their counts are 0.
     */
    private void validatePassengerPricesPresence(Map<String, Double> breakdown, PriceDetail priceDetail) {
        // Validate that adult count is at least 1 (mandatory requirement)
        Assertion.assertThat("At least 1 adult passenger is required for booking",
            priceDetail.adult >= 1, is(true));

        // Always validate Adult prices (at least 1 adult is required for booking)
        logger.log("--- Validating Adult Passenger Price Presence (Count: " + priceDetail.adult + ") ---");

        long adultEntryCount = breakdown.keySet().stream()
            .filter(key -> key.toLowerCase().contains("adult") || key.toLowerCase().contains("dewasa"))
            .count();

        Assertion.assertThat("Adult price should be present for " + priceDetail.adult + " passenger(s)",
            adultEntryCount, greaterThanOrEqualTo(1L));

        logger.log("Found " + adultEntryCount + " Adult price entry/entries");

        // Only validate Child prices if child count > 0
        if (priceDetail.child > 0) {
            logger.log("--- Validating Child Passenger Price Presence (Count: " + priceDetail.child + ") ---");

            long childEntryCount = breakdown.keySet().stream()
                .filter(key -> key.toLowerCase().contains("child") || key.toLowerCase().contains("anak"))
                .count();

            Assertion.assertThat("Child price should be present for " + priceDetail.child + " passenger(s)",
                childEntryCount, greaterThanOrEqualTo(1L));

            logger.log("Found " + childEntryCount + " Child price entry/entries");
        } else {
            logger.log("--- Child validation skipped (count: 0) ---");
        }

        // Only validate Infant prices if infant count > 0
        if (priceDetail.infant > 0) {
            logger.log("--- Validating Infant Passenger Price Presence (Count: " + priceDetail.infant + ") ---");

            long infantEntryCount = breakdown.keySet().stream()
                .filter(key -> key.toLowerCase().contains("infant") || key.toLowerCase().contains("bayi"))
                .count();

            Assertion.assertThat("Infant price should be present for " + priceDetail.infant + " passenger(s)",
                infantEntryCount, greaterThanOrEqualTo(1L));

            logger.log("Found " + infantEntryCount + " Infant price entry/entries");
        } else {
            logger.log("--- Infant validation skipped (count: 0) ---");
        }
    }

    /**
     * Gets all cashback items from the price detail section.
     * Handles multiple entries with the same label by adding an index (e.g., "Flight cashback (1)", "Flight cashback (2)").
     * @return Map of cashback label to value (e.g., "Flight cashback (1)" -> "Get 8,166 points")
     */
    public Map<String, String> getCashbackItems() {
        Map<String, String> cashbackItems = new LinkedHashMap<>();
        Map<String, Integer> labelCounts = new java.util.HashMap<>();

        Locator labelsLocator = new Locator("CASHBACK_LABELS", new By.ByXPath(CASHBACK_LABELS_XPATH));
        Locator amountsLocator = new Locator("CASHBACK_AMOUNTS", new By.ByXPath(CASHBACK_AMOUNTS_XPATH));

        java.util.List<WebElement> labels = driverTools.elements(labelsLocator).orElse(java.util.Collections.emptyList());
        java.util.List<WebElement> amounts = driverTools.elements(amountsLocator).orElse(java.util.Collections.emptyList());

        logger.log("Found " + labels.size() + " cashback labels and " + amounts.size() + " cashback amounts");

        int minSize = Math.min(labels.size(), amounts.size());
        for (int i = 0; i < minSize; i++) {
            String label = driverTools.getText(labels.get(i));
            String amount = driverTools.getText(amounts.get(i));
            if (label != null && amount != null) {
                // Track how many times we've seen this label
                int count = labelCounts.getOrDefault(label, 0) + 1;
                labelCounts.put(label, count);

                // If this is the second+ occurrence, add an index
                String key = count > 1 ? label + " (" + count + ")" : label;

                cashbackItems.put(key, amount);
                logger.log("Cashback: " + key + " = " + amount);
            }
        }

        return cashbackItems;
    }

    /**
     * Gets the "Total after cashback" / "Total setelah cashback" amount.
     * @return Total after cashback string or null if not found
     */
    public String getTotalAfterCashback() {
        Locator totalLocator = new Locator("TOTAL_AFTER_CASHBACK", new By.ByXPath(TOTAL_AFTER_CASHBACK_XPATH));
        if (driverTools.waitForElementToPresence(totalLocator, 3).isPresent()) {
            String amount = driverTools.getText(totalLocator).orElse(null);
            logger.log("Total after cashback: " + amount);
            return amount;
        }
        logger.log("Total after cashback not found");
        return null;
    }

    /**
     * Gets the total amount from the bottom sheet footer.
     * This is the final price shown at the bottom (e.g., "IDR 1.775.133")
     * @return Bottom sheet total amount or null if not found
     */
    public String getBottomSheetTotalAmount() {
        if (driverTools.waitForElementToPresence(BOTTOM_SHEET_TOTAL_AMOUNT, 3).isPresent()) {
            String amount = driverTools.getText(BOTTOM_SHEET_TOTAL_AMOUNT).orElse(null);
            logger.log("Bottom Sheet Total Amount: " + amount);
            return amount;
        }
        logger.log("Bottom Sheet Total Amount not found");
        return null;
    }


    public void openFlightFacilities() {
        driverTools.waitForElementToAppear(FLIGHT_ROUTE_CARD, 20).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(FLIGHT_FACILITIES_BUTTON, 20).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public boolean isFlightFacilitiesSectionVisible(){
        return driverTools.waitForElementToAppear(FLIGHT_FACILITIES_SECTION,20).isPresent();
    }

    public void closeFlightFacilitiesSection(){
        driverTools.waitForElementToAppear(FLIGHT_FACILITY_CLOSE_BUTTON,20);
        driverTools.clickWithJavascriptExecutor(FLIGHT_FACILITY_CLOSE_BUTTON);
        driverTools.waitForElementToDisappear(FLIGHT_FACILITIES_SECTION,20);
    }

    public boolean isRefundSectionVisible(){
        return driverTools.waitForElementToAppear(REFUND_SECTION,20).isPresent();
    }

    public void clickRescheduleButtonChip(){
        driverTools.waitForElementToAppear(RESCHEDULE_BUTTON_CHIP,20);
        driverTools.clickWithJavascriptExecutor(RESCHEDULE_BUTTON_CHIP);
    }

    public boolean isRescheduleSectionVisible(){
        return driverTools.waitForElementToAppear(RESCHEDULE_SECTION,20).isPresent();
    }

    public boolean verifyFAQSection(){
        boolean is_faq_section_displayed = driverTools.waitForElementToAppear(FAQ_SECTION,20).isPresent();
        logger.log("is FAQ section displayed : " + is_faq_section_displayed);
        driverTools.scrollIntoWebView(FAQ_SECTION);
        driverTools.waitForElementToAppear(FAQ_QUESTION, 20);
        driverTools.clickWithJavascriptExecutor(FAQ_QUESTION);
        driverTools.clickWithJavascriptExecutor(ARROW);
        Optional<WebElement> faqAnswer = driverTools.waitForElementToAppear(FAQ_ANSWER, 20);
        Assertion.assertThat("Verify FAQ answer is visible after clicking question", faqAnswer.isPresent(), is(true));
        return is_faq_section_displayed;
    }

    public void clickSelectBtnRegularReturn(){
        try {
            driverTools.waitForElementToPresence(SELECT_BTN_REGULAR);
            driverTools.clickWithJavascriptExecutor(SELECT_BTN_REGULAR);
        }catch(Exception e){
            driverTools.clickWithJavascriptExecutor(SELECT_BTN_REGULAR);
        }
   }

    public boolean isCheckInAgainInfoVisible() {
        if (driverTools.waitForElementToAppear(CHECK_IN_AGAIN_LIST_ITEM, 10).isPresent()) {
            return true;
        }
        return driverTools.waitForElementToAppear(SELF_TRANSFER_PROTECTION_INFO, 5).isPresent();
    }

    /**
     * Clicks the "Self transfer ... in/di &lt;city&gt;" header on the route card to
     * open the {@code TransitDetailInfoPopUp} modal that lists transit instructions
     * and protection coverage.
     */
    public void openSelfTransferDetails() {
        driverTools.waitForElementToAppear(SELF_TRANSFER_TITLE, 10);
        driverTools.scrollIntoWebView(SELF_TRANSFER_TITLE);
        driverTools.clickWithJavascriptExecutor(SELF_TRANSFER_TITLE);
    }

    /**
     * Verifies that the self-transfer popup confirms protection coverage.
     * EN: "Self Transfer Protection included" / ID: "Termasuk Proteksi Transit Mandiri".
     */
    public boolean isSelfTransferProtectionIncludedVisible() {
        return driverTools.waitForElementToAppear(SELF_TRANSFER_PROTECTION_TITLE, 10).isPresent();
    }

    public boolean isFullRefundAvail(){
        driverTools.waitForElementToAppear(FULL_REFUND_OPTION);
        return driverTools.isElementPresent(FULL_REFUND_OPTION);
    }

    private boolean fareCardHasFreeMeal(List<String> fareCardInfoTexts) {
        for (String text : fareCardInfoTexts) {
            String textLowerCase = text.toLowerCase();
            if ((textLowerCase.contains("free:") || textLowerCase.contains("gratis:"))
                    && (textLowerCase.contains("meal selection") || textLowerCase.contains("pilih makanan"))) {
                return true;
            }
            if (textLowerCase.contains("meals:") && textLowerCase.contains("free")) {
                return true;
            }
            if (textLowerCase.contains("makanan") && textLowerCase.contains("free")) {
                return true;
            }
        }
        return false;
    }

    private boolean fareCardHasFreeSeat(List<String> fareCardInfoTexts) {
        for (String text : fareCardInfoTexts) {
            String textLowerCase = text.toLowerCase();
            if ((textLowerCase.contains("free:") || textLowerCase.contains("gratis:"))
                    && (textLowerCase.contains("seat selection") || textLowerCase.contains("pilih kursi"))) {
                return true;
            }
            if (textLowerCase.contains("seats:") && textLowerCase.contains("free")) {
                return true;
            }
            if (textLowerCase.contains("seat") && textLowerCase.contains("free")) {
                return true;
            }
        }
        return false;
    }

    private boolean fareCardMatchesCriteria(List<String> fareCardInfoTexts, List<String> criteriaList) {
        if (criteriaList.contains("free meal") && !fareCardHasFreeMeal(fareCardInfoTexts)) {
            return false;
        }
        if (criteriaList.contains("free seat") && !fareCardHasFreeSeat(fareCardInfoTexts)) {
            return false;
        }
        return true;
    }

    public void selectFare(String criteria){
        List<String> criteriaList = Arrays.asList(criteria.toLowerCase().split("-"));
        ArrayList<Integer> eligibleFareIndex = new ArrayList<>();

        driverTools.waitForElementToPresence(SELECT_BTN_REGULAR);
        int fareCardCount = driverTools.elements(FARE_CARD_WRAPPER).get().size();
        int iterations = fareCardCount;
        if (driverTools.isElementPresent(NEXT_FARE_BUTTON)) {
            iterations = Math.max(fareCardCount, 4);
        }

        for (int i = 0; i < iterations; i++) {

            if (i > 2 && driverTools.isElementPresent(NEXT_FARE_BUTTON)) {
                driverTools.waitForElementToAppear(NEXT_FARE_BUTTON);
                driverTools.click(NEXT_FARE_BUTTON);
                driverTools.waitForElementToDisappear(NEXT_FARE_BUTTON,2);

                for (int j = 0; j < 5; j++) {
                    List<String> fareCardInfoTexts = driverTools.getTexts(getFareCardInfoTextLocatorByFareCardIndex(i + 1));
                    if (fareCardInfoTexts.isEmpty()) {
                        driverTools.waitForElementToAppear(getSelectFareCardButtonLocatorByIndex(i + 1),1);
                    } else {
                        break;
                    }
                }
            }

            int cardIndex = i + 1;
            if (cardIndex > driverTools.elements(FARE_CARD_WRAPPER).get().size()) {
                continue;
            }

            List<String> fareCardInfoTexts = driverTools.getTexts(getFareCardInfoTextLocatorByFareCardIndex(cardIndex));
            if (fareCardMatchesCriteria(fareCardInfoTexts, criteriaList)) {
                eligibleFareIndex.add(i);
            }
        }

        if (eligibleFareIndex.size() == 1) {
            int index = eligibleFareIndex.get(0)+1;
            driverTools.clickWithJavascriptExecutor(getSelectFareCardButtonLocatorByIndex(index));
        } else if (eligibleFareIndex.size() > 1) {
            int index = CoreFunctions.generateRandomInt(0,eligibleFareIndex.size()-1);
            driverTools.clickWithJavascriptExecutor(getSelectFareCardButtonLocatorByIndex(eligibleFareIndex.get(index)+1));
        } else {
            throw new NoSuchElementException("No eligible fare found to select");
        }
    }

    private Locator getFareCardInfoTextLocatorByFareCardIndex(int index) {
        return new Locator("FARE_CARD_INFO_TEXTS_" + index,
                By.xpath("(//div[contains(@class,'FareCard_box')])["+index+"]//*[contains(@class,'FareCard_info_text')]/span"));
    }

    private Locator getSelectFareCardButtonLocatorByIndex(int index) {
        return new Locator("FARE_CARD_SELECT_BUTTON_" + index,
                By.xpath("(//div[contains(@class,'FareCard_box')])["+index+"]//button[contains(@class,'FareCard_select_btn')]"));
    }

    public boolean is100pRefundFareCardDisplayed() {
        return driverTools.waitForElementToAppear(REFUND_100P_FARE_CARD, 15).isPresent();
    }

    public boolean is100pRefundFareCardAddonWordingDisplayed() {
        return driverTools.waitForElementToAppear(REFUND_100P_FARE_CARD_ADDON_PRICE, 10).isPresent()
                && driverTools.waitForElementToAppear(REFUND_100P_FARE_CARD_ADDON_DESCRIPTION, 10).isPresent();
    }

    private void logSelected100pRefundFareCard() {
        driverTools.getText(REFUND_100P_FARE_CARD_FOOTER_LABEL).ifPresent(label ->
                logger.log("Selected 100% Refund fare card footer: " + label));
    }

    public String get100pRefundFareCardAddonWordingText() {
        driverTools.scrollIntoWebView(REFUND_100P_FARE_CARD_ADDON_PRICE);
        driverTools.waitForElementToAppear(REFUND_100P_FARE_CARD_ADDON_PRICE, 10);
        logSelected100pRefundFareCard();
        String priceText = driverTools.getText(REFUND_100P_FARE_CARD_ADDON_PRICE).orElse("");
        String descriptionText = driverTools.getText(REFUND_100P_FARE_CARD_ADDON_DESCRIPTION).orElse("");
        String wording = (priceText + descriptionText).trim();
        if (wording.isBlank()) {
            throw new IllegalStateException("100% Refund fare card '+ XXX get full refund' wording is not visible");
        }
        logger.log("100% Refund fare card addon wording on PRP: " + wording);
        return wording;
    }

    public long get100pRefundFareCardAddonPricePerPerson() {
        driverTools.scrollIntoWebView(REFUND_100P_FARE_CARD_ADDON_PRICE);
        driverTools.waitForElementToAppear(REFUND_100P_FARE_CARD_ADDON_PRICE, 10);
        logSelected100pRefundFareCard();
        String priceText = driverTools.getText(REFUND_100P_FARE_CARD_ADDON_PRICE).orElse("");
        if (priceText.isBlank()) {
            throw new IllegalStateException("100% Refund fare card addon price is not visible");
        }
        long perPerson = Long.parseLong(CoreFunctions.removeNonDigit(priceText));
        logger.log("100% Refund fare card addon per-person price on PRP: " + perPerson + " (raw: " + priceText + ")");
        return perPerson;
    }

    public double get100pRefundFareCardAddonPriceFromWording() {
        return get100pRefundFareCardAddonPricePerPerson();
    }

    public void clickSeeDetailsOn100pRefundFareCard() {
        driverTools.scrollIntoWebView(REFUND_100P_SEE_DETAILS);
        driverTools.waitForElementToAppear(REFUND_100P_SEE_DETAILS);
        driverTools.clickWithJavascriptExecutor(REFUND_100P_SEE_DETAILS);
    }

    public void clickSelect100pRefundFareCard() {
        driverTools.scrollIntoWebView(SELECT_BTN_100P_REFUND);
        driverTools.waitForElementToAppear(SELECT_BTN_100P_REFUND);
        driverTools.clickWithJavascriptExecutor(SELECT_BTN_100P_REFUND);
    }

    public void clickSelectTicketOn100pRefundFareCardPopup() {
        if (driverTools.waitForElementToAppear(SELECT_TICKET_100P_REFUND_POPUP, 5).isPresent()) {
            driverTools.scrollIntoWebView(SELECT_TICKET_100P_REFUND_POPUP);
            driverTools.clickWithJavascriptExecutor(SELECT_TICKET_100P_REFUND_POPUP);
            return;
        }
        clickSelect100pRefundFareCard();
    }

    public boolean is100pRefundPolicyVisibleOnRefundTab() {
        return driverTools.waitForElementToAppear(REFUND_100P_POLICY_TIMELINE, 10).isPresent();
    }

    private String extractPriceTextFromElement(WebElement element) {
        String text = driverTools.getText(element);
        if (containsNumericPrice(text)) {
            return text.trim();
        }
        try {
            String textContent = element.getAttribute("textContent");
            if (containsNumericPrice(textContent)) {
                return textContent.trim();
            }
        } catch (Exception ignored) {
            // fall through
        }
        return "";
    }

    private boolean containsNumericPrice(String text) {
        return text != null && !text.isBlank() && text.replaceAll("[^0-9]", "").length() > 0;
    }

    private int getPaxCountFromBreakdownLabel(String label, int defaultPax) {
        if (label == null || label.isBlank()) {
            return defaultPax;
        }
        Matcher paxMatcher = Pattern.compile("\\(x(\\d+)\\)", Pattern.CASE_INSENSITIVE).matcher(label);
        return paxMatcher.find() ? Integer.parseInt(paxMatcher.group(1)) : defaultPax;
    }

    private String get100pRefundBreakdownAddonLabelText() {
        driverTools.waitForElementToAppear(REFUND_100P_BREAKDOWN_LABEL, 10);
        return driverTools.getText(REFUND_100P_BREAKDOWN_LABEL).orElse("");
    }

    private String get100pRefundBreakdownAddonAmountText(String currency) {
        Locator amountLocator = getRefund100pBreakdownAmountLocator(currency);
        driverTools.waitForElementToAppear(amountLocator, 10);
        return driverTools.getText(amountLocator).orElse("");
    }

    private boolean is100pRefundAddonPresentInPriceBreakdown(String currency) {
        Locator amountLocator = getRefund100pBreakdownAmountLocator(currency);
        return driverTools.waitForElementToAppear(REFUND_100P_BREAKDOWN_LABEL, 10).isPresent()
                && driverTools.waitForElementToAppear(amountLocator, 10).isPresent();
    }

    private void ensure100pRefundAddonInBreakdown(Map<String, Double> breakdown, String currency) {
        if (breakdown.entrySet().stream().anyMatch(entry -> {
            String key = entry.getKey().toLowerCase();
            return key.contains("100") && key.contains("refund");
        })) {
            return;
        }
        String label = get100pRefundBreakdownAddonLabelText();
        String amountText = get100pRefundBreakdownAddonAmountText(currency);
        if (label.isBlank() || amountText.isBlank()) {
            return;
        }
        double amount = extractNumericValue(amountText);
        breakdown.put(label.trim(), amount);
        logger.log("100% Refund addon line added to PRP breakdown map: " + label.trim() + " = " + amountText + " (" + amount + ")");
    }

    private String getFareCardPopupComponentLabel(WebElement component) {
        Locator labelLocator = new Locator("FARE_CARD_POPUP_PRICE_LABEL",
                new By.ByXPath(".//div[contains(@class,'PriceDetail_fare_label')]//span"));
        List<WebElement> labels = driverTools.elements(labelLocator, component).orElse(Collections.emptyList());
        for (WebElement labelElement : labels) {
            String label = driverTools.getText(labelElement);
            if (label != null && !label.isBlank()) {
                return label.trim();
            }
        }
        return "";
    }

    private String getFareCardPopupComponentAmount(WebElement component, String currency) {
        Locator amountSpansLocator = new Locator("FARE_CARD_POPUP_PRICE_AMOUNT_SPANS",
                new By.ByXPath(".//div[contains(@class,'PriceDetail_fare_amount')]//span"));
        List<WebElement> amountSpans = driverTools.elements(amountSpansLocator, component).orElse(Collections.emptyList());
        for (WebElement amountSpan : amountSpans) {
            String amountText = extractPriceTextFromElement(amountSpan);
            if (!amountText.isBlank()) {
                return amountText;
            }
        }

        Locator amountDivLocator = new Locator("FARE_CARD_POPUP_PRICE_AMOUNT_DIV",
                new By.ByXPath(".//div[contains(@class,'PriceDetail_fare_amount')]"));
        List<WebElement> amountDivs = driverTools.elements(amountDivLocator, component).orElse(Collections.emptyList());
        for (WebElement amountDiv : amountDivs) {
            String amountText = extractPriceTextFromElement(amountDiv);
            if (!amountText.isBlank()) {
                return amountText;
            }
        }

        String label = getFareCardPopupComponentLabel(component).toLowerCase();
        if (label.contains("100") && label.contains("refund") && label.contains("addon")) {
            return get100pRefundBreakdownAddonAmountText(currency);
        }
        return "";
    }

    /**
     * Parses the FareCard detail popup Price Detail tab (PriceDetail_fare_component layout).
     */
    public Map<String, Double> getFareCardDetailPopUpPriceBreakdown(String currency) {
        Map<String, Double> breakdown = new LinkedHashMap<>();
        Locator componentsLocator = new Locator("FARE_CARD_POPUP_PRICE_COMPONENTS",
                new By.ByXPath(FARE_CARD_POPUP_PRICE_COMPONENTS_XPATH));
        List<WebElement> components = driverTools.elements(componentsLocator).orElse(Collections.emptyList());
        logger.log("Found " + components.size() + " fare-card popup price components");

        int itemIndex = 1;
        for (WebElement component : components) {
            String label = getFareCardPopupComponentLabel(component);
            String value = getFareCardPopupComponentAmount(component, currency);
            if (label.isBlank() || value.isBlank()) {
                continue;
            }

            String normalizedLabel = label.toLowerCase();
            if (normalizedLabel.contains("cashback")
                    || normalizedLabel.contains("pay now")
                    || normalizedLabel.contains("bayar sekarang")
                    || normalizedLabel.contains("total after cashback")) {
                continue;
            }

            double numericValue = extractNumericValue(value);
            boolean isDiscount = normalizedLabel.contains("discount") || normalizedLabel.contains("diskon")
                    || normalizedLabel.contains("deduction") || normalizedLabel.contains("potongan")
                    || normalizedLabel.contains("gledek") || value.contains("-");
            if (isDiscount && numericValue > 0) {
                numericValue = -numericValue;
            }

            String uniqueKey = label.trim();
            if (breakdown.containsKey(uniqueKey)) {
                uniqueKey = label.trim() + " (" + itemIndex + ")";
                itemIndex++;
            }
            breakdown.put(uniqueKey, numericValue);
            logger.log("Fare-card popup price item: " + uniqueKey + " = " + value + " (" + numericValue + ")");
        }
        ensure100pRefundAddonInBreakdown(breakdown, currency);
        return breakdown;
    }

    private Double find100pRefundInsurancePriceInBreakdown(Map<String, Double> breakdown, String currency) {
        ensure100pRefundAddonInBreakdown(breakdown, currency);
        for (Map.Entry<String, Double> entry : breakdown.entrySet()) {
            String key = entry.getKey().toLowerCase();
            if (key.contains("100") && key.contains("refund")) {
                return entry.getValue();
            }
        }
        String amountText = get100pRefundBreakdownAddonAmountText(currency);
        if (amountText.isBlank()) {
            return null;
        }
        double amount = extractNumericValue(amountText);
        String label = get100pRefundBreakdownAddonLabelText();
        breakdown.put(label.isBlank() ? "100% Refund addon" : label.trim(), amount);
        return amount;
    }

    /**
     * Before selecting fare:
     * 1) verify '+ XXX get full refund' on PRP fare card and store per-person price (abc),
     * 2) open See details -> Price Detail and read 100% Refund addon total (xyz),
     * 3) assert abc x pax from breakdown label equals xyz.
     */
    public Map<String, Double> get100pRefundInsurancePricesFromPrp(int chargeablePax, String currency) {
        Map<String, Double> prices = new LinkedHashMap<>();

        Assertion.assertThat("100% Refund '+ XXX get full refund' wording is visible on PRP fare card",
                is100pRefundFareCardAddonWordingDisplayed(), is(true));
        String fareCardWording = get100pRefundFareCardAddonWordingText();
        Assertion.assertThat("100% Refund fare card addon wording contains full refund copy",
                fareCardWording.toLowerCase(), anyOf(containsString("get full refund"),
                        containsString("per full refund"), containsString("pengembalian dana penuh"),
                        containsString("pengembalian penuh"), containsString("dapat refund penuh")));
        Assertion.assertThat("100% Refund fare card addon wording starts with +", fareCardWording, containsString("+"));

        long abc = get100pRefundFareCardAddonPricePerPerson();
        prices.put("prpFareCardAddonPerPerson", (double) abc);

        clickSeeDetailsOn100pRefundFareCard();
        Assertion.assertThat("100% Refund fare card detail popup is visible",
                isFacilitiesPopupVisible() || isPenaltyInfoVisible(), is(true));

        clickPenaltyInfoTabPriceDetail();
        Assertion.assertThat("100% Refund addon is present in PRP price breakdown",
                is100pRefundAddonPresentInPriceBreakdown(currency), is(true));

        String breakdownLabel = get100pRefundBreakdownAddonLabelText();
        String breakdownAmountText = get100pRefundBreakdownAddonAmountText(currency);
        double xyz = extractNumericValue(breakdownAmountText);
        int addonPax = getPaxCountFromBreakdownLabel(breakdownLabel, chargeablePax);
        double addonPerPerson = xyz / addonPax;

        prices.put("prpBreakdownTotal", xyz);
        prices.put("prpAddonPerPerson", addonPerPerson);
        prices.put("addonPaxCount", (double) addonPax);

        long expectedTotal = abc * addonPax;
        logger.log("100% Refund PRP price check: " + abc + ", pax=" + addonPax
                + ", expectedTotal=" + expectedTotal + ", breakdownTotal=" + Math.round(xyz));
        Assertion.assertThat("PRP fare card per-person addon x pax matches price breakdown total "
                        + abc + " x " + addonPax + " = " + expectedTotal + ", breakdown=" + Math.round(xyz) + ")",
                (int) Math.round(xyz), is((int) expectedTotal));
        Assertion.assertThat("PRP fare card per-person addon matches breakdown per-person addon",
                (int) abc, is((int) Math.round(addonPerPerson)));

        return prices;
    }


    public void enablePrpFlightDetailCapture() {
        driverTools.enableNetworkRequestCapture(PRP_FLIGHT_DETAIL_URL, PRP_FLIGHT_DETAIL_STORAGE_KEY);
        logger.log("Enabled capture for tix-flight-search/v3/prp/flightDetail");
    }

    public String getCapturedPrpFlightDetailBody() {
        for (int i = 0; i < 10; i++) {
            String body = readPrpFlightDetailBody();
            if (!body.isBlank() && body.contains("supplierId")) {
                logger.log("Captured prp/flightDetail body length=" + body.length());
                return body;
            }
            driverTools.dummyWait(1);
        }
        return readPrpFlightDetailBody();
    }

    private String readPrpFlightDetailBody() {
        String body = driverTools.readCapturedNetworkRequestBody(PRP_FLIGHT_DETAIL_STORAGE_KEY);
        if (!body.isBlank()) {
            return body;
        }
        return driverTools.readNetworkRequestBodyFromPerformanceLogs(PRP_FLIGHT_DETAIL_URL);
    }

    public String getEncodedSupplierIdFromPrpUrl() {
        String url = driverTools.getCurrentURL();
        logger.log("PRP url: " + url);
        java.util.regex.Matcher matcher = Pattern.compile("[?&]dSupplierId=([^&]+)").matcher(url);
        if (!matcher.find()) {
            return "";
        }
        return java.net.URLDecoder.decode(matcher.group(1), java.nio.charset.StandardCharsets.UTF_8);
    }

    private String readCapturedPrpFlightDetailBody() {
        Object result = ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "try { return window.__tiketPrpFlightDetailBody"
                        + " || sessionStorage.getItem('__tiketPrpFlightDetail') || ''; }"
                        + " catch (e) { return ''; }");
        return result == null ? "" : result.toString();
    }

}