package com.tiket.web.dashboard.page.hotel.demand;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Collections;

public class FailedIssuedDashboardPage <T extends WebDriver> extends BasePage<T> {
    public FailedIssuedDashboardPage(Session<T> session) {super(session);}

    private static final Locator FAILED_ISSUED_DASHBOARD_PAGE = new Locator("FAILED_ISSUED_DASHBOARD_PAGE", By.xpath("//span[normalize-space()='Hotel / Failed Issued']"));
    private static final Locator ORDER_ID_INPUT_FIELD = new Locator("ORDER_ID_INPUT_FIELD", By.xpath("//input[@name='orderId']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[@class='btn btn-primary']"));
    private static final Locator ORDER_ID = new Locator("ORDER_ID", By.xpath("//tbody/tr/td[2]"));
    private static final Locator ORDER_DETAILS = new Locator("ORDER_DETAILS", By.xpath("//tbody/tr/td[3]"));
    private static final Locator REFRENCE_ID = new Locator("REFRENCE_ID", By.xpath("//tbody/tr/td[5]"));
    private static final Locator SOURCE = new Locator("SOURCE", By.xpath("//tbody/tr/td[6]"));
    private static final Locator PAYMENT_STATUS = new Locator("PAYMENT_STATUS", By.xpath("//tbody/tr/td[7]"));
    private static final Locator PAYMENT_OPTION = new Locator("PAYMENT_OPTION", By.xpath("//tbody/tr/td[8]"));
    private static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", By.xpath("//tbody/tr/td[9]"));
    private static final Locator BOOKING_DATE = new Locator("BOOKING_DATE", By.xpath("//tbody/tr/td[10]"));
    private static final Locator CHECKIN = new Locator("CHECKIN", By.xpath("//tbody/tr/td[11]"));
    private static final Locator CHECKOUT = new Locator("CHECKOUT", By.xpath("//tbody/tr/td[12]"));
    private static final Locator TOTAL_NIGHT = new Locator("TOTAL_NIGHT", By.xpath("//tbody/tr/td[13]"));
    private static final Locator GUEST_NAME = new Locator("GUEST_NAME", By.xpath("//tbody/tr/td[14]"));
    private static final Locator TOTAL_GUEST = new Locator("TOTAL_GUEST", By.xpath("//tbody/tr/td[15]"));
    private static final Locator TOTAL_ROOM = new Locator("TOTAL_ROOM", By.xpath("//tbody/tr/td[16]"));
    private static final Locator ORDER_ISSUED = new Locator("ORDER_ISSUED", By.xpath("//tbody/tr/td[17]"));
    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", By.xpath("//tbody/tr/td[18]"));
    private static final Locator STATUS = new Locator("STATUS", By.xpath("//tbody/tr/td[19]"));
    private static final Locator REASON = new Locator("REASON", By.xpath("//tbody/tr/td[20]"));
    private static final Locator ACTION_COLUMN = new Locator("ACTION_COLUMN", By.xpath("//tbody/tr/td[21]"));
    private static final Locator INDEX = new Locator("INDEX", By.xpath("//tbody/tr/td[1]"));
    private static final Locator REBOOK_BUTTON = new Locator("REBOOK_BUTTON", By.xpath("//button[text()='Rebooking']"));
    private static final Locator RELOCATION_BUTTON = new Locator("RELOCATION_BUTTON", By.xpath("//button[text()='Relocation']"));
    private static final Locator REMOVE_FROM_LIST_BUTTON = new Locator("REMOVE_FROM_LIST_BUTTON", By.xpath("//button[text()='Remove from list']"));
    private static final Locator REMOVE_CONFIRMATION_POPUP = new Locator("REMOVE_CONFIRMATION_POPUP", By.xpath("//div[@class='modal-content']"));
    private static final Locator BOOKING_STATUS_DROPDOWN = new Locator("BOOKING_STATUS_DROPDOWN", By.xpath("//select[@name='bookingStatus']"));
    private static final Locator REFUNDED = new Locator("REFUNDED_DROPDOWN", By.xpath("//option[text()='REFUNDED']"));
    private static final Locator REASON_DROPDOWN = new Locator("REASON_DROPDOWN", By.xpath("//select[@name='removeListReason']"));
    private static final Locator ISSUED = new Locator("ISSUED", By.xpath("//option[text()='ISSUED']"));
    private static final Locator YES_BUTTON = new Locator("YES_BUTTON", By.xpath("//option[text()='ISSUED']"));
    private static final Locator NO_BUTTON = new Locator("NO_BUTTON", By.xpath("//option[text()='ISSUED']"));
    private static final Locator REBOOK_ROOMS = new Locator("REBOOK_ROOMS", By.xpath("//div[@class='dash-hotel-room-item']"));
    private static final Locator ROOM_IMAGE = new Locator("ROOM_IMAGE", By.xpath("//div[@class='room-photo-gallery']"));
    private static final Locator ROOM_NAME = new Locator("ROOM_NAME", By.xpath("//H3[@class='prc-name']"));
    private static final Locator ROOM_PRICE = new Locator("ROOM_PRICE", By.xpath("//div[@class='room-price']"));
    private static final Locator SELECT_ROOM_BUTTON = new Locator("SELECT_ROOM_BUTTON", By.xpath("//div[@class='prc-section-right-bottom']//button"));
    private static final Locator SELECT_BUTTON = new Locator("SELECT_BUTTON", By.xpath("//button[@type='button']"));
    private static final Locator NEW_HOTEL_PAGE = new Locator("NEW_HOTEL_PAGE", By.xpath("//div[@class='flex flex-col gap-6']"));
    private static final Locator TERMS_CONDITION = new Locator("TERMS_CONDITION", By.xpath("//div[@class='flex items-center justify-between']"));
    private static final Locator TERMS_CHECKBOX = new Locator("TERMS_CHECKBOX", By.xpath("//span[@class='Check_custom_checkbox__jSS6O']"));
    private static final Locator CONTINUE_BOOKING_BUTTON = new Locator("CONTINUE_BOOKING_BUTTON", By.xpath("//button[contains(@class, 'Button_button') and text()='Continue Booking']"));
    private static final Locator REBOOK_POPUP = new Locator("REBOOK_POPUP", By.xpath("//div[contains(@class,'Dialog_pop_up')]"));
    private static final Locator RELOCATION_HOTEL_LIST_NAME_TITLE = new Locator("RELOCATION_HOTEL_LIST_NAME_TITLE", By.xpath("//*[@class='select-hotelroom']//*[contains(@class,'relocation-hotel-title')]"));
    private static final Locator RELOCATION_GET_REFUND_SECTION = new Locator("RELOCATION_GET_REFUND_SECTION", By.xpath("//*[text()='Get Refund']"));
    private static final Locator RELOCATION_PAYMENT_REFUND_DETAILS_MODAL = new Locator("RELOCATION_PAYMENT_REFUND_DETAILS_MODAL", By.xpath("//section//*[text()='Payment & Refund Details']"));
    private static final Locator CHECK_AGAIN_BUTTON = new Locator("CHECK_AGAIN_BUTTON", By.xpath("//button[contains(@class, 'Button_variant_secondary') and normalize-space(text())='Check Again']"));
    private static final Locator CONTINUE_BOOKING_REBOOK_BUTTON = new Locator("CONTINUE_BOOKING_REBOOK_BUTTON", By.xpath("(//button[normalize-space(text())='Continue Booking'])[2]"));
    private static final Locator RELOCATION_CONTINUE_BOOKING_MODAL = new Locator("RELOCATION_CONTINUE_BOOKING_MODAL", By.xpath("//section//*[contains(text(),'Continue to book')]"));
    private static final Locator TABLE_ROW_ELEMENT = new Locator("TABLE_ROW_ELEMENT", By.xpath("//div[contains(@class,'table-responsive')]//table//tbody//tr//td"));
    private static final Locator CHECKIN_START_INPUT = new Locator("CHECKIN_START_INPUT", By.xpath("//input[@id='checkin_start']"));
    private static final Locator CHECKIN_END_INPUT = new Locator("CHECKIN_END_INPUT", By.xpath("//input[@id='checkin_end']"));
    private static final Locator BOOKING_START_INPUT = new Locator("BOOKING_START_INPUT", By.xpath("//input[@id='booking_start']"));
    private static final Locator BOOKING_END_INPUT = new Locator("BOOKING_END_INPUT", By.xpath("//input[@id='booking_end']"));
    private static final Locator TABLE_HEADINGS = new Locator("TABLE_HEADINGS", By.xpath("//div[contains(@class,'table-responsive')]//table//thead//tr/th"));
    private static final Locator LEFT_MONTH_HEADING = new Locator("LEFT_MONTH_HEADING", By.xpath("(//div[contains(@class,'CalendarMonth')][@data-visible='true']//div[contains(@class,'CalendarMonth_caption')])[1]"));
    private static final Locator PREVIOUS_MONTH_BUTTON = new Locator("PREVIOUS_MONTH_BUTTON", By.xpath("//div[contains(@aria-label,'previous') or contains(@aria-label,'back')]//*[name()='svg']"));
    private static final Locator NEXT_MONTH_BUTTON = new Locator("NEXT_MONTH_BUTTON", By.xpath("//div[contains(@aria-label,'next') or contains(@aria-label,'forward')]//*[name()='svg']"));
    private static final Locator RELOCATION_SEARCH_BUTTON = new Locator("RELOCATION_SEARCH_BUTTON", By.xpath("//button[@type='button'][contains(normalize-space(.),'Search')]"));
    private static final Locator RELOCATION_SEARCH_INPUT = new Locator("RELOCATION_SEARCH_INPUT", By.xpath("//input[@name='hotelName']"));
    private static final Locator RELOCATION_EMPTY_RESULT = new Locator("RELOCATION_EMPTY_RESULT", By.xpath("//*[@class='select-hotelroom']//*[contains(@class,'empty-result')]"));
    private static final Locator RELOCATION_ROOM_BACK_ARROW = new Locator("RELOCATION_ROOM_BACK_ARROW", By.xpath("//*[contains(@class,'rebooking')]//*[contains(@class,'arrow-left') or contains(@class,'back')]"));
    private static final Locator RELOCATION_PAGE_ROOM_FILTER_CHIPS = new Locator("RELOCATION_PAGE_ROOM_FILTER_CHIPS", By.xpath("//div[contains(@class,'room-filter')][contains(@class,'item')]"));
    private static final Locator REFUND_RESCHEDULE_DETAILS = new Locator("REFUND_RESCHEDULE_DETAILS", By.xpath("//span[contains(text(),'refund') or contains(text(),'reschedule')]"));
    private static final Locator REFUND_RESCHEDULE_DETAILS_BUTTON = new Locator("REFUND_RESCHEDULE_DETAILS_BUTTON", By.xpath("//span[contains(text(),'refund') or contains(text(),'reschedule')]//ancestor::div[2]//span[contains(text(),'See details')]"));
    private static final Locator MODAL_REFUND_RESCHEDULE_DETAILS = new Locator("MODAL_REFUND_RESCHEDULE_DETAILS", By.xpath("//div[contains(@class,'modal')]//span[contains(text(),'refund') or contains(text(),'reschedule')]"));
    private static final Locator MODAL_CLOSE_BUTTON = new Locator("MODAL_CLOSE_BUTTON", By.xpath("//div[contains(@class,'modal')]//button[contains(@data-testid,'close-button')]/*[name()='svg']"));
    private static final Locator MODAL_BOX = new Locator("MODAL_BOX", By.xpath("//div[contains(@class,'modal')]"));
    private static final Locator SUCCESS_PAGE = new Locator("SUCCESS_PAGE", By.xpath("//div[@data-testid='layout-container']//span"));
    private static final Locator SINGLE_CHIP = new Locator("SINGLE_CHIP", By.xpath("//div[contains(@class, 'room-filter-component__item')]"));
    private static final Locator ROOM_CARDS = new Locator("ROOM_CARDS", By.xpath("//div[contains(@class, 'post-room-card')]"));
    private static final Locator SEE_MORE_BUTTON = new Locator("SEE_MORE_BUTTON", By.xpath("//div[contains(@class, 'see-more-room') and contains(text(), 'See more rooms')]"));
    private static final Locator HIDE_BUTTON = new Locator("HIDE_BUTTON", By.xpath("//div[contains(@class, 'see-more-room') and normalize-space()='Hide']"));

    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter TARGET_UI_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter HEADER_FORMATTER = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter FORMATTER_ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter FULL_INPUT_FORMATTER = DateTimeFormatter.ofPattern("E, d MMM yyyy", Locale.ENGLISH);

