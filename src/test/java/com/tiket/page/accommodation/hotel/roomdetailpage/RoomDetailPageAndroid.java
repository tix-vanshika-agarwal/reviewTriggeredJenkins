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

import java.util.List;

import static com.tiket.page.accommodation.hotel.detailpage.DetailPageAndroid.INCREMENT_BUTTON;
import static com.tiket.page.accommodation.hotel.roomlistpage.RoomListPageAndroid.*;
import static com.tiket.page.setting.SettingPageAndroid.SNACKBAR_TEXT;
import static org.hamcrest.Matchers.*;

public class RoomDetailPageAndroid <T extends WebDriver> extends RoomDetailPage<T> {

    public RoomDetailPageAndroid(Session<T> session) {
        super(session);
    }

    public static final Locator ROOM_SECTION_TITLE = new Locator("ROOM_SECTION_TITLE", By.xpath("//*[contains(@resource-id,'rv_item')]//*[contains(@resource-id,'tv_title')]"));
    public static final Locator ROOM_PRICE_ON_ROOM_DETAIL = new Locator("ROOM_PRICE_ON_ROOM_DETAIL", By.xpath("//*[contains(@resource-id,'btn_book')]//preceding-sibling::*[contains(@resource-id,'price_module')]//*[contains(@resource-id, 'price_module_final_price') or contains(@resource-id,'tv_main_price')]"));
    public static final Locator ROOM_PRICE_ON_ROOM_DETAIL_AFTER_TAX = new Locator("ROOM_PRICE_ON_ROOM_DETAIL", By.xpath("//*[contains(@resource-id,'footer_rate_info')]//*[contains(@resource-id,'tv_unit_2')]"));
    public static final Locator BOOK_BUTTON = new Locator("BOOK_BUTTON", By.xpath("(//*[(contains(@resource-id,'footer_rate_info') or contains(@resource-id,'btn_book'))]//*[contains(@resource-id,'tds_button_view')])[last()]"));
    public static final Locator SEE_ALL_PHOTOS = new Locator("SEE_ALL_PHOTOS", By.id("tv_see_all"));
    public static final Locator PHOTOS_INSIDE_GALLERY = new Locator("PHOTOS_INSIDE_GALLERY", By.id("v_filter"));
    private static final Locator CHILD_GUEST_COUNT = new Locator("CHILD_GUEST_COUNT", AppiumBy.xpath("//*[contains(@resource-id,'tv_child_occupancy') and (contains(@text,'Child') or contains(@text,'Anak') or contains(@text,'child') or contains(@text,'anak')) and not(contains(@text,'('))]"));
    private static final Locator CHILD_MEAL_COUNT = new Locator("CHILD_MEAL_COUNT", AppiumBy.xpath("//*[contains(@resource-id,'v_label_containers')]//*[(contains(@text,'Child') or contains(@text,'Anak') or contains(@text,'child') or contains(@text,'anak')) and contains(@text,'(')]"));
    private static final Locator CHILD_POLICY = new Locator("CHILD_POLICY", AppiumBy.xpath("//*[contains(@resource-id,'tv_item_description') and (contains(@text,'anak') or contains(@text,'children'))]"));
    private static final Locator PRICE_DETAIL_EXPAND = new Locator("PRICE_DETAIL_EXPAND", AppiumBy.id("v_click_area"));
    private static final Locator PRICE_DETAIL_SUB_TOTAL_EXPAND = new Locator("PRICE_DETAIL_SUB_TOTAL_EXPAND", AppiumBy.xpath("//*[contains(@resource-id,'tv_left') and contains(@text,'Subtotal')]//following-sibling::*[contains(@resource-id,'iv_chevron')]"));
    private static final Locator PRICE_DETAIL_TOTAL_PRICE = new Locator("PRICE_DETAIL_TOTAL_PRICE", AppiumBy.id("tv_right_bot"));
    private static final Locator CLOSE_BTN = new Locator("CLOSE_BTN", By.id("iv_close"));
    public static final Locator PAYING_AMOUNT_TEXT_GREEN = new Locator("PAYING_AMOUNT_TEXT_GREEN", AppiumBy.xpath("//*[contains(@text,'cashback')]"));

