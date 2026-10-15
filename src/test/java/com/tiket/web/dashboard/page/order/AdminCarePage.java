package com.tiket.web.dashboard.page.order;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;

public class AdminCarePage<T extends WebDriver> extends BasePage<T> {
    private static final Locator ADMIN_CARE_SKELETON = new Locator("ADMIN_CARE_SKELETON", new By.ByXPath("//*[contains(@class, 'animate-pulse')]"));
    private static final Locator ADMIN_CARE_LOGO = new Locator("ADMIN_CARE_LOGO", new By.ByXPath("//*[@data-testid='sidemenu']//div[contains(@class,'active-label')]//*[contains(text(),'Admin Care')]"));
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//*[@data-testid='username']"));
    private static final Locator TITLE_GENERAL_SEARCH = new Locator("TITLE_GENERAL_SEARCH", new By.ByXPath("//*[contains(text(),'General Search')]"));
    private static final Locator WRAP_SIDEBAR = new Locator("WRAP_SIDEBAR", new By.ByXPath("//*[@data-testid='sidemenu']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//*[@data-testid='filter-submit-btn']"));
    private static final Locator ORDERLIST_ID = new Locator("ORDERLIST_ID", new By.ByXPath("(//td[@data-testid='general-list']//a//span)[1]"));
    private static final Locator ORDERLIST_ID_LINK = new Locator("ORDERLIST_ID_LINK", new By.ByXPath("//a/span[@class]"));
    private static final Locator EMPTY_SEARCH_DATA = new Locator("EMPTY_SEARCH_DATA", new By.ByXPath("//table//*[contains(text(),'No data available')]"));
    private static final Locator DROPDOWN_MENU_SEARCH_BY = new Locator("DROPDOWN_MENU_SEARCH_BY", new By.ByXPath("//input[@data-testid='search-type-input']"));
    private static final Locator PHONE_LOCATOR = new Locator("PHONE_LOCATOR", new By.ByXPath("//*[text()='Phone Number']"));
    private static final Locator KEYWORD = new Locator("KEYWORD", new By.ByXPath("//input[@data-testid='search-input']"));
    private static final Locator CHECK_ELEMENT = new Locator("CHECK_ELEMENT", new By.ByXPath("//td[@data-testid='general-list']//a"));
    private static final Locator MENU_REFUND = new Locator("MENU_REFUND", new By.ByXPath("//span[contains(@class, 'menuLabel') and text()='Refund']"));
    private static final Locator BOOKING_CODE = new Locator("BOOKING_CODE_LOCATER", new By.ByXPath("//*[text()='Booking Code']"));
    private static final Locator ACCOUNT = new Locator("ACCOUN_LOCATOR", new By.ByXPath("//*[text()='Account']"));
    private static final Locator DROPDOWN_MENU_ORDER_STATUS = new Locator("DROPDOWN_MENU_ORDER_STATUS_LOCATOR", new By.ByXPath("//input[@data-testid='status-type-input']/ancestor::div[contains(@class,'input_inner_wrapper')][1]"));
    private static final Locator E_TICKET_ISSUED = new Locator("E_TICKET_ISSUED_LOCATOR", new By.ByXPath("//div[contains(@data-testid,'dropdown-check-container') and contains(@data-testid,'ETICKET_ISSUED')]"));
    private static final Locator PROCESSING_E_TICKET = new Locator("PROCESSING_E_TICKET_LOCATOR", new By.ByXPath("//div[contains(@data-testid,'dropdown-check-container') and contains(@data-testid,'PROCESSING_ETICKET')]"));
    private static final Locator SEE_MORE_FILTER_SECTION = new Locator("SEE_MORE_FILTER_SECTION_LOCATOR", new By.ByXPath("//*[@class='filter-show-btn']"));
    private static final Locator DROPDOWN_MENU_PRODUCT_TYPE = new Locator("PRODUCT_TYPE_LOCATOR", new By.ByXPath("//input[@data-testid='product-type-input']"));
    private static final Locator START_DATE_TRANSACTION_RANGE = new Locator("START_DATE_TRANSACTION_RANGE_LOCATOR", new By.ByXPath("//input[@id='date-transaction-start']"));
    private static final Locator END_DATE_TRANSACTION_RANGE = new Locator("END_DATE_TRANSACTION_RANGE_LOCATOR", new By.ByXPath("//*[@data-testid='datepicker-dropdown-Date_Transaction_End']/ancestor::div[contains(@class,'input_inner_wrapper')][1]"));
    private static final Locator START_DATE_SCHEDULE_RANGE = new Locator("START_DATE_SCHEDULE_RANGE_LOCATOR", new By.ByXPath("//input[@id='date-schedule-start']"));
    private static final Locator END_DATE_SCHEDULE_RANGE = new Locator("END_DATE_SCHEDULE_RANGE_LOCATOR", new By.ByXPath("//input[@id='date-schedule-end']"));
    private static final Locator DATEPICKER_CONTAINER = new Locator("DATEPICKER_CONTAINER",
            new By.ByXPath("//div[contains(@class,'datepickerTimepickerContainer')]"));
    private static final Locator MONTH_IN_CALENDAR = new Locator("MONTH_IN_CALENDAR",
            new By.ByXPath("//div[contains(@class,'datepickerTimepickerContainer')]//button[contains(@class,'_month_menu_button')]"));
    private static final Locator YEAR_IN_CALENDAR = new Locator("YEAR_IN_CALENDAR",
            new By.ByXPath("//div[contains(@class,'datepickerTimepickerContainer')]//button[contains(@class,'_year_menu_button')]"));
    private static final Locator MONTH_GRID = new Locator("MONTH_GRID",
            new By.ByXPath("//div[contains(@class,'datepickerTimepickerContainer')]//div[contains(@class,'month_grid')]"));
    private static final Locator YEAR_GRID = new Locator("YEAR_GRID",
            new By.ByXPath("//div[contains(@class,'datepickerTimepickerContainer')]//div[contains(@class,'year_grid')]"));
    private static final Locator CALANDER_BACK_BUTTON = new Locator("CALANDER_BACK_BUTTON_LOCATOR", new By.ByXPath("//button[contains(@class, '_prev_button')]"));
    private static final Locator SAVE_BUTTON_LOCATOR = new Locator("SAVE_BUTTON_LOCATOR",
            new By.ByXPath("//div[contains(@class,'datepickerTimepickerContainer')]//footer[contains(@class,'action_area')]//button[.//span[normalize-space()='Save']]"));
    private static final Locator CALANDER_BACK_BUTTON_SECHEDULE = new Locator("CALANDER_BACK_BUTTON_SCHEDULE_RANGE", new By.ByXPath("//button[contains(@class, '_prev_button')]"));
    private static final Locator ORDER_STATUS_VALUE = new Locator("ORDER_STATUS_VALUE_LOCATOR", new By.ByXPath("(//div[@data-testid='label-status']//span)[1]"));
    private static final Locator PRODUCT_TYPE_LOCATOR = new Locator("PRODUCT_TYPE_LOCATOR_IN_ORDER", new By.ByXPath("(//td[contains(@class, 'border-r')]//span[contains(@class, '_text_')])[2]"));
    private static final Locator DATE_LOCATOR = new Locator("DATE_LOCATOR_IN_ORDER", new By.ByXPath("(//table//tbody//tr[1]//td)[3]"));
    private static final Locator MORE_IN_ACTION_COLUMN_LOCATOR = new Locator( "MORE_IN_ACTION_COLUMN", new By.ByXPath("(//*[@data-testid='more-btn-single'])"));
    private static final Locator SEE_MORE_FILTER = new Locator("SEE_MORE_FILTER", new By.ByXPath("//span[contains(@class,'filter-show-btn')]"));
    private static final Locator FIRST_DAY_MONTH = new Locator("FIRST_DAY_MONTH", new By.ByXPath("//div[@class='day-mod-body'][text()='1']"));
    private static final Locator PREV_MONTH_TRANSACTION = new Locator("PREV_MONTH_TRANSACTION", new By.ByXPath("//div[contains(@class,'range-picker')]/span[contains(@class,'left')]"));
    private static final Locator DONE_SELECT_TRANSACTION_DATE = new Locator("DONE_SELECT_TRANSACTION_DATE", new By.ByXPath("//label[contains(@class,'submit-date-time')]"));
    private static final Locator LOADING_STATE = new Locator("LOADING_STATE", new By.ByXPath("//*[contains(@class,'loading_indicator_container')]"));
    private static final Locator PAYMENT_METHOD_DROPDOWN_LOCATOR = new Locator("PAYMENT_METHOD_DROPDOWN_LOCATOR", new By.ByXPath("//input[@data-testid='payment-type-input']"));
    private static final Locator CREDIT_CARD_LOCATOR = new Locator("CREDIT_CARD_LOCATOR", new By.ByXPath("(//*[text()='Credit Card - Visa'])"));
    private static final Locator PAYMENT_METHOD_OUTSIDE_LOCATOR = new Locator("PAYMENT_METHOD_OUTSIDE_LOCATOR", new By.ByXPath("//*[@class='payment-method'][1]"));
    private static final Locator COPY_IN_ACTION_COLUMN_LOCATOR = new Locator("COPY_IN_MORE_SECTION", new By.ByXPath("//div[@data-testid='copy-btn']"));
    private static final Locator MANUAL_ISSUED_LOCATOR = new Locator("MANUAL_ISSUED_LOCATOR", new By.ByXPath("//div[@data-testid='manual-issued-btn']"));
    private static final Locator CHOOSE_PAYMENT_DROPDOWN_LOCATOR = new Locator("CHOOSE_PAYMENT_DROPDOWN_LOCATOR", new By.ByXPath("//input[@data-testid='dropdown-payment-method']"));
    private static final Locator CC_LOCATOR = new Locator("CC_LOCATOR",
            new By.ByXPath("//*[@data-testid='dropdown-payment-method']/following::*[normalize-space()='Credit Card'][1]"));
    private static final Locator MESSAGE_BOX_LOCATOR = new Locator("MESSAGE_BOX_LOCATOR", new By.ByXPath("//textarea[@data-testid='manual-issued-message']"));
    private static final Locator OK_BUTTON_LOCATOR = new Locator("OK_BUTTON_LOCATOR", new By.ByXPath("//*[text()='OK']"));
    private static final Locator GREEN_SUCCESS_BAR = new Locator("GREEN_SUCCESS_BAR", new By.ByXPath("//div[contains(@class,'flash success')]"));
    private static final Locator STATUS_LOCATOR = new Locator("STATUS_LOCATOR", new By.ByXPath("(//*[contains(@class,'status')])[2]"));
    private static final Locator BACK_TO_ORDER_LIST_LOCATOR = new Locator("BACK_TO_ORDER_LIST_LOCATOR", new By.ByXPath("//div[@class='back-button']"));
    private static final Locator SELECT_MONTH_CALENDAR = new Locator("SELECT_MONTH_CALENDAR", By.xpath("//*[contains(@class,'month_menu')]"));
    private static final Locator SELECT_YEAR_CALENDAR = new Locator("SELECT_YEAR_CALENDAR", By.xpath("//*[contains(@class,'year_menu')]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//*[contains(text(),'Save')]"));

