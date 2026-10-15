package com.tiket.page.home;

import com.tiket.appium.Swipe;
import com.tiket.capabilities.CapabilitiesHandler;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.landingpage.LandingPageIOS;
import com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPageIOS;
import com.tiket.page.account.AccountPage;
import com.tiket.page.account.voucherboxpage.VoucherBoxPage;
import com.tiket.page.setting.SettingPage;
import com.tiket.page.carrental.searchpage.SearchPageCarRental;
import com.tiket.page.cartransfer.searchpage.CarTransferSearchPage;
import com.tiket.page.cartransfer.searchpage.CarTransferSearchPageIOS;
import com.tiket.page.flight.searchpage.FlightSearchPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.train.searchpage.TrainSearchPage;
import com.tiket.page.ttd.landingpage.TTDLandingPage;
import com.tiket.page.utilities.discovery.promo.PromoPage;
import com.tiket.pojo.utilities.*;
import com.tiket.service.BundleID;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static com.tiket.core.CoreFunctions.splitLanguageData;
import static com.tiket.page.account.AccountPageIOS.FLAG_TOP_OF_ACCOUNT_PAGE;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class HomePageIOS <T extends WebDriver> extends HomePage<T>{

    public static final Locator HOME = new Locator("HOME", AppiumBy.xpath("//XCUIElementTypeButton[@name='home' or @label='Home' or @label='Beranda'] | //XCUIElementTypeTabBar//*[@name='home']"));
    private static final Locator CURRENCY_COUNTRY_POPUP_TITLE = new Locator("CURRENCY_COUNTRY_POPUP_TITLE", AppiumBy.xpath("//*[contains(@label,'preferred currency') or contains(@name,'preferred currency') or contains(@label,'mata uang dan negara') or contains(@name,'mata uang dan negara')]"));
    private static final Locator CURRENCY_COUNTRY_CONTINUE_BUTTON = new Locator("CURRENCY_COUNTRY_CONTINUE_BUTTON", AppiumBy.xpath("//*[@type='XCUIElementTypeButton' and ((contains(@label,'Continue with') or contains(@name,'Continue with') or contains(@label,'Lanjut') or contains(@name,'Lanjut')) and (contains(@label,'(') or contains(@name,'(')))]"));
    private static final Locator CURRENCY_COUNTRY_KEEP_EXISTING_BUTTON = new Locator("CURRENCY_COUNTRY_KEEP_EXISTING_BUTTON", AppiumBy.xpath("//*[@type='XCUIElementTypeButton' and (contains(@label,'Keep using') or contains(@name,'Keep using') or contains(@label,'Tetap gunakan') or contains(@name,'Tetap gunakan'))]"));
    private static final Locator WELCOME_REWARDS_SHEET = new Locator("WELCOME_REWARDS_SHEET", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'lucky gift') or contains(@label,'New User Mission') or contains(@label,'Earn IDR') or contains(@label,'Kejutan buat pengguna baru') or contains(@label,'Misi Pengguna Baru') or contains(@label,'Raih IDR')]"));
    private static final Locator WELCOME_REWARDS_CLOSE = new Locator("WELCOME_REWARDS_CLOSE", AppiumBy.id("ic cancel grey"));
    // "halo tiket is ready to help you" / "halo tiket siap bantu, kapan pun" coach mark shown once after onboarding.
    private static final Locator COACH_MARK_TITLE = new Locator("COACH_MARK_TITLE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'halo tiket') or contains(@label,'BCA Blibli Tiket')]"));
    private static final Locator COACH_MARK_OK_BUTTON = new Locator("COACH_MARK_OK_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='OK' or @name='Oke']"));
    private static final Locator INFO_PAGES_POLICY_TAB = new Locator("INFO_PAGES_POLICY_TAB", AppiumBy.iOSClassChain("**/*[`label == 'Kebijakan Perjalanan' OR label == 'Travel Policy'`]"));
    private static final Locator INFO_PAGES_BENEFIT_TAB = new Locator("INFO_PAGES_BENEFIT_TAB", AppiumBy.iOSClassChain("**/*[`label == 'Benefit & Panduan' OR label == 'Benefits & Guides'`]"));
    private static final Locator INFO_PAGES_ITEMS = new Locator("INFO_PAGES_ITEMS", AppiumBy.xpath("//XCUIElementTypeCell[//XCUIElementTypeStaticText[contains(@label,'info & guide') or contains(@label, 'Info & panduan')]]//XCUIElementTypeImage[@name='bn_grid']"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("//*[(@name='tds_ic_back' or @name='tds ic back' or @name='back_button' or @name='TDSNavbarButton' or @name='back_btn' or @name='back') and @visible='true']"));
    private static final Locator BOTTOM_MENU_ICONS = new Locator("BOTTOM_MENU_ICONS", AppiumBy.accessibilityId("Tab Bar"));
    private static final Locator ACCOUNT_ICON = new Locator("ACCOUNT_ICON", AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`name == \"login\"`]"));
    private static final Locator ACCOUNT_ICON_TEXT_LOGIN = new Locator("ACCOUNT_ICON_TEXT_LOGIN", AppiumBy.xpath("//XCUIElementTypeButton/following-sibling::*//*[@name='Account' or @name='Akun']"));
    private static final Locator ACCOUNT_NAV_BAR_TEXT_BAHASA = new Locator("ACCOUNT_NAV_BAR_TEXT_BAHASA", AppiumBy.xpath("//XCUIElementTypeOther//*[contains(@label,'Akun') or contains(@label,'Masuk')]"));
    private static final Locator SEARCH_ICON = new Locator("SEARCH_ICON", AppiumBy.xpath("//*[@name='tds_ic_search' or @name='gs_search' or @name='gs_input']"));
    private static final Locator SEARCH_ICON_OLD = new Locator("SEARCH_ICON_OLD", AppiumBy.accessibilityId("gs_search"));
    private static final Locator TTD_LANDING_SEARCH_BAR = new Locator("TTD_LANDING_SEARCH_BAR", AppiumBy.name("searchBoxView"));
    private static final Locator INBOX = new Locator("inbox",AppiumBy.xpath("//*[@name='Notification' or @name='Notifikasi']"));
    private static final Locator INBOX_OLD = new Locator("inbox",AppiumBy.xpath("//XCUIElementTypeOther[@name='tds_ic_inbox'] | //XCUIElementTypeOther[@name='promo']/preceding-sibling::*//XCUIElementTypeImage"));
    //note : elements name is always changing, please change this locator.
    private static final Locator FIRST_PRODUCT_IN_PRODUCT_LIST = new Locator("FIRST_PRODUCT_IN_PRODUCT_LIST", AppiumBy.xpath(
            "(//*[@name='secondaryVerticalListView']//*[@label='Flights' or @label='Pesawat' or @label='To Dos' or @label='Hotels' or @label='Hotel' "
                    + "or @label='Trains' or @label='Train' or @label='Kereta' or @label='Airport Pickups' "
                    + "or @label='Jemputan Bandara' or @label='Car Rentals' or @label='Claim Center' or @label='Villa & Apt.'])[1]"));
    private static final Locator PRODUCT_LIST = new Locator("PRODUCT_LIST", AppiumBy.xpath("//*[@name='secondaryVerticalListView']"));
    private static final Locator PRODUCT_LIST_NON_IDR = new Locator("PRODUCT_LIST_NON_IDR", AppiumBy.xpath("(((//*[contains(@name,'hero_banner')]//following-sibling::XCUIElementTypeCell)[1])//XCUIElementTypeOther)[4]"));
    private static final Locator PRODUCT_LIST_IDR = new Locator("PRODUCT_LIST_NON_IDR", AppiumBy.id("vertical_icon_container"));
    private static final Locator INBOX_HEADER = new Locator("INBOX_HEADER", AppiumBy.xpath("//*[@name='inbox_header']"));
    private static final Locator ALERT_UNALLOW = new Locator("NOTIFICATION_UNALLOW", AppiumBy.xpath("//XCUIElementTypeButton[@label='Nanti aja' or @label='Ask App Not to Track' or @label='Don’t Allow']"));
    private static final Locator ALERT_ALLOW = new Locator("ALERT_ALLOW", AppiumBy.xpath("//XCUIElementTypeButton[@label='Nanti aja' or @label='Allow']"));
    private static final Locator FINANCE_BAR = new Locator("FINANCE_BAR", AppiumBy.xpath("//*[@type='XCUIElementTypeOther' and @name='finance_bar']"));
    private static final Locator SEE_ALL_PRODUCT = new Locator("SEE_ALL_PRODUCT", AppiumBy.xpath("//*[@name='seeAllText' or @label='Show all' or @label='Lihat semua']"));
    private static final Locator ALL_PRODUCT_CONTAINER = new Locator("ALL_PRODUCT_CONTAINER", AppiumBy.xpath("(//*[contains(@label,'All Products') or contains(@label,'Semua Produk')]//parent::*)[last()]//parent::*"));
    private static final String HOME_FEED_XPATH = "(//XCUIElementTypeCollectionView)[1]";
    public static final Locator SCREEN = new Locator("SCREEN", AppiumBy.xpath(HOME_FEED_XPATH));
    private static final Locator HOME_FEED_RECYCLER = SCREEN;

    //flash sale
    private static final Locator FLASH_SALE_INVENTORY_PRICE = new Locator("FLASH_SALE_INVENTORY_PRICE",AppiumBy.name("bn_chips_price"));
    public static final Locator INVENTORY_NAME = new Locator("INVENTORY_NAME", AppiumBy.xpath("(//*[@name='product_name'])[1]"));
    private static final Locator FLIGHT_INVENTORY_NAME = new Locator("FLIGHT_INVENTORY_NAME", AppiumBy.xpath("//*[@name='bn_chips']//following::*[contains(@label,'→') and @visible='true']"));
    private static final Locator ALL_INVENTORY_NAME = new Locator("ALL_INVENTORY_NAME", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='product_name' or contains(@label,'→')]"));
    private static final Locator FLASH_SALE_HOUR_COUNTDOWN = new Locator("FLASH_SALE_HOUR_COUNTDOWN", AppiumBy.accessibilityId("flash_hour_count_down"));
    private static final Locator FLASH_SALE_MINUTE_COUNTDOWN = new Locator("FLASH_SALE_MINUTE_COUNTDOWN", AppiumBy.accessibilityId("flash_minute_count_down"));
    private static final Locator FLASH_SALE_SECOND_COUNTDOWN = new Locator("FLASH_SALE_SECOND_COUNTDOWN", AppiumBy.accessibilityId("flash_second_count_down"));
    private static final Locator FLASH_SALE_SEE_ALL = new Locator("FLASH_SALE_SEE_ALL", AppiumBy.accessibilityId("flash_sale_see_all"));
    private static final Locator HOMES_RECENT_SEARCH_CHIP = new Locator("HOMES_RECENT_SEARCH_CHIP", AppiumBy.xpath("//*[@name='page_module_title' and (contains(@label,'Pencarian Terakhir') or contains(@label,'Recently search') or contains(@label,'Recently Search'))]//following::XCUIElementTypeStaticText[(@label='Villa & Apt.' or @label='Vila & Apt.') and @visible='true']"));
    public static final Locator CANCEL_TICKET_POPUP = new Locator("CANCEL_TICKET_POPUP", AppiumBy.id("ic cancel grey"));
    public static final Locator PROMO_ICON = new Locator("PROMO_ICON", AppiumBy.accessibilityId("tds_ic_discount_ribbon"));
    private static final Locator MY_ORDER = new Locator("ORDER", AppiumBy.name("your_order"));
    private static final Locator INBOX_BOTTOM_SHEET = new Locator("INBOX_BOTTOM_SHEET", AppiumBy.id("Horizontal scroll bar, 1 page"));
    private static final Locator SKIP_ONBOARDING = new Locator("SKIP_ONBOARDING", AppiumBy.accessibilityId("skip_on_boarding"));
    private static final Locator SKIP_ONBOARDING_SECOND_PAGE = new Locator("SKIP_ONBOARDING_SECOND_PAGE", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'Lewati') or contains(@label,'Skip')]"));
    public static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.accessibilityId("button"));
    public static final Locator CONTINUE_BUTTON_FIRST_PAGE = new Locator("CONTINUE_BUTTON_FIRST_PAGE", AppiumBy.xpath("//*[@name='tds_button_view'] | //XCUIElementTypeButton[contains(@label,'Lanjutkan') or contains(@label,'Lanjut') or contains(@label,'Continue')]"));
    public static final Locator BLIBLI_POINTS = new Locator("BLIBLI_POINTS", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@label,'Blibli Tiket Points') or contains(@label,'PayLater') or contains(@label,'Blipay') or contains(@label,'BlibliPay') or contains(@label,'Log in') or contains(@label,'Activate now')])[1]"));
    public static final Locator HOME_ICON = new Locator("HOME_ICON", AppiumBy.accessibilityId("home"));
    private static final Locator HERO_BANNER = new Locator("HERRO_BANNER", AppiumBy.name("hero_banner"));
    private static final Locator HERO_BANNER_VISIBLE = new Locator("HERO_BANNER_VISIBLE", AppiumBy.xpath("//*[@type='XCUIElementTypeOther' and ./*[@name='verticalView']]"));
    // Shared across parallel TestNG threads (parallel=methods) via a common session pool, so use a
    // thread-safe map to avoid corruption / cross-thread onboarding-state leakage.
    private static final Map<String, Boolean> FLAG_MAP = new java.util.concurrent.ConcurrentHashMap<>();
    // Tracks per-session dismissal of the one-time "halo tiket" coach mark so later calls skip the
    // detection wait once it has been confirmed dismissed (mirrors Android's COACH_MARK_MAP).
    private static final Map<String, Boolean> COACH_MARK_MAP = new java.util.concurrent.ConcurrentHashMap<>();
    public static final Locator PLAY_BUTTON_LIVE = new Locator("PLAY_BUTTON_LIVE", AppiumBy.xpath("//*[contains(@src,'ic-play.')]"));
    private static final Locator RECENT_SEARCH_CONTAINER = new Locator("RECENT_SEARCH_CONTAINER", AppiumBy.xpath("//*[@name='page_module_title' and (contains(@label,'Pencarian Terakhir') or contains(@label,'Recently search') or contains(@label,'Recently Search'))]//following::XCUIElementTypeCollectionView"));
    private static final Locator RECENT_SEARCH_SECTION = new Locator("RECENT_SEARCH_SECTION", AppiumBy.xpath("//*[contains(@label,'Pencarian Terakhir') or contains(@label,'Recently search') or contains(@label,'Recently Search')]"));
    private static final Locator RECENT_SEARCH_TITLE = new Locator("RECENT_SEARCH_TITLE", AppiumBy.xpath("//*[@name='page_module_title' and (contains(@label,'Pencarian Terakhir') or contains(@label,'Recently search') or contains(@label,'Recently Search'))]"));
    private static final Locator CLEAR_RECENT_SEARCH = new Locator("CLEAR_RECENT_SEARCH", AppiumBy.xpath("//*[@name='page_module_hyperlink_text' and (contains(@label,'Clear') or contains(@label,'Hapus'))]"));
    private static final Locator CONFIRM_CLEAR_RECENT_SEARCH = new Locator("CONFIRM_CLEAR_RECENT_SEARCH", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'Delete') or contains(@label,'Hapus')]"));
    public static final Locator GS_INPUT_BAR = new Locator("GS_INPUT_BAR", AppiumBy.name("gs_input"));
    public static final Locator HOME_PAGE_GLOBAL_SEARCH = new Locator("HOME_PAGE_GLOBAL_SEARCH", AppiumBy.xpath("//*[@name='tds_ic_discount_ribbon']//preceding::XCUIElementTypeStaticText[last()]"));
    public static final Locator PROMO_CHIP = new Locator("PROMO_CHIP", AppiumBy.accessibilityId("tds_ic_discount_ribbon"));
    private static final Locator ACTIVATE_PIN = new Locator("ACTIVATE_PIN", AppiumBy.accessibilityId("banner_container"));
    public static final Locator ALLOW_ACCESS_BUTTON = new Locator("ALLOW_ACCESS_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'Allow Once')]"));
    public static final Locator LOCATION_BANNER = new Locator("LOCATION_BANNER_TITLE_HEADING", AppiumBy.name("banner_location"));
    public static final Locator CONTINUE_BUTTON_LOCATION = new Locator("CONTINUE_BUTTON_LOCATION", AppiumBy.name("buttonPrimary"));
    private static final Locator PROMO_VOUCHER_BOX = new Locator("PROMO_VOUCHER_BOX", AppiumBy.accessibilityId("Voucher Box"));
    private static final Locator PROMO_SCROLL_BAR = new Locator("PROMO_SCROLL_BAR", AppiumBy.xpath("//XCUIElementTypeScrollView"));
    private static final Locator DOWN_ARROW = new Locator("DOWN_ARROW", AppiumBy.name("tds_ic_arrow_down"));
    private static final Locator UP_ARROW = new Locator("UP_ARROW", AppiumBy.xpath("//*[@type='XCUIElementTypeOther' and ./*[@name='Back to top'] or ./*[@name='Kembali ke atas']]"));
    public static final Locator CLOSE_GIVE_RATING_POPUP = new Locator("CLOSE_GIVE_RATING_POPUP", AppiumBy.xpath("//XCUIElementTypeButton[@label='Not Now' or @label='Nanti saja' or @label='Nanti aja' or @label='Lain kali']"));

    //global search locator
    private static final Locator SUGGESTION_LIST_TITLE = new Locator("SUGGESTION_LIST_TITLE", AppiumBy.xpath("//XCUIElementTypeTable//XCUIElementTypeCell//XCUIElementTypeStaticText"));
    private static final Locator SUGGESTION_LIST_SUBTITLE = new Locator("SUGGESTION_LIST_SUBTITLE", AppiumBy.xpath("//XCUIElementTypeTable//XCUIElementTypeCell//XCUIElementTypeStaticText[2]"));
    private static final Locator SUGGESTION_LIST_ICON = new Locator("SUGGESTION_LIST_ICON", AppiumBy.xpath("//XCUIElementTypeTable//XCUIElementTypeCell//*[contains(@name,'gs_img_tdm_sectioned_global_search_result_item')]"));
    private static final Locator DEFAULT_CHIPS_LOCATOR = new Locator("DEFAULT_CHIPS_LOCATOR", AppiumBy.accessibilityId("bn_chips"));
    private static final Locator FINANCE_BAR_INFO = new Locator("FINANCE_BAR_INFO", AppiumBy.id("tds_ic_alert_high"));
    private static final Locator UNREAD_COUNT = new Locator("UNREAD_COUNT", AppiumBy.xpath("//*[@name='unread_count']"));
    private static final Locator WEBVIEW_POST = new Locator("WEBVIEW_POST", AppiumBy.xpath("//*[contains(@class,'post-wrapper')]"));
    private static final Locator BLIBLI_TIKET_POINTS_FINANCE_BAR = new Locator("BLIBLI_TIKET_POINTS_FINANCE_BAR", AppiumBy.id("finance_bar_benefit_text"));
    private static final Locator BLIBLI_TIKET_POINTS_PAGE_HEADER = new Locator("BLIBLI_TIKET_POINTS_PAGE_HEADER", AppiumBy.xpath("//*[@name='Blibli Tiket Rewards']"));
    private static final Locator REWARDS_BOTTOM = new Locator("REWARDS_BOTTOM", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and @label='Rewards']"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", AppiumBy.xpath("//*[@label='Log in' and @type='XCUIElementTypeStaticText']"));
    private static final Locator LOGIN_REGISTER_BUTTON = new Locator("LOGIN_REGISTER_BUTTON", AppiumBy.xpath("//*[@label='Log in or register' and @type='XCUIElementTypeStaticText']"));
    private static final Locator BACK_BLIBLI_TIKET_POINTS = new Locator("BACK_BLIBLI_TIKET_POINTS", AppiumBy.xpath("//*[@class='header__btn']"));
    private static final Locator PAYLATER_FINANCE_BAR = new Locator("PAYLATER_FINANCE_BAR", AppiumBy.xpath("//*[@name='finance_bar_heading' and @label='PayLater']"));
    private static final Locator BACK_PAYLATER = new Locator("BACK_PAYLATER", AppiumBy.name("tds_ic_back"));
    private static final Locator PAYLATER_PAGE= new Locator("PAYLATER_PAGE", AppiumBy.xpath("//*[contains(@label,'PayLater')]"));
    private static final Locator PAYLATER_USP_LIMIT_LABEL = new Locator("PAYLATER_USP_LIMIT_LABEL", AppiumBy.xpath("//*[contains(@label,'Get a limit of up to')]"));
    private static final Locator PAYLATER_USP_CTA = new Locator("PAYLATER_USP_CTA", AppiumBy.xpath("//*[contains(@label,'Activate to claim benefits') or contains(@label,'Activate')]"));
    private static final Locator BLIBLIPAY_FINANCE_BAR = new Locator("BLIBLIPAY_FINANCE_BAR", AppiumBy.xpath("//*[@label='BlibliPay' and @name='finance_bar_heading']"));
    private static final Locator PAYLATER_DETAIL_TEXT = new Locator("PAYLATER_DETAIL_TEXT", AppiumBy.xpath("//*[contains(@name,'finance_bar')]//*[@label='PayLater']"));
    private static final Locator BLIPAY_DETAIL_TEXT = new Locator("BLIPAY_DETAIL_TEXT", AppiumBy.xpath("//*[contains(@name,'finance_bar')]//*[@label='BlibliPay']"));
    private static final Locator BLIBLIPAY_HEADER = new Locator("BLIBLIPAY_PAGE", AppiumBy.name("BlibliPay"));
    private static final Locator BACK_BUTTON_WEBVIEW = new Locator("BACK_BUTTON_WEBVIEW", AppiumBy.name("back"));
    private static final Locator PDP_PAGE_BACK_BUTTON = new Locator("PDP_PAGE_BACK_BUTTON", AppiumBy.name("back-button"));
    private static final Locator HIGHTLIGHT_DISCOVER_PRODUCT_IMAGE = new Locator("HIGHTLIGHT_DISCOVER_PRODUCT_IMAGE", AppiumBy.name("highlight_image"));
    private static final Locator COUNTDOWN_CONTINUE_TO_PAYMENT = new Locator("COUNTDOWN_CONTINUE_TO_PAYMENT", AppiumBy.xpath("//*[contains(@label,'Complete your booking in') or contains(@label,'Selesaikan pemesanan dalam')]"));
    private static final Locator CONTINUE_TO_PAYMENT_BTN = new Locator("CONTINUE_TO_PAYMENT_BTN", AppiumBy.accessibilityId("continue_payment_button"));
    public static final Locator PROMO_OVERLAY_BANNER_CLOSE_BTN = new Locator("PROMO_OVERLAY_BANNER_CLOSE_BTN", AppiumBy.xpath("")); //TODO FIX
    public static final Locator TOOLTIP_MESSAGE = new Locator("TOOLTIP_MESSAGE", AppiumBy.accessibilityId("finance_bar_info"));
    //Upcoming Booking Page Module
    private static final Locator UPCOMING_BOOKING_FLIGHT = new Locator("UPCOMING_BOOKING_FLIGHT", AppiumBy.xpath("//*[contains(@label,'Flight to') or contains(@label,'Penerbangan ke') or contains(@label,'Pesawat') or contains(@label,'Flight')]"));
    private static final Locator UPCOMING_BOOKING_COUNTDOWN = new Locator("UPCOMING_BOOKING_COUNTDOWN", AppiumBy.xpath("//*[contains(@label,'HOUR') or contains(@label,'DAY') or contains(@label,'JAM') or contains(@label,'HARI')]"));
    private static final Locator UPCOMING_BOOKING_STAY = new Locator("UPCOMING_BOOKING_STAY", AppiumBy.xpath("//*[contains(@label,'Stay at') or contains(@label,'Menginap di')]"));
    private static final Locator UPCOMING_BOOKING_HOTEL_DETAILS = new Locator("UPCOMING_BOOKING_HOTEL_DETAILS", AppiumBy.xpath("//*[contains(@label,'Stay at') or contains(@label,'Menginap di')]/following-sibling::*[1]"));
    private static final Locator SEE_ALL_UPCOMING_BOOKING = new Locator("SEE_ALL_UPCOMING_BOOKING", AppiumBy.xpath("//*[@name='page_module_hyperlink_text' and (contains(@label,'See all (') or contains(@label,'Lihat semua ('))]"));
    private static final Locator TITLE_CROSS_SELL = new Locator("TITLE_CROSS_SELL", AppiumBy.xpath("//*[contains(@name,'upcoming preprod') and @type='XCUIElementTypeStaticText']"));
    private static final Locator CROSS_SELL_CARDS = new Locator("CROSS_SELL_CARDS", AppiumBy.xpath("//*[contains(@name,'upcoming preprod') and @type=;XCUIElementTypeStaticText']/following-sibling:: XCUIElementTypeCollectionView[1]"));
    private static final Locator TRANSPORT_CROSS_SELL_CARD = new Locator("TRANSPORT_CROSS_SELL_CARD", AppiumBy.xpath("//*[(contains(@label,'Transportasi') or contains(@label,'Transportations')) and @type=\"XCUIElementTypeStaticText\"]"));
    private static final Locator FLIGHT_CARD_CROSS_SELL = new Locator("FLIGHT_CARD_CROSS_SELL", AppiumBy.xpath("//*[(contains(@name,'Pesawat') or contains(@name,'Flights'))  and @type='XCUIElementTypeStaticText']"));
    private static final Locator UPCOMING_BOOKING_DEPARTURE_TIME = new Locator("UPCOMING_BOOKING_DEPARTURE_TIME", AppiumBy.xpath("(//XCUIElementTypeButton[@name='tds ic cross big']/following-sibling::XCUIElementTypeOther)[1]/XCUIElementTypeStaticText[2]"));
    private static final Locator UPCOMING_BOOKING_ROUTE_INFO = new Locator("UPCOMING_BOOKING_ROUTE_INFO", AppiumBy.xpath("(//XCUIElementTypeButton[@name='tds ic cross big']/following-sibling::XCUIElementTypeOther)[1]/XCUIElementTypeStaticText[1]"));
    private static final Locator UPCOMING_BOOKING_VENDOR_ICON = new Locator("UPCOMING_BOOKING_VENDOR_ICON", AppiumBy.xpath("//XCUIElementTypeButton[@name='tds ic cross big']/following-sibling::XCUIElementTypeImage"));
    private static final Locator UPCOMING_BOOKING_SEE_MORE_INFO = new Locator("UPCOMING_BOOKING_SEE_MORE_INFO", AppiumBy.xpath("//XCUIElementTypeButton[@name='tds ic cross big']/following-sibling::XCUIElementTypeButton[@name='See More' or @name='Selengkapnya']"));
    private static final Locator UPCOMING_BOOKING_CLOSE_BUTTON = new Locator("UPCOMING_BOOKING_CLOSE_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='tds ic cross big']"));
    private static final Locator UPCOMING_BOOKING_CROSS_SELL_TITLE = new Locator("UPCOMING_BOOKING_CROSS_SELL_TITLE", AppiumBy.xpath("//XCUIElementTypeButton[@name='tds ic cross big']/ancestor::XCUIElementTypeCell//XCUIElementTypeCollectionView[./XCUIElementTypeOther[contains(@name,'Vertical scroll bar')]]/parent::*/parent::*/parent::*/XCUIElementTypeStaticText"));
    private static final Locator UPCOMING_BOOKING_CROSS_SELL_CARD = new Locator("UPCOMING_BOOKING_CROSS_SELL_CARD", AppiumBy.xpath("(//XCUIElementTypeButton[@name='tds ic cross big']/ancestor::XCUIElementTypeCell//XCUIElementTypeCollectionView[./XCUIElementTypeOther[contains(@name,'Vertical scroll bar')]]/child::XCUIElementTypeCell)[1]"));
    private static final Locator UPCOMING_BOOKING_PASSENGER_NAME = new Locator("UPCOMING_BOOKING_PASSENGER_NAME", AppiumBy.xpath("(//XCUIElementTypeButton[@name='tds ic cross big']/following-sibling::XCUIElementTypeOther)[2]//XCUIElementTypeStaticText[3]"));
    private static final Locator UPCOMING_BOOKING_CLASS_NAME = new Locator("UPCOMING_BOOKING_CLASS_NAME", AppiumBy.xpath("(//XCUIElementTypeButton[@name='tds ic cross big']/following-sibling::XCUIElementTypeOther)[2]//XCUIElementTypeStaticText[1]"));
    private static final Locator UPCOMING_BOOKING_ORIGIN_STATION = new Locator("UPCOMING_BOOKING_ORIGIN_STATION", AppiumBy.xpath("(//XCUIElementTypeButton[@name='tds ic cross big']/following-sibling::XCUIElementTypeOther)[2]//XCUIElementTypeStaticText[2]"));
    private static final Locator SEE_MORE_UPCOMING_BOOKING_CARD = new Locator("SEE_MORE_UPCOMING_BOOKING_CARD", AppiumBy.xpath("//XCUIElementTypeButton[@name='tds ic cross big']/ancestor::XCUIElementTypeCell//XCUIElementTypeButton[contains(@name,'(')]"));
    private static final Locator UPCOMING_BOOKING_CARD_LIST_BOTTOMSHEET = new Locator("UPCOMING_BOOKING_CARD_LIST_BOTTOMSHEET", AppiumBy.xpath("//XCUIElementTypeWindow[1]/XCUIElementTypeOther[2]//XCUIElementTypeTable[.//XCUIElementTypeButton[@name='tds ic cross big']]"));
    private static final Locator UPCOMING_BOOKING_CARD_LIST_BOTTOMSHEET_CLOSE_BUTTON = new Locator("UPCOMING_BOOKING_CARD_LIST_BOTTOMSHEET_CLOSE_BUTTON", AppiumBy.id("ic cancel grey"));
    private static final Locator UPCOMING_BOOKING_HIDE_CARD_BOTTOMSHEET = new Locator("UPCOMING_BOOKING_HIDE_CARD_BOTTOMSHEET", AppiumBy.xpath("//XCUIElementTypeWindow[1]/XCUIElementTypeOther[2]//XCUIElementTypeButton[@label='Hide' or @label='Sembunyikan']"));
    private static final Locator UPCOMING_BOOKING_HIDE_CARD_BOTTOMSHEET_CANCEL_BUTTON = new Locator("UPCOMING_BOOKING_HIDE_CARD_BOTTOMSHEET_CANCEL_BUTTON", AppiumBy.xpath("//XCUIElementTypeWindow[1]/XCUIElementTypeOther[2]//XCUIElementTypeButton[@label='Cancel' or @label='Batal']"));
    private static final Locator UPCOMING_BOOKING_CARD = new Locator("UPCOMING_BOOKING_CARD", AppiumBy.xpath("//XCUIElementTypeButton[@name='tds ic cross big']/parent::*/parent::*"));
    private static final Locator UPCOMING_BOOKING_VOUCHER_BOX = new Locator("UPCOMING_BOOKING_VOUCHER_BOX", AppiumBy.xpath("//XCUIElementTypeButton[@name='tds ic cross big']/ancestor::XCUIElementTypeCell//XCUIElementTypeCollectionView[./XCUIElementTypeOther[contains(@name,'Vertical scroll bar')]]/child::XCUIElementTypeCell[.//XCUIElementTypeStaticText[contains(@label,'Voucher')]]"));
    private static final Locator UPCOMING_BOOKING_ALL_PROMO = new Locator("UPCOMING_BOOKING_ALL_PROMO", AppiumBy.xpath("//XCUIElementTypeButton[@name='tds ic cross big']/ancestor::XCUIElementTypeCell//XCUIElementTypeCollectionView[./XCUIElementTypeOther[contains(@name,'Vertical scroll bar')]]/child::XCUIElementTypeCell[.//XCUIElementTypeStaticText[contains(@label,'Promo')]]"));
    private static final Locator CROSS_SELL_CAROUSEL = new Locator("CROSS_SELL_CAROUSEL", AppiumBy.xpath("//XCUIElementTypeButton[@name='tds ic cross big']/ancestor::XCUIElementTypeCell//XCUIElementTypeCollectionView[./XCUIElementTypeOther[contains(@name,'Vertical scroll bar')]]"));
    private static final Locator UPCOMING_BOOKING_VERTICAL = new Locator("UPCOMING_BOOKING_VERTICAL", AppiumBy.xpath("(//XCUIElementTypeButton[@name='tds ic cross big']/ancestor::XCUIElementTypeCell//XCUIElementTypeCollectionView[./XCUIElementTypeOther[contains(@name,'Vertical scroll bar')]]/child::XCUIElementTypeCell[.//XCUIElementTypeImage[@name='tds_ic_oval_plus']])[1]"));
    private static final Locator UPCOMING_BOOKING_VERTICAL_TEXT = new Locator("UPCOMING_BOOKING_VERTICAL_TEXT", AppiumBy.xpath("(//XCUIElementTypeButton[@name='tds ic cross big']/ancestor::XCUIElementTypeCell//XCUIElementTypeCollectionView[./XCUIElementTypeOther[contains(@name,'Vertical scroll bar')]]/child::XCUIElementTypeCell//XCUIElementTypeImage[@name='tds_ic_oval_plus']/preceding-sibling::*)[1]"));
    private static final Locator NEXT_PAGE_MODULE = new Locator("NEXT_PAGE_MODULE", AppiumBy.xpath("(//XCUIElementTypeButton[@name='tds ic cross big']/ancestor::XCUIElementTypeCell/following-sibling::XCUIElementTypeCell)[1]"));
    private static final Locator CONTINUE_AS_GUEST = new Locator("CONTINUE_AS_GUEST", AppiumBy.xpath("//XCUIElementTypeButton[@label='Continue as guest' or @label='Lanjut sebagai tamu']"));

    private static final Locator GS_INFO_CONTAINER = new Locator("GS_INFO_CONTAINER", AppiumBy.xpath("(//XCUIElementTypeOther[@name=\\\"gs_info\\\"])[1]"));

    private static final Locator GS_INFO = new Locator("GS_INFO",AppiumBy.xpath("(//XCUIElementTypeStaticText[@name=\"gs_info_text\"])[1]"));
    private static final Locator GS_INFO_BANNER = new Locator("GS_INFO_BANNER",AppiumBy.xpath("(//XCUIElementTypeOther[@name=\"gs_info\"])[1]"));
    private static final Locator GS_HOME_PAGE_SEARCH_BAR = new Locator("GS_HOME_PAGE_SEARCH_BAR",AppiumBy.name("gs_input"));
    private static final Locator GS_INFO_CLOSE = new Locator("GS_INFO_CLOSE", AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label == \"tds ic cross big\"`][1]"));
    private static final Locator WISHLIST_ICON = new Locator("WISHLIST_ICON", AppiumBy.id("wishlist"));
    private static final Locator WISHLIST_PAGE = new Locator("WISHLIST_PAGE", AppiumBy.id("Wishlist Landing"));
    private static final Locator PROMO_POPUP = new Locator("PROMO_POPUP", AppiumBy.iOSClassChain("**/*[`name=='pr close' OR name=='closeImageView'`]"));
    private static final Locator FINANCE_BAR_TITLE = new Locator("FINANCE_BAR_TITLE", AppiumBy.accessibilityId("finance_bar_heading"));
    private static final Locator CLOSE_IMAGE = new Locator("CLOSE_IMAGE", AppiumBy.xpath("//*[@name='closeImageView']"));

    public HomePageIOS(Session<T> session) {
        super(session);
    }

    private Locator getMessageLocator(String message){
        String messageLower = message == null ? "" : message.toLowerCase();
        return new Locator("messageElement", AppiumBy.xpath("(//*[contains(@name,'inbox_notification_title') and contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'"+messageLower+"')])[1]"));
    }

    private void swipeToProductsMenu(Locator product){
        driverTools.swipeByDistance(PRODUCT_LIST,Swipe.RIGHT,0.6);
        driverTools.swipeByDistance(PRODUCT_LIST,Swipe.RIGHT,0.6);
        WebElement e = driverTools.swipeByDistanceToElement(product,PRODUCT_LIST,Swipe.LEFT,0.6,5).orElseThrow();
        driverTools.click(e);
    }

    private boolean isProductListOnHomePageSwipelable() {
        driverTools.switchToNative();
        closeGiveRatingPopup();
        if (!isOnHomePage(5)) {
            returnToHomeFromVertical();
        }
        waitForHomePageToLoad();
        driverTools.swipeToPresentedElement(PRODUCT_LIST, Swipe.UP, 8);
        if (!driverTools.waitForElementToPresence(PRODUCT_LIST, 10).isPresent()) {
            return false;
        }
        Locator firstProduct = driverTools.isElementPresent(FIRST_PRODUCT_IN_PRODUCT_LIST)
                ? FIRST_PRODUCT_IN_PRODUCT_LIST
                : INVENTORY_NAME;
        if (!driverTools.isElementPresent(firstProduct)) {
            return true;
        }
        driverTools.swipeByDistance(PRODUCT_LIST, Swipe.RIGHT, 0.8);
        driverTools.swipeByDistance(PRODUCT_LIST, Swipe.RIGHT, 0.8);
        WebElement productElement = driverTools.waitForElementToPresence(firstProduct, 10).orElseThrow();
        String productNameBeforeSwipe = driverTools.getText(productElement);
        if (productNameBeforeSwipe == null || productNameBeforeSwipe.isBlank()) {
            productNameBeforeSwipe = productElement.getAttribute("label");
        }
        driverTools.swipeLeft(PRODUCT_LIST);
        driverTools.swipeLeft(PRODUCT_LIST);
        productElement = driverTools.waitForElementToPresence(firstProduct, 10).orElseThrow();
        String productNameAfterSwipe = driverTools.getText(productElement);
        if (productNameAfterSwipe == null || productNameAfterSwipe.isBlank()) {
            productNameAfterSwipe = productElement.getAttribute("label");
        }
        logger.log("product name before swipe -> " + productNameBeforeSwipe);
        logger.log("product name after swipe -> " + productNameAfterSwipe);
        return productNameBeforeSwipe != null && !productNameBeforeSwipe.equals(productNameAfterSwipe);
    }

    private boolean isVerticalPageOpen(String verticalName) {
        switch (verticalName) {
            case "Airport Pickups" -> {
                try {
                    driverTools.waitForWebview();
                    driverTools.switchToWebViewUrl("airport-transfer");
                    driverTools.waitForWebToLoad();
                    return driverTools.waitForElementToAppear(CarTransferSearchPage.CHOOSE_AIRPORT, 15).isPresent()
                            || driverTools.waitForElementToAppear(CarTransferSearchPage.SEARCH_BAR_FOR_AIRPORT, 15).isPresent();
                } catch (Exception e) {
                    logger.log("Airport Pickups webview check skipped: " + e.getMessage());
                    return false;
                } finally {
                    driverTools.switchToNative();
                }
            }
            case "Car Rentals" -> {
                driverTools.waitForWebview();
                driverTools.switchToWebView();
                Locator carRentalsIcon = new Locator("VERTICAL_ICON", AppiumBy.xpath(
                        "//*[contains(text(),'Car Rentals') or contains(text(),'Rental Mobil')]"));
                boolean result = driverTools.waitForElementToAppear(carRentalsIcon, 10).isPresent();
                driverTools.switchToNative();
                return result;
            }
            case "To Dos" -> {
                return driverTools.waitForElementToPresence(
                        new Locator("VERTICAL_ICON", AppiumBy.name("searchBoxView")), 10).isPresent();
            }
            case "Trains" -> {
                return driverTools.waitForElementToPresence(
                        new Locator("VERTICAL_ICON", AppiumBy.id("topSearchForm")), 10).isPresent();
            }
            case "Flights" -> {
                return driverTools.waitForElementToPresence(
                        new Locator("VERTICAL_ICON", AppiumBy.id("flightSearchButton")), 10).isPresent();
            }
            case "Hotels" -> {
                return driverTools.waitForElementToPresence(
                        new Locator("VERTICAL_ICON", AppiumBy.id("search_field_btn")), 10).isPresent();
            }
            default -> throw new NotImplementedException("Unimplemented vertical type!");
        }
    }

    private boolean isSessionInBHS(){
        if(driverTools.waitForElementToPresence(SEE_ALL_PRODUCT).isPresent()){
            String seeAllProductText = driverTools.getText(SEE_ALL_PRODUCT).orElseThrow();
            logger.log("seeAllProductText : " + seeAllProductText);
            return seeAllProductText.equalsIgnoreCase("lihat semua");
        }
        return driverTools.waitForElementToPresence(ACCOUNT_NAV_BAR_TEXT_BAHASA).isPresent();
    }

    private Locator getGSWarningLocator(String expectedWarningKeywordId,String expectedWarningKeywordEn,String currentAppLang){
        if(currentAppLang.equals("en")){
            return new Locator("GS_WARNING_KEYWORD", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@value, '"+expectedWarningKeywordEn+"')]"));
        }else{
            return new Locator("GS_WARNING_KEYWORD", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@value, '"+expectedWarningKeywordId+"')]"));
        }
    }

    @Override
    public String getLanguageApp() {
        String language = "";
        if (!isSessionInBHS()) language = "en";
        else language = "id";
        logger.log("language : " + language);
        return language;
    }

    private void initFlag(String sessionID, boolean check){
        FLAG_MAP.put(sessionID,check);
    }

    @Override
    public boolean isSuggestionItemListsShown() {
        boolean title = driverTools.waitForElementToAppear(SUGGESTION_LIST_TITLE).isPresent();
        boolean subtitle = driverTools.waitForElementToAppear(SUGGESTION_LIST_SUBTITLE).isPresent();
        boolean icon = driverTools.waitForElementToAppear(SUGGESTION_LIST_ICON).isPresent();
        Assertion.assertThat("Verify title present", title, is(true));
        Assertion.assertThat("Verify subtitle present", subtitle, is(true));
        return true;
    }

    @Override
    public ArrayList<String> getSuggestionItemListTitle() {
        driverTools.waitForElementToAppear(SUGGESTION_LIST_TITLE,10).orElseThrow();
        ArrayList<String> titles = new ArrayList<>();
        List<WebElement> elementSearchResultRecomendations = driverTools.elements(SUGGESTION_LIST_TITLE).orElseThrow();
        for(WebElement listTitleElement: elementSearchResultRecomendations){
            String title = driverTools.getText(listTitleElement);
            logger.log("text on the title -> " + title);
            titles.add(title);
        }
        return titles;
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
    public void selectInfoPagesItem() {
        List<WebElement> items = driverTools.elements(INFO_PAGES_ITEMS).orElseThrow();
        int totalItem = items.size();
        int randomItem = generateRandomInt(0, totalItem - 1);
        logger.log("index of randomItem -> " + randomItem);
        driverTools.click(items.get(randomItem));
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
    public void clickHomeIcon() {
        if (driverTools.waitForElementToAppear(HOME, 8).isPresent()) {
            driverTools.click(HOME);
            return;
        }
        goToHomeUsingDeeplinkWithoutCondition();
    }

    @Override
    public void gotoHome() {
        String sessionID = session.sessionId();
        driverTools.switchToNative();

        if(Configs.CODE_COVERAGE) {
            if(driverTools.waitForElementToPresence(CONTINUE_AS_GUEST,3).isPresent()){
                driverTools.click(CONTINUE_AS_GUEST);
            }
            goToHomeDeeplink();
        }

        if(FLAG_MAP.get(sessionID)==null||!FLAG_MAP.get(sessionID)){
            logger.log("on-boarding process start");
            boolean check = false;
            Optional<WebElement> conitnueButton = driverTools.waitForElementToAppear(CONTINUE_BUTTON_FIRST_PAGE,20);
            if(conitnueButton.isPresent()) {
                driverTools.clickElementByCoordinate(CONTINUE_BUTTON_FIRST_PAGE);

                driverTools.waitForElementToBeClickable(CONTINUE_BUTTON,20);
                driverTools.clickElementByCoordinate(CONTINUE_BUTTON);

                Map<String, Object> caps = driverTools.getCapabilities();

                boolean autoDismissAlerts = caps.get("autoDismissAlerts")==null? false : (boolean) caps.get("autoDismissAlerts");
                logger.log("autoDismissAlerts: " + autoDismissAlerts);

                boolean isRunningOnDeviceFarm = CapabilitiesHandler.isRunningOnDeviceFarm();
                logger.log("is on isRunningOnDeviceFarm: " + isRunningOnDeviceFarm);

                if(!autoDismissAlerts || !isRunningOnDeviceFarm){
                    if(driverTools.waitForElementToPresence(ALERT_ALLOW).isPresent()){
                        closeAlerts(ALERT_ALLOW);
                        closeAlerts(ALERT_ALLOW);
                    }else{
                        closeAlerts(ALERT_UNALLOW);
                        closeAlerts(ALERT_UNALLOW);
                    }
                }

                if (driverTools.waitForElementToBeClickable(CONTINUE_BUTTON,3).isPresent())
                    driverTools.clickElementByCoordinate(CONTINUE_BUTTON);

                if(!autoDismissAlerts || !isRunningOnDeviceFarm){
                    if(driverTools.waitForElementToPresence(ALERT_ALLOW).isPresent()){
                        closeAlerts(ALERT_ALLOW);
                        closeAlerts(ALERT_ALLOW);
                    }else{
                        closeAlerts(ALERT_UNALLOW);
                        closeAlerts(ALERT_UNALLOW);
                    }
                }

                if(driverTools.waitForElementToAppear(SKIP_ONBOARDING_SECOND_PAGE,5).isPresent()){
                    driverTools.clickElementByCoordinate(SKIP_ONBOARDING_SECOND_PAGE);
                    check = true;
                }

              if (driverTools.waitForElementToAppear(CANCEL_TICKET_POPUP, 5).isPresent()) {
                driverTools.click(CANCEL_TICKET_POPUP);
              }

                initFlag(sessionID,check);
                logger.log("on-boarding process success: " + check);
                goToHomeDeeplink(); //workaround home icon
            }
        }else{
            //if failure happened when automation is in webview context it will switch back to native back.
            goToHomeDeeplink();
            driverTools.switchToNative();

        }
        if(waitForHomePageToLoad()) {
            coachMarkHandling();
            return;
        }

        if(!Configs.CODE_COVERAGE) {
            IOSDriver iosDriver = (IOSDriver) driver;
            iosDriver.terminateApp(BundleID.getID());
            iosDriver.activateApp(BundleID.getID());
        } else {
            Deeplink homeDeeplink = new Deeplink("Home",
                    "tiket://gatotkaca.tiket.com",
                    "tiket://preprod.tiket.com",
                    "tiket://www.tiket.com");
            driverTools.deeplink(homeDeeplink);
            driverTools.switchToNative();
        }

        //check if give rating popup appear
        driverTools.waitForElementToAppear(CLOSE_GIVE_RATING_POPUP).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(UPCOMING_BOOKING_CARD_LIST_BOTTOMSHEET_CLOSE_BUTTON).ifPresent(driverTools::click);

        for(int i=0; i<10 && !waitForHomePageToLoad(); i++) {
            logger.log("Waited for homepage to load, retrying " + (i+1) + " times.");
        }
        coachMarkHandling();
    }

    /**
     * Completes the fresh-install onboarding on iOS (regional page → notification permission prompts →
     * onboarding carousel → ticket popup) WITHOUT terminating/re-launching the app or forcing a home
     * deeplink, so post-onboarding surfaces such as the Welcome Rewards bottom sheet are preserved.
     * Mirrors the onboarding portion of {@link #gotoHome()}.
     */
    public void completeFreshInstallOnboarding() {
        driverTools.switchToNative();

        // Advance from the regional "start your journey" screen (where the currency was just changed) by
        // tapping its Continue button. This is best-effort: if it is not present we may already have moved
        // past the regional page, so we must NOT abort here (otherwise the onboarding Continue and the
        // carousel-skip steps below would be skipped, leaving onboarding incomplete). Mirrors Android's
        // finishOnboardingBySkipping(), which always proceeds to the onboarding Continue + skip steps.
        if (driverTools.waitForElementToAppear(CONTINUE_BUTTON_FIRST_PAGE, 20).isPresent()) {
            driverTools.clickElementByCoordinate(CONTINUE_BUTTON_FIRST_PAGE);
        } else {
            logger.log("Onboarding regional 'Continue' not found; assuming already past the regional page and continuing with the skip steps");
        }

        if (driverTools.waitForElementToBeClickable(CONTINUE_BUTTON, 20).isPresent()) {
            driverTools.clickElementByCoordinate(CONTINUE_BUTTON);
        }

        Map<String, Object> caps = driverTools.getCapabilities();
        boolean autoDismissAlerts = caps.get("autoDismissAlerts") != null && (boolean) caps.get("autoDismissAlerts");
        boolean isRunningOnDeviceFarm = CapabilitiesHandler.isRunningOnDeviceFarm();
        logger.log("autoDismissAlerts: " + autoDismissAlerts + ", isRunningOnDeviceFarm: " + isRunningOnDeviceFarm);

        dismissOnboardingPermissionAlerts(autoDismissAlerts, isRunningOnDeviceFarm);

        if (driverTools.waitForElementToBeClickable(CONTINUE_BUTTON, 3).isPresent()) {
            driverTools.clickElementByCoordinate(CONTINUE_BUTTON);
        }

        dismissOnboardingPermissionAlerts(autoDismissAlerts, isRunningOnDeviceFarm);

        if (driverTools.waitForElementToAppear(SKIP_ONBOARDING_SECOND_PAGE, 5).isPresent()) {
            driverTools.clickElementByCoordinate(SKIP_ONBOARDING_SECOND_PAGE);
        }

        // NOTE: unlike gotoHome(), we intentionally do NOT dismiss CANCEL_TICKET_POPUP here. On iOS its
        // locator ("ic cancel grey") is identical to WELCOME_REWARDS_CLOSE, so dismissing it would close
        // the very Welcome Rewards sheet this flow needs to verify. Any blocking popup is instead handled
        // by isWelcomeRewardsShownWithFallback() (promo-popup dismissal + deeplink fallback).
        //
        // The onboarding-complete session flag is set by the caller (OnboardingPageIOS.finishOnboardingBySkipping)
        // based on whether the onboarding screen is actually gone, which is reliable even when the Welcome
        // Rewards sheet overlays home.
    }

    @Override
    public void clearAppDataAndRelaunch() {
        throw new UnsupportedOperationException("clearAppDataAndRelaunch is Android-only; not supported on iOS");
    }

    @Override
    public void markOnboardingComplete() {
        initFlag(session.sessionId(), true);
    }

    private void dismissOnboardingPermissionAlerts(boolean autoDismissAlerts, boolean isRunningOnDeviceFarm) {
        if (autoDismissAlerts && isRunningOnDeviceFarm) {
            return;
        }
        if (driverTools.waitForElementToPresence(ALERT_ALLOW).isPresent()) {
            closeAlerts(ALERT_ALLOW);
            closeAlerts(ALERT_ALLOW);
        } else {
            closeAlerts(ALERT_UNALLOW);
            closeAlerts(ALERT_UNALLOW);
        }
    }

    @Override
    public void goToHomeDeeplink() {
        driverTools.switchToNative();
        driverTools.closeApp();
        Deeplink homeDeeplink = new Deeplink("Home",
                "tiket://",
                "tiket://",
                "tiket://");
        driverTools.deeplink(homeDeeplink);
        if (driverTools.isElementDisplayed(CONTINUE_BUTTON_FIRST_PAGE)) driverTools.click(CONTINUE_BUTTON_FIRST_PAGE);
        coachMarkHandling();
    }

    @Override
    public void goToHomeUsingDeeplinkWithoutCondition(){
        Deeplink homeDeeplink = new Deeplink("Home",
                "tiket://",
                "tiket://",
                "tiket://");
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

    private static Locator recentSearchTitle(String locationEn, String locationId) {
        return new Locator("RECENT_SEARCH_TITLE", AppiumBy.xpath("//*[@name='tds ic chevron right']//preceding-sibling::XCUIElementTypeOther//XCUIElementTypeStaticText[contains(@label,'"+locationEn+"') or contains(@label,'"+locationId+"')]"));
    }

    private static Locator recentSearchAttributeDate(String attributeName, String otherAttribute) {
        return new Locator("RECENT_SEARCH_ATTRIBUTE_DATE", AppiumBy.xpath("//*[contains(@label,'•') or @name='tds ic chevron right']//following-sibling::XCUIElementTypeOther//XCUIElementTypeOther//XCUIElementTypeStaticText[contains(@label,'"+attributeName+"') and contains(@label,'"+otherAttribute+"')]"));
    }

    private static Locator recentSearchAttributeLocation(String attributeName) {
        return new Locator("RECENT_SEARCH_ATTRIBUTE_LOCATION", AppiumBy.xpath("//*[contains(@label,'•') or @name='tds ic chevron right']//following::XCUIElementTypeStaticText[contains(@label,'"+attributeName+"')]"));
    }

    @Override
    public boolean checkRecentSearch(boolean location, String recentSearchCardtitle, String recentSearchAttribute) {
        driverTools.waitForElementToAppear(BOTTOM_MENU_ICONS);
        driverTools.waitForElementToAppear(SEARCH_ICON);

        String locationEn;
        String locationId;
        if (recentSearchCardtitle.contains("/")) {
            locationEn = splitLanguageData(recentSearchCardtitle, "/")[0];
            locationId = splitLanguageData(recentSearchCardtitle, "/")[1];
        } else {
            locationEn = recentSearchCardtitle;
            locationId = recentSearchCardtitle;
        }

        Assertion.assertThat("recent search title card: \""+recentSearchCardtitle+"\" should be displayed on the recent search section", driverTools.swipeToPresentedElement(recentSearchTitle(locationEn, locationId), Swipe.UP, 3).isPresent(), is(true));

        if (recentSearchAttribute.contains(" - ")) {
            String checkInDate = recentSearchAttribute.split(" - ")[0];
            String checkOutDate = recentSearchAttribute.split(" - ")[1];
            Assertion.assertThat("recent attribute: \""+recentSearchAttribute+"\" should be displayed on the recent search section", driverTools.swipeToElement(recentSearchAttributeDate(checkInDate, checkOutDate), Swipe.UP, 3).isPresent(), is(true));
        } else {
            driverTools.waitForElementToPresence(recentSearchAttributeLocation(recentSearchAttribute));
            Assertion.assertThat("recent attribute: \""+recentSearchAttribute+"\" should be displayed on the recent search section", driverTools.swipeToElement(recentSearchAttributeLocation(recentSearchAttribute), Swipe.UP, 3).isPresent(), is(true));
        }
        return true;
    }

    private static Locator recentSearchChip(String chipTitle) {
        return new Locator("RECENT_SEARCH_CHIP", AppiumBy.xpath("//*[@name='page_module_title' and (contains(@label,'Pencarian Terakhir') or contains(@label,'Recently search') or contains(@label,'Recently Search'))]//following::XCUIElementTypeStaticText[@label='"+chipTitle+"' and @visible='true']"));
    }

    @Override
    public boolean checkTotalAllGuestRecentSearch(int totalAllGuest) {
        return false; //FIXME: Pending implementation
    }

    @Override
    public void goToRecentSearch(String chipTitle) {
        driverTools.swipeDown();
        if (driverTools.isElementDisplayed(RECENT_SEARCH_SECTION)) driverTools.click(RECENT_SEARCH_SECTION);
        driverTools.swipeToElement(RECENT_SEARCH_CONTAINER, Swipe.UP, 3);
        if (driverTools.isElementDisplayed(RECENT_SEARCH_SECTION)) driverTools.click(RECENT_SEARCH_SECTION);

        Locator chipLocator;
        if (chipTitle.equalsIgnoreCase("homes")) {
            chipLocator = HOMES_RECENT_SEARCH_CHIP;
        } else {
            chipLocator = recentSearchChip(chipTitle);
        }

        if (driverTools.swipeToElement(chipLocator, RECENT_SEARCH_CONTAINER, Swipe.DOWN, 1).isPresent()) {
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

    private void closeAlerts(Locator alert){
        if(driverTools.waitForElementToAppear(alert,5).isPresent()){
            try {
                driverTools.waitForElementToAppear(alert, 5).ifPresent(driverTools::clickElementByCoordinate);
            }
            catch(Exception e){

            };
            driverTools.waitForElementToDisappear(alert);
        }
    }

    @Override
    public Boolean waitForHomePageToLoad() {
        return driverTools.isElementDisplayed(BOTTOM_MENU_ICONS) && (driverTools.waitForElementToAppear(SEARCH_ICON,6).isPresent() || driverTools.waitForElementToAppear(SEARCH_ICON_OLD,6).isPresent());
    }

    @Override
    public void openAccount() {
        if(driverTools.isElementDisplayed(ACCOUNT_ICON)) {
            driverTools.clickElementByCoordinate(ACCOUNT_ICON);
        }else{
            logger.log("Account icon not found, opening account page using deeplink");
            AccountPage<T> accountPage = AccountPage.getInstance(session);
            accountPage.openAccountPageUsingDeeplink();
        }
        driverTools.swipeToElement(FLAG_TOP_OF_ACCOUNT_PAGE,Swipe.DOWN, 3);
    }

    @Override
    public void closePromoPopup() {
        if (driverTools.isElementPresent(PROMO_POPUP))
            driverTools.click(PROMO_POPUP);
    }

    @Override
    public void closeCurrencyCountryPopup() {
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
        if (driverTools.waitForElementToPresence(CURRENCY_COUNTRY_POPUP_TITLE, 8).isPresent()) {
            return true;
        }
        if (driverTools.waitForElementToPresence(CURRENCY_COUNTRY_CONTINUE_BUTTON, 3).isPresent()) {
            return true;
        }
        return driverTools.waitForElementToPresence(CURRENCY_COUNTRY_KEEP_EXISTING_BUTTON, 3).isPresent();
    }

    @Override
    public void clickCurrencyCountryPopupPrimaryButton() {
        if (driverTools.waitForElementToPresence(CURRENCY_COUNTRY_CONTINUE_BUTTON, 10).isEmpty()) {
            throw new NoSuchElementException("Currency/country popup continue button not found");
        }
        driverTools.clickElementByCoordinate(CURRENCY_COUNTRY_CONTINUE_BUTTON);
        driverTools.waitForElementToAbsence(CURRENCY_COUNTRY_CONTINUE_BUTTON, 15);
    }

    @Override
    public void closeGiveRatingPopup() {
        driverTools.waitForElementToAppear(CLOSE_GIVE_RATING_POPUP).ifPresent(closeBtn -> driverTools.click(closeBtn));
    }

    private Locator getVerticalLocator(String verticalName) {
        return new Locator("HOME_PRODUCT_ICON", AppiumBy.xpath(
                switch (verticalName) {
                    case "Flight", "Flights", "Pesawat" ->
                            "//*[@label='Flights' or @label='Pesawat']";
                    case "Hotels", "Hotel" ->
                            "//*[@label='Hotel' or @label='Hotels']";
                    case "To Dos", "To Do" ->
                            "//*[@label='To Dos']";
                    case "Airport Pickups", "Jemputan Bandara" ->
                            "//*[@name='animationView' and ./following-sibling::*[@label='Airport Pickup' or @label='Jemputan Bandara' or @label='Airport Transfers']]";
                    case "Trains", "Train", "Kereta Api", "Kereta" ->
                            "//*[@label='Train' or @label='Trains' or @label='Kereta']";
                    case "Car Rentals", "Sewa Mobil" ->
                            "//*[@label='Car Rentals' or @label='Sewa Mobil']";
                    default ->
                            "//*[@label='" + verticalName + "']";
                }
        ));
    }

    private Locator getVerticalLocatorPopUp(String verticalName) {
        String labelMatch = switch (verticalName) {
            case "Flight", "Flights", "Pesawat" -> "@label='Flights' or @label='Pesawat'";
            case "Hotels", "Hotel" -> "@label='Hotel' or @label='Hotels'";
            case "To Dos", "To Do" -> "@label='To Dos'";
            case "Airport Pickups", "Jemputan Bandara" -> "contains(@label,'Airport Pickup') or contains(@label,'Jemputan Bandara') or contains(@label,'Airport Transfer')";
            case "Trains", "Train", "Kereta Api", "Kereta" -> "contains(@label,'Train') or contains(@label,'Kereta')";
            case "Car Rentals", "Sewa Mobil" -> "@label='Car Rentals' or @label='Sewa Mobil'";
            default -> "@label='" + verticalName + "'";
        };
        return new Locator("HOME_PRODUCT_ICON_POPUP", AppiumBy.xpath("//*[@name='verticalItemView']//*[" + labelMatch + " and @visible='true']"));
    }

    private void returnToHomeFromVertical() {
        driverTools.switchToNative();
        if (isOnHomePage(5)) {
            closeGiveRatingPopup();
            return;
        }
        for (int attempt = 0; attempt < 3 && !isOnHomePage(3); attempt++) {
            driverTools.waitForElementToPresence(BACK_BUTTON, 2)
                    .ifPresent(driverTools::clickElementByCoordinate);
            driverTools.waitForElementToPresence(HOME, 2)
                    .ifPresent(driverTools::clickElementByCoordinate);
            driverTools.back();
        }
        if (!isOnHomePage(10)) {
            goToHomeDeeplink();
            waitForHomePageToLoad();
        }
        closeGiveRatingPopup();
    }

    private void clickHomeProduct(String vertical) {
        if (!isOnHomePage(3)) {
            goToHomeDeeplink();
            waitForHomePageToLoad();
        }
        closeGiveRatingPopup();
        driverTools.swipeToElement(FINANCE_BAR, Swipe.DOWN, 5).orElseThrow();
        Locator homeProductIcon = this.getVerticalLocator(vertical);

        Optional<WebElement> visibleIcon = driverTools.swipeToPresentedElement(homeProductIcon, Swipe.DOWN, 5);
        if (visibleIcon.isEmpty()) {
            visibleIcon = driverTools.swipeToPresentedElement(homeProductIcon, Swipe.UP, 3);
        }
        if (visibleIcon.isPresent()) {
            driverTools.clickElementByCoordinate(visibleIcon.get());
            return;
        }

        driverTools.swipeToElement(SEE_ALL_PRODUCT, Swipe.DOWN, 5);
        driverTools.waitForElementToPresence(SEE_ALL_PRODUCT, 10)
                .ifPresent(seeAll -> driverTools.clickElementByCoordinate(seeAll));
        homeProductIcon = this.getVerticalLocatorPopUp(vertical);
        Optional<WebElement> popupIcon = driverTools.swipeByDistanceToElement(homeProductIcon, ALL_PRODUCT_CONTAINER, Swipe.UP, 0.7, 5);
        if (popupIcon.isEmpty()) {
            popupIcon = driverTools.swipeToPresentedElement(homeProductIcon, Swipe.UP, 5);
        }
        driverTools.clickElementByCoordinate(popupIcon.orElseThrow());
    }

    private boolean checkHomeProduct(String verticalName){
        final Locator HOME_PRODUCT_ICON = this.getVerticalLocator(verticalName);
        driverTools.waitForElementToAppear(HOME_PRODUCT_ICON);
        if(!driverTools.isElementDisplayed(HOME_PRODUCT_ICON)) driverTools.click(SEE_ALL_PRODUCT);
        boolean isProductDisplayed = driverTools.isElementDisplayed(HOME_PRODUCT_ICON);
        logger.log("is product displayed : " + isProductDisplayed);
        return isProductDisplayed;
    }

    private String convertVerticalNameIfRequired(String verticalName){
        if (isSessionInBHS()) {
            logger.log("session is in bhs");
            verticalName = this.translateVerticalNameToBhs(verticalName);
        }
        return verticalName;
    }

    @Override
    public void openVerticalPage(String vertical) {
        if (!isOnHomePage(3)) {
            returnToHomeFromVertical();
        }
        if (driverTools.isElementDisplayed(CONTINUE_AS_GUEST)) driverTools.click(CONTINUE_AS_GUEST);
        String verticalName = convertVerticalNameIfRequired(vertical);
        clickHomeProduct(verticalName);
    }

    @Override
    public boolean checkVerticalPresence(String vertical){
        String verticalName = convertVerticalNameIfRequired(vertical);
        return checkHomeProduct(verticalName);
    }

    private void backFromVerticalToHome(String vertical) {
        switch (vertical) {
            case "Flights" -> FlightSearchPage.getInstance(session).backToHomePage();
            case "Hotels" -> LandingPage.getInstance(session).backToHomePage();
            case "Trains" -> TrainSearchPage.getInstance(session).backToHomePage();
            case "To Dos" -> TTDLandingPage.getInstance(session).backToHomePage();
            case "Airport Pickups" -> ((CarTransferSearchPageIOS<T>) CarTransferSearchPage.getInstance(session)).backToHomePage();
            case "Car Rentals" -> SearchPageCarRental.getInstance(session).backToHomePage();
            default -> driverTools.back();
        }
        if (!isOnHomePage(5)) {
            returnToHomeFromVertical();
        }
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
        backFromVerticalToHome("Flights");

        this.openVerticalPage("Hotels");
        boolean isHotelsVerticalPageOpen = this.isVerticalPageOpen("Hotels");
        backFromVerticalToHome("Hotels");

        this.openVerticalPage("Trains");
        boolean isTrainVerticalPageOpen = this.isVerticalPageOpen("Trains");
        trainSearchPage.skipPopup();
        backFromVerticalToHome("Trains");

        this.openVerticalPage("To Dos");
        boolean isToDosVerticalPageOpen = this.isVerticalPageOpen("To Dos");
        backFromVerticalToHome("To Dos");

        this.openVerticalPage("Airport Pickups");
        airportTransferSearchPage.allowLocationAccess();
        airportTransferSearchPage.closeInformationPopUp();
        driverTools.switchToNative();
        boolean isAirportTransferVerticalPageOpen = this.isVerticalPageOpen("Airport Pickups");
        backFromVerticalToHome("Airport Pickups");

        this.openVerticalPage("Car Rentals");
        carRentalSearchPage.closeInformationPopUp();
        boolean isCarRentalsVerticalPageOpen = this.isVerticalPageOpen("Car Rentals");
        backFromVerticalToHome("Car Rentals");

        boolean isProductListHomePageSwipeable = this.isProductListOnHomePageSwipelable();

        logger.log("is flight vertical open -> " + isFlightVerticalPageOpen);
        logger.log("is hotel vertical open -> " + isHotelsVerticalPageOpen);
        logger.log("is to do vertical open -> " + isToDosVerticalPageOpen);
        logger.log("is airport transfer vertical open -> " + isAirportTransferVerticalPageOpen);
        logger.log("is car rental vertical open -> " + isCarRentalsVerticalPageOpen);
        logger.log("is train vertical open -> " + isTrainVerticalPageOpen);
        logger.log("is product list swipeable -> " + isProductListHomePageSwipeable);

        return isFlightVerticalPageOpen && isHotelsVerticalPageOpen && isToDosVerticalPageOpen
                && isAirportTransferVerticalPageOpen && isCarRentalsVerticalPageOpen && isTrainVerticalPageOpen
                && isProductListHomePageSwipeable;
    }

    @Override
    public boolean isOnHomePage(){
        return waitForHomePageToLoad();
    }

    @Override
    public boolean isOnHomePage(int duration){
        if (driverTools.waitForElementToPresence(TTD_LANDING_SEARCH_BAR, 2).isPresent()) {
            return false;
        }
        boolean hasHomeSearch = driverTools.waitForElementToPresence(SEARCH_ICON, duration).isPresent()
                || driverTools.waitForElementToPresence(SEARCH_ICON_OLD, duration).isPresent();
        return hasHomeSearch
                || driverTools.isElementDisplayed(HERO_BANNER)
                || driverTools.isElementDisplayed(FINANCE_BAR);
    }

    @Override
    public boolean isUserLogin(){
        if(driverTools.waitForElementToPresence(SEE_ALL_PRODUCT,3).isPresent() && driverTools.waitForElementToPresence(ACCOUNT_ICON_TEXT_LOGIN,3).isPresent()){
            //faster check if inbox icon is present
            return true;
        }
        if(!driverTools.isElementVisible(SEE_ALL_PRODUCT)){
            driverTools.swipeToElement(PROMO_ICON, Swipe.DOWN, 3);
        }
        return driverTools.waitForElementToPresence(ACCOUNT_ICON_TEXT_LOGIN,4).isPresent();
    }

    @Override
    public boolean isInboxListOpen(){
        Boolean isInboxHeaderPresent = driverTools.waitForElementToPresence(INBOX_HEADER,15).isPresent();
        logger.log("is inbox header present -> " + isInboxHeaderPresent);
        return isInboxHeaderPresent;
    }

    @Override
    public void clickInboxMessage(String message){
        final Locator messageElement = this.getMessageLocator(message);
        WebElement e = driverTools.swipeToPresentedElement(messageElement,Swipe.UP,10).orElseThrow();
        driverTools.click(e);
        driverTools.waitForElementToDisappear(messageElement,3);
    }

    @Override
    public void clickInboxIcon(){
        closeGiveRatingPopup();
        driverTools.clickWhenPresent(INBOX, 15);
        if (!driverTools.waitForElementToPresence(INBOX_HEADER, 10).isPresent()) {
            driverTools.clickWhenPresent(INBOX, 15);
            driverTools.waitForElementToPresence(INBOX_HEADER, 15).orElseThrow();
        }
    }

    @Override
    public boolean isFlashSaleInventoryPricePresent(String currency){
        driverTools.swipeToElement(getFlashSaleInventoryPrice(currency),Swipe.UP, 2);
        Optional<WebElement> e = driverTools.waitForElementToPresence(getFlashSaleInventoryPrice(currency),10);
        return e.isPresent();
    };

    public Locator getFlashSaleInventoryPrice(String currency){
        return new Locator("FLASH_SALE_INVENTORY_PRICE", AppiumBy.xpath("//*[contains(@label,'"+currency+"') and @type='XCUIElementTypeStaticText']"));
    }

    @Override
    public boolean isInboxDetailHasSubtitle(){
        return driverTools.waitForElementToAppear(INBOX_BOTTOM_SHEET,20).isPresent();
    }

    @Override
    public boolean isFlashListSaleSwipeAble(String flashSaleInventoryName){ //Argument not used in ios
        driverTools.waitForElementToAppear(INVENTORY_NAME);
        String hotelInventoryNameBefore = driverTools.getText(INVENTORY_NAME).orElseThrow();
        logger.log("Inventory name before swipe -> " + hotelInventoryNameBefore);
        driverTools.swipeLeft(INVENTORY_NAME);
        driverTools.swipeLeft(INVENTORY_NAME);
        String hotelInventoryNameAfter = driverTools.element(INVENTORY_NAME).orElseThrow().getText();
        logger.log("Inventory name after swipe -> " + hotelInventoryNameAfter);
        return !Objects.equals(hotelInventoryNameBefore, hotelInventoryNameAfter);
    }

    @Override
    public boolean isFlashSaleCountDownPresent(){
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
    public void verifyInboxIcon(){} // FIXME: Implementation pending
    @Override
    public String clickOneOfFlashSaleInventory(){
        driverTools.swipeToElement(INVENTORY_NAME,Swipe.UP, 1);
        String flashSaleInventoryName = driverTools.getText(INVENTORY_NAME).orElseThrow();
        driverTools.click(INVENTORY_NAME);
        return flashSaleInventoryName;
    }

    @Override
    public void clickSeeAllFlashSale(String flashSaleTitle){
        driverTools.swipeUp(1);
        WebElement flashSaleSeeAllBtn = driverTools.swipeToElement(FLASH_SALE_SEE_ALL,Swipe.UP, 1).orElseThrow();
        driverTools.click(flashSaleSeeAllBtn);
    }

    @Override
    public boolean verifyRedirectFlashSale(String flashSaleInveentoryName){
        Locator hotelName = this.getHotelInventoryLocator(flashSaleInveentoryName);
        Boolean isReidrectToHotelDetail = driverTools.waitForElementToAppear(hotelName,20).isPresent();
        return isReidrectToHotelDetail;
    }

    private Locator getHotelInventoryLocator(String hotelName){
        return new Locator("HOTEL_NAME", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'"+hotelName+"')]"));
    }
    @Override
    public void swipeToCard(String cardType) {
        logger.log("Swiping to card with title: '" + cardType + "'");
        clickHomeIcon();
        clickHomeIcon();
        driverTools.swipeToElement(FINANCE_BAR, Swipe.DOWN, 10).orElseThrow();
        final int maxSwipeAttempts = 100;
        boolean found = false;
        boolean bottom;


        for(int i=0; i<maxSwipeAttempts; i++) {
            found = driverTools.isElementDisplayed(getCardLocator(cardType));
            bottom = driverTools.isElementDisplayed(UP_ARROW);
            if(!found && !bottom) {
                driverTools.swipeUp();
            } else {
                break;
            }
        }
        if(!found) {
            throw new RuntimeException("Card with title '" + cardType + "' not found after " + maxSwipeAttempts + " swipes.");
        }
        driverTools.swipeElementToMidScreen(getCardLocator(cardType));
    }

    @Override
    public void goToSection(String sectionTitle) {
        driverTools.swipeByDistanceToElement(getCardLocator(sectionTitle), SCREEN,Swipe.UP,0.6,40).orElseThrow();
        driverTools.swipeByDistance(SCREEN,Swipe.UP,0.05);
    }

    private Locator getCampaignLabelProduct(String productName) {
        if (productName.contains("/")) {
            String productEn = productName.split("/")[0];
            String productId = productName.split("/")[1];
            return new Locator("CAMPAIGN_LABEL_PRODUCT", AppiumBy.xpath("//*[(contains(@label,'"+productEn+"') or contains(@label,'"+productId+"')) and @name='product_name']//parent::*//parent::*//parent::*//parent::*//parent::*//parent::*//following-sibling::*//*[@name='page_module_product_card_campaign_label']"));
        } else {
            return new Locator("CAMPAIGN_LABEL_PRODUCT", AppiumBy.xpath("//*[contains(@label,'"+productName+"') and @name='product_name']//parent::*//parent::*//parent::*//parent::*//parent::*//parent::*//following-sibling::*//*[@name='page_module_product_card_campaign_label']"));
        }
    }

    @Override
    public String getCampaignProduct(String productName) {
        Locator campaignLabel = getCampaignLabelProduct(productName);
        driverTools.swipeToElement(campaignLabel, Swipe.UP, 3);
        return driverTools.getText(campaignLabel).orElseThrow();
    }

    private Locator dynamicElement(String titleSection, String locatorType) {
        return new Locator("HOME_SECTION", AppiumBy.xpath("//*[@label='" + titleSection + "']//following::" + locatorType + "[not(@label='Lihat Semua' or @label='See All')][1]"));
    }

    @Override
    public String getTheSubtitle(String titleSection) {
        return driverTools.getText(dynamicElement(titleSection, "XCUIElementTypeStaticText")).orElseThrow();
    }

    private Locator selectedChipsButton(String titleSection, String chipText) {
        if (chipText.equals("")) {
            return new Locator("CHIPS_BUTTON", AppiumBy.xpath("//*[@label='" + titleSection + "']//following::XCUIElementTypeScrollView[1]//XCUIElementTypeStaticText[@visible='true']"));
        } else {
            return new Locator("CHIPS_BUTTON", AppiumBy.xpath("//*[@label='" + titleSection + "']//following::XCUIElementTypeScrollView[last()]//XCUIElementTypeStaticText[contains(@label,'" + chipText + "') and @visible='true']"));
        }
    }

    private Locator allChipsButton(String titleSection) {
        return new Locator("ALL_CHIPS_BUTTON", AppiumBy.xpath("//*[@label='" + titleSection + "']//following::XCUIElementTypeCollectionView[1]//preceding::XCUIElementTypeScrollView[last()]//XCUIElementTypeStaticText"));
    }

    @Override
    public boolean swipeChipsButton(String titleSection) {
        boolean swipeStatus;
        List<WebElement> chipsButton = driverTools.elements(allChipsButton(titleSection)).orElseThrow();
        if (chipsButton.size() > 3) {
            driverTools.swipeByDistance(dynamicElement(titleSection, "XCUIElementTypeScrollView"), Swipe.LEFT, 1);
            swipeStatus = true;
        } else {
            swipeStatus = false;
        }
        return swipeStatus;
    }

    private Locator selectedInventory(String titleSection) {
        return new Locator("INVENTORY", AppiumBy.xpath("//*[@label='" + titleSection + "']//following::XCUIElementTypeCollectionView[1]//*[(@name='product_name' or contains(@label,'→')) and @visible='true']"));
    }

    @Override
    public boolean compareBeforeAfterSwipe(String titleSection, String item, String inventoryName, boolean isSwipe) {
        boolean status;
        if (isSwipe) {
            switch (item.toLowerCase()) {
                case "chips" -> status = driverTools.element(selectedChipsButton(titleSection, "selected")).isEmpty();
                case "inventory" -> {
                    String currentInventoryName = driverTools.getText(selectedInventory(titleSection)).orElseThrow();
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
            List<WebElement> chipButtonItem = driverTools.elements(selectedChipsButton(titleSection, "")).orElseThrow();
            int randomItem = generateRandomInt(0, chipButtonItem.size() - 1);
            logger.log("index of randomItem -> " + randomItem);
            chipButtonText = driverTools.getText(chipButtonItem.get(randomItem));
            driverTools.click(chipButtonItem.get(randomItem));
        } else {
            chipButtonText = driverTools.getText(selectedChipsButton(titleSection, chipsText)).orElseThrow();
            driverTools.click(selectedChipsButton(titleSection, chipsText));
        }
        return chipButtonText;
    }

    @Override
    public String getCtaButtonTextApp(String titleSection) {
        return driverTools.getText(dynamicElement(titleSection, "XCUIElementTypeButton")).orElseThrow();
    }

    @Override
    public String getInventoryName(String titleSection) {
        driverTools.swipeByDistance(SCREEN,Swipe.UP,0.5);
        String result;
        boolean inventoryName = driverTools.element(INVENTORY_NAME).isPresent();
        if (inventoryName) {
            result = driverTools.getText(INVENTORY_NAME).orElseThrow();
        } else {
            result = driverTools.getText(FLIGHT_INVENTORY_NAME).orElseThrow();
        }
        return result;
    }

    // TODO: iOS implementation to be added later — the accommodation recommendation currency-switch
    //  case (testVerifyAccommodationRecommendationPriceOnCurrencySwitch) is Android-only for now.
    @Override
    public String getSectionInventoryName(String titleSection) {
        throw new UnsupportedOperationException("getSectionInventoryName is not implemented on iOS yet");
    }

    @Override
    public String getInventoryPrice(String titleSection, String inventoryName) {
        throw new UnsupportedOperationException("getInventoryPrice is not implemented on iOS yet");
    }

    @Override
    public boolean swipeInventoryList(String titleSection) {
        boolean swipeStatus;
        List<WebElement> inventoryList = driverTools.elements(ALL_INVENTORY_NAME).orElseThrow();
        if (inventoryList.size() > 2) {
            driverTools.swipeByDistance(dynamicElement(titleSection, "XCUIElementTypeCollectionView"), Swipe.LEFT, 1);
            swipeStatus = true;
        } else {
            swipeStatus = false;
        }
        return swipeStatus;
    }

    private Locator inventoryGrid(String titleSection) {
        return new Locator("INVENTORY_GRID", AppiumBy.xpath("//*[@label='" + titleSection + "']//following::XCUIElementTypeImage[@name='bn_grid']"));
    }

    @Override
    public String selectInventory(String titleSection, String inventoryList, String productType) {
        List<WebElement> inventoryItem = switch (productType.toUpperCase()) {
            case "TIXHOTEL", "TODO" -> driverTools.elements(INVENTORY_NAME).orElseThrow();
            case "FLIGHT" -> driverTools.elements(FLIGHT_INVENTORY_NAME).orElseThrow();
            case "WEBVIEW" -> driverTools.elements(inventoryGrid(titleSection)).orElseThrow();
            default -> throw new RuntimeException("invalid productType -> " + productType);
        };
        int indexItem;
        if (inventoryList.equals("random")) {
            indexItem = generateRandomInt(0, Objects.requireNonNull(inventoryItem).size() - 1);
        } else {
            indexItem = Integer.parseInt(inventoryList);
        }
        logger.log("index of indexItem -> " + indexItem);
        String inventoryName = driverTools.getText(Objects.requireNonNull(inventoryItem).get(indexItem));
        if (productType.equalsIgnoreCase("WEBVIEW")) inventoryName = String.valueOf(indexItem);
        driverTools.click(inventoryItem.get(indexItem));
        return inventoryName;
    }

    @Override
    public void clickCtaButton(String titleSection) {
        driverTools.click(dynamicElement(titleSection, "XCUIElementTypeButton"));
    }

    @Override
    public void clickMemberCompleteNow() {
        //FIXME: Pending implementation
    }

    @Override
    public void clickCard(String cardType){} // FIXME: Implementation pending

    @Override
    public void openMyOrderPage(){
        closeWelcomeRewards();
        driverTools.waitForElementToAppear(CLOSE_GIVE_RATING_POPUP).ifPresent(driverTools::click);
        driverTools.click(MY_ORDER);
    }

    @Override
    public void clickOnRewardButton() {
        waitForHomePageToLoad();
        driverTools.waitForElementToPresence(CLOSE_IMAGE, 10).ifPresent(driverTools::click);
        driverTools.waitForElementToPresence(REWARDS_BOTTOM, 30);
        driverTools.clickElementByCoordinate(REWARDS_BOTTOM);
    }

    @Override
    public void clickLoginButton() {
        driverTools.waitForElementToPresence(LOGIN_BUTTON,30).orElseThrow();
        driverTools.clickElementByCoordinate(LOGIN_BUTTON);

        driverTools.waitForElementToPresence(LOGIN_REGISTER_BUTTON,30).orElseThrow();
        driverTools.clickElementByCoordinate(LOGIN_REGISTER_BUTTON);
    }

    public Locator getCardLocator(String cardName) {
        String feedScope = HOME_FEED_XPATH;
        String titleNode = "@type='XCUIElementTypeStaticText'";
        if (cardName.contains("/") && !cardName.contains("//")) {
            String en = splitLanguageData(cardName, "/")[0];
            String id = splitLanguageData(cardName, "/")[1];
            return new Locator("CARD_LOCATOR", AppiumBy.xpath(feedScope + "//*[" + titleNode
                    + " and (contains(@label,\"" + en + "\") or contains(@name,\"" + en + "\") or contains(@value,\"" + en + "\") or contains(@label,\"" + id + "\") or contains(@name,\"" + id + "\") or contains(@value,\"" + id + "\"))]"));
        }
        return new Locator("CARD_LOCATOR", AppiumBy.xpath(feedScope + "//*[" + titleNode
                + " and (contains(@label,\"" + cardName + "\") or contains(@name,\"" + cardName + "\") or contains(@value,\"" + cardName + "\"))]"));
    }

    /**
     * Enables or disables the location service.
     * ON uses the hotel Near Me flow. OFF toggles location off from Account settings.
     *
     * @param action {@code on} to enable or {@code off} to disable
     */
    @Override
    public void setLocationService(String action) {
        HomePage<T> homePage = HomePage.getInstance(session);
        LandingPageIOS<T> hotelLandingPage = new LandingPageIOS<>(session);
        SearchResultPageIOS<T> hotelSearchResultPage = new SearchResultPageIOS<>(session);

        if(action.equalsIgnoreCase("on")) {
            homePage.openVerticalPageDeeplink("Hotel", "en", "IDR");
            hotelLandingPage.clickNearMeIcon();
            hotelLandingPage.clickSubmit();

            hotelLandingPage.enableLocation();
            hotelLandingPage.fillNightCheckInForm();
            hotelSearchResultPage.clickBackButton();
            hotelLandingPage.clickBackButton();
        } else if (action.equalsIgnoreCase("off")) {
            SettingPage<T> settingPage = SettingPage.getInstance(session);
            openAccount();
            settingPage.setLocationSwitch("OFF");
            settingPage.backToHome();
        } else {
            logger.log("Parameter "+action+" is not valid");
        }
    }

    private Locator getCurrencyLocator(String currency){
        return new Locator("currency",AppiumBy.xpath("//*[@label='"+currency.toUpperCase()+"']"));
    }

    @Override
    public void clickVideo() {
    } //deprecated

    @Override
    public void clickHeroBanner() {
        driverTools.waitForElementToAppear(HERO_BANNER,5).ifPresentOrElse(driverTools::click, ()-> {throw new NoSuchElementException();});
    }

    public void clickGlobalSearch() {
        driverTools.waitForElementToAppear(HOME_PAGE_GLOBAL_SEARCH,20).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException("Global search bar not found in home page");
        });
        driverTools.hideKeyboard(1);
    }

    private Locator gsInfoLocator(int i){
        return new Locator("GS_INFO", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='gs_info_text'])["+i+"]"));
    }

    @Override
    public boolean verifyGlobalSearchInfo(GSLandingResponse res) {
        driverTools.hideKeyboard(1);
        Boolean isInfoBannerVisible = driverTools.swipeToElement(GS_INFO_BANNER, Swipe.UP, 10).isPresent();
        Assertion.assertThat("Verify info banner appear", isInfoBannerVisible, is(true));

        Boolean gsCloseVisible = driverTools.isElementDisplayed(GS_INFO_CLOSE);
        Assertion.assertThat("Verify close button visible", gsCloseVisible, is(true));

        GSLandingResponse.Data gsInfoData = res.data.stream().filter(e->e.templateCode.equals("GS_INFO")).findFirst().orElseThrow();
        // check gs info banner scrollable
        WebElement gsInfoElement;
        for(int i = 0; i<3;i++){
            gsInfoElement = driverTools.waitForElementToAppear(GS_INFO, 20).orElseThrow();
            Assertion.assertThat("Verify info module text is like from api (Assertion index : "+i+")", gsInfoElement.getText(),
                    containsString(gsInfoData.dynamicContents.get(i).buttonText.replace("!", "")));
            driverTools.swipeByDistance(GS_INFO_BANNER, Swipe.LEFT, 1, 1);
        }
        // check if gs info clickable
        driverTools.click(GS_INFO);
        Boolean isGSBarVisible = driverTools.waitForElementToDisappear(GS_INFO,10);
        Assertion.assertThat("Verify info link clickable,by checking gs bar not visible", !isGSBarVisible, is(false));
        return true;
    }

    private Locator getGSInfoBannerLocator(String sectionTitle){
        return new Locator("GS_INFO_BANNER_COMPONENT", AppiumBy.xpath("(//XCUIElementTypeCell[.//*[@type='XCUIElementTypeStaticText' and (@label='" + sectionTitle + "' or @label='" + sectionTitle.replace("EN", "ID") + "')]]//*[@type='XCUIElementTypeStaticText'])[1]"));
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
        return new Locator("GLOBAL_LOCATOR", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and @name='" + keyword + "']"));
    }

    @Override
    public boolean verifyGlobalSearcWarningMinimunChar(String expectedWarningKeywordId,String expectedWarningKeywordEn,String currentAppLang) {
        WebElement warningKeywordElement = driverTools.waitForElementToAppear(getGSWarningLocator(expectedWarningKeywordId, expectedWarningKeywordEn,currentAppLang)).orElseThrow();
        return driverTools.isElementDisplayed(warningKeywordElement);
    }

    @Override
    public void clickPromoChip() {
        WebElement promoChipElement = driverTools.swipeToElement(PROMO_CHIP, Swipe.DOWN, 10).orElseThrow();
        driverTools.click(promoChipElement);
    }

    @Override
    public boolean verifyLocationBanner() {
        scrollUntilBottom(15);
        return driverTools.isElementDisplayed(LOCATION_BANNER);
    }

    @Override
    public void clickAllowLocationFromBanner() {
        driverTools.click(LOCATION_BANNER);
        driverTools.click(CONTINUE_BUTTON_LOCATION);
        driverTools.click(ALLOW_ACCESS_BUTTON);
    }

    @Override
    public boolean isLocationBannerDisappear() {
        return !driverTools.isElementDisplayed(LOCATION_BANNER);
    }

    @Override
    public void clickCtaButton(String[] sectionTitle) {
        driverTools.swipeToElement(getSectionButton(sectionTitle), SCREEN, Swipe.UP, 3);
        driverTools.click(getSectionButton(sectionTitle));
    }

    @Override
    public boolean isCurrencyOnPageModuleCorrect(String[] targetModuleNames, String currency) {
        boolean result = false;
        for(String moduleTitle : targetModuleNames) {
            swipeToCard(moduleTitle);
            Locator priceLocator = getPriceLocatorOnCTASection(moduleTitle);
            driverTools.swipeToElement(priceLocator, SCREEN, Swipe.UP, 3).orElseThrow();
            List<WebElement> priceElement = driverTools.elements(priceLocator).orElseThrow();

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
        final Locator PRICE_LOCATOR_1 = new Locator("PRICE_LOCATOR_1", AppiumBy.xpath("//XCUIElementTypeCell[.//XCUIElementTypeStaticText[@label='"+moduleTitle+"']]//XCUIElementTypeStaticText[@name='bn_chips_price']"));
        final Locator PRICE_LOCATOR_2 = new Locator("PRICE_LOCATOR_2", AppiumBy.xpath("(//XCUIElementTypeCell[.//XCUIElementTypeStaticText[@label='"+moduleTitle+"']]//XCUIElementTypeCell//XCUIElementTypeOther[last()]//XCUIElementTypeStaticText[contains(@label,'.') or contains(@label,',')])[last()]"));
        return driverTools.waitForElementToPresence(PRICE_LOCATOR_1).isPresent() ? PRICE_LOCATOR_1 : PRICE_LOCATOR_2;
    }

    private Locator getSectionButton(String[] sectionTitle) {
        return new Locator("CTA_BUTTON", AppiumBy.xpath("//*[contains(@label, '" + sectionTitle[0].trim() + "')]//ancestor::*//*[@name='page_module_see_all_button']"));
    }

    @Override
    public void clickActivatePinCard() {
        WebElement activatePinCard = driverTools.swipeToElement(ACTIVATE_PIN,Swipe.UP,20).orElseThrow();
        driverTools.click(activatePinCard);
        if(driverTools.waitForElementToAppear(ALLOW_ACCESS_BUTTON).isPresent()){
            driverTools.click(ALLOW_ACCESS_BUTTON);
            activatePinCard = driverTools.swipeToElement(ACTIVATE_PIN,Swipe.UP,20).orElseThrow();
            driverTools.click(activatePinCard);
        }
    }

    @Override
    public boolean isOnPromoPage() {
        boolean isPromoVoucherDisplayed = driverTools.isElementDisplayed(PROMO_VOUCHER_BOX);
        logger.log("isPromoVoucherDisplayed-->"+isPromoVoucherDisplayed);
        boolean isAppBarScrollable = Boolean.parseBoolean(driverTools.getValueAttribute(PROMO_SCROLL_BAR, "enabled"));
        logger.log("isAppBarScrollable-->"+isAppBarScrollable);

        return isPromoVoucherDisplayed && isAppBarScrollable;
    }

    private Locator chipsLocator(String chipsName) {
        return new Locator("CHIPS_"+chipsName, AppiumBy.xpath("//*[(@label='"+chipsName+"')]"));
    }

    @Override
    public boolean verifyCardDefaultChips(HomePageModule.PageModules pageModules){
        boolean isChipsGroupDisplayed = driverTools.isElementDisplayed(chipsLocator(pageModules.dynamicContents.get(0).chips.get(0).text));
        logger.log("is chips group displayed : " + isChipsGroupDisplayed);

        boolean isCardDisplayed = driverTools.isElementDisplayed(chipsLocator(pageModules.dynamicContents.get(0).chips.get(0).cardDefaults.get(0).contentTitle));
        logger.log("is card displayed : " + isCardDisplayed);

        WebElement seeAllBtn = driverTools.swipeToElement(chipsLocator((pageModules.dynamicContents.get(0).chips.get(0).buttonText)), Swipe.UP, 1).orElseThrow();
        boolean isSeeAllBtnDisplayed = driverTools.isElementDisplayed(seeAllBtn);
        logger.log("is see all btn displayed : " + isSeeAllBtnDisplayed);

        return  isChipsGroupDisplayed && isCardDisplayed && isSeeAllBtnDisplayed;
    }

    @Override
    public boolean VerifyScrollCardDefaultChips(HomePageModule.PageModules pageModules){
        boolean isCardScrollable = Boolean.valueOf(driverTools.isElementDisplayed(DEFAULT_CHIPS_LOCATOR));
        logger.log("is chips scrollable : " + isCardScrollable);

        boolean isSwipe;
        String titleSection = pageModules.moduleDetail.heading.title;
        String firstInventoryName = getInventoryName(titleSection);
        isSwipe = swipeInventoryList(titleSection);

        return compareBeforeAfterSwipe(titleSection, "inventory", firstInventoryName, isSwipe) && isCardScrollable;
    }

    @Override
    public boolean verifyClickCard(HomePageModule.PageModules pageModules){
        String expectedURL = pageModules.dynamicContents.get(0).chips.get(0).cardDefaults.get(0).url;
        String finalURL = getAppUrl();
        return finalURL.contains(CoreFunctions.removeUrlProtocol(expectedURL));
    }

    @Override
    public void clickBackBtn(){
        boolean backButtonNative = driverTools.waitForElementToPresence(BACK_BUTTON).isPresent();
        if (backButtonNative) {
            driverTools.clickElementByCoordinate(BACK_BUTTON);
        } else {
            clickBackBtnWebview();
        }
    }

    @Override
    public boolean clickSeeAllBtn(HomePageModule.PageModules pageModules){
        String cardDefaultChipsTitleApi = pageModules.moduleDetail.heading.title;
        driverTools.click(chipsLocator(cardDefaultChipsTitleApi));
        return isOnHomePage();
    }

    public boolean verifyGlobalSearchVerticalSection(String verticalName) {
        // FIXME implementation pending

        return false;
    }

    @Override
    public boolean verifyGlobalSearchArticleSection(GSSearchPageResponse response) {
        // FIXME implementation pending
        return false;
    }

    @Override
    public void inputSearchKeyword(String keyword) {
        // FIXME implementation pending

    }

    @Override
    public boolean checkRunningTextFinanceBar() {
        ArrayList<String> financeBarList = new ArrayList<>();
        int i=0;
        while(i<15){
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
        driverTools.swipeToElement(FINANCE_BAR, Swipe.DOWN, 5).orElseThrow();
        ArrayList<String> financeBarList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            WebElement financeBar = driverTools.waitForElementToPresence(FINANCE_BAR_TITLE, 10).orElseThrow();
            String financeBarText = driverTools.getText(financeBar);
            if (!financeBarList.contains(financeBarText)) {
                financeBarList.add(financeBarText);
            }
            logger.log("Finance bar text (non-IDR): " + financeBarText);
            if (financeBarList.size() >= 2 && i >= 5) {
                break;
            }
            driverTools.dummyWait(2);
        }
        logger.log("Finance bar list (non-IDR): " + financeBarList);

        Assertion.assertThat("Finance bar should not show Blibli Tiket Rewards for non-IDR",
                financeBarList.stream().noneMatch(text -> text.contains("Blibli Tiket Rewards")), is(true));
        Assertion.assertThat("Finance bar should not show PayLater for non-IDR",
                financeBarList.stream().noneMatch(text -> text.contains("PayLater")), is(true));
        Assertion.assertThat("Finance bar should not show BlibliPay for non-IDR",
                financeBarList.stream().noneMatch(text -> text.contains("BlibliPay")), is(true));

        WebElement financeBarToolTip = driverTools.waitForElementToPresence(TOOLTIP_MESSAGE).orElseThrow();
        String financeBarToolTipText = driverTools.getText(financeBarToolTip);
        String expectedMessage = getNonIdrFinanceBarExpectedMessage(currency, language);
        logger.log("Finance bar running text (non-IDR): " + financeBarToolTipText + ", expected: " + expectedMessage);

        Assertion.assertThat("Finance bar running text should match " + language + " message for " + currency,
                financeBarToolTipText, is(expectedMessage));

        return financeBarList.stream().noneMatch(text -> text.contains("PayLater"))
                && financeBarList.stream().noneMatch(text -> text.contains("BlibliPay"))
                && financeBarList.stream().noneMatch(text -> text.contains("Blibli Tiket Rewards"))
                && financeBarToolTipText.equals(expectedMessage);
    }

    @Override
    public void clickPayLaterOnFinanceBar() {
        driverTools.click(driverTools.element(PAYLATER_DETAIL_TEXT).orElseThrow());
    }

    private Locator loyaltyTierBadge(String tier) {
        return new Locator("LOYALTY_TIER_BADGE_" + tier.toUpperCase().replace(" ", "_"),
                AppiumBy.xpath("//*[contains(@name,'finance_bar')]//*[contains(@label,'" + tier + "')]"));
    }

    @Override
    public boolean areIdrLoginDetailsShown(String tier) {
        boolean isLoyaltyTierShown = driverTools.waitForElementToAppear(loyaltyTierBadge(tier), 10).isPresent();
        boolean isPayLaterShown = driverTools.waitForElementToAppear(PAYLATER_DETAIL_TEXT, 10).isPresent();
        boolean isBlibliPayShown = driverTools.waitForElementToAppear(BLIPAY_DETAIL_TEXT, 10).isPresent();
        logger.log("IDR login details - tier: " + isLoyaltyTierShown + ", PayLater: " + isPayLaterShown + ", BlibliPay: " + isBlibliPayShown);
        return isLoyaltyTierShown && isPayLaterShown && isBlibliPayShown;
    }

    @Override
    public boolean checkLoginTextFinanceBar() {
        WebElement financeBarLoginText = driverTools.waitForElementToPresence(getFinanceBarLocator("Log in"),30).orElseThrow();
        String financeBarLoginTextString = driverTools.getText(financeBarLoginText);
        logger.log("Finance bar login text : " + financeBarLoginTextString);
        Assertion.assertThat("Verify finance bar login text is correct", financeBarLoginTextString, containsString("Log in"));
        return financeBarLoginTextString.toLowerCase().contains("log in");
    }

    private Locator getFinanceBarRunningTextLocator(String sectionTitle) {
        return new Locator("FINANCE_BAR_RUNNING_TEXT", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and @name='finance_bar_heading' and contains(@label,'" + sectionTitle + "')]"));
    }

    private Locator getFinanceBarLocator(String input) {
        return getFinanceBarRunningTextLocator(input);
    }

    private Locator getFinanceBarLocator(String input, String alternative) {
        return new Locator("FINANCE_BAR", AppiumBy.xpath("(//XCUIElementTypeCollectionView/XCUIElementTypeCell)[1]/preceding-sibling::XCUIElementTypeOther[1]//XCUIElementTypeStaticText[contains(@label,'"+input+"') or contains(@label,'"+alternative+"')]"));
    }

    @Override
    public void clickFinanceBar() {
        driverTools.click(FINANCE_BAR);
    }

    @Override
    public void clickFinanceBarRunningText(String sectionTitle) {
        Locator runningTextLocator = getFinanceBarRunningTextLocator(sectionTitle);
        driverTools.swipeToElement(FINANCE_BAR, Swipe.DOWN, 5).orElseThrow();
        for (int attempt = 0; attempt < 15; attempt++) {
            if (driverTools.waitForElementToPresence(runningTextLocator, 3).isPresent()) {
                logger.log("Finance bar running text found: " + sectionTitle);
                driverTools.click(runningTextLocator);
                return;
            }
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
        return driverTools.waitForElementToAppear(PAYLATER_PAGE, 15).isPresent()
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
        return driverTools.isElementDisplayed(FINANCE_BAR_INFO);
    }

    @Override
    public Boolean isToolTipMessageAppears(String currency) {
        WebElement financeBarToolTip = driverTools.waitForElementToPresence(TOOLTIP_MESSAGE).orElseThrow();
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
                Locator bannerCarouselV2ChipLocator = getBannerCarouselV2ChipLocator(pageModuleTitle, "1");
                driverTools.swipeToElement(bannerCarouselV2ChipListLocator, Swipe.UP, 3);

                int initChipX = driverTools.element(bannerCarouselV2ChipLocator).get().getLocation().x;
                driverTools.swipeLeft(bannerCarouselV2ChipLocator,1);
                int swipedChipX = driverTools.element(bannerCarouselV2ChipLocator).get().getLocation().x;

                return driverTools.waitForElementToAppear(bannerCarouselV2ChipListLocator).isPresent()
                        && driverTools.waitForElementToBeClickable(bannerCarouselV2ChipLocator).isPresent()
                        && initChipX != swipedChipX;
            case "IMAGE_BANNER_LIST":
                Locator bannerCarouselV2BannerListLocator = getBannerCarouselV2BannerListLocator(pageModuleTitle);
                Locator bannerCarouselV2BannerLocator = getBannerCarouselV2BannerLocator(pageModuleTitle, "1");
                driverTools.swipeToElement(bannerCarouselV2BannerListLocator, Swipe.UP, 3);

                return driverTools.waitForElementToAppear(bannerCarouselV2BannerListLocator).isPresent()
                        && driverTools.waitForElementToBeClickable(bannerCarouselV2BannerLocator).isPresent();
            case "SEE_ALL_LINK":
                Locator bannerCarouselV2SeeAllLinkLocator = getBannerCarouselV2SeeAllLinkLocator(pageModuleTitle);
                driverTools.swipeToElement(bannerCarouselV2SeeAllLinkLocator, Swipe.UP, 3);
                return driverTools.waitForElementToAppear(bannerCarouselV2SeeAllLinkLocator).isPresent()
                        && driverTools.waitForElementToBeClickable(bannerCarouselV2SeeAllLinkLocator).isPresent();
            case "CTA_BUTTON_WITH_TEXT":
                Locator bannerCarouselV2CTAButtonLocator = getBannerCarouselV2CTAButtonLocator(pageModuleTitle);
                driverTools.swipeToElement(bannerCarouselV2CTAButtonLocator, Swipe.UP, 3);
                return driverTools.waitForElementToAppear(bannerCarouselV2CTAButtonLocator).isPresent()
                        && driverTools.waitForElementToBeClickable(bannerCarouselV2CTAButtonLocator).isPresent();
            default:
                logger.warn("Unhandled section type '"+sectionType+"'");
        }

        return false;
    }

    private Locator getBannerCarouselV2TitleLocator(String pageModuleTitle) {
        return new Locator("BANNER_CAROUSEL_V2_TITLE", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='"+pageModuleTitle+"']"));
    }

    private Locator getBannerCarouselV2SubtitleLocator(String pageModuleTitle) {
        return new Locator("BANNER_CAROUSEL_V2_SUBTITLE", AppiumBy.xpath("(//XCUIElementTypeStaticText[@label='"+pageModuleTitle+"']/parent::*/parent::*/parent::*//XCUIElementTypeStaticText)[last()]"));
    }

    private Locator getBannerCarouselV2ChipListLocator(String pageModuleTitle) {
        return new Locator("BANNER_CAROUSEL_V2_CHIP_LIST", AppiumBy.xpath("//XCUIElementTypeCell[.//XCUIElementTypeStaticText[@label='"+pageModuleTitle+"']]//XCUIElementTypeOther[contains(@name,'selected')]/parent::*/parent::*"));
    }

    private Locator getBannerCarouselV2ChipLocator(String pageModuleTitle, String index) {
        if(index.equals("all")) {
            return new Locator("BANNER_CAROUSEL_V2_CHIP", AppiumBy.xpath("//XCUIElementTypeCell[.//XCUIElementTypeStaticText[@label='"+pageModuleTitle+"']]//XCUIElementTypeOther[contains(@name,'selected')]/parent::*//XCUIElementTypeStaticText"));
        }

        return new Locator("BANNER_CAROUSEL_V2_CHIP", AppiumBy.xpath("(//XCUIElementTypeCell[.//XCUIElementTypeStaticText[@label='"+pageModuleTitle+"']]//XCUIElementTypeOther[contains(@name,'selected')]/parent::*//XCUIElementTypeStaticText)["+index+"]"));
    }

    private Locator getBannerCarouselV2BannerListLocator(String pageModuleTitle) {
        return new Locator("BANNER_CAROUSEL_V2_BANNER_LIST", AppiumBy.xpath("//XCUIElementTypeCell[.//XCUIElementTypeStaticText[@label='" + pageModuleTitle + "']]//XCUIElementTypeCollectionView[.//XCUIElementTypeImage[@name='bn_carrousel']]/parent::*"));
    }

    private Locator getBannerCarouselV2BannerLocator(String pageModuleTitle, String index) {
        if(index.equals("all")) {
            return new Locator("BANNER_CAROUSEL_V2_BANNER", AppiumBy.xpath("//XCUIElementTypeCell[.//XCUIElementTypeStaticText[@label='"+pageModuleTitle+"']]//XCUIElementTypeImage[@name='bn_carrousel']"));
        }

        return new Locator("BANNER_CAROUSEL_V2_BANNER", AppiumBy.xpath("(//XCUIElementTypeCell[.//XCUIElementTypeStaticText[@label='"+pageModuleTitle+"']]//XCUIElementTypeImage[@name='bn_carrousel'])["+index+"]"));
    }

    private Locator getBannerCarouselV2SeeAllLinkLocator(String pageModuleTitle) {
        return new Locator("BANNER_CAROUSEL_V2_SEE_ALL_LINK", AppiumBy.xpath("(//XCUIElementTypeStaticText[@label='"+pageModuleTitle+"']/parent::*/parent::*//XCUIElementTypeStaticText)[2]"));
    }

    private Locator getBannerCarouselV2CTAButtonLocator(String pageModuleTitle) {
        return new Locator("BANNER_CAROUSEL_V2_CTA_BUTTON", AppiumBy.xpath("//XCUIElementTypeCell[.//XCUIElementTypeStaticText[@label='"+pageModuleTitle+"']]//*[@name='page_module_see_all_button']"));
    }

    @Override
    public void clickPageModuleChip(String pageModuleTitle, String byType, String input) {
        switch (byType) {
            case "index":
                int index = Integer.parseInt(input);
                driverTools.swipeToElement(getBannerCarouselV2ChipLocator(pageModuleTitle, "1"), Swipe.DOWN, 3);
                List<WebElement> chipsElementList = driverTools.elements(getBannerCarouselV2ChipLocator(pageModuleTitle, "all")).orElseThrow();
                driverTools.click(chipsElementList.get(index));
                break;
            case "name":
                throw new NotImplementedException();
        }
    }

    @Override
    public void clickBannerCarouselV2BannerByIndex(HomePageModule.PageModules targetPageModule, int index) {
        driverTools.click(getBannerCarouselV2BannerLocator(targetPageModule.moduleDetail.heading.title, "1"));
    }

    private String getSeasonalContentTextCondition(String contentTitle) {
        if (contentTitle.contains("/")) {
            String en = splitLanguageData(contentTitle, "/")[0];
            String id = splitLanguageData(contentTitle, "/")[1];
            return "(contains(@label,\"" + en + "\") or contains(@label,\"" + id + "\"))";
        }
        return "contains(@label,\"" + contentTitle + "\")";
    }

    private Locator getSeasonalBannerContents(String apiText) {
        return new Locator("SEASONAL_BANNER", AppiumBy.xpath(
                "//XCUIElementTypeStaticText[" + getSeasonalContentTextCondition(apiText) + "]"));
    }

    private Locator getSeasonalBannerCarousel(String contentTitle) {
        return new Locator("SEASONAL_CAROUSEL", AppiumBy.xpath(
                "//XCUIElementTypeStaticText[" + getSeasonalContentTextCondition(contentTitle)
                        + "]/ancestor::XCUIElementTypeCollectionView[1]"));
    }

    private Locator getSeasonalBannerCarouselFirstCardLabel(String contentTitle) {
        return new Locator("SEASONAL_FIRST_CARD_LABEL", AppiumBy.xpath(
                "(//XCUIElementTypeStaticText[" + getSeasonalContentTextCondition(contentTitle)
                        + "]/ancestor::XCUIElementTypeCollectionView[1]//XCUIElementTypeCell[1]//XCUIElementTypeStaticText)[1]"));
    }

    private void bringSeasonalBannerIntoView(Locator seasonalCard) {
        driverTools.waitForElementToPresence(seasonalCard, 5)
                .orElseThrow(() -> new NoSuchElementException("Seasonal banner was not present after waiting: " + seasonalCard));
        driverTools.swipeByDistance(HOME_FEED_RECYCLER, Swipe.UP, 0.18);
        driverTools.swipeByDistance(HOME_FEED_RECYCLER, Swipe.UP, 0.15);
        logger.log("Scrolled seasonal banner up to clear bottom nav and floating modules");
    }

    private Locator getButtonByText(String apiButtonText) {
        return new Locator("BUTTON_" + apiButtonText, AppiumBy.xpath(
                "//*[@name='tds_button_view' or @type='XCUIElementTypeButton'][contains(@label,'" + apiButtonText + "')]"));
    }

    @Override
    public boolean verifySeasonalBanner(HomePageModule.PageModules pageModule) {
        WebElement titleElement = driverTools.swipeToElement(getSeasonalBannerContents(pageModule.dynamicContents.get(0).contentTitle), Swipe.UP, 3).orElseThrow();
        boolean titleAppears = driverTools.isElementDisplayed(titleElement);
        logger.log("Is Content Title Text Correct" + titleAppears);

        WebElement descriptionElement = driverTools.swipeToElement(getSeasonalBannerContents(pageModule.dynamicContents.get(0).contentDescription), Swipe.UP, 3).orElseThrow();
        boolean contentDescriptionAppears = driverTools.isElementDisplayed(descriptionElement);
        logger.log("Is content Description Correct" + contentDescriptionAppears);

        WebElement buttonElement = driverTools.swipeToElement(getButtonByText(pageModule.dynamicContents.get(0).buttonText), Swipe.UP, 3).orElseThrow();
        boolean buttonTextIsCorrect = driverTools.isElementDisplayed(buttonElement);
        logger.log("Is Button Text Correct" + buttonTextIsCorrect);

        WebElement pageIndicatorElement = driverTools.swipeToElement(getButtonByText(pageModule.dynamicContents.get(0).tabText), Swipe.UP, 3).orElseThrow();
        boolean pageIndicator = driverTools.isElementDisplayed(pageIndicatorElement);
        logger.log("Page Indicator Correct" + pageIndicator);

        return titleAppears && buttonTextIsCorrect && contentDescriptionAppears && pageIndicator;
    }

    @Override
    public boolean verifySeasonalBannerClick(HomePageModule.PageModules pageModules) {
        WebElement webElement = driverTools.swipeToElement(getButtonByText(pageModules.dynamicContents.get(0).buttonText), Swipe.UP, 3).orElseThrow();
        driverTools.click(webElement);
        boolean redirected = driverTools.waitForElementToAppear(CONTINUE_BUTTON_FIRST_PAGE, 15).isPresent()
                || driverTools.waitForElementToAppear(LandingPageIOS.SEARCH_FIELD_BUTTON, 5).isPresent();
        logger.log("Seasonal banner redirected page marker visible: " + redirected);
        if (driverTools.waitForElementToAppear(LandingPageIOS.SEARCH_FIELD_BUTTON, 2).isPresent()) {
            LandingPage.getInstance(session).backToHomePage();
        } else {
            driverTools.back();
        }
        return redirected;
    }

    @Override
    public boolean verifySeasonalBannerScrollable(HomePageModule.PageModules pageModules) {
        String contentTitle = pageModules.dynamicContents.get(0).contentTitle;
        swipeToCard(contentTitle);
        bringSeasonalBannerIntoView(getSeasonalBannerContents(contentTitle));

        Locator carousel = getSeasonalBannerCarousel(contentTitle);
        driverTools.waitForElementToPresence(carousel, 10).orElseThrow();

        Locator firstCardLabel = getSeasonalBannerCarouselFirstCardLabel(contentTitle);
        String labelBefore = driverTools.getText(firstCardLabel).orElse("");
        driverTools.swipeByDistance(carousel, Swipe.LEFT, 0.75, 2);
        String labelAfter = driverTools.getText(firstCardLabel).orElse("");
        boolean labelChanged = !labelBefore.isBlank() && !labelBefore.equals(labelAfter);
        logger.log("Seasonal carousel swipe - before: [" + labelBefore + "], after: [" + labelAfter + "], changed: " + labelChanged);
        return labelChanged;
    }

    @Override
    public boolean scrollUntilBottom(int numberOfClicks) {
        driverTools.swipeDown(20);
        driverTools.waitForElementToPresence(DOWN_ARROW);
        boolean isAtBottom= false;
        for(int i = 0; i < numberOfClicks; i++) {
            if(driverTools.waitForElementToPresence(DOWN_ARROW,5).isPresent()){
                driverTools.clickElementByCoordinate(DOWN_ARROW);
            }else{
                isAtBottom = driverTools.waitForElementToPresence(UP_ARROW,5).isPresent();
            }
            if(isAtBottom){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean scrollBackToTop() {
        driverTools.clickElementByCoordinate(UP_ARROW);
        return driverTools.isElementDisplayed(HERO_BANNER_VISIBLE);
    }

    @Override
    public boolean checkHighLightDiscoverModule(String title) {
        String sectionTitle = title.split(", ")[0];
        String sectionLabel = title.split(", ")[5];
        String sectionDescription = title.split(", ")[6];

        swipeToCard(sectionTitle);
        driverTools.swipeToElement(getHighLightDiscoverModuleLocator(sectionTitle), Swipe.UP, 15);
        boolean Title = driverTools.isElementDisplayed(getHighLightDiscoverModuleLocator(sectionTitle));
        boolean label = driverTools.isElementDisplayed(getHighLightDiscoverModuleLocator(sectionLabel));
        boolean Description =driverTools.isElementDisplayed(getHighLightDiscoverModuleLocator(sectionDescription));
        boolean moduleIcon = driverTools.isElementDisplayed(getHighlightIconLocator(sectionTitle));
        return Title && label && Description && moduleIcon;
    }

    private Locator getHighLightDiscoverModuleLocator(String ModuleTitle) {
        return new Locator("HIGHLIGHT_DISCOVER_PRODUCT_SECTION", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='"+ModuleTitle+"']"));
    }

    @Override
    public boolean checkHighLightDiscoverModuleProduct(String title) {
        String ProductTitle = title.split(", ")[1];
        Locator getHighLightDiscoverModuleTitle = getHighLightDiscoverModuleLocator(ProductTitle);
        driverTools.swipeToElement(getHighLightDiscoverModuleTitle, Swipe.UP, 5);
        boolean ModuleProductTitle =  driverTools.isElementDisplayed(getHighLightDiscoverModuleTitle);
        return  ModuleProductTitle;
    }

    @Override
    public boolean checkHighLightDiscoverModuleLocation(String Location) {
        String LocationText = Location.split(", ")[2];
        Locator getHighLightDiscoverModuleLocation = HighLightDiscoverModuleLocation(LocationText);
        return driverTools.isElementDisplayed(getHighLightDiscoverModuleLocation);
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
        return driverTools.isElementDisplayed(getHighLightDiscoverModuleRating);
    }
    private Locator HighLightDiscoverModuleLocation(String  Location) {
        return new Locator("HIGHLIGHT_DISCOVER_PRODUCT_LOCATION", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'"+Location+"')]"));
    }

    @Override
    public boolean checkHighLightDiscoverModulePrice(String price) {
        String priceText = "IDR " + CoreFunctions.thousandSeparator(price.split(", ")[4], ",");
        driverTools.swipeToElement(getHighLightDiscoverModuleLocator(priceText),Swipe.UP,2);
        return driverTools.isElementDisplayed(getHighLightDiscoverModuleLocator(priceText));
    }

    private Locator highLightDiscoverClickScrollLocator(String Attribute) {
        return new Locator("HIGHLIGHT_DISCOVER_PRODUCT", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'"+Attribute+"')]"));
    }

    @Override
    public boolean verifyDiscoverModuleProductRedirection(String Attribute) {
        String AttributeText = Attribute.split(", ")[1];
        String priceText = "IDR " + Attribute.split(", ")[4];

        //check inventory card redirect
        driverTools.waitForElementToAppear(highLightDiscoverClickScrollLocator(AttributeText),30);
        driverTools.click(highLightDiscoverClickScrollLocator(AttributeText));
        boolean isRedirectCorrectCard = driverTools.waitForElementToAppear(PDP_PAGE_BACK_BUTTON).isPresent();
        logger.log("when click on inventory card is redirect to PDP page: "+isRedirectCorrectCard);
        driverTools.click(PDP_PAGE_BACK_BUTTON);

        //check image redirect
        driverTools.waitForElementToAppear(HIGHTLIGHT_DISCOVER_PRODUCT_IMAGE,30).ifPresent(driverTools::click);
        boolean isRedirectCorrectImg = driverTools.waitForElementToDisappear(HIGHTLIGHT_DISCOVER_PRODUCT_IMAGE,30);
        logger.log("when click on image is redirect to PDP page: "+isRedirectCorrectImg);
        driverTools.click(PDP_PAGE_BACK_BUTTON);

        driverTools.waitForElementToAppear(highLightDiscoverClickScrollLocator(AttributeText),30);
        driverTools.swipeLeft(getHighLightDiscoverModuleLocator(AttributeText),2);
        boolean isScrollWorking= driverTools.waitForElementToDisappear(getHighLightDiscoverModuleLocator(priceText));
        return isRedirectCorrectImg && isRedirectCorrectCard && isScrollWorking;
    }

    @Override
    public boolean verifyUnreadCount(Map<String, Integer> unreadCount) {
        int chatUnreadCount = unreadCount.get("chatTotalUnreadCount");
        int inboxTotalUnreadCount = unreadCount.get("inboxTotalUnreadCount");
        //In homepage if unread count greater than 99 it will show 99+
        int total = chatUnreadCount + inboxTotalUnreadCount;
        String expectedUnreadCount = total > 99 ? "99+" : String.valueOf(total);
        String actualUnreadCount = driverTools.getText(UNREAD_COUNT).orElseThrow();
        Assertion.assertThat("Verify unread count match", actualUnreadCount, equalTo(expectedUnreadCount));
        return true;
    }

    @Override
    public boolean verifySwipeHighlightDiscoverArticle(HighlightDiscoverPOJO highlightArticleSection) {
        final Locator FAB = new Locator("FAB", AppiumBy.name("tds_ic_cross"));
        String moduleTitle = highlightArticleSection.moduleTitle;
        String sectionTitle = highlightArticleSection.dynamicContentTitle;
        //swipe to section
        Locator sectionContainer = getHighlightSectionContainer(moduleTitle);
        driverTools.swipeByDistanceToElement(sectionContainer,SCREEN,Swipe.UP,0.075,10);
        driverTools.swipeByDistance(SCREEN,Swipe.UP,0.2);
        // Close FAB if appearing
        driverTools.element(FAB).ifPresent(driverTools::click);
        //swipe left
        Locator sectionTitleLocator = getCardLocator(sectionTitle);
        String bounds1 = driverTools.getValueAttribute(sectionTitleLocator, "rect");
        logger.log("bounds before swipe: "+bounds1);
        driverTools.swipeByDistance(sectionContainer,Swipe.LEFT,0.7);
        String bounds2 = driverTools.getValueAttribute(sectionTitleLocator, "rect");
        logger.log("bounds after swipe: "+bounds2);
        //verify disappear
        boolean isCardGoneAfterSwipe = !bounds1.equals(bounds2);
        logger.log("Is card moved out of view after swipe: "+isCardGoneAfterSwipe);
        //swipe right
        driverTools.swipeByDistance(sectionContainer,Swipe.RIGHT,0.7);
        String bounds3 = driverTools.getValueAttribute(sectionTitleLocator, "rect");
        logger.log("bounds after swipe back: "+bounds3);
        //verify appear
        boolean isCardAppearAfterSwipe = bounds1.equals(bounds3);
        logger.log("Is card moved to view after swipe: "+isCardAppearAfterSwipe);

        return isCardGoneAfterSwipe && isCardAppearAfterSwipe;
    }

    private Locator getHighlightSectionContainer(String title){
        return new Locator(title,AppiumBy.xpath("//XCUIElementTypeStaticText[@label='"+title+"']//following::XCUIElementTypeCollectionView"));
    }

    @Override
    public boolean verifyClickHighlightDiscoverArticle(HighlightDiscoverPOJO highlightArticleSection) {
        String sectionTitle = highlightArticleSection.dynamicContentTitle;
        String sectionSubtitle = highlightArticleSection.dynamicContentSubtitle;
        String expectedUrl = highlightArticleSection.dynamicContentUrlRedirect;
        String buttonText = highlightArticleSection.dynamicContentButtonText;

        Locator highlightArticleButton = new Locator("Highlight Article Text", AppiumBy.xpath("//*[@label='" + sectionTitle + "']//following-sibling::*[@label='" + sectionSubtitle + "']//following-sibling::*//XCUIElementTypeButton[@label='" + buttonText + "']"));
        boolean isLocatorUsingTextFound = driverTools.isElementDisplayed(highlightArticleButton);
        logger.log("is 3 text correct: " + isLocatorUsingTextFound);

        logger.log("Checking redirect button");
        driverTools.swipeToElement(highlightArticleButton, Swipe.UP, 3).orElseThrow();
        driverTools.click(highlightArticleButton);

        if(expectedUrl.contains("/hotel?utm_logic=&utm_section=pageModule")) {
            logger.log("Hotel landing page redirect detected, checking room and guest text");
            return !LandingPage.getInstance(session).getRoomAndGuestText().isEmpty();
        }

        driverTools.waitForWebview();
        driverTools.switchToWebView();

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

        driverTools.click(getImageLocator(sectionTitle));
        driverTools.waitForWebview();
        driverTools.switchToWebView();

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
    public boolean verifyHighlightDiscoverArticleTitle(HighlightDiscoverPOJO highlightArticleSection) {
        String moduleLabel = highlightArticleSection.moduleLabel;
        String moduleTitle = highlightArticleSection.moduleTitle;
        String moduleSubtitle = highlightArticleSection.moduleSubtitle;

        WebElement labelElement = driverTools.swipeToElement(getTextLocator(moduleLabel), Swipe.UP, 3).orElseThrow();
        boolean isLabelShown = driverTools.isElementDisplayed(labelElement);
        logger.log("is label shown: "+isLabelShown);

        WebElement titleElement = driverTools.swipeToElement(getTextLocator(moduleTitle), Swipe.UP, 3).orElseThrow();
        boolean isTitleShown = driverTools.isElementDisplayed(titleElement);
        logger.log("is title shown: "+isTitleShown);

        WebElement iconElement = driverTools.swipeToElement(getHighlightIconLocator(moduleTitle), Swipe.UP, 3).orElseThrow();
        boolean isIconShown = driverTools.isElementDisplayed(iconElement);
        logger.log("is icon shown: "+isIconShown);

        boolean isSubtitleShown =true;
        if(!moduleSubtitle.equals("")){
            isSubtitleShown = driverTools.isElementDisplayed(getTextLocator(moduleSubtitle));
            logger.log("is subtitle shown: "+isSubtitleShown);
        }

        return isLabelShown && isTitleShown && isIconShown && isSubtitleShown;
    }

    private Locator getTextLocator(String text){
        return new Locator(text,AppiumBy.xpath("//*[@label='"+text+"']"));
    }

    private Locator getImageLocator(String text){
        return new Locator(text,AppiumBy.xpath("(//*[@label='"+text+"']/ancestor::XCUIElementTypeCollectionView/ancestor::XCUIElementTypeOther/preceding-sibling::XCUIElementTypeImage[not(@label)])"));
    }

    private Locator getHighlightIconLocator(String title){
        return new Locator(title+" icon",AppiumBy.xpath("//*[@label='"+title+"']/..//XCUIElementTypeImage"));
    }

    public boolean verifyFinanceBarRedirect(boolean isOnProd) {
        driverTools.swipeToElement(PROMO_ICON,Swipe.DOWN,30);
        boolean isInBlibliTiketRewardsPage = true;
        if(isOnProd){
            driverTools.click(BLIBLI_TIKET_POINTS_FINANCE_BAR);
            driverTools.waitForWebview();
            driverTools.switchToWebView();
            isInBlibliTiketRewardsPage = driverTools.waitForElementToAppear(BLIBLI_TIKET_POINTS_PAGE_HEADER).isPresent();
            Assertion.assertThat("is redirect to blibli tiket points page", isInBlibliTiketRewardsPage, is(true));
            driverTools.click(BACK_BLIBLI_TIKET_POINTS);
            driverTools.switchToNative();
        }
        //check paylater
        driverTools.click(PAYLATER_FINANCE_BAR);
        boolean isInPaylaterPage = driverTools.waitForElementToAppear(PAYLATER_PAGE).isPresent();
        Assertion.assertThat("is redirect to paylater page",isInPaylaterPage, is(true));
        driverTools.click(BACK_PAYLATER);

        //check bliblipay
        driverTools.click(BLIBLIPAY_FINANCE_BAR);
        boolean isInBlibliPayPage = driverTools.waitForElementToAppear(BLIBLIPAY_HEADER).isPresent();
        Assertion.assertThat("is redirect to bliblipay page",isInPaylaterPage, is(true));

        return isInBlibliTiketRewardsPage && isInPaylaterPage && isInBlibliPayPage;
    }

    public boolean verifyFinanceBarText(FinanceBarPOJO data) {
        String tiketPoints = data.tiketPoints;
        String paylater = data.payLater;
        String blipay = data.blibliPay;
        driverTools.waitForElementToPresence(new Locator(tiketPoints,AppiumBy.name(tiketPoints))).orElseThrow();
        boolean isTiketPointsCorrect = driverTools.waitForElementToPresence(new Locator(tiketPoints,AppiumBy.xpath("(//*[contains(@label,'Blibli Tiket Points')])//following-sibling::*[contains(@label,'"+tiketPoints+"')]"))).isPresent();
        Assertion.assertThat("Is correct tiket points shown",isTiketPointsCorrect, is(true));
        boolean isPaylaterCorrect = driverTools.waitForElementToPresence(new Locator(paylater,AppiumBy.xpath("(//*[contains(@label,'PayLater')])//following-sibling::*[contains(@label,'"+paylater+"')]"))).isPresent();
        Assertion.assertThat("Is correct paylater shown", isPaylaterCorrect, is(true));
        boolean isBlipayCorrect = driverTools.waitForElementToPresence(new Locator(blipay,AppiumBy.xpath("(//*[contains(@label,'BlibliPay')])//following-sibling::*[contains(@label,'"+blipay+"')]"))).isPresent();
        Assertion.assertThat("Is correct blipay shown", isBlipayCorrect, is(true));

        return isTiketPointsCorrect && isPaylaterCorrect && isBlipayCorrect;
    }

    private Locator getTitleLocator(String title){
        return new Locator("TITLE_LOCATOR_OF_" + title, AppiumBy.xpath("//*[@name='continue_payment_button']/ancestor::*[.//XCUIElementTypeStaticText[contains(@label,'"+title+"')]][1]//XCUIElementTypeStaticText[contains(@label,'"+title+"')]"));
    }

    private Locator getContinuePaymentPriceLocator(){
        return new Locator("CONTINUE_PAYMENT_PRICE", AppiumBy.xpath("//*[@name='continue_payment_button']/ancestor::*[.//XCUIElementTypeStaticText[contains(@label,'IDR') or contains(@label,'Rp')]][1]//XCUIElementTypeStaticText[contains(@label,'IDR') or contains(@label,'Rp')]"));
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
        return new Locator("TITLE_GV_CLAIM", AppiumBy.xpath("//*[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + textArr[0].toLowerCase() + "') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + textArr[textArr.length - 1].toLowerCase() + "')]"));
    }

    private Locator getIconGVClaimVoucherLocator(String voucherTitle){
        return new Locator("ICON_GV_CLAIM_VOUCHER",AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'"+voucherTitle+"')]//preceding-sibling::*//XCUIElementTypeImage"));
    }

    private Locator getTitleGVClaimVoucherLocator(String voucherTitle){
        return new Locator("TITLE_GV_CLAIM_VOUCHER",AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'"+voucherTitle+"')]"));
    }

    private Locator getParentGVClaimCardLocator(String ctnBtnText){
        return new Locator("PARENT_GV_CLAIM_VOUCHER",AppiumBy.xpath("((//XCUIElementTypeButton[contains(@label,'"+ctnBtnText+"')]//parent::*)[1]//parent::*//preceding-sibling::*)//XCUIElementTypeCollectionView"));
    }

    private Locator getCTAGVClaimBtnLocator(String ctnBtnText){
        return new Locator("CTA_GV_CLAIM_BTN",AppiumBy.xpath("((//XCUIElementTypeButton[contains(@label,'"+ctnBtnText+"')]//parent::*)[1]//parent::*//preceding-sibling::*)//XCUIElementTypeCollectionView"));
    }

    @Override
    public void swipeToCtaBtn(String btnName){
        driverTools.swipeByDistanceToElement(BLIBLI_POINTS,SCREEN,Swipe.DOWN,0.68,20).orElseThrow();
        final Locator CARD_LOCATOR = this.getCTAGVClaimBtnLocator(btnName);
        driverTools.swipeByDistanceToElement(CARD_LOCATOR,SCREEN,Swipe.UP,0.5,50).orElseThrow();
        driverTools.swipeByDistance(SCREEN,Swipe.UP,0.3);
    }

    @Override
    public Boolean isUpcomingBookingCountdownDisplayed() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_COUNTDOWN,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.isElementDisplayed(UPCOMING_BOOKING_COUNTDOWN);
    }

    @Override
    public Boolean isFlightUpcomingBookingDisplayed() {
        return driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_FLIGHT,SCREEN,Swipe.UP,0.1,3).isPresent();
    }

    @Override
    public Boolean isUpcomingBookingHotelNameDisplayed() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_STAY,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.isElementDisplayed(UPCOMING_BOOKING_STAY);
    }

    @Override
    public Boolean checkInDetails() {
        driverTools.swipeToElement(UPCOMING_BOOKING_HOTEL_DETAILS,Swipe.UP,1);
        String checkInDetails = driverTools.getValueAttribute(UPCOMING_BOOKING_HOTEL_DETAILS,"label");
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
        String seeAllText = driverTools.getValueAttribute(SEE_ALL_UPCOMING_BOOKING,"label");
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
        return driverTools.getValueAttribute(UPCOMING_BOOKING_DEPARTURE_TIME,"label");
    }

    @Override
    public String getUpcomingBookingRouteInformation() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_ROUTE_INFO,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.getValueAttribute(UPCOMING_BOOKING_ROUTE_INFO, "label");
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
        return driverTools.getValueAttribute(UPCOMING_BOOKING_PASSENGER_NAME, "label");
    }

    @Override
    public String getUpcomingBookingClassName() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_CLASS_NAME,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.getValueAttribute(UPCOMING_BOOKING_CLASS_NAME, "label");
    }

    @Override
    public String getUpcomingBookingOriginStation() {
        driverTools.swipeByDistanceToElement(UPCOMING_BOOKING_ORIGIN_STATION,SCREEN,Swipe.UP,0.1,3).orElseThrow();
        return driverTools.getValueAttribute(UPCOMING_BOOKING_ORIGIN_STATION, "label");
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
        HomePage<T> homePage = HomePage.getInstance(session);

        try {
            orderDetailPage.getOrderID();
        } catch (NoSuchElementException e) {
            return false;
        }

        orderDetailPage.clickBack();
        homePage.goToHomeDeeplink();

        return true;
    }

    @Override
    public Boolean verifyUpcomingBookingVoucherBox() {
        driverTools.swipeByDistanceToElement(NEXT_PAGE_MODULE,SCREEN,Swipe.UP,0.4,3).orElseThrow();
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
        String verticalName = driverTools.getValueAttribute(UPCOMING_BOOKING_VERTICAL_TEXT, "label");
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
        WebElement scrollableToLast = driverTools.swipeByDistanceToElement(getTitleGVClaimVoucherLocator(dynamicContents.get(dynamicContents.size()-1).title),getParentGVClaimCardLocator(ctaBtn),Swipe.LEFT,1,15).orElseThrow();
        return driverTools.isElementDisplayed(scrollableToLast);
    }

    @Override
    public void clickGVVoucher(ArrayList<HomePageModule.DynamicContent> dynamicContents){
        driverTools.click(getTitleGVClaimVoucherLocator(dynamicContents.get(dynamicContents.size()-1).title));
    }

    @Override
    public void clickGVCtaBtn(String btnTitle){
        final Locator CARD_LOCATOR = this.getCTAGVClaimBtnLocator(btnTitle);
        driverTools.swipeByDistanceToElement(CARD_LOCATOR,SCREEN,Swipe.UP,0.5,50).orElseThrow();
        WebElement ctaBtnElement = driverTools.waitForElementToAppear(CARD_LOCATOR).orElseThrow();
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
        String endDate = dateSplit[1].trim();

        if(startDate.split("")[0].equals("0")){
            startDate = startDate.substring(1);
        }
        if(endDate.split("")[0].equals("0")){
            endDate = endDate.substring(1);
        }

        Locator recentlySearchedPageModule = getRecentlySearchedLocatorNearMe(startDate,endDate);
        WebElement recentlySearchedTextElement = driverTools.swipeToElement(recentlySearchedPageModule,Swipe.UP,10).orElseThrow();
        return driverTools.getText(recentlySearchedTextElement);
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

    private Locator getRecentlySearchedLocatorNearMe(String startDate, String endDate){
        return new Locator("NEAR ME",AppiumBy.xpath("//*[contains(@label,'"+startDate+"') and contains(@label,'"+endDate+"')]/parent::XCUIElementTypeOther/parent::XCUIElementTypeOther/preceding-sibling::XCUIElementTypeOther/XCUIElementTypeStaticText[contains(@label, 'Near Me') or contains(@label, 'Dekat saya')]"));
    }

    @Override
    public void openWishlist(){
        driverTools.waitForElementToAppear(CLOSE_GIVE_RATING_POPUP).ifPresent(closeBtn -> driverTools.click(closeBtn));
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
        driverTools.waitForElementToAppear(BLIBLI_TIKET_POINTS_FINANCE_BAR).ifPresent(driverTools::click);
        boolean isInBlibliTiketRewardsPage = driverTools.waitForElementToAppear(BLIBLI_TIKET_POINTS_PAGE_HEADER).isPresent();
        clickHomeIcon();
        return isInBlibliTiketRewardsPage;
    }

    @Override
    public Boolean verifyPayLaterRedirection(){
        driverTools.swipeLeft(FINANCE_BAR);
        driverTools.waitForElementToPresence(PAYLATER_FINANCE_BAR).ifPresent(driverTools::click);
        boolean isInPaylaterPage = driverTools.waitForElementToAppear(PAYLATER_PAGE).isPresent();
        driverTools.click(BACK_PAYLATER);
        return isInPaylaterPage;
    }

    /**
     * The info screen carries no uniquely identifiable back button, so the redirection is confirmed
     * structurally instead: a navigation bar or webview is pushed on top of the home feed.
     */
    private static final Locator DYNAMIC_WIDGET_INFO_DETAIL = new Locator("DYNAMIC_WIDGET_INFO_DETAIL", AppiumBy.xpath(
            "//XCUIElementTypeNavigationBar | //XCUIElementTypeWebView | //XCUIElementTypeSheet"));

    private static final Locator HOME_FEED = new Locator("HOME_FEED", AppiumBy.xpath(HOME_FEED_XPATH));

    /** iOS exposes the heading text on any of label, name or value depending on the build. */
    private static String dynamicWidgetTextMatch(String text) {
        return "(contains(@label,\"" + text + "\") or contains(@name,\"" + text + "\") or contains(@value,\"" + text + "\"))";
    }

    /**
     * Heading node of the dynamic widget. {@code sectionTitle} accepts the same "English/Bahasa"
     * form as {@link #getCardLocator(String)}, so the same case runs on either app language.
     */
    private String dynamicWidgetTitleNode(String sectionTitle) {
        String titleText;
        if (sectionTitle.contains("/") && !sectionTitle.contains("//")) {
            String[] languages = splitLanguageData(sectionTitle, "/");
            titleText = "(" + dynamicWidgetTextMatch(languages[0]) + " or " + dynamicWidgetTextMatch(languages[1]) + ")";
        } else {
            titleText = dynamicWidgetTextMatch(sectionTitle);
        }
        return "//*[@type='XCUIElementTypeStaticText' and " + titleText + "]";
    }

    private Locator dynamicWidgetTitle(String sectionTitle) {
        return new Locator("DYNAMIC_WIDGET_TITLE", AppiumBy.xpath(dynamicWidgetTitleNode(sectionTitle)));
    }

    private Locator dynamicWidgetSubtitle(String sectionTitle) {
        return new Locator("DYNAMIC_WIDGET_SUBTITLE", AppiumBy.xpath(dynamicWidgetTitleNode(sectionTitle)
                + "//following::XCUIElementTypeStaticText[not(@label='Lihat Semua' or @label='See All')][1]"));
    }

    private Locator dynamicWidgetImage(String sectionTitle) {
        return new Locator("DYNAMIC_WIDGET_IMAGE", AppiumBy.xpath(dynamicWidgetTitleNode(sectionTitle)
                + "//following::XCUIElementTypeImage[1]"));
    }

    /**
     * The info ("i") button carries no name of its own - it is the first button inside the widget's
     * {@code name='main'} container, ahead of the background-progress-bar and countdown images.
     */
    private Locator dynamicWidgetInfoButton(String sectionTitle) {
        return new Locator("DYNAMIC_WIDGET_INFO_BUTTON", AppiumBy.xpath("(" + dynamicWidgetTitleNode(sectionTitle)
                + "//following::XCUIElementTypeOther[@name='main']//XCUIElementTypeButton)[1]"));
    }

    @Override
    public void swipeToDynamicWidget(String sectionTitle) {
        logger.log("Swiping to dynamic widget: " + sectionTitle);
        clickHomeIcon();
        driverTools.swipeToElement(FINANCE_BAR, Swipe.DOWN, 10);
        Locator heading = dynamicWidgetTitle(sectionTitle);
        final int maxSwipeAttempts = 60;
        boolean found = false;
        for (int i = 0; i < maxSwipeAttempts; i++) {
            found = driverTools.isElementDisplayed(heading);
            if (found || driverTools.isElementDisplayed(UP_ARROW)) {
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
        boolean isDetailOnTop = driverTools.waitForElementToAppear(DYNAMIC_WIDGET_INFO_DETAIL, 15).isPresent();
        boolean isHomeFeedGone = !driverTools.isElementDisplayed(HOME_FEED);
        logger.log("info detail on top -> " + isDetailOnTop);
        logger.log("home feed dismissed -> " + isHomeFeedGone);
        return isDetailOnTop || isHomeFeedGone;
    }

    @Override
    public Boolean verifyBlipayRedirection(){
        driverTools.swipeLeft(BLIBLI_TIKET_POINTS_FINANCE_BAR,5);
        driverTools.waitForElementToAppear(BLIBLIPAY_FINANCE_BAR,5);
        driverTools.click(BLIBLIPAY_FINANCE_BAR);
        driverTools.waitForWebview();
        String url = driverTools.switchToWebViewUrl("blipay");
        boolean isInBlibliPayPage = url.contains("blipay");
        driverTools.switchToNative();
        return isInBlibliPayPage;
    }
}