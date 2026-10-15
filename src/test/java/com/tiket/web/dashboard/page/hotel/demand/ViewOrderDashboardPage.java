package com.tiket.web.dashboard.page.hotel.demand;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static com.tiket.core.CoreFunctions.randomName;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class ViewOrderDashboardPage <T extends WebDriver> extends BasePage<T> {
    public ViewOrderDashboardPage(Session<T> session) {super(session);}

    private static final Locator INPUT_ID_FIELD = new Locator("INPUT_ID_FIELD", By.xpath("//input[@placeholder='Enter the ID' and @type='text']"));
    private static final Locator INPUT_ORDER_ID = new Locator("INPUT_ORDER_ID", By.xpath("//input[@placeholder='Enter the Order ID' and @type='text']"));
    private static final Locator INPUT_ITINERARY_ID = new Locator(" INPUT_ITINERARY_ID", By.xpath("//input[@placeholder='Enter the Itinerary ID' and @type='text']"));
    private static final Locator INPUT_VENDOR_ITINERARY_ID = new Locator(" INPUT_VENDOR_ITINERARY_ID", By.xpath("//input[@placeholder='Enter the Vendor Itinerary ID' and @type='text']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[@type='submit'][normalize-space(.)='Search']"));
    private static final Locator ORDER_TABLE = new Locator("ORDER_TABLE", By.xpath("//div[@class='table-responsive']"));
    private static final Locator ORDER_ID_LABEL = new Locator("ORDER_ID_LABEL", By.xpath("//span[text()='Order ID']"));
    private static final Locator ITINERARY_ID_LABEL = new Locator("ITINERARY_ID_LABEL", By.xpath("//li[span[normalize-space()='Itinerary ID']]"));
    private static final Locator VENDOR_ITINERARY_ID_LABEL = new Locator("VENDOR_ITINERARY_ID_LABEL", By.xpath("//li[span[normalize-space()='Vendor Itinerary ID']]"));
    private static final Locator SELECT_SOURCE_DROPDOWN = new Locator("SELECT_SOURCE_DROPDOWN", By.xpath("//input[@id='select-source']"));
    private static final Locator SELECT_SOURCE_DROPDOWN_GENERAL = new Locator("SELECT_SOURCE_DROPDOWN_GENERAL", By.xpath("//div[@class='dropdown-fields']"));
    private static final Locator SEARCH_ORDER_DETAIL_BOX = new Locator("SEARCH_ORDER_DETAIL_BOX", By.id("select-source"));
    private static final Locator ORDER_DETAIL_BOX = new Locator("ORDER_DETAIL_BOX", By.xpath("//th[normalize-space()='Order Detail']"));
    private static final Locator ORDER_ID = new Locator("ORDER_ID", By.xpath("//tbody/tr[1]/td[1]"));
    private static final Locator ITINERARY_ID = new Locator("ITINERARY_ID", By.xpath("//tbody/tr[1]/td[2]"));
    private static final Locator DATE_PURCHASED = new Locator("DATE_PURCHASED", By.xpath("//tbody/tr[1]/td[3]"));
    private static final Locator PRODUCT_TYPE = new Locator("PRODUCT_TYPE", By.xpath("//tbody/tr[1]/td[4]"));
    private static final Locator ORDER_DETAIL = new Locator("ORDER_DETAIL", By.xpath("//tbody/tr[1]/td[5]"));
    private static final Locator ORDER_ID_INPUT_FIELD = new Locator("ORDER_ID_INPUT_FIELD", By.xpath("//input[@placeholder='Enter the Order ID' and @type='text']"));
    private static final Locator PASSENGER_DETAIL = new Locator("PASSENGER_DETAIL", By.xpath("//td[ol/li]"));
    private static final Locator ORDER_STATUS = new Locator("ORDER_STATUS", By.xpath("//tbody/tr[1]/td[7]"));
    private static final Locator ACTION_PANE = new Locator("ACTION_PANE", By.xpath("//th[contains(normalize-space(),'Action')]"));
    private static final Locator FAILED_ORDER_STATUS = new Locator("FAILED_ORDER_STATUS", By.xpath("//div[contains(@class,'Label')]//strong"));
    private static final Locator MORE_ACTION_BUTTON = new Locator("MORE_ACTION_BUTTON", By.xpath("//button[normalize-space()='More']"));
    private static final Locator MORE_ACTION_BOX = new Locator("MORE_ACTION_BOX", By.xpath("//ul[contains(@class,'min-w-[200px]')]"));
    private static final Locator EDIT_BOOKING_BUTTON = new Locator("EDIT_BOOKING_BUTTON", By.xpath("//span[text()='Edit Booking']"));
    private static final Locator REPORT_FAILED_CHECKIN_BUTTON = new Locator("REPORT_FAILED_CHECKIN_BUTTON", By.xpath("//span[normalize-space()='Report Failed Checkin']"));
    private static final Locator CANCEL_BOOKING_BUTTON = new Locator("CANCEL_BOOKING_BUTTON", By.xpath("//li[.//span[normalize-space()='Cancel Booking']]"));
    private static final Locator SUCCESS_ORDER_STATUS = new Locator("SUCCESS_ORDER_STATUS", By.xpath("//div[contains(@class,'Label')]//strong"));

    private static final Locator CANCELLED_ORDER_STATUS = new Locator("CANCELLED_ORDER_STATUS", By.xpath("//div[contains(@class,'Label')]//strong"));
    private static final Locator EDIT_BOOKING_PAGE = new Locator("EDIT_BOOKING_PAGE", By.xpath("//div[@class='edit-booking']"));
    private static final Locator GUEST_DETAILS_SECTION = new Locator("GUEST_DETAILS_SECTION", By.xpath("//form[@class='edit-guest-info-form']"));
    private static final Locator GUEST_SUBMIT_BUTTON = new Locator("GUEST_SUBMIT_BUTTON", By.xpath("(//button[@type='submit'])[1]"));
    private static final Locator GUEST_SUBMIT_POPUP = new Locator("GUEST_SUBMIT_POPUP", By.xpath("//div[@class='modal-body']"));
    private static final Locator POPUP_OK_BUTTON = new Locator("POPUP_OK_BUTTON", By.xpath("//button[contains(@class, 'button') and text()='OK']"));
    private static final Locator FIRST_NAME = new Locator("FIRST_NAME", By.xpath("//td/ol/li[1]"));
    private static final Locator SECOND_NAME = new Locator("SECOND_NAME", By.xpath("//td/ol/li[2]"));
    private static final Locator MORE_ACTION = new Locator("MORE_ACTION", By.xpath("//div[@class=\"context-menu-container\"]"));
    private static final Locator EDIT_BOOKING = new Locator("EDIT_BOOKING", By.xpath("//div[@class=\"context-menu-item\"][text()=\"Edit Booking\"]"));
    private static final Locator GUEST_NAME_FIELD = new Locator("GUEST_NAME_FIELD", By.xpath("//div[@class=\"edit-guest-info-input-name-wrapper\"]//input"));
    private static final Locator SPECIAL_REQUEST_SECTION = new Locator("SPECIAL_REQUEST_SECTION", By.xpath("//form[@data-testid='form-test-id' and contains(@class, 'edit-special-request-form')]"));
    private static final Locator SPECIAL_REQUEST_INPUT_SECTION = new Locator("SPECIAL_REQUEST_INPUT_SECTION", By.xpath("//textarea[@name='specialRequest' and @validation='required|different_text_value']"));
    private static final Locator SPECIAL_REQUEST_SUBMIT_BUTTON = new Locator("SPECIAL_REQUEST_SUBMIT_BUTTON", By.xpath("(//button[@type='submit' and normalize-space(text())='Submit'])[2]"));
    private static final Locator SPECIAL_REQUEST_CONFIRMATION_POPUP = new Locator("SPECIAL_REQUEST_CONFIRMATION_POPUP", By.xpath("//div[@class='modal-body']"));
    private static final Locator SPECIAL_REQUEST_OK_BUTTON = new Locator("SPECIAL_REQUEST_OK_BUTTON", By.xpath("//button[@type='button' and normalize-space(text())='OK']"));
    private static final Locator TOGGLE_MM_APPROVE = new Locator("TOGGLE_MM_APPROVE", By.xpath("(//div[@class=\"failed-checkin-list-toggle-wrapper\"])[2]"));
    private static final Locator REPORT_FAILED_CHECKIN = new Locator("REPORT_FAILED_CHECKIN", By.xpath("//div[@class=\"context-menu-item\"][text()=\"Report Failed Checkin\"]"));
    private static final Locator SELECT_CATEGORY = new Locator("SELECT_CATEGORY", By.xpath("//input[@name=\"category\"]"));
    private static final Locator FAILED_CHECKIN_CATEGORY = new Locator("FAILED_CHECKIN_CATEGORY", By.xpath("//span[normalize-space()='Barcode Voucher Sudah Digunakan atau Tidak Berlaku']"));
    private static final Locator SELECT_CHECKIN_PROBLEM = new Locator("SELECT_CHECKIN_PROBLEM", By.xpath("//input[@name=\"checkInProblem\"]"));
    private static final Locator FAILED_CHECKIN_PROBLEM = new Locator("FAILED_CHECKIN_PROBLEM", By.xpath("//span[normalize-space()='Hotel Full Book']"));
    private static final Locator SELECT_REPORTED_FROM = new Locator("SELECT_REPORTED_FROM", By.xpath("//input[@name=\"reportedFrom\"]"));
    private static final Locator FAILED_REPORTED_FROM = new Locator("FAILED_REPORTED_FROM", By.xpath("//span[normalize-space()='Customer']"));
    private static final Locator SELECT_ROOT_CAUSE = new Locator("SELECT_ROOT_CAUSE", By.xpath("//input[@name=\"rootCause\"]"));
    private static final Locator FAILED_ROOT_CAUSE = new Locator("FAILED_ROOT_CAUSE", By.xpath("//span[normalize-space()='Salah mapping']"));
    private static final Locator CONTINUE_FAILED_CHECKIN = new Locator("CONTINUE_FAILED_CHECKIN", By.xpath("//button[normalize-space()='Yes, Continue']"));
    private static final Locator CONFIRMATION_BOX = new Locator("CONFIRMATION_BOX", By.xpath("//div[@class=\"hotel-order-detail__modal__title\"]"));
    private static final Locator ORDERID_FAILED_CHECKIN = new Locator("ORDERID_FAILED_CHECKIN", By.xpath("(//input[@placeholder=\"Keyword\"])[2]"));
    private static final Locator SHOW_ORDERID_FAILED_CHECKIN = new Locator("SHOW_ORDERID_FAILED_CHECKIN", By.xpath("(//tbody//td[contains(@class,'table-cell')])[2]"));
    private static final Locator SEARCH_FAILED_CHECKIN = new Locator("SEARCH_FAILED_CHECKIN", By.xpath(" (//button[@type=\"button\" and text()='Search'])[1]"));
    private static final Locator RELOCATION = new Locator("RELOCATION", By.xpath("//div[@class=\"context-menu-item\" and text()='Relocation']"));
    private static final Locator ORDERID_RELOCATION = new Locator("ORDERID_RELOCATION", By.xpath("(//div[@class=\"col-md-6\"])[1]"));
    private static final Locator SPECIAL_REQUEST_CHECKBOX = new Locator("SPECIAL_REQUEST_CHECKBOX",By.xpath("(//div[@class='check v3'])[2]"));
    private static final Locator SPECIAL_REQUEST_CROSS_BUTTON = new Locator("SPECIAL_REQUEST_CROSS_BUTTON",By.xpath("//div[contains(@class, 'edit-booking__modal-response__close')]/i"));
    private static final Locator GUEST_DETAIL_CHECKBOX = new Locator("SPECIAL_REQUEST_CHECKBOX",By.xpath("(//div[@class='check v3'])[1]"));
    private static final Locator SEARCH_BOOKING_MENU_ITEM = new Locator("SEARCH_BOOKING_MENU_ITEM", By.xpath("//a[contains(., 'Hotel')]/following-sibling::ul//a[contains(., 'Search Booking')]"));
    private static final Locator SEARCH_BOOKING_PAGE_HEADER = new Locator("SEARCH_BOOKING_PAGE_HEADER", By.xpath("//span[text()='Search Bookings']"));
    private static final Locator HOTEL_DROPDOWN_TOGGLE = new Locator("HOTEL_DROPDOWN_TOGGLE", By.xpath("//a[contains(@class, 'nav-dropdown-toggle')][contains(., 'Hotel')]"));
    private static final Locator ORDER_ID_INPUT = new Locator("MODERN_ORDER_ID_INPUT", By.id("enter-order-id"));
    private static final Locator SEARCH_SUBMIT_BUTTON = new Locator("MODERN_SEARCH_SUBMIT_BUTTON", By.xpath("//button[@type='submit' and contains(., 'Search')]"));
    private static final Locator RESULT_ROW_ACTION_BUTTON = new Locator("RESULT_ROW_ACTION_BUTTON", By.xpath("//tbody/tr[1]//button[.]"));
    private static final Locator CHANGE_GUEST_NAME_OPTION = new Locator("CHANGE_GUEST_NAME_OPTION", By.xpath("//li[contains(., 'Change Guest Name')]"));
    private static final Locator MODAL_FULL_NAME_INPUT = new Locator("MODAL_FULL_NAME_INPUT", By.id("full-name"));
    private static final Locator MODAL_SUBMIT_BUTTON = new Locator("MODAL_SUBMIT_BUTTON", By.xpath("//button[@type='submit' and text()='Submit']"));
    private static final Locator TABLE_FIRST_ROW_PASSENGER_NAME = new Locator("TABLE_FIRST_ROW_PASSENGER_NAME", By.xpath("//tbody/tr[1]//div/div/span[1]"));
    private static final Locator CREATE_VOUCHER_WITHOUT_UPDATING_VENDOR_CHECKBOX = new Locator("CREATE_VOUCHER_WITHOUT_UPDATING_VENDOR_CHECKBOX", By.xpath("//label[contains(., 'Create voucher without updating vendor')]"));
    private static final Locator CONFIRMATION_SUBMIT_BUTTON = new Locator("CONFIRMATION_SUBMIT_BUTTON", By.xpath("//div[contains(@class, 'Dialog_body')]//button[text()='Submit']"));
    private static final Locator MODAL_CLOSE_BUTTON = new Locator("MODAL_CLOSE_BUTTON", By.xpath("//button[@data-testid='modal-close-button']"));
    private static final Locator EDIT_PAGE_TABLE_ORDER_ID = new Locator("EDIT_PAGE_TABLE_ORDER_ID", By.xpath("//div[@class='edit-booking--order-detail-table']//tbody/tr[1]/td[1][normalize-space()!='']"));
    private static final Locator GUEST_TITLE_INPUT_FIELD = new Locator("GUEST_TITLE_INPUT_FIELD", By.xpath("//input[@name='title-0']"));
    private static final Locator TABLE_FIRST_ROW_ORDER_ID = new Locator("TABLE_FIRST_ROW_ORDER_ID", By.xpath("//tbody/tr[1]/td[4]"));
    private static final Locator MODAL_TITLE_INPUT = new Locator("MODAL_TITLE_INPUT", By.id("title"));
    private static final Locator CHECKIN_DATE_INPUT_FIELD = new Locator("CHECKIN_DATE_INPUT_FIELD", By.xpath("//div[@class='cdf-checkin-info']//input[@name='startDate']"));
    private static final Locator CHECKOUT_DATE_INPUT_FIELD = new Locator("CHECKOUT_DATE_INPUT_FIELD", By.xpath("//div[@class='cdf-checkin-info']//label[normalize-space()='Check-out Date']/preceding-sibling::input"));
    private static final Locator REASON_DROPDOWN_INPUT = new Locator("REASON_DROPDOWN_INPUT", By.xpath("//div[@class='dropdown-hotel-wrapper']//input[@name='reason']"));
    private static final Locator REASON_OPTIONS = new Locator("REASON_OPTIONS", By.xpath("//div[@class='dropdown-hotel-wrapper']//div[@class='dropdown-hotel']"));
    private static final Locator DESCRIPTION_TEXT_AREA = new Locator("DESCRIPTION_TEXT_AREA", By.xpath("//div[@class='cdf-reason']//textarea[@name='description']"));
    private static final Locator CHANGE_TRAVEL_DATE_MODAL_CLOSE_BTN = new Locator("CHANGE_TRAVEL_DATE_MODAL_CLOSE_BTN", By.xpath("//div[@class='modal-body']//div[@class='modal-response__close']/i"));
    private static final Locator CHANGE_TRAVEL_DATE_MODAL_OK_BTN = new Locator("CHANGE_TRAVEL_DATE_MODAL_OK_BTN", By.xpath("//div[@class='modal-body']//div[@class='modal-response__btn']/button"));
    private static final Locator CHANGE_TRAVEL_DATE_MODAL_TITLE = new Locator("CHANGE_TRAVEL_DATE_MODAL_TITLE", By.xpath("//div[@class='modal-body']//div[@class='modal-response__title']"));
    private static final Locator CHANGE_TRAVEL_DATE_MODAL_DESC = new Locator("CHANGE_TRAVEL_DATE_MODAL_DESC", By.xpath("//div[@class='modal-body']//div[@class='modal-response__desc']"));
    private static final Locator CHANGE_TRAVEL_DATE_MODAL_BODY_CONTAINER = new Locator("CHANGE_TRAVEL_DATE_MODAL_BODY_CONTAINER", By.xpath("//div[@class='modal-body']"));
    private static final Locator REPORT_FAILED_CHECKIN_MODAL = new Locator("REPORT_FAILED_CHECKIN_MODAL", By.xpath("//div[@class='modal-body']"));
    private static final Locator RESET_BUTTON = new Locator("RESET_BUTTON", By.xpath("//button[normalize-space()='Reset']"));
    private static final Locator SELECT_SOURCE_BUTTON_AFTER_RESET = new Locator("SELECT_SOURCE_BUTTON_AFTER_RESET", By.xpath("//label[@for='select-source']"));
    private static final Locator RESEND_CANC_EMAIL = new Locator("RESEND_CANC_EMAIL", By.xpath("//span[text()='Resend Partner Hotel Cancellation Email']"));
    private static final Locator SNACKBAR_TEXT = new Locator("SNACKBAR_TEXT", By.xpath("//div[contains(@class,'Snackbar_text')]"));
    
    public void setInputIdField(String id){
        driverTools.waitForElementToBeClickable(INPUT_ID_FIELD);
        driverTools.sendKeys(INPUT_ID_FIELD, id);
    }

    public void SetInputOrderId(String id){
        driverTools.waitForElementToBeClickable(INPUT_ORDER_ID);
        driverTools.clearInput(INPUT_ORDER_ID);
        driverTools.sendKeys(INPUT_ORDER_ID, id);
    }

    public void SetInputItineraryId(String id){
      driverTools.clearInput(INPUT_ITINERARY_ID);
        driverTools.waitForElementToBeClickable(INPUT_ITINERARY_ID);
        driverTools.sendKeys(INPUT_ITINERARY_ID, id);
    }

    public void SetInputVendorItineraryId(String id){
        driverTools.clearInput(INPUT_VENDOR_ITINERARY_ID);
        driverTools.waitForElementToBeClickable(INPUT_VENDOR_ITINERARY_ID);
        driverTools.sendKeys(INPUT_VENDOR_ITINERARY_ID, id);
    }

    public void clickSearch(){
        driverTools.click(SEARCH_BUTTON);
        driverTools.dummyWait(3);
    }

    public boolean isOrderTableShown(){
        return driverTools.isElementDisplayed(ORDER_TABLE);
    }

    public void clickSourceDropdown(){
        driverTools.click(SELECT_SOURCE_DROPDOWN);
        driverTools.waitForElementToBeClickable(ORDER_ID_LABEL);
    }

    public boolean isSourceDropdownClickable(){
        return driverTools.waitForElementToBeClickable(SELECT_SOURCE_DROPDOWN).isPresent();
    }

    public String getSourceDropdownText(){
        driverTools.waitForElementsToAppear(SELECT_SOURCE_BUTTON_AFTER_RESET).orElseThrow();
        return driverTools.getText(SELECT_SOURCE_BUTTON_AFTER_RESET).orElse("");
    }

    public String getSourceInputFieldText(){
        driverTools.waitForElementsToAppear(INPUT_ID_FIELD).orElseThrow();
        return driverTools.getValue(INPUT_ID_FIELD).trim();
    }

    public boolean isResetButtonClickable(){
        return driverTools.waitForElementToBeClickable(RESET_BUTTON).isPresent();
    }

    public void clickResetButton(){
        driverTools.click(RESET_BUTTON);
    }

    public void clickSourceDropdownItineraryId(){
        driverTools.click(SELECT_SOURCE_DROPDOWN_GENERAL);
        driverTools.waitForElementToBeClickable(ITINERARY_ID_LABEL);
    }

    public void clickSourceDropdownVendorItineraryId(){
        driverTools.click(SELECT_SOURCE_DROPDOWN_GENERAL);
        driverTools.waitForElementToBeClickable(VENDOR_ITINERARY_ID_LABEL);
    }

    public boolean isSourceOrderIDSelectable(){
        return driverTools.waitForElementToBeClickable(ORDER_ID_LABEL).isPresent();
    }

    public boolean isSourceItineraryIDSelectable(){
        return driverTools.waitForElementToBeClickable(ITINERARY_ID_LABEL).isPresent();
    }

    public boolean isSourceVendorItineraryIDSelectable(){
        return driverTools.waitForElementToBeClickable(VENDOR_ITINERARY_ID_LABEL).isPresent();
    }

    public void selectSourceOrderID(){
        driverTools.click(ORDER_ID_LABEL);
    }

    public void selectSourceItineraryID(){
        driverTools.click(ITINERARY_ID_LABEL);
    }

    public void selectSourceVendorItineraryID(){
        driverTools.click(VENDOR_ITINERARY_ID_LABEL);
    }

    public boolean checkSelectedSource(String selectedSource){
        Locator SELECTED_SOURCE_DROPDOWN = new Locator("SELECTED_SOURCE_DROPDOWN", By.xpath("//input[@name='searchBy' and @value='"+selectedSource+"']"));
        return driverTools.isElementDisplayed(SELECTED_SOURCE_DROPDOWN);
    }

    public boolean isSearchOrderDetailBoxLoaded(){
        return driverTools.waitForElementsToAppear(SEARCH_ORDER_DETAIL_BOX).isPresent();
    }

    public boolean isOrderDetailBoxLoaded(){
        return driverTools.waitForElementsToAppear(ORDER_DETAIL_BOX).isPresent();
    }

    public String getOrderId(){
        WebElement orderId=driverTools.waitForElementToAppear(ORDER_ID).orElseThrow();
        return driverTools.getText(orderId);
    }

    public boolean isItineraryIdLoaded(){
        return driverTools.waitForElementsToAppear(ITINERARY_ID).isPresent();
    }

    public boolean isOrderDetailItineraryIdPresent() {
        return driverTools.waitForElementsToAppear(ITINERARY_ID).isPresent();
    }

    public boolean isDatePurchasedLoaded(){
        return driverTools.waitForElementsToAppear(DATE_PURCHASED).isPresent();
    }

    public boolean isProductTypeLoaded(){
        return driverTools.waitForElementsToAppear(PRODUCT_TYPE).isPresent();
    }

    public boolean isOrderDetailLoaded(){
        return driverTools.waitForElementsToAppear(ORDER_DETAIL).isPresent();
    }

    public boolean isPassengerDetailLoaded(){
        return driverTools.waitForElementsToAppear(PASSENGER_DETAIL).isPresent();
    }

    public boolean isOrderStatusLoaded(){
        return driverTools.waitForElementsToAppear(ORDER_STATUS).isPresent();
    }

    public boolean isActionPaneLoaded(){
        return driverTools.waitForElementsToAppear(ACTION_PANE).isPresent();
    }

    public void setOrderIdInput(String id){
        driverTools.waitForElementToBeClickable(ORDER_ID_INPUT_FIELD);
        WebElement input = driverTools.waitForElementToAppear(ORDER_ID_INPUT_FIELD).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(ORDER_ID_INPUT_FIELD, id);
    }

    public String getSuccessOrderStatus(){
        WebElement orderStatus=driverTools.waitForElementToAppear(SUCCESS_ORDER_STATUS).orElseThrow();
        return driverTools.getText(orderStatus);
    }

    public String getCancelledOrderStatus(){
        WebElement orderStatus = driverTools.waitForElementToAppear(CANCELLED_ORDER_STATUS).orElseThrow();
        return driverTools.getText(orderStatus);
    }

    public String getFailedOrderStaus(){
        WebElement orderStatus=driverTools.waitForElementToAppear(FAILED_ORDER_STATUS).orElseThrow();
        return driverTools.getText(orderStatus);
    }

    public void clickMoreActionButton(){
        WebElement button = driverTools.waitForElementToBeClickable(MORE_ACTION_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isActionButtonClickable(){
        return driverTools.waitForElementToBeClickable(MORE_ACTION_BUTTON).isPresent();
    }

    public boolean isMoreActionBoxLoaded(){
        return driverTools.waitForElementsToAppear(MORE_ACTION_BOX).isPresent();
    }

    public String isEditBookingVisible(){
        WebElement Button=driverTools.waitForElementToAppear(EDIT_BOOKING_BUTTON).orElseThrow();
        return driverTools.getText(Button);
    }

    public String isReportFailedCheckinVisible(){
        WebElement Button=driverTools.waitForElementToAppear(REPORT_FAILED_CHECKIN_BUTTON).orElseThrow();
        return driverTools.getText(Button);
    }

    public String isCancelBookingVisible(){
        WebElement Button=driverTools.waitForElementToAppear(CANCEL_BOOKING_BUTTON).orElseThrow();
        return driverTools.getText(Button);
    }

    public void clickEditBookingButton(){
        WebElement button = driverTools.waitForElementToBeClickable(EDIT_BOOKING_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isEditBookingPageLoaded(){
        return driverTools.waitForElementsToAppear(EDIT_BOOKING_PAGE).isPresent();
    }

    public boolean isGuestDetailsSectionLoaded(){
        return driverTools.waitForElementsToAppear(GUEST_DETAILS_SECTION).isPresent();
    }

    private Locator getRoomsLocator(int guestIndex) {
        return new Locator("Room Index" + guestIndex,
                new By.ByXPath("(//div[@class='edit-guest-info-input-group'])[" + guestIndex + "]"));
    }

    private Locator getTitleLocator(int titleIndex) {
        return new Locator("Title Index" + titleIndex,
                new By.ByXPath("//input[@name='title-"+titleIndex+"']"));
    }

    private Locator getNameLocator(int NameIndex) {
        return new Locator("NameIndex" + NameIndex,
                new By.ByXPath("//input[@name='name-"+NameIndex+"']"));
    }

    public boolean checkElementPresent(Locator locator){
        return driverTools.waitForElementToAppear(locator).isPresent();
    }

    public void isAllGuestDetailsLoaded(int adult){
        for (int i = 1; i <= adult; i++) {
            Locator locator = getRoomsLocator(i);
            if (!driverTools.isElementDisplayed(locator)) {
                break;
            }

            Assertion.assertThat("Room " + i + " is Present", checkElementPresent(locator), is(true));

            Locator titleLocator = getTitleLocator(i - 1);
            Assertion.assertThat("Title " + i + " is shown", driverTools.getValue(titleLocator), is(notNullValue()));

            Locator nameLocator = getNameLocator(i - 1);
            Assertion.assertThat("Guest Name " + i + " is shown", driverTools.getValue(nameLocator), is(notNullValue()));
        }
    }

    public void isGuestNameMatchedWithThePaymentPage(int adult, String title, String  guestFullName){
        for (int i = 1; i <= adult; i++) {
            Locator locator = getRoomsLocator(i);
            if (!driverTools.isElementDisplayed(locator)) {
                break;
            }
            Assertion.assertThat("Room " + i + " is Present", checkElementPresent(locator), is(true));

            Locator titleLocator = getTitleLocator(i - 1);
            String value = driverTools.getValue(titleLocator);
            value = (value != null) ? value.replace(".", "") : "";
            Assertion.assertThat("Title " + i + " is shown", value, is(title));

            Locator nameLocator = getNameLocator(i - 1);
            Assertion.assertThat("Guest Name " + i + " is shown", driverTools.getValue(nameLocator), is(guestFullName));
        }
    }

    public void changeGuestName(String text, Locator locator) {
        WebElement input = driverTools.waitForElementToAppear(locator).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input,String.valueOf(text));
    }

    public boolean isGuestNameChanged(Locator locator,String PreviousName){
        String newName=driverTools.getValue(locator);
        return !newName.equals(PreviousName);
    }

    public void fillGuestDetails(int adult){
        for (int i = 1; i <= adult; i++) {
            Locator locator = getRoomsLocator(i);

            if (!driverTools.isElementDisplayed(locator)) {
                break;
            }

            Locator nameLocator = getNameLocator(i - 1);
            String previousName = driverTools.getValue(nameLocator);

            changeGuestName(randomName(), nameLocator);

            Assertion.assertThat("Guest Name " + i + " is changed",
                    isGuestNameChanged(nameLocator, previousName), is(true));
        }
    }

    public boolean isSubmitButtonClickable(){
        return driverTools.waitForElementToBeClickable(GUEST_SUBMIT_BUTTON).isPresent();
    }

    public void clickSubmitButton(){
        WebElement button = driverTools.waitForElementToBeClickable(GUEST_SUBMIT_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(button);
    }

    public boolean isPopUpLoaded(){
        return driverTools.waitForElementsToAppear(GUEST_SUBMIT_POPUP).isPresent();
    }

    public void clickOkButton(){
        WebElement button = driverTools.waitForElementToBeClickable(POPUP_OK_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public String getFirstName(){
        driverTools.waitForElementToAppear(FIRST_NAME);
        return driverTools.getText(FIRST_NAME).orElseThrow();
    }

    public String getSecondName(){
        driverTools.waitForElementToAppear(SECOND_NAME);
        return driverTools.getText(SECOND_NAME).orElseThrow();
    }

    public boolean isOkButtonClickable(){
        return driverTools.waitForElementToBeClickable(POPUP_OK_BUTTON).isPresent();
    }


    public String getItinid(){
        driverTools.waitForElementsToAppear(ITINERARY_ID).orElseThrow();
        return driverTools.getText(ITINERARY_ID).orElseThrow();

    }
    public boolean isSpecialRequestSectionLoaded(){
        return driverTools.waitForElementsToAppear(SPECIAL_REQUEST_SECTION).isPresent();
    }

    public void setSpecialRequestInputIdField(String id){
        driverTools.waitForElementToBeClickable(SPECIAL_REQUEST_INPUT_SECTION);
        WebElement input = driverTools.waitForElementToAppear(SPECIAL_REQUEST_INPUT_SECTION).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(SPECIAL_REQUEST_INPUT_SECTION, id);
    }

    public String getNewSpecialRequest(){
        driverTools.waitForElementToAppear(SPECIAL_REQUEST_INPUT_SECTION);
        return driverTools.getText(SPECIAL_REQUEST_INPUT_SECTION).orElseThrow();
    }

    public boolean isSpecialRequestSubmitButtonClickable(){
        return driverTools.waitForElementToBeClickable(SPECIAL_REQUEST_SUBMIT_BUTTON).isPresent();
    }

    public void clickSpecialRequestSubmitButton(){
        WebElement button = driverTools.waitForElementToBeClickable(SPECIAL_REQUEST_SUBMIT_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(button);
    }

    public boolean isSpecialRequestPopUpLoaded(){
        return driverTools.waitForElementsToAppear(SPECIAL_REQUEST_CONFIRMATION_POPUP).isPresent();
    }

    public boolean isSpecialRequestOkButtonClickable(){
        return driverTools.waitForElementToBeClickable(SPECIAL_REQUEST_OK_BUTTON).isPresent();
    }

    public boolean isToggleClickable(){
        return driverTools.waitForElementToBeClickable(TOGGLE_MM_APPROVE).isPresent();
    }

    public void clickMoreAction(){
        driverTools.click(MORE_ACTION);
    }

    public void clickEditBooking(){
        driverTools.click(EDIT_BOOKING);
    }

    public void clickFailedCheckin(){
        driverTools.click(REPORT_FAILED_CHECKIN_BUTTON);
    }

    public void guestNameField(String guestName){
        driverTools.waitForElementToBeClickable(GUEST_NAME_FIELD);
        driverTools.clearInput(GUEST_NAME_FIELD);
        driverTools.sendKeys(GUEST_NAME_FIELD, guestName);
    }
    public void scrollEditBooking(){
        driverTools.waitForElementToPresence(GUEST_NAME_FIELD);
        driverTools.scrollElementToCenter(GUEST_NAME_FIELD);
    }
    public boolean checkGuestInput(String guestName){
      return  driverTools.getValueAttribute(GUEST_NAME_FIELD, "value").equals(guestName);
    }

    public String getConfirmBox(){
        WebElement confirmBox=driverTools.waitForElementToAppear(CONFIRMATION_BOX).orElseThrow();
        return driverTools.getText(confirmBox);
    }

    public void clickCategoryDropdown(){
        driverTools.click(SELECT_CATEGORY);
        driverTools.waitForElementToBeClickable(FAILED_CHECKIN_CATEGORY);
    }

    public void selectFailedCheckinCategory(){
        driverTools.click(FAILED_CHECKIN_CATEGORY);
    }

    public void clickCheckinProblemDropdown(){
        driverTools.click(SELECT_CHECKIN_PROBLEM);
        driverTools.waitForElementToBeClickable(FAILED_CHECKIN_PROBLEM);
    }

    public void SelectCheckinProblem(){
        driverTools.click(FAILED_CHECKIN_PROBLEM);
    }

    public void clickReportedDropdown(){
        driverTools.click(SELECT_REPORTED_FROM);
        driverTools.waitForElementToBeClickable(FAILED_REPORTED_FROM);
    }

    public void selectReportedCategory(){
        driverTools.click(FAILED_REPORTED_FROM);
    }

    public void clickRootCauseDropdown(){
        driverTools.click(SELECT_ROOT_CAUSE);
        driverTools.waitForElementToBeClickable(FAILED_ROOT_CAUSE);
    }

    public void selectRootCause(){
        driverTools.click(FAILED_ROOT_CAUSE);
    }

    public void clickContinueFailed(){

        driverTools.click(CONTINUE_FAILED_CHECKIN);
    }

    public String getFailedCheckinErrorMessageByInputName(String inputName) {
        String xpath = "//div[.//*[normalize-space()='%s']]//span[normalize-space()='This field is required']".formatted(inputName);
        Locator errorLocator = new Locator(inputName + " Error Message", By.xpath(xpath));
        WebElement element = driverTools.waitForElementToAppear(errorLocator)
                .orElseThrow(() -> new RuntimeException("Could not find error message div for input ID: " + inputName));

        return driverTools.getText(element).trim();
    }

    public void clickRelocation(){
        driverTools.click(RELOCATION);
    }
    public void inputOrderidFailedCheckin(String id){
        driverTools.waitForElementToBeClickable(ORDERID_FAILED_CHECKIN);
        driverTools.sendKeys(ORDERID_FAILED_CHECKIN, id);
    }

    public String getOrderIdFailedCheckin(){
        WebElement orderId=driverTools.waitForElementToAppear(SHOW_ORDERID_FAILED_CHECKIN).orElseThrow();
        return driverTools.getText(orderId);
    }

    public String getOrderIdRelocation(){
        WebElement orderId=driverTools.waitForElementToAppear(ORDERID_RELOCATION).orElseThrow();
        return driverTools.getText(orderId);
    }

    public void clickSearchFailedCheckIn(){
        driverTools.click(SEARCH_FAILED_CHECKIN);
    }

    public Boolean isSpecialRequestCheckboxClickable() {
        return driverTools.waitForElementToBeClickable(SPECIAL_REQUEST_CHECKBOX).isPresent();
    }

    public Boolean isSpecialRequestCrossButtonClickable() {
        return driverTools.waitForElementToBeClickable(SPECIAL_REQUEST_CROSS_BUTTON).isPresent();
    }

    public Boolean isGuestDetailCheckboxClickable() {
        return driverTools.waitForElementToBeClickable(GUEST_DETAIL_CHECKBOX).isPresent();
    }

    public void clickGuestDetailCheckbox(){
        driverTools.click(GUEST_DETAIL_CHECKBOX);
    }

    public void clickSearchBookingMenu() {
        driverTools.waitForElementToBeClickable(HOTEL_DROPDOWN_TOGGLE);
        driverTools.click(HOTEL_DROPDOWN_TOGGLE);

        driverTools.waitForElementToBeClickable(SEARCH_BOOKING_MENU_ITEM);
        driverTools.click(SEARCH_BOOKING_MENU_ITEM);
    }

    public boolean isSearchBookingPageLoaded() {
        return driverTools.waitForElementsToAppear(SEARCH_BOOKING_PAGE_HEADER).isPresent();
    }

    public void searchBookingByOrderId(String orderId) {
        driverTools.waitForElementToBeClickable(ORDER_ID_INPUT);
        driverTools.clearInput(ORDER_ID_INPUT);
        driverTools.sendKeys(ORDER_ID_INPUT, orderId);

        driverTools.waitForElementToBeClickable(SEARCH_SUBMIT_BUTTON);
        driverTools.click(SEARCH_SUBMIT_BUTTON);
    }

    public void clickResultRowActionButton() {
        driverTools.waitForElementToBeClickable(RESULT_ROW_ACTION_BUTTON);
        driverTools.click(RESULT_ROW_ACTION_BUTTON);
    }

    public boolean isModalCloseButtonClickable() {
        return driverTools.waitForElementToBeClickable(MODAL_CLOSE_BUTTON).isPresent();
    }

    public void clickModalCloseButton() {
        driverTools.waitForElementToBeClickable(MODAL_CLOSE_BUTTON);
        driverTools.click(MODAL_CLOSE_BUTTON);
    }

    public void clickChangeGuestNameOption() {
        driverTools.waitForElementToBeClickable(CHANGE_GUEST_NAME_OPTION);
        driverTools.click(CHANGE_GUEST_NAME_OPTION);
    }

    public String getGuestNameFromModal() {
        driverTools.waitForElementsToAppear(MODAL_FULL_NAME_INPUT);
        return driverTools.getValueAttribute(MODAL_FULL_NAME_INPUT, "value");
    }

    public void updateGuestNameAndSubmit(String newName) {
        WebElement nameInput = driverTools.waitForElementToAppear(MODAL_FULL_NAME_INPUT).orElseThrow();
        driverTools.clearInput(nameInput);
        driverTools.sendKeys(MODAL_FULL_NAME_INPUT, newName);

        driverTools.waitForElementToBeClickable(MODAL_SUBMIT_BUTTON);
        driverTools.click(MODAL_SUBMIT_BUTTON);

        driverTools.waitForElementToBeClickable(CONFIRMATION_SUBMIT_BUTTON);
        driverTools.click(CONFIRMATION_SUBMIT_BUTTON);
    }

    public boolean waitForPassengerNameToBeUpdated(String expectedName) {
        Locator updatedNameLocator = new Locator("UPDATED_NAME_LOCATOR", By.xpath("//tbody/tr[1]//div/div/span[1][text()='" + expectedName + "']"));
        return driverTools.waitForElementsToAppear(updatedNameLocator).isPresent();
    }

    public String getPassengerNameFromTable() {
        driverTools.waitForElementToAppear(TABLE_FIRST_ROW_PASSENGER_NAME);
        WebElement inlineName = driverTools.waitForElementToAppear(TABLE_FIRST_ROW_PASSENGER_NAME).orElseThrow();
        return driverTools.getText(inlineName);
    }

    public void clickCreateVoucherWithoutUpdatingVendorCheckbox() {
        driverTools.waitForElementToBeClickable(CREATE_VOUCHER_WITHOUT_UPDATING_VENDOR_CHECKBOX);
        driverTools.click(CREATE_VOUCHER_WITHOUT_UPDATING_VENDOR_CHECKBOX);
    }

public String getEditPageTableOrderId() {
    WebElement orderIdCell = driverTools.waitForElementToAppear(EDIT_PAGE_TABLE_ORDER_ID).orElseThrow();
    return driverTools.getText(orderIdCell).trim();
}

public String getGuestSalutationValue() {
    driverTools.waitForElementsToAppear(GUEST_TITLE_INPUT_FIELD);
    String value = driverTools.getValue(GUEST_TITLE_INPUT_FIELD);
    return value != null ? value.replace(".", "") : "";
}

    public String getOrderIdFromTable() {
        WebElement orderIdCell = driverTools.waitForElementToAppear(TABLE_FIRST_ROW_ORDER_ID).orElseThrow();
        return driverTools.getText(orderIdCell).trim();
    }

public String getGuestTitleFromModal() {
    driverTools.waitForElementsToAppear(MODAL_TITLE_INPUT);
    String value = driverTools.getValue(MODAL_TITLE_INPUT);
    return value != null ? value.replace(".", "") : "";
}
    public LocalDate changeTravelDateSelectCheckInDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale.ENGLISH);

        WebElement inputElement = driverTools.waitForElementToAppear(CHECKIN_DATE_INPUT_FIELD)
                .orElseThrow(() -> new RuntimeException("Check-in input field not found on page!"));

        String currentText = inputElement.getAttribute("value");
        if (currentText == null || currentText.isEmpty()) {
            throw new IllegalStateException("The check-in date input field is empty. Cannot calculate offset.");
        }

        return LocalDate.parse(currentText, formatter);
    }

    public LocalDate changeTravelDateUpdateCheckInDate(int k, LocalDate checkInDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale.ENGLISH);
        WebElement inputElement = driverTools.waitForElementToAppear(CHECKIN_DATE_INPUT_FIELD)
                .orElseThrow(() -> new RuntimeException("Check-in input field not found on page!"));

        String currentText = inputElement.getAttribute("value");
        if (currentText == null || currentText.isBlank()) {
            throw new IllegalStateException("The check-in date input field is empty. Cannot calculate offset.");
        }

        LocalDate updatedCheckInDate = checkInDate.plusDays(k);
        String targetDate = updatedCheckInDate.format(formatter);

        driverTools.clickWithJavascriptExecutor(CHECKIN_DATE_INPUT_FIELD);
        Locator loc = getDateLocator(targetDate);

        int i = 0;
        while (i++ < 24 && driverTools.waitForElementToAppear(loc).isEmpty()) {

            Locator NEXT_MONTH_BTN = new Locator("NEXT_MONTH_BTN",By.xpath("//div[@class='datepicker-navbar']//i[contains(@class, 'datepicker-navbar-right')]"));
            driverTools.waitForElementToAppear(NEXT_MONTH_BTN)
                    .orElseThrow(() -> new RuntimeException("Calendar next month navigation button not found!"));

            driverTools.clickWithJavascriptExecutor(NEXT_MONTH_BTN);
        }

        driverTools.clickWithJavascriptExecutor(getDateLocator(targetDate));
        return updatedCheckInDate;
    }

    private Locator getDateLocator(String date) {
        String cleanDate = date.replace(",", "");
        String[] parts = cleanDate.split(" ");

        String dayOfWeek = parts[0];
        String dayOfMonth = parts[1];
        String monthStr = parts[2];
        String yearStr = parts[3];

        if (dayOfMonth.length() == 1) {
            dayOfMonth = "0" + dayOfMonth;
        }

        String locatorTextEng = "%s %s %s %s".formatted(dayOfWeek, monthStr, dayOfMonth, yearStr);
        String monthId = CoreFunctions.monthShortToShortEngToId(monthStr);
        if (monthId == null) {
            monthId = CoreFunctions.monthEngToId(monthStr);
        }
        String locatorTextId = "%s %s %s %s".formatted(dayOfWeek, monthId, dayOfMonth, yearStr);

        return new Locator(locatorTextEng, By.xpath(
                "//*[contains(@aria-label, '" + locatorTextEng + "') or contains(@aria-label, '" + locatorTextId + "')]"));
    }

    public LocalDate changeTravelDateSelectCheckOutDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale.ENGLISH);

        WebElement inputElement = driverTools.waitForElementToAppear(CHECKOUT_DATE_INPUT_FIELD)
                .orElseThrow(() -> new RuntimeException("Check-out input field not found on page!"));

        String currentText = inputElement.getAttribute("value");
        if (currentText == null || currentText.isEmpty()) {
            throw new IllegalStateException("The check-out date input field is empty. Cannot calculate offset.");
        }

        return LocalDate.parse(currentText, formatter);
    }

    public void changeTravelDateSelectRandomReason() {
        WebElement dropdownInput = driverTools.waitForElementToAppear(REASON_DROPDOWN_INPUT)
                .orElseThrow(() -> new RuntimeException("Reason dropdown input field was not found!"));

        driverTools.clickWithJavascriptExecutor(REASON_DROPDOWN_INPUT);

        List<WebElement> options = driverTools.waitForElementToBeClickable(REASON_OPTIONS)
                .map(ignored -> driverTools.elements(REASON_OPTIONS).orElseThrow())
                .orElseThrow(() -> new RuntimeException("Reason options did not become interactable in time!"));

        if (options.isEmpty()) {
            throw new IllegalStateException("Dropdown menu opened, but no options were found inside it.");
        }

        int randomIndex = CoreFunctions.generateRandomInt(0, options.size() - 1);
        WebElement selectedOption = options.get(randomIndex);

        String chosenText = selectedOption.getText();
        logger.log("Randomly selecting reason: " + chosenText);
        selectedOption.click();
    }

    public void changeTravelDateFillDescription(String descriptionText) {
        WebElement textarea = driverTools.waitForElementToAppear(DESCRIPTION_TEXT_AREA)
                .orElseThrow(() -> new RuntimeException("Description textarea field was not found!"));

        driverTools.clickWithJavascriptExecutor(DESCRIPTION_TEXT_AREA);
        textarea.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        textarea.sendKeys(descriptionText);
        textarea.sendKeys(Keys.TAB);
    }

    public boolean checkIfChangeTravelDateCheckBoxClickableOrNot() {
        Locator CHECKBOX_LABEL_LOCATOR = new Locator("CHECKBOX_LABEL_LOCATOR",
                By.xpath("//div[@class='section-right']//label[contains(text(),'Check')]"));

        return driverTools.waitForElementToBeClickable(CHECKBOX_LABEL_LOCATOR).isPresent();
    }

    public void clickChangeTravelDateCheckBox() {
        Locator CHECKBOX_LOCATOR = new Locator("CHECKBOX_LABEL_LOCATOR",
                By.xpath("//div[@class='section-right']//label[contains(text(),'Check')]"));

        driverTools.waitForElementToAppear(CHECKBOX_LOCATOR).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CHECKBOX_LOCATOR);
    }

    public boolean clickChangeTravelDateSubmitButton(){
        Locator SUBMIT_BTN = new Locator("SUBMIT_BTN",By.xpath("//div[@class='section-right']//button[@type='submit']"));
        if (!driverTools.waitForElementToBeClickable(SUBMIT_BTN).isPresent()){
            return false;
        }
        driverTools.clickWithJavascriptExecutor(SUBMIT_BTN);
        return true;
    }

    public boolean isModalLoadedWhenChangeTravelDate() {
        return driverTools.waitForElementToAppear(CHANGE_TRAVEL_DATE_MODAL_BODY_CONTAINER)
                .map(WebElement::isDisplayed)
                .orElse(false);
    }

    public boolean isCloseButtonClickableWhenChangeTravelDate() {
        return driverTools.waitForElementToBeClickable(CHANGE_TRAVEL_DATE_MODAL_CLOSE_BTN).isPresent();
    }

    public boolean isOkButtonClickableWhenChangeTravelDate() {
        return driverTools.waitForElementToBeClickable(CHANGE_TRAVEL_DATE_MODAL_OK_BTN).isPresent();
    }

    public void clickOkButtonWhenChangeTravelDate() {
        driverTools.waitForElementToBeClickable(CHANGE_TRAVEL_DATE_MODAL_OK_BTN)
                .orElseThrow(() -> new RuntimeException("Modal OK button was not clickable!"));

        driverTools.clickWithJavascriptExecutor(CHANGE_TRAVEL_DATE_MODAL_OK_BTN);
        logger.log("Successfully clicked modal OK button.");
    }

    public String getModalTitleWhenChangeTravelDate() {
        return driverTools.waitForElementToAppear(CHANGE_TRAVEL_DATE_MODAL_TITLE)
                .map(WebElement::getText)
                .orElse("")
                .trim();
    }

    public String getModalDescriptionWhenChangeTravelDate() {
        return driverTools.waitForElementToAppear(CHANGE_TRAVEL_DATE_MODAL_DESC)
                .map(WebElement::getText)
                .orElse("")
                .trim();
    }

    public boolean isReportFailedCheckInPageLoaded(){
            return driverTools.waitForElementToAppear(SELECT_CATEGORY).isPresent()
                    && driverTools.waitForElementToAppear(SELECT_CHECKIN_PROBLEM).isPresent()
                    && driverTools.waitForElementToAppear(SELECT_REPORTED_FROM).isPresent()
                    && driverTools.waitForElementToAppear(SELECT_ROOT_CAUSE).isPresent();
    }

    public String getNoDataAvailableErrorMessage() {
        Locator NO_DATA_AVAILABLE = new Locator("NO_DATA_AVAILABLE", By.xpath("//span[normalize-space()='Booking not found']/parent::div"));
        return driverTools.waitForElementToAppear(NO_DATA_AVAILABLE).orElseThrow().getText().replace("\n", ". ");
    }

    private boolean isElementDisabled(Locator locator) {
        WebElement element = driverTools.waitForElementToAppear(locator)
                .orElseThrow(() -> new RuntimeException("Target locator not found on page: " + locator.toString()));

        if (!element.isEnabled()) {
            return true;
        }

        String disabledAttribute = element.getAttribute("disabled");
        if (disabledAttribute != null && (disabledAttribute.equalsIgnoreCase("true") || disabledAttribute.equalsIgnoreCase("disabled"))) {
            return true;
        }

        String ariaDisabled = element.getAttribute("aria-disabled");
        if (ariaDisabled != null && ariaDisabled.equalsIgnoreCase("true")) {
            return true;
        }

        String classAttribute = element.getAttribute("class");
        if (classAttribute != null) {
            String lowerClass = classAttribute.toLowerCase();
            if (lowerClass.contains("disabled") || lowerClass.contains("pointer-events-none")) {
                return true;
            }
        }

        return false;
    }

    public boolean isSearchButtonDisabled(){
        return isElementDisabled(SEARCH_BUTTON);
    }

    public boolean isResetButtonDisabled(){
        return isElementDisabled(RESET_BUTTON);
    }
    public boolean isResendEmailDisplayed(){
        driverTools.waitForElementToAppear(RESEND_CANC_EMAIL);
        return driverTools.isElementDisplayed(RESEND_CANC_EMAIL);
    }

    public void clickResendCancEmail(){
        driverTools.click(RESEND_CANC_EMAIL);
    }

    public String getSnackbarText(){
        driverTools.waitForElementToAppear(SNACKBAR_TEXT);
        return driverTools.getText(SNACKBAR_TEXT).orElseThrow();
    }
}


