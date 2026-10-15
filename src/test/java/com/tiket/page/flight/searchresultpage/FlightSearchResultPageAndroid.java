package com.tiket.page.flight.searchresultpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.flight.searchpage.FlightSearchPage;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.CoreMatchers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tiket.core.CoreFunctions.convertPrice;
import static com.tiket.page.flight.pdp.FlightDescriptionPageAndroid.STOP_OVER_PDP;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class FlightSearchResultPageAndroid<T extends WebDriver> extends FlightSearchResultPage<T> {

    public static final Locator FILTER_ICON = new Locator("FILTER_ICON", AppiumBy.xpath("//*[contains(@resource-id,'chips_flight_srp_filter_and_sort') or contains(@resource-id,'tds_chips_icon_left')]"));
    /**
     * Airlines tab on bottom_menu_flight_srp_filter (flightsrp.xml / flightsrp2.xml).
     * Click the clickable parent ViewGroup — {@code tds_title_tv} itself is clickable=false, so indexing
     * {@code (//tds_title_tv)[3]} often "succeeds" without opening the Airlines sheet (filter.xml).
     */
    public static final Locator FILTER_OLD = new Locator("FILTER_OLD", AppiumBy.xpath("//*[contains(@resource-id,'bottom_menu_flight_srp_filter')]//*[@clickable='true' and .//*[contains(@resource-id,'tds_title_tv') and (@text='Airlines' or @text='Maskapai')]]"));
    public static final Locator FILTER_TIME = new Locator("FILTER_TIME", AppiumBy.xpath("//*[contains(@resource-id,'bottom_menu_flight_srp_filter')]//*[@clickable='true' and .//*[contains(@resource-id,'tds_title_tv') and (@text='Time' or @text='Waktu')]]"));
    public static final Locator TIME_FIRST = new Locator("TIME_FIRST", AppiumBy.xpath("(//*[contains(@resource-id,'tds_subtitle_list')])[1]"));
    public static final Locator FILTER_SCROLLABLE = new Locator("FILTER_SCROLLABLE", AppiumBy.className("androidx.recyclerview.widget.RecyclerView"));
    public static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and contains(@text,'Reset')]"));
    public static final Locator SAVE_FILTER_BUTTON = new Locator("SAVE_BUTTON",AppiumBy.id("btn_action_bottom"));
    public static final Locator READMORE = new Locator("READMORE", AppiumBy.id("tds_content_notification_banner"));
    public static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON",AppiumBy.id("tds_button_view"));
    public static final Locator PROMO_ICON = new Locator("PROMO_ICON",AppiumBy.id("fab_flight_srp_information"));
    public static final Locator PROMO_WEBVIEW = new Locator("PROMO_WEBVIEW",AppiumBy.xpath("//*[@alt='promo tiket']"));
    public static final Locator INFOPAGE_WEBVIEW = new Locator("INFOPAGE_WEBVIEW",AppiumBy.xpath("//*[@class='full-text-content']"));
    public static final Locator INFOPAGE_BACK = new Locator("INFOPAGE_BACK",AppiumBy.className("android.widget.ImageButton"));
    private static final Locator ROUTE_TEXT = new Locator("ROUTE_TEXT", AppiumBy.id("tds_header_navigation_appbar"));
    public static final Locator PROMO_CLOSE = new Locator("INFOPAGE_CLOSE",AppiumBy.xpath("//android.view.View[@content-desc]"));
    /**
     * First selectable SRP card (flightsrp.xml): clickable {@code container} wrapping {@code cl_flight_item}.
     * Avoids xpath {@code |} union and loose ancestor walks that can hit the wrong parent.
     */
    public static final Locator FLIGHT_CARD_1 = new Locator("FLIGHT_CARD_1", AppiumBy.xpath("(//*[contains(@resource-id,'container') and @clickable='true' and .//*[contains(@resource-id,'cl_flight_item')]])[1]"));
    /** Legacy SRP price node when {@code cl_flight_item} / clickable container is absent. */
    private static final Locator FLIGHT_CARD_1_LEGACY = new Locator("FLIGHT_CARD_1_LEGACY", AppiumBy.xpath("(//*[contains(@resource-id,'tv_price_deferred_cost')])[1]"));
    public static final Locator IMPORTANT_MESSAGE = new Locator("IMPORTANT_MESSAGE",AppiumBy.id("tv_message"));
    public static final Locator IMPORTANT_MESSAGE_WEBVIEW = new Locator("IMPORTANT_MESSAGE_WEBVIEW",AppiumBy.xpath("//*[@class='info-page']"));
    public static final Locator IMPORTANT_MESSAGE_CLOSE = new Locator("IMPORTANT_MESSAGE_CLOSE",AppiumBy.id("tw_btn_close"));
    public static final Locator OTHER_FACILITIES = new Locator("OTHER_FACILITIES",AppiumBy.id("tv_see_other_facilities"));
    public static final Locator OTHER_FACILITIES_DETAIL = new Locator("OTHER_FACILITIES_DETAIL",AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_detail_page_facilities')]"));
    public static final Locator CLOSE = new Locator("CLOSE",AppiumBy.accessibilityId("Close"));
    public static final Locator CLOSE_PDP = new Locator("CLOSE_PDP",AppiumBy.id("iv_flight_detail_page_close"));
    public static final Locator PRICE_POLICY_TAB = new Locator("PRICE_POLICY_TAB",AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_review_fare_card_detail')]"));

    public static final Locator SEE_DETAILS = new Locator("SEE_DETAILS",AppiumBy.xpath("(//*[contains(@resource-id, 'flight_detail_expand')])[1]"));
    public static final Locator REFUND_ALLOWED = new Locator("REFUND_ALLOWED",AppiumBy.xpath("//*[contains(@resource-id,'tv_refund_status') and (contains(@text,'Bisa') or contains(@text,'allowed'))]"));
    public static final Locator RESCHEDULE_ALLOWED = new Locator("RESCHEDULE_ALLOWED",AppiumBy.xpath("//*[contains(@resource-id,'tv_reschedule_status') and (contains(@text,'Bisa') or contains(@text,'allowed'))]"));
    public static final Locator POLICY_DETAIL = new Locator("POLICY_DETAIL",AppiumBy.id("btn_detail"));
    public static final Locator REFUND_TAB = new Locator("REFUND_TAB",AppiumBy.accessibilityId("Refund"));
    public static final Locator RESCHEDULE_TAB = new Locator("RESCHEDULE_TAB",AppiumBy.accessibilityId("Reschedule"));
    public static final Locator POLICY_INFO = new Locator("POLICY_INFO",AppiumBy.id("rv_policy_info"));
    public static final Locator CLOSE_POLICY = new Locator("CLOSE_POLICY",AppiumBy.id("iv_flight_policies_page_close"));
    public static final Locator PRICE_BREAKDOWN = new Locator("PRICE_BREAKDOWN",AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_review_price_breakdown_value')]"));
    public static final Locator DEPARTURE_DATE = new Locator("DEPARTURE_DATE",AppiumBy.id("tv_flight_departure_date_time"));
    public static final Locator DEPARTURE_AIRPORT = new Locator("DEPARTURE_AIRPORT",AppiumBy.id("tv_flight_departure_airport_name"));
    public static final Locator ARRIVAL_DATE = new Locator("ARRIVAL_DATE",AppiumBy.id("tv_flight_arrival_date_time"));
    public static final Locator ARRIVAL_AIRPORT = new Locator("ARRIVAL_AIRPORT",AppiumBy.id("tv_flight_arrival_airport_name"));
    public static final Locator SELECT_TICKET = new Locator("SELECT_TICKET",AppiumBy.xpath("//*[contains(@resource-id, 'tds_button_view')]"));
    public static final Locator PRICE_DETAIL = new Locator("PRICE_DETAIL",AppiumBy.xpath("(//*[@class='android.widget.TextView' and contains(@resource-id, 'tds_title_tab_layout')])[4]"));
    public static final Locator LPG_BANNER = new Locator("LPG_BANNER",AppiumBy.id("banner_flight_srp_lpg"));
    public static final Locator LPG_CLOSE = new Locator("LPG_CLOSE",AppiumBy.className("android.widget.ImageButton"));
    public static final Locator LPG_WEBVIEW = new Locator("LPG_WEBVIEW",AppiumBy.xpath("//*[@class='text-page']"));
    private static final Locator TRANSIT_FILTER_DIRECT = new Locator("TRANSIT_FILTER_DIRECT",AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and (contains(@text,'Langsung') or contains(@text,'Direct'))]"));
    private static final Locator TRANSIT_FILTER_TRANSIT = new Locator("TRANSIT_FILTER_TRANSIT",AppiumBy.xpath("(//*[contains(@resource-id,'tds_chips_text') and contains(@text,'transit')])[1]"));
    private static final Locator TRANSIT_FILTER_1_TRANSIT = new Locator("TRANSIT_FILTER_1_TRANSIT",AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and (contains(@text,'1 transit') or contains(@text,'1 Transit'))]"));
    private static final Locator TRANSIT_FILTER_2_TRANSIT = new Locator("TRANSIT_FILTER_2_TRANSIT",AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and (contains(@text,'2+ transit') or contains(@text,'2+ Transit'))]"));
    public static final Locator BACK_BTN = new Locator("BACK_BTN",AppiumBy.xpath("//*[contains(@resource-id,'tds_icon_navigation_toolbar') or contains(@resource-id,'fl_left_icon')]"));
    public static final Locator QUICK_FILTER_CHIPS = new Locator("QUICK_FILTER_CHIPS",AppiumBy.id("tds_chips"));
    public static final Locator LOYALTY_CARD_TITLE = new Locator("LOYALTY_CARD_TITLE",AppiumBy.id("tv_loyalty_card_title"));
    public static final Locator HIDE_DETAILS = new Locator("HIDE_DETAILS",AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_hide_detail_benefit')]"));
    public static final Locator SHOW_DETAILS = new Locator("SHOW_DETAILS",AppiumBy.xpath("(//*[contains(@resource-id, 'flight_detail_expand')])[1]"));
    public static final Locator DISCOUNT_TAG = new Locator("DISCOUNT_TAG",AppiumBy.xpath("//*[contains(@resource-id, 'tv_benefit_description') and contains(@text, 'Disc up to') or contains(@text, 'Diskon hingga') or contains(@text, 'Elite Diamond Benefit')]"));
    public static final Locator DISCOUNT_TAG_PRICE_AND_POLICY = new Locator("DISCOUNT_TAG_PRICE_AND_POLICY",AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and ( contains(@text,'Discount') or contains(@text,'Diskon') )]"));
    public static final Locator DISCOUNT_PRICE_PRICE_AND_POLICY = new Locator("DISCOUNT_PRICE_PRICE_AND_POLICY",AppiumBy.xpath("(//*[contains(@resource-id,'cl_flight_price_deferred_cost')]//*[contains(@resource-id,'tv_flight_body') and (contains(@text,'IDR') or contains(@text,'MYR') or contains(@text,'SGD') or contains(@text,'USD') or contains(@text,'THB') or contains(@text,'PHP') or contains(@text,'AUD') or contains(@text,'EUR') or contains(@text,'JPY') or contains(@text,'VND') or contains(@text,'HKD') or contains(@text,'CNY') or contains(@text,'GBP') or contains(@text,'KRW') or contains(@text,'Rp'))])[1] | (//*[contains(@resource-id,'tv_price_deferred_cost')])[1]"));
    public static final Locator DISCOUNT_PRICE_SRP = new Locator("DISCOUNT_PRICE_PRICE_AND_POLICY",AppiumBy.xpath("(//*[contains(@resource-id,'tv_discount') and ( contains(@text,'Discount') or contains(@text,'Diskon') )])[1]"));
    /**
     * "Save IDR &lt;N&gt;" (EN) / "Hemat IDR &lt;N&gt;" (ID) savings amount rendered on the first matching
     * SRP card for special-campaign fares (e.g. Harga Gledek). Targeted by visible text so it
     * is independent of the resource-id used for the regular fare price ({@code tv_price_deferred_cost})
     * and the legacy discount label ({@code tv_discount}).
     *
     * <p>The xpath additionally requires the {@code IDR} currency token in the text so it does
     * not collide with non-monetary banners such as "Save more with the best promo" that also
     * contain the word "Save"; an index of {@code [1]} is used as a defensive guard for the
     * (rare) case where multiple priced cards are visible.</p>
     */
    public static final Locator SPECIAL_CAMPAIGN_SAVINGS_PRICE = new Locator("SPECIAL_CAMPAIGN_SAVINGS_PRICE",
            AppiumBy.xpath("(//*[@class=\"android.widget.TextView\" and (contains(@text, \"Save\") or contains(@text, \"Hemat\")) and contains(@text, \"IDR\")])[1]"));
    public static final Locator TEST = new Locator("TEST",AppiumBy.xpath("(//*[contains(@resource-id,'rl_label_container')]//*[contains(@text,'Antigen Test')])"));
    public static final Locator FREE_TRANSFER = new Locator("FREE_TRANSFER",AppiumBy.xpath("(//*[contains(@resource-id,'v_flight_srp_promo_benefit')]//*[contains(@text,'Gratis Airport Transfer') or contains(@text,'Free Airport Transfer')])[1]"));
    public static final Locator FREE_TRANSFER_DETAIL = new Locator("FREE_TRANSFER_DETAIL",AppiumBy.xpath("//*[contains(@text, \"Airport Transfer\") and ./preceding-sibling::*[@resource-id=\"com.tiket.gits.preprod:id/iv_benefit_icon\"]]"));
    public static final Locator FREE_TRANSFER_DETAIL_SRP = new Locator("FREE_TRANSFER_DETAIL_SRP",AppiumBy.xpath("//*[contains(@resource-id,'tv_benefit_title') and (contains(@text,'Gratis Airport Transfer') or contains(@text,'Free Airport Transfer'))]"));
    public static final Locator TEST_DETAIL = new Locator("TEST_DETAIL",AppiumBy.xpath("//*[@text=\"Free Antigen\"]"));
    public static final Locator TEST_DETAIL_SRP = new Locator("TEST_DETAIL_SRP",AppiumBy.xpath("//*[contains(@resource-id,'tv_benefit_title') and (contains(@text,'Gratis Antigen') or contains(@text,'Free Antigen'))]"));
    public static final Locator POLICY_SCROLLABLE = new Locator("POLICY_SCROLLABLE",AppiumBy.id("rv_pdp_price_policies"));
    public static final Locator SPECIAL_DISCOUNT_LOGO = new Locator("SPECIAL_DISCOUNT_LOGO",AppiumBy.id("iv_special_discount"));
    public static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE",AppiumBy.xpath("//*[@class='android.view.ViewGroup' and (.//*[contains(@text,'Pay now') or contains(@text,'Pay Now')] or .//*[contains(@text,'Bayar sekarang')])]"));
    public static final Locator HARGA_GLEDEK_DISCOUNT = new Locator("HARGA_GLEDEK_DISCOUNT", By.xpath("//*[contains(@resource-id,'rv_price_detail')]//*[contains(@resource-id,'tv_title') and contains(@text,'Harga Gledek')]"));
    public static final Locator HARGA_GLEDEK_WHAT_YOU_GET = new Locator("HARGA_GLEDEK_WHAT_YOU_GET",AppiumBy.xpath("//*[contains(@resource-id,'iv_icon')]/following-sibling::*[contains(@resource-id,'tv_title') and contains(@text,'Harga Gledek')]"));
    public static final Locator LOYALTY_WHAT_YOU_GET = new Locator("LOYALTY_WHAT_YOU_GET",AppiumBy.xpath("//*[contains(@resource-id,'iv_icon')]/following-sibling::*[contains(@resource-id,'tv_title') and contains(@text,'Elite') and contains(@text,'Benefit')]"));
    public static final Locator LOYALTY_DISCOUNT = new Locator("LOYALTY_DISCOUNT",AppiumBy.xpath("//*[contains(@resource-id,'rv_price_detail')]//*[contains(@resource-id,'tv_title') and contains(@text,'Elite') and (contains(@text,'Diskon') or contains(@text,'Discount'))]"));
    public static final Locator PRICE_ON_FLIGHT_PRICE_DATE_PICKER_SRP = new Locator("PRICE_ON_FLIGHT_PRICE_DATE_PICKER_SRP",AppiumBy.xpath("//*[contains(@resource-id,'selected_stripe')]//preceding-sibling::*[contains(@resource-id,'tv_price')]"));
    public static final Locator ICON_ON_THE_RIGHT_OF_TOTAL_PAYMENT = new Locator("ICON_ON_THE_RIGHT_OF_TOTAL_PAYMENT",AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_price_total')]//following-sibling::*[contains(@resource-id,'iv_loyalty')]"));
    public static final Locator REFUND_POPUP = new Locator("REFUND_POPUP",AppiumBy.xpath("//*[contains(@resource-id,'tds_btn') and (contains(@text,'Siaaap!') or contains(@text,'Got it!'))]"));
    public static final Locator CLOSE_DETAIL = new Locator("CLOSE_DETAIL",AppiumBy.xpath("//*[contains(@resource-id, 'iv_flight_review_fare_detail_close')]"));
    public static final Locator SEE_MORE_AIRLINE = new Locator("SEE_MORE_AIRLINE",AppiumBy.id("btn_flight_srp_filter_see_more"));
    public static final Locator FILTER_AIRLINE_BODY = new Locator("FILTER_AIRLINE_BODY",AppiumBy.xpath("(//*[contains(@resource-id,'tv_flight_base_bottom_sheet_title')]//following-sibling::*//android.widget.LinearLayout)[1]"));
    public static final Locator FILTER_AIRLINE_TITLE= new Locator("FILTER_AIRLINE_TITLE",AppiumBy.xpath("//*[contains(@resource-id,'tds_title_list') or contains(@resource-id,'tv_option_label')]"));
    public static final Locator FILTER_CHIPS_BODY = new Locator("FILTER_CHIPS_BODY",AppiumBy.id("chips_quick_filter"));
    private static final Locator JOIN_NOW_SRP= new Locator("JOIN_NOW_SRP",AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Gabung Sekarang') or contains(@text,'Join Now'))]"));
    public static final Locator LABEL_100_P_REFUND = new Locator("LABEL_100_P_REFUND",AppiumBy.id("tv_flight_pdp_refund_100_percent"));
    public static final Locator MEGAPHONE_ICON_SRP = new Locator("MEGAPHONE_ICON_SRP",AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_notification_banner')]//*[(contains(@text,'Read more') or contains(@text,'Baca selengkapnya'))]"));
    private static final Locator CORPORATE_DEALS_BANNER = new Locator("CORPORATE_DEALS_BANNER",AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_notification_banner')]//*[contains(@text,'Corporate Deal')]"));
    private static final Locator CORPORATE_DEALS_BENEFIT_CARD = new Locator("CORPORATE_DEALS_BENEFIT_CARD",AppiumBy.xpath("//*[contains(@resource-id,'v_flight_price_label_deferred_cost')]//*[contains(@text,'Corporate Deals')]"));
    private static final Locator TRANSIT_INFO_SRP_CARD_DETAIL = new Locator("TRANSIT_INFO_SRP_CARD_DETAIL",AppiumBy.xpath("//*[contains(@label,'Transit info') or contains(@label,'Informasi transit') or contains(@label,'Info transit')]"));
    public static final Locator PRICES = new Locator("PRICES",AppiumBy.id("tv_price"));
    public static final Locator FLIGHT_DETAIL_EXPAND = new Locator("FLIGHT_DETAIL_EXPAND",AppiumBy.xpath("(//*[contains(@resource-id,'flight_detail_expand')])[1]"));
    public static final Locator FLOATING_ICON_SRP = new Locator("FLOATING_ICON_SRP",AppiumBy.id("fab_flight_srp_information"));
    /** Sticky bottom promo entry that replaced the SRP FAB on newer builds. */
    public static final Locator PROMO_INFINITY_BANNER = new Locator("PROMO_INFINITY_BANNER", AppiumBy.id("banner_flight_search_infinity"));
    public static final Locator FLIGHT_TRANSIT_FILTER = new Locator("FLIGHT_TRANSIT_FILTER",AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tv') and contains(@text,'Transit')]"));
    public static final Locator FLIGHT_SORT_FILTER = new Locator("FLIGHT_SORT_FILTER",AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tv') and (contains(@text,'Sort') or contains(@text,'Urutkan'))]"));
    private static final Locator SORT_TAB_IN_ADVANCED_FILTER = new Locator("SORT_TAB_IN_ADVANCED_FILTER", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tab_layout') and (contains(@text,'Sort') or contains(@text,'Urutkan'))]"));
    private static final Locator SORT_BY_SECTION_HEADER = new Locator("SORT_BY_SECTION_HEADER", AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_header_2') and (contains(@text,'Sort by') or contains(@text,'Urutkan'))]"));
    private static final Locator SORT_BY_FILTER_BADGE = new Locator("SORT_BY_FILTER_BADGE", AppiumBy.xpath("(//*[contains(@resource-id,'tv_flight_header_2') and (contains(@text,'Sort by') or contains(@text,'Urutkan'))]/following-sibling::*[contains(@resource-id,'badge_view')])[1]"));
    private static final Locator SORT_SEE_MORE = new Locator("SORT_SEE_MORE", AppiumBy.xpath("(//*[contains(@resource-id,'tv_flight_header_2') and (contains(@text,'Sort by') or contains(@text,'Urutkan'))]/ancestor::*[contains(@resource-id,'recyclerview') or contains(@class,'RecyclerView')][1]//*[contains(@resource-id,'btn_flight_srp_filter_see_more')])[1]"));
    public static final Locator FLIGHT_CARD_FIRST_PRICE = new Locator("FLIGHT_CARD_FIRST_PRICE",AppiumBy.xpath("(//*[contains(@resource-id,'cl_flight_price_deferred_cost')]//*[contains(@resource-id,'tv_flight_body') and (contains(@text,'IDR') or contains(@text,'MYR') or contains(@text,'SGD') or contains(@text,'USD') or contains(@text,'THB') or contains(@text,'PHP') or contains(@text,'AUD') or contains(@text,'EUR') or contains(@text,'JPY') or contains(@text,'VND') or contains(@text,'HKD') or contains(@text,'CNY') or contains(@text,'GBP') or contains(@text,'KRW') or contains(@text,'Rp'))])[1] | (//*[contains(@resource-id,'tv_price_deferred_cost')])[1]"));
    public static final Locator ROUND_TRIP_DEAL_LABEL = new Locator("ROUND_TRIP_DEAL_LABEL",AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_srp_benefit') and (contains(@text,'Round-trip Deals') or contains(@text,'round-trip deals'))]"));
    public static final Locator LAST_CARD_PRICE_BEFORE_DELTA_DESC = new Locator("LAST_CARD_PRICE_BEFORE_DELTA_DESC",AppiumBy.xpath("(//*[contains(@resource-id,'ll_delta_price')]/preceding-sibling::*[1]//*[contains(@resource-id,'cl_flight_item') or contains(@resource-id,'tv_price_deferred_cost')]//*[contains(@resource-id,'tv_flight_body')])[1]"));
    public static final Locator DELTA_PRICE_DESCRIPTION = new Locator("DELTA_PRICE_DESCRIPTION",AppiumBy.id("tv_flight_result_delta_description"));
    public static final Locator FIRST_CARD_PRICE_AFTER_DELTA_DESC = new Locator("FIRST_CARD_PRICE_AFTER_DELTA_DESC",AppiumBy.xpath("(//*[contains(@resource-id,'ll_delta_price')]/following-sibling::*[1]//*[contains(@resource-id,'cl_flight_item') or contains(@resource-id,'tv_price_deferred_cost')]//*[contains(@resource-id,'tv_flight_body')])[1]"));
    public static final Locator CHANGE_BTN = new Locator("CHANGE_BTN",AppiumBy.id("tds_button_view"));
    public static final Locator RELOAD = new Locator("RELOAD",AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Muat ulang') or contains(@text,'Reload'))]"));
    public static final Locator SELECTED_DATE = new Locator("SELECTED_DATE",AppiumBy.xpath("//*[contains(@resource-id,'selected_bg')]//following-sibling::*[contains(@resource-id,'tv_date')]"));
    public static final Locator CLOSE_BTN_FILTER = new Locator("CLOSE_BTN_FILTER",AppiumBy.id("iv_close"));
    public static final Locator CLOSE_BTN_SORT_FILTER = new Locator("CLOSE_BTN_SORT_FILTER",AppiumBy.id("iv_close"));
    private static final Locator SRP_NUDGES = new Locator("SRP_NUDGES", AppiumBy.xpath("//*[contains(@resource-id, 'tv_nudges')]"));
    private static Map<String, Boolean> FLAG_MAP = new HashMap<>();
    public static final Locator TITLE_SUB_HEADER = new Locator("TITLE_SUB_HEADER", AppiumBy.xpath("//*[contains(@resource-id,'tds_sub_header_navigation_appbar') or contains(@resource-id,'tv_subtext')]"));
    public static final Locator CHANGE_SEARCH_BTN_SRP = new Locator("ECONOMY_CLASS_TITLE_SUB_HEADER", AppiumBy.xpath("//android.widget.Button[contains(@resource-id,'tds_button_view') and ( contains(@text,'Ubah Pencarian') or contains(@text,'Change Search') )]"));
    public static final Locator PRICE_SELECTED_DATE = new Locator("PRICE_SELECTED_DATE", AppiumBy.xpath("//*[contains(@resource-id,'selected_stripe')]//preceding-sibling::*[contains(@resource-id,'tv_price')] | //*[contains(@resource-id,'rv_date_price_picker')]//preceding-sibling::*[contains(@resource-id,'tds_extended_tab_content')]"));
    public static final Locator AIRLINE_FILTER_TITLE = new Locator("AIRLINE_FILTER_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_list') or contains(@resource-id,'tv_option_label')]"));
    public static final Locator APPLY_PROMO_INFINITY = new Locator("APPLY_PROMO_INFINITY", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Apply') or contains(@text,'Pakai'))]"));
    public static final Locator CANCEL_PROMO_INFINITY = new Locator("CANCEL_PROMO_INFINITY", AppiumBy.xpath("//*[contains(@resource-id,'banner_flight_search_infinity')]//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Cancel') or contains(@text,'Batalkan'))]"));
    public static final Locator PROMO_APPLIED_TEXT = new Locator("PROMO_APPLIED_TEXT", AppiumBy.xpath("//*[contains(@text,'Promo terbaik terpasang. Pilihan bagus!') or contains(@text,'Best promo applied. Good choice!')]"));
    public static final Locator PROMO_APPLIED_LABEL = new Locator("PROMO_APPLIED_LABEL", AppiumBy.xpath("//*[(contains(@text,'After promo') or contains(@text,'Setelah promo')) and contains(@resource-id,'tv_flight_body')]"));
    public static final Locator AIRLINE_NAME = new Locator("AIRLINE_NAME", AppiumBy.id("tv_airline_name"));
    public static final Locator AIRLINE_COUNT = new Locator("AIRLINE_COUNT", AppiumBy.xpath("(//*[contains(@resource-id, 'tds_button_view')])[2]"));
    public static final Locator PROGRESS_BAR_SRP = new Locator("PROGRESS_BAR_SRP", AppiumBy.className("android.widget.ProgressBar"));
    public static final Locator SEE_DEPARTURE_DETAILS = new Locator("SEE_DEPARTURE_DETAILS", AppiumBy.xpath("//*[@resource-id=\"com.tiket.gits.preprod:id/v_flight_benefit\"]"));
    public static final Locator AIRPORT_TRANSFER_DETAIL = new Locator("AIRPORT_TRANSFER_DETAIL", AppiumBy.xpath("//*[@class=\"android.view.ViewGroup\" and (./*[@text=\"Free Airport Transfer\"] or ./*[@text=\"Gratis Antar-Jemput Bandara\"]) ]"));
    public static final Locator ANTIGEN_TEST_DETAIL = new Locator("ANTIGEN_TEST_DETAIL", AppiumBy.xpath("//*[@class=\"android.view.ViewGroup\" and (./*[@text=\"Free Antigen\"] or ./*[@text=\"Gratis Antigen\"]) ]"));
    public static final Locator SELECT_TICKECT = new Locator("SELECT_TICKECT", AppiumBy.xpath("//*[@text=\"Select your ticket\" or @text=\"Pilih tiket Anda\"]"));
    public static final Locator CLOSE_BTN_PRP = new Locator("CLOSE_BTN_PRP", AppiumBy.xpath("//*[@resource-id=\"com.tiket.gits.preprod:id/iv_close\"]"));
    public static final Locator SAVE_LABEL = new Locator("SAVE_LABEL", AppiumBy.xpath("//*[contains(@text,'Save')]"));
    public static final Locator CASHBACK_LABEL = new Locator("CASHBACK_LABEL", AppiumBy.id("tv_after_cashback_deferred_cost"));
    public static final Locator CLICK_DETAILS = new Locator("CLICK_DETAILS", AppiumBy.xpath("(//*[contains(@resource-id, 'tv_flight_review_fare_card_detail') and contains(@text, 'See details') or contains(@text, 'Lihat Detail')])[1]"));
    public static final Locator MASTER_FILTER = new Locator("MASTER_FILTER", AppiumBy.xpath("//*[contains(@resource-id,'bottom_menu_flight_srp_filter')]//*[@clickable='true' and .//*[contains(@resource-id,'tds_title_tv') and (@text='Filter' or @text='Saring' or @text='Filters')]]"));
    public static final Locator OTHER_FILTER_TITLE = new Locator("OTHER_FILTER_TITLE",AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_header_2') and (contains(@text,'Other Filters') or contains(@text,'Filter Lainnya'))]"));
    public static final Locator SEE_ALL_OTHER_FILTER = new Locator("SEE_ALL_OTHER_FILTER",AppiumBy.xpath("(//*[contains(@resource-id,'tv_flight_header_2') and (contains(@text,'Other Filters') or contains(@text,'Filter Lainnya'))]//parent::*//following-sibling::*//*[contains(@resource-id,'btn_flight_srp_filter_see_more')])[1]"));
    /** Airline label on each flight card under the dynamic list body (SRP). */
    private static final Locator SRP_FLIGHT_CARD_AIRLINE_NAMES = new Locator("SRP_FLIGHT_CARD_AIRLINE_NAMES", AppiumBy.xpath("//*[contains(@resource-id,'flight_info')]//*[contains(@resource-id,'tv_flight_body')]"));
    /** First card only; same {@code tv_airline_name} pattern as {@link #SRP_FLIGHT_CARD_AIRLINE_NAMES}. */
    private static final Locator SRP_FLIGHT_CARD_AIRLINE_NAMES_FIRST = new Locator("SRP_FLIGHT_CARD_AIRLINE_NAMES", AppiumBy.xpath("(//*[contains(@resource-id,'flight_info')]//*[contains(@resource-id,'tv_flight_body')])[1]"));
    /** Flight result card root on SRP ({@code cl_flight_item} from flightsrp / flightsrp2 dumps). */
    private static final Locator FLIGHT_ITEM_CARDS = new Locator("FLIGHT_ITEM_CARDS", AppiumBy.xpath("//*[contains(@resource-id,'cl_flight_item')]"));
    private static final Locator FLIGHT_CARD_DEPART_HOUR = new Locator("FLIGHT_CARD_DEPART_HOUR", AppiumBy.xpath(".//*[contains(@resource-id,'tv_depart_hour')]"));
    private static final Locator FLIGHT_CARD_ARRIVAL_HOUR = new Locator("FLIGHT_CARD_ARRIVAL_HOUR", AppiumBy.xpath(".//*[contains(@resource-id,'tv_arrival_hour')]"));
    /** Airline name is typically the last non-transit {@code tv_flight_body} under {@code ll_flight_info}. */
    private static final Locator FLIGHT_CARD_AIRLINE_FROM_INFO = new Locator("FLIGHT_CARD_AIRLINE_FROM_INFO", AppiumBy.xpath("(.//*[contains(@resource-id,'ll_flight_info')]//*[contains(@resource-id,'tv_flight_body') and not(contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'transit'))])[last()]"));
    private static final Locator FLIGHT_CARD_AIRLINE_NAME_LEGACY = new Locator("FLIGHT_CARD_AIRLINE_NAME_LEGACY", AppiumBy.xpath(".//*[contains(@resource-id,'tv_airline_name')]"));
    /** Airlines filter bottomsheet list recycler — scoped under bottomsheet (filter.xml). */
    private static final Locator AIRLINE_FILTER_RECYCLER = new Locator("AIRLINE_FILTER_RECYCLER",
            AppiumBy.xpath("//*[contains(@resource-id,'fl_bottomsheet_content') or contains(@resource-id,'crf_bottom_sheet')]//*[contains(@resource-id,'recycler_view')]"));
    // Nudge lives on tv_nudges inside cl_nudges (legacy: resource-id containing 'label')
    public static final Locator MEET_TRAVEL_POLICY_1 = new Locator("MEET_TRAVEL_POLICY_1", AppiumBy.xpath(
            "(//*[contains(@resource-id,'tv_nudges') and (contains(@text,'Meets Travel Policy') or contains(@text,'Sesuai Kebijakan'))]"
                    + " | //*[contains(@resource-id,'cl_nudges')]//*[contains(@text,'Meets Travel Policy') or contains(@text,'Sesuai Kebijakan')]"
                    + " | //*[contains(@resource-id,'label')]//*[contains(@text,'Meets Travel Policy') or contains(@text,'Sesuai Kebijakan')])[1]"));

    public FlightSearchResultPageAndroid(Session<T> session) {
        super(session);
    }

    /** XPath 1.0 string literal for {@code contains(...)} — escapes {@code '} in airline names. */
    private static String xpathStringLiteralForContains(String value) {
        if (value == null || value.isEmpty()) {
            return "''";
        }
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        String[] parts = value.split("'", -1);
        StringBuilder b = new StringBuilder("concat(");
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                b.append(", \"'\", ");
            }
            b.append("'").append(parts[i]).append("'");
        }
        b.append(")");
        return b.toString();
    }

    /**
     * Airlines bottomsheet option row for {@code airline}.
     * New UI (filter.xml): {@code tv_option_label} text like {@code Garuda Indonesia (1)} — click the clickable parent row.
     * Legacy UI: {@code tds_title_list}.
     */
    private Locator getAirlineFilterOptionLocator(String airline) {
        String key = airline.toLowerCase(Locale.ROOT).trim();
        String keyLit = xpathStringLiteralForContains(key);
        String keyWithCountLit = xpathStringLiteralForContains(key + " (");
        // Prefer starts-with "Airline (" for new UI (e.g. Garuda Indonesia (1)) to avoid short substring misses.
        // Legacy tds_title_list: starts-with full name, optionally followed by " (".
        return new Locator("Airline Filter Option",
                AppiumBy.xpath("//*[contains(@resource-id,'tv_option_label') and starts-with(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," + keyWithCountLit + ")]/ancestor::*[@clickable='true'][1] | //*[contains(@resource-id,'tv_option_label') and translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')=" + keyLit + "]/ancestor::*[@clickable='true'][1] | //*[contains(@resource-id,'tds_title_list') and starts-with(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," + keyLit + ")]"));
    }

    @Override
    public void filterAirline(ArrayList<String> airlines) {
        driverTools.waitForElementToAppear(CLOSE_BTN_FILTER);
        this.resetFilter();

        Collections.sort(airlines);
        Map<String, Boolean> airlineCount = new HashMap<>();
        int retry = 8;

        while (retry > 0 && airlineCount.size() != airlines.size()) {
            for (String airline : airlines){
                String key = airline.toLowerCase().trim();
                Locator airlineName = airlineOptionLocator(key);
                if(driverTools.isElementDisplayed(airlineName)){
                    if(!airlineCount.containsKey(key)){
                        driverTools.click(airlineName);
                        logger.log("airline[ name: " + key + ",isPresent: " + true + " ]");
                        airlineCount.put(key,true);
                    }
                }
            }
            if(airlineCount.size() == airlines.size())
                break;
            driverTools.swipeUp();
            retry--;
        }

        if(airlineCount.isEmpty()){
            throw new NoSuchElementException("required airline is not present " + airlines);
        }
    }

    /** Airline row label in filter sheet — supports legacy {@code tds_title_list} and new {@code tv_option_label}. */
    private Locator airlineOptionLocator(String airlineSubstring) {
        String key = airlineSubstring == null ? "" : airlineSubstring.toLowerCase().trim();
        return new Locator("Airline Name", AppiumBy.xpath(
                "//*[contains(@resource-id,'crf_bottom_sheet') or contains(@resource-id,'fl_bottomsheet_content')]//*[(contains(@resource-id,'tds_title_list') or contains(@resource-id,'tv_option_label')) and contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + key + "')]"));
    }

    /**
     * Sorts the provided airline list alphabetically (A-Z) and selects the first available airline.
     * The method checks each airline in sorted order and returns immediately when the first one is found.
     *
     * @param airlines List of airline names to check (will be sorted A-Z)
     * @return The name of the first airline that was found and selected from the sorted list
     * @throws NoSuchElementException if none of the airlines from the list are available
     */
    @Override
    public String filterFirstFromListAirline(ArrayList<String> airlines) {
        String methodName = "filterFirstFromListAirline";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForElementToAppear(CLOSE_BTN_FILTER);
            this.resetFilter();

            // Sort airlines alphabetically A-Z
            Collections.sort(airlines);

            // Track which airlines have been checked
            Set<String> checkedAirlines = new HashSet<>();
            int maxRetries = 6;

            // Try to find airlines with scrolling
            for (int retry = 0; retry < maxRetries; retry++) {
                // Check each airline in sorted order
                for (String airline : airlines) {
                    // Skip if already checked this airline
                    if (checkedAirlines.contains(airline)) {
                        continue;
                    }

                    String formattedAirline = CoreFunctions.titleize(airline);
                    if (formattedAirline.contains("nam")) formattedAirline = formattedAirline.replace("Nam", "NAM");

                    Locator airlineName = airlineOptionLocator(formattedAirline);

                    if (driverTools.isElementDisplayed(airlineName)
                            || driverTools.swipeToElement(airlineName, AIRLINE_FILTER_RECYCLER, Swipe.UP, 3).isPresent()
                            || driverTools.swipeToElement(airlineName, Swipe.UP, 3).isPresent()) {
                        // Found the airline - select it and return
                        driverTools.click(airlineName);
                        logger.log("airline selected: " + formattedAirline + " (from sorted list)");
                        return airline;
                    }

                    // Mark as checked to avoid rechecking in next scroll iteration
                    checkedAirlines.add(airline);
                }

                // If all airlines checked, no need to scroll more
                if (checkedAirlines.size() == airlines.size()) {
                    break;
                }

                // Scroll to see more airlines
                driverTools.swipeUp();
            }

            // No airline from the list was found
            throw new NoSuchElementException("No airline from the provided list is available. Tried airlines (A-Z): " + airlines);
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void changeDate(String date){
        this.checkReloadPopup();
    }

    @Override
    public void changeTrip(String tripType){}

    @Override
    public void checkReloadPopup(){
        if (Configs.ENVIRONMENT == Environment.GK) {
            //these popups happen when there are flight adjustments, usually on GK
            for(int tries = 5; tries>0 && driverTools.waitForElementToAppear(RELOAD,3).isPresent(); tries--){
                driverTools.click(RELOAD);
            }
        }
    }

    @Override
    public void openFilter() {
        String methodName = "openFilter";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForElementToAppear(FLIGHT_CARD_1, 20);
            if (driverTools.isElementDisplayed(FILTER_ICON))
                driverTools.click(FILTER_ICON);
            else
                driverTools.click(FILTER_OLD);
            // Airlines bottomsheet (filter.xml) exposes iv_close — fail fast if tap did not open it.
            driverTools.waitForElementToAppear(CLOSE_BTN_FILTER, 10).orElseThrow();
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void openAdvancedFilter() {
        String methodName = "openAdvancedFilter";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForElementToAppear(FLIGHT_CARD_1, 20);
            if (driverTools.isElementDisplayed(FILTER_ICON))
                driverTools.click(FILTER_ICON);
            else
                driverTools.click(MASTER_FILTER);
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void filterMorningFlight() {
        driverTools.click(FILTER_TIME);
        driverTools.click(TIME_FIRST);
        saveFilter();
    }

    @Override
    public void saveFilter() {
        String methodName = "saveFilter";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.click(SAVE_FILTER_BUTTON);
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void resetFilter() {
        if (driverTools.isElementEnabled(RESET_FILTER_BUTTON)) {
            driverTools.click(RESET_FILTER_BUTTON);
        }
    }


    private Locator otherFilterLocator(String filterType){
        return new Locator("Other Filter Locator",AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_list') and contains(@text,'"+filterType+"')]"));
    }

    private void openMasterFilter(){
        driverTools.waitForElementToAppear(MASTER_FILTER,15);
        driverTools.click(MASTER_FILTER);
        driverTools.waitForElementToPresence(CLOSE);
    }

    private void filterOtherType(String filterType){
        driverTools.swipeToElement(OTHER_FILTER_TITLE, Swipe.UP,10).orElseThrow();
        Locator otherFilter = otherFilterLocator(filterType);
        driverTools.swipeToElement(otherFilter, Swipe.UP,1);
        if(driverTools.isElementDisplayed(otherFilter))
            driverTools.click(otherFilter);
        else {
            driverTools.swipeDown();
            driverTools.swipeToElement(SEE_ALL_OTHER_FILTER, Swipe.UP,1).orElseThrow();
            driverTools.click(SEE_ALL_OTHER_FILTER);
            driverTools.waitForElementToPresence(CLOSE);
            driverTools.click(otherFilter);
            driverTools.click(SAVE_FILTER_BUTTON);
        }
    }

    @Override
    public void filterOther(String filterType){
        openMasterFilter();
        filterOtherType(filterType);
        saveFilter();
    }

    @Override
    public boolean isPromoPagePresent() {
        logger.log("Verifying promo page");
        driverTools.click(PROMO_ICON);
        driverTools.waitForElementToAppear(PROMO_CLOSE);
        driverTools.switchToWebView();
        boolean promoPage = driverTools.waitForElementToAppear(PROMO_WEBVIEW,10).isPresent();
        driverTools.switchToNative();
        driverTools.click(PROMO_CLOSE);
        return promoPage;
    }

    @Override
    public boolean isTravelInfoPagePresent() {
        logger.log("Verifying travel info page");
        driverTools.click(READMORE);
        driverTools.waitForElementToAppear(INFOPAGE_BACK);
        driverTools.switchToWebView();
        boolean infoPage = driverTools.waitForElementToAppear(INFOPAGE_WEBVIEW,10).isPresent();
        driverTools.switchToNative();
        driverTools.click(INFOPAGE_BACK);
        return infoPage;
    }

    @Override
    public void selectFlight() {
        String methodName = "selectFlight";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            // After saveFilter any card may be visible while the first card is still off-screen —
            // always swipe the first-card target into view before tapping (do not skip on FLIGHT_ITEM_CARDS alone).
            driverTools.waitForElementToAppear(FLIGHT_ITEM_CARDS, 20);
            if (driverTools.waitForElementToPresence(FLIGHT_CARD_1).isPresent()) {
                WebElement flightCard = driverTools.swipeToPresentedElement(FLIGHT_CARD_1, Swipe.DOWN, 3).orElseThrow();
                driverTools.clickElementByCoordinate(flightCard);
            } else {
                driverTools.swipeToPresentedElement(FLIGHT_CARD_1_LEGACY, Swipe.DOWN, 3).orElseThrow();
                driverTools.clickElementByCoordinate(FLIGHT_CARD_1_LEGACY);
            }
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void selectFlightSpecialCampaign(String airline) {
        String methodName = "selectFlight";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.swipeToElement(getAirlineLocator(airline),Swipe.DOWN,1);
            driverTools.click(getAirlineLocator(airline));
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    private Locator getAirlineLocator(String airline){
        return new Locator("Airline",AppiumBy.xpath("(//*[contains(@resource-id, 'tv_airline_name') and contains(@text, '" + airline + "')])[1]"));
    }

    @Override
    public void selectFlightVI(String airline){
        driverTools.swipeToElement(getViAirlineLocatorIndex(airline,1),Swipe.UP,6);
        driverTools.click(getViAirlineLocator());
    }

    @Override
    public void selectFlightCorporateDeals() {
        driverTools.swipeToElement(CORPORATE_DEALS_BENEFIT_CARD, Swipe.UP, 5);
        driverTools.click(CORPORATE_DEALS_BENEFIT_CARD);
    }

    @Override
    public void isSearchDateToday(String departureDate,String lang) {
        WebElement selectedDateElement = driverTools.waitForElementToAppear(SELECTED_DATE).orElseThrow();
        String selectedDate = driverTools.getText(selectedDateElement);

        String[] dateSplit = departureDate.split(" ");
        String month = changeMonthAccordingToLanguage(dateSplit[1],lang).substring(0,3);
        logger.log("month code -> " + month);
        String expectedDate = dateSplit[0] + " " + month + " " + dateSplit[2];
        Assertion.assertThat("is selected date today",selectedDate,is(containsString(expectedDate)));
    }

    private String changeMonthAccordingToLanguage(String month,String language){
        if(!language.equalsIgnoreCase("en"))
            return CoreFunctions.monthEngToId(month);
        return month;
    }

    private Locator getFlightCardLocator(int index){
        return new Locator("FLIGHT_CARD_" + index,AppiumBy.xpath("(//*[contains(@resource-id,'cardView')])["+index+"]"));
    }
    @Override
    public void selectFlightStopOver(){
        for (int i=1;i<=3;i++){
            driverTools.swipeToElement(getFlightCardLocator(i),Swipe.UP,1);
            driverTools.click(getFlightCardLocator(i));
            if(driverTools.isElementDisplayed(STOP_OVER_PDP)){
                break;
            }
            driverTools.click(CLOSE_PDP);
            logger.log("fail to find stop over : " + i);
        }
    }

    @Override
    public boolean isImportantInfoPagePresent() {
        logger.log("Verifying important info page");
        driverTools.click(IMPORTANT_MESSAGE);
        driverTools.waitForElementToAppear(IMPORTANT_MESSAGE_CLOSE);
        driverTools.switchToWebView();
        boolean infoPage = driverTools.waitForElementToAppear(IMPORTANT_MESSAGE_WEBVIEW,10).isPresent();
        driverTools.switchToNative();
        driverTools.click(IMPORTANT_MESSAGE_CLOSE);
        return infoPage;
    }

    @Override
    public boolean isOtherFacilitiesPresent() {
        logger.log("Verifying other facilities");
        driverTools.click(OTHER_FACILITIES);
        boolean otherFacilities = driverTools.waitForElementToAppear(OTHER_FACILITIES_DETAIL,10).isPresent();
        driverTools.click(CLOSE);
        return otherFacilities;
    }

    @Override
    public void clickPricePolicyTab() {
        driverTools.waitForElementToPresence(SEE_DETAILS);
        driverTools.click(SEE_DETAILS);
        driverTools.waitForElementToPresence(PRICE_DETAIL);
        driverTools.click(PRICE_DETAIL);
    }

    @Override
    public boolean isRefundReschedulePolicyCorrect() {
        logger.log("Verifying refund and reschedule policy");
        boolean refundAllowed = driverTools.isElementDisplayed(REFUND_ALLOWED);
        logger.log("Is refund allowed:  " + refundAllowed);

        boolean rescheduleAllowed = driverTools.isElementDisplayed(RESCHEDULE_ALLOWED);
        logger.log("Is reschedule allowed:  " + rescheduleAllowed);

        boolean refundPolicy = true;
        boolean reschedulePolicy = true;

        if (refundAllowed || rescheduleAllowed){
            driverTools.click(POLICY_DETAIL);
            refundPolicy = verifyPolicy("refund",refundPolicy);
            reschedulePolicy = verifyPolicy("reschedule",reschedulePolicy);
            driverTools.click(CLOSE_POLICY);
        }
        return refundPolicy && reschedulePolicy;
    }

    private boolean verifyPolicy(String policyType, boolean allowedPolicy){
        Locator tab = null;
        if(policyType.equals("refund")){
            tab = REFUND_TAB;
        } else if(policyType.equals("reschedule")) {
            tab = RESCHEDULE_TAB;
        } else{
            throw new NotImplementedException("Not implemented policy");
        }
        driverTools.click(tab);
        return driverTools.isElementDisplayed(POLICY_INFO);
    }
    @Override
    public boolean isPriceBreakdownCorrect(int adult, int child, int infant) {

        logger.log("Verifying price detail");

        boolean adultBreakdown = isBreakdownDisplayed("adult",adult);
        boolean childBreakdown = isBreakdownDisplayed("child",child);
        boolean infantBreakdown = isBreakdownDisplayed("infant",infant);
        boolean priceCorrect = isPriceSumCorrect();

        return adultBreakdown && childBreakdown && infantBreakdown && priceCorrect;
    }

    private boolean isBreakdownDisplayed(String type, int amount){
        boolean priceBreakdown;
        if(amount>0){
            return !(getPrice(type,amount) == null);
        }else{
            return true;
        }
    }

    @Override
    public boolean isPriceSumCorrect(){
        driverTools.click(PRICE_DETAIL);
        List<WebElement> prices = driverTools.elements(PRICE_BREAKDOWN).orElseThrow();

        ArrayList<Double> arrayPrice = new ArrayList<>();
        Double nextAfterFree = null;

        for (int i = 0; i < prices.size(); i++) {
            String text = driverTools.getText(prices.get(i));

            if (text.equalsIgnoreCase("free") || text.equalsIgnoreCase("gratis")) {
                if (i + 1 < prices.size()) {
                    String nextText = driverTools.getText(prices.get(i + 1));
                    try {
                        nextAfterFree = convertPrice(nextText);
                    } catch (NumberFormatException e) {
                        logger.log("Next after free is not numeric: " + nextText);
                    }
                }
                break;
            }

            try {
                double price = convertPrice(text);
                arrayPrice.add(price);
            } catch (NumberFormatException ignored) {
                logger.log("arrayprice -> " + arrayPrice);
            }
        }

        double sum = arrayPrice.stream().mapToDouble(Double::doubleValue).sum();
        logger.log("arrayPrice: " + arrayPrice);
        logger.log("sum: " + sum);
        logger.log("Total amt: " + nextAfterFree);

        Assertion.assertThat("Total price calculation should be correct", nextAfterFree, is(sum));
        return true;
    }

    @Override
    public String getPrice(String type, int amount){
        Optional<WebElement> priceElement = driverTools.waitForElementToAppear(getPriceBreakdownLocator(type,amount));
        boolean priceAvailable = priceElement.isPresent();
        if(priceAvailable){
            String price = driverTools.getText(priceElement.orElseThrow());
            logger.log( type + " price:  " + price);
            return price;
        }else{
            return null;
        }
    }

    private Locator getPriceBreakdownLocator(String type, int amount){
        String typeId,typeEng;
        switch (type.toLowerCase()){
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
            case "harga gledek":{
                return new Locator("PRICE AND POLICY " + type.toUpperCase() +" PRICE: ", AppiumBy.xpath("//*[contains(@resource-id,'rv_price_detail')]//*[contains(@resource-id,'tv_title') and contains(@text,'Harga Gledek') ]//following-sibling::*[contains(@resource-id,'tv_price')]"));
            }
            default: throw new NotImplementedException("Unimplemented passenger type!");
        }
       return new Locator("Price breakdown",AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'"+typeId+" (x"+amount+")') or contains(@text,'"+typeEng+" (x"+amount+")'))]//following-sibling::*[contains(@resource-id,'tv_price')]"));
    }
    @Override
    public String getDepartureDate() {
        return driverTools.getText(DEPARTURE_DATE).orElseThrow();
    }

    @Override
    public String getDepartureAirport() {
        return driverTools.getText(DEPARTURE_AIRPORT).orElseThrow();
    }

    @Override
    public String getArrivalDate() {
        return driverTools.getText(ARRIVAL_DATE).orElseThrow();
    }

    @Override
    public String getArrivalAirport() {
        return driverTools.getText(ARRIVAL_AIRPORT).orElseThrow();
    }

    @Override
    public void selectTicket() {
        driverTools.click(SELECT_TICKET);
    }

    @Override
    public boolean isLowestPriceGuaranteePagePresent() {
        driverTools.click(LPG_BANNER);
        driverTools.waitForElementToAppear(LPG_CLOSE);
        driverTools.switchToWebView();
        boolean lpg = driverTools.waitForElementToAppear(LPG_WEBVIEW,10).isPresent();
        driverTools.switchToNative();
        driverTools.click(LPG_CLOSE);
        return lpg;
    }

    @Override
    public void filterTransitType(String type) {
        WebElement e = driverTools.swipeToElement(getTransitTypeLocator(type),FILTER_SCROLLABLE,Swipe.UP,5).orElseThrow();
        driverTools.click(e);
    }

    private Locator getTransitTypeLocator(String type){
        switch (type.toLowerCase()){
            case "langsung":
            case "direct": {
                return TRANSIT_FILTER_DIRECT;
            }
            case "transit":{
                return TRANSIT_FILTER_TRANSIT;
            }
            case "1 transit":{
                return  TRANSIT_FILTER_1_TRANSIT;
            }
            case "2 transit":{
                return  TRANSIT_FILTER_2_TRANSIT;
            }
            default: throw new NotImplementedException("Unimplemented transit type!");
        }
    }

    private Locator getOtherFilterTypeLocator(String type){
        return new Locator(type + " Filter", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and contains(@text,'"+type+"')]"));
    }

    @Override
    public void filterChips(String type) {
        driverTools.waitForElementToAppear(FLIGHT_CARD_1);
        driverTools.waitForElementToPresence(getOtherFilterTypeLocator(type));
        driverTools.swipeToElement(getOtherFilterTypeLocator(type),FILTER_CHIPS_BODY,Swipe.LEFT,6).orElseThrow();
        driverTools.click(getOtherFilterTypeLocator(type));
    }

    @Override
    public boolean isFreeTransfer(){
        return driverTools.isElementDisplayed(FREE_TRANSFER);
    }

    @Override
    public boolean isTest(){
        return driverTools.isElementDisplayed(TEST);
    }

    @Override
    public boolean isFreeTransferDetail() {
        if (driverTools.isElementDisplayed(POLICY_SCROLLABLE)) {
            driverTools.swipeUp(POLICY_SCROLLABLE);
        }
        return driverTools.isElementDisplayed(FREE_TRANSFER_DETAIL);
    }

    @Override
    public boolean checkDepartureDetailsPRP(){
        driverTools.waitForElementToAppear(SEE_DEPARTURE_DETAILS);
        driverTools.click(SEE_DEPARTURE_DETAILS);
        boolean isAirportTransferDetail = driverTools.waitForElementToAppear(AIRPORT_TRANSFER_DETAIL).isPresent();
        Assertion.assertThat("Airport Transfer Detail is present on departure flight ", isAirportTransferDetail, is(true));
        boolean isAntigenTestDetail = driverTools.waitForElementToAppear(ANTIGEN_TEST_DETAIL).isPresent();
        Assertion.assertThat("Antigen Test Detail is present on departure flight ", isAntigenTestDetail, is(true));
        driverTools.click(CLOSE_BTN_PRP);
        return isAirportTransferDetail && isAntigenTestDetail;
    }

    @Override
    public boolean checkReturnDetailsPRP(){
        driverTools.swipeToElement(SELECT_TICKECT, Swipe.UP, 5);
        driverTools.waitForElementToAppear(SEE_DEPARTURE_DETAILS);
        driverTools.click(SEE_DEPARTURE_DETAILS);
        boolean isAirportTransferDetail = driverTools.waitForElementToAppear(AIRPORT_TRANSFER_DETAIL).isPresent();
        Assertion.assertThat("Airport Transfer Detail is present on return flight ", isAirportTransferDetail, is(true));
        boolean isAntigenTestDetail = driverTools.waitForElementToAppear(ANTIGEN_TEST_DETAIL).isPresent();
        Assertion.assertThat("Antigen Test Detail is present on return flight ", isAntigenTestDetail, is(true));
        driverTools.click(CLOSE_BTN_PRP);
        return isAirportTransferDetail && isAntigenTestDetail;
    }

    @Override
    public boolean isFreeTransferDetailOneWay(){
        return driverTools.isElementDisplayed(FREE_TRANSFER_DETAIL_SRP);
    }

    @Override
    public boolean isTestDetail(){
        if (driverTools.isElementDisplayed(POLICY_SCROLLABLE)) {
            driverTools.swipeUp(POLICY_SCROLLABLE);
        }
        return driverTools.isElementDisplayed(TEST_DETAIL);
    }

    @Override
    public boolean isTestDetailOneWay(){
        return driverTools.isElementDisplayed(TEST_DETAIL_SRP);
    }

    @Override
    public boolean verifyLoyaltyBanner(String loyaltyType){
        WebElement bannerTitle = driverTools.waitForElementToAppear(LOYALTY_CARD_TITLE).orElseThrow();
        return driverTools.getText(bannerTitle).contains(loyaltyType);
    }

    @Override
    public boolean isCorporateDealsBannerPresent() {
        driverTools.swipeToElement(CORPORATE_DEALS_BANNER,Swipe.DOWN,8);
        return driverTools.waitForElementToPresence(CORPORATE_DEALS_BANNER,15).isPresent();
    }

    @Override
    public boolean gotoCorporateDealsBenefitCard() {
        boolean benefitCard = driverTools.swipeToElement(CORPORATE_DEALS_BENEFIT_CARD, Swipe.UP, 2).isPresent();
        if (!benefitCard) driverTools.swipeToElement(CORPORATE_DEALS_BENEFIT_CARD, Swipe.DOWN, 2);
        return benefitCard;
    }

    @Override
    public boolean isTransitInfoPresentOnSrpCardDetail() {
        driverTools.waitForElementToPresence(HIDE_DETAILS, 10);
        boolean present = driverTools.isElementDisplayed(TRANSIT_INFO_SRP_CARD_DETAIL);
        logger.log("Transit info present on expanded SRP card detail: " + present);
        return present;
    }

    @Override
    public boolean verifyDiscountTag(){
        clickSeeDetailSrp(2);
        driverTools.waitForElementToPresence(SHOW_DETAILS).ifPresent(driverTools::click);
        boolean res = driverTools.waitForElementToAppear(DISCOUNT_TAG).isPresent();
        return res;
    }

    @Override
    public double verifyDiscountPrice(String discType){
        String discountPrice = driverTools.getText(getDiscountPrice(discType)).orElseThrow();
        double price = convertPrice(discountPrice);
        logger.log("Discount price: "+price);
        return price;
    }

    private Locator getDiscountPrice(String loyaltyType){
        return new Locator("discount Locator",AppiumBy.xpath("//*[contains(@text,'Elite') and contains(@text,'"+loyaltyType+"') and (contains(@text,'Discount') or contains(@text,'Diskon'))]/following-sibling::*"));
    }

    @Override
    public boolean isWhatYouGetCorrect(String label) {
        boolean res = driverTools.swipeToElement(getWhatYouGetLocator(label),Swipe.UP,3).isPresent();
        driverTools.click(CLOSE_DETAIL);
        return res;
    }

    @Override
    public boolean isWhatYouGetCorrectSRP(String label) {
        return driverTools.swipeToElement(getWhatYouGetLocatorSRP(label),Swipe.UP,3).isPresent();
    }

    private Locator getWhatYouGetLocatorSRP(String whatYouGet) {
        switch (whatYouGet.toLowerCase()) {
            case "diamond","platinum" -> {
                return new Locator("WHAT YOU GET PDP" + whatYouGet.toUpperCase(), AppiumBy.xpath("//*[ contains(@resource-id,'tv_benefit_title') and contains(@text,'Benefit') and contains(@text,'Elite') and contains(@text,'"+whatYouGet+"')]"));
            }
            case "harga gledek" -> {
                return new Locator("WHAT YOU GET PDP" + whatYouGet.toUpperCase(), AppiumBy.xpath("//*[ contains(@resource-id,'tv_benefit_title') and contains(@text,'"+whatYouGet+"')]"));
            }
            default -> throw new NotImplementedException("Unimplemented what you get type!");
        }
    }

    private Locator getWhatYouGetLocator(String whatYouGet) {
        switch (whatYouGet.toLowerCase()) {
            case "diamond", "platinum" -> {
                return new Locator("WHAT YOU GET PDP" + whatYouGet.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id, 'tv_flight_review_price_breakdown_item_label') and contains(@text, '"+whatYouGet+"')]"));
            }
            case "harga gledek" -> {
                return new Locator("WHAT YOU GET " + whatYouGet.toUpperCase(), AppiumBy.xpath("//*[contains(@text,'What You') or contains(@text,'Yang akan')]//following-sibling::android.view.ViewGroup//*[contains(@resource-id,'tv_title') and contains(@text,'"+whatYouGet+"')]"));
            }
            default -> throw new NotImplementedException("Unimplemented what you get type!");
        }
    }

    @Override
    public void backToFlightPage(){
        driverTools.click(BACK_BTN);
    }

    @Override
    public String getRouteText() {
        return driverTools.getText(ROUTE_TEXT).orElseThrow();
    }

    @Override
    public boolean isOnSearchResultPage(){
        WebElement flightChevron = driverTools.waitForElementToAppear(FLIGHT_CARD_1,15).orElseThrow();

        boolean isQuickFilterChips = driverTools.isElementDisplayed(QUICK_FILTER_CHIPS);
        boolean isFlightChevron = driverTools.isElementDisplayed(flightChevron);
        boolean isFilterIcon = driverTools.isElementDisplayed(FILTER_ICON);
        if(!isFilterIcon)
            isFilterIcon = driverTools.isElementDisplayed(FILTER_OLD);
        logger.log("quick filter : " + isQuickFilterChips);
        logger.log("flight chevron : " + isFlightChevron);
        logger.log("filter icon : " + isFilterIcon);
        return isQuickFilterChips && isFlightChevron && isFilterIcon;
    }

    // TODO: verify on-device — best-guess locator for the SRP error/retry bottomsheet.
    private static final Locator ERROR_BOTTOMSHEET = new Locator("ERROR_BOTTOMSHEET",
            AppiumBy.xpath("//*[(contains(@resource-id,'bottom_sheet') or contains(@resource-id,'tds_bottom_sheet') or contains(@resource-id,'error')) and .//*[contains(@text,'Terjadi kesalahan') or contains(@text,'Something went wrong') or contains(@text,'Coba lagi') or contains(@text,'Try again') or contains(@text,'Muat ulang') or contains(@text,'Reload')]]"));

    @Override
    public boolean isErrorBottomSheetDisplayed() {
        boolean displayed = driverTools.waitForElementToAppear(ERROR_BOTTOMSHEET, 3).isPresent();
        logger.log("error bottomsheet displayed : " + displayed);
        return displayed;
    }

    private Locator getStrikePriceLocator(int index){
        return new Locator("STRIKE PRICE",AppiumBy.xpath("(//*[contains(@resource-id,'tv_price_strike_through') or contains(@resource-id,'tv_price_strikethrough') or contains(@resource-id,'tv_slash_price') or contains(@resource-id,'tv_price_slash')])["+index+"] | (//*[contains(@resource-id,'cl_flight_price')]//*[contains(@resource-id,'tv_price') and (contains(@resource-id,'strike') or contains(@resource-id,'slash'))])["+index+"]"));
    }

    private Locator getPriceLocator(int index){
        return new Locator("DISCOUNTED PRICE",AppiumBy.xpath("(//*[contains(@resource-id,'cl_flight_price')]//*[contains(@resource-id,'tv_price') and not(contains(@resource-id,'tv_price_strike_through'))])["+index+"]"));
    }

    @Override
    public boolean isAirlineWithDiscountedPrice(){
        driverTools.waitForElementToAppear(FLIGHT_CARD_1,15).orElseThrow();

        Locator discountPriceLabel = new Locator("DISCOUNT_PRICE_LABEL", AppiumBy.xpath("(//*[contains(@resource-id,'container') and @clickable='true' and .//*[contains(@resource-id,'cl_flight_item')]])[1]//*[contains(@text,'Discount Price') or contains(@text,'Harga Diskon')]"));
        if (driverTools.waitForElementToAppear(discountPriceLabel, 5).isPresent()) {
            logger.log("Discounted price label is displayed on first flight card");
            return true;
        }

        WebElement strikePriceElement = driverTools.waitForElementToAppear(getStrikePriceLocator(1), 10).orElseThrow();
        WebElement discountedPriceElement = driverTools.waitForElementToAppear(getPriceLocator(1), 10).orElseThrow();

        double strikePrice = convertPrice(driverTools.getText(strikePriceElement));
        double discountedPrice = convertPrice(driverTools.getText(discountedPriceElement));

        logger.log("strike price: " + strikePrice);
        logger.log("discounted price: " + discountedPrice);

        return strikePrice-discountedPrice > 0;
    }

    @Override
    public boolean isDiscountTagPresent(){
        return driverTools.isElementDisplayed(DISCOUNT_TAG_PRICE_AND_POLICY);
    }

    @Override
    public String getDiscountPrice(){
        WebElement discountPriceElement = driverTools.waitForElementToAppear(DISCOUNT_PRICE_PRICE_AND_POLICY).orElseThrow();
        return driverTools.getText(discountPriceElement);
    }

    @Override
    public String getSpecialCampaignSavingsPrice(){
        WebElement savingsElement = driverTools.waitForElementToAppear(SPECIAL_CAMPAIGN_SAVINGS_PRICE).orElseThrow();
        String text = driverTools.getText(savingsElement);
        logger.log("[getSpecialCampaignSavingsPrice] text: " + text);
        return text;
    }

    @Override
    public boolean isAdjustmentPresentSearch(){
        boolean discountedPrice = driverTools.isElementDisplayed(getStrikePriceLocator(1));
        logger.log("Discounted price: "+discountedPrice);
        boolean specialDiscount = driverTools.isElementDisplayed(SPECIAL_DISCOUNT_LOGO);
        logger.log("Special discount: "+specialDiscount);
        boolean discountTag = driverTools.isElementDisplayed(DISCOUNT_TAG);
        logger.log("Discount tag: "+specialDiscount);
        boolean loyaltyBanner = driverTools.isElementDisplayed(LOYALTY_CARD_TITLE);
        logger.log("Loyalty Banner: "+specialDiscount);


        return discountedPrice && specialDiscount && discountTag && loyaltyBanner;
    }

    @Override
    public boolean isAdjustmentPresentDetail(){
        boolean hargaGledek = driverTools.isElementDisplayed(HARGA_GLEDEK_DISCOUNT);
        logger.log("Harga gledek discount: "+hargaGledek);
        boolean discountPrice = driverTools.isElementDisplayed(DISCOUNT_PRICE_PRICE_AND_POLICY);
        logger.log("Discount price: "+discountPrice);
        boolean loyaltyDiscount = driverTools.isElementDisplayed(LOYALTY_DISCOUNT);
        logger.log("Loyalty discount: "+discountPrice);
        driverTools.swipeUp();
        boolean whatYouGetGledek = driverTools.isElementDisplayed(HARGA_GLEDEK_WHAT_YOU_GET);
        logger.log("What you get Harga Gledek "+discountPrice);
        boolean whatYouGetLoyalty = driverTools.isElementDisplayed(LOYALTY_WHAT_YOU_GET);
        logger.log("What you get loyalty "+discountPrice);

        return hargaGledek && discountPrice && loyaltyDiscount && whatYouGetLoyalty && whatYouGetGledek;
    }

    @Override
    public boolean areFlightsAvailable(){
        driverTools.waitForElementToAppear(CHANGE_SEARCH_BTN_SRP);
        return !driverTools.isElementDisplayed(CHANGE_SEARCH_BTN_SRP);
    }

    @Override
    public boolean isTotalPriceWithoutDiscount(int adult){
        String price = driverTools.getText(TOTAL_PRICE).orElseThrow();
        double totalPrice = convertPrice(price);
        logger.log("Total Price: " + totalPrice);
        String priceAdult = driverTools.getText(getPriceBreakdownLocator("adult",adult)).orElseThrow();
        double totalPriceAdult = convertPrice(priceAdult);
        logger.log("Adult Price: " + totalPriceAdult);
        return totalPrice == totalPriceAdult;
    }

    private Locator getPriceOnSrpForSpecialCampaignLocator(int index){
        return new Locator("PRICE SRP",AppiumBy.xpath("(//*[contains(@text,'/pax')])["+index+"]//preceding-sibling::*[contains(@resource-id,'tv_price')]"));
    }

    private Locator getDiscountPriceOnSrpForSpecialCampaignLocator(int index){
        return new Locator("DISCOUNT PRICE SRP",AppiumBy.xpath("(//*[contains(@resource-id,'tv_discount')])["+index+"]"));
    }

    @Override
    public boolean isDiscountPriceForSpecialCampaignCorrectOnSRP(String date){
        driverTools.waitForElementToAppear(FLIGHT_CARD_1,20).orElseThrow();
        driverTools.waitForElementToAppear(REFUND_POPUP,20);

        WebElement priceElement = driverTools.waitForElementToAppear(getPriceOnSrpForSpecialCampaignLocator(1),5).orElseThrow();
        double price = convertPrice(driverTools.getText(priceElement));

        WebElement discountElement = driverTools.waitForElementToAppear(getDiscountPriceOnSrpForSpecialCampaignLocator(1),5).orElseThrow();
        double discount = convertPrice(driverTools.getText(discountElement));

        WebElement priceAfterDiscountElement = driverTools.waitForElementToAppear(PRICE_ON_FLIGHT_PRICE_DATE_PICKER_SRP,5).orElseThrow();
        double priceAfterDiscountExpected = convertPrice(driverTools.getText(priceAfterDiscountElement));

        //discount is in -ve so i have added it -> price + (-discount) = price - discount

        double priceAfterDiscountActual = price + discount;

        logger.log("price: " + price);
        logger.log("discounted price: " + discount);
        logger.log("discounted price on price date picker: " + priceAfterDiscountExpected);
        logger.log("discounted price after calculation: " + priceAfterDiscountActual);

        return priceAfterDiscountActual == priceAfterDiscountExpected;
    }

    @Override
    public boolean checkLabelOnCardSRP(String labelExpected){
        return driverTools.isElementDisplayed(getLabelLocatorOnCardSRP(1,labelExpected));
    }

    private Locator getLabelLocatorOnCardSRP(int index,String label){
        switch (label.toLowerCase()) {
            case "harga gledek" -> {
                return new Locator("LABEL ON CARD SRP " + label.toUpperCase(),AppiumBy.xpath("(//*[contains(@text, '" + label + "') and @class='android.widget.TextView'])[" + index + "]"));
            }
            case "free reschedule" -> {
                return new Locator("LABEL ON CARD SRP " + label.toUpperCase(),AppiumBy.xpath("(//*[contains(@resource-id,'label')]//*[contains(@text,'"+label+"')])[1]"));
            }
            case "discount" -> {
                return new Locator("DISCOUNT PRICE SRP " + label.toUpperCase(),AppiumBy.xpath("(//*[contains(@resource-id,'tv_discount')])["+index+"]"));
            }
            case "round-trip deals" -> {
                return new Locator("LABEL ON CARD SRP " + label.toUpperCase(), AppiumBy.xpath("(//*[contains(@text,'"+label+"')])["+index+"]"));
            }
            default -> throw new NotImplementedException("Unimplemented label type!");
        }
    }

    private Locator getTagLocatorOnPriceAndPolicyTab(String tag){
        switch (tag.toLowerCase()) {
            case "discount", "diskon" -> {
                return new Locator("DISCOUNT_TAG_PRICE_AND_POLICY", AppiumBy.xpath("//*[contains(@resource-id,'rv_price_detail')]//*[contains(@resource-id,'tv_title') and ( contains(@text,'Discount') or contains(@text,'Diskon') )]"));
            }
            case "harga gledek" -> {
                return new Locator("TAG PRICE AND POLICY " + tag.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'rv_price_detail')]//*[contains(@resource-id,'tv_title') and contains(@text,'Harga Gledek')]"));
            }
            default -> throw new NotImplementedException("Unimplemented tag type!");
        }
    }

    @Override
    public boolean checkTagPresentOnPriceAndPolicyTab(String expectedTag){
        WebElement tagElement = driverTools.waitForElementToAppear(getTagLocatorOnPriceAndPolicyTab(expectedTag)).orElseThrow();
        return tagElement.isDisplayed();
    }

    @Override
    public boolean isIconDisplayedOnTheRightOfTotalPayment(){
        return driverTools.isElementDisplayed(ICON_ON_THE_RIGHT_OF_TOTAL_PAYMENT);
    }

    @Override
    public String getPriceBasedOnLabel(String label){
        return getPrice(label,1);
    }

    @Override
    public boolean isRegisterBannerDisappear() {
        return !driverTools.isElementDisplayed(JOIN_NOW_SRP);
    }

    private Locator get100pRefundAttribute(int index){
        return new Locator("100P_REFUND_ATTRIBUTE_LOCATOR",AppiumBy.xpath("//*[@selected=\"true\" and contains(@resource-id,'tds_chips_text') and contains(@text,'100% Refund')]"));
    }

    @Override
    public boolean is100pRefundDisplayed(){
        driverTools.waitForElementToAppear(get100pRefundAttribute(1),15);
        boolean is100pRefundDisplayedFor1Card = driverTools.isElementDisplayed(get100pRefundAttribute(1));
        logger.log("is 100p refund displayed for 1st card : " + is100pRefundDisplayedFor1Card);
        return  is100pRefundDisplayedFor1Card;
    }

    @Override
    public boolean check100pRefundOnPdp(){
        boolean is100pRefundDisplayedOnPdp = driverTools.isElementDisplayed(LABEL_100_P_REFUND);
        logger.log("is 100p refund displayed on pdp page : " + is100pRefundDisplayedOnPdp);
        return  is100pRefundDisplayedOnPdp;
    }

    @Override
    public boolean isMegaphoneIconDisplayed(){
        WebElement megaPhoneIcone = driverTools.swipeToElement(MEGAPHONE_ICON_SRP,Swipe.UP,15).orElseThrow();
        boolean isMegaPhoneIconDisplayed = driverTools.isElementDisplayed(megaPhoneIcone);
        logger.log("mega phone icon displayed : " + isMegaPhoneIconDisplayed);
        return isMegaPhoneIconDisplayed;
    }

    @Override
    public void clickMegaphoneIcon(){
        driverTools.swipeUp();
        driverTools.click(MEGAPHONE_ICON_SRP);
    }

    @Override
    public void clickLoginBanner(){
        driverTools.swipeToElement(JOIN_NOW_SRP, Swipe.UP, 15);
        driverTools.swipeUp();
        driverTools.swipeToElement(JOIN_NOW_SRP, Swipe.DOWN, 1);
        driverTools.click(JOIN_NOW_SRP);
    }

    @Override
    public List<String> getPrices(){
        List<String> prices = driverTools.getTexts(PRICES);
        logger.log("prices: "+prices);
        Assertion.assertThat("Prices exist", !prices.isEmpty(),is(true));
        return prices;
    }

    @Override
    public void clickSeeDetailSrp(int index){
        // For the return leg (index == 2) the chevron sits well below the fold on dense
        // international RT cards. Two staged scrolls handle two distinct problems:
        //
        //   1. swipeToElement(UP) - render-aware, short-circuits if already visible. Needed
        //      because the chevron may not yet be in the accessibility tree (lazy-rendered
        //      RecyclerView), in which case calling swipeElementToMidScreen directly throws
        //      because it does element(locator).orElseThrow() upfront.
        //
        //   2. swipeElementToMidScreen - now that the chevron is rendered, centre it in the
        //      40-60% vertical band so the floating discount/cashback badge in the viewport's
        //      lower band doesn't intercept the coordinate tap. Same pattern used by
        //      RoomListPage / SearchResultPage / FlightProductReviewPage for bottom overlays.
        //
        // The previous swipeUp(0.5) "worked" only because the blind half-screen swipe forced
        // the RecyclerView to materialise new items, but it overshoots so the chevron lands
        // under the floating badge and the tap hits the badge instead.
        if (index == 2){
            logger.log("Bringing flight detail expand into view, then centring to avoid floating badge");
            driverTools.swipeToElement(FLIGHT_DETAIL_EXPAND, Swipe.UP, 8);
            driverTools.swipeElementToMidScreen(FLIGHT_DETAIL_EXPAND);
        }

        // Re-resolve after the scrolls - the element reference can stale across swipe gestures.
        WebElement flightDetailExpand = driverTools.waitForElementToPresence(FLIGHT_DETAIL_EXPAND).orElseThrow();
        driverTools.clickElementByCoordinate(flightDetailExpand);
    }

    @Override
    public void clickDetailPRP(){
        driverTools.swipeToElement(CLICK_DETAILS, Swipe.UP,5).orElseThrow();
        driverTools.click(CLICK_DETAILS);
    }

    private Locator getNudgeLocator(String enText,String idText){
        return new Locator("NUDGE_LOCATOR_" + enText.toUpperCase(),AppiumBy.xpath("//*[contains(@resource-id,'tv_flight_nudges_best_experience') and (contains(@text,'"+enText+"') or contains(@text,'"+idText+"'))]"));
    }

    @Override
    public boolean checkNudge(String enText,String idText){
        driverTools.waitForElementToAppear(getNudgeLocator(enText,idText),15);
        WebElement nudgeElement = driverTools.swipeToElement(getNudgeLocator(enText,idText),Swipe.UP,8).orElseThrow();
        return driverTools.isElementDisplayed(nudgeElement);
    }

    @Override
    public void clickFlightWithNudge(String enText,String idText){
        WebElement nudgeElement = driverTools.swipeToElement(getNudgeLocator(enText,idText),Swipe.UP,4).orElseThrow();
        driverTools.click(nudgeElement);
    }

    @Override
    public boolean isFloatingIconDisplayed(){
        if (driverTools.waitForElementToAppear(FLOATING_ICON_SRP, 5).isPresent()
                && driverTools.isElementDisplayed(FLOATING_ICON_SRP)) {
            return true;
        }
        driverTools.waitForElementToAppear(PROMO_INFINITY_BANNER, 15);
        return driverTools.isElementDisplayed(PROMO_INFINITY_BANNER)
                || driverTools.isElementDisplayed(APPLY_PROMO_INFINITY);
    }

    @Override
    public void clickFloatingIcon(){
        if (driverTools.isElementDisplayed(FLOATING_ICON_SRP)) {
            driverTools.click(FLOATING_ICON_SRP);
            return;
        }
        driverTools.waitForElementToAppear(APPLY_PROMO_INFINITY, 10);
        driverTools.click(APPLY_PROMO_INFINITY);
    }

    @Override
    public void backFromFloatingIcon(){
        // Infinity promo Apply stays on SRP (Apply → Cancel); no navigation to unwind.
        if (driverTools.isElementDisplayed(CANCEL_PROMO_INFINITY)
                || driverTools.isElementDisplayed(PROMO_APPLIED_TEXT)) {
            return;
        }
        driverTools.back();
    }

    @Override
    public void waitForFloatingIconToDisappeared(){
        if (driverTools.isElementPresent(FLOATING_ICON_SRP) && driverTools.isElementDisplayed(FLOATING_ICON_SRP)) {
            driverTools.waitForElementToDisappear(FLOATING_ICON_SRP);
            return;
        }
        // After Apply on the sticky promo banner, Cancel / applied copy appears on SRP.
        driverTools.waitForElementToAppear(CANCEL_PROMO_INFINITY, 15);
    }

    @Override
    public boolean isPromoInfinityEntryHandled() {
        return driverTools.waitForElementToAppear(CANCEL_PROMO_INFINITY, 5).isPresent()
                || driverTools.isElementDisplayed(PROMO_APPLIED_TEXT);
    }

    @Override
    public void openTransitFilter(){
        driverTools.click(FLIGHT_TRANSIT_FILTER);
    }

    @Override
    public void openSortFilter() {
        driverTools.click(FLIGHT_SORT_FILTER);
    }

    @Override
    public void sortBy(String type) {
        WebElement e = driverTools.swipeToElement(getSortTypeLocator(type), FILTER_SCROLLABLE, Swipe.UP, 5).orElseThrow();
        driverTools.click(e);
    }

    private Locator getSortTypeLocator(String type) {
        switch (type.toLowerCase(Locale.ROOT)) {
            case "recommended":
            case "rekomendasi":
                return sortRowLocator("Recommended", "Rekomendasi");
            case "lowest price":
            case "harga terendah":
                return sortRowLocator("Lowest price", "Harga terendah");
            case "earliest departure":
            case "keberangkatan paling awal":
                return sortRowLocator("Earliest departure", "Keberangkatan paling awal");
            case "latest departure":
            case "keberangkatan paling akhir":
                return sortRowLocator("Latest departure", "Keberangkatan paling akhir");
            case "earliest arrival":
            case "kedatangan paling awal":
                return sortRowLocator("Earliest arrival", "Kedatangan paling awal");
            case "latest arrival":
            case "kedatangan paling akhir":
                return sortRowLocator("Latest arrival", "Kedatangan paling akhir");
            case "shortest duration":
            case "durasi terpendek":
                return sortRowLocator("Shortest duration", "Durasi terpendek");
            default:
                throw new NotImplementedException("Unimplemented sort type: " + type);
        }
    }

    private Locator sortRowLocator(String en, String id) {
        String name = "SORT_BY_" + en.toUpperCase().replace(" ", "_");
        return new Locator(name, AppiumBy.xpath(
                "//*[contains(@resource-id,'tv_value') and (contains(@text,'" + en + "') or contains(@text,'" + id + "'))]"
        ));
    }

    @Override
    public void verifySortSelected(String type) {
        openSortFilter();
        Locator selectedIndicator = getSortSelectedIndicatorLocator(type);
        List<WebElement> views = driverTools.elements(selectedIndicator, 5).orElse(Collections.emptyList());
        int count = views.size();
        logger.log("Sort '" + type + "' selected-indicator view count: " + count + " (expected 2)");
        Assertion.assertThat(
                "Sort option '" + type + "' should be selected after sortBy() (selected indicator view count expected to be 2)",
                count,
                is(2)
        );
        driverTools.click(CLOSE_BTN_SORT_FILTER);
    }

    @Override
    public void verifyResetFilterDisabled() {
        WebElement resetBtn = driverTools.waitForElementToAppear(RESET_FILTER_BUTTON, 15).orElseThrow();
        boolean disabled = !driverTools.isElementEnabled(resetBtn);
        logger.log("Reset filter button disabled: " + disabled);
        Assertion.assertThat("Reset filter button should be disabled by default on advanced filter bottom sheet", disabled, is(true));
    }

    @Override
    public void verifyResetFilterEnabled() {
        boolean enabled = driverTools.isElementEnabled(RESET_FILTER_BUTTON);
        logger.log("Reset filter button enabled: " + enabled);
        Assertion.assertThat("Reset filter button should be enabled after selecting a sort option", enabled, is(true));
    }

    @Override
    public void verifyNoSortByGreenDot() {
        boolean badgeDisplayed = driverTools.isElementDisplayed(SORT_BY_FILTER_BADGE);
        logger.log("Sort-by green dot badge displayed: " + badgeDisplayed);
        Assertion.assertThat("Green dot badge should not be displayed before any sort filter is applied", badgeDisplayed, is(false));
    }

    @Override
    public void openAdvancedFilterSortTab() {
        driverTools.waitForElementToAppear(RESET_FILTER_BUTTON, 15);
        if (!driverTools.isElementDisplayed(SORT_BY_SECTION_HEADER)) {
            driverTools.click(SORT_TAB_IN_ADVANCED_FILTER);
        }
        driverTools.waitForElementToAppear(SORT_BY_SECTION_HEADER, 10).orElseThrow();
    }

    @Override
    public void selectSortOptionInAdvancedFilter(String type) {
        Locator sortOption = getSortTypeLocator(type);
        if (driverTools.isElementDisplayed(sortOption)) {
            driverTools.click(sortOption);
            return;
        }
        Optional<WebElement> option = driverTools.swipeToElement(sortOption, FILTER_SCROLLABLE, Swipe.UP, 3);
        if (option.isEmpty() && driverTools.isElementDisplayed(SORT_SEE_MORE)) {
            driverTools.click(SORT_SEE_MORE);
            driverTools.waitForElementToAppear(sortOption, 5);
            option = driverTools.swipeToElement(sortOption, FILTER_SCROLLABLE, Swipe.UP, 5);
        }
        driverTools.click(option.orElseThrow(() -> new NoSuchElementException("Sort option not found in advanced filter: " + type)));
    }

    @Override
    public void verifySortByFilterSelectedIndicator(String type) {
        verifyResetFilterEnabled();
        Locator selectedIndicator = getSortSelectedIndicatorLocator(type);
        List<WebElement> views = driverTools.elements(selectedIndicator, 3).orElse(Collections.emptyList());
        Assertion.assertThat(
                "Sort option '" + type + "' should be selected in advanced filter",
                views.size(),
                is(2)
        );
    }

    @Override
    public void verifySortByFilterReset(String type) {
        boolean badgeDisplayed = driverTools.isElementDisplayed(SORT_BY_FILTER_BADGE);
        logger.log("Sort-by green dot badge displayed after reset: " + badgeDisplayed);
        Assertion.assertThat("Green dot badge should not be displayed after resetting advanced filter", badgeDisplayed, is(false));
        verifyResetFilterDisabled();
        verifyNoSortByGreenDot();
        Locator selectedIndicator = getSortSelectedIndicatorLocator(type);
        List<WebElement> views = driverTools.elements(selectedIndicator, 3).orElse(Collections.emptyList());
        Assertion.assertThat(
                "Sort option '" + type + "' should not remain selected after reset",
                views.size(),
                is(1)
        );
    }

    private Locator getSortSelectedIndicatorLocator(String type) {
        switch (type.toLowerCase(Locale.ROOT)) {
            case "recommended":
            case "rekomendasi":
                return sortSelectedIndicatorLocator("Recommended", "Rekomendasi");
            case "lowest price":
            case "harga terendah":
                return sortSelectedIndicatorLocator("Lowest price", "Harga terendah");
            case "earliest departure":
            case "keberangkatan paling awal":
                return sortSelectedIndicatorLocator("Earliest departure", "Keberangkatan paling awal");
            case "latest departure":
            case "keberangkatan paling akhir":
                return sortSelectedIndicatorLocator("Latest departure", "Keberangkatan paling akhir");
            case "earliest arrival":
            case "kedatangan paling awal":
                return sortSelectedIndicatorLocator("Earliest arrival", "Kedatangan paling awal");
            case "latest arrival":
            case "kedatangan paling akhir":
                return sortSelectedIndicatorLocator("Latest arrival", "Kedatangan paling akhir");
            case "shortest duration":
            case "durasi terpendek":
                return sortSelectedIndicatorLocator("Shortest duration", "Durasi terpendek");
            default:
                throw new NotImplementedException("Unimplemented sort type: " + type);
        }
    }

    private Locator sortSelectedIndicatorLocator(String en, String id) {
        String name = "SORT_SELECTED_" + en.toUpperCase().replace(" ", "_");
        return new Locator(name, AppiumBy.xpath(
                "//*[contains(@resource-id,'tv_value') and (contains(@text,'" + en + "') or contains(@text,'" + id
                        + "'))]/following-sibling::*//android.view.View"
        ));
    }

    @Override
    public double getPriceOnDatePicker() {
        WebElement element = driverTools.waitForElementToAppear(PRICE_SELECTED_DATE).orElseThrow();
        String text = driverTools.getText(element);
        double price = convertPrice(text);
        logger.log("Price on date picker: " + text + " -> " + price);
        return price;
    }

    @Override
    public double getFirstFlightCardPrice() {
        WebElement element = driverTools.waitForElementToAppear(FLIGHT_CARD_FIRST_PRICE).orElseThrow();
        String text = driverTools.getText(element);
        double price = convertPrice(text);
        logger.log("First flight card price: " + text + " -> " + price);
        return price;
    }

    @Override
    public boolean isRoundTripDealLabelDisplayed() {
        boolean displayed = driverTools.isElementDisplayed(ROUND_TRIP_DEAL_LABEL);
        logger.log("Round-trip Deals label displayed: " + displayed);
        return displayed;
    }

    @Override
    public double getLastFlightCardPriceBeforeDeltaDesc() {
        driverTools.swipeToElement(DELTA_PRICE_DESCRIPTION, Swipe.UP, 20);
        driverTools.swipeElementToMidScreen(DELTA_PRICE_DESCRIPTION);

        WebElement element = driverTools.swipeToPresentedElement(LAST_CARD_PRICE_BEFORE_DELTA_DESC, Swipe.DOWN, 3).orElseThrow();
        String text = driverTools.getText(element);
        double price = convertPrice(text);
        logger.log("Last flight card price before delta desc: " + text + " -> " + price);
        return price;
    }

    @Override
    public double getFirstFlightCardPriceAfterDeltaDesc() {
        WebElement element = driverTools.swipeToPresentedElement(FIRST_CARD_PRICE_AFTER_DELTA_DESC, Swipe.UP, 5).orElseThrow();
        String text = driverTools.getText(element);
        double price = convertPrice(text);
        logger.log("First flight card price after delta desc: " + text + " -> " + price);
        return price;
    }

    @Override
    public void swipeToDeltaPriceDesc() {
        WebElement element = driverTools.swipeToElement(DELTA_PRICE_DESCRIPTION, Swipe.UP, 15).orElseThrow();
        String actualText = driverTools.getText(element);
        logger.log("Delta price description text: " + actualText);
        Assertion.assertThat(
                "Delta price description text should match Round-trip Deals copy",
                actualText,
                anyOf(
                        containsString("If you return without Round-trip Deals flight"),
                        containsString("Jika pulang tanpa penerbangan Round-trip Deals")
                )
        );
    }

    @Override
    public void selectFirstFlightWithRoundTripDealsLabel(String roundTripDealsFilter) {
        // Departure cards no longer render a Round-trip Deals tag (same as web). The test
        // applies the filter first; this then selects the first remaining SMART_TRIP card.
        waitForPageLoad();
        driverTools.waitForElementToAppear(FLIGHT_CARD_1, 15).orElseThrow();
        driverTools.clickElementByCoordinate(FLIGHT_CARD_1);
        waitForPageLoad();
    }

    @Override
    public void selectFirstFlightWithoutRoundTripDealsAfterDeltaDesc() {
        swipeToDeltaPriceDesc();
        WebElement priceElement = driverTools.swipeToPresentedElement(FIRST_CARD_PRICE_AFTER_DELTA_DESC, Swipe.UP, 5)
                .orElseThrow();
        driverTools.clickElementByCoordinate(priceElement);
        waitForPageLoad();
    }

    @Override
    public void clickChangeBtn(){
        driverTools.waitForElementToAppear(FLIGHT_CARD_1,15);
        driverTools.click(CHANGE_BTN);
    }

    @Override
    public void clickSearchBtn(){
        FlightSearchPage<T> flightSearchPage = FlightSearchPage.getInstance(session);
        flightSearchPage.searchFlight();
    }

    @Override
    public void verifySelectedDateSRP(String dateSelectedDepart,String dateSelectedReturn,String lang){
        FlightSearchPage<T> flightSearchPage = FlightSearchPage.getInstance(session);
        flightSearchPage.verifySelectedDate(dateSelectedDepart,dateSelectedReturn,lang);
    }

    @Override
    public boolean isSearchResultPageEmptyForEconomy(String currency){
        driverTools.waitForElementToAppear(CHANGE_SEARCH_BTN_SRP,25).orElseThrow();

        String subHeader = driverTools.getText(TITLE_SUB_HEADER).orElseThrow();
        boolean isSearchSubHeaderShowsEconomy = (subHeader.contains("Economy") || subHeader.contains("Ekonomi") )&& !subHeader.contains("Premium");
        logger.log("sub header : " + subHeader);
        Assertion.assertThat("sub header contains only Economy",isSearchSubHeaderShowsEconomy,is(true));

        List<WebElement> priceSelectedDates = driverTools.waitForPresenceElementsList(PRICE_SELECTED_DATE).orElseThrow();
        List<String> priceSelectedDateTexts = priceSelectedDates.stream()
                .map(driverTools::getText)
                .toList();
        boolean isPriceSelectedDateNotContainsCurrency = priceSelectedDateTexts.stream()
                .anyMatch(priceText -> !priceText.contains(currency));
        logger.log("price selected dates: " + priceSelectedDateTexts
                + " | currency: " + currency
                + " | found price without currency: " + isPriceSelectedDateNotContainsCurrency);
        Assertion.assertThat("Price selected date not contains currency",isPriceSelectedDateNotContainsCurrency,is(true));

        boolean isChangeSearchBtnDisplayed = driverTools.isElementDisplayed(CHANGE_SEARCH_BTN_SRP);
        logger.log("is change search btn displayed : " + isChangeSearchBtnDisplayed);
        Assertion.assertThat("Change search btn displayed",isChangeSearchBtnDisplayed,is(true));

        return isSearchSubHeaderShowsEconomy && isPriceSelectedDateNotContainsCurrency && isChangeSearchBtnDisplayed;
    }

    @Override
    public boolean isSearchResultShownForPremiumEconomy(String currency){
        driverTools.waitForElementToAppear(FLIGHT_CARD_1,25).orElseThrow();

        String subHeader = driverTools.getText(TITLE_SUB_HEADER).orElseThrow();
        boolean isSearchSubHeaderShowsPremiumEconomy = subHeader.contains("Premium");
        logger.log("subHeader title : " + subHeader);
        Assertion.assertThat("sub header contains Premium",isSearchSubHeaderShowsPremiumEconomy,is(true));

        List<WebElement> priceSelectedDates = driverTools.waitForPresenceElementsList(PRICE_SELECTED_DATE).orElseThrow();
        List<String> priceSelectedDateTexts = priceSelectedDates.stream()
                .map(driverTools::getText)
                .toList();
        boolean isPriceSelectedDateContainsCurrency = priceSelectedDateTexts.stream()
                .anyMatch(priceText -> priceText.contains(currency));
        logger.log("price selected dates: " + priceSelectedDateTexts
                + " | currency: " + currency
                + " | found price with currency: " + isPriceSelectedDateContainsCurrency);
        Assertion.assertThat("Price selected date contains currency",isPriceSelectedDateContainsCurrency,is(true));

        boolean isFlightCardDisplayed = driverTools.isElementDisplayed(FLIGHT_CARD_1);
        logger.log("is flight card displayed : " + isFlightCardDisplayed);
        Assertion.assertThat("Flight card displayed",isFlightCardDisplayed,is(true));

        return isSearchSubHeaderShowsPremiumEconomy && isPriceSelectedDateContainsCurrency && isFlightCardDisplayed;
    }

    @Override
    public boolean isMeetTravelPolicy(){
        return driverTools.waitForElementToPresence(MEET_TRAVEL_POLICY_1).isPresent();
    }

    @Override
    public boolean isMeetTravelPolicyAll(String airline){
        boolean isMeetTravelPolicy = driverTools.waitForElementToPresence(MEET_TRAVEL_POLICY_1).isPresent();
        logger.log("is meet travel policy displayed : " + isMeetTravelPolicy);

        this.openFilter();

        driverTools.waitForElementToAppear(CLOSE_BTN_FILTER);
        logger.log("check only one airline present: " + airline);

        List<WebElement> airlineTitleInFilter = driverTools.elements(airlineOptionLocator(airline)).orElseThrow();
        boolean sizeIs1 = airlineTitleInFilter.size() == 1;
        Assertion.assertThat("Only one airline present in filter",airlineTitleInFilter.size(),is(1));

        boolean isAirlinePresent = driverTools.isElementDisplayed(airlineNameLocator(airline));
        logger.log("is airline present : " + isAirlinePresent);
        return isMeetTravelPolicy && sizeIs1 && isAirlinePresent;
    }

    private Locator airlineNameLocator(String airline){
        airline = CoreFunctions.titleize(airline);
        if (airline.contains("nam")) airline = airline.replace("Nam", "NAM");
        return airlineOptionLocator(airline);
    }

    @Override
    public void applyPromoInfinity(){
        driverTools.waitForElementToAppear(APPLY_PROMO_INFINITY);
        driverTools.click(APPLY_PROMO_INFINITY);
    }

    @Override
    public boolean isPromoInfinityApplyButtonDisplayed(){
        boolean isApplyBtnDisplayed = driverTools.waitForElementToAppear(APPLY_PROMO_INFINITY,15).isPresent();
        logger.log("is promo banner apply button displayed : " + isApplyBtnDisplayed);
        return isApplyBtnDisplayed;
    }

    @Override
    public boolean isPromoInfinityApplied(){
        driverTools.waitForElementToAppear(PROMO_APPLIED_LABEL);
        boolean isCancelBtnDisplayed = driverTools.isElementDisplayed(CANCEL_PROMO_INFINITY);
        logger.log("isCancelBtnDisplayed: " + isCancelBtnDisplayed);

        boolean isTextPromoApplied = driverTools.isElementDisplayed(PROMO_APPLIED_TEXT);
        logger.log("isTextPromoApplied: " + isTextPromoApplied);

        boolean isPromoLabelShownOnCard = driverTools.waitForElementToPresence(PROMO_APPLIED_LABEL).isPresent();
        logger.log("isPromoLabelShownOnCard: " + isPromoLabelShownOnCard);

        return isCancelBtnDisplayed && isTextPromoApplied && isPromoLabelShownOnCard;
    }

    @Override
    public List<String> getAirlineNames(String airline1, String airline2) {
        List<String> airlineNames = driverTools.getTexts(AIRLINE_NAME);

        boolean found = false;
        for (String name : airlineNames) {
            logger.log("Found airline: " + name);
            if (name.contains(airline1) || name.contains(airline2)) {
                found = true;
            }
        }

        if (found) {
            logger.log("Given airlines '" + airline1 + "' or '" + airline2 + "' found.");
        } else {
            logger.warn("Given airlines '" + airline1 + "' and '" + airline2 + "' not found.");
        }

        Assertion.assertThat("Airline names should not be empty", airlineNames, CoreMatchers.is(not(empty())));
        return airlineNames;
    }

    @Override
    public boolean checkAirlineVI(ArrayList<String> airline) {
        ArrayList<String> airlineList = new ArrayList<>();
        int retry = 10;
        while (retry>=0){
            List<String> airlineNames = driverTools.getTexts(AIRLINE_NAME);
            for(String airlineOnCard : airlineNames){
                if(!airlineList.contains(airlineOnCard) && airlineOnCard.contains("+")){
                    logger.log("airline added in list: " + airlineOnCard);
                    String[] airlineCard = airlineOnCard.split("\\+");
                    airlineList.add(airlineCard[0].trim());
                    airlineList.add(airlineCard[1].trim());
                }
            }
            driverTools.swipeUp();
            retry--;
        }
        boolean found = airlineList.containsAll(airline);
        if (found) {
            logger.log("Given airlines '" +  airline + "' or '" + airlineList + "' found.");
        } else {
            logger.warn("Given airlines '" + airline + "' and '" + airlineList + "' not found.");
        }
        Assertion.assertThat("Airline names should not be empty", airlineList, CoreMatchers.is(not(empty())));
        return found;
    }


    private boolean checkAirlineName(String airline,int retry) {
        ArrayList<String> airlineList = new ArrayList<>();
        while (retry>=0){
            List<String> airlineNames = driverTools.getTexts(AIRLINE_NAME);
            for(String airlineOnCard : airlineNames){
                if(!airlineList.contains(airlineOnCard)){
                    logger.log("airline added in list: " + airlineOnCard);
                    airlineList.add(airlineOnCard.trim());
                }
            }
            driverTools.swipeUp();
            retry--;
        }

        boolean found = airlineList.stream().allMatch(airlineName -> airlineName.contains(airline));

        if (found) {
            logger.log("Given airlines '" +  airline + "' or '" + airlineList + "' found.");
        } else {
            logger.warn("Given airlines '" + airline + "' and '" + airlineList + "' not found.");
        }
        Assertion.assertThat("Airline names should not be empty " + airline, airlineList, CoreMatchers.is(not(empty())));
        return found;
    }

    @Override
    public boolean checkAirlines(ArrayList<String> airlines,int numberOfSwipe){
        for (String airline : airlines){
            logger.log("filter airline : " + airline);
            this.openFilter();
            this.resetFilter();
            this.filterAirline(airline);
            this.saveFilter();
            this.waitForPageLoad();

            logger.log("check airline on srp");
            Assertion.assertThat("is airline filtered shown on srp : " + airline,this.checkAirlineName(airline,numberOfSwipe),is(true));
        }
        return true;
    }

    private Map<String, Integer> getNumberOfFlightOnFilter(ArrayList<String> airlines,int retry){
        Map<String, Integer> airlineCount = new HashMap<>();
        Collections.sort(airlines);

        while (retry > 0 && airlineCount.size() != airlines.size()) {
            for (String airline : airlines){
                airline = CoreFunctions.titleize(airline);
                if (airline.contains("nam")) airline = airline.replace("Nam", "NAM");
                Locator airlineName = airlineOptionLocator(airline);
                if(!airlineCount.containsKey(airline.toLowerCase()) && driverTools.isElementDisplayed(airlineName)){
                    if(!airlineCount.containsKey(airline.toLowerCase())){
                        driverTools.click(airlineName);
                        int count = countNumberOfFlights();
                        logger.log("number of airline for [ name: " + airline + ",count: " + count + " ]");
                        airlineCount.put(airline.toLowerCase(),count);
                        driverTools.click(airlineName);
                    }
                }
            }
            driverTools.swipeUp();
            retry--;
        }
        return airlineCount;
    }

    @Override
    public boolean checkAirlinesOnFilter(ArrayList<String> airlines,int numberOfSwipe){
        this.openFilter();
        this.resetFilter();
        driverTools.waitForElementToAppear(CLOSE_BTN_FILTER);

        Map<String, Integer> airlineCount = getNumberOfFlightOnFilter(airlines,6);
        boolean found = true;

        for (String airline : airlines) {
            int count = airlineCount.get(airline.toLowerCase()) == null ? -1 : airlineCount.get(airline.toLowerCase());
            boolean isCountMoreThenZero = count > 0;
            found = found & isCountMoreThenZero;
            Assertion.assertThat("is [airlineName: " + airline + " count: " + count + " ] more then zero ",count,is(not(0)));
        }
        driverTools.click(CLOSE_BTN_FILTER);
        return found;
    }

    @Override
    public ArrayList<String> getAirlineNames(ArrayList<String> airline) {
        boolean found = false;
        ArrayList<String> airlineList = new ArrayList<>();
        int retry = 8;
        while (!found && retry > 0){
            List<String> airlineNames = driverTools.getTexts(AIRLINE_NAME);
            for (String airlineOnCard : airlineNames){
                if(!airlineList.contains(airlineOnCard)){
                    airlineList.add(airlineOnCard.trim());
                }
            }
           found = airlineList.containsAll(airline);
           driverTools.swipeUp(1);
            retry--;
        }

        if (found) {
            logger.log("Given airlines '" + airline + " in list " + airlineList);
        } else {
            logger.warn("Given airlines '" + airline + "' not found." + " in list " + airlineList);
        }
        Assertion.assertThat("Airline names should not be empty", airlineList, is(notNullValue()));
        return airlineList;
    }

    @Override
    public boolean verifyAllVisibleFlightCardsMatchAirlineFilter(String appliedAirline, int maxFlightCardsToCheck) {
        if (appliedAirline == null || appliedAirline.isBlank()) {
            logger.log("verifyAllVisibleFlightCardsMatchAirlineFilter: applied airline is blank");
            return false;
        }
        if (maxFlightCardsToCheck <= 0) {
            logger.log("verifyAllVisibleFlightCardsMatchAirlineFilter: maxFlightCardsToCheck must be positive");
            return false;
        }
        String needle = CoreFunctions.titleize(appliedAirline.trim());

        String needleLower = needle.toLowerCase(Locale.ROOT);
        Optional<List<WebElement>> rowsOpt = driverTools.elements(SRP_FLIGHT_CARD_AIRLINE_NAMES);
        if (rowsOpt.isEmpty() || rowsOpt.get().isEmpty()) {
            logger.log("No flight card airline labels found under dynamic_view_body for filter verification");
            return false;
        }
        List<WebElement> rows = rowsOpt.get();
        int toCheck = Math.min(maxFlightCardsToCheck, rows.size());
        if (rows.size() < maxFlightCardsToCheck) {
            logger.log("SRP airline rows on viewport: " + rows.size() + " (verifying all, max cap is " + maxFlightCardsToCheck + ")");
        }
        for (int i = 0; i < toCheck; i++) {
            String text = driverTools.getText(rows.get(i));
            if (text == null) {
                logger.log("Empty airline text on flight card index " + i);
                return false;
            }
            text = text.trim();
            if (text.isEmpty()) {
                logger.log("Empty airline text on flight card index " + i);
                return false;
            }
            if (!text.toLowerCase(Locale.ROOT).contains(needleLower)) {
                logger.log("SRP airline text does not match filter. Filter substring: " + needle + ", card index " + i + ", card text: " + text);
                return false;
            }
        }
        logger.log("First " + toCheck + " flight card(s) match filter substring: " + needle);
        return true;
    }

    private int countNumberOfFlights(){
        try {
            String text = String.valueOf(driverTools.getText(AIRLINE_COUNT));
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group());
            } else {
                throw new IllegalArgumentException("No number found in the input text: " + text);
            }
        }
        catch (Exception e) {
            return 0;
        }
    }

    /** Fast child text read — no DriverTools retry loop (partial/off-screen cards are common while scrolling). */
    private String readChildText(WebElement parent, Locator child) {
        try {
            List<WebElement> found = parent.findElements(child.by());
            if (found.isEmpty()) {
                return "";
            }
            String text = found.get(0).getText();
            return text == null ? "" : text.trim();
        } catch (RuntimeException e) {
            return "";
        }
    }

    private String readFlightNameFromCard(WebElement card) {
        String fromInfo = readChildText(card, FLIGHT_CARD_AIRLINE_FROM_INFO);
        if (!fromInfo.isBlank()) {
            return fromInfo;
        }
        return readChildText(card, FLIGHT_CARD_AIRLINE_NAME_LEGACY);
    }

    /**
     * Parses {@code N} from filter labels like {@code Garuda Indonesia (2)} / {@code SriLankan Airlines (7)}.
     */
    private static int parseAirlineFilterCount(String labelText) {
        if (labelText == null || labelText.isBlank()) {
            return -1;
        }
        Matcher matcher = Pattern.compile("\\((\\d+)\\)\\s*$").matcher(labelText.trim());
        if (!matcher.find()) {
            return -1;
        }
        return Integer.parseInt(matcher.group(1));
    }

    @Override
    public int getAirlineFilterOptionCount(String airline) {
        if (airline == null || airline.isBlank()) {
            logger.log("getAirlineFilterOptionCount: airline is blank");
            return -1;
        }
        Locator option = airlineOptionLocator(airline);
        if (!driverTools.isElementDisplayed(option)) {
            driverTools.swipeToElement(option, AIRLINE_FILTER_RECYCLER, Swipe.UP, 5);
        }
        String label = driverTools.getText(option).orElse("").trim();
        int count = parseAirlineFilterCount(label);
        logger.log("getAirlineFilterOptionCount: airline='" + airline + "', label='" + label + "', count=" + count);
        return count;
    }

    @Override
    public boolean areAllFlightSchedulesUnique(String expectedAirline, int expectedCardCountFromFilter) {
        if (expectedAirline == null || expectedAirline.isBlank()) {
            logger.log("areAllFlightSchedulesUnique: expectedAirline is blank");
            return false;
        }
        if (expectedCardCountFromFilter < 0) {
            logger.log("areAllFlightSchedulesUnique: expectedCardCountFromFilter is invalid: "
                    + expectedCardCountFromFilter);
            return false;
        }
        String airlineNeedle = expectedAirline.trim().toLowerCase(Locale.ROOT);
        LinkedHashSet<String> discoveredSchedules = new LinkedHashSet<>();
        List<String[]> mappedRows = new ArrayList<>(); // [card#, depart, arrival, schedule, airline, filterMatch]
        boolean airlineMatchedOnAllCards = true;
        boolean schedulesAreUnique = true;
        int cardNumber = 0;
        int stagnantRounds = 0;
        int swipeBudget = 12;
        Set<String> previousViewportSchedules = new HashSet<>();

        logger.log("=== SRP unique schedule check START | filter airline: '" + expectedAirline
                + "' | expected card count from filter: " + expectedCardCountFromFilter + " ===");
        driverTools.waitForElementToAppear(FLIGHT_ITEM_CARDS, 20);

        while (stagnantRounds < 2 && swipeBudget > 0) {
            List<WebElement> cards = driverTools.elements(FLIGHT_ITEM_CARDS).orElse(Collections.emptyList());
            cards.sort(Comparator.comparingInt(c -> c.getLocation().getY()));
            List<String> allSchedulesInViewport = new ArrayList<>();
            List<String> newInViewport = new ArrayList<>();
            int newlyDiscovered = 0;
            int swipeRound = 12 - swipeBudget + 1;
            logger.log("--- swipe round " + swipeRound + " | visible cards=" + cards.size()
                    + " | already mapped=" + discoveredSchedules.size() + " ---");

            for (WebElement card : cards) {
                String departHour = readChildText(card, FLIGHT_CARD_DEPART_HOUR);
                String arrivalHour = readChildText(card, FLIGHT_CARD_ARRIVAL_HOUR);
                if (departHour.isBlank() || arrivalHour.isBlank()) {
                    continue;
                }
                String flightName = readFlightNameFromCard(card);
                // Partially scrolled card — times visible but airline row not painted yet
                if (flightName.isBlank()) {
                    logger.log("  [skip partial] depart=" + departHour + " arrival=" + arrivalHour
                            + " (airline not visible yet)");
                    continue;
                }
                String schedule = departHour + " - " + arrivalHour;
                allSchedulesInViewport.add(schedule);

                if (discoveredSchedules.contains(schedule)) {
                    // Still overlapping after swipe — OK. Reappeared after leaving viewport — real duplicate.
                    if (!previousViewportSchedules.contains(schedule)) {
                        logger.log("  [FAIL] duplicate schedule reappeared after leaving viewport: "
                                + schedule + " | airline=" + flightName);
                        schedulesAreUnique = false;
                    } else {
                        logger.log("  [already mapped / still visible] schedule=" + schedule
                                + " | airline=" + flightName);
                    }
                    continue;
                }

                newInViewport.add(schedule);
                discoveredSchedules.add(schedule);
                newlyDiscovered++;
                cardNumber++;
                boolean filterMatch = flightName.toLowerCase(Locale.ROOT).contains(airlineNeedle);
                mappedRows.add(new String[]{
                        String.valueOf(cardNumber),
                        departHour,
                        arrivalHour,
                        schedule,
                        flightName,
                        filterMatch ? "YES" : "NO"
                });
                logger.log("  [NEW Card #" + cardNumber + "] depart=" + departHour
                        + " | arrival=" + arrivalHour
                        + " | schedule=" + schedule
                        + " | airline/flightName=" + flightName
                        + " | filterMatch=" + (filterMatch ? "YES" : "NO"));
                if (!filterMatch) {
                    logger.log("  [FAIL] Card #" + cardNumber + " airline does not contain filter '"
                            + expectedAirline + "'");
                    airlineMatchedOnAllCards = false;
                }
            }

            Set<String> uniqueInViewport = new HashSet<>(allSchedulesInViewport);
            if (uniqueInViewport.size() != allSchedulesInViewport.size()) {
                logger.log("  [FAIL] duplicate schedule(s) in current viewport: " + allSchedulesInViewport);
                schedulesAreUnique = false;
            } else if (!newInViewport.isEmpty()) {
                logger.log("  [ok] " + newlyDiscovered + " new unique schedule(s) this round: " + newInViewport);
            } else {
                logger.log("  [ok] no new schedules this round (stagnant=" + (stagnantRounds + 1) + ")");
            }

            previousViewportSchedules = new HashSet<>(allSchedulesInViewport);

            if (newlyDiscovered == 0) {
                stagnantRounds++;
            } else {
                stagnantRounds = 0;
            }
            driverTools.swipeUp();
            swipeBudget--;
        }

        int mappedCount = mappedRows.size();
        boolean countMatchesFilter = mappedCount == expectedCardCountFromFilter;
        logger.log("=== SRP unique schedule check SUMMARY ===");
        logger.log("Expected filter airline : " + expectedAirline);
        logger.log("Filter listed count (N) : " + expectedCardCountFromFilter);
        logger.log("Total cards mapped      : " + mappedCount);
        logger.log("Unique schedules        : " + discoveredSchedules.size());
        logger.log("Card count == filter (N): " + (countMatchesFilter ? "PASS" : "FAIL"));
        logger.log("Airline filter matched  : " + (airlineMatchedOnAllCards ? "PASS" : "FAIL"));
        logger.log("Schedules unique        : " + (schedulesAreUnique && mappedCount == discoveredSchedules.size()
                ? "PASS" : "FAIL"));
        if (!countMatchesFilter) {
            logger.log("  [FAIL] SRP mapped cards (" + mappedCount
                    + ") != filter label count (" + expectedCardCountFromFilter + ")");
        }
        logger.log("--- card map (one per line) ---");
        for (String[] row : mappedRows) {
            logger.log("Card #" + row[0]
                    + " | depart=" + row[1]
                    + " | arrival=" + row[2]
                    + " | schedule=" + row[3]
                    + " | airline/flightName=" + row[4]
                    + " | filterMatch=" + row[5]);
        }
        boolean hasCards = !discoveredSchedules.isEmpty();
        boolean result = hasCards && schedulesAreUnique && airlineMatchedOnAllCards
                && mappedCount == discoveredSchedules.size()
                && countMatchesFilter;
        logger.log("=== SRP unique schedule check END | result=" + (result ? "PASS" : "FAIL") + " ===");
        return result;
    }

    @Override
    public boolean isVI_AirlineDisplayed(){
        driverTools.swipeToElement(getViAirlineLocator(),Swipe.UP,6);
        return driverTools.isElementDisplayed(getViAirlineLocator());
    }

    private Locator getViAirlineLocator() {
        String xpath = "//*[contains(@resource-id, 'tv_flight_body') and (contains(@text, '+') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), 'self-transfer'))]";
        return new Locator("AIRLINE_NAME_ON_CARD", AppiumBy.xpath(xpath));
    }

    private Locator getViAirlineLocatorIndex(String airlineName,int index){
        String airlineNameLiteral = CoreFunctions.toXPathLiteral(airlineName.toLowerCase(Locale.ROOT));
        return new Locator("AIRLINE_NAME_ON_CARD",AppiumBy.xpath("(//*[contains(@resource-id,'tv_flight_body') and (contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," + airlineNameLiteral + ") and contains(@text,'+') and not(contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'airline') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'maskapai') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), 'self-transfer')))])["+index+"]"));
    }

    @Override
    public List<String> getFirstFlightCardAirlineNamesSplitByPlus() {
        driverTools.swipeToElement(SRP_FLIGHT_CARD_AIRLINE_NAMES_FIRST, Swipe.UP, 6);
        String raw = driverTools.getText(SRP_FLIGHT_CARD_AIRLINE_NAMES_FIRST).orElse("");
        return splitAirlineNamesCombinedByPlus(raw);
    }

    @Override
    public void waitForPageLoad(){
        driverTools.waitForElementToAppear(PROGRESS_BAR_SRP,10);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_SRP);
    }

    @Override
    public Boolean verifySRPNudges(String language) {
        List<String> srpNudges = driverTools.getTexts(SRP_NUDGES);
        String english_keyword = "fly with";
        String bahasa_keyword = "Terbang dengan";
        String keyword = language.equals("en") ? english_keyword : bahasa_keyword;
        for (String nudge : srpNudges)
            Assertion.assertThat("All SRP nudges is in the correct language", nudge.toLowerCase(), containsString(keyword.toLowerCase()));

        return !srpNudges.isEmpty();
    }

    @Override
    public boolean SaveLabel(){
        return driverTools.waitForElementToPresence(SAVE_LABEL,20).isPresent();
    }

    @Override
    public boolean CheckCashBackLabel(){
        return driverTools.waitForElementToPresence(CASHBACK_LABEL, 20).isPresent();
    }

    @Override
    public void selectTicketFromDetailsPage(){
        selectTicket();
    }

}