    private static Locator getFilterChipByName(String filterChip){
        return new Locator("RELOCATION_PAGE_ROOM_FILTER_CHIPS", By.xpath("//div[contains(@class,'room-filter')][contains(@class,'item')][normalize-space(.)=%s]".formatted(CoreFunctions.toXPathLiteral(filterChip))));
    }
    private static Locator getSelectedFilterChipByName(String filterChip){
        return new Locator("RELOCATION_PAGE_ROOM_FILTER_CHIPS", By.xpath("//div[contains(@class,'room-filter')][contains(@class,'item')][contains(@class,'selected')][normalize-space(.)=%s]".formatted(CoreFunctions.toXPathLiteral(filterChip))));
    }
    private static Locator getUnselectedFilterChipByName(String filterChip){
        return new Locator("RELOCATION_PAGE_ROOM_FILTER_CHIPS", By.xpath("//div[contains(@class,'room-filter')][contains(@class,'item')][not(contains(@class,'selected'))][normalize-space(.)=%s]".formatted(CoreFunctions.toXPathLiteral(filterChip))));
    }
    private static Locator getRelocationHotelListNameByHotelName(String hotelName) {
        return new Locator("RELOCATION_HOTEL_LIST_NAME_TITLE", By.xpath(String.format("//*[contains(@class,'relocation-hotel-title')]//span[contains(text(),'%s')]", hotelName)));
    }

