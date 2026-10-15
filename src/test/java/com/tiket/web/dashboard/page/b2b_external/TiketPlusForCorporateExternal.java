package com.tiket.web.dashboard.page.b2b_external;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.*;

public class TiketPlusForCorporateExternal <T extends WebDriver> extends BasePage<T> {

    public TiketPlusForCorporateExternal(Session<T> session) {
        super(session);
    }

    private static final Locator QUOTATION_NUMBER_INPUT = new Locator("QUOTATION_NUMBER_INPUT", By.xpath("//input[@type='text' and @placeholder='Quotation number']"));
    private static final Locator TABLE_ROW_MORE_ACTIONS_BUTTON = new Locator("TABLE_ROW_MORE_ACTIONS_BUTTON", By.xpath("(//table//*[name()='svg' and contains(@class,'cursor-pointer')])[1]"));
    private static final Locator QUOTATION_DETAILS_QUOTATION_NUMBER = new Locator("QUOTATION_DETAILS_QUOTATION_NUMBER", By.xpath("(//div[contains(@class,'Card')]//span)[1]"));
    private static final Locator QUOTATION_LIST_HEADER = new Locator("QUOTATION_LIST_HEADER", new By.ByXPath("(//span[text()='Quotation List'])[last()]"));
    private static final Locator QUOTATION_DETAILS_COMPANY_NAME = new Locator("QUOTATION_DETAILS_COMPANY_NAME", new By.ByXPath("//span[contains(normalize-space(.),'Company Name')]//following-sibling::span"));
    private static final Locator QUOTATION_DETAILS_EMAIL = new Locator("QUOTATION_DETAILS_EMAIL", new By.ByXPath("//span[contains(normalize-space(.),'Email Address')]//following-sibling::span"));
    private static final Locator QUOTATION_DETAILS_TOTAL_AMOUNT = new Locator("QUOTATION_DETAILS_TOTAL_AMOUNT", By.xpath("//span[normalize-space(.)='Total Amount']/ancestor::td/following-sibling::td/span"));
    private static final Locator QUOTATION_DETAILS_ACTIVITY_LOG_HEADING = new Locator("QUOTATION_DETAILS_ACTIVITY_LOG_HEADING", By.xpath("//span[contains(normalize-space(.),'Activity Log')]"));
    private static final Locator ACCEPT_QUOTATION_BUTTON = new Locator("ACCEPT_QUOTATION_BUTTON", By.xpath("(//button[contains(normalize-space(.),'Accept')])[last()]"));
    private static final Locator REJECT_QUOTATION_BUTTON = new Locator("REJECT_QUOTATION_BUTTON", By.xpath("//button[contains(normalize-space(.),'Reject')]"));
    private static final Locator MODAL_HEADING = new Locator("MODAL_HEADING", By.xpath("//div[contains(@class,'Modal')]//header/*[self::h2 or self::span]"));
    private static final Locator MODAL_DESCRIPTION = new Locator("MODAL_DESCRIPTION", By.xpath("(//div[contains(@class,'Modal')]//div[contains(@class,'ModalBody')]//span)[1]"));
    private static final Locator MODAL_CLOSE_BUTTON = new Locator("MODAL_CLOSE_BUTTON", By.xpath("//button[contains(normalize-space(.),'Close')]"));
    private static final Locator MODAL_ORDER_ID = new Locator("MODAL_ORDER_ID", By.xpath("//div[contains(@class,'Modal')]//div[contains(@class,'ModalBody')]//span[contains(normalize-space(.),'Order ID')]//following-sibling::span"));
    private static final Locator REASON_INPUT = new Locator("REASON_INPUT", By.xpath("//input[@id='reason']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[normalize-space(.)='Save']"));
    private static final Locator QUOTATION_ACTIVE_STATUS = new Locator("QUOTATION_ACTIVE_STATUS", By.xpath("//div[contains(@class,'Card')]//span[normalize-space(.)='Active']"));
    private static final Locator QUOTATION_ACCEPTED_STATUS = new Locator("QUOTATION_ACCEPTED_STATUS", By.xpath("//div[contains(@class,'Card')]//span[normalize-space(.)='Accepted']"));
    private static final Locator QUOTATION_REJECTED_STATUS = new Locator("QUOTATION_REJECTED_STATUS", By.xpath("//div[contains(@class,'Card')]//span[normalize-space(.)='Rejected']"));
    private static final Locator NEW_REQUEST_BUTTON = new Locator("NEW_REQUEST_BUTTON", new By.ByXPath("//button//span[normalize-space(text())='New Request']"));
    private static final Locator NEW_EVENT_REQUEST_TITLE = new Locator("NEW_EVENT_REQUEST_TITLE", new By.ByXPath("//span[normalize-space(text())='New Event Request']"));
    private static final Locator TAKEOVER_PAYMENT_TITLE = new Locator("TAKEOVER_PAYMENT_TITLE", new By.ByXPath("//span[normalize-space(text())='Takeover Payment']"));
    private static final Locator CREATE_EVENT_REQUEST_BUTTON = new Locator("CREATE_EVENT_REQUEST_BUTTON", new By.ByXPath("//button//span[normalize-space(text())='Create event request']"));
    private static final Locator NEW_BOOKING_REQUEST_HEADER = new Locator("NEW_BOOKING_REQUEST_HEADER", new By.ByXPath("//span[normalize-space(text())='New Booking Request']"));
    private static final Locator EVENT_NAME_INPUT = new Locator("EVENT_NAME_INPUT", By.id("event-name"));
    private static final Locator PARTICIPANTS_INPUT = new Locator("PARTICIPANTS_INPUT", By.id("number-of-participants"));
    private static final Locator AREA_CITY_INPUT = new Locator("AREA_CITY_INPUT", By.id("area--city"));
    private static final Locator DESCRIPTION_TEXTAREA = new Locator("DESCRIPTION_TEXTAREA", By.id("description"));
    private static final Locator START_DATE_INPUT = new Locator("START_DATE_INPUT", By.id("event-start-date"));
    private static final Locator END_DATE_INPUT = new Locator("END_DATE_INPUT", By.id("event-end-date"));
    private static final Locator NEXT_EVENT_PRODUCTS_BTN = new Locator("NEXT_EVENT_PRODUCTS_BTN", new By.ByXPath("//button//span[normalize-space(text())='Next: Event Products']"));
    private static final Locator EVENT_PRODUCTS_HEADER = new Locator("EVENT_PRODUCTS_HEADER", new By.ByXPath("//span[normalize-space(text())='Event Products' and contains(@class, 'Text_size_h3')]"));
    private static final Locator ADDITIONAL_REQ_TEXTAREA = new Locator("ADDITIONAL_REQ_TEXTAREA", new By.ByXPath("//span[normalize-space(text())='Additional Requirements']/following-sibling::div//textarea"));
    private static final Locator NEXT_CONTACT_APPROVAL_BTN = new Locator("NEXT_CONTACT_APPROVAL_BTN", new By.ByXPath("//button//span[normalize-space(text())='Next: Contact & Approval']"));
    private static final Locator CONTACT_PARTICIPANT_HEADER = new Locator("CONTACT_PARTICIPANT_HEADER", new By.ByXPath("//span[normalize-space(text())='Contact & Participant Details' and contains(@class, 'Text_size_h3')]"));
    private static final Locator PARTICIPANT_NAME_INPUT = new Locator("PARTICIPANT_NAME_INPUT", By.id("participant-name"));
    private static final Locator ID_NUMBER_INPUT = new Locator("ID_NUMBER_INPUT", new By.ByXPath("//input[contains(@id, 'nikid-number-')]"));
    private static final Locator DOB_INPUT = new Locator("DOB_INPUT", By.id("date-of-birth"));
    private static final Locator TRAVEL_APPROVAL_INPUT = new Locator("TRAVEL_APPROVAL_INPUT", By.id("travel-approval-number"));
    private static final Locator ADD_PARTICIPANT_BTN = new Locator("ADD_PARTICIPANT_BTN", new By.ByXPath("//button//span[normalize-space(text())='Add Participant' and not(@disabled)]"));
    private static final Locator DESKTOP_CALENDAR_PREV_MONTH_BTN = new Locator("DESKTOP_CALENDAR_PREV_MONTH_BTN", new By.ByXPath("//div[contains(@class, 'DatePickerDesktop_custom_calendar')]//button[contains(@class, 'Navigation_prev_button')]"));
    private static final Locator DESKTOP_CALENDAR_SAVE_BTN = new Locator("DESKTOP_CALENDAR_SAVE_BTN", new By.ByXPath("//footer[contains(@class, 'DatePickerDesktop_action_area')]//button//span[text()='Save']"));
    private static final Locator NEXT_REVIEW_SUBMIT_BTN = new Locator("NEXT_REVIEW_SUBMIT_BTN", new By.ByXPath("//button//span[contains(normalize-space(.), 'Next: Review & Submit')]"));
    private static final Locator REVIEW_SUBMIT_ACTIVE_STEP = new Locator("REVIEW_SUBMIT_ACTIVE_STEP", new By.ByXPath("//div[contains(@class, 'bg-[#E7F2FF]')]//span[normalize-space(text())='Review & Submit']"));
    private static final Locator FINAL_SUBMIT_BTN = new Locator("FINAL_SUBMIT_BTN", new By.ByXPath("//button//span[normalize-space(text())='Submit request']"));
    private static final Locator SUBMIT_CONFIRMATION_MODAL_TITLE = new Locator("SUBMIT_CONFIRMATION_MODAL_TITLE", new By.ByXPath("//h2[contains(normalize-space(.), 'Submit this booking request?')]"));
    private static final Locator MODAL_SUBMIT_REQUEST_BTN = new Locator("SUBMIT_REQUEST_BTN", new By.ByXPath("//footer[contains(@class, 'Modal_modal_footer')]//button//span[normalize-space(text())='Submit request']"));
    private static final Locator SEARCH_BY_KEYWORD_INPUT = new Locator("SEARCH_BY_KEYWORD_INPUT", new By.ByXPath("//input[@placeholder='Search by keyword']"));
    private static final Locator SNACKBAR_CONTAINER = new Locator("SNACKBAR_CONTAINER", By.xpath("//div[contains(@class, 'SnackbarsPositioner')]"));
    private static final Locator SNACKBAR_TEXT_CONTENT = new Locator("SNACKBAR_TEXT_CONTENT", By.xpath("//div[contains(@class, 'Snackbar_text_content')]"));
    private static final Locator CANCEL_REQUEST_MENU_ITEM = new Locator("CANCEL_REQUEST_MENU_ITEM", By.xpath("//li//span[text()='Cancel request']"));
    private static final Locator CANCEL_REQUEST_CONFIRM_BTN = new Locator("CANCEL_REQUEST_CONFIRM_BTN", By.xpath("//section//button//span[text()='Cancel request']"));
    private static final Locator TAKE_OVER_START_DATE_INPUT = new Locator("TAKE_OVER_START_DATE_INPUT", By.id("order-start-date"));
    private static final Locator TAKE_OVER_END_DATE_INPUT = new Locator("TAKE_OVER_END_DATE_INPUT", By.id("order-end-date"));
    private static final Locator NEXT_CONTACT_TRAVEL_BTN = new Locator("NEXT_CONTACT_TRAVEL_BTN", new By.ByXPath("//button//span[normalize-space(text())='Next: Contact & Travel']"));
    private static final Locator CONTACT_AND_BOOKING_PAGE_HEADER = new Locator("CONTACT_AND_BOOKING_PAGE_HEADER", new By.ByXPath("//span[normalize-space(text())='Contact & Booking Details' and contains(@class, 'Text_size_h3')]"));
    private static final Locator CREATE_PAYMENT_REQUEST_BTN = new Locator("CREATE_PAYMENT_REQUEST_BTN", new By.ByXPath("//button//span[normalize-space(text())='Create payment request']"));
    private static final Locator TAKEOVER_PAYMENT_PAGE_TITLE = new Locator("TAKEOVER_PAYMENT_PAGE_TITLE", new By.ByXPath("//span[normalize-space(text())='Takeover Payment' and contains(@class, 'Text_weight_bold')]"));
    private static final Locator TAKEOVER_ORDER_AMOUNT = new Locator("TAKEOVER_ORDER_AMOUNT", By.id("order-amount"));
    private static final Locator TAKEOVER_VENDOR_NAME = new Locator("TAKEOVER_VENDOR_NAME", By.id("vendor-name"));
    private static final Locator BOOKING_CODE_INPUT = new Locator("BOOKING_CODE_INPUT", By.id("booking-code"));
    private static final Locator TAKE_OVER_SUBMIT_CONFIRMATION_MODAL_TITLE = new Locator("TAKE_OVER_SUBMIT_CONFIRMATION_MODAL_TITLE", new By.ByXPath("//h2[contains(normalize-space(.), 'Submit this takeover payment request?')]"));
    private static final Locator QUOTATION_DETAILS_DISCUSSION_HEADING = new Locator("QUOTATION_DETAILS_DISCUSSION_HEADING", new By.ByXPath("//span[contains(normalize-space(.),'Discussion')]"));
    private static final Locator QUOTATION_STATUS = new Locator("QUOTATION_STATUS", By.xpath("(//div[contains(@class,'Card')]//span)[1]/following-sibling::span"));
    private static final Locator QUOTE_DATE_VALUE = new Locator("QUOTE_DATE_VALUE", By.xpath("//span[text()='Quote Date']/parent::div/following-sibling::div/span"));

