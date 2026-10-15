package com.tiket.apiPage;

import com.google.gson.Gson;
import com.tiket.api.accommodation.hotel.PublishFinishOrderApi;
import com.tiket.api.order.CompleteOrderListApi;
import com.tiket.api.order.HistoryRefundListApi;
import com.tiket.api.order.InternalOrderDetailApi;
import com.tiket.api.order.TripsListApi;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.order.CompleteOrderListResponse;
import com.tiket.pojo.order.GeneralVerticalInternalOrderDetail;
import com.tiket.pojo.order.HotelInternalOrderDetail;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

/**
 * Provides API operations related to orders, such as retrieving hotel internal order details
 * and extracting order names from order data. This class acts as a wrapper for order-related
 * API calls and response parsing, facilitating test automation and integration with the order system.
 *
 * @param <T> the type of WebDriver used for browser automation
 */
public class OrderApi<T extends WebDriver> extends BasePage<T> {

    public OrderApi(Session<T> session) {
        super(session);
    }

    @SneakyThrows
    public HotelInternalOrderDetail getHotelInternalOrderData(String orderId) {
        Gson gson = new Gson();
        return gson.fromJson(this.getInternalOrderDetail(orderId).toString(), HotelInternalOrderDetail.class);
    }

    @SneakyThrows
    public MapVerticalWithOrderId getAvailaibleOrderByVertical(String vertical, String accessToken) {
        MapVerticalWithOrderId mapVerticalWithOrderId = new MapVerticalWithOrderId();

        CompleteOrderListApi.Inputs inputs = new CompleteOrderListApi.Inputs(accessToken);
        CompleteOrderListApi completeOrderListApi = new CompleteOrderListApi(inputs);
        ApiResponse apiResponse = completeOrderListApi.hitApi(apiTools);
        CompleteOrderListResponse response = new Gson().fromJson(apiResponse.getBody(), CompleteOrderListResponse.class);

        Optional<CompleteOrderListResponse.View> activeCardsView = response.data.views.stream()
                .filter(e -> e.id.equals("activeCards"))
                .findFirst();
        if (activeCardsView.isEmpty()) {
            logger.log("activeCards view not found, returning empty order list");
            return mapVerticalWithOrderId;
        }
        CompleteOrderListResponse.View filteredCard = activeCardsView.get();

        mapVerticalWithOrderId.isEmpty = false;
        vertical = normalizeVerticalFilter(vertical);
        logger.log("vertical: " + vertical);

        for (int i = 0; i < filteredCard.activeCards.cards.size(); i++) {
            String orderType = filteredCard.activeCards.cards.get(i).orderData.orderType;
            String subOrderType = filteredCard.activeCards.cards.get(i).orderData.subOrderType;
            if (subOrderType == null) {
                subOrderType = "";
            }
            if (!orderType.equalsIgnoreCase(vertical) && !subOrderType.equalsIgnoreCase(vertical)) {
                continue;
            }
            MapVerticalWithOrderId.TitleAndOrderId titleAndOrder = getOrderData(filteredCard.activeCards.cards.get(i));
            if (titleAndOrder == null) {
                continue;
            }
            addOrderByType(mapVerticalWithOrderId, orderType, subOrderType, titleAndOrder);
        }
        return mapVerticalWithOrderId;
    }