    public AdminCarePage(Session<T> session) {
        super(session);
    }

    private Locator getFilterByName(String filterName) {
        return new Locator("FILTER_SEARCH_BY_"+filterName.toUpperCase(), new By.ByXPath(String.format("//*[@data-testid='"+filterMap.get(filterName)+"']")));
    }

    private Locator getDropdownValue(String dropdown) {
        return new Locator("DROPDOWN_VALUE_"+dropdown.toUpperCase(), new By.ByXPath(String.format("//*[contains(@data-testid,'dropdown-option')]//*[contains(text(),'"+dropdown+"')]")));
    }

    private static final Locator dateLocator(String date) {
        return new Locator("DYNAMIC_START_DATE_LOCATOR",
                new By.ByXPath("//div[contains(@class,'datepickerTimepickerContainer')]"
                        + "//button[contains(@class,'Day_day')][.//span[normalize-space()='" + date + "']]"));
    }

    private static Locator dateLocatorByAriaLabel(String day, String indonesianMonth, String year) {
        return new Locator("DATE_BY_ARIA_" + day + "_" + indonesianMonth + "_" + year,
                By.xpath("//div[contains(@class,'datepickerTimepickerContainer')]"
                        + "//button[contains(@class,'Day_day')]"
                        + "[.//span[contains(@aria-label,'" + day + " " + indonesianMonth + " " + year + "')]]"));
    }

