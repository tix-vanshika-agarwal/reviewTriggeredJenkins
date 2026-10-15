package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.*;

import java.util.*;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.*;

public class BulkUpdatePage <T extends WebDriver> extends BasePage<T> {

    public BulkUpdatePage(Session<T> session) {
        super(session);
    }

    private static final Locator BULK_UPDATE_PAGE = new Locator("BULK_UPDATE_PAGE", new By.ByXPath("//label[text()='Bulk Updates']"));
    private static final Locator START_DATE_INPUT = new Locator("START_DATE_INPUT", new By.ByXPath("(//div[@class=\"calendar-section\"]//div[@class=\"the-input-text\"])[1]"));
    private static final Locator PRICE_TAB = new Locator("PRICE_TAB", new By.ByXPath("(//div[contains(@class,\"the-tab blackout-tab\")])[1]"));
    private static final Locator ALLOTMENT_TAB = new Locator("ALLOTMENT_TAB", new By.ByXPath("//div[text()='Allotment']"));
    private static final Locator CLOSE_OUT_TAB = new Locator("CLOSE_OUT_TAB", new By.ByXPath("//div[text()='Close Out']"));
    private static final Locator SAVE_CHANGES_BUTTON = new Locator("SAVE_CHANGES_BUTTON", new By.ByXPath("//button[contains(@class, 'yellow btn-save')]"));
    private static final Locator SAVE_CHANGES_CONFIRMATION_BUTTON = new Locator("SAVE_CHANGES_CONFIRMATION_BUTTON", new By.ByXPath("(//button[contains(@class, 'btn-save')])[2]"));
    private static final Locator ROOM_TYPE_INPUT = new Locator("ROOM_TYPE_INPUT", new By.ByXPath("//input[contains(@class, 'dropdown-input-room')]"));
    private static final Locator SELECT_ALL_ROOM_TYPE_CHECKBOX = new Locator("SELECT_ALL_ROOM_TYPE_CHECKBOX", new By.ByXPath("//div[@class='dropdown-checkbox-all']"));
    private static final Locator APPLY_ROOM_TYPE_BUTTON = new Locator("APPLY_ROOM_TYPE_BUTTON", new By.ByXPath("//button[contains(@class, 'btn-apply')]"));
    private static final Locator UPDATE_PREVIEW_MODAL = new Locator("UPDATE_PREVIEW_MODAL", new By.ByXPath("//div[@class='the-modal update-preview-modal show']"));
    private static final Locator UPDATE_PREVIEW_MODAL_OK_BUTTON = new Locator("UPDATE_PREVIEW_MODAL_OK_BUTTON", new By.ByXPath("//div[@class='the-modal update-preview-modal show']//button"));
    private static final Locator SAVE_CHANGES_ALLOTMENT_MODAL = new Locator("SAVE_CHANGES_ALLOTMENT_MODAL", new By.ByXPath("//div[@data-testid='the-modal' and @class='the-modal show']//div[@class='the-modal-content ']"));
    private static final Locator SELECTED_WEEK_DAYS_CHECKBOXES = new Locator("SELECTED_WEEK_DAYS_CHECKBOXES", By.xpath("//div[contains(@class,'calendar')]//following-sibling::div[contains(@class,'checkbox')]//span[contains(@class,'checkbox') and not(contains(@class,'disabled'))]"));
    private static final Locator DROPDOWN_OPTIONS = new Locator("DROPDOWN_OPTIONS", By.xpath("//div[contains(@class,'dropdown')]//label[contains(@class,'checkbox')]"));

