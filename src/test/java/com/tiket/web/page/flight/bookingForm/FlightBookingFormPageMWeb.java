package com.tiket.web.page.flight.bookingForm;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.flight.FlightPaxDetail;
import com.tiket.testbase.Assertion;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import lombok.SneakyThrows;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.WheelInput;
import java.util.List;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.core.Is.is;

public class FlightBookingFormPageMWeb <T extends WebDriver> extends FlightBookingFormPageWeb<T> {

    public FlightBookingFormPageMWeb(Session<T> session) { super(session); }

    private static final Locator NEXT_BTN_MWEB = new Locator("NEXT_BTN_MWEB", By.xpath("//div[contains(@class,'BookingFooter_footer__')]//button[contains(@class,'Button')]"));
    private static final Locator CONTACT_DETAILS_CONTAINER = new Locator("CONTACT_DETAILS_CONTAINER", By.xpath("//button[contains(@class, 'GroupFieldBody_button_container')]"));
    private static final Locator SAVE_CONTACT_DETAILS = new Locator("SAVE_CONTACT_DETAILS", By.xpath("//button[contains(@class, 'BookingContactAndPassengers_button') and (contains(text(),'Simpan') or contains(text(),'Save'))]"));
    protected static final Locator CONTINUE_TO_PAYMENT_PAGE_POPUP = new Locator("CONTINUE_TO_PAYMENT_PAGE_POPUP", new By.ByXPath("//button[span[normalize-space(text())='Continue to Payment' or (text())='Lanjut Bayar']]"));
    protected static final Locator CONTINUE_TO_PAYMENT_PAGE_DIALOG_POPUP = new Locator("CONTINUE_TO_PAYMENT_PAGE_DIALOG_POPUP", new By.ByXPath("//div[contains(@class, 'Figure_footer')]"));
    protected static final Locator CONTINUE_TO_PAYMENT_BUTTON_ON_DIALOG_POPUP = new Locator("CONTINUE_TO_PAYMENT_BUTTON_ON_DIALOG_POPUP", new By.ByXPath("//div[contains(@class, 'Figure_footer')]//button[2]//span[text()='Lanjut Bayar' or text()='Continue to Payment']"));
    private static final Locator FLIGHT_DETAILS = new Locator("FLIGHT_DETAILS", new By.ByXPath("//div[contains(@class, 'Card_card_container')]//*[contains(@class, 'FlightSummaryCard_schedule_section')]"));
    private static final Locator BOOKING_FORM = new Locator("BOOKING_FORM", new By.ByXPath("//*[contains(@class, 'BookingFlightSummary_card_container') or contains(@class, 'AddonsCard_card')]"));
    private static final Locator FLIGHT_FACILITY_POPUP = new Locator("FLIGHT_FACILITY_POPUP", new By.ByXPath("//*[contains(@class, 'BottomSheet_bottom_sheet')]"));
    private static final Locator RRPOPUP = new Locator("RRPOPUP", new By.ByXPath("//*[@data-testid='bottom-sheet-body']"));
    private static final Locator CLOSE_SECOND = new Locator("CLOSE_SECOND", new By.ByXPath("(//button[contains(@class,'BottomSheet_close_button')])[2]"));
    private static final Locator CLOSE = new Locator("CLOSE", new By.ByXPath("//button[contains(@class,'BottomSheet_close_button')]"));
    private static final Locator PASSENGER_DATA_MODAL = new Locator("PASSENGER_DATA_MODAL", By.xpath("//*[contains(@class,'BookingPassengerSection_person_card')]"));
    private static final Locator DOB_SUBMIT = new Locator("DOB_SUBMIT", new By.ByXPath("//button[contains(@class, 'DatePicker_datepicker_button')]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//span[contains(text(), 'Simpan') or contains(text(), 'Save')]//parent::button[contains(@class, 'Button_variant_primary')]"));


    @Override
    public void fillOutSalutation(String salutation) {
        driverTools.waitForElementToAppear(CONTACT_DETAILS_CONTAINER).ifPresent(driverTools::click);
        driverTools.clickWithJavascriptExecutor(getSalutationLocator(salutation));
        driverTools.click(SAVE_CONTACT_DETAILS);
        driverTools.waitForElementToDisappear(SAVE_CONTACT_DETAILS);
    }