    private static final Locator dateLocatorScheduleStartLeft(String date) {
        return new Locator("DYNAMIC_START_DATE_LEFT_LOCATOR", new By.ByXPath("(//*[@class='pika-button pika-day' and text()='" + date + "'])[1]"));
    }

    private static final Locator dateLocatorScheduleStartRight(String date) {
        return new Locator("DYNAMIC_START_DATE_RIGHT_LOCATOR", new By.ByXPath("(//*[@class='pika-button pika-day' and text()='" + date + "'])[2]"));
    }

    private static final Locator dateLocatorScheduleEndLeft(String date) {
        return new Locator("DYNAMIC_END_DATE_LEFT_LOCATOR", new By.ByXPath("(//*[@class='pika-button pika-day' and text()='" + date + "'])[3]"));
    }

    private static final Locator dateLocatorScheduleEndRight(String date) {
        return new Locator("DYNAMIC_END_DATE_RIGHT_LOCATOR", new By.ByXPath("(//*[@class='pika-button pika-day' and text()='" + date + "'])[4]"));
    }

    private static final Locator getProductTypeValue(String productType) {
        return new Locator("VALUE_PRODUCT_TYPE_LOCATOR",
                new By.ByXPath("//div[contains(@data-testid, 'dropdown-check-container--value-dropdown-option') and contains(@data-testid, '" + productType.toLowerCase() + "')]"));
    }

