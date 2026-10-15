package com.tiket.web.page.order.OrderList;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;

import javax.security.auth.login.FailedLoginException;

import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.gson.Gson;
import com.tiket.api.accommodation.hotel.PublishFinishHotelOrderApi;
import com.tiket.api.order.CompleteOrderListApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.order.orderlist.OrderListPage.OrderStatus;
import com.tiket.pojo.order.CompleteOrderListResponse;
import com.tiket.pojo.order.GeneralVerticalInternalOrderDetail;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dWeb.page.nft.car_transfer.CarTransferPage;
import com.tiket.web.dWeb.page.nft.train.TrainPage;
import com.tiket.web.dWeb.page.reschedule.ReschedulePage;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.platform.sphinx.refund.RefundPageWeb;

import lombok.SneakyThrows;

public abstract class OrderListWeb<T extends WebDriver> extends BasePage<T> {

    public OrderListWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> OrderListWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new OrderListDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new OrderListMweb<>(session);
            }
            default ->
                throw new NotImplementedException("Not implemented");
        }
    }

    public static final Locator RESERVATION_DETAIL_HEADER = new Locator("RESERVATION_DETAIL_HEADER", new By.ByXPath("//span[text()='Reservation Details' or text()='Detail Reservasi']"));
    private static final Locator ONGOING_REFUND_TAB = new Locator("ONGOING_REFUND_LIST", By.xpath("//*[contains(text(),'Ongoing Refund') or contains(text(),'Refund Berjalan')]"));
    private static final Locator SEE_DETAIL_BUTTON = new Locator("SEE_DETAIL_BUTTON", By.xpath("(//*[contains(@class, 'OrderCard_payment_options_wrapper')]//*[contains(text(), 'Detail')])[2]"));
    public static final Locator ELLIPSIS_BUTTON = new Locator("ELLIPSIS_BUTTON", By.xpath("(//*[name()='svg'][@data-testid='icon-more'])[1]"));
    public static final Locator CHECKBOX_CANCEL_BUTTON = new Locator("CHECKBOX_CANCEL_BUTTON", By.xpath("//span[contains (@class, 'checkbox')]"));
    public static final Locator AGREE_CANCEL_BUTTON = new Locator("AGREE_CANCEL_BUTTON", By.xpath("//*[text()='Setuju' or text()='Agree']"));
    public static final Locator POPUP_SUCCESS_CANCEL = new Locator("POPUP_SUCCESS_CANCEL", By.xpath("//div[contains (@class, 'Snackbar_text_content')]"));
    private static final Locator LATEST_ORDER_ID = new Locator("LATEST_ORDER_ID", By.xpath("(//span[contains(text(), 'Order ID:')])[1]"));
    private static final Locator ORDER_IDS = new Locator("ORDER_IDS", By.xpath("//span[contains(@class,'OrderCard_truncate') and contains(text(),'Order ID:')]"));
    private static final Locator ORDER_SUCCESSFUL = new Locator("ORDER_SUCCESSFUL", new By.ByXPath("(//*[normalize-space()='Order successful'])[2]"));
    private static final Locator GIFT_CARD_ORDER = new Locator("GIFT_CARD_ORDER", new By.ByXPath("//p[contains(normalize-space(.), 'Gift Card')]"));
    private static final Locator RESCHEDULE_BUTTON = new Locator("RESCHEDULE_BUTTON", new By.ByXPath("//div[contains(@class,'ActionButton_action_title')]//span[text()='Reschedule' or text()='Jadwal Ulang']"));
    private static final Locator YOUR_ORDERS_HEADER = new Locator("YOUR_ORDERS_HEADER", By.xpath("//span[text()='Your Orders']"));
    private static final Locator FIRST_ORDER_CARD = new Locator("FIRST_ORDER_CARD", By.xpath("(//div[contains(@class,'TiketCard_main_container')])[1]"));
    private static final Locator ORDER_HISTORY_HEADER_LINK = new Locator("ORDER_HISTORY_HEADER_LINK",
            By.xpath("//*[contains(@class,'Header_redirection_container')]"));
    private static final Locator HIDE_BUTTON = new Locator("HIDE_BUTTON", By.xpath("//p[normalize-space()='Hide' or normalize-space()='Tutup']"));
    private static final Locator SORT_BUTTON = new Locator("SORT_BUTTON",
            By.xpath("//button[contains(@class,'ActionChip_chip_button') and (.//span[normalize-space()='Sort' or normalize-space()='Urutkan'] or .//img[@alt='sort'])] | //div[contains(@class,'ActiveTikets_sort_action_container')]//button[contains(@class,'ActionChip_chip_button')]"));
    private static final Locator REFUND_STATUS = new Locator("REFUND_STATUS", By.xpath("//span[normalize-space()='Refund' or normalize-space()='Pengembalian dana' or normalize-space()='Pengembalian Dana']/following-sibling::span[1]"));
    public static final Locator INSURANCE_DETAILS = new Locator("INSURANCE_DETAILS", new By.ByXPath("//p[normalize-space()='Insurance Details' or normalize-space()='Detail Asuransi']"));
    private static final Locator TOTAL_AMOUNT = new Locator("TOTAL_AMOUNT", new By.ByXPath("//div[contains(@class,'TotalAmount_total_amount_and_label')]//span"));

    private static Locator getRefundStatusByRefundId(String refundId) {
        return new Locator("REFUNDID_" + refundId + "_STATUS", By.xpath("//*[contains(text(),'" + refundId + "')]/ancestor::*[contains(@class,'RefundCard_content_extra')]//*[contains(@class,'RefundCard_action')]"));
    }

    private static Locator getProductNameByRefundId(String refundId) {
        return new Locator("REFUNDID_" + refundId + "_PRODUCT_NAME", By.xpath("//*[contains(text(),'" + refundId + "')]/ancestor::*[contains(@class,'RefundCard_heading_wrapper')]//*[contains(@class,'RefundCard_heading')]"));
    }

    private static Locator getTotalRefundByRefundId(String refundId) {
        return new Locator("REFUNDID_" + refundId + "_TOTAL_REFUND", By.xpath("//*[contains(text(),'" + refundId + "')]/ancestor::*[contains(@class,'RefundCard_content_container')]//*[contains(text(),'total pembayaran') or contains(text(),'total payment')]/following-sibling::*[contains(@class,'RefundCard_price')]"));
    }

    private static Locator getReceiveDateByRefundId(String refundId) {
        return new Locator("REFUNDID_" + refundId + "_RECEIVE_DATE", By.xpath("//*[contains(text(),'" + refundId + "')]/ancestor::*[contains(@class,'RefundCard_content_container')]//*[contains(text(),'dana diterima') or contains(text(),'fund received')]/following-sibling::*[contains(@class,'RefundCard_price')]"));
    }

    protected abstract Locator getRefundIdByCurrency(String currency);

    public String getLatestOrderId() {
        WebElement latestOrder = driverTools.waitForElementToAppear(LATEST_ORDER_ID).orElseThrow();
        return driverTools.getText(latestOrder);
    }

    @SneakyThrows
    public JSONObject getInternalOrderData(String orderId) {
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        JSONObject response = orderDetailPage.getInternalOrderDetail(orderId);
        return response;
    }

    public GeneralVerticalInternalOrderDetail getVerticalInternalOrderDtata(JSONObject responseInternalOrder) {
        Gson gson = new Gson();
        return gson.fromJson(responseInternalOrder.toString(), GeneralVerticalInternalOrderDetail.class);
    }

    protected String getVerticalName(GeneralVerticalInternalOrderDetail orderDetailData) {
        String orderType = "";
        for (int i = 0; i < orderDetailData.data.orderDetails.size(); i++) {
            if (!orderDetailData.data.orderDetails.get(i).type.equals("insurance")) {
                return orderDetailData.data.orderDetails.get(i).type;
            }
        }
        return orderType;
    }

    public String getBookingCode(String orderId) {
        JSONObject internalOrderJson = getInternalOrderData(orderId);
        GeneralVerticalInternalOrderDetail internalOrderData = getVerticalInternalOrderDtata(internalOrderJson);
        String bookingCode = "";
        for (int i = 0; i < internalOrderData.data.orderDetails.size(); i++) {
            if (!internalOrderData.data.orderDetails.get(i).type.equalsIgnoreCase("insurance")) {
                bookingCode = internalOrderData.data.orderDetails.get(i).bookingCode;
            }
        }
        logger.log("Got booking code:" + bookingCode + " from order id: " + orderId);
        return bookingCode;
    }

    public String getCorrectVerticalOrderDetailId(GeneralVerticalInternalOrderDetail orderDetailData) {
        String insuranceOrderDetailId = "";
        for (int i = 0; i < orderDetailData.data.orderDetails.size(); i++) {
            GeneralVerticalInternalOrderDetail.OrderDetail detail = orderDetailData.data.orderDetails.get(i);
            if (!detail.type.equals("insurance")) {
                return detail.vertical.orderDetailId == null ? detail.orderDetailId : detail.vertical.orderDetailId;
            }
            if (insuranceOrderDetailId.isEmpty()) {
                insuranceOrderDetailId = detail.orderDetailId;
            }
        }
        return insuranceOrderDetailId;
    }

    public JSONObject getOrderDetailObjectByOrderDetailId(String orderId, String orderDetailId) {
        JSONObject internalOrderJson = getInternalOrderData(orderId);
        JSONArray orderDetailJsonArray = internalOrderJson.getJSONObject("data").getJSONArray("orderDetails");
        for (int i = 0; i < orderDetailJsonArray.length(); i++) {
            if (String.valueOf(orderDetailJsonArray.getJSONObject(i).getInt("orderDetailId")).equals(orderDetailId)) {
                return orderDetailJsonArray.getJSONObject(i);
            }
        }
        return new JSONObject();
    }

    private MapVerticalWithOrderId.TitleAndOrderId getOrderData(CompleteOrderListResponse.Card card) {
        if (card.orderData == null) return null;
        MapVerticalWithOrderId.TitleAndOrderId titleAndOrder = new MapVerticalWithOrderId.TitleAndOrderId();
        titleAndOrder.orderId = card.orderData.orderId;
        titleAndOrder.orderHash = card.orderData.orderHash;
        titleAndOrder.title = card.title;
        if (card.content != null
                && card.content.contents != null
                && !card.content.contents.isEmpty()
                && card.content.contents.get(0).subcontents != null
                && !card.content.contents.get(0).subcontents.isEmpty()) {
            titleAndOrder.subtitle = card.content.contents.get(0).subcontents.get(0).title;
        } else {
            titleAndOrder.subtitle = "";
        }
        JSONObject internalOrderJSON = getInternalOrderData(titleAndOrder.orderId);
        GeneralVerticalInternalOrderDetail internalOrderData = getVerticalInternalOrderDtata(internalOrderJSON);
        if (internalOrderData.data == null) {
            return null;
        }
        titleAndOrder.userLang = internalOrderData.data.userLang;
        titleAndOrder.internalOrderDetailJSONObject = internalOrderJSON.toString();
        titleAndOrder.verticalName = getVerticalName(internalOrderData);
        titleAndOrder.verticalName = titleAndOrder.normalizeVerticalName();
        if (internalOrderData.data.payment.isEmpty()) {
            titleAndOrder.currency = "";
            titleAndOrder.paymentSource = "";
        } else {
            titleAndOrder.currency = internalOrderData.data.payment.get(0).paymentCurrency;
            titleAndOrder.paymentSource = internalOrderData.data.payment.get(0).paymentSource;
        }
        titleAndOrder.userLang = internalOrderData.data.userLang;
        titleAndOrder.orderDetailId = getCorrectVerticalOrderDetailId(internalOrderData);
        titleAndOrder.orderNameDetail = internalOrderData.data.orderDetails.get(0).orderNameDetail;
        titleAndOrder.orderName = internalOrderData.data.orderDetails.get(0).orderName;
        titleAndOrder.totalPrice = String.valueOf(internalOrderData.data.price);

        titleAndOrder.orderStatus = internalOrderData.data.orderStatus;
        titleAndOrder.subOrderType = Objects.requireNonNullElseGet(card.orderData.subOrderType, () -> card.orderData.subOrderType = "");
        return titleAndOrder;
    }

    @SneakyThrows
    public MapVerticalWithOrderId.TitleAndOrderId findFirstOrderFilterByVertical(String accessToken, String username, String accountId, String vertical, String currency, boolean refundAble, boolean reschedulable,
            OrderStatus orderStatus) {
        // pojo of data
        MapVerticalWithOrderId.TitleAndOrderId mapVerticalWithOrderId = new MapVerticalWithOrderId.TitleAndOrderId();
        CompleteOrderListApi.Inputs inputs = new CompleteOrderListApi.Inputs(accessToken);
        CompleteOrderListApi CompleteOrderListApi = new CompleteOrderListApi(inputs);
        ApiResponse apiResponse = CompleteOrderListApi.hitApi(apiTools);
        CompleteOrderListResponse response = new Gson().fromJson(apiResponse.getBody(), CompleteOrderListResponse.class);
        CompleteOrderListResponse.View filteredCard = response.data.views.stream().filter(e -> e.id.equals("activeCards")).findFirst().orElseThrow();
        if (filteredCard.activeCards.cards.size() == 0) {
            return mapVerticalWithOrderId;
        }
        String verticall = vertical.equalsIgnoreCase("to_do") ? "ttd" : vertical.equalsIgnoreCase("airporttransfer") ? "airport_transfer" : vertical.equalsIgnoreCase("homes") ? "hotel_homes" : vertical.equalsIgnoreCase("car_rental") ? "car" : vertical;
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundPageWeb.CheckRefundAbleStatus filterRefundAble = refundPage.checkRefundAbleStatus(username, accountId);
        ReschedulePage<?> reschedulePage = new ReschedulePage<>(session);
        ReschedulePage.CheckRescheduleAble filterRescheduleAble = reschedulePage.checkRescheduleableStatus(username);
        try {
            mapVerticalWithOrderId = filteredCard.activeCards.cards.stream().map(d -> getOrderData(d)).filter(Objects::nonNull)
                    .filter(d -> d.verticalName != null && d.verticalName.equalsIgnoreCase(verticall) && (currency.equalsIgnoreCase("") || currency.equalsIgnoreCase(d.currency)))
                    .filter(d -> refundAble == false || (refundAble && filterRefundAble.check(d)))
                    .filter(d -> reschedulable == false || (filterRescheduleAble.check(d) && reschedulable))
                    .filter(d -> orderStatus.toString().equalsIgnoreCase(d.orderStatus))
                    .findFirst().orElse(null);
            return mapVerticalWithOrderId;
        } catch (Exception e) {
            logger.log("Error finding order: " + e.getMessage());
        }
        return null;
    }

    @SneakyThrows
    public MapVerticalWithOrderId.TitleAndOrderId findRefundableOrderByRefundMethod(String accessToken, String username, String accountId, boolean refundAble, String refundMethod) {
        String forceBank = refundMethod.equalsIgnoreCase("bank_transfer") ? "true" : "false";
        MapVerticalWithOrderId.TitleAndOrderId mapVerticalWithOrderId = new MapVerticalWithOrderId.TitleAndOrderId();
        CompleteOrderListApi.Inputs inputs = new CompleteOrderListApi.Inputs(accessToken);
        CompleteOrderListApi CompleteOrderListApi = new CompleteOrderListApi(inputs);
        ApiResponse apiResponse = CompleteOrderListApi.hitApi(apiTools);
        CompleteOrderListResponse response = new Gson().fromJson(apiResponse.getBody(), CompleteOrderListResponse.class);
        CompleteOrderListResponse.View filteredCard = response.data.views.stream().filter(e -> e.id.equals("activeCards")).findFirst().orElseThrow();
        if (filteredCard.activeCards.cards.size() == 0) {
            return mapVerticalWithOrderId;
        }
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundPageWeb.CheckRefundAbleStatus filterRefundAble = refundPage.checkRefundAbleStatus(username, accountId);
        try {
            mapVerticalWithOrderId = filteredCard.activeCards.cards.stream().map(d -> getOrderData(d))
                    .filter(Objects::nonNull)
                    .filter(d -> filterRefundAble.check(d))
                    .filter(d -> refundPage.getRefundMethodKey(d.orderId, d.orderDetailId, forceBank).equalsIgnoreCase(refundMethod))
                    .findFirst().get();
            return mapVerticalWithOrderId;
        } catch (NoSuchElementException e) {
            logger.log("No order with refund method: " + refundMethod + "found");
        }
        return null;
    }

    @SneakyThrows
    public MapVerticalWithOrderId.TitleAndOrderId findFirstOrderFilterByPaymentSource(String accessToken, String username, String accountId, String payment, String currency, boolean refundAble, boolean reschedulable, OrderStatus orderStatus) {
        // pojo of data
        MapVerticalWithOrderId.TitleAndOrderId mapVerticalWithOrderId = new MapVerticalWithOrderId.TitleAndOrderId();
        CompleteOrderListApi.Inputs inputs = new CompleteOrderListApi.Inputs(accessToken);
        CompleteOrderListApi CompleteOrderListApi = new CompleteOrderListApi(inputs);
        ApiResponse apiResponse = CompleteOrderListApi.hitApi(apiTools);
        CompleteOrderListResponse response = new Gson().fromJson(apiResponse.getBody(), CompleteOrderListResponse.class);
        CompleteOrderListResponse.View filteredCard = response.data.views.stream().filter(e -> e.id.equals("activeCards")).findFirst().orElseThrow();
        if (filteredCard.activeCards.cards.size() == 0) {
            return mapVerticalWithOrderId;
        }
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundPageWeb.CheckRefundAbleStatus filterRefundAble = refundPage.checkRefundAbleStatus(username, accountId);
        ReschedulePage<?> reschedulePage = new ReschedulePage<>(session);
        ReschedulePage.CheckRescheduleAble filterRescheduleAble = reschedulePage.checkRescheduleableStatus(username);
        try {
            mapVerticalWithOrderId = filteredCard.activeCards.cards.stream().map(d -> getOrderData(d)).filter(Objects::nonNull)
                    .filter(d -> currency.equalsIgnoreCase("") || currency.equalsIgnoreCase(d.currency))
                    .filter(d -> payment.equalsIgnoreCase(d.paymentSource))
                    .filter(d -> refundAble == false || (refundAble && filterRefundAble.check(d)))
                    .filter(d -> reschedulable == false || (filterRescheduleAble.check(d) && reschedulable))
                    .filter(d -> orderStatus.toString().equalsIgnoreCase(d.orderStatus))
                    .findFirst().get();
            return mapVerticalWithOrderId;
        } catch (NoSuchElementException e) {
            logger.log("No order found");
        }
        return null;
    }

    @SneakyThrows
    public MapVerticalWithOrderId.TitleAndOrderId findFirstOrderByStatus(
            String accessToken,
            String currency,
            OrderStatus orderStatus
    ) {
        MapVerticalWithOrderId.TitleAndOrderId mapVerticalWithOrderId = new MapVerticalWithOrderId.TitleAndOrderId();
        CompleteOrderListApi.Inputs inputs = new CompleteOrderListApi.Inputs(accessToken);
        CompleteOrderListApi completeOrderListApi = new CompleteOrderListApi(inputs);
        ApiResponse apiResponse = completeOrderListApi.hitApi(apiTools);
        CompleteOrderListResponse response = new Gson().fromJson(apiResponse.getBody(), CompleteOrderListResponse.class);
        CompleteOrderListResponse.View filteredCard = response.data.views.stream()
                .filter(e -> e.id.equals("activeCards")).findFirst().orElseThrow();
        if (filteredCard.activeCards.cards.isEmpty()) {
            return mapVerticalWithOrderId;
        }
        try {
            mapVerticalWithOrderId = filteredCard.activeCards.cards.stream()
                    .map(this::getOrderData)
                    .filter(Objects::nonNull)
                    .filter(d -> currency.equalsIgnoreCase("") || currency.equalsIgnoreCase(d.currency))
                    .filter(d -> orderStatus.toString().equalsIgnoreCase(d.orderStatus))
                    .filter(d -> !isPartiallyPaidHotelOrder(d))
                    .findFirst()
                    .get();
            return mapVerticalWithOrderId;
        } catch (NoSuchElementException e) {
            logger.log("No fully issued order found for status: " + orderStatus);
        }
        return null;
    }

    private boolean isPartiallyPaidHotelOrder(MapVerticalWithOrderId.TitleAndOrderId order) {
        String vertical = order.verticalName == null ? "" : order.verticalName;
        if (!vertical.equalsIgnoreCase("hotel") && !vertical.equalsIgnoreCase("hotel_homes")
                && !vertical.equalsIgnoreCase("homes")) {
            return false;
        }
        try {
            JSONObject data = new JSONObject(order.internalOrderDetailJSONObject).optJSONObject("data");
            if (data == null) {
                return true;
            }
            double price = data.optDouble("price", 0);
            JSONArray payments = data.optJSONArray("payment");
            if (payments == null || payments.length() == 0) {
                return true;
            }
            double paid = 0;
            for (int i = 0; i < payments.length(); i++) {
                paid += payments.getJSONObject(i).optDouble("paymentAmount", 0);
            }
            final double paymentTolerance = 1.0d;
            return paid + paymentTolerance < price;
        } catch (Exception e) {
            logger.log("Unable to check partial hotel payment for orderId: " + order.orderId
                    + " - " + e.getMessage());
            return true;
        }
    }

    public void reloadPage() {
        driver.navigate().refresh();
    }

    public void openActiveTicketViaURL(String orderId) {
        JSONObject response = getInternalOrderData(orderId);
        logger.log(String.valueOf(response));
        GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = getVerticalInternalOrderDtata(response);
        String orderHash = generalVerticalInternalOrderDetail.data.orderHash;
        String orderDetailId = getCorrectVerticalOrderDetailId(generalVerticalInternalOrderDetail);

        Deeplink deepLink = new Deeplink("ORDER_DETAIL_PAGE_" + orderId,
                "https://m.gatotkaca.tiket.com/yourorder/detail?orderId=" + orderId + "&orderHash=" + orderHash + "&orderDetailId=" + orderDetailId,
                "https://m.preprod.tiket.com/yourorder/detail?orderId=" + orderId + "&orderHash=" + orderHash + "&orderDetailId=" + orderDetailId,
                "https://m.tiket.com/yourorder/detail?orderId=" + orderId + "&orderHash=" + orderHash + "&orderDetailId=" + orderDetailId);
        if (Configs.ENVIRONMENT == Environment.PROD) {
            driverTools.get(deepLink.prodDeeplink());
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get(deepLink.preprodDeeplink());
        } else {
            driverTools.get(deepLink.gkDeeplink());
        }
        if (driverTools.waitForElementToBeClickable(HIDE_BUTTON, 5).isPresent()) {
            driverTools.clickWithJavascriptExecutor(HIDE_BUTTON);
            driverTools.waitForElementToDisappear(HIDE_BUTTON, 5);
        }
        if (!driverTools.isElementDisplayed(INSURANCE_DETAILS)) {
            driverTools.isElementDisplayed(RESERVATION_DETAIL_HEADER);
        }
    }

    public void openActiveTicketViaURL(String orderId,String orderHash, String orderDetailId) {
        Deeplink deepLink = new Deeplink("ORDER_DETAIL_PAGE_" + orderId,
                "https://m.gatotkaca.tiket.com/yourorder/detail?orderId=" + orderId + "&orderHash=" + orderHash + "&orderDetailId=" + orderDetailId,
                "https://m.preprod.tiket.com/yourorder/detail?orderId=" + orderId + "&orderHash=" + orderHash + "&orderDetailId=" + orderDetailId,
                "https://m.tiket.com/yourorder/detail?orderId=" + orderId + "&orderHash=" + orderHash + "&orderDetailId=" + orderDetailId);
        if (Configs.ENVIRONMENT == Environment.PROD) {
            driverTools.get(deepLink.prodDeeplink());
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get(deepLink.preprodDeeplink());
        } else {
            driverTools.get(deepLink.gkDeeplink());
        }
        driverTools.waitForElementToPresence(HIDE_BUTTON, 2).ifPresent(driverTools::click);
        driverTools.isElementDisplayed(RESERVATION_DETAIL_HEADER);
    }

    public HashMap<String, String> getOrderIdHashAndDetailId(String orderId) {
        HashMap<String, String> result = new HashMap<>();

        JSONObject response = getInternalOrderData(orderId);
        GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = getVerticalInternalOrderDtata(response);
        result.put("orderId", orderId);
        result.put("orderHash", generalVerticalInternalOrderDetail.data.orderHash);
        result.put("orderDetailId", getCorrectVerticalOrderDetailId(generalVerticalInternalOrderDetail));

        return result;
    }

    public String getActiveTicketURL(String orderId) {
        JSONObject response = getInternalOrderData(orderId);
        logger.log(String.valueOf(response));
        GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = getVerticalInternalOrderDtata(response);
        String orderHash = generalVerticalInternalOrderDetail.data.orderHash;
        String orderDetailId = getCorrectVerticalOrderDetailId(generalVerticalInternalOrderDetail);

        Deeplink deepLink = new Deeplink("ORDER_DETAIL_PAGE_" + orderId,
                "https://m.gatotkaca.tiket.com/yourorder/detail?orderId=" + orderId + "&orderHash=" + orderHash + "&orderDetailId=" + orderDetailId,
                "https://m.preprod.tiket.com/yourorder/detail?orderId=" + orderId + "&orderHash=" + orderHash + "&orderDetailId=" + orderDetailId,
                "https://m.tiket.com/yourorder/detail?orderId=" + orderId + "&orderHash=" + orderHash + "&orderDetailId=" + orderDetailId);
        if (Configs.ENVIRONMENT == Environment.PROD) {
            return deepLink.prodDeeplink();
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            return deepLink.preprodDeeplink();
        } else {
            return deepLink.gkDeeplink();
        }
    }

    @SneakyThrows
    public void openIssuedTicketViaURL(String orderId) {
        openIssuedTicketViaURL(orderId, 3);
    }

    @SneakyThrows
    public void openIssuedTicketViaURL(String orderId, int triesMax) {
        String orderHash = "";
        String orderDetailId = "";
        String status = "";

        for (int tries = 0; tries < triesMax; tries++) {
            JSONObject response = getInternalOrderData(orderId);
            GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = getVerticalInternalOrderDtata(response);
            orderHash = generalVerticalInternalOrderDetail.data.orderHash;
            orderDetailId = getCorrectVerticalOrderDetailId(generalVerticalInternalOrderDetail);
            status = generalVerticalInternalOrderDetail.data.orderStatus;
            logger.log("Order status: " + status);

            if (status.equalsIgnoreCase("ETICKET_ISSUED")) {
                break;
            }
            Thread.sleep(5000);
        }

        if (!status.equalsIgnoreCase("ETICKET_ISSUED")) {
            Assertion.fail("Order status is not ETICKET_ISSUED");
        }

        Thread.sleep(5000);

        Deeplink deepLink = new Deeplink("ORDER_DETAIL_PAGE_" + orderId,
                "https://m.gatotkaca.tiket.com/yourorder/detail?orderId=" + orderId + "&orderHash=" + orderHash + "&orderDetailId=" + orderDetailId,
                "https://m.preprod.tiket.com/yourorder/detail?orderId=" + orderId + "&orderHash=" + orderHash + "&orderDetailId=" + orderDetailId,
                "https://m.tiket.com/yourorder/detail?orderId=" + orderId + "&orderHash=" + orderHash + "&orderDetailId=" + orderDetailId);
        switch (Configs.ENVIRONMENT) {
            case PROD:
                driverTools.get(deepLink.prodDeeplink());
                break;
            case PREPROD:
                driverTools.get(deepLink.preprodDeeplink());
                break;
            case GK:
                driverTools.get(deepLink.gkDeeplink());
                break;
        }
        driverTools.isElementDisplayed(RESERVATION_DETAIL_HEADER);
    }

    @SneakyThrows
    public String getOrderStatus(String orderId) {
        JSONObject response = getInternalOrderData(orderId);
        GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = getVerticalInternalOrderDtata(response);
        return generalVerticalInternalOrderDetail.data.orderStatus;
    }

    @SneakyThrows
    public boolean waitForPaymentSuccessful(String orderId, int triesMax) {
        String status = "";

        for (int tries = 0; tries < triesMax; tries++) {
            JSONObject response = getInternalOrderData(orderId);
            GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = getVerticalInternalOrderDtata(response);
            status = generalVerticalInternalOrderDetail.data.orderStatus;
            logger.log("Order status: " + status);

            if (status.equalsIgnoreCase("ETICKET_ISSUED") || status.equalsIgnoreCase("PROCESSING_ETICKET")) {
                return true;
            }
            Thread.sleep(5000);
        }
        logger.log("Order " + orderId + " did not reach an issued status (ETICKET_ISSUED/PROCESSING_ETICKET) within "
                + triesMax + " attempts; last status seen: " + status);
        return false;
    }

    @SneakyThrows
    public void publishFinishHotelOrder(String orderId, String orderDetailId) {
        PublishFinishHotelOrderApi publishFinishHotelOrderApi = new PublishFinishHotelOrderApi(
                new PublishFinishHotelOrderApi.Inputs(orderId, orderDetailId));
        ApiResponse response = publishFinishHotelOrderApi.hitApi(apiTools);
        logger.log("Publish finish hotel order response: " + response.getStatusCode() + " - " + response.getBody());
        Assertion.assertThat("Publish finish hotel order API should succeed",
                response.getStatusCode(), Matchers.anyOf(Matchers.is(200), Matchers.is(201)));
    }

    @SneakyThrows
    public boolean waitForHotelOrderFinished(String orderId, int maxAttempts, int sleepSeconds) {
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            JSONObject response = getInternalOrderData(orderId);
            JSONArray orderDetails = response.getJSONObject("data").getJSONArray("orderDetails");
            for (int i = 0; i < orderDetails.length(); i++) {
                String status = orderDetails.getJSONObject(i).optString("status", "");
                logger.log("Hotel order detail status (attempt " + (attempt + 1) + "): " + status);
                if (status.toUpperCase().contains("FINISH")) {
                    return true;
                }
            }
            Thread.sleep(sleepSeconds * 1000L);
        }
        return false;
    }

    @SneakyThrows
    public ArrayList<String> getOrderDetailIdAndHashId(String orderId) {
        ArrayList<String> orderDetailIds = new ArrayList<>();
        JSONObject response = getInternalOrderData(orderId);
        GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = getVerticalInternalOrderDtata(response);
        String orderHash = generalVerticalInternalOrderDetail.data.orderHash;
        for (int i = 0; i < generalVerticalInternalOrderDetail.data.orderDetails.size(); i++) {
            orderDetailIds.add(String.valueOf(generalVerticalInternalOrderDetail.data.orderDetails.get(i).orderDetailId));
            logger.log("Got order detail id: " + generalVerticalInternalOrderDetail.data.orderDetails.get(i).orderDetailId + " from order id: " + orderId);
            logger.log("orderNameDetail is: " + generalVerticalInternalOrderDetail.data.orderDetails.get(i).orderNameDetail);
        }
        orderDetailIds.add(orderHash);
        return orderDetailIds;
    }

    /**
     * Resolves bus round-trip {@code orderDetailId}s by leg. Internal order {@code orderDetails} array order is not
     * guaranteed to be depart-first; use {@code vertical.busTrip} ({@code DEPART} / {@code RETURN}), with
     * {@code refId} prefix as fallback when {@code busTrip} is absent.
     */
    @SneakyThrows
    public BusRoundTripOrderDetailIds getBusRoundTripOrderDetailIdsByBusTrip(String orderId) {
        JSONObject response = getInternalOrderData(orderId);
        JSONObject data = response.getJSONObject("data");
        String orderHash = data.getString("orderHash");
        JSONArray orderDetails = data.getJSONArray("orderDetails");
        String departureOrderDetailId = null;
        String returnOrderDetailId = null;
        for (int i = 0; i < orderDetails.length(); i++) {
            JSONObject detail = orderDetails.getJSONObject(i);
            if (!"bus".equalsIgnoreCase(detail.optString("type", ""))) {
                continue;
            }
            String oidStr = String.valueOf(detail.getLong("orderDetailId"));
            JSONObject vertical = detail.optJSONObject("vertical");
            String trip = vertical != null ? vertical.optString("busTrip", "").trim() : "";
            if (trip.isEmpty()) {
                String refId = detail.optString("refId", "");
                String refUpper = refId.toUpperCase(Locale.ROOT);
                if (refUpper.startsWith("DEPART")) {
                    trip = "DEPART";
                } else if (refUpper.startsWith("RETURN")) {
                    trip = "RETURN";
                }
            }
            if ("DEPART".equalsIgnoreCase(trip)) {
                departureOrderDetailId = oidStr;
                logger.log("Bus DEPART orderDetailId=" + oidStr + " for orderId=" + orderId);
            } else if ("RETURN".equalsIgnoreCase(trip)) {
                returnOrderDetailId = oidStr;
                logger.log("Bus RETURN orderDetailId=" + oidStr + " for orderId=" + orderId);
            }
        }
        return new BusRoundTripOrderDetailIds(orderHash, departureOrderDetailId, returnOrderDetailId);
    }

    /** {@link #getBusRoundTripOrderDetailIdsByBusTrip(String)} result: hash plus one id per bus leg when present. */
    public static final class BusRoundTripOrderDetailIds {
        public final String orderHash;
        /** {@code vertical.busTrip} {@code DEPART} */
        public final String departureOrderDetailId;
        /** {@code vertical.busTrip} {@code RETURN} */
        public final String returnOrderDetailId;

        public BusRoundTripOrderDetailIds(String orderHash, String departureOrderDetailId, String returnOrderDetailId) {
            this.orderHash = orderHash;
            this.departureOrderDetailId = departureOrderDetailId;
            this.returnOrderDetailId = returnOrderDetailId;
        }
    }

    public String getHotelBookedOrderId(
            orderCreationPage<?> orderCreationPage,
            MapVerticalWithOrderId.TitleAndOrderId activeOrderData,
            String email,
            String password,
            String startDate,
            String hotelName,
            int adult,
            int night,
            int room,
            String orderFullName,
            String orderNameTitle,
            String guestFullName,
            String guestTitle
    ) {
        if (activeOrderData == null) {
            orderCreationPage.createHotelSakukuIssuedOrderApi(
                    email, password, startDate, hotelName, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle, "IDR"
            );
            logger.log("Found Order Id from active order data: " + orderCreationPage.getOrderId());
            return String.valueOf(orderCreationPage.getOrderId());
        } else {
            return activeOrderData.orderId;
        }
    }

    public String getHotelBookedOrderIdCard(
            orderCreationPage<?> orderCreationPage,
            MapVerticalWithOrderId.TitleAndOrderId activeOrderData,
            String email,
            String password,
            String startDate,
            String hotelName,
            int adult,
            int night,
            int room,
            String currency,
            String orderFullName,
            String orderNameTitle,
            String guestFullName,
            String guestTitle,
            String cardNumber,
            String expiry,
            String cvv,
            String cardName
    ) {
        if (activeOrderData == null) {
            orderCreationPage.createHotelCreditCardIssuedOrderApi(email, password, startDate, hotelName, adult, night, room,
                    currency, orderFullName, orderNameTitle, guestFullName, guestTitle, cardNumber, expiry, cvv, cardName);
            logger.log("Found Order Id from active order data: " + orderCreationPage.getOrderId());
            return String.valueOf(orderCreationPage.getOrderId());
        } else {
            return activeOrderData.orderId;
        }
    }

    public String getCarBookedOrderId(
            CarTransferPage<?> carTransferPage,
            MapVerticalWithOrderId.TitleAndOrderId activeOrderData,
            String username,
            String password,
            String adminName,
            String adminPhoneNumber,
            String origin,
            String destination,
            String type,
            String rangeAddDate,
            String pickupTime,
            int pax
    ) {
        if (activeOrderData == null) {
            return carTransferPage.createIssuedOrderApi(
                    username, password, adminName, adminPhoneNumber,
                    origin, destination, type, rangeAddDate, pickupTime, pax);
        } else {
            return activeOrderData.orderId;
        }
    }

    public String getTrainBookedOrderId(
            TrainPage<?> trainPage,
            MapVerticalWithOrderId.TitleAndOrderId activeOrderData,
            String email,
            String password,
            String name,
            String phone,
            String idNumber,
            String originCity,
            String destinationCity,
            String startDate,
            String tripType,
            int adultCount,
            int infantCount
    ) {
        if (activeOrderData == null) {
            return trainPage.createIssuedOrderApi(
                    email, password, name, phone, idNumber,
                    originCity, destinationCity, startDate,
                    tripType, adultCount, infantCount);
        } else {
            return activeOrderData.orderId;
        }
    }

    public String getTodoBookedOrderId(
            orderCreationPage<?> orderCreationPage,
            MapVerticalWithOrderId.TitleAndOrderId activeOrderData,
            String email,
            String password,
            String currency,
            String productUrlName,
            boolean refundable,
            String contactFullName,
            String contactSalutation,
            String guestFullName,
            String guestEmailAddress,
            String guestSalutation,
            String cardNumber,
            String expiry,
            String cvv,
            String cardName
    ) {
        String orderId = "";
        if (activeOrderData == null) {
            orderCreationPage.createIssuedOrderApiTodoCard(email, password, currency, productUrlName, refundable,
                    contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, cardNumber, expiry, cvv, cardName);
            orderId = String.valueOf(orderCreationPage.getOrderId());
        } else {
            orderId = activeOrderData.orderId;
        }
        logger.log("Get To-Do Order ID: " + orderId);
        return orderId;
    }

    public String getTTDBookedOrderId(
            orderCreationPage<?> orderCreationPage,
            MapVerticalWithOrderId.TitleAndOrderId activeOrderData,
            String email,
            String password,
            String currency,
            String productUrlName,
            boolean refundable,
            String contactFullName,
            String contactSalutation,
            String guestFullName,
            String guestEmailAddress,
            String guestSalutation
    ) {
        String orderId = "";
        if (activeOrderData == null) {
            orderCreationPage.createIssuedOrderApiTodo(email, password, productUrlName, refundable, currency, contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation);
            orderId = String.valueOf(orderCreationPage.getOrderId());
        } else {
            orderId = activeOrderData.orderId;
        }
        logger.log("Get To-Do Order ID: " + orderId);
        return orderId;
    }

    public String getCarRentalBookedOrderId(
            orderCreationPage<?> orderCreationPage,
            MapVerticalWithOrderId.TitleAndOrderId activeOrderData,
            String email,
            String phoneNumber,
            String password,
            String driverOption,
            String location,
            String startDate,
            String pickupTime,
            String type,
            String fullName,
            String nameTitle,
            String adminName,
            String adminPhoneNumber,
            int rentalDuration
    ) {
        if (activeOrderData == null) {
            orderCreationPage.createIssuedOrderApiCar(
                    email, phoneNumber, password, driverOption, location, startDate, pickupTime,
                    type, fullName, nameTitle, adminName, adminPhoneNumber, rentalDuration
            );
            return String.valueOf(orderCreationPage.getOrderId());
        } else {
            return activeOrderData.orderId;
        }
    }

    public String getATBookedOrderId(
            orderCreationPage<?> orderCreationPage,
            MapVerticalWithOrderId.TitleAndOrderId activeOrderData,
            String email,
            String password,
            String adminName,
            String adminPhoneNumber,
            String origin,
            String destination,
            String type,
            String rangeAddDate,
            String pickupTime,
            int pax
    ) {
        if (activeOrderData == null) {
            CarTransferPage<?> carTransferPage = new CarTransferPage<>(session);
            String orderId = carTransferPage.createIssuedOrderApi(
                    email, password, adminName, adminPhoneNumber,
                    origin, destination, type, rangeAddDate, pickupTime, pax);
            logger.log("Created AT order Id: " + orderId);
            return orderId;
        } else {
            return activeOrderData.orderId;
        }
    }

    public abstract void openOrderHistoryPage();

    public void openOngoingRefundList() {
        driverTools.waitForElementToAppear(ONGOING_REFUND_TAB, 3).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(ONGOING_REFUND_TAB + " element not found");
        });
    }

    public String getCurrentUrl() {
        return driverTools.getCurrentURL();
    }

    public String getFirstRefundIdByCurrency(String currency) {
        WebElement refundCard = driverTools.waitForElementToAppear(getRefundIdByCurrency(currency)).orElseThrow(() -> {
            Assertion.skip("No ongoing refund list for currency: " + currency);
            return new org.openqa.selenium.NoSuchElementException("No ongoing refund list for currency: " + currency);
        });
        String refundId = driverTools.getText(refundCard);
        refundId = CoreFunctions.getIntFromString(refundId).orElseThrow().toString();
        logger.log("Got Refund Id by currency " + currency + " on past refund list: " + refundId);
        driverTools.scrollIntoWebView(refundCard);
        return refundId;
    }

    @SneakyThrows
    public String getRefundStatusCard(String refundId) {
        for (int tries = 0; tries < 3; tries++) {
            try {
                WebElement refundStatus = driverTools.waitForElementToAppear(getRefundStatusByRefundId(refundId), 2).orElseThrow();
                return driverTools.getText(refundStatus);
            } catch (Exception e) {
                logger.log("Refund status not found, retrying...");
                Thread.sleep(2000);
                driverTools.refreshWebpage();
                openOngoingRefundList();
            }
        }
        throw new NoSuchElementException("Refund status not found");
    }


    public String getProductNameCard(String refundId) {
        WebElement productName = driverTools.waitForElementToAppear(getProductNameByRefundId(refundId)).orElseThrow();
        return driverTools.getText(productName);
    }

    public String getReceiveDateCard(String refundId) {
        WebElement receiveDate = driverTools.waitForElementToAppear(getReceiveDateByRefundId(refundId)).orElseThrow();
        return driverTools.getText(receiveDate);
    }

    public String getTotalRefundAmountCard(String refundId) {
        WebElement refundAmount = driverTools.waitForElementToAppear(getTotalRefundByRefundId(refundId)).orElseThrow();
        return driverTools.getText(refundAmount);
    }

    public void clickOnRefundCard(String refundId) {
        driverTools.click(getProductNameByRefundId(refundId));
    }

    public void openOrderDetailPage(String orderId) {
        Locator ELLIPSIS_BUTTON = getEllipsisButtonLocator(orderId);
        driverTools.waitForElementToAppear(ELLIPSIS_BUTTON);
        driverTools.click(ELLIPSIS_BUTTON);
        driverTools.waitForElementToAppear(SEE_DETAIL_BUTTON);
        driverTools.clickWithJavascriptExecutor(SEE_DETAIL_BUTTON);
    }

    private Locator getEllipsisButtonLocator(String orderId) {
        return new Locator("ELLIPSIS_BUTTON", new By.ByXPath("//*[contains(text(), '" + orderId + "')]/following-sibling::*[contains(@class, 'OrderCard_icon_more')]"));
    }

    public String getFerryBookedOrderId(
            orderCreationPage<?> orderCreationPage,
            MapVerticalWithOrderId.TitleAndOrderId activeOrderData,
            String username,
            String password,
            String origin,
            String destination,
            String departureDate,
            String departureTime,
            String vehicleType,
            int adult,
            int child,
            int infant,
            int elderly
    ) {
        if (activeOrderData == null) {
            orderCreationPage.createIssuedOrderApiFerry(
                    username, password, origin, destination, departureDate, departureTime, vehicleType,
                    adult, child, infant, elderly
            );
            return String.valueOf(orderCreationPage.getOrderId());
        } else {
            return activeOrderData.orderId;
        }
    }

    public String getFlightBookedOrderId(
            orderCreationPage<?> orderCreationPage,
            MapVerticalWithOrderId.TitleAndOrderId activeOrderData,
            String username,
            String password,
            String originStation,
            String destinationStation,
            String departureDate,
            String searchType,
            int adult,
            int child,
            int infant,
            String airline,
            String[] passengerNames,
            String phoneNumber
    ) {
        if (activeOrderData == null) {
            orderCreationPage.createOrderFlightB2CDomestic(
                    username, password, originStation, destinationStation, departureDate, searchType,
                    adult, child, infant, airline, passengerNames, phoneNumber
            );
            return String.valueOf(orderCreationPage.getOrderId());
        } else {
            return activeOrderData.orderId;
        }
    }

    public abstract boolean cancelOrder();

    public void openActiveTicketViaURL(String orderId, String tripType) {
        JSONObject response = getInternalOrderData(orderId);
        GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = getVerticalInternalOrderDtata(response);
        String orderHash = generalVerticalInternalOrderDetail.data.orderHash;

        String orderDetailId = "";

        for (GeneralVerticalInternalOrderDetail.OrderDetail detail : generalVerticalInternalOrderDetail.data.orderDetails) {
            if ("train".equalsIgnoreCase(detail.type)
                    && detail.vertical != null
                    && tripType.equalsIgnoreCase(detail.vertical.tripType)) {

                orderDetailId = String.valueOf(detail.orderDetailId);
                break;
            }
        }

        if (orderDetailId.isEmpty()) {
            throw new RuntimeException("No orderDetail found for tripType: " + tripType);
        }

        Deeplink deepLink = new Deeplink(
                "ORDER_DETAIL_PAGE_" + orderId,
                "https://m.gatotkaca.tiket.com/yourorder/detail?orderId=" + orderId + "&orderHash=" + orderHash + "&orderDetailId=" + orderDetailId,
                "https://m.preprod.tiket.com/yourorder/detail?orderId=" + orderId + "&orderHash=" + orderHash + "&orderDetailId=" + orderDetailId,
                "https://m.tiket.com/yourorder/detail?orderId=" + orderId + "&orderHash=" + orderHash + "&orderDetailId=" + orderDetailId
        );

        if (Configs.ENVIRONMENT == Environment.PROD) {
            driverTools.get(deepLink.prodDeeplink());
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get(deepLink.preprodDeeplink());
        } else {
            driverTools.get(deepLink.gkDeeplink());
        }
        driverTools.isElementDisplayed(RESERVATION_DETAIL_HEADER);
    }

    public void verifyID(String orderIdCreated) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> orderIDs = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(ORDER_IDS.by())
        );
        List<String> foundIds = new ArrayList<>();
        for (WebElement orderID : orderIDs) {
            String fullText = driverTools.getText(orderID);
            String numberOnly = fullText.replace("Order ID:", "").trim();
            foundIds.add(numberOnly);
        }
        Assertion.assertThat("Verify order ID is present in order list", foundIds, Matchers.hasItem(orderIdCreated));
    }
    public String getRefundStatus() {
        return driverTools.getText(REFUND_STATUS).orElse("");
    }

    public boolean isOrderIssued() {
        driverTools.waitForElementToPresence(ORDER_SUCCESSFUL, 20);
        driverTools.waitForElementToPresence(GIFT_CARD_ORDER, 10);
        return driverTools.isElementPresent(ORDER_SUCCESSFUL) && driverTools.isElementPresent(GIFT_CARD_ORDER);
    }

    public void loginFromOrderDetailPage(String username, String password, String otp, int retryCounter) throws FailedLoginException, InterruptedException {
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        loginPage.inputEmail(username);
        loginPage.inputPassword(password);
        loginPage.inputOTPCode(otp, retryCounter);
    }

    public void clickRescheduleButton() {
        driverTools.waitForElementToAppear(RESCHEDULE_BUTTON);
        driverTools.clickWithJavascriptExecutor(RESCHEDULE_BUTTON);
    }
    public String getTotalAmount() {
        try {
        return driverTools.getAttribute(
                getTotalAmountLocator().by(), "textContent").trim();
        } catch(Exception e) {
            logger.log("failed to get attribute, trying to get the text natively. " + e);
            return driverTools.getText(TOTAL_AMOUNT).orElseThrow();
        }
    }

    private Locator getTotalAmountLocator() {
        return new Locator("TOTAL_AMOUNT", new By.ByXPath("//div[contains(@class,'TotalAmount_total_amount_and_label')]//span"));
    }

    public boolean isOrderListPageDisplayed() {
        return driverTools.isElementDisplayed(YOUR_ORDERS_HEADER);
    }

    public void clickFirstOrderCard() {
        driverTools.waitForElementToPresence(FIRST_ORDER_CARD);
        driverTools.click(FIRST_ORDER_CARD);
    }

    private static Locator getSortActionLocator(String sortBy) {
        String testId;
        switch (sortBy.toLowerCase(Locale.ROOT)) {
            case "dot" -> testId = "sort-option-EARLIEST_TRIP_DATE";
            case "latesttx" -> testId = "sort-option-RECENTLY_PURCHASED";
            default -> throw new IllegalArgumentException(
                    "Unsupported sort option: " + sortBy + ". Expected 'dot' or 'latestTx'.");
        }
        return new Locator("SORT_OPTION_" + testId, By.cssSelector("div[data-testid='" + testId + "']"));
    }

    public void clickSortButton() {
        driverTools.waitForElementToAppear(SORT_BUTTON);
        driverTools.click(SORT_BUTTON);
    }

    public void clickSortAction(String sortBy) {
        Locator sortOption = getSortActionLocator(sortBy);
        driverTools.waitForElementToAppear(sortOption);
        driverTools.click(sortOption);
    }

    public void waitSeconds(int seconds) {
        driverTools.dummyWait(seconds);
    }


    public void openOrderHistoryViaHeader() {
        driverTools.waitForElementToAppear(ORDER_HISTORY_HEADER_LINK);
        driverTools.refreshWebpage();
        WebElement orderHistoryLink = driverTools.waitForElementToAppear(ORDER_HISTORY_HEADER_LINK).orElseThrow();
        driverTools.click(orderHistoryLink);
    }

    private Locator getOrderHistoryRefundInfoLocator(String hotelName) {
        String hotelNameLiteral = CoreFunctions.toXPathLiteral(hotelName);
        return new Locator("ORDER_HISTORY_REFUND_INFO_" + hotelName.hashCode(),
                By.xpath("//*[contains(@class,'TiketCard_main_container') and .//*[contains(@class,'TiketCard_text_black') and normalize-space()=" + hotelNameLiteral + "]]//*[contains(@class,'TiketCard_action_wrapper')]"));
    }

    private Locator getOrderHistoryRefundStatusLocator(String hotelName) {
        String hotelNameLiteral = CoreFunctions.toXPathLiteral(hotelName);
        return new Locator("ORDER_HISTORY_REFUND_STATUS_" + hotelName.hashCode(),
                By.xpath("//*[contains(@class,'TiketCard_main_container') and .//*[contains(@class,'TiketCard_text_black') and normalize-space()=" + hotelNameLiteral + "]]//*[contains(@class,'TiketCard_eticket_text_wrapper')]"));
    }

    public String getOrderHistoryRefundInfoByHotelName(String hotelName) {
        Locator refundInfoLocator = getOrderHistoryRefundInfoLocator(hotelName);
        driverTools.scrollUntilElementFound(refundInfoLocator);
        return CoreFunctions.normalizeNonBreakingSpaceText(driverTools.getText(refundInfoLocator).orElseThrow());
    }

    public String getOrderHistoryRefundStatusByHotelName(String hotelName) {
        Locator refundStatusLocator = getOrderHistoryRefundStatusLocator(hotelName);
        driverTools.scrollUntilElementFound(refundStatusLocator);
        return CoreFunctions.normalizeNonBreakingSpaceText(driverTools.getText(refundStatusLocator).orElseThrow());
    }

    public void verifyOrderHistoryCardMatchingBookingData(String hotelName, String checkInDate, String nightLabel) {
        String normalizedCheckInDate = CoreFunctions.normalizeNonBreakingSpaceText(checkInDate).toLowerCase(Locale.ROOT);
        String normalizedNightLabel = CoreFunctions.normalizeNonBreakingSpaceText(nightLabel).toLowerCase(Locale.ROOT);
        int maxAttempts = 5;
        String normalizedRefundInfo = "";

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            normalizedRefundInfo = CoreFunctions.normalizeNonBreakingSpaceText(
                    getOrderHistoryRefundInfoByHotelName(hotelName)).toLowerCase(Locale.ROOT);

            if (normalizedRefundInfo.contains(normalizedCheckInDate)
                    && normalizedRefundInfo.contains(normalizedNightLabel)) {
                logger.log("Order history card matched booking data on attempt " + attempt + " of " + maxAttempts);
                break;
            }

            if (attempt < maxAttempts) {
                logger.log("Order history card does not match booking data on attempt " + attempt
                        + " of " + maxAttempts + " — refreshing page");
                driverTools.refreshWebpage();
                driverTools.dummyWait(3);
            }
        }

        Assertion.assertThat(
                "Order history refund info should contain check-in date: " + checkInDate,
                normalizedRefundInfo, Matchers.containsString(normalizedCheckInDate));
        Assertion.assertThat(
                "Order history refund info should contain night label: " + nightLabel,
                normalizedRefundInfo, Matchers.containsString(normalizedNightLabel));
    }
}
