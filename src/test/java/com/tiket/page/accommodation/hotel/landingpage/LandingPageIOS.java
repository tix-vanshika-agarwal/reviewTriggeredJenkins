package com.tiket.page.accommodation.hotel.landingpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.accommodation.homes.homeslandingpage.HomesLandingPageIOS;
import com.tiket.page.common.calendar.CalendarPage;
import com.tiket.pojo.accommodation.hotel.LandingPageModulePOJO;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.page.accommodation.homes.homeslandingpage.HomesLandingPageIOS.CLOSE_SEARCH_PAGE_TWO;
import static com.tiket.page.accommodation.homes.homeslandingpage.HomesLandingPageIOS.SEARCH_FIELD_POPUP;
import static com.tiket.page.home.HomePageIOS.INVENTORY_NAME;
import static com.tiket.page.home.HomePageIOS.CLOSE_GIVE_RATING_POPUP;
import static org.hamcrest.core.Is.is;

public class LandingPageIOS <T extends WebDriver> extends LandingPage<T> {
    public LandingPageIOS(Session<T> session) { super(session); }

    private static final Locator EMPTY_LANDING_PAGE_SECTION = new Locator("EMPTY_LANDING_PAGE_SECTION", AppiumBy.name("TVCell<UIView>+RxCell.EmptyView"));
    private static final Locator SEE_MORE_BUTTON = new Locator("SEE_MORE_BUTTON", AppiumBy.id("view_more_button"));
    private static final Locator SEE_ALL_BUTTON = new Locator("SEE_ALL_BUTTON", AppiumBy.xpath("//XCUIElementTypeCollectionView//parent::*//following-sibling::*[@name='page_module_see_all_button' and @visible='true']"));
    private static final Locator CHIP_PAGE_MODULE = new Locator("CHIP_PAGE_MODULE", AppiumBy.iOSClassChain("**/XCUIElementTypeAny[`name == 'page_module_chip_text' AND visible == 1`]"));
    public static final Locator SEARCH_FIELD_BUTTON = new Locator("SEARCH_FIELD_BUTTON", AppiumBy.xpath("//*[@name='search_field_btn']"));
    public static final Locator DATE_FIELD_BUTTON = new Locator("DATE_FIELD_BUTTON", AppiumBy.id("check_in_out_field"));
    /**
     * Destination search-sheet input ({@code search_field} outside landing {@code search_field_btn}).
     * On open this is often {@code XCUIElementTypeOther}; after focus it may become a TextField with a
     * different accessibility tree — use {@link #SEARCH_SHEET_TEXT_FIELD} / keyboard no-click to type.
     */
    public static final Locator SEARCH_FIELD_TEXT = new Locator("SEARCH_FIELD_TEXT",
            AppiumBy.xpath("//*[@name='search_field' and not(ancestor::*[@name='search_field_btn'])]"));
    /** Focused sheet TextField (placeholder e.g. "Accommodation name, destination, etc."). */
    private static final Locator SEARCH_SHEET_TEXT_FIELD = new Locator("SEARCH_SHEET_TEXT_FIELD",
            AppiumBy.xpath("//XCUIElementTypeTextField[contains(@value,'Accommodation') or contains(@label,'Accommodation') or contains(@value,'destination') or contains(@label,'destination') or contains(@value,'destinasi') or contains(@label,'destinasi') or @name='search_field']"));
    public static final Locator LOCATION_BUTTON = new Locator("LOCATION_BUTTON", AppiumBy.name("tds_ic_near_me"));
    public static final Locator DONE_BUTTON = new Locator("DONE_BUTTON", AppiumBy.accessibilityId("done_button"));
    public static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", AppiumBy.xpath("//*[@name='search_button']"));
    public static final Locator SUBMIT_BUTTON_CALENDAR = new Locator("SUBMIT_BUTTON_CALENDAR", AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == 'calendarHotelControl'`]/**/XCUIElementTypeButton"));
    public static final Locator POP_UP = new Locator("POP_UP", AppiumBy.name("ok_button"));
    public static final Locator ROOM_AND_GUEST_BTN = new Locator("ROOM_AND_GUEST_BTN", AppiumBy.iOSClassChain("**/*[`name == 'totalRoom' OR name == 'totalGuest' OR name == 'total_room_guest'`]"));
    public static final Locator ROOM_COUNT = new Locator("ROOM_COUNT", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='quantity_label'])[1]"));
    public static final Locator ADULT_COUNT = new Locator("ADULT_COUNT", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='quantity_label'])[last()-1]"));
    public static final Locator CHILD_COUNT = new Locator("CHILD_COUNT", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='quantity_label'])[last()]"));
    public static final Locator ROOM_DECREMENT_BTN = new Locator("ROOM_DECREMENT_BTN", AppiumBy.xpath("(//XCUIElementTypeButton[@name='subtract_btn'])[1]"));
    public static final Locator ADULT_DECREMENT_BTN = new Locator("ADULT_DECREMENT_BTN", AppiumBy.xpath("(//XCUIElementTypeButton[@name='subtract_btn'])[last()-1]"));
    public static final Locator CHILD_DECREMENT_BTN = new Locator("CHILD_DECREMENT_BTN", AppiumBy.xpath("(//XCUIElementTypeButton[@name='subtract_btn'])[last()]"));
    public static final Locator ROOM_INCREMENT_BTN = new Locator("ROOM_INCREMENT_BTN", AppiumBy.xpath("(//XCUIElementTypeButton[@name='add_btn'])[1]"));
    public static final Locator ADULT_INCREMENT_BTN = new Locator("ADULT_INCREMENT_BTN", AppiumBy.xpath("(//XCUIElementTypeButton[@name='add_btn'])[last()-1]"));
    public static final Locator CHILD_INCREMENT_BTN = new Locator("CHILD_INCREMENT_BTN", AppiumBy.xpath("(//XCUIElementTypeButton[@name='add_btn'])[last()]"));
    public static final Locator CHECK_IN_NOW = new Locator("CHECK_IN_NOW", AppiumBy.id("tds_ic_chevron_right"));
    public static final Locator CHECK_IN_LATER = new Locator("CHECK_IN_LATER", AppiumBy.name("laterTitleLabel"));
    private static final Locator NEW_FILTER_POPUP = new Locator("NEW_FILTER_POPUP", AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label CONTAINS 'Cobain sekarang' OR label CONTAINS 'Try it now'`]"));
    private static final Locator NEXT_POPUP_BUTTON = new Locator("NEXT_POPUP_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@label='Next' or @label='Lanjut']"));
    private static final Locator GOT_IT_POPUP_BUTTON = new Locator("UNDERSTAND_POPUP_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='Mengerti' or @label='Got it']"));
    private static final Locator FLASHSALE_SECTION_TITLE = new Locator("FLASHSALE_SECTION_TITLE", AppiumBy.xpath("//*[contains(@label,'Berakhir dalam') or contains(@label,'Promo ends in')]/preceding-sibling::XCUIElementTypeStaticText[1]"));
    private static final Locator MODULE_SECTION_TITLE = new Locator("MODULE_SECTION_TITLE", AppiumBy.name("page_module_title"));
    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON",AppiumBy.accessibilityId("back_button"));
    public static final Locator RECENTLY_SEARCHED_DETAIL = new Locator("RECENTLY_SEARCHED_DETAIL", AppiumBy.iOSClassChain("**/*[`name == 'recent_hotel_location'`][1]"));
    public static final Locator CLOSE_SEARCH = new Locator("CLOSE_SEARCH", AppiumBy.xpath("//XCUIElementTypeButton[@name='ic cancel grey' and @visible='true']"));
    private static final Locator TIMED_PROMO_TITLE = new Locator("TIMED_PROMO_TITLE", AppiumBy.iOSClassChain("**/*[`name == 'timed_promo'`][1]/*[`name == 'title'`]"));
    private static final Locator TIMED_PROMO_LOCATION_BUTTON = new Locator("TIMED_PROMO_LOCATION_BUTTON", AppiumBy.iOSClassChain("**/*[`name == 'timed_promo'`]/*[`name == 'location_list'`]/*"));
    private static final Locator TIMED_PROMO_HOTEL_NAME = new Locator("TIMED_PROMO_HOTEL_NAME", AppiumBy.iOSClassChain("**/*[`name == 'timed_promo'`]/*[`name == 'hotel_name'`]"));
    private static final Locator TIMED_PROMO_SEE_ALL_BUTTON = new Locator("TIMED_PROMO_SEE_ALL_BUTTON", AppiumBy.iOSClassChain("**/*[`name == 'timed_promo'`]/*[`type == 'XCUIElementTypeButton'`]"));
    public static final Locator MULTIPLE_SQUARE_CARD_SECTION = new Locator("MULTIPLE_SQUARE_CARD_SECTION", AppiumBy.iOSClassChain("**/*[`name == 'multiple_square_card'`][1]"));
    public static final Locator MULTIPLE_SQUARE_CARD_TITLE = new Locator("MULTIPLE_SQUARE_CARD_TITLE", AppiumBy.iOSClassChain("**/*[`name == 'multiple_square_card'`][1]/*[`name == 'title'`]"));
    public static final Locator MULTIPLE_SQUARE_CARD_SUB_TITLE = new Locator("MULTIPLE_SQUARE_CARD_SUB_TITLE", AppiumBy.iOSClassChain("**/*[`name == 'multiple_square_card'`][1]/*[`name == 'subtitle'`]"));
    public static final Locator MULTIPLE_SQUARE_CARD_CONTENT_TITLE = new Locator("MULTIPLE_SQUARE_CARD_CONTENT_TITLE", AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`name == 'multiple_square_card'`][1]/XCUIElementTypeStaticText[`name == 'card_name'`]"));
    private static final Locator RECENTLY_SEARCHED_LOCATION_ID = new Locator("RECENTLY_SEARCHED_LOCATION_ID", AppiumBy.xpath("(//*[@name='recent_location'])[last()]"));
    private static final Locator RECENTLY_SEARCHED_LOCATION_XPATH = new Locator("RECENTLY_SEARCHED_LOCATION_XPATH", AppiumBy.xpath("//*[@name=\"recent_hotel_name\"]"));
    public static final Locator OK_POPUP = new Locator("OK_POPUP", AppiumBy.xpath("//XCUIElementTypeButton[@name='Oke' or @name='OK']"));
    private static final Locator POPULAR_DESTINATION = new Locator("POPULAR_DESTINATION", AppiumBy.xpath("//*[@name='popular_destination_list']//XCUIElementTypeStaticText"));
    public static final Locator NEAR_ME_ICON = new Locator("NEAR_ME_ICON", AppiumBy.accessibilityId("location_button"));
    public static final Locator CONTINUE_ALLOW_LOCATION = new Locator("CONTINUE_ALLOW_LOCATION", AppiumBy.accessibilityId("buttonPrimary"));
    public static final Locator PERMISSION_TYPE_ALLOW_ONCE = new Locator("PERMISSION_TYPE_ALLOW_ONCE", AppiumBy.name("Allow Once"));
    public static final Locator CURRENT_LOCATION_SEARCH = new Locator("CURRENT_LOCATION_SEARCH", AppiumBy.xpath("//XCUIElementTypeOther[@name='search_field_btn']//XCUIElementTypeStaticText"));
    public static final Locator ALLOW_ONCE = new Locator("ALLOW_ONCE", AppiumBy.xpath("//*[@name='Allow']"));
    private static final Locator RECENT_DELETE_BUTTON = new Locator("RECENT_DELETE_BUTTON", AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label ==[c] 'clear all' OR label ==[c] 'hapus semua'`]"));
    private static final Locator PAGE_MODULE_TITLE = new Locator("PAGE_MODULE_TITLE", AppiumBy.iOSClassChain("**/*[`name ==[c] 'page_module_title'`]"));
    private static final Locator PAGE_MODULE_PRODUCT_CARD = new Locator("PAGE_MODULE_PRODUCT_CARD", AppiumBy.iOSClassChain("**/*[`name CONTAINS[c] 'page_module_product_card'`]"));

    public enum CounterType {
        ROOM(ROOM_INCREMENT_BTN, ROOM_DECREMENT_BTN, ROOM_COUNT),
        ADULT(ADULT_INCREMENT_BTN, ADULT_DECREMENT_BTN, ADULT_COUNT),
        CHILD(CHILD_INCREMENT_BTN, CHILD_DECREMENT_BTN, CHILD_COUNT);

        private final Locator increment;
        private final Locator decrement;
        private final Locator count;

        CounterType(Locator increment, Locator decrement, Locator count) {
            this.increment = increment;
            this.decrement = decrement;
            this.count = count;
        }

        public Locator getIncrement() { return increment; }
        public Locator getDecrement() { return decrement; }
        public Locator getCount() { return count; }
    }


    /**
     * Autocomplete suggestion after typing. Single class-chain node (no xpath {@code |} unions —
     * those hang iOS WDA). Matches ID and/or EN label when both are provided.
     */
    public static Locator searchList(String locationId, String locationEn) {
        String id = locationId == null ? "" : locationId.strip().replace("'", "");
        String en = locationEn == null ? "" : locationEn.strip().replace("'", "");
        if (id.isEmpty() && en.isEmpty()) {
            throw new IllegalArgumentException("searchList requires a non-empty locationId or locationEn");
        }
        List<String> labelParts = new ArrayList<>();
        if (!id.isEmpty()) {
            labelParts.add("label contains '" + id + "'");
            String idLower = id.toLowerCase(Locale.ROOT);
            if (!idLower.equals(id)) {
                labelParts.add("label contains '" + idLower + "'");
            }
        }
        if (!en.isEmpty() && !en.equalsIgnoreCase(id)) {
            labelParts.add("label contains '" + en + "'");
            String enLower = en.toLowerCase(Locale.ROOT);
            if (!enLower.equals(en)) {
                labelParts.add("label contains '" + enLower + "'");
            }
        }
        String labelPredicate = labelParts.size() == 1
                ? labelParts.get(0)
                : "(" + String.join(" OR ", labelParts) + ")";
        return new Locator("SEARCH_LIST", AppiumBy.iOSClassChain(
                "**/XCUIElementTypeAny[`name == 'suggestion_name_list' AND " + labelPredicate + "`]"));
    }

    private static Locator timedPromoHotelName(String hotelName) {
        return new Locator("TIMED_PROMO_HOTEL_NAME", AppiumBy.iOSClassChain("**/*[`name == 'timed_promo'`]/*[`name == 'hotel_name' AND label contains '" + hotelName + "'`]"));
    }

    @Override
    public void clickSeeMoreFloatingButton() {
        driverTools.waitForElementToPresence(SEE_MORE_BUTTON);
        driverTools.click(SEE_MORE_BUTTON);
    }

    @Override
    public String getModuleTitleSection() {
        if (driverTools.waitForElementToPresence(FLASHSALE_SECTION_TITLE).isPresent()) {
            return driverTools.getText(FLASHSALE_SECTION_TITLE).orElseThrow();
        } else {
            // Optimized: swipeToElement now checks visibility first, so this is already optimized
            driverTools.swipeToElement(MODULE_SECTION_TITLE, Swipe.UP, 6);
            return driverTools.getText(MODULE_SECTION_TITLE).orElseThrow();
        }
    }

    @Override
    public void clickSeeAllButton() {
        // Optimized: Check if element is visible before swiping
        Optional<WebElement> seeAllButton = driverTools.element(SEE_ALL_BUTTON);
        if (seeAllButton.isEmpty() || !seeAllButton.get().isDisplayed()) {
            driverTools.swipeToElement(SEE_ALL_BUTTON, Swipe.UP, 6);
        }
        // Try direct click first (faster), fallback to coordinate click if needed
        try {
            driverTools.click(SEE_ALL_BUTTON);
        } catch (Exception e) {
            logger.log("Direct click failed, using coordinate click: " + e.getMessage());
            driverTools.clickElementByCoordinate(SEE_ALL_BUTTON);
        }
    }

    @Override
    public void clickFirstSeeAllButton() {
        driverTools.waitForElementToDisappear(EMPTY_LANDING_PAGE_SECTION);
        clickSeeMoreFloatingButton();
        clickSeeAllButton();
    }

    @Override
    public void clickFirstHotelCard() {
        int tries = 0;
        while(!driverTools.waitForElementToAppear(INVENTORY_NAME).isPresent() && tries++ < 5){
            driverTools.swipeUp(2);
        }
        driverTools.clickElementByCoordinate(INVENTORY_NAME);
    }

    @Override
    public void searchHotelList(List<String> hotelList) {
        throw new NotImplementedException("Not implemented"); //FIXME: Pending implementation
    }

    @Override
    public void selectRecentViewedTab() {
        throw new NotImplementedException("Not implemented"); //FIXME: Pending implementation
    }

    @Override
    public int deleteRecentViewItems(List<String> hotelList, int maxRecentlyViewed) {
        throw new NotImplementedException("Not implemented"); //FIXME: Pending implementation
    }

    @Override
    public boolean isRecentViewedLandingPageEmpty() {
        throw new NotImplementedException("Not implemented"); //FIXME: Pending implementation
    }

    @Override
    public void clearAllRecentViewed() {
        if (driverTools.waitForElementToAppear(RECENT_DELETE_BUTTON).isPresent())
            driverTools.click(RECENT_DELETE_BUTTON);
    }

    @Override
    public int getRecentViewedItemCount(List<String> hotelList, int maxRecentlyViewed) {
        throw new NotImplementedException("Not implemented"); //FIXME: Pending implementation
    }

    @Override
    public void clickSearchField() {
        driverTools.click(SEARCH_FIELD_BUTTON);
    }

    @Override
    public boolean isRecentViewedSearchPopupEmpty() {
        throw new NotImplementedException("Not implemented"); //FIXME: Pending implementation
    }

    @Override
    public boolean verifyRedirectSearchForm(LandingPageModulePOJO.Redirect redirectData) {
        driverTools.waitForElementToPresence(POPULAR_DESTINATION).orElseThrow();

        int totalPopularDestinationApi = redirectData.popularDestination.destinations.size();
        List<WebElement> popularDestinationItemApp = driverTools.elements(POPULAR_DESTINATION).orElseThrow();
        int totalPopularDestinationApp = popularDestinationItemApp.size();

        Assertion.assertThat("Total popular destination on the App and API should be match", totalPopularDestinationApi, is(totalPopularDestinationApp));

        for (int i = 0; i < totalPopularDestinationApi; i++) {
            logger.log("index -> " + i);
            String popularDestinationApi = redirectData.popularDestination.destinations.get(i).name;
            String popularDestinationApp = popularDestinationItemApp.get(i).getText();
            Assertion.assertThat("Popular destination item on the App and API should be match", popularDestinationApi, is(popularDestinationApp));
        }
        return true;
    }

    @Override
    public void searchStayLocation(String location) {
        driverTools.waitForElementToAppear(CLOSE_GIVE_RATING_POPUP, 3).ifPresent(driverTools::click);
        String methodName = "searchStayLocation";
        long startTime = CoreFunctions.startTiming(methodName);

        try {
            String[] splitData = splitLanguageData(location, "/");
            String locationId = splitData[0];
            String locationEn = splitData.length > 1 ? splitData[1] : splitData[0];

            driverTools.waitForElementToAppear(SEARCH_FIELD_BUTTON, 10).orElseThrow();
            try {
                driverTools.click(SEARCH_FIELD_BUTTON);
            } catch (Exception e) {
                driverTools.clickElementByCoordinate(SEARCH_FIELD_BUTTON);
            }

            // Open sheet input, type, select suggestion (same flow as before — no extra asserts)
            WebElement field = driverTools.waitForElementToAppear(SEARCH_FIELD_TEXT, 12).orElseThrow();
            try {
                driverTools.click(field);
            } catch (Exception e) {
                driverTools.clickElementByCoordinate(field);
            }
            // After focus Other may unmount — type focused TextField or keyboard Actions (no re-find)
            Optional<WebElement> textField = driverTools.waitForElementToAppear(SEARCH_SHEET_TEXT_FIELD, 5);
            if (textField.isPresent()) {
                try {
                    driverTools.clear(textField.get());
                } catch (Exception ignored) {
                }
                driverTools.sendKeys(textField.get(), locationId);
            } else {
                driverTools.sendKeysKeyboardNoClick(field, locationId);
            }

            WebElement searchResult = driverTools.waitForElementToAppear(searchList(locationId, locationEn), 12)
                    .orElseThrow();
            try {
                driverTools.click(searchResult);
            } catch (Exception e) {
                // WebElement coordinate click — avoids re-running findElement on Locator (WDA hang)
                driverTools.clickElementByCoordinate(searchResult);
            }
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public boolean verifyPoiOnHotelCards(String poi) {
        String poiLower = poi == null ? "" : poi.strip().toLowerCase();
        Locator poiLocationInfo = new Locator("POI_LOCATION_INFO_" + poiLower.toUpperCase(),
                AppiumBy.xpath("//*[@name='locationLabel' and (contains(@label,'from " + poiLower + "') or contains(@label,'dari " + poiLower + "'))]"));
        return driverTools.waitForElementToPresence(poiLocationInfo, 10).isPresent();
    }

    private void clickWithFallback(Locator locator) {
        try {
            driverTools.click(locator);
        } catch (Exception e) {
            logger.log("Direct click failed, using coordinate click for " + locator);
            driverTools.clickElementByCoordinate(locator);
        }
    }

    private void sendKeysDebounced(Locator locator, String text) {
        driverTools.sendKeysKeyboard(locator, text);
        driverTools.dummyWait(2); // allow suggestions to stabilize
    }



    @Override
    public String[] setStayDate(String type, String date) {
        CalendarPage<T> calendarPage = CalendarPage.getInstance(session);

        String[] stayDate = date.split("-");

        if(stayDate[1].equals("0")){
            return null;
        }

        int startDate = Integer.parseInt(stayDate[0]);
        int endDate = Integer.parseInt(stayDate[1]);

        if(startDate<=0 || startDate>31 || endDate<=0 || endDate>31){
            throw new DriverToolsException("Test data 'date' with value '"+ date +"' is not valid, please check your data and input the valid date");
        }

        switch (type){
            case "exact date":
                break;
            case "range date":{
                int randomStartDate = generateRandomInt(startDate,endDate-1);
                int randomEndDate = generateRandomInt(randomStartDate+1, endDate);
                logger.log("Start date: '"+ startDate +"',  End date: '"+ endDate +"'");

                startDate = randomStartDate;
                endDate = randomEndDate;
                break;
            }
            case "add date":{
                LocalDate currentDateString = LocalDate.now();
                int currentDate = currentDateString.getDayOfMonth();
                startDate = currentDate+startDate;
                endDate = startDate+endDate;
                break;
            }
            default:
                throw new IllegalArgumentException("Date type '"+ type +"' is not supported. Make sure to input 'exact date' or 'range date'");
        }

        List <Locator> dateFieldLocators = new LinkedList();
        dateFieldLocators.add(DATE_FIELD_BUTTON);
        dateFieldLocators.add(HomesLandingPageIOS.DATE_FIELD_BUTTON);
        driverTools.click(dateFieldLocators, driverTools::isElementDisplayed);

        LocalDate currentDateString = LocalDate.now();
        int currentDate = currentDateString.getDayOfMonth();
        String currentMonth = String.valueOf(currentDateString.getMonth());
        String currentYear = String.valueOf(currentDateString.getYear());
        String month = currentMonth.charAt(0)+currentMonth.substring(1).toLowerCase();

        String startDateString = startDate + " " + month + " " + currentYear;
        logger.log("startDateString -> " + startDateString);
        String endDateString = endDate + " " + month  + " " + currentYear;
        logger.log("endDateString -> " + endDateString);

        int daysToAdd = startDate < currentDate ? currentDateString.lengthOfMonth() : 0;
        String checkInLong = CoreFunctions.addToLongDate(startDateString, daysToAdd);
        String checkOutLong = CoreFunctions.addToLongDate(endDateString, daysToAdd);

        calendarPage.selectDate(checkInLong);
        // Calendar re-renders after check-in; brief settle before checkout cell lookup
        driverTools.dummyWait(2);
        calendarPage.selectDate(checkOutLong);

        submitCalendar();

        return new String[]{startDate + " " + month.substring(0,3) + " " + currentYear, endDate + " " + month.substring(0,3) + " " + currentYear};
    }

    @Override
    public String getSelectedCheckInOut() {
        return driverTools.getText(DATE_FIELD_BUTTON).orElseThrow();
    }

    @Override
    public String getRoomAndGuestText() {
        return driverTools.getText(ROOM_AND_GUEST_BTN).orElseThrow().trim();
    }

    @Override
    public void clickDone() {
        String methodName = "clickDone";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            WebElement done_btn = driverTools.waitForElementToPresence(DONE_BUTTON).orElseThrow();
            driverTools.click(done_btn);
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void clickSubmit() {
        String methodName = "clickSubmit";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            driverTools.waitForElementToPresence(SUBMIT_BUTTON, 20).orElseThrow();
            try {
                driverTools.click(SUBMIT_BUTTON);
            } catch (Exception e) {
                logger.log("Direct click failed, using coordinate click: " + e.getMessage());
                driverTools.clickElementByCoordinate(SUBMIT_BUTTON);
            }
            if (!driverTools.waitForElementToDisappear(SUBMIT_BUTTON, 4)) {
                driverTools.clickElementByCoordinate(SUBMIT_BUTTON);
            }
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    public void submitCalendar(){
        WebElement save = driverTools.waitForElementToPresence(SUBMIT_BUTTON_CALENDAR)
                .orElseThrow(() -> new NoSuchElementException("Submit calendar not found"));
        if (!save.isEnabled()) {
            throw new IllegalStateException("Calendar Save is disabled — check-in/check-out not both selected");
        }
        driverTools.click(save);
    }

    @Override
    public void fillNightCheckInForm(){
        if (driverTools.waitForElementToAppear(CLOSE_GIVE_RATING_POPUP).isPresent()) { 
            driverTools.click(CLOSE_GIVE_RATING_POPUP);
        }
        if(driverTools.isElementDisplayed(CHECK_IN_LATER)){
            driverTools.click(CHECK_IN_LATER);
        }

        if(driverTools.waitForElementToPresence(NEXT_POPUP_BUTTON).isPresent()){
            driverTools.click(NEXT_POPUP_BUTTON);
        }
        if(driverTools.waitForElementToPresence(GOT_IT_POPUP_BUTTON).isPresent()){
            driverTools.click(GOT_IT_POPUP_BUTTON);
        } else{
            Optional<WebElement> newFilterPopup = driverTools.waitForElementToPresence(NEW_FILTER_POPUP);
            if (newFilterPopup.isPresent()) {
                driverTools.click(newFilterPopup.orElseThrow());
                driverTools.click(CLOSE_SEARCH);
            }
            if (driverTools.isElementDisplayed(POP_UP)) {
                driverTools.click(POP_UP);
                if (driverTools.isElementDisplayed(CLOSE_SEARCH)) {
                    driverTools.click(CLOSE_SEARCH);
                }
            }
        }
    }

    @Override
    public void clickRoomAndGuestButton() {
        String methodName = "clickRoomAndGuestButton";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            WebElement webElement = driverTools.waitForElementToPresence(ROOM_AND_GUEST_BTN).orElseThrow();
            driverTools.click(webElement);
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void setTotalRoom(int totalRoomInput) {
        String methodName = "setTotalRoom";
        long startTime = CoreFunctions.startTiming(methodName);

        try {
            setCount(CounterType.ROOM, totalRoomInput);
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }


    @Override
    public void setTotalGuest(int totalGuestInput) {
        String methodName = "setTotalGuest";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            setCount(CounterType.ADULT, totalGuestInput);
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    @Override
    public void setTotalChildren(int totalChildInput) {
        String methodName = "setTotalChildren";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            setCount(CounterType.CHILD, totalChildInput);
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    private void setCount(CounterType type, int targetCount) {

        WebElement countElement = driverTools.waitForElementToPresence(type.getCount()).orElseThrow();
        int currentCount = Integer.parseInt(
                driverTools.getText(countElement)
        );

        int delta = targetCount - currentCount;

        if (delta == 0) {
            return; // Fast path
        }

        Locator action =
                delta > 0 ? type.getIncrement() : type.getDecrement();

        int steps = Math.abs(delta);

        for (int i = 0; i < steps; i++) {
            driverTools.click(action);
            driverTools.dummyWait(1); // allow UI to update
        }
    }

    @Override
    public void removePopUp(){
        if (driverTools.waitForElementToAppear(POP_UP,5).isPresent()) {
            driverTools.click(POP_UP);
        }
    }

    @Override
    public void backToHomePage(){
        driverTools.waitForElementToAppear(OK_POPUP).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(BACK_BUTTON).ifPresentOrElse(driverTools::click, ()-> {throw new NoSuchElementException("Back button not found");});
        if (driverTools.isElementDisplayed(SUBMIT_BUTTON))
            driverTools.waitForElementToDisappear(BACK_BUTTON);
    }

    private String getNextMonth(){
        LocalDate currentDate = LocalDate.now();
        int currentMonthInt = currentDate.getMonthValue();
        int nextMonthInt = currentMonthInt + 1;
        String nextMonthEngName = String.valueOf(Month.of(nextMonthInt));
        nextMonthEngName = nextMonthEngName.substring(0, 1).toUpperCase() + nextMonthEngName.substring(1).toLowerCase();
        String nextMonthIdName = monthEngToId(nextMonthEngName);

        return nextMonthIdName;
    }

    private String getCurrentMonth(){
        LocalDate currentDate = LocalDate.now();
        int currentMonthInt = currentDate.getMonthValue();
        String monthEngName = String.valueOf(Month.of(currentMonthInt));
        monthEngName = monthEngName.substring(0, 1).toUpperCase() + monthEngName.substring(1).toLowerCase();
        String monthIdnName = monthEngToId(monthEngName);

        return monthIdnName;
    }

    private Locator getNextMonthElement(String nextMonthIdName) {
        return new Locator("NEXT_MONTH", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and contains(@label, '" + nextMonthIdName + "')]"));
    }

    private Locator getDateElement(String elementName, String month, int date){
        return new Locator(elementName, AppiumBy.xpath("//XCUIElementTypeButton[contains(@label, '" + month + " " + date + "')]"));
    }

    private Locator getAreaElement(String location){
        String separator = "/";
        String[] splitData = splitLanguageData(location, separator);
        String locationId = splitData[0];
        String locationEn = splitData[1];
        return new Locator("AREA", AppiumBy.xpath("//*[@name='suggestion_name_list' and (@label='"+locationId+"' or @label='"+locationEn+"' or @label='"+locationId.toLowerCase()+"' or @label='"+locationEn.toLowerCase()+"')]//following-sibling::*[@name='sub_suggestion_name_list']"));
    }
    @Override
    public String getAreaText(String location) {
        List <Locator> searchField = new LinkedList();
        searchField.add(SEARCH_FIELD_BUTTON);
        searchField.add(SEARCH_FIELD_POPUP);
        driverTools.click(searchField, driverTools::isElementDisplayed);
        WebElement webElement = driverTools.waitForElementToPresence(getAreaElement(location)).orElseThrow();
        String area = driverTools.getValue(webElement);
        List <Locator> closeSearch = new LinkedList();
        closeSearch.add(CLOSE_SEARCH);
        closeSearch.add(CLOSE_SEARCH_PAGE_TWO);
        driverTools.click(closeSearch, driverTools::isElementDisplayed);
        return area;
    }
    @Override
    public boolean recentlySearchedStatus(String dateRoomGuestDetail, String searchText, String area) {
        String detail = driverTools.getText(RECENTLY_SEARCHED_DETAIL).orElseThrow();
        String[] detailsLandingPage = detail.split(" ");
        logger.log("detailsLandingPage -> " + Arrays.toString(detailsLandingPage));
        String checkInDate = String.valueOf(CoreFunctions.getIntFromString(detailsLandingPage[0]).orElseThrow());
        logger.log("detail checkInDate -> " + checkInDate);
        String checkOutDate = String.valueOf(CoreFunctions.getIntFromString(detailsLandingPage[4]).orElseThrow());
        logger.log("detail checkOutDate -> " + checkOutDate);
        logger.log("dateRoomGuestDetail -> " + dateRoomGuestDetail);
        String[] detailsSRP = dateRoomGuestDetail.split(" ");
        logger.log("detailsSRP -> " + Arrays.toString(detailsSRP));
        boolean dateCheck = false;
        if(detail.toLowerCase().contains("kamar") | detail.toLowerCase().contains("room")) {
            Assertion.assertThat("Check-in date should be the same between landing page and SRP", checkInDate + " " + detailsLandingPage[1], is(detailsSRP[0] + " " + detailsSRP[1]));
            Assertion.assertThat("Check-out date should be the same between landing page and SRP", checkOutDate + " " + detailsLandingPage[5], is(detailsSRP[3] + " " + detailsSRP[4]));
            Assertion.assertThat("Room and guest details should be the same between landing page and SRP", detailsLandingPage[8] + " " + detailsLandingPage[10], is(detailsSRP[6] + " " + detailsSRP[9]));
            dateCheck = true;
        }
        else {
            Assertion.assertThat("Check-in date should be the same between landing page and SRP", checkInDate + detailsLandingPage[1], is(detailsSRP[0] + detailsSRP[1]));
            Assertion.assertThat("Check-out date should be the same between landing page and SRP", checkOutDate + detailsLandingPage[5], is(detailsSRP[3] + detailsSRP[4]));
            Assertion.assertThat("Room and guest details should be the same between landing page and SRP", detailsLandingPage[8] + " " + detailsLandingPage[9], is(detailsSRP[6] + " " + detailsSRP[7]));
            dateCheck = true;
        }
        String [] areas = area.toLowerCase().split(",");
        String location;
        if (driverTools.isElementDisplayed(RECENTLY_SEARCHED_LOCATION_ID)) {
            location = driverTools.getText(RECENTLY_SEARCHED_LOCATION_ID).orElseThrow();
        } else {
            location = driverTools.getText(RECENTLY_SEARCHED_LOCATION_XPATH).orElseThrow();
        }
        location = location.toLowerCase();
        logger.log("location -> " + location);

        boolean placeCheck = false;

        String separator = "/";
        String[] splitData = splitLanguageData(searchText.toLowerCase(), separator);
        String searchTextId = splitData[0]+", "+areas[0];
        String searchTextEn = splitData[1]+", "+areas[0];
        logger.log("ID -> " + searchTextId);
        logger.log("EN -> " + searchTextEn);

        logger.log(location);
        logger.log(searchTextId);
        if(location.equals(searchTextId) || location.equals(searchTextEn)) {
            logger.log("this is ran");
            placeCheck = true;
        }
        logger.log("place check-> " + placeCheck);
        logger.log("date check-> " + dateCheck);
        return placeCheck & dateCheck;
    }
    @Override
    public boolean isRecentlySearchAvailable(String location) {
        String separator = "/";
        String[] splitData = splitLanguageData(location, separator);
        String locationId = splitData[0];
        String locationEn = splitData[1];

        List<Locator> searchField = new LinkedList<>();
        searchField.add(SEARCH_FIELD_BUTTON);
        searchField.add(SEARCH_FIELD_POPUP);
        driverTools.click(searchField, driverTools::isElementDisplayed);
        Locator RECENTLY_SEARCH_TITILE = new Locator("RECENTLY_SEARCH_TITILE", AppiumBy.xpath("//XCUIElementTypeCell//XCUIElementTypeStaticText[contains(@label,'"+locationId+"') or contains(@label,'"+locationEn+"') or contains(@label,'"+locationId.toLowerCase()+"') or contains(@label,'"+locationEn.toLowerCase()+"')]"));
        String title = driverTools.getText(RECENTLY_SEARCH_TITILE).orElseThrow();
        List <Locator> closeSearch = new LinkedList<>();
        closeSearch.add(CLOSE_SEARCH);
        closeSearch.add(CLOSE_SEARCH_PAGE_TWO);
        driverTools.click(closeSearch, driverTools::isElementDisplayed);
        return title.contains(locationId) || title.contains(locationEn) || title.contains(locationId.toLowerCase()) || title.contains(locationEn.toLowerCase());
    }

    @Override
    public boolean isRecentlySearchAvailable() {
        return driverTools.waitForElementToAppear(RECENTLY_SEARCHED_LOCATION_XPATH).isPresent() ||
            driverTools.isElementDisplayed(RECENTLY_SEARCHED_DETAIL);
    }

    @Override
    public String getRecentlySearchLocation() {
        String location = driverTools.getText(RECENTLY_SEARCHED_LOCATION_XPATH).orElseThrow();
        if(location.contains(", ")) {
            return location.split(", ")[0];
        } else {
            return location;
        }
    }

    @Override
    public boolean isPageModulePresent() {
        return driverTools.waitForElementToAppear(PAGE_MODULE_TITLE).isPresent();
    }

    @Override
    public String getPageModuleTitle() {
        return driverTools.getText(PAGE_MODULE_TITLE).orElseThrow();
    }

    @Override
    public String getPageModuleProductCard() {
        driverTools.swipeToElement(PAGE_MODULE_PRODUCT_CARD, Swipe.UP, 2);
        WebElement productCard = driverTools.waitForElementToPresence(PAGE_MODULE_PRODUCT_CARD).orElseThrow();
        return driverTools.getText(productCard);
    }

    @Override
    public String getRecentTotalGuest(String location, String[] checkInOutDate) {
        return null; //FIXME: Pending implementation
    }

    @Override
    public void openCalendarPage(){
        driverTools.click(DATE_FIELD_BUTTON);
    }

    @Override
    public void goToTimedPromoSection() {
        driverTools.waitForElementToAppear(SEARCH_FIELD_BUTTON).orElseThrow();
        driverTools.swipeToElement(TIMED_PROMO_SEE_ALL_BUTTON, Swipe.UP, 10).orElseThrow();
    }

    @Override
    public String getTimedPromoTitle() {
        WebElement title = driverTools.waitForElementToAppear(TIMED_PROMO_TITLE).orElseThrow();
        return driverTools.getText(title);
    }

    @Override
    public String selectTimedPromoLocation(int index) {
        List<WebElement> locationList = driverTools.elements(TIMED_PROMO_LOCATION_BUTTON).orElseThrow();
        WebElement selectedLocation = locationList.get(index - 1);
        String locationTitle = driverTools.getValueAttribute(selectedLocation, "name");
        if (locationTitle.contains(" (selected)")) locationTitle = locationTitle.replace(" (selected)", "");
        driverTools.click(selectedLocation);
        return locationTitle;
    }

    @Override
    public String clickSeeAllButtonTimedPromo() {
        WebElement button = driverTools.waitForElementToAppear(TIMED_PROMO_SEE_ALL_BUTTON).orElseThrow();
        if (!button.isDisplayed()) driverTools.swipeUp();
        String buttonText = driverTools.getValueAttribute(button, "label");
        driverTools.click(button);
        return buttonText;
    }

    @Override
    public boolean compareTimedPromoHotelName(ArrayList<String> hotelNameJson) {
        boolean result = false;
        driverTools.elements(TIMED_PROMO_HOTEL_NAME).orElseThrow();
        int totalHotelParam = hotelNameJson.size();
        logger.log("totalHotelParam-> " + totalHotelParam);

        if (totalHotelParam > 3) totalHotelParam = 3;
        for (int i = 1; i < totalHotelParam; i++) {
            String hotelNameApi = hotelNameJson.get(i);
            logger.log(i + " index of hotelNameApi-> " + hotelNameApi);

            boolean isHotelNamePresent = driverTools.swipeToElement(timedPromoHotelName(hotelNameApi), TIMED_PROMO_HOTEL_NAME, Swipe.LEFT, 5).isPresent();
            logger.log("isHotelNamePresent on the App -> " + isHotelNamePresent);
            if (!isHotelNamePresent) {
                logger.fail(i + " index = Hotel name from the API is \"" + hotelNameApi + "\", but not found on the App");
                result = false;
                break;
            } else {
                result = true;
            }
        }
        return result;
    }

    @Override
    public String getMasterTitle() {
    return null; //FIXME IOS IMPLEMENTATION PENDING
    }
    @Override
    public String getMasterSubTitle() {
        return null; //FIXME IOS IMPLEMENTATION PENDING
    }
    @Override
    public boolean checkLocationTagsForMultipleHotelId(LandingPageModulePOJO landingPageModuleApi) {
        return false;
    }
    @Override
    public String getButtonText(){ return null; } //FIXME IOS IMPLEMENTATION PENDING
     @Override
    public void seeAllMultipleID(){}//FIXME IOS IMPLEMENTATION PENDING

    @Override
    public void goToMultipleSquareCard() {
        driverTools.swipeToElement(MULTIPLE_SQUARE_CARD_SECTION, Swipe.UP, 10);
    }

    @Override
    public String getMultipleSquareCardTitle() {
        driverTools.swipeToElement(MULTIPLE_SQUARE_CARD_TITLE, Swipe.UP, 1);
        return driverTools.getValueAttribute(MULTIPLE_SQUARE_CARD_TITLE,"label");
    }

    @Override
    public String getMultipleSquareCardSubTitle() {
        driverTools.swipeToElement(MULTIPLE_SQUARE_CARD_SUB_TITLE, Swipe.UP, 1);
        return driverTools.getValueAttribute(MULTIPLE_SQUARE_CARD_SUB_TITLE,"label");

    }

    @Override
    public String[] getMultipleSquareCardTitleList() {
        List<String> resultList = new ArrayList<>();
        List<WebElement> contentTitleElementList = driverTools.elements(MULTIPLE_SQUARE_CARD_CONTENT_TITLE).orElseThrow();

        for(WebElement e : contentTitleElementList) {
            String contentTitle = driverTools.getValueAttribute(e,"label");

            if (contentTitle == null) {
                resultList.add("");
            } else {
                resultList.add(contentTitle);
            }
        }

        return resultList.toArray(new String [0]);
    }

    @Override
    public LandingPageModulePOJO.List chooseRandomMultipleSquareCard(LandingPageModulePOJO inputApi) {
        ArrayList<LandingPageModulePOJO.List> contentArray = inputApi.data.list;
        int randomIndex = CoreFunctions.generateRandomInt(0,contentArray.size()-1);
        LandingPageModulePOJO.List contentObject = contentArray.get(randomIndex);
        logger.log("contentObject -> " + contentObject);

        randomIndex = randomIndex == 0 ? contentArray.size() : randomIndex;
        clickMultipleSquareCard(randomIndex);

        return contentObject;
    }

    private void clickMultipleSquareCard(int index) {
        WebElement multipleSquareCardElement = driverTools.swipeToElement(getMultipleSquareCardLocator(index), Swipe.UP, 2).orElseThrow();
        driverTools.click(multipleSquareCardElement);
    }

    private Locator getMultipleSquareCardLocator(int index) {
        return new Locator("MULTIPLE_SQUARE_CARD_CONTENT_TITLE", AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`name == 'multiple_square_card'`][1]/XCUIElementTypeStaticText[`name == 'card_name'`]["+index+"]"));
    }

    /**
     * This method is used to click the Near Me icon
     */
    @Override
    public void clickNearMeIcon() {
        driverTools.waitForElementToAppear(NEAR_ME_ICON);
        // Optimized: Try direct click first, fallback to coordinate click if needed
        try {
            driverTools.click(NEAR_ME_ICON);
        } catch (Exception e) {
            logger.log("Direct click failed, using coordinate click: " + e.getMessage());
            driverTools.clickElementByCoordinate(NEAR_ME_ICON);
        }
    }

    /**
     * This method is used to click the back button
     */
    @Override
    public void clickBackButton() {
        driverTools.waitForElementToAppear(BACK_BUTTON);
        driverTools.clickElementByCoordinate(BACK_BUTTON);
    }

    /**
     * This method is used to enable location
     */
    @Override
    public void enableLocation() {
        if(driverTools.isElementDisplayed(CONTINUE_ALLOW_LOCATION)){
            driverTools.clickElementByCoordinate(CONTINUE_ALLOW_LOCATION);   
        }
        if (driverTools.isElementDisplayed(PERMISSION_TYPE_ALLOW_ONCE)) {
            driverTools.clickElementByCoordinate(PERMISSION_TYPE_ALLOW_ONCE);
        }        
    }

    @Override
    public String getCurrentSearchLocation(){
        String currentLocation = driverTools.getText(CURRENT_LOCATION_SEARCH).orElseThrow();
        logger.log("Current location search: "+currentLocation);
        return currentLocation;
    }

    @Override
    public Boolean isLandingPageValid() {
        boolean isSearchFieldButtonPresent = driverTools.waitForElementToAppear(SEARCH_FIELD_BUTTON).isPresent();
        boolean isSubmitButtonPresent = driverTools.waitForElementToAppear(SUBMIT_BUTTON).isPresent();

        return isSearchFieldButtonPresent && isSubmitButtonPresent;
    }

    @Override
    public String getFirstHotelName(){
        driverTools.swipeToElement(INVENTORY_NAME, Swipe.UP, 5);
        return driverTools.getText(INVENTORY_NAME).orElseThrow();
    }

    @Override
    public String checkHotelPrice(boolean isCrossPrice, String hotelName, String price) {
        //isCrossPrice only for Android
        return getHotelPrice(hotelName, price);
    }

    private String getHotelPrice(String hotelName, String price){
        WebElement priceElement = driverTools.swipeToElement(getHotelPriceLocator(hotelName, price), Swipe.UP, 5).orElseThrow();
        String priceText = driverTools.getText(priceElement);
        logger.log("Price text found: " + priceText);
        return priceText;
    }

    @Override
    public boolean checkHotelPrice(String hotelName, String price){
        return driverTools.swipeToElement(getHotelPriceLocator(hotelName, price), Swipe.UP, 10).isPresent();
    }

    private Locator getHotelPriceLocator(String hotelName, String price) {
        return new Locator("HOTEL_PRICE", AppiumBy.xpath("//*[@name='product_name' and @label='"+hotelName+"']//following-sibling::*[contains(@label,'"+price+"')]"));
    }

    @Override
    public void selectModuleChip(String chipName) {
        driverTools.swipeToElement(getModuleChipLocator(chipName), Swipe.UP, 30);
        driverTools.click(getModuleChipLocator(chipName));
    }

    private Locator getModuleChipLocator(String chip) {
        return new Locator("MODULE_CHIP", AppiumBy.xpath("//*[contains(@name,'page_module_chip_text')]//*[@label='" + chip + "']") );
    }
}