    private static Locator getRelocationHotelListPriceByHotelName(String hotelName) {
        return new Locator("RELOCATION_HOTEL_LIST_PRICE", By.xpath(String.format("//*[contains(@class,'relocation-hotel-title')]//span[contains(text(),'%s')]//ancestor::*[@class='section-right']//*[contains(@class,'after-price')]", hotelName)));
    }

    private static Locator getTableColumnLocator(int index){
        return new Locator("TABLE_COLUMN_" + index, By.xpath("//div[contains(@class,'table-responsive')]//tbody//tr//td[%d]".formatted(index)));
    }

    public boolean isFailedIssuedDashboardPageLoaded(){
        return driverTools.waitForElementsToAppear(FAILED_ISSUED_DASHBOARD_PAGE).isPresent();
    }

    public void inputOrderId(String id){
        driverTools.clearInput(ORDER_ID_INPUT_FIELD);
        driverTools.sendKeys(ORDER_ID_INPUT_FIELD, id);
    }

    public void clickSearch(){
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
    }

    public String isOrderIdBoxLoaded(){
        WebElement orderId=driverTools.waitForElementToAppear(ORDER_ID).orElseThrow();
        return driverTools.getText(orderId);
    }
    public String isOrderStatusLoaded(){
        WebElement orderStatusElement = driverTools.waitForElementToAppear(ORDER_ISSUED).orElseThrow();
        String orderStatus = driverTools.getText(orderStatusElement).trim();
        if (orderStatus.isEmpty()) {
            orderStatus = orderStatusElement.getAttribute("textContent").trim();
        }
        return orderStatus;
    }

    public boolean isOrderDetailsLoaded(){
        return driverTools.waitForElementsToAppear(ORDER_DETAILS).isPresent();
    }

    public boolean isRefrenceLoaded(){
        return driverTools.waitForElementsToAppear(REFRENCE_ID).isPresent();
    }

    public boolean isSourceLoaded(){
        return driverTools.waitForElementsToAppear(SOURCE).isPresent();
    }

    public boolean isPaymentStatusLoaded(){
        return driverTools.waitForElementsToAppear(PAYMENT_STATUS).isPresent();
    }

    public boolean isPaymentOptionLoaded(){
        return driverTools.waitForElementsToAppear(PAYMENT_OPTION).isPresent();
    }

    public boolean isHotelNameLoaded(){
        return driverTools.waitForElementsToAppear(HOTEL_NAME).isPresent();
    }

    public boolean isBookingDateLoaded(){
        return driverTools.waitForElementsToAppear(BOOKING_DATE).isPresent();
    }

    public boolean isCheckinLoaded(){
        return driverTools.waitForElementsToAppear(CHECKIN).isPresent();
    }

    public boolean isCheckOutLoaded(){
        return driverTools.waitForElementsToAppear(CHECKOUT).isPresent();
    }

    public boolean isTotalNightLoaded(){
        return driverTools.waitForElementsToAppear(TOTAL_NIGHT).isPresent();
    }

    public boolean isGuestNameLoaded(){
        return driverTools.waitForElementsToAppear(GUEST_NAME).isPresent();
    }

    public boolean isTotalGuestLoaded(){
        return driverTools.waitForElementsToAppear(TOTAL_GUEST).isPresent();
    }

    public boolean isTotalRoomLoaded(){
        return driverTools.waitForElementsToAppear(TOTAL_ROOM).isPresent();
    }

    public boolean isOrderIssuedLoaded(){
        return driverTools.waitForElementsToAppear(ORDER_ISSUED).isPresent();
    }

    public boolean isTotalPriceLoaded(){
        return driverTools.waitForElementsToAppear(TOTAL_PRICE).isPresent();
    }

    public boolean isStatusLoaded(){
        return driverTools.waitForElementsToAppear(STATUS).isPresent();
    }

    public boolean isReasonLoaded(){
        return driverTools.waitForElementsToAppear(REASON).isPresent();
    }

    public boolean isActionColumnLoaded(){
        return driverTools.waitForElementsToAppear(ACTION_COLUMN).isPresent();
    }

    public boolean isIndexLoaded(){
        return driverTools.waitForElementsToAppear(INDEX).isPresent();
    }

    public boolean isRebookingButtonLoaded(){
        return driverTools.waitForElementsToAppear(REBOOK_BUTTON).isPresent();
    }

    public boolean isRelocationButtonLoaded(){
        return driverTools.waitForElementsToAppear(RELOCATION_BUTTON).isPresent();
    }

    public boolean isRemoveFromListButtonLoaded(){
        return driverTools.waitForElementsToAppear(REMOVE_FROM_LIST_BUTTON).isPresent();
    }

    public boolean isReebookingButtonClickable(){
        return driverTools.waitForElementToBeClickable(REBOOK_BUTTON).isPresent();
    }

    public boolean isRelocationButtonClickable(){
        return driverTools.waitForElementToBeClickable(RELOCATION_BUTTON).isPresent();
    }

    public boolean isRemoveFromListButtonClickable(){
        return driverTools.waitForElementToBeClickable(REMOVE_FROM_LIST_BUTTON).isPresent();
    }