    private void goToAddOnPageMWEB() {
        driverTools.waitForElementToAppear(NEXT_BTN_MWEB, 3);
        if(driverTools.isElementDisplayed(NEXT_BTN_MWEB)) {
            driverTools.scrollIntoWebView(NEXT_BTN_MWEB);
            driverTools.click(NEXT_BTN_MWEB);
        }
    }

    @Override
    public void goToPaymentPageFromBookingFormPage() {
        goToAddOnPageMWEB();
        WebElement continueToPayment = driverTools.waitForElementToAppear(NEXT_BUTTON).orElseThrow();
        driverTools.scrollIntoWebView(continueToPayment);
        driverTools.clickWithJavascriptExecutor(NEXT_BUTTON);
        driverTools.scrollUntilElementFound(CONTINUE_TO_PAYMENT_PAGE_POPUP);
        driverTools.click(CONTINUE_TO_PAYMENT_PAGE_POPUP);
        driverTools.waitForElementToAppear(CONTINUE_TO_PAYMENT_PAGE_DIALOG_POPUP);
        driverTools.clickWithJavascriptExecutor(CONTINUE_TO_PAYMENT_BUTTON_ON_DIALOG_POPUP);
    }

    @Override
    public void goToPaymentPageFromAddonsPage() {
        driverTools.scrollUntilElementFound(CONTINUE_TO_PAYMENT_PAGE_POPUP);
        driverTools.click(CONTINUE_TO_PAYMENT_PAGE_POPUP);
        driverTools.waitForElementToAppear(CONTINUE_TO_PAYMENT_PAGE_DIALOG_POPUP);
        driverTools.clickWithJavascriptExecutor(CONTINUE_TO_PAYMENT_BUTTON_ON_DIALOG_POPUP);
    }

    @Override
    public String getPriceText() {
        goToAddOnPageMWEB();
        driverTools.waitForElementToAppear(PRICE_TEXT, 3);
        driverTools.scrollElementToCenter(PRICE_TEXT);
        return driverTools.getText(PRICE_TEXT).orElse("");
    };

    @Override
    public boolean isBookingFormVisible() {
        return driverTools.waitForElementToAppear(BOOKING_FORM).isPresent();
    }

    @Override
    public boolean isFlightDetailsVisible() {
        return driverTools.waitForElementToAppear(FLIGHT_DETAILS).isPresent();
    }

    @Override
    public boolean isFlightFacilityVisible(){
        return driverTools.waitForElementToAppear(FLIGHT_FACILITY_POPUP).isPresent();
    }

    @Override
    public void goOnFlightDetail(){
        driverTools.scrollIntoWebView(FLIGHT_DETAILS);
        driverTools.clickWithJavascriptExecutor(FLIGHT_DETAILS);
    }

    @Override
    public void closePopUp(){
        driverTools.waitForElementToAppear(CLOSE);
        driverTools.clickWithJavascriptExecutor(CLOSE);
        driverTools.waitForElementToDisappear(CLOSE);
    }

    public boolean isRRPopUpAppear(){
        return driverTools.waitForElementToAppear(RRPOPUP).isPresent();
    }

    @Override
    public void closeRRPopUp(){
        driverTools.waitForElementToAppear(CLOSE_SECOND);
        driverTools.clickWithJavascriptExecutor(CLOSE_SECOND);
        driverTools.waitForElementToDisappear(RRPOPUP);
    }

    @Override
    public void closeSecondPopup(){
        driverTools.waitForElementToAppear(CLOSE_SECOND);
        driverTools.click(CLOSE_SECOND);
        driverTools.waitForElementToDisappear(FLIGHT_FACILITY_POPUP);
    }

