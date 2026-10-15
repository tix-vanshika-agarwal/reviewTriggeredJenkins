package com.tiket.web.page.accommodation.hotels.SRP;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.web.page.common.home.HomePageWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SRPPageMweb<T extends WebDriver> extends SRPPageWeb<T> {
    private static final Locator HOTEL_CARD = new Locator("HOTEL_CARD",new By.ByXPath("(//a[contains(@class,'FullProductCard_container') and @data-testid='full-product-card'])"));
    private static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]//h3"));
    private static final Locator PRICE_TAG = new Locator("PRICE_TAG", new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]//div[contains(@class,'PriceModule_final')]"));
    private static final Locator HOTEL_LOCATION = new Locator("HOTEL_LOCATION", new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]//div[contains(@class,'address')]//span"));
    private static final Locator HOTEL_IMAGE_CARD = new Locator("HOTEL_IMAGE_CARD", new By.ByXPath("((//a[contains(@class, 'ProductCard_container')])[1]//img)[1]"));
    private static final Locator SRP_MAP = new Locator("SRP_MAP", new By.ByXPath("//span[contains(@class,'MapEntryButton')]"));
    private static final Locator HOTEL_CARD_BENEFITS = new Locator("HOTEL_CARD_BENEFITS", new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]//div[contains(@class,'Benefits')]"));
    private static final Locator SEARCH_BAR = new Locator("SEARCH_BAR", new By.ByXPath("//div[contains(@class, 'SearchBox_placeholder')]//input"));
    private static final Locator CHANGE_SEARCH_BUTTON = new Locator("CHANGE_SEARCH_BUTTON",new By.ByXPath("//button[contains(@class, 'index_mobile_button_change_search')]"));
    private static final Locator CHANGE_DESTINATION_INPUT = new Locator("CHANGE_DESTINATION_INPUT",new By.ByXPath("//div[contains(@class, 'ChangeSearchMobile_destination')]"));
    private static final Locator CHANGE_DATE_INPUT = new Locator("CHANGE_DATE_INPUT",new By.ByXPath("//div[contains(@class, 'ChangeSearchMobile_calendar')]"));
    private static final Locator CHANGE_GUEST_INPUT = new Locator("CHANGE_GUEST_INPUT",new By.ByXPath("//div[contains(@class, 'ChangeSearchMobile_guest_room')]"));
    private static final Locator DONE_CHANGE_GUEST = new Locator("DONE_CHANGE_GUEST",new By.ByXPath("//button[@data-testid='guest-room-submit']"));
    private static final Locator CONFIRM_DATE_CHANGE = new Locator("CONFIRM_DATE_CHANGE",new By.ByXPath("//button[@data-testid='date-confirmation-btn']"));
    private static final Locator CURRENT_SEARCH_LOCATION_SRP = new Locator("CHANGE_GUEST_INPUT",new By.ByXPath("//span[contains(@class, 'index_mobile_text')]"));
    private static final Locator CURRENT_NO_OF_ROOM = new Locator("CURRENT_NO_OF_ROOM",new By.ByXPath("//div[contains(@class, 'index_mobile_date_container')]//span[contains(text(), 'Room') or contains(text(), 'Kamar')]"));
    private static final Locator CURRENT_NO_OF_GUEST = new Locator("CURRENT_NO_OF_GUEST",new By.ByXPath("//div[contains(@class, 'index_mobile_date_container')]//span[contains(text(), 'Guest') or contains(text(), 'Tamu')]"));
    private static final Locator ADULT_INC_BUTTON = new Locator("ADULT_INC_BUTTON", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[4]"));
    private static final Locator ADVANCED_FILTER_ACTIVE = new Locator("ADVANCED_FILTER_ACTIVE", new By.ByXPath("//button[contains(@class, 'Chip_chip')]//span[contains(text(), 'Filter')]/../following-sibling::*//*[contains(@class, 'Notif_color_green')]"));
    private static final Locator QUICK_FILTER_SORT_ACTIVE = new Locator("QUICK_FILTER_SORT_ACTIVE", new By.ByXPath("//button[contains(@class, 'Chip_chip')]//span[contains(text(), 'Sort') or contains(text(), 'Urutkan')]/../following-sibling::*//*[contains(@class, 'Notif_color_green')]"));
    private static final Locator QUICK_FILTER_STAR_RATING_ACTIVE = new Locator("QUICK_FILTER_STAR_RATING_ACTIVE", new By.ByXPath("//button[contains(@class, 'Chip_active')]//span[contains(text(), 'Star') or contains(text(), 'Bintang')]"));
    private static final Locator HOTEL_INPUT_LOCATION = new Locator("HOTEL_INPUT_LOCATION", new By.ByXPath("//span[contains(@class, 'index_mobile_text__Csl__')]"));
    private static final Locator CHANGE_SEARCH_LOCATION_INPUT = new Locator("CHANGE_SEARCH_LOCATION_INPUT", new By.ByXPath("//*[@data-testid='mobile-change-search-container']//*[contains(@class,'ChangeSearchMobile_destination_container')]"));
    private static final Locator CHANGE_SEARCH_CRITERIA_BUTTON = new Locator("CHANGE_SEARCH_CRITERIA_BUTTON", new By.ByXPath("//button[contains(@class, 'index_mobile_button_change_search')]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[@data-testid='product-container-mobile-search-submit']"));
    private static final Locator CHANGE_LOC = new Locator("CHANGE_LOC", new By.ByXPath("(//div[contains(@class,'ChangeSearchMobile_input_container_var_b')])[1]"));
    private static final Locator CHANGE_LOC_SEARCHBAR = new Locator("CHANGE_LOC_SEARCHBAR", new By.ByXPath("//input[@data-testid='destination-search-box']"));
    private static final Locator SEARCH_LOCATION_HEADER_LABEL_MWEB = new Locator("SEARCH_LOCATION_HEADER_LABEL_MWEB", new By.ByXPath("//span[contains(@class,'index_mobile_text__Csl__')]"));
    private static final Locator SEARCH_DATE_HEADER_LABEL_MWEB = new Locator("SEARCH_DATE_HEADER_LABEL_MWEB", new By.ByXPath("//div[contains(@class,'index_mobile_date_container')]/span[1]"));
    private static final Locator SEARCH_GUEST_HEADER_LABEL_MWEB = new Locator("SEARCH_GUEST_HEADER_LABEL_MWEB", new By.ByXPath("//div[contains(@class,'index_mobile_date_container')]/span[3]"));
    private static final Locator MAIN_FILTER_CONTAINER = new Locator("MAIN_FILTER_CONTAINER", new By.ByXPath("//div[contains(@class, 'mobile_main_filter_containe')]"));
    private static final Locator POPULAR_FILTER = new Locator("POPULAR_FILTER", new By.ByXPath("//span[contains(text(), 'Popular') or contains(text(), 'Populer')]"));
    private static final Locator POPULAR_FILTER_BOTTOMSHEET = new Locator("POPULAR_FILTER", new By.ByXPath("//h2[contains(text(), 'Popular Filter in') or contains(text(), 'Filter Populer di')]"));
    private static final Locator POPULAR_FILTER_CHIPS = new Locator("POPULAR_FILTER_CHIPS", new By.ByXPath("//div[contains(@class, 'PopularFilterTag_chips_wrapper')]//button[contains(@class, 'Chip_chip')]"));
    private static final Locator POPULAR_FILTER_CHIPS_ACTIVE = new Locator("POPULAR_FILTER_CHIPS_ACTIVE", new By.ByXPath("//div[contains(@class, 'PopularFilterTag_chips_wrapper')]//button[contains(@class, 'Chip_active')]"));
    private static final Locator POPULAR_FILTER_SAVE_BUTTON = new Locator("POPULAR_FILTER_SAVE_BUTTON", new By.ByXPath("//div[contains(@class, 'PopularFilterTag_action_container__QC59f')]//button[contains(@class, 'Button')]"));
    private static final Locator POPULAR_FILTER_RESET_BUTTON = new Locator("POPULAR_FILTER_SAVE_BUTTON", new By.ByXPath("//button[text()='Reset']"));
    private static final Locator FILTER_CONTAINER = new Locator("FILTER_CONTAINER", new By.ByXPath("//*[contains(@class, 'SrpFilter_wrapper')]"));

    public SRPPageMweb(Session<T> session) {
        super(session);
    }

    @Override
    public void setPriceTaxFilter(boolean isTotalPrice, boolean isAfterTax) throws InterruptedException {
        openFilterMenu();
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        homePageWeb.continueWithoutApp(5);
        if (isTotalPrice) clickTotalPriceToggle();
        if (isAfterTax) clickIncludeTaxToggle();
    }

    private void clickTotalPriceToggle() {
        driverTools.waitForElementToAppear(TOTAL_PRICE_TOGGLE);
        driverTools.scrollInsideElement(FILTER_CONTAINER, 100);
        driverTools.click(TOTAL_PRICE_TOGGLE);
    }

    private void clickIncludeTaxToggle() {
        driverTools.waitForElementToAppear(TOTAL_PRICE_TOGGLE);
        driverTools.scrollInsideElement(FILTER_CONTAINER, 100);
        driverTools.click(AFTER_TAX_TOGGLE);
    }


    @Override
    public void applyQuickFilters() {
        driverTools.waitForElementToPresence(QUICK_FILTER_SORT_CHIP);

        driverTools.click(QUICK_FILTER_SORT_CHIP);
        driverTools.click(QUICK_FILTER_SORT_ITEM);
        driverTools.waitForElementToDisappear(SRP_LOADING);

        driverTools.click(QUICK_FILTER_STAR_RATING_CHIP);
        driverTools.click(QUICK_FILTER_STAR_RATING_ITEM);
        driverTools.click(QUICK_FILTER_STAR_RATING_SAVE);
        driverTools.waitForElementToDisappear(SRP_LOADING);
    }

    @Override
    public boolean isQuickFilterApplied() {
        driverTools.scrollElementToCenter(QUICK_FILTER_SORT_ACTIVE);
        boolean sortPresent = driverTools.waitForElementToPresence(QUICK_FILTER_SORT_ACTIVE).isPresent();

        driverTools.scrollElementToCenter(QUICK_FILTER_STAR_RATING_ACTIVE);
        boolean starRatingPresent = driverTools.waitForElementToPresence(QUICK_FILTER_STAR_RATING_ACTIVE).isPresent();

        return sortPresent && starRatingPresent;
    }

    @Override
    public void applyAdvancedFilters() {
        driverTools.click(ADVANCED_FILTER_CHIP);

        clickAdvancedFilterItem(ADVANCED_FILTER_POPULAR_ITEM, false);
        clickAdvancedFilterItem(ADVANCED_FILTER_SWITCH_INPUT_ITEM, false);
        clickAdvancedFilterItem(ADVANCED_FILTER_FACILITY_GROUP_ITEM, false);
        clickAdvancedFilterItem(ADVANCED_FILTER_CHECKBOX_ITEM, true);

        driverTools.click(ADVANCED_FILTER_SAVE);
        driverTools.waitForElementToDisappear(SRP_LOADING);
    }

    @Override
    public boolean isAdvancedFilterApplied() {
        driverTools.scrollElementToCenter(ADVANCED_FILTER_ACTIVE);

        return driverTools.waitForElementToPresence(ADVANCED_FILTER_ACTIVE).isPresent();
    }

    @Override
    public void clickChangeSearchCriteria() {
        driverTools.click(CHANGE_SEARCH_CRITERIA_BUTTON);
    }

    @Override
    public void clickHotelLocationSearchField() {
        driverTools.click(CHANGE_SEARCH_LOCATION_INPUT);
    }

    @Override
    public void clickSearchOnSRP() {
        driverTools.click(SEARCH_BUTTON);
    }

    public boolean isMapVisible(){
        driverTools.waitForElementToAppear(SRP_MAP,5);
        return driverTools.isElementDisplayed(SRP_MAP);
    }

    public boolean isHotelCardVisible(){
        driverTools.waitForElementToAppear(HOTEL_CARD,5);
        return driverTools.isElementDisplayed(HOTEL_CARD);
    }

    public void clickRoomAndGuestButton(){
        driverTools.click(CHANGE_GUEST_INPUT);
        driverTools.waitForElementsToAppear(ADULT_INC_BUTTON);
    }

    public boolean isHotelImageVisible(){
        driverTools.waitForElementToAppear(HOTEL_IMAGE_CARD,5);
        return driverTools.isElementDisplayed(HOTEL_IMAGE_CARD);
    }
    public boolean isHotelBenefitsVisible(){
        driverTools.waitForElementToAppear(HOTEL_CARD_BENEFITS,5);
        return driverTools.isElementDisplayed(HOTEL_CARD_BENEFITS);
    }

    public void selectHotelCard() {
        driverTools.clickWithJavascriptExecutor(HOTEL_CARD);
    }

    public String getLocationNameFromInput() {
        driverTools.waitForElementToAppear(CURRENT_SEARCH_LOCATION_SRP).orElseThrow();
        return driverTools.getText(CURRENT_SEARCH_LOCATION_SRP).orElseThrow();
    }

    public String getHotelName() {
        driverTools.waitForElementToAppear(HOTEL_NAME).orElseThrow();
        return driverTools.getText(HOTEL_NAME).orElseThrow();
    }

    public String getHotelPrice() {
        driverTools.waitForElementToAppear(PRICE_TAG).orElseThrow();
        return driverTools.getText(PRICE_TAG).orElseThrow();
    }

    public String[] getHotelLocation() {
        driverTools.waitForElementToAppear(HOTEL_LOCATION).orElseThrow();
        String text = driverTools.getText(HOTEL_LOCATION).orElseThrow();
        String[] locations = text.split(",");
        return locations;
    }

    public String getRoomAndGuestInfoFromInput() {
        driverTools.waitForElementToAppear(CURRENT_NO_OF_ROOM).orElseThrow();
        String roomsAndGuest = driverTools.getText(CURRENT_NO_OF_ROOM).orElseThrow();
        roomsAndGuest += ", " + driverTools.getText(CURRENT_NO_OF_GUEST).orElseThrow();
        return roomsAndGuest;
    }

    public void updateStayLocation(String location){
        driverTools.click(CHANGE_SEARCH_BUTTON);
        driverTools.waitForElementsToAppear(CHANGE_DESTINATION_INPUT);
        driverTools.click(CHANGE_DESTINATION_INPUT);
        driverTools.waitForElementsToAppear(SEARCH_BAR).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEARCH_BAR);
        driverTools.sendKeys(SEARCH_BAR, location);
        driverTools.waitForElementToAppear(getSearchFormResultLocator(location)).orElseThrow();
        driverTools.click(getSearchFormResultLocator(location));
    }

    public void setStayDate(String type, String date){
        int[] startAndEndDate = CoreFunctions.setStayDateRange(date);
        int startDate = startAndEndDate[0];
        int endDate = startAndEndDate[1];

        logger.log("stringStartDate is '" + startDate + "', stringEndDate is '" + endDate + "'");

        final Locator SELECTED_START_DATE = getDateElement("SELECTED_START_DATE", startDate);
        final Locator SELECTED_END_DATE = getDateElement("SELECTED_END_DATE", startDate, endDate);

        driverTools.click(CHANGE_DATE_INPUT);

        driverTools.waitForElementsToAppear(SELECTED_START_DATE);
        driverTools.clickWithJavascriptExecutor(SELECTED_START_DATE);
        driverTools.clickWithJavascriptExecutor(SELECTED_END_DATE);
        driverTools.waitForElementToBeClickable(CONFIRM_DATE_CHANGE);
        driverTools.click(CONFIRM_DATE_CHANGE);
    }

    public void clickDone(){
        driverTools.click(DONE_CHANGE_GUEST);
    }

    public void openPopularFilter() {
        driverTools.swipeLeftUntilFound(MAIN_FILTER_CONTAINER, POPULAR_FILTER, 5);
        driverTools.click(POPULAR_FILTER);
        driverTools.waitForElementsToAppear(POPULAR_FILTER_BOTTOMSHEET);
    }

    public boolean selectPopularFilterChips() {
        List<WebElement> chips = driverTools.elements(POPULAR_FILTER_CHIPS).orElse(null);

        if (chips == null || chips.isEmpty()) {
            logger.log("No popular filter chips found");
            return false;
        }

        int totalChips = chips.size();
        logger.log("Clicking " + totalChips + " chips");

        for (int i = 0; i < totalChips; i++) {
            try {
                chips = driverTools.elements(POPULAR_FILTER_CHIPS).orElse(null);
                if (chips != null && i < chips.size()) {
                    WebElement chip = chips.get(i);
                    driverTools.click(chip);

                    // Quick verify
                    List<WebElement> activeChips = driverTools.elements(POPULAR_FILTER_CHIPS_ACTIVE).orElse(null);
                    if (activeChips != null && activeChips.size() >= (i + 1)) {
                        logger.log("Chip " + (i + 1) + " active");
                    }
                }
            } catch (Exception e) {
                logger.log("Failed chip " + (i + 1) + ": " + e.getMessage());
            }
        }

        logger.log("Done - clicked all " + totalChips + " chips");
        return true;
    }

    public boolean deselectAllChipsExceptFirst() {
        List<WebElement> activeChips = driverTools.elements(POPULAR_FILTER_CHIPS_ACTIVE).orElse(null);

        if (activeChips == null || activeChips.isEmpty()) {
            logger.log("No active chips found");
            return false;
        }

        int totalActiveChips = activeChips.size();

        if (totalActiveChips <= 1) {
            logger.log("Only 1 active chip, nothing to deselect");
            return false;
        }

        logger.log("Deselecting " + (totalActiveChips - 1) + " chips");

        // Start from last, work backwards
        for (int i = totalActiveChips - 1; i >= 1; i--) {
            try {
                activeChips = driverTools.elements(POPULAR_FILTER_CHIPS_ACTIVE).orElse(null);

                if (activeChips != null && i < activeChips.size()) {
                    WebElement activeChip = activeChips.get(i);
                    driverTools.click(activeChip);

                    // Quick verify
                    List<WebElement> remaining = driverTools.elements(POPULAR_FILTER_CHIPS_ACTIVE).orElse(null);
                    logger.log("Deselected chip " + (i + 1) + ", remaining: " + (remaining != null ? remaining.size() : 0));
                }
            } catch (Exception e) {
                logger.log("Failed to deselect chip " + (i + 1) + ": " + e.getMessage());
            }
        }

        // Final check
        List<WebElement> finalActive = driverTools.elements(POPULAR_FILTER_CHIPS_ACTIVE).orElse(null);
        logger.log("Done - final active: " + (finalActive != null ? finalActive.size() : 0) + " (expected: 1)");

        return true;
    }

    public boolean clickResetButton() {
        logger.log("Clicking reset button to deselect all chips");

        // Wait for reset button to appear and click it
        driverTools.waitForElementToAppear(POPULAR_FILTER_RESET_BUTTON, 5);
        driverTools.click(POPULAR_FILTER_RESET_BUTTON);

        // Wait for chips to be deselected
        driverTools.waitForElementsToAppear(POPULAR_FILTER_CHIPS, 2);

        logger.log("Reset button clicked successfully");
        return true;
    }

    public boolean verifyAllChipsUnselected() {
        logger.log("Verifying all chips are unselected");

        // Wait a moment for the UI to update
        driverTools.waitForElementsToAppear(POPULAR_FILTER_CHIPS, 2);

        // Check if any active chips exist
        List<WebElement> activeChips = driverTools.elements(POPULAR_FILTER_CHIPS_ACTIVE).orElse(null);
        int activeCount = (activeChips != null) ? activeChips.size() : 0;

        logger.log("Active chips count: " + activeCount + " (expected: 0)");

        if (activeCount == 0) {
            logger.log("Verification passed: All chips are unselected");
            return true;
        } else {
            logger.log("Verification failed: " + activeCount + " chip(s) still active");
            return false;
        }
    }

    public boolean clickSaveButton() {
        logger.log("Clicking save button to close bottom sheet");

        // Wait for save button to appear and click it
        driverTools.waitForElementToAppear(POPULAR_FILTER_SAVE_BUTTON, 5);
        driverTools.click(POPULAR_FILTER_SAVE_BUTTON);

        logger.log("Save button clicked successfully");
        return true;
    }

    public boolean verifyBottomSheetClosed() {
        logger.log("Verifying bottom sheet is closed");

        // Wait for the bottom sheet to disappear
        driverTools.waitForWebToLoad();

        // Check if bottom sheet is no longer visible
        List<WebElement> bottomSheet = driverTools.elements(POPULAR_FILTER_BOTTOMSHEET).orElse(null);
        boolean isClosed = (bottomSheet == null || bottomSheet.isEmpty());

        if (isClosed) {
            logger.log("Verification passed: Bottom sheet is closed");
            return true;
        } else {
            logger.log("Verification failed: Bottom sheet is still visible");
            return false;
        }
    }

    public void changeLocation(String loc){
        driverTools.waitForElementToAppear(CHANGE_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CHANGE_BUTTON);
        driverTools.waitForElementToAppear(CHANGE_LOC).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CHANGE_LOC);
        driverTools.waitForElementToAppear(CHANGE_LOC_SEARCHBAR).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CHANGE_LOC_SEARCHBAR);
        driverTools.sendKeys(CHANGE_LOC_SEARCHBAR,loc);
        WebElement searchFormResult = driverTools.waitForElementToAppear(getSearchLocator(loc),10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(searchFormResult);
        driverTools.waitForElementToAppear(LETS_SEARCH_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(LETS_SEARCH_BUTTON);
    }

    private Locator getSearchLocator(String location) {
        driverTools.waitForWebToLoad();
        return new Locator("SEARCH_FORM_RESULT", new By.ByXPath("(//p[@data-testid='destination-name']//span[normalize-space()='"+location+"'])[1]"));
    }

    @Override
    public String getLocationHeaderLabel() {
        driverTools.waitForElementToAppear(SEARCH_LOCATION_HEADER_LABEL_MWEB).orElseThrow();
        return driverTools.getText(SEARCH_LOCATION_HEADER_LABEL_MWEB).orElseThrow();
    }

    @Override
    public String getDateHeaderLabel() {
        driverTools.waitForElementToAppear(SEARCH_DATE_HEADER_LABEL_MWEB).orElseThrow();
        return driverTools.getText(SEARCH_DATE_HEADER_LABEL_MWEB).orElseThrow();
    }

    @Override
    public String getGuestHeaderLabel() {
        driverTools.waitForElementToAppear(SEARCH_GUEST_HEADER_LABEL_MWEB).orElseThrow();
        return driverTools.getText(SEARCH_GUEST_HEADER_LABEL_MWEB).orElseThrow();
    }
}