package com.tiket.page.accommodation.hotel.detailpage;

import static com.tiket.core.CoreFunctions.convertPrice;
import static com.tiket.core.CoreFunctions.findIndex;
import static com.tiket.core.CoreFunctions.normalizeNonBreakingSpaceText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.tiket.appium.DriverTools;
import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.service.BundleID;
import com.tiket.testbase.Assertion;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
public class DetailPageIOS <T extends WebDriver> extends DetailPage<T> {

    public DetailPageIOS(Session<T> session) { super(session); }

    public static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", AppiumBy.xpath("//*[@name='TVCell<TDSText>+HotelName']//XCUIElementTypeStaticText"));
    public static final Locator SHARE_BUTTON = new Locator("SHARE_BUTTON", AppiumBy.name("share_button"));
    public static final Locator HOTEL_IMAGE = new Locator("HOTEL_IMAGE", AppiumBy.xpath("//*[contains(@name, 'DetailThumbnailView')]"));
    public static final Locator HOTEL_CATEGORY = new Locator("HOTEL_CATEGORY", AppiumBy.xpath("//*[contains(@name, 'DetailPropertyOverviewBadgeStarView')]"));
    public static final Locator HOTEL_STAR = new Locator("HOTEL_STAR", AppiumBy.xpath("//*[@name='TVCell<TACMProductDetailPropertyOverviewBadgeStarView>']//XCUIElementTypeOther//XCUIElementTypeOther"));
    public static final Locator HOTEL_LOCATION = new Locator("HOTEL_LOCATION", AppiumBy.xpath("//*[contains(@name, 'DetailPropertyOverviewReviewWithLocationView')]//XCUIElementTypeButton[not(contains(@label, 'Review') or contains(@label, 'review'))]"));
    public static final Locator HOTEL_REVIEW = new Locator("HOTEL_REVIEW", AppiumBy.xpath("//*[@type='XCUIElementTypeButton' and @name='review_button']"));
    public static final Locator HOTEL_PRICE = new Locator("HOTEL_PRICE", AppiumBy.name("mainPrice"));
    public static final Locator BOOK_ROOM = new Locator("BOOK_ROOM", AppiumBy.xpath("//*[@type='XCUIElementTypeButton' and @name='actionButton' and (contains(@label, 'Book') or contains(@label, 'Pesan'))]"));
    // Keep past label locators + current accessibility names (UI label is "See rooms", not only name=see_rooms).
    public static final Locator SEE_HOTEL_ROOM_BUTTON = new Locator("SEE_HOTEL_ROOM_BUTTON", AppiumBy.iOSNsPredicateString("name == 'see_rooms' OR name == 'continue_button' OR label == 'See rooms' OR label == 'See Rooms' OR label == 'Lihat kamar' OR label == 'Lihat Kamar' OR label == 'See Units' OR label == 'Lihat Unit' OR label == 'Lanjutkan'"));
    private static final Locator HOTEL_ROOMS_POPUP = new Locator("HOTEL_ROOMS_POPUP", AppiumBy.name("skip_button"));
    private static final Locator HOTEL_ROOMS_OPTION_POPUP = new Locator("HOTEL_ROOMS_OPTION_POPUP", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == 'Selesai' or label == 'Done' or label == 'Lewati' or label == 'Skip'`]"));
    public static final Locator HOTEL_FIRST_BOOK_BUTTON = new Locator("HOTEL_FIRST_BOOK_BUTTON", AppiumBy.xpath("(//XCUIElementTypeButton[@name='roomCard.bookButton' and @visible='true'])[1]"));
    public static final Locator ADD_ROOM_BUTTON = new Locator("ADD_ROOM_BUTTON", AppiumBy.name("add_btn"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", AppiumBy.name("book_button"));
    public static final Locator CONTINUE_BOOK = new Locator("CONTINUE_BOOK", AppiumBy.xpath("//XCUIElementTypeButton[@label='Continue' or @label='Lanjutkan']"));

    // hotel rooms identifier
    public static final Locator ROOM_CARDS = new Locator("ROOM_CARDS", AppiumBy.xpath("//XCUIElementTypeButton[@name='roomCard.bookButton']/parent::*"));
    public static final Locator BACK = new Locator("BACK_BUTTON", AppiumBy.xpath("(//*[@name=\"back_button\"])[2]"));
    public static final Locator GALLERY_TEXT = new Locator("GALLERY_TEXT", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Galeri' or @name='Gallery']"));
    public static final Locator GALLERY_IMAGE = new Locator("GALLERY_IMAGE", AppiumBy.xpath("//XCUIElementTypeCollectionView"));
    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.id("back_button"));
    public static final Locator FIRST_ROOM_DETAIL = new Locator("FIRST_ROOM_DETAIL", AppiumBy.name("roomCard.headerTitle"));
    public static final Locator EXPAND_PRICE_DETAIL = new Locator("EXPAND_PRICE_DETAIL", AppiumBy.name("tds_ic_chevron_down"));
    public static final Locator TOTAL_PRICE_ON_PRICE_DETAIL = new Locator("TOTAL_PRICE_ON_PRICE_DETAIL", AppiumBy.iOSClassChain("**/*[`name == 'totalView'`]/*[`name == 'rightText'`]"));
    public static final Locator PRICE_BREAKDOWN =  new Locator ("PRICE_BREAKDOWN" , AppiumBy.xpath("//*[@name='titleView']//*[@name='rightText' and @label]"));
    public static final Locator HOTEL_REVIEW_BUTTON = new Locator("HOTEL_REVIEW_BUTTON", AppiumBy.iOSClassChain("**/*[`name CONTAINS 'textLabel' AND label == 'Review'`]"));
    public static final Locator LOCATION_TAB_BUTTON = new Locator("LOCATION_TAB_BUTTON", AppiumBy.xpath("(//*[@type=\"XCUIElementTypeStaticText\" and (@label=\"Location\" or @label=\"Lokasi\")])[1]"));
    public static final Locator REVIEW_RATING= new Locator("REVIEW_RATING", AppiumBy.xpath("(//XCUIElementTypeCell[@name=\"TVCell<TNMHomesDetailVerifiedBadgeView>\"]//XCUIElementTypeStaticText)[2]"));
    public static final Locator SEE_MORE_REVIEW_BUTTON= new Locator("SEE_MORE_REVIEW_BUTTON", AppiumBy.xpath("//*[@name='TVCell<TACMHeaderWithSeeAllView>+Review']//XCUIElementTypeStaticText[@label='Lihat semua' or @label='See more' or @label='See all']"));
    public static final Locator REVIEW_IMPRESSION= new Locator("REVIEW_IMPRESSION", AppiumBy.xpath("(//XCUIElementTypeCell[@name=\"TVCell<TNMHomesDetailVerifiedBadgeView>\"]//XCUIElementTypeStaticText)[4]"));
    public static final Locator REVIEW_COUNT= new Locator("REVIEW_COUNT", AppiumBy.xpath("(//XCUIElementTypeCell[@name=\"TVCell<TNMHomesDetailVerifiedBadgeView>\"]//XCUIElementTypeStaticText)[1]"));
    public static final Locator TEXT_OF_RATING= new Locator("TEXT_OF_RATING", AppiumBy.xpath("//XCUIElementTypeCell[@name=\"TVCell<TNMHomesDetailVerifiedBadgeView>\"]//XCUIElementTypeStaticText[3]"));
    public static final Locator REVIEW_RATING_REVIEW_PAGE= new Locator("REVIEW_RATING_REVIEW_PAGE", AppiumBy.xpath("//XCUIElementTypeImage[@name='ic_tiket']//following-sibling::*[1]"));
    public static final Locator REVIEW_COUNT_REVIEW_PAGE= new Locator("REVIEW_COUNT_REVIEW_PAGE", AppiumBy.xpath("//XCUIElementTypeImage[@name='ic_tiket']//following-sibling::*[3]"));
    public static final Locator REVIEW_IMPRESSION_REVIEW_PAGE= new Locator("REVIEW_IMPRESSION_REVIEW_PAGE", AppiumBy.xpath("//XCUIElementTypeImage[@name='ic_tiket']//following-sibling::*[2]"));
    public static final Locator ROOM_BOOK_BUTTON = new Locator("ROOM_BOOK_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@label='Pesan' or @label='Book' or @label='Pilih' or @label='Select']"));
    public static final Locator ROOM_BOOK_BUTTON_100P_REFUND = new Locator("ROOM_BOOK_BUTTON_100P_REFUND", AppiumBy.xpath("//*[@name='cancellationPoliciesText' and (contains(@label,'100% refund') or contains(@label,'100% Refund'))]"));
    public static final Locator REFUND_AND_RESCHEDULE_TITLE = new Locator("REFUND_AND_RESCHEDULE", AppiumBy.xpath("//*[(@name='TVCell<TACMLabelWithImageView>') and contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'refund & reschedule')]"));
    private static final Locator REFUND_AND_RESCHEDULE_FEE = new Locator("REFUND_AND_RESCHEDULE_FEE", AppiumBy.iOSClassChain("**/*[`name CONTAINS 'charges'`]"));
    public static final Locator CANCEL_BUTTON = new Locator("CANCEL_BUTTON", AppiumBy.accessibilityId("ic cancel grey"));
    public static final Locator CANCEL_BUTTON_ROOM_CAPACITY_WARNING = new Locator("CANCEL_BUTTON_ROOM_CAPACITY_WARNING", AppiumBy.name("cancelButton"));
    public static final Locator BOOK_BUTTON_ROOM_CAPACITY_WARNING = new Locator("BOOK_BUTTON_ROOM_CAPACITY_WARNING", AppiumBy.name("bookButton"));

    // ====== In-app map & nearby chips on PDP ======
    // Map kit reports XCUIElementTypeMap as visible=false; use visible map chrome instead
    private static final Locator IN_APP_MAP_POPUP = new Locator("IN_APP_MAP_POPUP", AppiumBy.xpath("//XCUIElementTypeButton[@name='Accommodations nearby' or contains(@label,'nearby') or contains(@label,'sekitar')] | //*[@name='Map pin' and @visible='true']"));
    private static final Locator HOTEL_MARKER_ON_MAP = new Locator("HOTEL_MARKER_ON_MAP", AppiumBy.xpath("//*[@name='Map pin']//following-sibling::XCUIElementTypeStaticText"));
    private static final Locator NEARBY_BUTTON = new Locator("NEARBY_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'nearby') or contains(@label,'sekitar')]"));
    private static final Locator NEARBY_POINT = new Locator("NEARBY_POINT", AppiumBy.xpath("//*[@name='Map pin']//following-sibling::XCUIElementTypeStaticText//following-sibling::*[@name='Map pin' and @visible='true']"));
    private static final Locator NEARBY_ACCOMMODATION_LIST_CONTAINER = new Locator("NEARBY_ACCOMMODATION_LIST_CONTAINER", AppiumBy.xpath("//*[@name='hotelNameLabel' and @visible='true']/parent::*/parent::*/parent::XCUIElementTypeCell"));
    private static final Locator NEARBY_ACCOMMODATION_CROSS_SELL_NUDGE = new Locator("NEARBY_ACCOMMODATION_CROSS_SELL_NUDGE", AppiumBy.accessibilityId("crossSellNudgeView"));
    private static final Locator NEARBY_ACCOMMODATION_HOTEL_NAME_WITH_CROSS_SELL = new Locator("NEARBY_ACCOMMODATION_HOTEL_NAME_WITH_CROSS_SELL", AppiumBy.xpath("(//XCUIElementTypeCell[.//*[@name='crossSellNudgeView']]//*[@name='hotelNameLabel' and @visible='true'])[1]"));
    private static final Locator NEARBY_CHIPS =new Locator("NEARBY_CHIPS", AppiumBy.xpath("//XCUIElementTypeTable[@visible='true']//preceding-sibling::XCUIElementTypeOther//XCUIElementTypeStaticText"));

    // ====== Infinity promo cards on PDP ======
    private static final Locator INFINITY_PROMO_SECTION = new Locator("INFINITY_PROMO_SECTION", AppiumBy.name("collectionView"));
    private static final Locator INFINITY_PROMO_CARDS = new Locator("INFINITY_PROMO_CARDS", AppiumBy.name("TACMInfinityPromoCarouselItemView"));
    private static final Locator INFINITY_PROMO_COPY_BTN = new Locator("INFINITY_PROMO_COPY_BTN", AppiumBy.name("copyIcon"));
    private static final Locator INFINITY_PROMO_CODE = new Locator("INFINITY_PROMO_FIRST_CODE", AppiumBy.name("promoLabel"));
    private static final Locator SEE_ALL_PROMO_BTN = new Locator("SEE_ALL_PROMO_BTN", AppiumBy.xpath("//*[@name='tds_ic_next' and @visible='true']"));
    private static final Locator SEE_ALL_PROMO_SECTION = new Locator("SEE_ALL_PROMO_SECTION", AppiumBy.xpath("//*[@name='ic cancel grey']/parent::*/parent::*/parent::XCUIElementTypeOther"));
    private static final Locator SEE_ALL_PROMO_COPY_BTN = new Locator("SEE_ALL_PROMO_COPY_BTN", AppiumBy.xpath("//*[@name='ctaButton' and @visible='true']"));
    private static final Locator SEE_ALL_PROMO_CODE = new Locator("SEE_ALL_PROMO_CODE", AppiumBy.xpath("//*[@name='codeLabel' and @visible='true']"));

    private static final Locator LOYALTY_BENEFIT_SECTION = new Locator("LOYALTY_BENEFIT_SECTION", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and (contains(@label, 'tier benefits for you') or contains(@label, 'Ada Benefit Tier'))]"));
    public static final Locator PREFERRED_PARTNER_INDICATOR_PDP = new Locator("PREFERRED_PARTNER_INDICATOR_PDP",
            AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Preferred Partner' or @label='Preferred Partner Plus' or contains(@label,'Preferred Partner')]"
                    + " | //*[@name='preferred_partner' or @name='preferred_partner_plus']"));
    public static final Locator BOOK_BUTTON = new Locator("BOOK_BUTTON", AppiumBy.xpath("//*[(@name='book_button' or @name='bookButton') and @visible='true']"));
    public static final Locator RECOMMENDED_TITLE = new Locator("RECOMMENDED_TITLE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Recommended') or contains(@label,'Rekomendasi')]"));
    public static final Locator CANCELLATION_CHARGE = new Locator("CANCELLATION_CHARGE", AppiumBy.id("cancel_with_charges"));

    //review section 
    public static final Locator REVIEW_SECTION = new Locator("REVIEW_SECTION", AppiumBy.name("TVCell<TACMHeaderWithSeeAllView>+Review"));
    public static final Locator REVIEW_RATING_SECTION = new Locator("REVIEW_RATING_SECTION", AppiumBy.name("TVCell<TACMProductDetailVerifiedBadgeView>"));
    public static final Locator REVIEW_BY_USER_SECTION = new Locator("REVIEW_BY_USER", AppiumBy.name("TVCell<TDSCarouselView>+User-Reviews"));
    public static final Locator REVIEW_CAROUSEL = new Locator("REVIEW_CAROUSEL", AppiumBy.id("TVCell<TDSCarouselView>+User-Reviews"));
    public static final Locator VISIBLE_REVIEW_CAROUSEL = new Locator("VISIBLE_REVIEW_CAROUSEL", AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`name == 'TVCell<TDSCarouselView>+User-Reviews'`]/**/XCUIElementTypeStaticText[`visible == 1 AND label CONTAINS ' ' AND label MATCHES '.*\\\\s.*\\\\s.*\\\\s.*'`]"));
    public static final Locator REVIEW_PHOTO_COUNT_PDP = new Locator("REVIEW_PHOTO_COUNT_PDP", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'Photos') or contains(@name,'Foto')]"));
    public static final Locator REVIEW_PHOTO = new Locator("REVIEW_PHOTO", AppiumBy.xpath("//XCUIElementTypeCell[@name='TVCell<TNMHomesDetailRatingImagePreviewView>']"));
    public static final Locator REVIEW_PHOTO_COUNT_BOTTOM_SHEET = new Locator("REVIEW_PHOTO_COUNT_BOTTOM_SHEET", AppiumBy.xpath("//XCUIElementTypeCell[@name='TVCell<TNMHomesDetailRatingImagePreviewView>']//XCUIElementTypeStaticText"));
    private static final Locator SEE_ALL_REVIEW_BOTTOM_SHEET = new Locator("SEE_ALL_REVIEW", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'All Reviews') or contains(@label,'Semua Review')]"));
    public static final Locator REVIEW_APP_STORE = new Locator("REVIEW_APP_STORE", AppiumBy.xpath("//XCUIElementTypeButton[@label='Not Now']"));

    //facilities section
    public static final Locator FACILITIES_SECTION = new Locator("FACILITIES_SECTION", AppiumBy.id("TVCell<TACMHeaderWithSeeAllView>+PopularFacilityHeader"));
    public static final Locator FACILITIES_GRID = new Locator("FACILITIES_GRID", AppiumBy.id("TVCell<TDSGridView>"));
    private static final Locator SEE_MAPS_BUTTON = new Locator("SEE_MAPS_BUTTON", AppiumBy.xpath("//*[@name='TVCell<TACMHeaderWithSeeAllView>+LocationHeader']//XCUIElementTypeButton | //*[@name='TVCell<TACMProductDetailLocationSummaryView>+LocationSummary']/*[(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='see maps' or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='lihat peta') and @visible='true']"));
    public static final Locator LOCATION_SECTION = new Locator("LOCATION_SECTION", AppiumBy.id("TVCell<TACMHeaderWithSeeAllView>+LocationHeader"));
    public static final Locator ABOUT_ACCOMMODATION_SECTION = new Locator("ABOUT_ACCOMMODATION_SECTION", AppiumBy.id("TVCell<TNMHeaderWithSeeAllView>+AboutAccommodationHeader"));
    public static final Locator ABOUT_ACCOMMODATION_CONTENT = new Locator("ABOUT_ACCOMMODATION_CONTENT", AppiumBy.id("TVAtomicCell<HtmlLabel>+AboutAccommodation"));
    public static final Locator TOTAL_PRICE_LABEL = new Locator("TOTAL_PRICE_LABEL", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Total harga' or @label='Total price']"));
    public static final Locator PER_NIGHT_TEXT = new Locator("PER_NIGHT_TEXT", AppiumBy.xpath("//XCUIElementTypeStaticText[ contains(@label, '/night') or contains(@label, '/malam') or contains(@name, '/room/night') or contains(@name, '/kamar/malam')]"));
    public static final Locator LABEL_PER_ROOM_PER_NIGHT = new Locator("LABEL_PER_ROOM_PER_NIGHT", AppiumBy.name("primaryContext"));
    public static final Locator HEART_ICON = new Locator("HEART_ICON", AppiumBy.xpath("//*[@name='wishlist']"));
    public static final Locator HEART_ICON_RED = new Locator("HEART_ICON_RED", AppiumBy.xpath("//*[@name='wishlist (selected)']"));
    public static final Locator MANAGE_WISHLIST = new Locator("MANAGE_WISHLIST", AppiumBy.xpath("//XCUIElementTypeButton[@label='Manage' or @label='Atur']"));

    public static final Locator YES_CONTINUE_MEET_POLICY_BTN = new Locator("YES_CONTINUE_MEET_POLICY_BTN", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'Ya,') or contains(@label,'Yes,')]"));
    public static final Locator HOTEL = new Locator("HOTEL", AppiumBy.xpath("//*[@name=\"hotel_name\"]"));
    public static final Locator CASHBACK_AMOUNT = new Locator("CASHBACK_AMOUNT",AppiumBy.name("points"));

    public String getHotelNameDetail() {
        driverTools.waitForElementToPresence(HOTEL_NAME).orElseThrow();
        return driverTools.getText(HOTEL_NAME).orElseThrow();
    }

    public String getHotelName() {
        driverTools.waitForElementToPresence(HOTEL);
        String hotel = String.valueOf(driverTools.getText(HOTEL));
        return hotel;
    }

    public String getHotelLocationDetail() {
        String location = driverTools.getText(HOTEL_LOCATION).orElseThrow();
        location = location.replace(" · ", "").replace(" . ", "");
        logger.log("location -> " + location);
        return location;
    }

    @Override
    public String checkPolicyAccommodation() {
        return ""; //FIXME: Pending implementation
    }

    @Override
    public void openAccommodationRules() {
        //FIXME: Pending implementation
    }

    @Override
    public boolean isAccommodationRulesOpen() {
        return false; //FIXME: Pending implementation
    }

    @Override
    public void closePdpDrawer() {
        //FIXME: Pending implementation
    }

    public boolean isHotelDetailShown() {
        driverTools.waitForElementsToAppear(SHARE_BUTTON);
        driverTools.waitForElementsToAppear(HOTEL_IMAGE);
        driverTools.waitForElementsToAppear(HOTEL_CATEGORY);
        driverTools.waitForElementsToAppear(HOTEL_NAME);
        driverTools.waitForElementsToAppear(HOTEL_LOCATION);
        driverTools.waitForElementsToAppear(SEE_HOTEL_ROOM_BUTTON);

        Boolean shareButtonDisplayed = driverTools.isElementDisplayed(SHARE_BUTTON);
        Boolean hotelImageDisplayed = driverTools.isElementDisplayed(HOTEL_IMAGE);
        Boolean hotelCategoryDisplayed = driverTools.isElementDisplayed(HOTEL_CATEGORY);
        Boolean hotelNameDisplayed = driverTools.waitForElementToPresence(HOTEL_NAME).isPresent();
        Boolean hotelLocationDisplayed = driverTools.isElementDisplayed(HOTEL_LOCATION);
        Boolean footerButtonDisplayed = driverTools.isElementDisplayed(SEE_HOTEL_ROOM_BUTTON);

        Assertion.assertThat("SHARE_BUTTON displayed on PDP", shareButtonDisplayed, is(true));
        Assertion.assertThat("HOTEL_IMAGE should be present", hotelImageDisplayed, is(true));
        Assertion.assertThat("HOTEL_CATEGORY should be present", hotelCategoryDisplayed, is(true));
        Assertion.assertThat("HOTEL_NAME should be present", hotelNameDisplayed, is(true));
        Assertion.assertThat("HOTEL_STAR should be present", getHotelStar(), is(true));
        Assertion.assertThat("HOTEL_LOCATION should be present", hotelLocationDisplayed, is(true));
        Assertion.assertThat("FOOTER_BUTTON should be present", footerButtonDisplayed, is(true));
        return true;
    }

    @Override
    public boolean isHaveHotelImage() {
        driverTools.waitForElementToAppear(REVIEW_APP_STORE, 10).ifPresent(driverTools::click);
        return driverTools.isElementDisplayed(HOTEL_IMAGE);
    }

    @Override
    public String getHotelReview() {
        String fullText = driverTools.getText(HOTEL_REVIEW).orElseThrow();
        Matcher matcher = Pattern.compile("\\(([^)]+)\\)").matcher(fullText);
        return matcher.find() ? matcher.group(1).strip() : fullText.strip();
    }

    @Override
    public boolean getHotelStar() {
        return driverTools.waitForElementToPresence(HOTEL_STAR).isPresent();
    }

    @Override
    public boolean isPreferredPartnerVisibleOnPdp() {
        return driverTools.swipeToElement(PREFERRED_PARTNER_INDICATOR_PDP, Swipe.UP, 10).isPresent();
    }

    public boolean checkLoyaltyTierBenefit() {
        WebElement LoyaltyTierBenefit = driverTools.waitForElementToAppear(LOYALTY_BENEFIT_SECTION).orElseThrow();
        boolean status = driverTools.isElementDisplayed(LoyaltyTierBenefit);
        logger.log("checkLoyaltyTierBenefit status -> " + status);
        return status;
    }

    public String getHotelPrice() {
        driverTools.waitForElementToAppear(HOTEL_NAME);
        driverTools.waitForElementToPresence(HOTEL_PRICE).orElseThrow();
        return driverTools.getText(HOTEL_PRICE).orElseThrow();
    }

    @Override
    public void seeHotelRoom() {
        driverTools.waitForElementToPresence(HOTEL_PRICE);
        WebElement seeHotelButton = driverTools.waitForElementToBeClickable(SEE_HOTEL_ROOM_BUTTON).orElseThrow();
        driverTools.click(seeHotelButton);
        if (driverTools.isElementDisplayed(SEE_HOTEL_ROOM_BUTTON)) {
            driverTools.clickElementByCoordinate(SEE_HOTEL_ROOM_BUTTON);
        }
    }

    @Override
    public boolean isRoomAvailable() {
        // Must match seeHotelRoom(): only See rooms CTA counts. Book/Pesan alone would select a PDP
        // that openFirstAvailablePayAtHotelPdpDeeplink cannot open via seeHotelRoom().
        return driverTools.waitForElementToPresence(SEE_HOTEL_ROOM_BUTTON, 6).isPresent();
    }

    @Override
    public void RemovePopUp() {
        if (driverTools.isElementDisplayed(HOTEL_ROOMS_POPUP)) {
            driverTools.click(HOTEL_ROOMS_POPUP);
        }
        if (driverTools.isElementDisplayed(HOTEL_ROOMS_OPTION_POPUP)) {
            driverTools.click(HOTEL_ROOMS_OPTION_POPUP);
            if (driverTools.isElementDisplayed(BACK_BUTTON)) driverTools.click(BACK_BUTTON);
            if (driverTools.isElementDisplayed(BACK)) driverTools.click(BACK);
            seeHotelRoom();
        }
    }

    @Override
    public boolean isRoomDetailShown() {
        return driverTools.swipeToElement(ROOM_CARDS, Swipe.UP, 3).isPresent();
    }

    @Override
    public void clickBookButton(int totalRoom) {
        if(driverTools.waitForElementToAppear(HOTEL_FIRST_BOOK_BUTTON, 5).isPresent()) {
            driverTools.click(HOTEL_FIRST_BOOK_BUTTON);
        } else {
            WebElement roomBookBtn = driverTools.waitForElementToPresence(BOOK_BUTTON).orElseThrow();
            driverTools.clickElementByCoordinate(roomBookBtn);
        }

        if (driverTools.waitForElementToAppear(ADD_ROOM_BUTTON).isPresent()) {
            for (int i = 1; i < totalRoom; i++) {
                driverTools.click(ADD_ROOM_BUTTON);
            }
            if (totalRoom > 1) driverTools.click(SAVE_BUTTON);
        }

        if (driverTools.waitForElementToAppear(CANCEL_BUTTON_ROOM_CAPACITY_WARNING, 10).isPresent()) {
            driverTools.click(BOOK_BUTTON_ROOM_CAPACITY_WARNING); //this one is by name
        }

        else if(driverTools.waitForElementToAppear(CANCEL_BUTTON,5).isPresent()){
            driverTools.click(CONTINUE_BOOK);
        }
      
        if (driverTools.isElementDisplayed(BOOK_BUTTON)) driverTools.click(BOOK_BUTTON);
    }

    @Override
    public void clickBookButton100pRefund(int totalRoom) {
        WebElement roomBookBtn = driverTools.swipeToElement(ROOM_BOOK_BUTTON_100P_REFUND,Swipe.UP,15).orElseThrow();
        driverTools.click(roomBookBtn);

        if (driverTools.waitForElementToAppear(ADD_ROOM_BUTTON).isPresent()) {
            for (int i = 1; i < totalRoom; i++) {
                driverTools.click(ADD_ROOM_BUTTON);
            }
            if (totalRoom > 1) driverTools.click(SAVE_BUTTON);
        }

        driverTools.click(BOOK_BUTTON);
    }

    public void clickBackButton() {
        driverTools.click(BACK_BUTTON);
    }

    @Override
    public String getRefundPolicyValue() {
        String value;

        Optional<WebElement> titleElement = driverTools.swipeToElement(REFUND_AND_RESCHEDULE_TITLE, Swipe.UP, 3);
        WebElement policyElement = titleElement.orElseGet(() ->
                driverTools.swipeToElement(REFUND_AND_RESCHEDULE_FEE, Swipe.UP, 3).orElseThrow());

        String allText = driverTools.getText(policyElement);
        String refundAndRescheduleTitle = allText.toLowerCase();
        logger.log("Refund and reschedule tittle : " + refundAndRescheduleTitle);

        if(refundAndRescheduleTitle.contains("gratis") || refundAndRescheduleTitle.contains("free") || refundAndRescheduleTitle.contains("100%")){
            value = "IDR 0";
        } else if (allText.contains("IDR")) {
            String[] split =  allText.split(" ");
            int currencyIndex = findIndex(split, "IDR");
            value = split[currencyIndex] + " " + split[currencyIndex + 1];
        } else {
            logger.log("Refund policy value is not found or in different format, value: " + allText);
            value = allText;
        }

        logger.log("Refund policy value: "+value);

        return value;
    }

    @Override
    public List<String> getCancellationCharges() {
        boolean chargePresent = driverTools.swipeToElement(CANCELLATION_CHARGE,Swipe.UP,5).isPresent();
        if(chargePresent){
            List<String> priceItems = driverTools.getTexts(CANCELLATION_CHARGE);
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
        List<String> priceItems = new ArrayList<>(driverTools.getTexts(PRICE_BREAKDOWN));
        Assertion.assertThat("Prices exist", priceItems, is(not(empty())));
        priceItems.add(driverTools.getText(TOTAL_PRICE_ON_PRICE_DETAIL).orElseThrow());
        logger.log("prices: "+priceItems);
        return priceItems;
    }

    @Override
    public void openPriceDetail() {
        driverTools.waitForElementToBeClickable(EXPAND_PRICE_DETAIL);
        driverTools.click(EXPAND_PRICE_DETAIL);
    }

    @Override
    public void closePriceDetail() {
        driverTools.click(CANCEL_BUTTON);
    }

    @Override
    public boolean priceMatch(Double price, int totalRoom) {
        double totalPriceOnPriceDetail = convertPrice(driverTools.getText(TOTAL_PRICE_ON_PRICE_DETAIL).orElseThrow());
        double expectedPrice = price * totalRoom;
        double expectedPriceFirst = expectedPrice - 1;
        double expectedPriceSecond = expectedPrice + 1;
        if (expectedPrice <= totalPriceOnPriceDetail) {
            logger.log("expectedPrice -> " + expectedPrice + " & " + "totalPriceOnPriceDetail -> " + totalPriceOnPriceDetail);
            return true;
        } else if (expectedPriceFirst <= totalPriceOnPriceDetail) {
            logger.log("expectedPriceFirst -> " + expectedPriceFirst + " & " + "totalPriceOnPriceDetail -> " + totalPriceOnPriceDetail);
            return true;
        } else if (expectedPriceSecond <= totalPriceOnPriceDetail) {
            logger.log("expectedPriceSecond -> " + expectedPriceSecond + " & " + "totalPriceOnPriceDetail -> " + totalPriceOnPriceDetail);
            return true;
        } else {
            logger.warn("expectedPrice -> " + expectedPrice + " & " + "totalPriceOnPriceDetail -> " + totalPriceOnPriceDetail);
            return false;
        }
    }

    @Override
    public boolean checkPriceBreakdown(Double totalPrice, int totalRoom) {
       List<WebElement> prices = driverTools.elements(PRICE_BREAKDOWN).orElseThrow();
       ArrayList<Double> arrayPrice = new ArrayList<>();

        for (WebElement element : prices) {
            String text = driverTools.getText(element);
            try {
                double price = convertPrice(text);
                arrayPrice.add(price);
            } catch (NumberFormatException message) {
                logger.log(message.getMessage());
            }
        }
        logger.log("arrayPrice-> " + arrayPrice);

        double sum = 0;
        for (double i : arrayPrice) {
            sum += i;
        }

        String totalPriceBreakdown = driverTools.getValueAttribute(TOTAL_PRICE_ON_PRICE_DETAIL, "label");
        double totalPricePriceDetail = convertPrice(totalPriceBreakdown);
        double actualPrice = sum;
        double totalPriceRoomDetail = totalPrice * totalRoom;
        logger.log("Total price from price detail:  " + totalPricePriceDetail);
        logger.log("Total price got:  " + actualPrice);
        logger.log("Total price from the room detail: " + totalPriceRoomDetail);

        return totalPricePriceDetail == actualPrice && totalPriceRoomDetail <= actualPrice;
    }

    @Override
    public void clickLocationTab() {
        // FIXME: Implementation Pending
    }

    @Override
    public void clickSeeMapsButton() {
        // FIXME: Implementation Pending
    }

    @Override
    public boolean isNativeMapsShown() {
        return false; //FIXME: Pending implementation
    }

    @Override
    public String getHotelNamePoi() {
        return ""; //FIXME: Pending implementation
    }

    @Override
    public boolean verifyRedirectToMaps() {
        Locator nativeMaps = new Locator("NATIVE_MAPS", AppiumBy.xpath("//XCUIElementTypeApplication[@name='Maps' or @name='Google Maps']"));
        Locator openInMaps = new Locator("OPEN_IN_MAPS", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'Maps') or contains(@name,'Maps')]"));
        boolean mapsShown = driverTools.waitForAnyLocator(10, nativeMaps, openInMaps).isPresent();
        try {
            ((IOSDriver) driver).activateApp(BundleID.getID());
        } catch (Exception ignored) {
            driverTools.back();
        }
        return mapsShown;
    }
    @Override
    public  void openReviewSection(){
        WebElement hotelReviewSection = driverTools.swipeToElement(HOTEL_REVIEW_BUTTON, Swipe.UP,5).orElseThrow();
        driverTools.clickElementByCoordinate(hotelReviewSection);
    }
    @Override
    public boolean checkHotelReview() {
        String  []getReviewCardValue = getReviewCardValues();
        String  []getReviewPageValue = getReviewPageValues();

        for(int i =0; i<getReviewPageValue.length; i++) {
            logger.log("Array elements are equal or not "+ getReviewCardValue[i] + " "+ getReviewPageValue[i]);
            if (!(getReviewCardValue[i].equals(getReviewPageValue[i]))) {
                return false;
            }
        }
        return true;
    }

    private String[] getReviewCardValues(){
        String[] reviewCardElements = new String[4];
        reviewCardElements[0] =(driverTools.getText(REVIEW_RATING).orElseThrow());
        reviewCardElements[1] = driverTools.getText(REVIEW_IMPRESSION).orElseThrow();
        reviewCardElements[2]= (driverTools.getText(TEXT_OF_RATING).orElseThrow()).replace("/","");
        String reviewCount = (driverTools.getText(REVIEW_COUNT).orElseThrow()).replace(".","");
        String[] reviewCounting = reviewCount.split(" ");
        reviewCardElements[3] = reviewCounting[1];
        for ( String str: reviewCardElements)
        {
            logger.log("Review Card Elements -> " + str);
        }
        return reviewCardElements;
    }

    private String[] getReviewPageValues(){
        driverTools.waitForElementToPresence(SEE_MORE_REVIEW_BUTTON).orElseThrow();
        driverTools.click(SEE_MORE_REVIEW_BUTTON);
        String[] getReviewPageValues = new String[4];
        WebElement reviewRatingElement = driverTools.waitForElementToPresence(REVIEW_RATING_REVIEW_PAGE).orElseThrow();
        String [] getReviewRating = driverTools.getText(reviewRatingElement).split("/");
        getReviewPageValues[0] = getReviewRating[0];
        getReviewPageValues[1] = driverTools.getText(REVIEW_IMPRESSION_REVIEW_PAGE).orElseThrow();
        getReviewPageValues[2]= getReviewRating[1];
        String reviewCountReviewPage = driverTools.getText(REVIEW_COUNT_REVIEW_PAGE).orElseThrow().replace(".","");
        String[] reviewCount = reviewCountReviewPage.split(" ");
        getReviewPageValues[3] = reviewCount[1];
        for ( String str: getReviewPageValues)
        {
            logger.log("Review Page Elements ->" + str);
        }
        return getReviewPageValues;
    }
    @Override
    public boolean checkReviewPhotoCount(){
        return false;    // FIXME: Implementation Pending
    }

    /**
     *
     * @param tierLevel varieties of tier input (Gold, Platinum, Diamond)
     * @return does the benefit detail pop up showed or not
     */
    @Override
    public boolean verifyLoyaltyBenefitPopUp(String tierLevel){
        driverTools.click(LOYALTY_BENEFIT_SECTION);
        WebElement loyaltyBenefitPopUp = driverTools.waitForElementToPresence(checkLoyaltyBenefitPopUp(tierLevel)).orElseThrow();
        boolean status = driverTools.isElementDisplayed(loyaltyBenefitPopUp);
        logger.log("verifyLoyaltyBenefitPopUp status -> " + status);
        driverTools.click(CANCEL_BUTTON);
        return status;
    }

    /**
     *
     * @param tierLevel varieties of tier input (Gold, Platinum, Diamond)
     * @return Locator of benefit detail pop up
     */
    private Locator checkLoyaltyBenefitPopUp(String tierLevel){
        return new Locator(tierLevel, AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@label, '" + tierLevel + "')])[last()]"));
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
    public void selectRoomMultiOrder(int roomcount) {

        if (roomcount > 1) {
            driverTools.swipeToElement(selectRoomMultiOrderLocator(1), Swipe.UP, 15);
            for (int i = 1; i <= roomcount; i++) {
               driverTools.swipeToElement(selectRoomMultiOrderLocator(1), Swipe.UP, 6).orElseThrow();
               WebElement webElement= driverTools.waitForElementToPresence(selectRoomMultiOrderLocator(1)).orElseThrow();
               driverTools.clickSidePartOfElement(webElement, DriverTools.SideOfElement.topleft);
            }
            driverTools.click(BOOK_BUTTON);
        } else {
            clickBookButton(roomcount);
        }
    }

    private Locator selectRoomMultiOrderLocator(int index) {
        return new Locator("ROOM_SELECT_BTN ", AppiumBy.xpath("(//XCUIElementTypeButton[(@label='Pilih' or @label='Select') and (@visible='true') and @accessible='true'])["+index+"]"));
    }

    @Override
    public void seeAllReview() {
        if (isBottomSheetReviewShown()) {
            driverTools.swipeUntilElementVisible(SEE_MORE_REVIEW_BUTTON, REVIEW_PHOTO, Swipe.DOWN, 1, 2);
        }
        driverTools.click(SEE_MORE_REVIEW_BUTTON);
    }

    /**
     * Checks if the review section is displayed on the page.
     * It first swipes to the review by user section, then checks if the review section, review rating section, and review by user section are displayed.
     * @return true if all the elements are displayed, false otherwise.
     */
    @Override
    public boolean isReviewSectionShown() {
        driverTools.swipeToElement(REVIEW_BY_USER_SECTION, Swipe.UP, 2);
        return driverTools.isElementDisplayed(REVIEW_SECTION) && driverTools.isElementDisplayed(REVIEW_RATING_SECTION) && driverTools.isElementDisplayed(REVIEW_BY_USER_SECTION);
    }

    /**
     * Checks if the facilities section is displayed on the page.
     * It first swipes to the facilities section, then checks if it and the facilities grid are displayed.
     * @return true if both elements are displayed, false otherwise.
     */
    @Override
    public boolean isFacilitiesSectionShown() {
        boolean facilitiesSection = driverTools.swipeToElement(FACILITIES_SECTION, Swipe.UP, 2).isPresent();
        logger.log("facilitiesSection -> " + facilitiesSection);
        boolean facilitiesGrid = driverTools.swipeToElement(FACILITIES_GRID, Swipe.UP, 2).isPresent();
        logger.log("facilitiesGrid -> " + facilitiesGrid);
        return facilitiesSection && facilitiesGrid;
    }

    /**
     * Checks if the location section is displayed on the page.
     * It first swipes to the location section, then checks if it is displayed.
     * @return true if the location section is displayed, false otherwise.
     */
    @Override
    public boolean isLocationSectionShown() {
        driverTools.swipeToElement(LOCATION_SECTION, Swipe.UP, 2);
        return driverTools.isElementDisplayed(LOCATION_SECTION);
    }

    /**
     * Checks if the about accommodation section is displayed on the page.
     * It first swipes to the about accommodation section, then checks if it and the about accommodation content are displayed.
     * @return true if both elements are displayed, false otherwise.
     */
    @Override   
    public boolean isAboutAccommodationSectionShown() {
        driverTools.swipeToElement(ABOUT_ACCOMMODATION_SECTION, Swipe.UP, 8);
        return driverTools.isElementDisplayed(ABOUT_ACCOMMODATION_SECTION) && driverTools.isElementDisplayed(ABOUT_ACCOMMODATION_CONTENT);
    }

    /**
     * Clicks on the hotel image and checks if the gallery is opened.
     * After checking, it clicks the back button to return to the previous page.
     * @return true if the gallery is opened, false otherwise.
     */
    @Override
    public boolean clickImageAndVerifyGallery() {
        driverTools.clickElementByCoordinate(HOTEL_IMAGE);
        driverTools.waitForElementToPresence(GALLERY_TEXT);
        boolean isGalleryOpened = driverTools.isElementDisplayed(CANCEL_BUTTON) && driverTools.isElementDisplayed(GALLERY_TEXT) && driverTools.isElementDisplayed(GALLERY_IMAGE);
        driverTools.click(CANCEL_BUTTON);
        return isGalleryOpened;
    }

    @Override
    public boolean checkReviewWidgetSwipe(){
        driverTools.swipeToElement(REVIEW_CAROUSEL,Swipe.UP,5);
        String firstReview = driverTools.getText(driverTools.waitForElementToPresence(VISIBLE_REVIEW_CAROUSEL).orElseThrow());
        logger.log("Current visible review: "+firstReview);

        driverTools.swipeByDistance(REVIEW_CAROUSEL,Swipe.LEFT,0.8,2);
        String secondReview = driverTools.getText(driverTools.waitForElementToPresence(VISIBLE_REVIEW_CAROUSEL).orElseThrow());
        logger.log("Current visible review: "+secondReview);
        Assertion.assertThat("Can swipe left to other review",secondReview,is(not(firstReview)));

        driverTools.swipeByDistance(REVIEW_CAROUSEL,Swipe.RIGHT,0.8,2);
        String thirdReview = driverTools.getText(driverTools.waitForElementToPresence(getReviewLocator(firstReview)).orElseThrow());
        logger.log("Current visible review: "+thirdReview);
        Assertion.assertThat("Can swipe right to first review",thirdReview,containsString(firstReview));

        return true;
    }

    public Locator getReviewLocator(String text) {
        return new Locator("Review", AppiumBy.xpath("//*[contains(@label,'" + text + "') and @visible='true']"));
    }

    public boolean checkReviewImageBottomSheetSwipe() {
        driverTools.click(REVIEW_PHOTO_COUNT_PDP);
        String image1Text = driverTools.getText(REVIEW_PHOTO_COUNT_BOTTOM_SHEET).orElseThrow().split("/")[0];
        driverTools.swipeLeftLong();
        String image2Text = driverTools.getText(REVIEW_PHOTO_COUNT_BOTTOM_SHEET).orElseThrow().split("/")[0];
        Assertion.assertThat("Image no should be different", image1Text, is(not(image2Text)));
        return !image1Text.equals(image2Text);
    }

    @Override
    public void clickAllReviewsBtnOnReviewBottomSheet() {
        driverTools.click(SEE_ALL_REVIEW_BOTTOM_SHEET);
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
        return driverTools.waitForElementToPresence(LABEL_PER_ROOM_PER_NIGHT).isPresent();
    }

    @Override
    public void clickHeartIcon() {
        driverTools.waitForElementToPresence(HOTEL_NAME, 30).orElseThrow();
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
        return driverTools.getText(MANAGE_WISHLIST).orElseThrow();
    }

    @Override
    public boolean isWishlistActive() {
        return driverTools.waitForElementToPresence(HEART_ICON_RED, 10).isPresent();
    }

    @Override
    public void clickLoginFromPDP() {
        //need to be implemented.
    }

    @Override
    public boolean isLoginButtonDisplayed() {
        return false;
        //FIXME
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
        driverTools.waitForElementToPresence(HEART_ICON,5);
        Assertion.assertThat("Heart icon should be turned grey", driverTools.isElementDisplayed(HEART_ICON), is(true));
    }

    @Override
    public boolean isInfinityPromoSectionDisplayed() {
        driverTools.waitForElementToPresence(INFINITY_PROMO_SECTION);
        driverTools.swipeToElement(INFINITY_PROMO_SECTION, Swipe.UP, 5);
        boolean displayed = driverTools.isElementDisplayed(INFINITY_PROMO_SECTION) || driverTools.isElementDisplayed(INFINITY_PROMO_CARDS);
        logger.log("isInfinityPromoSectionDisplayed -> " + displayed);
        return displayed;
    }

    @Override
    public int getInfinityPromoCardCount() {
        driverTools.swipeToElement(INFINITY_PROMO_CARDS, Swipe.UP, 5);
        if (!driverTools.waitForElementToPresence(INFINITY_PROMO_CODE).isPresent()) {
            logger.log("getInfinityPromoCardCount -> no promo code on carousel");
            return 0;
        }
        LinkedHashSet<String> seenPromoCodes = new LinkedHashSet<>();
        mergeVisibleInfinityPromoLabels(seenPromoCodes);
        if (seenPromoCodes.isEmpty()) {
            logger.log("getInfinityPromoCardCount -> empty visible promo code list");
            return 0;
        }

        final int maxLeftSwipes = 5;
        for (int swipe = 0; swipe < maxLeftSwipes; swipe++) {
            driverTools.swipeByDistance(INFINITY_PROMO_SECTION, Swipe.LEFT, 0.8);
            driverTools.waitForElementToPresence(INFINITY_PROMO_CODE, 5);
            mergeVisibleInfinityPromoLabels(seenPromoCodes);
        }

        int count = seenPromoCodes.size();
        logger.log("getInfinityPromoCardCount -> " + count + " distinct promo code(s) (merged visible labels; "
                + maxLeftSwipes + " left swipes max)");
        return count;
    }

    /** Collects normalized text from every visible {@link #INFINITY_PROMO_CODE} and merges into {@code seen}. */
    private void mergeVisibleInfinityPromoLabels(LinkedHashSet<String> seenPromoCodes) {
        List<WebElement> labelEls = driverTools.elements(INFINITY_PROMO_CODE).orElse(List.of());
        List<String> batch = new ArrayList<>();
        for (WebElement el : labelEls) {
            String t = normalizeNonBreakingSpaceText(driverTools.getText(el));
            if (!t.isEmpty()) {
                batch.add(t);
                seenPromoCodes.add(t);
            }
        }
        logger.log("getInfinityPromoCardCount batch -> " + batch + "; distinct merged=" + seenPromoCodes.size());
    }

    @Override
    public String copyFirstInfinityPromoCode() {
        WebElement firstCopyBtn = driverTools.swipeToElement(INFINITY_PROMO_COPY_BTN, Swipe.UP, 5).orElseThrow();
        String code = driverTools.getText(INFINITY_PROMO_CODE).orElse("");
        driverTools.clearClipboard();
        driverTools.click(firstCopyBtn);
        logger.log("copyFirstInfinityPromoCode tapped copy on first promo card; code -> " + code);
        return code;
    }

    @Override
    public boolean isSeeAllPromoButtonDisplayed() {
        return driverTools.waitForElementToPresence(SEE_ALL_PROMO_BTN).isPresent();
    }

    @Override
    public void clickSeeAllPromoButton() {
        driverTools.click(SEE_ALL_PROMO_BTN);
    }

    @Override
    public boolean isSeeAllPromoSectionShown() {
        return driverTools.waitForElementToAppear(SEE_ALL_PROMO_SECTION, 5).isPresent()
                || driverTools.waitForElementToAppear(SEE_ALL_PROMO_COPY_BTN, 3).isPresent();
    }

    @Override
    public String copyFirstPromoCodeFromSection() {
        WebElement promoCode = driverTools.waitForElementToPresence(SEE_ALL_PROMO_CODE).orElseThrow();
        String code = driverTools.getText(promoCode);
        driverTools.clearClipboard();
        driverTools.click(SEE_ALL_PROMO_COPY_BTN);
        logger.log("copyFirstPromoCodeFromSection tapped copy on first promo in see-all section; code -> " + code);
        return code;
    }

    @Override
    public void closeSeeAllPromoSection() {
        if (driverTools.isElementDisplayed(CANCEL_BUTTON)) {
            driverTools.click(CANCEL_BUTTON);
        } else if (driverTools.isElementDisplayed(BACK_BUTTON)) {
            driverTools.click(BACK_BUTTON);
        } else {
            driverTools.back();
        }
    }

    @Override
    public void openInAppMap() {
        driverTools.swipeToElement(LOCATION_TAB_BUTTON, Swipe.UP, 10);
        driverTools.click(LOCATION_TAB_BUTTON);
        driverTools.swipeToElement(SEE_MAPS_BUTTON, Swipe.UP, 5);
        driverTools.click(SEE_MAPS_BUTTON);
    }

    @Override
    public boolean isInAppMapPopupShown() {
        return driverTools.waitForElementToPresence(IN_APP_MAP_POPUP).isPresent();
    }

    @Override
    public boolean isChosenHotelHighlightedOnMap(String hotelName) { //Android only
        return driverTools.waitForElementToAppear(HOTEL_MARKER_ON_MAP, 10).isPresent();
    }

    @Override
    public boolean areNearbyPointsShownOnMap(String hotelName) { //Android only
        List<WebElement> nearbyPoints = driverTools.waitForPresenceElementsList(NEARBY_POINT).orElseThrow();
        int numberOfNearbyPoints = nearbyPoints.size();
        logger.log("areNearbyPointsShownOnMap -> " + numberOfNearbyPoints);
        return numberOfNearbyPoints > 0;
    }

    @Override
    public void clickNearbyChip() {
        driverTools.click(NEARBY_BUTTON);
    }

    @Override
    public boolean isNearbyAccommodationListShown() {
        return driverTools.waitForElementToPresence(NEARBY_ACCOMMODATION_LIST_CONTAINER, 30).isPresent();
    }

    @Override
    public boolean isNearbyChipDataShown() {
        return driverTools.waitForElementToAppear(NEARBY_CHIPS).isPresent();
    }

    @Override
    public void scrollRapidlyUntilRoomListAboutToOpen() {
        driverTools.waitForElementToAppear(HOTEL_NAME);
        for (int i = 0; i < 20; i++) {
            if (driverTools.waitForElementToPresence(SEE_HOTEL_ROOM_BUTTON, 1).isPresent()) {
                logger.log("See hotel room button is visible; room list is about to open");
                return;
            }
            driverTools.swipeUp();
        }
        throw new org.openqa.selenium.NoSuchElementException("See hotel room button not visible after rapid scroll");
    }

    @Override
    public boolean isInAppMapBackButtonWithinSafeArea() {
        WebElement backButton = driverTools.waitForElementToAppear(BACK).orElseThrow();
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
                AppiumBy.xpath("(//*[@name='hotelNameLabel' and @label='" + hotelName + "'])[1]"));
    }

    @Override
    public void clickInAppMapBackButton() {
        driverTools.waitForElementToAppear(BACK).orElseThrow();
        driverTools.click(BACK);
        driverTools.waitForElementToDisappear(BACK);
    }

    @Override
    public boolean isCrossSellNudgeShownOnNearbyAccommodation() {
        for (int i = 0; i < 5; i++) {
            if (driverTools.waitForElementToPresence(NEARBY_ACCOMMODATION_HOTEL_NAME_WITH_CROSS_SELL).isPresent()) {
                logger.log("Cross sell nudge is shown on nearby accommodation");
                return true;
            }
            WebElement itemContainer = driverTools.waitForElementToPresence(NEARBY_ACCOMMODATION_LIST_CONTAINER, 20).orElseThrow();
            driverTools.swipeByDistance(itemContainer, Swipe.LEFT, 0.9);
        }
        logger.log("Cross sell nudge is not shown on nearby accommodation");
        return driverTools.waitForElementToAppear(NEARBY_ACCOMMODATION_CROSS_SELL_NUDGE, 3).isPresent();
    }

    @Override
    public String getNearbyAccommodationHotelNameWithCrossSell() {
        if (driverTools.waitForElementToPresence(NEARBY_ACCOMMODATION_HOTEL_NAME_WITH_CROSS_SELL).isEmpty()) {
            WebElement itemContainer = driverTools.waitForElementToPresence(NEARBY_ACCOMMODATION_LIST_CONTAINER, 20).orElseThrow();
            driverTools.swipeByDistance(itemContainer, Swipe.LEFT, 0.9);
        }
        String hotelName = driverTools.getText(
            driverTools.waitForElementToPresence(NEARBY_ACCOMMODATION_HOTEL_NAME_WITH_CROSS_SELL)
            .orElseThrow()).trim();
        logger.log("Nearby accommodation with cross sell -> " + hotelName);
        return hotelName;
    }

    @Override
    public void clickNearbyAccommodationWithCrossSell() {
        if (driverTools.waitForElementToAppear(NEARBY_ACCOMMODATION_HOTEL_NAME_WITH_CROSS_SELL).isEmpty()) {
            WebElement itemContainer = driverTools.waitForElementToPresence(NEARBY_ACCOMMODATION_LIST_CONTAINER, 20).orElseThrow();
            driverTools.swipeByDistance(itemContainer, Swipe.LEFT, 0.9);
        }
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
