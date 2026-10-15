package com.tiket.page.bus.bookingform;

import com.costumeMatcher.IsSubstringOfMatcher;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.bus.BusSrpOneWayData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public abstract class BusBookingFormPage  <T extends WebDriver> extends BasePage<T> {
    private static final Locator TOGGLE_SAME_AS_CONTACT = new Locator("TOGGLE_SAME_AS_CONTACT", AppiumBy.xpath("//div[contains(@class,'toggle')]"));
    private static final Locator FIRST_PASSENGER_UNTOGGLED = new Locator("FIRST_PASSENGER_UNTOGGLED", AppiumBy.xpath("//p[contains(@class,'Text_variant_activity') and contains(text(),'1')]"));
    private static final Locator SAVE_PASSENGER_DETAIL_BTN = new Locator("SAVE_PASSENGER_DETAIL_BTN", AppiumBy.xpath("//*[contains(text(),'Simpan') or contains(text(),'Save')]//parent::button"));
    private static final Locator BOOKING_FORM_FINAL_PRICE = new Locator("BOOKING_FORM_FINAL_PRICE", AppiumBy.xpath("//*[contains(@class,'PriceSummaryItem_label') and (contains(text(),'Total bayar') or contains(text(),'Total to pay'))]//following-sibling::span"));
    private static final Locator CONTINUE_TO_PAYMENT_BTN = new Locator("CONTINUE_TO_PAYMENT_BTN", AppiumBy.xpath("//div[contains(@class,'BookingFooter_price_container')]//button"));
    private static final Locator CONFIRM_CONTINUE_TO_PAYMENT_BTN = new Locator("CONFIRM_CONTINUE_TO_PAYMENT_BTN", AppiumBy.xpath("//*[contains(@class,'Dialog_body')]//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator CONTACT_MR_SALUTATION_BTN = new Locator("CONTACT_MR_SALUTATION_BTN", AppiumBy.xpath("//label[contains(@class,'Radio') and span[contains(text(),'Tuan') or contains(text(),'Mr')]]"));
    private static final Locator CONTACT_FULLNAME_INPUT_BAR = new Locator("CONTACT_FULLNAME_INPUT_BAR", AppiumBy.xpath("//input[@name='fullName']"));
    private static final Locator PASSENGER_MR_SALUTATION_BTN = new Locator("PASSENGER_MR_SALUTATION_BTN", AppiumBy.xpath("//*[contains(@class,'PassengerForm')]//label[contains(@class,'Radio') and span[contains(text(),'Tuan') or contains(text(),'Mr')]]"));
    private static final Locator PASSENGER_FULLNAME_INPUT_BAR = new Locator("PASSENGER_FULLNAME_INPUT_BAR", AppiumBy.xpath("//*[contains(@class,'PassengerForm')]//*[contains(@class,'Autocomplete_wrapper')]//input"));
    private static final Locator CONTACT_PHONE_NUMBER_INPUT_BAR = new Locator("CONTACT_PHONE_NUMBER_INPUT_BAR", AppiumBy.xpath("//input[@id='nomor-ponsel' or @id='mobile-number']"));
    private static final Locator CONTACT_DETAIL_SECTION_CARD = new Locator("CONTACT_DETAIL_SECTION_CARD", AppiumBy.xpath("//div[contains(@class,'ContactDetailSection_card')]"));
    private static final Locator EMAIL_ADDRESS_INPUT_BAR = new Locator("EMAIL_ADDRESS_INPUT_BAR", AppiumBy.xpath("//input[@name='emailAddress']"));
    private static final Locator CONTACT_DETAIL_SAVE_BTN = new Locator("CONTACT_DETAIL_SAVE_BTN", AppiumBy.xpath("//button[contains(@class,'ContactDetailSection_button')]"));
    private static final Locator PASSENGER_DETAIL_DOB = new Locator("PASSENGER_DETAIL_DOB", AppiumBy.xpath("//*[@id='date-input']"));
    private static final Locator YEAR_DOB_PICKER = new Locator("YEAR_DOB_PICKER", AppiumBy.xpath("(//*[contains(@class,'DateWheel_keen_slider')])[last()]"));
    private static final Locator SAVE_DOB_BTN = new Locator("SAVE_DOB_BTN", AppiumBy.xpath("//button[contains(@class,'datepicker_button')]"));
    private static final Locator PASSENGER_CARDS = new Locator("PASSENGER_CARDS", AppiumBy.xpath("//*[contains(@class,'PassengersDetailSection_card_inner')]"));
    private static final Locator PRICE_CHANGE_POPUP = new Locator("PRICE_CHANGE_POPUP", AppiumBy.xpath("//h2[contains(text(),'price has changed')  or contains(text(),'Harga telah berubah')]"));
    private static final Locator CONFIRM_PRICE_CHANGE_BTN = new Locator("CONFIRM_PRICE_CHANGE_BTN", AppiumBy.xpath("//*[contains(@class, 'pop_up')]//button[contains(@class, 'variant_primary')]"));
    private static final Locator PASSENGER_NAME = new Locator("PASSENGER_NAME", AppiumBy.xpath("//div[contains(@class,'PassengersDetailSection_card_inner')]//p[contains(@class, 'Text_text')]"));
    private static final Locator BUS_SEAT_NO_DETAIL = new Locator("BUS_SEAT_NO_DETAIL", AppiumBy.xpath("(//*[contains(@class, 'PassengersDetailSection_info')]//div[contains(@class,'Text_text__')])"));
    private static final Locator DEPARTURE_DATE = new Locator("DEPARTURE_DATE", AppiumBy.xpath("(//*[contains(@class, 'BookingReviewCards_date_time__')]/*[contains(@class,'Text_text__') and contains(text(),'202')])[1]"));
    private static final Locator RETURN_DATE = new Locator("RETURN_DATE", AppiumBy.xpath("(//*[contains(@class, 'BookingReviewCards_date_time__')]/*[contains(@class,'Text_text__') and contains(text(),'202')])[last()]"));
    private static final Locator DEPARTURE_TIME = new Locator("DEPARTURE_TIME", AppiumBy.xpath("//*[contains(@class, 'BookingReviewCards_date_time__')]/*[contains(@class,'Text_text__')][last()]"));
    private static final Locator DEPARTURE_ORIGIN = new Locator("DEPARTURE_ORIGIN", AppiumBy.xpath("(//div[contains(@class,'BookingReviewCards_origin')])[1]//span[1]"));
    private static final Locator DEPARTURE_DESTINATION = new Locator("DEPARTURE_DESTINATION", AppiumBy.xpath("(//div[contains(@class,'BookingReviewCards_origin')])[1]//span[2]"));
    private static final Locator DEPARTURE_OPERATOR_NAME = new Locator("DEPARTURE_OPERATOR_NAME", AppiumBy.xpath("(//div[contains(@class,'BookingReviewCards_operator') and span[contains(@class,'divider_dot')]])[1]//span[1]"));
    private static final Locator DEPARTURE_BUS_CLASS = new Locator("DEPARTURE_BUS_CLASS", AppiumBy.xpath("(//div[contains(@class,'BookingReviewCards_operator') and span[contains(@class,'divider_dot')]])[1]//span[last()]"));
    private static final Locator PRICE_CHANGE_DESCRIPTION = new Locator("PRICE_CHANGE_DESCRIPTION", AppiumBy.xpath("//*[contains(@class,'description')]"));
    private static final Locator PASSENGER_NAME_BOOKING_PAGE = new Locator("PASSENGER_NAME_BOOKING_PAGE", AppiumBy.xpath("//*[contains(@class,'PassengersDetailSection_card_inner')]//p"));
    private static final Locator PRICE_DETAIL_TOTAL_PRICE = new Locator("PRICE_DETAIL_TOTAL_PRICE", AppiumBy.xpath("//div[contains(@class,'PriceContent_total')]//span[2]"));
    private static final Locator PRICE_DETAIL_COMPONENTS = new Locator("PRICE_DETAIL_COMPONENTS", AppiumBy.xpath("//*[contains(@class,'PriceSummaryItem_label') and not(contains(text(),'Total bayar') or contains(text(),'Total to pay') or contains(text(),'Sub'))]//following-sibling::span"));

    public BusBookingFormPage(Session<T> session) {
        super(session);
    }
    
    public static <T extends WebDriver> BusBookingFormPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new BusBookingFormPageAndroid<>(session);}
            case IOS -> {return new BusBookingFormPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public void togglePassengerSameAsContact(){
        driverTools.switchToWebViewUrl("bus-travel/booking");
        if(driverTools.waitForElementToAppear(FIRST_PASSENGER_UNTOGGLED,5).isPresent()){
            driverTools.click(TOGGLE_SAME_AS_CONTACT);
            driverTools.clickWithJavascriptExecutor(PASSENGER_DETAIL_DOB);
            driverTools.waitForElementToAppear(YEAR_DOB_PICKER);
            driverTools.click(SAVE_DOB_BTN);
            driverTools.click(SAVE_PASSENGER_DETAIL_BTN);
            driverTools.waitForElementToDisappear(SAVE_PASSENGER_DETAIL_BTN);
        }
        driverTools.switchToNative();
    }

    private void fillContactField(String fullName,String phoneNumber,String email){
        logger.log("email LOCATION : " + email);
        driverTools.click(CONTACT_MR_SALUTATION_BTN);
        driverTools.clearWithJsExecutor(CONTACT_FULLNAME_INPUT_BAR);
        driverTools.sendKeys(CONTACT_FULLNAME_INPUT_BAR, fullName);
        int maxRetries = 3;
        int retryCount = 0;
        while(!driverTools.isElementDisplayed(getContactLocator(phoneNumber)) && retryCount < maxRetries){
            driverTools.waitForElementToPresence(CONTACT_PHONE_NUMBER_INPUT_BAR);
            driverTools.clearWithJsExecutor(CONTACT_PHONE_NUMBER_INPUT_BAR);
            driverTools.sendKeys(CONTACT_PHONE_NUMBER_INPUT_BAR, phoneNumber);
            retryCount++;
            driverTools.waitForElementToPresence(getContactLocator(phoneNumber),10);
        }
        retryCount = 0;
        while(!driverTools.isElementDisplayed(getContactLocator(email)) && retryCount < maxRetries){
            driverTools.waitForElementToPresence(EMAIL_ADDRESS_INPUT_BAR);
            driverTools.clearWithJsExecutor(EMAIL_ADDRESS_INPUT_BAR);
            driverTools.sendKeys(EMAIL_ADDRESS_INPUT_BAR, email);
            retryCount++;
            driverTools.waitForElementToPresence(getContactLocator(email),10);
        }
    }

    public Locator getContactLocator(String type){
        return new Locator(type, AppiumBy.xpath("//input[contains(@value,'" + type + "')]"));
    }

    public void inputGuestContactDetail(String fullName,String phoneNumber,String email){
        driverTools.switchToWebViewUrl("bus-travel/booking");
        if(driverTools.isElementDisplayed(CONTACT_DETAIL_SECTION_CARD)){
            driverTools.click(CONTACT_DETAIL_SECTION_CARD);
            driverTools.click(CONTACT_MR_SALUTATION_BTN);
            driverTools.click(CONTACT_DETAIL_SAVE_BTN);
        }else{
            fillContactField(fullName, phoneNumber, email);
        }
        driverTools.switchToNative();
        driverTools.hideKeyboard(1);
    }

    public void inputPassengerGuestDetail(){
        driverTools.switchToWebViewUrl("bus-travel/booking");
        //first passenger of guest condition
        driverTools.swipeUntilElementVisible(TOGGLE_SAME_AS_CONTACT,Swipe.UP,3);
        driverTools.click(TOGGLE_SAME_AS_CONTACT);
        driverTools.waitForElementToAppear(PASSENGER_DETAIL_DOB,10);
        driverTools.clickWithJavascriptExecutor(PASSENGER_DETAIL_DOB);
        driverTools.click(SAVE_DOB_BTN);
        driverTools.click(SAVE_PASSENGER_DETAIL_BTN);
        driverTools.switchToNative();
    }

    public void fillPassengerData(int passenger){
        driverTools.swipeUp(2);
        driverTools.switchToWebViewUrl("bus-travel/booking");
        //fill other passengers
        fillGuestDetail(passenger);
        driverTools.switchToNative();
    }

    private void fillGuestDetail(int passenger){
        for(int i = 0; i < passenger; i++){
            // Access the passenger card by its index; the card list may change after each passenger is filled
            // Re-fetch element right before clicking to avoid stale element exceptions
            clickPassengerCardWithRetry(i);
            driverTools.waitForElementToAppear(PASSENGER_DETAIL_DOB);
            driverTools.click(PASSENGER_MR_SALUTATION_BTN);
            if(!(driverTools.getValueAttribute(PASSENGER_FULLNAME_INPUT_BAR, "value").length() > 3)) {
                driverTools.sendKeys(PASSENGER_FULLNAME_INPUT_BAR, CoreFunctions.randomName());
            }
            driverTools.waitForElementToPresence(PASSENGER_DETAIL_DOB).orElseThrow();
            driverTools.clickWithJavascriptExecutor(PASSENGER_DETAIL_DOB);
            driverTools.waitForElementToAppear(YEAR_DOB_PICKER);
            driverTools.swipeDown(YEAR_DOB_PICKER, 10);
            driverTools.click(SAVE_DOB_BTN);
            driverTools.click(SAVE_PASSENGER_DETAIL_BTN);
            driverTools.waitForElementToDisappear(SAVE_PASSENGER_DETAIL_BTN);
        }
    }
    
    /**
     * Clicks a passenger card by index with retry logic to handle stale element exceptions.
     * Re-fetches the element right before clicking to avoid stale references.
     * 
     * @param passengerIndex the index of the passenger card to click
     */
    private void clickPassengerCardWithRetry(int passengerIndex) {
        final int MAX_RETRIES = 3;
        int retryCount = 0;
        driverTools.swipeDown();
        while (retryCount < MAX_RETRIES) {
            try {
                // Re-fetch element right before each operation to avoid stale references
                WebElement passengerCard = getPassengerCardElement(passengerIndex);
                driverTools.waitForElementToAppear(passengerCard);
                
                // Re-fetch again right before clicking to ensure fresh reference
                // (DOM may have changed during waitForElementToAppear)
                passengerCard = getPassengerCardElement(passengerIndex);
                driverTools.clickWithJavascriptExecutor(passengerCard);
                
                // Success - exit retry loop
                return;
                
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                retryCount++;
                logger.softWarning("Stale element exception when clicking passenger card " + passengerIndex + 
                                 " (retry " + retryCount + " of " + MAX_RETRIES + ")");
                
                if (retryCount >= MAX_RETRIES) {
                    logger.fail("Failed to click passenger card after " + MAX_RETRIES + " retries due to stale element");
                    throw new RuntimeException("Unable to click passenger card at index " + passengerIndex + 
                                             " after " + MAX_RETRIES + " retries. DOM may be changing frequently.", e);
                }
                
                // Small delay before retry to allow DOM to stabilize
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting to retry passenger card click", ie);
                }
            }
        }
    }

    private WebElement getPassengerCardElement(int passenger){
        List<WebElement> passengerCards = driverTools.elements(PASSENGER_CARDS).orElseThrow();
        logger.log("passenger cards available: "+passengerCards.size());
        logger.log("picking for passenger on index: "+passenger);
        
        // Validate index bounds before accessing
        if (passenger < 0 || passenger >= passengerCards.size()) {
            throw new IndexOutOfBoundsException(
                String.format("Passenger index %d is out of bounds. Available cards: %d", 
                    passenger, passengerCards.size()));
        }
        
        return passengerCards.get(passenger);
    }

    public String getBookingPagePriceCurrency(){
        driverTools.switchToWebViewUrl("bus-travel/booking");
        driverTools.swipeToElement(BOOKING_FORM_FINAL_PRICE, Swipe.UP, 3);
        String finalPrice = driverTools.getText(BOOKING_FORM_FINAL_PRICE).orElseThrow();
        String finalPriceCurrency = finalPrice.split(" ")[0];
        driverTools.switchToNative();
        return finalPriceCurrency;
    }

    public void continueToPaymentPage(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("bus-travel/booking");
        driverTools.swipeToElement(CONTINUE_TO_PAYMENT_BTN, Swipe.UP, 3);
        driverTools.scrollIntoWebView(CONTINUE_TO_PAYMENT_BTN);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(CONTINUE_TO_PAYMENT_BTN);
        driverTools.click(CONFIRM_CONTINUE_TO_PAYMENT_BTN);
        try {
            driverTools.waitForElementToAppear(PRICE_CHANGE_POPUP, 5);
            driverTools.waitForElementToAppear(CONFIRM_PRICE_CHANGE_BTN).ifPresent(driverTools::clickWithJavascriptExecutor);
        }catch (Exception e){
            logger.log("Price change not found");
        }
        driverTools.switchToNative();
    }

    private String getChangedPrice(String description){
        String[] arr = description.split(" ");
        String oldPrice = null,newPrice=null;
        for(int i=0;i<arr.length;i++){
            if(arr[i].equals("IDR")){
                if(oldPrice == null){
                    oldPrice = arr[i] + " " + arr[i+1];
                }else{
                    newPrice = arr[i] + " " + arr[i+1];
                }
            }
        }
        logger.log("old price : " + oldPrice + "changed to new price : " + newPrice);
        return newPrice;
    }

    public String continueToPaymentPageAndGetPriceChange(){
        driverTools.switchToWebViewUrl("bus-travel/booking");
        WebElement continuePaymentBtn = driverTools.swipeToElement(CONTINUE_TO_PAYMENT_BTN, Swipe.UP, 3).orElseThrow();
        driverTools.click(continuePaymentBtn);
        driverTools.click(CONFIRM_CONTINUE_TO_PAYMENT_BTN);
        if(driverTools.waitForElementToAppear(CONFIRM_PRICE_CHANGE_BTN, 5).isPresent()){
            WebElement priceChangeDesc = driverTools.waitForElementToAppear(PRICE_CHANGE_DESCRIPTION).orElseThrow();
            String priceChangeDescText = driverTools.getText(priceChangeDesc);
            driverTools.clickWithJavascriptExecutor(CONFIRM_PRICE_CHANGE_BTN);
            return getChangedPrice(priceChangeDescText);
        }
        driverTools.switchToNative();
        return null;
    }

    public String getBusPassengerNameDetail() {
        driverTools.switchToWebViewUrl("bus-travel/booking");
        String[] busDetail = driverTools.elements(PASSENGER_NAME).orElseThrow().stream().map(WebElement::getText).toArray(String[]::new);
        String result = String.join(", ", busDetail);
        logger.log("Bus Detail : " + result);
        driverTools.switchToNative();
        return result;
    }

    public String getSeatNoDetail() {
        driverTools.switchToWebViewUrl("bus-travel/booking");
        driverTools.swipeUp();
        String[] seatNoDetail = driverTools.elements(BUS_SEAT_NO_DETAIL).orElseThrow().stream().map(WebElement::getText).toArray(String[]::new);
        String result = String.join(", ", seatNoDetail);
        logger.log("Seat Number : " + result);
        driverTools.switchToNative();
        return result;
    }

    public String getDepartureDate() {
        driverTools.switchToWebViewUrl("bus-travel/booking");
        String departureDate =  driverTools.getText(DEPARTURE_DATE).orElseThrow();
        String result = String.join(", ", departureDate);
        logger.log("Departure Date : " + result);
        driverTools.switchToNative();
        return result;
    }

    public String getReturnDate() {
        driverTools.switchToWebViewUrl("bus-travel/booking");
        driverTools.swipeRight(RETURN_DATE,2);
        String returnDate = driverTools.getText(RETURN_DATE).orElseThrow();
        String result = String.join(", ", returnDate);
        logger.log("Return Date : " + result);
        driverTools.switchToNative();
        return result;
    }

    public String getTotalPrice() {
        driverTools.switchToWebViewUrl("bus-travel/booking");
        String finalPrice = driverTools.getText(BOOKING_FORM_FINAL_PRICE).orElseThrow();
        String result = String.join(", ", finalPrice);
        logger.log("Final Price : " + result);
        driverTools.switchToNative();
        return result;
    }

    public boolean verifyBookingFormSummary(BusSrpOneWayData busSrpData){
        driverTools.switchToWebViewUrl("bus-travel/booking");
        String departureDate = driverTools.getText(DEPARTURE_DATE).orElseThrow();
        logger.log("departure date booking form : " + departureDate);
        String departureTime = driverTools.getText(DEPARTURE_TIME).orElseThrow();
        logger.log("departure time booking form : " + departureTime);
        String departureBusClass = driverTools.getText(DEPARTURE_BUS_CLASS).orElseThrow();
        logger.log("departure bus class booking form : " + departureBusClass);
        String departureOperatorName = driverTools.getText(DEPARTURE_OPERATOR_NAME).orElseThrow();
        logger.log("departure bus operator name booking form: " + departureOperatorName);
        String departureOrigin = driverTools.getText(DEPARTURE_ORIGIN).orElseThrow();
        logger.log("departure origin booking form: " + departureOrigin);
        String departureDestination = driverTools.getText(DEPARTURE_DESTINATION).orElseThrow();
        logger.log("departure destination booking form: " + departureDestination);
        driverTools.switchToNative();
        Assertion.assertThat("Verify departure origin booking form equal to srp", departureOrigin, equalTo(busSrpData.origin));
        Assertion.assertThat("Verify departure destination booking form equal to srp", departureDestination, equalTo(busSrpData.destination));
        Assertion.assertThat("Verify departure date booking form equal to srp", departureDate, containsString(busSrpData.departDate));
        Assertion.assertThat("Verify departure time booking form equal to srp", departureTime, equalTo(busSrpData.departTime));
        Assertion.assertThat("Verify departure operator name booking form equal to srp", departureOperatorName, equalTo(busSrpData.operatorName));
        Assertion.assertThat("Verify departure bus class booking form equal to srp", departureBusClass, IsSubstringOfMatcher.isSubsStringOf(busSrpData.busClass));
        return true;
    }

    public String getPassengerName(){
        driverTools.switchToWebViewUrl("bus-travel/booking");
        WebElement passengerNameElement = driverTools.waitForElementToAppear(PASSENGER_NAME_BOOKING_PAGE).orElseThrow();
        String passengerName = driverTools.getText(passengerNameElement);
        logger.log("passenger Name : " + passengerName);
        driverTools.switchToNative();
        return passengerName;
    }

    public String getBusOperatorName(){
        driverTools.switchToWebViewUrl("bus-travel/booking");
        WebElement operatorNameElement = driverTools.waitForElementToAppear(DEPARTURE_OPERATOR_NAME).orElseThrow();
        String operatorName = driverTools.getText(operatorNameElement);
        logger.log("operator Name : " + operatorName);
        driverTools.switchToNative();
        return operatorName;
    }

    public String getBusClassName(){
        driverTools.switchToWebViewUrl("bus-travel/booking");
        WebElement busClassElement = driverTools.waitForElementToAppear(DEPARTURE_BUS_CLASS).orElseThrow();
        String busClass = driverTools.getText(busClassElement);
        logger.log("bus class : " + busClass);
        driverTools.switchToNative();
        return busClass;
    }

    public void openPriceDetail(){
        driverTools.switchToWebViewUrl("bus-travel/booking");
        driverTools.swipeToElement(BOOKING_FORM_FINAL_PRICE, Swipe.UP, 5).orElseThrow();
        driverTools.click(BOOKING_FORM_FINAL_PRICE);
        driverTools.switchToNative();
    }

    public String getBookingPagePrice(){
        driverTools.switchToWebViewUrl("bus-travel/booking");
        driverTools.swipeUp(3);
        String finalPrice = driverTools.getText(BOOKING_FORM_FINAL_PRICE).orElseThrow();
        driverTools.switchToNative();
        return finalPrice;
    }

    public boolean verifyBookingFormPriceDetail(String priceTotalBookingPage){
        driverTools.switchToWebViewUrl("bus-travel/booking");
        String actualTotalPrice = driverTools.getText(BOOKING_FORM_FINAL_PRICE).orElseThrow();
        //compare price total in booking page before expanding the price detail section should be the same as price detail
        Assertion.assertThat("Verify total price in price detail is the same as total price in booking form", actualTotalPrice, equalTo(priceTotalBookingPage));
        //verify sum price component 
        List<String> priceBreakDownComponent = driverTools.getNumberTexts(PRICE_DETAIL_COMPONENTS);

        if (priceBreakDownComponent.isEmpty()) {
            logger.log("WARNING: No price components found. Cannot verify price breakdown.");
            throw new AssertionError("No price components found in PRICE_DETAIL_COMPONENTS");
        }

        logger.log("number of price component present : " + priceBreakDownComponent.size());
        double sumOfPriceDetail = CoreFunctions.sumUpStringNumbers(priceBreakDownComponent);
        double actualTotalPriceDouble = CoreFunctions.convertPriceSafely(actualTotalPrice);
        boolean isEqual = Double.compare(actualTotalPriceDouble, sumOfPriceDetail) == 0;
        Assertion.assertThat("Verify price detail total is the same as ",actualTotalPriceDouble , equalTo(sumOfPriceDetail));
        driverTools.switchToNative();
        return isEqual;
    }

}