    @Override
    public FlightPaxDetail fillPassengerDetailsLogin(int adultPax, int childPax, int infantPax, String route, String airlineName) {
        driverTools.waitForElementToAppear(PASSENGER_DATA_MODAL, 5);
        List<WebElement> passengerElements = driverTools.elements(PASSENGER_DATA_MODAL).orElseThrow();
        logger.log("Number of passenger elements found: " + passengerElements.size() + "number of passenger expected : " + (adultPax + childPax + infantPax));
        Assertion.assertThat("Number of passenger elements should be equal to total number of passengers", passengerElements.size(), is(adultPax + childPax + infantPax));
        //generate FlightPaxDetail for each passenger
        FlightPaxDetail flightPaxDetail = new FlightPaxDetail(adultPax, childPax, infantPax, route, airlineName, "Indonesia");

        int totalPax = adultPax + childPax + infantPax;
        for (int paxIndex = 1; paxIndex <= totalPax; paxIndex++) {
            chooseDomesticOrInternational(paxIndex, flightPaxDetail, route);
        }
        return flightPaxDetail;
    }

    private  static Locator getFormButton(int paxIndex) {
        return new Locator("FORM_"+paxIndex, By.xpath("(//div[contains(@class,'BookingPassengerSection_person_card')]//button[contains(@class, 'GroupFieldBody_button_container')])["+paxIndex+"]"));
    }

    public void chooseDomesticOrInternational(int indexPax, FlightPaxDetail flightPaxDetail,String flightRoute) {
        try {
            //print name of passenger, dob, type
            logger.log("================ Filling passenger details for passenger " + indexPax + " ========");
            logger.log("  Type: " + flightPaxDetail.data.get(indexPax).paxType);
            logger.log("  Name: " + flightPaxDetail.data.get(indexPax).paxFirstName + " " + flightPaxDetail.data.get(indexPax).paxLastName);
            logger.log("  DOB: " + flightPaxDetail.data.get(indexPax).paxBirthDate);
        }catch (Exception e){
            logger.log("flightPaxDetail is null or empty for passenger " + indexPax);
        }

        driverTools.click(getFormButton(indexPax));

        fillFullName(indexPax,flightPaxDetail);
        flightPaxDetail.data.get(indexPax).paxTitle = fillTitle();
        if (flightRoute.equals("domestic")) {
            fillPassengerDetailDomestic(indexPax,flightPaxDetail);
        } else if (flightRoute.equals("international")) {
            fillPassengerDetailInternational(indexPax,flightPaxDetail);
        }
        logger.log("================= Filling passenger details for passenger " + indexPax + " completed ========");

        driverTools.click(SAVE_BUTTON);
    }

    protected void fillFullName(int indexPax, FlightPaxDetail flightPaxDetail) {
        String firstName = flightPaxDetail.data.get(indexPax).paxFirstName;
        String lastName = flightPaxDetail.data.get(indexPax).paxLastName;

        logger.log("Filling full name: " + firstName + " " + lastName);
        Locator fullNameLocator = getFullNameLocator();
        Locator clearFullNameLocator = getClearFullNameLocator();
        Locator lastNameLocator = getLastNameLocator();
        Locator clearLastNameLocator = getClearLastNameLocator();
        driverTools.waitForElementToAppear(fullNameLocator);
        boolean hasLastNameField = driverTools.isElementDisplayed(lastNameLocator);
        if (hasLastNameField) {
            clearAndFillField(fullNameLocator, firstName, "nameOnBookingForm", clearFullNameLocator);
            clearAndFillField(lastNameLocator, lastName, "lastNameOnBookingForm",clearLastNameLocator);
        } else {
            String fullName = firstName + " " + lastName;
            clearAndFillField(fullNameLocator, fullName, "nameOnBookingForm",clearFullNameLocator);
        }
        fillFullNameAccordingToIdIfPresent(indexPax, flightPaxDetail);
    }

    private String fillTitle(){
        List<WebElement> titleElements = driverTools.elements(titleLocator()).orElseThrow();
        int totalTitles = titleElements.size();
        //select a random title from the list
        int randomIndex = CoreFunctions.generateRandomInt(0, totalTitles - 1);
        WebElement titleElement = titleElements.get(randomIndex);
        String title = titleElement.getAttribute("value");
        logger.log("Selected title: " + title);
        driverTools.clickWithJavascriptExecutor(titleElement);
        return title.toUpperCase();
    }

