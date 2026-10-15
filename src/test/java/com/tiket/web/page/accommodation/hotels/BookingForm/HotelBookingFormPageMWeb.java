package com.tiket.web.page.accommodation.hotels.BookingForm;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import static org.hamcrest.core.Is.is;
import java.util.List;


public class HotelBookingFormPageMWeb <T extends WebDriver> extends HotelBookingFormPageWeb<T> {

    public HotelBookingFormPageMWeb(Session<T> session) {
        super(session);
    }

    private static final Locator SAVE_GUEST_BUTTON_MWEB = new Locator("HOTEL_SAVE_GUEST_BUTTON", By.xpath("//button[contains(@class,'Button_button') and .//span[normalize-space()='Save' or normalize-space()='Simpan']]"));
    private static final Locator CONTACT_CARD = new Locator("CONTACT_CARD", new By.ByXPath("(//button[contains(@class, 'GroupFieldBody_button_container')])[1]"));
    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", new By.ByXPath("//div[contains(@class,'PriceSummary_total_price_container')]/span[last()]"));
    private static final Locator CONTACT_DETAILS_CONTAINER = new Locator("CONTACT_DETAILS_CONTAINER", new By.ByXPath("(//button[contains(@class, 'GroupFieldBody_button_container')])[1]"));
    private static final Locator CONTACT_DETAILS_SAVE = new Locator("CONTACT_DETAILS_SAVE", new By.ByXPath("//button[contains(@class,'ContactDetail_button_save') and (span[contains(text(),'Simpan')] or span[contains(text(),'Save')])]"));
    private static final Locator PRICE_TEXT_MWEB = new Locator("PRICE_TEXT_MWEB", new By.ByXPath("//p[contains(@class, 'PriceFooter_final_price_text')]"));
    private static final Locator SAVE_GUEST_BUTTON = new Locator("SAVE_GUEST_BUTTON", new By.ByXPath("//button[contains(@class, 'Button_button') and (text()='Save' or text()='Simpan')]"));
    private static final Locator SPECIAL_REQUEST_SAVE = new Locator("SPECIAL_REQUEST_SAVE", new By.ByXPath("//button[contains(@class,'Button_button') and .//span[normalize-space()='Save' or normalize-space()='Simpan']]"));
    private static final Locator SUBTOTAL_AREA=new Locator("SUBTOTAL_AREA",new By.ByXPath("(//div[contains(@class,'PriceSummary_accordion_title_section')])[1]"));
    public static final Locator PRICE_TOTAL_BOOKING_PAGE1 = new Locator("PRICE_TOTAL_BOOKING_PAGE1", new By.ByXPath("//p[contains(@class,'PriceFooter_final_price_text')]"));

    public void fillGuestDetailsCombinedRandom(int guests) {
        Locator guestNameButton = getGuestNameButtonLocator(1);
        driverTools.waitForElementToAppear(guestNameButton, 5);
        driverTools.clickWithJavascriptExecutor(guestNameButton);

        for(int i = 1; i <= guests; i++) {
            Locator guestNameInput = getGuestNameInputLocator(i);
            WebElement fullNameInput = driverTools.waitForElementToAppear(guestNameInput).orElseThrow();
            driverTools.clearInput(fullNameInput);
            driverTools.sendKeys(fullNameInput, CoreFunctions.randomName());

            // Robust click with retry mechanism - try different click strategies until button disappears
            boolean buttonDisappeared = false;
            int maxAttempts = 3;

            for (int attempt = 1; attempt <= maxAttempts && !buttonDisappeared; attempt++) {
                try {
                    logger.log("Attempting to click SAVE_GUEST_BUTTON (attempt " + attempt + "/" + maxAttempts + ")");
                    driverTools.clickWithJavascriptExecutor(SAVE_GUEST_BUTTON_MWEB);
                    buttonDisappeared = driverTools.waitForElementToDisappear(SAVE_GUEST_BUTTON_MWEB,  5);

                } catch (Exception e) {
                    logger.log("Exception during attempt " + attempt + ": " + e.getMessage());
                    if (attempt == maxAttempts) {
                        logger.log("All retry attempts exhausted. Button may still be visible.");
                    }
                }
            }
        }
    }

