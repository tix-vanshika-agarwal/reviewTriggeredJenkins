package com.tiket.web.page.accommodation.hotels.PDP;

import com.tiket.apiPage.HotelApi;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.room.SelectRoomResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.accommodation.hotels.SRP.SRPPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.core.Is;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.tiket.core.CoreFunctions.normalizeNonBreakingSpaceText;
import static com.tiket.core.CoreFunctions.thousandSeparator;
import static org.hamcrest.Matchers.*;
import java.util.Locale;

public abstract class PDPPageWeb<T extends WebDriver> extends WebPage<T> {

    private static final Locator HOTEL_NAME = new Locator("HOTEL_NAME",new By.ByXPath("//h1[@data-testid='name']"));
    private static final Locator NEXT_AVAILABLE_DATE_NIGHT = new Locator("NEXT_AVAILABLE_DATE_NIGHT",new By.ByXPath("//div[@data-testid='night']"));
    private static final Locator NEXT_AVAILABLE_DATE_DATERANGE = new Locator("NEXT_AVAILABLE_DATE_DATERANGE",new By.ByXPath("//div[@data-testid='date']"));
    private static final Locator NEXT_AVAILABLE_DATE_PRICE = new Locator("NEXT_AVAILABLE_DATE_PRICE",new By.ByXPath("//span[@data-testid='price']"));
    private static final Locator NEXT_AVAILABLE_DATE_TOTAL_PRICE_TEXT = new Locator("NEXT_AVAILABLE_DATE_TOTAL_PRICE_TEXT",new By.ByXPath("//span[@data-testid='price']"));
    private static final Locator FULLY_BOOKED_TITLE = new Locator("FULLY_BOOKED_TITLE",new By.ByXPath("//h4[contains(@class, 'Heading_title')]"));
    private static final Locator BEST_PRICE_RIBBON_PDP = new Locator("BEST_PRICE_RIBBON_PDP",new By.ByXPath("(//div[contains(@class, 'Label_color_green') and contains(@class, 'ribbon') ])[1]"));
    private static final Locator BEST_PRICE_RIBBON_ROOM_LIST = new Locator("BEST_PRICE_RIBBON_ROOM_LIST",new By.ByXPath("(//div[contains(@class, 'Label_color_green') and contains(@class, 'ribbon') ])[2]"));
    private static final Locator NEXT_AVAILABLE_DATE_NO_ROOM_TEXT = new Locator("NEXT_AVAILABLE_DATE_NO_ROOM_TEXT",new By.ByXPath("//div[contains(@class, 'no_room_text')]"));
    private static final String STAGING_URL = "https://gatotkaca.tiket.com/hotel";
    private static final String PREPROD_URL = "https://preprod.tiket.com/hotel";
    private static final String PREPROD_URL_NHA = "https://preprod.tiket.com/homes";
    private static final String PROD_URL = "https://tiket.com/hotel";
    private static final Locator HEADING_PDP = new Locator("HEADING_PDP", new By.ByXPath("//h1[@data-testid='name']"));
    private static final Locator REGION=new Locator("REGION", new By.ByXPath("(//a[@data-testid='breadcrumb-link-item'])[2]"));
    private static final Locator CITY=new Locator("CITY", new By.ByXPath("(//a[@data-testid='breadcrumb-link-item'])[3]"));
    private static final Locator AREA=new Locator("AREA", new By.ByXPath("(//a[@data-testid='breadcrumb-link-item'])[4]"));
    private static final Locator BOOKING_FULL_TEXT=new Locator("BOOKING_FULL_TEXT", new By.ByXPath("//p[contains(@class, 'ActionBanner_title') and contains(@class, 'Text_text') and contains(@class, 'Text_variant_highEmphasis')]"));
    private static final Locator HOTEL_IMAGE_CARD = new Locator("HOTEL_IMAGE_CARD", new By.ByXPath("//*[@data-testid='main-image']"));
    private static final Locator SEE_ROOM = new Locator("SEE_ROOM", new By.ByXPath("//*[@data-testid='button-see-room']"));
    private static final Locator CLOSE_ROOM_DETAIL_BUTTON = new Locator("CLOSE_ROOM_DETAIL_BUTTON", new By.ByXPath("//*[contains(@class,'HeroBanner_close_icon_cnt')]"));
    private static final Locator HIGHLIGHTS = new Locator("HIGHLIGHTS", new By.ByXPath("(//div[contains(@class, 'Container_container')])[6]"));
    private static final Locator REVIEWS = new Locator("REVIEWS", new By.ByXPath("(//div[contains(@class, 'Container_container')])[7]"));
    private static final Locator POPULAR_FACILITIES = new Locator("POPULAR_FACILITIES", new By.ByXPath("(//div[contains(@class, 'Container_container')])[8]"));
    private static final Locator PDP_MAP = new Locator("PDP_MAP", new By.ByXPath("//*[@data-testid='map-link' and contains(@class,'LocationSection_see_maps')]"));
    private static final Locator ACCOMMODATION_POLICIES = new Locator("ACCOMMODATION_POLICIES", new By.ByXPath("(//div[contains(@class, 'Container_container')])[10]"));
    private static final Locator FAQ = new Locator("FAQ", new By.ByXPath("(//div[contains(@class, 'Container_container')])[12]"));
    private static final Locator BOOK_BTN = new Locator("BOOK_BTN", By.xpath("//button[@data-testid='room-card-button-book']"));
    public static final Locator NEXT_AVAILABLE_DATE_LIST_ITEM = new Locator("NEXT_AVAILABLE_DATE_LIST_ITEM",new By.ByXPath("//div[contains(@class, 'ListItem_next_available_date_list_item')]"));
    private static final Locator FIRST_ROOM_CARD_TITLE = new Locator("FIRST_ROOM_CARD_TITLE", new By.ByXPath("//*[contains(@class, 'RoomCard_title_text')][1]"));
    private static final Locator FIRST_ROOM_CARD_PRICE = new Locator("FIRST_ROOM_CARD_PRICE", new By.ByXPath("(//div[contains(@class,'Price') or contains(@class,'Summary')]//span[contains(text(),'IDR')])[1]"));
    private static final Locator FIRST_ROOM_CARD_BED_TYPE = new Locator("FIRST_ROOM_CARD_BED_TYPE", new By.ByXPath("(//*[@data-testid='room-card-container'])[1]//*[@data-testid='new-bedroom-type-text'][1]"));
    private static final Locator ROOM_DETAIL_PRICE = new Locator("ROOM_DETAIL_PRICE", new By.ByXPath("(//*[@data-testid='price-detail-container'])[1]//*[contains(@class, 'PriceModule_final_price')]//*[contains(@class,'Text_variant_price')][1]"));
    private static final Locator ROOM_DETAIL_TITLE = new Locator("ROOM_DETAIL_TITLE", new By.ByXPath("(//*[contains(@class, 'Content_room_description')])[1]//*[contains(@class, 'Text_variant_highEmphasis')][1]"));
    private static final Locator ROOM_DETAIL_BED_TYPE = new Locator("ROOM_DETAIL_BED_TYPE", new By.ByXPath("(//*[contains(@class, 'BedroomTypeContent')])[1]//*[@data-testid='new-bedroom-type-text']"));
    private static final Locator ROOM_DETAIL_BOOK_BUTTON = new Locator("ROOM_DETAIL_BOOK_BUTTON", new By.ByXPath("//button[@data-testid='footer-button-book']"));
    private static final Locator ROOM_DETAIL_PRICEBREAKDOWN_CHEVRON = new Locator("ROOM_DETAIL_PRICEBREAKDOWN_CHEVRON", new By.ByXPath("//*[contains(@data-testid, 'footer-button-view-price-detail')]"));
    private static final Locator ROOM_DETAIL_PRICEBREAKDOWN_BOTTOMSHEET_CONTAINER = new Locator("ROOM_DETAIL_PRICEBREAKDOWN_BOTTOMSHEET_CONTAINER", new By.ByXPath("//div[contains(@class, 'BottomSheet_body')]"));
    private static final Locator ROOM_DETAIL_PRICEBREAKDOWN_TOTALPRICE = new Locator("ROOM_DETAIL_PRICEBREAKDOWN_TOTALPRICE", new By.ByXPath("//span[@data-testid='total-payment-price']"));
    private static final Locator ROOM_DETAIL_PRICEBREAKDOWN_PRICEDETAIL = new Locator("ROOM_DETAIL_PRICEBREAKDOWN_PRICEDETAIL", new By.ByXPath("//div[contains(@class, 'Footer_price_details')]"));
    private static final Locator ROOM_DETAIL_PRICEBREAKDOWN_TAX = new Locator("ROOM_DETAIL_PRICEBREAKDOWN_TAX", new By.ByXPath("//*[contains(text(), 'Pajak') or contains(text(), 'Tax')]"));
    private static final Locator ROOM_DETAIL_PRICEBREAKDOWN_TIKETREWARD = new Locator("ROOM_DETAIL_PRICEBREAKDOWN_TIKETREWARD", new By.ByXPath("//div[contains(@class, 'Footer_tiket_point')]"));
    private static final Locator OPEN_ROOM_GALLERY = new Locator("OPEN_ROOM_GALLERY", new By.ByXPath("//div[contains(@class,'Label_truncation_lines')][normalize-space()='See all photos' or normalize-space()='Lihat semua foto']"));
    private static final Locator IMAGE_DESCRIPTION_COUNTER = new Locator("IMAGE_DESCRIPTION_COUNTER", new By.ByXPath("//div[contains(@class, 'ImageDescription_counter')]"));
    private static final Locator IMAGE_DESCRIPTION_TITLE = new Locator("IMAGE_DESCRIPTION_TITLE", new By.ByXPath("//p[contains(@class, 'ImageDescription_title')]"));
    private static final Locator IMAGE_THUMBNAIL = new Locator("IMAGE_THUMBNAIL", new By.ByXPath("//div[contains(@class, 'ImageThumbnailMobile_image')]"));
    private static final Locator CROSS_PRICE_PDP = new Locator("CROSS_PRICE_PDP", new By.ByXPath("//*[contains(@class,'PriceFooter_price_footer_section')]//*[contains(@class, 'Text_diagonal_strikethrough')]//*[contains(@class,'Text_variant')]"));
    private static final Locator PRICE_PDP = new Locator("PRICE_PDP", new By.ByXPath("((//*[contains(@class,'HotelPriceInfo_hotel_price_details')]//*[contains(@class, 'Text_variant_price') and contains(@class, 'Text_align_right')])[1] | (//*[contains(@class,'main_price_footer')]//*[contains(@class, 'Text_variant_price') and contains(@class, 'Text_align_left')])[last()])[last()]"));
    private static final Locator TOTAL_PRICE_LABEL = new Locator("TOTAL_PRICE_LABEL", new By.ByXPath("//*[contains(@class,'PriceFooter_price_footer_section')]//*[contains(@class,'Text_variant') and (text()='Total price' or text()='Total harga')]"));
    private static final Locator STAR_RATING_PDP = new Locator("STAR_RATING_PDP", new By.ByXPath("//div[@data-testid='property-star-rating-container']"));
    private static final Locator PROPERTY_TYPE_PDP = new Locator("PROPERTY_TYPE_PDP", new By.ByXPath("//span[@data-testid='category']"));
    private static final Locator HOTEL_LOCATION_PDP = new Locator("HOTEL_LOCATION_PDP", new By.ByXPath("//span[@data-testid='location']"));
    private static final Locator HOTEL_REVIEW_PDP = new Locator("HOTEL_REVIEW_PDP", new By.ByXPath("//div[contains(@class, 'rr___ReviewCard-module__review_container')]"));
    private static final Locator HOTEL_FACILITIES_PDP = new Locator("HOTEL_FACILITIES_PDP", new By.ByXPath("//div[@data-testid='pdp-popular-facility-list-container']"));
    private static final Locator REVIEW_SECTION_PDP = new Locator("REVIEW_SECTION_PDP", new By.ByXPath("//span[contains(@class, 'rr___Rating-module__rating_title__')]"));
    private static final Locator HOTEL_PRICE_PDP = new Locator("HOTEL_PRICE_PDP", new By.ByXPath("//div[contains(@class, 'HotelPriceInfo_hotel_price_section_')]"));
    private static final Locator ABOUT_HOTEL_PDP = new Locator("ABOUT_HOTEL_PDP", new By.ByXPath("//div[@data-testid='pdp-about-accommodation-container']"));
    private static final Locator ROOM_FULLY_BOOKED = new Locator("ROOM_FULLY_BOOKED", new By.ByXPath("//p[normalize-space(text())='This place is fully booked. Adjust the dates or other details.'   or normalize-space(text())='Semua kamar penuh. Ubah tanggal atau detail lainnya, yuk!']"));
    private static final Locator CALENDER_POP_UP = new Locator("CALENDER_POP_UP", new By.ByXPath("//button[@data-testid='room-list-search-button-open-mobile-ui']"));
    private static final Locator CHANGE_DATE = new Locator("CHANGE_DATE", new By.ByXPath("//button[contains(@class, 'ActionBanners_action_button')]"));
    private static final Locator DATE_CLICK = new Locator("DATE_CLICK", new By.ByXPath("//button[@data-testid='room-list-search-button-open-mobile-ui']"));
    private static final Locator CALENDER_SAVE = new Locator("CALENDER_SAVE", new By.ByXPath("//button[@data-testid='room-list-search-popup-calendar-button-save']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[@data-testid='room-list-search-button-submit']\n"));
    private static final Locator DATE_FIELD = new Locator("DATE_FIELD", new By.ByXPath("//div[@data-testid='room-list-search-button-open-calendar-popup']"));
    private static final Locator ROOM_RECOMMENDATION_CARD = new Locator("ROOM_RECOMMENDATION_CARD", By.xpath("//div[contains(@class,'RecommendationCard_recommendation_card_container')]"));
    private static final Locator BOOK_ROOM_BUTTON = new Locator("BOOK_ROOM_BUTTON", By.xpath("//button[normalize-space()='Book 1 Room' or normalize-space()='Pesan 1 Kamar']"));
    private static final Locator ROOM_RECOMMENDATION_TOTAL = new Locator("ROOM_RECOMMENDATION_TOTAL", By.xpath("//div[@data-testid='total-room-night-summary']"));
    private static final Locator ROOM_DETAIL_TOTAL_ROOM = new Locator("ROOM_DETAIL_TOTAL_ROOM", By.xpath("//div[contains(@class,'RoomSummary_total_price_summary')]"));