    private static final Locator getSearchByDropDown(String SearchType) {
        return new Locator("VALUE_SEARCH_BY_LOCATOR", new By.ByXPath(
                "//div[contains(@data-testid,'dropdown-check-container')][.//span[normalize-space()='" + SearchType + "']]"));
    }

    private static final Locator getMoreDropdown(String item) {
        return new Locator("DETAILS/COPY_IN_MORE_SECTION", By.xpath("//*[@data-testid='"+moreActionMap.get(item)+"']"));
    }

    private static final Locator getValueByColumnName(String columnName) {
        return new Locator("VALUE_BY_COLUMN_NAME_"+columnName, By.xpath("//td[position() = count(//p[contains(text(),'"+columnName+"')]/ancestor::th/preceding-sibling::th)+1]"));
    }

    private static final Locator getDateInCalendar(String date) {
        return new Locator("DATE_IN_CALENDAR_"+date, By.xpath("//*[contains(@class,'month')]//*[text()='"+date+"']"));
    }

    private static final Locator getMonthInCalendar(String month) {
        return new Locator("MONTH_IN_CALENDAR_" + month,
                By.xpath("//div[contains(@class,'datepickerTimepickerContainer')]"
                        + "//div[contains(@class,'month_grid')]//button[normalize-space()='" + month + "']"));
    }

    private static final Locator getYearInCalendar(String year) {
        return new Locator("YEAR_IN_CALENDAR_" + year,
                By.xpath("//div[contains(@class,'datepickerTimepickerContainer')]"
                        + "//div[contains(@class,'year_grid')]//button[normalize-space()='" + year + "']"));
    }

    Map<String, String> filterMap = Map.ofEntries(
        new AbstractMap.SimpleEntry<>("Search By", "search-type-input"),
        new AbstractMap.SimpleEntry<>("Search By Input", "search-input"),
        new AbstractMap.SimpleEntry<>("Order Detail Id", "order-detail-id"),
        new AbstractMap.SimpleEntry<>("Product Type", "product-type-input"),
        new AbstractMap.SimpleEntry<>("Date Transaction Start", "datepicker-dropdown-Date_Transaction_Start"),
        new AbstractMap.SimpleEntry<>("Date Transaction End", "datepicker-dropdown-Date_Transaction_End"),
        new AbstractMap.SimpleEntry<>("Date Schedule Start", "datepicker-dropdown-Date_Schedule_Start"),
        new AbstractMap.SimpleEntry<>("Date Schedule End", "datepicker-dropdown-Date_Schedule_End"),
        new AbstractMap.SimpleEntry<>("Order Status", "status-type-input"),
        new AbstractMap.SimpleEntry<>("Payment Method", "payment-type-input")
    );

    public static final Map<String, String> moreActionMap = Map.of(
            "Details", "detail-link",
            "Copy", "copy-btn",
            "Manual Issued", "manual-issued-btn"
    );

    public void goToAdminCarePage(String username, String password) {
        driverTools.maximizeWindow();
        new MemberLoginPage<>(session).adminLoginViaCookie(username, password);
        String adminCarePath = "/admin/care/v2";
        Deeplink deeplink = new Deeplink("ADMIN_CARE", "https://gatotkaca.tiket.com", "https://preprod.tiket.com", "https://tiket.com");
        String url = switch (Configs.ENVIRONMENT) {
            case PROD -> deeplink.prodDeeplink()+adminCarePath;
            case PREPROD -> deeplink.preprodDeeplink()+adminCarePath;
            case GK -> deeplink.gkDeeplink()+adminCarePath;
            default -> deeplink.gkDeeplink()+adminCarePath;
        };
        try {
            driverTools.get(url);
        } catch (UnhandledAlertException e) {
            driverTools.acceptBrowserAlert();
        } finally {
            int maxRetries = 3;
            int attempt = 0;
            while (!isAdminCareLoggedIn() && attempt < maxRetries) {
                attempt++;
                logger.log("Admin Care page not ready (attempt " + attempt + "/" + maxRetries + "). Hard refreshing page (bypass cache).");
                try {
                    driverTools.hardRefreshWebpage();
                } catch (UnhandledAlertException e) {
                    driverTools.acceptBrowserAlert();
                }
                driverTools.waitForElementToDisappear(ADMIN_CARE_SKELETON, 15);
            }
            if (!isAdminCareLoggedIn()) {
                AdminCareLoginPage<?> adminCareLoginPage = new AdminCareLoginPage<>(session);
                adminCareLoginPage.loginToAdminCare(username, password);
            }
        }
    }

