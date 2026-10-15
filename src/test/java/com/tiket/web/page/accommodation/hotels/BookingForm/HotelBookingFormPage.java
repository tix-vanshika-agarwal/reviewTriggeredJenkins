package com.tiket.web.page.accommodation.hotels.BookingForm;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.tiket.testbase.Assertion.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.core.Is.is;

public class HotelBookingFormPage<T extends WebDriver> extends BasePage<T>{
    private static final Locator ROOM_NAME = new Locator("ROOM_NAME", new By.ByXPath("//h4[contains(@class, 'FormSubheading_title_text')]"));
    private static final Locator LOGIN_BANNER = new Locator("LOGIN_BANNER", new By.ByXPath("//*[@data-testid='login-banner']"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", new By.ByXPath("//*[@data-testid='price-footer-container']//button"));
    private static final Locator SELECT_PAYMENT_BUTTON = new Locator("SELECT_PAYMENT_BUTTON", new By.ByXPath("//*[@data-testid='price-summary-container']//button[contains(@class,'PriceSummarySection_complete_booking_button')]"));
    private static final Locator CONTINUE_BOOKING_BUTTON = new Locator("CONTINUE_BOOKING_BUTTON", new By.ByXPath("//*[contains(@class,'Dialog_pop_up')]//*[contains(@class,'Button_variant_primary')]"));
    private static final Locator SKIP_ROOM_CHANGE_POPUP = new Locator("SKIP_ROOM_CHANGE_POPUP", new By.ByXPath("//*[contains(@class,'Modal_modal_popup')]//*[contains(@class,'Button_variant_primary')]"));
    private static final Locator OTHER_CHANGE_POPUP = new Locator("OTHER_CHANGE_POPUP", new By.ByXPath("//*[contains(text(),'continue booking') or contains(text(),'Lanjut pesan')]"));
    private static final Locator CONTACT_CARD = new Locator("CONTACT_CARD", new By.ByXPath("(//*[contains(@data-testid, 'contact-details-form')])"));
    private static final Locator CONTACT_SALUTATION = new Locator("CONTACT_SALUTATION", new By.ByXPath("(//*[contains(@data-testid, 'contact-details-form')])//label[contains(@class, 'Radio_radio')]"));
    private static final Locator FULL_NAME_INPUT = new Locator("FULL_NAME_INPUT", new By.ByXPath("//input[@id='full-name-according-to-id']"));
    private static final Locator PHONE_NUMBER_INPUT = new Locator("PHONE_NUMBER_INPUT", new By.ByXPath("//input[@id='mobile-number']"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", new By.ByXPath("//input[@id='email-address']"));
    private static final Locator EMAIL_DISABLED = new Locator("EMAIL_DISABLED", new By.ByXPath("(//*[contains(@class, 'styles_state_disabled')])[2]"));
    private static final Locator ENTER_GUEST_NAME_BUTTON = new Locator("ENTER_GUEST_NAME_BUTTON", new By.ByXPath("(//div[contains(@class, 'FormItem_room_guest_row')]//span)[2]"));
    private static final Locator INPUT_GUEST_NAME = new Locator("INPUT_GUEST_NAME", new By.ByXPath("(//input[contains(@data-testid, 'guest-form-full-name-text-input')])[1]"));
    private static final Locator SAVE_GUEST_BUTTON = new Locator("SAVE_GUEST_BUTTON", new By.ByXPath("//footer[contains(@class, 'BottomSheet_sticky_footer')]//button"));
    private static final Locator SPECIAL_REQUEST_BUTTON = new Locator("SPECIAL_REQUEST_BUTTON", new By.ByXPath("//span[contains(@class, 'special')]"));
    private static final Locator SPECIAL_REQUEST_CHIPS = new Locator("SPECIAL_REQUEST_CHIPS", new By.ByXPath("(//div[contains(@data-testid, 'special-request-chips-container')]//button)[1]"));
    private static final Locator SPECIAL_REQUEST_SAVE = new Locator("SPECIAL_REQUEST_SAVE", new By.ByXPath("(//div[contains(@data-testid, 'action-container')]//button)[2]"));
    private static final Locator ADDONS_BUTTON = new Locator("ADDONS_BUTTON", new By.ByXPath("//*[@data-testid='add-on-card-item-container']//button"));
    private static final Locator ADDONS_RADIO_BUTTON = new Locator("ADDONS_RADIO_BUTTON", new By.ByXPath("//span[contains(@class, 'Check_custom_checkbox')]"));
    private static final Locator ADDONS_INC_BUTTON = new Locator("ADDONS_INC_BUTTON", new By.ByXPath("//button[contains(@class, 'QuantityEditor_operation_button')][2]"));
    private static final Locator ADDONS_SAVE_BUTTON = new Locator("ADDONS_SAVE_BUTTON", new By.ByXPath("//div[contains(@class, 'Footer_footer')]//button"));
    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", new By.ByXPath("//span[contains(@data-testid, 'total-price-text')]"));
    private static final Locator INSURANCE_BUTTON = new Locator("SPECIAL_REQUEST_SAVE", new By.ByXPath("//div[contains(@class, 'insurance_tab')]//button"));
    private static final Locator ENTER_SEC_GUEST_NAME_BUTTON = new Locator("ENTER_GUEST_NAME_BUTTON", new By.ByXPath("(//div[contains(@class, 'FormItem_room_guest_row')][2]//span)[2]"));
    private static final Locator GUEST_STAY_DETAILS_CARD = new Locator("GUEST_STAY_DETAILS_CARD", new By.ByXPath("//div[contains(@class, 'StayDetails')]"));
    private static final Locator BOOKINF_FORM_HOTEL_NAME = new Locator("BOOKINF_FORM_HOTEL_NAME", new By.ByXPath("//*[@data-testid='item-name']"));
    private static final Locator SEE_OTHER_ROOMS = new Locator("SEE_OTHER_ROOMS", new By.ByXPath("//div[contains(@class, 'Figure_footer')]//button[1]"));
    private static final Locator SPECIAL_REQUEST_TEXT = new Locator("SPECIAL_REQUEST_TEXT", new By.ByXPath("//span[contains(@class, 'special_request')]"));
    private static final Locator SPECIAL_REQUEST_TEXTAREA = new Locator("SPECIAL_REQUEST_TEXTAREA", new By.ByXPath("//textarea"));
    private static final Locator GUEST_NAME = new Locator("GUEST_NAME", new By.ByXPath("//span[contains(@class, 'guest_name')]"));
    private static final Locator BOOKING_PRICE = new Locator("BOOKING_PRICE", new By.ByXPath("//span[contains(@data-testid, 'total-price-text')]"));
    private static final Locator BOOKING_DATES = new Locator("BOOKING_DATES", new By.ByXPath("//p[contains(@data-testid, 'item-date')]"));
    private static final Locator BOOKING_ROOM_NIGHT = new Locator("BOOKING_DATES", new By.ByXPath("//p[contains(@data-testid, 'item-room-night')]"));
    private static final Locator CLOSE_MODAL = new Locator("BOOKING_DATES", new By.ByXPath("//header[contains(@class, 'modal')]//button"));
    private static final Locator BOOK_NAME_BUTTON = new Locator("BOOKING_DATES", new By.ByXPath("(//span[contains(@class, 'guest_name')])[2]"));
    private static final Locator INPUT_BOOK_NAME = new Locator("BOOKING_DATES", new By.ByXPath("(//input[contains(@data-testid, 'guest-form-full-name-text-input')])[2]"));
    private static final Locator B2B_MULTIGUEST_ERROR = new Locator("B2B_MULTIGUEST_ERROR", new By.ByXPath("//button[text()='Select 1 room type' or text()='Pilih 1 tipe kamar']"));
    private static final Locator B2B_EMAIL_MULTIGUEST_ERROR = new Locator("B2B_EMAIL_MULTIGUEST_ERROR", new By.ByXPath("//span[contains(text(),'tiket.com for Corporates')]"));
    private static final Locator SALUTATION_MESSAGE=new Locator("SALUTATION_MESSAGE",new By.ByXPath("//div[contains(@class, 'GroupField_helper_or_error_text') and contains(@class, 'Text_variant_alert')]"));
    private static final Locator RATE_PLAN_POPUP = new Locator("RATE_PLAN_POPUP", new By.ByXPath("//button[contains(@class, 'Button_button') and contains(@class, 'Button_variant_primary') and contains(@class, 'Button_full_width')]"));

    public HotelBookingFormPage(Session<T> session) {
        super(session);
    }

    public void clickLoginBanner() {
        driverTools.waitForElementToAppear(LOGIN_BANNER).orElseThrow();
        driverTools.click(LOGIN_BANNER);
    }

    public void continueToPaymentPage() {
        driverTools.waitForElementToAppear(SELECT_PAYMENT_BUTTON);
        driverTools.click(SELECT_PAYMENT_BUTTON);
        if(!driverTools.waitForElementToDisappear(SELECT_PAYMENT_BUTTON,3)){
            driverTools.scrollIntoWebView(SELECT_PAYMENT_BUTTON);
            driverTools.clickWithJavascriptExecutor(SELECT_PAYMENT_BUTTON);
        }
        checkRatePlanPopUp();
    }

    public void checkRatePlanPopUp(){
        driverTools.waitForElementToAppear(RATE_PLAN_POPUP, 10)
                .ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(RATE_PLAN_POPUP, 10)
                .ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public boolean isAddonThereOnBookingForm() {
        driverTools.waitForElementToPresence(SELECT_PAYMENT_BUTTON).orElseThrow();
        String buttonText = driverTools.getText(SELECT_PAYMENT_BUTTON).orElseThrow();
        assertThat("Button text should indicate payment selection", buttonText, either(containsString("Select Payment")).or(containsString("Pilih Pembayaran")));
        return true;
    }
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

    public boolean isContactForm(){
        driverTools.waitForElementToAppear(CONTACT_CARD, 5);
        return driverTools.isElementDisplayed(CONTACT_CARD);
    }

    public boolean isGuestStayForm(){
        driverTools.waitForElementToAppear(GUEST_STAY_DETAILS_CARD, 5);
        return driverTools.isElementDisplayed(GUEST_STAY_DETAILS_CARD);
    }

    public boolean isSpecialRequest(){
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_BUTTON, 5);
        return driverTools.isElementDisplayed(SPECIAL_REQUEST_BUTTON);
    }

    public boolean isSpecialRequestPopup(){
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_CHIPS, 5);
        return driverTools.isElementDisplayed(SPECIAL_REQUEST_CHIPS);
    }

    private Locator getGuestNameButtonLocator(int guestIndex) {
        return new Locator("ENTER_GUEST_NAME_BUTTON_" + guestIndex,
                new By.ByXPath("(((//div[contains(@class, 'FormItem_room_guest_row')])["+guestIndex+"])//span)[2]"));
    }

    private Locator getGuestNameInputLocator(int guestIndex) {
        return new Locator("INPUT_GUEST_NAME_" + guestIndex,
                new By.ByXPath("(//input[contains(@data-testid, 'guest-form-full-name-text-input')])[" + guestIndex + "]"));
    }
    private Locator getSpecialRequestLocator(String requestText) {
        return new Locator("SPECIAL_REQUEST_CHIPS" + requestText,
                new By.ByXPath("//div[contains(@data-testid, 'special-request-chips-container')]//span[contains(text(), '" + requestText + "')]"));
    }

    public void fillGuestDetailsCombinedRandom(int guests) {
        Locator guestNameButton = getGuestNameButtonLocator(1);
        driverTools.waitForElementToAppear(guestNameButton, 5);
        driverTools.clickWithJavascriptExecutor(guestNameButton);

        for(int i = 1; i <= guests; i++) {
            Locator guestNameInput = getGuestNameInputLocator(i);
            WebElement fullNameInput = driverTools.waitForElementToAppear(guestNameInput).orElseThrow();
            driverTools.clearInput(fullNameInput);
            driverTools.sendKeys(fullNameInput, CoreFunctions.randomName());
            driverTools.waitForElementToAppear(SAVE_GUEST_BUTTON, 5);
            driverTools.clickWithJavascriptExecutor(SAVE_GUEST_BUTTON);
        }
    }

    public void fillGuestDetails(int guestIndex, String name, boolean isGuest, boolean isSpecialRequest,String chips) {
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
            assertThat("Special request button should be visible ",driverTools.isElementDisplayed(SPECIAL_REQUEST_BUTTON),is(true));
            driverTools.clickWithJavascriptExecutor(SPECIAL_REQUEST_BUTTON);

            Locator specialReqChips=getSpecialRequestLocator(chips);

            driverTools.waitForElementToAppear(specialReqChips, 5);
            assertThat("Special request Pop Container should be visible ",driverTools.isElementDisplayed(specialReqChips),is(true));
            driverTools.clickWithJavascriptExecutor(specialReqChips);
            driverTools.click(SPECIAL_REQUEST_SAVE);
        }
    }

    public void fillGuestDetailsMultiRoom(int noOfGuest) {
        for (int i = 2; i <= noOfGuest; i++) { fillGuestDetails(i, CoreFunctions.randomName(), true, false, ""); }
    }

    public void skipRoomChangePopup(){
        if(driverTools.waitForElementToAppear(CONTINUE_BOOKING_BUTTON, 5).isPresent()){
            driverTools.clickWithJavascriptExecutor(CONTINUE_BOOKING_BUTTON);
        }
        if(driverTools.waitForElementToAppear(SKIP_ROOM_CHANGE_POPUP, 5).isPresent()){
            driverTools.clickWithJavascriptExecutor(SKIP_ROOM_CHANGE_POPUP);
        }
        if(driverTools.waitForElementToAppear(OTHER_CHANGE_POPUP, 5).isPresent()){
            driverTools.clickWithJavascriptExecutor(OTHER_CHANGE_POPUP);
        }
    }

    public void continueToNextPage(){
        skipRoomChangePopup();
        driverTools.waitForElementToAppear(CONTINUE_BUTTON, 5);
        driverTools.scrollIntoWebView(CONTINUE_BUTTON);
        driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON);
    }

    public int getTotalPrice(){
        String priceText = driverTools.getText(driverTools.waitForElementToAppear(TOTAL_PRICE).orElseThrow());
        assertThat("Total price should be visible",driverTools.isElementDisplayed(TOTAL_PRICE),is(true));
        logger.log("Extracted price text: " + priceText);
        String numericValue = priceText.replaceAll("[^0-9.]", "");
        return (int) Math.round(Double.parseDouble(numericValue));
    }

    public String getBookingHotelName(){
        WebElement webElement = driverTools.waitForElementToAppear(BOOKINF_FORM_HOTEL_NAME).orElseThrow();
        return driverTools.getText(webElement);
    }

    public void chooseAddOnsAndInsurance(Boolean isProtection){
        driverTools.waitForElementToAppear(ADDONS_BUTTON,3);
        assertThat("AddOns should be visible If available",driverTools.isElementDisplayed(ADDONS_BUTTON),is(true));
        driverTools.click(ADDONS_BUTTON);

        driverTools.waitForElementToAppear(ADDONS_RADIO_BUTTON,3);
        driverTools.clickWithJavascriptExecutor(ADDONS_RADIO_BUTTON);

        driverTools.waitForElementToAppear(ADDONS_INC_BUTTON,3);
        driverTools.clickWithJavascriptExecutor(ADDONS_INC_BUTTON);

        driverTools.waitForElementToAppear(ADDONS_SAVE_BUTTON,3);
        driverTools.click(ADDONS_SAVE_BUTTON);

        if(isProtection){
            driverTools.waitForElementToAppear(ADDONS_SAVE_BUTTON,3);
            assertThat("Insurance like Stay Protection should be visible If available",driverTools.isElementDisplayed(INSURANCE_BUTTON),is(true));
            String ButtonText=driverTools.getText(INSURANCE_BUTTON).orElseThrow();
            logger.log("Button Text->"+ ButtonText);
            if(ButtonText.contains("Add")){
                driverTools.click(INSURANCE_BUTTON);
            }
        }

    }

    public void clickSeeOtherRooms(){
        driverTools.clickWithJavascriptExecutor(SEE_OTHER_ROOMS);
    }

    public void enterSpecialRequest(String customReq){
        driverTools.clickWithJavascriptExecutor(SPECIAL_REQUEST_BUTTON);
        driverTools.waitForElementsToAppear(SPECIAL_REQUEST_TEXTAREA,5);
        driverTools.waitForElementToBeClickable(SPECIAL_REQUEST_TEXTAREA,5);

        driverTools.sendKeys(SPECIAL_REQUEST_TEXTAREA,customReq);
        driverTools.clickWithJavascriptExecutor(SPECIAL_REQUEST_SAVE);
    }

    public String getSpecialReqText(){
        WebElement webElement = driverTools.waitForElementToAppear(SPECIAL_REQUEST_TEXT).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getGuestName(){
        WebElement webElement = driverTools.waitForElementToAppear(GUEST_NAME).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getBookingPrice(){
        WebElement webElement = driverTools.waitForElementToAppear(BOOKING_PRICE).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getBookingDate(){
        WebElement webElement = driverTools.waitForElementToAppear(BOOKING_DATES).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getBookingRoomNight(){
        WebElement webElement = driverTools.waitForElementToAppear(BOOKING_ROOM_NIGHT).orElseThrow();
        return driverTools.getText(webElement);
    }

    private Locator getRoomPrice(int index) {
        return new Locator("ROOM_PRICE_BOOKING_" + index,
                new By.ByXPath("(//div[contains(@data-testid, 'price-per-night-item-container')]//span[contains(@class,'price_text')])[" + index + "]"));
    }

    public int getIndividualPrice(int index){
        Locator roomPrice = getRoomPrice(index);
        WebElement element = driverTools.waitForElementToAppear(roomPrice).orElseThrow();
        String price= driverTools.getText(element);
        return Integer.parseInt(price.split(" ")[1].replace(",", ""));
    }

    public void openPriceModal(){
        driverTools.clickWithJavascriptExecutor(TOTAL_PRICE);
    }
    public void closePriceModal(){
        driverTools.clickWithJavascriptExecutor(CLOSE_MODAL);
    }

    public void fillGuestDifferentRatePlan(String name){
        driverTools.clickWithJavascriptExecutor(BOOK_NAME_BUTTON);
        driverTools.waitForElementToAppear(INPUT_BOOK_NAME);
        driverTools.sendKeys(INPUT_BOOK_NAME,name);
        driverTools.clickWithJavascriptExecutor(SAVE_GUEST_BUTTON);
    }

    public boolean verifyValidationErrorForMultiGuestB2B() {
        return driverTools.waitForElementToPresence(B2B_MULTIGUEST_ERROR, 15).isPresent();
    }

    public boolean verifyEmailErrorWhenPutB2BEmailMultiGuest(){
        return driverTools.waitForElementToPresence(B2B_EMAIL_MULTIGUEST_ERROR, 15).isPresent();
    }

    public String getBookingRoomName(){
        WebElement webElement=driverTools.waitForElementToAppear(ROOM_NAME).orElseThrow();
        return driverTools.getText(webElement);
    }

    public boolean checkSalutation(){
        WebElement webElement=driverTools.waitForElementToAppear(GUEST_NAME).orElseThrow();
        String name=webElement.getText();
        if(name.contains("Mr.")|| name.contains("Mrs.") || name.contains("Ms.")){
            return false;
        }else{
            return true;
        }
    }

    public boolean checkMessage(){
        return driverTools.isElementDisplayed(SALUTATION_MESSAGE);
    }

    public void moveToRoomList(){
        driverTools.back();
        driverTools.click(SEE_OTHER_ROOMS);
    }
}