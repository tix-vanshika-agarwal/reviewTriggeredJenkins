package com.tiket.page.train.searchpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.common.nftPopup.NFTCommonPopup;
import com.tiket.pojo.train.TrainClass;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.Matchers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;


public class TrainSearchPageIOS <T extends WebDriver> extends TrainSearchPage<T> {

    private static final Locator BACK_BUTTON_SEARCH_PAGE = new Locator("BACK_BUTTON", AppiumBy.accessibilityId("TDSNavbarButton"));
    private static final Locator BACK_BUTTON_SRP = new Locator("BACK_BUTTON_SRP", AppiumBy.accessibilityId("back"));
    private static final Locator TRAIN_ICON = new Locator("TrainIcon", AppiumBy.accessibilityId("Kereta Api"));
    private static final Locator ORIGIN = new Locator("Origin", AppiumBy.accessibilityId("topSearchForm"));
    private static final Locator ORIGIN_TEXT = new Locator("OriginText", AppiumBy.accessibilityId("originTextField"));
    private static final Locator DESTINATION = new Locator("Destination", AppiumBy.accessibilityId("bottomSearchForm"));
    private static final Locator DESTINATION_TEXT = new Locator("DestinationText", AppiumBy.accessibilityId("destinationTextField"));
    private static final Locator SEARCH_BUTTON = new Locator("SearchButton", AppiumBy.xpath("//XCUIElementTypeStaticText[@value=\"Search trains\" or @value=\"Cari kereta\"]"));
    private static final Locator LOCATION_SWITCH = new Locator("LocationSwitch", AppiumBy.accessibilityId("searchFormRoundTripSwitchTerminal"));
    private static final Locator FIRST_STATION = new Locator("FIRST_STATION", AppiumBy.xpath("(//XCUIElementTypeCell[@name='trainStationTableViewCell0' and @visible='true'])[last()]"));
    private static final Locator TRAIN_1 = new Locator("Train_1", AppiumBy.name("train_name_1"));
    private static final Locator RESULT_VIEW = new Locator("ResultView", AppiumBy.accessibilityId("trainSearchResultSearchInformationView"));
    private static final Locator SEARCH_FIELD = new Locator("SEARCH_FIELD",AppiumBy.xpath("//*[contains(@name,'tds_ic_search')]/parent::*/following-sibling::XCUIElementTypeTextField"));
    private static final Locator DEPART_CALENDAR= new Locator("DEPART_CALENDAR",AppiumBy.xpath("//XCUIElementTypeStaticText[@value='Round trip?' or @value='Pulang pergi?']//preceding-sibling::*[contains(@name, ',') and not(contains(@name,'Travel') or contains(@name,'Transportasi')) and @visible='true'][last()]"));
    private static final Locator RETURN_CALENDAR= new Locator("RETURN_CALENDAR",AppiumBy.xpath("//XCUIElementTypeStaticText[@value='Round trip?' or @value='Pulang pergi?']//preceding-sibling::*[contains(@name, ',')][last()-1]\n"));
    public static final Locator TOGGLE_SELECTED = new Locator("TOGGLE_SELECTED", AppiumBy.accessibilityId("switch (selected)"));
    public static final Locator TOGGLE_SELECT = new Locator("TOGGLE_SELECT", AppiumBy.accessibilityId("switch"));
    private static final Locator TRIP_TOGGLE = new Locator("TRIP_TOGGLE", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='Trains' or @name='Kereta Api' or @name='Whoosh'])[2]/preceding-sibling:: XCUIElementTypeOther[@accessible='true']"));
    private static final Locator PREVIOUS_MONTH = new Locator("PREVIOUS_MONTH",AppiumBy.accessibilityId("calendarPreviousMonthButton"));
    private static final Locator NEXT_MONTH = new Locator("NEXT_MONTH",AppiumBy.accessibilityId("calendarNextMonthButton"));
    private static final Locator ADD_PASSENGER = new Locator("ADD_PASSENGER",AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'Passenger') or contains(@name, 'Penumpang')]"));
    private static final Locator ADD_PASSENGER_ADULT = new Locator("ADD_PASSENGER_ADULT",AppiumBy.xpath("(//XCUIElementTypeButton[@name='add_btn'])[1]"));
    private static final Locator ADD_PASSENGER_INFANT = new Locator("ADD_PASSENGER_INFANT",AppiumBy.xpath("(//XCUIElementTypeButton[@name='add_btn'])[2]"));
    private static final Locator SUBTRACT_PASSENGER_ADULT = new Locator("SUBTRACT_PASSENGER_ADULT",AppiumBy.xpath("(//XCUIElementTypeButton[@name='subtract_btn'])[1]"));
    private static final Locator SUBTRACT_PASSENGER_INFANT = new Locator("SUBTRACT_PASSENGER_INFANT",AppiumBy.xpath("(//XCUIElementTypeButton[@name='subtract_btn'])[2]"));
    public static final Locator ADULT_COUNT = new Locator("ADULT_COUNT", AppiumBy.xpath("(//*[@name='quantity_label'])[1]"));
    public static final Locator INFANT_COUNT = new Locator("INFANT_COUNT", AppiumBy.xpath("(//*[@name='quantity_label'])[2]"));
    private static final Locator PICKER_WHEEL_CLOSE = new Locator("PICKER_WHEEL_TITLE",AppiumBy.accessibilityId("ic cancel grey"));
    private static final Locator PASSENGER_SAVE = new Locator("PASSENGER_SAVE",AppiumBy.xpath("//XCUIElementTypeStaticText[@label=\"Save\" or @label=\"Simpan\"]"));
    private static final Locator CLEAR_SERACH = new Locator("CLEAR_SERACH",AppiumBy.name("icSearchBoxClose"));
    private static final Locator FILTER = new Locator("FILTER",AppiumBy.accessibilityId("filterSortCoachmarkView"));
    private static final Locator SAVE_FILTER = new Locator("SAVE_FILTER",AppiumBy.xpath("//XCUIElementTypeButton[(@label=\"Simpan\" or @label=\"Save\" or contains(@label,'Done')) and @visible='true']"));
    private static final Locator TRAIN_DETAIL = new Locator("TRAIN_DETAIL",AppiumBy.xpath("//XCUIElementTypeCell[contains(@name,'trainDetailButton')]"));
    private static final Locator CURRENT_SELECTED_DATE = new Locator("CURRENT_SELECTED_DATE",AppiumBy.xpath("(//XCUIElementTypeStaticText[@name=\"Pergi\" or @name=\"Departure\"]/following-sibling::XCUIElementTypeStaticText[@visible='true'])[last()]"));
    private static final Locator CLEAR_RECENT  = new Locator("CLEAR_RECENT",AppiumBy.xpath("//*[contains(@label,'Clear') or contains(@label,'Hapus')]"));
    private static final Locator SKIP_COACHMARK = new Locator("SKIP_COACHMARK", AppiumBy.xpath("//XCUIElementTypeStaticText[@label=\"Lewati\" or @label=\"Skip\"]"));
    public static final Locator PRICE  = new Locator("PRICE",AppiumBy.xpath("//XCUIElementTypeStaticText[@label='/pax']/preceding-sibling::*[contains(@label,'.') or contains(@label,',')] | //XCUIElementTypeStaticText[@label='/pax']/following-sibling::*[contains(@label,'.') or contains(@label,',')]"));
    public static final Locator CROSS_SELL_BTN  = new Locator("CROSS_SELL_BTN",AppiumBy.xpath("(//*[contains(@label, 'Cari bus dan travel') or contains(@label, 'Search bus and shuttle')])[last()]"));
    private static final Locator CROSS_SELL_PARTIAL_BANNER = new Locator("CROSS_SELL_PARTIAL_BANNER", AppiumBy.xpath("//*[contains(@label,'No worries! You can take bus and shuttle to your destination.') or contains(@label,'Tenang aja, kamu juga bisa naik bus dan travel ke kota tujuanmu, lho.')]"));
    private static final Locator SELECTED_DATE = new Locator("SELECTED_DATE",AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,\",\")]"));
    public static final Locator SEE_ALL_NAME = new Locator( "SEE_ALL_NAME", AppiumBy.xpath("//*[@label=\"Train Name\" or @label='Nama Kereta Api']//following-sibling::XCUIElementTypeButton[contains(@label,'See more') or contains(@label,'Lihat semua')]"));
    private static final Locator CALENDAR_CONTENT_AREA = new Locator("CALENDAR_CONTENT_AREA", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='Set The Date' or @name='Atur Tanggal']/parent::*/following-sibling::XCUIElementTypeOther)[1]"));
    //recent search
    public static final Locator ROUND_TRIP_TOGGLE  = new Locator("ROUND_TRIP_TOGGLE", AppiumBy.xpath("//*[@name='switch']"));
    public static final Locator ROUND_TRIP_TOGGLED  = new Locator("ROUND_TRIP_TOGGLED", AppiumBy.xpath("//*[@name='switch (selected)']"));
    private static final Locator CLEAR_RECENT_SEARCH  = new Locator("CLEAR_RECENT_SEARCH", AppiumBy.xpath("//*[contains(@label,'Clear') or contains(@label,'Hapus')]"));
    public static final Locator RECENT_SEARCH  = new Locator("RECENT_SEARCH",AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Recently Searched' or @label='Pencarian Terakhir']"));
    public static final Locator RECENT_SEARCH_PAX_INFO  = new Locator("RECENT_SEARCH_PAX_INFO",AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Recently Searched' or @name='Pencarian Terakhir']//following-sibling::*//XCUIElementTypeStaticText[contains(@name,'Passenger') or contains(@name,'Penumpang')]"));

    // AB TEST LOCATORS FOR NEW UI
    //   public static final Locator NO_TRAIN_AVAILABLE  = new Locator("NO_TRAIN_AVAILABLE",AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label, 'Kereta belum tersedia di tanggal ini') or contains(@label, 'This route is not yet available') or contains(@label,'No trains is available') or contains(@label,'Kereta belum tersedia di tanggal ini')]"));
    //private static final Locator CURRENT_SELECTED_DATE = new Locator("CURRENT_SELECTED_DATE",AppiumBy.xpath("(//XCUIElementTypeStaticText[@name=\"Pergi\" or @name=\"Departure\"]/following-sibling::XCUIElementTypeStaticText[@visible='true'])[last()]"));
    //    private static final Locator INCEREMENT_PASSENGER_ADULT = new Locator("PICKER_WHEEL",AppiumBy.xpath("(//XCUIElementTypeButton[@name=\"add_btn\"])[1]"));
    //    private static final Locator INCEREMENT_WHEEL_PASSENGER_INFANT = new Locator("PICKER_WHEEL",AppiumBy.xpath("(//XCUIElementTypeButton[@name=\"add_btn\"])[2]"));
    //    private static final Locator ADD_PASSENGER = new Locator("ADD_PASSENGER",AppiumBy.xpath("//XCUIElementTypeStaticText[@name=\"1 Passenger \" or contains(@name, '1 Penumpang')]"));
    //    private static final Locator ORIGIN_SEARCH_FIELD= new Locator("ORIGIN_SEARCH_FIELD",AppiumBy.xpath("//XCUIElementTypeTextField[@value='Enter the city or station name.' or @value='Masukin nama kota atau stasiun.']\n"));
    //    private static final Locator DEPART_CALENDAR= new Locator("DEPART_CALENDAR",AppiumBy.xpath("//XCUIElementTypeStaticText[@value='Round trip?' or @value='Pulang pergi?']//preceding-sibling::*[contains(@name, ',')][last()]"));
    //    private static final Locator RETURN_CALENDAR= new Locator("RETURN_CALENDAR",AppiumBy.xpath("//XCUIElementTypeStaticText[@value='Round trip?' or @value='Pulang pergi?']//preceding-sibling::*[contains(@name, ',')][last()-1]\n"));
    //    private static final Locator TRIP_TOGGLE= new Locator("TRIP_TOGGLE",AppiumBy.accessibilityId("switch"));
    //    private static final Locator TRAIN_1 = new Locator("Train_1", AppiumBy.accessibilityId("train_name_1"));
    //    private static final Locator SEARCH_BUTTON = new Locator("SearchButton", AppiumBy.xpath("//XCUIElementTypeStaticText[@name=\"Search trains\" or @name=\"Cari kereta\"]\n"));
    //    private static final Locator DESTINATION = new Locator("Destination", AppiumBy.accessibilityId("bottomSearchForm"));
    //    private static final Locator ORIGIN = new Locator("Origin", AppiumBy.accessibilityId("topSearchForm"));

    public TrainSearchPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public void openTrainPage() {
        driverTools.click(TRAIN_ICON);
        driverTools.waitForElementToAppear(SEARCH_BUTTON);
    }

    @Override
    public void selectOrigin(String stationName) {
        skipPopup();
        WebElement originElement = driverTools.waitForElementToPresence(ORIGIN).orElseThrow();
        driverTools.clickElementByCoordinate(originElement);
        logger.log("Searching station: "+stationName);
        try {
        driverTools.sendKeys(SEARCH_FIELD,stationName);
        } catch (Exception e) {
            logger.log("Failed to send keys using sendKeys, trying pressKey instead");
            driverTools.pressKey(SEARCH_FIELD,stationName);
        }
        driverTools.waitForElementToAppear(selectCityNameStationCode(stationName));
        driverTools.click(selectCityNameStationCode(stationName));
    }
    private Locator selectCityNameStationCode(String stationName) {
        String capitalizedStationName = stationName.substring(0, 1).toUpperCase() + stationName.substring(1);
        return new Locator("FIRST_STATION", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='" + capitalizedStationName + "' or @name='" + capitalizedStationName.toUpperCase() + "']"));
    }

    @Override
    public void selectDestination(String stationName) {
        driverTools.click(DESTINATION);
        logger.log("Searching station: "+stationName);
        driverTools.sendKeys(SEARCH_FIELD,stationName);
        driverTools.waitForElementToAppear(selectCityNameStationCode(stationName));
        driverTools.click(selectCityNameStationCode(stationName));
    }

    @Override
    public String selectOriginGetStationName(String stationName) {
        driverTools.waitForElementToAppear(ORIGIN).orElseThrow();
        driverTools.click(ORIGIN);
        logger.log("Searching station: "+stationName);
        driverTools.sendKeys(SEARCH_FIELD,stationName);
        driverTools.click(CLEAR_SERACH);
        driverTools.sendKeys(SEARCH_FIELD,stationName);
        driverTools.waitForElementToAppear(FIRST_STATION);
        String originCityName = driverTools.getText(getCityNameStationCode(stationName)).orElseThrow();
        driverTools.click(FIRST_STATION);
        return originCityName;
    }

    @Override
    public String selectDestinationGetStationName(String stationName) {
        driverTools.click(DESTINATION);
        logger.log("Searching station: "+stationName);
        driverTools.sendKeys(SEARCH_FIELD,stationName);
        driverTools.click(CLEAR_SERACH);
        driverTools.sendKeys(SEARCH_FIELD,stationName);
        driverTools.waitForElementToAppear(FIRST_STATION);
        String destinationCityName = driverTools.getText(getCityNameStationCode(stationName)).orElseThrow();
        driverTools.click(FIRST_STATION);
        return destinationCityName;
    }

    private Locator getCityNameStationCode(String stationName) {
        return new Locator("FIRST_STATION", AppiumBy.xpath("//XCUIElementTypeCell[@name='trainStationTableViewCell0' and .//XCUIElementTypeStaticText[@name='codeLabel' and @label='"+stationName.toUpperCase()+"']]//XCUIElementTypeStaticText[@name='locationLabel']"));
    }

    @Override
    public void toggleTripType(String tripType) {
        driverTools.swipeDown();
        //clicking on toggle twice first because locator is changing after first toggle ON within single session
        try {
            driverTools.waitForElementToAppear(TRIP_TOGGLE);
            driverTools.click(TRIP_TOGGLE);
            driverTools.waitForElementToAppear(TRIP_TOGGLE);
            driverTools.click(TRIP_TOGGLE);
        }catch (Exception e){
           logger.log(e.getMessage());
        }

        boolean current_state = driverTools.waitForElementToPresence(TOGGLE_SELECTED, 10).isPresent();
        logger.log("Toggling trip type: " + tripType);
        tripType = tripType.toLowerCase();
        switch (tripType.toLowerCase()) {
            case "roundtrip" -> {
                if (!current_state) driverTools.clickElementByCoordinate(TOGGLE_SELECT);
                driverTools.waitForElementToAppear(TOGGLE_SELECTED).orElseThrow();
            }
            case "oneway" -> {
                if (current_state) driverTools.clickElementByCoordinate(TOGGLE_SELECTED);
                driverTools.waitForElementToDisappear(TOGGLE_SELECTED);
            }
        }
    }

    @Override
    public void selectDate(String departureDate, String returnDate) {
        WebElement departCalendar = driverTools.waitForElementToPresence(DEPART_CALENDAR).orElseThrow();
        driverTools.click(departCalendar);
        if(driverTools.isElementDisplayed(DEPART_CALENDAR)){
            departCalendar = driverTools.waitForElementToPresence(DEPART_CALENDAR).orElseThrow();
            driverTools.clickElementByCoordinate(departCalendar);
        }

        logger.log("Departing on: "+ departureDate + " Returning on: " + returnDate);

        Swipe swipeDepart = verifyMonth(departureDate);
        clickDate(departureDate,false,swipeDepart);

        Swipe swipeRet = verifyMonth(returnDate);
        clickDate(returnDate,true,swipeRet);
        if(driverTools.isElementDisplayed(PASSENGER_SAVE)){
            driverTools.click(PASSENGER_SAVE);
        }
    }

    @Override
    public void selectDate(String departureDate) {
        driverTools.waitForElementToPresence(DEPART_CALENDAR).orElseThrow();
        driverTools.clickElementByCoordinate(DEPART_CALENDAR);
        logger.log("Departing on: " + departureDate);

        Swipe swipe = verifyMonth(departureDate);
        clickDate(departureDate,false,swipe);
        driverTools.click(PASSENGER_SAVE);
    }

    private Swipe verifyMonth(String date) {
        String departureMonth = CoreFunctions.monthEngToId(CoreFunctions.getSectionOfString(date, " ", 1)) + " " + CoreFunctions.getSectionOfString(date, " ", 2);
        String calendarMonth = getCalendarMonth();
        logger.log("departure month sheet: " + departureMonth);
        logger.log("departure month app: " + calendarMonth);
        if(!departureMonth.equals(calendarMonth)) {
            return goToCorrectMonth(departureMonth, calendarMonth);
        }
        return null;
    }

    /*
     * Select available date, if not available select date + 7 days (for return date only)
     * this is because in dom if we select depart date in current week date for return is not in dom until next week
     */
    private Locator getAvailableSelectDateLocator(String date,boolean isReturnDate){
        String departureDay = CoreFunctions.getSectionOfString(date, " ", 0);
        String selectMonthEn = CoreFunctions.getSectionOfString(date, " ", 1);
        String selectMonthId = CoreFunctions.monthEngToId(selectMonthEn);
        logger.log("Selecting date with day: " + departureDay + " monthEn: " + selectMonthEn + " monthId: " + selectMonthId);
        Locator dateCalendar = getDateLocator(departureDay, selectMonthEn, selectMonthId);
        boolean isDatePresent = driverTools.waitForElementToPresence(dateCalendar).isPresent();
        if(!isDatePresent && isReturnDate){
            logger.log("Date not found with index: " + departureDay + " " + selectMonthEn + " or " + selectMonthId);
            date = CoreFunctions.addToLongDate(date,7);
            departureDay = CoreFunctions.getSectionOfString(date, " ", 0);
            selectMonthEn = CoreFunctions.getSectionOfString(date, " ", 1);
            selectMonthId = CoreFunctions.monthEngToId(selectMonthEn);
            logger.log("Trying to select date with day: " + departureDay + " monthEn: " + selectMonthEn + " monthId: " + selectMonthId);
            return getDateLocator(departureDay, selectMonthEn, selectMonthId);
        }
        return dateCalendar;
    }

    private void clickDate(String date,boolean isReturnDate,Swipe swipe) {
        Locator dateCalendar = getAvailableSelectDateLocator(date,isReturnDate);
        boolean isDateSelected = false;
        if(driverTools.waitForElementToPresence(dateCalendar).isPresent()){
            WebElement dateToSelect  = driverTools.waitForElementToPresence(dateCalendar).orElseThrow();
            driverTools.waitForElementToBeClickable(dateToSelect);
            driverTools.clickElementByCoordinate(dateToSelect);
            isDateSelected = true;
        }else {
            for(int i = 4; i > 0; i--){
                if(driverTools.waitForElementToAppear(dateCalendar,1).isPresent()){
                    WebElement dateToSelect  = driverTools.waitForElementToPresence(dateCalendar).orElseThrow();
                    driverTools.waitForElementToBeClickable(dateToSelect);
                    driverTools.clickElementByCoordinate(dateToSelect);
                    isDateSelected = true;
                    break;
                }
                logger.log("Date not found, swiping to find date. Attempts left: " + (i-1) + " with swipe: " + swipe);
                WebElement calendarArea = driverTools.waitForElementToAppear(CALENDAR_CONTENT_AREA).orElseThrow();
                driverTools.swipeCalendarByCoordinates(calendarArea, swipe);
            }
        }
        if (!isDateSelected)
            throw new NoSuchElementException("Date not found: " + date);
    }

    private Locator getDateLocator(String departureDay, String selectMonthEn, String selectMonthId) {
        return new Locator("DATE", AppiumBy.xpath("//XCUIElementTypeButton[(contains(concat(' ', normalize-space(@label), ' '), ' " + departureDay + " ') and (contains(@label,'"+selectMonthEn+"') or contains(@label,'"+selectMonthId+"'))) and @visible='true']"));
    }

    private Swipe goToCorrectMonth(String departureMonth, String calendarMonth){

        boolean reverse = false;

        int departYear = Integer.parseInt(CoreFunctions.getSectionOfString(departureMonth, " ", 1));
        departureMonth = CoreFunctions.getSectionOfString(departureMonth, " ", 0);

        int calendarYear = Integer.parseInt(CoreFunctions.getSectionOfString(calendarMonth, " ", 1));
        calendarMonth = CoreFunctions.getSectionOfString(calendarMonth, " ", 0);

        int departureMonthNum = CoreFunctions.getMonthNumber(departureMonth);
        int calendarMonthNum = CoreFunctions.getMonthNumber(calendarMonth);

        departureMonthNum = departureMonthNum + (departYear - calendarYear) * 12;
        int distanceBetweenMonths = departureMonthNum - calendarMonthNum;

        if (distanceBetweenMonths < 0) {
            reverse = true;
        }
        return reverse ? Swipe.DOWN : Swipe.UP;
    }

    private String formatMonth(String data){
        return data.replaceFirst(".*_", "");
    }

    private String getCalendarMonth(){
        WebElement currentSelectedDateElement = driverTools.waitForElementToAppear(CURRENT_SELECTED_DATE,5).orElseThrow();
        String currentSelectedDate = driverTools.getText(currentSelectedDateElement);
        logger.log("Current selected date from app : " + currentSelectedDate);
        String[] date = currentSelectedDate.split(" ");
        String year = date[date.length-1];
        logger.log("year : " + year);
        String month = date[date.length-2];
        logger.log("month en: " + month);
        String monthFormatted = CoreFunctions.monthShortToShortEngToId(month);
        logger.log("month id: " + monthFormatted);
        if(monthFormatted != null){
            month = monthFormatted;
        }
        month = CoreFunctions.monthShortToLongId(month);
        String calendarDate = month + " " + year;
        logger.log(calendarDate);
        return calendarDate;
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
        String origin = driverTools.getText(ORIGIN_TEXT).orElseThrow();
        String destination = driverTools.getText(DESTINATION_TEXT).orElseThrow();
        return new String[] {origin, destination};
    }

    @Override
    public boolean isResultViewLoaded() {
        return driverTools.waitForElementToAppear(RESULT_VIEW)
                .map(driverTools::isElementDisplayed)
                .orElseThrow();

    }

    @Override
    public void backToHomePage(){
        driverTools.waitForElementToAppear(BACK_BUTTON_SEARCH_PAGE).ifPresentOrElse(driverTools::click, ()-> {throw new NoSuchElementException();});
    }

    /**
     * Add passenger to the search form
     * @param adults number of adult passengers
     * @param infants number of infant passengers
     */
    @Override
    public void addPassenger(int adults, int infants){
        driverTools.waitForElementToAppear(ADD_PASSENGER);
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
     * Get the locator of the passenger count
     * @param passengerType type of passenger (adult or infant)
     * @return locator of the passenger count
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
     * Get the locator of the add or subtract button of the passenger
     * @param passengerType type of passenger (adult or infant)
     * @param action action to be performed (increment or decrement)
     * @return locator of the add or subtract button of the passenger
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
        String nameBasedOnIndex = "train_name_" + index;
        return new Locator("Train_"+index, AppiumBy.xpath("//*[@name='" + nameBasedOnIndex + "']"));
    }

    @Override
    public void selectTrainViaIndex(int index){
        driverTools.waitForElementToAppear(getTrainSelectLocatorByIndex(index),10);
        driverTools.click(getTrainSelectLocatorByIndex(index));
    }

    @Override
    public String getSelectedDate(TrainAndWhooshGeneralData trainAndWhooshGeneralData){
        String selectedDateFormattedEn = trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_En;
        String selectedDateFormattedId = trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_Id;

        Locator dateLocator = getDateLocator(selectedDateFormattedEn,selectedDateFormattedId);
        driverTools.waitForElementToAppear(dateLocator);
        String date = driverTools.getValueAttribute(dateLocator,"value");
        date = date.split(", ")[1];
        String[] dateArr = date.split(" ");
        int day = Integer.parseInt(dateArr[0]);
        if(day<10){
            dateArr[0] = String.valueOf(day);
        }
        date = dateArr[0] + " " + dateArr[1];
        return date;
    }

    private Locator getDateLocator(String dateEn,String dateId){
        return new Locator("DATE_SELECTED", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,',') and (contains(@label,'" + dateEn + "') or contains(@label,'" + dateId + "'))]"));
    }

    @Override
    public boolean verifyRecentlySearch(TrainAndWhooshGeneralData trainAndWhooshGeneralData){
        driverTools.swipeUp(RECENT_SEARCH);
        boolean isRecentSearchVisible = driverTools.waitForElementToAppear(RECENT_SEARCH).isPresent();
        logger.log("is recent search visible: "+isRecentSearchVisible);

        WebElement recentOrigin = driverTools.waitForElementToAppear(getRecentSearchSearchDestinationLocator(trainAndWhooshGeneralData.originStation),5).orElseThrow();
        String recentOriginText = driverTools.getText(recentOrigin).replaceAll("\\s*\\(.*\\)$", "").trim().toLowerCase();
        boolean isRecentOriginVisible = recentOriginText.contains(trainAndWhooshGeneralData.originStation);
        logger.log("is recent origin visible: "+isRecentOriginVisible + " ,expected: "+ trainAndWhooshGeneralData.originStation + " ,actual: "+recentOriginText);
        Assertion.assertThat("recent origin is correct",recentOriginText,containsStringIgnoringCase(trainAndWhooshGeneralData.originStation));

        WebElement recentDestination = driverTools.waitForElementToAppear(getRecentSearchSearchDestinationLocator(trainAndWhooshGeneralData.destinationStation),5).orElseThrow();
        String recentDestinationText = driverTools.getText(recentDestination).replaceAll("\\s*\\(.*\\)$", "").trim().toLowerCase();
        boolean isRecentDestinationVisible = recentDestinationText.contains(trainAndWhooshGeneralData.destinationStation);
        logger.log("is recent destination visible: "+isRecentDestinationVisible + " ,expected: "+ trainAndWhooshGeneralData.destinationStation + " ,actual: "+recentDestinationText);
        Assertion.assertThat("recent destination is correct",recentDestinationText,containsStringIgnoringCase(trainAndWhooshGeneralData.destinationStation));

        Locator recentSearchDateLocator = getRecentSearchSearchCardLocator(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_En, trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_Id);
        WebElement recentDate = driverTools.waitForElementToAppear(recentSearchDateLocator,5).orElseThrow();
        String recentDateText = driverTools.getText(recentDate);
        boolean isRecentDateVisible = recentDateText.contains(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_En) || recentDateText.contains(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_Id);
        logger.log("is recent date visible: "+isRecentDateVisible + " ,expected: "+ trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_En + " or "+ trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_Id + " ,actual: "+recentDateText);
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

    private Locator getRecentSearchSearchCardLocator(String whooshDateEn,String whooshDateId){
        return new Locator("RECENT_SEARCH_DATE", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Recently Searched' or @name='Pencarian Terakhir']//following-sibling::*//XCUIElementTypeStaticText[contains(@name,'"+whooshDateEn+"') or contains(@name,'"+whooshDateId+"')]"));
    }

    private Locator getRecentSearchSearchDestinationLocator(String location){
        // Capitalize first word only
        String capitalizedLocation = location.substring(0, 1).toUpperCase() + location.substring(1).toLowerCase();

        // Capitalize first letter of each word
        String titleizedLocation = Arrays.stream(location.split("\\s+"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));

        return new Locator("RECENT_SEARCH_ORIGIN_OR_DESTINATION", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Recently Searched' or @label='Pencarian Terakhir' ]//following-sibling::*//*[contains(@label,'"+capitalizedLocation+"') or contains(@label,'"+titleizedLocation+"')]"));
    }

    @Override
    public void clearRecentlySearch(){
        driverTools.waitForElementToAppear(CLEAR_RECENT_SEARCH).ifPresent(driverTools::click);
    }

    @Override
    public void clickRecentlySearch(String DateEn,String DateId){
        driverTools.click(getRecentSearchSearchCardLocator(DateEn,DateId));
    }

    public void openFilter(){
        driverTools.clickElementByCoordinate(FILTER);
    }

    @Override
    public void saveFilter(){
        driverTools.click(SAVE_FILTER);
    }

    @Override
    public void filterByClass(TrainClass trainClass){
        WebElement classTrain = driverTools.swipeToElement(getClassLocator(trainClass), Swipe.UP,5).orElseThrow();
        driverTools.click(classTrain);
    }

    private Locator getClassLocator(TrainClass trainClass) {
        String classId = trainClass.id;
        String classEng = trainClass.eng;
//        return new Locator("TRAIN CLASS", AppiumBy.xpath("//*[(contains(@label,'"+classId+"') or contains(@label,'"+classEng+"')) and @visible='true']"));
        return new Locator("TRAIN CLASS", AppiumBy.xpath("//*[(contains(@label,'" + classId + "') or contains(@label,'" + classEng + "'))]"));
    }

    @Override
    public boolean verifyClass(TrainClass trainClass){
        driverTools.waitForElementToAppear(TRAIN_DETAIL);

        List<WebElement> trainSchedules = driverTools.elements(TRAIN_DETAIL).orElseThrow();
        int numOfTrains = trainSchedules.size();

        logger.log("number of train schedules: "+numOfTrains);

        List<WebElement> trainClasses = driverTools.elements(getTrainClassResult(trainClass)).orElseThrow();
        int numOfClass = trainClasses.size();

        logger.log("number of train schedules with "+trainClass.eng+" class: "+numOfClass);

        return numOfTrains == numOfClass;
    }

    private Locator getTrainClassResult(TrainClass trainClass) {
        String classId = trainClass.id;
        String classEng = trainClass.eng;
        return new Locator("TRAIN CLASS", AppiumBy.xpath("//XCUIElementTypeCell[contains(@name,'trainDetailButton')]//*[contains(@label,'"+classId+"') or contains(@label,'"+classEng+"')]"));
    }

    @Override
    public void filterByName(String trainName){
        driverTools.swipeToElement(getNameLocator(trainName), Swipe.UP, 5);
        if (driverTools.isElementDisplayed(getNameLocator(trainName))){
            driverTools.click(getNameLocator(trainName));
        }else {
            driverTools.swipeToElement(SEE_ALL_NAME, Swipe.UP, 5).orElseThrow();
            driverTools.click(SEE_ALL_NAME);
            driverTools.waitForElementToAppear(PICKER_WHEEL_CLOSE);
            driverTools.swipeToElement(getNameLocator(trainName), Swipe.UP, 5).orElseThrow();
            driverTools.click(getNameLocator(trainName));
            driverTools.click(SAVE_FILTER);
        }
    }

    @Override
    public void openCalendar() {
        driverTools.click(DEPART_CALENDAR);
        if(driverTools.isElementDisplayed(DEPART_CALENDAR)){
            driverTools.clickElementByCoordinate(DEPART_CALENDAR);
        }
    }

    @Override
    public void skipPopup() {
        driverTools.waitForElementToAppear(SKIP_COACHMARK).ifPresent(driverTools::clickElementByCoordinate);
    }

    private Locator getNameLocator(String trainName) {
        return new Locator("TRAIN NAME", AppiumBy.xpath("//*[(contains(@label,'"+trainName+"')) and @visible='true']"));
    }

    @Override
    public boolean verifyName(String trainName){
        driverTools.waitForElementToAppear(TRAIN_DETAIL);

        List<WebElement> trainSchedules = driverTools.elements(TRAIN_DETAIL).orElseThrow();
        int numOfTrains = trainSchedules.size();

        logger.log("number of train schedules: "+numOfTrains);

        List<WebElement> trainNames = driverTools.elements(getTrainNameResult(trainName)).orElseThrow();
        int numOfName = trainNames.size();

        logger.log("number of train schedules with "+trainName+" name: "+numOfName);

        return numOfTrains == numOfName;
    }

    private Locator getTrainNameResult(String trainName) {
        return new Locator("TRAIN NAME", AppiumBy.xpath("//XCUIElementTypeCell[contains(@name,'trainDetailButton')]//*[contains(@label,'"+trainName+"')]"));
    }

    @Override
    public void openViaDeeplink(){
        Deeplink deepLink = new Deeplink("TRAIN_SEARCH_PAGE",
                "tiket://m.gatotkaca.tiket.com/kereta-api",
                "tiket://m.preprod.tiket.com/kereta-api",
                "tiket://m.tiket.com/kereta-api");
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
        driverTools.swipeToElement(CROSS_SELL_BTN,Swipe.UP,15).orElseThrow();
        boolean CrossSellBusBanner =driverTools.isElementDisplayed(CROSS_SELL_PARTIAL_BANNER);
        driverTools.click(CROSS_SELL_BTN);
        return CrossSellBusBanner;
    }

    @Override
    public void openTrainDetailWithNudges() {
        //TODO fix method after this task is unblocked
        //https://borobudur.atlassian.net/browse/QAAUT-13542
        throw new UnsupportedOperationException("Unimplemented method 'openTrainDetailWithNudges'");
    }

    @Override
    public boolean verifyTrainDetailPrice() {
        //TODO fix method after this task is unblocked
        //https://borobudur.atlassian.net/browse/QAAUT-13542
        //
        throw new UnsupportedOperationException("Unimplemented method 'verifyTrainDetailPricePresent'");
    }

    @Override
    public void clickChooseInTrainDetail() {
        //TODO fix method after this task is unblocked
        //https://borobudur.atlassian.net/browse/QAAUT-13542
        throw new UnsupportedOperationException("Unimplemented method 'clickChooseInTrainDetail'");
    }
}