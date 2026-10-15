package com.tiket.web.page.nft.bus.bookingForm_Page;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.bus.*;
import com.tiket.testbase.Assertion;
import com.tiket.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.costumeMatcher.IsSubstringOfMatcher.isSubsStringOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public abstract class BusBookingFormPageWeb<T extends WebDriver> extends WebPage<T> {

    public BusBookingFormPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> BusBookingFormPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new BusBookingFormPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new BusBookingFormPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator ORDER_SUMMARY_LINK = new Locator("ORDER_SUMMARY_LINK", new By.ByXPath("//*[contains(@class, 'BookingReviewCards_details')]"));
    private static final Locator BUS_IMAGE = new Locator("BUS_IMAGE", new By.ByXPath("//*[contains(@class, 'GalleryCover')]"));
    private static final Locator DETAIL_TAB = new Locator("DETAIL_TAB", new By.ByXPath("//button//*[contains(text(), 'Detail')]"));
    private static final Locator BUS_FACILITY = new Locator("BUS_FACILITY", new By.ByXPath("//*[contains(@class, 'Features_seat_info')]"));
    private static final Locator BUS_OPERATOR = new Locator("BUS_OPERATOR", new By.ByXPath("//*[contains(@class, 'VendorDetail_operator_rating')]//p"));
    private static final Locator BUS_CLASS = new Locator("BUS_CLASS", new By.ByXPath("//*[contains(@class, 'VendorDetail_vehicle')]"));
    private static final Locator BUS_ORIGIN = new Locator("BUS_ORIGIN", new By.ByXPath("//*[contains(@class, 'TripRoute_departure')]//*[contains(@class, 'TripRoute_location_name')]"));
    private static final Locator BUS_DEPARTURE_DATE = new Locator("BUS_DEPARTURE_DATE", new By.ByXPath("(//*[contains(@class, 'TripRoute_departure_checkpoint_wrapper')]//*[contains(@class, 'TripRoute_time')]//*[contains(@class, 'Text_text')])[2]"));
    private static final Locator BUS_DEPARTURE_TIME = new Locator("BUS_DEPARTURE_TIME", new By.ByXPath("(//*[contains(@class, 'TripRoute_departure_checkpoint_wrapper')]//*[contains(@class, 'TripRoute_time')]//*[contains(@class, 'Text_text')])[1]"));
    private static final Locator BUS_DESTINATION = new Locator("BUS_DESTINATION", new By.ByXPath("//*[contains(@class, 'TripRoute_arrival')]//*[contains(@class, 'TripRoute_location_name')]"));
    private static final Locator TRAVEL_INFO = new Locator("TRAVEL_INFO", new By.ByXPath("//*[contains(@class, 'TravelInfo_travel_info')]"));
    private static final Locator TNC_TAB = new Locator("TNC_TAB", new By.ByXPath("//span[contains(@class, 'Tabs') and (contains(text(), 'Syarat & Ketentuan') or (contains(text(), 'Terms & Conditions')))]//parent::button"));

    private static final Locator TNC_CONTENT = new Locator("TNC_CONTENT", new By.ByXPath("//*[@data-testid='pdp-content-tnc']"));

    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", new By.ByXPath("//*[contains(@class, 'AnimatedChevronIcon_icon_chevron')]//preceding-sibling::span"));
    private static final Locator CONTACT_CARD = new Locator("CONTACT_CARD", new By.ByXPath("//*[contains(@class, 'ContactDetailSection_contact_form_card')]"));
    private static final Locator PASSENGER_NAME_HEADER = new Locator("PASSENGER_NAME_HEADER", new By.ByXPath("(//*[contains(@class, 'PassengersDetailSection_card_inner')])[2]"));
    private static final Locator CONTACT_SALUTATION = new Locator("CONTACT_SALUTATION", new By.ByXPath("(//*[contains(@class, 'Radio_checkbox')])[1]"));
    private static final Locator FULL_NAME_INPUT = new Locator("FULL_NAME_INPUT", new By.ByXPath("//input[@name='fullName']"));
    private static final Locator PHONE_NUMBER_INPUT = new Locator("PHONE_NUMBER_INPUT", new By.ByXPath("//input[@id='mobile-number' or @id='nomor-ponsel']"));
    private static final Locator EMAIL_DISABLED = new Locator("EMAIL_DISABLED", new By.ByXPath("//*[contains(@class, 'styles_state_disabled')]"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", new By.ByXPath("//input[@name='emailAddress']"));
    private static final Locator SUBMIT_BUTTON_DISABLED = new Locator("SUBMIT_BUTTON_DISABLED", new By.ByXPath("//*[contains(@class, 'BookingFooter_price_container')]//button[contains(@class, 'Button_state_disabled')]"));
    private static final Locator TNC_CHECK_BOX = new Locator("TNC_CHECK_BOX", new By.ByXPath("//span[contains(@class, 'Check_custom_checkbox')]"));
    private static final Locator TOTAL_PRICE_FOOTER = new Locator("TOTAL_PRICE_FOOTER", new By.ByXPath("//*[contains(@class, 'BookingFooter_price')]//*[contains(@class, 'Text_variant_highEmphasis')]"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", new By.ByXPath("//*[contains(@class, 'BookingFooter_price_container')]//button"));
    private static final Locator CONTINUE_PAYMENT_BUTTON = new Locator("CONTINUE_PAYMENT_BUTTON", new By.ByXPath("//*[contains(@class, 'pop_up')]//button[contains(@class, 'variant_primary')]"));
    private static final Locator PRICE_CHANGE_POPUP = new Locator("PRICE_CHANGE_POPUP", new By.ByXPath("//h2[contains(text(),'price has changed')  or contains(text(),'Harga telah berubah')]"));
    private static final Locator SUBTOTAL_ACCORDION_BTN = new Locator("SUBTOTAL_ACCORDION_BTN", By.xpath("//div[@role='button']//span[contains(text(), 'Subtotal')]"));
    private static final Locator SUBTOTAL_PRICE = new Locator("SUBTOTAL_PRICE", By.xpath("//span[contains(text(), 'Subtotal')]/following-sibling::span[contains(@class, 'PriceSummaryItem_price')]"));
    private static final Locator TAX_AND_FEES_PRICE = new Locator("TAX_AND_FEES_PRICE", By.xpath("//span[contains(@class,'PriceSummaryItem_label') and (contains(normalize-space(.), 'Tax') or contains(normalize-space(.), 'Pajak'))]/following-sibling::span[contains(@class, 'PriceSummaryItem_price')]"));
    private static final Locator TOTAL_PAYMENT_PRICE = new Locator("TOTAL_PAYMENT_PRICE", By.xpath("//span[contains(@class,'PriceSummaryItem_label') and (contains(normalize-space(.), 'Total to pay') or contains(normalize-space(.), 'Total bayar'))]/following-sibling::span[contains(@class, 'PriceSummaryItem_price')]"));private static final Locator COLLAPSE_CONTAINER = new Locator("COLLAPSE_CONTAINER", By.xpath("//div[@data-testid='collapse-container']"));
    private static final Locator ROUTE_HEADER = new Locator("ROUTE_HEADER", By.xpath("//div[@data-testid='collapse-container']//div[contains(@class, 'PriceSummaryList_list_row')][1]//span[contains(@class, 'PriceSummaryItem_label')]"));
    private static final Locator EXPANDED_ITEM_ROWS = new Locator("EXPANDED_ITEM_ROWS", By.xpath("//div[@data-testid='collapse-container']//div[contains(@class, 'PriceSummaryList_children_container')]//div[contains(@class, 'PriceSummaryList_list_row')]"));
    public abstract boolean verifyPriceDetail();

    public abstract void pressFillButtonPassengerDetail(int index);

    public abstract void clickDetails();

    public void verifyOrderSummary(BusSrpOneWayData busSrpData) {

        boolean busImage = driverTools.waitForElementToAppear(BUS_IMAGE).isPresent();
        Assertion.assertThat("Verify bus image is displayed in order summary", busImage, is(true));

        driverTools.scrollIntoWebView(DETAIL_TAB);
        Assertion.assertThat("Verify detail tab is displayed in order summary", driverTools.isElementDisplayed(DETAIL_TAB), is(true));

        driverTools.clickWithJavascriptExecutor(DETAIL_TAB);
        driverTools.waitForElementToAppear(BUS_FACILITY);
        Assertion.assertThat("Verify bus facility is displayed in order summary", driverTools.isElementDisplayed(BUS_FACILITY), is(true));

        driverTools.scrollIntoWebView(BUS_OPERATOR);

        String operatorName = driverTools.getText(BUS_OPERATOR).orElseThrow();
        Assertion.assertThat("Verify bus operator in order summary is equal to SRP", operatorName, equalTo(busSrpData.operatorName));

        String busClass = driverTools.getText(BUS_CLASS).orElseThrow();
        Assertion.assertThat("Verify bus class in order summary is equal to SRP", busClass, isSubsStringOf(busSrpData.busClass));

        driverTools.scrollIntoWebView(BUS_ORIGIN);
        String origin = driverTools.getText(BUS_ORIGIN).orElseThrow();
        Assertion.assertThat("Verify origin in order summary is equal to SRP", origin, equalTo(busSrpData.origin));

        String departDate = driverTools.getText(BUS_DEPARTURE_DATE).orElseThrow();
        Assertion.assertThat("Verify departure date in order summary is equal to SRP", departDate, equalTo(busSrpData.departDate));

        String departTime = driverTools.getText(BUS_DEPARTURE_TIME).orElseThrow();
        Assertion.assertThat("Verify departure time in order summary is equal to SRP", departTime, equalTo(busSrpData.departTime));

        String destination = driverTools.getText(BUS_DESTINATION).orElseThrow();
        Assertion.assertThat("Verify destination in order summary is equal to SRP", destination, equalTo(busSrpData.destination));

        Assertion.assertThat("Verify travel info is displayed in order summary", driverTools.isElementDisplayed(TRAVEL_INFO), is(true));
        Assertion.assertThat("Verify terms & condition tab is displayed in order summary", driverTools.isElementDisplayed(TNC_TAB), is(true));

        WebElement tncTab = driverTools.waitForElementToPresence(TNC_TAB).orElseThrow();
        driverTools.clickWithJavascriptExecutor(tncTab);

        if (driverTools.waitForElementToAppear(TNC_CONTENT, 6).isEmpty()) {
            driverTools.clickWithJavascriptExecutor(TNC_TAB);
        }

        boolean tncContent = driverTools.isElementPresent(TNC_CONTENT);
        Assertion.assertThat("Verify terms and condition content is displayed in order summary", tncContent, is(true));
    }

    public boolean verifyBookingPriceInIDR() {
        List<WebElement> priceList = driverTools.elements(TOTAL_PRICE).orElseThrow();
        for (WebElement priceElement : priceList) {
            String priceText = driverTools.getText(priceElement);
            if (!priceText.contains("IDR")) {
                return false;
            }
        }
        return true;
    }

    public void fillContactDetail(String fullName, String phoneNumber, String email) {
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
            if (!driverTools.getValue(emailInput).equals(email)) {
                driverTools.clearInput(emailInput);
                driverTools.sendKeys(emailInput, email);
            }
        }
    }

    public void fillPassengerDetail(int totalPassenger) {
        for (int i = 1; i <= totalPassenger; i++) {
            Locator PASSENGER_HEADER = getPassengerHeaderLocator(i);
            driverTools.waitForElementToAppear(PASSENGER_HEADER, 5);
            if (driverTools.element(PASSENGER_HEADER).isPresent() && i > 1) {
                driverTools.scrollIntoWebView(PASSENGER_HEADER);
                driverTools.clickWithJavascriptExecutor(PASSENGER_HEADER);
            } else if (driverTools.element(PASSENGER_HEADER).isEmpty() && i > 1) {
                driverTools.clickWithJavascriptExecutor(PASSENGER_NAME_HEADER);
            }
            pressFillButtonPassengerDetail(i);
            driverTools.clickWithJavascriptExecutor(getPassengerSalutationInput(i));
            enterPassengerFullName(i);
            enterPassengerDateOfBirth(i);
            enterPassengerIdNumber(i);
        }
    }

    private Locator getPassengerHeaderLocator(int index) {
        return new Locator("PASSENGER_HEADER_" + index, new By.ByXPath("//div[contains(@class, 'PassengersDetailSection_card_inner')]//p[contains(text(), '" + index + "')]"));
    }

    private Locator getPassengerSalutationInput(int index) {
        return new Locator("PASSENGER_SALUTATION_" + index, new By.ByXPath("((//*[contains(@class, 'PassengerForm_radio')])//*[contains(@class, 'Radio_children') and (text()='Mr' or text()='Tuan')])[" + index + "]"));
    }

    private void enterPassengerFullName(int index) {
        Locator PASSENGER_FULL_NAME_INPUT = getPassengerFullNameInput(index);
        if (driverTools.getValueAttribute(PASSENGER_FULL_NAME_INPUT, "value").isEmpty()) {
            driverTools.waitForElementToBeClickable(PASSENGER_FULL_NAME_INPUT);
            driverTools.sendKeys(PASSENGER_FULL_NAME_INPUT, CoreFunctions.randomNameWithoutTest());
        }
    }

    private Locator getPassengerFullNameInput(int index) {
        return new Locator("PASSENGER_FULL_NAME_INPUT_" + index, new By.ByXPath("(//div[contains(@class, 'PassengerForm')]//input[@id='full-name-according-to-idpassport' or @id='nama-lengkap-sesuai-ktppaspor'])[" + index + "]"));
    }

    public abstract void enterPassengerDateOfBirth(int index);

    public abstract Locator getPassengerDateOfBirthInput(int index);

    private void enterPassengerIdNumber(int index) {
        Locator ID_NUMBER_INPUT = getPassengerIdNumberInput(index);
        if (driverTools.isElementDisplayed(ID_NUMBER_INPUT) && driverTools.getValueAttribute(ID_NUMBER_INPUT, "value").isEmpty()) {
            driverTools.sendKeys(ID_NUMBER_INPUT, "327" + CoreFunctions.randomInt(13));
        }
    }

    private Locator getPassengerIdNumberInput(int index) {
        return new Locator("ID_NUMBER_INPUT_" + index, new By.ByXPath("(//input[@id='id-number'])[" + index + "]"));
    }

    public void clickTncCheckBox() {
        boolean submitBtnDisabled = driverTools.isElementDisplayed(SUBMIT_BUTTON_DISABLED);
        if (submitBtnDisabled) {
            WebElement tncCheckBox = driverTools.waitForElementToAppear(TNC_CHECK_BOX).orElseThrow();
            driverTools.scrollIntoWebView(tncCheckBox);
            driverTools.clickWithJavascriptExecutor(TNC_CHECK_BOX);
        }
    }

    public String getTotalPriceFooter() {
        return driverTools.getText(TOTAL_PRICE_FOOTER).orElseThrow();
    }

    public void continueToPaymentPage() {
        driverTools.clickWithJavascriptExecutor(SUBMIT_BUTTON);
        driverTools.waitForElementToAppear(CONTINUE_PAYMENT_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public void priceChangePopUp(){
        driverTools.waitForElementToAppear(PRICE_CHANGE_POPUP, 5);
        driverTools.waitForElementToAppear(CONTINUE_PAYMENT_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public Map<String, String> capturePriceSummaryDetails() {
        Map<String, String> summaryDetails = new LinkedHashMap<>();
        String subtotal = driverTools.getText(SUBTOTAL_PRICE).orElseThrow();
        String taxAndFees = driverTools.getText(TAX_AND_FEES_PRICE).orElseThrow();
        String total = driverTools.getText(TOTAL_PAYMENT_PRICE).orElseThrow();
        summaryDetails.put("Subtotal", subtotal);
        summaryDetails.put("Tax & other fees", taxAndFees);
        summaryDetails.put("Total to pay", total);
        return summaryDetails;
    }
}