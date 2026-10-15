package com.tiket.api.extranet.booking;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GETHotelBookingDetailApi(Inputs inputs) implements BaseApi {

    public record Inputs(int hotelId, long itineraryId, String username, long accountId, String currency) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(
                "https://hotel-native-core-be-svc.testing-hotel-cluster.tiket.com",
                "https://hotel-native-core-be-svc.preprod-hotel-cluster.tiket.com"
        );

        Map<String, Object> headers = getStaticMap("src/test/resources/api/extranet/booking/GETHotelBookingDetailApi_headers.properties");

        // Add dynamic headers
        headers.put("username", inputs.username);
        headers.put("X-Account-Id", String.valueOf(inputs.accountId));
        headers.put("currency", inputs.currency);

        String endpoint = "/tix-hotel-native-core/hotels/" + inputs.hotelId + "/bookings/" + inputs.itineraryId;

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}

