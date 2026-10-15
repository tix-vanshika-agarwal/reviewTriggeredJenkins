package com.tiket.page.order.orderlist;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.login.LoginPageIOS;
import com.tiket.page.order.orderdetail.OrderDetailPageIOS;
import com.tiket.page.order.orderhistory.OrderHistoryPage;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import com.tiket.pojo.order.GeneralVerticalInternalOrderDetail;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class OrderListPageIOS <T extends WebDriver> extends OrderListPage<T> {

    public OrderListPageIOS(Session<T> session) {
        super(session);
    }

    private static final Locator FIRST_WAITING_PAYMENT_ORDER = new Locator("FIRST_WAITING_PAYMENT_ORDER", AppiumBy.xpath("//XCUIElementTypeTable/XCUIElementTypeCell/XCUIElementTypeStaticText[@name='order_id']"));
    private static final Locator ORDER_DETAIL_BACK = new Locator("ORDER_DETAIL_BACK", AppiumBy.accessibilityId("back"));
    private static final Locator HISTORY_ICON = new Locator("HISTORY_ICON", AppiumBy.xpath("(//*[@label='Your Orders']//following::XCUIElementTypeButton)[1]"));
    private static final Locator SKIP = new Locator("SKIP", AppiumBy.xpath("//*[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lewati') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'skip')]"));
    private static final Locator HIDE_BANNER_BUTTON = new Locator("HIDE_BANNER_BUTTON", AppiumBy.xpath("//*[@label='Hide' or @label='Tutup']"));
    private static final Locator FIRST_CARD_ORDER_ID = new Locator("FIRST_CARD_ORDER_ID", AppiumBy.xpath("(//*[@name='order_id'])[1]"));
    private static final Locator ORDER_IDS = new Locator("ORDER_IDS", AppiumBy.accessibilityId("order_id"));
    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", AppiumBy.xpath("(//*[contains(@label,'IDR')])[1]"));
    private static final Locator PRODUCT_TITLE = new Locator("PRODUCT_TITLE", AppiumBy.xpath("(//*[@name='product_name'])[1]"));
    private static final Locator GIFT_ORDER_CARD = new Locator("GIFT_ORDER_CARD", AppiumBy.xpath("(//*[@name='order_card' and @type='XCUIElementTypeCell']//*[@name='product_name' and contains(@label, 'Gift Card')])[1]"));
    private static final Locator CANCEL_BTN = new Locator("CANCEL_BTN", AppiumBy.xpath("//*[@label='Cancel order' or @label='Batalkan pesanan' or @label='Cancel Order' or @label='Batalkan Pesanan']"));
    private static final Locator CHECKBOX_CANCEL_BTN = new Locator("CHECKBOX_CANCEL_BTN", AppiumBy.xpath("//*[@type='XCUIElementTypeOther' and @height>0 and ./parent::*[@type='XCUIElementTypeOther' and ((./preceding-sibling::* | ./following-sibling::*)[contains(@name,'E-ti') or contains(@name,'voucher') or contains(@name,'Tiket Points')]) and ./parent::*[@type='XCUIElementTypeOther']]]"));
    private static final Locator AGREE_CANCEL_BTN = new Locator("AGREE_CANCEL_BTN", AppiumBy.name("cancel_agree_button"));
    private static final Locator POPUP_SUCCESS_CANCEL = new Locator("POPUP_SUCCESS_CANCEL_BTN", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'pesanan berhasil dibatalkan dan dipindahkan ke riwayat pesanan.') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'your order was canceled successfully and moved to the order history.')]"));
    private static final Locator SKIP_INFORMATION_BTN = new Locator("SKIP_INFORMATION_BTN", AppiumBy.iOSClassChain("**/*[`name == 'coachmark_skip' or name == 'skip_button'`]"));
    private static final Locator NEXT_INFORMATION_BTN = new Locator("NEXT_INFORMATION_BTN", AppiumBy.name("coachmark_next"));
    private static final Locator OK_INFORMATION_BTN = new Locator("OK_INFORMATION_BTN", AppiumBy.name("coachmark_ok"));
    private static final Locator ACTIVE_ORDERS_BUTTON = new Locator("ACTIVE_ORDERS_BUTTON", AppiumBy.name("list_view"));
    private static final Locator LINK_TO_VIDEO = new Locator("LINK_TO_VIDEO", AppiumBy.xpath("//XCUIElementTypeButton[(contains(@label,'Link') and contains(@label,'Video')) or (contains(@label,'Menonton') or contains(@label,'Watch'))]"));
    private static final Locator ORDER_CARD = new Locator("ORDER_CARD", AppiumBy.accessibilityId("order_card"));
    private static final Locator ACTIVE_ORDER_PRODUCT_NAME = new Locator("ACTIVE_ORDER_PRODUCT_NAME", AppiumBy.xpath("//*[@name='order_card']//*[@name='product_name']"));
    private static final Locator ACTIVE_ORDER_SUB_CONTENT = new Locator("ACTIVE_ORDER_SUBCONTENT", AppiumBy.accessibilityId("product_description"));
    private static final Locator ACTIVE_ORDER_STATUS = new Locator("ACTIVE_ORDER_STATUS", AppiumBy.name("order_status"));
    private static final Locator ACTIVE_TICKET_TAB = new Locator("ACTIVE_TICKET_TAB", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Active Tickets') or contains(@label,'Tiket Aktif')]"));
    private static final Locator ONGOING_REFUND_TAB = new Locator("ONGOING_REFUND_TAB", AppiumBy.xpath("//*[contains(@name,'ongoing_refund')]"));
    private static final Locator APP_WINDOW = new Locator("APP_WINDOW", AppiumBy.className("XCUIElementTypeApplication"));
    private static final Locator REFUND_IDS = new Locator("REFUND_IDS", AppiumBy.accessibilityId("refund_id"));
    private static final Locator ESTIMATED_TOTAL_PAYMENT = new Locator("ESTIMATED_TOTAL_PAYMENT", AppiumBy.xpath("//*[@name='refund_status']//following-sibling::*[@name='refund_description'][2]"));
    private static final Locator HOTEL_REFUND_STATUS = new Locator("HOTEL_REFUND_STATUS", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@label, 'Diverifikasi hotel') or contains(@label, 'Verified by hotel')])[1]"));
    private static final Locator ORDER_CROSS_SELL_SEE_ALL_BTN = new Locator("ORDER_CROSS_SELL_SEE_ALL_BTN", AppiumBy.xpath("//*[contains(@name,'page_module_see_all_button') and following-sibling::*[contains(@name,'bn_chips_price')]]"));
    private static final Locator ORDER_CHIPS = new Locator("ORDER_CHIPS", AppiumBy.xpath("//*[*[contains(@name,'page_module_see_all_button')]]//*[contains(@name,'page_module_chip_text')]")) ;
    private static final Locator CROSS_SELL_INVENTORY_TITLE = new Locator("CROSS_SELL_INVENTORY_TITLE", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='product_name']"));
    private static final Locator CROSS_SELL_MODULE_NOT_LOADED = new Locator("CROSS_SELL_MODULE_NOT_LOADED", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Reload']"));
    private static final Locator OK_BTN = new Locator("OK_BTN", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'Ok') or contains(@label,'OK')]"));
    private static final Locator FINAL_STEPS_BOTTOMSHEET = new Locator("FINAL_STEPS_BOTTOMSHEET", AppiumBy.xpath("//XCUIElementTypeStaticText[(@name='Final steps to complete your trip' or contains(@name,'langkah terakhir') or contains(@name,'Langkah terakhir')) and @visible='true']"));
    private static final Locator LOGIN_BTN = new Locator("LOGIN_BTN", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@label,'Log in')])[1]"));
    private static final Locator GIFT_CARD_HEADER = new Locator("GIFT_CARD_HEADER", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Gift Card Details' or @label='Gift Card Details']"));
    private static final Locator GIFT_CARD_DETAILS = new Locator("GIFT_CARD_DETAILS", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Gift Card Sender' or @label='Gift Card Sender']"));
    private static final Locator GIFT_CARD_DETAILS_LINK = new Locator("GIFT_CARD_DETAILS_LINK", AppiumBy.xpath("//XCUIElementTypeOther[@name='Details' and ./XCUIElementTypeStaticText[@name='Details']] | (//XCUIElementTypeStaticText[@name='Details' and @label='Details'])[1]"));
    public static final Locator YOUR_ORDER_HEADER = new Locator("YOUR_ORDER_HEADER", AppiumBy.xpath("//XCUIElementTypeStaticText[translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='your orders']"));
    private static final Locator WAITING_PAYMENT_ORDER_SECTION = new Locator("WAITING_PAYMENT_ORDER_SECTION", AppiumBy.xpath("//*[contains(@name,'order_id')]/ancestor::XCUIElementTypeCell[1]"));
    private static final Locator WAITING_PAYMENT_SEE_ALL_BTN = new Locator("WAITING_PAYMENT_SEE_ALL_BTN", AppiumBy.name("waiting_seeall_btn"));

    @Override
    public boolean isOrderListPage() {
        if (driverTools.waitForElementToAppear(YOUR_ORDER_HEADER, 20).isEmpty()) {
          logger.softWarning("isOrderListPage: order list not shown after back click, retrying navigation");
          OrderHistoryPage.getInstance(session).clickBackBtnOnRefundDetailPage();
        }
        Assertion.assertThat("Order card should be displayed", driverTools.waitForElementToAppear(YOUR_ORDER_HEADER, 20).isPresent(), is(true));
        Assertion.assertThat("Active ticket tab should be displayed", driverTools.waitForElementToAppear(ACTIVE_TICKET_TAB).isPresent(), is(true));
        Assertion.assertThat("Ongoing refund tab should be displayed", driverTools.waitForElementToAppear(ONGOING_REFUND_TAB).isPresent(), is(true));
        return true;
    }

    @Override
    public Boolean checkIfOrderIdExist(String orderID) {
        driverTools.waitForElementToAppear(getOrderIdLocator(orderID));
        driverTools.swipeToFullyVisibleElement(getOrderIdLocator(orderID), WAITING_PAYMENT_ORDER_SECTION, Swipe.LEFT, 5);
        return driverTools.swipeToElement(getOrderIdLocator(orderID), WAITING_PAYMENT_ORDER_SECTION, Swipe.LEFT, 5).isPresent();
    }

    private Locator getOrderIdLocator(String orderID) {
        return new Locator("ORDER_ID_CARD", AppiumBy.xpath("//*[contains(@name,'order_id') and contains(@label,'" + orderID + "')]"));
    }

    @Override
    public String getTotalPriceByOrderId(String orderID) {
        return driverTools.getText(getTotalPriceByOrderIdLocator(orderID)).orElseThrow();
    }

    private Locator getTotalPriceByOrderIdLocator(String orderID) {
        return new Locator("TOTAL_PRICE_CARD", AppiumBy.xpath("//*[contains(@name,'order_id') and contains(@label,'"+orderID+"')]//following-sibling::*[contains(@name,'total_price')][1]"));
    }

    @Override
    public String getProductTitleByOrderId(String orderID) {
        return driverTools.getText(getProductTitleByOrderIdLocator(orderID)).orElseThrow();
    }

    private Locator getProductTitleByOrderIdLocator(String orderID) {
        return new Locator("PRODUCT_TITLE_CARD", AppiumBy.xpath("//*[contains(@name,'order_id') and contains(@label,'"+orderID+"')]//following-sibling::*[contains(@name,'product_name')][1]"));
    }

    @Override
    public String getProductTitleByOrderId(String orderID, String productName) {
        return driverTools.getText(getProductTitleByOrderIdLocator(orderID, productName)).orElseThrow();
    }

    private Locator getProductTitleByOrderIdLocator(String orderID, String productName) {
        return new Locator("PRODUCT_TITLE_CARD", AppiumBy.xpath("//*[contains(@name,'order_id') and contains(@label,'"+orderID+"')]/following-sibling::*[contains(@name,'product_name') and @label='"+productName+"']"));
    }

    @Override
    public void openFirstGiftCardOrder(){
        driverTools.switchToNative();
        driverTools.click(GIFT_ORDER_CARD);
    }


    private Locator completePaymentButton(String orderId) {
        return new Locator("COMPLETE_PAYMENT_BUTTON", AppiumBy.xpath("(//*[contains(@name,'order_id') and contains(@label,'"+orderId+"')]//following-sibling::*[contains(@name,'waiting_payment_btn') and (contains(@label,'Selesaikan') or contains(@label,'Complete'))])[1]"));
    }

    private Locator completePaymentButton(int index) {
        return new Locator("COMPLETE_PAYMENT_BUTTON_BY_INDEX", AppiumBy.xpath("(//*[contains(@name,'waiting_payment_btn') and (contains(@label,'Selesaikan') or contains(@label,'Complete'))])[" + index + "]"));
    }

    private Locator completePaymentButtonSeeAll(String orderId) {
        return new Locator("COMPLETE_PAYMENT_BUTTON_BOTTOMSHEET", AppiumBy.xpath("//*[@name='ic cancel grey']//following::*[contains(@name,'order_id') and contains(@label,'"+orderId+"')]//following-sibling::*[@name='waiting_payment_btn' and (contains(@label,'Selesaikan') or contains(@label,'Complete'))]"));
    }

    private Locator waitingPaymentCard(String orderId) {
        return new Locator("WAITING_PAYMENT_CARD", AppiumBy.xpath("//*[@name='order_id' and contains(@value,'"+orderId+"')]"));
    }

    private Locator refundIdTitle(String refundId) {
        return new Locator("REFUND_ID", AppiumBy.xpath("//XCUIElementTypeCell//*[contains(@label,'Refund ID: " + refundId + "')]"));
    }

    @Override
    public void openOrderCompletePayment(String orderId) {
        openActiveOrdersList();
        driverTools.swipeDown(2);
        boolean isFound = false;
        for(int tries = 5; tries>0; tries--) {
            isFound = checkIfOrderIdExist(orderId);
            if(driverTools.waitForElementToAppear(waitingPaymentCard(orderId),3).isPresent()){
                break;
            }
            driverTools.swipeDown(2);
        }
        if (isFound) {
            try {
                int index = 0;
                List<WebElement> orderIds = driverTools.elements(ORDER_IDS).orElseThrow();
                logger.log("Order IDs: " + orderIds.size());
                for(int i = 1; i <= orderIds.size(); i++) {
                    String orderIdText = driverTools.getText(orderIds.get(i-1));
                    logger.log("Order ID: " + orderIdText);
                    if(orderIdText.contains(orderId)) {
                        index = i;
                        logger.log("Index: " + index);
                        driverTools.click(completePaymentButton(index));
                        return;
                    }
                }
            } catch (Exception e) {
                logger.log("Error interacting with complete payment button: " + e.getMessage());
            }
        }
        logger.log("Opening from see all bottomsheet (Order not visible or error occurred)");
        openFromSeeAllBottomsheet(orderId);
    }

    private void openFromSeeAllBottomsheet(String orderId) {
        driverTools.swipeToElement(WAITING_PAYMENT_SEE_ALL_BTN, Swipe.UP, 1);
        driverTools.click(WAITING_PAYMENT_SEE_ALL_BTN);
        WebElement completePaymentButtonElement = driverTools.swipeToElement(completePaymentButtonSeeAll(orderId), Swipe.UP, 5).orElseThrow();
        driverTools.click(completePaymentButtonElement);
    }

    @Override
    public void openOrderWaitingPayment(String orderId) {
        driverTools.swipeDown(2);
        checkIfOrderIdExist(orderId);
        driverTools.clickElementByCoordinate(completePaymentButton(orderId));
    }

    @Override
    public void clickLinkToVideo() {
        WebElement webElement = driverTools.waitForElementToBeClickable(LINK_TO_VIDEO).orElseThrow();
        driverTools.click(webElement);
    }

    @Override
    public boolean verifyGiftOrderDetails(){
        driverTools.switchToNative();
        driverTools.swipeToElement(GIFT_CARD_HEADER, Swipe.UP, 5);
        boolean isHeaderShown = driverTools.waitForElementToAppear(GIFT_CARD_HEADER, 10).isPresent();
        boolean isDetailsShown = driverTools.waitForElementToAppear(GIFT_CARD_DETAILS, 10).isPresent();
        return isHeaderShown && isDetailsShown;
    }

    @Override
    public boolean verifyRedeemCodePresent() {
        // On iOS the order detail page is rendered natively; the gift card detail
        // (with the redeem code & share button) opens as a webview from the
        // "Details" link inside the gift card section.
        driverTools.switchToNative();
        WebElement detailsLink = driverTools.swipeToElement(GIFT_CARD_DETAILS_LINK, Swipe.UP, 5).orElseThrow();
        driverTools.click(detailsLink);

        driverTools.switchToWebViewUrl("gift-card");
        driverTools.waitForElementToPresence(REDEEM_CODE).orElseThrow();
        driverTools.waitForElementToBeClickable(SHARE_BUTTON);
        boolean isRedeemCodePresent = driverTools.isElementPresent(SHARE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SHARE_BUTTON);
        driverTools.switchToNative();
        return isRedeemCodePresent;
    }

    @Override
    public String getOrderId() {
        List<WebElement> orderIds = driverTools.waitForPresenceElementsList(ORDER_IDS).orElseThrow();
        int index = orderIds.size()-1;
        logger.log("selected index: " + index);
        return driverTools.getText(orderIds.get(index));
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
    public String findActiveTicket(String name, String detail, String orderID, int count) {
        logger.log("Searching for order name : " + name +" with detail : " + detail + " orderId: " + orderID);
        driverTools.swipeDown(5);
        driverTools.waitForElementToPresence(ORDER_CARD).orElseThrow();
        String orderStatus = "";

        for (int a = 1; a <= count; a++) {
            List<WebElement> activeOrdersByName = driverTools.elements(ACTIVE_ORDER_PRODUCT_NAME).orElseThrow();
            List<WebElement> activeOrdersBySubContent = driverTools.elements(ACTIVE_ORDER_SUB_CONTENT).orElseThrow();
            List<WebElement> activeOrderStatus = driverTools.elements(ACTIVE_ORDER_STATUS).orElseThrow();
            List<WebElement> activeOrderCard = driverTools.elements(ORDER_CARD).orElseThrow();
            logger.log("active orders by name: " + activeOrdersByName.size());
            logger.log("active orders by subcontent: " + activeOrdersByName.size());

            for (int x= 0; x<activeOrdersByName.size() && x<2 ; x++) {
                String activeOrderName = driverTools.getText(activeOrdersByName.get(x));
                if(!activeOrderName.toLowerCase().contains(name.toLowerCase())){
                    continue;
                }
                orderStatus = driverTools.getText(activeOrderStatus.get(x));
                driverTools.click(activeOrderCard.get(x));
                driverTools.waitForElementToAppear(SKIP,7).ifPresent(driverTools::click);
                driverTools.waitForElementToAppear(OK_BTN,5).ifPresent(driverTools::click);
                if(driverTools.getValueAttribute(OrderDetailPageIOS.ORDER_ID,"label").contains(orderID)) {
                    logger.log("opened order detail of order ->  " + orderID);
                    logger.log("orderStatus -> " + orderStatus);
                    return orderStatus;
                }

                driverTools.click(ORDER_DETAIL_BACK);
                driverTools.waitForElementToAppear(HISTORY_ICON);
            }
            driverTools.swipeUp();
            activeOrdersByName = driverTools.elements(ACTIVE_ORDER_PRODUCT_NAME).orElseThrow();
            activeOrdersBySubContent = driverTools.elements(ACTIVE_ORDER_SUB_CONTENT).orElseThrow();
            activeOrderStatus = driverTools.elements(ACTIVE_ORDER_STATUS).orElseThrow();
            activeOrderCard = driverTools.elements(ORDER_CARD).orElseThrow();
        }
        logger.log("orderStatus -> " + orderStatus);
        Assertion.assertThat("Order "+orderID+" found ", orderStatus, not(blankOrNullString()));
        return orderStatus;
    }

    private Locator getActiveOrderLocator(String name, String detail) {
        if (detail.equals("-")) {
            return new Locator("ORDER_NAME- "+name, AppiumBy.xpath("//XCUIElementTypeCell[contains(@name,'order_card') and ./XCUIElementTypeStaticText[contains(@label,'"+name+"') or contains(@label,'"+name.toLowerCase()+"')]]"));
        }

        return new Locator("ORDER_NAME- "+name+" ORDER_DETAIL- "+detail, AppiumBy.xpath("//XCUIElementTypeCell[contains(@name,'order_card') and ./XCUIElementTypeStaticText[contains(@label,'"+name+"') or contains(@label,'"+name.toLowerCase()+"')] and ./XCUIElementTypeStaticText[contains(@label,'"+detail+"')]]"));
    }

    private Locator getOrderStatus(String name, String detail, int index) {
        if (detail.equals("-")) {
            return new Locator("ORDER_STATUS- "+name, AppiumBy.xpath("(//XCUIElementTypeCell[contains(@name,'order_card') and ./XCUIElementTypeStaticText[contains(@label,'"+name+"') or contains(@label,'"+name.toLowerCase()+"')] and @visible='true'])["+index+"]//XCUIElementTypeStaticText[contains(@label,'issued') or contains(@label,'terbit') or contains(@label,'refund')]"));
        }

        return new Locator("ORDER_STATUS- "+name+" WITH_DETAIL- "+detail, AppiumBy.xpath("(//XCUIElementTypeCell[contains(@name,'order_card') and ./XCUIElementTypeStaticText[contains(@label,'"+name+"') or contains(@label,'"+name.toLowerCase()+"')] and ./XCUIElementTypeStaticText[contains(@label,'"+detail+"')] and @visible='true'])["+index+"]//XCUIElementTypeStaticText[contains(@label,'issued') or contains(@label,'terbit') or contains(@label,'refund')]"));
    }

    private Locator getActiveOrderIndex(String name, String detail, int index) {
        if (detail.equals("-")) {
            return new Locator("ORDER_NAME- "+name, AppiumBy.xpath("(//XCUIElementTypeCell[contains(@name,'order_card') and ./XCUIElementTypeStaticText[contains(@label,'"+name+"') or contains(@label,'"+name.toLowerCase()+"')] and @visible='true'])["+index+"]"));
        }

        return new Locator("ORDER_NAME- "+name+" ORDER_DETAIL- "+detail, AppiumBy.xpath("(//XCUIElementTypeCell[contains(@name,'order_card') and ./XCUIElementTypeStaticText[contains(@label,'"+name+"') or contains(@label,'"+name.toLowerCase()+"')] and ./XCUIElementTypeStaticText[contains(@label,'"+detail+"')] and @visible='true'])["+index+"]"));
    }

    @Override
    public void openOrderDetail(String productName) {
        WebElement orderDetail = driverTools.waitForElementToAppear(orderDetailLocator(productName), 10).orElseThrow();
        driverTools.click(orderDetail);
    }

    private Locator orderDetailLocator(String productName) {
        return new Locator("ORDER", AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`name == 'order_card'`]/XCUIElementTypeStaticText[`label == '"+productName+"'`]"));
    }

    @Override
    public void removePopUp() {
        driverTools.waitForElementToAppear(SKIP_INFORMATION_BTN).ifPresent(driverTools::click);
    }

    @Override 
    public void openActiveOrdersList() {
        removePopUp();
        driverTools.click(ACTIVE_ORDERS_BUTTON);
    }

    private Locator getOrderId(String expectedType,String type) {
        return new Locator(type,AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'" + expectedType + "')]"));
    }

    @Override
    public boolean matchOrderId(String expectedOrderId) {
        WebElement orderId = driverTools.swipeToElement(getOrderId(expectedOrderId,"orderId"), Swipe.RIGHT,5).orElseThrow();
        return driverTools.isElementDisplayed(orderId);
    }

    @Override
    public boolean matchDetails(String expectedType,String type){
        WebElement orderId = driverTools.swipeToElement(getOrderId(expectedType,type), Swipe.RIGHT,5).orElseThrow();
        boolean isSuccess = driverTools.isElementDisplayed(orderId);
        return isSuccess;
    }

    @Override
    public void openActiveTicketTab() {
        driverTools.waitForElementToPresence(ACTIVE_TICKET_TAB, 15).orElseThrow();
        driverTools.click(ACTIVE_TICKET_TAB);
    }

    @Override
    public void openOngoingRefundTab() {
        driverTools.waitForElementToPresence(HISTORY_ICON, 15).orElseThrow();
        driverTools.swipeByDistance(APP_WINDOW,Swipe.DOWN,0.5,2);
        driverTools.waitForElementToPresence(ONGOING_REFUND_TAB, 15).orElseThrow();
        driverTools.clickElementByCoordinate(driverTools.onScreenElement(ONGOING_REFUND_TAB));
    }

    @Override
    public boolean ongoingRefundTabStatus() {
        return driverTools.waitForElementToPresence(REFUND_IDS).isPresent();
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
        driverTools.swipeToElement(HOTEL_REFUND_STATUS, Swipe.UP, 10).orElseThrow();
    }

    @Override
    public String getRefundId() {
        String refundIdText = driverTools.getText(REFUND_IDS).orElseThrow();
        String[] refundIdArray = refundIdText.split(": ");
        String refundId = refundIdArray[1];
        logger.log("Refund ID Order List: " + refundId);
        return refundId;
    }

    @Override
    public String getEstimatedTotalPayment() {
        driverTools.swipeToElement(ESTIMATED_TOTAL_PAYMENT, Swipe.UP, 2).orElseThrow();
        return driverTools.getText(ESTIMATED_TOTAL_PAYMENT).orElseThrow();
    }

    @Override
    public String getRefundHotelName(String ProductName) {
        String hotelName = driverTools.getText(getrefundHotelNameLocator(ProductName)).orElseThrow();
        logger.log("Hotel Name Order List: " + hotelName);
        return hotelName;
    }

    private Locator getrefundHotelNameLocator(String productName) {
        return new Locator("REDUFUD_ORDER_HOTEL_NAME", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name=\"refund_id\"])[1]/preceding-sibling::XCUIElementTypeStaticText[@label='"+productName+"']"));
    }

    @Override
    public String getHotelRefundEstimatedPayment(String refundID) {
        String estimatedRefund = driverTools.getText(getrefundHotelPaymentLocator(refundID)).orElseThrow();
        logger.log("refund payment Order List: " + estimatedRefund);
        return estimatedRefund;
    }

    private Locator getrefundHotelPaymentLocator(String refundID) {
        return new Locator("REDUFUD_ORDER_PAYMENT", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@label,'"+refundID+"')])[1]//parent::*//XCUIElementTypeStaticText[contains(@label,'IDR')]"));
    }

    @Override
    public String getHotelRefundEstimatedDate(String refundID) {
        String estimatedDateText = driverTools.getText(getrefundHotelDateLocator(refundID)).orElseThrow();
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

    private Locator getrefundHotelDateLocator(String refundID) {
        return new Locator("REDUFUD_ORDER_PAYMENT", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@label,'"+refundID+"')])//parent::*//XCUIElementTypeStaticText[contains(@label,'202')]"));
    }

    @Override
    public String getHotelRefundStatus() {
        String status = driverTools.getText(HOTEL_REFUND_STATUS).orElseThrow();
        logger.log("Refund Status Order List: " + status);
        return status;
    }

    @Override
    public void openHotelRefund(String hotelName) {
        driverTools.click(getrefundHotelNameLocator(hotelName));
    }

    public void openOrderHistory(){
        driverTools.waitForElementToPresence(HISTORY_ICON).orElseThrow();
        driverTools.click(HISTORY_ICON);
    }

    private Locator getOrderByIdLocator(String orderId) {
        return new Locator("THE_ORDER", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`name == 'order_id' and label == '" + orderId + "'`]"));
    }

    @Override
    public void findOrder(String orderId){
        Locator elispeBtnLocator = new Locator("elipseBtnLocator", AppiumBy.xpath("//XCUIElementTypeCell[*[contains(@label,'"+orderId+"')]]//*[contains(@name,'ellipses')]"));
        driverTools.click(elispeBtnLocator);
    }

    @Override
    public void seeDetails() {
        List<WebElement> ordeIds = driverTools.elements(ORDER_IDS).orElseThrow();
        driverTools.click(ordeIds.get(0));
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
                    driverTools.waitForElementToAppear(SKIP, 7).ifPresent(driverTools::click);
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
        logger.log("checkboxes size: " + checkBoxes.size());
        WebElement agreeBtn = driverTools.waitForElementToAppear(AGREE_CANCEL_BTN).orElseThrow();
        driverTools.click(agreeBtn);

        boolean success = driverTools.waitForElementToPresence(POPUP_SUCCESS_CANCEL).isPresent();
        driverTools.swipeDown(3);

        return success;
    }
    /**
     * for searching unpaid order on order list page by order id
     * */
    @Override
    public void openOrderDetailWaitingForPayment(String orderID){
        logger.log("searching order id of : " + orderID);
        WebElement orderElement = driverTools.swipeToElement(getOrderWaitingForPaymentLocator(orderID),Swipe.LEFT,4).orElseThrow();
        driverTools.click(orderElement);
    }

    private Locator getOrderWaitingForPaymentLocator(String orderID){
        return new Locator("ORDER", AppiumBy.iOSClassChain("**/*[`name == 'order_id' AND label CONTAINS '"+orderID+"'`]"));
    }

    @Override
    public void refreshTheOrderList(int count) {
        driverTools.waitForElementToPresence(HISTORY_ICON).orElseThrow();
        for (int i = 0; i < count; i++) {
            driverTools.swipeDown();
        }
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
        boolean checkChipsPresent = driverTools.swipeToElement(ORDER_CHIPS, Swipe.UP, 5).isPresent();
        logger.log("is present chips present? : " + checkChipsPresent);
        while(!checkChipsPresent && reoloadYourOrderCounter<3){
            logger.log("reopen my order - " + reoloadYourOrderCounter);
            reopenYourOrderPage();
            checkChipsPresent =  driverTools.swipeToElement(ORDER_CHIPS, Swipe.UP, 5).isPresent();
            logger.log("is present chips present? : " + checkChipsPresent);
            reoloadYourOrderCounter = reoloadYourOrderCounter+1;
        }
        List<WebElement> orderChips = driverTools.elements(ORDER_CHIPS).orElseThrow();
        boolean isCrossSellChipsDisplayed = driverTools.isElementDisplayed(ORDER_CHIPS);
        Assertion.assertThat("Verify cross sell chips displayed",isCrossSellChipsDisplayed , is(true));

        driverTools.swipeToElement(CROSS_SELL_INVENTORY_TITLE, Swipe.UP, 2);
        String firstInventoryNameFirstChips = driverTools.getText(CROSS_SELL_INVENTORY_TITLE).orElseThrow();
        driverTools.click(orderChips.get(1));
        driverTools.swipeUp(2);
        driverTools.waitForElementToAppear(CROSS_SELL_MODULE_NOT_LOADED,2).ifPresent(driverTools::clickElementByCoordinate);
        String firstInventoryNameSecondChips = driverTools.getText(CROSS_SELL_INVENTORY_TITLE).orElseThrow();
        Assertion.assertThat("Chips clickable", firstInventoryNameFirstChips, is(not(equalTo(firstInventoryNameSecondChips))));

        boolean isCrossSellInvetoryPresent = driverTools.isElementDisplayed(CROSS_SELL_INVENTORY_TITLE);
        Assertion.assertThat("Verify cross sell inventory present", isCrossSellInvetoryPresent, is(true));
        return true;
    }
    @Override
    public boolean verifyCrossSellSeeAllBtn() {
        //check first chips section see all btn will redirect to ttd
        List<WebElement> orderChips = driverTools.elements(ORDER_CHIPS).orElseThrow();
        driverTools.click(orderChips.get(0));

        driverTools.swipeToElement(ORDER_CROSS_SELL_SEE_ALL_BTN, Swipe.UP, 2);
        driverTools.swipeUp();
        WebElement crossSeellBtn = driverTools.element(ORDER_CROSS_SELL_SEE_ALL_BTN).orElseThrow();
        Assertion.assertThat("Verify cross sell see all btn present", driverTools.isElementDisplayed(crossSeellBtn), is(true));

        driverTools.swipeToElement(ORDER_CROSS_SELL_SEE_ALL_BTN,Swipe.UP,1);
        driverTools.click(ORDER_CROSS_SELL_SEE_ALL_BTN);
        TTDSearchResultPage<T> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        ttdSearchResultPage.isSearchResultPageShown();
        boolean isTtdSrpShown = ttdSearchResultPage.isSearchResultPageShown();
        Assertion.assertThat("Verify ttd srp shown", isTtdSrpShown, is(true));
        ttdSearchResultPage.clickBack();
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
                Thread.sleep(8000); // this is added since sometimes it is not instantly synced between UI and response, e.g from API got issued but when deeplinking it is still in processing
                break;
            }
            Thread.sleep(1000);
        }
        if("ETICKET_ISSUED".equalsIgnoreCase(status)){
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
                Thread.sleep(8000); // this is added since sometimes it is not instantly synced between UI and response, e.g from API got issued but when deeplinking it is still in processing
                break;
            }
            Thread.sleep(1000);
        }
        if ("ETICKET_ISSUED".equalsIgnoreCase(status) || "PROCESSING_ETICKET".equalsIgnoreCase(status) || "COMPLETED".equalsIgnoreCase(status) || "REFUND_COMPLETED".equalsIgnoreCase(status) || "SELECT_PAYMENT_METHOD".equalsIgnoreCase(status) || "WAITING_PAYMENT".equalsIgnoreCase(status) || "CANCELLED".equalsIgnoreCase(status) || "REFUNDED".equalsIgnoreCase(status) || "CANCEL_AFTER_PAID".equalsIgnoreCase(status)) {
            openActiveTicketViaDeeplink(orderId,orderHash,orderDetailId);
        } else{
            throw new RuntimeException("ORDER ID: "+orderId+" is not in issued state, status: "+status);
        }
    }

    @Override
    public void openActiveTicketViaDeeplink(String orderId, String orderHash, String orderDetailId) {
        Deeplink deepLink = new Deeplink("ORDER_DETAIL_PAGE_"+orderId,
                "tiket://gatotkaca.tiket.com/yourorder/detail?orderId="+orderId+"&orderHash="+orderHash+"&orderDetailId="+orderDetailId,
                "tiket://preprod.tiket.com/yourorder/detail?orderId="+orderId+"&orderHash="+orderHash+"&orderDetailId="+orderDetailId,
                "tiket://www.tiket.com/yourorder/detail?orderId="+orderId+"&orderHash="+orderHash+"&orderDetailId="+orderDetailId);
        driverTools.deeplink(deepLink);
        driverTools.switchToNative();
        nextOkInformationPopup();
        closeGiveRatingPopupIfPresent();
        driverTools.waitForElementToAppear(HIDE_BANNER_BUTTON, 5).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(SKIP,15).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(OK_BTN,5).ifPresent(driverTools::click);
        // "View orders by destination" coachmark can show up late with Skip off-screen (not visible); only Next is tappable
        nextOkInformationPopup();
        driverTools.swipeDown(3);
        dismissFinalStepsBottomSheetIfPresent();

        boolean onOrderDetailPage = driverTools.waitForElementToAppear(OrderDetailPageIOS.ORDER_ID).isPresent();
        Assertion.assertThat(
                "Order detail page should be displayed after deeplink for orderId: " + orderId,
                onOrderDetailPage,
                is(true)
        );
    }

    private void dismissFinalStepsBottomSheetIfPresent() {
        if (driverTools.waitForElementToAppear(FINAL_STEPS_BOTTOMSHEET, 5).isPresent()) {
            driverTools.clickElementByCoordinate(APP_WINDOW);
        }
    }

    private void nextOkInformationPopup() {
        if (driverTools.waitForElementToPresence(NEXT_INFORMATION_BTN).isPresent())
            driverTools.click(NEXT_INFORMATION_BTN);
        if (driverTools.waitForElementToPresence(OK_INFORMATION_BTN).isPresent())
            driverTools.click(OK_INFORMATION_BTN);
    }

    private void closeGiveRatingPopupIfPresent() {
        if (driverTools.waitForElementToPresence(LoginPageIOS.REVIEW_APP_STORE, 5).isPresent()) {
            driverTools.clickElementByCoordinate(LoginPageIOS.REVIEW_APP_STORE);
        }
    }
    @Override
    public void loginForOrderList(String username, String password) {
        driverTools.waitForElementToAppear(LOGIN_BTN).ifPresent(driverTools::clickWithActions);
        LoginPage<T> loginPage = LoginPage.getInstance(session);
        loginPage.loginFromDifferentPages(username, password);
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
        return  new Locator("REFUND_STATUS", AppiumBy.xpath("//*[@name='refund_id' and contains(@label,'Refund ID: "+refundID+"')]//preceding-sibling::*[@name='refund_status']"));
    }

    private Locator getRefundEstimatedDate(String refundId){
        return new Locator("REFUND_ESTIMATED_DATE", AppiumBy.xpath("(//*[@name='refund_id' and contains(@label,'Refund ID: "+refundId+"')]//preceding-sibling::*[@name='refund_description'])[1]"));
    }

    @Override
    public String getRefundOrderTitle(String refundId){
        driverTools.swipeToElement(getRefundOrderTitleLocator(refundId),Swipe.UP,5);
        return driverTools.getText(getRefundOrderTitleLocator(refundId)).orElseThrow();
    }

    private Locator getRefundOrderTitleLocator(String refundId){
        return new Locator("REFUND_ORDER_TITLE", AppiumBy.xpath("//*[@name='refund_id' and contains(@label,'Refund ID: "+refundId+"')]//preceding-sibling::*[@name='product_name']"));
    }

    @Override
    public String getEstimatedTotalPayment(String refundId){
        driverTools.swipeToElement(getEstimatedPaymentLocator(refundId),Swipe.UP,5);
        return driverTools.getText(getEstimatedPaymentLocator(refundId)).orElseThrow();
    }

    private Locator getEstimatedPaymentLocator(String refundId){
        return new Locator("ESTIMATED_PAYMENT", AppiumBy.xpath("(//*[@name='refund_id' and contains(@label,'Refund ID: "+refundId+"')]//preceding-sibling::*[@name='refund_description'])[2]"));
    }
}
