package com.tiket.web.page.accommodation.hotels.SRP;

import com.tiket.appium.PopupObserver;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.testng.Assert.assertEquals;

public class HotelSearchResultPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator HOTEL_CARD = new Locator("HOTEL_CARD",new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]"));
    private static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]//h3"));
    private static final Locator POPUP_GOT_IT = new Locator("POPUP_GOT_IT", new By.ByXPath("//button[contains(@class, 'Coachmark_coachmark_done_btn')]"));
    private static final Locator PRICE_TAG = new Locator("PRICE_TAG", new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]//div[contains(@class,'PriceModule_final')]"));
    private static final Locator HOTEL_LOCATION = new Locator("HOTEL_LOCATION", new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]//div[contains(@class,'address')]//span"));
    private static final Locator HOTEL_INPUT_LOCATION = new Locator("HOTEL_LOCATION", new By.ByXPath("(//span[contains(@class, 'index_desktop')])[1]"));
    private static final Locator HOTEL_CALENDAR = new Locator("HOTEL_CALENDAR", new By.ByXPath("(//span[contains(@class, 'index_desktop')])[2]"));
    private static final Locator HOTEL_ROOM_N_GUEST = new Locator("HOTEL_ROOM_N_GUEST", new By.ByXPath("(//span[contains(@class, 'index_desktop')])[3]"));
    private static final Locator HOTEL_IMAGE_CARD = new Locator("HOTEL_IMAGE_CARD", new By.ByXPath("((//a[contains(@class, 'ProductCard_container')])[1]//img)[1]"));
    private static final Locator SRP_MAP = new Locator("SRP_MAP", new By.ByXPath("//*[contains(@class,'main_map_filter_button')]"));
    private static final Locator HOTEL_CARD_BENEFITS = new Locator("HOTEL_CARD_BENEFITS", new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]//div[contains(@class,'Benefits')]"));
    private static final Locator ROOM_INC_BUTTON = new Locator("ROOM_INC_BUTTON", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[2]"));
    private static final Locator ROOM_DEC_BUTTON = new Locator("ROOM_DEC_BUTTON", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[1]"));
    private static final Locator ADULT_INC_BUTTON = new Locator("ADULT_INC_BUTTON", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[4]"));
    private static final Locator ADULT_DEC_BUTTON = new Locator("ADULT_DEC_BUTTON", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[3]"));
    private static final Locator DONE_BUTTON = new Locator("DONE_BUTTON", new By.ByXPath("//div[contains(@class, 'Modal_modal_footer_button_group')]//button"));
    private static final Locator SEARCH_BAR = new Locator("SEARCH_BAR", new By.ByXPath("//div[contains(@class, 'SearchBox_placeholder')]//input"));
    private static final Locator MASTER_TAG = new Locator("MASTER_TAG", new By.ByXPath("//div[contains(@class, 'MasterTagFilter_chips_wrapper')]"));
    private static final Locator CURRENT_ROOM_AMOUNT = new Locator("MASTER_TAG", new By.ByXPath("(//*[contains(@class,'QuantityEditor_quantity_input')])[1]"));
    private static final Locator CURRENT_ADULT_AMOUNT = new Locator("MASTER_TAG", new By.ByXPath("(//*[contains(@class,'QuantityEditor_quantity_input')])[2]"));
    private static final Locator STAR_FILTER_BUTTON = new Locator("STAR_FILTER_BUTTON", AppiumBy.xpath("//*[contains(@class,'desktop_chip_wrappe')]//*[contains(text(),'Bintang') or contains(text(),'Star')]"));
    private static final Locator SORT_BUTTON = new Locator("SORT_BUTTON", AppiumBy.xpath("//*[contains(@class,'desktop_chip_wrappe')]//*[contains(text(),'Urutkan') or contains(text(),'Sort')]"));
    private static final Locator STAR_OPTIONS = new Locator("STAR_OPTIONS", AppiumBy.xpath("//*[contains(@class,'StarFilter_chip_options')]"));
    private static final Locator SAVE_STAR_FILTER = new Locator("SAVE_STAR_FILTER", AppiumBy.xpath("//*[contains(@class,'PopupStarFilter')]//*[contains(@class,'button')]"));
    private static final Locator MEETS_TRAVEL_POLICY_NUDGE = new Locator("MEETS_TRAVEL_POLICY_NUDGE", AppiumBy.xpath("//*[contains(@class,'PropertyLabel') and text()='Meets Travel Policy' or text()='Sesuai Kebijakan']"));
    private static final Locator SEARCH_NOT_FOUND_IMAGE = new Locator("SEARCH_NOT_FOUND_IMAGE", AppiumBy.xpath("//*[contains(@class,'NoSearchResult_not_found_img')]"));
    private static final Locator HOTEL_PRICE = new Locator("HOTEL_PRICE", AppiumBy.xpath("//*[contains(@class,'PriceModule_final_price_wrapper')]//*[text() and contains(@class,'align_right')]"));
    private static final Locator HOTEL_PRICE_TAX = new Locator("HOTEL_PRICE_TAX", AppiumBy.xpath("//*[contains(@class,'ProductCard_price_tax')]/span"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[@data-testid='search-button-desktop']"));
    public static final Locator FLEXIBLE_STAY_CARD = new Locator("FLEXIBLE_STAY_CARD", new By.ByXPath("//div[contains(@class, 'InterceptSoldOut_intercept_sold_out')]//section[contains(@class,'Layout_icon')]"));
    private static final Locator FLEXIBLE_STAY_HEADING = new Locator("FLEXIBLE_STAY_HEADING", new By.ByXPath("//*[contains(@class,'Layout_icon')]/following-sibling::*//*[contains(@class,'Layout_title') and (contains(.,'flexible') or contains(.,'fleksibel'))]"));
    private static final Locator FLEXIBLE_STAY_IMAGE = new Locator("FLEXIBLE_STAY_IMAGE", new By.ByXPath("(//div[contains(@class, 'InterceptSoldOut_intercept_sold_out')]//a//img)[1]"));
    private static final Locator FLEXIBLE_STAY_HOTEL_NAME = new Locator("FLEXIBLE_STAY_HOTEL_HEADING", new By.ByXPath("//div[contains(@class, 'InterceptSoldOut_layout_container')]//*[contains(@class,'SmallProductCard_hotel_name')]"));
    private static final Locator FLEXIBLE_STAY_TEXT = new Locator("FLEXIBLE_STAY_TEXT", new By.ByXPath(" //div[contains(@class, 'InterceptSoldOut_intercept_sold_out')]//a//*[contains(@class, 'sold')]"));
    private static final Locator FLEXIBLE_STAY_HOTEL_CARDS = new Locator("FLEXIBLE_STAY_HOTEL_CARDS", new By.ByXPath("//div[contains(@class, 'InterceptSoldOut_layout_container')]//*[contains(@class,'SmallProductCard_hotel_name')]"));
    private static final Locator FLEXIBLE_STAY_HOTEL_PRICE = new Locator("FLEXIBLE_STAY_HOTEL_PRICE", new By.ByXPath("//div[contains(@class, 'InterceptSoldOut_intercept_sold_out')]//*[contains(@class,'PriceModule_final') or contains(@class,'SmallProductCard_final_price')]"));
    private static final Locator HOTEL_TAG = new Locator("HOTEL_CARD", new By.ByXPath("(//a[contains(@class, 'FullProductCard')])"));
    private static final Locator LOCATION=new Locator("LOCATION",new By.ByXPath("(//span[contains(@class, 'index_desktop_with_ellipsis') and contains(@class, 'Text_weight_bold') and contains(@class, 'Text_variant_highEmphasis')])[1]"));
    private static final Locator PRICE_FILTER_BUTTON = new Locator("PRICE_FILTER_BUTTON", new By.ByXPath("(//button[contains(@class, 'Chip_chip') and .//span[contains(@class, 'Chip_icon_right')]])[2]"));
    private static final Locator CURRENCY_PRICE_FILTER(String currency){
        return new Locator("CURRENCY_PRICE_FILTER", By.xpath(String.format("//span[contains(@class, 'Chip_text') and contains(text(), '%s')]",currency)));
    }
    private static final Locator PRICE_FILTER_SAVE = new Locator("PRICE_FILTER_SAVE", new By.ByXPath("//button[contains(@class, 'Button_button') and contains(@class, 'Button_variant_primary')]"));
    private static final Locator SORT_BY_BUTTON = new Locator("SORT_BY_BUTTON", new By.ByXPath("(//button//span[contains(@class, 'mobile_chip_text_wrapper__')])[2]"));
    private static final Locator SORT_BY_LOWEST_BUTTON = new Locator("SORT_BY_LOWEST_BUTTON", new By.ByXPath("(//label[contains(@class, 'Sort_option_item') and contains(@class, 'Radio_radio')])[4]"));
    private static final Locator STAR_BUTTON = new Locator("STAR_BUTTON", new By.ByXPath("(//button[contains(@class, 'Chip_chip') and contains(@class, 'Chip_size_default')])[6]"));
    private static final Locator STAR_RATINGS = new Locator("STAR_RATINGS", new By.ByXPath("(//a[contains(@class, 'FullProductCard_container')])[1]//*[local-name()='svg' and contains(@data-testid, 'product-card-star-rating-full')]"));
    private static final Locator STAR_5 = new Locator("STAR_5", new By.ByXPath("//button[.//span[text()='5']]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[contains(@class, 'Button_variant_primary__')]"));
    private static final Locator NEARBY_LEPUS_HEADING = new Locator("NEARBY_LEPUS_HEADING", new By.ByXPath("//*[contains(@class,'LepusNearbyHotels_lepus_nearby_hotels')]//*[contains(@class,'Layout_title')]"));
    private static final Locator NEARBY_LEPUS_HOTEL_NAME = new Locator("NEARBY_LEPUS_HOTEL_NAME", new By.ByXPath("//*[contains(@class,'LepusNearbyHotels_lepus_nearby_hotels')]//*[contains(@class,'SmallProductCard_hotel_name')]"));
    private static final Locator NEARBY_LEPUS_STAR_RATING = new Locator("NEARBY_LEPUS_STAR_RATING", new By.ByXPath("//*[contains(@class,'LepusNearbyHotels_lepus_nearby_hotels')]//*[contains(@class,'SmallProductCard_star_rating_container')]"));
    private static final Locator NEARBY_LEPUS_FIRST_CARD = new Locator("NEARBY_LEPUS_FIRST_CARD", new By.ByXPath("(//*[contains(@class,'LepusNearbyHotels_lepus_nearby_hotels')]//a)[1]"));
    private static final Locator NEARBY_LEPUS_FIRST_HOTEL_NAME = new Locator("NEARBY_LEPUS_FIRST_HOTEL_NAME", new By.ByXPath("(//*[contains(@class,'LepusNearbyHotels_lepus_nearby_hotels')]//*[contains(@class,'SmallProductCard_hotel_name')])[1]"));
    private static final Locator SAVED_TO_WISHLIST_POPUP = new Locator("SAVED_TO_WISHLIST_POPUP", new By.ByXPath("//div[contains(@class,'Snackbar_text_content') and (contains(text(),'Saved to All Wishlist') or contains(text(),'Disimpan ke Semua Wishlist'))]"));

    public HotelSearchResultPage(Session<T> session) {
        super(session);
    }

    public void clickPopUp() {
        PopupObserver.inject(driver, List.of(POPUP_GOT_IT));
    }

    public boolean isMapVisible(){
        driverTools.waitForElementToAppear(SRP_MAP,5);
        return driverTools.isElementDisplayed(SRP_MAP);
    }

    public boolean isHotelCardVisible(){
        driverTools.waitForElementToAppear(HOTEL_CARD,15);
        return driverTools.isElementDisplayed(HOTEL_CARD);
    }

    public boolean isMasterTagVisible(){
        driverTools.waitForElementToAppear(MASTER_TAG,5);
        return driverTools.isElementDisplayed(MASTER_TAG);
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

    public String getStayDateFromInput() {
        driverTools.waitForElementToAppear(HOTEL_CALENDAR).orElseThrow();
        return driverTools.getText(HOTEL_CALENDAR).orElseThrow();
    }

    public void UpdateStayLocation(String location){
        driverTools.click(HOTEL_INPUT_LOCATION);
        driverTools.waitForElementToAppear(SEARCH_BAR).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEARCH_BAR);
        driverTools.sendKeys(SEARCH_BAR, location);
        WebElement searchFormResult = driverTools.waitForElementToAppear(getSearchFormResultLocator(location)).orElseThrow();
        driverTools.click(searchFormResult);
    }

    private Locator getSearchFormResultLocator(String location){
        return new Locator("SEARCH_FORM_RESULT", new By.ByXPath("//*[@data-testid='destination-name']//*[text()='"+location+"']"));
    }

    public void setStayDate(String type, String date){
        String[] stayDate = date.split("-");

        if(stayDate[0].equals("0")){
            return;
        }

        int startDate = Integer.parseInt(stayDate[0]);
        int endDate = Integer.parseInt(stayDate[1]);

        if (startDate <= 0
                || startDate > 31
                || startDate > endDate
                || endDate > 31
                || endDate <= 0) {
            throw new DriverToolsException("Test data 'date' with value '" + date + "' is not valid, please check again");
        }

        if (type.equals("range date")) {
            int randomStartDate = generateRandomInt(startDate, endDate - 1);
            int randomEndDate = generateRandomInt(randomStartDate + 1, endDate);

            startDate = randomStartDate;
            endDate = randomEndDate;
        }

        logger.log("stringStartDate is '" + startDate + "', stringEndDate is '" + endDate + "'");

        final Locator SELECTED_START_DATE = getDateElement("SELECTED_START_DATE", startDate);
        final Locator SELECTED_END_DATE = getDateElement("SELECTED_END_DATE", startDate, endDate);

        driverTools.click(HOTEL_CALENDAR);

        driverTools.clickWithJavascriptExecutor(SELECTED_START_DATE);
        driverTools.clickWithJavascriptExecutor(SELECTED_END_DATE);
    }

    private Locator getDateElement(String elementName, int date) {
        return new Locator(elementName, AppiumBy.xpath("(//*[not(contains(@class,'disabled')) and contains(@class,'Day_day_number') and text()='"+date+"'])[1]"));
    }

    private Locator getDateElement(String elementName, int startDate, int endDate) {
        return new Locator(elementName, AppiumBy.xpath("//*[not(contains(@class,'disabled')) and contains(@class,'Day_day_number') and text()='"+startDate+"']//following::*//*[contains(@class,'Day_day_number') and text()='"+endDate+"']"));
    }

    public void clickRoomAndGuestButton(){
        driverTools.click(HOTEL_ROOM_N_GUEST);
    }

    public void setTotalRoom(int room){
        int currentRoom = Integer.parseInt(driverTools.getValueAttribute(CURRENT_ROOM_AMOUNT,"value"));
        logger.log("currentRoom is '" + currentRoom + "'");
        if(room > currentRoom){
            for(int i = 0; i < room - currentRoom; i++){
                driverTools.clickWithJavascriptExecutor(ROOM_INC_BUTTON);
            }
        } else if(room < currentRoom){
            for(int i = 0; i < currentRoom - room; i++){
                driverTools.clickWithJavascriptExecutor(ROOM_DEC_BUTTON);
            }
        }
    }

    public void setTotalGuest(int adult){
        int currentAdult = Integer.parseInt(driverTools.getValueAttribute(CURRENT_ADULT_AMOUNT,"value"));
        logger.log("currentAdult is '" + currentAdult + "'");
        if(adult > currentAdult){
            for(int i = 0; i < adult - currentAdult; i++){
                driverTools.clickWithJavascriptExecutor(ADULT_INC_BUTTON);
            }
        } else if(adult < currentAdult){
            for(int i = 0; i < currentAdult - adult; i++){
                driverTools.clickWithJavascriptExecutor(ADULT_DEC_BUTTON);
            }
        }
    }

    public void clickDone(){
        driverTools.click(DONE_BUTTON);
    }

    public void selectFilterStarRange(String starRange) {
        driverTools.click(STAR_FILTER_BUTTON);
        driverTools.waitForElementToAppear(STAR_OPTIONS);
        if (starRange.contains("-")) {
            String[] starRangeSplit = starRange.split("-");
            for (int i = 0; i < starRangeSplit.length; i++) {
                int star = Integer.parseInt(starRangeSplit[i]);
                driverTools.click(getStarLocator(star));
            }
        } else {
            driverTools.click(getStarLocator(Integer.parseInt(starRange)));
        }
        driverTools.click(SAVE_STAR_FILTER);
    }

    private Locator getStarLocator(int star) {
        return new Locator("STAR_OPTIONS_" + star, AppiumBy.xpath("//*[contains(@class,'PopupStarFilter')]//*[text()='" + star + "']"));
    }

    public void selectHotelWithMeetTravelPolicyNudge() {
        driverTools.waitForElementToPresence(MEETS_TRAVEL_POLICY_NUDGE, 5);
        driverTools.clickWithJavascriptExecutor(MEETS_TRAVEL_POLICY_NUDGE);
    }

    public boolean checkEmptyHotelList() {
        return driverTools.waitForElementToPresence(SEARCH_NOT_FOUND_IMAGE).isPresent();
    }

    public boolean isMeetsTravelPolicyNudgeVisible() {
        return driverTools.waitForElementToPresence(MEETS_TRAVEL_POLICY_NUDGE).isPresent();
    }

    public void selectFilterSortBy(String sortTypeEn) {
        driverTools.click(SORT_BUTTON);
        String sortTypeId = "";
        switch (sortTypeEn) {
            case "Lowest price":
                sortTypeId = "Harga terendah";
                break;
            case "Highest price":
                sortTypeId = "Harga tertinggi";
                break;
            default:
                throw new NotImplementedException("not implemented sort type");
        }
        Locator sortOptionLocator = getSortOptionLocator(sortTypeEn, sortTypeId);
        driverTools.click(sortOptionLocator);
    }

    private Locator getSortOptionLocator(String sortTypeEn, String sortTypeId) {
        return new Locator("SORT_OPTIONS_" + sortTypeEn, AppiumBy.xpath("//*[contains(@class,'Radio_children') and (text()='" + sortTypeEn + "' or text()='" + sortTypeId + "')]"));
    }

    public boolean isHotelRangeMeetPolicyPriceRange(String priceRange) {
        driverTools.waitForElementToPresence(HOTEL_CARD, 60).orElseThrow();
        driverTools.scrollIntoWebView(HOTEL_CARD);
        driverTools.waitForWebToLoad();
        int numberOfCards = driverTools.elements(HOTEL_CARD).orElseThrow().size();
        logger.log("Number of hotel cards: " + numberOfCards);

        int numberOfHotelMeetPolicy = driverTools.elements(MEETS_TRAVEL_POLICY_NUDGE).orElseThrow().size();
        logger.log("Number of hotel cards meet policy: " + numberOfHotelMeetPolicy);

        int numberOfPrices = driverTools.elements(HOTEL_PRICE_TAX).orElseThrow().size();
        logger.log("Number of prices: " + numberOfPrices);

        List<String> prices = driverTools.getTexts(HOTEL_PRICE);

        if (driverTools.waitForElementToPresence(HOTEL_PRICE_TAX).isPresent()) {
            prices = driverTools.getTexts(HOTEL_PRICE_TAX);
        }

        for (String price : prices) {
            int lowerLimit = Integer.parseInt(priceRange.split("-")[0]);
            int upperLimit = Integer.parseInt(priceRange.split("-")[1]);
            logger.log("Price: " + price);
            logger.log("lowerLimit: " + lowerLimit);
            logger.log("upperLimit: " + upperLimit);
            int priceInt = (int) CoreFunctions.convertPriceSafely(price);
            logger.log("priceInt: " + priceInt);
            if (priceInt < lowerLimit || priceInt > upperLimit) {
                return false;
            }
        }

        assertEquals(numberOfHotelMeetPolicy, numberOfCards, "Hotel meet policy count doesn't match number of cards");
        assertEquals(numberOfPrices, numberOfCards, "Price count doesn't match number of cards");

        return true;
    }

    public void clickSearchOnSRP() {
        driverTools.click(SEARCH_BUTTON);
    }

    public void scrollToFlexibleStay() {
        driverTools.waitForElementToAppear(FLEXIBLE_STAY_CARD, 5);
        driverTools.scrollUntilElementFound(FLEXIBLE_STAY_CARD);
    }

    public boolean isFlexibleStayVisible() {
        return driverTools.isElementDisplayed(FLEXIBLE_STAY_CARD);
    }

    public boolean isFlexibleStayImageVisible() {
        driverTools.waitForElementToAppear(FLEXIBLE_STAY_IMAGE, 5);
        return driverTools.isElementDisplayed(FLEXIBLE_STAY_IMAGE);
    }

    public String getFlexibleStayHeading() {
        WebElement webElement = driverTools.waitForElementToAppear(FLEXIBLE_STAY_HEADING).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getFlexibleStayHotelName() {
        WebElement webElement = driverTools.waitForElementToAppear(FLEXIBLE_STAY_HOTEL_NAME).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getFlexibleStayText() {
        WebElement webElement = driverTools.waitForElementToAppear(FLEXIBLE_STAY_TEXT).orElseThrow();
        return driverTools.getText(webElement);
    }

    public void clickOnFlexibleStayCard() {
        driverTools.clickWithJavascriptExecutor(FLEXIBLE_STAY_HOTEL_NAME);
    }

    public void selectHotel() {
        driverTools.clickWithJavascriptExecutor(HOTEL_TAG);
    }

    public String getLocation(){
        WebElement webElement=driverTools.waitForElementToAppear(LOCATION).orElseThrow();
        return driverTools.getText(webElement);
    }

    public boolean rightHotel(String location){
        int index=1;
        do{
            Locator HOTEL_LOC=new Locator("HOTEL_LOCATION",new By.ByXPath("(//span[contains(@class, 'FullProductCard_hotel_address') and contains(@class, 'Text_size_b3')])"+"["+index+"]"));
            if(driverTools.isElementDisplayed(HOTEL_LOC)){
                String loc= String.valueOf(driverTools.getText(HOTEL_LOC));
                if(!loc.contains(location)) return false;
            }else{
                return true;
            }
            index++;
        }while(index!=3);
        return true;
    }

    public void clickPriceFilter() {
        driverTools.waitForElementToAppear(PRICE_FILTER_BUTTON);
        driverTools.clickWithJavascriptExecutor(PRICE_FILTER_BUTTON);
    }

    public Boolean checkPriceFilter(String currency) {
        driverTools.waitForElementToAppear(CURRENCY_PRICE_FILTER(currency));
        return driverTools.isElementDisplayed(CURRENCY_PRICE_FILTER(currency));
    }

    public void clickPriceFilterSave() {
        driverTools.waitForElementToAppear(PRICE_FILTER_SAVE);
        driverTools.clickWithJavascriptExecutor(PRICE_FILTER_SAVE);
    }

    public void clickSortButton() {
        driverTools.waitForElementToAppear(SORT_BY_BUTTON);
        driverTools.clickWithJavascriptExecutor(SORT_BY_BUTTON);
    }

    public void clickSortByLowest() {
        driverTools.waitForElementToAppear(SORT_BY_LOWEST_BUTTON);
        driverTools.clickWithJavascriptExecutor(SORT_BY_LOWEST_BUTTON);
    }

    public Boolean checkSorting() {
        Locator FIRST_VALUE = new Locator("FIRST_VALUE", new By.ByXPath("(//div[contains(@class, 'Text_variant_highEmphasis') and (contains(text(), 'SGD') or contains(text(), 'IDR'))])[1]"));
        Locator SECOND_VALUE = new Locator("SECOND_VALUE", new By.ByXPath("(//div[contains(@class, 'Text_variant_highEmphasis') and (contains(text(), 'SGD') or contains(text(), 'IDR'))])[2]"));

        WebElement webElementFirst = driverTools.waitForElementToAppear(FIRST_VALUE).orElseThrow();
        String firstText = driverTools.getText(webElementFirst);
        int firstNum = (int)CoreFunctions.convertPriceSafely(firstText);
        WebElement webElementSecond = driverTools.waitForElementToAppear(SECOND_VALUE).orElseThrow();
        String secondText = driverTools.getText(webElementSecond);
        int secondNum = (int) CoreFunctions.convertPriceSafely(secondText);
        return firstNum <= secondNum;
    }

    public int checkStars() {
        driverTools.waitForElementToAppear(STAR_RATINGS, 5);
        List<WebElement> stars = driver.findElements(STAR_RATINGS.by());
        return stars.size();
    }

    public void clickStarFilter() {
        driverTools.waitForElementToAppear(STAR_BUTTON);
        driverTools.clickWithJavascriptExecutor(STAR_BUTTON);
    }

    public void selectStar() {
        driverTools.waitForElementToAppear(STAR_5);
        driverTools.clickWithJavascriptExecutor(STAR_5);
    }

    public void clickStarSave() {
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public void moveToLastPage() throws InterruptedException {
        driverTools.back();
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.back();
    }

    public boolean isNearbyLepusVisible(String currency){
        WebElement nearbyLepusHeadingElement = driverTools.scrollUntilElementFound(NEARBY_LEPUS_HEADING, 500, 2, 40)
                .orElseThrow(() -> new RuntimeException("Nearby Lepus section not available on this page"));

        String nearbyLepusHeadingText = driverTools.getText(nearbyLepusHeadingElement);
        logger.log("Nearby Lepus Heading Text: " + nearbyLepusHeadingText);
        String expectedHeadingEn = "Hotels near your selected area";
        String expectedHeadingId = "Hotel di dekat lokasi pilihanmu";
        boolean isHeadingVisible = nearbyLepusHeadingText.equals(expectedHeadingEn) || nearbyLepusHeadingText.equals(expectedHeadingId);
        logger.log("isHeadingVisible: " + isHeadingVisible);
        Assertion.assertThat("Nearby Lepus heading is visible", nearbyLepusHeadingText, anyOf(containsString(expectedHeadingEn), containsString(expectedHeadingId)));

        //step 2: check number of hotels name in lepus card
        List<WebElement> lepusHotelNames = driverTools.elements(NEARBY_LEPUS_HOTEL_NAME).orElseThrow();
        int lepusHotelCount = lepusHotelNames.size();
        logger.log("Number of Nearby Lepus { "+ lepusHotelCount +" } Hotel Names: " + lepusHotelCount);
        Assertion.assertThat("There are hotel names in Nearby Lepus cards", lepusHotelCount, greaterThan(0));
        //step 3: check stars in lepus card
        List<WebElement> lepusStarRatings = driverTools.elements(NEARBY_LEPUS_STAR_RATING).orElseThrow();
        int lepusStarCount = lepusStarRatings.size();
        logger.log("Number of Nearby Lepus { "+ lepusStarCount +" } Star Ratings: " + lepusStarCount);
        Assertion.assertThat("There are star ratings in Nearby Lepus cards", lepusStarCount, greaterThan(0));
        Assertion.assertThat("Number of Nearby Lepus hotel names matches number of star ratings", lepusStarCount, equalTo(lepusHotelCount));

        //step 4: check price in lepus card
        List<WebElement> lepusPrices = driverTools.elements(getNearbyLepusPrice(currency)).orElseThrow();
        int lepusPriceCount = lepusPrices.size();
        logger.log("Number of Nearby Lepus { "+ lepusPriceCount +" } Prices: " + lepusPriceCount);
        Assertion.assertThat("There are prices in Nearby Lepus cards", lepusPriceCount, greaterThan(0));
        Assertion.assertThat("Number of Nearby Lepus prices matches number of hotel names", lepusPriceCount, equalTo(lepusHotelCount));
        return isHeadingVisible && lepusHotelCount > 0 && lepusStarCount > 0 && lepusPriceCount > 0;
    }

    private Locator getNearbyLepusPrice(String currency){
        return new Locator("NEARBY_LEPUS_PRICE",new By.ByXPath("//*[contains(@class,'LepusNearbyHotels_lepus_nearby_hotels')]//*[contains(@class,'SmallProductCard_final_price')]//*[contains(text(),'"+currency+"')]"));
    }

    /**
     * Clicks the first property card under the "Hotels near your selected area" (Nearby Lepus) section
     * on the SRP. Returns the displayed hotel name of that card so callers can correlate it with the
     * hotel that opens on the PDP.
     *
     * <p>Pre-condition: caller has already scrolled the section into view (e.g. via
     * {@link #isNearbyLepusVisible(String)}) so the lazy-loaded section is mounted in the DOM.</p>
     *
     * @return the hotel name shown on the clicked card
     * @throws RuntimeException if no Nearby Lepus card is present after the section was rendered
     */
    public String clickFirstNearbyLepusHotelCard() {
        WebElement firstHotelName = driverTools.waitForElementToPresence(NEARBY_LEPUS_FIRST_HOTEL_NAME, 10)
                .orElseThrow(() -> new RuntimeException("No hotel card available under 'Hotels near your selected area' section"));
        String hotelName = driverTools.getText(firstHotelName);
        logger.log("Selecting first nearby Lepus hotel card: " + hotelName);

        WebElement firstCard = driverTools.waitForElementToPresence(NEARBY_LEPUS_FIRST_CARD, 10)
                .orElseThrow(() -> new RuntimeException("Nearby Lepus card link not found"));
        driverTools.scrollIntoWebView(firstCard);
        driverTools.clickWithJavascriptExecutor(firstCard);
        return hotelName;
    }

    public void openHotelSrpViaDeeplink(String region, String locationId, String locationType, int room, int adult, String checkInDate, String checkOutDate) {
        String baseUrl = switch (Configs.ENVIRONMENT) {
            case GK -> "https://gatotkaca.tiket.com";
            case PREPROD -> "https://preprod.tiket.com";
            case PROD -> "https://www.tiket.com";
            default -> throw new NotImplementedException("Unsupported environment: " + Configs.ENVIRONMENT);
        };

        String deeplink = String.format(
                "%s/hotel/search?room=%d&adult=%d&id=%s&type=%s&q=%s&checkin=%s&checkout=%s",
                baseUrl, room, adult, locationId, locationType, region, checkInDate, checkOutDate
        );
        logger.log("Opening hotel SRP deeplink: " + deeplink);
        driverTools.getWithDynamicTimeout(deeplink, 60);
        driverTools.waitForElementToAppear(HOTEL_CARD, 30);
    }

    public String getCurrentUrl() {
        return driverTools.getCurrentURL();
    }

    public int getFlexibleStayHotelCardCount() {
        return driverTools.elements(FLEXIBLE_STAY_HOTEL_CARDS).orElseThrow().size();
    }

    public boolean isFlexibleStayPriceVisible() {
        return driverTools.isElementDisplayed(FLEXIBLE_STAY_HOTEL_PRICE);
    }

    public void clickHeartIconOnHotelCard(String hotelNameOnSrp) {
        Locator HOTEL_CARD_HEART_BUTTON = new Locator("HOTEL_CARD_HEART_BUTTON", new By.ByXPath("//a[contains(@class, 'FullProductCard_container')][.//*[contains(text(),'"+hotelNameOnSrp+"')]]//div[contains(@class,'PropertyImagesDesktop_wishlist_wrapper')]//span"));
        driverTools.waitForElementToAppear(HOTEL_CARD_HEART_BUTTON, 15).orElseThrow();
        driverTools.scrollIntoWebView(HOTEL_CARD_HEART_BUTTON);
        driverTools.clickWithJavascriptExecutor(HOTEL_CARD_HEART_BUTTON);
        driverTools.waitForElementToAppear(SAVED_TO_WISHLIST_POPUP,5);
    }

    public boolean isHeartIconActiveOnHotelCard(String hotelNameOnSrp) {
        Locator HOTEL_CARD_HEART_ACTIVE = new Locator("HOTEL_CARD_HEART_ACTIVE", new By.ByXPath("//a[contains(@class, 'FullProductCard_container')][.//*[contains(text(),'"+hotelNameOnSrp+"')]]//div[contains(@class,'PropertyImagesDesktop_wishlist_wrapper')]//span[.//*[name()='circle'][@fill-opacity='1']]"));
        return driverTools.waitForElementToAppear(HOTEL_CARD_HEART_ACTIVE, 10).isPresent()
                && driverTools.isElementDisplayed(HOTEL_CARD_HEART_ACTIVE);
    }
}