    private Locator titleLocator() {
        return new Locator("TITLE_LOCATOR", By.xpath("//*[contains(@class,'BookingPassengerForm_radio')]//input"));
    }

    private Locator getFullNameLocator() {
        StringBuilder xpath = new StringBuilder("//*[contains(@class,'BookingPassengerForm_passenger_form')]//input[");
        for (int i = 0; i < FULL_NAME_IDS.length; i++) {
            xpath.append("@id='").append(FULL_NAME_IDS[i]).append("'");
            if (i < FULL_NAME_IDS.length - 1) {
                xpath.append(" or ");
            }
        }
        xpath.append("]");
        return new Locator("FULL_NAME_LOCATOR", By.xpath(xpath.toString()));
    }

    private Locator getClearFullNameLocator() {
        StringBuilder xpath = new StringBuilder("//*[contains(@class,'BookingPassengerForm_passenger_form')]//input[");
        for (int i = 0; i < FULL_NAME_IDS.length; i++) {
            xpath.append("@id='").append(FULL_NAME_IDS[i]).append("'");
            if (i < FULL_NAME_IDS.length - 1) {
                xpath.append(" or ");
            }
        }
        xpath.append("]//following-sibling::*//button[contains(@class,'styles_clear_button')]");
        return new Locator("CLEAR_FULL_NAME_LOCATOR", By.xpath(xpath.toString()));
    }

    private Locator getLastNameLocator() {
        StringBuilder xpath = new StringBuilder("//*[contains(@class,'BookingPassengerForm_passenger_form')]//input[");
        for (int i = 0; i < LAST_NAME_IDS.length; i++) {
            xpath.append("@id='").append(LAST_NAME_IDS[i]).append("'");
            if (i < LAST_NAME_IDS.length - 1) {
                xpath.append(" or ");
            }
        }
        xpath.append("]");
        return new Locator("LAST_NAME_LOCATOR", By.xpath(xpath.toString()));
    }

    private Locator getClearLastNameLocator() {
        StringBuilder xpath = new StringBuilder("//*[contains(@class,'BookingPassengerForm_passenger_form')]//input[");
        for (int i = 0; i < LAST_NAME_IDS.length; i++) {
            xpath.append("@id='").append(LAST_NAME_IDS[i]).append("'");
            if (i < LAST_NAME_IDS.length - 1) {
                xpath.append(" or ");
            }
        }
        xpath.append("]//following-sibling::*//button[contains(@class,'styles_clear_button')]");
        return new Locator("CLEAR_LAST_NAME_LOCATOR", By.xpath(xpath.toString()));
    }

    private Locator outsideLocator() {
        return new Locator("CLICK_OUTSIDE", By.xpath("//*[contains(@class,'BookingPassengerForm_passenger_form')]//*[contains(@class,'styles_helper_text_container')]"));
    }

    private void clearAndFillField(Locator fieldLocator, String value, String logLabel,Locator clearLocator) {
        driverTools.waitForElementToAppear(fieldLocator);
        WebElement fieldElement = driverTools.element(fieldLocator).orElseThrow();
        if(!driverTools.getText(fieldElement).isEmpty()){
            logger.log("Clearing field: " + logLabel);
            driverTools.clearWithJsExecutor(fieldLocator);
            String valueOnForm = driverTools.getValueAttribute(fieldLocator, "value");
            logger.log(logLabel + " ->" + valueOnForm);
            if(!valueOnForm.isEmpty()){
                driverTools.clickWithJavascriptExecutor(clearLocator);
            }
        }
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(fieldLocator, value);
        driverTools.click(outsideLocator());
        String valueOnFormFinal = driverTools.getValueAttribute(fieldLocator, "value");
        logger.log(logLabel + "Final ->" + valueOnFormFinal);
        Assertion.assertThat(logLabel + "Final should be " + value, valueOnFormFinal, is(equalToIgnoringCase(value)));
    }

