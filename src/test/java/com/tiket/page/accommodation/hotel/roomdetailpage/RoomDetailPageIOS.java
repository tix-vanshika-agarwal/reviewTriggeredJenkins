package com.tiket.page.accommodation.hotel.roomdetailpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tiket.page.accommodation.hotel.roomlistpage.RoomListPageIOS.BOOK_BUTTON_LIST;
import static org.hamcrest.Matchers.*;

public class RoomDetailPageIOS <T extends WebDriver> extends RoomDetailPage<T> {

    public RoomDetailPageIOS(Session<T> session) {
        super(session);
    }

    public static final Locator ROOM_NAME_ON_ROOM_DETAIL = new Locator("ROOM_NAME_ON_ROOM_DETAIL", By.id("TVCell<TDSText>"));
    public static final Locator ROOM_PRICE_ON_ROOM_DETAIL = new Locator("ROOM_PRICE_ON_ROOM_DETAIL", By.xpath("//*[@name='tds_ic_chevron_down']//parent::*//preceding-sibling::XCUIElementTypeStaticText"));
    private static final Locator PRICE_WITHOUT_CHEVRON = new Locator("PRICE_WITHOUT_CHEVRON", AppiumBy.iOSClassChain("**/*[`name == 'priceModuleView'`][1]/**/XCUIElementTypeStaticText"));
    public static final Locator ROOM_DETAIL_TABS = new Locator("ROOM_DETAIL_TABS", AppiumBy.xpath("//*[@name='tabView']//*[contains(@name,'textLabel')]"));
    public static final Locator BOOK_BUTTON = new Locator("BOOK_BUTTON", AppiumBy.name("bookButton"));
    public static final Locator PHOTOS_INSIDE_GALLERY = new Locator("PHOTOS_INSIDE_GALLERY", AppiumBy.iOSClassChain("**/XCUIElementTypeScrollView/XCUIElementTypeOther/XCUIElementTypeImage"));
    public static final Locator SEE_ALL_PHOTOS = new Locator("SEE_ALL_PHOTOS", By.xpath("//XCUIElementTypeButton[@label='Lihat semua foto' or @label='See all photos']"));
    public static final Locator ADD_ROOM_BUTTON = new Locator("ADD_ROOM_BUTTON", By.name("add_btn"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.name("book_button"));
    private static final Locator PRICE_DETAIL_EXPAND = new Locator("PRICE_DETAIL_EXPAND", AppiumBy.accessibilityId("tds_ic_chevron_down"));
    private static final Locator CLOSE_BTN = new Locator("PRICE_DETAIL_EXPAND", AppiumBy.accessibilityId("ic cancel grey"));
    public static final Locator PAYING_AMOUNT_TEXT_GREEN = new Locator("PAYING_AMOUNT_TEXT_GREEN", AppiumBy.xpath("//*[contains(@label,'cashback points')]"));

    // ====== Room info section, MO add+save ======
    private static final Locator ROOM_INFO_CAPACITY = new Locator("ROOM_INFO_CAPACITY", AppiumBy.xpath("//*[@name='occupancyInfoText' and @visible='true']"));
    private static final Locator ROOM_INFO_MEAL = new Locator("ROOM_INFO_MEAL", AppiumBy.xpath("//*[@name='textlabel_1' and @visible='true']"));
    private static final Locator ROOM_INFO_BED_TYPE = new Locator("ROOM_INFO_BED_TYPE", AppiumBy.xpath("//*[@name='bedroomLabel' and @visible='true']"));
    private static final Locator ROOM_INFO_SMOKING_PREFERENCES = new Locator("ROOM_INFO_SMOKING_PREFERENCES", AppiumBy.xpath("//*[@name='smokingPreferencesLabel' and @visible='true']"));

    @Override
    public String getHotelRoomNameOnRoomDetail() {
        driverTools.waitForElementToAppear(ROOM_NAME_ON_ROOM_DETAIL).orElseThrow();
        String name = driverTools.getText(ROOM_NAME_ON_ROOM_DETAIL).orElseThrow();
        if(name.contains("x)")){
            name = name.substring(name.indexOf(' ') + 1);
            logger.log("Hotel name: "+name);
        }
        return name;
    }

    @Override
    public String getRawHotelRoomNameOnRoomDetail() {
        WebElement roomNameElement = driverTools.waitForElementToAppear(ROOM_NAME_ON_ROOM_DETAIL, 10).orElseThrow();
        String roomName = driverTools.getText(roomNameElement);
        logger.log("roomName -> " + roomName);
        return roomName;
    }

    @Override
    public String getHotelRoomPriceOnRoomDetail() {
        String roomPrice;
        if (driverTools.waitForElementToPresence(ROOM_PRICE_ON_ROOM_DETAIL, 20).isPresent()) {
            WebElement roomPriceElement = driverTools.waitForElementToPresence(ROOM_PRICE_ON_ROOM_DETAIL).orElseThrow();
            roomPrice = driverTools.getText(roomPriceElement);
            logger.log("roomPrice on room detail with chevron-> " + roomPrice);
        } else {
            logger.log("Failed to get room price with chevron down, trying without chevron down");
            WebElement priceWithoutChevron = driverTools.waitForElementToPresence(PRICE_WITHOUT_CHEVRON).orElseThrow();
            roomPrice = driverTools.getText(priceWithoutChevron);
            logger.log("roomPrice on room detail without chevron-> " + roomPrice);
        }
        String pattern = "[A-Z]{3}\\s\\d{1,3}(?:[\\.,]\\d{3})*(?:[\\.,]\\d{1,2})?";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(roomPrice);

        if (matcher.find()) {
            roomPrice = matcher.group();
        } else {
            throw new NoSuchElementException("Room price format not match with regex");
        }
        logger.log("roomPrice on room detail-> " + roomPrice);
        return roomPrice;
    }

    @Override
    public String getHotelRoomPriceOnRoomDetailAfterTax(String currency) {
        //fixme: Pending implementation
        return "";
    }

    @Override
    public String getTotalPriceOnRoomDetail() {
        //fixme: Pending implementation
        return "";
    }

    /**
     * This method validates the tabs in the hotel room detail page on iOS.
     * It iterates over the tabs, checks if they are displayed, and clicks on them.
     * It then retrieves the title of the current tab and the title of the current page, and logs them.
     * If the tab title contains "Benefit", and the page title contains "Spesial" or "Special", it sets the page title to "Benefit".
     * It then asserts that the page title contains the tab title.
     *
     * @return true if all assertions pass, indicating that the tabs are valid.
     */
    @Override
    public Boolean hotelRoomDetailTabIsValid() {

        driverTools.swipeUp();

        int tabs_size = driverTools.elements(ROOM_DETAIL_TABS).orElseThrow().size();
        int tabIndex = 2;

        while (tabIndex < tabs_size){
            Locator currentTab = getHotelRoomDetailTab(tabIndex);
            Locator previousTab = getHotelRoomDetailTab(tabIndex-1);
            Locator currentPage = getHotelRoomDetailPage(tabIndex-1);

            if(!driverTools.isElementDisplayed(currentTab)){
                driverTools.swipeLeft(previousTab);
            }

            driverTools.click(currentTab);
            String tabTitle = driverTools.getText(currentTab).orElseThrow().toLowerCase();
            WebElement pageTitleElement = driverTools.waitForElementToPresence(currentPage).orElseThrow();
            String pageTitle = driverTools.getText(pageTitleElement).toLowerCase();
            logger.log("tabTitle is \""+ tabTitle +"\" and pageTitle is \"" + pageTitle + "\"");

            if (tabTitle.contains("benefit") && (pageTitle.contains("spesial") || pageTitle.contains("special") || pageTitle.contains("discount") || pageTitle.contains("diskon"))) pageTitle = "benefits";
            Assertion.assertThat("The title section is '" + pageTitle + "' that CONTAINS text on the title tab: '" + tabTitle + "'", pageTitle, is(containsString(tabTitle)));
            tabIndex++;
        }

        return true;
    }

    /**
     * This method returns a Locator object for a specific tab in the hotel room detail page.
     * The tab is identified by its index in the list of tabs.
     *
     * @param index The index of the tab in the list of tabs.
     * @return A Locator object representing the tab at the specified index.
     */
    private Locator getHotelRoomDetailTab(int index) {
        return new Locator("ROOM_DETAIL_TAB", AppiumBy.xpath("(//*[@name='tabView']//*[contains(@name,'textLabel')])["+index+"]"));
    }

    /**
     * This method returns a Locator object for a specific page in the hotel room detail page.
     * The page is identified by its index in the list of pages.
     *
     * @param index The index of the page in the list of pages.
     * @return A Locator object representing the page at the specified index.
     */
    private Locator getHotelRoomDetailPage(int index) {
        return new Locator("ROOM_DETAIL_PAGE", AppiumBy.xpath("(//XCUIElementTypeCell[contains(@name,'TVCell')]//*[@name='titleLabel'])["+index+"]"));
    }

    @Override
    public void clickBookButton(int totalRoom){
        if(driverTools.swipeToElement(BOOK_BUTTON_LIST,Swipe.UP,2).isPresent()){
            driverTools.click(BOOK_BUTTON_LIST);
        }else{
            WebElement bookButton = driverTools.swipeToElement(BOOK_BUTTON,Swipe.DOWN,2).orElseThrow();
            driverTools.click(bookButton);
        }

        if (driverTools.waitForElementToPresence(ADD_ROOM_BUTTON).isPresent()) {
            for (int i = 1; i < totalRoom; i++) {
                driverTools.click(ADD_ROOM_BUTTON);
            }
            driverTools.click(SAVE_BUTTON);
            driverTools.waitForElementToDisappear(SAVE_BUTTON);
            if(driverTools.isElementDisplayed(SAVE_BUTTON)){
                driverTools.click(SAVE_BUTTON);
            }
        }
    }

    @Override
    public void clickAddRoomButton(int totalRoom){
        logger.log("totalRoom -> " + totalRoom);
        if(driverTools.swipeToElement(BOOK_BUTTON_LIST,Swipe.UP,2).isPresent()){
            driverTools.click(BOOK_BUTTON_LIST);
        }else{
            WebElement bookButton = driverTools.swipeToElement(BOOK_BUTTON,Swipe.DOWN,2).orElseThrow();
            driverTools.click(bookButton);
        }

        if (driverTools.waitForElementToPresence(ADD_ROOM_BUTTON).isPresent()) {
            for (int i = 1; i < totalRoom; i++) {
                driverTools.click(ADD_ROOM_BUTTON);
            }
            driverTools.click(SAVE_BUTTON);
            driverTools.waitForElementToDisappear(SAVE_BUTTON);
        }
    }


    public void seeAllphotos(){
        driverTools.click(SEE_ALL_PHOTOS);
    }

    @Override
    public boolean isPhotoGalleryVisible(){
        return driverTools.isElementDisplayed(PHOTOS_INSIDE_GALLERY);
    }

    /**
     * @param tierLevel varieties of tier input (Diamond, Platinum, Gold)
     */
    @Override
    public  Boolean verifyLoyaltyBenefitSection(String tierLevel){
        boolean status = driverTools.swipeToElement(checkLoyaltyBenefitSection(tierLevel), Swipe.UP,3).isPresent();
        logger.log("verifyLoyaltyBenefitSection status -> " + status);
        return status;
    }

    /**
     * @param tierLevel varieties of tier input (Diamond, Platinum, Gold)
     */
    private Locator checkLoyaltyBenefitSection(String tierLevel){
        return new Locator(tierLevel, AppiumBy.xpath("//XCUIElementTypeCell[@name='TVCell<THMRoomDetailSectionHeaderWithImageView>']//XCUIElementTypeStaticText[contains(@value, '"+tierLevel+"')]"));
    }

    @Override
    public boolean checkChildGuest(){
        return false; //FIXME: Pending implementation
    }

    @Override
    public boolean checkChildMeal() {
        return false; //FIXME: Pending implementation
    }

    @Override
    public boolean checkChildRoomPolicy() {
        return false; //FIXME: Pending implementation
    }

    /**
     * Checks the room detail by comparing the total guest, room capacity and search criteria.
     * It calculates the updated room count and asserts that the room name contains the updated room count.
     * It then opens the price detail and asserts that the price detail item contains the updated room count.
     * Finally, it closes the price detail.
     *
     * @param totalGuest The total number of guests.
     * @param roomCapacity The capacity of the room.
     * @param searchCriteria The search criteria to match. If it equals "less", the room name should contain the updated room count.
     * @return true if the room name contains the updated room count and the price detail item contains the updated room count, false otherwise.
     */
    @Override
    public boolean checkRoomDetail(int totalGuest,int roomCapacity,String searchCriteria){
        //when search criteria matches then there will be no updated room count display with room name
        int updatedRoomCount = CoreFunctions.divisionAndRoundUp(totalGuest,roomCapacity);
        logger.log("total Guest: " + totalGuest + " room capacity: " + roomCapacity + " updated room count: " + updatedRoomCount);

        WebElement roomNameElement = driverTools.waitForElementToBeClickable(ROOM_NAME_ON_ROOM_DETAIL).orElseThrow();
        String roomName = driverTools.getText(roomNameElement);
        boolean isRoomNameContainsUpdatedRoomCount = false;
        if (searchCriteria.equals("less")){
            isRoomNameContainsUpdatedRoomCount = roomName.contains("("+updatedRoomCount + "x)");
            Assertion.assertThat("Room name contains updated room count ("+updatedRoomCount+"x) is displayed", roomName, is(containsString("("+updatedRoomCount + "x)")));
        }else {
            isRoomNameContainsUpdatedRoomCount = !roomName.isEmpty();
            Assertion.assertThat("Room name not contains updated room count",roomName , is(not(emptyString())));
        }

        openPriceDetail();
        WebElement priceDetailItem = driverTools.waitForElementToAppear(getFirstPriceDetailItemLocator(1)).orElseThrow();
        String priceDetailItemText = driverTools.getText(priceDetailItem);
        boolean isPriceDetailItemContainsUpdatedRoom = priceDetailItemText.contains(String.valueOf(updatedRoomCount));
        Assertion.assertThat("Price detail item contains updated room ", priceDetailItemText, is(containsString(String.valueOf(updatedRoomCount))));
        closePriceDetail();
        return isRoomNameContainsUpdatedRoomCount && isPriceDetailItemContainsUpdatedRoom;
    }

    /**
     * Opens the price detail by clicking on the PRICE_DETAIL_EXPAND element.
     */
    private void openPriceDetail(){
        driverTools.click(PRICE_DETAIL_EXPAND);
    }

    /**
     * Closes the price detail by clicking on the CLOSE_BTN element.
     */
    private void closePriceDetail(){
        driverTools.click(CLOSE_BTN);
    }

    /**
     * Gets the locator of the first price detail item.
     * The locator is created with the xpath of the price detail item element, which contains the text "Rooms" or "Kamar".
     *
     * @return a Locator object for the first price detail item.
     */
    private Locator getFirstPriceDetailItemLocator(int index){
        return new Locator("PRICE_DETAIL_ITEM", AppiumBy.xpath("(//XCUIElementTypeCell[contains(@name,'TVCell<THMPriceDetailTwoGridView>') and contains(@name,'BillRxCell')]//*[@name='label'])["+index+"]"));
    }

    @Override
    public boolean isRoomInfoDetailsDisplayed() {
        boolean capacity = driverTools.waitForElementToPresence(ROOM_INFO_CAPACITY).isPresent();
        boolean meal = driverTools.waitForElementToPresence(ROOM_INFO_MEAL).isPresent();
        boolean bed = driverTools.waitForElementToPresence(ROOM_INFO_BED_TYPE).isPresent();
        boolean smokingPreferences = driverTools.waitForElementToPresence(ROOM_INFO_SMOKING_PREFERENCES).isPresent();
        logger.log("isRoomInfoDetailsDisplayed -> capacity:" + capacity + ", meal:" + meal + ", bed:" + bed + ", smokingPreferences:" + smokingPreferences);
        return capacity && meal && bed && smokingPreferences;
    }
    
    @Override
    public boolean isPayingThisAmountTextInGreen() {
        openPriceDetail();
        WebElement payingAmount = driverTools.swipeToElement(PAYING_AMOUNT_TEXT_GREEN, Swipe.UP, 5).orElse(null);
        if (payingAmount == null) {
            logger.softWarning("'You will be paying this amount' nudge not found on Room Detail");
            return false;
        }
        String payingAmountText = driverTools.getText(payingAmount);
        logger.log("payingAmountText -> " + payingAmountText);
        closePriceDetail();
        return !payingAmountText.isEmpty();
    }
}