    @SneakyThrows
    public MapVerticalWithOrderId getAvailaibleOrder(String accessToken) {
        MapVerticalWithOrderId mapVerticalWithOrderId = new MapVerticalWithOrderId();

        CompleteOrderListApi.Inputs inputs = new CompleteOrderListApi.Inputs(accessToken);
        CompleteOrderListApi completeOrderListApi = new CompleteOrderListApi(inputs);
        ApiResponse apiResponse = completeOrderListApi.hitApi(apiTools);
        CompleteOrderListResponse response = new Gson().fromJson(apiResponse.getBody(), CompleteOrderListResponse.class);

        Optional<CompleteOrderListResponse.View> activeCardsView = response.data.views.stream()
                .filter(e -> e.id.equals("activeCards"))
                .findFirst();
        if (activeCardsView.isEmpty()) {
            logger.log("activeCards view not found, returning empty order list");
            mapVerticalWithOrderId.isEmpty = true;
            return mapVerticalWithOrderId;
        }
        CompleteOrderListResponse.View filteredCard = activeCardsView.get();

        if (filteredCard.activeCards.cards.isEmpty()) {
            mapVerticalWithOrderId.isEmpty = true;
            return mapVerticalWithOrderId;
        }

        mapVerticalWithOrderId.isEmpty = false;
        mapVerticalWithOrderId.firstOrder = getOrderData(filteredCard.activeCards.cards.get(0));

        for (int i = 0; i < filteredCard.activeCards.cards.size(); i++) {
            String orderType = filteredCard.activeCards.cards.get(i).orderData.orderType;
            String subOrderType = filteredCard.activeCards.cards.get(i).orderData.subOrderType;
            if (subOrderType == null) {
                subOrderType = "";
            }
            MapVerticalWithOrderId.TitleAndOrderId titleAndOrder = getOrderData(filteredCard.activeCards.cards.get(i));
            if (titleAndOrder == null) {
                continue;
            }
            addOrderByType(mapVerticalWithOrderId, orderType, subOrderType, titleAndOrder);
        }
        return mapVerticalWithOrderId;
    }

    private static String normalizeVerticalFilter(String vertical) {
        if (vertical.equalsIgnoreCase("ttd")) {
            return "to_do";
        }
        if (vertical.equalsIgnoreCase("airporttransfer")) {
            return "airport_transfer";
        }
        if (vertical.equalsIgnoreCase("homes")) {
            return "hotel_homes";
        }
        return vertical;
    }

    private static void addOrderByType(
            MapVerticalWithOrderId mapVerticalWithOrderId,
            String orderType,
            String subOrderType,
            MapVerticalWithOrderId.TitleAndOrderId titleAndOrder
    ) {
        switch (orderType) {
            case "FLIGHT" -> mapVerticalWithOrderId.flight.add(titleAndOrder);
            case "HOTEL" -> {
                if (subOrderType.equals("HOTEL_HOMES")) {
                    mapVerticalWithOrderId.homes.add(titleAndOrder);
                } else {
                    mapVerticalWithOrderId.hotel.add(titleAndOrder);
                }
            }
            case "CAR" -> mapVerticalWithOrderId.carRental.add(titleAndOrder);
            case "TRAIN" -> mapVerticalWithOrderId.train.add(titleAndOrder);
            case "AIRPORT_TRANSFER" -> mapVerticalWithOrderId.airportTransfer.add(titleAndOrder);
            case "TO_DO" -> mapVerticalWithOrderId.ttd.add(titleAndOrder);
            case "BUS" -> mapVerticalWithOrderId.bus.add(titleAndOrder);
            case "FERRY" -> mapVerticalWithOrderId.ferry.add(titleAndOrder);
            default -> {
            }
        }
    }

