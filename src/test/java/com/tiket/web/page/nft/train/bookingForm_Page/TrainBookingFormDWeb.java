package com.tiket.web.page.nft.train.bookingForm_Page;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TrainBookingFormDWeb <T extends WebDriver> extends TrainBookingFormWeb<T>{

    private static final Locator FORM_ID_INFANT = new Locator("FORM_ID_INFANT", new By.ByXPath("(//input[(@id='id-number' or @id='nomor-nik') and @aria-label='identityNumber'])[2]"));
    private static final Locator INFANT_SALUTATION_RADIO = new Locator("INFANT_SALUTATION_RADIO", new By.ByXPath("(//label[.//span[normalize-space(text())='Ms.' or normalize-space(text())='Nona']])[3]"));
    private static final Locator FORM_NAME_INFANT = new Locator("FORM_NAME_INFANT", new By.ByXPath("(//div[contains(@class, 'PassengerForm')]//input[@id='full-name-according-to-idpassport' or @id='nama-lengkap-sesuai-kkpaspor'])[2]"));
    private static final Locator PRICE_DETAILS_CLOSE_BUTTON = new Locator("PRICE_DETAILS_CLOSE_BUTTON", By.xpath("//button[@data-testid='modal-close-button']"));
    private static final Locator PRICE_DETAIL_BTN = new Locator("PRICE_DETAIL_BTN", new By.ByXPath("//*[@data-testid='accordion-icon']"));
    private static final Locator PRICE_DETAIL_HEADING = new Locator("PRICE_DETAIL_HEADING", new By.ByXPath("(//*[@data-testid='collapse-container']//*[contains(@class, 'PriceSummaryItem_label')])[1]"));

    public TrainBookingFormDWeb(Session<T> session) {
        super(session);
    }

    public String getFinalPrice() {
        WebElement chevronIcon = driverTools.waitForElementToAppear(CHEVRON_PRICE).orElseThrow();
        driverTools.click(chevronIcon);
        WebElement finalPrice = driverTools.waitForElementToAppear(FINAL_PRICE).orElseThrow();
        String finalPriceText = driverTools.getText(finalPrice);
        return finalPriceText.replaceAll("[^0-9]", "");
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

        fillPassengerDetail(id);
    }

    public void fillPassengerDetail(String id) {
        driverTools.waitForElementToAppear(PASSENGER_DETAIL_TOGGLE).ifPresent(driverTools::clickWithJavascriptExecutor);

        driverTools.waitForElementToAppear(FORM_ID);
        driverTools.sendKeys(FORM_ID, id);
    }

    @Deprecated
    public void fillPassengerDetailInfant(String name, String id) {
        driverTools.waitForElementToAppear(PASSENGER_DETAIL_HEADER_INFANT).ifPresent(driverTools::scrollIntoWebView);
        driverTools.clickWithJavascriptExecutor(PASSENGER_DETAIL_HEADER_INFANT);

        driverTools.waitForElementToAppear(INFANT_SALUTATION_RADIO).orElseThrow();
        driverTools.clickWithJavascriptExecutor(INFANT_SALUTATION_RADIO);

        driverTools.waitForElementToAppear(FORM_NAME_INFANT).orElseThrow();
        driverTools.clickWithJavascriptExecutor(FORM_NAME_INFANT);
        driverTools.sendKeys(FORM_NAME_INFANT, name);

        driverTools.waitForElementToAppear(FORM_ID_INFANT).orElseThrow();
        driverTools.sendKeys(FORM_ID_INFANT, id);
    }

    @Override
    public Locator getPassengerButton(int index) {
        String xpath = String.format(
            "//p[contains(@class,'PassengersDetailSection_passenger_name') and (contains(text(),'Passenger %1$d') or contains(text(),'Penumpang %1$d'))]",
            index
        );
        return new Locator("PASSENGER_BUTTON_" + index, By.xpath(xpath));
    }

    public void fillPassengerDetailAdult(int adults) {
        toggleSameAsContact();
        fillId(1);
        for (int x = 2; x <= adults; x++) {
            Locator passengerButton = getPassengerButton(x);
            driverTools.scrollToElement(passengerButton);
            driverTools.clickWithJavascriptExecutor(passengerButton);
            logger.log("Filling detail for passenger " + x);
            fillTitleAdult(x);
            fillName(x + 1);
            fillId(x);

        }
    }

    public void fillPassengerDetailInfant(int adults, int infants) {
        for (int x = 1; x <= infants; x++) {
            Locator passengerButton = getPassengerButton(adults + x);
            driverTools.scrollToElement(passengerButton);
            driverTools.clickWithJavascriptExecutor(passengerButton);
            logger.log("Filling detail for infant " + x);
            fillTitleInfant(adults + x);
            fillName(adults + x + 1);
            fillId(adults + x);
        }
    }

    private void fillTitleAdult(int passengerIndex) {
        Locator titleAdult = getTitleLocatorByIndexAdult(passengerIndex);
        driverTools.scrollToElement(titleAdult);
        driverTools.waitForElementToAppear(titleAdult, 5);
        driverTools.clickWithJavascriptExecutor(titleAdult);
    }

    private void fillTitleInfant(int passengerIndex) {
        Locator titleInfant = getTitleLocatorByIndexInfant(passengerIndex);
        driverTools.scrollToElement(titleInfant);
        driverTools.waitForElementToAppear(titleInfant, 5);
        driverTools.waitForElementToBeClickable(titleInfant);
        driverTools.clickWithJavascriptExecutor(titleInfant);
    }

    private void fillName(int idx) {
        String name = CoreFunctions.randomName();
        Locator inputNameLocator = getNameLocatorbyIndex(idx);
        WebElement inputName = driverTools.waitForElementToAppear(inputNameLocator, 5).orElseThrow(() -> new DriverToolsException("Passenger name input not rendered: " + inputNameLocator));
        driverTools.scrollToElement(inputName);
        driverTools.clickWithJavascriptExecutor(inputName);
        driverTools.sendKeys(inputName, name + " Test");
    }

    public void fillId(int index) {
        String fullId = String.valueOf(CoreFunctions.generateRandomInt(16));
        Locator idNumber = getIdNumberInput(index);
        driverTools.scrollToElement(idNumber);
        driverTools.waitForElementToAppear(idNumber);
        driverTools.waitForElementToBeClickable(idNumber);
        driverTools.clickWithJavascriptExecutor(idNumber);
        driverTools.clearInput(idNumber);
        driverTools.sendKeys(idNumber, fullId);
    }

    private static final Locator TRAIN_CONTINUE_TO_PAY = new Locator("TRAIN_CONTINUE_TO_PAY", new By.ByXPath("//button[contains(@class, 'Button_variant_primary') and contains(@class, 'Button_full_width')]"));
    private static final Locator TRAIN_CONFIRM_BOOKING = new Locator("TRAIN_CONFIRM_BOOKING", new By.ByXPath("(//button[contains(@class, 'Button_variant_primary') and contains(@class, 'Button_full_width')])[2]"));

    public void continueToPay() {
        driverTools.waitForElementToPresence(CONTINUE_BUTTON, 3);
        driverTools.scrollToElement(TRAIN_CONTINUE_TO_PAY);
        driverTools.waitForElementToBeClickable(TRAIN_CONTINUE_TO_PAY).ifPresent(driverTools::clickWithJavascriptExecutor);
        logger.log("Clicked Continue to Pay button");
    }

    public void goToPaymentPage() {
        driverTools.waitForElementToAppear(TRAIN_CONFIRM_BOOKING).ifPresent(driverTools::clickWithJavascriptExecutor);
        logger.log("Navigated to Payment Page");
    }

    @Override
    public Locator getTitleLocatorByIndexAdult(int passengerIndex) {
        String xpath = String.format("(//div[contains(@class, 'PassengerForm_wrapper')])[%d]//label[.//span[normalize-space()='Mr.' or normalize-space()='Tuan']]", passengerIndex);
        return new Locator("TITLE_OPTION_ADULT_" + passengerIndex, By.xpath(xpath));
    }

    @Override
    public Locator getTitleLocatorByIndexInfant(int passengerIndex) {
        String xpath = String.format("(//div[contains(@class, 'PassengerForm_wrapper')])[%d]//label[.//span[normalize-space()='Mstr.' or normalize-space()='Tuan']]", passengerIndex);
        return new Locator("TITLE_OPTION_INFANT_" + passengerIndex, By.xpath(xpath));
    }

    @Override
    public Locator getIdNumberInput(int passengerIndex) {
        String xpath = String.format("(//input[@id='id-number' or @id='nomor-nik'])[%d]", passengerIndex);
        return new Locator("ID_NUMBER_INPUT_" + passengerIndex, By.xpath(xpath));
    }

    @Override
    public Locator getNameLocatorbyIndex(int idx) {
        String xpath = String.format("(.//input[(contains(@id, 'full-name') or contains(@id, 'nama-lengkap')) and @type='text'])[%d]", idx);
        return new Locator("INPUT_NAME" + idx, By.xpath(xpath));
    }

    @Override
    public void checkPriceBtn() {
        driverTools.waitForElementToAppear(PRICE_DETAIL_BTN, 5);
        driverTools.scrollUntilElementFound(PRICE_DETAIL_BTN);
        driverTools.click(PRICE_DETAIL_BTN);
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
        WebElement passengerForm = driverTools.waitForElementToAppear(PASSENGER_NAME).orElseThrow();
        return passengerForm.getAttribute("value");
    }
}
