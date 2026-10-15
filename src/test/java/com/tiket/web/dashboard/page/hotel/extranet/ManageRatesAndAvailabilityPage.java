package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class ManageRatesAndAvailabilityPage <T extends WebDriver> extends BasePage<T> {
    public ManageRatesAndAvailabilityPage(Session<T> session) {
        super(session);
    }
    
    private static final Locator MANAGE_RATE_AND_AVAIL_PAGE = new Locator("MANAGE_RATE_AND_AVAIL_PAGE", new By.ByXPath("//div[contains(@class,'the-manage-rates')]"));
    private static final Locator ROOM_NAME_DROPDOWN_INPUT = new Locator("ROOM_NAME_DROPDOWN_INPUT", new By.ByName("roomOptions"));
    private static final Locator RIGHT_DRAWER = new Locator("RIGHT_DRAWER", new By.ByXPath("//div[contains(@class, 'tix-drawer-right')]"));
    private static final Locator RIGHT_DRAWER_START_DATE_INPUT = new Locator("RIGHT_DRAWER_START_DATE_INPUT", new By.ByXPath("//input[@name='start-date']"));
    private static final Locator RIGHT_DRAWER_END_DATE_INPUT = new Locator("RIGHT_DRAWER_END_DATE_INPUT", new By.ByXPath("//input[@name='end-date']"));
    private static final Locator ALLOTMENT_INPUT = new Locator("ALLOTMENT_INPUT", new By.ByXPath("//input[contains(@name,'allotment-input')]"));
    private static final Locator RATES_INPUT = new Locator("RATES_INPUT", new By.ByXPath("//input[contains(@name,'rates-input')]"));
    private static final Locator CLOSE_OUT_ALL_TOGGLE_BUTTON = new Locator("CLOSE_OUT_ALL_TOGGLE_BUTTON", new By.ByXPath("(//div[@id='close-out']//span[contains(@class, 'the-toggle')])[1]"));
    private static final Locator CLOSE_OUT_ALL_ON_TOGGLE = new Locator("CLOSE_OUT_ALL_ON_TOGGLE", new By.ByXPath("//div[@class=\"allotment-options\"]//div[@id='close-out']//span[contains(@class,'the-toggle-btn-on')]"));
    private static final Locator CLOSE_OUT_ALL_OFF_TOGGLE = new Locator("CLOSE_OUT_ALL_OFF_TOGGLE", new By.ByXPath("//div[@class=\"allotment-options\"]//div[@id='close-out']//span[contains(@class,'the-toggle-btn-off')]"));
    private static final Locator CHOOSE_RATE_PLAN_INPUT = new Locator("CHOOSE_RATE_PLAN_INPUT", new By.ByXPath("//input[@name='ratePlanOptions']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", new By.ByXPath("//button[contains(@type,'submit')]"));
    private static final Locator CONFIRMATION_SUBMIT_BUTTON = new Locator("CONFIRMATION_SUBMIT_BUTTON", new By.ByXPath("//div[@class='submit-modal-footer']//button[2]"));
    private static final Locator SUCCESS_TOAST = new Locator("SUCCESS_TOAST", new By.ByXPath("//div[@class='the-toast success opened']"));
    private static final Locator RIGHT_DRAWER_COACHMARK_SKIP_BUTTON = new Locator("RIGHT_DRAWER_COACHMARK_SKIP_BUTTON", new By.ByXPath("//button[contains(@class,'coachmark-button-secondary')]"));
    private static final Locator CALENDAR_PAGE_TITLE = new Locator("CALENDAR_PAGE_TITLE", new By.ByXPath("//label[contains(@class, 'manage-rates-header-title') and (contains(text(), 'Manage Rate & Availability') or contains(text(), 'Atur Harga'))]"));
    private static final Locator FULL_CALENDAR_CONTAINER = new Locator("FULL_CALENDAR_CONTAINER", new By.ByXPath("//div[contains(@class, 'fc-view-harness-active')]"));
    private static final Locator NEXT_MONTH_BUTTON = new Locator("NEXT_MONTH_BUTTON", new By.ByXPath("//button[contains(@class, 'fc-nextBtn-button')]"));
    private static final Locator MONTH_YEAR_INPUT = new Locator("MONTH_YEAR_INPUT", new By.ByXPath("//input[@name='monthOptions']"));
    private static final Locator DRAWER_RATE_PLAN_CARDS = new Locator("DRAWER_RATE_PLAN_CARDS", new By.ByCssSelector("div.calendar-drawer-rate-plan-card"));
    private static final Locator DRAWER_SAVE_BUTTON = new Locator("DRAWER_SAVE_BUTTON", new By.ByXPath("//div[contains(@class,'calendar-drawer-form-footer')]//button[contains(text(),'SAVE') or contains(text(),'SIMPAN')]"));
    private static final Locator ALLOTMENT_ERROR_MESSAGE = new Locator("ALLOTMENT_ERROR_MESSAGE", new By.ByXPath("//div[@id='allotment-input']//div[contains(@class,'error-message')]"));
    private static final Locator RATE_ERROR_MESSAGES = new Locator("RATE_ERROR_MESSAGES", new By.ByCssSelector("div.rates-input-text div.error-message"));
    private static final Locator CLOSE_DRAWER_BUTTON = new Locator("CLOSE_DRAWER_BUTTON", new By.ByXPath("//div[@class='calendar-drawer-form-header']/span[contains(@class, 'tixicon-close')]"));
    private static final Locator LANGUAGE_SELECTOR = new Locator("LANGUAGE_SELECTOR", new By.ByCssSelector("div.head-language"));
    private static final Locator LANGUAGE_DROPDOWN_CONTAINER = new Locator("LANGUAGE_DROPDOWN_CONTAINER", new By.ByCssSelector("div.popover-list-container"));
    private static final Locator RATE_PLAN_SETUP_TITLE = new Locator("RATE_PLAN_SETUP_TITLE", new By.ByXPath("//div[contains(@class, 'page-header')]//label[contains(normalize-space(.), 'Rate Plan Setup') or contains(normalize-space(.), 'Pengaturan Rate Plan')]"));
    private static final Locator ADD_NEW_RATE_PLAN_BTN = new Locator("ADD_NEW_RATE_PLAN_BTN", new By.ByXPath("//button[contains(@class, 'add-button') and (contains(normalize-space(.), 'Add New Rate Plan') or contains(normalize-space(.), 'Tambah Rate Plan'))]"));
    private static final Locator RATE_PLAN_TYPE_MODAL_HEADING = new Locator("RATE_PLAN_TYPE_MODAL_HEADING", new By.ByXPath("//div[contains(@class, 'add-rateplan-modal')]//h2[contains(normalize-space(.), 'select the rate plan type') or contains(normalize-space(.), 'Pilih tipe rate plan dulu')]"));
    private static final Locator CUSTOM_RATE_PLAN_OPTION = new Locator("CUSTOM_RATE_PLAN_OPTION", new By.ByXPath("//div[contains(@class, 'options-item')]//span[contains(@class, 'title') and (contains(normalize-space(.), 'Custom rate plan') or contains(normalize-space(.), 'Rate plan custom'))]"));
    private static final Locator ADD_NEW_RATE_PLAN_PAGE_TITLE = new Locator("ADD_NEW_RATE_PLAN_PAGE_TITLE", new By.ByXPath("//h1[contains(normalize-space(.), 'Add New Rate Plan') or contains(normalize-space(.), 'Tambah Rate Plan Baru')]"));
    private static final Locator SUBMIT_NEW_RATE_PLAN_BTN = new Locator("SUBMIT_NEW_RATE_PLAN_BTN", new By.ByXPath("//button[@type='submit' and (contains(normalize-space(.), 'Add new rate plan') or contains(normalize-space(.), 'Tambah rate plan baru'))]"));
    private static final Locator SUBMIT_EDIT_RATE_PLAN_BTN = new Locator("SUBMIT_EDIT_RATE_PLAN_BTN", new By.ByXPath("//button[@type='submit' and (contains(normalize-space(.), 'Edit rate plan') or contains(normalize-space(.), 'Ubah rate plan'))]"));
    private static final String DYNAMIC_ERROR_MSG_XPATH = "//*[normalize-space(text())=\"%s\" or normalize-space(text())=\"%s\"]";
    private static final Locator RATE_PLAN_NAME_INPUT = new Locator("RATE_PLAN_NAME_INPUT", new By.ByXPath("//input[@id='rate-plan-name' or @id='nama-rate-plan']"));
    private static final Locator MINIMUM_NIGHTS_INPUT = new Locator("MINIMUM_NIGHTS_INPUT", new By.ByXPath("//input[@name='minDaysStay']"));
    private static final Locator MAXIMUM_NIGHTS_INPUT = new Locator("MAXIMUM_NIGHTS_INPUT", new By.ByXPath("//input[@name='maxDaysStay']"));
    private static final Locator NO_MEAL_RADIO_LABEL = new Locator("NO_MEAL_RADIO_LABEL", new By.ByXPath("//input[@name='meal' and contains(@value, 'NO_MEAL')]//ancestor::label"));
    private static final Locator DEFAULT_RATE_INPUT = new Locator("DEFAULT_RATE_INPUT", new By.ByXPath("//input[@id='default-rate' or contains(@name, 'defaultRate-')]"));
    private static final Locator REMOVE_RATE_BTN = new Locator("REMOVE_RATE_BTN", new By.ByXPath("//span[contains(@class, 'cursor-pointer') and (contains(text(), 'Remove') or contains(text(), 'Delete') or contains(text(), 'Hapus'))]"));
    private static final Locator NON_REFUNDABLE_RADIO_LABEL = new Locator("NON_REFUNDABLE_RADIO_LABEL", new By.ByXPath("//input[@name='refundable' and @value='false']//ancestor::label"));
    private static final Locator RATE_PLAN_SEARCH_INPUT = new Locator("RATE_PLAN_SEARCH_INPUT", By.name("search"));
    private static final Locator RATE_PLAN_GO_BUTTON = new Locator("RATE_PLAN_GO_BUTTON", new By.ByXPath("//button[contains(@class, 'go-button')]"));
    private static final Locator RATE_PLAN_FIRST_ROW_NAME_CELL = new Locator("RATE_PLAN_FIRST_ROW_NAME_CELL", new By.ByXPath("//tbody[contains(@class, 'table-body')]/tr[1]/td[1]"));
    private static final Locator RATE_PLAN_FIRST_ROW_TOGGLE_LABEL = new Locator("RATE_PLAN_FIRST_ROW_TOGGLE_LABEL", new By.ByXPath("//tbody[contains(@class, 'table-body')]/tr[1]//div[@class='the-toggle']/label"));
    private static final Locator RATE_PLAN_FIRST_ROW_EDIT_BUTTON = new Locator("RATE_PLAN_FIRST_ROW_EDIT_BUTTON", new By.ByXPath("//tbody[contains(@class, 'table-body')]/tr[1]//button[.//span[contains(@class, 'tixicon-edit_content')]]"));
    private static final Locator RATE_PLAN_FIRST_ROW_DELETE_BUTTON = new Locator("RATE_PLAN_FIRST_ROW_DELETE_BUTTON", new By.ByXPath("//tbody[contains(@class, 'table-body')]/tr[1]//button[.//span[contains(@class, 'tixicon-trash')]]"));
    private static final Locator EDIT_RATE_PLAN_HEADER = new Locator("EDIT_RATE_PLAN_HEADER", new By.ByXPath("//h1[(contains(text(), 'Edit') or contains(text(), 'Ubah')) and contains(., 'Rate Plan')]"));
    private static final Locator RATE_PLAN_DELETE_CONFIRMATION_MODAL = new Locator("RATE_PLAN_DELETE_CONFIRMATION_MODAL", new By.ByXPath("//div[contains(@class, 'rateplan-delete-modal-confirmation')]//button[contains(@class, 'delete-rateplan-button')]"));
    private static final Locator RATE_PLAN_CANCEL_DELETE_BUTTON = new Locator("RATE_PLAN_CANCEL_DELETE_BUTTON", By.id("cancel-button"));
    private static final Locator RATE_PLAN_CONFIRM_DELETE_BUTTON = new Locator("RATE_PLAN_CONFIRM_DELETE_BUTTON", By.cssSelector("button.delete-rateplan-button"));
    private static final Locator RATE_PLAN_DELETE_SUCCESS_MODAL = new Locator("RATE_PLAN_DELETE_SUCCESS_MODAL", By.cssSelector(".success-container"));
    private static final Locator RATE_PLAN_BACK_TO_LIST_BUTTON = new Locator("RATE_PLAN_BACK_TO_LIST_BUTTON", By.id("button-success"));
    private static final Locator RATE_PLAN_NO_DATA_CONTAINER = new Locator("NO_DATA_CONTAINER", By.cssSelector("div.no-data"));
    private static final Locator VIEW_LOG_BUTTON = new Locator("VIEW_LOG_BUTTON", By.xpath("//button[.//img[@alt='view ari logs button']]"));
    private static final Locator LOG_PAGE_TITLE = new Locator("LOG_PAGE_TITLE", By.xpath("//span[contains(text(), 'Rates & Availability Log') or contains(text(), 'Log Harga & Ketersediaan')]"));
    private static final Locator ITINERARY_ID_TAB = new Locator("ITINERARY_ID_TAB", By.xpath("//div[contains(@class, 'cursor-pointer')]//span[text()='Itinerary ID']"));
    private static final Locator ITINERARY_SEARCH_INPUT = new Locator("ITINERARY_SEARCH_INPUT", By.xpath("//input[@placeholder='Search by Itinerary ID' or @placeholder='Cari berdasarkan Itinerary ID']"));
    private static final Locator LOG_SEARCH_BUTTON = new Locator("LOG_SEARCH_BUTTON", By.xpath("//button[normalize-space()='Search' or normalize-space()='Cari']"));
    private static final Locator SUMMARY_GUEST_NAME = new Locator("SUMMARY_GUEST_NAME", By.xpath("//span[text()='Guest Name' or text()='Nama Tamu']/following-sibling::span"));
    private static final Locator SUMMARY_BOOKING_TIME = new Locator("SUMMARY_BOOKING_TIME", By.xpath("//span[text()='Booking Time' or text()='Waktu Pemesanan']/following-sibling::span"));
    private static final Locator CUSTOM_POLICY_RADIO = new Locator("CUSTOM_POLICY_RADIO", By.xpath("//input[@name='refundable' and @value='true']/ancestor::label"));
    private static final Locator REFUNDABLE_DAYS_INPUT = new Locator("REFUNDABLE_DAYS_INPUT", By.xpath("//input[@name='daysBefore']"));

    private static final Locator CANCEL_PERCENT_RADIO = new Locator("CANCEL_PERCENT_RADIO", By.xpath("//input[@name='cancellationCharge-type' and contains(@value, 'percentage')]/ancestor::label"));
    private static final Locator CANCEL_NIGHT_RADIO = new Locator("CANCEL_NIGHT_RADIO", By.xpath("//input[@name='cancellationCharge-type' and contains(@value, 'night')]/ancestor::label"));
    private static final Locator CANCELLATION_FEE_AMOUNT_RADIO = new Locator("CANCELLATION_FEE_AMOUNT_RADIO", By.xpath("//input[@name='cancellationCharge-type' and contains(@value, 'amount')]/ancestor::label"));

    private static final Locator CANCELLATION_PCT_INPUT = new Locator("CANCELLATION_PCT_INPUT", By.xpath("//input[@name='cancellationCharge-value-percentage']"));
    private static final Locator CANCELLATION_AMOUNT_INPUT = new Locator("CANCELLATION_AMOUNT_INPUT", By.xpath("//input[@name='cancellationAmount' or @name='cancellationCharge-value-amount']"));
    private static final Locator CANCEL_NIGHT_DROPDOWN = new Locator("CANCEL_NIGHT_DROPDOWN", By.xpath("//input[@name='cancellationCharge-value-night']"));

    private static final Locator NO_SHOW_PERCENT_RADIO = new Locator("NO_SHOW_PERCENT_RADIO", By.xpath("//input[@name='noShow-type' and contains(@value, 'percentage')]/ancestor::label"));
    private static final Locator NO_SHOW_NIGHT_RADIO = new Locator("NO_SHOW_NIGHT_RADIO", By.xpath("//input[@name='noShow-type' and contains(@value, 'night')]/ancestor::label"));
    private static final Locator NO_SHOW_AMOUNT_RADIO = new Locator("NO_SHOW_AMOUNT_RADIO", By.xpath("//input[@name='noShow-type' and contains(@value, 'amount')]/ancestor::label"));

    private static final Locator NO_SHOW_PCT_INPUT = new Locator("NO_SHOW_PCT_INPUT", By.xpath("//input[@name='noShow-value-percentage']"));
    private static final Locator NO_SHOW_AMOUNT_INPUT = new Locator("NO_SHOW_AMOUNT_INPUT", By.xpath("//input[@name='noShowAmount' or @name='noShow-value-amount']"));
    private static final Locator NO_SHOW_NIGHT_DROPDOWN = new Locator("NO_SHOW_NIGHT_DROPDOWN", By.xpath("//input[@name='noShow-value-night' or @name='noShowNight']"));
    private static final Locator DROPDOWN_OPT_1 = new Locator("DROPDOWN_OPT_1", By.xpath("(//li[@data-testid='dropdown-item'])[1]"));
    private static final Locator DROPDOWN_OPT_2 = new Locator("DROPDOWN_OPT_2", By.xpath("(//li[@data-testid='dropdown-item'])[2]"));

    private static final Locator SUMMARY_CANCELLATION_POLICY_TEXT = new Locator("SUMMARY_CANCELLATION_POLICY_TEXT", By.xpath("//span[contains(., 'Based on the above details') or contains(., 'Berdasarkan detail di atas')]/following-sibling::ol"));
    private static Locator getActiveToastLocator(String expectedEn, String expectedId) {
        String xpath = String.format(
                "//div[contains(@class, 'the-toast') and not(contains(@class, 'closed')) and (contains(., '%s') or contains(., '%s'))]",
                expectedEn,
                expectedId
        );
        String safeIdName = expectedEn.replaceAll("[^a-zA-Z0-9]", "_").toUpperCase();
        return new Locator("TOAST_MSG_" + safeIdName, By.xpath(xpath));
    }
    private static Locator getRoomCheckboxLocator(String roomName) {
        String xpath = String.format("//span[contains(@class, 'Check_children') and text()='%s']//ancestor::label", roomName);
        return new Locator("ROOM_CHECKBOX_" + roomName.replaceAll("\\s+", "_").toUpperCase(), new By.ByXPath(xpath));
    }

    private static Locator getSetDefaultTextLocator(String roomName) {
        String xpath = String.format("//span[text()='%s']//ancestor::label/following-sibling::span[contains(normalize-space(), 'Set default') or contains(normalize-space(), 'Atur default')]", roomName);
        return new Locator("SET_DEFAULT_TEXT_" + roomName.replaceAll("\\s+", "_").toUpperCase(), new By.ByXPath(xpath));
    }

    private static Locator getRoomDropdownByName(String name) {
        return new Locator("ROOM_DROPDOWN_LIST", new By.ByXPath(String.format("//div[@id=\"roomOptions\"]//label[text()='%s']/parent::span[contains(@class,'popoverlist-list')]", name)));
    }

    private static Locator getRatePlanDropdownCheckboxByName(String name) {
        return new Locator("RATE_PLAN_DROPDOWN_CHECKBOX", new By.ByXPath(String.format("//input[@name=\"ratePlanOptions\"]//ancestor::div[contains(@class,\"the-dropdown\")]//div[@class=\"dropdown-checkbox-label\"]/span[text()='%s']//ancestor::div[@class=\"dropdown-checkbox\"]//input[@type=\"checkbox\"]", name)));
    }

    private static Locator getRatePlanDropdownByName(String name) {
        return new Locator("RATE_PLAN_DROPDOWN_LIST", new By.ByXPath(String.format("//input[@name=\"ratePlanOptions\"]//ancestor::div[contains(@class,\"the-dropdown\")]//div[@class=\"dropdown-checkbox-label\"]/span[text()='%s']", name)));
    }

    private static Locator getCalendarDateByIndex(int index) {
        return new Locator("CALENDAR_DATE", new By.ByXPath(String.format("(//td[contains(@class,'fc-day-future') or contains(@class,'fc-day-today')])[%d]", index)));
    }

    private static Locator getCalendarAllotmentByIndex(int index) {
        return new Locator("CALENDAR_ALLOTMENT_LABEL", new By.ByXPath(String.format("(//td[contains(@class,'fc-day-future') or contains(@class,'fc-day-today')])[%d]//a[contains(@class,'allotment')]//bold", index-1)));
    }

    private static Locator getCalendarRateByIndex(int index) {
        return new Locator("CALENDAR_RATE_LABEL", new By.ByXPath(String.format("(//td[contains(@class,'fc-day-future') or contains(@class,'fc-day-today')])[%d]//div[@class=\"price-info\"]/span", index-1)));
    }

    private static Locator getCalendarCloseOutByIndex(int index) {
        return new Locator("CALENDAR_CLOSE_OUT_LABEL", new By.ByXPath(String.format("(//td[contains(@class,'fc-day-future') or contains(@class,'fc-day-today')])[%d]//div[@data-testid=\"close-out-event-hoverable\"]/span", index)));
    }

    private static Locator getDatepickerDayByIndex(int index) {
        return new Locator("DATEPICKER_DAY", new By.ByXPath(String.format("(//div[@class='the-datepicker']//div[contains(@class,'DayPicker-Day')][@aria-disabled='false'])[%d]", index)));
    }

    public boolean isManageRatesAndAvailabilityPageLoaded(){
        driverTools.waitForElementToAppear(MANAGE_RATE_AND_AVAIL_PAGE);
        return driverTools.isElementDisplayed(MANAGE_RATE_AND_AVAIL_PAGE);
    }

    public boolean isRatePlanPageLoaded(){
        driverTools.waitForElementToAppear(RATE_PLAN_SETUP_TITLE);
        return driverTools.isElementDisplayed(RATE_PLAN_SETUP_TITLE);
    }

    public boolean isTheRightDrawerLoaded(){
        driverTools.waitForElementToAppear(RIGHT_DRAWER);
        return driverTools.isElementDisplayed(RIGHT_DRAWER);
    }

    public void clickSingleDate(String date){
        Locator singleDate = new Locator("SINGLE_DATE", new By.ByXPath(String.format("//td[contains(@data-date, '%s')]", date)));
        driverTools.waitForElementToAppear(singleDate);
        driverTools.click(singleDate);
    }

    public void fillOutAllotmentInput(String allotment){
        WebElement input = driverTools.waitForElementToAppear(ALLOTMENT_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, allotment);
    }

    public void fillOutRatesInput(String rates){
        WebElement input = driverTools.waitForElementToAppear(RATES_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, rates);
    }

    public void clickSubmitButton(){
        driverTools.waitForElementToAppear(SUBMIT_BUTTON);
        driverTools.clickWithJavascriptExecutor(SUBMIT_BUTTON);
        driverTools.waitForElementToAppear(CONFIRMATION_SUBMIT_BUTTON);
        driverTools.clickWithJavascriptExecutor(CONFIRMATION_SUBMIT_BUTTON);
    }

    public void clickRightDrawerEndDateInput(){
        driverTools.waitForElementToAppear(RIGHT_DRAWER_END_DATE_INPUT);
        driverTools.click(RIGHT_DRAWER_END_DATE_INPUT);
    }

    public void chooseEndDate(String date){
        WebElement endDate = driver.findElement(By.xpath(String.format("//div[text()='%s']", date)));
        driverTools.waitForElementToAppear(endDate);
        driverTools.click(endDate);
    }

    public void selectRoom(String name) {
        WebElement input = driverTools.waitForElementToBeClickable(ROOM_NAME_DROPDOWN_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement option = driverTools.waitForElementToBeClickable(getRoomDropdownByName(name)).orElseThrow();
        driverTools.click(option);
    }

    public boolean isRatePlanSelected(String name) {
        WebElement checkbox = driverTools.waitForElementToPresence(getRatePlanDropdownCheckboxByName(name)).orElseThrow();
        return driverTools.isElementSelected(checkbox);
    }

    public void clickChooseRatePlanInput(){
        driverTools.waitForElementToAppear(CHOOSE_RATE_PLAN_INPUT);
        driverTools.click(CHOOSE_RATE_PLAN_INPUT);
    }

    public void chooseRatePlanTypeByName(String ratePlanName){
        List<WebElement> ratePlans = driver.findElements(By.xpath("//div[@class='dropdown-checkbox-label']//span[1]"));
        for (WebElement ratePlan: ratePlans){
            driverTools.waitForElementToAppear(ratePlan);
            if (!ratePlan.getText().equals(ratePlanName)){
                driverTools.click(ratePlan);
            }
        }
    }

    public void selectRatePlanTypeByName(String name, boolean isExpectedlySelected) {
        WebElement input = driverTools.waitForElementToBeClickable(CHOOSE_RATE_PLAN_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getRatePlanDropdownByName(name)).orElseThrow();
        boolean initialState = isRatePlanSelected(name);
        if (initialState && (!isExpectedlySelected)) {
            driverTools.click(dropdown);
        } else if ((!initialState) && isExpectedlySelected) {
            driverTools.click(dropdown);
        }
    }

    public void selectCalendarActiveDateByIndex(int index) {
        WebElement date = driverTools.waitForElementToBeClickable(getCalendarDateByIndex(index)).orElseThrow();
        driverTools.click(date);
    }

    public void selectDatepickerByIndex(String dateType, int index) {
        WebElement input = switch (dateType) {
            case "START_DATE" -> driverTools.waitForElementToBeClickable(RIGHT_DRAWER_START_DATE_INPUT).orElseThrow();
            case "END_DATE" -> driverTools.waitForElementToBeClickable(RIGHT_DRAWER_END_DATE_INPUT).orElseThrow();
            default -> null;
        };
        assert input != null;
        driverTools.clickWithJavascriptExecutor(input);
        WebElement datepicker = driverTools.waitForElementToBeClickable(getDatepickerDayByIndex(index)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(datepicker);
    }

    public boolean isAllotmentUpdatedByIndex(int index, String allotment) {
        boolean isCalendarAllotmentBadgeDisplayed;
        boolean result = false;
        while (index > 0) {
            isCalendarAllotmentBadgeDisplayed = driverTools.waitForElementToAppear(getCalendarAllotmentByIndex(index)).isPresent();
            if (isCalendarAllotmentBadgeDisplayed) {
                WebElement label = driverTools.waitForElementToAppear(getCalendarAllotmentByIndex(index)).orElseThrow();
                result = driverTools.getText(label).equals(allotment);
                index = 0;
            } else {
                index--;
            }
        }
        return result;
    }

    public boolean isRateUpdatedByIndex(int index, int rate) {
            WebElement label = driverTools.waitForElementToAppear(getCalendarRateByIndex(index)).orElseThrow();
            String labelText = driverTools.getText(label);
            String numericText = labelText.replaceAll("[^0-9]", "");
            int actualRate = Integer.parseInt(numericText);
            return actualRate == rate;
    }

    public boolean isCalendarCloseOutByIndex(int index) {
        boolean isCalendarCloseOutBadgeDisplayed;
        boolean result = false;
        while (index > 0) {
            isCalendarCloseOutBadgeDisplayed = driverTools.waitForElementToAppear(getCalendarCloseOutByIndex(index)).isPresent();
            if (isCalendarCloseOutBadgeDisplayed) {
                WebElement label = driverTools.waitForElementToAppear(getCalendarCloseOutByIndex(index)).orElseThrow();
                result = driverTools.getText(label).contains("Close Out");
                index = 0;
            } else {
                index--;
            }
        }
        return result;
    }

    public boolean verifyChooseRatePlanByName(String ratePlanName){
        return driverTools.getValue(CHOOSE_RATE_PLAN_INPUT).equals(ratePlanName);
    }

    public boolean verifyUpdatedAllotment(String allotment, String date){
        Locator updatedAllotment = new Locator("UPDATED_ALLOTMENT", new By.ByXPath(String.format("//td[contains(@data-date, '%s')]//bold[text()='%s']", date, allotment)));
        driverTools.waitForElementToAppear(updatedAllotment);
        return driverTools.isElementDisplayed(updatedAllotment);
    }

    public boolean verifyUpdatedRate(String rate, String date){
        WebElement updatedRate = driver.findElement(new By.ByXPath(String.format("//td[contains(@data-date, '%s')]//span[contains(text(), 'IDR')]", date)));
        driverTools.waitForElementToAppear(updatedRate);

        DecimalFormat decimalFormat = new DecimalFormat("#,###.###");
        String formattedRate = decimalFormat.format(Double.parseDouble(rate));
        System.out.println(Double.parseDouble(rate));
        System.out.println(updatedRate.getText());
        return driverTools.isElementDisplayed(updatedRate) && updatedRate.getText().contains(formattedRate);
    }

    public void clickCloseOutAllToggleButton(){
        driverTools.waitForElementToAppear(CLOSE_OUT_ALL_TOGGLE_BUTTON);
        driverTools.click(CLOSE_OUT_ALL_TOGGLE_BUTTON);
    }

    public void clickCloseOutAllToggleButton(boolean expectedStatus, String allotment, String rate){
        WebElement toggle = null;
        boolean isToggleOff = driverTools.waitForElementToAppear(CLOSE_OUT_ALL_OFF_TOGGLE).isPresent();
        if (isToggleOff) {
            toggle = driverTools.waitForElementToBeClickable(CLOSE_OUT_ALL_OFF_TOGGLE).orElseThrow();
        } else {
            toggle = driverTools.waitForElementToBeClickable(CLOSE_OUT_ALL_ON_TOGGLE).orElseThrow();
        }

        if ((expectedStatus && isToggleOff) || ((!expectedStatus) && (!isToggleOff))) {
            driverTools.clickWithJavascriptExecutor(toggle);
        }

        if (!expectedStatus) {
            fillOutAllotmentInput(allotment);
            fillOutRatesInput(rate);
        }
    }

    public boolean isSuccessToastAppeared(){
        driverTools.waitForElementToAppear(SUCCESS_TOAST);
        return driverTools.isElementDisplayed(SUCCESS_TOAST);
    }

    public void skipRightDrawerCoachmark() {
        try {
            WebElement coachmark = driverTools.waitForElementToBeClickable(RIGHT_DRAWER_COACHMARK_SKIP_BUTTON).orElseThrow();
            driverTools.click(coachmark);
        } catch (Exception ignored) {

        }
    }

    public void verifySuccessToastMessage(String expectedEN, String expectedID) {
        WebElement toastElement = driverTools.waitForElementToAppear(SUCCESS_TOAST).orElseThrow(() -> new RuntimeException("Success toast element did not appear on the UI!"));

        String actualMessage = toastElement.getText().trim();
        Assertion.assertThat(
                "Verify success toast text matches either English or Indonesian expectations",
                actualMessage,
                anyOf(containsString(expectedEN), containsString(expectedID))
        );
    }

    public void verifyCalendarPageOpened() {
        boolean isTitleVisible = driverTools.waitForElementToAppear(CALENDAR_PAGE_TITLE).isPresent();
        boolean isCalendarVisible = driverTools.waitForElementToAppear(FULL_CALENDAR_CONTAINER).isPresent();

        Assertion.assertThat("Verify Manage Rates header title is visible", isTitleVisible, is(true));
        Assertion.assertThat("Verify FullCalendar view layout grid is loaded", isCalendarVisible, is(true));
        logger.log("Manage Rate & Availability calendar page verified successfully.");
    }

    public String getCurrentCalendarMonth(){
        String fullText = driverTools.waitForElementToAppear(MONTH_YEAR_INPUT).map(el -> el.getAttribute("value").trim()).orElse("");
        return fullText.split(" ")[0];
    }

    public void navigateToNextMonth(int numberOfMonths) {
        for (int i = 1; i <= numberOfMonths; i++) {
            int attempts = 0;
            boolean success = false;
            while (attempts < 3 && !success) {
                try {
                    String initialMonth = getCurrentCalendarMonth();
                    WebElement nextBtn = driverTools.waitForElementToBeClickable(NEXT_MONTH_BUTTON)
                            .orElseThrow(() -> new RuntimeException("Next month button (>) is not clickable"));
                    driverTools.click(nextBtn);
                    org.openqa.selenium.support.ui.WebDriverWait wait =
                            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5));
                    wait.until(d -> {
                        String monthValue = driverTools.getAttribute(MONTH_YEAR_INPUT.by(), "value");
                        return monthValue != null && !monthValue.trim().toLowerCase().startsWith(initialMonth.toLowerCase());
                    });
                    success = true;
                } catch (org.openqa.selenium.StaleElementReferenceException e) {
                    logger.log("Caught Stale Element Reference on attempt " + (attempts + 1) + ". Retrying click...");
                    attempts++;
                }
            }

            if (!success) {
                throw new RuntimeException("Failed to navigate to next month after 3 attempts due to DOM instability.");
            }
        }
    }

    public void dragSelectDatesByIndices(int startIndex, int endIndex) {
        Locator startDay = new Locator("START_DAY_CELL",
                By.xpath(String.format("(//td[contains(@class, 'fc-daygrid-day')])[%d]", startIndex)));
        Locator endDay = new Locator("END_DAY_CELL",
                By.xpath(String.format("(//td[contains(@class, 'fc-daygrid-day')])[%d]", endIndex)));

        WebElement startElement = driverTools.waitForElementToBeClickable(startDay).orElseThrow();
        WebElement endElement = driverTools.waitForElementToBeClickable(endDay).orElseThrow();

        Actions actions = new Actions(driver);
        actions.moveToElement(startElement)
                .clickAndHold()
                .pause(java.time.Duration.ofMillis(300))
                .moveToElement(endElement)
                .pause(java.time.Duration.ofMillis(300))
                .release()
                .build()
                .perform();

        logger.log(String.format("Successfully dragged selection from index %d to %d", startIndex, endIndex));
    }

    public boolean areIndicesHighlighted(int startIndex) {
        Locator highlight = new Locator(
                "CALENDAR_HIGHLIGHT_" + startIndex,
                By.xpath(String.format("(//td[contains(@class, 'fc-daygrid-day')])[%d]//div[contains(@class, 'fc-highlight')]", startIndex))
        );
        return driverTools.waitForElementToAppear(highlight).isPresent();
    }

    public int getVisibleRatePlansCount() {
        driverTools.waitForElementToAppear(DRAWER_RATE_PLAN_CARDS);
        return driver.findElements(By.cssSelector("div.calendar-drawer-rate-plan-card")).size();
    }

    public void clearGlobalAllotmentInput() {
        WebElement allotmentInput = driverTools.waitForElementToAppear(ALLOTMENT_INPUT).orElseThrow();
        allotmentInput.click();
        allotmentInput.clear();
        allotmentInput.sendKeys(Keys.END);
        for (int k = 0; k < 12; k++) {
            allotmentInput.sendKeys(Keys.BACK_SPACE);
        }
        allotmentInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        allotmentInput.sendKeys(Keys.TAB);
        String finalAllotmentValue = allotmentInput.getAttribute("value");
        Assertion.assertThat("Global allotment input was not completely cleared! Remaining data found: [" + finalAllotmentValue + "]",finalAllotmentValue, is(""));
    }

    public void clearAllRatePlanInputs() {
        int totalRateFields = driver.findElements(By.cssSelector("input[name^='rates-input-']")).size();
        for (int i = 0; i < totalRateFields; i++) {
            WebElement rateInput = driver.findElement(By.xpath("(//input[contains(@name, 'rates-input-')])[" + (i + 1) + "]"));
            rateInput.click();
            rateInput.clear();
            rateInput.sendKeys(Keys.END);
            for (int k = 0; k < 12; k++) {
                rateInput.sendKeys(Keys.BACK_SPACE);
            }
            rateInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
            rateInput.sendKeys(Keys.TAB);
            WebElement verifiedInput = driver.findElement(By.xpath("(//input[contains(@name, 'rates-input-')])[" + (i + 1) + "]"));
            String finalRateValue = verifiedInput.getAttribute("value").trim();
            Assertion.assertThat("Rate field at index [" + (i + 1) + "] was not completely cleared! Remaining data found: [" + finalRateValue + "]", finalRateValue, is(""));
        }
    }

    public void clickDrawerSaveButton() {
        driverTools.waitForElementToBeClickable(DRAWER_SAVE_BUTTON).orElseThrow();
        driverTools.click(DRAWER_SAVE_BUTTON);
    }

    public void verifyAllotmentErrorMessage(String expectedEN, String expectedID) {
        WebElement allotmentError = driverTools.waitForElementToAppear(ALLOTMENT_ERROR_MESSAGE).orElseThrow();
        String actualMessage = allotmentError.getText().trim();
         Assertion.assertThat(
                "Verify allotment validation error message matches either English or Indonesian expectations",
                actualMessage,
                anyOf(containsString(expectedEN), containsString(expectedID))
        );
    }

    public void verifyRateErrorsOneByOne(String expectedEN, String expectedID, String value) {
        driverTools.waitForElementToAppear(RATE_ERROR_MESSAGES).orElseThrow();
        List<WebElement> rateErrors = driver.findElements(By.cssSelector("div.rates-input-text div.error-message"));
        Assertion.assertThat("Verify that rate error fields are present on the UI", rateErrors.isEmpty(), is(false));
        String cleanExpectedEN = expectedEN+" ("+value+")";
        String cleanExpectedID = expectedID+" ("+value+").";
        for (int i = 0; i < rateErrors.size(); i++) {
            String actualMessage = rateErrors.get(i).getText();
            Assertion.assertThat(
                    "Verify rate validation error message at index [" + (i + 1) + "] matches either English or Indonesian expectations",
                    actualMessage,
                    anyOf(containsString(cleanExpectedEN), containsString(cleanExpectedID))
            );
        }
    }

    public void verifyNoSuccessToastAppeared() {
        boolean isToastPresent = driverTools.waitForElementToAppear(SUCCESS_TOAST).isPresent();
        Assertion.assertThat("Verify that no success toast/snackbar appeared after invalid save attempt", isToastPresent, is(false));
    }

    public boolean waitForSuccessToastToDisappear() {
        try {
            driverTools.waitForElementToAppear(SUCCESS_TOAST).orElseThrow();
            return driverTools.waitForElementToAbsence(SUCCESS_TOAST, 15);
        } catch (Exception e) {
            logger.log("Toast disappeared (element not found): " + e.getMessage());
            return true;
        }
    }

    public void fillAllotmentForAllRatePlans(String allotment) {
        WebElement allotmentInput = driverTools.waitForElementToAppear(ALLOTMENT_INPUT).orElseThrow();
        allotmentInput.click();
        allotmentInput.clear();
        allotmentInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        allotmentInput.sendKeys(allotment);
        allotmentInput.sendKeys(Keys.TAB);
        String verifiedValue = allotmentInput.getAttribute("value").trim();
        Assertion.assertThat("Allotment field filled with expected value [" + allotment + "], actual [" + verifiedValue + "]", verifiedValue, is(allotment));
    }

    public void fillRateForAllRatePlans(String rate) {
        int totalRateFields = driver.findElements(By.cssSelector("input[name^='rates-input-']")).size();
        for (int i = 0; i < totalRateFields; i++) {
            WebElement rateInput = driver.findElement(By.xpath("(//input[contains(@name, 'rates-input-')])[" + (i + 1) + "]"));
            rateInput.click();
            rateInput.clear();
            rateInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
            rateInput.sendKeys(rate);
            rateInput.sendKeys(Keys.TAB);
            WebElement verifiedInput = driver.findElement(By.xpath("(//input[contains(@name, 'rates-input-')])[" + (i + 1) + "]"));
            String actualValue = verifiedInput.getAttribute("value").trim();
            String cleanedActualValue = actualValue.replaceAll(",", "");
            Assertion.assertThat("Rate field at index [" + (i + 1) + "] filled with expected value [" + rate + "], actual [" + cleanedActualValue + "]", cleanedActualValue, is(rate));
        }
    }

    public void verifyNoAllotmentErrorDisplayed() {
        boolean isAllotmentErrorPresent = driverTools.waitForElementToAppear(ALLOTMENT_ERROR_MESSAGE).isPresent();
        Assertion.assertThat("Verify that no allotment error message is displayed when allotment value is 0", isAllotmentErrorPresent, is(false));
    }

    public boolean isConfirmationPopupDisplayed() {
        Locator CONFIRM_MODAL = new Locator("CONFIRM_MODAL", new By.ByXPath("//div[@class='submit-modal-footer']"));
        return driverTools.waitForElementToAppear(CONFIRM_MODAL).isPresent();
    }

    public String getConfirmationPopupDateText() {
        Locator CONFIRM_MODAL_DESCRIPTION = new Locator("CONFIRM_MODAL_DESCRIPTION", new By.ByXPath("//div[@class='submit-modal-content']//span[@class='submit-modal-content-description']"));
        WebElement descriptionEl = driverTools.waitForElementToAppear(CONFIRM_MODAL_DESCRIPTION).orElseThrow();
        return descriptionEl.getText().trim();
    }

    public void clickConfirmationSaveButton() {
        driverTools.waitForElementToAppear(CONFIRMATION_SUBMIT_BUTTON);
        driverTools.clickWithJavascriptExecutor(CONFIRMATION_SUBMIT_BUTTON);
    }

    public void verifyCalendarAllotmentForSelectedDates(int startIndex, int endIndex, String expectedAllotment) {
        for (int i = startIndex; i <= endIndex; i++) {
            Locator calAllotment = new Locator("CALENDAR_ALLOTMENT_INDEX_" + i,
                    new By.ByXPath(String.format("(//td[contains(@class,'fc-daygrid-day')])[%d]//bold", i)));
            boolean isPresent = driverTools.waitForElementToAppear(calAllotment).isPresent();
            if (isPresent) {
                WebElement label = driverTools.waitForElementToAppear(calAllotment).orElseThrow();
                String actualAllotment = driverTools.getText(label).trim();
                Assertion.assertThat(
                        "Calendar allotment at date index [" + i + "]. Expected to contain [" + expectedAllotment + "], Actual: [" + actualAllotment + "]",
                        actualAllotment.contains(expectedAllotment), is(true));
            }
        }
        logger.log(String.format("Calendar allotment verified as [%s] for date range [%d] to [%d]", expectedAllotment, startIndex, endIndex));
    }

    public void verifyDrawerAllotmentValue(String expectedAllotment) {
        WebElement allotmentInput = driverTools.waitForElementToAppear(ALLOTMENT_INPUT).orElseThrow();
        String actualValue = allotmentInput.getAttribute("value").trim();
        Assertion.assertThat("Verify allotment value in drawer after reopening. Expected: [" + expectedAllotment + "], Actual: [" + actualValue + "]", actualValue, is(expectedAllotment));
    }

    public void verifyDrawerRateValuesForAllRatePlans(String expectedRate) {
        int totalRateFields = driver.findElements(By.cssSelector("input[name^='rates-input-']")).size();
        Assertion.assertThat("Verify that at least one rate field is present in the drawer", totalRateFields > 0, is(true));
        for (int i = 0; i < totalRateFields; i++) {
            WebElement rateInput = driver.findElement(By.xpath("(//input[contains(@name, 'rates-input-')])[" + (i + 1) + "]"));
            String actualValue = rateInput.getAttribute("value").trim();
            String cleanedActualValue = actualValue.replaceAll(",", "");
            Assertion.assertThat(
                    "Rate field at index [" + (i + 1) + "] after reopen. Expected: [" + expectedRate + "], Actual: [" + cleanedActualValue + "]",
                    cleanedActualValue, is(expectedRate));
        }
    }

    public void verifyNoValidationErrorsInDrawer() {
        List<WebElement> allotmentErrors = driver.findElements(By.xpath("//div[@id='allotment-input']//div[contains(@class,'error-message')]"));
        List<WebElement> rateErrors = driver.findElements(By.cssSelector("div.rates-input-text div.error-message"));
        Assertion.assertThat("Verify no allotment error message after reopening saved dates", allotmentErrors.isEmpty(), is(true));
        Assertion.assertThat("Verify no rate error messages after reopening saved dates", rateErrors.isEmpty(), is(true));
    }

    public void closeRightDrawer() {
        driverTools.waitForElementToAppear(CLOSE_DRAWER_BUTTON)
                .orElseThrow(() -> new RuntimeException("Close drawer button did not appear!"));
        driverTools.click(CLOSE_DRAWER_BUTTON);
    }

    public void clickLanguageSelector() {
        driverTools.waitForElementToAppear(LANGUAGE_SELECTOR)
                .orElseThrow(() -> new RuntimeException("Language selector icon context not found!"));
        driverTools.click(LANGUAGE_SELECTOR);
    }

    public boolean isLanguageDropdownVisible() {
        try {
            driverTools.waitForElementToAppear(LANGUAGE_DROPDOWN_CONTAINER);
            return driverTools.isElementDisplayed(LANGUAGE_DROPDOWN_CONTAINER);
        } catch (Exception e) {
            return false;
        }
    }

    private static Locator getLanguageOptionByName(String name) {
        return new Locator("LANGUAGE_OPTION", new By.ByXPath(String.format("//div[@class='popover-list' and text()='%s']", name)));
    }

    public void selectLanguageByName(String languageName) {
        Locator languageOption = getLanguageOptionByName(languageName);
        driverTools.waitForElementToAppear(languageOption)
                .orElseThrow(() -> new RuntimeException("Language option item '" + languageName + "' could not be targeted in dropdown!"));
        driverTools.click(languageOption);
        driver.navigate().refresh();
        boolean isPageReady = isManageRatesAndAvailabilityPageLoaded();
        Assertion.assertThat("Verify page DOM successfully reloaded after language change", isPageReady, is(true));
        logger.log("Browser DOM reloaded cleanly. Application state stabilized in: " + languageName);
    }

    public String getRightDrawerStartDateValue() {
        WebElement input = driverTools.waitForElementToAppear(RIGHT_DRAWER_START_DATE_INPUT)
                .orElseThrow(() -> new RuntimeException("Right drawer start date input field not visible"));
        String rawDate = input.getAttribute("value").trim();

        return rawDate.contains(",") ? rawDate.split(",")[1].trim() : rawDate;
    }

    public String getRightDrawerEndDateValue() {
        WebElement input = driverTools.waitForElementToAppear(RIGHT_DRAWER_END_DATE_INPUT)
                .orElseThrow(() -> new RuntimeException("Right drawer end date input field not visible"));
        String rawDate = input.getAttribute("value").trim();

        return rawDate.contains(",") ? rawDate.split(",")[1].trim() : rawDate;
    }

    public void clickAddNewRatePlanButton() {
        WebElement addBtn = driverTools.waitForElementToBeClickable(ADD_NEW_RATE_PLAN_BTN).orElseThrow();
        Assertion.assertThat("Verify the Add New Rate Button is visible", addBtn.isDisplayed(), is(true));
        driverTools.click(addBtn);
    }

    public void verifyRatePlanTypeModalAppears() {
        WebElement modalHeading = driverTools.waitForElementToAppear(RATE_PLAN_TYPE_MODAL_HEADING).orElseThrow();
        Assertion.assertThat("Verify the 'Select rate plan type' modal is displayed", modalHeading.isDisplayed(), is(true));
    }

    public void clickCustomRatePlanOption() {
        WebElement customOption = driverTools.waitForElementToBeClickable(CUSTOM_RATE_PLAN_OPTION).orElseThrow();
        Assertion.assertThat("Verify the Custom Rate Plan Option is visible", customOption.isDisplayed(), is(true));
        driverTools.click(customOption);
    }

    public void verifyAddNewRatePlanPageLoaded() {
        WebElement pageTitle = driverTools.waitForElementToAppear(ADD_NEW_RATE_PLAN_PAGE_TITLE).orElseThrow();
        Assertion.assertThat("Verify the 'Add New Rate Plan' page is successfully loaded", pageTitle.isDisplayed(), is(true));
    }

    public void clickSubmitNewRatePlanButton() {
        driverTools.scrollElementToCenter(SUBMIT_NEW_RATE_PLAN_BTN);
        WebElement submitBtn = driverTools.waitForElementToBeClickable(SUBMIT_NEW_RATE_PLAN_BTN).orElseThrow();
        Assertion.assertThat("Verify Submit button is visible", submitBtn.isDisplayed(), is(true));
        driverTools.click(submitBtn);
    }

    public void verifyErrorMessage(String expectedEn, String expectedId) {
        String dynamicXpath = String.format(DYNAMIC_ERROR_MSG_XPATH, expectedEn, expectedId);
        Locator errorLocator = new Locator("ERROR_MSG_" + expectedEn, By.xpath(dynamicXpath));
        WebElement errorElement = driverTools.waitForElementToAppear(errorLocator).orElseThrow();
        driverTools.scrollElementToCenter(errorElement);
        String actualText = errorElement.getText().trim();
        Assertion.assertThat("Verify validation error message is correctly displayed. Expected = "+expectedEn+" Actual = "+actualText, actualText, anyOf(containsString(expectedEn), containsString(expectedId)));
    }

    public void fillRatePlanName(String ratePlanName) {
        driverTools.scrollElementToCenter(RATE_PLAN_NAME_INPUT);
        WebElement nameInput = driverTools.waitForElementToBeClickable(RATE_PLAN_NAME_INPUT).orElseThrow();
        nameInput.click();
        driverTools.clearInput(nameInput);
        driverTools.sendKeys(nameInput, ratePlanName);
    }

    public void verifyRatePlanNameFilled(String expectedName) {
        WebElement nameInput = driverTools.waitForElementToAppear(RATE_PLAN_NAME_INPUT).orElseThrow();
        String actualName = nameInput.getAttribute("value");
        Assertion.assertThat("Verify the Rate Plan Name field is filled correctly. Expected = "+expectedName+" Actual = "+actualName, actualName, is(equalTo(expectedName)));
    }

    public void fillMinimumNights(String minNights) {
        WebElement minInput = driverTools.waitForElementToBeClickable(MINIMUM_NIGHTS_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(minInput);
        minInput.click();
        driverTools.clearInput(minInput);
        driverTools.sendKeys(minInput, minNights);
    }

    public void verifyMinimumNightsFilled(String expectedMin) {
        WebElement minInput = driverTools.waitForElementToAppear(MINIMUM_NIGHTS_INPUT).orElseThrow();
        String actualMin = minInput.getAttribute("value");
        Assertion.assertThat("Verify Minimum Nights field is filled correctly", actualMin, is(equalTo(expectedMin)));
    }

    public void fillMaximumNights(String maxNights) {
        WebElement maxInput = driverTools.waitForElementToBeClickable(MAXIMUM_NIGHTS_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(maxInput);
        maxInput.click();
        driverTools.clearInput(maxInput);
        driverTools.sendKeys(maxInput, maxNights);
    }

    public void verifyMaximumNightsFilled(String expectedMax) {
        WebElement maxInput = driverTools.waitForElementToAppear(MAXIMUM_NIGHTS_INPUT).orElseThrow();
        String actualMax = maxInput.getAttribute("value");
        Assertion.assertThat("Verify Maximum Nights field is filled correctly", actualMax, is(equalTo(expectedMax)));
    }

    public void selectNoMealOption() {
        WebElement radioLabel = driverTools.waitForElementToBeClickable(NO_MEAL_RADIO_LABEL).orElseThrow();
        driverTools.scrollElementToCenter(radioLabel);
        driverTools.click(radioLabel);
    }

    public void verifyNoMealOptionSelected() {
        WebElement radioInput = driver.findElement(By.xpath("//input[@name='meal' and contains(@value, 'NO_MEAL')]"));
        Assertion.assertThat("Verify the 'No Meal' radio option is successfully selected", radioInput.isSelected(), is(true));
    }

    public void selectLinkedRoom(String roomName) {
        Locator roomCheckbox = getRoomCheckboxLocator(roomName);
        WebElement checkboxLabel = driverTools.waitForElementToBeClickable(roomCheckbox).orElseThrow();
        driverTools.scrollElementToCenter(checkboxLabel);
        driverTools.click(checkboxLabel);
    }

    public void fillLinkedDefaultRate(String rateValue) {
        WebElement rateInput = driverTools.waitForElementToBeClickable(DEFAULT_RATE_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(rateInput);
        rateInput.click();
        driverTools.clearInput(rateInput);
        driverTools.sendKeys(rateInput, rateValue);
    }

    public void verifyLinkedDefaultRateFilled(String expectedRate) {
        WebElement rateInput = driverTools.waitForElementToAppear(DEFAULT_RATE_INPUT).orElseThrow();
        String actualRate = rateInput.getAttribute("value").replaceAll("[.,]", "");
        Assertion.assertThat("Verify Default Rate field is filled correctly. Expected = "+expectedRate+" Actual = "+actualRate, actualRate, is(equalTo(expectedRate)));
    }

    public void clickLinkedRemoveDefaultRate() {
        WebElement removeBtn = driverTools.waitForElementToBeClickable(REMOVE_RATE_BTN).orElseThrow();
        driverTools.click(removeBtn);
    }

    public void verifySetDefaultAppearsForRoom(String roomName) {
        Locator setDefaultText = getSetDefaultTextLocator(roomName);
        WebElement setDefaultElement = driverTools.waitForElementToAppear(setDefaultText).orElseThrow();
        Assertion.assertThat("Verify 'Set default' Hyperlink is displayed.", setDefaultElement.isDisplayed(), is(true));
    }

    public void clickSetDefaultHyperlink(String roomName) {
        Locator setDefaultText = getSetDefaultTextLocator(roomName);
        WebElement setDefaultElement = driverTools.waitForElementToAppear(setDefaultText).orElseThrow();
        driverTools.click(setDefaultElement);
    }

    public void selectNonRefundableOption() {
        WebElement radioLabel = driverTools.waitForElementToBeClickable(NON_REFUNDABLE_RADIO_LABEL).orElseThrow();
        driverTools.scrollElementToCenter(radioLabel);
        driverTools.click(radioLabel);
    }

    public void verifyNonRefundableOptionSelected() {
        WebElement radioInput = driver.findElement(By.xpath("//input[@name='refundable' and @value='false']"));
        Assertion.assertThat("Verify the 'Non-refundable' radio option is successfully selected", radioInput.isSelected(), is(true));
    }

    public void searchRatePlan(String ratePlanName) {
        WebElement searchBox = driverTools.waitForElementToBeClickable(RATE_PLAN_SEARCH_INPUT).orElseThrow();
        driverTools.clearInput(searchBox);
        driverTools.sendKeys(searchBox, ratePlanName);
        WebElement goBtn = driverTools.waitForElementToBeClickable(RATE_PLAN_GO_BUTTON).orElseThrow();
        driverTools.click(goBtn);

        // Wait until either results or "no data" is rendered
        if (driverTools.waitForElementToAppear(RATE_PLAN_FIRST_ROW_NAME_CELL, 5).isEmpty()) {
            driverTools.waitForElementToAppear(RATE_PLAN_NO_DATA_CONTAINER, 5).orElseThrow();
        }
    }

    public void verifySingleEntryAndNameMatch(String expectedName) {
        WebElement nameCell = driverTools.waitForElementToAppear(RATE_PLAN_FIRST_ROW_NAME_CELL).orElseThrow();
        int rowCount = driver.findElements(By.xpath("//tbody[contains(@class, 'table-body')]/tr[contains(@class, 'table-row')]")).size();
        Assertion.assertThat("Verify exactly one rate plan is displayed in the table", rowCount, is(1));

        String actualName = nameCell.getText().trim();
        Assertion.assertThat("Verify the rate plan name matches the search criteria. Expected = "+expectedName+" Actual = "+actualName, actualName, is(equalTo(expectedName)));
    }

    public void verifyStatusToggleIsOff() {
        Locator toggleInputLocator = new Locator(
                "RATE_PLAN_FIRST_ROW_TOGGLE_INPUT",
                By.xpath("//tbody[contains(@class, 'table-body')]/tr[1]//input[contains(@class, 'the-toggle-input')]")
        );
        WebElement toggleInput = driverTools.waitForElementToAppear(toggleInputLocator).orElseThrow();
        Assertion.assertThat("Verify the status toggle is turned OFF", toggleInput.isSelected(), is(false));
    }

    public void turnOnOffStatusToggle() {
        WebElement toggleLabel = driverTools.waitForElementToBeClickable(RATE_PLAN_FIRST_ROW_TOGGLE_LABEL).orElseThrow();
        driverTools.scrollElementToCenter(toggleLabel);
        driverTools.click(toggleLabel);
    }

    public void verifyToastMessage(String expectedEn, String expectedId) {
        Locator toastLocator = getActiveToastLocator(expectedEn, expectedId);
        WebElement toastElement = driverTools.waitForElementToAppear(toastLocator).orElseThrow();
        String actualText = toastElement.getText().trim();
        Assertion.assertThat("Verify the correct toast notification is displayed. Expected En = "+expectedEn+" Expected Id = "+expectedId+" Actual = "+actualText, actualText, anyOf(containsString(expectedEn), containsString(expectedId)));
    }

    public void clickRatePlanEditButton() {
        WebElement editBtn = driverTools.waitForElementToBeClickable(RATE_PLAN_FIRST_ROW_EDIT_BUTTON).orElseThrow();
        driverTools.scrollElementToCenter(editBtn);
        driverTools.click(editBtn);
    }

    public void verifyEditRatePlanPageLoaded() {
        WebElement header = driverTools.waitForElementToAppear(EDIT_RATE_PLAN_HEADER).orElseThrow();
        Assertion.assertThat("Verify the Edit Rate Plan page is successfully loaded", header.isDisplayed(), is(true));
    }

    public void clickRatePlanDeleteButton() {
        WebElement deleteBtn = driverTools.waitForElementToBeClickable(RATE_PLAN_FIRST_ROW_DELETE_BUTTON).orElseThrow();
        driverTools.scrollElementToCenter(deleteBtn);
        driverTools.click(deleteBtn);
    }

    public void clickSubmitEditRatePlanButton() {
        Assertion.assertThat("Edit Rate Plan Submit Button Displayed",driverTools.scrollAndCheckElement(SUBMIT_EDIT_RATE_PLAN_BTN), is(true));
        WebElement submitBtn = driverTools.waitForElementToAppear(SUBMIT_EDIT_RATE_PLAN_BTN).orElseThrow();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", submitBtn);
    }

    public void verifyDeleteConfirmationModalAppears() {
        WebElement modal = driverTools.waitForElementToAppear(RATE_PLAN_DELETE_CONFIRMATION_MODAL).orElseThrow();
        Assertion.assertThat("Verify the Delete Confirmation modal is displayed", modal.isDisplayed(), is(true));
    }

    public void clickCancelDelete() {
        WebElement cancelBtn = driverTools.waitForElementToBeClickable(RATE_PLAN_CANCEL_DELETE_BUTTON).orElseThrow();
        driverTools.click(cancelBtn);
    }

    public void clickConfirmDelete() {
        WebElement deleteBtn = driverTools.waitForElementToBeClickable(RATE_PLAN_CONFIRM_DELETE_BUTTON).orElseThrow();
        driverTools.click(deleteBtn);
    }

    public void verifyDeleteSuccessModalAppears() {
        WebElement successContainer = driverTools.waitForElementToAppear(RATE_PLAN_DELETE_SUCCESS_MODAL).orElseThrow();
        Assertion.assertThat("Verify the Delete Success modal is displayed", successContainer.isDisplayed(), is(true));
    }

    public void clickBackToList() {
        WebElement backToListBtn = driverTools.waitForElementToBeClickable(RATE_PLAN_BACK_TO_LIST_BUTTON).orElseThrow();
        driverTools.click(backToListBtn);
    }

    public void verifyNoDataFound(String expectedEn, String expectedId) {
        WebElement noDataElement = driverTools.waitForElementToAppear(RATE_PLAN_NO_DATA_CONTAINER).orElseThrow();
        String actualText = noDataElement.getText().trim();
        Assertion.assertThat("Verify the table is empty. Expected En = " + expectedEn + " Expected Id = " + expectedId + " Actual = " + actualText, actualText, anyOf(containsString(expectedEn), containsString(expectedId)));
    }

    public void deleteRatePlanIfExists(String expectedEn, String expectedId, String rateName) {
        // After searching, ensure we only delete when the first row matches the expected rate plan
        if (driverTools.waitForElementToAppear(RATE_PLAN_FIRST_ROW_NAME_CELL, 3).isPresent()) {
            String actualName = driverTools.waitForElementToAppear(RATE_PLAN_FIRST_ROW_NAME_CELL).orElseThrow().getText().trim();
            Assertion.assertThat(
                    "Search results did not match expected rate plan before deleting. Expected: " + rateName + ", Actual: " + actualName,
                    actualName,
                    is(equalTo(rateName))
            );

            clickRatePlanDeleteButton();
            verifyDeleteConfirmationModalAppears();
            clickConfirmDelete();
            verifyDeleteSuccessModalAppears();
            clickBackToList();
            verifyNoDataFound(expectedEn, expectedId);
        } else {
            // Explicitly validate that the empty state is shown
            verifyNoDataFound(expectedEn, expectedId);
        }
    }

    public void clickViewLogAndVerifyPageLoaded() {
        WebElement viewLogBtn = driverTools.waitForElementToAppear(VIEW_LOG_BUTTON, 5).orElseThrow(() -> new NoSuchElementException("'View log' button not found"));
        viewLogBtn.click();
        WebElement pageTitle = driverTools.waitForElementToAppear(LOG_PAGE_TITLE, 5).orElseThrow(() -> new NoSuchElementException("Rates & Availability Log page title did not load"));
        Assertion.assertThat("Verify Log page title is displayed", pageTitle.isDisplayed(), is(true));
    }

    public void searchByItineraryId(String itineraryId) {
        WebElement itineraryTab = driverTools.waitForElementToAppear(ITINERARY_ID_TAB, 5).orElseThrow();
        itineraryTab.click();

        WebElement searchInput = driverTools.waitForElementToAppear(ITINERARY_SEARCH_INPUT, 5).orElseThrow();
        searchInput.click();
        driverTools.clearInput(searchInput);
        searchInput.sendKeys(itineraryId);

        WebElement searchBtn = driverTools.waitForElementToAppear(LOG_SEARCH_BUTTON, 5).orElseThrow();
        searchBtn.click();
    }

    public void verifyLogTimestampForItinerary(String itineraryId, String expectedUtcTime) {
        String rowXpath = "//tbody/tr[td[contains(text(), 'Itin ID: " + itineraryId + "')]]";
        String timestampCellXpath = rowXpath + "/td[1]";
        Locator timestampCell = new Locator("TIMESTAMP_CELL", By.xpath(timestampCellXpath));
        WebElement cellElement = driverTools.waitForElementToAppear(timestampCell, 5).orElseThrow(() -> new NoSuchElementException("Row for Itinerary ID " + itineraryId + " was not found in the table"));
        String actualTimeDisplayed = cellElement.getText().trim();
        Assertion.assertThat("Verify the Last Updated time matches the API server time. Expected: "+expectedUtcTime+" Actual: "+actualTimeDisplayed, actualTimeDisplayed, is(expectedUtcTime));
    }

    public void verifyBookingSummaryDetails(String expectedGuestName, String expectedUiTime) {
        WebElement guestNameElement = driverTools.waitForElementToAppear(SUMMARY_GUEST_NAME, 5).orElseThrow(() -> new NoSuchElementException("Guest Name element not found in Booking Summary"));
        String actualGuestName = guestNameElement.getText().trim();
        Assertion.assertThat("Verify Guest Name in summary matches. Expected: "+expectedGuestName+" Actual: "+actualGuestName, actualGuestName, is(expectedGuestName));
        WebElement bookingTimeElement = driverTools.waitForElementToAppear(SUMMARY_BOOKING_TIME, 5).orElseThrow(() -> new NoSuchElementException("Booking Time element not found in Booking Summary"));
        String actualBookingTime = bookingTimeElement.getText().trim();
        Assertion.assertThat("Verify Booking Time in summary matches expected UI time. Expected: "+expectedUiTime+" Actual: "+actualBookingTime, actualBookingTime, is(expectedUiTime));
    }

    public void selectCustomPolicy() {
        WebElement customPolicyOption = driverTools.waitForElementToBeClickable(CUSTOM_POLICY_RADIO).orElseThrow(() -> new NoSuchElementException("Custom policy radio option not found."));
        driverTools.scrollElementToCenter(customPolicyOption);
        driverTools.click(customPolicyOption);
    }

    public void selectCancelNightAndVerifyOptions(String opt1En, String opt1Id, String opt2En, String opt2Id) {
        WebElement radio = driverTools.waitForElementToBeClickable(CANCEL_NIGHT_RADIO).orElseThrow();
        driverTools.scrollElementToCenter(radio);
        driverTools.click(radio);

        WebElement dropdown = driverTools.waitForElementToBeClickable(CANCEL_NIGHT_DROPDOWN).orElseThrow();
        driverTools.click(dropdown);

        String actualOpt1 = driverTools.waitForElementToAppear(DROPDOWN_OPT_1).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Cancellation Option 1", actualOpt1, anyOf(containsString(opt1En), containsString(opt1Id)));

        String actualOpt2 = driverTools.waitForElementToAppear(DROPDOWN_OPT_2).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Cancellation Option 2", actualOpt2, anyOf(containsString(opt2En), containsString(opt2Id)));

        driverTools.click(driverTools.waitForElementToBeClickable(DROPDOWN_OPT_1).orElseThrow());
    }

    public void selectNoShowNightAndVerifyOptions(String opt1En, String opt1Id, String opt2En, String opt2Id) {
        WebElement radio = driverTools.waitForElementToBeClickable(NO_SHOW_NIGHT_RADIO).orElseThrow();
        driverTools.scrollElementToCenter(radio);
        driverTools.click(radio);

        WebElement dropdown = driverTools.waitForElementToBeClickable(NO_SHOW_NIGHT_DROPDOWN).orElseThrow();
        driverTools.click(dropdown);

        String actualOpt1 = driverTools.waitForElementToAppear(DROPDOWN_OPT_1).orElseThrow().getText().trim();
        Assertion.assertThat("Verify No-Show Option 1", actualOpt1, anyOf(containsString(opt1En), containsString(opt1Id)));

        String actualOpt2 = driverTools.waitForElementToAppear(DROPDOWN_OPT_2).orElseThrow().getText().trim();
        Assertion.assertThat("Verify No-Show Option 2", actualOpt2, anyOf(containsString(opt2En), containsString(opt2Id)));

        driverTools.click(driverTools.waitForElementToBeClickable(DROPDOWN_OPT_2).orElseThrow());
    }

    public void fillCustomPolicyFields(String days, String cancelPct, String noShowPct) {
        WebElement daysInput = driverTools.waitForElementToBeClickable(REFUNDABLE_DAYS_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(daysInput);
        driverTools.clearInput(daysInput);
        daysInput.sendKeys(days);

        WebElement cancelPctRadio = driverTools.waitForElementToBeClickable(CANCEL_PERCENT_RADIO).orElseThrow();
        driverTools.click(cancelPctRadio);

        WebElement cancelPctInput = driverTools.waitForElementToBeClickable(CANCELLATION_PCT_INPUT).orElseThrow();
        driverTools.clearInput(cancelPctInput);
        cancelPctInput.sendKeys(cancelPct);

        WebElement noShowPctRadio = driverTools.waitForElementToBeClickable(NO_SHOW_PERCENT_RADIO).orElseThrow();
        driverTools.scrollElementToCenter(noShowPctRadio);
        driverTools.click(noShowPctRadio);

        WebElement noShowPctInput = driverTools.waitForElementToBeClickable(NO_SHOW_PCT_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(noShowPctInput);
        driverTools.clearInput(noShowPctInput);
        noShowPctInput.sendKeys(noShowPct);
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

    public void verifySummaryPolicyDescription(String expectedValue) {
        WebElement summaryEl = driverTools.waitForElementToAppear(SUMMARY_CANCELLATION_POLICY_TEXT).orElseThrow(() -> new NoSuchElementException("Summary policy text block not found."));
        driverTools.scrollElementToCenter(summaryEl);
        String summaryText = summaryEl.getText().trim().replaceAll("[,\\.]", "");
        Assertion.assertThat("Verify Summary Policy reflects the entered value: " + expectedValue, summaryText, containsString(expectedValue));
    }

    public void verifySummaryPolicyDescriptionForNight(String expectedEn, String expectedId) {
        WebElement summaryEl = driverTools.waitForElementToAppear(SUMMARY_CANCELLATION_POLICY_TEXT).orElseThrow(
                () -> new NoSuchElementException("Summary policy text block not found.")
        );
        driverTools.scrollElementToCenter(summaryEl);
        String summaryText = summaryEl.getText().trim();
        Assertion.assertThat("Verify Summary Policy reflects the selected night option", summaryText, anyOf(containsStringIgnoringCase(expectedEn.toLowerCase()), containsStringIgnoringCase(expectedId.toLowerCase())));
    }
}