package com.tiket.page.flight.prp;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.flight.bookingform.FlightBookingFormPage;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tiket.page.flight.bookingform.FlightBookingFormPageAndroid.SELECT_TICKET_OLD;
import static com.tiket.page.flight.bookingform.FlightBookingFormPageAndroid.TRIP_DETAIl;
import static org.hamcrest.core.Is.is;

public class FlightProductReviewPageAndroid <T extends WebDriver> extends FlightProductReviewPage<T> {
    public FlightProductReviewPageAndroid(Session<T> session) {
        super(session);
    }

    public static final Locator SELECT_BTN_100_P_REFUND = new Locator("SELECT_BTN_100_P_REFUND", AppiumBy.xpath("(//*[contains(@resource-id, 'tv_flight_review_bundling_add_ons_name') and contains(@text, '100% Refund')])[1]/following::*[contains(@resource-id, 'tds_button_view') and (contains(@text,'Select') or contains(@text,'Pilih'))][1]"));
    public static final Locator SELECT_BTN_100_P_REFUND_RESCHEDULE = new Locator("SELECT_BTN_100_P_REFUND", AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_review_default_fare_title') and contains(@text,'100%') and contains(@text,'Reschedule')]//following-sibling::*//*[contains(@resource-id,'tds_button_view')]"));
    public static final Locator LABEL_100_P_REFUND = new Locator("LABEL_100_P_REFUND", AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_review_bundling_add_ons_name') and contains(@text, '100% Refund')]"));
    public static final Locator PRICE_PER_PERSON_100P = new Locator("PRICE_PER_PERSON_100P", AppiumBy.xpath("(//*[contains(@resource-id,'rv_content')]/*[2]/*[@class='android.view.ViewGroup']/*[@class='android.view.ViewGroup']//*[contains(@resource-id,'tv_flight_review_default_fare_price_per_pax') and ./preceding-sibling::*[@class='android.widget.TextView']])[1]"));
    public static final Locator TOTAL_PRICE_100P = new Locator("TOTAL_PRICE_100P", AppiumBy.xpath("//*[contains(@resource-id,'rv_content')]/*[2]/*[@class='android.view.ViewGroup']/*[@class='android.view.ViewGroup']/*[@class='android.view.ViewGroup']/*[@class='android.widget.LinearLayout']/*[@class='android.widget.TextView']"));
    public static final Locator TOTAL_PRICE_REGULAR = new Locator("TOTAL_PRICE_REGULAR", AppiumBy.xpath("(//*[@class='android.widget.TextView' and contains(@resource-id,'tv_flight_review_deferred_total_price')])[1]"));
    private static final Locator CORPORATE_DEALS_BENEFIT = new Locator("CORPORATE_DEALS_BENEFIT", AppiumBy.xpath("//*[contains(@resource-id,'tv_benefit_title') and contains(@text,'Corporate Deals')]"));
    public static final Locator SELECT_BTN_REGULAR = new Locator("SELECT_BTN_REGULAR", AppiumBy.xpath("(//*[contains(@resource-id,'btn_flight_review_fare_card_price_section')]//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Select') or contains(@text,'Pilih'))])[1] | (//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Select') or contains(@text,'Pilih'))])[1]"));
    public static final Locator REGULAR_SECTION = new Locator("REGULAR_SECTION", AppiumBy.xpath("//*[@class='android.widget.LinearLayout' and ./*[contains(@resource-id, 'rv_flight_review_fare_card_multifare_2')] and .//*[contains(@resource-id, 'tv_flight_review_fare_card_see_details_cabin_class') and (contains(translate(normalize-space(@text),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), 'economy') or contains(translate(normalize-space(@text),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), 'ekonomi') or contains(translate(normalize-space(@text),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), 'regular fare'))]]"));
    public static final Locator REGULAR_SECTION_2 = new Locator("REGULAR_SECTION_2", AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_review_fare_card_see_details_cabin_class') and (@text='Regular Fare' or @text='Tarif Reguler' or @text='Harga Reguler')]"));
    public static final Locator SELECT_BTN_REGULAR_2 = new Locator("SELECT_BTN_REGULAR_2", AppiumBy.xpath("(//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Select') or contains(@text,'Pilih'))])[last()]"));
    private static final Locator FARE_ERROR = new Locator("FARE_ERROR", AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_review_error_bundling_fare_add_ons_description')]"));
    private static final Locator LOAD_ERROR_RETRY = new Locator("LOAD_ERROR_RETRY", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (@text='Reload' or @text='Muat ulang' or @text='Coba lagi' or @text='Try again')] | //android.widget.Button[@text='Reload' or @text='Muat ulang' or @text='Coba lagi' or @text='Try again'] | //*[@clickable='true' and (@text='Reload' or @text='Muat ulang' or @text='Coba lagi' or @text='Try again')]"));
    private static final Locator NO_UPGRADE_SECTION = new Locator("NO_UPGRADE_SECTION", AppiumBy.xpath("//*[contains(@resource-id, 'btn_flight_review_no_upgrade_select')]"));
    private static final Locator NO_UPGRADE_SELECT_BTN = new Locator("NO_UPGRADE_SELECT_BTN", AppiumBy.xpath("(//*[contains(@resource-id, 'btn_flight_review_no_upgrade_select')]//*[contains(@resource-id, 'tds_button_view')])[1]"));
    private static final Locator NO_UPGRADE_SELECT_BTN_2 = new Locator("NO_UPGRADE_SELECT_BTN_2", AppiumBy.xpath("(//*[contains(@resource-id, 'btn_flight_review_no_upgrade_select')]//*[contains(@resource-id, 'tds_button_view')])[last()]"));
    /** Last fare Select CTA; avoids matching the first //*[@text='Select'] on screen (wrong leg for RT). */
    public static final Locator SELECT_BTN = new Locator("SELECT_BTN", AppiumBy.xpath("(//*[contains(@resource-id, 'tds_button_view') and (contains(@text,'Select') or contains(@text,'Pilih'))])[1]"));
    public static final Locator SELECT_TICKET = new Locator("SELECT_TICKET", AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_body') and (@text='Select your ticket fare' or @text='Pilih tarif tiket Anda' or @text='Pilih tarif tiketmu')]"));
    public static final Locator MULTI_FARE = new Locator("MULTI_FARE", AppiumBy.xpath("//*[contains(@resource-id, 'tds_stepper_text')]"));

    public static final Locator AIRLINE_LOGO = new Locator("AIRLINE_LOGO",AppiumBy.id("iv_airline_detail_logo"));
    public static final Locator ORIGIN = new Locator("ORIGIN",AppiumBy.id("tv_origin"));
    public static final Locator DESTINATION = new Locator("DESTINATION",AppiumBy.id("tv_destination"));
    /** Legacy entry opened price breakdown value; new PRP uses fare-card "See details". */
    public static final Locator PRICE_DETAIL = new Locator("PRICE_DETAIL",AppiumBy.xpath("(//*[contains(@resource-id,'tv_flight_review_fare_card_see_details') and (contains(@text,'See details') or contains(@text,'Lihat detail') or contains(@text,'Lihat Detail'))])[1] | (//*[contains(@resource-id,'tv_flight_review_price_breakdown_value')])[1]"));
    public static final Locator PRICE_BREAKDOWN_TAB = new Locator("PRICE_BREAKDOWN_TAB",AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tab_layout') and (contains(@text,'Price Breakdown') or contains(@text,'Detail Harga') or contains(@text,'Price Detail') )]"));
    public static final Locator CLOSE_PRICE_DETAIL = new Locator("CLOSE_PRICE_DETAIL",AppiumBy.xpath("//*[contains(@resource-id,'iv_flight_review_fare_detail_close') or contains(@resource-id,'iv_flight_refund_100_close')]"));
    public static final Locator PRICE_LOCATOR = new Locator("PRICE_LOCATOR",AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_review_price_breakdown_value') or contains(@resource-id,'tv_flight_price_breakdown_detail_price')]"));
    public static final Locator PRICE_LOCATOR_TOTAL_TEXT = new Locator("PRICE_LOCATOR_TOTAL_TEXT",AppiumBy.xpath("//*[(contains(@resource-id,'tv_flight_review_price_breakdown_item_label') or contains(@resource-id,'tv_flight_price_breakdown_detail_name') or contains(@resource-id,'tv_flight_total')) and (contains(@text,'Total') or contains(@text,'Pay Now') or contains(@text,'Bayar'))]"));
    private static final Locator PAY_NOW_VALUE = new Locator("PAY_NOW_VALUE", AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_review_price_breakdown_item_label') and (contains(@text,'Pay Now') or contains(@text,'Bayar Sekarang'))]/following-sibling::*[contains(@resource-id,'tv_flight_review_price_breakdown_value')][1]"));
    private static final Locator TOTAL_AFTER_CASHBACK_VALUE = new Locator("TOTAL_AFTER_CASHBACK_VALUE", AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_review_price_breakdown_item_label') and (contains(@text,'Total after cashback') or contains(@text,'Total After Cashback') or contains(@text,'Total setelah cashback'))]/following-sibling::*[contains(@resource-id,'tv_flight_review_price_breakdown_value')][1]"));
    private static final Locator FOOTER_TOTAL_PRICE = new Locator("FOOTER_TOTAL_PRICE", AppiumBy.id("tv_flight_price_total"));
    public static final Locator NOT_MEET_TRAVEL_POLICY_POPUP = new Locator("NOT_MEET_TRAVEL_POLICY_POPUP",AppiumBy.xpath("//*[contains(@resource-id,'tds_txt_header_info_view') and (contains(@text,'This order doesn’t meet the travel policy') or contains(@text,'doesn’t meet') or contains(@text,'Pesanan ini tidak sesuai dengan kebijakan perjalanan perusahaan'))]"));
    public static final Locator NOT_MEET_TRAVEL_POLICY_POPUP_NO_CANCEL = new Locator("NOT_MEET_TRAVEL_POLICY_POPUP_NO_CANCEL",AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'No, cancel') or contains(@text,'Tidak, batalkan'))]"));
    public static final Locator NOT_MEET_TRAVEL_POLICY_POPUP_YES_CONTINUE = new Locator("NOT_MEET_TRAVEL_POLICY_POPUP_YES_CONTINUE",AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Yes, continue') or contains(@text,'Ya, lanjutkan'))]"));
    public static final Locator PROMO_APPLIED_TEXT = new Locator("PROMO_APPLIED_TEXT", AppiumBy.xpath("//*[contains(@text,'Promo terbaik terpasang. Pilihan bagus!') or contains(@text,'Best promo applied. Good choice!')]"));
    public static final Locator PROMO_APPLIED_LABEL_REGULAR = new Locator("PROMO_APPLIED_LABEL", AppiumBy.xpath("//*[contains(@resource-id, 'card_flight_review_fare')][.//*[contains(@resource-id, 'tv_flight_review_fare_title') and not(contains(@text,'100'))]]//*[@text='BLUINFINITY']"));
    public static final Locator PROMO_APPLIED_LABEL_100P_REFUND = new Locator("PROMO_APPLIED_LABEL_100P_REFUND", AppiumBy.xpath("//*[contains(@resource-id, 'card_flight_review_fare')][.//*[contains(@resource-id, 'tv_flight_review_fare_title') and contains(@text, '100') and not(contains(@text,'Reschedule'))]]//*[@text='BLUINFINITY']"));
    public static final Locator PROMO_APPLIED_LABEL_100P_REFUND_RESCHEDULE = new Locator("PROMO_APPLIED_LABEL_100P_REFUND_RESCHEDULE", AppiumBy.xpath("//*[contains(@resource-id, 'card_flight_review_fare')][.//*[contains(@resource-id, 'tv_flight_review_fare_title') and contains(@text, '100') and(contains(@text,'Reschedule'))]]//*[@text='BLUINFINITY']"));
    // PRP transit indicator changed: the page now shows the new self-transfer banner
    // (tv_transit_title = "Self transfer 1h 50m in Surabaya" in EN,
    // "Self transfer 1j 50m di Surabaya" in ID — the "Self transfer" prefix is not
    // localized, only the duration unit ('h'/'j') and "in"/"di" change) instead of the
    // old tv_benefit_title with "Check-in again"/"Check-in ulang".
    public static final Locator CHECK_IN_AGAIN_TEXT_PRP = new Locator("CHECK_IN_AGAIN_TEXT_PRP",
            AppiumBy.xpath("//*[contains(@resource-id, 'tv_transit_title') and contains(@text, 'Self transfer')]"));
    // "Self Transfer Protection included" (EN) / "Termasuk Proteksi Transit Mandiri" (ID)
    // is rendered inside the bottom sheet that opens when the self-transfer banner is tapped.
    public static final Locator SELF_TRANSFER_PROTECTION_INCLUDED_BS = new Locator("SELF_TRANSFER_PROTECTION_INCLUDED_BS",
            AppiumBy.xpath("//*[contains(@text, 'Self Transfer Protection') or contains(@text, 'Proteksi Transit Mandiri')]"));
    public static final Locator SELF_TRANSFER_BOTTOM_SHEET_CLOSE = new Locator("SELF_TRANSFER_BOTTOM_SHEET_CLOSE",
            AppiumBy.id("iv_close"));
    // Chevron that opens the self-transfer / protection detail bottom sheet on the PRP card.
    //
    // The PRP renders three different elements with @resource-id='iv_chevron' on a single
    // multi-leg flight (one per airline leg + one for the transit section). We MUST target
    // the one that's a sibling of `tv_transit_title`. Anchoring on the "Self Transfer
    // Protection included" text and using XPath `following::*[iv_chevron]` is incorrect
    // because the protection text is rendered AFTER the transit chevron in document order,
    // so the next chevron found is actually the following leg's airline chevron (e.g. Lion
    // Air), which opens an unrelated flight-detail sheet without any protection copy
    // (see Jenkins 2026-05-26 run, where the test clicked Lion Air's chevron and timed out
    // looking for the protection text inside its sheet).
    //
    // Anchor on `tv_transit_title` instead, then take its sibling chevron — that's the
    // chevron immediately to the right of the "Self transfer 3h Xm in <city>" banner and
    // it opens the transit / protection bottom sheet. Falls back to `v_bg_transit`
    // (the clickable background covering the same banner area) when the chevron resource id
    // is missing on alternative builds.
    public static final Locator SELF_TRANSFER_PROTECTION_CHEVRON = new Locator("SELF_TRANSFER_PROTECTION_CHEVRON",
            AppiumBy.xpath("(//*[contains(@resource-id,'tv_transit_title') "
                    + "and (contains(@text,'Self transfer') or contains(@text,'Self Transfer'))]"
                    + "/following-sibling::*[contains(@resource-id,'iv_chevron') "
                    + "or contains(@resource-id,'v_bg_transit')])[1]"));
    private static final Locator REFUND_100P_PRP = new Locator("REFUND_100P_PRP", AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_review_fare_title') and contains(@text, 'Economy + tiket 100% Refund') or contains(@text, 'Ekonomi + tiket 100% Refund')]"));
    private static final Locator REFUND_100P_SEE_DETAILS = new Locator("REFUND_100P_SEE_DETAILS", AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_review_fare_card_detail') and ./preceding-sibling::*[(contains(@text, 'Economy + tiket 100% Refund') or contains(@text, 'Ekonomi + tiket 100% Refund'))]]"));
    private static final Locator REFUND_RESCHEDULE_TAB = new Locator("REFUND_RESCHEDULE_TAB", AppiumBy.xpath("//*[contains(@resource-id, 'tds_title_tab_layout') and contains(@text, 'Refund & Reschedule')]"));
    private static final Locator PRICE_DETAIL_TAB = new Locator("PRICE_DETAIL_TAB", AppiumBy.xpath("//*[contains(@resource-id, 'tds_title_tab_layout') and (contains(@text, 'Price Detail') or contains(@text, 'Detail Harga'))]"));
    public static final Locator GREEN_BOX = new Locator("GREEN_BOX", AppiumBy.xpath("//*[contains(@resource-id, 'cv_flight_review_refund_100_percent_card_background')]"));
    public static final Locator PER_PERSON_PRICE_PRP = new Locator("PER_PERSON_PRICE_PRP", AppiumBy.xpath("(//*[contains(@resource-id, 'tv_flight_penalty_info_title') and contains(@text, '100% refund') or contains(@text, 'Refund 100%')])[2]"));
    private static final Locator WHOLE_PRICE = new Locator("WHOLE_PRICE", AppiumBy.xpath("(//*[contains(@resource-id, 'tv_flight_price_breakdown_detail_price')])[1]"));
    private static final Locator CLOSE_TABS = new Locator("CLOSE_TABS", AppiumBy.xpath("//*[contains(@resource-id, 'iv_flight_review_fare_detail_close')]"));
    private static final Locator DISCOUNT_SECTION = new Locator("DISCOUNT_SECTION", AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_body') and @text='Price Deduction' or @text='Potongan Harga']"));
    private static final Locator DISCOUNT_PRICE = new Locator("DISCOUNT_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_price_breakdown_detail_price') and contains(@text, '-')]"));
    public static final Locator CLICK_DETAILS = new Locator("CLICK_DETAILS", AppiumBy.xpath("(//*[contains(@resource-id,'tv_flight_review_fare_card_see_details') and (contains(@text,'See details') or contains(@text,'Lihat detail') or contains(@text,'Lihat Detail'))])[1]"));
    public static final Locator DEPARTURE_FLIGHT = new Locator("DEPARTURE_FLIGHT", AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_body') and contains(@text, 'Departure Flight') or contains(@text, 'Penerbangan Pergi')]"));
    public static final Locator REFUND_PERCENTAGE = new Locator("REFUND_PERCENTAGE", AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_penalty_info_title') and contains(@text, 'refund')]"));
    public static final Locator RETURN_FLIGHT = new Locator("RETURN_FLIGHT", AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_body') and contains(@text, 'Return Flight') or contains(@text, 'Penerbangan Pulang')]"));
    public static final Locator RESCHEDULE_CHIP = new Locator("RESCHEDULE_CHIP", AppiumBy.xpath("//*[contains(@resource-id, 'tds_chips_text') and contains(@text,'Reschedule')]"));
    public static final Locator RESCHEDULE_PERCENTAGE = new Locator("RESCHEDULE_PERCENTAGE", AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_penalty_info_title') and contains(@text, 'reschedule')]"));
    public static final Locator REFUND = new Locator("REFUND", AppiumBy.xpath("(//*[contains(@resource-id,'tv_flight_penalty_info_not_available_title') and (contains(normalize-space(@text),'Refund') or contains(normalize-space(@text),'refund'))])[1]"));
    public static final Locator RESCHEDULE = new Locator("RESCHEDULE", AppiumBy.xpath("//*[(contains(@resource-id,'tv_flight_body') or contains(@resource-id,'tv_flight_penalty_info_not_available_title')) and (contains(normalize-space(@text),'Reschedule') or contains(normalize-space(@text),'reschedule'))]"));
    /** Refund sub-tab inside the Refund &amp; Reschedule popup. Use {@code contains} on the resource-id so it works across build flavors (preprod/prod/staging). */
    private static final Locator REFUND_TAB = new Locator("REFUND_TAB", AppiumBy.xpath(
            "//*[contains(@resource-id,'tds_title_tab_layout') and (@text='Refund' or @text='Pengembalian Dana')]"));
    /** Reschedule sub-tab inside the Refund &amp; Reschedule popup. */
    private static final Locator RESCHEDULE_TAB = new Locator("RESCHEDULE_TAB", AppiumBy.xpath(
            "//*[contains(@resource-id,'tds_title_tab_layout') and (@text='Reschedule' or @text='Penjadwalan Ulang')]"));
    /** Depart chip switching the visible flight leg under either Refund or Reschedule tab. */
    private static final Locator DEPART_CHIP = new Locator("DEPART_CHIP", AppiumBy.xpath(
            "//*[contains(@resource-id,'tds_chips_text') and (@text='Depart' or @text='Pergi' or @text='Berangkat')]"));
    /** Return chip switching the visible flight leg under either Refund or Reschedule tab. */
    private static final Locator RETURN_CHIP = new Locator("RETURN_CHIP", AppiumBy.xpath(
            "//*[contains(@resource-id,'tds_chips_text') and (@text='Return' or @text='Pulang' or @text='Kembali')]"));
    /** Any rendered policy row inside the active tab/chip - present when policy info IS available. */
    private static final Locator INFO_ROW_CONTAINER = new Locator("INFO_ROW_CONTAINER", AppiumBy.xpath(
            "//*[contains(@resource-id,'cl_row_container')]"));
    /**
     * "&lt;Refund|Reschedule|Return&gt; information is not available" copy in English and Bahasa.
     * The Bahasa UI uses the loanwords "refund"/"reschedule" rather than fully-translated terms,
     * with inconsistent casing of the second word across builds (lowercase and Title Case both seen).
     * Includes fully-translated Bahasa strings as defensive fallbacks.
     */
    private static final Locator INFO_NOT_AVAILABLE = new Locator("INFO_NOT_AVAILABLE", AppiumBy.xpath(
            "//*[contains(@resource-id,'tv_title') and ("
                    + "contains(@text,'Refund information is not available') or "
                    + "contains(@text,'Reschedule information is not available') or "
                    + "contains(@text,'Return information is not available') or "
                    + "contains(@text,'Informasi refund tidak tersedia') or "
                    + "contains(@text,'Informasi reschedule tidak tersedia') or "
                    + "contains(@text,'Informasi Refund tidak tersedia') or "
                    + "contains(@text,'Informasi Reschedule tidak tersedia') or "
                    + "contains(@text,'Informasi pengembalian dana tidak tersedia') or "
                    + "contains(@text,'Informasi penjadwalan ulang tidak tersedia')"
                    + ")]"));
    @Override
    public void swipeTo100pSection(){
        driverTools.swipeToElement(SELECT_BTN_100_P_REFUND, Swipe.UP,8);
    }

    @Override
    public boolean check100pRefundAttribute(){
        boolean is100pRefundAttributeDisplayed = driverTools.isElementDisplayed(LABEL_100_P_REFUND);
        logger.log("is 100p refund displayed on prp : " + is100pRefundAttributeDisplayed);
        return is100pRefundAttributeDisplayed;
    }

    @Override
    public boolean checkPrice(){
        WebElement regularPriceElement = driverTools.swipeToElement(TOTAL_PRICE_REGULAR,Swipe.UP,2).orElseThrow();
        double regularPrice = CoreFunctions.convertPriceSafely(driverTools.getText(regularPriceElement));
        logger.log("regular total price " + regularPrice);

        driverTools.swipeToElement(PRICE_PER_PERSON_100P, Swipe.UP, 5);
        boolean isPerPersonPriceDisplayed = driverTools.isElementDisplayed(PRICE_PER_PERSON_100P);
        logger.log("is price per person displayed : " + isPerPersonPriceDisplayed);

        WebElement refund100PElement = driverTools.swipeToElement(TOTAL_PRICE_100P, Swipe.UP, 5).orElseThrow();
        boolean isTotalPriceDisplayed = driverTools.isElementDisplayed(refund100PElement);
        logger.log("is total price 100p refund displayed : " + isTotalPriceDisplayed);
        double refund100Price = CoreFunctions.convertPriceSafely(driverTools.getText(refund100PElement));
        logger.log("100 p total price " + refund100Price);

        boolean isRefund100pMoreThenRegular = (refund100Price - regularPrice) > 0;
        return isPerPersonPriceDisplayed && isTotalPriceDisplayed && isRefund100pMoreThenRegular;
    }

    @Override
    public void continueToBookingPageUsing100pSelectBtn(){
        driverTools.swipeToElement(SELECT_BTN_100_P_REFUND, Swipe.UP, 5);
        driverTools.click(SELECT_BTN_100_P_REFUND);
        FlightBookingFormPage<T> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        flightBookingFormPage.skipFullProtectionPrp();
        flightBookingFormPage.isOnBookingPage();
    }

    @Override
    public void swipeToRegularSection(){
        if (driverTools.isElementDisplayed(MULTI_FARE)){
            driverTools.swipeToElement(SELECT_BTN_REGULAR_2, Swipe.UP,10);
        }
        else
            driverTools.swipeToElement(SELECT_BTN_REGULAR, Swipe.UP,10);
    }

    @Override
    public boolean isCorporateDealsPresent(){
        return driverTools.swipeToElement(CORPORATE_DEALS_BENEFIT, Swipe.UP, 5).isPresent();
    }

    @Override
    public void continueToBookingForm(String tripType){
        logger.log("[continueToBookingForm] start - tripType=" + tripType);
        dismissPrpLoadErrorIfShown();
        driverTools.swipeToElement(SELECT_TICKET, Swipe.UP, 10).orElseThrow();
        driverTools.swipeElementToMidScreen(SELECT_TICKET);
        logger.log("[continueToBookingForm] SELECT_TICKET swiped into view");

        boolean multiFareDisplayed = driverTools.isElementDisplayed(MULTI_FARE);
        boolean regularSectionDisplayed = driverTools.isElementDisplayed(REGULAR_SECTION);
        logger.log("[continueToBookingForm] MULTI_FARE displayed? " + multiFareDisplayed
                + " | REGULAR_SECTION displayed? " + regularSectionDisplayed);

        //airlines with insurance
        if (multiFareDisplayed){
            logger.log("[continueToBookingForm] branch=MULTI_FARE (with insurance)");
            // SELECT_TICKET is the heading label, not a button - swipe to it, then click the actual button (SELECT_BTN)
            boolean selectBtnDisplayed = driverTools.isElementDisplayed(SELECT_BTN);
            logger.log("[continueToBookingForm] SELECT_BTN displayed before click? " + selectBtnDisplayed);
            if(!selectBtnDisplayed){
                logger.log("[continueToBookingForm] SELECT_BTN not visible, swiping up");
                driverTools.swipeUp();
            }
            WebElement selectBtn1 = driverTools.waitForElementToPresence(SELECT_BTN).orElseThrow();
            logger.log("[continueToBookingForm] clicking first SELECT_BTN by coordinate");
            driverTools.clickElementByCoordinate(selectBtn1);
            // roundtrip: click second segment select button (only when MULTI_FARE is present)
            if (tripType.equalsIgnoreCase("roundtrip")){
                logger.log("[continueToBookingForm] roundtrip - waiting for SELECT_BTN_REGULAR_2 (second Select button)");
                WebElement selectBtn2 = driverTools.waitForElementToPresence(SELECT_BTN_REGULAR_2, 10).orElseThrow();
                logger.log("[continueToBookingForm] clicking SELECT_BTN_REGULAR_2 by coordinate");
                driverTools.clickElementByCoordinate(selectBtn2);
            }

        }
        //airlines without insurance
        else if (regularSectionDisplayed){
            logger.log("[continueToBookingForm] branch=REGULAR_SECTION (without insurance)");
            boolean selectBtnRegularDisplayed = driverTools.isElementDisplayed(SELECT_BTN_REGULAR);
            logger.log("[continueToBookingForm] SELECT_BTN_REGULAR displayed before click? " + selectBtnRegularDisplayed);
            if(!selectBtnRegularDisplayed){
                logger.log("[continueToBookingForm] SELECT_BTN_REGULAR not visible, swiping up");
                driverTools.swipeUp();
            }
            driverTools.waitForElementToPresence(SELECT_BTN_REGULAR);
            logger.log("[continueToBookingForm] clicking SELECT_BTN_REGULAR by coordinate");
            driverTools.clickElementByCoordinate(SELECT_BTN_REGULAR);
        } else {
            logger.softWarning("[continueToBookingForm] neither MULTI_FARE nor REGULAR_SECTION was displayed");
            boolean fareErrorDisplayed = driverTools.isElementDisplayed(FARE_ERROR);
            boolean noUpgradeSectionDisplayed = driverTools.isElementDisplayed(NO_UPGRADE_SECTION);
            logger.log("[continueToBookingForm] FARE_ERROR displayed? " + fareErrorDisplayed
                    + " | NO_UPGRADE_SECTION displayed? " + noUpgradeSectionDisplayed);
            if (noUpgradeSectionDisplayed) {
                logger.log("[continueToBookingForm] falling back to 'No upgrade' Select button");
                boolean noUpgradeBtnVisible = driverTools.isElementDisplayed(NO_UPGRADE_SELECT_BTN);
                logger.log("[continueToBookingForm] NO_UPGRADE_SELECT_BTN visible? " + noUpgradeBtnVisible);
                if (!noUpgradeBtnVisible) {
                    logger.log("[continueToBookingForm] NO_UPGRADE_SELECT_BTN not visible, swiping up");
                    driverTools.swipeUp();
                }
                WebElement noUpgradeBtn = driverTools.waitForElementToPresence(NO_UPGRADE_SELECT_BTN).orElseThrow();
                driverTools.clickElementByCoordinate(noUpgradeBtn);
                if (tripType.equalsIgnoreCase("roundtrip")) {
                    logger.log("[continueToBookingForm] roundtrip - waiting for second 'No upgrade' Select button");
                    driverTools.waitForElementToPresence(NO_UPGRADE_SELECT_BTN_2, 10)
                            .ifPresent(btn -> {
                                logger.log("[continueToBookingForm] clicking second NO_UPGRADE_SELECT_BTN_2");
                                driverTools.clickElementByCoordinate(btn);
                            });
                }
            } else {
                logger.softWarning("[continueToBookingForm] no fallback available - no select action performed yet");
                driverTools.waitForElementToPresence(SELECT_TICKET_OLD).ifPresent(driverTools::click);
            }
        }

        FlightBookingFormPage<T> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        boolean tripDetailPresent = driverTools.waitForElementToAppear(TRIP_DETAIl,5).isPresent();
        logger.log("[continueToBookingForm] TRIP_DETAIl present after select? " + tripDetailPresent);
        if(tripDetailPresent) {
            logger.log("[continueToBookingForm] TRIP_DETAIl found - returning early (booking form already reached)");
            return;
        }
        logger.log("[continueToBookingForm] skipping full protection popup and verifying booking page");
        flightBookingFormPage.skipFullProtectionPrp();
        dismissPrpLoadErrorIfShown();
        flightBookingFormPage.isOnBookingPage();
        logger.log("[continueToBookingForm] done");
    }

    private void dismissPrpLoadErrorIfShown() {
        if (!driverTools.isElementDisplayed(LOAD_ERROR_RETRY)) {
            return;
        }
        for (int i = 0; i < 3 && driverTools.waitForElementToAppear(LOAD_ERROR_RETRY, 2).isPresent(); i++) {
            logger.log("[continueToBookingForm] load error overlay — tapping Reload / Coba lagi");
            driverTools.click(LOAD_ERROR_RETRY);
        }
    }

    @Override
    public boolean isProductReviewPageDisplayed() {
        return driverTools.waitForElementToPresence(SELECT_TICKET, 15).isPresent();
    }

    @Override
    public void checkRefundAsAddOnPRP(int pax, String currency) {
        logger.log("Checking 100p refund card is there on the PRP page");
        Assertion.assertThat("100p refund card is there on the PRP page", driverTools.waitForElementToPresence(REFUND_100P_PRP).isPresent(), is(true));

        logger.log("Clicking see detail link on the 100p refund card");
        driverTools.click(REFUND_100P_SEE_DETAILS);

        logger.log("Checking refund & reschedule tab is there on the price detail popup");
        Assertion.assertThat("Refund & reschedule tab is there on the See detail popup", driverTools.waitForElementToPresence(REFUND_RESCHEDULE_TAB).isPresent(), is(true));
        driverTools.click(REFUND_RESCHEDULE_TAB);

        logger.log("Verifying details on the refund & reschedule tab");
        Assertion.assertThat("Green box information is there on the refund & reschedule tab", driverTools.waitForElementToPresence(GREEN_BOX).isPresent(), is(true));

        String perPersonPrice = driverTools.getText(driverTools.waitForElementToPresence(PER_PERSON_PRICE_PRP).orElseThrow());
        logger.log("Per person price on the refund & reschedule tab: " + perPersonPrice);
        String[] parts = perPersonPrice.split("\\s+");
        String priceOnly = parts[parts.length - 1];
        logger.log("Extracted price: " + priceOnly);
        int numericPrice = Integer.parseInt(priceOnly.replace(",", ""));

        logger.log("Checking Price details tab is there on the price detail popup");
        Assertion.assertThat("Price details tab is there on the See detail popup", driverTools.waitForElementToPresence(PRICE_DETAIL_TAB).isPresent(), is(true));
        driverTools.click(PRICE_DETAIL_TAB);

        logger.log("Verifying details on the Price detail tab");
        Locator refundprice = getRefundPriceLocator(pax);
        Assertion.assertThat("Refund price is there on the Price detail tab as an AddOn for " + pax + " passengers ", driverTools.isElementDisplayed(refundprice), is(true));

        logger.log("Comparing prices on the refund tab and Price detail tab");
        String refundPrice = driverTools.getText(driverTools.waitForElementToPresence(WHOLE_PRICE).orElseThrow());
        refundPrice = refundPrice.replaceAll("[^\\d,]", "");
        int priceOnlyInt = Integer.parseInt(refundPrice.replace(",", ""));

        logger.log("Checking if price deduction section is present on the Price detail tab");
        if(driverTools.isElementDisplayed(DISCOUNT_SECTION)){
            String discountPrice = driverTools.getText(driverTools.waitForElementToPresence(getDiscountPrice(currency)).orElseThrow());
            discountPrice = discountPrice.replaceAll("[^\\d,]", "");
            int discountPriceInt = Integer.parseInt(discountPrice.replace(",", ""));
            logger.log("Discount price on the Price detail tab: " + discountPriceInt);
            priceOnlyInt -= Math.abs(discountPriceInt);
            logger.log("Adjusted price on the Price detail tab after adding discount: " + priceOnlyInt);
        }

        logger.log("Price on the Price detail tab: " + priceOnlyInt);
        int total = numericPrice * pax;
        logger.log("Total price for " + pax + " pax as per refund tab: " + total);
        Assertion.assertThat("Price on the refund & reschedule tab is the same as on the Price detail tab", priceOnlyInt == total, is(true));

        logger.log("Closing the See detail popup");
        driverTools.click(CLOSE_TABS);
    }

    private Locator getRefundPriceLocator(int index) {
        return new Locator("Refund price Locator", AppiumBy.xpath("//*[@class='android.view.ViewGroup' and ./*[@text='tiket 100% Refund (x" + index + ")']]"));
    }

    private Locator getDiscountPrice(String currency) {
        return new Locator("Discount price Locator", AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_price_breakdown_detail_price') and contains(@text, '-" + currency + "')]"));
    }

    @Override
    public void clickSelectRegularBySwipingDown(){
        driverTools.swipeToElement(REGULAR_SECTION,Swipe.DOWN,10).orElseThrow();
        if(!driverTools.isElementDisplayed(SELECT_BTN_REGULAR)){
            driverTools.swipeDown();
        }
        driverTools.waitForElementToPresence(SELECT_BTN_REGULAR);
        driverTools.click(SELECT_BTN_REGULAR);

        FlightBookingFormPage<T> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        flightBookingFormPage.isOnBookingPage();
    }

    @Override
    public void clickSelectBtnRegular(){
        driverTools.swipeToElement(REGULAR_SECTION,Swipe.UP,10).orElseThrow();
        driverTools.swipeUp();
        driverTools.waitForElementToPresence(SELECT_BTN_REGULAR);
        driverTools.click(SELECT_BTN_REGULAR);
    }

    @Override
    public boolean isAirlineLogoDisplayedPRP(){
        WebElement airlineLogo = driverTools.waitForElementToPresence(AIRLINE_LOGO).orElseThrow();
        return driverTools.isElementDisplayed(airlineLogo);
    }

    @Override
    public boolean isAirlineRouteCorrectPRP(String route){
        String[] routeArr = route.contains(" ke ") ? route.split(" ke ") : route.split(" to ");
        String origin = routeArr[0];
        WebElement originPRPElement = driverTools.waitForElementToAppear(ORIGIN).orElseThrow();
        String originPRP = driverTools.getText(originPRPElement);
        logger.log("origin : " + origin);
        logger.log("originPRP : " + originPRP);
        String destination = routeArr[1];
        WebElement destinationPRPElement = driverTools.waitForElementToAppear(DESTINATION).orElseThrow();
        String destinationPRP = driverTools.getText(destinationPRPElement);
        logger.log("destination : " + destination);
        logger.log("destinationPRP : " + destinationPRP);

        return origin.equals(originPRP) && destination.equals(destinationPRP);
    }

    private void openPriceDetail(){
        swipeToRegularSection();
        driverTools.swipeUp();
        if (driverTools.waitForElementToAppear(CLICK_DETAILS, 8).isPresent()) {
            driverTools.click(CLICK_DETAILS);
        } else {
            WebElement priceDetail = driverTools.waitForElementToAppear(PRICE_DETAIL).orElseThrow();
            driverTools.click(priceDetail);
        }
        if (driverTools.waitForElementToAppear(PRICE_BREAKDOWN_TAB, 8).isPresent()) {
            driverTools.click(PRICE_BREAKDOWN_TAB);
        } else if (driverTools.waitForElementToAppear(PRICE_DETAIL_TAB, 5).isPresent()) {
            driverTools.click(PRICE_DETAIL_TAB);
        }
    }
    private void closePriceDetail(){
        if (driverTools.isElementDisplayed(CLOSE_TABS)) {
            driverTools.click(CLOSE_TABS);
        } else if (driverTools.isElementDisplayed(CLOSE_PRICE_DETAIL)) {
            driverTools.click(CLOSE_PRICE_DETAIL);
        } else {
            driverTools.back();
        }
    }

    private boolean priceBreakdownCorrect(){
        driverTools.swipeToElement(PRICE_LOCATOR_TOTAL_TEXT, Swipe.UP, 3);

        // New fare-detail Price Detail tab: Pay Now + Total after cashback rows.
        if (driverTools.waitForElementToAppear(PAY_NOW_VALUE, 5).isPresent()) {
            double payNow = CoreFunctions.convertPrice(driverTools.getText(PAY_NOW_VALUE).orElseThrow());
            double totalAfterCashback = CoreFunctions.convertPrice(
                    driverTools.getText(TOTAL_AFTER_CASHBACK_VALUE).orElseThrow());
            double footerTotal = driverTools.waitForElementToAppear(FOOTER_TOTAL_PRICE, 5)
                    .map(el -> CoreFunctions.convertPrice(driverTools.getText(el)))
                    .orElse(0d);
            logger.log("Pay Now: " + payNow);
            logger.log("Total after cashback: " + totalAfterCashback);
            logger.log("Footer total price: " + footerTotal);
            return payNow > 0 && totalAfterCashback > 0 && footerTotal > 0;
        }

        // Legacy breakdown sheet.
        List<WebElement> prices = driverTools.elements(PRICE_LOCATOR).orElseThrow();
        ArrayList<Double> arrayPrice = new ArrayList<>();

        for (WebElement element : prices) {
            String text = driverTools.getText(element);
            try {
                arrayPrice.add(CoreFunctions.convertPrice(text));
            } catch (NumberFormatException ignored) {
            }
        }

        double sum = 0;
        for (double i : arrayPrice) {
            sum += i;
        }

        double totalPrice = arrayPrice.get(arrayPrice.size() - 1);
        logger.log("Total price text:  " + totalPrice);
        logger.log("Total price got:  " + sum / 2);
        return totalPrice == sum / 2;
    }

    @Override
    public boolean verifyPriceBreakdownPRP(){
        openPriceDetail();
        boolean isBreakdownCorrect = priceBreakdownCorrect();
        closePriceDetail();
        return isBreakdownCorrect;
    }

    @Override
    public boolean isNotMeetTravelPolicyPopupDisplayed(){
        boolean isNotMeetTravelPolicyPopupDisplayed = driverTools.waitForElementToPresence(NOT_MEET_TRAVEL_POLICY_POPUP).isPresent();
        logger.log("is not meet travel policy popup displayed : " + isNotMeetTravelPolicyPopupDisplayed);

        boolean isNoCancelBtnDisplayed = driverTools.isElementDisplayed(NOT_MEET_TRAVEL_POLICY_POPUP_NO_CANCEL);
        logger.log("is no cancel button displayed : " + isNoCancelBtnDisplayed);

        boolean isYesContinueBtnDisplayed = driverTools.isElementDisplayed(NOT_MEET_TRAVEL_POLICY_POPUP_YES_CONTINUE);
        logger.log("is yes continue button displayed : " + isYesContinueBtnDisplayed);
        return isNotMeetTravelPolicyPopupDisplayed && isNoCancelBtnDisplayed && isYesContinueBtnDisplayed;
    }

    private void continueIfTravelPolicyPopupDisplayed() {
        if (driverTools.isElementDisplayed(NOT_MEET_TRAVEL_POLICY_POPUP_YES_CONTINUE)) {
            logger.log("Travel policy popup displayed, clicking Yes, continue");
            driverTools.click(NOT_MEET_TRAVEL_POLICY_POPUP_YES_CONTINUE);
        }
    }

    @Override
    public boolean verifyPromoInfinitySection(){
        if (driverTools.isElementDisplayed(PROMO_APPLIED_TEXT))
            return driverTools.isElementDisplayed(PROMO_APPLIED_TEXT);
        else driverTools.swipeToElement(PROMO_APPLIED_TEXT,Swipe.UP,6);
        return driverTools.isElementDisplayed(PROMO_APPLIED_TEXT);
    }

    @Override
    public boolean isLabelPromoInfinityDisplayed(){
        boolean isRegularPromoLabelDisplayed = driverTools.waitForElementToPresence(PROMO_APPLIED_LABEL_REGULAR, 20).isPresent();
        logger.log("isRegularPromoLabelDisplayed: " + isRegularPromoLabelDisplayed);
        Assertion.assertThat("is promo infinity label displayed on regular",isRegularPromoLabelDisplayed,is(true));

        driverTools.swipeToElement(PROMO_APPLIED_LABEL_100P_REFUND,Swipe.UP,3).orElseThrow();
        boolean isRefund100pPromoLabelDisplayed = driverTools.waitForElementToPresence(PROMO_APPLIED_LABEL_100P_REFUND, 20).isPresent();
        logger.log("isRefund100pPromoLabelDisplayed: " + isRefund100pPromoLabelDisplayed);
        Assertion.assertThat("is promo infinity label displayed on 100p refund",isRefund100pPromoLabelDisplayed,is(true));

        driverTools.swipeToElement(SELECT_BTN_100_P_REFUND_RESCHEDULE,Swipe.UP,3);
        if(driverTools.isElementDisplayed(SELECT_BTN_100_P_REFUND_RESCHEDULE)){
            driverTools.swipeToElement(PROMO_APPLIED_LABEL_100P_REFUND_RESCHEDULE,Swipe.UP,3).orElseThrow();
            boolean isRefundReschedule100pPromoLabelDisplayed = driverTools.waitForElementToPresence(PROMO_APPLIED_LABEL_100P_REFUND_RESCHEDULE).isPresent();
            logger.log("isRefundReschedule100pPromoLabelDisplayed: " + isRefundReschedule100pPromoLabelDisplayed);
            Assertion.assertThat("is promo infinity label displayed on 100p refund & reschedule",isRefundReschedule100pPromoLabelDisplayed,is(true));

            return isRegularPromoLabelDisplayed && isRefund100pPromoLabelDisplayed && isRefundReschedule100pPromoLabelDisplayed;
        }
        return isRegularPromoLabelDisplayed && isRefund100pPromoLabelDisplayed;
    }

    @Override
    public boolean isCheckInAgainTextDisplayed(){
        WebElement isCheckInAgainDisplayed = driverTools.swipeToElement(CHECK_IN_AGAIN_TEXT_PRP,Swipe.UP,3).orElseThrow();
        return driverTools.isElementDisplayed(isCheckInAgainDisplayed);
    }

    @Override
    public boolean isConnectingFlightProtection(){
        // Open the Self Transfer Protection bottom sheet and verify the protection copy is
        // shown inside it. The PRP exposes two open triggers depending on the build/flight:
        //   - A chevron / info icon next to the inline "Self Transfer Protection included"
        //     text. This is the precise affordance that opens the protection detail sheet.
        //   - Tapping the "Self transfer 3h Xm in <city>" banner text. This is a less
        //     reliable trigger — for some flight options it opens a generic transit-info
        //     sheet that does NOT contain the protection copy.
        // Prefer the chevron when it's present; fall back to the banner tap otherwise.
        // Sometimes the protection only renders inline (no detail sheet exists at all for
        // that flight) — accept the inline presence as a last-resort signal so the test
        // doesn't false-fail on a UI variant.
        boolean sheetOpened = openSelfTransferProtectionBottomSheet();

        boolean isProtectionShown;
        if (sheetOpened) {
            isProtectionShown = driverTools.waitForElementToAppear(SELF_TRANSFER_PROTECTION_INCLUDED_BS, 8).isPresent();
            if (!isProtectionShown) {
                logger.log("Bottom sheet opened but protection text not visible — attempting one swipe up inside the sheet");
                driverTools.swipeUp();
                isProtectionShown = driverTools.waitForElementToPresenceSafe(SELF_TRANSFER_PROTECTION_INCLUDED_BS, 3).isPresent();
            }
            dismissSelfTransferBottomSheet();
        } else {
            logger.log("Could not open Self Transfer Protection bottom sheet — verifying inline PRP card as fallback");
            isProtectionShown = driverTools.waitForElementToPresenceSafe(SELF_TRANSFER_PROTECTION_INCLUDED_BS, 2).isPresent();
        }

        logger.log("isSelfTransferProtectionIncludedShown : " + isProtectionShown);
        return isProtectionShown;
    }

    /**
     * Opens the Self Transfer Protection bottom sheet from the PRP card. Tries the chevron
     * next to the inline "Self Transfer Protection included" text first because it's the
     * specific affordance for that detail sheet. Falls back to tapping the "Self transfer"
     * banner static text when the chevron isn't rendered.
     *
     * <p>Returns {@code true} only when the close button of the bottom sheet is detected,
     * which is the most reliable signal that an Android bottom sheet is fully presented.
     */
    private boolean openSelfTransferProtectionBottomSheet() {
        Optional<WebElement> chevron = driverTools.waitForElementToPresenceSafe(SELF_TRANSFER_PROTECTION_CHEVRON, 3);
        if (chevron.isPresent()) {
            logger.log("Opening Self Transfer Protection bottom sheet via chevron next to protection text");
            driverTools.click(chevron.get());
        } else {
            Optional<WebElement> bannerOpt = driverTools.swipeToElement(CHECK_IN_AGAIN_TEXT_PRP, Swipe.UP, 3);
            if (bannerOpt.isEmpty()) {
                logger.log("Neither chevron nor self-transfer banner found on PRP — cannot open bottom sheet");
                return false;
            }
            logger.log("Chevron not found; falling back to tapping the self-transfer banner text");
            driverTools.click(bannerOpt.get());
        }
        return driverTools.waitForElementToPresenceSafe(SELF_TRANSFER_BOTTOM_SHEET_CLOSE, 10).isPresent();
    }

    /**
     * Dismisses the self-transfer protection bottom sheet so subsequent PRP actions
     * are not blocked by a modal. Tries the explicit close button first, then falls
     * back to the device back action which closes any standard Android bottom sheet.
     *
     * <p>Uses the close-button locator (not the protection-text locator) to detect
     * whether the sheet is still open: the protection text can also be rendered
     * inline on the underlying PRP card, so checking for it yields false positives
     * once the sheet has actually been closed and the PRP becomes visible again.
     */
    private void dismissSelfTransferBottomSheet() {
        if (driverTools.waitForElementToPresenceSafe(SELF_TRANSFER_BOTTOM_SHEET_CLOSE, 3).isPresent()) {
            driverTools.click(SELF_TRANSFER_BOTTOM_SHEET_CLOSE);
        } else {
            logger.log("Self-transfer bottom sheet close button not found; falling back to device back to dismiss.");
            driverTools.back();
        }
        if (driverTools.waitForElementToPresenceSafe(SELF_TRANSFER_BOTTOM_SHEET_CLOSE, 2).isPresent()) {
            logger.log("Self-transfer bottom sheet still visible after dismissal attempt; performing device back as last resort.");
            driverTools.back();
        }
    }

    @Override
    public void clickDetailPRP(){
        driverTools.swipeToElement(CLICK_DETAILS, Swipe.UP,5).orElseThrow();
        driverTools.click(CLICK_DETAILS);
    }

    @Override
    public void clickRefundReschedulePolicyTab(){
        // The legacy "Refund & Reschedule" combined tab no longer exists on the new UI which
        // exposes Refund and Reschedule as separate top-level tabs. Click only if the legacy
        // header is still rendered to remain backwards-compatible with older builds.
        if (driverTools.isElementDisplayed(REFUND_RESCHEDULE_TAB)) {
            driverTools.click(REFUND_RESCHEDULE_TAB);
        } else {
            logger.log("Legacy 'Refund & Reschedule' tab not present - new UI has separate Refund/Reschedule tabs");
        }
    }

    @Override
    public void clickRefundTab() {
        driverTools.waitForElementToAppear(REFUND_TAB, 10);
        logger.log("Clicking 'Refund' policy sub-tab");
        driverTools.click(REFUND_TAB);
    }

    @Override
    public void clickRescheduleTab() {
        driverTools.waitForElementToAppear(RESCHEDULE_TAB, 10);
        logger.log("Clicking 'Reschedule' policy sub-tab");
        driverTools.click(RESCHEDULE_TAB);
    }

    @Override
    public void clickDepartChip() {
        driverTools.waitForElementToAppear(DEPART_CHIP, 10);
        logger.log("Clicking 'Depart' leg chip");
        driverTools.click(DEPART_CHIP);
    }

    @Override
    public void clickReturnChip() {
        driverTools.waitForElementToAppear(RETURN_CHIP, 10);
        logger.log("Clicking 'Return' leg chip");
        driverTools.click(RETURN_CHIP);
    }

    @Override
    public boolean isPolicyInfoVisible() {
        boolean rowVisible = driverTools.isElementDisplayed(INFO_ROW_CONTAINER);
        boolean notAvailableVisible = !rowVisible && driverTools.isElementDisplayed(INFO_NOT_AVAILABLE);
        boolean visible = rowVisible || notAvailableVisible;
        logger.log("Policy info - row container present? " + rowVisible
                + " | 'info not available' text present? " + notAvailableVisible);
        driverTools.click(CLOSE_TABS);
        return visible;
    }

    @Override
    public boolean isPolicyRowVisible() {
        boolean rowVisible = driverTools.isElementDisplayed(INFO_ROW_CONTAINER);
        logger.log("[isPolicyRowVisible] INFO_ROW_CONTAINER present? " + rowVisible);
        return rowVisible;
    }

    @Override
    public boolean verifyRefundInfo(){
        clickRefundTab();
        clickDepartChip();
        boolean depart = isPolicyInfoVisible();
        Assertion.assertThat("Refund - Depart - policy row OR 'info not available' text is shown",
                depart, is(true));

        clickReturnChip();
        boolean ret = isPolicyInfoVisible();
        Assertion.assertThat("Refund - Return - policy row OR 'info not available' text is shown",
                ret, is(true));
        return depart && ret;
    }

    @Override
    public boolean isRefundNotAllowedTextDisplayed(){
        driverTools.swipeToElement(DEPARTURE_FLIGHT, Swipe.DOWN,5);
        logger.log("Departure refund percentage information is displayed");
        boolean res1 = driverTools.isElementDisplayed(REFUND);
        driverTools.swipeToElement(RETURN_FLIGHT, Swipe.UP,5).orElseThrow();
        driverTools.swipeUp(3);
        logger.log("Return refund percentage information is displayed");
        boolean res2 = driverTools.isElementDisplayed(REFUND);
        return res1 && res2;
    }

    @Override
    public boolean isRescheduleNotAllowedTextDisplayed(){
        driverTools.click(RESCHEDULE_CHIP);
        logger.log("Departure refund percentage information is displayed");
        boolean res1 = driverTools.isElementDisplayed(RESCHEDULE);
        driverTools.swipeToElement(RETURN_FLIGHT, Swipe.UP,5).orElseThrow();
        driverTools.swipeUp(3);
        logger.log("Return refund percentage information is displayed");
        boolean res2 = driverTools.isElementDisplayed(RESCHEDULE);
        return res1 && res2;
    }

    @Override
    public boolean verifyRescheduleInfo(){
        clickRescheduleTab();
        clickDepartChip();
        boolean depart = isPolicyInfoVisible();
        Assertion.assertThat("Reschedule - Depart - policy row OR 'info not available' text is shown",
                depart, is(true));

        clickReturnChip();
        boolean ret = isPolicyInfoVisible();
        Assertion.assertThat("Reschedule - Return - policy row OR 'info not available' text is shown",
                ret, is(true));

        driverTools.click(CLOSE_TABS);
        return depart && ret;
    }

    @Override
    public void continueToBookingForm(){
        // Prefer No upgrade when present; otherwise first fare Select (single-fare).
        Locator selectBtn = NO_UPGRADE_SELECT_BTN;
        if (driverTools.swipeToElement(selectBtn, Swipe.UP, 5).isEmpty()
                && driverTools.waitForElementToAppear(selectBtn, 3).isEmpty()) {
            selectBtn = SELECT_BTN_REGULAR;
            driverTools.swipeToElement(REGULAR_SECTION, Swipe.UP, 10);
        }
        driverTools.swipeToElement(selectBtn, Swipe.UP, 5);
        if (!driverTools.isElementDisplayed(selectBtn)) {
            driverTools.swipeUp();
        }
        driverTools.waitForElementToPresence(selectBtn);
        driverTools.click(selectBtn);

        FlightBookingFormPage<T> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        if(driverTools.waitForElementToAppear(TRIP_DETAIl,5).isPresent())
            return;
        continueIfTravelPolicyPopupDisplayed();
        flightBookingFormPage.skipFullProtectionPrp();
        flightBookingFormPage.isOnBookingPage();
    }

    @Override
    public boolean isMultiFarePRP() {
        // Bring the fare-selection section into view first; without this the stepper may sit
        // below the fold and isElementDisplayed would return a false negative.
        driverTools.swipeToElement(SELECT_TICKET, Swipe.UP, 10);
        boolean multiFare = driverTools.isElementDisplayed(MULTI_FARE);
        logger.log("[isMultiFarePRP] multi-fare stepper displayed? " + multiFare);
        return multiFare;
    }

    @Override
    public Map<String, Boolean> verifyRefundRescheduleInfoMultiFareAndContinue(String tripType) {
        logger.log("[verifyRefundRescheduleInfoMultiFareAndContinue] start - tripType=" + tripType);
        Map<String, Boolean> rowVisibilityPerLeg = new HashMap<>();
        rowVisibilityPerLeg.put("depart", false);
        rowVisibilityPerLeg.put("return", false);

        // ---------- Step 1: Depart leg ----------
        boolean departRowVisible = verifyMultiFareRefundReschedulePopupForLeg("Depart");
        rowVisibilityPerLeg.put("depart", departRowVisible);
        // Click the first Select on the fare list — advances RT from step 1→2, or OW directly to booking form.
        clickFirstSelectMultiFare("Depart");

        if (tripType.equalsIgnoreCase("roundtrip")) {
            // Wait for step-2 (return) fare list to render before opening its See details.
            driverTools.waitForElementToAppear(CLICK_DETAILS, 10);
            // ---------- Step 2: Return leg ----------
            boolean returnRowVisible = verifyMultiFareRefundReschedulePopupForLeg("Return");
            rowVisibilityPerLeg.put("return", returnRowVisible);
            // Final Select on step 2 commits the return fare and advances to the booking form.
            clickFirstSelectMultiFare("Return");
        }

        FlightBookingFormPage<T> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        if (driverTools.waitForElementToAppear(TRIP_DETAIl, 5).isPresent()) {
            logger.log("[verifyRefundRescheduleInfoMultiFareAndContinue] booking form reached without extra popup; row visibility per leg: " + rowVisibilityPerLeg);
            return rowVisibilityPerLeg;
        }
        flightBookingFormPage.skipFullProtectionPrp();
        flightBookingFormPage.isOnBookingPage();
        logger.log("[verifyRefundRescheduleInfoMultiFareAndContinue] done; row visibility per leg: " + rowVisibilityPerLeg);
        return rowVisibilityPerLeg;
    }

    /**
     * Opens the See details popup for the first fare card in the current step, then asserts
     * that policy info is rendered under both the Refund and Reschedule tabs, then closes
     * the popup. The multi-fare popup is per-leg, so unlike the legacy flow there are no
     * Depart/Return chips to interact with — the leg is implied by which step is active.
     *
     * @return {@code true} if the populated row container ({@code INFO_ROW_CONTAINER}) was
     *         rendered on EITHER the Refund or Reschedule tab — i.e. actual policy data was
     *         shown for this leg (as opposed to the "info not available" empty-state).
     */
    private boolean verifyMultiFareRefundReschedulePopupForLeg(String legLabel) {
        logger.log("[multiFare] Opening 'See details' popup for " + legLabel + " leg");
        driverTools.swipeToElement(CLICK_DETAILS, Swipe.UP, 5).orElseThrow();
        driverTools.click(CLICK_DETAILS);

        driverTools.waitForElementToAppear(REFUND_TAB, 10).orElseThrow();
        logger.log("[multiFare] Clicking 'Refund' tab on " + legLabel + " leg popup");
        driverTools.click(REFUND_TAB);
        boolean refundRowVisible = driverTools.isElementDisplayed(INFO_ROW_CONTAINER);
        boolean refundVisible = isMultiFarePolicyInfoVisible();
        Assertion.assertThat(
                "Multi-fare PRP - " + legLabel + " - Refund policy info present",
                refundVisible, is(true));

        driverTools.waitForElementToAppear(RESCHEDULE_TAB, 10).orElseThrow();
        logger.log("[multiFare] Clicking 'Reschedule' tab on " + legLabel + " leg popup");
        driverTools.click(RESCHEDULE_TAB);
        boolean rescheduleRowVisible = driverTools.isElementDisplayed(INFO_ROW_CONTAINER);
        boolean rescheduleVisible = isMultiFarePolicyInfoVisible();
        Assertion.assertThat(
                "Multi-fare PRP - " + legLabel + " - Reschedule policy info present",
                rescheduleVisible, is(true));

        logger.log("[multiFare] Closing " + legLabel + " leg popup");
        driverTools.click(CLOSE_TABS);

        boolean anyRowVisible = refundRowVisible || rescheduleRowVisible;
        logger.log("[multiFare] " + legLabel + " leg - row container present? refund=" + refundRowVisible
                + ", reschedule=" + rescheduleRowVisible + " => anyRowVisible=" + anyRowVisible);
        return anyRowVisible;
    }

    /**
     * Multi-fare-safe visibility check — mirrors {@link #isPolicyInfoVisible()} but does
     * NOT auto-close the popup, because we need to keep the popup open to switch between
     * Refund and Reschedule tabs.
     */
    private boolean isMultiFarePolicyInfoVisible() {
        boolean rowVisible = driverTools.isElementDisplayed(INFO_ROW_CONTAINER);
        boolean notAvailableVisible = !rowVisible && driverTools.isElementDisplayed(INFO_NOT_AVAILABLE);
        boolean visible = rowVisible || notAvailableVisible;
        logger.log("[multiFare] policy info - row container present? " + rowVisible
                + " | 'info not available' present? " + notAvailableVisible);
        return visible;
    }

    /**
     * Clicks the first visible Select CTA on the multi-fare fare-list page. We rely on
     * {@code SELECT_BTN} which is anchored to {@code tds_button_view[1]} so it picks the
     * top-most/cheapest fare's Select button on whichever step is currently active.
     */
    private void clickFirstSelectMultiFare(String legLabel) {
        if (!driverTools.isElementDisplayed(SELECT_BTN)) {
            logger.log("[multiFare] SELECT_BTN not visible for " + legLabel + " — swiping up");
            driverTools.swipeUp();
        }
        WebElement selectBtn = driverTools.waitForElementToPresence(SELECT_BTN).orElseThrow();
        logger.log("[multiFare] Clicking first Select on " + legLabel + " leg");
        driverTools.clickElementByCoordinate(selectBtn);
    }

    private static final Locator FARE_SELECT_WITH_MEAL_AND_SEAT = new Locator("FARE_SELECT_WITH_MEAL_AND_SEAT", AppiumBy.xpath("(//*[(contains(@resource-id,'card_flight_review_fare') or contains(@resource-id,'rv_flight_review_fare_card') or contains(@resource-id,'fare_card'))][.//*[contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'meal') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'makanan')] and .//*[contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'seat') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'kursi')]]//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Select') or contains(@text,'Pilih'))])[1]"));
    private static final Locator BASE_FARE_WITH_MEAL = new Locator("BASE_FARE_WITH_MEAL", AppiumBy.xpath("//*[contains(@text,'Meals:') or contains(@text,'Makanan:') or contains(@text,'1xFree') or contains(@content-desc,'Meals') or contains(@content-desc,'Makanan')]"));
    private static final Locator FARE_SELECT_WITH_MEAL = new Locator("FARE_SELECT_WITH_MEAL", AppiumBy.xpath("(//*[(contains(@resource-id,'card_flight_review_fare') or contains(@resource-id,'rv_flight_review_fare_card') or contains(@resource-id,'fare_card') or contains(@resource-id,'rv_content'))][.//*[contains(@text,'Meals:') or contains(@text,'Makanan:') or contains(@text,'1xFree')]]//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Select') or contains(@text,'Pilih'))])[1] | (//*[contains(@text,'Meals:') or contains(@text,'Makanan:') or contains(@text,'1xFree')]/ancestor::*[.//*[@clickable='true' and (contains(@text,'Select') or contains(@text,'Pilih'))]][1]//*[@clickable='true' and (contains(@text,'Select') or contains(@text,'Pilih'))])[1]"));

    @Override
    public void selectFare(String criteria) {
        String normalized = criteria == null ? "regular" : criteria.toLowerCase().trim();
        driverTools.swipeToElement(SELECT_TICKET, Swipe.UP, 8);
        if (normalized.contains("regular") && !normalized.contains("meal") && !normalized.contains("seat")) {
            logger.log("[selectFare] selecting regular fare");
            if (driverTools.isElementDisplayed(SELECT_BTN_REGULAR) || driverTools.swipeToElement(SELECT_BTN_REGULAR, Swipe.UP, 5).isPresent()) {
                driverTools.clickElementByCoordinate(SELECT_BTN_REGULAR);
            } else {
                clickFirstSelectMultiFare("regular");
            }
            return;
        }
        boolean needMeal = normalized.contains("meal") || normalized.contains("makanan");
        boolean needSeat = normalized.contains("seat") || normalized.contains("kursi");
        if (needMeal && needSeat) {
            logger.log("[selectFare] selecting fare with meal and seat");
            Optional<WebElement> mealSeatSelect = driverTools.swipeToElement(FARE_SELECT_WITH_MEAL_AND_SEAT, Swipe.UP, 4)
                    .or(() -> driverTools.waitForElementToPresence(FARE_SELECT_WITH_MEAL_AND_SEAT, 3));
            if (mealSeatSelect.isPresent()) {
                driverTools.clickElementByCoordinate(mealSeatSelect.get());
                return;
            }
            logger.log("[selectFare] meal+seat fare card not found — trying bundled free-meal fare");
            selectBaseFareWithBundledMeal(criteria);
            return;
        }
        if (needMeal) {
            logger.log("[selectFare] selecting fare with meal");
            selectBaseFareWithBundledMeal(criteria);
            return;
        }
        logger.log("[selectFare] fallback to first Select for criteria: " + criteria);
        clickFirstSelectMultiFare(criteria);
    }

    /**
     * Bundled free-meal PRP variants: No upgrade (insurance layout), Select on meal fare card
     * (single-fare), or first Select.
     */
    private void selectBaseFareWithBundledMeal(String criteria) {
        boolean mealOnBase = driverTools.waitForElementToPresence(BASE_FARE_WITH_MEAL, 5).isPresent()
                || driverTools.swipeToElement(BASE_FARE_WITH_MEAL, Swipe.UP, 3).isPresent();
        Assertion.assertThat("Base fare should show free meal benefit for criteria: " + criteria, mealOnBase, is(true));

        Optional<WebElement> selectBtn = driverTools.waitForElementToPresence(NO_UPGRADE_SELECT_BTN, 2)
                .or(() -> driverTools.swipeToElement(NO_UPGRADE_SELECT_BTN, Swipe.UP, 2));
        if (selectBtn.isPresent()) {
            logger.log("[selectFare] clicking No upgrade Select");
            driverTools.clickElementByCoordinate(selectBtn.get());
            return;
        }

        selectBtn = driverTools.swipeToElement(FARE_SELECT_WITH_MEAL, Swipe.UP, 4)
                .or(() -> driverTools.waitForElementToPresence(FARE_SELECT_WITH_MEAL, 5));
        if (selectBtn.isPresent()) {
            logger.log("[selectFare] clicking Select on fare card with free meal");
            driverTools.clickElementByCoordinate(selectBtn.get());
            return;
        }

        selectBtn = driverTools.swipeToElement(SELECT_BTN, Swipe.UP, 4)
                .or(() -> driverTools.waitForElementToPresence(SELECT_BTN, 5));
        if (selectBtn.isPresent()) {
            logger.log("[selectFare] clicking first Select as last resort");
            driverTools.clickElementByCoordinate(selectBtn.get());
            return;
        }

        throw new RuntimeException(
                "No meal+seat / No upgrade / meal fare Select found for criteria: " + criteria);
    }

    @Override
    public void continueToBookingFormWithFareCriteria(String tripType, String departFareCriteria, String returnFareCriteria) {
        logger.log("[continueToBookingFormWithFareCriteria] tripType=" + tripType
                + " depart=" + departFareCriteria + " return=" + returnFareCriteria);
        driverTools.swipeToElement(SELECT_TICKET, Swipe.UP, 10).orElseThrow();
        selectFare(departFareCriteria);
        if (tripType != null && tripType.equalsIgnoreCase("roundtrip")) {
            selectFare(returnFareCriteria);
        }
        FlightBookingFormPage<T> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        if (driverTools.waitForElementToAppear(TRIP_DETAIl, 5).isPresent()) {
            return;
        }
        flightBookingFormPage.skipFullProtectionPrp();
        flightBookingFormPage.isOnBookingPage();
    }
}