    @SneakyThrows
    private MapVerticalWithOrderId.TitleAndOrderId getOrderData(CompleteOrderListResponse.Card card) {
        MapVerticalWithOrderId.TitleAndOrderId titleAndOrder = new MapVerticalWithOrderId.TitleAndOrderId();
        titleAndOrder.orderId = card.orderData.orderId;
        titleAndOrder.orderHash = card.orderData.orderHash;
        titleAndOrder.title = card.title;
        titleAndOrder.subtitle = card.content.contents.get(0).subcontents.get(0).title;

        JSONObject internalOrderJSON = getInternalOrderDetail(titleAndOrder.orderId);
        GeneralVerticalInternalOrderDetail internalOrderData = getVerticalInternalOrderData(internalOrderJSON);
        if (internalOrderData.data == null) {
            return null;
        }

        titleAndOrder.userLang = internalOrderData.data.userLang;
        titleAndOrder.internalOrderDetailJSONObject = internalOrderJSON.toString();
        titleAndOrder.verticalName = getVerticalName(internalOrderData);
        titleAndOrder.verticalName = titleAndOrder.normalizeVerticalName();
        titleAndOrder.orderDetails = internalOrderData.data.orderDetails;

        if (internalOrderData.data.payment.isEmpty()) {
            titleAndOrder.currency = "";
            titleAndOrder.paymentSource = "";
        } else {
            titleAndOrder.currency = internalOrderData.data.payment.get(0).paymentCurrency;
            titleAndOrder.paymentSource = internalOrderData.data.payment.get(0).paymentSource;
        }

        titleAndOrder.orderDetailId = getCorrectVerticalOrderDetailId(internalOrderData);
        titleAndOrder.orderNameDetail = internalOrderData.data.orderDetails.get(0).orderNameDetail;
        titleAndOrder.orderName = internalOrderData.data.orderDetails.get(0).orderName;
        titleAndOrder.totalPrice = String.valueOf(internalOrderData.data.price);
        titleAndOrder.orderStatus = internalOrderData.data.orderStatus;
        titleAndOrder.subOrderType = Objects.requireNonNullElseGet(card.orderData.subOrderType, () -> card.orderData.subOrderType = "");
        return titleAndOrder;
    }

    private GeneralVerticalInternalOrderDetail getVerticalInternalOrderData(JSONObject responseInternalOrder) {
        return new Gson().fromJson(responseInternalOrder.toString(), GeneralVerticalInternalOrderDetail.class);
    }

    private String getVerticalName(GeneralVerticalInternalOrderDetail orderDetailData) {
        for (int i = 0; i < orderDetailData.data.orderDetails.size(); i++) {
            if (!orderDetailData.data.orderDetails.get(i).type.equals("insurance")) {
                return orderDetailData.data.orderDetails.get(i).type;
            }
        }
        return "";
    }

