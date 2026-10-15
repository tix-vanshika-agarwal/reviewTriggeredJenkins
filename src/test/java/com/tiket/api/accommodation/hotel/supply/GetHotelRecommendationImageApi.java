package com.tiket.api.accommodation.hotel.supply;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.UUID;

/** GET /tix-hotel-core/hotel/{hotelId}/recommendation-image — include/exclude photos of a hotel with their {@code image_raw} ids. */
public record GetHotelRecommendationImageApi(Inputs inputs) implements BaseApi {

    public record Inputs(String hotelId) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(
                "http://hotel-core-be.accom-supply-ns.svc.tiket",
                "https://hotel-core-be-svc.preprod-hotel-cluster.tiket.com");

        Map<String, Object> headers = getStaticMap(
                "src/test/resources/api/accommodation/hotel/supply/GetHotelRecommendationImageApi_headers.properties");
        headers.put("requestId", UUID.randomUUID().toString());

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-hotel-core/hotel/" + inputs.hotelId() + "/recommendation-image"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
