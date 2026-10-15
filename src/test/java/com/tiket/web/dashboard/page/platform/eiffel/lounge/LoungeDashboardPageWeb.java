package com.tiket.web.dashboard.page.platform.eiffel.lounge;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class LoungeDashboardPageWeb<T extends WebDriver> extends WebPage<T> {
    public LoungeDashboardPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> LoungeDashboardPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new LoungeDashboardPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new LoungeDashboardPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator LOUNGE_MENU = new Locator("LOUNGE_MENU", By.xpath("//nav[contains(@class,'Sidebar_sidebar_nav')]//a[normalize-space()='Lounge']"));
    private static final Locator CHECK_IN_MENU = new Locator("CHECK_IN_MENU", By.xpath("//nav[contains(@class,'Sidebar_sidebar_nav')]//a[normalize-space()='Check-In']"));
    private static final Locator CHECK_IN_HISTORY_MENU = new Locator("CHECK_IN_HISTORY_MENU", By.xpath("//nav[contains(@class,'Sidebar_sidebar_nav')]//a[normalize-space()='Check-In History']"));
    private static final Locator USER_MANAGEMENT_MENU = new Locator("USER_MANAGEMENT_MENU", By.xpath("//nav[contains(@class,'Sidebar_sidebar_nav')]//a[normalize-space()='User Management']"));
    private static final Locator USER_PROFILE_DROPDOWN_MENU= new Locator("USER_PROFILE_DROPDOWN_MENU", By.cssSelector("[class*='UserProfileDropdown_dropdown_trigger']"));
    private static final Locator CHANGE_PASSWORD_MENU = new Locator("CHANGE_PASSWORD_MENU", By.xpath("//a[normalize-space()='Change Password']"));
    private static final Locator LOGOUT_MENU = new Locator("LOGOUT_MENU", By.cssSelector("a[href*='/external/logout']"));
    private static final Locator ON_SCHEDULE_FLIGHTS_CONTAINER = new Locator("ON_SCHEDULE_FLIGHTS_CONTAINER", By.cssSelector("table[class*='styles_table']"));
    private static final Locator DELAYED_FLIGHTS_CONTAINER = new Locator("DELAYED_FLIGHTS_CONTAINER", By.cssSelector("table[class*='styles_table']"));
    /**
     * Rendered in place of the flights table whenever the list is empty (no flights, or a search with no matches).
     * Keyed on the container class (not its message) so it matches every empty variant.
     */
    private static final Locator FLIGHTS_LIST_EMPTY_STATE = new Locator("FLIGHTS_LIST_EMPTY_STATE", By.cssSelector("[class*='styles_empty_container']"));
    private static final Locator CHECK_IN_PAGE_TITLE = new Locator("CHECK_IN_PAGE_TITLE", By.xpath("//h3[normalize-space()='Check-In']"));
    private static final Locator ON_SCHEDULE_TAB = new Locator("ON_SCHEDULE_TAB", By.xpath("//button[normalize-space()='On Schedule']"));
    private static final Locator DELAYED_TAB = new Locator("DELAYED_TAB", By.xpath("//button[normalize-space()='Delayed']"));
    private static final Locator LOADING_OVERLAY = new Locator("LOADING_OVERLAY", By.cssSelector("[data-testid='loading-container']"));

    private static final Locator CHECK_IN_BY_VOUCHER_BUTTON = new Locator("CHECK_IN_BY_VOUCHER_BUTTON", By.xpath("//button[normalize-space()='Check-In by Voucher']"));
    private static final Locator E_VOUCHER_MODAL = new Locator("E_VOUCHER_MODAL", By.xpath("//section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Check-in by e-Voucher']]"));
    private static final Locator E_VOUCHER_MODAL_ORDER_ID_INPUT = new Locator("E_VOUCHER_MODAL_ORDER_ID_INPUT", By.id("evouchers-order-id"));
    private static final Locator MODAL_SAVE_BUTTON = new Locator("MODAL_SAVE_BUTTON", By.xpath("//section[contains(@class,'Modal_modal_popup')]//button[normalize-space()='Save']"));
    private static final Locator CONFIRM_CHECK_IN_BUTTON = new Locator("CONFIRM_CHECK_IN_BUTTON", By.xpath("//button[contains(normalize-space(),'Confirm Check')]"));
    private static final Locator ANY_SNACKBAR = new Locator("ANY_SNACKBAR", By.cssSelector("[class*='snackbars_positioner']"));

    /** The "Manage Check In" side panel opened from a booking row (shared by every on-list check-in flow below). */
    private static final Locator MANAGE_CHECK_IN_PANEL = new Locator("MANAGE_CHECK_IN_PANEL", By.xpath("//section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Manage Check In']]"));

    /**
     * First booking with a check-in-eligible passenger: status "Available" (nobody checked in) or "Partial" (some
     * checked in, some not). A fully "Checked-In" booking has no un-checked passenger to check in, so it is excluded.
     * Extracted so the row and its booking-code cell key off exactly the same predicate and can never resolve to
     * two different rows.
     */
    private static final String ELIGIBLE_ROW_XPATH =
            "(//table//tbody//tr[.//p[normalize-space()='Available' or contains(normalize-space(),'Partial')]])[1]";
    private static final Locator FIRST_ELIGIBLE_ROW = new Locator("FIRST_ELIGIBLE_ROW", By.xpath(ELIGIBLE_ROW_XPATH));
    /**
     * Booking-code cell of that first eligible row — read before check-in so the same booking can be re-opened by
     * code afterwards (its status changes once a passenger is checked in, so a status-based row locator no longer
     * matches it). Targets the code's {@code <p>} (not the whole {@code <td>}) so the text captured here round-trips
     * exactly with the {@code .//p[normalize-space()=code]} predicate {@link #bookingRowByCode(String)} re-matches on.
     */
    private static final Locator FIRST_ELIGIBLE_ROW_BOOKING_CODE = new Locator("FIRST_ELIGIBLE_ROW_BOOKING_CODE", By.xpath(ELIGIBLE_ROW_XPATH + "//td[1]//p[1]"));
    /**
     * First booking that already carries a check-in — status "Partial" (some passengers checked in) or "Checked-In"
     * (all checked in). Such a booking may hold a cancellable *phone* check-in the cancel case can cancel directly,
     * so it does not have to create its own first. Kept as its own predicate (like {@link #ELIGIBLE_ROW_XPATH}) so
     * the row and its booking-code cell always resolve to the same row.
     */
    private static final String CHECKED_IN_ROW_XPATH =
            "(//table//tbody//tr[.//p[contains(normalize-space(),'Checked-In') or contains(normalize-space(),'Partial')]])[1]";
    private static final Locator FIRST_CHECKED_IN_ROW = new Locator("FIRST_CHECKED_IN_ROW", By.xpath(CHECKED_IN_ROW_XPATH));
    private static final Locator FIRST_CHECKED_IN_ROW_BOOKING_CODE = new Locator("FIRST_CHECKED_IN_ROW_BOOKING_CODE", By.xpath(CHECKED_IN_ROW_XPATH + "//td[1]//p[1]"));
    /** A passenger card inside the "Manage Check In" panel. A booking has one card per passenger. */
    private static final Locator PASSENGER_CARD = new Locator("PASSENGER_CARD", By.cssSelector("[class*='PassengerCard_passenger_card']"));
    /**
     * Resolved *within a passenger card* (relative xpath, subtree-scoped) — the phone input id "phone-number" is
     * duplicated across every passenger being checked in, so it must never be looked up globally. The
     * {@code CARD_*} locators below follow the same subtree-scoped rule.
     */
    private static final Locator CARD_CHECK_IN_TOGGLE = new Locator("CARD_CHECK_IN_TOGGLE", By.xpath(".//div[contains(@class,'Toggle_toggle')]"));
    private static final Locator CARD_EDIT_LABEL = new Locator("CARD_EDIT_LABEL", By.xpath(".//p[contains(@class,'PassengerCard_edit_label')][normalize-space()='Edit']"));
    private static final Locator CARD_PHONE_OPTION = new Locator("CARD_PHONE_OPTION", By.xpath(".//div[contains(@class,'PassengerCard_option')][normalize-space()='Phone Number']"));
    /**
     * Phone check-in input. Its presence in an expanded (Edit) card also identifies a *phone* check-in — the
     * cancellable kind.
     */
    private static final Locator CARD_PHONE_INPUT = new Locator("CARD_PHONE_INPUT", By.xpath(".//input[@id='phone-number']"));
    /**
     * e-Voucher check-in input. Its presence in an expanded (Edit) card identifies a *voucher* check-in, which the
     * backend does not allow to be cancelled (its toggle is even rendered disabled), so such passengers are skipped.
     */
    private static final Locator CARD_EVOUCHER_INPUT = new Locator("CARD_EVOUCHER_INPUT", By.xpath(".//input[@id='evoucher-id']"));
    /** The "reason" field that appears only after a checked-in passenger is toggled back off. */
    private static final Locator CARD_CANCEL_NOTE_INPUT = new Locator("CARD_CANCEL_NOTE_INPUT", By.xpath(".//textarea[@id='add-any-reason-why-uncheck-this-passenger']"));
    /** Collapses a passenger's expanded Edit form without saving (used to move on from a voucher passenger). */
    private static final Locator CARD_EDIT_CANCEL_BUTTON = new Locator("CARD_EDIT_CANCEL_BUTTON", By.xpath(".//button[contains(@class,'PassengerCard_button_cancel')]"));
    /** Check-In button inside a specific booking row (hover-gated, revealed on row hover). */
    private static final Locator ROW_CHECK_IN_BUTTON = new Locator("ROW_CHECK_IN_BUTTON", By.xpath(".//button[normalize-space()='Check-In']"));

    private static Locator bookingRowByCode(String bookingCode) {
        return new Locator("BOOKING_ROW_" + bookingCode,
                By.xpath("//table//tbody//tr[.//p[normalize-space()='" + bookingCode + "']]"));
    }

    private void waitForLoadingOverlayToDisappear() {
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 15);
    }

    /**
     * Waits for the On Schedule / Delayed flights list to finish its asynchronous fetch before the list is
     * inspected.
     * <p>
     * The flights are fetched asynchronously after the tab/date/lounge is resolved, and the loading overlay
     * is an unreliable gate on its own: it frequently mounts a beat <em>after</em> the tab is clicked, so a
     * bare "overlay has disappeared" check can pass instantly — before the fetch has even started. So we (1)
     * give the overlay a short window to appear, (2) wait for it to clear, then (3) wait for the list to settle
     * into one of its two terminal states: the flights table (has data) or the empty-state container (no data).
     * Waiting for <em>either</em> — rather than the table alone — lets an empty list settle in a beat instead of
     * burning the full table timeout, so the caller's "no available booking" skip returns promptly.
     * <p>
     * The On Schedule and Delayed lists render the same {@code table[class*='styles_table']} markup, so
     * {@link #ON_SCHEDULE_FLIGHTS_CONTAINER} matches the flights table on <em>either</em> tab. If neither terminal
     * state renders within the window this returns anyway (best-effort) after logging it, leaving the caller to
     * decide what an unsettled list means: {@link #hasBookingMatching(Locator, String)} re-inspects the same two
     * states and raises a descriptive load-failure error, while the by-code look-ups fail with their own not-found
     * messages — so an unsettled list is never mistaken for an empty one and never passes silently.
     */
    private void waitForFlightsListToLoad() {
        driverTools.waitForElementToAppear(LOADING_OVERLAY, 3);
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 30);
        for (int attempt = 0; attempt < 15; attempt++) {
            if (driverTools.isElementDisplayed(ON_SCHEDULE_FLIGHTS_CONTAINER)
                    || driverTools.isElementDisplayed(FLIGHTS_LIST_EMPTY_STATE)) {
                return;
            }
            driverTools.dummyWait(1);
        }
        logger.log("Flights list did not settle into a terminal state within the wait window (neither the flights "
                + "table nor the empty-state container rendered) — proceeding; the caller will surface this as a "
                + "load-failure or not-found error rather than a silent pass.");
    }

    public void goToSpecificPage(String menu) {
        switch (menu.toLowerCase()) {
            case "check-in" -> {
                driverTools.click(CHECK_IN_MENU);
            }
            case "lounge" -> {
                driverTools.click(LOUNGE_MENU);
            }
            case "check-in history" -> {
                driverTools.click(CHECK_IN_HISTORY_MENU);
            }
            case "user management" -> {
                driverTools.click(USER_MANAGEMENT_MENU);
            }
            default -> throw new NoSuchElementException("There no menu for " + menu);
        }
        driverTools.dummyWait(1);
    }

    /**
     * Selects the On Schedule / Delayed tab. {@code desiredType} may be null/blank (the on-schedule case
     * omits the param) — anything that isn't "Delayed" is treated as On Schedule, which is the default tab.
     */
    public void selectFlightType(String desiredType) {
        waitForLoadingOverlayToDisappear();
        boolean delayed = desiredType != null && desiredType.equalsIgnoreCase("Delayed");
        Locator tab = delayed ? DELAYED_TAB : ON_SCHEDULE_TAB;
        driverTools.click(tab);
        driverTools.waitForWebToLoad();
    }

    public boolean areOnScheduleFlightsVisible() {
        driverTools.waitForElementToAppear(ON_SCHEDULE_FLIGHTS_CONTAINER, 10);
        return driverTools.isElementDisplayed(ON_SCHEDULE_FLIGHTS_CONTAINER);
    }

    /**
     * Counts the flight rows shown on the currently selected Check-In tab (On Schedule or Delayed).
     * <p>
     * In V2 the flights table only renders when there is at least one flight for the selected date/lounge;
     * with zero flights nothing renders. Returns 0 in that case so callers can skip (rather than fail) a
     * data-dependent visibility check when there is simply nothing to display.
     */
    public int getFlightsRowCount() {
        waitForLoadingOverlayToDisappear();
        Optional<WebElement> table = driverTools.waitForElementToAppear(ON_SCHEDULE_FLIGHTS_CONTAINER, 10);
        if (table.isEmpty() || !driverTools.isElementDisplayed(table.get())) {
            logger.log("Flights table did not render within the wait window — there are no flights for the "
                    + "selected date/lounge (empty list). Treating the flight row count as 0.");
            return 0;
        }
        return table.get().findElements(By.cssSelector("tbody tr")).size();
    }

    public boolean areDelayedFlightsVisible() {
        driverTools.waitForElementToAppear(DELAYED_FLIGHTS_CONTAINER, 10);
        return driverTools.isElementDisplayed(DELAYED_FLIGHTS_CONTAINER);
    }

    public boolean isCheckInPageOpened() {
        driverTools.waitForElementToAppear(CHECK_IN_PAGE_TITLE, 10);
        boolean isCheckInPageOpened = driverTools.isElementDisplayed(CHECK_IN_PAGE_TITLE);
        if(!isCheckInPageOpened){
            logger.log("Check In page is not opened");
        }
        return isCheckInPageOpened;
    }

    public boolean confirmSuccessfullyLogin() {
        driverTools.dummyWait(3);
        return !driverTools.getCurrentURL().contains("login");
    }

    public void openUserProfileMenu() {
        driverTools.click(USER_PROFILE_DROPDOWN_MENU);
    }

    public void selectChangePasswordMenu() {
        driverTools.waitForElementToAppear(CHANGE_PASSWORD_MENU);
        driverTools.clickElementByCoordinateDWeb(CHANGE_PASSWORD_MENU);
    }

    public void selectLogoutMenu() {
        driverTools.waitForElementToAppear(LOGOUT_MENU);
        driverTools.clickElementByCoordinateDWeb(LOGOUT_MENU);
    }

    /**
     * Idempotent logout. Some flows (e.g. changing your own password) invalidate the session and bounce to
     * the centralized login page where the profile dropdown no longer exists; if the trigger isn't present
     * we're already logged out, so there's nothing to do.
     */
    public void logout() {
        if (driverTools.waitForElementToAppear(USER_PROFILE_DROPDOWN_MENU, 5).isEmpty()) {
            logger.log("Profile dropdown not present — assuming already logged out, skipping logout");
            return;
        }
        openUserProfileMenu();
        selectLogoutMenu();
    }

    /**
     * True when the On Schedule list shows at least one booking with a check-in-eligible (un-checked-in) passenger —
     * i.e. status "Available" or "Partial". This is what both the on-list check-in and the cancel-check-in cases
     * need: each only requires one passenger it can check in via phone (a Partial booking still has one), so neither
     * need restrict itself to fully "Available" bookings.
     */
    public boolean hasEligibleBooking() {
        return hasBookingMatching(FIRST_ELIGIBLE_ROW, "'Available' or 'Partial'");
    }

    /**
     * True when the On Schedule list shows at least one booking that already carries a check-in — status "Partial"
     * or "Checked-In". The cancel-check-in case prefers such a booking so it can cancel a pre-existing <b>phone</b>
     * check-in directly (via {@link #cancelFirstExistingPhoneCheckIn(String)}) instead of first having to check a
     * passenger in on an "Available"/"Partial" booking. (Whether that booking actually holds a cancellable phone
     * check-in — as opposed to only e-Voucher ones — can only be known after opening it.)
     */
    public boolean hasBookingWithExistingCheckIn() {
        return hasBookingMatching(FIRST_CHECKED_IN_ROW, "'Partial' or 'Checked-In'");
    }

    /**
     * Shared "is there a booking whose first-row status matches {@code firstRow}?" check, keyed off the
     * always-visible <b>row</b> (the status cell), NOT the per-row "Check-In" button: that button is hover-gated
     * (its container only reveals on row hover), so an appearance wait on it times out even when the list is fully
     * populated.
     * <p>
     * We report "no booking" (which the caller turns into a skip) only when the list positively renders one of its
     * known terminal states — the empty-state container ({@link #FLIGHTS_LIST_EMPTY_STATE}: no flights today) or the
     * flights table ({@link #ON_SCHEDULE_FLIGHTS_CONTAINER}) with no matching row. If neither renders, the list is in
     * an unrecognized state (a load failure or a table/empty-state markup change), so we throw instead of returning
     * false: breakage must surface as a failure, not hide behind a data-driven skip.
     */
    private boolean hasBookingMatching(Locator firstRow, String statusDescription) {
        waitForFlightsListToLoad();
        if (driverTools.isElementDisplayed(FLIGHTS_LIST_EMPTY_STATE)) {
            logger.log("Check-In list rendered its empty-state — no flights on the On Schedule list today");
            return false;
        }
        if (driverTools.isElementDisplayed(ON_SCHEDULE_FLIGHTS_CONTAINER)) {
            boolean present = driverTools.waitForElementToAppear(firstRow, 15).isPresent();
            logger.log(present
                    ? "Check-In list shows a booking with " + statusDescription + " status on the On Schedule list"
                    : "Check-In list has flights but none with " + statusDescription + " status today");
            return present;
        }
        boolean checkInPanelRendered = driverTools.waitForElementToAppear(ON_SCHEDULE_TAB, 5).isPresent()
                || driverTools.waitForElementToAppear(CHECK_IN_BY_VOUCHER_BUTTON, 5).isPresent();
        throw new AssertionError(checkInPanelRendered
                ? "Check-In panel loaded but the flights list rendered neither the flights table nor the known "
                    + "empty-state container — likely a markup/locator change in the list. Investigate before "
                    + "trusting a skip."
                : "Check-In page did not render (neither the 'On Schedule' tab nor the 'Check-In by Voucher' "
                    + "button is present) — treat this as a load failure, not an empty list.");
    }

    public void openCheckInByVoucher() {
        waitForLoadingOverlayToDisappear();
        driverTools.waitForElementToAppear(CHECK_IN_BY_VOUCHER_BUTTON, 10);
        driverTools.click(CHECK_IN_BY_VOUCHER_BUTTON);
        driverTools.waitForElementToAppear(E_VOUCHER_MODAL, 10);
    }

    public void inputEVoucherOrderIdOnModal(String orderId) {
        driverTools.waitForElementToAppear(E_VOUCHER_MODAL_ORDER_ID_INPUT, 10);
        driverTools.clearInput(E_VOUCHER_MODAL_ORDER_ID_INPUT);
        driverTools.sendKeys(E_VOUCHER_MODAL_ORDER_ID_INPUT, orderId);
    }

    public void saveCheckIn() {
        driverTools.waitForElementToBeClickable(MODAL_SAVE_BUTTON, 10);
        driverTools.click(MODAL_SAVE_BUTTON);
    }

    /**
     * Clicks the "Confirm Check-In" button when the confirmation dialog is shown. The "Check-In by Voucher"
     * flow raises this confirmation after Save; the per-row flow may check in directly, so the button is
     * optional and this is a no-op when it never appears.
     */
    public void confirmCheckInIfPresent() {
        driverTools.waitForElementToAppear(CONFIRM_CHECK_IN_BUTTON, 6)
                .ifPresent(driverTools::click);
    }

    /**
     * True when a success snackbar is shown after saving/confirming a check-in <em>or</em> its cancellation (the app
     * shows the same "You've successfully ..." toast for both).
     * <p>
     * The snackbar positioner ({@link #ANY_SNACKBAR}, {@code [class*='snackbars_positioner']}) mounts a beat before
     * the toast text inside it, so reading once right after it appears can catch it <b>empty</b> — exactly what made
     * the cancel flow flake (positioner present, text {@code ""}, so a real success was reported as failure). So once
     * the positioner is present we poll its text until it is populated (or the window elapses) before deciding.
     * Success is a case-insensitive "success" match, tolerant of wording ("You've successfully check-in ...",
     * "Check-in success", etc.); error toasts ("failed"/"invalid"/"unsuccessful"/...) do not match — note
     * "unsuccessful" contains the substring "success", so it is excluded explicitly — and a toast that never renders
     * text returns false. The exact text is logged (useful for debugging a failed save — e.g. "voucher already used").
     */
    public boolean isCheckInSuccessSnackbarShown() {
        Optional<WebElement> snackbar = driverTools.waitForElementToAppear(ANY_SNACKBAR, 12);
        if (snackbar.isEmpty()) {
            logger.log("No check-in snackbar appeared within the wait window");
            return false;
        }
        String text = "";
        for (int attempt = 0; attempt <= 8 && text.isBlank(); attempt++) {
            try {
                text = driverTools.getText(snackbar.get());
            } catch (StaleElementReferenceException e) {
                text = driverTools.getText(ANY_SNACKBAR).orElse("");
            }
            if (text == null) {
                text = "";
            }
            if (text.isBlank()) {
                driverTools.dummyWait(1);
            }
        }
        logger.log("Check-in snackbar: " + text);
        String normalized = text.toLowerCase();
        if (normalized.contains("unsuccess")) {
            return false;
        }
        return normalized.contains("success");
    }

    /**
     * Reads the booking code of the first check-in-eligible booking ("Available" or "Partial") on the On Schedule
     * list. Captured up-front so the same booking can be re-opened by code after a passenger has been checked in
     * (which flips its status). Assumes an eligible booking exists — gate the call with {@link #hasEligibleBooking()}.
     */
    public String getFirstEligibleBookingCode() {
        waitForFlightsListToLoad();
        driverTools.waitForElementToAppear(FIRST_ELIGIBLE_ROW_BOOKING_CODE, 15)
                .orElseThrow(() -> new AssertionError("No eligible booking row to read a booking code from"));
        String bookingCode = driverTools.getText(FIRST_ELIGIBLE_ROW_BOOKING_CODE)
                .orElseThrow(() -> new AssertionError("Could not read the booking code from the first eligible row"))
                .trim();
        logger.log("First eligible booking code: " + bookingCode);
        return bookingCode;
    }

    /**
     * Reads the booking code of the first booking that already carries a check-in ("Partial" or "Checked-In") on the
     * On Schedule list. Captured up-front so the same booking can be re-opened by code (its status can flip once a
     * check-in is cancelled). Assumes such a booking exists — gate the call with
     * {@link #hasBookingWithExistingCheckIn()}.
     */
    public String getFirstCheckedInBookingCode() {
        waitForFlightsListToLoad();
        driverTools.waitForElementToAppear(FIRST_CHECKED_IN_ROW_BOOKING_CODE, 15)
                .orElseThrow(() -> new AssertionError("No checked-in/partial booking row to read a booking code from"));
        String bookingCode = driverTools.getText(FIRST_CHECKED_IN_ROW_BOOKING_CODE)
                .orElseThrow(() -> new AssertionError("Could not read the booking code from the first checked-in/partial row"))
                .trim();
        logger.log("First checked-in/partial booking code: " + bookingCode);
        return bookingCode;
    }

    /**
     * Opens the "Manage Check In" panel for a booking identified by its booking code (status-agnostic, so it works
     * whether the booking is Available, Partial or Checked-In).
     * <p>
     * First waits for any previously-open panel to close — force-dismissing one left open by an aborted flow (a
     * failed save, or a mid-flow skip/exception on the way to cleanup) — so the row is never hovered behind a
     * still-open modal. The row's Check-In button is hover-gated (revealed on row hover), so the row is scrolled
     * into view and hovered, then the button is polled (bounded) rather than looked up once so a slightly delayed
     * reveal is not missed. Finally waits for the passenger cards, which render a beat after the panel frame, so
     * callers never inspect an empty panel.
     */
    public void openManageCheckInForBooking(String bookingCode) {
        if (!driverTools.waitForElementToDisappear(MANAGE_CHECK_IN_PANEL, 15)) {
            dismissManageCheckInPanelIfOpen();
        }
        waitForFlightsListToLoad();
        WebElement row = driverTools.waitForElementToAppear(bookingRowByCode(bookingCode), 15)
                .orElseThrow(() -> new AssertionError("Booking '" + bookingCode + "' not found on the Check-In list"));
        driverTools.scrollElementToCenter(row);
        driverTools.hoverToElement(row);
        WebElement checkInButton = waitForElementWithin(row, ROW_CHECK_IN_BUTTON, 10)
                .orElseThrow(() -> new AssertionError("Check-In button not found on booking row '" + bookingCode + "'"));
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(checkInButton);
        manageCheckInPanel();
        driverTools.waitForElementToAppear(PASSENGER_CARD, 10);
    }

    /**
     * Checks in the first check-in-eligible (un-checked-in) passenger on the currently open booking using the Phone
     * Number method — the partial precondition for the cancel-check-in case, which only needs one cancellable
     * (phone) check-in to then cancel. Checking in a single passenger instead of all of them keeps the flow far less
     * flaky. Passengers already checked in (phone or voucher — both rendered with an "Edit" affordance instead of a
     * toggle) are skipped.
     * <p>
     * The chosen passenger's number is assigned <b>by its position</b> — passenger {@code i} gets
     * {@code phoneNumbers[i]}. Within one booking a phone number can only be used once, so a position-based number
     * stays collision-free even if the booking already carries a phone check-in on another passenger; a number's
     * usage is scoped to this booking, so the same {@code phoneNumbers} pool works for every booking.
     * <p>
     * Returns the index of the passenger checked in, or {@code -1} when every passenger is already checked in (no
     * eligible passenger). Caller must still {@link #saveCheckIn()} to persist.
     */
    public int checkInFirstEligiblePassenger(String[] phoneNumbers) {
        if (phoneNumbers == null || phoneNumbers.length == 0) {
            throw new IllegalArgumentException("At least one phone number is required to check in a passenger");
        }
        int passengerCount = passengerCards().size();
        if (passengerCount == 0) {
            throw new AssertionError("The Manage Check In panel opened but shows no passenger cards");
        }
        for (int i = 0; i < passengerCount; i++) {
            Optional<WebElement> cardOpt = passengerCardAt(i);
            if (cardOpt.isEmpty()) {
                continue;
            }
            WebElement card = cardOpt.get();
            if (driverTools.element(CARD_EDIT_LABEL, card).isPresent()) {
                continue;
            }
            if (i >= phoneNumbers.length) {
                throw new AssertionError("The first eligible passenger is at position " + (i + 1) + " but only "
                        + phoneNumbers.length + " phone number(s) were provided in the 'phoneNumbers' test data — a "
                        + "passenger needs its position's number to stay collision-free. Add more '#'-separated numbers.");
            }
            checkInPassengerViaPhone(i, phoneNumbers[i]);
            logger.log("Checked in passenger " + (i + 1) + " via phone (partial precondition)");
            return i;
        }
        logger.log("No check-in-eligible (un-checked-in) passenger found on the open booking");
        return -1;
    }

    /**
     * Checks the passenger at {@code index} in via the Phone Number method on the currently open booking, filling
     * {@code phone}. Used by {@link #checkInFirstEligiblePassenger(String[])} to check the first eligible passenger
     * in. Returns {@code false} when that passenger is already checked in (rendered with an "Edit" affordance, so
     * there is no toggle to turn on), or {@code true} once the phone number is filled — the caller must still
     * {@link #saveCheckIn()} to persist.
     * <p>
     * The toggle and method-option are custom controls that can be briefly not-interactable while the form animates
     * in, so they are clicked via the JS fallback; Phone Number is the default method, so its option is selected
     * best-effort and the phone input (which renders for the default method regardless) is the real gate.
     */
    public boolean checkInPassengerViaPhone(int index, String phone) {
        WebElement card = passengerCardAt(index)
                .orElseThrow(() -> new AssertionError("No passenger card at position " + (index + 1) + " to check in"));
        if (driverTools.element(CARD_EDIT_LABEL, card).isPresent()) {
            logger.log("Passenger " + (index + 1) + " is already checked in — nothing to check in");
            return false;
        }
        driverTools.scrollElementToCenter(card);
        WebElement toggle = driverTools.element(CARD_CHECK_IN_TOGGLE, card)
                .orElseThrow(() -> new AssertionError("Check-In toggle not found on passenger " + (index + 1) + "'s card"));
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(toggle);
        waitForCardElement(index, CARD_PHONE_OPTION, 5)
                .ifPresent(driverTools::clickOrFallbackToClickWithJavascriptExecutor);
        WebElement phoneInput = waitForCardElement(index, CARD_PHONE_INPUT, 10,
                "Phone input did not appear for passenger " + (index + 1));
        fillInputRobustly(phoneInput, phone);
        return true;
    }

    /**
     * Cancels the check-in of the passenger at {@code index} — the exact passenger the precondition checked in — but
     * only when it is a <b>phone</b> check-in (an e-Voucher check-in cannot be un-checked; the app renders its toggle
     * disabled). Targeting the specific passenger, rather than "the first phone check-in on the booking", is critical
     * on a Partial booking: a lower-indexed passenger may already be phone-checked-in by real B2C data, and
     * cancelling <em>that</em> one would corrupt pre-existing state and leave the injected check-in behind. Opens the
     * passenger's Edit form, and when it is a phone check-in toggles Check-In off (which reveals the reason field) and
     * enters {@code note}. Returns {@code true} when the cancellation was prepared (caller must still
     * {@link #saveCheckIn()} to persist), or {@code false} when that passenger is not a cancellable phone check-in
     * (e.g. the injected check-in did not persist, or the slot turned out to be an e-Voucher).
     */
    public boolean cancelPhoneCheckInForPassenger(int index, String note) {
        WebElement card = passengerCardAt(index)
                .orElseThrow(() -> new AssertionError("No passenger card at position " + (index + 1) + " to cancel"));
        if (driverTools.element(CARD_EDIT_LABEL, card).isEmpty()) {
            logger.log("Passenger " + (index + 1) + " is not checked in — nothing to cancel");
            return false;
        }
        driverTools.scrollElementToCenter(card);
        WebElement editLabel = driverTools.element(CARD_EDIT_LABEL, card)
                .orElseThrow(() -> new AssertionError("Edit option vanished from checked-in passenger " + (index + 1)));
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(editLabel);
        String method = detectExpandedCheckInMethod(index, 6);
        if (!"PHONE".equals(method)) {
            logger.log("Passenger " + (index + 1) + " is checked in by "
                    + ("VOUCHER".equals(method) ? "e-Voucher — it cannot be cancelled" : "an unrecognized method")
                    + "; collapsing its editor without cancelling");
            collapseCardEditor(index);
            return false;
        }
        WebElement toggle = waitForCardElement(index, CARD_CHECK_IN_TOGGLE, 10,
                "Check-In toggle did not appear on passenger " + (index + 1) + "'s edit form");
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(toggle);
        WebElement noteField = waitForCardElement(index, CARD_CANCEL_NOTE_INPUT, 10,
                "The uncheck-reason field did not appear after toggling off passenger " + (index + 1));
        fillInputRobustly(noteField, note);
        logger.log("Prepared cancellation of the phone check-in for passenger " + (index + 1));
        return true;
    }

    /**
     * Determines how the passenger at {@code index} was checked in by reading its <b>expanded</b> Edit form (call
     * only after the card's "Edit" has been clicked). Polls (bounded) because the form animates in: returns
     * {@code "PHONE"} once the phone field renders, {@code "VOUCHER"} once the e-Voucher field renders, or
     * {@code "UNKNOWN"} if neither appears in time.
     */
    private String detectExpandedCheckInMethod(int index, int timeoutSeconds) {
        for (int attempt = 0; attempt <= timeoutSeconds; attempt++) {
            Optional<WebElement> card = passengerCardAt(index);
            if (card.isPresent()) {
                if (driverTools.element(CARD_EVOUCHER_INPUT, card.get()).isPresent()) {
                    return "VOUCHER";
                }
                if (driverTools.element(CARD_PHONE_INPUT, card.get()).isPresent()) {
                    return "PHONE";
                }
            }
            driverTools.dummyWait(1);
        }
        return "UNKNOWN";
    }

    /** Collapses the expanded Edit form of the passenger at {@code index} (best-effort), discarding any edit. */
    private void collapseCardEditor(int index) {
        passengerCardAt(index)
                .flatMap(card -> driverTools.element(CARD_EDIT_CANCEL_BUTTON, card))
                .ifPresent(driverTools::clickOrFallbackToClickWithJavascriptExecutor);
        driverTools.dummyWait(1);
    }

    /**
     * Best-effort cleanup that undoes the single phone check-in this test created — the passenger at {@code index} —
     * returning the booking toward the state it was found in. Re-opens the booking by code and cancels only that
     * passenger's phone check-in; every other passenger (a pre-existing phone or e-Voucher check-in that was not
     * created here) is left untouched. Never throws — a cleanup failure only logs a warning so it cannot turn a
     * passing test red.
     */
    public void undoPhoneCheckIn(String bookingCode, int index, String note) {
        try {
            openManageCheckInForBooking(bookingCode);
            if (!cancelPhoneCheckInForPassenger(index, note)) {
                logger.log("Cleanup: passenger " + (index + 1) + " on '" + bookingCode + "' is no longer a "
                        + "cancellable phone check-in — nothing to undo");
                return;
            }
            saveCheckIn();
            driverTools.waitForElementToDisappear(MANAGE_CHECK_IN_PANEL, 10);
            logger.log("Cleanup: undid the injected phone check-in for passenger " + (index + 1) + " on '"
                    + bookingCode + "'");
        } catch (Exception e) {
            logger.softWarning("Cleanup: failed to undo the phone check-in for passenger " + (index + 1) + " on '"
                    + bookingCode + "': " + e.getMessage());
        }
    }

    /**
     * On the currently open booking, prepares the cancellation of the first passenger already checked in via
     * <b>phone</b> — the only cancellable kind (an e-Voucher check-in cannot be un-checked; the app renders its
     * toggle disabled). Walks the passenger cards, delegating each to {@link #cancelPhoneCheckInForPassenger(int,
     * String)}, which expands the card, reads its method and — for a phone check-in — toggles Check-In off and fills
     * {@code note}; un-checked and e-Voucher/unknown passengers return {@code false} there (the latter are collapsed)
     * and are skipped. This lets the cancel case reuse a pre-existing phone check-in instead of creating one first.
     * <p>
     * Returns the index whose phone check-in was prepared (caller must still {@link #saveCheckIn()} to persist), or
     * {@code -1} when the booking has no cancellable phone check-in (empty list, or only un-checked/e-Voucher
     * passengers).
     */
    public int cancelFirstExistingPhoneCheckIn(String note) {
        int passengerCount = passengerCards().size();
        if (passengerCount == 0) {
            throw new AssertionError("The Manage Check In panel opened but shows no passenger cards");
        }
        for (int i = 0; i < passengerCount; i++) {
            if (cancelPhoneCheckInForPassenger(i, note)) {
                logger.log("Prepared cancellation of an existing phone check-in on passenger " + (i + 1));
                return i;
            }
        }
        logger.log("No cancellable phone check-in on the open booking (only un-checked or e-Voucher passengers)");
        return -1;
    }

    private WebElement manageCheckInPanel() {
        return driverTools.waitForElementToAppear(MANAGE_CHECK_IN_PANEL, 10)
                .orElseThrow(() -> new AssertionError("The Manage Check In panel did not open"));
    }

    /**
     * Public, speculative "close the panel if one is open" — used when a flow opened a booking to inspect it but
     * decided not to act (e.g. the cancel case opened a checked-in booking that turned out to hold only e-Voucher
     * check-ins) and now needs to read the list underneath again. No-op when no panel is open.
     */
    public void closeManageCheckInPanel() {
        dismissManageCheckInPanelIfOpen();
    }

    /**
     * Force-dismisses a Manage Check In panel left open by an aborted flow (a failed save, or a mid-flow
     * skip/exception on the way to cleanup). The panel is a blocking modal with no stable close control to key off,
     * so we clear it deterministically with a page refresh and wait for it to go before returning. Because a refresh
     * resets the client-side tab selection to its default, the On Schedule tab (which every check-in flow here
     * operates on) is re-asserted afterwards, so a later look-up by booking code does not run against the wrong tab
     * and spuriously miss the booking. No-op when no panel is open, so it is safe to call speculatively.
     */
    private void dismissManageCheckInPanelIfOpen() {
        if (!driverTools.isElementDisplayed(MANAGE_CHECK_IN_PANEL)) {
            return;
        }
        logger.log("A Manage Check In panel is still open — refreshing to dismiss it before continuing");
        driverTools.refreshWebpage();
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToDisappear(MANAGE_CHECK_IN_PANEL, 10);
        selectFlightType("On Schedule");
    }

    /**
     * Waits (bounded) for {@code locator} to resolve inside the already-resolved {@code parent}, retrying on each
     * poll so a slightly delayed render (e.g. a hover-gated control) is tolerated rather than missed by a single
     * fail-fast scoped lookup. Returns empty when it never appears within {@code timeoutSeconds}.
     */
    private Optional<WebElement> waitForElementWithin(WebElement parent, Locator locator, int timeoutSeconds) {
        for (int attempt = 0; attempt <= timeoutSeconds; attempt++) {
            Optional<WebElement> found = driverTools.element(locator, parent);
            if (found.isPresent()) {
                return found;
            }
            driverTools.dummyWait(1);
        }
        return Optional.empty();
    }

    private List<WebElement> passengerCards() {
        WebElement panel = manageCheckInPanel();
        return driverTools.elements(PASSENGER_CARD, panel).orElseGet(List::of);
    }

    private Optional<WebElement> passengerCardAt(int index) {
        List<WebElement> cards = passengerCards();
        return index >= 0 && index < cards.size() ? Optional.of(cards.get(index)) : Optional.empty();
    }

    /**
     * Waits (bounded) for a card-scoped element to render inside the passenger card at {@code index}, re-resolving
     * both the card and the element on every poll so it survives the re-render a toggle/edit triggers. Needed
     * because no implicit wait is configured, so the scoped {@code element(...)} lookups otherwise fail fast —
     * before an asynchronously-mounted field (phone input, reason box, edit-form toggle) has rendered. Returns
     * empty (rather than throwing) when the element never renders, so callers can treat it as optional.
     */
    private Optional<WebElement> waitForCardElement(int index, Locator locator, int timeoutSeconds) {
        for (int attempt = 0; attempt <= timeoutSeconds; attempt++) {
            Optional<WebElement> found = passengerCardAt(index).flatMap(c -> driverTools.element(locator, c));
            if (found.isPresent()) {
                return found;
            }
            driverTools.dummyWait(1);
        }
        return Optional.empty();
    }

    /** {@link #waitForCardElement(int, Locator, int)} that throws {@code notFoundMessage} when the element never renders. */
    private WebElement waitForCardElement(int index, Locator locator, int timeoutSeconds, String notFoundMessage) {
        return waitForCardElement(index, locator, timeoutSeconds)
                .orElseThrow(() -> new AssertionError(notFoundMessage));
    }

    /**
     * Types {@code value} into an already-resolved card field ({@code <input>} or {@code <textarea>}) robustly.
     * These custom check-in fields are consistently rejected by a real-keystroke {@code sendKeys} in ChromeDriver
     * with "element not interactable" (even though they are visible, enabled and unobscured), so we attempt the
     * standard path once for fidelity and fall back to a JS value-set that dispatches {@code input}/{@code change}
     * — verified to stick and be accepted by the app's React state. The field is passed in (not re-looked-up)
     * because its id is duplicated across passenger cards and must stay scoped to one card.
     */
    private void fillInputRobustly(WebElement input, String value) {
        driverTools.scrollElementToCenter(input);
        try {
            driverTools.clearInput(input);
            driverTools.sendKeys(input, value);
        } catch (Exception e) {
            logger.softWarning("Standard typing failed (" + e.getClass().getSimpleName() + ") — this is a custom "
                    + "control ChromeDriver treats as not-interactable; setting the value via JS with input/change "
                    + "events instead");
            driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(input, value);
        }
    }
}
