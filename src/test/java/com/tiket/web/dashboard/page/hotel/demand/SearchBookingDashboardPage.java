package com.tiket.web.dashboard.page.hotel.demand;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class SearchBookingDashboardPage <T extends WebDriver> extends BasePage<T> {
    public SearchBookingDashboardPage(Session<T> session) {
        super(session);
    }

    private static final Locator INPUT_ORDER_ID = new Locator("INPUT_ORDER_ID", By.xpath("//input[@placeholder='Enter Order ID' and @type='text']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[@type='submit' and text()='Search']"));
    private static final Locator ORDER_ID = new Locator("ORDER_ID", By.xpath("//a[@class='order-detail-orderid']"));
    private static final Locator ORDER_DETAIL_TABLE = new Locator("ORDER_DETAIL_TABLE", By.xpath("//table[contains(@class,'border-separate')]"));
    private static final Locator RESCHEDULE_BUTTON = new Locator("RESCHEDULE_BUTTON",
            By.xpath("//ul//li//span[text()='Reschedule']"));
    private static final Locator FIRST_MORE_VERTICAL_ICON = new Locator("FIRST_MORE_VERTICAL_ICON",
            By.xpath("//table/tbody/tr/td[last()]//button"));
    private static final Locator CHECKIN_DATE_INPUT_FIELD = new Locator("CHECKIN_DATE_INPUT_FIELD", By.xpath("//input[@id='checkin-date']"));
    private static final Locator CHECKOUT_DATE_INPUT_FIELD = new Locator("CHECKOUT_DATE_INPUT_FIELD", By.xpath("//input[@id='checkout-date']"));
    private static final Locator REASON_DROPDOWN_INPUT = new Locator("REASON_DROPDOWN_INPUT", By.xpath("//input[@id='reason']"));
    private static final Locator REASON_OPTIONS = new Locator("REASON_OPTIONS", By.xpath("//div[contains(@class, 'BaseDropdown_base_dropdown')]//ul//li/span"));
    private static final Locator DESCRIPTION_TEXT_AREA = new Locator("DESCRIPTION_TEXT_AREA", By.xpath("//textarea[@id='description']"));
    private static final Locator CHANGE_TRAVEL_DATE_MODAL_TITLE = new Locator("CHANGE_TRAVEL_DATE_MODAL_TITLE", By.xpath("//div[contains(@class,'Dialog')]//span[1]"));
    private static final Locator CHANGE_TRAVEL_DATE_MODAL_DESC = new Locator("CHANGE_TRAVEL_DATE_MODAL_DESC", By.xpath("//div[contains(@class,'Dialog')]//span[2]"));
    private static final Locator CHANGE_TRAVEL_DATE_MODAL_BODY_CONTAINER = new Locator("CHANGE_TRAVEL_DATE_MODAL_BODY_CONTAINER", By.xpath("//div[contains(@class,'Dialog_dialog')]//div[contains(@class,'Dialog_pop_up')]"));
    private static final Locator MODAL_HEADER_TEXT = new Locator("MODAL_HEADER_TEXT",
            By.xpath("//section[contains(@class, 'Modal_modal_popup')]//header//h2[contains(@class, 'Text_text')]"));
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter CALENDAR_FORMATTER = DateTimeFormatter.ofPattern("d MMMM yyyy EEEE", Locale.ENGLISH);
    private static final Locator TABLE_HEADINGS = new Locator("TABLE_HEADINGS", By.xpath("//table//thead//tr/th[12]"));
    private static final Locator TABLE_COLOUMN = new Locator("TABLE_COLOUMN", By.xpath("//tbody//tr//td[12]"));

    public void setInputOrderId(String id){
        driverTools.waitForElementToBeClickable(INPUT_ORDER_ID);
        driverTools.sendKeys(INPUT_ORDER_ID, id);
    }

    public void clickSearch(){
        driverTools.click(SEARCH_BUTTON);
    }

    public String getOrderId(){
        WebElement orderId=driverTools.waitForElementToAppear(ORDER_ID).orElseThrow();
        return driverTools.getText(orderId);
    }


    public boolean isActionIconLoaded(){
        return driverTools.waitForElementToAppear(FIRST_MORE_VERTICAL_ICON).isPresent();
    }

    public Boolean isOrderDetailTableLoaded() {
        return driverTools.waitForElementsToAppear(ORDER_DETAIL_TABLE).isPresent();
    }

    public void clickActionIcon() {
        driverTools.waitForElementToAppear(FIRST_MORE_VERTICAL_ICON)
                .orElseThrow(() -> new RuntimeException("No vertical more-options menu icons were found on the page!"));

        driverTools.clickWithJavascriptExecutor(FIRST_MORE_VERTICAL_ICON);
        logger.log("Successfully clicked the first vertical menu icon.");
    }

    public void clickRescheduleButton() {
        driverTools.waitForElementToAppear(RESCHEDULE_BUTTON)
                .orElseThrow(() -> new RuntimeException("The 'Cancel' button text element was not found on the page!"));

        driverTools.clickWithJavascriptExecutor(RESCHEDULE_BUTTON);

        logger.log("Successfully clicked the 'Cancel' button.");
    }

    public boolean areColumnValuesNotEmpty(String targetHeadingName) {
        Locator headerLocator = new Locator(targetHeadingName, By.xpath("//table/thead/tr/th"));
        Locator rowsLocator = new Locator("rows", By.xpath("//table/tbody/tr"));

        driverTools.waitForElementToAppear(headerLocator);
        driverTools.waitForElementToAppear(rowsLocator);

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

        List<WebElement> rows = driverTools.getDriver().findElements(rowsLocator.by());
        if (rows.isEmpty()) {
            logger.log("The table body is empty. No rows to validate.");
            return false;
        }

        logger.log("Validating column values for: " + targetHeadingName);
        for (WebElement row : rows) {
            By cellLocator = By.xpath("./td[" + targetColumnIndex + "]");

            List<WebElement> cells = row.findElements(cellLocator);
            if (cells.isEmpty()) {
                logger.log("Validation failed: Cell structure is missing for a row under column index " + targetColumnIndex);
                return false;
            }

            String cellText = cells.get(0).getText();

            logger.log("Cell text: " + cellText);
            if (cellText == null || cellText.isBlank()) {
                logger.log("Validation failed: Found an empty value in column '" + targetHeadingName + "'");
                return false;
            }
        }

        logger.log("Success! All values under column '" + targetHeadingName + "' are non-empty.");
        return true;
    }

    public boolean getTableValueByHeading(String heading) {
             WebElement headings = driverTools.waitForElementToAppear(TABLE_HEADINGS).orElseThrow(() -> new IllegalStateException("No headings found"));
             if(headings.getText().trim().equalsIgnoreCase(heading)){
                return true;
             }
             return false;
    }
    public boolean getTableValueByRow() {
        WebElement row = driverTools.waitForElementToAppear(TABLE_COLOUMN).orElseThrow(() -> new IllegalStateException("No headings found"));
        return driverTools.waitForElementToBeClickable(row).isPresent();
    }

    public boolean checkReschedulePopupIsLoadedAndVerifyHeading(String expectedHeading) {
        if (expectedHeading == null) {
            throw new IllegalArgumentException("Expected heading parameter cannot be null.");
        }

        return driverTools.waitForElementToAppear(MODAL_HEADER_TEXT)
                .map(WebElement::getText)
                .map(actualText -> {
                    String cleanedActual = actualText.trim();
                    logger.log("Modal heading found: '" + cleanedActual + "'. Comparing with: '" + expectedHeading + "'");
                    return cleanedActual.equalsIgnoreCase(expectedHeading.trim());
                })
                .orElse(false);
    }

    public String customFormatDate(LocalDate date){
        return date.format(INPUT_FORMATTER);
    }

    public LocalDate changeTravelDateSelectCheckInDate() {
        WebElement inputElement = driverTools.waitForElementToAppear(CHECKIN_DATE_INPUT_FIELD)
                .orElseThrow(() -> new RuntimeException("Check-in input field not found on page!"));

        String currentText = inputElement.getAttribute("value");
        if (currentText == null || currentText.isBlank()) {
            throw new IllegalStateException("The check-in date input field is empty. Cannot calculate offset.");
        }

        return LocalDate.parse(currentText.trim(), INPUT_FORMATTER);
    }

    public LocalDate changeTravelDateUpdateCheckInDate(int k, LocalDate checkInDate) {
        WebElement inputElement = driverTools.waitForElementToAppear(CHECKIN_DATE_INPUT_FIELD)
                .orElseThrow(() -> new RuntimeException("Check-in input field not found on page!"));

        LocalDate updatedCheckInDate = checkInDate.plusDays(k);

        driverTools.clickWithJavascriptExecutor(CHECKIN_DATE_INPUT_FIELD);

        Locator loc = getDateLocator(updatedCheckInDate);

        int i = 0;
        while (i++ < 24 && driverTools.waitForElementToAppear(loc).isEmpty()) {

            Locator NEXT_MONTH_BTN = new Locator("NEXT_MONTH_BTN", By.xpath("//button[@type='button' and contains(@aria-label,'Next month')]"));
            driverTools.waitForElementToAppear(NEXT_MONTH_BTN)
                    .orElseThrow(() -> new RuntimeException("Calendar next month navigation button not found!"));

            driverTools.clickWithJavascriptExecutor(NEXT_MONTH_BTN);
        }

        driverTools.clickWithJavascriptExecutor(getDateLocator(updatedCheckInDate));
        return updatedCheckInDate;
    }

    private Locator getDateLocator(LocalDate date) {
        String locatorTextEng = date.format(CALENDAR_FORMATTER);

        String monthEng = date.format(DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH));
        String monthId = CoreFunctions.monthEngToId(monthEng);
        String locatorTextId = locatorTextEng.replace(monthEng, monthId);

        return new Locator(locatorTextEng, By.xpath(
                "//*[contains(@aria-label, '" + locatorTextEng + "') or contains(@aria-label, '" + locatorTextId + "')]"));
    }

    public LocalDate changeTravelDateSelectCheckOutDate() {
        WebElement inputElement = driverTools.waitForElementToAppear(CHECKOUT_DATE_INPUT_FIELD)
                .orElseThrow(() -> new RuntimeException("Check-out input field not found on page!"));

        String currentText = inputElement.getAttribute("value");
        if (currentText == null || currentText.isBlank()) {
            throw new IllegalStateException("The check-out date input field is empty. Cannot calculate offset.");
        }

        return LocalDate.parse(currentText.trim(), INPUT_FORMATTER);
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
                By.xpath("//div//span[contains(@class,'Check_custom_checkbox')]"));

        return driverTools.waitForElementToBeClickable(CHECKBOX_LABEL_LOCATOR).isPresent();
    }

    public void clickChangeTravelDateCheckBox() {
        Locator CHECKBOX_LOCATOR = new Locator("CHECKBOX_LABEL_LOCATOR",
                By.xpath("//div//span[contains(@class,'Check_custom_checkbox')]"));

        driverTools.waitForElementToAppear(CHECKBOX_LOCATOR).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CHECKBOX_LOCATOR);
    }

    public boolean clickChangeTravelDateSubmitButton(){
        Locator SUBMIT_BTN = new Locator("SUBMIT_BTN",By.xpath("//section[contains(@class, 'Modal_modal_popup')]//button[@type='submit' and contains(@class,'Button_button')]"));
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

    public void clickPopupSubmitButtonWhenChangeTravelDate() {
        Locator CHANGE_TRAVEL_DATE_POPUP_OK_BTN = new Locator("CHANGE_TRAVEL_DATE_POPUP_OK_BTN", By.xpath("//div[contains(@class,'Dialog')]/div[contains(@class,'Dialog')]//button[2]"));
        driverTools.waitForElementToBeClickable(CHANGE_TRAVEL_DATE_POPUP_OK_BTN)
                .orElseThrow(() -> new RuntimeException("POPUP OK button was not clickable!"));

        driverTools.clickWithJavascriptExecutor(CHANGE_TRAVEL_DATE_POPUP_OK_BTN);
        logger.log("Successfully clicked POPUP OK button.");
    }

    public boolean isPopupCancelButtonClickableWhenChangeTravelDate() {
        Locator CHANGE_TRAVEL_DATE_POPUP_CLOSE_BTN = new Locator("CHANGE_TRAVEL_DATE_POPUP_CLOSE_BTN", By.xpath("//div[contains(@class,'Dialog')]/div[contains(@class,'Dialog')]//button[1]"));
        return driverTools.waitForElementToBeClickable(CHANGE_TRAVEL_DATE_POPUP_CLOSE_BTN).isPresent();
    }

    public boolean isPopupSubmitButtonClickableWhenChangeTravelDate() {
        Locator CHANGE_TRAVEL_DATE_POPUP_OK_BTN = new Locator("CHANGE_TRAVEL_DATE_POPUP_OK_BTN", By.xpath("//div[contains(@class,'Dialog')]/div[contains(@class,'Dialog')]//button[2]"));
        return driverTools.waitForElementToBeClickable(CHANGE_TRAVEL_DATE_POPUP_OK_BTN).isPresent();
    }
}
