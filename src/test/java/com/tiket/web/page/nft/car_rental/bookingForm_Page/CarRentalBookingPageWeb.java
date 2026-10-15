package com.tiket.web.page.nft.car_rental.bookingForm_Page;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.pojo.carrental.CarRentalBookingPageData;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class CarRentalBookingPageWeb<T extends WebDriver> extends WebPage<T> {

    public CarRentalBookingPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CarRentalBookingPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CarRentalBookingPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CarRentalBookingPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator CONTACT_CARD = new Locator("CONTACT_CARD", new By.ByXPath("(//*[contains(@class, 'ContactDetailSection_container')])"));
    private static final Locator CONTACT_SALUTATION = new Locator("CONTACT_SALUTATION", new By.ByXPath("((//*[contains(@class, 'ContactForm_salutation')])//label[contains(@class, 'Radio_radio')])"));
    private static final Locator FULL_NAME_INPUT = new Locator("FULL_NAME_INPUT", new By.ByXPath("//input[@id='full-name-according-to-idpassport' or @id='nama-lengkap-sesuai-ktppasporsim']"));
    private static final Locator PHONE_NUMBER_INPUT = new Locator("PHONE_NUMBER_INPUT", new By.ByXPath("//input[@id='phone-number' or @id='nomor-ponsel']"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", new By.ByXPath("//input[@id='email-address' or @id='alamat-email']"));
    private static final Locator EMAIL_DISABLED = new Locator("EMAIL_DISABLED", new By.ByXPath("//*[contains(@class, 'styles_state_disabled')]"));
    protected static final Locator SELECT_PICKUP_LOCATION = new Locator("SELECT_PICKUP_LOCATION", new By.ByXPath("(//*[contains(@class, 'LocationInputField_location_text')])[1]"));
    protected static final Locator SELECT_DROP_OFF_LOCATION = new Locator("SELECT_DROP_OFF_LOCATION", new By.ByXPath("(//*[contains(@class, 'LocationInputField_location_text')])[2]"));
    protected static final Locator PICKUP_LOCATION_SEARCH_BOX = new Locator("PICKUP_LOCATION_SEARCH_BOX", new By.ByXPath("(//*[contains(@class, 'SearchBox_placeholder')])//input"));
    protected static final Locator DROP_OFF_LOCATION_SEARCH_BOX = new Locator("DROP_OFF_LOCATION_SEARCH_BOX", new By.ByXPath("(//*[contains(@class, 'SearchBox_placeholder')])//input"));
    protected static final Locator SAME_AS_PICKUP_TOGGLE = new Locator("SAME_AS_PICKUP_TOGGLE",new By.ByXPath("//input[@type='checkbox' and contains(@class, 'Toggle_hidden_checkbox') and @data-testid='booking-same-as-pickup-toggle']"));
    protected static final Locator PICK_FIRST_LOCATION = new Locator("PICKUP_LOCATION_SEARCH_BOX", new By.ByXPath("(//*[contains(@class, 'LocationPicker_location')])[1]"));
    protected static final Locator SET_DROP_OFF_LOCATION = new Locator("SET_DROP_OFF_LOCATION", new By.ByXPath("//*[contains(@class,'Modal_modal_footer')]//button"));
    private static final Locator ADD_SPECIAL_REQUEST = new Locator("ADD_SPECIAL_REQUEST", new By.ByXPath("//*[name()='svg' and @data-testid='booking-special-request-icon']"));
    private static final Locator ADD_SPECIAL_REQUEST_BOOKING_SUMMERY = new Locator("ADD_SPECIAL_REQUEST_BOOKING_SUMMERY", new By.ByXPath("//*[contains(@class,'BookingSummaryCard_others')]"));
    private static final Locator INPUT_SPECIAL_REQUEST = new Locator("INPUT_SPECIAL_REQUEST", new By.ByXPath("(//*[contains(@class, 'styles_input_inner_wrapper')])//textarea"));
    private static final Locator SAVE_SPECIAL_REQUEST = new Locator("SAVE_SPECIAL_REQUEST", new By.ByXPath("//*[contains(@class,'SpecialRequestCard_details_page_footer') or contains(@class,'Modal_modal_footer')]//button"));
    private static final Locator TOTAL_PRICE_UNDER_BOTTOM_SHEET = new Locator("TOTAL_PRICE_UNDER_BOTTOM_SHEET", new By.ByXPath("(//*[contains(@class, 'PriceDetail_price')]//span)[last()]"));
    private static final Locator SELECT_PAYMENT = new Locator("SELECT_PAYMENT", new By.ByXPath("(//*[contains(@class, 'SubmissionSection_bottom_section')])//button"));
    private static final Locator CONTINUE_TO_PAYMENT = new Locator("CONTINUE_TO_PAYMENT", new By.ByXPath("(//*[contains(@class, 'Figure_footer')])//button[2]"));
    private static final Locator SPECIAL_REQUEST_SECTION = new Locator("SPECIAL_REQUEST_SECTION", new By.ByXPath("(//*[contains(@class, 'BookingExtraFacilitiesCard_section_card')])"));
    protected static final Locator SNACK_BAR_OK = new Locator("SNACK_BAR_OK", new By.ByXPath("//button[contains(text(), 'Ok')]"));
    private static final Locator OPEN_PRICE_BOTTOM_SHEET = new Locator("OPEN_PRICE_BOTTOM_SHEET", new By.ByXPath("(//*[contains(@class, 'TotalPrice_container')]//div)[3]"));
    private static final Locator CLOSE_PRICE_BOTTOM_SHEET = new Locator("CLOSE_PRICE_BOTTOM_SHEET",new By.ByXPath("//*[contains(@class, 'BottomSheet_close_button') or contains(@class, 'Modal_modal_close')]"));
    private static final Locator SPECIAL_REQUEST_TEXT = new Locator("SPECIAL_REQUEST_TEXT", new By.ByXPath("(//div[contains(@class, 'SpecialRequestCard_extra_facility')]//span[contains(@class, 'SpecialRequestCard_list_per_day') and text()])[2]"));
    private static final Locator CAR_MODEL_BF = new Locator("CAR_MODEL_BF", By.xpath("//div[contains(@class, 'BookingSummaryCard_info')]//span[contains(@class, 'Text_weight_bold')]"));
    private static final Locator VENDOR_NAME_BF = new Locator("VENDOR_NAME_BF", By.xpath("(//div[contains(@class, 'BookingSummaryCard_vendor')]//span)[3]"));
    private static final Locator DRIVER_TYPE_BF = new Locator("DRIVER_TYPE_BF", By.xpath("//span[contains(@class, 'Text_text') and contains(@class, 'Text_size_b3') and (text()='Self drive' or text()='With driver')]"));
    private static final Locator PICKUP_LOCATION = new Locator("PICKUP_LOCATION", new By.ByXPath("(//h3[contains(@class, 'Text_size_h3')])[1]"));
    private static final Locator DROPOFF_LOCATION = new Locator("DROPOFF_LOCATION", new By.ByXPath("(//h3[contains(@class, 'Text_size_h3')])[2]"));
    private static final Locator PICKUP_LOCATION_TEXT = new Locator("PICKUP_LOCATION_TEXT", By.xpath("(//span[contains(@class, 'LocationInputField_location_text')])[1]"));
    private static final Locator DROPOFF_LOCATION_TEXT = new Locator("DROPOFF_LOCATION_TEXT", By.xpath("(//span[contains(@class, 'LocationInputField_location_text')])[2]"));
    private static final Locator ADDITIONAL_PICK_UP_PRICE = new Locator("ADDITIONAL_PICK_UP_PRICE", By.xpath("(//span[contains(@class, 'Text_variant_positive') and contains(@class, 'Text_weight_bold') and starts-with(normalize-space(), '+')])[last()]"));
    private static final Locator CLOSE_MODAL = new Locator("CLOSE_MODAL", By.xpath("//button[@data-testid='modal-close-button' or contains(@class, 'BottomSheet_close_button')]"));
    private static final Locator PRICE_DETAILS = new Locator("PRICE_DETAILS", By.xpath("//div[contains(@class, 'TotalPrice_price')]"));
    protected static final Locator DAYS_TEXT = new Locator("DAYS_TEXT", By.xpath("//span[contains(@class, 'Text_size_b2') and ((contains(text(),'day') and contains(text(),'per')) or (contains(text(),'hari') and contains(text(),'per')))]"));
    protected static final Locator CAR_PRICE = new Locator("CAR_PRICE", By.xpath("(//span[contains(@class, 'PriceDetail_price')])[1]"));
    private static final Locator PACKAGE_PRICE = new Locator("PACKAGE_PRICE", By.xpath("(//span[contains(@class, 'PriceDetail_price')])[2]"));
    private static final Locator PICK_UP_PRICE = new Locator("PICK_UP_PRICE", By.xpath("(//span[contains(@class, 'PriceDetail_price')])[3]"));
    private static final Locator DROP_OFF_PRICE = new Locator("DROP_OFF_PRICE", By.xpath("(//span[contains(@class, 'PriceDetail_price')])[4]"));
    private static final Locator PACKAGE_NAME = new Locator("PACKAGE_NAME", By.xpath("//span[contains(@class, 'Text_size_b2') and ((contains(text(), 'Package') and contains(text(), 'days')) or (contains(text(), 'Paket') and contains(text(), 'hari')))]"));
    private static final Locator PICK_UP_TEXT = new Locator("PICK_UP_TEXT", By.xpath("//span[contains(@class, 'Text_size_b2') and (starts-with(text(), 'Pickup at Area') or starts-with(text(), 'Ambil di Area'))]"));
    private static final Locator DROP_OFF_TEXT = new Locator("DROP_OFF_TEXT", By.xpath("//span[contains(@class, 'Text_size_b2') and (starts-with(text(), 'Drop-off at Area') or starts-with(text(), 'Turun di Area'))]"));
    private static final Locator PICK_UP_AREA = new Locator("PICK_UP_AREA", By.xpath("//div[contains(@class, 'MapsPinPicker_zone_pricing')]/span[1]"));
    private static final Locator DELIVERABLE_LOCATION = new Locator("DELIVERABLE_LOCATION", new By.ByXPath("(//*[contains(@class, 'BookingLocationInput_options')])[2]"));
    private static final Locator DRIVER_TYPE_BOOKING_SUMMERY = new Locator("DRIVER_TYPE_BOOKING_SUMMERY", new By.ByXPath("(//*[contains(@class,'BookingSummaryCard_info')]//*[contains(@class,'Text_text')])[1]"));
    private static final Locator PICK_UP_AREA_DATE_AND_TIME = new Locator("PICK_UP_AREA_DATE_AND_TIME", By.xpath("//*[contains(@class,'Text_text') and (contains(text(),'Pickup') or contains(text(),'Jemput') or contains(text(),'Pengambilan'))]/following-sibling::*[contains(text(),':')]"));
    private static final Locator DROP_OFF_AREA_DATE_AND = new Locator("DROP_OFF_AREA_DATE_AND_TIME", By.xpath("(//*[contains(@class,'Text_text') and contains(text(),'Drop-off') or contains(text(),'Return') or contains(text(),'Pengembalian')]/following-sibling::*)[2]"));
    private static final Locator VEHICLE_NAME_BOOKING_SUMMERY = new Locator("VEHICLE_NAME_BOOKING_SUMMERY", By.xpath("//*[contains(@class,'BookingSummaryCard_info')]//*[contains(@class,'Text_weight_bold')]"));
    private static final Locator VENDOR_NAME_BOOKING_SUMMERY = new Locator("VENDOR_NAME_BOOKING_SUMMERY", By.xpath("//*[contains(@class,'BookingSummaryCard_vendor')]//*[contains(@class,'Text_text')]"));
    private static final Locator POP_UP_PAYMENT = new Locator("POP_UP_PAYMENT", new By.ByXPath("//button[normalize-space(text())='Go to the payment page']"));

    public abstract String setPickUpLocation(String addLocation);

    public abstract String setSetDropOffLocation(String addDestination);

    public abstract void continueBookFormMWEB();

    public Boolean isBookingTextExists(){
        driverTools.waitForElementToAppear(PICKUP_LOCATION);
        driverTools.waitForElementToAppear(DROPOFF_LOCATION);

        return driverTools.isElementDisplayed(PICKUP_LOCATION) && driverTools.isElementDisplayed(DROPOFF_LOCATION);
    }

    public void addSpecialRequest(String request) {
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_SECTION, 3);
        driverTools.waitForElementToAppear(ADD_SPECIAL_REQUEST, 5);
        WebElement specialRequestBtn = driver.findElement(ADD_SPECIAL_REQUEST.by());
        driverTools.clickSvgOrNonHtmlElements(specialRequestBtn);

        driverTools.waitForElementToAppear(INPUT_SPECIAL_REQUEST, 3);
        driverTools.clickWithJavascriptExecutor(INPUT_SPECIAL_REQUEST);
        driverTools.sendKeys(INPUT_SPECIAL_REQUEST, request);

        driverTools.click(SAVE_SPECIAL_REQUEST);
    }

    public String getSpecialRequest() {
        return driverTools.waitForElementToAppear(SPECIAL_REQUEST_TEXT).orElseThrow().getText();
    }

    public String getCarModel() {
        return driverTools.waitForElementToAppear(CAR_MODEL_BF).orElseThrow().getText();
    }

    public String getVendorName() {
        return driverTools.waitForElementToAppear(VENDOR_NAME_BF).orElseThrow().getText();
    }

    public String getDriverType() {
        return driverTools.waitForElementToAppear(DRIVER_TYPE_BF).orElseThrow().getText();
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

    public String getAddSpecialRequestBookingSummery() {
        driverTools.waitForElementToAppear(ADD_SPECIAL_REQUEST, 5);
        WebElement specialRequestBtn = driverTools.waitForElementToPresence(ADD_SPECIAL_REQUEST_BOOKING_SUMMERY).orElseThrow();
        String specialRequestText = driverTools.getText(specialRequestBtn);
        logger.log("Special request text: " + specialRequestText);
        return specialRequestText;
    }

    public String getFooterTotalPrice() {
        driverTools.waitForElementToAppear(PRICE_DETAILS, 10);
        return driverTools.getText(PRICE_DETAILS).orElseThrow();
    }

    public String getTotalPrice() {
        driverTools.waitForElementToAppear(OPEN_PRICE_BOTTOM_SHEET);
        driverTools.clickWithJavascriptExecutor(OPEN_PRICE_BOTTOM_SHEET);
        driverTools.waitForElementToAppear(TOTAL_PRICE_UNDER_BOTTOM_SHEET,2);
        String totalPrice = driverTools.getText(TOTAL_PRICE_UNDER_BOTTOM_SHEET).orElseThrow();
        driverTools.click(CLOSE_PRICE_BOTTOM_SHEET);
        return totalPrice;
    }

    public void selectPayment() {
        driverTools.waitForElementToAppear(SELECT_PAYMENT);
        driverTools.clickWithJavascriptExecutor(SELECT_PAYMENT);

        driverTools.waitForElementToAppear(CONTINUE_TO_PAYMENT, 3);
        driverTools.click(CONTINUE_TO_PAYMENT);
    }

    public void priceChangePopup(){
        driverTools.waitForElementToPresence(POP_UP_PAYMENT).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public List<String> getPickUpAreaAndPrice(String location) {
        driverTools.waitForElementToAppear(SELECT_PICKUP_LOCATION);
        driverTools.clickWithJavascriptExecutor(SELECT_PICKUP_LOCATION);

        driverTools.clickWithJavascriptExecutor(PICKUP_LOCATION_SEARCH_BOX);
        driverTools.sendKeys(PICKUP_LOCATION_SEARCH_BOX, location);

        Locator LOCATION_TEXT = getLocationTextLocatorBySearch(location);
        driverTools.waitForElementToAppear(LOCATION_TEXT);
        driverTools.clickWithJavascriptExecutor(LOCATION_TEXT);

        driverTools.waitForElementToBeClickable(PICK_FIRST_LOCATION, 3)
                .ifPresent(driverTools::clickWithJavascriptExecutor);

        driverTools.waitForElementToPresence(PICK_UP_AREA);

        String area = driverTools.getText(PICK_UP_AREA)
                .orElseThrow(() -> new RuntimeException("Pickup area not found"));

        driverTools.waitForElementToAppear(ADDITIONAL_PICK_UP_PRICE);
        String expectedAdditionalPickUpPrice = driverTools.getText(ADDITIONAL_PICK_UP_PRICE)
                .orElseThrow(() -> new RuntimeException("Pickup price not found"));

        driverTools.waitForElementToAppear(SET_DROP_OFF_LOCATION);
        driverTools.clickWithJavascriptExecutor(SET_DROP_OFF_LOCATION);

        if (driverTools.isElementDisplayed(PICKUP_LOCATION_SEARCH_BOX)) {
            driverTools.clickWithJavascriptExecutor(CLOSE_MODAL);
        }

        return List.of(expectedAdditionalPickUpPrice, area);
    }


    public List<String> getDropOffAreaAndPrice(String location) {
        driverTools.waitForElementToAppear(SELECT_DROP_OFF_LOCATION, 3);
        driverTools.clickWithJavascriptExecutor(SELECT_DROP_OFF_LOCATION);

        driverTools.waitForElementToAppear(DELIVERABLE_LOCATION);
        if (driverTools.isElementDisplayed(DELIVERABLE_LOCATION))
            driverTools.clickWithJavascriptExecutor(DELIVERABLE_LOCATION);

        WebElement addDestinationInput = driverTools.waitForElementToAppear(DROP_OFF_LOCATION_SEARCH_BOX).orElseThrow();
        if (!driverTools.getValue(addDestinationInput).equals(location)) {
            driverTools.clearInput(addDestinationInput);
            driverTools.sendKeys(addDestinationInput, location);
        }
        driverTools.waitForElementToAppear(PICK_FIRST_LOCATION);
        driverTools.click(PICK_FIRST_LOCATION);

        driverTools.waitForElementToPresence(PICK_UP_AREA);
        String area = driverTools.getText(PICK_UP_AREA)
                .orElseThrow(() -> new RuntimeException("Pickup area not found"));

        driverTools.waitForElementToAppear(ADDITIONAL_PICK_UP_PRICE);
        String expectedAdditionalDropOffPrice = driverTools.getText(ADDITIONAL_PICK_UP_PRICE).orElseThrow();

        driverTools.waitForElementToAppear(SET_DROP_OFF_LOCATION);
        driverTools.clickWithJavascriptExecutor(SET_DROP_OFF_LOCATION);

        return List.of(expectedAdditionalDropOffPrice, area);
    }

    private Locator getLocationTextLocatorBySearch(String location) {
        return new Locator("LOCATION_TEXT", new By.ByXPath("(//*[contains(@class, 'highlighted') and text()='" + location + "'])[1]"));
    }

    public void clickPriceDetails() {
        driverTools.waitForElementToAppear(PRICE_DETAILS);
        driverTools.clickWithJavascriptExecutor(PRICE_DETAILS);
    }

    public String getPriceOfCar(String duration) {
        driverTools.waitForElementToAppear(DAYS_TEXT);
        String daysText = driverTools.getText(DAYS_TEXT).orElseThrow();
        logger.log(daysText);
        if (!daysText.contains(String.valueOf(duration))) {
            return "";
        }
        String carPrice = driverTools.getText(CAR_PRICE).orElseThrow();
        Optional<Integer> price = CoreFunctions.getIntFromString(carPrice);
        int priceOfCar = price.orElseThrow();
        int days = Integer.parseInt(duration);
        logger.log(String.valueOf(priceOfCar / days));
        return String.valueOf(priceOfCar / days);
    }

    public Boolean getPackagePrice(String packageName, String duration, String originalPrice) {
        String packageOnBKP = driverTools.getText(PACKAGE_NAME).orElseThrow();
        if (!packageOnBKP.contains(String.valueOf(packageName))) {
            return false;
        }
        String packagePrice = driverTools.getText(PACKAGE_PRICE).orElseThrow();
        Optional<Integer> price = CoreFunctions.getIntFromString(packagePrice);
        Optional<Integer> origPrice = CoreFunctions.getIntFromString(originalPrice);
        int packagePriceOnSRP = origPrice.orElseThrow();
        int priceOfPackage = price.orElseThrow();
        int days = Integer.parseInt(duration);
        return packagePriceOnSRP == (priceOfPackage / days);
    }

    public String checkPickUpAddOn(String area) {
        String pickUpText = driverTools.getText(PICK_UP_TEXT).orElseThrow();
        if (!pickUpText.contains("Pickup") || !pickUpText.contains(area)) return "";
        return driverTools.getText(PICK_UP_PRICE).orElseThrow();
    }

    public String checkDropOffAddOn(String area) {
        String dropOffText = driverTools.getText(DROP_OFF_TEXT).orElseThrow();
        if (!dropOffText.contains("Drop-off") || !dropOffText.contains(area)) return "";
        return driverTools.getText(DROP_OFF_PRICE).orElseThrow();
    }

    public abstract String verifiesPickUpArea();

    public abstract String verifiesDropOffArea();

    public boolean isDropOffLocationUnset() {
        driverTools.waitForElementToAppear(SELECT_DROP_OFF_LOCATION, 5);
        String dropOffText = driverTools.getText(SELECT_DROP_OFF_LOCATION).orElse("");
        return dropOffText.isBlank()
                || dropOffText.contains("Pilih lokasi")
                || dropOffText.contains("Select location")
                || dropOffText.contains("Choose location");
    }

    public boolean isSameAsPickupToggleChecked() {
        return driverTools.waitForElementToPresence(SAME_AS_PICKUP_TOGGLE, 10)
                .map(WebElement::isSelected)
                .orElse(false);
    }

    public void enableSameAsPickupToggleIfNeeded() {
        if (!isSameAsPickupToggleChecked()) {
            clickSameAsPickupToggle();
        }
    }

    public void clickSameAsPickupToggle() {
        WebElement toggle = driverTools.waitForElementToPresence(SAME_AS_PICKUP_TOGGLE, 10).orElseThrow();
        if (Configs.PLATFORM == Platform.IOS) {
            try {
                driverTools.click(toggle.findElement(By.xpath("./ancestor::label[1]")));
            } catch (NoSuchElementException e) {
                driverTools.clickWithJavascriptExecutor(SAME_AS_PICKUP_TOGGLE);
            }
        } else {
            driverTools.clickWithJavascriptExecutor(SAME_AS_PICKUP_TOGGLE);
        }
        driverTools.waitForWebToLoad();
    }

    public void ensureDropOffLocationSet(String pickupLocation) {
        if (isDropOffLocationUnset()) {
            enableSameAsPickupToggleIfNeeded();
        }
        if (isDropOffLocationUnset()) {
            setSetDropOffLocation(pickupLocation);
        }
    }

    public String getPickupLocation() {
        driverTools.waitForElementToAppear(PICKUP_LOCATION);
        driverTools.waitForElementToAppear(PICKUP_LOCATION_TEXT);
        return driverTools.getText(PICKUP_LOCATION_TEXT).orElseThrow();
    }

    public String getDropoffLocation() {
        driverTools.waitForElementToAppear(DROPOFF_LOCATION);
        driverTools.waitForElementToAppear(DROPOFF_LOCATION_TEXT);
        return driverTools.getText(DROPOFF_LOCATION_TEXT).orElseThrow();
    }

    public CarRentalBookingPageData getPickUpAndDropOffData(){
        CarRentalBookingPageData bookingPageData = new CarRentalBookingPageData();
        Locator pickUpAreaLocator = getAreaPartLocator("pickup");
        Locator dropOffAreaLocator = getAreaPartLocator("dropoff");
        Locator pickUpPriceLocator = getAreaPricePartLocator("pickup");
        Locator dropOffPriceLocator = getAreaPricePartLocator("dropoff");

        WebElement pickUpAreaElement = driverTools.waitForElementToAppear(pickUpAreaLocator).orElseThrow();
        String pickUpAreaText = driverTools.getText(pickUpAreaElement);
        bookingPageData.pickUpArea = pickUpAreaText;

        WebElement dropOffAreaElement = driverTools.waitForElementToAppear(dropOffAreaLocator).orElseThrow();
        String dropOffAreaText = driverTools.getText(dropOffAreaElement);
        bookingPageData.dropOffArea = dropOffAreaText;

        WebElement pickUpPriceElement = driverTools.waitForElementToAppear(pickUpPriceLocator).orElseThrow();
        String pickUpPriceText = driverTools.getText(pickUpPriceElement);
        bookingPageData.pickUpPrice = pickUpPriceText;

        WebElement dropOffPriceElement = driverTools.waitForElementToAppear(dropOffPriceLocator).orElseThrow();
        String dropOffPriceText = driverTools.getText(dropOffPriceElement);
        bookingPageData.dropOffPrice = dropOffPriceText;

        return bookingPageData;
    }

    private Locator getAreaPartLocator(String type){
        int index = type.equals("pickup") ? 1 : 2;
        return new Locator("PICK_UP_AREA_CONTAINER", By.xpath("(//span[contains(@class, 'Text_variant') and starts-with(normalize-space(), 'Area')])["+index+"]"));
    }

    private Locator getAreaPricePartLocator(String type){
        int index = type.equals("pickup") ? 1 : 2;
        return new Locator("PICK_UP_AREA_CONTAINER", By.xpath("(//span[contains(@class, 'Text_variant') and starts-with(normalize-space(), '+')])["+index+"]"));
    }

    private Locator getPackageSelectedLocatorBookingSummery(String packageName){
        return new Locator("SELECTED_PACKAGE_NAME_BOOKING_SUMMERY", By.xpath("//*[contains(@class,'BookingSummaryCard_flex_start')]//*[contains(text(),'"+packageName+"')]"));
    }

    public String getSelectedDriverType(){
        WebElement driverType = driverTools.waitForElementToAppear(DRIVER_TYPE_BOOKING_SUMMERY).orElseThrow();
        return driverTools.getText(driverType);
    }

    public List<String> getPickUpDateAndTime(){
        WebElement pickUpDateAndTime = driverTools.waitForElementToAppear(PICK_UP_AREA_DATE_AND_TIME).orElseThrow();
        String dateAndTime = driverTools.getText(pickUpDateAndTime);
        logger.log("Pick up date and time: " + dateAndTime);
        String[] parts = dateAndTime.split(", ");
        return Arrays.stream(parts).toList();
    }

    public String getDropOffDateAndTime(){
        WebElement dropOffDateAndTime = driverTools.waitForElementToAppear(DROP_OFF_AREA_DATE_AND).orElseThrow();
        return driverTools.getText(dropOffDateAndTime);
    }

    public String getVehicleNameBookingSummery(){
        WebElement vehicleName = driverTools.waitForElementToAppear(VEHICLE_NAME_BOOKING_SUMMERY).orElseThrow();
        return driverTools.getText(vehicleName);
    }

    public String getVendorNameBookingSummery(){
        WebElement vendorName = driverTools.waitForElementToAppear(VENDOR_NAME_BOOKING_SUMMERY).orElseThrow();
        return driverTools.getText(vendorName);
    }

    public String isPackageSelectedBookingSummery(String packageName){
        Locator selectedPackageLocator = getPackageSelectedLocatorBookingSummery(packageName);
        WebElement selectedPackageElement = driverTools.waitForElementToAppear(selectedPackageLocator).orElseThrow();
        return driverTools.getText(selectedPackageElement);
    }
}
