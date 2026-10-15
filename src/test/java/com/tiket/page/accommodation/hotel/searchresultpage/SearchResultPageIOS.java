package com.tiket.page.accommodation.hotel.searchresultpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.accommodation.homes.HomesPage;
import com.tiket.page.accommodation.homes.homessearchresultpage.HomesSearchResultPageIOS;
import com.tiket.page.accommodation.hotel.HotelPage;
import com.tiket.page.accommodation.hotel.detailpage.DetailPageIOS;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.landingpage.LandingPageIOS;
import com.tiket.page.home.HomePage;
import com.tiket.pojo.accommodation.homes.HomesData;
import com.tiket.pojo.accommodation.homes.HomesNHAResponse;
import com.tiket.pojo.accommodation.hotel.FilterInfo;
import com.tiket.pojo.accommodation.hotel.LandingPageModulePOJO;
import com.tiket.pojo.accommodation.master_tag.AccommodationMasterTagResponse;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Stream;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.page.accommodation.hotel.landingpage.LandingPageIOS.*;
import static com.tiket.page.home.HomePageIOS.CLOSE_GIVE_RATING_POPUP;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

public class SearchResultPageIOS<T extends WebDriver> extends SearchResultPage<T> {

    public SearchResultPageIOS(Session<T> session) {
        super(session);
    }

    public static final Locator LOADING_STATE = new Locator("LOADING_STATE", AppiumBy.id("TVCell<TDSTransitionLargeFullWidthBanner>"));
    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("//*[@name='icBackNavigation' or @name='back']"));
    // Info-page WebView back has no accessibility name — first button in the top banner
    private static final Locator INFO_PAGE_BACK_BUTTON = new Locator("INFO_PAGE_BACK_BUTTON", AppiumBy.xpath("//*[@name='banner']/XCUIElementTypeButton[1]"));
    public static final Locator HOTEL_CARD = new Locator("HOTEL_CARD", AppiumBy.xpath("//XCUIElementTypeCell[starts-with(@name,'TACMSRPCardCell')]"));
    // New UI: hotelNameLabel > hotelName; legacy: TACMLabelWithAttachmentView.label
    public static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", AppiumBy.xpath("//*[@name='hotelNameLabel']//*[@name='hotelName' or @name='TACMLabelWithAttachmentView.label'] | //XCUIElementTypeStaticText[@name='hotelName']"));
    public static final Locator SRP_TITLE = new Locator("SRP_TITLE", AppiumBy.xpath("//*[@name='location_label']"));
    public static final Locator HOTEL_PRICE = new Locator("HOTEL_PRICE", AppiumBy.name("mainPrice"));
    private static final Locator VISIBLE_HOTEL_CONTAINER = new Locator("VISIBLE_HOTEL_CONTAINER", AppiumBy.xpath("//XCUIElementTypeCell[starts-with(@name,'TACMSRPCardCell') and @visible='true']"));
    public static final Locator HOTEL_LOCATION = new Locator("HOTEL_LOCATION", AppiumBy.xpath("//XCUIElementTypeCell[starts-with(@name,'TACMSRPCardCell')]//*[@name='locationLabel']"));
    public static final Locator CHANGE = new Locator("CHANGE", AppiumBy.iOSClassChain("**/*[`name CONTAINS 'change_button'`]"));
    public static final Locator SEARCH_FIELD_BUTTON = new Locator("SEARCH_FIELD_BUTTON", AppiumBy.xpath("//XCUIElementTypeCell[@name=\"THMChangeSearchDestinationTableViewCell\"]/XCUIElementTypeButton[2]"));
    private static final Locator SEARCH_FIELD_SECTION = new Locator("SEARCH_FIELD_SECTION", AppiumBy.name("THMChangeSearchDestinationTableViewCell"));
    public static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", AppiumBy.xpath("(//XCUIElementTypeButton[contains(@label,'Search') or contains(@label,'Ayo Cari') or contains(@label,'Selesai') or contains(@label,'Done')])[last()]"));
    public static final Locator CURRENT_LOCATION = new Locator("CURRENT_LOCATION", AppiumBy.name("location_label"));
    public static final Locator PRICE_FILTER = new Locator("PRICE_FILTER", AppiumBy.xpath("//XCUIElementTypeButton[@name='tds ic bottom more']//ancestor::*[@name='Price' or @name='Harga']"));
    private static final Locator LOYALTY_FILTER_TOGGLE = new Locator("LOYALTY_FILTER_TOGGLE", AppiumBy.name("switch_loyalty"));
    private static final Locator STAR_FILTER_SECTION = new Locator("STAR_FILTER_SECTION", AppiumBy.xpath("//*[@name='TVCell<TACMFilterGroupAccordionView>+groupHotelStarRatings']//*[@name='tds_ic_review_star_full' and @visible='true']"));
    public static final Locator PRICE_FILTER_SIGN = new Locator("PRICE_FILTER_SIGN", AppiumBy.xpath("//*[@name='bottomActionItem_price (badged)'] | //XCUIElementTypeOther[contains(@name,'selected') and (contains(@name, '<') or contains(@name, '>') or contains(@name, '≤') or contains(@name, '≥') or contains(@name, '-'))]"));
    private static final Locator FREQUENT_PRICE_FILTER_CHIP_ALL = new Locator("FREQUENT_PRICE_FILTER_CHIP_ALL", AppiumBy.xpath("//*[contains(@name,'PriceRangeChips')]//XCUIElementTypeStaticText"));
    public static final Locator FILTER_SUBMIT_BUTTON = new Locator("FILTER_SUBMIT_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[(contains(@label,'Simpan') or contains(@label,'Save') or contains(@label,'Tampilkan hasil') or contains(@label,'Show result')) and @visible='true']"));
    public static final Locator DATE_ROOM_GUEST_DETAIL = new Locator("DATE_ROOM_GUEST_DETAIL", AppiumBy.name("date_room_guest_label"));
    public static final Locator HOTEL_CARD_IMAGE = new Locator("HOTEL_CARD_IMAGE", AppiumBy.id("srpCard.image-0"));
    private static final Locator NOT_FOUND_CONTAINER = new Locator("NOT_FOUND_CONTAINER", AppiumBy.xpath("//*[@name='Empty list' or @name='TVCell<THMErrorContainer>']"));
    public static final Locator EMPTY_HOTEL_BANNER = new Locator("EMPTY_HOTEL_BANNER", AppiumBy.iOSClassChain("**/XCUIElementTypeTable[`name == 'Empty list' OR name == 'TVCell<THMErrorContainer>'`]"));
    public static final Locator QUICK_FILTER_CONTAINER = new Locator("QUICK_FILTER_CONTAINER", AppiumBy.xpath("(//*[@name='tds_ic_filter']//parent::*//parent::*//parent::*//parent::*//parent::*//parent::*)[1]"));
    public static final Locator MANAGE_WISHLIST = new Locator("MANAGE_WISHLIST", AppiumBy.iOSClassChain("**/*[`label == 'Manage' OR label == 'Atur'`]"));

    // search result page identifier element
    public static final Locator SRP_FILTER_BAR = new Locator("SRP_FILTER_BAR", AppiumBy.xpath("//*[@name='icBackNavigation' or @name='tds_ic_back']/parent::*/parent::*/following-sibling::XCUIElementTypeOther/XCUIElementTypeOther"));
    public static final Locator SEE_MAP_BUTTON = new Locator("SEE_MAP_BUTTON", AppiumBy.name("mapsFloatingButton"));
    private static final Locator HOTEL_NAME_MAPS = new Locator("HOTEL_NAME_MAPS", AppiumBy.xpath("//XCUIElementTypeTable/following-sibling::XCUIElementTypeOther//XCUIElementTypeCollectionView/XCUIElementTypeCell//*[@name='hotelNameLabel' and @visible='true']"));
    public static final Locator HOTEL_PRICE_MAPS = new Locator("HOTEL_PRICE_MAPS", AppiumBy.xpath("//XCUIElementTypeMap//following-sibling::*//*[@name='MapPriceMarker']//parent::*[contains(@label,' ')]"));
    private static final Locator HOTEL_STAR_CARD_MAPS = new Locator("HOTEL_STAR_CARD_MAPS", AppiumBy.xpath("//*[contains(@name,'starItemView')]/parent::XCUIElementTypeOther[@visible='true']"));
    public static final Locator MAP_CLOSE = new Locator("MAP_CLOSE", AppiumBy.name("tds_ic_cross_big"));
    private static final Locator VISIBLE_HOTEL_PRICE_CARD = new Locator("VISIBLE_HOTEL_PRICE_CARD", AppiumBy.xpath("//*[@name='mainPrice' and @visible='true']"));
    private static final Locator VISIBLE_HOTEL_PRICE_CARD_MAPS = new Locator("VISIBLE_HOTEL_PRICE_CARD_MAPS", AppiumBy.xpath("(//*[@name='hotelNameLabel' and @visible='true']//following-sibling::*//XCUIElementTypeOther//XCUIElementTypeStaticText[not(contains(@label,':') or contains(@label,'%'))])[1]"));
    public static final Locator LOYALTY_BADGE_HOTEL_CARD = new Locator("LOYALTY_BADGE_HOTEL_CARD", AppiumBy.xpath("//*[@name='TACMSRPCardCell<TACMSRPCaliforniaBigCardView>']/*[@name='loyaltyLabel']/*[@name='srpCard.loyalty' and string-length(@label) > 0]"));
    private static final Locator PREFERRED_PARTNER_ICON = new Locator("PREFERRED_PARTNER_ICON", AppiumBy.xpath("//*[@name='preferredPartnerType' and contains(translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'tier')]"));
    private static final Locator MAPS_CONTAINER = new Locator("MAPS_CONTAINER", AppiumBy.iOSClassChain("**/XCUIElementTypeMap"));
    private static final Locator SEARCH_THIS_AREA_BUTTON = new Locator("SEARCH_THIS_AREA_BUTTON", AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label CONTAINS 'Cari di area ini' OR label CONTAINS 'Search in this area'`]"));
    private static final Locator SEARCH_PREVIOUS_BUTTON = new Locator("SEARCH_PREVIOUS_BUTTON", AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label CONTAINS 'pencarian sebelumnya' OR label CONTAINS 'previous search'`]"));
    private static final Locator HOTEL_LIST_CONTAINER = new Locator("HOTEL_LIST_CONTAINER", AppiumBy.xpath("//XCUIElementTypeCell[starts-with(@name,'TACMSRPCardCell')]/parent::XCUIElementTypeTable"));
    private static final Locator SRP_BANNER_TEXT = new Locator("SRP_BANNER_TEXT", AppiumBy.xpath("//*[@name='TACMSRPBannerView.textLabel' and @visible='true']"));
    private static final Locator SRP_BANNER_CONTAINER = new Locator("SRP_BANNER_CONTAINER", AppiumBy.id("TVCell<TACMSRPBannerListView>"));
    private static final Locator LOGIN_BANNER = new Locator("LOGIN_BANNER", AppiumBy.xpath("//*[@name='Log in to enjoy the benefits!' or @name='Log in buat dapet benefitnya!']"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", AppiumBy.xpath("//*[@value='Log in']"));
    private static final Locator TITLE_BOTTOM_POPUP = new Locator("TITLE_BOTTOM_POPUP", AppiumBy.xpath("(//*[@name='ic cancel grey']//parent::*//parent::XCUIElementTypeOther//following-sibling::XCUIElementTypeOther//XCUIElementTypeStaticText)[1]"));

    // first hotel card srp element (name may be bare TACMSRPCardCell or typed TACMSRPCardCell<...>)
    public static final Locator FIRST_CARD_HOTEL = new Locator("FIRST_CARD_HOTEL", AppiumBy.xpath("(//XCUIElementTypeCell[starts-with(@name,'TACMSRPCardCell')])[1]"));
    public static final Locator FIRST_CARD_HOTEL_NAME = new Locator("FIRST_CARD_HOTEL_NAME", AppiumBy.xpath("(//*[@name='hotelNameLabel']//*[@name='hotelName' or @name='TACMLabelWithAttachmentView.label'] | //XCUIElementTypeStaticText[@name='hotelName'])[1]"));
    public static final Locator FIRST_CARD_HOTEL_LOCATION = new Locator("FIRST_CARD_HOTEL_LOCATION", AppiumBy.xpath("(//XCUIElementTypeCell[starts-with(@name,'TACMSRPCardCell')]//*[@name='locationLabel'])[1]"));
    public static final Locator FIRST_CARD_HOTEL_PRICE = new Locator("FIRST_CARD_HOTEL_PRICE", AppiumBy.xpath("(//*[@name='mainPrice'])[1]"));
    public static final Locator FIRST_CARD_HOTEL_REVIEW = new Locator("FIRST_CARD_HOTEL_REVIEW", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name=\"TACMRatingView.rating\"])[1]"));
    public static final Locator ADDED_BENEFIT = new Locator("ADDED_BENEFIT", AppiumBy.xpath("(//XCUIElementTypeCell[starts-with(@name,'TACMSRPCardCell')])[1]//XCUIElementTypeStaticText[@name='srpCard.highlightedBenefits']"));
    public static final Locator CANCEL_BUTTON = new Locator("CANCEL_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='ic cancel grey']"));
    public static final Locator MEET_TRAVEL_POLICY_NUDGE = new Locator("MEET_TRAVEL_POLICY_NUDGE", AppiumBy.xpath("(//XCUIElementTypeOther[@name='travelPolicyLabel']//XCUIElementTypeStaticText[@label='Meets Travel Policy' or @label='Sesuai Kebijakan' or @value='Meets Travel Policy' or @value='Sesuai Kebijakan'] | //XCUIElementTypeStaticText[@label='Meets Travel Policy' or @label='Sesuai Kebijakan'])[1]"));
    public static final Locator MEET_TRAVEL_POLICY_HOTEL_CARD = new Locator("MEET_TRAVEL_POLICY_HOTEL_CARD", AppiumBy.xpath("(//XCUIElementTypeCell[contains(@name,'TACMSRPCardCell')][.//XCUIElementTypeStaticText[@label='Meets Travel Policy' or @label='Sesuai Kebijakan' or @value='Meets Travel Policy' or @value='Sesuai Kebijakan']])[1]"));
    public static final Locator LABEL_PER_ROOM_PER_NIGHT = new Locator("LABEL_PER_ROOM_PER_NIGHT", AppiumBy.xpath("(//XCUIElementTypeStaticText[(@name='srpCard.priceTax' or @name='unitText2')])[1]"));
    public static final Locator GOT_IT = new Locator("GOT_IT", AppiumBy.xpath("//*[contains(@name, 'Got it') or contains(@name, 'Lanjut') and @type='XCUIElementTypeButton']"));

    private static final Locator FLEXIBLE_STAY_HEADING = new Locator("FLEXIBLE_STAY_HEADING",
            AppiumBy.xpath("//*[contains(@name,'Do you have flexible stay dates') or contains(@label,'Do you have flexible stay dates') or contains(@name,'Tanggal nginepmu fleksibel') or contains(@label,'Tanggal nginepmu fleksibel')]"));
    private static final Locator FLEXIBLE_STAY_HOTEL_CARDS = new Locator("FLEXIBLE_STAY_HOTEL_CARDS",
            AppiumBy.xpath("//*[@name='TVCell<TACMFlexibleStayView>']//XCUIElementTypeCollectionView//XCUIElementTypeCell"));

    // sort element locator
    public static final Locator SORT_BY_BUTTON = new Locator("SORT_BY_BUTTON", AppiumBy.iOSClassChain("**/*[`name CONTAINS 'bottomActionItem_sort' || name == 'tds_ic_sort'`]"));
    public static final Locator SORT_BY_RECOMMENDATION = new Locator("SORT_BY_RECOMMENDATION", AppiumBy.xpath("//XCUIElementTypeCell[contains(@name,'SearchResultQuickSortOptionTableViewCell')][1]"));
    public static final Locator SORT_BY_HIGHEST_RATING = new Locator("SORT_BY_HIGHEST_RATING", AppiumBy.xpath("//XCUIElementTypeCell[contains(@name,'SearchResultQuickSortOptionTableViewCell')][2]"));
    public static final Locator SORT_BY_HIGHEST_PRICE = new Locator("SORT_BY_HIGHEST_PRICE", AppiumBy.xpath("//XCUIElementTypeCell[contains(@name,'SearchResultQuickSortOptionTableViewCell')][3]"));
    public static final Locator SORT_BY_LOWEST_PRICE = new Locator("SORT_BY_LOWEST_PRICE", AppiumBy.xpath("//XCUIElementTypeCell[contains(@name,'SearchResultQuickSortOptionTableViewCell')][4]"));
    public static final Locator SORT_BY_HIGHEST_STAR = new Locator("SORT_BY_HIGHEST_STAR", AppiumBy.xpath("//XCUIElementTypeCell[contains(@name,'SearchResultQuickSortOptionTableViewCell')][5]"));
    public static final Locator SORT_BY_BUTTON_BADGED = new Locator("SORT_BY_BUTTON_BADGED", AppiumBy.xpath("//*[@name='tds_ic_sort']/parent::*/parent::XCUIElementTypeOther/following-sibling::*/*/XCUIElementTypeStaticText"));

    // filter-related element
    public static final Locator FILTER_BUTTON = new Locator("FILTER_BUTTON", AppiumBy.xpath("//*[@name='Filter' or @label='Filter'] | //*[@name='bottomActionItem_all' or contains(@name,'bottomActionItem_filter') or @name='tds_ic_filter' or @name='tds ic filter'] | //XCUIElementTypeButton[@name='tds ic bottom more']//ancestor::*[@name='Filter' or @label='Filter']"));
    public static final Locator INTERCEPT_FILTER = new Locator("INTERCEPT_FILTER", AppiumBy.xpath("(//XCUIElementTypeCell[contains(@name,'TVCell') and contains(@name,'BadgeCardView')])[1]"));
    public static final Locator FILTER_BUTTON_BADGE = new Locator("FILTER_BUTTON_BADGE", AppiumBy.xpath("//*[@name='Filter']/*/*/XCUIElementTypeOther/*/XCUIElementTypeStaticText"));
    public static final Locator RESET_FILTER = new Locator("RESET", AppiumBy.name("Reset"));
    public static final Locator PRICE_CHANGE_TEXT_FIELD = new Locator("PRICE_CHANGE_TEXT_FIELD", AppiumBy.xpath("//XCUIElementTypeCell[@name='TVCell<THMPriceRangeView>']//XCUIElementTypeTextField[1]"));
    public static final Locator PRICE_CHANGE_TEXT_FIELDS = new Locator("PRICE_CHANGE_TEXT_FIELDS", AppiumBy.xpath("//XCUIElementTypeCell[@name='TVCell<THMPriceRangeView>']//XCUIElementTypeTextField"));
    private static final Locator MIN_PRICE_CURRENCY = new Locator("MIN_PRICE_CURRENCY", AppiumBy.xpath("(//XCUIElementTypeTextField)[2]//XCUIElementTypeStaticText"));
    private static final Locator MAX_PRICE_CURRENCY = new Locator("MAX_PRICE_CURRENCY", AppiumBy.xpath("(//XCUIElementTypeTextField)[1]//XCUIElementTypeStaticText"));
    public static final Locator MIN_PRICE_FILTER_INPUT = new Locator("MIN_PRICE_FILTER_INPUT", AppiumBy.iOSClassChain("**/XCUIElementTypeTextField[2]"));
    public static final Locator MAX_PRICE_FILTER_INPUT = new Locator("MAX_PRICE_FILTER_INPUT", AppiumBy.iOSClassChain("**/XCUIElementTypeTextField[1]"));
    public static final Locator KEYBOARD_DELETE_BUTTON = new Locator("KEYBOARD_DELETE_BUTTON", AppiumBy.iOSClassChain("**/XCUIElementTypeKey[`label == 'Delete'`]"));
    public static final Locator FILTER_POPUP_TRY_BTN = new Locator("FILTER_POPUP_TRY_BTN", AppiumBy.xpath("//*[@name=\"Got it\" and @type=\"XCUIElementTypeButton\"]"));
    private static final Locator REFUND_RESCHEDULE_TOGGLE = new Locator("REFUND_RESCHEDULE_TOGGLE", AppiumBy.xpath("//*[contains(@name,'switch_freeCancellation')]"));
    private static final Locator STAR_FILTER_BUTTON = new Locator("STAR_FILTER_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='tds ic bottom more']//ancestor::*[@name='Star' or @name='Bintang']"));
    private static final Locator FREE_BREAKFAST = new Locator("FREE_BREAKFAST", AppiumBy.xpath("//*[@label='Free Breakfast' or @label='Sarapan Gratis']"));
    private static final Locator FREE_CANCELLATION = new Locator("FREE_CANCELLATION", AppiumBy.xpath("//*[@label='Free Cancellation' or @label='Pembatalan Gratis']"));
    private static final Locator DEALS = new Locator("DEALS", AppiumBy.xpath("//*[@label='Deals' or @label='Promo']"));
    private static final Locator PREFERRED_PARTNER_PLUS = new Locator("PREFERRED_PARTNER_PLUS", AppiumBy.xpath("//*[@label='Preferred Partner Plus']"));
    private static final Locator PREFERRED_PARTNER = new Locator("PREFERRED_PARTNER", AppiumBy.xpath("//*[@label='Preferred Partner']"));
    private static final Locator SKIP_POPUP = new Locator("SKIP_POPUP", AppiumBy.xpath("//*[@type='XCUIElementTypeButton' and (@name='Got it' or @name='Mengerti')]"));
    private static final Locator POPULAR_PLACE = new Locator("POPULAR_PLACE", AppiumBy.xpath("//XCUIElementTypeCell[@name='TVCell<TACMFilterGroupAccordionView>+groupPoi']/*[@name='checkbox']"));
    private static final Locator AFTER_TAX_CHECKBOX = new Locator("AFTER_TAX_CHECKBOX", AppiumBy.xpath("//XCUIElementTypeCell[@name='TVCell<TACMFilterGroupAccordionView>+groupPrice']//XCUIElementTypeOther[contains(@name,'switch_isAat')]"));
    private static final Locator TOTAL_PRICE_LABEL = new Locator("TOTAL_PRICE_LABEL", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Total harga (termasuk pajak)' or @label='Total price (including taxes)']"));
    public static final Locator TOTAL_PRICE_LABEL_WITHOUT_TAX = new Locator("TOTAL_PRICE_LABEL_WITHOUT_TAX", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'(setelah pajak:') or contains(@label,'(after taxes:')]"));
    private static final Locator MEETS_TRAVEL_POLICY_NUDGE = new Locator("MEETS_TRAVEL_POLICY_NUDGE", AppiumBy.xpath("(//XCUIElementTypeStaticText[@label='Meets Travel Policy' or @label='Sesuai Kebijakan'])[1]"));
    private static final Locator ALL_ITEM_SAVE_BUTTON = new Locator("ALL_ITEM_SAVE_BUTTON", AppiumBy.xpath("//*[@name='ic cancel grey' and @visible='true']/following::XCUIElementTypeButton"));
    private static final Locator FIRST_WISHLIST_BTN = new Locator("FIRST_WISHLIST_BTN", AppiumBy.xpath("(//*[@name='wishlist' and ./preceding-sibling::*[@name='reviewNudgeLabel']])[1]"));
    private static final Locator MANAGE_SNACKBAR = new Locator("MANAGE_SNACKBAR", AppiumBy.xpath("//*[@name='mapsFloatingButton']"));
    private static final Locator HOTEL_CARD_MAPS = new Locator("HOTEL_CARD_MAPS", AppiumBy.xpath("//*[@type='XCUIElementTypeOther' and ./*[@name='wishlist']]"));
    public static final Locator WISHLIST_ICON_RED = new Locator("WISHLIST_ICON_RED", AppiumBy.xpath("(//*[@name='wishlist (selected)'])[1]"));
    public static final Locator REMOVE_WISHLIST = new Locator("REMOVE_WISHLIST", AppiumBy.xpath("//*[@name='snackbar']//*[@label='Removed from All Wishlist.' or @label='Dihapus dari Semua Wishlist.']"));
    private static final Locator HIGHLIGHT_LABEL = new Locator("HIGHLIGHT_LABEL", AppiumBy.xpath("//*[@name='highlightedHeaderView']"));
    private static final Locator VISIBLE_VENDOR_CODE = new Locator("VISIBLE_VENDOR_CODE", AppiumBy.xpath("//*[@name='hotelNameLabel' and @visible='true']/following-sibling::*[@name='vendorCodeLabel']"));
    private static final Locator ALL_VENDOR_CODE = new Locator("ALL_VENDOR_CODE", AppiumBy.name("vendorCodeLabel"));
    private static final Locator VISIBLE_HOTEL_NAME = new Locator("VISIBLE_HOTEL_NAME", AppiumBy.xpath("//*[@name='hotelName' and @visible='true']"));
    private static final Locator TAXES_FILTER_SWITCH = new Locator("TAXES_FILTER_SWITCH", AppiumBy.accessibilityId("switch (selected)"));
    private static final Locator PER_NIGHT_RADIO_BTN = new Locator("PER_NIGHT_RADIO_BTN", AppiumBy.xpath("(//XCUIElementTypeOther[@name=\"radioButton (selected)\"])[1]"));
    private static final Locator TOTAL_PRICE_FILTER_OPTION = new Locator("TOTAL_PRICE_FILTER_OPTION", AppiumBy.iOSClassChain("**/*[`name CONTAINS 'switch_priceDisplay'`]"));
    private static final Locator PRICE_TAX_FILTER_OPTION = new Locator("PRICE_TAX_FILTER_OPTION", AppiumBy.iOSClassChain("**/*[`name CONTAINS 'switch_isAat'`]"));

    private Locator getHotelName(int index) {
        return new Locator("HOTEL_NAME_SELECTED", AppiumBy.xpath("(//*[@name='hotelNameLabel']//*[@name='hotelName' or @name='TACMLabelWithAttachmentView.label'] | //XCUIElementTypeStaticText[@name='hotelName'])[" + index + "]"));
    }

    private Locator getHotelLocation(int index) {
        return new Locator("HOTEL_LOCATION_SELECTED", AppiumBy.xpath("(//XCUIElementTypeCell[starts-with(@name,'TACMSRPCardCell')]//*[@name='locationLabel'])[" + index + "]"));
    }

    private Locator searchType(String location, String locationType) {
        return new Locator("SEARCH_TYPE_" + location.toUpperCase(), AppiumBy.xpath("(//*[@name='suggestion_name_list' and (@value='" + location + "' or @value='" + location.toLowerCase() + "')])[1]//following-sibling::*[@name='suggestion_type_list' and contains(@label,'" + locationType + "')]"));
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
        WebElement hotelNameByIndex = driverTools.waitForElementToPresence(getHotelName(index), 20).orElseThrow();
        driverTools.click(hotelNameByIndex);
    }

