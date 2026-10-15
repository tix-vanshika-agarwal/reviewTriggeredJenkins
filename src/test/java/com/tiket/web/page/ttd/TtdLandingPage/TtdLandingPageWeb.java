package com.tiket.web.page.ttd.TtdLandingPage;

import com.tiket.appium.PopupObserver;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.*;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import com.tiket.web.page.ttd.TtdSearchResultPage.TtdSearchResultPageWeb;
import com.tiket.web.page.ttd.TtdDestinationPage.TtdDestinationPageWeb;
import com.tiket.web.page.ttd.TtdPartnerPage.TtdPartnerPageWeb;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.*;
import com.tiket.model.Platform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static org.hamcrest.Matchers.*;

public abstract class TtdLandingPageWeb<T extends WebDriver> extends WebPage<T> {

    public TtdLandingPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TtdLandingPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new TtdLandingPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new TtdLandingPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final String PARTNER_PAGE_URL = "https://en.preprod.tiket.com/to-do/partner/preprod-partner";
    private static final String TTD_LANDING_PAGE_URL_GK = "https://en.gatotkaca.tiket.com/to-do?utm_source=INTERNAL&utm_medium=home";
    private static final String TTD_LANDING_PAGE_URL_PROD = "https://www.tiket.com/to-do";
    private static final String TTD_LANDING_PAGE_URL_PREPROD = "https://preprod.tiket.com/to-do/";
    private static final Locator APP_DOWNLOAD_POPUP = new Locator("APP_DOWNLOAD_POPUP", new By.ByXPath("//*[contains(@class,'FloatingBannerDownloadApp_close')]"));
    private static final Locator LOCATION_LABEL = new Locator("LOCATION_LABEL", new By.ByXPath("//button[contains(@class,'HomeBanner_see_nearby')]//span"));
    protected static final Locator SEARCH_INPUT = new Locator("SEARCH_INPUT", new By.ByXPath("//input[@aria-label='Search Text Input']"));
    protected static final Locator SEARCH_INPUT_INTERNAL = new Locator("SEARCH_INPUT_INTERNAL", new By.ByXPath("//input[@id='search-ttd-box']"));
    private static final Locator GROUP_ICON_BUTTON = new Locator("GROUP_ICON_BUTTON", new By.ByXPath("//*[@data-testid='chip-group']//button"));
    private static final Locator CATEGORY_WITHOUT_PROMO = new Locator("CATEGORY_WITHOUT_PROMO", new By.ByXPath("//*[contains(@class, 'HomeMenuSelection_menu_item_image') and not(*[contains(@class,'HomeMenuSelection_menu_item_label')])]//following-sibling::*[contains(@class, 'HomeMenuSelection_menu_item_text') and (not(contains(text(),'Promo')) and (not(contains(text(),'All')) or not(contains(text(),'Semua'))))]"));
    private static final Locator CATEGORY_WITH_PROMO = new Locator("CATEGORY_WITH_PROMO", new By.ByXPath("//*[contains(@class, 'HomeMenuSelection_menu_item_image') and *[contains(@class,'HomeMenuSelection_menu_item_label')]]//following-sibling::*[contains(@class, 'HomeMenuSelection_menu_item_text')]"));
    private static final Locator ALL_ITEM_ICON = new Locator("ALL_ITEM_ICON", new By.ByXPath("//*[contains(@class, 'HomeMenuSelection_menu_item_image') and not(*[contains(@class,'HomeMenuSelection_menu_item_label')])]//following-sibling::*[contains(@class, 'HomeMenuSelection_menu_item_text') and (not(contains(text(),'Promo')) and (contains(text(),'All') or contains(text(),'Semua')))]"));
    public static final Locator CLOSE_POPUP = new Locator("CLOSE_POPUP", new By.ByXPath("//*[@data-testid='modal-close-button']"));
    public static final Locator CROSS_POPUP = new Locator("CLOSE_POPUP", new By.ByXPath("//*[@data-testid='bottom-sheet-header']"));
    private static final Locator TITLE_POPUP = new Locator("TITLE_POPUP", new By.ByXPath("//div[contains(@class,'BottomSheet_body_title_wrapper')]//h2"));    private static final Locator CATEGORY_NAME_POPUP = new Locator("CATEGORY_NAME_POPUP", new By.ByXPath("//*[contains(@class, 'AllCategoriesDialog_category_section_title')]"));
    private static final Locator CATEGORY_CHEVRON_POPUP = new Locator("CATEGORY_CHEVRON_POPUP", new By.ByXPath("//*[contains(@class, 'AllCategoriesDialog_category_section_title')]//*[contains(@id,'chevron-right')]"));
    private static final Locator SUBCATEGORY_ICON_POPUP = new Locator("SUBCATEGORY_ICON_POPUP", new By.ByXPath("//*[contains(@class, 'AllCategoriesDialog_category_item')]"));
    private static final Locator SEARCH_BAR_POPUP = new Locator("SEARCH_BAR_POPUP", new By.ByXPath("(//*[contains(@class,'SearchBox_placeholder')]//input)[2]"));
    private static final Locator DESTINATION_SECTION_POPUP = new Locator("DESTINATION_SECTION_POPUP", new By.ByXPath("//*[contains(@class,'DestinationSearchInitialContent_carousel') or contains(@class,'DestinationSearchInitialContent_destination_suggestion')]//preceding-sibling::*//*[contains(@class,'Text_size_h3')]"));
    public static final Locator SEARCH_ITEM = new Locator("SEARCH_ITEM", new By.ByXPath("//*[contains(@class,'SearchItem_search_item_description')]//preceding-sibling::*[contains(@class,'SearchItem_search_item_title')]"));
    private static final Locator TOP_KEYWORD_TEXT = new Locator("TOP_KEYWORD_TEXT", new By.ByXPath("//*[contains(@class,'SearchInitialContent_top_keyword_item_result')]//*[contains(@class,'Chip_text')]"));
    private static final Locator TOP_KEYWORD_BUTTON = new Locator("TOP_KEYWORD_BUTTON", new By.ByXPath("//*[contains(@class,'SearchInitialContent_top_keyword_item_result')]//button"));
    private static final Locator REFRESH_TOP_KEYWORD = new Locator("REFRESH_TOP_KEYWORD", new By.ByXPath("//*[contains(@class,'SearchInitialContent_search_label')]//button"));
    private static final Locator RECENT_SEARCH_ITEM = new Locator("RECENT_SEARCH_ITEM", new By.ByXPath("//*[contains(@class,'RecentlySearched_container')]//span"));
    private static final Locator POPULAR_ATTRACTIONS_RANK = new Locator("POPULAR_ATTRACTIONS_RANK", new By.ByXPath("//*[contains(text(),'Popular Attractions') or contains(text(),'Atraksi Populer')]//following-sibling::*//*[contains(@class,'PopularSection_search_result_item')]//*[contains(@class,'SearchItem_rank_indicator')]"));
    private static final Locator POPULAR_EVENTS_RANK = new Locator("POPULAR_EVENTS_RANK", new By.ByXPath("//div[contains(@class,'PopularSection_popular_section_result')]   [.//h3[normalize-space()='Popular Events' or normalize-space()='Event Populer']]   //span[contains(@class,'SearchItem_top_badge')][@data-rank]"));
    private static final Locator POPULAR_EVENTS_NAMES = new Locator("POPULAR_EVENTS_NAMES", new By.ByXPath("//div[contains(@class,'PopularSection_popular_section_result')]   [.//h3[normalize-space()='Popular Events' or normalize-space()='Event Populer']]   //span[contains(@class,'SearchItem_search_item_title')]"));
    private static final Locator KEYWORD_CHIPS = new Locator("KEYWORD_CHIPS", new By.ByXPath("(//div[contains(@class,'top_keyword_item_result')]//button)"));
    private static final Locator SELECTED_KEYWORD_IN_SEARCH = new Locator("SELECTED_KEYWORD_IN_SEARCH", new By.ByXPath("//*[contains(@class,'SearchItem_title_two_line')]//span"));
    private static final Locator SEE_ALL_ACTIVITIES = new Locator("SEE_ALL_ACTIVITIES", new By.ByXPath("//button[contains(@class,'HomeBanner_see_nearby_activitites')]"));
    private static final Locator FILTER_BUTTON = new Locator("FILTER_BUTTON", new By.ByXPath("//button[contains(@class, 'SortFilterSection_filter_button__QsvRF')]"));
    private static final Locator FILTER_BUTTON_BADGE = new Locator("FILTER_BUTTON_BADGE", new By.ByXPath("//span[contains(@class, 'Notif_notif__jT3gD') and contains(@class, 'Notif_color_green__ezbF1')]\n"));
    private static final Locator ALL_LOCATIONS = new Locator("ALL_LOCATIONS", new By.ByXPath("//span[contains(@class, 'Chip_text')]/span[contains(@class, 'Text_weight_bold') and text()='All locations']\n"));
    private static final Locator ATTRACTION_BUTTON = new Locator("ATTRACTION_BUTTON", new By.ByXPath("//span[contains(@class, 'HomeMenuSelection_menu_item_text') and (text()='Attraction' or text()='Atraksi')]\n"));
    private static final Locator SEARCH_INPUT_ATTRACTION = new Locator("SEARCH_INPUT_ATTRACTION", new By.ByXPath("//input[@type='text' and (@placeholder='Search activities' or @placeholder='Cari aktivitas')]"));
    private static final Locator SEE_ALL_ACTIVITIES_NEARBY = new Locator("SEE_ALL_ACTIVITIES_NEARBY", new By.ByXPath("//button[.//span[text()='See all activities nearby' or text()='Lihat semua aktivitas terdekat']]"));
    private static final Locator ALLOW_ACCESS_BUTTON = new Locator("ALLOW_ACCESS_BUTTON", new By.ByXPath("//*[contains(@class, 'Dialog_pop_up')]//*[contains(@class, 'Figure_footer_accessibility')]//span[contains(@class, 'Button_truncation') and contains(text(), 'Allow access') or contains(text(), 'Beri izin akses')]"));
    private static final Locator SEE_ALL_ACTIVITIES_LOCATION_ON = new Locator("SEE_ALL_ACTIVITIES_LOCATION_ON", new By.ByXPath("//button[contains(@class, 'Chip_chip')]//span[contains(@class, 'Text_weight_bold')]"));
    private static final Locator NEW_ACTIVITIES_ITEM = new Locator("NEW_ACTIVITIES_ITEM", new By.ByXPath("//*[contains(text(),'New Activities') or contains(text(),'Aktivitas Baru')]//following-sibling::*//*[contains(@class,'PopularSection_search_result_item')]//*[contains(@class,'SearchItem_search_item_title')]"));
    private static final Locator NEW_ACTIVITIES_ITEM_SEE_ALL_BUTTON = new Locator("NEW_ACTIVITIES_ITEM_SEE_ALL_BUTTON", new By.ByXPath("//*[contains(text(),'New Activities') or contains(text(),'Aktivitas Baru')]//following-sibling::*[contains(@class,'PopularSection_see_all')]//*[contains(@class,'Button')]"));
    private static final Locator POPULAR_DESTINATION_SEE_ALL_BUTTON = new Locator("POPULAR_DESTINATION_SEE_ALL_BUTTON", new By.ByXPath("//*[contains(text(),'Popular Destinations') or contains(text(),'Destinasi Populer')]//following-sibling::*[contains(@class,'PopularSection_see_all')]//*[contains(@class,'Button')]"));
    private static final Locator DESTINATION_LIST_PAGE = new Locator("DESTINATION_LIST_PAGE", new By.ByXPath("//span[contains(text(),'Tempat Populer di Indonesia') or contains(text(),'Popular Places in Indonesia')]"));
    private static final Locator POPULAR_EVENTS = new Locator("POPULAR_EVENTS", new By.ByXPath("//*[text()='Popular Events' or text()='Event Populer']\n"));
    private static final Locator TODO_RANK = new Locator("TODO_RANK", new By.ByXPath("//div[contains(@class, 'PopularSection_popular_section_result')]//a[contains(@class, 'PopularSection_search_result_item')][.//span[contains(@class, 'SearchItem_')]]"));
    private static final Locator TODO_NAME = new Locator("TODO_NAME", new By.ByXPath("//span[contains(@class, 'SearchItem_search_item_title')]"));
    private static final Locator TODO_LIST_CONTAINER = new Locator("TODO_LIST_CONTAINER", new By.ByXPath("//div[contains(@class, 'PopularSection_popular_section_result')]//a[contains(@class, 'PopularSection_search_result_item')][span//span[@data-rank and number(@data-rank) >= 1 and number(@data-rank) <= 10]]"));
    private static final Locator SEE_ALL_EVENTS_BUTTON = new Locator("SEE_ALL_EVENTS_BUTTON", new By.ByXPath("//a[contains(text(), 'See all popular events') or contains(text(), 'Lihat semua event populer')]\n"));
    private static final Locator POPULAR_ATTRACTION = new Locator("POPULAR_ATTRACTION", new By.ByXPath("//h3[contains(@class, 'Text_size_h3')]"));
    private static final Locator SEE_ALL_ATTRACTION_BUTTON = new Locator("SEE_ALL_ATTRACTION_BUTTON", new By.ByXPath("//a[contains(text(), 'See all popular attractions') or contains(text(), 'Lihat semua atraksi populer')]\n"));
    private static final Locator CLEAR_ALL = new Locator("CLEAR_ALL", new By.ByXPath("(//button[contains(@class, 'SearchLabel_action')])[2]"));
    private static final Locator ATTRACTION_ITEM = new Locator("ATTRACTION_ITEM", new By.ByXPath("//*[contains(text(),'Popular Attractions') or contains(text(),'Atraksi Populer')]//following-sibling::*//*[contains(@class,'PopularSection_search_result_item')]//*[contains(@class,'SearchItem_search_item_title')]"));
    private static final Locator EVENT_ITEM = new Locator("EVENT_ITEM", new By.ByXPath("//*[contains(text(),'Popular Events') or contains(text(),'Event Populer')]//following-sibling::*//*[contains(@class,'PopularSection_search_result_item')]//*[contains(@class,'SearchItem_search_item_title')]"));
    private static final Locator FIRST_SEARCH_RESULT = new Locator("TTD_FIRST_RESULT", By.xpath("(//a[contains(@class,'SearchItem_search_item')]//span[contains(@class,'SearchItem_search_item_title')])[1]"));
    private static final Locator SEARCH_BAR_TOP = new Locator("SEARCH_BAR_TOP", By.xpath("//*[contains(@class, 'SearchBox_search_box')]"));