    public void clickRemoveFromListButton(){
        WebElement button = driverTools.waitForElementToBeClickable(REMOVE_FROM_LIST_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isRemoveConfirmationPopupLoaded(){
        return driverTools.waitForElementsToAppear(REMOVE_CONFIRMATION_POPUP).isPresent();
    }

    public void clickBookingStatusDropdown(){
        driverTools.click(BOOKING_STATUS_DROPDOWN);
        driverTools.waitForElementToBeClickable(REFUNDED);
    }

    public void selectRefunded(){
        driverTools.click(REFUNDED);
    }

    public void clickReasonDropdown(){
        driverTools.click(REASON_DROPDOWN);
        driverTools.waitForElementToBeClickable(ISSUED);
    }

    public void selectIssued(){
        driverTools.click(ISSUED);
    }

    public boolean isYesButtonClickable(){
        return driverTools.waitForElementToBeClickable(YES_BUTTON).isPresent();
    }

    public boolean isNoButtonClickable(){
        return driverTools.waitForElementToBeClickable(NO_BUTTON).isPresent();
    }

    public boolean isRoomLoaded(){
        return driverTools.waitForElementsToAppear(REBOOK_ROOMS,10).isPresent();
    }

    public boolean isRoomImageLoaded(){
        return driverTools.waitForElementsToAppear(ROOM_IMAGE).isPresent();
    }

    public boolean isRoomNameLoaded(){
        return driverTools.waitForElementsToAppear(ROOM_NAME).isPresent();
    }

    public boolean isRoomPriceLoaded(){
        return driverTools.waitForElementsToAppear(ROOM_PRICE).isPresent();
    }

    public boolean isSelectButtonClickable(){
        return driverTools.waitForElementToBeClickable(SELECT_BUTTON).isPresent();
    }

    public void clickRebookingButton(){
        driverTools.click(REBOOK_BUTTON);
    }

    public void clickRelocationButton(){
        driverTools.click(RELOCATION_BUTTON);
    }

    public boolean isRelocationHotelListDisplayed() {
        return driverTools.waitForElementsToAppear(RELOCATION_HOTEL_LIST_NAME_TITLE).isPresent();
    }

    public void selectRelocationHotel() {
        driverTools.scrollAndClick(RELOCATION_HOTEL_LIST_NAME_TITLE);
    }

    public void selectRelocationHotelByHotelName(String hotelName) {
        WebElement element = driverTools.waitForElementToPresence(getRelocationHotelListPriceByHotelName(hotelName)).orElseThrow();
        driverTools.scrollElementToCenter(element);
        driverTools.click(getRelocationHotelListNameByHotelName(hotelName));
    }

    public void clickSelectButton(){
        driverTools.clickWithJavascriptExecutor(SELECT_BUTTON);
    }

    public void clickSelectRoom() {
        List<WebElement> elements = driverTools.waitForElementsToAppear(SELECT_ROOM_BUTTON).orElseThrow();
        driverTools.scrollElementToCenter(elements.get(0));
        driverTools.click(SELECT_ROOM_BUTTON);
    }

    public boolean isNewHotelPageLoaded(){
        return driverTools.waitForElementsToAppear(NEW_HOTEL_PAGE).isPresent();
    }

    public boolean isTermsAndConditionLoaded(){
        return driverTools.waitForElementsToAppear(TERMS_CONDITION).isPresent();
    }

    public boolean isCheckBoxLoaded(){
        return driverTools.waitForElementsToAppear(TERMS_CHECKBOX).isPresent();
    }

    public void clickCheckBoxButton(){
        driverTools.waitForElementToAppear(TERMS_CHECKBOX, 60);
        driverTools.clickWithJavascriptExecutor(TERMS_CHECKBOX);
    }

    public boolean isContinueBookingButtonClickable(){
        return driverTools.waitForElementToAppear(CONTINUE_BOOKING_BUTTON).isPresent();
    }

    public boolean isCheckAgainButtonClickable(){
        return driverTools.waitForElementToAppear(CHECK_AGAIN_BUTTON).isPresent();
    }

    public void clickContinueBookingButton(){
        driverTools.click(CONTINUE_BOOKING_BUTTON);
    }

    public void clickCheckAgainButton(){
        driverTools.click(CHECK_AGAIN_BUTTON);
    }

    public boolean isRebookPopUpLoaded(){
        return driverTools.waitForElementsToAppear(REBOOK_POPUP).isPresent();
    }

    public void clickGetRefund() {
        driverTools.click(RELOCATION_GET_REFUND_SECTION);
    }

    public boolean isPaymentAndRefundDetailsModalShown() {
        return driverTools.waitForElementsToAppear(RELOCATION_PAYMENT_REFUND_DETAILS_MODAL).isPresent();
    }

    public boolean isContinueBookingRebookButtonClickable(){
        return driverTools.waitForElementToBeClickable(CONTINUE_BOOKING_REBOOK_BUTTON).isPresent();
    }

    public void clickContinueBookingRebookButton(){
        driverTools.click(CONTINUE_BOOKING_REBOOK_BUTTON);
    }

    public boolean isContinueToBookModalShown() {
        return driverTools.waitForElementToDisappear(RELOCATION_CONTINUE_BOOKING_MODAL);
    }

    public boolean isOrderIdInputClickable() {
        return isInputClickable(ORDER_ID_INPUT_FIELD);
    }
    public boolean isOrderIdInputSelectableAndEditable() {
        return isInputSelectableAndEditable(ORDER_ID_INPUT_FIELD);
    }

    public String getOrderIdInputValue(){
        return driverTools.getValue(ORDER_ID_INPUT_FIELD);
    }

    public boolean isInputSelectableAndEditable(Locator inputLocator) {
        try {
            WebElement input = driverTools.waitForElementToBeClickable(inputLocator).orElseThrow();
            boolean isEnabled = input.isEnabled();
            String readonlyAttr = input.getAttribute("readonly");
            boolean isReadOnly = readonlyAttr != null && (readonlyAttr.equalsIgnoreCase("true") || readonlyAttr.equalsIgnoreCase("readonly"));

            return isEnabled && !isReadOnly;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isInputClickable(Locator inputLocator) {
        try {
            driverTools.waitForElementToBeClickable(inputLocator, 5).orElseThrow();
            return true;
        } catch (Exception e) {
            logger.log("Element is not clickable: " + inputLocator.name());
            return false;
        }
    }


    public String getTableNoDataMessage(){
        WebElement element = driverTools.waitForElementToAppear(TABLE_ROW_ELEMENT).orElseThrow();
        String text = driverTools.getText(element);
        if (text.isEmpty()) {
            text = element.getAttribute("textContent").trim();
        }
        return text;
    }

    public boolean isCheckInInputClickable() {
        return isInputClickable(CHECKIN_START_INPUT) && isInputClickable(CHECKIN_END_INPUT);
    }
    public boolean isCheckInInputSelectableAndEditable() {
        return isInputSelectableAndEditable(CHECKIN_START_INPUT) && isInputSelectableAndEditable(CHECKIN_END_INPUT);
    }
    public boolean isBookingInputClickable() {
        return isInputClickable(BOOKING_START_INPUT) &&  isInputClickable(BOOKING_END_INPUT);
    }
    public boolean isBookingInputSelectableAndEditable() {
        return isInputSelectableAndEditable(BOOKING_START_INPUT) && isInputSelectableAndEditable(BOOKING_END_INPUT);
    }

    private Locator getDateLocatorByDate(String rawDate) {
        if (rawDate == null || rawDate.isBlank()) {
            throw new IllegalArgumentException("Date string cannot be null or empty");
        }
        LocalDate parsedDate = parseFlexibleDate(rawDate.trim());
        String targetAriaLabel = parsedDate.format(TARGET_UI_FORMATTER);
        String xpath = "//div[contains(@class,'Calendar')]//*[contains(@aria-label, '%s')]".formatted(targetAriaLabel);
        return new Locator("Calendar Day Element: " + targetAriaLabel, By.xpath(xpath));
    }

    private LocalDate parseFlexibleDate(String dateStr) {
        logger.log("Formatting %s to format: ".formatted(dateStr) + INPUT_FORMATTER);
        String cleanDate = dateStr.trim().replace("\u2011", "-");
        String standardizedUsString;

        if (cleanDate.contains("-")) {
            standardizedUsString = LocalDate.parse(cleanDate, FORMATTER_ISO).format(INPUT_FORMATTER);
        } else {
            standardizedUsString = LocalDate.parse(cleanDate, INPUT_FORMATTER).format(INPUT_FORMATTER);
        }

        logger.log("Conversion successful '%s' => %s".formatted(dateStr, standardizedUsString));
        return LocalDate.parse(standardizedUsString, INPUT_FORMATTER);
    }

    private void navigateToTargetMonth(String targetDateStr) {
        LocalDate parsedDate = LocalDate.parse(targetDateStr.trim(), INPUT_FORMATTER);
        YearMonth targetYearMonth = YearMonth.from(parsedDate);
        logger.log("Target Year Month: " + targetYearMonth.format(HEADER_FORMATTER));

        int maxAttempts = 24;
        for (int i = 0; i < maxAttempts; i++) {
            WebElement leftHeaderElement = driverTools.waitForElementToAppear(LEFT_MONTH_HEADING)
                    .orElseThrow(() -> new IllegalStateException("Calendar left month header was not visible."));

            String rawHeaderText = leftHeaderElement.getText().trim();
            if (rawHeaderText.isEmpty()) {
                rawHeaderText = leftHeaderElement.getAttribute("textContent").trim();
            }

            YearMonth displayedLeftMonth = YearMonth.parse(rawHeaderText, HEADER_FORMATTER);
            if (targetYearMonth.equals(displayedLeftMonth) || targetYearMonth.equals(displayedLeftMonth.plusMonths(1))) {
                return;
            }

            WebElement monthChangeButton;
            YearMonth expectedLeftMonth;
            if (targetYearMonth.isBefore(displayedLeftMonth)) {
                monthChangeButton = driverTools.waitForElementToAppear(PREVIOUS_MONTH_BUTTON)
                        .orElseThrow(() -> new IllegalStateException("Previous month button was not visible."));
                expectedLeftMonth = displayedLeftMonth.minusMonths(1);
            } else {
                monthChangeButton = driverTools.waitForElementToAppear(NEXT_MONTH_BUTTON)
                        .orElseThrow(() -> new IllegalStateException("Next month button was not visible."));
                expectedLeftMonth = displayedLeftMonth.plusMonths(1);
            }
            driverTools.clickSvgOrNonHtmlElements(monthChangeButton);

            try {
                Thread.sleep(250);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while navigating calendar months", ie);
            }

            String expectedText = expectedLeftMonth.format(HEADER_FORMATTER);
            logger.log("Waiting for calendar header transition: From '"
                    + displayedLeftMonth.format(HEADER_FORMATTER) + "' to '" + expectedText + "'");

            if (!driverTools.waitForTextToBePresent(LEFT_MONTH_HEADING, expectedText, 5)) {
                throw new IllegalStateException("Calendar month header failed to update to '"
                        + expectedText + "' after navigation click execution.");
            }
        }
    }

    private void setDate(Locator inputLocator, String date1, String date2){
        driverTools.click(inputLocator);

        navigateToTargetMonth(date1);
        Locator firstDateLocator = getDateLocatorByDate(date1);
        driverTools.waitForElementToAppear(firstDateLocator);
        driverTools.click(firstDateLocator);

        navigateToTargetMonth(date2);
        Locator secondDateLocator = getDateLocatorByDate(date2);
        driverTools.waitForElementToAppear(secondDateLocator);
        driverTools.click(secondDateLocator);
    }

    public void setBookingDate(String date1, String date2) {
        logger.log("Selected Booking Date Range: %s - %s".formatted(date1, date2));
        clearBookingDate();
        setDate(BOOKING_START_INPUT, date1, date2);
    }

    public void setCheckInDate(String date1, String date2) {
        logger.log("Selected CheckIn Date Range: %s - %s".formatted(date1, date2));
        clearCheckInDate();
        setDate(CHECKIN_START_INPUT, date1, date2);
    }

    public void clearBookingDate() {
        driverTools.clearInput(BOOKING_START_INPUT);
        driverTools.clearInput(BOOKING_END_INPUT);
    }

    public void clearCheckInDate() {
        driverTools.clearInput(CHECKIN_START_INPUT);
        driverTools.clearInput(CHECKIN_END_INPUT);
    }

    public String getBookingDate() {
        String date1 =  driverTools.getValue(BOOKING_START_INPUT);
        String date2 =  driverTools.getValue(BOOKING_END_INPUT);
        return date1 + " - " + date2;
    }

    public String getCheckInDate() {
        String date1 =  driverTools.getValue(CHECKIN_START_INPUT);
        String date2 =  driverTools.getValue(CHECKIN_END_INPUT);
        return date1 + " - " + date2;
    }

    private int getTableHeadingIndex(String heading) {
        driverTools.waitForElementToAppear(TABLE_HEADINGS);
        List<WebElement> headings = driverTools.elements(TABLE_HEADINGS).orElseThrow(() -> new IllegalStateException("No headings found"));
        if (headings.isEmpty()) {
            return -1;
        }

        for (int i = 0; i < headings.size(); i++) {
            String text = headings.get(i).getText().trim();
            if (text.isEmpty()) {
                text = headings.get(i).getAttribute("textContent").trim();
            }
            if (text.equalsIgnoreCase(heading)) {
                return i;
            }
        }
        return -1;
    }

    public List<String> getTableValuesByHeading(String heading) {
        List<String> values = new ArrayList<>();
        int index = getTableHeadingIndex(heading);
        if (index == -1) {
            return values;
        }

        Locator COLUMN_VALUES = getTableColumnLocator(index + 1);
        driverTools.waitForElementToAppear(COLUMN_VALUES);
        List<WebElement> columnValues = driverTools.elements(COLUMN_VALUES)
                .orElseThrow(() -> new IllegalStateException("No column values found for Heading: %s".formatted(heading)));

        for (WebElement element : columnValues) {
            String text =  element.getText().trim();
            if (text.isEmpty()) {
                text = element.getAttribute("textContent").trim();
            }
            values.add(text);
        }

        return values;
    }

    public boolean areDatesWithInRange(String dateRange1, String dateRange2, String date) {
        logger.log("Checking range layout: %s <= %s <= %s".formatted(dateRange1, date, dateRange2));

        LocalDate masterStart = parseFlexibleDate(dateRange1);
        LocalDate masterEnd = parseFlexibleDate(dateRange2);
        LocalDate targetDate = parseFlexibleDate(date);

        logger.log("Parsed Evaluation: %s <= %s <= %s".formatted(masterStart, targetDate, masterEnd));
        boolean isAfterOrEqualStart = !targetDate.isBefore(masterStart);
        boolean isBeforeOrEqualEnd = !targetDate.isAfter(masterEnd);

        return isAfterOrEqualStart && isBeforeOrEqualEnd;
    }

    public String getTableValueByHeading(String heading) {
        int index = getTableHeadingIndex(heading);
        if (index == -1) {
            logger.log("Heading: %s not found returning empty value".formatted(heading));
            return "";
        }

        Locator COLUMN_VALUES = getTableColumnLocator(index + 1);
        driverTools.waitForElementToAppear(COLUMN_VALUES);
        WebElement element = driverTools.firstElement(COLUMN_VALUES)
                .orElseThrow(() -> new IllegalStateException("No column value found for Heading: %s".formatted(heading)));

        String text =  element.getText().trim();
        if (text.isEmpty()) {
            text = element.getAttribute("textContent").trim();
        }

        logger.log("Got %s value for heading: %s".formatted(text, heading));
        return text;
    }

    public String getRelocationHotelNameByIndex(int index) {
        String xpathStr = "(//*[@class='select-hotelroom']//*[contains(@class,'relocation-hotel-title')])[%d]//span[1]".formatted(index);
        Locator HOTEL_NAME_BY_INDEX = new Locator("HOTEL_NAME_BY_INDEX", By.xpath(xpathStr));
        driverTools.waitForElementToAppear(HOTEL_NAME_BY_INDEX);
        WebElement hotelNameElement = driverTools.waitForElementToAppear(HOTEL_NAME_BY_INDEX).orElseThrow(() -> new IllegalStateException("No hotel name found for index: %d".formatted(index)));

        String hotelName = hotelNameElement.getText().trim();
        if (hotelName.isEmpty()) {
            hotelName = hotelNameElement.getAttribute("textContent").trim();
        }

        logger.log("Getting Hotel name for Hotel Number %d ==> %s".formatted(index, hotelName));
        return hotelName;
    }
    public List<String> getRelocationAllHotelName() {
        List<String>  hotelNames = new ArrayList<>();
        driverTools.waitForElementToAppear(RELOCATION_HOTEL_LIST_NAME_TITLE);
        List<WebElement> hotelNameElements = driverTools.elements(RELOCATION_HOTEL_LIST_NAME_TITLE).orElseThrow(() -> new IllegalStateException("No hotel name found"));

        for (WebElement element : hotelNameElements) {
            String hotelName = element.getText().trim();
            if (hotelName.isEmpty()) {
                hotelName = element.getAttribute("textContent").trim();
            }
            hotelNames.add(hotelName);
        }

        logger.log("Getting ALl Hotel name ==> " + hotelNames);
        return hotelNames;
    }

    public void setRelocationHotelName(String hotelName) {
        driverTools.clearInput(RELOCATION_SEARCH_INPUT);
        driverTools.sendKeys(RELOCATION_SEARCH_INPUT, hotelName);
    }

    public boolean isRelocationSearchButtonClickable() {
        return isInputClickable(RELOCATION_SEARCH_BUTTON);
    }
    public boolean isRelocationSearchInputEditable() {
        return isInputSelectableAndEditable(RELOCATION_SEARCH_INPUT);
    }
    public void clickRelocationSearch() {
        driverTools.scrollAndClick(RELOCATION_SEARCH_BUTTON);
    }

    public String getRelocationEmptySearchResult(){
        WebElement element = driverTools.waitForElementToAppear(RELOCATION_EMPTY_RESULT).orElseThrow(() -> new IllegalStateException("Element not found for " + RELOCATION_EMPTY_RESULT));
        String text = element.getText().trim();
        if (text.isEmpty()) {
            text = element.getAttribute("textContent").trim();
        }
        return text;
    }

    public String getRelocationPageValueByHeading(String heading) {
        Locator VALUE = new Locator("VALUE", By.xpath("//*[contains(@class,'rebooking')][contains(@class,'item')]//*[contains(normalize-space(.),'%s')]//following-sibling::div".formatted(heading)));
        driverTools.waitForElementToAppear(VALUE);
        String text = driverTools.getText(VALUE).orElseThrow(() -> new IllegalStateException("Value not found for heading: %s".formatted(heading))).trim();
        if (text.isEmpty()) {
            text = driverTools.getAttributeTextContent(VALUE).orElseThrow(() -> new IllegalStateException("Text not found for " + heading)).trim();
        }

        return text.trim();
    }

    public boolean isRelocationRoomPageBackArrowButtonClickable(){
        return isInputClickable(RELOCATION_ROOM_BACK_ARROW);
    }
    public void clickRelocationRoomPageBackArrowButton(){
        driverTools.click(RELOCATION_ROOM_BACK_ARROW);
    }

    public boolean isFieldValueCorrect(String fieldName, String expectedValue) {
        String xpathExpression = String.format("//div[contains(@class, 'rebooking-data-item')][div[normalize-space()='%s']]/div[contains(@class, 'col-') and not(normalize-space()='%s')]", fieldName, fieldName);
        Locator valueLocator = new Locator("DYNAMIC_FIELD_VALUE", By.xpath(xpathExpression));
        driverTools.waitForElementToPresence(valueLocator, 10);
        String actualValue = driverTools.getText(valueLocator).orElse("").trim();        
        return actualValue.equals(expectedValue.trim());
    }
    public boolean areFilterChipsPresent() {
        driverTools.waitForElementToPresence(SINGLE_CHIP, 10);
        List<WebElement> chips = driverTools.elements(SINGLE_CHIP).orElse(Collections.emptyList());
        if (chips.isEmpty()) {
            logger.log("No filter chips found");
            return false;
        }
        logger.log("Total filter chips found: " + chips.size());
        return true;
    }
    public boolean verifySeeMoreRoomsButtonIfApplicable() {
        List<WebElement> totalRooms = driverTools.elements(ROOM_CARDS).orElse(Collections.emptyList());
        logger.log("Total room cards found in initial view: " + totalRooms.size());
        if (totalRooms.size() >= 3) {
            if (driverTools.waitForElementToPresence(SEE_MORE_BUTTON, 10).isEmpty()) {
                return false;
            }
            driverTools.scrollIntoWebViewOptional(SEE_MORE_BUTTON);
            return driverTools.isElementVisible(SEE_MORE_BUTTON);
        }
        logger.log("Rooms are less than 3, See More Rooms button is not required.");
        return true;
    }

    public void clickFilterChip(String chipName) {
        Locator chipLocator = new Locator("FILTER_CHIP", new By.ByXPath(String.format("//div[contains(@class, 'room-filter-component__item') and normalize-space()='%s']", chipName)));
        driverTools.waitForElementToBeClickable(chipLocator, 10);
        driverTools.click(chipLocator);
    }

    public List<String> getSelectedFilterChipNames() {
        Locator selectedChips = new Locator("SELECTED_CHIPS", new By.ByXPath("//div[contains(@class, 'room-filter-component__item') and contains(@class, 'selected')]"));
        List<WebElement> elements = driverTools.getDriver().findElements(selectedChips.by());
        List<String> selectedNames = new ArrayList<>();
        for (WebElement el : elements) {
            selectedNames.add(el.getText().trim());
        }
        return selectedNames;
    }

    public List<WebElement> getAllRoomCardElements() {
        Locator roomCards = new Locator("ROOM_CARDS", new By.ByXPath("//div[contains(@class, 'post-room-card')]"));
        driverTools.waitForElementToPresence(roomCards, 10);
        return driverTools.getDriver().findElements(roomCards.by());
    }

    public int getTotalVisibleRoomCardsCount() {
        Locator roomCards = new Locator("ROOM_CARDS", new By.ByXPath("//div[contains(@class, 'post-room-card')]"));
        driverTools.waitForElementToPresence(roomCards, 10);
        return (int) driverTools.getDriver().findElements(roomCards.by()).stream()
                .filter(driverTools::isElementVisible)
                .count();
    }

    public boolean verifyRoomMatchesActiveFilters(WebElement roomCard, List<String> activeFilters) {
        String roomText = roomCard.getText().toLowerCase();
        
        for (String filter : activeFilters) {
            String normalizedFilter = filter.trim().toLowerCase();
            
            if (normalizedFilter.equals("breakfast")) {
                if (roomText.contains("breakfast not included")) {
                    return false;
                }
            } 
            else if (normalizedFilter.equals("lunch")) {
                if (!roomText.contains("lunch") && !roomText.contains("all inclusive meals") && !roomText.contains("3 meals daily")) {
                    return false;
                }
            } 
            else if (normalizedFilter.equals("dinner")) {
                if (!roomText.contains("dinner") && !roomText.contains("all inclusive meals") && !roomText.contains("3 meals daily")) {
                    return false;
                }
            } 
            else if (normalizedFilter.equals("all inclusive meals")) {
                if (!roomText.contains("all inclusive meals")) {
                    return false;
                }
            } 
            else if (normalizedFilter.equals("free wifi")) {
                if (!roomText.contains("free wifi")) {
                    return false;
                }
            } 
            else if (normalizedFilter.equals("free refund & reschedule")) {
                if (!roomText.contains("free refund & reschedule")) {
                    return false;
                }
            } 
        }
        return true;
    }

public boolean isSeeMoreRoomsButtonPresent() {
        if (driverTools.waitForElementToPresence(SEE_MORE_BUTTON, 3).isEmpty()) {
            return false;
        }
        driverTools.scrollIntoWebViewOptional(SEE_MORE_BUTTON);
        return driverTools.isElementVisible(SEE_MORE_BUTTON);
    }

    public String getSeeMoreRoomsButtonText() {
        return driverTools.getText(SEE_MORE_BUTTON).orElse("").trim();
    }

    public void clickSeeMoreRoomsButton() {
        driverTools.scrollIntoWebView(SEE_MORE_BUTTON);
        driverTools.waitForElementToBeClickable(SEE_MORE_BUTTON, 10);
        driverTools.click(SEE_MORE_BUTTON);
    }

    public boolean isHideButtonPresent() {
        driverTools.waitForElementToPresence(HIDE_BUTTON, 10);
        return driverTools.isElementVisible(HIDE_BUTTON);
    }

    public void clickHideButton() {
        driverTools.scrollIntoWebView(HIDE_BUTTON);
        driverTools.waitForElementToBeClickable(HIDE_BUTTON, 10);
        driverTools.click(HIDE_BUTTON);
    }
    public boolean isRelocationHotelByHotelNameSelectable(String hotelName) {
        return isInputClickable(getRelocationHotelListPriceByHotelName(hotelName));
    }

    public boolean isRefundRescheduleDetailsAppear(){
        return driverTools.waitForElementToAppear(REFUND_RESCHEDULE_DETAILS).isPresent();
    }
    public void clickRefundRescheduleDetails() {
        driverTools.scrollAndClick(REFUND_RESCHEDULE_DETAILS_BUTTON);
    }
    public boolean isModalRefundRescheduleDetailsAppear(){
        return driverTools.waitForElementToAppear(MODAL_REFUND_RESCHEDULE_DETAILS).isPresent();
    }
    public void clickModalCloseButton(){
        WebElement closeButton = driverTools.waitForElementToAppear(MODAL_CLOSE_BUTTON).orElseThrow();
        driverTools.clickSvgOrNonHtmlElements(closeButton);
    }
    public boolean isModalDisappeared(){
        return driverTools.waitForElementToDisappear(MODAL_BOX);
    }

    public boolean isDialogBoxDisappeared(){
        return driverTools.waitForElementToDisappear(REBOOK_POPUP);
    }

    public String getRoomPrice() {
        List<WebElement> elements = driverTools.waitForElementsToAppear(ROOM_PRICE).orElseThrow();
        WebElement element = elements.get(0);
        driverTools.scrollElementToCenter(element);
        String price = element.getText().trim();
        if (price.isEmpty()) {
            price = element.getAttribute("textContent").trim();
        }

        return price;
    }

    public List<String> getAllFilterChipsRelocationRoomPage() {
        List<String> filterChips = new ArrayList<>();
        driverTools.waitForElementsToAppear(RELOCATION_PAGE_ROOM_FILTER_CHIPS);

        int retry = 10;
        while (retry-- > 0 && driverTools.getAttributeTextContent(RELOCATION_PAGE_ROOM_FILTER_CHIPS).orElse("").trim().isEmpty()) {
            driverTools.dummyWait(1);
        }

        List<WebElement> elements = driverTools.elements(RELOCATION_PAGE_ROOM_FILTER_CHIPS).orElseThrow(() ->  new IllegalStateException("No filter chips found"));

        for (WebElement element : elements) {
            String text = element.getText().trim();
            if (text.isEmpty()) {
                text = element.getAttribute("textContent").trim();
            }
            filterChips.add(text);
        }

        logger.log("Filter chips found: " + filterChips);
        return filterChips;
    }

    public boolean isFilterChipSelectableRelocationRoomPage(String filterChip) {
        return isInputClickable(getFilterChipByName(filterChip));
    }

    public void selectFilterChipRelocationRoomPage(String filterChip) {
        driverTools.scrollAndClick(getFilterChipByName(filterChip));
    }

    public boolean isFilterChipSelectedRelocationRoomPage(String filterChip) {
        return driverTools.waitForElementsToAppear(getSelectedFilterChipByName(filterChip)).isPresent();
    }

    public void unselectFilterChipRelocationRoomPage(String filterChip) {
        driverTools.scrollAndClick(getSelectedFilterChipByName(filterChip));
    }

    public boolean isFilterChipUnselectedRelocationRoomPage(String filterChip) {
        return driverTools.waitForElementsToAppear(getUnselectedFilterChipByName(filterChip)).isPresent();
    }

    public boolean isRebookingSuccessPageLoaded() {
        return driverTools.waitForElementToAppear(SUCCESS_PAGE).isPresent();
    }
    public String getRebookingSuccessPageMessage() {
        return driverTools.getAttributeTextContent(SUCCESS_PAGE).orElseThrow(() -> new IllegalStateException("Success page message not found"));
    }

    public boolean isMainCardContainerVisiblePreviewPage(String sectionType) {
        String xpath = "//span[text()='%s']/following-sibling::div".formatted(sectionType);
        Locator containerLocator = new Locator(sectionType + " Container", By.xpath(xpath));
        return driverTools.waitForElementToAppear(containerLocator).isPresent();
    }

    public String getHotelNamePreviewPage(String sectionType) {
        String xpath = "//span[text()='%s']/following-sibling::div//h3".formatted(sectionType);
        Locator headerLocator = new Locator(sectionType + " Header", By.xpath(xpath));

        WebElement element = driverTools.waitForElementToAppear(headerLocator)
                .orElseThrow(() -> new RuntimeException("Header not found for section: " + sectionType));

        return driverTools.getText(element).trim();
    }

    public String getDateRangePreviousDetailPreviewPage() {
        Locator dateLocator = new Locator("Previous Details Date Range", By.xpath("//span[text()='Previous Details']/following-sibling::div//div[contains(text(), ' - ')]"));
        WebElement element = driverTools.waitForElementToAppear(dateLocator)
                .orElseThrow(() -> new RuntimeException("Date range not found for Previous Details"));

        String[] parts = driverTools.getText(element).split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Input must contain exactly one start date and one end date separated by '-'");
        }

        LocalDate startDate = LocalDate.parse(parts[0].trim(), FULL_INPUT_FORMATTER);
        LocalDate endDate = LocalDate.parse(parts[1].trim(), FULL_INPUT_FORMATTER);

        String formattedStart = startDate.format(FORMATTER_ISO);
        String formattedEnd = endDate.format(FORMATTER_ISO);
        return "%s - %s".formatted(formattedStart, formattedEnd).replace("\u2011", "-");

    }

    public String getDateRangeNewDetailPreviewPage() {
        String checkInXpath = "//span[text()='New Details']/following-sibling::div//div[div[text()='Check-in']]/div[2]";
        String checkOutXpath = "//span[text()='New Details']/following-sibling::div//div[div[text()='Check-out']]/div[2]";

        WebElement checkInEl = driverTools.waitForElementToAppear(new Locator("New Check-In", By.xpath(checkInXpath)))
                .orElseThrow(() -> new RuntimeException("Check-in date block not found for New Details"));
        WebElement checkOutEl = driverTools.waitForElementToAppear(new Locator("New Check-Out", By.xpath(checkOutXpath)))
                .orElseThrow(() -> new RuntimeException("Check-out date block not found for New Details"));

        LocalDate startDate = LocalDate.parse(driverTools.getText(checkInEl).trim(), FULL_INPUT_FORMATTER);
        LocalDate endDate = LocalDate.parse(driverTools.getText(checkOutEl).trim(), FULL_INPUT_FORMATTER);

        String formattedStart = startDate.format(FORMATTER_ISO);
        String formattedEnd = endDate.format(FORMATTER_ISO);
        return "%s - %s".formatted(formattedStart, formattedEnd).replace("\u2011", "-");
    }
}