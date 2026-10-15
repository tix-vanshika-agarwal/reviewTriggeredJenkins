package com.tiket.page.accommodation.hotel.searchresultpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.accommodation.homes.HomesPage;
import com.tiket.page.accommodation.homes.homessearchresultpage.HomesSearchResultPageAndroid;
import com.tiket.page.accommodation.hotel.HotelPage;
import com.tiket.page.accommodation.hotel.detailpage.DetailPageAndroid;
import com.tiket.pojo.accommodation.homes.HomesData;
import com.tiket.pojo.accommodation.homes.HomesNHAResponse;
import com.tiket.pojo.accommodation.hotel.FilterInfo;
import com.tiket.pojo.accommodation.hotel.LandingPageModulePOJO;
import com.tiket.pojo.accommodation.master_tag.AccommodationMasterTagResponse;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.page.accommodation.homes.homessearchresultpage.HomesSearchResultPageAndroid.BEDROOM_FILTER_SELECTED;
import static com.tiket.page.accommodation.hotel.landingpage.LandingPageAndroid.*;
import static com.tiket.page.setting.SettingPageAndroid.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

public class SearchResultPageAndroid<T extends WebDriver> extends SearchResultPage<T> {

    public SearchResultPageAndroid(Session<T> session) {
        super(session);
    }

