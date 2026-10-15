package com.tiket.web.page.accommodation.hotels.SRP;

import com.tiket.appium.PopupObserver;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;


public class SRPPageDWeb<T extends WebDriver> extends SRPPageWeb<T> {

    private static final Locator HOTEL_CARD = new Locator("HOTEL_CARD",new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]"));
    private static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]//h3"));
    private static final Locator POPUP_GOT_IT = new Locator("POPUP_GOT_IT", new By.ByXPath("//button[contains(@class, 'Coachmark_coachmark_done_btn')]"));
    private static final Locator PRICE_TAG = new Locator("PRICE_TAG", new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]//div[contains(@class,'PriceModule_final')]"));
    private static final Locator HOTEL_LOCATION = new Locator("HOTEL_LOCATION", new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]//div[contains(@class,'address')]//span"));
    private static final Locator HOTEL_INPUT_LOCATION = new Locator("HOTEL_LOCATION", new By.ByXPath("(//span[contains(@class, 'index_desktop')])[1]"));
    private static final Locator HOTEL_CALENDAR = new Locator("HOTEL_CALENDAR", new By.ByXPath("(//span[contains(@class, 'index_desktop')])[2]"));
    private static final Locator HOTEL_ROOM_N_GUEST = new Locator("HOTEL_ROOM_N_GUEST", new By.ByXPath("(//span[contains(@class, 'index_desktop_text_input')])[3]"));
    private static final Locator HOTEL_IMAGE_CARD = new Locator("HOTEL_IMAGE_CARD", new By.ByXPath("((//a[contains(@class, 'ProductCard_container')])[1]//img)[1]"));
    private static final Locator SRP_MAP = new Locator("SRP_MAP", new By.ByXPath("(//div[contains(@class,'map')])[3]"));
    private static final Locator HOTEL_CARD_BENEFITS = new Locator("HOTEL_CARD_BENEFITS", new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]//div[contains(@class,'Benefits')]"));
    private static final Locator DONE_BUTTON = new Locator("DONE_BUTTON", new By.ByXPath("//div[contains(@class, 'Modal_modal_footer_button_group')]//button"));
    private static final Locator SEARCH_BAR = new Locator("SEARCH_BAR", new By.ByXPath("//div[contains(@class, 'SearchBox_placeholder')]//input"));
    private static final Locator ADULT_INC_BUTTON = new Locator("ADULT_INC_BUTTON", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[4]"));
    private static final Locator ADVANCED_FILTER_ACTIVE = new Locator("ADVANCED_FILTER_ACTIVE", new By.ByXPath("//button[contains(@class, 'Chip_chip')]//span[contains(text(), 'Filter')]/following-sibling::*//*[contains(@class, 'Notif_color_green')]"));
    private static final Locator QUICK_FILTER_SORT_ACTIVE = new Locator("QUICK_FILTER_SORT_ACTIVE", new By.ByXPath("//button[contains(@class, 'Chip_chip')]//span[contains(text(), 'Sort') or contains(text(), 'Urutkan')]/following-sibling::*//*[contains(@class, 'Notif_color_green')]"));
    private static final Locator QUICK_FILTER_STAR_RATING_ACTIVE = new Locator("QUICK_FILTER_STAR_RATING_ACTIVE", new By.ByXPath("//button[contains(@class, 'Chip_chip')]//span[contains(text(), 'Star') or contains(text(), 'Bintang')]/following-sibling::*//*[contains(@class, 'Notif_color_green')]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[@data-testid='search-button-desktop']"));

    public SRPPageDWeb(Session<T> session) {
        super(session);
    }

    @Override
    public void setPriceTaxFilter(boolean isTotalPrice, boolean isAfterTax) {
        if (isTotalPrice) clickTotalPriceToggle();
        if (isAfterTax) clickIncludeTaxToggle();
    }

    private void clickTotalPriceToggle() {
        driverTools.waitForElementToAppear(HOTEL_NAME_SRP);
        driverTools.scrollUntilElementFound(TOTAL_PRICE_TOGGLE);
        driverTools.click(TOTAL_PRICE_TOGGLE);
        driverTools.waitForElementToAppear(TOTAL_PRICE_TOGGLE_DISABLED);
        driverTools.waitForElementToDisappear(TOTAL_PRICE_TOGGLE_DISABLED);
        driverTools.waitForElementToDisappear(SRP_LOADING);
        driverTools.waitForElementToAppear(HOTEL_NAME_SRP);
    }

    private void clickIncludeTaxToggle() {
        driverTools.waitForElementToAppear(HOTEL_NAME_SRP);
        driverTools.scrollUntilElementFound(TOTAL_PRICE_TOGGLE);
        driverTools.click(AFTER_TAX_TOGGLE);
        driverTools.waitForElementToAppear(AFTER_TAX_TOGGLE_DISABLED);
        driverTools.waitForElementToDisappear(AFTER_TAX_TOGGLE_DISABLED);
        driverTools.waitForElementToDisappear(SRP_LOADING);
        driverTools.waitForElementToAppear(HOTEL_NAME_SRP);
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
        // TODO fix later to cover dweb
    }

    @Override
    public void clickHotelLocationSearchField() {
        // TODO fix later to cover dweb
    }

    @Override
    public void clickSearchOnSRP() {
        driverTools.click(SEARCH_BUTTON);
    }

    public void clickPopUp() {
        PopupObserver.inject(driver, List.of(POPUP_GOT_IT));
    }

    public boolean isMapVisible(){
        driverTools.waitForElementToAppear(SRP_MAP,5);
        return driverTools.isElementDisplayed(SRP_MAP);
    }

    public boolean isHotelCardVisible(){
        driverTools.waitForElementToAppear(HOTEL_CARD,8);
        if (!driverTools.isElementDisplayed(HOTEL_CARD)){
            driverTools.scrollUntilElementFound(HOTEL_CARD);
        }
        return driverTools.isElementDisplayed(HOTEL_CARD);
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
        driverTools.waitForElementToAppear(HOTEL_INPUT_LOCATION).orElseThrow();
        return driverTools.getText(HOTEL_INPUT_LOCATION).orElseThrow();
    }

    public void clickRoomAndGuestButton(){
        driverTools.click(HOTEL_ROOM_N_GUEST);
        driverTools.waitForElementsToAppear(ADULT_INC_BUTTON);
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
        driverTools.waitForElementToAppear(HOTEL_ROOM_N_GUEST).orElseThrow();
        return driverTools.getText(HOTEL_ROOM_N_GUEST).orElseThrow();
    }

    public void updateStayLocation(String location){
        driverTools.click(HOTEL_INPUT_LOCATION);
        driverTools.waitForElementToAppear(SEARCH_BAR).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEARCH_BAR);
        driverTools.sendKeys(SEARCH_BAR, location);
        WebElement searchFormResult = driverTools.waitForElementToAppear(getSearchFormResultLocator(location)).orElseThrow();
        driverTools.click(searchFormResult);
    }


    public void setStayDate(String type, String date){
        int[] startAndEndDate = CoreFunctions.setStayDateRange(date);
        int startDate = startAndEndDate[0];
        int endDate = startAndEndDate[1];

        logger.log("stringStartDate is '" + startDate + "', stringEndDate is '" + endDate + "'");

        final Locator SELECTED_START_DATE = getDateElement("SELECTED_START_DATE", startDate);
        final Locator SELECTED_END_DATE = getDateElement("SELECTED_END_DATE", startDate, endDate);

        driverTools.click(HOTEL_CALENDAR);

        driverTools.waitForElementsToAppear(SELECTED_START_DATE);
        driverTools.clickWithJavascriptExecutor(SELECTED_START_DATE);
        driverTools.clickWithJavascriptExecutor(SELECTED_END_DATE);
    }

    public void changeLocation(String loc){
        return; //fix me later
    }

    public void clickDone(){
        driverTools.click(DONE_BUTTON);
    }

    @Override
    public void openPopularFilter() {

    }

    public boolean isFlexibleStayVisible(){
        return driverTools.isElementDisplayed(HotelSearchResultPage.FLEXIBLE_STAY_CARD);
    }

    @Override
    public String getLocationHeaderLabel() {
        return null;
    }

    @Override
    public String getDateHeaderLabel() {
        return null;
    }

    @Override
    public String getGuestHeaderLabel() {
        return null;
    }
}


