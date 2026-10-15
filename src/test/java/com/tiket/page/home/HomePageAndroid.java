package com.tiket.page.home;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiket.appium.Swipe;
import com.tiket.model.Deeplink;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.account.AccountPage;
import com.tiket.page.account.voucherboxpage.VoucherBoxPage;
import com.tiket.page.carrental.searchpage.SearchPageCarRental;
import com.tiket.page.cartransfer.searchpage.CarTransferSearchPage;
import com.tiket.page.flight.searchpage.FlightSearchPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.setting.SettingPage;
import com.tiket.page.train.searchpage.TrainSearchPage;
import com.tiket.page.utilities.discovery.promo.PromoPage;
import com.tiket.pojo.utilities.*;
import com.tiket.service.PackageName;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static com.tiket.core.CoreFunctions.splitLanguageData;
import static com.tiket.page.login.LoginPageAndroid.CONTINUE_AS_GUEST;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class HomePageAndroid <T extends WebDriver> extends HomePage<T> {
    private static final Locator HOME = new Locator("HOME", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_bottom_navigation') and (@text='Home' or @text='Beranda')]"));
    private static final Locator SUGGESTION_LIST_TITLE = new Locator("SUGGESTION_LIST_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and not(@text='')]"));
    private static final Locator SUGGESTION_LIST_SUBTITLE = new Locator("SUGGESTION_LIST_SUBTITLE", AppiumBy.id("tv_subtitle"));
    private static final Locator SUGGESTION_LIST_ICON = new Locator("SUGGESTION_LIST_ICON", AppiumBy.id("iv_image"));
    private static final Locator MEMBER_COMPLETE_NOW_BUTTON = new Locator("MEMBER_COMPLETE_NOW_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'component_card_member')]//*[contains(@resource-id,'subtitle_notification_banner')]"));
    private static final Locator INFO_PAGES_POLICY_TAB = new Locator("INFO_PAGES_POLICY_TAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and (@text='Kebijakan Perjalanan' or @text='Travel Policy')] "));
    private static final Locator INFO_PAGES_BENEFIT_TAB = new Locator("INFO_PAGES_BENEFIT_TAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and (@text='Benefit & Panduan' or @text='Benefits & Guides')] "));
    private static final Locator INFO_PAGES_ITEMS = new Locator("INFO_PAGES_ITEMS", AppiumBy.xpath("//*[contains(@resource-id,'constraint_root')]//*[contains(@resource-id,'tv_title') and (contains(@text,'info & guide') or contains(@text,'Info & panduan'))]//following-sibling::*//*[contains(@resource-id,'iv_banner')]"));
    private static final Locator INFO_PAGES_TOOLBAR = new Locator("INFO_PAGES_TOOLBAR", AppiumBy.id("toolbar"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'toolbar')]//android.widget.ImageButton"));
    private static final Locator CONTINUE_BUTTON_FIRST_PAGE = new Locator("CONTINUE_BUTTON_FIRST_PAGE", By.id("tds_button_view"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.xpath("//*[@text='Lanjutkan' or @text='Lanjut' or @text='Continue']"));
    private static final Locator SKIP_NOTIFICATION = new Locator("SKIP_NOTIFICATION", AppiumBy.id("tds_btn_primary_info_view"));
    private static final Locator ALLOW_NOTIFICATION = new Locator("ALLOW_NOTIFICATION", AppiumBy.xpath("//*[contains(@resource-id,'permission_allow_button')]"));
    private static final Locator SKIP_ONBOARDING = new Locator("SKIP_ONBOARDING", By.id("tv_skip"));
    private static final Locator SEARCH_ICON = new Locator("SEARCH_ICON", By.id("app_bar_search_box"));
    private static final Locator SEARCH_ICON_OLD = new Locator("SEARCH_ICON_OLD", By.id("tds_search_magnifier"));
    public static final Locator CROUSEL_TEXT = new Locator("CROUSEL_TEXT", AppiumBy.id("titleTextView"));
    /** App bar inbox entry (messages), not the bottom-nav account tab. */
    private static final Locator INBOX = new Locator("INBOX", AppiumBy.xpath("//*[@text='Notification' or @text='Notifikasi']"));
    /** Logged-in account tab on bottom navigation (for login-state checks). */
    public static final Locator ACCOUNT_BOTTOM_NAV = new Locator("ACCOUNT_BOTTOM_NAV", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_bottom_navigation') and (@text='Account' or @text='Akun')]"));
    private static final Locator BOTTOM_NAV_ENGLISH_LABELS = new Locator("BOTTOM_NAV_ENGLISH_LABELS", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_bottom_navigation') and (@text='Log in' or @text='Account')]"));
    private static final Locator CAROUSEL = new Locator("CAROUSEL", AppiumBy.xpath("(//*[contains(@resource-id,'ll_vertical_item')]/ancestor::*[contains(@class,'recyclerview')])[last()]"));
    private static final Locator SEE_ALL_PRODUCT = new Locator("SEE_ALL_PRODUCT", AppiumBy.id("see_more_button"));
    private static final Locator ALL_PRODUCT_CONTAINER = new Locator("ALL_PRODUCT_CONTAINER", AppiumBy.id("bottomSheet_dynamic_content_view"));
    private static final Locator INBOX_HEADER = new Locator("INBOX_HEADER", AppiumBy.id("tds_main_item_group"));
    private static final Locator INBOX_LIST = new Locator("INBOX_LIST", AppiumBy.id("rv_inbox_list"));
    private static final Locator INBOX_DETAIL_SUBTITLE = new Locator("INBOX_DETAIL_SUBTITLE", AppiumBy.id("tv_inbox_detail_title"));
    private static final Locator FLASH_SALE_INVENTORY = new Locator("FLASH_SALE_INVENTORY", AppiumBy.xpath("(//*[contains(@resource-id,'countdown')]//following-sibling::*[contains(@resource-id,'recycler_view')]//*[contains(@resource-id,'card_view')]//*[contains(@resource-id,'container')]//*[contains(@resource-id,'tv_title')])[1]"));
    private static final Locator FLASH_SALE_HOUR_COUNTDOWN = new Locator("FLASH_SALE_HOUR_COUNTDOWN", AppiumBy.id("tds_hour_countdown"));
    private static final Locator FLASH_SALE_MINUTE_COUNTDOWN = new Locator("FLASH_SALE_MINUTE_COUNTDOWN", AppiumBy.id("tds_minute_countdown"));
    private static final Locator FLASH_SALE_SECOND_COUNTDOWN = new Locator("FLASH_SALE_SECOND_COUNTDOWN", AppiumBy.id("tds_second_countdown"));
    private static final Locator FLASH_SALE_INVENTORY_PRICE = new Locator("FLASH_SALE_INVENTORY_PRICE",AppiumBy.xpath("(//*[contains(@resource-id,'price_module_final_price')])[1]"));
    private static final Locator HOMES_RECENT_SEARCH_CHIP = new Locator("HOMES_RECENT_SEARCH_CHIP", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Pencarian Terakhir') or contains(@text,'Recently search') or contains(@text,'Recently Search'))]//following-sibling::*[contains(@resource-id,'chip_group')]//*[contains(@resource-id,'tds_chips_text') and (contains(@text,'Villa') or contains(@text,'Vila'))]"));
    private static final Locator RECENT_SEARCH_TITLE = new Locator("RECENT_SEARCH_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Pencarian Terakhir') or contains(@text,'Recently search') or contains(@text,'Recently Search'))]"));
    private static final Locator CLEAR_RECENT_SEARCH = new Locator("CLEAR_RECENT_SEARCH", AppiumBy.id("btn_clear"));
    private static final Locator CONFIRM_CLEAR_RECENT_SEARCH = new Locator("CONFIRM_CLEAR_RECENT_SEARCH", AppiumBy.id("tds_btn_primary_info_view"));
    public static final Locator TIKET_POINTS_CONTAINER = new Locator("TIKET_POINT_CONTAINER", AppiumBy.id("finance_bar"));
    public static final Locator ALLOW_PERMISSION_BUTTON = new Locator("ALLOW_PERMISSION_BUTTON_LANDINGPAGE", AppiumBy.id("tds_btn_primary_info_view"));
    public static final Locator MY_ORDER = new Locator("ORDER", AppiumBy.xpath("//*[contains(@resource-id, 'tds_title_bottom_navigation') and contains(@text,'Order')]"));
    private static final Locator REWARDS_BOTTOM_NAV = new Locator("REWARDS_BOTTOM_NAV", AppiumBy.xpath("//*[contains(@resource-id, 'tds_title_bottom_navigation') and @text='Rewards']/preceding-sibling::*[contains(@resource-id, 'tds_icon_bottom_navigation')]"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", AppiumBy.xpath("(//android.widget.ImageView[contains(@resource-id, 'tds_icon_bottom_navigation')])[5]"));
    private static final Locator LOGIN_REGISTER_BUTTON = new Locator("LOGIN_REGISTER_BUTTON", AppiumBy.xpath("//android.widget.Button[contains(@resource-id, 'tds_button_view') and @text='Log in or register']"));
    /** Bottom-nav account tab for opening Account / login (guest or logged-in). */
    public static final Locator ACCOUNT_ICON = new Locator("ACCOUNT_ICON", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_bottom_navigation') and ((@text='Account' or @text='Akun') or (@text='Masuk' or @text='Log in'))]"));
    public static final Locator BLIBLI_POINTS = new Locator("BLIBLI_POINTS", AppiumBy.xpath("(//*[contains(@resource-id,'finance_bar')]//*[contains(@text,'Blibli Tiket Points') or contains(@text,'PayLater') or contains(@text,'Blipay')])[1]"));
    public static final Locator FIRST_PRODUCT = new Locator("FIRST_PRODUCT", AppiumBy.xpath("(//*[contains(@resource-id,'ll_vertical_item')]//*[contains(@resource-id,'tv_title')])[1]"));
    public static final Locator MY_ORDER_HEADER = new Locator("MY_ORDER_HEADER", AppiumBy.id("tds_header_navigation_appbar"));
    public static final Locator WHILE_USING_APP_PERMISSION_BUTTON = new Locator("WHILE_USING_APP_PERMISSION_BUTTON", AppiumBy.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button"));
    private static final Locator VIDEO_TITLE = new Locator("VIDEO_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'constraint_root')]//*[contains(@resource-id,'tv_title') and contains(@text,'live')]"));
    private static final Locator VIDEO_BANNER = new Locator("VIDEO_BANNER", AppiumBy.xpath("//*[contains(@resource-id,'constraint_root')]//*[contains(@resource-id,'tv_subtitle') and contains(@text,'Live')]//following-sibling::*//*[contains(@resource-id,'iv_banner')]"));
    private static final Locator HERRO_BANNER = new Locator("HERRO_BANNER", AppiumBy.xpath("(//*[contains(@resource-id, 'bannerContainer') or contains(@resource-id,'iv_banner')])[1]"));
    private static final Locator GS_INFO_CONTAINER = new Locator("GS_INFO_CONTAINER", AppiumBy.id("tds_content_notification_banner"));
    private static final Locator GS_INFO = new Locator("GS_INFO",AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_notification_banner')]//*"));
    private static final Locator GS_INFO_BANNER = new Locator("GS_INFO_BANNER", AppiumBy.xpath("//*[contains(@resource-id,'tds_content_notification_banner') or contains(@resource-id,'tds_icon_notification_banner') or contains(@resource-id,'tds_title_notification_banner')]"));
    private static final Locator GS_HOME_PAGE_SEARCH_BAR = new Locator("GS_HOME_PAGE_SEARCH_BAR", AppiumBy.xpath("//*[contains(@resource-id,'tds_text_search_box') or contains(@resource-id,'app_bar_search_box')]"));
    private static final Locator GS_INFO_CLOSE = new Locator("GS_INFO_CLOSE", AppiumBy.id("tds_close_notification_banner"));
    private static final Locator HOME_PAGE_PROMO_CHIP = new Locator("HOME_PAGE_PROMO_CHIP", AppiumBy.xpath("(//*[contains(@resource-id,'app_bar_item_icon')])[last()]"));
    private static final Locator HOME_PAGE_PROMO_CHIP_OLD = new Locator("HOME_PAGE_PROMO_CHIP_OLD", AppiumBy.xpath("//*[contains(@resource-id,'tds_appbar_item_text') and contains(@text,'Promo')]"));
    private static final Locator RECENT_SEARCH_CONTAINER = new Locator("RECENT_SEARCH_CONTAINER", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Pencarian Terakhir') or contains(@text,'Recently search') or contains(@text,'Recently Search'))]//following-sibling::*[contains(@resource-id,'recycler_view')]"));
    private static final Locator GS_INPUT_BAR = new Locator("GS_INPUT_BAR", AppiumBy.xpath("//*[contains(@resource-id,'tds_search_et')]"));
    private static final Locator LOCATION_BANNER_TITLE_HEADING = new Locator("LOCATION_BANNER_TITLE_HEADING", AppiumBy.id("shared_component_location_banner_title"));
    private static final Locator LOCATION_BANNER_SUBTITLE_HEADING = new Locator("LOCATION_BANNER_SUBTITLE_HEADING", AppiumBy.id("shared_component_location_banner_subtitle"));
    private static final Locator LOCATION_BANNER_CROSS_BUTTON = new Locator("LOCATION_BANNER_CROSS_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'shared_component_location_banner')]//*[contains(@resource-id,'tds_close_notification_banner')]"));
    private static final Locator LOCATION_BANNER_ICON = new Locator("LOCATION_BANNER_ICON", AppiumBy.id("tds_icon_notification_banner"));
    private static final Locator LOCATION_BANNER_CONTENT_TITLE = new Locator("LOCATION_BANNER_CONTENT_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'shared_component_location_banner')]//*[contains(@resource-id,'tds_title_notification_banner')]"));
    private static final Locator LOCATION_BANNER_CONTENT_SUBTITLE = new Locator("LOCATION_BANNER_CONTENT_SUBTITLE", AppiumBy.xpath("//*[contains(@resource-id,'shared_component_location_banner')]//*[contains(@resource-id,'tds_subtitle_notification_banner')]"));
    private static final Locator ALLOW_LOCATION_ONE_TIME = new Locator("ALLOW_LOCATION_ONE_TIME", AppiumBy.id("com.android.permissioncontroller:id/permission_allow_one_time_button"));
    public static final Locator ACTIVATE_PIN = new Locator("ACTIVATE_PIN", AppiumBy.id("shared_component_card_member"));
    public static final Locator PROMO_CONTAINER = new Locator("PROMO_CONTAINER", AppiumBy.id("promo_container"));
    public static final Locator PROMO_VOUCHER_BOX = new Locator("PROMO_VOUCHER_BOX", AppiumBy.id("card_voucher_box"));
    public static final Locator PROMO_PAGE_SCROLL_BAR = new Locator("PROMO_PAGE_SCROLL_BAR", AppiumBy.id("tab_layout_tab"));
    public static final Locator PROMO_APPBAR = new Locator("PROMO_APPBAR", AppiumBy.id("promo_appbar"));
    public static final Locator PROMO_SCROLLABLE_CHIPS = new Locator("PROMO_SCROLLABLE_CHIPS", AppiumBy.id("tds_chip_group"));
    public static final Locator CHIPS_GROUP = new Locator("CHIPS_GROUP", AppiumBy.id("chip_group"));
    public static final Locator CARD_VIEW = new Locator("CARD_VIEW", AppiumBy.id("card_view"));
    public static final Locator CARD_VIEW_TITLE = new Locator("CARD_VIEW_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'card_view')]//*[contains(@resource-id,'tv_title')]"));
    public static final Locator CARD_VIEW_SUBTITLE = new Locator("CARD_VIEW_SUBTITLE", AppiumBy.xpath("//*[contains(@resource-id,'card_view')]//*[contains(@resource-id,'tv_subtitle')]"));
    public static final Locator BANNER_IMG = new Locator("BANNER_IMG", AppiumBy.xpath("//*[contains(@resource-id,'card_view')]//following-sibling::*[contains(@resource-id,'iv_banner')]"));
    public static final Locator BLOG_PAGE = new Locator("BLOG_PAGE", AppiumBy.xpath("//*[contains(@content-desc,'blog')]"));
    public static final Locator BACK_BTN_BLOG_PAGE = new Locator("BACK_BTN_BLOG_PAGE", AppiumBy.xpath("//*[contains(@resource-id,'toolbar') ]//android.widget.ImageButton"));
    private static final Locator VERTICAL_TITLE = new Locator("VERTICAL_TITLE", AppiumBy.xpath("(//*[contains(@resource-id, 'recyclerView')]//*[contains(@resource-id, 'tv_title') and not(contains(@resource-id, 'tv_subtitle'))])[1]"));
    private static final Locator VERTICAL_IMAGE = new Locator("VERTICAL_IMAGE", AppiumBy.xpath("(//*[contains(@resource-id,'tv_title')]//preceding-sibling::*)[1]"));
    private static final Locator HOME_PAGE_SEARCH_BAR = new Locator("HOME_PAGE_SEARCH_BAR", AppiumBy.xpath("//*[contains(@resource-id,'tv_text') and not(ancestor::*[contains(@resource-id,'finance_bar')])]"));
    private static final Locator FINANCE_BAR = new Locator("FINANCE_BAR", AppiumBy.id("finance_bar"));
    private static final Locator FINANCE_BAR_INFO = new Locator("FINANCE_BAR_INFO", AppiumBy.xpath("(//*[ contains(@resource-id, 'iv_multi_currency_info')])"));
    private static final Locator FINANCE_BAR_TOOL_TIP = new Locator("FINANCE_BAR_TOOL_TIP", AppiumBy.xpath("//*[contains(@resource-id,'finance_bar')]//*[contains(@resource-id,'tv_text')]"));
    private static final Locator SEASONAL_BANNER = new Locator("SEASONAL_BANNER", AppiumBy.id("carousel"));
    private static final Locator SLIDER_BUTTON = new Locator("SLIDER_BUTTON", AppiumBy.id("tds_extended_fab_group"));
    private static final Locator BACK_TO_TOP_MARKER = new Locator("BACK_TO_TOP_MARKER", AppiumBy.xpath("//*[contains(@resource-id,'tds_extended_fab_text') and (contains(@text,'Top') or contains(@text,'Atas'))]"));
    public static final Locator SCREEN = new Locator("SCREEN", AppiumBy.id("action_bar_root"));
    private static final Locator HOME_FEED_RECYCLER = new Locator("HOME_FEED_RECYCLER", AppiumBy.xpath("(//*[contains(@resource-id,'recycler_view') or contains(@resource-id,'recyclerView')])[1]"));
    private static final Locator DISCOVER_SLIDER_BUTTON = new Locator("DISCOVER_SLIDER_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_extended_fab_text') and contains(@text,'Discover')]"));
    private static final Locator RECENT_SEARCH_SECTION = new Locator("RECENT_SEARCH_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'tds_extended_fab_text') and (contains(@text,'Pencarian Terakhir') or contains(@text,'Recently search') or contains(@text,'Recently Search'))]"));
    private static final Locator ICON_HIGHLIGHT = new Locator("ICON_HIGHLIGHT", AppiumBy.id("iv_icon"));
    private static final Locator HIGHLIGHT_DISCOVER_IMAGE = new Locator("HIGHLIGHT_DISCOVER_IMAGE", AppiumBy.xpath("(//*[contains(@resource-id,'iv_background')])[last()]"));
    private static final Locator UNREAD_COUNT = new Locator("UNREAD_COUNT", AppiumBy.xpath("//*[contains(@resource-id,'tds_appbar_items_container')]//*[contains(@resource-id,'tds_appbar_badge')]//android.widget.TextView"));
    private static final Locator WEBVIEW_POST = new Locator("WEBVIEW_POST", AppiumBy.xpath("//*[contains(@class,'post-wrapper')]"));
    private static final Locator BACK_BUTTON_WEBVIEW = new Locator("BACK_BUTTON_WEBVIEW", AppiumBy.className("android.widget.ImageButton"));
    private static final Locator BLIBLI_TIKET_POINTS_FINANCE_BAR = new Locator("BLIBLI_TIKET_POINTS_FINANCE_BAR", AppiumBy.id("tv_points_value"));
    private static final Locator BLIBLI_TIKET_POINTS_PAGE_HEADER = new Locator("BLIBLI_TIKET_POINTS_PAGE_HEADER", AppiumBy.xpath("//*[contains(@text,'Blibli Tiket Rewards')]"));
    private static final Locator BACK_BLIBLI_TIKET_POINTS = new Locator("BACK_BLIBLI_TIKET_POINTS", AppiumBy.xpath("//*[@class='header__btn']"));
    private static final Locator PAYLATER_FINANCE_BAR = new Locator("PAYLATER_FINANCE_BAR", AppiumBy.xpath("//*[contains(@resource-id,'tv_text') and contains(@text,'PayLater')]"));
    private static final Locator BACK_PAYLATER = new Locator("BACK_PAYLATER", AppiumBy.id("btn_back"));
    private static final Locator PAYLATER_PAGE_CAROUSEL= new Locator("PAYLATER_PAGE_CAROUSEL", AppiumBy.xpath("//*[contains(@resource-id,'tv_carousel_title') and contains(@text,'PayLater')]"));
    private static final Locator PAYLATER_USP_BANNER = new Locator("PAYLATER_USP_BANNER", AppiumBy.id("item_banner_carousel"));
    private static final Locator PAYLATER_USP_LIMIT_SECTION = new Locator("PAYLATER_USP_LIMIT_SECTION", AppiumBy.id("item_limit_unregistered"));
    private static final Locator PAYLATER_USP_LIMIT_LABEL = new Locator("PAYLATER_USP_LIMIT_LABEL", AppiumBy.xpath("//*[contains(@resource-id,'tv_limit_label') and contains(@text,'Get a limit of up to')]"));
    private static final Locator PAYLATER_USP_CTA = new Locator("PAYLATER_USP_CTA", AppiumBy.xpath("//*[contains(@resource-id,'cta_text') and contains(@text,'Activate')]"));
    private static final Locator BLIBLIPAY_FINANCE_BAR = new Locator("BLIBLIPAY_FINANCE_BAR", AppiumBy.xpath("//*[contains(@resource-id,'tv_text') and contains(@text,'BlibliPay')]"));
    private static final Locator PAYLATER_DETAIL_TEXT = new Locator("PAYLATER_DETAIL_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'tv_text_paylater') and @text='PayLater']"));
    private static final Locator BLIPAY_DETAIL_TEXT = new Locator("BLIPAY_DETAIL_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'tv_text_blipay') and @text='BlibliPay']"));
    private static final Locator BLIBLIPAY_HEADER = new Locator("BLIBLIPAY_HEADER", AppiumBy.xpath("//*[contains(@resource-id,'linear_web_view')]//android.view.View[@text='BlibliPay']"));
    private static final Locator HIGHLIGHT_ARTICLE_CONTAINER = new Locator("HIGHLIGHT_ARTICLE_CONTAINER", AppiumBy.xpath("//*[contains(@resource-id,'card_view')]//ancestor::*[contains(@resource-id,'recyclerView')]"));
    private static final Locator HIGHLIGHT_ARTICLE_IMAGE = new Locator("HIGHLIGHT_ARTICLE_IMAGE", AppiumBy.xpath("//*[contains(@resource-id,'card_view')]//ancestor::*[contains(@resource-id,'recyclerView')]/preceding-sibling::*[contains(@resource-id,'iv_background')]"));
    private static final Locator PDP_TOOLBAR = new Locator("PDP_TOOLBAR", AppiumBy.xpath("//android.widget.Button[contains(@text,'back-button')]"));
    private static final Locator HIGHLIGHT_DISCOVER_PRODUCT_ICON = new Locator("HIGHLIGHT_DISCOVER_PRODUCT_ICON", AppiumBy.id("iv_icon"));
    private static final Locator COUNTDOWN_CONTINUE_TO_PAYMENT = new Locator("COUNTDOWN_CONTINUE_TO_PAYMENT", AppiumBy.id("view_countdown"));
    private static final Locator CONTINUE_TO_PAYMENT_BTN = new Locator("CONTINUE_TO_PAYMENT_BTN", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Continue Your Payment') or contains(@text,'Lanjutkan Pembayaranmu'))]"));
    private static final Locator GV_CLAIM_CARD_PARENT = new Locator("GV_CLAIM_CARD_PARENT", AppiumBy.xpath("//*[contains(@resource-id,'btn_see_all')]//preceding-sibling::*[contains(@resource-id,'recycler_view')]"));
    public static final Locator PROMO_OVERLAY_BANNER_CLOSE_BTN = new Locator("PROMO_OVERLAY_BANNER_CLOSE_BTN", AppiumBy.xpath("//android.widget.RelativeLayout//android.widget.Image"));
    // Shared across parallel TestNG threads (parallel=methods) via a common session pool, so use
    // thread-safe maps to avoid corruption / cross-thread onboarding-state leakage.
    private static  Map<String, Boolean> FLAG_MAP = new ConcurrentHashMap<>();
    public static  Map<String, Boolean> FLAG_MAP_BRAZE = new ConcurrentHashMap<>();
    // Tracks whether the one-time "halo tiket" coach mark has been dismissed for a session, so we stop
    // paying the detection wait on every subsequent gotoHome() once it has been handled.
    public static  Map<String, Boolean> COACH_MARK_MAP = new ConcurrentHashMap<>();
    // Exclude the Welcome Rewards bottom sheet (crf_bottom_sheet) subtree: its close button also uses
    // iv_close, so an unscoped ticket-popup dismiss during onboarding could close Welcome Rewards on a
    // pooled session that re-entered gotoHome() with the sheet open.
    public static final Locator CANCEL_TICKET_POPUP = new Locator("CANCEL_TICKET_POPUP", AppiumBy.xpath("//*[contains(@resource-id,'iv_close') and not(ancestor-or-self::*[contains(@resource-id,'crf_bottom_sheet')])]"));
    private static final Locator WEBVIEW_LAYOUT = new Locator("WEBVIEW_LAYOUT", AppiumBy.xpath("//*[contains(@resource-id,'com_braze_inappmessage_html_full_webview')]"));
    private static final Locator CLOSE_BUTTON_OVERLAY = new Locator("CLOSE_BUTTON_OVERLAY", AppiumBy.xpath("//*[contains(@class,'button-close')]"));

    //UpcomingBooking module
    private static final Locator UPCOMING_BOOKING_FLIGHT = new Locator("UPCOMING_BOOKING_FLIGHT", AppiumBy.xpath("//*[contains(resource-id,tv_title) and contains(@text,'Flight to') or contains(@text,'Penerbangan ke')]"));
    private static final Locator UPCOMING_BOOKING_STAY = new Locator("UPCOMING_BOOKING_STAY", AppiumBy.xpath("//*[contains(resource-id,tv_title) and contains(@text,'Stay at') or contains(@text,'Menginap di')]"));
    private static final Locator UPCOMING_BOOKING_HOTEL_DETAILS = new Locator("UPCOMING_BOOKING_HOTEL_DETAILS", AppiumBy.id("tv_subTitle"));
    private static final Locator SEE_ALL_UPCOMING_BOOKING = new Locator("SEE_ALL_UPCOMING_BOOKING", AppiumBy.id("tv_see_all_action"));
    private static final Locator TITLE_CROSS_SELL = new Locator("TITLE_CROSS_SELL", AppiumBy.xpath("//*[contains(resource-id,tv_title) and contains(@text,'upcoming preprod EN')]"));
    private static final Locator CROSS_SELL_CARDS = new Locator("CROSS_SELL_CARDS", AppiumBy.id("chip_group"));
    private static final Locator TRANSPORT_CROSS_SELL_CARD = new Locator("TRANSPORT_CROSS_SELL_CARD", AppiumBy.xpath("//*[@text='Transportations']"));
    private static final Locator FLIGHT_CARD_CROSS_SELL = new Locator("FLIGHT_CARD_CROSS_SELL", AppiumBy.xpath("//*[@text='Flights']"));
    public static final Locator UPCOMING_BOOKING_COUNTDOWN = new Locator("UPCOMING_BOOKING_COUNTDOWN", AppiumBy.id("tv_countdown"));
    public static final Locator UPCOMING_BOOKING_DEPARTURE_TIME = new Locator("UPCOMING_BOOKING_DEPARTURE_TIME", AppiumBy.xpath("//*[contains(@resource-id,'card_upcoming_booking')]//*[contains(@resource-id,'tv_date')]"));
    public static final Locator UPCOMING_BOOKING_ROUTE_INFO = new Locator("UPCOMING_BOOKING_ROUTE_INFO", AppiumBy.xpath("//*[contains(@resource-id,'card_upcoming_booking')]//*[contains(@resource-id,'tv_title')]"));
    public static final Locator UPCOMING_BOOKING_VENDOR_ICON = new Locator("UPCOMING_BOOKING_VENDOR_ICON", AppiumBy.xpath("//*[contains(@resource-id,'card_upcoming_booking')]//*[contains(@resource-id,'iv_logo')]"));
    public static final Locator UPCOMING_BOOKING_SEE_MORE_INFO = new Locator("UPCOMING_BOOKING_SEE_MORE_INFO", AppiumBy.xpath("//*[contains(@resource-id,'card_upcoming_booking')]//*[contains(@resource-id,'tv_see_more_action')]"));
    public static final Locator UPCOMING_BOOKING_CLOSE_BUTTON = new Locator("UPCOMING_BOOKING_CLOSE_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'card_upcoming_booking')]/parent::*//*[contains(@resource-id,'iv_close')]"));
    public static final Locator UPCOMING_BOOKING_CROSS_SELL_TITLE = new Locator("UPCOMING_BOOKING_CROSS_SELL_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'card_upcoming_booking')]/parent::*//*[contains(@resource-id,'tv_cross_sell_title')]"));
    public static final Locator UPCOMING_BOOKING_CROSS_SELL_CARD = new Locator("UPCOMING_BOOKING_COUNTDOWN", AppiumBy.xpath("//*[contains(@resource-id,'card_upcoming_booking')]/parent::*//*[contains(@resource-id,'rv_cross_sell')]"));
    public static final Locator UPCOMING_BOOKING_PASSENGER_NAME = new Locator("UPCOMING_BOOKING_PASSENGER_NAME", AppiumBy.xpath("//*[contains(@resource-id,'card_upcoming_booking')]//*[contains(@resource-id,'layout_additional_info')]/child::*[2]"));
    public static final Locator UPCOMING_BOOKING_CLASS_NAME = new Locator("UPCOMING_BOOKING_CLASS_NAME", AppiumBy.xpath("//*[contains(@resource-id,'card_upcoming_booking')]//*[contains(@resource-id,'tv_additional_title')]"));
    public static final Locator UPCOMING_BOOKING_ORIGIN_STATION = new Locator("UPCOMING_BOOKING_ORIGIN_STATION", AppiumBy.xpath("//*[contains(@resource-id,'card_upcoming_booking')]//*[contains(@resource-id,'layout_additional_info')]/child::*[1]"));
    public static final Locator SEE_MORE_UPCOMING_BOOKING_CARD = new Locator("SEE_MORE_UPCOMING_BOOKING_CARD", AppiumBy.xpath("//*[contains(@resource-id,'card_upcoming_booking')]/parent::*/*[contains(@resource-id,'tv_see_all_action')]"));
    public static final Locator UPCOMING_BOOKING_CARD_LIST_BOTTOMSHEET = new Locator("UPCOMING_BOOKING_CARD_LIST_BOTTOMSHEET", AppiumBy.xpath("//*[contains(@resource-id,'crf_bottom_sheet')]//*[contains(@resource-id,'rv_upcoming_bookings')]"));
    public static final Locator UPCOMING_BOOKING_CARD_LIST_BOTTOMSHEET_CLOSE_BUTTON = new Locator("UPCOMING_BOOKING_CARD_LIST_BOTTOMSHEET_CLOSE_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'crf_bottom_sheet')]/*[contains(@resource-id,'ml_root')]/*[contains(@resource-id,'iv_close')]"));
    public static final Locator UPCOMING_BOOKING_HIDE_CARD_BOTTOMSHEET = new Locator("UPCOMING_BOOKING_HIDE_CARD_BOTTOMSHEET", AppiumBy.id("design_bottom_sheet"));
    public static final Locator UPCOMING_BOOKING_HIDE_CARD_BOTTOMSHEET_CANCEL_BUTTON = new Locator("UPCOMING_BOOKING_HIDE_CARD_BOTTOMSHEET_CANCEL_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'design_bottom_sheet')]//*[contains(@resource-id,'tds_btn_secondary_info_view')]"));
    public static final Locator UPCOMING_BOOKING_CARD = new Locator("UPCOMING_BOOKING_CARD", AppiumBy.xpath("//*[contains(@resource-id,'card_upcoming_booking')]"));
    public static final Locator UPCOMING_BOOKING_VOUCHER_BOX = new Locator("UPCOMING_BOOKING_VOUCHER_BOX", AppiumBy.xpath("//*[contains(@resource-id,'rv_cross_sell')]//*[contains(@resource-id,'container')]//*[contains(@resource-id,'tv_title2') and contains(@text,'Voucher')]"));
    public static final Locator UPCOMING_BOOKING_ALL_PROMO = new Locator("UPCOMING_BOOKING_ALL_PROMO", AppiumBy.xpath("//*[contains(@resource-id,'rv_cross_sell')]//*[contains(@resource-id,'container')]//*[contains(@resource-id,'tv_title2') and contains(@text,'Promo')]"));
    public static final Locator UPCOMING_BOOKING_VERTICAL = new Locator("UPCOMING_BOOKING_VERTICAL", AppiumBy.xpath("//*[contains(@resource-id,'rv_cross_sell')]//*[contains(@resource-id,'container') and (./*[contains(@resource-id,'tv_title')])  and (./*[contains(@resource-id,'iv_plus')])]"));
    public static final Locator UPCOMING_BOOKING_VERTICAL_TEXT = new Locator("UPCOMING_BOOKING_VERTICAL_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'rv_cross_sell')]//*[contains(@resource-id,'container')]/*[contains(@resource-id,'tv_title') and not(contains(@resource-id,'2'))]"));
    public static final Locator CROSS_SELL_CAROUSEL = new Locator("CROSS_SELL_CAROUSEL", AppiumBy.xpath("//*[contains(@resource-id,'rv_cross_sell')]"));
    public static final Locator WISHLIST_ICON = new Locator("WISHLIST_ICON", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_bottom_navigation') and @text='Wishlist']"));
    public static final Locator WISHLIST_PAGE = new Locator("WISHLIST_PAGE", AppiumBy.xpath("//*[@text='Wishlist Landing']"));
    public static final Locator BRAZE_CLOSE = new Locator("BRAZE_CLOSE", AppiumBy.xpath("//*[contains(@resource-id,'com_braze_inappmessage_html')]//android.widget.Image"));
    public static final Locator CURRENT_CURRENCY = new Locator("CURRENT_CURRENCY", AppiumBy.xpath("(//*[contains(@resource-id, \"tv_text\")])[1]"));
    public static final Locator FINANCE_BAR_TITLE = new Locator("FINANCE_BAR_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'finance_bar')]//*[contains(@resource-id,'tv_title')]"));
    // Exclude the Welcome Rewards bottom sheet (crf_bottom_sheet) subtree: its close button also uses
    // iv_close, so an unscoped promo-dismiss would close Welcome Rewards instead of a promo popup.
    public static final Locator PROMO_POPUP = new Locator("PROMO_POPUP", AppiumBy.xpath("//*[(contains(@resource-id,'layout_close') or contains(@resource-id,'iv_close')) and not(ancestor-or-self::*[contains(@resource-id,'crf_bottom_sheet')])]"));
    public static final Locator COACH_MARK_OK_BUTTON = new Locator("COACH_MARK_OK_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_frame_coach_mark')]//*[contains(@resource-id,'tds_btn')]"));
    public static final Locator CURRENCY_COUNTRY_POPUP = new Locator("CURRENCY_COUNTRY_POPUP", AppiumBy.id("crf_bottom_sheet"));
    public static final Locator CURRENCY_COUNTRY_POPUP_TITLE = new Locator("CURRENCY_COUNTRY_POPUP_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'crf_bottom_sheet')]//*[contains(@resource-id,'tv_title')]"));
    public static final Locator CURRENCY_COUNTRY_CONTINUE_BUTTON = new Locator("CURRENCY_COUNTRY_CONTINUE_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'btn_apply')]//*[contains(@resource-id,'tds_button_view')]"));
    public static final Locator SEE_MORE_VERTICAL = new Locator("SEE_MORE_VERTICAL", AppiumBy.id("tv_see_more"));
    private static final Locator WELCOME_REWARDS_SHEET = new Locator("WELCOME_REWARDS_SHEET", AppiumBy.xpath("//*[contains(@resource-id,'crf_bottom_sheet')]//*[contains(@text,'lucky gift') or contains(@text,'New User Mission') or contains(@text,'Earn IDR') or contains(@text,'Kejutan buat pengguna baru') or contains(@text,'Misi Pengguna Baru') or contains(@text,'Raih IDR')]"));
    private static final Locator WELCOME_REWARDS_CLOSE = new Locator("WELCOME_REWARDS_CLOSE", AppiumBy.xpath("//*[contains(@resource-id,'crf_bottom_sheet')]//*[contains(@resource-id,'motionLayout')]//*[contains(@resource-id,'iv_close')]"));

    public HomePageAndroid(Session<T> session) {
        super(session);
    }

    private Locator getMessageLocator(String message) {
        String messageLower = message == null ? "" : message.toLowerCase();
        return new Locator("messageNoDeepLink", AppiumBy.xpath("(//*[contains(@resource-id,'tv_inbox_title') and contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'"+messageLower+"')])[1]"));
    }

    @Override
    public void closePromoPopup() {
    }

    @Override
    public void closeCurrencyCountryPopup() {
        // The currency/country sheet can surface slightly after the deeplink resolves, so poll a
        // few times rather than a single check. Best-effort: its absence is fine (the popup may not appear).
        for (int attempt = 1; attempt <= 3; attempt++) {
            if (isCurrencyCountryPopupDisplayed()) {
                logger.log("Currency/country selection popup detected - accepting via continue button (attempt "
                        + attempt + ")");
                clickCurrencyCountryPopupPrimaryButton();
                return;
            }
        }
    }

    @Override
    public boolean isCurrencyCountryPopupDisplayed() {
        return driverTools.waitForElementToAppear(CURRENCY_COUNTRY_POPUP, 10).isPresent();
    }

    @Override
    public void clickCurrencyCountryPopupPrimaryButton() {
        driverTools.waitForElementToAppear(CURRENCY_COUNTRY_CONTINUE_BUTTON, 10).orElseThrow();
        driverTools.click(CURRENCY_COUNTRY_CONTINUE_BUTTON);
        driverTools.waitForElementToDisappear(CURRENCY_COUNTRY_POPUP, 15);
    }

    @Override
    public void closeGiveRatingPopup() {
    }

    private boolean isProductListOnHomePageSwipelable() {
        driverTools.swipeByDistance(CAROUSEL,Swipe.RIGHT,0.8);
        driverTools.swipeByDistance(CAROUSEL,Swipe.RIGHT,0.8);
        final String productNameBeforeSwipe = driverTools.waitForElementToAppear(FIRST_PRODUCT,10).orElseThrow().getText();
        driverTools.waitForElementToAppear(CAROUSEL,10).ifPresent(e->driverTools.swipeLeft(e));
        final String productNameAfterSwipe = driverTools.waitForElementToAppear(FIRST_PRODUCT,10).orElseThrow().getText();
        return productNameBeforeSwipe!=productNameAfterSwipe;
    }

    private Locator getGSWarningLocator(String expectedWarningKeywordId,String expectedWarningKeywordEn,String currentAppLang){
        if(currentAppLang.equals("en")){
            return new Locator("GS_WARNING_KEYWORD", AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_notification_banner')]//*[contains(@text, '"+expectedWarningKeywordEn+"')]"));
        }else{
            return new Locator("GS_WARNING_KEYWORD", AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_notification_banner')]//*[contains(@text, '"+expectedWarningKeywordId+"')]"));
        }
    }

    private Locator getVerticalLocator(String verticalName){
        return new Locator("HOME_PRODUCT_ICON",AppiumBy.xpath(
                switch (verticalName.toLowerCase()) {
                    case "to dos", "to do" ->
                            "//*[contains(@resource-id,'ll_vertical_item')]//*[contains(@resource-id,'tv_title') and (contains(@text,'To Do') or contains(@text,'To Dos') or contains(@text,'Aktivitas') or contains(@text,'Activity') or contains(@text,'Experience') or contains(@text,'Things To Do'))]";
                    case "car transfers", "jemputan bandara", "airport pickups" ->
                            "//*[contains(@resource-id,'ll_vertical_item')]//*[contains(@resource-id,'tv_title') and (contains(@text,'Jemputan') or contains(@text,'Bandara') or contains(@text,'Airport Pickups') or contains(@text,'Airport'))]";
                    case "hotel" ->
                            "//*[contains(@resource-id,'ll_vertical_item')]//*[contains(@resource-id,'tv_title') and contains(@text,'" + verticalName + "')]";
                    case "homes" ->
                            "//*[contains(@resource-id,'ll_vertical_item')]//*[contains(@resource-id,'tv_title') and (@text='" + verticalName + "') or (contains(@text,'Villas & Apt') or contains(@text,'Villa & Apt') or contains(@text,'Vila & Apt'))]";
                    case "flights", "pesawat", "flight" ->
                            "//*[contains(@resource-id,'ll_vertical_item')]//*[contains(@resource-id,'tv_title') and (@text='" + verticalName + "') or (contains(@text,'Flight') or contains(@text,'Flights') or contains(@text,'Pesawat'))]";
                    case "trains", "train", "kereta api", "kereta" ->
                            "//*[contains(@resource-id,'ll_vertical_item')]//*[contains(@resource-id,'tv_title') and (contains(@text,'Kereta') or contains(@text,'Train'))]";
                    default ->
                            "//*[contains(@resource-id,'ll_vertical_item')]//*[contains(@resource-id,'tv_title') and contains(@text,'" + verticalName + "')]";
                }
        ));
    }

    public Locator getCampaignLabelProduct(String productName) {
        if (productName.contains("/")) {
            String productEn = productName.split("/")[0];
            String productId = productName.split("/")[1];
            return new Locator("CAMPAIGN_LABEL_PRODUCT", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'"+productId+"') or contains(@text,'"+productEn+"'))]//parent::*[contains(@resource-id,'container')]//parent::*[contains(@resource-id,'card_view')]//following-sibling::*[contains(@resource-id,'ribbon')]//*[contains(@resource-id,'tds_ribbon_label')]"));
        } else {
            return new Locator("CAMPAIGN_LABEL_PRODUCT", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'"+productName+"')]//parent::*[contains(@resource-id,'container')]//parent::*[contains(@resource-id,'card_view')]//following-sibling::*[contains(@resource-id,'ribbon')]//*[contains(@resource-id,'tds_ribbon_label')]"));
        }
    }


    private void clickHomeProduct(String vertical) {
        goToHomeDeeplink();
        waitForHomePageToLoad();
        driverTools.swipeToElement(TIKET_POINTS_CONTAINER,Swipe.DOWN,3).orElseThrow();

        driverTools.waitForElementToAppear(BLIBLI_POINTS,10);
        Locator HOME_PRODUCT_ICON = this.getVerticalLocator(vertical);

        if (!driverTools.isElementDisplayed(HOME_PRODUCT_ICON)) {
            driverTools.waitForElementToPresence(HOME_PRODUCT_ICON, 50);
            driverTools.click(SEE_ALL_PRODUCT);
            driverTools.swipeByDistanceToElement(HOME_PRODUCT_ICON,ALL_PRODUCT_CONTAINER,Swipe.UP,0.7,5).orElseThrow();
        }
        driverTools.click(HOME_PRODUCT_ICON);
    }

    private boolean checkHomeProduct(String vertical) {
        final Locator HOME_PRODUCT_ICON = this.getVerticalLocator(vertical);
        driverTools.waitForElementToAppear(HOME_PRODUCT_ICON);
        if(!driverTools.isElementDisplayed(HOME_PRODUCT_ICON)) driverTools.click(SEE_ALL_PRODUCT);
        boolean isProductDisplayed = driverTools.isElementDisplayed(HOME_PRODUCT_ICON);
        logger.log("is product displayed : " + isProductDisplayed);
        return isProductDisplayed;
    }

    private boolean isVerticalPageOpen(String verticalName) {
        final Locator VERTICAL_ICON;
        switch(verticalName) {
            case "Airport Pickups" :
                VERTICAL_ICON = new Locator("VERTICAL_ICON", AppiumBy.className("android.webkit.WebView")); break;
            case "Car Rentals" :
                VERTICAL_ICON = new Locator("VERTICAL_ICON", AppiumBy.xpath("//*[contains(@text,'Car Rentals') or contains(@text,'Rental Mobil')]")); break;
            case "To Dos" :
                VERTICAL_ICON = new Locator("VERTICAL_ICON", AppiumBy.id("tds_search_et")); break;
            case "Trains" :
                VERTICAL_ICON = new Locator("VERTICAL_ICON", AppiumBy.id("fl_search_form")); break;
            case "Flights":
                VERTICAL_ICON = new Locator("VERTICAL_ICON", AppiumBy.id("tds_search_form_title_group")); break;
            case "Hotels":
                VERTICAL_ICON = new Locator("VERTICAL_ICON", AppiumBy.id("vg_search_form")); break;
            default :
                throw new NotImplementedException("Unimplemented vertical type!");
        }
        return driverTools.waitForElementToPresence(VERTICAL_ICON).isPresent();
    }

    private Locator getHotelInventoryLocator(String hotelName) {
        return new Locator("HOTEL_NAME", AppiumBy.xpath("//*[contains(@text,'"+hotelName+"')]"));
    }

    private boolean isSessionInEnglish() {
        return driverTools.waitForElementToPresence(BOTTOM_NAV_ENGLISH_LABELS, 5).isPresent();
    }

    public String getLanguageApp() {
        String language = "";
        if (isSessionInEnglish()) language = "en";
        else language = "id";
        logger.log("language :" + language);
        return language;
    }

    public Locator getCardLocator(String cardName) {
        String feedScope = "//*[contains(@resource-id,'recycler_view') or contains(@resource-id,'recyclerView')]";
        String titleNode = "(contains(@resource-id,'tv_title') or contains(@resource-id,'tv_subtitle'))";
        if (cardName.contains("/") && !cardName.contains("//")) {
            String en = splitLanguageData(cardName, "/")[0];
            String id = splitLanguageData(cardName, "/")[1];
            return new Locator("CARD_LOCATOR", AppiumBy.xpath(feedScope + "//*[" + titleNode
                    + " and (contains(@text,\"" + en + "\") or contains(@text,\"" + id + "\"))]"));
        }
        return new Locator("CARD_LOCATOR", AppiumBy.xpath(feedScope + "//*[" + titleNode
                + " and contains(@text,\"" + cardName + "\")]"));
    }

    private void initFlag(String sessionID, boolean check){
        FLAG_MAP.put(sessionID,check);
    }

    public void initFlagBraze(String sessionID, boolean check){
        FLAG_MAP_BRAZE.put(sessionID,check);
    }


    @Override
    public void selectInfoPagesItem() {
        List<WebElement> items = driverTools.elements(INFO_PAGES_ITEMS).orElseThrow();
        int totalItem = items.size();
        int randomItem = generateRandomInt(0, totalItem - 1);
        logger.log("index of randomItem -> " + randomItem);
        driverTools.click(items.get(randomItem));
        driverTools.waitForElementToAppear(INFO_PAGES_TOOLBAR).orElseThrow();
    }

    @Override
    public boolean isSuggestionItemListsShown() {
        boolean title = driverTools.waitForElementToAppear(SUGGESTION_LIST_TITLE).isPresent();
        boolean subtitle = driverTools.waitForElementToAppear(SUGGESTION_LIST_SUBTITLE).isPresent();
        boolean icon = driverTools.waitForElementToAppear(SUGGESTION_LIST_ICON).isPresent();
        logger.log("status of the title -> " + title + ", subtitle -> " + subtitle + ", icon -> " + icon);
        return title && subtitle && icon;
    }

    @Override
    public ArrayList<String> getSuggestionItemListTitle() {
        driverTools.waitForElementToAppear(SUGGESTION_LIST_TITLE,10).orElseThrow();
        ArrayList<String> listTitles = new ArrayList<>();
        List<WebElement> listTitleElements = driverTools.elements(SUGGESTION_LIST_TITLE).orElseThrow();
        for(WebElement listTitleElement: listTitleElements){
            String title = driverTools.getText(listTitleElement);
            logger.log("text on the title -> " + title);
            listTitles.add(title);
        }
        return listTitles;
    }

    @Override
    public String selectInfoPagesTab(String title) {
        String selectedTab;
        switch (title) {
            case "random" -> {
                Locator selectedInfoPagesTab = randomInfoPagesTab();
                selectedTab = driverTools.getText(selectedInfoPagesTab).orElseThrow();
                driverTools.click(selectedInfoPagesTab);
            }
            case "policy" -> {
                selectedTab = driverTools.getText(INFO_PAGES_POLICY_TAB).orElseThrow();
                driverTools.click(INFO_PAGES_POLICY_TAB);
            }
            case "benefit" -> {
                selectedTab = driverTools.getText(INFO_PAGES_BENEFIT_TAB).orElseThrow();
                driverTools.click(INFO_PAGES_BENEFIT_TAB);
            }
            default -> throw new NotImplementedException("\"" + title + "\" Not implemented");
        }
        return selectedTab;
    }

    private Locator randomInfoPagesTab() {
        int randomInt = generateRandomInt(0, 1);
        if (randomInt == 0) {
            return INFO_PAGES_POLICY_TAB;
        } else {
            return INFO_PAGES_BENEFIT_TAB;
        }
    }


    @Override
    public String selectInfoPagesItem(JSONObject objectItem, String selectedItem) {
        JSONArray item = objectItem.getJSONArray(selectedItem);
        int randomItem = generateRandomInt(0, item.length() - 1);
        logger.log("index of randomItem -> " + randomItem);

        List<WebElement> items = driverTools.elements(INFO_PAGES_ITEMS).orElseThrow();
        driverTools.click(items.get(randomItem));

        return item.getString(randomItem);
    }

    @Override
    public boolean isInfoPagesDisplayed() {
        driverTools.waitForElementToPresence(BACK_BUTTON).orElseThrow();
        return isInfoPageContentDisplayed();
    }

    @Override
    public void gotoHome() {
        String sessionID = session.sessionId();

        //if failure happened when automation is in webview context it will switch back to native back.
        driverTools.switchToNative();

        // Check if already on home page - early exit optimization
        if(FLAG_MAP.get(sessionID) != null && FLAG_MAP.get(sessionID) && isOnHomePage()){
            logger.log("Already on home page, skipping onboarding");
            // Handle BRAZE popup if needed (reduced wait time)
            if(Configs.ENVIRONMENT == Environment.PROD && FLAG_MAP_BRAZE.get(sessionID)==null){
                driverTools.waitForElementToPresence(BRAZE_CLOSE, 5).ifPresent(element -> {
                    driverTools.clickElementByCoordinate(element);
                    initFlagBraze(sessionID,true);
                });
            }
            // Quick check for homepage load (reduced retries)
            for (int i=0; i<3 && !waitForHomePageToLoad(); i++) {
                logger.log("Waited for homepage to load, retrying " + (i+1) + " times.");
            }
            coachMarkHandling();
            // NOTE: no coach-mark dismissal here. The "halo tiket" coach mark is a one-time, post-onboarding
            // overlay, so it is handled in the onboarding branch (below) on first gotoHome (or by the Welcome
            // Rewards flow). Calling it on this already-onboarded early-exit path would only add repeated
            // detection waits on every reused pooled driver for no benefit.
            return;
        }

        // Handle BRAZE popup before onboarding (reduced wait time from 10 to 5)
        if(Configs.ENVIRONMENT == Environment.PROD && FLAG_MAP_BRAZE.get(sessionID)==null){
            driverTools.waitForElementToPresence(BRAZE_CLOSE, 5).ifPresent(element -> {
                driverTools.clickElementByCoordinate(element);
                initFlagBraze(sessionID,true);
            });
        }

        if(FLAG_MAP.get(sessionID)==null || !FLAG_MAP.get(sessionID) ){
            logger.log("on-boarding process start");
            boolean check = false;
            int c=0;

            // Optimize: Reduced wait times for onboarding elements
            if(driverTools.waitForElementToAppear(CONTINUE_BUTTON_FIRST_PAGE, 5).isPresent()){
                driverTools.click(CONTINUE_BUTTON_FIRST_PAGE);
                if(!driverTools.waitForElementToDisappear(CONTINUE_BUTTON_FIRST_PAGE, 5)) {
                    driverTools.click(CONTINUE_BUTTON_FIRST_PAGE);
                }
                c++;
            }
            if (driverTools.waitForElementToAppear(SKIP_NOTIFICATION, 2).isPresent()) {
                driverTools.click(SKIP_NOTIFICATION);
                driverTools.waitForElementToPresence(ALLOW_NOTIFICATION, 2).ifPresent(driverTools::click);
                c++;
            }
            if(driverTools.waitForElementToAppear(SKIP_ONBOARDING, 5).isPresent()){
                driverTools.click(SKIP_ONBOARDING);
                c++;
            }
            if(c==3){
                check = true;
            }

            // Check if on home page before calling expensive goToHomeDeeplink()
            if(isOnHomePage()){
                check = true;
            }

            initFlag(sessionID, check);
            logger.log("on-boarding process Success: " + check);

            // Only call goToHomeDeeplink() if needed (consolidated logic)
            boolean needsDeeplink = !check || (Configs.ENVIRONMENT == Environment.PROD && FLAG_MAP_BRAZE.get(sessionID)==null) || !isOnHomePage();

            if(needsDeeplink){
                goToHomeUsingDeeplinkWithoutCondition();

                // Handle BRAZE after deeplink (reduced wait time, only if not already handled)
                if(Configs.ENVIRONMENT == Environment.PROD && FLAG_MAP_BRAZE.get(sessionID)==null){
                    driverTools.waitForElementToPresence(BRAZE_CLOSE, 5).ifPresent(element -> {
                        driverTools.clickElementByCoordinate(element);
                        initFlagBraze(sessionID,true);
                    });
                }
            }

        }else {
            goToHomeUsingDeeplinkWithoutCondition();
        }

        // Optimize: Reduced retries from 10 to 5, and add timeout check
        for (int i=0; i<5 && !waitForHomePageToLoad(); i++) {
            logger.log("Waited for homepage to load, retrying " + (i+1) + " times.");
            if(i == 4){
                logger.log("Homepage load check failed after 5 retries, but continuing");
            }
        }
    }

    @Override
    public void goToHomeDeeplink() {
        String sessionID = session.sessionId();

        // Optimize: Check if BRAZE already handled to avoid redundant isOnHomePage() call
        boolean brazeAlreadyHandled = FLAG_MAP_BRAZE.get(sessionID) != null;

        try {
            ApplicationState appState = ((AndroidDriver) driver).queryAppState(PackageName.get());
            if (appState != ApplicationState.NOT_INSTALLED && appState != ApplicationState.NOT_RUNNING) {
                driverTools.closeApp();
            }
        } catch (Exception e) {
            logger.log("Skipping closeApp: " + e.getMessage());
        }
        Deeplink homeDeeplink = new Deeplink("Home",
                "tiket://gatotkaca.tiket.com",
                "tiket://preprod.tiket.com",
                "tiket://www.tiket.com");
        driverTools.deeplink(homeDeeplink);

        //Handle continue button if present (quick check)
        if (driverTools.isElementDisplayed(CONTINUE_BUTTON)) {
            driverTools.click(CONTINUE_BUTTON);
        }

        // Optimize: Handle BRAZE popup with reduced wait time
        if (Configs.ENVIRONMENT == Environment.PROD && !brazeAlreadyHandled) {
            // Reduced wait time: Use 5 seconds instead of 10 (isOnHomePage check was expensive)
            if (driverTools.waitForElementToPresence(BRAZE_CLOSE, 5).isPresent()) {
                driverTools.clickElementByCoordinate(BRAZE_CLOSE);
                initFlagBraze(sessionID, true);
            }
        }
        coachMarkHandling();
    }

    @Override
    public void goToHomeUsingDeeplinkWithoutCondition(){
        Deeplink homeDeeplink = new Deeplink("Home",
                "tiket://gatotkaca.tiket.com",
                "tiket://preprod.tiket.com",
                "tiket://www.tiket.com");
        try {
            driverTools.deeplink(homeDeeplink);
            coachMarkHandling();
        }catch (Exception e){
            logger.log(e.getLocalizedMessage());
        }
    }

    @Override
    public boolean isWelcomeRewardsShown(int seconds) {
        return driverTools.waitForElementToAppear(WELCOME_REWARDS_SHEET, seconds).isPresent();
    }

    @Override
    public void closeWelcomeRewards() {
        if (isWelcomeRewardsShown(3)) {
            driverTools.waitForElementToAppear(WELCOME_REWARDS_CLOSE, 5).ifPresent(driverTools::click);
        }
    }

    @Override
    public void markOnboardingComplete() {
        initFlag(session.sessionId(), true);
    }

    @Override
    public void clearAppDataAndRelaunch() {
        String sessionID = session.sessionId();
        FLAG_MAP.remove(sessionID);
        FLAG_MAP_BRAZE.remove(sessionID);
        logger.log("Cleared onboarding flags for session before app data wipe");
        driverTools.clearAppDataAndRelaunch();
    }

    private static Locator recentSearchChip(String chipTitle) {
        return new Locator("RECENT_SEARCH_CHIP", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Pencarian Terakhir') or contains(@text,'Recently search') or contains(@text,'Recently Search'))]//following-sibling::*[contains(@resource-id,'chip_group')]//*[contains(@resource-id,'tds_chips_text') and contains(@text,'"+chipTitle+"')]"));
    }

    @Override
    public void goToRecentSearch(String chipTitle) {
        driverTools.swipeDown(2);
        if (driverTools.isElementDisplayed(RECENT_SEARCH_SECTION)) driverTools.click(RECENT_SEARCH_SECTION);
        driverTools.swipeToElement(RECENT_SEARCH_CONTAINER, Swipe.UP, 3);
        if (driverTools.isElementDisplayed(RECENT_SEARCH_SECTION)) driverTools.click(RECENT_SEARCH_SECTION);

        Locator chipLocator;
        if (chipTitle.equalsIgnoreCase("homes")) {
            chipLocator = HOMES_RECENT_SEARCH_CHIP;
        } else {
            chipLocator = recentSearchChip(chipTitle);
        }

        if (driverTools.swipeToElement(chipLocator, RECENT_SEARCH_CONTAINER, Swipe.DOWN, 2).isPresent()) {
            driverTools.click(chipLocator);
        } else {
            logger.log("There is no chip button on the recent search section");
            driverTools.swipeToElement(RECENT_SEARCH_TITLE, Swipe.UP, 1);
        }
    }

    @Override
    public void clearRecentSearch() {
        if (driverTools.isElementDisplayed(CLEAR_RECENT_SEARCH)) {
            driverTools.click(CLEAR_RECENT_SEARCH);
            driverTools.click(CONFIRM_CLEAR_RECENT_SEARCH);
            driverTools.waitForElementToDisappear(CLEAR_RECENT_SEARCH);
        }
    }

    private static Locator recentSearchLocation(String locationEn, String locationId) {
        return new Locator("RECENT_SEARCH_LOCATION", AppiumBy.xpath("//*[contains(@resource-id,'card_view')]//*[contains(@resource-id,'view_title')]//*[contains(@class,'TextView') and (contains(@text,'" + locationEn + "') or contains(@text,'" + locationId + "'))]"));
    }

    private static Locator recentSearchHotelName(String hotelName) {
        return new Locator("RECENT_SEARCH_HOTELNAME", AppiumBy.xpath("//*[contains(@resource-id,'card_view')]//*[contains(@resource-id,'tv_title') and contains(@text,'" + hotelName + "')]"));
    }

    private static Locator recentSearchAttribute(String attributeName, String otherAttribute) {
        return new Locator("RECENT_SEARCH_ATTRIBUTE", AppiumBy.xpath("//*[contains(@resource-id,'card_view')]//*[contains(@resource-id,'view_attribute')]//*[contains(@class,'TextView') and (contains(@text,'" + attributeName + "') and contains(@text,'" + otherAttribute + "'))]"));
    }

    @Override
    public boolean checkRecentSearch(boolean location, String recentSearchCardtitle, String recentSearchAttribute) {
        driverTools.waitForElementToAppear(HOME_PAGE_PROMO_CHIP);

        String locationEn;
        String locationId;
        if (recentSearchCardtitle.contains("/")) {
            locationEn = splitLanguageData(recentSearchCardtitle, "/")[0];
            locationId = splitLanguageData(recentSearchCardtitle, "/")[1];
        } else {
            locationEn = recentSearchCardtitle;
            locationId = recentSearchCardtitle;
        }

        Locator locator;
        if (location) {
            locator = recentSearchLocation(locationEn, locationId);
        } else {
            locator = recentSearchHotelName(recentSearchCardtitle);
        }
        Assertion.assertThat("recent search title card: \""+recentSearchCardtitle+"\" should be displayed on the recent search section", driverTools.swipeToElement(locator, Swipe.UP, 20).isPresent(), is(true));

        String checkInDate;
        String checkOutDate;
        if (recentSearchAttribute.contains(" - ")) {
            checkInDate = recentSearchAttribute.split(" - ")[0];
            checkOutDate = recentSearchAttribute.split(" - ")[1];
        } else {
            checkInDate = recentSearchAttribute;
            checkOutDate = recentSearchAttribute;
        }
        Assertion.assertThat("recent attribute: \""+recentSearchAttribute+"\" should be displayed on the recent search section", driverTools.swipeToElement(recentSearchAttribute(checkInDate, checkOutDate), Swipe.UP, 10).isPresent(), is(true));
        return true;
    }

    private static Locator recentSearchTotalGuest(int guest) {
        return new Locator("RECENT_SEARCH_TOTAL_GUEST", AppiumBy.xpath("//*[contains(@resource-id,'card_view')]//*[contains(@resource-id,'view_attribute')]//*[@text='•']//following-sibling::*//*[contains(@class,'TextView') and @text='" + guest + "']"));
    }

    @Override
    public boolean checkTotalAllGuestRecentSearch(int totalAllGuest) {
        return driverTools.isElementDisplayed(recentSearchTotalGuest(totalAllGuest));
    }

    @Override
    public void clickHomeIcon() {
        driverTools.click(HOME);
    }

    @Override
    public Boolean waitForHomePageToLoad() {
        // Optimize: Reduced wait time from default (10s) to 6 seconds for faster checks
        return driverTools.waitForElementToPresence(HOME_PAGE_PROMO_CHIP, 6).isPresent();
    }

    @Override
    public void openAccount() {
        driverTools.waitForElementToAppear(HOME_PAGE_SEARCH_BAR,5);
        driverTools.waitForElementToAppear(ACCOUNT_ICON,10).orElseThrow();
        driverTools.click(ACCOUNT_ICON);
        try{
            String isAccountSelected = driverTools.getValueAttribute(ACCOUNT_ICON,"selected");
            logger.log("is on account page : " + isAccountSelected);
            int i = 0;
            while (isAccountSelected.equals("false") && i<3) {
                driverTools.click(ACCOUNT_ICON);
                driverTools.swipeDown(1);
                isAccountSelected = driverTools.getValueAttribute(ACCOUNT_ICON,"selected");
                logger.log("Retry \'"+i+"\'.Is on account page : " + isAccountSelected);
                i++;
            }
            if(i==3 && isAccountSelected.equals("false")){
                Assertion.fail("Fail to open account page");
            }
        }catch(Exception e){
            //do nothing
            logger.log("Fail in retrying open account. Account page could be open already");
        }
    }

    private String convertVerticalNameIfRequired(String verticalName){
        if (!isSessionInEnglish()) {
            verticalName = this.translateVerticalNameToBhs(verticalName);
        }
        return verticalName;
    }

    @Override
    public void openVerticalPage(String vertical) {
        if (!driverTools.isElementDisplayed(HERRO_BANNER)) driverTools.click(HOME);
        final String verticalName=convertVerticalNameIfRequired(vertical);
        this.clickHomeProduct(verticalName);
    }

    @Override
    public boolean checkVerticalPresence(String vertical){
        final String verticalName=convertVerticalNameIfRequired(vertical);
        return checkHomeProduct(verticalName);
    }

    @Override
    public boolean verifyIconsProduct() {
        CarTransferSearchPage<T> airportTransferSearchPage = CarTransferSearchPage.getInstance(session);
        SearchPageCarRental<T> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        FlightSearchPage<T> flightPage = FlightSearchPage.getInstance(session);
        TrainSearchPage<T> trainSearchPage = TrainSearchPage.getInstance(session);

        this.openVerticalPage("Flights");
        flightPage.closePopup();
        boolean isFlightVerticalPageOpen = this.isVerticalPageOpen("Flights");
        driverTools.back();

        this.openVerticalPage("Hotels");
        boolean isHotelsVerticalPageOpen = this.isVerticalPageOpen("Hotels");
        driverTools.back();

        this.openVerticalPage("Trains");
        boolean isTrainVerticalPageOpen = this.isVerticalPageOpen("Trains");
        trainSearchPage.skipPopup();
        driverTools.back();

        this.openVerticalPage("To Dos");
        boolean isToDosVerticalPageOpen = this.isVerticalPageOpen("To Dos");
        driverTools.back();

        this.openVerticalPage("Airport Pickups");
        airportTransferSearchPage.allowLocationAccess();
        airportTransferSearchPage.closeInformationPopUp();
        boolean isAirportTransferVerticalPageOpen = this.isVerticalPageOpen("Airport Pickups");
        driverTools.back();

        this.openVerticalPage("Car Rentals");
        carRentalSearchPage.closeInformationPopUp();
        boolean isCarRentalsVerticalPageOpen = this.isVerticalPageOpen("Car Rentals");
        driverTools.back();

        boolean isProductListHomePageSwipeable = this.isProductListOnHomePageSwipelable();

        logger.log("is flight vertical open -> " + isFlightVerticalPageOpen);
        logger.log("is hotel vertical open -> " + isHotelsVerticalPageOpen);
        logger.log("is to do vertical open -> " + isToDosVerticalPageOpen);
        logger.log("is airport transfer vertical open -> " + isAirportTransferVerticalPageOpen);
        logger.log("is car rental vertical open -> " + isCarRentalsVerticalPageOpen);
        logger.log("is train vertical open -> " + isTrainVerticalPageOpen);
        logger.log("is product list swipeable -> " + isProductListHomePageSwipeable);

        return isFlightVerticalPageOpen && isHotelsVerticalPageOpen && isToDosVerticalPageOpen
                && isAirportTransferVerticalPageOpen && isCarRentalsVerticalPageOpen && isTrainVerticalPageOpen && isProductListHomePageSwipeable;
    }

    @Override
    public boolean isOnHomePage() {
        closePromoPopup();
        return driverTools.waitForElementToPresence(SEARCH_ICON).isPresent();
    }

    @Override
    public boolean isOnHomePage(int duration) {
        closePromoPopup();
        return driverTools.waitForElementToPresence(SEARCH_ICON,duration).isPresent();
    }

    @Override
    public boolean isUserLogin(){
        // Login state uses ACCOUNT_BOTTOM_NAV (Account/Akun), not INBOX (app bar message icon).
        Optional<List<WebElement>> quick = driverTools.waitForPresenceElementsList(ACCOUNT_BOTTOM_NAV, 3);
        if (quick.isPresent()) {
            int n = quick.get().size();
            if (n == 1 || n == 2) {
                logger.log("User login status (Account/Akun tab visible, count=" + n + ")");
                return true;
            }
        }
        driverTools.swipeToPresentedElement(SEE_MORE_VERTICAL, Swipe.DOWN, 3);
        if (driverTools.waitForElementToPresence(ACCOUNT_BOTTOM_NAV, 10).isEmpty()) {
            logger.log("User login status: not logged in (Account/Akun tab not found)");
            return false;
        }
        List<WebElement> accountTabs = driverTools.waitForPresenceElementsList(ACCOUNT_BOTTOM_NAV)
                .orElse(Collections.emptyList());
        if (!(accountTabs.size() == 1 || accountTabs.size() == 2)) {
            driverTools.swipeDown();
            accountTabs = driverTools.waitForPresenceElementsList(ACCOUNT_BOTTOM_NAV)
                    .orElse(Collections.emptyList());
        }
        int count = accountTabs.size();
        logger.log("User login status (Account/Akun matches): " + count);
        return count == 1 || count == 2;
    }

    @Override
    public void clickInboxIcon() {
        WebElement e = driverTools.waitForElementToAppear(INBOX,10).orElseThrow();
        driverTools.click(e);
    }

    @Override
    public boolean isInboxListOpen() {
        Boolean isInboxHeaderPresent = driverTools.waitForElementToAppear(INBOX_HEADER,15).isPresent();
        logger.log("is inbox header present -> " + isInboxHeaderPresent);
        return isInboxHeaderPresent;
    }

    @Override
    public void clickInboxMessage(String message) {
        final Locator messageNoDeeplink = getMessageLocator(message);
        WebElement e = driverTools.swipeToElement(messageNoDeeplink,Swipe.UP,12).orElseThrow();
        driverTools.click(e);
        driverTools.waitForElementToDisappear(messageNoDeeplink,3);
    }

    @Override
    public boolean isInboxDetailHasSubtitle() {
        return driverTools.element(INBOX_DETAIL_SUBTITLE).isPresent();
    }

    @Override
    public String clickOneOfFlashSaleInventory() {
        driverTools.swipeToElement(FLASH_SALE_INVENTORY,Swipe.UP, 4);
        WebElement e = driverTools.element(FLASH_SALE_INVENTORY).orElseThrow();
        String flashSaleInventoryName = e.getText();
        logger.log("Flash sale Inventory name: " + flashSaleInventoryName);
        driverTools.click(e);
        return flashSaleInventoryName;
    }

    private Locator getFlashSaleInventoryContainer(String flashSaleInventoryName){
        return new Locator("FLASH_SALE_INVENTORY_CONTAINER", AppiumBy.xpath("//android.widget.TextView[contains(@text,\""+flashSaleInventoryName+"\")]//following-sibling::*[contains(@resource-id,'recycler_view')]"));
    }

    @Override
    public boolean isFlashListSaleSwipeAble(String flashSaleInventoryName) {
        driverTools.swipeToElement(FLASH_SALE_INVENTORY, Swipe.UP, 2);
        String hotelInventoryNameBefore = driverTools.element(FLASH_SALE_INVENTORY).orElseThrow().getText();
        logger.log("Inventory name before swipe -> " + hotelInventoryNameBefore);
        driverTools.swipeLeft(getFlashSaleInventoryContainer(flashSaleInventoryName));
        String hotelInventoryNameAfter = driverTools.element(FLASH_SALE_INVENTORY).orElseThrow().getText();
        logger.log("Inventory name after swipe -> " + hotelInventoryNameAfter);
        return hotelInventoryNameBefore!=hotelInventoryNameAfter;
    }

    @Override
    public boolean isFlashSaleInventoryPricePresent(String currency) {
        driverTools.swipeToElement(FLASH_SALE_INVENTORY_PRICE,Swipe.UP,1);
        Optional<WebElement> e = driverTools.waitForElementToAppear(FLASH_SALE_INVENTORY_PRICE,10);
        return e.isPresent();
    };

    @Override
    public boolean isFlashSaleCountDownPresent() {
        Optional<WebElement> hourCountDown = driverTools.swipeToElement(FLASH_SALE_HOUR_COUNTDOWN,Swipe.UP, 2);
        boolean isHourCountDownPresent = hourCountDown.isPresent();
        boolean isMinuteCountDownPresent = driverTools.element(FLASH_SALE_MINUTE_COUNTDOWN).isPresent();
        boolean isSecondCountDownPresent = driverTools.element(FLASH_SALE_SECOND_COUNTDOWN).isPresent();
        logger.log("Hour countdown -> "+isHourCountDownPresent);
        logger.log("Minute countdown -> "+isMinuteCountDownPresent);
        logger.log("Second countdown -> "+isSecondCountDownPresent);
        return isHourCountDownPresent && isMinuteCountDownPresent && isSecondCountDownPresent;
    }

    @Override
    public void clickSeeAllFlashSale(String flashSaleTitle) {
        WebElement flashSaleSeeAllBtn = driverTools.swipeToElement(getFlashSaleSeeAllLocator(flashSaleTitle), Swipe.UP, 2).orElseThrow();
        driverTools.click(flashSaleSeeAllBtn);
    }

    @Override
    public boolean verifyRedirectFlashSale(String flashSaleInventoryName) {
        Locator hotelName = this.getHotelInventoryLocator(flashSaleInventoryName);
        Boolean isRedirectToHotelDetail = driverTools.waitForElementToAppear(hotelName,20).isPresent();
        return isRedirectToHotelDetail;
    }

    @Override
    public void swipeToCard(String cardTitle) {
        logger.log("Swiping to card with title: " + cardTitle);
        clickHomeIcon();
        clickHomeIcon();
        driverTools.swipeToElement(TIKET_POINTS_CONTAINER, Swipe.DOWN, 10);
        final int maxSwipeAttempts = 100;
        boolean found = false;
        boolean bottom;
        for(int i=0; i<maxSwipeAttempts; i++) {
            found = driverTools.isElementDisplayed(getCardLocator(cardTitle));
            bottom = driverTools.isElementDisplayed(BACK_TO_TOP_MARKER);
            if(!found && !bottom) {
                driverTools.swipeUp();
            } else {
                break;
            }
        }
        if(!found) {
            throw new RuntimeException("Card with title '" + cardTitle + "' not found after " + maxSwipeAttempts + " swipes.");
        }
        driverTools.swipeElementToMidScreen(getCardLocator(cardTitle));
    }

    @Override
    public void goToSection(String sectionTitle) {
        final int maxSwipeAttempts = 100;
        driverTools.swipeByDistanceToElement(getCardLocator(sectionTitle), SCREEN,Swipe.UP,0.6,maxSwipeAttempts).orElseThrow();
        driverTools.swipeByDistance(SCREEN,Swipe.UP,0.05);
    }

    @Override
    public String getCampaignProduct(String productName) {
        Locator campaignLabel = getCampaignLabelProduct(productName);
        driverTools.swipeToElement(campaignLabel, Swipe.UP, 3);
        return driverTools.getText(campaignLabel).orElseThrow();
    }

    private Locator subtitleSection(String titleSection) {
        return new Locator("SUBTITLE", AppiumBy.xpath("//*[@text='" + titleSection + "' and contains(@resource-id,'tv_title')]//following-sibling::*[contains(@resource-id,'tv_subtitle')]"));
    }

    @Override
    public String getTheSubtitle(String titleSection) {
        return driverTools.getText(subtitleSection(titleSection)).orElseThrow();
    }

    private Locator allChipsButton(String titleSection) {
        return new Locator("ALL_CHIPS_BUTTON", AppiumBy.xpath("//*[@text='" + titleSection + "' and contains(@resource-id,'tv_title')]//following-sibling::*[contains(@resource-id,'chip_group')]//*[contains(@resource-id,'tds_chips_text')]"));
    }

    private Locator chipsGroup(String titleSection) {
        return new Locator("CHIPS_GROUP", AppiumBy.xpath("//*[@text='" + titleSection + "' and contains(@resource-id,'tv_title')]//following-sibling::*[contains(@resource-id,'chip_group')]"));
    }

    @Override
    public boolean swipeChipsButton(String titleSection) {
        boolean swipeStatus;
        driverTools.waitForElementToPresence(allChipsButton(titleSection)).orElseThrow();
        List<WebElement> chipsButton = driverTools.elements(allChipsButton(titleSection)).orElseThrow();
        int totalChipsButton = chipsButton.size();
        logger.log("totalChipsButton -> " + totalChipsButton);
        if (totalChipsButton > 3) {
            driverTools.swipeLeft(chipsGroup(titleSection), 2);
            swipeStatus = true;
        } else {
            swipeStatus = false;
        }
        return swipeStatus;
    }

    private Locator selectedChipsButton(String titleSection, String chipText) {
        if (chipText.equals("")) {
            return new Locator("SELECTED_CHIP_BUTTON", AppiumBy.xpath("//*[@text='" + titleSection + "' and contains(@resource-id,'tv_title')]//following-sibling::*[contains(@resource-id,'chip_group')]//*[contains(@resource-id,'tds_chips_text') and @selected='true']"));
        } else {
            return new Locator("SELECTED_CHIP_BUTTON", AppiumBy.xpath("//*[@text='" + titleSection + "' and contains(@resource-id,'tv_title')]//following-sibling::*[contains(@resource-id,'chip_group')]//*[contains(@resource-id,'tds_chips_text') and @selected='true' and @text='" + chipText + "']"));
        }
    }

    @Override
    public boolean compareBeforeAfterSwipe(String titleSection, String item, String inventoryName, boolean isSwipe) {
        boolean status;
        if (isSwipe) {
            switch (item.toLowerCase()) {
                case "chips" -> status = driverTools.element(selectedChipsButton(titleSection, "")).isEmpty();
                case "inventory" -> {
                    String currentInventoryName = driverTools.getText(allInventoryName(titleSection)).orElseThrow();
                    status = !currentInventoryName.equals(inventoryName);
                }
                default -> throw new RuntimeException("invalid item section -> " + item);
            }
        } else {
            logger.softWarning("isSwipe is " + false);
            status = true;
        }
        return status;
    }

    @Override
    public String selectChipsButton(String titleSection, String chipsText) {
        String chipButtonText;
        if (chipsText.equals("random")) {
            driverTools.waitForElementToPresence(allChipsButton(titleSection)).orElseThrow();
            List<WebElement> chipButtonItem = driverTools.elements(allChipsButton(titleSection)).orElseThrow();
            int totalChipItem = chipButtonItem.size();
            logger.log("totalChipItem -> " + totalChipItem);
            int randomItem = generateRandomInt(0, totalChipItem - 1);
            logger.log("index of randomItem -> " + randomItem);
            chipButtonText = driverTools.getText(chipButtonItem.get(randomItem));
            driverTools.click(chipButtonItem.get(randomItem));
        } else {
            chipButtonText = driverTools.getText(selectedChipsButton(titleSection, chipsText)).orElseThrow();
            driverTools.click(selectedChipsButton(titleSection, chipsText));
        }
        return chipButtonText;
    }

    private Locator ctaButton(String titleSection) {
        return new Locator("CTA_BUTTON", AppiumBy.xpath("//*[@text='" + titleSection + "' and contains(@resource-id,'tv_title')]//following-sibling::*[contains(@resource-id,'btn_see_all')]//*[contains(@resource-id,'tds_button_view')]"));
    }

    @Override
    public String getCtaButtonTextApp(String titleSection) {
        driverTools.waitForElementToAppear(ctaButton(titleSection)).orElseThrow();
        return driverTools.getText(ctaButton(titleSection)).orElseThrow();
    }

    @Override
    public String getInventoryName(String titleSection) {
        driverTools.swipeByDistanceToElement(allInventoryName(titleSection),SCREEN,Swipe.UP,0.6,30).orElseThrow();
        return driverTools.getText(allInventoryName(titleSection)).orElseThrow();
    }

    private Locator allInventoryName(String titleSection) {
        return new Locator("ALL_INVENTORY_NAME", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and @text='" + titleSection + "']/ancestor::android.view.ViewGroup//following-sibling::*[contains(@resource-id,'recycler_view')]//*[contains(@resource-id,'card_view')]//*[contains(@resource-id,'tv_title')]"));
    }

    /**
     * Cards of the section headed by {@code titleSection}. The heading is matched with
     * {@code contains()} — same as {@link #getCardLocator(String)} — because headings carry
     * trailing punctuation ("Take a peek at these recommendations!") that callers rarely pass in,
     * and an exact {@code @text} match silently finds nothing.
     */
    private Locator sectionInventoryCard(String titleSection) {
        return new Locator("SECTION_INVENTORY_CARD", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,\"" + titleSection + "\")]/ancestor::android.view.ViewGroup//following-sibling::*[contains(@resource-id,'recycler_view')]//*[contains(@resource-id,'card_view')]//*[contains(@resource-id,'tv_title')]"));
    }

    @Override
    public String getSectionInventoryName(String titleSection) {
        Locator inventoryName = sectionInventoryCard(titleSection);
        driverTools.swipeByDistanceToElement(inventoryName, SCREEN, Swipe.UP, 0.3, 30).orElseThrow();
        String name = driverTools.getText(inventoryName).orElseThrow();
        logger.log("First inventory on section '" + titleSection + "' -> " + name);
        return name;
    }

    private Locator inventoryFinalPrice(String titleSection, String inventoryName) {
        return new Locator("INVENTORY_FINAL_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,\"" + titleSection + "\")]/ancestor::android.view.ViewGroup//following-sibling::*[contains(@resource-id,'recycler_view')]//*[contains(@resource-id,'card_view') and .//*[contains(@resource-id,'tv_title') and @text=\"" + inventoryName + "\"]]//*[contains(@resource-id,'price_module_final_price')]"));
    }

    @Override
    public String getInventoryPrice(String titleSection, String inventoryName) {
        Locator finalPrice = inventoryFinalPrice(titleSection, inventoryName);
        driverTools.swipeByDistanceToElement(finalPrice, SCREEN, Swipe.UP, 0.3, 30).orElseThrow();
        String priceText = driverTools.getText(finalPrice).orElseThrow();
        logger.log("Final price of '" + inventoryName + "' on section '" + titleSection + "' -> " + priceText);
        return priceText;
    }

    private Locator allInventoryBanner(String titleSection) {
        return new Locator("ALL_INVENTORY_BANNER", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and @text='" + titleSection + "']/ancestor::android.view.ViewGroup//*[contains(@resource-id,'iv_banner')]"));
    }

    @Override
    public boolean swipeInventoryList(String titleSection) {
        boolean swipeStatus;
        List<WebElement> inventoryList = driverTools.elements(allInventoryName(titleSection)).orElseThrow();
        int totalInventory = inventoryList.size();
        logger.log("totalInventory -> " + totalInventory);
        if (totalInventory > 2) {
            driverTools.swipeLeft(getCardViewParentLocator(titleSection), 2);
            swipeStatus = true;
        } else {
            swipeStatus = false;
        }
        return swipeStatus;
    }

    @Override
    public String selectInventory(String titleSection, String inventoryList, String productType) {
        List<WebElement> inventoryItem;
        if (productType.equalsIgnoreCase("WEBVIEW")) {
            driverTools.swipeToFullyVisibleElement(allInventoryBanner(titleSection), Swipe.UP, 3).orElseThrow();
            inventoryItem = driverTools.elements(allInventoryBanner(titleSection)).orElseThrow();
        } else {
            driverTools.swipeToFullyVisibleElement(allInventoryName(titleSection), Swipe.UP, 3).orElseThrow();
            inventoryItem = driverTools.elements(allInventoryName(titleSection)).orElseThrow();
        }
        int indexItem;
        if (inventoryList.equals("random")) {
            indexItem = generateRandomInt(0, inventoryItem.size() - 1);
        } else {
            indexItem = Integer.parseInt(inventoryList);
        }
        logger.log("index of indexItem -> " + indexItem);
        String inventoryName = driverTools.getText(inventoryItem.get(indexItem));
        if (productType.equalsIgnoreCase("WEBVIEW")) inventoryName = String.valueOf(indexItem);
        driverTools.click(inventoryItem.get(indexItem));
        return inventoryName;
    }


    @Override
    public void clickCtaButton(String titleSection) {
        driverTools.swipeToElement(getCTAButtonLocator(titleSection), Swipe.UP, 3);
        driverTools.click(getCTAButtonLocator(titleSection));
    }

    private Locator getCTAButtonLocator(String titleSection) {
        return new Locator("PAGE_MODULE_CTA_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'recycler_view')]//*[./*[contains(@resource-id,'tv_title') and @text='"+titleSection+"']]//*[contains(@resource-id,'tds_button_view')]"));
    }

    @Override
    public void clickMemberCompleteNow() {
        driverTools.click(MEMBER_COMPLETE_NOW_BUTTON);
    }

    @Override
    public void clickCard(String cardType) {} // FIXME: Implementation pending

    @Override
    public void openMyOrderPage() {
        waitForHomePageToLoad();
        closeWelcomeRewards();
        driverTools.click(MY_ORDER);
        if(driverTools.waitForElementToAppear(MY_ORDER_HEADER,5).isEmpty()){
            driverTools.click(MY_ORDER);
        }
    }

    @Override
    public void clickOnRewardButton() {
        waitForHomePageToLoad();
        driverTools.click(REWARDS_BOTTOM_NAV);
    }

    @Override
    public void clickLoginButton() {
        driverTools.waitForElementToAppear(LOGIN_BUTTON).orElseThrow();
        driverTools.click(LOGIN_BUTTON);

        driverTools.waitForElementToAppear(LOGIN_REGISTER_BUTTON).orElseThrow();
        driverTools.click(LOGIN_REGISTER_BUTTON);
    }

    @Override
    public void verifyInboxIcon() {
        // FIXME: Implementation pending
    }

    @Override
    public void setLocationService(String action) {
        SettingPage<T> settingPage = SettingPage.getInstance(session);

        driverTools.waitForElementToAppear(ACCOUNT_ICON).ifPresent(driverTools::click);
        settingPage.setLocationSwitch(action.toUpperCase());
        settingPage.backToHome();
    }

    private Locator getCurrencyLocator(String currency){
        return new Locator("currency",AppiumBy.xpath("//*[contains(@resource-id,'tv_code') and contains(@text,'"+currency.toUpperCase()+"')]"));
    }

    @Override
    public void clickVideo() {
        driverTools.swipeToElement(VIDEO_TITLE, Swipe.UP, 20);
        driverTools.click(VIDEO_BANNER);
    }

    @Override
    public void clickHeroBanner() {
        driverTools.waitForElementToAppear(HERRO_BANNER,5).ifPresentOrElse(driverTools::click, ()-> {throw new NoSuchElementException();});
    }

    public void clickGlobalSearch() {
        Locator searchEntry = driverTools.waitForAnyLocator(10,
                        SEARCH_ICON, SEARCH_ICON_OLD, GS_HOME_PAGE_SEARCH_BAR, HOME_PAGE_SEARCH_BAR)
                .orElseThrow(() -> new NoSuchElementException("Global search bar not found in home page"));
        driverTools.waitForElementToBeClickable(searchEntry, 5)
                .ifPresentOrElse(driverTools::click, () -> {
                    throw new NoSuchElementException("Global search bar not clickable in home page");
                });
        driverTools.waitForElementToAppear(GS_INPUT_BAR, 20)
                .orElseThrow(() -> new NoSuchElementException("Global search page did not open"));
    }

    @Override
    public boolean verifyGlobalSearchInfo(GSLandingResponse res) {
        driverTools.hideKeyboard(1);
        Boolean isInfoBannerVisible = driverTools.swipeToElement(GS_INFO_BANNER, Swipe.UP, 10).isPresent();
        Assertion.assertThat("Verify info banner appear", isInfoBannerVisible, is(true));
        GSLandingResponse.Data gsInfoData = res.data.stream().filter(e->e.templateCode.equals("GS_INFO")).findFirst().orElseThrow();
        // check gs info banner scrollable
        for(int i = 0; i<3;i++){
            WebElement gsInfoElement = driverTools.waitForElementToAppear(GS_INFO, 10).orElseThrow();
            Assertion.assertThat("Verify info module text is like from api (Assertion index : "+i+")", gsInfoElement.getText(),
                    containsString(gsInfoData.dynamicContents.get(i).buttonText));
            driverTools.swipeLeftLong(GS_INFO_CONTAINER);
            Boolean gsCloseVisible = driverTools.isElementDisplayed(GS_INFO_CLOSE);
            Assertion.assertThat("Verify close button visible", gsCloseVisible, is(true));
        }
        // check if gs info clickable
        driverTools.click(GS_INFO);
        Boolean isGSBarVisible = driverTools.waitForElementToDisappear(GS_INFO,10);
        Assertion.assertThat("Verify info link clickable,by checking gs bar not visible", !isGSBarVisible, is(false));
        return true;
    }

    private Locator getGSInfoBannerLocator(String sectionTitle){
        return new Locator("GS_INFO_BANNER_COMPONENT", AppiumBy.xpath("(//*[contains(@resource-id,'section_title') and @text='"+sectionTitle+"']//following-sibling::androidx.recyclerview.widget.RecyclerView//\t\n" + //
                "android.view.ViewGroup)[1]"));
    }

    @Override
    public boolean verifyGLobalSearchHolidayInspiration(GSLandingResponse res) {
        String holidayInspiration = res.data.stream().filter(e->e.templateCode.equals("IS_INFO")).findFirst().orElseThrow().moduleDetail.heading.title;
        Locator holidayInspirationLocator = getGSInfoBannerLocator(holidayInspiration);
        WebElement holidayInspirationElement = driverTools
                .swipeToElement(holidayInspirationLocator, Swipe.UP, 10).orElseThrow(() -> {
                    throw new NoSuchElementException("Cannot find global search info banner");
                });
                
        driverTools.click(holidayInspirationElement);
        Boolean isGSBarVisible = driverTools.waitForElementToDisappear(GS_HOME_PAGE_SEARCH_BAR, 10);
        Assertion.assertThat("Verify info link clickable,by checking gs bar not visible", !isGSBarVisible, is(false));
        // back to home as we don't know what page its lead to
        gotoHome();
        // back to global search
        clickGlobalSearch();
        return true;
    }

    @Override
    public void enterKeywordToGlobalSearchBar(String keyword) {
        driverTools.waitForElementToAppear(GS_INPUT_BAR,10).ifPresent(driverTools::click);
        driverTools.sendKeys(GS_INPUT_BAR, keyword);
    }

    @Override
    public void openGlobalSearchResult(String keyword){
        driverTools.click(getGlobalSearchLocator(keyword));
    }

    private Locator getGlobalSearchLocator(String keyword) {
        return new Locator("GLOBAL_LOCATOR", AppiumBy.xpath("//*[contains(@resource-id, 'tv_title') and @text='" + keyword + "']"));
    }

    @Override
    public boolean verifyGlobalSearcWarningMinimunChar(String expectedWarningKeywordId,String expectedWarningKeywordEn,String currentAppLang) {
        WebElement warningKeywordElement = driverTools.waitForElementToAppear(getGSWarningLocator(expectedWarningKeywordId, expectedWarningKeywordEn,currentAppLang),5).orElseThrow();
        return driverTools.isElementDisplayed(warningKeywordElement);
    }

    @Override
    public void clickPromoChip() {
        WebElement promoChipElement = driverTools.swipeToElement(HOME_PAGE_PROMO_CHIP, Swipe.DOWN, 10).orElseThrow();
        driverTools.click(promoChipElement);
    }

    @Override
    public boolean verifyLocationBanner() {

        scrollUntilBottom(15);

        boolean titleHeadingAppears = driverTools.isElementDisplayed(LOCATION_BANNER_TITLE_HEADING);
        Assertion.assertThat("Title Heading appear",titleHeadingAppears,is(true));

        boolean subTitleHeadingAppears = driverTools.isElementDisplayed(LOCATION_BANNER_SUBTITLE_HEADING);
        Assertion.assertThat("Subtitle Heading appear",subTitleHeadingAppears,is(true));

        boolean iconAppears = driverTools.isElementDisplayed(LOCATION_BANNER_ICON);
        Assertion.assertThat("icon appear",iconAppears,is(true));

        boolean crossButtonClickable = driverTools.waitForElementToBeClickable(LOCATION_BANNER_CROSS_BUTTON).isPresent();
        Assertion.assertThat("croos button appear",crossButtonClickable,is(true));

        boolean titleAppears = driverTools.isElementDisplayed(LOCATION_BANNER_CONTENT_TITLE);
        Assertion.assertThat("Title content appear",titleAppears,is(true));

        boolean subTitleAppears = driverTools.isElementDisplayed(LOCATION_BANNER_CONTENT_SUBTITLE);
        Assertion.assertThat("Subtitle content appear",subTitleAppears,is(true));

        return titleHeadingAppears && subTitleHeadingAppears && iconAppears && crossButtonClickable && titleAppears && subTitleAppears;
    }

    @Override
    public void clickAllowLocationFromBanner() {
        driverTools.click(LOCATION_BANNER_ICON);
        driverTools.click(ALLOW_PERMISSION_BUTTON);
        if (driverTools.isElementDisplayed(ALLOW_LOCATION_ONE_TIME)) {
            driverTools.click(ALLOW_LOCATION_ONE_TIME);
        }
    }

    @Override
    public boolean isLocationBannerDisappear() {
        return !(driverTools.isElementDisplayed(LOCATION_BANNER_TITLE_HEADING) && driverTools.isElementDisplayed(LOCATION_BANNER_ICON));
    }

    @Override
    public void clickCtaButton(String[] sectionTitle) {
        driverTools.swipeToElement(getSectionButton(sectionTitle[0].trim()), Swipe.UP, 3);
        driverTools.click(getSectionButton(sectionTitle[0].trim()));
    }

    @Override
    public boolean isCurrencyOnPageModuleCorrect(String[] targetModuleNames, String currency) {
        boolean result = false;
        for(String moduleTitle : targetModuleNames) {
            swipeToCard(moduleTitle);
            driverTools.swipeByDistanceToElement(getPriceLocatorOnCTASection(moduleTitle),SCREEN,Swipe.UP,0.1,10).orElseThrow();
            driverTools.swipeByDistance(SCREEN,Swipe.UP,0.05);
            List<WebElement> priceElement = driverTools.elements(getPriceLocatorOnCTASection(moduleTitle)).orElseThrow();

            for(WebElement element : priceElement) {
                String displayedPriceText = driverTools.getText(element);
                logger.log("PRICE TEXT : " + displayedPriceText);
                if(!displayedPriceText.contains(currency) || displayedPriceText.contains("Penuh") || displayedPriceText.contains("Terjual") || displayedPriceText.contains("Fully") || displayedPriceText.contains("Sold")) {
                    result = false;
                    break;
                } else {
                    result = true;
                }
            }
        }
        return result;
    }

    private Locator getPriceLocatorOnCTASection(String moduleTitle) {
        return new Locator("PRICE_LOCATOR", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and @text='"+moduleTitle+"']/ancestor::android.view.ViewGroup[1]/..//*[contains(@resource-id,'price_module_final_price') or contains(@resource-id,'tv_final_price')]"));
    }

    private Locator getSectionButton(String sectionTitle) {
        return new Locator("CTA_BUTTON", AppiumBy.xpath("//android.view.ViewGroup[./*[contains(@resource-id,'tv_title') and @text='"+sectionTitle+"']]//parent::*//*[contains(@resource-id,'btn_see_all')]"));
    }

    @Override
    public void clickActivatePinCard() {
        WebElement activatePinCard = driverTools.swipeToElement(ACTIVATE_PIN,Swipe.UP,20).orElseThrow();
        driverTools.click(activatePinCard);
    }

    @Override
    public boolean isOnPromoPage() {
        boolean isPromoContainerVisible = driverTools.waitForElementToAppear(PROMO_CONTAINER,10).isPresent();
        logger.log("isPromoContainerVisible-->"+isPromoContainerVisible);
        boolean isVoucherBoxVisible = driverTools.isElementDisplayed(PROMO_VOUCHER_BOX);
        logger.log("isVoucherBoxVisible-->"+isVoucherBoxVisible);
        boolean isScrollBarScrollable = Boolean.parseBoolean(driverTools.getValueAttribute(PROMO_PAGE_SCROLL_BAR, "scrollable"));
        logger.log("isScrollBarScrollable-->"+isScrollBarScrollable);
        // The chip group only reports scrollable="true" when the chips overflow the screen width,
        // which varies with the promo content, so presence is what identifies the page.
        boolean isChipsVisible = driverTools.isElementDisplayed(PROMO_SCROLLABLE_CHIPS);
        logger.log("isChipsVisible-->"+isChipsVisible);
        boolean isAppBarVisible = driverTools.isElementDisplayed(PROMO_APPBAR);
        logger.log("isAppBarVisible-->"+isAppBarVisible);

        return isPromoContainerVisible && isVoucherBoxVisible && isScrollBarScrollable && isChipsVisible && isAppBarVisible;
    }

    private Locator getSeeAllBtn(String titleCardDefaultChips){
        return new Locator("SEE_ALL_BTN_CARD_DEFAULT_CHIPS",AppiumBy.xpath("(//*[contains(@resource-id,'tv_title') and contains(@text,'"+titleCardDefaultChips+"')]//following-sibling::*[contains(@resource-id,'btn_see_all')])[1]"));
    }

    private Locator getCardViewParentLocator(String titleCardDefaultChips){
        return new Locator("PARENT_CARD_VIEW_CARD_DEFAULT_CHIPS",AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'"+titleCardDefaultChips+"')]//following-sibling::*[contains(@resource-id,'recycler_view')]"));
    }

    @Override
    public boolean verifyCardDefaultChips(HomePageModule.PageModules pageModules){
        String cardDefaultChipsTitle = pageModules.moduleDetail.heading.title;

        boolean isChipsGroupDisplayed = driverTools.isElementDisplayed(CHIPS_GROUP);
        logger.log("is chips group displayed : " + isChipsGroupDisplayed);

        boolean isCardDisplayed = driverTools.isElementDisplayed(CARD_VIEW);
        logger.log("is card displayed : " + isCardDisplayed);

        boolean isImageBannerDisplayed = driverTools.isElementDisplayed(BANNER_IMG);
        logger.log("is image banner displayed : " + isImageBannerDisplayed);

        boolean isTitles = driverTools.isElementDisplayed(CARD_VIEW_TITLE);
        logger.log("is card view titles displayed : " + isTitles);

        boolean isSubTitles = driverTools.isElementDisplayed(CARD_VIEW_SUBTITLE);
        logger.log("is card view sub titles displayed : " + isSubTitles);

        WebElement seeAllBtn = driverTools.swipeToElement(getSeeAllBtn(cardDefaultChipsTitle),Swipe.UP,2).orElseThrow();
        boolean isSeeAllBtnDisplayed = driverTools.isElementDisplayed(seeAllBtn);
        logger.log("is see all btn displayed : " + isSeeAllBtnDisplayed);

        return  isChipsGroupDisplayed && isCardDisplayed && isImageBannerDisplayed
                && isTitles && isSubTitles && isSeeAllBtnDisplayed;
    }

    private Locator getChipsLocator(String text){
        return new Locator("CHIPS_LOCATOR", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and contains(@text,'"+text+"')]"));
    }

    private Locator getCardsTitleLocator(String text){
        return new Locator("CARDS_VIEW_TITLE_LOCATOR", AppiumBy.xpath("//*[contains(@resource-id,'card_view')]//*[contains(@resource-id,'tv_title') and contains(@text,'"+text+"')]"));
    }

    private boolean scrollAndSelect(String desc,Locator elementToScroll,Locator elementParent){
        WebElement element = driverTools.swipeToElement(elementToScroll,elementParent,Swipe.LEFT,5).orElseThrow();
        boolean isElementDisplayed = driverTools.isElementDisplayed(element);
        logger.log(desc + " scrollable : " + isElementDisplayed);
        driverTools.click(element);

        return isElementDisplayed;
    }

    private boolean scroll(String desc,Locator elementToScroll,Locator elementParent){
        WebElement element = driverTools.swipeToElement(elementToScroll,elementParent,Swipe.LEFT,5).orElseThrow();
        boolean isElementDisplayed = driverTools.isElementDisplayed(element);
        logger.log(desc + " scrollable : " + isElementDisplayed);

        return isElementDisplayed;
    }

    @Override
    public boolean VerifyScrollCardDefaultChips(HomePageModule.PageModules pageModules){
        String cardDefaultChipsTitleApi = pageModules.moduleDetail.heading.title;
        int cardViewSizeFirst = pageModules.dynamicContents.get(0).chips.get(0).cardDefaults.size();
        String cardViewLastApi = pageModules.dynamicContents.get(0).chips.get(0).cardDefaults.get(cardViewSizeFirst-1).contentTitle;

        int chipsSize = pageModules.dynamicContents.get(0).chips.size();
        String chipsLast = pageModules.dynamicContents.get(0).chips.get(chipsSize-1).text;

        boolean isCardScrollable = scroll("card",getCardsTitleLocator(cardViewLastApi),getCardViewParentLocator(cardDefaultChipsTitleApi));
        logger.log("is card scrollable : " + isCardScrollable);

        boolean isChipsScrollableAndClickable = scrollAndSelect("chips",getChipsLocator(chipsLast),CHIPS_GROUP);
        logger.log("is card scrollable : " + isChipsScrollableAndClickable);

        return isCardScrollable && isChipsScrollableAndClickable;
    }

    @Override
    public boolean verifyClickCard(HomePageModule.PageModules pageModules){
        int chipsSize = pageModules.dynamicContents.get(0).chips.size();
        String cardViewTitle = null;

        for(HomePageModule.CardDefault cardDefault : pageModules.dynamicContents.get(0).chips.get(chipsSize-1).cardDefaults){
            if(cardDefault.url.contains("blog")){
                cardViewTitle = cardDefault.contentTitle;
                break;
            }
        }
        driverTools.click(getCardsTitleLocator(cardViewTitle));

        WebElement blogPage = driverTools.waitForElementToAppear(BLOG_PAGE,15).orElseThrow();
        boolean isOnBlogPage = driverTools.isElementDisplayed(blogPage);
        logger.log("is on blog page : " + isOnBlogPage);
        return isOnBlogPage;
    }

    @Override
    public void clickBackBtn(){
        try {
            driverTools.click(BACK_BTN_BLOG_PAGE);
        } catch (Exception e) {
            driverTools.back();
        }
    }

    @Override
    public boolean clickSeeAllBtn(HomePageModule.PageModules pageModules){
        String cardDefaultChipsTitleApi = pageModules.moduleDetail.heading.title;
        driverTools.click(getSeeAllBtn(cardDefaultChipsTitleApi));
        return isOnHomePage();
    }

    public boolean verifyGlobalSearchVerticalSection(String verticalName) {
        boolean isVerticalImageAppear = driverTools.isElementDisplayed(VERTICAL_IMAGE);
        Assertion.assertThat("Is vertical image appear", isVerticalImageAppear, is(true));
        driverTools.waitForElementToAppear(getTitleNameLocator(verticalName));
        boolean isVerticalTitleAppear = driverTools.isElementDisplayed(getTitleNameLocator(verticalName));
        Assertion.assertThat("Is vertical title appear", isVerticalTitleAppear, is(true));
        driverTools.click(VERTICAL_TITLE);
        logger.log("Redirected to vertical: " + verticalName);
        boolean isRedirectCorrect = driverTools.isElementDisplayed(getVerticalNameLocator(verticalName));
        Assertion.assertThat("Is redirected to correct vertical", isRedirectCorrect, is(true));
        // Back to home as we don't know what page it leads to
        gotoHome();
        // Back to global search
        clickGlobalSearch();
        return isVerticalImageAppear && isVerticalTitleAppear && isRedirectCorrect;
    }

    private Locator getTitleNameLocator(String verticalName) {
        return new Locator(verticalName, AppiumBy.xpath("(//*[contains(@resource-id,'tv_title') and contains(@text,'"+verticalName+"')])[1]"));
    }

    private Locator getVerticalNameLocator(String verticalName) {
        return new Locator(verticalName, AppiumBy.xpath("//*[contains(@resource-id,'initial_state_title') and contains(@text,'"+verticalName+"')]"));
    }
    private Locator getArticleNameLocator(String articleName) {
        return new Locator(articleName, AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'"+articleName+"')]"));
    }

    private Locator getSubtitleLocator(String subtitleText) {
        return new Locator(subtitleText, AppiumBy.xpath("//*[contains(@resource-id,'tv_subtitle') and contains(@text,'"+subtitleText+"')]"));
    }

    private Locator getButtonLocator(String buttonText) {
        return new Locator(buttonText, AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and contains(@text,'"+buttonText+"')]"));
    }

    @Override
    public boolean verifyGlobalSearchArticleSection(GSSearchPageResponse response) {
        driverTools.hideKeyboard(1);
        GSSearchPageResponse.Datum articlesData = response.data.stream().filter(d -> d.type.equals("ARTICLES")).findFirst().orElseThrow();
        ArrayList<String> titles = new ArrayList<String>();
        for (int i = 0; i < articlesData.contents.size(); i++) {
            titles.add(articlesData.contents.get(i).title);
        }
        int randomNumber = generateRandomInt(0, titles.size() - 1);
        driverTools.swipeToElement(getArticleNameLocator(titles.get(randomNumber)),Swipe.UP,10);
        driverTools.click(getArticleNameLocator(titles.get(randomNumber)));
        Boolean isGSBarVisible = driverTools.waitForElementToDisappear(GS_HOME_PAGE_SEARCH_BAR, 10);
        Assertion.assertThat("Verify Article title clickable", !isGSBarVisible, is(false));
        return  isGSBarVisible;
    }

    @Override
    public void inputSearchKeyword(String keyword) {
        String searchInput = "";
        if(keyword.contains("/")) {
            searchInput += keyword.split("/")[0];
        } else {
            searchInput += keyword;
        }
        driverTools.click(HOME_PAGE_SEARCH_BAR);
        driverTools.sendKeys(GS_INPUT_BAR, searchInput);
    }

    @Override
    public boolean checkRunningTextFinanceBar() {
        ArrayList<String> financeBarList = new ArrayList<>();
        int i=0;
        while(i<10){
            WebElement financeBar = driverTools.waitForElementToPresence(FINANCE_BAR_TITLE,10).orElseThrow();
            String financeBarText = driverTools.getText(financeBar);
            if(!financeBarList.contains(financeBarText)){
                financeBarList.add(financeBarText);
            }
            logger.log("Finance bar text : " + financeBarText);
            if(financeBarList.size() == 3)
                break;
            i++;
        }
        logger.log("Finance bar list : " + financeBarList);
        //check Blibli Tiket Rewards, PayLater, BlibliPay
        Assertion.assertThat("Finance bar should contain Tiket Rewards", financeBarList.stream().anyMatch(e -> e.contains("Blibli Tiket Rewards")), is(true));
        Assertion.assertThat("Finance bar should contain PayLater", financeBarList.stream().anyMatch(e -> e.contains("PayLater")), is(true));
        Assertion.assertThat("Finance bar should contain BlibliPay", financeBarList.stream().anyMatch(e -> e.contains("BlibliPay")), is(true));
        return financeBarList.size() == 3;
    }

    @Override
    public boolean checkRunningTextFinanceBarForNonIdrCurrency(String currency, String language) {
        driverTools.waitForElementToPresence(CURRENT_CURRENCY, 20);

        WebElement financeBarToolTip = driverTools.waitForElementToPresence(FINANCE_BAR_TOOL_TIP).orElseThrow();
        String expectedMessage = getNonIdrFinanceBarExpectedMessage(currency, language);
        driverTools.waitForTextToBePresent(FINANCE_BAR_TOOL_TIP, expectedMessage);
        String financeBarToolTipText = driverTools.getText(financeBarToolTip);

        boolean isPayLaterHidden = !driverTools.isElementDisplayed(PAYLATER_DETAIL_TEXT);
        boolean isBlibliPayHidden = !driverTools.isElementDisplayed(BLIPAY_DETAIL_TEXT);

        logger.log("non-IDR finance bar - PayLater hidden: " + isPayLaterHidden + ", BlibliPay hidden: " + isBlibliPayHidden
                + ", running text: " + financeBarToolTipText + ", expected: " + expectedMessage);

        Assertion.assertThat("Finance bar running text should match " + language + " message for " + currency,
                financeBarToolTipText, is(expectedMessage));
        Assertion.assertThat("Finance bar should not show PayLater for non-IDR", isPayLaterHidden, is(true));
        Assertion.assertThat("Finance bar should not show BlibliPay for non-IDR", isBlibliPayHidden, is(true));

        return financeBarToolTipText.equals(expectedMessage) && isPayLaterHidden && isBlibliPayHidden;
    }

    @Override
    public void clickPayLaterOnFinanceBar() {
        driverTools.click(driverTools.element(PAYLATER_DETAIL_TEXT).orElseThrow());
    }

    private Locator loyaltyTierBadge(String tier) {
        return new Locator("LOYALTY_TIER_BADGE_" + tier.toUpperCase().replace(" ", "_"),
                AppiumBy.xpath("//*[contains(@resource-id,'tv_loyalty_badge') and contains(@text,'" + tier + "')]"));
    }

    @Override
    public boolean areIdrLoginDetailsShown(String tier) {
        boolean isLoyaltyTierShown = driverTools.waitForElementToAppear(loyaltyTierBadge(tier), 30).isPresent();
        boolean isPayLaterShown = driverTools.waitForElementToAppear(PAYLATER_DETAIL_TEXT, 20).isPresent();
        boolean isBlibliPayShown = driverTools.waitForElementToAppear(BLIPAY_DETAIL_TEXT, 20).isPresent();
        logger.log("IDR login details - tier: " + isLoyaltyTierShown + ", PayLater: " + isPayLaterShown + ", BlibliPay: " + isBlibliPayShown);
        return isLoyaltyTierShown && isPayLaterShown && isBlibliPayShown;
    }

    @Override
    public boolean checkLoginTextFinanceBar() {
        WebElement financeBarLoginText = driverTools.waitForElementToAppear(FINANCE_BAR_TITLE,15).orElseThrow();
        String financeBarLoginTextString = driverTools.getText(financeBarLoginText);
        logger.log("Finance bar login text : " + financeBarLoginTextString);
        Assertion.assertThat("Finance bar should contain Log In text", financeBarLoginTextString.toLowerCase(), containsString("log in"));
        return financeBarLoginTextString.toLowerCase().contains("log in");
    }

    private Locator getFinanceBarRunningTextLocator(String sectionTitle) {
        return new Locator("FINANCE_BAR_RUNNING_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'finance_bar')]//*[contains(@resource-id,'tv_title') and @text='" + sectionTitle + "']"));
    }

    private Locator getFinanceBarLocator(String input) {
        return getFinanceBarRunningTextLocator(input);
    }

    @Override
    public void clickFinanceBar() {
        driverTools.waitForElementToPresence(FINANCE_BAR_TITLE,20).orElseThrow();
        driverTools.clickElementByCoordinate(FINANCE_BAR);
        driverTools.waitForElementToDisappear(FINANCE_BAR_TITLE);
    }

    @Override
    public void clickFinanceBarRunningText(String sectionTitle) {
        Locator runningTextLocator = getFinanceBarRunningTextLocator(sectionTitle);
        driverTools.swipeToElement(FINANCE_BAR, Swipe.DOWN, 5).orElseThrow();
        for (int attempt = 0; attempt < 12; attempt++) {
            List<Boolean> foundList = new ArrayList<>();
            driverTools.waitForElementToPresence(runningTextLocator, 3).ifPresent(element -> {
                        element.click();
                        logger.log("Finance bar running text found: " + sectionTitle);
                        foundList.add(true);
                    });
                    if(!foundList.isEmpty()) return;
            logger.log("Finance bar running text not visible yet, swiping: " + sectionTitle);
            driverTools.swipeLeft(FINANCE_BAR);
            driverTools.dummyWait(1);
        }
        throw new NoSuchElementException("Finance bar running text not found: " + sectionTitle);
    }

    @Override
    public boolean isOnBlibliTiketRewardsPage() {
        if (driverTools.waitForElementToAppear(BLIBLI_TIKET_POINTS_PAGE_HEADER, 15).isPresent()) {
            return true;
        }
        return isOnRewardsPage();
    }

    @Override
    public boolean isOnPaylaterUspPage() {
        return driverTools.waitForElementToAppear(PAYLATER_PAGE_CAROUSEL, 15).isPresent()
                || driverTools.waitForElementToAppear(PAYLATER_USP_BANNER, 5).isPresent()
                || driverTools.waitForElementToAppear(PAYLATER_USP_LIMIT_SECTION, 5).isPresent()
                || driverTools.waitForElementToAppear(PAYLATER_USP_LIMIT_LABEL, 5).isPresent()
                || driverTools.waitForElementToAppear(PAYLATER_USP_CTA, 5).isPresent();
    }

    @Override
    public boolean isOnBlipayDashboard() {
        if (driverTools.waitForWebview()) {
            String url = driverTools.switchToWebViewUrl("blipay");
            driverTools.switchToNative();
            return url != null && url.contains("blipay");
        }
        return driverTools.isElementDisplayed(BLIBLIPAY_HEADER);
    }

    @Override
    public Boolean isFinanceBarDisabled() {
        driverTools.waitForElementToAppear(FINANCE_BAR, 40);
        return driverTools.isElementDisplayed(FINANCE_BAR_INFO);
    }

    @Override
    public Boolean isToolTipMessageAppears(String currency) {
        WebElement financeBarToolTip = driverTools.waitForElementToPresence(FINANCE_BAR_TOOL_TIP).orElseThrow();
        String financeBarToolTipText = driverTools.getText(financeBarToolTip);
        logger.log("Finance bar tooltip text : " + financeBarToolTipText);
        Assertion.assertThat("Finance bar tooltip should contain " + currency, financeBarToolTipText, containsString(currency));
        return financeBarToolTipText.contains(currency);
    }

    @Override
    public boolean isBannerCarouselV2ComponentValid(String pageModuleTitle, String sectionType) {
        switch (sectionType) {
            case "TITLE_HEADING":
                Locator bannerCarouselV2TitleLocator = getBannerCarouselV2TitleLocator(pageModuleTitle);
                driverTools.swipeToElement(bannerCarouselV2TitleLocator, Swipe.UP, 3);
                return driverTools.waitForElementToAppear(bannerCarouselV2TitleLocator).isPresent();
            case "SUBTITLE_HEADING":
                Locator bannerCarouselV2SubtitleLocator = getBannerCarouselV2SubtitleLocator(pageModuleTitle);
                driverTools.swipeToElement(bannerCarouselV2SubtitleLocator, Swipe.UP, 3);
                return driverTools.waitForElementToAppear(bannerCarouselV2SubtitleLocator).isPresent();
            case "CHIP_LIST":
                Locator bannerCarouselV2ChipListLocator = getBannerCarouselV2ChipListLocator(pageModuleTitle);
                Locator bannerCarouselV2ChipLocator = getBannerCarouselV2ChipLocator(pageModuleTitle);

                driverTools.swipeToFullyVisibleElement(bannerCarouselV2ChipListLocator, Swipe.UP, 3).orElseThrow();
                Boolean isBannerCarouselV2ChipAppeared = driverTools.waitForElementToAppear(bannerCarouselV2ChipListLocator).isPresent();
                Boolean isBannerCarouselV2ChipClickable = Boolean.parseBoolean(driverTools.getValueAttribute(bannerCarouselV2ChipLocator,"clickable"));
                Boolean isBannerCarouselV2ChipScrollable = driverTools.elements(bannerCarouselV2ChipListLocator).orElseThrow().size() < 5 || Boolean.parseBoolean(driverTools.getValueAttribute(bannerCarouselV2ChipListLocator, "scrollable"));

                logger.log("CHIP Appear : " + isBannerCarouselV2ChipAppeared + "\n" + "CHIP Clickable : " + isBannerCarouselV2ChipClickable + "\n" + "CHIP Scrollable : " + isBannerCarouselV2ChipScrollable);

                driverTools.swipeToElement(bannerCarouselV2ChipListLocator, Swipe.UP, 3);
                return isBannerCarouselV2ChipAppeared
                        && isBannerCarouselV2ChipClickable
                        && isBannerCarouselV2ChipScrollable;
            case "IMAGE_BANNER_LIST":
                Locator bannerCarouselV2BannerListLocator = getBannerCarouselV2BannerListLocator(pageModuleTitle);
                Locator bannerCarouselV2BannerLocator = getBannerCarouselV2BannerLocator(pageModuleTitle);

                Boolean isBannerCarouselV2BannerAppeared = driverTools.waitForElementToAppear(bannerCarouselV2BannerListLocator).isPresent();
                Boolean isBannerCarouselV2BannerClickable = Boolean.parseBoolean(driverTools.getValueAttribute(bannerCarouselV2BannerLocator,"clickable"));
                Boolean isBannerCarouselV2BannerScrollable = driverTools.elements(getBannerCarouselV2BannerLocator(pageModuleTitle)).orElseThrow().size() < 5 || Boolean.parseBoolean(driverTools.getValueAttribute(bannerCarouselV2BannerListLocator,"scrollable"));

                logger.log("IMAGE_BANNER Appear : " + isBannerCarouselV2BannerAppeared + "\n" + "IMAGE_BANNER Clickable : " + isBannerCarouselV2BannerClickable + "\n" + "IMAGE_BANNER Scrollable : " + isBannerCarouselV2BannerScrollable);

                driverTools.swipeToElement(bannerCarouselV2BannerListLocator, Swipe.UP, 3);
                return isBannerCarouselV2BannerAppeared
                        && isBannerCarouselV2BannerClickable
                        && isBannerCarouselV2BannerScrollable;
            case "SEE_ALL_LINK":
                Locator bannerCarouselV2SeeAllLinkLocator = getBannerCarouselV2SeeAllLinkLocator(pageModuleTitle);
                driverTools.swipeToElement(bannerCarouselV2SeeAllLinkLocator, Swipe.UP, 3);
                return driverTools.waitForElementToAppear(bannerCarouselV2SeeAllLinkLocator).isPresent()
                        && Boolean.parseBoolean(driverTools.getValueAttribute(bannerCarouselV2SeeAllLinkLocator,"clickable"));
            case "CTA_BUTTON_WITH_TEXT":
                Locator bannerCarouselV2CTAButtonLocator = getBannerCarouselV2CTAButtonLocator(pageModuleTitle);
                driverTools.swipeToElement(bannerCarouselV2CTAButtonLocator, Swipe.UP, 3);
                return driverTools.waitForElementToAppear(bannerCarouselV2CTAButtonLocator).isPresent()
                        && Boolean.parseBoolean(driverTools.getValueAttribute(bannerCarouselV2CTAButtonLocator,"clickable"));
            default:
                logger.warn("Unhandled section type '"+sectionType+"'");
        }

        return false;
    }

    @Override
    public void clickPageModuleChip(String pageModuleTitle, String byType, String input) {
        switch (byType) {
            case "index":
                int index = Integer.parseInt(input);
                List<WebElement> chipsElementList = driverTools.elements(getBannerCarouselV2ChipLocator(pageModuleTitle)).orElseThrow();
                driverTools.click(chipsElementList.get(index));
                break;
            case "name":
                throw new NotImplementedException();
        }
    }

    @Override
    public void clickBannerCarouselV2BannerByIndex(HomePageModule.PageModules targetPageModule, int index) {
        driverTools.click(getBannerCarouselV2BannerLocator(targetPageModule.moduleDetail.heading.title));
    }

    public Locator getFlashSaleSeeAllLocator(String pageModuleTitle) {
        return new Locator("FLASH_SALE_SEE_ALL", AppiumBy.xpath("//*[contains(@resource-id,'recycler_view') or contains(@resource-id,'recyclerView')]//*[(contains(@resource-id,'tv_title') or contains(@resource-id,'tv_subtitle')) and contains(@text,\"" + pageModuleTitle + "\")]/following-sibling::android.widget.FrameLayout//*[contains(@resource-id, 'tds_button_view')]"));
    }

    private Locator getBannerCarouselV2TitleLocator(String pageModuleTitle) {
        return new Locator("BANNER_CAROUSEL_V2_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'recycler_view')]//*[contains(@resource-id,'tv_title') and @text=\""+pageModuleTitle+"\"]"));
    }
    private Locator getBannerCarouselV2SubtitleLocator(String pageModuleTitle) {
        return new Locator("BANNER_CAROUSEL_V2_SUBTITLE", AppiumBy.xpath("//*[contains(@resource-id,'recycler_view')]//*[./*[contains(@resource-id,'tv_title') and @text=\""+pageModuleTitle+"\"]]/*[contains(@resource-id,'tv_subtitle')]"));
    }

    private Locator getBannerCarouselV2SeeAllLinkLocator(String pageModuleTitle) {
        return new Locator("BANNER_CAROUSEL_V2_SEE_ALL_LINK", AppiumBy.xpath("//*[contains(@resource-id,'recycler_view')]//*[./*[contains(@resource-id,'tv_title') and @text=\"" + pageModuleTitle + "\"]]//*[contains(@resource-id,\"tv_see_all_section\")]"));
    }

    private Locator getBannerCarouselV2ChipListLocator(String pageModuleTitle) {
        return new Locator("BANNER_CAROUSEL_V2_CHIP_LIST", AppiumBy.xpath("//*[contains(@resource-id,\"tv_title\") and @text=\"" + pageModuleTitle + "\"]/ancestor::android.view.ViewGroup/ancestor::android.view.ViewGroup//*[contains(@resource-id, 'chip_group')]"));
    }

    private Locator getBannerCarouselV2ChipLocator(String pageModuleTitle) {
        return new Locator("BANNER_CAROUSEL_V2_CHIP", AppiumBy.xpath("//*[contains(@resource-id,\"tv_title\") and @text=\"" + pageModuleTitle + "\"]/ancestor::android.view.ViewGroup/ancestor::android.view.ViewGroup//*[contains(@resource-id, 'tds_chips')]"));
    }

    private Locator getBannerCarouselV2BannerListLocator(String pageModuleTitle) {
        return new Locator("BANNER_CAROUSEL_V2_BANNER_LIST", AppiumBy.xpath("//*[contains(@resource-id,\"tv_title\") and @text=\"" + pageModuleTitle + "\"]/ancestor::android.view.ViewGroup/following-sibling::androidx.recyclerview.widget.RecyclerView[2]"));
    }

    private Locator getBannerCarouselV2BannerLocator(String pageModuleTitle) {
        return new Locator("BANNER_CAROUSEL_V2_BANNER", AppiumBy.xpath("//*[contains(@resource-id,\"tv_title\") and @text=\"" + pageModuleTitle + "\"]/ancestor::android.view.ViewGroup/following-sibling::androidx.recyclerview.widget.RecyclerView[2]//android.view.ViewGroup"));
    }

    private Locator getBannerCarouselV2CTAButtonLocator(String pageModuleTitle) {
        return new Locator("BANNER_CAROUSEL_V2_CTA_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,\"tv_title\") and @text=\"" + pageModuleTitle + "\"]/ancestor::android.view.ViewGroup/ancestor::android.view.ViewGroup//android.widget.Button"));
    }

    @Override
    public boolean verifySeasonalBanner(HomePageModule.PageModules pageModule) {
        boolean titleAppears = driverTools.isElementDisplayed(getArticleNameLocator(pageModule.dynamicContents.get(0).contentTitle));
        logger.log("Is Content Title Text Correct" + titleAppears);

        boolean contentDescriptionAppears = driverTools.isElementDisplayed(getSubtitleLocator(pageModule.dynamicContents.get(0).contentDescription));
        logger.log("Is content Description Correct" + contentDescriptionAppears);

        boolean buttonTextIsCorrect = driverTools.isElementDisplayed(getButtonLocator(pageModule.dynamicContents.get(0).buttonText));
        logger.log("Is Button Text Correct" + buttonTextIsCorrect);

        boolean pageIndicator = driverTools.isElementDisplayed(getArticleNameLocator(pageModule.dynamicContents.get(0).tabText));
        logger.log("Page Indicator Correct" + pageIndicator);

        return titleAppears && buttonTextIsCorrect && contentDescriptionAppears && pageIndicator;
    }

    @Override
    public boolean verifySeasonalBannerClick(HomePageModule.PageModules pageModules) {
        driverTools.click(getButtonLocator(pageModules.dynamicContents.get(0).buttonText));
        boolean redirected = driverTools.waitForElementToAppear(CONTINUE_BUTTON_FIRST_PAGE, 15).isPresent();
        logger.log("Seasonal banner redirected page marker (tds_button_view) visible: " + redirected);
        driverTools.back();
        return redirected;
    }

    @Override
    public boolean verifySeasonalBannerScrollable(HomePageModule.PageModules pageModules) {
        return Boolean.valueOf(driverTools.getValueAttribute(SEASONAL_BANNER, "Scrollable"));
    }

    @Override
    public boolean scrollUntilBottom(int numberOfClicks) {
        driverTools.swipeDown(2);
        driverTools.waitForElementToAppear(SLIDER_BUTTON);
        for(int i = 0; i < numberOfClicks; i++) {
            driverTools.click(SLIDER_BUTTON);
            boolean isAtBottom = driverTools.isElementDisplayed(BACK_TO_TOP_MARKER);
            if(isAtBottom){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean scrollBackToTop() {
        driverTools.clickElementByCoordinate(BACK_TO_TOP_MARKER);
        return driverTools.isElementDisplayed(HOME_PAGE_PROMO_CHIP);
    }

    @Override
    public boolean checkHighLightDiscoverModule(String title) {
        String sectionTitle = title.split(", ")[0];
        String sectionLabel = title.split(", ")[5];
        String sectionDescription = title.split(", ")[6];
        Locator getHighLightDiscoverModuleLocator = getHighLightDiscoverModuleLocator(sectionTitle);
        swipeToCard(sectionTitle);
        boolean moduleIcon = driverTools.isElementDisplayed(HIGHLIGHT_DISCOVER_PRODUCT_ICON);
        boolean moduleTitle = driverTools.isElementDisplayed(getHighLightDiscoverModuleLocator);
        boolean getHighLightDiscoverModuleLocatorLabel = driverTools.isElementDisplayed(getHighLightDiscoverModuleLabelLocator(sectionLabel));
        boolean getHighLightDiscoverModuleLocatorDescription =driverTools.isElementDisplayed(HighLightDiscoverModuleLocation(sectionDescription));
        return moduleIcon && moduleTitle && getHighLightDiscoverModuleLocatorLabel && getHighLightDiscoverModuleLocatorDescription ;
    }

    private Locator getHighLightDiscoverModuleLocator(String ModuleTitle) {
        return new Locator("HIGHLIGHT_DISCOVER_PRODUCT_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'"+ModuleTitle+"')]"));
    }
    private Locator getHighLightDiscoverModuleLabelLocator(String ModuleLabel) {
        return new Locator("HIGHLIGHT_DISCOVER_PRODUCT_LABEL", AppiumBy.xpath("//*[contains(@resource-id,'tv_label') and contains(@text,'"+ModuleLabel+"')]"));
    }
    private Locator getHighLightDiscoverModuleDescriptionLocator(String ModuleDescription) {
        return new Locator("HIGHLIGHT_DISCOVER_PRODUCT_DESCRIPTION", AppiumBy.xpath("//*[contains(@resource-id,'ModuleDescription') and contains(@text,'"+ModuleDescription+"')]"));
    }

    @Override
    public boolean checkHighLightDiscoverModuleProduct(String title) {
        String ProductTitle = title.split(", ")[1];
        driverTools.swipeToElement(getHighLightDiscoverModuleLocator(ProductTitle), Swipe.UP, 2);
        boolean ModuleProductTitle = driverTools.isElementDisplayed(getHighLightDiscoverModuleLocator(ProductTitle));
        return ModuleProductTitle;
    }

    @Override
    public boolean checkHighLightDiscoverModuleLocation(String Location) {
        String LocationText = Location.split(", ")[2];
        Locator getHighLightDiscoverModuleLocation = HighLightDiscoverModuleLocation(LocationText);
        return driverTools.isElementDisplayed(getHighLightDiscoverModuleLocation);
    }

    private Locator HighLightDiscoverModuleLocation(String ModuleTitle) {
        return new Locator("HIGHLIGHT_DISCOVER_PRODUCT_LOCATION", AppiumBy.xpath("//*[contains(@resource-id,'tv_subtitle') and contains(@text,'"+ModuleTitle+"')]"));
    }

    @Override
    public boolean checkHighLightDiscoverModulePrice(String price) {
        String priceText = price.split(", ")[4];
        Locator getHighLightDiscoverModulePrice = HighLightDiscoverModulePrice(priceText);
        return driverTools.isElementPresent(getHighLightDiscoverModulePrice);
    }
    @Override
    public boolean checkHighLightDiscoverModuleReview(String review) {
        String reviewText = review.split(", ")[7];
        Locator getHighLightDiscoverModuleLocation = HighLightDiscoverModuleLocation(reviewText);
        return driverTools.isElementDisplayed(getHighLightDiscoverModuleLocation);
    }
    @Override
    public boolean checkHighLightDiscoverModuleRating(String rating) {
        String ratingText = rating.split(", ")[8];
        Locator getHighLightDiscoverModuleRating = HighLightDiscoverModuleLocation(ratingText);
        if(!driverTools.waitForElementToDisappear(getHighLightDiscoverModuleRating)){
            driverTools.swipeToElement(getHighLightDiscoverModuleRating,Swipe.LEFT,2);
            return driverTools.isElementDisplayed(getHighLightDiscoverModuleRating);
        }
        return false;
    }

    @Override
    public boolean verifyDiscoverModuleProductRedirection(String Attribute) {
        String AttributeText = Attribute.split(", ")[1];
        String priceText = Attribute.split(", ")[4];

        //check entory card redirect
        driverTools.waitForElementToAppear(getHighLightDiscoverModuleLocator(AttributeText),30);
        driverTools.click(getHighLightDiscoverModuleLocator(AttributeText));
        boolean isRedirectCorrectCard = driverTools.waitForElementToAppear(PDP_TOOLBAR,10).isPresent();
        logger.log("when click on inventory card is redirect to PDP page: "+isRedirectCorrectCard);
        driverTools.click(PDP_TOOLBAR);

        //check image redirect
        driverTools.waitForElementToAppear(HIGHLIGHT_DISCOVER_IMAGE,30).ifPresent(driverTools::click);
        boolean isRedirectCorrectImg = driverTools.waitForElementToDisappear(HIGHLIGHT_DISCOVER_IMAGE,30);
        driverTools.click(PDP_TOOLBAR);

        driverTools.waitForElementToAppear(getHighLightDiscoverModuleLocator(AttributeText),30);
        driverTools.swipeLeft(getHighLightDiscoverModuleLocator(AttributeText),4);
        boolean isScrollWorking= driverTools.waitForElementToDisappear(getHighLightDiscoverModuleLocator(priceText));
        return isRedirectCorrectImg && isRedirectCorrectCard && isScrollWorking ;
    }

    private Locator HighLightDiscoverModulePrice(String priceText ) {
        return new Locator("HIGHLIGHT_DISCOVER_PRODUCT_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'tv_final_price') and contains(@text,'"+priceText+"')]"));
    }

    @Override
    public boolean verifyUnreadCount(Map<String, Integer> unreadCount) {
        int chatUnreadCount = unreadCount.get("chatTotalUnreadCount");
        int inboxTotalUnreadCount = unreadCount.get("inboxTotalUnreadCount");
        //In homepage if unread count greater than 99 it will show 99+
        int total = chatUnreadCount + inboxTotalUnreadCount;
        String expectedUnreadCount = total > 99 ? "99+" : String.valueOf(total);
        driverTools.waitForElementToAppear(UNREAD_COUNT,10);
        String actualUnreadCount = driverTools.getText(UNREAD_COUNT).orElseThrow();
        Assertion.assertThat("Verify unread count match", actualUnreadCount, equalTo(expectedUnreadCount));
        return true;
    }

    @Override
    public boolean verifySwipeHighlightDiscoverArticle(HighlightDiscoverPOJO highlightArticleSection) {
        final Locator FAB = new Locator("FAB", AppiumBy.id("iv_action"));
        String sectionTitle = highlightArticleSection.dynamicContentTitle;
        //swipe to section
        driverTools.swipeByDistanceToElement(HIGHLIGHT_ARTICLE_CONTAINER,SCREEN,Swipe.UP,0.075,10);
        driverTools.swipeByDistance(SCREEN,Swipe.UP,0.2);

        // Close FAB if appearing
        driverTools.element(FAB).ifPresent(driverTools::click);

        //swipe left
        Locator sectionTitleLocator = getCardLocator(sectionTitle);
        String bounds1 = driverTools.getValueAttribute(sectionTitleLocator, "bounds");
        logger.log("bounds before swipe: "+bounds1);
        driverTools.swipeByDistance(HIGHLIGHT_ARTICLE_CONTAINER,Swipe.LEFT,0.7);
        String bounds2 = driverTools.getValueAttribute(sectionTitleLocator, "bounds");
        logger.log("bounds after swipe: "+bounds2);
        //verify disappear
        boolean isCardGoneAfterSwipe = !bounds1.equals(bounds2);
        logger.log("Is card moved out of view after swipe: "+isCardGoneAfterSwipe);
        //swipe right
        driverTools.swipeByDistance(HIGHLIGHT_ARTICLE_CONTAINER,Swipe.RIGHT,0.7);
        String bounds3 = driverTools.getValueAttribute(sectionTitleLocator, "bounds");
        logger.log("bounds after swipe back: "+bounds3);
        //verify appear
        boolean isCardAppearAfterSwipe = bounds1.equals(bounds3);
        logger.log("Is card moved to view after swipe: "+isCardAppearAfterSwipe);

        return isCardGoneAfterSwipe && isCardAppearAfterSwipe;
    }

    @SneakyThrows
    @Override
    public boolean verifyClickHighlightDiscoverArticle(HighlightDiscoverPOJO highlightArticleSection) {

        String sectionTitle = highlightArticleSection.dynamicContentTitle;
        String sectionSubtitle = highlightArticleSection.dynamicContentSubtitle;
        String expectedUrl = highlightArticleSection.dynamicContentUrlRedirect;
        String buttonText = highlightArticleSection.dynamicContentButtonText;

        logger.log("HighllightDiscoverPojo: " + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(highlightArticleSection));

        Locator highlightArticleButton = new Locator("Highlight Article Text", AppiumBy.xpath("//*[@text='" + sectionTitle + "']//following-sibling::*[@text='" + sectionSubtitle + "']//following-sibling::*//*[@text='" + buttonText + "']"));
        boolean isLocatorUsingTextFound = driverTools.isElementDisplayed(highlightArticleButton);
        logger.log("is 3 text correct: " + isLocatorUsingTextFound);
        logger.log("Checking redirect button");
        driverTools.click(highlightArticleButton);

        if(expectedUrl.contains("/hotel?utm_logic=&utm_section=pageModule")) {
            logger.log("Hotel landing page redirect detected, checking room and guest text");
            return !LandingPage.getInstance(session).getRoomAndGuestText().isEmpty();
        }

        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl(expectedUrl);

        boolean isContentDisplayedButton = driverTools.isElementDisplayed(WEBVIEW_POST);
        logger.log("is webview content displayed: " + isContentDisplayedButton);
        String webviewUrl = driverTools.getCurrentURL();
        logger.log("expected url: " + expectedUrl);
        logger.log("got url: " + webviewUrl);
        String url = expectedUrl.replaceAll("/", "");
        webviewUrl = webviewUrl.replaceAll("/", "");
        boolean isUrlSameButton = webviewUrl.contains(url) || url.contains(webviewUrl);
        logger.log("is url correct button: " + isUrlSameButton);
        driverTools.switchToNative();

        driverTools.click(BACK_BUTTON_WEBVIEW);

        logger.log("Checking redirect image");

        driverTools.click(HIGHLIGHT_ARTICLE_IMAGE);
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl(expectedUrl);

        boolean isContentDisplayedImage = driverTools.isElementDisplayed(WEBVIEW_POST);
        logger.log("is webview content displayed: " + isContentDisplayedImage);

        webviewUrl = driverTools.getCurrentURL();
        logger.log("expected url: " + expectedUrl);
        logger.log("got url: " + webviewUrl);
        url = expectedUrl.replaceAll("/", "");
        webviewUrl = webviewUrl.replaceAll("/", "");
        boolean isUrlSameImage = webviewUrl.contains(url) || url.contains(webviewUrl);
        logger.log("is url correct image: " + isUrlSameImage);
        driverTools.switchToNative();

        return isContentDisplayedButton && isUrlSameButton && isContentDisplayedImage && isUrlSameImage;
    }

    @Override
    public boolean verifyFinanceBarRedirect(boolean isOnProd) {
        driverTools.swipeToElement(TIKET_POINTS_CONTAINER,Swipe.DOWN,30).orElseThrow();
        boolean isInBlibliTiketRewardsPage = true;
        if(isOnProd){
            driverTools.click(BLIBLI_TIKET_POINTS_FINANCE_BAR);
            driverTools.waitForWebview();
            driverTools.switchToWebView();
            isInBlibliTiketRewardsPage = driverTools.waitForElementToAppear(BLIBLI_TIKET_POINTS_PAGE_HEADER).isPresent();
            logger.log("is redirect to blibli tiket points page: "+isInBlibliTiketRewardsPage);
            driverTools.click(BACK_BLIBLI_TIKET_POINTS);
            driverTools.switchToNative();
        }
        driverTools.waitForElementToAppear(PAYLATER_FINANCE_BAR,5);
        //check paylater
        driverTools.click(PAYLATER_FINANCE_BAR);
        boolean isInPaylaterPage = driverTools.waitForElementToAppear(PAYLATER_PAGE_CAROUSEL).isPresent();
        logger.log("is redirect to paylater page: "+isInPaylaterPage);
        driverTools.click(BACK_PAYLATER);

        //check bliblipay
        driverTools.waitForElementToAppear(BLIBLIPAY_FINANCE_BAR,5);
        driverTools.click(BLIBLIPAY_FINANCE_BAR);
        driverTools.waitForWebview();
        String url = driverTools.switchToWebViewUrl("blipay");
        boolean isInBlibliPayPage = url.contains("blipay");
        logger.log("is redirect to bliblipay page: "+isInBlibliPayPage);
        driverTools.switchToNative();

        return isInBlibliTiketRewardsPage && isInPaylaterPage && isInBlibliPayPage;
    }

    @Override
    public boolean verifyHighlightDiscoverArticleTitle(HighlightDiscoverPOJO highlightArticleSection) {
        String moduleLabel = highlightArticleSection.moduleLabel;
        String moduleTitle = highlightArticleSection.moduleTitle;
        String moduleSubtitle = highlightArticleSection.moduleSubtitle;

        boolean isLabelShown = driverTools.isElementDisplayed(getTextLocatorHighlightDiscover(moduleLabel));
        logger.log("is label shown: "+isLabelShown);

        boolean isTitleShown = driverTools.isElementDisplayed(getTextLocatorHighlightDiscover(moduleTitle));
        logger.log("is title shown: "+isTitleShown);

        boolean isIconShown = driverTools.isElementDisplayed(ICON_HIGHLIGHT);
        logger.log("is icon shown: "+isIconShown);

        boolean isSubtitleShown =true;
        if(!moduleSubtitle.equals("")){
            isSubtitleShown = driverTools.isElementDisplayed(getTextLocatorHighlightDiscover(moduleSubtitle));
            logger.log("is subtitle shown: "+isSubtitleShown);
        }

        return isLabelShown && isTitleShown && isIconShown && isSubtitleShown;
    }

    private Locator getTextLocatorHighlightDiscover(String text){
        return new Locator(text,AppiumBy.xpath("//*[contains(@resource-id,'container_title_and_label')]//following::*[@text='"+text+"']"));
    }

    public boolean verifyFinanceBarText(FinanceBarPOJO data) {
        String tiketPoints = data.tiketPoints;
        String paylater = data.payLater;
        String blipay = data.blibliPay;

        boolean isTiketPointsCorrect = driverTools.waitForElementToPresence(new Locator(tiketPoints,AppiumBy.xpath("//*[contains(@resource-id,'tiket_point')]//*[@text='"+tiketPoints+"']"))).isPresent();
        logger.log("Is correct tiket points shown: "+ isTiketPointsCorrect);
        boolean isPaylaterCorrect = driverTools.waitForElementToPresence(new Locator(paylater,AppiumBy.xpath("//*[contains(@resource-id,'paylater')]//*[@text='"+paylater+"']"))).isPresent();
        logger.log("Is correct paylater shown: "+ isPaylaterCorrect);
        boolean isBlipayCorrect = driverTools.waitForElementToPresence(new Locator(blipay,AppiumBy.xpath("//*[contains(@resource-id,'blipay')]//*[@text='"+blipay+"']"))).isPresent();
        logger.log("Is correct blipay shown: "+ isBlipayCorrect);

        return isTiketPointsCorrect && isPaylaterCorrect && isBlipayCorrect;
    }

    private Locator getTitleLocator(String title){
        return new Locator("TITLE_LOCATOR_OF_" + title,AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'"+title+"') or contains(@text,'Car Rental'))]"));
    }

    private Locator getContinuePaymentPriceLocator(){
        return new Locator("CONTINUE_PAYMENT_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'view_countdown')]/ancestor::*[.//*[contains(@resource-id,'tv_price')]][1]//*[contains(@resource-id,'tv_price')]"));
    }

    @Override
    public void verifyContinueToPaymentSection(String name, String totalPrice){
        swipeToCard(name);
        boolean isCountDownDisplayed = driverTools.isElementDisplayed(COUNTDOWN_CONTINUE_TO_PAYMENT);
        Assertion.assertThat("is countdown displayed ",isCountDownDisplayed,is(true));
        boolean isInventoryNameDisplayed = driverTools.isElementDisplayed(getTitleLocator(name));
        Assertion.assertThat("is inventory name displayed ",isInventoryNameDisplayed,is(true));
        WebElement priceElement = driverTools.element(getContinuePaymentPriceLocator()).orElseThrow();
        String price = driverTools.getText(priceElement);
        Assertion.assertThat("is total price of inventory displayed ", price.replaceAll("[^0-9]", ""), containsString(totalPrice.replaceAll("[^0-9]", "")));
        boolean isContinueToPaymentBtnDisplayed = driverTools.isElementDisplayed(CONTINUE_TO_PAYMENT_BTN);
        Assertion.assertThat("is continue to payment btn displayed ",isContinueToPaymentBtnDisplayed,is(true));
    }

    @Override
    public void clickContinueToPayment(){
        driverTools.click(CONTINUE_TO_PAYMENT_BTN);
    }

    private Locator getTitleOfGVClaimLocator(String text){
        String[] textArr = text.split("'");
        return new Locator("TITLE_GV_CLAIM", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + textArr[0].toLowerCase() + "') and contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + textArr[textArr.length - 1].toLowerCase() + "')]"));
    }

    private Locator getIconGVClaimVoucherLocator(String voucherTitle){
        return new Locator("ICON_GV_CLAIM_VOUCHER",AppiumBy.xpath("//*[contains(@text,'"+voucherTitle+"')]//preceding-sibling::*[contains(@resource-id,'img_voucher')]"));
    }

    private Locator getTitleGVClaimVoucherLocator(String voucherTitle){
        return new Locator("TITLE_GV_CLAIM_VOUCHER",AppiumBy.xpath("//*[contains(@resource-id,'tv_title')and contains(@text,'"+voucherTitle+"')]"));
    }

    private Locator getCTAGVClaimBtnLocator(String ctnBtnText){
        return new Locator("CTA_GV_CLAIM_BTN",AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and contains(@text,'"+ctnBtnText+"')]"));
    }

    @Override
    public void swipeToCtaBtn(String btnName) {
        driverTools.swipeByDistanceToElement(TIKET_POINTS_CONTAINER,SCREEN,Swipe.DOWN,0.6,20).orElseThrow();
        final Locator CARD_LOCATOR = this.getCTAGVClaimBtnLocator(btnName);
        driverTools.swipeByDistanceToElement(CARD_LOCATOR,SCREEN,Swipe.UP,0.4,50).orElseThrow();
        driverTools.swipeByDistance(SCREEN,Swipe.UP,0.05);
    }

    @Override
    public Boolean isUpcomingBookingCountdownDisplayed() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_COUNTDOWN,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.isElementDisplayed(UPCOMING_BOOKING_COUNTDOWN);
    }

    @Override
    public Boolean isFlightUpcomingBookingDisplayed() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_FLIGHT,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.isElementDisplayed(UPCOMING_BOOKING_FLIGHT);
    }

    @Override
    public Boolean isUpcomingBookingHotelNameDisplayed() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_STAY,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.isElementDisplayed(UPCOMING_BOOKING_STAY);
    }

    @Override
    public Boolean checkInDetails() {
        driverTools.swipeToElement(UPCOMING_BOOKING_HOTEL_DETAILS,Swipe.UP,1);
        String checkInDetails = driverTools.getText(UPCOMING_BOOKING_HOTEL_DETAILS).orElseThrow();
        String[] parts = checkInDetails.split(",");
        String date = parts[1].trim();
        String time = parts[2].trim().split(" ")[0];
        String nights = parts[2].replaceAll("[^0-9]", "");
        logger.log("Check-in Date: " + date);
        Assertion.assertThat("Check-in Date should not be empty", !date.isEmpty(), is(true));
        logger.log("Check-in Time: " + time);
        Assertion.assertThat("Check-in Time should not be empty", !time.isEmpty(), is(true));
        logger.log("Number of Nights: " + nights);
        Assertion.assertThat("Number of Nights should not be empty", !nights.isEmpty(), is(true));
        return !date.isEmpty() && !time.isEmpty() && !nights.isEmpty();
    }

    @Override
    public void clickSeeAllUpcomingBooking(){
        driverTools.swipeToElement(SEE_ALL_UPCOMING_BOOKING,Swipe.UP,1);
        driverTools.click(SEE_ALL_UPCOMING_BOOKING);
    }

    @Override
    public Boolean countOfSeeAll(){
        driverTools.swipeToElement(SEE_ALL_UPCOMING_BOOKING,Swipe.UP,1);
        String seeAllText = driverTools.getText(SEE_ALL_UPCOMING_BOOKING).orElseThrow();
        String countText = seeAllText.replaceAll("[^0-9]", "");
        int count = Integer.parseInt(countText);
        logger.log("Count of See All Upcoming Booking: " + count);
        Assertion.assertThat("Count of See All Upcoming Booking should be more than 1", count , greaterThan(1));
        return count > 1;
    }

    @Override
    public Boolean isUpcomingCardClickable(){
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_STAY,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.isElementDisplayed(UPCOMING_BOOKING_STAY) && driverTools.waitForElementToBeClickable(UPCOMING_BOOKING_CARD).isPresent();
    }

    @Override
    public Boolean isTitleCrossSellAppears(){
        driverTools.swipeToElement(TITLE_CROSS_SELL,Swipe.UP,1);
        return driverTools.isElementDisplayed(TITLE_CROSS_SELL);
    }

    @Override
    public Boolean isCrossSellCardsClickableAndScrollable(){
        driverTools.swipeToElement(CROSS_SELL_CARDS,Swipe.UP,1);
        boolean isCardClickable = driverTools.isElementDisplayed(CROSS_SELL_CARDS) && driverTools.waitForElementToBeClickable(CROSS_SELL_CARDS).isPresent();
        logger.log("Is Cross Sell Card Clickable: "+isCardClickable);
        Assertion.assertThat("Cross Sell Card is clickable",isCardClickable,is(true));
        int initialX = driverTools.element(CROSS_SELL_CARDS).orElseThrow().getLocation().x;
        driverTools.swipeLeft(CROSS_SELL_CARDS,2);
        int swipedX = driverTools.element(CROSS_SELL_CARDS).orElseThrow().getLocation().x;
        boolean isCardSwiped = initialX != swipedX;
        logger.log("Is Cross Sell Card Swiped: "+isCardSwiped);
        Assertion.assertThat("Cross Sell Card is swiped",isCardSwiped,is(true));
        return isCardClickable && isCardSwiped;
    }

    @Override
    public void clickUpcomingBooking(){
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_COUNTDOWN,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        driverTools.clickElementByCoordinate(UPCOMING_BOOKING_COUNTDOWN);
    }

    @Override
    public void clickCrossSellCard(){
        driverTools.waitForElementToPresence(TRANSPORT_CROSS_SELL_CARD,5).ifPresent(driverTools::click);
        driverTools.swipeToElement(FLIGHT_CARD_CROSS_SELL,Swipe.UP,3);
        driverTools.click(FLIGHT_CARD_CROSS_SELL);
    }

    @Override
    public String getUpcomingBookingDepartureTime() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_DEPARTURE_TIME,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.getText(UPCOMING_BOOKING_DEPARTURE_TIME).orElseThrow();
    }

    @Override
    public String getUpcomingBookingRouteInformation() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_ROUTE_INFO,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.getText(UPCOMING_BOOKING_ROUTE_INFO).orElseThrow();
    }

    @Override
    public Boolean isUpcomingBookingVendorIconDisplayed() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_VENDOR_ICON,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.isElementDisplayed(UPCOMING_BOOKING_VENDOR_ICON);
    }

    @Override
    public Boolean isUpcomingBookingSeeMoreDisplayed() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_SEE_MORE_INFO,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.isElementDisplayed(UPCOMING_BOOKING_SEE_MORE_INFO);
    }

    @Override
    public Boolean isUpcomingBookingCloseButtonDisplayed() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_CLOSE_BUTTON,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.isElementDisplayed(UPCOMING_BOOKING_CLOSE_BUTTON);
    }

    @Override
    public Boolean isUpcomingBookingCrossSellTitleDisplayed() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_CROSS_SELL_TITLE,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.isElementDisplayed(UPCOMING_BOOKING_CROSS_SELL_TITLE);
    }

    @Override
    public Boolean isUpcomingBookingCrossSellCardDisplayed() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_CROSS_SELL_CARD,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.isElementDisplayed(UPCOMING_BOOKING_CROSS_SELL_CARD);
    }

    @Override
    public Boolean verifyUpcomingBookingSeeMore() {
        driverTools.clickElementByCoordinate(UPCOMING_BOOKING_SEE_MORE_INFO);
        driverTools.waitForElementToAppear(UPCOMING_BOOKING_PASSENGER_NAME);
        return driverTools.isElementDisplayed(UPCOMING_BOOKING_PASSENGER_NAME);
    }

    @Override
    public String getUpcomingBookingPassengerName() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_PASSENGER_NAME,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.getText(UPCOMING_BOOKING_PASSENGER_NAME).orElseThrow();
    }

    @Override
    public String getUpcomingBookingClassName() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_CLASS_NAME,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.getText(UPCOMING_BOOKING_CLASS_NAME).orElseThrow();
    }

    @Override
    public String getUpcomingBookingOriginStation() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_ORIGIN_STATION,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.getText(UPCOMING_BOOKING_ORIGIN_STATION).orElseThrow();
    }

    @Override
    public Boolean verifySeeMoreUpcomingBooking() {
        driverTools.clickElementByCoordinate(SEE_MORE_UPCOMING_BOOKING_CARD);

        boolean bottomsheetDisplayed = driverTools.waitForElementToAppear(UPCOMING_BOOKING_CARD_LIST_BOTTOMSHEET).isPresent();
        boolean bottomsheetCrossButtonDisplayed = driverTools.waitForElementToAppear(UPCOMING_BOOKING_CARD_LIST_BOTTOMSHEET_CLOSE_BUTTON).isPresent();;

        driverTools.clickElementByCoordinate(UPCOMING_BOOKING_CARD_LIST_BOTTOMSHEET_CLOSE_BUTTON);

        return bottomsheetDisplayed && bottomsheetCrossButtonDisplayed;
    }

    @Override
    public Boolean verifyUpcomingBookingCardXButton() {
        driverTools.clickElementByCoordinate(UPCOMING_BOOKING_CLOSE_BUTTON);

        boolean bottomsheetDisplayed = driverTools.waitForElementToAppear(UPCOMING_BOOKING_HIDE_CARD_BOTTOMSHEET).isPresent();
        boolean bottomsheetCancelButtonDisplayed = driverTools.waitForElementToAppear(UPCOMING_BOOKING_HIDE_CARD_BOTTOMSHEET_CANCEL_BUTTON).isPresent();;

        driverTools.clickElementByCoordinate(UPCOMING_BOOKING_HIDE_CARD_BOTTOMSHEET_CANCEL_BUTTON);

        return bottomsheetDisplayed && bottomsheetCancelButtonDisplayed;
    }

    @Override
    public Boolean verifyUpcomingBookingCard() {
        driverTools.clickElementByCoordinate(UPCOMING_BOOKING_CARD);

        OrderDetailPage<T> orderDetailPage = OrderDetailPage.getInstance(session);

        try {
            orderDetailPage.getOrderID();
        } catch (NoSuchElementException e) {
            return false;
        }

        orderDetailPage.clickBack();

        return true;
    }

    @Override
    public Boolean verifyUpcomingBookingVoucherBox() {
        driverTools.swipeUp();
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_VOUCHER_BOX,SCREEN,Swipe.UP,0.3,3).orElseThrow();
        driverTools.clickElementByCoordinate(UPCOMING_BOOKING_VOUCHER_BOX);

        VoucherBoxPage<T> voucherBoxPage = VoucherBoxPage.getInstance(session);
        boolean result = voucherBoxPage.isVoucherPageOpen();
        voucherBoxPage.clickBackBtnVoucherBoxPage();

        return result;
    }

    @Override
    public Boolean verifyUpcomingBookingAllPromoCard() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_ALL_PROMO,SCREEN,Swipe.UP,0.3,3).orElseThrow();
        driverTools.clickElementByCoordinate(UPCOMING_BOOKING_ALL_PROMO);

        PromoPage<T> promoPage = PromoPage.getInstance(session);
        boolean result = promoPage.isPromoListPageValid();
        promoPage.clickBack();

        return result;
    }

    @Override
    public Boolean verifyUpcomingBookingVerticalCard() {
        driverTools.waitForElementToAppear(CROSS_SELL_CAROUSEL);
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_VERTICAL, CROSS_SELL_CAROUSEL,Swipe.LEFT,0.4,3).orElseThrow();
        String verticalName = driverTools.getText(UPCOMING_BOOKING_VERTICAL_TEXT).orElseThrow();
        driverTools.clickElementByCoordinate(UPCOMING_BOOKING_VERTICAL);

        return super.verifyVertical(verticalName);
    }

    @Override
    public void verifyClaimGVSection(HomePageModule.PageModules pageModules){
        boolean isTitleGVClaimDisplayed = driverTools.isElementDisplayed(getTitleOfGVClaimLocator(pageModules.moduleDetail.heading.title));
        Assertion.assertThat("is title GV Claim Displayed",isTitleGVClaimDisplayed,is(true));

        boolean isGVClaimVoucherIconDisplayed = driverTools.isElementDisplayed(getIconGVClaimVoucherLocator(pageModules.dynamicContents.get(0).title));
        Assertion.assertThat("is voucher Icon GV claim displayed",isGVClaimVoucherIconDisplayed,is(true));

        boolean isGVClaimVoucherTitleDisplayed = driverTools.isElementDisplayed(getTitleGVClaimVoucherLocator(pageModules.dynamicContents.get(0).title));
        Assertion.assertThat("is voucher title GV claim displayed",isGVClaimVoucherTitleDisplayed,is(true));

        boolean isGVClaimVoucherScrollable = checkScrollableGVClaim(pageModules.dynamicContents,pageModules.moduleDetail.cta.title);
        Assertion.assertThat("is voucher GV claim scrollable ",isGVClaimVoucherScrollable,is(true));

        boolean isCtaBtnAndTextDisplayed = driverTools.isElementDisplayed(getCTAGVClaimBtnLocator(pageModules.moduleDetail.cta.title));
        Assertion.assertThat("is GV claim CTA Btn and Text displayed ",isCtaBtnAndTextDisplayed,is(true));
    }

    private boolean checkScrollableGVClaim(ArrayList<HomePageModule.DynamicContent> dynamicContents,String ctaBtn){
        WebElement scrollableToLast = driverTools.swipeToElement(getTitleGVClaimVoucherLocator(dynamicContents.get(dynamicContents.size()-1).title),GV_CLAIM_CARD_PARENT,Swipe.LEFT,20).orElseThrow();
        return driverTools.isElementDisplayed(scrollableToLast);
    }

    @Override
    public void clickGVVoucher(ArrayList<HomePageModule.DynamicContent> dynamicContents){
        driverTools.click(getTitleGVClaimVoucherLocator(dynamicContents.get(dynamicContents.size()-1).title));
    }

    @Override
    public void clickGVCtaBtn(String btnTitle){
        WebElement ctaBtnElement = driverTools.waitForElementToAppear(getCTAGVClaimBtnLocator(btnTitle)).orElseThrow();
        driverTools.click(ctaBtnElement);
    }

    @Override
    public void refreshHomePage(int numberOfRefresh) {
        logger.log("refreshing homepage");
        for(int i=0;i<numberOfRefresh ; i ++){
            driverTools.swipeDown();
            waitForHomePageToLoad();
        }
    }

    @Override
    public String swipeToRecentlySearchedNearMe(String date){
        driverTools.swipeDown(3);

        String[] dateSplit = date.split("-");
        String startDate = dateSplit[0].trim();
        startDate = startDate.split(" ")[0];
        String endDate = dateSplit[1].trim();
        endDate = endDate.split(" ")[0];

        if(startDate.split("")[0].equals("0")){
            startDate = startDate.substring(1);
        }
        if(endDate.split("")[0].equals("0")){
            endDate = endDate.substring(1);
        }

        Locator recentlySearchedPageModule = getRecentlySearchedLocatorNearMe(startDate,endDate);
        driverTools.swipeToElement(recentlySearchedPageModule,Swipe.UP,10);
        WebElement recentlySearchedTextElement = driverTools.waitForElementToPresence(recentlySearchedPageModule).orElseThrow();
        return driverTools.getText(recentlySearchedTextElement);
    }

    private Locator getRecentlySearchedLocatorNearMe(String startDate, String endDate){
        return new Locator("NEAR_ME",AppiumBy.xpath("//*[@class='android.widget.TextView' and contains(@text,'"+startDate+"') and contains(@text,'"+endDate+"')]//parent::*[contains(@resource-id,'view_attribute')]//preceding-sibling::*[contains(@resource-id,'view_title')]//*[@class='android.widget.TextView']"));
    }

    @Override
    public void openWishlist(){
        if (driverTools.isElementPresent(WISHLIST_ICON)) {
            driverTools.click(WISHLIST_ICON);
            boolean isInWishlist = driverTools.waitForElementToPresence(WISHLIST_PAGE).isPresent();
            if(!isInWishlist){
                driverTools.click(WISHLIST_ICON);
            }
        } else {
            AccountPage<T> accountPage = AccountPage.getInstance(session);

            openAccount();
            accountPage.openWishlistPage();
        }
    }

    @Override
    public Boolean isPayLaterSectionAppear(){
        driverTools.waitForElementToAppear(PAYLATER_FINANCE_BAR,5);
        return driverTools.waitForElementToPresence(PAYLATER_FINANCE_BAR).isPresent();
    }

    @Override
    public Boolean isBlipaySectionAppear(){
        driverTools.swipeToElement(BLIBLIPAY_FINANCE_BAR,FINANCE_BAR,Swipe.LEFT,5);
        return driverTools.waitForElementToPresence(BLIBLIPAY_FINANCE_BAR,40).isPresent();
    }

    @Override
    public Boolean verifyTiketPointRedirection(){
        boolean isInBlibliTiketRewardsPage = false;
        for(int i = 0; i < 3 && !isInBlibliTiketRewardsPage; i++){
            driverTools.waitForElementToAppear(BLIBLI_TIKET_POINTS_FINANCE_BAR, 30).ifPresent(driverTools::click);
            isInBlibliTiketRewardsPage = driverTools.waitForElementToAppear(BLIBLI_TIKET_POINTS_PAGE_HEADER, 30).isPresent();
        }
        clickHomeIcon();
        return isInBlibliTiketRewardsPage;
    }

    @Override
    public Boolean verifyPayLaterRedirection(){
        driverTools.waitForElementToAppear(PAYLATER_FINANCE_BAR,5);
        driverTools.click(PAYLATER_FINANCE_BAR);
        boolean isInPaylaterPage = driverTools.waitForElementToAppear(PAYLATER_PAGE_CAROUSEL).isPresent();
        driverTools.click(BACK_PAYLATER);
        return isInPaylaterPage;
    }

    @Override
    public Boolean verifyBlipayRedirection(){
        driverTools.swipeToElement(TIKET_POINTS_CONTAINER,Swipe.LEFT,2);
        driverTools.waitForElementToAppear(BLIBLIPAY_FINANCE_BAR,5);
        driverTools.click(BLIBLIPAY_FINANCE_BAR);
        driverTools.waitForWebview();
        String url = driverTools.switchToWebViewUrl("blipay");
        boolean isInBlibliPayPage = url.contains("blipay");
        driverTools.switchToNative();
        return isInBlibliPayPage;
    }

    /**
     * Dynamic widget info content lands on a native bottom sheet, a pushed detail screen, or a
     * WebView campaign page whose artwork is tagged main-hero-banner / root-image.
     */
    private static final Locator DYNAMIC_WIDGET_INFO_DETAIL = new Locator("DYNAMIC_WIDGET_INFO_DETAIL", AppiumBy.xpath(
            "//*[contains(@resource-id,'crf_bottom_sheet') or contains(@resource-id,'design_bottom_sheet')"
                    + " or contains(@resource-id,'bottomSheet_dynamic_content_view')"
                    + " or contains(@resource-id,'tds_header_navigation_appbar') or contains(@resource-id,'toolbar')]"
                    + " | //android.webkit.WebView//android.widget.Image"
                    + "[@content-desc='main-hero-banner' or @content-desc='root-image']"));

    /**
     * Heading node of the dynamic widget. Matched on text alone - the heading carries no
     * tv_title resource-id, so {@link #getCardLocator(String)} cannot see it. The subtitle repeats
     * the heading text, so the first match in document order is taken.
     * <p>
     * {@code sectionTitle} accepts the same "English/Bahasa" form as the other home locators.
     */
    private String dynamicWidgetTitleNode(String sectionTitle) {
        String titleText;
        if (sectionTitle.contains("/") && !sectionTitle.contains("//")) {
            String[] languages = splitLanguageData(sectionTitle, "/");
            titleText = "(contains(@text,\"" + languages[0] + "\") or contains(@text,\"" + languages[1] + "\"))";
        } else {
            titleText = "contains(@text,\"" + sectionTitle + "\")";
        }
        return "(//*[" + titleText + "])[1]";
    }

    private Locator dynamicWidgetTitle(String sectionTitle) {
        return new Locator("DYNAMIC_WIDGET_TITLE", AppiumBy.xpath(dynamicWidgetTitleNode(sectionTitle)));
    }

    /** The subtitle is the next text bearing node after the heading, with no resource-id of its own. */
    private Locator dynamicWidgetSubtitle(String sectionTitle) {
        return new Locator("DYNAMIC_WIDGET_SUBTITLE", AppiumBy.xpath("(" + dynamicWidgetTitleNode(sectionTitle)
                + "//following::*[string-length(@text)>0])[1]"));
    }

    /**
     * The widget body is a WebView, so its nodes expose content-desc values from the web page
     * instead of native resource-ids. The artwork is the first image that is neither the progress
     * bar nor the countdown container.
     */
    private Locator dynamicWidgetImage(String sectionTitle) {
        return new Locator("DYNAMIC_WIDGET_IMAGE", AppiumBy.xpath("(" + dynamicWidgetTitleNode(sectionTitle)
                + "//following::android.webkit.WebView[1]//android.widget.Image"
                + "[not(contains(@content-desc,'progress-bar')) and not(contains(@content-desc,'countdown'))])[1]"));
    }

    /** The info ("i") button is the first button rendered inside the widget WebView. */
    private Locator dynamicWidgetInfoButton(String sectionTitle) {
        return new Locator("DYNAMIC_WIDGET_INFO_BUTTON", AppiumBy.xpath("(" + dynamicWidgetTitleNode(sectionTitle)
                + "//following::android.webkit.WebView[1]//android.widget.Button)[1]"));
    }

    @Override
    public void swipeToDynamicWidget(String sectionTitle) {
        logger.log("Swiping to dynamic widget: " + sectionTitle);
        clickHomeIcon();
        driverTools.swipeToElement(TIKET_POINTS_CONTAINER, Swipe.DOWN, 10);
        Locator heading = dynamicWidgetTitle(sectionTitle);
        final int maxSwipeAttempts = 60;
        boolean found = false;
        for (int i = 0; i < maxSwipeAttempts; i++) {
            found = driverTools.isElementDisplayed(heading);
            if (found || driverTools.isElementDisplayed(BACK_TO_TOP_MARKER)) {
                break;
            }
            driverTools.swipeUp();
        }
        if (!found) {
            throw new RuntimeException("Dynamic widget '" + sectionTitle + "' not found on the home feed after "
                    + maxSwipeAttempts + " swipes.");
        }
        driverTools.swipeElementToMidScreen(heading);
    }

    @Override
    public String getDynamicWidgetTitle(String sectionTitle) {
        return driverTools.getText(dynamicWidgetTitle(sectionTitle)).orElse("");
    }

    @Override
    public String getDynamicWidgetSubtitle(String sectionTitle) {
        return driverTools.getText(dynamicWidgetSubtitle(sectionTitle)).orElse("");
    }

    @Override
    public boolean isDynamicWidgetImageDisplayed(String sectionTitle) {
        return driverTools.waitForElementToAppear(dynamicWidgetImage(sectionTitle), 10).isPresent();
    }

    @Override
    public void clickDynamicWidgetInfoButton(String sectionTitle) {
        driverTools.swipeToElement(dynamicWidgetInfoButton(sectionTitle), Swipe.UP, 2);
        driverTools.click(driverTools.waitForElementToBeClickable(dynamicWidgetInfoButton(sectionTitle), 10).orElseThrow());
    }

    @Override
    public boolean isDynamicWidgetInfoDetailDisplayed() {
        return driverTools.waitForElementToAppear(DYNAMIC_WIDGET_INFO_DETAIL, 15).isPresent();
    }

    @Override
    public void coachMarkHandling() {
        Boolean handled = FLAG_MAP_OK_COACHMARK.get(session.sessionId());
        if(handled != null && handled.equals(true)) {
            logger.log("Coachmark already handled 1 time");
            return;
        }
        driverTools.waitForElementToAppear(COACH_MARK_OK_BUTTON, 3).ifPresent(element -> {
            logger.log("Dismissing 'halo tiket' coach mark popup");
            driverTools.click(element);
            boolean gone = driverTools.waitForElementToDisappear(COACH_MARK_OK_BUTTON, 3);
            if(gone) {
                FLAG_MAP_OK_COACHMARK.put(session.sessionId(), true);
            }
        });
    }
}