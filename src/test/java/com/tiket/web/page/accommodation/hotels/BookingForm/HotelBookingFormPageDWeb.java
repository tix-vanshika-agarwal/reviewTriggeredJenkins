package com.tiket.web.page.accommodation.hotels.BookingForm;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
import java.util.List;

public class HotelBookingFormPageDWeb<T extends WebDriver> extends HotelBookingFormPageWeb<T> {
    public HotelBookingFormPageDWeb(Session<T> session) {
        super(session);
    }

    private static final Locator CONTACT_CARD = new Locator("CONTACT_CARD", new By.ByXPath("(//button[contains(@class, 'GroupFieldBody_button_container')])[1]"));
    private static final Locator CONTACT_DETAILS_FULL_NAME = new Locator("CONTACT_DETAILS_FULL_NAME", By.xpath(
            "//*[@data-testid='contact-details-form']//input[@id='full-name-according-to-id' or @id='full-name-according-to-idpassport'"
                    + " or @id='nama-lengkap-sesuai-ktppasporsim' or @id='full-name' or @id='nama-lengkap']"));
    private static final Locator CONTACT_DETAILS_PHONE = new Locator("CONTACT_DETAILS_PHONE", By.xpath(
            "//*[@data-testid='contact-details-form']//input[@id='mobile-number']"));
    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", new By.ByXPath("//span[contains(@data-testid, 'total-price-text')]"));
    private static final Locator SAVE_GUEST_BUTTON = new Locator("SAVE_GUEST_BUTTON", new By.ByXPath("//footer[contains(@class, 'Modal_modal_footer')]//button"));
    private static final Locator SPECIAL_REQUEST_SAVE = new Locator("SPECIAL_REQUEST_SAVE", new By.ByXPath("//div[contains(@data-testid, 'action-container')]//button"));
    private static final Locator RATE_PLAN_POPUP = new Locator("RATE_PLAN_POPUP", new By.ByXPath("//button[contains(@class, 'Button_button') and contains(@class, 'Button_variant_primary') and contains(@class, 'Button_full_width')]"));
    /** Pre-payment "The price has been updated" dialog body (used to read its text). */
    private static final Locator RATE_PLAN_POPUP_DIALOG = new Locator("RATE_PLAN_POPUP_DIALOG", new By.ByXPath("//div[contains(@class,'Dialog_pop_up') or contains(@class,'Dialog_dialog')]//h2"));


    @Override
    public void fillGuestDetailsCombinedRandom(int guests) {
        Locator guestNameButton = getGuestNameButtonLocator(1);
        driverTools.waitForElementToAppear(guestNameButton, 5);
        driverTools.clickWithJavascriptExecutor(guestNameButton);

        for (int i = 1; i <= guests; i++) {
            Locator guestNameInput = getGuestNameInputLocator(i);
            WebElement fullNameInput = driverTools.waitForElementToAppear(guestNameInput).orElseThrow();
            driverTools.clearInput(fullNameInput);
            driverTools.sendKeys(fullNameInput, CoreFunctions.randomName());
            driverTools.waitForElementToAppear(SAVE_GUEST_BUTTON, 5);
            driverTools.clickWithJavascriptExecutor(SAVE_GUEST_BUTTON);
        }
    }

    @Override
    public void fillContactDetailIfEmpty(String fullName, String phoneNumber) {
        driverTools.waitForElementToAppear(CONTACT_CARD, 5);
        selectFirstContactSalutation();

        driverTools.waitForElementToAppear(CONTACT_DETAILS_FULL_NAME, 10).ifPresent(fullNameInput -> {
            String currentName = driverTools.getValue(fullNameInput);
            if (currentName == null || currentName.trim().isEmpty()) {
                driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(CONTACT_DETAILS_FULL_NAME, fullName);
            }
        });

        driverTools.waitForElementToAppear(CONTACT_DETAILS_PHONE, 10).ifPresent(phoneInput -> {
            String currentPhone = driverTools.getValue(phoneInput);
            if (currentPhone == null || currentPhone.trim().isEmpty()) {
                driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(CONTACT_DETAILS_PHONE, phoneNumber);
            }
        });
    }