    private String getCorrectVerticalOrderDetailId(GeneralVerticalInternalOrderDetail orderDetailData) {
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

    private JSONObject getInternalOrderDetail(String orderID) {
        InternalOrderDetailApi.Inputs input = new InternalOrderDetailApi.Inputs(orderID);
        InternalOrderDetailApi internalOrderDetailApi = new InternalOrderDetailApi(input);
        ApiResponse response = internalOrderDetailApi.hitApi(apiTools);
        return new JSONObject(response.getBody());
    }

    public String getHotelOrderNameFromInternalOrder(HotelInternalOrderDetail response, String orderId) {
        for (HotelInternalOrderDetail.OrderDetails orderDetail : response.data.orderDetails) {
            if (orderDetail.type.equalsIgnoreCase("tixhotel") || orderDetail.type.equalsIgnoreCase("hotel_homes")) {
                return orderDetail.orderName;
            }
        }
        throw new RuntimeException("Cannot find order name in order detail with orderID : " + orderId);
    }

    /**
     * Resolves the hotel {@code orderDetailId} for a given {@code orderId} from the internal order detail.
     *
     * @param orderId the order id (typically captured from the payment / order page)
     * @return the hotel order detail id
     */
    public long getHotelOrderDetailId(String orderId) {
        return getHotelOrderDetailId(getHotelInternalOrderData(orderId), orderId);
    }

    /**
     * Resolves the hotel {@code orderDetailId} from an already-fetched internal order detail. Use this overload
     * to avoid re-hitting {@code /tix-order-go/internal/order/{orderId}/details} when the caller already has the
     * parsed response.
     *
     * @param response an already-fetched internal order detail
     * @param orderId  the order id (for the error message)
     * @return the hotel order detail id
     */
    public long getHotelOrderDetailId(HotelInternalOrderDetail response, String orderId) {
        for (HotelInternalOrderDetail.OrderDetails orderDetail : response.data.orderDetails) {
            if (orderDetail.type.equalsIgnoreCase("tixhotel") || orderDetail.type.equalsIgnoreCase("hotel_homes")) {
                return orderDetail.orderDetailId;
            }
        }
        throw new RuntimeException("Cannot find hotel orderDetailId in order detail with orderID : " + orderId);
    }

    /**
     * Triggers the hotel publish-finish-order (finished order) endpoint that the loyalty mission
     * engine consumes when evaluating mission stamp / GBV eligibility for a transaction.
     *
     * @param orderId       the order id
     * @param orderDetailId the hotel order detail id
     * @return the raw API response
     */
    public ApiResponse publishFinishOrder(long orderId, long orderDetailId) {
        PublishFinishOrderApi.Inputs inputs = new PublishFinishOrderApi.Inputs(orderId, orderDetailId);
        PublishFinishOrderApi api = new PublishFinishOrderApi(inputs);
        ApiResponse response = api.hitApi(apiTools);
        logger.log("Publish finish order response (orderId=" + orderId + ", orderDetailId=" + orderDetailId
                + "): status=" + response.getStatusCode() + ", body=" + response.getBody());
        Assertion.assertThat(
                "Publish-finish-order should succeed (2xx) for orderId=" + orderId + ". A failed publish "
                        + "would otherwise surface only later as a waitForOrderFinished timeout, hiding the real "
                        + "cause.",
                response.getStatusCode(), allOf(greaterThanOrEqualTo(200), lessThan(300)));
        return response;
    }

    /**
     * Publishes the finish-order event and waits for the order to reach {@code FINISHED}, retrying the
     * publish when the order does not finish within the wait window. The publish-finish event is
     * occasionally not consumed on the first hit, so a single publish + wait can time out even though a
     * subsequent publish would succeed; this re-publishes and re-polls up to {@code maxPublishAttempts}.
     *
     * @param orderId             the order id
     * @param orderDetailId       the hotel order detail id
     * @param maxPublishAttempts  how many times to publish before giving up (must be &gt;= 1)
     * @param timeoutSeconds      max time to wait for {@code FINISHED} after each publish
     * @param pollIntervalSeconds delay between status checks within each wait
     * @return {@code true} if the order reached {@code FINISHED} within the attempts, else {@code false}
     */
    public boolean publishFinishOrderUntilFinished(long orderId, long orderDetailId, int maxPublishAttempts,
                                                   long timeoutSeconds, long pollIntervalSeconds) {
        String orderIdStr = String.valueOf(orderId);
        boolean finished = false;
        for (int attempt = 1; attempt <= maxPublishAttempts && !finished; attempt++) {
            logger.log("Publish-finish-order attempt " + attempt + "/" + maxPublishAttempts
                    + " for orderId " + orderId);
            publishFinishOrder(orderId, orderDetailId);
            finished = waitForOrderFinished(orderIdStr, timeoutSeconds, pollIntervalSeconds);
            if (!finished && attempt < maxPublishAttempts) {
                logger.log("Order " + orderId + " not FINISHED after publish attempt " + attempt
                        + " - re-publishing the finish-order event.");
            }
        }
        if (!finished) {
            logger.log("Order " + orderId + " did not reach FINISHED after " + maxPublishAttempts
                    + " publish-finish-order attempt(s).");
        }
        return finished;
    }

    /**
     * Reads the top-level {@code data.orderStatus} from the internal order detail endpoint
     * ({@code /tix-order-go/internal/order/{orderId}/details}), e.g. {@code "FINISHED"}.
     *
     * @param orderId the order id
     * @return the current order status, or an empty string if it cannot be read
     */
    public String getOrderStatus(String orderId) {
        JSONObject response = getInternalOrderDetail(orderId);
        JSONObject data = response.optJSONObject("data");
        String status = data == null ? "" : data.optString("orderStatus", "");
        logger.log("Order " + orderId + " current status: " + status);
        return status;
    }

    /**
     * Polls the internal order detail endpoint until the order reaches {@code FINISHED} (the status
     * the loyalty mission engine consumes), or the timeout elapses. Replaces a fixed sleep so the
     * test waits exactly as long as the order needs to finish.
     *
     * @param orderId             the order id
     * @param timeoutSeconds      max time to wait for the {@code FINISHED} status
     * @param pollIntervalSeconds delay between status checks
     * @return {@code true} if the order reached {@code FINISHED} within the timeout, else {@code false}
     */
    @SneakyThrows
    public boolean waitForOrderFinished(String orderId, long timeoutSeconds, long pollIntervalSeconds) {
        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000;
        String status = "";
        while (true) {
            status = getOrderStatus(orderId);
            if ("FINISHED".equalsIgnoreCase(status)) {
                logger.log("Order " + orderId + " reached FINISHED status.");
                return true;
            }
            long remainingMillis = deadline - System.currentTimeMillis();
            if (remainingMillis <= 0) {
                break;
            }
            // Cap the sleep to the time left so we never wait past the configured timeout (and still
            // poll usefully even if pollIntervalSeconds is larger than timeoutSeconds).
            Thread.sleep(Math.min(pollIntervalSeconds * 1000, remainingMillis));
        }
        logger.log("Timed out after " + timeoutSeconds + "s waiting for order " + orderId
                + " to reach FINISHED; last seen status='" + status + "'.");
        return "FINISHED".equalsIgnoreCase(status);
    }

    /**
     * Reads each {@code orderDetailId} and its per-detail {@code status} from the internal order detail
     * endpoint ({@code /tix-order-go/internal/order/{orderId}/details}). Insertion order (as returned by
     * the API) is preserved.
     *
     * @param orderId the order id
     * @return a map of {@code orderDetailId -> status}, preserving API order
     */
    public LinkedHashMap<String, String> getOrderDetailStatusById(String orderId) {
        JSONArray orderDetails = getInternalOrderDetail(orderId).getJSONObject("data").getJSONArray("orderDetails");
        LinkedHashMap<String, String> orderDetailStatusById = new LinkedHashMap<>();
        for (int i = 0; i < orderDetails.length(); i++) {
            JSONObject detail = orderDetails.getJSONObject(i);
            orderDetailStatusById.put(detail.optString("orderDetailId"), detail.optString("status", ""));
        }
        logger.log("Order " + orderId + " orderDetailId -> status: " + orderDetailStatusById);
        return orderDetailStatusById;
    }

    /**
     * Resolves the {@code orderDetailId} of the order detail with the given vertical {@code type} (e.g.
     * {@code event} for ToDo) from the internal order detail endpoint.
     *
     * @param orderId the order id
     * @param type    the order-detail {@code type}, compared case-insensitively
     * @return the matching order detail id
     */
    public long getOrderDetailIdByType(String orderId, String type) {
        JSONArray orderDetails = getInternalOrderDetail(orderId).getJSONObject("data").getJSONArray("orderDetails");
        for (int i = 0; i < orderDetails.length(); i++) {
            JSONObject detail = orderDetails.getJSONObject(i);
            if (type.equalsIgnoreCase(detail.optString("type"))) {
                return detail.getLong("orderDetailId");
            }
        }
        throw new RuntimeException("Cannot find a '" + type + "' orderDetailId in order detail with orderID : " + orderId);
    }

    /**
     * Asserts the account has no entries on the order history refund list
     * ({@code /yourorder/v1/historyrefundlist}).
     */
    @SneakyThrows
    public void verifyNoRefundHistory(String username, String password) {
        int refundHistoryCount = getRefundHistoryCount(username, password);
        logger.log("Refund history count for account " + username + ": " + refundHistoryCount);
        Assertion.assertThat("User should have no refunded orders in history",
                refundHistoryCount, is(0));
    }

    /**
     * Returns the total number of refund-history cards for the account across paginated pages.
     */
    @SneakyThrows
    public int getRefundHistoryCount(String username, String password) {
        Unm<?> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password, "B2C");
        return getRefundHistoryCount(new HistoryRefundListApi.Inputs(accessToken));
    }

