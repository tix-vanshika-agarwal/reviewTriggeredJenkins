package com.tiket.web.dashboard.page.b2b_external;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.*;

import java.util.NoSuchElementException;
import java.util.*;

public class TrainOrderPage<T extends WebDriver> extends BasePage<T> {
    private static final String GK_CORP_DASHBOARD = "https://corporate-gatotkaca.tiket.com";
    private static final String PREPROD_CORP_DASHBOARD = "https://corporate.preprod.tiket.com";
    private static final String PROD_CORP_DASHBOARD = "https://corporate.tiket.com";
    private static final Locator TRAIN_BUTTON = new Locator("TRAIN_BUTTON", new By.ByXPath("//a[@href='/kereta-api'][1]"));
    private static final Locator TRAIN_DEPARTURE_ICON = new Locator("TRAIN_DEPARTURE_ICON", new By.ByXPath("//span[contains(text(), 'station')]"));
    private static final Locator SET_DEPARTURE_CITY_TEXTFIELD = new Locator("SET_DEPARTURE_CITY", new By.ByXPath("//span[contains(text(), 'station')]"));
    private static final Locator SET_ARRIVAL_CITY_TEXTFIELD = new Locator("SET_ARRIVAL_CITY", new By.ByXPath("//span[contains(text(), 'Going')]"));
    private static final Locator STATION_DROPDOWN = new Locator("STATION_DROPDOWN", new By.ByXPath("//div[contains(@class, 'station-list-box')]"));
    private static final Locator CALENDAR_ICON = new Locator("CALENDAR_ICON", new By.ByXPath("//div[@class='DateInput_input_tap_area__g8jL0']"));
    private static final Locator DATEPICKER = new Locator("DATEPICKER", new By.ByXPath("(//div[@class='wrapper-datepicker datepicker-depart'])"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//*[text()='Cari Kereta Api' or text()='Search Train']"));
    private static final Locator DATEPICKER_WIDGET = new Locator("DATEPICKER_WIDGET", new By.ByXPath("//div[contains(@class,'widget-date-picker-container')]"));
    private static final Locator ADD_MATURE_PASSENGER = new Locator("ADD_MATURE_PASSENGER", new By.ByXPath("(//div[@class='passenger-list-box'])[1]//*[contains(@class, 'plus')]"));
    private static final Locator ADD_BABY_PASSENGER = new Locator("ADD_BABY_PASSENGER", new By.ByXPath("//div[@id='modal-root']//div[2]//div[1]//button[2]"));
    private static final Locator CLOSE_PASSENGER_WIDGET = new Locator("CLOSE_PASSENGER_WIDGET", new By.ByXPath("//*[text()='Finished' or text()='Selesai']"));
    private static final Locator PASSENGER_WIDGET = new Locator("CLOSE_PASSENGER_WIDGET", new By.ByXPath("//div[contains(@class, 'passenger-action-box')]"));
    private static final Locator CLOSE_ONBOARDING_DIALOG = new Locator("CLOSE_ONBOARDING_DIALOG", new By.ByXPath("//*[contains(text(), 'Lewati') or contains(text(), 'Skip')]"));
    private static final Locator TRAIN_ORDER_CARD = new Locator("TRAIN_ORDER_CARD", new By.ByXPath("(//div[contains(@class, 'ScheduleList_main_card')])[1]"));
    private static final Locator CONTACT_DETAIL_SECTION = new Locator("CONTACT_DETAIL_SECTION", new By.ByXPath("//h3[contains(@class, 'ContactDetailSection')]"));
    private static final Locator SAVE_PASSENGER_DETAIL = new Locator("SAVE_PASSENGER_DETAIL", new By.ByXPath("//button[contains(text(), 'Simpan') or contains(text(), 'Lanjutkan')"));
    private static final Locator DETAIL_CARD_INNER_FIRST_PASSENGER = new Locator("DETAIL_CARD_INNER_FIRST_PASSENGER", new By.ByXPath("//div[contains(@class, 'PassengersDetailSection_card_inner')]//p"));
    private static final Locator FIRST_PASSENGER_NAME_BS = new Locator("FIRST_PASSENGER_NAME", new By.ByXPath("(//div[@data-bottom-sheet-scrollable-element]//input[@type='text'])[1]"));
    private static final Locator FIRST_PASSENGER_NIK_BS = new Locator("FIRST_PASSENGER_NIK", new By.ByXPath("(//div[@data-bottom-sheet-scrollable-element]//input[@type='text'])[2]"));
    private static final Locator FIRST_PASSENGER_PASSPORT = new Locator("FIRST_PASSENGER_PASSPORT", new By.ByXPath("//input[contains(@id, 'nomor-paspor')]"));
    private static final Locator FIRST_PASSENGER_PASSPORT_CHECKBOX = new Locator("FIRST_PASSENGER_PASSPORT_CHECKBOX", new By.ByXPath("//span[contains(text(), 'Paspor')]"));
    private static final Locator FIRST_PASSENGER_NAME = new Locator("FIRST_PASSENGER_NAME", new By.ByXPath("//*[contains(@class, 'PassengerForm')]//div[contains(@class,'styles_input_inner_wrapper__Xi61m')][1]"));
    private static final Locator NIK_RADIO_CHECKBOX = new Locator("PASSPORT_RADIO_CHECKBOX", new By.ByXPath("(//span[contains(@class, 'Radio_checkbox')])[last()-1]"));
    private static final Locator MR_RADIO_CHECKBOX_BS = new Locator("MR_RADIO_CHECKBOX", new By.ByXPath("//*[contains(@class, 'ContactForm')]//span[contains(@class, 'Radio_icon__uvFtE')][1]"));
    private static final Locator MR_RADIO_CHECKBOX = new Locator("MR_RADIO_CHECKBOX", new By.ByXPath("(//*[contains(@class, 'Passenger')]//span[contains(@class, 'Radio_checkbox')])[1]"));
    private static final Locator CONTINUE_TO_SEAT_AND_PROTECTION = new Locator("CONTINUE_TO_SEAT_AND_PROTECTION", new By.ByXPath("//button[contains(@class, 'train_footer_button')]"));
    private static final Locator CONTINUE_TO_PAYMENT = new Locator("CONTINUE_TO_PAYMENT", new By.ByXPath("//button[normalize-space()='Continue']"));
    private static final Locator CONFIRM_CONTINUE_TO_PAYMENT = new Locator("CONFIRM_CONTINUE_TO_PAYMENT", new By.ByXPath("//button[normalize-space()='Continue to pay']"));
    private static final Locator INTRIM_PAGE = new Locator("INTRIM_PAGE", new By.ByXPath("//button[normalize-space()='Continue to payment']"));
    private static final Locator CONFIRM_PAYMENT = new Locator("CONFIRM_PAYMENT", new By.ByXPath("//button[normalize-space()='Continue to Payment']"));
    private static final Locator SKIP_TUTORIAL = new Locator("SKIP_TUTORIAL", new By.ByXPath("//button[contains(@class,'Coachmark_coachmark_skip_btn')]"));
    private static final Locator SELECT_DEPART_CITY = new Locator("SELECT_DEPART_CITY", new By.ByXPath("//span[normalize-space()='BD']"));
    private static final Locator SELECT_DESTINATION_CITY = new Locator("SELECT_DESTINATION_CITY", new By.ByXPath("//span[normalize-space()='GMR']"));
    private static final Locator SUBMIT = new Locator("SUBMIT", new By.ByXPath("//button[normalize-space()='Save']"));
    private static final Locator SEARCH_TRAIN = new Locator("SEARCH_TRAIN", new By.ByXPath("//button[normalize-space()='Search trains']"));
    private static final Locator SELECT_PASSENGER = new Locator("SELECT_PASSENGER", new By.ByXPath("//span[contains(text(), 'Passenger')]//parent::div[@class='PassengerInput_input_text__dMiSD']"));
    private static final Locator Text_Bar  = new Locator("Text_Bar", new By.ByXPath("(//label[contains(text(), 'Full Name According to ID')]/preceding-sibling::input[contains(@class, 'styles_input')])[2]"));
    private static final Locator ENTER_PASSPORT_VALUE  = new Locator("ENTER_PASSPORT_VALUE", new By.ByXPath("//*[@id='passport-number']"));
    private static final Locator PASSPORT_RADIO_BUTTON  = new Locator("PASSPORT_RADIO_BUTTON", new By.ByXPath("(//*[@data-testid='Paspor' or @data-testid='Passport'])[1]"));

    private static final Locator SUBMIT_PAYMENT = new Locator("SUBMIT_PAYMENT", new By.ByXPath("//*[contains(@class, 'SubmitPayment')]"));
    private static final Locator CORPORATE_BILLING_SUCCESS = new Locator("CORPORATE_BILLING_SUCCESS", new By.ByXPath("//*[contains(@class, 'CoporateBilingSuccess')]"));
    private static final Locator ORDER_CARD_COUNTDOWN = new Locator("ORDER_CARD_COUNTDOWN", new By.ByXPath("//div[contains(@class, 'OrderCard_countdown')]"));
    private static final Locator ORDER_ID = new Locator("ORDER_ID", new By.ByXPath("//*[contains(text(), 'Order ID:')]"));
    private static final Locator ORDER_CARD = new Locator("ORDER_CARD", new By.ByXPath("//div[contains(@class, 'OrderCard_countdown')]/ancestor::div[contains(@class, 'OrderCard_oc_details_container')]"));
    private static final Locator TRAIN_DETAIL_HEADER = new Locator("ORDER_CARD", new By.ByXPath("//div[contains(@class, 'detail_order')]"));
    private static final Locator DEPARTURE_STATION = new Locator("DEPARTURE_STATION", new By.ByXPath("(//*[contains(@class, 'DetailReservation_flight_subtitle')])[1]"));
    private static final Locator ARRIVAL_STATION = new Locator("ARRIVAL_STATION", new By.ByXPath("(//*[contains(@class, 'DetailReservation_flight_subtitle')])[2]"));
    private static final Locator DEPARTURE_DATE = new Locator("DEPARTURE_DATE", new By.ByXPath("(//*[contains(text(), ',') and contains(text(), '-')])[1]"));
    private static final Locator EXPAND_PASSENGER_DETAIL = new Locator("EXPAND_PASSENGER_DETAIL", new By.ByXPath("//*[contains(text(), 'Passenger') or contains(text(), 'Penumpang')]"));
    private static final Locator PASSENGER_COLLAPSED = new Locator("ORDER_CARD_COUNTDOWN", new By.ByXPath("//*[contains(@class, 'passenger-name-fullname')]"));
    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", new By.ByXPath("//*[contains(@class, 'Text_text') and contains(text(), 'IDR')]"));
    private static final Locator PRICE_DETAIL_COLLAPSE = new Locator("PRICE_DETAIL_COLLAPSE", new By.ByXPath("//div[@class='price-total' or @class='price-total dividerBottom']"));
    private static final Locator PRICE_DETAIL_COLLAPSED = new Locator("PRICE_DETAIL_COLLAPSE", new By.ByXPath("//div[@class='price-collapse isOpen collapse in']"));
    private static final Locator TRAIN_NAME = new Locator("TRAIN_NAME", new By.ByXPath("//*[contains(@class, 'DetailReservation_flight_info')]//span"));
    private static final Locator FIRST_PASSENGER_FULLNAME = new Locator("FIRST_PASSENGER_FULLNAME", new By.ByXPath("(//*[contains(@class, 'styles_train')]//span)[1]"));
    private static final Locator RELOAD_BUTTON = new Locator("RELOAD_BUTTON", new By.ByXPath("//button[contains(text(), 'Reload') or contains(text(), 'Try again') or contains(text(), 'Coba lagi')]"));
    private static final Locator LOOK_AT_OTHER_TRAINS_BUTTON = new Locator("LOOK_AT_OTHER_TRAINS_BUTTON", new By.ByXPath("//*[contains(text(), 'Lihat Kereta Lain') or contains(text(), 'Look at other trains')]"));
    private static final Locator TRAIN_SCHEDULE_LIST = new Locator("TRAIN_SCHEDULE_LIST", new By.ByXPath("(//div[contains(@class, 'ScheduleList_main_card')])"));
    private static final Locator PASSENGER_NAMES = new Locator("PASSENGER_NAMES", new By.ByXPath("//*[contains(@class, 'passenger-name-fullname')]"));
    private static final Locator PRICE_DETAIL = new Locator("PRICE_DETAIL", new By.ByXPath("//li[@class='row-label-price']"));

    private static final Locator dateLocator(String date) {
        return new Locator("DATE", new By.ByXPath(("(//span[contains(@class, 'Day_day_number__rQ1Bc') and text()='" + date + "'])[1]")));
    }

    public TrainOrderPage(Session<T> session) {
        super(session);
    }

    private String getEnvironmentUrl() {
        return switch (Configs.ENVIRONMENT) {
            case PROD -> PROD_CORP_DASHBOARD;
            case PREPROD -> PREPROD_CORP_DASHBOARD;
            case GK -> GK_CORP_DASHBOARD;
            default -> "";
        };
    }

    public void GoToTrainPage(){
        driverTools.click(TRAIN_BUTTON);
        driverTools.waitForElementToAppear(TRAIN_DEPARTURE_ICON, 10);
    }

    public void setDeparture(String departureStationCode){
        driverTools.click(SKIP_TUTORIAL);
        driverTools.click(SET_DEPARTURE_CITY_TEXTFIELD);
        driverTools.waitForElementToAppear(SELECT_DEPART_CITY);
        driverTools.click(SELECT_DEPART_CITY);
    }

    public void setArrival(String arrivalStationCode){
        driverTools.click(SET_ARRIVAL_CITY_TEXTFIELD);
        driverTools.waitForElementToAppear(SELECT_DESTINATION_CITY);
        driverTools.click(SELECT_DESTINATION_CITY);
    }

    public void selectStation(String stationCode){
        Locator SELECTED_STATION = new Locator("ARRIVAL_STATION", new By.ByXPath(String.format("//div[@class='station-code' and text()='%s']", stationCode.toUpperCase())));
        driverTools.waitForElementToAppear(SELECTED_STATION);
        driverTools.clickElementByCoordinateDWeb(SELECTED_STATION);
    }

    public void setDate(int daysToDepartureDate){
        daysToDepartureDate += 1;
        driverTools.click(CALENDAR_ICON);
        driverTools.waitForElementToDisappear(DATEPICKER_WIDGET);
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String departureDay = CoreFunctions.getSectionOfString(departureDate," ", 0);
        logger.log("Departing on: "+ departureDate);
        driverTools.waitForElementToPresence(dateLocator(departureDay),180);
        driverTools.clickElementByCoordinateDWeb(dateLocator(departureDay));
        driverTools.waitForElementToDisappear(DATEPICKER_WIDGET, 180);
    }

    public void setPassengers(int maturePassenger, int babyPassenger){
        driverTools.waitForElementToAppear(SELECT_PASSENGER);
        driverTools.click(SELECT_PASSENGER);
        addBabyPassenger(babyPassenger);
        clickSubmit();
    }

    public void clickSubmit(){
        driverTools.waitForElementToAppear(SUBMIT);
        driverTools.click(SUBMIT);
        driverTools.waitForElementToAppear(SEARCH_TRAIN);
        driverTools.click(SEARCH_TRAIN);
    }

    public void addMaturePassenger(int maturePassenger){
        for (int i = 1; i < maturePassenger; i++) {
            driverTools.click(ADD_MATURE_PASSENGER);
        }
    }

    public void addBabyPassenger(int babyPassenger){
        for (int i = 0; i < babyPassenger; i++) {
            driverTools.click(ADD_BABY_PASSENGER);
        }
    }

    public List<WebElement> searchTrain(){
        driverTools.waitForElementToAppear(TRAIN_ORDER_CARD);
        List<WebElement> trainTickets = driverTools.elements(TRAIN_SCHEDULE_LIST).orElseThrow();
        return trainTickets;
    }

    public void clickTrainCard(int i){
        driverTools.waitForElementToAppear(CLOSE_ONBOARDING_DIALOG);
        if (driverTools.isElementDisplayed(CLOSE_ONBOARDING_DIALOG)) {
            driverTools.click(CLOSE_ONBOARDING_DIALOG);
        }
        Locator TRAIN_ORDER_CARD = new Locator("CLOSE_PASSENGER_WIDGET", new By.ByXPath("(//div[contains(@class, 'ScheduleList_main_card')])["+i+"]"));
        driverTools.click(TRAIN_ORDER_CARD);
        driverTools.waitForElementToAppear(CONTACT_DETAIL_SECTION);
    }

    public void fillPassengerDetail(int passengers){
        boolean withBottomSheet = false;
        clickMrRadioButton(withBottomSheet);
        inputFirstPassengerName(withBottomSheet);
        clickPassportRadioButton();
        inputFirstPassengerPassport(withBottomSheet);
        if (passengers == 1) return;
        for (int i = 1; i< passengers; i++){
            setOtherPassengerDetail("not implemented", "yet");
        }
    }
    public void fillPassengerDetail1(String name){
        driverTools.waitForElementToAppear(MR_RADIO_CHECKBOX);
        driverTools.click(MR_RADIO_CHECKBOX);
        driverTools.sendKeys(Text_Bar, name);
        driverTools.scrollIntoWebView(PASSPORT_RADIO_BUTTON);
        driverTools.maximizeWindow();
        driverTools.waitForElementToAppear(PASSPORT_RADIO_BUTTON);
        driverTools.clickWithJavascriptExecutor(PASSPORT_RADIO_BUTTON);
        driverTools.clickWithJavascriptExecutor(ENTER_PASSPORT_VALUE);
        driverTools.sendKeys(ENTER_PASSPORT_VALUE,"1234554502");
    }

    public String name(){
        String Name = CoreFunctions.randomName();
        return Name;
    }

    public void findOtherTrains(List<WebElement> totalTrainOptions, int passengers) {
        boolean trainIsAvailable = false;
        for (int i = 2; i < totalTrainOptions.size(); i++) {
            clickTrainCard(i);
            fillPassengerDetail(passengers);
            trainIsAvailable = continueToSeatAndProtection();
            if (trainIsAvailable) {
                return;
            }
        }
        throw new NoSuchElementException("There are no available train tickets");
    }

    public void fillPassengerDetailWithBottomSheetUI(int passengers){
        boolean withBottomSheet = true;
        openFirstPassengerDetailBS();
        clickMrRadioButton(withBottomSheet);
        inputFirstPassengerName(withBottomSheet);
        clickPassportRadioButton();
        inputFirstPassengerPassport(withBottomSheet);
        savePassengerDetail();
        if (passengers == 1) return;
        for (int i = 1; i< passengers; i++){
            setOtherPassengerDetail("not implemented", "yet");
        }
    }

    public void savePassengerDetail(){
        driverTools.click(SAVE_PASSENGER_DETAIL);
        driverTools.waitForElementToDisappear(FIRST_PASSENGER_NAME_BS);
    }

    public void openFirstPassengerDetailBS(){
        driverTools.waitForElementToAppear(DETAIL_CARD_INNER_FIRST_PASSENGER, 60);
        driverTools.clickElementByCoordinateDWeb(DETAIL_CARD_INNER_FIRST_PASSENGER);
        driverTools.waitForElementToAppear(SAVE_PASSENGER_DETAIL).orElseThrow();
    }

    public void clickPassportRadioButton(){
        driverTools.waitForElementToAppear(FIRST_PASSENGER_PASSPORT_CHECKBOX);
        driverTools.waitForElementToBeClickable(FIRST_PASSENGER_PASSPORT_CHECKBOX);
        try {
            driverTools.click(FIRST_PASSENGER_PASSPORT_CHECKBOX);
        } catch (Exception e) {
            driverTools.clickElementByCoordinateDWeb(FIRST_PASSENGER_PASSPORT_CHECKBOX);
        }
    }

    public void inputFirstPassengerName(boolean withBS){
        String randomName = CoreFunctions.randomName();
        Locator PASSENGGER_NAME_LOCATOR;
        if (withBS) {PASSENGGER_NAME_LOCATOR = FIRST_PASSENGER_NAME_BS;}
        else PASSENGGER_NAME_LOCATOR = FIRST_PASSENGER_NAME;
        try {
            driverTools.click(PASSENGGER_NAME_LOCATOR);
        } catch (Exception e) {
            driverTools.clickElementByCoordinateDWeb(PASSENGGER_NAME_LOCATOR);
        }
        driverTools.sendKeys(PASSENGGER_NAME_LOCATOR, randomName);
    }

    public void inputFirstPassengerPassport(boolean withBS){
        String uniqueNIK = String.valueOf(CoreFunctions.generateRandomInt(100000,999999));
        Locator PASSENGER_NIK_LOCATOR;
        if (withBS) {PASSENGER_NIK_LOCATOR = FIRST_PASSENGER_NIK_BS;}
        else PASSENGER_NIK_LOCATOR = FIRST_PASSENGER_PASSPORT;

        driverTools.waitForElementToAppear(PASSENGER_NIK_LOCATOR, 60);
        driverTools.sendKeys(PASSENGER_NIK_LOCATOR, uniqueNIK);
    }

    public void clickMrRadioButton(boolean withBS){
        Locator MR_LOCATOR;
        if (withBS) {MR_LOCATOR = MR_RADIO_CHECKBOX_BS;}
        else MR_LOCATOR = MR_RADIO_CHECKBOX;
        driverTools.waitForElementToBeClickable(MR_LOCATOR);
        driverTools.clickWithJavascriptExecutor(MR_LOCATOR);
    }

    public void setOtherPassengerDetail(String name, String passport){
        //not implemented yet
    }

    public boolean continueToSeatAndProtection(){
        try {
            driverTools.click(CONTINUE_TO_SEAT_AND_PROTECTION);
        } catch (Exception e) {
            driverTools.clickElementByCoordinateDWeb(CONTINUE_TO_SEAT_AND_PROTECTION);
        }
        driverTools.waitForElementToAppear(CONTINUE_TO_PAYMENT, 20);
        if (driverTools.isElementDisplayed(RELOAD_BUTTON)) {
            driverTools.click(RELOAD_BUTTON);
            continueToSeatAndProtection();
        }
        if (driverTools.isElementDisplayed(LOOK_AT_OTHER_TRAINS_BUTTON)){
            return false;
        }
        return true;
    }

    public void continueToPayment(){
        boolean isSuccessful = false;
        for (int retryCounter = 0; retryCounter < 3; retryCounter++) {
            isSuccessful = methodSuccessChecker(CONTINUE_TO_PAYMENT, CONFIRM_CONTINUE_TO_PAYMENT);
            logger.log("Continue to payment successful? " + isSuccessful);
            if (isSuccessful == true){
                confirmToPaymentPage();
                return;
            }
        }
        if (!isSuccessful) {
            logger.log("Retried 3x but still can't advance to summon Continue to Payment Dialog");
            throw new NoSuchElementException();
        }

    }

    public void continuePayment(){
        driverTools.clickWithJavascriptExecutor(CONTINUE_TO_PAYMENT);
        driverTools.waitForElementToAppear(CONFIRM_CONTINUE_TO_PAYMENT);
        driverTools.scrollIntoWebView(CONFIRM_CONTINUE_TO_PAYMENT);
        driverTools.clickWithJavascriptExecutor(CONFIRM_CONTINUE_TO_PAYMENT);
        driverTools.waitForElementToAppear(CONFIRM_PAYMENT);
        driverTools.clickWithJavascriptExecutor(CONFIRM_PAYMENT);
        driverTools.click(INTRIM_PAGE);
    }
  
    public boolean methodSuccessChecker(Locator clickLocator, Locator successIndicator){
        try {
            logger.log("Default Click");
            driverTools.click(clickLocator);
            driverTools.waitForElementToAppear(successIndicator).orElseThrow();
        }  catch (DriverToolsException webDriverException) {
            logger.log("DWEB Coordinate Click");
            driverTools.clickElementByCoordinateDWeb(clickLocator);
            driverTools.waitForElementToAppear(successIndicator).orElseThrow();
        } catch (ElementClickInterceptedException elementClickInterceptedException) {
            logger.log("JS Click");
            driverTools.clickWithJavascriptExecutor(clickLocator);
            driverTools.waitForElementToAppear(successIndicator).orElseThrow();
        }
        catch (NoSuchElementException noSuchElementException) {
            logger.log("Unable to click with JS Executor");
            if (driverTools.isElementDisplayed(RELOAD_BUTTON)){
                return false;
            }
            logger.log("Can't continue and no reload dialog is present");
            throw new NoAlertPresentException();
        }
        return true;
    }

    public void confirmToPaymentPage(){
        boolean isSuccessful = false;
        for (int retryCounter = 0; retryCounter<3; retryCounter++){
            retryCounter++;
            isSuccessful = methodSuccessChecker(CONFIRM_CONTINUE_TO_PAYMENT, SUBMIT_PAYMENT);
            if (isSuccessful == true){
                return;
            }
        }
        if (!isSuccessful) {
            logger.log("Retried 3x but still can't advance to Payment Page");
            throw new NoSuchElementException();
        }
    }

    public void confirmPayment(){
        driverTools.waitForElementToAppear(SUBMIT_PAYMENT);
        try{
            driverTools.click(SUBMIT_PAYMENT);
        } catch (StaleElementReferenceException staleElementReferenceException) {
            driverTools.clickElementByCoordinateDWeb(SUBMIT_PAYMENT);
        }
        driverTools.waitForElementToAppear(CORPORATE_BILLING_SUCCESS);
    }

    public void goToMyOrders() {
        String url = getEnvironmentUrl() + "/yourorder";
        driverTools.get(url);
        driverTools.waitForElementToAppear(ORDER_CARD_COUNTDOWN);
    }

    public String getOrderID(){
        driverTools.waitForElementToAppear(ORDER_ID);
        String text1 = String.valueOf(driverTools.getText(ORDER_ID));
        String orderID = text1.split("Order ID: ")[1].trim().replace("]", "");
        System.out.println("New OrderId " + orderID);
        System.out.println("New OrderId = " + orderID);
        return orderID;
    }

    public HashMap<String, Object> getOrderDetails(String email, String orderID, int maturePassenger, int babyPassenger){
        goToOrderDetail();
        HashMap<String, Object> orderDetailsMap = new HashMap<>();
        orderDetailsMap.put("email", email);
        orderDetailsMap.put("orderIDFromDetail", orderID);
        orderDetailsMap.put("departureStation", getDepartureStation());
        orderDetailsMap.put("arrivalStation", getArrivalStation());
        orderDetailsMap.put("departureDate", getDepartureDate());
        orderDetailsMap.put("totalPrice", getTotalPrice());
        orderDetailsMap.put("trainName", getTrainName().split("-")[0]);
        orderDetailsMap.put("trainClass", getTrainName().split("-")[1].split("\\(")[0].trim());
        if (maturePassenger == 1 && babyPassenger == 0) {
            orderDetailsMap.put("passengers", getOnePassenger());
        } else {
            orderDetailsMap.put("passengers", getPassengers());
        }
        return  orderDetailsMap;
    }

    public String getDepartureStation() {
        driverTools.waitForElementToAppear(DEPARTURE_STATION, 60);
        String departureStation = driverTools.getText(DEPARTURE_STATION).orElseThrow().toLowerCase();
        departureStation = departureStation.split(" - ")[0].trim();
        return departureStation;
    }

    public String getArrivalStation(){
        String arrivalStation = driverTools.getText(ARRIVAL_STATION).orElseThrow().toLowerCase();
        arrivalStation = arrivalStation.split(" - ")[0].trim();
        return arrivalStation;
    }

    public String getDepartureDate(){
        String departureDate = driverTools.getText(DEPARTURE_DATE).orElseThrow().toLowerCase();
        departureDate = departureDate.split(", ")[1];
        departureDate = departureDate.split(" -")[0];
        departureDate = departureDate.trim().toLowerCase();
        return departureDate;
    }

    public String getTotalPrice(){
        return driverTools.getText(TOTAL_PRICE).orElseThrow().replace(",", ".");
    }

    public String getTrainName() {
        driverTools.waitForElementToAppear(TRAIN_NAME, 60);
        return driverTools.getText(TRAIN_NAME).orElseThrow().toLowerCase();
    }

    public ArrayList<String> getPassengers(){
        driverTools.clickWithJavascriptExecutor(EXPAND_PASSENGER_DETAIL);
        driverTools.waitForElementToPresence(PASSENGER_COLLAPSED);
        List<WebElement> passengerNameElements = driverTools.elements(PASSENGER_NAMES).orElseThrow();
        ArrayList<String> passengers = new ArrayList<>();
        for (WebElement passengerNameElement : passengerNameElements) {
            String passengerName = driverTools.getText(passengerNameElement);
            passengers.add(passengerName);
        }
        return passengers;
    }

    public ArrayList<String> getOnePassenger() {
        ArrayList<String> passenger = new ArrayList<>();
        String passengerName = driverTools.getText(FIRST_PASSENGER_FULLNAME).orElseThrow();
        passengerName = passengerName.split("\\. ")[1];
        passengerName = passengerName.toLowerCase();
        passengerName = passengerName.trim();
        passenger.add(passengerName);
        return passenger;
    }

    public HashMap<String, String> getPriceDetails(){
        driverTools.clickWithJavascriptExecutor(PRICE_DETAIL_COLLAPSE);
        driverTools.waitForElementToPresence(PRICE_DETAIL_COLLAPSED);
        List<WebElement> priceDetailRows = driverTools.elements(PRICE_DETAIL).orElseThrow();
        HashMap<String, String> priceDetailBreakdown = new HashMap<>();
        for (WebElement priceDetailRow : priceDetailRows) {
            String feeName = driverTools.getText(priceDetailRow.findElement(By.xpath("//div//span")));
            String fee = driverTools.getText(priceDetailRow.findElement(By.xpath("//p")));
            fee = fee.replace(",", ".");
            priceDetailBreakdown.put(feeName, fee);
        }
        return priceDetailBreakdown;
    }

    public void goToOrderDetail(){
        driverTools.click(ORDER_CARD);
        driverTools.waitForElementToAppear(TRAIN_DETAIL_HEADER);
    }

    public void printMap(HashMap orderDetailMap) {
        List arrayListOrderMap = Arrays.asList(orderDetailMap); // method 1
        logger.log("From Your Orders: " + arrayListOrderMap);
    }
}