    private static final Locator CANCELLATION_POLICY_TAB = new Locator("CANCELLATION_POLICY_TAB", new By.ByXPath("//div[text()='Cancellation Policy' or text()='Kebijakan Pembatalan']"));
    private static final Locator ROOM_TYPE_DROPDOWN = new Locator("ROOM_TYPE_DROPDOWN", new By.ByXPath("//input[@id='select-room-types' or @id='pilih-tipe-kamar']"));
    private static final Locator FIRST_ROOM_OPTION = new Locator("FIRST_ROOM_OPTION", new By.ByXPath("(//div[contains(@class, 'room-and-rateplan--dropdown-item')][not(contains(., 'Select all')) and not(contains(., 'Pilih semua'))]//div[contains(@class, 'tc-checkbox')])[1]"));
    private static final Locator SAVE_ROOM_BTN = new Locator("SAVE_ROOM_BTN", new By.ByXPath("//button[@data-testid='save-room-options']"));
    private static final Locator FIRST_RATE_PLAN_OPTION = new Locator("FIRST_RATE_PLAN_OPTION", new By.ByXPath("(//div[contains(@class, 'rate-plan-group--content-item')]//div[contains(@class, 'tc-checkbox')])[1]"));
    private static final Locator NON_REFUNDABLE_RADIO = new Locator("NON_REFUNDABLE_RADIO", new By.ByXPath("//div[contains(@class, 'cancellation-policy-type--radio') and .//span[contains(normalize-space(.), 'Non-refundable') or contains(normalize-space(.), 'Tidak bisa refund')]]//div[contains(@class, 'tc-checkbox')]"));
    private static final Locator SAVE_BTN = new Locator("SAVE_RATE_PLAN_BTN", new By.ByXPath("//button[@data-testid='submit-button']"));
    private static final Locator BULK_UPDATE_SAVE_DIALOG_SUBJECT = new Locator("BULK_UPDATE_SAVE_DIALOG_SUBJECT", By.xpath("//div[contains(@class, 'dialog-popup')]//h3[contains(@class, 'figure-title')] | //div[contains(@class, 'dialog-popup')]//div[@class='footer']/button"));
    private static final Locator BULK_UPDATE_SAVE_BTN = new Locator("BULK_UPDATE_SAVE_BTN", By.xpath("//div[contains(@class, 'dialog-popup')]//div[@class='footer']/button[2]"));
    private static final Locator CANCELLATION_MODAL_TITLE = new Locator("CANCELLATION_MODAL_TITLE", new By.ByXPath("//h3[contains(@class, 'figure-title') and (contains(normalize-space(.), 'Save the cancellation policy bulk update?') or contains(normalize-space(.), 'Simpan bulk update kebijakan pembatalan?'))]"));
    private static final Locator CANCELLATION_MODAL_DESC = new Locator("CANCELLATION_MODAL_DESC", new By.ByXPath("//p[contains(@class, 'figure-description')]"));
    private static final Locator SUMMARY_MODAL_CONTAINER = new Locator("SUMMARY_MODAL_CONTAINER", new By.ByXPath("//div[contains(@class, 'summary-modal')]"));
    private static final Locator SUMMARY_PERIOD_TEXT = new Locator("SUMMARY_PERIOD_TEXT", new By.ByXPath("//div[contains(@class, 'summary-detail') and (contains(., 'Period:') or contains(., 'Periode:'))]//span[contains(@class, 'bold')]"));
    private static final Locator SUMMARY_POLICY_TEXT = new Locator("SUMMARY_POLICY_TEXT", new By.ByXPath("//div[contains(@class, 'summary-detail') and (contains(., 'Cancellation Policy:') or contains(., 'Kebijakan Pembatalan:'))]//span[contains(@class, 'bold')]"));
    private static final Locator SUMMARY_ROOM_TEXT = new Locator("SUMMARY_ROOM_TEXT", new By.ByXPath("//div[contains(@class, 'summary-card--item')]//span"));
    private static final Locator SUMMARY_RATE_PLAN_TEXT = new Locator("SUMMARY_RATE_PLAN_TEXT", new By.ByXPath("//div[contains(@class, 'summary-card--item')]//small"));
    private static final Locator SUMMARY_OK_BTN = new Locator("SUMMARY_OK_BTN", new By.ByXPath("//button[@data-testid='summary-modal-ok']"));
    private static final Locator SELECTED_ROOM_TITLE_TEXT = new Locator("SELECTED_ROOM_TITLE_TEXT", new By.ByXPath("(//div[contains(@class, 'rate-plan-group--header')]/span)[1]"));
    private static final Locator FIRST_RATE_PLAN_TEXT = new Locator("FIRST_RATE_PLAN_TEXT", new By.ByXPath("(//div[contains(@class, 'rate-plan-group--content-item')])[1]/span[contains(@class, 'tix-font')]"));
    private static final Locator CUSTOM_POLICY_RADIO = new Locator("CUSTOM_POLICY_RADIO", new By.ByXPath("//div[contains(@class, 'cancellation-policy-type--radio-group')]/div[2]//label"));
    private static final Locator REFUNDABLE_DAYS_ERROR = new Locator("REFUNDABLE_DAYS_ERROR", new By.ByXPath("//div[@id='refundable-days']//div[contains(@class, 'error-message')]"));
    private static final Locator CANCELLATION_PCT_ERROR = new Locator("CANCELLATION_PCT_ERROR", new By.ByXPath("//div[@id='cancellationPercentage']//div[contains(@class, 'error-message')]"));
    private static final Locator NO_SHOW_PCT_ERROR = new Locator("NO_SHOW_PCT_ERROR", new By.ByXPath("//div[@id='noShowPercentage']//div[contains(@class, 'error-message')]"));
    private static final Locator REFUNDABLE_DAYS_INPUT = new Locator("REFUNDABLE_DAYS_INPUT", new By.ByXPath("//input[@data-testid='refundable-days']"));
    private static final Locator CANCELLATION_PCT_INPUT = new Locator("CANCELLATION_PCT_INPUT", new By.ByXPath("//input[@data-testid='cancellation-percentage']"));
    private static final Locator NO_SHOW_PCT_INPUT = new Locator("NO_SHOW_PCT_INPUT", new By.ByXPath("//input[@data-testid='no-show-percentage']"));
    private static final Locator CANCEL_NIGHT_RADIO = new Locator("CANCEL_NIGHT_RADIO", new By.ByXPath("//input[@data-testid='radio-cancellation-night']/ancestor::div[contains(@class, 'tc-checkbox')]"));
    private static final Locator NO_SHOW_NIGHT_RADIO = new Locator("NO_SHOW_NIGHT_RADIO", new By.ByXPath("//input[@data-testid='radio-no-show-night']/ancestor::div[contains(@class, 'tc-checkbox')]"));
    private static final Locator CANCEL_NIGHT_DROPDOWN = new Locator("CANCEL_NIGHT_DROPDOWN", new By.ByXPath("//input[@data-testid='cancellation-night']"));
    private static final Locator NO_SHOW_NIGHT_DROPDOWN = new Locator("NO_SHOW_NIGHT_DROPDOWN", new By.ByXPath("//input[@data-testid='no-show-night']"));
    private static final Locator CANCEL_OPT_1 = new Locator("CANCEL_OPT_1", new By.ByXPath("(//div[@data-testid='cancel-percent-night-wrap']//span[contains(@class, 'popoverlist-list')]/label)[1]"));
    private static final Locator CANCEL_OPT_2 = new Locator("CANCEL_OPT_2", new By.ByXPath("(//div[@data-testid='cancel-percent-night-wrap']//span[contains(@class, 'popoverlist-list')]/label)[2]"));
    private static final Locator NO_SHOW_OPT_1 = new Locator("NO_SHOW_OPT_1", new By.ByXPath("(//div[@data-testid='noshow-percent-night-wrap']//span[contains(@class, 'popoverlist-list')]/label)[1]"));
    private static final Locator NO_SHOW_OPT_2 = new Locator("NO_SHOW_OPT_2", new By.ByXPath("(//div[@data-testid='noshow-percent-night-wrap']//span[contains(@class, 'popoverlist-list')]/label)[2]"));
    private static final Locator CANCEL_PERCENT_RADIO = new Locator("CANCEL_PERCENT_RADIO", new By.ByXPath("//input[@data-testid='radio-cancellation-percentage']/ancestor::div[contains(@class, 'tc-checkbox')]"));
    private static final Locator NO_SHOW_PERCENT_RADIO = new Locator("NO_SHOW_PERCENT_RADIO", new By.ByXPath("//input[@data-testid='radio-no-show-percentage']/ancestor::div[contains(@class, 'tc-checkbox')]"));
    private static final Locator RESTRICTIONS_TAB = new Locator("RESTRICTIONS_TAB", new By.ByXPath("//div[text()='Restrictions']"));
    private static final Locator LOS_TOGGLE_INPUT = new Locator("LOS_TOGGLE_INPUT", new By.ByXPath("//input[@data-testid='Durasi menginap' or @data-testid='Length of stay']"));
    private static final Locator ADV_BOOKING_TOGGLE_INPUT = new Locator("ADV_BOOKING_TOGGLE_INPUT", new By.ByXPath("//input[@data-testid='Advance Booking']"));
    private static final Locator CTA_TOGGLE_INPUT = new Locator("CTA_TOGGLE_INPUT", new By.ByXPath("//input[@data-testid='Closed to arrival']"));
    private static final Locator CTD_TOGGLE_INPUT = new Locator("CTD_TOGGLE_INPUT", new By.ByXPath("//input[@data-testid='Closed to departure']"));
    private static final Locator LOS_TOGGLE_BTN = new Locator("LOS_TOGGLE_BTN", new By.ByXPath("//input[@data-testid='Durasi menginap' or @data-testid='Length of stay']/following-sibling::span"));
    private static final Locator ADV_BOOKING_TOGGLE_BTN = new Locator("ADV_BOOKING_TOGGLE_BTN", new By.ByXPath("//input[@data-testid='Advance Booking']/following-sibling::span"));
    private static final Locator CTA_TOGGLE_BTN = new Locator("CTA_TOGGLE_BTN", new By.ByXPath("//input[@data-testid='Closed to arrival']/following-sibling::span"));
    private static final Locator CTD_TOGGLE_BTN = new Locator("CTD_TOGGLE_BTN", new By.ByXPath("//input[@data-testid='Closed to departure']/following-sibling::span"));
    private static final Locator LOS_MIN_INPUT = new Locator("LOS_MIN_INPUT", new By.ByXPath("//input[@data-testid='min-daysStay']"));
    private static final Locator LOS_MAX_INPUT = new Locator("LOS_MAX_INPUT", new By.ByXPath("//input[@data-testid='max-daysStay']"));
    private static final Locator ADV_MIN_INPUT = new Locator("ADV_MIN_INPUT", new By.ByXPath("//input[@data-testid='min-daysBooking']"));
    private static final Locator ADV_MAX_INPUT = new Locator("ADV_MAX_INPUT", new By.ByXPath("//input[@data-testid='max-daysBooking']"));
    private static final Locator LOS_MIN_ERROR = new Locator("LOS_MIN_ERROR", new By.ByXPath("//input[@data-testid='min-daysStay']/ancestor::div[contains(@class, 'input-forms')]//p[contains(@class, 'danger') and contains(@class, 'description')]"));
    private static final Locator LOS_MAX_ERROR = new Locator("LOS_MAX_ERROR", new By.ByXPath("//input[@data-testid='max-daysStay']/ancestor::div[contains(@class, 'input-forms')]//p[contains(@class, 'danger') and contains(@class, 'description')]"));
    private static final Locator ADV_MIN_ERROR = new Locator("ADV_MIN_ERROR", new By.ByXPath("//input[@data-testid='min-daysBooking']/ancestor::div[contains(@class, 'input-forms')]//p[contains(@class, 'danger') and contains(@class, 'description')]"));
    private static final Locator ADV_MAX_ERROR = new Locator("ADV_MAX_ERROR", new By.ByXPath("//input[@data-testid='max-daysBooking']/ancestor::div[contains(@class, 'input-forms')]//p[contains(@class, 'danger') and contains(@class, 'description')]"));
    private static final Locator RESTRICTION_MODAL_TITLE = new Locator("RESTRICTION_MODAL_TITLE", new By.ByXPath("//h3[contains(@class, 'figure-title')]"));
    private static final Locator RESTRICTION_MODAL_DESC = new Locator("RESTRICTION_MODAL_DESC", new By.ByXPath("//p[contains(@class, 'figure-description')]"));
    private static final Locator RESTRICTION_MODAL_YES_BTN = new Locator("RESTRICTION_MODAL_YES_BTN", new By.ByXPath("//div[contains(@class, 'footer')]//button[contains(normalize-space(.), 'Yes, save') or contains(normalize-space(.), 'Ya, simpan')]"));
    private static final Locator SUMMARY_RESTRICTION_TEXT = new Locator("SUMMARY_RESTRICTION_TEXT", new By.ByXPath("//div[contains(@class, 'summary-detail') and contains((.), 'Restrictions:')]"));
    private static final Locator CANCELLATION_FEE_AMOUNT_RADIO = new Locator("CANCELLATION_FEE_AMOUNT_RADIO", By.xpath("//div[contains(@class, 'tix-radio') and .//input[@data-testid='radio-cancellation-amount']]//label"));
    private static final Locator NO_SHOW_AMOUNT_RADIO = new Locator("NO_SHOW_AMOUNT_RADIO", By.xpath("//div[contains(@class, 'tix-radio') and .//input[@data-testid='radio-no-show-amount']]//label"));
    private static final Locator CANCELLATION_AMOUNT_INPUT = new Locator("CANCELLATION_AMOUNT_INPUT", By.xpath("//input[@name='cancellationAmount']"));
    private static final Locator NO_SHOW_AMOUNT_INPUT = new Locator("NO_SHOW_AMOUNT_INPUT", By.xpath("//input[@name='noShowAmount']"));
    private static final Locator CANCELLATION_AMOUNT_ERROR = new Locator("CANCELLATION_AMOUNT_ERROR", By.xpath("//div[@id='cancellationAmount']//div[contains(@class, 'error-message')]"));
    private static final Locator NO_SHOW_AMOUNT_ERROR = new Locator("NO_SHOW_AMOUNT_ERROR", By.xpath("//div[@id='noShowAmount']//div[contains(@class, 'error-message')]"));
    private static final Locator SUMMARY_CANCELLATION_POLICY_TEXT = new Locator("SUMMARY_CANCELLATION_POLICY_TEXT", By.xpath("//div[contains(@class, 'summary-cancellation-policy')]//ol"));
    private static final Locator AMOUNT_INFO_ICON = new Locator("AMOUNT_INFO_ICON", By.xpath("//div[contains(@class, 'tix-radio') and .//input[contains(@data-testid, 'amount')]]//*[local-name()='svg' and @fill='#D8DCE8']"));
    private Locator getNextMonthDayLocator(int day) {
        return new Locator("NEXT_MONTH_DAY_" + day, new By.ByXPath("(//div[@class='DayPicker-Month'])[2]//div[contains(@class, 'DayPicker-Day') and not(contains(@class, '--disabled')) and not(contains(@class, '--outside'))]//div[normalize-space(text())='" + day + "']/ancestor::div[contains(@class, 'DayPicker-Day')]"));
    }
    private static Locator getCalendarDateByIndex(int index) {
        return new Locator("CALENDAR_DATE", new By.ByXPath(String.format("(//div[contains(@class,\"DayPicker-Day\")][@aria-disabled=\"false\"])[%d]", index)));
    }

