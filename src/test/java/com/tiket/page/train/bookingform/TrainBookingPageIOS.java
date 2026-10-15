package com.tiket.page.train.bookingform;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.common.InsuranceGeneralData;
import com.tiket.pojo.train.TrainData;
import com.tiket.pojo.train.TrainPriceDetail;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;


public class TrainBookingPageIOS<T extends WebDriver> extends TrainBookingPage<T> {

    public static final Locator TITLE_GUEST = new Locator("TITLE_RADIO", AppiumBy.xpath("(//XCUIElementTypeOther[@name='title_radio'])[1]"));
    public static final Locator TITLE_PASSENGER = new Locator("TITLE_PASSENGER", AppiumBy.xpath("(//XCUIElementTypeOther[@name='title_radio_passenger'])[1]"));
    public static final Locator FULL_NAME = new Locator("FULL_NAME", AppiumBy.accessibilityId("fullName"));
    public static final Locator FULL_NAME_PASSENGER = new Locator("FULL_NAME_PASSENGER", AppiumBy.xpath("//*[@name='fullName' and ./parent::*[./parent::*[@type='XCUIElementTypeOther']]]"));
    public static final Locator PHONE = new Locator("PHONE", AppiumBy.name("phone"));
    public static final Locator ID_NUMBER = new Locator("ID_NUMBER",AppiumBy.accessibilityId("identityNumber"));
    public static final Locator DOB = new Locator("DOB",AppiumBy.xpath("//*[@name='Date of Birth' or @name='Tanggal Lahir']"));
    public static final Locator DONE_BTN = new Locator("DONE_BTN",AppiumBy.xpath("//*[@name='Selesai' or @name='Done' and @type='XCUIElementTypeButton']"));
    public static final Locator SAME_AS_CONTACT_DETAIL_SWITCH = new Locator("SAME_AS_CONTACT_DETAIL_SWITCH",AppiumBy.accessibilityId("switch"));
    public static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON",AppiumBy.xpath("//XCUIElementTypeButton[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'save') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'simpan')]"));
    public static final Locator SELECT_SEAT_BUTTON = new Locator("SELECT_SEAT_BUTTON",AppiumBy.xpath("//XCUIElementTypeButton[@name='Pilih Kursi' or @name='Select Seat']"));
    public static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON",AppiumBy.accessibilityId("ic cancel grey"));
    public static final Locator CONTINUE_PAYMENT_BUTTON = new Locator("CONTINUE_PAYMENT_BUTTON",AppiumBy.name("continue_payment_button"));
    public static final Locator CONFIRM_PAYMENT_BUTTON = new Locator("CONFIRM_PAYMENT_BUTTON",AppiumBy.name("confirm_payment_button"));
    public static final Locator EMAIL = new Locator("EMAIL",AppiumBy.id("emailAddress"));
    public static final Locator BTN_CONTINUE = new Locator("BTN_CONTINUE",AppiumBy.name("continue_button"));
    public static final Locator CHOOSE_SEAT = new Locator("CHOOSE_SEAT",AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == 'unselected_seat'`][1]"));
    public static final Locator DROPDOWN_DEPART = new Locator("DROPDOWN_DEPART",AppiumBy.id("drop_down_departure"));
    public static final Locator DROPDOWN_RETURN = new Locator("DROPDOWN_RETURN",AppiumBy.id("drop_down_return"));
    public static final Locator SEAT_DEPART = new Locator("SEAT_DEPART",AppiumBy.id("change_seat_departure"));
    public static final Locator SEAT_RETURN = new Locator("SEAT_RETURN",AppiumBy.id("change_seat_return"));
    public static final Locator BUTTONS = new Locator("BUTTONS", AppiumBy.xpath("//XCUIElementTypeButton[@visible='true']"));
    public static final Locator ID = new Locator("ID",AppiumBy.xpath("//XCUIElementTypeStaticText[@label='KTP' or @label='ID']"));
    public static final Locator SELECT_INSURANCE = new Locator("SELECT_INSURANCE", AppiumBy.name("add_insurance_button"));
    public static final Locator INSURANCE_PRICE = new Locator("INSURANCE_PRICE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'/p')]"));
    public static final Locator PRICE_DETAIL_ARROW = new Locator("PRICE_DETAIL_ARROW",AppiumBy.xpath("//*[@name='titleView']//*[@name='leftTextChevronImage' and @visible='true']"));
    public static final Locator CLOSED_PRICE_DETAIL = new Locator("CLOSED_PRICE_DETAIL",AppiumBy.id("ic cancel grey"));
    public static final Locator TOTAL_PRICE_PRICE_DETAIL = new Locator("TOTAL_PRICE_PRICE_DETAIL",AppiumBy.xpath("//*[@name='totalView']//*[@name='rightText']"));
    private static final Locator LABEL_INSURANCE = new Locator("LABEL_INSURANCE", AppiumBy.accessibilityId("insurance_label"));
    public static final Locator DATE_AND_TIME = new Locator("DATE_AND_TIME",AppiumBy.accessibilityId("date_departure"));
    public static final Locator PASSENGER_NAME_1 = new Locator("PASSENGER_NAME_1",AppiumBy.accessibilityId("passenger_name"));
    public static final Locator SEAT_DETAILS = new Locator("SEAT_DETAILS",AppiumBy.accessibilityId("passenger_seat"));
    public static final Locator TITLE_INSURANCE = new Locator("TITLE_INSURANCE",AppiumBy.xpath("//*[contains(@label,'Extra')]"));
    private static final Locator BTN_JOIN_NOW = new Locator("BTN_JOIN_NOW",AppiumBy.xpath("//*[contains(@name,'Log in')]"));
    private static final Locator CONTACT_CARD = new Locator("CONTACT_CARD", AppiumBy.accessibilityId("contact_detail_card"));
    private static final Locator DEPARTURE_ORIGIN_STATION_IN_DEPARTURE_SEAT = new Locator("DEPARTURE_ORIGIN_STATION_IN_DEPARTURE_SEAT", AppiumBy.accessibilityId("stations_departure"));
    private static final Locator RETURN_ORIGIN_STATION_IN_DEPARTURE_SEAT = new Locator("RETURN_ORIGIN_STATION_IN_DEPARTURE_SEAT", AppiumBy.accessibilityId("stations_return"));
    private static final Locator PRICES_BREAKDOWN = new Locator("PRICES_BREAKDOWN", AppiumBy.xpath("//*[@name='itemView' or @name='groupView']/*[@name='rightText' and @label] | //*[@name='titleView']/*[@name='rightText' and contains(@label,'-')]"));
    private static final Locator EDIT_PASSENGER = new Locator("EDIT_PASSENGER", AppiumBy.xpath("//XCUIElementTypeOther[@name='switch']/preceding-sibling::*"));
    private static final Locator TRAIN_NAME = new Locator("TRAIN_NAME", AppiumBy.xpath("//XCUIElementTypeStaticText[@label=\"Departure\" or @name=\"Pergi\"]/preceding-sibling::*[contains(@label,'•')]"));
    private static final Locator ADULT_NAME = new Locator("ADULT_NAME", AppiumBy.xpath("(//XCUIElementTypeButton[@name=\"body_btn\"])[1]/following-sibling::XCUIElementTypeStaticText"));
    private static final Locator INFANT_NAME = new Locator("INFANT_NAME", AppiumBy.xpath(" (//XCUIElementTypeButton[@name=\"body_btn\"])[2]/preceding-sibling::XCUIElementTypeStaticText"));
    private static final Locator FIRST_INSURANCE_EXTRA_PROTECTION_TITLE = new Locator("FIRST_INSURANCE_EXTRA_PROTECTION_TITLE", AppiumBy.xpath("(//*[contains(@name,'insurance_title') and @visible='true'])[1]"));
    private static final Locator FIRST_INSURANCE_EXTRA_PROTECTION_CANCEL_BTN = new Locator("FIRST_INSURANCE_EXTRA_PROTECTION_CANCEL_BTN", AppiumBy.xpath("(//*[contains(@name,'insurance_title')])[1]//following-sibling::*[contains(@label,'sudah dilindungi') or contains(@label,'added protection')]//following-sibling::*[(contains(@name,'add_insurance_button') or @label='Cancel' or @label='Batalkan') and @visible='true']"));
    private static final Locator FIRST_INSURANCE_EXTRA_PROTECTION_BTN = new Locator("FIRST_INSURANCE_EXTRA_PROTECTION_BTN", AppiumBy.xpath("(//*[contains(@name,'insurance_title')])[1]//following-sibling::XCUIElementTypeButton"));
    private static final Locator FIRST_INSURANCE_EXTRA_PROTECTION_ADD_BTN = new Locator("FIRST_INSURANCE_EXTRA_PROTECTION_ADD_BTN", AppiumBy.xpath("(//*[contains(@name,'insurance_title')])[1]//following-sibling::*[contains(@name,'add_insurance_button')]"));
    private static final Locator FIRST_INSURANCE_EXTRA_PROTECTION_BASE_PRICE = new Locator("FIRST_INSURANCE_EXTRA_PROTECTION_BASE_PRICE", AppiumBy.xpath("(//*[contains(@name,'insurance_title')])[1]//following-sibling::*[contains(@label,'IDR') and (contains(@label,'/passenger') or contains(@label,'/penumpang'))]"));
    private static final Locator SUBTOTAL_EXPAND_BUTTON = new Locator("SUBTOTAL_EXPAND_BUTTON", AppiumBy.xpath("//*[@name='groupView']/*[@name='leftTextChevronImage']"));

    public TrainBookingPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public void fillGuestDetail(String email){
        driverTools.swipeUp();
        if(driverTools.waitForElementToAppear(PHONE).isPresent()){
            driverTools.click(TITLE_GUEST);
            driverTools.sendKeys(FULL_NAME,CoreFunctions.randomName());
            fillPhone();
            driverTools.sendKeys(EMAIL,email);
            driverTools.hideKeyboard();
        }
    }

    @Override
    public void continueToPayment() {
        driverTools.swipeToElementTillPresented(CONTINUE_PAYMENT_BUTTON,Swipe.UP,10);
        driverTools.click(CONTINUE_PAYMENT_BUTTON);
        driverTools.click(CONFIRM_PAYMENT_BUTTON);
    }

    private void closeInfantPopup(){
        List<WebElement> buttons = driverTools.elements(BUTTONS).orElseThrow();
        if (buttons.size() == 1) {
            buttons.get(0).click();
        }
    }

    @Override
    @SneakyThrows
    public void selectSeat(String triptype,int adults) {
        if (driverTools.isElementDisplayed(SELECT_SEAT_BUTTON)) {
            driverTools.click(SELECT_SEAT_BUTTON);

            boolean inPage = driverTools.waitForElementToAppear(SAVE_BUTTON, 30).isPresent();

            if(!inPage){
                closeInfantPopup();
            }

            driverTools.clickElementByCoordinate(CHOOSE_SEAT);
            driverTools.click(SAVE_BUTTON);

            if (triptype.equals("roundtrip")) {
                driverTools.clickElementByCoordinate(CHOOSE_SEAT);
                driverTools.click(SAVE_BUTTON);
            }
        }
        else {
            boolean inPage = driverTools.waitForElementToAppear(DROPDOWN_DEPART,30).isPresent();
            if(!inPage){
                closeInfantPopup();
            }
            if (triptype.equals("roundtrip")) {
                //change seat element sometime missing after changing depart seat 
                //so clicking dropdown return first so the element get loaded by appium
                driverTools.swipeToElement(DROPDOWN_RETURN , Swipe.UP, 2);
                driverTools.click(DROPDOWN_RETURN);
                //select depart seat first
                driverTools.click(SEAT_DEPART);
                driverTools.clickElementByCoordinate(CHOOSE_SEAT);
                driverTools.click(SAVE_BUTTON);
                driverTools.swipeToElement(SEAT_RETURN, Swipe.UP, 3);
                WebElement seatReturnElement = driverTools.waitForElementToAppear(SEAT_RETURN,10).orElseThrow();
                driverTools.click(seatReturnElement);
                driverTools.clickElementByCoordinate(CHOOSE_SEAT);
                driverTools.click(SAVE_BUTTON);
            }else{
                driverTools.click(SEAT_DEPART);
                driverTools.clickElementByCoordinate(CHOOSE_SEAT);
                driverTools.click(SAVE_BUTTON);
            }
        }
    }

    @Override
    public TrainData fillPassengerDetail(int adults, int infants) {
        TrainData trainData = new TrainData();
        ArrayList<String> passengerNames = new ArrayList<>();
        ArrayList<String> toggledFirstNameAndId = toggleSameAsContact();
        ArrayList<String> idList = new ArrayList<>();

        driverTools.swipeToElement(SELECT_SEAT_BUTTON, Swipe.UP,2);

        for(int x=2;x<=(adults+infants);x++){
            Locator passengerButton = getPassengerButton(x);
            WebElement passengerButtonElement = driverTools.waitForElementToPresence(passengerButton, 4).orElseThrow();
            driverTools.click(passengerButtonElement);
            driverTools.waitForElementToAppear(CLOSE_BUTTON);
            fillTitle();
            passengerNames.add(fillName());
            if(x>adults){
                fillDob();
            }
            idList.add(fillId());
            driverTools.click(SAVE_BUTTON);
        }
        passengerNames.add(0,toggledFirstNameAndId.get(0));
        idList.add(0,toggledFirstNameAndId.get(1));
        trainData.guestList = passengerNames;
        trainData.idNumberList = idList;
        return trainData;
    }

    private Locator getPassengerButton(int index){
        return new Locator("Passenger Button",AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'"+index+" (')]"));
    }
    
    private ArrayList<String> toggleSameAsContact(){
        driverTools.swipeToElement(SAME_AS_CONTACT_DETAIL_SWITCH, Swipe.UP, 2);
        driverTools.click(SAME_AS_CONTACT_DETAIL_SWITCH);
        String id = fillId();
        WebElement nameElement = driverTools.waitForElementToAppear(FULL_NAME_PASSENGER).orElseThrow();
        String name = driverTools.getValueAttribute(nameElement,"value");
        driverTools.click(SAVE_BUTTON);
        ArrayList<String> nameAndId = new ArrayList<String>();
        nameAndId.add(name);
        nameAndId.add(id);
        return nameAndId;
    }

    private String fillName(){
        String name = CoreFunctions.randomName() +" Test" ;
        driverTools.sendKeys(FULL_NAME_PASSENGER,name);
        driverTools.hideKeyboard(1);
        return name;
    }
    private void fillPhone(){
        String phone = String.valueOf(CoreFunctions.randomInt(10));
        driverTools.hideKeyboard();
        driverTools.sendKeys(PHONE,"8"+phone);
        driverTools.hideKeyboard(1);
    }
    private void fillTitle(){
        List<WebElement> titleRadio = driverTools.elements(TITLE_PASSENGER).orElseThrow();
        driverTools.click(titleRadio.get(CoreFunctions.generateRandomInt(0,titleRadio.size()-2)));
    }
    private String fillId(){
        driverTools.waitForElementToAppear(ID).ifPresent(driverTools::click);
        driverTools.clear(ID_NUMBER);
        String id = String.valueOf(CoreFunctions.randomInt(7));
        String finalId = "327104190"+id;
        driverTools.sendKeys(ID_NUMBER,finalId);
        driverTools.hideKeyboard(1);
        return finalId;
    }

    private void fillDob(){
        if(driverTools.waitForElementToPresence(DOB,3).isEmpty())
            return;
        driverTools.click(DOB);
        driverTools.click(DONE_BTN);
    }

    @Override
    public InsuranceGeneralData selectInsurance(){
        InsuranceGeneralData insuranceGeneralData = new InsuranceGeneralData();
        driverTools.swipeToElement(TITLE_INSURANCE,Swipe.UP ,2);

        WebElement titleElement = driverTools.swipeToElement(FIRST_INSURANCE_EXTRA_PROTECTION_TITLE,Swipe.UP,5).orElseThrow();
        String title = driverTools.getText(titleElement);
        logger.log("title: " + title + " will be added for insurance verification");
        insuranceGeneralData.insuranceTitle = title;

        driverTools.swipeToElement(FIRST_INSURANCE_EXTRA_PROTECTION_BTN,Swipe.UP,2);
        boolean isInsuranceAdded = false;
        if (driverTools.waitForElementToPresence(FIRST_INSURANCE_EXTRA_PROTECTION_CANCEL_BTN).isPresent()) {
            driverTools.swipeToElement(FIRST_INSURANCE_EXTRA_PROTECTION_CANCEL_BTN,Swipe.UP,2);
            isInsuranceAdded = driverTools.waitForElementToPresence(FIRST_INSURANCE_EXTRA_PROTECTION_CANCEL_BTN).isPresent();
        }
        logger.log("is insurance already added " + isInsuranceAdded);
        if(!isInsuranceAdded){
            driverTools.click(FIRST_INSURANCE_EXTRA_PROTECTION_ADD_BTN);
        }

        WebElement insurancePriceElement = driverTools.swipeToElement(FIRST_INSURANCE_EXTRA_PROTECTION_BASE_PRICE,Swipe.UP,2).orElseThrow();
        String insurancePrice = removeExtraData(driverTools.getText(insurancePriceElement));
        logger.log("selected insurance price : " + insurancePrice);
        insuranceGeneralData.insurancePrice = insurancePrice;
        return insuranceGeneralData;
    }

    @Override
    public void continueTo2ndHalf(){
        WebElement cntBtn = driverTools.waitForElementToAppear((BTN_CONTINUE)).orElseThrow();
        driverTools.click(cntBtn);
    }

    @Override
    public void openPriceDetail(String tripType){
        driverTools.swipeToElement(CONTINUE_PAYMENT_BUTTON,Swipe.UP,10);
        WebElement priceDetailArrow = driverTools.waitForElementToAppear(PRICE_DETAIL_ARROW).orElseThrow();
        driverTools.click(priceDetailArrow);
    }

    @Override
    public void closePriceDetail(){
        if (driverTools.isElementDisplayed(CLOSED_PRICE_DETAIL)) {
            driverTools.click(CLOSED_PRICE_DETAIL);
        } else {
            logger.log("CLOSED_PRICE_DETAIL is not displayed, cannot close price detail");
        }
    }


    @Override
    public boolean isPriceSumCorrect (){
        List<WebElement> prices = driverTools.elements(PRICES_BREAKDOWN).orElseThrow();

        ArrayList<Double> arrayPrice = new ArrayList<>();
        for (WebElement element : prices) {
            String text = driverTools.getText(element);
            logger.log("price text: " + text);
            try {
                double price = CoreFunctions.convertPrice(text);
                arrayPrice.add(price);
            } catch (NumberFormatException message) {
                logger.log(message.getMessage());
            }
        }
        logger.log("arrayPrice-> " + arrayPrice);

        double sum = 0;
        for (double i : arrayPrice) {
            sum += i;
        }

        String price = driverTools.getText(TOTAL_PRICE_PRICE_DETAIL).orElseThrow();
        double totalPrice = CoreFunctions.convertPrice(price);
        logger.log("Total price text:  " + totalPrice);
        logger.log("Total price got:  " + sum);
        return totalPrice == sum;
    }

    @Override
    public boolean isInsuranceShow(){
        boolean insuranceLabel = driverTools.isElementDisplayed(LABEL_INSURANCE);
        return insuranceLabel;
    }

    @Override
    public boolean isPriceBreakdownCorrect(TrainPriceDetail priceDetail){
        boolean priceCorrect = isPriceSumCorrect();
        logger.log("Total price breakdown correct: " + priceCorrect);
        return  priceCorrect;
    }

    private String convertSeatTrain(String seat){
        seat = seat.replace("/","-");
        return seat;
    }

    private String removeDayOfWeek(String date) {
        return date.replaceFirst("^.*?,\\s*","");
    }

    //there are some encoding problems where while getting text from element some character are displayed as something else
    //ex:"Surabaya Gubeng" is displayed as "Surabaya�Gubeng" causing test to fail
    private String replaceUnwantedChar(String data){
        return data.replaceAll("[^A-Za-z_0-9]"," ");
    }

    private String convertMonthToEng(String depDate, String language){
        String[] newDate = depDate.split(" ");
        if(language.isEmpty()){
            newDate[1] = CoreFunctions.monthShortToShortIdToEng(newDate[1]);
        }

        StringBuilder convertedDate= new StringBuilder();
        for(String x:newDate){
            convertedDate.append(x);
            convertedDate.append(" ");

        }
        depDate = String.valueOf(convertedDate).substring(0,convertedDate.length()-1);
        return depDate;
    }

    private String getArrivalDate(String depDate,String startTime,String endTime, String language){
        String country = language.equalsIgnoreCase("en") ? "US" : "ID";
        Locale loc = new Locale(language.toLowerCase(), country);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM HH:mm",loc);
        SimpleDateFormat ansDateFormat = new SimpleDateFormat("dd MMMM",loc);
        try {
            // Parse the departure date and start time
            Date start = sdf.parse(depDate + " " + startTime);
            // Parse the departure date and end time
            Date end = sdf.parse(depDate + " " + endTime);
            // Check if the end time is before the start time
            if (end.before(start)) {
                // Add one day to the end date
                end.setTime(end.getTime() + 24 * 60 * 60 * 1000);
            }
            // Format the end date
            String finalDate = ansDateFormat.format(end);
            String[] newDate = finalDate.split(" ");
            logger.log("arrival date is : " + finalDate);
            newDate[1] = newDate[1].substring(0,3);
            StringBuilder convertedDate= new StringBuilder();
            for(String x:newDate){
                convertedDate.append(x);
                convertedDate.append(" ");

            }
            finalDate = String.valueOf(convertedDate).substring(0,convertedDate.length()-1);
            return finalDate;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private String formatDate(String date){
        String[] newDate = date.split(" ");
        String monthConvert = CoreFunctions.monthShortToShortIdToEng(newDate[1]);
        if(monthConvert != null){
            newDate[1] = monthConvert;
        }
        newDate[2] = "20" + newDate[2];
        if(newDate[0].length()!=2)
            newDate[0] = "0"+newDate[0];
        StringBuilder convertedDate= new StringBuilder();
        for(String x:newDate){
            convertedDate.append(x);
            convertedDate.append(" ");

        }
        date = String.valueOf(convertedDate).substring(0,convertedDate.length()-1);
        return date;
    }

    private String getInfoUsingLocator(Locator locator){
        WebElement dataElement = driverTools.waitForElementToAppear(locator).orElseThrow();
        String data = driverTools.getText(dataElement);
        return data;
    }

    @Override
    public TrainData getDetailsTrainAndPassenger(String language) {
        TrainData data = new TrainData();

        String departureStation = driverTools.getText(DEPARTURE_ORIGIN_STATION_IN_DEPARTURE_SEAT).orElseThrow();
        departureStation = departureStation.replaceAll("[^\\x00-\\x7F]", " ");
        logger.log("departure statsion : " + departureStation);

        String arrivalStation = driverTools.getText(RETURN_ORIGIN_STATION_IN_DEPARTURE_SEAT).orElseThrow();
        arrivalStation = arrivalStation.replaceAll("[^\\x00-\\x7F]", " ");
        logger.log("arrival statsion : " + arrivalStation);

        data.departureStation = departureStation;
        data.arrivalStation = arrivalStation;

        String dateAndTime = removeDayOfWeek(getInfoUsingLocator(DATE_AND_TIME));
        logger.log("date and time : " + dateAndTime);
        String[] dateAndTimeArray = dateAndTime.split(" • ");
        String[] timeArray = dateAndTimeArray[1].split(" - ");
        logger.log("Date and time array : " + dateAndTimeArray);

        String startTime = timeArray[0];
        String endTime = timeArray[1];

        data.departureDate = dateAndTimeArray[0];
        logger.log("departure date : " + data.departureDate);
        data.arrivalDate = getArrivalDate(data.departureDate, startTime, endTime, language);
        logger.log("arrival date : " + data.arrivalDate);
        data.departureTime = startTime;
        logger.log("departure time : " + data.departureTime);
        data.arrivalTime = endTime;
        logger.log("arrival time : " + data.arrivalTime);

        String passengerName = getInfoUsingLocator(PASSENGER_NAME_1);
        logger.log("passenger name : " + passengerName);
        data.guestName = passengerName;

        String seatDetail = getInfoUsingLocator(SEAT_DETAILS);
        data.seat = convertSeatTrain(seatDetail);
        logger.log("seat details : " + seatDetail);

        String trainName = driverTools.getValueAttribute(TRAIN_NAME, "value");
        data.trainName = trainName;
        logger.log("Train name details : " + trainName);
        return data;
    }

    @Override
    public String getTotalPayment(){
        driverTools.swipeToElement(CONTINUE_PAYMENT_BUTTON,Swipe.UP,10);
        WebElement totalPaymentElement = driverTools.waitForElementToAppear(TOTAL_PRICE_PRICE_DETAIL).orElseThrow();
        String totalPayment = driverTools.getText(totalPaymentElement);
        logger.log("Total payment: "+totalPayment);
        return totalPayment;
    }

    @Override
    public boolean isLoggedIn(){
        return driverTools.waitForElementToAppear(CONTACT_CARD).isPresent();
    }

    @Override
    public void openLoginPage() {
        driverTools.swipeToElement(BTN_JOIN_NOW, Swipe.UP,2);
        driverTools.click(BTN_JOIN_NOW);
    }

    @Override
    public boolean verifyContactDetailData(String email, String phoneNumber, String fullName) {
        driverTools.swipeUp();
        WebElement contactCard = driverTools.waitForElementToAppear(CONTACT_CARD).orElseThrow();
        driverTools.click(contactCard);
        String actualFullName = driverTools.getValueAttribute(FULL_NAME,"value");
        Assertion.assertThat("Verify full name is the same as registered", actualFullName, containsString(fullName));
        String actualPhoneNumber =driverTools.getValueAttribute(PHONE,"value");
        Assertion.assertThat("Verify phone number is the same as registered", actualPhoneNumber, containsString(phoneNumber));
        String actualEmail =driverTools.getValueAttribute(EMAIL,"value");
        Assertion.assertThat("Verify phone number is the same as registered", actualEmail, containsString(email));
        return true;
    }

    @Override
    public String getPassengerId(){
        driverTools.click(EDIT_PASSENGER);
        String id =  driverTools.getText(ID_NUMBER).orElseThrow();
        driverTools.click(SAVE_BUTTON);
        return id;
    }

    @Override
    public List<String> getInsurancePrices(){
        driverTools.swipeToElement(SELECT_INSURANCE,Swipe.UP,4);
        List<String> priceItems = new ArrayList<>(driverTools.getTexts(INSURANCE_PRICE));
        Assertion.assertThat("Prices exist", !priceItems.isEmpty(),is(true));
        logger.log("prices: "+priceItems);
        return priceItems;
    }

    @Override
    public List<String> getPriceDetailPrices(){
        List<String> priceItems = new ArrayList<>(driverTools.getTexts(PRICES_BREAKDOWN));
        Assertion.assertThat("Prices exist", !priceItems.isEmpty(),is(true));
        String totalPrice = driverTools.getText(TOTAL_PRICE_PRICE_DETAIL).orElseThrow();
        priceItems.add(totalPrice);
        logger.log("Prices: " + priceItems);
        return priceItems;
    }

    @Override
    public boolean verifyFirstPageStepper() {
        //TODO fix method after this task is unblocked
        //https://borobudur.atlassian.net/browse/QAAUT-13542
        throw new UnsupportedOperationException("Unimplemented method 'verifyFirstPageStepper'");
    }

    @Override
    public boolean verifySecondPageStepper() {
         //TODO fix method after this task is unblocked
        //https://borobudur.atlassian.net/browse/QAAUT-13542
        throw new UnsupportedOperationException("Unimplemented method 'verifySecondPageStepper'");
    }

    /**
     * Fetches the actual price displayed on the price detail without running assertions.
     */
    private double getActualPriceFromDetail(String type, int totalPax, String label, int index) {
        Locator priceLocator = getPriceLocator(type, label, totalPax, index);
        WebElement priceElement = driverTools.waitForElementToAppear(priceLocator).orElseThrow();
        String actualPrice = driverTools.getText(priceElement);
        return CoreFunctions.convertPriceSafely(actualPrice);
    }

    /**
     * Verifies that the price details for adult, infant, and insurance match the expected values
     * in the price breakdown for both departure and return trips.
     *
     * @param trainAndWhooshGeneralData the general data containing expected prices and passenger counts
     * @return true if all price details match, false otherwise
     */
    @Override
    public boolean verifyGeneralDataPriceDetail(TrainAndWhooshGeneralData trainAndWhooshGeneralData) {
        boolean isRoundTrip = "roundtrip".equalsIgnoreCase(trainAndWhooshGeneralData.tripType);
        String srpFirstCardPricePerPaxDepart;
        String srpFirstCardPricePerPaxReturn;
        if(trainAndWhooshGeneralData.slashedPricePerPaxDepart != null && !trainAndWhooshGeneralData.slashedPricePerPaxDepart.isEmpty()) {
            srpFirstCardPricePerPaxDepart = trainAndWhooshGeneralData.slashedPricePerPaxDepart;
        } else {
            srpFirstCardPricePerPaxDepart = trainAndWhooshGeneralData.srpFirstCardPricePerPaxDepart;
        }
        if(trainAndWhooshGeneralData.slashedPricePerPaxReturn != null && !trainAndWhooshGeneralData.slashedPricePerPaxReturn.isEmpty()) {
            srpFirstCardPricePerPaxReturn = trainAndWhooshGeneralData.slashedPricePerPaxReturn;
        } else {
            srpFirstCardPricePerPaxReturn = trainAndWhooshGeneralData.srpFirstCardPricePerPaxReturn;
        }
        boolean checkAdultPriceDepart = checkPriceOnPriceDetail(
                "adult",
                srpFirstCardPricePerPaxDepart,
                trainAndWhooshGeneralData.adultPassenger,
                "",
                1,
                isRoundTrip
        );
        boolean checkAdultPriceReturn = !isRoundTrip || checkPriceOnPriceDetail(
                "adult",
                srpFirstCardPricePerPaxReturn,
                trainAndWhooshGeneralData.adultPassenger,
                "",
                2,
                true
        );

        boolean checkInfantPriceDepart = checkPriceOnPriceDetail(
                "infant",
                "0",
                trainAndWhooshGeneralData.infantPassenger,
                "",
                1,
                isRoundTrip
        );
        boolean checkInfantPriceReturn = !isRoundTrip || checkPriceOnPriceDetail(
                "infant",
                "0",
                trainAndWhooshGeneralData.infantPassenger,
                "",
                2,
                true
        );

        boolean isInsuranceCorrect = true;

        if (trainAndWhooshGeneralData.insuranceGeneralData != null) {
            // 1. Calculate Expected Total
            double expectedTotalInsurance = CoreFunctions.convertPriceSafely(trainAndWhooshGeneralData.insuranceGeneralData.insurancePrice) * trainAndWhooshGeneralData.adultPassenger;
            // 2. Fetch Actual Depart Insurance from UI
            double actualDepartInsurance = getActualPriceFromDetail(
                    "insurance",
                    trainAndWhooshGeneralData.adultPassenger,
                    trainAndWhooshGeneralData.insuranceGeneralData.insuranceTitle,
                    1
            );
            // 3. Fetch Actual Return Insurance from UI if Round Trip
            double actualReturnInsurance = 0;
            if (isRoundTrip) {
                actualReturnInsurance = getActualPriceFromDetail(
                        "insurance",
                        trainAndWhooshGeneralData.adultPassenger,
                        trainAndWhooshGeneralData.insuranceGeneralData.insuranceTitle,
                        2
                );
            }
            // 4. Sum them up and verify
            double actualTotalInsurance = actualDepartInsurance + actualReturnInsurance;
            isInsuranceCorrect = Math.abs(expectedTotalInsurance - actualTotalInsurance) <= 0.001;
            logger.log("Verifying total insurance sum. Expected: " + expectedTotalInsurance + ", Actual: " + actualTotalInsurance);
            CoreFunctions.assertPriceEquals("Total insurance sum should match depart + return insurance breakdown", actualTotalInsurance, expectedTotalInsurance);
        }
        return checkAdultPriceDepart
                && checkAdultPriceReturn
                && checkInfantPriceDepart
                && checkInfantPriceReturn
                && isInsuranceCorrect;
    }

    /**
     * Checks if the actual price displayed matches the expected price for a given type and label.
     *
     * @param type the type of price (adult, infant, insurance)
     * @param expectedPrice the expected price per passenger
     * @param totalPax the total number of passengers
     * @param label the label for insurance (if applicable)
     * @param index the index to differentiate between departure and return trip
     * @param isRoundTrip whether the trip is a round trip or one-way
     * @return true if the actual price matches the expected price, false otherwise
     */
    private boolean checkPriceOnPriceDetail(String type, String expectedPrice, int totalPax, String label, int index, boolean isRoundTrip) {
        logger.log("Checking " + type + " price on price detail. Expected per person price: " + expectedPrice +
                ", Total pax: " + totalPax + ", Label: " + label);
        String typeSupport = index == 1 ? "departure" : "return";
        Locator priceLocator = getPriceLocator(type, label, totalPax, index);
        if (index > 1 && !driverTools.isElementDisplayed(priceLocator)) {
            driverTools.swipeUp();
            List<WebElement> expandButtons = driverTools.elements(SUBTOTAL_EXPAND_BUTTON).orElseThrow();
            logger.log("Expand buttons size: " + expandButtons.size());
            // index is 1-based for XPath ([1]=departure, [2]=return); list indices are 0-based
            int expandIndex = index - 1;
            logger.log("Clicking expand button at list index: " + expandIndex + " (trip index " + index + ")");
            driverTools.clickElementByCoordinate(expandButtons.get(expandIndex));
            driverTools.swipeUp();
        }
        WebElement priceElement = driverTools.waitForElementToAppear(priceLocator).orElseThrow();
        String actualPrice = driverTools.getText(priceElement);
        logger.log("Verifying " + type + " price on price detail. Expected per person price: " + expectedPrice +
                ", Actual total price: " + actualPrice);
        double expectedPriceDouble = CoreFunctions.convertPriceSafely(expectedPrice) * totalPax;
        double actualPriceDouble = CoreFunctions.convertPriceSafely(actualPrice);
        boolean isPriceMatch = Double.compare(expectedPriceDouble, actualPriceDouble) == 0;
        logger.log(type + " price match: " + isPriceMatch);
        Assertion.assertThat(type +" { " + typeSupport + " } price should match", actualPriceDouble, is(expectedPriceDouble));
        return isPriceMatch;
    }

    /**
     * Returns the appropriate Locator for the price detail element based on type, label, and passenger count.
     *
     * @param type the type of price (adult, infant, insurance)
     * @param label the label for insurance (if applicable)
     * @param totalPax the total number of passengers
     * @return the Locator for the price detail element
     */
    private Locator getPriceLocator(String type, String label, int totalPax,int index) {
        return switch (type.toLowerCase()) {
            case "adult" ->
                    new Locator("ADULT_PRICE_DETAIL", AppiumBy.xpath(
                            "(//*[contains(@label,'Adult (x"+totalPax+")') or contains(@label,'Dewasa (x"+totalPax+")') ]//following-sibling::*[@name='rightText'])["+index+"]"));
            case "infant" ->
                    new Locator("INFANT_PRICE_DETAIL", AppiumBy.xpath(
                            "(//*[contains(@label,'Infant (x"+totalPax+")') or contains(@label,'Bayi (x"+totalPax+")') ]//following-sibling::*[@name='rightText'])["+index+"]"));
            case "insurance" ->
                    new Locator("INSURANCE_PRICE_DETAIL", AppiumBy.xpath(
                            "(//*[contains(@label,'"+label+" (x"+totalPax+")')]//following-sibling::*[contains(@label,'IDR')])["+index+"]"));
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }
}