    @SneakyThrows
    protected void fillPassengerDetailInternational(int indexPax, FlightPaxDetail flightPaxDetail) {
        logger.log("Fill international passenger data");
        if(driverTools.isElementPresent(getDobLocator())) {
            fillDob(indexPax, flightPaxDetail);
        }
        if(driverTools.isElementPresent(getNationalityLocator())) {
            fillNationality(flightPaxDetail.data.get(indexPax).paxNationality);
        }
        if(driverTools.isElementPresent(fillIdLocator())) {
            flightPaxDetail.data.get(indexPax).paxPassportNumber = fillPassport(indexPax, flightPaxDetail);
        }
    }


    public void fillNationality(String nation){
        driverTools.waitForElementToAppear(getNationalityLocator(), 15);
        if (driverTools.isElementDisplayed(getNationalityLocator())){
            driverTools.clickWithJavascriptExecutor(getNationalityLocator());
            fillAndSelectCountry(nation);
        }else{
            if (driverTools.isElementDisplayed(CLICK_PAX_CHEVRON)) {
                driverTools.clickWithJavascriptExecutor(CLICK_PAX_CHEVRON);
            }
            if (driverTools.isElementDisplayed(getNationalityLocator())) {
                driverTools.clickWithJavascriptExecutor(getNationalityLocator());
                fillAndSelectCountry(nation);
            }
        }
    }

    protected void fillPassengerDetailDomestic(int indexPax, FlightPaxDetail flightPaxDetail) {
        logger.log("Fill domestic passenger data");
        if(driverTools.isElementPresent(getDobLocator())) {
            fillDob(indexPax, flightPaxDetail);
        }
        if(driverTools.isElementPresent(getNationalityLocator())) {
            fillNationality(flightPaxDetail.data.get(indexPax).paxNationality);
        }
        if(driverTools.isElementPresent(fillIdLocator())) {
            flightPaxDetail.data.get(indexPax).paxPassportNumber = fillId();
        }
    }

    private Locator getDobLocator() {
        return new Locator("DOB_LOCATOR", By.xpath("//*[contains(@class,'BookingPassengerForm_passenger_form')]//input[contains(@id,'date-of-birth') or contains(@id,'tanggal-lahir')]"));
    }

    private Locator getNationalityLocator(){
        return new Locator("NATIONALITY", By.xpath("//*[contains(@class,'BookingPassengerForm_passenger_form')]//input[contains(@id,'nationality') or contains(@id,'kewarganegaraan')]"));
    }

    private Locator fillIdLocator() {
        return new Locator("FILL_ID_LOCATOR", By.xpath("//*[contains(@class,'BookingPassengerForm_passenger_form')]//input[contains(@aria-label,'passportNo')]"));
    }

    private Locator getIssueDateLocator() {
        return new Locator("ISSUE_DATE_LOCATOR", By.xpath("(//*[contains(@class,'BookingPassengerForm_passenger_form')])[1]//input[@id='issued-date']"));
    }

    private Locator getExpiryDateLocator() {
        return new Locator("EXPIRY_DATE_LOCATOR", By.xpath("(//*[contains(@class,'BookingPassengerForm_passenger_form')])[1]//input[contains(@id,'expiry-date')]"));
    }

    private Locator getPassportNationLocator() {
        return new Locator("PASSPORT_NATION_LOCATOR", By.xpath("(//*[contains(@class,'BookingPassengerForm_passenger_form')])[1]//input[@id='issuing-country' or @aria-label='issuingCountry' or contains(@placeholder,'Issuing Country') or contains(@placeholder,'Negara Penerbit')]"));
    }

    private Locator getDurationTextLocator(){
        return new Locator("YEAR_WHEEL_CONTAINER_TARGET", By.xpath("(//div[contains(@class,'DateWheel_wheel__') and contains(@class, 'DateWheel_keen_slider__')])[3]//span[normalize-space(text())='2026']"));
    }