    private static Locator getRoomListCheckboxByName(String name) {
        return new Locator("ROOM_LIST_CHECKBOX", new By.ByXPath(String.format("//label[text()='%s']/span", name)));
    }

    private static Locator getRatePlanAllotmentInputByRoomName(String name) {
        return new Locator("RATE_PLAN_INPUT", new By.ByXPath(String.format("//span[contains(@class,\"allotment text\")][text()='%s']//ancestor::div[@class=\"allotment-container\"]//*[@class=\"allotment\"]//input", name)));
    }

    private static Locator getRatePlanRateInputByRoomName(String name) {
        return new Locator("RATE_PLAN_INPUT", new By.ByXPath(String.format("//span[contains(@class,\"room title\")][text()='%s']//ancestor::div[@class=\"room-container\"]//div[@class=\"room\"]//input", name)));
    }

    private static Locator getRatePlanCloseOutInputByRoomName(String name) {
        return new Locator("RATE_PLAN_INPUT", new By.ByXPath(String.format("//span[contains(@class,\"allotment title\")][text()='%s']//ancestor::div[@class=\"allotment-container\"]//span[@class=\"allotment rateplan-list\"]//input", name)));
    }

    private static Locator getRatePlanDropdownList(String name) {
        return new Locator("RATE_PLAN_DROPDOWN_LIST", new By.ByXPath(String.format("//label[text()='%s']/parent::span[contains(@class,'popoverlist-list')]", name)));
    }
    private static Locator getRatePlanDropdownListFirstOption(String roomName) {
        return new Locator("RATE_PLAN_DROPDOWN_LIST_FIRST_OPTION", new By.ByXPath(String.format("(//span[contains(@class,'room title')][text()='%s']//ancestor::div[@class='room-container']//span[contains(@class,'popoverlist-list')])[1]", roomName)));
    }
    private static Locator getRatePlanDropdownList(String roomName, String ratePlanName) {
        return new Locator("RATE_PLAN_DROPDOWN_LIST", new By.ByXPath(String.format("//span[contains(@class,\"room title\")][text()='%s']//ancestor::div[@class=\"room-container\"]//label[text()='%s']/parent::span[contains(@class,'popoverlist-list')]", roomName, ratePlanName)));
    }
    private static Locator getRatePlanErrorMessageLocator(String roomName) {
        return new Locator("RATE_PLAN_ERROR_MESSAGE", By.xpath("//input[contains(@name,'%s')]//following-sibling::div[contains(@class,'error')]".formatted(roomName)));
    }

    private static Locator getAllotmentInputByRoomName(String name) {
        return new Locator("ALLOTMENT_INPUT", new By.ByXPath(String.format("//span[contains(@class,\"allotment text\")][text()='%s']//ancestor::div[@class=\"allotment-container\"]//*[@class=\"allotment\"]//input[contains(@name,\"roomAllotment\")]", name)));
    }

    private static Locator getRateInputByRoomName(String name) {
        return new Locator("RATE_INPUT", new By.ByXPath("//input[contains(@name,'%s')]".formatted(name)));
    }

    private static Locator getOpenRadioByRoomName(String name) {
        return new Locator("OPEN_RADIO_BUTTON", new By.ByXPath(String.format("(//span[contains(@class,\"allotment title\")][text()='%s']//ancestor::div[@class=\"allotment-container\"]//span[@class=\"allotment\"]//span[contains(@class,\"radio-style\")])[1]", name)));
    }

    private static Locator getCloseOutRadioByRoomName(String name) {
        return new Locator("CLOSE_OUT_RADIO_BUTTON", new By.ByXPath(String.format("(//span[contains(@class,\"allotment title\")][text()='%s']//ancestor::div[@class=\"allotment-container\"]//span[@class=\"allotment\"]//span[contains(@class,\"radio-style\")])[2]", name)));
    }

    public boolean isBulkUpdatePageLoaded(){
        driverTools.waitForElementToAppear(BULK_UPDATE_PAGE);
        return driverTools.isElementDisplayed(BULK_UPDATE_PAGE);
    }

    public boolean isUpdatePreviewModalAppeared(){
        driverTools.waitForElementToAppear(UPDATE_PREVIEW_MODAL);
        return driverTools.isElementDisplayed(UPDATE_PREVIEW_MODAL);
    }
    public boolean isUpdatePreviewModalDisappeared(){
        return driverTools.waitForElementToDisappear(UPDATE_PREVIEW_MODAL);
    }

    public void clickUpdatePreviewModalOKButton(){
        driverTools.waitForElementToAppear(UPDATE_PREVIEW_MODAL_OK_BUTTON);
        driverTools.click(UPDATE_PREVIEW_MODAL_OK_BUTTON);
    }

    public void clickPriceTab() {
        WebElement tab = driverTools.waitForElementToBeClickable(PRICE_TAB).orElseThrow();
        driverTools.click(tab);
    }

    public void clickAllotmentTab(){
        driverTools.waitForElementToAppear(ALLOTMENT_TAB);
        driverTools.click(ALLOTMENT_TAB);
    }

    public void clickCloseOutTab(){
        driverTools.waitForElementToAppear(CLOSE_OUT_TAB);
        driverTools.click(CLOSE_OUT_TAB);
    }

    public void clickRoomTypeInput(){
        driverTools.waitForElementToAppear(ROOM_TYPE_INPUT);
        driverTools.click(ROOM_TYPE_INPUT);
    }

    public void clickSelectAllRoomTypeCheckbox(){
        driverTools.waitForElementToAppear(SELECT_ALL_ROOM_TYPE_CHECKBOX);
        driverTools.click(SELECT_ALL_ROOM_TYPE_CHECKBOX);
    }

    public void clickApplyRoomTypeButton(){
        driverTools.waitForElementToAppear(APPLY_ROOM_TYPE_BUTTON);
        driverTools.click(APPLY_ROOM_TYPE_BUTTON);
    }

    public void clickSaveChangesButton(){
        driverTools.waitForElementToAppear(SAVE_CHANGES_BUTTON);
        driverTools.click(SAVE_CHANGES_BUTTON);
    }

    public void clickSaveChangesConfirmationButton(){
        driverTools.waitForElementToAppear(SAVE_CHANGES_CONFIRMATION_BUTTON);
        driverTools.click(SAVE_CHANGES_CONFIRMATION_BUTTON);
    }

    public WebElement getRatePlanType(int num){
        return driver.findElement(By.xpath(String.format("((//div[@class='the-dropdown'])[%d]//span[contains(@class, 'with-label')])[1]", num+1)));
    }

    public void fillOutAllRatePlanNameInput(String price){
        List<WebElement> RATE_PLAN_TYPE_DROPDOWNS = driver.findElements(By.xpath("//div[@class='the-dropdown']"));
        List<WebElement> RATE_PLAN_INPUT_ELEMENTS = driver.findElements(By.xpath("//input[contains(@class, 'input-bulkupdate')]"));
        for (int i = 0; i < RATE_PLAN_INPUT_ELEMENTS.size(); i++) {
            driverTools.waitForElementToAppear(RATE_PLAN_TYPE_DROPDOWNS.get(i));
            driverTools.click(RATE_PLAN_TYPE_DROPDOWNS.get(i));
            
            WebElement ratePlanType = getRatePlanType(i);
            driverTools.waitForElementToAppear(ratePlanType);
            driverTools.click(ratePlanType);
            
            driverTools.waitForElementToAppear(RATE_PLAN_INPUT_ELEMENTS.get(i));
            driverTools.sendKeys(RATE_PLAN_INPUT_ELEMENTS.get(i), price);
        }
    }

    public void fillOutAllAllotmentInputs(String allotment){
        List<WebElement> ROOM_ALLOTMENT_INPUT_ELEMENTS = driver.findElements(By.xpath("//input[contains(@name, 'roomAllotment')]"));
        for (WebElement inputElement: ROOM_ALLOTMENT_INPUT_ELEMENTS) {
            System.out.println("inp" + inputElement.toString());
            driverTools.waitForElementToAppear(inputElement);
            driverTools.clearInput(inputElement);
            driverTools.sendKeys(inputElement, allotment);
        }
    }