    private Locator getTopKeywordLocatorByIndex(int index) {
        return new Locator("TOP_KEYWORD_TEXT", new By.ByXPath("//*[contains(@class,'SearchInitialContent_top_keyword_item_result')]//button["+index+"]//*[contains(@class,'Chip_text')]"));
    }

    private static final Locator SEE_ALL_BUTTON = new Locator("SEE_ALL_BUTTON", new By.ByXPath("//button[@role='button' and (normalize-space(text())='See all' or normalize-space(text())='Lihat semua')]"));
    private static final Locator SEO_TITLE = new Locator("SEO_TITLE", new By.ByXPath("//h1[@data-testid='seo-title']"));
    private static final Locator FIRST_RECENTLY_VIEWED_ITEM = new Locator("FIRST_RECENTLY_VIEWED_ITEM", new By.ByXPath("(//a[contains(@class,'SearchItem_search_item__')])[1]"));
    public static final Locator FIRST_SEARCH_ITEM = new Locator("FIRST_SEARCH_ITEM", new By.ByXPath("(//a[contains(@class,'SearchItem_search_item__')])[1]"));
    public static final Locator FIRST_SEARCH_PRODUCT_ITEM = new Locator("FIRST_SEARCH_PRODUCT_ITEM", By.xpath("(//a[contains(@class, 'SearchItem_search_item__') and .//div[contains(@class, 'SearchItem_search_item_description')]])[1]"));
    private static final Locator SHOW_ALL_PACKAGES_BUTTON = new Locator("SHOW_ALL_PACKAGES_BUTTON", new By.ByXPath("//span[contains(@class,'Text_text__') and (contains(normalize-space(text()),'Show all packages') or contains(normalize-space(text()),'Lihat semua paket'))]"));
    private static final Locator SELECT_BUTTON = new Locator("SELECT_BUTTON", new By.ByXPath("(//button[contains(@class,'Button_variant_primary__') and (contains(normalize-space(.),'Select') or contains(normalize-space(.),'Pilih'))])[11]"));
    private static final Locator ADULT_PLUS_BUTTON = new Locator("QUANTITY_PLUS_BUTTON", new By.ByXPath("(//button[contains(@class,'QuantityEditor_operation_button__')])[2]"));
    private static final Locator CHILD_PLUS_BUTTON = new Locator("CHILD_PLUS_BUTTON", new By.ByXPath("(//button[contains(@class,'QuantityEditor_operation_button__')])[4]"));
    private static final Locator BOOK_BUTTON = new Locator("BOOK_BUTTON", new By.ByXPath("//button[contains(@class,'Button_variant_primary__') and (.='Book' or .='Pesan')]"));
    private static final Locator CLAIM_BUTTON = new Locator("CLAIM_BUTTON", new By.ByXPath("(//button[contains(@class,'GiftVoucherCard_claim_button__') and (normalize-space(.)='Claim' or normalize-space(.)='Klaim')])[3]"));
    public static final Locator PROMO_SECTION = new Locator("PROMO_SECTION", new By.ByXPath("//*[contains(@class,'BenefitSection_wrapper')]"));
    private static final Locator PROMO_SECTION_CLAIM_BTN = new Locator("PROMO_SECTION_CLAIM_BTN", new By.ByXPath("//*[contains(@class,'BenefitSection_cta_button') and (normalize-space(.)='Claim' or normalize-space(.)='Klaim')]"));
    private static final Locator CLAIM_POPUP = new Locator("CLAIM_POPUP", new By.ByXPath("//div[contains(@class,'Snackbar_text_content') and (contains(text(),'Vouchers will go to your Voucher Box') or contains(text(),'Voucher akan masuk ke Voucher Box-mu'))]"));
    private static final Locator PREPROD_AUTOMATION_TESTING = new Locator("PREPROD_AUTOMATION_TESTING", new By.ByXPath("//span[normalize-space(text())='Preprod Automation Testing' or normalize-space(text())='Pengujian Otomasi Preprod']"));
    private static final Locator SELECT_SEATING_BUTTON = new Locator("SELECT_SEATING_BUTTON", new By.ByXPath("(//span[contains(normalize-space(text()),'Select') or contains(normalize-space(text()),'Pilih')])[13]"));
    private static final Locator REFUND_ALLOWED_LABEL = new Locator("REFUND_ALLOWED_LABEL", new By.ByXPath("//div[@data-testid='refund-tier-label']//span[contains(normalize-space(text()),'Refund allowed') or contains(normalize-space(text()),'Refund diperbolehkan')]"));
    private static final Locator SEAT_SELECTED_AUTO_LABEL = new Locator("SEAT_SELECTED_AUTO_LABEL", new By.ByXPath("//div[contains(@class,'OrderDetail_seat_type')]//span[contains(normalize-space(text()),'Seat selected automatically') or contains(normalize-space(text()),'Kursi dipilih otomatis')]"));
    private static final Locator CONTINUE_TO_PAYMENT_BUTTON = new Locator("CONTINUE_TO_PAYMENT_BUTTON", new By.ByXPath("//button[contains(@class,'Button_variant_primary') or contains(@class,'PaymentDetail_button_payment')]//span[contains(normalize-space(.),'Continue to payment') or contains(normalize-space(.),'Lanjut ke pembayaran')]"));
    private static final Locator RADIO_SALUTATION_MR = new Locator("RADIO_SALUTATION_MR", new By.ByXPath("(//label[.//span[contains(@class,'Radio_children') and (contains(normalize-space(.),'Mr') or contains(normalize-space(.),'Tn'))]]//input)[1]"));
    private static final Locator COUNTRY_DROPDOWN = new Locator("COUNTRY_DROPDOWN", new By.ByXPath("//input[@id='countryregion-of-residence' and @placeholder='Country/Region of residence']"));
    private static final Locator COUNTRY_INDONESIA = new Locator("COUNTRY_INDONESIA", new By.ByXPath("//p[contains(@class,'List_title') and contains(.,'Indonesia (+62)')]"));
    private static final Locator CONTACT_DETAILS_LOGIN = new Locator("CONTACT_DETAILS_LOGIN", new By.ByXPath("//div[@data-testid='contact-login']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[contains(@class,'Button_variant_primary__') and (contains(normalize-space(.),'Save') or contains(normalize-space(.),'Simpan'))]"));
    private static final Locator ANDROID_ACCEPT_ALERT = new Locator("ANDROID_ACCEPT_ALERT", AppiumBy.xpath("//*[@resource-id=\"com.android.chrome:id/positive_button\"]"));
    public static final Locator ANDROID_ALLOW_THE_SITE = new Locator("ALLOW_THE_SITE", AppiumBy.accessibilityId("Allow while visiting the site"));
    public static final Locator IOS_ALLOW = new Locator("IOS_ALLOW", AppiumBy.accessibilityId("Allow"));
    private static final Locator NEAR_ME = new Locator("NEAR_ME", new By.ByXPath("//*[@data-testid='nearby-destination']"));
    private static final Locator CONTINUE_ON_WEB_POPUP = new Locator("CONTINUE_ON_WEB_POPUP", new By.ByXPath("//*[@data-qa-id='secondaryCtaClose-button']"));
    private static final Locator CAMPAIGN_LABEL = new Locator("CAMPAIGN_LABEL", new By.ByXPath("(//div[contains(@class, 'Label_label') and contains(., 'Campaign Maryanto EN')])[1]"));
    private static final Locator SELECT_SESSION_BTN = new Locator("SELECT_SESSION_BTN", By.xpath("(//span[contains(@class,'Button_truncation_not_applied') and text()='Select'])[1]"));
    private static final Locator TOMORROW_DATE_CARD = new Locator("TOMORROW_DATE_CARD", By.xpath("//div[contains(@class,'DateFilter_date_card')]//span[text()='Tomorrow' or text()='Besok']/parent::div"));
    private static final Locator SELECT_SESSION_DROPDOWN = new Locator("SELECT_SESSION_DROPDOWN", By.xpath("//button[contains(@class,'ScheduleSession_selector')]//span[text()='Select session' or text()='Pilih sesi']"));
    private static final Locator SESSION_ITEM = new Locator("SESSION_ITEM", By.xpath("(//button[contains(@class,'ScheduleSession_session_item')])[3]"));
    private static final Locator BOOKING_PAGE_SESSION_INFO = new Locator("BOOKING_PAGE_SESSION_INFO", By.xpath("//div[contains(@class,'OrderDetail_schedule_session_detail')]//div[contains(@class,'OrderDetail_schedule_session_content')]"));
    private static final Locator NEAREST_BUTTON = new Locator("NEAREST_BUTTON",By.xpath("//button[contains(@class,'Chip_chip') and .//span[text()='Nearest']]"));
    private static final Locator ENABLE_GPS_TITLE = new Locator("ENABLE_GPS_TITLE", By.xpath("//h2[contains(@class,'Figure_title') and text()='Enable GPS service']"));
    private static final Locator OKAY_BUTTON = new Locator("OKAY_BUTTON", By.xpath("//button[contains(@class,'Button_variant_primary') and .//span[text()='Okay']]"));
    private static final Locator FIRST_PRODUCT_CARD = new Locator("FIRST_PRODUCT_CARD",By.xpath("(//div[contains(@class,'ProductCardTTD_card_content_wrapper')])[1]"));
    private static final Locator NEVER_ALLOW = new Locator("NEVER_ALLOW", By.xpath("//*[@content-desc='Never allow']"));
    private static final Locator SEO_TITLE_PREPROD_ANNISA = new Locator("SEO_TITLE_PREPROD_ANNISA", By.xpath("//h1[@data-testid='seo-title' and contains(@class,'Text_text__')]"));
    private static final Locator PACKAGE_OPTIONS = new Locator("PACKAGE_OPTIONS", By.xpath("//div[contains(@class,'FloatingNavMobile_button_nav')]"));
    private static final Locator PACKAGE_OPTIONS_TITLE = new Locator("PACKAGE_OPTIONS_TITLE", By.xpath("//h2[contains(@class,'BottomSheet_body_title') and text()='Package Options']"));
    private static final Locator PACKAGE_CARDS = new Locator("PACKAGE_CARDS", By.xpath("//div[@data-testid='package-card']"));
    private static final Locator PRODUCT_TITLE_PARTNER_PAGE = new Locator("PRODUCT_TITLE_PARTNER_PAGE", By.xpath("//*[@data-testid='partner-info-name']"));
    private static final Locator RECENT_VIEW_CARD_CLOSE_BUTTON = new Locator("RECENT_VIEW_CARD_CLOSE_BUTTON",
            By.xpath("//div[contains(@class,'RecentlyViewCard_card_top_section')]/*[local-name()='svg' and starts-with(@data-testid,'icon-close-')]"));
    private static final Locator RECENTLY_VIEW_CARDS = new Locator("RECENTLY_VIEW_CARDS",
            By.xpath("//div[contains(@class,'SearchOverlay_search_content')]//div[@data-testid='recently-view']//div[@data-testid='carousel-children']//a[contains(@class,'RecentlyViewCard_card_wrapper')]"));

