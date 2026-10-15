package com.tiket.web.dashboard.page.ttd.Ignis.IgnisManageOrderPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.util.*;

public class IgnisManageOrderPageWeb<T extends WebDriver> extends WebPage<T> {
    public IgnisManageOrderPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> IgnisManageOrderPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new IgnisManageOrderPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new IgnisManageOrderPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    // Manage Order Page
    private static final Locator TTD_DROPDOWN = new Locator("TTD_DROPDOWN", By.xpath("//div[contains(@class, 'form-control form-select filterable-select list-filter__product')]//fieldset"));
    private static final Locator TTD_DROPDOWN_SEARCH_FIELD = new Locator("TTD_DROPDOWN_SEARCH_FIELD", By.xpath("//input[@name='filterable-select-listitem-search']"));
    private static final Locator TTD_DROPDOWN_SELECTION = new Locator("TTD_DROPDOWN_SELECTION", By.xpath("//div[@class='menu-item']"));
    private static final Locator PACKAGE_DROPDOWN = new Locator("PACKAGE_DROPDOWN", By.xpath("//div[@class='form-control form-select filterable-select list-filter__packages']//fieldset"));
    private static final Locator PACKAGE_DROPDOWN_SEARCH_FIELD = new Locator("PACKAGE_DROPDOWN_SEARCH_FIELD", By.xpath("//input[@name='filterable-select-listitem-search']"));
    private static final Locator PACKAGE_DROPDOWN_SELECTION = new Locator("PACKAGE_DROPDOWN_SELECTION", By.xpath("//div[@class='menu-item']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[contains(@class, 'actions-item__search')]"));
    private static final Locator TABLE_ROWS = new Locator("TABLE_ROWS", By.xpath("//div[@class='order-list__item order-item']"));
    private static final Locator MANAGE_ORDER_BUTTON = new Locator("MANAGE_ORDER_BUTTON", By.xpath("//button[contains(@class, 'action-item__manage-order')]"));
    private static final Locator ORDER_DETAIL_ID_INPUT = new Locator("ORDER_DETAIL_ID_INPUT", By.xpath("//input[@name='orderId']"));

    // Update Order Pop Up
    private static final Locator UPDATE_ORDER_POPUP = new Locator("UPDATE_ORDER_POPUP", By.xpath("//div[@class='main__dialog bulk-update-dialog']"));
    private static final Locator NEW_VISIT_DATE_DATE_PICKER = new Locator("NEW_VISIT_DATE_DATE_PICKER", By.xpath("//div[@class='inputs-item__date']//fieldset"));
    private static final Locator NEW_VISIT_DATE_POPUP = new Locator("NEW_VISIT_DATE_POPUP", By.xpath("//div[contains(@class, 'date-picker-popup')]"));
    private static final Locator NEW_VISIT_DATE_POPUP_NEXT_MONTH_BUTTON = new Locator("NEW_VISIT_DATE_POPUP_NEXT_MONTH_BUTTON", By.xpath("//button[@style='float: right;']"));
    private static final Locator NEW_VISIT_DATE_POPUP_DATE_SELECTOR_DAYS = new Locator("NEW_VISIT_DATE_POPUP_DATE_SELECTOR_DAYS", By.xpath("//div[contains(@class, 'DayPicker-Day') and @aria-disabled='false']"));
    private static final Locator TIME_SLOT_DROPDOWN = new Locator("TIME_SLOT_DROPDOWN", By.xpath("//div[@class='inputs-item__timeslot']"));
    private static final Locator TIME_SLOT_DROPDOWN_SELECTION = new Locator("TIME_SLOT_DROPDOWN_SELECTION", By.xpath("//button[contains(@class, 'menu-item')]"));
    private static final Locator UPDATE_ORDER_POPUP_OK_BUTTON = new Locator("UPDATE_ORDER_POPUP_OK_BUTTON", By.xpath("//button[@class='btn btn-primary']"));

    // Preview Pop Up
    private static final Locator PREVIEW_POPUP = new Locator("PREVIEW_POPUP", By.xpath("//div[@class='dialog-preview-manage-order']"));
    private static final Locator PREVIEW_POPUP_NEW_DATE_TEXT = new Locator("PREVIEW_POPUP_NEW_DATE_TEXT", By.xpath("//div[@class='section-value text-green']"));
    private static final Locator PREVIEW_POPUP_APPLY_BUTTON = new Locator("PREVIEW_POPUP_APPLY_BUTTON", By.xpath("//div[@class='preview-action']//button[2]"));

