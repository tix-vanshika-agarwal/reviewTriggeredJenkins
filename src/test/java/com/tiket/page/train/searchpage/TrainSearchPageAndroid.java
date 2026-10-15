package com.tiket.page.train.searchpage;


import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.common.nftPopup.NFTCommonPopup;
import com.tiket.pojo.train.TrainClass;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.Matchers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;


public class TrainSearchPageAndroid <T extends WebDriver> extends TrainSearchPage<T> {


    public static final Locator CROUSEL_TEXT = new Locator("CROUSEL_TEXT", AppiumBy.id("tv_main_title"));
    public static final Locator ORIGIN = new Locator("Origin", AppiumBy.id("sf_origin"));
    public static final Locator SKIP_POPUP = new Locator("SKIP_POPUP", AppiumBy.id("tds_link_coach_mark"));
    public static final Locator DESTINATION = new Locator("Destination", AppiumBy.id("sf_destination"));
    public static final Locator SEARCH_BUTTON = new Locator("SearchButton", AppiumBy.id("tds_button_view"));
    public static final Locator LOCATION_SWITCH = new Locator("LocationSwitch", AppiumBy.id("btn_reverse"));
    public static final Locator RESULT_VIEW = new Locator("ResultView", AppiumBy.id("cl_toolbar_header"));
    public static final Locator ORIGIN_SEARCH_FIELD = new Locator("ORIGIN_SEARCH_FIELD",AppiumBy.id("tds_search_et"));
    public static final Locator FIRST_STATION = new Locator("FIRST_STATION", AppiumBy.xpath("(//*[contains(@resource-id,'recycler_view')]/android.view.ViewGroup)[1]"));
    public static final Locator RETURN_CALENDAR = new Locator("RETURN_CALENDAR",AppiumBy.xpath("//*[contains(@resource-id,'sf_booking_hour')]//android.widget.TextView[contains(@resource-id,'tds_search_form_subtitle')]"));
    public static final Locator DEPART_CALENDAR = new Locator("DEPART_CALENDAR",AppiumBy.xpath("//*[contains(@resource-id,'sf_booking_hour')]//android.widget.TextView[contains(@resource-id,'tds_search_form_title')]"));
    public static final Locator TRIP_TOGGLE = new Locator("TRIP_TOGGLE",AppiumBy.id("tds_toggle_group"));
    public static final Locator ADD_PASSENGER = new Locator("ADD_PASSENGER",AppiumBy.id("sf_passenger"));
    public static final Locator PASSENGER_SAVE = new Locator("PASSENGER_SAVE",AppiumBy.id("tds_button_view"));
    public static final Locator TRAIN_1 = new Locator("Train_1", AppiumBy.id("tv_train_name"));
    public static final Locator FILTER = new Locator("Filter", AppiumBy.id("chip_filter_sort"));
    public static final Locator SAVE_FILTER = new Locator("Save Filter", AppiumBy.id("btn_action_bottom"));
    public static final Locator TRAIN_CLASS = new Locator( "TRAIN_CLASS", AppiumBy.id("tv_train_class"));
    public static final Locator TRAIN_NAME = new Locator("TRAIN_NAME", By.xpath("//div[contains(@class,'DetailReservation_flight_name_info')]//span[1]"));
    public static final Locator SEE_ALL_NAME = new Locator( "SEE_ALL_NAME", AppiumBy.id("btn_action"));
    public static final Locator SAVE_TRAIN_NAME = new Locator( "SAVE_TRAIN_NAME", AppiumBy.id("tds_btn_apply"));
    public static final Locator SKIP_COACHMARK = new Locator( "SKIP_COACHMARK", AppiumBy.id("tds_link_coach_mark"));
    public static final Locator BOOKING_FORM_PAGE = new Locator( "BOOKING_FORM_PAGE", AppiumBy.id("ps_booking"));
    public static final Locator PRICE = new Locator( "PRICE", AppiumBy.id("tv_price"));
    private static final Locator CARD_DETAIL_WITH_NUDGES_CHEVRON_BTN = new Locator("CARD_DETAIL_WITH_NUDGES_CHEVRON_BTN", AppiumBy.xpath("(//android.widget.ImageView[contains(@resource-id,\"ic_see_detail\") and (preceding-sibling::*[contains(@resource-id,'lbl_top_info')] or following-sibling::*[contains(@resource-id,'lbl_in_card_label')] )])[1]"));
    private static final Locator TRAIN_DETAIL_TOTAL_PRICE = new Locator("TRAIN_DETAIL_TOTAL_PRICE", AppiumBy.id("tv_total_value"));
    private static final Locator TRAIN_DETAIL_PRICE_TAB = new Locator("TRAIN_DETAIL_PRICE_TAB", AppiumBy.xpath("(//android.widget.TextView[contains(@resource-id,'tds_title_tab_layout')])[2]"));
    private static final Locator TRAIN_DETAIL_PRICE_BREAKDOWN = new Locator("TRAIN_DETAIL_PRICE_BREAKDOWN", AppiumBy.id("tv_price"));
    private static final Locator TRAIN_DETAIL_SELECT_BTN = new Locator("TRAIN_DETAIL_SELECT_BTN", AppiumBy.id("tds_button_view"));
    private static final Locator CROSS_SELL_BTN_PARTIAL = new Locator("CROSS_SELL_BTN_PARTIAL", AppiumBy.xpath("//*[contains(@resource-id,'tds_btn') and contains(@text,'Search bus and shuttle') or  contains(@text,'Cari bus dan travel')]"));
    private static final Locator CROSS_SELL_PARTIAL_BANNER = new Locator("CROSS_SELL_PARTIAL_BANNER", AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_action_banner') and contains(@text,'bus and shuttle') or  contains(@text,'bus dan travel')]"));
    private static final Locator SAVE_BUTTON_CALENDAR = new Locator("SAVE_BUTTON_CALENDAR", AppiumBy.id("tds_button_view"));
    public static final Locator SELECTED_DATE_CALENDER = new Locator("SELECTED_DATE_CALENDER",AppiumBy.id("tds_calendar_start_date"));
    public static final Locator CALENDAR_SCROLLABLE = new Locator("CALENDAR_SCROLLABLE",AppiumBy.id("tds_calendar_month_rv"));
    public static final Locator ADULT_COUNT = new Locator("ADULT_COUNT", AppiumBy.xpath("//*[contains(@resource-id,'qe_adult')]//*[contains(@resource-id,'tv_count')]"));
    public static final Locator INFANT_COUNT = new Locator("INFANT_COUNT", AppiumBy.xpath("//*[contains(@resource-id,'qe_infant')]//*[contains(@resource-id,'tv_count')]"));
    public static final Locator ADULT_MINUS = new Locator("ADULT_MINUS",AppiumBy.xpath("//*[contains(@resource-id,'qe_adult')]//*[contains(@resource-id,'btn_decrement')]"));
    public static final Locator INFANT_MINUS = new Locator("INFANT_MINUS",AppiumBy.xpath("//*[contains(@resource-id,'qe_infant')]//*[contains(@resource-id,'tv_count')]"));
    public static final Locator ADD_PASSENGER_ADULT = new Locator("ADD_PASSENGER_ADULT",AppiumBy.xpath("(//*[contains(@resource-id,'btn_increment')])[1]"));
    public static final Locator ADD_PASSENGER_INFANT = new Locator("ADD_PASSENGER_INFANT",AppiumBy.xpath("(//*[contains(@resource-id,'btn_increment')])[2]"));
    public static final Locator SUBTRACT_PASSENGER_ADULT = new Locator("SUBTRACT_PASSENGER_ADULT",AppiumBy.xpath("(//*[contains(@resource-id,'btn_decrement')])[1]"));
    public static final Locator SUBTRACT_PASSENGER_INFANT = new Locator("SUBTRACT_PASSENGER_INFANT",AppiumBy.xpath("(//*[contains(@resource-id,'btn_decrement')])[2]"));
    public static final Locator PICKER_WHEEL_CLOSE = new Locator("PICKER_WHEEL_CLOSE",AppiumBy.id("iv_close"));
    private static final Locator SELECTED_DATE = new Locator("SELECTED_DATE",AppiumBy.xpath("//*[contains(@resource-id,'sf_booking_hour')]//android.widget.TextView[contains(@resource-id,'tds_search_form_title')]"));
    private static final Locator CLEAR_RECENT_SEARCH = new Locator("CLEAR_RECENT_SEARCH", AppiumBy.id("tv_recent_search_clear"));
    private static final Locator RETURN_DATE_FLAG = new Locator("RETURN_DATE_FLAG", AppiumBy.xpath("//*[contains(@resource-id,'tds_search_form_subtitle') and contains(@text,',')]"));
    private static final Locator ROUND_TRIP_TOGGLE  = new Locator("ROUND_TRIP_TOGGLE", AppiumBy.xpath("//*[contains(@resource-id, 'tds_toggle_circle')]"));
    private static final Locator RECENT_SEARCH_ORIGIN = new Locator("RECENT_SEARCH_ORIGIN", AppiumBy.id("tv_origin_name"));
    private static final Locator RECENT_SEARCH_DESTINATION = new Locator("RECENT_SEARCH_DESTINATION", AppiumBy.id("tv_destination_name"));
    private static final Locator RECENT_SEARCH_DATE = new Locator("RECENT_SEARCH_DATE", AppiumBy.id("tv_date"));
    private static final Locator RECENT_SEARCH_PAX_INFO = new Locator("RECENT_SEARCH_PAX_INFO", AppiumBy.id("tv_passenger"));

    public TrainSearchPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void openTrainPage() {
//        driverTools.get("tiket://kereta-api/");
        driverTools.elements(CROUSEL_TEXT).orElseThrow().stream()
                .filter(element -> element.getText().equals("Kereta Api"))
                .findFirst()
                .ifPresentOrElse(driverTools::click, ()->{throw new DriverToolsException("Failed to open trains page");});
        driverTools.waitForElementToAppear(SEARCH_BUTTON);
    }

    @Override
    public void selectOrigin(String stationName) {
        driverTools.waitForElementToPresence(SKIP_POPUP, 15).ifPresent(driverTools::click);
        driverTools.click(ORIGIN);
        logger.log("Searching station: "+stationName);
        driverTools.sendKeys(ORIGIN_SEARCH_FIELD,stationName);
        driverTools.click(FIRST_STATION);
    }

    @Override
    public void selectDestination(String stationName) {
        driverTools.click(DESTINATION);
        logger.log("Searching station: "+stationName);
        driverTools.sendKeys(ORIGIN_SEARCH_FIELD,stationName);
        driverTools.click(FIRST_STATION);
    }

    @Override
    public String selectOriginGetStationName(String stationName) {
        driverTools.click(ORIGIN);
        logger.log("Searching station: "+stationName);
        driverTools.sendKeys(ORIGIN_SEARCH_FIELD,stationName);
        String originCityName = driverTools.getText(getCityNameStationCode(stationName)).orElseThrow();
        driverTools.click(FIRST_STATION);
        return originCityName;
    }

    @Override
    public String selectDestinationGetStationName(String stationName) {
        driverTools.click(DESTINATION);
        logger.log("Searching station: "+stationName);
        driverTools.sendKeys(ORIGIN_SEARCH_FIELD,stationName);
        String destinationCityName = driverTools.getText(getCityNameStationCode(stationName)).orElseThrow();
        driverTools.click(FIRST_STATION);
        return destinationCityName;
    }

    private Locator getCityNameStationCode(String stationName) {
        throw new NotImplementedException("Android getCityNameStationCode(String stationName)"); //TODO: Implement Method
    }

    @Override
    public void selectDate(String departDate,String returnDate) {
        driverTools.click(DEPART_CALENDAR);
        logger.log("Departing on: "+ departDate);
        clickDate(departDate);
        logger.log("returning on: "+ returnDate);
        clickDate(returnDate);
        driverTools.click(SAVE_BUTTON_CALENDAR);
    }

    private void clickDate(String date){
        Swipe direction = getDirection(date);
        logger.log("direction : " + direction);

        String year = CoreFunctions.getSectionOfString(date," ", 2);
        String monthEng = CoreFunctions.getSectionOfString(date," ", 1);
        String monthId = CoreFunctions.monthEngToId(monthEng);
        String day= CoreFunctions.getSectionOfString(date," ", 0);
        Locator dateLocator = getDateLocator(monthId,monthEng,day);

        for(int i = 10; i > 0; i--){
            if(driverTools.waitForElementToAppear(dateLocator,1).isPresent()){
                driverTools.click(dateLocator);
                break;
            }
            if(direction == Swipe.UP)
                driverTools.swipeUp(CALENDAR_SCROLLABLE);
            else
                driverTools.swipeDown(CALENDAR_SCROLLABLE);
        }
    }

    private Swipe getDirection(String dateToSelect){
        WebElement selectedDateElement = driverTools.waitForElementToAppear(SELECTED_DATE_CALENDER).orElseThrow();
        String selectedDate = selectedDateElement.getText();

        logger.log("date to select : " + dateToSelect);
        logger.log("selected date : " + selectedDate);

        String[] dateToSelectArr = dateToSelect.split(" ");
        int dateToSelectDay = Integer.parseInt(dateToSelectArr[0]);
        String dateToSelectMonth = dateToSelectArr[1];
        int dateToSelectYear = Integer.parseInt(dateToSelectArr[2]);

        String[] selectedDateArr = selectedDate.split(" ");
        int selectedDateDay = Integer.parseInt(selectedDateArr[0]);
        String selectedDateMonth = selectedDateArr[1];
        int SelectedDateYear = Integer.parseInt(selectedDateArr[2]);

        int selectedMonthNum = CoreFunctions.monthToMonthNumber(selectedDateMonth);
        logger.log("selected month number: " + selectedMonthNum);
        int dateToSelectMonthNum = CoreFunctions.monthToMonthNumber(dateToSelectMonth);
        logger.log("date to select month number: " + dateToSelectMonthNum);

        int yearDif = dateToSelectYear - SelectedDateYear;
        if(yearDif == 0){
            int monthDif = dateToSelectMonthNum - selectedMonthNum;
            if(monthDif == 0){
                return dateToSelectDay - selectedDateDay < 0 ? Swipe.DOWN: Swipe.UP;
            }
            return monthDif < 0 ? Swipe.DOWN : Swipe.UP;
        }
        return yearDif < 0 ? Swipe.DOWN : Swipe.UP;
    }

    private Locator getDateLocator(String monthId, String monthEng, String day){
        return new Locator("Date Locator", AppiumBy.xpath("//*[contains(@resource-id,'month_name') and (contains(@text,'"+monthId+"') or contains(@text,'"+monthEng+"'))]//following-sibling::*//*[contains(@resource-id,'day')]//*[@text='"+day+"']"));
    }

    @Override
    public void selectDate(String departureDate) {
        driverTools.click(DEPART_CALENDAR);
        logger.log("Departing on: "+ departureDate);
        clickDate(departureDate);
        driverTools.click(SAVE_BUTTON_CALENDAR);
    }
    @Override
    public void searchTrain() {
        driverTools.click(SEARCH_BUTTON);
    }

    @Override
    public void switchOriginAndDestination() {
        driverTools.click(LOCATION_SWITCH);
    }

    @Override
    public String[] getOriginAndDestination() {
        String origin = driverTools.getText(ORIGIN).orElseThrow();
        String destination = driverTools.getText(DESTINATION).orElseThrow();
        return new String[] {origin, destination};
    }

    @Override
    public boolean isResultViewLoaded() {
        driverTools.waitForElementToAppear(RESULT_VIEW);
        return driverTools.isElementDisplayed(RESULT_VIEW);
    }
    
    @Override
    public void backToHomePage(){}

    @Override
    public void toggleTripType(String tripType) {
        boolean current_state = driverTools.waitForElementToAppear(RETURN_CALENDAR, 3).isPresent();
        tripType = tripType.toLowerCase();
        switch (tripType) {
            case "roundtrip" -> {
                if (!current_state) driverTools.click(TRIP_TOGGLE);
            }
            case "oneway" -> {
                if (current_state) driverTools.click(TRIP_TOGGLE);
            }
        }
    }

    /**
     * Adds passengers to the search form.
     * <p>
     * This method clicks the "Add Passenger" button, waits for the passenger picker wheel to appear,
     * and then sets the number of adults and infants to the provided values. It then clicks the "Save" button
     * to save the passenger count.
     *</p>
     * @param adults The number of adults to add.
     * @param infants The number of infants to add.
     */
    @Override
    public void addPassenger(int adults, int infants){
        driverTools.click(ADD_PASSENGER);
        driverTools.waitForElementToAppear(PICKER_WHEEL_CLOSE).orElseThrow();
        setAddOrSubtractPassenger(adults,"adult");
        setAddOrSubtractPassenger(infants,"infant");
        driverTools.click(PASSENGER_SAVE);
    }

    /**
     * Sets the number of passengers of the specified type.
     * <p>
     * This method sets the number of passengers of the specified type (adult or infant) to the provided count.
     * It first retrieves the current passenger count, calculates the difference between the current count and the
     * desired count, and then clicks the "Add" or "Subtract" button the appropriate number of times to reach the desired count.
     * </p>
     * @param paxCount The desired number of passengers.
     * @param paxType The type of passenger (adult or infant).
     */
    private void setAddOrSubtractPassenger(int paxCount,String paxType){
        Locator count = getPassengerCountLocator(paxType);
        Locator increasePassenger = getPassengerAddSubtractButtonLocator(paxType,"increment");
        Locator decreasePassenger = getPassengerAddSubtractButtonLocator(paxType,"decrement");

        int currentCount = Integer.parseInt(driverTools.getText(count).orElseThrow());
        int toAdd = paxCount-currentCount;

        boolean increase = true;
        if (toAdd < 0){
            increase = false;
            toAdd *= -1;
        }

        for (int i=0; i<toAdd; i++) {
            if(increase){
                driverTools.clickElementByCoordinate(increasePassenger);
            }else{
                driverTools.clickElementByCoordinate(decreasePassenger);
            }
        }

        currentCount = Integer.parseInt(driverTools.getText(count).orElseThrow());

        Assertion.assertThat("Correct Passenger Amount Added " + paxType + " : " + currentCount,
                currentCount==paxCount, is(true));
    }

    /**
     * Returns the locator for the passenger count of the specified type.
     * @param passengerType The type of passenger (adult or infant).
     * @return The locator for the passenger count.
     */
    private Locator getPassengerCountLocator(String passengerType){
        if (passengerType.equals("adult")) {
            return ADULT_COUNT;
        } else if (passengerType.equals("infant")) {
            return INFANT_COUNT;
        } else {
            throw new IllegalArgumentException("Invalid passenger type");
        }
    }

    /**
     * Returns the locator for the "Add" or "Subtract" button for the specified passenger type.
     * @param passengerType The type of passenger (adult or infant).
     * @param action The action to perform (increment or decrement).
     * @return The locator for the "Add" or "Subtract" button.
     */
    private Locator getPassengerAddSubtractButtonLocator(String passengerType, String action){
        Map<String, Map<String, Locator>> locatorMap = Map.of(
                "adult", Map.of(
                        "increment", ADD_PASSENGER_ADULT,
                        "decrement", SUBTRACT_PASSENGER_ADULT
                ),
                "infant", Map.of(
                        "increment", ADD_PASSENGER_INFANT,
                        "decrement", SUBTRACT_PASSENGER_INFANT
                )
        );

        if (!locatorMap.containsKey(passengerType) || !locatorMap.get(passengerType).containsKey(action)) {
            throw new IllegalArgumentException("Invalid passenger type or action");
        }

        return locatorMap.get(passengerType).get(action);
    }

    private Locator getTrainSelectLocatorByIndex(int index){
        return new Locator("Train_"+index, AppiumBy.xpath("(//*[contains(@resource-id,'tv_train_name')])["+index+"]"));
    }

    @Override
    public void selectTrainViaIndex(int index){
        driverTools.swipeDown();
        driverTools.waitForElementToAppear(getTrainSelectLocatorByIndex(index));
        driverTools.click(getTrainSelectLocatorByIndex(index));
        driverTools.waitForElementToAppear(BOOKING_FORM_PAGE);
    }

    @Override
    public String getSelectedDate(TrainAndWhooshGeneralData trainAndWhooshGeneralData) {
        String date = driverTools.getText(SELECTED_DATE).orElseThrow();
        date = date.split(", ")[1];
        String[] dateArr = date.split(" ");
        int day = Integer.parseInt(dateArr[0]);
        if(day<10){
            dateArr[0] = String.valueOf(day);
        }
        date = dateArr[0] + " " + dateArr[1];
        return date;
    }

    @Override
    public boolean verifyRecentlySearch(TrainAndWhooshGeneralData trainAndWhooshGeneralData) {
        driverTools.swipeToElement(CLEAR_RECENT_SEARCH, Swipe.UP,1);
        boolean isRecentSearchVisible = driverTools.waitForElementToAppear(CLEAR_RECENT_SEARCH).isPresent();
        logger.log("is recent search visible: "+isRecentSearchVisible);

        WebElement recentOrigin = driverTools.waitForElementToAppear(RECENT_SEARCH_ORIGIN,5).orElseThrow();
        String recentOriginText = driverTools.getText(recentOrigin).replaceAll("\\s*\\(.*\\)$", "").trim().toLowerCase();
        boolean isRecentOriginVisible = recentOriginText.contains(trainAndWhooshGeneralData.originStation);
        logger.log("is recent origin visible: "+isRecentOriginVisible + " ,expected: "+ trainAndWhooshGeneralData.originStation + " ,actual: "+recentOriginText);
        Assertion.assertThat("recent origin is correct",recentOriginText,containsStringIgnoringCase(trainAndWhooshGeneralData.originStation));

        WebElement recentDestination = driverTools.waitForElementToAppear(RECENT_SEARCH_DESTINATION,5).orElseThrow();
        String recentDestinationText = driverTools.getText(recentDestination).replaceAll("\\s*\\(.*\\)$", "").trim().toLowerCase();
        boolean isRecentDestinationVisible = recentDestinationText.contains(trainAndWhooshGeneralData.destinationStation);
        logger.log("is recent destination visible: "+isRecentDestinationVisible + " ,expected: "+ trainAndWhooshGeneralData.destinationStation + " ,actual: "+recentDestinationText);
        Assertion.assertThat("recent destination is correct",recentDestinationText,containsStringIgnoringCase(trainAndWhooshGeneralData.destinationStation));

        WebElement recentDate = driverTools.waitForElementToAppear(RECENT_SEARCH_DATE,5).orElseThrow();
        String recentDateText = driverTools.getText(recentDate);
        boolean isRecentDateVisible = recentDateText.contains(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_En) || recentDateText.contains(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_Id);
        logger.log("is recent date visible: "+isRecentDateVisible + " expected: "+ trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_En + " or "+ trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_Id + " actual: "+recentDateText);
        Assertion.assertThat("recent date is correct",recentDateText,anyOf(containsString(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_En),containsString(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_Id)));

        //verify pax info
        WebElement recentPaxInfo = driverTools.waitForElementToAppear(RECENT_SEARCH_PAX_INFO,5).orElseThrow();
        String recentPaxInfoText = driverTools.getText(recentPaxInfo);
        int totalPaxInApp = getNumberOfPassengers(recentPaxInfoText);
        boolean isRecentPaxInfoVisible = totalPaxInApp == trainAndWhooshGeneralData.numberOfPassenger;
        logger.log("is recent pax info visible: "+isRecentPaxInfoVisible + " expected: "+ trainAndWhooshGeneralData.numberOfPassenger + " actual: "+totalPaxInApp);
        Assertion.assertThat("recent pax info is correct",totalPaxInApp, Matchers.is(trainAndWhooshGeneralData.numberOfPassenger));

        return isRecentSearchVisible && isRecentOriginVisible && isRecentDestinationVisible && isRecentDateVisible && isRecentPaxInfoVisible;
    }

    private Locator getRecentlySearchLocator(String trainDate){
        return new Locator("Recently search",AppiumBy.xpath("//*[contains(@resource-id,'tv_date') and contains(@text,'"+trainDate+"')]"));
    }

    @Override
    public void clearRecentlySearch(){
        driverTools.waitForElementToAppear(CLEAR_RECENT_SEARCH).ifPresent(driverTools::click);
    }

    @Override
    public void clickRecentlySearch(String dateEn,String dateId) {
        driverTools.click(RECENT_SEARCH_ORIGIN);
    }

    public void openFilter(){
        driverTools.click(FILTER);
    }

    @Override
    public void saveFilter(){
        driverTools.click(SAVE_FILTER);
    }

    @Override
    public void filterByClass(TrainClass trainClass){
        driverTools.swipeToElement(getClassLocator(trainClass), Swipe.UP,5).orElseThrow().click();
    }

    @Override
    public void filterByName(String trainName){
        Optional<WebElement> trainNameLocator = driverTools.swipeToElement(getNameLocator(trainName), Swipe.UP,5);
        if(trainNameLocator.isPresent()){
            driverTools.click(trainNameLocator.get());
        } else{
            driverTools.click(SEE_ALL_NAME);
            driverTools.click(getNameLocator(trainName));
            driverTools.click(SAVE_TRAIN_NAME);
        }
    }

    @Override
    public void openCalendar() {
        driverTools.click(DEPART_CALENDAR);
    }

    @Override
    public void skipPopup() {
        driverTools.waitForElementToAppear(SKIP_COACHMARK).ifPresent(driverTools::click);
    }

    /**
     * Verifies that all train names in the list contain the expected name.
     *
     * This method waits for the train names to appear on the screen, retrieves
     * the list of displayed train names, and checks if each train name contains
     * the provided expected string (trainName). It returns `true` if all train names
     * contain the expected string; otherwise, it returns `false`.
     *
     * @param trainName The expected string that should be part of each train name.
     * @return `true` if all train names contain the expected string; `false` otherwise.
     * @throws AssertionError if no train schedules are found on the page.
     */
    @Override
    public boolean verifyName(String trainName) {
        driverTools.waitForElementToAppear(TRAIN_NAME);
        List<WebElement> trainSchedules = driverTools.elements(TRAIN_NAME)
                .orElseThrow(() -> new AssertionError("No train schedules found."));
        int numOfTrains = trainSchedules.size();

        logger.log("Number of train schedules: " + numOfTrains);
        for (WebElement element : trainSchedules) {
            String trainClassName = element.getText();
            logger.log("Train name: " + trainClassName);

            if (!trainClassName.contains(trainName)) {
                logger.log("Train name does not contain the expected string: " + trainName);
                return false;
            }
        }
        return true;
    }

    /**
     * Verifies if the train schedules contain the specified train class and ensures
     * the count of schedules matches the count of the specified train class.
     *
     * @param trainClass the {@link TrainClass} object containing the class BHASA and English name to verify.
     * @return {@code true} if all train schedules contain the specified class and the count matches, {@code false} otherwise.
     */
    @Override
    public boolean verifyClass(TrainClass trainClass) {

        driverTools.waitForElementToAppear(TRAIN_1);

        List<WebElement> trainSchedules = driverTools.elements(TRAIN_1)
                .orElseThrow(() -> new AssertionError("No train schedules found."));
        int numOfTrains = trainSchedules.size();
        logger.log("Number of train schedules: " + numOfTrains);

        boolean areTrainClassesValid = checkTrainClass(trainClass);

        driverTools.swipeUp();
        List<WebElement> trainClasses = driverTools.elements(TRAIN_CLASS)
                .orElseThrow(() -> new AssertionError("No train classes found."));
        int numOfClass = trainClasses.size();
        logger.log("Number of train schedules with " + trainClass.eng + " class: " + numOfClass);

        Assertion.assertThat("Number of train schedules match number of train classes.", areTrainClassesValid,is(true));

        return numOfTrains == numOfClass;
    }

    private boolean checkTrainClass(TrainClass trainClass) {
        String classId = trainClass.id;
        String classEng = trainClass.eng;
        List<WebElement> listOfTrainClass = driverTools.elements(TRAIN_CLASS).orElseThrow();
        for (WebElement element : listOfTrainClass) {
            String trainClassName = element.getText();
            logger.log("Train class: " + trainClassName);
            if (!(trainClassName.contains(classId) || trainClassName.contains(classEng))) {
                return false;
            }
        }
        return true;
    }

    private boolean checkTrainName(String trainName){
        List<WebElement> listOfTrainName = driverTools.elements(TRAIN_NAME).orElseThrow();
        for (WebElement element : listOfTrainName) {
            String trainClassName = element.getText();
            logger.log("train class: " + trainClassName);
            if(!trainClassName.contains(trainName)){
                return false;
            }
        }
        return true;
    }

    private Locator getClassLocator(TrainClass trainClass) {
        String classId = trainClass.id;
        String classEng = trainClass.eng;
        return new Locator("TRAIN CLASS", AppiumBy.xpath("//*[contains(@resource-id,'tv_subtitle') and contains(@text,'"+classId+"') or contains(@text,'"+classEng+"')]"));
    }

    private Locator getNameLocator(String trainName) {
        return new Locator("TRAIN NAME", AppiumBy.xpath("//*[contains(@text,'"+trainName+"')]//following-sibling::*"));
    }

    @Override
    public void openViaDeeplink(){
        Deeplink deepLink = new Deeplink("TRAIN_SEARCH_PAGE",
                "https://m.gatotkaca.tiket.com/kereta-api",
                "https://m.preprod.tiket.com/kereta-api",
                "https://m.tiket.com/kereta-api");
        driverTools.deeplink(deepLink);
    }

    @Override
    public List<String> getPrices(){
        List<String> priceItems = new ArrayList<>(driverTools.getTexts(PRICE));
        Assertion.assertThat("Prices exist", !priceItems.isEmpty(),is(true));
        logger.log("prices: "+priceItems);
        return priceItems;
    }

    @Override
    public boolean VerifyAndClickPartialSellBanner() {
        driverTools.swipeToElement(CROSS_SELL_BTN_PARTIAL,Swipe.UP,50).orElseThrow();
        boolean CrossSellBusBanner =driverTools.isElementDisplayed(CROSS_SELL_PARTIAL_BANNER);
        driverTools.click(CROSS_SELL_BTN_PARTIAL);
        return CrossSellBusBanner;
    }

    @Override
    public void openTrainDetailWithNudges() {
       driverTools.swipeToElement(CARD_DETAIL_WITH_NUDGES_CHEVRON_BTN, Swipe.UP, 4).orElseThrow();
       driverTools.click(CARD_DETAIL_WITH_NUDGES_CHEVRON_BTN);
    }

    @Override
    public boolean verifyTrainDetailPrice() {
        driverTools.click(TRAIN_DETAIL_PRICE_TAB);
        driverTools.waitForElementToAppear(TRAIN_DETAIL_PRICE_BREAKDOWN,3);
        WebElement totalPriceElement = driverTools.element(TRAIN_DETAIL_TOTAL_PRICE).orElseThrow();
        boolean isTotalPricePresent = driverTools.isElementDisplayed(totalPriceElement);
        String totalPriceValue = driverTools.getText(totalPriceElement);
        Assertion.assertThat("Is total value present", isTotalPricePresent, is(true));
        List<String> priceBreakDownComponent = driverTools.getNumberTexts(TRAIN_DETAIL_PRICE_BREAKDOWN);
        double actualTotalPrice = CoreFunctions.sumUpStringNumbers(priceBreakDownComponent);
        Assertion.assertThat("Verify total price of price breakdown is the same as total price showed in ui", actualTotalPrice, equalTo(CoreFunctions.convertPrice(totalPriceValue)));
        return true;
    }

    @Override
    public void clickChooseInTrainDetail() {
       driverTools.click(TRAIN_DETAIL_SELECT_BTN);
    }
}
