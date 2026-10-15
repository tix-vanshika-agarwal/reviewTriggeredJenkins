package com.tiket.web.dashboard.page.refund;

import com.tiket.core.CoreFunctions;
import com.tiket.dbclient.TiketMysqlDBManager;
import com.tiket.dto.RefundRequestDTO;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import nl.jiankai.mapper.ResultSetMapperFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tiket.core.CoreFunctions.capitalize;
import static org.hamcrest.Matchers.nullValue;

public class RefundTransactionPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator MENU_REFUND_TRANSACTION = new Locator("MENU_REFUND_TRANSACTION", new By.ByXPath("//span[text()='Transaction']"));
    private static final Locator DATA_PER_PAGE_INPUT = new Locator("DATA_PER_PAGE_INPUT", new By.ByXPath("//*[@data-testid='size-trigger']"));
    private static final Locator LOADING_TABLE_CONTENT = new Locator("LOADING_TABLE_CONTENT", new By.ByXPath("//td[contains(@class, 'text-center')]//div[contains(@class, 'rounded-md')]"));
    private static final Locator TABLE_TRANSACTION_ROW = new Locator("TABLE_TRANSACTION_ROW", new By.ByXPath("//div[@data-testid='data-table']//tr"));
    private static final Locator TABLE_CHECKBOX = new Locator("TABLE_CHECKBOX", new By.ByXPath("//input[@type='checkbox']"));
    private static final Locator CALENDAR_POPUP = new Locator("CALENDAR_POPUP", new By.ByXPath("//div[contains(@class, 'calendar')]"));
    private static final Locator START_DATE = new Locator("START_DATE", new By.ByXPath("//button[@data-testid='field-startRequestedAt-trigger']//input"));
    private static final Locator END_DATE = new Locator("END_DATE", new By.ByXPath("//button[@data-testid='field-endRequestedAt-trigger']//input"));
    private static final Locator PREV_MONTH_FILTER_DATE = new Locator("PREV_MONTH_FILTER_DATE", new By.ByXPath("//button[contains(@class, 'prev_button')]"));
    private static final Locator NEXT_MONTH_FILTER_DATE = new Locator("NEXT_MONTH_FILTER_DATE", new By.ByXPath("//button[contains(@class, 'next_button')]"));
    private static final Locator DROPDOWN_FILTER = new Locator("DROPDOWN_FILTER", new By.ByXPath("//div[@data-radix-select-viewport]//div[@role='option']"));
    private static final Locator DESCRIPTION_MODAL = new Locator("DESCRIPTION_MODAL", new By.ByXPath("//div[@data-testid='confirmation-modal']"));
    private static final Locator TITLE_MODAL = new Locator("TITLE_MODAL", new By.ByXPath("//div[@class='modal-body']//h2"));
    private static final Locator COLLAPSIBLE_REFUND_METHOD = new Locator("COLLAPSIBLE_REFUND_METHOD", new By.ByXPath("//td/i[contains(@class,'tix-down')]"));
    private static final Locator CHECKBOX_SELECT_ALL_ROW_CLICKABLE = new Locator("CHECKBOX_SELECT_ALL_ROW_CLICKABLE", new By.ByXPath("//input[@data-testid='checkbox-header']/parent::div"));
    private static final Locator CHECKBOX_SELECT_ALL_ROW = new Locator("CHECKBOX_SELECT_ALL_ROW", new By.ByXPath("//input[@data-testid='checkbox-header']"));
    private static final Locator CONFIRMATION_MODAL = new Locator("CONFIRMATION_MODAL", By.xpath("//*[@data-testid='confirmation-modal']"));
    private Locator getRefundTransactionTab(String tabName) {
        return new Locator("TAB_REFUND_TRANSACTION", new By.ByXPath("//div[contains(@class, 'tab_list')]//span[text()='"+tabName+"']"));
    }
    private Locator getButton(String buttonName) {
        return new Locator("BUTTON_TRANSACTION", new By.ByXPath("//button[contains(normalize-space(.), '"+buttonName+"')]"));
    }

    private Locator getActionButton(String buttonName) {
        return new Locator("BUTTON_TRANSACTION", new By.ByXPath("//button[contains(@class,'btn-header-action')][text()='"+buttonName+"']"));
    }
    private Locator getDayOnFilterDate(String day) {
        return new Locator("DAY_ON_FILTER_DATE", new By.ByXPath("//div[contains(@class,'day-mod-body')][text()='"+day+"']"));
    }
    private Locator buttonSortColumn(String columnName) {
        return new Locator("SORT_COLUMN_"+columnName.toUpperCase(), new By.ByXPath("//span[text()='"+columnName+"']/following-sibling::i"));
    }
    private Locator getColumnValue(String columnName) {
        return new Locator("COLUMN_VALUE", new By.ByXPath("(//tbody/tr/td[count(//thead//th[.//div[translate(normalize-space(text()),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='"+columnName+"']]/preceding-sibling::th)+1]//p)[1]"));
    }
    private Locator getFilterInput(String filterName) {
        return new Locator("FILTER_INPUT_"+filterName, new By.ByXPath("//label[translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')= '"+filterName+"']/preceding-sibling::input"));
    }
    private Locator getFilterDropdown(String filterName) {
        return new Locator("FILTER_DROPDOWN_"+filterName, new By.ByXPath("//button[.//span[translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='"+filterName+"']]"));
    }
    private Locator getFilterDropdownOption(String option) {
        return new Locator("FILTER_DROPDOWN_OPTION", new By.ByXPath("//div[@data-testid='selection-values']//div[@role='option'][.//span[normalize-space()='"+option+"']]"));
    }
    private Locator getActionButtonRefundRequest(String action) {
        return new Locator("ACTION_REFUND_REQUEST", new By.ByXPath("//td[position()=count(//span[text()='Action']/ancestor::th/preceding-sibling::th)+1]//div[text()='"+action+"']/parent::div"));
    }
    private Locator getActionButtonRefundMethod(String action) {
        return new Locator("ACTION_REFUND_METHOD", new By.ByXPath("//td[position()=count(//th[text()='Action']/preceding-sibling::th)+1]//div[text()='"+action+"']/parent::div"));
    }
    private Locator getFlashAction(String flash) {
        return new Locator("FLASH_ACTION_"+flash.toUpperCase(), new By.ByXPath("//div[@class='flash "+flash+"']"));
    }
    private Locator dropDownDataPage(String dataPage) {
        return new Locator("DROP_DOWN_DATA_PER_PAGE", new By.ByXPath("//div[@role='option']//span[text()='"+dataPage+"']"));
    }
    public final Map<String, String> refundType = new HashMap<String, String>() {{
        put("erefund", "E-Refund");
        put("b2b-refund", "B2B Refund");
        put("middleware-refund", "Middleware Refund");
        put("reschedule", "Reschedule");
    }};
    private Locator selectDayMonthCalendar(String dateMonth) {
        return new Locator("SELECT_DATE_MONTH", new By.ByXPath("//button[contains(@class, 'day')]//span[contains(@aria-label,'"+ dateMonth +"')]"));
    };

    public RefundTransactionPage(Session<T> session) { super(session); }
    
    public List<RefundRequestDTO> getDataRefundDB (String query) {
        var db = TiketMysqlDBManager.getInstance("refund");
        try {
            List<RefundRequestDTO> dbExecute = ResultSetMapperFactory.getResultSetMapperIdentity().map(db.executeQuery(query),RefundRequestDTO.class);
            if (dbExecute.stream().count() < 1) {
                throw new RuntimeException("Data not found in Database!");
            }
            logger.log("Retrieved data -> "+dbExecute.toString());
            return dbExecute;
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public void goToRefundTransaction() {
        WebElement refundTransaction = driverTools.waitForElementToAppear(MENU_REFUND_TRANSACTION).orElseThrow();
        driverTools.click(refundTransaction);
        driverTools.waitForElementToDisappear(LOADING_TABLE_CONTENT);
    }

    public void goToRefundTransactionTab(String tabName) {
        WebElement tab = driverTools.waitForElementToAppear(getRefundTransactionTab(tabName)).orElseThrow();
        driverTools.scrollToElement(tab);
        driverTools.click(tab);
        driverTools.waitForElementToDisappear(LOADING_TABLE_CONTENT);
    }

    public void clickButton(String buttonName) {
        driverTools.dummyWait(1);
        WebElement searchButton = driverTools.waitForElementToAppear(getButton(buttonName)).orElseThrow();
        driverTools.scrollToElement(searchButton);
        driverTools.click(searchButton);
        driverTools.waitForElementToDisappear(LOADING_TABLE_CONTENT);
    }

    public void setTotalDataPerPage(String data) {
        driverTools.waitForElementToAppear(DATA_PER_PAGE_INPUT).orElseThrow();
        driverTools.scrollToElement(DATA_PER_PAGE_INPUT);
        driverTools.click(DATA_PER_PAGE_INPUT);
        driverTools.click(dropDownDataPage(data));
        driverTools.waitForElementToDisappear(LOADING_TABLE_CONTENT);
    }

    public String getTotalDisplayedData() {
        List<WebElement> totalDataList = driverTools.elements(TABLE_CHECKBOX).orElseThrow();
        String totalRow = String.valueOf(totalDataList.size()-1);
        logger.log("Total row displayed -> "+totalRow);
        return totalRow;
    }

    public void clickStartDateFilter() {
        WebElement startDate = driverTools.waitForElementToAppear(START_DATE).orElseThrow();
        driverTools.click(startDate);
    }

    public void setFilterDate(String startDate, String endDate, int monthDuration){
        driverTools.dummyWait(5);
        driverTools.click(START_DATE);
        driverTools.waitForElementsToAppear(CALENDAR_POPUP);
        for (int i=0; i<=monthDuration; i++) {
            if (driverTools.isElementPresent(selectDayMonthCalendar(startDate))) {
                    driverTools.click(selectDayMonthCalendar(startDate));
                    break;
                }
            driverTools.click(PREV_MONTH_FILTER_DATE);
            }
        driverTools.waitForElementToDisappear(CALENDAR_POPUP);
        driverTools.waitForElementToAppear(END_DATE);
        driverTools.click(END_DATE);
        driverTools.waitForElementsToAppear(CALENDAR_POPUP);
        for (int i=0; i<=monthDuration-1; i++) {
            if (driverTools.isElementPresent(selectDayMonthCalendar(endDate))) {
                driverTools.click(selectDayMonthCalendar(endDate));
                break;
                }
            driverTools.click(NEXT_MONTH_FILTER_DATE);
            }
        driverTools.waitForElementToDisappear(CALENDAR_POPUP);
    }

    public boolean verifyFilterDate(String startDate, String endDate) {
        //get filtered request date
        DateTimeFormatter createdFormatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy",Locale.ENGLISH);
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);

        LocalDate startDateReq = LocalDate.parse(startDate, inputFormatter);
        LocalDate endDateReq = LocalDate.parse(endDate, inputFormatter);

        //get correct date format from refund detail
        String regex = "\\w{3}, \\d{1,2} \\w{3} \\d{4}";
        Pattern pattern = Pattern.compile(regex);

        List<WebElement> requestDateRowList = driverTools.waitForElementsToAppear(getColumnValue("request date")).orElseThrow();
        for (WebElement requestDateRow : requestDateRowList) {
            String requestDate = driverTools.getText(requestDateRow);
            Matcher matcher = pattern.matcher(requestDate);
            if (matcher.find()) {
                requestDate = matcher.group();
            }
            LocalDate requestDateLocal = LocalDate.parse(requestDate, createdFormatter);
            int actualToStart = requestDateLocal.compareTo(startDateReq);
            int actualToEnd = requestDateLocal.compareTo(endDateReq);
            if ((actualToStart < 0) || (actualToEnd > 0)) {
                logger.log("date not in range: "+requestDate);
                return false;
            }
        }
        return true;
    }

    public void inputFilter(String filterName, String value) {
        WebElement filter = driverTools.waitForElementToBeClickable(getFilterInput(filterName)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(filter);
        driverTools.clear(filter);
        driverTools.sendKeys(filter, value);
        logger.log("Set filter: "+filterName+" with this value: "+value);
    }

    public String selectFilterDropdownByKey(String filterName, String key) {
        WebElement filter = driverTools.waitForElementToAppear(getFilterDropdown(filterName)).orElseThrow();
        driverTools.click(filter);
        driverTools.waitForElementToAppear(DROPDOWN_FILTER, 6).orElseThrow();
        List<WebElement> dropdown = driverTools.elements(DROPDOWN_FILTER).orElseThrow();
        int index = Integer.parseInt(key);
        String option = driverTools.getText(dropdown.get(index));
        driverTools.click(dropdown.get(index));
        logger.log("Set filter: "+filterName+" with this dropdown value: "+option);
        return option;
    }

    public void selectFilterDropdownByOption(String filterName, String option) {
        WebElement filter = driverTools.waitForElementToAppear(getFilterDropdown(filterName)).orElseThrow();
        driverTools.click(filter);
        WebElement optionDropdown = driverTools.waitForElementToAppear(getFilterDropdownOption(option)).orElseThrow();
        driverTools.click(optionDropdown);
        logger.log("Set filter: "+filterName+" with this dropdown value: "+option);
    }

    public String getFilterDataFromDTO(RefundRequestDTO result, String dataName) {
        switch (dataName.trim().toLowerCase()) {
            case "refund request id":
                return String.valueOf(result.getId());
            case "order id":
                return String.valueOf(result.getReference_order_id());
            case "order detail id":
                return String.valueOf(result.getReference_order_detail_id());
            default:
                return "";
        }
    }

    public boolean verifyRowValueOfColumn(String columnName, String expected) {
        expected = expected.trim();
        if ("manual checking".equalsIgnoreCase(columnName)
            && "invalid".equalsIgnoreCase(expected)) {
                expected = "-";
        }
        if ("protect product name".equalsIgnoreCase(columnName)) {
            switch (expected.toLowerCase()) {
                case "100% refund":
                    expected = "100pRefund";
                    break;
    
                case "100% refund & reschedule":
                    expected = "100pReschedule";
                    break;

                case "ttdcancellation":
                    expected = "TTDCancellation";
                    break;
            }
        }
        if ("refund type".equalsIgnoreCase(columnName)) {
            switch (expected.toLowerCase()) {
                case "e-refund":
                    expected = "erefund";
                    break;
    
                case "b2b-refund":
                    expected = "b2b-refund";
                    break;

                case "middleware-refund":
                    expected = "middleware-refund";
                    break;

                case "reschedule":
                    expected = "reschedule";
                    break;
            }
        }
        driverTools.dummyWait(2);
        List<WebElement> rowsValue = driverTools.waitForElementsToAppear(getColumnValue(columnName)).orElseThrow();
        List<WebElement> paymentMethods = driverTools.waitForElementsToAppear(getColumnValue("payment method")).orElseThrow();
        for (int i=0; i<rowsValue.size(); i++) {
            String value = driverTools.getText(rowsValue.get(i)).trim();
            if (columnName.equals("refund method")) {
                if (!value.contains(expected)) {
                    String paymentMethod = driverTools.getText(paymentMethods.get(i));
                    if (value.equals(paymentMethod)) { //make isCorrect true if refund != payment method
                        Assertion.fail("Expected refund method: "+expected+" not same with actual: "+value+". But actual refund same as payment method: "+paymentMethod);
                    }
                }
            } else {
                if (!value.equals(expected)) {
                    Assertion.fail("Column name: "+columnName+", Expected value: "+expected+" is not same with actual value: "+value);
                }
            }
        }
        return true;
    }

    public void openDropdownRefundMethod() {
        String classValue = driverTools.getValueAttribute(COLLAPSIBLE_REFUND_METHOD, "class");
        if (classValue.contains("tixicon-down")) {
            driverTools.click(COLLAPSIBLE_REFUND_METHOD);
        }
    }

    public void clickActionButton(String action, boolean actionPerMethod) {
        if (actionPerMethod) {
            openDropdownRefundMethod();
            driverTools.click(getActionButtonRefundMethod(action));
        } else {
            driverTools.click(getActionButtonRefundRequest(action));
        }
        if (driverTools.waitForElementToBeClickable(CONFIRMATION_MODAL, 2).isEmpty()) {
            if (actionPerMethod) {
                driverTools.click(getActionButtonRefundMethod(action));
            } else {
                driverTools.click(getActionButtonRefundRequest(action));
            }
        }
    }
    
    public void clickActionButtonBulk(String action) {
        driverTools.click(driverTools.waitForElementToAppear(getActionButton(action)).orElseThrow());
    }
    
    public void selectAllRow() {
        WebElement checkbox = driverTools.waitForElementToAppear(CHECKBOX_SELECT_ALL_ROW_CLICKABLE).orElseThrow();
        driverTools.click(checkbox);
    }

    public boolean verifyDoAction(String action, String description, String dataCount) {
        String titleModal = driverTools.getText(driverTools.waitForElementToAppear(TITLE_MODAL, 2).orElseThrow());
        String descModal = driverTools.getText(driverTools.waitForElementToAppear(DESCRIPTION_MODAL, 2).orElseThrow());
        
        String title = capitalize(action)+" data refund";
        description = description.replaceAll("x", dataCount);
        if (action.equals("Submit")) description = description.replaceAll("report", "on process");
        if (!titleModal.equals(title)) {
            logger.fail("Title modal ("+titleModal+") is not same with expected title ("+title+")");
            return false;
        }
        if (!descModal.equals(description)) {
            logger.fail("Description modal ("+descModal+") is not same with expected description ("+description+")");
            return false;
        }
        return true;
    }

    public boolean verifyActionButtonsNotShown() {
        return driverTools.waitForElementToAppear(getColumnValue("Action"), 1).isEmpty();
    }

    public boolean verifySelectAllRowDisabled() {
        WebElement checkbox = driverTools.element(CHECKBOX_SELECT_ALL_ROW).orElseThrow();
        Object disabled = driverTools.getValueAttribute(checkbox, "disabled");
        return !disabled.equals(nullValue());
    }

    public boolean verifyFlashAction(String expectedMessage) {
        Optional<WebElement> flashSuccess = driverTools.waitForElementToAppear(getFlashAction("success"), 3);
        if (flashSuccess.isEmpty()) {
            logger.log("Flash success not shown");
            return false;
        }
        String message = driverTools.getText(flashSuccess.get());
        if (!message.equals(expectedMessage)) {
            logger.fail("Flash message -> "+message+", is not same with expected -> "+expectedMessage);
            return false;
        }
        return true;
    }
}