    @SneakyThrows
    public int getRefundHistoryCount(HistoryRefundListApi.Inputs inputs) {
        int totalCards = 0;
        int page = inputs.page();
        int maxPages = 20;

        while (page < maxPages) {
            HistoryRefundListApi historyRefundListApi = new HistoryRefundListApi(
                    new HistoryRefundListApi.Inputs(
                            inputs.accessToken(), page, inputs.deviceId(), inputs.currency(),
                            inputs.countryCode(), inputs.lang(), inputs.clientOs()));
            ApiResponse response = historyRefundListApi.hitApi(apiTools);
            Assertion.assertThat("History refund list API should succeed on page " + page,
                    response.getStatusCode(), allOf(greaterThanOrEqualTo(200), lessThan(300)));

            JSONObject body = new JSONObject(response.getBody());
            int pageCount = countRefundHistoryCards(body);
            totalCards += pageCount;
            logger.log("Refund history cards on page " + page + ": " + pageCount);

            if (pageCount == 0 || !hasMoreRefundHistoryPages(body)) {
                break;
            }
            page++;
        }
        return totalCards;
    }

    private int countRefundHistoryCards(JSONObject body) {
        JSONObject data = body.optJSONObject("data");
        if (data == null) {
            return 0;
        }

        int count = 0;
        JSONArray views = data.optJSONArray("views");
        if (views != null) {
            for (int i = 0; i < views.length(); i++) {
                JSONObject view = views.optJSONObject(i);
                if (view != null) {
                    count += countCardsInJsonObject(view);
                }
            }
        }
        count += countCardsInJsonObject(data);
        return count;
    }

