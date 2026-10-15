package com.tiket.web.dashboard.page.b2b_external.reschedule_List;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RescheduleListPageWeb<T extends WebDriver> extends WebPage<T> {

    public RescheduleListPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> RescheduleListPageWeb<?> getInstance(Session<T> session) {
        return switch (Configs.PLATFORM) {
            case DWEB -> new RescheduleListPageDWeb<>(session);
            case AMWEB, IMWEB, ANDROID, IOS -> new RescheduleListPageMWeb<>(session);
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        };
    }

    public static final Locator SEARCH_BTN = new Locator("SEARCH_BTN", By.xpath("//button[text()='SEARCH']"));
    public static final Locator HOTEL_BTN = new Locator("HOTEL_BTN", By.xpath("//a[@data-testid='verticalTab-hotel']"));
    public static final Locator HOTEL_LOCATION = new Locator("HOTEL_LOCATION", By.xpath("//span[@data-testid='destination-input-text']"));
    public static final Locator HOTEL_LOCATION_INPUT = new Locator("HOTEL_LOCATION_INPUT", By.xpath("//input[@placeholder='Search by accommodation name, destination, etc.']"));
    public static final Locator HOTEL_FIRST_SUGGESTION = new Locator("HOTEL_FIRST_SUGGESTION", By.xpath("(//p[@data-testid='item-name'])[1]"));
    public static final Locator DATE_CHECKIN = new Locator("DATE_CHECKIN", By.xpath("(//div[contains(@class,'SearchFormLandingPage_input_date')])[1]"));
    public static final Locator SEARCH_HOTEL_BTN = new Locator("SEARCH_HOTEL_BTN", By.xpath("//button[@data-testid='search-button-lp']"));
    public static final Locator BOOK_BTN = new Locator("BOOK_BTN", By.xpath("(//button[contains(@class,'Button_variant_primary') and text()='Book'])[1]"));
    public static final Locator HOTEL_BOOK_BTN = new Locator("HOTEL_BOOK_BTN", By.xpath("//button[@data-testid='footer-button-book']"));
    public static final Locator YES_BTN = new Locator("YES_BTN", By.xpath("//button[normalize-space()='Yes, continue']"));
    public static final Locator CONTINUE_BTN = new Locator("CONTINUE_BTN", By.xpath("//button[normalize-space()='Continue']"));
    public static final Locator GUEST_NAME = new Locator("GUEST_NAME", By.xpath("//div[contains(@class,'FormItem_room_guest_row')]//span[contains(@class,'FormItem_guest_name_text')]"));
    public static final Locator NAME_INPUT = new Locator("NAME_INPUT", By.xpath("//input[@data-testid='guest-form-full-name-text-input']"));
    public static final Locator SAVE_BTN = new Locator("SAVE_BTN", By.xpath("//button[normalize-space()='Save']"));
    public static final Locator SELECT_PAYMENT_BTN = new Locator("SELECT_PAYMENT_BTN", By.xpath("//button[normalize-space()='Select Payment']"));
    public static final Locator APPROVAL_NOTES_INPUT = new Locator("APPROVAL_NOTES_INPUT", By.xpath("//textarea[contains(@placeholder,'Add a note for your approver')]"));
    public static final Locator CONTINUE_PAYMENT_BTN = new Locator("CONTINUE_PAYMENT_BTN", By.xpath("//button[normalize-space()='Continue to payment']"));
    public static final Locator CORPORATE_BILLING_BTN = new Locator("CORPORATE_BILLING_BTN", By.xpath("//button[@data-testid='submit_button']"));
    public static final Locator HOTEL_SELECTED = new Locator("HOTEL_SELECTED", By.xpath("(//span[@data-testid='cancellation-policies-title' and contains(.,'100% refund & reschedule')])[1]"));
    public static final Locator ORDER_ID = new Locator("ORDER_ID", By.xpath("(//div[contains(@class,'OrderCard_oc_header__')]/span)[1]"));
    public static final Locator ORDER_LIST_BTN = new Locator("ORDER_LIST_BTN", By.xpath("//button[contains(@class, 'Button_mode_inverted__kJXmN')]"));
    public static final Locator PROFILE_ICON = new Locator("PROFILE_ICON", By.xpath("//button[contains(@class,'rr___UserModal-module__container')]"));
    public static final Locator YOUR_ORDERS = new Locator("YOUR_ORDERS", By.xpath("//a[contains(text(),'Your Orders')]"));
    public static final Locator HOTEL_SELECTED_RESCHEDULE = new Locator("HOTEL_SELECTED_RESCHEDULE", By.xpath("(//div[contains(@class,'TiketCard_ticket_header')]//p[text()='SDET International'])[1]"));
    public static final Locator RESCHEDULE_BTN = new Locator("RESCHEDULE_BTN", By.xpath("//div[contains(@class,'ActionButton_action_title')]//span[text()='Reschedule']"));
    public static final Locator CHECK_AVAILABILITY_BTN = new Locator("CHECK_AVAILABILITY_BTN", By.xpath("//button[contains(.,'Check availability')]"));
    public static final Locator CHECK_BOX = new Locator("CHECK_BOX", By.xpath("//span[contains(@class,'Check_custom_checkbox')]"));
    public static final Locator RESCHEDULE_CONFIRM_BTN = new Locator("RESCHEDULE_CONFIRM_BTN", By.xpath("//button[contains(@class,'Button_variant_primary') and text()='Request reschedule']"));
    public static final Locator RESCHEDULE_REQUEST = new Locator("RESCHEDULE_REQUEST", By.xpath("//button[.//span[text()='Reschedule Request']]"));
    public static final Locator APPROVAL_NOTES = new Locator("APPROVAL_NOTES", By.xpath("//div[div[text()='Notes from employee']]/div[contains(@class, 'overflow-auto')]"));
    public static final Locator PAY_BTN = new Locator("PAY_BTN", By.xpath("//button[.//span[normalize-space()='Pay with refund deposit']]"));
    public static final Locator SUBMIT_REQUEST_BTN = new Locator("SUBMIT_REQUEST_BTN", By.xpath("//button[@data-testid='continue-submit-refund']"));

    public void clickSearchButton() {
        driverTools.clickWithJavascriptExecutor(SEARCH_BTN);
    }

    public void goToRescheduleRequest() {
        driverTools.waitForElementToAppear(RESCHEDULE_REQUEST);
        driverTools.click(RESCHEDULE_REQUEST);
    }

    public boolean isApprovalNotesVisible(String notes) {
        driverTools.waitForElementToAppear(APPROVAL_NOTES);

        String approvalNotesText = String.valueOf(driverTools.getText(APPROVAL_NOTES));
        return approvalNotesText.contains(notes);
    }

    public void gotoHotelPage() {
        driverTools.dummyWait(4);
        driverTools.waitForElementToAppear(HOTEL_BTN);
        driverTools.click(HOTEL_BTN);
    }

    public void enterHotelLocation(String location) {
        driverTools.waitForElementToAppear(HOTEL_LOCATION);
        driverTools.click(HOTEL_LOCATION);

        driverTools.waitForElementToAppear(HOTEL_LOCATION_INPUT);
        driverTools.sendKeys(HOTEL_LOCATION_INPUT, location);
        driverTools.waitForElementToAppear(HOTEL_FIRST_SUGGESTION);
        driverTools.click(HOTEL_FIRST_SUGGESTION);
    }

    public void enterDate() {
        driverTools.waitForElementToAppear(DATE_CHECKIN);
        driverTools.click(DATE_CHECKIN);

        setStartDate();
        setEndDate();
    }

    public void setStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 10);
        String startDate = new SimpleDateFormat("d MMMM yyyy EEEE", Locale.ENGLISH).format(calendar.getTime());
        Locator START_DATE = new Locator("START_DATE", By.xpath("//span[@aria-label='" + startDate + "']"));
        driverTools.waitForElementToAppear(START_DATE, 10);
        driverTools.click(START_DATE);
    }

    public void setEndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 11);
        String endDate = new SimpleDateFormat("d MMMM yyyy EEEE", Locale.ENGLISH).format(calendar.getTime());
        Locator END_DATE = new Locator("END_DATE", By.xpath("//span[@aria-label='" + endDate + "']"));
        driverTools.waitForElementToAppear(END_DATE, 10);
        driverTools.click(END_DATE);
    }

    public void clickSearchHotelButton() {
        driverTools.waitForElementToAppear(SEARCH_HOTEL_BTN);
        driverTools.click(SEARCH_HOTEL_BTN);
    }

    public void bookHotelWithFullRefundAndReschedule() {
        driverTools.waitForElementToAppear(HOTEL_SELECTED);
        driverTools.click(HOTEL_SELECTED);

        driverTools.waitForElementToBeClickable(HOTEL_BOOK_BTN);
        driverTools.click(HOTEL_BOOK_BTN);

        driverTools.waitForElementToAppear(YES_BTN, 10);
        driverTools.click(YES_BTN);
    }

    public void enterGuestName(String name) {
        driverTools.waitForElementToAppear(GUEST_NAME, 10);
        driverTools.click(GUEST_NAME);

        driverTools.waitForElementToAppear(NAME_INPUT);
        driverTools.clear(NAME_INPUT);
        driverTools.sendKeys(NAME_INPUT, name);

        saveGuestName();

        driverTools.waitForElementToAppear(CONTINUE_BTN, 10);
        driverTools.click(CONTINUE_BTN);
    }

    public void saveGuestName() {
        driverTools.waitForElementToAppear(SAVE_BTN);
        driverTools.click(SAVE_BTN);
    }

    public void selectPaymentMethod() {
        driverTools.waitForElementToAppear(SELECT_PAYMENT_BTN, 10);
        driverTools.click(SELECT_PAYMENT_BTN);
    }

    public void enterApprovalNotes(String notes) {
        driverTools.waitForElementToAppear(APPROVAL_NOTES_INPUT);
        driverTools.click(APPROVAL_NOTES_INPUT);
        driverTools.sendKeys(APPROVAL_NOTES_INPUT, notes);
    }

    public void clickContinueToPayment() {
        driverTools.waitForElementToAppear(CONTINUE_PAYMENT_BTN);
        driverTools.click(CONTINUE_PAYMENT_BTN);
    }

    public void clickPayWithCorporateBilling() {
        driverTools.dummyWait(5);
        driverTools.waitForElementToAppear(CORPORATE_BILLING_BTN, 10);
        driverTools.click(CORPORATE_BILLING_BTN);

        driverTools.waitForElementToAppear(YES_BTN, 10);
        driverTools.click(YES_BTN);
    }

    public void clickSeeOrderList() {
        driverTools.waitForElementToAppear(ORDER_LIST_BTN);
        driverTools.click(ORDER_LIST_BTN);
    }

    private String Id = "";

    public String getOrderID() {
        driverTools.waitForElementToAppear(ORDER_ID);
        Id = driverTools.getText(ORDER_ID).orElse("");
        return Id;
    }

    public void goToOrdersPage() {
        driverTools.waitForElementToAppear(PROFILE_ICON);
        driverTools.click(PROFILE_ICON);

        driverTools.waitForElementToBeClickable(YOUR_ORDERS);
        driverTools.click(YOUR_ORDERS);
    }

    public void rescheduleBooking() {
        driverTools.waitForElementToAppear(HOTEL_SELECTED_RESCHEDULE, 20);
        driverTools.click(HOTEL_SELECTED_RESCHEDULE);

        driverTools.waitForElementToBeClickable(RESCHEDULE_BTN);
        driverTools.click(RESCHEDULE_BTN);

        driverTools.dummyWait(3);
    }

    public void checkAvailability() {
        driverTools.waitForElementToBeClickable(CHECK_AVAILABILITY_BTN);
        driverTools.click(CHECK_AVAILABILITY_BTN);

        driverTools.dummyWait(2);


    }

    public void makeRescheduleRequest() {
        driverTools.waitForElementToBeClickable(BOOK_BTN);
        driverTools.clickWithJavascriptExecutor(BOOK_BTN);
        driverTools.dummyWait(5);

        driverTools.waitForElementToBeClickable(CHECK_BOX);
        driverTools.click(CHECK_BOX);

        driverTools.waitForElementToAppear(CONTINUE_BTN, 10);
        driverTools.clickWithJavascriptExecutor(CONTINUE_BTN);

        driverTools.waitForElementToBeClickable(RESCHEDULE_CONFIRM_BTN);
        driverTools.click(RESCHEDULE_CONFIRM_BTN);
    }

    public void payWithRefundDeposit() {
        driverTools.waitForElementToBeClickable(PAY_BTN, 20);
        driverTools.click(PAY_BTN);

        driverTools.waitForElementToBeClickable(CHECK_BOX);
        driverTools.click(CHECK_BOX);

        driverTools.waitForElementToBeClickable(SUBMIT_REQUEST_BTN, 10);
        driverTools.clickWithJavascriptExecutor(SUBMIT_REQUEST_BTN);
    }
}