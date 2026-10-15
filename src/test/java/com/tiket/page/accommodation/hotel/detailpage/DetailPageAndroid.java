package com.tiket.page.accommodation.hotel.detailpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.bookingformpage.BookingFormPage;
import com.tiket.page.home.HomePageAndroid;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tiket.core.CoreFunctions.normalizeNonBreakingSpaceText;
import static com.tiket.page.setting.SettingPageAndroid.SNACKBAR_TEXT;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class DetailPageAndroid <T extends WebDriver> extends DetailPage<T> {

    public DetailPageAndroid(Session<T> session) { super(session); }

    public static final Locator HOTEL_IMAGE = new Locator("HOTEL_IMAGE", AppiumBy.id("rv_hotel_image_detail"));
    public static final Locator HOTEL_GALLERY_LOCATOR = new Locator("HOTEL_GALLERY_LOCATOR", AppiumBy.id("iv_gallery"));
    public static final Locator HOTEL_CATEGORY = new Locator("HOTEL_CATEGORY", AppiumBy.id("tv_category"));
    public static final Locator HOTEL_STAR = new Locator("HOTEL_STAR", AppiumBy.id("rb_hotel_star"));
    public static final Locator HOTEL_LOCATION = new Locator("HOTEL_LOCATION", AppiumBy.id("tv_location"));
    public static final Locator FOOTER_BUTTON = new Locator("FOOTER_BUTTON", AppiumBy.id("cl_footer_container"));
    public static final Locator HOTEL_REVIEW = new Locator("HOTEL_REVIEW", AppiumBy.id("tv_reviews"));
    public static final Locator HOTEL_PRICE = new Locator("HOTEL_PRICE", AppiumBy.xpath("//*[contains(@resource-id, 'price_module_final_price') or contains(@resource-id,'tv_main_price')]"));
    public static final Locator HOTEL_LOYALTY_BENEFIT_MARK = new Locator("HOTEL_LOYALTY_BENEFIT_MARK", AppiumBy.id("-"));
    public static final Locator SEE_HOTEL_ROOM_BUTTON_V4 = new Locator("SEE_HOTEL_ROOM_BUTTON_V4", AppiumBy.xpath("//*[contains(@resource-id, 'cl_footer_container')]//*[contains(@resource-id, 'btn_continue')]//*[contains(@resource-id, 'tds_button_view') and (contains(@text,'See') or contains(@text,'Lihat'))]"));
    public static final Locator INFO_BENEFIT_POPUP = new Locator("INFO_BENEFIT_POPUP", AppiumBy.id("tds_link_coach_mark"));
    private static final Locator INFO_OPTION_ROOM_POPUP = new Locator("INFO_OPTION_ROOM_POPUP", AppiumBy.id("tds_primary_coach_mark"));
    public static final Locator ROOM_CARDS = new Locator("ROOM_CARDS", AppiumBy.xpath("(//*[contains(@resource-id, 'card_room')])[1]"));
    public static final Locator FIRST_ROOM_CARD = new Locator("FIRST_ROOM_CARD", AppiumBy.xpath("//*[contains(@resource-id, 'rv_room')]//*[contains(@resource-id, 'card_room')]//*[contains(@resource-id, 'tv_room_name')]"));
    private static final Locator BOOK_BUTTON = new Locator("BOOK_BUTTON", AppiumBy.xpath("(//*[contains(@resource-id,'vg_price') or contains(@resource-id,'vg_rate_section')]//*[contains(@resource-id,'btn_book')])[last()]"));
    private static final Locator BOOK_BUTTON_100P_REFUND = new Locator("BOOK_BUTTON_100P_REFUND", AppiumBy.xpath("//*[contains(@resource-id,'tv_cancellation_policy') and contains(@text,'100% Refund')]"));
    public static final Locator BOOK_BUTTON_MULTI_ROOM = new Locator("BOOK_BUTTON_MULTI_ROOM", AppiumBy.xpath("//*[contains(@resource-id,'price_footer')]//*[contains(@resource-id,'btn_book')]"));
    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.id("appbar_back_icon"));
    private static final Locator ROOM_BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.id("tds_icon_navigation_toolbar"));
    public static final Locator ROOM_NAME = new Locator("ROOM_NAME", AppiumBy.id("tv_room_name"));
    public static final Locator FIRST_ROOM_DETAIL = new Locator("FIRST_ROOM_DETAIL", AppiumBy.id("tv_room_name"));
    public static final Locator ROOM_DETAIL_SECTION_REFUND_AND_RESCHEDULE = new Locator("ROOM_DETAIL_SECTION_REFUND_AND_RESCHEDULE", AppiumBy.xpath("//*[contains(@resource-id, 'tv_policy') and (contains(@text,'Refund') or contains(@text,'Cancellation') or contains(@text,'Pembatalan'))]"));
    public static final Locator FREE_CANCELLATION = new Locator("FREE_CANCELLATION", AppiumBy.xpath("//*[contains(@resource-id, 'tv_cancellation') and (contains(@text, 'gratis') or contains(@text, 'Free') or contains(@text,'100%'))]"));
    public static final Locator CANCELLATION_FEE = new Locator("CANCELLATION_FEE", AppiumBy.xpath("//*[contains(@resource-id, 'tv_description')  and (contains(@text, 'Biaya') or contains(@text,'fee'))]"));
    public static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", AppiumBy.id("tv_name"));
    public static final Locator LOGIN_BUTTON = new Locator("LOGIN", AppiumBy.xpath("//*[contains(@resource-id,'right_button')]//*[contains(@resource-id,'tds_button_view')]"));
    private static final Locator HOMES_NAME = new Locator("HOMES_NAME", AppiumBy.id("tv_hotel_name"));
    public static final Locator EXPAND_PRICE_DETAIL = new Locator("EXPAND_PRICE_DETAIL", AppiumBy.xpath("(//*[contains(@resource-id,'v_price_module')]//*[contains(@resource-id,'iv_icon')])[last()]"));
    public static final Locator TOTAL_PRICE_ON_PRICE_DETAIL = new Locator("TOTAL_PRICE_ON_PRICE_DETAIL", AppiumBy.id("tv_right_bot"));
    private static final Locator PRICE_BREAKDOWN =  new Locator ("PRICE_BREAKDOWN" , AppiumBy.xpath("//*[contains(@resource-id,'tv_right') and not(contains(@resource-id,'bot'))]"));
    private static final Locator LOCATION_TAB = new Locator("LOCATION_TAB", AppiumBy.xpath("//android.widget.LinearLayout[@content-desc='Location' or @content-desc='Lokasi']"));
    private static final Locator SEE_MAPS_BUTTON = new Locator("SEE_MAPS_BUTTON", AppiumBy.id("tv_see_map"));
    private static final Locator MAPS_SKIP_BUTTON = new Locator("MAPS_SKIP_BUTTON", AppiumBy.xpath("//android.widget.Button[@text='SKIP' or @text='Skip']"));
    public static final Locator MAPS_VIEW = new Locator("MAPS_VIEW", AppiumBy.id("com.google.android.apps.maps:id/map_frame"));
    public static final Locator HOTEL_REVIEW_BUTTON = new Locator("HOTEL_REVIEW_BUTTON", AppiumBy.xpath("//*[contains(@resource-id, 'tds_title_tab_layout') and (@text='Review')]"));
    public static final Locator REVIEW_RATING= new Locator("REVIEW_RATING", AppiumBy.id("tv_rating"));
    public static final Locator SEE_MORE_REVIEW_BUTTON= new Locator("SEE_MORE_REVIEW_BUTTON", AppiumBy.xpath("(//*[contains(@resource-id, 'tv_action') and (normalize-space(contains(@text,'See all')) or normalize-space(contains(@text, 'Lihat semua')))])[1]"));
    public static final Locator REVIEW_IMPRESSION= new Locator("REVIEW_IMPRESSION", AppiumBy.id("tv_impression"));
    public static final Locator REVIEW_COUNT= new Locator("REVIEW_COUNT", AppiumBy.xpath("//*[contains(@resource-id, 'tv_review_count')]"));
    public static final Locator REVIEW_RATING_REVIEW_PAGE= new Locator("REVIEW_RATING_REVIEW_PAGE", AppiumBy.xpath("(//*[contains(@resource-id, 'tv_rating')])[1]"));
    public static final Locator REVIEW_IMPRESSION_REVIEW_PAGE= new Locator("REVIEW_IMPRESSION_REVIEW_PAGE", AppiumBy.xpath("//*[contains(@resource-id, 'tv_rating_category')]"));
    public static final Locator REVIEW_COUNT_REVIEW_PAGE= new Locator("REVIEW_COUNT_REVIEW_PAGE", AppiumBy.xpath("//*[contains(@resource-id, 'tv_total_review')]"));
    public static final Locator TEXT_OF_RATING= new Locator("TEXT_OF_RATING", AppiumBy.id("text_of_rating"));
    public static final Locator TEXT_OF_RATING_REVIEW_PAGE= new Locator("TEXT_OF_RATING_REVIEW_PAGE", AppiumBy.xpath("//*[contains(@resource-id, 'tv_rating_total')]"));
    public static final Locator REVIEW_PHOTO_COUNT= new Locator("REVIEW_PHOTO_COUNT", AppiumBy.id("tv_image_count"));
    public static final Locator REVIEW_PHOTO_COUNT_PARENT = new Locator("REVIEW_PHOTO_COUNT_PARENT", AppiumBy.id("rv_item"));
    public static final Locator LOYALTY_BENEFIT_SECTION= new Locator("LOYALTY_BENEFIT_SECTION", AppiumBy.id("tv_loyalty"));
    public static final Locator PREFERRED_PARTNER_INDICATOR_PDP = new Locator("PREFERRED_PARTNER_INDICATOR_PDP",
            AppiumBy.xpath("//*[@content-desc='preferred_partner' or @content-desc='preferred_partner_plus']"
                    + " | //*[contains(@class,'TextView') and (contains(@text,'Preferred Partner') or contains(@text,'preferred partner'))]"));
    public static final Locator LOYALTY_BENEFIT_ARROW= new Locator("LOYALTY_BENEFIT_ARROW", AppiumBy.id("iv_arrow"));
    public static final Locator CLOSE_POPUP_BUTTON= new Locator("CLOSE_POPUP_BUTTON", AppiumBy.id("iv_close"));
    public static final Locator INCREMENT_BUTTON = new Locator("INCREMENT_BUTTON", AppiumBy.id("btn_increment"));
    public static final Locator SELECT_BUTTON_MO = new Locator("SELECT_BUTTON_MO", AppiumBy.xpath("//*[contains(@resource-id, 'tds_btn') and contains(@text, 'rooms') or contains(@text,'kamar')]"));
    private static final Locator CONTAINER_TAB = new Locator("", AppiumBy.id("tab_layout_tab"));
    private static final Locator ACCOMMODATION_RULES_TAB = new Locator("ACCOMMODATION_RULES_TAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tab_layout') and (@text='Accommodation Rules' or @text='Kebijakan Akomodasi')]"));
    private static final Locator POPULAR_FACILITIES_TAB = new Locator("POPULAR_FACILITIES_TAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tab_layout') and (@text='Popular Facilities' or @text='Fasilitas Populer')]"));
    private static final Locator SEE_MORE_POPULAR_FACILITIES = new Locator("SEE_MORE_POPULAR_FACILITIES", AppiumBy.xpath("//*[contains(@resource-id, 'tv_title') and contains(@text,'Popular Facilities') or contains(@text, 'Fasilitas Populer')]//following-sibling::*[contains(@resource-id, 'tv_action') and contains(@text,'See all') or contains(@text, 'Lihat semua')]"));
    private static final Locator LIST_FACILITIES = new Locator("LIST_FACILITIES", AppiumBy.id("tds_subtitle_list"));
    private static final Locator POLICY_ACCOMMODATION = new Locator("POLICY_ACCOMMODATION", AppiumBy.id("tv_policy"));
    private static final Locator SEE_MORE_ACCOMMODATION_RULES = new Locator("SEE_MORE_ACCOMMODATION_RULES", AppiumBy.xpath("//*[contains(@resource-id, 'tv_title') and contains(@text,'Accommodation Rules') or contains(@text, 'Kebijakan Akomodasi')]//following-sibling::*[contains(@resource-id, 'tv_action') and contains(@text,'See all') or contains(@text, 'Lihat semua') or contains(@text,'See more')]"));
    private static final Locator ACCOMMODATION_RULES_POPUP = new Locator("ACCOMMODATION_RULES_POPUP", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'Accommodation Rules') or contains(@text, 'Kebijakan Akomodasi')]//following-sibling::*[contains(@resource-id,'container_content')]"));
    private static final Locator SEE_ALL_REVIEW = new Locator("SEE_ALL_REVIEW", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Review') or contains(@text,'Review'))]//following-sibling::*[contains(@resource-id,'tv_action')]"));
    private static final Locator SEE_ALL_REVIEW_BOTTOM_SHEET = new Locator("SEE_ALL_REVIEW", AppiumBy.id("btn_all_review"));
    public static final Locator REVIEW_BY_USER_SECTION = new Locator("REVIEW_BY_USER", AppiumBy.id("tv_user_rating"));
    public static final Locator LOCATION_SECTION = new Locator("LOCATION_SECTION", AppiumBy.id("tv_hotel_location_header"));
    public static final Locator LOCATION_TAB_BUTTON = new Locator("LOCATION_TAB_BUTTON", AppiumBy.xpath("//*[contains(@resource-id, 'tds_title_tab_layout') and (@text='Location' or @text='Lokasi')]"));
    public static final Locator ABOUT_ACCOMMODATION_CONTENT = new Locator("ABOUT_ACCOMMODATION_CONTENT", AppiumBy.id("tv_info"));
    public static final Locator ABOUT_ACCOMMODATION_TAB_BUTTON = new Locator("ABOUT_ACCOMMODATION_TAB_BUTTON", AppiumBy.xpath("//*[contains(@resource-id, 'tds_title_tab_layout') and (@text='About Accommodation' or @text='Tentang Akomodasi')]"));
    /** PDP location-section map preview (click to open full-screen in-app map). */
    public static final Locator MAPS_CONTAINER = new Locator("MAPS_CONTAINER", AppiumBy.id("iv_hotel_location_map"));
    /** Full-screen in-app map after "See maps" (not an external map app). */
    private static final Locator FULL_SCREEN_MAP = new Locator("FULL_SCREEN_MAP", AppiumBy.id("fl_map"));
    public static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.xpath("(//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Book') or contains(@text,'book') or contains(@text,'Pesan') or contains(@text,'pesan'))])[1]"));
    public static final Locator REVIEW_CAROUSEL = new Locator("REVIEW_CAROUSEL", AppiumBy.id("rv_item"));
    public static final Locator VISIBLE_REVIEW_CAROUSEL = new Locator("VISIBLE_REVIEW_CAROUSEL", AppiumBy.id("tv_comment"));
    public static final Locator REVIEW_PHOTO_BOTTOM_SHEET_PARENT= new Locator("REVIEW_PHOTO_PARENT", AppiumBy.id("rv_header"));
    private static final Locator HOTEL_NAME_POI = new Locator("HOTEL_NAME_POI", AppiumBy.id("tv_hotel_name"));
    private static final Locator PER_NIGHT_TEXT = new Locator("PER_NIGHT_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'price_module_description') or contains(@resource-id,'tv_unit_2')]"));
    public static final Locator TOTAL_PRICE_LABEL = new Locator("TOTAL_PRICE_LABEL", AppiumBy.xpath("//*[(contains(@resource-id,'price_module_description') or contains(@resource-id,'tv_unit_2')) and (@text='Total harga' or @text='Total price')]"));
    public static final Locator LABEL_PER_ROOM_PER_NIGHT = new Locator("LABEL_PER_ROOM_PER_NIGHT", AppiumBy.xpath("(//*[contains(@resource-id,'price_module_description') or contains(@resource-id,'tv_unit_2')])[1]"));
    public static final Locator HEART_ICON = new Locator("HEART_ICON", AppiumBy.xpath("//*[@class='android.widget.ImageView' and ./parent::*[@content-desc='wishlist']]"));
    public static final Locator HEART_ICON_RED = new Locator("HEART_ICON_RED", AppiumBy.xpath("//*[@class='android.widget.ImageView' and ./parent::*[@content-desc='wishlist (selected)']]"));
    public static final Locator MANAGE_WISHLIST = new Locator("MANAGE_WISHLIST", AppiumBy.id("tds_snackbar_action"));

    // ====== In-app map & nearby chips ======
    private static final Locator IN_APP_MAP_POPUP = new Locator("IN_APP_MAP_POPUP", AppiumBy.xpath("//*[contains(@resource-id,'fl_map')]//*[contains(@content-desc,'Google')]"));
    private static final Locator NEARBY_BUTTON = new Locator("NEARBY_BUTTON", AppiumBy.id("tds_extended_fab_text"));
    private static final Locator NEARBY_ACCOMMODATION_LIST_CONTAINER = new Locator("NEARBY_ACCOMMODATION_LIST_CONTAINER", AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_name')]//parent::*[contains(@resource-id,'cl_main_content')]"));
    private static final Locator NEARBY_CHIPS = new Locator("NEARBY_CHIPS", AppiumBy.id("tds_chips_text"));
    private static final Locator NEARBY_ACCOMMODATION_CROSS_SELL_NUDGE = new Locator("NEARBY_ACCOMMODATION_CROSS_SELL_NUDGE", AppiumBy.id("v_cross_sell_nudges"));
    private static final Locator NEARBY_ACCOMMODATION_HOTEL_NAME_WITH_CROSS_SELL = new Locator("NEARBY_ACCOMMODATION_HOTEL_NAME_WITH_CROSS_SELL", AppiumBy.xpath("//*[contains(@resource-id,'v_cross_sell_nudges')]/parent::*/preceding-sibling::*/*[contains(@resource-id,'tv_hotel_name')]"));

    // ====== Infinity promo cards on PDP ======
    private static final Locator INFINITY_PROMO_SECTION = new Locator("INFINITY_PROMO_SECTION", AppiumBy.id("rv_promo"));
    private static final Locator INFINITY_PROMO_CARDS = new Locator("INFINITY_PROMO_CARDS", AppiumBy.id("gvc_voucher"));
    private static final Locator INFINITY_PROMO_COPY_BTN = new Locator("INFINITY_PROMO_COPY_BTN", AppiumBy.id("iv_copy"));
    private static final Locator INFINITY_PROMO_CODE = new Locator("INFINITY_PROMO_CODE", AppiumBy.id("tv_promo_code"));
    private static final Locator SEE_ALL_PROMO_BTN = new Locator("SEE_ALL_PROMO_BTN", AppiumBy.id("iv_see_all"));
    private static final Locator SEE_ALL_PROMO_SECTION = new Locator("SEE_ALL_PROMO_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'rv_promo_list') or contains(@resource-id,'bottom_sheet_promo') or contains(@resource-id,'vg_promo_list')]"));
    private static final Locator SEE_ALL_PROMO_COPY_BTN = new Locator("SEE_ALL_PROMO_COPY_BTN", AppiumBy.id("btn_action"));
    private static final Locator SEE_ALL_PROMO_CODE = new Locator("SEE_ALL_PROMO_CODE", AppiumBy.id("tv_promo_code"));

    public static final Locator YES_CONTINUE_MEET_POLICY_BTN = new Locator("YES_CONTINUE_MEET_POLICY_BTN", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Ya,') or contains(@text,'Yes,'))]"));
    public static final Locator ITEM_REMOVED_FROM_WISHLIST_POPUP = new Locator("ITEM_REMOVED_FROM_WISHLIST_POPUP", AppiumBy.id("tds_snackbar_text"));
    public static final Locator SAVE_BUTTON_MULTI_ROOM = new Locator("SAVE_BUTTON_MULTI_ROOM",AppiumBy.id("btn_save"));
    public static final Locator CASHBACK_AMOUNT = new Locator("CASHBACK_AMOUNT",AppiumBy.id("tv_tix_point"));

    public String getHotelNameDetail() {
        if (driverTools.waitForElementToPresence(HOTEL_NAME).isPresent()) {
            return driverTools.getText(HOTEL_NAME).orElseThrow();
        } else {
            return driverTools.getText(HOMES_NAME).orElseThrow();
        }
    }

    public String getHotelName() {
        ///implementation pending
        return null;
    }

    public String getHotelLocationDetail() {
        return driverTools.getText(HOTEL_LOCATION).orElseThrow();
    }

    @Override
    public String checkPolicyAccommodation() {
        driverTools.swipeUp(2);
        driverTools.swipeToElement(ACCOMMODATION_RULES_TAB, CONTAINER_TAB, Swipe.LEFT, 10).orElseThrow();
        driverTools.click(ACCOMMODATION_RULES_TAB);
        driverTools.swipeToElement(POLICY_ACCOMMODATION, Swipe.UP, 5);
        return driverTools.getText(POLICY_ACCOMMODATION).orElseThrow();
    }

    public void goToFacilitiesSection() {
        driverTools.swipeUp(1);
        driverTools.swipeToElement(POPULAR_FACILITIES_TAB, CONTAINER_TAB, Swipe.LEFT, 10).orElseThrow();
        driverTools.click(POPULAR_FACILITIES_TAB);
    }

    public void openFacilitiesPage() {
        driverTools.swipeToElement(SEE_MORE_POPULAR_FACILITIES, Swipe.DOWN, 1);
        driverTools.click(SEE_MORE_POPULAR_FACILITIES);
    }

    public List<String> getListFacilities() {
        driverTools.waitForElementToAppear(LIST_FACILITIES);
        List<WebElement> facilities = driverTools.elements(LIST_FACILITIES).orElseThrow();
        List<String> listFacilities = new ArrayList<>();
        for (WebElement element : facilities) {
            listFacilities.add(driverTools.getText(element));
            logger.log("listFacilities -> " + listFacilities);
        }
        return listFacilities;
    }

    @Override
    public void openAccommodationRules() {
        driverTools.swipeToElement(SEE_MORE_ACCOMMODATION_RULES, Swipe.DOWN, 1);
        driverTools.click(SEE_MORE_ACCOMMODATION_RULES);
    }

    @Override
    public boolean isAccommodationRulesOpen() {
        return driverTools.waitForElementsToAppear(ACCOMMODATION_RULES_POPUP).isPresent();
    }

    @Override
    public void closePdpDrawer() {
        driverTools.click(CLOSE_POPUP_BUTTON);
    }

    @Override
    public boolean isHotelDetailShown() {
        Assertion.assertThat("HOTEL_IMAGE should be present", driverTools.waitForElementToAppear(HOTEL_IMAGE).isPresent(), is(true));
        Assertion.assertThat("HOTEL_CATEGORY should be present", driverTools.waitForElementToAppear(HOTEL_CATEGORY).isPresent(), is(true));
        Assertion.assertThat("HOTEL_NAME should be present", driverTools.waitForElementToAppear(HOTEL_NAME).isPresent(), is(true));
        Assertion.assertThat("HOTEL_STAR should be present", driverTools.waitForElementToAppear(HOTEL_STAR).isPresent(), is(true));
        Assertion.assertThat("HOTEL_LOCATION should be present", driverTools.waitForElementToAppear(HOTEL_LOCATION).isPresent(), is(true));
        Assertion.assertThat("FOOTER_BUTTON should be present", driverTools.waitForElementToAppear(FOOTER_BUTTON).isPresent(), is(true));

        return true;
    }

    public boolean isHaveHotelImage() {
        return driverTools.isElementDisplayed(HOTEL_IMAGE);
    }

    public String getHotelReview() {
        return driverTools.getText(HOTEL_REVIEW).orElseThrow();
    }

    public boolean getHotelStar() {
        // for now we are unable to get how much star it has
        return driverTools.isElementDisplayed(HOTEL_STAR);
    }

    public String getHotelPrice() {
        driverTools.waitForElementToAppear(HOTEL_PRICE).orElseThrow();
        return driverTools.getText(HOTEL_PRICE).orElseThrow();
    }

    public void seeHotelRoom() {
        driverTools.waitForElementToAppear(HOTEL_NAME);
        driverTools.waitForElementToPresence(SEE_HOTEL_ROOM_BUTTON_V4,20).orElseThrow();
        driverTools.click(SEE_HOTEL_ROOM_BUTTON_V4);
    }

    @Override
    public boolean isRoomAvailable() {
        // Must match seeHotelRoom(): only See/Lihat footer counts as "available".
        // Book/Pesan (CONTINUE_BUTTON) is not navigable via seeHotelRoom and would select a PDP
        // that then fails when the PAH test opens the room list.
        return driverTools.waitForElementToPresence(SEE_HOTEL_ROOM_BUTTON_V4).isPresent();
    }

    public void RemovePopUp() {
        if (driverTools.isElementDisplayed(INFO_BENEFIT_POPUP)) {
            driverTools.click(INFO_BENEFIT_POPUP);
        }
        if (driverTools.isElementDisplayed(INFO_OPTION_ROOM_POPUP)) {
            driverTools.click(INFO_OPTION_ROOM_POPUP);
            driverTools.click(ROOM_BACK_BUTTON);
            seeHotelRoom();
        }
    }

    public boolean isRoomDetailShown() {
        return driverTools.isElementDisplayed(ROOM_CARDS);
    }

    public void clickBookButton(int totalRoom) {
        driverTools.waitForElementToAppear(ROOM_NAME);
        WebElement webElement = driverTools.swipeToElement(BOOK_BUTTON, Swipe.UP, 2).orElseThrow();
        driverTools.click(webElement);
        driverTools.waitForElementToDisappear(SNACKBAR_TEXT);
        if (totalRoom > 1) driverTools.swipeToElement(INCREMENT_BUTTON, Swipe.UP, 1);
        if (driverTools.isElementDisplayed(INCREMENT_BUTTON)) {
            driverTools.swipeToElement(INCREMENT_BUTTON, Swipe.UP, 2);
            for(int i = 1; i < totalRoom; i++) {
                driverTools.click(INCREMENT_BUTTON);
            }
            driverTools.waitForElementToAppear(SAVE_BUTTON_MULTI_ROOM, 3).ifPresent(driverTools::click);
            driverTools.click(BOOK_BUTTON_MULTI_ROOM);
        }
        if(driverTools.isElementDisplayed(CONTINUE_BUTTON)){
            driverTools.click(CONTINUE_BUTTON);
        }
    }

    public void clickBookButton100pRefund(int totalRoom) {
        driverTools.waitForElementToAppear(ROOM_NAME);
        driverTools.swipeToElement(BOOK_BUTTON_100P_REFUND, Swipe.UP, 15).orElseThrow();
        driverTools.swipeElementToMidScreen(BOOK_BUTTON_100P_REFUND);
        driverTools.click(BOOK_BUTTON_100P_REFUND);
        driverTools.waitForElementToDisappear(SNACKBAR_TEXT);
        if (totalRoom > 1) driverTools.swipeToElement(INCREMENT_BUTTON, Swipe.UP, 1);
        if (driverTools.isElementDisplayed(INCREMENT_BUTTON)) {
            driverTools.swipeToElement(INCREMENT_BUTTON, Swipe.UP, 2);
            for(int i = 1; i < totalRoom; i++) {
                driverTools.click(INCREMENT_BUTTON);
            }
            driverTools.click(BOOK_BUTTON_MULTI_ROOM);
        }
        driverTools.click(CONTINUE_BUTTON);
    }

    public void clickBackButton() {
        WebElement webElement = driverTools.waitForElementToAppear(BACK_BUTTON).orElseThrow();
        driverTools.click(webElement);
    }

    @Override
    public String getRefundPolicyValue() {
        driverTools.swipeToElement(ROOM_DETAIL_SECTION_REFUND_AND_RESCHEDULE, Swipe.UP, 5);
        String value;
        Optional<WebElement> freeCancellationElement = driverTools.swipeToElement(FREE_CANCELLATION,Swipe.UP, 2);
        if (freeCancellationElement.isPresent()) {
            value = "IDR 0";
        } else {
            value = driverTools.getText(CANCELLATION_FEE).orElseThrow();
            Pattern pattern = Pattern.compile("([A-Z]{3,}\\s[\\d,]+)");
            Matcher matcher = pattern.matcher(value);

            if (matcher.find()) {
                value = matcher.group(1); //works for IDR 621,000 or USD 500, or MYR 1,200
            }
        }
        logger.log("Refund policy value: " + value);
        return value;
    }

    @Override
    public void openPriceDetail() {
        WebElement element = driverTools.waitForElementToPresence(EXPAND_PRICE_DETAIL).orElseThrow();
        driverTools.click(element);
        if (driverTools.isElementDisplayed(EXPAND_PRICE_DETAIL))
            driverTools.clickElementByCoordinate(EXPAND_PRICE_DETAIL);
        Assertion.assertThat("Price detail should be opened", driverTools.isElementDisplayed(EXPAND_PRICE_DETAIL), is(false));
    }

    @Override
    public void closePriceDetail() {
        driverTools.click(CLOSE_POPUP_BUTTON);
    }

    @Override
    public boolean priceMatch(Double price, int totalRoom) {
        double totalPriceOnPriceDetail = CoreFunctions.convertPrice(driverTools.getText(TOTAL_PRICE_ON_PRICE_DETAIL).orElseThrow());
        logger.log("price=" + price+ "\nprice * " + totalRoom + " = " + price * totalRoom);
        logger.log("price on price detail=" + totalPriceOnPriceDetail);
        Assertion.assertThat("Summary of the total price on the room details should be correct", totalPriceOnPriceDetail, greaterThanOrEqualTo(price * totalRoom));
        return true;
    }

    @Override
    public boolean checkPriceBreakdown(Double price, int totalRoom) {
        driverTools.waitForElementToPresence(PRICE_BREAKDOWN);
        List<WebElement> prices = driverTools.elements(PRICE_BREAKDOWN).orElseThrow();
        ArrayList<Double> arrayPrice = new ArrayList<Double>();
        for (WebElement element : prices) {
            String text = driverTools.getText(element);
            try {
                double priceBreak = CoreFunctions.convertPrice(text);
                arrayPrice.add(priceBreak);
            } catch (NumberFormatException message) {
                logger.log(message.getMessage());
            }
        }
        double sum = 0;
        for (double i : arrayPrice) {
            sum += i;
        }
        logger.log("Total price text:  " + price * totalRoom);
        logger.log("Total price got:  " + sum);
        Assertion.assertThat("Summary of the total price on the price detail should be correct", sum, greaterThanOrEqualTo(price * totalRoom));
        return true;
    }

    @Override
    public void clickLocationTab() {
        WebElement locationTab = driverTools.swipeToElement(LOCATION_TAB, Swipe.UP, 6).orElseThrow();
        driverTools.click(locationTab);
    }

    @Override
    public void clickSeeMapsButton() {
        WebElement seeMapsButton = driverTools.swipeToElement(SEE_MAPS_BUTTON, Swipe.UP, 3).orElseThrow();
        driverTools.click(seeMapsButton);
    }

    @Override
    public boolean isNativeMapsShown() {
        // App opens in-app Google Map (fl_map), not an external map app.
        return driverTools.waitForElementToAppear(FULL_SCREEN_MAP, 20).isPresent()
                || driverTools.waitForElementToAppear(IN_APP_MAP_POPUP, 5).isPresent();
    }

    @Override
    public String getHotelNamePoi() {
        if (driverTools.waitForElementToAppear(HOTEL_NAME_POI, 10).isPresent()) {
            return driverTools.getText(HOTEL_NAME_POI).orElse("");
        }
        return "";
    }

    @Override
    public boolean verifyRedirectToMaps() {
        boolean status;
        if (driverTools.waitForElementToAppear(MAPS_SKIP_BUTTON).isPresent()) {
            driverTools.click(MAPS_SKIP_BUTTON);
        }
        if (driverTools.waitForElementToAppear(MAPS_VIEW).isPresent()) {
            status = true;
        } else {
            status = false;
        }
        driverTools.back();
        if (driverTools.isElementDisplayed(MAPS_VIEW)) driverTools.back();
        return status;
    }

    @Override
    public  void openReviewSection(){
        WebElement hotelReviewSection = driverTools.swipeToElement(HOTEL_REVIEW_BUTTON, Swipe.UP,5).orElseThrow();
        driverTools.click(hotelReviewSection);
    }

    @Override
    public boolean checkHotelReview() {
        String  []getReviewCardValue = getReviewCardValues();
        String  []getReviewPageValue = getReviewPageValues();

        for(int i =0; i<getReviewPageValue.length; i++) {
            String reviewCardValue = getReviewCardValue[i].replace(",", "");
            String reviewPageValue = getReviewPageValue[i].replace(",", "");
           logger.log("Array elements are equal or not "+ reviewCardValue + " "+ reviewPageValue);
            if (!(reviewCardValue.equals(reviewPageValue))) {
                return false;
            }
        }
        return true;
    }
    private String[] getReviewCardValues(){
        String  reviewCardElements[] = new String[4];
        reviewCardElements[0] =(driverTools.getText(REVIEW_RATING).orElseThrow());
        reviewCardElements[1] = driverTools.getText(REVIEW_IMPRESSION).orElseThrow();
        reviewCardElements[2]= driverTools.getText(TEXT_OF_RATING).orElseThrow();
        String reviewCount = driverTools.getText(REVIEW_COUNT).orElseThrow();
        String[] reviewCounting = reviewCount.split(" ");
        reviewCardElements[3] = reviewCounting[1];
        for ( String str: reviewCardElements)
        {
            logger.log("Review Card Elements" + str);
        }
        return reviewCardElements;
    }
    private String[] getReviewPageValues(){
        driverTools.click(SEE_MORE_REVIEW_BUTTON);
        String  getReviewPageValues[] = new String[4];
        getReviewPageValues[0] =(driverTools.getText(REVIEW_RATING_REVIEW_PAGE).orElseThrow());
        getReviewPageValues[1] = driverTools.getText(REVIEW_IMPRESSION_REVIEW_PAGE).orElseThrow();
        getReviewPageValues[2]= (driverTools.getText(TEXT_OF_RATING_REVIEW_PAGE).orElseThrow()).replace("/", "");
        String reviewCountReviewPage=(driverTools.getText(REVIEW_COUNT_REVIEW_PAGE).orElseThrow()).replace(".","");
        String[] reviewCount = reviewCountReviewPage.split(" ");
        getReviewPageValues[3] = reviewCount[1];
        for ( String str: getReviewPageValues)
        {
            logger.log("Review Page Elements" + str);
        }
        return getReviewPageValues;
    }

    @Override
    public boolean checkReviewPhotoCount() {
       String totalReviewPhoto = driverTools.getText(REVIEW_PHOTO_COUNT).orElseThrow();
       String [] totalReviewPhotoCount = totalReviewPhoto.split(" ");
       logger.log("Review Page Elements  " + totalReviewPhotoCount[0]);
       driverTools.click(REVIEW_PHOTO_COUNT);
       String totalReviewPhotoInside = driverTools.getText(REVIEW_PHOTO_COUNT).orElseThrow();
       String [] totalReviewPhotoInsideCount = totalReviewPhotoInside.split("/");

       logger.log("Review Page Elements  " + totalReviewPhotoInsideCount[0]);
       logger.log("Review Page Elements  " + totalReviewPhotoInsideCount[1]);

       return totalReviewPhotoCount[0].equals(totalReviewPhotoInsideCount[1]);
    }
    @Override
    public boolean isPreferredPartnerVisibleOnPdp() {
        return driverTools.swipeToElement(PREFERRED_PARTNER_INDICATOR_PDP, Swipe.UP, 10).isPresent();
    }

    public boolean checkLoyaltyTierBenefit(){
        WebElement LoyaltyTierBenefit = driverTools.swipeToElement(LOYALTY_BENEFIT_SECTION, Swipe.UP, 10).orElseThrow();
        if(driverTools.isElementDisplayed(LoyaltyTierBenefit)){return true;}
        else
            return false;
    }
    private Locator checkLoyaltyBenefitPopUp(String tierLevel){
        return new Locator(tierLevel, AppiumBy.xpath("//*[contains(@text,'Benefit') or contains(@text,'benefits') and contains(@text,'"+tierLevel+"')]"));
    }
    @Override
    public boolean verifyLoyaltyBenefitPopUp(String tierLevel){
        driverTools.swipeUp(1);
        driverTools.swipeToElement(LOYALTY_BENEFIT_ARROW, Swipe.UP, 5).orElseThrow();
        driverTools.click(LOYALTY_BENEFIT_ARROW);
        WebElement loyaltyBenefitPopUp =  driverTools.waitForElementToAppear(checkLoyaltyBenefitPopUp(tierLevel)).orElseThrow();
        boolean  loyaltyBenefitPopUpVisble = loyaltyBenefitPopUp.isDisplayed();
        driverTools.click(CLOSE_POPUP_BUTTON);
        return loyaltyBenefitPopUpVisble;
    }

    @Override
    public boolean checkHotelName(String name){
        WebElement hotelNameElement = driverTools.waitForElementToAppear(HOTEL_NAME).orElseThrow();
        String hotelName = driverTools.getText(hotelNameElement);
        boolean isHotelNameSame = name.equalsIgnoreCase(hotelName);
        logger.log("is name from partner page is same with pdp : " + isHotelNameSame);

        return isHotelNameSame;
    }

    @Override
    public void selectRoomMultiOrder(int roomCount) {
        driverTools.swipeToElement(selectRoomMultiOrderLocator(1),Swipe.UP,15);
        if (roomCount > 1) {
            logger.log("Selecting " + roomCount + " rooms");
            driverTools.swipeUp(0.1);
            driverTools.click(selectRoomMultiOrderLocator(1));
            for (int i = 0; i < roomCount - 2; i++) {
                logger.log("Clicking increment button for room " + (i + 2));
                driverTools.click(INCREMENT_BUTTON);

            }
            driverTools.waitForElementToDisappear(selectRoomMultiOrderLocator(1),2);
            driverTools.swipeUp(0.1);
            driverTools.swipeToElement(selectRoomMultiOrderLocator(1),Swipe.UP,6);
            driverTools.click(selectRoomMultiOrderLocator(1));
            driverTools.click(BOOK_BUTTON_MULTI_ROOM);
        } else {
            clickBookButton(roomCount);
        }
        BookingFormPage<?> bookingFormPage = BookingFormPage.getInstance(session);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
    }

    private Locator selectRoomMultiOrderLocator(int index) {
        return new Locator("SELECT_BTN_MO", AppiumBy.xpath("(//*[contains(@resource-id, 'tds_button_view') and (contains(@text,'Pilih') or contains(@text,'Select'))])[" + index + "]"));
    }

    @Override
    public List<String> getCancellationCharges() {
        boolean chargePresent = driverTools.swipeToElement(CANCELLATION_FEE,Swipe.UP,4).isPresent();
        if(chargePresent){
            List<String> priceItems = driverTools.getTexts(CANCELLATION_FEE);
            Assertion.assertThat("Prices exist", priceItems, is(not(empty())));
            logger.log("prices: "+priceItems);
            return priceItems;
        } else{
            logger.log("No Charges present");
            List<String> noChargePresent = new ArrayList<String>();
            noChargePresent.add("No Charges present");
            return noChargePresent;
        }
    }

    @Override
    public List<String> getPriceDetailPrices() {
        driverTools.waitForElementToAppear(PRICE_BREAKDOWN).orElseThrow();
        List<String> priceItems = new ArrayList<>(driverTools.getTexts(PRICE_BREAKDOWN));
        Assertion.assertThat("Prices exist", priceItems, is(not(empty())));
        priceItems.add(driverTools.getText(TOTAL_PRICE_ON_PRICE_DETAIL).orElseThrow());
        logger.log("prices: "+priceItems);
        priceItems.removeIf(str -> !str.contains(" "));
        return priceItems;
    }

    @Override
    public void seeAllReview() {
        driverTools.swipeToElement(SEE_ALL_REVIEW, Swipe.UP, 2).orElseThrow();
        driverTools.click(SEE_ALL_REVIEW);
        if (driverTools.isElementDisplayed(SEE_ALL_REVIEW)) driverTools.clickElementByCoordinate(SEE_ALL_REVIEW);
    }

    /**
     * Checks if the review section is displayed on the page.
     * It first swipes to the review rating element, then checks if the 'See More Review' button, review rating, and review by user section are displayed.
     * @return true if all the elements are displayed, false otherwise.
     */
    @Override
    public boolean isReviewSectionShown() {
        driverTools.swipeToElement(HOTEL_REVIEW_BUTTON, Swipe.UP, 2);
        driverTools.click(HOTEL_REVIEW_BUTTON);
        return driverTools.isElementDisplayed(SEE_MORE_REVIEW_BUTTON) && driverTools.isElementDisplayed(REVIEW_RATING) && driverTools.isElementDisplayed(REVIEW_BY_USER_SECTION);
    }

    /**
     * Checks if the facilities section is displayed on the page.
     * It first swipes to the popular facilities tab, then checks if it is displayed.
     * @return true if the popular facilities tab is displayed, false otherwise.
     */
    @Override
    public boolean isFacilitiesSectionShown() {
        driverTools.swipeToElement(POPULAR_FACILITIES_TAB, Swipe.UP, 2);
        return driverTools.isElementDisplayed(POPULAR_FACILITIES_TAB);
    }

    /**
     * Checks if the location section is displayed on the page.
     * It first swipes to the location section, then checks if it and the Google Maps element are displayed.
     * @return true if both elements are displayed, false otherwise.
     */
    @Override
    public boolean isLocationSectionShown() {
        driverTools.swipeToElement(LOCATION_SECTION, Swipe.UP, 2);
        driverTools.click(LOCATION_TAB_BUTTON);
        boolean isLocationSectionDisplayed = driverTools.isElementDisplayed(LOCATION_SECTION);
        Assertion.assertThat("Location section should be displayed", isLocationSectionDisplayed, is(true));
        boolean isGoogleMapsDisplayed = driverTools.isElementDisplayed(MAPS_CONTAINER);
        Assertion.assertThat("Google Maps should be displayed", isGoogleMapsDisplayed, is(true));
        return isLocationSectionDisplayed && isGoogleMapsDisplayed;
    }

    /**
     * Checks if the about accommodation section is displayed on the page.
     * It first swipes to the about accommodation content, then checks if it is displayed.
     * @return true if the about accommodation content is displayed, false otherwise.
     */
    @Override   
    public boolean isAboutAccommodationSectionShown() {
        driverTools.swipeToElement(ABOUT_ACCOMMODATION_TAB_BUTTON, CONTAINER_TAB, Swipe.LEFT, 10).orElseThrow();
        driverTools.click(ABOUT_ACCOMMODATION_TAB_BUTTON);
        return driverTools.isElementDisplayed(ABOUT_ACCOMMODATION_CONTENT);
    }

    /**
     * Clicks on the hotel image and checks if the gallery is opened.
     * After checking, it clicks the back button to return to the previous page.
     * @return true if the gallery is opened, false otherwise.
     */
    @Override
    public boolean clickImageAndVerifyGallery() {
        driverTools.click(HOTEL_IMAGE);
        boolean isGalleryOpened = driverTools.waitForElementToPresence(HOTEL_GALLERY_LOCATOR).isPresent();
        driverTools.click(CLOSE_POPUP_BUTTON);
        return isGalleryOpened;
    }

    @Override
    public boolean checkReviewWidgetSwipe() {
        driverTools.swipeToElement(REVIEW_CAROUSEL, Swipe.UP, 5);
        String firstReview = driverTools.getText(VISIBLE_REVIEW_CAROUSEL).orElseThrow();
        logger.log("Current visible review: " + firstReview);

        driverTools.swipeByDistance(REVIEW_CAROUSEL, Swipe.LEFT, 0.8, 2);
        String secondReview = driverTools.getText(VISIBLE_REVIEW_CAROUSEL).orElseThrow();
        logger.log("Current visible review: " + secondReview);
        Assertion.assertThat("Can swipe left to other review", secondReview, is(not(firstReview)));

        driverTools.swipeByDistance(REVIEW_CAROUSEL, Swipe.RIGHT, 0.8, 2);
        String thirdReview = driverTools.getText(VISIBLE_REVIEW_CAROUSEL).orElseThrow();
        logger.log("Current visible review: " + thirdReview);
        Assertion.assertThat("Can swipe right to first review", thirdReview, is(firstReview));

        return true;
    }

    public boolean checkReviewImageBottomSheetSwipe() {
        WebElement reviewPhotoCount = driverTools.swipeToElement(REVIEW_PHOTO_COUNT,REVIEW_PHOTO_COUNT_PARENT, Swipe.LEFT, 10).orElseThrow();
        driverTools.click(reviewPhotoCount);
        String image1Text = driverTools.getText(REVIEW_PHOTO_COUNT).orElseThrow().split("/")[0];
        driverTools.waitForElementToAppear(REVIEW_PHOTO_BOTTOM_SHEET_PARENT).orElseThrow();
        driverTools.swipeByDistance(REVIEW_PHOTO_BOTTOM_SHEET_PARENT, Swipe.LEFT, 0.8, 1);
        String image2Text = driverTools.getText(REVIEW_PHOTO_COUNT).orElseThrow().split("/")[0];
        Assertion.assertThat("Image no should be different", image1Text, is(not(image2Text)));
        return !image1Text.equals(image2Text);
    }

    private Locator tripInfoLocator(String text){
        return new Locator("Trip Info", AppiumBy.xpath("//*[contains(@resource-id,'v_trip_info') and not(contains(@text,'"+text+"'))]"));
    }

    @Override
    public void clickAllReviewsBtnOnReviewBottomSheet() {
        driverTools.click(SEE_ALL_REVIEW_BOTTOM_SHEET);
        if (driverTools.isElementDisplayed(SEE_ALL_REVIEW_BOTTOM_SHEET)) driverTools.clickElementByCoordinate(SEE_ALL_REVIEW_BOTTOM_SHEET);
    }

    @Override
    public boolean isBottomSheetReviewShown(){
        return driverTools.isElementDisplayed(SEE_ALL_REVIEW_BOTTOM_SHEET);
    }

    @Override
    public boolean isLabelTotalPrice() {
        return driverTools.isElementDisplayed(TOTAL_PRICE_LABEL);
    }

    @Override
    public boolean isLabelPerRoomPerNight(){
        WebElement labelPerRoomPerNight = driverTools.waitForElementToAppear(LABEL_PER_ROOM_PER_NIGHT).orElseThrow();
        String labelPerRoomPerNightText = driverTools.getText(labelPerRoomPerNight);
        return labelPerRoomPerNightText.contains("/room/night") || labelPerRoomPerNightText.contains("/kamar/malam");
    }

    @Override
    public void clickHeartIcon() {
        driverTools.waitForElementToPresence(HOTEL_NAME, 30).orElseThrow();
        driverTools.waitForElementToPresence(HEART_ICON, 10);
        driverTools.click(HEART_ICON);
    }

    @Override
    public void clickManageWishlist() {
        try {
            WebElement manageButton = driverTools.waitForElementToPresence(MANAGE_WISHLIST,3).orElseThrow();
            driverTools.click(manageButton);
        } catch (Exception e) {
            clickHeartIcon();
            WebElement manageButton = driverTools.waitForElementToPresence(MANAGE_WISHLIST,3).orElseThrow();
            driverTools.click(manageButton);
        }
    }

    @Override
    public String isWishlistDialogShown() {
        driverTools.waitForElementToAppear(HOTEL_NAME);
        WebElement manageWishlist = driverTools.waitForElementToPresence(MANAGE_WISHLIST).orElseThrow();
        return driverTools.getText(manageWishlist);
    }

    @Override
    public boolean isWishlistActive() {
        return driverTools.waitForElementToPresence(HEART_ICON_RED, 10).isPresent();
    }

    @Override
    public void clickLoginFromPDP() {
        driverTools.waitForElementToPresence(LOGIN_BUTTON, 30).orElseThrow();
        driverTools.click(LOGIN_BUTTON);
    }

    @Override
    public boolean isLoginButtonDisplayed() {
        driverTools.waitForElementToPresence(LOGIN_BUTTON, 30);
        return driverTools.isElementDisplayed(LOGIN_BUTTON);
    }

    @Override
    public boolean isMeetTravelPolicy() {
        return driverTools.waitForElementToPresence(YES_CONTINUE_MEET_POLICY_BTN).isPresent();
    }

    @Override
    public void clickYesContinue() {
        driverTools.waitForElementToPresence(YES_CONTINUE_MEET_POLICY_BTN, 10);
        driverTools.click(YES_CONTINUE_MEET_POLICY_BTN);
    }

    @Override
    public void TurnOffHeartIcon(){
        Assertion.assertThat("Heart icon red should be present", driverTools.isElementDisplayed(HEART_ICON_RED), is(true));
        driverTools.click(HEART_ICON_RED);
        driverTools.waitForElementToPresence(HEART_ICON);
        Assertion.assertThat("Heart icon should be turned grey", driverTools.isElementDisplayed(HEART_ICON), is(true));
    }

    @Override
    public boolean isInfinityPromoSectionDisplayed() {
        boolean displayed = driverTools.waitForElementToPresence(INFINITY_PROMO_SECTION, 20).isPresent();
        logger.log("isInfinityPromoSectionDisplayed -> " + displayed);
        return displayed;
    }

    @Override
    public int getInfinityPromoCardCount() {
        if (!driverTools.waitForElementToPresence(INFINITY_PROMO_CODE).isPresent()) {
            logger.log("getInfinityPromoCardCount -> no promo code on carousel");
            return 0;
        }
        String first = normalizeNonBreakingSpaceText(
                driverTools.getText(INFINITY_PROMO_CODE).orElse(""));
        if (first.isEmpty()) {
            logger.log("getInfinityPromoCardCount -> empty first promo code");
            return 0;
        }
        int distinct = 1;
        String prev = first;
        int staleSwipes = 0;
        final int maxSwipes = 30;
        for (int i = 0; i < maxSwipes; i++) {
            driverTools.swipeByDistance(INFINITY_PROMO_SECTION, Swipe.LEFT, 0.8);
            driverTools.waitForElementToPresence(INFINITY_PROMO_CODE);
            String curr = normalizeNonBreakingSpaceText(
                    driverTools.getText(INFINITY_PROMO_CODE).orElse(""));
            if (curr.isEmpty()) {
                logger.log("getInfinityPromoCardCount -> empty code after swipe; distinct=" + distinct);
                break;
            }
            if (curr.equals(prev)) {
                staleSwipes++;
                if (staleSwipes >= 3) {
                    logger.log("getInfinityPromoCardCount -> promo unchanged after 3 swipes; distinct=" + distinct);
                    break;
                }
                continue;
            }
            staleSwipes = 0;
            if (distinct > 1 && curr.equals(first)) {
                logger.log("getInfinityPromoCardCount -> wrapped to first promo; distinct=" + distinct);
                break;
            }
            distinct++;
            prev = curr;
        }
        logger.log("getInfinityPromoCardCount -> " + distinct + " distinct promo code(s) via carousel swipe");
        return distinct;
    }

    @Override
    public String copyFirstInfinityPromoCode() {
        driverTools.swipeToElement(INFINITY_PROMO_CARDS, Swipe.UP, 1);
        driverTools.swipeUp();
        WebElement firstCopyBtn = driverTools.waitForElementToPresence(INFINITY_PROMO_COPY_BTN).orElseThrow();
        String codeBeforeCopy = driverTools.getText(INFINITY_PROMO_CODE).orElse("");
        driverTools.clearClipboard();
        driverTools.click(firstCopyBtn);
        logger.log("copyFirstInfinityPromoCode tapped copy on first promo card; code on card -> " + codeBeforeCopy);
        return codeBeforeCopy;
    }

    @Override
    public boolean isSeeAllPromoButtonDisplayed() {
        boolean visible = driverTools.waitForElementToPresence(SEE_ALL_PROMO_BTN).isPresent();
        logger.log("isSeeAllPromoButtonDisplayed -> " + visible);
        return visible;
    }

    @Override
    public void clickSeeAllPromoButton() {
        WebElement seeAllBtn = driverTools.waitForElementToPresence(SEE_ALL_PROMO_BTN).orElseThrow();
        driverTools.click(seeAllBtn);
    }

    @Override
    public boolean isSeeAllPromoSectionShown() {
        return driverTools.waitForElementToAppear(SEE_ALL_PROMO_SECTION, 5).isPresent()
                || driverTools.waitForElementToAppear(SEE_ALL_PROMO_COPY_BTN, 3).isPresent();
    }

    @Override
    public String copyFirstPromoCodeFromSection() {
        WebElement firstCopyBtn = driverTools.swipeToElement(SEE_ALL_PROMO_COPY_BTN, Swipe.UP, 5).orElseThrow();
        String code = driverTools.getText(SEE_ALL_PROMO_CODE).orElse("");
        driverTools.clearClipboard();
        driverTools.click(firstCopyBtn);
        logger.log("copyFirstPromoCodeFromSection tapped copy on first promo in see-all section; code -> " + code);
        return code;
    }

    @Override
    public void closeSeeAllPromoSection() {
        if (driverTools.isElementDisplayed(CLOSE_POPUP_BUTTON)) {
            driverTools.click(CLOSE_POPUP_BUTTON);
        } else {
            driverTools.back();
        }
    }

    @Override
    public void openInAppMap() {
        driverTools.swipeToElement(LOCATION_SECTION, Swipe.UP, 2);
        try {
            driverTools.click(LOCATION_TAB_BUTTON);
        } catch (Exception e) {
            logger.log("failed to click, try to swipe then click again");
            driverTools.swipeUp();
            driverTools.click(LOCATION_TAB_BUTTON);
        }
        driverTools.swipeDown();
        // Prefer the map preview embedded in the location section.
        if (!driverTools.isElementDisplayed(MAPS_CONTAINER)) {
            driverTools.swipeToElement(MAPS_CONTAINER, Swipe.UP, 8);
        }
        if (driverTools.isElementDisplayed(MAPS_CONTAINER)) {
            driverTools.click(MAPS_CONTAINER);
        } else {
            // Fallback: click "See maps" button if no preview is shown.
            WebElement seeMaps = driverTools.swipeToElement(SEE_MAPS_BUTTON, Swipe.DOWN, 8).orElseThrow();
            driverTools.click(seeMaps);
        }
    }

    @Override
    public boolean isInAppMapPopupShown() {
        return driverTools.waitForElementToAppear(IN_APP_MAP_POPUP, 10).isPresent();
    }

    @Override
    public boolean isChosenHotelHighlightedOnMap(String hotelName) { //Android only
        return driverTools.waitForElementToAppear(hotelMarkerOnMapLocator(hotelName), 10).isPresent();
    }

    private Locator hotelMarkerOnMapLocator(String hotelName) {
        return new Locator("HOTEL_MARKER_ON_MAP_" + hotelName.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'fl_map')]//*[contains(@content-desc,'Google')]/*[contains(@class,'View') and contains(@content-desc,'"+hotelName+"')]"));
    }

    @Override
    public boolean areNearbyPointsShownOnMap(String hotelName) {
        return driverTools.waitForElementToAppear(nearbyPointLocator(hotelName), 5).isPresent();
    }

    private Locator nearbyPointLocator(String hotelName) {
        return new Locator("NEARBY_POINT", AppiumBy.xpath("//*[contains(@resource-id,'fl_map')]//*[contains(@content-desc,'Google')]/*[contains(@class,'View') and not(contains(@content-desc,'"+hotelName+"'))]"));
    }

    @Override
    public void clickNearbyChip() {
        driverTools.click(NEARBY_BUTTON);
    }

    @Override
    public boolean isNearbyAccommodationListShown() {
        return driverTools.waitForElementToPresence(NEARBY_ACCOMMODATION_LIST_CONTAINER, 20).isPresent();
    }

    @Override
    public boolean isNearbyChipDataShown() {
        return driverTools.waitForElementToAppear(NEARBY_CHIPS).isPresent();
    }

    @Override
    public void scrollRapidlyUntilRoomListAboutToOpen() {
        driverTools.waitForElementToAppear(HOTEL_NAME);
        for (int i = 0; i < 20; i++) {
            if (driverTools.waitForElementToPresence(SEE_HOTEL_ROOM_BUTTON_V4, 1).isPresent()) {
                logger.log("See hotel room button is visible; room list is about to open");
                return;
            }
            driverTools.swipeByDistance(HOTEL_NAME, Swipe.UP, 0.85, 1);
        }
        throw new org.openqa.selenium.NoSuchElementException("See hotel room button not visible after rapid scroll");
    }

    @Override
    public boolean isInAppMapBackButtonWithinSafeArea() {
        WebElement backButton = driverTools.waitForElementToAppear(BACK_BUTTON).orElseThrow();
        return isElementCenterWithinSafeArea(backButton);
    }

    @Override
    public boolean isAccommodationNearbySectionWithinSafeAreaAndTappable() {
        WebElement nearbySection = driverTools.waitForElementToAppear(NEARBY_BUTTON).orElseThrow();
        if (!isElementCenterWithinSafeArea(nearbySection)) {
            return false;
        }
        if (!driverTools.isElementClickable(NEARBY_BUTTON, 5)) {
            return false;
        }
        driverTools.click(NEARBY_BUTTON);
        return isNearbyAccommodationListShown();
    }

    @Override
    public boolean isFirstNearbyAccommodationHotelCardShown(String hotelName) {
        return driverTools.waitForElementToAppear(nearbyAccommodationHotelCard(hotelName), 10).isPresent();
    }

    @Override
    public boolean isFirstNearbyAccommodationHotelCardHidden(String hotelName) {
        return driverTools.waitForElementToDisappear(nearbyAccommodationHotelCard(hotelName), 10);
    }

    private Locator nearbyAccommodationHotelCard(String hotelName) {
        return new Locator("NEARBY_ACCOMMODATION_HOTEL_CARD_" + hotelName.toUpperCase(),
                AppiumBy.xpath("(//*[contains(@resource-id,'cl_main_content')]//*[contains(@resource-id,'tv_hotel_name') and @text='" + hotelName + "'])[1]"));
    }

    @Override
    public void clickInAppMapBackButton() {
        driverTools.waitForElementToAppear(BACK_BUTTON).orElseThrow();
        driverTools.click(BACK_BUTTON);
    }

    @Override
    public boolean isCrossSellNudgeShownOnNearbyAccommodation() {
        for (int i = 0; i < 5; i++) {
            if (driverTools.waitForElementToPresence(NEARBY_ACCOMMODATION_HOTEL_NAME_WITH_CROSS_SELL).isPresent()) {
                logger.log("Cross sell nudge is shown on nearby accommodation");
                return true;
            }
            driverTools.swipeByDistance(NEARBY_ACCOMMODATION_LIST_CONTAINER, Swipe.LEFT, 0.9);
        }
        logger.log("Cross sell nudge is not shown on nearby accommodation");
        return driverTools.waitForElementToAppear(NEARBY_ACCOMMODATION_CROSS_SELL_NUDGE, 3).isPresent();
    }

    @Override
    public String getNearbyAccommodationHotelNameWithCrossSell() {
        if (driverTools.waitForElementToPresence(NEARBY_ACCOMMODATION_HOTEL_NAME_WITH_CROSS_SELL).isEmpty())
            driverTools.swipeByDistance(NEARBY_ACCOMMODATION_LIST_CONTAINER, Swipe.LEFT, 0.9);
        String hotelName = driverTools.getText(NEARBY_ACCOMMODATION_HOTEL_NAME_WITH_CROSS_SELL).orElseThrow().trim();
        logger.log("Nearby accommodation with cross sell -> " + hotelName);
        return hotelName;
    }

    @Override
    public void clickNearbyAccommodationWithCrossSell() {
        if (driverTools.waitForElementToAppear(NEARBY_ACCOMMODATION_HOTEL_NAME_WITH_CROSS_SELL).isEmpty())
            driverTools.swipeByDistance(NEARBY_ACCOMMODATION_LIST_CONTAINER, Swipe.LEFT, 0.9);
        driverTools.click(NEARBY_ACCOMMODATION_HOTEL_NAME_WITH_CROSS_SELL);
    }
    
    @Override
    public String getTotalCashback(){
        driverTools.waitForElementsToAppear(CASHBACK_AMOUNT);
        String cashback = driverTools.getText(CASHBACK_AMOUNT).orElseThrow();
        cashback = cashback.replaceAll("[^0-9]", "");
        logger.log("Cashback Amount -> "+cashback);
        return cashback;
    }

}

