package com.tiket.web.dashboard.page.hotel.demand;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FailedCheckinDashboardPage <T extends WebDriver> extends BasePage<T> {
    public FailedCheckinDashboardPage(Session<T> session) {super(session);}

    private static final Locator REPORT_FAILED_CHECKIN_BUTTON = new Locator("REPORT_FAILED_CHECKIN_BUTTON", By.xpath("//div[@class='context-menu-item' and text()='Report Failed Checkin']"));
    private static final Locator CONFIRMATION_POPUP_BOX = new Locator("CONFIRMATION_POPUP_BOX", By.xpath("//h2[normalize-space()='Confirmation']"));
    private static final Locator CATEGORY_OPTION = new Locator("CATEGORY_OPTION", By.xpath("//label[normalize-space()='Category']/following::div[contains(@class,'DropdownFormDisplay_icon_container')][1]"));
    private static final Locator CHECKIN_OPTION = new Locator("CHECKIN_OPTION", By.xpath("//label[normalize-space()='Check-in problem']/following::div[contains(@class,'DropdownFormDisplay_icon_container')][1]"));
    private static final Locator REPORTED_FROM_OPTION = new Locator("REPORTED_FROM_OPTION", By.xpath("//label[normalize-space()='Reported from']/following::div[contains(@class,'DropdownFormDisplay_icon_container')][1]"));
    private static final Locator ROOT_CAUSE_OPTION = new Locator("ROOT_CAUSE_OPTION", By.xpath("//label[normalize-space()='Root cause']/following::div[contains(@class,'DropdownFormDisplay_icon_container')][1]"));
    private static final Locator CONFIRMATION_POPUP_BOX_CS = new Locator("CONFIRMATION_POPUP_BOX_CS", By.xpath("//div[@class='modal-header']/div[normalize-space()='Confirmation']"));
    private static final Locator FIRST_CATEGORY_OPTION = new Locator("FIRST_CATEGORY_OPTION", By.xpath("//span[normalize-space()='Barcode Voucher Sudah Digunakan atau Tidak Berlaku']"));
    private static final Locator FIRST_SELECTED_CATEGORY = new Locator("FIRST_SELECTED_CATEGORY", By.xpath("//input[@name='category' and @value='Barcode Voucher Sudah Digunakan atau Tidak Berlaku']"));
    private static final Locator FIRST_CHECKIN_PROBLEM_OPTION = new Locator("FIRST_CHECKIN_PROBLEM_OPTION", By.xpath("//input[@name='checkInProblem' and @value='Hotel Full Book']"));
    private static final Locator FIRST_CHECKIN_PROBLEM_OPTION_PREVIOUS = new Locator("FIRST_CHECKIN_PROBLEM_OPTION_PREVIOUS", By.xpath("//span[normalize-space()='Hotel Full Book']"));
    private static final Locator FIRST_REPORTED_FROM_OPTION = new Locator("FIRST_REPORTED_FROM_OPTION", By.xpath("//span[normalize-space()='Customer']"));
    private static final Locator FIRST_REPORTED_FROM_OPTION_PREVIOUS = new Locator("FIRST_REPORTED_FROM_OPTION_PREVIOUS", By.xpath("//span[normalize-space()='Customer']"));
    private static final Locator FIRST_ROOT_CAUSE_OPTION = new Locator("FIRST_ROOT_CAUSE_OPTION", By.xpath("//input[@name='rootCause' and @value='CM tidak connect ke Extranet']"));
    private static final Locator FIRST_ROOT_CAUSE_OPTION_PREVIOUS = new Locator("FIRST_ROOT_CAUSE_OPTION_PREVIOUS", By.xpath("//span[normalize-space()='CM tidak connect ke Extranet']"));
    private static final Locator YES_CONTINUE_BUTTON = new Locator("YES_CONTINUE_BUTTON", By.xpath("//button[normalize-space()='Yes, Continue']"));
    private static final Locator FAILED_CHECKIN_PAGE = new Locator("FAILED_CHECKIN_PAGE", By.xpath("//div[@class='card']"));
    private static final Locator FAILED_CHECKIN_PAGE_BANNER = new Locator("FAILED_CHECKIN_PAGE_BANNER", By.xpath("//h6[@class='card-header' and text()='Hotel / Failed Check-In']"));
    private static final Locator ORDERID_FILTER = new Locator("ORDERID_FILTER", By.xpath("(//div[contains(@class,'active')]//span[@class='filter-field-label' and text()='Order ID'])[1]"));
    private static final Locator ORDERID_FILTER_INPUT = new Locator("ORDERID_FILTER_INPUT", By.xpath("(//div[contains(@class,'active')]//input[@name='orderId' and @placeholder='Keyword' and @type='text'])[1]"));
    private static final Locator ITINERARYID_FILTER_INPUT = new Locator("ITINERARYID_FILTER_INPUT", By.xpath("(//div[contains(@class,'active')]//input[@name='itineraryId' and @placeholder='Keyword' and @type='text'])[1]"));
    private static final Locator NEED_CS_TAB = new Locator("NEED_CS_TAB", By.xpath("//div/ul/li/a[contains(@class,'failed-checkin-tab') and text()='Need CS']"));
    private static final Locator WAITING_FOR_APPROVAL_TAB = new Locator("WAITING_FOR_APPROVAL_TAB", By.xpath("//div/ul/li/a[contains(@class,'failed-checkin-tab') and text()='Waiting for Approval']"));
    private static final Locator WAITING_MM_APPROVAL_TAB = new Locator("WAITING_MM_APPROVAL_TAB", By.xpath("//div/ul/li/a[contains(@class,'failed-checkin-tab') and text()='Waiting MM Approval']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//div[contains(@class,'active')]//button[text()='Search']"));
    private static final Locator MORE_ACTION_BUTTON = new Locator("MORE_ACTION_BUTTON", By.xpath("//div[contains(@class,'active')]//div[@class='context-menu']"));
    private static final Locator OTHER_SOLUTION_BUTTON = new Locator("OTHER_SOLUTION_BUTTON", By.xpath("//div[contains(@class,'active')]//div[@class='context-menu-item' and text()='Other Solution']"));
    private static final Locator WHY_SHOULD_BE_REMOVED_OPTION = new Locator("WHY_SHOULD_BE_REMOVED_OPTION", By.xpath("//input[@name='type' and @type='text']"));
    private static final Locator REASON_OPTION = new Locator("REASON_OPTION", By.xpath("//input[@name='reason' and @type='text']"));
    private static final Locator OTHER_SOLUTION_DROPDOWN = new Locator("OTHER_SOLUTION_DROPDOWN", By.xpath("(//div[contains(@class,'show')]//div[@class='popover-content'])[1]"));
    private static final Locator FIRST_OTHER_SOLUTION_OPTION = new Locator("FIRST_OTHER_SOLUTION_OPTION", By.xpath("(//div[contains(@class,'show')]//div[@class='dropdown-hotel'])[1]"));
    private static final Locator OTHER_SOLUTION_DESC = new Locator("OTHER_SOLUTION_DESC", By.xpath("//textarea[@name='description']"));
    private static final Locator OTHER_SOLUTION_SUCCESS_PAGE_TITLE = new Locator("OTHER_SOLUTION_SUCCESS_PAGE_TITLE", By.xpath("//div[contains(@class,'rebook-redirect-page')]//h2[@class='title']"));
    private static final Locator OTHER_SOLUTION_SUCCESS_PAGE_DESC = new Locator("OTHER_SOLUTION_SUCCESS_PAGE_DESC", By.xpath("//div[contains(@class,'rebook-redirect-page')]//p[@class='description']"));
    private static final Locator RELOCATION = new Locator("RELOCATION", By.xpath("//div[@class=\"context-menu-item\" and contains(text(), 'Relocation')]"));
    private static final Locator APPROVAL_PREVIEW_ACTION_BTN = new Locator("APPROVAL_PREVIEW_ACTION_BTN", By.xpath("//div[contains(@class,'active')]//i[contains(@class,'icon') and contains(@class,'action-view')]"));

    public void clickReportFailedCheckinButton(){
        WebElement button = driverTools.waitForElementToBeClickable(REPORT_FAILED_CHECKIN_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isConfirmationPopupBoxLoaded(){
        return driverTools.waitForElementsToAppear(CONFIRMATION_POPUP_BOX).isPresent();
    }

    public boolean isConfirmationPopupBoxLoadedCS(){
        return driverTools.waitForElementsToAppear(CONFIRMATION_POPUP_BOX_CS).isPresent();
    }

    public boolean isCategoryVisible(){
        return driverTools.waitForElementsToAppear(CATEGORY_OPTION).isPresent();
    }

    public boolean isCheckInProblemVisible(){
        return driverTools.waitForElementsToAppear(CHECKIN_OPTION).isPresent();
    }

    public boolean isReportedFromVisible(){
        return driverTools.waitForElementsToAppear(REPORTED_FROM_OPTION).isPresent();
    }

    public boolean isRootCauseVisible(){
        return driverTools.waitForElementsToAppear(ROOT_CAUSE_OPTION).isPresent();
    }

    public void selectCategory(){
        driverTools.click(CATEGORY_OPTION);
    }

    public void selectFirstCategoryOption(){
        driverTools.click(FIRST_CATEGORY_OPTION);
    }

    public String isFirstCategoryOptionVisible(){
        WebElement Button=driverTools.waitForElementToAppear(FIRST_SELECTED_CATEGORY).orElseThrow();
        return driverTools.getValue(Button);
    }

    public void selectCheckInProblem(){
        driverTools.click(CHECKIN_OPTION);
    }

    public boolean isCategoryDropdownVisible(){
        return driverTools.waitForElementsToAppear(CATEGORY_OPTION).isPresent();
    }

    public boolean isCheckInProblemDropdownVisible(){
        return driverTools.waitForElementsToAppear(CHECKIN_OPTION).isPresent();
    }

    public void selectFirstCheckInProblemOption(){
        driverTools.click(FIRST_CHECKIN_PROBLEM_OPTION_PREVIOUS);
    }

    public String isFirstCheckInProblemOptionVisible(){
        WebElement Button=driverTools.waitForElementToAppear(FIRST_CHECKIN_PROBLEM_OPTION).orElseThrow();
        return driverTools.getValue(Button);
    }

    public void selectReportedFrom(){
        driverTools.click(REPORTED_FROM_OPTION);
    }

    public boolean isReportedFromDropdownVisible(){
        return driverTools.waitForElementsToAppear(REPORTED_FROM_OPTION).isPresent();
    }

    public void selectFirstReportedFromOption(){
        driverTools.click(FIRST_REPORTED_FROM_OPTION_PREVIOUS);
    }

    public String isFirstReportedFromOptionVisible(){
        WebElement Button=driverTools.waitForElementToAppear(FIRST_REPORTED_FROM_OPTION).orElseThrow();
        return driverTools.getText(Button);
    }

    public void selectRootCause(){
        driverTools.click(ROOT_CAUSE_OPTION);
    }

    public boolean isRootCauseDropdownVisible(){
        return driverTools.waitForElementsToAppear(ROOT_CAUSE_OPTION).isPresent();
    }

    public void selectFirstRootCauseOption(){
        driverTools.click(FIRST_ROOT_CAUSE_OPTION_PREVIOUS);
    }

    public String isFirstRootCauseOptionVisible(){
        WebElement Button=driverTools.waitForElementToAppear(FIRST_ROOT_CAUSE_OPTION).orElseThrow();
        return driverTools.getValue(Button);
    }

    public void clickYesContinueButton(){
        driverTools.click(YES_CONTINUE_BUTTON);
    }

    public boolean isFailedCheckinPageLoaded(){
        return driverTools.waitForElementsToAppear(FAILED_CHECKIN_PAGE).isPresent() && driverTools.waitForElementsToAppear(FAILED_CHECKIN_PAGE_BANNER).isPresent() ;
    }

    public boolean isOrderIdFilterLoaded(){
        return driverTools.waitForElementsToAppear(ORDERID_FILTER).isPresent() && driverTools.waitForElementsToAppear(ORDERID_FILTER_INPUT).isPresent() ;
    }

    public void setOrderIdInput(String id){
        driverTools.waitForElementToBeClickable(ORDERID_FILTER_INPUT);
        WebElement input = driverTools.waitForElementToAppear(ORDERID_FILTER_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(ORDERID_FILTER_INPUT, id);
    }

    public String newEnteredOrderId(){
        driverTools.waitForElementToAppear(ORDERID_FILTER_INPUT);
        return driverTools.getValue(ORDERID_FILTER_INPUT);
    }

    public boolean isDefaultTabNeedCS() {
        Locator CURRENT_TAB_NAME_LOCATOR = new Locator("CURRENT_TAB_NAME_LOCATOR", By.xpath("//ul//li//a[contains(@class,'selected')]"));
        String currentTabName = driverTools.waitForElementToAppear(CURRENT_TAB_NAME_LOCATOR).orElseThrow().getText();
        return currentTabName.equalsIgnoreCase("Need CS");
    }
  
    public void clickSearchButton() {
        driverTools.waitForElementToBeClickable(SEARCH_BUTTON);
        driverTools.click(SEARCH_BUTTON);
        logger.log("Waiting for 2 seconds for new search data to appear");
        driverTools.dummyWait(2);
    }
  
    public void clickMoreActionButton(){
        WebElement button = driverTools.waitForElementToBeClickable(MORE_ACTION_BUTTON).orElseThrow();
        driverTools.click(button);
    }
  
    public boolean isMoreActionButtonClickable(){
        return driverTools.waitForElementToBeClickable(MORE_ACTION_BUTTON).isPresent();
    }
  
    public boolean isOtherSolutionButtonClickable(){
        return driverTools.waitForElementToBeClickable(OTHER_SOLUTION_BUTTON).isPresent();
    }
 
    public boolean isWhyShouldBeRemovedVisible(){
        return driverTools.waitForElementsToAppear(WHY_SHOULD_BE_REMOVED_OPTION).isPresent();
    }

    public void selectWhyShouldBeRemoved(){
        driverTools.click(WHY_SHOULD_BE_REMOVED_OPTION);
    }
  
    public void clickOtherSolutionButton(){
        WebElement button = driverTools.waitForElementToBeClickable(OTHER_SOLUTION_BUTTON).orElseThrow();
        driverTools.click(button);
    }
  
    public void selectFirstDropdownOption(){
        driverTools.click(FIRST_OTHER_SOLUTION_OPTION);
    }

    public String isFirstWhyShouldBeRemovedOptionVisible(){
        WebElement Button=driverTools.waitForElementToPresence(WHY_SHOULD_BE_REMOVED_OPTION).orElseThrow();
        return driverTools.getValue(Button);
    }

    public boolean isOtherSolutionDropdownVisible(){
        return driverTools.waitForElementsToAppear(OTHER_SOLUTION_DROPDOWN).isPresent();
    }

    public boolean isReasonVisible(){
        return driverTools.waitForElementsToAppear(REASON_OPTION).isPresent();
    }

    public void selectReason(){
        driverTools.click(REASON_OPTION);
    }
  
    public void fillOtherSolutionDescription(String description) {
        driverTools.sendKeys(OTHER_SOLUTION_DESC, description);
    }

    public void clickOtherSolutionPopupYesContinueButton() {
        Locator OTHER_SOLUTION_YES_CONTINUE_BUTTON = new Locator("OTHER_SOLUTION_YES_CONTINUE_BUTTON", By.xpath("//button[contains(@class, 'button') and text()='Yes, Continue']"));
        driverTools.waitForElementToBeClickable(OTHER_SOLUTION_YES_CONTINUE_BUTTON).orElseThrow();
        driverTools.click(OTHER_SOLUTION_YES_CONTINUE_BUTTON);
    }

    public String isFirstReasonOptionVisible(){
        WebElement Button=driverTools.waitForElementToPresence(REASON_OPTION).orElseThrow();
        return driverTools.getValue(Button);
    }
  
    public Boolean isOtherSolutionSuccessPageAppears() {
        return driverTools.waitForElementToAppear(OTHER_SOLUTION_SUCCESS_PAGE_TITLE).isPresent();
    }

    public String isOtherSolutionSuccessTitleAppears() {
        return driverTools.waitForElementToAppear(OTHER_SOLUTION_SUCCESS_PAGE_TITLE).orElseThrow().getText();
    }

    public String isOtherSolutionSuccessDescriptionAppears() {
        return driverTools.waitForElementToAppear(OTHER_SOLUTION_SUCCESS_PAGE_DESC).orElseThrow().getText();
    }

    public boolean isDescriptionVisible(){
        return driverTools.waitForElementsToAppear(OTHER_SOLUTION_DESC).isPresent();
    }
  
    public void setItineraryIdInput(String id){
        driverTools.waitForElementToBeClickable(ITINERARYID_FILTER_INPUT);
        WebElement input = driverTools.waitForElementToAppear(ITINERARYID_FILTER_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(ITINERARYID_FILTER_INPUT, id);
    }

    public boolean isLocatorFieldEditable(Locator locator){
        return driverTools.waitForElementToAppear(locator)
                .map(element -> {
                    if (!element.isDisplayed()) {
                        logger.log("Field is not editable because it is hidden.");
                        return false;
                    }

                    if (!element.isEnabled()) {
                        logger.log("Field is not editable because it is disabled.");
                        return false;
                    }

                    String readonlyAttr = element.getAttribute("readonly");
                    if (readonlyAttr != null && (readonlyAttr.equalsIgnoreCase("true") || readonlyAttr.equalsIgnoreCase("readonly"))) {
                        logger.log("Field is not editable because it is marked as read-only.");
                        return false;
                    }

                    return true;
                })
                .orElse(false);
    }

    public void switchFailedCheckinApprovalTab(String tabName) {
        Locator CURRENT_TAB_NAME_LOCATOR = new Locator("CURRENT_TAB_NAME_LOCATOR", By.xpath("//ul//li//a[contains(@class,'selected')]"));
        String currentTabName = driverTools.waitForElementToAppear(CURRENT_TAB_NAME_LOCATOR).orElseThrow().getText();

        if (tabName.equals(currentTabName)) {
            logger.log("Already at target tab: " + currentTabName);
            return;
        }

        logger.log("Switching to tab: " + tabName);
        if (tabName.equalsIgnoreCase("Need CS")) {
            driverTools.waitForElementToAppear(NEED_CS_TAB);
            driverTools.clickWithJavascriptExecutor(NEED_CS_TAB);
            driverTools.waitForTextToBePresent(CURRENT_TAB_NAME_LOCATOR, "Need CS", 10);
        }
        else if (tabName.equalsIgnoreCase("Waiting for Approval")) {
            driverTools.waitForElementToAppear(WAITING_FOR_APPROVAL_TAB);
            driverTools.clickWithJavascriptExecutor(WAITING_FOR_APPROVAL_TAB);
            driverTools.waitForTextToBePresent(CURRENT_TAB_NAME_LOCATOR, "Waiting for Approval", 10);
        }
        else if (tabName.equalsIgnoreCase("Waiting MM Approval")) {
            driverTools.waitForElementToAppear(WAITING_MM_APPROVAL_TAB);
            driverTools.clickWithJavascriptExecutor(WAITING_MM_APPROVAL_TAB);
            driverTools.waitForTextToBePresent(CURRENT_TAB_NAME_LOCATOR, "Waiting MM Approval", 10);
        } else {
            throw new IllegalArgumentException("Unknown tab name: " + tabName);
        }
    }

    public boolean isOrderIdFilterEditable() {
        return isLocatorFieldEditable(ORDERID_FILTER_INPUT);
    }

    public boolean isItineraryIdFilterEditable() {
        return isLocatorFieldEditable(ITINERARYID_FILTER_INPUT);
    }

    public boolean verifyNoDataAvailable() {
        Locator NO_DATA_AVAILABLE = new Locator("NO_DATA_AVAILABLE", By.xpath("//div[contains(@class,'active')]//div[@class='failed-checkin-list-nodata']"));
        return driverTools.waitForElementToAppear(NO_DATA_AVAILABLE).orElseThrow().getText().equalsIgnoreCase("No Data Available");
    }
    public String getNoDataAvailableErrorMessage() {
        Locator NO_DATA_AVAILABLE = new Locator("NO_DATA_AVAILABLE", By.xpath("//div[contains(@class,'active')]//div[@class='failed-checkin-list-nodata']"));
        return driverTools.waitForElementToAppear(NO_DATA_AVAILABLE).orElseThrow().getText();
    }

    public boolean verifyDataAvailable() {
        Locator DATA_LOCATOR = new Locator("DATA_LOCATOR", By.xpath("//div[contains(@class,'active')]//div[@class='table-wrapper']//table//tbody//tr"));
        driverTools.waitForElementToAppear(DATA_LOCATOR);
        return !driverTools.elements(DATA_LOCATOR).orElseThrow().isEmpty();
    }

    public List<String> getColumnValues(String targetHeadingName) {
        List<String> columnValues = new ArrayList<>();
        Locator headerLocator = new Locator(targetHeadingName, By.xpath("//div[contains(@class,'active')]//table/thead/tr/*[self::th or self::td]"));
        driverTools.waitForElementToAppear(headerLocator);

        logger.log("Now getting table headings");
        List<WebElement> headers = driverTools.getDriver().findElements(headerLocator.by());
        logger.log(String.format("Checking if there are %s column values present", headers.size()));

        int targetColumnIndex = -1;
        for (int i = 0; i < headers.size(); i++) {
            logger.log("Heading: " + headers.get(i).getText());
            if (headers.get(i).getText().trim().equalsIgnoreCase(targetHeadingName)) {
                targetColumnIndex = i + 1;
                break;
            }
        }

        if (targetColumnIndex == -1) {
            throw new IllegalArgumentException("Heading column '" + targetHeadingName + "' was not found in the table.");
        }

        Locator rowIndexLocator = new Locator("rowIndexLocator", By.xpath("//div[contains(@class,'active')]//table/tbody/tr/td[%d]".formatted(targetColumnIndex)));
        driverTools.waitForElementToAppear(rowIndexLocator);
        List<WebElement> rowIndexElements = driver.findElements(rowIndexLocator.by());
        if (rowIndexElements.isEmpty()) {
            logger.log("There are no rows in the table.");
            return columnValues;
        }

        for (WebElement element : rowIndexElements) {
            columnValues.add(driverTools.getText(element));
        }

        return columnValues;
    }

    public String getOrderIdTable() {
        List<String> values = getColumnValues("Order Id");
        if (values.isEmpty()) { throw new IllegalStateException("No rows found in Failed Check-in table"); }
        return values.get(0);
    }
    public String getItineraryIdTable() {
        List<String> values = getColumnValues("Itinerary Id");
        if (values.isEmpty()) { throw new IllegalStateException("No rows found in Failed Check-in table"); }
        return values.get(0);
    }
    public String getTicketStatusTable() {
        List<String> values = getColumnValues("Ticket Status");
        if (values.isEmpty()) { throw new IllegalStateException("No rows found in Failed Check-in table"); }
        return values.get(0);
    }

    public boolean isRelocationButtonClickable() {
        driverTools.waitForElementToAppear(RELOCATION);
        driverTools.scrollIntoWebView(RELOCATION);
        return driverTools.waitForElementToBeClickable(RELOCATION).isPresent();
    }
    public void clickRelocationButton(){
        driverTools.scrollIntoWebView(RELOCATION);
        driverTools.click(RELOCATION);
    }

    public Boolean isRelocationPageOpened() {
        Locator RELOCATION_PAGE = new Locator("RELOCATION_PAGE", By.xpath("//div[@class='rebooking']"));
        return driverTools.waitForElementToBeClickable(RELOCATION_PAGE).isPresent();
    }

    public String getRelocationOrderId() {
        Locator RELOCATION_ORDER_ID = new Locator("RELOCATION_ORDER_ID", By.xpath("//div[text()='Order ID']//following-sibling::div"));
        return driverTools.getText(RELOCATION_ORDER_ID).orElseThrow(() -> new IllegalStateException("Order ID not found in Relocation page"));
    }
    public String getRelocationItineraryId() {
        Locator RELOCATION_ITINERARY_ID = new Locator("RELOCATION_ITINERARY_ID", By.xpath("//div[text()='Itinerary ID']//following-sibling::div"));
        return driverTools.getText(RELOCATION_ITINERARY_ID).orElseThrow(() -> new IllegalStateException("Itinerary ID not found in Relocation page"));
    }
    public String getRelocationOrderIssuedStatus() {
        Locator RELOCATION_ORDER_STATUS = new Locator("RELOCATION_ORDER_STATUS", By.xpath("//div[text()='Order Issued']//following-sibling::div"));
        return driverTools.getText(RELOCATION_ORDER_STATUS).orElseThrow(() -> new IllegalStateException("Order Issued status not found in Relocation page"));
    }

    public String selectRelocationHotel() {
        Locator RELOCATION_HOTELS = new Locator("RELOCATION_HOTELS", By.xpath("//div[@class='relocation-hotel']"));
        driverTools.waitForElementsToAppear(RELOCATION_HOTELS);
        List<WebElement> relocationHotels = driverTools.elementsUntilExist(RELOCATION_HOTELS)
                .orElseThrow(() -> new IllegalStateException("Hotel not found in Relocation page"));
        int randomIndex = CoreFunctions.generateRandomInt(0, relocationHotels.size() - 1);
        if (randomIndex < 0 || randomIndex >= relocationHotels.size()) {
            randomIndex = 0;
        }

        driverTools.scrollIntoWebView(relocationHotels.get(randomIndex));
        driverTools.waitForElementToBeClickable(relocationHotels.get(randomIndex));
        String hotelName = driverTools.getText(new Locator("RELOCATION_HOTEL_NAME", By.xpath("(//div[contains(@class,'relocation-hotel-title')])[%d]//span[1]".formatted(randomIndex + 1)))).orElseThrow(() -> new IllegalStateException("Hotel name not found in Relocation page"));
        driverTools.clickWithJavascriptExecutor(relocationHotels.get(randomIndex));

        Locator SELECT_BTN = new Locator("SELECT_BTN", By.xpath("//button[@type='button' and contains(text(),'select')]"));
        driverTools.waitForElementToBeClickable(SELECT_BTN);
        driverTools.clickWithJavascriptExecutor(SELECT_BTN);

        return hotelName;
    }

    public String getCheckInCheckOutDateTable(boolean includeDayOfWeek) {
        List<String> checkinValues = getColumnValues("Checkin");
        List<String> checkoutValues = getColumnValues("Checkout");

        if (checkinValues.isEmpty()) { throw new IllegalStateException("No rows found in Check-in table"); }
        if (checkoutValues.isEmpty()) { throw new IllegalStateException("No rows found in Checkout table"); }

        String rawCheckIn = checkinValues.get(0).trim();
        String rawCheckOut = checkoutValues.get(0).trim();

        DateTimeFormatter tableParser = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US);

        LocalDate checkInDate = LocalDate.parse(rawCheckIn, tableParser);
        LocalDate checkOutDate = LocalDate.parse(rawCheckOut, tableParser);

        String targetPattern = includeDayOfWeek ? "E, dd MMM yyyy" : "dd MMM yyyy";
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(targetPattern, Locale.US);

        return "%s - %s".formatted(checkInDate.format(outputFormatter), checkOutDate.format(outputFormatter));
    }

    public void clickRebookingContinueBookingButton(){
        Locator CONTINUE_BOOKING_BTN = new Locator("CONTINUE_BOOKING_BTN", By.xpath("//button[text()='Continue Booking']"));
        driverTools.waitForElementToBeClickable(CONTINUE_BOOKING_BTN);
        driverTools.click(CONTINUE_BOOKING_BTN);
    }

    public Boolean isRebookingPopupAppears() {
        Locator POP_UP = new Locator("POP_UP", By.xpath("//div[contains(@class,'Dialog_container')]"));
        return driverTools.waitForElementToAppear(POP_UP).isPresent();
    }
    public String rebookingPopupHeading(){
        Locator POPUP_HEADING = new Locator("POPUP_HEADING", By.xpath("//div[contains(@class,'Dialog_container')]//h2[contains(@class,'title')]"));
        driverTools.waitForElementToAppear(POPUP_HEADING);
        return driverTools.getText(POPUP_HEADING).orElseThrow(() -> new IllegalStateException("Heading not found in Dialog heading"));
    }
    public String rebookingPopupDescription(){
        Locator POPUP_DESCRIPTION = new Locator("POPUP_DESCRIPTION", By.xpath("//div[contains(@class,'Dialog_container')]//p[contains(@class,'description')]"));
        driverTools.waitForElementToAppear(POPUP_DESCRIPTION);
        return driverTools.getText(POPUP_DESCRIPTION).orElseThrow(() -> new IllegalStateException("Description not found in Dialog heading"));
    }
    public void clickRebookingPopupContinueBookingButton(){
        Locator CONTINUE_BOOKING_BTN = new Locator("CONTINUE_BOOKING_BTN", By.xpath("//div[contains(@class,'Dialog_container')]//button[text()='Continue Booking']"));
        driverTools.waitForElementToBeClickable(CONTINUE_BOOKING_BTN);
        driverTools.click(CONTINUE_BOOKING_BTN);
    }

    public Boolean isRebookingSuccessPageAppears() {
        Locator SUCCESS_PAGE = new Locator("SUCCESS_PAGE", By.xpath("//div//img[contains(@alt,'check the details')]//following-sibling::div"));
        return driverTools.waitForElementToAppear(SUCCESS_PAGE).isPresent();
    }
    public String rebookingSuccessPageHeading(){
        Locator SUCCESS_PAGE_HEADING = new Locator("SUCCESS_PAGE_HEADING", By.xpath("//div//img[contains(@alt,'check the details')]//following-sibling::div//span[1]"));
        driverTools.waitForElementToAppear(SUCCESS_PAGE_HEADING);
        return driverTools.getText(SUCCESS_PAGE_HEADING).orElseThrow(() -> new IllegalStateException("Heading not found in Dialog heading"));
    }
    public String rebookingSuccessPageDescription(){
        Locator SUCCESS_PAGE_DESCRIPTION = new Locator("SUCCESS_PAGE_DESCRIPTION", By.xpath("//div//img[contains(@alt,'check the details')]//following-sibling::div//span[2]"));
        driverTools.waitForElementToAppear(SUCCESS_PAGE_DESCRIPTION);
        return driverTools.getText(SUCCESS_PAGE_DESCRIPTION).orElseThrow(() -> new IllegalStateException("Description not found in Dialog heading"));
    }

    public Boolean isApprovalPreviewActionButtonClickable() {
        return driverTools.waitForElementToBeClickable(APPROVAL_PREVIEW_ACTION_BTN).isPresent();
    }

    public void clickApprovalPreviewActionButton() {
        WebElement button = driverTools.waitForElementToBeClickable(APPROVAL_PREVIEW_ACTION_BTN).orElseThrow();
        driverTools.click(button);
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
        return driverTools.getText(element).trim();
    }

    public String getRoomDetailPreviousDetailPreviewPage() {
        Locator roomLocator = new Locator("Previous Details Room", By.xpath("//span[text()='Previous Details']/following-sibling::div//p[contains(normalize-space(.), 'Room')]"));
        WebElement element = driverTools.waitForElementToAppear(roomLocator)
                .orElseThrow(() -> new RuntimeException("Room details not found for Previous Details"));
        return driverTools.getText(element).trim();
    }

    public String getGuestDetailPreviousDetailPreviewPage() {
        Locator guestLocator = new Locator("Previous Details Guest", By.xpath("//span[text()='Previous Details']/following-sibling::div//p[contains(normalize-space(.), 'Guest')]"));
        WebElement element = driverTools.waitForElementToAppear(guestLocator)
                .orElseThrow(() -> new RuntimeException("Guest details not found for Previous Details"));
        return driverTools.getText(element).trim();
    }

    public String getNightDetailPreviousDetailPreviewPage() {
        Locator nightLocator = new Locator("Previous Details Night", By.xpath("//span[text()='Previous Details']/following-sibling::div//p[contains(normalize-space(.), 'Night')]"));
        WebElement element = driverTools.waitForElementToAppear(nightLocator)
                .orElseThrow(() -> new RuntimeException("Night details not found for Previous Details"));
        return driverTools.getText(element).trim();
    }

    public String getDateRangeNewDetailPreviewPage() {
        String checkInXpath = "//span[text()='New Details']/following-sibling::div//div[div[text()='Check-in']]/div[2]";
        String checkOutXpath = "//span[text()='New Details']/following-sibling::div//div[div[text()='Check-out']]/div[2]";

        WebElement checkInEl = driverTools.waitForElementToAppear(new Locator("New Check-In", By.xpath(checkInXpath)))
                .orElseThrow(() -> new RuntimeException("Check-in date block not found for New Details"));
        WebElement checkOutEl = driverTools.waitForElementToAppear(new Locator("New Check-Out", By.xpath(checkOutXpath)))
                .orElseThrow(() -> new RuntimeException("Check-out date block not found for New Details"));

        return String.format("%s - %s", driverTools.getText(checkInEl).trim(), driverTools.getText(checkOutEl).trim());
    }

    public String getRoomDetailNewDetailPreviewPage() {
        Locator roomLocator = new Locator("New Details Room", By.xpath("//span[text()='New Details']/following-sibling::div//div[div[text()='Room']]//span[contains(normalize-space(.), 'Room')]"));
        WebElement element = driverTools.waitForElementToAppear(roomLocator)
                .orElseThrow(() -> new RuntimeException("Room details span not found for New Details"));
        return driverTools.getText(element).trim();
    }

    public String getGuestDetailNewDetailPreviewPage() {
        Locator guestLocator = new Locator("New Details Guest", By.xpath("//span[text()='New Details']/following-sibling::div//div[div[text()='Room']]//span[contains(normalize-space(.), 'Guest')]"));
        WebElement element = driverTools.waitForElementToAppear(guestLocator)
                .orElseThrow(() -> new RuntimeException("Guest details span not found for New Details"));
        return driverTools.getText(element).trim();
    }

    public String getNightDetailNewDetailPreviewPage() {
        Locator nightLocator = new Locator("New Details Night", By.xpath("//span[text()='New Details']/following-sibling::div//div[div[text()='Duration']]/div[2]"));
        WebElement element = driverTools.waitForElementToAppear(nightLocator)
                .orElseThrow(() -> new RuntimeException("Duration night details not found for New Details"));
        return driverTools.getText(element).trim();
    }

    public void clickApprovePreviewPageButton() {
        Locator APPROVE_BUTTON = new Locator("APPROVE_BUTTON", By.xpath("//button[contains(text(),'Approve')]"));
        driverTools.waitForElementToBeClickable(APPROVE_BUTTON);
        driverTools.click(APPROVE_BUTTON);
    }

    public String getTotalPricePreviewPage(){
        Locator allPriceLocator = new Locator("Preview Page Total Price", By.xpath("//span[contains(normalize-space(.),'Price')]/ancestor::div[1]/following-sibling::div[1]/span"));
        WebElement element = driverTools.waitForElementToAppear(allPriceLocator)
                .orElseThrow(() -> new RuntimeException("Total Price details not found"));

        String rawPrice = driverTools.getText(element);
        String noSpaces = rawPrice.replace(" ", "").trim();
        return noSpaces.replaceAll("[A-Za-z\\$\\u20AC\\u00A3\\u00A5]", "");
    }
}