    public void goToAdminCarePageViaUI(String username, String password) {
        driverTools.maximizeWindow();
        String adminCarePath = "/admin/care/v2";
        Deeplink deeplink = new Deeplink("ADMIN_CARE", "https://gatotkaca.tiket.com", "https://preprod.tiket.com", "https://tiket.com");
        String url = switch (Configs.ENVIRONMENT) {
            case PROD -> deeplink.prodDeeplink()+adminCarePath;
            case PREPROD -> deeplink.preprodDeeplink()+adminCarePath;
            case GK -> deeplink.gkDeeplink()+adminCarePath;
            default -> deeplink.gkDeeplink()+adminCarePath;
        };
        try {
            driverTools.get(url);
        } catch (UnhandledAlertException e) {
            driverTools.acceptBrowserAlert();
        } finally {
            if (!isAdminCareLoggedIn()) {
                AdminCareLoginPage<?> adminCareLoginPage = new AdminCareLoginPage<>(session);
                adminCareLoginPage.loginToAdminCare(username, password);
            }
        }
    }

    public boolean isAdminCareLoggedIn() {
        driverTools.waitForElementToDisappear(ADMIN_CARE_SKELETON);
        Optional<WebElement> admincareLogo = driverTools.waitForElementToAppear(ADMIN_CARE_LOGO, 3);
        Optional<WebElement> greeting = driverTools.waitForElementToAppear(USERNAME_GREETING, 3);
        Optional<WebElement> generalSearch = driverTools.waitForElementToAppear(TITLE_GENERAL_SEARCH, 3);
        return (admincareLogo.isPresent() || greeting.isPresent() || generalSearch.isPresent());
    }

    public void selectFilterByName(String filterName) {
        Locator filterLocator = getFilterByName(filterName);
        driverTools.waitForElementToAppear(filterLocator, 3);
        driverTools.click(filterLocator);
    }

    public void selectDropdownMenu(String dropdownValue) {
        driverTools.scrollElementToCenter(getDropdownValue(dropdownValue));
        driverTools.click(getDropdownValue(dropdownValue));
        driverTools.scrollIntoWebView(WRAP_SIDEBAR);
    }

    public void inputKeyword(String input) {
        driverTools.click(getFilterByName("Search By Input"));
        driverTools.sendKeys(getFilterByName("Search By Input"), input);
    }

    public void clickSearch() {
        WebElement searchButton = driverTools.waitForElementToAppear(SEARCH_BUTTON).orElseThrow();
        driverTools.click(searchButton);
    }

    public void clickSave() {
        WebElement saveButton = driverTools.waitForElementToPresence(SAVE_BUTTON, 3).orElseThrow();
        driverTools.click(saveButton);
    }

    public void clickDropdownInSearchBy() {
        int retry = 3;
        for (int i = 0; i < retry; i++) {
            driverTools.waitForElementToDisappear(ADMIN_CARE_SKELETON);
            driverTools.waitForElementToAppear(LOADING_STATE).ifPresentOrElse(driverTools::waitForElementToDisappear,
                    () -> logger.log("Loading state not appeared"));
            Optional<WebElement> dropdownInSearchBy = driverTools.waitForElementToAppear(DROPDOWN_MENU_SEARCH_BY, 30);
            if (dropdownInSearchBy.isPresent()) {
                driverTools.click(dropdownInSearchBy.get());
                return;
            }
            logger.log("Search by dropdown not loaded, refreshing page. Attempt: " + (i + 1));
            driverTools.refreshWebpage();
        }
        throw new NoSuchElementException(DROPDOWN_MENU_SEARCH_BY + " not present after " + retry + " attempts");
    }

    public void clickPhonenumber() {
        WebElement phonenumber = driverTools.waitForElementToAppear(PHONE_LOCATOR,3).orElseThrow();
        driverTools.click(phonenumber);
    }

    public void input(String value) {
        WebElement inputArea = driverTools.waitForElementToAppear(KEYWORD,3).orElseThrow();
        driverTools.scrollIntoWebView(inputArea);
        driverTools.clickWithJavascriptExecutor(inputArea);
        driverTools.sendKeys(inputArea, value);
    }

    public boolean checkElement() {
        driverTools.waitForElementToAppear(CHECK_ELEMENT);
        return driverTools.isElementDisplayed(CHECK_ELEMENT);
    }

    public void maximize() {
        driverTools.maximizeWindow();
    }

    public void clickOrderId() {
        WebElement orderId = driverTools.waitForElementToAppear(CHECK_ELEMENT).orElseThrow();
        driverTools.click(orderId);
    }