    public void closeContinueOnWebPopup() {
        driverTools.waitForElementToBeClickable(CONTINUE_ON_WEB_POPUP, 5);
        if (driverTools.isElementDisplayed(CONTINUE_ON_WEB_POPUP)) {
            driverTools.click(CONTINUE_ON_WEB_POPUP);
        }
    }

    public void clickNearMeLocation() {
        if (Configs.PLATFORM == Platform.AMWEB) {
            driverTools.switchToNative();
            driverTools.waitForElementToAppear(ANDROID_ACCEPT_ALERT, 15).ifPresent(driverTools::click);
            driverTools.switchToWebView();
            closeContinueOnWebPopup();
            driverTools.switchToNative();
            driverTools.waitForElementToAppear(ANDROID_ALLOW_THE_SITE, 15).ifPresent(driverTools::click);
            if (!driver.findElements(AppiumBy.androidUIAutomator("new UiSelector().textContains(\"While using the app\")")).isEmpty()) {
                driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().textContains(\"While using the app\")")).click();
            }
            driverTools.waitForWebview();
            driverTools.switchToWebView();
        } else if (Configs.PLATFORM == Platform.IMWEB) {
            driverTools.waitForElementToAppear(NEAR_ME);
            driverTools.clickWithJavascriptExecutor(NEAR_ME);
            driverTools.switchToNative();
            driverTools.waitForElementToPresence(IOS_ALLOW, 5).ifPresent(driverTools::click);
            driverTools.switchToWebView();
        } else {
            throw new NotImplementedException("clickNearMeLocation is not implemented for platform: " + Configs.PLATFORM);
        }
    }

    public abstract void inputSearchKeyword(String keyword);

    public void goToTtdLandingPage() {
        if (Configs.ENVIRONMENT == Environment.GK)
            driverTools.get(TTD_LANDING_PAGE_URL_GK);
        else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get(TTD_LANDING_PAGE_URL_PREPROD, 3);
        } else
            driverTools.get(TTD_LANDING_PAGE_URL_PROD);
        PopupObserver.inject(driver, List.of(APP_DOWNLOAD_POPUP));
    }

    public void clickDownloadApp() {
        PopupObserver.inject(driver, List.of(APP_DOWNLOAD_POPUP));
    }

    public boolean isUserLocationOff() {
        driverTools.waitForElementsToAppear(SEE_ALL_ACTIVITIES_NEARBY);
        boolean locationState = driverTools.waitForElementsToAppear(SEE_ALL_ACTIVITIES_NEARBY).isPresent();
        if (locationState){
            turnLocationOn();
            clickNearMeLocation();
        }else{
            driverTools.waitForElementToAppear(LOCATION_LABEL);
            locationState = driverTools.waitForElementToAppear(LOCATION_LABEL).isPresent();
        }
        return locationState;
    }

    public void turnLocationOn() {
        driverTools.waitForElementsToAppear(SEE_ALL_ACTIVITIES_NEARBY);
        driverTools.click(SEE_ALL_ACTIVITIES_NEARBY);
        driverTools.waitForElementsToAppear(ALLOW_ACCESS_BUTTON);
        driverTools.click(ALLOW_ACCESS_BUTTON);
    }

    public void openPartnerPage(String productName) {
        driverTools.get(PARTNER_PAGE_URL);
        String partnerName = driverTools.getText(driverTools.waitForElementToPresence(PRODUCT_TITLE_PARTNER_PAGE).orElseThrow());
        logger.log("Partner name: "+partnerName);
        Assertion.assertThat("Partner name is correct", partnerName.toLowerCase(), containsString(productName.toLowerCase()));
    }

    public List<String> getTopKeywordList() {
        driverTools.waitForElementToAppear(TOP_KEYWORD_TEXT,5);
        List<WebElement> listOfTopKeywordElements = driverTools.elements(TOP_KEYWORD_TEXT).orElseThrow();
        List<String> listOfTopKeyword = new ArrayList<>();
        for(int i = 0; i < listOfTopKeywordElements.size(); i++) {
            WebElement topKeywordElement = driverTools.element(getTopKeywordLocatorByIndex(i+1)).orElseThrow();
            String topKeywordText = driverTools.getText(topKeywordElement);
            listOfTopKeyword.add(topKeywordText);
        }
        logger.log("List->"+listOfTopKeyword);
        return listOfTopKeyword;
    }

    public void clickRefreshTopKeyword() {
        driverTools.click(REFRESH_TOP_KEYWORD);
    }

    public String getLocationLabel() {
        driverTools.waitForElementToPresence(LOCATION_LABEL);
        String label = driverTools.getText(LOCATION_LABEL).orElseThrow();
        logger.log("Got label "+label);
        return label;
    }

    public void openSearchInput() {
        driverTools.click(SEARCH_INPUT);
        if (Configs.PLATFORM != Platform.DWEB) {
            return;
        }

        if (driverTools.waitForElementToPresence(RECENTLY_VIEW_CARDS, 3).isPresent()) {
            int recentViewCardCount = driverTools.elements(RECENTLY_VIEW_CARDS).orElse(List.of()).size();
            logger.log("Recently viewed cards count before cleanup: " + recentViewCardCount);

            for (int i = 0; i < recentViewCardCount; i++) {
                if (!driverTools.waitForElementToPresence(RECENT_VIEW_CARD_CLOSE_BUTTON, 2).isPresent()) {
                    break;
                }
                driverTools.clickWithJavascriptExecutor(RECENT_VIEW_CARD_CLOSE_BUTTON);
            }
        }
    }

    public List<String> getGroupIconText() {
        driverTools.waitForElementToAppear(GROUP_ICON_BUTTON);
        List<WebElement> groupIcon = driverTools.elements(GROUP_ICON_BUTTON).orElseThrow();
        List<String> groupIconText = new ArrayList<>();
        for (WebElement icon : groupIcon) {
            groupIconText.add(driverTools.getText(icon));
        }
        return groupIconText;
    }

    public void clickGroupIcon(String text) {
        driverTools.waitForElementToAppear(GROUP_ICON_BUTTON);
        List<WebElement> groupIcon = driverTools.elements(GROUP_ICON_BUTTON).orElseThrow();

        WebElement selectedIcon = groupIcon.stream()
                .filter(icon -> driverTools.getText(icon).equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Group icon not found: " + text));
        driverTools.clickWithJavascriptExecutor(selectedIcon);
    }

    public String chooseCategoryIcon(String categoryPromo, String categoryWithoutPromo) {
        boolean isPromo = driverTools.waitForElementToAppear(CATEGORY_WITH_PROMO).isPresent();
        String categoryName = isPromo ? categoryPromo : categoryWithoutPromo;
        if (!isPromo) {
            logger.softWarning("No promo category found; selecting non-promo category.");
            driverTools.waitForElementToAppear(CATEGORY_WITHOUT_PROMO).orElseThrow();
        }
        return chooseCategory(isPromo, categoryName);
    }

    public String chooseCategory(boolean isPromo, String categoryName) {
        List<WebElement> categoryList = driverTools.elements(isPromo ? CATEGORY_WITH_PROMO : CATEGORY_WITHOUT_PROMO).orElseThrow();
        WebElement selectedCategory = categoryList.stream()
                .filter(category -> categoryName.equalsIgnoreCase(SortItem.RANDOM.toString()) || driverTools.getText(category).equalsIgnoreCase(categoryName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Category not found: " + categoryName));

        String result = driverTools.getText(selectedCategory);
        driverTools.clickWithJavascriptExecutor(selectedCategory);
        return result;
    }

    public String openAllItemIcon() {
        WebElement selectedCategory = driverTools.waitForElementToAppear(ALL_ITEM_ICON).orElseThrow();
        String result = driverTools.getText(selectedCategory);
        driverTools.clickWithJavascriptExecutor(selectedCategory);
        return result;
    }

    public boolean checkOpeningPopup(String itemName, String destinationItemList) {
        driverTools.waitForElementToAppear(TITLE_POPUP);
        List<String> expectedItems = Arrays.asList("all categories", "semua kategori", "all activities", "semua aktivitas");
        boolean isCategories = expectedItems.stream().anyMatch(expectedItem -> expectedItem.equalsIgnoreCase(itemName));
        if (isCategories) {
            Assertion.assertThat("Popup title is displayed", driverTools.getText(TITLE_POPUP).orElseThrow().toLowerCase(), is(oneOf(expectedItems.toArray(new String[0]))));
            Assertion.assertThat("Category name is displayed", driverTools.waitForElementsToAppear(CATEGORY_NAME_POPUP).isPresent(), is(true));
            Assertion.assertThat("Category chevron is displayed", driverTools.isElementDisplayed(CATEGORY_CHEVRON_POPUP), is(true));
            Assertion.assertThat("Subcategory icon is displayed", driverTools.isElementDisplayed(SUBCATEGORY_ICON_POPUP), is(true));
        } else if (itemName.equalsIgnoreCase("all destinations") || itemName.equalsIgnoreCase("semua destinasi")) {
            Assertion.assertThat("Destination search bar is displayed", driverTools.waitForElementsToAppear(SEARCH_BAR_POPUP).isPresent(), is(true));
            List<String> expectedSection = List.of(destinationItemList.split(" & "));
            driverTools.waitForElementToAppear(DESTINATION_SECTION_POPUP).orElseThrow();
            List<WebElement> destinationSection = driverTools.elements(DESTINATION_SECTION_POPUP).orElseThrow();
            int totalDestinationSection = destinationSection.size();
            Assertion.assertThat("Total destination section is correct", totalDestinationSection, is(expectedSection.size()));
            for (int i = 0; i < totalDestinationSection; i++) {
                Assertion.assertThat("Destination section is correct", driverTools.getText(destinationSection.get(i)), equalToIgnoringCase(expectedSection.get(i)));
            }
        } else {
            logger.softWarning("There is no handle for itemName '" + itemName + "'");
            return false;
        }
        driverTools.clickWithJavascriptExecutor(CROSS_POPUP);
        return true;
    }

    public String getRecentSearch() {
        driverTools.waitForElementToAppear(RECENT_SEARCH_ITEM);
        return driverTools.getText(RECENT_SEARCH_ITEM).orElseThrow();
    }

    public void selectFirstProductSuggestion() {
        driverTools.waitForElementToAppear(FIRST_SEARCH_PRODUCT_ITEM);
        driverTools.clickWithJavascriptExecutor(FIRST_SEARCH_PRODUCT_ITEM);
    }

    public boolean isRecentSearchDisplayed() {
        return driverTools.waitForElementToAppear(RECENT_SEARCH_ITEM).isPresent();
    }

    public void clickClearRecentSearch() {
        driverTools.waitForElementToAppear(CLEAR_ALL);
        driverTools.clickWithJavascriptExecutor(CLEAR_ALL);
    }

    public boolean isRecentSearchCleared() {
        return driverTools.waitForElementsToAppear(RECENT_SEARCH_ITEM, 3).isEmpty();
    }

    private Locator recentlyViewedCardByProduct(String product) {
        return new Locator("RECENTLY_VIEWED_CARD_BY_PRODUCT", new By.ByXPath("//div[@data-testid='recently-view']//a[contains(@class,'RecentlyViewCard_card_wrapper')][.//*[contains(normalize-space(.),\"" + product + "\")]]"));
    }

    public boolean isRecentlyViewedSectionDisplayed(String product) {
        driverTools.scrollToElement(recentlyViewedCardByProduct(product));
        return driverTools.waitForElementToAppear(recentlyViewedCardByProduct(product), 10).isPresent();
    }

    public boolean isProductInRecentlyViewedInSearchPopUp(String product) {
        driverTools.click(SEARCH_INPUT);
        driverTools.scrollToElement(recentlyViewedCardByProduct(product));
        return driverTools.waitForElementToAppear(recentlyViewedCardByProduct(product), 10).isPresent();
    }

    public boolean isAttractionPageOpened() {
        boolean flag = true;
        driverTools.waitForElementToAppear(ATTRACTION_BUTTON);
        if(!driverTools.isElementDisplayed(ATTRACTION_BUTTON)) flag = false;
        driverTools.click(ATTRACTION_BUTTON);
        return flag;
    }

    public void selectFirstSearchResult() {
        driverTools.waitForElementToAppear(FIRST_SEARCH_RESULT);
        if(driverTools.isElementDisplayed(FIRST_SEARCH_RESULT)) {
            driverTools.click(FIRST_SEARCH_RESULT);
        } else {
            logger.softWarning("First search result is not displayed");
        }
    }

    public abstract void search();

    public void clickSearch(){
        driverTools.click(SEARCH_BAR_TOP);
    }

    public boolean checkClickTopKeyword() {
        try {
            List<WebElement> topKeywords = driverTools.elements(TOP_KEYWORD_BUTTON).orElseThrow();
            WebElement topKeyword = CoreFunctions.getRandomObject(topKeywords);

            driverTools.click(topKeyword);
            return true;
        } catch (ElementClickInterceptedException e) {
            return false;
        } catch (NoSuchElementException e) {
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    public void inputSearchKeywordAttractionPage(String keyword) {
        String searchInput = "";
        if(keyword.contains("/")) {
            searchInput += keyword.split("/")[0];
        } else {
            searchInput += keyword;
        }
        driverTools.click(SEARCH_INPUT_ATTRACTION);
        driverTools.sendKeys(SEARCH_INPUT_ATTRACTION, searchInput);
    }

    public void selectRecommendedActivity(String keyword) {
        driverTools.waitForElementToAppear(SEARCH_ITEM);

        String keyword1, keyword2;
        if (keyword.contains("/")) {
            String[] keywords = keyword.split("/");
            keyword1 = keywords[0];
            keyword2 = keywords[1];
        } else {
            keyword1 = keyword;
            keyword2 = keyword;
        }

        String searchItem = driverTools.getText(SEARCH_ITEM).orElseThrow();
        if (searchItem.equalsIgnoreCase(keyword1) || searchItem.equalsIgnoreCase(keyword2)) {
            driverTools.click(SEARCH_ITEM);
        } else {
            List<WebElement> searchItems = driverTools.elements(SEARCH_ITEM).orElseThrow();
            for (WebElement searchItemElement : searchItems) {
                String searchItemText = driverTools.getText(searchItemElement);
                if (searchItemText.equalsIgnoreCase(keyword1) || searchItemText.equalsIgnoreCase(keyword2)) {
                    driverTools.scrollIntoWebView(searchItemElement);
                    driverTools.clickWithJavascriptExecutor(searchItemElement);
                    return;
                }
            }
            logger.warn("No " + keyword + " on the search item list, just getting " + searchItem);
        }
    }

    public List<String> redirectionSearchInput() {
        List<String> actualRedirection = new ArrayList<>();
        driverTools.waitForElementToPresence(TtdProductDetailPageWeb.PRODUCT_TITLE);
        actualRedirection.add(driverTools.getText(TtdProductDetailPageWeb.PRODUCT_TITLE).orElse("")); //if redirect to the product detail page
        actualRedirection.add(driverTools.getText(TtdSearchResultPageWeb.SEARCH_LOCATION).orElse("")); //if redirect to the search result page
        actualRedirection.add(driverTools.getText(TtdPartnerPageWeb.PARTNER_NAME).orElse("")); //if redirect to the partner page
        driverTools.waitForElementToPresence(TtdProductDetailPageWeb.PRODUCT_TITLE);
        actualRedirection.add(driverTools.getText(TtdProductDetailPageWeb.PRODUCT_TITLE).orElse("")); //if redirect to the product detail page
        actualRedirection.add(driverTools.getText(TtdSearchResultPageWeb.SEARCH_LOCATION).orElse("")); //if redirect to the search result page
        actualRedirection.add(driverTools.getText(TtdPartnerPageWeb.PARTNER_NAME).orElse("")); //if redirect to the partner page
        return actualRedirection;
    }

    public List<String> redirectionCategoryIcon() {
        List<String> actualRedirection = new ArrayList<>();
        if (driverTools.waitForElementToAppear(TtdSearchResultPageWeb.SEARCH_LOCATION).isPresent()) { //if redirect to the search result page
            TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
            Assertion.assertThat("Search location is displayed", ttdSearchResultPageWeb.getSearchLocation(), either(equalToIgnoringCase("all locations")).or(equalToIgnoringCase("semua lokasi")));
            actualRedirection.add(ttdSearchResultPageWeb.getSearchInputProduct());
            actualRedirection.add(ttdSearchResultPageWeb.getSelectedFilterOnly());
            List<String> campaignSrp = new ArrayList<>(driverTools.getTexts(TtdSearchResultPageWeb.PRODUCT_CAMPAIGN));
            actualRedirection.addAll(campaignSrp);
        }
        actualRedirection.add(driverTools.getText(TtdProductDetailPageWeb.PRODUCT_TITLE).orElse("")); //if redirect to the product detail page
        actualRedirection.add(driverTools.getText(TtdPartnerPageWeb.PARTNER_NAME).orElse("")); //if redirect to the partner page
        actualRedirection.add(driverTools.getText(TtdDestinationPageWeb.DESTINATION_NAME).orElse("")); //if redirect to the destination page
        actualRedirection.add(driverTools.getText(TtdProductDetailPageWeb.PRODUCT_TITLE).orElse("")); //if redirect to the product detail page
        actualRedirection.add(driverTools.getText(TtdPartnerPageWeb.PARTNER_NAME).orElse("")); //if redirect to the partner page
        actualRedirection.add(driverTools.getText(TtdDestinationPageWeb.DESTINATION_NAME).orElse("")); //if redirect to the destination page
        return actualRedirection;
    }

    private Locator createSearchItemAttractionLocator(String productName) {
        String formattedProductName = productName.trim();

        String xpath = String.format("(//a[contains(@class,'SearchItem_search_item')]//span[contains(@class,'SearchItem_search_item_title')]//span[text()='%s'])[1]", formattedProductName);

        return new Locator("SEARCH_ITEM_ATTRACTION", By.xpath(xpath));
    }


    public void selectRecommendedActivityAttractionPage(String keyword) {
        driverTools.waitForElementToAppear(createSearchItemAttractionLocator(keyword));
        String keyword1;
        String keyword2;
        if (keyword.contains("/")) {
            keyword1 = keyword.split("/")[0];
            keyword2 = keyword.split("/")[1];
        } else {
            keyword1 = keyword;
            keyword2 = keyword1;
        }
        String searchItem = driverTools.getText(createSearchItemAttractionLocator(keyword)).orElseThrow();
        if (searchItem.equalsIgnoreCase(keyword1) || searchItem.equalsIgnoreCase(keyword2)) {
            driverTools.click(createSearchItemAttractionLocator(keyword));
        } else {
            logger.warn("No " + keyword + " on the search item list, just getting " + searchItem);
        }
    }

    public boolean checkPopularEventsRank(int expectedTotalRank) {
        driverTools.waitForElementToPresence(POPULAR_EVENTS_RANK, 20);
        List<WebElement> toDoRank = driverTools.elements(POPULAR_EVENTS_RANK).orElseThrow();
        for (int i = 1; i <= expectedTotalRank; i++) {
            driverTools.scrollIntoWebView(toDoRank.get(i-1));
            driverTools.waitForElementToAppear(toDoRank.get(i-1)).orElseThrow();
            Assertion.assertThat("Popular events should have rank " + i, driverTools.getText(toDoRank.get(i-1)), is(String.valueOf(i)));
        }
        return true;
    }

    public abstract boolean checkPopularAttractionsRank(int expectedTotalRank);

    public abstract void clickSeeAllPopularAttractions();

    public void clickSeeAllNewActivites() {
        driverTools.waitForElementToAppear(NEW_ACTIVITIES_ITEM_SEE_ALL_BUTTON,3);
        driverTools.clickWithJavascriptExecutor(NEW_ACTIVITIES_ITEM_SEE_ALL_BUTTON);
    }

    public void clickSeeAllPopularDestination() {
        driverTools.waitForElementToAppear(POPULAR_DESTINATION_SEE_ALL_BUTTON,3);
        driverTools.clickWithJavascriptExecutor(POPULAR_DESTINATION_SEE_ALL_BUTTON);
    }

    public void clickSeeAllActivities() throws InterruptedException {
        driverTools.scroll(2,400);
        driverTools.clickWithJavascriptExecutor(SEE_ALL_ACTIVITIES);
    }

    public boolean checkAllLocationsBadge() {
        if(driverTools.isElementDisplayed(ALL_LOCATIONS)) {
            return true;
        }
        return false;
    }

    public String getSeeAllHomeParameterURL() {
        String url = driverTools.getCurrentURL();
        return url;
    }

    public boolean isFilterActivated(String filterType) {
        //verify general filter green icon is applied
        filterType = filterType.toLowerCase();
        driverTools.waitForElementToAppear(FILTER_BUTTON).orElseThrow();
        Boolean filter_badge = driverTools.isElementDisplayed(FILTER_BUTTON_BADGE);

        Boolean filter_type_badge;
        if (filterType.equals("filter")) {
            filter_type_badge = filter_badge;
        } else {
            //verify selected filter green icon is applied
            filter_type_badge = driverTools.isElementDisplayed(getFilterTypeBadgeLocator(filterType));
            logger.log("filter_badge : '" + filter_badge + "'\nfilter_type_badge : '" + filter_type_badge + "'");
        }
        //both filter green icon much exist
        return filter_badge && filter_type_badge;
    }

    private Locator getFilterTypeBadgeLocator(String filterType) {
        String english = "";
        String indonesian = "";
        switch (filterType) {
            case "price" -> {
                english = "Price";
                indonesian = "Harga";
            }
            case "star" -> {
                english = "Star";
                indonesian = "Bintang";
            }
            case "bedrooms" -> {
                english = "Bedrooms";
                indonesian = "Jumlah Kamar";
            }
            default -> {
                logger.fail("There is no handle for filterType '" + filterType + "'");
                throw new NoSuchMethodError();
            }
        }
        return new Locator(filterType.toUpperCase() + "FILTER_BUTTON_BADGE", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tv') and (contains(@text,'" + english + "') or contains(@text,'" + indonesian + "'))]//preceding-sibling::*[contains(@resource-id,'badge')]"));
    }

    public String selectPopularEvents(int rank) {
        driverTools.waitForElementToAppear(POPULAR_EVENTS_NAMES);
        List<WebElement> toDoRank = driverTools.elements(POPULAR_EVENTS_NAMES).orElseThrow();
        logger.log(String.valueOf(rank));
        driverTools.scrollIntoWebView(toDoRank.get(rank-1));
        driverTools.waitForElementToAppear(toDoRank.get(rank-1)).orElseThrow();
        String result = driverTools.getText(toDoRank.get(rank-1));
        logger.log("Selected event : "+result);
        driverTools.clickWithJavascriptExecutor(toDoRank.get(rank-1));
        return result;
    }

    public abstract void selectPopularAttractions(int rank);

    public abstract int getPopularDestinationsAmount();

    public int getNewActivitesAmount() {
        driverTools.waitForElementToAppear(NEW_ACTIVITIES_ITEM);
        List<WebElement> items = driverTools.elements(NEW_ACTIVITIES_ITEM).orElseThrow();
        return items.size();
    }

    public String selectActivityFromNewActivitySection() {
        List<WebElement> newActivities = driverTools.waitForElementsToAppear(NEW_ACTIVITIES_ITEM)
                .orElseThrow(() -> new NoSuchElementException("No activities found in the New Activity section"));
        String activityToBeSelected = newActivities.get(0).getText();
        driverTools.click(newActivities.get(0));
        return activityToBeSelected;
    }

    public abstract String selectPopularDestinationsWithLotsActivity();

    public abstract String selectPopularDestinations(String destination);

    public String clickRandomChipKeyword() {
        driverTools.waitForElementToPresence(KEYWORD_CHIPS);
        List<WebElement> keywords = driverTools.elements(KEYWORD_CHIPS).orElseThrow();

        logger.log("Size of List: "+ (keywords.size()));
        int randomNum = generateRandomInt(0, keywords.size()-1);
        String keyword = driverTools.getText(keywords.get(randomNum));
        driverTools.click(keywords.get(randomNum));
        return keyword;
    }

    public String getSearchBarInputValue() {
        return driverTools.getValue(SEARCH_INPUT);
    }

    public boolean verifySearchResultRelatedToSearchKeyword() {
        driverTools.waitForElementToPresence(SELECTED_KEYWORD_IN_SEARCH);
        List<WebElement> searchResults = driverTools.elements(SELECTED_KEYWORD_IN_SEARCH).orElseThrow();
        return searchResults.size()>0;
    }

    public boolean verifyDestinationListPage(){
        return driverTools.waitForElementToPresence(DESTINATION_LIST_PAGE).isPresent();
    }

    public boolean isPopularEventsPresent() {
        driverTools.waitForElementToAppear(POPULAR_EVENTS);
        Assertion.assertThat("Popular events section is present", driverTools.isElementDisplayed(POPULAR_EVENTS), is(true));
        return true;
    }

    public boolean isPopularAttractionPresent() {
        driverTools.waitForElementToAppear(POPULAR_ATTRACTION);
        Assertion.assertThat("Popular attractions section is present", driverTools.isElementDisplayed(POPULAR_ATTRACTION), is(true));
        return true;
    }

    public List<String> getPopularAttractionProductTitleOrder() {
        List<String> productTitleOrder = new ArrayList<>();
        driverTools.waitForElementsToAppear(ATTRACTION_ITEM);
        List<WebElement> attractionItems = driverTools.elements(ATTRACTION_ITEM).orElseThrow();
        int totalAttractionItems = attractionItems.size();
        logger.log("Total attraction items: " + totalAttractionItems);
        for (int i = 0; i < totalAttractionItems; i++) {
            WebElement attractionItem = attractionItems.get(i);
            driverTools.scrollIntoWebView(attractionItem);
            String productTitle = driverTools.getText(attractionItem);
            logger.log(i + " Product title: " + productTitle);
            productTitleOrder.add(productTitle.toLowerCase());
        }   
        return productTitleOrder;
    }

    public String selectAttractionProduct() throws InterruptedException {
        List<WebElement> attraction = driverTools.waitForElementsToAppear(ATTRACTION_ITEM)
                .orElseThrow(() -> new NoSuchElementException("No attractions found in the Popular Attractions section"));
        int randomIndex = generateRandomInt(0, attraction.size() - 1);
        logger.log("Random index: " + randomIndex);
        String attractionToBeSelected = driverTools.getText(attraction.get(randomIndex));
        driverTools.scrollIntoWebView(attraction.get(randomIndex));
        driverTools.clickWithJavascriptExecutor(attraction.get(randomIndex));
        return attractionToBeSelected;
    }

    public String selectEventProduct() throws InterruptedException {
        List<WebElement> event = driverTools.waitForElementsToAppear(EVENT_ITEM)
                .orElseThrow(() -> new NoSuchElementException("No events found in the Popular Event section"));
        int randomIndex = generateRandomInt(0, event.size() - 1);
        String eventToBeSelected = event.get(randomIndex).getText();
        String locator = CoreFunctions.getValueFromXPath(EVENT_ITEM);
        WebElement randomProduct = driver.findElement(By.xpath("(" + locator + ")[" + randomIndex + "]"));
        driverTools.scrollToElement(randomProduct);
        driverTools.scroll(2,5);
        driverTools.click(randomProduct);
        return eventToBeSelected;
    }

    public int checkEventsToDoRank() {
        driverTools.swipeToElement(SEE_ALL_EVENTS_BUTTON, TODO_LIST_CONTAINER, Swipe.UP, 3).orElseThrow();
        List<WebElement> toDoRank = driverTools.elements(TODO_RANK).orElseThrow();
        logger.log("Total products found: " + toDoRank.size()/4);
        return toDoRank.size()/4;
    }

    public int checkAttractionsToDoRank() {
        driverTools.swipeToElement(SEE_ALL_ATTRACTION_BUTTON, TODO_LIST_CONTAINER, Swipe.UP, 3).orElseThrow();
        List<WebElement> toDoRank = driverTools.elements(TODO_RANK).orElseThrow();
        logger.log("Total products found: " + toDoRank.size()/4);
        return toDoRank.size()/4;
    }

    public void clickSeeAllEventsButton(){
        driverTools.scrollIntoWebView(SEE_ALL_EVENTS_BUTTON);
        driverTools.swipeToElement(SEE_ALL_EVENTS_BUTTON, TODO_LIST_CONTAINER, Swipe.UP, 3).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEE_ALL_EVENTS_BUTTON);
    }

    public void clickSeeAllAttractionsButton(){
        driverTools.scrollIntoWebView(SEE_ALL_ATTRACTION_BUTTON);
        driverTools.swipeToElement(SEE_ALL_ATTRACTION_BUTTON, TODO_LIST_CONTAINER, Swipe.UP, 3).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEE_ALL_ATTRACTION_BUTTON);
    }

    public void navigateRecentlyViewedSection(){
        driverTools.waitForElementsToAppear(SEE_ALL_BUTTON);
        driverTools.click(SEE_ALL_BUTTON);
    }

    public void clickRecentItem(){
        driverTools.waitForElementsToAppear(FIRST_RECENTLY_VIEWED_ITEM);
        driverTools.click(FIRST_RECENTLY_VIEWED_ITEM);
    }

    public boolean isRecentItemOpened(String product) {
        driverTools.waitForElementsToAppear(SEO_TITLE);
        String titleText = String.valueOf(driverTools.getText(SEO_TITLE));
        return titleText.contains(product);
    }

    public abstract void searchProduct(String product);

    public void openSpecialPackages(){
        driverTools.waitForElementsToAppear(SHOW_ALL_PACKAGES_BUTTON,5);
        driverTools.click(SHOW_ALL_PACKAGES_BUTTON);

        driverTools.waitForElementsToAppear(SELECT_BUTTON);
        driverTools.clickWithJavascriptExecutor(SELECT_BUTTON);
    }

    public void setTicketQuantity(){
        driverTools.waitForElementsToAppear(ADULT_PLUS_BUTTON);
        driverTools.clickWithJavascriptExecutor(ADULT_PLUS_BUTTON);

        driverTools.waitForElementsToAppear(CHILD_PLUS_BUTTON);
        driverTools.clickWithJavascriptExecutor(CHILD_PLUS_BUTTON);
    }

    public void clickBookButton() {
        driverTools.waitForElementToAppear(BOOK_BUTTON);
        driverTools.clickWithJavascriptExecutor(BOOK_BUTTON);
    }

    public boolean verifyPOPUp(){
        driverTools.waitForElementToAppear(CLAIM_BUTTON);
        driverTools.clickWithJavascriptExecutor(CLAIM_BUTTON);

        driverTools.waitForElementToAppear(CLAIM_POPUP);
        return driverTools.isElementPresent(CLAIM_POPUP);
    }

    public void openProduct(){
        driverTools.waitForElementToAppear(PREPROD_AUTOMATION_TESTING);
        driverTools.clickWithJavascriptExecutor(PREPROD_AUTOMATION_TESTING);

        driverTools.waitForElementsToAppear(SHOW_ALL_PACKAGES_BUTTON,5);
        driverTools.clickWithJavascriptExecutor(SHOW_ALL_PACKAGES_BUTTON);
    }

    public void clickSelectAndBook(){
        driverTools.waitForElementToAppear(SELECT_SEATING_BUTTON);
        driverTools.clickWithJavascriptExecutor(SELECT_SEATING_BUTTON);

        clickBookButton();
    }

    public boolean isRefundLabelPresent(){
        return driverTools.isElementPresent(REFUND_ALLOWED_LABEL);
    }

    public boolean isSelectLabelPresent(){
        return driverTools.isElementPresent(SEAT_SELECTED_AUTO_LABEL);
    }

    public void detailAutoFill(){
        driverTools.waitForElementToAppear(CONTACT_DETAILS_LOGIN);
        driverTools.clickWithJavascriptExecutor(CONTACT_DETAILS_LOGIN);

        driverTools.waitForElementToAppear(RADIO_SALUTATION_MR);
        driverTools.clickWithJavascriptExecutor(RADIO_SALUTATION_MR);

        driverTools.waitForElementToAppear(COUNTRY_DROPDOWN);
        driverTools.clickWithJavascriptExecutor(COUNTRY_DROPDOWN);

        driverTools.scrollToElement(COUNTRY_INDONESIA);
        driverTools.clickWithJavascriptExecutor(COUNTRY_INDONESIA);

        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public void completeBooking(){
        driverTools.waitForElementToAppear(CONTINUE_TO_PAYMENT_BUTTON);
        driverTools.clickWithJavascriptExecutor(CONTINUE_TO_PAYMENT_BUTTON);
    }

    private Locator getRecentSearchProductChip(String product) {
        return new Locator("RECENT_SEARCH_PRODUCT_CHIP", new By.ByXPath("//button[contains(@class, 'RecentlySearched_chip')]//span[text()='" + product + "']"));
    }

    public boolean verifyProductInRecentSearch(String product) {
        driverTools.waitForElementToAppear(SEARCH_INPUT);
        driverTools.clickWithJavascriptExecutor(SEARCH_INPUT);

        Locator productChip = getRecentSearchProductChip(product);

        return driverTools.isElementPresent(productChip);
    }

    public void openProductViaURL(String product){
        String URL;
        switch (Configs.ENVIRONMENT) {
            case GK -> URL = "https://en.gatotkaca.tiket.com/to-do/" + product;
            case PREPROD -> URL = "https://preprod.tiket.com/to-do/" + product;
            case PROD -> URL = "https://www.tiket.com/to-do/" + product;
            default -> throw new IllegalStateException("Unsupported environment: " + Configs.ENVIRONMENT);
        }
        driverTools.openNewTabAndPasteUrl(URL);
    }

    public boolean isCampaignPresent(){
        driverTools.waitForElementToAppear(CAMPAIGN_LABEL);
        return driverTools.isElementPresent(CAMPAIGN_LABEL);
    }

    public void clickSelectSessionBTN (){
        driverTools.waitForElementToAppear(SELECT_SESSION_BTN);
        driverTools.clickWithJavascriptExecutor(SELECT_SESSION_BTN);
    }

    public String selectSessionDetails() {
        driverTools.waitForElementToAppear(TOMORROW_DATE_CARD);
        driverTools.clickWithJavascriptExecutor(TOMORROW_DATE_CARD);

        driverTools.waitForElementToAppear(SELECT_SESSION_DROPDOWN);
        driverTools.clickWithJavascriptExecutor(SELECT_SESSION_DROPDOWN);

        driverTools.waitForElementToAppear(SESSION_ITEM);
        String sessionInfo = driverTools.getText(SESSION_ITEM).orElse("");
        driverTools.clickWithJavascriptExecutor(SESSION_ITEM);

        return sessionInfo;
    }

    public boolean verifySessionDetailsOnBookingPage(String sessionDetails) {
        driverTools.waitForElementToAppear(BOOKING_PAGE_SESSION_INFO);
        String bookingPageSessionInfo = driverTools.getText(BOOKING_PAGE_SESSION_INFO).orElse("");
        logger.log("Booking page session info: " + bookingPageSessionInfo);
        return sessionDetails.contains(bookingPageSessionInfo);
    }

    public boolean isPartnerOpened(){
        driverTools.waitForElementToAppear(SEO_TITLE_PREPROD_ANNISA);
        return driverTools.isElementPresent(SEO_TITLE_PREPROD_ANNISA);
    }

    public void openPackageOptions(String options){
        driverTools.waitForElementToAppear(PACKAGE_OPTIONS);
        driverTools.clickWithJavascriptExecutor(PACKAGE_OPTIONS);

        selectPackageOptions(options);
    }

    public void selectPackageOptions(String options){
        driverTools.waitForElementToAppear(PACKAGE_OPTIONS_TITLE);
        Locator packageOptionLocator = getPackageOptionLocator(options);
        driverTools.waitForElementToAppear(packageOptionLocator);
        driverTools.clickWithJavascriptExecutor(packageOptionLocator);
    }

    private Locator getPackageOptionLocator(String packageName) {
        return new Locator("PACKAGE_OPTION", By.xpath("//button[contains(@class,'PackageGroupSelection_package_group_name') and contains(text(),'" + packageName + "')]"));
    }

    private Locator getPackageGroupHeaderLocator(String groupName) {
        return new Locator("PACKAGE_GROUP_HEADER", By.xpath("//div[contains(@class,'PackageGroup_wrapper')]//h2[contains(@class,'Text_size_h3') and text()='" + groupName + "']"));
    }

    public boolean isPackageGroupDisplayed(String groupName) {
        Locator packageGroupHeader = getPackageGroupHeaderLocator(groupName);
        driverTools.waitForElementToAppear(packageGroupHeader);
        return driverTools.isElementPresent(packageGroupHeader);
    }

    public boolean arePackagesDisplayed() {
        driverTools.waitForElementToAppear(PACKAGE_CARDS);
        return driverTools.isElementPresent(PACKAGE_CARDS);
    }

    private Locator getPartnerNameLocator(String partnerName) {
        return new Locator("PARTNER_NAME", By.xpath("//span[contains(@class,'SpecialInfo_partner_name') and text()='" + partnerName + "']"));
    }

    public void goToPartner(String partnerName){
        Locator partnerNameLocator = getPartnerNameLocator(partnerName);
        driverTools.waitForElementToAppear(partnerNameLocator);
        driverTools.clickWithJavascriptExecutor(partnerNameLocator);
    }

    public void clickNearestButton(){
        driverTools.waitForElementToAppear(NEAREST_BUTTON);
        driverTools.clickWithJavascriptExecutor(NEAREST_BUTTON);
    }

    public boolean enableGPSPopUPVisible(){
        driverTools.waitForElementToAppear(ENABLE_GPS_TITLE);
        return driverTools.isElementPresent(ENABLE_GPS_TITLE);
    }

    public void disableLocationAccess(){
        if (Configs.PLATFORM == Platform.AMWEB){
            driverTools.switchToNative();
            driverTools.waitForElementToPresence(NEVER_ALLOW, 10).ifPresent(driverTools::click);
            driverTools.waitForWebview();
            driverTools.switchToWebView();
        }
    }

    public void clickOkayButton(){
        driverTools.waitForElementToAppear(OKAY_BUTTON);
        driverTools.clickWithJavascriptExecutor(OKAY_BUTTON);
    }

    public boolean isDefaultPageDisplayed() {
        driverTools.waitForElementToAppear(FIRST_PRODUCT_CARD);
        return driverTools.isElementDisplayed(FIRST_PRODUCT_CARD);
    }

    public boolean verifyPromoSection() {
        boolean isPromoSectionDisplayed = driverTools.waitForElementToAppear(PROMO_SECTION).isPresent();
        boolean isPromoSectionClaimBtnDisplayed = driverTools.isElementDisplayed(PROMO_SECTION_CLAIM_BTN);
        logger.log("Promo Section is displayed: " + isPromoSectionDisplayed);
        logger.log("Promo Section Claim Button is displayed: " + isPromoSectionClaimBtnDisplayed);
        return isPromoSectionDisplayed && isPromoSectionClaimBtnDisplayed;
    }
}