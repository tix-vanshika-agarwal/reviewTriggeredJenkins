package com.tiket.web.page.nft.train.bookingForm_Page;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TrainBookingFormMWeb <T extends WebDriver> extends TrainBookingFormWeb<T>{

    private static final Locator INFANT_SALUTATION_RADIO = new Locator("INFANT_SALUTATION_RADIO", new By.ByXPath("//label[.//span[normalize-space(text())='Mstr.' or normalize-space(text())='Tuan']]"));
    private static final Locator FORM_NAME_INFANT = new Locator("FORM_NAME_INFANT", new By.ByXPath("//div[contains(@class, 'PassengerForm')]//input[@id='full-name-according-to-idpassport' or @id='nama-lengkap-sesuai-kkpaspor']"));
    private static final Locator PRICE_DETAIL_BTN = new Locator("PRICE_DETAIL_BTN", new By.ByXPath("//div[contains(@class, 'SubmissionFooter_price')]/div[contains(@class, 'AnimatedChevronIcon_icon_chevron')]"));
    private static final Locator PRICE_DETAIL_HEADING = new Locator("PRICE_DETAIL_HEADING", new By.ByXPath("//h2[contains(@class, 'BottomSheet_body_title') and contains(@class, 'Text_text') and contains(@class, 'Text_size_h2')]"));
    private static final Locator PRICE_DETAILS_CLOSE_BUTTON = new Locator("PRICE_DETAILS_CLOSE_BUTTON", By.xpath("//button[contains(@class, 'BottomSheet_close_button')]"));
    private static final Locator PASSENGER_NAME = new Locator("PASSENGER_NAME", By.xpath("//p[contains(@class,'ContactDetailSection_capitalize')]"));

    public TrainBookingFormMWeb(Session<T> session) {
        super(session);
    }

    public String getFinalPrice() {
        return "Running on mWeb, method not available";
    }

    @Override
    public String getInitialPrice() {
        WebElement priceLocator = driverTools.waitForElementToAppear(INITIAL_PRICE).orElseThrow();
        return driverTools.getText(priceLocator);
    }

    public void fillBookingForm(String username, String phoneNumber, String email, String id) {
        driverTools.waitForElementToAppear(SALUTATION_RADIO).ifPresent(driverTools::click);

        driverTools.waitForElementToAppear(FORM_NAME);
        driverTools.sendKeys(FORM_NAME, username);

        driverTools.waitForElementToAppear(FORM_PHONE);
        driverTools.sendKeys(FORM_PHONE, phoneNumber);

        driverTools.waitForElementToAppear(FORM_EMAIL);
        driverTools.sendKeys(FORM_EMAIL, email);

        if(Configs.PLATFORM == Platform.AMWEB){
            driverTools.hideKeyboard();
            driverTools.scrollToElement(CONTINUE_BUTTON);
        }

        fillPassengerDetail(id);
    }

    public void fillPassengerDetail(String id) {
        driverTools.waitForElementToAppear(PASSENGER_DETAIL_TOGGLE).ifPresent(driverTools::clickWithJavascriptExecutor);

        driverTools.waitForElementToAppear(FORM_ID);
        driverTools.sendKeys(FORM_ID, id);

        driverTools.waitForElementToAppear(PASSENGER_DETAIL_SAVE).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    @Deprecated
    public void fillPassengerDetailInfant(String name, String id) {
        driverTools.waitForElementToAppear(PASSENGER_DETAIL_HEADER_INFANT).ifPresent(driverTools::scrollIntoWebView);
        driverTools.clickWithJavascriptExecutor(PASSENGER_DETAIL_HEADER_INFANT);

        driverTools.waitForElementToAppear(INFANT_SALUTATION_RADIO).orElseThrow();
        driverTools.clickWithJavascriptExecutor(INFANT_SALUTATION_RADIO);

        driverTools.waitForElementToAppear(FORM_NAME_INFANT).orElseThrow();
        driverTools.sendKeys(FORM_NAME_INFANT, name);

        driverTools.waitForElementToAppear(FORM_ID).orElseThrow();
        driverTools.sendKeys(FORM_ID, id);

        driverTools.waitForElementToAppear(PASSENGER_DETAIL_SAVE).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public void fillPassengerDetailAdult(int adults) {
        toggleSameAsContact();
        fillId(1);
        driverTools.waitForElementToAppear(PASSENGER_DETAIL_SAVE).ifPresent(driverTools::clickWithJavascriptExecutor);
        for (int x = 2; x <= adults; x++) {
            Locator passengerButton = getPassengerButton(x);
            driverTools.clickWithJavascriptExecutor(passengerButton);
            logger.log("Filling detail for passenger " + x);
            fillTitleAdult(x);
            fillName(x + 1);
            fillId(x);
            driverTools.waitForElementToAppear(PASSENGER_DETAIL_SAVE).ifPresent(driverTools::clickWithJavascriptExecutor);
        }
    }

    public void fillPassengerDetailInfant(int adults, int infants) {
        for (int x = 1; x <= infants; x++) {
            Locator passengerButton = getPassengerButton(adults + x);
            driverTools.clickWithJavascriptExecutor(passengerButton);
            logger.log("Filling detail for infant " + x);
            fillTitleInfant(x);
            fillName(adults + x + 1);
            fillId(adults + x);
            driverTools.waitForElementToAppear(PASSENGER_DETAIL_SAVE).ifPresent(driverTools::clickWithJavascriptExecutor);
        }
    }

    private void fillTitleAdult(int passengerIndex) {
        Locator titleAdult = getTitleLocatorByIndexAdult(passengerIndex);
        driverTools.waitForElementToAppear(titleAdult, 5);
        driverTools.clickWithJavascriptExecutor(titleAdult);
    }

    private void fillTitleInfant(int passengerIndex) {
        Locator titleInfant = getTitleLocatorByIndexInfant(passengerIndex);
        driverTools.waitForElementToAppear(titleInfant, 5);
        driverTools.waitForElementToBeClickable(titleInfant);
        driverTools.clickWithJavascriptExecutor(titleInfant);
    }

    private void fillName(int idx) {
        String name = CoreFunctions.randomName();
        Locator inputName = getNameLocatorbyIndex(idx);
        driverTools.clickWithJavascriptExecutor(inputName);
        driverTools.sendKeys(inputName, name + " Test");
    }

    public void fillId(int index) {
        String fullId = String.valueOf(CoreFunctions.generateRandomInt(16));
        Locator idNumber = getIdNumberInput(index);
        driverTools.waitForElementToAppear(idNumber);
        driverTools.waitForElementToBeClickable(idNumber);
        driverTools.clickWithJavascriptExecutor(idNumber);
        driverTools.clearInput(idNumber);
        driverTools.sendKeys(idNumber, fullId);
    }

    public void continueToPay() {
        driverTools.waitForElementToBeClickable(CONTINUE_TO_PAY).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    @Override
    public Locator getTitleLocatorByIndexAdult(int passengerIndex) {
        String xpath = "((//div[contains(@class, 'PassengerForm_wrapper')])//label[contains(@class, 'Radio_radio') and .//span[contains(@class, 'Text_text') and (normalize-space(text())='Mr.' or normalize-space(text())='Tuan')]])";
        return new Locator("TITLE_OPTION_ADULT", By.xpath(xpath));
    }

    @Override
    public Locator getTitleLocatorByIndexInfant(int passengerIndex) {
        String xpath = "((//div[contains(@class, 'PassengerForm_wrapper')])//label[contains(@class, 'Radio_radio') and .//span[contains(@class, 'Text_text') and (normalize-space(text())='Mstr.' or normalize-space(text())='Tuan')]])";
        return new Locator("TITLE_OPTION_INFANT_" + passengerIndex, By.xpath(xpath));
    }

    @Override
    public Locator getIdNumberInput(int passengerIndex) {
        String xpath = "(//input[@id='id-number' or @id='nomor-nik'])[1]";
        return new Locator("ID_NUMBER_INPUT_" + passengerIndex, By.xpath(xpath));
    }

    @Override
    public Locator getNameLocatorbyIndex(int idx) {
        String xpath = "(//input[contains(@id, 'full-name') and @type='text'])[last()]";
        return new Locator("INPUT_NAME" + idx, By.xpath(xpath));
    }

    @Override
    public void checkPriceBtn() {
        driverTools.waitForElementToAppear(PRICE_DETAIL_BTN);
        driverTools.clickWithJavascriptExecutor(PRICE_DETAIL_BTN);
    }

    @Override
    public boolean isPriceDetailsDisplayed() {
        driverTools.waitForElementToAppear(PRICE_DETAIL_HEADING, 3);
        return driverTools.isElementDisplayed(PRICE_DETAIL_HEADING);
    }

    @Override
    public void closePricePopUp() {
        driverTools.waitForElementToAppear(PRICE_DETAILS_CLOSE_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    @Override
    public String getPassengerName() {
        WebElement passengerNameLocator = driverTools.waitForElementToAppear(PASSENGER_NAME).orElseThrow();
        String passengerFullName = passengerNameLocator.getText().trim();
        String passengerNameOnly = passengerFullName.replaceAll("(?i)^\\s*\\d*\\.?\\s*(mr|mrs|ms|tuan|nyonya|nona)\\.?\\s+", "").trim();
        return passengerNameOnly;
    }
}