@Override
    public boolean isHotelNameAdsVisible(String hotelName) {
        Assertion.skip("Skip on iOS: promoted ads badge check is pending implementation");
        return false;
    }

    /**
     * This method is used to get the text of the Search Result Page (SRP) banner.
     * It waits for the SRP_BANNER_TEXT element to be present and then retrieves its text.
     *
     * @return String representing the text of the SRP banner.
     * @throws NoSuchElementException if the SRP_BANNER_TEXT element is not found.
     */
    @Override
    public String getSrpBanner() {
        WebElement srpBanner = driverTools.swipeByDistanceToElement(SRP_BANNER_TEXT, HOTEL_LIST_CONTAINER, Swipe.UP, 0.7, 6).orElseThrow();
        return driverTools.getText(srpBanner);
    }

    /**
     * This method checks if the banner text on the Search Result Page (SRP) has changed from its initial state.
     * It performs this check up to 20 times.
     *
     * @param initBanner The initial banner text to compare against.
     * @return true if the banner text is different from the initial banner text, false otherwise.
     * @throws NoSuchElementException if the SRP_BANNER_TEXT element is not present.
     */
    @Override
    public boolean checkBannerChanges(String initBanner) {
        for (int i = 0; i < 20; i++) {
            driverTools.waitForElementToPresence(SRP_BANNER_TEXT);
            String bannerText = driverTools.getText(SRP_BANNER_TEXT).orElseThrow();
            if (!bannerText.equals(initBanner)) {
                Assertion.assertThat("Banner text should be different", bannerText, not(equalTo(initBanner)));
                break;
            }
        }
        return true;
    }

    @Override
    public boolean checkHighlightedResult() {
        return driverTools.isElementDisplayed(HIGHLIGHT_LABEL);
    }

    /**
     * This method is used to swipe the banner on the Search Result Page (SRP).
     *
     * @param swipe Enum representing the direction to swipe (LEFT or RIGHT).
     * @return String representing the text of the banner after the swipe.
     */
    @Override
    public String swipeBanner(Swipe swipe) {
        driverTools.waitForElementToAppear(SRP_BANNER_CONTAINER).orElseThrow();
        driverTools.swipeByDistance(SRP_BANNER_CONTAINER, swipe, 0.9, 1);
        return getSrpBanner();
    }

    /**
     * This method is used to click on the banner on the Search Result Page (SRP).
     */
    @Override
    public void clickBanner() {
        WebElement srpBanner = driverTools.swipeByDistanceToElement(SRP_BANNER_TEXT, HOTEL_LIST_CONTAINER, Swipe.UP, 0.7, 6).orElseThrow();
        driverTools.click(srpBanner);
        if (driverTools.isElementDisplayed(SRP_BANNER_TEXT)) driverTools.clickElementByCoordinate(SRP_BANNER_TEXT);
    }

    @Override
    public void scrollToBannerSection() {
        driverTools.swipeByDistanceToElement(SRP_BANNER_TEXT, HOTEL_LIST_CONTAINER, Swipe.UP, 0.7, 6).orElseThrow();
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
            driverTools.swipeByDistance(SRP_BANNER_CONTAINER, Swipe.LEFT, 0.9, 1);
            driverTools.dummyWait(1);
        }
        throw new org.openqa.selenium.NoSuchElementException("SRP banner not found: " + bannerTitle);
    }

    private Locator srpBannerByTitle(String title) {
        return new Locator("SRP_BANNER_TITLE", AppiumBy.xpath("//*[@name='TACMSRPBannerView.textLabel' and contains(@label,'" + title + "')]"));
    }

    @Override
    public void navigateBackToSrp() {
        Locator back = driverTools.waitForElementToPresence(INFO_PAGE_BACK_BUTTON, 5).isPresent()
                ? INFO_PAGE_BACK_BUTTON
                : BACK_BUTTON;
        driverTools.waitForElementToPresence(back).orElseThrow();
        driverTools.click(back);
        driverTools.waitForElementToPresence(HOTEL_CARD, 20).orElseThrow();
    }

    /**
     * This method is used to get the redirection banner text or the URL of the opened WebView.
     *
     * @return String representing the redirection banner text or the URL of the opened WebView.
     * @throws NoSuchElementException if the TITLE_BOTTOM_POPUP element is not found.
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

        int swipeIndex;
        int totalSwipe = driverTools.waitForPresenceElementsList(ALL_VENDOR_CODE).orElseThrow().size();
        for (swipeIndex = 0; swipeIndex < totalSwipe; swipeIndex++) {
            if (swipeIndex >= 1) {
                logger.log("Swipe " + swipeIndex);
                driverTools.swipeUp();
            }
            List<WebElement> visibleVendorCodeList = driverTools.waitForPresenceElementsList(VISIBLE_VENDOR_CODE).orElseThrow();
            for (int visibleIndex = 1; visibleIndex <= visibleVendorCodeList.size(); visibleIndex++) {
                String currentVendorCode = driverTools.getText(visibleVendorCodeList.get(visibleIndex - 1));
                if (vendorCode.equals(currentVendorCode)) {
                    WebElement selectedHotel = driverTools.waitForPresenceElementsList(VISIBLE_HOTEL_NAME).orElseThrow().get(visibleIndex - 1);
                    String hotelNameLabel = driverTools.getText(selectedHotel);
                    driverTools.click(selectedHotel);
                    logger.log("Found vendor code " + vendorCode + " at swipe index " + swipeIndex + ", visible index " + visibleIndex + ", opening hotel PDP: " + hotelNameLabel);
                    return;
                }
                logger.log("vendorCode: " + vendorCode + " is not found on the visibleIndex: " + visibleIndex);
            }
        }
        throw new NoSuchElementException("vendor code: " + vendorCode + " is not found on the swipeIndex " + swipeIndex);
    }

    private Locator colorVendor(String colorHex) {
        return new Locator("COLOR_VENDOR_" + colorHex, AppiumBy.name("color_vendor_" + colorHex.toLowerCase()));
    }

    @Override
    public void changeLocation(String newLocation) {
        String separator = "/";
        String[] splitData = splitLanguageData(newLocation, separator);
        String locationId = splitData[0];
        String locationEn = splitData[1];

        driverTools.click(CHANGE);
        driverTools.click(SEARCH_FIELD_BUTTON);

        driverTools.waitForElementToAppear(SEARCH_FIELD_TEXT).orElseThrow();
        driverTools.sendKeys(SEARCH_FIELD_TEXT, locationId);
        driverTools.hideKeyboard();

        // search result elements
        driverTools.click(searchList(locationId, locationEn));
    }

    @Override
    public void changeLocation(String newLocation, String locationType) {
        driverTools.click(CHANGE);
        driverTools.click(SEARCH_FIELD_BUTTON);

        driverTools.waitForElementToAppear(SEARCH_FIELD_TEXT).orElseThrow();
        driverTools.sendKeys(SEARCH_FIELD_TEXT, newLocation);
        driverTools.hideKeyboard();

        WebElement searchItem = driverTools.swipeToElement(searchType(newLocation, locationType), Swipe.UP, 3).orElseThrow();
        driverTools.click(searchItem);
    }

    @Override
    public String changeNearMeLocation() {
        driverTools.click(CHANGE);
        driverTools.click(SEARCH_FIELD_BUTTON);

        driverTools.waitForElementToAppear(SEARCH_FIELD_TEXT).orElseThrow();
        driverTools.click(LOCATION_BUTTON);

        driverTools.waitForElementToPresence(SEARCH_FIELD_SECTION).orElseThrow();
        String searchFieldText = driverTools.getValueAttribute(SEARCH_FIELD_SECTION, "label");
        driverTools.click(SUBMIT_BUTTON);

        driverTools.waitForElementToPresence(CONTINUE_ALLOW_LOCATION).ifPresent(driverTools::clickElementByCoordinate);
        driverTools.waitForElementToPresence(CONTINUE_ALLOW_LOCATION).ifPresent(driverTools::click);

        IOSDriver iosDriver = (IOSDriver) driver;
        iosDriver.setSetting("defaultActiveApplication", "com.apple.springboard");
        driverTools.waitForElementToAppear(PERMISSION_TYPE_ALLOW_ONCE).ifPresent(driverTools::click);
        iosDriver.setSetting("defaultActiveApplication", "auto");

        return searchFieldText;
    }

    @Override
    public void clickSubmit() {
        driverTools.click(SUBMIT_BUTTON);
    }

    @Override
    public String getCurrentLocation() {
        driverTools.waitForElementToAppear(HOTEL_CARD);
        driverTools.waitForElementToAppear(CURRENT_LOCATION).orElseThrow();
        String currentLocation = String.valueOf(driverTools.getText(CURRENT_LOCATION));
        currentLocation = currentLocation.substring(9, currentLocation.length() - 1);
        logger.log("Current location: " + currentLocation);
        return currentLocation;
    }

    @Override
    public boolean verifyLoopingHeaderQuickFilter(int index) {
        // not yet implemented
        return false;
    }

    @Override
    public String getPriceDescription() {
        logger.log("this is method for Android only");
        return "";
    }

    @Override
    public void clickFilterButton() {
        driverTools.waitForElementToPresence(HOTEL_NAME, 30).orElseThrow();
        driverTools.waitForElementToAppear(SRP_FILTER_BAR);
        if (!driverTools.isElementVisible(FILTER_BUTTON)) {
            for (int i = 0; i < 5; i++) {
                if (driverTools.waitForElementToPresence(SRP_FILTER_BAR, 2).isEmpty()) {
                    break;
                }
                driverTools.swipeByDistance(SRP_FILTER_BAR, Swipe.RIGHT, 0.8);
                if (driverTools.isElementVisible(FILTER_BUTTON)) {
                    break;
                }
            }
        }
        if (driverTools.isElementVisible(FILTER_BUTTON)) {
            driverTools.clickElementByCoordinate(FILTER_BUTTON);
        } else if (driverTools.isElementVisible(FILTER_BUTTON_BADGE)) {
            driverTools.clickElementByCoordinate(FILTER_BUTTON_BADGE);
        } else {
            throw new org.openqa.selenium.NoSuchElementException("Filter button is not visible after swiping");
        }
    }

    @Override
    public void swipeToFilter(String filterName) {
        if (filterName.toLowerCase().contains("hotel star")) {
            driverTools.swipeToPresentedElement(STAR_FILTER_SECTION, Swipe.UP, 10);
        } else if (filterName.toLowerCase().contains("refund") || filterName.toLowerCase().contains("reschedule")) {
            driverTools.swipeToPresentedElement(REFUND_RESCHEDULE_TOGGLE, Swipe.UP, 5);
            driverTools.swipeUp();
        } else {
            driverTools.swipeToPresentedElement(getFilterSectionLocator(filterName), Swipe.UP, 10);
        }
    }

    @Override
    public void selectHotelStarFilter(String starsString) {
        String[] selectedStarsFilter = splitLanguageData(starsString, "-");
        if (selectedStarsFilter[0].equals(selectedStarsFilter[1])) {
            selectedStarsFilter = new String[]{selectedStarsFilter[0]};
        }
        for (String starString : selectedStarsFilter) {
            starString = starString.equalsIgnoreCase("0") ? "Unrated" : starString;
            WebElement stars = driverTools.swipeToPresentedElement(getHotelStarSelectionLocator(starString), Swipe.UP, 3).orElseThrow();
            driverTools.clickElementByCoordinate(stars);
        }
    }

    @Override
    public void selectHotelPaymentMethod(String paymentMethod) {
        WebElement checkbox = driverTools.swipeToPresentedElement(getCheckboxLocator(paymentMethod), Swipe.UP, 3).orElseThrow();
        driverTools.swipeElementToMidScreen(checkbox);
        driverTools.click(checkbox);
    }

    private Locator getCheckboxLocator(String checkboxTitle) {
        String checkboxTitleEnglish = splitLanguageData(checkboxTitle, "/")[0];
        String checkboxTitleBahasa = splitLanguageData(checkboxTitle, "/")[1];
        return new Locator("CHECKBOX", AppiumBy.xpath("//*[contains(@name,'TVCell<TACMFilterGroupAccordionView>')]/XCUIElementTypeStaticText[contains(@label,'" + checkboxTitleEnglish + "') or contains(@label,'" + checkboxTitleBahasa + "')]"));
    }

    @Override
    public void clickFilterSubmit() {
        WebElement filterSubmit = driverTools.waitForElementToPresence(FILTER_SUBMIT_BUTTON, 20).orElseThrow();
        driverTools.clickElementByCoordinate(filterSubmit);
        driverTools.swipeDown(2);
    }

    private Locator getGeneralFilterBenefitLocator(String benefitType) {
        String[] benefitTypeArray = splitLanguageData(benefitType, "/");
        return new Locator("BENEFIT_LOCATOR", AppiumBy.xpath("//XCUIElementTypeStaticText[(@name='" + benefitTypeArray[0] + "' or @name='" + benefitTypeArray[1] + "') and @visible='true']/preceding-sibling::XCUIElementTypeOther[@name='switch'][last()]"));
    }

    public void selectBenefit(String benefitType) {
        driverTools.clickElementByCoordinate(getGeneralFilterBenefitLocator(benefitType));
        driverTools.click(FILTER_SUBMIT_BUTTON);
    }

    /**
     * Generic method to get data on specific attribute of a hotel/homes card in SRP
     *
     * @param cardAttribute attribute of the card want to be scanned ex. "price"
     * @param totalHotel    number of card want to be scanned
     * @param param         is additional data that need to be added to the array returned
     * @return with data scanned through the page
     */
    @Override
    public String[] getHotelCardList(String cardAttribute, int totalHotel, String[] param) {
        driverTools.waitForElementToPresence(HOTEL_NAME).orElseThrow();
        List<String> resultList = new ArrayList<>();
        List<WebElement> hotelCardList = driverTools.elements(HOTEL_NAME).orElseThrow();
        int hotelCardCount = hotelCardList.size();

        if (hotelCardCount < totalHotel) {
            totalHotel = hotelCardCount;
            logger.softWarning("SRP only displayed " + hotelCardCount + " card");
        }

        if (totalHotel > 0) {
            for (int index = 1; index <= totalHotel; index++) {
                hotelCardList = driverTools.elements(HOTEL_NAME).orElseThrow();
                String hotelCardNameText = driverTools.getText(hotelCardList.get(index - 1));
                resultList.add(hotelCardNameText);
                resultList.add(getCardData(cardAttribute, hotelCardNameText));
            }
            for (String e : param) {
                resultList.add(e);
            }
            logger.log("resultList -> " + resultList);
        } else if (totalHotel <= 0) {
            logger.fail("Testdata for totalHotel is not >0");
            throw new InvalidParameterException();
        } else if (hotelCardCount == 0) {
            logger.fail("There is no result on SRP");
        }

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

    /**
     * @param cardAttribute which attribute on product card that will be asserted
     * @param totalHotel    number of product card that will be asserted
     * @param param         necessary parameters needed for assertion
     * @return assertion of all product card
     */
    @Override
    public boolean checkHotelCardList(String cardAttribute, int totalHotel, String[] param) {
        driverTools.waitForElementToDisappear(NOT_FOUND_CONTAINER, 20);
        driverTools.isElementGone(NOT_FOUND_CONTAINER);
        List<WebElement> hotelCardList = driverTools.waitForPresenceElementsList(HOTEL_NAME, 20).orElseThrow();
        int hotelCardCount = hotelCardList.size();
        logger.log("hotelCardCount -> " + hotelCardCount);

        if (hotelCardCount < totalHotel) {
            totalHotel = hotelCardCount;
            logger.softWarning("SRP only displayed " + hotelCardCount + " card");
        }

        //check totalHotel validity
        if (totalHotel > 0) {
            //assertion for each product card
            for (int index = 1; index <= totalHotel; index++) {
                logger.log("index -> " + index);
                hotelCardList = driverTools.waitForPresenceElementsList(HOTEL_NAME).orElseThrow();
                if (!driverTools.isElementDisplayed(hotelCardList.get(index - 1))) {
                    driverTools.swipeToElement(getHotelName(index), Swipe.UP, 3);
                    hotelCardList = driverTools.waitForPresenceElementsList(HOTEL_NAME).orElseThrow();
                }
                String hotelCardNameText = driverTools.getText(hotelCardList.get(index - 1));
                driverTools.swipeToElement(hotelCardPriceLocator(hotelCardNameText), Swipe.UP, 5).orElseThrow();
                handleVerifyCheck(cardAttribute, hotelCardNameText, param);
            }

        } else if (totalHotel <= 0) {
            logger.fail("Testdata for totalHotel is not >0");
            throw new InvalidParameterException();
        } else if (hotelCardCount == 0) {
            logger.fail("There is no result on SRP");
            return false;
        }

        return true;
    }

    @Override
    public String getHotelNameList(String list) {
        driverTools.waitForElementToPresence(HOTEL_NAME, 5).orElseThrow();
        String hotelNameList;
        if (list.isEmpty() || list.isBlank()) {
            driverTools.swipeToElement(HOTEL_NAME, Swipe.UP, 5);
            hotelNameList = normalizeNonBreakingSpaceText(driverTools.getText(HOTEL_NAME).orElseThrow());
        } else {
            int index = getIndexFromList(list);
            final Locator HOTEL_NAME_SELECTED = getHotelName(index);
            WebElement selectedHotelNameElement = driverTools.swipeToElement(HOTEL_NAME_SELECTED, Swipe.UP, 5).orElseThrow();
            hotelNameList = normalizeNonBreakingSpaceText(driverTools.getText(selectedHotelNameElement));
        }
        return hotelNameList;
    }

    @Override
    public String getHotelLocationList(String list) {
        driverTools.waitForElementToPresence(HOTEL_CARD).orElseThrow();
        WebElement hotelLocation;
        if (list.isEmpty() || list.isBlank()) {
            hotelLocation = driverTools.waitForElementToPresence(HOTEL_LOCATION).orElseThrow();
        } else {
            int index = getIndexFromList(list);
            hotelLocation = driverTools.waitForElementToPresence(getHotelLocation(index)).orElseThrow();
        }
        return String.valueOf(driverTools.getText(hotelLocation));
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
    public boolean isSearchResultShown() {
        swipeToFirstCard();
        Optional<WebElement> checkHotelCard = driverTools.waitForElementToAppear(HOTEL_CARD, 20);
        if (checkHotelCard.isPresent()) {
            logger.log("HOTEL_CARD is exist");
        } else {
            logger.warn("HOTEL_CARD is not exist");
        }
        Optional<WebElement> checkFilter = driverTools.waitForElementToAppear(SRP_FILTER_BAR);
        if (checkFilter.isPresent()) {
            logger.log("SRP_FILTER_BAR is exist");
        } else {
            logger.warn("SRP_FILTER_BAR is not exist");
        }
        Optional<WebElement> changeButton = driverTools.waitForElementToAppear(CHANGE);
        if (changeButton.isPresent()) {
            logger.log("Change button exist");
        } else {
            logger.warn("Change button not exist");
        }
        Optional<WebElement> hotelImage = driverTools.waitForElementToAppear(HOTEL_CARD_IMAGE);
        if (hotelImage.isPresent()) {
            logger.log("hotel image is exist");
        } else {
            logger.warn("Hotel image is not exist");
        }
        Optional<WebElement> hotelPrice = driverTools.swipeToElement(FIRST_CARD_HOTEL_PRICE, Swipe.UP, 5);
        if (hotelPrice.isPresent()) {
            logger.log("hotel price is exist");
        } else {
            logger.warn("Hotel price is not exist");
        }

        return checkHotelCard.isPresent()
                && checkFilter.isPresent()
                && changeButton.isPresent()
                && hotelImage.isPresent()
                && hotelPrice.isPresent();
    }

    @Override
    public boolean isHeaderSearchResultShown() {
        Optional<WebElement> changeButton = driverTools.waitForElementToPresence(CHANGE);
        if (changeButton.isPresent()) {
            logger.log("Change button is exist");
        } else {
            logger.fail("Change button is not exist");
        }

        Optional<WebElement> hotelCardName = driverTools.waitForElementToPresence(HOTEL_NAME);
        Optional<WebElement> srpContent = hotelCardName.isPresent() ?
                hotelCardName : driverTools.waitForElementToAppear(NOT_FOUND_CONTAINER);
        if (srpContent.isPresent()) {
            logger.log("SRP_CONTENT is exist");
        } else {
            logger.fail("SRP_CONTENT is not exist");
        }

        return changeButton.isPresent() && srpContent.isPresent();
    }

     /*
     docs:
     simple methods below can be used to get data from 1st hotel card list on srp,
     if test cases is simple and does not require to do random pick.
     for specific number card pick or random card please use/create method like getHotelLocationList(String list).
     */

    @Override
    public void selectFirstHotelCard() {
        driverTools.waitForElementToAppear(CLOSE_GIVE_RATING_POPUP, 3).ifPresent(driverTools::click);
        goToFirstHotelCard();
        // hotelName StaticText is accessible=false on new SRP — tap the accessible card cell
        WebElement card = driverTools.waitForElementToPresence(FIRST_CARD_HOTEL, 15).orElseThrow();
        try {
            driverTools.swipeElementToMidScreen(card);
        } catch (Exception e) {
            logger.log("swipeElementToMidScreen skipped: " + e.getMessage());
        }
        card = driverTools.waitForElementToPresence(FIRST_CARD_HOTEL, 10).orElseThrow();
        driverTools.clickElementByCoordinate(card);
    }

    @Override
    public void selectFirstHotelCardFromPrice() {
        driverTools.waitForElementToPresence(FIRST_CARD_HOTEL_PRICE).orElseThrow();
        WebElement webElement = driverTools.swipeToElement(FIRST_CARD_HOTEL_PRICE, Swipe.UP, 2).orElseThrow();
        driverTools.click(webElement);
    }

    @Override
    public Optional<WebElement> goToFirstHotelCard() {
        // mapsFloatingButton is intermittent — do not block SRP readiness on it
        driverTools.waitForElementToPresence(SEE_MAP_BUTTON, 3);
        driverTools.waitForElementToPresence(SKIP_POPUP, 3).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(CLOSE_GIVE_RATING_POPUP, 2).ifPresent(driverTools::click);

        Optional<WebElement> card = driverTools.waitForElementToPresence(FIRST_CARD_HOTEL, 30);
        if (card.isEmpty()) {
            card = driverTools.swipeToElement(FIRST_CARD_HOTEL, Swipe.UP, 5);
        }
        if (card.isEmpty()) {
            // Price is always present on loaded cards
            driverTools.waitForElementToPresence(FIRST_CARD_HOTEL_PRICE, 15).orElseThrow();
            card = driverTools.waitForElementToPresence(FIRST_CARD_HOTEL, 10);
        }
        return card.isPresent() ? card : driverTools.swipeToElement(FIRST_CARD_HOTEL_NAME, Swipe.UP, 3);
    }

    @Override
    public String getFirstHotelName() {
        goToFirstHotelCard();
        WebElement webElement = driverTools.waitForElementToPresence(FIRST_CARD_HOTEL_NAME, 10).orElseThrow();
        return driverTools.getText(webElement);
    }

    @Override
    public String getFirstHotelLocation() {
        driverTools.waitForElementToAppear(FIRST_CARD_HOTEL_LOCATION);
        if (!driverTools.isElementDisplayed(FIRST_CARD_HOTEL_LOCATION)) {
            driverTools.swipeUp();
        }
        return normalizeNonBreakingSpaceText(driverTools.getText(FIRST_CARD_HOTEL_LOCATION).orElseThrow());
    }

    @Override
    public String getFirstHotelPrice() {
        WebElement hotelPrice;
        try {
            hotelPrice = driverTools.waitForElementToPresence(FIRST_CARD_HOTEL_PRICE).orElseThrow();
        } catch (NoSuchElementException e) {
            logger.log("FIRST_CARD_HOTEL_PRICE not found, trying to swipe to element");
            driverTools.swipeToElement(FIRST_CARD_HOTEL_PRICE, Swipe.UP, 2);
            hotelPrice = driverTools.waitForElementToPresence(FIRST_CARD_HOTEL_PRICE).orElseThrow();
        }

        return driverTools.getText(hotelPrice);
    }

    @Override
    public void filterSortBy(String filterType, String hotelCount) {
        // method no implemented
    }

    @Override
    public Boolean isFirstHotelNameDisplayed() {
        driverTools.waitForElementToPresence(FIRST_CARD_HOTEL, 30).orElseThrow();
        WebElement hotelName = driverTools.swipeToElement(FIRST_CARD_HOTEL_NAME, Swipe.UP, 2).orElseThrow();
        return driverTools.isElementDisplayed(hotelName);
    }

    @Override
    public Boolean isFirstHotelPriceDisplayed() {
        driverTools.waitForElementToPresence(FIRST_CARD_HOTEL, 30).orElseThrow();
        WebElement priceHotel = driverTools.swipeToElement(FIRST_CARD_HOTEL_PRICE, Swipe.UP, 3).orElseThrow();
        return driverTools.isElementDisplayed(priceHotel);
    }

    @Override
    public boolean getFirstHotelStar() {
        return driverTools.waitForElementToPresence(FIRST_CARD_HOTEL, 30).isPresent();
    }

    @Override
    public String getFirstHotelReview() {
        driverTools.waitForElementToPresence(FIRST_CARD_HOTEL, 30).orElseThrow();
        return driverTools.getText(FIRST_CARD_HOTEL_REVIEW).orElseThrow();
    }

    private Locator getStarLocator(int star) {
        return new Locator("START_LOCATOR_FILTER", AppiumBy.xpath("//XCUIElementTypeOther[@name='" + star + "']//*[@name='tds_ic_review_star_full']"));
    }

    @Override
    public void selectFilterStar(String star) {
        driverTools.swipeUntilElementVisible(STAR_FILTER_BUTTON, SRP_FILTER_BAR, Swipe.LEFT, 0.8, 5);
        driverTools.click(STAR_FILTER_BUTTON);
        driverTools.waitForElementToAppear(getStarLocator(Integer.parseInt(star)));
        logger.log("random index -> " + Integer.parseInt(star));
        driverTools.click(getStarLocator(Integer.parseInt(star)));
        driverTools.click(FILTER_SUBMIT_BUTTON);
    }

    public void selectFilterSortBy(String filterType) {
        driverTools.waitForElementToPresence(HOTEL_NAME, 30).orElseThrow();
        driverTools.swipeUntilElementVisible(SORT_BY_BUTTON, SRP_FILTER_BAR, Swipe.RIGHT, 0.8, 5);
        driverTools.click(SORT_BY_BUTTON);
        if (driverTools.waitForElementToAppear(SORT_BY_LOWEST_PRICE).isEmpty()) {
            driverTools.click(SORT_BY_BUTTON);
        }
        switch (filterType) {
            case "lowest price" -> {
                driverTools.click(SORT_BY_LOWEST_PRICE);
            }
            case "highest price" -> {
                driverTools.click(SORT_BY_HIGHEST_PRICE);
            }
            case "highest rating" -> {
                driverTools.click(SORT_BY_HIGHEST_RATING);
            }
            case "highest star" -> {
                driverTools.click(SORT_BY_HIGHEST_STAR);
            }
            case "recommended" -> {
                driverTools.click(SORT_BY_RECOMMENDATION);
            }
            default -> {
                throw new RuntimeException("wrong test data 'filter', please check testdata.xlsx");
            }
        }
        driverTools.waitForElementToAppear(SORT_BY_BUTTON_BADGED);
    }

    /**
     * method review: default number of hotel that suppose to be compared is 5, method works by comparing
     * each hotel price from 1st index(hotel price) to 2nd index, update the isValid variable (return value), and then swipe
     * to compare 2nd index to 3rd index.
     */

    @Override
    public Boolean isSortByLowestPriceValid(String totalHotel) {
        driverTools.waitForElementToPresence(HOTEL_NAME, 30).orElseThrow();
        int totalHotelInt = Integer.parseInt(totalHotel);
        boolean isValid = false;
        for (int i = 1; i < totalHotelInt; i++) {
            driverTools.waitForElementToPresence(getHotelPriceLocator(i), 30).orElseThrow();
            String currentHotelPrice = driverTools.getValueAttribute(getHotelPriceLocator(i), "label");
            currentHotelPrice = currentHotelPrice.replace("?", Integer.toString(UNAVAILABLE_PRICE));
            double currentHotelPriceNum = convertPriceSafely(currentHotelPrice);

            WebElement nextHotelPriceLocator = driverTools.swipeToElement(getHotelPriceLocator(i + 1), Swipe.UP, 3).orElseThrow();
            String nextHotelPrice = driverTools.getText(nextHotelPriceLocator);
            nextHotelPrice = nextHotelPrice.replace("?", Integer.toString(UNAVAILABLE_PRICE));
            double nextHotelPriceNum = convertPriceSafely(nextHotelPrice);

            if (currentHotelPriceNum == UNAVAILABLE_PRICE || nextHotelPriceNum == UNAVAILABLE_PRICE)
                isValid = true;
            else
                isValid = currentHotelPriceNum <= nextHotelPriceNum;

            logger.log("compare index " + i + " and index " + (i + 1) + "\n" +
                    "1st hotel price :" + currentHotelPrice + "\n" +
                    "2nd hotel price :" + nextHotelPrice + "\n" +
                    "isValid status :" + isValid + "\n" +
                    "swiping to next hotel locator"
            );

            if (!isValid) {
                return false;
            }

            driverTools.swipeUp(nextHotelPriceLocator);
        }
        return isValid;
    }

    @Override
    public void openPriceFilter() {
        if (driverTools.waitForElementToAppear(PRICE_FILTER).isEmpty()) {
            driverTools.waitForElementToPresence(SRP_FILTER_BAR);
            driverTools.swipeUntilElementVisible(PRICE_FILTER, SRP_FILTER_BAR, Swipe.LEFT, 0.8, 3);
        }
        driverTools.click(PRICE_FILTER);
    }

    private Locator getFilterSectionLocator(String filterName) {
        if (filterName.toLowerCase().contains("loyalty")) {
            return LOYALTY_FILTER_TOGGLE;
        } else if (filterName.contains("/")) {
            String filterNameEnglish = splitLanguageData(filterName, "/")[0];
            String filterNameBahasa = splitLanguageData(filterName, "/")[1];
            return new Locator("SELECTED_FILTER_SECTION", AppiumBy.xpath("//XCUIElementTypeCell[contains(@label,'" + filterNameBahasa + "') or contains(@label,'" + filterNameEnglish + "')]"));
        } else {
            return new Locator("SELECTED_FILTER_SECTION", AppiumBy.xpath("//XCUIElementTypeCell[contains(@label,'" + filterName + "')]"));
        }
    }

    private Locator getHotelStarSelectionLocator(String starString) {
        return new Locator("CHIP_FILTER_HOTEL_STAR", AppiumBy.xpath("//*[contains(@name,'groupHotelStarRatings')]//*[@label='" + starString + "']"));
    }

    private String getHotelCardStar(String hotelCardNameText) {
        final Locator hotelCardStar = new Locator("HOTEL_CARD_STAR", AppiumBy.xpath("//*[@name='hotelNameLabel']//*[(@name='TACMLabelWithAttachmentView.label' or @name='hotelName') and contains(@value,'" + hotelCardNameText + "')]/parent::*/following-sibling::*[contains(@name,'starItemView')]"));
        return Integer.toString(driverTools.elements(hotelCardStar).get().size());
    }

    private String getHotelAccomType(String hotelCardNameText) {
        final Locator accomType = new Locator("ACCOMMODATION_TYPE", AppiumBy.xpath("//*[@name='hotelNameLabel']//*[(@name='TACMLabelWithAttachmentView.label' or @name='hotelName') and contains(@value,'" + hotelCardNameText + "')]/parent::*/following-sibling::XCUIElementTypeStaticText[@name='hotelCategoryLabel']"));
        return driverTools.getText(accomType).orElseThrow();
    }

    private void accommTypeCheck(String hotelCardName, String[] param) {
        String type = getHotelAccomType(hotelCardName);
        logger.log(hotelCardName + " is a " + type);
        Assertion.assertThat("Assert type of " + hotelCardName + " is already valid", Arrays.stream(param).anyMatch(type::contains), is(true));
    }

    /**
     * Gateway method for assertion, passing data to the correct product card's attribute
     *
     * @param cardAttribute product card's attribute what will be asserted
     * @param hotelCardName name of the product
     * @param param         parameters for assertion
     */

    private void handleVerifyCheck(String cardAttribute, String hotelCardName, String[] param) {
        for (String p : param) {
            logger.log("param -> " + p);
        }
        switch (cardAttribute.toLowerCase()) {
            case "multiple" -> hotelCardMultipleFilterCheck(hotelCardName, param);
            case "star" -> hotelCardStarCheck(hotelCardName, param);
            case "price" -> hotelCardPriceCheck(hotelCardName, param);
            case "total_price" -> hotelCardTotalPriceCheck(hotelCardName, param);
            case "bedrooms" ->
                    new HomesSearchResultPageIOS<>(session).homesCardCheckTotalBedrooms(hotelCardName, param); //used for homes test case only
            case "benefit" -> hotelCardBenefitCheck(hotelCardName, param);
            case "area" -> hotelCardAreaCheck(hotelCardName, param);
            case "property rating", "guest rating" -> hotelCardPropertyRatingCheck(hotelCardName, param);
            case "facility" -> hotelCardFacilityCheck(hotelCardName, param);
            case "name" -> hotelCardNameCheck(hotelCardName, param);
            case "property type" -> accommTypeCheck(hotelCardName, param);
            default -> {
                logger.warn("There is no '" + cardAttribute + "' type of check");
                throw new NoSuchMethodError();
            }
        }
    }

    /**
     * Method to check multiple attribute of one hotel card at one time
     *
     * @param hotelCardName hotel name displayed on card
     * @param param         the composition of array is must [{series of filterTypes ex. Price Range},separator,{series of filterOption ex. <500}]
     */
    private void hotelCardMultipleFilterCheck(String hotelCardName, String[] param) {
        int separatorIndex = Arrays.asList(param).indexOf("separator");

        for (int i = 0; i < separatorIndex; i++) {
            int paramIndex = i + separatorIndex + 1;

            switch (param[i]) {
                case "Price Display/Tampilan Harga" -> {
                    throw new NotImplementedException();
                }
                case "Price Range per night/Rentang Harga per malam" -> {
                    hotelCardPriceCheck(hotelCardName, splitLanguageData(convertPriceParam(param[paramIndex]), "-"));
                }
                case "Number of bedrooms/Jumlah kamar tidur" -> {
                    new HomesSearchResultPageIOS<>(session).homesCardCheckTotalBedrooms(hotelCardName, new String[]{param[paramIndex]});
                }
                case "Areas/Area" -> {
                    hotelCardAreaCheck(hotelCardName, new String[]{param[paramIndex]});
                }
                case "Facilities/Fasilitas" -> {
                    hotelCardFacilityCheck(hotelCardName, new String[]{param[paramIndex]});
                }
                case "Property Rating/Rating Properti", "Guest Rating" -> {
                    hotelCardPropertyRatingCheck(hotelCardName, new String[]{param[paramIndex]});
                }
                case "Accomodation Types/Tipe Akomodasi" -> {
                    hotelCardBenefitCheck(hotelCardName, splitLanguageData(param[paramIndex], "/"));
                }
            }
        }
    }


    /**
     *
     * @param homesName name of the product card (can be use either hotel or homes)
     * @return locator of 'other benefits' label on the product card
     */
    public Locator getHomesOtherBenefitsLabelElement(String homesName) {
        return new Locator("HOMES_OTHER_BENEFITS_LABEL", AppiumBy.xpath("//XCUIElementTypeStaticText[(@name='TACMLabelWithAttachmentView.label' or @name='hotelName') and contains(@value, '" + homesName + "')]/parent::*/following-sibling::*[@name='roomStatisticView']/*/*[@name='TACMLabelWithAttachmentView.label']"));
    }

    private Locator getFacilityLocatorDetail(String facility) {
        String[] facilityArray = splitLanguageData(facility, "/");
        return new Locator("FACILITY_CHECK", AppiumBy.xpath("//*[@name='TVCell<TNMDetailFacilityView>' and (normalize-space(contains(@label,'" + facilityArray[0] + "')) or normalize-space(contains(@label,'" + facilityArray[1] + "')))]"));
    }

    private Locator getHotelLocatorByName(String hotelCardName) {
        return new Locator("OPEN_" + hotelCardName + "", AppiumBy.xpath("//*[@name='hotelNameLabel']//*[(@name='TACMLabelWithAttachmentView.label' or @name='hotelName') and @label='" + hotelCardName + "']"));
    }

    private void hotelCardNameCheck(String hotelCardName, String[] validationData) {
        logger.log("hotelCardName -> " + hotelCardName);
        Stream<String> validationDataStream = Arrays.stream(validationData);
        Boolean result = true;
        if (!(validationDataStream.anyMatch(x -> x.equals(hotelCardName)))) {
            result = false;
        }
        Assertion.assertThat("hotel name in list", result, is(true));
    }

    private void hotelCardFacilityCheck(String hotelCardName, String[] param) {
        logger.log("****Opening " + hotelCardName + "****");
        WebElement hotelCards = driverTools.swipeToElement(getHotelLocatorByName(hotelCardName), Swipe.UP, 10).orElseThrow();
        driverTools.click(hotelCards);

        logger.log("****clicking See all Button For facilities in PDP****");
        WebElement seeAllPDP = driverTools.swipeToElement(getSeeAllBtnFilterLocator("Facilities/Fasilitas"), Swipe.UP, 4).orElseThrow();
        driverTools.click(seeAllPDP);

        logger.log("****Searching for selected facilities****");
        for (int i = 0; i < param.length; i++) {
            Locator faciltiyCheck = getFacilityLocatorDetail(param[i]);
            driverTools.swipeToElement(faciltiyCheck, Swipe.UP, 5);
            driverTools.swipeToElement(faciltiyCheck, Swipe.DOWN, 5);

            Assertion.assertThat("Facility `" + param[i] + "` on `" + hotelCardName + "` is displayed", driverTools.isElementDisplayed(faciltiyCheck), is(true));
        }
        logger.log("****back to SRP****");
        driverTools.click(CANCEL_BUTTON);
        driverTools.waitForElementToPresence(DetailPageIOS.BACK_BUTTON).orElseThrow();
        driverTools.click(DetailPageIOS.BACK_BUTTON);
    }

    private Locator hotelCardBenefitLocator(String hotelCardName, String[] param) {
        return new Locator("BENEFIT_LABEL", AppiumBy.xpath("//*[(contains(@name,'TACMLabelWithAttachmentView.label') or @name='hotelName') and contains(@value,'" + hotelCardName + "')]/parent::*/following-sibling::*[@name='roomStatisticView' or @name='srpCard.highlightedBenefits']/*[normalize-space(contains(@label,'" + param[0] + "')) or normalize-space(contains(@label,'" + param[1] + "'))]"));
    }

    private void hotelCardBenefitCheck(String hotelCardName, String[] param) {
        WebElement benefitLabelElement = driverTools.swipeToElement(hotelCardBenefitLocator(hotelCardName, param), Swipe.UP, 4).orElseThrow();
        boolean isBenefitDisplayed = driverTools.isElementDisplayed(benefitLabelElement);

        Assertion.assertThat("Accomodation Type/Benefit '" + String.join("/", param) + "' is displayed", isBenefitDisplayed, is(true));
    }

    private void hotelCardPriceCheck(String hotelCardName, String[] param) {
        Integer hotelCardPrice = getHotelCardPrice(hotelCardName);
        // if the hotel is full which the value is changed to 0 on getHotelCardPrice, skip assertion
        if (hotelCardPrice != 0) {
            int min = Integer.parseInt(param[0]);
            logger.log("hotelCardPrice '" + hotelCardPrice + "'");

            if (param[1].equalsIgnoreCase("inf")) {
                Assertion.assertThat("Assert Hotel Price of '" + hotelCardName + "' is already valid", hotelCardPrice >= min, is(true));
            } else {
                int max = Integer.parseInt(param[1]);
                Assertion.assertThat("Assert Hotel Price of '" + hotelCardName + "' is already valid", hotelCardPrice >= min && hotelCardPrice <= max, is(true));
            }
        } else {
            logger.log("hotel is full");
        }
    }

    /**
     * This method is for checking filter `Total Price` on Hotel/Homes SRP result
     *
     * @param hotelCardName is the accomodation name ex. "Puri Casablanca ..."
     * @param param         usage is [hotelName1,hotelNormalPrice1,hotelName2,hotelNormalPrice2, ... ,totalNights]
     */
    private void hotelCardTotalPriceCheck(String hotelCardName, String[] param) {
        int index = Arrays.asList(param).indexOf(hotelCardName);
        int hotelCardPrice = Integer.parseInt(param[index + 1]);
        int hotelCardTotalPrice = getHotelCardPrice(hotelCardName);
        int totalNights = Integer.parseInt(param[param.length - 1]);

        if (hotelCardPrice != 0) {
            logger.log("hotelCardPrice '" + hotelCardPrice + "'");
            logger.log("totalNights '" + totalNights + "'");
            logger.log("hotelCardTotalPrice '" + hotelCardTotalPrice + "'");

            Assertion.assertThat("Assert Hotel Price of '" + hotelCardName + "' is already valid", hotelCardPrice * totalNights == hotelCardTotalPrice, is(true));
        } else {
            logger.log("hotel is full");
        }
    }

    private Integer getHotelCardPrice(String hotelCardName) {
        WebElement hotelPriceElement = driverTools.swipeToElement(hotelCardPriceLocator(hotelCardName), Swipe.UP, 3).orElseThrow();
        String hotelPrice = driverTools.getText(hotelPriceElement);
        if (hotelPrice.equals("Kamar Penuh") || hotelPrice.equals("Fully Booked")) {
            logger.softWarning("Current hotel is full, price changed to IDR 0 to avoid exception");
            hotelPrice = "IDR 0";
        }
        return Math.toIntExact(Math.round(convertPrice(hotelPrice)));
    }

    private Locator hotelCardPriceLocator(String hotelCardName) {
        return new Locator("HOTEL_CARD_PRICE", AppiumBy.xpath("//XCUIElementTypeCell[.//*[@name='hotelName' and (contains(@label,'" + hotelCardName + "') or contains(@value,'" + hotelCardName + "'))]]//*[@name='mainPrice'] | //*[@name='TACMLabelWithAttachmentView.label' and contains(@value,'" + hotelCardName + "')]/parent::*[@name='hotelNameLabel']/following-sibling::XCUIElementTypeStaticText[@name='mainPrice']"));
    }

    private Locator getCheckFlowerNHALocator(String hotelName) {
        return new Locator("HOTEL_CONTAINS_FLOWER_RATING", AppiumBy.xpath("//*[@name='hotelNameLabel']//*[(@name='TACMLabelWithAttachmentView.label' or @name='hotelName') and @label='" + hotelName + "']/parent::*/following-sibling::*[contains(@name,'starItemView')]"));
    }

    private void hotelCardStarCheck(String hotelCardName, String[] param) {
        boolean isHotelContainsNHAStarsRating = driverTools.isElementDisplayed(getCheckFlowerNHALocator(hotelCardName));
        if (isHotelContainsNHAStarsRating)
            return;
        String hotelCardStar = getHotelCardStar(hotelCardName);
        logger.log("'" + hotelCardName + "' is having '" + hotelCardStar + "' stars with filter");
        Assertion.assertThat("Assert Hotel Star of '" + hotelCardName + "' is already valid", Arrays.stream(param).toList().contains(hotelCardStar), is(true));
    }

    @Override
    public String selectPriceFilterByRange(String priceRange, String currency) {
        String[] priceRangeSplit = minMaxPrice(priceRange, currency);
        String minPrice = priceRangeSplit[0];
        String maxPrice = priceRangeSplit[1];
        driverTools.click(MIN_PRICE_FILTER_INPUT);
        driverTools.clear(MIN_PRICE_FILTER_INPUT);
        driverTools.sendKeys(MIN_PRICE_FILTER_INPUT, minPrice);
        driverTools.click(MAX_PRICE_FILTER_INPUT);
        driverTools.clear(MAX_PRICE_FILTER_INPUT);
        driverTools.sendKeys(MAX_PRICE_FILTER_INPUT, maxPrice);
        driverTools.hideKeyboard();
        logger.log("input price range with min price '" + minPrice + "' and max price '" + maxPrice + "'");
        return minPrice + "-" + maxPrice;
    }

    /**
     * method review: method is used on srp to verify 'filtered hotel price by range' with asserted 'price range'.
     * default number of hotel that suppose to be compared with price range is 5 ('totalHotel').
     * method is using loop and comparing the hotel price with price range condition('priceRange') and update the isCorrect
     * variable if hotel price is not valid.
     */

    @Override
    public boolean isPriceFilterByRangeCorrect(String priceRange, String totalHotel) {
        boolean isCorrect = true;
        driverTools.waitForElementToPresence(PRICE_FILTER_SIGN, 30).orElseThrow();

        List<WebElement> hotelList = driverTools.waitForPresenceElementsList(HOTEL_PRICE).orElseThrow();
        int actualTotalHotel = hotelList.size();
        logger.log("actualTotalHotel -> " + actualTotalHotel);
        int expectedTotalHotel = Integer.parseInt(totalHotel);
        if (actualTotalHotel < expectedTotalHotel) expectedTotalHotel = actualTotalHotel;
        for (int i = 1; i <= expectedTotalHotel; i++) {
            Optional<WebElement> hotelPriceLocator = Optional.of(driverTools.waitForElementToPresence(getHotelPriceLocator(i), 30).orElseThrow());
            String hotelPriceString = driverTools.getText(hotelPriceLocator.orElseThrow());
            hotelPriceString = hotelPriceString.replace("?", "0");
            double hotelPrice = convertPriceSafely(hotelPriceString);
            logger.log("Price on the \"" + i + "\" index is \"" + hotelPrice + "\"");
            if (priceRange.equals("<200") && hotelPrice <= 200000) {
                isCorrect = checkPriceRange(hotelPrice, 0, 200000);
                logger.log("Actual hotel price is \"" + hotelPrice + "\", in range <=200");
                break;
            } else if (priceRange.equals("<500") && hotelPrice <= 500000) {
                isCorrect = checkPriceRange(hotelPrice, 0, 500000);
                logger.log("Actual hotel price is \"" + hotelPrice + "\", in range <=500");
                break;
            } else if (priceRange.equals("500-1000") && (hotelPrice >= 500000 && hotelPrice <= 1000000)) {
                isCorrect = checkPriceRange(hotelPrice, 500000, 1000000);
                logger.log("Actual hotel price is \"" + hotelPrice + "\", in range 500-1000");
                break;
            } else if (priceRange.equals(">1000") && !(hotelPrice >= 1000000)) {
                isCorrect = checkPriceRange(hotelPrice, 1000000, Integer.MAX_VALUE);
                logger.log("Actual hotel price is \"" + hotelPrice + "\",in range >=1000");
                isCorrect = false;
                break;
            } else {
                priceRange = priceRange.replaceAll("[A-Z]+", "");

                logger.log("checking for filter " + priceRange);

                boolean condition;
                if (priceRange.contains("-")) {
                    logger.log("price range contains '-'");
                    int bottomLimit = Integer.parseInt(priceRange.split("-")[0]);
                    int upperLimit = Integer.parseInt(priceRange.split("-")[1]);
                    condition = checkPriceRange(hotelPrice, bottomLimit, upperLimit);

                } else if (priceRange.contains("<") || priceRange.contains("≤") || priceRange.contains(">") || priceRange.contains("≥")) {
                    int limit = getIntFromString(priceRange).orElseThrow();
                    if (priceRange.contains("<") || priceRange.contains("≤")) {
                        logger.log("price range contains '<' or '≤'");
                        condition = checkPriceRange(hotelPrice, 0, limit);
                    } else {
                        logger.log("price range contains '>' or '≥'");
                        condition = checkPriceRange(hotelPrice, limit, Integer.MAX_VALUE);
                    }
                } else {
                    throw new NotImplementedException("not implemented price filter");
                }

                if (!condition) {
                    logger.log("Actual hotel price is \"" + hotelPrice + "\", NOT in range " + priceRange);
                    isCorrect = false;
                    break;
                }

            }
            driverTools.swipeUp();
        }
        return isCorrect;
    }

    public Locator getHotelName(String hotelName) {
        return new Locator("HOTEL_NAME_" + hotelName, AppiumBy.xpath("//*[@name='hotelNameLabel']//*[(@name='TACMLabelWithAttachmentView.label' or @name='hotelName') and contains(@value, '" + hotelName + "')]"));
    }

    private boolean checkPriceRange(double hotelPrice, double startLimit, double endLimit) {
        logger.log("checking hotel price " + hotelPrice + " with price range " + startLimit + "-" + endLimit);
        return startLimit <= hotelPrice && hotelPrice <= endLimit;
    }

    @Override
    public boolean isPriceFilterByManualCorrect(String priceRange, String totalHotel) {
        boolean isCorrect = true;
        driverTools.waitForElementToAppear(FIRST_CARD_HOTEL);
        driverTools.waitForElementToPresence(PRICE_FILTER_SIGN, 30).orElseThrow();
        for (int i = 1; i <= Integer.parseInt(totalHotel); i++) {

            //handle login banner
            if (i == 4) {
                continue;
            }

            Optional<WebElement> hotelPriceLocator = Optional.of(driverTools.waitForElementToPresence(getHotelPriceLocator(i), 30).orElseThrow());
            String hotelPriceString = driverTools.getText(hotelPriceLocator.orElseThrow());
            double hotelPrice = convertPrice(hotelPriceString);
            logger.log("Price on the \"" + i + "\" index is \"" + hotelPrice + "\"");
            //manual price filter
            double minPrice = Integer.parseInt(priceRange.split("-")[0]);
            double maxPrice = Integer.parseInt(priceRange.split("-")[1]);

            if (!(hotelPrice >= minPrice) && !(hotelPrice <= maxPrice)) {
                logger.log("Actual hotel price is \"" + hotelPrice + "\", NOT in range " + priceRange);
                isCorrect = false;
                break;
            }

            driverTools.swipeUp();
        }
        return isCorrect;
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
                //add currency as 2nd parameter to make sure the correct price filter is selected, since the price range text is same for all currency
                case "Price Range (per night)/Rentang Harga (per malam)" -> {
                    selectPriceFilterByRange(filterOption, "");
                }
                case "Number of bedrooms/Jumlah kamar tidur" -> {
                    HomesSearchResultPageIOS.getInstance(session).addTotalBedroom(filterOption);
                }
                case "Areas/Area" -> {
                    selectAreaFilter(filterOption);
                }
                case "Facilities/Fasilitas" -> {
                    selectFacility(filterOption);
                }
                case "Property Rating/Rating Properti" -> {
                    selectPropertyRatingFilter(filterOption);
                }
                case "Accomodation Types/Tipe Akomodasi" -> {
                    HomesSearchResultPageIOS.getInstance(session).selectAccomodationTypeFilter(filterOption);
                }
            }
        }
    }

    private Locator getHotelPriceLocator(int index) {
        return new Locator("HOTEL_PRICE_BY_INDEX", AppiumBy.xpath("(//*[@name='mainPrice'])[" + index + "]"));
    }

    private Locator getNthHotelReviewLocator(int index) {
        return new Locator("HOTEL_REVIEW_BY_INDEX", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name=\"TACMRatingView.rating\"])[" + index + "]"));
    }

    @Override
    public String getDateRoomGuestDetail() {
        return driverTools.getText(DATE_ROOM_GUEST_DETAIL).orElseThrow();
    }

    @Override
    public String getCheckInOutDate() {
        String value = getDateRoomGuestDetail();
        return value.split(" • ")[0];
    }

    @Override
    public int getTotalGuestHeader() {
        return 0; //FIXME: Pending implementation
    }

    @Override
    public void clickBackButton() {
        driverTools.waitForElementToAppear(BACK_BUTTON);
        driverTools.click(BACK_BUTTON);
    }

    @Override
    public void checkPopupAndClickBackBtn() {
        LandingPage<T> landingPage = LandingPage.getInstance(session);
        if (!driverTools.isElementClickable(BACK_BUTTON, 10)) {
            landingPage.fillNightCheckInForm();
        }
        driverTools.waitForElementToPresence(FIRST_CARD_HOTEL, 30);
        driverTools.click(BACK_BUTTON);

        // Deeplink SRP can pop past hotel landing (e.g. to home). Wait for landing, recover if needed.
        // Must use LandingPageIOS locators — this class's SEARCH_FIELD_BUTTON/SUBMIT_BUTTON are SRP change-search.
        driverTools.waitForElementToDisappear(FIRST_CARD_HOTEL, 15);
        driverTools.waitForElementToAppear(CLOSE_GIVE_RATING_POPUP, 3).ifPresent(driverTools::click);

        boolean onLanding = driverTools.waitForElementToAppear(LandingPageIOS.SEARCH_FIELD_BUTTON, 15).isPresent()
                && driverTools.waitForElementToAppear(LandingPageIOS.SUBMIT_BUTTON, 8).isPresent();
        if (!onLanding) {
            logger.log("Hotel landing not ready after SRP back; reopening Hotel vertical via deeplink");
            HomePage.getInstance(session).openVerticalPageDeeplink("Hotel", "", "");
            landingPage.fillNightCheckInForm();
            driverTools.waitForElementToAppear(LandingPageIOS.SEARCH_FIELD_BUTTON, 20).orElseThrow();
            driverTools.waitForElementToAppear(LandingPageIOS.SUBMIT_BUTTON, 10).orElseThrow();
        }
    }

    @Override
    public Boolean istFirstHotelReviewDisplayed() {
        driverTools.waitForElementToPresence(FIRST_CARD_HOTEL, 30).orElseThrow();
        return driverTools.isElementDisplayed(FIRST_CARD_HOTEL_REVIEW);
    }

    @Override
    public void swipeToFirstCard() {
        driverTools.swipeToElement(FIRST_CARD_HOTEL_NAME, HOTEL_LIST_CONTAINER, Swipe.DOWN, 10);
    }

    @Override
    public Boolean isNthHotelReviewDisplayed(int cardIndex) {
        if (cardIndex < 1) {
            return false;
        }
        driverTools.waitForElementToPresence(FIRST_CARD_HOTEL, 30).orElseThrow();
        Locator locator = getNthHotelReviewLocator(cardIndex);
        return driverTools.swipeToElement(locator, Swipe.UP, 3)
                .map(driverTools::isElementDisplayed)
                .orElse(false);
    }

    @Override
    public String getNthHotelReview(int cardIndex) {
        driverTools.waitForElementToPresence(FIRST_CARD_HOTEL, 30).orElseThrow();
        WebElement el = driverTools.swipeToElement(getNthHotelReviewLocator(cardIndex), Swipe.UP, 3).orElseThrow();
        return driverTools.getText(el);
    }

    @Override
    public boolean istFirstHotelLocationDisplayed() {
        WebElement webElement = driverTools.swipeToElement(FIRST_CARD_HOTEL_LOCATION, Swipe.UP, 2).orElseThrow();
        return driverTools.isElementDisplayed(webElement);
    }

    @Override
    public boolean isAddedBenefitVisible() {
        List<WebElement> addedBenefits = driverTools.elements(ADDED_BENEFIT).orElseThrow();
        return !addedBenefits.isEmpty();
    }

    @Override
    public boolean isBenefitVisible(String benefitData, int maxHotelsToCheck) {
        final int MAX_SAME_NAME_SWIPE = 3;

        driverTools.waitForElementToAppear(HOTEL_NAME).orElseThrow();
        String currentHotelName = getHotelNameList("");
        logger.log("Benefit check started on hotel '" + currentHotelName + "' (looking for '" + benefitData + "')");

        boolean benefitFound = isBenefitOnHotelCard(currentHotelName, benefitData);
        int hotelChecked = 1;

        while (hotelChecked < maxHotelsToCheck) {
            String nextHotelName = currentHotelName;
            int sameNameSwipe = 0;
            while (currentHotelName.equals(nextHotelName)) {
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
        driverTools.waitForElementToPresence(benefitAndFacilityParentLocator(hotelName)).orElseThrow();
        String benefitText = getJoinedBenefitText(hotelName);
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

    private String getJoinedBenefitText(String hotelName) {
        List<WebElement> benefitElements = driverTools.elements(benefitTextLocator(hotelName)).orElse(Collections.emptyList());
        return benefitElements.stream()
                .map(driverTools::getText)
                .filter(text -> text != null && !text.isBlank())
                .reduce((left, right) -> left + " " + right)
                .orElse("");
    }

    private Locator benefitAndFacilityParentLocator(String hotelName) {
        return new Locator("BENEFIT_FACILITY_PARENT", AppiumBy.xpath("//*[@name='hotelNameLabel']//*[@name='hotelName' or @name='TACMLabelWithAttachmentView.label'][contains(@label,'" + hotelName + "') or contains(@value,'" + hotelName + "')]/parent::*/following-sibling::*[@name='srpCard.highlightedBenefits']"));
    }

    private Locator benefitTextLocator(String hotelName) {
        return new Locator("BENEFIT_TEXT", AppiumBy.xpath("//*[@name='hotelNameLabel']//*[@name='hotelName' or @name='TACMLabelWithAttachmentView.label'][contains(@label,'" + hotelName + "') or contains(@value,'" + hotelName + "')]/parent::*/following-sibling::*[@name='srpCard.highlightedBenefits']//XCUIElementTypeStaticText"));
    }

    @Override
    public FilterInfo findAndChooseInterceptFilter() {
        FilterInfo filterInfo = new FilterInfo();
        filterInfo.filterType = getInterceptFilterType();
        filterInfo.choosenFilter = handleInterceptFilter(filterInfo.filterType);
        logger.log("filterInfo-> " + filterInfo);
        return filterInfo;
    }

    private String getInterceptFilterType() {
        WebElement interceptFilterElement = driverTools.swipeToElement(INTERCEPT_FILTER, Swipe.UP, 15).orElseThrow();
        String interceptFilterElementContentDesc = interceptFilterElement.getAttribute("name");
        String filterType = "";
        if (interceptFilterElementContentDesc.toLowerCase().contains("price")) {
            filterType += "Price";
        } else if (interceptFilterElementContentDesc.toLowerCase().contains("star")) {
            filterType += "Star";
        }
        return filterType;
    }

    private String handleInterceptFilter(String filterType) {
        final Locator INTERCEPT_FILTER_OPTION = getInterceptFilterOption(filterType);

        driverTools.swipeToElement(INTERCEPT_FILTER_OPTION, Swipe.UP, 2).orElseThrow();
        List<WebElement> filterOptionElements = driverTools.elements(INTERCEPT_FILTER_OPTION).orElseThrow();
        WebElement choosenFilterElement = filterOptionElements.get(generateRandomInt(0, filterOptionElements.size() - 1));

        String choosenFilterRaw = driverTools.getValueAttribute(choosenFilterElement, "name");
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
                throw new NoSuchMethodError();
            }
        } else if (filterType.equalsIgnoreCase("star")) {
            choosenFilter += choosenFilterRaw.substring(0, 1);
        }
        return choosenFilter;
    }

    private Locator getInterceptFilterOption(String filterType) {
        return new Locator("INTERCEPT_FILTER_OPTION", AppiumBy.xpath("//XCUIElementTypeCell[contains(@name,'TVCell') and contains(@name,'THM" + filterType + "BadgeCardView')]//XCUIElementTypeOther[string-length(@name)>0]"));
    }

    @Override
    public boolean isFilterActivated(String filterType) {
        //verify general filter green icon is applied
        boolean result = false;
        Boolean filter_badge = driverTools.waitForElementToPresence(FILTER_BUTTON_BADGE, 10).isPresent();
        logger.log("filter_badge : '" + filter_badge + "'");
        if (!filter_badge) {
            return false;
        }

        //verify selected filter green icon is applied
        if (filterType.equalsIgnoreCase("filter")) {
            logger.log("filterType is 'filter', so the filter badge is already applied");
            result = true;
        } else {
            driverTools.waitForElementToPresence(SRP_FILTER_BAR);
            for (String e : filterType.split("&")) {
                logger.log("finding filter badge for ->" + e);
                Optional<WebElement> filter_type_badge = driverTools.swipeUntilElementVisible(getFilterTypeBadgeLocator(e), SRP_FILTER_BAR, Swipe.RIGHT, 0.8, 10);

                if (!filter_type_badge.isPresent()) {
                    filter_type_badge = driverTools.swipeUntilElementVisible(getFilterTypeBadgeLocator(e), SRP_FILTER_BAR, Swipe.LEFT, 0.8, 10);
                }

                logger.log("filter_type_badge_" + e.toLowerCase() + " : '" + filter_type_badge.isPresent() + "'");

                result = filter_badge && filter_type_badge.isPresent();
                if (!result) {
                    break;
                }
            }
        }

        //filter green icon exist
        return result;
    }

    /**
     * @param filterType name of the filter, can be seen on bottom bar of search result page
     * @return locator of the `green icon` of the filter after the filter is applied
     */
    private Locator getFilterTypeBadgeLocator(String filterType) {
        List<String> finder = new ArrayList<>();
        switch (filterType) {
            case "Price":
                return new Locator(filterType.toUpperCase() + "_FILTER_BUTTON_BADGE", AppiumBy.xpath("//*[contains(@name,'selected') and (contains(@name, '<') or contains(@name, '>') or contains(@name, '≤') or contains(@name, '≥') or contains(@name, '-'))]"));
            case "Star":
                finder.add("Star");
                finder.add("Bintang");
                break;
            case "Bedrooms":
                finder.add("Bedrooms");
                finder.add("Kamar");
                break;
            default: {
                logger.fail("There is no handle for filterType '" + filterType + "'");
                throw new NoSuchMethodError();
            }
        }
        return new Locator(filterType.toUpperCase() + "_FILTER_BUTTON_BADGE", AppiumBy.xpath("//*[contains(@name,'selected')]//*[(contains(@label,'" + finder.get(0) + "') or contains(@label,'" + finder.get(1) + "')) and @visible='true']"));
    }

    @Override
    public void zoomMaps(boolean zoomIn) {
        driverTools.waitForElementToPresence(MAPS_CONTAINER).orElseThrow();
        driverTools.zoomInOut(MAPS_CONTAINER, zoomIn);
    }

    @Override
    public void swipeMaps(Swipe swipe, int numberOfSwipe) {
        logger.log("swipeMaps with swipe: " + swipe + " and numberOfSwipe: " + numberOfSwipe);
        driverTools.swipeByDistance(MAPS_CONTAINER, swipe, 0.4, numberOfSwipe);
    }

    @Override
    public void searchThisArea() {
        driverTools.click(SEARCH_THIS_AREA_BUTTON);
    }

    @Override
    public String getVisibleHotelPrice() {
        String price = "IDR 0";
        if (driverTools.waitForElementToPresence(VISIBLE_HOTEL_PRICE_CARD).isPresent())
            price = driverTools.getText(VISIBLE_HOTEL_PRICE_CARD).orElseThrow();
        return price;
    }

    @Override
    public String getVisibleHotelPriceMaps() {
        WebElement visiblePriceMaps = driverTools.waitForElementToPresence(VISIBLE_HOTEL_PRICE_CARD_MAPS, 20).orElseThrow();
        return driverTools.getText(visiblePriceMaps);
    }

    @Override
    public void searchPrevious() {
        driverTools.click(SEARCH_PREVIOUS_BUTTON);
    }

    @Override
    public void swipeHotelCardItem() {
        WebElement visibleHotelContainer = driverTools.waitForElementToPresence(VISIBLE_HOTEL_CONTAINER, 20).orElseThrow();
        driverTools.swipeByDistance(visibleHotelContainer, Swipe.LEFT, 0.9);
    }

    @Override
    public boolean isHotelNameCardVisibleOnMaps() {
        return driverTools.waitForElementToPresence(HOTEL_NAME_MAPS, 30).isPresent();
    }

    @Override
    public String selectVisibleHotelItem() {
        WebElement visibleHotel = driverTools.waitForElementToPresence(HOTEL_NAME_MAPS).orElseThrow();
        String hotelName = driverTools.getText(visibleHotel);
        driverTools.click(visibleHotel);
        return hotelName;
    }

    @Override
    public void clickSeeMapButton() {
        driverTools.waitForElementToPresence(SEE_MAP_BUTTON);
        if (!driverTools.isElementDisplayed(SEE_MAP_BUTTON)) {
            driverTools.swipeToElement(SEE_MAP_BUTTON, Swipe.DOWN, 5);
        }
        driverTools.click(SEE_MAP_BUTTON);
    }

    @Override
    public String[] getHotelPriceMaps() {
        WebElement hotelPriceMapsButton = driverTools.waitForElementToPresence(HOTEL_PRICE_MAPS).orElseThrow();
        String hotelPriceMapsText = driverTools.getText(hotelPriceMapsButton);
        String[] priceMaps = {"", hotelPriceMapsText};
        driverTools.clickElementByCoordinate(hotelPriceMapsButton);
        return priceMaps;
    }

    @Override
    public boolean isHotelStarCardVisibleOnMaps() {
        return driverTools.waitForElementToPresence(HOTEL_STAR_CARD_MAPS).isPresent();
    }

    @Override
    public String getHotelPriceMapsText() {
        WebElement hotelPriceMapsButton = driverTools.waitForElementToPresence(HOTEL_PRICE_MAPS).orElseThrow();
        driverTools.clickElementByCoordinate(hotelPriceMapsButton);
        return driverTools.getText(hotelPriceMapsButton);
    }

    @Override
    public String getHotelPriceCard(String priceText, boolean isMaps) {
        String hotelPriceCard = driverTools.getText(getHotelPriceLocator(priceText)).orElseThrow();
        logger.log("Hotel Price In Card: " + hotelPriceCard);
        return hotelPriceCard;
    }

    /**
     * This method is to choose filter option for 'Price Display/Tampilan Harga` filter
     *
     * @param filterOption is for option text both english and bahasa ex. "Total price/Total harga"
     */
    @Override
    public void selectHotelPriceDisplay(String filterOption) {
        if (filterOption.toLowerCase().contains("per night")) {
            WebElement totalPriceFilterOption = driverTools.swipeToElement(TOTAL_PRICE_FILTER_OPTION, Swipe.UP, 2).orElseThrow();
            String totalPriceFilterOptionText = driverTools.getText(totalPriceFilterOption);
            if (totalPriceFilterOptionText.toLowerCase().contains("selected")) {
                driverTools.click(totalPriceFilterOption);
            } else {
                logger.log("Total price filter option is not selected, so no need to click it");
            }
        } else if (filterOption.toLowerCase().contains("total price")) {
            WebElement totalPriceFilterOption = driverTools.swipeToElement(TOTAL_PRICE_FILTER_OPTION, Swipe.UP, 2).orElseThrow();
            String totalPriceFilterOptionText = driverTools.getText(totalPriceFilterOption);
            if (!totalPriceFilterOptionText.toLowerCase().contains("selected")) {
                driverTools.click(totalPriceFilterOption);
            } else {
                logger.log("Total price filter option is selected, so no need to click it");
            }
        } else {
            WebElement paymentMethodOptionElement = driverTools.swipeToElement(getHotelPriceDisplayFilterOptionLocator(filterOption), Swipe.UP, 2).orElseThrow();
            driverTools.click(paymentMethodOptionElement);
        }
    }

    private Locator getHotelPriceDisplayFilterOptionLocator(String filterOption) {
        String filterNameEnglish = splitLanguageData(filterOption, "/")[0];
        String filterNameBahasa = splitLanguageData(filterOption, "/")[1];
        return new Locator("HOTEL_PRICE_DISPLAY_FILTER_OPTION", AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`name == \"THMTextRadioButtonTableViewCell\" AND (label == \"" + filterNameBahasa + "\" OR label == \"" + filterNameEnglish + "\")`]"));
    }

    private Locator getHotelPriceLocator(String priceText) {
        return new Locator(priceText, AppiumBy.xpath("//*[@name='mainPrice' and contains(@label,'" + priceText + "')]"));
    }

    public void clickSeeAllBtnFilter(String filterName) {
        driverTools.swipeToElement(getSeeAllBtnFilterLocator(filterName), Swipe.UP, 6).orElseThrow();
        driverTools.click(getSeeAllBtnFilterLocator(filterName));
    }

    private Locator getSeeAllBtnFilterLocator(String filterName) {
        String filterNameEnglish = splitLanguageData(filterName, "/")[0];
        String filterNameBahasa = splitLanguageData(filterName, "/")[1];
        return new Locator("SEE_ALL_BUTTON", AppiumBy.xpath("//XCUIElementTypeCell[contains(@label, '" + filterNameEnglish + "') or contains(@label, '" + filterNameBahasa + "')]//*[contains(@label,'Show') or contains(@label,'Lihat')]"));
    }

    @Override
    public String[] selectLocationFilter(String locationType, String locationName) {
        String[] selectedItems = locationName.split("/");
        driverTools.swipeToElementTillPresented(getFilterSectionLocator(locationType), Swipe.UP, 5).orElseThrow();
        for (String item : selectedItems) {
            if (driverTools.waitForElementToPresence(itemCheckboxList(locationType, item)).isPresent()) {
                driverTools.swipeToElementTillPresented(itemCheckboxList(locationType, item), Swipe.UP, 1);
                driverTools.swipeUp();
                driverTools.click(itemCheckboxList(locationType, item));
            } else {
                Optional<WebElement> showMoreButton = driverTools.waitForElementToPresence(showMoreItem(locationType), 5);
                if (showMoreButton.isEmpty()) {
                    driverTools.swipeToElementTillPresented(showMoreItem(locationType), Swipe.UP, 2).orElseThrow();
                }
                driverTools.swipeUp();
                driverTools.clickElementByCoordinate(showMoreItem(locationType));
                if (driverTools.waitForElementToPresence(allItemCheckboxList(item)).isPresent()) {
                    driverTools.click(showMoreItem(locationType));
                }
                driverTools.click(allItemCheckboxList(item));
                driverTools.click(ALL_ITEM_SAVE_BUTTON);
            }
        }
        return selectedItems;
    }

    private Locator itemCheckboxList(String sectionName, String itemName) {
        if (sectionName.contains("/")) {
            String sectionNameEnglish = sectionName.split("/")[0];
            String sectionNameBahasa = sectionName.split("/")[1];
            return new Locator("ITEM_CHECKBOX_LIST", AppiumBy.xpath("//XCUIElementTypeCell[contains(@label,'" + sectionNameEnglish + "') or contains(@label,'" + sectionNameBahasa + "')]/*[contains(@label,'" + itemName + "')]"));
        } else {
            return new Locator("ITEM_CHECKBOX_LIST", AppiumBy.xpath("//XCUIElementTypeCell[contains(@label,'" + sectionName + "')]/*[contains(@label,'" + itemName + "')]"));
        }
    }

    private Locator showMoreItem(String sectionName) {
        if (sectionName.contains("/")) {
            String sectionNameEnglish = sectionName.split("/")[0];
            String sectionNameBahasa = sectionName.split("/")[1];
            return new Locator("SHOW_MORE_ITEM", AppiumBy.xpath("//XCUIElementTypeCell[contains(@label,'" + sectionNameEnglish + "') or contains(@label,'" + sectionNameBahasa + "')]//*[contains(@label,'Show ') or contains(@label,'Lihat ')]"));
        } else {
            return new Locator("SHOW_MORE_ITEM", AppiumBy.xpath("//XCUIElementTypeCell[contains(@label,'" + sectionName + "')]/*[contains(@label,'Show ') or contains(@label,'Lihat ')]"));
        }
    }

    private Locator allItemCheckboxList(String itemName) {
        return new Locator("ALL_ITEM_CHECKBOX_LIST", AppiumBy.xpath("//*[@name='ic cancel grey' and @visible='true']/following::XCUIElementTypeStaticText[contains(@label,'" + itemName + "')]"));
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
                final Locator AREA_CHECK_BOX = getAreaLocatorByIndex(index[k]);
                WebElement areaCheckBox = driverTools.swipeToElement(AREA_CHECK_BOX, Swipe.UP, 3).orElseThrow();
                areaName = driverTools.getText(AREA_CHECK_BOX).orElseThrow();
                logger.log("area name: " + areaName);
                areaList.add(areaName);
                driverTools.swipeElementToMidScreen(areaCheckBox);
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
                final Locator AREA_CHECK_BOX = getAreaLocatorByName(areaArray[k]);
                WebElement areaCheckBox = driverTools.waitForElementToPresence(AREA_CHECK_BOX)
                        .or(() -> driverTools.swipeToElement(AREA_CHECK_BOX, Swipe.UP, 3))
                        .orElseThrow(() -> new NoSuchElementException("AREA_CHECK_BOX not found"));

                driverTools.swipeElementToMidScreen(areaCheckBox);
                driverTools.clickElementByCoordinate(areaCheckBox);
            }
        }
        return areaArray;
    }

    private Locator getAreaLocatorByIndex(int index) {
        return new Locator("AREA_CHECK_BOX", AppiumBy.xpath("(//XCUIElementTypeOther[contains(@name,'checkbox') and @visible='true'])[" + index + "]"));
    }

    private Locator getAreaLocatorByName(String areaName) {
        return new Locator("AREA_CHECK_BOX", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@name,'" + areaName + "') and @visible='true'])[1]"));
    }

    private void hotelCardAreaCheck(String hotelCardName, String[] param) {
        String area = driverTools.getText(getHotelCardAreaLocator(hotelCardName)).orElseThrow();
        logger.log(hotelCardName + " area is in " + area);
        Assertion.assertThat("Assert area of " + hotelCardName + " is already valid", Arrays.stream(param).anyMatch(area::contains), is(true));
    }

    private Locator getHotelCardAreaLocator(String hotelCardName) {
        return new Locator("HOTEL_CARD_AREA", AppiumBy.xpath("//*[@name='hotelNameLabel']//*[(@name='TACMLabelWithAttachmentView.label' or @name='hotelName') and contains(@value, '" + hotelCardName + "')]/parent::*/following-sibling::*[@name='locationLabel']"));
    }

    @Override
    public String selectPropertyRatingFilter(String rating) {
        String ratingText;
        if (rating.equals("random")) {
            String[] propertyRating = {"3", "4", "4.5"};
            int random = new Random().nextInt(propertyRating.length);
            rating = propertyRating[random];
        }
        if (rating.equals("4.5")) {
            // english: 4.5, indonesian: 4,5
            ratingText = "5";
        } else {
            ratingText = rating;
        }
        final Locator PROPERTY_RATING_RADIO_BUTTON = getPropertyRatingSelectionLocator(ratingText);
        WebElement ratingRadioBtn = driverTools.swipeToElement(PROPERTY_RATING_RADIO_BUTTON, Swipe.UP, 3).orElseThrow();
        driverTools.click(ratingRadioBtn);
        return rating;
    }

    private Locator getPropertyRatingSelectionLocator(String rating) {
        return new Locator("PROPERTY_RATING_RADIO_BUTTON", AppiumBy.xpath("//*[contains(@name,'groupReviewOptions')]//*[contains(@label,'" + rating + "')]"));
    }

    private void hotelCardPropertyRatingCheck(String hotelCardName, String[] param) {
        double rating = Double.parseDouble(param[0]);
        String propertyRatingText = driverTools.getText(getHotelCardPropertyRatingLocator(hotelCardName)).orElseThrow();
        String propertyRatingDecimal = propertyRatingText.replace(",", ".");
        String[] propertyRatingArray = splitLanguageData(propertyRatingDecimal, "/");
        logger.log(hotelCardName + " is having " + propertyRatingArray[0] + " property rating");
        double hotelCardPropertyRating = Double.parseDouble(propertyRatingArray[0]);
        Assertion.assertThat("Assert property rating of " + hotelCardName + " is already valid", hotelCardPropertyRating >= rating, is(true));
    }

    private Locator getHotelCardPropertyRatingLocator(String hotelCardName) {
        return new Locator("HOTEL_CARD_PROPERTY_RATING", AppiumBy.xpath("//XCUIElementTypeCell[.//*[@name='hotelName' and (contains(@label,'" + hotelCardName + "') or contains(@value,'" + hotelCardName + "'))]]//*[@name='TACMRatingView.rating'] | //*[@name='hotelNameLabel']//*[@name='TACMLabelWithAttachmentView.label' and contains(@value, '" + hotelCardName + "')]/parent::*/following-sibling::*[@name='TACMRatingView.rating']"));
    }

    @Override
    public void selectFacility(String facilityType) {
        String[] facilityTypeArray = facilityType.split("_");
        for (int i = 0; i < facilityTypeArray.length; i++) {
            WebElement webElement = driverTools.swipeToElement(getFacilityLocator(facilityTypeArray[i]), Swipe.UP, 5).orElseThrow();
            driverTools.click(webElement);
        }
    }

    public Locator getFacilityLocator(String facilityType) {
        String[] facilityLanguage = splitLanguageData(facilityType, "/");
        return new Locator("FACILITY_SELECT", AppiumBy.xpath("(//*[@label='" + facilityLanguage[0] + "' or @label='" + facilityLanguage[1] + "'])[last()]"));
    }

    @Override
    public void changePriceFilter(String newFilterPrice) {
        logger.log("changing price range to ->" + newFilterPrice);
        driverTools.swipeToElement(PRICE_CHANGE_TEXT_FIELD, Swipe.UP, 5);
        driverTools.click(PRICE_CHANGE_TEXT_FIELD);
        WebElement searchFieldText = driverTools.waitForElementToAppear(PRICE_CHANGE_TEXT_FIELD).orElseThrow();
        driverTools.clear(PRICE_CHANGE_TEXT_FIELD);
        driverTools.sendKeys(searchFieldText, newFilterPrice);
        driverTools.click(SUBMIT_BUTTON);
    }

    @Override
    public void goToCardWithLoyaltyBenefitBadge() {
        driverTools.swipeToElement(LOYALTY_BADGE_HOTEL_CARD, Swipe.UP, 30).orElseThrow();
        driverTools.click(LOYALTY_BADGE_HOTEL_CARD);
    }

    @Override
    public void applyPreferredPartnerFilters() {
        clickFilterButton();
        WebElement preferredPartnerPlusEl = driverTools.swipeToElement(PREFERRED_PARTNER_PLUS, Swipe.UP, 8).orElseThrow();
        driverTools.click(preferredPartnerPlusEl);
        WebElement preferredPartnerEl = driverTools.swipeToElement(PREFERRED_PARTNER, Swipe.UP, 8).orElseThrow();
        driverTools.click(preferredPartnerEl);
        clickFilterSubmit();
    }

    @Override
    public boolean isPreferredPartnerIconOnFirstHotelCardVisible() {
        return driverTools.waitForElementToPresence(PREFERRED_PARTNER_ICON).isPresent();
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

        if (vertical.equalsIgnoreCase("Hotel")) {
            expectedDate += HotelPage.getInstance(session).expectedDateRoomGuest(language);
        } else if (vertical.equalsIgnoreCase("Homes")) {
            expectedDate += HomesPage.getInstance(session).expectedDateRoom(language);
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

        String[] locationData = splitLanguageData(location, "-");
        for (int i = 0; i < locationData.length; i++) {
            locationData[i] = locationData[i].substring(0, 1).toUpperCase() + locationData[i].substring(1);
        }
        String srpLocationApi = String.join(" ", Arrays.copyOfRange(locationData, 0, locationData.length - 1));
        String srpLocationApp = getCurrentLocation();

        logger.log("expected location api : " + srpLocationApi);
        logger.log("actual location app: " + srpLocationApp);
        boolean isLocationCorrect = srpLocationApp.equalsIgnoreCase(srpLocationApi);
        logger.log("is location from api match with app: " + isLocationCorrect);

        String expectedDate = convertDateGuest(HomesPage.getInstance(session).expectedDateRoom(language));
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

    private Locator getMasterTagLocator(String name) {
        return new Locator("Master_TAG", AppiumBy.xpath("//*[contains(@name,'master_tag_list')]//*[@label='" + name + "']"));
    }

    private Locator getMasterTagLocator(String name, int index) {
        return new Locator("Master_TAG", AppiumBy.xpath("(//*[contains(@name,'master_tag_list')]//*[@label='" + name + "'])[" + index + "]"));
    }

    @Override
    public boolean checkMasterTags(AccommodationMasterTagResponse arrays, int totalMasterTag, String groupFilterName) {
        for (int i = 0; i < totalMasterTag && i < arrays.data.length; i++) {
            String name = String.valueOf(arrays.data[i].name);
            boolean isMasterTagDisplayed = driverTools.waitForElementToPresence(getMasterTagLocator(name)).isPresent();
            if (isMasterTagDisplayed && name.equals(groupFilterName)) {
                selectMasterTag(groupFilterName);
            }
            driverTools.swipeLeft(getMasterTagLocator(name));
            if (!isMasterTagDisplayed) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void selectMasterTag(String name) {
        driverTools.swipeLeft(getMasterTagLocator(name), 2);
        driverTools.swipeRight(getMasterTagLocator(name), 4);
        driverTools.click(getMasterTagLocator(name));
    }

    private Locator getSRPMasterTagLocator(int index) {
        return new Locator("SRP_MASTER_TAG", AppiumBy.xpath("(//*[@name='master_tag_list']//XCUIElementTypeStaticText)[" + index + "]"));
    }

    @Override
    public String selectMasterTagByIndex(int index) {
        driverTools.swipeToPresentedElement(getSRPMasterTagLocator(index), QUICK_FILTER_CONTAINER, Swipe.LEFT, 3);
        WebElement masterTag = driverTools.waitForElementToPresence(getSRPMasterTagLocator(index)).orElseThrow();
        String masterTagName = driverTools.getText(masterTag);
        driverTools.click(masterTag);
        return masterTagName;
    }

    @Override
    public boolean isSelectedMasterTagDuplicate(String masterTag) {
        return driverTools.swipeToPresentedElement(getMasterTagLocator(masterTag, 2), QUICK_FILTER_CONTAINER, Swipe.LEFT, 3).isPresent();
    }

    public Locator getSubtitleLocator(String label) {
        return new Locator("QUICK_FILTER_HEADER_TEXT", AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`label == '" + label + "'`]"));
    }

    @Override
    public boolean checkSubtitleMasterTag(String groupFilterSubtitle) {
        return driverTools.waitForElementToPresence(getSubtitleLocator(groupFilterSubtitle)).isPresent();
    }

    @Override
    public boolean checkEmptyHotelList() {
        return driverTools.waitForElementToAppear(EMPTY_HOTEL_BANNER).isPresent();
    }

    private Locator getPopularDestination(String name) {
        return new Locator("HOMES_POPULAR_DESTINATION", AppiumBy.xpath("//*[@name='popular_destination_list' and @label='" + name + "']"));
    }

    @Override
    public boolean verifySearchFormWithPojo(HomesData.ContentData list) {
        HomesNHAResponse.Redirect redirectData = list.redirect;
        boolean isCancelButtonDisplayed = driverTools.isElementDisplayed(CANCEL_BUTTON);

        boolean isLocationListCorrect = true;
        for (int i = 0; i < redirectData.popularDestination.destinations.size(); i++) {
            String location = redirectData.popularDestination.destinations.get(i).name;
            isLocationListCorrect = driverTools.isElementDisplayed(getPopularDestination(location));
            if (!isLocationListCorrect) {
                logger.log("Location not found on app");
                return false;
            }
        }
        return isCancelButtonDisplayed && isLocationListCorrect;
    }

    @Override
    public void closeFilterPopup() {
        driverTools.waitForElementToAppear(FILTER_POPUP_TRY_BTN).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(CANCEL_BUTTON).ifPresent(driverTools::click);
    }

    @Override
    public List<String> getPrices() {
        driverTools.swipeToElement(HOTEL_PRICE, Swipe.UP, 3);
        WebElement hotelPrice = driverTools.waitForElementToPresence(HOTEL_PRICE).orElseThrow();
        List<String> priceItems = driverTools.getTexts(hotelPrice);
        Assertion.assertThat("Prices exist", priceItems, is(not(empty())));
        logger.log("prices: " + priceItems);
        return priceItems;
    }

    @Override
    public List<String> getPricesInFilter() {
        String minCurrency = driverTools.getText(MIN_PRICE_CURRENCY).orElseThrow();
        String maxCurrency = driverTools.getText(MAX_PRICE_CURRENCY).orElseThrow();
        List<String> currencyItems = new ArrayList<>(Arrays.asList(minCurrency, maxCurrency));
        Assertion.assertThat("Currency exist", currencyItems, is(not(empty())));
        logger.log("Currency: " + currencyItems);
        return currencyItems;
    }

    @Override
    public void closeMap() {
        driverTools.click(MAP_CLOSE);
    }

    @Override
    public void resetPriceFilter() {
        if (driverTools.isElementDisplayed(PRICE_FILTER_SIGN)) {
            driverTools.click(PRICE_FILTER_SIGN);
            driverTools.click(RESET_FILTER);
            clickFilterSubmit();
            driverTools.waitForElementToDisappear(PRICE_FILTER_SIGN);
            driverTools.waitForElementToAppear(SEE_MAP_BUTTON, 10).orElseThrow();
        }
    }

    @Override
    public void resetFilter() {
        resetPriceFilter(); //iOS Pending improvement, new filter section
    }

    @Override
    public void clickResetFilterButton() {
        WebElement resetButton = driverTools.waitForElementToPresence(RESET_FILTER, 10).orElseThrow();
        driverTools.clickElementByCoordinate(resetButton);
        logger.log("Reset filter button clicked");
    }

    @Override
    public void selectFilterToggle(String filterOption) {
        WebElement toggle = null;
        if (filterOption.contains("Refund") || filterOption.contains("Reschedule")) {
            toggle = driverTools.swipeToPresentedElement(REFUND_RESCHEDULE_TOGGLE, Swipe.UP, 5).orElseThrow();
        } else {
            throw new NotImplementedException("Filter toggle not found: " + filterOption);
        }
        driverTools.swipeElementToMidScreen(toggle);
        String text = driverTools.getText(toggle);
        if (text.contains("selected")) {
            logger.log(filterOption + " filter toggle already active (contains 'selected'); skipping click.");
            return;
        }
        driverTools.swipeUp();
        driverTools.click(toggle);
    }

    /**
     * Generic selector for a checkbox-style filter option inside the filter modal.
     * Supports labels in "English/Bahasa" or "Bahasa/English" format (handled by getCheckboxLocator).
     * Suitable for filters such as `tiket.com's Choices/Pilihan tiket.com`, `Non Smoking/Tidak Merokok`, payment method, etc.
     */
    @Override
    public void selectFilterCheckbox(String filterOption) {
        driverTools.swipeToElementTillPresented(getCheckboxLocator(filterOption), Swipe.UP, 5).orElseThrow();
        driverTools.swipeUp();
        driverTools.click(getCheckboxLocator(filterOption));
        logger.log("Checkbox filter selected: " + filterOption);
    }

    /**
     * Checks whether a filter section (e.g. `Popular Places/Tempat Populer`, `Loyalty Tier/Tier Loyalitas`)
     * is currently rendered inside the filter modal. Returns false when the section is absent without
     * throwing, so it can be used to assert disappearance/visibility based on context.
     */
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
        driverTools.waitForElementToAppear(HOTEL_LOCATION, 10);
        List<String> hotelDistances = driverTools.getTexts(HOTEL_LOCATION);
        Assertion.assertThat("List is not empty", hotelDistances.size(), greaterThan(0));
        logger.log("Hotel distances: " + hotelDistances);
        return hotelDistances;
    }

    public void applyAdvanceFilter(String priceRange, String benefit, String facility, String currency) {
        applyPriceFilter(priceRange, currency);
        applyBenefitFilter(benefit);
        applyFacilityFilter(facility);
    }

    private void applyPriceFilter(String priceRange, String currency) {
        driverTools.swipeToElement(MIN_PRICE_FILTER_INPUT, Swipe.UP, 5).orElseThrow();
        //add currency as 2nd parameter to make sure the correct price filter is selected, since the price range text is same for all currency
        selectPriceFilterByRange(priceRange, currency);
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
        return new Locator("FACILITY", AppiumBy.xpath("//*[@name='TVCell<TACMFilterGroupAccordionView>+groupFacilityGroups']/*[@label='" + facilityEn + "' or @label='" + facilityId + "']"));
    }

    private Locator benefitAndFacilityLocator(String textEn, String textId, int index) {
        return new Locator(textEn.toUpperCase(), AppiumBy.xpath("(//XCUIElementTypeStaticText[(@name='TACMLabelWithAttachmentView.label' or @name='hotelName')])[" + index + "]/parent::*/following-sibling::*[@name='srpCard.highlightedBenefits']/*[normalize-space(contains(@label,'" + textEn + "')) or normalize-space(contains(@label,'" + textId + "'))]"));
    }

    @Override
    public void checkBenefitAndFacility(String benefit, String facility, String totalHotel) {
        String[] facilityArr = facility.split("/");
        String[] benefitArr = benefit.split("/");
        driverTools.waitForElementToDisappear(LOADING_STATE);

        driverTools.waitForElementToAppear(HOTEL_NAME);
        int actualTotalHotel = driverTools.elements(HOTEL_NAME).orElseThrow().size();
        if (actualTotalHotel < Integer.parseInt(totalHotel)) {
            logger.log("Total hotel: " + totalHotel + " is less than expected, actual total hotel: " + actualTotalHotel);
            totalHotel = String.valueOf(actualTotalHotel);
            logger.log("Total hotel is updated to: " + totalHotel);
        }

        for (int i = 0; i < Integer.parseInt(totalHotel); i++) {
            Locator benefitLocator = benefitAndFacilityLocator(benefitArr[0], benefitArr[benefitArr.length - 1], i + 1);
            Locator facilityLocator = benefitAndFacilityLocator(facilityArr[0], facilityArr[facilityArr.length - 1], i + 1);
            WebElement benefitElement = driverTools.swipeToElement(benefitLocator, Swipe.UP, 3).orElseThrow();
            WebElement facilityElement = driverTools.swipeToElement(facilityLocator, Swipe.UP, 3).orElseThrow();

            Assertion.assertThat("is benefit displayed on the card " + (i + 1), driverTools.isElementDisplayed(benefitElement), is(true));
            Assertion.assertThat("is facility displayed on the card " + (i + 1), driverTools.isElementDisplayed(facilityElement), is(true));
        }
        driverTools.swipeDown(Integer.parseInt(totalHotel));
    }

    @Override
    public String selectPopularPlaceFilter() {
        WebElement popularPlaceElement = driverTools.swipeToElement(POPULAR_PLACE, Swipe.UP, 10).orElseThrow();
        String popularPlace = driverTools.getText(popularPlaceElement);
        logger.log("Popular place: " + popularPlace);
        driverTools.click(popularPlaceElement);
        return popularPlace;
    }

    @Override
    public void waitForSrpToLoad() {
        if (driverTools.waitForElementToAppear(HOTEL_NAME).isPresent()) {
        } else {
            driverTools.waitForElementToAppear(NOT_FOUND_CONTAINER, 15);
        }
    }

    @Override
    public void selectPriceAfterTaxFilter(boolean isTax, String priceDescription) { // priceDescription for Android Only
        driverTools.swipeToElement(AFTER_TAX_CHECKBOX, Swipe.UP, 10).orElseThrow();
        String currentFilter = driverTools.getValueAttribute(AFTER_TAX_CHECKBOX, "name");
        if (isTax && !currentFilter.contains("selected")) {
            driverTools.click(AFTER_TAX_CHECKBOX);
        } else if (!isTax && currentFilter.contains("selected")) {
            driverTools.click(AFTER_TAX_CHECKBOX);
        }
        logger.log("tax button status -> " + driverTools.getValueAttribute(AFTER_TAX_CHECKBOX, "name"));
    }

    @Override
    public void checkAndSelectPriceAfterTaxFilter(boolean isTaxApplied) {
        driverTools.swipeToElement(AFTER_TAX_CHECKBOX, Swipe.UP, 10).orElseThrow();
        String currentFilter = driverTools.getValueAttribute(AFTER_TAX_CHECKBOX, "name");
        if (!currentFilter.contains("selected")) {
            driverTools.click(AFTER_TAX_CHECKBOX);
        }
    }

    public void selectHotelWithMeetTravelPolicyNudge() {
        driverTools.waitForElementToPresence(MEET_TRAVEL_POLICY_NUDGE).orElseThrow();
        driverTools.waitForElementToPresence(MEET_TRAVEL_POLICY_HOTEL_CARD).orElseThrow();
        driverTools.clickElementByCoordinate(MEET_TRAVEL_POLICY_HOTEL_CARD);
    }

    @Override
    public boolean isHotelRangeMeetPolicyPriceRange(int totalCard, String priceRange) {
        driverTools.waitForElementToAppear(HOTEL_NAME);
        Map<String, Boolean> hotelPrice = new HashMap<>();
        boolean isHotelPriceMeetPolicy = true;
        int count = totalCard;
        while (count > 0) {
            driverTools.swipeToElement(HOTEL_NAME, Swipe.UP, 5);
            List<WebElement> hotelName = driverTools.elements(HOTEL_NAME).orElseThrow();
            ArrayList<String> hotelNameList = new ArrayList<>();
            for (WebElement element : hotelName) {
                hotelNameList.add(driverTools.getText(element));
            }
            int i = 0;
            while (i < hotelNameList.size() && i < totalCard) {
                String hotelNameText = hotelNameList.get(i);
                if (!hotelPrice.containsKey(hotelNameText)) {
                    driverTools.swipeDown(1);
                    WebElement hotelPriceElement = driverTools.swipeToElement(getHotelPriceUsingHotelName(hotelNameText), Swipe.UP, 5).orElseThrow();
                    String price = driverTools.getText(hotelPriceElement);
                    logger.log("hotel name : " + hotelNameText + " price : " + price);
                    hotelPrice.put(hotelNameText, true);

                    int actualPrice = (int) CoreFunctions.convertPrice(price);
                    int minPrice = Integer.parseInt(priceRange.split("-")[0]);
                    int maxPrice = Integer.parseInt(priceRange.split("-")[1]);
                    isHotelPriceMeetPolicy = actualPrice >= minPrice && actualPrice <= maxPrice;
                    logger.log("is hotel price meet policy : " + isHotelPriceMeetPolicy);

                    if (!isHotelPriceMeetPolicy) {
                        logger.fail("hotel price not meet policy");
                        break;
                    }
                }
                if (i == totalCard - 1) {
                    count = 0;
                }
                i++;
            }
            count--;
        }
        logger.log("size: " + hotelPrice.size() + " total card: " + totalCard);
        logger.log("isHotelPriceMeetPolicy: " + isHotelPriceMeetPolicy);

        return isHotelPriceMeetPolicy && !hotelPrice.isEmpty();
    }

    private Locator getHotelPriceUsingHotelName(String hotelName) {
        return new Locator("HOTEL_PRICE_" + hotelName.toUpperCase(), AppiumBy.xpath("//XCUIElementTypeStaticText[(@name='TACMLabelWithAttachmentView.label' or @name='hotelName') and @label='" + hotelName + "']/parent::*/following-sibling::*[contains(@name,'mainPrice')]"));
    }

    @Override
    public boolean isMeetsTravelPolicyNudgeVisible() {
        driverTools.waitForElementToAppear(MEETS_TRAVEL_POLICY_NUDGE, 20);
        return driverTools.isElementDisplayed(MEETS_TRAVEL_POLICY_NUDGE);
    }

    private Locator getHotelPriceAfterTaxesLocator(String price) {
        return new Locator("HOTEL_PRICE_AFTER_TAXES", AppiumBy.xpath(
                "//XCUIElementTypeStaticText[" +
                        "contains(@label, '/night (after taxes: IDR " + price + ")') or " +
                        "contains(@label, '/malam (setelah pajak: IDR " + price + ")') or " +
                        "contains(@label, '/room/night (after taxes: IDR " + price + ")') or " +
                        "contains(@label, '/kamar/malam (setelah pajak: IDR " + price + ")')" +
                        "]"
        ));
    }

    private Locator getCrossedHotelPriceLocator(String price) {
        return new Locator("HOTEL_CROSSED_PRICE", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='srpCard.crossedPrice' and contains(@label, 'IDR " + price + "')]"));
    }

    private Locator getPerNightBtnFilterLocator(String filterName) {
        String filterNameEN = splitLanguageData(filterName, "/")[0];
        String filterNameID = splitLanguageData(filterName, "/")[1];
        return new Locator("PER_NIGHT_BTN_FILTER", AppiumBy.xpath("//XCUIElementTypeCell[@name='THMTextRadioButtonTableViewCell' and (translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='" + filterNameEN + "' or translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='" + filterNameID + "')]"));
    }

    @Override
    public void checkPerNightBtnFilter(String filterName) {
        if (driverTools.isElementDisplayed(TAXES_FILTER_SWITCH)) {
            driverTools.click(TAXES_FILTER_SWITCH); //turn switch off for Display price including taxes
        }
        logger.log("Check per night filter: " + filterName);
        driverTools.click(getPerNightBtnFilterLocator(filterName));
        driverTools.isElementDisplayed(PER_NIGHT_RADIO_BTN);
        if (driverTools.isElementDisplayed(FILTER_SUBMIT_BUTTON)) {
            driverTools.click(FILTER_SUBMIT_BUTTON);
        } else {
            driverTools.click(CANCEL_BUTTON);
        }
    }

    @Override
    public boolean checkHotelPrice(boolean isCrossPrice, String hotelName, String price) {
        //isCrossPrice only for Android
        return checkHotelPrice(hotelName, price);
    }

    @Override
    public boolean checkHotelPrice(String hotelName, String price) {
        return driverTools.swipeToElement(getHotelPriceLocator(hotelName, price), Swipe.UP, 5).isPresent();
    }

    private Locator getHotelPriceLocator(String hotelName, String price) {
        return new Locator("HOTEL_PRICE", AppiumBy.xpath("//*[@name='hotelNameLabel']//*[(@name='TACMLabelWithAttachmentView.label' or @name='hotelName') and @label='" + hotelName + "']/parent::*/following-sibling::*[contains(@label,'" + price + "')]"));
    }
  /*
    @Override
    public long getHotelFinalPriceAmountByName(String hotelName) {
        WebElement priceElement = driverTools
                .swipeToElement(getHotelPriceLocator(hotelName), Swipe.UP, 5)
                .orElseThrow();

        String priceText = driverTools.getText(priceElement);

        return CoreFunctions.getIntFromString(priceText.replaceAll("[^0-9]", ""))
                .orElse(0);
    }
*/

    private Locator getHotelLocator(String hotelName) {
        return new Locator("HOTEL_NAME", AppiumBy.xpath("//*[@name='hotelNameLabel']//*[(@name='TACMLabelWithAttachmentView.label' or @name='hotelName') and contains(@value, '" + hotelName + "')]"));
    }

    private Locator getHotelFinalPriceLocator(String hotelName) {
        return new Locator("HOTEL_FINAL_PRICE", AppiumBy.xpath("//*[@name='hotelNameLabel']//*[(@name='TACMLabelWithAttachmentView.label' or @name='hotelName') and @label='" + hotelName + "']/parent::*/following-sibling::*[@name='mainPrice']"));
    }

    @Override
    public boolean isLabelTotalPrice(boolean isTax) {
        return (isTax) ? driverTools.waitForElementToPresence(TOTAL_PRICE_LABEL).isPresent() : driverTools.waitForElementToPresence(TOTAL_PRICE_LABEL_WITHOUT_TAX).isPresent();
    }

    @Override
    public boolean isLabelTotalPrice() {
        return driverTools.waitForElementToPresence(TOTAL_PRICE_LABEL).isPresent();
    }

    @Override
    public void selectFilterStarRange(String starRange) {
        driverTools.swipeUntilElementVisible(STAR_FILTER_BUTTON, SRP_FILTER_BAR, Swipe.LEFT, 0.8, 5);
        driverTools.click(STAR_FILTER_BUTTON);
        String[] starRangeSplit = starRange.contains("-") ? starRange.split("-") : new String[]{starRange};
        for (String value : starRangeSplit) {
            int star = Integer.parseInt(value.trim());
            driverTools.waitForElementToAppear(getStarLocator(star));
            logger.log("range index -> " + star);
            driverTools.click(getStarLocator(star));
        }
        driverTools.click(FILTER_SUBMIT_BUTTON);
    }

    /*
    /room/night (after taxes)
    /kamar/malam (setelah pajak)
    /night (include taxes)
    /malam (termasuk pajak)
     */
    @Override
    public String getPriceRemarkText() {
        WebElement priceRemarkElement = driverTools.waitForElementToPresence(LABEL_PER_ROOM_PER_NIGHT).orElseThrow();
        String priceRemarkText = driverTools.getText(priceRemarkElement);
        logger.log("Price remark text: " + priceRemarkText);
        return priceRemarkText;
    }

    @Override
    public String clickHeartIcon() {
        WebElement firstHotelNameElement = driverTools.waitForElementToPresence(HOTEL_NAME).orElseThrow();
        String firstHotelName = driverTools.getText(firstHotelNameElement);
        if (driverTools.waitForElementToPresence(getSelectedWishlistIconLocator(firstHotelName)).isPresent()) {
            WebElement selectedWishlistIcon = driverTools.swipeToElement(getSelectedWishlistIconLocator(firstHotelName), Swipe.DOWN, 2).orElseThrow();
            logger.log("Property already there in the wishlist, proceed to remove from wishlist");
            driverTools.clickElementByCoordinate(selectedWishlistIcon);
            driverTools.waitForElementToDisappear(selectedWishlistIcon);
        }
        driverTools.swipeToElement(getWishlistIconLocator(firstHotelName), Swipe.DOWN, 2);
        WebElement wishlistIcon = driverTools.swipeToElement(getWishlistIconLocator(firstHotelName), Swipe.UP, 3).orElseThrow();
        driverTools.clickElementByCoordinate(wishlistIcon);
        return firstHotelName;
    }

    @Override
    public String isWishlistDialogShown() {
        driverTools.waitForElementToAppear(MANAGE_WISHLIST, 30);
        WebElement manageWishlist = driverTools.waitForElementToPresence(MANAGE_WISHLIST, 30).orElseThrow();
        return driverTools.getText(manageWishlist);
    }

    private final String expectedStatus = "wishlist (selected)";

    @Override
    public boolean isWishlistActive(String hotelName) {
        driverTools.waitForElementsToAppear(HOTEL_NAME);
        driverTools.swipeToElement(getHotelName(hotelName), Swipe.UP, 10).orElseThrow();
        driverTools.swipeToElement(getSelectedWishlistIconLocator(hotelName), Swipe.DOWN, 2);
        WebElement wishlistIcon = driverTools.swipeToElement(getSelectedWishlistIconLocator(hotelName), Swipe.UP, 5).orElseThrow();
        String wishlistIconStatus = driverTools.getValueAttribute(wishlistIcon, "name");
        Assertion.assertThat("Wishlist icon status: " + wishlistIconStatus, wishlistIconStatus, equalToIgnoringCase(expectedStatus));
        return wishlistIconStatus.equalsIgnoreCase(expectedStatus);
    }

    private Locator getWishlistIconLocator(String hotelName) {
        return new Locator("WISHLIST_ICON", AppiumBy.xpath("//*[(@name='TACMLabelWithAttachmentView.label' or @name='hotelName') and contains(@value, '" + hotelName + "')]/parent::*[@name='hotelNameLabel']/following-sibling::*[contains(@name,'wishlist')]"));
    }

    private Locator getSelectedWishlistIconLocator(String hotelName) {
        return new Locator("SELECTED_WISHLIST_ICON", AppiumBy.xpath("//*[(@name='TACMLabelWithAttachmentView.label' or @name='hotelName') and contains(@value, '" + hotelName + "')]/parent::*[@name='hotelNameLabel']/following-sibling::*[contains(@name,'wishlist (selected)')]"));
    }

    @Override
    public boolean isWishlistActiveOnMap(String hotelName) {
        List<WebElement> hotelPriceMaps = driverTools.waitForElementsToAppear(HOTEL_PRICE_MAPS).orElseThrow();
        driverTools.clickElementByCoordinate(hotelPriceMaps.get(0));

        int maxLoop = 0;
        WebElement hotelPriceMapsButton = driverTools.waitForElementToPresence(HOTEL_PRICE_MAPS).orElseThrow();
        driverTools.clickElementByCoordinate(hotelPriceMapsButton);
        Optional<WebElement> wishlistIconOnMap = driverTools.waitForElementToPresence(getWishlistIconLocatorOnMap(hotelName));
        while (wishlistIconOnMap.isEmpty()) {
            logger.log("Swiping loop: " + maxLoop);
            WebElement visibleHotelContainer = driverTools.waitForElementToPresence(VISIBLE_HOTEL_CONTAINER, 20).orElseThrow();
            driverTools.swipeByDistance(visibleHotelContainer, Swipe.LEFT, 0.8);
            wishlistIconOnMap = driverTools.waitForElementToPresence(getWishlistIconLocatorOnMap(hotelName));
            if (wishlistIconOnMap.isPresent()) break;
            maxLoop++;
            if (maxLoop > 10) {
                logger.log("Hotel " + hotelName + " not found on map");
                break;
            }
        }

        WebElement wishlistIconElement = driverTools.waitForElementToPresence(getWishlistIconLocatorOnMap(hotelName)).orElseThrow();
        String wishlistIconStatus = driverTools.getValueAttribute(wishlistIconElement, "name");
        Assertion.assertThat("Wishlist icon status: " + wishlistIconStatus, wishlistIconStatus, equalToIgnoringCase(expectedStatus));
        return wishlistIconStatus.equalsIgnoreCase(expectedStatus);
    }

    private Locator hotelCardMapsContainer(String hotelName) {
        return new Locator("HOTEL_CARD_MAPS_CONTAINER_" + hotelName.toUpperCase(), AppiumBy.xpath("//*[@name='hotelNameLabel' and @visible='true' and @label='" + hotelName + "']//parent::*"));
    }

    private Locator getWishlistIconLocatorOnMap(String hotelName) {
        return new Locator("WISHLIST_ICON_MAPS", AppiumBy.xpath("//*[@name='hotelNameLabel' and contains(@value, '" + hotelName + "') and @visible='true']/following-sibling::*[contains(@name,'wishlist')]"));
    }

    @Override
    public void chooseHotelCardOnMap(String hotelName) {
        driverTools.click(hotelCardMapsContainer(hotelName));
    }

    @Override
    public boolean isPriceAfterTaxFilterApplied() {
        driverTools.waitForElementToAppear(HOTEL_NAME, 20);
        Optional <WebElement> label = driverTools.swipeToElement(TOTAL_PRICE_LABEL_WITHOUT_TAX,Swipe.UP,2);
        return label.isEmpty();
    }

    @Override
    public boolean clickWishlistFirstHotelCard() {
        driverTools.swipeToElement(FIRST_WISHLIST_BTN, Swipe.UP, 3).orElseThrow();
        driverTools.clickElementByCoordinate(FIRST_WISHLIST_BTN);
        driverTools.waitForElementToAppear(MANAGE_SNACKBAR, 10);
        return driverTools.isElementDisplayed(MANAGE_SNACKBAR);
    }

    @Override
    public void clickWishlistFirstHotelCardForRemoval() {
        if (driverTools.isElementDisplayed(WISHLIST_ICON_RED)) {
            logger.log("Property already there in the wishlist, proceed to remove from wishlist");
            return; //element already in the wishlist
        }
        driverTools.swipeToElement(FIRST_WISHLIST_BTN, Swipe.UP, 3).orElseThrow();
        driverTools.clickElementByCoordinate(FIRST_WISHLIST_BTN);
        driverTools.waitForElementToAppear(MANAGE_SNACKBAR, 30);

    }

    @Override
    public boolean clickRedWishlistIcon() {
        driverTools.click(WISHLIST_ICON_RED);
        return driverTools.waitForElementToAppear(REMOVE_WISHLIST).isPresent();
    }

    @Override
    public void openHotelCardInMaps() {
        String price = getFirstHotelPrice();
        driverTools.waitForElementToAppear(SEE_MAP_BUTTON);
        driverTools.click(SEE_MAP_BUTTON);
        Locator mapFirstHotelPrice = getMapWishlistButton(price);
        driverTools.waitForElementToAppear(mapFirstHotelPrice, 10).orElseThrow();
        driverTools.click(mapFirstHotelPrice);
        logger.log("Checking if hotel card is displayed");
        driverTools.waitForElementToAppear(HOTEL_CARD_MAPS, 15);
        Assertion.assertThat("Hotel card is displayed", driverTools.isElementDisplayed(HOTEL_CARD_MAPS), is(true));
    }

    private Locator getMapWishlistButton(String Price) {
        return new Locator("WISLIST_PRICE" + Price, AppiumBy.xpath("//*[@type='XCUIElementTypeOther' and ./parent::*[@name='" + Price + "']]"));
    }

    @Override
    public void closeMaps() {
        driverTools.click(MAP_CLOSE);
    }

    @Override
    public boolean isWishlistIconSelected() {
        driverTools.waitForElementToPresence(WISHLIST_ICON_RED, 10);
        boolean res = driverTools.isElementDisplayed(WISHLIST_ICON_RED);
        return res;
    }


    @Override
    public String verifyCorrectSRP(String chip) {
        driverTools.waitForElementToPresence(GOT_IT).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(SRP_TITLE, 15);
        WebElement element = driverTools.swipeToElement(SRP_TITLE, Swipe.UP, 1).orElseThrow();
        String text = driverTools.getText(element);
        return text;
    }

    @Override
    public void verifyCurrencyOnFilter(String currency) {
        Locator currencyLocator = getCurrencyLocator(currency);
        driverTools.swipeToElement(currencyLocator, Swipe.UP, 5).orElseThrow();
        Assertion.assertThat("Currency filter is displayed with correct currency", driverTools.isElementDisplayed(currencyLocator), is(true));
    }

    @Override
    public boolean verifyNearMeOnHotelCards() {
        Locator nearMeLocationInfo = new Locator("NEAR_ME_LOCATION_INFO",
                AppiumBy.xpath("//*[@name='locationLabel' and (contains(@label,'from your location') or contains(@label,'dari lokasimu'))]"));
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

    public static final Locator HOTELS_NEAR_SELECTED_AREA_HEADING = new Locator("HOTELS_NEAR_SELECTED_AREA_HEADING", AppiumBy.xpath("//*[(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')=\"hotels near your selected area\" or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')=\"hotel di dekat lokasi pilihanmu\") and @type=\"XCUIElementTypeStaticText\"]"));
    public static final Locator HOTELS_NEAR_SELECTED_AREA_CARDS = new Locator("HOTELS_NEAR_SELECTED_AREA_CARDS",
            AppiumBy.xpath("//*[(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')=\"hotels near your selected area\" or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')=\"hotel di dekat lokasi pilihanmu\") and @type=\"XCUIElementTypeStaticText\"]/following::XCUIElementTypeOther//*[@name='hotelNameLabel']"));
    public static final Locator HOTELS_NEAR_SELECTED_AREA_FIRST_CARD = new Locator("HOTELS_NEAR_SELECTED_AREA_FIRST_CARD",
            AppiumBy.xpath("(//*[(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')=\"hotels near your selected area\" or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')=\"hotel di dekat lokasi pilihanmu\") and @type=\"XCUIElementTypeStaticText\"]/following::XCUIElementTypeOther//*[@name='hotelNameLabel'])[1]"));

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
        WebElement firstCard = driverTools.swipeToElement(HOTELS_NEAR_SELECTED_AREA_FIRST_CARD, Swipe.UP, 15)
                .orElseThrow(() -> new IllegalStateException("No property card found under 'Hotels near your selected area' section"));
        String hotelName = driverTools.getText(firstCard);
        logger.log("Tapping first hotel card in 'Hotels near your selected area' section: " + hotelName);
        driverTools.click(firstCard);
        return hotelName;
    }

    private Locator getCurrencyLocator(String currency) {
        return new Locator("CURRENCY_" + currency.toUpperCase(), AppiumBy.xpath("//XCUIElementTypeTextField/*[contains(@value,'" + currency + "')]"));
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
        boolean hotelPrice = driverTools.isElementDisplayed(HOTEL_PRICE);
        if (!hotelCards.isEmpty()) {
            driverTools.click(hotelCards.get(0));
        }
        return hotelPrice;
    }

    @Override
    public void applyAndVerifyPreferredPartnerFilter(String filterName) {
        logger.log("Opening filter sheet on iOS SRP");
        clickFilterButton();

        driverTools.waitForElementToAppear(FILTER_POPUP_TRY_BTN, 2).ifPresent(driverTools::click);

        if (driverTools.isElementDisplayed(RESET_FILTER)) {
            logger.log("Previous filter detected in filter sheet. Clicking Reset...");
            driverTools.click(RESET_FILTER);
        }

        logger.log("Locating filter option switch for: " + filterName);

        if (filterName == null) {
            throw new IllegalArgumentException("filterName must not be null");
        }

        String switchName;
        if (filterName.equalsIgnoreCase("Preferred Partner Plus")) {
            switchName = "switch_preferredPartnerPlus";
        } else if (filterName.equalsIgnoreCase("Preferred Partner")) {
            switchName = "switch_preferredPartner";
        } else {
            throw new IllegalArgumentException("Unknown Preferred Partner filter: " + filterName);
        }

        Locator switchLocator = new Locator("FILTER_SWITCH", AppiumBy.name(switchName));

        WebElement targetSwitch = driverTools.swipeToElement(switchLocator, Swipe.UP, 4)
                .or(() -> driverTools.swipeToPresentedElement(switchLocator, Swipe.UP, 4))
                .orElseThrow(() -> new RuntimeException("Filter switch '" + switchName + "' for '" + filterName + "' not found inside iOS Filter sheet"));

        logger.log("Toggling filter switch ON for: " + filterName);
        try {
            driverTools.click(targetSwitch);
        } catch (Exception e) {
            logger.log("Standard click failed (" + e.getClass().getSimpleName() + ": " + e.getMessage() + "), executing coordinate-based click on filter switch");
            driverTools.clickElementByCoordinate(targetSwitch);
        }

        logger.log("Submit filter and wait for search results to load");
        clickFilterSubmit();
        waitForSrpToLoad();

        logger.log("Assert Preferred Partner badge icon is visible on the hotel card on SRP");
        Assertion.assertThat("Preferred Partner icon should be visible on SRP card",
                isPreferredPartnerIconOnFirstHotelCardVisible(), is(true));
    }
}
