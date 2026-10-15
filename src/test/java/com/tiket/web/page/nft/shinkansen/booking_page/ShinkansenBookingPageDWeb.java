package com.tiket.web.page.nft.shinkansen.booking_page;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class ShinkansenBookingPageDWeb<T extends WebDriver> extends ShinkansenBookingPageWeb<T> {
    public ShinkansenBookingPageDWeb(Session<T> session) {
        super(session);
    }

    // Locators mirrored from Whoosh DWeb for now.
    private static final Locator INPUT_NAME = new Locator("INPUT_NAME", new By.ByXPath("//input[@type='text' and (contains(@id, 'full-name') or contains(@id, 'nama-lengkap'))]"));
    private static final Locator TOGGLE_BTN = new Locator("TOGGLE_BTN", new By.ByXPath("//div[contains(@class, 'Toggle_knob')]"));
    private static final Locator SAVE_BTN = new Locator("SAVE_BTN", new By.ByXPath("//button[contains(@class, 'Button_variant_primary')]//*[(normalize-space(text())='Simpan' or normalize-space(text())='Save')]"));
    private static final Locator DEPARTURE_SHINKANSEN = new Locator("DEPARTURE_SHINKANSEN", new By.ByXPath("(//div[contains(@class,'JourneySummarySection_train_class_container')]//span)[1]"));
    private static final Locator DEPARTURE_FROM = new Locator("DEPARTURE_FROM", new By.ByXPath("(//ol[contains(@class, 'ProgressStepVerticalDots_progress_step_vertical_dots')]//li[1]//div[contains(@class,'Text_variant_highEmphasis')])[1]"));
    private static final Locator DEPARTURE_TO = new Locator("DEPARTURE_TO", new By.ByXPath("(//ol[contains(@class, 'ProgressStepVerticalDots_progress_step_vertical_dots')]//li[2]//div[contains(@class,'Text_variant_highEmphasis')])[1]"));
    private static final Locator RETURN_SHINKANSEN = new Locator("RETURN_SHINKANSEN", new By.ByXPath("(//div[contains(@class,'JourneySummarySection_train_class_container')]//span)[2]"));
    private static final Locator RETURN_FROM = new Locator("RETURN_FROM", new By.ByXPath("(//ol[contains(@class, 'ProgressStepVerticalDots_progress_step_vertical_dots')]//li[1]//div[contains(@class,'Text_variant_highEmphasis')])[2]"));
    private static final Locator RETURN_TO = new Locator("RETURN_TO", new By.ByXPath("(//ol[contains(@class, 'ProgressStepVerticalDots_progress_step_vertical_dots')]//li[2]//div[contains(@class,'Text_variant_highEmphasis')])[2]"));

    @Override
    public List<String> fillPassengerDetailAdult(int adults) {
        driverTools.waitForElementToAppear(INPUT_NAME);
        String loggedInName = driverTools.getAttribute(INPUT_NAME.by(), "value");
        List<String> nameListAdult = new ArrayList<>();
        nameListAdult.add("MR " + loggedInName);
        Locator firstPassenger = getPassengerButton(1);
        driverTools.scrollIntoWebView(firstPassenger);
        toggleSameAsContact();
        fillId(1);
        for (int x = 2; x <= adults; x++) {
            Locator passengerButton = getPassengerButton(x);
            driverTools.scrollIntoWebView(passengerButton);
            driverTools.clickWithJavascriptExecutor(passengerButton);
            logger.log("Filling detail for passenger " + x);
            fillTitleAdult(x);
            String name = fillName(x + 1);
            logger.log("Expected Adult : " + name);
            nameListAdult.add("MR " + name);
            fillId(x);
        }
        return nameListAdult;
    }

    @Override
    public List<String> fillPassengerDetailInfant(int adults, int infants) {
        List<String> nameListInfants = new ArrayList<>();
        for (int x = 1; x <= infants; x++) {
            Locator passengerButton = getPassengerButton(adults + x);
            driverTools.scrollIntoWebView(passengerButton);
            driverTools.clickWithJavascriptExecutor(passengerButton);
            logger.log("Filling detail for infant " + x);
            fillTitleInfant(x);
            String name = fillName(adults + x + 1);
            logger.log("Expected Infant : " + name);
            nameListInfants.add("MR " + name);
            fillDOB(x);
            fillId(adults + x);
        }
        return nameListInfants;
    }

    private Locator getPassengerButton(int index) {
        String xpath = "//*[contains(text(),'" + index + "') and (contains(text(),'Passenger') or contains(text(),'Penumpang'))]";
        return new Locator("PASSENGER_BUTTON_" + index, By.xpath(xpath));
    }

    private void toggleSameAsContact() {
        driverTools.waitForElementToBeClickable(TOGGLE_BTN);
        driverTools.clickWithJavascriptExecutor(TOGGLE_BTN);
        fillId(1);
    }

    private void fillTitleAdult(int passengerIndex) {
        Locator titleAdult = getTitleLocatorByIndexAdult(passengerIndex);
        driverTools.scrollIntoWebView(titleAdult);
        driverTools.waitForElementToAppear(titleAdult, 5);
        driverTools.clickWithJavascriptExecutor(titleAdult);
    }

    private void fillTitleInfant(int passengerIndex) {
        Locator titleInfant = getTitleLocatorByIndexInfant(passengerIndex);
        driverTools.scrollIntoWebView(titleInfant);
        driverTools.waitForElementToAppear(titleInfant, 5);
        driverTools.waitForElementToBeClickable(titleInfant);
        driverTools.clickWithJavascriptExecutor(titleInfant);
    }

    private String fillName(int idx) {
        String name = CoreFunctions.randomName();
        Locator inputName = getNameLocatorbyIndex(idx);
        driverTools.scrollToElement(inputName);
        driverTools.clickWithJavascriptExecutor(inputName);
        driverTools.sendKeys(inputName, name + " Test");
        return driverTools.getAttribute(inputName.by(), "value");
    }

    private void fillDOB(int idx) {
        Locator DOB = getDOBLocator(idx);
        driverTools.scrollToElement(DOB);
        driverTools.waitForElementToAppear(DOB);
        driverTools.clickWithJavascriptExecutor(DOB);
        driverTools.waitForElementToAppear(SAVE_BTN);
        driverTools.clickWithJavascriptExecutor(SAVE_BTN);
    }

    private void fillId(int index) {
        String fullId = String.valueOf(CoreFunctions.generateRandomInt(16));
        Locator idNumber = getIdNumberInput(index);
        driverTools.scrollToElement(idNumber);
        driverTools.waitForElementToAppear(idNumber);
        driverTools.waitForElementToBeClickable(idNumber);
        driverTools.clickElementByCoordinateDWeb(idNumber);
        driverTools.clearInput(idNumber);
        driverTools.sendKeys(idNumber, fullId);
    }

    private Locator getIdNumberInput(int passengerIndex) {
        String xpath = String.format("(//div[contains(@class, 'PassengerForm_wrapper')]//input[@id='id-number' or @id='nomor-nik'])[%d]", passengerIndex);
        return new Locator("ID_NUMBER_INPUT_" + passengerIndex, By.xpath(xpath));
    }

    private Locator getTitleLocatorByIndexAdult(int passengerIndex) {
        String xpath = String.format("(//div[contains(@class, 'PassengerForm_wrapper')])[%d]//label[.//span[normalize-space()='Mr.' or normalize-space()='Tuan']]", passengerIndex);
        return new Locator("TITLE_OPTION_ADULT_" + passengerIndex, By.xpath(xpath));
    }

    private Locator getTitleLocatorByIndexInfant(int passengerIndex) {
        String xpath = String.format("((//div[contains(@class, 'PassengerForm_wrapper')])//label[.//span[normalize-space()='Mstr.' or normalize-space()='Tuan']])[%d]", passengerIndex);
        return new Locator("TITLE_OPTION_INFANT_" + passengerIndex, By.xpath(xpath));
    }

    private static final String[] NAME_IDS = {
            "full-name",
            "nama-lengkap"
    };

    private Locator getNameLocatorbyIndex(int idx) {
        StringBuilder xpath = new StringBuilder("(.//input[(");
        for (int i = 0; i < NAME_IDS.length; i++) {
            xpath.append("contains(@id, '").append(NAME_IDS[i]).append("')");
            if (i < NAME_IDS.length - 1) {
                xpath.append(" or ");
            }
        }
        xpath.append(") and @type='text'])[").append(idx).append("]");
        return new Locator("INPUT_NAME" + idx, By.xpath(xpath.toString()));
    }

    private Locator getDOBLocator(int idx) {
        String xpath = String.format("(//input[@id='date-of-birth' or @id='tanggal-lahir'])[%d]", idx);
        return new Locator("DOB" + idx, By.xpath(xpath));
    }

    @Override
    public String verifiesDepartureShinkansen(String tripType) {
        return driverTools.getText(DEPARTURE_SHINKANSEN).orElseThrow();
    }

    @Override
    public String verifiesReturnShinkansen() {
        return driverTools.getText(RETURN_SHINKANSEN).orElseThrow();
    }

    @Override
    public String verifyDepartureOrigin(String tripType) {
        return driverTools.getText(DEPARTURE_FROM).orElseThrow();
    }

    @Override
    public String verifyDepartureDestination(String tripType) {
        return driverTools.getText(DEPARTURE_TO).orElseThrow();
    }

    @Override
    public String verifyReturnOrigin() {
        return driverTools.getText(RETURN_FROM).orElseThrow();
    }

    @Override
    public String verifyReturnDestination() {
        return driverTools.getText(RETURN_TO).orElseThrow();
    }

    @Override
    public List<String> getPassengerIDs(int adultCount, int infantCount) {
        List<String> passengerIDs = new ArrayList<>();
        for (int i = 1; i <= adultCount + infantCount; i++) {
            Locator idNumber = getIdNumberInput(i);
            driverTools.scrollToElement(idNumber);
            driverTools.waitForElementToAppear(idNumber);
            String id = driverTools.getAttribute(idNumber.by(), "value");
            logger.log("Passenger" + i + ":" + id);
            passengerIDs.add(id);
        }
        return passengerIDs;
    }
}
