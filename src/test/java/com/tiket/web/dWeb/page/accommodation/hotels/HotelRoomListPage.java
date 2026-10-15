package com.tiket.web.dWeb.page.accommodation.hotels;

import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

import static com.tiket.core.CoreFunctions.rgbToHex;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.core.Is.is;

public class HotelRoomListPage<T extends WebDriver> extends BasePage<T>{

    private static final Locator BOOK_ROOM = new Locator("BOOK_ROOM", new By.ByXPath("//div[@data-testid='rate-plan-card-container']//button[starts-with(@data-testid,'book-button')]"));
    private static final Locator BOOK_BUTTON_OF_NON_PAH_ROOM = new Locator("BOOK_BUTTON_OF_NON_PAH_ROOM", new By.ByXPath("//div[@data-testid='rate-plan-card-container'][not(descendant::*[contains(text(), 'Pay at Hotel')])]/descendant::button[starts-with(@data-testid, 'book-button-')]"));
    private static final Locator BOOK_BUTTON_OF_PAH_ROOM = new Locator("BOOK_BUTTON_OF_PAH_ROOM", new By.ByXPath("//div[@data-testid='rate-plan-card-container'][descendant::*[contains(text(), 'Pay at Hotel') or contains(text(), 'Bayar di Hotel')]]/descendant::button[starts-with(@data-testid, 'book-button-')]"));
    private static final Locator RECOMMENDED_ROOM = new Locator("RECOMMENDED_ROOM", new By.ByXPath("//*[@data-testid='room-recommendation-container']"));
    private static final Locator RECOMMENDED_ROOM_MWEB = new Locator("RECOMMENDED_ROOM", new By.ByXPath("//div[@data-testid='room-inner-card-container']"));
    private static final Locator RECOMMENDED_ROOM_GUEST = new Locator("RECOMMENDED_ROOM_GUEST", new By.ByXPath("(//*[@data-testid='room-recommendation-container']//h3)[1]"));
    private static final Locator ROOM_RECOMMENDATION_HOTEL_CARD = new Locator("ROOM_RECOMMENDATION_HOTEL_CARD",new By.ByXPath("//div[@data-testid='room-inner-card-container']"));
    private static final Locator BEST_PICK_TEXT = new Locator("BEST_PICK_TEXT", new By.ByXPath("(//*[@data-testid='room-recommendation-container']//h3)[2]"));
    private static final Locator FIRST_ROOM_CARD = new Locator("FIRST_ROOM_CARD", new By.ByXPath("(//div[@data-testid='rate-plan-card-container'])[1]"));
    private static final Locator FIRST_ROOM_IMG = new Locator("FIRST_ROOM_IMG", new By.ByXPath("//div[starts-with(@data-testid,'group-slider-item-')]//img[@data-testid='final-result-image']"));
    private static final Locator FIRST_BOOK_ROOM = new Locator("FIRST_BOOK_ROOM", new By.ByXPath("(//button[starts-with(@data-testid, 'book-button')])[1]"));
    private static final Locator SECOND_BOOK_ROOM = new Locator("SECOND_BOOK_ROOM", new By.ByXPath("(//button[starts-with(@data-testid, 'book-button')])[2]"));
    private static final Locator ROOM_GROUP_ITEM_SECTION = new Locator("ROOM_GROUP_ITEM_SECTION", new By.ByXPath("//*[@data-testid='room-group-item-section']"));
    private static final Locator CURRENT_ROOM_AMOUNT = new Locator("CURRENT_ROOM_AMOUNT", new By.ByXPath("(//input[contains(@class, 'QuantityEditor_quantity_input')])[1]"));
    private static final Locator ROOM_PLUS = new Locator("ROOM_PLUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[2]"));
    private static final Locator ROOM_MINUS = new Locator("ROOM_MINUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[1]"));
    private static final Locator CURRENT_ADULT_AMOUNT = new Locator("CURRENT_ADULT_AMOUNT", new By.ByXPath("(//input[contains(@class, 'QuantityEditor_quantity_input')])[2]"));
    private static final Locator ADULT_PLUS = new Locator("ADULT_PLUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[4]"));
    private static final Locator ADULT_MINUS = new Locator("ADULT_MINUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[3]"));
    private static final Locator CURRENT_CHILD_AMOUNT = new Locator("CURRENT_CHILD_AMOUNT", new By.ByXPath("(//input[contains(@class, 'QuantityEditor_quantity_input')])[3]"));
    private static final Locator CHILD_PLUS = new Locator("CHILD_PLUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[6]"));
    private static final Locator CHILD_MINUS = new Locator("CHILD_MINUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[5]"));
    private static final Locator ROOM_N_GUEST = new Locator("ROOM_N_GUEST", new By.ByXPath("//div[contains(@data-testid, 'room-list-search-button-open-room-guest-popup')]"));
    private static final Locator GUEST_DONE = new Locator("GUEST_DONE", new By.ByXPath("//footer[contains(@class, 'Modal_modal_footer')]//button"));
    private static final Locator CHANGE_BUTTON = new Locator("CHANGE_BUTTON", new By.ByXPath("//button[contains(@data-testid, 'room-list-search-button-submit')]"));
    private static final Locator POPUP_NOT_MEET_POLICY = new Locator("POPUP_NOT_MEET_POLICY", new By.ByXPath("//*[contains(@class,'Dialog_pop_up')]//*[text()='Pesanan ini tidak sesuai dengan kebijakan perjalanan perusahaan' or text()='This order doesn’t meet the travel policy']"));
    private static final Locator MEETS_TRAVEL_POLICY_NUDGE = new Locator("MEETS_TRAVEL_POLICY_NUDGE", AppiumBy.xpath("//*[contains(@class,'FeatureLabel') and text()='Meets Travel Policy' or text()='Sesuai Kebijakan']"));
    private static final Locator ROOM_SELECT_PLUS= new Locator("ROOM_SELECT_PLUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[2]"));
    private static final Locator ROOM_SELECT_MINUS = new Locator("ROOM_SELECT_MINUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[1]"));
    private static final Locator MULTI_ROOM_BOOK = new Locator("MULTI_ROOM_BOOK", new By.ByXPath("//button[contains(@data-testid, 'multi-order-price-footer-button-book')]"));
    private static final Locator ROOM_DETAIL_CONTAINER = new Locator("ROOM_DETAIL_CONTAINER", new By.ByXPath("//div[@data-testid='room-detail-container']"));
    private static final Locator IMAGE_ROOM_DETAIL = new Locator("IMAGE_ROOM_DETAIL", new By.ByXPath("(//div[@data-testid='room-detail-container']//img[@data-testid='final-result-image'])[1]"));
    private static final Locator PRICE_ROOM_DETAIL = new Locator("PRICE_ROOM_DETAIL", new By.ByXPath("//div[@data-testid='room-detail-container']//div[contains(@class, 'PriceModule_final')]"));
    private static final Locator BOOK_ROOM_DETAIL = new Locator("BOOK_ROOM_DETAIL", new By.ByXPath("//div[@data-testid='room-detail-container']//button[@data-testid='footer-button-book']"));
    private static final Locator ROOM_PRICE = new Locator("ROOM_PRICE", new By.ByXPath("//*[contains(@class,'RatePlanPrice_final_price_wrapper')]/*[contains(@class,'Text_variant_price')]"));
    private static final Locator DATES = new Locator("ROOM_PRICE", new By.ByXPath("(//span[contains(@class, 'desktop_text_input')])[1]"));
    private static final Locator NIGHTS_ADULTS = new Locator("ROOM_PRICE", new By.ByXPath("(//span[contains(@class, 'desktop_text_input')])[2]"));
    private static final Locator VENDOR_COLOR = new Locator("VENDOR_COLOR", new By.ByXPath("//div[contains(@class, 'vendor_indicator_circle')]"));
    private static final Locator TOTAL_ROOM_NIGHT_RECOMMENDED = new Locator("TOTAL_ROOM_NIGHT_RECOMMENDED", new By.ByXPath("//div[@data-testid='total-room-night-summary']"));
    private static final Locator TOTAL_PRICE_RECOMMENDED= new Locator("BOOK_ROOM_DETAIL", new By.ByXPath("//div[contains(@class, 'total_price')]"));
    private static final Locator TOTAL_ROOM_RECOMMENDED_ICON= new Locator("TOTAL_ROOM_RECOMMENDED_ICON", new By.ByXPath("//div[@data-testid='alternate-room-recommendation-amount']"));
    private static final Locator TOTAL_ROOM_RECOMMENDED = new Locator("TOTAL_ROOM_RECOMMENDED", new By.ByXPath("(//div[contains( @class, 'RoomInnerCard_icon_text')]//span)[1]"));
    private static final Locator BOOK_BUTTON_ROOM_RECOMMENDED_ROOM_LIST = new Locator("BOOK_BUTTON_ROOM_RECOMMENDED_ROOM_LIST", new By.ByXPath("(//button//*[(contains(text(), 'Pesan') or contains(text(), 'Book') ) ])[1]"));
    private static final Locator BOOK_BUTTON_ROOM_RECOMMENDED_ROOM_DETAIL = new Locator("BOOK_BUTTON_ROOM_RECOMMENDED_ROOM_DETAIL", new By.ByXPath("(//button//*[(contains(text(), 'Pesan') or contains(text(), 'Book') ) ])[1]"));
    private static final Locator FIRST_ROOM_NAME= new Locator("BOOK_ROOM_DETAIL", new By.ByXPath("//div[@data-testid='room-group-desktop-title-wrapper']//span"));
    private static final Locator SIMILAR_PROPERTIES_SECTION= new Locator("SIMILAR_PROPERTIES_SECTION", new By.ByXPath("//section[contains(@class,'Layout_icon')]"));
    private static final Locator SIMILAR_PROPERTIES_HEADING_TEXT= new Locator("SIMILAR_PROPERTIES_HEADING_TEXT", new By.ByXPath("//div[contains(@class,'Layout_title')]"));
    private static final Locator SIMILAR_PROPERTIES_CARD= new Locator("SIMILAR_PROPERTIES_CARD", new By.ByXPath("(//section[contains(@class,'Layout_icon')]//a)[1]"));
    private static final Locator MOBILE_NUMBER_INPUT = new Locator("MOBILE_NUMBER_INPUT", By.id("mobile-number"));
    private static final Locator SKIP_CONTINUE = new Locator("SKIP_CONTINUE", new By.ByXPath("//button[contains(@class,'PriceFooter_continue_button')]"));
    /** Same as booking form: rate plan / combination change popups after multi-book → booking step. */
    private static final Locator RATE_PLAN_CHANGE_DIALOG_PRIMARY_BTN = new Locator("RATE_PLAN_CHANGE_DIALOG_PRIMARY_BTN", new By.ByXPath(
            "//div[contains(@class,'Dialog_dialog')]//button[contains(@class,'Button_variant_primary') and contains(@class,'Button_size_large')]"));
    private static final Locator RATE_PLAN_CHANGE_DIALOG = new Locator("RATE_PLAN_CHANGE_DIALOG", new By.ByXPath("//div[contains(@class,'Dialog_dialog')]"));
    private static final Locator COMBINATION_CHANGES_FOOTER_PRIMARY_BTN = new Locator("COMBINATION_CHANGES_FOOTER_PRIMARY_BTN", new By.ByXPath(
            "//footer[contains(@class,'Modal_modal_footer')]//div[contains(@class,'CombinationChangesPopup_combination_changes_footer_modal')]//button[contains(@class,'Button_variant_primary')]"));

    public HotelRoomListPage(Session<T> session) {
        super(session);
    }

    @SneakyThrows
    public void clickBookButton() throws InterruptedException {
        driverTools.waitForElementToPresence(BOOK_ROOM, 15).orElseThrow();
        driverTools.scrollIntoWebView(BOOK_ROOM);
        driverTools.clickWithJavascriptExecutor(BOOK_ROOM);
    }

    @SneakyThrows
    public void clickBookButtonOfNonPAHRoom() throws InterruptedException {
        driverTools.waitForElementToPresence(BOOK_BUTTON_OF_NON_PAH_ROOM, 15).orElseThrow();
        driverTools.scroll(2,20);
        driverTools.scrollIntoWebView(BOOK_BUTTON_OF_NON_PAH_ROOM);
        driverTools.clickWithJavascriptExecutor(BOOK_BUTTON_OF_NON_PAH_ROOM);
    }

    /**
     * Clicks the Book button of the first rate-plan card that advertises <b>Pay at Hotel</b>
     * (both English and Bahasa Indonesia labels). Mirrors {@link #clickBookButtonOfNonPAHRoom()}
     * for the PAH-specific flow.
     */
    @SneakyThrows
    public void clickBookButtonOfPAHRoom() throws InterruptedException {
        driverTools.waitForElementToPresence(BOOK_BUTTON_OF_PAH_ROOM, 15).orElseThrow();
        driverTools.scroll(2, 20);
        driverTools.scrollIntoWebView(BOOK_BUTTON_OF_PAH_ROOM);
        driverTools.clickWithJavascriptExecutor(BOOK_BUTTON_OF_PAH_ROOM);
    }

    public void clickContinue() {
        dismissRatePlanChangePopupsIfPresent();
        driverTools.waitForElementToAppear(SKIP_CONTINUE, 5);
        if(driverTools.isElementDisplayed(SKIP_CONTINUE)){
            driverTools.clickWithJavascriptExecutor(SKIP_CONTINUE);
        }
    }

    public void enterMobile(String mobileNumber) {
        driverTools.waitForElementToAppear(MOBILE_NUMBER_INPUT, 5);
        driverTools.sendKeys(MOBILE_NUMBER_INPUT, mobileNumber);
    }

    public boolean isRecommendedRoomVisible(){
        driverTools.waitForElementToAppear(RECOMMENDED_ROOM,5);
        return driverTools.isElementDisplayed(RECOMMENDED_ROOM);
    }

    public boolean isRecommendedRoomMWEBVisible(){
        driverTools.waitForElementToAppear(RECOMMENDED_ROOM_MWEB,5);
        return driverTools.isElementDisplayed(RECOMMENDED_ROOM_MWEB);
    }

    public void selectRoomRecommendationCard() {
        driverTools.clickWithJavascriptExecutor(ROOM_RECOMMENDATION_HOTEL_CARD);
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

        Assertion.assertThat("Best pick for you HEADING should be visible", driverTools.isElementDisplayed(BEST_PICK_TEXT), is(true));
    }

    public boolean isRoomListSectionVisible(){
        driverTools.waitForElementToAppear(FIRST_ROOM_CARD,5);
        return driverTools.isElementDisplayed(FIRST_ROOM_CARD);
    }

    public boolean isRoomImageVisible(){
        driverTools.waitForElementToAppear(FIRST_ROOM_IMG,5);
        return driverTools.isElementDisplayed(FIRST_ROOM_IMG);
    }
    public void roomListDetails(int index) {
        driverTools.scrollUntilElementFound(ROOM_GROUP_ITEM_SECTION);
        int roomGroupSectionCount = driverTools.elements(ROOM_GROUP_ITEM_SECTION).orElse(List.of()).size();
        logger.log("Room group section count: " + roomGroupSectionCount);
        int effectiveIndex = roomGroupSectionCount < index ? roomGroupSectionCount : index;
        logger.log("Effective index: " + effectiveIndex);

        for (int i = 1; i <= effectiveIndex; i++) {
            Assertion.assertThat("Room no " + i + " is visible on room details section", isRoomVisible(i, ""), is(true));
            Assertion.assertThat("Room no " + i + "'s feature label is visible", isRoomVisible(i, "//div[@data-testid='room-card-feature-label-item']"), is(true));
            Assertion.assertThat("Room no " + i + "'s cancellation policy is visible", isRoomVisible(i, "//span[@data-testid='cancellation-policies-title']"), is(true));
            Assertion.assertThat("Room no " + i + "'s occupancy info is visible", isRoomVisible(i, "//div[contains(@class,'RatePlanInformation_occupancy_information__')]"), is(true));
            Assertion.assertThat("Room no " + i + "'s price is visible on room details section", isRoomVisible(i, "//div[contains(@class,'Text_variant_price__')]"), is(true));
            Assertion.assertThat("Room no " + i + "'s book button is visible", isRoomVisible(i, "//button[contains(@data-testid,'book-button')]"), is(true));
        }
    }


    public boolean isRoomVisible(int index, String subElement) {
        Locator roomElement = getLocatorForRoom(index, subElement);
        driverTools.waitForElementToAppear(roomElement, 5);
        return driverTools.isElementDisplayed(roomElement);
    }

    private Locator getLocatorForRoom(int index, String subElement) {
        String baseXPath = "(//div[@data-testid='rate-plan-card-container'])[" + index + "]";

        String finalXPath;
        if (subElement == null || subElement.isEmpty()) {
            finalXPath = baseXPath;
        } else {
            finalXPath = baseXPath + subElement;
        }

        return new Locator("RoomDetailsCard", new By.ByXPath(finalXPath));
    }


    public void scrollToRoomListSection(){
        driverTools.waitForElementToAppear(FIRST_ROOM_CARD,10);
        if(Configs.PLATFORM == Platform.AMWEB){
            driverTools.scrollToElement(FIRST_ROOM_CARD);
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

    public void setTotalRoom(int room) {
        int currentRoom = Integer.parseInt(driverTools.getValueAttribute(CURRENT_ROOM_AMOUNT, "value"));
        setTotal(currentRoom, room, ROOM_PLUS, ROOM_MINUS, "Room");
    }

    public void setTotalGuest(int adult) {
        int currentAdult = Integer.parseInt(driverTools.getValueAttribute(CURRENT_ADULT_AMOUNT, "value"));
        setTotal(currentAdult, adult, ADULT_PLUS, ADULT_MINUS, "Adult");
    }

    public void setTotalChild(int child) {
        int currentChild = Integer.parseInt(driverTools.getValueAttribute(CURRENT_CHILD_AMOUNT, "value"));
        setTotal(currentChild, child, CHILD_PLUS, CHILD_MINUS, "Child");
    }

    public void clickRoomNGuestButton() {
        driverTools.waitForElementToAppear(ROOM_N_GUEST).orElseThrow();
        driverTools.clickWithJavascriptExecutor(ROOM_N_GUEST);
    }

    public void clickGuestDoneButton() {
        driverTools.waitForElementToAppear(GUEST_DONE).orElseThrow();
        driverTools.clickWithJavascriptExecutor(GUEST_DONE);
    }

    public void clickRoomListChangeButton() {
        driverTools.waitForElementToAppear(CHANGE_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CHANGE_BUTTON);
    }

   public void selectMultipleRoom(int noOfRoom){
        for (int i = 0; i < noOfRoom; i++) {
            // Click from last to first to avoid UI shift issues
            Locator roomSelector = getRoomSelector(noOfRoom - i);
            driverTools.waitForElementToAppear(roomSelector).orElseThrow();
            driverTools.scrollIntoWebView(roomSelector);
            driverTools.clickWithJavascriptExecutor(roomSelector);
        }
    }

    private Locator getRoomSelector(int index) {
        String xpath = "(//button[contains(@data-testid,'book-button') and (contains(.,'Select') or contains(.,'Pilih'))])[" + index + "]";
        return new Locator("IndividualRoomSelector", new By.ByXPath(xpath));
    }
    public boolean doesNotMeetTravelPolicyPopUpAppears(){
        return  driverTools.waitForElementToPresence(POPUP_NOT_MEET_POLICY,5).isPresent();
    }

    public boolean isMeetsTravelPolicyNudgeVisible(){
        return driverTools.waitForElementToPresence(MEETS_TRAVEL_POLICY_NUDGE).isPresent();
    }

    public void selectMultipleRoomWithSameRatePlan(int noOfRoom) {
        int currentRoom = Integer.parseInt(driverTools.getValueAttribute(CURRENT_ROOM_AMOUNT, "value"));
        setTotal(currentRoom, noOfRoom, ROOM_SELECT_PLUS, ROOM_SELECT_MINUS, "Individual room");
    }

    public void clickMultiBookButton() {
        if(!driverTools.isElementDisplayed(MULTI_ROOM_BOOK)){
            for (int attempt = 0; attempt < 8; attempt++) {
                driverTools.scrollUp(200);
                if (driverTools.waitForElementToPresence(MULTI_ROOM_BOOK, 2).isPresent()) {
                    break;
                }
            }
        }
        driverTools.scrollToBottomOfPage();
        driverTools.click(MULTI_ROOM_BOOK);
        dismissRatePlanChangePopupsIfPresent();
    }

    /** Align with HotelBookingFormPageWeb: dialog primary → wait disappear → combination footer primary. */
    public void dismissRatePlanChangePopupsIfPresent() {
        if (driverTools.waitForElementToAppear(RATE_PLAN_CHANGE_DIALOG_PRIMARY_BTN, 5).isPresent()) {
            driverTools.clickWithJavascriptExecutor(RATE_PLAN_CHANGE_DIALOG_PRIMARY_BTN);
            driverTools.waitForElementToDisappear(RATE_PLAN_CHANGE_DIALOG, 15);
        }
        driverTools.waitForElementToAppear(COMBINATION_CHANGES_FOOTER_PRIMARY_BTN, 5)
                .ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public void firstBookRoom(){
        driverTools.waitForElementToAppear(FIRST_BOOK_ROOM).orElseThrow();
        driverTools.clickWithJavascriptExecutor(FIRST_BOOK_ROOM);
    }

    public void roomDetailsVisible() {
        Assertion.assertThat("Room Name and room description is visible on room detail Page" , isRoomDetail(2), is(true));
        Assertion.assertThat("Refund & Reschedule details are visible on room detail Page", isRoomDetail(3), is(true));
        Assertion.assertThat("Check-in procedures are visible on room detail Page", isRoomDetail(4), is(true));
        Assertion.assertThat("Room price is visible on room detail Page", isRoomDetail(5), is(true));
    }

    public boolean isRoomDetail(int index) {
        Locator roomElement = getContentRoomDescriptionLocator(index);
        driverTools.waitForElementToAppear(roomElement, 5);
        return driverTools.isElementDisplayed(roomElement);
    }

    public Locator getContentRoomDescriptionLocator(int index) {
        String xpath = "(//div[contains(@class,'RoomDetailMainContent_') and (contains(@class,'room_info_container') or contains(@class,'section_item_wrapper') or contains(@class,'footer_wrapper'))])[" + index + "]";
        return new Locator("ContentRoomDescriptionTab", new By.ByXPath(xpath));
    }

    public boolean isRoomDetailPage(){
        driverTools.waitForElementToAppear(ROOM_DETAIL_CONTAINER,5);
        return driverTools.isElementDisplayed(ROOM_DETAIL_CONTAINER);
    }
    public boolean isImageOnRoomDetail(){
        driverTools.waitForElementToAppear(IMAGE_ROOM_DETAIL,5);
        return driverTools.isElementDisplayed(IMAGE_ROOM_DETAIL);
    }
    public boolean isPriceOnRoomDetail(){
        driverTools.waitForElementToAppear(PRICE_ROOM_DETAIL,5);
        return driverTools.isElementDisplayed(PRICE_ROOM_DETAIL);
    }
    public void clickBookRoomDetail(){
        driverTools.waitForElementToAppear(BOOK_ROOM_DETAIL,5);
        driverTools.clickWithJavascriptExecutor(BOOK_ROOM_DETAIL);
    }
    public void openRoomDetail(){
        driverTools.waitForElementToAppear(FIRST_ROOM_CARD,5);
        driverTools.clickWithJavascriptExecutor(FIRST_ROOM_CARD);
    }
    public String getRoomPrice(){
        WebElement webElement = driverTools.waitForElementToAppear(ROOM_PRICE).orElseThrow();
        String roomPriceText = driverTools.getText(webElement);
        logger.log(roomPriceText);
        return roomPriceText;
    }

    public boolean isTotalRoomNightRecommended(){
        driverTools.waitForElementToAppear(TOTAL_ROOM_NIGHT_RECOMMENDED,5);
        return driverTools.isElementDisplayed(TOTAL_ROOM_NIGHT_RECOMMENDED);
    }

    public boolean isRecommendedTotalPrice(){
        driverTools.waitForElementToAppear(TOTAL_PRICE_RECOMMENDED,5);
        return driverTools.isElementDisplayed(TOTAL_PRICE_RECOMMENDED);
    }

    public boolean isRecommendedTotalRoomIcon(){
        driverTools.waitForElementToAppear(TOTAL_ROOM_RECOMMENDED_ICON,5);
        return driverTools.isElementDisplayed(TOTAL_ROOM_RECOMMENDED_ICON);
    }

    public String getRecommendedTotalRoom(){
        return driverTools.getText(TOTAL_ROOM_RECOMMENDED).orElseThrow();
    }

    public String getRecommendedTotalRoomBookButtonRoomList(){
        return driverTools.getText(BOOK_BUTTON_ROOM_RECOMMENDED_ROOM_LIST).orElseThrow();
    }

    public String getRecommendedTotalRoomBookButtonRoomDetail(){
        return driverTools.getText(BOOK_BUTTON_ROOM_RECOMMENDED_ROOM_DETAIL).orElseThrow();
    }

    public String getVendorNameFromColor() {
        WebElement webElement = driverTools.waitForElementToAppear(VENDOR_COLOR).orElseThrow();
        String styleValue = webElement.getAttribute("style");
        String rgbValue = styleValue.split(":")[1].trim().replace(";", "");
        String hexColor = rgbToHex(rgbValue);

        switch (hexColor) {
            case "#FF0000": return "AGODA";
            case "#FEDD00": return "EXPEDIA";
            case "#FCA12F": return "HOTELBEDS";
            case "#0064D2": return "TIKET";
            case "#30F639": return "EXPEDIA_RAPID";
            case "#30B6C5": return "WEBBEDS";
            default: return "UNKNOWN";
        }
    }

    public void secondBookRoom(){
        driverTools.scrollIntoWebView(SECOND_BOOK_ROOM);
        driverTools.waitForElementToAppear(SECOND_BOOK_ROOM).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SECOND_BOOK_ROOM);
    }

    public String getRoomName(){
        WebElement webElement = driverTools.waitForElementToAppear(FIRST_ROOM_NAME).orElseThrow();
        return driverTools.getText(webElement);
    }

    public boolean isSimilarPropertiesVisible(){
        driverTools.waitForElementToAppear(SIMILAR_PROPERTIES_SECTION,5);
        driverTools.scrollIntoWebView(SIMILAR_PROPERTIES_SECTION);
        return driverTools.isElementDisplayed(SIMILAR_PROPERTIES_SECTION);
    }

    public String getSimilarPropertiesHeader(){
        driverTools.waitForElementToAppear(SIMILAR_PROPERTIES_HEADING_TEXT,5);
        return driverTools.getText(SIMILAR_PROPERTIES_HEADING_TEXT).orElseThrow();
    }

    public boolean isSimilarHotelcard(){
        driverTools.waitForElementToAppear(SIMILAR_PROPERTIES_CARD,5);
        return driverTools.isElementDisplayed(SIMILAR_PROPERTIES_CARD);
    }

    public void clickSimilarHotel(){
        driverTools.clickWithJavascriptExecutor(SIMILAR_PROPERTIES_CARD);
    }

}