package com.tiket.web.dashboard.page.platform.eiffel.loyalty;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.web.WebPage;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * New Mission form of the Loyalty admin panel's Mission Config page
 * ({@code /admin/care/loyalty/mission-config/form}), step 1 ("Scheme"), where the optional
 * BIN allow-list under Payment Method is configured.
 *
 * <p><b>Start Date must be a genuine future date.</b> The calendar lets "Today" be picked, but
 * {@code POST .../tix-mission/api/admin/mission/data/validate} (fired by Continue) rejects it with
 * {@code "startAt: Mission start date must be in the future."} — and Continue then silently stays
 * on step 1 with no on-page error or toast. {@link #selectStartDate(int)} must be given a strictly
 * positive day offset, and also pushes the field's own Time forward a few minutes (see
 * {@link #pushStartTimeForward(int)}) so the timestamp doesn't go stale between selection and submit.
 */
public class MissionConfigFormPage<T extends WebDriver> extends WebPage<T> {

    private static final String MISSION_CONFIG_LIST_URL = "/admin/care/loyalty/mission-config";

    private static final int DEFAULT_WAIT_TIMEOUT = 20;

    /**
     * The New Mission form's first paint is the slowest step in this page: on a cold browser
     * profile (no cached chunks — always the case in a fresh test session) it loads several
     * JS/CSS chunks and blocks on two API calls (point-types, payment-source-list) before
     * Mission Name renders. 20s is enough once assets are cached but has been observed to time
     * out on a cold run, so this transition alone gets a longer allowance.
     */
    private static final int FORM_LOAD_WAIT_TIMEOUT = 45;

    /**
     * Allowance for the BIN combobox's async lookup (debounce + GET bankBin + repaint). Measured at
     * ~1.2–1.5s on preprod, but this must never be a fixed sleep: while the request is in flight the
     * menu renders <em>nothing at all</em> — no option and no message — so typing and then acting
     * after a fixed pause silently selects nothing whenever the call runs long. Every BIN step waits
     * for the menu to actually settle instead, and only uses this as an upper bound.
     */
    private static final int BIN_LOOKUP_TIMEOUT = 20;

    /**
     * How far the Start Date's Time field is nudged past whatever it is showing when the picker
     * opens (it defaults to "now"). A future calendar day alone isn't enough: {@code validate}
     * checks the full timestamp, so if the shown time has slipped into the past by the moment
     * Continue posts — the rest of the form takes a while to fill — the same future-date rejection
     * from the class-level note fires on the time component instead.
     */
    private static final int START_TIME_BUFFER_MINUTES = 5;

    /** Menu copy when the typed BIN is shorter than the lookup's minimum searchable length. */
    public static final String BIN_MIN_LENGTH_HINT = "type at least 6 more character(s)";
    /** Menu copy when a correctly-sized BIN matches nothing in the bank BIN list. */
    public static final String BIN_NO_RESULTS_MESSAGE = "No results matched your search";

    private static final DateTimeFormatter CALENDAR_DAY_FORMAT =
            DateTimeFormatter.ofPattern("d MMMM yyyy EEEE", Locale.ENGLISH);

    /** The mission fields this page treats as boilerplate — not the BIN feature under test. */
    public record RequiredMissionDetails(String missionName, String slug, String labelTextEn, String labelTextId,
                                          String totalOrdersOrStampsRequired, String numberOfFreeStamps,
                                          String rewardTitleEn, String rewardTitleId, String pointType,
                                          String amount, String rewardQuota) {
    }

    private static final Locator CREATE_NEW_BUTTON = new Locator("CREATE_NEW_BUTTON",
            By.xpath("//button[normalize-space()='Create new']"));
    private static final Locator LOADING_OVERLAY = new Locator("LOADING_OVERLAY",
            By.cssSelector("[class*='LoadingWithIllustration_container']"));

    private static final Locator MISSION_NAME_INPUT = new Locator("MISSION_NAME_INPUT", By.id("mission-name"));
    private static final Locator SLUG_INPUT = new Locator("SLUG_INPUT", By.id("slug"));
    private static final Locator LABEL_TEXT_EN_INPUT = new Locator("LABEL_TEXT_EN_INPUT", By.id("label-text-en"));
    private static final Locator LABEL_TEXT_ID_INPUT = new Locator("LABEL_TEXT_ID_INPUT", By.id("label-text-id"));
    private static final Locator START_DATE_INPUT = new Locator("START_DATE_INPUT", By.id("start-date"));
    private static final Locator END_DATE_INPUT = new Locator("END_DATE_INPUT", By.id("end-date"));
    private static final Locator TOTAL_ORDERS_INPUT = new Locator("TOTAL_ORDERS_INPUT",
            By.id("total-orders-or-stamps-required"));
    private static final Locator NUMBER_OF_FREE_STAMPS_INPUT = new Locator("NUMBER_OF_FREE_STAMPS_INPUT",
            By.id("number-of-free-stamps"));
    private static final Locator REWARD_TITLE_EN_INPUT = new Locator("REWARD_TITLE_EN_INPUT", By.id("reward-title-en"));
    private static final Locator REWARD_TITLE_ID_INPUT = new Locator("REWARD_TITLE_ID_INPUT", By.id("reward-title-id"));
    private static final Locator POINT_TYPES_FIELD = new Locator("POINT_TYPES_FIELD", By.id("point-types"));
    private static final Locator AMOUNT_INPUT = new Locator("AMOUNT_INPUT", By.id("amount"));
    private static final Locator REWARD_QUOTA_INPUT = new Locator("REWARD_QUOTA_INPUT", By.id("reward-quota"));

    /**
     * Start Date's own "Time" field. Its {@code id="time"} is duplicated by End Date's identical
     * field, so it is anchored to the shared date+time wrapper that is unique per side rather than
     * to the id alone.
     */
    private static final Locator START_TIME_INPUT = new Locator("START_TIME_INPUT",
            By.xpath("//input[@id='start-date']/ancestor::div[contains(@class,'InputDate_with_time_container')]"
                    + "//input[@id='time']"));
    /**
     * The floating time picker's hour and minute segments, in DOM order. It is a single shared
     * portal (only one instance exists while open), so no per-field scoping is needed once it is
     * confirmed open.
     */
    private static final Locator TIME_PICKER_HOUR_INPUT = new Locator("TIME_PICKER_HOUR_INPUT",
            By.xpath("(//div[contains(@class,'TimePickerDesktop_time_segment')])[1]//input"));
    private static final Locator TIME_PICKER_MINUTE_INPUT = new Locator("TIME_PICKER_MINUTE_INPUT",
            By.xpath("(//div[contains(@class,'TimePickerDesktop_time_segment')])[2]//input"));
    private static final Locator TIME_PICKER_SAVE_BUTTON = new Locator("TIME_PICKER_SAVE_BUTTON",
            By.cssSelector("[class*='PickerAction_picker_action'] button"));

    private static final Locator PAYMENT_METHOD_FIELD = new Locator("PAYMENT_METHOD_FIELD", By.id("payment-method"));
    /**
     * The visible {@code <label>} and an sr-only {@code styles_invisible_label} div both carry this
     * exact text, so matching any tag ({@code //*}) hits both; the label is the one this page asserts on.
     */
    private static final Locator BIN_OPTIONAL_LABEL = new Locator("BIN_OPTIONAL_LABEL",
            By.xpath("//label[normalize-space(text())='BIN (Optional)']"));
    /**
     * Scoped to the Payment Method / BIN section rather than the bare {@code .input_select__control}
     * class, which every react-select combobox on this form shares.
     */
    private static final Locator BIN_INPUT = new Locator("BIN_INPUT",
            By.cssSelector("[class*='SchemeCommonReward_payment_setting_container'] .input_select__control input"));
    /**
     * Matches only the outer chip {@code div.input_select__multi-value}, not its
     * {@code __label}/{@code __remove} children — both of those also contain
     * "input_select__multi-value" in their class name, so excluding just "remove" here
     * doubled every real selection.
     */
    private static final Locator BIN_CHIPS = new Locator("BIN_CHIPS",
            By.xpath("//div[contains(@class,'input_select__multi-value') and not(contains(@class,'input_select__multi-value__'))]"));
    private static final Locator BIN_MENU_MESSAGE = new Locator("BIN_MENU_MESSAGE",
            By.cssSelector("[class*='styles_no_options_message']"));
    private static final Locator BIN_MENU = new Locator("BIN_MENU", By.cssSelector("div.input_select__menu"));

    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON",
            By.cssSelector("[data-testid='continue-btn']"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", By.cssSelector("[data-testid='back-btn']"));
    /** The step indicator's "Content" label picks up this styling only once step 2 is current. */
    private static final Locator CONTENT_STEP_ACTIVE = new Locator("CONTENT_STEP_ACTIVE",
            By.xpath("//*[contains(@class,'Step_title') and contains(@class,'Text_variant_highEmphasis')"
                    + " and normalize-space(text())='Content']"));

    public MissionConfigFormPage(Session<T> session) {
        super(session);
    }

    public void goToCreateNewMissionForm(String userName, String password) {
        MemberLoginPage<T> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(userName, password);
        memberLoginPage.goToPage(MISSION_CONFIG_LIST_URL);
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, DEFAULT_WAIT_TIMEOUT);
        driverTools.click(CREATE_NEW_BUTTON);
        driverTools.waitForElementToAppear(MISSION_NAME_INPUT, FORM_LOAD_WAIT_TIMEOUT).orElseThrow();
    }

    public boolean isBinFieldLabelledOptional() {
        return driverTools.isElementDisplayed(BIN_OPTIONAL_LABEL);
    }

    public void fillRequiredMissionDetails(RequiredMissionDetails details) {
        driverTools.sendKeys(MISSION_NAME_INPUT, details.missionName());
        driverTools.sendKeys(SLUG_INPUT, details.slug());
        driverTools.sendKeys(LABEL_TEXT_EN_INPUT, details.labelTextEn());
        driverTools.sendKeys(LABEL_TEXT_ID_INPUT, details.labelTextId());
        driverTools.sendKeys(TOTAL_ORDERS_INPUT, details.totalOrdersOrStampsRequired());
        driverTools.sendKeys(NUMBER_OF_FREE_STAMPS_INPUT, details.numberOfFreeStamps());
        driverTools.sendKeys(REWARD_TITLE_EN_INPUT, details.rewardTitleEn());
        driverTools.sendKeys(REWARD_TITLE_ID_INPUT, details.rewardTitleId());
        selectPointType(details.pointType());
        driverTools.sendKeys(AMOUNT_INPUT, details.amount());
        driverTools.sendKeys(REWARD_QUOTA_INPUT, details.rewardQuota());
    }

    /**
     * Selects the day, then pushes the field's own Time {@value #START_TIME_BUFFER_MINUTES} minutes
     * ahead of whatever it is currently showing — see {@link #pushStartTimeForward(int)}.
     *
     * @param daysFromToday must be at least 1 — see the class-level note on the Start Date bug
     */
    public void selectStartDate(int daysFromToday) {
        selectCalendarDate(START_DATE_INPUT, daysFromToday);
        pushStartTimeForward(START_TIME_BUFFER_MINUTES);
    }

    public void selectEndDate(int daysFromToday) {
        selectCalendarDate(END_DATE_INPUT, daysFromToday);
    }

    /**
     * Ticks a payment method in the dropdown and closes it again.
     *
     * <p>The option row carries React's {@code onClick}; the {@code <label>} nested inside it wraps
     * both the checkbox and its text, so a pointer click anywhere on that label reaches the handler
     * twice (once directly, once via the click the label forwards to its checkbox) and the two
     * toggles cancel out. Clicking the row through the JS executor dispatches exactly one event and
     * sidesteps that, whatever the label's width. Closing is likewise a click on the field rather
     * than an Escape key, because {@code #payment-method} is {@code readonly} and WebDriver refuses
     * to send keys to it.
     *
     * <p>Note this only drives the widget: on preprod the mission payload still leaves
     * {@code challenge.payments} empty however the option is ticked, so nothing here asserts that a
     * payment method was actually applied.
     */
    public void selectPaymentMethod(String paymentMethodLabel) {
        driverTools.click(PAYMENT_METHOD_FIELD);
        Locator option = dropdownOption(paymentMethodLabel);
        driverTools.waitForElementToAppear(option, DEFAULT_WAIT_TIMEOUT).orElseThrow();
        driverTools.clickWithJavascriptExecutor(option);
        driverTools.click(PAYMENT_METHOD_FIELD);
        driverTools.waitForElementToDisappear(option, DEFAULT_WAIT_TIMEOUT);
    }

    /**
     * Types a BIN into the combobox and waits for its lookup to settle — that is, until the menu
     * shows either a matching option or its feedback message. Both are real end states; the
     * in-flight state shows neither, so this never returns mid-request.
     *
     * <p>A space inside {@code bin} never reaches the field — the widget silently drops that
     * keystroke rather than rejecting it after the fact — so a BIN "with an internal space" ends up
     * typed as its digits with no gap; {@link #getBinInputValue()} confirms this.
     */
    public void typeBin(String bin) {
        driverTools.click(BIN_INPUT);
        driverTools.sendKeys(BIN_INPUT, bin);
        driverTools.waitForAnyLocator(BIN_LOOKUP_TIMEOUT, binOption(bin), BIN_MENU_MESSAGE)
                .orElseThrow(() -> new IllegalStateException("BIN lookup never settled for '" + bin
                        + "' — the menu showed neither an option nor a feedback message within "
                        + BIN_LOOKUP_TIMEOUT + "s"));
    }

    /** @return the BIN combobox's current raw input text */
    public String getBinInputValue() {
        return driverTools.getValue(BIN_INPUT);
    }

    /**
     * @return the menu's feedback copy when no BIN can be selected — either {@link #BIN_MIN_LENGTH_HINT}
     *         for an input shorter than the lookup's minimum length, or {@link #BIN_NO_RESULTS_MESSAGE}
     *         for a correctly-sized BIN with no lookup match. Empty once a real option is on offer.
     */
    public String getBinMenuMessage() {
        return driverTools.getText(BIN_MENU_MESSAGE).orElse("");
    }

    /**
     * Selects the BIN the lookup is offering for {@code bin} and waits for its chip to appear.
     *
     * <p>The option is clicked rather than confirmed with Enter: Enter is a no-op whenever the menu
     * has not finished loading, which silently drops the selection instead of failing, and was the
     * cause of the intermittent "1 chip instead of 2". Clicking a located option cannot half-happen,
     * and waiting for the chip proves the selection reached React state.
     */
    public void selectBinOption(String bin) {
        Locator option = binOption(bin);
        driverTools.waitForElementToAppear(option, BIN_LOOKUP_TIMEOUT)
                .orElseThrow(() -> new IllegalStateException("BIN lookup offered no option for: " + bin));
        driverTools.click(option);
        driverTools.waitForElementToAppear(binChip(bin), DEFAULT_WAIT_TIMEOUT)
                .orElseThrow(() -> new IllegalStateException("BIN was clicked but no chip was added for: " + bin));
    }

    /**
     * Closes the BIN menu without selecting anything. Waits for the menu to actually go, because the
     * same Escape is what clears the typed text — returning early would leave the next
     * {@link #typeBin(String)} appending onto the previous query.
     */
    public void closeBinMenu() {
        driverTools.sendKeys(BIN_INPUT, Keys.ESCAPE.toString());
        driverTools.waitForElementToDisappear(BIN_MENU, DEFAULT_WAIT_TIMEOUT);
    }

    /** @return the selected BIN chips' visible text, e.g. {@code "<bin> - <cardScheme> - <issuerAlias> - <cardName>"} */
    public List<String> getSelectedBins() {
        return driverTools.getTexts(BIN_CHIPS);
    }

    /** @return how many BIN chips are currently selected — the same source {@link #getSelectedBins()} reads. */
    public int getSelectedBinCount() {
        return getSelectedBins().size();
    }

    /**
     * Waits until the BIN field is showing exactly {@code expectedCount} chips. The chips are
     * restored from React state on a remount (e.g. after {@link #clickBack()}), so they lag the
     * rest of the form by a repaint; reading them immediately after a step transition can otherwise
     * catch the field mid-restore.
     */
    public void waitForSelectedBinCount(int expectedCount) {
        driverTools.waitUntil(d -> getSelectedBinCount() == expectedCount ? Boolean.TRUE : null, DEFAULT_WAIT_TIMEOUT)
                .orElseThrow(() -> new IllegalStateException("Expected " + expectedCount
                        + " BIN chip(s) but the field settled on " + getSelectedBinCount()));
    }

    /**
     * A BIN option in the lookup menu. Anchored on the {@code "<bin> - "} prefix of the option's
     * label so a 6-digit BIN does not also match the 8-digit BIN that extends it — {@code 42424242}
     * starts with {@code 424242} but not with {@code "424242 - "}. Spaces are stripped because the
     * field itself refuses them, so what the lookup ran on is always the space-free text.
     */
    private static Locator binOption(String bin) {
        String typedBin = bin.replace(" ", "");
        return new Locator("BIN_OPTION_" + typedBin, By.xpath(String.format(
                "//div[contains(@class,'input_select__option')][.//span[starts-with(normalize-space(text()),'%s -')]]",
                typedBin)));
    }

    /** The selected chip for one BIN — same prefix anchoring as {@link #binOption(String)}. */
    private static Locator binChip(String bin) {
        String typedBin = bin.replace(" ", "");
        return new Locator("BIN_CHIP_" + typedBin, By.xpath(String.format(
                "//div[contains(@class,'input_select__multi-value') and not(contains(@class,'input_select__multi-value__'))]"
                        + "[starts-with(normalize-space(.),'%s -')]",
                typedBin)));
    }

    /** Submits step 1 and waits for the wizard to actually land on step 2 ("Content"). */
    public void clickContinue() {
        driverTools.click(CONTINUE_BUTTON);
        driverTools.waitForElementToAppear(CONTENT_STEP_ACTIVE, DEFAULT_WAIT_TIMEOUT)
                .orElseThrow(() -> new IllegalStateException("Continue did not advance past the Scheme step. The "
                        + "validate call rejects the form silently, so check: every required field filled, Start "
                        + "Date strictly in the future, and the slug not already taken by an existing mission"));
    }

    /** Goes from step 2 ("Content") back to step 1, where the BIN selections were made. */
    public void clickBack() {
        driverTools.click(BACK_BUTTON);
        driverTools.waitForElementToAppear(MISSION_NAME_INPUT, DEFAULT_WAIT_TIMEOUT).orElseThrow();
    }

    private void selectPointType(String pointType) {
        driverTools.click(POINT_TYPES_FIELD);
        Locator option = dropdownOption(pointType);
        driverTools.waitForElementToAppear(option, DEFAULT_WAIT_TIMEOUT).orElseThrow();
        driverTools.click(option);
    }

    /**
     * An option row shared by every dropdown on this form built from the same component (Payment
     * Method, Point Types, ...). The label sits in a nested {@code <p>}, not as a direct text node
     * of the option {@code <div>}, so matching {@code text()} on the div itself would never find it.
     */
    private static Locator dropdownOption(String label) {
        return new Locator("DROPDOWN_OPTION_" + label, By.xpath(String.format(
                "//div[contains(@class,'styles_dropdown_option')]//p[normalize-space(text())='%s']", label)));
    }

    /**
     * Builds the day's accessible label (e.g. {@code "18 September 2026 Friday"}) rather than
     * hardcoding a date, so the picker resolves correctly no matter which day the suite runs on.
     * The two-month view the picker opens with is wide enough for any {@code daysFromToday} this
     * page is realistically asked for, so no "next month" navigation is needed.
     *
     * <p>Both clicks scroll first. The period fields sit near the top of a long form, so by the time
     * the rest of the mission data has been filled in they are well above the viewport; and the
     * picker then opens *below* its field, far enough down that the day cell falls past the bottom
     * edge even once the field itself is centred. The picker scrolls with the document and stays
     * open, so centring the day cell before clicking it is safe.
     */
    private void selectCalendarDate(Locator dateField, int daysFromToday) {
        String label = LocalDate.now().plusDays(daysFromToday).format(CALENDAR_DAY_FORMAT);
        driverTools.scrollAndClick(dateField);
        Locator day = calendarDay(label);
        driverTools.waitForElementToAppear(day, DEFAULT_WAIT_TIMEOUT)
                .orElseThrow(() -> new IllegalStateException("Calendar day not found: " + label));
        driverTools.scrollElementToCenter(day);
        driverTools.click(day);
    }

    private static Locator calendarDay(String accessibleLabel) {
        return new Locator("CALENDAR_DAY_" + accessibleLabel, By.xpath(String.format(
                "//span[contains(@class,'Day_day_number__') and @aria-label='%s']", accessibleLabel)));
    }

    /**
     * Opens the Start Date's Time picker and advances it {@code minutesToAdd} minutes past whatever
     * hour/minute it currently shows.
     *
     * <p>The picker's own spin buttons clamp at {@code :00}/{@code :59} instead of rolling into the
     * hour, so clicking the "minute up" button {@code minutesToAdd} times would silently add less
     * than intended whenever the current minute is within {@code minutesToAdd} of the hour boundary.
     * Reading the picker's own hour/minute text, computing the target with {@link LocalTime}, and
     * typing both segments directly sidesteps that and also handles the hour rolling over.
     */
    private void pushStartTimeForward(int minutesToAdd) {
        driverTools.click(START_TIME_INPUT);
        driverTools.waitForElementToAppear(TIME_PICKER_SAVE_BUTTON, DEFAULT_WAIT_TIMEOUT).orElseThrow();
        LocalTime current = LocalTime.of(
                Integer.parseInt(driverTools.getValue(TIME_PICKER_HOUR_INPUT)),
                Integer.parseInt(driverTools.getValue(TIME_PICKER_MINUTE_INPUT)));
        LocalTime target = current.plusMinutes(minutesToAdd);
        driverTools.deleteUsingBackspace(TIME_PICKER_HOUR_INPUT);
        driverTools.sendKeys(TIME_PICKER_HOUR_INPUT, String.format("%02d", target.getHour()));
        driverTools.deleteUsingBackspace(TIME_PICKER_MINUTE_INPUT);
        driverTools.sendKeys(TIME_PICKER_MINUTE_INPUT, String.format("%02d", target.getMinute()));
        driverTools.click(TIME_PICKER_SAVE_BUTTON);
        driverTools.waitForElementToDisappear(TIME_PICKER_SAVE_BUTTON, DEFAULT_WAIT_TIMEOUT);
    }
}