    // Apply Update Pop Up
    private static final Locator APPLY_UPDATE_POPUP = new Locator("APPLY_UPDATE_POPUP", By.xpath("//div[contains(@class, 'dialog-submit-manage-order')]"));
    private static final Locator APPLY_UPDATE_POPUP_CONTINUE_BUTTON = new Locator("APPLY_UPDATE_POPUP_CONTINUE_BUTTON", By.xpath("//button[@class='button variant-primary size-medium horizontal-padding']"));

    // Update Pop Up
    private static final Locator UPDATE_POPUP = new Locator("UPDATE_POPUP", By.xpath("//div[contains(@class, 'dialog-submit-manage-order')]"));
    private static final Locator UPDATE_POPUP_ORDER_REPORT_BUTTON = new Locator("UPDATE_POPUP_ORDER_REPORT_BUTTON", By.xpath("//div[@class='btn-container']/button[1]"));

    // Check Progress Log Pop Up
    private static final Locator PROGRESS_LOG_POPUP = new Locator("PROGRESS_LOG_POPUP", By.xpath("//div[contains(@class, 'dialog-submit-manage-order')]"));
    private static final Locator PROGRESS_LOG_POPUP_PRIMARY_BUTTON = new Locator("PROGRESS_LOG_POPUP_PRIMARY_BUTTON", By.xpath("//button[@class='button variant-primary size-medium horizontal-padding']"));

    private void setFilterWithSearchBar(String filterType, String name, Locator dropdown, Locator dropdownSelection, Locator dropdownSearch) {
        driverTools.waitForElementToAppear(dropdown);
        driverTools.click(dropdown);
        driverTools.dummyWait(1);

        inputDropdownSearchBar(dropdownSearch, name.toLowerCase());
        driverTools.dummyWait(1);

        List<WebElement> selections = driverTools.elementsUntilExist(dropdownSelection).orElseThrow();
        boolean found = false;
        for (WebElement e : selections) {
            String elementText = driverTools.getText(e).toLowerCase();
            if (elementText.equalsIgnoreCase(name)) {
                found = true;
                driverTools.click(e);
                break;
            }
        }

        if (!found) {
            throw new NoSuchElementException(String.format("There is no %s filter '%s'", filterType, name));
        }
    }

    public void setTTDFilter(String ttdName) {
        setFilterWithSearchBar("product", ttdName, TTD_DROPDOWN, TTD_DROPDOWN_SELECTION, TTD_DROPDOWN_SEARCH_FIELD);
    }

    public void setPackageFilter(String packageName) {
        setFilterWithSearchBar("product", packageName, PACKAGE_DROPDOWN, PACKAGE_DROPDOWN_SELECTION, PACKAGE_DROPDOWN_SEARCH_FIELD);
    }

    public void clickSearchButton() {
        driverTools.scrollElementToCenter(SEARCH_BUTTON);
        driverTools.click(SEARCH_BUTTON);
        driverTools.dummyWait(1);
    }

    public List<String> selectRandomRow(int amount) {
        List<WebElement> tableRows = driverTools.waitForElementsToAppear(TABLE_ROWS).orElse(null);
        if (tableRows == null) throw new NoSuchElementException("No Data");

        List<String> selectedOrderIds = new ArrayList<>();
        List<Integer> randomRows = generateNonRepeatingRandomNumbers(tableRows.size(), amount);
        for (Integer row: randomRows) {
            checkTableRow(row);
            selectedOrderIds.add(getOrderIdFromTableRow(row));
        }

        return selectedOrderIds;
    }

    public void clickManageOrderButton() {
        driverTools.scrollElementToCenter(MANAGE_ORDER_BUTTON);
        driverTools.click(MANAGE_ORDER_BUTTON);
    }

