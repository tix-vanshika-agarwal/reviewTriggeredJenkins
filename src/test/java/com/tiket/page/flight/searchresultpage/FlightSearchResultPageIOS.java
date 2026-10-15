package com.tiket.page.flight.searchresultpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.CoreMatchers;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tiket.core.CoreFunctions.convertPrice;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class FlightSearchResultPageIOS<T extends WebDriver> extends FlightSearchResultPage<T> {

    public FlightSearchResultPageIOS(Session<T> session) {
        super(session);
    }

    public static final Locator DATE_LIST = new Locator("DATE_LIST",AppiumBy.xpath("//*[@type='XCUIElementTypeCollectionView']"));
    public static final Locator CHANGE = new Locator("CHANGE",AppiumBy.id("change_btn"));
    private static final Locator CHANGE_SEARCH_BUTTON = new Locator("CHANGE_SEARCH_BUTTON",AppiumBy.xpath("(//*[@name='flight_card']/parent::*/preceding-sibling::*//XCUIElementTypeImage)[last()]"));
    public static final Locator TOGGLE_TRIP = new Locator("TOGGLE_TRIP",AppiumBy.id("toggle_rt"));
    public static final Locator TOGGLE_TRIP_ACTIVE = new Locator("TOGGLE_TRIP_ACTIVE",AppiumBy.id("toggle_rt (selected)"));
    public static final Locator FLIGHT_SEARCH_BUTTON = new Locator("FLIGHT_SEARCH_BUTTON",AppiumBy.id("flightSearchButton"));
    public static final Locator FILTER = new Locator("FILTER",AppiumBy.xpath("//*[contains(@name,'airline')]"));
    public static final Locator FILTER_OLD = new Locator("FILTER",AppiumBy.accessibilityId("bottomActionItem_airline"));/** New SRP bottom bar (Jul 2026): {@code bottomActionItem_airline} replaces {@code airline} on some builds. */
    public static final Locator FILTER_AIRLINE_BOTTOM_ACTION = new Locator("FILTER_AIRLINE_BOTTOM_ACTION", AppiumBy.accessibilityId("bottomActionItem_airline"));
    public static final Locator FILTER_ICON = new Locator("FILTER_ICON",AppiumBy.xpath("(//*[@label='Filter' and @type='XCUIElementTypeStaticText'])[1]"));
    /** Airlines bottomsheet is open when close/reset/title/checkbox row is visible. */
    private static final Locator AIRLINE_FILTER_SHEET_READY = new Locator("AIRLINE_FILTER_SHEET_READY", AppiumBy.xpath("//*[@name='ic cancel grey'] | //XCUIElementTypeButton[@name='Reset'] | (//XCUIElementTypeStaticText[@name='Maskapai' or @name='Airlines'])[last()] | //*[contains(@name,'filter_bottom_checkbox')] | //*[contains(@name,'filter_group_checkbox_')]"));
    /** Scrollable airline list inside the Airlines bottomsheet (flightsrpfilter.xml). */
    private static final Locator AIRLINE_FILTER_SHEET_TABLE = new Locator("AIRLINE_FILTER_SHEET_TABLE", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='Airlines' or @name='Maskapai']/following::XCUIElementTypeTable[1])[1]"));
    private static final int AIRLINE_FILTER_MAX_SCROLLS = 8;
    /** Display-name fragments → {@code filter_group_checkbox_*} suffix (new iOS Airlines sheet). */
    private static final Map<String, String> AIRLINE_NAME_TO_FILTER_CODE = Map.ofEntries(
            Map.entry("airasia indonesia", "QZ"),
            Map.entry("airasia", "QZ"),
            Map.entry("lion air", "JT"),
            Map.entry("garuda indonesia", "GA"),
            Map.entry("citilink", "QG"),
            Map.entry("batik air indonesia", "ID"),
            Map.entry("batik air", "ID"),
            Map.entry("nam air", "IN"),
            Map.entry("super air jet", "IU"),
            Map.entry("pelita air", "IP"),
            Map.entry("sriwijaya air", "SJ"),
            Map.entry("transnusa", "8B"),
            Map.entry("singapore airlines", "SQ"),
            Map.entry("singapore airline", "SQ")
    );
    public static final Locator SAVE_AIRLINE = new Locator("SAVE_AIRLINE",AppiumBy.id("see_flights"));
    public static final Locator RESET_FILTER = new Locator("RESET_FILTER",AppiumBy.xpath("//*[@name='Reset']"));
    public static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='Reset']"));
    private static final Locator SORT_TAB_IN_ADVANCED_FILTER = new Locator("SORT_TAB_IN_ADVANCED_FILTER", AppiumBy.xpath("(//XCUIElementTypeStaticText[@label='Sort' or @name='Urutkan'])[1]"));
    private static final Locator SORT_BY_SECTION_HEADER = new Locator("SORT_BY_SECTION_HEADER", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Sort by' or @name='Urutkan berdasarkan']"));
    private static final Locator SORT_SEE_MORE = new Locator("SORT_SEE_MORE", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Sort by' or @name='Urutkan berdasarkan']/ancestor::XCUIElementTypeCell[1]//XCUIElementTypeButton[contains(@name,'more')]"));
    public static final Locator MASTER_FILTER = new Locator("MASTER_FILTER",AppiumBy.xpath("//*[contains(@name,'bottomActionItem_filter')]"));
    public static final Locator OTHER_FILTER_TITLE = new Locator("OTHER_FILTER_TITLE",AppiumBy.xpath("//XCUIElementTypeOther[contains(@name,'Other filters') or contains(@name,'Filter Lainnya')]"));
    public static final Locator CRP_MASTER_FILTER = new Locator("CRP_MASTER_FILTER",AppiumBy.xpath("//*[contains(@name,'filter_bottom_checkbox_CORPORATE_DEAL')]"));
    public static final Locator SEE_ALL_OTHER_FILTER = new Locator("SEE_ALL_OTHER_FILTER",AppiumBy.xpath("//XCUIElementTypeOther[contains(@name,'btn_see_more_fareType')]"));
    public static final Locator SEE_MORE_FARETYPE_IN_OTHER_FILTERS = new Locator("SEE_MORE_FARETYPE_IN_OTHER_FILTERS",AppiumBy.xpath("//*[@name='Other filters' and @type='XCUIElementTypeOther']/ancestor::*[1]//*[@name='btn_see_more_fareType']"));
    public static final Locator SELF_TRANSFER_CHECKBOX = new Locator("SELF_TRANSFER_CHECKBOX",AppiumBy.xpath("//*[@name='Self-transfer']/ancestor::*[1]//*[@name='filter_group_checkbox_TIKET-VI']"));
    public static final Locator SAVE_SEE_ALL_OTHER_FILTER = new Locator("SAVE_SEE_ALL_OTHER_FILTER",AppiumBy.xpath("//XCUIElementTypeButton[(contains(@name,'Save') or contains(@name,'Simpan')) and @visible='true']"));
    /** First price cell on card: sibling whose label contains "+" (combined VI airline line). */
    private static final Locator SRP_FIRST_FLIGHT_CARD_VI_AIRLINE_ADJACENT_TO_PRICE = new Locator("SRP_FIRST_FLIGHT_CARD_VI_AIRLINE_ADJACENT_TO_PRICE", AppiumBy.xpath("//XCUIElementTypeStaticText[(contains(@name,'+') or contains(@label,'+'))]"));
    public static final Locator PRICE_POLICY_TAB = new Locator("PRICE_POLICY_TAB",AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and (contains(@name,'See details') or contains(@name,'Lihat Detail'))]"));
    public static final Locator PRICE = new Locator("PRICE",AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and (@label='Price Detail' or @label='Detail Harga')]"));
    public static final Locator REFUND_ALLOWED = new Locator("REFUND_ALLOWED",AppiumBy.xpath("//*[@label='Bisa refund' or @label='Refund allowed']"));
    public static final Locator RESCHEDULE_ALLOWED = new Locator("RESCHEDULE_ALLOWED",AppiumBy.xpath("///*[@label='Bisa reschedule' or @label='Reschedule allowed']"));
    public static final Locator POLICY_DETAIL = new Locator("POLICY_DETAIL",AppiumBy.xpath("//*[@label='Detail' or @label='Details' and @type='XCUIELementTypeButton']"));
    public static final Locator REFUND_TAB = new Locator("REFUND_TAB",AppiumBy.accessibilityId("Refund"));
    public static final Locator RESCHEDULE_TAB = new Locator("RESCHEDULE_TAB",AppiumBy.accessibilityId("Reschedule"));
    public static final Locator REFUND_INFO = new Locator("REFUND_INFO",AppiumBy.xpath("//*[contains(@label,'Biaya Pembatalan' ) or contains(@label,'Cancellation Fee')]"));
    public static final Locator RESCHEDULE_INFO = new Locator("RESCHEDULE_INFO",AppiumBy.xpath("//*[contains(@label,'Biaya Reschedule' ) or contains(@label,'Reschedule Fee')]"));
    public static final Locator CLOSE_POLICY = new Locator("CLOSE_POLICY",AppiumBy.xpath("//*[@name='ic cancel grey' and ./parent::*[./parent::*[(./preceding-sibling::* | ./following-sibling::*)[./*[./*[./*[./*[./*[./*[@label='Ticket Policies' or @label='Kebijakan Tiket']]]]]]]]]]"));
    public static final Locator SELECT_TICKET = new Locator("SELECT_TICKET",AppiumBy.xpath("//*[@class='XCUIElementTypeButton' and @name='select']"));
    public static final Locator PROMO_ICON = new Locator("PROMO_ICON",AppiumBy.id("flightInfoButton"));
    public static final Locator PROMO_CLOSE = new Locator("PROMO_CLOSE",AppiumBy.xpath("//*[@label='Kembali ke Search' or @label='Back to Search']"));
    public static final Locator PROMO_WEBVIEW = new Locator("PROMO_WEBVIEW",AppiumBy.xpath("//*[@label='List Promo' or @label='Promo List']"));
    public static final Locator TRAVEL_INFO = new Locator("TRAVEL_INFO",AppiumBy.xpath("//*[@label='General travel info' or @label='Info perjalanan umum']"));
    public static final Locator TRAVEL_INFO_DETAIL = new Locator("TRAVEL_INFO_BACK",AppiumBy.xpath("//*[@label='General travel info' or @label='Info perjalanan umum']"));
    public static final Locator INFOPAGE_BACK = new Locator("INFOPAGE_BACK",AppiumBy.id("back_btn"));
    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON",AppiumBy.xpath("//*[@name='back_btn' or @name='tds_ic_back']"));
    private static final Locator ROUTE_TEXT = new Locator("ROUTE_TEXT", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@label,'to') or contains(@label,'ke')])[1]"));
    public static final Locator IMPORTANT_MESSAGE = new Locator("IMPORTANT_MESSAGE",AppiumBy.id("flight_note"));
    public static final Locator IMPORTANT_MESSAGE_CLOSE = new Locator("IMPORTANT_MESSAGE_CLOSE",AppiumBy.xpath("//*[@name='ic cancel grey' and (./preceding-sibling::* | ./following-sibling::*)[@label='General travel info' or @label='Info perjalanan umum']]"));
    public static final Locator IMPORTANT_MESSAGE_WEBVIEW = new Locator("IMPORTANT_MESSAGE_WEBVIEW",AppiumBy.xpath("//*[@label='General travel info' or @label='Info perjalanan umum' and ./parent::*[(./preceding-sibling::* | ./following-sibling::*)[@type='XCUIElementTypeOther'] and ./parent::*[@type='XCUIElementTypeOther']]]"));
    public static final Locator OTHER_FACILITIES = new Locator("OTHER_FACILITIES",AppiumBy.xpath("//*[@name='See other facilities' or @name='Lihat fasilitas lain' and @type='XCUIElementTypeButton']"));
    public static final Locator CLOSE = new Locator("CLOSE",AppiumBy.xpath("//*[@name='ic cancel grey' and ./parent::*[./parent::*[(./preceding-sibling::* | ./following-sibling::*)[./*[@label='Flight Facilities' or @label='Fasilitas Penerbangan']]]]]"));
    public static final Locator OTHER_FACILITIES_DETAIL = new Locator("OTHER_FACILITIES_DETAIL",AppiumBy.xpath("//*[@label='Flight Facilities' or @label='Fasilitas Penerbangan']"));
    public static final Locator CLOSED = new Locator("CLOSED",AppiumBy.id("ic cancel grey"));
    public static final Locator RELOAD = new Locator("RELOAD",AppiumBy.xpath("//*[@label='Reload' and @type='XCUIElementTypeButton']"));
    public static final Locator FREE_TRANSFER = new Locator("FREE_TRANSFER",AppiumBy.xpath("(//*[contains(@label,'Gratis Airport Transfer') or contains(@label,'Free Airport Transfer')])[2]"));
    public static final Locator TEST = new Locator("TEST",AppiumBy.xpath("(//*[contains(@label,'Antigen Test') or contains(@label,'Antigen Test')])[1]"));
    public static final Locator LOYALTY_DISC_UP_TO = new Locator("LOYALTY_DISC_UP_TO",AppiumBy.xpath("//XCUIElementTypeCell//*[ (contains(@name,'Benefit') or contains(@label,'Benefit')) and (contains(@name,'Platinum') or contains(@label,'Platinum') or contains(@name,'Diamond') or contains(@label,'Diamond')) ]"));
    public static final Locator CLOSE_DETAIL = new Locator("CLOSE_DETAIL",AppiumBy.xpath("//*[@name='ic cancel grey']"));
    public static final Locator PRICE_BREAKDOWN = new Locator("PRICE_BREAKDOWN", AppiumBy.xpath("//*[contains(@label,'Price Details') or contains(@label,'Detail Harga')]//following-sibling::*//XCUIElementTypeStaticText[contains(@label,'IDR')]"));
    public static final Locator HIDE_DETAILS = new Locator("HIDE_DETAILS", AppiumBy.xpath("//*[@name='hide']"));
    public static final Locator FREE_TRANSFER_DETAIL = new Locator("FREE_TRANSFER_DETAIL",AppiumBy.xpath("(//*[contains(@label, 'Airport Transfer') and @type='XCUIElementTypeStaticText'])[2]"));
    public static final Locator SEE_DEPARTURE_DETAILS = new Locator("SEE_DEPARTURE_DETAILS",AppiumBy.xpath("//*[(@name='See other facilities' or @name='lihat benefit lainnya') and ./preceding-sibling::*[@type='XCUIElementTypeOther']]"));
    public static final Locator AIRPORT_TRANSFER_DETAIL = new Locator("AIRPORT_TRANSFER_DETAIL",AppiumBy.xpath("(//*[@type='XCUIElementTypeStaticText' and contains(@name, 'Airport Transfer')])[3]"));
    public static final Locator ANTIGEN_TEST_DETAIL = new Locator("ANTIGEN_TEST_DETAIL",AppiumBy.xpath("(//*[@type='XCUIElementTypeStaticText' and contains(@name,'Antigen Test')])[2]"));
    public static final Locator SEE_ORIGIN_DETAILS = new Locator("SEE_ORIGIN_DETAILS",AppiumBy.xpath("//*[@name='See details' or @name='Lihat benefit lainnya' and @type='XCUIElementTypeStaticText']"));
    public static final Locator CLOSE_BTN_PRP = new Locator("CLOSE_BTN_PRP",AppiumBy.xpath("//*[@name='ic cancel grey']"));
    public static final Locator FREE_TRANSFER_DETAIL_SRP = new Locator("FREE_TRANSFER_DETAIL_SRP",AppiumBy.xpath("//*[contains(@name,'what_you_get') and (contains(@label,'Gratis Airport Transfer') or contains(@label,'Free Airport Transfer'))]"));
    public static final Locator TEST_DETAIL = new Locator("TEST_DETAIL",AppiumBy.xpath("(//*[contains(@name, 'Antigen Test') and @type='XCUIElementTypeStaticText'])"));
    public static final Locator TEST_DETAIL_SRP = new Locator("TEST_DETAIL_SRP",AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@label,'Hide') or contains(@label,'Sembunyikan')])[1]//preceding-sibling::*[contains(@label,'Gratis Antigen') or contains(@label,'Free Antigen')]"));
    private static final Locator CORPORATE_DEALS_BANNER = new Locator("CORPORATE_DEALS_BANNER",AppiumBy.xpath("//XCUIElementTypeTable//XCUIElementTypeCell[1]//XCUIElementTypeStaticText[contains(@label,'Corporate Deal')]"));
    private static final Locator CORPORATE_DEALS_BANNER_PARENT = new Locator("CORPORATE_DEALS_BANNER_PARENT",AppiumBy.xpath("//XCUIElementTypeTable//XCUIElementTypeCell[1]"));
    private static final Locator CORPORATE_DEALS_BENEFIT_CARD = new Locator("CORPORATE_DEALS_BENEFIT_CARD",AppiumBy.xpath("(//*[@label='Corporate Deals' and @type='XCUIElementTypeStaticText'])[1]"));
    public static final Locator DISCOUNT_TAG_PRICE_AND_POLICY = new Locator("DISCOUNT_TAG_PRICE_AND_POLICY",AppiumBy.xpath("//*[@name='price_detail_info' and (@label='Diskon' or @label='Discount')]"));
    public static final Locator DISCOUNT_PRICE_SRP = new Locator("DISCOUNT_PRICE_SRP",AppiumBy.xpath("(//*[@type='XCUIElementTypeStaticText' and contains(@name, '_price') and contains(@name, 'card_')])[1]"));
    /**
     * "Save IDR &lt;N&gt;" (EN) / "Hemat IDR &lt;N&gt;" (ID) savings badge rendered on the first
     * matching SRP card for special-campaign fares (e.g. Harga Gledek). Targeted by visible
     * accessibility label so it is independent of the per-card price element used for the
     * regular fare price ({@link #DISCOUNT_PRICE_SRP}).
     *
     * <p>The xpath additionally requires the {@code IDR} currency token so it does not collide
     * with non-monetary banners such as "Save more with the best promo" that also contain the
     * word "Save"; an index of {@code [1]} is used as a defensive guard for the case where
     * multiple priced cards are visible.</p>
     */
    public static final Locator SPECIAL_CAMPAIGN_SAVINGS_PRICE = new Locator("SPECIAL_CAMPAIGN_SAVINGS_PRICE",
            AppiumBy.xpath("(//*[@type='XCUIElementTypeStaticText' and (contains(@label, 'Save') or contains(@label, 'Hemat')) and contains(@label, 'IDR')])[1]"));
    public static final Locator SPECIAL_CAMPAIGN = new Locator("SPECIAL_CAMPAIGN",AppiumBy.xpath("//*[@label='Harga Gledek' and @type='XCUIElementTypeStaticText' and (./preceding-sibling::* | ./following-sibling::*)[@label=':']]"));
    public static final Locator PRICETAG = new Locator("PRICETAG",AppiumBy.xpath("//*[contains(@label,'%') or (contains(@label,'Disc') or contains(@label,'Diskon'))]"));
    public static final Locator DISCOUNT_OR_CAMPAIGN_PRICE = new Locator("DISCOUNT_OR_CAMPAIGN_PRICE",AppiumBy.xpath("//*[contains(@label,'Discount') or contains(@label,'Diskon') or (@label='Harga Gledek')]//following-sibling::*[contains(@label,'-IDR')and not(contains(@label,'Anti Galau'))]"));
    public static final Locator LOYALTY_WHAT_YOU_GET = new Locator("LOYALTY_WHAT_YOU_GET",AppiumBy.xpath("//*[contains(@label,'Elite') and (./preceding-sibling::* | ./following-sibling::*)[contains(@label,'ll Get') or contains(@label,'Kamu Dapatkan')]]"));
    public static final Locator HARGA_GLEDEK_WHAT_YOU_GET = new Locator("HARGA_GLEDEK_WHAT_YOU_GET",AppiumBy.xpath("//*[contains(@label,'Harga Gledek') and (./preceding-sibling::* | ./following-sibling::*)[contains(@label,'ll Get') or contains(@label,'Kamu Dapatkan')]]"));
    public static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE",AppiumBy.xpath("//*[@label='Total']//following-sibling::*[contains(@label,'IDR')]"));
    public static final Locator PRICE_ADULT = new Locator("PRICE_ADULT",AppiumBy.xpath("//*[contains(@label,'Adult') or contains(@label,'Dewasa')]//following-sibling::*[contains(@label,'IDR')]"));
    public static final Locator ICON_ON_THE_RIGHT_OF_TOTAL_PAYMENT = new Locator("ICON_ON_THE_RIGHT_OF_TOTAL_PAYMENT",AppiumBy.xpath("(//XCUIElementTypeStaticText[@label='Total']//following-sibling::*//XCUIElementTypeImage[not(@label)])[1]"));
    public static final Locator SEE_DETAILS = new Locator("SEE_DETAILS",AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and (contains(@name, 'See details') or contains(@name, 'Lihat detail'))]"));
    public static final Locator SEE_MORE_FILTER_BTN = new Locator("SEE_MORE_FILTER_BTN",AppiumBy.id("btn_see_more_filterAirline"));
    public static final Locator SAVE_AIRLINE_FILTER = new Locator("SAVE_AIRLINE_FILTER",AppiumBy.xpath("(//XCUIElementTypeOther/XCUIElementTypeButton[contains(@label,'Save') or contains(@label,'Simpan')])"));
    public static final Locator FILTER_AIRLINE = new Locator("FILTER_AIRLINE",AppiumBy.xpath("(//XCUIElementTypeStaticText[@label='Airlines' or @label='Maskapai'])[1]"));
    public static final Locator CLOSE_BTN = new Locator("CLOSE_BTN",AppiumBy.id("ic cancel grey"));
    public static final Locator SEE_MORE_FILTER_CLOSE_BTN = new Locator("SEE_MORE_FILTER_CLOSE_BTN",AppiumBy.xpath("//*[contains(@name,'ic cancel grey') and @visible='true']"));
    public static final Locator FILTER_AIRLINE_PARENT = AIRLINE_FILTER_SHEET_TABLE;
    public static final Locator FILTER_AIRLINE_TITLE = new Locator("FILTER_AIRLINE_TITLE", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='Maskapai' or @name='Airlines'])[last()]"));
    public static final Locator LABEL_100_P_REFUND = new Locator("LABEL_100_P_REFUND",AppiumBy.xpath("//XCUIElementTypeButton[@label='Detail' or @label='Details']//preceding-sibling::*[@label='Bisa 100% Refund' or @label='100% Refund Option']"));
    public static final Locator MEGAPHONE_ICON_SRP = new Locator("MEGAPHONE_ICON_SRP",AppiumBy.id("banner_container"));
    /**
     * Horizontal scrollview that wraps the SRP filter chip strip — present <em>only</em> when the
     * chips overflow the viewport. When the chips fit on screen iOS renders them as plain
     * {@code XCUIElementTypeOther} siblings (no scrollview). We therefore scope this locator to a
     * scrollview that actually contains a {@code filter_chips_*} node so we don't accidentally
     * match the price-by-date calendar or any other unrelated scrollview on the page.
     */
    public static final Locator FILTER_CHIPS_BODY = new Locator("FILTER_CHIPS_BODY",AppiumBy.xpath("(//XCUIElementTypeOther[.//*[contains(@name,'filter_chips_')]])[last()]"));
    // Use XPath rather than NSPredicate: the chips are published with accessible="false", and
    // NSPredicate / accessibilityId queries walk the accessibility hierarchy which excludes
    // non-accessible nodes. XPath traverses the raw element tree and reaches them.
    public static final Locator CHIPS_100_REFUND_RESCHEDULE = new Locator("CHIPS_100_REFUND_RESCHEDULE",AppiumBy.xpath("//*[@name='filter_chips_100_REFUND_RESCHEDULE' or contains(@label,'100% Refund Reschedule') or contains(@name,'100% Refund Reschedule')]"));
    public static final Locator PRICE_SELECTED_DATE = new Locator("PRICE_SELECTED_DATE",AppiumBy.xpath("//XCUIElementTypeCollectionView//XCUIElementTypeStaticText[last()]"));
    public static final Locator PRICES = new Locator("PRICES",AppiumBy.name("price"));
    public static final Locator SELECTED_DATE = new Locator("SELECTED_DATE",AppiumBy.xpath("(//XCUIElementTypeCollectionView//XCUIElementTypeStaticText[1])[3]"));
    public static final Locator FLAG_LOAD = new Locator("FLAG_LOAD", AppiumBy.accessibilityId("change_btn"));
    public static final Locator FLIGHT_DETAIL_EXPAND = new Locator("FLIGHT_DETAIL_EXPAND",AppiumBy.xpath("(//*[contains(@name,'show_detail')])[1]"));
    public static final Locator FLIGHT_CARD_CAMPAIGN = new Locator("FLIGHT_CARD_CAMPAIGN",AppiumBy.xpath("//*[@type='XCUIElementTypeCell' and ./*[@name='show_detail_2']]"));
    /**
     * First selectable SRP card. New UI drops {@code card_*_price} names — cells expose price
     * ({@code IDR}/{@code Rp}) plus either airline•equipment / transit bullet ({@code • }) or a
     * Direct/Langsung label (some domestic cards omit the bullet).
     */
    private static final String FLIGHT_CARD_MARKERS =
            "contains(@name,' • ') or contains(@label,' • ') "
                    + "or @name='Direct' or @label='Direct' or @name='Langsung' or @label='Langsung' "
                    + "or contains(@name,'transit') or contains(@label,'transit') "
                    + "or contains(@name,'Transit') or contains(@label,'Transit')";
    private static final String FLIGHT_CARD_CURRENCY =
            "contains(@name,'MYR') or contains(@label,'MYR') or contains(@name,'THB') or contains(@label,'THB') "
                    + "or contains(@name,'SGD') or contains(@label,'SGD') or contains(@name,'IDR') or contains(@label,'IDR') "
                    + "or starts-with(@name,'Rp') or starts-with(@label,'Rp')";
    private static final String FLIGHT_CARD_EXCLUDE_SCHEDULE =
            "not(.//XCUIElementTypeStaticText[contains(@name,'Depart') or contains(@label,'Depart') "
                    + "or contains(@name,'Berangkat') or contains(@label,'Berangkat')])";
    public static final Locator FLIGHT_CARD = new Locator("FLIGHT_CARD", AppiumBy.xpath(
            "//*[contains(@name,'flight_card')]"));
    /** Legacy accessibility ids {@code card_*_price} on older builds. */
    private static final Locator FLIGHT_CARD_LEGACY = new Locator("FLIGHT_CARD_LEGACY", AppiumBy.xpath("(//*[contains(@name,'card') and contains(@name,'price')])[1]"));
    public static final Locator FLOATING_ICON_SRP = new Locator("FLOATING_ICON_SRP",AppiumBy.accessibilityId("flightInfoButton"));
    public static final Locator BACK_TO_SRP = new Locator("BACK_TO_SRP",AppiumBy.xpath("//*[@name='back']"));
    public static final Locator FLIGHT_TRANSIT_FILTER = new Locator("FLIGHT_TRANSIT_FILTER",AppiumBy.xpath("//*[contains(@name,'bottomActionItem_transit')]"));
    public static final Locator FLIGHT_TRANSIT_FILTER_INFINITY = new Locator("FLIGHT_TRANSIT_FILTER_INFINITY",AppiumBy.xpath("//XCUIElementTypeButton[@name='transit']"));
    public static final Locator FLIGHT_SORT_FILTER = new Locator("FLIGHT_SORT_FILTER", AppiumBy.xpath("//*[contains(@name,'bottomActionItem_sort')]"));
    public static final Locator FLIGHT_CARD_FIRST_PRICE = new Locator("FLIGHT_CARD_FIRST_PRICE", AppiumBy.xpath(
            "(//XCUIElementTypeTable//XCUIElementTypeCell[.//XCUIElementTypeStaticText[" + FLIGHT_CARD_MARKERS + "] "
                    + "and " + FLIGHT_CARD_EXCLUDE_SCHEDULE + "]"
                    + "//XCUIElementTypeStaticText[(contains(@name,'IDR') or contains(@label,'IDR') or starts-with(@name,'Rp') or starts-with(@label,'Rp')) "
                    + "and not(contains(@name,'/pax') or contains(@label,'/pax'))])[1]"));
    public static final Locator ROUND_TRIP_DEAL_LABEL = new Locator("ROUND_TRIP_DEAL_LABEL", AppiumBy.xpath("//*[@name='show_detail_2']//following-sibling::*[contains(@label,'Round-trip Deals')]"));
    /** Delta-desc anchor text (EN/ID). Reused by the before/after price locators below. */
    private static final String DELTA_DESC_XPATH = "//XCUIElementTypeStaticText[contains(@name,'If you return without Round-trip Deals flight') or contains(@label,'If you return without Round-trip Deals flight') or contains(@name,'Jika pulang tanpa penerbangan Round-trip Deals') or contains(@label,'Jika pulang tanpa penerbangan Round-trip Deals')]";
    /** Flight-card cell: airline•aircraft, transit bullet, or Direct/Langsung. */
    private static final String CARD_CELL_PREDICATE = "XCUIElementTypeCell[.//XCUIElementTypeStaticText[" + FLIGHT_CARD_MARKERS + "]]";
    /** Card price text: starts with a currency code/prefix (avoids matching "Departure flight +IDR ..."). */
    private static final String CARD_PRICE_PREDICATE = "XCUIElementTypeStaticText[starts-with(@name,'IDR') or starts-with(@label,'IDR') or starts-with(@name,'MYR') or starts-with(@label,'MYR') or starts-with(@name,'SGD') or starts-with(@label,'SGD') or starts-with(@name,'Rp') or starts-with(@label,'Rp')]";
    // First price of the last flight card that appears before the delta description. preceding::/following::
    // axes are used (instead of parent/sibling chains) so the locator survives cell re-nesting.
    public static final Locator LAST_CARD_PRICE_BEFORE_DELTA_DESC = new Locator("LAST_CARD_PRICE_BEFORE_DELTA_DESC", AppiumBy.xpath("((" + DELTA_DESC_XPATH + "/preceding::" + CARD_CELL_PREDICATE + ")[last()]//" + CARD_PRICE_PREDICATE + ")[1]"));
    public static final Locator DELTA_PRICE_DESCRIPTION = new Locator("DELTA_PRICE_DESCRIPTION", AppiumBy.xpath(DELTA_DESC_XPATH));
    public static final Locator FIRST_CARD_PRICE_AFTER_DELTA_DESC = new Locator("FIRST_CARD_PRICE_AFTER_DELTA_DESC", AppiumBy.xpath("((" + DELTA_DESC_XPATH + "/following::" + CARD_CELL_PREDICATE + ")[1]//" + CARD_PRICE_PREDICATE + ")[1]"));
    private static final Locator TRANSIT_FILTER_DIRECT = new Locator("TRANSIT_FILTER_DIRECT",AppiumBy.xpath("//*[@name='Direct']//*[@label='Direct']"));
    private static final Locator TRANSIT_FILTER_TRANSIT = new Locator("TRANSIT_FILTER_TRANSIT",AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@name,'1 Transit') or contains(@name,'+ Transit')])[1]"));
    private static final Locator TRANSIT_1_FILTER_TRANSIT = new Locator("TRANSIT_1_FILTER_TRANSIT",AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'1 Transit')]"));
    private static final Locator TRANSIT_2_FILTER_TRANSIT = new Locator("TRANSIT_2_FILTER_TRANSIT",AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'2+ Transit')]"));
    public static final Locator JOIN_NOW = new Locator("JOIN_NOW", AppiumBy.id("JoinOfferSearchResult"));
    public static final Locator ECONOMY_CLASS_TITLE_SUB_HEADER = new Locator("ECONOMY_CLASS_TITLE_SUB_HEADER", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'economy') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'ekonomi')]"));
    public static final Locator CHANGE_SEARCH_BTN_SRP = new Locator("ECONOMY_CLASS_TITLE_SUB_HEADER", AppiumBy.xpath("//XCUIElementTypeButton[@name='Change Search' or @name='Ubah Pencarian']"));
    public static final Locator PREMIUM_ECONOMY_CLASS_TITLE_SUB_HEADER = new Locator("PREMIUM_ECONOMY_CLASS_TITLE_SUB_HEADER", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'premium economy') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'premium ekonomi')]"));
    public static final Locator MEET_TRAVEL_POLICY_1 = new Locator("MEET_TRAVEL_POLICY_1", AppiumBy.xpath("//*[contains(@name,'flight_card')]//*[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'meets travel policy') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sesuai kebijakan')]"));
    public static final Locator AIRLINE_FILTER_TITLE = new Locator("AIRLINE_FILTER_TITLE", AppiumBy.xpath("//*[contains(@label,'filter_bottom_checkbox')]"));
    public static final Locator APPLY_PROMO_INFINITY = new Locator("APPLY_PROMO_INFINITY", AppiumBy.xpath("//XCUIElementTypeButton[@name='Apply' or @name='Pakai']"));
    public static final Locator CANCEL_PROMO_INFINITY = new Locator("CANCEL_PROMO_INFINITY", AppiumBy.xpath("//XCUIElementTypeButton[@name='Cancel' or @name='Batalkan']"));
    public static final Locator PROMO_APPLIED_TEXT = new Locator("PROMO_APPLIED_TEXT", AppiumBy.xpath("//*[contains(@label,'Promo terbaik terpasang. Pilihan bagus!') or contains(@label,'Best promo applied. Good choice!')]"));
    public static final Locator AIRLINE_COUNT = new Locator("AIRLINE_COUNT", AppiumBy.xpath("//*[@name='see_flights']//XCUIElementTypeStaticText"));
    public static final Locator PROGRESS_BAR_SRP = new Locator("PROGRESS_BAR_SRP", AppiumBy.className("XCUIElementTypeProgressIndicator"));
    public static final Locator CASHBACK_LABEL = new Locator("CASHBACK_LABEL", AppiumBy.xpath("//*[@label='After cashback']"));
    public static final Locator SAVE_LABEL = new Locator("SAVE_LABEL", AppiumBy.xpath("//*[contains(@label,'Save')]"));
    private static final Locator SRP_NUDGES = new Locator("SRP_NUDGES", AppiumBy.xpath("//XCUIElementTypeStaticText[(contains(@name,'fly with') or contains(@label,'fly with') or contains(@value,'fly with') or contains(@name,'Fly with') or contains(@label,'Fly with') or contains(@value,'Fly with') or contains(@name,'Terbang dengan') or contains(@label,'Terbang dengan') or contains(@value,'Terbang dengan'))]"));
    private static final Locator SELECT_TICKET_FROM_DETAIL_PAGE = new Locator("SELECT_TIKET_FROM_DETAIL_PAGE", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and @name='Select Ticket' or @name='Pilih Tiket']"));

    private static Map<String, Boolean> FLAG_MAP = new HashMap<>();

    private Locator getDateLocator(String date){
        return new Locator("Date Locator",AppiumBy.xpath("(//*[contains(@label,'"+date+"')])[1]"));
    }

    private String changeMonthAccordingToLanguage(String month){
        WebElement changeBtnElement = driverTools.waitForElementToAppear(CHANGE).orElseThrow();
        String changeBtnText = driverTools.getText(changeBtnElement);
        if(changeBtnText.equalsIgnoreCase("ubah"))
            return CoreFunctions.monthEngToId(month);
        return month;
    }

    private Swipe getDirection(String dateToSelect){
        WebElement selectedDateElement = driverTools.waitForElementToAppear(SELECTED_DATE, 20).orElseThrow();
        String selectedDate = selectedDateElement.getText().split(", ")[1];

        logger.log("date to select : " + dateToSelect);
        logger.log("selected date : " + selectedDate);

        String[] dateToSelectArr = dateToSelect.split(" ");
        int dateToSelectDay = Integer.parseInt(dateToSelectArr[0]);
        String dateToSelectMonth = dateToSelectArr[1];
        int dateToSelectYear = Integer.parseInt(dateToSelectArr[2]);

        String[] selectedDateArr = selectedDate.split(" ");
        int selectedDateDay = Integer.parseInt(selectedDateArr[0]);
        String selectedDateMonth = selectedDateArr[1];
        int SelectedDateYear = Integer.parseInt(selectedDateArr[2]);

        int selectedMonthNum = CoreFunctions.monthToMonthNumber(selectedDateMonth);
        logger.log("selected month number: " + selectedMonthNum);
        int dateToSelectMonthNum = CoreFunctions.monthToMonthNumber(dateToSelectMonth);
        logger.log("date to select month number: " + dateToSelectMonthNum);

        int yearDif = dateToSelectYear - SelectedDateYear;
        if(yearDif == 0){
            int monthDif = dateToSelectMonthNum - selectedMonthNum;
            if(monthDif == 0){
                return dateToSelectDay - selectedDateDay < 0 ? Swipe.RIGHT: Swipe.LEFT;
            }
            return monthDif < 0 ? Swipe.RIGHT : Swipe.LEFT;
        }
        return yearDif < 0 ? Swipe.RIGHT : Swipe.LEFT;
    }

    @Override
    public void changeDate(String date){
        Swipe direction = getDirection(date);
        logger.log("direction : " + direction);

        String[] dateSplit = date.split(" ");
        String month = changeMonthAccordingToLanguage(dateSplit[1]).substring(0,3);
        logger.log("month code -> " + month);
        String formattedDate = dateSplit[0] + " " + month + " " + dateSplit[2];
        driverTools.swipeToElement(getDateLocator(formattedDate),DATE_LIST, direction,13).orElseThrow();
        driverTools.clickElementByCoordinate(getDateLocator(formattedDate));
        checkReloadPopup();
    }

    @Override
    public void changeTrip(String tripType) {
        driverTools.waitForElementToAppear(FLIGHT_DETAIL_EXPAND);
        try {
            driverTools.click(CHANGE);
        } catch (Exception e) {
            logger.log("Failed to click the old CHANGE button: " + e.getMessage());
            logger.log("Clicking the new CHANGE SEARCH button instead.");
            driverTools.click(CHANGE_SEARCH_BUTTON);
        }
        if (tripType.equals("roundtrip")){
            driverTools.waitForElementToAppear(TOGGLE_TRIP).ifPresent(driverTools::click);
        } else{
            driverTools.waitForElementToAppear(TOGGLE_TRIP_ACTIVE).ifPresent(driverTools::click);
        }
        driverTools.click(FLIGHT_SEARCH_BUTTON);
        checkReloadPopup();
    }

    @Override
    public void checkReloadPopup(){
        if (Configs.ENVIRONMENT == Environment.GK) {
            //these popups happen when there are flight adjustments, usually on GK
            for(int tries = 5; tries>0 && driverTools.waitForElementToAppear(RELOAD,10).isPresent(); tries--){
                driverTools.click(RELOAD);
            }
        }
    }

    /**
     * Airlines bottomsheet option row for {@code airline}, matched by name: any element under a
     * {@code filter_group_checkbox_*} container whose label contains the airline name, ignoring case
     * (e.g. "AirAsia Indonesia (7)" matches "airasia indonesia").
     * See {@link #getAirlineFilterOptionByCodeLocator(String)} for the code-based locator.
     */
    private Locator getAirlineFilterOptionLocator(String airline) {
        String key = airline.toLowerCase(Locale.ROOT).trim();
        String keyLit = xpathStringLiteralForContains(key);
        String labelLocator = "//*[contains(@name,'filter_group_checkbox')]//*[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),"+keyLit+")]" +
            "| //*[@name='checkbox']/following-sibling::*[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),"+keyLit+") and @visible='true']";
        return new Locator("Airline Locator Filter", AppiumBy.xpath(labelLocator));
    }

    /**
     * Airlines bottomsheet option row for {@code airline}, matched by its {@code filter_group_checkbox_<code>}
     * accessibility id. The code is resolved from the airline name via {@link #resolveAirlineFilterCode(String)}.
     */
    private Locator getAirlineFilterOptionByCodeLocator(String airline) {
        String code = resolveAirlineFilterCode(airline);
        if (code == null) {
            throw new IllegalArgumentException("No airline filter code mapped for airline: " + airline);
        }
        return new Locator("Airline Filter Option " + code,
                AppiumBy.xpath("//*[@name='filter_group_checkbox_" + code + "']"));
    }

    private String resolveAirlineFilterCode(String normalizedAirlineName) {
        if (normalizedAirlineName == null || normalizedAirlineName.isBlank()) {
            return null;
        }
        String key = normalizedAirlineName.toLowerCase(Locale.ROOT).trim();
        if (AIRLINE_NAME_TO_FILTER_CODE.containsKey(key)) {
            return AIRLINE_NAME_TO_FILTER_CODE.get(key);
        }
        String bestMatch = null;
        int bestLen = 0;
        for (Map.Entry<String, String> entry : AIRLINE_NAME_TO_FILTER_CODE.entrySet()) {
            String fragment = entry.getKey();
            if ((key.contains(fragment) || fragment.contains(key)) && fragment.length() > bestLen) {
                bestMatch = entry.getValue();
                bestLen = fragment.length();
            }
        }
        return bestMatch;
    }

    private boolean isAirlineFilterOptionOnScreen(Locator airlineOption) {
        return driverTools.waitForElementToPresence(airlineOption).isPresent();
    }

    private void scrollAirlineFilterSheetOnce() {
        if (driverTools.isElementDisplayed(AIRLINE_FILTER_SHEET_TABLE)) {
            driverTools.swipeUp(AIRLINE_FILTER_SHEET_TABLE);
        } else {
            driverTools.swipeUp();
        }
    }

    private void clickAirlineFilterTab() {
        if (driverTools.isElementDisplayed(FILTER_OLD)) {
            driverTools.click(FILTER_OLD);
        } else if (driverTools.isElementDisplayed(FILTER)) {
            driverTools.click(FILTER);
        } else if (driverTools.isElementDisplayed(FILTER_AIRLINE_BOTTOM_ACTION)) {
            driverTools.click(FILTER_AIRLINE_BOTTOM_ACTION);
        }else if (driverTools.isElementDisplayed(FILTER_ICON)) {
            driverTools.click(FILTER_ICON);
        } else {
            throw new NoSuchElementException("Airlines filter tab not found (airline / bottomActionItem_airline)");
        }
    }

    private void waitForAirlineFilterSheet() {
        driverTools.waitForElementToAppear(AIRLINE_FILTER_SHEET_READY, 15).orElseThrow();
    }


    @Override
    public void filterAirline(ArrayList<String> airlines) {
        driverTools.waitForElementToAppear(CLOSE_BTN).orElseThrow();
        this.resetFilter();

        Map<String, Boolean> airlineCount = new HashMap<>();
        Collections.sort(airlines);
        int retry = AIRLINE_FILTER_MAX_SCROLLS;
        while (retry > 0 && airlineCount.size() != airlines.size()) {
            for (String airline : airlines){
                String formattedAirline = airline;
                if (formattedAirline.contains("nam")) formattedAirline = formattedAirline.replace("Nam", "NAM");
                Locator airlineName = getAirlineFilterOptionLocator(formattedAirline);
                if(isAirlineFilterOptionOnScreen(airlineName)){
                    String key = formattedAirline.toLowerCase();
                    if(!airlineCount.containsKey(key)){
                        WebElement element = driverTools.waitForElementToPresence(airlineName).orElseThrow();
                        driverTools.click(element);
                        logger.log("airline[ name: " + key + ",isPresent: " + true + " ]");
                        airlineCount.put(key,true);
                    }

                }
            }

            if (airlineCount.size() == airlines.size()){
                break;
            }
            scrollAirlineFilterSheetOnce();
            retry--;
        }

        if(airlineCount.isEmpty()){
            throw new java.util.NoSuchElementException("required airline is not present " + airlines);
        }
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
            waitForAirlineFilterSheet();
            resetFilter();

            airlines.sort(String::compareToIgnoreCase);

            for (int scroll = 0; scroll < AIRLINE_FILTER_MAX_SCROLLS; scroll++) {
                for (String airline : airlines) {
                    Locator airlineOption = getAirlineFilterOptionLocator(airline);
                    if (isAirlineFilterOptionOnScreen(airlineOption)) {
                        driverTools.click(airlineOption);
                        logger.log("Airline selected: " + airline + " (A–Z)");
                        return airline;
                    }
                }
                if (scroll < AIRLINE_FILTER_MAX_SCROLLS - 1) {
                    scrollAirlineFilterSheetOnce();
                }
            }

            throw new NoSuchElementException(
                    "No airline from the provided list is available after " + AIRLINE_FILTER_MAX_SCROLLS
                            + " scrolls. Tried airlines (A–Z): " + airlines
            );

        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void openFilter() {
        String methodName = "openFilter";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            if (driverTools.waitForElementToAppear(FLIGHT_CARD, 15).isEmpty()) {
                driverTools.waitForElementToAppear(FLIGHT_CARD_LEGACY, 10);
            }
            clickAirlineFilterTab();
            driverTools.waitForElementToAppear(AIRLINE_FILTER_SHEET_READY, 10).orElseThrow();
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void openAdvancedFilter() {
        String methodName = "openAdvancedFilter";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            WebElement filterElement = driverTools.waitForElementToAppear(MASTER_FILTER, 15).orElseThrow();
            driverTools.click(filterElement);
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

    @Override
    public void filterMorningFlight() {
       //fixme implementation pending
    }

    @Override
    public void saveFilter() {
        String methodName = "saveFilter";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            Locator saveButton = resolveSaveAirlineFilterButton();
            driverTools.click(saveButton);
            driverTools.waitForElementToDisappear(AIRLINE_FILTER_SHEET_READY, 5);
            if (driverTools.waitForElementToAppear(FLIGHT_CARD, 15).isEmpty()) {
                driverTools.waitForElementToAppear(FLIGHT_CARD_LEGACY, 10).orElseThrow();
            }
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    private Locator resolveSaveAirlineFilterButton() {
        if (driverTools.isElementDisplayed(SAVE_AIRLINE)) {
            return SAVE_AIRLINE;
        }
        if (driverTools.isElementDisplayed(SAVE_AIRLINE_FILTER)) {
            return SAVE_AIRLINE_FILTER;
        }
        if (driverTools.waitForElementToAppear(SAVE_AIRLINE, 5).isPresent()) {
            return SAVE_AIRLINE;
        }
        if (driverTools.waitForElementToAppear(SAVE_AIRLINE_FILTER, 8).isPresent()) {
            return SAVE_AIRLINE_FILTER;
        }
        throw new NoSuchElementException("Save airline filter button not found");
    }

    private Locator otherFilterLOcator(String filterType){
        return new Locator("Other Filter Locator",AppiumBy.xpath("//*[contains(@name,'"+filterType+"') and @visible='true']"));
    }

    private void openMasterFilter(){
        driverTools.waitForElementToAppear(MASTER_FILTER,15);
        driverTools.click(MASTER_FILTER);
        driverTools.waitForElementToPresence(CLOSE_BTN);
    }

    private void filterOtherType(String filterType){
        driverTools.swipeToElement(OTHER_FILTER_TITLE, Swipe.UP,10).orElseThrow();
        driverTools.swipeToElement(CRP_MASTER_FILTER, Swipe.UP,1);
        if(driverTools.isElementDisplayed(CRP_MASTER_FILTER))
            driverTools.click(CRP_MASTER_FILTER);
        else {
            driverTools.swipeDown();
            driverTools.swipeToElement(SEE_ALL_OTHER_FILTER, Swipe.UP,1).orElseThrow();
            driverTools.click(SEE_ALL_OTHER_FILTER);
            driverTools.waitForElementToPresence(CLOSE_BTN);
            Locator otherFilter = otherFilterLOcator(filterType);
            driverTools.click(otherFilter);
            driverTools.click(SAVE_SEE_ALL_OTHER_FILTER);
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
        boolean promoPage = driverTools.waitForElementToAppear(PROMO_WEBVIEW,10).isPresent();
        driverTools.click(PROMO_CLOSE);
        return promoPage;
    }

    @Override
    public boolean isTravelInfoPagePresent() {
        logger.log("Verifying travel info page");
        driverTools.click(TRAVEL_INFO);
        driverTools.waitForElementToAppear(INFOPAGE_BACK);
        boolean infoPage = driverTools.waitForElementToAppear(TRAVEL_INFO_DETAIL,10).isPresent();
        driverTools.click(INFOPAGE_BACK);
        return infoPage;
    }

    @Override
    public void selectFlight() {
        String methodName = "selectFlight";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.swipeDown();
            driverTools.waitForElementToPresence(CLOSED,2);
            driverTools.waitForElementToDisappear(CLOSED,3);
            Locator card = resolveFirstFlightCard();
            // After saveFilter, bring first card on-screen before tap (presence ≠ visible mid-viewport).
            driverTools.swipeToPresentedElement(card, Swipe.DOWN, 5).orElseThrow();
            // Prefer fare text on the row — whole-cell centre often lands on an overlapping SRP banner.
            if (card == FLIGHT_CARD && driverTools.waitForElementToPresence(FLIGHT_CARD_FIRST_PRICE, 5).isPresent()) {
                driverTools.clickElementByCoordinate(FLIGHT_CARD_FIRST_PRICE);
            } else {
                driverTools.clickElementByCoordinate(card);
            }
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    private Locator resolveFirstFlightCard() {
        if (driverTools.waitForElementToPresence(FLIGHT_CARD, 15).isPresent()) {
            return FLIGHT_CARD;
        }
        logger.log("[selectFlight] new FLIGHT_CARD not found — falling back to card_*_price legacy");
        driverTools.waitForElementToPresence(FLIGHT_CARD_LEGACY, 10).orElseThrow();
        return FLIGHT_CARD_LEGACY;
    }

    @Override
    public void selectFlightSpecialCampaign(String airline) {
        String methodName = "selectFlight";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForElementToPresence(CLOSED,2);
            driverTools.waitForElementToDisappear(CLOSED,3);
            Locator selectAirline = getAirlineLocator(airline);
            driverTools.swipeToElement(selectAirline, Swipe.DOWN, 5);
            driverTools.waitForElementToPresence(selectAirline).orElseThrow();
            driverTools.click(selectAirline);
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    private Locator getAirlineLocator(String airline){
        return new Locator("Airline",AppiumBy.xpath("(//*[@type='XCUIElementTypeStaticText' and contains(@label, '" + airline + "')])[1]"));
    }

    @Override
    public void selectFlightVI(String airline){
        driverTools.swipeToElement(getViAirlineLocator(airline),Swipe.UP,6);
        driverTools.click(getViAirlineLocator(airline));
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
        String month = changeMonthAccordingToLanguage(dateSplit[1]).substring(0,3);
        logger.log("month code -> " + month);
        String expectedDate = dateSplit[0] + " " + month + " " + dateSplit[2];
        Assertion.assertThat("is selected date today",selectedDate,is(containsString(expectedDate)));
    }

    @Override
    public void selectFlightStopOver(){
        //fixme implementation pending ios -> missing unique id
    }

    @Override
    public boolean isImportantInfoPagePresent() {
        logger.log("Verifying important info page");
        driverTools.click(IMPORTANT_MESSAGE);
        driverTools.waitForElementToAppear(IMPORTANT_MESSAGE_CLOSE);
        boolean infoPage = driverTools.waitForElementToAppear(IMPORTANT_MESSAGE_WEBVIEW,10).isPresent();
        driverTools.click(IMPORTANT_MESSAGE_CLOSE);
        return infoPage;
    }//FIXME: IMPLEMENTATION PENDING

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
        driverTools.waitForElementToAppear(PRICE);
        driverTools.click(PRICE);

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
        boolean info = false;
        if(policyType.equals("refund")){
            tab = REFUND_TAB;
        } else if(policyType.equals("reschedule")) {
            tab = RESCHEDULE_TAB;
        } else{
            throw new NotImplementedException("Not implemented policy");
        }
        driverTools.click(tab);
        if (policyType == "refund") {
            info = driverTools.isElementDisplayed(REFUND_INFO);
        } else if (policyType == "reschedule") {
            info = driverTools.isElementDisplayed(RESCHEDULE_INFO);
        }
        return info;
    }
    @Override
    public boolean isPriceBreakdownCorrect(int adult, int child, int infant) {
        logger.log("Verifying price detail");

        boolean adultBreakdown = isBreakdownDisplayed("adult",adult);
        boolean childBreakdown = isBreakdownDisplayed("child",child);
        boolean infantBreakdown = isBreakdownDisplayed("infant",infant);

        return adultBreakdown && childBreakdown && infantBreakdown;
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
    public String getDepartureDate() {
        return "";
    }//FIXME: IMPLEMENTATION PENDING

    @Override
    public String getDepartureAirport() {
        return "";
    }//FIXME: IMPLEMENTATION PENDING

    @Override
    public String getArrivalDate() {
        return "";
    }//FIXME: IMPLEMENTATION PENDING

    @Override
    public String getArrivalAirport() {
        return "";
    }//FIXME: IMPLEMENTATION PENDING

    @Override
    public void selectTicket() {
        driverTools.swipeToElement(SELECT_TICKET, Swipe.UP, 5);
        driverTools.waitForElementToBeClickable(SELECT_TICKET);
        driverTools.clickElementByCoordinate(SELECT_TICKET);
    }

    @Override
    public boolean isLowestPriceGuaranteePagePresent() {
        return true;
    }//FIXME: IMPLEMENTATION PENDING

    private Locator getPriceBreakdownLocator(String type, int amount){
        switch (type.toLowerCase()) {
            case "harga gledek" -> {
                return new Locator("PRICE AND POLICY " + type.toUpperCase() + " PRICE: ", AppiumBy.xpath("//*[@name='price_detail_info' and contains(@label,'"+type+"')]//following-sibling::*[@name='price_detail_price']"));
            }
            default -> throw new NotImplementedException("Unimplemented price locator type!");
        }
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
                return TRANSIT_1_FILTER_TRANSIT;
            }
            case "2 transit":{
                return TRANSIT_2_FILTER_TRANSIT;
            }
            default: throw new NotImplementedException("Unimplemented transit type!");
        }
    }

    @Override
    public void filterTransitType(String type) {
        WebElement e = driverTools.swipeToPresentedElement(getTransitTypeLocator(type),Swipe.UP,5).orElseThrow();
        driverTools.click(e);
    }

    private Locator getOtherFilterTypeLocator(String type){
        if(type.contains("100")){
            return new Locator(type + " Filter", AppiumBy.accessibilityId("filter_chips_100_REFUND"));
        }
        if(type.contains("Free")){
            return new Locator(type + " Filter", AppiumBy.accessibilityId("filter_chips_FLEXI"));
        }
        if(type.contains("Round")){
            return new Locator(type + " Filter", AppiumBy.name("Round-trip Deals"));
        }
        return new Locator(type + " Filter", AppiumBy.xpath("//*[contains(@label,'"+type+"')]"));
    }
    @Override
    public void filterChips(String type) {
        if(type.equals("100% Refund")){
            Locator Refund_100 = new Locator("100% Refund Filter", AppiumBy.xpath("//*[contains(@label,'"+type+"')]"));
            driverTools.waitForElementToAppear(Refund_100,15);
            driverTools.click(Refund_100);
            return;
        }
        driverTools.waitForElementToAppear(FLIGHT_DETAIL_EXPAND,15);
        if(driverTools.isElementDisplayed(CHIPS_100_REFUND_RESCHEDULE) && type.contains("100")){
            driverTools.click(CHIPS_100_REFUND_RESCHEDULE);
        }else {
            Locator chipLocator = getOtherFilterTypeLocator(type);
            if(driverTools.isElementVisible(chipLocator))
                driverTools.click(chipLocator);
             else {
                logger.log("filter chip not visible, swiping to find the chip");
                WebElement chips = driverTools.swipeToPresentedElement(chipLocator, FILTER_CHIPS_BODY, Swipe.LEFT, 8).orElseThrow();
                driverTools.click(chips);
            }
        }
    }

    @Override
    public boolean isFreeTransfer(){
        boolean isFreeTransferPresent = driverTools.waitForElementToAppear(FREE_TRANSFER).orElseThrow().isDisplayed();
        return isFreeTransferPresent;
    }
    @Override
    public boolean isTest(){
        boolean isFreeTransferPresent = driverTools.waitForElementToAppear(TEST).orElseThrow().isDisplayed();
        return isFreeTransferPresent;
    }

    @Override
    public boolean isFreeTransferDetail(){
        WebElement freeTransferDetail = driverTools.swipeToElement(FREE_TRANSFER_DETAIL,Swipe.UP,3).orElseThrow();
        boolean isFreeTransferPresentDetail = freeTransferDetail.isDisplayed();
        return isFreeTransferPresentDetail;
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
        driverTools.swipeToElement(SEE_ORIGIN_DETAILS, Swipe.UP, 5);
        driverTools.waitForElementToAppear(SEE_ORIGIN_DETAILS);
        driverTools.click(SEE_ORIGIN_DETAILS);
        boolean isAirportTransferDetail = driverTools.waitForElementToAppear(AIRPORT_TRANSFER_DETAIL).isPresent();
        Assertion.assertThat("Airport Transfer Detail is present on return flight ", isAirportTransferDetail, is(true));
        boolean isAntigenTestDetail = driverTools.waitForElementToAppear(ANTIGEN_TEST_DETAIL).isPresent();
        Assertion.assertThat("Antigen Test Detail is present on return flight ", isAntigenTestDetail, is(true));
        driverTools.click(CLOSE_BTN_PRP);
        return isAirportTransferDetail && isAntigenTestDetail;
    }

    @Override
    public boolean isFreeTransferDetailOneWay(){
        WebElement freeTransferDetailOneWay = driverTools.waitForElementToAppear(FREE_TRANSFER_DETAIL_SRP).orElseThrow();
        return freeTransferDetailOneWay.isDisplayed();
    }

    @Override
    public boolean isTestDetail(){
        WebElement testDetail = driverTools.swipeToElement(TEST_DETAIL,Swipe.UP,3).orElseThrow();
        boolean isFreeTransferPresentDetail = testDetail.isDisplayed();
        return isFreeTransferPresentDetail;
    }

    @Override
    public boolean isTestDetailOneWay(){
        WebElement testDetail = driverTools.waitForElementToAppear(TEST_DETAIL_SRP).orElseThrow();
        return testDetail.isDisplayed();
    }

    @Override
    public boolean verifyLoyaltyBanner(String loyaltyType){
        return driverTools.waitForElementToAppear(getLoyaltyBannerTitleLocator(loyaltyType)).isPresent();
    }

    @Override
    public boolean isCorporateDealsBannerPresent() {
        driverTools.swipeToElement(CORPORATE_DEALS_BANNER,Swipe.DOWN,8);
        boolean isBannerNotDisplayed = driverTools.waitForElementToPresence(CORPORATE_DEALS_BANNER).isPresent();
        if(!isBannerNotDisplayed){
            logger.log("swiping left to get banner");
            driverTools.swipeToElement(CORPORATE_DEALS_BANNER,CORPORATE_DEALS_BANNER_PARENT,Swipe.RIGHT,3).orElseThrow();
            isBannerNotDisplayed = driverTools.waitForElementToPresence(CORPORATE_DEALS_BANNER).isPresent();
        }
        return isBannerNotDisplayed;
    }

    @Override
    public boolean gotoCorporateDealsBenefitCard() {
        boolean benefitCard = driverTools.swipeToElement(CORPORATE_DEALS_BENEFIT_CARD, Swipe.UP, 2).isPresent();
        if (!benefitCard) driverTools.swipeToElement(CORPORATE_DEALS_BENEFIT_CARD, Swipe.DOWN, 2);
        return benefitCard;
    }

    @Override
    public boolean isTransitInfoPresentOnSrpCardDetail() {
        throw new NotImplementedException("isTransitInfoPresentOnSrpCardDetail not implemented for iOS");
    }

    private Locator getLoyaltyBannerTitleLocator(String loyaltyType){
        return new Locator("Banner Locator",AppiumBy.xpath("//*[@name='loyalty_title' and contains(@value,'"+loyaltyType+"')]"));
    }

    @Override
    public boolean verifyDiscountTag(){
        driverTools.waitForElementToPresence(FLIGHT_DETAIL_EXPAND).ifPresent(driverTools::click);
        Boolean res = driverTools.waitForElementToAppear(LOYALTY_DISC_UP_TO, 30).isPresent();
        driverTools.waitForElementToPresence(HIDE_DETAILS).ifPresent(driverTools::click);
        return res;
    }

    private Locator getDiscountTagLocator(String discountAmount){
        return new Locator("discount Amount",AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@label,'/pax')]//following-sibling::*[(contains(@value,'up to') or contains(@value,'hingga'))and contains(@value,'"+discountAmount+"')])[1]"));
    }

    @Override
    public double verifyDiscountPrice(String discType){
        String discountPrice = driverTools.getText(getDiscountPrice(discType)).orElseThrow();
        double price = CoreFunctions.convertPrice(discountPrice);
        logger.log("Discount price: "+price);
        return price;
    }

    private Locator getDiscountPrice(String loyaltyType){
        return new Locator("discount Locator",AppiumBy.xpath("(//*[contains(@name,'IDR') and @type='XCUIElementTypeStaticText'])[5]"));
    }

    @Override
    public boolean isPriceSumCorrect(){
        List<WebElement> prices = driverTools.elements(PRICE_BREAKDOWN).orElseThrow();
        logger.log("raw prices ->" + prices);

        ArrayList<Double> arrayPrice = new ArrayList<>();
        Double nextAfterFree = null;

        for (int i = 0; i < prices.size(); i++) {
            String text = driverTools.getText(prices.get(i));
            logger.log("price text -> " + text);

            try {
                double price = convertPrice(text);
                arrayPrice.add(price);
            } catch (NumberFormatException ignored) {}
        }

        //removing cashback or discount from the list
        arrayPrice.remove(arrayPrice.size() - 1);
        nextAfterFree = arrayPrice.get(arrayPrice.size() - 1);
        arrayPrice.remove(arrayPrice.size() - 1);
        double sum = arrayPrice.stream().mapToDouble(Double::doubleValue).sum();
        logger.log("arrayPrice: " + arrayPrice);
        logger.log("sum: " + sum);
        logger.log("Total amt: " + nextAfterFree);

        Assertion.assertThat("Total price calculation should be correct", nextAfterFree, is(sum));
        return true;
    }

    @Override
    public boolean isWhatYouGetCorrect(String label){
        boolean res = driverTools.swipeToElement(getWhatYouGetLocator(label),Swipe.UP,3).isPresent();
        driverTools.click(CLOSE_DETAIL);
        return res;
    }

    @Override
    public boolean isWhatYouGetCorrectSRP(String label) {
        return driverTools.swipeToElement(getWhatYouGetLocatorSRP(label),Swipe.UP,3).isPresent();
    }

    private Locator getWhatYouGetLocator(String label) {
        switch (label.toLowerCase()) {
            case "diamond", "platinum" -> {
                // Anchor to the full price-breakdown row label ("Elite Platinum Discount"/"Diskon"/"Benefit")
                // instead of just any StaticText containing the tier name. The previous "[2]th Platinum"
                // xpath was too loose: it matched hidden StaticText nodes still attached to the
                // accessibility tree (banner / collapsed sections), causing swipeToElement to keep
                // returning an undisplayed element and ultimately Optional.empty().
                return new Locator("Banner Locator",
                        AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and contains(@label,'Elite') and contains(@label,'" + label + "') and (contains(@label,'Discount') or contains(@label,'Diskon') or contains(@label,'Benefit'))]"));
            }
            case "harga gledek" -> {
                return new Locator("WHAT YOU GET :" + label.toUpperCase(), AppiumBy.xpath("//*[@name='what_you_get_title' and contains(@label,'" + label + "')]"));
            }
            default -> throw new NotImplementedException("Unimplemented label type!");
        }
    }

    private Locator getWhatYouGetLocatorSRP(String label) {
        switch (label.toLowerCase()) {
            case "diamond", "platinum" -> {
                return new Locator("Banner Locator", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@label,'Hide') or contains(@label,'Sembunyikan')])[1]//following-sibling::*[contains(@label,'Elite') and contains(@label,'"+label+"')]"));
            }
            case "harga gledek" -> {
                return new Locator("WHAT YOU GET :" + label.toUpperCase(), AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@label,'Hide') or contains(@label,'Sembunyikan')])[1]//following-sibling::*[contains(@label,'"+label+"')]"));
            }
            default -> throw new NotImplementedException("Unimplemented label type!");
        }
    }

    public void backToFlightPage(){
        driverTools.click(BACK_BUTTON);
    }

    @Override
    public String getRouteText() {
        return driverTools.getText(ROUTE_TEXT).orElseThrow();
    }

    @Override
    public boolean isOnSearchResultPage() {
        boolean isFilterShow = driverTools.waitForElementToAppear(FILTER,30).isPresent();
        if(!isFilterShow)
            isFilterShow = driverTools.waitForElementToAppear(FILTER_OLD).isPresent();
        logger.log("filter : " + isFilterShow);
        return isFilterShow;
    }

    // TODO: verify on-device — best-guess locator for the SRP error/retry bottomsheet.
    private static final Locator ERROR_BOTTOMSHEET = new Locator("ERROR_BOTTOMSHEET",
            AppiumBy.xpath("//*[contains(@label,'Terjadi kesalahan') or contains(@label,'Something went wrong') or contains(@label,'Coba lagi') or contains(@label,'Try again') or contains(@label,'Muat ulang') or contains(@label,'Reload') or contains(@name,'error')]"));

    @Override
    public boolean isErrorBottomSheetDisplayed() {
        boolean displayed = driverTools.waitForElementToAppear(ERROR_BOTTOMSHEET, 3).isPresent();
        logger.log("error bottomsheet displayed : " + displayed);
        return displayed;
    }

    private Locator getStrikePriceLocator(int index) {
        return new Locator("STRIKE PRICE", AppiumBy.xpath("(//*[@type='XCUIElementTypeStaticText' and contains(@name,'IDR ')])[" + index + "]"));
    }

    private Locator getPriceLocator(int index){
        return new Locator("DISCOUNTED PRICE",AppiumBy.xpath("(//*[contains(@label,'/pax')])["+index+"]"));
    }

    @Override
    public boolean isAirlineWithDiscountedPrice(){
        driverTools.waitForElementToAppear(FLIGHT_DETAIL_EXPAND,15).orElseThrow();

        WebElement strikePriceElement = driverTools.waitForElementToAppear(getStrikePriceLocator(1)).orElseThrow();
        WebElement discountedPriceElement = driverTools.waitForElementToAppear(getPriceLocator(1)).orElseThrow();

        double strikePrice = CoreFunctions.convertPrice(driverTools.getText(strikePriceElement));
        double discountedPrice = CoreFunctions.convertPrice(driverTools.getText(discountedPriceElement));

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
        WebElement discountPriceElement = driverTools.waitForElementToAppear(DISCOUNT_PRICE_SRP).orElseThrow();
        return driverTools.getText(discountPriceElement);
    }

    @Override
    public String getSpecialCampaignSavingsPrice(){
        WebElement savingsElement = driverTools.waitForElementToAppear(FLIGHT_CARD_FIRST_PRICE).orElseThrow();
        String text = driverTools.getText(savingsElement);
        logger.log("[getSpecialCampaignSavingsPrice] text: " + text);
        return text;
    }

    @Override
    public boolean isAdjustmentPresentSearch(){
        boolean discountedPrice = driverTools.isElementDisplayed(getStrikePriceLocator(1));
        logger.log("Discounted price: "+discountedPrice);
        boolean loyaltyCampaign = driverTools.isElementDisplayed(LOYALTY_DISC_UP_TO);
        logger.log("Loyalty Banner: "+loyaltyCampaign);
        boolean specialCampaign = driverTools.isElementDisplayed(SPECIAL_CAMPAIGN);
        logger.log("Loyalty Banner: "+specialCampaign);
        boolean discountTag = driverTools.isElementDisplayed(PRICETAG);
        logger.log("Discount Tag: "+discountTag);

        return discountedPrice && loyaltyCampaign && specialCampaign && discountTag;
    }

    @Override
    public boolean isAdjustmentPresentDetail(){
        boolean discountPrice = driverTools.isElementDisplayed(DISCOUNT_OR_CAMPAIGN_PRICE);
        logger.log("discount price: "+discountPrice);
        driverTools.swipeUp();
        boolean whatYouGetGledek = driverTools.isElementDisplayed(HARGA_GLEDEK_WHAT_YOU_GET);
        logger.log("What you get Harga Gledek "+discountPrice);
        boolean whatYouGetLoyalty = driverTools.isElementDisplayed(LOYALTY_WHAT_YOU_GET);
        logger.log("What you get loyalty "+discountPrice);
        driverTools.swipeDown();
        return  discountPrice && whatYouGetLoyalty && whatYouGetGledek;
    }

    @Override
    public boolean areFlightsAvailable(){
        driverTools.waitForElementToAppear(CHANGE_SEARCH_BTN_SRP);
        return !driverTools.isElementDisplayed(CHANGE_SEARCH_BTN_SRP);
    }

    private Locator getPriceOnSrpForSpecialCampaignLocator(int index){
        return new Locator("PRICE SRP",AppiumBy.xpath("(//*[contains(@name,'/pax')])["+index+"]"));
    }

    private Locator getDiscountPriceOnSrpForSpecialCampaignLocator(int index){
        return new Locator("DISCOUNT PRICE SRP",AppiumBy.xpath("(//*[contains(@label,':')]//following-sibling::*[contains(@label,':')]//following-sibling::XCUIElementTypeStaticText[contains(@label,'Diskon') or contains(@label,'Discount')])["+index+"]"));
    }

    @Override
    public boolean isTotalPriceWithoutDiscount(int adult){
        String price = driverTools.getText(TOTAL_PRICE).orElseThrow();
        double totalPrice = CoreFunctions.convertPrice(price);
        logger.log("Total Price: " + totalPrice);
        String priceAdult = driverTools.getText(PRICE_ADULT).orElseThrow();
        double totalPriceAdult = CoreFunctions.convertPrice(priceAdult);
        logger.log("Adult Price: " + totalPriceAdult);
        return totalPrice == totalPriceAdult;
    }

    @Override
    public boolean isDiscountPriceForSpecialCampaignCorrectOnSRP(String date){
        driverTools.waitForElementToAppear(FLIGHT_DETAIL_EXPAND,15).orElseThrow();

        WebElement priceElement = driverTools.waitForElementToAppear(getPriceOnSrpForSpecialCampaignLocator(1),5).orElseThrow();
        double price = CoreFunctions.convertPrice(driverTools.getText(priceElement));

        WebElement discountElement = driverTools.waitForElementToAppear(getDiscountPriceOnSrpForSpecialCampaignLocator(1),5).orElseThrow();
        double discount = CoreFunctions.convertPrice(driverTools.getText(discountElement));

        WebElement priceSelectedDate = driverTools.waitForElementToAppear(PRICE_SELECTED_DATE,5).orElseThrow();
        double priceAfterDiscountExpected = CoreFunctions.convertPrice(driverTools.getText(priceSelectedDate));

        //discount is in -ve so i have added it -> price + (-discount) = price - discount

        double priceAfterDiscountActual = price + discount;

        logger.log("price: " + price);
        logger.log("discounted price: " + discount);
        logger.log("discounted price on price date picker: " + priceAfterDiscountExpected);
        logger.log("discounted price after calculation: " + priceAfterDiscountActual);

        return priceAfterDiscountActual == priceAfterDiscountExpected;
    }

    private Locator getLabelOfCardSRP(int index,String label){
        switch (label.toLowerCase()) {
            case "harga gledek" -> {
                return new Locator("LABEL ON CARD SRP " + label.toUpperCase(), AppiumBy.xpath("(//*[@type='XCUIElementTypeStaticText' and @name='Harga Gledek Testing'])[2]"));
            }
            case "free reschedule","discount" ->{
                return new Locator("LABEL ON CARD SRP " + label.toUpperCase(), AppiumBy.xpath("((//*[contains(@name,'card') and contains(@name,'price')])["+index+"]//following-sibling::*[contains(@label,'"+label+"')])[1]"));
            }
            default -> throw new NotImplementedException("Unimplemented label type!");
        }
    }

    @Override
    public boolean checkLabelOnCardSRP(String label){
        driverTools.waitForElementToAppear(getLabelOfCardSRP(1,label));
        return driverTools.isElementDisplayed(getLabelOfCardSRP(1,label));
    }

    private Locator getTagLocatorOnPriceAndPolicyTab(String tag){
        switch (tag.toLowerCase()) {
            case "discount", "diskon" -> {
                return new Locator("DISCOUNT_TAG_PRICE_AND_POLICY ", AppiumBy.xpath("//*[(@label='Diskon' or @label='Discount') and @type='XCUIElementTypeStaticText']"));
            }
            case "harga gledek" -> {
                return new Locator("TAG PRICE AND POLICY " + tag.toUpperCase(), AppiumBy.xpath("//*[@name='price_detail_info' and contains(@label,'" + tag + "')]"));
            }
            default -> throw new NotImplementedException("Unimplemented tag type!");
        }
    }

    @Override
    public boolean checkTagPresentOnPriceAndPolicyTab(String expectedTag) {
        return driverTools.isElementDisplayed(getTagLocatorOnPriceAndPolicyTab(expectedTag));
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
        return !driverTools.isElementDisplayed(JOIN_NOW);
    }

    private Locator get100pRefundAttribute(){
        return new Locator("100P_REFUND_ATTRIBUTE_LOCATOR",AppiumBy.xpath("//*[contains(@name,'Refund (selected)')]"));
    }

    @Override
    public boolean is100pRefundDisplayed(){
        boolean is100pRefundDisplayedFor1Card = driverTools.isElementDisplayed(get100pRefundAttribute());
        logger.log("is 100p refund displayed : " + is100pRefundDisplayedFor1Card);
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
        driverTools.swipeUp(1);
        WebElement megaPhoneIcone = driverTools.swipeToElement(MEGAPHONE_ICON_SRP,Swipe.UP,15).orElseThrow();
        boolean isMegaPhoneIconDisplayed = driverTools.isElementDisplayed(megaPhoneIcone);
        logger.log("mega phone icon displayed : " + isMegaPhoneIconDisplayed);
        return isMegaPhoneIconDisplayed;
    }

    @Override
    public void clickMegaphoneIcon(){
        driverTools.click(MEGAPHONE_ICON_SRP);
    }

    @Override
    public void clickLoginBanner(){
        driverTools.swipeToElement(JOIN_NOW, Swipe.UP, 10).ifPresentOrElse(driverTools::click,(()->{
            Assertion.assertThat("Join Now button not found",false,is(true));
        }));
    }

    @Override
    public List<String> getPrices(){
        driverTools.waitForElementToAppear(PRICES,20).orElseThrow();

        for(int tries = 5; tries>0; tries--){
            if(!driverTools.elements(PRICES).orElseThrow().isEmpty()){
                break;
            }
        }

        List<String> prices = driverTools.getTexts(PRICES);
        logger.log("prices: "+prices);
        Assertion.assertThat("Prices exist", !prices.isEmpty(),is(true));
        return prices;
    }

    @Override
    public void clickSeeDetailSrp(int index){
        driverTools.swipeToElement(FLIGHT_DETAIL_EXPAND, Swipe.UP,15);
        WebElement flightDetailExpand = driverTools.waitForElementToAppear(FLIGHT_DETAIL_EXPAND).orElseThrow();
        driverTools.click(flightDetailExpand);
    }

    @Override
    public void clickDetailPRP(){
        /// fixme implementation pending
    }

    private Locator getNudgeLocator(String enText,String idText){
        return new Locator("NUDGE_LOCATOR_" + enText.toUpperCase(),AppiumBy.xpath("//*[contains(@label,'"+enText+"') or contains(@label,'"+idText+"')]"));
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
        return driverTools.waitForElementToPresence(FLOATING_ICON_SRP, 15).isPresent();
    }

    @Override
    public void clickFloatingIcon(){
        driverTools.click(FLOATING_ICON_SRP);
    }

    @Override
    public void backFromFloatingIcon(){
        driverTools.switchToNative();
        driverTools.click(BACK_TO_SRP);
    }

    @Override
    public void waitForFloatingIconToDisappeared(){
        driverTools.waitForElementToDisappear(FLOATING_ICON_SRP);
    }

    @Override
    public void openTransitFilter(){
        if(driverTools.isElementDisplayed(FLIGHT_TRANSIT_FILTER))
            driverTools.click(FLIGHT_TRANSIT_FILTER);
        else
            driverTools.click(FLIGHT_TRANSIT_FILTER_INFINITY);
    }

    @Override
    public void openSortFilter() {
        driverTools.click(FLIGHT_SORT_FILTER);
    }

    @Override
    public void sortBy(String type) {
        WebElement e = driverTools.swipeToPresentedElement(getSortTypeLocator(type), Swipe.UP, 5).orElseThrow();
        driverTools.click(e);
    }

    private Locator getSortTypeLocator(String type) {
        switch (type.toLowerCase(Locale.ROOT)) {
            case "recommended":
            case "rekomendasi":
                return sortTitleLocator("Recommended", "Rekomendasi");
            case "lowest price":
            case "harga terendah":
                return sortTitleLocator("Lowest price", "Harga terendah");
            case "earliest departure":
            case "keberangkatan paling awal":
                return sortTitleLocator("Earliest departure", "Keberangkatan paling awal");
            case "latest departure":
            case "keberangkatan paling akhir":
                return sortTitleLocator("Latest departure", "Keberangkatan paling akhir");
            case "earliest arrival":
            case "kedatangan paling awal":
                return sortTitleLocator("Earliest arrival", "Kedatangan paling awal");
            case "latest arrival":
            case "kedatangan paling akhir":
                return sortTitleLocator("Latest arrival", "Kedatangan paling akhir");
            case "shortest duration":
            case "durasi terpendek":
                return sortTitleLocator("Shortest duration", "Durasi terpendek");
            default:
                throw new NotImplementedException("Unimplemented sort type: " + type);
        }
    }

    private Locator sortTitleLocator(String en, String id) {
        String name = "SORT_BY_" + en.toUpperCase().replace(" ", "_");
        return new Locator(name, AppiumBy.xpath(
                "(//*[(@name='sort_by_" + en + "_title' or @name='sort_by_" + id + "_title') and @visible='true'])"
                        + " | (//XCUIElementTypeStaticText[(@name='" + en + "' or @name='" + id + "') and @visible='true'])"
        ));
    }

    @Override
    public void verifySortSelected(String type) {
        openSortFilter();
        Locator selected = getSortSelectedRadioLocator(type);
        driverTools.waitForElementToAppear(selected);
        boolean isSelected = driverTools.isElementDisplayed(selected);
        logger.log("Sort '" + type + "' selected on iOS: " + isSelected);
        Assertion.assertThat(
                "Sort option '" + type + "' should be selected after sortBy() (selected radio indicator present)",
                isSelected,
                is(true)
        );
        driverTools.click(CLOSE_BTN);
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
        Locator recommendedSelected = getAdvancedFilterSortSelectedRadioLocator("Recommended");
        boolean defaultSort = driverTools.isElementDisplayed(recommendedSelected);
        logger.log("Recommended selected as default sort on iOS (no filter applied): " + defaultSort);
        Assertion.assertThat("Sort filter should be in default state with no applied sort change", defaultSort, is(true));
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
        driverTools.click(SORT_SEE_MORE);
        Locator sortOption = getSortTypeLocator(type);
        driverTools.waitForElementToPresence(sortOption, 5).orElseThrow(() -> new NoSuchElementException("Sort option not found in advanced filter: " + type));
        driverTools.click(sortOption);
    }

    @Override
    public void verifySortByFilterSelectedIndicator(String type) {
        verifyResetFilterEnabled();
        driverTools.click(SORT_SEE_MORE);
        Locator selected = getSortSelectedRadioLocator(type);
        boolean isSelected = driverTools.isElementDisplayed(selected);
        logger.log("Sort option '" + type + "' selected in advanced filter on iOS: " + isSelected);
        Assertion.assertThat(
                "Sort option '" + type + "' should be selected in advanced filter",
                isSelected,
                is(true)
        );
        driverTools.click(SEE_MORE_FILTER_CLOSE_BTN);
    }

    @Override
    public void verifySortByFilterReset(String type) {
        verifyResetFilterDisabled();
        verifyNoSortByGreenDot();
        driverTools.click(SORT_SEE_MORE);
        Locator selected = getSortSelectedRadioLocator(type);
        boolean stillSelected = driverTools.isElementDisplayed(selected);
        logger.log("Sort option '" + type + "' still selected after reset on iOS: " + stillSelected);
        Assertion.assertThat(
                "Sort option '" + type + "' should not remain selected after reset",
                stillSelected,
                is(false)
        );
        driverTools.click(SEE_MORE_FILTER_CLOSE_BTN);
    }

    private Locator getAdvancedFilterSortSelectedRadioLocator(String type) {
        switch (type.toLowerCase(Locale.ROOT)) {
            case "recommended":
            case "rekomendasi":
                return advancedFilterSortSelectedRadioLocator("Recommended", "Rekomendasi");
            case "lowest price":
            case "harga terendah":
                return advancedFilterSortSelectedRadioLocator("Lowest price", "Harga terendah");
            case "earliest departure":
            case "keberangkatan paling awal":
                return advancedFilterSortSelectedRadioLocator("Earliest departure", "Keberangkatan paling awal");
            case "latest departure":
            case "keberangkatan paling akhir":
                return advancedFilterSortSelectedRadioLocator("Latest departure", "Keberangkatan paling akhir");
            case "earliest arrival":
            case "kedatangan paling awal":
                return advancedFilterSortSelectedRadioLocator("Earliest arrival", "Kedatangan paling awal");
            case "latest arrival":
            case "kedatangan paling akhir":
                return advancedFilterSortSelectedRadioLocator("Latest arrival", "Kedatangan paling akhir");
            case "shortest duration":
            case "durasi terpendek":
                return advancedFilterSortSelectedRadioLocator("Shortest duration", "Durasi terpendek");
            default:
                throw new NotImplementedException("Unimplemented sort type: " + type);
        }
    }

    private Locator advancedFilterSortSelectedRadioLocator(String en, String id) {
        String name = "ADV_SORT_SELECTED_" + en.toUpperCase().replace(" ", "_");
        return new Locator(name, AppiumBy.xpath(
                "//XCUIElementTypeStaticText[@name='" + en + "' or @name='" + id
                        + "']/preceding-sibling::*[@name='radioButton (selected)'][1]"
        ));
    }

    private Locator getSortSelectedRadioLocator(String type) {
        switch (type.toLowerCase(Locale.ROOT)) {
            case "recommended":
            case "rekomendasi":
                return sortSelectedRadioLocator("Recommended", "Rekomendasi");
            case "lowest price":
            case "harga terendah":
                return sortSelectedRadioLocator("Lowest price", "Harga terendah");
            case "earliest departure":
            case "keberangkatan paling awal":
                return sortSelectedRadioLocator("Earliest departure", "Keberangkatan paling awal");
            case "latest departure":
            case "keberangkatan paling akhir":
                return sortSelectedRadioLocator("Latest departure", "Keberangkatan paling akhir");
            case "earliest arrival":
            case "kedatangan paling awal":
                return sortSelectedRadioLocator("Earliest arrival", "Kedatangan paling awal");
            case "latest arrival":
            case "kedatangan paling akhir":
                return sortSelectedRadioLocator("Latest arrival", "Kedatangan paling akhir");
            case "shortest duration":
            case "durasi terpendek":
                return sortSelectedRadioLocator("Shortest duration", "Durasi terpendek");
            default:
                throw new NotImplementedException("Unimplemented sort type: " + type);
        }
    }

    private Locator sortSelectedRadioLocator(String en, String id) {
        String name = "SORT_SELECTED_" + en.toUpperCase().replace(" ", "_");
        return new Locator(name, AppiumBy.xpath(
                "//XCUIElementTypeCell[" +
                        ".//*[@name='sort_by_" + en + "_title' or @name='sort_by_" + id + "_title']" +
                        "]//*[contains(@name,'radio_btn') and contains(@name,'selected')]"
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
        WebElement element = driverTools.swipeToPresentedElement(LAST_CARD_PRICE_BEFORE_DELTA_DESC, Swipe.UP, 15).orElseThrow();
        String text = driverTools.getText(element);
        double price = convertPrice(text);
        logger.log("Last flight card price before delta desc: " + text + " -> " + price);
        return price;
    }

    @Override
    public double getFirstFlightCardPriceAfterDeltaDesc() {
        WebElement element = driverTools.swipeToPresentedElement(FIRST_CARD_PRICE_AFTER_DELTA_DESC, Swipe.UP, 15).orElseThrow();
        String text = driverTools.getText(element);
        double price = convertPrice(text);
        logger.log("First flight card price after delta desc: " + text + " -> " + price);
        return price;
    }

    @Override
    public void swipeToDeltaPriceDesc() {
        driverTools.swipeToPresentedElement(DELTA_PRICE_DESCRIPTION, Swipe.UP, 15).orElseThrow();
        logger.log("Swiped to delta price description");
    }

    @Override
    public void selectFirstFlightWithRoundTripDealsLabel(String roundTripDealsFilter) {
        filterChips(roundTripDealsFilter);
        waitForPageLoad();
        Locator flightCardWithRoundTripDeals = new Locator("FLIGHT_CARD_WITH_ROUND_TRIP_DEALS",
                AppiumBy.xpath("(//*[(contains(@label,'Round-trip Deals') or contains(@name,'Round-trip Deals'))]/ancestor::*[contains(@name,'card_')])[1]"));
        driverTools.swipeToElement(flightCardWithRoundTripDeals, Swipe.UP, 15).orElseThrow();
        driverTools.clickElementByCoordinate(flightCardWithRoundTripDeals);
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
        driverTools.waitForElementToAppear(FLIGHT_CARD,15);
        driverTools.click(CHANGE);
    }

    @Override
    public void clickSearchBtn(){
        //fixme implementation pending
    }

    @Override
    public void verifySelectedDateSRP(String dateSelectedDepart,String dateSelectedReturn,String lang){
        //fixme implementation pending
    }

    @Override
    public boolean isSearchResultPageEmptyForEconomy(String currency){
        driverTools.waitForElementToAppear(CHANGE_SEARCH_BTN_SRP,25).orElseThrow();

        boolean isSearchSubHeaderShowsEconomy = driverTools.isElementDisplayed(ECONOMY_CLASS_TITLE_SUB_HEADER);
        logger.log("is sub header shows economy : " + isSearchSubHeaderShowsEconomy);
        Assertion.assertThat("is sub header shows economy",isSearchSubHeaderShowsEconomy,is(true));

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

        driverTools.waitForElementToPresence(CHANGE_SEARCH_BTN_SRP);
        boolean isChangeSearchBtnDisplayed = driverTools.isElementDisplayed(CHANGE_SEARCH_BTN_SRP);
        logger.log("is change search btn displayed : " + isChangeSearchBtnDisplayed);
        Assertion.assertThat("Change search btn displayed",isChangeSearchBtnDisplayed,is(true));

        return isSearchSubHeaderShowsEconomy && isPriceSelectedDateNotContainsCurrency && isChangeSearchBtnDisplayed;
    }

    @Override
    public boolean isSearchResultShownForPremiumEconomy(String currency){
        driverTools.waitForElementToAppear(FLIGHT_CARD,25).orElseThrow();

        boolean isSearchSubHeaderShowsPremiumEconomy = driverTools.isElementDisplayed(PREMIUM_ECONOMY_CLASS_TITLE_SUB_HEADER);
        logger.log("is sub header shows premium economy : " + isSearchSubHeaderShowsPremiumEconomy);
        Assertion.assertThat("is sub header shows premium economy",isSearchSubHeaderShowsPremiumEconomy,is(true));

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

        boolean isFlightCardDisplayed = driverTools.isElementDisplayed(FLIGHT_CARD);
        logger.log("is flight card displayed : " + isFlightCardDisplayed);
        Assertion.assertThat("Flight card displayed",isFlightCardDisplayed,is(true));

        return isSearchSubHeaderShowsPremiumEconomy && isPriceSelectedDateContainsCurrency && isFlightCardDisplayed;
    }

    @Override
    public boolean isMeetTravelPolicy(){
        return driverTools.isElementDisplayed(MEET_TRAVEL_POLICY_1);
    }

    @Override
    public boolean isMeetTravelPolicyAll(String airline){
        boolean isMeetTravelPolicy = driverTools.isElementDisplayed(MEET_TRAVEL_POLICY_1);
        logger.log("is meet travel policy displayed : " + isMeetTravelPolicy);

        this.openFilter();

        driverTools.waitForElementToAppear(CLOSE_BTN);
        logger.log("check only one airline present: " + airline);

        List<WebElement> airlineTitleInFilter = driverTools.elements(getAirlineFilterOptionLocator(airline)).orElseThrow();
        boolean sizeIs1 = airlineTitleInFilter.size() == 1;
        Assertion.assertThat("Only one airline present in filter",airlineTitleInFilter.size(),is(1));

        boolean isAirlinePresent = driverTools.isElementDisplayed(getAirlineFilterOptionLocator(airline));
        logger.log("is airline present : " + isAirlinePresent);
        return isMeetTravelPolicy && sizeIs1 && isAirlinePresent;
    }

    @Override
    public void applyPromoInfinity(){
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
        boolean isCancelBtnDisplayed = driverTools.isElementDisplayed(CANCEL_PROMO_INFINITY);
        logger.log("isCancelBtnDisplayed: " + isCancelBtnDisplayed);

        boolean isTextPromoApplied = driverTools.isElementDisplayed(PROMO_APPLIED_TEXT);
        logger.log("isTextPromoApplied: " + isTextPromoApplied);
        return isCancelBtnDisplayed && isTextPromoApplied;
    }

    @Override
    public List<String> getAirlineNames(String airline1, String airline2) {
        List<String> airlineNames1 = driverTools.getTexts(getAirlineName(airline1));
        List<String> airlineNames2 = driverTools.getTexts(getAirlineName(airline2));

        List<String> allAirlineNames = new ArrayList<>();
        allAirlineNames.addAll(airlineNames1 != null ? airlineNames1 : Collections.emptyList());
        allAirlineNames.addAll(airlineNames2 != null ? airlineNames2 : Collections.emptyList());

        allAirlineNames.forEach(name -> logger.log("Found airline: " + name));

        Assertion.assertThat("Airline names should not be empty", allAirlineNames, CoreMatchers.is(not(empty())));
        return allAirlineNames;
    }

    @Override
    public ArrayList<String> getAirlineNames(ArrayList<String> airline) {
        ArrayList<String> airlineList = new ArrayList<>();
        boolean found = false;
        for(String airlineNames : airline){
            List<String> airlineNames1 = driverTools.getTexts(getAirlineNameStrict(airlineNames));
            logger.log("size: " + airlineNames1.size());
            for (String airlineOnCard : airlineNames1){
                if(!airlineList.contains(airlineOnCard)){
                    airlineList.add(airlineOnCard.trim());
                }
            }
            found = airlineList.containsAll(airline);
        }

        if (found) {
            logger.log("Given airlines '" + airline + " in list " + airlineList);
        } else {
            logger.warn("Given airlines '" + airline + "' not found." + " in list " + airlineList);
        }
        Assertion.assertThat("Airline names should not be empty", airlineList, CoreMatchers.is(not(empty())));
        return airlineList;
    }

    /**
     * XPath 1.0 string literal for use inside {@code contains(@attr, ...)}; escapes quotes in {@code value}.
     */
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
     * SRP airline row on cards: {@code n}-th occurrence (1-based) of StaticText whose {@code @label} contains
     * {@code airlineLabel} (handles trailing spaces etc.).
     * Shape: {@code (//*[@type="XCUIElementTypeStaticText" and contains(@label,'Batik Air')])[n]}.
     */
    private Locator srpNthAirlineFilterStaticTextByLabelContains(String airlineLabel, int nthOneBased) {
        String labelLit = xpathStringLiteralForContains(airlineLabel);
        String xpath = "(//*[@type=\"XCUIElementTypeStaticText\" and contains(@label," + labelLit + ")])["
                + nthOneBased + "]";
        return new Locator("SRP_AIRLINE_FILTER_STATIC_TEXT_LABEL_CONTAINS_N" + nthOneBased, AppiumBy.xpath(xpath));
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

        String label = CoreFunctions.titleize(appliedAirline.trim());
        for (int i = 1; i <= maxFlightCardsToCheck; i++) {
            Locator nthAirline = srpNthAirlineFilterStaticTextByLabelContains(label, i);
            driverTools.swipeToElement(nthAirline, Swipe.UP, 5);
            if (!driverTools.isElementDisplayed(nthAirline)) {
                logger.log("verifyAllVisibleFlightCardsMatchAirlineFilter: instance [" + i + "] of "
                        + maxFlightCardsToCheck + " not displayed for contains(@label,\"" + label + "\")");
                if (i > 1) return true;
                return false;
            }
        }
        logger.log("verifyAllVisibleFlightCardsMatchAirlineFilter: instances [1]..[" + maxFlightCardsToCheck
                + "] displayed for contains(@label,\"" + label + "\")");
        return true;
    }

    @Override
    public int getAirlineFilterOptionCount(String airline) {
        // not implemented
        throw new NotImplementedException("getAirlineFilterOptionCount is not implemented for iOS");
    }

    @Override
    public boolean areAllFlightSchedulesUnique(String expectedAirline, int expectedCardCountFromFilter) {
        // not implemented
        throw new NotImplementedException("areAllFlightSchedulesUnique is not implemented for iOS");
    }

    private Locator getAirlineName(String airline) {
        return new Locator("AIRLINE_NAMES",
                AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name, '" + airline + "')]"));
    }

    @Override
    public boolean checkAirlineVI(ArrayList<String> airline) {
        ArrayList<String> airlineList = new ArrayList<>();
        for (String airlineName : airline){
            List<String> airlineNames = driverTools.getTexts(getViAirlineLocator(airlineName));
            for(String airlineOnCard : airlineNames){
                if(!airlineList.contains(airlineOnCard) && airlineOnCard.contains("+")){
                    logger.log("airline added in list: " + airlineOnCard);
                    String[] airlineCard = airlineOnCard.split("\\+");
                    airlineList.add(airlineCard[0].trim());
                    airlineList.add(airlineCard[1].trim());
                }
            }
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

    private Locator getAirlineNameStrict(String airline) {
        return new Locator("AIRLINE_NAMES",AppiumBy.xpath("//*[contains(@name,'card_')]//following-sibling::XCUIElementTypeStaticText[@name='"+airline+"' or @name='"+airline+" ']  |  //*[contains(@name,'card_')]//preceding-sibling::XCUIElementTypeStaticText[@name='"+airline+"' or @name='"+airline+" ']"));
    }

    private boolean getAirlineList(String airline) {
        ArrayList<String> airlineList = new ArrayList<>();
        List<String> airlineNames = driverTools.getTexts(getAirlineName(airline));
        for(String airlineOnCard : airlineNames){
            if(!airlineList.contains(airlineOnCard) ){
                logger.log("airline added in list: " + airlineOnCard);
                airlineList.add(airlineOnCard.trim());
            }
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
            Assertion.assertThat("is airline filtered shown on srp : " + airline,this.getAirlineList(airline),is(true));
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
                Locator airlineName = getAirlineFilterOptionLocator(airline);
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
            scrollAirlineFilterSheetOnce();
            retry--;
        }
        return airlineCount;
    }

    @Override
    public boolean checkAirlinesOnFilter(ArrayList<String> airlines,int numberOfSwipe){
        this.openFilter();
        this.resetFilter();
        driverTools.waitForElementToAppear(CLOSE_BTN);

        Map<String, Integer> airlineCount = getNumberOfFlightOnFilter(airlines,6);
        boolean found = true;

        for (String airline : airlines) {
            int count = airlineCount.get(airline.toLowerCase()) == null ? -1 : airlineCount.get(airline.toLowerCase());
            boolean isCountMoreThenZero = count > 0;
            found = found & isCountMoreThenZero;
            Assertion.assertThat("is [airlineName: " + airline + " count: " + count + " ] more then zero ",count,is(not(0)));
        }
        driverTools.click(CLOSE_BTN);
        return found;
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

    @Override
    public boolean isVI_AirlineDisplayed(){
        if (driverTools.waitForElementToAppear(FLIGHT_CARD, 15).isEmpty()) {
            driverTools.waitForElementToAppear(FLIGHT_CARD_LEGACY, 10);
        }
        driverTools.swipeToElement(getViAirlineLocator(), Swipe.UP, 6);
        return driverTools.isElementDisplayed(getViAirlineLocator());
    }

    private Locator getViAirlineLocator(){
        return new Locator("AIRLINE_NAME_ON_CARD",AppiumBy.xpath("//XCUIElementTypeTable//XCUIElementTypeCell//XCUIElementTypeStaticText[contains(@label,'+') and not(contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'airline') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'maskapai') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'self-transfer'))] | //*[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'self-transfer')]/preceding-sibling::*[last()]"));
    }

    private Locator getViAirlineLocator(String airlineName){
        String airlineNameLiteral = CoreFunctions.toXPathLiteral(airlineName.toLowerCase(Locale.ROOT));
        return new Locator("AIRLINE_NAME_ON_CARD",AppiumBy.xpath(
                "//XCUIElementTypeTable//XCUIElementTypeCell//XCUIElementTypeStaticText[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," + airlineNameLiteral + ") and contains(@label,'+') and not(contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'airline') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'maskapai') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'self-transfer'))]"
                        + " | //*[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'self-transfer')]/preceding-sibling::*[last()][contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," + airlineNameLiteral + ")]"));
    }

    @Override
    public List<String> getFirstFlightCardAirlineNamesSplitByPlus() {
        driverTools.waitForElementToAppear(SRP_FIRST_FLIGHT_CARD_VI_AIRLINE_ADJACENT_TO_PRICE, 15);
        String raw = driverTools.getText(SRP_FIRST_FLIGHT_CARD_VI_AIRLINE_ADJACENT_TO_PRICE).orElse("");
        return splitAirlineNamesCombinedByPlus(raw);
    }

    @Override
    public void waitForPageLoad(){
        driverTools.waitForElementToAppear(PROGRESS_BAR_SRP,6);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_SRP);
    }

    @Override
    public boolean SaveLabel(){
        return driverTools.waitForElementToPresence(SAVE_LABEL,20).isPresent();
    }

    @Override
    public boolean CheckCashBackLabel(){
        return driverTools.isElementDisplayed(CASHBACK_LABEL);
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
    public void selectTicketFromDetailsPage(){
        driverTools.click(SELECT_TICKET_FROM_DETAIL_PAGE);
    }
}
