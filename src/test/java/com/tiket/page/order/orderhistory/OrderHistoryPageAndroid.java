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

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class OrderHistoryPageAndroid<T extends WebDriver> extends OrderHistoryPage<T> {

    private static final Locator PAST_REFUND_BTN = new Locator("PAST_REFUND_BTN", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tab_layout') and (contains(@text,'Past Refund') or contains(@text,'Refund Lama') )]"));
    private static final Locator PRODUCT_NAME = new Locator("PRODUCT_NAME", AppiumBy.id("tv_title"));
    private static final Locator CHEVRON_ARROW_BTN_PAST_REFUND = new Locator("CHEVRON_ARROW_BTN_PAST_REFUND", AppiumBy.xpath("//*[contains(@resource-id,'tv_header')]//following-sibling::*[contains(@resource-id,'iv_chevron_right')]"));
    private static final Locator FILTER = new Locator("FILTER", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tv') and (contains(@text,'Filter'))]"));
    private static final Locator SORT_BUTTON = new Locator("SORT_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tv') and contains(@text,'Sort')  or contains(@text,'Urutkan')]"));
    private static final Locator SORT_CLOSEST_DATE = new Locator("SORT_CLOSEST_DATE", AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_list') and contains(@text,'Closest date') or contains(@text,'Tanggal perjalanan terdekat')]"));
    private static final Locator APPLY_FILTER = new Locator("APPLY_FILTER", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Terapkan') or contains(@text,'Apply'))]"));
    private static final Locator FILTER_RECYCLER_VIEW = new Locator("FILTER_RECYCLER_VIEW", AppiumBy.xpath("//android.widget.ScrollView[contains(@resource-id,'sv_container')]"));
    private static final Locator FILTER_OVERLAY = new Locator("FILTER_OVERLAY", AppiumBy.xpath("//*[contains(@resource-id,'fl_bottomsheet_content')]"));
    private static final Locator TWELVE_MONTHS_FILTER_SELECTED = new Locator("TWELVE_MONTHS_FILTER_SELECTED", AppiumBy.xpath("//*[contains(@resource-id,'tds_txt_header_info_view') and (contains(@text,'last 12 months') or contains(@text,'dalam 12 bulan'))]"));
    private static final Locator SEE_PREVIOUS_BOOKINGS_BTN = new Locator("SEE_PREVIOUS_BOOKINGS_BTN", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view')]"));
    private static final Locator GREEN_DOTS = new Locator("GREEN_DOTS", AppiumBy.id("tds_badge"));
    private static final Locator ORDER_ID = new Locator("ORDER_ID", AppiumBy.xpath("//*[contains(text(),'Order ID')]"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", AppiumBy.xpath("//*[contains(@class,'Button_variant_danger')]"));
    private static final Locator YES_DELETE = new Locator("YES_DELETE", AppiumBy.xpath("//*[contains(@class,'dialog')]//*[contains(@class,'Button_variant_danger')]"));
    private static final Locator NO_ORDERS = new Locator("NO_ORDERS", AppiumBy.xpath("//*[contains(@resource-id,'tds_txt_header_info_view') and (contains(@text,'No orders in 90 days') or contains(@text,'Tidak ada pesanan dalam 90 hari '))]"));
    private static final Locator SET_PERIOD = new Locator("SET_PERIOD", AppiumBy.xpath("(//*[contains(@resource-id,'v_radio_button')])[4]"));
    private static final Locator PERIOD_START = new Locator("PERIOD_START", AppiumBy.id("ll_container_start_date"));
    private static final Locator YEAR = new Locator("YEAR", AppiumBy.xpath("(//*[contains(@resource-id,'tds_rv_right_picker')])[last()]"));
    private static final Locator DONE_PERIOD = new Locator("DONE_PERIOD", AppiumBy.id("tds_btn_submit_picker"));
    private static final Locator CANCELLED_ORDER = new Locator("CANCELLED_ORDER", AppiumBy.xpath("//*[contains(@resource-id,'tv_status') and (@text='Canceled' or @text='Dibatalkan')]"));
    private static final Locator FINISHED_ORDER = new Locator("FINISHED_ORDER", AppiumBy.xpath("//*[contains(@resource-id,'tv_status') and (@text='Finished' or @text='Selesai')]"));
    private static final Locator RESCHEDULE_ORDER = new Locator("RESCHEDULE_ORDER", AppiumBy.xpath("//*[contains(@resource-id,'tv_status') and (@text='Di-reschedule' or @text='Rescheduled')]"));
    private static final Locator REFUND_COMPLETED_ORDER = new Locator("REFUND_COMPLETED_ORDER", AppiumBy.xpath("//*[contains(@resource-id,'tv_status') and (@text='Refund has completed' or @text='Refund selesai')]"));
    public OrderHistoryPageAndroid(Session<T> session) {
        super(session);
    }

    private Locator refundId(String currency) {
        return new Locator("REFUND_ID_" + currency.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'" + currency + "')]//parent::*[contains(@class,'ViewGroup')]//parent::*[contains(@resource-id,'fbl_subtitle')]//parent::*[contains(@class,'ViewGroup')]//parent::*[contains(@resource-id,'ll_detail_content')]//preceding-sibling::*[contains(@resource-id,'tv_header')]"));
    }

    private Locator refundProductName(String currency) {
        return new Locator("REFUND_PRODUCT_NAME_" + currency.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'" + currency + "')]//parent::*[contains(@class,'ViewGroup')]//parent::*[contains(@resource-id,'fbl_subtitle')]//parent::*[contains(@class,'ViewGroup')]//parent::*[contains(@resource-id,'ll_detail_content')]//preceding-sibling::*[contains(@resource-id,'iv_icon')]//following-sibling::*[contains(@resource-id,'tv_title')]"));
    }

    private Locator refundReceiveDate(String currency) {
        return new Locator("REFUND_RECEIVE_DATE_" + currency.toUpperCase(), AppiumBy.xpath("(//*[contains(@resource-id,'tv_title') and contains(@text,'" + currency + "')]//parent::*[contains(@class,'ViewGroup')]//parent::*[contains(@resource-id,'fbl_subtitle')]//parent::*[contains(@class,'ViewGroup')]//parent::*[contains(@resource-id,'ll_detail_content')]//*[contains(@resource-id,'tv_title') and (contains(@text,'Dana') or contains(@text,'dana') or contains(@text,'received'))])[1]//following-sibling::*[contains(@resource-id,'fbl_subtitle')]//*[contains(@resource-id,'tv_title')]"));
    }

    private Locator totalRefundAmount(String currency) {
        return new Locator("TOTAL_REFUND_AMOUNT_" + currency.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Total') or contains(@text,'total'))]//following-sibling::*[contains(@resource-id,'fbl_subtitle')]//*[contains(@resource-id,'tv_title') and contains(@text,'" + currency + "')]"));
    }

    private Locator refundStatus(String currency) {
        return new Locator("REFUND_STATUS_" + currency.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'" + currency + "')]//parent::*[contains(@class,'ViewGroup')]//parent::*[contains(@resource-id,'fbl_subtitle')]//parent::*[contains(@class,'ViewGroup')]//parent::*[contains(@resource-id,'ll_detail_content')]//following-sibling::*[contains(@resource-id,'tv_status')]"));
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

    public boolean verifyPastOrderList() {
        driverTools.swipeDown();
        driverTools.waitForElementToAppear(PRODUCT_NAME).orElseThrow();
        List<WebElement> pastOrderList = driverTools.elements(PRODUCT_NAME).orElseThrow();
        logger.log("past order list length: " + pastOrderList.size());
        return !pastOrderList.isEmpty();
    }

    @Override
    public boolean verifyPastRefundList(String testCase) {
        switch (testCase) {
            case "login":
                WebElement pastRefundBtn = driverTools.waitForElementToAppear(PAST_REFUND_BTN).orElseThrow();
                driverTools.click(pastRefundBtn);

                driverTools.waitForElementToAppear(CHEVRON_ARROW_BTN_PAST_REFUND).orElseThrow();
                List<WebElement> pastRefundList = driverTools.elements(CHEVRON_ARROW_BTN_PAST_REFUND).orElseThrow();
                logger.log("Past refund list length: " + pastRefundList.size());
                return !pastRefundList.isEmpty();
            case "guest":
                pastRefundBtn = driverTools.waitForElementToAppear(PAST_REFUND_BTN).orElseThrow();
                return pastRefundBtn.isDisplayed();
            default:
                logger.log("Invalid case provided: " + testCase);
                return false;
        }
    }

    private Locator getFilter(String filter) {
        switch (filter.toLowerCase()) {
            case "car rentals":
                return new Locator(filter, AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Car Rentals') or contains(@text,'Sewa Mobil'))]"));
            case "last 90 days":
                return new Locator(filter, AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Last 90 days') or contains(@text,'90 hari terakhir'))]"));
            case "this month":
                return new Locator(filter, AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'This month') or contains(@text,'Bulan ini'))]"));
            default:
                throw new NotImplementedException("Unimplemented filter type!");
        }
    }

    private void applyFilter(String testTypeFilter, String durationTypeFilter) {
        driverTools.click(FILTER);
        driverTools.click(getFilter(testTypeFilter));
        driverTools.swipeToElement(getFilter(durationTypeFilter), Swipe.UP, 5);
        driverTools.click(getFilter(durationTypeFilter));
        WebElement applyBtn = driverTools.swipeToElement(APPLY_FILTER, Swipe.UP, 3).orElseThrow();
        driverTools.click(applyBtn);
    }

    private Locator getArrowLocator(int index) {
        return new Locator("CHEVRON_ARROW_BTN", AppiumBy.xpath("(//*[contains(@resource-id,'iv_chevron_right')])[" + index + "]"));
    }

    private int findOrderOnHistoryPage(String orderId, String testTypeFilter, String durationTypeFilter) {
        final Locator SCHEVRONS = new Locator("SCHEVRONS", AppiumBy.xpath("//*[contains(@resource-id,'iv_chevron_right')]"));
        driverTools.waitForElementToAppear(PRODUCT_NAME).orElseThrow();
        applyFilter(testTypeFilter, durationTypeFilter);
        boolean findMyOrder = false;
        int i = 1;
        List<WebElement> webElements = driverTools.elements(SCHEVRONS).orElseThrow();
        while (!findMyOrder && i <= webElements.size()) {
            WebElement chevronArrow = driverTools.waitForElementToAppear(getArrowLocator(i), 20).orElseThrow();
            driverTools.click(chevronArrow);

            driverTools.switchToWebViewUrl("yourOrderHistory");
            WebElement orderWebElement = driverTools.waitForElementToAppear(ORDER_ID).orElseThrow();

            String[] orderTextParts = driverTools.getText(orderWebElement).split(" ");
            String actualOrderId = orderTextParts.length > 2 ? orderTextParts[2] : "";

            driverTools.switchToNative();

            logger.log("Actual Order ID: " + actualOrderId);
            logger.log("Expected Order ID: " + orderId);

            findMyOrder = orderId.equals(actualOrderId);
            logger.log("Order->" + i + " status: " + findMyOrder);
            driverTools.back();
            i++;
        }
        logger.log("Order Found: " + findMyOrder);
        return findMyOrder ? i - 1 : -1;
    }


    private Locator statusLocator(int index) {
        return new Locator("STATUS LOCATOR", AppiumBy.xpath("(//*[contains(@resource-id,'tv_status')])[" + index + "]"));
    }

    private Locator openOrder(int index) {
        return new Locator("OPEN ORDER", AppiumBy.xpath("(//*[contains(@resource-id,'iv_chevron_right')])[" + index + "]"));
    }

    @Override
    public boolean verifyCancelStatusOnOrderHistoryPage(String orderId, String testTypeFilter, String durationTypeFilter) {
        driverTools.swipeDown();
        boolean checkStatus = false;
        int pos = findOrderOnHistoryPage(orderId, testTypeFilter, durationTypeFilter);
        if (pos != -1) {
            WebElement statusLoc = driverTools.waitForElementToAppear(statusLocator(pos)).orElseThrow();
            checkStatus = ("Dibatalkan").equalsIgnoreCase(statusLoc.getText()) || ("Canceled").equalsIgnoreCase(statusLoc.getText());
        }
        return checkStatus;
    }

    @Override
    public void deleteOrder(String orderId) {
        driverTools.swipeDown();
        int pos = findOrderOnHistoryPage(orderId, "car rentals", "This month");
        driverTools.click(openOrder(pos));
        driverTools.switchToWebViewUrl("yourOrderHistory");
        driverTools.click(DELETE_BUTTON);
        driverTools.clickElementByCoordinate(YES_DELETE);
        driverTools.switchToNative();
        driverTools.swipeDown();
    }

    @Override
    public boolean isOrderDeleted(String orderId) {
        Boolean findMyOrder = true;
        int i = 0;
        while (!!findMyOrder && i <= 3) {
            driverTools.swipeDown();
            if (driverTools.waitForElementToPresence(NO_ORDERS, 5).isPresent()) {
                return true;
            }
            WebElement chevronArrow = driverTools.waitForElementToAppear(getArrowLocator(1)).orElseThrow();
            driverTools.click(chevronArrow);
            driverTools.switchToWebViewUrl("yourOrderHistory");
            WebElement orderWebElement = driverTools.waitForElementToAppear(ORDER_ID).orElseThrow();
            logger.log(driverTools.getText(orderWebElement).split(" ")[2]);
            logger.log(orderId);
            String actualOrderId = driverTools.getText(orderWebElement).split(" ")[2];
            driverTools.switchToNative();
            findMyOrder = orderId.equals(actualOrderId);
            logger.log("Order->" + 1 + " status: " + findMyOrder);
            driverTools.back();
            i++;
        }
        return !(findMyOrder);
    }

    @Override
    public Boolean isFinishedOrderShown() {
        return driverTools.swipeToElement(FINISHED_ORDER, Swipe.UP, 10).isPresent();
    }

    @Override
    public Boolean isRescheduleOrderShown() {
        return driverTools.swipeToElement(RESCHEDULE_ORDER, Swipe.UP, 10).isPresent();
    }

    @Override
    public Boolean isRefundCompletedOrderShown() {
        return driverTools.swipeToElement(REFUND_COMPLETED_ORDER, Swipe.UP, 10).isPresent();
    }

    @Override
    public Boolean isCancelledOrderShown() {
        return driverTools.swipeToElement(CANCELLED_ORDER, Swipe.UP, 10).isPresent();
    }

    @Override
    public void applySort() {
        driverTools.click(SORT_BUTTON);
        driverTools.click(SORT_CLOSEST_DATE);
    }

    @Override
    public Boolean bothSortAndFilterApplied() {
        List<WebElement> greenDots = driverTools.elements(GREEN_DOTS).orElseThrow();
        return greenDots.size() == 2;
    }

    @Override
    public void openPastRefund() {
        driverTools.click(PAST_REFUND_BTN);
    }

    @Override
    public String getPastRefundCurrency(String refundId) {
        WebElement pricePastRefundElement = driverTools.waitForElementToAppear(getPriceOfPastRefund(refundId)).orElseThrow();
        String currency = getCurrency(driverTools.getText(pricePastRefundElement));
        logger.log("currency: " + currency);
        return currency;
    }

    @Override
    public String getPastRefundId(int index) {
        WebElement refundIdElement = driverTools.waitForElementToAppear(getRefundID(index)).orElseThrow();
        String refundId = driverTools.getText(refundIdElement);
        logger.log("refundId: " + refundId);
        return refundId;
    }

    @Override
    public String getPastRefundOrderTitle(String refundId) {
        WebElement orderTitleElement = driverTools.waitForElementToAppear(getPastRefundProductName(refundId)).orElseThrow();
        String orderTitle = driverTools.getText(orderTitleElement);
        logger.log("orderTitle: " + orderTitle);
        return orderTitle;
    }

    private Locator getRefundID(int index) {
        return new Locator("PAST_REFUND_ID_" + index, AppiumBy.xpath("(//*[contains(@resource-id,'tv_header')])[" + index + "]"));
    }

    private Locator getRefundID(String refundID) {
        return new Locator("PAST_REFUND_ID_" + refundID, AppiumBy.xpath("//*[contains(@resource-id,'tv_header') and contains(@text,'" + refundID + "')]"));
    }

    private Locator getPriceOfPastRefund(String refundId) {
        return new Locator("PRICE_PAST_REFUND", AppiumBy.xpath("//*[contains(@resource-id,'tv_header') and contains(@text,'" + refundId + "')]//following-sibling::*//*[contains(@resource-id,'tv_title') and contains(@text,'Total')]//following-sibling::*//*[contains(@resource-id,'tv_title')]"));
    }

    private Locator getPastRefundProductName(String refundID) {
        return new Locator("PAST_REFUND_PRODUCT_NAME_" + refundID, AppiumBy.xpath("//*[contains(@resource-id,'tv_header') and contains(@text,'" + refundID + "')]//following-sibling::*[contains(@resource-id,'tv_title')]"));
    }

    private Locator getPastRefundStatus(String refundID) {
        return new Locator("PAST_REFUND_STATUS_" + refundID, AppiumBy.xpath("//*[contains(@resource-id,'tv_header') and contains(@text,'" + refundID + "')]//following-sibling::*[contains(@resource-id,'tv_status')]"));
    }

    private Locator getPastRefundDate(String refundID) {
        return new Locator("PAST_REFUND_STATUS_" + refundID, AppiumBy.xpath("//*[contains(@resource-id,'tv_header') and contains(@text,'" + refundID + "')]//following-sibling::*//*[contains(@resource-id,'tv_title') and (contains(@text,'Payment received') or contains(@text,'Dana telah diterima'))]//following-sibling::*//*[contains(@resource-id,'tv_title')]"));
    }

    @Override
    public void swipeToPastRefund(String pastRefundId) {
        driverTools.swipeToElement(getRefundID(pastRefundId), Swipe.UP, 10);
    }

    @Override
    public boolean verifyPastRefundCard(String refundId, String expectedCurrency) {
        boolean isRefundIdDisplayed = driverTools.isElementDisplayed(getRefundID(refundId));
        logger.log("isRefundIdDisplayed: " + isRefundIdDisplayed);
        Assertion.assertThat("refund id displayed ", isRefundIdDisplayed, is(true));

        boolean isProductNameDisplayed = driverTools.isElementDisplayed(getPastRefundProductName(refundId));
        logger.log("isProductNameDisplayed: " + isProductNameDisplayed);
        Assertion.assertThat("refund product name displayed ", isProductNameDisplayed, is(true));

        WebElement refundStatusElement = driverTools.waitForElementToAppear(getPastRefundStatus(refundId)).orElseThrow();
        String refundStatus = driverTools.getText(refundStatusElement);
        logger.log("refundStatus: " + refundStatus);
        Assertion.assertThat("refund product name displayed ", refundStatus, is(either(containsString("Completed")).or(containsString("Selesai"))));

        WebElement refundDateElement = driverTools.waitForElementToAppear(getPastRefundDate(refundId)).orElseThrow();
        String refundDate = driverTools.getText(refundDateElement);
        logger.log("refundDate: " + refundDate);
        Assertion.assertThat("is refund date more then 24 hours", CoreFunctions.isMoreThan24Hours(refundDate), is(true));

        String currentCurrency = getPastRefundCurrency(refundId);
        logger.log("current currency : " + currentCurrency);
        Assertion.assertThat("is current currency is same as expected currency", currentCurrency, equalTo(expectedCurrency));
        return true;
    }

    @Override
    public void openPastRefundDetail(String refundId) {
        driverTools.click(getRefundID(refundId));
    }

    @Override
    public boolean isRedirectedToCorrectPage() {
        return driverTools.isElementDisplayed(getRefundID(1));
    }

    @Override
    public void clickFilter() {
        driverTools.click(FILTER);
    }

    @Override
    public void selectFilterVertical(String vertical, String lang) {
        WebElement filter = driverTools.waitForElementToPresence(getVerticalFilter(vertical, lang)).orElseThrow();
        driverTools.clickElementByCoordinate(filter);
    }

    private Locator getVerticalFilter(String vertical, String lang) {
        if (lang.equals("id")) {
            vertical = CoreFunctions.verticalEngToId(vertical);
        }
        return new Locator("VERTICAL_FILTER", AppiumBy.xpath("//*[contains(@text,'" + vertical + "')]//following-sibling::*[contains(@resource-id,'v_checkbox')]"));
    }

    @Override
    public void setPeriod() {
        driverTools.swipeToElement(SET_PERIOD, Swipe.UP, 5).orElseThrow();
        driverTools.clickElementByCoordinate(SET_PERIOD);
        clickApplyFilter();
        clickFilter();
        driverTools.swipeToElement(PERIOD_START, Swipe.UP, 10).orElseThrow();
        driverTools.clickElementByCoordinate(PERIOD_START);
        driverTools.swipeByDistance(YEAR, Swipe.DOWN, 0.5);
        driverTools.click(DONE_PERIOD);
    }

    @Override
    public void clickApplyFilter() {
        driverTools.click(APPLY_FILTER);
    }

    @Override
    public boolean isTwelveMonthsFilterSelected() {
        boolean selected = driverTools.waitForElementToAppear(TWELVE_MONTHS_FILTER_SELECTED).isPresent();
        logger.log("12 months filter selected: " + selected);
        return selected;
    }

    @Override
    public void clickSeePreviousBookings() {
        WebElement seePreviousBookingsBtn = driverTools.waitForElementToAppear(SEE_PREVIOUS_BOOKINGS_BTN).orElseThrow();
        driverTools.click(seePreviousBookingsBtn);
    }

    @Override
    public boolean isFilterTabOpened() {
        boolean opened = driverTools.waitForElementToAppear(FILTER_OVERLAY).isPresent();
        boolean isFilterButtonPresent = driverTools.swipeToElement(APPLY_FILTER, FILTER_RECYCLER_VIEW, Swipe.UP, 10).isPresent();
        logger.log("Filter tab opened: " + opened);
        return opened && isFilterButtonPresent;
    }
}