    // Gallery locators
    private static final Locator GALLERY_CONTAINER = new Locator("GALLERY_CONTAINER", new By.ByXPath("//section[contains(@class,'desktop_image_gallery_popup')]//div[contains(@class,'Navigation_navigation_wrapper')]"));
    private static final Locator GALLERY_TITLE = new Locator("GALLERY_TITLE", new By.ByXPath("//h3[contains(@class, 'Text_text') and (text()='Gallery' or text()='Galeri')]"));
    private static final Locator GALLERY_CLOSE_BUTTON = new Locator("GALLERY_CLOSE_BUTTON", new By.ByXPath("//button[contains(@class, 'desktop_image_gallery_close')]"));
    private static final Locator GALLERY_IMAGE_ITEM = new Locator("GALLERY_IMAGE_ITEM", new By.ByXPath("//div[@data-testid='image-gallery-desktop-navigation-image-item-0-0-0']"));

    // Room list filter locators
    private static final Locator ROOM_LIST_FILTER_CHIPS_CONTAINER = new Locator("ROOM_LIST_FILTER_CHIPS_CONTAINER", new By.ByXPath("//div[contains(@class, 'ChipsCarousel_container')]"));
    private static final Locator SECOND_ROOM_LIST_FILTER_CHIP = new Locator("SECOND_ROOM_LIST_FILTER_CHIP", new By.ByXPath("(//button[@data-testid='room-list-filter-chip-item'])[2]"));

    private static Locator getFilterChipByText(String chipText) {
        return new Locator("FILTER_CHIP_BY_TEXT", new By.ByXPath(
            String.format("//button[@data-testid='room-list-filter-chip-item']//span[contains(text(), '%s')]", chipText)));
    }


    private static final Locator ROOM_RECOMMENDATION_CARD_CONTAINER = new Locator("ROOM_RECOMMENDATION_CARD_CONTAINER", new By.ByXPath("//div[contains(@class, 'RecommendationCard_recommendation_card_container')]"));
    private static final Locator RATE_PLAN_CARD_CONTAINER = new Locator("RATE_PLAN_CARD_CONTAINER", new By.ByXPath("//div[contains(@class, 'RatePlanCard_rate_plan_card_item_container')]"));
    private static final Locator ROOM_CARD_ANY = new Locator("ROOM_CARD_ANY", new By.ByXPath("//div[contains(@class, 'RecommendationCard_recommendation_card_container') or contains(@class, 'RatePlanCard_rate_plan_card_item_container')]"));
    private static final Locator ROOM_INNER_CARD_CONTAINER = new Locator("ROOM_INNER_CARD_CONTAINER", new By.ByXPath("//div[@data-testid='room-inner-card-container']"));

    private static final Locator ROOM_NAME_IN_RECOMMENDATION_CARD = new Locator("ROOM_NAME_IN_RECOMMENDATION_CARD", new By.ByXPath("//h4[contains(@class, 'RoomInnerCard_room_name_title')]"));
    private static final Locator ROOM_NAME_IN_FACILITIES_NUDGES = new Locator("ROOM_NAME_IN_FACILITIES_NUDGES", new By.ByXPath("//div[contains(@class, 'FacilitiesNudges_mobile_room_group_expanded_title')]//h3"));
    private static final Locator ROOM_INNER_CARD_CHEVRON = new Locator("ROOM_INNER_CARD_CHEVRON", new By.ByXPath("(//div[contains(@class, 'RatePlanInformation_occupancy_information')])[1]"));
    private static final Locator ROOM_CARD_FEATURE_LABEL = new Locator("ROOM_CARD_FEATURE_LABEL", new By.ByXPath("//div[@data-testid='room-card-feature-label-item']//span[contains(@class, 'FeatureLabels_label_text')]"));

    // Room detail page locators
    private static final Locator ROOM_DETAIL_CONTAINER = new Locator("ROOM_DETAIL_CONTAINER", new By.ByXPath("//div[@data-testid='room-detail-container']"));
    private static final Locator ROOM_DETAIL_NAME = new Locator("ROOM_DETAIL_NAME", new By.ByXPath("//div[contains(@class, 'RoomOverview_room_overview_container')]//span[contains(@class, 'Text_size_h2') and contains(@class, 'Text_weight_bold')]"));
    private static final Locator ROOM_DETAIL_SIZE = new Locator("ROOM_DETAIL_SIZE", new By.ByXPath("//div[contains(@class, 'RoomOverview_room_overview_item')]//span[contains(text(), 'm²')]"));
    private static final Locator ROOM_DETAIL_BED_TYPE_OVERVIEW = new Locator("ROOM_DETAIL_BED_TYPE_OVERVIEW", new By.ByXPath("//div[contains(@class, 'BedTypesContent_bed_types_content_container')]//span[contains(@class, 'Text_text')]"));
    private static final Locator ROOM_DETAIL_CHECKIN_PROCEDURE_CONTAINER = new Locator("ROOM_DETAIL_CHECKIN_PROCEDURE_CONTAINER", new By.ByXPath("//div[@data-testid='check-in-procedure-content-container']"));
    private static final Locator ROOM_DETAIL_CHECKIN_TIME = new Locator("ROOM_DETAIL_CHECKIN_TIME", new By.ByXPath("//div[@data-testid='check-in-procedure-content-container']//div[contains(@class, 'check_in_out_time_item')][1]//h3"));
    private static final Locator ROOM_DETAIL_CHECKOUT_TIME = new Locator("ROOM_DETAIL_CHECKOUT_TIME", new By.ByXPath("//div[@data-testid='check-in-procedure-content-container']//div[contains(@class, 'check_in_out_time_item')][2]//h3"));
    private static final Locator ROOM_INFO_CAPACITY = new Locator("ROOM_INFO_CAPACITY", new By.ByXPath("//*[contains(@class, 'RoomOverview_occupancy_item')]"));
    private static final Locator ROOM_INFO_MEAL = new Locator("ROOM_INFO_MEAL", new By.ByXPath("//*[contains(@class,'RoomOverview_room_overview_container')]//*[@data-testid='room-card-feature-label-item']"));
    private static final Locator ROOM_INFO_BED_TYPE = new Locator("ROOM_INFO_BED_TYPE", new By.ByXPath("//*[contains(@class,'RoomOverview_room_overview_container')]//*[contains(@class,'BedTypesContent_bed_types_main_content_wrapper')]"));
    private static final Locator ROOM_INFO_SMOKING_PREFERENCES = new Locator("ROOM_INFO_SMOKING_PREFERENCES", new By.ByXPath("//*[contains(@class,'RoomOverview_room_overview_item')]//*[contains(@class,'Text')]"));

    // Price detail page locators
    private static final Locator ROOM_DETAIL_FOOTER_PRICE = new Locator("ROOM_DETAIL_FOOTER_PRICE", new By.ByXPath("//div[@data-testid='price-detail-container']//div[contains(@class, 'Text_variant_price')]"));
    private static final Locator ROOM_DETAIL_FOOTER_PRICE_CHEVRON = new Locator("ROOM_DETAIL_FOOTER_PRICE_CHEVRON", new By.ByXPath("//*[@data-testid='footer-button-view-price-detail']"));
    private static final Locator ROOM_DETAIL_FOOTER_BOOK_BUTTON = new Locator("ROOM_DETAIL_FOOTER_BOOK_BUTTON", new By.ByXPath("//button[@data-testid='footer-button-book']"));
    private static final Locator PRICE_DETAILS_CONTAINER = new Locator("PRICE_DETAILS_CONTAINER", new By.ByXPath("//div[@data-testid='price-details-container']"));
    private static final Locator PRICE_DETAILS_TITLE = new Locator("PRICE_DETAILS_TITLE", new By.ByXPath("//h2[contains(@class, 'BottomSheet_body_title') and (text()='Price Details' or text()='Rincian Harga')]"));
    private static final Locator PRICE_DETAILS_SUBTOTAL = new Locator("PRICE_DETAILS_SUBTOTAL", new By.ByXPath("//div[@data-testid='price-summary-container']//span[text()='Subtotal']/ancestor::div[contains(@class, 'PriceSummary_accordion_header')]//following-sibling::div//span[contains(@class, 'Text_text')]"));
    private static final Locator PRICE_DETAILS_OTHER_FEES = new Locator("PRICE_DETAILS_OTHER_FEES", new By.ByXPath("//span[text()='Other Fees' or text()='Biaya Lainnya']/ancestor::div[contains(@class, 'PriceSummary_accordion_header')]//following-sibling::div//span[contains(@class, 'Text_text')]"));
    private static final Locator PRICE_DETAILS_DISCOUNT = new Locator("PRICE_DETAILS_DISCOUNT", new By.ByXPath("//span[text()='Discount' or text()='Diskon']/ancestor::div[contains(@class, 'PriceSummary_accordion_header')]//following-sibling::div//span[contains(@class, 'Text_variant_positive')]"));
    private static final Locator PRICE_DETAILS_TOTAL = new Locator("PRICE_DETAILS_TOTAL", new By.ByXPath("//div[contains(@class, 'PriceSummary_total_price_container')]//span[contains(@class, 'Text_weight_bold')][2]"));
    private static final Locator PRICE_DETAILS_BOOK_BUTTON = new Locator("PRICE_DETAILS_BOOK_BUTTON", new By.ByXPath("//footer[contains(@class, 'BottomSheet_sticky_footer')]//button[contains(@class, 'Button_variant_primary')]"));
    
