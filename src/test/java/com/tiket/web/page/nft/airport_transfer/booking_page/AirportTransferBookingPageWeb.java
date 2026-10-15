package com.tiket.web.page.nft.airport_transfer.booking_page;

import com.tiket.appium.DriverTools;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.tiket.web.page.nft.airport_transfer.pdp.AirportTransferPDPWeb.SUBMIT_BUTTON;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public abstract class AirportTransferBookingPageWeb<T extends WebDriver> extends WebPage<T> {

    public AirportTransferBookingPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> AirportTransferBookingPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new AirportTransferBookingPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new AirportTransferBookingPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    public static final Locator CONTINUE_PAY = new Locator("CONTINUE_PAY", AppiumBy.xpath("//*[contains(@class,'Figure_footer')]//*[contains(@class,'Button_variant_primary')]"));
    private static final Locator CONTACT_CARD = new Locator("CONTACT_CARD", new By.ByXPath("(//*[contains(@id, 'contact-form-container')])"));
    private static final Locator CONTACT_SALUTATION = new Locator("CONTACT_SALUTATION", new By.ByXPath("((//*[contains(@class, 'Form_title')])//label[contains(@class, 'Radio_radio')])"));
    private static final Locator FULL_NAME_INPUT = new Locator("FULL_NAME_INPUT", new By.ByXPath("//input[@id='CONTACT_PERSON-FULLNAME']"));
    private static final Locator PHONE_NUMBER_INPUT = new Locator("PHONE_NUMBER_INPUT", new By.ByXPath("//input[@id='mobile-number' or @id='nomor-ponsel']"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", new By.ByXPath("//input[@id='email-address' or @id='alamat-email']"));
    private static final Locator EMAIL_DISABLED = new Locator("EMAIL_DISABLED", new By.ByXPath("//*[contains(@class, 'styles_state_disabled')]"));
    private static final Locator CHECK_BOOKING_CONTACT_DETAILS_CARD = new Locator("CHECK_BOOKING_CONTACT_DETAILS_CARD", new By.ByXPath("//*[contains(@class, 'booking_contact_detail')]"));
    private static final Locator CONTINUE_TO_PAYMENT_PAGE = new Locator("CONTINUE_TO_PAYMENT_PAGE", new By.ByXPath("//button//*[normalize-space(text())='Continue' or normalize-space(text())='Lanjutkan']"));
    private static final Locator CONTINUE_TO_PAYMENT_PAGE_POPUP = new Locator("CONTINUE_TO_PAYMENT_PAGE_POPUP", new By.ByXPath("//button//*[normalize-space(text())='Continue to pay' or normalize-space(text())='Lanjutkan pembayaran' or normalize-space(text())='Lanjut Bayar']"));
    private static final Locator AIRPORT_LOCATION_NAME_BOOKING_FORM = new Locator("AIRPORT_LOCATION_NAME_BOOKING_FORM", new By.ByXPath("(//*[contains(@class, 'TripRoute_trip_route_origin_detail')])[2]"));
    private static final Locator DESTINATION_LOCATION_NAME_BOOKING_FORM = new Locator("DESTINATION_LOCATION_NAME_BOOKING_FORM", new By.ByXPath("(//*[contains(@class, 'TripRoute_trip_route_destination')])[2]"));
    private static final Locator PRICE_BOTTOM_SHEET = new Locator("PRICE_BOTTOM_SHEET", new By.ByXPath("(//*[contains(@class, 'SubmissionFooter_final_price')])"));
    private static final Locator CLOSE_BOTTOM_SHEET = new Locator("CLOSE_BOTTOM_SHEET", new By.ByXPath("(//*[contains(@class, 'BottomSheet_close_button')])"));
    private static final Locator TOTAL_PRICE_UNDER_BOTTOM_SHEET = new Locator("TOTAL_PRICE_UNDER_BOTTOM_SHEET", new By.ByXPath("(//*[contains(@class, 'PriceInfoBottomSheet_total_price_section')])//span[2]"));
    private static final Locator AIRLINE_DROPDOWN = new Locator("AIRLINE_DROPDOWN", new By.ByXPath("//*[contains(@class,'EditView_flight_code')]//*[contains(@class,'EditView_dropdown_icon')]"));
    private static final Locator AIRLINE_LIST_ITEM = new Locator("AIRLINE_LIST_ITEM", new By.ByXPath("//*[contains(@class,'EditView_airline_list')]"));
    private static final Locator FLIGHT_NUMBER_INPUT = new Locator("FLIGHT_NUMBER_INPUT", new By.ByXPath("//*[contains(@class,'EditView_flight_no_field')]//input"));
    private static final Locator ORIGIN_IN_PRICE_DETAIL = new Locator("ORIGIN_IN_PRICE_DETAIL", AppiumBy.xpath("(//div[contains(@class, 'TripRoute_trip_route_name__')])[3]"));
    private static final Locator DESTINATION_IN_PRICE_DETAIL = new Locator("DESTINATION_IN_PRICE_DETAIL", AppiumBy.xpath("(//div[contains(@class, 'TripRoute_trip_route_name__')])[4]"));
    private static final Locator CAR_TYPE_IN_PRICE_DETAIL = new Locator("CAR_TYPE_IN_PRICE_DETAIL", AppiumBy.xpath("//span[contains(@class, 'Text_text__') and contains(@class, 'Text_variant_highEmphasis__') and contains(@class, 'Text_weight_bold__')]"));
    private static final Locator PRICE_BOOKING = new Locator("PRICE_BOOKING", AppiumBy.xpath("(//*[contains(@class,'PriceInfoBottomSheet_item')]//span[contains(@class,'Text_weight_bold')])[last()]"));

    public void clickSubmitBooking() {
        driverTools.click(SUBMIT_BUTTON);
        driverTools.click(CONTINUE_PAY);
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

    public void checkBookingContactDetailsCard(){
        driverTools.waitForElementToAppear(CHECK_BOOKING_CONTACT_DETAILS_CARD,5);
        driverTools.isElementDisplayed(CHECK_BOOKING_CONTACT_DETAILS_CARD);
    }

    public String getFilledFullName() {
        WebElement input = driverTools.waitForElementToAppear(FULL_NAME_INPUT).orElseThrow();
        return driverTools.getValue(input);
    }

    public String getFilledPhoneNumber() {
        WebElement input = driverTools.waitForElementToAppear(PHONE_NUMBER_INPUT).orElseThrow();
        return driverTools.getValue(input);
    }

    public String getFilledEmail() {
        WebElement input = driverTools.waitForElementToAppear(EMAIL_INPUT).orElseThrow();
        return driverTools.getValue(input);
    }

    public void goToPaymentPage(){
        driverTools.waitForElementToAppear(CONTINUE_TO_PAYMENT_PAGE);
        driverTools.clickWithJavascriptExecutor(CONTINUE_TO_PAYMENT_PAGE);
        driverTools.waitForWebview();
        driverTools.waitForElementToDisappear(CONTINUE_TO_PAYMENT_PAGE,3);

        if(driverTools.waitForElementToAppear(CONTINUE_TO_PAYMENT_PAGE_POPUP,3).isPresent()){
            driverTools.clickWithJavascriptExecutor(CONTINUE_TO_PAYMENT_PAGE_POPUP);
            driverTools.waitForElementToDisappear(CONTINUE_TO_PAYMENT_PAGE_POPUP);
        }
    }

    public String checkForAirportLocationName(){
        driverTools.waitForElementToAppear(AIRPORT_LOCATION_NAME_BOOKING_FORM).orElseThrow();
        return driverTools.getText(AIRPORT_LOCATION_NAME_BOOKING_FORM).orElseThrow();
    }
    public String checkForDestinationLocationName(){
        driverTools.waitForElementToAppear(DESTINATION_LOCATION_NAME_BOOKING_FORM).orElseThrow();
        return driverTools.getText(DESTINATION_LOCATION_NAME_BOOKING_FORM).orElseThrow();
    }

    public void openPriceBottomSheet(){
        driverTools.waitForElementToAppear(PRICE_BOTTOM_SHEET,5);
        driverTools.clickWithJavascriptExecutor(PRICE_BOTTOM_SHEET);
    }

    public String getTotalPrice(){
        driverTools.waitForElementToAppear(TOTAL_PRICE_UNDER_BOTTOM_SHEET).orElseThrow();
        return driverTools.getText(TOTAL_PRICE_UNDER_BOTTOM_SHEET).orElseThrow();
    }

    public void closePriceBottomSheet(){
        driverTools.back();
    }

    /**
     * Opens the airline dropdown, picks a random airline currently in the
     * viewport (the dropdown renders many items but only ~16 are visible at a
     * time), then enters a random 3-digit flight number.
     */
    public void fillFlightDetails() {
        driverTools.click(AIRLINE_DROPDOWN);
        driverTools.waitForElementToAppear(AIRLINE_LIST_ITEM).orElseThrow();

        List<WebElement> visibleAirlines = driverTools.elementsInViewport(AIRLINE_LIST_ITEM);
        WebElement randomAirline = CoreFunctions.getRandomObject(visibleAirlines);
        driverTools.click(randomAirline);

        String flightNumber = String.valueOf(CoreFunctions.randomInt(3));
        driverTools.click(FLIGHT_NUMBER_INPUT);
        driverTools.sendKeys(FLIGHT_NUMBER_INPUT, flightNumber);
    }

    public String verifyOrigin(String origin) {
        driverTools.waitForElementToAppear(CLOSE_BOTTOM_SHEET);
        WebElement tripDetailOrigin = driverTools.waitForElementToAppear(ORIGIN_IN_PRICE_DETAIL,2).orElseThrow();
        String actualOrigin = driverTools.getText(tripDetailOrigin);
        logger.log("Origin in price detail -> " + actualOrigin );
        return actualOrigin;
    }

    public String verifyDestination(String destination) {
        driverTools.waitForElementToAppear(CLOSE_BOTTOM_SHEET);
        WebElement tripDetailOrigin = driverTools.waitForElementToAppear(DESTINATION_IN_PRICE_DETAIL,2).orElseThrow();
        String actualOrigin = driverTools.getText(tripDetailOrigin);
        logger.log("Destination in price detail -> " + actualOrigin );
        return actualOrigin;
    }

    public boolean verifyPriceDetail(String price, String carType) {
        WebElement element = driverTools.waitForElementToAppear(PRICE_BOOKING).orElseThrow();
        String priceFromBookingForm = driverTools.getText(element);
        logger.log("total price in booking form -> " + priceFromBookingForm );
        logger.log("total price in detail form -> " + price );

        Assertion.assertThat("Total price on the booking form should be equal with the details", priceFromBookingForm, equalToIgnoringCase(price));

        WebElement carTypeElement = driverTools.waitForElementToAppear(CAR_TYPE_IN_PRICE_DETAIL).orElseThrow();
        String carTypeName = driverTools.getText(carTypeElement);
        Assertion.assertThat("Car type name on the booking form should match the details", carTypeName, containsString(carType));
        logger.log("Car type name in booking form -> " + carTypeName );
        logger.log("Car type name in PDP -> " + carType );
        return true;
    }
}
