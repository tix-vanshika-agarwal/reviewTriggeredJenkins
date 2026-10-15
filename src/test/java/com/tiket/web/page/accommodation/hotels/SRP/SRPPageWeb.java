package com.tiket.web.page.accommodation.hotels.SRP;

import com.tiket.apiPage.HotelApi;
import com.tiket.appium.PopupObserver;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.pojo.accommodation.hotel.SearchHotelResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.accommodation.hotels.PDP.PDPPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tiket.core.CoreFunctions.getRandomObject;
import static com.tiket.core.CoreFunctions.normalizeNonBreakingSpaceText;
import static com.tiket.core.CoreFunctions.parseDecimalNumber;
import static com.tiket.core.CoreFunctions.thousandSeparator;
import static com.tiket.core.CoreFunctions.toXPathLiteral;
import static com.tiket.web.page.common.home.HomePageWeb.*;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

public abstract class SRPPageWeb<T extends WebDriver> extends WebPage<T> {

    private static final Locator POPUP_GOT_IT = new Locator("POPUP_GOT_IT", new By.ByXPath("//button[contains(@class, 'Coachmark_coachmark_done_btn')]"));
    private static final Locator ROOM_INC_BUTTON = new Locator("ROOM_INC_BUTTON", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[2]"));
    private static final Locator ROOM_DEC_BUTTON = new Locator("ROOM_DEC_BUTTON", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[1]"));
    private static final Locator ADULT_INC_BUTTON = new Locator("ADULT_INC_BUTTON", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[4]"));
    private static final Locator ADULT_DEC_BUTTON = new Locator("ADULT_DEC_BUTTON", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[3]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[@data-testid='product-container-mobile-search-submit']"));
    private static final Locator SEARCH_BAR = new Locator("SEARCH_BAR", new By.ByXPath("//div[contains(@class, 'SearchBox_placeholder')]//input"));
    private static final Locator FLEXIBLE_STAY_CARD = new Locator("FLEXIBLE_STAY_CARD", new By.ByXPath("//div[contains(@class, 'InterceptSoldOut_intercept_sold_out')]//section[contains(@class,'Layout_icon')]"));
    private static final Locator FLEXIBLE_STAY_HEADING = new Locator("FLEXIBLE_STAY_HEADING", new By.ByXPath("//div[contains(@class, 'InterceptSoldOut_intercept_sold_out')]//h2"));
    private static final Locator FLEXIBLE_STAY_IMAGE = new Locator("FLEXIBLE_STAY_IMAGE", new By.ByXPath("(//div[contains(@class, 'InterceptSoldOut_intercept_sold_out')]//a//img)[1]"));
    private static final Locator FLEXIBLE_STAY_HOTEL_NAME = new Locator("FLEXIBLE_STAY_HOTEL_HEADING", new By.ByXPath("//div[contains(@class, 'InterceptSoldOut_intercept_sold_out')]//a//h2"));
    private static final Locator FLEXIBLE_STAY_TEXT = new Locator("FLEXIBLE_STAY_TEXT", new By.ByXPath(" //div[contains(@class, 'InterceptSoldOut_intercept_sold_out')]//a//*[contains(@class, 'sold')]"));
    private static final Locator HOTEL_TAG = new Locator("HOTEL_CARD",new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[2]"));
    private static final Locator MAP_ENTRY_BUTTON = new Locator("MAP_ENTRY_BUTTON", new By.ByXPath("//*[@data-testid='map-entry-button']"));
    private static final Locator MAP_WRAPPER = new Locator("MAP_WRAPPER", new By.ByXPath("//div[contains(@class, 'main_map_filter_button')]"));
    private static final Locator MAP_CHIP_BUTTON = new Locator("MAP_CHIP_BUTTON", new By.ByXPath("//button[contains(@class,'ProductPriceMarker')]"));
    private static final Locator SRP_MAP_CARD = new Locator("SRP_MAP_CARD", new By.ByXPath("//h3[contains(@class, 'SmallProductCard_hotel_name')]"));
    private static final Locator SRP_HOTEL_STAR = new Locator("SRP_HOTEL_STAR", new By.ByXPath("//div[contains(@class, 'HotelStarRating_container')]"));
    private static final Locator SRP_HOME_STAR = new Locator("SRP_HOME_STAR", new By.ByXPath("//div[contains(@class, 'NHAStarRating_nha_star_rating_image_wrapper') and contains(@class,'NHAStarRating_xs')]"));
    protected static final Locator SRP_LOADING = new Locator("SRP_LOADING", new By.ByXPath("//*[contains(@class, 'main_loading')]"));
    private static final Locator HOTEL_CARD = new Locator("HOTEL_CARD",new By.ByXPath("(//a[contains(@class,'FullProductCard_container') and @data-testid='full-product-card'])"));
    private static final Locator HOTEL_CARD_LIST = new Locator("HOTEL_CARD_LIST", new By.ByXPath("//a[contains(@class,'FullProductCard_container') and @data-testid='full-product-card']"));
    private static final Locator HOTEL_CARD_ADDRESS_SCOPED = new Locator("HOTEL_CARD_ADDRESS", new By.ByXPath(".//span[contains(@class,'FullProductCard_hotel_address')]"));
    private static final Locator HOTEL_CARD_NAME_SCOPED = new Locator("HOTEL_CARD_NAME", new By.ByXPath(".//h3[contains(@class,'FullProductCard_hotel_name')]//span"));
    private static final Locator HOTEL_CARD_BENEFITS_SCOPED = new Locator("HOTEL_CARD_BENEFITS", new By.ByXPath(".//*[contains(@class,'ProductBenefits_benefits_wrapper')]"));
    private static final Locator HOTEL_CARD_BENEFITS_OTHERS_SCOPED = new Locator("HOTEL_CARD_BENEFITS_OTHERS", new By.ByXPath(".//*[contains(@class,'ProductBenefits_others')]"));
    private static final Locator HOTEL_CARD_BENEFITS_TOOLTIP = new Locator("HOTEL_CARD_BENEFITS_TOOLTIP", new By.ByXPath("//*[contains(@class,'ProductBenefits_tooltip_item_list')]"));
    private static final Locator PREFERRED_PARTNER_ICON_SCOPED = new Locator("PREFERRED_PARTNER_ICON_SCOPED", new By.ByXPath(".//*[@alt='preferred partner icon']"));
    private static final Locator PREFERRED_PARTNER_ICON_TOOLTIP = new Locator("PREFERRED_PARTNER_ICON_TOOLTIP", new By.ByXPath("//*[contains(@class,'PreferredPartner_tooltip_content')]/div[contains(@class,'Text')]"));
    private static final Locator DONE_BUTTON = new Locator("DONE_BUTTON", new By.ByXPath("//div[contains(@class, 'Modal_modal_footer_button_group')]//button"));
    protected static final Locator ADVANCED_FILTER_CHIP = new Locator("ADVANCED_FILTER_CHIP", new By.ByXPath("//*[@data-testid='advanced-filter-chip']"));
    protected static final Locator ADVANCED_FILTER_SAVE = new Locator("ADVANCED_FILTER_SAVE", new By.ByXPath("//button[text()='Save' or text()='Simpan']"));
    protected static final Locator ADVANCED_FILTER_POPULAR_ITEM = new Locator("ADVANCED_FILTER_POPULAR_CHIP", new By.ByXPath("//*[contains(@class, 'PopularFilter_chips_wrapper')]//button[1]"));
    protected static final Locator ADVANCED_FILTER_SWITCH_INPUT_ITEM = new Locator("ADVANCED_FILTER_SWITCH_INPUT_ITEM", new By.ByXPath("//*[contains(@class, 'SwitchInput_content')]/following-sibling::*[contains(@class, 'SwitchInput_toggle')][1]"));
    protected static final Locator ADVANCED_FILTER_FACILITY_GROUP_ITEM = new Locator("ADVANCED_FILTER_FACILITY_GROUP_ITEM", new By.ByXPath("//*[@data-testid='facility'][1]"));
    protected static final Locator ADVANCED_FILTER_CHECKBOX_ITEM = new Locator("ADVANCED_FILTER_CHECKBOX_ITEM", new By.ByXPath("//*[contains(@class, 'CheckboxGroupInput_group')]//input[@data-testid='checkbox'][1]"));
    protected static final Locator QUICK_FILTER_SORT_CHIP = new Locator("QUICK_FILTER_SORT_CHIP", new By.ByXPath("//span[contains(@class, 'Chip_text') and contains(text(), 'Sort')]"));
    protected static final Locator QUICK_FILTER_SORT_ITEM = new Locator("QUICK_FILTER_SORT_ITEM", new By.ByXPath("//*[@data-testid='sort-container']//label[2]//span[contains(@class, 'Radio_checkbox')]"));
    protected static final Locator QUICK_FILTER_STAR_RATING_CHIP = new Locator("QUICK_FILTER_STAR_RATING_CHIP", new By.ByXPath("//button[contains(@class, 'Chip_chip')]//span[contains(text(), 'Star') or contains(text(), 'Bintang')]"));
    protected static final Locator QUICK_FILTER_STAR_RATING_ITEM = new Locator("QUICK_FILTER_STAR_ITEM", new By.ByXPath("//*[contains(@class, 'StarFilter_chip_options')]/button[1]"));
    protected static final Locator QUICK_FILTER_STAR_RATING_SAVE = new Locator("QUICK_FILTER_STAR_RATING_SAVE", new By.ByXPath("//*[contains(@class, 'PopupStarFilter_action_container')]//button[text()='Save' or text()='Simpan']"));
    private static final Locator MASTER_TAG = new Locator("MASTER_TAG", new By.ByXPath("//div[contains(@class, 'MasterTagFilter_chips_wrapper')]"));
    private static final Locator EMPTY_RESULT = new Locator("EMPTY_RESULT", new By.ByXPath("//div[contains(@class, 'NoSearchResult')]"));
    private static final Locator FIRST_MASTER_TAG_ITEM = new Locator("FIRST_MASTER_TAG_ITEM", new By.ByXPath("//div[contains(@class, 'MasterTagFilter_chips_wrapper')]//button[1]"));
    private static final Locator FIRST_MASTER_TAG_ACTIVE = new Locator("FIRST_MASTER_TAG_ITEM_ACTIVE", new By.ByXPath("//div[contains(@class, 'MasterTagFilter_chips_wrapper')]//button[1][@aria-checked='true']"));
    public static final Locator MAP_HOTEL_NAME = new Locator("MAP_HOTEL_NAME", AppiumBy.xpath("//h3[contains(@class, 'MapCarouselProductCard_title')]"));
    public static final Locator MAP_HOTEL_RATING = new Locator("MAP_HOTEL_RATING", AppiumBy.xpath("//div[contains(@class, 'ProductRatingAndReviews_rating_text')]"));
    public static final Locator MAP_HOTEL_IMAGE = new Locator("MAP_HOTEL_IMAGE", AppiumBy.xpath("//img[@data-testid='final-result-image' and contains(@src, '352d3142-829b-42ff-982d-c9f982571d05')]"));
    public static final Locator FIRST_POPULAR_PLACE_ITEM = new Locator("FIRST_POPULAR_PLACE_ITEM", new By.ByXPath("(//*[@data-testid='radio' and (@name='Popular Places' or @name='Tempat Populer')])[1]"));
    private static final Locator RECENTLY_SEARCHED_SECTION = new Locator("RECENTLY_SEARCHED_SECTION", new By.ByXPath("//*[@data-testid='recent_list_item_title']"));
    private static final Locator CLEAR_ALL_RECENTLY_SEARCHED_BUTTON = new Locator("CLEAR_ALL_RECENTLY_SEARCHED_BUTTON", new By.ByXPath("//*[@data-testid='recent_list_item_clear']"));
    private static final Locator HOTEL_NAME_MAP = new Locator("HOTEL_NAME_MAP", new By.ByXPath("//*[@data-testid='small-product-card']//*[contains(@class,'SmallProductCard_hotel_name')]"));
    private static final Locator HOTEL_STAR_RATING_MAP = new Locator("HOTEL_STAR_RATING_MAP", new By.ByXPath("//*[@data-testid='small-product-card']//*[@data-testid='property-star-rating-container']"));
    private static final Locator HOTEL_PRICE_MAP = new Locator("HOTEL_PRICE_MAP", new By.ByXPath("//*[@data-testid='small-product-card']//*[contains(@class,'SmallProductCard_final_price_wrapper')]"));
    public static final Locator HOTEL_NAME_SRP = new Locator("HOTEL_NAME_SRP", new By.ByXPath("//h3[contains(@class, 'FullProductCard_hotel_name')]//span"));
    private static final Locator HOTEL_PRICE_SRP = new Locator("HOTEL_PRICE_SRP", new By.ByXPath("//div[contains(@class,'PriceArea_final_price_wrapper')]//div"));
    private static final Locator HOTEL_REVIEWS_SRP = new Locator("HOTEL_REVIEWS_SRP", new By.ByXPath("//div[@data-testid='rating-review']//span"));
    private static final Locator HOTEL_CARD_REVIEW_SCOPED = new Locator("HOTEL_CARD_REVIEW_SCOPED", new By.ByXPath(".//div[@data-testid='rating-review']//span"));
    private static final Locator FILTER_CHIP_BADGE = new Locator("FILTER_CHIP_BADGE", new By.ByXPath("//*[contains(@class, 'Notif_color_green')]"));
    private static final Locator HOTEL_STAR_RATING_SRP = new Locator("HOTEL_STAR_RATING_SRP", new By.ByXPath(".//*[local-name()='svg' and contains(@data-testid,'hotel-star-rating-full')]/parent::div"));
    private static final Locator PRICE_TAG = new Locator("PRICE_TAG", new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]//div[contains(@class,'PriceModule_final')]"));
    private static final Locator HOTEL_LOCATION = new Locator("HOTEL_LOCATION", new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]//div[contains(@class,'address')]//span"));
    private static final Locator HOTEL_INPUT_LOCATION = new Locator("HOTEL_LOCATION", new By.ByXPath("(//span[contains(@class, 'index_desktop')])[1]"));
    private static final Locator HOTEL_CALENDAR = new Locator("HOTEL_CALENDAR", new By.ByXPath("(//span[contains(@class, 'index_desktop')])[2]"));
    private static final Locator HOTEL_ROOM_N_GUEST = new Locator("HOTEL_ROOM_N_GUEST", new By.ByXPath("(//span[contains(@class, 'index_desktop')])[3]"));
    private static final Locator HOTEL_IMAGE_CARD = new Locator("HOTEL_IMAGE_CARD", new By.ByXPath("((//a[contains(@class, 'ProductCard_container')])[1]//img)[1]"));
    private static final Locator SRP_MAP = new Locator("SRP_MAP", new By.ByXPath("(//div[contains(@class,'map')])[3]"));
    private static final Locator HOTEL_CARD_BENEFITS = new Locator("HOTEL_CARD_BENEFITS", new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]//div[contains(@class,'Benefits')]"));
    private static final Locator CURRENT_ROOM_AMOUNT = new Locator("CURRENT_ROOM_AMOUNT", new By.ByXPath("//*[@data-testid='room-section']//*[contains(@class,'QuantityEditor_quantity_input')]"));
    private static final Locator CURRENT_ADULT_AMOUNT = new Locator("CURRENT_ADULT_AMOUNT", new By.ByXPath("//*[@data-testid='adults-section']//*[contains(@class,'QuantityEditor_quantity_input')]"));
    private static final Locator STAR_FILTER_BUTTON = new Locator("STAR_FILTER_BUTTON", AppiumBy.xpath("//*[contains(@class,'desktop_chip_wrappe')]//*[contains(text(),'Bintang') or contains(text(),'Star')]"));
    private static final Locator SORT_BUTTON = new Locator("SORT_BUTTON", AppiumBy.xpath("//*[contains(@class,'desktop_chip_wrappe')]//*[contains(text(),'Urutkan') or contains(text(),'Sort')]"));
    private static final Locator STAR_OPTIONS = new Locator("STAR_OPTIONS", AppiumBy.xpath("//*[contains(@class,'StarFilter_chip_options')]"));
    private static final Locator SAVE_STAR_FILTER = new Locator("SAVE_STAR_FILTER", AppiumBy.xpath("//*[contains(@class,'PopupStarFilter')]//*[contains(@class,'button')]"));
    private static final Locator MEETS_TRAVEL_POLICY_NUDGE = new Locator("MEETS_TRAVEL_POLICY_NUDGE", AppiumBy.xpath("//*[contains(@class,'PropertyLabel') and text()='Meets Travel Policy' or text()='Sesuai Kebijakan']"));
    private static final Locator SEARCH_NOT_FOUND_IMAGE = new Locator("SEARCH_NOT_FOUND_IMAGE", AppiumBy.xpath("//*[contains(@class,'NoSearchResult_not_found_img')]"));
    private static final Locator HOTEL_PRICE_TAX = new Locator("HOTEL_PRICE_TAX", AppiumBy.xpath("//*[contains(@class,'ProductCard_price_tax')]/span"));
    private static final Locator LOCATION=new Locator("LOCATION",new By.ByXPath("(//span[contains(@class, 'index_desktop_with_ellipsis') and contains(@class, 'Text_weight_bold') and contains(@class, 'Text_variant_highEmphasis')])[1]"));
    private static final Locator CURRENCY_PRICE_FILTER(String currency){
        return new Locator("CURRENCY_PRICE_FILTER", By.xpath(String.format("//span[contains(@class, 'Chip_text') and contains(text(), '%s')]",currency)));
    }
    private static final Locator SRP_FACILITIES=new Locator("SRP_FACILITIES",new By.ByXPath("//div[contains(@class,'ProductBenefits_benefits_wrapper')]"));
    private static final Locator SRP_FACILITIES_OTHERS=new Locator("SRP_FACILITIES_OTHERS",new By.ByXPath("//*[contains(@class,'ProductBenefits_others')]"));
    private static final Locator SORT_BY_BUTTON = new Locator("SORT_BY_BUTTON", new By.ByXPath("(//button//span[contains(@class, 'mobile_chip_text_wrapper__')])[2]"));
    private static final Locator SORT_BY_LOWEST_BUTTON = new Locator("SORT_BY_LOWEST_BUTTON", new By.ByXPath("(//label[contains(@class, 'Sort_option_item') and contains(@class, 'Radio_radio')])[4]"));
    private static final Locator STAR_BUTTON = new Locator("STAR_BUTTON", new By.ByXPath("(//button[contains(@class, 'Chip_chip') and contains(@class, 'Chip_size_default')])[6]"));
    private static final Locator STAR_5 = new Locator("STAR_5", new By.ByXPath("(//span[contains(@class, 'Chip_text__') and contains(text(), '5')])[1]"));
    private static final Locator STAR_4 = new Locator("STAR_4", new By.ByXPath("(//span[contains(@class, 'Chip_text__') and contains(text(), '4')])[1]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[contains(@class,'Button_variant_primary') and (normalize-space()='Save' or normalize-space()='Simpan')]"));
    private static final Locator STAR_RATINGS = new Locator("STAR_RATINGS", new By.ByXPath("(//div[@data-testid='property-star-rating-container'])[2]"));
    private static final Locator LOC = new Locator("LOC", new By.ByXPath("//span[contains(@class,'index_mobile_text__Csl__')]"));
    private static final Locator NEAR_ME = new Locator("NEAR_ME", new By.ByXPath("//button[contains(@class,'ChangeSearchMobile_near_me_button')]"));
    public static final Locator LETS_SEARCH_BUTTON = new Locator("LETS_SEARCH_BUTTON", new By.ByXPath("//*[@data-testid='product-container-mobile-search-submit']"));
    public static final Locator CHANGE_BUTTON = new Locator("CHANGE_BUTTON", new By.ByXPath("//button[contains(@class,'index_mobile_button_change_search')]"));
    private static final Locator FULL_STARS = new Locator("FULL_STARS", new By.ByXPath(".//*[local-name()='svg' and contains(@data-testid,'hotel-star-rating-full')]"));
    private static final Locator HALF_STARS = new Locator("HALF_STARS", new By.ByXPath(".//*[local-name()='svg' and contains(@data-testid,'hotel-star-rating-half')]"));
    private static final Locator PRICE_BUTTON = new Locator("PRICE_BUTTON", new By.ByXPath("(//button[contains(@class,'Chip_chip__') and contains(@class,'PriceFilter_option_item__')])[3]"));
    private static final Locator PRICE_FILTER_BUTTON = new Locator("PRICE_FILTER_BUTTON", new By.ByXPath("(//button[contains(@class, 'Chip_chip') and .//span[contains(@class, 'Chip_icon_right')]])[2]"));
    private static final Locator HOTEL_PRICE = new Locator("HOTEL_PRICE", AppiumBy.xpath(".//div[contains(@class,'PriceModule_final_price_wrapper')]/div[contains(@class,'align_right')]"));
    private static final Locator PRICE_FILTER_SAVE = new Locator("PRICE_FILTER_SAVE", new By.ByXPath("//button[contains(@class,'Button_variant_primary') and (normalize-space()='Save' or normalize-space()='Simpan')]"));
    private static final Locator LOGIN_BANNER = new Locator("LOGIN_BANNER", new By.ByXPath(
            "//*[contains(text(),'Log in to enjoy the benefits!') or contains(text(),'Log in buat dapet benefitnya!')]"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", new By.ByXPath(
            "//button/*[normalize-space(text())='Log in' or normalize-space(text())='Masuk']"));
    private static final Locator TIX_POINT_SRP = new Locator("TIX_POINT_SRP", new By.ByXPath("//div[@data-testid='product-card-tix-points']//span")) ;
    private static final Locator POPULAR_FILTER_CHIP = new Locator("POPULAR_FILTER_CHIP", new By.ByXPath("//span[contains(@class, 'Chip_text__') and (text()='Popular Filter' or text()='Filter Populer')]")) ;
    private static final Locator POPULAR_FILTER_REVIEW_CHIP_ITEM = new Locator("POPULAR_FILTER_REVIEW_CHIP_ITEM", new By.ByXPath("//span[contains(@class, 'Chip_text__') and (normalize-space(text())='Review 4+ (Good)' or normalize-space(text())='Ulasan 4+ (Bagus)')]")) ;
    private static final Locator POPULAR_FILTER_STAR_RATING_CHIP = new Locator("POPULAR_FILTER_STAR_RATING_CHIP", new By.ByXPath("//span[contains(@class, 'Chip_text__') and (normalize-space(text())='4-star' or normalize-space(text())='4-bintang')]\n")) ;
    private static final Locator POPULAR_FILTER_SAVE = new Locator("POPULAR_FILTER_SAVE", new By.ByXPath("//button[contains(@class, 'Button_button__') and (normalize-space(text())='Save' or normalize-space(text())='Simpan')]")) ;
    private static final Locator POPULAR_FILTER_REVIEW_ACTIVE = new Locator("POPULAR_FILTER_REVIEW_ACTIVE", new By.ByXPath("//span[contains(@class, 'Chip_text__') and (normalize-space(text())='4+ (Good)' or normalize-space(text())='4+ (Bagus)')]")) ;
    private static final Locator POPULAR_FILTER_STAR_RATING_ACTIVE = new Locator("POPULAR_FILTER_STAR_RATING_ACTIVE", new By.ByXPath("//span[contains(@class, 'Chip_text__') and (normalize-space(text())='Star 4' or normalize-space(text())='Bintang 4')]")) ;
    private static final Locator FILTER_MENU_BUTTON = new Locator("FILTER_MENU_BUTTON", new By.ByXPath("//button[.//span[text()='Filter']]"));
    private static final Locator SAVE_FILTERS = new Locator("SAVE_FILTERS", new By.ByXPath("//button[.//span[contains(text(), 'Show results') or contains(text(), 'Tampilkan hasil')]]"));
    public static final Locator TOTAL_PRICE_TOGGLE = new Locator("TOTAL_PRICE_TOGGLE", new By.ByXPath("//*[contains(text(),'total price') or contains(text(),'total harga')]//following-sibling::*[contains(@class,'Toggle_container')]//label"));
    public static final Locator TOTAL_PRICE_TOGGLE_DISABLED = new Locator("TOTAL_PRICE_TOGGLE_DISABLED", new By.ByXPath("//*[contains(text(),'total price') or contains(text(),'total harga')]//following-sibling::*[contains(@class,'Toggle_container')]//*[@type='checkbox' and @disabled]"));
    public static final Locator SHOW_PRICE_INCLUDING_TAXES_TOGGLE_CHECKBOX = new Locator("SHOW_PRICE_INCLUDING_TAXES_TOGGLE_CHECKBOX",
            new By.ByXPath("//*[contains(@class,'FilterPriceRange_toggle_item')]/*[contains(text(),'pajak') or contains(text(),'taxes')]/following-sibling::*[contains(@class,'Toggle_container')]//*[@type='checkbox']"));
    public static final Locator SHOW_PRICE_INCLUDING_TAXES_TOGGLE_LABEL = new Locator("SHOW_PRICE_INCLUDING_TAXES_TOGGLE_LABEL",
            new By.ByXPath("//*[contains(@class,'FilterPriceRange_toggle_item')]/*[contains(text(),'pajak') or contains(text(),'taxes')]/following-sibling::*[contains(@class,'Toggle_container')]//label"));
    public static final Locator AFTER_TAX_TOGGLE = SHOW_PRICE_INCLUDING_TAXES_TOGGLE_LABEL;
    public static final Locator AFTER_TAX_TOGGLE_DISABLED = new Locator("AFTER_TAX_TOGGLE_DISABLED",
            new By.ByXPath("//*[contains(@class,'FilterPriceRange_toggle_item')]/*[contains(text(),'pajak') or contains(text(),'taxes')]/following-sibling::*[contains(@class,'Toggle_container')]//*[@type='checkbox' and @disabled]"));
    private static final Locator SHOW_PRICE_INCLUDING_TAXES_TOGGLE = new Locator("SHOW_PRICE_INCLUDING_TAXES_TOGGLE",
            new By.ByXPath("//span[contains(text(),'Show price including taxes') or contains(text(),'Tampilkan harga setelah pajak')]/ancestor::div[contains(@class,'FilterPriceRange_toggle_item')]//div[contains(@class,'Toggle_toggle')]"));
    private static final Locator FIRST_CARD_PRICE_SECTION = new Locator("FIRST_CARD_PRICE_SECTION",
                new By.ByXPath("(//a[@data-testid='full-product-card']//div[contains(@class,'PriceArea')])[1]"));
    private static final Locator TOTAL_PRICE_LABEL = new Locator("TOTAL_PRICE_LABEL", new By.ByXPath("//*[contains(@class,'PriceModule_container')]//*[contains(@class,'price_description')]//parent::*[not(contains(@class,'price_description')) and contains(@class,'align_right') and (normalize-space(text()='Total price') or normalize-space(text()='Total harga'))]"));
    private static final Locator SRP_BANNER = new Locator("SRP_BANNER",new By.ByXPath("//div[contains(@class, 'GradientBanner_variant_big_rectangle')]"));
    private static final Locator SRP_BANNER_PREFERREDPARTNER = new Locator("SRP_BANNER_PREFERREDPARTNER",new By.ByXPath("//b[contains(text(), 'Preferred Partner')]"));
    private static final Locator SRP_BANNER_PREFERREDPARTNER_BS = new Locator("SRP_BANNER_PREFERREDPARTNER_BS",new By.ByXPath("//div[contains(@class, 'BottomSheet_body')]"));
    private static final Locator SRP_BANNER_PREFERREDPARTNER_TITLE = new Locator("SRP_BANNER_PREFERREDPARTNER_TITLE",new By.ByXPath("//*[contains(@class, 'BottomSheet_body_title')]"));
    private static final Locator SRP_BANNER_BENEFITS = new Locator("SRP_BANNER_BENEFITS",new By.ByXPath("//div[contains(@class, 'BenefitsCard_container')]"));
    private static final Locator SRP_BANNER_BENEFITS_BS_CLOSE_BUTTON = new Locator("SRP_BANNER_BENEFITS_BS_CLOSE_BUTTON",new By.ByXPath("//button[contains(@class, 'BottomSheet_close_button')]"));
    private static final Locator LOADING_STATE = new Locator("LOADING_STATE", new By.ByXPath("//*[contains(@class,'ContentLoader')]"));
    private static final Locator MAP_BACK_TO_SRP_BUTTON = new Locator("MAP_BACK_TO_SRP_BUTTON", new By.ByXPath("//*[contains(@class,'SrpMap_back_button')]"));
    private static final Locator DESKTOP_SEARCH_TRIGGER = new Locator("DESKTOP_SEARCH_TRIGGER", By.xpath("//div[@data-testid='input-search-button-desktop']"));
    private static final Locator MODAL_SEARCH_INPUT = new Locator("MODAL_SEARCH_INPUT", By.xpath("//input[@data-testid='destination-search-box']"));
    private static final Locator NO_RESULT_CONTAINER = new Locator("NO_RESULT_CONTAINER", By.xpath("//div[@data-testid='no-search-result']"));
    private static final Locator NO_RESULT_TITLE = new Locator("NO_RESULT_TITLE", By.xpath("//div[@data-testid='no-search-result']//h2"));
    private static final Locator NO_RESULT_DESCRIPTION = new Locator("NO_RESULT_DESCRIPTION", By.xpath("//div[@data-testid='no-search-result']//p"));
    private static final Locator HOTEL_ADS = new Locator("HOTEL_ADS", new By.ByXPath("//span[contains(@class,'HotelAdsLabel')]"));
    public SRPPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> SRPPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new SRPPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new SRPPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final String HOTEL_NAME_ON_CARD_NORMALIZED_XPATH =
            "normalize-space(translate(normalize-space(.), '_', ''))";

    private String normalizeHotelNameForMatch(String hotelName) {
        if (hotelName == null) {
            return "";
        }
        return normalizeNonBreakingSpaceText(hotelName).replace("_", "");
    }

    private String getHotelCardByNamePredicate(String hotelName) {
        String normalizedName = normalizeHotelNameForMatch(hotelName);
        String nameLiteral = toXPathLiteral(normalizedName);
        return ".//h3[contains(@class,'FullProductCard_hotel_name')]//span["
                + HOTEL_NAME_ON_CARD_NORMALIZED_XPATH + "=" + nameLiteral + "]";
    }

    private Locator getHotelCardScopedLocator(String hotelName, String relativeXPath) {
        String xpath = "//a[@data-testid='full-product-card'][" + getHotelCardByNamePredicate(hotelName) + "]" + relativeXPath;
        return new Locator("HOTEL_CARD_SCOPED", new By.ByXPath(xpath));
    }

    private Locator getHotelNameSrp(String hotelName) {
        return getHotelCardScopedLocator(hotelName, "//h3[contains(@class, 'FullProductCard_hotel_name')]//span");
    }

    public void openSRPViaUrl(String accommodationType, int room, int adult, String locationId, String locationType, String location, String checkInDate, String checkOutDate) {
        String baseUrl;
        if (Configs.ENVIRONMENT == Environment.GK) {
            baseUrl = STAGING_WEB_URL;
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            baseUrl = PROD_WEB_URL;
        } else {
            baseUrl = PREPROD_WEB_URL;
        }

        String url = baseUrl +
                accommodationType.toLowerCase() +
                "/search?" +
                "&room=" + room +
                "&adult=" + adult +
                "&id=" + locationId +
                "&type=" + locationType.toUpperCase() +
                "&q=" + location +
                "&checkin=" + checkInDate +
                "&checkout=" + checkOutDate;

        driverTools.getWithDynamicTimeout(url, 60);
        driverTools.waitForElementToDisappear(SRP_LOADING);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        homePageWeb.continueWithoutApp(5);
    }

    public abstract void setPriceTaxFilter(boolean isTotalPrice, boolean isAfterTax) throws InterruptedException;

    public void checkSrpTotalPrice(SearchHotelResponse searchHotelResponse, String currency, String separator, boolean isTax) {
        HotelApi<?> hotelApi = new HotelApi<>(session);
        String srpHotelNameApi = searchHotelResponse.data.contents.get(0).hotelName;
        String hotelPriceSrpApi = thousandSeparator(String.valueOf((int)hotelApi.getSrpHotelTotalPriceWithTax(isTax, searchHotelResponse)), separator);
        String hotelBasePriceSrpApi = thousandSeparator(String.valueOf((int)hotelApi.getSrpHotelCrossedTotalPriceWithTax(isTax, searchHotelResponse)), separator);
        isSRPHotelName();
        Assertion.assertThat("Price in SRP page is same as API price for hotel " + srpHotelNameApi, checkHotelPrice(false, srpHotelNameApi, hotelPriceSrpApi, currency), is(true));
        if(!hotelPriceSrpApi.equals(hotelBasePriceSrpApi)) {
            logger.log("SRP: There is crossed price for this hotel");
            Assertion.assertThat("Crossed price in SRP page is same as API price for hotel " + srpHotelNameApi, checkHotelPrice(true, srpHotelNameApi, hotelBasePriceSrpApi, currency), is(true));
        }
    }

    private boolean checkHotelPrice(boolean isCrossPrice, String hotelName, String price, String currency) {
        price = currency.toUpperCase() + " " + price;
        return (isCrossPrice) ? checkHotelCrossPrice(hotelName, price) : checkHotelFinalPrice(hotelName, price);
    }

    public boolean isHotelTotalPriceDisplayed(String hotelName, String formattedPrice, String currency) {
        return checkHotelPrice(false, hotelName, formattedPrice, currency);
    }

    public boolean isShowPriceIncludingTaxesToggleActive() {
        return driverTools.waitForElementToAppear(SHOW_PRICE_INCLUDING_TAXES_TOGGLE_CHECKBOX, 10)
                .map(WebElement::isSelected)
                .orElse(false);
    }

    public void clickShowPriceIncludingTaxesToggle() {
        driverTools.waitForElementToAppear(HOTEL_NAME_SRP);
        driverTools.scrollUntilElementFound(SHOW_PRICE_INCLUDING_TAXES_TOGGLE_LABEL);
        driverTools.clickWithJavascriptExecutor(SHOW_PRICE_INCLUDING_TAXES_TOGGLE_LABEL);
        driverTools.waitForElementToAppear(AFTER_TAX_TOGGLE_DISABLED).orElse(null);
        driverTools.waitForElementToDisappear(AFTER_TAX_TOGGLE_DISABLED);
        driverTools.waitForElementToDisappear(SRP_LOADING);
        driverTools.waitForElementToAppear(HOTEL_NAME_SRP);
    }

    public void clickShowPriceIncludingTaxesFilter() throws InterruptedException {
        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            openFilterMenu();
        }
        clickShowPriceIncludingTaxesToggle();
        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            saveFilters();
        }
        waitForSrpToLoad();
    }

    public boolean isAbtWordingVisibleOnHotelCard() {
        String priceRemark = getPriceRemarkText();
        return anyOf(
                containsStringIgnoringCase("after taxes"),
                containsStringIgnoringCase("setelah pajak")
        ).matches(priceRemark);
    }

    public void checkABTtoggle() {
        driverTools.waitForElementToAppear(HOTEL_NAME_SRP);
        if (isAbtWordingVisibleOnHotelCard()) {
            logger.log("ABT wording (after taxes / setelah pajak) already visible on hotel card");
            return;
        }

        logger.log("ABT wording not visible — clicking 'Show price including taxes' toggle");
        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            openFilterMenu();
        }
        driverTools.scrollUntilElementFound(SHOW_PRICE_INCLUDING_TAXES_TOGGLE);
        driverTools.clickWithJavascriptExecutor(SHOW_PRICE_INCLUDING_TAXES_TOGGLE);

        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            saveFilters();
        }
        waitForSrpToLoad();
        driverTools.waitForElementToAppear(HOTEL_NAME_SRP);

        String priceRemark = getPriceRemarkText();
        Assertion.assertThat(
                "Hotel card should show ABT wording (after taxes / setelah pajak) after toggling",
                priceRemark,
                anyOf(containsStringIgnoringCase("after taxes"), containsStringIgnoringCase("setelah pajak")));
    }

    private Locator getHotelPriceSectionLocator(String hotelName) {
        return getHotelCardScopedLocator(hotelName, "//div[contains(@class,'PriceArea')]");
    }

    private Locator getHotelFinalPriceByNameLocator(String hotelName) {
        return getHotelCardScopedLocator(hotelName, "//div[contains(@class,'PriceArea_final_price_wrapper')]//div");
    }

    private static final Locator FIRST_HOTEL_NAME_ON_SRP = new Locator("FIRST_HOTEL_NAME_ON_SRP",
            new By.ByXPath("(//a[@data-testid='full-product-card']//h3[contains(@class,'FullProductCard_hotel_name')]//span)[1]"));

    private boolean isFirstHotelCard(String hotelName) {
        return driverTools.waitForElementToAppear(FIRST_HOTEL_NAME_ON_SRP, 10)
                .map(element -> normalizeHotelNameForMatch(hotelName)
                        .equals(normalizeHotelNameForMatch(driverTools.getText(element))))
                .orElse(false);
    }

    private boolean hasAbtPriceRemark(String priceRemark) {
        String normalized = priceRemark.toLowerCase(Locale.ROOT);
        return normalized.contains("after taxes") || normalized.contains("setelah pajak");
    }

    private String readPriceRemarkTextByHotelName(String hotelName) {
        driverTools.waitForElementToAppear(getHotelPriceSectionLocator(hotelName));
        driverTools.scrollIntoWebView(getHotelPriceSectionLocator(hotelName));
        return normalizeNonBreakingSpaceText(driverTools.getText(getHotelPriceSectionLocator(hotelName)).orElseThrow());
    }

    public String getPriceRemarkTextByHotelName(String hotelName) {
        return getPriceRemarkTextByHotelName(hotelName, false);
    }

    public String getPriceRemarkTextByHotelName(String hotelName, boolean ensureAbtOnFirstCard) {
        String priceRemark = readPriceRemarkTextByHotelName(hotelName);
        if (ensureAbtOnFirstCard && isFirstHotelCard(hotelName) && !hasAbtPriceRemark(priceRemark)) {
            clickShowPriceIncludingTaxesToggle();
            priceRemark = readPriceRemarkTextByHotelName(hotelName);
        }
        return priceRemark;
    }

    public String getPriceRemarkText() {
        WebElement priceSection = driverTools.waitForElementToAppear(FIRST_CARD_PRICE_SECTION, 10).orElseThrow();
        return normalizeNonBreakingSpaceText(driverTools.getText(priceSection));
    }

    public long getHotelFinalPriceAmountByName(String hotelName) {
        WebElement priceElement = driverTools.waitForElementToAppear(getHotelFinalPriceByNameLocator(hotelName), 10).orElseThrow();
        driverTools.scrollIntoWebView(priceElement);
        String priceText = normalizeNonBreakingSpaceText(driverTools.getText(priceElement));
        return CoreFunctions.getIntFromString(priceText.replaceAll("[^0-9]", "")).orElse(0);
    }

    private Locator getHotelCrossPrice(String hotelName, String price) {
        return getHotelCardScopedLocator(hotelName, "//*[contains(@class,'slash_price')]//*[text()='" + price + "']");
    }

    private boolean checkHotelCrossPrice(String hotelName, String price) {
         return driverTools.isElementDisplayed(getHotelCrossPrice(hotelName, price));
    }

    private Locator getHotelFinalPrice(String hotelName, String price) {
        return getHotelCardScopedLocator(hotelName, "//*[contains(@class,'final_price')]//*[text()='" + price + "']");
    }

    private boolean checkHotelFinalPrice(String hotelName, String price) {
         return driverTools.isElementDisplayed(getHotelFinalPrice(hotelName, price));
    }

    public boolean isLabelTotalPrice() {
        return driverTools.waitForElementToPresence(TOTAL_PRICE_LABEL).isPresent();
    }

    public abstract void applyQuickFilters();

    public abstract boolean isQuickFilterApplied();

    public abstract void applyAdvancedFilters();

    public abstract boolean isAdvancedFilterApplied();
    public abstract String getLocationHeaderLabel();
    public abstract String getDateHeaderLabel();
    public abstract String getGuestHeaderLabel();

    public void applyPopularPlaceFilter() {
        clickAdvancedFilterItem(FIRST_POPULAR_PLACE_ITEM, true);
        driverTools.click(ADVANCED_FILTER_SAVE);
        driverTools.waitForElementToDisappear(SRP_LOADING);
    }

    public void openAdvancedFilterMenu() {
        driverTools.click(ADVANCED_FILTER_CHIP);
    }

    public abstract void clickChangeSearchCriteria();

    public abstract void clickHotelLocationSearchField();

    protected void clickAdvancedFilterItem(Locator locator, Boolean withJSExecutor) {
        driverTools.waitForElementToPresence(locator).orElseThrow();
        driverTools.scrollIntoWebView(locator);
        if (withJSExecutor) {
            driverTools.clickWithJavascriptExecutor(locator);
        } else {
            driverTools.click(locator);
        }
    }

    public void applyPopularFilters(){
        driverTools.waitForElementToPresence(POPULAR_FILTER_CHIP);
        driverTools.clickWithJavascriptExecutor(POPULAR_FILTER_CHIP);
        driverTools.clickWithJavascriptExecutor(POPULAR_FILTER_REVIEW_CHIP_ITEM);

        driverTools.clickWithJavascriptExecutor(POPULAR_FILTER_STAR_RATING_CHIP);
        driverTools.click(POPULAR_FILTER_SAVE);
    }

    public boolean isPopularFilterApplied(){
        return driverTools.isElementDisplayed(POPULAR_FILTER_STAR_RATING_ACTIVE) & driverTools.isElementDisplayed(POPULAR_FILTER_REVIEW_ACTIVE);
    }

    public String getFirstPopularPlaceText() {
        driverTools.waitForElementToPresence(FIRST_POPULAR_PLACE_ITEM).orElseThrow();
        driverTools.scrollIntoWebView(FIRST_POPULAR_PLACE_ITEM);
        WebElement firstPopularDestinationElement = driverTools.waitForElementToPresence(FIRST_POPULAR_PLACE_ITEM).orElseThrow();
        return driverTools.getText(firstPopularDestinationElement);
    }

    protected Locator getHotelCardLocator(int index) {
        String baseLocator = "(//*[contains(@class, 'FullProductCard_container')])[%d]";
        Locator hotelCardLocator = new Locator("HOTEL_CARD", new By.ByXPath(String.format(baseLocator, index)));
        driverTools.waitForElementToAppear(hotelCardLocator).orElseThrow();

        return hotelCardLocator;
    }

    public void clickHotelCard(int index, boolean openSameTab) {
        Locator destinationHotelCard = getHotelCardLocator(index);
        driverTools.scrollIntoWebView(destinationHotelCard);

        if (openSameTab) {
            WebElement element = driver.findElement(destinationHotelCard.by());
            ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('target');", element);
            driverTools.clickWithJavascriptExecutor(destinationHotelCard);
        } else {
            driverTools.clickWithJavascriptExecutor(destinationHotelCard);
            driverTools.switchToNewTab();
        }
    }

    private String normalizeHexColor(String vendorColor) {
        String hex = vendorColor.trim();
        return hex.startsWith("#") ? hex : "#" + hex;
    }

    private Locator getHotelCardByVendorColorLocator(String vendorColor) {
        return new Locator(
                "HOTEL_CARD_BY_VENDOR_COLOR",
                new By.ByXPath(
                        String.format("//div[@data-testid='tiket-vendor-code']/span[text()='%s']", vendorColor)
                )
        );
    }

    public void clickHotelByVendorCodeAndOpenPdp(String vendorColor) {
        Locator hotelCard = getHotelCardByVendorColorLocator(vendorColor);
        driverTools.waitForElementToAppear(hotelCard);
        driverTools.scrollUntilElementFound(hotelCard);
        driverTools.clickWithJavascriptExecutor(hotelCard);
        driverTools.switchToNewTab();
    }

    protected Locator getHotelAddressLocator(int index) {
        String baseLocator = "//span[contains(@class, 'FullProductCard_hotel_address')][%d]";
        return new Locator("HOTEL_ADDRESS", new By.ByXPath(String.format(baseLocator, index)));
    }

    public boolean isHotelLocationExists(String location) {
        for (int i = 1; i < 5; i++) {
            WebElement hotelAddressElement = driverTools.waitForElementToAppear(getHotelAddressLocator(i)).orElseThrow();
            String currentHotelAddress = driverTools.getText(hotelAddressElement);

            if (currentHotelAddress.contains(location)) {
                return true;
            }
        }
        return false;
    }

    private List<WebElement> getHotelCardsToCheck(int maxHotelsToCheck) {
        driverTools.waitForElementToAppear(HOTEL_CARD_LIST, 10).orElseThrow();
        List<WebElement> hotelCards = driverTools.elements(HOTEL_CARD_LIST).orElseThrow();
        int hotelsToCheck = Math.min(maxHotelsToCheck, hotelCards.size());
        return hotelCards.subList(0, hotelsToCheck);
    }

    /**
     * Collects all benefit text rendered on a hotel card, including any items hidden behind the
     * "+N others" chip (hover reveals tooltip {@code ProductBenefits_tooltip_item_list}).
     */
    private String getHotelCardBenefitsText(WebElement hotelCard) {
        StringBuilder collected = new StringBuilder();
        for (WebElement benefitElement : driverTools.elements(HOTEL_CARD_BENEFITS_SCOPED, hotelCard).orElse(Collections.emptyList())) {
            collected.append(driverTools.getText(benefitElement)).append(" | ");
        }
        for (WebElement othersElement : driverTools.elements(HOTEL_CARD_BENEFITS_OTHERS_SCOPED, hotelCard).orElse(Collections.emptyList())) {
            driverTools.scrollElementToCenter(othersElement);
            driverTools.hoverWithJavascriptExecutor(othersElement);
            driverTools.dummyWait(1);
            WebElement tooltipElement = driverTools.waitForElementToAppear(HOTEL_CARD_BENEFITS_TOOLTIP, 3).orElseThrow();
            collected.append(driverTools.getText(tooltipElement)).append(" | ");
        }
        return collected.toString();
    }

    /**
     * Asserts the first {@code maxHotelsToCheck} SRP hotel cards have an address line containing the selected
     * location/area (supports bilingual {@code "English/Bahasa"} filter labels).
     * Iterates per {@code FullProductCard} so benefit/address nodes are not mixed with {@code ProductBenefits_others}.
     */
    public boolean checkHotelLocation(String selectedLocation, int maxHotelsToCheck) {
        String locationEn = selectedLocation.trim();
        String locationId = selectedLocation.trim();
        if (selectedLocation.contains("/")) {
            String[] parts = selectedLocation.split("/", 2);
            locationEn = parts[0].trim();
            locationId = parts[1].trim();
        }
        String locationEnNorm = normalizeNonBreakingSpaceText(locationEn).toLowerCase(Locale.ROOT);
        String locationIdNorm = normalizeNonBreakingSpaceText(locationId).toLowerCase(Locale.ROOT);

        List<WebElement> hotelCards = getHotelCardsToCheck(maxHotelsToCheck);
        int cardIndex = 0;
        for (WebElement hotelCard : hotelCards) {
            cardIndex++;
            WebElement addressElement = driverTools.elements(HOTEL_CARD_ADDRESS_SCOPED, hotelCard)
                    .filter(list -> !list.isEmpty())
                    .map(list -> list.get(0))
                    .orElseThrow();
            String address = normalizeNonBreakingSpaceText(driverTools.getText(addressElement)).toLowerCase(Locale.ROOT);
            logger.log("Hotel card #" + cardIndex + " address: '" + address + "'");
            Assertion.assertThat("Hotel card #" + cardIndex + " address should contain selected location '" + selectedLocation + "'",
                    address, anyOf(containsString(locationEnNorm), containsString(locationIdNorm)));
        }
        Assertion.assertThat("At least one hotel card should be checked for selected location '" + selectedLocation + "'",
                cardIndex > 0, is(true));
        return cardIndex > 0;
    }

    public abstract void clickSearchOnSRP();

    public boolean isMasterTagVisible(){
        driverTools.waitForElementToAppear(MASTER_TAG,5);
        return driverTools.isElementDisplayed(MASTER_TAG);
    }

    public void clickBack() {
        driverTools.back();
    }

    public boolean isEmptySearchResult() {
        return driverTools.isElementDisplayed(EMPTY_RESULT);
    }

    public boolean isSRPStarRating() {
        return driverTools.isElementDisplayed(HOTEL_STAR_RATING_SRP);
    }

    public boolean isSRPFacilities() {
        return driverTools.isElementDisplayed(SRP_FACILITIES);
    }

    public String getSRPFacilities() {
        WebElement element = driverTools.waitForElementToAppear(SRP_FACILITIES).orElseThrow();
        return driverTools.getText(element);
    }

    public boolean isSRPHotelName() {
        return driverTools.waitForElementToPresence(HOTEL_NAME_SRP).isPresent();
    }

    public String getSRPHotelName() {
        return driverTools.waitForElementToAppear(HOTEL_NAME_SRP).orElseThrow().getText();
    }

    /**
     * Returns hotel names from the first {@code maxHotelsToCheck} cards on the SRP list.
     */
    public List<String> getHotelNamesOnSrp(int maxHotelsToCheck) {
        List<WebElement> hotelCards = getHotelCardsToCheck(maxHotelsToCheck);
        List<String> hotelNames = new ArrayList<>();
        int cardIndex = 0;
        for (WebElement hotelCard : hotelCards) {
            cardIndex++;
            WebElement nameElement = driverTools.elements(HOTEL_CARD_NAME_SCOPED, hotelCard)
                    .filter(list -> !list.isEmpty())
                    .map(list -> list.get(0))
                    .orElseThrow();
            String hotelName = normalizeHotelNameForMatch(driverTools.getText(nameElement));
            logger.log("Hotel card #" + cardIndex + " name: '" + hotelName + "'");
            Assertion.assertThat("Hotel name on card #" + cardIndex + " should not be empty",
                    hotelName, not(emptyString()));
            hotelNames.add(hotelName);
        }
        Assertion.assertThat("At least one hotel card should be available on SRP", hotelNames.isEmpty(), is(false));
        return hotelNames;
    }

    public void selectHotelName(String hotelName) {
        driverTools.dummyWait(3);
        Locator hotelNameLocator = getHotelNameSrp(hotelName);
        WebElement hotelNameElement = driverTools.waitForElementToAppear(hotelNameLocator).orElseThrow();
        driverTools.click(hotelNameElement);
    }

    public String selectHotelName() {
        WebElement hotelNameElement = driverTools.waitForElementToAppear(HOTEL_NAME_SRP).orElseThrow();
        String hotelName = driverTools.getText(hotelNameElement);
        driverTools.click(hotelNameElement);
        return hotelName;
    }

    public boolean isSRPHotelPrice(){
        return driverTools.isElementDisplayed(HOTEL_PRICE_SRP);
    }

    public boolean isSRPHotelTixPoint(){
        return driverTools.isElementDisplayed(TIX_POINT_SRP);
    }

    public String getSRPHotelPrice() {
        return driverTools.waitForElementToAppear(HOTEL_PRICE_SRP).orElseThrow().getText();
    }

    public boolean isSRPHotelReview(){
        return driverTools.isElementDisplayed(HOTEL_REVIEWS_SRP);
    }

    public String getSRPHotelReview() {
        return driverTools.waitForElementToAppear(HOTEL_REVIEWS_SRP).orElseThrow().getText();
    }

    /**
     * Verifies that at least one hotel card on the SRP shows a "from your location" / "dari lokasimu"
     * address text, confirming the search result is correctly bound to the user's current location (Near Me).
     *
     * @return true if at least one hotel card's address contains the Near Me text in EN or ID
     */
    public boolean verifyNearMeOnHotelCards() {
        Locator nearMeAddress = new Locator("NEAR_ME_HOTEL_ADDRESS",
                By.xpath("//span[contains(@class,'FullProductCard_hotel_address') and (contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'from your location') or contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'dari lokasimu'))]"));
        return driverTools.waitForElementToPresence(nearMeAddress, 10).isPresent();
    }

    /**
     * Toggles a quick filter on the SRP card-filter panel by its display name (e.g. "Preferred Partner",
     * "Preferred Partner Plus", "Free Breakfast", "Deals", "Loyalty", "100% Refund &amp; Reschedule").
     * Each filter is rendered as {@code FilterToggleComponent_filter_toggle_container} containing an
     * image whose {@code alt} attribute is the filter name. Clicks the associated toggle label.
     *
     * @param filterName exact filter name as shown in the UI / image alt
     */
    public void toggleSrpQuickFilter(String filterName) {
        Locator toggleLabel = getSrpQuickFilterToggleLabel(filterName);
        driverTools.waitForElementToAppear(toggleLabel, 10);
        driverTools.scrollIntoWebView(toggleLabel);
        driverTools.clickWithJavascriptExecutor(toggleLabel);
    }

    /**
     * Verifies the "Showing great accommodations that fit your needs." (or its Bahasa equivalent)
     * filter-applied confirmation banner is visible on SRP after applying quick filters.
     */
    public boolean isFilterAppliedBannerVisible() {
        return driverTools.waitForElementToPresence(getFilterAppliedBanner(), 15).isPresent();
    }

    /**
     * Verifies the Preferred Partner badge / icon is visible on the first hotel card on SRP.
     */
    public boolean isPreferredPartnerIconOnFirstHotelCardVisible() {
        for (int retry = 0; retry < 3; retry++) {
            if (driverTools.waitForElementToPresence(
                    getPreferredPartnerIconOnHotelCardByIndex(1), 10).isPresent()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Asserts every hotel card in the first {@code maxHotelsToCheck} results (or all cards when fewer)
     * displays the Preferred Partner icon and that its tooltip contains {@code expectedTooltipText}.
     */
    public void assertPreferredPartnerIconAndTooltipOnAllHotelCards(int maxHotelsToCheck, String expectedTooltipText) {
        List<WebElement> hotelCards = getHotelCardsToCheck(maxHotelsToCheck);
        int cardIndex = 0;
        for (WebElement hotelCard : hotelCards) {
            cardIndex++;
            driverTools.scrollElementToCenter(hotelCard);
            WebElement iconElement = driverTools.elements(PREFERRED_PARTNER_ICON_SCOPED, hotelCard)
                    .filter(list -> !list.isEmpty())
                    .map(list -> list.get(0))
                    .orElse(null);
            Assertion.assertThat(
                    "Preferred Partner icon should be visible on hotel card #" + cardIndex,
                    iconElement, is(notNullValue()));

            String tooltipText = getPreferredPartnerIconTooltipText(iconElement);
            logger.log("Hotel card #" + cardIndex + " Preferred Partner tooltip: '" + tooltipText + "'");
            Assertion.assertThat(
                    "Preferred Partner icon tooltip on hotel card #" + cardIndex
                            + " should contain '" + expectedTooltipText + "'",
                    tooltipText, containsStringIgnoringCase(expectedTooltipText));
        }
    }

    /**
     * Applies a Preferred Partner quick filter and asserts icon + tooltip on all checked SRP hotel cards.
     */
    public void applyPreferredPartnerQuickFilterAndAssertResults(int maxHotelsToCheck, String filterName) {
        toggleSrpQuickFilter(filterName);
        waitForSrpToLoad();
        assertPreferredPartnerIconAndTooltipOnAllHotelCards(maxHotelsToCheck, filterName);
    }

    /**
     * Remembers the current browser tab before opening PDP in a new tab from SRP.
     */
    public String rememberCurrentWindowHandle() {
        return driver.getWindowHandle();
    }

    /**
     * Closes the active tab (typically PDP) and switches back to the given window handle (typically SRP).
     */
    public void closeCurrentTabAndSwitchToWindow(String windowHandle) {
        driverTools.closeCurrentTab();
        if (windowHandle != null && driverTools.getWindowHandles().contains(windowHandle)) {
            driverTools.switchToWindow(windowHandle);
        }
    }

    private String getPreferredPartnerIconTooltipText(WebElement iconElement) {
        driverTools.scrollElementToCenter(iconElement);
        driverTools.hoverWithJavascriptExecutor(iconElement);
        driverTools.dummyWait(1);
        WebElement tooltipElement = driverTools.waitForElementToAppear(PREFERRED_PARTNER_ICON_TOOLTIP, 5).orElseThrow();
        return normalizeNonBreakingSpaceText(driverTools.getText(tooltipElement));
    }

    /**
     * Opens hotel SRP via deeplink URL, dismisses coachmark popup, waits for load, and asserts hotel cards are visible.
     */
    public void openHotelSrpViaDeeplink(String accommodationType, int room, int adult, String locationId,
            String locationType, String location, String checkInDate, String checkOutDate) {
        openSRPViaUrl(accommodationType, room, adult, locationId, locationType, location, checkInDate, checkOutDate);
        clickPopUp();
        waitForSrpToLoad();
        Assertion.assertThat("Hotel SRP should load", isHotelCardVisible(), is(true));
    }

    /**
     * Applies a Preferred Partner quick filter on SRP, validates icon + tooltip on hotel cards,
     * opens the first hotel in a new PDP tab, and asserts the PDP section + bottom sheet.
     */
    public void verifyPreferredPartnerFilterOnSrpAndPdp(String filterName, int maxHotel) {
        PDPPageWeb<?> pdpPageWeb = PDPPageWeb.getInstance(session);

        logger.log("Apply '" + filterName + "' quick filter on SRP");
        applyPreferredPartnerQuickFilterAndAssertResults(maxHotel, filterName);

        logger.log("Open first hotel card on SRP — PDP opens in a new tab");
        String srpWindowHandle = rememberCurrentWindowHandle();
        clickHotelCard(1, false);

        logger.log("Verify PDP is loaded");
        Assertion.assertThat("Selected PDP is opened", pdpPageWeb.isPdpLoaded(), is(true));

        logger.log("Verify '" + filterName + "' section is visible on PDP");
        pdpPageWeb.assertPreferredPartnerSectionVisibleOnPdp(filterName);

        logger.log("Click '" + filterName + "' section and verify bottom sheet opens on PDP");
        pdpPageWeb.openPreferredPartnerBottomSheetOnPdp(filterName);

        logger.log("Close PDP tab and return to SRP tab");
        closeCurrentTabAndSwitchToWindow(srpWindowHandle);
    }

    private Locator getSrpQuickFilterToggleLabel(String filterName) {
        return new Locator("SRP_QUICK_FILTER_TOGGLE_LABEL",
                new By.ByXPath("//div[contains(@class,'FilterToggleComponent_filter_toggle_container')][.//img[@alt='"+filterName+"']]//label"));
    }

    private Locator getFilterAppliedBanner() {
        String lowerText = "translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')";
        return new Locator("SRP_FILTER_APPLIED_BANNER",
                new By.ByXPath(
                        "//div[contains(@class,'Text_text') and contains(@class,'Text_weight_bold') and ("
                                + "contains(" + lowerText + ", 'great accommodations that fit your needs')"
                                + " or contains(" + lowerText + ", 'showing great accommodations')"
                                + " or contains(" + lowerText + ", 'menampilkan pilihan akomodasi')"
                                + " or contains(" + lowerText + ", 'akomodasi yang cocok untukmu')"
                                + ")]"));
    }

    private Locator getPreferredPartnerIconOnHotelCardByIndex(int index) {
        return new Locator("PREFERRED_PARTNER_ICON_ON_HOTEL_CARD",
                new By.ByXPath("(//a[contains(@class,'ProductCard_container') or contains(@class,'FullProductCard')])["+index+"]//img[@alt='preferred partner icon' or contains(@class,'PreferredPartner_icon')]"));
    }

    public boolean isThereHotelNearby() {
        for (int i = 1; i < 10; i++) {
            Locator loc = getHotelAddressLocator(i);

            boolean isLocatorMissing = driverTools.waitForElementToPresence(loc, 10).isEmpty();

            if (isLocatorMissing && i == 1) {
                logger.log("[Nearby Hotel] No hotel address locator found for the first entry.");
            }

            String[] sep = driverTools.getText(loc).get().split(" ");
            String metric = sep[1];
            float distance = Float.parseFloat(sep[0]);

            logger.log(String.format("[Nearby Hotel] found %.2f %s", distance, metric));

            switch (metric) {
                case "m":
                    return true;
                case "km":
                    if (distance < 50) {
                        return true;
                    }
                default:
                    break;
            }
        }

        return false;
    }

    public void clickFirstMasterTag() {
        driverTools.clickWithJavascriptExecutor(FIRST_MASTER_TAG_ITEM);
    }

    public boolean isFirstMasterTagActive() {
        return driverTools.isElementDisplayed(FIRST_MASTER_TAG_ACTIVE);
    }

    public boolean isRecentlySearchedSectionAppeared() {
        return driverTools.waitForElementToAppear(RECENTLY_SEARCHED_SECTION).isPresent();
    }
    public boolean isRecentlySearchedSectionDisappeared() {
        return driverTools.waitForElementToDisappear(RECENTLY_SEARCHED_SECTION);
    }
    public void clearAllRecentlySearched() {
        driverTools.click(CLEAR_ALL_RECENTLY_SEARCHED_BUTTON);
    }


    public void clickPopUp() {
        PopupObserver.inject(driver, List.of(POPUP_GOT_IT));
    }


    public void setTotalRoom(int room){
        int currentRoom = Integer.parseInt(driverTools.getValueAttribute(CURRENT_ROOM_AMOUNT,"value"));
        logger.log("currentRoom is '" + currentRoom + "'");
        if(room > currentRoom){
            for(int i = 0; i < room - currentRoom; i++){
                driverTools.clickWithJavascriptExecutor(ROOM_INC_BUTTON);
            }
        } else if(room < currentRoom){
            for(int i = 0; i < currentRoom - room; i++){
                driverTools.clickWithJavascriptExecutor(ROOM_DEC_BUTTON);
            }
        }
    }

    public void setTotalGuest(int adult){
        int currentAdult = Integer.parseInt(driverTools.getValueAttribute(CURRENT_ADULT_AMOUNT,"value"));
        logger.log("currentAdult is '" + currentAdult + "'");
        if(adult > currentAdult){
            for(int i = 0; i < adult - currentAdult; i++){
                driverTools.clickWithJavascriptExecutor(ADULT_INC_BUTTON);
            }
        } else if(adult < currentAdult){
            for(int i = 0; i < currentAdult - adult; i++){
                driverTools.clickWithJavascriptExecutor(ADULT_DEC_BUTTON);
            }
        }
    }

    public Locator getSearchFormResultLocator(String location){
        return new Locator("SEARCH_FORM_RESULT", new By.ByXPath("//*[@data-testid='destination-name']//*[text()='"+location+"']"));
    }

    public  Locator getDateElement(String elementName, int date) {
        return new Locator(elementName, AppiumBy.xpath("(//*[not(contains(@class,'disabled')) and contains(@class,'Day_day_number') and text()='"+date+"'])[1]"));
    }

    public  Locator getDateElement(String elementName, int startDate, int endDate) {
        return new Locator(elementName, AppiumBy.xpath("//*[not(contains(@class,'disabled')) and contains(@class,'Day_day_number') and text()='"+startDate+"']//following::*//*[contains(@class,'Day_day_number') and text()='"+endDate+"']"));
    }


    public void changeSearchLocation() {
        clickSearchOnSRP();
        driverTools.click(SEARCH_BAR);
    }

    public void scrollToFlexibleStay() {
        driverTools.waitForElementToAppear(FLEXIBLE_STAY_CARD,5);
        driverTools.scrollToElement(FLEXIBLE_STAY_CARD);
    }

    public boolean isFlexibleStayVisible(){
        return driverTools.isElementDisplayed(FLEXIBLE_STAY_CARD);
    }

    public boolean isFlexibleStayImageVisible(){
        driverTools.waitForElementToAppear(FLEXIBLE_STAY_IMAGE,5);
        return driverTools.isElementDisplayed(FLEXIBLE_STAY_IMAGE);
    }

    public String getFlexibleStayHeading() {
        WebElement webElement = driverTools.waitForElementToAppear(FLEXIBLE_STAY_HEADING).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getFlexibleStayHotelName() {
        WebElement webElement = driverTools.waitForElementToAppear(FLEXIBLE_STAY_HOTEL_NAME).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getFlexibleStayText() {
        WebElement webElement = driverTools.waitForElementToAppear(FLEXIBLE_STAY_TEXT).orElseThrow();
        return driverTools.getText(webElement);
    }

    public void clickMap() {
        driverTools.clickElementByCoordinateDWeb(MAP_WRAPPER);
    }

    /**
     * Opens the map view from hotel SRP (floating map entry on mWeb, overlay / map wrapper on dWeb).
     */
    public void clickSeeMapButton() {
        if (Configs.PLATFORM == Platform.DWEB) {
            if (driverTools.waitForElementToAppear(MAP_ENTRY_BUTTON, 5).isPresent()) {
                driverTools.clickWithJavascriptExecutor(MAP_ENTRY_BUTTON);
            } else {
                clickMap();
            }
        } else {
            Locator mapEntryButton = new Locator("MAP_ENTRY_MWEB", new By.ByXPath(
                    "//span[contains(@class,'MapEntryButton')]/ancestor::button[1] | //div[contains(@class,'main_srp_map_overlay')]//button"));
            driverTools.waitForElementToAppear(mapEntryButton, 10).orElseThrow();
            driverTools.clickWithJavascriptExecutor(mapEntryButton);
        }
        driverTools.waitForElementToDisappear(SRP_LOADING);
        driverTools.waitForElementToAppear(MAP_CHIP_BUTTON, 20);
    }

    /**
     * Taps a visible price marker on the map and returns its label (e.g. {@code IDR 500.000}).
     */
    public String getHotelPriceMapsText() {
        WebElement mapPriceChip = driverTools.waitForElementToAppear(MAP_CHIP_BUTTON, 20).orElseThrow();
        String priceText = normalizeNonBreakingSpaceText(driverTools.getText(mapPriceChip));
        if (priceText == null || priceText.isBlank()) {
            priceText = normalizeNonBreakingSpaceText(driverTools.getValueAttribute(mapPriceChip, "aria-label"));
        }
        driverTools.clickWithJavascriptExecutor(mapPriceChip);
        driverTools.waitForElementToAppear(HOTEL_PRICE_MAP, 15);
        return priceText;
    }

    public String getHotelPriceCard(String priceText, boolean isMaps) {
        if (isMaps) {
            WebElement priceElement = driverTools.waitForElementToAppear(HOTEL_PRICE_MAP, 10).orElseThrow();
            String hotelPriceCardText = normalizeNonBreakingSpaceText(driverTools.getText(priceElement));
            logger.log("Hotel price on map carousel card: " + hotelPriceCardText + " (marker: " + priceText + ")");
            return hotelPriceCardText;
        }
        throw new NotImplementedException("List-view price card lookup is not implemented on web");
    }

    public boolean isHotelNameCardVisibleOnMaps() {
        return isHotelNameMapVisible();
    }

    public boolean isHotelStarCardVisibleOnMaps() {
        return isHotelStarRatingMapVisible();
    }

    /**
     * Opens PDP from the visible map carousel hotel card and returns the selected hotel name.
     */
    public String selectVisibleHotelItem() {
        WebElement hotelNameElement = driverTools.waitForElementToAppear(HOTEL_NAME_MAP, 10)
                .or(() -> driverTools.waitForElementToAppear(MAP_HOTEL_NAME, 5))
                .orElseThrow();
        String hotelName = normalizeNonBreakingSpaceText(driverTools.getText(hotelNameElement));
        driverTools.clickWithJavascriptExecutor(hotelNameElement);
        return hotelName;
    }

    /**
     * Captures map-view tab state on the main window before tapping a hotel card that opens PDP in a new tab.
     *
     * @return {@code Object[2]} — {@code [0]} map view window handle ({@link String}),
     *         {@code [1]} window handle count before PDP click ({@link Integer})
     */
    public Object[] rememberMapViewTabBeforePdpNavigation() {
        return new Object[] {
                driver.getWindowHandle(),
                driverTools.getWindowHandles().size()
        };
    }

    /**
     * After {@link #selectVisibleHotelItem()} on the main tab, PDP opens in a new tab — switch to it for assertions.
     */
    public void switchToPdpTabOpenedFromMap(int windowHandleCountBeforePdpClick) {
        driverTools.waitForNewTabAndSwitchToLatest(windowHandleCountBeforePdpClick);
    }

    /**
     * Closes the PDP tab and switches back to the map-view tab on the main window.
     */
    public void closePdpTabAndReturnToMapView(String mapViewWindowHandle) {
        driverTools.closeCurrentTab();
        if (mapViewWindowHandle != null && driverTools.getWindowHandles().contains(mapViewWindowHandle)) {
            driverTools.switchToWindow(mapViewWindowHandle);
        } else if (!driverTools.getWindowHandles().isEmpty()) {
            driverTools.switchToWindow(driverTools.getWindowHandles().iterator().next());
        }
    }

    /**
     * Exits map view back to the hotel SRP list (list / map entry toggle).
     */
    public void closeMap() {
        if (driverTools.waitForElementToAppear(MAP_BACK_TO_SRP_BUTTON, 5).isPresent()) {
            driverTools.clickWithJavascriptExecutor(MAP_BACK_TO_SRP_BUTTON);
        } else {
            driverTools.back();
        }
    }

    public void clickChip() {
        WebElement mapChipButton = driverTools.waitForElementToAppear(MAP_CHIP_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(mapChipButton);
    }

    public boolean isHotelNameMapVisible() {
        driverTools.waitForElementToAppear(HOTEL_NAME_MAP,5);
        return driverTools.isElementDisplayed(HOTEL_NAME_MAP);
    }

    public boolean isHotelNameVisible() {
        driverTools.waitForElementToAppear(HOTEL_NAME_SRP,5);
        return driverTools.isElementDisplayed(HOTEL_NAME_SRP);
    }

    public boolean isHotelStarRatingMapVisible() {
        driverTools.waitForElementToAppear(HOTEL_STAR_RATING_MAP,5);
        return driverTools.isElementDisplayed(HOTEL_STAR_RATING_MAP);
    }

    public boolean isHotelStarVisible() {
        return driverTools.isElementDisplayed(HOTEL_REVIEWS_SRP);
    }

    public boolean isHotelPriceMapVisible() {
        driverTools.waitForElementToAppear(HOTEL_PRICE_MAP,5);
        return driverTools.isElementDisplayed(HOTEL_PRICE_MAP);
    }

    public boolean isHotelPriceVisible() {
        return driverTools.isElementDisplayed(HOTEL_PRICE_SRP);
    }

    public boolean isNHAStarVisible() {
        return driverTools.isElementDisplayed(SRP_HOME_STAR);
    }

    public void clickSortButton() {
        driverTools.waitForElementToAppear(SORT_BY_BUTTON);
        driverTools.clickWithJavascriptExecutor(SORT_BY_BUTTON);
    }

    public void clickSortByLowest() {
        driverTools.waitForElementToAppear(SORT_BY_LOWEST_BUTTON);
        driverTools.clickWithJavascriptExecutor(SORT_BY_LOWEST_BUTTON);
    }

    public void clickPriceFilter() {
        driverTools.waitForElementToAppear(PRICE_FILTER_BUTTON);
        driverTools.clickWithJavascriptExecutor(PRICE_FILTER_BUTTON);
    }

    public void clickPriceFilterSave() {
        driverTools.waitForElementToAppear(PRICE_FILTER_SAVE);
        driverTools.clickWithJavascriptExecutor(PRICE_FILTER_SAVE);
    }

    public void clickStarFilter() {
        driverTools.waitForElementToAppear(STAR_BUTTON);
        driverTools.clickWithJavascriptExecutor(STAR_BUTTON);
    }

    public void selectStar() {
        driverTools.waitForElementToAppear(STAR_4);
        driverTools.clickWithJavascriptExecutor(STAR_4);
        driverTools.waitForElementToAppear(STAR_5);
        driverTools.clickWithJavascriptExecutor(STAR_5);
    }

    public void clickStarSave() {
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public int checkStars() {
        driverTools.scrollElementToCenter(STAR_RATINGS);
        driverTools.waitForElementToAppear(STAR_RATINGS);
        List<WebElement> stars = driverTools.elements(STAR_RATINGS,null).orElse(Collections.emptyList());
        return stars.size();
    }

    public Boolean checkPriceFilter(String currency) {
        driverTools.waitForElementToAppear(CURRENCY_PRICE_FILTER(currency));
        return driverTools.isElementDisplayed(CURRENCY_PRICE_FILTER(currency));
    }

    public Boolean checkSorting() {
        Locator FIRST_VALUE = new Locator("FIRST_VALUE", new By.ByXPath("(//div[contains(@class,'main_list_item')])[1]//div[contains(@class,'PriceArea_final_price_wrapper')]/div[2]"));
        Locator SECOND_VALUE = new Locator("SECOND_VALUE", new By.ByXPath("(//div[contains(@class,'main_list_item')])[2]//div[contains(@class,'PriceArea_final_price_wrapper')]/div[2]"));

        WebElement webElementFirst = driverTools.waitForElementToAppear(FIRST_VALUE).orElseThrow();
        String firstText = driverTools.getText(webElementFirst);
        int firstNum = (int) CoreFunctions.convertPriceSafely(firstText);
        driverTools.scrollIntoWebView(SECOND_VALUE);
        WebElement webElementSecond = driverTools.waitForElementToAppear(SECOND_VALUE).orElseThrow();
        String secondText = driverTools.getText(webElementSecond);
        int secondNum = (int) CoreFunctions.convertPriceSafely(secondText);
        return firstNum <= secondNum;
    }

    public boolean checkLocation(String loc){
        WebElement Location = driverTools.waitForElementToAppear(LOC).orElseThrow();
        String srpLocation=driverTools.getText(Location);
        return srpLocation.equalsIgnoreCase(loc);
    }

    public void changeLocationToNearMe(){
        driverTools.waitForElementToAppear(CHANGE_BUTTON).orElseThrow();
        driverTools.click(CHANGE_BUTTON);
        driverTools.waitForElementToAppear(NEAR_ME).orElseThrow();
        driverTools.click(NEAR_ME);
        driverTools.waitForElementToAppear(LETS_SEARCH_BUTTON).orElseThrow();
        driverTools.click(LETS_SEARCH_BUTTON);
    }

    public abstract void changeLocation(String loc);

    public void clickOnFlexibleStayCard(){
        driverTools.clickWithJavascriptExecutor(FLEXIBLE_STAY_HOTEL_NAME);
    }

    public void selectHotel() {
        driverTools.clickWithJavascriptExecutor(HOTEL_TAG);
    }

    public abstract void clickRoomAndGuestButton();

    public abstract boolean isMapVisible();

    public abstract boolean isHotelCardVisible();

    public abstract boolean isHotelImageVisible();

    public abstract boolean isHotelBenefitsVisible();

    public abstract void selectHotelCard();

    public abstract String getLocationNameFromInput();

    public abstract String getHotelName();

    public abstract String getHotelPrice();

    public abstract String[] getHotelLocation();

    public abstract String getRoomAndGuestInfoFromInput();

    public abstract void updateStayLocation(String location);

    public abstract void setStayDate(String type, String date);

    public abstract void clickDone();

    public abstract void openPopularFilter();

    public double[] checkStarFilter(int numberOfHotels) {
        driverTools.waitForElementToAppear(HOTEL_CARD, 10);
        List<WebElement> hotelCards = driverTools.elements(HOTEL_CARD, null).orElse(Collections.emptyList());
        int limit = Math.min(numberOfHotels, hotelCards.size());
        double[] starRatings = new double[limit];

        driverTools.waitForElementToAppear(FULL_STARS, 5);
        driverTools.waitForElementToAppear(HALF_STARS, 5);
        for (int i = 0; i < limit; i++) {
            WebElement hotelCard = hotelCards.get(i);
            List<WebElement> fullStars = driverTools.elements(FULL_STARS, hotelCard).orElse(Collections.emptyList());
            List<WebElement> halfStars =  driverTools.elements(HALF_STARS, hotelCard).orElse(Collections.emptyList());

            double totalStars = fullStars.size() + (0.5 * halfStars.size());
            starRatings[i] = totalStars;
        }

        return starRatings;
    }

    public List<String> checkPriceFilter() {
        driverTools.waitForElementToAppear(HOTEL_CARD, 40);
        List<WebElement> hotelCards = driver.findElements(HOTEL_CARD.by());
        int limit = Math.min(4, hotelCards.size());

        String currency = driverTools.getText(driver.findElement(PRICE_FILTER_BUTTON.by())).replaceAll("[^A-Z]", "");

        Map<String, int[]> ranges = Map.of(
                "IDR", new int[]{500_000, 1_000_000},
                "SGD", new int[]{50, 100},
                "USD", new int[]{50, 100},
                "AUD", new int[]{70, 120}
        );
        int[] range = ranges.getOrDefault(currency, new int[]{0, Integer.MAX_VALUE});

        List<String> results = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            String priceText = driverTools.getText(hotelCards.get(i).findElement(HOTEL_PRICE.by()));
            Integer price = Integer.parseInt(priceText.replaceAll("[^0-9]", ""));
            if (price==null) continue;
            boolean valid = price >= range[0] && price <= range[1];
            results.add("Price " + price + " → " + valid);
        }
        return results;
    }

    public void selectPriceFilter(){
        driverTools.waitForElementToAppear(PRICE_BUTTON);
        driverTools.clickWithJavascriptExecutor(PRICE_BUTTON);
    }


    public boolean isLoginBannerDisplayed() {
        return driverTools.waitForElementToPresence(LOGIN_BANNER, 15).isPresent()
                && driverTools.waitForElementToPresence(LOGIN_BUTTON, 15).isPresent();
    }

    public boolean isLoginButtonClickable() {
        return driverTools.waitForElementToPresence(LOGIN_BUTTON, 15)
                .map(button -> driverTools.isElementDisplayed(button) && driverTools.isElementEnabled(button))
                .orElse(false);
    }

    public void clickLoginBanner() {
        clickLogInButton();
    }

    public void clickLogInButton() {
        driverTools.waitForElementToAppear(LOGIN_BUTTON);
        driverTools.clickWithJavascriptExecutor(LOGIN_BUTTON);
    }

    public Boolean logInSuccessfullOnSRP() {
        return driverTools.waitForElementToAppear(LOGIN_BUTTON).isEmpty();
    }

    /**
     * Applies a sort quick-filter on SRP (e.g. {@code highest price}, {@code lowest price}).
     */
    public void selectFilterSortBy(String filterType) {
        String normalized = filterType == null ? "" : filterType.trim().toLowerCase(Locale.ROOT);
        String sortTypeEn;
        String sortTypeId;
        switch (normalized) {
            case "lowest price" -> {
                sortTypeEn = "Lowest price";
                sortTypeId = "Harga terendah";
            }
            case "highest price" -> {
                sortTypeEn = "Highest price";
                sortTypeId = "Harga tertinggi";
            }
            default -> throw new NotImplementedException("not implemented sort type: " + filterType);
        }
        Locator sortOption = new Locator("SORT_OPTION_" + sortTypeEn, new By.ByXPath(
                "//*[contains(@class,'SrpSortDesktop_chips_carousel_wrapper')]//span[contains(normalize-space(.),'"+sortTypeEn+"') or contains(normalize-space(.),'"+sortTypeId+"')]"));
        driverTools.waitForElementToPresence(sortOption, 10).orElseThrow();
        driverTools.click(sortOption);
        driverTools.waitForElementToAppear(LOADING_STATE).orElseThrow();
        driverTools.waitForElementToDisappear(LOADING_STATE);
    }

    public void scrollToFindSrpBanner() {
        if(!(Configs.PLATFORM == Platform.DWEB)){
            driverTools.swipeToElement(SRP_BANNER, Swipe.UP, 5);
        }else {
            driverTools.scrollToElement(SRP_BANNER);
        }
    }

    public boolean isBannerVisible(){

        return  driverTools.isElementDisplayed(SRP_BANNER);
    }

    public void clickPreferredPartnerBanner(){
        driverTools.scrollUp(200);
        driverTools.clickWithJavascriptExecutor(SRP_BANNER_PREFERREDPARTNER);
        driverTools.waitForElementsToAppear(SRP_BANNER_PREFERREDPARTNER_BS);
    }

    public boolean isBottomSheetTitleVisible(){
        return driverTools.isElementDisplayed(SRP_BANNER_PREFERREDPARTNER_TITLE);
    }

    public boolean isBottomBenefitsVisible(){
        driverTools.waitForElementToPresence(SRP_BANNER_BENEFITS,10);
        return driverTools.isElementDisplayed(SRP_BANNER_BENEFITS);
    }

    public boolean isCloseButtonWorks(){
        driverTools.click(SRP_BANNER_BENEFITS_BS_CLOSE_BUTTON);
        return !driverTools.isElementDisplayed(SRP_BANNER_PREFERREDPARTNER_BS);
    }

    public void openFilterMenu(){
        driverTools.waitForElementToPresence(FILTER_MENU_BUTTON);
        driverTools.clickWithJavascriptExecutor(FILTER_MENU_BUTTON);
    }

    public void saveFilters(){
        driverTools.dummyWait(3);
        driverTools.clickWithJavascriptExecutor(SAVE_FILTERS);
    }

    protected Locator getFilterToggleLocator(String filterName) {
        String[] parts = filterName.split("/", 2);
        String en = parts[0].trim();
        String id = parts.length > 1 ? parts[1].trim() : en;
        String xpath = "//*[contains(text(),'"+en+"') or contains(text(),'"+id+"')]/parent::*[contains(@class,'FilterToggleComponent_filter_icon_name')]/following-sibling::*[contains(@class,'FilterToggleComponent_toggle')]/*/*[contains(@class,'toggle_small')]";
        return new Locator("FILTER_TOGGLE", new By.ByXPath(xpath));
    }

    /**
     * Generic locator for a filter checkbox/option inside the filter modal.
     * Supports labels in "English/Bahasa" format. Matches any label whose visible text
     * equals either the English or Bahasa value, then targets the related checkbox input.
     */
    protected Locator getFilterCheckboxLocator(String filterOption) {
        String[] parts = filterOption.split("/", 2);
        String en = parts[0].trim();
        String id = parts.length > 1 ? parts[1].trim() : en;
        String xpath = "//*[contains(text(),'"+en+"') or contains(text(),'"+id+"')]/following-sibling::*/*[contains(@class,'Check_checkbox')]";
        return new Locator("FILTER_CHECKBOX", new By.ByXPath(xpath));
    }

    /**
     * Generic locator for a filter section header (e.g. "Popular Places/Tempat Populer",
     * "Loyalty Tier/Tier Loyalitas") inside the filter modal.
     */
    protected Locator getFilterSectionLocator(String filterName) {
        String[] parts = filterName.split("/", 2);
        String en = parts[0].trim();
        String id = parts.length > 1 ? parts[1].trim() : en;
        String xpath = "//*[contains(@class,'CardFilter_title_wrapper')]/*[contains(text(),'"+en+"') or contains(text(),'"+id+"')]";
        return new Locator("FILTER_SECTION", new By.ByXPath(xpath));
    }

    /**
     * "Show N more" control inside an accordion filter card (e.g. Area locations).
     * Anchors on {@code span.Accordion_text_truncate_accessibility} title within {@code CardFilter_wrapper}.
     *
     * @param filterSectionTitle bilingual "EN/Bahasa" or single label (case-insensitive substring match on normalized title text)
     */
    protected Locator getFilterSectionShowMoreButtonLocator(String filterSectionTitle) {
        String[] parts = filterSectionTitle.split("/", 2);
        String en = toXPathLiteral(parts[0].trim().toLowerCase(Locale.ROOT));
        String id = toXPathLiteral((parts.length > 1 ? parts[1].trim() : parts[0].trim()).toLowerCase(Locale.ROOT));
        String xpath = "//div[contains(@class,'CardFilter_wrapper')]"
                + "[.//span[contains(@class,'Accordion_text_truncate_accessibility')]"
                + "[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," + en + ")"
                + " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," + id + ")]]"
                + "//button[contains(@class,'show_more_btn')]";
        return new Locator("FILTER_SECTION_SHOW_MORE", new By.ByXPath(xpath));
    }

    /**
     * Locator for the Reset button inside the filter modal/bottom-sheet.
     */
    protected static final Locator FILTER_RESET_BUTTON = new Locator("FILTER_RESET_BUTTON", new By.ByXPath("//button[normalize-space(text())='Reset' or normalize-space(text())='Atur Ulang']"));

    /**
     * Click the filter menu button (alias for openFilterMenu) for parity with mobile tests.
     */
    public void clickFilterButton() {
        openFilterMenu();
    }

    /**
     * Submit / close the filter modal (alias for saveFilters) for parity with mobile tests.
     */
    public void clickFilterSubmit() {
        saveFilters();
        driverTools.waitForElementToDisappear(SRP_LOADING);
    }

    public void selectFilterToggle(String filterName) {
        Locator locator = getFilterToggleLocator(filterName);
        driverTools.waitForElementToPresence(locator).orElseThrow();
        driverTools.scrollIntoWebView(locator);
        driverTools.clickWithJavascriptExecutor(locator);
        logger.log("Filter toggle selected: " + filterName);
        driverTools.waitForElementToAppear(LOADING_STATE).orElseThrow();
        driverTools.waitForElementToDisappear(LOADING_STATE);
    }

    public void selectFilterCheckbox(String filterOption) {
        Locator locator = getFilterCheckboxLocator(filterOption);
        driverTools.waitForElementToPresence(locator).orElseThrow();
        driverTools.scrollIntoWebView(locator);
        driverTools.clickWithJavascriptExecutor(locator);
        logger.log("Filter checkbox selected: " + filterOption);
        driverTools.waitForElementToAppear(LOADING_STATE).orElseThrow();
        driverTools.waitForElementToDisappear(LOADING_STATE);
    }

    public void selectFilterCheckbox(String filterOption, String filterSection) {
        Locator locator = getFilterCheckboxLocator(filterOption);
        if (driverTools.waitForElementToPresence(locator).isEmpty()) {
            driverTools.scrollIntoWebView(getFilterSectionShowMoreButtonLocator(filterSection));
            driverTools.clickWithJavascriptExecutor(getFilterSectionShowMoreButtonLocator(filterSection));
        }
        driverTools.waitForElementToPresence(locator).orElseThrow();
        driverTools.scrollIntoWebView(locator);
        driverTools.clickWithJavascriptExecutor(locator);
        logger.log("Filter checkbox selected: " + filterOption);
        driverTools.waitForElementToAppear(LOADING_STATE).orElseThrow();
        driverTools.waitForElementToDisappear(LOADING_STATE);
    }

    public boolean isGreenDotVisible(String filterName) {
        return driverTools.waitForElementToAppear(getGreenDotLocator(filterName), 3).isPresent();
    }

    private Locator getGreenDotLocator(String filterName) {
        if (filterName.contains("/")) {
            String[] filterNameParts = filterName.split("/");
            String filterNameEnglish = filterNameParts[0];
            String filterNameBahasa = filterNameParts[1];
            return new Locator("GREEN_DOT", new By.ByXPath("//*[contains(@class,'Accordion_text') and (contains(text(),'"+filterNameEnglish+"') or contains(text(),'"+filterNameBahasa+"'))]//following-sibling::*[contains(@class,'Badge')]/*[contains(@class,'Notif_color_green')]"));
        } else {
            return new Locator("GREEN_DOT", new By.ByXPath("//*[contains(@class,'Accordion_text') and contains(text(),'"+filterName+"')]//following-sibling::*[contains(@class,'Badge')]/*[contains(@class,'Notif_color_green')]"));
        }
    }

    public void isBenefitVisible(String benefitData, int maxHotelsToCheck) {
        String benefitEnglish = benefitData;
        String benefitBahasa = benefitData;
        if (benefitData.contains("/")) {
            String[] parts = benefitData.split("/", 2);
            benefitEnglish = parts[0].trim();
            benefitBahasa = parts[1].trim();
        }

        List<WebElement> hotelCards = getHotelCardsToCheck(maxHotelsToCheck);
        StringBuilder combinedBenefitText = new StringBuilder();
        int cardIndex = 0;
        for (WebElement hotelCard : hotelCards) {
            cardIndex++;
            String cardBenefitText = getHotelCardBenefitsText(hotelCard);
            logger.log("Hotel card #" + cardIndex + " benefits: '" + cardBenefitText + "'");
            combinedBenefitText.append(cardBenefitText).append(" || ");
        }
        String benefitsAcrossCards = normalizeNonBreakingSpaceText(combinedBenefitText.toString()).toLowerCase(Locale.ROOT);
        Assertion.assertThat(
                "Benefit '" + benefitData + "' should be visible on at least one of the first " + maxHotelsToCheck + " hotel cards",
                benefitsAcrossCards,
                anyOf(containsString(benefitEnglish.toLowerCase(Locale.ROOT)),
                        containsString(benefitBahasa.toLowerCase(Locale.ROOT))));
    }

    /**
     * Clicks the location list "Show N more" button under the accordion whose title matches {@code filterSectionTitle}
     * (e.g. {@code "Areas/Area"}, {@code "Area"}, {@code "Wilayah"}).
     */
    public void clickFilterSectionShowMore(String filterSectionTitle) {
        Locator locator = getFilterSectionShowMoreButtonLocator(filterSectionTitle);
        driverTools.waitForElementToPresence(locator).orElseThrow();
        driverTools.scrollIntoWebView(locator);
        driverTools.clickWithJavascriptExecutor(locator);
        logger.log("Filter section show-more clicked: " + filterSectionTitle);
    }

    /**
     * Checks whether the "Show more" control exists for the given accordion section (may be absent when all rows fit).
     */
    public boolean isFilterSectionShowMoreVisible(String filterSectionTitle) {
        Locator locator = getFilterSectionShowMoreButtonLocator(filterSectionTitle);
        return driverTools.waitForElementToAppear(locator, 3).isPresent();
    }

    /**
     * Checks whether a filter section (e.g. "Popular Places/Tempat Populer", "Loyalty Tier/Tier Loyalitas")
     * is currently rendered inside the filter modal. Returns false when the section is absent
     * (used to assert disappearance / conditional visibility).
     */
    public boolean isFilterSectionVisible(String filterName) {
        Locator locator = getFilterSectionLocator(filterName);
        boolean visible = driverTools.waitForElementToAppear(locator, 3).isPresent();
        logger.log("Filter section '" + filterName + "' visible: " + visible);
        return visible;
    }

    public boolean isFilterToggleVisible(String filterName) {
        Locator locator = getFilterToggleLocator(filterName);
        boolean visible = driverTools.waitForElementToAppear(locator, 3).isPresent();
        logger.log("Filter toggle '" + filterName + "' visible: " + visible);
        return visible;
    }

    /**
     * Click the Reset button inside the filter modal to deselect all selected filters.
     */
    public void clickResetFilterButton() {
        driverTools.waitForElementToAppear(FILTER_RESET_BUTTON, 5).orElseThrow();
        driverTools.clickWithJavascriptExecutor(FILTER_RESET_BUTTON);
        logger.log("Reset filter button clicked");
    }

    public void waitForSrpToLoad() {
        driverTools.waitForElementToAppear(LOADING_STATE);
        driverTools.waitForElementToDisappear(LOADING_STATE);
    }

    /**
     * Scrolls the filter modal until the section titled {@code filterName} is visible (e.g. {@code Rating}).
     */
    public void scrollToFilter(String filterName) {
        Locator sectionLocator = getFilterSectionLocator(filterName);
        if (driverTools.waitForElementToAppear(sectionLocator, 3).isEmpty()) {
            sectionLocator = getFilterSectionLocator("Rating dari Tamu/Guests Rating");
        }
        driverTools.scrollIntoWebView(sectionLocator);
    }

    /** Alias for {@link #scrollToFilter(String)} to mirror mobile {@code SearchResultPage#swipeToFilter}. */
    public void swipeToFilter(String filterName) {
        scrollToFilter(filterName);
    }

    private Locator getNthHotelReviewLocator(int cardIndex) {
        return new Locator("HOTEL_REVIEW_BY_INDEX", new By.ByXPath(
                        "(//a[@data-testid='full-product-card'] | //a[@data-testid='small-product-card'])["
                                + cardIndex + "]//*["
                                + "contains(@class,'ReviewRating_review_rating_container') "
                                + "or @data-testid='rating-review'" + "]"));
    }

    public Boolean isNthHotelReviewDisplayed(int cardIndex) {
        if (cardIndex < 1) {
            return false;
        }
        Locator locator = getNthHotelReviewLocator(cardIndex);
        return driverTools.waitForElementToAppear(locator, 5)
                .map(element -> driverTools.isElementDisplayed(element))
                .orElse(false);
    }

    public String getNthHotelReview(int cardIndex) {
        WebElement reviewElement = driverTools.waitForElementToAppear(getNthHotelReviewLocator(cardIndex), 5).orElseThrow();
        return driverTools.getText(reviewElement);
    }

    /**
     * Scans cards {@code 1} through {@code maxCardsToScan} and returns the first visible guest review line, or {@code null}.
     */
    public String findReviewLineAmongFirstCards(int maxCardsToScan) {
        for (int cardIndex = 1; cardIndex <= maxCardsToScan; cardIndex++) {
            if (Boolean.TRUE.equals(isNthHotelReviewDisplayed(cardIndex))) {
                String candidate = normalizeNonBreakingSpaceText(getNthHotelReview(cardIndex));
                if (candidate != null && !candidate.isBlank()) {
                    return candidate;
                }
            }
        }
        return null;
    }

    private Locator getPropertyRatingFilterOptionLocator(String rating, String ratingText) {
        if ("4.5".equals(rating)) {
            return new Locator("PROPERTY_RATING_4_5",
                    new By.ByXPath("//label[contains(normalize-space(.),'4.5+') or contains(normalize-space(.),'4,5+')]"));
        }
        return new Locator("PROPERTY_RATING_" + ratingText,
                new By.ByXPath("//label[contains(normalize-space(.),'" + ratingText + "+')]"));
    }

    /**
     * Selects a guest (property) rating filter inside the filter modal. Use {@code random} to auto-pick 3, 4, or 4.5.
     */
    public String selectPropertyRatingFilter(String rating) {
        if ("random".equals(rating)) {
            rating = getRandomObject(new String[]{"3", "4", "4.5"});
        }
        String ratingText = "4.5".equals(rating) ? "5" : rating;
        Locator optionLocator = getPropertyRatingFilterOptionLocator(rating, ratingText);
        driverTools.waitForElementToPresence(optionLocator).orElseThrow();
        driverTools.scrollIntoWebView(optionLocator);
        driverTools.clickWithJavascriptExecutor(optionLocator);
        driverTools.waitForElementToAppear(LOADING_STATE).orElseThrow();
        driverTools.waitForElementToDisappear(LOADING_STATE);
        return rating;
    }

    /**
     * Returns true when the main Filter chip shows an active (green) badge after applying filters.
     */
    public boolean isFilterActivated(String filterType) {
        if ("filter".equalsIgnoreCase(filterType)) {
            return driverTools.waitForElementToAppear(FILTER_CHIP_BADGE, 5).isPresent();
        }
        return isGreenDotVisible(filterType);
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

    /**
     * Asserts the first {@code totalHotel} SRP cards meet the guest-rating threshold from
     * {@link #selectPropertyRatingFilter(String)} (e.g. {@code 4} means score &gt;= 4).
     */
    public boolean checkHotelCardList(String cardAttribute, int totalHotel, String[] validationData) {
        if ("guest rating".equalsIgnoreCase(cardAttribute) || "property rating".equalsIgnoreCase(cardAttribute)) {
            return checkGuestRatingOnFirstCards(totalHotel, validationData);
        }
        logger.warn("checkHotelCardList is not implemented on web for attribute: " + cardAttribute);
        return false;
    }

    private boolean checkGuestRatingOnFirstCards(int totalHotel, String[] validationData) {
        String thresholdJoined = Arrays.stream(Objects.requireNonNullElse(validationData, new String[0]))
                .filter(Objects::nonNull)
                .map(String::trim)
                .reduce("", String::concat);
        Assertion.assertThat("Guest rating filter threshold param should not be empty", thresholdJoined, not(emptyString()));

        double minExpected = parseDecimalNumber(thresholdJoined);
        List<WebElement> hotelCards = getHotelCardsToCheck(totalHotel);
        int cardIndex = 0;
        for (WebElement hotelCard : hotelCards) {
            cardIndex++;
            WebElement reviewElement = driverTools.elements(HOTEL_CARD_REVIEW_SCOPED, hotelCard)
                    .filter(list -> !list.isEmpty())
                    .map(list -> list.get(0))
                    .orElseThrow();
            String rawLine = normalizeNonBreakingSpaceText(driverTools.getText(reviewElement));
            double actualScore = parseGuestRatingScoreFromCardLine(rawLine);
            logger.log("Hotel card #" + cardIndex + " guest rating line: '" + rawLine + "' (parsed=" + actualScore + ")");
            Assertion.assertThat(
                    "Guest rating on card #" + cardIndex + " (" + actualScore + ") should be >= selected filter (" + minExpected + ")",
                    actualScore,
                    greaterThanOrEqualTo(minExpected));
        }
        return cardIndex > 0;
    }

    public void searchAndVerifyNoResults(List<String> invalidKeywords) {
        driverTools.click(DESKTOP_SEARCH_TRIGGER);
        WebElement searchInput = driverTools.waitForElementToAppear(MODAL_SEARCH_INPUT, 5).orElseThrow();

        for (String invalidKeyword : invalidKeywords) {
            driverTools.clearInput(searchInput);
            driverTools.sendKeys(searchInput, invalidKeyword);
            driverTools.waitForElementToAppear(NO_RESULT_CONTAINER, 5).orElseThrow();

            String actualTitle = driverTools.getText(NO_RESULT_TITLE).orElse("");
            String actualDescription = driverTools.getText(NO_RESULT_DESCRIPTION).orElse("");

            Assertion.assertThat("Verify 'No result' title text for: " + invalidKeyword, actualTitle, anyOf(is("Let's change your keyword!"), is("Ubah kata kunci dulu, yuk!")));
            Assertion.assertThat("Verify 'No result' description text for: " + invalidKeyword, actualDescription, anyOf(containsString("No results match your search"),containsString("Nggak ada hasil untuk pencarianmu.")));
            // Brief pause before the next iteration to ensure UI stabilizes
            driverTools.dummyWait(1);
        }
    }
    public String getTextHotelAds(){
        driverTools.waitForElementToAppear(HOTEL_ADS);
        return driverTools.getText(HOTEL_ADS).orElseThrow();
    }
}