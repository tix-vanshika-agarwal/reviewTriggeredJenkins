package com.tiket.page.order.orderlist;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import com.tiket.pojo.order.GeneralVerticalInternalOrderDetail;
import com.tiket.testbase.Assertion;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.NoSuchContextException;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class OrderListPageAndroid <T extends WebDriver> extends OrderListPage<T> {

    public OrderListPageAndroid(Session<T> session) {
        super(session);
    }

    private static final Locator ORDER_ID = new Locator("ORDER_ID", AppiumBy.id("tv_order_id"));
    private static final Locator PRODUCT_TITLE = new Locator("PRODUCT_TITLE", AppiumBy.id("tv_title"));
    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", AppiumBy.id("tv_price"));
    public static final Locator SKIP_INFORMATION_BTN = new Locator("SKIP_INFORMATION_BTN", AppiumBy.id("tds_link_coach_mark"));
    private static final Locator ACTIVE_ORDERS_BUTTON = new Locator("ACTIVE_ORDERS_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'button_title') and (translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='active orders' or translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='pesanan aktif')]"));
    private static final Locator LINK_TO_VIDEO = new Locator("LINK_TO_VIDEO", AppiumBy.xpath("//*[contains(@resource-id,'tds_btn') and contains(@text,'Link')]"));
    private static final Locator ACTIVE_ORDER_VIEW = new Locator("ACTIVE_ORDER_VIEW", AppiumBy.xpath("//*[contains(@resource-id,'active_order_container')]//*[contains(@resource-id,'rv_content')]"));
    private static final Locator ACTIVE_ORDERS = new Locator("ACTIVE_ORDERS", AppiumBy.xpath("//*[contains(@resource-id,'active_order_container')]//*[contains(@resource-id,'rv_content')]/child::*"));
    private static final Locator ORDER_DETAIL_BACK_BUTTON = new Locator("ORDER_DETAIL_BACK_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_icon_navigation_toolbar_group') or contains(@class,'android.widget.Button')]"));
    private static final Locator ORDER_DETAIL_BACK_BUTTON_WEBVIEW = new Locator("ORDER_DETAIL_BACK_BUTTON_WEBVIEW", AppiumBy.xpath("//*[@data-testid='back_button']"));
    private static final Locator ORDER_ID_PAYMENT = new Locator("ORDER_ID_PAYMENT", AppiumBy.xpath("//*[contains(@text, 'Order ID')]"));
    private static final Locator ORDER_ID_WEBVIEW = new Locator("ORDER_ID_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'styles_header__')]//*[ contains(text(),'Order ID')]"));
    private static final Locator ORDER_CARD = new Locator("ORDER_CARD", AppiumBy.xpath("//*[contains(@resource-id,'cl_root')]//*[contains(@resource-id,'tv_title')]"));
    private static final Locator GIFT_ORDER_CARD = new Locator("GIFT_ORDER_CARD", AppiumBy.xpath("(//*[@class='android.view.ViewGroup' and ./*[contains(@resource-id, 'll_information_container')]]/*[contains(@text, 'Gift Card')])[1]"));
    private static final Locator ACTIVE_TICKET_TAB = new Locator("ACTIVE_TICKET_TAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tab_layout') and (contains(@text,'Active Tickets') or contains(@text,'Tiket Aktif'))]"));
    private static final Locator ONGOING_REFUND_TAB = new Locator("ONGOING_REFUND_TAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tab_layout') and (contains(@text,'Ongoing Refund') or contains(@text,'Refund Berjalan'))]"));
    private static final Locator SELECTED_ONGOING_REFUND_TAB = new Locator("SELECTED_ONGOING_REFUND_TAB", AppiumBy.xpath("//*[(@content-desc='Ongoing Refund' or @content-desc='Refund Berjalan') and @selected='true']"));
    private static final Locator REFUND_ID = new Locator("REFUND_ID", AppiumBy.xpath("(//*[contains(@resource-id,'tv_header') and contains(@text,'Refund ID:')])[1]"));
    private static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", AppiumBy.xpath("//*[contains(@resource-id,'tv_status') and (contains(@text,'Verified by hotel') or contains(@text,'Diverifikasi hotel') or contains(@text,'tiket.com') or contains(@text,'Refund has completed') or contains(@text,'Refund selesai') or contains(@text,'tiket.com'))]//parent::*//*[contains(@resource-id,'tv_title') and contains(@text,'Hotel') or contains(@text,'hotel')]"));
    private static final Locator HOTEL_ESTIMATED_FUND_RECEIVED = new Locator("HOTEL_ESTIMATED_FUND_RECEIVED", AppiumBy.xpath("//*[contains(@resource-id,'tv_status') and (contains(@text,'Verified by hotel') or contains(@text,'Diverifikasi hotel')  or contains(@text,'Refund has completed') or contains(@text,'Refund selesai') or contains(@text,'tiket.com'))]//parent::*//*[contains(@resource-id,'tv_title') and contains(@text,'20') and not(contains(@text,'IDR'))]"));
    private static final Locator ESTIMATED_PAYMENT = new Locator("ESTIMATED_PAYMENT", AppiumBy.xpath("(//*[contains(@resource-id,'fbl_subtitle')])[2]//*[contains(@resource-id,'tv_title')]"));
    private static final Locator ESTIMATED_TOTAL_PAYMENT = new Locator("ESTIMATED_TOTAL_PAYMENT", AppiumBy.xpath("//*[contains(@resource-id,'ll_detail_content')]//following-sibling::*[contains(@resource-id,'divider_bottom')]//preceding::*[contains(@resource-id,'tv_title')][1]"));
    private static final Locator REFUND_STATUS = new Locator("REFUND_STATUS", AppiumBy.xpath("(//*[contains(@resource-id,'tv_status') and (contains(@text,'Verified by hotel') or contains(@text,'Diverifikasi hotel') or contains(@text,'Refund has completed') or contains(@text,'Refund selesai') or contains(@text,'tiket.com'))])"));
    private static final Locator ORDER_HISTORY_ICON = new Locator("ORDER_HISTORY_ICON", AppiumBy.id("tds_icon_appbar"));
    private static final Locator CANCEL_BTN = new Locator("CANCEL_BTN", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and ( contains(@text,'Cancel') or contains(@text,'Batalkan') )]//following-sibling::*[contains(@resource-id,'iv_chevron_right')]"));
    private static final Locator CHECKBOX_CANCEL_BTN = new Locator("CHECKBOX_CANCEL_BTN", AppiumBy.id("cb_agreement_item"));
    private static final Locator AGREE_CANCEL_BTN = new Locator("AGREE_CANCEL_BTN", AppiumBy.id("btn_positive"));
    private static final Locator ACTIVE_ORDER_STATUS = new Locator("ACTIVE_ORDER_STATUS", AppiumBy.id("tv_status"));
    private static final Locator POPUP_SUCCESS_CANCEL = new Locator("POPUP_SUCCESS_CANCEL_BTN", AppiumBy.id("tds_snackbar_root"));
    private static final Locator SEE_DETAILS = new Locator("SEE_DETAILS", AppiumBy.id("cl_container"));
    private static final Locator FIRST_WAITING_PAYMENT_ORDER = new Locator("FIRST_WAITING_PAYMENT_ORDER", AppiumBy.xpath("(//*[contains(@resource-id,'cl_container')])[1]"));
    private static final Locator REFUND_DETAILS_BUTTON = new Locator("REFUND_DETAILS_BUTTON", AppiumBy.id("iv_chevron_right"));
    private static final Locator PAGE_LOADED = new Locator("PAGE_LOADED", AppiumBy.xpath("//*[contains(@class,'ETicket_wrapper')]"));
    private static final Locator ORDER_CROSS_SELL_SEE_ALL_BTN = new Locator("ORDER_CROSS_SELL_ALL_BTN", AppiumBy.xpath("(//*[contains(@resource-id,'btn_see_all')])[1]"));
    private static final Locator ORDER_CHIPS = new Locator("ORDER_CHIPS", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_container')]"));
    private static final Locator CROSS_SELL_INVENTORY_CONTAINER = new Locator("CROSS_SELL_INVENTORY_CONTAINER", AppiumBy.xpath("//*[contains(@resource-id,'card_view')]//*[contains(@resource-id,'container')]"));
    private static final Locator CROSS_SELL_INVENTORY_TITLE = new Locator("CROSS_SELL_INVENTORY_TITLE", AppiumBy.xpath("(//*[contains(@resource-id,'card_view')]//*[contains(@resource-id,'tv_title')])[1]"));
    private static final Locator CROSS_SELL_INVENTORY_ERROR = new Locator("CROSS_SELL_INVENTORY_ERROR", AppiumBy.id("error_view"));
    private static final Locator OK_BTN = new Locator("OK_BTN", AppiumBy.xpath("//*[contains(@text,'Ok') or contains(@text,'OK')]"));
    private static final Locator SKIP_INFORMATION_BUTTON = new Locator("SKIP_INFORMATION_BUTTON", AppiumBy.xpath("//button[contains(@class,'coachmark_done_btn') or contains(@class,'coachmark_skip_btn')]"));
    private static final Locator WAITING_PAYMENT_ORDER_SECTION = new Locator("WAITING_PAYMENT_ORDER_SECTION", AppiumBy.id("rv_waiting_payment_content"));
    private static final Locator REFUND_ORDER_TITLE = new Locator("REFUND_ORDER_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_header')]//following-sibling::*[contains(@resource-id,'tv_title')]"));
    private static final Locator GIFT_CARD_HEADER = new Locator("GIFT_CARD_HEADER", AppiumBy.xpath("//*[@class='android.view.ViewGroup' and ./*[contains(@resource-id, 'll_information_container')]]/*[contains(@text, 'Gift Card')]"));

    @Override
    public boolean isOrderListPage() {
        Assertion.assertThat("Order card should be displayed", driverTools.waitForElementToAppear(ORDER_CARD).isPresent(), is(true));
        Assertion.assertThat("Active ticket tab should be displayed", driverTools.waitForElementToAppear(ACTIVE_TICKET_TAB).isPresent(), is(true));
        Assertion.assertThat("Ongoing refund tab should be displayed", driverTools.waitForElementToAppear(ONGOING_REFUND_TAB).isPresent(), is(true));
        return true;
    }

    @Override
    public Boolean checkIfOrderIdExist(String orderID) {
        driverTools.waitForElementToAppear(getOrderIdLocator(orderID));
        return driverTools.swipeToElement(getOrderIdLocator(orderID), WAITING_PAYMENT_ORDER_SECTION, Swipe.LEFT, 5).isPresent();
    }

    private Locator getOrderIdLocator(String orderID) {
        return new Locator("ORDER_ID_CARD", AppiumBy.xpath("//*[contains(@resource-id,'rv_waiting_payment_content')]//*[contains(@resource-id,'tv_order_id') and contains(@text,'" + orderID + "')]"));
    }

    @Override
    public String getTotalPriceByOrderId(String orderID) {
        driverTools.swipeToElement(getTotalPriceByOrderIdLocator(orderID), WAITING_PAYMENT_ORDER_SECTION, Swipe.LEFT_LONG, 5);
        return driverTools.getText(getTotalPriceByOrderIdLocator(orderID)).orElseThrow();
    }

    private Locator getTotalPriceByOrderIdLocator(String orderID) {
        return new Locator("TOTAL_PRICE_"+orderID, AppiumBy.xpath("//*[contains(@resource-id,'rv_waiting_payment_content')]//*[contains(@resource-id,'cl_container') and ./*[//*[contains(@resource-id,'tv_order_id') and contains(@text,'" + orderID + "')]]]/*[contains(@resource-id,'tv_price')]"));
    }

    @Override
    public String getProductTitleByOrderId(String orderID) {
        driverTools.swipeToElement(getProductTitleByOrderIdLocator(orderID), WAITING_PAYMENT_ORDER_SECTION, Swipe.LEFT_LONG, 5);
        return driverTools.getText(getProductTitleByOrderIdLocator(orderID)).orElseThrow();
    }

    @Override
    public String getProductTitleByOrderId(String orderID, String productName) { // Android ignores productName and delegates to the orderId-based lookup.
        return getProductTitleByOrderId(orderID);
    }

    @Override
    public void openFirstGiftCardOrder(){
        driverTools.click(GIFT_ORDER_CARD);
    }

    private Locator getProductTitleByOrderIdLocator(String orderID) {
        return new Locator("PRODUCT_TITLE_"+orderID, AppiumBy.xpath("//*[contains(@resource-id,'rv_waiting_payment_content')]//*[contains(@resource-id,'cl_container') and ./*[//*[contains(@resource-id,'tv_order_id') and contains(@text,'" + orderID + "')]]]/*[contains(@resource-id,'tv_title')]"));
    }

    private Locator completePaymentButton(String orderId) {
        return new Locator("COMPLETE_PAYMENT_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tv_order_id') and contains(@text,'" + orderId + "')]//following-sibling::*[contains(@resource-id,'btn_complete')]"));
    }

    private Locator waitingPaymentCard(String orderId) {
        return new Locator("WAITING_PAYMENT_CARD", AppiumBy.xpath("//*[contains(@resource-id,'tv_order_id') and contains(@text,'" + orderId + "')]"));
    }

    private Locator refundIdTitle(String refundId) {
        return new Locator("REFUND_ID", AppiumBy.xpath("//*[contains(@text,'Refund ID: " + refundId + "')]"));
    }

    @Override
    public void openOrderCompletePayment(String orderId) {
        openActiveOrdersList();
        driverTools.swipeDown(4);
        checkIfOrderIdExist(orderId);
        driverTools.waitForElementToAppear(completePaymentButton(orderId));// StaleElementReference
        driverTools.click(completePaymentButton(orderId));
    }

    @Override
    public void openOrderWaitingPayment(String orderId) {
        driverTools.swipeDown(2);
        checkIfOrderIdExist(orderId);
        driverTools.waitForElementToAppear(waitingPaymentCard(orderId));// StaleElementReference
        driverTools.click(waitingPaymentCard(orderId));
    }

    private Locator getOrderId(String expectedType,String type) {
        return new Locator(type,AppiumBy.xpath("//*[contains(@resource-id,'cl_container')]//*[(contains(@resource-id,'tv_order_id')) and (contains(@text,'"+expectedType+"'))]"));
    }

    @Override
    public String getOrderId() {
        WebElement webElement = driverTools.waitForElementToAppear(ORDER_ID).orElseThrow();
        return driverTools.getText(webElement);
    }

    @Override
    public String getProductTitle() {
        WebElement webElement = driverTools.waitForElementToAppear(PRODUCT_TITLE).orElseThrow();
        return driverTools.getText(webElement);
    }

    @Override
    public String getTotalPrice() {
        WebElement webElement = driverTools.waitForElementToAppear(TOTAL_PRICE).orElseThrow();
        return driverTools.getText(webElement);
    }

    @Override
    @SneakyThrows
    public String findActiveTicket(String name, String detail, String orderID, int count) {
        driverTools.swipeDown(3);
        driverTools.waitForElementToAppear(getActiveOrderCard(1));
        boolean isDone = false;
        String orderStatus = "";
        
        for (int i = 0; i < count; i++) {
            driverTools.waitForElementToAppear(getActiveOrderCard(1));
            List<WebElement> activeOrderDisplayedList = driverTools.elements(ACTIVE_ORDERS).orElseThrow();

            for (int j = 0; j < activeOrderDisplayedList.size(); j++) {
                boolean isOrderDetailValid = isOrderDetailValueValid(j+1, detail);
                boolean isDetailEmpty = detail.equals("-");

                if ((getActiveOrderName(j+1).equalsIgnoreCase(name) || getActiveOrderName(j+1).contains(name))
                        && (getActiveOrderDetail(j+1).toLowerCase().contains(detail.toLowerCase()) || isDetailEmpty || isOrderDetailValid)
                ) {
                    driverTools.swipeToElement(getStatusOrder(j + 1), Swipe.UP, 3).orElseThrow();
                    orderStatus = driverTools.getText(getStatusOrder(j + 1)).orElseThrow();
                    driverTools.click(getActiveOrderCard(j + 1));
                    driverTools.waitForElementToAppear(OK_BTN,5).ifPresent(driverTools::click);
                    if (getActiveOrderID().contains(orderID)) {
                        logger.log("Order ID is equal '" + orderID + "'");
                        isDone = true;
                        break;
                    } else {
                        logger.log("Order ID should be '" + orderID + "'");
                        logger.log("But displayed '" + getActiveOrderID() + "'");
                        clickBackButton();

                        for (int k = 0; k < i; k++) {
                            driverTools.swipeUp(ACTIVE_ORDER_VIEW);
                        }
                    }
                }
            }

            if (isDone) {
                break;
            } else {
                driverTools.swipeUp(ACTIVE_ORDER_VIEW);
            }
        }
        logger.log("orderStatus -> " + orderStatus);
        return orderStatus;
    }

    private Locator getTrainOrderCard(String route, String date){
        return new Locator("route",AppiumBy.xpath("//*[contains(@text,'"+route+"')]//following-sibling::*//*[contains(@text,'"+date+"')]"));
    }

    private boolean isOrderDetailValueValid(int index, String detail) {
        Locator orderDetailLocator = getActiveOrderDetailLocator(index, detail);
        driverTools.waitForElementToAppear(orderDetailLocator);
        return driverTools.isElementDisplayed(orderDetailLocator);
    }

    private Locator getStatusOrder(int index) {
        return new Locator("ORDER_STATUS-" + index, AppiumBy.xpath("//*[contains(@resource-id,'active_order_container')]//*[contains(@resource-id,'rv_content')]/child::*[" + index + "]//*[contains(@resource-id,'tv_status')]"));
    }

    private Locator getActiveOrderCard(int index) {
        return new Locator("ACTIVE_ORDER-" + index, AppiumBy.xpath("//*[contains(@resource-id,'active_order_container')]//*[contains(@resource-id,'rv_content')]/child::*[" + index + "]"));
    }

    private void clickBackButton() {
        driverTools.switchToWebView();
        driverTools.click(ORDER_DETAIL_BACK_BUTTON_WEBVIEW);
        driverTools.switchToNative();
    }

    private String getActiveOrderID() {
        String result;
        driverTools.switchToWebViewUrl("order");
        if(driverTools.waitForElementToAppear(PAGE_LOADED,30).isPresent()) {
            driverTools.waitForElementToAppear(ORDER_ID_WEBVIEW,40);
            result = driverTools.getText(ORDER_ID_WEBVIEW).orElseThrow();
            driverTools.switchToNative();
        } else {
            driverTools.switchToNative();
            driverTools.waitForElementToPresence(ORDER_ID_PAYMENT,60);
            result = driverTools.getText(ORDER_ID_PAYMENT).orElseThrow();
        }

        return result.replaceAll("[^0-9]", "");
    }

    private String getActiveOrderDetail(int index) {
        Locator orderDetailLocator = getActiveOrderDetailLocator(index);
        driverTools.waitForElementToAppear(orderDetailLocator);
        return !driverTools.isElementDisplayed(orderDetailLocator) ? "Empty" : driverTools.getText(orderDetailLocator).orElseThrow();
    }

    private Locator getActiveOrderDetailLocator(int index) {
        return new Locator("ORDER_DETAIL-'" + index + "'", AppiumBy.xpath("//*[contains(@resource-id,'active_order_container')]//*[contains(@resource-id,'rv_content')]/child::*[" + index + "]//*[contains(@resource-id,'ll_information_container')]/child::*[1]"));
    }

    private Locator getActiveOrderDetailLocator(int index, String value) {
        return new Locator("ORDER_DETAIL_VALUE-'" + index + "'", AppiumBy.xpath("//*[contains(@resource-id,'active_order_container')]//*[contains(@resource-id,'rv_content')]/child::*[" + index + "]//*[contains(@resource-id,'ll_information_container')]//child::*[contains(@text,'"+value+"')]"));
    }

    private String getActiveOrderName(int index) {
        Locator orderNameLocator = getActiveOrderNameLocator(index);
        driverTools.waitForElementToAppear(orderNameLocator);
        return !driverTools.isElementDisplayed(orderNameLocator) ? "Empty" : driverTools.getText(orderNameLocator).orElseThrow();
    }

    private Locator getActiveOrderNameLocator(int index) {
        return new Locator("ORDER_NAME-" + index, AppiumBy.xpath("//*[contains(@resource-id,'active_order_container')]//*[contains(@resource-id,'rv_content')]/child::*[" + index + "]//*[contains(@resource-id,'tv_title')]"));
    }

    @Override
    public void openOrderDetail(String productName) {
        WebElement orderDetail = driverTools.waitForElementToAppear(orderDetailLocator(productName), 10).orElseThrow();
        driverTools.click(orderDetail);
    }

    private Locator orderDetailLocator(String productName) {
        return new Locator("bottomMenuIcons", By.xpath("(//*[contains(@resource-id, 'active_order_container')]//*[contains(@resource-id, 'tv_title') and contains( translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), translate('" + productName + "','ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'))])[1]"));
    }

    @Override
    public void removePopUp() {
        driverTools.waitForElementToAppear(SKIP_INFORMATION_BTN, 10).ifPresent(driverTools::click);
    }

    @Override 
    public void openActiveOrdersList() {
        removePopUp();
        driverTools.click(ACTIVE_ORDERS_BUTTON);
    }

    private Locator getWaitingPaymentOrderLocator(String orderID) {
        return new Locator("WAITING_PAYMENT_ORDER", AppiumBy.xpath("(//*[contains(@resource-id,'tv_order_id') and contains(@text,'" + orderID + "')])"));
    }

    @Override
    public void clickLinkToVideo() {
        WebElement webElement = driverTools.waitForElementToBeClickable(LINK_TO_VIDEO).orElseThrow();
        driverTools.click(webElement);
    }

    private Locator getIssuedOrderLocator(String productName) {
        return new Locator("ISSUED_ORDER", AppiumBy.xpath("(//*[contains(@resource-id,'active_order_container')]//*[@resource-id,'rv_content']//*[@text='" + productName + "'])[1]"));
    }

    @Override
    public boolean matchOrderId(String expectedOrderId) {
        WebElement orderId = driverTools.swipeToElement(getOrderId(expectedOrderId,"orderId"), Swipe.RIGHT,5).orElseThrow();
        return driverTools.isElementDisplayed(orderId);
    }

    @Override
    public boolean matchDetails(String expectedType,String type) {
        // FIXME: IMPLEMENTATION PENDING
        return false;
    }

    @Override
    public void openActiveTicketTab() {
        driverTools.waitForElementToPresence(ACTIVE_TICKET_TAB, 15).orElseThrow();
        driverTools.click(ACTIVE_TICKET_TAB);
    }

    @Override
    public void openOngoingRefundTab() {
        driverTools.waitForElementToPresence(ONGOING_REFUND_TAB, 15).orElseThrow();
        driverTools.click(ONGOING_REFUND_TAB);
    }

    @Override
    public boolean ongoingRefundTabStatus() {
        return driverTools.waitForElementToPresence(SELECTED_ONGOING_REFUND_TAB).isPresent();
    }

    @Override
    public boolean findRefundCard(String refundId) {
        return driverTools.swipeToElement(refundIdTitle(refundId), Swipe.UP, 20).isPresent();
    }

    @Override
    public void openRefundCard(String refundId) {
        driverTools.click(refundIdTitle(refundId));
    }

    @Override
    public void findHotelRefund() {
        driverTools.swipeToElement(REFUND_STATUS, Swipe.UP, 20).orElseThrow();
    }

    @Override
    public String getRefundId() {
        driverTools.swipeToElement(ESTIMATED_PAYMENT, Swipe.UP, 2).orElseThrow();
        String refundIdText = driverTools.getText(REFUND_ID).orElseThrow();
        String[] refundIdArray = refundIdText.split(": ");
        String refundId = refundIdArray[1];
        logger.log("Refund ID Order List: " + refundId);
        return refundId;
    }

    @Override
    public String getEstimatedTotalPayment() {
        driverTools.swipeToElement(ESTIMATED_TOTAL_PAYMENT, Swipe.UP, 2);
        return driverTools.getText(ESTIMATED_TOTAL_PAYMENT).orElseThrow();
    }

    @Override
    public String getRefundHotelName(String ProductName) {
        driverTools.swipeToElement(REFUND_STATUS,Swipe.UP, 2).orElseThrow();
        String hotelName = driverTools.getText(getrefundHotelName(ProductName.toLowerCase())).orElseThrow();
        logger.log("Refund Hotel Name Order List: " + hotelName);
        return hotelName;
    }

    private Locator getrefundHotelName(String productName) {
        return new Locator("Refund Hotel", AppiumBy.xpath("//*[contains(@resource-id,'tv_status') and (contains(@text,'Verified by hotel') or contains(@text,'Diverifikasi hotel') or contains(@text,'Refund has completed') or contains(@text,'tiket.com') or contains(@text,'Refund selesai'))]//parent::*//*[contains(@resource-id,'tv_title') and contains(@text,'"+productName+"')]"));
    }

    @Override
    public String getHotelRefundEstimatedDate(String refundID) {
        String estimatedDateText = driverTools.getText(HOTEL_ESTIMATED_FUND_RECEIVED).orElseThrow();
        // sample estimated date text -> 01 January 2023
        String[] estimatedDateOld = estimatedDateText.split(" ");
        char firstDigitDay = estimatedDateOld[0].charAt(0);
        String day;
        // if first digit of day is 0 (ex: 01, 02, 03, etc)
        if (firstDigitDay == '0') {
            // remove 0 from day
            day = Character.toString(estimatedDateOld[0].charAt(1));
        }
        // if first digit of day is not 0 (ex: 11, 21, 31, etc)
        else {
            // keep the day as it is
            day = estimatedDateOld[0];
        }
        // get first three letters of month (ex: January -> Jan)
        String month = estimatedDateOld[1].substring(0, 3);
        String year = estimatedDateOld[2];
        String[] estimatedDateNew = new String[]{day, month, year};
        String estimatedDate = String.join(" ", estimatedDateNew);
        // sample estimatedDate = 1 Jan 2023
        logger.log("Estimated Fund Received Order List: " + estimatedDate);
        return estimatedDate;
    }

    @Override
    public String getHotelRefundEstimatedPayment(String Currency) {
        String estimatedPaymentText = driverTools.getText(getHotelRefundEstimatedPaymentLocator(Currency)).orElseThrow();
        // sample estimatedPaymentText = IDR 123.456 or on special case estimatedPaymentText = IDR 123.456 + tiketPoints icon
        // need to split text to prevent error occurring on special case if it appears on first refund card
        String[] estimatedPaymentArray = estimatedPaymentText.split(" \\+ ");
        String estimatedPayment = estimatedPaymentArray[0];
        logger.log("Estimated Payment Order List: " + estimatedPayment);
        return estimatedPayment;
    }
    private Locator getHotelRefundEstimatedPaymentLocator(String Currency) {
        return new Locator("ESTIMATE_PAYMENT", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'"+Currency+"')]"));
    }

    @Override
    public String getHotelRefundStatus() {
        String status = driverTools.getText(REFUND_STATUS).orElseThrow();
        logger.log("Refund Status Order List: " + status);
        return status;
    }

    @Override
    public void openHotelRefund(String hotelName) {
        driverTools.click(REFUND_DETAILS_BUTTON);
    }

    public void openOrderHistory() {
        WebElement orderHistoryIcon = driverTools.waitForElementToAppear(ORDER_HISTORY_ICON).orElseThrow();
        driverTools.click(orderHistoryIcon);
    }

    private Locator getPickOneActionLocator(String orderId){
        return new Locator("PickOneActionLocator",AppiumBy.xpath("//*[contains(@resource-id,'tv_order_id') and contains(@text,'"+orderId+"')]//following-sibling::*[contains(@resource-id,'iv_ellipsis')]"));
    }
    @Override
    public void findOrder(String orderId){
        driverTools.waitForElementToAppear(WAITING_PAYMENT_ORDER_SECTION,10).orElseThrow();
        driverTools.swipeByDistance(WAITING_PAYMENT_ORDER_SECTION,Swipe.DOWN,0.5,2);
        WebElement ellipseBtn = driverTools.swipeByDistanceToElement(getPickOneActionLocator(orderId),WAITING_PAYMENT_ORDER_SECTION,Swipe.LEFT,0.6,4).orElseThrow();
        driverTools.click(ellipseBtn);
    }
    @Override
    public void openPaymentRequiredOrder() {
        driverTools.swipeToElement(ACTIVE_ORDER_STATUS, Swipe.UP, 5).orElseThrow();
        for (int i = 0; i < 5; i++) {
            List<WebElement> activeOrderStatus = driverTools.elements(ACTIVE_ORDER_STATUS).orElseThrow();
            for (WebElement status : activeOrderStatus) {
                String statusText = driverTools.getText(status);
                if (statusText.equalsIgnoreCase("Payment Required") || statusText.equalsIgnoreCase("Belum Lunas")) {
                    logger.log("Opening order with status -> " + statusText);
                    driverTools.click(status);
                    driverTools.waitForElementToAppear(OK_BTN, 5).ifPresent(driverTools::click);
                    return;
                }
            }
            driverTools.swipeUp();
        }
        Assertion.assertThat("Order with status Payment Required / Belum Lunas should be found", false, is(true));
    }

    @Override
    public boolean cancelOrder(){
        WebElement cancelBtn = driverTools.waitForElementToAppear(CANCEL_BTN).orElseThrow();
        driverTools.click(cancelBtn);

        driverTools.waitForElementToAppear(CHECKBOX_CANCEL_BTN).orElseThrow();
        List<WebElement> checkBoxes = driverTools.elements(CHECKBOX_CANCEL_BTN).orElseThrow();
        for (WebElement checkbox : checkBoxes) {
            driverTools.click(checkbox);
        }

        WebElement agreeBtn = driverTools.waitForElementToAppear(AGREE_CANCEL_BTN).orElseThrow();
        driverTools.click(agreeBtn);

        boolean success = driverTools.waitForElementToPresence(POPUP_SUCCESS_CANCEL).isPresent();

        driverTools.swipeDown(3);
        return success;
    }

    @Override
    public void seeDetails() {
        driverTools.click(SEE_DETAILS);
    }


    /**
     * for searching unpaid order on order list page by order id
     * */
    @Override
    public void openOrderDetailWaitingForPayment(String orderID){
        logger.log("searching order id of : " + orderID);
        openActiveOrdersList();
        refreshTheOrderList(2);
        driverTools.swipeToElement(getOrderWaitingForPaymentLocator(orderID),Swipe.LEFT,4).orElseThrow();
        driverTools.click(getOrderWaitingForPaymentLocator(orderID));
        driverTools.dummyWait(10);
    }

    private Locator getOrderWaitingForPaymentLocator(String orderID){
        return new Locator("ORDER", By.xpath("//*[contains(@resource-id, 'tv_order_id') and contains(@text, '"+orderID+"')]"));
    }

    @Override
    public void refreshTheOrderList(int count) {
        driverTools.swipeDown(count);
    }

    @Override
    public void openFirstWaitingPaymentOrder() {
        driverTools.click(FIRST_WAITING_PAYMENT_ORDER);
    }

    @Override
    public boolean verifyWaitingForPayment(String OrderId) {
        return driverTools.isElementDisplayed(getOrderWaitingForPaymentLocator(OrderId));
    }

    private void reopenYourOrderPage(){
        HomePage<T> homePage = HomePage.getInstance(session);
        homePage.clickHomeIcon();
        homePage.waitForHomePageToLoad();
        homePage.openMyOrderPage();
    }


    @Override
    public boolean verifyCrossSellInOrderListPage() {
        int reoloadYourOrderCounter = 0;
        driverTools.swipeDown(3);
        boolean checkChipsPresent = driverTools.swipeToElement(ORDER_CHIPS, Swipe.UP, 3).isPresent();
        while(!checkChipsPresent && reoloadYourOrderCounter<3){
            logger.log("reopen my order - " +  reoloadYourOrderCounter);
            reopenYourOrderPage();
            checkChipsPresent =  driverTools.swipeToElement(ORDER_CHIPS, Swipe.UP, 3).isPresent();
            reoloadYourOrderCounter = reoloadYourOrderCounter+1;
        }

        boolean isCrossSellChipsDisplayed = driverTools.isElementDisplayed(ORDER_CHIPS);
        Assertion.assertThat("Verify cross sell chips displayed",isCrossSellChipsDisplayed , is(true));
        List<WebElement> orderChips = driverTools.elements(ORDER_CHIPS).orElseThrow();

        driverTools.swipeToElement(CROSS_SELL_INVENTORY_TITLE, Swipe.UP, 2);
        String firstInventoryNameFirstChips = driverTools.getText(CROSS_SELL_INVENTORY_TITLE).orElseThrow();
        driverTools.click(orderChips.get(1));
        driverTools.waitForElementToAppear(CROSS_SELL_INVENTORY_ERROR,2).ifPresent(driverTools::clickElementByCoordinate);
        String firstInventoryNameSecondChips = driverTools.getText(CROSS_SELL_INVENTORY_TITLE).orElseThrow();
        Assertion.assertThat("Chips clickable", firstInventoryNameFirstChips, is(not(equalTo(firstInventoryNameSecondChips))));

        boolean isCrossSellInvetoryPresent = driverTools.isElementDisplayed(CROSS_SELL_INVENTORY_CONTAINER);
        Assertion.assertThat("Verify cross sell inventory present", isCrossSellInvetoryPresent, is(true));
        return true;
    }

    @Override
    public boolean verifyCrossSellSeeAllBtn(){
        //check first chips section see all btn will redirect to ttd
        List<WebElement> orderChips = driverTools.elements(ORDER_CHIPS).orElseThrow();
        driverTools.click(orderChips.get(0));

        driverTools.swipeToElement(ORDER_CROSS_SELL_SEE_ALL_BTN, Swipe.UP, 2);
        driverTools.swipeUp();
        WebElement crossSeellBtn = driverTools.element(ORDER_CROSS_SELL_SEE_ALL_BTN).orElseThrow();
        Assertion.assertThat("Verify cross sell see all btn present", driverTools.isElementDisplayed(crossSeellBtn), is(true));

        driverTools.click(ORDER_CROSS_SELL_SEE_ALL_BTN);
        TTDSearchResultPage<T> ttdSearchResultPage = TTDSearchResultPage.getInstance(session); 
        ttdSearchResultPage.isSearchResultPageShown();
        boolean isTtdSrpShown = ttdSearchResultPage.isSearchResultPageShown();
        Assertion.assertThat("Verify ttd srp shown", isTtdSrpShown, is(true));
        driverTools.back();
        return isTtdSrpShown;
    }

    @Override
    @SneakyThrows
    public void openActiveTicketViaDeeplink(String orderId) {
        JSONObject response = getInternalOrderData(orderId);
        GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = getVerticalInternalOrderDtata(response);
        String orderHash = generalVerticalInternalOrderDetail.data.orderHash;
        String orderDetailId = getCorrectVerticalOrderDetailId(generalVerticalInternalOrderDetail);
        openActiveTicketViaDeeplink(orderId,orderHash,orderDetailId);
    }

    @Override
    public void openActiveTicketViaDeeplink(String orderId, String orderHash, String orderDetailId) {
        Deeplink deepLink = new Deeplink("ORDER_DETAIL_PAGE_"+orderId,
                "https://m.gatotkaca.tiket.com/yourorder/detail?orderId="+orderId+"&orderHash="+orderHash+"&orderDetailId="+orderDetailId,
                "https://m.preprod.tiket.com/yourorder/detail?orderId="+orderId+"&orderHash="+orderHash+"&orderDetailId="+orderDetailId,
                "https://m.tiket.com/yourorder/detail?orderId="+orderId+"&orderHash="+orderHash+"&orderDetailId="+orderDetailId);
        driverTools.deeplink(deepLink);
        driverTools.switchToNative();
        try{
            driverTools.switchToWebViewUrl("detail?orderId="+orderId);
            driverTools.waitForElementToAppear(OrderDetailWeb.HIDE_BANNER_BUTTON, 5).ifPresent(driverTools::click);
        }catch(NoSuchContextException e ){
            logger.log("No webview context found.");
        }finally{
            driverTools.switchToNative();
        }
        Locator orderIdDetailPage = new Locator("ORDER_DETAIL_ID", AppiumBy.xpath("//android.widget.TextView[contains(@text,'"+orderId+"')] | //span[contains(text(),'"+orderId+"')]"));
        boolean onOrderDetailPage = driverTools.waitForElementToAppear(orderIdDetailPage).isPresent();
        Assertion.assertThat(
                "Order detail page should be displayed after deeplink for orderId: " + orderId,
                onOrderDetailPage,
                is(true)
        );
    }

    private void closeGiveRatingPopupIfPresent() {
        logger.log("Not implemented in Android");
    }

    @Override
    @SneakyThrows
    public void openIssuedTicketViaDeeplink(String orderId) {
        String status=null;
        String orderHash=null;
        String orderDetailId=null;
        for(int tries = 60; tries>0; tries--) {
            JSONObject response = getInternalOrderData(orderId);
            GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = getVerticalInternalOrderDtata(response);
            orderHash = generalVerticalInternalOrderDetail.data.orderHash;
            orderDetailId = getCorrectVerticalOrderDetailId(generalVerticalInternalOrderDetail);
            status = generalVerticalInternalOrderDetail.data.orderStatus;
            logger.log("Status: "+status);
            if ("ETICKET_ISSUED".equalsIgnoreCase(status)) {
                Thread.sleep(5000); // this is added since sometimes it is not instantly synced between UI and response, e.g from API got issued but when deeplinking it is still in processing
                break;
            }
            Thread.sleep(3000);
        }
        if(status.equalsIgnoreCase("ETICKET_ISSUED")){
            openActiveTicketViaDeeplink(orderId,orderHash,orderDetailId);
        } else{
            throw new RuntimeException("ORDER ID: "+orderId+" is not in issued state, status: "+status);
        }
    }

    @Override
    @SneakyThrows
    public void openOrderDetailViaDeeplink(String orderId) {
        String status=null;
        String orderHash=null;
        String orderDetailId=null;
        for(int tries = 60; tries>0; tries--) {
            JSONObject response = getInternalOrderData(orderId);
            GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = getVerticalInternalOrderDtata(response);
            orderHash = generalVerticalInternalOrderDetail.data.orderHash;
            orderDetailId = getCorrectVerticalOrderDetailId(generalVerticalInternalOrderDetail);
            status = generalVerticalInternalOrderDetail.data.orderStatus;
            logger.log("Status: "+status);
            if ("ETICKET_ISSUED".equalsIgnoreCase(status) || "PROCESSING_ETICKET".equalsIgnoreCase(status) || "COMPLETED".equalsIgnoreCase(status) || "REFUND_COMPLETED".equalsIgnoreCase(status) || "SELECT_PAYMENT_METHOD".equalsIgnoreCase(status) || "WAITING_PAYMENT".equalsIgnoreCase(status) || "CANCELLED".equalsIgnoreCase(status) || "REFUNDED".equalsIgnoreCase(status) || "CANCEL_AFTER_PAID".equalsIgnoreCase(status)) {
                Thread.sleep(5000); // this is added since sometimes it is not instantly synced between UI and response, e.g from API got issued but when deeplinking it is still in processing
                break;
            }
            Thread.sleep(3000);
        }
        if("ETICKET_ISSUED".equalsIgnoreCase(status) || "PROCESSING_ETICKET".equalsIgnoreCase(status) || "COMPLETED".equalsIgnoreCase(status) || "REFUND_COMPLETED".equalsIgnoreCase(status) || "SELECT_PAYMENT_METHOD".equalsIgnoreCase(status) || "WAITING_PAYMENT".equalsIgnoreCase(status) || "CANCELLED".equalsIgnoreCase(status) || "REFUNDED".equalsIgnoreCase(status) || "CANCEL_AFTER_PAID".equalsIgnoreCase(status)){
            openActiveTicketViaDeeplink(orderId,orderHash,orderDetailId);
        } else{
            throw new RuntimeException("ORDER ID: "+orderId+" is not in issued state, status: "+status);
        }
    }

    @Override
    public void loginForOrderList(String username, String password) {
        LoginPage<T> loginPage = LoginPage.getInstance(session);
        loginPage.loginFromOrderPage(username, password);
    }

    @Override
    public String getRefundOrderTitle(String refundId){
        driverTools.swipeToElement(getRefundOrderTitleLocator(refundId),Swipe.UP,5);
        return driverTools.getText(getRefundOrderTitleLocator(refundId)).orElseThrow();
    }

    @Override
    public String getEstimatedTotalPayment(String refundId){
        driverTools.swipeToElement(getEstimatedPaymentLocator(refundId),Swipe.UP,5);
        return driverTools.getText(getEstimatedPaymentLocator(refundId)).orElseThrow();
    }

    private Locator getEstimatedPaymentLocator(String refundId){
        return new Locator("ESTIMATED_PAYMENT", AppiumBy.xpath("(//*[contains(@resource-id,'tv_header') and contains(@text,'Refund ID: "+refundId+"')]//following-sibling::*[contains(@resource-id,'ll_detail_content')]//*[contains(@resource-id,'fbl_subtitle')]//*[contains(@resource-id,'tv_title')])[2]"));
    }

    private Locator getRefundOrderTitleLocator(String refundId){
        return new Locator("REFUND_ORDER_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_header') and contains(@text,'Refund ID: "+refundId+"')]//following-sibling::*[contains(@resource-id,'tv_title')]"));
    }

    @Override
    public void swipeToActiveRefund(String refundId){
        driverTools.swipeToElement(refundIdTitle(refundId),Swipe.UP,5);
    }

    @Override
    public boolean verifyActiveRefundCard(String refundId,String expectedCurrency){
        boolean isRefundIdDisplayed = driverTools.swipeToElement(refundIdTitle(refundId),Swipe.UP,5).isPresent();
        logger.log("isRefundIdDisplayed: " + isRefundIdDisplayed);
        Assertion.assertThat("refund id displayed ",isRefundIdDisplayed,is(true));

        boolean isProductNameDisplayed = driverTools.swipeToElement(getRefundOrderTitleLocator(refundId),Swipe.UP,5).isPresent();
        logger.log("isProductNameDisplayed: " + isProductNameDisplayed);
        Assertion.assertThat("refund product name displayed ",isProductNameDisplayed,is(true));

        WebElement refundStatusElement = driverTools.swipeToElement(getRefundStatusLocator(refundId),Swipe.UP,5).orElseThrow();
        String refundStatus = driverTools.getText(refundStatusElement);
        if (!refundStatus.contains("Completed") && !refundStatus.contains("Selesai")) {
            logger.log("refundStatus: " + refundStatus);
            Assertion.assertThat("refund status displayed ",refundStatus, not(either(containsString("Completed")).or(containsString("Selesai"))));
        } else {
            WebElement refundDateElement = driverTools.swipeToElement(getRefundEstimatedDate(refundId),Swipe.UP,5).orElseThrow();
            String refundDate = driverTools.getText(refundDateElement);
            logger.log("refundDate: " + refundDate);
            Assertion.assertThat("is refund date less than 24 hours", CoreFunctions.isMoreThan24Hours(refundDate),is(false));
        }

        String currentCurrency = getEstimatedTotalPayment(refundId).split(" ")[0];
        logger.log("current currency : " + currentCurrency);
        Assertion.assertThat("is current currency is same as expected currency",currentCurrency,equalTo(expectedCurrency));
        return true;
    }

    private Locator getRefundStatusLocator(String refundID){
        return  new Locator("REFUND_STATUS", AppiumBy.xpath("//*[contains(@resource-id,'tv_header') and contains(@text,'Refund ID: "+refundID+"')]//following-sibling::*[contains(@resource-id,'tv_status')]"));
    }

    private Locator getRefundEstimatedDate(String refundId){
        return new Locator("REFUND_ESTIMATED_DATE", AppiumBy.xpath("(//*[contains(@resource-id,'tv_header') and contains(@text,'Refund ID: "+refundId+"')]//following-sibling::*[contains(@resource-id,'ll_detail_content')]//*[contains(@resource-id,'fbl_subtitle')]//*[contains(@resource-id,'tv_title')])[1]"));
    }
}