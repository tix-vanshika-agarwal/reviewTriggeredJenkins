package com.tiket.page.ferry.FerryBookingPage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.core.Every;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public abstract class FerryBookingPage <T extends WebDriver> extends BasePage<T>{
    private static final Locator FULLNAME_INPUT = new Locator("FULLNAME_INPUT", AppiumBy.xpath("//*[@name='fullName']"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", AppiumBy.xpath("//*[@name='emailAddress']"));
    private static final Locator PHONE_NUMBER = new Locator("PHONE_NUMBER", AppiumBy.xpath("//input[@id='mobile-number' or @id='nomor-ponsel']"));
    private static final Locator CONTACT_CARD = new Locator("CONTACT_CARD", AppiumBy.xpath("//div[contains(@class,'ContactDetailSection_container')]//div[contains(@class,'Card_card_container')]"));
    private static final Locator CONTACT_SALUTATION = new Locator("CONTACT_SALUTATION", AppiumBy.xpath("(//*[contains(@class,'ContactForm')]//span[contains(@class,'Radio_checkbox')])[1]"));
    private static final Locator PASSENGER_SALUTATION = new Locator("PASSENGER_SALUTATION", AppiumBy.xpath("(//div[@id='modal-root']//*[contains(@class,'Radio_checkbox')])[1]"));
    private static final Locator ORIGIN_CITY_1 = new Locator("ORIGIN_CITY_1", AppiumBy.xpath("(//div[contains(@class,'DropdownRadio_radio_label')])[1]"));
    private static final Locator SELECT_CITY_INPUT = new Locator("SELECT_CITY_INPUT", AppiumBy.xpath("//*[@id='select-origin-city' or @id='pilih-kota-domisili']"));
    private static final Locator SAVE_PASSENGER_DATA_BTN = new Locator("SAVE_PASSENGR_DATA_BTN", AppiumBy.xpath("//div[contains(@class,'PassengerForm_submit_button')]//button"));
    private static final Locator PASSENGER_VALUE = new Locator("PASSENGER_VALUE", AppiumBy.xpath("(//div[contains(@class, 'PassengerDetailSection_card_inner')])[1]"));
    private static final Locator SAVE_CONTACT_DATA_BTN = new Locator("SAVE_CONTACT_DATA_BTN", AppiumBy.xpath("//button[contains(@class,'ContactForm_button')]"));
    private static final Locator LICENSE_PLATE_NUMBER_INPUT = new Locator("LICENSE_PLATE_NUMBER_INPUT", AppiumBy.xpath("//input[@name='licensePlate']"));
    private static final Locator TOTAL_PRICE_BOOKING_PAGE = new Locator("TOTAL_PRICE_BOOKING_PAGE", AppiumBy.xpath("//div[contains(@class,'TotalPrice_price')]"));
    private static final Locator PRICE_DETAIL_COMPONENTS = new Locator("PRICE_DETAIL_COMPONENTS", AppiumBy.xpath("//div[contains(@class,'price_item')]//span[contains(@class,'PriceDetail_price')]"));
    private static final Locator PRICE_DETAIL_TOTAL_PRICE_VALUE = new Locator("PRICE_DETAIL_TOTAL_PRICE_VALUE", AppiumBy.xpath("//span[contains(@class,'PriceDetail_price')]//span"));
    private static final Locator CHECK_TERM_AND_CONDITION = new Locator("CHECK_TERM_AND_CONDITION", AppiumBy.xpath("//span[contains(@class,'Check_custom_checkbox')]"));
    private static final Locator RANDOM_CLICK = new Locator("RANDOM_CLICK", AppiumBy.xpath("//h3[contains(@class, 'ContactDetailSection_contact_section_title')]"));
    private static final Locator SUBMIT_BOOKING_BTN = new Locator("SUBMIT_BOOKING_BTN", AppiumBy.xpath("//button[contains(@class,'SubmissionSection_button')]"));
    private static final Locator SUBMIT_BOOKING_BTN_NOT_DISABLED = new Locator("SUBMIT_BOOKING_BTN_NOT_DISABLED", AppiumBy.xpath("//button[contains(@class,'SubmissionSection_button') and not(@disabled)]"));
    private static final Locator CLOSE_BTM_SHEET = new Locator("CLOSE_BTM_SHEET", AppiumBy.xpath("//button[contains(@class,'BottomSheet_close_button')]"));
    private static final Locator CONFIRM_PAYMENT = new Locator("CONFIRM_PAYMENT", AppiumBy.xpath("//*[contains(@class,'Dialog')]//button[contains(@class,'primary')]"));
    private static final Locator TRIP_SUMMARY = new Locator("TRIP_SUMMARY", AppiumBy.xpath("//section[contains(@class,'TripSummarySection_container')]"));
    private static final Locator TNC_PAGE_BUTTON = new Locator("TNC_PAGE_BUTTON", AppiumBy.xpath("//*[contains(@class,'SubmissionSection_tnc_label_')]"));
    private static final Locator TNC_PAGE = new Locator("TNC_PAGE", AppiumBy.xpath("//h2[contains(@class, 'BottomSheet_body_title')]"));
    private static final Locator VACCINE_POP_UP = new Locator("VACCINE_POP_UP", AppiumBy.xpath("(//*[contains(text(),'vaccination') or contains(text(),'vaksinasi')])[1]"));
    private static final Locator VACCINE_REASON = new Locator("VACCINE_REASON", AppiumBy.xpath("//label[contains(@class, 'Radio_radio_position_right')]/span[contains(@class, 'Radio_checkbox')]/span[contains(@class, 'Radio_icon__')]"));
    private static final Locator COVID_TEST_POPUP = new Locator("COVID_TEST_POPUP", AppiumBy.xpath("(//*[contains(text(),'COVID-19')])[1]"));
    private static final Locator VACCINATION_VERIFIED_MARK = new Locator("VACCINATION_VERIFIED_MARK", AppiumBy.xpath("//div[contains(@class, 'PassengerForm_covid_verification_status__') and contains(@class, 'PassengerForm_verified__')]"));
    private static final Locator VACCINATION_VERIFIED_PASSENGER = new Locator("VACCINATION_VERIFIED_PASSENGER", AppiumBy.xpath("(//div[contains(@class,'PassengerDetailSection_card')]//p[contains(@class,'Text_text__')])[1]"));
    private static final Locator SAME_ID_FAILED_STRING = new Locator("SAME_ID_FAILED_STRING", AppiumBy.xpath("//*[contains(@class,'styles_helper_text_container')]//*[contains(text(),'Verification failed due to similar ID Card number') or contains(text(),'Verifikasi gagal karena nomor NIK sama') or contains(text(),'Verifikasi gagal karena ada nomor NIK yang sama.')]"));
    private static final Locator FERRY_ROUTE = new Locator("FERRY_ROUTE", AppiumBy.xpath("//*[contains(@class,'TripSummarySection_route_info__')]"));
    private static final Locator CHECK_IN_DATE = new Locator("CHECK_IN_DATE", AppiumBy.xpath("//*[contains(@class,'TripSummarySection_checkin_info_')]//*[contains(@class,'Text_text__')][last()]"));
    private static final Locator PASSENGER_NAMES = new Locator("PASSENGER_NAMES", AppiumBy.xpath("//*[contains(@class,'PassengerDetailSection_card_inner')]//*[contains(@class,'Text_text__')]"));
    private static final Locator PASSENGER_DOB_INPUT = new Locator("PASSENGER_DOB_INPUT", AppiumBy.xpath("//*[contains(@id,'date') or contains(@id,'tanggal-lahir')]"));
    private static final Locator DOB_SAVE_BTN = new Locator("DOB_SAVE_BTN", AppiumBy.xpath("//button[contains(@class,'DatePicker_datepicker_button')]"));

    public FerryBookingPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FerryBookingPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case IOS -> {
                return new FerryBookingPageIOS<>(session);
            }
            case ANDROID -> {
                return new FerryBookingPageAndroid<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private void clearAndFillField(Locator fieldLocator, String value, String logLabel) {
        driverTools.waitForElementToAppear(fieldLocator);
        WebElement fieldElement = driverTools.element(fieldLocator).orElseThrow();
        if(!driverTools.getText(fieldElement).isEmpty()){
            logger.log("Clearing field: " + logLabel);
            driverTools.clearWithJsExecutor(fieldLocator);
            String valueOnForm = driverTools.getValueAttribute(fieldLocator, "value");
            logger.log(logLabel + " ->" + valueOnForm);
            if(!valueOnForm.isEmpty()){
                driverTools.clearWithJsExecutor(fieldLocator);
            }
        }
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(fieldLocator, value);
        driverTools.hideKeyboard();
        String valueOnFormFinal = driverTools.getValueAttribute(fieldLocator, "value");
        logger.log(logLabel + "Final ->" + valueOnFormFinal);
    }

    public void fillGuestContactDetail(String username,String fullName,String phoneNumber){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("ferry/booking", 2);
        if(driverTools.waitForElementToAppear(CONTACT_CARD,4).isPresent()){
            driverTools.click(CONTACT_CARD);
        }
        driverTools.click(CONTACT_SALUTATION);
        clearAndFillField(FULLNAME_INPUT, fullName, "nameOnBookingForm");
        clearAndFillField(PHONE_NUMBER, phoneNumber, "phoneNumberOnBookingForm");
        clearAndFillField(EMAIL_INPUT, username, "emailOnBookingForm");
        driverTools.waitForElementToPresence(SAVE_CONTACT_DATA_BTN,3).ifPresent(driverTools::click);
        driverTools.switchToNative();
    }

    private Locator getAdultPassengerContainerLocator(int index){
        return new Locator("PASSENGER_"+index, AppiumBy.xpath("//div[contains(@class,'PassengerDetailSection_card')]//p[contains(text(),'"+index+"') and (contains(text(),'Adult') or contains(text(),'Dewasa'))]"));
    }

    private Locator getChildPassengerContainerLocator(int index){
        return new Locator("PASSENGER_"+index, AppiumBy.xpath("(//div[contains(@class,'PassengerDetailSection_card')]//p[contains(text(),'Child') or contains(text(),'Anak')])["+ index +"]"));
    }

    private Locator getElderlyPassengerContainerLocator(int index){
        return new Locator("PASSENGER_"+index, AppiumBy.xpath("(//div[contains(@class,'PassengerDetailSection_card')]//p[contains(text(),'Senior') or contains(text(),'Lansia') or contains(text(),'Elderly')])["+ index +"]"));
    }
    private Locator getPassengerFullNameInput(int index){
        return new Locator("PASSENGER_FULL_NAME_"+index, AppiumBy.xpath("//input[@name='fullName"+index+"']"));
    }

    private Locator getAgeLocator(int index){
        return new Locator( "PASSENGER_AGE_"+index, AppiumBy.xpath("//input[@name='age"+index+"']"));
    }
    private Locator getIdNumberInputLocator(int index){
        return new Locator("IDENTITY_NUMBER", AppiumBy.xpath("//input[@name='identityNumber"+index+"']"));
    }

    private Locator getDriverLicenseLocator(int index){
        return new Locator("DRIVER_LICESNSE", AppiumBy.xpath("//input[@name='driverLicense"+index+"']"));
    }

    private void fillAdultGuestData(int numberOfAdult){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("ferry/booking");
        for(int i = 0 ;i<numberOfAdult ;i++){
            Locator passengerContainerLocator = getAdultPassengerContainerLocator(i+1);
            if(driverTools.element(passengerContainerLocator).isEmpty()) {
                break;
            }
            driverTools.swipeUntilElementVisible(passengerContainerLocator, Swipe.UP, 2);
            driverTools.click(passengerContainerLocator);
            driverTools.click(PASSENGER_SALUTATION);
            Locator passengerFullnameInputLocator = getPassengerFullNameInput(i);
            driverTools.sendKeys(passengerFullnameInputLocator,CoreFunctions.randomName());
            driverTools.hideKeyboard();
            // Send age directly to the input field
            Locator passengerAgeInputLocator = getAgeLocator(i);
            driverTools.sendKeys(passengerAgeInputLocator, String.valueOf(CoreFunctions.generateRandomInt(18, 55)));
            driverTools.hideKeyboard();
            driverTools.click(SELECT_CITY_INPUT);
            driverTools.click(ORIGIN_CITY_1);
            Locator idNumberLocator = getIdNumberInputLocator(i);
            driverTools.swipeToElement(idNumberLocator, Swipe.UP, 3);
            driverTools.sendKeys(idNumberLocator, "327" + CoreFunctions.randomInt(13));
            driverTools.hideKeyboard();
            if(i==0){
                Locator driverLicenseLocator = getDriverLicenseLocator(i);
                if(driverTools.isElementDisplayed(driverLicenseLocator)){
                    driverTools.sendKeys(driverLicenseLocator, String.valueOf(CoreFunctions.randomInt(4)));
                    driverTools.hideKeyboard();
                }
            }
            driverTools.click(SAVE_PASSENGER_DATA_BTN);
        }
        driverTools.switchToNative();
    }

    public void fillGuestData(int numberOfAdult,int numberOfChild,int numberOfInfant,int numberOfEldery){
        fillAdultGuestData(numberOfAdult);
        fillChildGuestData(numberOfChild);
        fillInfantGuestData(numberOfInfant);
        fillElderlyGuestData(numberOfEldery);
    }

    private void fillChildGuestData(int numberOfChild){
        driverTools.switchToWebViewUrl("ferry/booking");
        for(int i = 0 ;i<numberOfChild ;i++) {
            Locator passengerContainerLocator = getChildPassengerContainerLocator(i+1);
            if(driverTools.element(passengerContainerLocator).isEmpty()) {
                break;
            }
            driverTools.swipeToElement(passengerContainerLocator, Swipe.UP, 2);
            driverTools.click(passengerContainerLocator);
            driverTools.click(PASSENGER_SALUTATION);
            Locator passengerFullnameInputLocator = getPassengerFullNameInput(i+1);
            driverTools.sendKeys(passengerFullnameInputLocator, CoreFunctions.randomName());
            driverTools.hideKeyboard();
            if(driverTools.isElementDisplayed(PASSENGER_DOB_INPUT)) {
                driverTools.click(PASSENGER_DOB_INPUT);
                driverTools.click(DOB_SAVE_BTN);
                driverTools.hideKeyboard();
            }
            driverTools.hideKeyboard();
            driverTools.click(SELECT_CITY_INPUT);
            driverTools.click(ORIGIN_CITY_1);
            driverTools.click(SAVE_PASSENGER_DATA_BTN);
        }
        driverTools.switchToNative();
    }

    private void fillInfantGuestData(int numberOfInfant){
        driverTools.switchToWebViewUrl("ferry/booking");
        for(int i = 0 ;i<numberOfInfant ;i++) {
            Locator passengerContainerLocator = getInfantPassengerContainerLocator(i+1);
            if(driverTools.element(passengerContainerLocator).isEmpty()) {
                break;
            }
            driverTools.swipeToElement(passengerContainerLocator, Swipe.UP, 2);
            driverTools.click(passengerContainerLocator);
            driverTools.click(PASSENGER_SALUTATION);
            Locator passengerFullnameInputLocator = getPassengerFullNameInput(i+1);
            driverTools.sendKeys(passengerFullnameInputLocator, CoreFunctions.randomName());
            driverTools.hideKeyboard();
            if(driverTools.isElementDisplayed(PASSENGER_DOB_INPUT)) {
                driverTools.click(PASSENGER_DOB_INPUT);
                driverTools.click(DOB_SAVE_BTN);
                driverTools.hideKeyboard();
            }
            driverTools.hideKeyboard();
            driverTools.click(SELECT_CITY_INPUT);
            driverTools.click(ORIGIN_CITY_1);
            driverTools.click(SAVE_PASSENGER_DATA_BTN);
        }
        driverTools.switchToNative();
    }

    private Locator getInfantPassengerContainerLocator(int index) {
        return new Locator("PASSENGER_CONTAINER_" + index, new By.ByXPath("(//div[contains(@class, 'PassengerDetailSection_card')]//p[contains(text(), 'Infant') or contains(text(), 'Bayi')])[" + index + "]"));
    }

    private void fillElderlyGuestData(int numberOfEldery){
        driverTools.switchToWebViewUrl("ferry/booking");
        for(int i = 0 ;i<numberOfEldery ;i++) {
            Locator passengerContainerLocator = getElderlyPassengerContainerLocator(i+1);
            if(driverTools.element(passengerContainerLocator).isEmpty()) {
                break;
            }
            driverTools.swipeToElement(passengerContainerLocator, Swipe.UP, 2);
            try {
                driverTools.clickWithJavascriptExecutor(passengerContainerLocator);
            }catch (Exception e) {
                logger.log("Failed to click passenger container using JavascriptExecutor, trying normal click. Error: " + e.getMessage());
                driverTools.click(passengerContainerLocator);
            }
            driverTools.click(PASSENGER_SALUTATION);
            Locator passengerFullnameInputLocator = getPassengerFullNameInput(i+1);
            driverTools.sendKeys(passengerFullnameInputLocator, CoreFunctions.randomName());
            driverTools.hideKeyboard();
            // Send age directly to the input field
            Locator passengerAgeInputLocator = getAgeLocator(i+1);
            driverTools.sendKeys(passengerAgeInputLocator, String.valueOf(CoreFunctions.generateRandomInt(18, 55)));

            driverTools.hideKeyboard();
            driverTools.click(SELECT_CITY_INPUT);
            driverTools.click(ORIGIN_CITY_1);
            driverTools.click(SAVE_PASSENGER_DATA_BTN);
        }
        driverTools.switchToNative();
    }

    public void fillLicensePlateNumber(){
        driverTools.switchToWebViewUrl("ferry/booking");
        boolean isPlateNumberRequired = driverTools.swipeToElement(LICENSE_PLATE_NUMBER_INPUT, Swipe.UP, 5).isPresent();
        if(isPlateNumberRequired){
            WebElement element = driverTools.element(LICENSE_PLATE_NUMBER_INPUT).orElseThrow();
            driverTools.clearInput(element);
            driverTools.sendKeys(LICENSE_PLATE_NUMBER_INPUT, "DR"+CoreFunctions.randomInt(4)+"JY");
            driverTools.hideKeyboard();
        }
        driverTools.switchToNative();
    }

    public boolean verifTotalPriceIsInIdr(){
        driverTools.switchToWebViewUrl("ferry/booking");
        driverTools.swipeToElement(TOTAL_PRICE_BOOKING_PAGE, Swipe.UP,5);
        String totalPrice = driverTools.getText(TOTAL_PRICE_BOOKING_PAGE).orElseThrow();
        Assertion.assertThat("Verify total price is in IDR", totalPrice, containsString("IDR"));
        driverTools.switchToNative();
        return true;
    }

    public void openPriceDetail(){
        driverTools.switchToWebViewUrl("ferry/booking");
        driverTools.swipeUntilElementVisible(SUBMIT_BOOKING_BTN, Swipe.UP, 4);
        driverTools.click(TOTAL_PRICE_BOOKING_PAGE);
        driverTools.switchToNative();
    }

    public boolean verifyPriceDetailIsInIDR(){
        openPriceDetail();
        driverTools.switchToWebViewUrl("ferry/booking");
        List<String> priceComponentsInPriceDetail = driverTools.getTexts(PRICE_DETAIL_COMPONENTS);
        logger.log("number of price component present : " + priceComponentsInPriceDetail.size());
        String totalPrice = driverTools.getText(PRICE_DETAIL_TOTAL_PRICE_VALUE).orElseThrow();
        // assertion price components should be in IDR
        Assertion.assertThat("Verify price components are in idr", priceComponentsInPriceDetail,
                Every.everyItem(anyOf(containsString("IDR"),equalTo("Termasuk"),equalTo("Included"))));
        Assertion.assertThat("Verify total price is in IDR", totalPrice, containsString("IDR"));

        // assertion for total price same as manual sum calculation
        List<Double> priceComponentsNumbersValue = priceComponentsInPriceDetail.stream()
                .filter(n -> n.matches(".*[0-9]+.*"))
                .map(n -> CoreFunctions.convertPriceSafely(n)).toList();// remove currency word from the value. Eg ->
        // IDR 5000 into 5000 (with data type double)
        // sum price components
        double sumOfPriceDetail = CoreFunctions.sumUpDouble(priceComponentsNumbersValue);
        // compare sumOfPriceDetail with actual value in ui
        Assertion.assertThat("Verify total sum is the same", CoreFunctions.convertPriceSafely(totalPrice),
                equalTo(sumOfPriceDetail));
        driverTools.click(CLOSE_BTM_SHEET);
        driverTools.switchToNative();
        return true;
    }

    public void clickTncCheckBox() {
        driverTools.switchToWebViewUrl("ferry/booking");
        driverTools.swipeUntilElementVisible(CHECK_TERM_AND_CONDITION, Swipe.UP, 5);
        driverTools.click(CHECK_TERM_AND_CONDITION);
        Assertion.assertThat("booking btn is enabled after tnc checked",driverTools.isElementDisplayed(SUBMIT_BOOKING_BTN_NOT_DISABLED), is(true));
        driverTools.switchToNative();
    }

    public void submitBooking(){
        driverTools.switchToWebViewUrl("ferry/booking");
        driverTools.swipeToElement(SUBMIT_BOOKING_BTN, Swipe.UP, 3);
        driverTools.clickWithJavascriptExecutor(SUBMIT_BOOKING_BTN);
        driverTools.click(CONFIRM_PAYMENT);
        driverTools.switchToNative();
    }

    public Boolean VerifyTermAndConditionPage(){
        driverTools.switchToWebViewUrl("ferry/booking");
        driverTools.click(TNC_PAGE_BUTTON);
        driverTools.waitForElementToAppear(TNC_PAGE, 10);
        Boolean isElementsDisplayed = driverTools.isElementDisplayed(TNC_PAGE) && driverTools.isElementDisplayed(CLOSE_BTM_SHEET);
        driverTools.switchToNative();
        return isElementsDisplayed;

    }

    public boolean verifyVaccinatedStatus(String vaccineCheck) {
        boolean userVaccinatedBadge = false;
        driverTools.switchToWebViewUrl("ferry/booking");

        if (vaccineCheck.equals("true")) {
            driverTools.click(VACCINATION_VERIFIED_PASSENGER);
            if (driverTools.isElementDisplayed(VACCINATION_VERIFIED_MARK)) {
                userVaccinatedBadge = true;
                Assertion.assertThat("User Vaccinated green badge is visible inside fill guest detail section", userVaccinatedBadge, is(true));
            }
        } else if (vaccineCheck.equals("false")) {
            logger.softWarning("Vaccine verification failed from ASDP");
        }
        driverTools.waitForElementToAppear(CLOSE_BTM_SHEET).ifPresent(driverTools::click);
        driverTools.switchToNative();
        return userVaccinatedBadge;
    }

    public void fillGuestsAndVaccineDetails(String vaccineCheck, String vaccineVerifiedID, int numberOfAdults, int elderlyGuestCount) {
        driverTools.switchToWebViewUrl("ferry/booking");

        // Filling details for adult guests
        for (int i = 0; i < numberOfAdults; i++) {
            Locator passengerContainerLocator = getAdultPassengerContainerLocator(i+1);
            fillPassengerDetails(passengerContainerLocator, vaccineCheck, vaccineVerifiedID, i, false);
        }

        // Filling details for elderly guests
        for (int i = 0; i < elderlyGuestCount; i++) {
            Locator passengerContainerLocator = getElderlyPassengerContainerLocator(i +1);
            fillPassengerDetails(passengerContainerLocator, vaccineCheck, vaccineVerifiedID, i+1, true);
        }

        driverTools.switchToNative();
    }

    private void fillPassengerDetails(Locator passengerContainerLocator, String vaccineCheck, String vaccineVerifiedID, int passengerIndex, boolean isElderly) {
        driverTools.clickElementByCoordinate(RANDOM_CLICK);
        if (driverTools.isElementDisplayed(passengerContainerLocator)) {
            try {
                driverTools.clickWithJavascriptExecutor(passengerContainerLocator);
            }catch (Exception e) {
                logger.log("Failed to click passenger container using JavascriptExecutor, trying normal click. Error: " + e.getMessage());
                driverTools.click(passengerContainerLocator);
            }
        }
        else return;
        driverTools.click(PASSENGER_SALUTATION);
        Locator passengerFullnameInputLocator = getPassengerFullNameInput(passengerIndex);
        driverTools.sendKeys(passengerFullnameInputLocator, CoreFunctions.randomName());
        driverTools.hideKeyboard();
        Locator passengerAgeInputLocator = getAgeLocator(passengerIndex);
        int age = isElderly ? CoreFunctions.generateRandomInt(60, 100) : CoreFunctions.generateRandomInt(18, 55);
        driverTools.sendKeys(passengerAgeInputLocator, String.valueOf(age));
        driverTools.hideKeyboard();
        Locator idNumberLocator = getIdNumberInputLocator(passengerIndex);
        if (passengerIndex == 0) {
            // Use the VaccineVerifiedID for the first passenger
            driverTools.waitForElementToPresence(idNumberLocator);
            driverTools.sendKeys(idNumberLocator, vaccineVerifiedID);
            driverTools.hideKeyboard();
        } else {
            // Generate a random ID number for subsequent passengers
            long randomIDNumber =  CoreFunctions.generateRandomInt(16);
            driverTools.swipeToElement(idNumberLocator, Swipe.UP, 3);
            driverTools.sendKeys(idNumberLocator, String.valueOf(randomIDNumber));
            driverTools.hideKeyboard();
        }
        driverTools.waitForElementToAppear(SELECT_CITY_INPUT, 10).ifPresent(driverTools::click);
        driverTools.click(ORIGIN_CITY_1);

        driverTools.click(SAVE_PASSENGER_DATA_BTN);
        fillVaccineDetails(vaccineCheck);
    }

    public void fillVaccineDetails(String vaccineCheck){
        if (vaccineCheck.equals("true")) {
            if (driverTools.isElementDisplayed(VACCINE_POP_UP) || driverTools.isElementDisplayed(COVID_TEST_POPUP)) {
                if (driverTools.isElementDisplayed(VACCINE_POP_UP)) {
                    driverTools.waitForElementToAppear(VACCINE_REASON);
                    List<WebElement> vaccineReason = driverTools.elements(VACCINE_REASON).get();
                    logger.log("Number of vaccine reasons found: " + vaccineReason.size());
                    driverTools.click(vaccineReason.get(CoreFunctions.generateRandomInt(0, vaccineReason.size()-1)));
                }

                if (driverTools.waitForElementToAppear(COVID_TEST_POPUP).isPresent()) {
                    driverTools.waitForElementToAppear(VACCINE_REASON,15);
                    List<WebElement> testReason = driverTools.elements(VACCINE_REASON).get();
                    logger.log("Number of COVID test reasons found: " + testReason.size());
                    WebElement covidReason = testReason.get(CoreFunctions.generateRandomInt(0, testReason.size()-1));
                    driverTools.waitForElementToAppear(covidReason);
                    driverTools.clickWithJavascriptExecutor(covidReason);
                }
            }
            driverTools.waitForElementToPresence(SAVE_PASSENGER_DATA_BTN, 20).ifPresent(driverTools::click);
        } else if (vaccineCheck.equals("false")) {
            logger.log("vaccine verification false from ASDP");
        }
    }

    public boolean VerifyGuestDetailSameId(int numberOfAdult, String vaccineCheck) {
        driverTools.switchToWebViewUrl("ferry/booking");
        boolean sameIDWarning = false;

        String id = "327" + CoreFunctions.randomInt(13);

        for (int i = 0; i < numberOfAdult; i++) {
            Locator passengerContainerLocator = getAdultPassengerContainerLocator(i + 1);
            driverTools.swipeToElement(passengerContainerLocator, Swipe.UP, 15);
            if(driverTools.isElementDisplayed(passengerContainerLocator)){
                driverTools.click(passengerContainerLocator);
            }
            else continue;
            driverTools.clickWithJavascriptExecutor(PASSENGER_SALUTATION);
            Locator passengerFullnameInputLocator = getPassengerFullNameInput(i);
            driverTools.sendKeys(passengerFullnameInputLocator, CoreFunctions.randomName());
            driverTools.hideKeyboard();
            Locator passengerAgeInputLocator = getAgeLocator(i);
            driverTools.sendKeys(passengerAgeInputLocator, String.valueOf(CoreFunctions.generateRandomInt(18, 55)));
            driverTools.hideKeyboard();
            driverTools.click(SELECT_CITY_INPUT);
            driverTools.click(ORIGIN_CITY_1);
            Locator idNumberLocator = getIdNumberInputLocator(i);
            driverTools.waitForElementToAppear(idNumberLocator);
            driverTools.swipeToElement(idNumberLocator, Swipe.UP, 3);
            driverTools.sendKeys(idNumberLocator, id);
            driverTools.hideKeyboard();

            if (driverTools.isElementDisplayed(SAME_ID_FAILED_STRING)) {
                sameIDWarning = true;
                WebElement element = driver.findElement(idNumberLocator.by());
                while (!element.getAttribute("value").isEmpty()) {
                    element.sendKeys(Keys.BACK_SPACE);
                    driverTools.hideKeyboard();
                }
                driverTools.sendKeys(idNumberLocator, "327" + CoreFunctions.randomInt(13));
                driverTools.hideKeyboard();
            }

            driverTools.waitForElementToAppear(SAVE_PASSENGER_DATA_BTN, 10).ifPresent(driverTools::click);
            fillVaccineDetails(vaccineCheck);


        }
        driverTools.switchToNative();
        return sameIDWarning;
    }

    private boolean isPassengerValueAlreadyFilled(){
        boolean flag = true;
        String passengerValue = driverTools.getText(PASSENGER_VALUE).orElse("");
        if(passengerValue.contains("Passenger")){
            flag = false;
            logger.log("Passenger value is not filled yet");
        } else {
            logger.log("Passenger value is already filled: " + passengerValue);
        }
        return flag;
    }

    public Optional<String> getFerryRouteBookingPage(){
        driverTools.switchToWebViewUrl("ferry");
        Optional<String> routeText = driverTools.getText(FERRY_ROUTE);
        logger.log( "Ferry route text: " + routeText.orElse("No route text found"));
        driverTools.switchToNative();
        return routeText;
    }

    public Optional<String> getFerryCheckInDateBookingPage(){
        driverTools.switchToWebViewUrl("ferry");
        Optional<String> dateTime = driverTools.getText(CHECK_IN_DATE); // Assuming CHECK_IN_DATE is your locator

        String date = dateTime
                .map(s -> {
                    String[] parts = s.split("\\s");
                    StringBuilder dateBuilder = new StringBuilder();
                    for (int i = 0; i < parts.length; i++) {
                        if (parts[i].matches("\\d{2}[,:.]\\d{2}")) {
                            break;
                        }
                        dateBuilder.append(parts[i]).append(" ");
                    }
                    return dateBuilder.toString().trim();
                }) // Extract the first part until the time format is encountered
                .orElse("");

        logger.log("Ferry check-in date: " + date);
        driverTools.switchToNative();
        return Optional.ofNullable(date);
    }

    public List<String> getFerryPassengerNamesBookingPage() {
        driverTools.switchToWebViewUrl("ferry");
        List<WebElement> passengerNameElements = driverTools.elements(PASSENGER_NAMES).orElseThrow();
        List<String> passengerNames = new ArrayList<>();
        for (WebElement element : passengerNameElements) {
            String passengerName = driverTools.getText(element).trim();
            logger.log("Ferry passenger name: " + passengerName);
            passengerNames.add(passengerName);
        }
        driverTools.switchToNative();
        return passengerNames;
    }

    public abstract void setLocationService();

}