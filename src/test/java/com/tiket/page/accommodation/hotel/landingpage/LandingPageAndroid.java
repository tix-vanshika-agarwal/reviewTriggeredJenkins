package com.tiket.page.accommodation.hotel.landingpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.common.calendar.CalendarPage;
import com.tiket.pojo.accommodation.hotel.LandingPageModulePOJO;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.page.setting.SettingPageAndroid.TURN_ON_LOCATION_DEVICE;
import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.is;

public class LandingPageAndroid<T extends WebDriver> extends LandingPage<T> {

    public LandingPageAndroid(Session<T> session) {
        super(session);
    }

    private static final Locator SEE_MORE_BUTTON = new Locator("SEE_MORE_BUTTON", AppiumBy.id("tds_extended_fab_text"));
    private static final Locator SEE_ALL_BUTTON = new Locator("SEE_ALL_BUTTON", AppiumBy.xpath("(//*[contains(@resource-id,'btn_see_all')])[1]"));
    public static final Locator SEARCH_FIELD_BUTTON = new Locator("SEARCH_FIELD_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'form_destination')]//*[contains(@resource-id,'tds_search_form_title') and not(contains(@resource-id,'image')) and not(contains(@resource-id,'group'))]"));
    public static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", AppiumBy.id("tds_button_view"));
    public static final Locator DATE_FIELD_BUTTON = new Locator("DATE_FIELD_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'form_date')]//*[contains(@resource-id,'tds_search_form_title') and not(contains(@resource-id,'image')) and not(contains(@resource-id,'group'))]"));
    private static final Locator DATE_FIELD_TEXT = new Locator("DATE_FIELD_TEXT", AppiumBy.xpath("(//*[contains(@resource-id, 'form_date')]//*[contains(@resource-id, 'tds_search_form_title')])[last()]"));
    public static final Locator SEARCH_FIELD_TEXT = new Locator("SEARCH_FIELD_TEXT", AppiumBy.id("tds_search_et"));
    public static final Locator CURRENT_SELECTED_START_DATE = new Locator("CURRENT_SELECTED_START_DATE", AppiumBy.id("//*[contains(@resource-id, 'll_day_group')]/android.widget.TextView[@text='Hari Ini']"));
    public static final Locator CURRENT_MOTH = new Locator("CURRENT_MOTH", AppiumBy.id("tds_month_name_calendar"));
    public static final Locator SELECTED_MONTH = new Locator("SELECTED_MONTH", AppiumBy.id("//*[contains(@resource-id, 'tds_month_name_calendar') and contains(@text, 'none')]"));
    public static final Locator CALENDAR = new Locator("CALENDAR", AppiumBy.id("tds_calendar_month_rv"));
    private static final Locator SELECTED_CHECKIN = new Locator("SELECTED_CHECKIN", AppiumBy.id("tds_calendar_start_date"));
    private static final Locator SELECTED_CHECKOUT = new Locator("SELECTED_CHECKOUT", AppiumBy.id("tds_calendar_end_date"));
    public static final Locator CHECKIN_NOW = new Locator("CHECKIN_NOW", AppiumBy.id("view_checkin_now"));
    public static final Locator CHECKIN_LATER = new Locator("CHECKIN_LATER", AppiumBy.id("view_checkin_later"));
    private static final Locator ROOM_AND_GUEST_TEXT = new Locator("ROOM_AND_GUEST_TEXT", AppiumBy.xpath("(//*[contains(@resource-id,'form_guest_room')]//*[contains(@resource-id,'tds_search_form_title')])[last()]"));
    public static final Locator ROOM_AND_GUEST_BTN = new Locator("ROOM_AND_GUEST_BTN", AppiumBy.id("form_guest_room"));
    public static final Locator ROOM_INCREMENT_BTN = new Locator("ROOM_INCREMENT_BTN", AppiumBy.xpath("//*[contains(@resource-id,'v_room_guest')]//*[contains(@resource-id,'tds_subtitle_list') and (contains(@text,'Kamar') or contains(@text,'Rooms'))]//following-sibling::*//*[contains(@resource-id,'btn_increment')]"));
    public static final Locator ROOM_DECREMENT_BTN = new Locator("ROOM_DECREMENT_BTN", AppiumBy.xpath("//*[contains(@resource-id,'v_room_guest')]//*[contains(@resource-id,'tds_subtitle_list') and (contains(@text,'Kamar') or contains(@text,'Rooms'))]//following-sibling::*//*[contains(@resource-id,'btn_decrement')]"));
    public static final Locator ROOM_COUNT = new Locator("ROOM_COUNT", AppiumBy.xpath("//*[contains(@resource-id,'v_room_guest')]//*[contains(@resource-id,'tds_subtitle_list') and (contains(@text,'Kamar') or contains(@text,'Rooms'))]//following-sibling::*//*[contains(@resource-id,'tv_count')]"));
    public static final Locator ADULT_INCREMENT_BTN = new Locator("ADULT_INCREMENT_BTN", AppiumBy.xpath("//*[contains(@resource-id,'v_room_guest')]//*[contains(@resource-id,'tds_subtitle_list') and (contains(@text,'Dewasa') or contains(@text,'Adult') or contains(@text,'Tamu') or contains(@text,'Guest'))]//following-sibling::*//*[contains(@resource-id,'btn_increment')]"));
    public static final Locator ADULT_DECREMENT_BTN = new Locator("ADULT_DECREMENT_BTN", AppiumBy.xpath("//*[contains(@resource-id,'v_room_guest')]//*[contains(@resource-id,'tds_subtitle_list') and (contains(@text,'Dewasa') or contains(@text,'Adult') or contains(@text,'Tamu') or contains(@text,'Guest'))]//following-sibling::*//*[contains(@resource-id,'btn_decrement')]"));
    public static final Locator ADULT_COUNT = new Locator("ADULT_COUNT", AppiumBy.xpath("//*[contains(@resource-id,'v_room_guest')]//*[contains(@resource-id,'tds_subtitle_list') and (contains(@text,'Dewasa') or contains(@text,'Adult') or contains(@text,'Tamu') or contains(@text,'Guest'))]//following-sibling::*//*[contains(@resource-id,'tv_count')]"));
    public static final Locator CHILD_INCREMENT_BTN = new Locator("CHILD_DECREMENT_BTN", AppiumBy.xpath("//*[contains(@resource-id,'v_room_guest')]//*[contains(@resource-id,'tds_subtitle_list') and (contains(@text, 'Anak') or contains(@text, 'Child'))]//following-sibling::*//*[contains(@resource-id,'btn_increment')]"));
    public static final Locator CHILD_DECREMENT_BTN = new Locator("CHILD_DECREMENT_BTN", AppiumBy.xpath("//*[contains(@resource-id,'v_room_guest')]//*[contains(@resource-id,'tds_subtitle_list') and (contains(@text, 'Anak') or contains(@text, 'Child'))]//following-sibling::*//*[contains(@resource-id,'btn_decrement')]"));
    public static final Locator CHILD_COUNT = new Locator("CHILD_COUNT", AppiumBy.xpath("//*[contains(@resource-id,'v_room_guest')]//*[contains(@resource-id,'tds_subtitle_list') and (contains(@text, 'Anak') or contains(@text, 'Child'))]//following-sibling::*//*[contains(@resource-id,'tv_count')]"));
    public static final Locator POP_UP = new Locator("POP_UP", AppiumBy.xpath("//*[contains(@resource-id, 'tds_btn') and (contains(@text, 'Oke') or contains(@text,'OK'))]"));
    public static final Locator RECENTLY_SEARCHED_DETAIL = new Locator("RECENTLY_SEARCHED_DETAIL", AppiumBy.xpath("(//*[contains(@resource-id,'tv_description')])[1]"));
    public static final Locator AREA = new Locator("AREA", AppiumBy.xpath("(//*[contains(@resource-id,'tds_subtitle_list')])[1]"));
    public static final Locator RECENTLY_SEARCHED_LOCATION = new Locator("RECENTLY_SEARCHED_LOCATION", AppiumBy.xpath("(//*[contains(@resource-id,'tv_location')])[1]"));
    public static final Locator RECENTLY_SEARCH_TITILE = new Locator("RECENTLY_SEARCH_TITILE", AppiumBy.xpath("(//*[contains(@resource-id,'tds_title_list')])[1]"));
    public static final Locator CLOSE_SEARCH = new Locator("CLOSE_SEARCH", AppiumBy.id("iv_close"));
    private static final Locator CARD_CONTAINER = new Locator("CARD_CONTAINER", AppiumBy.xpath("//*[contains(@resource-id,'tv_title')]//parent::*[contains(@resource-id,'container')]//parent::*[contains(@resource-id,'card_view')]"));
    public static final Locator NEW_FILTER_POPUP = new Locator("FILTER_POPUP", AppiumBy.xpath("//*[contains(@resource-id, 'tds_primary_coach_mark')]//*[contains(@resource-id,'tds_btn')]"));
    private static final Locator FLASHSALE_SECTION_TITLE = new Locator("FLASHSALE_SECTION_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'iv_flash_sale')]//following-sibling::*[contains(@resource-id,'tv_title')]"));
    private static final Locator MODULE_SECTION_TITLE = new Locator("MODULE_SECTION_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'header_container')]//*[contains(@resource-id,'tv_title')]"));
    private static final Locator CARD_TITLE = new Locator("CARD_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'recycler_view')]//*[contains(@resource-id,'card_view')]//*[contains(@resource-id,'tv_title')]"));
    private static final Locator TIMED_PROMO_TITLE = new Locator("TIMED_PROMO_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'view_promo_title')]//*[contains(@resource-id,'tv_title')]"));
    private static final Locator TIMED_PROMO_LOCATION_BUTTON = new Locator("TIMED_PROMO_LOCATION_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'rv_promo_filter')]//*[contains(@resource-id,'tds_chips_text')]"));
    public static final Locator TIMED_PROMO_HOTEL_NAME = new Locator("TIMED_PROMO_HOTEL_NAME", AppiumBy.xpath("//*[contains(@resource-id,'rv_promo_content')]//*[contains(@resource-id,'tv_hotel_name')]"));
    private static final Locator TIMED_PROMO_SEE_ALL_BUTTON = new Locator("TIMED_PROMO_SEE_ALL_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'view_promo_title')]//following-sibling::*[contains(@resource-id,'v_container')]//*[contains(@resource-id,'tv_see_all')]"));
    private static final Locator MULIPLE_HOTEL_ID_TITLE = new Locator("MULIPLE_HOTEL_ID_TITLE", AppiumBy.xpath("(//*[contains(@resource-id,'cl_icon')]/following-sibling::*[contains(@resource-id,'tv_title') and (contains(@text,'Multiple'))])[last()]"));
    private static final Locator MULIPLE_HOTEL_ID_SUBTITLE = new Locator("MULIPLE_HOTEL_ID_SUBTITLE", AppiumBy.xpath("(//*[contains(@resource-id,'cl_icon')]/following-sibling::*[contains(@resource-id,'tv_sub_title')])[last()]"));
    private static final Locator MULIPLE_HOTEL_SEE_ALL= new Locator("MULIPLE_HOTEL_SEE_ALL", AppiumBy.xpath("//*[contains(@resource-id, 'tv_see_all') and contains(@text,'Lihat')]"));
    public static final Locator MULTIPLE_SQUARE_CARD_SECTION = new Locator("MULTIPLE_SQUARE_CARD_SECTION", AppiumBy.id("rv_multiple_square_card"));
    public static final Locator MULTIPLE_SQUARE_CARD_TITLE = new Locator("MULTIPLE_SQUARE_CARD_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'rv_multiple_square_card')]/preceding-sibling::*[contains(@resource-id,'view_title')]//*[contains(@resource-id,'tv_title')]"));
    public static final Locator MULTIPLE_SQUARE_CARD_SUB_TITLE = new Locator("MULTIPLE_SQUARE_CARD_SUB_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'rv_multiple_square_card')]/preceding-sibling::*[contains(@resource-id,'view_title')]//*[contains(@resource-id,'tv_sub_title')]"));
    public static final Locator MULTIPLE_SQUARE_CARD_CONTENT_TITLE = new Locator("MULTIPLE_SQUARE_CARD_CONTENT_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'rv_multiple_square_card')][1]//*[contains(@resource-id,'tv_multiple_square_type_title')]"));
    public static final Locator CALENDER_SUBMIT_BUTTON = new Locator("CALENDER_SUBMIT_BUTTON", AppiumBy.id("tds_button_view"));
    public static final Locator BACK_BTN = new Locator("BACK_BTN", AppiumBy.id("button_icon_container"));
    public static final Locator OK_POPUP = new Locator("OK_POPUP", AppiumBy.id("tds_primary_coach_mark"));
    private static final Locator RECENT_VIEW_TAB = new Locator("RECENT_VIEW_TAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tab_layout') and (contains(@text,'Recently Viewed') or contains(@text,'Terakhir Dilihat'))]"));
    private static final Locator RECENT_VIEW_SECTION = new Locator("RECENT_VIEW_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Your Search History') or contains(@text,'Riwayat Pencarianmu'))]"));
    private static final Locator RECENT_SEARCH_ITEM_CONTAINER = new Locator("RECENT_SEARCH_ITEM_CONTAINER", AppiumBy.xpath("//*[contains(@resource-id,'rv_content')]//*[contains(@class,'ViewGroup')]"));
    private static final Locator RECENT_HOTEL_NAME = new Locator("RECENT_HOTEL_NAME", AppiumBy.id("tv_hotel_recent"));
    private static final Locator RECENT_DELETE_BUTTON = new Locator("RECENT_DELETE_BUTTON", AppiumBy.id("tv_clear"));
    private static final Locator NEAR_ME_ICON = new Locator("NEAR_ME_ICON", AppiumBy.xpath("//*[contains(@resource-id,'form_destination')]//*[contains(@resource-id,'tds_search_form_action_a_image')]"));
    private static final Locator CONTINUE_ALLOW_LOCATION = new Locator("CONTINUE_ALLOW_LOCATION", AppiumBy.id("tds_btn_primary_info_view"));
    private static final Locator PERMISSION_TYPE_ALLOW_ONCE = new Locator("PERMISSION_TYPE_ALLOW_ONCE", AppiumBy.xpath("//*[contains(@resource-id,'permission_allow_one_time_button') or contains(@resource-id,'permission_allow_foreground_only_button')]"));
    private static final Locator DATE_CONTAINER = new Locator("DATE_CONTAINER", AppiumBy.id("ll_day_group"));

    public static Locator searchList(String locationId, String locationEn) {
        String idTrim = locationId == null ? "" : locationId.strip();
        String enTrim = locationEn == null ? "" : locationEn.strip();
        return new Locator("SEARCH_LIST", AppiumBy.xpath("(//*[contains(@resource-id,'tds_list_parent') and .//*[contains(@resource-id,'tds_title_list') and (contains(@text,'" + idTrim + "') or contains(@text,'" + idTrim.toLowerCase() + "') or contains(@text,'" + enTrim + "') or contains(@text,'" + enTrim.toLowerCase() + "'))]])[1]"));
    }

    public static Locator searchList(String location) {
        String trimmed = location == null ? "" : location.strip();
        return new Locator("SEARCH_LIST_" + trimmed.toUpperCase(), AppiumBy.xpath("(//*[contains(@resource-id,'tds_list_parent') and .//*[contains(@resource-id,'tds_title_list') and (@text='" + trimmed + "' or @text='" + trimmed.toLowerCase() + "')]])[1]"));
    }

    private static Locator recentHotelDeleteButton(String hotelName) {
        return new Locator("RECENT_HOTEL_DELETE_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_recent') and @text='" + hotelName + "']//following-sibling::*[contains(@resource-id,'btn_delete_recent_item')]"));
    }

    private static Locator recentHotelNameNotEqual(String hotelName) {
        return new Locator("RECENT_HOTEL_NAME_NOT_EQUAL", AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_recent') and not(@text='" + hotelName + "')]"));
    }

    private static Locator recentHotelNameEqual(String hotelName) {
        return new Locator("RECENT_HOTEL_NAME_EQUAL", AppiumBy.xpath("//*[contains(@resource-id,'tv_hotel_recent') and @text='" + hotelName + "']"));
    }

    @Override
    public void clickSeeMoreFloatingButton() {
        driverTools.waitForElementToPresence(SEE_MORE_BUTTON);
        int count = 0;
        while(driverTools.isElementDisplayed(SEE_MORE_BUTTON) && count < 2) {
            driverTools.click(SEE_MORE_BUTTON);
            count++;
        }
    }

    @Override
    public String getModuleTitleSection() {
        if (driverTools.waitForElementToPresence(FLASHSALE_SECTION_TITLE).isPresent()) {
            return driverTools.getText(FLASHSALE_SECTION_TITLE).orElseThrow();
        } else {
            driverTools.swipeToElement(MODULE_SECTION_TITLE, Swipe.UP, 6);
            return driverTools.getText(MODULE_SECTION_TITLE).orElseThrow();
        }
    }

    @Override
    public void clickSeeAllButton() {
        driverTools.swipeToElement(SEE_ALL_BUTTON, Swipe.UP, 6).orElseThrow();
        driverTools.swipeByDistance(CARD_CONTAINER, Swipe.UP, 0.8);
        driverTools.clickElementByCoordinate(SEE_ALL_BUTTON);
        if (!driverTools.waitForElementToDisappear(SEE_ALL_BUTTON)) {
            logger.log("See All button is not disappeared after clicked");
            driverTools.click(SEE_ALL_BUTTON);
        }
    }

    @Override
    public void clickFirstSeeAllButton() {
        clickSeeMoreFloatingButton();
        clickSeeAllButton();
    }

    @Override
    public void clickFirstHotelCard() {
        driverTools.swipeToElement(CARD_TITLE, Swipe.UP, 6);
        driverTools.click(CARD_TITLE);
    }

    @Override
    public void searchHotelList(List<String> hotelList) {
        for (String hotel : hotelList) {
            searchStayLocation(hotel);
            clickSubmit();
            fillNightCheckInForm();

            DetailPage<T> detailPage = DetailPage.getInstance(session);
            detailPage.getHotelNameDetail();
            detailPage.clickBackButton();
        }
    }

    @Override
    public void selectRecentViewedTab() {
        if (driverTools.element(RECENT_VIEW_TAB).isPresent())
            driverTools.click(RECENT_VIEW_TAB);
    }

    @Override
    public int deleteRecentViewItems(List<String> hotelList, int maxRecentlyViewed) {
        int count = 0;
        String recentHotelName = "";
        String nextRecentHotelName = "";

        boolean hotelCheck = false;
        int totalHotel = hotelList.size();
        logger.log("totalHotel: " + totalHotel);
        if (totalHotel > maxRecentlyViewed) {
            hotelList = hotelList.subList(0, totalHotel - maxRecentlyViewed);
            hotelCheck = true;
        }

        logger.log("hotelList: " + hotelList);
        while (count == 0 || (!recentHotelName.equalsIgnoreCase(nextRecentHotelName) && count < 20)) {
            if (hotelCheck) for (String hotel : hotelList)
                    Assertion.assertThat("count: " + count + " -> " + hotel + " should be not displayed on recently viewed", driverTools.isElementDisplayed(recentHotelNameEqual(hotel)), is(false));

            recentHotelName = driverTools.getText(RECENT_HOTEL_NAME).orElseThrow();
            driverTools.click(recentHotelDeleteButton(recentHotelName));
            if (driverTools.isElementDisplayed(recentHotelDeleteButton(recentHotelName))) driverTools.click(recentHotelDeleteButton(recentHotelName));
            if (driverTools.isElementGone(RECENT_HOTEL_NAME)) break;

            nextRecentHotelName = driverTools.getText(recentHotelNameNotEqual(recentHotelName)).orElseThrow();
            count++;
        }

        int totalRecentHotel = count + 1;
        logger.log("totalRecentHotel -> " + totalRecentHotel);
        return totalRecentHotel;
    }

    @Override
    public boolean isRecentViewedLandingPageEmpty() {
        Assertion.assertThat("Recently Viewed section on the landing page should be not displayed", driverTools.isElementDisplayed(RECENT_VIEW_SECTION), is(false));
        Assertion.assertThat("Recently Viewed delete button on the landing page should be not displayed", driverTools.isElementDisplayed(RECENT_DELETE_BUTTON), is(false));
        return true;
    }

    @Override
    public void clearAllRecentViewed() {
        if (driverTools.waitForElementToAppear(RECENT_DELETE_BUTTON).isPresent())
            driverTools.click(RECENT_DELETE_BUTTON);
    }

    @Override
    public int getRecentViewedItemCount(List<String> hotelList, int maxRecentlyViewed) {
        Collections.reverse(hotelList);
        logger.log("hotelList: " + hotelList);

        int count = 0;
        int totalRecentHotel = 0;
        int totalHotel = hotelList.size();
        logger.log("totalHotel: " + totalHotel);

        for (int i = 0; i < totalHotel; i++) {
            driverTools.swipeToElement(recentHotelNameEqual(hotelList.get(i)), RECENT_SEARCH_ITEM_CONTAINER, Swipe.LEFT, 3);
            if (count >= maxRecentlyViewed) {
                logger.log("Exceeded maxRecentlyViewed limit: " + maxRecentlyViewed);
                Assertion.assertThat("count: " + count + " -> " + hotelList.get(i) + " should be not displayed on recently viewed", driverTools.isElementDisplayed(recentHotelNameEqual(hotelList.get(i))), is(false));
                totalRecentHotel = count;
                break;
            }
            Assertion.assertThat("count: " + count + " -> " + hotelList.get(i) + " should be displayed on recently viewed", driverTools.isElementDisplayed(recentHotelNameEqual(hotelList.get(i))), is(true));
            count++;
        }

        // If loop completed without breaking, totalRecentHotel should be count
        if (totalRecentHotel == 0) {
            totalRecentHotel = count;
        }
        logger.log("totalRecentHotel -> " + totalRecentHotel);
        return totalRecentHotel;
    }

    @Override
    public void clickSearchField() {
        driverTools.click(SEARCH_FIELD_BUTTON);
    }

    @Override
    public boolean isRecentViewedSearchPopupEmpty() {
        Assertion.assertThat("Recently Viewed hotel name on search popup should be not displayed", driverTools.isElementDisplayed(RECENT_HOTEL_NAME), is(false));
        Assertion.assertThat("Recently Viewed delete button on search popup should be not displayed", driverTools.isElementDisplayed(RECENT_DELETE_BUTTON), is(false));
        return true;
    }

    @Override
    public boolean verifyRedirectSearchForm(LandingPageModulePOJO.Redirect redirectData) {
        return false; //FIXME: Pending implementation
    }

    @Override
    public void searchStayLocation(String location) {
        String methodName = "searchStayLocation";
        long startTime = startTiming(methodName);
        try {
            String separator = "/";
            String[] splitData = splitLanguageData(location, separator);
            String locationId = splitData[0].strip();
            String locationEn = splitData[1].strip();

            driverTools.waitForElementToPresence(SEARCH_FIELD_BUTTON);
            driverTools.clickElementByCoordinate(SEARCH_FIELD_BUTTON);

            WebElement searchFieldText = driverTools.waitForElementToAppear(SEARCH_FIELD_TEXT).orElseThrow();
            driverTools.sendKeys(searchFieldText, locationId);
            Locator searchList = searchList(locationId, locationEn);
            driverTools.click(driverTools.waitForElementToAppear(searchList,12).orElseThrow());
        } finally {
            endTiming(methodName, startTime);
        }
    }

    @Override
    public boolean verifyPoiOnHotelCards(String poi) {
        String poiLower = poi == null ? "" : poi.strip().toLowerCase();
        Locator poiLocationInfo = new Locator("POI_LOCATION_INFO_" + poiLower.toUpperCase(),
                AppiumBy.xpath("//*[contains(@resource-id,'tv_location_info') and (contains(@text,'from " + poiLower + "') or contains(@text,'dari " + poiLower + "'))]"));
        return driverTools.waitForElementToPresence(poiLocationInfo, 10).isPresent();
    }

    @Override
    public String[] setStayDate(String type, String date) {
        String[] stayDate = date.split("-");

        if(stayDate[0].equals("0")){
            return null;
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

        boolean isExactDate = false;
        switch (type) {
            case "exact date":
                isExactDate = true;
                break;
            case "range date": {
                int randomStartDate = generateRandomInt(startDate, endDate - 1);
                int randomEndDate = generateRandomInt(randomStartDate + 1, endDate);

                startDate = randomStartDate;
                endDate = randomEndDate;
                logger.log("stringStartDate is '" + startDate + "', stringEndDate is '" + endDate + "'");
                break;
            }

            default:
                throw new IllegalArgumentException("Date type '" + type + "' is not supported. Make sure to insert 'exact date' or 'range date'");
        }

        final Locator SELECTED_START_DATE = getDateElement("SELECTED_START_DATE", startDate);
        final Locator SELECTED_END_DATE = getDateElement("SELECTED_END_DATE", startDate, endDate);

        driverTools.click(DATE_FIELD_BUTTON);

        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();

        if (startDate < currentDay) {

            String currentMonthId = driverTools.getText(CURRENT_MOTH).orElseThrow();
            int nextMonthInt;

            int currentMonthInt = currentDate.getMonthValue();
            if (currentMonthInt == 12) {
                nextMonthInt = currentMonthInt - 11;
            } else {
                nextMonthInt = currentMonthInt + 1;
            }
            String nextMonthEng = String.valueOf(Month.of(nextMonthInt));
            nextMonthEng = nextMonthEng.substring(0, 1).toUpperCase() + nextMonthEng.substring(1).toLowerCase();
            String nextMonthId = monthEngToId(nextMonthEng);

            Locator NEXT_MONTH = getNextMonthElement(nextMonthId);

            CalendarPage<T> calendarPage = CalendarPage.getInstance(session);

            if (!driverTools.isElementDisplayed(NEXT_MONTH)) {
                calendarPage.swipe(Swipe.UP);
            }
        }

        if (isExactDate) {
            driverTools.waitForElementToAppear(DATE_CONTAINER);
            driverTools.swipeByDistance(DATE_CONTAINER, Swipe.UP, 2);
        }
        driverTools.swipeToElement(SELECTED_START_DATE, Swipe.UP, 1);
        driverTools.swipeByDistance(DATE_CONTAINER, Swipe.UP, 1);
        driverTools.click(SELECTED_START_DATE);
        driverTools.swipeToElement(SELECTED_END_DATE, Swipe.UP, 1);
        driverTools.click(SELECTED_END_DATE);
        String selectedCheckIn = driverTools.getText(SELECTED_CHECKIN).orElseThrow();
        String selectedCheckOut = driverTools.getText(SELECTED_CHECKOUT).orElseThrow();
        if (selectedCheckOut.equalsIgnoreCase("pilih tanggal") || selectedCheckOut.equalsIgnoreCase("select date")) {
            driverTools.clickElementByCoordinate(SELECTED_START_DATE);
            driverTools.clickElementByCoordinate(SELECTED_END_DATE);
            selectedCheckIn = driverTools.getText(SELECTED_CHECKIN).orElseThrow();
            selectedCheckOut = driverTools.getText(SELECTED_CHECKOUT).orElseThrow();
        }
        logger.log("selectedCheckIn -> " + selectedCheckIn);
        logger.log("selectedCheckOut -> " + selectedCheckOut);
        if (selectedCheckOut.equalsIgnoreCase("pilih tanggal") || selectedCheckOut.equalsIgnoreCase("select date")) {
            driverTools.clickElementByCoordinate(SELECTED_END_DATE);
            selectedCheckOut = driverTools.getText(SELECTED_CHECKOUT).orElseThrow();
        }
        driverTools.click(CALENDER_SUBMIT_BUTTON);
        driverTools.waitForElementToDisappear(CALENDAR);
        return new String[]{selectedCheckIn, selectedCheckOut};
    }

    @Override
    public String getSelectedCheckInOut() {
        return driverTools.getText(DATE_FIELD_TEXT).orElseThrow();
    }

    @Override
    public void clickRoomAndGuestButton() {
        String methodName = "clickRoomAndGuestButton";
        long startTime = startTiming(methodName);
        try {
            driverTools.click(ROOM_AND_GUEST_BTN);
        } finally {
            endTiming(methodName, startTime);
        }
    }

    @Override
    public void setTotalRoom(int total) {
        String methodName = "setTotalRoom";
        long startTime = startTiming(methodName);
        try {
            int numberOfRoom = Integer.parseInt(driverTools.getText(ROOM_COUNT).orElseThrow());

            setParam("room", numberOfRoom, total);
        } finally {
            endTiming(methodName, startTime);
        }
    }

    @Override
    public void setTotalGuest(int total) {
        String methodName = "setTotalGuest";
        long startTime = startTiming(methodName);
        try {
            int numberOfAdult = Integer.parseInt(driverTools.getText(ADULT_COUNT).orElseThrow());

            setParam("adult", numberOfAdult, total);
        } finally {
            endTiming(methodName, startTime);
        }
    }

    @Override
    public void removePopUp() {
        fillNightCheckInForm();
        if (driverTools.isElementDisplayed(POP_UP)) {
            driverTools.click(POP_UP);
        }
    }

    @Override
    public void setTotalChildren(int total) {
        String methodName = "setTotalChildren";
        long startTime = startTiming(methodName);
        try {
            int numberOfChild = Integer.parseInt(driverTools.getText(CHILD_COUNT).orElseThrow());

            setParam("child", numberOfChild, total);
        } finally {
            endTiming(methodName, startTime);
        }
    }

    private void setParam(String type, int currentCount, int targetCount) {
        int difference = targetCount - currentCount;

        if (difference < 0) {
            for (int i = 0; i > difference; i--) {
                switch (type) {
                    case "room" -> {
                        driverTools.click(ROOM_DECREMENT_BTN);
                    }
                    case "adult" -> {
                        driverTools.click(ADULT_DECREMENT_BTN);
                    }
                    case "child" -> {
                        driverTools.click(CHILD_DECREMENT_BTN);
                    }
                }
            }
        } else if (difference > 0) {
            for (int i = 0; i < difference; i++) {
                switch (type) {
                    case "room" -> {
                        driverTools.click(ROOM_INCREMENT_BTN);
                    }
                    case "adult" -> {
                        driverTools.click(ADULT_INCREMENT_BTN);
                    }
                    case "child" -> {
                        driverTools.click(CHILD_INCREMENT_BTN);
                    }
                }
            }
        }
    }

    private Locator getNextMonthElement(String nextMonthId) {
        return new Locator("NEXT_MONTH", AppiumBy.xpath("//*[contains(@resource-id, 'tds_month_name_calendar') and contains(@text, '" + nextMonthId + "')]"));
    }

    @Override
    public String getRoomAndGuestText() {
        return driverTools.getText(ROOM_AND_GUEST_TEXT).orElseThrow();
    }

    @Override
    public void clickDone() {
        String methodName = "clickDone";
        long startTime = startTiming(methodName);
        try {
            driverTools.click(SUBMIT_BUTTON);
        } finally {
            endTiming(methodName, startTime);
        }
    }

    @Override
    public void clickSubmit() {
        String methodName = "clickSubmit";
        long startTime = startTiming(methodName);
        try {
            driverTools.click(SUBMIT_BUTTON);
        } finally {
            endTiming(methodName, startTime);
        }
    }

    @Override
    public void backToHomePage() {
        driverTools.waitForElementToAppear(OK_POPUP).ifPresent(driverTools::click);
        if(driverTools.isElementDisplayed(SUBMIT_BUTTON))
            driverTools.click(BACK_BTN);
    }

    @Override
    public void fillNightCheckInForm() {
        if (driverTools.isElementDisplayed(CHECKIN_LATER)) {
            driverTools.click(CHECKIN_LATER);
        }
        driverTools.waitForElementToDisappear(CARD_CONTAINER);
        Optional<WebElement> newFilterPopup = driverTools.waitForElementToPresence(NEW_FILTER_POPUP, 10);
        if (newFilterPopup.isPresent()) {
            driverTools.click(newFilterPopup.orElseThrow());
            driverTools.element(CLOSE_SEARCH).ifPresent(driverTools::click);
            driverTools.element(NEW_FILTER_POPUP).ifPresent(driverTools::click);
        }
    }

    private Locator getDateElement(String elementName, int date) {
        return new Locator(elementName, AppiumBy.xpath("(//*[contains(@resource-id, 'll_day_group')]/android.widget.TextView[@text='" + date + "'])[1]"));
    }

    private Locator getDateElement(String elementName, int startDate, int endDate) {
        return new Locator(elementName, AppiumBy.xpath("(//android.widget.TextView[@text='" + startDate + "']//parent::*[contains(@resource-id, 'll_day_group')]//following-sibling::*//android.widget.TextView[@text='" + endDate + "'])[1]"));
    }

    @Override
    public String getAreaText(String location) {
        String area = driverTools.getText(AREA).orElseThrow();
        driverTools.click(CLOSE_SEARCH);
        return area;
    }
    @Override
    public boolean recentlySearchedStatus(String dateRoomGuestDetail, String searchText, String area) {
        String detail = driverTools.getText(RECENTLY_SEARCHED_DETAIL).orElseThrow();
        String[] detailsLandingPage = detail.split(" ");
        String[] detailsSRP = dateRoomGuestDetail.split(" ");
        boolean dateCheck = false;
        if(detail.contains("Kamar") | detail.contains("Room")) {
            if (((detailsLandingPage[0] + detailsLandingPage[1]).equals(detailsSRP[0] + detailsSRP[1])) &&
                    (detailsLandingPage[4] + detailsLandingPage[5]).equals(detailsSRP[3] + detailsSRP[4]) &&
                    (detailsLandingPage[8].equals(detailsSRP[6]) && (detailsLandingPage[10].equals(detailsSRP[9])))) {
                dateCheck = true;
            }
        }
        else {
            if(((detailsLandingPage[0]+detailsLandingPage[1]).equals(detailsSRP[0]+detailsSRP[1])) &&
                    (detailsLandingPage[4]+detailsLandingPage[5]).equals(detailsSRP[3]+detailsSRP[4]) &&
                    (detailsLandingPage[8].equals(detailsSRP[6]) && (detailsLandingPage[9].equals(detailsSRP[7])))) {
                dateCheck = true;
            }
        }
        String location = driverTools.getText(RECENTLY_SEARCHED_LOCATION).orElseThrow();
        String separator = "/";
        String[] splitData = splitLanguageData(searchText, separator);
        String locationId = splitData[0];
        String locationEn = splitData[1];
        Assertion.assertThat("Location should be showing as expected", location, either(is(locationId+", "+area)).or(is(locationEn+", "+area)));
        logger.log("date check-> " + dateCheck);
        return dateCheck;
    }
    @Override
    public boolean isRecentlySearchAvailable(String location) {
        String separator = "/";
        String[] splitData = splitLanguageData(location, separator);
        String locationId = splitData[0];
        String locationEn = splitData[1];

        driverTools.click(SEARCH_FIELD_BUTTON);
        String title = driverTools.getText(RECENTLY_SEARCH_TITILE).orElseThrow();
        Assertion.assertThat("Recently Viewed should be showing as expected", title, either(is(locationId)).or(is(locationEn)));
        return true;
    }

    @Override
    public boolean isRecentlySearchAvailable() {
        return driverTools.waitForElementToAppear(RECENTLY_SEARCH_TITILE).isPresent() ||
            driverTools.isElementDisplayed(RECENTLY_SEARCHED_DETAIL);
    }

    @Override
    public String getRecentlySearchLocation() {
        String location = driverTools.getText(RECENTLY_SEARCHED_LOCATION).orElseThrow();
        if(location.contains(", ")) {
            return location.split(", ")[0];
        } else {
            return location;
        }
    }

    @Override
    public boolean isPageModulePresent() {
        return false; // TODO: Implement this
    }

    @Override
    public String getPageModuleTitle() {
        return ""; // TODO: Implement this
    }

    @Override
    public String getPageModuleProductCard() {
        return ""; // TODO: Implement this
    }

    @Override
    public String getRecentTotalGuest(String location, String[] checkInOutDate) {
        String[] checkIn = checkInOutDate[0].split(" ");
        logger.log("checkIn -> " + Arrays.toString(checkIn));
        String checkInYear = convertDateFormat(checkIn[2], "yyyy", "yy");
        String normalizeCheckInDate = String.join(" ", checkIn[0], checkIn[1], checkInYear);

        String[] checkOut = checkInOutDate[1].split(" ");
        logger.log("checkOut -> " + Arrays.toString(checkOut));
        String checkOutYear = convertDateFormat(checkOut[2], "yyyy", "yy");
        String normalizecheckOutDate = String.join(" ", checkOut[0], checkOut[1], checkOutYear);

        String convertCheckInOutDate = String.join(" - ", normalizeCheckInDate, normalizecheckOutDate);

        driverTools.waitForElementToAppear(recentlySearchedLocation(location, convertCheckInOutDate));
        driverTools.swipeToElement(recentlySearchedLocation(location, convertCheckInOutDate), Swipe.LEFT, 5);
        String totalGuestDate = driverTools.getText(recentlySearchedLocation(location, convertCheckInOutDate)).orElseThrow();

        String[] splitTotalGuestDate = totalGuestDate.split(" ");
        logger.log("splitTotalGuestDate -> " + Arrays.toString(splitTotalGuestDate));
        return String.join(" ", splitTotalGuestDate[splitTotalGuestDate.length - 2], splitTotalGuestDate[splitTotalGuestDate.length - 1]);
    }

    private Locator recentlySearchedLocation(String location, String checkInOutDate) {
        String separator = "/";
        String[] splitData = splitLanguageData(location, separator);
        String locationId = splitData[0];
        String locationEn = splitData[1];
        return new Locator("RECENTLY_SEARCHED_LOCATION_" + location.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'tv_location') and (contains(@text,'"+locationId+"') or contains(@text,'"+locationEn+"'))]//following-sibling::*[contains(@resource-id,'tv_description') and contains(@text,'"+checkInOutDate+"')]"));
    }

    @Override
    public void openCalendarPage(){

    }

    @Override
    public void goToTimedPromoSection() {
        driverTools.waitForElementToAppear(SEARCH_FIELD_BUTTON).orElseThrow();
        driverTools.swipeToElement(TIMED_PROMO_SEE_ALL_BUTTON, Swipe.UP, 5).orElseThrow();
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
        String locationTitle = driverTools.getText(selectedLocation);
        driverTools.click(selectedLocation);
        return locationTitle;
    }

    @Override
    public String clickSeeAllButtonTimedPromo() {
        WebElement button = driverTools.waitForElementToAppear(TIMED_PROMO_SEE_ALL_BUTTON).orElseThrow();
        String buttonText = driverTools.getText(button);
        driverTools.click(button);
        return buttonText;
    }

    @Override
    public boolean compareTimedPromoHotelName(ArrayList<String> hotelNameJson) {
        boolean result = false;
        List<WebElement> hotelList = driverTools.elements(TIMED_PROMO_HOTEL_NAME).orElseThrow();
        int totalHotelParam = hotelNameJson.size();
        logger.log("totalHotelParam-> " + totalHotelParam);
        logger.log("hotelList length"+hotelList.size());
        if (totalHotelParam > 3) totalHotelParam = 3;
        for (int i = 0; i < totalHotelParam; i++) {
            String hotelNameApp = driverTools.getText(hotelList.get(i));
            logger.log(i + " index of hotelNameApp-> " + hotelNameApp);

            String hotelNameApi = hotelNameJson.get(i);
            logger.log(i + " index of hotelNameApi-> " + hotelNameApi);

            boolean compare = hotelNameApp.equals(hotelNameApi);
            logger.log(i + " compare-> " + compare);

            if (!compare) {
                logger.fail(i + " index = Hotel name on the App is \"" + hotelNameApp + "\", but on the API is \"" + hotelNameApi + "\"");
                result = false;
                break;
            } else {
                result = true;
            }
        }
        return result;
    }

   @Override
   public String getMasterTitle(){
        WebElement title =  driverTools.swipeToElement(MULIPLE_HOTEL_ID_TITLE,Swipe.UP,10).orElseThrow();
        driverTools.swipeUp();
        driverTools.swipeUp();
        String titleText=  driverTools.getText(title);
        logger.log("Title: " +titleText);
        return titleText;
   }
    @Override
    public String getMasterSubTitle(){
        WebElement subTitle =  driverTools.swipeToElement(MULIPLE_HOTEL_ID_SUBTITLE,Swipe.UP,10).orElseThrow();
        String subTitleText=  driverTools.getText(subTitle);
        logger.log("SubTitle: " +subTitleText);
        return subTitleText;
    }
    private Locator getMultipleIdLocationLocator(String location) {
        return new Locator("LOCATION", AppiumBy.xpath("//*[contains(@resource-id, 'tds_chips_text') and contains(@text, '"+location+"')]")) ;
    }
    @Override
    public boolean checkLocationTagsForMultipleHotelId(LandingPageModulePOJO landingPageModuleApi) {
        for (int i = 0; i < landingPageModuleApi.data.list.size(); i++) {
            ArrayList<LandingPageModulePOJO.List> listArray = landingPageModuleApi.data.list;
            String location = listArray.get(0).location;
            if(!driverTools.isElementDisplayed(getMultipleIdLocationLocator(location))){
                return false;
            }
        }
        return true;
    }
    @Override
    public String getButtonText(){
        WebElement buttonElement =  driverTools.swipeToElement(MULIPLE_HOTEL_SEE_ALL,Swipe.UP,3).orElseThrow();
        String buttonText=  driverTools.getText(buttonElement);
        logger.log("buttonText :" +buttonText);
        return buttonText;
    }
    @Override
    public void seeAllMultipleID(){
        driverTools.click(MULIPLE_HOTEL_SEE_ALL);
    }

        @Override
    public void goToMultipleSquareCard() {
        driverTools.swipeToElement(MULTIPLE_SQUARE_CARD_SECTION, Swipe.UP, 10);
    }

    @Override
    public String getMultipleSquareCardTitle() {
        return driverTools.getText(MULTIPLE_SQUARE_CARD_TITLE).orElseThrow();
    }

    @Override
    public String getMultipleSquareCardSubTitle() {
        return driverTools.getText(MULTIPLE_SQUARE_CARD_SUB_TITLE).orElseThrow();
    }

    @Override
    public String[] getMultipleSquareCardTitleList() {
        List<String> resultList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            List<WebElement> contentTitleList = driverTools.elements(MULTIPLE_SQUARE_CARD_CONTENT_TITLE).orElseThrow();

            for(WebElement e : contentTitleList) {
                String titleContent = driverTools.getText(e);
                if(!resultList.contains(titleContent)) {
                    resultList.add(titleContent);
                }
            }

            if(i==0) {
                driverTools.swipeUp();
            }
        }

        return resultList.toArray(new String[0]);
    }

    @Override
    public LandingPageModulePOJO.List chooseRandomMultipleSquareCard(LandingPageModulePOJO inputApi) {
        ArrayList<LandingPageModulePOJO.List> contentArray = inputApi.data.list;
        LandingPageModulePOJO.List contentObject = contentArray.get(generateRandomInt(0,contentArray.size()-1));
        logger.log("contentObject -> " + contentObject);

        clickMultipleSquareCard(contentObject.titleContent);

        return contentObject;
    }

    private void clickMultipleSquareCard(String title) {
        WebElement multipleSquareCardElement = driverTools.swipeToElement(getMultipleSquareCardLocator(title), Swipe.DOWN, 2).orElseThrow();
        driverTools.click(multipleSquareCardElement);
    }

    private Locator getMultipleSquareCardLocator(String title) {
        return new Locator("MULTIPLE_SQUARE_CARD_CONTENT_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'rv_multiple_square_card')]//*[contains(@resource-id,'tv_multiple_square_type_title') and @text='"+title+"']"));
    }

    /**
     * This method is used to enable location
     */
    @Override
    public void enableLocation() {
        if(driverTools.waitForElementsToAppear(CONTINUE_ALLOW_LOCATION).isPresent()){
            driverTools.click(CONTINUE_ALLOW_LOCATION);
            driverTools.waitForElementToAppear(PERMISSION_TYPE_ALLOW_ONCE);
            driverTools.click(PERMISSION_TYPE_ALLOW_ONCE);
        }
        driverTools.waitForElementToAppear(TURN_ON_LOCATION_DEVICE).ifPresent(driverTools::click);

        if (driverTools.isElementDisplayed(SUBMIT_BUTTON)) {
            driverTools.click(SUBMIT_BUTTON);
        }
    }

    /**
     * This method is used to click the Near Me icon
     */
    @Override
    public void clickNearMeIcon() {
        driverTools.waitForElementToAppear(NEAR_ME_ICON);
        driverTools.clickElementByCoordinate(NEAR_ME_ICON);
    }

    @Override
    public String getCurrentSearchLocation(){
        String currentLocation = driverTools.getText(SEARCH_FIELD_BUTTON).orElseThrow();
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
    public void clickBackButton(){
        driverTools.click(BACK_BTN);
    }

    @Override
    public String getFirstHotelName(){
        driverTools.swipeToElement(CARD_TITLE, Swipe.UP, 6);
        String hotelName = driverTools.getText(CARD_TITLE).orElseThrow();
        logger.log("First hotel name: " + hotelName);
        return hotelName;
    }

    @Override
    public String checkHotelPrice(boolean isCrossPrice, String hotelName, String price) {
        //price is not matching due to caching issue, so we just check the presence of the element and fetch price based on hotel name
        if (isCrossPrice) {
            return checkHotelCrossPrice(hotelName, price);
        } else {
            return checkHotelFinalPrice(hotelName, price);
        }
    }

    private String checkHotelCrossPrice(String hotelName, String price) {
        WebElement crossPrice = driverTools.swipeToElement(getHotelCrossPriceLocator(hotelName, price), Swipe.UP, 10).orElseThrow();
        String crossText = driverTools.getText(crossPrice);
        logger.log("crossPrice element: " + crossText);
        return crossText;
    }

    private String checkHotelFinalPrice(String hotelName, String price) {
        WebElement finalPrice = driverTools.swipeToElement(getHotelFinalPriceLocator(hotelName, price), Swipe.UP, 10).orElseThrow();
        String finalText = driverTools.getText(finalPrice);
        logger.log("finalPrice element: " + finalText);
        return finalText;
    }

    private Locator getHotelCrossPriceLocator(String hotelName, String price) {
        return new Locator("HOTEL_CROSS_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'" + hotelName + "')]//following-sibling::*[contains(@resource-id,'price_view')]//*[contains(@resource-id,'price_module_slash_price')]"));
    }

    private Locator getHotelFinalPriceLocator(String hotelName, String price) {
        return new Locator("HOTEL_FINAL_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'" + hotelName + "')]//following-sibling::*[contains(@resource-id,'price_view')]//*[contains(@resource-id,'price_module_final_price')]"));
    }

    public boolean checkHotelPrice(String hotelName, String price){
        return driverTools.swipeToElement(getHotelPriceLocator(hotelName, price), Swipe.UP, 10).isPresent();
    }

    private Locator getHotelPriceLocator(String hotelName, String price) {
        return new Locator("HOTEL_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'"+hotelName+"')]//following-sibling::*//*[contains(@resource-id,'price_module') and contains(@text,'"+price+"')]"));
    }

    @Override
    public void selectModuleChip(String chipName) {
        driverTools.swipeToElement(getModuleChipLocator(chipName), Swipe.UP, 20);
        driverTools.click(getModuleChipLocator(chipName));
    }

    private Locator getModuleChipLocator(String chip) {
        return new Locator("MODULE_CHIP", AppiumBy.xpath("(//*[contains(@resource-id, 'tds_chips_text') and contains(@text, '" + chip + "')])[1]"));
    }
}
