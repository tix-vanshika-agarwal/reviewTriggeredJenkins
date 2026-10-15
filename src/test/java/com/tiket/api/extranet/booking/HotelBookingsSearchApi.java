package com.tiket.api.extranet.booking;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public record HotelBookingsSearchApi(Inputs inputs) implements BaseApi {

    public record Inputs(String accessToken, int hotelId, String orderId, String startDate, String endDate, String username, long accountId) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://tix.gatotkaca.tiket.com", "https://tix.preprod.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/HotelBookingsSearchApi_headers.properties");

        String requestId = java.util.UUID.randomUUID().toString().replace("-", "");
        headers.put("authorization", inputs.accessToken);
        headers.put("requestid", requestId);
        headers.put("username", inputs.username);
        headers.put("x-account-id", String.valueOf(inputs.accountId));
        headers.put("x-request-id", requestId);
        headers.put("x-username", inputs.username);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("dateType", "check_in");
        queryParams.put("startDate", inputs.startDate);
        queryParams.put("endDate", inputs.endDate);
        queryParams.put("orderId", inputs.orderId);

        String endpoint = "/tix-api-gateway-dashboard-hotel/tix-hotel-native-core/hotels/" + inputs.hotelId + "/bookings";

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}

