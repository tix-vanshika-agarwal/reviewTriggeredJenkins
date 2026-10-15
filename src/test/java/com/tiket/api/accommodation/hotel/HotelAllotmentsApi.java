package com.tiket.api.accommodation.hotel;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * POST /tix-api-gateway-dashboard-hotel/tix-hotel-native-core/v2/hotels/{hotelId}/allotments
 *
 * Updates the allotment / room rate for a hotel on the rates-and-availability dashboard.
 *
 * Cancellation policy is driven by {@code updateCancellationPolicy}:
 *  - true  -> refundable, using {@code cancellationChargeValue} / {@code cancellationNoShowValue} (1 day before)
 *  - false -> send null values (non-refundable)
 *
 * The room rate amount is driven by {@code rate}.
 */
public record HotelAllotmentsApi(Inputs inputs) implements BaseApi {

    private static final int DEFAULT_CANCELLATION_DAYS_BEFORE = 1;

    public record Inputs(
            String authorization,
            String cookie,
            String username,
            String accountId,
            String hotelId,
            String date,
            long allotmentId,
            long roomId,
            String roomName,
            long roomRateId,
            int ratePlanId,
            int rate,
            boolean updateCancellationPolicy,
            int cancellationChargeValue,
            int cancellationNoShowValue
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/HotelAllotments_headers.properties");
        headers.put("Authorization", inputs.authorization());
        headers.put("username", inputs.username());
        headers.put("x-username", inputs.username());
        headers.put("x-account-id", inputs.accountId());
        headers.put("Cookie", inputs.cookie());

        JSONObject body = buildBody();

        String baseUrl = "https://tix.preprod.tiket.com";
        String endpoint = "/tix-api-gateway-dashboard-hotel/tix-hotel-native-core/v2/hotels/" + inputs.hotelId() + "/allotments";

        ApiRequest request = new ApiRequest.Builder()
                .setUri(baseUrl + endpoint)
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }

    private JSONObject buildBody() {
        JSONObject room = new JSONObject();
        room.put("id", inputs.roomId());
        room.put("name", inputs.roomName());

        JSONObject roomRate = new JSONObject();
        roomRate.put("id", inputs.roomRateId());
        roomRate.put("version", JSONObject.NULL);
        roomRate.put("rate", inputs.rate());
        roomRate.put("noCheckin", false);
        roomRate.put("noCheckout", false);
        roomRate.put("closeOut", false);
        roomRate.put("minDaysStay", JSONObject.NULL);
        roomRate.put("maxDaysStay", JSONObject.NULL);
        roomRate.put("minDaysBooking", JSONObject.NULL);
        roomRate.put("maxDaysBooking", JSONObject.NULL);
        roomRate.put("cancellationPolicy", buildCancellationPolicy());
        roomRate.put("roomRateOccupancyBasedList", buildOccupancyBasedList());
        roomRate.put("childrenRoomRates", new JSONArray());
        roomRate.put("losRoomRates", JSONObject.NULL);
        roomRate.put("allotment", JSONObject.NULL);
        roomRate.put("usedAllotment", 0);
        roomRate.put("allotmentUpdatedDate", JSONObject.NULL);
        roomRate.put("allotmentUpdatedBy", JSONObject.NULL);
        roomRate.put("soldOut", true);
        roomRate.put("almostSoldOut", false);
        roomRate.put("occupancyRates", new JSONArray(new int[]{0, 0, 0, 0, 0}));
        roomRate.put("minStayThrough", JSONObject.NULL);
        roomRate.put("maxStayThrough", JSONObject.NULL);
        roomRate.put("breakfast", JSONObject.NULL);
        roomRate.put("lunch", JSONObject.NULL);
        roomRate.put("dinner", JSONObject.NULL);
        roomRate.put("allInclusive", JSONObject.NULL);
        roomRate.put("childRateFromMaster", JSONObject.NULL);
        roomRate.put("mealPlan", buildMealPlan());
        roomRate.put("ratePlanId", inputs.ratePlanId());

        JSONObject allotment = new JSONObject();
        allotment.put("id", inputs.allotmentId());
        allotment.put("version", JSONObject.NULL);
        allotment.put("room", room);
        allotment.put("date", inputs.date());
        allotment.put("allotment", JSONObject.NULL);
        allotment.put("usedAllotment", 0);
        allotment.put("closeOut", false);
        allotment.put("softblockAllotment", 0);
        allotment.put("softblockCloseOut", false);
        allotment.put("noCheckin", false);
        allotment.put("noCheckout", false);
        allotment.put("softblockUsedAllotment", 0);
        allotment.put("softblockMaxUsed", 0);
        allotment.put("isCalendarOverride", JSONObject.NULL);
        allotment.put("roomRates", new JSONArray().put(roomRate));
        allotment.put("occupancyBasedRate", false);
        allotment.put("vendor", JSONObject.NULL);

        JSONObject body = new JSONObject();
        body.put("hotelId", inputs.hotelId());
        body.put("allotments", new JSONArray().put(allotment));
        return body;
    }

    /**
     * true  -> refundable using the supplied charge & no-show percentages (1 day before).
     * false -> all charge fields null and non-refundable.
     */
    private JSONObject buildCancellationPolicy() {
        JSONObject cancellationPolicy = new JSONObject();
        cancellationPolicy.put("cancellationActive", true);
        if (inputs.updateCancellationPolicy()) {
            cancellationPolicy.put("cancellationChargeType", "percentage");
            cancellationPolicy.put("cancellationChargeValue", inputs.cancellationChargeValue());
            cancellationPolicy.put("cancellationDaysBefore", DEFAULT_CANCELLATION_DAYS_BEFORE);
            cancellationPolicy.put("cancellationNoShowType", "percentage");
            cancellationPolicy.put("cancellationNoShowValue", inputs.cancellationNoShowValue());
            cancellationPolicy.put("cancellationRefundable", true);
        } else {
            cancellationPolicy.put("cancellationChargeType", JSONObject.NULL);
            cancellationPolicy.put("cancellationChargeValue", JSONObject.NULL);
            cancellationPolicy.put("cancellationDaysBefore", JSONObject.NULL);
            cancellationPolicy.put("cancellationNoShowType", JSONObject.NULL);
            cancellationPolicy.put("cancellationNoShowValue", JSONObject.NULL);
            cancellationPolicy.put("cancellationRefundable", false);
        }
        return cancellationPolicy;
    }

    private JSONArray buildOccupancyBasedList() {
        JSONArray occupancyBasedList = new JSONArray();
        for (int guest = 5; guest >= 1; guest--) {
            JSONObject occupancy = new JSONObject();
            occupancy.put("id", JSONObject.NULL);
            occupancy.put("version", JSONObject.NULL);
            occupancy.put("guest", guest);
            occupancy.put("rate", 0);
            occupancy.put("losOccupancyRoomRateList", JSONObject.NULL);
            occupancyBasedList.put(occupancy);
        }
        return occupancyBasedList;
    }

    private JSONObject buildMealPlan() {
        JSONObject mealPlan = new JSONObject();
        mealPlan.put("breakfast", false);
        mealPlan.put("lunch", false);
        mealPlan.put("dinner", false);
        mealPlan.put("allInclusive", false);
        mealPlan.put("source", "RATE_PLAN");
        return mealPlan;
    }
}