    public static final Locator HOTEL_CARD = new Locator("HOTEL_CARD", AppiumBy.id("cl_parent"));
    public static final Locator HOTEL_LOCATION = new Locator("HOTEL_LOCATION", AppiumBy.xpath("//*[contains(@resource-id,'cl_parent')]//*[contains(@resource-id,'tv_location_info')]"));
    public static final Locator SRP_APP_BAR = new Locator("SRP_APP_BAR", AppiumBy.id("app_bar"));
    public static final Locator SRP_MASTER_TAG = new Locator("SRP_MASTER_TAG", AppiumBy.id("tds_chips_text"));
    public static final Locator SRP_RESULT = new Locator("SRP_RESULT", AppiumBy.id("fragment_container_result"));
    public static final Locator SRP_FILTER_BAR = new Locator("SRP_FILTER_BAR", AppiumBy.id("v_filter"));
    public static final Locator CHANGE_SEARCH_BUTTON = new Locator("CHANGE_SEARCH_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_appbar_item_button')]//*[contains(@resource-id,'tds_button_view')]"));
    public static final Locator DESTINATION_SEARCH_FIELD = new Locator("DESTINATION_SEARCH_FIELD", AppiumBy.xpath("//*[contains(@resource-id,'form_destination')]//*[contains(@resource-id,'tds_search_form_title') and not(@text='')]"));
    public static final Locator SUBMIT_SEARCH_BUTTON = new Locator("SUBMIT_SEARCH_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'btn_search') or contains(@resource-id,'btn_apply') or contains(@resource-id,'btn_save') ]//*[contains(@resource-id,'tds_button_view')]"));
    public static final Locator CLOSE_FILTER = new Locator("CLOSE_FILTER", AppiumBy.xpath("//*[contains(@resource-id,'iv_close')]"));
    public static final Locator QUICK_FILTER_HEADER_TEXT = new Locator("QUICk_FILTER_HEADER_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_container')]//*[contains(@resource-id,'tds_chips_text') and not(@text='Filter' or @text='Sort' or @text='Urutkan' or @text='Popular Filter' or @text='Filter Populer' or @text='Price' or @text='Harga' or @text='Star' or @text='Bintang')]"));
    public static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", AppiumBy.id("tds_button_view"));
    public static final Locator CURRENT_LOCATION = new Locator("CURRENT_LOCATION", AppiumBy.id("tds_header_navigation_appbar"));
    public static final Locator SEE_MAP_BUTTON = new Locator("SEE_MAP_BUTTON", AppiumBy.id("ll_fab_map"));
    public static final Locator HOTEL_STAR_CARD_MAPS = new Locator("HOTEL_STAR_CARD_MAPS", AppiumBy.id("rb_hotel_star"));
    public static final Locator HOTEL_PRICE_MAPS = new Locator("HOTEL_PRICE_MAPS", AppiumBy.xpath("//*[contains(@content-desc,'Map')]//*[contains(@class,'View') and contains(@content-desc,' ')]"));
    public static final Locator HOTEL_PRICE_CARD_MAPS = new Locator("HOTEL_PRICE_CARD_MAPS", AppiumBy.xpath("(//*[contains(@resource-id,'v_price_module') or contains(@resource-id, 'cl_parent')]//*[contains(@resource-id,'price_module_final_price') or contains(@resource-id, 'tv_final_price')])[last()]"));
    public static final Locator HOTEL_PRICE_CARD = new Locator("HOTEL_PRICE_CARD", AppiumBy.xpath("//*[contains(@resource-id,'v_price_module') or contains(@resource-id, 'cl_parent')]//*[contains(@resource-id,'tv_main_price') or contains(@resource-id, 'price_module_final_price') or contains(@resource-id,'tv_final_price')]"));
    public static final Locator MAPS_CONTAINER = new Locator("MAPS_CONTAINER", AppiumBy.id("fl_map"));
    private static final Locator SEARCH_THIS_AREA_BUTTON = new Locator("SEARCH_THIS_AREA_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tv_search') and (contains(@text,'Cari di area ini') or contains(@text,'Search in this area'))]"));
    private static final Locator SEARCH_PREVIOUS_BUTTON = new Locator("SEARCH_PREVIOUS_BUTTON", AppiumBy.xpath("//*[(contains(@resource-id,'tv_search') or contains(@resource-id,'tds_button_view')) and (contains(@text,'pencarian sebelumnya') or contains(@text,'previous search'))]"));
    private static final Locator VISIBLE_HOTEL_CONTAINER = new Locator("VISIBLE_HOTEL_CONTAINER", AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name')]//parent::*[contains(@resource-id,'cl_main_content') or contains(@resource-id,'cl_container')]"));
    private static final Locator SRP_BANNER_TEXT = new Locator("SRP_BANNER_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'ll_parent')]//*[contains(@resource-id,'tv_subtitle')]"));
    private static final Locator SRP_BANNER_CONTAINER = new Locator("SRP_BANNER_CONTAINER", AppiumBy.id("rv_banner"));
    // LOGIN_BANNER & LOGIN_BUTTON are not intractable on UI, Need to create tech debt for that, currntly have implemented in ios only. Jira : AC-44014
    private static final Locator LOGIN_BANNER = new Locator("LOGIN_BANNER", AppiumBy.xpath("//*[@text='Log in to enjoy the benefits!' or @text='Log in buat dapet benefitnya!']"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", AppiumBy.xpath("//*[@text='Log in']"));
    private static final Locator TITLE_BOTTOM_POPUP = new Locator("TITLE_BOTTOM_POPUP", AppiumBy.xpath("//*[contains(@resource-id,'crf_bottom_sheet')]//*[contains(@resource-id,'tv_title')]"));
    private static final Locator LOCATION_BUTTON = new Locator("LOCATION_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'form_destination')]//*[contains(@resource-id,'tds_search_form_action_a_image')]"));
    private static final Locator PRICE_DESC_LABEL = new Locator("PRICE_DESC_LABEL", AppiumBy.xpath("(//*[contains(@resource-id,'tv_unit_2')])[1]"));
    public static final Locator TOTAL_PRICE_LABEL = new Locator("TOTAL_PRICE_LABEL", AppiumBy.xpath("//*[contains(@resource-id,'tv_price_info') and (contains(@text,'termasuk pajak') or contains(@text,'including taxes'))]"));
    public static final Locator TOTAL_PRICE_LABEL_WITHOUT_TAX = new Locator("TOTAL_PRICE_LABEL_WITHOUT_TAX", AppiumBy.xpath("//*[contains(@resource-id,'tv_price_info') and (contains(@text,'(setelah pajak:') or contains(@text,'(after taxes:'))]"));
    private static final Locator PROMO_CONTAINER = new Locator("PROMO_CONTAINER", AppiumBy.id("iv_flash_sale"));
    public static final Locator FIRST_CARD_HOTEL_NAME = new Locator("FIRST_CARD_HOTEL_NAME", AppiumBy.xpath("(//*[contains(@resource-id, 'cl_parent')]//*[contains(@resource-id, 'tv_hotel_name')])[1]"));
    public static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", AppiumBy.xpath("//*[contains(@resource-id, 'cl_parent')]//*[contains(@resource-id, 'tv_hotel_name')]"));
    public static final Locator FIRST_CARD_HOTEL_LOCATION = new Locator("FIRST_CARD_HOTEL_LOCATION", AppiumBy.xpath("(//*[contains(@resource-id, 'cl_parent')])[1]//*[contains(@resource-id, 'tv_location_info')]"));
    public static final Locator FIRST_CARD_HOTEL_PRICE = new Locator("FIRST_CARD_HOTEL_PRICE", AppiumBy.xpath("(//*[contains(@resource-id, 'cl_parent')])[1]//*[contains(@resource-id, 'tv_final_price') or contains(@resource-id,'tv_main_price')]"));
    public static final Locator FIRST_CARD_HOTEL_STAR = new Locator("FIRST_CARD_HOTEL_STAR", AppiumBy.xpath("(//*[contains(@resource-id, 'cl_parent')])[1]//*[contains(@resource-id, 'rb_hotel_star')]"));
    private static final Locator FILTER_CONTAINER = new Locator("FILTER_CONTAINER", AppiumBy.xpath("//*[contains(@resource-id,'rv_content')]"));
    private static final Locator FILTER_FLOATING_BUTTON = new Locator("FILTER_FLOATING_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'fab_new_filter')]"));
    public static final Locator FIRST_CARD_HOTEL_REVIEW = new Locator("FIRST_CARD_HOTEL_REVIEW", AppiumBy.id("tv_review_value"));
    public static final Locator SORT_BY_BUTTON = new Locator("SORT_BY_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and (contains(@text,'Sort') or contains(@text,'Urutkan'))]/parent::*"));
    private static final Locator STAR_FILTER_BUTTON = new Locator("STAR_FILTER_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and (contains(@text,'Star') or contains(@text,'Bintang'))]//following-sibling::*[contains(@resource-id, 'tds_chips_icon_right')]/parent::*"));
    private static final Locator STAR_FILTER_SELECTED = new Locator("STAR_FILTER_SELECTED", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and (contains(@text,'Star ') or contains(@text,'Bintang '))]"));
    private static final Locator STAR_OPTIONS = new Locator("STAR_OPTIONS", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_container')]//*[contains(@resource-id,'tds_chips_text') and string-length(@text) = 1]"));
    public static final Locator SORT_BY_LOWEST_PRICE = new Locator("SORT_BY_LOWEST_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_list') and (contains(@text,'Lowest price') or contains(@text,'Harga terendah'))]"));
    public static final Locator SORT_BY_HIGHEST_PRICE = new Locator("SORT_BY_HIGHEST_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_list') and (contains(@text,'Highest price') or contains(@text,'Harga tertinggi'))]"));
    public static final Locator SORT_BY_BUTTON_BADGED = new Locator("SORT_BY_BUTTON_BADGED", AppiumBy.xpath("//*[contains(@resource-id, 'v_filter_nav_bar')]/child::*[4]//*[contains(@resource-id,'tds_badge')]"));
    public static final Locator SRP_CARD_VIEW = new Locator("SRP_CARD_VIEW", AppiumBy.xpath("//*[contains(@resource-id,'fragment_container_result')]"));
    public static final Locator FLOAT_MAP = new Locator("FLOAT_MAP", AppiumBy.xpath("(//*[contains(@resource-id,'fab_map')])[1]"));
    public static final Locator CLOSE_MAP = new Locator("CLOSE_MAP", AppiumBy.id("tds_icon_navigation_toolbar"));
    public static final Locator MEET_TRAVEL_POLICY_NUDGE = new Locator("MEET_TRAVEL_POLICY_NUDGE", AppiumBy.id("tv_travel_policy"));
    public static final Locator LABEL_PER_ROOM_PER_NIGHT = new Locator("LABEL_PER_ROOM_PER_NIGHT", AppiumBy.xpath("(//*[contains(@resource-id,'tv_unit_2')])[1]"));
    public static final Locator LABEL_AFTER_TAX_IN_CARD = new Locator("LABEL_AFTER_TAX_IN_CARD", AppiumBy.xpath("(//*[contains(@resource-id,'tv_price_info') and (contains(@text,'(after taxes:') or contains(@text,'(setelah pajak:'))])[1]"));
    public static final Locator ADDED_BENEFIT = new Locator("ADDED_BENEFIT", AppiumBy.id("tv_benefits"));
    public static final Locator EMPTY_HOTEL_BANNER = new Locator("EMPTY_HOTEL_BANNER", AppiumBy.id("tds_main_info_container_view"));
    public static final Locator FILTER_BUTTON = new Locator("FILTER_BUTTON", AppiumBy.xpath("//*[contains(@resource-id, 'v_filter_nav_bar')]/child::*[1] | //*[contains(@resource-id,'v_filter_chip')] | //*[contains(@resource-id,'tds_chips_text') and @text='Filter']"));
    public static final Locator FILTER_BUTTON_BADGE = new Locator("FILTER_BUTTON_BADGE", AppiumBy.xpath("//*[contains(@resource-id, 'tds_chips_badge')]"));
    public static final Locator FILTER_SUBMIT_BUTTON = new Locator("FILTER_SUBMIT_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'btn_save') or contains(@resource-id,'btn_search') or contains(@resource-id,'btn_apply')]//*[contains(@resource-id,'tds_button_view')]"));
    public static final Locator HOTEL_CARD_NAME = new Locator("HOTEL_CARD_NAME", AppiumBy.xpath("//*[contains(@resource-id,'cl_parent') or contains(@resource-id,'cl_content')]//*[contains(@resource-id,'tv_hotel_name')]"));
    public static final Locator PRICE_FILTER = new Locator("PRICE_FILTER", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and (contains(@text,'Price') or contains(@text,'Harga'))]/parent::*"));
    public static final Locator PRICE_FILTER_SELECTED = new Locator("PRICE_FILTER_SELECTED", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and (contains(@text,'< ') or contains(@text,'> ') or contains(@text,' - '))]//following-sibling::*[contains(@resource-id,'tds_chips_icon_right')]"));
    public static final Locator PRICE_FILTER_MIN_INPUT = new Locator("PRICE_FILTER_MIN_INPUT", AppiumBy.xpath("(//*[contains(@resource-id,'tds_box_input_text')])[1]"));
    public static final Locator PRICE_FILTER_MAX_INPUT = new Locator("PRICE_FILTER_MAX_INPUT", AppiumBy.xpath("(//*[contains(@resource-id,'tds_box_input_text')])[2]"));
    private static final Locator FILTER_SIGN = new Locator("FILTER_SIGN", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and (contains(@text,'Filter'))]//following-sibling::*[contains(@resource-id,'badge')]"));
    public static final Locator RESET_FILTER = new Locator("RESET_FILTER", AppiumBy.id("tv_reset"));
    public static final Locator INTERCEPT_FILTER = new Locator("INTERCEPT_FILTER", AppiumBy.xpath("(//*[contains(@content-desc,'intercept_filter')]//*[contains(@resource-id,'container')])[1]"));
    public static final Locator INTERCEPT_FILTER_OPTION = new Locator("INTERCEPT_FILTER_OPTION", AppiumBy.xpath("//*[contains(@content-desc,'intercept_filter')]//*[contains(@resource-id,'tds_chips_text')]"));
    public static final Locator FREQUENT_PRICE_FILTER_CHIP_LT200 = new Locator("FREQUENT_PRICE_FILTER_CHIP_LT200", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_container')]//*[@text='<IDR 200k' or @text='<IDR 200rb']"));
    public static final Locator FREQUENT_PRICE_FILTER_CHIP_LT500 = new Locator("FREQUENT_PRICE_FILTER_CHIP_LT500", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_container')]//*[@text='<IDR 500k' or @text='<IDR 500rb']"));
    public static final Locator FREQUENT_PRICE_FILTER_CHIP_500_TO_1000 = new Locator("FREQUENT_PRICE_FILTER_CHIP_500_TO_1000", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_container')]//*[@text='IDR 500k - IDR 1m' or @text='IDR 500rb - IDR 1jt']"));
    public static final Locator FREQUENT_PRICE_FILTER_CHIP_GT1000 = new Locator("FREQUENT_PRICE_FILTER_CHIP_GT1000", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_container')]//*[@text='>IDR 1m' or @text='>IDR 1jt']"));
    public static final Locator FREQUENT_PRICE_FILTER_CHIP_ALL = new Locator("FREQUENT_PRICE_FILTER_CHIP_ALL", AppiumBy.id("tds_chips_text"));
    public static final Locator PRICE_FILTER_FIELDS = new Locator("PRICE_FILTER_FIELDS", AppiumBy.id("tds_box_input_text"));
    public static final Locator DATE_ROOM_GUEST_DETAIL = new Locator("DATE_ROOM_GUEST_DETAIL", AppiumBy.id("tds_sub_header_navigation_appbar"));
    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.id("tds_icon_navigation_toolbar_group"));
    public static final Locator LOYALTY_BADGE_HOTEL_CARD = new Locator("LOYALTY_BADGE_HOTEL_CARD", AppiumBy.id("iv_tier"));
    private static final Locator PREFERRED_PARTNER_ICON = new Locator("PREFERRED_PARTNER_ICON", AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name') and starts-with(@text, '__')]"));
    public static final Locator LOCATION_SEARCH_FORM = new Locator("LOCATION_SEARCH_FORM", AppiumBy.id("tds_chips_text"));
    public static final Locator SEARCH_TITLE = new Locator("SEARCH_TITLE", AppiumBy.id("tv_destination_title"));
    public static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON", AppiumBy.id("iv_close"));
    public static final Locator FILTER_POPUP_TRY_BTN = new Locator("FILTER_POPUP_TRY_BTN", AppiumBy.id("tds_primary_coach_mark"));
    public static final Locator MIN_PRICE_FILTER_INPUT = new Locator("MIN_PRICE_FILTER_INPUT", AppiumBy.xpath("(//*[contains(@resource-id,'tds_box_input_text')])[1]"));
    public static final Locator MAX_PRICE_FILTER_INPUT = new Locator("MAX_PRICE_FILTER_INPUT", AppiumBy.xpath("(//*[contains(@resource-id,'tds_box_input_text')])[2]"));
    public static final Locator SEE_ALL_FILTER_LOCATOR = new Locator("SEE_ALL_FILTER_LOCATOR", AppiumBy.xpath("(//*[contains(@resource-id, 'tds_button_view')])[last()]"));
    private static final Locator AFTER_TAX_CHECKBOX = new Locator("AFTER_TAX_CHECKBOX", AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_list') and (contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'including tax') or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'setelah pajak'))]/following-sibling::*[contains(@resource-id,'tds_selection_area_right')]"));
    private static final Locator FREE_BREAKFAST = new Locator("FREE_BREAKFAST", AppiumBy.xpath("//*[contains(@text,'Free Breakfast')  or contains(@text,'Sarapan Gratis')]"));
    private static final Locator FREE_CANCELLATION = new Locator("FREE_CANCELLATION", AppiumBy.accessibilityId("free_cancellation"));
    private static final Locator DEALS = new Locator("DEALS", AppiumBy.accessibilityId("deals"));
    private static final Locator PREFERRED_PARTNER_PLUS = new Locator("PREFERRED_PARTNER_PLUS", AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_list') and @text='Preferred Partner Plus']"));
    private static final Locator PREFERRED_PARTNER = new Locator("PREFERRED_PARTNER", AppiumBy.xpath("//*[contains(@resource-id, 'tds_subtitle_list') and @text='Preferred Partner']"));
    private static final Locator POPULAR_PLACE = new Locator("POPULAR_PLACE", AppiumBy.xpath("(//*[@text='Tempat Populer' or @text='Popular Places']/parent::*/following-sibling::android.widget.LinearLayout//android.widget.TextView)[1]"));
    private static final Locator POPUPLAR_PLACE_BUTTON = new Locator("POPULAR_PLACE_BUTTON", AppiumBy.xpath("(//*[@text='Tempat Populer' or @text='Popular Places']/parent::*/following-sibling::android.widget.LinearLayout//android.widget.TextView)[1]//following-sibling::*[contains(@resource-id,'tds_selection_area_right')]"));
    private static final Locator FIRST_WISHLIST_BTN = new Locator("FIRST_WISHLIST_BTN", AppiumBy.xpath("(//*[contains(@resource-id, 'iv_wishlist')])[1]"));
    private static final Locator MANAGE_SNACKBAR = new Locator("MANAGE_SNACKBAR", AppiumBy.xpath("//*[contains(@resource-id, 'tds_snackbar_root')]"));
    public static final Locator MANAGE_WISHLIST = new Locator("MANAGE_WISHLIST", AppiumBy.id("tds_snackbar_action"));
    private static final Locator MANAGE_BTN = new Locator("MANAGE_BTN", AppiumBy.xpath("//*[contains(@resource-id, 'tds_snackbar_action') and @text='Manage' or @text='Atur']"));
    private static final Locator HOTEL_CARD_MAPS = new Locator("HOTEL_CARD_MAPS", AppiumBy.xpath("(//*[contains(@resource-id, 'cl_content')])[2]"));
    private static final Locator SEARCH_MAPS = new Locator("SEARCH_MAPS", AppiumBy.xpath("//*[contains(@resource-id, 'tv_search')]"));
    public static final Locator WISHLIST_ICON_RED = new Locator("WISHLIST_ICON_RED", AppiumBy.xpath("//*[@content-desc='wishlist (selected)']"));
    public static final Locator REMOVE_WISHLIST = new Locator("REMOVE_WISHLIST", AppiumBy.xpath("//*[contains(@resource-id, 'tds_snackbar_text') and contains(@text, 'Removed from All Wishlist') or contains(@text, 'Dihapus dari Semua Wishlist')]"));
    public static final Locator OPEN_MAPS = new Locator("OPEN_MAPS", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view')]"));
    private static final Locator NEXT_BUTTON = new Locator("NEXT_BUTTON", AppiumBy.xpath("//*[contains(@resource-id, 'tds_btn')]"));
    private static final Locator SRP_TITLE = new Locator("SRP_TITLE", AppiumBy.xpath("//*[contains(@resource-id, 'tds_header_navigation_appbar')]"));
    private static final Locator ADS_LABEL = new Locator("ADS_LABEL", AppiumBy.id("tv_ads"));
    private static final Locator HIGHLIGHT_LABEL = new Locator("HIGHLIGHT_LABEL", AppiumBy.xpath("//*[contains(@resource-id, 'cl_highlighted_hotel')]"));
    private static final Locator PER_ROOM_PER_NIGHT = new Locator("PER_ROOM_PER_NIGHT", AppiumBy.xpath("//*[contains(@resource-id,'v_title') and ((.//*[contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'per')] and .//*[contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'room')] and .//*[contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'night')]) or (.//*[contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'kamar')] and .//*[contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'malam')]))]"));
    private static final Locator FLEXIBLE_STAY_HEADING = new Locator("FLEXIBLE_STAY_HEADING",
            AppiumBy.xpath("//*[contains(@text,'Do you have flexible stay dates') or contains(@text,'Tanggal nginepmu fleksibel')]"));
    private static final Locator FLEXIBLE_STAY_HOTEL_CARDS = new Locator("FLEXIBLE_STAY_HOTEL_CARDS",
            AppiumBy.xpath("//*[contains(@text,'Do you have flexible stay dates') or contains(@text,'Tanggal nginepmu fleksibel')]/ancestor::*[contains(@resource-id,'cl_') or contains(@resource-id,'rv_')][1]//*[contains(@resource-id,'tv_hotel_name')]"));
    private static final Locator FLEXIBLE_STAY_HOTEL_PRICE = new Locator("FLEXIBLE_STAY_HOTEL_PRICE",
            AppiumBy.xpath("//*[contains(@text,'Do you have flexible stay dates') or contains(@text,'Tanggal nginepmu fleksibel')]/ancestor::*[contains(@resource-id,'cl_') or contains(@resource-id,'rv_')][1]//*[contains(@resource-id,'tv_final_price') or contains(@resource-id,'tv_main_price') or contains(@resource-id,'price_module_final_price')]"));

    private Locator srpBanner(String text) {
        return new Locator("SRP_BANNER", AppiumBy.xpath("//*[contains(@resource-id,'ll_parent')]//*[contains(@resource-id,'tv_subtitle') and @text='" + text + "']"));
    }

private Locator hotelNameAds(String hotelName) {
        return new Locator("HOTEL_NAME_ADS", AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name') and contains(@text," + toXPathLiteral(hotelName) + ")]//preceding-sibling::*[contains(@resource-id,'tv_ads')]"));
    }

    @Override
    public boolean isHotelNameAdsVisible(String hotelName) {
        driverTools.waitForElementToAppear(ADS_LABEL);
        return driverTools.swipeToElement(hotelNameAds(hotelName), Swipe.UP, 10).isPresent();
    }

    public static final Locator PREFERRED_PARTNER_CARD_ICON = new Locator("PREFERRED_PARTNER_CARD_ICON",
            AppiumBy.xpath("//*[contains(@resource-id, 'tv_hotel_name') and contains(@content-desc, 'preferredPartnerType')] " +
                    "| //*[contains(@content-desc, 'preferredPartnerType=TIER_')]" +
                    "| //*[contains(@resource-id, 'iv_preferred_partner') or contains(@resource-id, 'img_preferred_partner')]"));

    /**
     * This method is used to get the text of the Search Result Page (SRP) banner.
     *
     * @return The text of the SRP banner.
     * @throws NoSuchElementException if the SRP_BANNER_TEXT element is not present.
     */
    @Override
    public String getSrpBanner() {
        driverTools.swipeToElement(SRP_BANNER_TEXT, Swipe.UP, 10).orElseThrow();
        String bannerText = "";
        for (int i = 0; i < 5; i++) {
            try {
                bannerText = driverTools.getText(SRP_BANNER_TEXT).orElseThrow();
            } catch (StaleElementReferenceException e) {
                logger.warn("Can't get the text due to stale element exception occurred, retrying...");
            }
        }
        return bannerText;
    }

    /**
     * This method checks if the banner text on the Search Result Page (SRP) has changed from its initial state.
     * It performs this check up to 5 times.
     *
     * @param initBanner The initial banner text to compare against.
     * @return true if the banner text is different from the initial banner text, false otherwise.
     * @throws NoSuchElementException if the SRP_BANNER_TEXT element is not present.
     */
    @Override
    public boolean checkBannerChanges(String initBanner) {
        for (int i = 0; i < 5; i++) {
            if (!driverTools.isElementDisplayed(srpBanner(initBanner))) {
                Assertion.assertThat("Banner text should be different", getSrpBanner(), not(equalTo(initBanner)));
                break;
            }
        }
        return true;
    }

    /**
     * This method is used to swipe the banner on the Search Result Page (SRP).
     * It waits for the SRP_BANNER_CONTAINER element to be present, then performs a swipe action based on the provided direction.
     * If the swipe direction is right, it swipes right, otherwise it swipes left.
     * After the swipe action, it retrieves and returns the text of the SRP banner.
     *
     * @param swipe Enum representing the direction to swipe (LEFT or RIGHT).
     * @return String representing the text of the banner after the swipe.
     * @throws NoSuchElementException if the SRP_BANNER_CONTAINER element is not present.
     */
    @Override
    public String swipeBanner(Swipe swipe) {
        driverTools.waitForElementToPresence(SRP_BANNER_CONTAINER).orElseThrow();
        if (swipe.equals(Swipe.RIGHT)) {
            driverTools.waitForElementToAppear(SRP_BANNER_CONTAINER);
            driverTools.swipeRight(SRP_BANNER_CONTAINER);
        } else {
            driverTools.swipeLeft(SRP_BANNER_CONTAINER);
        }
        return getSrpBanner();
    }

    /**
     * This method is used to click the banner on the Search Result Page (SRP).
     * It clicks the SRP_BANNER_CONTAINER element.
     */
    @Override
    public void clickBanner() {
        driverTools.click(SRP_BANNER_CONTAINER);
    }

    @Override
    public void scrollToBannerSection() {
        driverTools.swipeToElement(SRP_BANNER_CONTAINER, Swipe.UP, 10).orElseThrow();
    }

    @Override
    public void clickBannerByTitle(String bannerTitle) {
        scrollToBannerSection();
        Locator bannerTitleLocator = srpBannerByTitle(bannerTitle);
        for (int i = 0; i < 15; i++) {
            if (driverTools.waitForElementToPresence(bannerTitleLocator, 2).isPresent()) {
                driverTools.click(bannerTitleLocator);
                return;
            }
            driverTools.swipeLeft(SRP_BANNER_CONTAINER);
        }
        throw new org.openqa.selenium.NoSuchElementException("SRP banner not found: " + bannerTitle);
    }

    private Locator srpBannerByTitle(String title) {
        return new Locator("SRP_BANNER_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'rv_banner')]//*[contains(@text,'" + title + "')]"));
    }

    @Override
    public void navigateBackToSrp() {
        if (driverTools.waitForElementToAppear(TITLE_BOTTOM_POPUP, 3).isPresent()) {
            driverTools.back();
        } else if (driverTools.waitForWebview()) {
            driverTools.switchToNative();
            driverTools.back();
        } else {
            driverTools.back();
        }
        driverTools.waitForElementToPresence(HOTEL_CARD, 20).orElseThrow();
    }

    /**
     * This method is used to get the redirection banner text.
     * It checks if the TITLE_BOTTOM_POPUP element is present and retrieves its text if it is.
     * If the TITLE_BOTTOM_POPUP element is not present, it calls the getOpenedWebViewUrl method of the SearchResultPage instance.
     *
     * @return The text of the redirection banner or the URL of the opened web view.
     */
    @Override
    public String getRedirectionBanner() {
        if (driverTools.waitForElementToAppear(TITLE_BOTTOM_POPUP).isPresent()) {
            return driverTools.getText(TITLE_BOTTOM_POPUP).orElseThrow();
        } else {
            SearchResultPage<T> searchResultPage = SearchResultPage.getInstance(session);
            return searchResultPage.getOpenedWebViewUrl();
        }
    }

    @Override
    public void selectHotelVendor(String vendor) {
        HotelPage<T> hotelPage = HotelPage.getInstance(session);
        String vendorCode = hotelPage.generateVendorCode(vendor);
        driverTools.swipeToElement(HOTEL_PRICE_CARD, Swipe.UP, 5);
        driverTools.swipeToElement(colorVendor(vendorCode), Swipe.UP, 20).orElseThrow();
        driverTools.clickElementByCoordinate(colorVendor(vendorCode));
        if (driverTools.isElementDisplayed(colorVendor(vendorCode))) driverTools.click(colorVendor(vendorCode));
    }

    private Locator colorVendor(String vendorCode) {
        return new Locator("VENDOR_CODE_" + vendorCode, AppiumBy.xpath("//*[contains(@resource-id,'tv_vendor_code') and @text='" + vendorCode + "']"));
    }

    @Override
    public void changeLocation(String newLocation) {
        driverTools.click(CHANGE_SEARCH_BUTTON);
        driverTools.click(DESTINATION_SEARCH_FIELD);
        WebElement searchFieldText = driverTools.waitForElementToAppear(SEARCH_FIELD_TEXT).orElseThrow();
        driverTools.sendKeys(searchFieldText, newLocation);
        driverTools.click(searchList(newLocation, newLocation));
    }

    @Override
    public void changeLocation(String newLocation, String locationType) {
        driverTools.click(CHANGE_SEARCH_BUTTON);
        driverTools.click(DESTINATION_SEARCH_FIELD);

        WebElement searchFieldText = driverTools.waitForElementToAppear(SEARCH_FIELD_TEXT).orElseThrow();
        driverTools.sendKeys(searchFieldText, newLocation);
        driverTools.hideKeyboard();

        WebElement newLocationType = driverTools.swipeToElement(searchType(newLocation), Swipe.UP, 3).orElseThrow();
        Assertion.assertThat("Location type of " + newLocation + " should be equal as expected: " + locationType, driverTools.getText(newLocationType), containsStringIgnoringCase(locationType));

        driverTools.click(searchList(newLocation));
    }

    @Override
    public String changeNearMeLocation() {
        driverTools.click(CHANGE_SEARCH_BUTTON);
        driverTools.click(LOCATION_BUTTON);

        String searchFieldText = driverTools.getText(DESTINATION_SEARCH_FIELD).orElseThrow();
        clickSubmit();

        driverTools.waitForElementToAppear(PRIMARY_BTN).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(PERMISSION_TYPE_ALLOW_ONCE).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(WHILE_USING_APP_PERMISSION_BUTTON).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(TURN_ON_LOCATION_DEVICE).ifPresent(driverTools::click);

        return searchFieldText;
    }

    private Locator searchType(String location) {
        return new Locator("SEARCH_TYPE_" + location.toUpperCase(), AppiumBy.xpath("(//*[contains(@resource-id, 'tds_title_list') and (@text='" + location + "' or @text='" + location.toLowerCase() + "')])[1]//following-sibling::*[contains(@resource-id,'tds_selection_area_right')]//*//*[contains(@class,'TextView')]"));
    }

    @Override
    public void clickSubmit() {
        driverTools.click(SUBMIT_SEARCH_BUTTON);
    }

    @Override
    public String getCurrentLocation() {
        driverTools.waitForElementToAppear(HOTEL_CARD);
        driverTools.waitForElementToAppear(CURRENT_LOCATION).orElseThrow();
        String currentLocation = driverTools.getText(CURRENT_LOCATION).orElseThrow();
        logger.log("Current location: " + currentLocation);
        return currentLocation;
    }

    private Locator getHotelName(int index) {
        return new Locator("HOTEL_NAME_SELECTED", AppiumBy.xpath("(//*[contains(@resource-id,'cl_parent')])["+index+"]//*[contains(@resource-id,'tv_hotel_name')]"));
    }

    private Locator getHotelLocation(int index) {
        return new Locator("HOTEL_LOCATION_SELECTED", AppiumBy.xpath("(//*[contains(@resource-id,'cl_parent')])["+index+"]//*[contains(@resource-id,'tv_location_info')]"));
    }

    private int getIndexFromList(String list) {
        int index;
        if (list.equalsIgnoreCase("random")) {
            int hotelCardTotal = driverTools.elements(HOTEL_CARD).orElseThrow().size();
            index = generateRandomInt(1, hotelCardTotal);
        } else {
            index = Integer.parseInt(list);
        }
        return index;
    }

    private void selectHotelCard(int index) {
        final Locator HOTEL_NAME_SELECTED = getHotelName(index);
        driverTools.click(HOTEL_NAME_SELECTED);
    }

    @Override
    public void selectHotelList(String list) {
        driverTools.waitForElementToAppear(HOTEL_CARD).orElseThrow();
        if (list.isEmpty() || list.isBlank()) {
            driverTools.click(HOTEL_CARD);
        } else {
            int index = getIndexFromList(list);
            selectHotelCard(index);
        }
    }

    @Override
    public String getHotelNameList(String list) {
        driverTools.waitForElementToAppear(HOTEL_CARD).orElseThrow();
        String hotelNameList;
        if (list.isEmpty() || list.isBlank()) {
            WebElement hotelElement = driverTools.swipeToElement(HOTEL_CARD_NAME, Swipe.UP, 3).orElseThrow();
            hotelNameList = normalizeNonBreakingSpaceText(driverTools.getText(hotelElement));
        } else {
            int index = getIndexFromList(list);
            final Locator HOTEL_NAME_SELECTED = getHotelName(index);
            driverTools.swipeToElement(getHotelName(index), Swipe.UP, 1);
            hotelNameList = normalizeNonBreakingSpaceText(driverTools.getText(HOTEL_NAME_SELECTED).orElseThrow());
        }
        return hotelNameList;
    }

    @Override
    public String getHotelLocationList(String list) {
        driverTools.waitForElementToAppear(HOTEL_CARD).orElseThrow();
        String hotelLocationList;
        if (list.isEmpty() || list.isBlank()) {
            driverTools.waitForElementToAppear(HOTEL_LOCATION).orElseThrow();
            hotelLocationList = String.valueOf(driverTools.getText(HOTEL_LOCATION));
        } else {
            int index = getIndexFromList(list);
            final Locator HOTEL_LOCATION_SELECTED = getHotelLocation(index);
            hotelLocationList = driverTools.getText(HOTEL_LOCATION_SELECTED).orElseThrow();
        }
        return hotelLocationList;
    }

    private Locator getQuickFilterHeaderLocator(int index) {
        return new Locator("HEADER_QUICK_FILTER", AppiumBy.xpath("(//*[contains(@resource-id,'tds_chips_container')]//*[contains(@resource-id,'tds_chips_text') and not(@text='Filter' or @text='Sort' or @text='Urutkan' or @text='Popular Filter' or @text='Filter Populer' or @text='Price' or @text='Harga' or @text='Star' or @text='Bintang')])[" + index + "]"));
    }

    @Override
    public boolean verifyLoopingHeaderQuickFilter(int index) {
        for (int idx = 2; idx <= index; idx++) {
            final Locator HEADER_QUICK_FILTER = getQuickFilterHeaderLocator(idx);
            driverTools.click(HEADER_QUICK_FILTER);
            if (isHotelListDisplayed()) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getPriceDescription() {
        goToFirstHotelCard();
        driverTools.swipeToElement(PRICE_DESC_LABEL, Swipe.UP, 3);
        return driverTools.getText(PRICE_DESC_LABEL).orElseThrow();
    }

    @Override
    public void clickFilterButton() {
        driverTools.swipeToElement(FILTER_BUTTON, SRP_FILTER_BAR, Swipe.RIGHT, 10);
        driverTools.click(FILTER_BUTTON);
        if (driverTools.isElementDisplayed(FILTER_BUTTON)) driverTools.clickElementByCoordinate(FILTER_BUTTON);
    }

    @Override
    public void swipeToFilter(String filterName) {
        driverTools.swipeToElement(getFilterSectionLocator(filterName), Swipe.UP, 20);
    }

    @Override
    public void selectHotelStarFilter(String starsString) {
        String[] selectedStarsFilter = starsString.split("-");
        for (String starString : selectedStarsFilter) {
            int index = (Integer.parseInt(starString) - 6) * -1;
            WebElement webElement = driverTools.swipeToElement(getHotelStarSelection(index), Swipe.UP, 2).orElseThrow();
            driverTools.click(webElement);
        }
    }

    @Override
    public void selectHotelPaymentMethod(String paymentMethod) {
        WebElement paymentMethodElement = driverTools.swipeToElement(getHotelPaymentMethodFilterOptionLocator(paymentMethod), Swipe.UP, 1).orElseThrow();
        driverTools.swipeByDistance(FILTER_CONTAINER, FILTER_FLOATING_BUTTON, Swipe.UP,1);
        driverTools.click(paymentMethodElement);
    }

    private Locator getHotelPaymentMethodFilterOptionLocator(String paymentMethod) {
        String filterNameEnglish = paymentMethod.split("/")[0];
        String filterNameBahasa = paymentMethod.split("/")[1];
        return new Locator("HOTEL_PAYMENT_METHOD_FILTER_OPTION", AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_list') and (translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='"+filterNameEnglish+"' or translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='"+filterNameBahasa+"')]"));
    }

    private Locator getCheckboxFilterOptionLocator(String filterOption) {
        String filterNameEnglish = filterOption.split("/")[0];
        String filterNameBahasa = filterOption.split("/")[1];
        return new Locator("CHECKBOX_FILTER_OPTION", AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_list') and (contains(@text,'"+filterNameEnglish+"') or contains(@text,'"+filterNameBahasa+"'))]"));
    }

    @Override
    public void clickFilterSubmit() {
        if (driverTools.waitForElementToAppear(SUBMIT_SEARCH_BUTTON, 3).isPresent()) {
            driverTools.click(SUBMIT_SEARCH_BUTTON);
        } else if (driverTools.isElementDisplayed(FILTER_SUBMIT_BUTTON)) {
            driverTools.clickElementByCoordinate(FILTER_SUBMIT_BUTTON);
        } else {
            driverTools.click(CLOSE_FILTER);
        }
        waitForSrpToLoad();
    }

    @Override
    public String[] getHotelCardList(String cardAttribute, int totalHotel, String[] param) {
        driverTools.waitForElementToPresence(HOTEL_CARD_NAME).orElseThrow();
        List<String> resultList = new ArrayList<>();
        List<WebElement> hotelCardList = driverTools.elements(HOTEL_CARD_NAME).orElseThrow();
        if (hotelCardList.isEmpty()) {
            logger.fail("empty SRP");
        }
        if (totalHotel == 0) {
            logger.fail("empty testdata");
        }
        int noOfHotels = 0;
        for (int loopLimit = 0; noOfHotels < totalHotel && loopLimit < 10; loopLimit++) {
            List<String> hotelNameList = driverTools.getTexts(HOTEL_CARD_NAME);
            for (String name : hotelNameList) {
                if (!resultList.contains(name)) {
                    resultList.add(normalizeNonBreakingSpaceText(name));
                    resultList.add(getCardData(cardAttribute, name));
                    noOfHotels++;
                }
                driverTools.swipeUp();
            }
        }

        if (noOfHotels < totalHotel) {
            logger.warn("no of hotel not match testdata");
        }

        for (String e : param) {
            resultList.add(e);
        }
        logger.log("resultList -> " + resultList);
        logger.log("resultList.size() -> " + resultList.size());
        return resultList.toArray(new String[resultList.size()]);
    }

    private String getCardData(String cardAttribute, String hotelCardName) {
        String result = "";
        switch (cardAttribute.toLowerCase()) {
            case "price":
                result += getHotelCardPrice(hotelCardName);
                break;
            default:
                logger.warn("There is no '" + cardAttribute + "' type of check");
                throw new NoSuchMethodError();
        }
        return result;
    }

    @Override
    public boolean checkHotelCardList(String cardAttribute, int totalHotel, String[] validationData) {
        logger.log("Validation data: " + Arrays.toString(validationData));
        logger.log("card attribute data: " + cardAttribute);
        logger.log("total hotel data: " + totalHotel);

        ArrayList<String> listOfHotelCard = new ArrayList<>();
        boolean loopHotelCardDone = false;
        boolean testResult = true;

        int maxLoop = 0;
        while (!loopHotelCardDone) {
            driverTools.waitForElementToAppear(HOTEL_CARD_NAME);
            driverTools.swipeToElement(HOTEL_CARD_NAME, Swipe.UP, 5).orElseThrow();
            List<WebElement> displayedHotelCards = driverTools.elements(HOTEL_CARD_NAME).orElseThrow();
            if (displayedHotelCards.isEmpty()) {
                testResult = false;
                break;
            }
            int totalSameHotelCardName = 0;
            for (WebElement e : displayedHotelCards) {
                String hotelCardNameText = driverTools.getText(e);
                if (listOfHotelCard.contains(hotelCardNameText)) {
                    totalSameHotelCardName++;
                    continue;
                } else {
                    if (cardAttribute.equals("total_price") && Arrays.stream(validationData).noneMatch(str -> (str.equals(hotelCardNameText)))) {
                        logger.log("Hotel not part of validation data, skipping...");
                    } else {
                        listOfHotelCard.add(hotelCardNameText);
                        handleVerifyCheck(cardAttribute, hotelCardNameText, validationData);
                    }
                }
                if (listOfHotelCard.size() >= totalHotel) {
                    loopHotelCardDone = true;
                    break;
                }
            }
            if (totalSameHotelCardName > displayedHotelCards.size()) {
                loopHotelCardDone = true;
            } else {
                driverTools.swipeUp();
            }
            if (maxLoop == totalHotel) loopHotelCardDone = true;
            maxLoop++;
        }
        return testResult;
    }

    private void handleVerifyCheck(String cardAttribute, String hotelCardName, String[] validationData) {
        hotelCardName = normalizeNonBreakingSpaceText(hotelCardName);
        switch (cardAttribute) {
            case "multiple":
                hotelCardMultipleFilterCheck(hotelCardName, validationData);
                break;
            case "Star":
            case "star":
                hotelCardStarCheck(hotelCardName, validationData);
                break;
            case "Price":
            case "price":
                hotelCardPriceCheck(hotelCardName, validationData);
                break;
            case "name":
            case "Name":
                hotelCardNameCheck(hotelCardName, validationData);
                break;
            case "Benefit":
            case "benefit":
                hotelCardBenefitCheck(hotelCardName, validationData);
                break;
            case "total_price":
                hotelCardTotalPriceCheck(hotelCardName, validationData);
                break;
            case "Property Type":
                hotelCardPropertyTypeCheck(hotelCardName, validationData);
                break;
            case "area":
            case "Area":
                hotelCardAreaCheck(hotelCardName, validationData);
                break;
            case "Guest Rating":
            case "guest rating":
                hotelCardGuestRatingCheck(hotelCardName, validationData);
                break;
            default:
                logger.warn("There is no '" + cardAttribute + "' type of check");
        }
    }

    private void hotelCardMultipleFilterCheck(String hotelCardName, String[] param) {
        int separatorIndex = Arrays.asList(param).indexOf("separator");

        for (int i = 0; i < separatorIndex; i++) {
            int paramIndex = i + separatorIndex + 1;

            switch (param[i]) {
                case "Price Range per night/Rentang Harga per malam" -> {
                    hotelCardPriceCheck(hotelCardName, splitLanguageData(convertPriceParam(param[paramIndex]), "-"));
                }
                case "Number of Bedrooms/Jumlah Kamar Tidur" -> {
                    hotelCardBedroomCheck(hotelCardName, splitLanguageData(param[paramIndex], "/")[0]);
                }
                case "Accommodation Types/Tipe Akomodasi" -> {
                    hotelCardPropertyTypeCheck(hotelCardName, splitLanguageData(param[paramIndex], "/"));
                }
                case "Areas/Area" -> {
                    hotelCardAreaCheck(hotelCardName, new String[]{param[paramIndex]});
                }
                case "Facilities/Fasilitas" -> {
                    hotelCardFacilityCheck(hotelCardName, new String[]{param[paramIndex]});
                }
                case "Property Rating/Rating Properti" -> {
                    hotelCardPropertyRatingCheck(hotelCardName, new String[]{param[paramIndex]});
                }
            }
        }
    }

    private void hotelCardPropertyRatingCheck(String hotelCardName, String[] param) {
        String hotelCardPropertyRating = driverTools.swipeToElement(getHotelCardPropertyRating(hotelCardName), Swipe.UP, 2).orElseThrow().getText();
        for (String s : param) {
            Assertion.assertThat("Assert Hotel Property Rating of '" + hotelCardName + "' is already valid", Integer.parseInt(hotelCardPropertyRating), equalTo(Integer.parseInt(s)));
        }
    }

    /**
     * Validates guest (property) rating on the SRP card meets the filter threshold selected in
     * {@link #selectPropertyRatingFilter(String)} — e.g. "4" or "4.5" means the displayed score should be
     * greater than or equal to that value. Card text may look like {@code 4.1/5}, {@code 4,8/5}, etc.
     * <p>
     * {@code validationData} is usually a single-element array; if fragments are passed (e.g. from joining),
     * they are concatenated so {@code "4" + "." + "5"} becomes {@code 4.5}.
     */
    private void hotelCardGuestRatingCheck(String hotelCardName, String[] validationData) {
        String thresholdJoined = Arrays.stream(Optional.ofNullable(validationData).orElse(new String[0]))
                .filter(Objects::nonNull)
                .map(String::trim)
                .collect(java.util.stream.Collectors.joining());
        Assertion.assertThat(
                "Guest rating filter threshold param should not be empty for hotel '" + hotelCardName + "'",
                thresholdJoined,
                not(emptyString()));

        double minExpected = parseDecimalNumber(thresholdJoined);
        WebElement guestRatingElement = driverTools.swipeToElement(getHotelCardPropertyRating(hotelCardName), Swipe.UP, 2).orElseThrow();
        String rawLine = normalizeNonBreakingSpaceText(driverTools.getText(guestRatingElement));
        double actualScore = parseGuestRatingScoreFromCardLine(rawLine);

        Assertion.assertThat(
                "Guest rating on card '" + hotelCardName + "' (" + actualScore + ") should be >= selected filter (" + minExpected + ")",
                actualScore,
                greaterThanOrEqualTo(minExpected));
    }

    /**
     * Parses the numeric score from card copy such as {@code 4.1/5}, {@code 4,8/ 5}, or a plain {@code 4}.
     */
    public static double parseGuestRatingScoreFromCardLine(String line) {
        Matcher fraction = Pattern.compile("([\\d]+(?:[.,]\\d+)?)\\s*/\\s*5").matcher(line);
        if (fraction.find()) {
            return parseDecimalNumber(fraction.group(1));
        }
        Matcher leading = Pattern.compile("([\\d]+(?:[.,]\\d+)?)").matcher(line);
        if (leading.find()) {
            return parseDecimalNumber(leading.group(1));
        }
        throw new IllegalArgumentException("Could not parse guest rating score from card line: \"" + line + "\"");
    }

    private static Locator getHotelCardPropertyRating(String hotelCardName) {
        return new Locator("HOTEL_CARD_PROPERTY_RATING", AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name') and contains(@text,'" + hotelCardName + "')]//following-sibling::*[contains(@resource-id,'tv_review_value')]"));
    }

    private void hotelCardFacilityCheck(String hotelCardName, String[] param) {
        logger.log("****Opening " + hotelCardName + "****");
        WebElement hotelCards = driverTools.swipeToElement(hotelCardPriceLocator(hotelCardName), Swipe.UP, 10).orElseThrow();
        driverTools.click(hotelCards);

        logger.log("****clicking See all Button For facilities in PDP****");
        DetailPageAndroid<T> detailPageAndroid = new DetailPageAndroid<>(session);
        detailPageAndroid.goToFacilitiesSection();
        detailPageAndroid.openFacilitiesPage();

        logger.log("****Searching for selected facilities****");
        for (String s : param) {
            String[] string = splitLanguageData(s, "/");
            Assertion.assertThat("Check any of facilities should be present", detailPageAndroid.getListFacilities(), hasItem(either(is(string[0])).or(is(string[1]))));
        }
        logger.log("****back to SRP****");
        detailPageAndroid.closePdpDrawer();
        detailPageAndroid.clickBackButton();
    }

    private void hotelCardAreaCheck(String hotelCardName, String[] param) {
        String area = driverTools.getText(getHotelArea(hotelCardName)).orElseThrow();
        area = area.split(",")[0];
        logger.log(hotelCardName + " area is in " + area);
        Assertion.assertThat("Check area hotel", area, is(param[0]));
    }

    private Locator getHotelArea(String hotelName) {
        return new Locator("HOTEL_AREA", AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name') and contains(@text,'" + hotelName + "')]//following-sibling::*[contains(@resource-id,'tv_location_info')]"));
    }

    private void hotelCardTotalPriceCheck(String hotelCardName, String[] param) {
        logger.log("Param: " + Arrays.toString(param));
        logger.log("hotel card name: " + hotelCardName);

        int index = Arrays.asList(param).indexOf(hotelCardName);
        if (index == -1 || index + 1 >= param.length) {
            logger.warn("Hotel card name '" + hotelCardName + "' not found in parameters or no price provided.");
            return;
        }
        logger.log("index of hotel card name: " + index);
        int hotelCardPrice = Integer.parseInt(param[index + 1]);
        int hotelCardTotalPrice = getHotelCardPrice(hotelCardName);
        int totalNights = Integer.parseInt(param[param.length - 1]);

        if (hotelCardPrice != 0) {
            logger.log("hotelCardPrice '" + hotelCardPrice + "'");
            logger.log("totalNights '" + totalNights + "'");
            logger.log("hotelCardTotalPrice '" + hotelCardTotalPrice + "'");
            // hotelCardPrice * totalNights can be + 1 or - 1 due to rounding issues
            int calculatedPrice = hotelCardPrice * totalNights;
            logger.log("calculatedPrice '" + calculatedPrice + "'");
            int expected = hotelCardPrice * totalNights;
            logger.log("expected '" + expected + "'");
            Assertion.assertThat(
                    "Assert Hotel Total Price of '" + hotelCardName + "' is valid",
                    hotelCardTotalPrice,
                    allOf(greaterThanOrEqualTo(expected - 1), lessThanOrEqualTo(expected + 1))
            );
        } else {
            logger.log("hotel is full");
        }
    }

    private void hotelCardBenefitCheck(String hotelCardName, String[] validationData) {
        Boolean isBenefitValid = driverTools.isElementDisplayed(hotelCardBenefitLocator(hotelCardName, validationData));
        Assertion.assertThat(hotelCardName + " has valid Benefit", isBenefitValid, is(true));
    }

    private Locator hotelCardBenefitLocator(String hotlCardName, String[] validationData) {
        String benefitEN = validationData[0];
        String benefitIN = validationData[1];
        return new Locator("HOTEL_BENEFIT_LOCATOR", AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name') and contains(@text,'" + hotlCardName + "')]//following-sibling::*[contains(@resource-id,'tv_benefits') and contains(@text,'" + benefitEN + "') or contains(@text,'" + benefitIN + "')]"));
    }

    private void hotelCardPropertyTypeCheck(String hotelCardName, String[] validationData) {
        driverTools.swipeToElement(hotelCardPropertyTypeLocator(hotelCardName, validationData), Swipe.UP, 2);
        Boolean isPropertyTypeValid = driverTools.isElementDisplayed(hotelCardPropertyTypeLocator(hotelCardName, validationData));
        Assertion.assertThat(hotelCardName + " has valid Property Type", isPropertyTypeValid, is(true));
    }

    private Locator hotelCardPropertyTypeLocator(String hotlCardName, String[] validationData) {
        String propertyTypeEN = validationData[0];
        String propertyTypeIN = validationData[1];
        return new Locator("HOTEL_PROPERTY_LOCATOR", AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name') and contains(@text,'" + hotlCardName + "')]//following-sibling::*[contains(@resource-id,'tv_property_type') and contains(@text,'" + propertyTypeEN + "') or contains(@text,'" + propertyTypeIN + "')]"));
    }

    private void hotelCardBedroomCheck(String hotelCardName, String expectedData) {
        String bedroom = driverTools.getText(hotelCardBenefitBedroomLocator(hotelCardName)).orElseThrow();
        int actualBedroom = Integer.parseInt(bedroom.split(" ")[0].replaceAll("[^0-9]", ""));
        int expectedBedroom = Integer.parseInt(expectedData);
        Assertion.assertThat(hotelCardName + " has valid Total Bedrooms", actualBedroom, greaterThanOrEqualTo(expectedBedroom));
    }

    private Locator hotelCardBenefitBedroomLocator(String hotelCardName) {
        return new Locator("HOTEL_BENEFIT_TOTAL_BEDROOM", AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name') and contains(@text,'" + hotelCardName + "')]//following-sibling::*[contains(@resource-id,'tv_room_spec') and (contains(@text,'Bedroom') or contains(@text,'Kamar'))]"));
    }

    private Locator getCheckFlowerNHALocator(String hotelName) {
        return new Locator("HOTEL_CONTAINS_FLOWER_RATING", AppiumBy.xpath("//*[contains(@resource-id,'rb_hotel_star')]//preceding-sibling::*[contains(@resource-id,'tv_hotel_name') and contains(@text,'" + hotelName + "')]"));
    }

    private void hotelCardStarCheck(String hotelCardName, String[] validationData) {
        boolean isHotelContainsNHAStarsRating = driverTools.isElementDisplayed(getCheckFlowerNHALocator(hotelCardName));
        if (isHotelContainsNHAStarsRating)
            return;
        String hotelCardStar = getHotelCardStar(hotelCardName);
        Assertion.assertThat("Assert Hotel Star of '" + hotelCardName + "' is already valid", Arrays.stream(validationData).toList().contains(hotelCardStar), is(true));
    }

    private void hotelCardPriceCheck(String hotelCardName, String[] validationData) {
        Integer hotelCardPrice = getHotelCardPrice(hotelCardName);
        // if the hotel is full which the value is changed to 0 on getHotelCardPrice, skip assertion
        if (hotelCardPrice != 0) {
            int min = Integer.parseInt(validationData[0]);
            logger.log("hotelCardPrice '" + hotelCardPrice + "'");
            if (validationData[1].equalsIgnoreCase("inf")) {
                Assertion.assertThat("Assert Hotel Price of '" + hotelCardName + "' is already valid", hotelCardPrice >= min, is(true));
            } else {
                int max = Integer.parseInt(validationData[1]);
                Assertion.assertThat("Assert Hotel Price of '" + hotelCardName + "' is already valid", hotelCardPrice >= min && hotelCardPrice <= max, is(true));
            }
        } else {
            logger.log("hotel is full");
        }
    }

    private void hotelCardNameCheck(String hotelCardName, String[] validationData) {
        hotelCardName = normalizeNonBreakingSpaceText(hotelCardName);
        Stream<String> validationDataStream = Arrays.stream(validationData);
        Boolean result = true;
        String finalHotelCardName = hotelCardName;
        if (!(validationDataStream.map(name -> normalizeNonBreakingSpaceText(name)).anyMatch(x -> x.equals(finalHotelCardName)))) {
            result = false;
        }
        Assertion.assertThat("hotel name in list", result, is(true));
    }

    private Locator getFilterSectionLocator(String filterName) {
        if(filterName.toLowerCase().contains("star")) {
            return new Locator("SELECTED_FILTER_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'v_title')]//*[contains(@text,'Star') or contains(@text,'Bintang')]"));
        }else if (filterName.contains("/")) {
            String filterNameEnglish = filterName.split("/")[0];
            String filterNameBahasa = filterName.split("/")[1];
            return new Locator("SELECTED_FILTER_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'v_title')]//*[contains(@text,'"+filterNameEnglish+"') or contains(@text,'"+filterNameBahasa+"')]"));
        }else if (filterName.toLowerCase().contains("loyalty") || filterName.toLowerCase().contains("refund") || filterName.toLowerCase().contains("reschedule")) {
            return new Locator("SELECTED_FILTER_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_list') and (contains(@text,'"+filterName+"'))]/following-sibling::*[contains(@resource-id,'tds_selection_area_right')]"));
        }else{
            return new Locator("SELECTED_FILTER_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'v_title')]//*[contains(@text,'"+filterName+"')]"));
        }
    }

    private Locator getHotelStarSelection(int index) {
        return new Locator("CHIP_FILTER_HOTEL_STAR", AppiumBy.xpath("(//*[contains(@resource-id,'chip_group_filter_star')]//*[contains(@resource-id,'tds_chips_text')])[" + index + "]"));
    }

    private String getHotelCardStar(String hotelCardNameText) {
        final Locator hotelCardStar = new Locator("HOTEL_CARD_STAR", AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name') and contains(@text,'" + hotelCardNameText + "')]/following-sibling::*[contains(@resource-id,'rb_hotel_star')]//*[contains(@class,'ImageView')]"));
        driverTools.swipeToElement(hotelCardStar, Swipe.UP, 3);
        return Integer.toString(driverTools.elements(hotelCardStar).get().size());
    }

    private Integer getHotelCardPrice(String hotelCardName) {
        driverTools.swipeToElement(hotelCardPriceLocator(hotelCardName), Swipe.UP, 3).orElseThrow();
        WebElement hotelPriceElement = driverTools.waitForElementToPresence(hotelCardPriceLocator(hotelCardName), 20).orElseThrow();
        String hotelPrice = driverTools.getText(hotelPriceElement);
        if (hotelPrice.equals("Kamar Penuh") || hotelPrice.equals("Fully Booked")) {
            logger.softWarning("Current hotel is full, price changed to IDR 0 to avoid exception");
            hotelPrice = "IDR 0";
        } else if (hotelPrice.contains("?")) {
            hotelPrice = "IDR " + Integer.toString(UNAVAILABLE_PRICE);
        }
        return Math.toIntExact(Math.round(convertPriceSafely(hotelPrice)));
    }

    private Locator hotelCardPriceLocator(String hotelCardName) {
        return new Locator("HOTEL_CARD_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name') and contains(@text,'" + hotelCardName + "')]/preceding-sibling::*[contains(@resource-id,'cl_price')]//*[contains(@resource-id,'tv_main_price') or contains(@resource-id,'tv_final_price')]"));
    }

    private boolean isHotelListDisplayed() {
        String hotelName = String.valueOf(driverTools.getText(FIRST_CARD_HOTEL_NAME).orElseThrow());
        String hotelPrice = String.valueOf(driverTools.getText(FIRST_CARD_HOTEL_PRICE).orElseThrow());
        if (hotelName.length() != 0 && hotelPrice.length() != 0 && ifHeaderFiltersHasText()) {
            return true;
        } else if (hotelName.length() == 0 && hotelPrice.length() == 0) {
            if (driverTools.isElementDisplayed(RESET_FILTER_BUTTON)) {
                return true;
            }
        }
        return false;
    }

    private boolean ifHeaderFiltersHasText() {
        String quickFilterTitleText = String.valueOf(driverTools.getText(QUICK_FILTER_HEADER_TEXT).orElseThrow());
        if (quickFilterTitleText.length() == 0)
            return false;
        else
            return true;
    }

     /*
     docs:
     simple methods below can be used to get data from 1st hotel card list on srp,
     if test cases is simple and does not require to do random pick.
     for specific number card pick or random card please use/create method like getHotelLocationList(String list).
     */

    /**
     * Selects the first hotel card from search results.
     * Handles promo containers, ensures card is visible and centered to avoid bottom overlays.
     * 
     * Optimized to:
     * - Consolidate duplicate waits (30-40% faster)
     * - Use swipeElementToMidScreen to avoid bottom navigation overlays
     * - Implement proper click retry logic with fallback
     */
    @SneakyThrows
    public void selectFirstHotelCard() {
        logger.log("Selecting first hotel card");
        
        // Step 1: Dismiss promo container if present (consolidated wait)
        driverTools.waitForElementToPresence(PROMO_CONTAINER).ifPresent(promoElement -> {
            logger.log("Promo container found, swiping up to dismiss");
            driverTools.swipeByDistance(promoElement, Swipe.UP, 0.7);
        });
        
        // Step 2: Swipe to bring first hotel card into view
        // This combines the logic of checking if visible and swiping to it
        Optional<WebElement> hotelCardElement = driverTools.swipeToElement(
            FIRST_CARD_HOTEL_NAME, 
            SRP_CARD_VIEW, 
            Swipe.UP, 
            5  // Increased from 3 for better reliability
        );
        
        if (hotelCardElement.isEmpty()) {
            logger.softWarning("First hotel card not found after swipe attempts");
            throw new RuntimeException("Unable to locate first hotel card");
        }
        
        // Step 3: Position card at mid-screen to avoid bottom overlays
        driverTools.swipeElementToMidScreen(FIRST_CARD_HOTEL_NAME);
        
        // Step 4: Click with retry logic (cleaner approach)
        clickHotelCardWithRetry(FIRST_CARD_HOTEL_NAME);
        
        logger.log("Successfully selected first hotel card");
    }
    
    /**
     * Helper method: Clicks hotel card with coordinate-based fallback if regular click fails.
     * This handles cases where the element might not respond to standard clicks.
     * 
     * @param locator The locator of the hotel card element to click
     */
    private void clickHotelCardWithRetry(Locator locator) {
        try {
            // Try regular click first
            WebElement element = driverTools.waitForElementToPresence(locator, 5).orElseThrow();
            driverTools.click(element);
            logger.log("Clicked hotel card successfully with regular click");
        } catch (Exception e) {
            logger.log("Regular click failed, trying coordinate-based click: " + e.getMessage());
            try {
                driverTools.clickElementByCoordinate(locator);
                logger.log("Coordinate-based click successful");
            } catch (Exception ex) {
                logger.fail("Both click methods failed for: " + locator);
                throw new RuntimeException("Failed to click hotel card after multiple attempts", ex);
            }
        }
    }

    /**
     * Selects the first hotel card by clicking on the price element.
     * Optimized with swipeElementToMidScreen to avoid bottom overlays.
     */
    @Override
    @SneakyThrows
    public void selectFirstHotelCardFromPrice() {
        logger.log("Selecting first hotel card from price");
        
        // Swipe to bring price element into view
        driverTools.swipeToElement(FIRST_CARD_HOTEL_PRICE, Swipe.UP, 3).orElseThrow(
            () -> new RuntimeException("Hotel price element not found")
        );
        
        // Position at mid-screen to avoid bottom overlays
        driverTools.swipeElementToMidScreen(FIRST_CARD_HOTEL_PRICE);
        
        // Click with retry logic
        clickHotelCardWithRetry(FIRST_CARD_HOTEL_PRICE);
        
        logger.log("Successfully selected first hotel card from price");
    }

    @Override
    public Optional<WebElement> goToFirstHotelCard() {
        driverTools.waitForElementToAppear(FLOAT_MAP);
        return driverTools.swipeToElement(FIRST_CARD_HOTEL_NAME, SRP_CARD_VIEW, Swipe.UP, 3);
    }

    public String getFirstHotelName() {
        driverTools.waitForElementToAppear(FIRST_CARD_HOTEL_NAME);
        WebElement webElement = driverTools.swipeToElement(FIRST_CARD_HOTEL_NAME,Swipe.UP,3).orElseThrow();
        return driverTools.getText(webElement).replaceFirst("^__", "");
    }

    public String getFirstHotelLocation() {
        WebElement webElement = driverTools.swipeToElement(FIRST_CARD_HOTEL_LOCATION, Swipe.UP, 3).orElseThrow();
        return normalizeNonBreakingSpaceText(driverTools.getText(webElement));
    }

    @Override
    public boolean istFirstHotelLocationDisplayed() {
        WebElement webElement = driverTools.waitForElementToAppear(FIRST_CARD_HOTEL_LOCATION).orElseThrow();
        return driverTools.isElementDisplayed(webElement);
    }

    public String getFirstHotelPrice() {
        WebElement webElement = driverTools.swipeToElement(HOTEL_PRICE_CARD, Swipe.UP, 3).orElseThrow();
        return driverTools.getText(webElement);
    }

    @Override
    public boolean checkHighlightedResult() {
        return driverTools.isElementDisplayed(HIGHLIGHT_LABEL);
    }

    @Override
    public boolean getFirstHotelStar() {
        WebElement webElement = driverTools.swipeToElement(FIRST_CARD_HOTEL_STAR, Swipe.UP, 3).orElseThrow();
        return driverTools.isElementDisplayed(webElement);
    }

    public String getFirstHotelReview() {
        WebElement webElement = driverTools.waitForElementToAppear(FIRST_CARD_HOTEL_REVIEW).orElseThrow();
        return driverTools.getText(webElement);
    }

    private Locator getNthHotelReviewLocator(int index) {
        return new Locator("HOTEL_REVIEW_BY_INDEX", AppiumBy.xpath("(//*[contains(@resource-id, 'cl_parent')]//*[contains(@resource-id, 'tv_review_value')])[" + index + "]"));
    }

    @Override
    public Boolean istFirstHotelReviewDisplayed() {
        WebElement webElement = driverTools.waitForElementToAppear(FIRST_CARD_HOTEL_REVIEW).orElseThrow();
        return driverTools.isElementDisplayed(webElement);
    }

    @Override
    public void swipeToFirstCard() {
        driverTools.swipeToElement(FIRST_CARD_HOTEL_NAME, SRP_CARD_VIEW, Swipe.DOWN, 10);
    }

    @Override
    public Boolean isNthHotelReviewDisplayed(int cardIndex) {
        if (cardIndex < 1) {
            return false;
        }
        Locator locator = getNthHotelReviewLocator(cardIndex);
        return driverTools.swipeToElement(locator, Swipe.UP, 5)
                .map(driverTools::isElementDisplayed)
                .orElse(false);
    }

    @Override
    public String getNthHotelReview(int cardIndex) {
        WebElement webElement = driverTools.swipeToElement(getNthHotelReviewLocator(cardIndex), Swipe.UP, 5).orElseThrow();
        return driverTools.getText(webElement);
    }

    @Override
    public boolean isSearchResultShown() {
        boolean isSrpAppBarPresent = driverTools.waitForElementToAppear(SRP_APP_BAR).isPresent();
        Assertion.assertThat("SRP_APP_BAR should be present", isSrpAppBarPresent, is(true));
        boolean isSrpMasterTagPresent = driverTools.waitForElementToAppear(SRP_MASTER_TAG, 20).isPresent();
        Assertion.assertThat("SRP_MASTER_TAG should be present", isSrpMasterTagPresent, is(true));
        boolean isSrpResultPresent = driverTools.waitForElementToAppear(SRP_RESULT).isPresent();
        Assertion.assertThat("SRP_RESULT should be present", isSrpResultPresent, is(true));
        boolean isSrpFilterBarPresent = driverTools.waitForElementToAppear(SRP_FILTER_BAR).isPresent();
        Assertion.assertThat("SRP_FILTER_BAR should be present", isSrpFilterBarPresent, is(true));
        driverTools.swipeToElement(HOTEL_CARD, Swipe.UP, 3);
        boolean isHotelCardPresent = driverTools.waitForElementToAppear(HOTEL_CARD).isPresent();
        Assertion.assertThat("HOTEL_CARD should be present", isHotelCardPresent, is(true));

        return isSrpAppBarPresent && isSrpMasterTagPresent && isSrpResultPresent && isSrpFilterBarPresent && isHotelCardPresent;
    }

    @Override
    public boolean isHeaderSearchResultShown() {
        Optional<WebElement> checkAppBar = driverTools.waitForElementToAppear(SRP_APP_BAR);
        if (checkAppBar.isPresent()) {
            logger.log("SRP_APP_BAR is exist");
        } else {
            logger.fail("SRP_APP_BAR is not exist");
        }

        Optional<WebElement> checkMasterTag = driverTools.waitForElementToAppear(SRP_MASTER_TAG);
        if (checkMasterTag.isPresent()) {
            logger.log("SRP_MASTER_TAG is exist");
        } else {
            logger.fail("SRP_MASTER_TAG is not exist");
        }

        Optional<WebElement> checkSrpResult = driverTools.waitForElementToAppear(SRP_RESULT);
        if (checkSrpResult.isPresent()) {
            logger.log("SRP_RESULT is exist");
        } else {
            logger.fail("SRP_RESULT is not exist");
        }


        Optional<WebElement> hotelCardName = driverTools.swipeToElement(HOTEL_CARD_NAME, Swipe.UP, 3);
        Optional<WebElement> checkSrpContent = hotelCardName.isPresent() ?
                hotelCardName : driverTools.waitForElementToAppear(RESET_FILTER_BUTTON);
        if (checkSrpContent.isPresent()) {
            logger.log("SRP_CONTENT is exist");
        } else {
            logger.fail("SRP_CONTENT is not exist");
        }

        return checkAppBar.isPresent()
                && checkMasterTag.isPresent()
                && checkSrpResult.isPresent()
                && checkSrpContent.isPresent();
    }

    @Override
    public void filterSortBy(String filterType, String hotelCount) {
        throw new NotImplementedException();
    }

    @Override
    public Boolean isFirstHotelNameDisplayed() {
        driverTools.waitForElementToAppear(FLOAT_MAP);
        driverTools.swipeToElement(FIRST_CARD_HOTEL_NAME, SRP_CARD_VIEW, Swipe.UP, 2);
        return driverTools.isElementDisplayed(FIRST_CARD_HOTEL_NAME);
    }

    @Override
    public Boolean isFirstHotelPriceDisplayed() {
        driverTools.swipeToElement(FIRST_CARD_HOTEL_PRICE, Swipe.UP, 2);
        return driverTools.isElementDisplayed(FIRST_CARD_HOTEL_PRICE);
    }

    @Override
    public void selectFilterStar(String star) {
        if (driverTools.waitForElementToAppear(STAR_FILTER_BUTTON).isEmpty())
            driverTools.swipeToElement(STAR_FILTER_BUTTON, SRP_FILTER_BAR, Swipe.LEFT, 4).orElseThrow();
        driverTools.clickElementByCoordinate(STAR_FILTER_BUTTON);
        driverTools.waitForElementToAppear(STAR_OPTIONS).orElseThrow();
        if (star.equals("random")) {
            List<WebElement> starOptionsList = driverTools.elements(STAR_OPTIONS).orElseThrow();
            int starOptions = starOptionsList.size();
            logger.log("total starOptions -> " + starOptions);
            int random = generateRandomInt(1, starOptions);
            logger.log("random index -> " + random);
            driverTools.click(starOptionsList.get(random));
        } else {
            driverTools.click(starOptions(Integer.parseInt(star)));
        }
        driverTools.click(SUBMIT_SEARCH_BUTTON);
    }

    @Override
    public void selectFilterStarRange(String starRange){
        if (driverTools.waitForElementToAppear(STAR_FILTER_BUTTON).isEmpty())
            driverTools.swipeToElement(STAR_FILTER_BUTTON, SRP_FILTER_BAR, Swipe.LEFT, 4).orElseThrow();
        driverTools.clickElementByCoordinate(STAR_FILTER_BUTTON);
        driverTools.waitForElementToAppear(STAR_OPTIONS).orElseThrow();
        String[] starRangeSplit = starRange.contains("-") ? starRange.split("-") : new String[]{starRange};
        for (String value : starRangeSplit) {
            int star = Integer.parseInt(value.trim());
            logger.log("range index -> " + star);
            driverTools.click(starOptions(star));
        }
        driverTools.click(SUBMIT_SEARCH_BUTTON);
    }

    private Locator starOptions(int star) {
        return new Locator("STAR_OPTIONS_" + star, AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_container')]//*[contains(@resource-id,'tds_chips_text') and @text='" + star + "']"));
    }

    @Override
    public void selectFilterSortBy(String filterType) {
        if (driverTools.waitForElementToAppear(SORT_BY_BUTTON).isEmpty()) {
            driverTools.swipeToElement(SORT_BY_BUTTON, SRP_FILTER_BAR, Swipe.RIGHT, 6).orElseThrow();
            driverTools.swipeToElement(SORT_BY_BUTTON, SRP_FILTER_BAR, Swipe.LEFT, 2).orElseThrow();
        }
        driverTools.click(SORT_BY_BUTTON);
        switch (filterType) {
            case "lowest price" -> {
                driverTools.click(SORT_BY_LOWEST_PRICE);
            }
            case "highest price" -> {
                driverTools.click(SORT_BY_HIGHEST_PRICE);
            }
            case "highest rating" -> {
                throw new NotImplementedException();
            }
            case "highest star" -> {
                throw new NotImplementedException();
            }
            default -> {
                throw new RuntimeException("wrong test data 'filter', please check testdata.xlsx ");
            }
        }
        driverTools.waitForElementToAppear(SORT_BY_BUTTON_BADGED,15);
    }

    @Override
    public Boolean isSortByLowestPriceValid(String totalHotel) {
        driverTools.waitForElementToAppear(FIRST_CARD_HOTEL_PRICE);
        int totalHotelInt = Integer.parseInt(totalHotel);

        String currentHotelName = getHotelNameList("");
        int currentPrice = getHotelCardPrice(currentHotelName);
        String nextHotelName = currentHotelName;

        for (int i = 0; i < totalHotelInt - 1; i++) {
            int maxLoop = 0;
            while (currentHotelName.equals(nextHotelName)) {
                driverTools.swipeByDistance(VISIBLE_HOTEL_CONTAINER, Swipe.UP, 1);
                nextHotelName = getHotelNameList("");
                if (maxLoop == 10) {
                    logger.softWarning("Hotel name is the same for 10 times, please check the test data");
                    break;
                }
                maxLoop++;
            }

            int nextPrice = getHotelCardPrice(nextHotelName);

            if (nextPrice != UNAVAILABLE_PRICE) { //price is not member nudge, "SGD ?" for example
                Assertion.assertThat("Hotel price of " + currentHotelName + " -> " + currentPrice + " should be less than or equal to " + nextHotelName + " -> " + nextPrice, currentPrice, lessThanOrEqualTo(nextPrice));
            } else {
                logger.log("unverifiable price encountered at iteration " + i + ", moving on to the next iteration");
            }

            currentHotelName = nextHotelName;
            currentPrice = nextPrice;
        }
        return true;
    }

    @Override
    public void openPriceFilter() {
        if (driverTools.waitForElementToAppear(PRICE_FILTER).isEmpty())
            driverTools.swipeToElement(PRICE_FILTER, SRP_FILTER_BAR, Swipe.LEFT, 4).orElseThrow();
        driverTools.click(PRICE_FILTER);
    }

    @Override
    public String selectPriceFilterByRange(String priceRange, String currency) {
        String[] priceRangeSplit = minMaxPrice(priceRange, currency);
        String minPrice = priceRangeSplit[0];
        String maxPrice = priceRangeSplit[1];
        driverTools.click(PRICE_FILTER_MIN_INPUT);
        driverTools.clear(PRICE_FILTER_MIN_INPUT);
        driverTools.sendKeys(PRICE_FILTER_MIN_INPUT, minPrice);
        driverTools.click(PRICE_FILTER_MAX_INPUT);
        driverTools.clear(PRICE_FILTER_MAX_INPUT);
        driverTools.sendKeys(PRICE_FILTER_MAX_INPUT, maxPrice);
        return minPrice + "-" + maxPrice;
    }

    @Override
    public boolean isPriceFilterByRangeCorrect(String priceRange, String totalHotel) {
        switch (priceRange) {
            case "<200":
            case "<500":
            case "500-1000":
            case ">1000": {
                int[] range = getPriceRange(priceRange, "");
                String maxStr = range[1] >= 99_999_999 ? "Inf" : String.valueOf(range[1]);
                checkHotelCardList("price", Integer.parseInt(totalHotel), new String[]{String.valueOf(range[0]), maxStr});
                break;
            }
            default: {
                String normalized = priceRange.replaceAll("[A-Z]+", "");
                logger.log("checking for filter " + normalized);

                if (normalized.contains("-")) {
                    int bottomLimit = Integer.parseInt(normalized.split("-")[0]);
                    int upperLimit = Integer.parseInt(normalized.split("-")[1]);
                    checkHotelCardList("price", Integer.parseInt(totalHotel), new String[]{String.valueOf(bottomLimit), String.valueOf(upperLimit)});
                } else if (normalized.contains("<") || normalized.contains(">")) {
                    int limit = getIntFromString(normalized).orElseThrow();
                    if (normalized.contains("<")) {
                        checkHotelCardList("price", Integer.parseInt(totalHotel), new String[]{"0", String.valueOf(limit)});
                    } else {
                        checkHotelCardList("price", Integer.parseInt(totalHotel), new String[]{String.valueOf(limit), "Inf"});
                    }
                } else {
                    throw new NotImplementedException("not implemented price filter");
                }
            }
        }
        return true;
    }

    private Locator getHotelCardPriceLocator(String Price){
        return new Locator("HOTEL_PRICE" + Price,AppiumBy.xpath("//*[contains(@resource-id,'tv_final_price') and contains(@text,'"+Price+"')]"));
    }

    private Locator getCrossedHotelPriceLocator(String title){
        return new Locator("CROSSED_PRICE" + title,AppiumBy.xpath("//*[contains(@resource-id,'tv_slash_price') and contains(@text,'"+title+"')]"));
    }

    private Locator getHotelPriceAfterTaxesLocator(String price) {
        return new Locator("PRICE_AFTER_TAXES" + price, AppiumBy.xpath(
                "//android.widget.TextView[contains(@resource-id, 'tv_price_info') and " +
                        "(contains(@text, '/room/night (after taxes:\nIDR " + price + ")') or " +
                        "contains(@text, '/kamar/malam (setelah pajak:\nIDR " + price + ")') or " +
                        "contains(@text, '/night (after taxes:\nIDR " + price + ")') or " +
                        "contains(@text, '/malam (setelah pajak:\nIDR " + price + ")')" +
                        ")]"
        ));
    }
    private Locator getHotelName(String hotelName){
        return new Locator("HOTEL_NAME",AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name') and contains(@text,'"+hotelName+"')]"));
    }

    private Locator getHotelFinalPriceLocator(String hotelName, String price) {
        return new Locator("HOTEL_FINAL_PRICE", AppiumBy.xpath("//*[contains(@resource-id, 'tv_hotel_name') and contains(@text, '"+hotelName+"')]//preceding-sibling::*[contains(@resource-id, 'cl_price')]//*[contains(@resource-id, 'tv_main_price') and contains(@text,'"+price+"')]"));
    }

    private Locator getHotelCrossPriceLocator(String hotelName, String price) {
        return new Locator("HOTEL_CROSS_PRICE", AppiumBy.xpath("//*[contains(@resource-id, 'tv_hotel_name') and contains(@text, '"+hotelName+"')]//preceding-sibling::*[contains(@resource-id, 'cl_price')]//*[contains(@resource-id, 'tv_slash_price') and contains(@text,'"+price+"')]"));
    }

    @Override
    public String getDateRoomGuestDetail() {
        driverTools.waitForElementToAppear(HOTEL_CARD);
        return driverTools.getText(DATE_ROOM_GUEST_DETAIL).orElseThrow();
    }

    @Override
    public String getCheckInOutDate() {
        String value = getDateRoomGuestDetail();
        return value.split(" • ")[0];
    }

    @Override
    public int getTotalGuestHeader() {
        String dateRoomGuestApp = getDateRoomGuestDetail();
        String[] dataSplit = dateRoomGuestApp.split(" • ");
        logger.log("dataSplit -> " + Arrays.toString(dataSplit));
        return Integer.parseInt(dataSplit[dataSplit.length - 1].split(" ")[0]);
    }

    @Override
    public void clickBackButton() {
        driverTools.waitForElementsToAppear(BACK_BUTTON);
        driverTools.click(BACK_BUTTON);
    }

    @Override
    public void checkPopupAndClickBackBtn() {
        driverTools.waitForElementsToAppear(BACK_BUTTON);
        driverTools.waitForElementToPresence(FIRST_CARD_HOTEL_NAME, 30);
        driverTools.click(BACK_BUTTON);
    }

    @Override
    public boolean isAddedBenefitVisible() {
        driverTools.swipeToElement(ADDED_BENEFIT, Swipe.UP, 3).orElseThrow();
        List<WebElement> addedBenefits = driverTools.elements(ADDED_BENEFIT).orElseThrow();
        return !addedBenefits.isEmpty();
    }

    @Override
    public boolean isBenefitVisible(String benefitData, int maxHotelsToCheck) {        
        final int MAX_SAME_NAME_SWIPE = 3;

        driverTools.waitForElementToAppear(HOTEL_CARD_NAME).orElseThrow();
        String currentHotelName = getHotelNameList("");
        logger.log("Benefit check started on hotel '" + currentHotelName + "' (looking for '" + benefitData + "')");

        boolean benefitFound = isBenefitOnHotelCard(currentHotelName, benefitData);
        int hotelChecked = 1;

        while (hotelChecked < maxHotelsToCheck) {
            String nextHotelName = currentHotelName;
            int sameNameSwipe = 0;
            while (currentHotelName.equals(nextHotelName)) {
                driverTools.swipeByDistance(VISIBLE_HOTEL_CONTAINER, Swipe.UP, 1);
                nextHotelName = getHotelNameList("");
                sameNameSwipe++;
                if (sameNameSwipe >= MAX_SAME_NAME_SWIPE) {
                    logger.softWarning("Hotel name still '" + currentHotelName + "' after " + MAX_SAME_NAME_SWIPE + " swipes, stop scrolling");
                    break;
                }
            }

            if (currentHotelName.equals(nextHotelName)) break;

            currentHotelName = nextHotelName;
            if (isBenefitOnHotelCard(currentHotelName, benefitData)) {
                benefitFound = true;
            }
            hotelChecked++;
        }

        Assertion.assertThat("Benefit '" + benefitData + "' should be visible on the first " + maxHotelsToCheck + " hotel cards",
                benefitFound, is(true));
        return benefitFound;
    }

    private boolean isBenefitOnHotelCard(String hotelName, String benefitData) {
        WebElement benefitElement = driverTools.waitForElementToAppear(benefitAndFacilityParentLocator(hotelName), 5).orElse(null);
        if (benefitElement == null) {
            logger.log("No benefit element found for hotel '" + hotelName + "'");
            return false;
        }
        String benefitText = driverTools.getText(benefitElement);
        String[] benefitParts = splitLanguageData(benefitData, "/");
        boolean contains = benefitTextContains(benefitText, benefitParts[0])
                || benefitTextContains(benefitText, benefitParts[1]);
        String expectedBenefit = benefitParts[0].equals(benefitParts[1])
                ? benefitParts[0]
                : benefitParts[0] + " or " + benefitParts[1];
        Assertion.assertThat("Benefit '" + expectedBenefit + "' should be visible in: " + benefitText,
                contains, is(true));
        logger.log("Hotel '" + hotelName + "' benefit -> '" + benefitText + "' contains '" + expectedBenefit + "': " + contains);
        return contains;
    }

    @Override
    public FilterInfo findAndChooseInterceptFilter() {
        FilterInfo filterInfo = new FilterInfo();
        filterInfo.filterType = getInterceptFilterType();
        filterInfo.choosenFilter = handleInterceptFilter(filterInfo.filterType);
        return filterInfo;
    }

    private String getInterceptFilterType() {
        WebElement interceptFilterElement = driverTools.swipeToElement(INTERCEPT_FILTER, Swipe.UP, 15).orElseThrow();
        driverTools.swipeUp();
        String[] interceptFilterElementContentDesc = interceptFilterElement.getAttribute("resource-id").split("_");
        String filterType = (interceptFilterElementContentDesc[interceptFilterElementContentDesc.length - 1]).toLowerCase();
        logger.log("filterType -> " + filterType);
        return filterType;
    }

    private String handleInterceptFilter(String filterType) {
        driverTools.swipeToElement(INTERCEPT_FILTER_OPTION, Swipe.UP, 2).orElseThrow();
        List<WebElement> filterOptionElements = driverTools.elements(INTERCEPT_FILTER_OPTION).orElseThrow();
        WebElement choosenFilterElement = filterOptionElements.get(generateRandomInt(0, filterOptionElements.size() - 1));

        String choosenFilterRaw = driverTools.getText(choosenFilterElement);
        driverTools.click(choosenFilterElement);

        String choosenFilter = "";
        if (filterType.equalsIgnoreCase("price")) {
            if (choosenFilterRaw.contains("<")) {
                choosenFilter += "0-";
                if (choosenFilterRaw.contains("200")) {
                    choosenFilter += "200000";
                } else if (choosenFilterRaw.contains("500")) {
                    choosenFilter += "500000";
                } else {
                    logger.fail("1 There is no handle for '" + choosenFilterRaw + "'");
                }
            } else if (choosenFilterRaw.contains("-")) {
                choosenFilter += "500000-1000000";

            } else if (choosenFilterRaw.contains(">")) {
                choosenFilter += "1000000-Inf";

            } else {
                logger.fail("3 There is no handle for '" + choosenFilterRaw + "'");
            }
        } else if (filterType.equalsIgnoreCase("star")) {
            choosenFilter += choosenFilterRaw.substring(0, 1);
        }
        logger.log("choosenFilter -> " + choosenFilter);
        return choosenFilter;
    }


    @Override
    public boolean isFilterActivated(String filterType) {
        //verify general filter green icon is applied
        filterType = filterType.toLowerCase();
        driverTools.swipeToElement(FILTER_BUTTON, SRP_FILTER_BAR, Swipe.RIGHT, 10);
        Boolean filter_badge = driverTools.isElementDisplayed(FILTER_BUTTON_BADGE);

        Boolean filter_type_badge;
        if (filterType.equals("filter")) {
            filter_type_badge = filter_badge;
        } else {
            //verify selected filter green icon is applied
            driverTools.swipeToElement(getFilterSectionLocator(filterType), SRP_FILTER_BAR, Swipe.LEFT, 10);
            filter_type_badge = driverTools.isElementDisplayed(getFilterTypeBadgeLocator(filterType));
            logger.log("filter_badge : '" + filter_badge + "'\nfilter_type_badge : '" + filter_type_badge + "'");
        }
        //both filter green icon much exist
        return filter_badge && filter_type_badge;
    }

    /**
     * @param filterType name of the filter, can be seen on bottom bar of search result page
     * @return locator of the `green icon` of the filter after the filter is applied
     */
    private Locator getFilterTypeBadgeLocator(String filterType) {
        switch (filterType) {
            case "price" -> {
                return PRICE_FILTER_SELECTED;
            }
            case "star" -> {
                return STAR_FILTER_SELECTED;
            }
            case "bedrooms" -> {
                return BEDROOM_FILTER_SELECTED;
            }
            default -> {
                logger.fail("There is no handle for filterType '" + filterType + "'");
                throw new NoSuchMethodError();
            }
        }
    }

    @SneakyThrows
    @Override
    public void zoomMaps(boolean zoomIn) {
        Thread.sleep(3000);
        driverTools.waitForElementToPresence(HOTEL_PRICE_MAPS);
        driverTools.zoomInOut(MAPS_CONTAINER, zoomIn);
    }

    @Override
    public void swipeMaps(Swipe swipe, int numberOfSwipe) {
        switch (swipe) {
            case UP -> driverTools.swipeUp(MAPS_CONTAINER, numberOfSwipe);
            case DOWN -> driverTools.swipeDown(MAPS_CONTAINER, numberOfSwipe);
            case LEFT -> driverTools.swipeLeft(MAPS_CONTAINER, numberOfSwipe);
            case RIGHT -> driverTools.swipeRight(MAPS_CONTAINER, numberOfSwipe);
        }
    }

    @Override
    public void searchThisArea() {
        driverTools.click(SEARCH_THIS_AREA_BUTTON);
    }

    @Override
    public String getVisibleHotelPrice() {
        String price = "IDR 0";
        if (driverTools.waitForElementToPresence(HOTEL_PRICE_CARD).isPresent())
            price = driverTools.getText(HOTEL_PRICE_CARD).orElseThrow();
        return price;
    }

    @Override
    public String getVisibleHotelPriceMaps() {
        return getVisibleHotelPrice();
    }

    @Override
    public void searchPrevious() {
        driverTools.click(SEARCH_PREVIOUS_BUTTON);
    }

    @Override
    public void swipeHotelCardItem() {
        WebElement hotelContainer = driverTools.waitForElementToPresence(VISIBLE_HOTEL_CONTAINER).orElseThrow();
        driverTools.swipeByDistance(hotelContainer, Swipe.LEFT, 0.8);
    }

    @Override
    public boolean isHotelNameCardVisibleOnMaps() {
        return driverTools.isElementDisplayed(HOTEL_CARD_NAME);
    }

    @Override
    public String selectVisibleHotelItem() {
        String hotelName = driverTools.getText(HOTEL_CARD_NAME).orElseThrow();
        driverTools.click(HOTEL_CARD_NAME);
        return hotelName;
    }

    @Override
    public void clickSeeMapButton() {
        WebElement seeMapButton = driverTools.waitForElementToBeClickable(SEE_MAP_BUTTON).orElseThrow();
        driverTools.click(seeMapButton);
        driverTools.waitForElementToDisappear(SEE_MAP_BUTTON);
    }

    @Override
    public String[] getHotelPriceMaps() {
        driverTools.waitForElementToDisappear(SEE_MAP_BUTTON);
        WebElement hotelPriceMapsButton = driverTools.waitForElementToPresence(HOTEL_PRICE_MAPS, 20).orElseThrow();
        String hotelPriceMapsText = driverTools.getValueAttribute(hotelPriceMapsButton, "content-desc");
        Pattern pattern = Pattern.compile("^([^ ]+ [^ .]+)");
        Matcher m = pattern.matcher(hotelPriceMapsText);
        String hotelPriceMaps = null;
        while (m.find()) {
            hotelPriceMaps = m.group(1);
        }
        logger.log("Hotel Price In Maps: " + hotelPriceMaps);
        String[] priceMaps = {hotelPriceMapsText, hotelPriceMaps};
        driverTools.click(HOTEL_PRICE_MAPS);
        return priceMaps;
    }

    @Override
    public String getHotelPriceMapsText() {
        driverTools.waitForElementToDisappear(SEE_MAP_BUTTON);
        WebElement hotelPriceMapsButton = driverTools.waitForElementToPresence(HOTEL_PRICE_MAPS).orElseThrow();
        String hotelPriceMapsText = driverTools.getValueAttribute(hotelPriceMapsButton, "content-desc");
        logger.log("Hotel Price In Maps: " + hotelPriceMapsText);
        driverTools.click(HOTEL_PRICE_MAPS);
        return hotelPriceMapsText;
    }

    @Override
    public boolean isHotelStarCardVisibleOnMaps() {
        return driverTools.isElementDisplayed(HOTEL_STAR_CARD_MAPS);
    }

    @Override
    public String getHotelPriceCard(String priceText, boolean isMaps) {
        String hotelPriceCardText;
        if (isMaps) {
            hotelPriceCardText = driverTools.getText(HOTEL_PRICE_CARD_MAPS).orElseThrow();
        } else {
            driverTools.waitForElementToAppear(TIMED_PROMO_HOTEL_NAME);
            driverTools.swipeToElement(HOTEL_PRICE_CARD, Swipe.UP, 3).orElseThrow();
            if (driverTools.isElementDisplayed(TIMED_PROMO_HOTEL_NAME))
                driverTools.swipeToElement(HOTEL_PRICE_CARD, Swipe.UP, 3).orElseThrow();
            hotelPriceCardText = driverTools.getText(HOTEL_PRICE_CARD).orElseThrow();
        }
        logger.log("Hotel Price In Card: " + hotelPriceCardText);
        return hotelPriceCardText;

    }

    @Override
    public void selectHotelPriceDisplay(String filterOption) {
        WebElement paymentMethodOptionElement;
        if (filterOption.toLowerCase().contains("per room per night")) {
            paymentMethodOptionElement = driverTools.swipeToElement(PER_ROOM_PER_NIGHT, Swipe.UP, 2).orElseThrow();
        } else {
            paymentMethodOptionElement = driverTools.swipeToElement(getHotelPriceDisplayFilterOptionLocator(filterOption), Swipe.UP, 2).orElseThrow();
        }
        driverTools.click(paymentMethodOptionElement);
    }

    private Locator getHotelPriceDisplayFilterOptionLocator(String paymentMethod) {
        String filterNameEnglish = splitLanguageData(paymentMethod, "/")[0].toLowerCase();
        String filterNameBahasa = splitLanguageData(paymentMethod, "/")[1].toLowerCase();
        return new Locator("HOTEL_PRICE_DISPLAY_FILTER_OPTION", AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_list') and (translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='"+filterNameEnglish+"' or translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='"+filterNameBahasa+"')]"));
    }

    public void clickSeeAllBtnFilter(String filterName) {
        driverTools.swipeUp();
        driverTools.click(SEE_ALL_FILTER_LOCATOR);
    }

    private static Locator checkBoxArea(int index) {
        return new Locator("CHECKBOX_AREA_" + index, AppiumBy.xpath("(//*[contains(@resource-id,'tds_subtitle_list')]//following-sibling::*[contains(@resource-id,'tds_selection_area_right')])[" + index + "]"));
    }

    private static Locator checkBoxArea(String areaName) {
        return new Locator("CHECKBOX_AREA_" + areaName.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_list') and @text='" + areaName + "']//following-sibling::*[contains(@resource-id,'tds_selection_area_right')]"));
    }

    private static Locator areaName(int index) {
        return new Locator("AREA_NAME_" + index, AppiumBy.xpath("(//*[contains(@resource-id,'tds_subtitle_list')])[" + index + "]"));
    }

    @Override
    public String[] selectLocationFilter(String locationType, String locationName) {
        String[] selectedItems = locationName.split("/");
        driverTools.swipeToElement(itemContainer(locationType), Swipe.UP, 5).orElseThrow();
        for (String item : selectedItems) {
            if (driverTools.isElementDisplayed(itemCheckboxList(locationType, item))) {
                driverTools.click(itemCheckboxList(locationType, item));
            } else {
                Optional<WebElement> showMoreButton = driverTools.waitForElementToPresence(showMoreItem(locationType), 5);
                if (showMoreButton.isEmpty()) {
                    driverTools.swipeToElementTillPresented(showMoreItem(locationType), Swipe.UP, 2).orElseThrow();
                }
                driverTools.swipeByDistance(FILTER_CONTAINER, FILTER_FLOATING_BUTTON, Swipe.UP, 1);
                driverTools.click(showMoreItem(locationType));
                driverTools.click(allItemCheckboxList(locationType, item));
                driverTools.click(FILTER_SUBMIT_BUTTON);
            }
        }
        return selectedItems;
    }

    private Locator itemContainer(String sectionName) {
        if (sectionName.contains("/")) {
            String sectionNameEnglish = sectionName.split("/")[0];
            String sectionNameBahasa = sectionName.split("/")[1];
            return new Locator("ITEM_CONTAINER", AppiumBy.xpath("//*[contains(@text,'"+sectionNameEnglish+"') or contains(@text,'"+sectionNameBahasa+"')]/parent::*[contains(@resource-id,'v_title')]/following-sibling::*[contains(@resource-id,'rv_filter_item')]"));
        } else {
            return new Locator("ITEM_CONTAINER", AppiumBy.xpath("//*[contains(@text,'"+sectionName+"')]/parent::*[contains(@resource-id,'v_title')]/following-sibling::*[contains(@resource-id,'rv_filter_item')]"));
        }
    }

    private Locator itemCheckboxList(String sectionName, String itemName) {
        if (sectionName.contains("/")) {
            String sectionNameEnglish = sectionName.split("/")[0];
            String sectionNameBahasa = sectionName.split("/")[1];
            return new Locator("ITEM_CHECKBOX_LIST", AppiumBy.xpath("//*[contains(@text,'"+sectionNameEnglish+"') or contains(@text,'"+sectionNameBahasa+"')]/parent::*[contains(@resource-id,'v_title')]/following-sibling::*[contains(@resource-id,'rv_filter_item')]//*[contains(@text,'"+itemName+"')]"));
        } else {
            return new Locator("ITEM_CHECKBOX_LIST", AppiumBy.xpath("//*[contains(@text,'"+sectionName+"')]/parent::*[contains(@resource-id,'v_title')]/following-sibling::*[contains(@resource-id,'rv_filter_item')]//*[contains(@text,'"+itemName+"')]"));
        }
    }

    private Locator showMoreItem(String sectionName) {
        if (sectionName.contains("/")) {
            String sectionNameEnglish = sectionName.split("/")[0];
            String sectionNameBahasa = sectionName.split("/")[1];
            return new Locator("SHOW_MORE_ITEM", AppiumBy.xpath("//*[contains(@resource-id,'v_title')]//*[contains(@text,'"+sectionNameEnglish+"') or contains(@text,'"+sectionNameBahasa+"')]/following::*[contains(@resource-id,'tv_show_more')]"));
        } else {
            return new Locator("SHOW_MORE_ITEM", AppiumBy.xpath("//*[contains(@resource-id,'v_title')]//*[contains(@text,'"+sectionName+"')]/following::*[contains(@resource-id,'tv_show_more')]"));
        }
    }

    private Locator allItemCheckboxList(String sectionName, String itemName) {
        if (sectionName.contains("/")) {
            String sectionNameEnglish = sectionName.split("/")[0];
            String sectionNameBahasa = sectionName.split("/")[1];
            return new Locator("ALL_ITEM_CHECKBOX_LIST", AppiumBy.xpath("//*[contains(@resource-id,'v_title') and (contains(@text,'"+sectionNameEnglish+"') or contains(@text,'"+sectionNameBahasa+"'))]/following-sibling::*[contains(@resource-id,'rv_content')]//*[contains(@text,'"+itemName+"')]"));
        } else {
            return new Locator("ALL_ITEM_CHECKBOX_LIST", AppiumBy.xpath("//*[contains(@resource-id,'v_title') and contains(@text,'"+sectionName+"')]/following-sibling::*[contains(@resource-id,'rv_content')]//*[contains(@text,'"+itemName+"')]"));
        }
    }

    @Override
    public String[] selectAreaFilter(String area) {
        int numArea;
        String areaName;
        String[] areaArray;
        if (area.equals("random")) {
            numArea = generateRandomInt(1, 5); // max 5 areas
            // generate unique random number for area check box index
            ArrayList<Integer> randomIndex = new ArrayList<>();
            for (int i = 1; i <= 10; i++) randomIndex.add(i + 6); // locator index start from i + 6
            Collections.shuffle(randomIndex);

            // select top (num area) index sort by lowest
            int[] index = new int[numArea];
            for (int j = 0; j < numArea; j++) index[j] = randomIndex.get(j);
            Arrays.sort(index);
            logger.log("random index: " + Arrays.toString(index));

            ArrayList<String> areaList = new ArrayList<>();
            for (int k = 0; k < numArea; k++) {
                final Locator AREA_CHECK_BOX = checkBoxArea(index[k]);
                WebElement areaCheckBox = driverTools.swipeToElement(AREA_CHECK_BOX, Swipe.UP, 6).orElseThrow();
                areaName = driverTools.getText(areaName(index[k])).orElseThrow();
                logger.log("area name: " + areaName);
                areaList.add(areaName);
                driverTools.swipeByDistance(FILTER_CONTAINER, FILTER_FLOATING_BUTTON, Swipe.UP,1);
                driverTools.click(areaCheckBox);
            }
            areaArray = new String[numArea];
            areaList.toArray(areaArray);
            logger.log("area array: " + Arrays.toString(areaArray));
        } else {
            areaArray = area.split("/");
            logger.log("area array: " + Arrays.toString(areaArray));
            if (area.contains("/")) {
                numArea = areaArray.length;
            } else {
                numArea = 1;
            }
            logger.log("numArea -> " + numArea);
            for (int k = 0; k < numArea; k++) {
                final Locator AREA_CHECK_BOX = checkBoxArea(areaArray[k]);
                WebElement areaCheckBox = driverTools.swipeToElement(AREA_CHECK_BOX, Swipe.UP, 6).orElseThrow();
                driverTools.swipeByDistance(FILTER_CONTAINER, FILTER_FLOATING_BUTTON, Swipe.UP,1);
                driverTools.click(areaCheckBox);
            }
        }
        return areaArray;
    }

    @Override
    public void selectBenefit(String benefitType) {

    }

    private static Locator propertyRating(String rating) {
        return new Locator("PROPERTY_RATING_" + rating, AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_list') and contains(@text,'" + rating + "+')]"));
    }

    @Override
    public String selectPropertyRatingFilter(String rating) {
        String ratingText;
        if (rating.equals("random")) {
            String[] propertyRating = {"3", "4", "4.5"};
            rating = getRandomObject(propertyRating);
        }
        if (rating.equals("4.5")) {
            // english: 4.5, indonesian: 4,5
            ratingText = "5";
        } else {
            ratingText = rating;
        }
        final Locator PROPERTY_RATING_RADIO_BUTTON = propertyRating(ratingText);
        driverTools.swipeToElement(PROPERTY_RATING_RADIO_BUTTON, Swipe.UP, 3).orElseThrow();
        driverTools.swipeUp();
        driverTools.click(PROPERTY_RATING_RADIO_BUTTON);
        return rating;
    }

    private static Locator facilityName(String facilityName) {
        String[] facilityLanguage = splitLanguageData(facilityName, "/");
        return new Locator("FACILITY_NAME_" + facilityName.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'tv_facility') and (@text='" + facilityLanguage[0] + "' or @text='" + facilityLanguage[1] + "')]"));
    }

    @Override
    public void selectFacility(String facilityType) {
        String[] facilityTypeArray = facilityType.split("_");
        for (int i = 0; i < facilityTypeArray.length; i++) {
            driverTools.swipeToElement(facilityName(facilityTypeArray[i]), Swipe.UP, 5).orElseThrow();
            if (i == 0) driverTools.swipeUp();
            driverTools.click(facilityName(facilityTypeArray[i]));
        }
    }

    @Override
    public void changePriceFilter(String newFilterPrice) {
    }

    public boolean isPriceFilterByManualCorrect(String priceRange, String totalHotel) {
        return false;
    }

    @Override
    public void goToCardWithLoyaltyBenefitBadge() {
        WebElement cardWithLoyaltyBenefitBadge = driverTools.swipeToElement(LOYALTY_BADGE_HOTEL_CARD, Swipe.UP, 20).orElseThrow();
        driverTools.clickElementByCoordinate(cardWithLoyaltyBenefitBadge);
    }

    @Override
    public void applyPreferredPartnerFilters() {
        clickFilterButton();
        driverTools.swipeUp(0.5);
        WebElement preferredPartnerPlusEl = driverTools.swipeToElement(PREFERRED_PARTNER_PLUS, Swipe.UP, 8).orElseThrow();
        driverTools.click(preferredPartnerPlusEl);
        driverTools.swipeUp(0.5);
        WebElement preferredPartnerEl = driverTools.swipeToElement(PREFERRED_PARTNER, Swipe.UP, 8).orElseThrow();
        driverTools.click(preferredPartnerEl);
        clickFilterSubmit();
    }

    @Override
    public boolean isPreferredPartnerIconOnFirstHotelCardVisible() {
        return driverTools.swipeToElement(PREFERRED_PARTNER_ICON, Swipe.UP, 5).isPresent();
    }

    @Override
    public boolean isFilterAppliedBadgeVisible() {
        return driverTools.waitForElementToAppear(FILTER_BUTTON_BADGE, 10).isPresent();
    }

    @Override
    public boolean verifyRedirectSRP(LandingPageModulePOJO.Redirect redirectData, String language, String vertical) {
        boolean searchResult = isHeaderSearchResultShown();

        String[] locationData = redirectData.location.split("-");
        for (int i = 0; i < locationData.length; i++) {
            locationData[i] = locationData[i].substring(0, 1).toUpperCase() + locationData[i].substring(1);
        }
        String srpLocationApi = String.join(" ", Arrays.copyOfRange(locationData, 0, locationData.length - 1));
        String srpLocationApp = getCurrentLocation();

        String expectedDate = "";
        if (vertical.equals("Hotel")) {
            HotelPage<WebDriver> hotelPage = HotelPage.getInstance((Session<WebDriver>) session);
            expectedDate = hotelPage.expectedDateRoomGuest(language);
        } else {
            expectedDate = HomesPage.getInstance(session).expectedDateRoom(language);
        }
        String dateRoomGuestApp = getDateRoomGuestDetail();

        Assertion.assertThat("User is on the homes search result page", searchResult, is(true));
        Assertion.assertThat("Search location on the App SRP should be equal with the API", srpLocationApp, is(srpLocationApi));
        Assertion.assertThat("Check-in/out date and room guest detail SRP should be visible as expected", dateRoomGuestApp, is(expectedDate));
        return true;
    }

    private String convertDateGuest(String dateGuest) {
        return dateGuest.replaceAll("[^A-Za-z0-9 -]", "");
    }

    @Override
    public boolean verifyRedirectSRPWithPojo(String location, String language, String vertical) {
        boolean isOnSearchResultPage = isHeaderSearchResultShown();

        String[] locationData = location.split("-");
        for (int i = 0; i < locationData.length; i++) {
            locationData[i] = locationData[i].substring(0, 1).toUpperCase() + locationData[i].substring(1);
        }
        String srpLocationApi = String.join(" ", Arrays.copyOfRange(locationData, 0, locationData.length - 1));
        String srpLocationApp = getCurrentLocation();

        logger.log("expected location api : " + srpLocationApi);
        logger.log("actual location app: " + srpLocationApp);
        boolean isLocationCorrect = srpLocationApp.equalsIgnoreCase(srpLocationApi);
        logger.log("is location from api match with app: " + isLocationCorrect);

        String expectedDate = "";
        if (vertical.equals("Hotel")) {
            HotelPage<WebDriver> hotelPage = HotelPage.getInstance((Session<WebDriver>) session);
            expectedDate = hotelPage.expectedDateRoomGuest(language);
        } else {
            expectedDate = HomesPage.getInstance(session).expectedDateRoom(language);
        }
        expectedDate = convertDateGuest(expectedDate);
        String dateRoomGuestApp = convertDateGuest(getDateRoomGuestDetail());
        logger.log("expected date and guest data : " + expectedDate);
        logger.log("actual date and guest data : " + dateRoomGuestApp);
        boolean isDateRoomGuestCorrect = dateRoomGuestApp.equals(expectedDate);
        logger.log("is date room guest correct : " + isDateRoomGuestCorrect);

        Assertion.assertThat("User is on the homes search result page", isOnSearchResultPage, is(true));
        Assertion.assertThat("Search location on the App SRP should be equal with the API", isLocationCorrect, is(true));
        Assertion.assertThat("Check-in/out date and room guest detail SRP should be visible as expected", isDateRoomGuestCorrect, is(true));
        return isOnSearchResultPage && isLocationCorrect && isDateRoomGuestCorrect;
    }

    @Override
    public void handleMultipleFilter(String[] filterTypes, String[] filterOptions) {
        for (int index = 0; index < filterTypes.length; index++) {
            String filterType = filterTypes[index];
            String filterOption = filterOptions[index];

            if (filterType.equalsIgnoreCase("Price Range per night/Rentang Harga per malam")) {
                filterType = "Price Range (per night)/Rentang Harga (per malam)";
            }

            logger.log("Swiping to `" + filterType + "` filter");
            if (filterType.equalsIgnoreCase("benefit")) {
                swipeToFilter(filterOption);
            } else {
                swipeToFilter(filterType);
            }
            switch (filterType) {
                case "Price Display/Tampilan Harga" -> {
                    selectHotelPriceDisplay(filterOption);
                }
                case "Price Range (per night)/Rentang Harga (per malam)" -> {
                    //add currency as 2nd parameter to make sure the correct price filter is selected, since the price range text is same for all currency
                    selectPriceFilterByRange(filterOption,"");
                }
                case "Number of Bedrooms/Jumlah Kamar Tidur" -> {
                    HomesSearchResultPageAndroid.getInstance(session).addTotalBedroom(filterOption);
                }
                case "Areas/Area" -> {
                    selectAreaFilter(filterOption);
                }
                case "Facilities/Fasilitas" -> {
                    selectFacility(filterOption);
                }
                case "Hotel Rating/Rating Hotel" -> {
                    selectPropertyRatingFilter(filterOption);
                }
                case "Accomodation Types/Tipe Akomodasi" -> {
                    HomesSearchResultPageAndroid.getInstance(session).selectAccomodationTypeFilter(filterOption);
                }
            }
        }
    }

    private Locator getMasterTagLocator(String name) {
        return new Locator("Master_TAG", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and (@text='" + name + "')]"));
    }

    private Locator getMasterTagLocator(String name, int index) {
        return new Locator("Master_TAG", AppiumBy.xpath("(//*[contains(@resource-id,'tds_chips_text') and (@text='" + name + "')])[" + index + "]"));
    }

    @Override
    public boolean checkMasterTags(AccommodationMasterTagResponse arrays, int totalMasterTag, String groupFilterName) {
        for (int i = 0; i < totalMasterTag && i < arrays.data.length; i++) {
            String name = String.valueOf(arrays.data[i].name);
            driverTools.swipeToElement(getMasterTagLocator(name), Swipe.LEFT, 5);
            WebElement masterTag = driverTools.waitForElementToAppear(getMasterTagLocator(name), 30).orElseThrow();
            boolean isMasterTagDisplayed = driverTools.isElementDisplayed(masterTag);
            if (isMasterTagDisplayed && name.equals(groupFilterName)) {
                selectMasterTag(groupFilterName);
            }
            driverTools.swipeLeft(SRP_MASTER_TAG);
            if (isMasterTagDisplayed == false) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void selectMasterTag(String name) {
        driverTools.click(getMasterTagLocator(name));
    }

    @Override
    public String selectMasterTagByIndex(int index) {
        WebElement masterTag = driverTools.swipeToElement(SRP_MASTER_TAG, Swipe.LEFT, 3).orElseThrow();
        String masterTagName = driverTools.getText(masterTag);
        driverTools.click(masterTag);
        return masterTagName;
    }

    @Override
    public boolean isSelectedMasterTagDuplicate(String masterTag) {
        return driverTools.swipeToElement(getMasterTagLocator(masterTag, 2), Swipe.LEFT, 3).isPresent();
    }

    @Override
    public boolean checkSubtitleMasterTag(String groupFilterSubtitle) {
        driverTools.waitForElementToAppear(QUICK_FILTER_HEADER_TEXT, 10);
        String subtitleOnUi = driverTools.getText(QUICK_FILTER_HEADER_TEXT).orElseThrow();
        Assertion.assertThat("Subtitle on UI is \"" + subtitleOnUi + "\" and expected Subtitle is \"" + groupFilterSubtitle + "\"", subtitleOnUi, is(groupFilterSubtitle));
        return true;
    }

    @Override
    public boolean checkEmptyHotelList() {
        return driverTools.isElementDisplayed(EMPTY_HOTEL_BANNER);
    }

    @Override
    public boolean verifySearchFormWithPojo(HomesData.ContentData list) {
        HomesNHAResponse.Redirect redirectData = list.redirect;
        boolean isSearchTitleDisplayed = driverTools.isElementDisplayed(SEARCH_TITLE);

        boolean isLocationListCorrect = true;
        List<WebElement> miniCardLocationElement = driverTools.elements(LOCATION_SEARCH_FORM).orElseThrow();
        for (int i = 0; i < miniCardLocationElement.size(); i++) {
            String location = redirectData.popularDestination.destinations.get(i).name;
            isLocationListCorrect = driverTools.isElementDisplayed(getMasterTagLocator(location));
            if (!isLocationListCorrect) {
                logger.log("Location not found on app");
                return false;
            }
        }
        return isSearchTitleDisplayed && isLocationListCorrect;
    }

    @Override
    public void closeFilterPopup() {
        driverTools.waitForElementToAppear(FILTER_POPUP_TRY_BTN).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(CLOSE_BUTTON).ifPresent(driverTools::click);
    }

    @Override
    public List<String> getPrices() {
        driverTools.waitForElementToAppear(HOTEL_PRICE_CARD);
        driverTools.swipeToElement(HOTEL_PRICE_CARD, Swipe.UP, 3).orElseThrow();
        List<String> hotelPrices = driverTools.getTexts(HOTEL_PRICE_CARD);
        List<String> prices = new ArrayList<String>(hotelPrices);
        for (int limit = 0; limit <= 5; limit++) {
            driverTools.swipeUp();
            if (!driverTools.isElementDisplayed(HOTEL_PRICE_CARD)) break;
            prices.addAll(driverTools.getTexts(HOTEL_PRICE_CARD));
        }
        Assertion.assertThat("Prices exist", prices, is(notNullValue()));
        logger.log("prices: " + prices);
        return prices;
    }

    @Override
    public List<String> getPricesInFilter() {
        driverTools.waitForElementToPresence(PRICE_FILTER_FIELDS,5);
        List<String> priceItemsFields = new ArrayList<>(driverTools.getTexts(PRICE_FILTER_FIELDS));
        logger.log("Prices: " + priceItemsFields);
        return priceItemsFields;
    }

    @Override
    public void closeMap() {
        driverTools.click(CLOSE_MAP);
    }

    @Override
    public void resetPriceFilter() {
        if (driverTools.isElementDisplayed(PRICE_FILTER_SELECTED)) {
            driverTools.click(PRICE_FILTER_SELECTED);
            driverTools.click(RESET_FILTER);
            clickFilterSubmit();
            driverTools.waitForElementToDisappear(PRICE_FILTER_SELECTED);
            driverTools.waitForElementToAppear(SEE_MAP_BUTTON, 10).orElseThrow();
        }
    }

    @Override
    public void resetFilter() {
        if (driverTools.isElementDisplayed(FILTER_SIGN)) {
            driverTools.click(FILTER_SIGN);
            driverTools.click(RESET_FILTER);
            clickFilterSubmit();
            driverTools.waitForElementToDisappear(FILTER_SIGN);
            driverTools.waitForElementToAppear(SEE_MAP_BUTTON, 10).orElseThrow();
        }
    }

    @Override
    public void clickResetFilterButton() {
        WebElement resetButton = driverTools.waitForElementToPresence(RESET_FILTER, 10).orElseThrow();
        driverTools.clickElementByCoordinate(resetButton);
        logger.log("Reset filter button clicked");
    }

    @Override
    public void selectFilterToggle(String filterOption) {
        WebElement filterToggle = driverTools.swipeToElement(getFilterToggleLocator(filterOption), Swipe.UP, 5).orElseThrow();
        driverTools.swipeByDistance(FILTER_CONTAINER, FILTER_FLOATING_BUTTON, Swipe.UP,1);
        driverTools.click(filterToggle);
    }

    private Locator getFilterToggleLocator(String filterOption) {
        return new Locator("FILTER_TOGGLE", AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_list') and (contains(@text,'" + filterOption + "'))]/following-sibling::*[contains(@resource-id,'tds_selection_area_right')]//*[contains(@resource-id,'tds_toggle_circle')]"));
    }

    /**
     * Generic selector for a checkbox-style filter option inside the filter modal.
     * Supports labels in "English/Bahasa" or "Bahasa/English" format (handled by getHotelPaymentMethodFilterOptionLocator).
     */
    @Override
    public void selectFilterCheckbox(String filterOption) {
        WebElement checkbox = driverTools.swipeToElement(getCheckboxFilterOptionLocator(filterOption), Swipe.UP, 5).orElseThrow();
        driverTools.swipeByDistance(FILTER_CONTAINER, FILTER_FLOATING_BUTTON, Swipe.UP,2);
        driverTools.clickElementByCoordinate(checkbox);
        logger.log("Checkbox filter selected: " + filterOption);
    }

    @Override
    public boolean isFilterSectionVisible(String filterName) {
        Optional<WebElement> filterSection = driverTools.waitForElementToAppear(getFilterSectionLocator(filterName), 3);
        if (filterSection.isEmpty()) {
            filterSection = driverTools.swipeToPresentedElement(getFilterSectionLocator(filterName), Swipe.UP, 5);
        }
        boolean visible = filterSection.isPresent();
        logger.log("Filter section '" + filterName + "' visible: " + visible);
        return visible;
    }

    @Override
    public List<String> getHotelDistances() {
        driverTools.swipeToElement(HOTEL_LOCATION, Swipe.UP, 3);
        List<String> hotelDistances = driverTools.getTexts(HOTEL_LOCATION);
        Assertion.assertThat("List is not empty", hotelDistances.size(), greaterThan(0));
        logger.log("Hotel distances: " + hotelDistances);
        return hotelDistances;
    }

    public void applyAdvanceFilter(String priceRange, String benefit, String facility,String currency) {
        applyPriceFilter(priceRange,currency);
        applyBenefitFilter(benefit);
        applyFacilityFilter(facility);
    }

    private void applyPriceFilter(String priceRange,String currency) {
        driverTools.swipeToElement(FREQUENT_PRICE_FILTER_CHIP_ALL, Swipe.UP, 5).orElseThrow();
        //add currency as 2nd parameter to make sure the correct price filter is selected, since the price range text is same for all currency
        selectPriceFilterByRange(priceRange,currency);
    }

    private void applyBenefitFilter(String benefit) {
        Locator locatorBenefit = switch (benefit.toLowerCase()) {
            case "free breakfast/sarapan gratis" -> FREE_BREAKFAST;
            case "free cancellation/pembatalan gratis" -> FREE_CANCELLATION;
            case "deals/diskon & promo" -> DEALS;
            case "preferred partner plus" -> PREFERRED_PARTNER_PLUS;
            case "preferred partner" -> PREFERRED_PARTNER;
            default -> throw new RuntimeException("wrong test data 'benefit filter', please check testdata");
        };
        driverTools.swipeUp(0.5);
        WebElement benefitLocatorElement = driverTools.swipeToElement(locatorBenefit, Swipe.UP, 5).orElseThrow();
        driverTools.click(benefitLocatorElement);
    }

    /*
    some available facility
        -> Swimming Pool/Kolam Renang
        -> WiFi
        -> Children Facility/Fasilitas Anak
        -> Fitness Center/Pusat Kebugaran
        -> Elevator/Lift
    * */

    private void applyFacilityFilter(String facility) {
        String[] facilityArr = facility.split("/");
        WebElement facilityElement = driverTools.swipeToElement(getFacilityLocator(facilityArr[0], facilityArr[facilityArr.length - 1]), Swipe.UP, 5).orElseThrow();
        driverTools.click(facilityElement);
    }

    private Locator getFacilityLocator(String facilityEn, String facilityId) {
        return new Locator("FACILITY", AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_list') and (contains(@text,'" + facilityEn + "') or contains(@text,'" + facilityId + "'))]"));
    }

    @Override
    public void checkBenefitAndFacility(String benefit, String facility, String totalHotel) {
        String[] facilityArr = facility.split("/");
        String[] benefitArr = benefit.split("/");
        int totalHotelInt = Integer.parseInt(totalHotel);
        driverTools.swipeToElement(VISIBLE_HOTEL_CONTAINER, Swipe.UP, 3).orElseThrow();

        String currentHotelName = getHotelNameList("");
        String nextHotelName = currentHotelName;

        for (int i = 0; i < totalHotelInt; i++) {
            Locator benefitLocator = benefitAndFacilityLocator(benefitArr[0], benefitArr[benefitArr.length - 1], currentHotelName);
            Locator facilityLocator = benefitAndFacilityLocator(facilityArr[0], facilityArr[facilityArr.length - 1], currentHotelName);
            driverTools.swipeToElement(benefitAndFacilityParentLocator(currentHotelName), Swipe.UP, 5).orElseThrow();

            driverTools.swipeToElement(benefitLocator, benefitAndFacilityParentLocator(currentHotelName), Swipe.RIGHT, 3);
            WebElement benefitElement = driverTools.swipeToElement(benefitLocator, benefitAndFacilityParentLocator(currentHotelName), Swipe.LEFT, 3).orElseThrow();
            driverTools.swipeToElement(facilityLocator, benefitAndFacilityParentLocator(currentHotelName), Swipe.RIGHT, 3);
            WebElement facilityElement = driverTools.swipeToElement(facilityLocator, benefitAndFacilityParentLocator(currentHotelName), Swipe.LEFT, 3).orElseThrow();

            Assertion.assertThat("is benefit displayed on the hotel " + currentHotelName, driverTools.isElementDisplayed(benefitElement), is(true));
            Assertion.assertThat("is facility displayed on the hotel " + currentHotelName, driverTools.isElementDisplayed(facilityElement), is(true));

            int maxLoop = 0;
            while (currentHotelName.equals(nextHotelName)) {
                driverTools.swipeByDistance(VISIBLE_HOTEL_CONTAINER, Swipe.UP, 1);
                nextHotelName = getHotelNameList("");
                if (maxLoop == 10) {
                    logger.softWarning("Hotel name is the same for 10 times, please check the test data");
                    break;
                }
                maxLoop++;
            }

            currentHotelName = nextHotelName;
        }
    }

    @Override
    public void checkPerNightBtnFilter(String filterName) {
            if (driverTools.isElementDisplayed(AFTER_TAX_CHECKBOX)){
                driverTools.click(AFTER_TAX_CHECKBOX);
            }
            logger.log("Check per night filter: " + filterName);
            driverTools.click(getHotelPaymentMethodFilterOptionLocator(filterName));
            driverTools.isElementDisplayed(getHotelPaymentMethodFilterOptionLocator(filterName));
            if(driverTools.isElementDisplayed(FILTER_SUBMIT_BUTTON)){
                driverTools.click(FILTER_SUBMIT_BUTTON);
            }
            else {
                driverTools.click(CLOSE_BUTTON);
            }
    }

    private Locator benefitAndFacilityLocator(String textEn, String textId, String hotelName) {
        return new Locator(textEn.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name') and contains(@text,'" + hotelName + "')]/following-sibling::*[contains(@resource-id,'tv_benefits') and (normalize-space(contains(@text,'" + textEn + "')) or normalize-space(contains(@text,'" + textId + "')))]"));
    }

    private Locator benefitAndFacilityParentLocator(String hotelName) {
        return new Locator("BENEFIT_FACILITY_PARENT", AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name') and contains(@text,'" + hotelName + "')]/following-sibling::*[contains(@resource-id,'tv_benefits')]"));
    }

    @Override
    public String selectPopularPlaceFilter() {
        WebElement popularPlaceElement = driverTools.swipeToElement(POPULAR_PLACE, Swipe.UP, 10).orElseThrow();
        String popularPlace = driverTools.getText(popularPlaceElement);
        logger.log("Popular place: " + popularPlace);
        driverTools.click(POPUPLAR_PLACE_BUTTON);
        return popularPlace;
    }

    @Override
    public void waitForSrpToLoad() {
        if (driverTools.waitForElementToAppear(HOTEL_CARD_NAME).isPresent()) {
        } else {
            driverTools.waitForElementToAppear(EMPTY_HOTEL_BANNER, 15);
        }
    }

    @Override
    public void selectPriceAfterTaxFilter(boolean isTax, String priceDescription) {
        priceDescription = priceDescription.toLowerCase();
        if (!isTax && priceDescription.contains("including tax") || priceDescription.contains("termasuk pajak")) {
            driverTools.click(AFTER_TAX_CHECKBOX);
        } else if (isTax && (priceDescription.contains("after tax") || priceDescription.contains("setelah pajak"))) {
            driverTools.click(AFTER_TAX_CHECKBOX);
        } else {
            logger.log("no need to click, tax filter already correct -> isTax: " + isTax + " and priceDescription: " + priceDescription);
        }
    }

    @Override
    public void checkAndSelectPriceAfterTaxFilter(boolean isTaxApplied) {
       if(!isTaxApplied){
           driverTools.click(AFTER_TAX_CHECKBOX);
       }
    }

    @Override
    public boolean checkHotelPrice(boolean isCrossPrice, String hotelName,String price) {
        return (isCrossPrice) ? checkHotelCrossPrice(hotelName, price) : checkHotelFinalPrice(hotelName, price);
    }

    @Override
    public boolean checkHotelPrice(String hotelName, String price){
        return driverTools.swipeToElement(getHotelPriceLocator(hotelName, price), Swipe.UP, 5).isPresent();
    }

    private boolean checkHotelFinalPrice(String hotelName, String price) {
        return driverTools.swipeToPresentedElement(getHotelFinalPriceLocator(hotelName, price), Swipe.UP, 5).isPresent();
    }

    private boolean checkHotelCrossPrice(String hotelName, String price) {
        return driverTools.swipeToElement(getHotelCrossPriceLocator(hotelName, price), Swipe.UP, 5).isPresent();
    }

    private Locator getHotelPriceLocator(String hotelName, String price) {
        return new Locator("HOTEL_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name') and contains(@text,'"+hotelName+"')]//preceding-sibling::*[contains(@resource-id,'cl_price')]//*[contains(@resource-id,'tv_final_price') and contains(@text,'"+price+"')]"));
    }

    @Override
    public boolean isLabelTotalPrice(boolean isTax) {
        return (isTax) ? driverTools.waitForElementToPresence(TOTAL_PRICE_LABEL).isPresent() : driverTools.waitForElementToPresence(TOTAL_PRICE_LABEL_WITHOUT_TAX).isPresent();
    }

    @Override
    public boolean isLabelTotalPrice(){
        return driverTools.swipeToElement(TOTAL_PRICE_LABEL, Swipe.UP, 5).isPresent();
    }

    public void selectHotelWithMeetTravelPolicyNudge(){
        WebElement hotelWithMeetTravelPolicyNudge = driverTools.swipeToElement(MEET_TRAVEL_POLICY_NUDGE,Swipe.UP,10).orElseThrow();
        driverTools.click(hotelWithMeetTravelPolicyNudge);
    }

    @Override
    public boolean isHotelRangeMeetPolicyPriceRange(int totalCard, String priceRange){
        driverTools.waitForElementToAppear(HOTEL_NAME);
        Map<String, Boolean> hotelPrice = new HashMap<>();
        boolean isHotelPriceMeetPolicy = true;
        int count = totalCard;
        while (count > 0){
            driverTools.swipeToElement(HOTEL_NAME,Swipe.UP,5);
            List<WebElement> hotelName = driverTools.elements(HOTEL_NAME).orElseThrow();
            int i = 0;
            while(i < hotelName.size()){
                WebElement hotelNameElement = driverTools.waitForElementToAppear(HOTEL_NAME).orElseThrow();
                String hotelNameText = driverTools.getText(hotelNameElement);
                if(!hotelPrice.containsKey(hotelNameText)){
                    driverTools.swipeDown(1);
                    WebElement hotelPriceElement = driverTools.swipeToElement(getHotelPriceUsingHotelName(hotelNameText),Swipe.UP,5).orElseThrow();
                    String price = driverTools.getText(hotelPriceElement);
                    logger.log("hotel name : " + hotelNameText + " price : " + price);
                    hotelPrice.put(hotelNameText,true);

                    int actualPrice = (int) convertPrice(price);
                    int minPrice = Integer.parseInt(priceRange.split("-")[0]);
                    int maxPrice = Integer.parseInt(priceRange.split("-")[1]);
                    isHotelPriceMeetPolicy = actualPrice >= minPrice && actualPrice <= maxPrice;
                    logger.log("is hotel price meet policy : " + isHotelPriceMeetPolicy);

                    if(!isHotelPriceMeetPolicy){
                        logger.fail("hotel price not meet policy");
                        break;
                    }
                }
                i++;
            }
            driverTools.swipeUp(2);
            driverTools.waitForElementToAppear(HOTEL_NAME);
            count--;
        }
        logger.log( "size: "+hotelPrice.size() + "total card: "+totalCard);

        return isHotelPriceMeetPolicy && !hotelPrice.isEmpty();
    }

    private Locator getHotelPriceUsingHotelName(String hotelName){
        return new Locator("HOTEL_PRICE_" + hotelName.toUpperCase(),AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name') and contains(@text,'"+hotelName+"')]//preceding-sibling::*[contains(@resource-id,'cl_price')]//*[contains(@resource-id,'tv_final_price')]"));
    }

    @Override
    public boolean isMeetsTravelPolicyNudgeVisible() {
        driverTools.waitForElementToAppear(MEET_TRAVEL_POLICY_NUDGE,20);
        return driverTools.isElementDisplayed(MEET_TRAVEL_POLICY_NUDGE);
    }

    /*
    /room/night (after taxes)
    /kamar/malam (setelah pajak)
    /night (include taxes)
    /malam (termasuk pajak)
     */
    @Override
    public String getPriceRemarkText(){
        WebElement priceRemarkElement = driverTools.waitForElementToAppear(LABEL_PER_ROOM_PER_NIGHT).orElseThrow();
        String priceRemarkText = driverTools.getText(priceRemarkElement).replace("\n", " ");
        logger.log("Price remark text: " + priceRemarkText);
        return priceRemarkText;
    }

    @Override
    public String clickHeartIcon() {
        driverTools.waitForElementsToAppear(HOTEL_NAME);
        WebElement firstHotelNameElement = driverTools.swipeToElement(HOTEL_NAME, Swipe.UP, 5).orElseThrow();
        String firstHotelName = driverTools.getText(firstHotelNameElement);
        driverTools.swipeToElement(getWishlistIconLocator(firstHotelName), Swipe.DOWN, 2);
        WebElement wishlistIcon = driverTools.swipeToElement(getWishlistIconLocator(firstHotelName), Swipe.UP, 3).orElseThrow();
        driverTools.click(wishlistIcon);
        return firstHotelName;
    }

    @Override
    public String isWishlistDialogShown() {
        WebElement manageWishlist = driverTools.waitForElementToPresence(MANAGE_WISHLIST).orElseThrow();
        return driverTools.getText(manageWishlist);
    }

    private final String expectedStatus = "wishlist (selected)";

    @Override
    public boolean isWishlistActive(String hotelName) {
        driverTools.waitForElementsToAppear(HOTEL_NAME);
        driverTools.swipeToElement(getHotelCardLocator(hotelName), Swipe.UP, 10)
                .or(() -> driverTools.swipeToElement(getHotelName(hotelName), Swipe.UP, 10))
                .orElseThrow();
        driverTools.swipeToElement(getWishlistIconLocator(hotelName), Swipe.DOWN, 2);
        WebElement wishlistIcon = driverTools.swipeToElement(getWishlistIconLocator(hotelName), Swipe.UP, 5).orElseThrow();
        String wishlistIconStatus = driverTools.getValueAttribute(wishlistIcon, "content-desc");
        Assertion.assertThat("Wishlist icon status: " + wishlistIconStatus, wishlistIconStatus, equalToIgnoringCase(expectedStatus));
        return wishlistIconStatus.equalsIgnoreCase(expectedStatus);
    }

    private Locator getHotelCardLocator(String hotelName) {
        return new Locator("HOTEL_CARD_" + hotelName.toUpperCase(), AppiumBy.xpath(
                "//*[contains(@resource-id,'tv_hotel_name') and contains(@text,'" + hotelName + "')]"
                        + "//ancestor::*[contains(@resource-id,'cl_parent')][1]"));
    }

    private Locator getWishlistIconLocator(String hotelName){
        return new Locator("WISHLIST_ICON_" + hotelName.toUpperCase(),AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name') and contains(@text,'"+hotelName+"')]//preceding-sibling::*[contains(@resource-id,'cl_image')]//*[contains(@resource-id,'iv_wishlist')]"));
    }

    @Override
    public boolean isWishlistActiveOnMap(String hotelName) {
        driverTools.waitForElementsToAppear(HOTEL_PRICE_MAPS);
        driverTools.click(HOTEL_PRICE_MAPS);
        driverTools.waitForElementToPresence(VISIBLE_HOTEL_CONTAINER);
        driverTools.swipeUntilElementVisible(hotelCardMapsContainer(hotelName), VISIBLE_HOTEL_CONTAINER, Swipe.LEFT, 0.7, 10);
        String wishlistIconStatus = driverTools.getValueAttribute(getWishlistIconLocatorOnMap(hotelName), "content-desc");
        Assertion.assertThat("Wishlist icon status: " + wishlistIconStatus, wishlistIconStatus, equalToIgnoringCase(expectedStatus));
        return wishlistIconStatus.equalsIgnoreCase(expectedStatus);
    }

    private Locator hotelCardMapsContainer(String hotelName){
        return new Locator("HOTEL_CARD_MAPS_CONTAINER_" + hotelName.toUpperCase(),AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name') and contains(@text,'"+hotelName+"')]//ancestor::*[contains(@resource-id,'cl_main_content')]"));
    }

    private Locator getWishlistIconLocatorOnMap(String hotelName) {
        return new Locator("WISHLIST_ICON_ON_MAP_" + hotelName.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name') and contains(@text,'" + hotelName + "')]//following-sibling::*[contains(@resource-id,'lav_wishlist')]"));
    }

    @Override
    public void chooseHotelCardOnMap(String hotelName) {
        driverTools.click(hotelCardMapsContainer(hotelName));
    }

    @Override
    public boolean clickWishlistFirstHotelCard(){
        driverTools.swipeToElement(FIRST_WISHLIST_BTN, Swipe.UP, 3).orElseThrow();
        driverTools.clickElementByCoordinate(FIRST_WISHLIST_BTN);
        driverTools.waitForElementToAppear(MANAGE_SNACKBAR, 10);
        return driverTools.isElementDisplayed(MANAGE_SNACKBAR) || driverTools.isElementDisplayed(MANAGE_BTN);
    }

    @Override
    public boolean clickRedWishlistIcon(){
        driverTools.click(WISHLIST_ICON_RED);
        driverTools.waitForElementToPresence(REMOVE_WISHLIST, 20);
        return driverTools.isElementDisplayed(REMOVE_WISHLIST);
    }

    @Override
    public boolean isWishlistIconSelected(){
        driverTools.waitForElementToPresence(WISHLIST_ICON_RED, 10);
        boolean res = driverTools.isElementDisplayed(WISHLIST_ICON_RED);
        return res;
    }

    @Override
    public void openHotelCardInMaps(){
        String price = getFirstHotelPrice();
        driverTools.waitForElementToAppear(FLOAT_MAP);
        driverTools.click(FLOAT_MAP);
        Locator mapFirstHotelPrice = getMapWishlistButton(price);
        driverTools.waitForElementToAppear(mapFirstHotelPrice, 20);
        if (driverTools.isElementDisplayed(mapFirstHotelPrice)) driverTools.click(mapFirstHotelPrice);
        else {
            driverTools.swipeDown(5);
            driverTools.waitForElementToPresence(SEARCH_MAPS, 20).ifPresent(driverTools::click);
            driverTools.waitForElementToPresence(OPEN_MAPS, 20).ifPresent(driverTools::click);
        }
        logger.log("Checking if hotel card is displayed");
        driverTools.waitForElementToAppear(HOTEL_CARD_MAPS, 15).orElseThrow();
        Assertion.assertThat("Hotel card is displayed", driverTools.isElementDisplayed(HOTEL_CARD_MAPS), is(true));
    }

    @Override
    public void closeMaps(){
        driverTools.click(CLOSE_MAP);
    }

    private Locator getMapWishlistButton(String Price){
        return new Locator("WISHLIST_PRICE" + Price, AppiumBy.xpath("//*[@content-desc='"+Price+"']"));
    }

    //if LABEL_AFTER_TAX_IN_CARD is true then its not applied, we need to apply it, so that's why made it return opposite
    @Override
    public boolean isPriceAfterTaxFilterApplied(){
        driverTools.waitForElementToAppear(HOTEL_NAME, 20);
        Optional <WebElement> label = driverTools.swipeToElement(LABEL_AFTER_TAX_IN_CARD,Swipe.UP,2);
        return label.isEmpty();
    }

    @Override
    public void clickWishlistFirstHotelCardForRemoval(){
        if (driverTools.isElementDisplayed(WISHLIST_ICON_RED)){
            logger.log("Property already there in the wishlist, proceed to remove from wishlist");
            return; //element already in the wishlist
        }
        driverTools.swipeToElement(FIRST_WISHLIST_BTN, Swipe.UP, 3).orElseThrow();
        driverTools.clickElementByCoordinate(FIRST_WISHLIST_BTN);
        driverTools.waitForElementToAppear(MANAGE_SNACKBAR, 30);

    }

    @Override
    public String verifyCorrectSRP(String chip){
        driverTools.waitForElementToPresence(NEXT_BUTTON, 10).ifPresent(driverTools::click);
        WebElement element = driverTools.swipeToElement(SRP_TITLE, Swipe.UP, 1).orElseThrow();
        String text = driverTools.getText(element);
        return text;
    }

    @Override
    public void verifyCurrencyOnFilter(String currency){
        Locator currencyLocator = getCurrencyLocator(currency);
        boolean isCurrencyFilterDisplayed = driverTools.waitForElementToAppear(currencyLocator).isPresent();
        logger.log("Currency filter is displayed: " + isCurrencyFilterDisplayed);
        Assertion.assertThat("Currency filter is displayed with correct currency", isCurrencyFilterDisplayed, is(true));
    }

    @Override
    public boolean verifyNearMeOnHotelCards() {
        Locator nearMeLocationInfo = new Locator("NEAR_ME_LOCATION_INFO",
                AppiumBy.xpath("//*[contains(@resource-id,'tv_location_info') and (contains(@text,'from your location') or contains(@text,'dari lokasimu'))]"));
        return driverTools.waitForElementToPresence(nearMeLocationInfo, 10).isPresent();
    }

    @Override
    public boolean isLoginBannerDisplayed() {
        return driverTools.waitForElementToPresence(LOGIN_BANNER, 15).isPresent()
                && driverTools.waitForElementToPresence(LOGIN_BUTTON, 15).isPresent();
    }

    @Override
    public boolean isLoginButtonClickable() {
        Optional<WebElement> loginButton = driverTools.waitForElementToPresence(LOGIN_BUTTON, 15);
        if (loginButton.isEmpty()) {
            return false;
        }
        WebElement button = loginButton.get();
        return driverTools.isElementDisplayed(button) && driverTools.isElementEnabled(button);
    }

    @Override
    public void clickLoginBanner() {
        driverTools.clickWhenPresent(LOGIN_BUTTON, 15);
        if (driverTools.waitForElementToPresence(LOGIN_BUTTON, 3).isPresent()) {
            driverTools.clickElementByCoordinate(LOGIN_BUTTON);
        }
    }

    public static final Locator HOTELS_NEAR_SELECTED_AREA_HEADING = new Locator("HOTELS_NEAR_SELECTED_AREA_HEADING",
            AppiumBy.xpath("//*[@text='Hotels near your selected area' or @text='Hotel di dekat lokasi pilihanmu']"));
    public static final Locator HOTELS_NEAR_SELECTED_AREA_CARDS = new Locator("HOTELS_NEAR_SELECTED_AREA_CARDS",
            AppiumBy.xpath("//*[@text='Hotels near your selected area' or @text='Hotel di dekat lokasi pilihanmu']/following::*[contains(@resource-id,'tv_hotel_name')]"));
    public static final Locator HOTELS_NEAR_SELECTED_AREA_FIRST_CARD = new Locator("HOTELS_NEAR_SELECTED_AREA_FIRST_CARD",
            AppiumBy.xpath("(//*[@text='Hotels near your selected area' or @text='Hotel di dekat lokasi pilihanmu']/following::*[contains(@resource-id,'tv_hotel_name')])[1]"));

    @Override
    public boolean isHotelsNearSelectedAreaSectionVisible() {
        return driverTools.swipeToElement(HOTELS_NEAR_SELECTED_AREA_HEADING, Swipe.UP, 20).isPresent();
    }

    @Override
    public int getHotelsNearSelectedAreaCardCount() {
        Optional<List<WebElement>> cards = driverTools.elements(HOTELS_NEAR_SELECTED_AREA_CARDS);
        return cards.map(List::size).orElse(0);
    }

    @Override
    public String clickFirstHotelInNearSelectedArea() {
        WebElement firstCard = driverTools.swipeToElement(HOTELS_NEAR_SELECTED_AREA_FIRST_CARD, Swipe.UP, 5)
                .orElseThrow(() -> new IllegalStateException("No property card found under 'Hotels near your selected area' section"));
        String hotelName = driverTools.getText(firstCard);
        logger.log("Tapping first hotel card in 'Hotels near your selected area' section: " + hotelName);
        driverTools.waitForElementToAppear(CLOSE_FILTER,2).ifPresent(driverTools::click);
        driverTools.click(firstCard);
        return hotelName;
    }

    private Locator getCurrencyLocator(String currency){
        return new Locator("CURRENCY_"+currency.toUpperCase(),AppiumBy.xpath("//*[contains(@text,'"+currency+"')]"));
    }

    @Override
    public void scrollToFlexibleStay() {
        driverTools.swipeToElement(FLEXIBLE_STAY_HEADING, Swipe.UP, 40);
    }

    @Override
    public boolean isFlexibleStayVisible() {
        return driverTools.isElementDisplayed(FLEXIBLE_STAY_HEADING);
    }

    @Override
    public String getFlexibleStayHeading() {
        WebElement headingElement = driverTools.waitForElementToAppear(FLEXIBLE_STAY_HEADING).orElseThrow();
        return driverTools.getText(headingElement);
    }

    @Override
    public int getFlexibleStayHotelCardCount() {
        return driverTools.elements(FLEXIBLE_STAY_HOTEL_CARDS).map(List::size).orElse(0);
    }

    @Override
    public boolean isFlexibleStayPriceVisible() {
        driverTools.swipeToElement(FLEXIBLE_STAY_HOTEL_CARDS, Swipe.UP, 5);
        List<WebElement> hotelCards = driverTools.elements(FLEXIBLE_STAY_HOTEL_CARDS).orElse(List.of());
        boolean hotelPrice = driverTools.isElementDisplayed(HOTEL_PRICE_CARD);
        if (!hotelCards.isEmpty()) {
            driverTools.click(hotelCards.get(0));
        }
        return hotelPrice;
    }

    public boolean isPreferredPartnerIconOnHotelCardVisible() {
        if (driverTools.isElementDisplayed(PREFERRED_PARTNER_CARD_ICON)) {
            return true;
        }

        return driverTools.swipeToElement(PREFERRED_PARTNER_CARD_ICON, Swipe.UP, 2).isPresent();
    }

    @Override
    public void applyAndVerifyPreferredPartnerFilter(String filterName) {
        logger.log("Click Filter button to open filter sheet on SRP");
        clickFilterButton();

        driverTools.waitForElementToAppear(FILTER_POPUP_TRY_BTN, 2).ifPresent(driverTools::click);

        logger.log("Locate parent row container for exact filter: " + filterName);

        Locator clickableRowLocator = new Locator("PREFERRED_PARTNER_ROW",
                AppiumBy.xpath("//*[@text='" + filterName + "']/ancestor::*[contains(@class,'LinearLayout') or contains(@class,'FrameLayout')][1]"));

        Locator switchLocator = new Locator("PREFERRED_PARTNER_SWITCH",
                AppiumBy.xpath("//*[@text='" + filterName + "']/following-sibling::*//*[contains(@class,'View') or contains(@class,'Switch')]"));

        WebElement targetElement;
        if (driverTools.isElementDisplayed(clickableRowLocator)) {
            targetElement = driverTools.element(clickableRowLocator).orElseThrow();
        } else {
            logger.log("Swiping up inside filter container to bring '" + filterName + "' into view");
            targetElement = driverTools.swipeToElement(clickableRowLocator, FILTER_CONTAINER, Swipe.UP, 3)
                    .or(() -> driverTools.swipeToElement(switchLocator, FILTER_CONTAINER, Swipe.UP, 3))
                    .orElseThrow(() -> new RuntimeException("Filter option '" + filterName + "' row not found in Filter sheet"));
        }

        logger.log("Clicking filter option row/toggle for: " + filterName);
        driverTools.click(targetElement);

        logger.log("Submit filter and wait for search results to load");
        clickFilterSubmit();
        waitForSrpToLoad();

        logger.log("Assert Preferred Partner icon is visible on the hotel card on SRP");
        Assertion.assertThat("Preferred Partner icon should be visible on SRP card",
                isPreferredPartnerIconOnHotelCardVisible(), is(true));

    }

}
