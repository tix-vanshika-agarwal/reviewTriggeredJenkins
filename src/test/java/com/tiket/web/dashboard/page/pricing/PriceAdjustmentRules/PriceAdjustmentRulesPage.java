package com.tiket.web.dashboard.page.pricing.PriceAdjustmentRules;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PriceAdjustmentRulesPage<T extends WebDriver> extends BasePage<T> {

    private static final Locator CAMPAIGN_AND_RULE_MANAGEMENT_SIDEBAR_BUTTON = new Locator(
            "CAMPAIGN_AND_RULE_MANAGEMENT_SIDEBAR_BUTTON",
            new By.ByXPath("//button[contains(normalize-space(.), 'Campaign & Rule')]"));
    private static final Locator PRICE_ADJUSTMENT_RULES_SIDEBAR_BUTTON = new Locator(
            "PRICE_ADJUSTMENT_RULES_SIDEBAR_BUTTON",
            new By.ByXPath("//button/span[contains(text(),'Price Adjustment Rules')]"));
    private static final Locator PRICE_ADJUSTMENT_RULES_TITLE = new Locator("PRICE_ADJUSTMENT_RULES_TITLE",
            new By.ByXPath("//div/span[contains(text(),'Price Adjustment Rules')]"));
    private static final Locator COMPLETED_RELEASED_SECTION = new Locator("COMPLETED_RELEASED_SECTION",
            new By.ByXPath("//div/span[contains(text(),'Completed / Released')]"));
    private static final Locator NEED_APPROVAL_SECTION = new Locator("NEED_APPROVAL_SECTION",
            new By.ByXPath("//div/span[contains(text(),'Need Approval')]"));
    private static final Locator NEED_APPROVAL_SECTION_NOTIF = new Locator("NEED_APPROVAL_SECTION_NOTIF",
            new By.ByXPath("//span[contains(@class, 'Notif_notif') and contains(@class, 'Notif_shimmering')]"));
    private static final Locator VERTICAL_DISTRIBUTION_CHANNEL_INPUT = new Locator(
            "VERTICAL_DISTRIBUTION_CHANNEL_INPUT",
            new By.ByXPath("//input[@id='vertical--distribution-channel']"));
    private static final Locator TESTING_VERTICAL = new Locator("TESTING_VERTICAL",
            new By.ByXPath("//div[@id='dropdown-options-verticalIds-21']"));
    private static final Locator DISPLAY_SCROLLBAR = new Locator("DISPLAY_SCROLLBAR",
            new By.ByXPath("(//div[@class='display-scrollbar'])[1]"));
    //from the PAR filter row; ids are stable across builds.
    private static final Locator START_DATE_INPUT_ID = new Locator("START_DATE_INPUT_ID",
            new By.ByXPath("//input[@id='start-date']"));
    private static final Locator END_DATE_INPUT_ID = new Locator("END_DATE_INPUT_ID",
            new By.ByXPath("//input[@id='end-date']"));
    //opening via slot avoids cases where the input click is swallowed by the label/grid layout.
    private static final Locator START_DATE_RIGHT_SLOT_TRIGGER = new Locator("START_DATE_RIGHT_SLOT_TRIGGER",
            new By.ByXPath("//div[contains(@class,'input_inner_wrapper')][.//input[@id='start-date']]//div[contains(@class,'right_slot')]"));
    private static final Locator END_DATE_RIGHT_SLOT_TRIGGER = new Locator("END_DATE_RIGHT_SLOT_TRIGGER",
            new By.ByXPath("//div[contains(@class,'input_inner_wrapper')][.//input[@id='end-date']]//div[contains(@class,'right_slot')]"));
    private static final Locator CALENDAR_PREV_BUTTON_MOBILE = new Locator("CALENDAR_PREV_BUTTON_MOBILE",
            new By.ByXPath("//button[contains(@class,'CalendarMobile_prev_button')]"));
    private static final Locator CALENDAR_PREV_BUTTON = new Locator("CALENDAR_PREV_BUTTON",
            new By.ByXPath("//button[contains(@class,'CalendarDesktop_prev_button')]"));
    private static final Locator CALENDAR_PREV_BY_ARIA = new Locator("CALENDAR_PREV_BY_ARIA",
            new By.ByXPath("//button[@aria-label='Previous month' or @aria-label='previous month' "
                + "or translate(@aria-label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='previous month']"));
    private static final Locator CALENDAR_PREV_LEGACY_SVG = new Locator("CALENDAR_PREV_LEGACY_SVG",
            new By.ByXPath("//div[contains(@class,'custom_calendar')]//*[name()='svg'][1]"));
    private static final Locator CALENDAR_NEXT_BUTTON_MOBILE = new Locator("CALENDAR_NEXT_BUTTON_MOBILE",
            new By.ByXPath("//button[contains(@class,'CalendarMobile_next_button')]"));
    private static final Locator CALENDAR_NEXT_BUTTON = new Locator("CALENDAR_NEXT_BUTTON",
            new By.ByXPath("//button[contains(@class,'CalendarDesktop_next_button')]"));
    private static final Locator CALENDAR_NEXT_BY_ARIA = new Locator("CALENDAR_NEXT_BY_ARIA",
            new By.ByXPath("//button[@aria-label='Next month' or @aria-label='next month' "
                + "or translate(@aria-label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='next month']"));
    private static final Locator CALENDAR_NEXT_LEGACY_SVG = new Locator("CALENDAR_NEXT_LEGACY_SVG",
            new By.ByXPath("//div[contains(@class,'custom_calendar')]//*[name()='svg'][2]"));
    //PAR CalendarMobile month/year title
    private static final Locator CALENDAR_MONTH_HEADING_MOBILE_CLASS = new Locator(
            "CALENDAR_MONTH_HEADING_MOBILE_CLASS",
            new By.ByXPath("//h2[contains(@class,'CalendarMobile_month_heading')]"));
    private static final Locator CALENDAR_MONTH_YEAR_HEADING = new Locator("CALENDAR_MONTH_YEAR_HEADING",
            new By.ByXPath("((//div[contains(@class,'Modal_modal_popup')]//section[contains(@class,'CalendarDesktop_month')]//h2)"
                    + " | (//div[contains(@class,'BaseModal_modal_popup')]//section[contains(@class,'CalendarDesktop_month')]//h2)"
                    + " | (//section[contains(@class,'CalendarDesktop_month')]//h2)"
                    + " | (//div[contains(@class,'CalendarDesktop_month')]//h2)"
                    + " | (//div[contains(@class,'Modal_modal_popup')]//div[contains(@class,'CalendarDesktop_month')]//h2)"
                    + " | (//div[contains(@class,'BaseModal_modal_popup')]//div[contains(@class,'CalendarDesktop_month')]//h2))[1]"));
    private static final Locator CALENDAR_CLOSE_BUTTON = new Locator("CALENDAR_CLOSE_BUTTON",
            new By.ByXPath("//button[@aria-label='close modal' or @aria-label='Close modal' or contains(@class,'Modal_modal_close_btn') or contains(@class,'Modal_modal_close')]"));
    private static final Locator MODAL_OVERLAY_BACKDROP = new Locator("MODAL_OVERLAY_BACKDROP",
            new By.ByXPath("//div[contains(@class, 'BaseModal_modal_overlay')]"));
    private static final Locator SEARCH_RULE_NAME_FIELD_INPUT = new Locator("SEARCH_RULE_NAME_FIELD_INPUT",
            new By.ByXPath("//input[@id='search-rule-name']"));
    private static final Locator NEED_APPROVAL_SUBMIT_BUTTON = new Locator("NEED_APPROVAL_SUBMIT_BUTTON",
            new By.ByXPath("//button[@type='submit']"));
    private static final Locator RULE_NAME_FROM_RESULT_TABLE = new Locator("RULE_NAME_FROM_RESULT_TABLE",
            new By.ByXPath("//td[@data-testid='table-cell-0-ruleName']"));
    private static final Locator SHOW_MINE_ONLY_CHECKBOX = new Locator("SHOW_MINE_ONLY_CHECKBOX",
            new By.ByXPath("//span[contains(text(),'Show mine only')]"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON",
            new By.ByXPath("//button[normalize-space()='Reset filter']"));
    private static final Locator APPROVE_BUTTON = new Locator("APPROVE_BUTTON",
            new By.ByXPath("(//button[normalize-space()='Approve'])[1]"));
    private static final Locator REJECT_BUTTON = new Locator("REJECT_BUTTON",
            new By.ByXPath("(//button[normalize-space()='Reject'])[1]"));
    private static final Locator USER_PROFILE_BUTTON = new Locator("USER_PROFILE_BUTTON", new By.ByXPath("//div[contains(@class, 'flex') and contains(@class, 'items-center') and contains(@class, 'px-1')]"));
    private static final Locator USER_LOGOUT_BUTTON = new Locator("USER_LOGOUT_BUTTON",
            new By.ByXPath("//span[text()='Log Out']"));
    private static final Locator RULE_CHANGES_BUTTON_1 = new Locator("RULE_CHANGES_BUTTON_1",
            new By.ByXPath("(//button[normalize-space()='Rule Changes'])[1]"));
    private static final Locator PREVIOUS_RULE_DETAIL = new Locator("PREVIOUS_RULE_DETAIL",
            new By.ByXPath("//span[normalize-space()='Previous Rule Detail']"));
    private static final Locator PREVIOUS_RULE_DETAIL_CONTAINER = new Locator("PREVIOUS_RULE_DETAIL_CONTAINER",
            new By.ByXPath("//span[normalize-space()='Previous Rule Detail']/parent::div/following-sibling::div[2]"));
    private static final Locator NEW_RULE_DETAIL = new Locator("NEW_RULE_DETAIL",
            new By.ByXPath("//span[normalize-space()='New Rule Detail']"));
    private static final Locator RULE_CHANGES_BUTTON_2 = new Locator("RULE_CHANGES_BUTTON_2",
            new By.ByXPath("(//button[normalize-space()='Rule Changes'])[2]"));
    private static final Locator RULE_CHANGES_VERTICAL_TESTING_PREV = new Locator(
            "RULE_CHANGES_VERTICAL_TESTING_PREV",
            new By.ByXPath("(//span[contains(text(),'TESTING')])[1]"));
    private static final Locator RULE_CHANGES_RULE_STATUS_PREV = new Locator("RULE_CHANGES_RULE_STATUS_PREV",
            new By.ByXPath("(//span[normalize-space()='Paused'])[1]"));

    private static final String AFTER_NEW_RULE_DETAIL_HEADING = "//span[normalize-space()='New Rule Detail']";

    private static final String TEXT_HIGH_EMPHASIS_SPAN = "span[contains(translate(@class,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'high-emphasis')]";

    private static final Locator RULE_CHANGES_VERTICAL_TESTING_NEW =
            new Locator("RULE_CHANGES_VERTICAL_TESTING_NEW",
                    new By.ByXPath(
                            "//span[normalize-space()='New Rule Detail']" +
                                    "/parent::div" +
                                    "/following-sibling::div[2]" +
                                    "//span[normalize-space()='Vertical / Distribution Channel']" +
                                    "/following-sibling::span"));

    private static final Locator RULE_CHANGES_RULE_NAME_NEW =
            new Locator("RULE_CHANGES_RULE_NAME_NEW",
                    new By.ByXPath(
                            "//span[normalize-space()='New Rule Detail']" +
                                    "/parent::div" +
                                    "/following-sibling::div[2]" +
                                    "//span[normalize-space()='Rule Name']" +
                                    "/following-sibling::span"));

    private static final Locator RULE_CHANGES_RULE_STATUS_NEW =
            new Locator("RULE_CHANGES_RULE_STATUS_NEW",
                    new By.ByXPath(
                            "//span[normalize-space()='New Rule Detail']" +
                                    "/parent::div" +
                                    "/following-sibling::div[2]" +
                                    "//span[normalize-space()='Status']" +
                                    "/following-sibling::span"));

    private static final Locator RULE_CHANGES_RULE_EDIT_BUTTON = new Locator("RULE_CHANGES_RULE_EDIT_BUTTON",
            new By.ByXPath("(//button[normalize-space()='Edit'])[1]"));
    private static final Locator RULE_CHANGES_BUTTON_3 = new Locator("RULE_CHANGES_BUTTON_3",
            new By.ByXPath("//button[normalize-space()='Rule Changes']"));
    public static final Locator RULE_CHANGES_BUTTON = new Locator("RULE_CHANGES_BUTTON",
            new By.ByXPath("//button[normalize-space()='Rule Changes']"));
    public static final Locator VERIFY_UPDATE_BY = new Locator("VERIFY_UPDATE_BY",
            new By.ByXPath("//p[normalize-space()='Updated By']"));
    public static final Locator VERIFY_UPDATE_BY_EMAIL = new Locator("VERIFY_UPDATE_BY_EMAIL",
            new By.ByXPath("//p[contains(@class,'highEmphasis') and contains(text(),'@tiket.com')]"));

    public PriceAdjustmentRulesPage(Session<T> session) {
        super(session);
    }

    private void waitUntilModalBackdropDoesNotBlockClicks() {
        driverTools.waitForElementToDisappear(MODAL_OVERLAY_BACKDROP, 15);
    }

    private void prepareCampaignAndRuleManagementSidebarButtonForClick() {
        waitUntilModalBackdropDoesNotBlockClicks();
        driverTools.waitForElementToAppear(CAMPAIGN_AND_RULE_MANAGEMENT_SIDEBAR_BUTTON);
        driverTools.scrollIntoWebView(CAMPAIGN_AND_RULE_MANAGEMENT_SIDEBAR_BUTTON);
    }

    public void verifyRuleUpdateBy() {
        driverTools.waitForElementToAppear(VERIFY_UPDATE_BY);
        driverTools.isElementDisplayed(VERIFY_UPDATE_BY_EMAIL);
    }

    public void clickCampAndRuleManagementSidebarButton() {
        prepareCampaignAndRuleManagementSidebarButtonForClick();
        try {
            driverTools.click(CAMPAIGN_AND_RULE_MANAGEMENT_SIDEBAR_BUTTON);
        } catch (ElementClickInterceptedException e) {
            waitUntilModalBackdropDoesNotBlockClicks();
            driverTools.scrollIntoWebView(CAMPAIGN_AND_RULE_MANAGEMENT_SIDEBAR_BUTTON);
            driverTools.clickWithJavascriptExecutor(CAMPAIGN_AND_RULE_MANAGEMENT_SIDEBAR_BUTTON);
        }
    }

    public void clickPriceAdjustmentRulesSidebarButton() {
        waitUntilModalBackdropDoesNotBlockClicks();
        driverTools.waitForElementToAppear(PRICE_ADJUSTMENT_RULES_SIDEBAR_BUTTON);
        driverTools.click(PRICE_ADJUSTMENT_RULES_SIDEBAR_BUTTON);
    }

    public boolean isPriceAdjustmentRulesPageLoaded() {
        driverTools.waitForElementToAppear(PRICE_ADJUSTMENT_RULES_TITLE);
        return driverTools.isElementDisplayed(PRICE_ADJUSTMENT_RULES_TITLE);
    }

    public boolean isCompRelSectionLoaded() {
        driverTools.waitForElementToAppear(COMPLETED_RELEASED_SECTION);
        return driverTools.isElementDisplayed(COMPLETED_RELEASED_SECTION);
    }

    public boolean isNeedApprovalSectionLoaded() {
        driverTools.waitForElementToAppear(NEED_APPROVAL_SECTION);
        return driverTools.isElementDisplayed(NEED_APPROVAL_SECTION);
    }

    public boolean isNeedApprovalSectionShowZero() {
        driverTools.waitForElementToAppear(NEED_APPROVAL_SECTION_NOTIF);
        return driverTools.isElementDisplayed(NEED_APPROVAL_SECTION_NOTIF);
    }

    public void changeToOtherVertical() {
        driverTools.waitForElementToAppear(VERTICAL_DISTRIBUTION_CHANNEL_INPUT);
        driverTools.click(VERTICAL_DISTRIBUTION_CHANNEL_INPUT);
        driverTools.scrollToBottomOfTable(DISPLAY_SCROLLBAR);
        driverTools.waitForElementToAppear(TESTING_VERTICAL);
        driverTools.clickWithJavascriptExecutor(TESTING_VERTICAL);
    }

    public boolean isNeedApprovalSectionGreaterThanZero() {
        WebElement textElement = driverTools.waitForElementToAppear(NEED_APPROVAL_SECTION_NOTIF).orElseThrow();
        String text = driverTools.getText(textElement);
        return text.indexOf('0') <= 0;
    }

    public void clickNeedApprovalSection() {
        driverTools.waitForElementToAppear(NEED_APPROVAL_SECTION);
        driverTools.click(NEED_APPROVAL_SECTION);
    }

    private static boolean looksLikeMonthYearHeading(String t) {
        if (t == null || t.isBlank()) {
            return false;
        }
        return t.matches(".*\\s\\d{4}$") || t.matches(".*\\d{4}.*");
    }

    private Optional<String> scanModalForMonthYearHeadingText() {
        List<WebElement> headings = driver.findElements(By.xpath("//div[contains(@class,'Modal_modal_popup') or contains(@class,'BaseModal_modal_popup') "
                + "or contains(@class,'BaseModal_modal_wrapper')]"
                + "//h2[normalize-space()!='']"));
        for (WebElement h : headings) {
            try {
                if (!h.isDisplayed()) {
                    continue;
                }
                String t = h.getText().replace('\n', ' ').trim();
                if (t.matches(".*\\s\\d{4}$")) {
                    return Optional.of(t);
                }
            } catch (Exception ignored) {
            }
        }
        return Optional.empty();
    }

    private WebElement resolveVisibleCalendarHeadingElement(int waitSeconds) {
        Locator[] order = new Locator[] { CALENDAR_MONTH_HEADING_MOBILE_CLASS, CALENDAR_MONTH_YEAR_HEADING };
        for (Locator loc : order) {
            Optional<WebElement> e = driverTools.waitForElementToAppear(loc, waitSeconds);
            if (e.isEmpty()) {
                continue;
            }
            try {
                WebElement h = e.get();
                if (!h.isDisplayed()) {
                    continue;
                }
                String t = h.getText().replace('\n', ' ').trim();
                if (looksLikeMonthYearHeading(t)) {
                    return h;
                }
            } catch (Exception ignored) {
            }
        }
        Optional<String> scanned = scanModalForMonthYearHeadingText();
        if (scanned.isPresent()) {
            String want = scanned.get();
            List<WebElement> h2s = driver.findElements(By.xpath("//h2"));
            for (WebElement h : h2s) {
                try {
                    if (h.isDisplayed() && want.equals(h.getText().replace('\n', ' ').trim())) {
                        return h;
                    }
                } catch (Exception ignored) {
                }
            }
        }
        throw new java.util.NoSuchElementException(
                "Calendar month/year heading not found (CalendarMobile_month_heading, Desktop h2, modal scan)");
    }

    private String readCalendarHeadingText(int waitSeconds) {
        WebElement h = resolveVisibleCalendarHeadingElement(waitSeconds);
        return h.getText().replace('\n', ' ').trim();
    }

    private WebElement requireOpenCalendarModalContainer() {
        WebElement heading = resolveVisibleCalendarHeadingElement(8);
        return heading.findElement(By.xpath("ancestor::div[contains(@class,'Modal_modal_wrapper') or contains(@class,'BaseModal_modal_wrapper') "
                + "or contains(@class,'Modal_modal_popup') or contains(@class,'BaseModal_modal_popup')][1]"));
    }

    private void clickWebElement(WebElement el) {
        try {
            driverTools.scrollIntoWebView(el);
        } catch (Exception ignored) {
        }
        try {
            el.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    private Optional<WebElement> findPrevMonthControlInModal(WebElement modal) {
        List<WebElement> divs = modal.findElements(By.xpath(".//div[contains(@class,'left-2')][contains(@class,'cursor-pointer')]"));
        for (WebElement d : divs) {
            try {
                if (d.isDisplayed() && !d.findElements(By.xpath(".//*[name()='svg']")).isEmpty()) {
                    return Optional.of(d);
                }
            } catch (Exception ignored) {
            }
        }
        return Optional.empty();
    }

    private Optional<WebElement> findNextMonthControlInModal(WebElement modal) {
        List<WebElement> divs = modal.findElements(By.xpath(".//div[contains(@class,'right-2')][contains(@class,'cursor-pointer')]"));
        for (WebElement d : divs) {
            try {
                if (d.isDisplayed() && !d.findElements(By.xpath(".//*[name()='svg']")).isEmpty()) {
                    return Optional.of(d);
                }
            } catch (Exception ignored) {
            }
        }
        return Optional.empty();
    }

    private void clickCalendarPreviousMonthOnce() {
        try {
            WebElement modal = requireOpenCalendarModalContainer();
            Optional<WebElement> scoped = findPrevMonthControlInModal(modal);
            if (scoped.isPresent()) {
                clickWebElement(scoped.get());
                return;
            }
        } catch (Exception ignored) {
        }
        Locator[] candidates = new Locator[] { CALENDAR_PREV_BUTTON_MOBILE, CALENDAR_PREV_BUTTON,
                CALENDAR_PREV_BY_ARIA, CALENDAR_PREV_LEGACY_SVG };
        for (Locator loc : candidates) {
            Optional<WebElement> el = driverTools.waitForElementToBeClickable(loc, 10);
            if (el.isPresent()) {
                try {
                    el.get().click();
                } catch (Exception e) {
                    driverTools.clickWithJavascriptExecutor(loc);
                }
                return;
            }
        }
        throw new java.util.NoSuchElementException(
                "Calendar previous-month control not found (modal left-2, CalendarMobile/Desktop buttons, aria-label, legacy svg)");
    }

    private void clickCalendarNextMonthOnce() {
        try {
            WebElement modal = requireOpenCalendarModalContainer();
            Optional<WebElement> scoped = findNextMonthControlInModal(modal);
            if (scoped.isPresent()) {
                clickWebElement(scoped.get());
                return;
            }
        } catch (Exception ignored) {
        }
        Locator[] candidates = new Locator[] { CALENDAR_NEXT_BUTTON_MOBILE, CALENDAR_NEXT_BUTTON,
                CALENDAR_NEXT_BY_ARIA, CALENDAR_NEXT_LEGACY_SVG };
        for (Locator loc : candidates) {
            Optional<WebElement> el = driverTools.waitForElementToBeClickable(loc, 10);
            if (el.isPresent()) {
                try {
                    el.get().click();
                } catch (Exception e) {
                    driverTools.clickWithJavascriptExecutor(loc);
                }
                return;
            }
        }
        throw new java.util.NoSuchElementException(
                "Calendar next-month control not found (modal right-2, CalendarMobile/Desktop buttons, aria-label, legacy svg)");
    }

    //Parses {@code Month yyyy} or {@code Multi Word Month yyyy} (e.g. {January 2026}).
    private static int[] parseMonthYearString(String raw, String label) {
        String t = raw.replace('\n', ' ').trim();
        String[] parts = t.split("\\s+");
        if (parts.length < 2) {
            throw new IllegalArgumentException(label + ": expected \"Month yyyy\", got: \"" + raw + "\"");
        }
        String yearStr = parts[parts.length - 1];
        if (!yearStr.matches("\\d{4}")) {
            throw new IllegalArgumentException(label + ": year must be 4 digits, got: \"" + raw + "\"");
        }
        int year = Integer.parseInt(yearStr);
        StringBuilder monthName = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length - 1; i++) {
            monthName.append(' ').append(parts[i]);
        }
        int month = CoreFunctions.monthToMonthNumber(monthName.toString());
        return new int[] { month, year };
    }

    private static long monthYearOrdinal(int month, int year) {
        return (long) year * 12L + month;
    }

    private void navigateCalendarUntilMonthYear(String monthYearRaw) {
        int[] target = parseMonthYearString(monthYearRaw, "target month/year");
        long targetOrd = monthYearOrdinal(target[0], target[1]);
        final int maxSteps = 48;
        for (int step = 0; step < maxSteps; step++) {
            String heading = readCalendarHeadingText(5);
            int[] cur = parseMonthYearString(heading, "calendar heading");
            long curOrd = monthYearOrdinal(cur[0], cur[1]);
            if (curOrd == targetOrd) {
                return;
            }
            if (curOrd > targetOrd) {
                clickCalendarPreviousMonthOnce();
            } else {
                clickCalendarNextMonthOnce();
            }
        }
        throw new IllegalStateException("Exceeded max steps navigating calendar to: " + monthYearRaw);
    }

    private void clickRandomDayInCalendarGrid() {
        WebElement modal = requireOpenCalendarModalContainer();
        List<WebElement> buttons = modal.findElements(By.xpath(".//div[contains(@class,'custom-calendar') or contains(@class,'custom_calendar') or contains(@class,'flex-custom-calendar')]"
                + "//button[contains(@class,'Day_day')][not(contains(@class,'disabled'))]"));
        if (buttons.isEmpty()) {
            buttons = modal.findElements(
                    By.xpath(".//button[contains(@class,'Day_day')][not(contains(@class,'disabled'))]"));
        }
        List<WebElement> clickable = new ArrayList<>();
        for (WebElement b : buttons) {
            try {
                if (!b.isDisplayed()) {
                    continue;
                }
                boolean hasDayNumber = false;
                for (WebElement span : b.findElements(By.xpath(".//span"))) {
                    String txt = span.getText().trim();
                    if (txt.matches("\\d{1,2}")) {
                        hasDayNumber = true;
                        break;
                    }
                }
                if (hasDayNumber) {
                    clickable.add(b);
                }
            } catch (Exception ignored) {
            }
        }
        if (clickable.isEmpty()) {
            throw new java.util.NoSuchElementException(
                    "No enabled calendar day cells found in date-picker modal");
        }
        int idx = CoreFunctions.generateRandomInt(0, clickable.size() - 1);
        clickWebElement(clickable.get(0));
    }

    private void dismissOpenDatePickerModalIfPresent() {
        try {
            Optional<WebElement> close = driverTools.waitForElementToAppear(CALENDAR_CLOSE_BUTTON, 5);
            if (close.isPresent() && close.get().isDisplayed()) {
                try {
                    close.get().click();
                } catch (Exception e) {
                    driverTools.clickWithJavascriptExecutor(CALENDAR_CLOSE_BUTTON);
                }
            }
        } catch (Exception ignored) {
        }
        if (driverTools.isElementDisplayed(MODAL_OVERLAY_BACKDROP)) {
            try {
                new Actions(driver).sendKeys(Keys.ESCAPE).perform();
            } catch (Exception ignored) {
            }
            driverTools.waitForElementToDisappear(MODAL_OVERLAY_BACKDROP, 5);
        }
    }

    //Opens the start-date picker, navigates with prev/next until the heading matches
    public void selectStartDateFromCalendar(String monthYear) {
        selectDateInFilterCalendarForMonthYear(START_DATE_INPUT_ID, START_DATE_RIGHT_SLOT_TRIGGER, monthYear);
        closeCalendar();
    }

    //Ensures any prior picker is closed, opens the end-date field, matches, picks a random
    public void selectEndDateFromCalendar(String monthYear) {
        closeCalendar();
        selectDateInFilterCalendarForMonthYear(END_DATE_INPUT_ID, END_DATE_RIGHT_SLOT_TRIGGER, monthYear);
        closeCalendar();
    }

    private boolean isDatePickerModalShowing() {
        return driverTools.waitForElementToAppear(CALENDAR_MONTH_HEADING_MOBILE_CLASS, 2).isPresent()
                || driverTools.waitForElementToAppear(CALENDAR_MONTH_YEAR_HEADING, 1).isPresent()
                || driverTools.waitForElementToBeClickable(CALENDAR_PREV_BUTTON_MOBILE, 2).isPresent()
                || driverTools.waitForElementToBeClickable(CALENDAR_PREV_BUTTON, 2).isPresent();
    }

    //Clicks the filter date text input, then the calendar icon slot if the modal did not open.
    private void openDateFilterPicker(Locator dateInput, Locator rightSlotTrigger) {
        driverTools.waitForElementToAppear(dateInput);
        driverTools.scrollIntoWebView(dateInput);
        try {
            driverTools.click(dateInput);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(dateInput);
        }
        if (!isDatePickerModalShowing()) {
            driverTools.waitForElementToAppear(rightSlotTrigger);
            driverTools.scrollIntoWebView(rightSlotTrigger);
            try {
                driverTools.click(rightSlotTrigger);
            } catch (ElementClickInterceptedException e) {
                driverTools.clickWithJavascriptExecutor(rightSlotTrigger);
            }
        }
    }

    private void selectDateInFilterCalendarForMonthYear(Locator dateInput, Locator rightSlotTrigger,
            String monthYear) {
        boolean completed = false;
        try {
            openDateFilterPicker(dateInput, rightSlotTrigger);
            readCalendarHeadingText(15);
            navigateCalendarUntilMonthYear(monthYear);
            clickRandomDayInCalendarGrid();
            completed = true;
        } finally {
            if (!completed) {
                dismissOpenDatePickerModalIfPresent();
            }
        }
    }

    //Closes the date-picker modal if it is open. Safe when the modal is already closed
    public void closeCalendar() {
        dismissOpenDatePickerModalIfPresent();
    }

    public void clickRuleNameField(String ruleName) {
        driverTools.waitForElementToAppear(SEARCH_RULE_NAME_FIELD_INPUT);
        driverTools.click(SEARCH_RULE_NAME_FIELD_INPUT);
        driverTools.sendKeys(SEARCH_RULE_NAME_FIELD_INPUT, ruleName);
    }

    public void clickSubmitButton() {
        driverTools.waitForElementToAppear(NEED_APPROVAL_SUBMIT_BUTTON);
        driverTools.click(NEED_APPROVAL_SUBMIT_BUTTON);
        driverTools.waitForElementToAppear(NEED_APPROVAL_SUBMIT_BUTTON);
    }

    public void clickRuleChangesButton() {
        driverTools.waitForElementToAppear(RULE_CHANGES_BUTTON);
        driverTools.click(RULE_CHANGES_BUTTON);
    }

    public boolean verifyRuleName(String ruleName) {
        WebElement textElement = driverTools.waitForElementToAppear(RULE_NAME_FROM_RESULT_TABLE, 10)
                .orElseThrow();
        String text = driverTools.getText(textElement);
        return text.contains(ruleName);
    }

    public void clickShowMineCheckBox() {
        driverTools.waitForElementToAppear(SHOW_MINE_ONLY_CHECKBOX);
        driverTools.click(SHOW_MINE_ONLY_CHECKBOX);
    }

    public void clickResetFilterButton() {
        driverTools.waitForElementToAppear(RESET_FILTER_BUTTON);
        driverTools.scrollElementToCenter(RESET_FILTER_BUTTON);
        try {
            driverTools.click(RESET_FILTER_BUTTON);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(RESET_FILTER_BUTTON);
        }
    }

    private static boolean isRuleNameSearchValueCleared(String valueAttribute) {
        return valueAttribute == null || valueAttribute.trim().isEmpty();
    }

    public boolean verifyResetFilter() {
        driverTools.waitForElementToAppear(SEARCH_RULE_NAME_FIELD_INPUT, 10).orElseThrow();
        final long deadlineMs = System.currentTimeMillis() + 10_000L;
        while (System.currentTimeMillis() < deadlineMs) {
            String text = driverTools.getValueAttribute(SEARCH_RULE_NAME_FIELD_INPUT, "value");
            if (isRuleNameSearchValueCleared(text)) {
                return true;
            }
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                String finalText = driverTools.getValueAttribute(SEARCH_RULE_NAME_FIELD_INPUT, "value");
                return isRuleNameSearchValueCleared(finalText);
            }
        }
        return isRuleNameSearchValueCleared(
                driverTools.getValueAttribute(SEARCH_RULE_NAME_FIELD_INPUT, "value"));
    }

    public boolean verifyApproveRejectButton(boolean shouldBeEnabled) {
        WebElement approveBtn = driverTools.waitForElementToAppear(APPROVE_BUTTON, 10).orElseThrow();
        boolean actualEnabledApprove = approveBtn.isEnabled();
        WebElement rejectBtn = driverTools.waitForElementToAppear(REJECT_BUTTON, 10).orElseThrow();
        boolean actualEnabledReject = rejectBtn.isEnabled();
        if (shouldBeEnabled) {
            return (actualEnabledApprove && actualEnabledReject);
        }
        return !(actualEnabledApprove && actualEnabledReject);
    }

    public void LogOut() {
        driverTools.waitForElementToAppear(USER_PROFILE_BUTTON, 3);
        driverTools.scrollIntoWebView(USER_PROFILE_BUTTON);
        try {
            driverTools.click(USER_PROFILE_BUTTON);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(USER_PROFILE_BUTTON);
        }
        driverTools.waitForElementToAppear(USER_LOGOUT_BUTTON, 3);
        driverTools.scrollElementToCenter(USER_LOGOUT_BUTTON);
        try {
            driverTools.click(USER_LOGOUT_BUTTON);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(USER_LOGOUT_BUTTON);
        }
    }

    public void clickOnRuleChangesButton(int buttonNumber) {
        if (buttonNumber == 1) {
            driverTools.waitForElementToAppear(RULE_CHANGES_BUTTON_1, 3);
            driverTools.click(RULE_CHANGES_BUTTON_1);
        } else if (buttonNumber == 2) {
            driverTools.waitForElementToAppear(RULE_CHANGES_BUTTON_2, 3);
            driverTools.click(RULE_CHANGES_BUTTON_2);
        } else if (buttonNumber == 3) {
            driverTools.waitForElementToAppear(RULE_CHANGES_BUTTON_3, 3);
            driverTools.click(RULE_CHANGES_BUTTON_3);
        }
    }

    public boolean verifyRuleChangesSection() {
        driverTools.waitForElementToAppear(PREVIOUS_RULE_DETAIL, 10).orElseThrow();
        driverTools.waitForElementToAppear(NEW_RULE_DETAIL, 10).orElseThrow();
        return (driverTools.isElementDisplayed(PREVIOUS_RULE_DETAIL)
                && driverTools.isElementDisplayed(NEW_RULE_DETAIL));
    }

    public boolean verifyPreviousRuleDetailInformation(String ruleName) {
        WebElement ruleChangesVertical = driverTools
                .waitForElementToAppear(RULE_CHANGES_VERTICAL_TESTING_PREV, 10)
                .orElseThrow();
        WebElement ruleChangesRuleName = driverTools.waitForElementToAppear(RULE_NAME_FROM_RESULT_TABLE, 10)
                .orElseThrow();
        WebElement ruleChangesRuleStatus = driverTools.waitForElementToAppear(RULE_CHANGES_RULE_STATUS_PREV, 10)
                .orElseThrow();
        String vertical = ruleChangesVertical.getText();
        String rule = ruleChangesRuleName.getText();
        String status = ruleChangesRuleStatus.getText();
        return (vertical.equals("TESTING") && rule.equals(ruleName) && status.equals("Paused"));
    }

    public boolean verifyNewRuleDetailInformation(String ruleName) {
        WebElement ruleChangesVertical = driverTools
                .waitForElementToAppear(RULE_CHANGES_VERTICAL_TESTING_NEW, 10)
                .orElseThrow();
        WebElement ruleChangesRuleName = driverTools.waitForElementToAppear(RULE_CHANGES_RULE_NAME_NEW, 10)
                .orElseThrow();
        WebElement ruleChangesRuleStatus = driverTools.waitForElementToAppear(RULE_CHANGES_RULE_STATUS_NEW, 10)
                .orElseThrow();
        String vertical = ruleChangesVertical.getText();
        String rule = ruleChangesRuleName.getText();
        String status = ruleChangesRuleStatus.getText();
        return (vertical.equals("TESTING") && rule.equals(ruleName) && status.equals("Paused"));
    }

    private static String toXPathLiteral(String value) {
        if (value == null) {
            return "''";
        }
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        StringBuilder xpathLiteral = new StringBuilder("concat(");
        String[] parts = value.split("'", -1);
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                xpathLiteral.append(", \"'\", ");
            }
            xpathLiteral.append("'").append(parts[i]).append("'");
        }
        xpathLiteral.append(")");
        return xpathLiteral.toString();
    }

    private static Locator ruleChangesUpdatedByLocator(String username) {
        String v = username == null ? "" : username.trim();
        if (v.isEmpty()) {
            throw new IllegalArgumentException("userName must not be blank for Rule Changes updated-by");
        }
        return new Locator("RULE_CHANGES_UPDATED_BY",
                By.xpath("//p[normalize-space()='Updated By']/following-sibling::p[contains(@class,'highEmphasis') and normalize-space()="
                + toXPathLiteral(v) + "]"));
    }

    public boolean verifyEditButtonForBelongingUserIsEnabled(String username) {
        Locator updatedBy = ruleChangesUpdatedByLocator(username);
        WebElement ruleChangesUpdatedBy = driverTools.waitForElementToAppear(updatedBy, 10).orElseThrow();
        String userEmail = ruleChangesUpdatedBy.getText();
        String expected = username == null ? "" : username.trim();
        return driverTools.isElementDisplayed(RULE_CHANGES_RULE_EDIT_BUTTON)
                && userEmail.trim().equals(expected);
    }

    public boolean verifyEditButtonForBelongingUserIsDisabled(String username) {
        WebElement ruleChangesUpdatedBy = driverTools.waitForElementToAppear(VERIFY_UPDATE_BY_EMAIL, 10)
                .orElseThrow();
        String userEmail = ruleChangesUpdatedBy.getText();
        String expected = username == null ? "" : username.trim();
        return !driverTools.isElementDisplayed(RULE_CHANGES_RULE_EDIT_BUTTON)
                && !userEmail.trim().equals(expected);
    }

    public boolean verifyEditButtonOnRulesForNonApproverIsDisabled() {
        return (!(driverTools.isElementDisplayed(RULE_CHANGES_RULE_EDIT_BUTTON))
                && verifyApproveRejectButton(false));
    }

    public String verifyPreviousRuleDetailIsBlank() {
        WebElement blankPreviousRule = driverTools.waitForElementToAppear(PREVIOUS_RULE_DETAIL_CONTAINER, 10)
                .orElseThrow();
        String str = driverTools.getText(blankPreviousRule);
        return str;
    }
}
