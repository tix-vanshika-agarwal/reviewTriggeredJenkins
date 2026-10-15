package com.tiket.page.train.bookingform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.common.InsuranceGeneralData;
import com.tiket.pojo.train.TrainData;
import com.tiket.pojo.train.TrainPriceDetail;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class TrainBookingPageAndroid<T extends WebDriver> extends TrainBookingPage<T> {

    public static final Locator PHONE = new Locator("PHONE", AppiumBy.xpath("//*[contains(@resource-id,'tf_phone') or contains(@resource-id,'tds_phone_number_field_layout')]//android.widget.EditText"));
    public static final Locator TITLE_RADIO = new Locator("TITLE_RADIO", AppiumBy.xpath("//*[contains(@resource-id,'vg_radio_view')]//*"));
    public static final Locator FULL_NAME = new Locator("FULL_NAME", AppiumBy.id("tds_box_autocomplete_input_text"));
    public static final Locator ID_NUMBER = new Locator("ID_NUMBER", AppiumBy.xpath("//*[contains(@resource-id,'tds_box_input_text') and (contains(@hint,'ID') or contains(@hint,'Nomor'))]"));
    public static final Locator DOB_PICKER = new Locator("ID_NUMBER", AppiumBy.xpath("//*[contains(@resource-id,'tds_box_input_text') and (contains(@hint,'Birth') or contains(@hint,'Lahir'))]"));
    public static final Locator YEAR_PICKER_WHEEL = new Locator("PASSPORT_EXPIRE_YEAR", AppiumBy.id("tds_rv_right_picker"));
    public static final Locator CURRENT_YEAR_PICKER_WHEEL = new Locator("CURRENT_YEAR_PICKER_WHEEL", AppiumBy.xpath("(//*[contains(@resource-id,'tds_rv_right_picker')]//*[contains(@resource-id,'tds_body_picker')])[2]"));
    public static final Locator DOB_SUBMIT = new Locator("DOB_SUBMIT", AppiumBy.id("tds_btn_submit_picker"));
    public static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", AppiumBy.id("tds_button_view"));
    public static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON", AppiumBy.id("iv_close"));
    public static final Locator CLOSE_SEAT_BUTTON = new Locator("CLOSE_SEAT_BUTTON", AppiumBy.id("iv_select_seat_close"));
    public static final Locator SAME_AS_CONTACT_DETAIL_SWITCH = new Locator("SAME_AS_CONTACT_DETAIL_SWITCH", AppiumBy.id("tds_toggle_group"));
    public static final Locator SELECT_SEAT_BUTTON = new Locator("SELECT_SEAT_BUTTON", AppiumBy.id("btn_change_seat"));
    public static final Locator CONTINUE_PAYMENT_BUTTON = new Locator("CONTINUE_PAYMENT_BUTTON",AppiumBy.xpath("//*[contains(@resource-id,'btn_action')]//*[contains(@resource-id,'tds_button_view')]"));
    public static final Locator CONFIRM_PAYMENT_BUTTON = new Locator("CONFIRM_PAYMENT_BUTTON",AppiumBy.xpath("//*[contains(@resource-id,'tds_btn_primary_dialog') or contains(@resource-id,'tds_btn_primary_info_view')]"));
    public static final Locator EMPTY_SEAT = new Locator("EMPTY_SEAT",AppiumBy.xpath("//*[contains(@resource-id,'seat_state') and not(following-sibling::*)]"));
    public static final Locator ORIGIN_STATION = new Locator("ORIGIN_STATION",AppiumBy.xpath("(//*[contains(@resource-id,'ps_origin_departure')]//android.widget.TextView)[1]"));
    public static final Locator SEAT_CONTAINER = new Locator("SEAT_CONTAINER",AppiumBy.id("vp_train_wagon"));
    public static final Locator NEXT_BOGGY_ARROW = new Locator("NEXT_BOGGY_ARROW",AppiumBy.id("iv_next_arrow"));
    public static final Locator BACK_BOGGY_ARROW = new Locator("BACK_BOGGY_ARROW",AppiumBy.id("iv_back_arrow"));
    public static final Locator TITLE_GUEST = new Locator("TITLE_RADIO", AppiumBy.xpath("//*[contains(@resource-id,'vg_radio_view')]//android.widget.TextView[@text='Mr.' or @text='Tuan']"));
    private static final Locator EMAIL_FIELD = new Locator("EMAIL_FIELD", AppiumBy.xpath("//android.widget.EditText[contains(@text,'Email')]"));
    private static final Locator LOGIN_CONTACT_DETAIL_EMAIL_FIELD = new Locator("LOGIN_CONTACT_DETAIL_EMAIL_FIELD", AppiumBy.xpath("//android.widget.EditText[contains(@hint,'Email')]"));
    private static final Locator BUTTON = new Locator("BUTTON",AppiumBy.id("tds_button_view"));
    private static final Locator ARROW_BTN_DETAIL = new Locator("ARROW_BTN_DETAIL",AppiumBy.xpath("(//*[contains(@resource-id,'iv_train_booking_seat_detail_arrow')])[2]"));
    private static final Locator CHANGE_SEAT_BTN_RETURN = new Locator("CHANGE_SEAT_BTN_RETURN",AppiumBy.xpath("(//*[contains(@resource-id,'btn_change_seat')])[2]"));
    private static final Locator FULL_TRIP_PROTECTION = new Locator("FULL_TRIP_PROTECTION",AppiumBy.xpath("//*[contains(@text,'Protection') or contains(@text,'Perlindungan')]/following-sibling::*[contains(@resource-id,'card_compound_button')]//*[@text='Add' or @text='Tambahkan']"));
    private static final Locator SEAT_TITLE = new Locator("SEAT_TITLE",AppiumBy.id("'tv_select_seat_title"));
    private static final Locator PRICE_DETAIL_ARROW = new Locator("PRICE_DETAIL_ARROW",AppiumBy.xpath("//*[contains(@resource-id,'tv_left') and contains(@text,'Sub')]//following-sibling::*[contains(@resource-id,'iv_chevron')]"));
    private static final Locator TOTAL_PRICE_PRICE_DETAIL = new Locator("TOTAL_PRICE_PRICE_DETAIL",AppiumBy.id("tv_total_price"));
    public static final Locator LABEL_EXTRA = new Locator("LABEL_EXTRA",AppiumBy.xpath("//*[contains(@resource-id,'tv_description') and (contains(@text,'Protection') or contains(@text,'Perlindungan Penuh'))]"));
    private static final Locator ORIGIN_STATION_NAME = new Locator("ORIGIN_STATION_NAME",AppiumBy.id("tv_origin_station"));
    private static final Locator DESTINATION_STATION_NAME = new Locator("DESTINATION_STATION_NAME",AppiumBy.xpath("(//*[contains(@resource-id,'ps_origin_departure')]//android.widget.TextView)[2]"));
    private static final Locator DATE_DEPARTURE = new Locator("DATE_DEPARTURE",AppiumBy.id("tv_departure_date")); @Deprecated
    private static final Locator TRAIN_NAME_AND_WAGON_NAME = new Locator("TRAIN_NAME_AND_WAGON_NAME",AppiumBy.id("tv_train_name_and_wagon_class")); @Deprecated
    private static final Locator PASSENGER_NAME = new Locator("PASSENGER_NAME",AppiumBy.id("tv_passenger_name"));
    private static final Locator SEAT_DETAIL = new Locator("SEAT_DETAIL",AppiumBy.id("tv_seat_detail"));
    private static final Locator TOTAL_PRICE_BOOKING_PAGE = new Locator("TOTAL_PRICE_BOOKING_PAGE",AppiumBy.xpath("//*[contains(@resource-id,'tv_left') and (contains(@text,'Total bayar') or contains(@text,'Total to pay'))]/following-sibling::*[contains(@resource-id,'tv_right')]"));
    private static final Locator PRICE_PRICE_DETAIL = new Locator("PRICE_PRICE_DETAIL",AppiumBy.xpath("//*[contains(@resource-id,'tv_left') and not(contains(@text,'Total bayar') or contains(@text,'Total to pay') or contains(@text,'Sub'))]/following-sibling::*[contains(@resource-id,'tv_right')]"));
    private static final Locator FULL_PROTECTION_PICKED = new Locator("FULL_PROTECTION_PICKED",AppiumBy.xpath("//*[contains(@text,'Protection') or contains(@text,'Perlindungan') or contains(@text,'Refund')]/following-sibling::*[contains(@resource-id,'card_compound_button')]//*[@text='Batalkan' or @text='Cancel']"));
    private static final Locator BTN_JOIN_NOW = new Locator("BTN_JOIN_NOW", AppiumBy.xpath("//android.widget.TextView[contains(@text,'Log in')]"));
    private static final Locator CONTACT_CARD = new Locator("CONTACT_CARD", AppiumBy.id("field_contact_card"));
    private static final Locator INSURANCE_BUTTON = new Locator("INSURANCE_BUTTON", AppiumBy.id("card_compound_button"));
    private static final Locator EDIT_PASSENGER = new Locator("EDIT_PASSENGER", AppiumBy.xpath("//*[contains(@resource-id,'vg_train_booking_passenger')]//*[contains(@resource-id,'body_icon')]"));
    private static final Locator INSURANCE_PRICE = new Locator("INSURANCE_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'insurance')]//*[contains(@resource-id,'tv_') and contains(@resource-id,'_price')]"));
    private static final Locator FIRST_PAGE_STEPPER = new Locator("FIRST_PAGE_STEPPER", AppiumBy.xpath("//*[contains(@resource-id,'tds_stepper_item_container') and *[contains(@text,'1')]]"));
    private static final Locator SECOND_PAGE_STEPPER = new Locator("SECOND_PAGE_STEPPER", AppiumBy.xpath("//*[contains(@resource-id,'tds_stepper_item_container') and *[contains(@text,'2')]]"));
    private static final Locator FIRST_INSURANCE_EXTRA_PROTECTION_TITLE = new Locator("FIRST_INSURANCE_EXTRA_PROTECTION_TITLE", AppiumBy.xpath("(//*[contains(@resource-id,'iv_card_compound')]//following-sibling::*[contains(@resource-id,'tv_title')])[1]"));
    private static final Locator FIRST_INSURANCE_EXTRA_PROTECTION_ADD_CANCEL_BTN = new Locator("FIRST_INSURANCE_EXTRA_PROTECTION_ADD_CANCEL_BTN", AppiumBy.xpath("(//*[contains(@resource-id,'iv_card_compound')]//following-sibling::*[contains(@resource-id,'tv_title')])[1]//following-sibling::*//*[contains(@resource-id,'tds_button_view')]"));
    private static final Locator FIRST_INSURANCE_EXTRA_PROTECTION_BASE_PRICE = new Locator("FIRST_INSURANCE_EXTRA_PROTECTION_BASE_PRICE", AppiumBy.xpath("(//*[contains(@resource-id,'iv_card_compound')]//following-sibling::*[contains(@resource-id,'tv_title')])[1]//following-sibling::*//*[contains(@resource-id,'tv_base_price')]"));
    private static final Locator ROUND_TRIP_FLAG_PRICE_SUMMARY = new Locator("ROUND_TRIP_FLAG_PRICE_SUMMARY", AppiumBy.xpath("//*[contains(@resource-id,'tv_left') and (contains(@text,'Adult') or contains(@text,'Dewasa'))]/following-sibling::*[contains(@resource-id,'tv_right')]"));

    public TrainBookingPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void fillGuestDetail(String email){
       if(driverTools.swipeToElement(TITLE_GUEST,Swipe.UP,4).isPresent()) {
            driverTools.click(TITLE_GUEST);
            driverTools.swipeToElement(FULL_NAME,Swipe.UP,4).orElseThrow();
            fillName();
            driverTools.swipeToElement(PHONE,Swipe.UP,4).orElseThrow();
            fillPhone();
            driverTools.swipeToElement(EMAIL_FIELD,Swipe.UP,4).orElseThrow();
            driverTools.click(EMAIL_FIELD);
            driverTools.sendKeys(EMAIL_FIELD,email);
            driverTools.hideKeyboard(1);
        }
    }

    @Override
    public TrainData fillPassengerDetail(int adults, int infants) {
        logger.log("number of adult : " + adults +"\nnumber of infant : " + infants);
        TrainData trainData = new TrainData();
        ArrayList<String> passengerNames = new ArrayList<>();
        ArrayList<String> idList = new ArrayList<>();

        ArrayList<String> toggledFirstNameAndId = toggleSameAsContact();

        driverTools.swipeToElement(BUTTON, Swipe.UP,2);

        for(int x=2;x<=(adults+infants);x++){
            Locator passengerButton = getPassengerButton(x);
            driverTools.click(passengerButton);
            if(driverTools.waitForElementToAppear(TITLE_RADIO,3).isEmpty()){
                driverTools.click(TITLE_RADIO);
            }

            logger.log("Filling detail for passenger " + x);

            driverTools.waitForElementToAppear(CLOSE_BUTTON);
            fillTitle();
            passengerNames.add(fillName());
            if(x>adults){
                fillDob();
            }
            idList.add(fillId());
            driverTools.click(SAVE_BUTTON);
        }
        passengerNames.add(0,toggledFirstNameAndId.get(0));
        idList.add(0,toggledFirstNameAndId.get(1));
        trainData.guestList = passengerNames;
        trainData.idNumberList = idList;
        return trainData;
    }
    private Locator getPassengerButton(int index){
        return new Locator("PASSENGER_BUTTON_"+index,AppiumBy.xpath("//*[contains(@text,'"+index+"') and (contains(@text,'Passenger') or contains(@text,'Penumpang'))]"));
    }

    private ArrayList<String> toggleSameAsContact(){
        driverTools.swipeToElement(SAME_AS_CONTACT_DETAIL_SWITCH,Swipe.UP,5);
        driverTools.click(SAME_AS_CONTACT_DETAIL_SWITCH);
        if(driverTools.waitForElementToAppear(TITLE_RADIO,3).isEmpty()){
            if(driverTools.waitForElementToAppear(SAVE_BUTTON).isPresent()){
                driverTools.click(SAVE_BUTTON);
            }
        }
        String id = fillId();
        WebElement nameField = driverTools.waitForElementToPresence(FULL_NAME,3).orElseThrow();
        String fullName = driverTools.getText(nameField);
        driverTools.click(SAVE_BUTTON);
        ArrayList<String> list = new ArrayList<>();
        list.add(fullName);
        list.add(id);
        return list;
    }

    private void fillTitle(){
        driverTools.waitForElementToPresence(TITLE_RADIO);
        List<WebElement> titleRadio = driverTools.waitForPresenceElementsList(TITLE_RADIO,3).orElseThrow();
        driverTools.click(titleRadio.get(CoreFunctions.generateRandomInt(0,titleRadio.size()-3)));
    }

    private String fillName(){
        driverTools.click(FULL_NAME);
        WebElement nameField = driverTools.waitForElementToPresence(FULL_NAME,3).orElseThrow();
        String passengerName = CoreFunctions.randomName() +" Test";
        driverTools.sendKeys(nameField,passengerName);
        driverTools.hideKeyboard();
        return passengerName;
    }

    private void fillPhone(){
        String phone = "8" + CoreFunctions.randomInt(10);
        driverTools.click(PHONE);
        // Native TDS phone field already shows "+62"; sendKeys is unreliable here.
        // pressKey types digits via Android KeyEvent so the value becomes +628...
        driverTools.pressKey(PHONE, phone);
        driverTools.hideKeyboard();
    }

    private String fillId(){
        String id = String.valueOf(CoreFunctions.randomInt(7));
        WebElement idField = driverTools.waitForElementToPresence(ID_NUMBER,3).orElseThrow();
        String idText = "32720220"+id+"1";
        driverTools.sendKeys(idField,idText);
        driverTools.click(ID_NUMBER);
        driverTools.hideKeyboard();
        return idText;
    }

    private void fillDob () {
        if(driverTools.waitForElementToPresence(DOB_PICKER,3).isEmpty())
            return;
        driverTools.click(DOB_PICKER);
        WebElement dobSubmit = driverTools.waitForElementToPresence(DOB_SUBMIT,3).orElseThrow();
        driverTools.click(dobSubmit);
    }

    private void selectSeatSteps(String tripType, int adults){
        WebElement seatBtn = driverTools.waitForElementToAppear(SELECT_SEAT_BUTTON,15).orElseThrow();
        driverTools.click(seatBtn);
        driverTools.waitForElementToAppear(CLOSE_SEAT_BUTTON).orElseThrow();

        logger.log("Changing seat for departure trip");

        changeSeat(adults);

        if(tripType.equals("roundtrip")){
            driverTools.click(SAVE_BUTTON);
            logger.log("Changing seat for return trip");
            if(!driverTools.isElementDisplayed(CHANGE_SEAT_BTN_RETURN))
                driverTools.click(ARROW_BTN_DETAIL);
            WebElement changeReturnSeat = driverTools.swipeToElement(CHANGE_SEAT_BTN_RETURN,Swipe.UP,2).orElseThrow();
            driverTools.click(changeReturnSeat);
            changeSeat(adults);
        }
        driverTools.click(SAVE_BUTTON);
    }
    @Override
    public void selectSeat(String tripType, int adults) {
        selectSeatSteps(tripType,adults);
    }

    /**
     * Selects seats for the specified number of adults.
     * This method searches for empty seats across multiple boggies (train cars) if needed.
     * 
     * @param adults the number of adult passengers requiring seats
     * @throws RuntimeException if no empty seats are found after checking all available boggies
     */
    private void changeSeat(int adults){
        for(int passengerIndex = 0; passengerIndex < adults; passengerIndex++){
            logger.log("Selecting seat for adult " + (passengerIndex + 1) + " of " + adults);
            
            boolean seatFound = findAndSelectSeatInAvailableBoggies();
            
            if (!seatFound) {
                throw new RuntimeException("No empty seats found after checking multiple boggies");
            }
            
            selectEmptySeat();
        }
    }
    
    /**
     * Searches for empty seats across available boggies.
     * Tries the current boggy first, then attempts to change to other boggies if needed.
     * 
     * @return true if empty seats are found, false otherwise
     */
    private boolean findAndSelectSeatInAvailableBoggies() {
        final int MAX_BOGGY_CHANGES = 3;
        final int MAX_TRIES_PER_BOGGY = 2;
        
        // Check current boggy first
        if (checkEmptySeat()) {
            logger.log("Empty seats found immediately in current boggy");
            return true;
        }
        
        // Try swiping in current boggy
        if (searchSeatsBySwiping(MAX_TRIES_PER_BOGGY)) {
            return true;
        }
        
        // If no seats found, try other boggies
        for (int boggyChange = 0; boggyChange < MAX_BOGGY_CHANGES; boggyChange++) {
            logger.log("No empty seats found in current boggy, attempting to change to next boggy");
            
            if (!changeBoggy()) {
                logger.log("Could not change boggy or no more boggies available");
                return false;
            }
            
            // Check new boggy
            if (checkEmptySeat()) {
                logger.log("Empty seats found in new boggy");
                return true;
            }
            
            // Try swiping in new boggy
            if (searchSeatsBySwiping(MAX_TRIES_PER_BOGGY)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Searches for empty seats by swiping in different directions.
     * Uses alternating vertical swipes (up/down) and horizontal swipes (left).
     * 
     * @param maxTries maximum number of swipe attempts
     * @return true if empty seats are found, false otherwise
     */
    private boolean searchSeatsBySwiping(int maxTries) {
        boolean swipeUp = true;
        
        for (int tryCount = maxTries; tryCount > 0; tryCount--) {
            // Perform vertical swipe (alternating up/down)
            if (swipeUp) {
                driverTools.swipeUpPresence(SEAT_CONTAINER);
            } else {
                driverTools.swipeDownPresence(SEAT_CONTAINER);
            }
            swipeUp = !swipeUp; // Toggle direction for next iteration
            
            // Check if seats are now visible
            if (checkEmptySeat()) {
                return true;
            }
            
            // Perform horizontal swipe (except on last try)
            if (tryCount > 1) {
                driverTools.swipeLeftPresence(SEAT_CONTAINER);
                swipeUp = true; // Reset to swipe up after horizontal swipe
            }
        }
        
        return false;
    }

    private boolean changeBoggy(){
        try {
            // Check if next arrow is present and try to click it
            Optional<WebElement> nextArrow = driverTools.waitForElementToPresence(NEXT_BOGGY_ARROW, 3);
            if (nextArrow.isPresent()) {
                WebElement arrowElement = nextArrow.get();
                logger.log("Next boggy arrow found, attempting to click");
                
                // Try direct click first
                try {
                    if (arrowElement.isDisplayed() && arrowElement.isEnabled()) {
                        driverTools.click(arrowElement);
                        logger.log("Successfully clicked next boggy arrow using direct click");
                    } else {
                        throw new Exception("Arrow element not displayed or enabled");
                    }
                } catch (Exception e) {
                    // If direct click fails (element might have clickable: false), try clicking by coordinates
                    logger.log("Direct click failed (" + e.getMessage() + "), attempting coordinate-based click");
                    try {
                        driverTools.clickElementByCoordinate(arrowElement);
                        logger.log("Successfully clicked next boggy arrow using coordinate-based click");
                    } catch (Exception coordEx) {
                        logger.log("Coordinate-based click also failed: " + coordEx.getMessage());
                        return false;
                    }
                }
                
                // Wait for new boggy to load (wait for seat container to refresh)
                logger.log("Waiting for new boggy to load");
                driverTools.waitForElementToPresence(SEAT_CONTAINER, 5);
                return true;
            } else {
                logger.log("Next boggy arrow not found - no more boggies available");
                return false;
            }
        } catch (Exception e) {
            logger.log("Error changing boggy: " + e.getMessage());
            return false;
        }
    }

    private boolean checkEmptySeat(){
        // Fast check: just verify if any empty seat exists (no height validation for speed)
        Optional<WebElement> firstElement = driverTools.waitForElementToPresence(EMPTY_SEAT, 2);
        return firstElement.isPresent();
    }

    private void selectEmptySeat(){
        // Fast selection: get elements and click first valid one immediately (from end for visibility)
        List<WebElement> e = driverTools.waitForPresenceElementsList(EMPTY_SEAT).orElseThrow();
        logger.log("Found " + e.size() + " empty seats, selecting first valid one");
        
        // Process from end (most visible) and click first valid seat immediately
        int maxToCheck = Math.min(e.size(), 5); // Check only last 5 seats for speed
        int staleElementRetryCount = 0;
        final int MAX_STALE_ELEMENT_RETRIES = 3; // Prevent infinite loop
        
        for (int i = e.size() - 1; i >= Math.max(0, e.size() - maxToCheck); i--) {
            try {
                // Quick height check - only validate if height > 20 (not cropped)
                int height = driverTools.getElementHeight(e.get(i));
                if (height > 20) {
                    logger.log("Selecting seat at index: " + i);
                    driverTools.click(e.get(i));
                    return;
                }
            } catch (org.openqa.selenium.StaleElementReferenceException ex) {
                staleElementRetryCount++;
                if (staleElementRetryCount > MAX_STALE_ELEMENT_RETRIES) {
                    logger.softWarning("Too many stale element exceptions (" + staleElementRetryCount + 
                                      "), breaking loop to prevent infinite retry");
                    break; // Exit loop to prevent infinite retry
                }
                
                // If stale, re-fetch and restart from end
                logger.softWarning("Stale element detected (retry " + staleElementRetryCount + 
                                 "), re-fetching elements");
                e = driverTools.waitForPresenceElementsList(EMPTY_SEAT).orElseThrow();
                
                // Reset loop to start from the end again
                maxToCheck = Math.min(e.size(), 5);
                i = e.size(); // Set to size so that after i-- it becomes size-1 (last element)
                continue;
            } catch (Exception ex) {
                continue; // Skip any other errors
            }
        }
        
        // Fallback: try clicking last element without height check if all checks failed
        // Make check and access atomic to prevent IndexOutOfBoundsException
        if (!e.isEmpty()) {
            try {
                // Store size atomically to prevent race conditions
                int lastIndex = e.size() - 1;
                // Re-verify list is still not empty before access (DOM might have changed)
                if (lastIndex >= 0 && lastIndex < e.size()) {
                    logger.log("Fallback: selecting last available seat at index " + lastIndex);
                    driverTools.click(e.get(lastIndex));
                    return;
                }
            } catch (IndexOutOfBoundsException ex) {
                logger.softWarning("Fallback: IndexOutOfBoundsException - list changed during access, re-fetching");
                // Re-fetch list and try again
                try {
                    e = driverTools.waitForPresenceElementsList(EMPTY_SEAT).orElseThrow();
                    if (!e.isEmpty()) {
                        int lastIndex = e.size() - 1;
                        logger.log("Fallback: selecting last available seat after re-fetch at index " + lastIndex);
                        driverTools.click(e.get(lastIndex));
                        return;
                    }
                } catch (Exception retryEx) {
                    logger.softWarning("Fallback: retry also failed: " + retryEx.getMessage());
                }
            } catch (Exception ex) {
                logger.softWarning("Fallback: exception during click: " + ex.getMessage());
            }
        }
        
        throw new RuntimeException("No valid empty seats found to select");
    }
    @Override
    public void continueToPayment() {
        driverTools.swipeUp(0.8);
      driverTools.swipeToElementTillPresented(CONTINUE_PAYMENT_BUTTON,Swipe.UP,10);
      if (driverTools.isElementDisplayed(CONTINUE_PAYMENT_BUTTON)) {
        driverTools.click(CONTINUE_PAYMENT_BUTTON);  
      } else {
        logger.log("continue payment button is not found");
      }
      driverTools.click(CONFIRM_PAYMENT_BUTTON);
    }

    @Override
    public InsuranceGeneralData selectInsurance(){
        InsuranceGeneralData insuranceGeneralData = new InsuranceGeneralData();
        driverTools.swipeToElement(INSURANCE_BUTTON,Swipe.UP,5).orElseThrow();

        WebElement titleElement = driverTools.swipeToElement(FIRST_INSURANCE_EXTRA_PROTECTION_TITLE,Swipe.UP,5).orElseThrow();
        String title = driverTools.getText(titleElement);
        logger.log("title: " + title + " will be added for insurance verification");
        insuranceGeneralData.insuranceTitle = title;

        WebElement btnStateElement = driverTools.waitForElementToPresence(FIRST_INSURANCE_EXTRA_PROTECTION_ADD_CANCEL_BTN).orElseThrow();
        String btnState = driverTools.getText(btnStateElement);
        boolean isInsuranceAdded = btnState.contains("Cancel") || btnState.contains("Batalkan");
        logger.log("is insurance already added " + isInsuranceAdded);
        if(!isInsuranceAdded){
            driverTools.click(FIRST_INSURANCE_EXTRA_PROTECTION_ADD_CANCEL_BTN);
        }

        WebElement insurancePriceElement = driverTools.waitForElementToPresence(FIRST_INSURANCE_EXTRA_PROTECTION_BASE_PRICE).orElseThrow();
        String insurancePrice = removeExtraData(driverTools.getText(insurancePriceElement));
        logger.log("selected insurance price : " + insurancePrice);
        insuranceGeneralData.insurancePrice = insurancePrice;
        return insuranceGeneralData;
    }

    @Override
    public void continueTo2ndHalf(){
        WebElement viewBtn = driverTools.swipeToElement(BUTTON,Swipe.UP,2).orElseThrow();
        driverTools.click(viewBtn);
        driverTools.waitForElementToDisappear(BUTTON);
    }

    @Override
    public void openPriceDetail(String tripType){
        driverTools.swipeToElement(CONTINUE_PAYMENT_BUTTON, Swipe.UP, 10);
        WebElement priceDetailArrow = driverTools.waitForElementToAppear(PRICE_DETAIL_ARROW).orElseThrow();
        driverTools.click(priceDetailArrow);
        logger.log("Departure price detail opened");

        if("roundtrip".equalsIgnoreCase(tripType)){
            List<WebElement> flagRoundTrip = driverTools.elements(ROUND_TRIP_FLAG_PRICE_SUMMARY).orElseThrow();
            logger.log("Round trip price elements found: " + flagRoundTrip.size());

            if(flagRoundTrip.size() == 1){
                driverTools.swipeToElement(getPriceDetailArrowLocator(2), Swipe.UP, 2);
                WebElement returnPriceDetailArrow = driverTools.waitForElementToAppear(getPriceDetailArrowLocator(2)).orElseThrow();
                driverTools.click(returnPriceDetailArrow);
                driverTools.swipeToElement(CONTINUE_PAYMENT_BUTTON, Swipe.UP, 1);
                logger.log("Return price detail opened");
            }
        }
    }

    private Locator getPriceDetailArrowLocator(int index){
        return new Locator("PRICE_DETAIL_ARROW",AppiumBy.xpath("(//*[contains(@resource-id,'tv_left') and contains(@text,'→')]/following-sibling::*[contains(@resource-id,'iv_chevron')])["+index+"]"));
    }

    @Override
    public boolean isPriceSumCorrect (){
        driverTools.swipeUp();
        driverTools.waitForElementToAppear(PRICE_PRICE_DETAIL,3);
        List<WebElement> prices = driverTools.elements(PRICE_PRICE_DETAIL).orElseThrow();

        ArrayList<Double> arrayPrice = new ArrayList<Double>();
        for (WebElement element : prices) {
            String text = driverTools.getText(element);
            logger.log("text -> " + text);
            try {
                double price = CoreFunctions.convertPrice(text);
                logger.log("price -> " + price);
                arrayPrice.add(price);
            } catch (NumberFormatException ignored) {
            }
        }

        double sum = 0;
        for (double i : arrayPrice) {
            sum += i;
        }
        driverTools.swipeToElement(TOTAL_PRICE_BOOKING_PAGE,Swipe.UP,1);
        String price = driverTools.getText(TOTAL_PRICE_BOOKING_PAGE).orElseThrow();
        double totalPrice = CoreFunctions.convertPrice(price);
        logger.log("Total price text:  " + totalPrice);
        logger.log("Total price got:  " + sum);
        return totalPrice == sum;
    }



    @Override
    public boolean isPriceBreakdownCorrect(TrainPriceDetail priceDetail){
        boolean priceCorrect = isPriceSumCorrect();
        logger.log("Total price breakdown correct: " + priceCorrect);
        return  priceCorrect;
    }
    @Override
    public boolean isInsuranceShow(){
        boolean fullInsurance = driverTools.isElementDisplayed(LABEL_EXTRA);

        return fullInsurance;
    }

    @Override
    public void closePriceDetail(){
        if (driverTools.isElementDisplayed(PRICE_DETAIL_ARROW)) {
            driverTools.click(PRICE_DETAIL_ARROW);
        } else {
            logger.log("PRICE_DETAIL_ARROW is not displayed, cannot close price detail");
        }
    }

    private String convertMonthToEng(String depDate, String language){
        String[] newDate = depDate.split(" ");
        if(language.isEmpty()){
            newDate[1] = CoreFunctions.monthShortToShortIdToEng(newDate[1]);
        }

        StringBuilder convertedDate= new StringBuilder();
        for(String x:newDate){
            convertedDate.append(x);
            convertedDate.append(" ");

        }
        depDate = String.valueOf(convertedDate).substring(0,convertedDate.length()-1);
        return depDate;
    }
    private String getArrivalDate(String depDate, String startTime, String endTime, String language){
        String country = language.equalsIgnoreCase("en") ? "US" : "ID";
        Locale loc = new Locale(language.toLowerCase(), country);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM HH:mm",loc);
        SimpleDateFormat ansDateFormat = new SimpleDateFormat("dd MMMM",loc);
        try {
            // Parse the departure date and start time
            Date start = sdf.parse(depDate + " " + startTime);
            // Parse the departure date and end time
            Date end = sdf.parse(depDate + " " + endTime);
            // Check if the end time is before the start time
            if (end.before(start)) {
                // Add one day to the end date
                end.setTime(end.getTime() + 24 * 60 * 60 * 1000);
            }
            // Format the end date
            String finalDate = ansDateFormat.format(end);
            String[] newDate = finalDate.split(" ");
            logger.log("arrival date is : " + finalDate);
            newDate[1] = newDate[1].substring(0,3);
            StringBuilder convertedDate= new StringBuilder();
            for(String x:newDate){
                convertedDate.append(x);
                convertedDate.append(" ");

            }
            finalDate = String.valueOf(convertedDate).substring(0,convertedDate.length()-1);
            return finalDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String removeDayOfWeek(String date) {
        return date.replaceFirst("^.*?,\\s*","");
    }

    private String getInfoUsingLocator(Locator locator){
        driverTools.waitForElementToAppear(locator);
        return driverTools.getText(locator).orElseThrow();
    }

    private String formatDate(String date){
        String[] newDate = date.split(" ");
        StringBuilder convertedDate= new StringBuilder();

        if(newDate[0].length()!=2)
            newDate[0] = "0"+newDate[0];

        for(String x:newDate){
            convertedDate.append(x);
            convertedDate.append(" ");
        }
        date = String.valueOf(convertedDate).substring(0,convertedDate.length()-1);
        return date;
    }

    //there are some encoding problems where while getting text from element some character are displayed as something else
    //ex:"Surabaya Gubeng" is displayed as "Surabaya�Gubeng" causing test to fail
    private String replaceUnwantedChar(String data){
        return data.replaceAll("[^A-Za-z_0-9]"," ");
    }

    @Override
    public TrainData getDetailsTrainAndPassenger(String language){
        TrainData data = new TrainData();

        data.departureStation = replaceUnwantedChar(getInfoUsingLocator(ORIGIN_STATION));
        logger.log("departure station name : " + data.departureStation);

        data.arrivalStation = replaceUnwantedChar(getInfoUsingLocator(DESTINATION_STATION_NAME));
        logger.log("arrival station name : " + data.arrivalStation);

        String journeyDate = getInfoUsingLocator(DATE_DEPARTURE);
        String[] splittedJourneyDate = journeyDate.split("\u2022");
        data.departureDate = formatDate(removeDayOfWeek(splittedJourneyDate[0]));
        logger.log("departure date : " + data.departureDate);

        String[] timeArray = splittedJourneyDate[1].trim().split(" - ");
        data.departureTime = timeArray[0];
        logger.log("departure time : " + data.departureTime);
        data.arrivalTime = timeArray[1];
        logger.log("arrival time : " + data.arrivalTime);

        data.arrivalDate = getArrivalDate(data.departureDate, data.departureTime, data.arrivalTime,language);
        logger.log("arrival date : " + data.arrivalDate);

        data.guestName = getInfoUsingLocator(PASSENGER_NAME);
        logger.log("passenger name : " + data.guestName);

        data.seat = CoreFunctions.removeTrailingOrStaringSpace(getInfoUsingLocator(SEAT_DETAIL).replace("/","-"));
        logger.log("seat details : " + data.seat);

        data.trainName = getInfoUsingLocator(TRAIN_NAME_AND_WAGON_NAME).split("•")[0].trim();
        logger.log("train name : " + data.trainName);

        return data;
    }

    @Override
    public String getTotalPayment(){
        driverTools.swipeToElement(CONTINUE_PAYMENT_BUTTON,Swipe.UP,10);
        WebElement totalPaymentElement = driverTools.waitForElementToAppear(TOTAL_PRICE_BOOKING_PAGE).orElseThrow();
        String totalPayment = driverTools.getText(totalPaymentElement);
        logger.log("Total payment: "+totalPayment);
        return totalPayment;
    }

    @Override
    public boolean isLoggedIn(){
        return !driverTools.isElementDisplayed(FULL_NAME);
    }

    @Override
    public void openLoginPage() {
        driverTools.waitForElementToAppear(BTN_JOIN_NOW,10);
        driverTools.click(BTN_JOIN_NOW);
    }

    @Override
    public boolean verifyContactDetailData(String email,String phoneNumber, String fullName) {
        driverTools.click(CONTACT_CARD);
        String actualFullName = driverTools.getText(FULL_NAME).orElseThrow();
        Assertion.assertThat("Verify full name is the same as registered", actualFullName, containsString(fullName));
        String actualPhoneNumber = driverTools.getText(PHONE).orElseThrow();
        Assertion.assertThat("Verify phone number is the same as registered", actualPhoneNumber, containsString(phoneNumber));
        String actualEmail = driverTools.getText(LOGIN_CONTACT_DETAIL_EMAIL_FIELD).orElseThrow();
        Assertion.assertThat("Verify phone number is the same as registered", actualEmail, containsString(email));
        return true;
    }

    @Override
    public String getPassengerId(){
        driverTools.click(EDIT_PASSENGER);
        String id =  driverTools.getText(ID_NUMBER).orElseThrow();
        driverTools.click(SAVE_BUTTON);
        return id;
    }

    @Override
    public List<String> getInsurancePrices(){
        driverTools.swipeToElement(INSURANCE_PRICE,Swipe.UP,10);
        List<String> priceItems = new ArrayList<>(driverTools.getTexts(INSURANCE_PRICE));
        Assertion.assertThat("Prices exist", !priceItems.isEmpty(),is(true));
        logger.log("prices: "+priceItems);
        return priceItems.stream().filter(e -> e.matches(".*[0-9]+.*")).toList();
    }

    @Override
    public List<String> getPriceDetailPrices(){
        driverTools.waitForElementToAppear(PRICE_PRICE_DETAIL,5);
        List<String> priceItems = new ArrayList<>(driverTools.getTexts(PRICE_PRICE_DETAIL));
        Assertion.assertThat("Prices exist", !priceItems.isEmpty(),is(true));
        String totalPrice = driverTools.getText(TOTAL_PRICE_PRICE_DETAIL).orElseThrow();
        priceItems.add(totalPrice);
        logger.log("Prices: " + priceItems);
        return priceItems;
    }

    @Override
    public boolean verifyFirstPageStepper() {
        return driverTools.isElementDisplayed(FIRST_PAGE_STEPPER);
    }

    @Override
    public boolean verifySecondPageStepper() {
        driverTools.waitForElementToAppear(SELECT_SEAT_BUTTON,15).orElseThrow();
        boolean isSecondPageStepperVisible = driverTools.isElementDisplayed(SECOND_PAGE_STEPPER);
        boolean isFirstPageStepperVisible = driverTools.isElementDisplayed(FIRST_PAGE_STEPPER);
        logger.log("is first page stepper not visible? " + !isFirstPageStepperVisible);
        logger.log("is second page stepper visible? " + isSecondPageStepperVisible);
        return isSecondPageStepperVisible && !isFirstPageStepperVisible;
    }

    /**
     * Fetches the actual price displayed on the price detail without running assertions.
     */
    private double getActualPriceFromDetail(String type, int totalPax, String label, int index) {
        Locator priceLocator = getPriceLocator(type, label, totalPax, index);
        WebElement priceElement = driverTools.waitForElementToAppear(priceLocator).orElseThrow();
        String actualPrice = driverTools.getText(priceElement);
        return CoreFunctions.convertPriceSafely(actualPrice);
    }

    /**
     * Verifies that the price details for adult, infant, and insurance match the expected values
     * in the price breakdown for both departure and return trips.
     *
     * @param trainAndWhooshGeneralData the general data containing expected prices and passenger counts
     * @return true if all price details match, false otherwise
     */
    @SneakyThrows
    @Override
    public boolean verifyGeneralDataPriceDetail(TrainAndWhooshGeneralData trainAndWhooshGeneralData) {
        logger.log("TrainAndWhooshGeneralData: " + new ObjectMapper().writeValueAsString(trainAndWhooshGeneralData));
        boolean isRoundTrip = "roundtrip".equalsIgnoreCase(trainAndWhooshGeneralData.tripType);
        String srpFirstCardPricePerPaxDepart;
        String srpFirstCardPricePerPaxReturn;
        if(trainAndWhooshGeneralData.slashedPricePerPaxDepart != null && !trainAndWhooshGeneralData.slashedPricePerPaxDepart.isEmpty()) {
            srpFirstCardPricePerPaxDepart = trainAndWhooshGeneralData.slashedPricePerPaxDepart;
        } else {
            srpFirstCardPricePerPaxDepart = trainAndWhooshGeneralData.srpFirstCardPricePerPaxDepart;
        }
        if(trainAndWhooshGeneralData.slashedPricePerPaxReturn != null && !trainAndWhooshGeneralData.slashedPricePerPaxReturn.isEmpty()) {
            srpFirstCardPricePerPaxReturn = trainAndWhooshGeneralData.slashedPricePerPaxReturn;
        } else {
            srpFirstCardPricePerPaxReturn = trainAndWhooshGeneralData.srpFirstCardPricePerPaxReturn;
        }
        boolean checkAdultPriceDepart = checkPriceOnPriceDetail(
                "adult",
                srpFirstCardPricePerPaxDepart,
                trainAndWhooshGeneralData.adultPassenger,
                "",
                1,
                isRoundTrip
        );
        boolean checkAdultPriceReturn = !isRoundTrip || checkPriceOnPriceDetail(
                "adult",
                srpFirstCardPricePerPaxReturn,
                trainAndWhooshGeneralData.adultPassenger,
                "",
                2,
                true
        );

        boolean checkInfantPriceDepart = checkPriceOnPriceDetail(
                "infant",
                "0",
                trainAndWhooshGeneralData.infantPassenger,
                "",
                1,
                isRoundTrip
        );
        boolean checkInfantPriceReturn = !isRoundTrip || checkPriceOnPriceDetail(
                "infant",
                "0",
                trainAndWhooshGeneralData.infantPassenger,
                "",
                2,
                true
        );

        boolean isInsuranceCorrect = true;

        if (trainAndWhooshGeneralData.insuranceGeneralData != null) {
            // 1. Calculate Expected Total
            double expectedTotalInsurance = CoreFunctions.convertPriceSafely(trainAndWhooshGeneralData.insuranceGeneralData.insurancePrice) * trainAndWhooshGeneralData.adultPassenger;
            // 2. Fetch Actual Depart Insurance from UI
            double actualDepartInsurance = getActualPriceFromDetail(
                    "insurance",
                    trainAndWhooshGeneralData.adultPassenger,
                    trainAndWhooshGeneralData.insuranceGeneralData.insuranceTitle,
                    1
            );
            // 3. Fetch Actual Return Insurance from UI if Round Trip
            double actualReturnInsurance = 0;
            if (isRoundTrip) {
                actualReturnInsurance = getActualPriceFromDetail(
                        "insurance",
                        trainAndWhooshGeneralData.adultPassenger,
                        trainAndWhooshGeneralData.insuranceGeneralData.insuranceTitle,
                        2
                );
            }
            // 4. Sum them up and verify
            double actualTotalInsurance = actualDepartInsurance + actualReturnInsurance;
            isInsuranceCorrect = Math.abs(expectedTotalInsurance - actualTotalInsurance) <= 0.001;
            logger.log("Verifying total insurance sum. Expected: " + expectedTotalInsurance + ", Actual: " + actualTotalInsurance);
            CoreFunctions.assertPriceEquals("Total insurance sum should match depart + return insurance breakdown", actualTotalInsurance, expectedTotalInsurance);
        }
        return checkAdultPriceDepart
                && checkAdultPriceReturn
                && checkInfantPriceDepart
                && checkInfantPriceReturn
                && isInsuranceCorrect;
    }

    /**
     * Checks if the actual price displayed matches the expected price for a given type and label.
     *
     * @param type the type of price (adult, infant, insurance)
     * @param expectedPrice the expected price per passenger
     * @param totalPax the total number of passengers
     * @param label the label for insurance (if applicable)
     * @param index the index of the trip (1 for departure, 2 for return)
     * @param isRoundTrip whether the trip is a round trip or one-way
     * @return true if the actual price matches the expected price, false otherwise
     */
    private boolean checkPriceOnPriceDetail(String type, String expectedPrice, int totalPax, String label,int index,boolean isRoundTrip) {
        logger.log("Checking " + type + " price on price detail. Expected per person price: " + expectedPrice +
                ", Total pax: " + totalPax + ", Label: " + label);
        String typeSupport = index == 1 ? "departure" : "return";
        Locator priceLocator = getPriceLocator(type, label, totalPax, index);
        WebElement priceElement = driverTools.waitForElementToAppear(priceLocator).orElseThrow();
        String actualPrice = driverTools.getText(priceElement);
        logger.log("Verifying " + type + " price on price detail. Expected per person price: " + expectedPrice +
                ", Actual total price: " + actualPrice);
        double expectedPriceDouble = CoreFunctions.convertPriceSafely(expectedPrice) * totalPax;
        double actualPriceDouble = CoreFunctions.convertPriceSafely(actualPrice);
        boolean isPriceMatch = Double.compare(expectedPriceDouble, actualPriceDouble) == 0;
        logger.log(type + " price match: " + isPriceMatch);
        Assertion.assertThat(type +" { " + typeSupport + " } price should match", actualPriceDouble, is(expectedPriceDouble));
        return isPriceMatch;
    }

    /**
     * Returns the Locator for price detail element.
     *
     * @param type     adult, infant, or insurance
     * @param label    insurance label (only used when type is "insurance")
     * @param totalPax total number of passengers
     * @param index    1 = departure, 2 = return
     */
    private Locator getPriceLocator(String type, String label, int totalPax, int index) {
        String tripType = index == 1 ? "DEPARTURE" : "RETURN";
        String labelEn, labelId;

        switch (type) {
            case "adult":
                labelEn = "Adult";
                labelId = "Dewasa";
                break;
            case "infant":
                labelEn = "Infant";
                labelId = "Bayi";
                break;
            case "insurance":
                labelEn = label;
                labelId = label;
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
        return new Locator(type.toUpperCase() + "_PRICE_DETAIL_" + tripType, AppiumBy.xpath("(//*[contains(@resource-id,'tv_left') and " +
                "(contains(@text,'" + labelEn + " (x" + totalPax + ")') or " +
                "contains(@text,'" + labelId + " (x" + totalPax + ")'))]" +
                "/following-sibling::*[contains(@resource-id,'tv_right')])[" + index + "]"));
    }
}
