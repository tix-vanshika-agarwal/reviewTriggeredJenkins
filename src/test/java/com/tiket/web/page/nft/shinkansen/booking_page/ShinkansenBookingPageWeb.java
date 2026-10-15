package com.tiket.web.page.nft.shinkansen.booking_page;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Optional;

public abstract class ShinkansenBookingPageWeb<T extends WebDriver> extends WebPage<T> {

    public ShinkansenBookingPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> ShinkansenBookingPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new ShinkansenBookingPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new ShinkansenBookingPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    // NOTE: Locators reused from Whoosh as placeholders.
    // Replace with dedicated Shinkansen UI locators once available.
    private static final Locator CONTINUE_BTN = new Locator("CONTINUE_BTN", new By.ByXPath("//button[contains(@class, 'BookingFooter_button')]"));
    private static final Locator INPUT_NAME = new Locator("INPUT_NAME", new By.ByXPath("//div[contains(@class,'Card_card_container')] //input[@id='full-name-as-per-passport']"));
    private static final Locator CONTACT_CARD = new Locator("CONTACT_CARD", new By.ByXPath("//div[contains(@class, 'Card_card_container') and .//div[contains(@class, 'ContactForm_wrapper')]]"));
    private static final Locator SALUTATION = new Locator("SALUTATION", new By.ByXPath("(//label[contains(@class,'Radio_radio') and (.//span[normalize-space()='Mr.'] or .//span[normalize-space()='Tuan'])])[1]"));
    private static final Locator PHONE_NUMBER_INPUT = new Locator("PHONE_NUMBER_INPUT", new By.ByXPath("//*[@id='phone-number']"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", new By.ByXPath("//input[@id='email-address' or @id='email']"));
    private static final Locator EMAIL_DISABLED = new Locator("EMAIL_DISABLED", new By.ByXPath("//*[contains(@class, 'styles_state_disabled')]"));
    private static final Locator SAVE_SEAT = new Locator("SAVE_SEAT", new By.ByXPath("//button[contains(@class, 'Button_variant_primary')]//span[normalize-space(text())='Save seat' or normalize-space(text())='Simpan kursi']"));
    private static final Locator ADD_INSURANCE = new Locator("ADD_INSURANCE", new By.ByXPath("(//button[normalize-space(text())='Tambahkan' or //span[normalize-space(text())='Add']])[last()-1]"));
    private static final Locator CONTINUE_TO_PAY = new Locator("CONTINUE_TO_PAY", new By.ByXPath("//button[contains(@class, 'Button_variant_primary') and contains(@class, 'Button_full_width')]"));
    private static final Locator CONFIRM_BOOKING = new Locator("CONFIRM_BOOKING", new By.ByXPath("(//button[contains(@class, 'Button_variant_primary') and contains(@class, 'Button_full_width')])[2]"));
    private static final Locator PRICE_DETAIL_BTN = new Locator("PRICE_DETAIL_BTN", new By.ByXPath("//div[contains(@class, 'BookingReviewCards_total_price')]//span[contains(text(), 'IDR')]"));
    private static final Locator PRICE_DETAIL_HEADING = new Locator("PRICE_DETAIL_HEADING", new By.ByXPath("//h2[contains(@class, 'Text_text') and contains(@class, 'Text_size_h3')]"));
    private static final Locator PRICE_OF_BOOKING = new Locator("PRICE_OF_BOOKING", new By.ByXPath("(//div[contains(@class, 'PriceContent_item_price_box')]//div[contains(text(), 'IDR')])[1]"));
    private static final Locator PRICE_ON_PAYMENT_PAGE = new Locator("PRICE_ON_PAYMENT_PAGE", new By.ByXPath("//*[contains(@class, 'PriceSummary_more_icon')]"));
    public static final Locator TOGGLE_BTN = new Locator("TOGGLE_BTN", new By.ByXPath("//div[contains(@class, 'Toggle_knob')]"));
    public static final Locator SAVE_BTN = new Locator("SAVE_BTN", new By.ByXPath("//button[contains(@class, 'Button_variant_primary') and (normalize-space(text())='Simpan' or normalize-space(text())='Save')]"));
    public static final Locator OPEN_PRICE_ON_COMPLETE_BOOKING = new Locator("OPEN_PRICE_ON_COMPLETE_BOOKING", new By.ByXPath("(//div[contains(@class, 'AnimatedChevronIcon_icon_chevron')])"));
    private static final Locator TOTAL_TO_PAY_AFTER_TAX_PRICE = new Locator("TOTAL_TO_PAY_AFTER_TAX_PRICE", new By.ByXPath("//div[contains(@class,'PriceSummaryItem_container')][contains(@class,'PriceSummary_total_price_item')][.//*[contains(@class,'PriceSummaryItem_label') and (contains(normalize-space(.),'Total to pay') or contains(normalize-space(.),'Total bayar'))]]//span[contains(@class,'PriceSummaryItem_price')]"));
    private static final Locator CONTACT_DETAILS = new Locator("CONTACT_DETAILS", new By.ByXPath("//span[contains(normalize-space(),'Contact Details') or contains(normalize-space(),'Detail Kontak')]"));
    public void clickContinue() {
        driverTools.waitForElementToAppear(CONTINUE_BTN);
        driverTools.clickWithJavascriptExecutor(CONTINUE_BTN);
        driverTools.waitForElementToDisappear(CONTINUE_BTN);
    }

    public void clickAnimatedChevronIcon() {
        driverTools.waitForElementToAppear(OPEN_PRICE_ON_COMPLETE_BOOKING);
        driverTools.clickWithJavascriptExecutor(OPEN_PRICE_ON_COMPLETE_BOOKING);
    }

    public abstract String verifiesDepartureShinkansen(String tripType);

    public abstract String verifiesReturnShinkansen();

    public abstract String verifyDepartureOrigin(String tripType);

    public abstract String verifyDepartureDestination(String tripType);

    public String getTotalToPayAfterTaxPrice() {
        driverTools.swipeToElement(TOTAL_TO_PAY_AFTER_TAX_PRICE, Swipe.UP, 10);
        driverTools.waitForElementToAppear(TOTAL_TO_PAY_AFTER_TAX_PRICE);
        String priceText = driverTools.getText(TOTAL_TO_PAY_AFTER_TAX_PRICE).orElseThrow().trim();
        int amount = CoreFunctions.getIntFromString(priceText).orElseThrow();
        logger.log("Total to pay (after tax): " + amount);
        return String.valueOf(amount);
    }

    public abstract String verifyReturnOrigin();

    public abstract String verifyReturnDestination();

    public void fillContactDetails(String fullName, String phoneNumber, String emailID) {
        driverTools.waitForElementToAppear(CONTACT_CARD, 5);
        driverTools.click(SALUTATION);
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
        WebElement emailInput = driverTools.waitForElementToAppear(EMAIL_INPUT).orElseThrow();
        if (!driverTools.isElementDisplayed(EMAIL_DISABLED)) {
            if (!driverTools.getValue(emailInput).equals(emailID)) {
                driverTools.clearInput(emailInput);
                driverTools.sendKeys(emailInput, emailID);
            }
        }
        driverTools.waitForElementToPresence(SAVE_SEAT, 5);
        driverTools.waitForElementToAppear(CONTACT_DETAILS,2).ifPresent(driverTools::click);
    }

    public abstract List<String> fillPassengerDetailAdult(int adults) throws InterruptedException;

    public abstract List<String> fillPassengerDetailInfant(int adults, int infants);

    public void saveSeat() {
        for (int i = 1; i <= 2; i++) {
            Locator ithCardSeat = getCardSeat(i);
            driverTools.waitForElementToPresence(ithCardSeat);
            driverTools.waitForElementToBeClickable(ithCardSeat);
            driverTools.clickWithJavascriptExecutor(ithCardSeat);

            driverTools.waitForElementToAppear(SAVE_SEAT);
            driverTools.waitForElementToBeClickable(SAVE_SEAT);
            driverTools.clickWithJavascriptExecutor(SAVE_SEAT);
            driverTools.dummyWait(1);
        }
    }

    private Locator getCardSeat(int idx) {
        String xpath = String.format("(//div[contains(@class, 'Card_card_container') and contains(@class, 'SeatSection_card')])[%d]//button[contains(@class, 'SeatSection_change_seat')]", idx);
        return new Locator("CARD_SEAT_" + idx, By.xpath(xpath));
    }

    public void addInsurance() {
        driverTools.waitForElementToBeClickable(ADD_INSURANCE);
        driverTools.clickWithJavascriptExecutor(ADD_INSURANCE);
    }

    public void continueToPay() {
        driverTools.waitForElementToPresence(CONTINUE_BTN, 3);
        driverTools.waitForElementToAppear(CONTINUE_TO_PAY);
        driverTools.waitForElementToBeClickable(CONTINUE_TO_PAY);
        driverTools.clickWithJavascriptExecutor(CONTINUE_TO_PAY);
    }

    public void goToPaymentPage() {
        driverTools.waitForElementToBeClickable(CONFIRM_BOOKING);
        driverTools.clickWithJavascriptExecutor(CONFIRM_BOOKING);
    }

    public void checkPriceBtn() {
        driverTools.waitForElementToAppear(PRICE_DETAIL_BTN);
        driverTools.clickWithJavascriptExecutor(PRICE_DETAIL_BTN);
    }

    public boolean isPriceDetailsDisplayed() {
        driverTools.waitForElementToAppear(PRICE_DETAIL_HEADING, 3);
        return driverTools.isElementDisplayed(PRICE_DETAIL_HEADING);
    }

    public String checkPrice(int adultCount) {
        String allText = driverTools.getText(PRICE_OF_BOOKING).orElseThrow();
        Optional<Integer> numericPrice = CoreFunctions.getIntFromString(allText);
        int totalBookingCost = numericPrice.orElseThrow();
        int totalCostOnBKP = totalBookingCost / adultCount;
        return String.valueOf(totalCostOnBKP);
    }

    public void clickPriceBtn() {
        driverTools.waitForElementToAppear(PRICE_ON_PAYMENT_PAGE);
        driverTools.click(PRICE_ON_PAYMENT_PAGE);
    }

    public abstract List<String> getPassengerIDs(int adultCount, int infantCount);
}