    public void selectRoomName(String name) {
        WebElement input = driverTools.waitForElementToBeClickable(ROOM_TYPE_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement checkbox = driverTools.waitForElementToBeClickable(getRoomListCheckboxByName(name)).orElseThrow();
        driverTools.click(checkbox);
        WebElement apply = driverTools.waitForElementToBeClickable(APPLY_ROOM_TYPE_BUTTON).orElseThrow();
        driverTools.click(apply);
    }

    public void enterAllotmentByRoomNameAndRatePlan(String roomName, String ratePlanName, String allotment) {
        WebElement input = driverTools.waitForElementToAppear(getAllotmentInputByRoomName(roomName)).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, allotment);
    }

    public void enterRateByRoomNameAndRatePlan(String roomName, String ratePlanName, String allotment) {
        WebElement ratePlanInput = driverTools.waitForElementToBeClickable(getRatePlanRateInputByRoomName(roomName)).orElseThrow();
        driverTools.click(ratePlanInput);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getRatePlanDropdownList(ratePlanName)).orElseThrow();
        driverTools.click(dropdown);
        WebElement input = driverTools.waitForElementToAppear(getRateInputByRoomName(roomName)).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, allotment);
    }

    public void selectCloseOutStatusByRoomName(String roomName, boolean isCloseOut) {
        WebElement radio = null;
        if (isCloseOut) {
            radio = driverTools.waitForElementToBeClickable(getCloseOutRadioByRoomName(roomName)).orElseThrow();
        } else {
            radio = driverTools.waitForElementToBeClickable(getOpenRadioByRoomName(roomName)).orElseThrow();
        }
        driverTools.click(radio);
    }

    public void clickAllCloseOutRadioButton(){
        List<WebElement> RADIO_BUTTONS = driver.findElements(By.xpath("//label[text()='Close Out' and @class='radio-container ']"));
        for (WebElement radioButton: RADIO_BUTTONS) {           
            driverTools.waitForElementToAppear(radioButton);
            driverTools.click(radioButton);
        }
    }

    public void selectDateByIndex(int startIndex, int endIndex) {
        selectDateRangeByIndex(startIndex, endIndex);
    }

    public String selectDateRangeByIndex(int startIndex, int endIndex) {
        WebElement input = driverTools.waitForElementToBeClickable(START_DATE_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement start = driverTools.waitForElementToBeClickable(getCalendarDateByIndex(startIndex)).orElseThrow();
        String startDate = start.getAttribute("aria-label");
        driverTools.click(start);
        WebElement end = driverTools.waitForElementToBeClickable(getCalendarDateByIndex(endIndex)).orElseThrow();
        String endDate = end.getAttribute("aria-label");
        driverTools.click(end);
        return startDate + " - " + endDate;
    }

    public String formatCalendarDateWithLang(String dateRange) {
        if (dateRange == null || dateRange.isBlank()) {
            throw new IllegalArgumentException("Date range parameter cannot be null or empty.");
        }

        String[] parts = dateRange.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid date range format. Expected a hyphen separator.");
        }

        // Calendar aria-label is always English; reorder tokens to the modal's "dd MMM yyyy" format.
        return "%s - %s".formatted(reorderCalendarDate(parts[0].trim()), reorderCalendarDate(parts[1].trim()));
    }

    // Converts calendar aria-label "EEE MMM dd yyyy" to modal format "dd MMM yyyy".
    private String reorderCalendarDate(String rawDate) {
        String[] token = rawDate.split("\\s+");
        if (token.length != 4) {
            throw new IllegalArgumentException("Unexpected calendar date format: '" + rawDate + "'");
        }
        return "%s %s %s".formatted(token[2], token[1], token[3]); // day month year
    }

    // English -> Bahasa (moment.js "id") month abbreviations; only these 4 differ, the rest are identical.
    private static final Map<String, String> EN_TO_ID_MONTH = Map.of("May", "Mei", "Aug", "Agt", "Oct", "Okt", "Dec", "Des");

    // The modal renders in the UI language, so build the Bahasa variant of an English "dd MMM yyyy" range.
    public String toBahasaDateRange(String englishDateRange) {
        String bahasaDateRange = englishDateRange;
        for (Map.Entry<String, String> month : EN_TO_ID_MONTH.entrySet()) {
            bahasaDateRange = bahasaDateRange.replaceAll("\\b" + month.getKey() + "\\b", month.getValue());
        }
        return bahasaDateRange;
    }

    public List<String> getAllotmentInputsErrors() {
        List<WebElement> ROOM_ALLOTMENT_ERROR_ELEMENTS = driver.findElements(By.xpath("//span[contains(@class,'allotment')]//div[contains(@class, 'error-message')]"));
        List<String> allotmentErrorMessages = new ArrayList<>();
        for (WebElement errorElement: ROOM_ALLOTMENT_ERROR_ELEMENTS) {
            driverTools.waitForElementToAppear(errorElement);
            String errorText = errorElement.getText();
            logger.log("ErrorText got: " + errorText);
            allotmentErrorMessages.add(errorText);
        }
        return allotmentErrorMessages;
    }

    public boolean isAllotmentChangesSaveChangesModalAppears() {
        return driverTools.waitForElementsToAppear(SAVE_CHANGES_ALLOTMENT_MODAL).isPresent();
    }

    public String allotmentChangesSaveChangesModalTitle() {
        Locator MODAL_TITLE_LOC = new Locator("MODAL_TITLE_LOC", By.xpath("//div[@data-testid='the-modal' and @class='the-modal show']//div[@class='the-modal-content ']//p[@class='title']"));
        driverTools.waitForElementToAppear(MODAL_TITLE_LOC);
        WebElement modalTitle = driver.findElement(MODAL_TITLE_LOC.by());
        return modalTitle.getText();
    }

    public String allotmentChangesSaveChangesModalDesc() {
        Locator MODAL_DESC_LOC = new Locator("MODAL_DESC_LOC", By.xpath("//div[@data-testid='the-modal' and @class='the-modal show']//div[@class='the-modal-content ']//p[@class='desc']"));
        driverTools.waitForElementToAppear(MODAL_DESC_LOC);
        WebElement modalDesc = driver.findElement(MODAL_DESC_LOC.by());
        return modalDesc.getText();
    }

    public boolean isSaveChangesButtonClickable() {
        return driverTools.waitForElementToBeClickable(SAVE_CHANGES_BUTTON).isPresent();
    }

    public String getUpdatePreviewModalDateRange() {
        Locator UPDATE_PREVIEW_MODAL_DATE_RANGE = new Locator("UPDATE_PREVIEW_MODAL_DATE_RANGE",
                By.xpath("//div[@class='the-modal update-preview-modal show']//span[@class='date-range']"));
        driverTools.waitForElementToAppear(UPDATE_PREVIEW_MODAL_DATE_RANGE);
        return driverTools.getText(UPDATE_PREVIEW_MODAL_DATE_RANGE).orElseThrow(() -> new IllegalStateException("The date range attribute is missing or empty!"));
    }

    public List<String> getAllAllotmentInputFields() {
        List<WebElement> ROOM_ALLOTMENT_INPUT_ELEMENTS = driver.findElements(By.xpath("//input[contains(@name, 'roomAllotment')]"));
        List<String> allAllotmentInputFields = new ArrayList<>();

        for (int i = 0; i < ROOM_ALLOTMENT_INPUT_ELEMENTS.size(); i++) {
            Locator ELEMENT_LOCATOR = new Locator("ELEMENT_LOCATOR",By.xpath("(//div[contains(@class,'allotment')]//span[contains(@class, 'allotment')][1])[%d]".formatted(i+2)));
            driverTools.waitForElementToAppear(ELEMENT_LOCATOR);
            driverTools.scrollIntoWebView(ELEMENT_LOCATOR);
            allAllotmentInputFields.add(driverTools.getText(ELEMENT_LOCATOR).orElseThrow(() -> new IllegalStateException("The allotment input field is missing or empty!")));
        }

        return allAllotmentInputFields;
    }

    public record Pair<K, V>(K first, V second) { }

    public List<Pair<String, String>> getAllAllotmentRoomNamesAndValues() {
        List<WebElement> ITEM_KEY_ELEMENTS = driver.findElements(By.xpath("//div[@class='update-preview-modal-content-item']//span[contains(@class,'item-key')]"));
        List<WebElement> ITEM_VALUE_ELEMENTS = driver.findElements(By.xpath("//div[@class='update-preview-modal-content-item']//span[@class='item-value']"));
        if (ITEM_KEY_ELEMENTS.size() != ITEM_VALUE_ELEMENTS.size()) {
            throw new IllegalStateException("Update preview modal items mismatch: keys=" + ITEM_KEY_ELEMENTS.size() + ", values=" + ITEM_VALUE_ELEMENTS.size());
        }
        int size = ITEM_VALUE_ELEMENTS.size();
        List<Pair<String, String>> allAllotmentRoomNamesAndValues = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            String itemKey = ITEM_KEY_ELEMENTS.get(i).getText();
            String itemValue = ITEM_VALUE_ELEMENTS.get(i).getText().replaceAll("[^0-9]", "").trim();
            allAllotmentRoomNamesAndValues.add(new Pair<>(itemKey, itemValue));
        }

        return allAllotmentRoomNamesAndValues;
    }

    public int getActiveWeekdaysCountPreviewPage() {
        driverTools.waitForElementToPresence(SELECTED_WEEK_DAYS_CHECKBOXES);
        List<WebElement> activeDays = driver.findElements(SELECTED_WEEK_DAYS_CHECKBOXES.by());
        return activeDays.size();
    }

    public List<String> getAllRoomTypesFromDropdown() {
        driverTools.scrollIntoWebView(ROOM_TYPE_INPUT);
        List<WebElement> ALL_ROOM_TYPES = driverTools.elements(DROPDOWN_OPTIONS).orElseThrow();
        List<String> allRoomTypes = new ArrayList<>();

        for (int i = 1; i < ALL_ROOM_TYPES.size(); i++) {
            WebElement element = ALL_ROOM_TYPES.get(i);
            allRoomTypes.add(driverTools.getText(element));
        }

        return allRoomTypes;
    }

    public String getRoomTagName(String roomType) {
        Locator ROOM_TAG = new Locator("ROOM_TAG_" + roomType, By.xpath("//div[contains(@class,'tag-room')]//span[contains(normalize-space(.),'%s')]".formatted(roomType)));
        driverTools.waitForElementToAppear(ROOM_TAG);
        return driverTools.getText(ROOM_TAG).orElseThrow(() -> new IllegalStateException("%s Room tag is missing".formatted(roomType)));
    }

    public boolean isElementDisabled(Locator locator) {
        logger.log("Checking if element is disabled: " + locator);
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

    public String selectFirstRatePlanWithValue(String roomName, String value) {
        driverTools.scrollAndClick(getRatePlanRateInputByRoomName(roomName));

        if (!driverTools.waitForElementsToAppear(getRatePlanDropdownListFirstOption(roomName)).isPresent()) {
            logger.log("No rate plans found for room name: " + roomName);
            return null;
        }

        String ratePlan = driverTools.getText(getRatePlanDropdownListFirstOption(roomName)).orElseThrow();
        if (ratePlan.isBlank()) {
            ratePlan = driverTools.getValueAttribute(getRatePlanDropdownListFirstOption(roomName), "textContent");
        }

        if (isElementDisabled(getRatePlanDropdownListFirstOption(roomName))) {
            logger.log("Rate Plan: %s is already selected".formatted(ratePlan));
        } else {
            logger.log("Selecting Rate Plan: " + ratePlan);
            WebElement dropdown = driverTools.waitForElementToBeClickable(getRatePlanDropdownListFirstOption(roomName)).orElseThrow();
            driverTools.click(dropdown);
        }

        WebElement input = driverTools.waitForElementToAppear(getRateInputByRoomName(roomName)).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, value);

        return ratePlan;
    }

    public boolean isRateInputFieldEnabled(String roomName) {
        return driverTools.isElementEnabled(getRateInputByRoomName(roomName));
    }
    public boolean isRateInputFieldDisabled(String roomType) {
        return !isRateInputFieldEnabled(roomType);
    }

    public String getRatePlanErrorMessage(String roomType) {
        if (!driverTools.waitForElementToAppear(getRatePlanErrorMessageLocator(roomType)).isPresent()) {
            return null;
        }
        return driverTools.getText(getRatePlanErrorMessageLocator(roomType)).orElseThrow();
    }

    public List<List<String>> getModalAllRoomNameRatePlanValue(List<List<String>> roomNameRatePlanValue) {
        List<List<String>> modalAllRoomNameRatePlanValue = new ArrayList<>();
        for (List<String> currRoomNameRatePlanValue: roomNameRatePlanValue) {
            String roomName = currRoomNameRatePlanValue.get(0);
            Locator MODAL_RATE_PLAN = new Locator("MODAL_RATE_PLAN", By.xpath("(//div[contains(@class,'update-preview-modal')]//span[contains(normalize-space(.),'%s')]//ancestor::div[1]//following-sibling::div)[last()]//span[contains(@class,'key')]".formatted(roomName)));
            Locator MODAL_RATE_VALUE = new Locator("MODAL_RATE_VALUE", By.xpath("(//div[contains(@class,'update-preview-modal')]//span[contains(normalize-space(.),'%s')]//ancestor::div[1]//following-sibling::div)[last()]//span[contains(@class,'value')]".formatted(roomName)));

            modalAllRoomNameRatePlanValue.add(List.of(
                    roomName,
                    driverTools.getText(MODAL_RATE_PLAN).orElseThrow(),
                    driverTools.getText(MODAL_RATE_VALUE).orElseThrow().replaceAll("[^0-9]", "").trim()
            ));
        }

        return modalAllRoomNameRatePlanValue;
    }

    public void clickCancellationPolicyTab(){
        driverTools.waitForElementToAppear(CANCELLATION_POLICY_TAB);
        driverTools.click(CANCELLATION_POLICY_TAB);
    }

    public Map<String, String> selectAndVerifyRoomAndRateType() {
        Map<String, String> selectedOptions = new HashMap<>();

        WebElement dropdown = driverTools.waitForElementToBeClickable(ROOM_TYPE_DROPDOWN).orElseThrow();
        driverTools.scrollElementToCenter(dropdown);
        driverTools.click(dropdown);
        WebElement firstRoom = driverTools.waitForElementToAppear(FIRST_ROOM_OPTION).orElseThrow();
        Assertion.assertThat("Verify at least one room option is displayed", firstRoom.isDisplayed(), is(true));
        driverTools.click(firstRoom);
        WebElement saveBtn = driverTools.waitForElementToBeClickable(SAVE_ROOM_BTN).orElseThrow();
        driverTools.click(saveBtn);
        WebElement roomTitleEl = driverTools.waitForElementToAppear(SELECTED_ROOM_TITLE_TEXT).orElseThrow();
        selectedOptions.put("roomName", roomTitleEl.getText().trim());
        WebElement ratePlanTitleEl = driverTools.waitForElementToAppear(FIRST_RATE_PLAN_TEXT).orElseThrow();
        selectedOptions.put("ratePlanName", ratePlanTitleEl.getText().trim());
        WebElement firstRatePlan = driverTools.waitForElementToAppear(FIRST_RATE_PLAN_OPTION).orElseThrow();
        Assertion.assertThat("Verify at least one rate plan option is displayed", firstRatePlan.isDisplayed(), is(true));
        driverTools.click(FIRST_RATE_PLAN_OPTION);
        return selectedOptions;
    }

    public void selectNonRefundablePolicy() {
        WebElement nonRefundableOption = driverTools.waitForElementToBeClickable(NON_REFUNDABLE_RADIO).orElseThrow();
        driverTools.scrollElementToCenter(nonRefundableOption);
        driverTools.click(nonRefundableOption);
    }

    public void verifyBulkUpdateSaveDialogAppears() {
        driverTools.waitForElementToAppear(BULK_UPDATE_SAVE_DIALOG_SUBJECT, 3);
    }

    public void clickSaveBulkUpdate() {
        driverTools.waitForElementToBeClickable(BULK_UPDATE_SAVE_BTN, 5).orElseThrow(() -> new NoSuchElementException("The Save button on the bulk update pop-up was not found or clickable")).click();
    }

    public void clickSavePolicyButton() {
        WebElement saveBtn = driverTools.waitForElementToBeClickable(SAVE_BTN).orElseThrow();
        driverTools.scrollElementToCenter(saveBtn);
        driverTools.click(saveBtn);
    }

    public void verifyCancellationPolicyModal(String expectedRoomCountEn,String expectedRoomCountId, String expectedDateRange) {
        WebElement modalTitle = driverTools.waitForElementToAppear(CANCELLATION_MODAL_TITLE).orElseThrow();
        Assertion.assertThat("Verify Cancellation Modal Title is displayed", modalTitle.isDisplayed(), is(true));

        String descriptionText = driverTools.waitForElementToAppear(CANCELLATION_MODAL_DESC).orElseThrow().getText();
        Assertion.assertThat("Verify Modal contains correct room count. Expected En: "+expectedRoomCountEn+" Expected Id: "+expectedRoomCountId+"  Actual: " + descriptionText, descriptionText, anyOf(containsString(expectedRoomCountEn), containsString(expectedRoomCountId)));
        Assertion.assertThat("Verify Modal contains correct date range. Expected to contain: " + expectedDateRange, descriptionText, anyOf(containsString(expectedDateRange), containsString(toBahasaDateRange(expectedDateRange))));
    }

    public void clickYesSavePolicy() {
        WebElement yesSaveBtn = driverTools.waitForElementToBeClickable(BULK_UPDATE_SAVE_BTN).orElseThrow();
        driverTools.click(yesSaveBtn);
    }

    public void verifySuccessSummaryModal(String expectedDateRange, String expectedPolicyEn, String expectedPolicyId, String expectedRoom, String expectedRatePlan) {
        WebElement modal = driverTools.waitForElementToAppear(SUMMARY_MODAL_CONTAINER).orElseThrow();
        Assertion.assertThat("Verify success summary modal is displayed", modal.isDisplayed(), is(true));

        String actualPeriod = driverTools.waitForElementToAppear(SUMMARY_PERIOD_TEXT).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Summary Modal Period. Expected to contain: " + expectedDateRange, actualPeriod, anyOf(containsString(expectedDateRange), containsString(toBahasaDateRange(expectedDateRange))));

        String actualPolicy = driverTools.waitForElementToAppear(SUMMARY_POLICY_TEXT).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Summary Modal Policy. Expected En: "+expectedPolicyEn+" Expected Id: "+expectedPolicyId+" Actual: "+ actualPolicy, actualPolicy, anyOf(containsString(expectedPolicyEn), containsString(expectedPolicyId)));

        String actualRoom = driverTools.waitForElementToAppear(SUMMARY_ROOM_TEXT).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Summary Modal Room. Expected to contain: " + expectedRoom, actualRoom, containsString(expectedRoom));

        String actualRatePlan = driverTools.waitForElementToAppear(SUMMARY_RATE_PLAN_TEXT).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Summary Modal Rate Plan. Expected to contain: " + expectedRatePlan, actualRatePlan, containsString(expectedRatePlan));
    }

    public void clickSummaryOkAndVerifyDisappears() {
        WebElement okBtn = driverTools.waitForElementToBeClickable(SUMMARY_OK_BTN).orElseThrow();
        driverTools.click(okBtn);

        boolean isGone = driverTools.waitForElementToDisappear(SUMMARY_MODAL_CONTAINER);
        Assertion.assertThat("Verify success summary modal disappeared after clicking Okay", isGone, is(true));
    }

    public String selectDateRangeInNextMonth(int startDay, int endDay) {
        WebElement input = driverTools.waitForElementToBeClickable(START_DATE_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(input);
        driverTools.click(input);
        WebElement startDayElement = driverTools.waitForElementToBeClickable(getNextMonthDayLocator(startDay)).orElseThrow(() -> new NoSuchElementException("Start day " + startDay + " not found in next month."));
        String startDate = startDayElement.getAttribute("aria-label");
        driverTools.click(startDayElement);
        WebElement endDayElement = driverTools.waitForElementToBeClickable(getNextMonthDayLocator(endDay)).orElseThrow(() -> new NoSuchElementException("End day " + endDay + " not found in next month."));
        String endDate = endDayElement.getAttribute("aria-label");
        driverTools.click(endDayElement);
        return startDate + " - " + endDate;
    }

    public void selectCustomPolicy() {
        WebElement customPolicyOption = driverTools.waitForElementToBeClickable(CUSTOM_POLICY_RADIO).orElseThrow(() -> new NoSuchElementException("Custom policy radio option not found."));
        driverTools.scrollElementToCenter(customPolicyOption);
        driverTools.click(customPolicyOption);
    }

    public void verifyCustomPolicyValidationErrors(String expectedDaysErrorEn, String expectedDaysErrorId, String expectedPctErrorEn, String expectedPctErrorId, String expectedNoShowErrorEn, String expectedNoShowErrorId) {
        WebElement daysErrorEl = driverTools.waitForElementToAppear(REFUNDABLE_DAYS_ERROR).orElseThrow(() -> new NoSuchElementException("Refundable days error not found."));
        String actualDaysError = daysErrorEl.getText().trim();
        Assertion.assertThat("Verify Refundable Days Error", actualDaysError, anyOf(containsString(expectedDaysErrorEn), containsString(expectedDaysErrorId)));

        WebElement cancelPctErrorEl = driverTools.waitForElementToAppear(CANCELLATION_PCT_ERROR).orElseThrow(() -> new NoSuchElementException("Cancellation percentage error not found."));
        String actualCancelPctError = cancelPctErrorEl.getText().trim();
        Assertion.assertThat("Verify Cancellation Pct Error", actualCancelPctError, anyOf(containsString(expectedPctErrorEn), containsString(expectedPctErrorId)));

        WebElement noShowPctErrorEl = driverTools.waitForElementToAppear(NO_SHOW_PCT_ERROR).orElseThrow(() -> new NoSuchElementException("No-show percentage error not found."));
        String actualNoShowPctError = noShowPctErrorEl.getText().trim();
        Assertion.assertThat("Verify No-show Pct Error", actualNoShowPctError, anyOf(containsString(expectedNoShowErrorEn), containsString(expectedNoShowErrorId)));
    }

    public void verifyCustomPolicyInvalidFieldValidationErrors(String expectedPctErrorEn, String expectedPctErrorId, String expectedNoShowErrorEn, String expectedNoShowErrorId) {
        WebElement cancelPctErrorEl = driverTools.waitForElementToAppear(CANCELLATION_PCT_ERROR).orElseThrow(() -> new NoSuchElementException("Cancellation percentage error not found."));
        String actualCancelPctError = cancelPctErrorEl.getText().trim();
        Assertion.assertThat("Verify Cancellation Pct Error", actualCancelPctError, anyOf(containsString(expectedPctErrorEn), containsString(expectedPctErrorId)));

        WebElement noShowPctErrorEl = driverTools.waitForElementToAppear(NO_SHOW_PCT_ERROR).orElseThrow(() -> new NoSuchElementException("No-show percentage error not found."));
        String actualNoShowPctError = noShowPctErrorEl.getText().trim();
        Assertion.assertThat("Verify No-show Pct Error", actualNoShowPctError, anyOf(containsString(expectedNoShowErrorEn), containsString(expectedNoShowErrorId)));
    }

    public void fillCustomPolicyFields(String days, String cancelPct, String noShowPct) {
        WebElement daysInput = driverTools.waitForElementToBeClickable(REFUNDABLE_DAYS_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(daysInput);
        daysInput.sendKeys(Keys.END);
        for (int i = 0; i < 5; i++) {
            daysInput.sendKeys(Keys.BACK_SPACE);
        }

        daysInput.sendKeys(days);
        String actualDays = daysInput.getAttribute("value");
        Assertion.assertThat("Verify Refundable Days input field", actualDays, is(equalTo(days)));

        WebElement cancelPctRadio = driverTools.waitForElementToBeClickable(CANCEL_PERCENT_RADIO).orElseThrow();
        driverTools.click(cancelPctRadio);

        WebElement cancelPctInput = driverTools.waitForElementToBeClickable(CANCELLATION_PCT_INPUT).orElseThrow();
        cancelPctInput.clear();
        cancelPctInput.sendKeys(cancelPct);

        String actualCancelPct = cancelPctInput.getAttribute("value");
        Assertion.assertThat("Verify Cancellation Percentage input field", actualCancelPct, is(equalTo(cancelPct)));

        WebElement noShowPctRadio = driverTools.waitForElementToBeClickable(NO_SHOW_PERCENT_RADIO).orElseThrow();
        driverTools.click(noShowPctRadio);

        WebElement noShowPctInput = driverTools.waitForElementToBeClickable(NO_SHOW_PCT_INPUT).orElseThrow();
        noShowPctInput.clear();
        noShowPctInput.sendKeys(noShowPct);

        String actualNoShowPct = noShowPctInput.getAttribute("value");
        Assertion.assertThat("Verify No-Show Percentage input field", actualNoShowPct, is(equalTo(noShowPct)));
    }

    public void enterRefundableDays(String days) {
        WebElement daysInput = driverTools.waitForElementToBeClickable(REFUNDABLE_DAYS_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(daysInput);
        daysInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        daysInput.sendKeys(days);
        daysInput.sendKeys(Keys.TAB);
    }

    public void selectCancelNightAndVerifyOptions(String opt1En, String opt1Id, String opt2En, String opt2Id) {
        WebElement radio = driverTools.waitForElementToBeClickable(CANCEL_NIGHT_RADIO).orElseThrow();
        driverTools.scrollElementToCenter(radio);
        driverTools.click(radio);

        WebElement dropdown = driverTools.waitForElementToBeClickable(CANCEL_NIGHT_DROPDOWN).orElseThrow();
        driverTools.click(dropdown);

        String actualOpt1 = driverTools.waitForElementToAppear(CANCEL_OPT_1).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Cancellation Option 1", actualOpt1, anyOf(containsString(opt1En), containsString(opt1Id)));

        String actualOpt2 = driverTools.waitForElementToAppear(CANCEL_OPT_2).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Cancellation Option 2", actualOpt2, anyOf(containsString(opt2En), containsString(opt2Id)));

        driverTools.click(driverTools.waitForElementToBeClickable(CANCEL_OPT_1).orElseThrow());
    }

    public void selectNoShowNightAndVerifyOptions(String opt1En, String opt1Id, String opt2En, String opt2Id) {
        WebElement radio = driverTools.waitForElementToBeClickable(NO_SHOW_NIGHT_RADIO).orElseThrow();
        driverTools.scrollElementToCenter(radio);
        driverTools.click(radio);
        WebElement dropdown = driverTools.waitForElementToBeClickable(NO_SHOW_NIGHT_DROPDOWN).orElseThrow();
        driverTools.click(dropdown);

        String actualOpt1 = driverTools.waitForElementToAppear(NO_SHOW_OPT_1).orElseThrow().getText().trim();
        Assertion.assertThat("Verify No-Show Option 1", actualOpt1, anyOf(containsString(opt1En), containsString(opt1Id)));

        String actualOpt2 = driverTools.waitForElementToAppear(NO_SHOW_OPT_2).orElseThrow().getText().trim();
        Assertion.assertThat("Verify No-Show Option 2", actualOpt2, anyOf(containsString(opt2En), containsString(opt2Id)));
        driverTools.click(driverTools.waitForElementToBeClickable(NO_SHOW_OPT_2).orElseThrow());
    }

    public void verifyCancellationFeeUIError(String expectedDaysErrorEn, String expectedDaysErrorId) {
        WebElement daysErrorEl = driverTools.waitForElementToAppear(REFUNDABLE_DAYS_ERROR).orElseThrow(() -> new NoSuchElementException("Refundable days error not found."));
        String actualDaysError = daysErrorEl.getText().trim();
        Assertion.assertThat("Verify Refundable Days Error", actualDaysError, anyOf(containsString(expectedDaysErrorEn), containsString(expectedDaysErrorId)));
    }

    public void clickRestrictionsTab(){
        driverTools.waitForElementToAppear(RESTRICTIONS_TAB);
        driverTools.scrollElementToCenter(RESTRICTIONS_TAB);
        driverTools.click(RESTRICTIONS_TAB);
    }

    public void turnOnAllRestrictionToggles() {
        WebElement losInput = driverTools.waitForElementToAppear(LOS_TOGGLE_INPUT).orElseThrow();
        if (!losInput.isSelected()) {
            WebElement losBtn = driverTools.waitForElementToBeClickable(LOS_TOGGLE_BTN).orElseThrow();
            driverTools.click(losBtn);
        }

        WebElement advInput = driverTools.waitForElementToAppear(ADV_BOOKING_TOGGLE_INPUT).orElseThrow();
        if (!advInput.isSelected()) {
            WebElement advBtn = driverTools.waitForElementToBeClickable(ADV_BOOKING_TOGGLE_BTN).orElseThrow();
            driverTools.click(advBtn);
        }

        WebElement ctaInput = driverTools.waitForElementToAppear(CTA_TOGGLE_INPUT).orElseThrow();
        if (!ctaInput.isSelected()) {
            WebElement ctaBtn = driverTools.waitForElementToBeClickable(CTA_TOGGLE_BTN).orElseThrow();
            driverTools.click(ctaBtn);
        }

        WebElement ctdInput = driverTools.waitForElementToAppear(CTD_TOGGLE_INPUT).orElseThrow();
        if (!ctdInput.isSelected()) {
            WebElement ctdBtn = driverTools.waitForElementToBeClickable(CTD_TOGGLE_BTN).orElseThrow();
            driverTools.click(ctdBtn);
        }
    }

    public void verifyOnlyLosAndAdvBookingInputsDisplayed() {
        Assertion.assertThat("Verify LOS Min Input is displayed", driverTools.waitForElementToAppear(LOS_MIN_INPUT).orElseThrow().isDisplayed(), is(true));
        Assertion.assertThat("Verify LOS Max Input is displayed", driverTools.waitForElementToAppear(LOS_MAX_INPUT).orElseThrow().isDisplayed(), is(true));

        Assertion.assertThat("Verify Advance Booking Min Input is displayed", driverTools.waitForElementToAppear(ADV_MIN_INPUT).orElseThrow().isDisplayed(), is(true));
        Assertion.assertThat("Verify Advance Booking Max Input is displayed", driverTools.waitForElementToAppear(ADV_MAX_INPUT).orElseThrow().isDisplayed(), is(true));

        List<WebElement> formGroups = driver.findElements(By.xpath("//div[contains(@class, 'restrictions-card--body-form')]//div[contains(@class, 'form-layout row-direction restriction-form-item')]"));
        Assertion.assertThat("Verify exactly 2 input groups (LOS and Adv Booking) are rendered", formGroups.size(), is(2));
    }

    public void verifyRestrictionValidationErrors(String expectedErrorEn, String expectedErrorId) {
        WebElement losMinErrorEl = driverTools.waitForElementToAppear(LOS_MIN_ERROR).orElseThrow(() -> new NoSuchElementException("LOS Min error message not found."));
        Assertion.assertThat("Verify LOS Minimum Error", losMinErrorEl.getText().trim(), anyOf(containsString(expectedErrorEn), containsString(expectedErrorId)));

        WebElement losMaxErrorEl = driverTools.waitForElementToAppear(LOS_MAX_ERROR).orElseThrow(() -> new NoSuchElementException("LOS Max error message not found."));
        Assertion.assertThat("Verify LOS Maximum Error", losMaxErrorEl.getText().trim(), anyOf(containsString(expectedErrorEn), containsString(expectedErrorId)));

        WebElement advMinErrorEl = driverTools.waitForElementToAppear(ADV_MIN_ERROR).orElseThrow(() -> new NoSuchElementException("Advance Booking Min error message not found."));
        Assertion.assertThat("Verify Advance Booking Minimum Error", advMinErrorEl.getText().trim(), anyOf(containsString(expectedErrorEn), containsString(expectedErrorId)));

        WebElement advMaxErrorEl = driverTools.waitForElementToAppear(ADV_MAX_ERROR).orElseThrow(() -> new NoSuchElementException("Advance Booking Max error message not found."));
        Assertion.assertThat("Verify Advance Booking Maximum Error", advMaxErrorEl.getText().trim(), anyOf(containsString(expectedErrorEn), containsString(expectedErrorId)));
    }

    public void fillRestrictionFields(String minLos, String maxLos, String minAdv, String maxAdv) {
        WebElement losMinInput = driverTools.waitForElementToBeClickable(LOS_MIN_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(losMinInput);
        losMinInput.click();
        losMinInput.sendKeys(Keys.END);
        for (int i = 0; i < 5; i++) losMinInput.sendKeys(Keys.BACK_SPACE);
        losMinInput.sendKeys(minLos);

        if (!minLos.isEmpty() && Integer.parseInt(minLos) < 0) {
            String expectedPositiveValue = String.valueOf(Math.abs(Integer.parseInt(minLos)));
            Assertion.assertThat("Verify Min LOS auto-corrected to "+minLos, losMinInput.getAttribute("value"), is(equalTo(expectedPositiveValue)));
        }

        WebElement losMaxInput = driverTools.waitForElementToBeClickable(LOS_MAX_INPUT).orElseThrow();
        losMaxInput.click();
        losMaxInput.sendKeys(Keys.END);
        for (int i = 0; i < 5; i++) losMaxInput.sendKeys(Keys.BACK_SPACE);
        losMaxInput.sendKeys(maxLos);
        losMaxInput.sendKeys(Keys.TAB);
        if (!maxLos.isEmpty() && Integer.parseInt(maxLos) > 365) {
            Assertion.assertThat("Verify Max LOS auto-corrected to 365. Entered value = "+maxLos, losMaxInput.getAttribute("value"), is(equalTo("365")));
        }

        WebElement advMinInput = driverTools.waitForElementToBeClickable(ADV_MIN_INPUT).orElseThrow();
        advMinInput.click();
        advMinInput.sendKeys(Keys.END);
        for (int i = 0; i < 5; i++) advMinInput.sendKeys(Keys.BACK_SPACE);
        advMinInput.sendKeys(minAdv);

        if (!minAdv.isEmpty() && Integer.parseInt(minAdv) < 0) {
            String expectedPositiveValue = String.valueOf(Math.abs(Integer.parseInt(minAdv)));
            Assertion.assertThat("Verify Min Adv Booking auto-corrected to "+minAdv, advMinInput.getAttribute("value"), is(equalTo(expectedPositiveValue)));
        }

        WebElement advMaxInput = driverTools.waitForElementToBeClickable(ADV_MAX_INPUT).orElseThrow();
        advMaxInput.click();
        advMaxInput.sendKeys(Keys.END);
        for (int i = 0; i < 5; i++) advMaxInput.sendKeys(Keys.BACK_SPACE);
        advMaxInput.sendKeys(maxAdv);
        advMaxInput.sendKeys(Keys.TAB);
        if (!maxAdv.isEmpty() && Integer.parseInt(maxAdv) > 500) {
            Assertion.assertThat("Verify Max Adv Booking auto-corrected to 500. Entered value = "+maxAdv, advMaxInput.getAttribute("value"), is(equalTo("500")));
        }
    }

    public void verifyMaxRestrictionErrors(String expectedLosMaxEn, String expectedLosMaxId, String expectedAdvMaxEn, String expectedAdvMaxId) {
        WebElement losMaxErrorEl = driverTools.waitForElementToAppear(LOS_MAX_ERROR).orElseThrow(() -> new NoSuchElementException("LOS Max error message not found for value 0."));
        Assertion.assertThat("Verify LOS Maximum Error", losMaxErrorEl.getText().trim(), anyOf(containsString(expectedLosMaxEn), containsString(expectedLosMaxId)));

        WebElement advMaxErrorEl = driverTools.waitForElementToAppear(ADV_MAX_ERROR).orElseThrow(() -> new NoSuchElementException("Advance Booking Max error message not found for value 0."));
        Assertion.assertThat("Verify Advance Booking Maximum Error", advMaxErrorEl.getText().trim(), anyOf(containsString(expectedAdvMaxEn), containsString(expectedAdvMaxId)));
    }

    public void verifyRestrictionConfirmationModal(String expectedRoomCountEn, String expectedRoomCountId, String expectedDateRange) {
        WebElement modalTitle = driverTools.waitForElementToAppear(RESTRICTION_MODAL_TITLE).orElseThrow(() -> new NoSuchElementException("Restriction confirmation modal title not found."));
        String titleText = modalTitle.getText().trim();

        Assertion.assertThat("Verify Restriction Modal Title", titleText, anyOf(is("Save the restrictions bulk update?"), is("Simpan bulk update restrictions?")));

        String descriptionText = driverTools.waitForElementToAppear(RESTRICTION_MODAL_DESC).orElseThrow().getText();

        Assertion.assertThat("Verify Modal contains correct room count. Actual: " + descriptionText, descriptionText, anyOf(containsString(expectedRoomCountEn), containsString(expectedRoomCountId)));

        Assertion.assertThat("Verify Modal contains correct date range. Expected to contain: " + expectedDateRange, descriptionText, anyOf(containsString(expectedDateRange), containsString(toBahasaDateRange(expectedDateRange))));
    }

    public void clickYesSaveRestriction() {
        WebElement yesBtn = driverTools.waitForElementToBeClickable(RESTRICTION_MODAL_YES_BTN).orElseThrow(() -> new NoSuchElementException("'Yes, save' button not found."));
        driverTools.click(yesBtn);
    }

    public void verifyRestrictionSuccessSummaryModal(String expectedDateRange, String expectedRoom, String expectedRatePlan, String expectedLos, String expectedAdv) {
        WebElement modal = driverTools.waitForElementToAppear(SUMMARY_MODAL_CONTAINER).orElseThrow(() -> new NoSuchElementException("Success summary modal did not appear."));
        Assertion.assertThat("Verify success summary modal is displayed", modal.isDisplayed(), is(true));

        String actualPeriod = driverTools.waitForElementToAppear(SUMMARY_PERIOD_TEXT).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Summary Modal Period", actualPeriod, anyOf(containsString(expectedDateRange), containsString(toBahasaDateRange(expectedDateRange))));

        String actualRoom = driverTools.waitForElementToAppear(SUMMARY_ROOM_TEXT).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Summary Modal Room", actualRoom, containsString(expectedRoom));

        String actualRatePlan = driverTools.waitForElementToAppear(SUMMARY_RATE_PLAN_TEXT).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Summary Modal Rate Plan", actualRatePlan, containsString(expectedRatePlan));

        String actualRestrictions = driverTools.waitForElementToAppear(SUMMARY_RESTRICTION_TEXT).orElseThrow().getText().trim();

        if (expectedLos.isEmpty() && expectedAdv.isEmpty()) {
            String leftOverText = actualRestrictions.replace("Restrictions:", "").trim();
            Assertion.assertThat("Verify Restrictions section is completely empty when toggles are OFF", leftOverText, is(""));
        } else {
            Assertion.assertThat("Verify LOS limits in modal", actualRestrictions, containsString(expectedLos));
            Assertion.assertThat("Verify Advance Booking limits in modal", actualRestrictions, containsString(expectedAdv));
            Assertion.assertThat("Verify Closed to arrival is Active", actualRestrictions, anyOf(containsString("Closed to arrival: Active"), containsString("Closed to arrival: Aktif")));
            Assertion.assertThat("Verify Closed to departure is Active", actualRestrictions, anyOf(containsString("Closed to departured: Active"), containsString("Closed to departured: Aktif")));
        }
    }

    public void turnOffAllRestrictionToggles() {
        WebElement losInput = driverTools.waitForElementToAppear(LOS_TOGGLE_INPUT).orElseThrow();
        if (losInput.isSelected()) {
            WebElement losBtn = driverTools.waitForElementToBeClickable(LOS_TOGGLE_BTN).orElseThrow();
            driverTools.click(losBtn);
        }

        WebElement advInput = driverTools.waitForElementToAppear(ADV_BOOKING_TOGGLE_INPUT).orElseThrow();
        if (advInput.isSelected()) {
            WebElement advBtn = driverTools.waitForElementToBeClickable(ADV_BOOKING_TOGGLE_BTN).orElseThrow();
            driverTools.click(advBtn);
        }

        WebElement ctaInput = driverTools.waitForElementToAppear(CTA_TOGGLE_INPUT).orElseThrow();
        if (ctaInput.isSelected()) {
            WebElement ctaBtn = driverTools.waitForElementToBeClickable(CTA_TOGGLE_BTN).orElseThrow();
            driverTools.click(ctaBtn);
        }

        WebElement ctdInput = driverTools.waitForElementToAppear(CTD_TOGGLE_INPUT).orElseThrow();
        if (ctdInput.isSelected()) {
            WebElement ctdBtn = driverTools.waitForElementToBeClickable(CTD_TOGGLE_BTN).orElseThrow();
            driverTools.click(ctdBtn);
        }
    }

    public void clickCancellationFeeAmountRadio() {
        WebElement radio = driverTools.waitForElementToBeClickable(CANCELLATION_FEE_AMOUNT_RADIO).orElseThrow();
        driverTools.scrollElementToCenter(radio);
        driverTools.click(radio);
    }

    public void clickNoShowAmountRadio() {
        WebElement radio = driverTools.waitForElementToBeClickable(NO_SHOW_AMOUNT_RADIO).orElseThrow();
        driverTools.scrollElementToCenter(radio);
        driverTools.click(radio);
    }

    public void fillAmountFields(String cancellationAmount, String noShowAmount) {
        WebElement cancelInput = driverTools.waitForElementToBeClickable(CANCELLATION_AMOUNT_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(cancelInput);
        driverTools.clearInput(cancelInput);
        cancelInput.sendKeys(cancellationAmount);

        WebElement noShowInput = driverTools.waitForElementToBeClickable(NO_SHOW_AMOUNT_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(noShowInput);
        driverTools.clearInput(noShowInput);
        noShowInput.sendKeys(noShowAmount);
    }

    public void verifyAmountValidationErrors(String expectedErrorEn, String expectedErrorId) {
        WebElement cancelErrorEl = driverTools.waitForElementToAppear(CANCELLATION_AMOUNT_ERROR).orElseThrow(() -> new NoSuchElementException("Cancellation amount error message not found."));
        String actualCancelError = cancelErrorEl.getText().trim();
        Assertion.assertThat("Verify Cancellation Amount Error", actualCancelError, anyOf(containsString(expectedErrorEn), containsString(expectedErrorId)));

        WebElement noShowErrorEl = driverTools.waitForElementToAppear(NO_SHOW_AMOUNT_ERROR).orElseThrow(() -> new NoSuchElementException("No-show amount error message not found."));
        String actualNoShowError = noShowErrorEl.getText().trim();
        Assertion.assertThat("Verify No-show Amount Error", actualNoShowError, anyOf(containsString(expectedErrorEn), containsString(expectedErrorId)));
    }

    public void verifySummaryPolicyDescription(String expectedAmount) {
        WebElement summaryEl = driverTools.waitForElementToAppear(SUMMARY_CANCELLATION_POLICY_TEXT).orElseThrow(() -> new NoSuchElementException("Summary policy text block not found."));
        driverTools.scrollElementToCenter(summaryEl);

        String summaryText = summaryEl.getText().trim().replaceAll("[,\\.]", "");
        Assertion.assertThat("Verify Summary Policy reflects the entered amount", summaryText, containsString(expectedAmount));
    }

    public void verifySummaryPolicyDescriptionForNight(String expectedEn, String expectedId) {
        WebElement summaryEl = driverTools.waitForElementToAppear(SUMMARY_CANCELLATION_POLICY_TEXT).orElseThrow(
                () -> new NoSuchElementException("Summary policy text block not found.")
        );
        driverTools.scrollElementToCenter(summaryEl);
        String summaryText = summaryEl.getText().trim().toLowerCase();
        Assertion.assertThat("Verify Summary Policy reflects the selected night option", summaryText, anyOf(containsStringIgnoringCase(expectedEn.toLowerCase()), containsStringIgnoringCase(expectedId.toLowerCase())));
    }

    public void verifyAmountInfoIconPresent() {
        driverTools.waitForElementsToAppear(AMOUNT_INFO_ICON);
        List<WebElement> icons = driver.findElements(AMOUNT_INFO_ICON.by());

        Assertion.assertThat("Verify exactly 2 Amount/Nominal info icons are present", icons.size(), is(2));
        Assertion.assertThat("Verify the Cancellation info icon is displayed", icons.get(0).isDisplayed(), is(true));
        Assertion.assertThat("Verify the No-show info icon is displayed", icons.get(1).isDisplayed(), is(true));

    }
}