package com.tiket.web.page.nft.whoosh.booking_page;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import java.util.List;
import java.util.Optional;

public abstract class WhooshBookingPageWeb<T extends WebDriver> extends WebPage<T> {

    public WhooshBookingPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> WhooshBookingPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new WhooshBookingPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new WhooshBookingPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator CONTINUE_BTN = new Locator("CONTINUE_BTN", new By.ByXPath("//button[contains(@class, 'booking_footer_button')]"));
    private static final Locator INPUT_NAME = new Locator("INPUT_NAME", new By.ByXPath("//div[contains(@class,'Card_card_container')] //input[@id='full-name-according-to-idpassport']"));
    private static final Locator CONTACT_CARD = new Locator("CONTACT_CARD", new By.ByXPath("//div[contains(@class, 'Card_card_container') and .//div[contains(@class, 'ContactForm_wrapper')]]"));
    private static final Locator SALUTATION = new Locator("SALUTATION", new By.ByXPath("(//label[contains(@class,'Radio_radio') and (.//span[normalize-space()='Mr.'] or .//span[normalize-space()='Tuan'])])[1]"));
    private static final Locator PHONE_NUMBER_INPUT = new Locator("PHONE_NUMBER_INPUT", new By.ByXPath("//input[@id='nomor-ponsel' or @id='mobile-number']"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", new By.ByXPath("//input[@id='email-address' or @id='email']"));
    private static final Locator EMAIL_DISABLED = new Locator("EMAIL_DISABLED", new By.ByXPath("//*[contains(@class, 'styles_state_disabled')]"));
    private static final Locator SAVE_SEAT = new Locator("SAVE_SEAT", new By.ByXPath("//button[contains(@class, 'Button_variant_primary')]//span[normalize-space(text())='Save seat' or normalize-space(text())='Simpan kursi']"));
    private static final Locator INSURANCE_CARD_ADD = new Locator("INSURANCE_CARD_ADD", new By.ByXPath("(//*[contains(@class,'Card_card_container') and (.//*[contains(text(),'Asuransi') or contains(text(),'Insurance') or contains(text(),'Proteksi') or contains(text(),'Protection')])]//button[normalize-space(.)='Tambahkan' or normalize-space(.)='Add'])[1]"));
    private static final Locator INSURANCE_CARD_ADDED = new Locator("INSURANCE_CARD_ADDED", new By.ByXPath("(//*[contains(@class,'Card_card_container') and (.//*[contains(text(),'Asuransi') or contains(text(),'Insurance') or contains(text(),'Proteksi') or contains(text(),'Protection')])]//button[normalize-space(.)='Batalkan' or normalize-space(.)='Cancel' or normalize-space(.)='Hapus' or normalize-space(.)='Remove'])[1]"));
    private static final Locator ADD_INSURANCE = new Locator("ADD_INSURANCE", new By.ByXPath("(//button[normalize-space(.)='Tambahkan' or normalize-space(.)='Add'])[last()-1]"));
    private static final Locator CONTINUE_TO_PAY = new Locator("CONTINUE_TO_PAY", new By.ByXPath("//button[contains(@class, 'Button_variant_primary') and contains(@class, 'Button_full_width')]"));
    private static final Locator CONFIRM_BOOKING = new Locator("CONFIRM_BOOKING", new By.ByXPath("(//button[contains(@class, 'Button_variant_primary') and contains(@class, 'Button_full_width')])[last()]"));
    private static final Locator PRICE_DETAIL_BTN = new Locator("PRICE_DETAIL_BTN", new By.ByXPath("//div[contains(@class, 'BookingReviewCards_total_price')]//span[contains(text(), 'IDR')]"));
    private static final Locator PRICE_DETAIL_HEADING = new Locator("PRICE_DETAIL_HEADING", new By.ByXPath("//h2[contains(@class, 'Text_text') and contains(@class, 'Text_size_h3')]"));
    private static final Locator PRICE_OF_BOOKING = new Locator("PRICE_OF_BOOKING", new By.ByXPath("(//div[contains(@class, 'PriceContent_item_price_box')]//div[contains(text(), 'IDR')])[1]"));
    private static final Locator PRICE_ON_PAYMENT_PAGE = new Locator("PRICE_ON_PAYMENT_PAGE", new By.ByXPath("//*[contains(@class, 'PriceSummary_more_icon')]"));
    private static final Locator STATION_HEADING = new Locator("STATION_HEADING", new By.ByXPath("(//div[contains(@class,'PriceDetailV4_price_details_v4_wrapper')]//h3[contains(@class,'PriceDetailV4_price_title')])[2]"));
    private static final Locator BASE_PRICE = new Locator("BASE_PRICE", new By.ByXPath("(//div[@data-testid='collapse-container']   //ul[contains(@class,'price_list')]   //li[contains(@class,'price_list_item')]   //div/*[last()])[2]"));
    private static final Locator CONVENIENCE_PRICE_TEXT = new Locator("CONVENIENCE_PRICE_TEXT", new By.ByXPath("(//div[contains(@class,'PriceDetailV4_detail_content_wrapper')]   [.//span[contains(@class,'PriceDetailV4_content_title')][normalize-space(.)='Convenience Fee' or normalize-space(.)='Biaya Pemesanan']]   /span[contains(@class,'Text_text')][not(contains(@class,'PriceDetailV4_content_title'))])[2]"));
    private static final Locator SERVICE_PRICE_TEXT = new Locator("SERVICE_PRICE_TEXT", new By.ByXPath("(//div[contains(@class,'PriceDetailV4_detail_content_wrapper')] [.//span[contains(@class,'PriceDetailV4_content_title')][normalize-space(.)='Service Fee' or normalize-space(.)='Biaya Jasa']] /span[position()=2])[2]"));
    private static final Locator TOTAL_PRICE_TEXT = new Locator("TOTAL_PRICE_TEXT", new By.ByXPath("(//div[contains(@class,'PriceDetailV4_total')]   /span[normalize-space(.)='Total']   /following-sibling::span[1])[2]"));
    private static final Locator CONVENIENCE_PRICE = new Locator("CONVENIENCE_PRICE", new By.ByXPath("(//span[contains(@class, 'Text_size_b2') and starts-with(text(), 'IDR')])[6]"));
    private static final Locator SERVICE_PRICE = new Locator("SERVICE_PRICE", new By.ByXPath("(//span[contains(@class, 'Text_size_b2') and starts-with(text(), 'IDR')])[7]"));
    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", new By.ByXPath("(//span[contains(@class, 'Text_size_b2') and starts-with(text(), 'IDR')])[8]"));
    private static final Locator BASE_PRICE_DROP_DOWN = new Locator("BASE_PRICE_DROP_DOWN", new By.ByXPath("(//*[@role='button' and contains(@class,'PriceDetailV4_accordion_button')]//*[local-name()='svg'][@data-testid='accordion-icon'])[2]"));
    public static final Locator TOGGLE_BTN = new Locator("TOGGLE_BTN", new By.ByXPath("//div[contains(@class, 'Toggle_knob')]"));
    public static final Locator SAVE_BTN = new Locator("SAVE_BTN", new By.ByXPath("//button[contains(@class, 'Button_variant_primary') and (normalize-space(text())='Simpan' or normalize-space(text())='Save')]"));
    public static final Locator OPEN_PRICE_ON_COMPLETE_BOOKING = new Locator("OPEN_PRICE_ON_COMPLETE_BOOKING", new By.ByXPath("(//div[contains(@class, 'AnimatedChevronIcon_icon_chevron')])"));
    private static final Locator TOTAL_TO_PAY_AFTER_TAX_PRICE = new Locator("TOTAL_TO_PAY_AFTER_TAX_PRICE", new By.ByXPath("//div[contains(@class,'PriceSummaryItem_container')][contains(@class,'PriceSummary_total_price_item')][.//*[contains(@class,'PriceSummaryItem_label') and (contains(normalize-space(.),'Total to pay') or contains(normalize-space(.),'Total bayar'))]]//span[contains(@class,'PriceSummaryItem_price')]"));


    public void clickContinue() {
        driverTools.waitForElementToAppear(CONTINUE_BTN);
        driverTools.clickWithJavascriptExecutor(CONTINUE_BTN);
        driverTools.waitForElementToDisappear(CONTINUE_BTN);
    }

    public void ClickAnimatedChevronIcon_icon_chevron() {
        driverTools.waitForElementToAppear(OPEN_PRICE_ON_COMPLETE_BOOKING);
        driverTools.clickWithJavascriptExecutor(OPEN_PRICE_ON_COMPLETE_BOOKING);
    }

    public abstract String verifiesDepartureWhoosh(String tripType);

    public abstract String verifiesReturnWhoosh();

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
        if (driverTools.waitForElementToPresence(INSURANCE_CARD_ADDED, 3).isPresent()) {
            return; // insurance already selected
        }
        Locator addButton = driverTools.waitForElementToPresence(INSURANCE_CARD_ADD, 10).isPresent()
                ? INSURANCE_CARD_ADD : ADD_INSURANCE;
        driverTools.scrollIntoWebView(addButton);
        driverTools.waitForElementToBeClickable(addButton);
        driverTools.clickWithJavascriptExecutor(addButton);
    }

    public void continueToPay() {
        driverTools.waitForElementToPresence(CONTINUE_BTN,5);

        WebElement continueButton = driverTools.waitForElementToAppear(CONTINUE_TO_PAY).orElseThrow();
        driverTools.waitForElementToBeClickable(CONTINUE_TO_PAY);
        driverTools.clickWithJavascriptExecutor(CONTINUE_TO_PAY);
        // Do not fire the next click while the cart is still being updated: wait until the click has taken
        // effect, i.e. the next step is rendered (confirm button added) or the page moved on.
        waitForNextStep(continueButton);
    }

    public void goToPaymentPage() {
        driverTools.waitForElementToAppear(CONFIRM_BOOKING).orElseThrow();
        WebElement confirmButton = driverTools.waitForElementToBeClickable(CONFIRM_BOOKING).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CONFIRM_BOOKING);
        driverTools.waitForElementToDisappear(confirmButton, 30);
    }

    private void waitForNextStep(WebElement clickedButton) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15)).until(d -> {
                if (d.findElements(CONTINUE_TO_PAY.by()).size() >= 2) {
                    return true;
                }
                try {
                    return !clickedButton.isDisplayed();
                } catch (StaleElementReferenceException e) {
                    return true;
                }
            });
        } catch (TimeoutException e) {
            logger.softWarning("Next step after 'continue' was not rendered in 15 seconds");
        }
    }

    public void checkPriceBtn(){
        driverTools.waitForElementToAppear(PRICE_DETAIL_BTN);
        driverTools.clickWithJavascriptExecutor(PRICE_DETAIL_BTN);
    }

    public boolean isPriceDetailsDisplayed(){
        driverTools.waitForElementToAppear(PRICE_DETAIL_HEADING,3);
        return driverTools.isElementDisplayed(PRICE_DETAIL_HEADING);
    }

    public String checkPrice(int adultCount){
        String allText = driverTools.getText(PRICE_OF_BOOKING).orElseThrow();
        Optional<Integer> numericPrice = CoreFunctions.getIntFromString(allText);
        int totalBookingCost = numericPrice.orElseThrow();
        int totalCostOnBKP = totalBookingCost/adultCount;
        return String.valueOf((totalCostOnBKP));
    }

    public void clickPriceBtn(){
        driverTools.waitForElementToAppear(PRICE_ON_PAYMENT_PAGE);
        driverTools.click(PRICE_ON_PAYMENT_PAGE);
    }

    public String verifyOrigin() {
        driverTools.waitForElementToAppear(STATION_HEADING);
        String fullText = driverTools.getText(STATION_HEADING).orElseThrow();
        logger.log("Full Text: '" + fullText + "'");

        String firstCity = fullText.split("→")[0].trim();
        return firstCity;
    }

    public String verifyDestination() {
        driverTools.waitForElementToAppear(STATION_HEADING);
        String fullText = driverTools.getText(STATION_HEADING).orElseThrow();
        logger.log("Full Text: '" + fullText + "'");

        String firstCity = fullText.split("→")[1].trim();
        return firstCity;
    }

    public String checkPriceOnPaymentPage(int adultCount){
        driverTools.waitForElementToAppear(BASE_PRICE_DROP_DOWN);
        driverTools.clickElementByCoordinateDWeb(BASE_PRICE_DROP_DOWN);
        driverTools.waitForElementToAppear(BASE_PRICE,3);
        String allText = driverTools.getText(BASE_PRICE).orElseThrow();
        Optional<Integer> numericPrice = CoreFunctions.getIntFromString(allText);
        int totalBookingCost = numericPrice.orElseThrow() ;
        int totalCostOnBKP = totalBookingCost/adultCount;
        logger.log(String.valueOf((totalCostOnBKP)));
        return String.valueOf((totalCostOnBKP));
    }

    public Boolean checkConvenienceFee(){
        driverTools.waitForElementToAppear(CONVENIENCE_PRICE_TEXT);
        return driverTools.isElementPresent(CONVENIENCE_PRICE_TEXT);
    }

    public String getConvenienceFee(){
        String allText = driverTools.getText(CONVENIENCE_PRICE).orElseThrow();
        Optional<Integer> numericPrice = CoreFunctions.getIntFromString(allText);
        int totalConvenienceFee = numericPrice.orElseThrow();
        logger.log(String.valueOf(totalConvenienceFee));
        return String.valueOf(totalConvenienceFee);
    }

    public Boolean checkServiceFee(){
        driverTools.waitForElementToAppear(SERVICE_PRICE_TEXT);
        return driverTools.isElementPresent(SERVICE_PRICE_TEXT);
    }

    public String getServiceFee(){
        String allText = driverTools.getText(SERVICE_PRICE).orElseThrow();
        Optional<Integer> numericPrice = CoreFunctions.getIntFromString(allText);
        int totalServiceFee = numericPrice.orElseThrow() ;
        logger.log(String.valueOf(totalServiceFee));
        return String.valueOf(totalServiceFee);
    }

    public String getTotalPrice(){
        driverTools.waitForElementToAppear(TOTAL_PRICE_TEXT);
        if(!driverTools.isElementPresent(TOTAL_PRICE_TEXT)) return "";
        String allText = driverTools.getText(TOTAL_PRICE_TEXT).orElseThrow();
        Optional<Integer> numericPrice = CoreFunctions.getIntFromString(allText);
        int totalPrice = numericPrice.orElseThrow();
        logger.log(String.valueOf(totalPrice));
        return String.valueOf(totalPrice);
    }

    public String getOverallPrice(String base){
        String conFee = driverTools.getText(CONVENIENCE_PRICE_TEXT).orElseThrow();
        String numeric = conFee.replaceAll("[^0-9]", "");
        int convenienceFees = Integer.parseInt(numeric);
        String serviceFee = driverTools.getText(SERVICE_PRICE_TEXT).orElseThrow();
        numeric = serviceFee.replaceAll("[^0-9]", "");
        int serviceFees = Integer.parseInt(numeric);
        int basePrice = Integer.parseInt(base);

        return String.valueOf(convenienceFees+serviceFees+basePrice);
    }

    public abstract List<String> getPassengerIDs(int adultCount, int infantCount);
}
