package com.tiket.web.page.accommodation.hotels.hotelRoomListPage;

import com.tiket.apiPage.HotelApi;
import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.room.SelectRoomResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.*;

import java.util.List;

import static com.tiket.core.CoreFunctions.hexColorToRgbCss;
import static com.tiket.core.CoreFunctions.thousandSeparator;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.core.Is.is;

public abstract class HotelRoomListPageWeb<T extends WebDriver> extends WebPage<T> {

    public HotelRoomListPageWeb(Session<T> session) { super(session); }

    public static <T extends WebDriver> HotelRoomListPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new HotelRoomListPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new HotelRoomListPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator HEADER_ROOM_GUEST_BUTTON = new Locator("HEADER_ROOM_GUEST_BUTTON", new By.ByXPath("//*[@data-testid='room-list-search-button-open-room-guest-popup']//*[contains(@class,'SearchBarDesktop')]"));
    private static final Locator SELECT_ROOM_BUTTON = new Locator("SELECT_ROOM_BUTTON", new By.ByXPath("(//button//span[text()='Select' or text()='Pilih'])[1]"));
    private static final Locator CURRENT_ROOM_AMOUNT = new Locator("CURRENT_ROOM_AMOUNT", new By.ByXPath("(//input[contains(@class, 'QuantityEditor_quantity_input')])[1]"));
    private static final Locator BEST_PICK_TEXT = new Locator("BEST_PICK_TEXT", new By.ByXPath("(//*[@data-testid='room-recommendation-container']//h3)[2]"));
    private static final Locator ROOM_PLUS = new Locator("ROOM_PLUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[2]"));
    private static final Locator ROOM_MINUS = new Locator("ROOM_MINUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[1]"));
    private static final Locator BOOK_ROOM = new Locator("BOOK_ROOM", new By.ByXPath("//div[@data-testid='rate-plan-card-container']//button[starts-with(@data-testid,'book-button')]"));
    private static final Locator BOOK_ROOM_CONFIRMATION = new Locator("BOOK_ROOM_CONFIRMATION", new By.ByXPath("//button[starts-with(@data-testid,'book-button')]"));
    private static final Locator FIRST_ROOM_NAME= new Locator("BOOK_ROOM_DETAIL", new By.ByXPath("//div[@data-testid='room-group-desktop-title-wrapper']//span"));
    private static final Locator FIRST_ROOM_CARD = new Locator("FIRST_ROOM_CARD", new By.ByXPath("(//div[@data-testid='rate-plan-card-container'])[1]"));
    private static final Locator FIRST_ROOM_IMG = new Locator("FIRST_ROOM_IMG", new By.ByXPath("//div[starts-with(@data-testid,'group-slider-item-')]//img[@data-testid='final-result-image']"));
    private static final Locator FIRST_SELECT_CARD = new Locator("FIRST_SELECT_CARD", new By.ByXPath("//button[contains(@data-testid,'book-button')]"));
    private static final Locator AVAILABLE_PRICE_CONTAINER = new Locator("AVAILABLE_PRICE_CONTAINER", new By.ByXPath("//button[contains(@data-testid,'book-button')]//parent::*[contains(@class,'RatePlanPrice')]//parent::*[contains(@class,'RatePlanPrice')]//preceding-sibling::*[contains(@class,'RatePlanPrice_items_container')]"));
    private static final Locator SELECT_ROOM_DETAIL_BUTTON = new Locator("SELECT_ROOM_DETAIL_BUTTON", new By.ByXPath("//button[@data-testid='footer-button-book']"));
    private static final Locator SAVE_ROOM_DETAIL_BUTTON = new Locator("SAVE_ROOM_DETAIL_BUTTON", new By.ByXPath("//button[@data-testid='footer-button-save']"));
    private static final Locator PRICE_FOOTER = new Locator("PRICE_FOOTER", new By.ByXPath("//*[contains(@class,'MultiOrderPriceFooter_final_price_wrapper')]//*[contains(@class,'Text_variant')]"));
    private static final Locator EXACT_MATCH_ROOM_CONTAINER = new Locator("EXACT_MATCH_ROOM_CONTAINER", new By.ByXPath("//div[@data-testid='exact-list-container']"));
    private static final Locator MULTI_ORDER_BANNER = new Locator("MULTI_ORDER_BANNER", new By.ByXPath("//button[contains(@data-testid, 'multi-order-price-footer-button-book')]"));
    private static final Locator ROOM_NAME_DETAIL=new Locator("ROOM_NAME_DETAIL",new By.ByXPath("//span[contains(@class, 'Text_size_h2') and contains(@class, 'Text_weight_bold')]"));
    private static final Locator RECOMMENDED_ROOM_GUEST = new Locator("RECOMMENDED_ROOM_GUEST", new By.ByXPath("(//*[@data-testid='room-recommendation-container']//h3)[1]"));
    private static final Locator RECOMMENDED_ROOM = new Locator("RECOMMENDED_ROOM", new By.ByXPath("//*[@data-testid='room-recommendation-container']"));
    private static final Locator ROOM_LIST_ANNOUNCEMENT_BANNER = new Locator("ROOM_LIST_ANNOUNCEMENT_BANNER", new By.ByXPath("//*[@data-testid='announcement-banner-item-container']"));
    private static final Locator SELECT_BOOK_BUTTON = new Locator("SELECT_ROOM_BUTTON", new By.ByXPath("(//button[contains(@data-testid, 'book-button')])[1]"));
    private static final Locator FIRST_BOOK_CARD = new Locator("FIRST_BOOK_CARD", new By.ByXPath("//button[contains(@data-testid,'book-button')]"));
    private static final Locator CURRENT_CHILD_AMOUNT = new Locator("CURRENT_CHILD_AMOUNT", new By.ByXPath("(//input[contains(@class, 'QuantityEditor_quantity_input')])[3]"));
    private static final Locator CHILD_PLUS = new Locator("CHILD_PLUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[6]"));
    private static final Locator CHILD_MINUS = new Locator("CHILD_MINUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[5]"));
    private static final Locator ROOM_CARD_TITLE = new Locator("ROOM_CARD_TITLE", new By.ByXPath("(//div[contains(@data-testid, 'group-slider-item-0-image')])[1]"));
    private static final Locator POPUP_ROOM_IMAGE = new Locator("POPUP_ROOM_IMAGE", new By.ByXPath("//div[@data-testid='image-gallery-mobile-list-group-item-0']//img[@data-testid='final-result-image']"));
    private static final Locator SLIDER_COUNT = new Locator("SLIDER_COUNT", new By.ByXPath("//div[contains(@class, 'SliderCounter_slider_counter_wrapper')]//span[@data-testid='slider-text' and contains(text(), '/')]"));
    private static final Locator ROOM_IMAGE = new Locator("ROOM_IMAGE", new By.ByXPath("//div[@data-testid='main-image']//img"));
    private static final Locator OTHER_COUNT = new Locator("OTHER_COUNT", new By.ByXPath("//h3[contains(text(),'(') and contains(text(),')')]"));
    private static final Locator ROOM_IMAGE_INSIDE = new Locator("ROOM_IMAGE_INSIDE", new By.ByXPath("//div[@data-testid='image-gallery-mobile-list-group-item-0']//img"));
    private static final Locator SLIDER_TEXT = new Locator("SLIDER_TEXT", new By.ByXPath("//span[@data-testid='slider-text']"));
    private static final Locator PARENT_CLASS_IMAGE = new Locator("PARENT_CLASS_IMAGE", new By.ByXPath("//div[contains(@class,'Carousel_image_wrapper')]"));
    private static final Locator CHEVRON_BUTTON=new Locator("CHEVRON_BUTTON",new By.ByXPath("//*[contains(@data-testid,'footer-button-view-price-detail')]"));
    private static final Locator PRICE_DETAIL_PAGE=new Locator("PRICE_DETAIL_PAGE",new By.ByXPath("(//div[contains(@class,'Footer_price_details')]//span[2])[1]"));
    private static final Locator PRICE_ROOM_DETAIL=new Locator("PRICE_ROOM_DETAIL",new By.ByXPath("(//div[contains(@class,'Text_variant_price')])[2]"));
    private static final Locator PRICE_ROOM_LIST = new Locator("PRICE_ROOM_LIST",new By.ByXPath("//*[@data-testid='rate-plan-cards-container']//*[contains(@class,'RatePlanPrice_final_price')]//*[contains(@class,'price')]"));
    private static final Locator CROSSED_PRICE_ROOM_LIST = new Locator("CROSSED_PRICE_ROOM_LIST",new By.ByXPath("//*[@data-testid='rate-plan-cards-container']//*[contains(@class,'Text_diagonal_strikethrough')]//*[contains(@class,'Text_variant')]"));
    private static final Locator TOTAL_PRICE_LABEL_LIST = new Locator("TOTAL_PRICE_LABEL_LIST",new By.ByXPath("//*[@data-testid='rate-plan-cards-container']//*[contains(@class,'PriceModule_final_price')]//following-sibling::*[contains(text(),'Total harga') or contains(text(),'Total price')]"));
    private static final Locator ROOM_NIGHT_PRICE_LABEL_LIST = new Locator("ROOM_NIGHT_PRICE_LABEL_LIST",new By.ByXPath("//*[@data-testid='rate-plan-cards-container']//*[contains(@class,'PriceModule_final_price')]//following-sibling::*[contains(text(),'/kamar/malam') or contains(text(),'/room/night')]"));
    private static final Locator TOTAL_PRICE_DETAIL_PAGE=new Locator("TOTAL_PRICE_DETAIL_PAGE",new By.ByXPath("(//div[contains(@class,'Footer_price_details')]//span[2])[3]"));
    private static final Locator TOTAL_PRICE_ROOM_DETAIL=new Locator("TOTAL_PRICE_ROOM_DETAIL",new By.ByXPath("(//div[contains(@class,'Text_variant_lowEmphasis')]//span[contains(@class,'Text_variant_highEmphasis')])[1]"));
    private static final Locator TOTAL_PRICE_DETAIL_POPUP = new Locator("TOTAL_PRICE_DETAIL_POPUP", new By.ByXPath("//*[contains(@class,'PriceSummary_total_price_container')]/span[last()]"));
    private static final Locator TAX_DETAIL_PAGE=new Locator("TAX_DETAIL_PAGE",new By.ByXPath("(//div[contains(@class,'Footer_price_details')]//span[2])[2]"));
    private static final Locator SEE_ROOM_BUTTON = new Locator("SEE_ROOM_BUTTON",new By.ByXPath("//*[@data-testid='button-see-room']"));
    private static final Locator MO_BOOK_BUTTOM=new Locator("MO_BOOK_BUTTOM",new By.ByXPath("//button[@data-testid='multi-order-price-footer-button-book']\n"));
    private static final Locator BOOK_BUTTON_OF_NON_PAH_ROOM = new Locator("BOOK_BUTTON_OF_NON_PAH_ROOM", new By.ByXPath("//div[@data-testid='rate-plan-card-container'][not(descendant::*[contains(text(), 'Pay at Hotel')])]/descendant::button[starts-with(@data-testid, 'book-button-')]"));
    private static final Locator BOOK_BUTTON_OF_NON_PAH_ROOM_AND_MULTI_GUEST = new Locator("BOOK_BUTTON_OF_NON_PAH_ROOM_AND_MULTI_GUEST", new By.ByXPath("//div[@data-testid='rate-plan-card-container'][not(descendant::*[contains(., 'Pay at Hotel')]) and (descendant::*[contains(., 'match') or contains(., 'sesuai')])]/descendant::button[starts-with(@data-testid, 'book-button-')]"));
    private static final Locator SHOW_MORE_ROOMS_BUTTON = new Locator("SHOW_MORE_ROOMS_BUTTON",new By.ByXPath("//button[@data-testid='expand-rate-plans-button']"));
    private static final Locator BOOK_FROM_POPUP_BUTTON = new Locator("BOOK_FROM_POPUP_BUTTON",new By.ByXPath("//div[contains(@class, 'Popup')]//button[contains(@class, 'primary')]"));
    private static final Locator FREE_REFUND_RESCHEDULE_CHIP = new Locator("FREE_REFUND_RESCHEDULE_CHIP", new By.ByXPath("//button[@data-testid='room-list-filter-chip-item']//span[contains(text(), 'Free Refund') or contains(text(), 'Refund ')]"));
    private static final Locator FULL_REFUND_LABEL = new Locator("FULL_REFUND_LABEL", new By.ByXPath("//span[@data-testid='cancellation-policies-title' and contains(@class, 'Text_variant_positive') and contains(text(), '100%') and (contains(text(), 'refund') or contains(text(), 'Refund'))]"));
    private static final Locator RECOMMENDATION_CARD = new Locator("RECOMMENDATION_CARD", new By.ByXPath("//div[contains(@class, 'RecommendationCard_recommendation_card_container')]"));
    private static final Locator AFTER_TAX_PRICE = new Locator("AFTER_TAX_PRICE", new By.ByXPath("//span[contains(@class,'Text_text') and contains(.,'after taxes') or contains(.,'setelah pajak')]//span[contains(@class,'Text_variant_highEmphasis')]"));
    private static final Locator AFTER_TAX_PRICE_UNDER_ROOMLIST = new Locator("AFTER_TAX_PRICE_UNDER_ROOMLIST", new By.ByXPath("(//section[contains(@class,'Modal_modal_popup')]//div[contains(@class,'PriceSummary_total_price_container')]//span[contains(@class,'Text_weight_bold')])[2]"));
    private static final Locator PRICE_AFTER_CASHBACK = new Locator("PRICE_AFTER_CASHBACK",By.xpath("//*[contains(@class,'RatePlanPrice_final_price_wrapper')]/*[contains(@class,'Text_variant_price')]"));
    private static final Locator DOWN_PAYMENT_CHIP_CLOSE_ICON = new Locator("DOWN_PAYMENT_CHIP_CLOSE_ICON", new By.ByXPath("//span[contains(@class,'Chip_text__tJ9s0')][contains(text(),'DownPayment') or contains(text(),'tiket DP')]"));


    private static Locator getMasterTagFilterChipByName(String filterEN, String filterID) {
        return new Locator("MASTER_TAG_FILTER_CHIP", By.xpath(String.format("//*[@data-testid=\"room-list-filter-chip-item\"]//*[text()='%s' or text()='%s']", filterEN, filterID)));
    }

    private static Locator getMealPlanLabelOnRoomCardByRoomName(String mealPlanEN, String mealPlanID, String roomName) {
        return new Locator("MEAL_PLAN_LABEL", By.xpath(String.format("//*[contains(@class,'RoomCard_title_text')][text()='%s']//ancestor::*[@data-testid='room-card-container']//*[@data-testid='bedroom-content-container']//following-sibling::*[contains(@class,'RoomCard_list_item')][1]/p[contains(text(),'%s') or contains(text(),'%s')]", roomName, mealPlanEN, mealPlanID)));
    }

    private static Locator getMealPlanLabelOnRoomCard(String mealPlanEN, String mealPlanID) {
        return new Locator("MEAL_PLAN_LABEL", By.xpath(String.format("//*[contains(@class,'FeatureLabels_label_text') and (contains(text(),'%s') or contains(text(),'%s'))]", mealPlanEN, mealPlanID)));
    }

    private static Locator getBookButtonPrice(String price) {
        return new Locator("BOOK_BUTTON_PRICE", By.xpath(String.format("//*[text()='%s']//parent::*[contains(@class,'RatePlanPrice_final_price_wrapper')]/parent::*/parent::*/parent::*/parent::*[contains(@class,'RatePlanPrice_items_container')]//following-sibling::*[contains(@class,'RatePlanPrice_cta')]//*[starts-with(@data-testid, 'book-button-')]", price)));
    }

    private Locator getRoomGroupItemSection() {
        return new Locator("ROOM_GROUP_ITEM_SECTION", new By.ByXPath("//*[@data-testid='room-group-item-section']"));
    }

    private Locator getNudgeOnRoomCard() {
        return new Locator("NUDGE_ON_ROOM_CARD", new By.ByXPath("//div[contains(@class,'CardWithNudge_header')]\n" + "    [.//h3[contains(@class,'CardWithNudge_card_title')]]"));
    }

    private Locator getRoomGroupSectionByIndex(int index) {
        return new Locator("ROOM_GROUP_SECTION", new By.ByXPath("(//*[@data-testid='room-group-item-section'])["+index+"]"));
    }

    private Locator getRoomSizeByIndex(int index) {
        return new Locator("ROOM_SIZE", new By.ByXPath("(//*[@data-testid='room-group-item-section'])["+index+"]//li[contains(@class,'RoomGroupFacilities_room_group_facility_item') and (contains(text(),'m²') or contains(text(),'sqft') or contains(text(),'m2'))]"));
    }

    private Locator getRoomCapacityByIndex(int index) {
        return new Locator("ROOM_CAPACITY", new By.ByXPath("(//*[@data-testid='room-group-item-section'])["+index+"]//*[contains(@class,'RatePlanInformation_occupancy_item')]//*[contains(@class,'Text_variant')]"));
    }

    private Locator getRoomMealPaxByIndex(int index) {
        return new Locator("ROOM_MEAL_PAX", new By.ByXPath("(//*[@data-testid='room-group-item-section'])["+index+"]//div[@data-testid='room-card-feature-label-item']"));
    }

    private Locator getRoomBedTypeByIndex(int index) {
        return new Locator("ROOM_BED_TYPE", new By.ByXPath("(//*[@data-testid='room-group-item-section'])["+index+"]//*[contains(@class,'BedTypesContent_bed_types_content_container')]//*[contains(@class,'Text_variant')]"));
    }

    private Locator getRoomValueAddedByIndex(int index) {
        return new Locator("ROOM_VALUE_ADDED", new By.ByXPath("(//*[@data-testid='room-group-item-section'])[" + index + "]" + "//div[contains(@class,'RatePlanInformation_rate_plan_additional_info_item')]"));
    }

    private Locator getRoomPriceByIndex(int index) {
        return new Locator("ROOM_PRICE", new By.ByXPath("(//*[@data-testid='room-group-item-section'])["+index+"]//*[contains(@class,'RatePlanPrice_final_price_wrapper')]//*[contains(@class,'Text_variant')]"));
    }

    private Locator getMealInfoOnRatePlanCardByIndex(int index) {
        String lowerText = "translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')";
        return new Locator("RATE_PLAN_CARD_MEAL_INFO",
                new By.ByXPath(
                        "(//div[@data-testid='rate-plan-card-container'])[" + index + "]"
                                + "//*[self::span or self::div or self::p][("
                                + "contains(" + lowerText + ", 'breakfast')"
                                + " or contains(" + lowerText + ", 'sarapan')"
                                + " or contains(" + lowerText + ", 'meal')"
                                + " or contains(" + lowerText + ", 'makan'))]"));
    }

    private Locator getRefundRescheduleInfoOnRatePlanCardByIndex(int index) {
        return new Locator("RATE_PLAN_CARD_CANCELLATION_POLICY",
                new By.ByXPath(
                        "(//div[@data-testid='rate-plan-card-container'])[" + index + "]"
                                + "//*[@data-testid='cancellation-policies-title']"));
    }

    private Locator getMaxOccupancyInfoOnRatePlanCardByIndex(int index) {
        return new Locator("RATE_PLAN_CARD_MAX_OCCUPANCY",
                new By.ByXPath(
                        "(//div[@data-testid='rate-plan-card-container'])[" + index + "]"
                                + "//*[contains(@class,'RatePlanInformation_occupancy_information')]"
                                + "//*[contains(@class,'RatePlanInformation_occupancy_item')]"));
    }

    public void setRoomGuestAmount(int room, int adult, int child) {
        driverTools.waitForElementToAppear(HEADER_ROOM_GUEST_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(HEADER_ROOM_GUEST_BUTTON);
        setTotalRoom(room);
        setTotalGuest(adult);
        setTotalChild(child);
        clickGuestDoneAndSubmitButton();
    }

    public void clickSeeRoomButton() {
        driverTools.waitForElementToAppear(SEE_ROOM_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEE_ROOM_BUTTON);
    }

    public void checkRoomListTotalPrice(SelectRoomResponse selectRoomResponse, String selectedHotelName, String currency, String separator, boolean isTax) {
        clickSeeRoomButton();
        HotelApi<?> hotelApi = new HotelApi<>(session);
        String hotelPriceRoomListApi = currency.toUpperCase() + " " + thousandSeparator(String.valueOf((int)hotelApi.getHotelTotalPriceWithTax(isTax, selectRoomResponse)), separator);
        String hotelBasePriceRoomListApi = currency.toUpperCase() + " " + thousandSeparator(String.valueOf((int)hotelApi.getHotelTotalCrossedPriceWithTax(isTax, selectRoomResponse)), separator);
        Assertion.assertThat("Price in Room List page is same as API price for hotel " + selectedHotelName, getRoomListPrice(), is(hotelPriceRoomListApi));
        if (!hotelPriceRoomListApi.equals(hotelBasePriceRoomListApi)) {
            logger.log("Room List: There is crossed price for this hotel");
            Assertion.assertThat("Crossed price in Room List page is same as API price for hotel " + selectedHotelName, getRoomListCrossedPrice(), is(hotelBasePriceRoomListApi));
        }
    }

    public void setTotal(int current, int target, Locator plusButton, Locator minusButton, String type) {
        logger.log(type + " current value is '" + current + "'");
        if (target > current) {
            for (int i = 0; i < target - current; i++) {
                driverTools.clickWithJavascriptExecutor(plusButton);
            }
        } else if (target < current) {
            for (int i = 0; i < current - target; i++) {
                driverTools.clickWithJavascriptExecutor(minusButton);
            }
        }
    }

    public abstract String getBookingButtonText();

    public void setTotalRoom(int room) {
        int currentRoom = Integer.parseInt(driverTools.getValueAttribute(CURRENT_ROOM_AMOUNT, "value"));
        setTotal(currentRoom, room, ROOM_PLUS, ROOM_MINUS, "Room");
    }

    public abstract void clickGuestDoneButton();

    public abstract void clickRoomListChangeButton();

    public abstract void clickRoomNGuestButton();

    public boolean isRoomListSectionVisible(){
        driverTools.waitForElementToAppear(FIRST_ROOM_CARD,5);
        return driverTools.isElementDisplayed(FIRST_ROOM_CARD);
    }

    public boolean isRoomImageVisible(){
        driverTools.waitForElementToAppear(FIRST_ROOM_IMG,10);
        return driverTools.isElementDisplayed(FIRST_ROOM_IMG);
    }

    /**
     * Verifies that a nudge banner (e.g. "Exclusive", rate nudges, etc.) is visible on top of
     * a room card in the Room List section. Detection is structural: it looks for the
     * {@code CardWithNudge_card_with_nudge} wrapper that contains a
     * {@code data-testid='rate-plan-card-container'}, which is how the front-end renders
     * any nudge banner above the rate-plan card regardless of the nudge text/wording.
     *
     * @return true if at least one nudge banner is rendered on top of any room card
     */
    public boolean isExclusiveNudgeOnRoomCardVisible() {
        return driverTools.waitForElementToPresence(getNudgeOnRoomCard(), 10).isPresent();
    }

    /**
     * Returns the final price text rendered on the rate-plan card of the given room-group index
     * on the Room List section (e.g. {@code "IDR 617,661"}).
     *
     * @param index 1-based room-group index
     * @return final price text on the rate-plan card
     */
    public String getRoomListFinalPriceByIndex(int index) {
        Locator priceLocator = getRoomPriceByIndex(index);
        WebElement priceElement = driverTools.waitForElementToAppear(priceLocator, 10).orElseThrow();
        return driverTools.getText(priceElement).strip();
    }

    /**
     * Whether the rate-plan card at the given index renders any meal/breakfast indicator,
     * regardless of whether breakfast is included or excluded (e.g. "Breakfast (1 pax)",
     * "No breakfast", "Sarapan termasuk", "Tanpa sarapan", etc.).
     */
    public boolean isMealInfoOnRatePlanCardVisibleByIndex(int index) {
        return driverTools.waitForElementToPresence(getMealInfoOnRatePlanCardByIndex(index), 10).isPresent();
    }

    /**
     * Whether the rate-plan card at the given index renders refund/reschedule policy info,
     * regardless of whether it's allowed or not (anchored on
     * {@code data-testid='cancellation-policies-title'}).
     */
    public boolean isRefundRescheduleInfoOnRatePlanCardVisibleByIndex(int index) {
        return driverTools.waitForElementToPresence(getRefundRescheduleInfoOnRatePlanCardByIndex(index), 10).isPresent();
    }

    /**
     * Whether the rate-plan card at the given index renders the max-people occupancy block
     * (e.g. "1 Adult", "2 Adults, 1 Child").
     */
    public boolean isMaxOccupancyInfoOnRatePlanCardVisibleByIndex(int index) {
        return driverTools.waitForElementToPresence(getMaxOccupancyInfoOnRatePlanCardByIndex(index), 10).isPresent();
    }

    @SneakyThrows
    public void clickBookButton(String price) throws InterruptedException {
        Locator BOOK_BUTTON_PRICE = getBookButtonPrice(price);
        driverTools.waitForElementToPresence(BOOK_BUTTON_PRICE, 15).orElseThrow();
        driverTools.scroll(2,20);
        driverTools.scrollIntoWebView(BOOK_BUTTON_PRICE);
        driverTools.clickWithJavascriptExecutor(BOOK_BUTTON_PRICE);
    }

    @SneakyThrows
    public void clickBookButton() {
        driverTools.waitForElementToPresence(BOOK_ROOM, 15).orElseThrow();
        driverTools.clickWithJavascriptExecutor(BOOK_ROOM);
    }

    @SneakyThrows
    public void clickBookButtonConfirmation() throws InterruptedException {
        driverTools.waitForElementToPresence(BOOK_ROOM_CONFIRMATION, 15).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public String getRoomName(){
        WebElement webElement = driverTools.waitForElementToAppear(FIRST_ROOM_NAME).orElseThrow();
        return driverTools.getText(webElement);
    }

    public void roomListDetails(int index) {
        for (int i = 1; i <= index; i++) {
            Assertion.assertThat("Room no " + i + " is visible on room List", isRoomVisible(i, ""), is(true));
            Assertion.assertThat("Room no " + i + "'s name is visible on room List", isRoomVisible(i, "//h3"), is(true));
            Assertion.assertThat("Room no " + i + "'s guest number is visible", isRoomVisible(i, "//p)[1]"), is(true));
            Assertion.assertThat("Room no " + i + "'s bed type is visible", isRoomVisible(i, "//p)[2]"), is(true));
            Assertion.assertThat("Room no " + i + "'s Price is visible on room list card", isRoomVisible(i, "//div[contains(@class, 'final')]"), is(true));
        }
    }

    /**
     * Verifies that the new room-group / rate-plan card UI displays the required room information
     * (size, capacity, meal pax, bed type, value-added, price) on the Room List section.
     *
     * <p>The base anchor is {@code data-testid='room-group-item-section'} which wraps a room and
     * its rate plans (each rate plan uses {@code data-testid='rate-plan-card-container'}).</p>
     *
     * @param index number of room groups (from top) to verify
     */
    public void verifyRoomCardDetailsOnRoomList(int index) {
        driverTools.scrollUntilElementFound(getRoomGroupItemSection());

        int roomGroupSectionCount = driverTools.elements(getRoomGroupItemSection()).orElse(List.of()).size();
        logger.log("Room group section count: " + roomGroupSectionCount);
        int effectiveIndex = Math.min(roomGroupSectionCount, index);
        logger.log("Effective index: " + effectiveIndex);

        for (int i = 1; i <= effectiveIndex; i++) {
            Assertion.assertThat("Room no " + i + " is visible on room List",
                    isLocatorVisible(getRoomGroupSectionByIndex(i)), is(true));
            Assertion.assertThat("Room no " + i + "'s size is visible on room List",
                    isLocatorVisible(getRoomSizeByIndex(i)), is(true));
            Assertion.assertThat("Room no " + i + "'s capacity (occupancy) is visible on room List",
                    isLocatorVisible(getRoomCapacityByIndex(i)), is(true));
            Assertion.assertThat("Room no " + i + "'s meal pax (feature label) is visible on room List",
                    isLocatorVisible(getRoomMealPaxByIndex(i)), is(true));
            Assertion.assertThat("Room no " + i + "'s value-added is visible on room List",
                    isLocatorVisible(getRoomValueAddedByIndex(i)), is(true));
            Assertion.assertThat("Room no " + i + "'s price is visible on room List",
                    isLocatorVisible(getRoomPriceByIndex(i)), is(true));
        }
    }

    private boolean isLocatorVisible(Locator locator) {
        driverTools.waitForElementToAppear(locator, 5);
        return driverTools.isElementDisplayed(locator);
    }

    public boolean isRoomVisible(int index, String subElement) {
        Locator roomElement = getLocatorForRoom(index, subElement);
        driverTools.waitForElementToAppear(roomElement, 5);
        return driverTools.isElementDisplayed(roomElement);
    }

    private Locator getLocatorForRoom(int index, String subElement) {
        // Base XPath for the room container
        String xpath = "(//div[@data-testid='room-card-container'])[" + index + "]";
        // Adding the parentheses dynamically if the subElement contains "p"
        if (subElement.contains("p")) {
            xpath = "(" + xpath + subElement;
        } else if (!subElement.isEmpty()) {
            // For other subElements, append directly
            xpath += subElement;
        }
        return new Locator("IndividualRoom", new By.ByXPath(xpath));
    }

    public void setTotalChild(int child) {
        int currentChild = Integer.parseInt(driverTools.getValueAttribute(CURRENT_CHILD_AMOUNT, "value"));
        setTotal(currentChild, child, CHILD_PLUS, CHILD_MINUS, "Child");
    }

    public abstract void setTotalGuest(int guests);

    public abstract void clickGuestDoneAndSubmitButton();

    public abstract void updateRoomAndGuest(int guests, int rooms);

    public abstract void clickMultiBook();

    public void scrollToRoomListSelectSection(){
        driverTools.waitForElementToAppear(FIRST_SELECT_CARD,5);
        driverTools.scrollUntilElementFound(FIRST_SELECT_CARD);
    }

    public void clickSelectRoomButton() {
        driverTools.waitForElementsToAppear(SELECT_ROOM_BUTTON);
        driverTools.clickWithJavascriptExecutor(SELECT_ROOM_BUTTON);
    }

    public void clickSelectRoomDetailButton() {
        driverTools.waitForElementsToAppear(SELECT_ROOM_DETAIL_BUTTON);
        driverTools.clickWithJavascriptExecutor(SELECT_ROOM_DETAIL_BUTTON);
    }

    public void clickSaveRoomDetailButton() {
        driverTools.waitForElementsToAppear(SAVE_ROOM_DETAIL_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_ROOM_DETAIL_BUTTON);
    }

    public void clickSelectRoomButtonMultiOrder(int noOfRoom) {
        int totalScrollAmount = 0;

        for (int i = 1; i <= noOfRoom; i++) {
            long startTime = System.currentTimeMillis();

            while (!driverTools.isElementDisplayed(SELECT_ROOM_BUTTON)) {
                if (System.currentTimeMillis() - startTime > 10000) {
                    throw new TimeoutException("Failed to display 'Select Room' button within 10 seconds");
                }
                driverTools.scrollDown(100);
                totalScrollAmount += 100;
            }

            driverTools.scrollDown(200);
            totalScrollAmount += 200;

            driverTools.waitForElementToBeClickable(SELECT_ROOM_BUTTON);

            try {
                driverTools.clickWithJavascriptExecutor(SELECT_ROOM_BUTTON);
            } catch (ElementClickInterceptedException e) {
                driverTools.scrollDown(200);
                totalScrollAmount += 200;
                driverTools.clickWithJavascriptExecutor(SELECT_ROOM_BUTTON);
            }
        }

        driverTools.scrollDown(-totalScrollAmount);
    }
    public void scrollToIncrement(){
        driverTools.waitForElementToAppear(ROOM_PLUS,5);
        driverTools.swipeToElement(ROOM_PLUS, Swipe.UP, 5);
    }

    public void clickIncrement(){
        driverTools.waitForElementToAppear(ROOM_PLUS,5);
        for(int i = 0; i < 8; i++){
            driverTools.swipeToElement(ROOM_PLUS, Swipe.UP, 5);
            driverTools.clickWithJavascriptExecutor(ROOM_PLUS);
        }
    }

    public boolean isMoBannerVisible() {
        driverTools.scrollUpUntilElementFound(MULTI_ORDER_BANNER, 300, 5);
        driverTools.waitForElementToAppear(MULTI_ORDER_BANNER, 5);
        return driverTools.isElementDisplayed(MULTI_ORDER_BANNER);
    }

    public boolean isExactMatchRoomVisible() {
        driverTools.waitForElementToAppear(EXACT_MATCH_ROOM_CONTAINER, 5);
        return driverTools.isElementDisplayed(EXACT_MATCH_ROOM_CONTAINER);
    }

    public boolean isPriceFooterVisible() {
        driverTools.scrollUpUntilElementFound(PRICE_FOOTER, 300, 5);
        driverTools.waitForElementToAppear(PRICE_FOOTER, 5);
        return driverTools.isElementDisplayed(PRICE_FOOTER);
    }

    public void clickFooterBookButton() {
        By footerBookButton = By.xpath("//button[@data-testid='multi-order-price-footer-button-book']");
        driverTools.click(driver.findElement(footerBookButton));
    }
    public void clickBookRoomButton() {
        driverTools.waitForElementsToAppear(SELECT_BOOK_BUTTON);
        try {
            driverTools.clickWithJavascriptExecutor(SELECT_BOOK_BUTTON);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(SELECT_BOOK_BUTTON);
        }
    }

    public abstract void clickChevronButton();

    public void scrollToRoomListBookSection(){
        driverTools.waitForElementToAppear(FIRST_BOOK_CARD,5);
        driverTools.swipeToElement(FIRST_BOOK_CARD, Swipe.UP, 5);
    }

    public void recommendedRoomForGuest(int guest){
        String GuestText=String.valueOf(guest);
        WebElement headingText = driverTools.waitForElementToAppear(RECOMMENDED_ROOM_GUEST).orElseThrow();
        String text= driverTools.getText(headingText);
        if(guest==1){
            Assertion.assertThat("Guest number on recommended Text should match the searched Guest",text, containsString("solo"));
        }else{
            Assertion.assertThat("Guest number on recommended Text should match the searched Guest",text, containsString(GuestText));
        }

        WebElement BestPick=driverTools.waitForElementToAppear(BEST_PICK_TEXT).orElseThrow();
        String BestPickMessage=driverTools.getText(BestPick);

        Assertion.assertThat("Best pick for you HEADING should be visible based on Selected Language",BestPickMessage, either(containsString("Best pick for you")).or(containsString("Pilihan terbaik untukmu")));
    }

    public boolean isRecommendedRoomVisible(){
        driverTools.waitForElementToAppear(RECOMMENDED_ROOM,5);
        return driverTools.isElementDisplayed(RECOMMENDED_ROOM);
    }

    public boolean isRoomRecommendationCardOnTop() {
        driverTools.scrollIntoWebView(RECOMMENDED_ROOM);
        return driverTools.waitForElementToAppear(RECOMMENDED_ROOM, 10).isPresent()
                && driverTools.isElementDisplayed(RECOMMENDED_ROOM);
    }

    public boolean isBestPickForYouHeaderVisible() {
        driverTools.scrollIntoWebView(RECOMMENDED_ROOM);
        return driverTools.waitForElementToAppear(BEST_PICK_TEXT, 10).isPresent();
    }

    public String getBestPickForYouHeaderText() {
        driverTools.scrollIntoWebView(BEST_PICK_TEXT);
        return driverTools.getText(BEST_PICK_TEXT).orElse("");
    }

    public boolean isAnnouncementBannerVisible() {
        driverTools.scrollIntoWebView(RECOMMENDED_ROOM);
        return driverTools.waitForElementToAppear(ROOM_LIST_ANNOUNCEMENT_BANNER).isPresent();
    }

    public void selectMasterTagFilter(String en, String id) {
        driverTools.click(getMasterTagFilterChipByName(en, id));
    }

    public boolean isMealPlanOnRoomCardDisplayed(String mealPlanEN, String mealPlanID, String roomName) {
        return driverTools.waitForElementToAppear(getMealPlanLabelOnRoomCardByRoomName(mealPlanEN, mealPlanID, roomName)).isPresent();
    }

    public boolean isMealPlanOnRoomCardDisplayed(String mealPlanEN, String mealPlanID) {
        boolean result = false;
        List<WebElement> mealPlanLabel = driverTools.waitForElementsToAppear(getMealPlanLabelOnRoomCard(mealPlanEN, mealPlanID)).orElseThrow();
        for (int i = 0; i < mealPlanLabel.size(); i++) {
            if (mealPlanLabel.get(i).isDisplayed()) {
                result = true;
            } else {
                result = false;
                break;
            }
        }
        return result;
    }

    public String getRoomNameFromDetail(){
        WebElement webElement=driverTools.waitForElementToAppear(ROOM_NAME_DETAIL).orElseThrow();
        return driverTools.getText(webElement);
    }

    public void openRoomDetailFromAvailablePriceContainer(){
        driverTools.waitForElementToAppear(AVAILABLE_PRICE_CONTAINER,5);
        driverTools.scrollUntilElementFound(AVAILABLE_PRICE_CONTAINER);
        driverTools.clickWithJavascriptExecutor(AVAILABLE_PRICE_CONTAINER);
    }

    public void openRoomDetail(){
        driverTools.waitForElementToAppear(FIRST_ROOM_CARD,5);
        driverTools.clickWithJavascriptExecutor(FIRST_ROOM_CARD);
    }

    public Boolean checkRoomCardGuestCount(int count) {
        // Covering 2 language and 2 different use case of wording
        String locatorTemplate = "//*[contains(@class,'RatePlanInformation_occupancy_item')]" +
                "//*[contains(text(), '%d Tamu') or " +
                "contains(text(), '%d Guest') or " +
                "contains(text(), '%d Dewasa') or " +
                "contains(text(), '%d Adult')]";
        Locator roomCardGuest = new Locator("ROOM_CARD_GUEST_TEXT", new By.ByXPath(String.format(locatorTemplate, count, count, count, count)));

        driverTools.waitForElementToAppear(roomCardGuest);
        return driverTools.isElementDisplayed(roomCardGuest);
    }

    public Boolean checkRoomCardChildCount(int count) {
        String locatorTemplate = "//*[contains(@class,'RatePlanInformation_occupancy_item')]//*[contains(text(), '%d Anak') or contains(text(), '%d Child')]";
        Locator roomCardChild = new Locator("ROOM_CARD_CHILD_TEXT", new By.ByXPath(String.format(locatorTemplate, count, count)));

        driverTools.waitForElementToAppear(roomCardChild);
        return driverTools.isElementDisplayed(roomCardChild);
    }

    public void clickRoomCard(){
        driverTools.waitForElementToAppear(ROOM_CARD_TITLE, 5);
        driverTools.clickWithJavascriptExecutor(ROOM_CARD_TITLE);
    }

    public String savePath(int value) {
        String dynamicXPath = "(//img[@data-testid='final-result-image'])[" + value + "]";
        Locator FINAL_IMAGE = new Locator("FINAL_IMAGE", new By.ByXPath(dynamicXPath));
        WebElement webElement = driverTools.waitForElementToPresence(FINAL_IMAGE, 20).orElseThrow();
        return webElement.getAttribute("src");
    }

    public abstract void clickNext(int count);

    public void clickRoomImage() {
        driverTools.waitForElementToAppear(FIRST_ROOM_IMG, 5);
        driverTools.clickWithJavascriptExecutor(FIRST_ROOM_IMG);
    }

    public void clickRoomImageInPopUp() {
        driverTools.waitForElementToAppear(POPUP_ROOM_IMAGE, 5);
        driverTools.clickWithJavascriptExecutor(POPUP_ROOM_IMAGE);
    }

    public String getImageCount() {
        driverTools.swipeToElement(SLIDER_COUNT, Swipe.UP,5);
        WebElement webElement = driverTools.waitForElementToAppear(SLIDER_COUNT).orElseThrow();
        return driverTools.getText(webElement);
    }

    public void clickRoomImageInPDP() {
        driverTools.waitForElementToAppear(ROOM_IMAGE, 5);
        driverTools.clickWithJavascriptExecutor(ROOM_IMAGE);
    }

    public int getOtherCount() {
        driverTools.waitForElementToAppear(OTHER_COUNT, 5);
        WebElement webElement = driverTools.waitForElementToAppear(OTHER_COUNT).orElseThrow();
        String text = driverTools.getText(webElement);
        String numberOnly = text.replaceAll("[^0-9]", "");
        return Integer.parseInt(numberOnly);
    }

    public void clickRoomMainImage() {
        driverTools.waitForElementToAppear(ROOM_IMAGE_INSIDE, 5);
        driverTools.clickWithJavascriptExecutor(ROOM_IMAGE_INSIDE);
    }

    public int getSliderTotalCount() {
        driverTools.waitForElementToAppear(SLIDER_TEXT, 5);
        WebElement webElement = driverTools.waitForElementToAppear(SLIDER_TEXT).orElseThrow();
        String text = driverTools.getText(webElement);
        String[] parts = text.split("/");
        int total = Integer.parseInt(parts[1]);
        return total;
    }

    public void slideImageRight(int count) {
        for (int i = 1; i <= count; i++) {
            String dynamicXPath = "//div[contains(@data-testid, 'image-gallery-desktop-navigation-button-next')][" + i + "]";
            Locator NEXT_BUTTON = new Locator("NEXT_BUTTON", new By.ByXPath(dynamicXPath));
            driverTools.waitForElementToAppear(NEXT_BUTTON, 5);
            driverTools.clickWithJavascriptExecutor(NEXT_BUTTON);
        }
    }

    public void slideImageLeft(int count) {
        for (int i = count; i >= 1; i--) {
            String dynamicXPath = "//div[contains(@data-testid, 'image-gallery-desktop-navigation-button-previous')][" + i + "]";
            Locator NEXT_BUTTON = new Locator("NEXT_BUTTON", new By.ByXPath(dynamicXPath));
            driverTools.waitForElementToAppear(NEXT_BUTTON, 5);
            driverTools.clickWithJavascriptExecutor(NEXT_BUTTON);
        }
    }

    public void clickOnPriceDetails(){
        driverTools.waitForElementToAppear(CHEVRON_BUTTON);
        driverTools.click(CHEVRON_BUTTON);
    }

    public void goToRoomDetailPriceSection() {
        openRoomDetail();
        clickOnPriceDetails();
    }

    public String getTotalPriceFromPriceDetail() {
        WebElement priceElement = driverTools.waitForElementToAppear(TOTAL_PRICE_DETAIL_POPUP, 10).orElseThrow();
        String roomPrice = driverTools.getText(priceElement);
        logger.log("roomPrice from room detail price section -> " + roomPrice);
        return roomPrice;
    }

    public void clickBookRoom() {
        clickBookRoomButton();
    }

    private Locator getBookRoomButtonByVendorCodeLocator(String vendorCode) {
        return new Locator(
                "BOOK_ROOM_BY_VENDOR_CODE",
                new By.ByXPath(
                        String.format(
                                "//span[normalize-space()='%s']/ancestor::div[contains(@class,'RatePlanCard_rate_plan_card_price_info')]//button[contains(@data-testid,'book-button')]",
                                vendorCode
                        )
                )
        );
    }

    public void clickBookRoomByVendorCode(String vendorCode) {
        Locator bookButton = getBookRoomButtonByVendorCodeLocator(vendorCode);
        driverTools.waitForElementToAppear(bookButton);
        driverTools.scrollUntilElementFound(bookButton);
        driverTools.clickWithJavascriptExecutor(bookButton);
    }

    public boolean checkPriceOnRoomDetail(String roomPrice){
        WebElement webElement=driverTools.waitForElementToAppear(PRICE_DETAIL_PAGE).orElseThrow();
        String val=webElement.getText();
        return val.contains(roomPrice);
    }

    public String getRoomPrice(){
        WebElement webElement=driverTools.waitForElementToAppear(PRICE_ROOM_DETAIL).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getRoomCapacityTextByIndex(int index) {
        WebElement webElement = driverTools.waitForElementToAppear(getRoomCapacityByIndex(index)).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getRoomListPrice() {
        driverTools.waitForElementToAppear(PRICE_ROOM_LIST).orElseThrow();
        driverTools.scrollUntilElementFound(PRICE_ROOM_LIST);
        return driverTools.getText(PRICE_ROOM_LIST).orElseThrow();
    }

    public String getRoomListCrossedPrice() {
        driverTools.waitForElementToAppear(CROSSED_PRICE_ROOM_LIST).orElseThrow();
        driverTools.scrollUntilElementFound(CROSSED_PRICE_ROOM_LIST);
        return driverTools.getText(CROSSED_PRICE_ROOM_LIST).orElseThrow();
    }

    public boolean isLabelTotalPrice() {
        driverTools.scrollUntilElementFound(TOTAL_PRICE_LABEL_LIST);
        return driverTools.isElementDisplayed(TOTAL_PRICE_LABEL_LIST);
    }

    public boolean isLabelRoomNightPrice() {
        driverTools.scrollUntilElementFound(ROOM_NIGHT_PRICE_LABEL_LIST);
        return driverTools.isElementDisplayed(ROOM_NIGHT_PRICE_LABEL_LIST);
    }

    public boolean checkTaxOnRoomDetail(){
        return driverTools.isElementPresent(TAX_DETAIL_PAGE);
    }

    public void checkFinalPriceOnRoomDetail(String finalPrice){
        String priceText1= String.valueOf(driverTools.getText(PRICE_DETAIL_PAGE)).replaceAll("[^0-9]", "");
        String priceText2= String.valueOf(driverTools.getText(TAX_DETAIL_PAGE)).replaceAll("[^0-9]", "");
        if (priceText1.isEmpty() || priceText2.isEmpty()) {
            logger.log("Price not present");
        }
        int price1=Integer.parseInt(priceText1);
        int price2=Integer.parseInt(priceText2);
        int total=price1+price2;
        WebElement webElement1=driverTools.waitForElementToAppear(TOTAL_PRICE_DETAIL_PAGE).orElseThrow();
        String val1=webElement1.getText().replaceAll("[^0-9]", "");
        if (val1.isEmpty()) {
            logger.log("Price not present");
        }
        int value=Integer.parseInt(val1);
        Assertion.assertThat("Final price numeric part matches val1", finalPrice.replaceAll("[^0-9]", ""), is(val1));
        Assertion.assertThat("Total equals value", total, is(value));
    }

    public String getFinalPrice(){
        WebElement webElement = driverTools.waitForElementToAppear(TOTAL_PRICE_ROOM_DETAIL).orElseThrow();
        return driverTools.getText(webElement);
    }

    public int getSliderCurrCount() {
        driverTools.waitForElementToAppear(SLIDER_TEXT, 5);
        WebElement webElement = driverTools.waitForElementToAppear(SLIDER_TEXT).orElseThrow();
        String text = driverTools.getText(webElement);
        String[] parts = text.split("/");
        return Integer.parseInt(parts[0]);
    }

    public void scrollToRoomListSection(){
        driverTools.waitForElementToAppear(FIRST_ROOM_CARD,5);
        driverTools.scrollIntoWebView(FIRST_ROOM_CARD);
    }

    public void clickMultiOrderBookButton() {
        driverTools.scrollToElement(FIRST_ROOM_CARD);
        driverTools.waitForElementToAppear(MO_BOOK_BUTTOM);
        driverTools.clickWithJavascriptExecutor(MO_BOOK_BUTTOM);
    }

    @SneakyThrows
    public void clickBookButtonOfNonPAHRoomAndMultiGuest() {
        driverTools.waitForElementToAppear(SHOW_MORE_ROOMS_BUTTON, 15).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToPresence(BOOK_BUTTON_OF_NON_PAH_ROOM_AND_MULTI_GUEST, 15).orElseThrow();
        driverTools.scrollIntoWebView(BOOK_BUTTON_OF_NON_PAH_ROOM_AND_MULTI_GUEST);
        driverTools.clickWithJavascriptExecutor(BOOK_BUTTON_OF_NON_PAH_ROOM_AND_MULTI_GUEST);
        driverTools.waitForElementToAppear(BOOK_FROM_POPUP_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    /**
     * Clicks on the "Free Refund & Reschedule" filter chip
     */
    public void clickFreeRefundRescheduleFilter() {
        driverTools.waitForElementToAppear(FREE_REFUND_RESCHEDULE_CHIP, 10);
        driverTools.scrollIntoWebView(FREE_REFUND_RESCHEDULE_CHIP);
        driverTools.clickWithJavascriptExecutor(FREE_REFUND_RESCHEDULE_CHIP);
        driverTools.dummyWait(2);
        logger.log("Clicked on Free Refund & Reschedule filter chip");
    }

    /**
     * Checks if the Free Refund & Reschedule filter chip is selected (has active state)
     */
    public boolean isFreeRefundRescheduleFilterSelected() {
        driverTools.waitForElementToAppear(FREE_REFUND_RESCHEDULE_CHIP, 10);
        return driverTools.isElementDisplayed(FREE_REFUND_RESCHEDULE_CHIP);
    }

    /**
     * Verifies that at least one room card has the 100% refund label
     */
    public boolean isFullRefundLabelVisible() {
        driverTools.waitForElementToAppear(FULL_REFUND_LABEL, 10);
        return driverTools.isElementDisplayed(FULL_REFUND_LABEL);
    }

    /**
     * Gets the text of the first 100% refund label found
     */
    public String getFullRefundLabelText() {
        WebElement element = driverTools.waitForElementToAppear(FULL_REFUND_LABEL, 10).orElseThrow();
        return driverTools.getText(element);
    }

    /**
     * Verifies all visible recommendation cards have the 100% refund label
     */
    public boolean doAllVisibleCardsHaveFullRefundLabel() {
        driverTools.waitForElementToAppear(RECOMMENDATION_CARD, 10);
        List<WebElement> cards = driverTools.elements(RECOMMENDATION_CARD).orElse(List.of());

        if (cards.isEmpty()) {
            logger.log("No recommendation cards found");
            return false;
        }

        for (WebElement card : cards) {
            try {
                WebElement refundLabel = card.findElement(By.xpath(".//span[@data-testid='cancellation-policies-title' and contains(@class, 'Text_variant_positive') and contains(text(), '100%')]"));
                if (refundLabel == null || !refundLabel.isDisplayed()) {
                    logger.log("Card found without 100% refund label");
                    return false;
                }
            } catch (Exception e) {
                logger.log("Card found without 100% refund label: " + e.getMessage());
                return false;
            }
        }
        logger.log("All " + cards.size() + " visible cards have 100% refund label");
        return true;
    }

    /**
     * Gets the after tax price from the room card
     */

    private static final Locator SEE_DETAILS_ON_FIRST_ROOM_CARD = new Locator("SEE_DETAILS_ON_FIRST_ROOM_CARD", new By.ByXPath("(//div[contains(@class,'RatePlanCard_rate_plan_card_item_container')]//div[contains(@class,'RatePlanInformation_rate_plan_information')]//button[contains(@class,'Button_variant_tertiary')])[1]"));
    private static final Locator ROOM_LIST_PRICE_DROPDOWN_ICON = new Locator("ROOM_LIST_PRICE_DROPDOWN_ICON", new By.ByXPath("//*[name()='svg' and @data-testid='footer-button-view-price-detail']"));
    private static final Locator ROOM_LIST_PRICE_DROPDOWN_ICON_FOR_MULTI_BOOK = new Locator("ROOM_LIST_PRICE_DROPDOWN_ICON_FOR_MULTI_BOOK", new By.ByXPath("//*[name()='svg' and contains(@class,'MultiOrderPriceFooter_chevron_icon')]"));

    public String getAfterTaxPrice() {
        driverTools.waitForElementToAppear(SEE_DETAILS_ON_FIRST_ROOM_CARD, 10);
        driverTools.click(SEE_DETAILS_ON_FIRST_ROOM_CARD);

        driverTools.waitForElementToAppear(ROOM_LIST_PRICE_DROPDOWN_ICON, 10);
        driverTools.click(ROOM_LIST_PRICE_DROPDOWN_ICON);

        WebElement element = driverTools.waitForElementToAppear(AFTER_TAX_PRICE_UNDER_ROOMLIST, 10).orElseThrow();
        return driverTools.getText(element);
    }
    public String getAfterTaxPriceForMultiBook() {
        driverTools.waitForElementToAppear(ROOM_LIST_PRICE_DROPDOWN_ICON_FOR_MULTI_BOOK, 10);
        driverTools.click(ROOM_LIST_PRICE_DROPDOWN_ICON_FOR_MULTI_BOOK);

        WebElement element = driverTools.waitForElementToAppear(AFTER_TAX_PRICE_UNDER_ROOMLIST, 10).orElseThrow();
        return driverTools.getText(element);
    }

    public String getPriceAfterCashback(){
        driverTools.waitForElementsToAppear(PRICE_AFTER_CASHBACK);
        String price = driverTools.getText(PRICE_AFTER_CASHBACK).orElseThrow();
        logger.log("Price after cashback -> "+price);
        return price;
    }

    public void applyDownPaymentFilter(){
        driverTools.waitForElementToAppear(DOWN_PAYMENT_CHIP_CLOSE_ICON, 20);
        driverTools.click(DOWN_PAYMENT_CHIP_CLOSE_ICON);
    }

}