    // columnIndex: 1=Event Name, 2=Request Type, 3=Requestor, 4=Start Date, 5=End Date, 6=Created Date, 7=Location, 8=Participants, 9=Products
    private static Locator getDashboardTableCellLocator(String eventName, int columnIndex) {
        String eventNameLiteral = com.tiket.core.CoreFunctions.toXPathLiteral(eventName);
        String xpath = String.format("//table/tbody/tr[td[1]//span[normalize-space(text())=%s]]/td[%d]", eventNameLiteral, columnIndex);
        return new Locator("DASHBOARD_TABLE_CELL_" + eventName.replaceAll("\\s+", "_") + "_" + columnIndex, new By.ByXPath(xpath));
    }

    private static Locator getDesktopCalendarDayLocator(int day) {
        return new Locator("DESKTOP_CALENDAR_DAY_" + day, new By.ByXPath("//div[contains(@class, 'DatePickerDesktop_custom_calendar')]//button[contains(@class, 'Day_day') and not(@disabled)]//span[text()='" + day + "']"));
    }

    private static Locator getProductTypeChip(String productName) {
        String productLiteral = com.tiket.core.CoreFunctions.toXPathLiteral(productName);
        return new Locator("PRODUCT_CHIP_" + productName.toUpperCase().replaceAll("\\s+", "_"), new By.ByXPath("//button[contains(@class, 'Chip_chip')]//span[text()=" + productLiteral + "]"));
    }
    private static Locator getEventTypeChip(String eventType) {
        String eventTypeLiteral = com.tiket.core.CoreFunctions.toXPathLiteral(eventType);
        return new Locator("EVENT_TYPE_CHIP_" + eventType.toUpperCase().replaceAll("\\s+", "_"), By.xpath("//button[contains(@class, 'Chip_chip')]//span[text()=" + eventTypeLiteral + "]"));
    }
    public boolean isQuotationListPageOpened() {
        driverTools.waitForElementToAppear(QUOTATION_LIST_HEADER);
        return driverTools.isElementPresent(QUOTATION_LIST_HEADER);
    }
    public boolean isQuotationDetailsPageLoaded(String quotationNumber){
        driverTools.dummyWait(2);
        Locator QUOTATION_DETAIL_PAGE = new Locator("QUOTATION_DETAIL_PAGE",By.xpath("//div[contains(@class,'Card')]//span[normalize-space(.)='%s']".formatted(quotationNumber)));
        driverTools.waitForElementToAppear(QUOTATION_DETAIL_PAGE);
        return driverTools.isElementPresent(QUOTATION_DETAIL_PAGE);
    }