    @SneakyThrows
    public void fillDob(int indexPax, FlightPaxDetail flightPaxDetail) {
        String type = flightPaxDetail.data.get(indexPax).paxType;
        Locator dobLocator = getDobLocator();
        try{
            if(driverTools.isElementDisplayed(dobLocator)){
                driverTools.clickWithJavascriptExecutor(dobLocator);
                if (type.equals("ADULT"))
                    fillDateDob();
                else
                    fillDate();
            }
        }catch(Exception e){
            if(driverTools.isElementDisplayed(dobLocator)) {
                driverTools.clickWithJavascriptExecutor(dobLocator);
                if (type.equals("ADULT"))
                    fillDateDob();
                else
                    fillDate();            }else{
                logger.log("DOB field for passenger is not displayed or clickable.");
            }
        }
    }

    private void fillDate() {
        driverTools.waitForElementToPresence(DOB_SUBMIT);
        driverTools.clickWithJavascriptExecutor(DOB_SUBMIT);
    }

    private String fillId(){
        Locator idLocator = fillIdLocator();
        String idValue = "";
        if(driverTools.isElementDisplayed(idLocator)){
            idValue = "3272022002" + CoreFunctions.generateRandomInt(3);
            driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(idLocator, idValue);
            driverTools.clickElementByCoordinateDWeb(outsideLocator());
            String idOnBookingForm = driverTools.getValueAttribute(idLocator, "value");
            logger.log("ID on booking form: " + idOnBookingForm);
            Assertion.assertThat("ID on booking form should be " + idValue, idOnBookingForm, is(equalToIgnoringCase(idValue)));
        }
        return idValue;
    }

    public void fillDateExpiry(){
        for (int i = 0; i < 10; i++) {
            driverTools.swipeByDistance(new Locator("Year", By.xpath("(//*[contains(@class, 'DateWheel_keen_slider')])[3]")), Swipe.UP, 0.15);
        }
        driverTools.waitForElementToAppear(DOB_SUBMIT);
        driverTools.click(DOB_SUBMIT);
    }

    public void fillDateDob() {
        Locator yearWheel = new Locator("Year Wheel", By.xpath("(//*[contains(@class, 'DateWheel_keen_slider')])[3]"));

        // Scroll element into view before swiping to avoid out of bounds error
        driverTools.scrollIntoWebView(yearWheel);
        driverTools.waitForElementToAppear(yearWheel, 5);
        driverTools.dummyWait(1); // Let element settle after scroll

        // Use smaller swipe distance to avoid out of bounds exception
        for (int i = 0; i < 6; i++) {
            try {
                // Reduced from 0.15 to 0.05 (5% of element height) to stay within bounds
                driverTools.swipeByDistance(yearWheel, Swipe.DOWN, 0.05);
                Thread.sleep(200); // Small delay between swipes for stability
            } catch (Exception e) {
                logger.log("Swipe attempt " + (i + 1) + " failed: " + e.getMessage() + ", continuing...");
                // Continue with remaining swipes even if one fails
            }
        }

        driverTools.waitForElementToAppear(DOB_SUBMIT);
        driverTools.click(DOB_SUBMIT);
    }

    public String fillPassport(int paxIndex,FlightPaxDetail flightPaxDetail) throws  InterruptedException{
        Locator passportLocator = fillIdLocator();
        String passportValue = "";
        if(driverTools.isElementDisplayed(passportLocator)){
            passportValue = String.valueOf(CoreFunctions.generateRandomInt(10)) ;
            driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(passportLocator, passportValue);
            driverTools.clickWithJavascriptExecutor(outsideLocator());
            String passportOnBookingForm = driverTools.getValueAttribute(passportLocator, "value");
            logger.log("Passport on booking form: " + passportOnBookingForm);
            Assertion.assertThat("Passport on booking form should be " + passportValue, passportOnBookingForm, is(equalToIgnoringCase(passportValue)));
        }
        Locator expiryDateLocator = getExpiryDateLocator();
        driverTools.waitForElementToPresence(expiryDateLocator,20);
        if(driverTools.isElementDisplayed(expiryDateLocator)){
            driverTools.scrollIntoWebView(expiryDateLocator);
            driverTools.clickWithJavascriptExecutor(expiryDateLocator);
            fillDateExpiry();
        }

        Locator issueDateLocator = getIssueDateLocator();
        if(driverTools.isElementDisplayed(issueDateLocator)){
            driverTools.clickWithJavascriptExecutor(issueDateLocator);
            fillDate();
        }
        Locator passportNationLocator = getPassportNationLocator();
        if(driverTools.isElementDisplayed(passportNationLocator)){
            String passportNation = flightPaxDetail.data.get(paxIndex).paxPassportCountry;
            driverTools.clickWithJavascriptExecutor(passportNationLocator);
            fillAndSelectCountry(passportNation);
        }
        return passportValue;
    }