    @Override
    public void fillContactDetail(String fullName, String phoneNumber, String emailId) {
        driverTools.waitForElementToAppear(CONTACT_CARD, 5);
        WebElement fullNameInput = driverTools.waitForElementToAppear(FULL_NAME_INPUT).orElseThrow();
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
            if (!driverTools.getValue(emailInput).equals(emailId)) {
                driverTools.clearInput(emailInput);
                driverTools.sendKeys(emailInput, emailId);
            }
        }
    }

    public boolean isContactForm(){
        driverTools.waitForElementToAppear(CONTACT_CARD, 5);
        return driverTools.isElementDisplayed(CONTACT_CARD);
    }

    @Override
    public int getTotalPrice(){
        String priceText = driverTools.getText(driverTools.waitForElementToAppear(PRICE_SUMMARY_TOTAL).orElseThrow());
        logger.log("Extracted price text: " + priceText);
        String numericValue = priceText.replaceAll("[^0-9.]", "");
        return (int) Math.round(Double.parseDouble(numericValue));
    }

    public void openPriceModal(){
        driverTools.clickWithJavascriptExecutor(SUBTOTAL_AREA);
    }

    @Override
    public void openPriceBreakdownPopUp() {
        driverTools.click(TOTAL_PRICE);
        driverTools.waitForElementsToAppear(ROOM_NAME_IN_PRICE_DETAIL);
    }

    @Override
    public int getTotalPriceInPriceDetail() {
        String priceText = driverTools.getText(TOTAL_PRICE_IN_PRICE_DETAIL).orElseThrow();
        logger.log("Extracted price text: " + priceText);
        String numericValue = priceText.replaceAll("[^0-9.]", "");
        return (int) Math.round(Double.parseDouble(numericValue));
    }

    @Override
    public void fillSalutations(String salutation) {
        driverTools.waitForElementToAppear(CONTACT_DETAILS_CONTAINER, 5).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(CONTACT_SALUTATION, 5);
        List<WebElement> salutationsList = driverTools.elements(CONTACT_SALUTATION).orElseThrow();
        if(salutationsList.size() != 3){
            Assertion.fail("Salutations list has to be 3 elements. But got " + salutationsList.size());
        }

        switch (salutation.toLowerCase()) {
            case "ms" -> driverTools.clickWithJavascriptExecutor(salutationsList.get(1));
            case "mrs" -> driverTools.clickWithJavascriptExecutor(salutationsList.get(2));
            default ->  driverTools.clickWithJavascriptExecutor(salutationsList.get(0));
        }
        driverTools.clickWithJavascriptExecutor(CONTACT_DETAILS_SAVE);
    }

    @Override
    public int getTotalPriceWithoutRounding(){
        String priceText = driverTools.getText(driverTools.waitForElementToAppear(PRICE_TEXT_MWEB).orElseThrow());
        logger.log("Extracted price text: " + priceText);
        try {
            String cleaned = priceText.replaceAll("[^\\d,.]", "");
            cleaned = cleaned.replace(".", "");

            return Integer.parseInt(cleaned);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse price text: " + priceText, e);
        }
    }

    @Override
    public void continueToPaymentPage() {
        if (driverTools.waitForElementToAppear(SELECT_PAYMENT_BUTTON,3).isPresent()) {
            driverTools.click(SELECT_PAYMENT_BUTTON);
        }
    }

    public void fillGuestDetails(int guestIndex, String name,boolean isGuest, boolean isSpecialRequest,String chips) {
        if(isGuest) {
            Locator guestNameButton = getGuestNameButtonLocator(guestIndex);
            Locator guestNameInput = getGuestNameInputLocator(guestIndex);

            driverTools.waitForElementToAppear(guestNameButton, 5);
            driverTools.clickWithJavascriptExecutor(guestNameButton);

            WebElement fullNameInput = driverTools.waitForElementToAppear(guestNameInput).orElseThrow();
            if (!driverTools.getValue(fullNameInput).equals(name)) {
                driverTools.clearInput(fullNameInput);
                driverTools.sendKeys(fullNameInput, name);

                driverTools.waitForElementToAppear(SAVE_GUEST_BUTTON_MWEB, 5);
                driverTools.clickWithJavascriptExecutor(SAVE_GUEST_BUTTON_MWEB);
            }
        }

        if (isSpecialRequest) {
            driverTools.waitForElementToAppear(SPECIAL_REQUEST_BUTTON, 5);
            Assertion.assertThat("Special request button should be visible ",driverTools.isElementDisplayed(SPECIAL_REQUEST_BUTTON),is(true));
            driverTools.clickWithJavascriptExecutor(SPECIAL_REQUEST_BUTTON);

            Locator specialReqChips=getSpecialRequestLocator(chips);

            driverTools.waitForElementToAppear(specialReqChips, 5);
            Assertion.assertThat("Special request Pop Container should be visible ",driverTools.isElementDisplayed(specialReqChips),is(true));
            driverTools.clickWithJavascriptExecutor(specialReqChips);
            driverTools.clickWithJavascriptExecutor(SPECIAL_REQUEST_SAVE);
        }
    }

    @Override
    public List<String> fillMultiGuestDetails(int totalGuest) {
        // TODO: Implement this method
        return null;
    }

    @Override
    public int getTotalPriceOnBookingPage1(){
        String priceText = driverTools.getText(driverTools.waitForElementToAppear(PRICE_TOTAL_BOOKING_PAGE1).orElseThrow());
        logger.log("Extracted price text: " + priceText);
        String numericValue = priceText.replaceAll("[^0-9.]", "");
        return (int) Math.round(Double.parseDouble(numericValue));
    }
}