    public void searchQuotationNumber(String quotationNumber) {
        driverTools.waitForElementToPresence(QUOTATION_NUMBER_INPUT);
        driverTools.clearInput(QUOTATION_NUMBER_INPUT);
        driverTools.sendKeys(QUOTATION_NUMBER_INPUT, quotationNumber);
        Locator QUOTATION_OPTION = new Locator("QUOTATION_OPTION", By.xpath("//div[contains(@class,'list_menu')]//span[contains(normalize-space(.),'%s')]".formatted(quotationNumber)));
        driverTools.waitForElementToAppear(QUOTATION_OPTION);
        driverTools.click(QUOTATION_OPTION);
        driverTools.dummyWait(2);
    }

    public String getTableDetailByHeading(String headingName, boolean isNumeric) {
        Locator headersLocator = new Locator("Table Headers", By.xpath("//table/thead/tr/th"));
        driverTools.waitForElementToPresence(headersLocator);
        List<WebElement> headers = driver.findElements(headersLocator.by());

        int columnIndex = -1;
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().trim().equalsIgnoreCase(headingName.trim())) {
                columnIndex = i + 1;
                break;
            }
        }

        if (columnIndex == -1) {
            throw new RuntimeException("Specified table heading column '" + headingName + "' was not found.");
        }

        String cellXpath = "//table/tbody/tr[1]/td[%d]".formatted(columnIndex);
        Locator targetCellLocator = new Locator("First Row Cell Index " + columnIndex, By.xpath(cellXpath));

        driverTools.waitForElementToPresence(targetCellLocator);
        WebElement cellElement = driver.findElements(targetCellLocator.by()).get(0);
        String rawText = cellElement.getText().trim();

        if (isNumeric) {
            return rawText.replaceAll("[^0-9.]", "").trim();
        }

        return rawText;
    }

    public void clickTableDetailMoreActionsButton(){
        driverTools.dummyWait(2);
        driverTools.waitForElementToAppear(TABLE_ROW_MORE_ACTIONS_BUTTON, 10);
        WebElement ele = driver.findElement(TABLE_ROW_MORE_ACTIONS_BUTTON.by());
        driverTools.clickSvgOrNonHtmlElements(ele);
    }
    public void clickQuotationActionButton(String action){
        Locator QUOTATION_ACTION_BUTTON = new Locator("QUOTATION_ACTION_BUTTON", By.xpath("//li//span[normalize-space(.)='%s']".formatted(action)));
        driverTools.click(QUOTATION_ACTION_BUTTON);
    }

    public Boolean isQuotationPageLoaded() {
        return  driverTools.waitForElementToAppear(QUOTATION_DETAILS_QUOTATION_NUMBER).isPresent();
    }
    public String getQuotationPageQuotationNumber() {
        return driverTools.getText(QUOTATION_DETAILS_QUOTATION_NUMBER).orElseThrow(() -> new IllegalStateException("Quotation number not found"));
    }
    public String getQuotationPageQuotationActiveStatus() {
        driverTools.waitForElementsToAppear(QUOTATION_ACTIVE_STATUS);
        return driverTools.getText(QUOTATION_ACTIVE_STATUS).orElseThrow(() -> new IllegalStateException("Quotation Active status not found"));
    }
    public String getQuotationPageQuotationAcceptedStatus() {
        driverTools.waitForElementsToAppear(QUOTATION_ACCEPTED_STATUS);
        return driverTools.getText(QUOTATION_ACCEPTED_STATUS).orElseThrow(() -> new IllegalStateException("Quotation Accepted status not found"));
    }
    public String getQuotationPageQuotationRejectedStatus() {
        driverTools.waitForElementsToAppear(QUOTATION_REJECTED_STATUS);
        return driverTools.getText(QUOTATION_REJECTED_STATUS).orElseThrow(() -> new IllegalStateException("Quotation Rejected status not found"));
    }
    public String getQuotationPageCompanyName() {
        return driverTools.getText(QUOTATION_DETAILS_COMPANY_NAME).orElseThrow(() -> new IllegalStateException("Quotation number not found"));
    }
    public String getQuotationPageEmailAddress() {
        return driverTools.getText(QUOTATION_DETAILS_EMAIL).orElseThrow(() -> new IllegalStateException("Email Address not found"));
    }
    public String getQuotationDetailTotalAmount() {
        return driverTools.getText(QUOTATION_DETAILS_TOTAL_AMOUNT).orElseThrow(() -> new IllegalStateException("Total Amount not found")).replaceAll("[^0-9]", "").trim();
    }
    public String getQuotationDetailActivityLogText() {
        return driverTools.getText(QUOTATION_DETAILS_ACTIVITY_LOG_HEADING).orElseThrow(() -> new IllegalStateException("Activity Log Text not found"));
    }
    public String getQuotationDetailDiscussionText() {
        return driverTools.getText(QUOTATION_DETAILS_DISCUSSION_HEADING).orElseThrow(() -> new IllegalStateException("Discussion Text not found"));
    }
    
    public String getQuotationPageQuotationStatus() {
        return driverTools.getText(QUOTATION_STATUS).orElseThrow(() -> new IllegalStateException("Quotation status not found"));
    }

    public String getQuotationDetailQuoteDate() {
        return driverTools.getText(QUOTE_DATE_VALUE).orElseThrow(() -> new IllegalStateException("Quote Date value not found"));
    }

    public void clickAcceptQuotation(){
        driverTools.click(ACCEPT_QUOTATION_BUTTON);
    }
    public void clickRejectQuotation(){
        driverTools.click(REJECT_QUOTATION_BUTTON);
    }
    public boolean isModalLoaded() {
        driverTools.dummyWait(2);
        return driverTools.waitForElementToAppear(MODAL_HEADING).isPresent();
    }
    public String getModalHeading(){
        return driverTools.getText(MODAL_HEADING).orElseThrow(() -> new IllegalStateException("Modal Heading not found"));
    }
    public String getModalDescription() {
        return driverTools.getText(MODAL_DESCRIPTION).orElseThrow(() -> new IllegalStateException("Modal Description not found"));
    }
    public void selectPaymentMethod(String paymentMethod) {
        Locator PAYMENT_METHOD = new Locator("PAYMENT_METHOD", By.xpath("//input[@type='radio' and @value='\"%s\"']".formatted(paymentMethod)));
        driverTools.clickWithJavascriptExecutor(PAYMENT_METHOD);
    }
    public Boolean isPaymentMethod(String paymentMethod) {
        Locator PAYMENT_METHOD = new Locator("PAYMENT_METHOD", By.xpath("//input[@type='radio' and @value='\"%s\"']".formatted(paymentMethod)));
        return driverTools.isElementSelected(PAYMENT_METHOD);
    }
    public void clickModalCloseButton() {
        driverTools.click(MODAL_CLOSE_BUTTON);
    }
    public String getModalOrderId() {
        return driverTools.getText(MODAL_ORDER_ID).orElseThrow(() -> new IllegalStateException("Modal Order Id not found"));
    }

    public void fillRandomReasonRejectQuotation(String reason) {
        driverTools.clear(REASON_INPUT);
        driverTools.sendKeys(REASON_INPUT,  reason);
    }

    public void clickSaveButtonRejectQuotation() {
        driverTools.click(SAVE_BUTTON);
    }

    public void clickNewRequestButton() {
        WebElement newRequestBtn = driverTools.waitForElementToBeClickable(NEW_REQUEST_BUTTON).orElseThrow();
        driverTools.scrollElementToCenter(newRequestBtn);
        driverTools.click(newRequestBtn);
    }

    public void verifyNewRequestPageLoaded() {
        WebElement eventRequest = driverTools.waitForElementToAppear(NEW_EVENT_REQUEST_TITLE).orElseThrow();
        WebElement takeoverPayment = driverTools.waitForElementToAppear(TAKEOVER_PAYMENT_TITLE).orElseThrow();

        Assertion.assertThat("Verify 'New Event Request' is displayed", eventRequest.isDisplayed(), is(true));
        Assertion.assertThat("Verify 'Takeover Payment' is displayed", takeoverPayment.isDisplayed(), is(true));
    }

    public void clickCreateEventRequestButton() {
        WebElement createEventBtn = driverTools.waitForElementToBeClickable(CREATE_EVENT_REQUEST_BUTTON).orElseThrow();
        driverTools.scrollElementToCenter(createEventBtn);
        driverTools.click(createEventBtn);
    }

    public void verifyNewBookingRequestPageOpened() {
        WebElement header = driverTools.waitForElementToAppear(NEW_BOOKING_REQUEST_HEADER).orElseThrow();
        Assertion.assertThat("Verify the New Booking Request page is successfully loaded", header.isDisplayed(), is(true));
    }

    public void selectEventType(String eventType) {
        WebElement chip = driverTools.waitForElementToBeClickable(getEventTypeChip(eventType)).orElseThrow();
        driverTools.scrollElementToCenter(chip);
        driverTools.click(chip);
    }

    public void fillEventDetails(String name, String participants, String area, String description) {
        WebElement nameInput = driverTools.waitForElementToBeClickable(EVENT_NAME_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(nameInput);
        driverTools.clearInput(nameInput);
        driverTools.sendKeys(nameInput, name);

        WebElement participantsInput = driverTools.waitForElementToBeClickable(PARTICIPANTS_INPUT).orElseThrow();
        driverTools.clearInput(participantsInput);
        driverTools.sendKeys(participantsInput, participants);

        WebElement areaInput = driverTools.waitForElementToBeClickable(AREA_CITY_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(areaInput);
        driverTools.clearInput(areaInput);
        driverTools.sendKeys(areaInput, area);

        WebElement descInput = driverTools.waitForElementToBeClickable(DESCRIPTION_TEXTAREA).orElseThrow();
        driverTools.scrollElementToCenter(descInput);
        driverTools.clearInput(descInput);
        driverTools.sendKeys(descInput, description);
    }

    public String getStartDate(){
        return driverTools.waitForElementToAppear(START_DATE_INPUT).orElseThrow().getAttribute("value");
    }

    public String getEndDate(){
        return driverTools.waitForElementToAppear(END_DATE_INPUT).orElseThrow().getAttribute("value");
    }

    public String getTakeOverStartDate(){
        return driverTools.waitForElementToAppear(TAKE_OVER_START_DATE_INPUT).orElseThrow().getAttribute("value");
    }

    public String getTakeOverEndDate(){
        return driverTools.waitForElementToAppear(TAKE_OVER_END_DATE_INPUT).orElseThrow().getAttribute("value");
    }


    public void selectDateInNextMonth(String inputId, int dayToSelect) {
        Locator dateInputLocator = new Locator("INPUT_" + inputId, By.id(inputId));
        Locator NextMonthBtn = new Locator("NEXT_MONTH_" + inputId, By.xpath("//input[@id='" + inputId + "']/ancestor::div[contains(@class, 'relative')]//*[local-name()='svg' and contains(@class, 'right-5')]"));
        Locator DayBtn = new Locator("DAY_" + dayToSelect + "_" + inputId, By.xpath("//input[@id='" + inputId + "']/ancestor::div[contains(@class, 'relative')]//button[not(@disabled)]//span[text()='" + dayToSelect + "']"));

        WebElement dateInput = driverTools.waitForElementToBeClickable(dateInputLocator).orElseThrow();
        driverTools.scrollElementToCenter(dateInput);
        driverTools.click(dateInput);

        WebElement nextMonthBtn = driverTools.waitForElementToBeClickable(NextMonthBtn).orElseThrow();
        driverTools.click(nextMonthBtn);

        WebElement dayBtn = driverTools.waitForElementToBeClickable(DayBtn).orElseThrow();
        driverTools.click(dayBtn);
    }

    public void clickNextEventProducts() {
        WebElement nextBtn = driverTools.waitForElementToBeClickable(NEXT_EVENT_PRODUCTS_BTN).orElseThrow();
        driverTools.scrollElementToCenter(nextBtn);
        driverTools.click(nextBtn);
    }

    public void verifyEventProductsSectionOpens() {
        WebElement header = driverTools.waitForElementToAppear(EVENT_PRODUCTS_HEADER).orElseThrow();
        Assertion.assertThat("Verify the 'Event Products' section is successfully opened", header.isDisplayed(), is(true));
    }

    public void selectProductTypes(String productTypesStr) {
        String[] products = productTypesStr.split("-");

        for (String product : products) {
            WebElement chip = driverTools.waitForElementToBeClickable(getProductTypeChip(product.trim())).orElseThrow();
            driverTools.scrollElementToCenter(chip);
            driverTools.click(chip);
        }
    }

    public void fillAdditionalRequirements(String requirements) {
        WebElement reqTextarea = driverTools.waitForElementToBeClickable(ADDITIONAL_REQ_TEXTAREA).orElseThrow();
        driverTools.scrollElementToCenter(reqTextarea);
        driverTools.clearInput(reqTextarea);
        driverTools.sendKeys(reqTextarea, requirements);
    }

    public void clickNextContactAndApproval() {
        WebElement nextBtn = driverTools.waitForElementToBeClickable(NEXT_CONTACT_APPROVAL_BTN).orElseThrow();
        driverTools.scrollElementToCenter(nextBtn);
        driverTools.click(nextBtn);
    }

    public void verifyContactAndParticipantSectionOpens() {
        WebElement header = driverTools.waitForElementToAppear(CONTACT_PARTICIPANT_HEADER).orElseThrow();
        Assertion.assertThat("Verify the 'Contact & Participant Details' section is successfully opened", header.isDisplayed(), is(true));
    }

    public void fillSingleParticipant(String name, String idNumber, int dobDay) {
        WebElement nameInput = driverTools.waitForElementToBeClickable(PARTICIPANT_NAME_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(nameInput);
        driverTools.clearInput(nameInput);
        driverTools.sendKeys(nameInput, name);
        WebElement idInput = driverTools.waitForElementToBeClickable(ID_NUMBER_INPUT).orElseThrow();
        driverTools.clearInput(idInput);
        driverTools.sendKeys(idInput, idNumber);
        WebElement dobInput = driverTools.waitForElementToBeClickable(DOB_INPUT).orElseThrow();
        driverTools.click(dobInput);
        WebElement prevMonthBtn = driverTools.waitForElementToBeClickable(DESKTOP_CALENDAR_PREV_MONTH_BTN).orElseThrow();
        driverTools.click(prevMonthBtn);
        WebElement dayBtn = driverTools.waitForElementToBeClickable(getDesktopCalendarDayLocator(dobDay)).orElseThrow();
        driverTools.click(dayBtn);
        WebElement saveBtn = driverTools.waitForElementToBeClickable(DESKTOP_CALENDAR_SAVE_BTN).orElseThrow();
        driverTools.click(saveBtn);
    }

    public void fillTravelApprovalNumber(String approvalNum) {
        WebElement approvalInput = driverTools.waitForElementToBeClickable(TRAVEL_APPROVAL_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(approvalInput);
        driverTools.clearInput(approvalInput);
        driverTools.sendKeys(approvalInput, approvalNum);
    }

    public void clickAddParticipant() {
        WebElement addBtn = driverTools.waitForElementToBeClickable(ADD_PARTICIPANT_BTN).orElseThrow();
        driverTools.scrollElementToCenter(addBtn);
        driverTools.click(addBtn);
    }

    public String getDOB(){
        return driverTools.waitForElementToAppear(DOB_INPUT).orElseThrow().getAttribute("value");
    }

    public void clickNextReviewAndSubmit() {
        WebElement nextBtn = driverTools.waitForElementToBeClickable(NEXT_REVIEW_SUBMIT_BTN).orElseThrow();
        driverTools.scrollElementToCenter(nextBtn);
        driverTools.click(nextBtn);
    }

    public void verifyReviewAndSubmitPageOpens() {
        WebElement activeStep = driverTools.waitForElementToAppear(REVIEW_SUBMIT_ACTIVE_STEP).orElseThrow();
        Assertion.assertThat("Verify navigated to Review & Submit page", activeStep.isDisplayed(), is(true));
    }

    private String getActualValueForLabel(String label) {
        String xpath = String.format("//span[contains(normalize-space(.), '%s')]/following-sibling::span", label);
        Locator loc = new Locator("LABEL_VALUE_" + label.replaceAll("\\s+", "_"), By.xpath(xpath));
        return driverTools.waitForElementToAppear(loc).orElseThrow().getText().trim();
    }

    private String getActualProductChip(String productName) {
        String xpath = String.format("//button[contains(@class, 'Chip_chip')]//span[normalize-space(text())='%s']", productName);
        Locator loc = new Locator("PRODUCT_CHIP_" + productName.replaceAll("\\s+", "_"), By.xpath(xpath));
        return driverTools.waitForElementToAppear(loc).orElseThrow().getText().trim();
    }

    private String formatDobForUi(String inputDate) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
            LocalDate date = LocalDate.parse(inputDate, inputFormatter);
            return date.format(outputFormatter);
        } catch (Exception e) {
            return inputDate;
        }
    }

    private String normalizeMonth(String date) {
        // Web/CLDR renders September as "Sept"; JVM formats it as "Sep". Unify them before comparing.
        return date == null ? null : date.replaceAll("(?i)\\bSept\\b", "Sep");
    }

    public void verifySummaryDetails(String eventName, String eventType, String participantsNum, String area, String eventDesc,
                                     String productTypesStr, String addReq,
                                     String firstPartName, String firstPartId, String firstPartDob,
                                     String secondPartName, String secondPartId, String secondPartDob,
                                     String approvalNum) {

        Locator eventNameLoc = new Locator("SUMMARY_EVENT_NAME", By.xpath("//div[contains(@class, 'mt-8')]/span[contains(@class, 'Text_weight_bold')]"));
        String actualEventName = driverTools.waitForElementToAppear(eventNameLoc).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Event Name. Expected: " + eventName + " Actual: " + actualEventName, actualEventName, is(equalTo(eventName)));

        String actualEventType = getActualValueForLabel("Event Type");
        Assertion.assertThat("Verify Event Type. Expected: " + eventType + " Actual: " + actualEventType, actualEventType, is(equalTo(eventType)));

        String actualParticipants = getActualValueForLabel("Number of Participants");
        Assertion.assertThat("Verify Participants. Expected to contain: " + participantsNum + " Actual: " + actualParticipants, actualParticipants, containsString(participantsNum));

        String actualArea = getActualValueForLabel("Area & City");
        Assertion.assertThat("Verify Area & City. Expected: " + area + " Actual: " + actualArea, actualArea, is(equalTo(area)));

        Locator eventDescLoc = new Locator("SUMMARY_EVENT_DESC", By.xpath("//div[contains(@class, 'mt-8')]/following-sibling::span[contains(@class, 'text-ellipsis')]"));
        String actualEventDesc = driverTools.waitForElementToAppear(eventDescLoc).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Event Description. Expected: " + eventDesc + " Actual: " + actualEventDesc, actualEventDesc, is(equalTo(eventDesc)));

        String[] products = productTypesStr.split("-");
        for (String product : products) {
            String trimmedProduct = product.trim();
            String actualProduct = getActualProductChip(trimmedProduct);
            Assertion.assertThat("Verify Product Chip. Expected: " + trimmedProduct + " Actual: " + actualProduct, actualProduct, is(equalTo(trimmedProduct)));
        }

        String actualAddReq = getActualValueForLabel("Additional Requirements:");
        Assertion.assertThat("Verify Additional Requirements. Expected: " + addReq + " Actual: " + actualAddReq, actualAddReq, is(equalTo(addReq)));

        verifyParticipantInTable(firstPartName, firstPartId, firstPartDob);
        verifyParticipantInTable(secondPartName, secondPartId, secondPartDob);

        String actualApprovalNum = getActualValueForLabel("Travel Approval Number");
        Assertion.assertThat("Verify Travel Approval Number. Expected: " + approvalNum + " Actual: " + actualApprovalNum, actualApprovalNum, is(equalTo(approvalNum)));
    }

    public void clickFinalSubmitRequestButton() {
        WebElement submitBtn = driverTools.waitForElementToBeClickable(FINAL_SUBMIT_BTN).orElseThrow();
        driverTools.scrollElementToCenter(submitBtn);
        driverTools.click(submitBtn);
    }

    private String getParticipantTableData(String participantName, int columnIndexOffset) {
        String participantLiteral = com.tiket.core.CoreFunctions.toXPathLiteral(participantName);
        String xpath = String.format("//table//tbody//tr/td[normalize-space(text())=%s]/following-sibling::td[%d]", participantLiteral, columnIndexOffset);
        Locator cellLocator = new Locator("TABLE_CELL_" + participantName.replaceAll("\\s+", "_") + "_" + columnIndexOffset, By.xpath(xpath));
        return driverTools.waitForElementToAppear(cellLocator).orElseThrow().getText().trim();
    }

    public void verifyParticipantInTable(String expectedName, String expectedId, String expectedDob) {
        String nameLiteral = com.tiket.core.CoreFunctions.toXPathLiteral(expectedName);
        Locator nameCell = new Locator("TABLE_NAME_" + expectedName.replaceAll("\\s+", "_"), By.xpath("//table//tbody//tr/td[normalize-space(text())=" + nameLiteral + "]"));
        boolean isNameDisplayed = driverTools.waitForElementToAppear(nameCell).isPresent();
        Assertion.assertThat("Verify Participant Name is in the table: " + expectedName, isNameDisplayed, is(true));

        String actualId = getParticipantTableData(expectedName, 1);
        Assertion.assertThat("Verify Participant ID for " + expectedName + ". Expected: " + expectedId + " Actual: " + actualId, actualId, is(equalTo(expectedId)));

        String actualDob = getParticipantTableData(expectedName, 2);
        String formattedExpectedDob = formatDobForUi(expectedDob);
        Assertion.assertThat("Verify Participant DOB for " + expectedName + ". Expected: " + formattedExpectedDob + " Actual: " + actualDob, normalizeMonth(actualDob), is(equalTo(normalizeMonth(formattedExpectedDob))));
    }

    public void verifySubmitConfirmationModalAppears() {
        WebElement modalTitle = driverTools.waitForElementToAppear(SUBMIT_CONFIRMATION_MODAL_TITLE).orElseThrow();
        Assertion.assertThat("Verify 'Submit this booking request?' modal is displayed", modalTitle.isDisplayed(), is(true));
    }

    public void clickSubmitRequestConfirmation() {
        WebElement submitReqBtn = driverTools.waitForElementToBeClickable(MODAL_SUBMIT_REQUEST_BTN).orElseThrow();
        driverTools.click(submitReqBtn);
    }

    public void searchForEventInDashboard(String eventName) {
        WebElement searchInput = driverTools.waitForElementToBeClickable(SEARCH_BY_KEYWORD_INPUT).orElseThrow();

        driverTools.clearInput(searchInput);
        driverTools.sendKeys(searchInput, eventName);

    }

    public void verifyEventInDashboardTable(String eventName, String expectedRequestType, String expectedRequestor,
                                            String expectedStartDate, String expectedEndDate,
                                            String expectedLocation, String expectedParticipants) {

        // Wait for the specific row to appear in the table
        Locator rowLocator = getDashboardTableCellLocator(eventName, 1);
        boolean isEventDisplayed = driverTools.waitForElementToAppear(rowLocator).isPresent();
        Assertion.assertThat("Verify Event '" + eventName + "' appears in dashboard table", isEventDisplayed, is(true));

        // Verify Request Type (Column 2)
        String actualRequestType = driverTools.waitForElementToAppear(getDashboardTableCellLocator(eventName, 2)).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Dashboard Request Type. Expected: " + expectedRequestType + " Actual: " + actualRequestType, actualRequestType, is(equalTo(expectedRequestType)));

        // Verify Requestor (Column 3)
        String actualRequestor = driverTools.waitForElementToAppear(getDashboardTableCellLocator(eventName, 3)).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Dashboard Requestor. Expected: " + expectedRequestor + " Actual: " + actualRequestor, actualRequestor, is(equalTo(expectedRequestor)));

        // Verify Start Date (Column 4)
        String actualStartDate = driverTools.waitForElementToAppear(getDashboardTableCellLocator(eventName, 4)).orElseThrow().getText().trim();
        String formattedStartDate = formatDobForUi(expectedStartDate);
        Assertion.assertThat("Verify Dashboard Start Date. Expected: " + formattedStartDate + " Actual: " + actualStartDate, normalizeMonth(actualStartDate), is(equalTo(normalizeMonth(formattedStartDate))));

        // Verify End Date (Column 5)
        String actualEndDate = driverTools.waitForElementToAppear(getDashboardTableCellLocator(eventName, 5)).orElseThrow().getText().trim();
        String formattedEndDate = formatDobForUi(expectedEndDate);
        Assertion.assertThat("Verify Dashboard End Date. Expected: " + formattedEndDate + " Actual: " + actualEndDate, normalizeMonth(actualEndDate), is(equalTo(normalizeMonth(formattedEndDate))));

        // Verify Location (Column 7)
        String actualLocation = driverTools.waitForElementToAppear(getDashboardTableCellLocator(eventName, 7)).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Dashboard Location. Expected: " + expectedLocation + " Actual: " + actualLocation, actualLocation, is(equalTo(expectedLocation)));

        // Verify Participants Count (Column 8)
        String actualParticipants = driverTools.waitForElementToAppear(getDashboardTableCellLocator(eventName, 8)).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Dashboard Participants Count. Expected: " + expectedParticipants + " Actual: " + actualParticipants, actualParticipants, is(equalTo(expectedParticipants)));
    }

    public void verifySnackbarAndWaitUntilDismissed(String expectedMessage) {
        WebElement snackbar = driverTools.waitForElementToAppear(SNACKBAR_CONTAINER).orElseThrow();

        String actualMessage = snackbar.getText().trim();
        Assertion.assertThat("Verify Snackbar Message. Expected: " + expectedMessage + " Actual: " + actualMessage, actualMessage, containsString(expectedMessage));

        driverTools.waitForElementToDisappear(SNACKBAR_CONTAINER);
    }

    public void cancelRequestFromDashboard() {
        clickTableDetailMoreActionsButton();

        WebElement cancelMenuItem = driverTools.waitForElementToBeClickable(CANCEL_REQUEST_MENU_ITEM).orElseThrow();
        driverTools.click(cancelMenuItem);

        WebElement confirmCancelBtn = driverTools.waitForElementToBeClickable(CANCEL_REQUEST_CONFIRM_BTN).orElseThrow();
        driverTools.click(confirmCancelBtn);
    }

    public void verifyCancelRequestSnackbar(String expectedMessage) {
        String actualMessage = driverTools.waitForElementToAppear(SNACKBAR_TEXT_CONTENT).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Cancel Request Snackbar. Expected: " + expectedMessage + " Actual: " + actualMessage, actualMessage, is(equalTo(expectedMessage)));
    }

    public void clickCreatePaymentRequest() {
        WebElement createBtn = driverTools.waitForElementToBeClickable(CREATE_PAYMENT_REQUEST_BTN).orElseThrow();
        driverTools.scrollElementToCenter(createBtn);
        driverTools.click(createBtn);
    }

    public void verifyTakeoverPaymentPageOpened() {
        WebElement pageTitle = driverTools.waitForElementToAppear(TAKEOVER_PAYMENT_PAGE_TITLE).orElseThrow();
        Assertion.assertThat("Verify 'Takeover Payment' page is displayed", pageTitle.isDisplayed(), is(true));
    }

    public void fillTakeoverTextInputs(String eventName, String participants, String amount, String vendor, String note) {
        WebElement nameInput = driverTools.waitForElementToBeClickable(EVENT_NAME_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(nameInput);
        driverTools.clearInput(nameInput);
        driverTools.sendKeys(nameInput, eventName);

        WebElement partsInput = driverTools.waitForElementToBeClickable(PARTICIPANTS_INPUT).orElseThrow();
        driverTools.clearInput(partsInput);
        driverTools.sendKeys(partsInput, participants);

        WebElement amountInput = driverTools.waitForElementToBeClickable(TAKEOVER_ORDER_AMOUNT).orElseThrow();
        driverTools.scrollElementToCenter(amountInput);
        driverTools.clearInput(amountInput);
        driverTools.sendKeys(amountInput, amount);

        WebElement vendorInput = driverTools.waitForElementToBeClickable(TAKEOVER_VENDOR_NAME).orElseThrow();
        driverTools.clearInput(vendorInput);
        driverTools.sendKeys(vendorInput, vendor);

        WebElement noteInput = driverTools.waitForElementToBeClickable(DESCRIPTION_TEXTAREA).orElseThrow();
        driverTools.scrollElementToCenter(noteInput);
        driverTools.clearInput(noteInput);
        driverTools.sendKeys(noteInput, note);
    }

    public void clickNextContactAndTravelButton() {
        WebElement nextBtn = driverTools.waitForElementToBeClickable(NEXT_CONTACT_TRAVEL_BTN).orElseThrow();
        driverTools.scrollElementToCenter(nextBtn);
        driverTools.click(nextBtn);
    }

    public void verifyContactAndBookingSectionOpens() {
        WebElement header = driverTools.waitForElementToAppear(CONTACT_AND_BOOKING_PAGE_HEADER).orElseThrow();
        Assertion.assertThat("Verify the 'Contact & Participant Details' section is successfully opened", header.isDisplayed(), is(true));
    }

    public void fillBookingAndTravelDetails(String bookingCode, String approvalNumber) {
        WebElement bookingInput = driverTools.waitForElementToBeClickable(BOOKING_CODE_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(bookingInput);
        driverTools.clearInput(bookingInput);
        driverTools.sendKeys(bookingInput, bookingCode);

        WebElement approvalInput = driverTools.waitForElementToBeClickable(TRAVEL_APPROVAL_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(approvalInput);
        driverTools.clearInput(approvalInput);
        driverTools.sendKeys(approvalInput, approvalNumber);
    }

    public void verifyTakeoverSummaryDetails(String productType, String eventName, String participantsNum,
                                             String expectedStartDay, String expectedEndDay,
                                             String vendorName, String orderAmount, String addNote,
                                             String bookingCode, String approvalNum) {

        String actualProduct = getActualProductChip(productType);
        Assertion.assertThat("Verify Product Chip. Expected: " + productType + " Actual: " + actualProduct, actualProduct, is(equalTo(productType)));

        String actualEventName = getActualValueForLabel("Event Name");
        Assertion.assertThat("Verify Event Name. Expected: " + eventName + " Actual: " + actualEventName, actualEventName, is(equalTo(eventName)));

        String actualParticipants = getActualValueForLabel("Number of Participants");
        Assertion.assertThat("Verify Participants. Expected to contain: " + participantsNum + " Actual: " + actualParticipants, actualParticipants, containsString(participantsNum));

        String actualStartDate = getActualValueForLabel("Order Start Date");
        Assertion.assertThat("Verify Order Start Date contains: " + expectedStartDay + " Actual: " + actualStartDate, actualStartDate, containsString(expectedStartDay));

        String actualEndDate = getActualValueForLabel("Order End Date");
        Assertion.assertThat("Verify Order End Date contains: " + expectedEndDay + " Actual: " + actualEndDate, actualEndDate, containsString(expectedEndDay));

        String actualVendor = getActualValueForLabel("Vendor Name");
        Assertion.assertThat("Verify Vendor Name. Expected: " + vendorName + " Actual: " + actualVendor, actualVendor, is(equalTo(vendorName)));

        String actualAmount = getActualValueForLabel("Order Amount").replaceAll("[^0-9]", "");
        Assertion.assertThat("Verify Order Amount. Expected: " + orderAmount + " Actual: " + actualAmount, actualAmount, is(equalTo(orderAmount)));

        Locator noteLocator = new Locator("SUMMARY_ADDITIONAL_NOTE", By.xpath("//span[strong[contains(normalize-space(.), 'Additional Note:')]]"));
        String fullNoteText = driverTools.waitForElementToAppear(noteLocator).orElseThrow().getText().trim();
        String actualNote = fullNoteText.replace("Additional Note:", "").trim();
        Assertion.assertThat("Verify Additional Note. Expected: " + addNote + " Actual: " + actualNote, actualNote, is(equalTo(addNote)));

        String actualBookingCode = getActualValueForLabel("Booking Code");
        Assertion.assertThat("Verify Booking Code. Expected: " + bookingCode + " Actual: " + actualBookingCode, actualBookingCode, is(equalTo(bookingCode)));

        String actualApprovalNum = getActualValueForLabel("Travel Approval Number");
        Assertion.assertThat("Verify Travel Approval Number. Expected: " + approvalNum + " Actual: " + actualApprovalNum, actualApprovalNum, is(equalTo(approvalNum)));
    }

    public void verifyTakeOverSubmitConfirmationModalAppears() {
        WebElement modalTitle = driverTools.waitForElementToAppear(TAKE_OVER_SUBMIT_CONFIRMATION_MODAL_TITLE).orElseThrow();
        Assertion.assertThat("Verify 'Submit this booking request?' modal is displayed", modalTitle.isDisplayed(), is(true));
    }

    public String getRequestNumber(String eventName) {
        String eventNameLiteral = com.tiket.core.CoreFunctions.toXPathLiteral(eventName);
        String xpath = "//span[normalize-space(text())=" + eventNameLiteral + "]/following-sibling::span";
        Locator requestNumberLocator = new Locator("REQUEST_NUMBER_FOR_" + eventName.replaceAll("\\s+", "_"), By.xpath(xpath));

        String reqNum = driverTools.waitForElementToAppear(requestNumberLocator).orElseThrow().getText().trim();
        logger.log("Extracted Request Number from External Dashboard: " + reqNum);
        return reqNum;
    }
}
