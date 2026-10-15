package com.tiket.web.page.nft.whoosh.booking_page;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class WhooshBookingPageMWeb<T extends WebDriver> extends WhooshBookingPageWeb<T> {

    public WhooshBookingPageMWeb(Session<T> session) {
        super(session);
    }

    private static final Locator SAVE_DOB = new Locator("SAVE_DOB", new By.ByXPath("//button[contains(@class,'DatePicker_datepicker_button')]"));
    private static final Locator RETURN_WHOOSH = new Locator("RETURN", new By.ByXPath("(//div[contains(@class,'JourneySummarySection_train_class_container')]//span[contains(text(), 'Whoosh')])[4]"));
    private static final Locator RETURN_FROM = new Locator("RETURN_FROM", new By.ByXPath("(//ol[contains(@class, 'ProgressStepVerticalDots_progress_step_vertical_dots')]//li[1]//div[contains(@class,'Text_variant_highEmphasis')])[4]"));
    private static final Locator RETURN_TO = new Locator("RETURN_TO", new By.ByXPath("(//ol[contains(@class, 'ProgressStepVerticalDots_progress_step_vertical_dots')]//li[2]//div[contains(@class,'Text_variant_highEmphasis')])[4]"));
    private static final Locator CLOSE_BTN = new Locator("CLOSE_BTN", By.xpath("//button[@type='button' and contains(@class, 'BottomSheet_close_button')]"));

    @Override
    public List<String> fillPassengerDetailAdult(int adults) throws InterruptedException {
        List<String> nameListAdult = new ArrayList<>();
        toggleSameAsContact();
        Locator firstPassenger = getPassengerButton(1);
        driverTools.scrollIntoWebViewMWeb(firstPassenger);
        Locator inputName = getNameLocatorbyIndex(1);
        String loggedInName = driverTools.getAttribute(inputName.by(), "value");
        nameListAdult.add("MR "+ loggedInName);
        fillId(1);
        driverTools.waitForElementToAppear(SAVE_BTN);
        driverTools.clickWithJavascriptExecutor(SAVE_BTN);
        for (int x = 2; x <= adults; x++) {
            Locator passengerButton = getPassengerButton(x);
            driverTools.scrollIntoWebViewMWeb(passengerButton);
            driverTools.clickWithJavascriptExecutor(passengerButton);
            logger.log("Filling detail for passenger " + x);
            fillTitleAdult(x);
            String name = fillName(x + 1);
            logger.log("Expected Adult : " + name);
            nameListAdult.add("MR " + name);
            fillId(x);
            driverTools.waitForElementToAppear(SAVE_BTN);
            driverTools.clickWithJavascriptExecutor(SAVE_BTN);
            driverTools.waitForElementToAppear(getPassengerButton(x+1));
        }
        return nameListAdult;
    }

    @Override
    public List<String> fillPassengerDetailInfant(int adults, int infants) {
        List<String> nameListInfants = new ArrayList<>();
        for (int x = 1; x <= infants; x++) {
            Locator passengerButton = getPassengerButton(adults + x);
            driverTools.scrollIntoWebViewMWeb(passengerButton);
            driverTools.clickWithJavascriptExecutor(passengerButton);
            logger.log("Filling detail for infant " + x);
            fillTitleInfant(x);
            String name = fillName(adults + x + 1);
            logger.log("Expected Infant : " + name) ;
            nameListInfants.add("MR " + name);
            fillDOB(x);
            fillId(adults + x);
            driverTools.waitForElementToAppear(SAVE_BTN);
            driverTools.clickWithJavascriptExecutor(SAVE_BTN);
        }
        return nameListInfants;
    }

    private Locator getPassengerButton(int index) {
        String xpath = String.format("(//p[contains(@class, 'PassengersDetailSection_passenger_name')])[%d]", index);
        return new Locator("PASSENGER_BUTTON_" + index, By.xpath(xpath));
    }

    private void toggleSameAsContact() {
        driverTools.waitForElementToBeClickable(TOGGLE_BTN);
        driverTools.clickWithJavascriptExecutor(TOGGLE_BTN);
        fillId(1);
    }

    private void fillTitleAdult(int passengerIndex) {
        Locator titleAdult = getTitleLocatorByIndexAdult(passengerIndex);
        driverTools.scrollIntoWebViewMWeb(titleAdult);
        driverTools.waitForElementToAppear(titleAdult, 5);
        driverTools.clickWithJavascriptExecutor(titleAdult);
    }

    private void fillTitleInfant(int passengerIndex) {
        Locator titleInfant = getTitleLocatorByIndexInfant(passengerIndex);
        driverTools.scrollIntoWebViewMWeb(titleInfant);
        driverTools.waitForElementToAppear(titleInfant, 5);
        driverTools.clickWithJavascriptExecutor(titleInfant);
    }

    private String fillName(int idx) {
        String name = CoreFunctions.randomName();
        Locator inputName = getNameLocatorbyIndex(idx);
        driverTools.scrollIntoWebViewMWeb(inputName);
        driverTools.waitForElementToAppear(inputName,5);
        driverTools.click(inputName);
        driverTools.clearInput(inputName);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(inputName, name + " Test");
        return driverTools.getAttribute(inputName.by(), "value");
    }

    private void fillDOB(int idx) {
        Locator DOB = getDOBLocator(idx);
        driverTools.scrollIntoWebViewMWeb(DOB);
        driverTools.waitForElementToAppear(DOB);
        driverTools.clickWithJavascriptExecutor(DOB);
        driverTools.waitForElementToAppear(SAVE_DOB);
        driverTools.clickWithJavascriptExecutor(SAVE_DOB);
    }

    private void fillId(int index) {
        String fullId = String.valueOf(CoreFunctions.generateRandomInt(16));
        Locator idNumber = getIdNumberInput(index);
        driverTools.scrollIntoWebViewMWeb(idNumber);
        driverTools.waitForElementToAppear(idNumber);
        driverTools.waitForElementToBeClickable(idNumber);
        driverTools.clickWithJavascriptExecutor(idNumber);
        driverTools.clearInput(idNumber);
        driverTools.sendKeys(idNumber, fullId);
    }

    private Locator getIdNumberInput(int passengerIndex) {
        String xpath = "(//div[contains(@class, 'PassengerForm_wrapper')]//input[@id='id-number'])";
        return new Locator("ID_NUMBER_INPUT_" + passengerIndex, By.xpath(xpath));
    }

    private Locator getTitleLocatorByIndexAdult(int passengerIndex) {
        String xpath = "(//div[contains(@class, 'PassengerForm_wrapper')])//label[.//span[normalize-space()='Mr.' or normalize-space()='Tuan']]";
        return new Locator("TITLE_OPTION_ADULT_" + passengerIndex, By.xpath(xpath));
    }


    private Locator getTitleLocatorByIndexInfant(int passengerIndex) {
        String xpath = "((//div[contains(@class, 'PassengerForm_wrapper')])//label[.//span[normalize-space()='Mstr.' or normalize-space()='Tuan']])";
        return new Locator("TITLE_OPTION_INFANT_" + passengerIndex, By.xpath(xpath));
    }

    private Locator getNameLocatorbyIndex(int idx) {
        String xpath = "//div[contains(@class,'PassengerForm_part')]//input[@id='full-name-according-to-idpassport']";
        return new Locator("INPUT_NAME" + idx, By.xpath(xpath));
    }

    private Locator getDOBLocator(int idx) {
        String xpath = ("//input[@id='date-input']");
        return new Locator("DOB" + idx, By.xpath(xpath));
    }

    @Override
    public String verifiesDepartureWhoosh(String tripType) {
        if (Objects.equals(tripType, "roundtrip")) {
            Locator departureWhoosh = getDepartureWhoosh(3);
            return driverTools.getText(departureWhoosh).orElseThrow();
        } else {
            Locator departureWhoosh = getDepartureWhoosh(2);
            return driverTools.getText(departureWhoosh).orElseThrow();
        }
    }

    @Override
    public String verifiesReturnWhoosh() {
        return driverTools.getText(RETURN_WHOOSH).orElseThrow();
    }

    @Override
    public String verifyDepartureOrigin(String tripType) {
        if (Objects.equals(tripType, "roundtrip")) {
            Locator departureOrigin = getDepartureOrigin(3);
            return driverTools.getText(departureOrigin).orElseThrow();
        } else {
            Locator departureOrigin = getDepartureOrigin(2);
            return driverTools.getText(departureOrigin).orElseThrow();
        }
    }

    @Override
    public String verifyDepartureDestination(String tripType) {
        if (Objects.equals(tripType, "roundtrip")) {
            Locator departureDestination = getDepartureDestination(3);
            return driverTools.getText(departureDestination).orElseThrow();
        } else {
            Locator departureDestination = getDepartureDestination(2);
            return driverTools.getText(departureDestination).orElseThrow();
        }
    }

    @Override
    public String verifyReturnOrigin() {
        return driverTools.getText(RETURN_FROM).orElseThrow();
    }

    @Override
    public String verifyReturnDestination() {
        return driverTools.getText(RETURN_TO).orElseThrow();
    }

    private Locator getDepartureWhoosh(int index) {
        String xpath = String.format("(//div[contains(@class,'JourneySummarySection_train_class_container')]//span[contains(text(), 'Whoosh')])[%d]", index);
        return new Locator("DEPARTURE_WHOOSH", By.xpath(xpath));
    }

    private Locator getDepartureOrigin(int index) {
        String xpath = String.format("(//ol[contains(@class, 'ProgressStepVerticalDots_progress_step_vertical_dots')]//li[1]//div[contains(@class,'Text_variant_highEmphasis')])[%d]", index);
        return new Locator("DEPARTURE_ORIGIN", By.xpath(xpath));
    }

    private Locator getDepartureDestination(int index) {
        String xpath = String.format("(//ol[contains(@class, 'ProgressStepVerticalDots_progress_step_vertical_dots')]//li[2]//div[contains(@class,'Text_variant_highEmphasis')])[%d]", index);
        return new Locator("DEPARTURE_ORIGIN", By.xpath(xpath));
    }

    @Override
    public List<String> getPassengerIDs(int adultCount, int infantCount){
        List<String> passengerIDs = new ArrayList<>() ;
        for ( int i = 1 ; i <= adultCount+infantCount ; i++ ) {
            Locator passengerButton = getPassengerButton(i);
            driverTools.scrollIntoWebViewMWeb(passengerButton);
            driverTools.clickWithJavascriptExecutor(passengerButton);
            Locator idNumber = getIdNumberInput(i);
            driverTools.scrollIntoWebViewMWeb(idNumber);
            driverTools.waitForElementToAppear(idNumber);
            String id = driverTools.getAttribute(idNumber.by(),"value");
            logger.log("Passenger" + i + ":" + id) ;
            passengerIDs.add(id);
            driverTools.waitForElementToBeClickable(CLOSE_BTN);
            driverTools.clickWithJavascriptExecutor(CLOSE_BTN);
        }
        return passengerIDs;
    }
}