    // ====== Room info section, MO add+save ======
    private static final Locator ROOM_INFO_CAPACITY = new Locator("ROOM_INFO_CAPACITY", AppiumBy.xpath("//*[contains(@resource-id,'rv_item')]//*[contains(@resource-id,'v_guest_info')]"));
    private static final Locator ROOM_INFO_MEAL = new Locator("ROOM_INFO_MEAL", AppiumBy.xpath("//*[contains(@resource-id,'rv_item')]//*[contains(@resource-id,'v_label_containers')]"));
    private static final Locator ROOM_INFO_BED_TYPE = new Locator("ROOM_INFO_BED_TYPE", AppiumBy.id("tv_bed_types"));
    private static final Locator ROOM_INFO_SMOKING_PREFERENCES = new Locator("ROOM_INFO_SMOKING_PREFERENCES", AppiumBy.xpath("//*[contains(@resource-id,'rv_item')]//*[contains(@resource-id,'tv_smoking_preference')]"));

    @Override
    public String getHotelRoomNameOnRoomDetail() {
        WebElement roomNameElement = driverTools.waitForElementToAppear(ROOM_SECTION_TITLE, 10).orElseThrow();
        String roomName = driverTools.getText(roomNameElement);
        if (roomName.contains("x)")) roomName = roomName.substring(roomName.indexOf(' ') + 1);
        logger.log("roomName -> " + roomName);
        return roomName;
    }

    @Override
    public String getRawHotelRoomNameOnRoomDetail() {
        WebElement roomNameElement = driverTools.waitForElementToAppear(ROOM_SECTION_TITLE, 10).orElseThrow();
        String roomName = driverTools.getText(roomNameElement);
        logger.log("roomName -> " + roomName);
        return roomName;
    }

    @Override
    public String getHotelRoomPriceOnRoomDetail() {
        driverTools.waitForElementToAppear(ROOM_PRICE_ON_ROOM_DETAIL).orElseThrow();
        return driverTools.getText(ROOM_PRICE_ON_ROOM_DETAIL).orElseThrow();
    }

    @Override
    public String getHotelRoomPriceOnRoomDetailAfterTax(String currency) {
        WebElement totalPriceAfterTaxElement = driverTools.waitForElementToAppear(ROOM_PRICE_ON_ROOM_DETAIL_AFTER_TAX).orElseThrow();
        String totalPriceAfterTax = driverTools.getText(totalPriceAfterTaxElement);
        int idx = totalPriceAfterTax.indexOf(currency.toUpperCase());
        if (idx != -1) {
            // Extract from currency to end or until next space/parenthesis
            int endIdx = totalPriceAfterTax.indexOf("/", idx);
            if (endIdx == -1) {
                endIdx = totalPriceAfterTax.length();
            }
            totalPriceAfterTax = totalPriceAfterTax.substring(idx, endIdx).replace(")", "").trim();
            // Remove any trailing text after the price (e.g., ")" or "/")
            int spaceIdx = totalPriceAfterTax.indexOf(" ");
            if (spaceIdx != -1) {
                // Keep currency and price
                totalPriceAfterTax = totalPriceAfterTax.substring(0, totalPriceAfterTax.length());
            }
        }
        logger.log("totalPriceAfterTax -> " + totalPriceAfterTax);
        return totalPriceAfterTax;
    }

    @Override
    public String getTotalPriceOnRoomDetail() {
        openPriceDetail();
        WebElement totalPriceElement = driverTools.waitForElementToAppear(PRICE_DETAIL_TOTAL_PRICE).orElseThrow();
        String totalPrice = driverTools.getText(totalPriceElement);
        logger.log("total price (after taxes) on room detail price detail -> " + totalPrice);
        closePriceDetail();
        return totalPrice;
    }

