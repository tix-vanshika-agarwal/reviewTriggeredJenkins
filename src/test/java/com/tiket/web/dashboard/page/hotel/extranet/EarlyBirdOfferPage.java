package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class EarlyBirdOfferPage <T extends WebDriver> extends BasePage<T> {
    public EarlyBirdOfferPage(Session<T> session) {
        super(session);
    }

    private static final Locator PROMO_NAME_INPUT = new Locator("PROMO_NAME_INPUT", new By.ByXPath("//input[@name='promoName']"));
    private static final Locator EARLY_BIRD_DISCOUNT_PERCENTAGE_INPUT = new Locator("EARLY_BIRD_DISCOUNT_PERCENTAGE_INPUT", new By.ByXPath("//input[@name='earlyBirdDiscountPercentage']"));
    private static final Locator EARLY_BIRD_MIN_DAYS_INPUT = new Locator("EARLY_BIRD_MIN_DAYS_INPUT", new By.ByXPath("//input[@name='earlyBirdMinDaysBeforeCheckIn']"));
    private static final Locator EARLY_BIRD_PAGE_TITLE = new Locator("EARLY_BIRD_PAGE_TITLE", new By.ByXPath("//h1[contains(@class,'Text_weight_bold')]"));
    private static final Locator EARLY_BIRD_PAGE_SUBTITLE = new Locator("EARLY_BIRD_PAGE_SUBTITLE", new By.ByXPath("//h1[contains(@class,'Text_weight_bold')]/following-sibling::span[1]"));
    private static final Locator EARLY_BIRD_INFO_BANNER = new Locator("EARLY_BIRD_INFO_BANNER", new By.ByXPath("//div[contains(@class,'NotificationBanner_variant_warning')]"));
    private static final Locator DESELECT_ALL_PLATFORM_LINKS = new Locator("DESELECT_ALL_PLATFORM_LINKS", new By.ByXPath("//div[@name='platforms']//span[contains(@class,'Text_variant_activity')]"));
    private static final Locator PLATFORM_CHECKBOXES = new Locator("PLATFORM_CHECKBOXES", new By.ByXPath("//div[@name='platforms']//input[@type='checkbox']"));
    private static final Locator FIRST_ROOM_FIRST_RATE_PLAN_CHECKBOX = new Locator("FIRST_ROOM_FIRST_RATE_PLAN_CHECKBOX", new By.ByXPath("(//div[@name='rooms']/div[contains(@class,'flex-col')])[1]//input[@type='checkbox'][1]"));
    private static final Locator SPECIFIC_BOOKING_PERIOD_RADIO = new Locator("SPECIFIC_BOOKING_PERIOD_RADIO", new By.ByXPath("//label[.//input[@name='bookingPeriod' and contains(@value,'specific')]]"));
    private static final Locator BOOKING_PERIOD_START_DATE_INPUT = new Locator("BOOKING_PERIOD_START_DATE_INPUT", new By.ByXPath("(//div[@name='bookingPeriodSection']//input[contains(@class,'DropdownFormDisplay_input')])[1]"));
    private static final Locator BOOKING_PERIOD_END_DATE_INPUT = new Locator("BOOKING_PERIOD_END_DATE_INPUT", new By.ByXPath("(//div[@name='bookingPeriodSection']//input[contains(@class,'DropdownFormDisplay_input')])[2]"));
    // Scoped to the LAST matching modal in the DOM: a closed modal can be left mounted (hidden, not removed) rather than unmounted,
    // and an unscoped/first-match locator would then risk matching that stale node instead of the current one.
    private static final By DATE_PICKER_MODAL_DAY_BUTTONS_BY = By.xpath("(//section[contains(@class,'Modal_modal_popup')])[last()]//button[contains(@class,'Day_day__')]");
    private static final By DATE_PICKER_TODAY_LABEL_XPATH = By.xpath(".//small[contains(@class,'Day_today_label')]");
    private static final Locator DATE_PICKER_MODAL = new Locator("DATE_PICKER_MODAL", new By.ByXPath("//section[contains(@class,'Modal_modal_popup')]"));
    private static final Locator DATE_PICKER_PREV_MONTH_BUTTON = new Locator("DATE_PICKER_PREV_MONTH_BUTTON", new By.ByXPath("(//section[contains(@class,'Modal_modal_popup')])[last()]//button[contains(@class,'CalendarDesktop_prev_button')]"));
    private static final Locator DATE_PICKER_NEXT_MONTH_BUTTON = new Locator("DATE_PICKER_NEXT_MONTH_BUTTON", new By.ByXPath("(//section[contains(@class,'Modal_modal_popup')])[last()]//button[contains(@class,'CalendarDesktop_next_button')]"));
    private static final By DATE_PICKER_MONTH_SECTIONS_BY = By.xpath("(//section[contains(@class,'Modal_modal_popup')])[last()]//section[contains(@class,'CalendarDesktop_month__')]");
    private static final By MONTH_HEADING_XPATH = By.xpath(".//h2");
    private static final Locator BOOKING_PERIOD_SELECT_DAYS_TOGGLE = new Locator("BOOKING_PERIOD_SELECT_DAYS_TOGGLE", new By.ByXPath("//div[@name='bookingPeriodSection']//div[.//input[@name='day_1']]/following-sibling::span[contains(@class,'Text_variant_activity')][1]"));
    private static final Locator BOOKING_PERIOD_END_TIME_INPUT = new Locator("BOOKING_PERIOD_END_TIME_INPUT", new By.ByXPath("//div[@name='bookingPeriodSection']//div[@name='endTime']//input"));
    private static final Locator BOOKING_PERIOD_START_TIME_INPUT = new Locator("BOOKING_PERIOD_START_TIME_INPUT", new By.ByXPath("//div[@name='bookingPeriodSection']//div[@name='endTime']/preceding-sibling::div[1]//input"));
    private static final Locator SPECIFIC_STAY_PERIOD_RADIO = new Locator("SPECIFIC_STAY_PERIOD_RADIO", new By.ByXPath("//label[.//input[@name='stayPeriod' and contains(@value,'specific')]]"));
    private static final Locator STAY_PERIOD_START_DATE_INPUT = new Locator("STAY_PERIOD_START_DATE_INPUT", new By.ByXPath("(//div[@id='stay-period-section']//input[contains(@class,'DropdownFormDisplay_input')])[1]"));
    private static final Locator STAY_PERIOD_END_DATE_INPUT = new Locator("STAY_PERIOD_END_DATE_INPUT", new By.ByXPath("(//div[@id='stay-period-section']//input[contains(@class,'DropdownFormDisplay_input')])[2]"));
    private static final Locator CANCELLATION_POLICY_YES_RADIO = new Locator("CANCELLATION_POLICY_YES_RADIO", new By.ByXPath("//label[.//input[@name='cancellationPolicy' and @value='true']]"));
    private static final Locator NON_REFUNDABLE_POLICY_RADIO = new Locator("NON_REFUNDABLE_POLICY_RADIO", new By.ByXPath("//label[.//input[@name='refundable' and @value='false']]"));
    private static final Locator CUSTOM_POLICY_RADIO = new Locator("CUSTOM_POLICY_RADIO", new By.ByXPath("//label[.//input[@name='refundable' and @value='true']]"));
    private static final Locator NON_REFUNDABLE_POLICY_INPUT = new Locator("NON_REFUNDABLE_POLICY_INPUT", new By.ByXPath("//input[@name='refundable' and @value='false']"));
    private static final Locator CUSTOM_POLICY_INPUT = new Locator("CUSTOM_POLICY_INPUT", new By.ByXPath("//input[@name='refundable' and @value='true']"));
    private static final Locator CANCELLATION_DAYS_BEFORE_INPUT = new Locator("CANCELLATION_DAYS_BEFORE_INPUT", new By.ByXPath("//input[@name='cancellationDaysBefore']"));
    private static final Locator CANCELLATION_CHARGE_VALUE_INPUT = new Locator("CANCELLATION_CHARGE_VALUE_INPUT", new By.ByXPath("//input[@name='cancellationChargeValue']"));
    private static final Locator NO_SHOW_VALUE_INPUT = new Locator("NO_SHOW_VALUE_INPUT", new By.ByXPath("//input[@name='noShowValue']"));
    private static final Locator CANCELLATION_POLICY_SUMMARY_BANNER = new Locator("CANCELLATION_POLICY_SUMMARY_BANNER", new By.ByXPath("//div[contains(@class,'FFF4E3')]"));
    private static final Locator COMBINABLE_PROMO_TOGGLE_CHECKBOX = new Locator("COMBINABLE_PROMO_TOGGLE_CHECKBOX", new By.ByXPath("(//label[.//input[@class='Toggle_hidden_checkbox__croZa']])[1]"));
    private static final Locator BLACKOUT_DATES_TOGGLE_CHECKBOX = new Locator("BLACKOUT_DATES_TOGGLE_CHECKBOX", new By.ByXPath("(//label[.//input[@class='Toggle_hidden_checkbox__croZa']])[last()]"));
    // The blackout calendar is rendered inline on the page (not in a modal like the other date pickers), so it's
    // excluded here to avoid ever matching a transient date-picker modal's calendar instead.
    private static final By BLACKOUT_CALENDAR_DAY_BUTTONS_BY = By.xpath("//div[contains(@class,'CalendarDesktop_calendar__pNrTr')][not(ancestor::section[contains(@class,'Modal_modal_popup')])]//button[contains(@class,'Day_day__')]");
    private static final Locator BLACKOUT_CALENDAR_NEXT_MONTH_BUTTON = new Locator("BLACKOUT_CALENDAR_NEXT_MONTH_BUTTON", new By.ByXPath("//div[contains(@class,'CalendarDesktop_calendar__pNrTr')][not(ancestor::section[contains(@class,'Modal_modal_popup')])]//button[contains(@class,'CalendarDesktop_next_button')]"));
    private static final By DAY_NUMBER_XPATH = By.xpath(".//span[contains(@class,'Day_day_number__')]");
    // Covers both UI locales so the calendar's month heading (e.g. "Oktober 2026" / "October 2026") can be parsed
    // into an actual month number regardless of which language is active.
    private static final Map<String, Integer> MONTH_NAME_TO_NUMBER = Map.ofEntries(
            Map.entry("january", 1), Map.entry("januari", 1),
            Map.entry("february", 2), Map.entry("februari", 2),
            Map.entry("march", 3), Map.entry("maret", 3),
            Map.entry("april", 4),
            Map.entry("may", 5), Map.entry("mei", 5),
            Map.entry("june", 6), Map.entry("juni", 6),
            Map.entry("july", 7), Map.entry("juli", 7),
            Map.entry("august", 8), Map.entry("agustus", 8),
            Map.entry("september", 9),
            Map.entry("october", 10), Map.entry("oktober", 10),
            Map.entry("november", 11),
            Map.entry("december", 12), Map.entry("desember", 12)
    );
    private static final Locator BLACKOUT_SELECTED_DATES_SCROLL_CONTAINER = new Locator("BLACKOUT_SELECTED_DATES_SCROLL_CONTAINER", new By.ByXPath("//div[contains(@class,'overflow-y-auto') and contains(@class,'scrollbar-gutter')]"));
    private static final Locator BLACKOUT_SELECTED_DATES_LIST_ITEMS = new Locator("BLACKOUT_SELECTED_DATES_LIST_ITEMS", new By.ByXPath("//div[contains(@class,'overflow-y-auto') and contains(@class,'scrollbar-gutter')]//ul/li"));
    private static final Locator EARLY_BIRD_REVIEW_BUTTON = new Locator("EARLY_BIRD_REVIEW_BUTTON", new By.ByXPath("//form//button[@type='submit']"));
    private static final Locator TIME_PICKER_POPUP = new Locator("TIME_PICKER_POPUP", new By.ByXPath("//div[contains(@class,'TimePickerDesktop_time_picker')]"));
    private static final By TIME_PICKER_SEGMENT_INPUTS_BY = By.xpath("//div[contains(@class,'TimePickerDesktop_time_picker')]//input[contains(@class,'TimePickerDesktop_input')]");
    private static final Locator TIME_PICKER_SAVE_BUTTON = new Locator("TIME_PICKER_SAVE_BUTTON", new By.ByXPath("//div[contains(@class,'PickerAction_picker_action')]//button"));
    private static final Locator REVIEW_MODAL = new Locator("REVIEW_MODAL", new By.ByXPath("//div[contains(@class,'Dialog_pop_up')]"));
    private static final Locator REVIEW_MODAL_EDIT_BUTTON = new Locator("REVIEW_MODAL_EDIT_BUTTON", new By.ByXPath("//div[contains(@class,'Dialog_pop_up')]//button[contains(@class,'Button_variant_secondary')]"));
    private static final Locator REVIEW_MODAL_CREATE_BUTTON = new Locator("REVIEW_MODAL_CREATE_BUTTON", new By.ByXPath("//div[contains(@class,'Dialog_pop_up')]//button[contains(@class,'Button_variant_primary')]"));
    // The price breakdown's "Early Bird" line is rendered in the design system's green text color for this promo type.
    private static final Locator REVIEW_MODAL_EARLY_BIRD_COMPONENT_ROW = new Locator("REVIEW_MODAL_EARLY_BIRD_COMPONENT_ROW", new By.ByXPath("//div[contains(@class,'Dialog_pop_up')]//div[contains(@class,'text-[#0bae54]')][contains(.,'Early Bird')]"));

    public boolean isEarlyBirdOfferPageLoaded() {
        driverTools.waitForElementToAppear(EARLY_BIRD_DISCOUNT_PERCENTAGE_INPUT);
        return driverTools.isElementDisplayed(EARLY_BIRD_DISCOUNT_PERCENTAGE_INPUT);
    }

    public void verifyEarlyBirdPageTitleAndSubtitle(String expectedTitleEn, String expectedTitleId, String expectedSubtitleEn, String expectedSubtitleId) {
        String actualTitle = driverTools.waitForElementToAppear(EARLY_BIRD_PAGE_TITLE).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Early Bird Offer page title", actualTitle, anyOf(is(expectedTitleEn), is(expectedTitleId)));
        String actualSubtitle = driverTools.waitForElementToAppear(EARLY_BIRD_PAGE_SUBTITLE).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Early Bird Offer page subtitle", actualSubtitle, anyOf(is(expectedSubtitleEn), is(expectedSubtitleId)));
    }

    public void fillOutPromoNameInput(String name) {
        WebElement input = driverTools.waitForElementToAppear(PROMO_NAME_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, name);
    }

    public void fillOutEarlyBirdDiscountPercentage(String discount) {
        WebElement input = driverTools.waitForElementToAppear(EARLY_BIRD_DISCOUNT_PERCENTAGE_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(input);
        driverTools.clearInput(input);
        driverTools.sendKeys(input, discount);
    }

    public void fillOutEarlyBirdMinDays(String days) {
        WebElement input = driverTools.waitForElementToAppear(EARLY_BIRD_MIN_DAYS_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(input);
        driverTools.clearInput(input);
        driverTools.sendKeys(input, days);
    }

    public void verifyEarlyBirdInfoBannerDisplayed(String discountPercentage, String minDays) {
        WebElement banner = driverTools.waitForElementToAppear(EARLY_BIRD_INFO_BANNER).orElseThrow(() -> new NoSuchElementException("Early Bird info banner not found"));
        String bannerText = banner.getText();
        String expectedDiscountText = discountPercentage + "%";
        Assertion.assertThat("Verify banner shows the entered discount percentage. Expected to contain: " + expectedDiscountText + " | Actual banner text: " + bannerText, bannerText, containsString(expectedDiscountText));
        Assertion.assertThat("Verify banner shows the entered minimum days. Expected to contain: " + minDays + " | Actual banner text: " + bannerText, bannerText, containsString(minDays));
    }

    public void deselectAllPlatforms() {
        List<WebElement> deselectAllLinks = driverTools.elements(DESELECT_ALL_PLATFORM_LINKS).orElseThrow();
        for (WebElement link : deselectAllLinks) {
            driverTools.click(link);
        }
    }

    // Clicks only the checkboxes that aren't already checked, so pre-selected platforms are left as-is.
    // The custom-styled checkbox span visually overlaps the raw input, so a normal click is intercepted; use JS executor instead.
    public void selectAllPlatforms() {
        List<WebElement> checkboxes = driverTools.elements(PLATFORM_CHECKBOXES).orElseThrow();
        for (WebElement checkbox : checkboxes) {
            if (!checkbox.isSelected()) {
                driverTools.scrollElementToCenter(checkbox);
                driverTools.clickWithJavascriptExecutor(checkbox);
            }
        }
    }

    public void clickFirstRoomFirstRatePlanCheckbox() {
        WebElement checkbox = driverTools.waitForElementToPresence(FIRST_ROOM_FIRST_RATE_PLAN_CHECKBOX).orElseThrow();
        driverTools.scrollElementToCenter(checkbox);
        driverTools.clickWithJavascriptExecutor(checkbox);
    }

    public void selectSpecificBookingPeriod() {
        WebElement radio = driverTools.waitForElementToAppear(SPECIFIC_BOOKING_PERIOD_RADIO).orElseThrow();
        driverTools.scrollElementToCenter(radio);
        driverTools.click(radio);
    }

    private void openDatePickerModal(Locator dateInputLocator) {
        WebElement dateInput = driverTools.waitForElementToAppear(dateInputLocator).orElseThrow();
        driverTools.scrollElementToCenter(dateInput);
        driverTools.clickWithJavascriptExecutor(dateInput);
        driverTools.waitForElementToAppear(DATE_PICKER_MODAL).orElseThrow(() -> new NoSuchElementException("Date picker modal did not open."));
    }

    private void clickDayAndCloseModal(List<WebElement> dayButtons, int targetIndex) {
        if (targetIndex < 0 || targetIndex >= dayButtons.size()) {
            throw new IndexOutOfBoundsException("Target date is out of the currently rendered calendar view. (Need index " + targetIndex + " but only have " + dayButtons.size() + ")");
        }
        driverTools.clickWithJavascriptExecutor(dayButtons.get(targetIndex));
        // The modal closes on selection (animated, not instant), so wait it out before the next field tries to open a fresh one.
        driverTools.waitForElementToDisappear(DATE_PICKER_MODAL, 5);
    }

    // Opens the date-picker modal on the given input and clicks the day that is `daysFromToday` after the modal's own "Today" marker.
    private void pickDateOffsetFromToday(Locator dateInputLocator, int daysFromToday) {
        openDatePickerModal(dateInputLocator);

        // The calendar keeps whatever month it was last scrolled to rather than resetting on open, so if a
        // previous pick left it scrolled forward, "Today" can be off-screen here. Step back until it's visible.
        List<WebElement> dayButtons = List.of();
        int todayIndex = -1;
        for (int attempt = 0; attempt < 24 && todayIndex == -1; attempt++) {
            dayButtons = driverTools.getDriver().findElements(DATE_PICKER_MODAL_DAY_BUTTONS_BY);
            for (int i = 0; i < dayButtons.size(); i++) {
                if (!dayButtons.get(i).findElements(DATE_PICKER_TODAY_LABEL_XPATH).isEmpty()) {
                    todayIndex = i;
                    break;
                }
            }
            if (todayIndex == -1) {
                WebElement prevMonthButton = driverTools.waitForElementToBeClickable(DATE_PICKER_PREV_MONTH_BUTTON, 2)
                        .orElseThrow(() -> new NoSuchElementException("Could not find the 'Today' element and no 'previous month' button is available to step back to it."));
                driverTools.click(prevMonthButton);
            }
        }
        if (todayIndex == -1) {
            throw new NoSuchElementException("Could not find the 'Today' element in the date picker calendar.");
        }
        clickDayAndCloseModal(dayButtons, todayIndex + daysFromToday);
    }

    // Opens the date-picker modal and clicks the day that is `daysAfterLastDisabledDay` after the last disabled (unselectable) day button.
    // Used for the end date: the app disables every day up to and including the selected start date, so the last disabled
    // day IS the start date, making this a reliable anchor regardless of where "Today" falls relative to that restriction.
    private void pickDateOffsetFromLastDisabledDay(Locator dateInputLocator, int daysAfterLastDisabledDay) {
        openDatePickerModal(dateInputLocator);
        List<WebElement> dayButtons = driverTools.getDriver().findElements(DATE_PICKER_MODAL_DAY_BUTTONS_BY);

        int lastDisabledIndex = -1;
        for (int i = 0; i < dayButtons.size(); i++) {
            boolean disabled = dayButtons.get(i).getAttribute("disabled") != null;
            if (disabled) {
                lastDisabledIndex = i;
            } else if (lastDisabledIndex != -1) {
                break;
            }
        }
        if (lastDisabledIndex == -1) {
            throw new NoSuchElementException("Could not find a disabled day in the date picker calendar to anchor from.");
        }
        clickDayAndCloseModal(dayButtons, lastDisabledIndex + daysAfterLastDisabledDay);
    }

    public void selectBookingPeriodStartDate(int daysFromToday) {
        pickDateOffsetFromToday(BOOKING_PERIOD_START_DATE_INPUT, daysFromToday);
    }

    public void selectBookingPeriodEndDate(int daysAfterStartDate) {
        // The last disabled day is the day BEFORE the start date (the start date itself stays enabled/selected),
        // so +1 lands on the start date and the rest of the offset counts forward from there.
        pickDateOffsetFromLastDisabledDay(BOOKING_PERIOD_END_DATE_INPUT, daysAfterStartDate + 1);
    }

    public void selectBookingPeriodEndDateMonthsFromToday(int monthsFromToday) {
        selectDateMonthsFromToday(BOOKING_PERIOD_END_DATE_INPUT, monthsFromToday);
    }

    // Clicks the toggle only if it currently reads "Select all"/"Pilih semua", so already-selected days are left as-is.
    public void selectAllBookingPeriodDays() {
        WebElement toggle = driverTools.waitForElementToAppear(BOOKING_PERIOD_SELECT_DAYS_TOGGLE).orElseThrow();
        String toggleText = toggle.getText().trim();
        if (toggleText.equalsIgnoreCase("Select all") || toggleText.equalsIgnoreCase("Pilih semua")) {
            driverTools.click(toggle);
        }
    }

    // The time field isn't a plain text input: clicking it opens a spinner popup with separate hour/minute
    // segment inputs and a "Simpan"/Save button that must be clicked to commit the value.
    private void setTimeViaPicker(Locator timeFieldLocator, String time) {
        String[] parts = time.split(":");
        WebElement timeField = driverTools.waitForElementToAppear(timeFieldLocator).orElseThrow();
        driverTools.scrollElementToCenter(timeField);
        driverTools.clickWithJavascriptExecutor(timeField);

        driverTools.waitForElementToAppear(TIME_PICKER_POPUP).orElseThrow(() -> new NoSuchElementException("Time picker popup did not open."));
        List<WebElement> segmentInputs = driverTools.getDriver().findElements(TIME_PICKER_SEGMENT_INPUTS_BY);
        if (segmentInputs.size() < 2) {
            throw new NoSuchElementException("Expected hour and minute segment inputs in the time picker, found " + segmentInputs.size());
        }
        driverTools.clearInput(segmentInputs.get(0));
        driverTools.sendKeys(segmentInputs.get(0), parts[0]);
        driverTools.clearInput(segmentInputs.get(1));
        driverTools.sendKeys(segmentInputs.get(1), parts[1]);

        WebElement saveButton = driverTools.waitForElementToBeClickable(TIME_PICKER_SAVE_BUTTON).orElseThrow();
        driverTools.click(saveButton);
    }

    public void fillOutBookingPeriodStartTime(String time) {
        setTimeViaPicker(BOOKING_PERIOD_START_TIME_INPUT, time);
    }

    public void fillOutBookingPeriodEndTime(String time) {
        setTimeViaPicker(BOOKING_PERIOD_END_TIME_INPUT, time);
    }

    public void verifyBookingPeriodTimesMatch(String expectedStartTime, String expectedEndTime) {
        String actualStartTime = driverTools.waitForElementToAppear(BOOKING_PERIOD_START_TIME_INPUT).orElseThrow().getAttribute("value");
        Assertion.assertThat("Verify Booking Period start time. Expected: " + expectedStartTime + " | Actual: " + actualStartTime, actualStartTime, is(expectedStartTime));
        String actualEndTime = driverTools.waitForElementToAppear(BOOKING_PERIOD_END_TIME_INPUT).orElseThrow().getAttribute("value");
        Assertion.assertThat("Verify Booking Period end time. Expected: " + expectedEndTime + " | Actual: " + actualEndTime, actualEndTime, is(expectedEndTime));
    }

    // Reuses the same date range logic as Booking Period, scoped to the Stay Period section, so passing the
    // same offsets lands on the identical dates ("select the same date range as selected in Booking Period").
    public void selectStayPeriodStartDate(int daysFromToday) {
        pickDateOffsetFromToday(STAY_PERIOD_START_DATE_INPUT, daysFromToday);
    }

    public void selectStayPeriodEndDate(int daysAfterStartDate) {
        pickDateOffsetFromLastDisabledDay(STAY_PERIOD_END_DATE_INPUT, daysAfterStartDate + 1);
    }

    public void selectStayPeriodEndDateMonthsFromToday(int monthsFromToday) {
        selectDateMonthsFromToday(STAY_PERIOD_END_DATE_INPUT, monthsFromToday);
    }

    public void selectSpecificStayPeriod() {
        WebElement radio = driverTools.waitForElementToAppear(SPECIFIC_STAY_PERIOD_RADIO).orElseThrow();
        driverTools.scrollElementToCenter(radio);
        driverTools.click(radio);
    }

    public void selectCancellationPolicyYes() {
        WebElement radio = driverTools.waitForElementToAppear(CANCELLATION_POLICY_YES_RADIO).orElseThrow();
        driverTools.scrollElementToCenter(radio);
        driverTools.click(radio);
    }

    public void selectNonRefundablePolicy() {
        WebElement radio = driverTools.waitForElementToAppear(NON_REFUNDABLE_POLICY_RADIO).orElseThrow();
        driverTools.scrollElementToCenter(radio);
        driverTools.click(radio);
    }

    public void selectCustomPolicy() {
        WebElement radio = driverTools.waitForElementToAppear(CUSTOM_POLICY_RADIO).orElseThrow();
        driverTools.scrollElementToCenter(radio);
        driverTools.click(radio);
    }

    public boolean isNonRefundablePolicySelected() {
        return driverTools.waitForElementToPresence(NON_REFUNDABLE_POLICY_INPUT).orElseThrow().isSelected();
    }

    public boolean isCustomPolicySelected() {
        return driverTools.waitForElementToPresence(CUSTOM_POLICY_INPUT).orElseThrow().isSelected();
    }

    public void fillOutCancellationDaysBefore(String days) {
        WebElement input = driverTools.waitForElementToAppear(CANCELLATION_DAYS_BEFORE_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(input);
        driverTools.clearInput(input);
        driverTools.sendKeys(input, days);
    }

    public void fillOutCancellationPercentage(String percentage) {
        WebElement input = driverTools.waitForElementToAppear(CANCELLATION_CHARGE_VALUE_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(input);
        driverTools.clearInput(input);
        driverTools.sendKeys(input, percentage);
    }

    public void fillOutNoShowPercentage(String percentage) {
        WebElement input = driverTools.waitForElementToAppear(NO_SHOW_VALUE_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(input);
        driverTools.clearInput(input);
        driverTools.sendKeys(input, percentage);
    }

    public void verifyCancellationPolicySummaryBanner(String days, String cancellationPercentage, String noShowPercentage) {
        WebElement banner = driverTools.waitForElementToAppear(CANCELLATION_POLICY_SUMMARY_BANNER).orElseThrow(() -> new NoSuchElementException("Cancellation policy summary banner not found"));
        String bannerText = banner.getText();
        Assertion.assertThat("Verify summary banner shows the cancellation days before check-in. Actual: " + bannerText, bannerText, containsString(days));
        Assertion.assertThat("Verify summary banner shows the cancellation percentage. Actual: " + bannerText, bannerText, containsString(cancellationPercentage + "%"));
        Assertion.assertThat("Verify summary banner shows the no-show percentage. Actual: " + bannerText, bannerText, containsString(noShowPercentage + "%"));
    }

    public void turnOnCombinablePromoToggle() {
        WebElement toggle = driverTools.waitForElementToAppear(COMBINABLE_PROMO_TOGGLE_CHECKBOX).orElseThrow();
        driverTools.scrollElementToCenter(toggle);
        driverTools.click(toggle);
    }

    public void turnOnBlackoutDatesToggle() {
        WebElement toggle = driverTools.waitForElementToAppear(BLACKOUT_DATES_TOGGLE_CHECKBOX).orElseThrow();
        driverTools.scrollElementToCenter(toggle);
        driverTools.click(toggle);
    }

    // Re-locates right before each click, rather than reusing a located WebElement - clicking advances the
    // calendar's displayed month, which re-renders the button and would leave a prior reference stale.
    private void clickBlackoutCalendarNextMonthButton() {
        driverTools.waitForElementToBeClickable(BLACKOUT_CALENDAR_NEXT_MONTH_BUTTON).orElseThrow();
        driverTools.scrollElementToCenter(BLACKOUT_CALENDAR_NEXT_MONTH_BUTTON);
        WebElement button = driverTools.waitForElementToBeClickable(BLACKOUT_CALENDAR_NEXT_MONTH_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(button);
    }

    // Parses a calendar month heading (e.g. "Oktober 2026" / "October 2026") into an actual YearMonth, independent
    // of the UI's active locale.
    private YearMonth parseMonthHeading(String headingText) {
        String[] parts = headingText.trim().split("\\s+");
        Integer month = MONTH_NAME_TO_NUMBER.get(parts[0].toLowerCase());
        if (month == null) {
            throw new IllegalStateException("Unrecognized month name in calendar heading: " + headingText);
        }
        int year = Integer.parseInt(parts[1]);
        return YearMonth.of(year, month);
    }

    // Reads the actual month/year straight off the day button's own month section heading instead of assuming how
    // far "N clicks of next month" moves the view - whether a calendar slides a fixed window or keeps accumulating
    // already-seen months underneath is an implementation detail we don't want to depend on, and guessing it wrong
    // silently computes the wrong date.
    private LocalDate resolveDayButtonDate(WebElement dayButton) {
        WebElement section = dayButton.findElement(By.xpath("ancestor::section[contains(@class,'CalendarDesktop_month__')]"));
        YearMonth yearMonth = parseMonthHeading(section.findElement(MONTH_HEADING_XPATH).getText());
        int dayNumber = Integer.parseInt(dayButton.findElement(DAY_NUMBER_XPATH).getText().trim());
        return yearMonth.atDay(dayNumber);
    }

    // Clicks forward through the date-picker modal's calendar until the target month is reached, then clicks the
    // day matching targetDate's day-of-month. Used to reach dates far beyond the currently rendered view (e.g. a
    // date 6 months out), which the "Today"/"last disabled day" anchored pickers can't reach on their own since
    // they only ever step backward to find their anchor, never forward.
    private void selectDateMonthsFromToday(Locator dateInputLocator, int monthsFromToday) {
        openDatePickerModal(dateInputLocator);
        LocalDate targetDate = LocalDate.now(CoreFunctions.JAKARTA_ZONE).plusMonths(monthsFromToday);
        YearMonth targetMonth = YearMonth.from(targetDate);

        WebElement targetDayButton = null;
        for (int attempt = 0; attempt < 24 && targetDayButton == null; attempt++) {
            List<WebElement> monthSections = driverTools.getDriver().findElements(DATE_PICKER_MONTH_SECTIONS_BY);
            for (WebElement section : monthSections) {
                YearMonth sectionMonth = parseMonthHeading(section.findElement(MONTH_HEADING_XPATH).getText());
                if (!sectionMonth.equals(targetMonth)) {
                    continue;
                }
                for (WebElement day : section.findElements(By.xpath(".//button[contains(@class,'Day_day__')]"))) {
                    List<WebElement> dayNumberEls = day.findElements(DAY_NUMBER_XPATH);
                    if (dayNumberEls.isEmpty()) {
                        continue;
                    }
                    int dayNumber = Integer.parseInt(dayNumberEls.get(0).getText().trim());
                    if (dayNumber == targetDate.getDayOfMonth() && day.getAttribute("disabled") == null) {
                        targetDayButton = day;
                        break;
                    }
                }
                break;
            }
            if (targetDayButton == null) {
                WebElement nextMonthButton = driverTools.waitForElementToBeClickable(DATE_PICKER_NEXT_MONTH_BUTTON, 2)
                        .orElseThrow(() -> new NoSuchElementException("Could not find target month " + targetMonth + " and no 'next month' button is available to advance to it."));
                driverTools.clickWithJavascriptExecutor(nextMonthButton);
            }
        }
        if (targetDayButton == null) {
            throw new NoSuchElementException("Could not find date " + targetDate + " in the date picker calendar.");
        }
        driverTools.clickWithJavascriptExecutor(targetDayButton);
        driverTools.waitForElementToDisappear(DATE_PICKER_MODAL, 5);
    }

    // Selects `count` random enabled days that are at least 2 months ahead from the inline blackout calendar,
    // and returns the actual calendar dates selected (yyyy-MM-dd) so a caller can use one for API verification.
    public List<String> selectRandomBlackoutDates(int count) {
        clickBlackoutCalendarNextMonthButton();
        clickBlackoutCalendarNextMonthButton();

        YearMonth earliestEligibleMonth = YearMonth.now(CoreFunctions.JAKARTA_ZONE).plusMonths(2);
        List<WebElement> dayButtons = driverTools.getDriver().findElements(BLACKOUT_CALENDAR_DAY_BUTTONS_BY);
        List<WebElement> eligibleDayButtons = new ArrayList<>();
        Map<WebElement, LocalDate> dateByButton = new HashMap<>();
        for (WebElement button : dayButtons) {
            if (button.getAttribute("disabled") != null) {
                continue;
            }
            LocalDate date = resolveDayButtonDate(button);
            if (!YearMonth.from(date).isBefore(earliestEligibleMonth)) {
                eligibleDayButtons.add(button);
                dateByButton.put(button, date);
            }
        }
        if (eligibleDayButtons.isEmpty()) {
            throw new NoSuchElementException("No enabled days at least 2 months ahead found in the blackout dates calendar.");
        }
        Collections.shuffle(eligibleDayButtons);
        int actualCount = Math.min(count, eligibleDayButtons.size());
        List<String> selectedDates = new ArrayList<>();
        for (int i = 0; i < actualCount; i++) {
            WebElement day = eligibleDayButtons.get(i);
            selectedDates.add(dateByButton.get(day).format(DateTimeFormatter.ISO_LOCAL_DATE));
            driverTools.scrollElementToCenter(day);
            driverTools.clickWithJavascriptExecutor(day);
        }
        return selectedDates;
    }

    public void verifyBlackoutDatesListIsScrollable() {
        WebElement scrollContainer = driverTools.waitForElementToAppear(BLACKOUT_SELECTED_DATES_SCROLL_CONTAINER).orElseThrow(() -> new NoSuchElementException("Selected blackout dates list container not found"));
        long scrollHeight = (long) ((JavascriptExecutor) driverTools.getDriver()).executeScript("return arguments[0].scrollHeight;", scrollContainer);
        long clientHeight = (long) ((JavascriptExecutor) driverTools.getDriver()).executeScript("return arguments[0].clientHeight;", scrollContainer);
        Assertion.assertThat("Verify the selected blackout dates list is scrollable. scrollHeight=" + scrollHeight + " clientHeight=" + clientHeight, scrollHeight > clientHeight, is(true));
    }

    public void scrollSelectedBlackoutDatesListToBottom() {
        WebElement scrollContainer = driverTools.waitForElementToAppear(BLACKOUT_SELECTED_DATES_SCROLL_CONTAINER).orElseThrow(() -> new NoSuchElementException("Selected blackout dates list container not found"));
        ((JavascriptExecutor) driverTools.getDriver()).executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", scrollContainer);
    }

    public void verifySelectedBlackoutDatesCount(int expectedCount) {
        List<WebElement> selectedItems = driverTools.getDriver().findElements(BLACKOUT_SELECTED_DATES_LIST_ITEMS.by());
        int actualCount = selectedItems.size();
        Assertion.assertThat("Verify the number of selected blackout dates. Expected: " + expectedCount + " | Actual: " + actualCount, actualCount, is(expectedCount));
    }

    public void clickEarlyBirdReviewButton() {
        WebElement button = driverTools.waitForElementToBeClickable(EARLY_BIRD_REVIEW_BUTTON).orElseThrow();
        driverTools.scrollElementToCenter(button);
        driverTools.click(button);
    }

    public void verifyPromoNameInReviewModal(String promoName) {
        Locator promoNameInModal = new Locator("REVIEW_MODAL_PROMO_NAME", new By.ByXPath(String.format("//div[contains(@class,'Dialog_pop_up')]//b[contains(text(), \"%s\")]", promoName)));
        WebElement element = driverTools.waitForElementToAppear(promoNameInModal).orElseThrow(() -> new NoSuchElementException("Promo name not found in the review modal subtitle"));
        Assertion.assertThat("Verify promo name is displayed in the review modal subtitle", element.getText(), containsString(promoName));
    }

    public void verifyEarlyBirdComponentInReviewModal(String expectedDiscountPercentage) {
        WebElement row = driverTools.waitForElementToAppear(REVIEW_MODAL_EARLY_BIRD_COMPONENT_ROW).orElseThrow(() -> new NoSuchElementException("Early Bird component row not found in the review modal's price breakdown"));
        String rowText = row.getText();
        Assertion.assertThat("Verify the Early Bird component label is present in the review modal's price breakdown (shown in green)", rowText, containsString("Early Bird"));
        String expectedPercentageText = "-" + expectedDiscountPercentage + "%";
        Assertion.assertThat("Verify the Early Bird component shows the correct discount percentage. Actual row text: " + rowText, rowText, containsString(expectedPercentageText));
    }

    public void clickEditPromoInReviewModal() {
        WebElement button = driverTools.waitForElementToBeClickable(REVIEW_MODAL_EDIT_BUTTON).orElseThrow();
        driverTools.click(button);
        driverTools.waitForElementToDisappear(REVIEW_MODAL, 5);
    }

    public void clickCreatePromoInReviewModal() {
        WebElement button = driverTools.waitForElementToBeClickable(REVIEW_MODAL_CREATE_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void verifyEarlyBirdFormStillFilled(String expectedPromoName) {
        Assertion.assertThat("Verify the user is navigated back to the Early Bird Offer page", isEarlyBirdOfferPageLoaded(), is(true));
        String actualPromoName = driverTools.waitForElementToAppear(PROMO_NAME_INPUT).orElseThrow().getAttribute("value");
        Assertion.assertThat("Verify the promo name is still filled in. Expected: " + expectedPromoName + " | Actual: " + actualPromoName, actualPromoName, is(expectedPromoName));
    }

}