    public void insuranceSelector(){
        WebElement continueToPayment = driverTools.waitForElementToAppear(NEXT_BUTTON).orElseThrow();
        driverTools.scrollIntoWebView(continueToPayment);
        driverTools.waitForElementToPresence(NEXT_BUTTON);
        driverTools.clickWithJavascriptExecutor(NEXT_BUTTON);
        driverTools.waitForElementToAppear(INSURANCE_SELECTOR);
        driverTools.scrollIntoWebView(INSURANCE_SELECTOR);
        driverTools.clickWithJavascriptExecutor(INSURANCE_SELECTOR);
    }

    private void fillPassengerDetailDomesticGuest(int indexPax, FlightPaxDetail flightPaxDetail) {
        logger.log("Fill domestic passenger data");
        fillDobGuest(indexPax, flightPaxDetail);

        if(driverTools.isElementPresent(getNationalityLocator())) {
            fillNationality(flightPaxDetail.data.get(indexPax).paxNationality);
        }
        if(driverTools.isElementPresent(fillIdLocator())) {
            flightPaxDetail.data.get(indexPax).paxPassportNumber = fillId();
        }
    }

    @SneakyThrows
    public void fillDobGuest(int indexPax, FlightPaxDetail flightPaxDetail) {
        int currentYear = CoreFunctions.getCurrentYear();
        int targetYear = currentYear - indexPax;
        String year = String.valueOf(targetYear);
        logger.log("Selecting DOB year: " + year + " (current year " + currentYear + " - indexPax " + indexPax + ")");
        selectDateOfBirthByYear(year);
    }

    public void scrollYearWheelByOffset(int yearsBack) {
        int currentYear = java.time.Year.now().getValue();
        int targetYear = currentYear - yearsBack;
        String targetYearStr = String.valueOf(targetYear);
        logger.log("Scrolling year wheel to: " + targetYearStr + " (" + yearsBack + " years back from " + currentYear + ")");
        scrollYearWheelTo(targetYearStr);
    }


    public void scrollYearWheelToYear(String targetYear) {
        logger.log("Scrolling year wheel to specific year: " + targetYear);
        scrollYearWheelTo(targetYear);
    }

    public void selectDateOfBirthByYear(String targetYear) {
        Locator dobLocator = getDobLocator();
        driverTools.waitForElementToAppear(dobLocator, 10);
        logger.log("Clicking on Date of Birth field to open calendar");
        driverTools.click(dobLocator);

        scrollYearWheelToYear(targetYear);

        driverTools.waitForElementToAppear(DOB_SUBMIT);
        driverTools.click(DOB_SUBMIT);
        logger.log("Date of Birth selected successfully for year: " + targetYear);
    }

    public FlightPaxDetail fillPassengerDetailsGuest(int adultPax, int childPax, int infantPax, String route, String airlineName){
        FlightPaxDetail flightPaxDetail = new FlightPaxDetail(adultPax, childPax, infantPax, route, airlineName, "Indonesia");

        int totalPax = adultPax + childPax + infantPax;
        for (int paxIndex = 1; paxIndex <= totalPax; paxIndex++) {
            chooseDomesticOrInternationalGuest(paxIndex, flightPaxDetail, route);
        }
        return flightPaxDetail;
    }

