package com.tiket.web.dashboard.page.refund;

import com.tiket.core.CoreFunctions;
import com.tiket.dbclient.TiketMysqlDBManager;
import com.tiket.dto.RefundRequestDTO;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.web.dashboard.page.order.AdminCarePage;
import nl.jiankai.mapper.ResultSetMapperFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.containsString;

public class RefundListPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator REFUND_LEFT_HEADER = new Locator("REFUND_LEFT_HEADER", new By.ByXPath("//header/a/span[contains(text(),'Dashboard Refund')]"));
    private static final Locator MENU_REFUND_LIST = new Locator("MENU_REFUND_LIST", new By.ByXPath("//*/span[contains(text(),'Refund List')]"));
    private static final Locator TITLE_REFUND_LIST = new Locator("TITLE_REFUND_LIST", new By.ByXPath("//h1[contains(text(),'Refund List')]"));
    private static final Locator NODATA_REFUND_LIST = new Locator("NODATA_REFUND_LIST", new By.ByXPath("//img[contains(@alt, 'empty refund list image')]"));
    private static final Locator LOADING_STATE = new Locator("REFUND_LIST_LOADING_SKELETON", new By.ByXPath("//*/td[contains(@class, 'animate-pulse')]"));
    private static final Locator LOADING_OVERLAY = new Locator("LOADING_OVERLAY", new By.ByXPath("//div[contains(@class,'w-screen') and contains(@class,'h-screen') and contains(@class,'bg-slate-800/35')]"));
    private static final Locator PAGE_LOADING_SKELETON = new Locator("PAGE_LOADING_SKELETON", new By.ByXPath("//*[contains(@class,'animate-pulse')]"));
    private static final Locator DROPDOWN_MENU_SEARCHBY = new Locator("DROPDOWN_MENU_SEARCHBY", new By.ByXPath("//div[@class='dropdown-menu']"));
    private static final Locator SEARCHBY_ORDER_ID = new Locator("SEARCHBY_ORDER_ID", new By.ByXPath("//input[@data-testid='filter-input-orderId-field']"));
    private static final Locator SEARCHBY_REFUND_ID = new Locator("SEARCHBY_REFUND_ID", new By.ByXPath("//input[@data-testid='filter-input-refundRequestId-field']"));
    private static final Locator SEARCH_BUTTON =  new Locator("SEARCH_BUTTON", new By.ByXPath("//*[@data-testid='filter-submit-btn']"));
    private static final Locator INPUT_SEARCHBY = new Locator("INPUT_SEARCHBY", new By.ByXPath("//input[@name='searchByValue']"));
    private static final Locator ORDER_PAIR = new Locator("ORDER_PAIR", new By.ByXPath("//*//tbody/tr/td[2]"));
    private static final Locator REQUEST_ID = new Locator("REQUEST_ID", new By.ByXPath("//tbody/tr/td[1]/p[contains(@class, 'cursor-pointer')][@role='button']"));
    private static final Locator REQUEST_DATE = new Locator("REQUEST_DATE", new By.ByXPath("//div[@class='date-time-custom-input']"));
    private static final Locator CS_REQ_DATE_FROM = new Locator("CS_REQ_DATE_FROM", new By.ByXPath("//input[@data-testid='field-startRequestedAt']"));
    private static final Locator CS_REQ_DATE_TO = new Locator("CS_REQ_DATE_TO", new By.ByXPath("//input[@data-testid='field-endRequestedAt']"));
    private static final Locator CALENDAR_POPUP = new Locator("CALENDAR_POPUP", new By.ByXPath("//div[contains(@class, 'calendar')]"));
    private static final Locator CALENDAR_PREV_MONTH = new Locator("CALENDAR_PREV_MONTH", new By.ByXPath("//button[contains(@class, 'prev_button')]"));
    private static final Locator CALENDAR_NEXT_MONTH = new Locator("CALENDAR_NEXT_MONTH", new By.ByXPath("//button[contains(@class, 'next_button')]"));
    private static final Locator DATE_MONTH_YEAR = new Locator("DATE_MONTH_YEAR", new By.ByXPath("//div[@class='DayPicker-Caption']"));
    private static final Locator DATE_PREV_MONTH = new Locator("DATE_PREV_MONTH", new By.ByXPath("//span[contains(@class,'nav left')]"));
    private static final Locator DATE_NEXT_MONTH = new Locator("DATE_NEXT_MONTH", new By.ByXPath("//span[contains(@class,'nav right')]"));
    private static final Locator DATE_DONE = new Locator("REQUEST_DATE_DONE", new By.ByXPath("//label[@class='submit-date-time']"));
    private static final Locator DATE_TODAY = new Locator("REQUEST_DATE_TODAY", new By.ByXPath("//div[@class='day-mod-head'][text()='Today']"));
    private static final Locator DATE_FILTERED = new Locator("DATE_FILTERED", new By.ByXPath("//div[@class='date-time-custom-input']//input"));
    private static final Locator SEE_REFUND_DETAIL = new Locator("SEE_REFUND_DETAIL", new By.ByXPath("//p[@aria-roledescription='See Details']"));
    private static final Locator CREATED_DATE_IN_REFUND_DETAILS = new Locator("INFO_DATE_REFUND_STATUS", new By.ByXPath("//*/p[normalize-space()='Created']/following-sibling::p"));
    private static final Locator INFO_TITLE_REFUND_STATUS = new Locator("INFO_TITLE_REFUND_STATUS", new By.ByXPath("//li[@class='enable']//div[contains(@class,'info-title')]"));
    private static final Locator REFUND_AMOUNT_SECTION = new Locator("REFUND_AMOUNT_SECTION", new By.ByXPath("//*[@data-testid='refund-detail-estimation-section']"));
    private static final Locator INFO_TITLE_STATUS_REJECTED = new Locator("INFO_TITLE_STATUS_REJECTED", new By.ByXPath("//li[@class='rejected']//div[contains(@class,'info-title')]"));
    private static final Locator TOGGLE_CLOSE_REFUND_DETAIL = new Locator("TOGGLE_CLOSE_REFUND_DETAIL", new By.ByXPath("//*[@data-testid='modal-close-button']"));
    private static final Locator PAGINATION_NEXT = new Locator("PAGINATION_NEXT", new By.ByXPath("//li[@class='page letters']//a[text()='Next']"));
    private static final Locator APPLY_BUTTON = new Locator("APPLY_BUTTON", new By.ByXPath("//button[.//span[normalize-space()='Apply']]"));
    private static final Locator VERTICAL_DETAIL = new Locator("VERTICAL_DETAIL", new By.ByXPath("//div[@data-testid='vertical-wrapper']"));
    private static final Locator FILTER_BUTTON = new Locator("FILTER_BUTTON", new By.ByXPath("//button[.//span[contains(normalize-space(),'Filter(s)')]]"));
    private static final Locator FILTER_DEPART_START = new Locator("FILTER_REFUND_STATUS", new By.ByXPath("//input[@id='from']"));
    private static final Locator FILTER_DEPART_END = new Locator("FILTER_REFUND_STATUS", new By.ByXPath("//input[@id='to']"));
    private static final Locator FILTER_REFUND_STATUS = new Locator("FILTER_REFUND_STATUS", new By.ByXPath("//*[@data-testid='filter-input-status-field']"));
    private static final Locator FILTER_PROGRAM_NAME = new Locator("FILTER_PROGRAM_NAME", new By.ByXPath("//*[@data-testid='filter-input-campaignProductName-field']"));
    private static final Locator REFUND_STATUS = new Locator("REFUND_STATUS", new By.ByXPath("//div[@class='refund-list-table']//tbody//td/a[@class='link-button']/parent::td"));
    private static final Locator DETAIL_PROGRAM_NAME = new Locator("DETAIL_PROGRAM_NAME", new By.ByXPath("//p[b[normalize-space()='Insurance:']]"));
    private static final Locator DETAIL_POLICY_NUMBER = new Locator("DETAIL_POLICY_NUMBER", new By.ByXPath("//p[b[normalize-space()='Policy Number:']]"));
    private static final Locator REFUND_DETAIL_HEADER = new Locator("REFUND_DETAIL_HEADER", new By.ByXPath("//div[@class='refund-detail-header']/strong"));
    private static final Locator VERTICAL_DETAILS = new Locator("VERTICAL_DETAILS", new By.ByXPath("//div[@class='vertical-detail-wrapper']/div"));
    private static final Locator REFUND_TYPE = new Locator("REFUND_TYPE", new By.ByXPath("//td[position() = count(//th[div/span[text()='Type']]/preceding-sibling::th) + 1]"));
    private static final Locator REFUND_ESTIMATION_TOTAL = new Locator("REFUND_ESTIMATION_TOTAL", new By.ByXPath("//p[contains(text(), 'Total Estimation Refund')]/following-sibling::div//p[text()]"));
    private static final Locator REFUND_AMOUNT_VENDOR = new Locator("REFUND_AMOUNT_VENDOR", new By.ByXPath("//input[@name='amountFromVendor']/preceding-sibling::div//span[text()]"));
    private static final Locator REFUND_AMOUNT_ACTUAL = new Locator("REFUND_AMOUNT_ACTUAL", new By.ByXPath("//input[@name='actualRefundAmount']/preceding-sibling::div//span[text()]"));
    private static final Locator SEE_DETAILS_IN_REFUND_DETAIL = new Locator("SEE_DETAILS_IN_REFUND_DETAIL", new By.ByXPath("//p[contains(text(), 'See Details')]"));
    private static final Locator EXPORT_CSV = new Locator("EXPORT_CSV", new By.ByXPath("//button[contains(text(),'Export To CSV')]"));
    private static final Locator REFUND_DETAIL_DESCRIPTION = new Locator("REFUND_DETAIL_DESCRIPTION", new By.ByXPath("//div[contains(@class,'refund-detail-info')]//td[contains(text(),'Description')]/following-sibling::td[2]"));
    private static final Locator REFUND_DETAIL_REJECT_REASON = new Locator("REFUND_DETAIL_REJECT_REASON", new By.ByXPath("//p[b[contains(normalize-space(),'Rejected Reason')]]"));
    private static final Locator DETAIL_REFUND_METHOD = new Locator("DETAIL_REFUND_METHOD", new By.ByXPath("//div[@class='refund-method-form']"));
    private static final Locator DETAIL_REFUND_METHODS = new Locator("DETAIL_REFUND_METHODS", By.xpath("//*[contains(@class,'refund-payment-method')]//tr[1]"));
    private static final Locator ESTIMATION_SEE_DETAILS = new Locator("ESTIMATION_SEE_DETAILS", new By.ByXPath("//div[@class='refund-amount-wrapper']//div[@class='card-collapsable']"));
    private static final Locator REFUND_DETAIL_BLIBLIPAY_PN = new Locator("REFUND_DETAIL_BLIBLIPAY_PN", new By.ByXPath("//div[@class='refund-method-form']//td[contains(text(),'Phone')]/following-sibling::td[2]"));
    private static final Locator REJECT_REASON_DROPDOWN = new Locator("REJECT_REASON_DROPDOWN", new By.ByXPath("//button[@data-testid='filter-input-rejectReason-field']"));
    private static final Locator REJECT_REASON_OPTION = new Locator("REJECT_REASON_OPTION", new By.ByXPath("//div[@data-testid='selection-values']//div[@role='option']"));
    private static final Locator APPROVE_REFUND_NOTE = new Locator("APPROVE_REFUND_NOTE", new By.ByXPath("//textarea[@name='statusNotes']"));
    private static final Locator REFUND_PAYMENT_BREAKDOWN = new Locator("REFUND_PAYMENT_BREAKDOWN", new By.ByXPath("//p[contains(text(),'Payment')]//following-sibling::ul//li"));
    private static final Locator REFUND_FEES_CHARGED_BREAKDOWN = new Locator("REFUND_FEES_CHARGED_BREAKDOWN", new By.ByXPath("//div[@class='refund-amount-wrapper']//li[3]//p[2]"));
    private static final Locator REFUND_TOTAL_ESTIMATE = new Locator("REFUND_TOTAL_ESTIMATE", new By.ByXPath("//*[text()='Estimated Refund']/parent::div[@class='m-b-2']//li//p[2]"));

    private Locator getRefundListTabName(String tabName) {
        return new Locator("TAB_REFUND", new By.ByXPath("//div[@class='!text-xs'][text()='"+ tabName +"']/parent::span"));
    }
    private Locator getDropdownSearchByOption(String searchBy) {
        return new Locator("DROPDOWN_MENU_OPTION", new By.ByXPath("//div[contains(@class,'dropdown-menu__option')][text()='"+ searchBy +"']"));
    }
    private Locator selectDayRequestDate(int day) {
        return new Locator("REQUEST_DATE_DAY", new By.ByXPath("//div[@class='day-mod-body '][text()='"+ day +"']"));
    }
    private Locator selectDayMonthCalendar(String dateMonth) {
        return new Locator("SELECT_DATE_MONTH", new By.ByXPath("//button[contains(@class, 'day')]//span[contains(@aria-label,'"+ dateMonth +"')]"));
    }
    private Locator selectFilterStatusProgram(String option) {
        return new Locator("REFUND_STATUS_PROGRAM_OPTION", new By.ByXPath("//span[contains(text(),'"+ option +"')]"));
    }
    private Locator refundCurrencyType (String currency){
        return  new Locator("REFUND_CURRENCY_TYPE", new By.ByXPath("//*[contains(@id,'refund-amount')]//*[contains(text(), '"+ currency +"')]"));
    }
    private Locator exchangeRateType (String currency){
        return  new Locator("EXCHANGE_RATE_TYPE", new By.ByXPath("//div[@data-testid='multi-currency-details']//p[contains(text(), '"+ currency +"')]"));
    }
    private Locator refundEstimationType (String estimationType){
        return new Locator("REFUND_ESTIMATION_TYPE", new By.ByXPath("//p[contains(text(), '"+ estimationType +"']"));
    }
    private Locator getRefundListByColumn(String column) {
        return new Locator("REFUND_LIST_BY_COLUMN", new By.ByXPath("//td[position() = count(//th[div[text()='"+column+"']]/preceding-sibling::th) + 1]"));
    }
    private Locator getAmountPerMethod(String refundMethod) {
        return new Locator("AMOUNT_REFUND_METHOD", new By.ByXPath("//p[contains(text(),'"+refundMethod+"')]/following-sibling::p"));
    }
    private Locator buttonDetailAction(String btnName) {
        return new Locator("BUTTON_DETAIL_ACTION", new By.ByXPath("//button[@role='radio'][@value='"+normalizeActionButton(btnName)+"']"));
    }
    private Locator buttonDetailActionModal(String btnName) {
        return new Locator("BUTTON_DETAIL_ACTION_MODAL", new By.ByXPath("//footer[contains(@class,'Modal_modal_footer')]//button[contains(@class,'Button_variant_primary')][.//span[normalize-space()='"+btnName+"']]"));
    }
    private String normalizeActionButton(String btnName) {
        String name = btnName == null ? "" : btnName.trim().toLowerCase();
        return switch (name) {
            case "received", "receive" -> "receive";
            case "approved", "approve" -> "approve";
            case "reject" -> "reject";
            case "cancel" -> "cancel";
            default -> name;
        };
    }
    private Locator getRefundDetailPriceBreakdowns(String item) {
        return new Locator("DETAIL_BREAKDOWN_"+item.toUpperCase(), By.xpath("//*[contains(@class,'card-body')]//p[contains(text(),'"+item+"')]/following-sibling::ul//p/following-sibling::p"));
    };
    private static final String[] tabName = {"Native Flights", "GDS Flight", "Hotel", "Homes", "Airport Transfer", "To Do", "Car Rental", "Train", "Bus & Shuttle", "Ferry"};
    private static final String[] refundStatuses = {"Created", "Hold", "Sent to tiket.com", "Reviewed by tiket.com", "Verified by tiket.com", "Processing the settlement", "Pending from vendor", "Pending in user", "Submission expired", "Refund request id rejected", "Refund has completed"};
    private static final Map<String, String> statusReceived = Map.of(
            "Native Flights","Reviewed by airline",
            "GDS Flights","Reviewed by airline",
            "Hotel","Reviewed by hotel",
            "Homes","Reviewed by hotel"
    );
    private static final Map<String, String> statusVerified = Map.of(
            "Native Flights","Verified by airline",
            "GDS Flights","Verified by airline",
            "Hotel","Verified by hotel",
            "Homes","Verified by hotel"
    );
    private static final Map<String, String> mapProgramName = Map.of(
            "100% Refund", "100pRefund",
            "EventCancellation", "EventCancellation",
            "TTDCancellation", "TTDCancellation",
            "100pReschedule", "100pReschedule"
    );
    private static final Map<String, String> mapTabNametoVetical = Map.of(
            "Native Flights", "flight_native",
            "GDS Flights", "flight_gds",
            "Hotel", "tixhotel",
            "Homes", "homes",
            "Airport Transfer", "airport_transfer",
            "To Do", "event",
            "Car Rental", "car",
            "Train", "train"
    );
    private String startDateString;
    private String endDateString;
    private String rejectReason;
    private static final String digitPattern = "[^\\d]";

    public RefundListPage(Session<T> session) { super(session); }

    public void goToRefundList(){
        driver.manage().window().maximize();
        int maxRetries = 3;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                navigateToRefundList();
                if (driverTools.waitForElementToAppear(TITLE_REFUND_LIST, 10).isPresent()) {
                    driverTools.waitForElementToDisappear(LOADING_STATE);
                    return;
                }
            } catch (Exception e) {
                logger.log("Attempt " + attempt + " to open Refund List failed: " + e.getLocalizedMessage());
            }
            logger.log("Refund List page not ready (attempt " + attempt + "/" + maxRetries + "). Hard refreshing page (bypass cache).");
            driverTools.hardRefreshWebpage();
            driverTools.waitForElementToDisappear(PAGE_LOADING_SKELETON, 15);
        }
        throw new NoSuchElementException("Failed to load Refund List page after " + maxRetries + " hard refresh attempts");
    }

    private void navigateToRefundList() {
        driverTools.waitForElementToAppear(MENU_REFUND_LIST, 2);
        driverTools.waitForElementToAppear(REFUND_LEFT_HEADER, 2);
        driverTools.waitForElementToAppear(TITLE_REFUND_LIST, 2);

        boolean isMenuRefundList = driverTools.isElementDisplayed(MENU_REFUND_LIST);
        boolean isRefundLeftHeader = driverTools.isElementDisplayed(REFUND_LEFT_HEADER);

        if (!isMenuRefundList && !isRefundLeftHeader) {
            AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
            adminCarePage.goToRefundPage();
        }
        if (!driverTools.isElementDisplayed(TITLE_REFUND_LIST)){ // re-check after any navigation to avoid a stale click decision
            driverTools.waitForElementToAppear(MENU_REFUND_LIST);
            driverTools.click(MENU_REFUND_LIST);
        }
    }

    public String goToRefundListTab() {
        driverTools.waitForElementToAppear(TITLE_REFUND_LIST);
        driverTools.scrollToElement(TITLE_REFUND_LIST);
        int tabIndex = CoreFunctions.generateRandomInt(0, 7);
        String name = tabName[tabIndex];
        logger.log("Selected vertical: " + name);
        driverTools.click(getRefundListTabName(name));
        driverTools.waitForElementToDisappear(LOADING_STATE);
        return name;
    }

    public void goToRefundListTab(String name) {
        driverTools.waitForElementToAppear(TITLE_REFUND_LIST);
        driverTools.scrollElementToCenter(TITLE_REFUND_LIST);
        driverTools.click(getRefundListTabName(name));
        driverTools.waitForElementToDisappear(LOADING_STATE);
    }

    public boolean verifyRefundListTab() {
        for (String t : tabName) {
            goToRefundListTab(t);
            driverTools.waitForElementToDisappear(LOADING_STATE);
            if (driverTools.isElementDisplayed(NODATA_REFUND_LIST)) {
                logger.log("Refund tab " + t + " is empty.");
                return true;
            }
        }
        return true;
    }

    public void clickSearchButton() {
        driverTools.click(SEARCH_BUTTON);
        driverTools.waitForElementToDisappear(LOADING_STATE);
    }

    public void clickFilterButton() {
        WebElement filterButton = driverTools.waitForElementToAppear(FILTER_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(filterButton);
    }

    public void clickApplyButton() {
        driverTools.click(APPLY_BUTTON);
        driverTools.waitForElementToDisappear(LOADING_STATE);
    }

    public String getIdFromOrderPair(String pair, String returnType){
        String regex = "#(\\d+) - (\\d+)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pair);
        String group = "";

        if (matcher.find()) {
            group = switch (returnType) {
                case "Order ID" -> matcher.group(1);
                case "Order Detail ID" -> matcher.group(2);
                default -> group;
            };
        }else {
            group = "Wrong order pair";
        }
        return group;
    }

    public void searchUsingFilterSearchBy(String searchBy, String keyword){
        Locator inputField;
        switch (searchBy.trim()) {
            case "Order ID":
                inputField = SEARCHBY_ORDER_ID;
                break;
            case "Request ID":
                inputField = SEARCHBY_REFUND_ID;
                break;
            default:
                throw new IllegalArgumentException("Unsupported searchBy value: '" + searchBy + "'. Expected 'Order ID' or 'Request ID'.");
        }
        driverTools.waitForElementToAppear(inputField).orElseThrow();
        driverTools.click(inputField);
        driverTools.sendKeys(inputField, keyword);
        clickSearchButton();
        driverTools.waitForElementToDisappear(LOADING_STATE);
    }

    public boolean verifyOrderPair(String toVerify, String returnType) {
        if (driverTools.isElementDisplayed(NODATA_REFUND_LIST)) return false;
        List<WebElement> orderPair = driverTools.elements(ORDER_PAIR).orElseThrow();
        for (WebElement pair : orderPair) {
            String id = getIdFromOrderPair(String.valueOf(driverTools.getText(pair)), returnType);
            if (!Objects.equals(toVerify, id)) return false;
        }
        logger.log("verify order pair "+toVerify+" has found");
        return true;
    }

    public void searchRefundByOrderId(String orderId){
        driverTools.click(SEARCHBY_ORDER_ID);
        driverTools.sendKeys(SEARCHBY_ORDER_ID, orderId);
        clickSearchButton();
        driverTools.waitForElementToDisappear(LOADING_STATE);
        scrollToRefundListColumn("Order ID");
    }

    public void searchRefundByRefundId(String refundId){
        driverTools.click(SEARCHBY_REFUND_ID);
        driverTools.sendKeys(SEARCHBY_REFUND_ID, refundId);
        clickSearchButton();
        driverTools.waitForElementToDisappear(LOADING_STATE);
        scrollToRefundListColumn("Request ID");
    }

    private void scrollToRefundListColumn(String column) {
        Locator columnCell = getRefundListByColumn(column);
        try {
            driverTools.scrollToElement(columnCell);
        } catch (StaleElementReferenceException se) {
            // list rows are re-rendered right after the loading skeleton disappears
            logger.log("Refund list re-rendered while scrolling to column " + column + ", re-fetching once");
            Locator refreshedColumnCell = getRefundListByColumn(column);
            driverTools.waitForElementsToAppear(refreshedColumnCell);
            driverTools.scrollToElement(refreshedColumnCell);
        }
    }

    public String getRequestIdInList(){
        driverTools.waitForElementToAppear(REQUEST_ID);
        List<WebElement> requestIds = driverTools.elements(REQUEST_ID).orElseThrow();
        try {
            if (requestIds.size() > 1) {
                int rowNum = CoreFunctions.generateRandomInt(1, 20);
                return String.valueOf(driverTools.getText(requestIds.get(rowNum)));
            }
            return String.valueOf(driverTools.getText(requestIds.get(0)));
        } catch (IndexOutOfBoundsException e) {
            logger.log(e.getLocalizedMessage());
            return null;
        }
    }

    public void selectTodayDate(String dateType) {
        driverTools.waitForElementToAppear(REQUEST_DATE);
        WebElement requestDate = switch (dateType) {
            case "start" -> driverTools.elements(REQUEST_DATE).orElseThrow().get(0);
            case "end" -> driverTools.elements(REQUEST_DATE).orElseThrow().get(1);
            default -> null;
        };
        driverTools.click(Objects.requireNonNull(requestDate));

        driverTools.waitForElementToAppear(DATE_TODAY);
        driverTools.click(DATE_TODAY);
        driverTools.click(DATE_DONE);
        setDateString();
    }

    public void selectRequestStartDate(String date, int monthDuration){
        driverTools.click(CS_REQ_DATE_FROM);
        driverTools.waitForElementsToAppear(CALENDAR_POPUP);
        for (int i=0; i<=monthDuration; i++) {
            if (driverTools.isElementPresent(selectDayMonthCalendar(date))) {
                driverTools.click(selectDayMonthCalendar(date));
                break;
                }
            driverTools.click(CALENDAR_PREV_MONTH);
            }
        driverTools.waitForElementToDisappear(CALENDAR_POPUP);
    }

    public void setDepartDate(String startDate, String endDate, int monthDuration){
        driverTools.click(FILTER_DEPART_START);
        driverTools.waitForElementsToAppear(CALENDAR_POPUP);
        for (int i=0; i<=monthDuration; i++) {
            if (driverTools.isElementPresent(selectDayMonthCalendar(startDate))) {
                    driverTools.click(selectDayMonthCalendar(startDate));
                    break;
                }
            driverTools.click(CALENDAR_PREV_MONTH);
            }
        driverTools.waitForElementToDisappear(CALENDAR_POPUP);
        driverTools.waitForElementToAppear(FILTER_DEPART_END);
        driverTools.click(FILTER_DEPART_END);
        driverTools.waitForElementsToAppear(CALENDAR_POPUP);
        for (int i=0; i<=monthDuration-1; i++) {
            if (driverTools.isElementPresent(selectDayMonthCalendar(endDate))) {
                driverTools.click(selectDayMonthCalendar(endDate));
                break;
                }
            driverTools.click(CALENDAR_NEXT_MONTH);
            }
        driverTools.waitForElementToDisappear(CALENDAR_POPUP);
    }
    public void selectDate(int day, String monthYear, String dateType) {
        // click filter request date
        WebElement requestDate = driverTools.waitForElementToAppear(REQUEST_DATE).orElseThrow();
        requestDate = switch (dateType) {
            case "start" -> driverTools.elements(REQUEST_DATE).orElseThrow().get(0);
            case "end" -> driverTools.elements(REQUEST_DATE).orElseThrow().get(1);
            default -> null;
        };
        driverTools.click(Objects.requireNonNull(requestDate));

        // format year month from param and actual on page
        String requestDateMonthYear = driverTools.getText(DATE_MONTH_YEAR).orElseThrow();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");

        YearMonth inputMonthYear = YearMonth.parse(monthYear, formatter);
        YearMonth actualMonthYear = YearMonth.parse(requestDateMonthYear, formatter);
        int compare = inputMonthYear.compareTo(actualMonthYear);

        // search for desired year month param
        while (compare != 0) {
            if (compare > 0) {
                driverTools.click(DATE_NEXT_MONTH);
            } else {
                driverTools.click(DATE_PREV_MONTH);
            }
            requestDateMonthYear = driverTools.getText(DATE_MONTH_YEAR).orElseThrow();
            actualMonthYear = YearMonth.parse(requestDateMonthYear, formatter);
            compare = inputMonthYear.compareTo(actualMonthYear);
        }

        // select day and done
        WebElement selectDayRequest = driverTools.waitForElementToAppear(selectDayRequestDate(day)).orElseThrow();
        driverTools.click(selectDayRequest);
        driverTools.click(DATE_DONE);
        setDateString();
    }

    public boolean verifyFilterDate(String startDate, String endDate, String dateType) {
        //get filtered request date
        DateTimeFormatter createdFormatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy",Locale.ENGLISH);
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);

        LocalDate startDateReq = LocalDate.parse(startDate, inputFormatter);
        LocalDate endDateReq = LocalDate.parse(endDate, inputFormatter);
        LocalDate actualDate = null;

        Locator dateLocator = switch (dateType) {
            case "request" -> SEE_REFUND_DETAIL;
            case "departure" -> VERTICAL_DETAIL;
            default -> throw new IllegalStateException("Unexpected value: " + dateType);
        };

        //get correct date format from refund detail
        String regex = "\\w{3}, \\d{1,2} \\w{3} \\d{4}";
        Pattern pattern = Pattern.compile(regex);

            driverTools.waitForElementToAppear(dateLocator);
            List<WebElement> detail = driverTools.elements(dateLocator).orElseThrow();
            driverTools.scrollIntoWebView(TITLE_REFUND_LIST);
            for (WebElement dt : detail) {
                String dateCreated = "";
                switch (dateType) {
                    case "request":
                        driverTools.click(dt);
                        driverTools.waitForElementToAppear(CREATED_DATE_IN_REFUND_DETAILS);
                        List<WebElement> infoDateRefund = driverTools.elements(CREATED_DATE_IN_REFUND_DETAILS).orElseThrow();
                        dateCreated = String.valueOf(driverTools.getText(infoDateRefund.get(0)));
                        break;
                    case "departure":
                        dateCreated = String.valueOf(driverTools.getText(dateLocator));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + dateType);
                }

                // get the actual created date from refund detail
                Matcher matcher = pattern.matcher(dateCreated);
                if (matcher.find()) {
                    actualDate = LocalDate.parse(matcher.group(), createdFormatter);
                } else {
                    logger.log("No matcher found");
                }
                // compare dates
                int actualToStart = Objects.requireNonNull(actualDate).compareTo(startDateReq);
                int actualToEnd = Objects.requireNonNull(actualDate).compareTo(endDateReq);
                if ((actualToStart < 0) || (actualToEnd > 0)) {
                    logger.log("date not in range: "+actualDate);
                    return false;
                }

                if (dateType.equals("request")) {
                    WebElement toggleCloseDetail = driverTools.waitForElementToAppear(TOGGLE_CLOSE_REFUND_DETAIL).orElseThrow();
                    driverTools.click(toggleCloseDetail);
                }
        }
        return true;
    }

    public void setDateString () {
        startDateString = driverTools.getValue(driverTools.elements(DATE_FILTERED).orElseThrow().get(0));
        endDateString = driverTools.getValue(driverTools.elements(DATE_FILTERED).orElseThrow().get(1));
        String[] partsStart = startDateString.split(" - ");
        String[] partsEnd = endDateString.split(" - ");
        if (partsStart.length == 2 && partsEnd.length == 2) {
            startDateString = partsStart[0];
            endDateString = partsEnd[0];
        }
    }

    public void selectFilterStatus (String status) {
        driverTools.click(FILTER_REFUND_STATUS);
        WebElement statusFilter = driverTools.waitForElementToAppear(selectFilterStatusProgram(status)).orElseThrow();
        driverTools.scrollIntoWebView(statusFilter);
        driverTools.click(statusFilter);
    }

    public String adjustRefundStatusName (String status, String vertical) {
        if (status.equals("Reviewed by tiket.com")) {
            status = switch (vertical) {
                case "Native Flights" -> statusReceived.get("Native Flights");
                case "GDS Flights" -> statusReceived.get("GDS Flights");
                case "Hotel" -> statusReceived.get("Hotel");
                case "Homes" -> statusReceived.get("Homes");
                default -> status;
            };
        } else if (status.equals("Verified by tiket.com")) {
            status = switch (vertical) {
                case "Native Flights" -> statusVerified.get("Native Flights");
                case "GDS Flights" -> statusVerified.get("GDS Flights");
                case "Hotel" -> statusVerified.get("Hotel");
                case "Homes" -> statusVerified.get("Homes");
                default -> status;
            };
        }
        return status;
    }

    public boolean hasNextPage () {
        if (driverTools.isElementDisplayed(PAGINATION_NEXT)) {
            driverTools.click(PAGINATION_NEXT);
            driverTools.waitForElementToDisappear(LOADING_STATE);
        } else {
            return false;
        }
        return true;
    }

    public boolean verifyFilterRefundStatus (String status, String vertical) {
        String regex = "^(.*?)\\n";
        Pattern pattern = Pattern.compile(regex);
        if (driverTools.isElementDisplayed(NODATA_REFUND_LIST)) return false;
        driverTools.waitForElementToAppear(REFUND_STATUS);

        status = adjustRefundStatusName(status, vertical);

        // verify status on refund list
        driverTools.scrollIntoWebView(TITLE_REFUND_LIST);
        List<WebElement> refundStatus = driverTools.elements(REFUND_STATUS).orElseThrow();
        for (WebElement rs : refundStatus) {
            String refundStatusList = driverTools.getText(rs);
            Matcher matcher = pattern.matcher(refundStatusList);
            if (matcher.find()) {
                if (!status.equals(matcher.group(1))) {
                    logger.log("refund status on list not equal: "+matcher.group(1));
                    return false;
                }
                // verify status on refund detail
                driverTools.click(SEE_REFUND_DETAIL);
                driverTools.waitForElementToAppear(INFO_TITLE_REFUND_STATUS);
                List<WebElement> infoTitleRefundStatus;
                if (status.contains("rejected")) {
                    infoTitleRefundStatus = driverTools.elements(INFO_TITLE_STATUS_REJECTED).orElseThrow();
                } else {
                    infoTitleRefundStatus = driverTools.elements(INFO_TITLE_REFUND_STATUS).orElseThrow();
                }
                String refundStatusDetail = driverTools.getText(infoTitleRefundStatus.get(infoTitleRefundStatus.size()-1));
                if (!status.equals(refundStatusDetail)) {
                    logger.log("refund status on detail not equal: "+refundStatusDetail);
                    logger.log(driverTools.getText(REFUND_DETAIL_HEADER).orElseThrow());
                    driverTools.click(TOGGLE_CLOSE_REFUND_DETAIL);
                    return false;
                }
                driverTools.click(TOGGLE_CLOSE_REFUND_DETAIL);
            } else {
                logger.log("No matcher found");
                return false;
            }
        }
        return true;
    }

    public void selectFilterProgramName (String program) {
        driverTools.waitForElementToBeClickable(FILTER_PROGRAM_NAME);
        driverTools.click(FILTER_PROGRAM_NAME);
        WebElement programFilter = driverTools.waitForElementToAppear(selectFilterStatusProgram(program)).orElseThrow();
        driverTools.scrollIntoWebView(programFilter);
        driverTools.click(programFilter);
    }

    public boolean verifyFilterProgramName (String programName) {
        programName = mapProgramName.get(programName);
        if (driverTools.isElementDisplayed(NODATA_REFUND_LIST)) return false;
        driverTools.scrollIntoWebView(TITLE_REFUND_LIST);
        List<WebElement> refundDetail = driverTools.elements(SEE_REFUND_DETAIL).orElseThrow();
        for (WebElement rd : refundDetail) {
            driverTools.click(rd);
            driverTools.waitForElementToAppear(INFO_TITLE_REFUND_STATUS);
            if (!driverTools.getText(DETAIL_PROGRAM_NAME).get().contains(programName)) {
                logger.log("program name is not same");
                logger.log(driverTools.getText(REFUND_DETAIL_HEADER).orElseThrow());
                return false;
                }
            if (driverTools.getText(DETAIL_POLICY_NUMBER).get().equals("-")) {
                logger.log("Policy number is empty");
                logger.log(driverTools.getText(REFUND_DETAIL_HEADER).orElseThrow());
                }
            driverTools.click(TOGGLE_CLOSE_REFUND_DETAIL);
        }
        return true;
    }

    public boolean verifyRefundType (String refundType){
        driverTools.scrollIntoWebView(TITLE_REFUND_LIST);
        if (!refundType.equals(driverTools.getText(REFUND_TYPE).get())) {
            logger.log("refund type is not equal");
            return false;
        }
        return true;
    }

    public void clickExportCSV(){
        WebElement exportCSV = driverTools.waitForElementToAppear(EXPORT_CSV).orElseThrow();
        driverTools.click(exportCSV);
    }

    public String exportFilenameBuilder(String tabName){
        String prefix = "RefundList";
        String separator = "_";
        String vertical = mapTabNametoVetical.get(tabName);

        ZoneId zone = ZoneId.systemDefault();

        ZonedDateTime startDefault = LocalDate.now().minusDays(7).atStartOfDay(zone);
        ZonedDateTime endDefault = LocalDate.now().atTime(23, 59, 59).atZone(zone);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH_mm_ssXXX").withZone(zone);
        String startDateStr = formatter.format(startDefault).replace(":", "_");
        String endDateStr = formatter.format(endDefault).replace(":", "_");

        return prefix + separator + vertical + separator + startDateStr + separator + endDateStr + ".csv";
    }

    public boolean verifyDownloadedExportCSV(String tabName, String timeOutMiliSec){
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        logger.log("Download folder: "+downloadDir);
        String expectedFilename = exportFilenameBuilder(tabName);
        logger.log("expected filename: "+expectedFilename);
        boolean fileDownloaded = false;

        long startTime = Instant.now().toEpochMilli();
        logger.log("Start time: "+startTime);
        long waitTime = startTime + Integer.parseInt(timeOutMiliSec);
        logger.log("Wait time: "+waitTime);

        while (Instant.now().toEpochMilli() < waitTime){
            File[] listFiles = new File(downloadDir).listFiles();
            for (File file : listFiles){
                if (file.getName().equals(expectedFilename)){
                    String[] listFilesName = new File(downloadDir).list(); //logging purpose
                    for (int i = 0; i < listFilesName.length; i++) {
                        logger.log("File name on dir: "+listFilesName[i]);
                    }
                    fileDownloaded = true;
                    file.delete();
                    break;
                }
            }
            if (fileDownloaded) break;
        }

        return fileDownloaded;
    }

    public String getRequestIdFromDB (String vertical, String vendor) throws Exception {
        String sqlQuery = "";
        RefundRequestDTO result;
        Set<String> vendorFlight = new HashSet<>(Arrays.asList("native", "sabre"));
        if (vendorFlight.contains(vendor)) {
            sqlQuery = "SELECT * FROM tix_refund.refund_request WHERE vertical_name = '"+vertical+"' AND vendor = '"+vendor+"' AND refund_option_key != 'manual-refund' ORDER BY id DESC LIMIT 1";
            logger.log("going to first");
        } else {
            sqlQuery = "SELECT * FROM tix_refund.refund_request WHERE vertical_name = '"+vertical+"' AND refund_option_key != 'manual-refund' ORDER BY id DESC LIMIT 1";
            logger.log("going to second");
        }

        var db = TiketMysqlDBManager.getInstance("refund");
        var dbExecute = ResultSetMapperFactory.getResultSetMapperIdentity().map(db.executeQuery(sqlQuery),RefundRequestDTO.class);
        if (dbExecute.stream().count() < 1) {
            throw new Exception("Data not found in Database!");
        }
        result = dbExecute.get(0);
        logger.log("Data found in Database"+result.toString());
        return result.getId().toString();
    }


    public String getOrderIdFromRefundDB (String vertical, String vendor) throws Exception {
        String sqlQuery = "";
        RefundRequestDTO result;
        Set<String> vendorFlight = new HashSet<>(Arrays.asList("native", "sabre"));
        if (vendorFlight.contains(vendor)) {
            sqlQuery = "SELECT * FROM tix_refund.refund_request WHERE vertical_name = '"+vertical+"' AND vendor = '"+vendor+"' AND refund_option_key != 'manual-refund' ORDER BY id DESC LIMIT 1";
            logger.log("going to first");
        } else {
            sqlQuery = "SELECT * FROM tix_refund.refund_request WHERE vertical_name = '"+vertical+"' AND refund_option_key != 'manual-refund' ORDER BY id DESC LIMIT 1";
            logger.log("going to second");
        }

        var db = TiketMysqlDBManager.getInstance("refund");
        var dbExecute = ResultSetMapperFactory.getResultSetMapperIdentity().map(db.executeQuery(sqlQuery),RefundRequestDTO.class);
        if (dbExecute.stream().count() < 1) {
            throw new Exception("Data not found in Database!");
        }
        result = dbExecute.get(0);
        logger.log("Data found in Database"+result.toString());
        return result.getReference_order_id().toString();
    }

    public boolean verifyRefundRequestOnDB(String orderId, String optionKey) throws Exception {
        String sqlQuery = "SELECT * FROM tix_refund.refund_request WHERE reference_order_id = "+orderId;
        RefundRequestDTO result;
        var db = TiketMysqlDBManager.getInstance("refund");
        var dbExecute = ResultSetMapperFactory.getResultSetMapperIdentity().map(db.executeQuery(sqlQuery),RefundRequestDTO.class);
        if (dbExecute.stream().count() < 1) {
            throw new Exception("Data not found in Database!");
        }
        result = dbExecute.get(0);
        logger.log("Data found in Database"+result.toString());
        // verify data
        if (dbExecute.stream().count() > 1) {
            logger.fail("More than 1 data found on DB, should be only 1");
            return false;
        }
        if (!result.getReference_order_id().equals(new BigInteger(orderId))) {
            logger.fail("order id in DB is different. DB order id -> "+result.getReference_order_id());
            return false;
        }
        if (!result.getRefund_option_key().equals(optionKey)) {
            logger.fail("incorrect option key. DB option key -> "+result.getRefund_option_key());
            return false;
        }
        return true;
    }

    public boolean verifyFilterRequestId(String id) {
        if (driverTools.isElementDisplayed(NODATA_REFUND_LIST)) return false;
        List<WebElement> requestIdElmnt = driverTools.elements(getRefundListByColumn("Request ID")).orElseThrow();
        if (requestIdElmnt.size() > 1) {
            logger.log("request id can't be more than 1");
            return false;
        }
        String requestId = driverTools.getText(requestIdElmnt.get(0));
        if (!Objects.equals(requestId, id)) {
            logger.log("Request id not match. request id on list: "+requestId+". request id from param: "+id);
            return false;
        }
        logger.log("verify request id "+requestId+" has found");
        return true;
    }

    public boolean verifyFilterOrderId(String id) {
        if (driverTools.isElementDisplayed(NODATA_REFUND_LIST)) return false;
        List<WebElement> orderIdElmnt = driverTools.elements(getRefundListByColumn("Order ID")).orElseThrow();
        String orderIdCell = driverTools.getText(orderIdElmnt.get(0));
        Matcher orderIdMatcher = Pattern.compile("\\d+").matcher(orderIdCell); // cell can be "1900652627" or "#1900652627 - 12345"
        if (!orderIdMatcher.find()) {
            logger.log("No Order Id found on list. Order Id column text: "+orderIdCell);
            return false;
        }
        String orderId = orderIdMatcher.group();
        if (!Objects.equals(orderId, id)) {
            logger.log("Order Id not match. Order Id on list: "+orderId+". Order Id from param: "+id);
            return false;
        }
        logger.log("verify Order Id "+orderId+" has found");
        return true;
    }

    public boolean verifyDetailsHotelList(String orderName, String roomName, String checkIn, String checkOut) {
        List<WebElement> detailsListElmts = driverTools.elements(getRefundListByColumn("Details")).orElseThrow();
        List<WebElement> seeDetail = driverTools.elements(SEE_REFUND_DETAIL).orElseThrow();
        ArrayList<String> expectedOrderReview = new ArrayList<String>(Arrays.asList(orderName, roomName, checkIn, checkOut));
        String stringDetail;
        for (int i = 0; i < detailsListElmts.size(); i++){
            stringDetail = driverTools.getText(detailsListElmts.get(i));
            for (String ex : expectedOrderReview) {
                if (!stringDetail.contains(ex)) {
                    logger.log(ex+" is not expected in Refund List");
                    return false;
                }
            }
            driverTools.click(seeDetail.get(i)); //go to refund detail
            Optional<WebElement> status = driverTools.waitForElementToAppear(INFO_TITLE_REFUND_STATUS);
            stringDetail = driverTools.getText(VERTICAL_DETAILS).orElseThrow();
            for (String ex : expectedOrderReview) {
                if (!stringDetail.contains(ex)) {
                    logger.log(ex+" is not expected in Refund Detail");
                    return false;
                }
            }
            driverTools.click(TOGGLE_CLOSE_REFUND_DETAIL);
        }
        return true;
    }

    public boolean verifyDetailsTodoList(String productName, String productCategory, int ticketQty, String validityDate) {
        String detailsListElement = driverTools.getText(getRefundListByColumn("Details")).orElseThrow();
        WebElement seeDetail = driverTools.element(SEE_REFUND_DETAIL).orElseThrow();
        String ticketQtyString = String.valueOf(ticketQty)+" Ticket(s)";
        ArrayList<String> expectedOrderReview = new ArrayList<String>(Arrays.asList(productName, productCategory, ticketQtyString, validityDate));
        for (String ex : expectedOrderReview) { // verify details in list
            if (!detailsListElement.contains(ex)) {
                logger.log(ex+" is not expected in Refund List");
                return false;
            }
        }
        driverTools.click(seeDetail); //go to refund detail
        Optional<WebElement> status = driverTools.waitForElementToAppear(INFO_TITLE_REFUND_STATUS);
        String verticalDetail = driverTools.getText(VERTICAL_DETAILS).orElseThrow();
        for (String ex : expectedOrderReview) { // verify details in refund detail
            if (!verticalDetail.contains(ex)) {
                logger.log(ex+" is not expected in Refund Detail");
                return false;
            }
        }
        driverTools.click(TOGGLE_CLOSE_REFUND_DETAIL);

        return true;
    }

    public boolean verifyDetailsCarList(String carName, String carVendor, String carStartDate, String pickupTime, int carCount, int rentalDuration) {
        String detailsListElement = driverTools.getText(getRefundListByColumn("Details")).orElseThrow();
        WebElement seeDetail = driverTools.element(SEE_REFUND_DETAIL).orElseThrow();
        String carCountString = String.valueOf(carCount)+" Car(s)";
        String rentalDurationString = String.valueOf(rentalDuration)+" Day(s)";
        ArrayList<String> expectedOrderReview = new ArrayList<String>(Arrays.asList(carName, carVendor, carStartDate, pickupTime, carCountString, rentalDurationString));
        for (String ex : expectedOrderReview) { // verify details in list
            if (!detailsListElement.contains(ex)) {
                logger.log(ex+" is not expected in Refund List");
                return false;
            }
        }
        driverTools.click(seeDetail); //go to refund detail
        Optional<WebElement> status = driverTools.waitForElementToAppear(INFO_TITLE_REFUND_STATUS);
        String verticalDetail = driverTools.getText(VERTICAL_DETAILS).orElseThrow();
        for (String ex : expectedOrderReview) { // verify details in refund detail
            if (!verticalDetail.contains(ex)) {
                logger.log(ex+" is not expected in Refund Detail");
                return false;
            }
        }
        driverTools.click(TOGGLE_CLOSE_REFUND_DETAIL);
        return true;
    }

    public boolean verifyRefundTypeList(String expectedType) {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        Locator typeColumn = getRefundListByColumn("Type");
        List<WebElement> typeListElmts = driverTools.elements(typeColumn).orElseThrow();
        if (typeListElmts.isEmpty()) {
            logger.log("Refund list is empty (no Type cells found), cannot verify Type='" + expectedType + "'");
            return false;
        }
        for (int i = 0; i < typeListElmts.size(); i++) {
            String type;
            try {
                type = driverTools.getText(typeListElmts.get(i));
            } catch (StaleElementReferenceException se) {
                logger.log("Stale element on row " + i + ", re-fetching list once");
                typeListElmts = driverTools.elements(typeColumn).orElseThrow();
                if (i >= typeListElmts.size()) {
                    logger.log("Row " + i + " no longer present after re-fetch, stopping");
                    break;
                }
                type = driverTools.getText(typeListElmts.get(i));
            }
            if (!type.equals(expectedType)) {
                logger.log("Incorrect Type: " + type);
                return false;
            }
        }
        return true;
    }

    public boolean verifyTotalAmountAndMethodList(BigInteger issuedTotalPayment, String currency, String refundMethod) {
        List<WebElement> amountListElements = driverTools.elements(getRefundListByColumn("Refund Amount")).orElseThrow();
        List<WebElement> seeDetail = driverTools.elements(SEE_REFUND_DETAIL).orElseThrow();
        String totalPayment = NumberFormat.getInstance().format(issuedTotalPayment);
        String expectedAmount = currency+" "+totalPayment;
        String amount;
        for (int i = 0; i < amountListElements.size(); i++){
            driverTools.scrollIntoWebView(amountListElements.get(i));
            amount = driverTools.getText(amountListElements.get(i));
            if (!amount.contains(expectedAmount)) { //verify in refund list
                logger.log("Incorrect Refund Amount"+amount);
                return false;
            }
            if (!amount.toLowerCase().contains(refundMethod.toLowerCase())) {
                logger.log("Incorrect Refund Method"+amount);
                return false;
            }
            driverTools.click(seeDetail.get(i)); //go to refund detail
            Optional<WebElement> status = driverTools.waitForElementToAppear(INFO_TITLE_REFUND_STATUS);
            driverTools.scrollIntoWebView(ESTIMATION_SEE_DETAILS);
            driverTools.click(ESTIMATION_SEE_DETAILS);

            String totalRefundAmount = driverTools.getText(REFUND_ESTIMATION_TOTAL).orElseThrow();
            if (!expectedAmount.equals(totalRefundAmount)) {
                logger.log("Incorrect Total Refund Amount: "+totalRefundAmount);
                return false;
            }
            if (refundMethod.equals("Bliblipay")) refundMethod = "BlibliPay";
            driverTools.scrollIntoWebView(getAmountPerMethod(refundMethod));
            String amountRefundMethod = driverTools.getText(getAmountPerMethod(refundMethod)).orElseThrow();
            if (!expectedAmount.equals(amountRefundMethod)) {
                logger.log("Incorrect Total Refund Amount per Method: "+amountRefundMethod);
                return false;
            }
            driverTools.scrollIntoWebView(DETAIL_REFUND_METHOD);
            String detailMethod = driverTools.getText(DETAIL_REFUND_METHOD).orElseThrow();
            if (!detailMethod.toLowerCase().contains(refundMethod.toLowerCase())) {
                logger.log("Incorrect Refund Method: "+detailMethod);
                return false;
            }
            driverTools.scrollIntoWebView(TOGGLE_CLOSE_REFUND_DETAIL);
            driverTools.click(TOGGLE_CLOSE_REFUND_DETAIL); //close refund detail
        }
        return true;
    }

    public ArrayList<String> getRefundMethodsDetailRefund() {
        driverTools.scrollIntoWebView(DETAIL_REFUND_METHODS);
        List<WebElement> estimationDetails = driverTools.elements(DETAIL_REFUND_METHODS).orElseThrow();
        ArrayList<String> refundMethods = new ArrayList<>();
        for (WebElement e : estimationDetails) {
            refundMethods.add(driverTools.getText(e));
        }
        return refundMethods;
    }

    public int getRefundDetailPriceBreakdownItem(String item) {
        driverTools.scrollIntoWebView(ESTIMATION_SEE_DETAILS);
        driverTools.click(ESTIMATION_SEE_DETAILS);
        int total = 0;
        try {
            List<WebElement> breakdowns = driverTools.waitForElementsToAppear(getRefundDetailPriceBreakdowns(item), 2).orElseThrow();
            for (WebElement element : breakdowns) {
                total += Integer.parseInt(driverTools.getText(element).replaceAll(digitPattern, ""));
            }
            logger.log("Got total amount: "+total+" for price breakdown: "+item);
        } catch (NoSuchElementException e) {
            logger.log(e.getLocalizedMessage());
        }
        return total;
    }

    public String getTotalEstimationRefundDetail() {
        try {
            driverTools.click(SEE_REFUND_DETAIL);
        } catch (ElementClickInterceptedException e) {
            logger.log(e.getLocalizedMessage());
        }
        WebElement amount = driverTools.waitForElementToAppear(REFUND_ESTIMATION_TOTAL).orElseThrow();
        driverTools.scrollIntoWebView(REFUND_ESTIMATION_TOTAL);
        driverTools.click(REFUND_ESTIMATION_TOTAL);
        return driverTools.getText(amount);
    }

    public String getBliblipayPhoneNumber() {
        driverTools.click(SEE_REFUND_DETAIL);
        Optional<WebElement> status = driverTools.waitForElementToAppear(INFO_TITLE_REFUND_STATUS);
        driverTools.scrollIntoWebView(REFUND_DETAIL_BLIBLIPAY_PN);
        String bliblipayPN = driverTools.getText(REFUND_DETAIL_BLIBLIPAY_PN).orElseThrow();
        driverTools.scrollIntoWebView(TOGGLE_CLOSE_REFUND_DETAIL);
        driverTools.click(TOGGLE_CLOSE_REFUND_DETAIL);
        return bliblipayPN;
    }

    public String getRefundDescription() {
        try {
            driverTools.click(SEE_REFUND_DETAIL);
        } catch (ElementClickInterceptedException e) {
            logger.log(e.getLocalizedMessage());
        }
        Optional<WebElement> status = driverTools.waitForElementToAppear(INFO_TITLE_REFUND_STATUS);
        driverTools.scrollIntoWebView(REFUND_DETAIL_DESCRIPTION);
        String refundDesc = driverTools.getText(REFUND_DETAIL_DESCRIPTION).orElseThrow();
        driverTools.scrollIntoWebView(TOGGLE_CLOSE_REFUND_DETAIL);
        driverTools.click(TOGGLE_CLOSE_REFUND_DETAIL);
        return refundDesc;
    }

    public Map<String, Boolean> isBtnDisabled() {
        driverTools.waitForElementToAppear(INFO_TITLE_REFUND_STATUS);
        Map<String, Boolean> isBtnMap = new HashMap<>();

        isBtnMap.put("received", isActionButtonDisabled("receive"));
        logger.log("is Received Button Disabled -> "+isBtnMap.get("received").toString());

        isBtnMap.put("reject", isActionButtonDisabled("reject"));
        logger.log("is Reject Button Disabled -> "+isBtnMap.get("reject").toString());

        isBtnMap.put("approved", isActionButtonDisabled("approve"));
        logger.log("is Approved Button Disabled -> "+isBtnMap.get("approved").toString());

        return isBtnMap;
    }

    private boolean isActionButtonDisabled(String btnValue) {
        WebElement button = driverTools.waitForElementToAppear(buttonDetailAction(btnValue)).orElseThrow();
        String disabledAttr = driverTools.getValueAttribute(button, "disabled");
        String classValue = driverTools.getValueAttribute(button, "class");
        boolean disabledByAttr = disabledAttr != null && !disabledAttr.equalsIgnoreCase("false");
        boolean disabledByClass = classValue != null && classValue.contains("disabled");
        return disabledByAttr || disabledByClass;
    }

    public boolean verifyRefundOpenState(String vertical) {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        boolean isStatusOpen = verifyFilterRefundStatus("Sent to tiket.com", vertical); // verify status is open
        logger.log("is Status Open -> "+isStatusOpen);

        driverTools.click(SEE_REFUND_DETAIL); // verify button disabled
        Map<String, Boolean> isBtnDisabled = isBtnDisabled();

        return isStatusOpen && !isBtnDisabled.get("received") && isBtnDisabled.get("reject") && isBtnDisabled.get("approved");
    }

    public boolean verifyRefundReceivedState(String vertical) {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        boolean isStatusCorrect = verifyFilterRefundStatus("Reviewed by tiket.com", vertical); // verify status is received
        logger.log("is Status Received -> "+isStatusCorrect);

        driverTools.click(SEE_REFUND_DETAIL); // verify button disabled
        Map<String, Boolean> isBtnDisabled = isBtnDisabled();

        return isStatusCorrect && isBtnDisabled.get("received") && !isBtnDisabled.get("reject") && !isBtnDisabled.get("approved");
    }

    public boolean verifyRefundRejectedState(String vertical) {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        boolean isStatusCorrect = verifyFilterRefundStatus("Refund request is rejected", vertical); // verify status is rejected
        logger.log("is Status Rejected -> "+isStatusCorrect);

        driverTools.click(SEE_REFUND_DETAIL); // verify button disabled
        Map<String, Boolean> isBtnDisabled = isBtnDisabled();

        String rejectReason = driverTools.getText(REFUND_DETAIL_REJECT_REASON).orElseThrow();
        boolean isCorrectRejectReason = rejectReason.contains(this.rejectReason); // verify reject reason (text includes the "Rejected Reason:" label)
        boolean isRejected = isStatusCorrect && isBtnDisabled.get("received") && isBtnDisabled.get("reject") && isBtnDisabled.get("approved") && isCorrectRejectReason;
        if (isRejected) driverTools.click(TOGGLE_CLOSE_REFUND_DETAIL);

        return isRejected;
    }

    public boolean verifyRefundApprovedState(String vertical) {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        boolean isStatusCorrect = verifyFilterRefundStatus("Verified by tiket.com", vertical); // verify status is rejected
        logger.log("is Status Approved -> "+isStatusCorrect);

        driverTools.click(SEE_REFUND_DETAIL); // verify button disabled
        Map<String, Boolean> isBtnDisabled = isBtnDisabled();
        boolean isApproved = isStatusCorrect && isBtnDisabled.get("received") && isBtnDisabled.get("reject") && isBtnDisabled.get("approved");
        if(isApproved) driverTools.click(TOGGLE_CLOSE_REFUND_DETAIL);

        return isApproved;
    }

    public void selectRejectReason() {
        WebElement rejectReasonDropdown = driverTools.waitForElementToAppear(REJECT_REASON_DROPDOWN).orElseThrow();
        driverTools.click(rejectReasonDropdown);
        driverTools.waitForElementToAppear(REJECT_REASON_OPTION);
        List<WebElement> rejectReasonOption = driverTools.elements(REJECT_REASON_OPTION).orElseThrow();
        rejectReason = driverTools.getText(rejectReasonOption.get(0));
        driverTools.click(rejectReasonOption.get(0));
    }

    public String getRejectReason() {
        logger.log("Rejected Reason -> "+rejectReason);
        return rejectReason;
    }

    public void addApproveNote(String note) {
        WebElement inputNote = driverTools.waitForElementToAppear(APPROVE_REFUND_NOTE).orElseThrow();
        driverTools.sendKeys(inputNote, note);
    }

    public void clickRefundActionButton(String btnName, String btnNameModal) {
        WebElement button = driverTools.waitForElementToBeClickable(buttonDetailAction(btnName)).orElseThrow();
        driverTools.click(button);
        switch (btnNameModal) {
            case "Reject":
                selectRejectReason();
                break;
            case "Approve":
                addApproveNote("Auto Approve");
                break;
            default:
                break;
        }
        WebElement buttonModal = driverTools.waitForElementToBeClickable(buttonDetailActionModal(btnNameModal)).orElseThrow();
        driverTools.click(buttonModal);
        driverTools.waitForElementToDisappear(LOADING_OVERLAY); // wait for processing overlay to clear before closing modal
        driverTools.waitForElementToDisappear(LOADING_STATE);
        Optional<WebElement> toggleCloseDetail = driverTools.waitForElementToAppear(TOGGLE_CLOSE_REFUND_DETAIL, 2);
        if (toggleCloseDetail.isPresent()) driverTools.click(toggleCloseDetail.get()); // close refund detail modal
    }

    public boolean verifyCurrencyType(String currencyType) {
        if (driverTools.isElementDisplayed(NODATA_REFUND_LIST)) return false;
        String currency = driverTools.getText(refundCurrencyType(currencyType)).get();
        if (!currency.contains(currencyType)) {
            logger.log("refund currency is not equal");
            return false;
        }
        logger.log("currency type matched");
        return true;
    }

    public boolean verifyExchangeRate(String currencyType){
        if (driverTools.isElementDisplayed(NODATA_REFUND_LIST)) return false;
        String exchangeRate = driverTools.getText(exchangeRateType(currencyType)).get();
        if (!exchangeRate.contains(currencyType)) {
            logger.log("exchange currency type is not equal");
            return false;
        }
        logger.log("currency type matched");
        return true;
    }

    public boolean verifyCurrencyInRefundDetail(String currencyType){
        driverTools.click(SEE_REFUND_DETAIL);
        driverTools.waitForElementToAppear(REFUND_AMOUNT_SECTION);
        if(!driverTools.getText(REFUND_AMOUNT_VENDOR).get().contains(currencyType)){
            logger.log("exchange currency from vendor is not equal");
            return false;
        }
        if(!driverTools.getText(REFUND_AMOUNT_ACTUAL).get().contains(currencyType)){
            logger.log("exchange currency actual is not equal");
            return false;
        }
        if(!driverTools.getText(REFUND_ESTIMATION_TOTAL).get().contains(currencyType)){
            logger.log("exchange currency type in estimation is not equal");
            return false;
        }
        return true;
    }
    
    public boolean verifyCurrencyInRefundEstimation(String currencyType) {
        driverTools.click(SEE_DETAILS_IN_REFUND_DETAIL);
        driverTools.waitForElementToBeClickable(REFUND_PAYMENT_BREAKDOWN, 2);
        for (WebElement element : driverTools.elements(REFUND_PAYMENT_BREAKDOWN).orElseThrow()) {
            Assertion.assertThat("is payment breakdown contains currency: "+currencyType, driverTools.getText(element), containsString(currencyType));
        }
        for (WebElement element : driverTools.elements(REFUND_FEES_CHARGED_BREAKDOWN).orElseThrow()) {
            Assertion.assertThat("is refund fess breakdown contains currency: "+currencyType, driverTools.getText(element), containsString(currencyType));
        }
        for (WebElement element : driverTools.elements(REFUND_TOTAL_ESTIMATE).orElseThrow()) {
            Assertion.assertThat("is refund total contains currency: "+currencyType, driverTools.getText(element), containsString(currencyType));
        }
        return true;
    }
}
