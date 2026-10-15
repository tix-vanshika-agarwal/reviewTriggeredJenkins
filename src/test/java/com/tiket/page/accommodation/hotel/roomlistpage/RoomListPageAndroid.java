package com.tiket.page.accommodation.hotel.roomlistpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.HotelPage;
import com.tiket.page.accommodation.hotel.bookingformpage.BookingFormPage;
import com.tiket.pojo.accommodation.hotel.ChooseFirstRoomData;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tiket.core.CoreFunctions.convertPrice;
import static com.tiket.core.CoreFunctions.splitLanguageData;
import static com.tiket.page.accommodation.hotel.detailpage.DetailPageAndroid.*;
import static com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPageAndroid.CHANGE_SEARCH_BUTTON;
import static com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPageAndroid.TOTAL_PRICE_LABEL_WITHOUT_TAX;
import static com.tiket.page.setting.SettingPageAndroid.SNACKBAR_TEXT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;

public class RoomListPageAndroid <T extends WebDriver> extends RoomListPage<T> {

    private static final Logger log = LoggerFactory.getLogger(RoomListPageAndroid.class);

    public RoomListPageAndroid(Session<T> session) {
        super(session);
    }

    private static final Locator BOOK_BUTTON_FOOTER = new Locator("BOOK_BUTTON_FOOTER", By.xpath("//*[contains(@resource-id,'stub_price_footer')]//*[contains(@resource-id,'btn_book')]"));
    private static final Locator BOOK_BUTTON_PRICE_DETAIL = new Locator("BOOK_BUTTON_PRICE_DETAIL", By.xpath("//*[contains(@resource-id, 'btn_book')]/*[contains(@resource-id, 'tds_button_view')]"));
    private static final Locator PRICE_DETAIL_SELECTED_ROOM = new Locator("PRICE_DETAIL_SELECTED_ROOM", By.xpath("//*[contains(@resource-id,'item_text')]//*[contains(@resource-id,'tv_right') and not(contains(@resource-id,'_bot'))]"));
    private static final Locator ROOM_LIST_CONTAINER = new Locator("ROOM_LIST_CONTAINER", By.id("rv_room"));
    private static final Locator PRICE_FOOTER_BUTTON = new Locator("PRICE_FOOTER_BUTTON", By.xpath("//*[contains(@resource-id,'price_footer')]//*[contains(@resource-id,'iv_icon')]"));
    private static final Locator PRICE_FOOTER_TEXT = new Locator("PRICE_FOOTER_TEXT", By.xpath("//*[contains(@resource-id,'stub_price_footer')]//*[contains(@resource-id,'tv_main_price')]"));
    private static final Locator UNSELECTED_ROOM_PRICE = new Locator("UNSELECTED_ROOM_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'v_book_button')]//preceding-sibling::*[contains(@resource-id,'v_price_module')]//*[contains(@resource-id,'price_module_final_price') or contains(@resource-id,'tv_main_price')]"));
    /** Room name from group header (when cards have a preceding ViewGroup with tv_group_name). Fails when some rooms already selected (no preceding ViewGroup). */
    private static final Locator UNSELECTED_ROOM_NAME = new Locator("UNSELECTED_ROOM_NAME", AppiumBy.xpath("//*[contains(@resource-id,'v_book_button')]//ancestor::*[contains(@resource-id, 'card_room_exclusive')]//preceding-sibling::android.view.ViewGroup//*[contains(@resource-id, 'tv_group_name') or contains(@resource-id,'tv_title')]"));
    /** Sticky header shows group/room name when some rooms are already selected (no per-card group header). */
    private static final Locator STICKY_ROOM_NAME = new Locator("STICKY_ROOM_NAME", AppiumBy.xpath("//*[contains(@resource-id,'vg_header_content')]//*[contains(@resource-id,'tv_title')]"));
    private static final Locator TIX_POINT_UP_TO = new Locator("TIX_POINT_UP_TO", By.id("tv_tix_point"));
    private static final Locator SELECT_BOOK_BUTTON = new Locator("SELECT_BOOK_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (@text='Book' or @text='Pesan')]"));
    private static final Locator EXTRA_ROOM_BOTTOMSHEET_PRIMARY_BTN = new Locator("EXTRA_ROOM_BOTTOMSHEET_PRIMARY_BTN", By.xpath("//*[contains(@resource-id,'bottomsheet_content')]//*[contains(@resource-id,'btn_primary')]"));
    private static final Locator FULLY_BOOKED_BOTTOMSHEET_BTN = new Locator("FULLY_BOOKED_BOTTOMSHEET_BTN", By.xpath("//*[contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'fully booked') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'kamar pilihanmu penuh')]/following-sibling::*[contains(@resource-id,'tds_btn_group_info_view')]"));
    private static final Locator SELECT_REFUND_AND_RESCHEDULE_ROOM = new Locator("SELECT_REFUND_AND_RESCHEDULE_BOOK_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tv_cancellation_policy') and not(contains(@text,'not') or contains(@text,'Tidak'))]"));
    private static final Locator ROOM_CARD_CONTAINER = new Locator("ROOM_CARD_CONTAINER", AppiumBy.xpath("//*[contains(@resource-id,'price')]//parent::*[contains(@class,'ViewGroup')]//parent::*[contains(@resource-id,'fl_child_container')]"));
    private static final Locator ROOM_BANNER_TEXT = new Locator("ROOM_BANNER_TEXT", By.xpath("//*[contains(@resource-id,'tds_subtitle_notification_banner')]//*[contains(@class,'TextView')]"));
    public static final Locator FIRST_HOTEL_ROOM_NAME = new Locator("FIRST_HOTEL_ROOM_NAME", By.xpath("//*[contains(@resource-id,'v_price_module')]//preceding::*[contains(@resource-id,'tv_group_name')]"));
    public static final Locator FIRST_HOTEL_ROOM_PRICE = new Locator("FIRST_HOTEL_ROOM_PRICE", By.xpath("//*[contains(@resource-id,'card_room')] [not(.//*[contains(@resource-id,'tv_room_info')])] //*[contains(@resource-id,'tv_main_price')]"));
    public static final Locator FIRST_ROOM_DETAIL_CHEVRON = new Locator("FIRST_ROOM_DETAIL_CHEVRON", By.xpath("(//*[contains(@resource-id,'iv_room_detail_chevron')])[1]"));
    private static final Locator ROOM_DETAIL_CHEVRON = new Locator("ROOM_DETAIL_CHEVRON", AppiumBy.xpath("//*[contains(@resource-id,'v_label_containers') and contains(@class,'android.widget.LinearLayout')]"));
    private static final Locator ROOM_DETAIL_PRICE_SECTION_ICON = new Locator("ROOM_DETAIL_PRICE_SECTION_ICON", AppiumBy.xpath("//*[contains(@resource-id,'iv_icon')]"));
    private static final Locator ROOM_DETAIL_PRICE_SECTION_TOTAL = new Locator("ROOM_DETAIL_PRICE_SECTION_TOTAL", AppiumBy.xpath("//*[contains(@resource-id,'tv_right_bot')]"));
    public static final Locator ROOM_RECOMMENDATION_BOOK_BTN = new Locator("ROOM_RECOMMENDATION_BOOK_BTN", AppiumBy.xpath("//*[contains(@resource-id,'card_room')]//*[contains(@resource-id,'btn_book')]"));
    public static final Locator EXCLUSIVE_ROOM_SECTION = new Locator("EXCLUSIVE_ROOM_SECTION", AppiumBy.id("card_room_exclusive"));
    private static final Locator ALTERNATE_ROOM_HEADER = new Locator("ALTERNATE_ROOM_HEADER", By.xpath("//*[contains(@resource-id,'tv_header') and (@text='No rooms match your search' or @text='Kamar incaranmu tidak tersedia')]"));
    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", By.id("tds_icon_navigation_toolbar_group"));
    public static final Locator RESCHEDULE_ROOM_LIST_VIEW = new Locator("RESCHEDULE_ROOM_LIST_VIEW", By.xpath("//*[contains(@resource-id,'rv_hotel_reschedule_room_list')]"));
    public static final Locator TEXT_VIEW_ADDITIONAL_PRICE = new Locator("TEXT_VIEW_ADDITIONAL_PRICE", By.xpath("//*[contains(@resource-id,'rv_hotel_reschedule_room_list')]/child::*//*[contains(@resource-id,'tv_price') and contains(@text,'+')][1]"));
    public static final Locator NEW_ROOM_NAME = new Locator("NEW_ROOM_NAME", By.xpath("//*[contains(@resource-id,'tv_title')]"));
    public static final Locator CARD_ROOM = new Locator("CARD_NAME", By.id("card_room"));
    public static final Locator CHILD_GUEST_COUNT = new Locator("CHILD_GUEST_COUNT", By.id("tv_child_occupancy"));
    private static final Locator NEW_CHILD_GUEST_COUNT = new Locator("NEW_CHILD_GUEST_COUNT", AppiumBy.xpath("//*[contains(@resource-id,'tv_adult_occupancy') and (contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'child') or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'anak'))]"));
    private static final Locator CHILD_MEAL_COUNT = new Locator("CHILD_MEAL_COUNT", AppiumBy.xpath("//*[contains(@resource-id,'child_container')]/*[contains(@class,'ViewGroup')]/*[contains(@resource-id,'v_label_containers')]//*[(contains(@text,'Child') or contains(@text,'Anak') or contains(@text,'child') or contains(@text,'anak')) and contains(@text,'(')]"));
    private static final Locator CHILD_MEAL_COUNT_NEW = new Locator("CHILD_MEAL_COUNT_NEW", AppiumBy.xpath("//*[contains(@resource-id,'tv_meal') and ((contains(@text,'Child') or contains(@text,'Anak') or contains(@text,'child') or contains(@text,'anak')) and contains(@text,'('))]"));
    private static final Locator SHOW_MORE_ROOMS = new Locator("SHOW_MORE_ROOMS", AppiumBy.id("tv_show_more"));
    private static final Locator CARD_ROOM_NEW = new Locator("CARD_ROOM_NEW", AppiumBy.xpath("//*[contains(@resource-id,'v_card_container')]//*[contains(@resource-id,'card_room')]"));
    private static final Locator ROOM_NAME_NEW = new Locator("ROOM_NAME_NEW", AppiumBy.id("tv_room_title"));
    public static final Locator REFUND_AND_RESCHEDULE_TEXT = new Locator("REFUND_AND_RESCHEDULE_TEXT", By.xpath("(//*[contains(@resource-id, 'tv_refund_reschedule') and contains(@text,'until') or contains(@text,' s.d. ')])[1]"));
    public static final Locator MASTER_TAG_SCROLL_VIEW = new Locator("MASTER_TAG_SCROLL_VIEW", By.id("chip_filter"));
    public static final Locator LOYALTY_BADGE = new Locator("LOYALTY_BADGE", By.id("tv_loyalty"));
    public static final Locator CLOSE_POPUP = new Locator("CLOSE_POPUP", By.id("iv_close"));
    public static final Locator ROOM_REFUND_ESTIMATION = new Locator("ROOM_REFUND_ESTIMATION",By.id("tv_get_refund"));
    private static final Locator BOOK_ROOM_BUTTON_CASHBACK = new Locator("BOOK_ROOM_BUTTON_CASHBACK", By.xpath("//*[contains(@resource-id,'tv_get_refund')]//following::*[contains(@resource-id,'btn_select_room')]"));
    public static final Locator FREE_ROOM = new Locator("FREE_ROOM", By.xpath("(//android.view.ViewGroup[./*[contains(@resource-id,'tv_free')]]//following-sibling::*[contains(@resource-id,'btn_select_room')])[1]"));
    public static final Locator FREE_ROOM_NO_CASHBACK = new Locator("FREE_ROOM_NO_CASHBACK", By.xpath("//*[contains(@resource-id,'tv_free') and not(./following-sibling::*[contains(@resource-id,'tv_get_refund')])]"));
    public static final Locator GUEST_COUNT = new Locator("GUEST_COUNT", AppiumBy.xpath("//*[contains(@resource-id,'tv_adult_occupancy')]"));
    private static final Locator ALTERNATE_TEXT = new Locator("ALTERNATE_TEXT", By.xpath("//*[contains(@resource-id,'tv_alternate')]"));
    public static final Locator ROOM_PRICE = new Locator("ROOM_PRICE", AppiumBy.xpath("//*[(contains(@resource-id,'price_module_final_price') or contains(@resource-id,'tv_main_price'))]"));
    private static final Locator HOTEL_PRICE_TAX = new Locator("HOTEL_PRICE_TAX", AppiumBy.xpath("//*[contains(@resource-id,'fl_child_container')]//*[contains(@resource-id,'v_price_module')]/*[contains(@resource-id,'tv_unit_2')]"));
    private static final Locator ROOM_QUANTITY_X = new Locator("ROOM_QUANTITY_X", AppiumBy.xpath("//*[contains(@resource-id,'card_room_exclusive')]//*[contains(@resource-id,'tds_button_view')]"));
    private static final Locator ROOM_FACILITY = new Locator("ROOM_FACILITY", AppiumBy.id("tv_facility"));
    private static final Locator TOTAL_ROOM_RECOMMENDATION_LIST = new Locator("TOTAL_ROOM_RECOMMENDATION", AppiumBy.id("tv_room_info"));
    private static final Locator TOTAL_ROOM_RECOMMENDATION_DETAIL = new Locator("TOTAL_ROOM_RECOMMENDATION", AppiumBy.xpath("//*[(contains(@resource-id,'price_module_description') or contains(@resource-id,'tv_unit_2'))]"));
    private static final Locator RECOMMENDATION_ROOM = new Locator("RECOMMENDATION_ROOM", AppiumBy.id("card_detail"));
    private static final Locator ROOM_LIST_ANNOUNCEMENT_BANNER = new Locator("ROOM_LIST_ANNOUNCEMENT_BANNER",
            AppiumBy.xpath("(//*[contains(@resource-id,'tds_content_notification_banner')])[2]"));
    private static final Locator MEETS_TRAVEL_POLICY_NUDGE = new Locator("MEETS_TRAVEL_POLICY_NUDGE", AppiumBy.xpath("(//android.widget.TextView[@text='Meets Travel Policy' or @text='Sesuai Kebijakan'])[1]"));
    private static final Locator DOES_NOT_MEET_TRAVEL_POLICY_POP_UP = new Locator("DOES_NOT_MEET_TRAVEL_POLICY_POP_UP", AppiumBy.xpath("//*[contains(@resource-id,'tds_txt_header_info_view') and (contains(@text,'This order doesn’t meet the travel policy') or contains(@text,'Pesanan ini tidak sesuai dengan kebijakan perjalanan perusahaan'))]"));
    private static final Locator PER_ROOM_PRICE_LABEL_WITHOUT_TAX = new Locator("PER_ROOM_PRICE_LABEL_WITHOUT_TAX", AppiumBy.xpath("//*[(contains(@resource-id,'price_module_description') or contains(@resource-id,'tv_unit_2')) and ((contains(@text,'/kamar/malam') or contains(@text,'/room/night')) and (contains(@text,'(setelah pajak:') or contains(@text,'(after taxes:')))]"));
    private static final Locator PER_ROOM_PRICE_LABEL = new Locator("PER_ROOM_PRICE_LABEL", AppiumBy.xpath("//*[(contains(@resource-id,'price_module_description') or contains(@resource-id,'tv_unit_2')) and ((contains(@text,'/kamar/malam') or contains(@text,'/room/night')) and (contains(@text,'(termasuk pajak)') or contains(@text,'(including taxes)')))]"));
    public static final Locator LABEL_PER_ROOM_PER_NIGHT = new Locator("LABEL_PER_ROOM_PER_NIGHT", AppiumBy.xpath("(//*[contains(@resource-id,'price_module_description') or contains(@resource-id,'tv_unit_2')])[1]"));
    private static final Locator TOTAL_PRICE_LABEL = new Locator("TOTAL_PRICE_LABEL", AppiumBy.xpath("//*[@text='Total harga (termasuk pajak)' or @text='Total price (including taxes)']"));
    private static final Locator ROOM_REFUND_RESCHEDULE = new Locator("ROOM_REFUND_RESCHEDULE", AppiumBy.xpath("//*[contains(@resource-id,'tv_cancellation_policy') and (contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'reschedule until') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'reschedule with fee') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'reschedule gratis') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'reschedule dengan biaya') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'reschedule s.d.'))]"));
    public static final Locator ROOM_FLEXIBLE_PAYMENT = new Locator("ROOM_FLEXIBLE_PAYMENT", AppiumBy.id("tv_flexible_payment"));
    private static final Locator ROOM_REFUND_RESCHEDULE_CHARGE = new Locator("ROOM_REFUND_RESCHEDULE_CHARGE", AppiumBy.xpath("//*[contains(@resource-id,'tv_cancellation_policy') and (contains(@text,'reschedule with fee') or contains(@text,'reschedule dengan biaya'))]"));

    @Override
    public void clickChangeButton() {
        driverTools.click(CHANGE_SEARCH_BUTTON);
    }

    @Override
    public void clickBookButtonFooter() {
        if (driverTools.isElementDisplayed(BOOK_BUTTON_FOOTER)) {
            driverTools.click(BOOK_BUTTON_FOOTER);
        } else if (priceBreakdownIsDisplayed()) {
            driverTools.click(BOOK_BUTTON_PRICE_DETAIL);
        } else {
            driverTools.click(BOOK_BUTTON_MULTI_ROOM);
        }
        BookingFormPage<T> bookingFormPage = BookingFormPage.getInstance(session);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
    }

    @Override
    public void clickMultiRoomBookButton(int totalRoom) {
        Locator multiRoomBookButton = getMultiRoomBookButtonLocator(totalRoom);
        driverTools.swipeElementToMidScreen(multiRoomBookButton);
        driverTools.click(multiRoomBookButton);
    }

    /**
     * Gets the locator of the footer book button that books totalRoom rooms of the same room type.
     * The locator matches the footer's book button whose text equals "Book {totalRoom} rooms" or "Pesan {totalRoom} kamar".
     *
     * @param totalRoom The total number of rooms shown on the button label.
     * @return a Locator object for the multi-room footer book button.
     */
    private Locator getMultiRoomBookButtonLocator(int totalRoom) {
        return new Locator("MULTI_ROOM_BOOK_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') " +
                "and (contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'pesan " + totalRoom + " kamar') " +
                "or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'book " + totalRoom + " room'))]"));
    }

    @Override
    public double sumPriceBreakdown() {
        List<WebElement> priceElements = driverTools.elements(PRICE_DETAIL_SELECTED_ROOM).orElseThrow();
        double totalPrice = 0;
        for (WebElement priceElement : priceElements) {
            double price = convertPrice(driverTools.getText(priceElement));
            totalPrice += price;
        }
        logger.log("totalPrice -> " + totalPrice);
        return totalPrice;
    }

    @Override
    public boolean priceBreakdownIsDisplayed() {
        return driverTools.waitForElementToAppear(PRICE_DETAIL_SELECTED_ROOM).isPresent();
    }

    @Override
    public void clickPriceFooter() {
        driverTools.click(PRICE_FOOTER_BUTTON);
    }

    @Override
    public String getPriceFooter() {
        return driverTools.getText(PRICE_FOOTER_TEXT).orElseThrow();
    }

    @Override
    public double selectRoom(int totalGuest, int roomIndex) {
        return selectRoomWithPrefer(totalGuest, roomIndex, null);
    }

    /**
     * When preferRoomName is non-null, tries to find an unselected room card with that name first (to select same room type again).
     */
    @Override
    public double selectRoomWithPrefer(int totalGuest, int roomIndex, String preferRoomName) {
        if (driverTools.isElementDisplayed(PRICE_FOOTER_TEXT)) driverTools.swipeUp();
        driverTools.waitForElementToAppear(SEE_HOTEL_ROOM_BUTTON_V4, 5).ifPresent(driverTools::click);

        Optional<WebElement> unselectedRoomName;
        if (preferRoomName != null && !preferRoomName.isEmpty()) {
            unselectedRoomName = findUnselectedRoomByName(preferRoomName);
        } else {
            unselectedRoomName = driverTools.swipeToPresentedElement(UNSELECTED_ROOM_NAME, Swipe.UP, 5);
        }

        // When some rooms are already selected, cards are direct card_room_exclusive (no preceding ViewGroup with name).
        // UNSELECTED_ROOM_NAME then does not match; find unselected card by Select button and use sticky header for name.
        if (unselectedRoomName.isEmpty()) {
            Optional<WebElement> unselectedCard = driverTools.swipeToPresentedElement(SELECT_BOOK_BUTTON, Swipe.UP, 5);
            if (unselectedCard.isPresent()) {
                unselectedRoomName = unselectedCard;
            }
        }

        String roomName = "";
        if (unselectedRoomName.isEmpty()) {
            logger.log("No more unselected room available at index " + roomIndex + (preferRoomName != null ? " (preferred: " + preferRoomName + ")" : ""));
            Assertion.fail("No unselected room card found (no Select/Pilih button) at room index " + roomIndex + "; cannot select another room.");
        } else {
            String fromNameLocator = driverTools.getText(UNSELECTED_ROOM_NAME).orElse(null);
            roomName = (fromNameLocator != null && !fromNameLocator.trim().isEmpty()) ? fromNameLocator : driverTools.getText(STICKY_ROOM_NAME).orElseThrow();
        }
        logger.log("current room name -> " + roomName);
        selectedRoomName.put(roomIndex, roomName);

        driverTools.swipeToPresentedElement(UNSELECTED_ROOM_PRICE, Swipe.UP, 5).orElseThrow();
        double priceText = convertPrice(driverTools.getText(UNSELECTED_ROOM_PRICE).orElseThrow());

        driverTools.swipeByDistance(ROOM_CARD_CONTAINER, Swipe.UP, 0.5, 1);
        Assertion.assertThat("text button on the room card is \"Select\" or \"Pilih\" at " + roomIndex + " index", driverTools.getText(SELECT_BOOK_BUTTON).orElseThrow(), either(is("Select")).or(is("Pilih")));
        driverTools.click(SELECT_BOOK_BUTTON);
        Assertion.assertThat("user can see the price footer room list after select the " + roomIndex + " of the room index", driverTools.isElementDisplayed(PRICE_FOOTER_TEXT), is(true));
        driverTools.waitForElementToDisappear(SNACKBAR_TEXT);
        driverTools.swipeUp();
        driverTools.swipeToElement(INCREMENT_BUTTON, Swipe.UP, 1);
        int currentGuest = 1;
        if (driverTools.isElementDisplayed(INCREMENT_BUTTON)) {
            driverTools.swipeToElement(INCREMENT_BUTTON, Swipe.UP, 2);
            for (int i = 1; i < totalGuest; i++) {
                driverTools.click(INCREMENT_BUTTON);
                logger.log("click increment button for guest " + (i + 1));
                currentGuest++;
            }
        }
        Assertion.assertThat("Guest count for room " + roomIndex + " should be set to " + totalGuest + " (increment button visible and used)", currentGuest, is(totalGuest));
        return priceText * currentGuest;
    }

    /**
     * Swipes through the room list to find an unselected room card whose name matches preferRoomName (trimmed, contains or equals).
     * Returns empty if not found after max swipes.
     */
    private Optional<WebElement> findUnselectedRoomByName(String preferRoomName) {
        if (preferRoomName == null || preferRoomName.isEmpty()) {
            return driverTools.swipeToPresentedElement(UNSELECTED_ROOM_NAME, Swipe.UP, 5);
        }
        String preferred = preferRoomName.trim();
        int maxTries = 5;
        for (int s = 0; s < maxTries; s++) {
            Optional<WebElement> el = driverTools.swipeToPresentedElement(UNSELECTED_ROOM_NAME, Swipe.UP, 1);
            if (el.isEmpty()) {
                return Optional.empty();
            }
            String raw = driverTools.getText(el.get());
            String currentName = raw != null ? raw.trim() : "";
            if (currentName.isEmpty()) {
                driverTools.swipeUp();
                continue;
            }
            if (currentName.equals(preferred) || currentName.contains(preferred) || preferred.contains(currentName)) {
                logger.log("Found unselected room matching preferred name: " + currentName);
                return el;
            }
            driverTools.swipeUp();
        }
        logger.log("Could not find unselected room with name matching '" + preferred + "', falling back to first unselected");
        return driverTools.swipeToPresentedElement(UNSELECTED_ROOM_NAME, Swipe.UP, 5);
    }

    @Override
    public boolean moBannerIsDisplayed() {
        driverTools.waitForElementToAppear(ROOM_BANNER_TEXT, 10);
        return driverTools.isElementDisplayed(ROOM_BANNER_TEXT);
    }

    @Override
    public String getTheFirstHotelRoomName() {
        driverTools.waitForElementToAppear(FIRST_HOTEL_ROOM_NAME);
        driverTools.swipeToElement(FIRST_HOTEL_ROOM_NAME, Swipe.UP, 5);
        if (driverTools.isElementDisplayed(FIRST_HOTEL_ROOM_NAME)) {
            return driverTools.getText(FIRST_HOTEL_ROOM_NAME).orElseThrow();
        } else {
            return driverTools.getText(NEW_ROOM_NAME).orElseThrow();
        }
    }

    private void swipeToFirstHotelRoomPrice(){
        int maxSwipes = 3;
        int swipes = 0;
        while (driverTools.isElementDisplayed(ROOM_RECOMMENDATION_BOOK_BTN) && swipes < maxSwipes) {
            driverTools.swipeUp(1);
            swipes++;
        }
        driverTools.swipeToElement(FIRST_HOTEL_ROOM_PRICE, Swipe.UP, 6);
        if (!driverTools.isElementPresent(FIRST_HOTEL_ROOM_PRICE))
            driverTools.swipeByDistance(FIRST_HOTEL_ROOM_PRICE, Swipe.UP, 0.9, 3);
    }

    @Override
    public String getTheFirstHotelRoomPrice() {
        swipeToFirstHotelRoomPrice();
        String priceText = driverTools.getText(FIRST_HOTEL_ROOM_PRICE).orElseThrow();
        logger.log("getTheFirstHotelRoomPrice -> " + priceText);
        return priceText;
    }

    @Override
    public String getTheFirstHotelRoomPrice(String currency) { // currency only for iOS compatibility
        return getTheFirstHotelRoomPrice();
    }

    @Override
    public String getTheFirstHotelRoomPrice(String vendor, boolean childMealPlan,String currency) {
        HotelPage<T> hotelPage = HotelPage.getInstance(session);
        String vendorCode = hotelPage.generateVendorCode(vendor);
        driverTools.swipeToElement(colorVendor(vendorCode, childMealPlan), Swipe.UP, 10);
        if (childMealPlan) {
            String childMealPrice;
            if (driverTools.isElementDisplayed(firstHotelRoomPriceChildMeal(vendorCode))) {
                childMealPrice = driverTools.getText(firstHotelRoomPriceChildMeal(vendorCode)).orElseThrow();
            } else {
                driverTools.swipeToElement(CARD_ROOM_NEW, Swipe.DOWN, 3).orElseThrow();
                WebElement webElement = driverTools.swipeByDistanceToElement(firstHotelRoomPriceChildMealNew(vendorCode), CARD_ROOM_NEW, Swipe.LEFT, 0.9, 5).orElseThrow();
                childMealPrice = driverTools.getText(webElement);
            }
            int idx = childMealPrice.indexOf(currency.toUpperCase());
            if (idx != -1) {
                // Extract from currency to end or until next space/parenthesis
                int endIdx = childMealPrice.indexOf("/", idx);
                if (endIdx == -1) {
                    endIdx = childMealPrice.length();
                }
                childMealPrice = childMealPrice.substring(idx, endIdx).replace(")", "").trim();
                // Remove any trailing text after the price (e.g., ")" or "/")
                int spaceIdx = childMealPrice.indexOf(" ");
                if (spaceIdx != -1) {
                    // Keep currency and price
                    childMealPrice = childMealPrice.substring(0, childMealPrice.length());
                }
            }
            logger.log("getTheFirstHotelRoomPrice with child meal plan -> " + childMealPrice);
            return childMealPrice;
        } else {
            return driverTools.getText(FIRST_HOTEL_ROOM_PRICE).orElseThrow();
        }
    }

    @Override
    public String getTheFirstRoomRefundEstimation() {
        driverTools.waitForElementToAppear(ROOM_REFUND_ESTIMATION);
        driverTools.swipeToElement(ROOM_REFUND_ESTIMATION, Swipe.UP, 5);
        String getRefundEstimation = driverTools.getValueAttribute(ROOM_REFUND_ESTIMATION, "text")
                .replace("Get refund ", "")
                .replace("Dapat refund ", "");
        logger.log("getRefundEstimation-> " + getRefundEstimation);
        return getRefundEstimation;    }

    @Override
    public void chooseRoomWithCashback() {
        driverTools.waitForElementToAppear(BOOK_ROOM_BUTTON_CASHBACK);
        driverTools.click(BOOK_ROOM_BUTTON_CASHBACK);
    }

    @Override
    public void openRoomDetailPage() {
        swipeToFirstHotelRoomPrice();
        if (driverTools.isElementDisplayed(FIRST_HOTEL_ROOM_PRICE)) {
            driverTools.clickElementByCoordinate(FIRST_HOTEL_ROOM_PRICE);
        } else {
            driverTools.click(CARD_ROOM);
        }
    }

    @Override
    public void openRoomDetailPage(String price) { // price for iOS only
        openRoomDetailPage();
    }

    @Override
    public void clickBackButton() {
        try {
            driverTools.click(BACK_BUTTON);
        } catch (Exception e) {
            logger.log("Failed to click back button: " + e.getMessage());
            driverTools.back();
        }
    }

    @Override
    public String chooseRoomWithAdditionalPrice() {
        driverTools.waitForElementToAppear(RESCHEDULE_ROOM_LIST_VIEW);
        WebElement textViewAdditionalPrice = driverTools.swipeToElement(TEXT_VIEW_ADDITIONAL_PRICE, Swipe.UP, 4).orElseThrow();
        driverTools.click(textViewAdditionalPrice);

        String newRoomName = getRoomName();

        clickBookRoom();

        return newRoomName;
    }

    @Override
    public String getPriceRoomWithAdditionalPrice() {
        driverTools.waitForElementToAppear(RESCHEDULE_ROOM_LIST_VIEW);
        WebElement textViewAdditionalPrice = driverTools.swipeToElement(TEXT_VIEW_ADDITIONAL_PRICE, Swipe.UP, 4).orElseThrow();
        return driverTools.getText(textViewAdditionalPrice);
    }

    @Override
    public ChooseFirstRoomData chooseFirstRoom() {
        ChooseFirstRoomData chooseFirstRoomData = new ChooseFirstRoomData();
        driverTools.waitForElementToAppear(RESCHEDULE_ROOM_LIST_VIEW);
        driverTools.swipeToElement(FREE_ROOM, Swipe.UP, 5);
        if(driverTools.isElementDisplayed(FREE_ROOM)){
            logger.log("FREE_ROOM is will be clicked");
            driverTools.click(FREE_ROOM);
            chooseFirstRoomData.isFreeRoom = true;
        }else {
            logger.log("room with additional price is will be clicked");
            driverTools.swipeToElement(TEXT_VIEW_ADDITIONAL_PRICE, Swipe.UP, 5);
            driverTools.click(TEXT_VIEW_ADDITIONAL_PRICE);
            chooseFirstRoomData.isFreeRoom = false;
        }
        chooseFirstRoomData.newRoomName = getRoomName();
        logger.log("newRoomName: " + chooseFirstRoomData.newRoomName);
        clickBookRoom();
        return chooseFirstRoomData;
    }

    private String getRoomName() {
        driverTools.waitForElementToAppear(NEW_ROOM_NAME);
        return driverTools.getText(NEW_ROOM_NAME).orElseThrow();
    }

    @Override
    public String getPriceAfterTax() {
        driverTools.swipeToElement(FIRST_HOTEL_ROOM_PRICE, Swipe.UP, 5);
        String priceAfterTax = driverTools.getText(HOTEL_PRICE_TAX).orElseThrow();
        priceAfterTax = priceAfterTax.split(": ")[1].replaceAll("[^a-zA-Z0-9.,\\s]", "");
        logger.log("priceAfterTax -> " + priceAfterTax);
        return priceAfterTax;
    }

    @Override
    public void clickBookRoom() {
        driverTools.swipeToElement(SELECT_BOOK_BUTTON, Swipe.UP, 5).orElseThrow();
        driverTools.swipeElementToMidScreen(SELECT_BOOK_BUTTON);
        driverTools.click(SELECT_BOOK_BUTTON);
        clickExtraRoomBottomSheetIfAppears();
        driverTools.waitForElementToDisappear(SELECT_BOOK_BUTTON);
        BookingFormPage<T> bookingFormPage = BookingFormPage.getInstance(session);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
    }

    @Override
    public void clickExtraRoomBottomSheetIfAppears() {
        driverTools.waitForElementToAppear(EXTRA_ROOM_BOTTOMSHEET_PRIMARY_BTN, 5).ifPresent(driverTools::click);
    }

    @Override
    public boolean clickFullyBookedBottomSheetIfAppears() {
        return driverTools.waitForElementToAppear(FULLY_BOOKED_BOTTOMSHEET_BTN, 5)
                .map(btn -> {
                    driverTools.click(btn);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public void clickBookRoomWithoutSkippingRatePlan() {
        driverTools.swipeToElement(SELECT_BOOK_BUTTON, Swipe.UP, 5).orElseThrow();
        driverTools.swipeElementToMidScreen(SELECT_BOOK_BUTTON);
        driverTools.click(SELECT_BOOK_BUTTON);
        driverTools.waitForElementToDisappear(SELECT_BOOK_BUTTON);
    }

    @Override
    public boolean checkChildGuest() {
        WebElement webElement;
        try {
            webElement = driverTools.swipeToElement(CHILD_GUEST_COUNT, Swipe.UP, 5).orElseThrow();
            return driverTools.isElementDisplayed(webElement);
        } catch (Exception e) {
            logger.log("CHILD_GUEST_COUNT is not found with the old UI, using new locator: " + e.getMessage());
            webElement = driverTools.swipeByDistanceToElement(NEW_CHILD_GUEST_COUNT, CARD_ROOM_NEW, Swipe.LEFT, 0.9, 5).orElseThrow();
            return driverTools.isElementDisplayed(webElement);
        }
    }

    @Override
    public boolean checkChildMeal() {
        try {
            driverTools.swipeToElement(CHILD_MEAL_COUNT, Swipe.DOWN, 1);
            if (driverTools.swipeToElement(CHILD_MEAL_COUNT, Swipe.UP, 10).isPresent()) {
                return driverTools.isElementDisplayed(CHILD_MEAL_COUNT);
            }
            logger.log("CHILD_MEAL_COUNT is not immediately visible, check and click the show more rooms button");
            if (driverTools.isElementDisplayed(SHOW_MORE_ROOMS)) {
                driverTools.click(SHOW_MORE_ROOMS);
            }
            logger.log("Attempting to swipe to CHILD_MEAL_COUNT after clicking show more rooms");
            driverTools.swipeToElement(CHILD_MEAL_COUNT, Swipe.UP, 10);
            return driverTools.isElementDisplayed(CHILD_MEAL_COUNT);
        } catch (Exception e) {
            logger.log("CHILD_MEAL_COUNT is not found with the old UI");
            driverTools.swipeToElement(ROOM_NAME_NEW, Swipe.DOWN, 1);
            driverTools.swipeToElement(ROOM_NAME_NEW, Swipe.UP, 3).orElseThrow();
            driverTools.swipeByDistanceToElement(CHILD_MEAL_COUNT_NEW, CARD_ROOM_NEW, Swipe.LEFT, 0.9, 5).orElseThrow();
            return driverTools.isElementDisplayed(CHILD_MEAL_COUNT_NEW);
        }
    }

    @Override
    public void openRoomWithRefundReschedulePolicy() {
        WebElement roomWithRefundReschedule = driverTools.swipeToElement(ROOM_REFUND_RESCHEDULE, Swipe.UP, 6).orElseThrow();
        driverTools.click(roomWithRefundReschedule);
    }

    @Override
    public void openRoomWithRefundRescheduleChargePolicy() {
        WebElement roomWithRefundRescheduleCharge = driverTools.swipeToElement(SELECT_REFUND_AND_RESCHEDULE_ROOM, Swipe.UP, 6).orElseThrow();
        driverTools.click(roomWithRefundRescheduleCharge);
    }

    @Override
    public void bookRoomWithRefundRescheduleAndFlexiblePayment() {
        for (int i = 0; i < 10; i++) {
            driverTools.swipeToElement(ROOM_REFUND_RESCHEDULE, Swipe.UP, 6);
            if (driverTools.isElementDisplayed(ROOM_FLEXIBLE_PAYMENT)) {
                logger.log("Found room with refund & reschedule and flexible payment");
                driverTools.swipeElementToMidScreen(ROOM_FLEXIBLE_PAYMENT);
                int flexiblePaymentY = driverTools.element(ROOM_FLEXIBLE_PAYMENT).orElseThrow().getLocation().getY();
                WebElement bookButton = driverTools.elements(SELECT_BOOK_BUTTON).orElseThrow().stream()
                        .filter(button -> button.getLocation().getY() > flexiblePaymentY)
                        .findFirst().orElseThrow();
                driverTools.click(bookButton);
                clickExtraRoomBottomSheetIfAppears();
                BookingFormPage<T> bookingFormPage = BookingFormPage.getInstance(session);
                bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
                return;
            }
            logger.log("No flexible payment on the refund & reschedule room, swipe to find another one");
            driverTools.swipeByDistance(ROOM_LIST_CONTAINER, Swipe.UP, 0.7, 1);
        }
        Assertion.assertThat("Room with refund & reschedule and flexible payment should be found", false, is(true));
    }

    @Override
    public String chooseRoomWithRefundReschedule() {
        WebElement textRefundAndReschedule = driverTools.swipeToElement(REFUND_AND_RESCHEDULE_TEXT, Swipe.UP, 6).orElseThrow();
        driverTools.click(textRefundAndReschedule);
        String newRoomName = getRoomName();
        return newRoomName;
    }
    @Override
    public void clickMastertag(String masterTagTitle) {
        driverTools.waitForElementToAppear(MASTER_TAG_SCROLL_VIEW);
        WebElement selectedMasterTagElement = driverTools.swipeToElement(getMasterTagLocator(masterTagTitle), MASTER_TAG_SCROLL_VIEW, Swipe.LEFT, 5).orElseThrow();
        driverTools.click(selectedMasterTagElement);
        driverTools.waitForElementToAppear(FIRST_HOTEL_ROOM_NAME);
    }

    private Locator getMasterTagLocator(String masterTagTitle) {
        // Accepts either a single label ("Pay at Hotel") or bilingual "EN/ID" ("Pay at Hotel/Bayar di Hotel").
        // splitLanguageData falls back to the same value for both sides when "/" is absent — a raw
        // split("/")[1] would throw ArrayIndexOutOfBoundsException for English-only titles.
        String[] languages = splitLanguageData(masterTagTitle, "/");
        String filterNameEnglish = languages[0].toLowerCase().strip();
        String filterNameBahasa = languages[1].toLowerCase().strip();
        // Use contains() (same as RoomListPageIOS) so chips with counts/promos/punctuation still match.
        return new Locator("ROOM_LIST_MASTER_TAG", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and (contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'"+filterNameEnglish+"') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'"+filterNameBahasa+"'))]"));
    }

    private Locator checkLoyaltyBenefitPopUp(String tierLevel){
        return new Locator(tierLevel, AppiumBy.xpath("//*[contains(@text,'Benefit') or contains(@text,'benefits') and contains(@text,'"+tierLevel+"')]"));
    }

    @Override
    public Boolean verifyLoyaltyBenefitPopUp(String tierLevel){
        driverTools.click(LOYALTY_BADGE);
        WebElement loyaltyBenefitPopUp = driverTools.waitForElementToAppear(checkLoyaltyBenefitPopUp(tierLevel)).orElseThrow();
        boolean loyaltyBenefitPopUpVisible = driverTools.isElementDisplayed(loyaltyBenefitPopUp);
        driverTools.click(CLOSE_POPUP);
        return loyaltyBenefitPopUpVisible;
    }

    @Override
    public Boolean checkLoyaltyTierBenefit(){
        WebElement  cardWithLoyaltyBenefitBadge =  driverTools.swipeToElement(LOYALTY_BADGE, Swipe.UP, 3).orElseThrow();
        return driverTools.isElementDisplayed(cardWithLoyaltyBenefitBadge);
    }

    @Override
    public String chooseRoomWithFreePayment() {
        driverTools.waitForElementToAppear(RESCHEDULE_ROOM_LIST_VIEW);
        WebElement testFreePayment = driverTools.swipeToElement(FREE_ROOM,Swipe.UP, 4).orElseThrow();
        driverTools.click(testFreePayment);

        String newRoomName = getRoomName();
        logger.log("room name -> " + newRoomName);
        return newRoomName;
    }

    @Override
    public void chooseRoomWithFreePaymentNoCashback() {
        driverTools.click(FREE_ROOM_NO_CASHBACK);
        driverTools.click(SELECT_BOOK_BUTTON);
    }

    @Override
    public boolean verifyAlternateRoomShowingCorrectly(int guest) {
        driverTools.swipeToElement(FIRST_HOTEL_ROOM_PRICE, Swipe.UP, 5);

        int swipes = 0;

        do {
            if (driverTools.waitForElementToPresence(GUEST_COUNT, 3).isEmpty()) {
                driverTools.swipeToElement(GUEST_COUNT, ROOM_LIST_CONTAINER, Swipe.UP, 2).orElseThrow();
            }
            int roomCapacity = CoreFunctions.getIntFromString(driverTools.getText(GUEST_COUNT).orElseThrow()).orElseThrow();

            if (roomCapacity > guest) {
                Assertion.assertThat("Room capacity " + roomCapacity + " matches search query", driverTools.isElementDisplayed(ALTERNATE_TEXT), is(false));
            } else if (roomCapacity == guest) {
                Assertion.assertThat("Room capacity " + roomCapacity + " matches or doesn't match search query", driverTools.isElementDisplayed(ALTERNATE_TEXT), anyOf(is(false), is(true)));
            } else {
                Assertion.assertThat("Room capacity " + roomCapacity + " doesn't match search query", driverTools.isElementDisplayed(ALTERNATE_TEXT), is(true));
            }

            driverTools.swipeUp(2);
            logger.log("Swipe no. " + swipes);
            swipes++;
        } while (swipes < 15);

        return true;
    }

    @Override
    public boolean verifyExactAlternateRoom(String expectedTextLabel, int totalLoop) {
        String textEnglish;
        String textBahasa;
        if (expectedTextLabel.contains("/")) {
            textEnglish = expectedTextLabel.split("/")[0].toLowerCase();
            textBahasa = expectedTextLabel.split("/")[1].toLowerCase();
        } else {
            textEnglish = expectedTextLabel.toLowerCase();
            textBahasa = expectedTextLabel.toLowerCase();
        }
        String alternateText;
        boolean alternateTextFound = false;

        driverTools.waitForElementToAppear(GUEST_COUNT, 3);
        for (int i = 0; i < totalLoop; i++) {
            logger.log("Swipe no. " + (i + 1));
            driverTools.swipeByDistance(ROOM_LIST_CONTAINER, Swipe.UP, 0.8, 1);
            if (i == totalLoop-1) {
                logger.log("Last swipe, checking the last guest info");
                Assertion.assertThat("Guest count is displayed", driverTools.swipeToElement(GUEST_COUNT, Swipe.UP, 1).isPresent(), is(true));
                Assertion.assertThat("Alternate text is correct", driverTools.getText(ALTERNATE_TEXT).orElseThrow().replace("\u00A0", " ").toLowerCase(), anyOf(containsString(textEnglish), containsString(textBahasa)));
                break;
            }

            if (driverTools.waitForElementToPresence(GUEST_COUNT, 3).isEmpty()) {
                driverTools.swipeToElement(GUEST_COUNT, ROOM_LIST_CONTAINER, Swipe.UP, 2).orElseThrow();
            }
            WebElement guestCount = driverTools.waitForElementToPresence(GUEST_COUNT, 3).orElseThrow();
            Assertion.assertThat("Guest count is displayed", driverTools.isElementDisplayed(guestCount), is(true));
            if (driverTools.isElementDisplayed(ALTERNATE_TEXT)) {
                alternateText = driverTools.getText(ALTERNATE_TEXT).orElseThrow().replace("\u00A0", " ").toLowerCase();
                Assertion.assertThat("Alternate text is correct", alternateText, anyOf(containsString(textEnglish), containsString(textBahasa)));
                alternateTextFound = true;
            } else if (alternateTextFound) {
                Assertion.assertThat("Alternate text should be displayed", driverTools.isElementDisplayed(ALTERNATE_TEXT), is(true));
            } else {
                logger.log("Alternate text is not displayed");
            }
        }
        return alternateTextFound;
    }

    @Override
    public List<String> getPrices(){
        driverTools.swipeToElement(HOTEL_PRICE, Swipe.UP, 5).orElseThrow();
        List<String> hotelPrices = driverTools.getTexts(HOTEL_PRICE);
        List<String> prices = new ArrayList<String>(hotelPrices);
        for(int limit=0; limit<=5; limit++){
            driverTools.swipeUp();
            if(!driverTools.isElementDisplayed(HOTEL_PRICE)) break;
            prices.addAll(driverTools.getTexts(HOTEL_PRICE));
        }
        Assertion.assertThat("Prices exist", prices, is(notNullValue()));
        logger.log("prices: "+prices);
        return prices;
    }

    @Override
    public void openFirstRoomDetail(String vendor, boolean childMealPlan) {
        HotelPage<T> hotelPage = HotelPage.getInstance(session);
        String vendorCode = hotelPage.generateVendorCode(vendor);
        Locator roomPriceLocator = colorVendor(vendorCode, childMealPlan);
        driverTools.waitForElementToAppear(roomPriceLocator);
        if (driverTools.swipeToElement(roomPriceLocator, Swipe.UP, 10).isPresent()) {
            driverTools.swipeElementToMidScreen(roomPriceLocator);
            driverTools.clickElementByCoordinate(roomPriceLocator);
        } else {
            Locator newRoomPriceLocator = colorVendorNew(vendorCode, childMealPlan);
            boolean isFound = false;
            for (int i = 0; i < 10; i++) {
                if (driverTools.swipeToElement(CARD_ROOM_NEW, Swipe.DOWN, 1).isPresent()) {
                    if (driverTools.swipeByDistanceToElement(newRoomPriceLocator, CARD_ROOM_NEW, Swipe.LEFT, 0.9, 5).isPresent()) {
                        driverTools.clickElementByCoordinate(newRoomPriceLocator);
                        isFound = true;
                        break;
                    }
                }
            }
            if (!isFound) {
                Assertion.assertThat("Vendor code is found on the room list new UI", isFound, is(true));
            }
        }
    }

    /**
     * Price text (with currency) of the child-inclusive card that belongs to the given vendor.
     * A child-inclusive room card exposes {@code tv_child_occupancy} (e.g. "2 Anak") inside its
     * {@code v_guest_info}; adult-only cards only have {@code tv_adult_occupancy}. Scoping to that
     * marker plus the vendor code inside the same {@code fl_child_container} means it cannot return an
     * adult-only card's price nor another vendor's price when multiple cards are present.
     */
    private Locator firstHotelRoomPriceChildMeal(String vendorCode) {
        return new Locator("FIRST_HOTEL_ROOM_PRICE_CHILD_MEAL_" + vendorCode, By.xpath("//*[contains(@resource-id,'fl_child_container') and .//*[contains(@resource-id,'tv_child_occupancy')]]//*[contains(@resource-id,'tv_vendor_code') and @text='"+vendorCode+"']/preceding-sibling::*[contains(@resource-id,'v_price_module')]//*[contains(@resource-id,'tv_main_price')]"));
    }

    private Locator firstHotelRoomPriceChildMealNew(String vendorCode) {
        return new Locator("NEW_FIRST_HOTEL_ROOM_PRICE_CHILD_MEAL_" + vendorCode, By.xpath("//*[contains(@resource-id,'tv_adult_occupancy') and (contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'child') or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'anak'))]/parent::*/following-sibling::*//*[contains(@resource-id,'tv_vendor_code') and @text='"+vendorCode+"']/preceding-sibling::*/*[contains(@resource-id,'tv_main_price')]"));
    }

    private Locator colorVendor(String vendorCode, boolean childMealPlan) {
        if (childMealPlan) {
            // Scope to the room card (fl_child_container) that actually accommodates children (tv_child_occupancy),
            // then find the vendor's price inside that card. Prevents matching an adult-only card that shares the
            // same vendor code but appears first in the list.
            return new Locator("VENDOR_CODE_" + vendorCode, AppiumBy.xpath("//*[contains(@resource-id,'fl_child_container') and .//*[contains(@resource-id,'tv_child_occupancy')]]//*[contains(@resource-id,'tv_vendor_code') and @text='"+vendorCode+"']/preceding-sibling::*[contains(@resource-id,'v_price_module')]//*[contains(@resource-id,'tv_main_price')]"));
        } else {
            return new Locator("VENDOR_CODE_" + vendorCode, AppiumBy.xpath("//*[contains(@resource-id,'tv_vendor_code') and @text='"+vendorCode+"']//preceding-sibling::*[contains(@resource-id,'v_price_module')]"));
        }
    }

    private Locator colorVendorNew(String vendorCode, boolean childMealPlan) {
        if (childMealPlan) {
            return new Locator("NEW_VENDOR_CODE_" + vendorCode, AppiumBy.xpath("//*[contains(@resource-id,'tv_adult_occupancy') and (contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'child') or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'anak'))]/parent::*/following-sibling::*//*[contains(@resource-id,'tv_vendor_code') and @text='"+vendorCode+"']"));
        } else {
            return new Locator("NEW_VENDOR_CODE_" + vendorCode, AppiumBy.xpath("//*[contains(@resource-id,'tv_vendor_code') and @text='"+vendorCode+"']"));
        }
    }

    @Override
    public String getRoomNudges(String nudgesText){
        Locator nudgesLocator = getRoomNudgesLocator(nudgesText);
        driverTools.swipeToElement(nudgesLocator, Swipe.DOWN, 5);
        if (driverTools.isElementDisplayed(SEE_HOTEL_ROOM_BUTTON_V4)) {
            driverTools.click(SEE_HOTEL_ROOM_BUTTON_V4);
        }
        return driverTools.getText(driverTools.swipeToElement(nudgesLocator, Swipe.UP, 5).orElseThrow());
    }

    private Locator getRoomNudgesLocator(String nudgesText){
        return new Locator("Nudges", AppiumBy.xpath("//*[@text='"+nudgesText+"']"));
    }

    private Locator getNudgesRoomLocator(String nudgesText){
        return new Locator("Nudges", AppiumBy.xpath("//*[@text='"+nudgesText+"']//ancestor::*[contains(@resource-id,'tds_cv_parent')]"));
    }

    @Override
    public void clickNudgesRoom(String nudgesText){
        driverTools.click(getNudgesRoomLocator(nudgesText));
        driverTools.waitForElementToDisappear(FIRST_HOTEL_ROOM_NAME);
    }

    public boolean assertThatPriceCurrency(String currency){
        List<String> prices = getPrices();
        for(String price: prices){
            Assertion.assertThat("Price currency is correct", price, anyOf(containsString(currency),containsString("GRATIS"),containsString("FREE")) );
        }
        return true;
    }

    @Override
    public boolean isNudgesPresent(String nudgesText) {
        return driverTools.swipeToElement(getRoomNudgesLocator(nudgesText), Swipe.UP, 5).isPresent();
    }

    /**
     * Checks the room recommendation based on the total guest, room capacity, and search criteria.
     * It calculates the updated room count and verifies the display of the room recommendation with total guest title, room info, and book button.
     * If the search criteria is "less", it also checks if the room quantity contains the updated room count.
     *
     * @param totalGuest The total number of guests.
     * @param roomCapacity The capacity of the room.
     * @param searchCriteria The search criteria to match. If it equals "less", the room quantity should contain the updated room count.
     * @return true if the room recommendation with total guest title, room info, and book button are displayed and the room quantity is correct (if applicable), false otherwise.
     */
    @Override
    public boolean checkRoomRecommendation(int totalGuest,int roomCapacity,String searchCriteria){
        boolean isRoomRecommendationWithTotalGuestTitleDisplayed;

        boolean isRoomQuantityDisplayed = true;
        int updatedRoomCount = CoreFunctions.divisionAndRoundUp(totalGuest,roomCapacity);
        logger.log("total Guest: " + totalGuest + " room capacity: " + roomCapacity + " updated room count: " + updatedRoomCount);

        WebElement roomRecommendationElement = driverTools.waitForElementToAppear(getRoomRecommendationLocator()).orElseThrow();
        String roomRecommendationTitle = driverTools.getText(roomRecommendationElement);
        logger.log("Room recommendation title: " + roomRecommendationTitle);

        if(searchCriteria.contains("less")){
            isRoomRecommendationWithTotalGuestTitleDisplayed = roomRecommendationTitle.contains(String.valueOf(totalGuest));
            Assertion.assertThat("Room recommendation with total guest title is displayed", roomRecommendationTitle, is(containsString(String.valueOf(totalGuest))));
        }else {
            isRoomRecommendationWithTotalGuestTitleDisplayed = !roomRecommendationTitle.contains(String.valueOf(totalGuest));
            Assertion.assertThat("Room recommendation with total guest title is displayed", roomRecommendationTitle, is(not(containsString(String.valueOf(totalGuest)))));
        }

        boolean isRoomRecommendationRoomInfoDisplayed = driverTools.isElementDisplayed(getRoomRecommendationRoomInfoLocator(updatedRoomCount));
        Assertion.assertThat("Room recommendation with room info is displayed", isRoomRecommendationRoomInfoDisplayed, is(true));

        boolean isRoomRecommendationBookBtnDisplayed = driverTools.isElementDisplayed(getRoomRecommendationBookBtnLocator(updatedRoomCount));
        Assertion.assertThat("Room recommendation with book button is displayed", isRoomRecommendationBookBtnDisplayed, is(true));
        return isRoomRecommendationWithTotalGuestTitleDisplayed && isRoomQuantityDisplayed && isRoomRecommendationRoomInfoDisplayed && isRoomRecommendationBookBtnDisplayed;
    }

    /**
     * Gets the locator of the room recommendation title.
     * The locator is created with the xpath of the room recommendation title element, which contains the text of total guest.
     *
     * @return a Locator object for the room recommendation title.
     */
    private Locator getRoomRecommendationLocator(){
        return new Locator("Room_Recommendation_Title", AppiumBy.xpath("(//*[contains(@resource-id,'tv_title')])[1]"));
    }

    /**
     * Gets the locator of the room recommendation room info.
     * The locator is created with the xpath of the room recommendation room info element, which contains the text of total room.
     *
     * @param totalRoom The total number of rooms.
     * @return a Locator object for the room recommendation room info.
     */
    private Locator getRoomRecommendationRoomInfoLocator(int totalRoom){
        return new Locator("Room_Recommendation_Room_Info", AppiumBy.xpath("//*[contains(@resource-id,'tv_room_info') and (contains(@text,'"+totalRoom+" room') or contains(@text,'("+totalRoom+" kamar'))]"));
    }

    /**
     * Gets the locator of the room recommendation book button.
     * The locator is created with the xpath of the room recommendation book button element, which contains the text of total room.
     *
     * @param totalRoom The total number of rooms.
     * @return a Locator object for the room recommendation book button.
     */
    private Locator getRoomRecommendationBookBtnLocator(int totalRoom) {
        return new Locator("Room_Recommendation_BOOK_BTN", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Book " + totalRoom + " room') or contains(@text,'Pesan " + totalRoom + " kamar'))]"));
    }

    /**
     * Opens the room detail for the recommended room.
     */
    @Override
    public void openRoomDetailForRecommendedRoom(){
        driverTools.click(ROOM_FACILITY);
    }

    @Override public String getRecommendedRoomAmount(){
        String recommendedRoomAmount;
        if(driverTools.isElementDisplayed(TOTAL_ROOM_RECOMMENDATION_LIST)){
            recommendedRoomAmount = driverTools.getText(TOTAL_ROOM_RECOMMENDATION_LIST).orElseThrow();
        } else{
            recommendedRoomAmount = driverTools.getText(TOTAL_ROOM_RECOMMENDATION_DETAIL).orElseThrow();
        }

        logger.log("Recommended total room amount: "+recommendedRoomAmount);
        return recommendedRoomAmount;
    }

    @Override public String getRecommendationAdditionalRoom(){
        String recommendationAdditionalRoom = driverTools.getText(ROOM_QUANTITY_X).orElseThrow();
        logger.log("Recommended addtional room: "+recommendationAdditionalRoom);
        return recommendationAdditionalRoom;
    }

    @Override
    public void clickRecommendedRoom() {
        driverTools.click(RECOMMENDATION_ROOM);

    }

    @Override
    public int getRecommendedRoomCapacity(){
        WebElement facilityElement = driverTools.waitForElementToAppear(ROOM_FACILITY).orElseThrow();
        String facility = driverTools.getText(facilityElement);
        String[] facilityArr = facility.split(",");

        for (String s : facilityArr) {
            if (s.contains("tamu") || s.contains("guest")) {
                return CoreFunctions.getIntFromString(s).orElseThrow();
            }
        }
        Assertion.fail("Guest capacity not found");
        return 0;
    }

    @Override
    public boolean checkHotelPrice(boolean isCrossPrice, String price) {
        if (isCrossPrice) {
            return checkHotelCrossPrice(price);
        } else {
            return checkHotelFinalPrice(price);
        }
    }

    @Override
    public boolean checkHotelPrice(String price){
        return driverTools.swipeToElement(getHotelPriceLocator(price), Swipe.UP, 5).isPresent();
    }

    private boolean checkHotelFinalPrice(String price) {
        return driverTools.swipeToElement(getHotelFinalPriceLocator(price), Swipe.UP, 5).isPresent();
    }

    private boolean checkHotelCrossPrice(String price) {
        return driverTools.swipeToElement(getHotelCrossPriceLocator(price), Swipe.UP, 5).isPresent();
    }

    private Locator getHotelFinalPriceLocator(String price) {
        return new Locator("HOTEL_FINAL_PRICE", AppiumBy.xpath("//*[(contains(@resource-id,'price_module_final_price') or contains(@resource-id,'tv_main_price')) and contains(@text,'"+price+"')]"));
    }

    private Locator getHotelCrossPriceLocator(String price) {
        return new Locator("HOTEL_CROSS_PRICE", AppiumBy.xpath("//*[(contains(@resource-id,'price_module_slash_price') or contains(@resource-id,'tv_slash_price')) and contains(@text,'" + price + "')]"));
    }

    private Locator getHotelPriceLocator(String price) {
        return new Locator("HOTEL_PRICE", AppiumBy.xpath("//*[(contains(@resource-id,'price_module_') or contains(@resource-id,'_price')) and contains(@text,'"+price+"')]"));
    }

    @Override
    public boolean isLabelTotalPrice(boolean isTax) {
        return (isTax) ? driverTools.swipeToElement(TOTAL_PRICE_LABEL, Swipe.UP, 3).isPresent() : driverTools.swipeToElement(TOTAL_PRICE_LABEL_WITHOUT_TAX, Swipe.UP, 3).isPresent();
    }

    @Override
    public boolean isLabelTotalPrice(){
        return driverTools.waitForElementToPresence(TOTAL_PRICE_LABEL).isPresent();
    }

    @Override
    public boolean isLabelPerRoom(boolean isTax) {
        return (isTax) ? driverTools.swipeToElement(PER_ROOM_PRICE_LABEL, Swipe.UP, 3).isPresent() : driverTools.swipeToElement(PER_ROOM_PRICE_LABEL_WITHOUT_TAX, Swipe.UP, 3).isPresent();
    }

    @Override
    public boolean isLabelPerRoom(){
        return driverTools.waitForElementToPresence(PER_ROOM_PRICE_LABEL).isPresent();
    }

    public boolean doesNotMeetTravelPolicyPopUpAppears() {
        return driverTools.isElementDisplayed(DOES_NOT_MEET_TRAVEL_POLICY_POP_UP) && driverTools.isElementDisplayed(YES_CONTINUE_MEET_POLICY_BTN);
    }

    @Override
    public boolean meetTravelPolicyDisplayed() {
        return driverTools.isElementDisplayed(MEETS_TRAVEL_POLICY_NUDGE);
    }

    @Override
    public boolean isLabelPerRoomPerNight(){
        WebElement labelPerRoomPerNight = driverTools.waitForElementToAppear(LABEL_PER_ROOM_PER_NIGHT).orElseThrow();
        String labelPerRoomPerNightText = driverTools.getText(labelPerRoomPerNight);
        return labelPerRoomPerNightText.contains("/room/night") || labelPerRoomPerNightText.contains("/kamar/malam");
    }

    @Override
    public boolean isRoomListShown() {
        boolean shown = driverTools.waitForElementToAppear(ROOM_LIST_CONTAINER, 10).isPresent()
                || driverTools.waitForElementToAppear(FIRST_HOTEL_ROOM_NAME, 5).isPresent()
                || driverTools.waitForElementToAppear(FIRST_HOTEL_ROOM_PRICE, 5).isPresent();
        logger.log("isRoomListShown -> " + shown);
        return shown;
    }

    @Override
    public void selectRoomByPriceMore(int price) {
        driverTools.swipeUp();
        for(int swipes = 0; swipes < 5; swipes++){

            List<WebElement> roomPrices = driverTools.elements(ROOM_PRICE).orElseThrow();
            for(int index = 0; index < roomPrices.size(); index++){
                String roomPrice = driverTools.getText(roomPrices.get(index));
                double pr = CoreFunctions.convertPrice(roomPrice);
                logger.log("Room price after conversion: "+ (int)pr);
                if((int)CoreFunctions.convertPrice(roomPrice) >= price){
                    List<WebElement> bookButtons = driverTools.elements(SELECT_BOOK_BUTTON).orElseThrow();
                    driverTools.click(bookButtons.get(index));
                    return;
                }
            }
            driverTools.swipeUp();
        }
        throw new AssertionError("No room with price more than " + price + " found");
    }

    private static final Locator FIRST_ROOM_CARD_MEAL_INFO = new Locator("FIRST_ROOM_CARD_MEAL_INFO",
            AppiumBy.xpath("(//*[contains(@resource-id,'v_label_containers')]"
                    + "//*[contains(@text,'Breakfast') or contains(@text,'breakfast')"
                    + " or contains(@text,'Sarapan') or contains(@text,'sarapan')"
                    + " or contains(@text,'Meal') or contains(@text,'meal')"
                    + " or contains(@text,'Makan') or contains(@text,'makan')])[1]"));

    private static final Locator FIRST_ROOM_CARD_REFUND_RESCHEDULE_INFO = new Locator("FIRST_ROOM_CARD_REFUND_RESCHEDULE_INFO",
            AppiumBy.xpath("(//*[contains(@resource-id,'tv_cancellation_policy')])[1]"));

    private static final Locator FIRST_ROOM_CARD_MAX_OCCUPANCY_INFO = new Locator("FIRST_ROOM_CARD_MAX_OCCUPANCY_INFO",
            AppiumBy.xpath("(//*[contains(@resource-id,'tv_adult_occupancy')])[1]"));

    @Override
    public boolean isMealInfoOnFirstRoomCardVisible() {
        return driverTools.waitForElementToAppear(FIRST_ROOM_CARD_MEAL_INFO, 10).isPresent();
    }

    @Override
    public boolean isRefundRescheduleInfoOnFirstRoomCardVisible() {
        return driverTools.waitForElementToAppear(FIRST_ROOM_CARD_REFUND_RESCHEDULE_INFO, 10).isPresent();
    }

    @Override
    public boolean isMaxOccupancyInfoOnFirstRoomCardVisible() {
        return driverTools.waitForElementToAppear(FIRST_ROOM_CARD_MAX_OCCUPANCY_INFO, 10).isPresent();
    }

    @Override
    public boolean isExclusiveNudgePresentOnRoomRecommendationCard(String nudgesText) {
        Locator nudgeOnRecommendationCard = new Locator("ROOM_RECOMMENDATION_CARD_NUDGE",
                AppiumBy.xpath("//*[contains(@resource-id,'tv_header_title') and @text='" + nudgesText + "']"));
        return driverTools.swipeToElement(nudgeOnRecommendationCard, Swipe.UP, 10).isPresent();
    }

    private static final Locator ROOM_RECOMMENDATION_CARD_MEAL_INFO = new Locator("ROOM_RECOMMENDATION_CARD_MEAL_INFO",
            AppiumBy.xpath("//*[contains(@resource-id,'card_detail')]"
                + "//*[contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'breakfast')"
                + " or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'sarapan')"
                + " or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'meal')"
                + " or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'makan')]"));

    private static final Locator ROOM_RECOMMENDATION_CARD_REFUND_RESCHEDULE_INFO = new Locator("ROOM_RECOMMENDATION_CARD_REFUND_RESCHEDULE_INFO",
            AppiumBy.xpath("//*[contains(@resource-id,'card_detail')]//*[contains(@resource-id,'tv_cancellation_policy') or contains(@resource-id,'tv_policy')]"));

    private static final Locator ROOM_RECOMMENDATION_CARD_MAX_OCCUPANCY_INFO = new Locator("ROOM_RECOMMENDATION_CARD_MAX_OCCUPANCY_INFO",
            AppiumBy.xpath("//*[contains(@resource-id,'card_detail')]//*[contains(@resource-id,'tv_adult_occupancy') or contains(@resource-id,'tv_facility')]"));

    private static final Locator ROOM_RECOMMENDATION_CARD_CONTAINER = new Locator("ROOM_RECOMMENDATION_CARD_CONTAINER",
            AppiumBy.xpath("(//*[contains(@resource-id,'tds_cv_parent') or contains(@resource-id,'card_room_exclusive')])[1]"));

    private static final Locator BEST_PICK_FOR_YOU_HEADER = new Locator("BEST_PICK_FOR_YOU_HEADER",
            AppiumBy.xpath("//*[contains(@resource-id,'tv_header_title') and (contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'best pick for you') or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'pilihan terbaik untukmu'))]"));

    @Override
    public boolean isRoomRecommendationCardOnTop() {
        return driverTools.waitForElementToAppear(ROOM_RECOMMENDATION_CARD_CONTAINER, 10).isPresent();
    }

    @Override
    public boolean isMealInfoOnRoomRecommendationCardVisible() {
        driverTools.swipeToElement(ROOM_RECOMMENDATION_CARD_MEAL_INFO, Swipe.DOWN, 10);
        if (driverTools.isElementDisplayed(SEE_HOTEL_ROOM_BUTTON_V4)) {
            driverTools.click(SEE_HOTEL_ROOM_BUTTON_V4);
        }
        return driverTools.swipeToElement(ROOM_RECOMMENDATION_CARD_MEAL_INFO, Swipe.UP, 10).isPresent();
    }

    @Override
    public boolean isRefundRescheduleInfoOnRoomRecommendationCardVisible() {
        return driverTools.swipeToElement(ROOM_RECOMMENDATION_CARD_REFUND_RESCHEDULE_INFO, Swipe.UP, 10).isPresent();
    }

    @Override
    public boolean isMaxOccupancyInfoOnRoomRecommendationCardVisible() {
        return driverTools.swipeToElement(ROOM_RECOMMENDATION_CARD_MAX_OCCUPANCY_INFO, Swipe.UP, 10).isPresent();
    }

    @Override
    public boolean isBestPickForYouHeaderVisible() {
        return driverTools.swipeToElement(BEST_PICK_FOR_YOU_HEADER, Swipe.UP, 10).isPresent();
    }

    @Override
    public String getBestPickForYouHeaderText() {
        return driverTools.swipeToElement(BEST_PICK_FOR_YOU_HEADER, Swipe.UP, 10)
                .map(driverTools::getText)
                .orElse("");
    }

    @Override
    public boolean isAnnouncementBannerVisible() {
        // Banner sits at the top of the room list; after scrolling to Best Pick it is above the viewport
        Optional<WebElement> banner = driverTools.swipeToElement(ROOM_LIST_ANNOUNCEMENT_BANNER, Swipe.DOWN, 10);
        if (banner.isEmpty()) {
            banner = driverTools.swipeToElement(ROOM_LIST_ANNOUNCEMENT_BANNER, Swipe.UP, 5);
        }
        if (banner.isEmpty()) {
            logger.log("Announcement banner (tds_content_notification_banner index 2) not found on room list");
            return false;
        }
        return driverTools.isElementDisplayed(banner.get());
    }

    @Override
    public void openRoomDetailFromRoomList() {
        swipeToFirstHotelRoomPrice();
        driverTools.click(FIRST_HOTEL_ROOM_PRICE);
    }

    @Override
    public void openPriceSectionOnRoomDetail() {
        WebElement priceSectionIcon = driverTools.waitForElementToAppear(ROOM_DETAIL_PRICE_SECTION_ICON)
                .or(() -> driverTools.swipeToElement(ROOM_DETAIL_PRICE_SECTION_ICON, Swipe.UP, 5))
                .orElseThrow();
        driverTools.click(priceSectionIcon);
    }

    @Override
    public String getRoomPriceFromPriceSection() {
        WebElement priceElement = driverTools.waitForElementToAppear(ROOM_DETAIL_PRICE_SECTION_TOTAL)
                .or(() -> driverTools.swipeToElement(ROOM_DETAIL_PRICE_SECTION_TOTAL, Swipe.UP, 5))
                .orElseThrow();
        String roomPrice = driverTools.getText(priceElement);
        logger.log("roomPrice from room detail price section -> " + roomPrice);
        return roomPrice;
    }
}
