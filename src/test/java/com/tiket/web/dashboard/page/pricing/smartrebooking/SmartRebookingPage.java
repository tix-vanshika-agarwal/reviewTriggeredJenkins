package com.tiket.web.dashboard.page.pricing.smartrebooking;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.*;
import java.util.List;

public class SmartRebookingPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator SMART_REBOOKING_SIDEBAR_BUTTON = new Locator("SMART_REBOOKING_SIDEBAR_BUTTON", new By.ByXPath("//button/span[contains(text(),'Smart Rebooking')]"));
    private static final Locator SMART_REBOOKING_TITLE = new Locator("SMART_REBOOKING_TITLE", new By.ByXPath("//div/span[contains(text(),'Smart Rebooking')]"));
    private static final Locator SMART_REBOOKING_SEARCH_BUTTON = new Locator("SMART_REBOOKING_SEARCH_BUTTON", new By.ByXPath("//button[contains(text(),'Search')]"));
    private static final Locator SMART_REBOOKING_RESET_FILTER_BUTTON = new Locator("SMART_REBOOKING_RESET_FILTER_BUTTON", new By.ByXPath("//button[@type='button' and contains(normalize-space(),'Reset Filter')]"));
    private static final Locator ORDER_ID_INPUT = new Locator("ORDER_ID_INPUT", new By.ByXPath("//input[@id='order-id']"));
    private static final Locator ORDER_DETAIL_ID_INPUT = new Locator("ORDER_DETAIL_ID_INPUT", new By.ByXPath("//input[@id='order-detail-id']"));
    private static final Locator ITINERARY_ID_INPUT = new Locator("ITINERARY_ID_INPUT", new By.ByXPath("//input[@id='itinerary-id']"));
    private static final Locator ROOM_SUPPLIER_NAME_INPUT = new Locator("ROOM_SUPPLIER_NAME_INPUT", new By.ByXPath("//input[@id='room-supplier-name']"));

    private static Locator filterChipAddSuggestionRow(String locatorName, String typedToken) {
        String labelPredicate;
        if (!typedToken.contains("'") && !typedToken.contains("\"")) {
            labelPredicate = "(contains(., 'Add \"" + typedToken + "\" as new value') or contains(., 'Add \""
                    + typedToken + "\" to'))";
        } else {
            labelPredicate = "(contains(., 'as new value') or contains(., 'Add to'))";
        }
        String xpath = "//div[contains(@class,'cursor-pointer')]"
                + "[.//span[contains(@class,'Text_text__') and contains(@class,'Text_variant_activity__') and "
                + labelPredicate + "]]";
        return new Locator(locatorName, new By.ByXPath(xpath));
    }
    private static final Locator ORDER_STATUS_DROPDOWN = new Locator("ORDER_STATUS_DROPDOWN", new By.ByXPath("//input[@id='order-status']"));
    private static final Locator ORDER_STATUS_BUTTON_ALL = new Locator("ORDER_STATUS_BUTTON_ALL", new By.ByXPath("(//span[text()='All'])[2]"));
    private static final Locator ORDER_STATUS_BUTTON_E_TICKET_ISSUED = new Locator("ORDER_STATUS_BUTTON_E_TICKET_ISSUED", new By.ByXPath("//span[contains(text(),'Eticket Issused')]"));
    private static final Locator ORDER_STATUS_BUTTON_PROCESSING_REFUND = new Locator("ORDER_STATUS_BUTTON_PROCESSING_REFUND", new By.ByXPath("//span[contains(text(),'Processing Refund')]"));
    private static final Locator ORDER_STATUS_BUTTON_REFUNDED = new Locator("ORDER_STATUS_BUTTON_REFUNDED", new By.ByXPath("//span[contains(text(),'Refunded')]"));
    private static final Locator ORDER_STATUS_BUTTON_RESCHEDULED = new Locator("ORDER_STATUS_BUTTON_RESCHEDULED", new By.ByXPath("//span[contains(text(),'Rescheduled')]"));
    private static final Locator ORDER_STATUS_BUTTON_REBOOKED = new Locator("ORDER_STATUS_BUTTON_REBOOKED", new By.ByXPath("//span[contains(text(),'Rebooked')]"));
    private static final Locator ORDER_STATUS_BUTTON_INACTIVE = new Locator("ORDER_STATUS_BUTTON_INACTIVE", new By.ByXPath("//span[contains(text(),'Inactive')]"));
    private static final Locator ALLOCATION_DROPDOWN = new Locator("ALLOCATION_DROPDOWN", new By.ByXPath("//input[@id='allocation']"));
    private static final Locator ALLOCATION_BUTTON_ALL = new Locator("ALLOCATION_BUTTON_ALL", new By.ByXPath("(//span[text()='All'])[1]"));
    private static final Locator ALLOCATION_BUTTON_TIKET = new Locator("ALLOCATION_BUTTON_TIKET", new By.ByXPath("//span[contains(text(),'Tiket')]"));
    private static final Locator ALLOCATION_BUTTON_ELBOWS = new Locator("ALLOCATION_BUTTON_ELBOWS", new By.ByXPath("//span[contains(text(),'Elbows')]"));
    private static final Locator ALLOCATION_BUTTON_UNKNOWN = new Locator("ALLOCATION_BUTTON_UNKNOWN", new By.ByXPath("//span[contains(text(),'Unknown')]"));
    private static final Locator SORT_BY_ORDER_CREATED_DATE = new Locator("SORT_BY_ORDER_CREATED_DATE", new By.ByXPath("//span[contains(text(),'Order Created Date')]"));
    private static final Locator PAGINATION_PREVIOUS_PAGE = new Locator("PAGINATION_PREVIOUS_PAGE", new By.ByXPath("//button[@aria-label='Previous Page']"));
    private static final Locator PAGINATION_NEXT_PAGE = new Locator("PAGINATION_NEXT_PAGE", new By.ByXPath("//button[@aria-label='Next Page']"));
    private static final Locator PAGINATION_DROPDOWN = new Locator("PAGINATION_DROPDOWN", new By.ByXPath("//input[@aria-label='Pagination Dropdown']"));
    private static final Locator NO_DATA_FOUND_RESET_FILTER_BUTTON = new Locator("NO_DATA_FOUND_RESET_FILTER_BUTTON", new By.ByXPath("//button[contains(@class,'Button_variant_primary') and normalize-space()='Reset Filter']"));
    private static final Locator NO_DATA_FOUND_TEXT = new Locator("NO_DATA_FOUND_TEXT", new By.ByXPath("//span[contains(text(),'No results found')]"));
    private static final Locator ACTION_MORE_BUTTON = new Locator("ACTION_MORE_BUTTON", new By.ByXPath("(//span[text()='More'])[1]"));
    private static final Locator ACTION_ACTIVITY_LOG_BUTTON = new Locator("ACTION_ACTIVITY_LOG_BUTTON", new By.ByXPath("(//div[@data-testid='action-button-option-undefined-Activity log'])[1]"));
    private static final Locator ACTION_PRICE_DROP_DETAIL_BUTTON = new Locator("ACTION_PRICE_DROP_DETAIL_BUTTON", new By.ByXPath("(//div[@data-testid='action-button-option-undefined-Price drop detail'])[1]"));
    private static final Locator ACTION_ACTIVITY_LOG_TITLE = new Locator("ACTION_ACTIVITY_LOG_TITLE", new By.ByXPath("//h2[contains(text(),'Activity Log')]"));
    private static final Locator ACTION_PRICE_DROP_DETAIL_TITLE = new Locator("ACTION_PRICE_DROP_DETAIL_TITLE", new By.ByXPath("//h2[contains(text(),'Price drop details')]"));
    private static final Locator ACTION_MODAL_CLOSE_BUTTON = new Locator("ACTION_MODAL_CLOSE_BUTTON", new By.ByXPath("//button[contains(@class, 'Modal_modal_close__Y2jEm')]"));
    private static final Locator USER_PROFILE_BUTTON = new Locator("USER_PROFILE_BUTTON", new By.ByXPath("//div[contains(@class, 'flex') and contains(@class, 'items-center') and contains(@class, 'px-1')]"));
    private static final Locator USER_LOGOUT_BUTTON = new Locator("USER_LOGOUT_BUTTON", new By.ByXPath("//span[text()='Log Out']"));

    public SmartRebookingPage(Session<T> session) {
        super(session);
    }

    public void maximize() {
        driverTools.maximizeWindow();
    }

    public void clickSmartRebookingSidebarButton() {
        driverTools.waitForElementToAppear(SMART_REBOOKING_SIDEBAR_BUTTON, 3);
        driverTools.click(SMART_REBOOKING_SIDEBAR_BUTTON);
    }

    public boolean isSmartRebookingPageLoaded() {
        driverTools.waitForElementToAppear(SMART_REBOOKING_TITLE, 3);
        return driverTools.isElementDisplayed(SMART_REBOOKING_TITLE);
    }

    public void clickSmartRebookingSearchButton() {
        driverTools.waitForElementToAppear(SMART_REBOOKING_SEARCH_BUTTON, 3);
        driverTools.click(SMART_REBOOKING_SEARCH_BUTTON);
    }

    public void clickSmartRebookingResetFilterButton() {
        driverTools.waitForElementToAppear(SMART_REBOOKING_RESET_FILTER_BUTTON, 3);
        driverTools.clickWithJavascriptExecutor(SMART_REBOOKING_RESET_FILTER_BUTTON);
    }

    public void inputOrderId(List<String> orderIds) {
        driverTools.waitForElementToAppear(ORDER_ID_INPUT, 3);
        for (String orderId : orderIds) {
            driverTools.click(ORDER_ID_INPUT);
            driverTools.sendKeys(ORDER_ID_INPUT, orderId);
            Locator chipRow = filterChipAddSuggestionRow("ORDER_ID_INPUT_BUTTON", orderId);
            driverTools.waitForElementToAppear(chipRow, 10);
            driverTools.clickWithJavascriptExecutor(chipRow);
        }
    }

    public void inputRandomOrderId() {
        driverTools.waitForElementToAppear(ORDER_ID_INPUT, 3);
        driverTools.click(ORDER_ID_INPUT);
        String randomId = Math.random() + "";
        driverTools.sendKeys(ORDER_ID_INPUT, randomId);
        Locator chipRow = filterChipAddSuggestionRow("ORDER_ID_INPUT_BUTTON", randomId);
        driverTools.waitForElementToAppear(chipRow, 10);
        driverTools.clickWithJavascriptExecutor(chipRow);
    }

    public boolean isOrderIDExist(List<String> orderIds) {
        for (String orderId : orderIds) {
            Locator orderIdLocator = new Locator("ORDER_ID", new By.ByXPath("//td[contains(.," + "'" + orderId + "')]"));
            if (driverTools.waitForElementToAppear(orderIdLocator, 10).isPresent()) {
                return true;
            }
        }
        return false;
    }

    public void inputOrderDetailId(List<String> orderDetailIds) {
        driverTools.waitForElementToAppear(ORDER_DETAIL_ID_INPUT, 3);
        for (String orderDetailId : orderDetailIds) {
            driverTools.click(ORDER_DETAIL_ID_INPUT);
            driverTools.sendKeys(ORDER_DETAIL_ID_INPUT, orderDetailId);
            Locator chipRow = filterChipAddSuggestionRow("ORDER_DETAIL_ID_INPUT_BUTTON", orderDetailId);
            driverTools.waitForElementToAppear(chipRow, 10);
            driverTools.clickWithJavascriptExecutor(chipRow);
        }
    }

    public boolean isOrderDetailIDExist(List<String> orderDetailIds) {
        for (String orderDetailId : orderDetailIds) {
            Locator orderDetailIdLocator = new Locator("ORDER_DETAIL_ID", new By.ByXPath("//td[@data-testid and contains(@data-testid, 'orderDetailId') and contains(., '" + orderDetailId + "')]"));
            if (driverTools.waitForElementToPresence(orderDetailIdLocator, 10).isPresent()) {
                return true;
            }
        }
        return false;
    }

    public void inputItinerarylId(List<String> ItineraryIds) {
        driverTools.waitForElementToAppear(ITINERARY_ID_INPUT, 3);
        for (String itineraryId : ItineraryIds) {
            driverTools.click(ITINERARY_ID_INPUT);
            driverTools.sendKeys(ITINERARY_ID_INPUT, itineraryId);
            Locator chipRow = filterChipAddSuggestionRow("ITINERARY_ID_INPUT_BUTTON", itineraryId);
            driverTools.waitForElementToAppear(chipRow, 10);
            driverTools.clickWithJavascriptExecutor(chipRow);
        }
    }

    public boolean isItineraryIDExist(List<String> itineraryIds) {
        for (String itineraryId : itineraryIds) {
            Locator itineraryIdLocator = new Locator("ITINERARY_ID", new By.ByXPath("//div[@title='" + itineraryId + "']"));
            if (driverTools.waitForElementToPresence(itineraryIdLocator, 10).isPresent()) {
                return true;
            }
        }
        return false;
    }

    public void inputRoomSupplierName(String roomSupplierName) {
        driverTools.waitForElementToAppear(ROOM_SUPPLIER_NAME_INPUT, 3);
        driverTools.click(ROOM_SUPPLIER_NAME_INPUT);
        driverTools.sendKeys(ROOM_SUPPLIER_NAME_INPUT, roomSupplierName);
        Locator chipRow = filterChipAddSuggestionRow("ROOM_SUPPLIER_NAME_INPUT_BUTTON", roomSupplierName);
        driverTools.waitForElementToAppear(chipRow, 10);
        driverTools.clickWithJavascriptExecutor(chipRow);
    }

    public boolean isRoomSupplierNameExist(String roomSupplierName) {
        Locator roomSupplierNameLocator = new Locator("ROOM_SUPPLIER_NAME", new By.ByXPath("//td[contains(.," + "'" + roomSupplierName + "')]"));
        return driverTools.waitForElementToAppear(roomSupplierNameLocator, 10).isPresent();
    }

    public void inputOrderStatusAll() {
        driverTools.waitForElementToAppear(ORDER_STATUS_DROPDOWN, 5);
        driverTools.click(ORDER_STATUS_DROPDOWN);
        driverTools.waitForElementToAppear(ORDER_STATUS_BUTTON_ALL, 5);
        driverTools.click(ORDER_STATUS_BUTTON_ALL);
    }

    public void inputOrderStatusEticketIssued() {
        driverTools.waitForElementToAppear(ORDER_STATUS_DROPDOWN, 5);
        driverTools.click(ORDER_STATUS_DROPDOWN);
        driverTools.waitForElementToAppear(ORDER_STATUS_BUTTON_E_TICKET_ISSUED, 5);
        driverTools.click(ORDER_STATUS_BUTTON_E_TICKET_ISSUED);
    }

    public void inputOrderStatusProcessingRefund() {
        driverTools.waitForElementToAppear(ORDER_STATUS_DROPDOWN, 5);
        driverTools.click(ORDER_STATUS_DROPDOWN);
        driverTools.waitForElementToAppear(ORDER_STATUS_BUTTON_PROCESSING_REFUND, 5);
        driverTools.click(ORDER_STATUS_BUTTON_PROCESSING_REFUND);
    }

    public void inputOrderStatusRefunded() {
        driverTools.waitForElementToAppear(ORDER_STATUS_DROPDOWN, 5);
        driverTools.click(ORDER_STATUS_DROPDOWN);
        driverTools.waitForElementToAppear(ORDER_STATUS_BUTTON_REFUNDED, 5);
        driverTools.click(ORDER_STATUS_BUTTON_REFUNDED);
    }

    public void inputOrderStatusRescheduled() {
        driverTools.waitForElementToAppear(ORDER_STATUS_DROPDOWN, 5);
        driverTools.click(ORDER_STATUS_DROPDOWN);
        driverTools.waitForElementToAppear(ORDER_STATUS_BUTTON_RESCHEDULED, 5);
        driverTools.click(ORDER_STATUS_BUTTON_RESCHEDULED);
    }

    public void inputOrderStatusRebooked() {
        driverTools.waitForElementToAppear(ORDER_STATUS_DROPDOWN, 5);
        driverTools.click(ORDER_STATUS_DROPDOWN);
        driverTools.waitForElementToAppear(ORDER_STATUS_BUTTON_REBOOKED, 5);
        driverTools.click(ORDER_STATUS_BUTTON_REBOOKED);
    }

    public void inputOrderStatusInactive() {
        driverTools.waitForElementToAppear(ORDER_STATUS_DROPDOWN, 5);
        driverTools.click(ORDER_STATUS_DROPDOWN);
        driverTools.waitForElementToAppear(ORDER_STATUS_BUTTON_INACTIVE, 5);
        driverTools.click(ORDER_STATUS_BUTTON_INACTIVE);
    }

    public void inputAllocationAll() {
        driverTools.waitForElementToAppear(ALLOCATION_DROPDOWN, 5);
        driverTools.click(ALLOCATION_DROPDOWN);
        driverTools.waitForElementToAppear(ALLOCATION_BUTTON_ALL, 5);
        driverTools.click(ALLOCATION_BUTTON_ALL);
    }

    public void inputAllocationTiket() {
        driverTools.waitForElementToAppear(ALLOCATION_DROPDOWN, 5);
        driverTools.click(ALLOCATION_DROPDOWN);
        driverTools.waitForElementToAppear(ALLOCATION_BUTTON_TIKET, 5);
        driverTools.click(ALLOCATION_BUTTON_TIKET);
    }

    public void inputAllocationElbows() {
        driverTools.waitForElementToAppear(ALLOCATION_DROPDOWN, 5);
        driverTools.click(ALLOCATION_DROPDOWN);
        driverTools.waitForElementToAppear(ALLOCATION_BUTTON_ELBOWS, 5);
        driverTools.click(ALLOCATION_BUTTON_ELBOWS);
    }

    public void inputAllocationUnknown() {
        driverTools.waitForElementToAppear(ALLOCATION_DROPDOWN, 5);
        driverTools.click(ALLOCATION_DROPDOWN);
        driverTools.waitForElementToAppear(ALLOCATION_BUTTON_UNKNOWN, 5);
        driverTools.click(ALLOCATION_BUTTON_UNKNOWN);
    }

    public void sortByOrderCreatedDate() {
        driverTools.waitForElementToAppear(SORT_BY_ORDER_CREATED_DATE, 3);
        driverTools.click(SORT_BY_ORDER_CREATED_DATE);
    }

    public List<String> getOrderCreatedDate() {
        return driverTools.getTexts(new Locator("ORDER_CREATED_DATE", new By.ByXPath("//tbody/tr/td[13]")));
    }

    public boolean isOrderCreatedDateSortedDescending() {
        List<String> orderCreatedDate = getOrderCreatedDate();
        for (int i = 0; i < orderCreatedDate.size() - 1; i++) {
            if (orderCreatedDate.get(i).compareTo(orderCreatedDate.get(i + 1)) < 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isOrderCreatedDateSortedAscending() {
        List<String> orderCreatedDate = getOrderCreatedDate();
        for (int i = 0; i < orderCreatedDate.size() - 1; i++) {
            if (orderCreatedDate.get(i).compareTo(orderCreatedDate.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }

    public String getTotalItems() {
        Locator TOTAL_ITEMS = new Locator("TOTAL_ITEMS", new By.ByXPath("//span[starts-with(text(),'Showing 1 to')]"));
        driverTools.waitForElementToAppear(TOTAL_ITEMS, 3);
        return driverTools.getText(TOTAL_ITEMS).orElseThrow().split(" ")[5];
    }

    public String getMinPage() {
        return "1";
    }

    public String getMaxPage() {
        String TOTAL_ITEMS = getTotalItems();
        int totalItems = Integer.parseInt(TOTAL_ITEMS);
        int maxPage = (totalItems + 9) / 10;
        return String.valueOf(maxPage);
    }
    public void scrollOnceAndClickNextPage(int numberOfClicks) {
        driverTools.waitForElementToAppear(PAGINATION_NEXT_PAGE);
        driverTools.scrollToElement(PAGINATION_NEXT_PAGE);

        for (int i = 0; i < numberOfClicks; i++) {
            clickPaginationNextPage();
        }
    }

    public void scrollOnceAndClickPreviousPage(int numberOfClicks) {
        driverTools.waitForElementToAppear(PAGINATION_PREVIOUS_PAGE);
        driverTools.scrollToElement(PAGINATION_PREVIOUS_PAGE);

        for (int i = 0; i < numberOfClicks; i++) {
            clickPaginationPreviousPage();
        }
    }

    public void clickPaginationPreviousPage() {
        driverTools.waitForElementToAppear(PAGINATION_PREVIOUS_PAGE);
        driverTools.waitForElementToBeClickable(PAGINATION_PREVIOUS_PAGE);
        driverTools.click(PAGINATION_PREVIOUS_PAGE);
    }

    public void clickPaginationNextPage() {
        driverTools.waitForElementToAppear(PAGINATION_NEXT_PAGE);
        driverTools.waitForElementToBeClickable(PAGINATION_NEXT_PAGE);
        driverTools.click(PAGINATION_NEXT_PAGE);
    }

    public void clickPaginationMaxPage(String MAX_PAGE) {
        Locator PAGINATION_MAX_PAGE = new Locator("PAGINATION_MAX_PAGE", new By.ByXPath("//button[@aria-label='Page " + MAX_PAGE + "']"));
        driverTools.waitForElementToAppear(PAGINATION_MAX_PAGE, 3);
        driverTools.click(PAGINATION_MAX_PAGE);
    }

    public void clickPaginationMinPage(String MIN_PAGE) {
        Locator PAGINATION_MIN_PAGE = new Locator("PAGINATION_MIN_PAGE", new By.ByXPath("//button[@aria-label='Page " + MIN_PAGE + "']"));
        driverTools.waitForElementToAppear(PAGINATION_MIN_PAGE, 3);
        driverTools.click(PAGINATION_MIN_PAGE);
    }

    public void clickPaginationDropdown() {
        driverTools.waitForElementToAppear(PAGINATION_DROPDOWN, 3);
        driverTools.click(PAGINATION_DROPDOWN);
    }

    public void clickPaginationRowsPerPageX(String X) {
        clickPaginationDropdown();
        Locator PAGINATION_DROPDOWN_ROWS_PER_PAGE_X = new Locator("PAGINATION_DROPDOWN_ROWS_PER_PAGE_X", new By.ByXPath("//li[@id='dropdown-options-Pagination-" + X + "']"));
        driverTools.waitForElementToAppear(PAGINATION_DROPDOWN_ROWS_PER_PAGE_X, 3);
        driverTools.click(PAGINATION_DROPDOWN_ROWS_PER_PAGE_X);
    }

    public boolean isPaginationRowsPerPageX(String X) {
        Locator ROWS_SHOWING = new Locator("ROWS_SHOWING", new By.ByXPath("//span[starts-with(text(),'Showing 1 to " + X + " of')]"));
        driverTools.waitForElementToAppear(ROWS_SHOWING, 3);
        return driverTools.isElementDisplayed(ROWS_SHOWING);
    }

    public void clickNoDataFoundResetFilterButton() {
        driverTools.waitForElementToAppear(NO_DATA_FOUND_RESET_FILTER_BUTTON, 3);
        driverTools.click(NO_DATA_FOUND_RESET_FILTER_BUTTON);
    }

    public boolean isNoDataFound() {
        return driverTools.waitForElementToAppear(NO_DATA_FOUND_TEXT, 10).isPresent();
    }

    public boolean isTableExists(String tableName) {
        Locator TABLE_NAME = new Locator("TABLE_NAME", new By.ByXPath("//th[contains(text(),'" + tableName + "')]"));
        driverTools.waitForElementToAppear(TABLE_NAME, 3);
        return driverTools.isElementDisplayed(TABLE_NAME);
    }

    public boolean isTableSpanExists(String tableName) {
        Locator TABLE_NAME = new Locator("TABLE_NAME", new By.ByXPath("//span[contains(text(),'" + tableName + "')]"));
        driverTools.waitForElementToAppear(TABLE_NAME, 3);
        return driverTools.isElementDisplayed(TABLE_NAME);
    }

    public void clickActionMoreButton() {
        driverTools.waitForElementToAppear(ACTION_MORE_BUTTON, 6);
        driverTools.clickWithJavascriptExecutor(ACTION_MORE_BUTTON);
    }

    public boolean isActionActityLogButtonExists() {
        driverTools.waitForElementToAppear(ACTION_ACTIVITY_LOG_BUTTON, 3);
        return driverTools.isElementDisplayed(ACTION_ACTIVITY_LOG_BUTTON);
    }

    public void clickActionActivityLogButton() {
        driverTools.waitForElementToAppear(ACTION_ACTIVITY_LOG_BUTTON, 3);
        driverTools.click(ACTION_ACTIVITY_LOG_BUTTON);
    }

    public boolean isActionActivityLogTitleExists() {
        driverTools.waitForElementToAppear(ACTION_ACTIVITY_LOG_TITLE, 3);
        return driverTools.isElementDisplayed(ACTION_ACTIVITY_LOG_TITLE);
    }

    public boolean isActionPriceDropDetailButtonExists() {
        driverTools.waitForElementToAppear(ACTION_PRICE_DROP_DETAIL_BUTTON, 3);
        return driverTools.isElementDisplayed(ACTION_PRICE_DROP_DETAIL_BUTTON);
    }

    public void clickActionPriceDropDetailButton() {
        driverTools.waitForElementToAppear(ACTION_PRICE_DROP_DETAIL_BUTTON, 3);
        driverTools.click(ACTION_PRICE_DROP_DETAIL_BUTTON);
    }

    public boolean isActionPriceDropDetailTitleExists() {
        driverTools.waitForElementToAppear(ACTION_PRICE_DROP_DETAIL_TITLE, 3);
        return driverTools.isElementDisplayed(ACTION_PRICE_DROP_DETAIL_TITLE);
    }

    public void clickActionModalCloseButton() {
        driverTools.waitForElementToAppear(ACTION_MODAL_CLOSE_BUTTON, 3);
        driverTools.click(ACTION_MODAL_CLOSE_BUTTON);
    }

    public void LogOut() {
        driverTools.waitForElementToAppear(USER_PROFILE_BUTTON, 3);
        driverTools.click(USER_PROFILE_BUTTON);
        driverTools.waitForElementToAppear(USER_LOGOUT_BUTTON, 3);
        driverTools.click(USER_LOGOUT_BUTTON);
    }
}