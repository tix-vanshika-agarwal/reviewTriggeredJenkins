package com.tiket.web.page.accommodation.hotels.hotelRoomListPage;

import com.tiket.appium.Swipe;
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

    private static final Locator BOOK_ROOM = new Locator("BOOK_ROOM", new By.ByXPath("//*[contains(@data-testid, 'book-button')]"));
    private static final Locator RECOMMENDED_ROOM = new Locator("RECOMMENDED_ROOM", new By.ByXPath("//div[@data-testid='room-inner-card-container']"));
    private static final Locator RECOMMENDED_ROOM_GUEST = new Locator("RECOMMENDED_ROOM_GUEST", new By.ByXPath("(//*[@data-testid='room-recommendation-container']//h3)[1]"));
    private static final Locator BEST_PICK_TEXT = new Locator("BEST_PICK_TEXT", new By.ByXPath("(//*[@data-testid='room-recommendation-container']//h3)[2]"));
    private static final Locator FIRST_ROOM_CARD = new Locator("FIRST_ROOM_CARD", new By.ByXPath("(//div[@data-testid='rate-plan-card-container'])[1]"));
    private static final Locator FIRST_ROOM_IMG = new Locator("FIRST_ROOM_IMG", new By.ByXPath("(//div[contains(@data-testid, 'group-slider-item')]//img[@data-testid='final-result-image'])[1]"));
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
    private static final Locator ROOM_NAME_DETAIL = new Locator("ROOM_NAME_DETAIL", new By.ByXPath("(//div[contains(@class, 'RoomOverview_room_overview_container')]//*//*[contains(@class,'Text_variant')])[1]"));
    private static final Locator REFUND_AND_RESCHEDULE_DETAILS = new Locator("REFUND_AND_RESCHEDULE_DETAILS", new By.ByXPath("(//div[contains(@class, 'RoomRefundOverview_main_content')]//*[contains(@class,'Text_variant')])[1]"));
    private static final Locator OTHER_FACILITIES_DETAILS = new Locator("OTHER_FACILITIES_DETAILS", new By.ByXPath("//*[@data-testid='other-facility-content-item']"));
    private static final Locator IMAGE_ROOM_DETAIL = new Locator("IMAGE_ROOM_DETAIL", new By.ByXPath("//div[@data-testid='room-detail-container']//img[@data-testid='final-result-image']"));
    private static final Locator PRICE_ROOM_DETAIL = new Locator("PRICE_ROOM_DETAIL", new By.ByXPath("//div[@data-testid='room-detail-container']//div[contains(@class, 'PriceModule_final')]"));
    private static final Locator BOOK_ROOM_DETAIL = new Locator("BOOK_ROOM_DETAIL", new By.ByXPath("//div[@data-testid='room-detail-container']//button[@data-testid='footer-button-book']"));
    private static final Locator ROOM_PRICE = new Locator("ROOM_PRICE", new By.ByXPath("(//span[contains(@class, 'price_tax')]//span)[1]"));
    private static final Locator DATES = new Locator("ROOM_PRICE", new By.ByXPath("(//span[contains(@class, 'desktop_text_input')])[1]"));
    private static final Locator NIGHTS_ADULTS = new Locator("ROOM_PRICE", new By.ByXPath("(//span[contains(@class, 'desktop_text_input')])[2]"));
    private static final Locator VENDOR_COLOR = new Locator("VENDOR_COLOR", new By.ByXPath("//div[contains(@class, 'vendor_indicator_circle')]"));
    private static final Locator TOTAL_ROOM_NIGHT_RECOMMENDED = new Locator("TOTAL_ROOM_NIGHT_RECOMMENDED", new By.ByXPath("//div[@data-testid='total-room-night-summary']"));
    private static final Locator TOTAL_PRICE_RECOMMENDED= new Locator("BOOK_ROOM_DETAIL", new By.ByXPath("//div[contains(@class, 'total_price')]"));
    private static final Locator FIRST_ROOM_NAME= new Locator("BOOK_ROOM_DETAIL", new By.ByXPath("(//div[@data-testid='room-card-container'])[1]//h3"));
    private static final Locator SIMILAR_PROPERTIES_SECTION= new Locator("SIMILAR_PROPERTIES_SECTION", new By.ByXPath("//section[contains(@class,'Layout_icon')]"));
    private static final Locator SIMILAR_PROPERTIES_HEADING_TEXT= new Locator("SIMILAR_PROPERTIES_HEADING_TEXT", new By.ByXPath("//section[contains(@class,'Layout_icon')]//h2"));
    private static final Locator SIMILAR_PROPERTIES_CARD= new Locator("SIMILAR_PROPERTIES_CARD", new By.ByXPath("//section[contains(@class,'Layout_icon')]//a"));
    private static final Locator GUEST_NUMBER= new Locator("GUEST_NUMBER", new By.ByXPath("//div[contains(@class, 'mobile_date_container')]//span[contains(@class, 'Text_variant_lowEmphasis')][3]"));
    private static final Locator NIGHT_NUMBER=new Locator("NIGHT_NUMBER",new By.ByXPath("//div[@data-testid='total-room-night-summary']"));
    private static final Locator ROOM_NAME=new Locator("ROOM_NAME",new By.ByXPath("(//h3[contains(@class, 'RoomCard_title_text') and contains(@class, 'Text_weight_bold')])[1]"));
    private static final Locator ROOM_CARD_CONTAINER_ANCESTOR = new Locator("ROOM_CARD_CONTAINER_ANCESTOR", new By.ByXPath(".//ancestor::div[@data-testid='rate-plan-card-container']"));
    private static final Locator BOOK_BUTTON_IN_CONTAINER = new Locator("BOOK_BUTTON_IN_CONTAINER", new By.ByXPath(".//*[contains(@data-testid, 'book-button')]"));
    private static final Locator ROOM_PRICE_BESIDE_ROOM_NAME = new Locator("ROOM_PRICE_BESIDE_ROOM_NAME", new By.ByXPath("//*[@data-testid='alternate-room-recommendation-amount']/span[1]"));
    private static final Locator TOTAL_RECOMMENDED_ROOMS = new Locator("TOTAL_RECOMMENDED_ROOMS", new By.ByXPath("//button[contains(@class,'Button_button__')]//span[contains(text(),'Room') or contains(text(),'Kamar')]"));
    private static final Locator ROOM_WITH_FEE_REFUND = new Locator("ROOM_WITH_FEE_REFUND", new By.ByXPath("//span[@data-testid='cancellation-policies-title' and (contains(normalize-space(.), 'with fee') or contains(normalize-space(.), 'biaya'))]"));
    private static final Locator ROOM_REFUND_CHARGE = new Locator("ROOM_REFUND_CHARGE", new By.ByXPath("(//ul[contains(@class,'RoomRefundOverview_cancellation_policies')]//span//b)[1]"));

    public HotelRoomListPage(Session<T> session) {
        super(session);
    }

    @SneakyThrows
    public void clickBookButton(){
        driverTools.waitForElementToPresence(BOOK_ROOM, 15).orElseThrow();
        driverTools.scroll(2,20);
        driverTools.scrollIntoWebView(BOOK_ROOM);
        driverTools.clickWithJavascriptExecutor(BOOK_ROOM);
    }

    public boolean isRecommendedRoomVisible(){
        driverTools.waitForElementToAppear(RECOMMENDED_ROOM,5);
        return driverTools.isElementDisplayed(RECOMMENDED_ROOM);
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
            Assertion.assertThat("Room no " + i + " is visible on room List", isRoomVisible(i, ""), is(true));
            Assertion.assertThat("Room no " + i + "'s name is visible on room List", isRoomVisible(i, "//*[@data-testid='room-group-desktop-title-wrapper']//*[contains(@class,'Text_variant')]"), is(true));
            Assertion.assertThat("Room no " + i + "'s guest number is visible", isRoomVisible(i, "//*[contains(@class,'RatePlanInformation_occupancy_item')]//*[contains(@class,'Text_variant')]"), is(true));
            Assertion.assertThat("Room no " + i + "'s bed type is visible", isRoomVisible(i, "//*[contains(@class,'BedTypesContent_bed_types_content_container')]//*[contains(@class,'Text_variant')]"), is(true));
            Assertion.assertThat("Room no " + i + "'s Price is visible on room list card", isRoomVisible(i, "//*[contains(@class,'RatePlanPrice_final_price_wrapper')]//*[contains(@class,'Text_variant')]"), is(true));
        }
    }

    public boolean isRoomVisible(int index, String subElement) {
        Locator roomElement = getLocatorForRoom(index, subElement);
        driverTools.waitForElementToAppear(roomElement, 5);
        return driverTools.isElementDisplayed(roomElement);
    }

    private Locator getLocatorForRoom(int index, String subElement) {
        String xpath = "(//*[@data-testid='room-group-item-section'])[" + index + "]";
        if (!subElement.isEmpty()) {
            xpath += subElement;
        }
        return new Locator("IndividualRoom", new By.ByXPath(xpath));
    }

    public void scrollToRoomListSection(){
        driverTools.waitForElementToAppear(FIRST_ROOM_CARD,5);
        if (Configs.PLATFORM == Platform.AMWEB) {
            driverTools.scrollToElement(FIRST_ROOM_CARD);
        } else {
            driverTools.scrollIntoWebView(FIRST_ROOM_CARD);
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
        Locator firstRoomSelector = getRoomSelector(1);
        driverTools.waitForElementToAppear(firstRoomSelector).orElseThrow();
        driverTools.clickWithJavascriptExecutor(firstRoomSelector);
        
        driverTools.waitForElementToAppear(CURRENT_ROOM_AMOUNT, 5);
        int currentRoom = Integer.parseInt(driverTools.getValueAttribute(CURRENT_ROOM_AMOUNT, "value"));
        logger.log("Current room count: " + currentRoom);
        
        int roomsSelected = currentRoom;
        if (noOfRoom > currentRoom) {
            // Try to select same room type using quantity selector
            // Loop until we reach the target or button becomes unavailable
            // Using roomsSelected as the primary limiter since it reflects actual UI state
            int maxAttempts = noOfRoom - currentRoom; // Safety limit to prevent infinite loops
            for (int attempt = 0; attempt < maxAttempts && roomsSelected < noOfRoom; attempt++) {
                driverTools.waitForElementToAppear(ROOM_SELECT_PLUS, 3);
                WebElement plusButton = driverTools.waitForElementToAppear(ROOM_SELECT_PLUS, 2).orElse(null);
                if (plusButton != null && plusButton.isEnabled()) {
                    String disabledAttr = plusButton.getAttribute("disabled");
                    if (disabledAttr == null || !disabledAttr.equals("true")) {
                        driverTools.clickWithJavascriptExecutor(ROOM_SELECT_PLUS);
                        driverTools.waitForElementToAppear(CURRENT_ROOM_AMOUNT, 2);
                        roomsSelected = Integer.parseInt(driverTools.getValueAttribute(CURRENT_ROOM_AMOUNT, "value"));
                        logger.log("Room count after increment: " + roomsSelected);
                        // Exit early if we've reached the target
                        if (roomsSelected >= noOfRoom) {
                            break;
                        }
                    } else {
                        logger.log("Plus button disabled, selecting from different room type");
                        break;
                    }
                } else {
                    logger.log("Plus button not enabled, selecting from different room type");
                    break;
                }
            }
        }
        
        // If same room type doesn't have enough availability, select from different room types
        if (roomsSelected < noOfRoom) {
            int remainingRooms = noOfRoom - roomsSelected;
            logger.log("Selecting " + remainingRooms + " more rooms from different room types");
            for (int i = 0; i < remainingRooms; i++) {
                int selectorIndex = 2 + i;
                Locator roomSelector = getRoomSelector(selectorIndex);
                if (driverTools.waitForElementToAppear(roomSelector, 3).isPresent()) {
                    driverTools.clickWithJavascriptExecutor(roomSelector);
                    driverTools.waitForElementToAppear(MULTI_ROOM_BOOK, 3);
                    roomsSelected++;
                } else {
                    logger.log("Room selector at index " + selectorIndex + " not found");
                    break;
                }
            }
        }
        
        logger.log("Total rooms selected: " + roomsSelected + " out of " + noOfRoom);
        driverTools.waitForElementToAppear(MULTI_ROOM_BOOK, 5);
    }

    public int getSelectedRoomCountFromQuantitySelector() {
        driverTools.waitForElementToAppear(CURRENT_ROOM_AMOUNT, 10);
        String roomCountValue = driverTools.getValueAttribute(CURRENT_ROOM_AMOUNT, "value");
        int roomCount = Integer.parseInt(roomCountValue);
        logger.log("Selected room count: " + roomCount);
        return roomCount;
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

    public void swipeToMultiRoomBookButton(){
        if(!driverTools.isElementDisplayed(MULTI_ROOM_BOOK)){
            for (int attempt = 0; attempt < 8; attempt++) {
                driverTools.scrollUp(200);
                if (driverTools.waitForElementToPresence(MULTI_ROOM_BOOK, 2).isPresent()) {
                    break;
                }
            }
        }
    }

    public void clickMultiBookButton() {
        swipeToMultiRoomBookButton();
        driverTools.click(MULTI_ROOM_BOOK);
    }

    public void firstBookRoom(){
        driverTools.waitForElementToAppear(FIRST_BOOK_ROOM).orElseThrow();
        driverTools.clickWithJavascriptExecutor(FIRST_BOOK_ROOM);
    }

    public void roomDetailsVisible() {
        Assertion.assertThat("Room Name is visible on room detail Page" , driverTools.isElementDisplayed(ROOM_NAME_DETAIL), is(true));
        Assertion.assertThat("Refund & Reschedule details are visible on room detail Page", driverTools.isElementDisplayed(REFUND_AND_RESCHEDULE_DETAILS), is(true));
        Assertion.assertThat("Other facilities are visible on room detail Page", driverTools.isElementDisplayed(OTHER_FACILITIES_DETAILS), is(true));

    }

    public boolean isRoomDetail(int index) {
        Locator roomElement = getContentRoomDescriptionLocator(index);
        driverTools.waitForElementToAppear(roomElement, 5);
        return driverTools.isElementDisplayed(roomElement);
    }

    public Locator getContentRoomDescriptionLocator(int index) {
        String xpath = "(//div[contains(@class, 'Content_room_description_tab')])[" + index + "]";
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
        return driverTools.getText(webElement);
    }

    public boolean isTotalRoomNightRecommended(){
        driverTools.waitForElementToAppear(TOTAL_ROOM_NIGHT_RECOMMENDED,5);
        return driverTools.isElementDisplayed(TOTAL_ROOM_NIGHT_RECOMMENDED);
    }

    public boolean isRecommendedTotalPrice(){
        driverTools.waitForElementToAppear(TOTAL_PRICE_RECOMMENDED,5);
        return driverTools.isElementDisplayed(TOTAL_PRICE_RECOMMENDED);
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

    public String getVendorNameFromColor(WebElement vendorElement) {
        String styleValue = vendorElement.getAttribute("style");
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

    @SneakyThrows
    public void clickBookButtonForVendor(WebElement vendorElement) {
        logger.log("Finding book button for the vendor element...");
        
        // Find the parent container (rate-plan-card-container)
        WebElement container = vendorElement.findElement(ROOM_CARD_CONTAINER_ANCESTOR.by());
        
        // Find the book button within the same container
        WebElement bookButton = container.findElement(BOOK_BUTTON_IN_CONTAINER.by());
        
        // Scroll to the book button and click it
        driverTools.scrollIntoWebView(bookButton);
        driverTools.waitForElementToAppear(bookButton, 1);
        driverTools.clickWithJavascriptExecutor(bookButton);
        logger.log("Successfully clicked book button for vendor");
    }

    @SneakyThrows
    public String searchForDesiredVendor(String desiredVendor, int maxTries) {
        String vendorName = "";
        int currentTry = 0;
        boolean vendorFound = false;

        logger.log("Searching for desired vendor: " + desiredVendor + " with max tries: " + maxTries);
        
        while (currentTry < maxTries && !vendorFound) {
            currentTry++;
            logger.log("Attempt " + currentTry + " to find vendor: " + desiredVendor);
            
            logger.log("Scrolling down to find vendor...");
            driverTools.scroll(5, 500);
            // Wait for vendor elements to be present after scrolling
            driverTools.waitForElementToPresence(VENDOR_COLOR, 2);
            
            // Check all vendor indicators on the page
            java.util.List<WebElement> vendorElements = driver.findElements(VENDOR_COLOR.by());
            logger.log("Found " + vendorElements.size() + " vendor indicators on attempt " + currentTry);
            
            for (WebElement vendorElement : vendorElements) {
                vendorName = getVendorNameFromColor(vendorElement);
                logger.log("Checking vendor: " + vendorName);
                
                if (vendorName.contains(desiredVendor)) {
                    vendorFound = true;
                    logger.log("Desired vendor " + desiredVendor + " found on attempt " + currentTry);
                    // Click book button for the vendor element we just found
                    logger.log("Clicking book button for desired vendor...");
                    clickBookButtonForVendor(vendorElement);
                    break;
                }
            }
            
            if (!vendorFound) {
                logger.log("Desired vendor not found. Continuing to scroll...");
            }
        }

        if (!vendorFound) {
            logger.warn("Desired vendor " + desiredVendor + " not found after " + maxTries + " attempts. Last vendor found: " + vendorName);
        }

        return vendorName;
    }

    public void secondBookRoom(){
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

    public boolean roomAvailable(){
        return driverTools.isElementDisplayed(ROOM_NAME);
    }
    public String getNumberOfGuest(){
        WebElement webElement=driverTools.waitForElementToAppear(GUEST_NUMBER).orElseThrow();
        return driverTools.getText(webElement);
    }

    public boolean getNightNumber(){
        WebElement webElement=driverTools.waitForElementToAppear(NIGHT_NUMBER).orElseThrow();
        String nightNumber=driverTools.getText(webElement);
        return nightNumber.contains("1 night");
    }

    public String getRoomNumberBesideRoomName(){
        WebElement webElement=driverTools.waitForElementToAppear(ROOM_PRICE_BESIDE_ROOM_NAME).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getTotalRecommendedRooms(){
        WebElement webElement=driverTools.waitForElementToAppear(TOTAL_RECOMMENDED_ROOMS).orElseThrow();
        String text= driverTools.getText(webElement);
        logger.log("Total recommended rooms text: " + text);
        return text.replaceAll("[^0-9]", "");
    }

    public void openRecommendedRoomDetails(){
        driverTools.waitForElementToAppear(RECOMMENDED_ROOM,5);
        driverTools.clickWithJavascriptExecutor(RECOMMENDED_ROOM);
    }

    public String selectRefundWithFeeRoom(){
        driverTools.waitForElementToAppear(ROOM_WITH_FEE_REFUND);
        Assertion.assertThat("Room with CHARGED fee refund option is selected", driverTools.isElementDisplayed(ROOM_WITH_FEE_REFUND), is(true));
        driverTools.clickWithJavascriptExecutor(ROOM_WITH_FEE_REFUND);
        String refundCharge = refundCharge();
        clickBookRoomDetail();
        return refundCharge;
    }

    public String refundCharge(){
        WebElement webElement=driverTools.waitForElementToAppear(ROOM_REFUND_CHARGE).orElseThrow();
        String feeText=driverTools.getText(webElement);
        logger.log("Refund charge detail: " + driverTools.getText(webElement));
        String amount = feeText.replaceAll("[^0-9,]", "");
        logger.log("Extracted refund charge amount: " + amount);
        return amount;
    }

    /**
     * Selects rooms based on the number of rooms required.
     * If more than 1 room, uses multi-room selection flow with verification.
     * Otherwise, selects the first room.
     */
    public void selectRoomsByCount(int noOfRoom) {
        if (noOfRoom > 1) {
            selectMultipleRoom(noOfRoom);
            int selectedRoomCount = getSelectedRoomCountFromQuantitySelector();
            Assertion.assertThat("Quantity selector should show " + noOfRoom + " rooms selected",
                    selectedRoomCount, is(noOfRoom));
            swipeToMultiRoomBookButton();
            clickMultiBookButton();
        } else {
            firstBookRoom();
        }
    }
}