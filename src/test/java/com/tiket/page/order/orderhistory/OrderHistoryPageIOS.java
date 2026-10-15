package com.tiket.page.order.orderhistory;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class OrderHistoryPageIOS <T extends WebDriver> extends OrderHistoryPage<T>{

    public OrderHistoryPageIOS(Session<T> session) {
        super(session);
    }
    private static final Locator PAST_REFUND_BTN = new Locator("PAST_REFUND_BTN", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Past Refund' or @label='Refund Lama']"));
    private static final Locator PRODUCT_NAME = new Locator("PRODUCT_NAME", AppiumBy.xpath("//*[@name='product_name' and @visible='true']"));
    private static final Locator ORDERS = new Locator("ORDERS", AppiumBy.xpath("//XCUIElementTypeTable[@visible='true']//XCUIElementTypeCell//*[contains(@label,':')]"));
    private static final Locator ORDER_ID = new Locator("ORDER_ID", AppiumBy.name("order_id"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", AppiumBy.xpath("(//*[contains(@label,'Delete') or contains(@label,'Hapus')])[last()]"));
    private static final Locator CONFIRM_DELETE = new Locator("CONFIRM_DELETE", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Ya') or contains(@name,'Yes')]"));
    private static final Locator BACK = new Locator("BACK", AppiumBy.id("tds ic back"));
    private static final Locator HISTORY_ICON = new Locator("HISTORY_ICON", AppiumBy.id("ic history"));
    private static final Locator FILTER = new Locator("FILTER", AppiumBy.name("tds_ic_filter"));
    private static final Locator APPLY_FILTER = new Locator("APPLY_FILTER", AppiumBy.xpath("//*[@name='Apply' or @name='Terapkan' and @type='XCUIElementTypeButton']"));
    private static final Locator BACK_BTN = new Locator("BACK_BTN", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name=\"Order ID\"]//parent::*//preceding::*)[2]"));
    private static final Locator SORT_BUTTON = new Locator("SORT_BUTTON", AppiumBy.id("ic-sort"));
    private static final Locator SAVE_FILTER = new Locator("SAVE_FILTER", AppiumBy.xpath("//XCUIElementTypeButton[@label=\"Apply\" or @label=\"Terapkan\"]"));
    private static final Locator FINISHED_ORDER = new Locator("FINISHED_ORDER", AppiumBy.xpath("(//XCUIElementTypeStaticText[@label=\"Finished\" or @label=\"Selesai\"])[1]"));
    private static final Locator RESCHEDULE_ORDER = new Locator("RESCHEDULE_ORDER", AppiumBy.xpath("(//XCUIElementTypeStaticText[@label=\"Di-reschedule\" or @label=\"Rescheduled\"])[1]"));
    private static final Locator REFUND_COMPLETED_ORDER = new Locator("REFUND_COMPLETED_ORDER", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='order_status' and (@label='Refund has completed' or @label='Refund selesai')]"));
    private static final Locator REFUND_REJECTED_ORDER = new Locator("REFUND_REJECTED_ORDER", AppiumBy.xpath("(//XCUIElementTypeStaticText[@label=\"Refund request is rejected\" or @label=\"Refund ditolak\"])[1]"));
    private static final Locator SORT_CLOSEST_DATE = new Locator("SORT_CLOSEST_DATE", AppiumBy.xpath("//XCUIElementTypeStaticText[@label=\"Closest date\" or @label=\"Tanggal terdekat\"]"));
    private static final Locator GREEN_DOTS = new Locator("GREEN_DOTS", AppiumBy.xpath("(//XCUIElementTypeImage[@name=\"ic-filter-badge\"])"));
    private static final Locator PAST_REFUND_TAB = new Locator("PAST_REFUND_TAB", AppiumBy.xpath("(//*[contains(@name,'textLabel')])[2]"));
    private static final Locator BACK_BTN_ORDER_AND_REFUND_HISTORY = new Locator("BACK_BTN_ORDER_AND_REFUND_HISTORY", AppiumBy.name("back"));
    private static final Locator ORDER_STATUS = new Locator("ORDER_STATUS", AppiumBy.name("order_status"));
    private static final Locator SET_PERIOD = new Locator("SET_PERIOD", AppiumBy.xpath("//*[contains(@label,'period') and @visible='true']"));
    private static final Locator PERIOD_START = new Locator("PERIOD_START", AppiumBy.xpath("//*[contains(@label,'period') and @visible='true']//following-sibling::XCUIElementTypeStaticText[last()-1]"));
    private static final Locator YEAR = new Locator("YEAR", AppiumBy.xpath("(//XCUIElementTypeTable)[last()]"));
    private static final Locator MONTH = new Locator("MONTH", AppiumBy.xpath("(//XCUIElementTypeTable)[last()-1]"));
    private static final Locator DONE_PERIOD = new Locator("DONE_PERIOD", AppiumBy.xpath("//XCUIElementTypeButton[@label='Done' or @label='Selesai']"));
    private static final Locator CANCELLED_ORDER = new Locator("CANCELLED_ORDER", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='order_status' and (@label='Dibatalkan' or @label='Canceled')]"));


    private Locator refundStatus(String currency) {
        return new Locator("REFUND_STATUS_" + currency.toUpperCase(), AppiumBy.xpath("//XCUIElementTypeCell//XCUIElementTypeStaticText[(contains(@label,'Refund has completed') or contains(@label,'Refund selesai')) and (./preceding-sibling::* | ./following-sibling::*)[contains(@label,'" + currency + "')]]"));
    }

    private Locator refundId(String currency) {
        return new Locator("REFUND_ID_" + currency.toUpperCase(), AppiumBy.xpath("//XCUIElementTypeCell[*[contains(@label,'" + currency + "')]]//*[contains(@name,'refund_id')]"));
    }

    private Locator refundProductName(String currency) {
        return new Locator("REFUND_PRODUCT_NAME_" + currency.toUpperCase(), AppiumBy.xpath("//XCUIElementTypeCell[*[contains(@label,'" + currency + "')]]//*[contains(@name,'product_name')]"));
    }

    private Locator refundReceiveDate(String currency) {
        return new Locator("REFUND_RECEIVE_DATE_" + currency.toUpperCase(), AppiumBy.xpath("//XCUIElementTypeCell[*[contains(@label,'" + currency + "')]]//*[contains(@name,'refund_description') and not(contains(@label,'"+currency+"'))]"));
    }

    private Locator totalRefundAmount(String currency) {
        return new Locator("TOTAL_REFUND_AMOUNT_" + currency.toUpperCase(), AppiumBy.xpath("//XCUIElementTypeCell[*[contains(@label,'" + currency + "')]]//*[contains(@name,'refund_description') and contains(@label,'"+currency+"')]"));
    }

    @Override
    public String getRefundId(String currency) {
        String refundId = driverTools.getText(refundId(currency)).orElseThrow();
        return refundId.replace("Refund ID: ", "");
    }

    @Override
    public String getRefundProductName(String currency) {
        return driverTools.getText(refundProductName(currency)).orElseThrow();
    }

    @Override
    public String getRefundReceiveDate(String currency) {
        String theDate = driverTools.getText(refundReceiveDate(currency)).orElseThrow();
        String[] dateSplit = theDate.split(" ");
        String dateOnly = dateSplit[0];
        String updateDate;
        if (dateOnly.charAt(0) == '0') {
            updateDate = Character.toString(dateOnly.charAt(1));
        } else {
            updateDate = dateOnly;
        }
        dateOnly = dateOnly.replace(dateOnly, updateDate);
        String[] newDate = new String[]{dateOnly, dateSplit[1], dateSplit[2]};
        theDate = String.join(" ", newDate);
        logger.log("theDate -> " + theDate);
        return theDate;
    }

    @Override
    public String getTotalRefund(String currency) {
        return driverTools.getText(totalRefundAmount(currency)).orElseThrow();
    }

    @Override
    public String getRefundStatus(String currency) {
        driverTools.swipeToElement(refundStatus(currency), Swipe.UP, 10).orElseThrow();
        return driverTools.getText(refundStatus(currency)).orElseThrow();
    }

    @Override
    public void selectRefundItem(String currency) {
        driverTools.click(totalRefundAmount(currency));
    }

    public boolean verifyPastOrderList(){
        driverTools.waitForElementToPresence(PRODUCT_NAME,20).orElseThrow();
        List<String> pastOrderList = driverTools.getTexts(PRODUCT_NAME);
        logger.log("past order list: "+ pastOrderList);
        return !pastOrderList.isEmpty();
    }

    public boolean verifyPastRefundList(String Case){
        switch (Case) {
            case "login":
                WebElement pastRefundBtn = driverTools.waitForElementToAppear(PAST_REFUND_BTN).orElseThrow();
                driverTools.click(pastRefundBtn);

                driverTools.waitForElementToAppear(PRODUCT_NAME).orElseThrow();
                List<WebElement> pastRefundList = driverTools.elements(PRODUCT_NAME).orElseThrow();
                logger.log("Past refund list length: " + pastRefundList.size());
                return !pastRefundList.isEmpty();
            case "guest":
                pastRefundBtn = driverTools.waitForElementToAppear(PAST_REFUND_BTN).orElseThrow();
                return pastRefundBtn.isDisplayed();
            default:
                logger.log("Invalid case provided: " + Case);
                return false;
        }
    }

    private Locator getFilter(String filter){
        switch (filter.toLowerCase()){
            case "car rentals":
                return new Locator(filter,AppiumBy.xpath("//*[@name='Car Rentals' or @name='Sewa Mobil']"));
            case "this month":
                return new Locator(filter,AppiumBy.xpath("//*[@name='This month' or @name='Bulan ini']"));
            default:
                throw new NotImplementedException("Unimplemented filter type!");
        }
    }

    private void applyFilter(String testTypeFilter, String durationTypeFilter){
        driverTools.click(FILTER);
        driverTools.click(getFilter(testTypeFilter));
        WebElement duration = driverTools.swipeToElement(getFilter(durationTypeFilter),Swipe.UP,5).orElseThrow();
        driverTools.clickElementByCoordinate(duration);
        WebElement applyBtn = driverTools.swipeToElement(APPLY_FILTER, Swipe.UP,3).orElseThrow();
        driverTools.click(applyBtn);
    }

    private Locator statusLocator(int index){
        return new Locator("STATUS LOCATOR", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@label,'Rescheduled') or contains(@label,'Dibatalkan') or contains(@label,'Di-reschedule') or contains(@label,'Canceled')])["+index+"]"));
    }
    private int findOrderOnHistoryPage(String orderId, String testTypeFilter, String durationTypeFilter) {
        driverTools.waitForElementToAppear(FILTER).orElseThrow();
        applyFilter(testTypeFilter, durationTypeFilter);
        boolean findMyOrder = false;
        int i = 1;
        while (!findMyOrder && i <= 3) {
            driverTools.waitForElementToAppear(statusLocator(i)).orElseThrow();
            driverTools.click(statusLocator(i)); //got error  https://www.selenium.dev/documentation/webdriver/troubleshooting/errors#stale-element-reference-exception
            WebElement orderWebElement = driverTools.waitForElementToAppear(ORDER_ID).orElseThrow();
            String actualOrderId = orderWebElement.getText().replaceAll("\\D", "").trim();
            logger.log("--order id FROM PAGE --" + actualOrderId);
            logger.log("--order id FROM PARAMETER --" + orderId);
            findMyOrder = actualOrderId.equals(orderId.replaceAll("\\D", "").trim());
            logger.log("Order->" + i + " status: " + findMyOrder);
            driverTools.click(BACK_BTN);
            i++;
        }

        logger.log("Order Found " + findMyOrder);
        return !findMyOrder ? -1 : i - 1;
    }

    @Override
    public boolean verifyCancelStatusOnOrderHistoryPage(String orderId, String testTypeFilter, String durationTypeFilter){
        driverTools.swipeDown();
        boolean checkStatus = false;
        int pos = findOrderOnHistoryPage(orderId, testTypeFilter, durationTypeFilter);
        if(pos != -1){
            WebElement statusLoc = driverTools.waitForElementToAppear(statusLocator(pos)).orElseThrow();
            checkStatus = ("Dibatalkan").equals(statusLoc.getAttribute("value")) | ("Canceled").equals(statusLoc.getAttribute("value"));
        }
        return checkStatus;

    }
    public void deleteOrder(String orderId){
        int pos = findOrderOnHistoryPage(orderId, "car rentals", "This month");
        driverTools.click(statusLocator(pos));
        driverTools.click(DELETE_BUTTON);
        driverTools.waitForElementToAppear(DELETE_BUTTON).ifPresent(WebElement::click);
    }

    @Override
    public boolean isOrderDeleted(String orderId) {
        if (driverTools.waitForElementToAppear(HISTORY_ICON).isPresent()) {
            driverTools.click(HISTORY_ICON);
        }
        driverTools.waitForElementToAppear(FILTER);
        for (int x = 5; x >= 0; x--) {
            driverTools.swipeDown();
            if (driverTools.waitForElementToAppear(statusLocator(1)).isPresent()) {
                driverTools.click(statusLocator(1));
                driverTools.waitForElementToAppear(ORDER_ID).orElseThrow();
                if (driverTools.getText(ORDER_ID).orElseThrow().contains(orderId.replaceAll("\\D", "").trim())) {
                    driverTools.click(BACK);
                } else{
                    return true;
                }
            } else{
                return true;
            }
        }
        return false;
    }

    @Override
    public void clickFilter() {
        driverTools.waitForElementToPresence(FILTER).orElseThrow();
        driverTools.click(FILTER);
    }

    @Override
    public void selectFilterVertical(String vertical, String lang){
        WebElement filter = driverTools.waitForElementToPresence(getVerticalFilter(vertical, lang)).orElseThrow();
        driverTools.clickElementByCoordinate(filter);
    }

    @Override
    public void setPeriod(){
        WebElement setPeriod = driverTools.swipeToPresentedElement(SET_PERIOD, Swipe.UP, 5).orElseThrow();
        driverTools.click(setPeriod);
        clickApplyFilter();
        clickFilter();
        WebElement periodStart = driverTools.swipeToPresentedElement(PERIOD_START, Swipe.UP, 10).orElseThrow();
        driverTools.click(periodStart);
        WebElement month = driverTools.waitForElementToPresence(MONTH).orElseThrow();
        driverTools.swipeByDistance(month,Swipe.DOWN,0.5);
        driverTools.click(DONE_PERIOD);
    }

    @Override
    public void clickApplyFilter(){
        driverTools.click(APPLY_FILTER);
        driverTools.waitForElementToAppear(PRODUCT_NAME);
    }

    private Locator getVerticalFilter(String vertical, String lang){
        if(lang.equals("id")){
            vertical = CoreFunctions.verticalEngToId(vertical);
        }
        return new Locator("VERTICAL_FILTER", AppiumBy.xpath("//*[contains(@label,'"+vertical+"')]//following-sibling::*[contains(@name,'checkbox')]"));
    }

    @Override
    public Boolean isFinishedOrderShown() {
        return driverTools.swipeToElement(FINISHED_ORDER,Swipe.UP,10).isPresent();

    }
    @Override
    public Boolean isRescheduleOrderShown() {
        return driverTools.swipeToElement(RESCHEDULE_ORDER,Swipe.UP,10).isPresent();
    }

    @Override
    public Boolean isRefundCompletedOrderShown() {
        return driverTools.swipeToElement(REFUND_COMPLETED_ORDER,Swipe.UP,10).isPresent();
    }

    @Override
    public void applySort() {
        driverTools.click(SORT_BUTTON);
        driverTools.click(SORT_CLOSEST_DATE);
        driverTools.waitForElementToAppear(PRODUCT_NAME);
    }

    @Override
    public Boolean bothSortAndFilterApplied() {
        List<WebElement> greenDots = driverTools.elements(GREEN_DOTS).orElseThrow();
        return greenDots.size()==2;
    }

    @Override
    public void openPastRefund(){
        driverTools.click(PAST_REFUND_BTN);
    }

    @Override
    public String getPastRefundCurrency(String refundId){
        WebElement pricePastRefundElement = driverTools.waitForElementToAppear(getPriceOfPastRefund(refundId)).orElseThrow();
        String currency = getCurrency(driverTools.getText(pricePastRefundElement));
        logger.log("currency: " + currency);
        return currency;
    }

    @Override
    public String getPastRefundId(int index){
        WebElement refundIdElement = driverTools.waitForElementToAppear(getRefundID(index)).orElseThrow();
        String refundId = driverTools.getText(refundIdElement);
        logger.log("refundId: " + refundId);
        return refundId;
    }

    private Locator getRefundID(int index){
        return new Locator("PAST_REFUND_ID_"+index, AppiumBy.xpath("(//*[contains(@name,'refund_id')])["+index+"]"));
    }

    private Locator getRefundID(String refundID){
        return new Locator("PAST_REFUND_ID_" + refundID, AppiumBy.xpath("//*[contains(@name,'refund_id') and contains(@label,'"+refundID+"')]"));
    }

    private Locator getPriceOfPastRefund(String refundId){
        return new Locator("PRICE_PAST_REFUND", AppiumBy.xpath("(//*[contains(@name,'refund_id') and contains(@label,'"+refundId+"')]//preceding-sibling::*[contains(@name,'refund_description')])[2]"));
    }

    private Locator getPastRefundProductName(String refundID){
        return new Locator("PAST_REFUND_PRODUCT_NAME_" + refundID, AppiumBy.xpath("//*[contains(@name,'refund_id') and contains(@label,'"+refundID+"')]//preceding-sibling::*[contains(@name,'product_name')]"));
    }

    private Locator getPastRefundStatus(String refundID){
        return new Locator("PAST_REFUND_STATUS_" + refundID, AppiumBy.xpath("//*[contains(@name,'refund_id') and contains(@label,'"+refundID+"')]//preceding-sibling::*[contains(@name,'refund_status')]"));
    }

    private Locator getPastRefundDate(String refundID){
        return new Locator("PAST_REFUND_STATUS_" + refundID, AppiumBy.xpath("(//*[contains(@name,'refund_id') and contains(@label,'"+refundID+"')]//preceding-sibling::*[contains(@name,'refund_description')])[1]"));
    }

    @Override
    public void swipeToPastRefund(String pastRefundId){
        driverTools.swipeToElement(getRefundID(pastRefundId),Swipe.UP,10);
    }

    @Override
    public String getPastRefundOrderTitle(String refundId){
        WebElement orderTitleElement = driverTools.waitForElementToAppear(getPastRefundProductName(refundId)).orElseThrow();
        String orderTitle = driverTools.getText(orderTitleElement);
        logger.log("orderTitle: " + orderTitle);
        return orderTitle;
    }

    @Override
    public boolean verifyPastRefundCard(String refundId,String expectedCurrency){
        boolean isRefundIdDisplayed = driverTools.isElementDisplayed(getRefundID(refundId));
        logger.log("isRefundIdDisplayed: " + isRefundIdDisplayed);
        Assertion.assertThat("refund id displayed ",isRefundIdDisplayed,is(true));

        boolean isProductNameDisplayed = driverTools.isElementDisplayed(getPastRefundProductName(refundId));
        logger.log("isProductNameDisplayed: " + isProductNameDisplayed);
        Assertion.assertThat("refund product name displayed ",isProductNameDisplayed,is(true));

        WebElement refundStatusElement = driverTools.waitForElementToAppear(getPastRefundStatus(refundId)).orElseThrow();
        String refundStatus = driverTools.getText(refundStatusElement);
        logger.log("refundStatus: " + refundStatus);
        Assertion.assertThat("refund product name displayed ",refundStatus, is(either(containsString("Completed")).or(containsString("Selesai"))));

        WebElement refundDateElement = driverTools.waitForElementToAppear(getPastRefundDate(refundId)).orElseThrow();
        String refundDate = driverTools.getText(refundDateElement);
        logger.log("refundDate: " + refundDate);
        Assertion.assertThat("is refund date more then 24 hours", CoreFunctions.isMoreThan24Hours(refundDate),is(true));

        String currentCurrency = getPastRefundCurrency(refundId);
        logger.log("current currency : " + currentCurrency);
        Assertion.assertThat("is current currency is same as expected currency",currentCurrency,equalTo(expectedCurrency));
        return true;
    }

    @Override
    public void openPastRefundDetail(String refundId){
        driverTools.click(getRefundID(refundId));
    }

    @Override
    public boolean isRedirectedToCorrectPage(){
        WebElement orderStatusElement;
        try {
          orderStatusElement = driverTools.waitForElementToAppear(ORDER_STATUS).orElseThrow();
        } catch (NoSuchElementException e) {
          logger.softWarning("isRedirectedToCorrectPage: order status not shown after back click, retrying navigation");
          clickBackBtnOnRefundDetailPage();
          orderStatusElement = driverTools.waitForElementToAppear(ORDER_STATUS).orElseThrow();
        }
        boolean isOrderStatusDisplayed = driverTools.getValueAttribute(orderStatusElement,"visible").contains("true");
        logger.log("isOrderStatusDisplayed: " + isOrderStatusDisplayed);
        return isOrderStatusDisplayed;
    }

    @Override
    public Boolean isCancelledOrderShown(){
        return driverTools.swipeToElement(CANCELLED_ORDER,Swipe.UP,10).isPresent();
    }

    @Override
    public boolean isTwelveMonthsFilterSelected() {
        throw new NotImplementedException("Not implemented for iOS");
    }

    @Override
    public void clickSeePreviousBookings() {
        throw new NotImplementedException("Not implemented for iOS");
    }

    @Override
    public boolean isFilterTabOpened() {
        throw new NotImplementedException("Not implemented for iOS");
    }
}