    private int countCardsInJsonObject(JSONObject jsonObject) {
        int count = 0;
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.opt(key);
            if (value instanceof JSONObject section) {
                JSONArray cards = section.optJSONArray("cards");
                if (cards != null) {
                    count += cards.length();
                }
            }
        }
        JSONArray directCards = jsonObject.optJSONArray("cards");
        if (directCards != null) {
            count += directCards.length();
        }
        return count;
    }

    private boolean hasMoreRefundHistoryPages(JSONObject body) {
        JSONObject data = body.optJSONObject("data");
        if (data == null) {
            return false;
        }
        if (data.has("hasMore")) {
            return data.optBoolean("hasMore");
        }
        if (data.has("last")) {
            return !data.optBoolean("last");
        }
        JSONObject pagination = data.optJSONObject("pagination");
        if (pagination != null && pagination.has("hasNext")) {
            return pagination.optBoolean("hasNext");
        }
        return false;
    }

    /**
     * Hits the your-order trips endpoint ({@code GET /yourorder/v1/trips}), which backs the
     * itinerary/"Trips" tab of the your-order page, and returns the parsed response body.
     *
     * @param accessToken the user's access token (Authorization header)
     * @return the parsed JSON response body
     */
    @SneakyThrows
    public JSONObject getTrips(String accessToken) {
        return getTrips(new TripsListApi.Inputs(accessToken));
    }

    /**
     * Hits the your-order trips endpoint ({@code GET /yourorder/v1/trips}) with explicit inputs
     * (device id, currency, country code, lang, client OS) and returns the parsed response body.
     *
     * @param inputs the trips list API inputs
     * @return the parsed JSON response body
     */
    @SneakyThrows
    public JSONObject getTrips(TripsListApi.Inputs inputs) {
        TripsListApi tripsListApi = new TripsListApi(inputs);
        ApiResponse response = tripsListApi.hitApi(apiTools);
        Assertion.assertThat("Trips list API should succeed",
                response.getStatusCode(), allOf(greaterThanOrEqualTo(200), lessThan(300)));
        return new JSONObject(response.getBody());
    }
}