    public void inputNewVisitDate(int daysAmount) {
        driverTools.waitForElementToAppear(UPDATE_ORDER_POPUP);
        driverTools.click(NEW_VISIT_DATE_DATE_PICKER);
        driverTools.waitForElementToAppear(NEW_VISIT_DATE_POPUP);

        LocalDate todaysDate = LocalDate.now();
        LocalDate targetDate = LocalDate.now().plusDays(daysAmount);

        int monthDifference = getMonthDifference(todaysDate, targetDate);

        for (int i = 0; i < monthDifference; i++) {
            driverTools.click(NEW_VISIT_DATE_POPUP_NEXT_MONTH_BUTTON);
            driverTools.dummyWait(1);
        }

        List<WebElement> days = driverTools.waitForElementsToAppear(NEW_VISIT_DATE_POPUP_DATE_SELECTOR_DAYS).get();
        driverTools.click(days.get(targetDate.getDayOfMonth() - 1));
        driverTools.dummyWait(2);
    }

    public void inputNewTimeVisit() {
        if (driverTools.element(TIME_SLOT_DROPDOWN).orElse(null) == null) {
            logger.softWarning("No dropdown for new time visit. Skipping.");
        } else {
            driverTools.click(TIME_SLOT_DROPDOWN);
            List<WebElement> selections = driverTools.waitForElementsToAppear(TIME_SLOT_DROPDOWN_SELECTION).get();
            WebElement selection = selections.get(CoreFunctions.generateRandomInt(0, selections.size()));
            driverTools.click(selection);
        }
    }

    public void clickOkUpdateOrderPopUp() {
        driverTools.click(UPDATE_ORDER_POPUP_OK_BUTTON);
    }

    public String getNewUpdatedDate() {
        driverTools.waitForElementToAppear(PREVIEW_POPUP);
        return driverTools.getText(PREVIEW_POPUP_NEW_DATE_TEXT).orElseThrow();
    }

    public void clickOkPreviewPopUp() {
        driverTools.click(PREVIEW_POPUP_APPLY_BUTTON);
    }

    public void clickOkApplyPopUp() {
        driverTools.waitForElementToAppear(APPLY_UPDATE_POPUP);
        driverTools.click(APPLY_UPDATE_POPUP_CONTINUE_BUTTON);
    }

    public static int getMonthDifference(LocalDate date1, LocalDate date2) {
        int months1 = date1.getYear() * 12 + date1.getMonthValue();
        int months2 = date2.getYear() * 12 + date2.getMonthValue();

        return months2 - months1;
    }

    private String getOrderIdFromTableRow(int index) {
        String name = "TABLE_ROWS_ORDER_ID_" + index;
        String xpathString = String.format("//div[@class='order-list__item order-item'][%s]//div[@class='heading__value'][1]", index);
        Locator locator = new Locator(name, By.xpath(xpathString));
        return driverTools.getText(locator).orElse("");
    }

    public void checkTableRow(int index) {
        String name = "TABLE_ROWS_CHECKBOX_" + index;
        String xpathString = String.format("//div[@class='order-list__item order-item'][%s]//div[@class='checkbox label-left']", index);
        Locator locator = new Locator(name, By.xpath(xpathString));
        driverTools.scrollElementToCenter(locator);
        driverTools.click(locator);
    }

    private static List<Integer> generateNonRepeatingRandomNumbers(int max, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot be negative.");
        }
        if (max < 1) {
            throw new IllegalArgumentException("Max must be at least 1 when generating numbers from 1.");
        }
        if (count > max) {
            throw new IllegalArgumentException("Cannot generate " + count + " unique numbers from a range of " + max + " elements (1 to " + max + ").");
        }

        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= max; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        return numbers.subList(0, count);
    }

    private void inputDropdownSearchBar(Locator searchField, String name) {
        driverTools.waitForElementToAppear(searchField);
        driverTools.clearInput(searchField);
        driverTools.sendKeys(searchField, name);
    }

    public void setOrderDetailIdFilter(String orderDetailId) {
        driverTools.waitForElementToAppear(ORDER_DETAIL_ID_INPUT);
        driverTools.clearInput(ORDER_DETAIL_ID_INPUT);
        driverTools.sendKeys(ORDER_DETAIL_ID_INPUT, orderDetailId);
    }

    public void inputNewValidityDate(String dateExtendRange) {
        int randomInt = CoreFunctions.generateRandomInt(Integer.parseInt(dateExtendRange.split("-")[0]), Integer.parseInt(dateExtendRange.split("-")[1]));
        inputNewVisitDate(randomInt);
    }

    public void clickGoToProgressLog() {
        driverTools.dummyWait(1);
        driverTools.waitForElementToAppear(PROGRESS_LOG_POPUP);
        driverTools.click(PROGRESS_LOG_POPUP_PRIMARY_BUTTON);
    }
}