    // Price breakdown value locators - dynamic methods below
    private static final Locator PRICE_DETAILS_DISCOUNT_LABEL = new Locator("PRICE_DETAILS_DISCOUNT_LABEL", new By.ByXPath("//span[text()='Discount' or text()='Diskon']"));

    // Dynamic locator methods for price breakdown (currency-dependent)
    private Locator getPriceDetailsSubtotalLocator(String currency) {
        return new Locator("PRICE_DETAILS_SUBTOTAL_VALUE", new By.ByXPath("//div[@data-testid='price-summary-container']//span[text()='Subtotal']/ancestor::div[contains(@class, 'accordion_header')]//span[contains(text(), '" + currency + "')]"));
    }

    private Locator getPriceDetailsOtherFeesLocator(String currency) {
        return new Locator("PRICE_DETAILS_OTHER_FEES_VALUE", new By.ByXPath("//span[text()='Other Fees' or text()='Biaya Lainnya']/ancestor::div[contains(@class, 'accordion_item')]//span[contains(text(), '" + currency + "')]"));
    }

    private Locator getPriceDetailsDiscountLocator(String currency) {
        return new Locator("PRICE_DETAILS_DISCOUNT_VALUE", new By.ByXPath("//span[text()='Discount' or text()='Diskon']/ancestor::div[contains(@class, 'accordion_header')]//span[contains(@class, 'Text_variant_positive') and contains(text(), '" + currency + "')]"));
    }

    // Booking page locators
    private static final Locator BOOKING_STAY_DETAILS_CHEVRON = new Locator("BOOKING_STAY_DETAILS_CHEVRON", new By.ByXPath("//div[contains(@class, 'GroupFieldBodyComponents_group_field_body_chevron_container')]"));
    private static final Locator STAY_DETAILS_BOTTOM_SHEET_TITLE = new Locator("STAY_DETAILS_BOTTOM_SHEET_TITLE", new By.ByXPath("//h2[contains(@class, 'BottomSheet_body_title') and (text()='Stay Details' or text()='Detail Menginap')]"));
    private static final Locator STAY_DETAILS_HOTEL_NAME = new Locator("STAY_DETAILS_HOTEL_NAME", new By.ByXPath("//h3[@data-testid='reservation-info-hotel-name-text']"));
    private static final Locator STAY_DETAILS_ROOM_NAME = new Locator("STAY_DETAILS_ROOM_NAME", new By.ByXPath("//div[@data-testid='additional-info-container']//p[contains(@class, 'AdditionalInfo_title')]"));
    private static final Locator STAY_DETAILS_ROOM_COUNT = new Locator("STAY_DETAILS_ROOM_COUNT", new By.ByXPath("//div[@data-testid='number-of-room-info-content']//p"));
    private static final Locator STAY_DETAILS_NIGHT_COUNT = new Locator("STAY_DETAILS_NIGHT_COUNT", new By.ByXPath("//div[@data-testid='number-of-night-info-content']//p"));
    private static final Locator STAY_DETAILS_GUEST_COUNT = new Locator("STAY_DETAILS_GUEST_COUNT", new By.ByXPath("//div[@data-testid='number-of-guests-info-content']//p"));
    private static final Locator STAY_DETAILS_BED_TYPE = new Locator("STAY_DETAILS_BED_TYPE", new By.ByXPath("//div[@data-testid='bedroom-content-container']//p[@data-testid='new-bedroom-type-text']"));
    private static final Locator STAY_DETAILS_CANCELLATION_POLICY_CONTAINER = new Locator("STAY_DETAILS_CANCELLATION_POLICY_CONTAINER", new By.ByXPath("//div[@data-testid='cancellation-stay-policies-container']"));
    private static final Locator STAY_DETAILS_CANCELLATION_POLICY_TITLE = new Locator("STAY_DETAILS_CANCELLATION_POLICY_TITLE", new By.ByXPath("//span[@data-testid='cancellation-policy-title-text']"));
    private static final Locator BOTTOM_SHEET_CLOSE_BUTTON = new Locator("BOTTOM_SHEET_CLOSE_BUTTON", new By.ByXPath("//button[contains(@class, 'BottomSheet_close_button')]"));

    // Special Request locators
    private static final Locator SPECIAL_REQUEST_BUTTON = new Locator("SPECIAL_REQUEST_BUTTON", new By.ByXPath("//span[contains(@class, 'FormItem_special_request_text') and (contains(text(), 'Special Request') or contains(text(), 'Permintaan Khusus'))]"));
    private static final Locator SPECIAL_REQUEST_FORM_CONTAINER = new Locator("SPECIAL_REQUEST_FORM_CONTAINER", new By.ByXPath("//div[@data-testid='special-request-chips-container']"));
    private static final Locator SPECIAL_REQUEST_FIRST_CHIP = new Locator("SPECIAL_REQUEST_FIRST_CHIP", new By.ByXPath("(//div[@data-testid='special-request-chips-container']//button)[1]"));
    private static final Locator SPECIAL_REQUEST_SECOND_CHIP = new Locator("SPECIAL_REQUEST_SECOND_CHIP", new By.ByXPath("(//div[@data-testid='special-request-chips-container']//button)[2]"));
    private static final Locator SPECIAL_REQUEST_OTHER_TEXTAREA = new Locator("SPECIAL_REQUEST_OTHER_TEXTAREA", new By.ByXPath("//textarea[@id='add-other-requests']"));
    private static final Locator SPECIAL_REQUEST_SAVE_BUTTON = new Locator("SPECIAL_REQUEST_SAVE_BUTTON", new By.ByXPath("//button[contains(@class, 'Button_variant_primary')]//span[text()='Save' or text()='Simpan']/parent::button"));

    private static final Locator MAPS_PAGE=new Locator("MAPS_PAGE",new By.ByXPath("//div[@style=\"cursor: pointer;\"]"));
    private static final Locator NEARBY_POINTS_ON_MAP = new Locator("NEARBY_POINTS_ON_MAP", new By.ByXPath("//*[contains(@class,'PoiMarkerList_pdp_hotel_marker') or contains(@class,'PoiMarkerList_poi_marker')]"));
    private static final Locator NEARBY_POINTS_EMPTY_STATE = new Locator("NEARBY_POINTS_EMPTY_STATE", new By.ByXPath("//*[contains(normalize-space(.),'Looks like there are no interesting places around this accommodation') or contains(normalize-space(.),'Sepertinya tidak ada tempat menarik di sekitar akomodasi ini') or contains(normalize-space(.),'tidak ada tempat menarik di sekitar')]"));
    private static final Locator REVIEW_TAB_BUTTON = new Locator("REVIEW_TAB_BUTTON", new By.ByXPath("//button[contains(@class, 'Tabs_tab')]//span[text()='Reviews' or text()='Ulasan']"));
    private static final Locator REVIEW_SECTION_HEADING = new Locator("REVIEW_SECTION_HEADING", new By.ByXPath("//div[contains(@class, 'ReviewWidget-module__heading')]//h2[text()='Reviews' or text()='Ulasan']"));
    private static final Locator REVIEW_CARDS_CONTAINER = new Locator("REVIEW_CARDS_CONTAINER", new By.ByXPath("//div[contains(@class, 'ReviewWidget-module__review_cards')]"));
    private static final Locator REVIEW_CARD = new Locator("REVIEW_CARD", new By.ByXPath("//div[@data-testid='review-card']"));
    private static final Locator FIRST_REVIEW_CARD = new Locator("FIRST_REVIEW_CARD", new By.ByXPath("(//div[@data-testid='review-card'])[1]"));
    private static final Locator REVIEW_LIKE_ICON = new Locator("REVIEW_LIKE_ICON", new By.ByXPath("//*[@data-testid='like-review']"));
    private static final Locator REVIEW_LOGIN_PROMPT_TITLE = new Locator("REVIEW_LOGIN_PROMPT_TITLE", new By.ByXPath("//h3[contains(@class, 'PopupLogin-module__popup_title')]"));
    private static final Locator REVIEW_LOGIN_CANCEL_BUTTON = new Locator("REVIEW_LOGIN_CANCEL_BUTTON", new By.ByXPath("//button[contains(@class, 'Button-module__variant_secondary')]"));
    private static final Locator REVIEW_POPUP_CLOSE_BUTTON = new Locator("REVIEW_POPUP_CLOSE_BUTTON", new By.ByXPath("//button[contains(@class, 'BottomSheet-module__close_button')]"));
    private static final Locator REVIEW_SEE_ALL_BUTTON = new Locator("REVIEW_SEE_ALL_BUTTON", new By.ByXPath("//span[@data-testid='see-all']"));
    private static final Locator REVIEW_PAGE_TITLE = new Locator("REVIEW_PAGE_TITLE", new By.ByXPath("//span[contains(@class, 'Text_size_b1') and contains(@class, 'Text_weight_bold')]"));
    private static final Locator REVIEW_PAGE_CARD_LIST = new Locator("REVIEW_PAGE_CARD_LIST", new By.ByXPath("//div[contains(@class, 'ReviewCards_review_card_list')]"));
    private static final Locator REVIEW_PAGE_CARD = new Locator("REVIEW_PAGE_CARD", new By.ByXPath("//div[contains(@class, 'ReviewCard_review_card')][@data-testid='review-card']"));
    private static final Locator REVIEW_PAGE_CARD_DATE = new Locator("REVIEW_PAGE_CARD_DATE", new By.ByXPath("//span[contains(@class, 'ReviewCard_date')]"));
    private static final Locator REVIEW_PAGE_CARD_RATING = new Locator("REVIEW_PAGE_CARD_RATING", new By.ByXPath("//span[contains(@class, 'ReviewCard_user_review')]"));
    private static final Locator REVIEW_PAGE_SORT_BUTTON = new Locator("REVIEW_PAGE_SORT_BUTTON", new By.ByXPath("(//div[contains(@class, 'FilterNavigationBottom_config_icon')]/parent::div[contains(@class, 'ButtonTabs_button_content')])[2]"));
    private static final Locator FIRST_REVIEW_PAGE_CARD_DATE = new Locator("FIRST_REVIEW_PAGE_CARD_DATE", new By.ByXPath("(//span[contains(@class, 'ReviewCard_date')])[1]"));
    private static final Locator FIRST_REVIEW_PAGE_CARD_RATING = new Locator("FIRST_REVIEW_PAGE_CARD_RATING", new By.ByXPath("(//span[contains(@class, 'ReviewCard_user_review')])[1]"));
    private static final Locator REVIEW_SORT_LATEST = new Locator("REVIEW_SORT_LATEST", new By.ByXPath("//label[contains(@class, 'Radio_radio')]//input[@data-testid='radio-option' and @value='\"0\"']"));
    private static final Locator REVIEW_SORT_MOST_HELPFUL = new Locator("REVIEW_SORT_MOST_HELPFUL", new By.ByXPath("//label[contains(@class, 'Radio_radio')]//input[@data-testid='radio-option' and @value='\"1\"']"));
    private static final Locator REVIEW_SORT_RATING_HIGH_TO_LOW = new Locator("REVIEW_SORT_RATING_HIGH_TO_LOW", new By.ByXPath("//label[contains(@class, 'Radio_radio')]//input[@data-testid='radio-option' and @value='\"2\"']"));
    private static final Locator REVIEW_SORT_RATING_LOW_TO_HIGH = new Locator("REVIEW_SORT_RATING_LOW_TO_HIGH", new By.ByXPath("//label[contains(@class, 'Radio_radio')]//input[@data-testid='radio-option' and @value='\"3\"']"));
    private static final Locator FIRST_REVIEW_PAGE_CARD_HELPFULNESS = new Locator("FIRST_REVIEW_PAGE_CARD_HELPFULNESS", new By.ByXPath("(//div[contains(@class, 'LikeReviewButton_like_review')]//span[contains(@class, 'Text_text')])[1]"));
    private static final Locator ACCOMMODATION_NEARBY_SECTION = new Locator("ACCOMMODATION_NEARBY_SECTION", new By.ByXPath("//button[contains(@class,'Navigation_main_navigation_button')]"));
    private static final Locator ACCOMMODATION_NEARBY_LIST_CONTAINER = new Locator("ACCOMMODATION_NEARBY_LIST_CONTAINER", new By.ByXPath("//*[starts-with(@data-testid,'hotel-marker-')]"));
    private static final Locator NEARBY_CHIP = new Locator("NEARBY_CHIP", new By.ByXPath("//*[contains(@class,'InAppMap_chip_item_wrapper')]/button[contains(@class,'Chip')]"));
    private static final Locator MAP_ADDRESS = new Locator("MAP_ADDRESS", new By.ByXPath("//span[contains(@class,'MapCarouselProductCard_hotel_address')]"));
    private static final Locator PROMO_LIST=new Locator("PROMO_LIST",By.xpath("//div[contains(@class, 'InfinityPromoList_wrapper__')]"));
    private static final Locator PROMO_COPY_BUTTON=new Locator("PROMO_COPY_BUTTON",By.xpath("(//p[contains(@class, 'InfinityPromoCard_code_text__')])[1]"));
    private static final Locator PROMO_SECTION_COPY_BTN=new Locator("PROMO_SECTION_COPY_BTN",By.xpath("(//button[@data-testid='copy-promo-code-button'])[1]"));
    private static final Locator PROMO_SECTION_CODE=new Locator("PROMO_SECTION_CODE",By.xpath("(//*[contains(@class,'InfinityPromoListPopupCard_code_field')][1]//span[normalize-space(.)!='' and not(contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'code')) and not(contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'kode'))])[1]"));
    private static final Locator COPIED_PROMO_CODE=new Locator("COPIED_PROMO_CODE",By.xpath("(//div[contains(@class, 'InfinityPromoCard_success_view')]//p)[1]"));
    private static final Locator SEE_ALL_BUTTON=new Locator("SEE_ALL_BUTTON",By.xpath("//div[contains(@class, 'InfinityPromoList_see_all_icon')]"));
    private static final Locator PROMO_SECTION=new Locator("PROMO_SECTION",By.xpath("//div[contains(@class, 'InfinityPromoList_list_wrapper__')]"));
    private static final Locator PROMO_CODE_COPIED=new Locator("PROMO_CODE_COPIED",By.xpath("//span[contains(@class, 'Ripple_ripple__') and contains(@class, 'Ripple_variant_secondary__')]"));
    private static final Locator CLOSE_BUTTON_PROMO_SECTION = new Locator("CLOSE_BUTTON_PROMO_SECTION",By.xpath("//*[@data-testid='modal-close-button']"));
    private static final Locator USER_AVATAR_BUTTON = new Locator("USER_AVATAR_BUTTON", By.xpath("//button[contains(@class, 'UserModal')]"));
    private static final Locator USER_LOYALTY_TIER_TEXT = new Locator("USER_LOYALTY_TIER_TEXT", By.xpath("//div[contains(@class, 'loyalty_tier')]//span[contains(@class, 'Text-module')]"));
    private static final Locator PREMIUM_TIER_BADGE = new Locator("PREMIUM_TIER_BADGE", By.xpath("//div[contains(@class, 'RatePlanPrice_loyalty_tier_container')]"));
    private static final Locator PREMIUM_DISCOUNT_TEXT_ROW = new Locator("PREMIUM_DISCOUNT_TEXT_ROW", By.xpath("//div[contains(@class, 'RatePlanInformation_rate_plan_additional_info_item')]"));
    private static final Locator SEARCH_BAR_DATE_TAB = new Locator("SEARCH_BAR_DATE_TAB", By.xpath("//div[@data-testid='room-list-search-button-open-calendar-popup']"));
    private static final Locator MODAL_CLOSE_BUTTON = new Locator("MODAL_CLOSE_BUTTON", By.xpath("//button[@data-testid='modal-close-button']"));
    private static final Locator SEARCH_BAR_DATE_TEXT = new Locator("SEARCH_BAR_DATE_TEXT", By.xpath("//div[@data-testid='room-list-search-button-open-calendar-popup']//span[contains(@class, 'SearchBarDesktop_text_input')]"));
    public static final Locator ROOMS_TAB = new Locator("ROOMS_TAB", new By.ByXPath("//div[contains(@class,'NavigationTabs_navigation_tab')]" + "//span[normalize-space()='Rooms' or normalize-space()='Kamar']"));
    private static final Locator FIRST_ROOM_CARD = new Locator("FIRST_ROOM_CARD", new By.ByXPath("(//*[@data-testid='room-inner-card-container'])[1]"));