    @Override
    public void fillContactDetail(String fullName, String phoneNumber, String emailId) {
        driverTools.waitForElementToAppear(CONTACT_CARD, 5);
        driverTools.click(CONTACT_SALUTATION);
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

    @Override
    public int getTotalPrice(){
        String priceText = driverTools.getText(driverTools.waitForElementToAppear(TOTAL_PRICE).orElseThrow());
        logger.log("Extracted price text: " + priceText);
        String digitsOnly = priceText.replaceAll("\\D", "");
        return Integer.parseInt(digitsOnly);
    }

    @Override
    public boolean isContactForm(){
        driverTools.waitForElementToAppear(CONTACT_CARD, 5);
        return driverTools.isElementDisplayed(CONTACT_CARD);
    }

    @Override
    public void fillSalutations(String salutation) {
        selectFirstContactSalutation();
    }

    @Override
    public int getTotalPriceWithoutRounding(){
        String priceText = driverTools.getText(driverTools.waitForElementToAppear(TOTAL_PRICE).orElseThrow());
        logger.log("Extracted price text: " + priceText);
        String numericValue = priceText.replaceAll("\\D", "");
        return Integer.parseInt(numericValue);
    }

    @Override
    public void continueToPaymentPage() {
        driverTools.waitForElementToAppear(SELECT_PAYMENT_BUTTON, 15);
        driverTools.scrollIntoWebView(SELECT_PAYMENT_BUTTON);
        driverTools.click(SELECT_PAYMENT_BUTTON);

        //HANDLING FOR BF2
        driverTools.waitForElementToPresence(SELECT_PAYMENT_BUTTON, 10).ifPresent(driverTools::clickWithJavascriptExecutor);

        checkRatePlanPopUp();
    }

    public void checkRatePlanPopUp(){
        driverTools.waitForElementToAppear(RATE_PLAN_POPUP, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(RATE_PLAN_POPUP, 10).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    /**
     * Clicks "Select Payment" to leave booking form page 2, detects whether the rate-plan change
     * popup is shown (e.g. when the room rate was updated via the supply allotments API after the
     * booking form loaded), dismisses it and proceeds to the payment page.
     *
     * @return the price-update dialog text when it appeared before the payment page, otherwise an empty string.
     */
    public String continueToPaymentPageDetectingRatePlanPopup() {
        clickPickPayMethodButton();
        driverTools.waitForElementToAppear(SELECT_PAYMENT_BUTTON, 15);
        driverTools.scrollIntoWebView(SELECT_PAYMENT_BUTTON);
        driverTools.clickWithJavascriptExecutor(SELECT_PAYMENT_BUTTON);
        driverTools.waitForElementToAppear(RATE_PLAN_POPUP_DIALOG).orElseThrow();
        String popupText = driverTools.getText(RATE_PLAN_POPUP_DIALOG).orElseThrow();
        logger.log("Price-update dialog before payment page -> " + popupText);
        checkRatePlanPopUp();
        return popupText;
    }
    @Override
    public void openPriceBreakdownPopUp(){
        driverTools.click(TOTAL_PRICE);
        driverTools.waitForElementsToAppear(ROOM_NAME_IN_PRICE_DETAIL);
    }

    @Override
    public int getTotalPriceInPriceDetail(){
        String priceText = driverTools.getText(TOTAL_PRICE_IN_PRICE_DETAIL).orElseThrow();
        logger.log("Extracted price text: " + priceText);
        String numericValue = priceText.replaceAll("[^0-9.]", "");
        return (int) Math.round(Double.parseDouble(numericValue));
    }

    @Override
    public void openPriceModal(){
        driverTools.clickWithJavascriptExecutor(TOTAL_PRICE);
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

                driverTools.waitForElementToAppear(SAVE_GUEST_BUTTON, 5);
                driverTools.clickWithJavascriptExecutor(SAVE_GUEST_BUTTON);
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
            driverTools.click(SPECIAL_REQUEST_SAVE);
        }
    }

    @Override
    public List<String> fillMultiGuestDetails(int totalGuest) {
        Locator guestNameButton = getGuestNameButtonLocator(1);
        driverTools.waitForElementToAppear(guestNameButton, 5);
        driverTools.clickWithJavascriptExecutor(guestNameButton);

        String name;
        String guestName;
        List<String> guestNames = new ArrayList<>();
        for (int i = 1; i <= totalGuest; i++) {
            driverTools.clickWithJavascriptExecutor(roomTypeTab(i));
            Locator guestNameInput = getGuestNameInputLocator(i);
            WebElement fullNameInput = driverTools.waitForElementToAppear(guestNameInput).orElseThrow();
            driverTools.clearInput(fullNameInput);
            name = CoreFunctions.randomName();
            guestName = name + " autguest";
            logger.log("Guest name to be filled at index " + i + ": " + guestName);
            driverTools.sendKeys(fullNameInput, guestName);
        }
        driverTools.waitForElementToAppear(SAVE_GUEST_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(SAVE_GUEST_BUTTON);
        for (int i = 1; i <= totalGuest; i++) {
            logger.log("Guest name filled at index " + i + ": " + getGuestNameValue(i));
            guestNames.add(getGuestNameValue(i));
        }
        logger.log("Guest names collected: " + guestNames);
        Assertion.assertThat("Guest names should be collected", guestNames.size(), is(totalGuest));
        return guestNames;
    }

    public Locator roomTypeTab(int index) {
        return new Locator("ROOM_TYPE_TAB_" + index, new By.ByXPath("//*[contains(@class,'Tabs_tab_container')]//button[" + index + "]"));
    }

    @Override
    public int getTotalPriceOnBookingPage1(){
        return getTotalPrice();
    }
}
