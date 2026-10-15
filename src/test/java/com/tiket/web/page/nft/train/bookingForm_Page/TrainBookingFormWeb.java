package com.tiket.web.page.nft.train.bookingForm_Page;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public abstract class TrainBookingFormWeb<T extends WebDriver> extends WebPage<T> {

    public static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.xpath("//button[contains(@class, 'Button_button')]//*[(normalize-space(text())='Continue' or normalize-space(text())='Lanjutkan')]"));
    public static final Locator CONTINUE_TO_PAY_BUTTON = new Locator("CONTINUE_TO_PAY_BUTTON", AppiumBy.xpath("//*[(normalize-space(text())='Continue to pay' or normalize-space(text())='Lanjut bayar')]"));
    private static final Locator CONTINUE_TO_PAY_BUTTON_DIALOG = new Locator("CONTINUE_TO_PAY_BUTTON_DIALOG", new By.ByXPath("//*[contains(@class,'Dialog_pop_up')]//*[contains(@class,'Button_variant_primary')]"));
    public static final Locator INITIAL_PRICE = new Locator("INITIAL_PRICE", AppiumBy.xpath("(//span[contains(@class, 'Text_weight_bold') and starts-with(normalize-space(text()), 'IDR')])[last()]"));
    public static final Locator FINAL_PRICE = new Locator("FINAL_PRICE", AppiumBy.xpath("//div[contains(@class, 'Text_weight_bold') and starts-with(normalize-space(text()), 'IDR')]"));
    public static final Locator TIME_LOCATOR = new Locator("TIME_LOCATOR", AppiumBy.xpath("//div[contains(@class, 'JourneySummarySection_date_and_time')]//span[contains(text(), ':')]"));

    public static final Locator CHEVRON_PRICE = new Locator("CHEVRON_PRICE", AppiumBy.xpath("(//div[contains(@class,'AnimatedChevronIcon_icon_chevron')])[1]"));
    public static final Locator PASSENGER_NAME = new Locator("PASSENGER_NAME", AppiumBy.xpath("(//input[@id='full-name-according-to-idpassport'] | //input[@id='nama-lengkap-sesuai-ktppasporsim'])[1]"));
    public static final Locator PASSENGER_DETAIL_TOGGLE = new Locator("PASSENGER_DETAIL_TOGGLE", AppiumBy.xpath("//div[contains(@class, 'Toggle_container')]//div[contains(@class, 'Toggle_toggle')]"));
    public static final Locator PASSENGER_DETAIL_SAVE = new Locator("PASSENGER_DETAIL_SAVE", AppiumBy.xpath("//button[contains(@class, 'Button_button') and (normalize-space(text())='Save' or normalize-space(text())='Simpan')]"));
    public static final Locator SALUTATION_RADIO = new Locator("SALUTATION_RADIO", AppiumBy.xpath("//label[.//span[normalize-space(text())='Mr.' or normalize-space(text())='Tuan']]"));
    public static final Locator FORM_NAME = new Locator("FORM_NAME", AppiumBy.xpath("//div[contains(@class, 'ContactForm_name')]//input[@name='fullName']"));
    public static final Locator FORM_PHONE = new Locator("FORM_PHONE", AppiumBy.xpath("//input[(@id='mobile-number' or @id='nomor-ponsel') and @type='tel']"));
    public static final Locator FORM_EMAIL = new Locator("FORM_EMAIL", AppiumBy.xpath("//input[(@id='email-address' or @id='alamat-email') and @name='emailAddress']"));
    public static final Locator FORM_ID = new Locator("FORM_ID", AppiumBy.xpath("//input[(@id='id-number' or @id='nomor-nik') and @aria-label='identityNumber']"));
    private static final Locator TRAIN_CARD_PRICE_BOOKING_FORM = new Locator("TRAIN_CARD_PRICE_BOOKING_FORM", new By.ByXPath("//div[contains(@class, 'footer')]//span[contains(@class, 'Text') and contains(text(), 'IDR')]"));
    public static final Locator PASSENGER_DETAIL_HEADER_INFANT = new Locator("PASSENGER_DETAIL_HEADER_INFANT", new By.ByXPath("(//p[contains(@class, 'passenger_name')])[2]"));
    private static final Locator CONTINUE_BTN = new Locator("CONTINUE_BTN", new By.ByXPath("//button[contains(@class, 'booking_footer_button')]"));
    private static final Locator INPUT_NAME = new Locator("INPUT_NAME", new By.ByXPath("//input[@type='text' and (contains(@id, 'full-name') or contains(@id, 'nama-lengkap'))]"));
    private static final Locator TOGGLE_BTN = new Locator("TOGGLE_BTN", new By.ByXPath("//div[contains(@class, 'Toggle_knob')]"));
    private static final Locator CONTACT_CARD = new Locator("CONTACT_CARD", new By.ByXPath("//div[contains(@class, 'Card_card_container') and .//div[contains(@class, 'ContactForm_wrapper')]]"));
    private static final Locator SALUTATION = new Locator("SALUTATION", new By.ByXPath("(//label[contains(@class,'Radio_radio') and (.//span[normalize-space()='Mr.'] or .//span[normalize-space()='Tuan'])])[1]"));
    private static final Locator PHONE_NUMBER_INPUT = new Locator("PHONE_NUMBER_INPUT", new By.ByXPath("//input[@id='nomor-ponsel' or @id='mobile-number']"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", new By.ByXPath("//input[@id='email-address' or @id='email' or @type='email' or @name='email' or contains(@id,'email')]"));
    private static final Locator EMAIL_DISABLED = new Locator("EMAIL_DISABLED", new By.ByXPath("//*[contains(@class, 'styles_state_disabled')]"));
    private static final Locator AUTOSELECTED_INSURANCE = new Locator("AUTOSELECTED_INSURANCE", new By.ByXPath("//button[normalize-space(text())='Batalkan' or normalize-space(text())='Cancel']"));
    private static final Locator ADD_INSURANCE = new Locator("ADD_INSURANCE", new By.ByXPath("(//*[contains(@class,'CardCompoundButton_button')])[1]"));
    private static final Locator ADD_LAST_INSURANCE = new Locator("ADD_LAST_INSURANCE", new By.ByXPath("(//*[contains(@class,'CardCompoundButton_button')])[last()]"));
    public static final Locator CONTINUE_TO_PAY = new Locator("CONTINUE_TO_PAY", new By.ByXPath("//button[contains(@class, 'Button_variant_primary') and contains(@class, 'Button_size_large')]"));
    private static final Locator CONFIRM_BOOKING = new Locator("CONFIRM_BOOKING", new By.ByXPath("(//button[contains(@class, 'Button_variant_primary') and contains(@class, 'Button_size_large')])[2]"));
    private static final Locator PRICE_OF_BOOKING = new Locator("PRICE_OF_BOOKING", new By.ByXPath("(//span[contains(@class,'PriceSummaryItem_price') and contains(@class,'Text_text')])[1]"));
    private static final Locator DEPARTURE_FROM = new Locator("DEPARTURE_FROM", new By.ByXPath("(//ol[contains(@class, 'ProgressStepVerticalDots_progress_step_vertical_dots')]//li[1]//div[contains(@class,'Text_variant_highEmphasis')])[1]"));
    private static final Locator DEPARTURE_TO = new Locator("DEPARTURE_TO", new By.ByXPath("(//ol[contains(@class, 'ProgressStepVerticalDots_progress_step_vertical_dots')]//li[2]//div[contains(@class,'Text_variant_highEmphasis')])[1]"));
    private static final Locator CLOSE_PRICE_POP_UP = new Locator("CLOSE_PRICE_POP_UP", new By.ByXPath("//button[contains(@class, 'Modal_modal_close')]"));
    private static final Locator PRICE_CHANGED_CONFIRMATION = new Locator("PRICE_CHANGED_CONFIRMATION", new By.ByXPath("//div[contains(@class, 'Figure_footer')]//button"));
    private static final Locator CHANGE_SEAT = new Locator("CHANGE_SEAT", new By.ByXPath("//button[.//span[normalize-space()='Change seat' or normalize-space()='Ubah kursi']]"));
    private static final Locator SAVE_SEAT = new Locator("SAVE_SEAT", new By.ByXPath("//button[.//span[normalize-space()='Save seat' or normalize-space()='Simpan kursi']]"));
    private static final Locator RETURN_FROM = new Locator("RETURN_FROM", new By.ByXPath("(//ol[contains(@class, 'ProgressStepVerticalDots_progress_step_vertical_dots')]//li[1]//div[contains(@class,'Text_variant_highEmphasis')])[2]"));
    private static final Locator RETURN_TO = new Locator("RETURN_TO", new By.ByXPath("(//ol[contains(@class, 'ProgressStepVerticalDots_progress_step_vertical_dots')]//li[2]//div[contains(@class,'Text_variant_highEmphasis')])[2]"));
    private static final Locator PASSENGER_NAME_BF2 = new Locator("PASSENGER_NAME_BF2", new By.ByXPath("//div[contains(@class, 'SeatSection_passenger')]//span[contains(@class, 'bold')]"));
    private static final Locator LOG_IN_NOW_BUTTON = new Locator("LOG_IN_NOW_BUTTON", new By.ByXPath("//div[text()=\"Log in now\" or text()=\"Log in sekarang\"]"));
    private static final Locator REGISTER_NOW = new Locator("REGISTER_NOW", new By.ByXPath("//*[contains(text(), \"Register now!\") or contains(text(), \"Daftar, yuk!\")]"));
    private static final Locator PRICE_DETAILS_CLOSE_BUTTON = new Locator("PRICE_DETAILS_CLOSE_BUTTON", By.xpath("//button[@data-testid='modal-close-button']"));

    public TrainBookingFormWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TrainBookingFormWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new TrainBookingFormDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new TrainBookingFormMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract String getInitialPrice();

    public abstract void fillBookingForm(String username, String phoneNumber, String email, String id);

    public abstract void fillPassengerDetail(String id);

    @Deprecated
    public abstract void fillPassengerDetailInfant(String name, String id);

    public abstract void fillPassengerDetailAdult(int adults);

    public abstract void fillPassengerDetailInfant(int adults, int infants);

    public abstract void fillId(int index);

    public abstract String getFinalPrice();

    public abstract void continueToPay();

    public String getDepartureArrivalTime(String type) {
        WebElement dateForm = driverTools.element(TIME_LOCATOR).orElseThrow();
        if (type == "departure") {
            return driverTools.getText(dateForm).split(" - ")[0];
        } else {
            return driverTools.getText(dateForm).split(" - ")[1];
        }
    }

    public abstract String getPassengerName();

    public void proceedToPaymentPage() {
        try{
            driverTools.waitForElementToAppear(CONTINUE_TO_PAY_BUTTON);
            driverTools.click(CONTINUE_TO_PAY_BUTTON);
            driverTools.waitForElementToPresence(CONTINUE_TO_PAY_BUTTON_DIALOG, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
        }catch(Exception e){
            logger.log(e.getMessage());
            driverTools.waitForElementToAppear(CONTINUE_TO_PAY_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToAppear(CONTINUE_TO_PAY_BUTTON_DIALOG).ifPresent(driverTools::clickWithJavascriptExecutor);
        }

    }

    public void continueToBookingForm2() {
        if(!driverTools.waitForElementToPresence(CONTINUE_BUTTON, 5).isPresent()){
            driverTools.scrollUntilElementFound(CONTINUE_BUTTON);
        }
        driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON);
        driverTools.waitForElementToDisappear(CONTINUE_BUTTON);
        logger.log("Navigated to Booking Form 2");
    }

    public boolean isBookingFormPriceShownIdr() {
        WebElement bookingFormPriceElement = driverTools.waitForElementToAppear(TRAIN_CARD_PRICE_BOOKING_FORM).orElseThrow();
        String bookingFormPriceValue = driverTools.getText(bookingFormPriceElement);
        String bookingFromPriceCurrency = CoreFunctions.getSectionOfString(bookingFormPriceValue, " ", 0).trim();
        logger.log("Booking form price currency: " + bookingFromPriceCurrency);
        return bookingFromPriceCurrency.equalsIgnoreCase("IDR");
    }

    public void fillContactDetails(String fullName, String phoneNumber, String emailID) {
        driverTools.waitForElementToAppear(CONTACT_CARD, 30);
        driverTools.scrollIntoWebView(CONTACT_CARD);
        driverTools.clickWithJavascriptExecutor(SALUTATION);
        WebElement fullNameInput = driverTools.waitForElementToAppear(INPUT_NAME).orElseThrow();
        if (!driverTools.getValue(fullNameInput).equals(fullName)) {
            driverTools.clearInput(fullNameInput);
            driverTools.sendKeys(fullNameInput, fullName);
        }
        WebElement phoneNumberInput = driverTools.waitForElementToAppear(PHONE_NUMBER_INPUT).orElseThrow();
        if (!driverTools.getValue(phoneNumberInput).equals(phoneNumber)) {
            driverTools.clearInput(phoneNumberInput);
            driverTools.sendKeys(phoneNumberInput, phoneNumber);
        }
        if (!driverTools.waitForElementToPresence(EMAIL_INPUT, 10).isPresent()) {
            driverTools.scrollUntilElementFound(EMAIL_INPUT);
        }
        driverTools.scrollIntoWebView(EMAIL_INPUT);
        WebElement emailInput = driverTools.waitForElementToAppear(EMAIL_INPUT, 20).orElseThrow();
        if (!driverTools.isElementDisplayed(EMAIL_DISABLED)) {
            if (!driverTools.getValue(emailInput).equals(emailID)) {
                driverTools.clearInput(emailInput);
                driverTools.sendKeys(emailInput, emailID);
            }
        }
    }

    public Locator getPassengerButton(int index) {
        String xpath = "//*[contains(text(),'" + index + "') and (contains(text(),'Passenger') or contains(text(),'Penumpang'))]";
        return new Locator("PASSENGER_BUTTON_" + index, By.xpath(xpath));
    }

    public void toggleSameAsContact() {
        driverTools.waitForElementToBeClickable(TOGGLE_BTN);
        driverTools.clickWithJavascriptExecutor(TOGGLE_BTN);
        fillId(1);
    }

    public abstract Locator getIdNumberInput(int passengerIndex);

    public abstract Locator getTitleLocatorByIndexAdult(int passengerIndex);

    public abstract Locator getTitleLocatorByIndexInfant(int passengerIndex);

    public abstract Locator getNameLocatorbyIndex(int idx);

    public void clickContinue() {
        driverTools.waitForElementToAppear(CONTINUE_BTN);
        driverTools.clickWithJavascriptExecutor(CONTINUE_BTN);
    }

    public void addInsurance() {
        driverTools.waitForElementToAppear(AUTOSELECTED_INSURANCE);
        if (!driverTools.isElementDisplayed(AUTOSELECTED_INSURANCE)) {
            driverTools.waitForElementToAppear(ADD_INSURANCE);
            driverTools.clickWithJavascriptExecutor(ADD_INSURANCE);
        }
    }

    public String verifyDepartureOrigin() {
        return driverTools.getText(DEPARTURE_FROM).orElseThrow();
    }

    public String verifyDepartureDestination() {
        return driverTools.getText(DEPARTURE_TO).orElseThrow();
    }

    public String verifyReturnOrigin() {
        return driverTools.getText(RETURN_FROM).orElseThrow();
    }

    public String verifyReturnDestination() {
        return driverTools.getText(RETURN_TO).orElseThrow();
    }

    public abstract void checkPriceBtn();

    public abstract boolean isPriceDetailsDisplayed();

    public String checkPrice(int adultCount) {
        driverTools.waitForElementToAppear(getPriceOfBooking(adultCount), 5);
        String allText = driverTools.getText(getPriceOfBooking(adultCount)).orElseThrow();
        Optional<Integer> numericPrice = CoreFunctions.getIntFromString(allText);
        int totalBookingCost = numericPrice.orElseThrow();
        int totalCostOnBKP = totalBookingCost / adultCount;
        return String.valueOf(totalCostOnBKP);
    }

    private Locator getPriceOfBooking(int adultCount) {
        return new Locator("PRICE_OF_BOOKING", new By.ByXPath("//*[@data-testid='collapse-container']//*[contains(@class, 'PriceSummaryItem_label') and contains(text(),'x"+adultCount+"')]//following-sibling::*[contains(@class,'PriceSummaryItem_price')]"));
    }

    public abstract void closePricePopUp();

    public void saveSeat() {
        driverTools.waitForElementToAppear(CHANGE_SEAT, 5);
        driverTools.clickWithJavascriptExecutor(CHANGE_SEAT);
        for (int i = 1; i <= 2; i++) {
            driverTools.waitForElementToDisappear(SAVE_SEAT, 5);

            do {
                driverTools.waitForElementToAppear(getCardSeat(i), 5);
                driverTools.clickWithJavascriptExecutor(getCardSeat(i));
            } while (!driverTools.isElementDisplayed(SAVE_SEAT));

            driverTools.clickWithJavascriptExecutor(SAVE_SEAT);
            driverTools.waitForElementToDisappear(SAVE_SEAT, 10);
        }
    }

    private Locator getCardSeat(int idx) {
        String index = (idx == 1) ? "1" : "last()";
        String xpath = String.format("(//div[contains(@class,'Card_card_container') and contains(@class,'SeatSection_card')]//button[contains(@class,'SeatSection_change_seat')])[%s]", index);
        return new Locator("CARD_SEAT_" + idx, By.xpath(xpath));
    }

    public void goToPaymentPage() {
        driverTools.waitForElementToBeClickable(CONFIRM_BOOKING);
        driverTools.click(CONFIRM_BOOKING);
        try {
            driverTools.waitForElementToAppear(PRICE_CHANGED_CONFIRMATION, 10);
            if (driverTools.isElementDisplayed(PRICE_CHANGED_CONFIRMATION)) {
                driverTools.click(PRICE_CHANGED_CONFIRMATION);
            }
        } catch (Exception ignored) {}
        logger.log("Navigated to Payment Page");
    }

    public String getPassengerNameOnBF2() {
        WebElement passengerNameElement = driverTools.waitForElementToAppear(PASSENGER_NAME_BF2).orElseThrow();
        String passengerFullName = passengerNameElement.getText().trim();
        String passengerNameOnly = passengerFullName.replaceAll("(?i)^\\s*\\d*\\.?\\s*(mr|mrs|ms|tuan|nyonya|nona)\\.?\\s+", "").trim();
        return passengerNameOnly;
    }

    public void clickLogInNowButton(){
        driverTools.waitForElementToAppear(LOG_IN_NOW_BUTTON);
        driverTools.clickWithJavascriptExecutor(LOG_IN_NOW_BUTTON);
    }

    public void clickRegisterNowButton() {
        driverTools.waitForElementToAppear(REGISTER_NOW) ;
        driverTools.clickWithJavascriptExecutor(REGISTER_NOW);
    }

    public Boolean isuserAbleToContinue() {
        return driverTools.waitForElementToAppear(CONTINUE_BTN).isPresent() ;
    }

    public void addNotAutoSelectedInsurance() {
        driverTools.waitForElementToAppear(ADD_LAST_INSURANCE);
        driverTools.clickWithJavascriptExecutor(ADD_LAST_INSURANCE);
    }

    public String getPriceDetails() {
        driverTools.waitForElementToAppear(PRICE_OF_BOOKING);
        WebElement bookingPrice = driverTools.waitForElementToAppear(PRICE_OF_BOOKING).orElseThrow();
        String bookingPriceText = driverTools.getText(bookingPrice);
        return bookingPriceText.replaceAll("[^0-9]", "");
    }
}