    public boolean verifyOrderStatus(String status) {
        WebElement orderStatus = driverTools.waitForElementToAppear(ORDER_STATUS_VALUE,3).orElseThrow();
        return driverTools.getText(orderStatus).contains(status);
    }

    public boolean verifyProductType(String product) {
        WebElement productType = driverTools.waitForElementToAppear(PRODUCT_TYPE_LOCATOR,3).orElseThrow();
        return driverTools.getText(productType).contains(product);
    }

    public String verifyDate() {
        WebElement dates = driverTools.waitForElementToAppear(DATE_LOCATOR,3).orElseThrow();
        return driverTools.getText(dates);
    }

    public void clickProductType() {
        WebElement productType = driverTools.waitForElementToAppear(PHONE_LOCATOR,3).orElseThrow();
        driverTools.click(productType);
    }

    public void goToRefundPage(){
        WebElement menuRefund = driverTools.waitForElementToAppear(MENU_REFUND).orElseThrow();
        driverTools.click(menuRefund);
    }

    public void clickAccount() {
        driverTools.click(ACCOUNT);
    }

    public void clickDropdownInOrderStatus() {
        driverTools.scrollIntoWebView(WRAP_SIDEBAR);
        driverTools.click(DROPDOWN_MENU_ORDER_STATUS);
    }

    public void clickETicketIssued() {
        driverTools.click(E_TICKET_ISSUED);
        clickSave();
    }

    public void clickProcessingETicket() {
        driverTools.click(PROCESSING_E_TICKET);
        driverTools.click(SAVE_BUTTON);
    }

    public void clickSeeMoreFilterSection() {
        driverTools.click(SEE_MORE_FILTER_SECTION);
    }

    public void clickBookingCode() {
        driverTools.click(BOOKING_CODE);
    }

    public void clickDropdownInProductType() {
        WebElement dropdownInProductType = driverTools.waitForElementToAppear(DROPDOWN_MENU_PRODUCT_TYPE,3).orElseThrow();
        driverTools.click(dropdownInProductType);
    }

    public void clickInProductType(String product) {
        driverTools.click(getProductTypeValue(product));
        driverTools.click(SAVE_BUTTON);
    }

    public void selectInSearchBy(String searchType) {
        driverTools.click(getSearchByDropDown(searchType));
    }

    public void clickStartDateTransactionRange() {
        WebElement startDateTransactionRange = driverTools.waitForElementToAppear(START_DATE_TRANSACTION_RANGE,3).orElseThrow();
        driverTools.click(startDateTransactionRange);
    }

    public void clickEndDateTransactionRange() {
        WebElement endDateTransactionRange = driverTools.waitForElementToAppear(END_DATE_TRANSACTION_RANGE,3).orElseThrow();
        driverTools.click(endDateTransactionRange);
    }

    public void selectDate(String monthAndYear, String date) throws InterruptedException {
        String[] parts = monthAndYear.trim().split("\\s+");
        String month = parts[0];
        String year = parts.length > 1 ? parts[1] : "";
        selectDateUsingMonthYearMenus(month, year, date);
    }

    public void clickStartDateScheduleRange() {
        WebElement startDateScheduleRange = driverTools.waitForElementToAppear(START_DATE_SCHEDULE_RANGE,3).orElseThrow();
        driverTools.click(startDateScheduleRange);
    }

    public void clickEndDateScheduleRange() {
        WebElement endDateScheduleRange = driverTools.waitForElementToAppear(END_DATE_SCHEDULE_RANGE,3).orElseThrow();
        driverTools.click(endDateScheduleRange);
    }

    public void selectDateScheduleRange(String month, String year, String date) throws InterruptedException {
        String monthToken = month.trim().split("\\s+")[0];
        selectDateUsingMonthYearMenus(monthToken, year, date);
    }

    private void selectDateUsingMonthYearMenus(String month, String year, String date) {
        String monthToken = month.trim().split("\\s+")[0];
        String indonesianMonth = CoreFunctions.getMonthName(CoreFunctions.getMonthNumber(monthToken), "id");

        driverTools.waitForElementToAppear(DATEPICKER_CONTAINER, 5).orElseThrow();
        driverTools.waitForElementToAppear(YEAR_IN_CALENDAR, 5).orElseThrow();
        driverTools.click(YEAR_IN_CALENDAR);
        driverTools.waitForElementToAppear(YEAR_GRID, 5).orElseThrow();
        driverTools.click(getYearInCalendar(year));
        driverTools.waitForElementToDisappear(YEAR_GRID, 5);

        driverTools.waitForElementToAppear(MONTH_IN_CALENDAR, 5).orElseThrow();
        driverTools.click(MONTH_IN_CALENDAR);
        driverTools.waitForElementToAppear(MONTH_GRID, 5).orElseThrow();
        driverTools.click(getMonthInCalendar(indonesianMonth));
        driverTools.waitForElementToDisappear(MONTH_GRID, 5);

        Locator dayLocator = dateLocatorByAriaLabel(date, indonesianMonth, year);
        if (driverTools.waitForElementToAppear(dayLocator, 3).isEmpty()) {
            dayLocator = dateLocator(date);
            driverTools.waitForElementToAppear(dayLocator, 3).orElseThrow();
        }
        driverTools.click(dayLocator);

        WebElement saveButton = driverTools.waitForElementToAppear(SAVE_BUTTON_LOCATOR, 5).orElseThrow();
        driverTools.click(saveButton);
        driverTools.waitForElementToDisappear(DATEPICKER_CONTAINER, 5);
    }

