package com.tiket.page.order.orderlist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tiket.api.order.CompleteOrderListApi;
import com.tiket.api.order.YourOrderComposerGET;
import com.tiket.api.refund.GETRefundAbleStatus;
import com.tiket.api.reschedule.GETRescheduleableStatus;
import com.tiket.apiPage.OrderApi;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.refund.RefundPage;
import com.tiket.page.reschedule.ReschedulePage;
import com.tiket.pojo.order.CompleteOrderListResponse;
import com.tiket.pojo.order.GeneralVerticalInternalOrderDetail;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.pojo.order.MapVerticalWithOrderId.TitleAndOrderId;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public abstract class OrderListPage<T extends WebDriver> extends BasePage<T> {

    public OrderListPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> OrderListPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new OrderListPageAndroid<>(session);
            }
            case IOS -> {
                return new OrderListPageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public List<TitleAndOrderId> filterOrderByStatus(List<TitleAndOrderId> orderList, String targetOrderStatus) {
        switch(targetOrderStatus) {
            case "PROCESSING_ETICKET" -> {
                return orderList.stream().filter(d -> d.orderStatus.equalsIgnoreCase("PROCESSING_ETICKET")).collect(Collectors.toList());
            }
            case "ETICKET_ISSUED" -> {
                return orderList.stream().filter(d -> d.orderStatus.equalsIgnoreCase("ETICKET_ISSUED")).collect(Collectors.toList());
            }
            case "WAITING_PAYMENT" -> {
                return orderList.stream().filter(d -> d.orderStatus.equalsIgnoreCase("WAITING_PAYMENT")).collect(Collectors.toList());
            }
            default -> {
                logger.log("Invalid target order status: " + targetOrderStatus);
            }
        }

        return orderList;
    }

    public List<TitleAndOrderId> filterUpcomingBooking(List<TitleAndOrderId> filteredTrainOrder, String vertical) {
        switch(vertical) {
            case "TRAIN" -> {
                return filteredTrainOrder.stream().filter(d -> {
                    String departureDateTime = d.orderDetails.get(0).vertical.departureDate;
                    departureDateTime = departureDateTime.replaceAll("-", "/");
                    String todayDateTime = CoreFunctions.getCurrentDateTime();

                    logger.log("order compare"+ d.orderId +": " + CoreFunctions.compareDateTime(departureDateTime, todayDateTime));
                    return CoreFunctions.compareDateTime(departureDateTime, todayDateTime) >= 0;
                }).collect(Collectors.toList());
            }
            case "HOTEL" -> {
                return filteredTrainOrder.stream().filter(d -> {
                    String departureDateTime = d.orderDetails.get(0).vertical.bookingCheckinDate + " "+ "00:00";
                    departureDateTime = departureDateTime.replaceAll("-", "/");
                    String todayDateTime = CoreFunctions.getCurrentDateTime();

                    logger.log("order compare"+ d.orderId +": " + CoreFunctions.compareDateTime(departureDateTime, todayDateTime));
                    return CoreFunctions.compareDateTime(departureDateTime, todayDateTime) >= 0;
                }).collect(Collectors.toList());
            }
            default -> {
                logger.log("Vertical not yet supported : " + vertical);
            }
        }

        return filteredTrainOrder;
    }

    @SneakyThrows
    public List<MapVerticalWithOrderId.TitleAndOrderId> sortByEarliestDeparture(List<MapVerticalWithOrderId.TitleAndOrderId> listOfOrders) {

        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Collections.sort(listOfOrders, new Comparator<MapVerticalWithOrderId.TitleAndOrderId>() {
            @Override
            public int compare(MapVerticalWithOrderId.TitleAndOrderId order1, MapVerticalWithOrderId.TitleAndOrderId order2) {
                String o1 = order1.orderDetails.get(0).vertical.bookingCheckinDate + " "+ "00:00";
                String o2 = order2.orderDetails.get(0).vertical.bookingCheckinDate + " "+ "00:00";
                try {
                    return dfm.parse(o1).compareTo(dfm.parse(o2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        return listOfOrders;
    }


    public void refreshPage(int maxTry) {
        driverTools.swipeDown(maxTry);
    }

    public abstract boolean isOrderListPage();
    public abstract Boolean checkIfOrderIdExist(String orderID);
    public abstract String getTotalPriceByOrderId(String orderID);
    public abstract String getProductTitleByOrderId(String orderID);
    public abstract String getProductTitleByOrderId(String orderID, String productName);
    public abstract void openFirstGiftCardOrder();

    protected static final Locator GIFT_CARD_HEADER = new Locator("GIFT_CARD_HEADER", AppiumBy.xpath("//span[contains(text(),'Gift Card')]"));
    protected static final Locator GIFT_CARD_DETAILS = new Locator("GIFT_CARD_DETAILS", AppiumBy.xpath("(//p[contains(text(),'Gift Card')])[1]"));
    protected static final Locator CLICK_DETAILS = new Locator("CLICK_DETAILS", AppiumBy.xpath("//button//span[contains(text(), 'Details') or contains(text(), 'Detail')]"));
    protected static final Locator REDEEM_CODE = new Locator("REDEEM_CODE", AppiumBy.xpath("//div[contains(@class, 'RecipientItem_share_link')]"));
    protected static final Locator SHARE_BUTTON = new Locator("SHARE_BUTTON", AppiumBy.xpath("//button[contains(text(), 'Bagikan') or contains(text(), 'Share')]"));

    public boolean verifyGiftOrderDetails(){
        driverTools.switchToWebViewUrl("yourorder");
        driverTools.waitForElementToAppear(GIFT_CARD_HEADER);
        boolean isGiftCardOpen = driverTools.isElementDisplayed(GIFT_CARD_HEADER) && driverTools.isElementDisplayed(GIFT_CARD_DETAILS);
        return isGiftCardOpen;
    }

    public boolean verifyRedeemCodePresent(){
        driverTools.switchToWebViewUrl("yourorder/detail");
        driverTools.clickWithJavascriptExecutor(CLICK_DETAILS);
        driverTools.switchToWebViewUrl("gift-card");
        driverTools.waitForElementToPresence(REDEEM_CODE).orElseThrow();
        driverTools.waitForElementToBeClickable(SHARE_BUTTON);
        boolean isRedeemCodePresent = driverTools.isElementPresent(SHARE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SHARE_BUTTON);
        return isRedeemCodePresent;
    }

    public enum OrderVerticalname {
        FLIGHT("FLIGHT"), CAR("CAR"), HOTEL("HOTEL"), TRAIN("TRAIN"), AIRPORT_TRANSFER("AIRPORT_TRANSFER"),
        TO_DO("TO_DO");

        private String name;

        OrderVerticalname(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public abstract void openOrderCompletePayment(String orderId);
    public abstract void openOrderWaitingPayment(String orderId);
    public abstract void clickLinkToVideo();

    public abstract String getOrderId();
    /** Opens the active order whose status is "Payment Required" / "Belum Lunas". */
    public abstract void openPaymentRequiredOrder();

    public abstract String getProductTitle();

    public abstract String getTotalPrice();

    public abstract String findActiveTicket(String name, String detail, String orderID, int count);

    public abstract void openActiveTicketViaDeeplink(String orderId);
    public abstract void openIssuedTicketViaDeeplink(String orderId);
    public abstract void openOrderDetailViaDeeplink(String orderId);

    public abstract void openActiveTicketViaDeeplink(String orderId,String orderHash,String ordeDetailId);

    public abstract void openOrderDetail(String productName);

    public abstract void removePopUp();
    public abstract void openActiveOrdersList();

    public abstract boolean matchOrderId(String orderId);

    public abstract boolean matchDetails(String expectedType, String type); // FIXME:IMPLEMENTATION PENDING ANDROID

    public abstract void openActiveTicketTab();

    public abstract void openOngoingRefundTab();

    public abstract boolean ongoingRefundTabStatus();

    public abstract boolean findRefundCard(String refundId);

    public abstract void openRefundCard(String refundId);

    public abstract void findHotelRefund();

    public abstract String getRefundId();

    public abstract String getEstimatedTotalPayment();

    public abstract String getRefundHotelName(String ProductName);
    public abstract String getRefundOrderTitle(String refundId);
    public abstract String getEstimatedTotalPayment(String refundId);

    public abstract String getHotelRefundEstimatedDate(String refundID);

    public abstract String getHotelRefundEstimatedPayment(String refundID);

    public abstract String getHotelRefundStatus();

    public abstract void openHotelRefund(String hotelName);

    public abstract void openOrderHistory();

    public abstract boolean cancelOrder();

    public abstract void findOrder(String orderId);

    public abstract void seeDetails();

    public abstract void openOrderDetailWaitingForPayment(String orderID);

    public abstract void refreshTheOrderList(int count);

    public abstract void openFirstWaitingPaymentOrder();

    public abstract boolean verifyWaitingForPayment(String OrderId);

    public abstract boolean verifyCrossSellInOrderListPage();

    public abstract boolean verifyCrossSellSeeAllBtn();
    public abstract void loginForOrderList(String username, String password);
    public abstract void swipeToActiveRefund(String refundId);
    public abstract boolean verifyActiveRefundCard(String refundId, String currency);

    @SneakyThrows
    public JSONObject getInternalOrderData(String orderId){
        OrderDetailPage<T> orderDetailPage = OrderDetailPage.getInstance(session);
        JSONObject response = orderDetailPage.getInternalOrderDetail(orderId);;
        return response;
    }


    public String getCorrectVerticalOrderDetailId(GeneralVerticalInternalOrderDetail orderDetailData){
        String orderDetailId = "";
        for(int i=0;i<orderDetailData.data.orderDetails.size();i++){
            if(!orderDetailData.data.orderDetails.get(i).type.equals("insurance")){
                return orderDetailData.data.orderDetails.get(i).vertical.orderDetailId==null ?  orderDetailData.data.orderDetails.get(i).orderDetailId : orderDetailData.data.orderDetails.get(i).vertical.orderDetailId;
            }
        }
        return orderDetailId;
    }

    /**
     * Resolves the per-itinerary {@code orderDetailId} for a given parent {@code orderId}
     * via the same internal-order API that {@link #openIssuedTicketViaDeeplink} already
     * uses to build its deeplink. Lets tests assert on the order detail id displayed by
     * the new iOS UI (the parent order id is not rendered natively there) using the
     * same source of truth the deeplink itself uses.
     */
    public String getOrderDetailIdByOrderId(String orderId){
        JSONObject response = getInternalOrderData(orderId);
        GeneralVerticalInternalOrderDetail orderDetail = getVerticalInternalOrderDtata(response);
        return getCorrectVerticalOrderDetailId(orderDetail);
    }

    protected String getVerticalName(GeneralVerticalInternalOrderDetail orderDetailData){
        String orderType = "";
        for(int i=0;i<orderDetailData.data.orderDetails.size();i++){
            if(!orderDetailData.data.orderDetails.get(i).type.equals("insurance")){
                return orderDetailData.data.orderDetails.get(i).type;
            }
        }
        return orderType;
    }

    public GeneralVerticalInternalOrderDetail getVerticalInternalOrderDtata(JSONObject responseInternalOrder){
        Gson gson = new Gson();
        GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = gson.fromJson(responseInternalOrder.toString(),GeneralVerticalInternalOrderDetail.class);
        return generalVerticalInternalOrderDetail;
    }

    private MapVerticalWithOrderId.TitleAndOrderId getOrderData(CompleteOrderListResponse.Card card){
        MapVerticalWithOrderId.TitleAndOrderId titleAndOrder = new MapVerticalWithOrderId.TitleAndOrderId();
        titleAndOrder.orderId = card.orderData.orderId;
        titleAndOrder.orderHash = card.orderData.orderHash;
        titleAndOrder.title = card.title;
        titleAndOrder.subtitle = card.content.contents.get(0).subcontents.get(0).title;
        JSONObject internalOrderJSON = getInternalOrderData(titleAndOrder.orderId);
        GeneralVerticalInternalOrderDetail internalOrderData = getVerticalInternalOrderDtata(internalOrderJSON);
        if(internalOrderData.data == null) return null;
        titleAndOrder.userLang = internalOrderData.data.userLang;
        titleAndOrder.internalOrderDetailJSONObject = internalOrderJSON.toString();
        titleAndOrder.verticalName = getVerticalName(internalOrderData);
        titleAndOrder.verticalName = titleAndOrder.normalizeVerticalName();
        titleAndOrder.orderDetails = internalOrderData.data.orderDetails;

        if (internalOrderData.data.payment.isEmpty()) {
            titleAndOrder.currency = "";
        } else {
            titleAndOrder.currency = internalOrderData.data.payment.get(0).paymentCurrency;
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

    /** return only first order component 
      * @param refundAble - Set this parameter to 'true' if the order needs to be refundable.
      *                    Set to 'false' to skip the refundable checking part.
    */
    @SneakyThrows
    public MapVerticalWithOrderId.TitleAndOrderId findFirstOrderFilterByVertical(String accessToken, String username,String accountId, String vertical,String currency,boolean refundAble,boolean reschedulable){
        // pojo of data
        MapVerticalWithOrderId.TitleAndOrderId mapVerticalWithOrderId = new MapVerticalWithOrderId.TitleAndOrderId();
        CompleteOrderListApi.Inputs inputs = new CompleteOrderListApi.Inputs(accessToken);
        CompleteOrderListApi CompleteOrderListApi = new CompleteOrderListApi(inputs);
        ApiResponse apiResponse = CompleteOrderListApi.hitApi(apiTools);
        CompleteOrderListResponse response = new Gson().fromJson(apiResponse.getBody(),
                CompleteOrderListResponse.class);
        CompleteOrderListResponse.View filteredCard = response.data.views.stream()
                .filter(e -> e.id.equals("activeCards")).findFirst().orElseThrow();
        if (filteredCard.activeCards.cards.size() == 0) {
            return mapVerticalWithOrderId;
        }
        String verticall = vertical.equalsIgnoreCase("ttd") ? "to_do" : vertical.equalsIgnoreCase("airporttransfer")? "airport_transfer" : vertical.equalsIgnoreCase("homes")? "hotel_homes" : vertical;
        logger.log("VERTICAL: "+verticall);
        RefundPage<T> refundPage = RefundPage.getInstance(session);
        RefundPage.CheckRefundAbleStatus filterRefundAble = refundPage.checkRefundAbleStatus(username,accountId);

        ReschedulePage<T> reschedulePage = ReschedulePage.getInstance(session);
        ReschedulePage.CheckRescheduleAble filterRescheduleAble = reschedulePage.checkRescheduleableStatus(username) ;
        try{
            mapVerticalWithOrderId = filteredCard.activeCards.cards.stream().map(d -> getOrderData(d))
                .filter(Objects::nonNull)
                .filter(d->(d.verticalName.equalsIgnoreCase(verticall) || (d.subOrderType!=null && d.subOrderType.equalsIgnoreCase(verticall)))&& (currency.equalsIgnoreCase("") || currency.equalsIgnoreCase(d.currency)))
                .filter(d -> refundAble==false || (refundAble && filterRefundAble.check(d)))
                .filter(d -> reschedulable==false || (filterRescheduleAble.check(d) && reschedulable))
                .findFirst().get();
                return mapVerticalWithOrderId;
        }catch(NoSuchElementException e){
            logger.log("No order found");
        }
        return null;
    }

    public enum OrderStatus{
        PROCESSING_ETICKET("PEOCESSING_ETICKET"), ETICKET_ISSUED("ETICKET_ISSUED"), WAITING_PAYMENT("WAITING_PAYMENT");
        private String name;

        OrderStatus(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @SneakyThrows
    public MapVerticalWithOrderId.TitleAndOrderId findFirstOrderFilterByVertical(String accessToken, String username,String accountId, String vertical,String currency,boolean refundAble,boolean reschedulable,OrderStatus orderStatus){
                // pojo of data
                MapVerticalWithOrderId.TitleAndOrderId mapVerticalWithOrderId = new MapVerticalWithOrderId.TitleAndOrderId();
                CompleteOrderListApi.Inputs inputs = new CompleteOrderListApi.Inputs(accessToken);
                CompleteOrderListApi CompleteOrderListApi = new CompleteOrderListApi(inputs);
                ApiResponse apiResponse = CompleteOrderListApi.hitApi(apiTools);
                logger.log("api order response ->" + apiResponse.getBody());
                CompleteOrderListResponse response = new Gson().fromJson(apiResponse.getBody(),
                        CompleteOrderListResponse.class);
                CompleteOrderListResponse.View filteredCard = response.data.views.stream()
                        .filter(e -> e.id.equals("activeCards")).findFirst().orElseThrow();
                if (filteredCard.activeCards.cards.size() == 0) {
                    return mapVerticalWithOrderId;
                }
                String verticall = vertical.equalsIgnoreCase("ttd") ? "to_do" : vertical.equalsIgnoreCase("airporttransfer")? "airport_transfer" : vertical.equalsIgnoreCase("homes")? "hotel_homes" : vertical;
                RefundPage<T> refundPage = RefundPage.getInstance(session);
                RefundPage.CheckRefundAbleStatus filterRefundAble = refundPage.checkRefundAbleStatus(username,accountId);
                ReschedulePage<T> reschedulePage = ReschedulePage.getInstance(session);
                ReschedulePage.CheckRescheduleAble filterRescheduleAble = reschedulePage.checkRescheduleableStatus(username) ;
                try{
                    mapVerticalWithOrderId = filteredCard.activeCards.cards.stream().map(d -> getOrderData(d))
                        .filter(Objects::nonNull)
                        .filter(d->(d.verticalName.equalsIgnoreCase(verticall))&& (currency.equalsIgnoreCase("") || currency.equalsIgnoreCase(d.currency)))
                        .filter(d -> refundAble==false || (refundAble && filterRefundAble.check(d)))
                        .filter(d -> reschedulable==false || (filterRescheduleAble.check(d) && reschedulable))
                        .filter(d -> orderStatus.toString().equalsIgnoreCase(d.orderStatus))
                        .findFirst().get();
                        return mapVerticalWithOrderId;
                }catch(NoSuchElementException e){
                    logger.log("No order found");
                }
                return null;
    }

    public MapVerticalWithOrderId.TitleAndOrderId getLatestActiveFlightOrderWithEticketIssuedAndDepartureAfterTwoMonths(String accessToken) {
        MapVerticalWithOrderId orders = new OrderApi<>(session).getAvailaibleOrderByVertical("flight", accessToken);
        if (orders == null || orders.flight == null || orders.flight.isEmpty()) return null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date twoMonthsLater;
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, 2);
            String twoMonthsLaterStr = sdf.format(cal.getTime());
            twoMonthsLater = sdf.parse(twoMonthsLaterStr);
        } catch (ParseException e) {
            logger.log("Error parsing reference date");
            return null;
        }

        return orders.flight.stream()
            .filter(order -> "ETICKET_ISSUED".equalsIgnoreCase(order.orderStatus))
            .filter(order -> {
                if (order.orderDetails == null || order.orderDetails.isEmpty()) return false;
                GeneralVerticalInternalOrderDetail.OrderDetail detail = order.orderDetails.get(0);
                if (detail.vertical == null || detail.vertical.flightDate == null) return false;
                try {
                    Date flightDate = sdf.parse(detail.vertical.flightDate);
                    return flightDate.after(twoMonthsLater);
                } catch (ParseException e) {
                    logger.log("Error parsing flightDate for orderId: " + order.orderId);
                    return false;
                }
            })
            .max(Comparator.comparing(order -> {
                try {
                    GeneralVerticalInternalOrderDetail.OrderDetail detail = order.orderDetails.get(0);
                    return sdf.parse(detail.vertical.flightDate);
                } catch (Exception e) {
                    return new Date(0);
                }
            }))
            .orElse(null);
    }

    private String getStartDateFromTrackers(List<GeneralVerticalInternalOrderDetail.Tracker> trackers) {
        if (trackers == null) return null;
        for (GeneralVerticalInternalOrderDetail.Tracker tracker : trackers) {
            if ("startDate".equalsIgnoreCase(tracker.key)) {
                return tracker.value;
            }
        }
        return null;
    }

    public boolean checkRescheduleableStatus(String username,MapVerticalWithOrderId.TitleAndOrderId orderData){
        GETRescheduleableStatus.Inputs input = new GETRescheduleableStatus.Inputs(username,orderData.orderId,orderData.orderDetailId);
        GETRescheduleableStatus getReschedulableStatus = new GETRescheduleableStatus(input);
        ApiResponse response;
        try {
            response = getReschedulableStatus.hitApi(apiTools);
            JSONObject obj = new JSONObject(response.getBody());
            logger.log("object : " + obj.toString());
            return obj.getJSONObject("data").getJSONObject("reschedulable").getBoolean("value");
        } catch (JSONException e ) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
        }
        return false;
    }

    public boolean checkRefundableStatus(String username,MapVerticalWithOrderId.TitleAndOrderId orderData,String accountId){
        GETRefundAbleStatus.Inputs input = new GETRefundAbleStatus.Inputs(username,orderData.orderId,orderData.orderDetailId,accountId);
        GETRefundAbleStatus getRefundAbleStatus = new GETRefundAbleStatus(input);
        ApiResponse response;
        try {
            response = getRefundAbleStatus.hitApi(apiTools);
            JSONObject obj = new JSONObject(response.getBody());
            logger.log("object : " + obj.toString());
            return obj.getJSONObject("data").getJSONObject("refundable").getBoolean("value");
        } catch (JSONException e ) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
        }
        return false;
    }

    public MapVerticalWithOrderId filterOrderByPaymentCurrency(MapVerticalWithOrderId orderData,String currency,int limit){
        //filter Car
        orderData.carRental = orderData.carRental.stream().filter(Objects::nonNull).filter(d -> d.currency.equalsIgnoreCase(currency)).limit(limit).toList();
        //filter Todo
        orderData.ttd = orderData.ttd.stream().filter(Objects::nonNull).filter(d -> d.currency.equalsIgnoreCase(currency)).limit(limit).toList();
        //filter Hotel
        orderData.hotel = orderData.hotel.stream().filter(Objects::nonNull).filter(d -> d.currency.equalsIgnoreCase(currency)).limit(limit).toList();
        //filter Homes
        orderData.homes = orderData.homes.stream().filter(Objects::nonNull).filter(d -> d.currency.equalsIgnoreCase(currency)).limit(limit).toList();
        //filter AT
        orderData.airportTransfer = orderData.airportTransfer.stream().filter(Objects::nonNull).filter(d -> d.currency.equalsIgnoreCase(currency)).limit(limit).toList();
        //filter Train
        orderData.train = orderData.train.stream().filter(Objects::nonNull).filter(d -> d.currency.equalsIgnoreCase(currency)).limit(limit).toList();
        //filter ferry
        orderData.ferry = orderData.ferry.stream().filter(Objects::nonNull).filter(d -> d.currency.equalsIgnoreCase(currency)).limit(limit).toList();
        //filter bus
        orderData.bus = orderData.bus.stream().filter(Objects::nonNull).filter(d -> d.currency.equalsIgnoreCase(currency)).limit(limit).toList();

        if(orderData.bus.size()==0 && orderData.ferry.size()==0 && orderData.train.size()==0 && orderData.airportTransfer.size()==0 && orderData.hotel.size()==0 && orderData.ttd.size()==0 && orderData.carRental.size()==0 && orderData.homes.size()==0){
            orderData.isEmpty = true;
        }
        return orderData;
    }

    public MapVerticalWithOrderId filterVerticalOrderByPaymentCurrency(String vertical, MapVerticalWithOrderId orderData,String currency){
        switch (vertical.toLowerCase()) {
            case "car":
                orderData.carRental = orderData.carRental.stream().filter(d -> d.currency.equalsIgnoreCase(currency)).toList();
                break;
            case "ttd":
                orderData.ttd = orderData.ttd.stream().filter(d -> d.currency.equalsIgnoreCase(currency)).toList(); break;
            case "hotel" :
                orderData.hotel = orderData.hotel.stream().filter(d -> d.currency.equalsIgnoreCase(currency)).toList(); break;
            case "airporttransfer" : 
                orderData.airportTransfer = orderData.airportTransfer.stream().filter(d -> d.currency.equalsIgnoreCase(currency)).toList(); break;
            case "train":  orderData.train = orderData.train.stream().filter(d -> d.currency.equalsIgnoreCase(currency)).toList(); break;
            case "ferry" : orderData.ferry = orderData.ferry.stream().filter(d -> d.currency.equalsIgnoreCase(currency)).toList(); break;
            case "bus" : orderData.bus = orderData.bus.stream().filter(d -> d.currency.equalsIgnoreCase(currency)).toList(); break;
            case "flight" :
                orderData.flight = orderData.flight.stream().filter(d -> d.currency.equalsIgnoreCase(currency)).toList(); break;
            default:
                break;
        }

        if(orderData.bus.isEmpty() && orderData.ferry.isEmpty() && orderData.train.isEmpty() && orderData.airportTransfer.isEmpty() && orderData.hotel.isEmpty() && orderData.ttd.isEmpty() && orderData.carRental.isEmpty() && orderData.flight.isEmpty()){
            orderData.isEmpty = true;
        }
        logger.log("isEmpty: "+orderData.isEmpty);
        return orderData;
    }

    interface CheckOrderData {
        boolean check(MapVerticalWithOrderId.TitleAndOrderId orderData);
    }

    private CheckOrderData checkOrderHasChatMenu(String username,String accessToken){
        return (orderData) ->{
            YourOrderComposerGET.Inputs input = new YourOrderComposerGET.Inputs(username,accessToken,orderData.orderId,orderData.orderHash,orderData.orderDetailId); 
            YourOrderComposerGET yourOrderComposerGET = new YourOrderComposerGET(input);
            ApiResponse response;
            try {
                // Parse JSON string
                response = yourOrderComposerGET.hitApi(apiTools);
                JSONObject obj = new JSONObject(response.getBody());
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(obj.toString());
    
                 // Get "views" node
                JsonNode viewsNode = rootNode.path("data").path("views");

                // Check each view for "Chat" action
                boolean chatActionExists = false;
                for (JsonNode view : viewsNode) {
                    if (view.has("id") && view.get("id").asText().equals("hotelDetailInformation")) continue;
                    JsonNode actionsNode = view.path("actions");
                    if (actionsNode.isArray()) {
                        for (JsonNode action : actionsNode) {
                            if (action.has("title") && action.get("title").asText().equals("Chat")) {
                                chatActionExists = true;
                                break;
                            }
                        }
                    }
                    if (chatActionExists) {
                        break;
                    }
                }
                // Output result
                if (chatActionExists) {
                    System.out.println("Chat action exists.");
                    return true;
                } else {
                    System.out.println("Chat action does not exist.");
                    return false;
                }
            } catch (Exception e) {
                logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            }
            return false;
        };
    }   

    public MapVerticalWithOrderId filterHotelOrderWithChat(MapVerticalWithOrderId orderData,String accessToken,String username){
        CheckOrderData checkOrderData = checkOrderHasChatMenu(username,accessToken);
        orderData.hotel = orderData.hotel.stream().filter(d -> checkOrderData.check(d)).toList();
        return orderData;
    }

    @SneakyThrows
    public boolean getOrderStatus(String orderId,String username,String password){
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password, "B2C");
        CompleteOrderListApi.Inputs inputs = new CompleteOrderListApi.Inputs(accessToken);
        return orderList(inputs, orderId);
    }

    private boolean orderList(CompleteOrderListApi.Inputs inputs, String orderId) throws ExecutionException, InterruptedException {
        logger.log("*********initial order id**********"+orderId);
        CompleteOrderListApi CompleteOrderListApi = new CompleteOrderListApi(inputs);
        boolean isIssued = false;
        for(int i=0;i<6;i++) {
            ApiResponse apiResponse = CompleteOrderListApi.hitApi(apiTools);
            CompleteOrderListResponse response = new Gson().fromJson(apiResponse.getBody(), CompleteOrderListResponse.class);
            for (CompleteOrderListResponse.View view : response.data.views) {
                if(view.id.contains("activeCards")){
                    for (CompleteOrderListResponse.Card cards:view.activeCards.cards){
                        if(cards.orderData.orderId.contains(orderId)){
                            logger.log("order status : " + cards.status.title);
                            if(cards.status.title.contains("E-tiket telah terbit") || cards.status.title.contains("E-ticket issued")){
                                return true;
                            }else
                                break;
                        }
                    }
                }
            }
            this.refreshTheOrderList(1);
            Thread.sleep(50000);
            this.refreshTheOrderList(1);
        }
        return isIssued;
    }

    public List<MapVerticalWithOrderId.TitleAndOrderId> filterHotelMultiOrderOnly(MapVerticalWithOrderId mapVerticalWithOrderId){
        logger.log("Filtering order by multi order. Length before filter : " + mapVerticalWithOrderId.hotel.size());
        List<MapVerticalWithOrderId.TitleAndOrderId> mapVerticalWithOrderIdFiltered = mapVerticalWithOrderId.hotel.stream().filter(d -> d.checkIfMultiOrder()).collect(Collectors.toList());
        logger.log("Filter multi order result length: " + mapVerticalWithOrderIdFiltered.size());
        
        return mapVerticalWithOrderIdFiltered;
    }    

    @SneakyThrows
    private boolean checkPaymentOrder(String internalOrderJsonString, PaymentType paymentType){
        logger.log("DEBUG check payment order: " + internalOrderJsonString);
        JSONObject internalOrderJSON = new JSONObject(internalOrderJsonString);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(internalOrderJSON.toString());
        // Get "payment" node
        JsonNode payments = rootNode.path("data").path("payment");
        boolean isPaymentTypeCorrect = false;
        int paymentTypeCount = 0;
        for (JsonNode payment : payments) {
            if (payment.has("paymentSource") && payment.get("paymentSource").asText().equals(paymentType.getPaymentType())) {
                isPaymentTypeCorrect = true;
            }
            paymentTypeCount++;
        }
        if (paymentTypeCount > 1 && isPaymentTypeCorrect && paymentType==PaymentType.fulltiketpoints) {
            isPaymentTypeCorrect = false;
        }

        return isPaymentTypeCorrect;
    }

    public static enum PaymentType{
        creditcard("creditcard")
        ,atm("atm"),fulltiketpoints("tiketpoint"),halftiketpoints("tiketpoint");

        private String paymentType;

        private PaymentType(String paymentType) {
            this.paymentType = paymentType;
        }
    
        public String getPaymentType() {
            return this.paymentType;
        }
    }

    public List<TitleAndOrderId> filterOrderByPaymentType(MapVerticalWithOrderId orderData,PaymentType paymentType){
        List<TitleAndOrderId> filteredOrder = new ArrayList<>();
        orderData.airportTransfer.stream().filter(d -> checkPaymentOrder(d.internalOrderDetailJSONObject, paymentType)).forEach(filteredOrder::add);
        orderData.hotel.stream().filter(d -> checkPaymentOrder(d.internalOrderDetailJSONObject, paymentType)).forEach(filteredOrder::add);
        orderData.carRental.stream().filter(d -> checkPaymentOrder(d.internalOrderDetailJSONObject, paymentType)).forEach(filteredOrder::add);
        orderData.ferry.stream().filter(d -> checkPaymentOrder(d.internalOrderDetailJSONObject, paymentType)).forEach(filteredOrder::add);
        orderData.flight.stream().filter(d -> checkPaymentOrder(d.internalOrderDetailJSONObject, paymentType)).forEach(filteredOrder::add);
        orderData.bus.stream().filter(d -> checkPaymentOrder(d.internalOrderDetailJSONObject, paymentType)).forEach(filteredOrder::add);
        orderData.train.stream().filter(d -> checkPaymentOrder(d.internalOrderDetailJSONObject, paymentType)).forEach(filteredOrder::add);
        logger.log("DEBUG list order after filter: \n" + filteredOrder.toString());
        return filteredOrder;
    }

    @SneakyThrows
    public String waitForOrderToBeInIssuedState(String orderId, int tries) {
        String status = null;
        for (int retries = tries; retries > 0; retries--) {
            JSONObject response = getInternalOrderData(orderId);
            GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = getVerticalInternalOrderDtata(response);
            status = generalVerticalInternalOrderDetail.data.orderStatus;
            logger.log("Status: " + status);
            if (status.equalsIgnoreCase("ETICKET_ISSUED")) {
                Thread.sleep(5000); // this is added since sometimes it is not instantly synced between UI and response, e.g from API got issued but when deeplinking it is still in processing
                break;
            }
            Thread.sleep(5000);
        }
        return status;
    }

    public Locator redundStatusLocator(String refundId) {
        return new Locator("REFUND_STATUS", AppiumBy.xpath("(//span[contains(@class,'RefundCard_truncate')and contains(text(),'"+refundId+"')]//parent::*//parent::*)[1]//div[contains(@class,'RefundCard_action_container')]//span"));
    }

    public String refundStatus(String refundId) {
        driverTools.switchToWebView();
        String refundStatus = driverTools.getText(redundStatusLocator(refundId)).orElseThrow();
        driverTools.switchToNative();
        return refundStatus;
    }

    @SneakyThrows
    public String waitForPaymentSuccessful(String orderId, int triesMax) {
        String status = "";

        for(int tries=0;tries<triesMax;tries++){
            JSONObject response = getInternalOrderData(orderId);
            GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = getVerticalInternalOrderDtata(response);
            status = generalVerticalInternalOrderDetail.data.orderStatus;
            logger.log("Order status: "+status);

            if(status.equalsIgnoreCase("ETICKET_ISSUED") || status.equalsIgnoreCase("PROCESSING_ETICKET")){
                break;
            }
            Thread.sleep(5000);
        }
        return status;
    }
}