    public PDPPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> PDPPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new PDPPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new PDPPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    public void switchToNewTab() {
        driverTools.switchToNewTab();
    }

    public String getHotelRegionDetail() {
        WebElement regionText = driverTools.waitForElementToAppear(REGION).orElseThrow();
        return driverTools.getText(regionText);
    }


    public void pdpSrpBackAndForth(int count) {
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);

        for (int i = 1; i < count; i++) {
            hotelSRP.clickHotelCard(i, true);
            Assertion.assertThat("Verify User is redirected to PDP", this.isPdpLoaded(), Is.is(true));
            driverTools.back();
        }
    }

    public void checkPdpTotalPrice(SelectRoomResponse selectRoomResponse, String selectedHotelName, String currency, String separator, boolean isTax) {
        HotelApi<?> hotelApi = new HotelApi<>(session);
        String hotelPricePdpApi = currency.toUpperCase() + " " + thousandSeparator(String.valueOf((int)hotelApi.getHotelTotalPriceWithTax(isTax, selectRoomResponse)), separator);
        String hotelBasePricePdpApi = currency.toUpperCase() + " " + thousandSeparator(String.valueOf((int)hotelApi.getHotelTotalCrossedPriceWithTax(isTax, selectRoomResponse)), separator);
        isPdpLoaded();
        Assertion.assertThat("Price in PDP page is same as API price for hotel " + selectedHotelName, getPdpPrice(), is(hotelPricePdpApi));
        if (!hotelPricePdpApi.equals(hotelBasePricePdpApi)) {
            logger.log("PDP: There is crossed price for this hotel");
            Assertion.assertThat("Crossed price in PDP page is same as API price for hotel " + selectedHotelName, getPdPCrossPrice(), is(hotelBasePricePdpApi));
        }
    }

    private String getPdpPrice() {
        WebElement priceText = driverTools.waitForElementToAppear(PRICE_PDP).orElseThrow();
        return driverTools.getText(priceText);
    }

    private String getPdPCrossPrice() {
        WebElement crossPriceText = driverTools.waitForElementToAppear(CROSS_PRICE_PDP).orElseThrow();
        return driverTools.getText(crossPriceText);
    }

    public boolean isLabelTotalPrice() {
        return driverTools.waitForElementToAppear(TOTAL_PRICE_LABEL).isPresent();
    }

    public String getRoomName() {
        WebElement loc = driverTools.waitForElementToAppear(FIRST_ROOM_CARD_TITLE).orElseThrow();
        return driverTools.getText(loc);
    }

    public String getRoomPrice() {
        WebElement loc = driverTools.waitForElementToAppear(FIRST_ROOM_CARD_PRICE).orElseThrow();
        return driverTools.getText(loc);
    }

    public String getRoomBedType() {
        WebElement loc = driverTools.waitForElementToAppear(FIRST_ROOM_CARD_BED_TYPE).orElseThrow();
        return driverTools.getText(loc);
    }

    public String getRoomDetailName() {
        WebElement loc = driverTools.waitForElementToAppear(ROOM_DETAIL_TITLE).orElseThrow();
        return driverTools.getText(loc);
    }

    public String getRoomDetailPrice() {
        WebElement loc = driverTools.waitForElementToAppear(ROOM_DETAIL_PRICE).orElseThrow();
        return driverTools.getText(loc).replace("\"", "");
    }

    public String getRoomDetailBedType() {
        WebElement loc = driverTools.waitForElementToAppear(ROOM_DETAIL_BED_TYPE).orElseThrow();
        return driverTools.getText(loc);
    }

    public void clickRoomCard() {
        driverTools.clickWithJavascriptExecutor(FIRST_ROOM_CARD_TITLE);
    }

    public void clickBookButtonRoomDetail() {
        driverTools.click(ROOM_DETAIL_BOOK_BUTTON);
    }

    public void clickRoomDetailPriceBreakdownChevron() {
        driverTools.click(ROOM_DETAIL_PRICEBREAKDOWN_CHEVRON);
        driverTools.waitForElementsToAppear(ROOM_DETAIL_PRICEBREAKDOWN_BOTTOMSHEET_CONTAINER);
    }

    public boolean isTotalPriceInRoomDetailPriceDetailVisible(){
        return driverTools.isElementPresent(ROOM_DETAIL_PRICEBREAKDOWN_TOTALPRICE);
    }

    public boolean isPriceDetailVisible() {
        return driverTools.isElementDisplayed(ROOM_DETAIL_PRICEBREAKDOWN_PRICEDETAIL);
    }

    public boolean isTaxVisible() {
        return  driverTools.isElementDisplayed(ROOM_DETAIL_PRICEBREAKDOWN_TAX);
    }

    public boolean isTiketRewardVisible() {
        return  driverTools.isElementDisplayed(ROOM_DETAIL_PRICEBREAKDOWN_TIKETREWARD);
    }

    public void seeAllPhotos() {
        driverTools.click(OPEN_ROOM_GALLERY);
    }

    public boolean isImageCounterVisible(){
        driverTools.waitForElementToAppear(SEE_ROOM,5);
        return driverTools.isElementDisplayed(IMAGE_DESCRIPTION_COUNTER);
    }

    public boolean isImageThumbnailVisible(){
        return driverTools.isElementDisplayed(IMAGE_THUMBNAIL);
    }

    public boolean isImageTitleVisible(){
        return driverTools.isElementDisplayed(IMAGE_DESCRIPTION_TITLE);
    }


    public void goToHotelViaDeeplink(String location, String publicHotelId, String day) {
        String checkInDate = driverTools.getNearestDayDate(day);
        String baseUrl = switch (Configs.ENVIRONMENT) {
            case GK -> STAGING_URL;
            case PREPROD -> PREPROD_URL;
            case PROD -> PROD_URL ;
            default -> throw new NotImplementedException("Unsupported environment: " + Configs.ENVIRONMENT);
        };

        String deeplink = String.format("%s/%s/%s?checkin=%s", baseUrl, location, publicHotelId, checkInDate);

        driverTools.getWithDynamicTimeout(deeplink, 60);
        driverTools.waitForElementsToAppear(HOTEL_NAME);
    }

    public void goToHotelViaDeeplink(String location, String publicHotelId, String day, int adult, int room) {
        String checkInDate = driverTools.getNearestDayDate(day);

        String baseUrl = switch (Configs.ENVIRONMENT) {
            case GK -> STAGING_URL;
            case PREPROD -> PREPROD_URL;
            case PROD -> PROD_URL;
            default -> throw new NotImplementedException("Unsupported environment: " + Configs.ENVIRONMENT);
        };

        String deeplink = String.format(
                "%s/%s/%s?checkin=%s&adult=%d&room=%d",
                baseUrl, location, publicHotelId, checkInDate, adult, room
        );

        driverTools.getWithDynamicTimeout(deeplink, 60);
        driverTools.waitForElementsToAppear(HOTEL_NAME);
    }
    public void goToHomeViaDeeplink(String location, String publicHotelId, String day) {
        String checkInDate = driverTools.getNearestDayDate(day);
        String baseUrl = switch (Configs.ENVIRONMENT) {
            case GK -> STAGING_URL;
            case PREPROD -> PREPROD_URL_NHA;
            case PROD -> PROD_URL;
            default -> throw new NotImplementedException("Unsupported environment: " + Configs.ENVIRONMENT);
        };
        String deeplink = String.format("%s/%s/%s?checkin=%s", baseUrl, location, publicHotelId, checkInDate);

        driverTools.getWithDynamicTimeout(deeplink, 60);
        driverTools.waitForElementsToAppear(HOTEL_NAME);
        }


        public boolean isHotelNameVisible() {
        return driverTools.isElementDisplayed(HOTEL_NAME);
    }

    public String getHotelName() {
        WebElement hotelName = driverTools.waitForElementToAppear(HOTEL_NAME).orElseThrow();
        return driverTools.getText(hotelName);
    }

    public boolean isNextAvailableDateNightVisible() {
        return driverTools.isElementDisplayed(NEXT_AVAILABLE_DATE_NIGHT);
    }

    public boolean isNextAvailableDateRangeVisible() {
        return driverTools.isElementDisplayed(NEXT_AVAILABLE_DATE_DATERANGE);
    }

    public boolean isNextAvailableDatePriceVisible() {
        return driverTools.isElementDisplayed(NEXT_AVAILABLE_DATE_PRICE);
    }

    public boolean isNextAvailableDateTotalPriceTextVisible() {
        return driverTools.isElementDisplayed(NEXT_AVAILABLE_DATE_TOTAL_PRICE_TEXT);
    }

    public boolean isFullyBookedTitleVisible() {
        return driverTools.isElementDisplayed(FULLY_BOOKED_TITLE);
    }

    public boolean isBestPriceRibbonPDPVisible() {
        return driverTools.isElementDisplayed(BEST_PRICE_RIBBON_PDP);
    }

    public boolean isBestPriceRibbonRoomListVisible() {
        return driverTools.isElementDisplayed(BEST_PRICE_RIBBON_ROOM_LIST);
    }

    public boolean isNextAvailableDateNoRoomTextVisible() {
        return driverTools.isElementDisplayed(NEXT_AVAILABLE_DATE_NO_ROOM_TEXT);
    }

    public abstract void clickNextAvailableDate();

    public abstract void clickSeeRoomButton();

    public void closeRoomDetail() {
        driverTools.clickWithJavascriptExecutor(CLOSE_ROOM_DETAIL_BUTTON);
    }

    public boolean isPdpLoaded() {
        driverTools.waitForElementToAppear(SEE_ROOM);
        driverTools.waitForElementToAppear(HOTEL_IMAGE_CARD);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        homePageWeb.continueWithoutApp(5);
        return driverTools.waitForElementToPresence(HEADING_PDP, 5).isPresent();
    }

    public boolean isHotelImageVisible() {
        driverTools.waitForElementToAppear(HOTEL_IMAGE_CARD, 5);
        return driverTools.isElementDisplayed(HOTEL_IMAGE_CARD);
    }

    public String getHotelNameDetail() {
        WebElement headingText = driverTools.waitForElementToAppear(HEADING_PDP).orElseThrow();
        return driverTools.getText(headingText);
    }

    public String getHotelAreaDetail() {
        WebElement areaText = driverTools.waitForElementToAppear(AREA).orElseThrow();
        return driverTools.getText(areaText);
    }

    public String getHotelCityDetail() {
        WebElement cityText = driverTools.waitForElementToAppear(CITY).orElseThrow();
        return driverTools.getText(cityText);
    }

    public boolean isHighLightsVisible() {
        driverTools.waitForElementToAppear(HIGHLIGHTS, 5);
        return driverTools.isElementDisplayed(HIGHLIGHTS);
    }

    public boolean isPopularFacilitiesVisible() {
        driverTools.waitForElementToAppear(POPULAR_FACILITIES, 5);
        return driverTools.isElementDisplayed(POPULAR_FACILITIES);
    }

    public boolean isReviewsVisible() {
        driverTools.waitForElementToAppear(REVIEWS, 5);
        return driverTools.isElementDisplayed(REVIEWS);
    }

    public boolean isPdpMapVisible() {
        driverTools.waitForElementToAppear(PDP_MAP, 5);
        return driverTools.isElementDisplayed(PDP_MAP);
    }

    public boolean isAccommodationPoliciesVisible() {
        driverTools.waitForElementToAppear(ACCOMMODATION_POLICIES, 5);
        return driverTools.isElementDisplayed(ACCOMMODATION_POLICIES);
    }

    public boolean isFAQVisible() {
        driverTools.waitForElementToAppear(FAQ, 5);
        return driverTools.isElementDisplayed(FAQ);
    }

    public boolean isRoomsAvailable() {
        driverTools.waitForElementToAppear(BOOKING_FULL_TEXT, 5);
        return !driverTools.isElementDisplayed(BOOKING_FULL_TEXT);
    }

    public abstract String getHotelPriceDetail();

    public boolean isSeeRoomVisible() {
        driverTools.waitForElementToAppear(SEE_ROOM, 5);
        return driverTools.isElementDisplayed(SEE_ROOM);
    }

    public String getBookingFullDetail() {
        WebElement bookingFullText = driverTools.waitForElementToAppear(BOOKING_FULL_TEXT).orElseThrow();
        return driverTools.getText(bookingFullText);
    }
    public boolean checkMaps(){
        driverTools.waitForElementToAppear(MAPS_PAGE);
        return driverTools.isElementDisplayed(MAPS_PAGE);
    }

    public boolean isChosenHotelHighlightedOnMap(String hotelName) {
        return driverTools.isElementDisplayed(hotelMarkerOnMapLocator(hotelName));
    }

    private Locator hotelMarkerOnMapLocator(String hotelName) {
        return new Locator("HOTEL_MARKER_ON_MAP_" + hotelName.toUpperCase(), new By.ByXPath("//*[contains(@class,'PoiInfoWindow_info_window')]/*[text()='"+hotelName+"']"));
    }

    /**
     * True when shopping/culinary POI markers are on the map, or when the map correctly
     * shows the empty-state banner (e.g. SDET hotels with no nearby interesting places).
     */
    public boolean areaNearbyPointsShownOnMap() {
        if (driverTools.isElementDisplayed(NEARBY_POINTS_ON_MAP)) {
            return true;
        }
        return driverTools.waitForElementToAppear(NEARBY_POINTS_EMPTY_STATE, 5).isPresent()
                && driverTools.isElementDisplayed(NEARBY_POINTS_EMPTY_STATE);
    }

    public void clickNearbyChip() {
        driverTools.waitForElementToAppear(ACCOMMODATION_NEARBY_SECTION, 10);
        driverTools.click(ACCOMMODATION_NEARBY_SECTION);
    }

    public boolean isNearbyAccommodationShown() {
        return driverTools.waitForElementToAppear(ACCOMMODATION_NEARBY_LIST_CONTAINER,15).isPresent();
    }

    public boolean isNearbyChipDataShown() {
        return driverTools.waitForElementToAppear(NEARBY_CHIP).isPresent();
    }

    public void goToMaps(){
        driverTools.scrollToElement(PDP_MAP);
        driverTools.waitForElementToAppear(PDP_MAP,10);
        driverTools.clickWithJavascriptExecutor(PDP_MAP);
    }

    public void seeHotelRoom() {
        driverTools.clickWithJavascriptExecutor(SEE_ROOM);
    }

    public boolean isStarRatingVisible() {
        driverTools.waitForElementToAppear(STAR_RATING_PDP, 5);
        return driverTools.isElementDisplayed(STAR_RATING_PDP);
    }

    public boolean isPropertyTypeVisible() {
        driverTools.waitForElementToAppear(PROPERTY_TYPE_PDP, 5);
        return driverTools.isElementDisplayed(PROPERTY_TYPE_PDP);
    }
    public boolean isPreferredPartnerVisible() {
        return driverTools.waitForElementToPresence(
                getPreferredPartnerSectionLocator("Preferred Partner"), 10).isPresent()
                || driverTools.waitForElementToPresence(
                getPreferredPartnerSectionLocator("Preferred Partner Plus"), 10).isPresent();
    }
    /**
     * Verifies that the "Preferred Partner Plus" list item is visible on PDP.
     */
    public boolean isPreferredPartnerPlusListItemVisible() {
        return driverTools.waitForElementToPresence(getPreferredPartnerSectionLocator("Preferred Partner Plus"), 10).isPresent();
    }

    private Locator getPreferredPartnerSectionLocator(String partnerLabel) {
        return new Locator("PDP_PREFERRED_PARTNER_SECTION",
                new By.ByXPath("//*[@data-testid='preferred-partner']//*[text()='" + partnerLabel + "']"));
    }

    private Locator getPreferredPartnerBottomSheetLocator(String partnerLabel) {
        return new Locator("PDP_PREFERRED_PARTNER_BOTTOM_SHEET",
                new By.ByXPath("//*[text()='" + partnerLabel + "']//parent::*[contains(@class,'modal_header')]/parent::section"));
    }

    /**
     * Asserts the Preferred Partner section (e.g. {@code Preferred Partner Plus}) is visible on PDP.
     */
    public void assertPreferredPartnerSectionVisibleOnPdp(String partnerLabel) {
        Locator section = getPreferredPartnerSectionLocator(partnerLabel);
        driverTools.waitForElementToAppear(section, 15).orElseThrow();
        Assertion.assertThat(
                "'" + partnerLabel + "' section should be visible on PDP",
                driverTools.isElementDisplayed(section), is(true));
    }

    /**
     * Clicks the Preferred Partner section on PDP to open its detail bottom sheet.
     */
    public void clickPreferredPartnerSectionOnPdp(String partnerLabel) {
        Locator section = getPreferredPartnerSectionLocator(partnerLabel);
        driverTools.waitForElementToAppear(section, 15).orElseThrow();
        driverTools.scrollIntoWebView(section);
        driverTools.clickWithJavascriptExecutor(section);
    }

    /**
     * Asserts the Preferred Partner bottom sheet is visible after clicking the section on PDP.
     */
    public void assertPreferredPartnerBottomSheetVisibleOnPdp(String partnerLabel) {
        Locator bottomSheet = getPreferredPartnerBottomSheetLocator(partnerLabel);
        driverTools.waitForElementToAppear(bottomSheet, 10).orElseThrow();
        Assertion.assertThat(
                "'" + partnerLabel + "' bottom sheet should be visible on PDP",
                driverTools.isElementDisplayed(bottomSheet), is(true));
    }

    /**
     * Clicks the Preferred Partner section and asserts its bottom sheet opens on PDP.
     */
    public void openPreferredPartnerBottomSheetOnPdp(String partnerLabel) {
        clickPreferredPartnerSectionOnPdp(partnerLabel);
        assertPreferredPartnerBottomSheetVisibleOnPdp(partnerLabel);
    }

    public boolean isHotelLocationVisible() {
        driverTools.waitForElementToAppear(HOTEL_LOCATION_PDP, 5);
        return driverTools.isElementDisplayed(HOTEL_LOCATION_PDP);
    }

    public void scrollToReviewSection(){
        driverTools.waitForElementToAppear(REVIEW_SECTION_PDP,5);
        driverTools.swipeToElement(REVIEW_SECTION_PDP, Swipe.UP, 5);
    }

    public boolean isHotelReviewVisible() {
        driverTools.waitForElementToAppear(HOTEL_REVIEW_PDP, 5);
        return driverTools.isElementDisplayed(HOTEL_REVIEW_PDP);
    }

    public boolean isHotelFacilitiesVisible() {
        driverTools.waitForElementToAppear(HOTEL_FACILITIES_PDP, 5);
        return driverTools.isElementDisplayed(HOTEL_FACILITIES_PDP);
    }

    public boolean isHotelPriceVisible() {
        driverTools.waitForElementToAppear(HOTEL_PRICE_PDP, 5);
        return driverTools.isElementDisplayed(HOTEL_PRICE_PDP);
    }

    public void scrollToHotelAboutSection(){
        driverTools.waitForElementToAppear(ABOUT_HOTEL_PDP,5);
        driverTools.swipeToElement(ABOUT_HOTEL_PDP, Swipe.UP, 5);
    }

    public boolean isHotelAboutVisible() {
        driverTools.waitForElementToAppear(ABOUT_HOTEL_PDP, 5);
        return driverTools.isElementDisplayed(ABOUT_HOTEL_PDP);
    }

    public boolean isAllRoomsFullyBooked() {
        return driverTools.waitForElementToPresence(ROOM_FULLY_BOOKED, 5).isPresent();
    }

    public void clickCalenderPopUp() {
        driverTools.scrollToElement(CALENDER_POP_UP);
        driverTools.clickWithJavascriptExecutor(CALENDER_POP_UP);
    }

    public void clickChangeDate() {
        driverTools.scrollToElement(CHANGE_DATE);
        driverTools.clickWithJavascriptExecutor(CHANGE_DATE);
    }

    public void clickDate() {
        driverTools.scrollToElement(DATE_CLICK);
        driverTools.clickWithJavascriptExecutor(DATE_CLICK);
    }

    public void clickNightSave() {
        driverTools.scrollToElement(CALENDER_SAVE);
        driverTools.clickWithJavascriptExecutor(CALENDER_SAVE);
    }
    public boolean isRoomRecommendationCardVisible() {
        driverTools.swipeToElement(ROOM_CARD_ANY, Swipe.UP, 5);
        driverTools.waitForElementToAppear(ROOM_CARD_ANY, 10);
        return driverTools.isElementDisplayed(ROOM_CARD_ANY);
    }


    public String getRoomNameFromCard() {
        if (driverTools.isElementPresent(ROOM_NAME_IN_RECOMMENDATION_CARD)) {
            return driverTools.getText(ROOM_NAME_IN_RECOMMENDATION_CARD).orElse("");
        }
        if (driverTools.isElementPresent(ROOM_NAME_IN_FACILITIES_NUDGES)) {
            return driverTools.getText(ROOM_NAME_IN_FACILITIES_NUDGES).orElse("");
        }
        return "";
    }

    public boolean isBookRoomButtonVisible() {driverTools.waitForElementToAppear(BOOK_ROOM_BUTTON, 10);
        return driverTools.isElementDisplayed(BOOK_ROOM_BUTTON);
    }

    public void clickBookRoomButton() {driverTools.waitForElementToAppear(BOOK_ROOM_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(BOOK_ROOM_BUTTON);
    }

    public boolean isRoomDetailTotalRoomVisible() {driverTools.waitForElementToAppear(ROOM_DETAIL_TOTAL_ROOM, 10);
        return driverTools.isElementDisplayed(ROOM_DETAIL_TOTAL_ROOM);
    }




    public void clickSearchButton() {
        driverTools.scrollToElement(SEARCH_BUTTON);
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
    }

    public abstract void setStayDatePDP(String type, String date);

    public void clickOpenDate() {
        driverTools.waitForElementToAppear(DATE_FIELD);
        driverTools.clickWithJavascriptExecutor(DATE_FIELD);
    }

    public boolean scrollUpToReviewTab() {
        driverTools.scrollToElement(REVIEW_TAB_BUTTON);
        driverTools.waitForElementToPresence(REVIEW_TAB_BUTTON, 15);
        return driverTools.isElementDisplayed(REVIEW_TAB_BUTTON);

    }

    public void clickReviewTab() {
        driverTools.clickWithJavascriptExecutor(REVIEW_TAB_BUTTON);
    }

    public boolean isReviewSectionHeadingVisible() {
        driverTools.waitForElementToAppear(REVIEW_SECTION_HEADING, 10);
        return driverTools.isElementDisplayed(REVIEW_SECTION_HEADING);
    }

    public boolean isReviewCardsVisible() {
        driverTools.waitForElementToAppear(REVIEW_CARD, 10);
        return driverTools.isElementDisplayed(REVIEW_CARD);
    }

    public void clickFirstReviewCard() {
        driverTools.waitForElementToAppear(FIRST_REVIEW_CARD, 10);
        driverTools.clickWithJavascriptExecutor(FIRST_REVIEW_CARD);
    }

    public void clickReviewLikeIcon() {
        driverTools.waitForElementToPresence(REVIEW_LIKE_ICON, 25);
        driverTools.click(REVIEW_LIKE_ICON);
    }

    public boolean isReviewLoginPromptVisible() {
        driverTools.waitForElementToAppear(REVIEW_LOGIN_PROMPT_TITLE, 10);
        return driverTools.isElementDisplayed(REVIEW_LOGIN_PROMPT_TITLE);
    }

    public void clickReviewLoginCancelButton() {
        driverTools.waitForElementToAppear(REVIEW_LOGIN_CANCEL_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(REVIEW_LOGIN_CANCEL_BUTTON);
    }

    public void clickReviewPopupCloseButton() {
        driverTools.waitForElementToAppear(REVIEW_POPUP_CLOSE_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(REVIEW_POPUP_CLOSE_BUTTON);
    }

    public void clickSeeAllReviews() {
        driverTools.waitForElementToAppear(REVIEW_SEE_ALL_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(REVIEW_SEE_ALL_BUTTON);
    }

    public boolean isReviewPageTitleVisible() {
        driverTools.waitForElementToAppear(REVIEW_PAGE_TITLE, 10);
        return driverTools.isElementDisplayed(REVIEW_PAGE_TITLE);
    }

    public boolean isReviewPageCardListVisible() {
        driverTools.waitForElementToAppear(REVIEW_PAGE_CARD_LIST, 10);
        return driverTools.isElementDisplayed(REVIEW_PAGE_CARD_LIST);
    }

    public boolean isReviewPageCardsVisible() {
        driverTools.waitForElementToAppear(REVIEW_PAGE_CARD, 10);
        return driverTools.isElementDisplayed(REVIEW_PAGE_CARD);
    }

    public boolean isReviewPageCardDateVisible() {
        driverTools.waitForElementToAppear(REVIEW_PAGE_CARD_DATE, 10);
        return driverTools.isElementDisplayed(REVIEW_PAGE_CARD_DATE);
    }

    public boolean isReviewPageCardRatingVisible() {
        driverTools.waitForElementToAppear(REVIEW_PAGE_CARD_RATING, 10);
        return driverTools.isElementDisplayed(REVIEW_PAGE_CARD_RATING);
    }

    public void clickReviewPageSortButton() {
        driverTools.waitForElementToAppear(REVIEW_PAGE_SORT_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(REVIEW_PAGE_SORT_BUTTON);
    }

    public String getFirstReviewCardDate() {
        driverTools.waitForElementToAppear(FIRST_REVIEW_PAGE_CARD_DATE, 10);
        return driverTools.getText(FIRST_REVIEW_PAGE_CARD_DATE).orElse("");
    }

    public String getFirstReviewCardRating() {
        driverTools.waitForElementToAppear(FIRST_REVIEW_PAGE_CARD_RATING, 10);
        return driverTools.getText(FIRST_REVIEW_PAGE_CARD_RATING).orElse("");
    }

    public void selectReviewSortOption(String sortOption) {
        Locator sortLocator;
        switch (sortOption.toLowerCase()) {
            case "latest review":
                sortLocator = REVIEW_SORT_LATEST;
                break;
            case "most helpful":
                sortLocator = REVIEW_SORT_MOST_HELPFUL;
                break;
            case "rating (high to low)":
                sortLocator = REVIEW_SORT_RATING_HIGH_TO_LOW;
                break;
            case "rating (low to high)":
                sortLocator = REVIEW_SORT_RATING_LOW_TO_HIGH;
                break;
            default:
                throw new IllegalArgumentException("Invalid sort option: " + sortOption);
        }
        driverTools.waitForElementToAppear(sortLocator, 10);
        driverTools.clickWithJavascriptExecutor(sortLocator);
        driverTools.waitForElementToAppear(REVIEW_PAGE_CARD, 5);
    }

    public void isDateMoreRecentOrEqual(String dateBeforeSort, String dateAfterSort) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
        LocalDate dateBefore = LocalDate.parse(dateBeforeSort, formatter);
        LocalDate dateAfter = LocalDate.parse(dateAfterSort, formatter);
        logger.log("Date before: " + dateBefore + ", Date after: " + dateAfter);
        Assertion.assertThat("Date after sorting should be more recent or equal", dateAfter, greaterThanOrEqualTo(dateBefore));
    }

    public void isRatingHigherOrEqual(String ratingBefore, String ratingAfter) {
        double before = Double.parseDouble(ratingBefore);
        double after = Double.parseDouble(ratingAfter);
        logger.log("Rating before: " + before + ", Rating after: " + after);
        Assertion.assertThat("Rating after sorting should be higher or equal", after, greaterThanOrEqualTo(before));
    }

    public void isRatingLowerOrEqual(String ratingBefore, String ratingAfter) {
        double before = Double.parseDouble(ratingBefore);
        double after = Double.parseDouble(ratingAfter);
        logger.log("Rating before: " + before + ", Rating after: " + after);
        Assertion.assertThat("Rating after sorting should be lower or equal", after, lessThanOrEqualTo(before));
    }

    public String getFirstReviewCardHelpfulnessCount() {
        driverTools.waitForElementToAppear(FIRST_REVIEW_PAGE_CARD_HELPFULNESS, 10);
        return driverTools.getText(FIRST_REVIEW_PAGE_CARD_HELPFULNESS).orElse("0");
    }

    public int parseHelpfulnessCount(String helpfulnessText) {
        try {
            String numericPart = helpfulnessText.replaceAll("[^0-9]", "");
            if (numericPart.isEmpty()) {
                return 0;
            }
            return Integer.parseInt(numericPart);
        } catch (Exception e) {
            logger.log("Error parsing helpfulness count: " + e.getMessage());
            return 1;
        }
    }

    public void isHelpfulnessCountHigherOrEqual(String helpfulnessBefore, String helpfulnessAfter) {
        int before = parseHelpfulnessCount(helpfulnessBefore);
        int after = parseHelpfulnessCount(helpfulnessAfter);
        logger.log("Helpfulness count before: " + before + ", after: " + after);
        Assertion.assertThat("Helpfulness count after sorting should be higher or equal", after, greaterThanOrEqualTo(before));
    }

    public void verifySortingApplied(String sortOption, String dateBeforeSort, String dateAfterSort, 
                                      String ratingBeforeSort, String ratingAfterSort) {
        verifySortingApplied(sortOption, dateBeforeSort, dateAfterSort, ratingBeforeSort, ratingAfterSort, "", "");
    }

    public void verifySortingApplied(String sortOption, String dateBeforeSort, String dateAfterSort, 
                                      String ratingBeforeSort, String ratingAfterSort,
                                      String helpfulnessBeforeSort, String helpfulnessAfterSort) {
        switch (sortOption.toLowerCase()) {
            case "latest review":
                isDateMoreRecentOrEqual(dateBeforeSort, dateAfterSort);
                break;
            case "rating (high to low)":
                isRatingHigherOrEqual(ratingBeforeSort, ratingAfterSort);
                break;
            case "rating (low to high)":
                isRatingLowerOrEqual(ratingBeforeSort, ratingAfterSort);
                break;
            case "most helpful":
                isHelpfulnessCountHigherOrEqual(helpfulnessBeforeSort, helpfulnessAfterSort);
                break;
            default:
                logger.log("Unknown sort option: " + sortOption);
        }
    }

    public String verifyLocationOnMap(){
        driverTools.waitForElementToAppear(ACCOMMODATION_NEARBY_SECTION,10);
        driverTools.clickWithJavascriptExecutor(ACCOMMODATION_NEARBY_SECTION);
        driverTools.waitForElementToAppear(MAP_ADDRESS,20);
        return driverTools.getText(MAP_ADDRESS).orElseThrow();
    }

    public void clickHotelImage() {
        driverTools.waitForElementToAppear(HOTEL_IMAGE_CARD, 10);
        driverTools.clickWithJavascriptExecutor(HOTEL_IMAGE_CARD);
    }


    public boolean isGalleryOpened() {
        driverTools.waitForElementToAppear(GALLERY_CONTAINER, 10);
        return driverTools.isElementDisplayed(GALLERY_CONTAINER) && driverTools.isElementDisplayed(GALLERY_TITLE);
    }


    public boolean isGalleryTitleVisible() {
        driverTools.waitForElementToAppear(GALLERY_TITLE, 10);
        return driverTools.isElementDisplayed(GALLERY_TITLE);
    }


    public boolean isGalleryImagesVisible() {
        driverTools.waitForElementToAppear(GALLERY_IMAGE_ITEM, 10);
        return driverTools.isElementDisplayed(GALLERY_IMAGE_ITEM);
    }


    public void closeGallery() {
        driverTools.waitForElementToAppear(GALLERY_CLOSE_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(GALLERY_CLOSE_BUTTON);
    }


    public boolean isGalleryClosed() {
        driverTools.waitForElementToDisappear(GALLERY_CONTAINER, 5);
        return !driverTools.isElementDisplayed(GALLERY_CONTAINER);
    }

    /**
     * Clicks the gallery thumbnail at the given 1-based index inside the currently
     * active tab/group of the desktop image gallery.
     *
     * @param index 1-based index of the thumbnail (1 = first image, 2 = second image, ...)
     */
    public void clickGalleryImageAt(int index) {
        Locator imageItem = getGalleryImageItemAtIndex(index);
        driverTools.waitForElementToAppear(imageItem, 10);
        driverTools.clickWithJavascriptExecutor(imageItem);
    }

    private Locator getGalleryImageItemAtIndex(int index) {
        return new Locator("GALLERY_IMAGE_ITEM_AT_INDEX", new By.ByXPath("(//div[starts-with(@data-testid,'image-gallery-desktop-navigation-image-item-')])["+index+"]"));
    }

    /**
     * Returns the gallery banner counter text, e.g. "1/174" or "2/174".
     */
    public String getGalleryCounterText() {
        Locator counter = new Locator(
                "GALLERY_SLIDER_COUNTER",
                By.xpath("//section[contains(@class,'desktop_image_gallery_popup')]//span[@data-testid='slider-text' and contains(text(),'/')]"));
        WebElement element = driverTools.waitForElementToAppear(counter, 10).orElseThrow();
        return driverTools.getText(element).strip();
    }

    /**
     * Parses the current image index from the gallery banner counter (e.g. for "2/174" returns 2).
     * Returns -1 if the counter cannot be parsed.
     */
    public int getGalleryCurrentImageIndex() {
        String counter = getGalleryCounterText();
        if (counter == null || !counter.contains("/")) {
            return -1;
        }
        try {
            return Integer.parseInt(counter.split("/")[0].strip());
        } catch (NumberFormatException ignored) {
            return -1;
        }
    }

    /**
     * Polls the gallery banner counter until its current index matches {@code expectedIndex}
     * or the timeout elapses. Useful right after clicking a thumbnail because the slider
     * counter updates asynchronously while the keen-slider transitions.
     *
     * @param expectedIndex 1-based index expected to be active in the gallery banner
     * @param timeoutSeconds maximum time to wait for the counter to reach the expected index
     * @return true if the counter reached {@code expectedIndex}, false on timeout
     */
    public boolean waitForGalleryCurrentImageIndexToBe(int expectedIndex, int timeoutSeconds) {
        AtomicInteger lastSeen = new AtomicInteger(-1);
        try {
            new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(Math.max(0, timeoutSeconds)))
                    .pollingEvery(Duration.ofMillis(250))
                    .ignoring(StaleElementReferenceException.class, NoSuchElementException.class)
                    .until(d -> {
                        int current = getGalleryCurrentImageIndex();
                        lastSeen.set(current);
                        return current == expectedIndex;
                    });
            return true;
        } catch (TimeoutException e) {
            logger.log("Gallery counter did not reach index " + expectedIndex
                    + " within " + timeoutSeconds + "s; last seen index=" + lastSeen.get());
            return false;
        }
    }


    public void clickSeeRoomsButton() {
        driverTools.waitForElementToAppear(SEE_ROOM, 10);
        driverTools.clickWithJavascriptExecutor(SEE_ROOM);
    }


    public boolean isRoomListFilterChipsVisible() {
        driverTools.waitForElementToAppear(ROOM_LIST_FILTER_CHIPS_CONTAINER, 10);
        return driverTools.isElementDisplayed(ROOM_LIST_FILTER_CHIPS_CONTAINER);
    }

    public void clickRoomListFilterChip(String chipText) {
        Locator filterChip = getFilterChipByText(chipText);
        driverTools.waitForElementToAppear(filterChip, 10);
        driverTools.scrollIntoWebView(filterChip);
        driverTools.clickWithJavascriptExecutor(filterChip);
        logger.log("Clicked on filter chip: " + chipText);
    }

    public void clickRoomInnerCardChevron() {
        if (driverTools.isElementPresent(ROOM_NAME_IN_RECOMMENDATION_CARD)) {
            driverTools.click(ROOM_NAME_IN_RECOMMENDATION_CARD);
        }
        else if (driverTools.isElementPresent(ROOM_NAME_IN_FACILITIES_NUDGES)) {
            driverTools.click(ROOM_INNER_CARD_CHEVRON);
        }
    }

    public boolean isRoomInfoDetailsDisplayed() {
        boolean capacity = driverTools.waitForElementToPresence(ROOM_INFO_CAPACITY).isPresent();
        boolean meal = driverTools.isElementDisplayed(ROOM_INFO_MEAL);
        boolean bed = driverTools.isElementDisplayed(ROOM_INFO_BED_TYPE);
        boolean smokingPreferences = driverTools.isElementDisplayed(ROOM_INFO_SMOKING_PREFERENCES);
        logger.log("isRoomInfoDetailsDisplayed -> capacity:" + capacity + ", meal:" + meal + ", bed:" + bed + ", smokingPreferences:" + smokingPreferences);
        return capacity && meal && bed && smokingPreferences;
    }

    public boolean isRoomDetailPageOpened() {
        driverTools.waitForElementToAppear(ROOM_DETAIL_CONTAINER, 10);
        return driverTools.isElementDisplayed(ROOM_DETAIL_CONTAINER);
    }

    public boolean isRoomDetailNameVisible() {
        driverTools.swipeToElement(ROOM_DETAIL_NAME, Swipe.UP, 3);
        driverTools.waitForElementToAppear(ROOM_DETAIL_NAME, 10);
        return driverTools.isElementDisplayed(ROOM_DETAIL_NAME);
    }


    public String getRoomDetailNames() {
        driverTools.waitForElementToAppear(ROOM_DETAIL_NAME, 10);
        return driverTools.getText(ROOM_DETAIL_NAME).orElse("");
    }

    public boolean isRoomDetailSizeVisible() {
        driverTools.swipeToElement(ROOM_DETAIL_SIZE, Swipe.UP, 3);
        driverTools.waitForElementToAppear(ROOM_DETAIL_SIZE, 10);
        return driverTools.isElementDisplayed(ROOM_DETAIL_SIZE);
    }


    public boolean isRoomDetailBedTypeVisible() {
        driverTools.swipeToElement(ROOM_DETAIL_BED_TYPE_OVERVIEW, Swipe.UP, 3);
        driverTools.waitForElementToAppear(ROOM_DETAIL_BED_TYPE_OVERVIEW, 10);
        return driverTools.isElementDisplayed(ROOM_DETAIL_BED_TYPE_OVERVIEW);
    }


    public boolean isRoomDetailCheckinProcedureVisible() {
        driverTools.swipeToElement(ROOM_DETAIL_CHECKIN_PROCEDURE_CONTAINER, Swipe.UP, 5);
        driverTools.waitForElementToAppear(ROOM_DETAIL_CHECKIN_PROCEDURE_CONTAINER, 10);
        return driverTools.isElementDisplayed(ROOM_DETAIL_CHECKIN_PROCEDURE_CONTAINER);
    }

    public void clickPriceDetailChevron() {
        driverTools.waitForElementToPresence(ROOM_DETAIL_FOOTER_PRICE_CHEVRON, 10);
        driverTools.click(ROOM_DETAIL_FOOTER_PRICE_CHEVRON);
    }


    public boolean isPriceDetailsPageOpened() {
        driverTools.waitForElementToAppear(PRICE_DETAILS_CONTAINER, 10);
        return driverTools.isElementDisplayed(PRICE_DETAILS_CONTAINER);
    }


    public boolean isPriceDetailsTitleVisible() {
        driverTools.waitForElementToAppear(PRICE_DETAILS_TITLE, 10);
        return driverTools.isElementDisplayed(PRICE_DETAILS_TITLE);
    }


    public String getPriceDetailsSubtotal(String currency) {
        Locator subtotalLocator = getPriceDetailsSubtotalLocator(currency);
        WebElement element = driverTools.waitForElementToAppear(subtotalLocator).orElseThrow();
        return driverTools.getText(element);
    }


    public String getPriceDetailsOtherFees(String currency) {
        Locator otherFeesLocator = getPriceDetailsOtherFeesLocator(currency);
        WebElement element = driverTools.waitForElementToAppear(otherFeesLocator).orElseThrow();
        return driverTools.getText(element);
    }


    public String getPriceDetailsDiscount(String currency) {
        try {
            Locator discountLocator = getPriceDetailsDiscountLocator(currency);
            WebElement element = driverTools.waitForElementToAppear(discountLocator, 3).orElse(null);
            if (element != null) {
                return driverTools.getText(element);
            }
        } catch (Exception e) {
            logger.log("Discount not present: " + e.getMessage());
        }
        return "";
    }


    public boolean isPriceDetailsDiscountPresent() {
        return driverTools.isElementPresent(PRICE_DETAILS_DISCOUNT_LABEL);
    }


    public String getPriceDetailsTotal() {
        WebElement element = driverTools.waitForElementToAppear(PRICE_DETAILS_TOTAL).orElseThrow();
        return driverTools.getText(element);
    }


    public void validatePriceBreakdownCalculation(String currency) {
        String subtotalStr = getPriceDetailsSubtotal(currency).replaceAll("[^0-9]", "");
        String otherFeesStr = getPriceDetailsOtherFees(currency).replaceAll("[^0-9]", "");
        String discountStr = getPriceDetailsDiscount(currency).replaceAll("[^0-9]", "");
        String totalStr = getPriceDetailsTotal().replaceAll("[^0-9]", "");

        long subtotal = Long.parseLong(subtotalStr);
        long otherFees = Long.parseLong(otherFeesStr);
        long discount = discountStr.isEmpty() ? 0 : Long.parseLong(discountStr);
        long total = Long.parseLong(totalStr);

        long calculatedTotal = subtotal + otherFees - discount;

        logger.log("Price Breakdown: Subtotal=" + subtotal + ", Other Fees=" + otherFees + ", Discount=" + discount + ", Expected Total=" + calculatedTotal + ", Actual Total=" + total);

        Assertion.assertThat("Price breakdown calculation is correct (Total = Subtotal + Other Fees - Discount)", calculatedTotal, Is.is(total));
    }


    public void clickPriceDetailsBookButton() {
        driverTools.waitForElementToAppear(PRICE_DETAILS_BOOK_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(PRICE_DETAILS_BOOK_BUTTON);
    }


    public void clickSpecialRequestButton() {
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(SPECIAL_REQUEST_BUTTON);
    }


    public void clickFirstSpecialRequestChip() {
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_FIRST_CHIP, 10);
        driverTools.clickWithJavascriptExecutor(SPECIAL_REQUEST_FIRST_CHIP);
    }


    public void clickSecondSpecialRequestChip() {
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_SECOND_CHIP, 10);
        driverTools.clickWithJavascriptExecutor(SPECIAL_REQUEST_SECOND_CHIP);
    }


    public void fillOtherRequestsTextarea(String text) {
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_OTHER_TEXTAREA, 10);
        driverTools.sendKeys(SPECIAL_REQUEST_OTHER_TEXTAREA, text);
    }


    public void clickSpecialRequestSaveButton() {
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_SAVE_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(SPECIAL_REQUEST_SAVE_BUTTON);
    }

    public void fillSpecialRequest(String otherRequestsText) {
        clickSpecialRequestButton();
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_FORM_CONTAINER, 10);
        clickFirstSpecialRequestChip();
        clickSecondSpecialRequestChip();
        fillOtherRequestsTextarea(otherRequestsText);
        clickSpecialRequestSaveButton();
    }

    public boolean isPromoListLoaded(){
        return  driverTools.waitForElementToPresence(PROMO_LIST,5).isPresent();
    }

    public void clickPromoCopyButton(){
        driverTools.scrollToElement(PROMO_COPY_BUTTON);
        driverTools.clickWithJavascriptExecutor(PROMO_COPY_BUTTON);
    }

    public String copyFirstInfinityPromoCode() {
        driverTools.waitForElementsToAppear(PROMO_COPY_BUTTON, 5);
        String codeBeforeCopy = normalizeNonBreakingSpaceText(driverTools.getText(PROMO_COPY_BUTTON).orElseThrow());
        driverTools.prepareBrowserClipboardCapture();
        clickPromoCopyButton();
        logger.log("copyFirstInfinityPromoCode -> " + codeBeforeCopy);
        return codeBeforeCopy;
    }

    public String getClipboardText() {
        return normalizeNonBreakingSpaceText(driverTools.getTextFromClipboardBrowser());
    }

    public String copyFirstPromoCodeFromSection() {
        driverTools.waitForElementsToAppear(PROMO_SECTION_COPY_BTN, 15);
        String code = normalizeNonBreakingSpaceText(driverTools.getText(PROMO_SECTION_CODE).orElse(""));
        code = stripPromoCodeLabel(code);
        driverTools.prepareBrowserClipboardCapture();
        clickPromoCopyButtonOnSection();
        if (code == null || code.isBlank()) {
            code = normalizeNonBreakingSpaceText(driverTools.getTextFromClipboardBrowser());
            code = stripPromoCodeLabel(code);
        }
        logger.log("copyFirstPromoCodeFromSection -> " + code);
        return code;
    }

    private String stripPromoCodeLabel(String text) {
        if (text == null) {
            return "";
        }
        String trimmed = text.trim();
        if (trimmed.contains(": ")) {
            return trimmed.substring(trimmed.indexOf(": ") + 2).trim();
        }
        return trimmed.replaceFirst("(?i)^(code|kode)\\s+", "").trim();
    }

    public boolean isPromoCopied(){
        String promo=driverTools.waitForElementToAppear(COPIED_PROMO_CODE,5).orElseThrow().getText();
        return  promo.contains("Promo code copied.") || promo.contains("Kode promo berhasil disalin.");
    }

    public void scrollToRightOfPromo() {
        driverTools.scrollToElement(SEE_ALL_BUTTON);
    }

    public boolean isSeeAllPromoButtonDisplayed() {
        return driverTools.waitForElementToPresence(SEE_ALL_BUTTON, 5).isPresent()
                && driverTools.isElementDisplayed(SEE_ALL_BUTTON);
    }

    public void clickSeeAllPromo() {
        driverTools.clickWithJavascriptExecutor(SEE_ALL_BUTTON);
    }

    public void closeSeeAllPromo() {
        driverTools.click(CLOSE_BUTTON_PROMO_SECTION);
    }

    public Boolean isPromoSectionLoaded() {
        return  driverTools.waitForElementToPresence(PROMO_SECTION,5).isPresent();
    }

    public boolean isPromoCopiedOnPromoSection() {
        return  driverTools.waitForElementToPresence(PROMO_CODE_COPIED,20).isPresent();
    }

    public void clickPromoCopyButtonOnSection() {
        driverTools.waitForElementsToAppear(PROMO_SECTION_COPY_BTN,5);
        if(driverTools.isElementDisplayed(PROMO_SECTION_COPY_BTN)){
            driverTools.click(PROMO_SECTION_COPY_BTN);
        }
    }

    public void goToHotelViaDeeplinkWithCheckinCheckout(String location,String publicHotelId,String checkInDate,String checkOutDate ){
        goToHotelViaDeeplinkWithCheckinCheckout(location, publicHotelId, checkInDate, checkOutDate, 1, 1);
    }

    public void goToHotelViaDeeplinkWithCheckinCheckout(String location, String publicHotelId, String checkInDate,
                                                        String checkOutDate, int room, int adult) {
        String baseUrl = switch (Configs.ENVIRONMENT) {
            case GK -> STAGING_URL;
            case PREPROD -> PREPROD_URL;
            case PROD -> PROD_URL;
            default -> throw new NotImplementedException("Unsupported environment: " + Configs.ENVIRONMENT);
        };

        String deeplink = String.format(
                "%s/%s/%s?checkin=%s&checkout=%s&room=%d&adult=%d",
                baseUrl, location, publicHotelId, checkInDate, checkOutDate, room, adult
        );

        driverTools.getWithDynamicTimeout(deeplink, 60);
        driverTools.waitForElementsToAppear(HOTEL_NAME);
    }

    public void clickUserAvatarAndVerifyTierIsNotPremium() {
        driverTools.waitForElementToAppear(USER_AVATAR_BUTTON, 10).orElseThrow(() -> new NoSuchElementException("User avatar button ('AA') not found"));
        // Re-locate on scroll and click (Locator-based) so a DOM re-render doesn't cause a StaleElementReferenceException.
        driverTools.scrollElementToCenter(USER_AVATAR_BUTTON);
        driverTools.click(USER_AVATAR_BUTTON);
        WebElement tierElement = driverTools.waitForElementToAppear(USER_LOYALTY_TIER_TEXT, 5).orElseThrow(() -> new NoSuchElementException("User loyalty tier text element not found inside modal"));
        String actualTier = tierElement.getText().trim();
        Assertion.assertThat("Verify user tier is a non-premium tier (Should NOT be Gold, Platinum, or Diamond). Current Tier is: "+actualTier, actualTier, not(anyOf(is("Gold"), is("Platinum"), is("Diamond"))));
    }

    public void verifyPremiumBenefitsNotDisplayedForNonPremiumAccount() {
        List<WebElement> tierBadges = driverTools.elements(PREMIUM_TIER_BADGE).orElseThrow();
        Assertion.assertThat("Verify premium tier badge layout does not render for non-premium tier", tierBadges.isEmpty(), is(true));
        List<WebElement> discountRows = driverTools.elements(PREMIUM_DISCOUNT_TEXT_ROW).orElseThrow();
        Assertion.assertThat("Verify additional structural discount labels are absent from the room details card", discountRows.isEmpty(), is(true));
    }


    public void selectDateAfterTenDays() {
        WebElement searchTab = driverTools.waitForElementToAppear(SEARCH_BAR_DATE_TAB, 10).orElseThrow(() -> new NoSuchElementException("Search date selector tab not found"));
        driverTools.scrollElementToCenter(searchTab);
        driverTools.clickWithJavascriptExecutor(searchTab);
        String checkInBaseDate = CoreFunctions.getJakartaDateYYYY_MM_DD(10);
        String checkOutBaseDate = CoreFunctions.getJakartaDateYYYY_MM_DD(11);
        String targetCheckInLabel = CoreFunctions.convertDateFormat(checkInBaseDate, "yyyy-MM-dd", "d MMMM yyyy EEEE");
        String targetCheckOutLabel = CoreFunctions.convertDateFormat(checkOutBaseDate, "yyyy-MM-dd", "d MMMM yyyy EEEE");

        Locator checkInDayLocator = new Locator("DYNAMIC_CHECK_IN_DAY", By.xpath("//button[contains(@class, 'Day_day')]//span[@aria-label='" + targetCheckInLabel + "']"));
        Locator checkOutDayLocator = new Locator("DYNAMIC_CHECK_OUT_DAY", By.xpath("//button[contains(@class, 'Day_day')]//span[@aria-label='" + targetCheckOutLabel + "']"));
        WebElement checkInElement = driverTools.waitForElementToAppear(checkInDayLocator, 5).orElseThrow(() -> new NoSuchElementException("Check-in date not visible in calendar: " + targetCheckInLabel));
        driverTools.scrollElementToCenter(checkInElement);
        driverTools.clickWithJavascriptExecutor(checkInElement);

        WebElement checkOutElement = driverTools.waitForElementToAppear(checkOutDayLocator, 5).orElseThrow(() -> new NoSuchElementException("Check-out date not visible in calendar: " + targetCheckOutLabel));
        driverTools.clickWithJavascriptExecutor(checkOutElement);
        WebElement closeBtn = driverTools.waitForElementToAppear(MODAL_CLOSE_BUTTON, 5).orElseThrow(() -> new NoSuchElementException("Modal close button context not found"));
        driverTools.clickWithJavascriptExecutor(closeBtn);

        String expectedCheckInUI = CoreFunctions.convertDateFormat(checkInBaseDate, "yyyy-MM-dd", "EEE, dd MMM yyyy");
        String expectedCheckOutUI = CoreFunctions.convertDateFormat(checkOutBaseDate, "yyyy-MM-dd", "EEE, dd MMM yyyy");
        WebElement searchDateTextElement = driverTools.waitForElementToAppear(SEARCH_BAR_DATE_TEXT, 5).orElseThrow(() -> new NoSuchElementException("Search bar date text span not found after update"));
        String actualSearchTabText = searchDateTextElement.getText();
        CoreFunctions.log("Validating search bar UI updated to: " + expectedCheckInUI + " - " + expectedCheckOutUI);
        Assertion.assertThat("Verify the search bar displays the selected check-in date", actualSearchTabText, containsString(expectedCheckInUI));
        Assertion.assertThat("Verify the search bar displays the selected check-out date", actualSearchTabText, containsString(expectedCheckOutUI));
    }
    public void clickRoomsTab() {
        WebElement roomsTab = driverTools.waitForElementToAppear(ROOMS_TAB, 10).orElseThrow(() -> new NoSuchElementException("Rooms tab not found on PDP"));
        driverTools.clickWithJavascriptExecutor(roomsTab);
    }
    public void clickFirstRoomCard() {
        WebElement firstRoomCard = driverTools.waitForElementToAppear(FIRST_ROOM_CARD, 10).orElseThrow(() -> new NoSuchElementException("First room card not found"));
        driverTools.clickWithJavascriptExecutor(firstRoomCard);
    }public boolean scrollToAndVerifyRoomDetailSection(String sectionName) {
        String sectionHeading = sectionName.equals("Benefits")
                ? "Special For You"
                : sectionName;

        String localizedTabName = switch (sectionName) {
            case "Popular Facilities" -> "Fasilitas Populer";
            case "Check-in Procedures" -> "Prosedur Check-in";
            case "Room Description" -> "Deskripsi Kamar";
            case "Other Facilities" -> "Fasilitas Lain";
            default -> sectionName;
        };

        Locator section = new Locator(
                "ROOM_DETAIL_SECTION_" + sectionName,
                new By.ByXPath(
                        "//h3[normalize-space()='" + sectionHeading + "']"));

        Locator activeTab = new Locator(
                "ACTIVE_ROOM_DETAIL_TAB_" + sectionName,
                new By.ByXPath(
                        "//span[contains(@class, 'Text_variant_activity') "
                                + "and (normalize-space()='" + sectionName + "' "
                                + "or normalize-space()='" + localizedTabName + "')]"));

        driverTools.scrollIntoWebView(section);

        try {
            driverTools.scroll(1, 100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return driverTools.waitForElementToAppear(activeTab, 5).isPresent();
    }
}