    public void chooseDomesticOrInternationalGuest(int indexPax, FlightPaxDetail flightPaxDetail,String flightRoute) {
        try {
            //print name of passenger, dob, type
            logger.log("================ Filling passenger details for passenger " + indexPax + " ========");
            logger.log("  Type: " + flightPaxDetail.data.get(indexPax).paxType);
            logger.log("  Name: " + flightPaxDetail.data.get(indexPax).paxFirstName + " " + flightPaxDetail.data.get(indexPax).paxLastName);
            logger.log("  DOB: " + flightPaxDetail.data.get(indexPax).paxBirthDate);
        }catch (Exception e){
            logger.log("flightPaxDetail is null or empty for passenger " + indexPax);
        }

        driverTools.click(getFormButton(indexPax));

        fillFullName(indexPax,flightPaxDetail);
        flightPaxDetail.data.get(indexPax).paxTitle = fillTitle();

        if (flightRoute.equals("domestic")) {
            fillPassengerDetailDomesticGuest(indexPax,flightPaxDetail);
        } else if (flightRoute.equals("international")) {
            fillPassengerDetailInternational(indexPax,flightPaxDetail);
        }
        logger.log("================= Filling passenger details for passenger " + indexPax + " completed ========");

        driverTools.click(SAVE_BUTTON);
    }

    @Override
    public void scrollYearWheelTo(String year) {
        String yearXpath = "//span[contains(@class,'DateWheel_wheel_slide') and normalize-space(text())='" + year + "']";
        By yearWheelBy = By.xpath("(//div[contains(@class, 'DateWheel_keen_slider')])[3]");

        int maxScrolls = 15;
        for (int i = 0; i < maxScrolls; i++) {
            try {
                WebElement targetYear = driver.findElement(By.xpath(yearXpath));

                if (targetYear.isDisplayed()) {
                    logger.log("Found year " + year + " - clicking to select");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", targetYear);
                    return;
                }
            } catch (Exception ignored) {
                // Not visible yet → continue scrolling
            }

            try {
                WebElement yearWheel = driver.findElement(yearWheelBy);
                logger.log("Scrolling year wheel to find year: " + year + " (attempt " + (i + 1) + "/" + maxScrolls + ")");

                ((JavascriptExecutor) driver).executeScript(
                        "const el = arguments[0];" +
                                "const rect = el.getBoundingClientRect();" +
                                "const centerX = rect.left + rect.width / 2;" +
                                "const startY = rect.top + rect.height * 0.7;" +
                                "const endY = rect.top + rect.height * 0.3;" +
                                "const touchStart = new Touch({identifier: Date.now(), target: el, clientX: centerX, clientY: startY});" +
                                "const touchEnd = new Touch({identifier: Date.now(), target: el, clientX: centerX, clientY: endY});" +
                                "el.dispatchEvent(new TouchEvent('touchstart', {bubbles: true, cancelable: true, touches: [touchStart], targetTouches: [touchStart], changedTouches: [touchStart]}));" +
                                "el.dispatchEvent(new TouchEvent('touchmove', {bubbles: true, cancelable: true, touches: [touchEnd], targetTouches: [touchEnd], changedTouches: [touchEnd]}));" +
                                "el.dispatchEvent(new TouchEvent('touchend', {bubbles: true, cancelable: true, touches: [], targetTouches: [], changedTouches: [touchEnd]}));",
                        yearWheel
                );

            } catch (Exception e) {
                logger.log("JS scroll failed: " + e.getMessage() + ", trying Actions scroll");
                try {
                    WebElement yearWheel = driver.findElement(yearWheelBy);
                    WheelInput.ScrollOrigin scrollOrigin = WheelInput.ScrollOrigin.fromElement(yearWheel);
                    new Actions(driver).scrollFromOrigin(scrollOrigin, 0, -100).perform();
                } catch (Exception e2) {
                    logger.log("Actions scroll also failed: " + e2.getMessage());
                }
            }
        }
    }

    private Locator getContactDetail(String text,String type){
        return new Locator("Locator " + type.toUpperCase(),By.xpath("//input[contains(@id,'email')]"));
    }



    @Override
    public boolean verifyChangeEmail(String email) {
        driverTools.waitForWebToLoad();
        boolean isEmailCorrect = isLoggedInUsingCorrectEmail(email);
        logger.log("is email correct after change: " + isEmailCorrect);
        return isEmailCorrect;
    }



}