    private Locator tabButton(int index) {
        return new Locator("TAB_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tab_layout') and contains(@class,'HorizontalScrollView')]//android.widget.LinearLayout[@index=" + index + "]"));
    }

    /**
     * This method validates the title of a section in the hotel room detail page.
     * It checks if the current tab title contains "Benefit" and if the section title text contains "Spesial" or "Special",
     * if so, it sets the section title text to "Benefit".
     * It then asserts that the section title text contains the current tab title.
     *
     * @param currentTabTitle The title of the current tab.
     * @param sectionTitleText The text of the section title.
     * @return true as the method always returns true after performing the assertion.
     */
    private boolean roomSectionTitle(String currentTabTitle, String sectionTitleText) {
        currentTabTitle = currentTabTitle.toLowerCase();
        sectionTitleText = sectionTitleText.toLowerCase();
        if (currentTabTitle.contains("benefit") && (sectionTitleText.contains("spesial") || sectionTitleText.contains("special"))) sectionTitleText = "benefit";
        Assertion.assertThat("The title section is '" + sectionTitleText + "' that CONTAINS text on the title tab: '" + currentTabTitle + "'", sectionTitleText, is(containsString(currentTabTitle)));
        return true;
    }

    @Override
    public Boolean hotelRoomDetailTabIsValid() {
        boolean isValid = false;
        boolean nextTabButton = true;
        String nextTabTitle;
        int tabIndex = 1;

        driverTools.swipeUp(ROOM_SECTION_TITLE);
        while (nextTabButton && tabIndex < 15) {
            WebElement sectionTabIndex = driverTools.waitForElementToBeClickable(tabButton(tabIndex)).orElseThrow();
            String currentTabTitle = driverTools.getValueAttribute(sectionTabIndex, "content-desc");
            String sectionTitleText = driverTools.getText(ROOM_SECTION_TITLE).orElseThrow();

            if (sectionTitleText.contains(currentTabTitle)) isValid = roomSectionTitle(currentTabTitle, sectionTitleText);
            if (!isValid) {
                List<WebElement> sectionTitle = driverTools.elements(ROOM_SECTION_TITLE).orElseThrow();
                int sectionTitleAmount = sectionTitle.size();
                for (int i = 1; i < sectionTitleAmount; i++) {
                    sectionTitleText = driverTools.getText(sectionTitle.get(i));
                    if (sectionTitleText.contains(currentTabTitle)) isValid = roomSectionTitle(currentTabTitle, sectionTitleText);
                    if (isValid) break;
                }
            }

            int nextTabIndex = tabIndex + 1;
            boolean nextSectionTabIndex = driverTools.isElementDisplayed(tabButton(nextTabIndex));
            if (nextSectionTabIndex) {
                nextTabTitle = driverTools.getValueAttribute(tabButton(nextTabIndex), "content-desc");
                if (nextTabTitle.equals(currentTabTitle)) {
                    break;
                }
                driverTools.click(tabButton(nextTabIndex));
                tabIndex++;
            } else {
                nextTabButton = false;
            }
        }
        return isValid;
    }

    @Override
    public void clickBookButton(int totalRoom) {
        logger.log("totalRoom -> " + totalRoom);
        WebElement webElement = driverTools.swipeToElement(BOOK_BUTTON, Swipe.UP, 1).orElseThrow();
        driverTools.click(webElement);
        if (totalRoom > 1 && driverTools.isElementDisplayed(INCREMENT_BUTTON)) {
            logger.log("multi room button");
            for(int i = 1; i < totalRoom; i++) {
                driverTools.swipeToElement(INCREMENT_BUTTON, Swipe.UP, 3);
                driverTools.click(INCREMENT_BUTTON);
            }
            driverTools.waitForElementToDisappear(SNACKBAR_TEXT);
            driverTools.click(BOOK_BUTTON);
            driverTools.waitForElementToDisappear(SNACKBAR_TEXT);
            if (driverTools.isElementDisplayed(EXCLUSIVE_ROOM_SECTION)){
                try {
                    driverTools.swipeByDistance(EXCLUSIVE_ROOM_SECTION, Swipe.UP, 0.9, 3);
                }catch (Exception e){
                    logger.log("EXCLUSIVE_ROOM_SECTION not found, swipe up to find BOOK_BUTTON");
                }
            }
            driverTools.waitForElementToAppear(BOOK_BUTTON).ifPresent(driverTools::click);
        }
    }
    
    @Override
    public void clickAddRoomButton(int totalRoom){
        logger.log("totalRoom -> " + totalRoom);
        WebElement webElement = driverTools.swipeToElement(BOOK_BUTTON, Swipe.UP, 1).orElseThrow();
        driverTools.click(webElement);
        if (totalRoom > 1 && driverTools.isElementDisplayed(INCREMENT_BUTTON)) {
            logger.log("multi room button");
            for(int i = 1; i < totalRoom; i++) {
                driverTools.swipeToElement(INCREMENT_BUTTON, Swipe.UP, 3);
                driverTools.click(INCREMENT_BUTTON);
            }
            driverTools.waitForElementToDisappear(SNACKBAR_TEXT);
            driverTools.click(BOOK_BUTTON);
            driverTools.waitForElementToDisappear(SNACKBAR_TEXT);
        }
    }


    @Override
    public void seeAllphotos(){
        driverTools.click(SEE_ALL_PHOTOS);
    }
    @Override
    public boolean isPhotoGalleryVisible(){
        return driverTools.waitForElementToAppear(PHOTOS_INSIDE_GALLERY).isPresent();
    }
    /**
     * @param tierLevel varieties of tier input (Diamond, Platinum, Gold, Silver, Blue)
     */
    private Locator checkLoyaltyBenefitSection(String tierLevel){
        return new Locator(tierLevel, AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'"+tierLevel+"')]"));
    }
    @Override
    public Boolean verifyLoyaltyBenefitSection(String tierLevel){
        WebElement loyaltyBenefitSection =  driverTools.swipeToElement(checkLoyaltyBenefitSection(tierLevel), Swipe.UP,3).orElseThrow();
        return driverTools.isElementDisplayed(loyaltyBenefitSection);
    }

    @Override
    public boolean checkChildGuest() {
        driverTools.waitForElementToAppear(CHILD_GUEST_COUNT);
        return driverTools.isElementDisplayed(CHILD_GUEST_COUNT);
    }

    @Override
    public boolean checkChildMeal() {
        return driverTools.isElementDisplayed(CHILD_MEAL_COUNT);
    }

    @Override
    public boolean checkChildRoomPolicy() {
        driverTools.swipeToElement(CHILD_POLICY, Swipe.UP, 10);
        return driverTools.isElementDisplayed(CHILD_POLICY);
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
    public boolean checkRoomDetail(int totalGuest,int roomCapacity,String searchCriteria) {
        //when search criteria matches then there will be no updated room count display with room name
        int updatedRoomCount = CoreFunctions.divisionAndRoundUp(totalGuest,roomCapacity);
        logger.log("total Guest: " + totalGuest + " room capacity: " + roomCapacity + " updated room count: " + updatedRoomCount);

        WebElement roomNameElement = driverTools.waitForElementToBeClickable(ROOM_SECTION_TITLE).orElseThrow();
        String roomName = driverTools.getText(roomNameElement);
        boolean isRoomNameDisplayed = false;
        if (searchCriteria.equals("less")){
            isRoomNameDisplayed = roomName.contains("("+updatedRoomCount+"x)");
            Assertion.assertThat("Room name contains updated room count ("+updatedRoomCount+"x) is displayed", roomName, is(containsString("("+updatedRoomCount + "x)")));
        }else {
            //no updated room count display with room name, so it will false, making it true for final return
            isRoomNameDisplayed = !roomName.isEmpty();
            Assertion.assertThat("Room name not contains updated room count",roomName , is(not(emptyString())));
        }

        openPriceDetail();
        driverTools.waitForElementToAppear(CLOSE_BTN);
        if(driverTools.waitForElementToAppear(getFirstPriceDetailItemLocator()).isEmpty()){
            driverTools.click(PRICE_DETAIL_SUB_TOTAL_EXPAND);
        }
        WebElement priceDetailItem = driverTools.waitForElementToAppear(getFirstPriceDetailItemLocator()).orElseThrow();
        String priceDetailItemText = driverTools.getText(priceDetailItem);
        boolean isPriceDetailItemDisplayed = priceDetailItemText.contains(String.valueOf(updatedRoomCount));
        Assertion.assertThat("Price detail item contains updated room ", priceDetailItemText, is(containsString(String.valueOf(updatedRoomCount))));
        closePriceDetail();
        return isRoomNameDisplayed && isPriceDetailItemDisplayed;
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
    private Locator getFirstPriceDetailItemLocator(){
        return new Locator("PRICE_DETAIL_ITEM", AppiumBy.xpath("//*[contains(@resource-id,'tv_left') and contains(@text,' x ')]"));
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