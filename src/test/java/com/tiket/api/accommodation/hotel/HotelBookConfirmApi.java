package com.tiket.api.accommodation.hotel;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public record HotelBookConfirmApi(Inputs inputs) implements BaseApi {

    public record Inputs(String itineraryId, String bookingToken) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/HotelBookConfirmApi_headers.properties");

        JSONObject body = new JSONObject();

        body.put("itineraryId", Integer.parseInt(inputs.itineraryId()));
        body.put("bookingToken", inputs.bookingToken());
        JSONObject priceThreshold = new JSONObject();
        priceThreshold.put("percentage", 0);
        priceThreshold.put("maxAmount", 0);
        priceThreshold.put("currency", "IDR");
        body.put("priceThreshold", priceThreshold);
        int randomOrderId = ThreadLocalRandom.current().nextInt(100000, 999999);
        body.put("orderId", randomOrderId);
        body.put("isMultiOrder", true);

        String baseUrl = "https://hotel-native-booking-v2-be-svc.preprod-hotel-cluster.tiket.com";
        String endpoint = "/tix-hotel-native-api/v2/hotel-api/book/confirm";

        ApiRequest request = new ApiRequest.Builder()
                .setUri(baseUrl + endpoint)
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}