    public void clickMoreInActionColumn() {
        WebElement moreInActionColumn = driverTools.waitForElementToAppear(MORE_IN_ACTION_COLUMN_LOCATOR,3).orElseThrow();
        driverTools.click(moreInActionColumn);
    }

    public void selectInMoreInActionColumn(String item) {
        WebElement inMoreInActionColumn = driverTools.waitForElementToAppear(getMoreDropdown(item),3).orElseThrow();
        driverTools.click(inMoreInActionColumn);
        driverTools.waitForElementToAppear(LOADING_STATE).ifPresentOrElse(driverTools::waitForElementToDisappear, () -> {logger.log("Loading state not appeared");});
    }

    public String getOrderListId(int index) {
        driverTools.waitForElementToAppear(ORDERLIST_ID);
        List<WebElement> orderListId = driverTools.elements(ORDERLIST_ID).orElseThrow();
        String[] orderIds = driverTools.getText(orderListId.get(index)).split(" - ");
        return orderIds[0];
    }

    public String getTableValueByColumnName(String columnName) {
        WebElement value = driverTools.waitForElementToPresence(getValueByColumnName(columnName), 3).orElseThrow();
        return driverTools.getText(value);
    }

    public void selectOrderListId(int index) {
        WebElement orderList = driverTools.waitForElementToAppear(ORDERLIST_ID_LINK, 2).orElseThrow();
        List<WebElement> orderListIdLink = driverTools.elements(ORDERLIST_ID_LINK).orElseThrow();
        driverTools.click(orderListIdLink.get(index));
        driverTools.waitForElementToDisappear(LOADING_STATE);
    }

    public void clickSeeMoreFilter() {
        WebElement seeMoreFilter = driverTools.waitForElementToAppear(SEE_MORE_FILTER).orElseThrow();
        driverTools.click(seeMoreFilter);
    }

    public boolean isEmptySearchResult() {
        Optional<WebElement> emptySearchResult = driverTools.waitForElementToAppear(EMPTY_SEARCH_DATA,3);
        return emptySearchResult.isPresent();
    }

    public void setFirstDayMonthTransaction(int backMonth) {
        for (int i = 0; i < backMonth; i++) {
            WebElement prevMonthTransaction = driverTools.waitForElementToAppear(PREV_MONTH_TRANSACTION,3).orElseThrow();
            driverTools.click(prevMonthTransaction);
        }
        WebElement firstDayMonth = driverTools.waitForElementToAppear(FIRST_DAY_MONTH,3).orElseThrow();
        driverTools.click(firstDayMonth);
        driverTools.click(DONE_SELECT_TRANSACTION_DATE);
    }

    public void searchOrderId(String orderId, int retry) {
        for(int i = 0; i < retry; i++) {
            driverTools.waitForElementToDisappear(LOADING_STATE);
            selectFilterByName("Search By");
            selectDropdownMenu("Order ID");
            inputKeyword(orderId);
            clickSearch();
            Optional<WebElement> orderList = driverTools.waitForElementToAppear(getValueByColumnName("Order ID - Order Detail ID"), 3);
            if (orderList.isPresent()) {
                String orderStatus = driverTools.getText(getValueByColumnName("Order Status")).get();
                logger.log(orderId + " status on admin care is " + orderStatus);
                break;
            }
            logger.log(orderId+" not shown on admin care");
            if (i < retry - 1) {
                driverTools.refreshWebpage();
            }
        }
    }

    private static final Locator getPaymentMethodOption(String paymentMethod) {
        return new Locator("PAYMENT_METHOD_OPTION_" + paymentMethod,
                By.xpath("//*[normalize-space()='" + paymentMethod + "']"));
    }

    public void clickPaymentMetodDropdown() throws InterruptedException {
        WebElement dropdown = driverTools.waitForElementToAppear(PAYMENT_METHOD_DROPDOWN_LOCATOR,3).orElseThrow();
        driverTools.click(dropdown);
    }

    public void selectCreditCardInPaymentMetodDropdown() throws InterruptedException {
        selectPaymentMethodInDropdown("Credit Card - Visa");
    }

    public void selectPaymentMethodInDropdown(String paymentMethod) throws InterruptedException {
        WebElement option = driverTools.waitForElementToAppear(getPaymentMethodOption(paymentMethod), 3).orElseThrow();
        try {
            driverTools.click(option);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(option);
        }
    }

    public boolean checkPaymentMetohd(String method) {
        WebElement paymentMethod = driverTools.waitForElementToAppear(PAYMENT_METHOD_OUTSIDE_LOCATOR, 3).orElseThrow();
        return driverTools.getText(paymentMethod).contains(method);
    }

    public String verifyCopiedData() throws InterruptedException {
        WebElement inputField = driverTools.waitForElementToAppear(KEYWORD, 3).orElseThrow();
        inputField.clear();
        if (CoreFunctions.isJenkinsSystem()) {
            inputField.sendKeys(Keys.chord(Keys.CONTROL, "v"));
        } else {
            inputField.sendKeys(Keys.chord(Keys.COMMAND, "v"));
        }
        String inputValue = inputField.getAttribute("value");
        return inputValue;
    }

    public void clickCopyOnOrderCard() throws InterruptedException {
        WebElement copy = driverTools.waitForElementToAppear(COPY_IN_ACTION_COLUMN_LOCATOR,3).orElseThrow();
        driverTools.click(copy);
    }

    public void selectManualIssued() throws InterruptedException {
        WebElement manualIssued = driverTools.waitForElementToAppear(MANUAL_ISSUED_LOCATOR,3).orElseThrow();
        driverTools.click(manualIssued);
    }

    public void clickChoosePaymentDropdown() throws InterruptedException {
        WebElement dropdown = driverTools.waitForElementToAppear(CHOOSE_PAYMENT_DROPDOWN_LOCATOR,3).orElseThrow();
        driverTools.click(dropdown);
    }

    public void chooseCC() throws InterruptedException {
        WebElement method = driverTools.waitForElementToAppear(CC_LOCATOR, 5).orElseThrow();
        driverTools.scrollIntoWebView(method);
        try {
            driverTools.click(method);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(method);
        }
    }

    public void clickAndSendInMessageBox(String input) throws InterruptedException {
        WebElement box = driverTools.waitForElementToAppear(MESSAGE_BOX_LOCATOR,3).orElseThrow();
        driverTools.click(box);
        driverTools.sendKeys(MESSAGE_BOX_LOCATOR, input);
    }

    public void clickOk() throws InterruptedException {
        WebElement okButton = driverTools.waitForElementToAppear(OK_BUTTON_LOCATOR,3).orElseThrow();
        driverTools.click(okButton);
    }

    public void selectDetailsInActionColumn(String item) throws InterruptedException {
        WebElement details = driverTools.waitForElementToAppear(getMoreDropdown(item),3).orElseThrow();
        driverTools.click(details);
    }

    public void clickSearchForManual() throws InterruptedException {
        WebElement searchButton = driverTools.waitForElementToAppear(SEARCH_BUTTON).orElseThrow();
        driverTools.click(searchButton);
        driverTools.waitForElementToAppear(LOADING_STATE).ifPresentOrElse(driverTools::waitForElementToDisappear,
                ()-> {throw new NoSuchElementException("Loading state not appeared");}
        );
    }

    public void goBackToOrderList() {
        WebElement back = driverTools.waitForElementToAppear(BACK_TO_ORDER_LIST_LOCATOR,3).orElseThrow();
        driverTools.click(back);
    }

    public void letMessageDisappear() throws InterruptedException {
        driverTools.waitForElementToDisappear(GREEN_SUCCESS_BAR, 10);
        driverTools.refreshWebpage();
    }

    public void checkWaitingForPayment(String item){
        WebElement status = driverTools.waitForElementToAppear(ORDER_STATUS_VALUE).orElseThrow();
        if(!status.getText().equals("Waiting for payment")){
            clickMoreInActionColumn();
            selectInMoreInActionColumn(item);
            goBackToOrderList();
        }
    }

    public void selectDateCalendar(String date, String month, String year) {
        driverTools.click(SELECT_YEAR_CALENDAR);
        driverTools.click(getYearInCalendar(year));
        driverTools.click(SELECT_MONTH_CALENDAR);
        driverTools.click(getMonthInCalendar(month));
        driverTools.click(getDateInCalendar(date));
        driverTools.click(SAVE_BUTTON);
    }
}