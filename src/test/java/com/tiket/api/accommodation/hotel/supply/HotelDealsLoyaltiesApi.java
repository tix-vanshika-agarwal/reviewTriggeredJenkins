package com.tiket.api.accommodation.hotel.supply;

import com.tiket.io.api.ApiTools;
import com.tiket.model.Environment;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;

import java.util.Map;

public record HotelDealsLoyaltiesApi(Inputs inputs) implements BaseApi {

    public record Inputs(String authorization, String cookie, int hotelId) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(
                "https://tix.gatotkaca.tiket.com",
                "https://tix.preprod.tiket.com"
        );

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/HotelDealsLoyaltiesApi_headers.properties");
        headers.put("authorization", inputs.authorization());
        headers.put("referer", getRefererUrl());
        if (inputs.cookie() != null && !inputs.cookie().isBlank()) {
            headers.put("cookie", inputs.cookie());
        }

        String endpoint = "/tix-api-gateway-dashboard-hotel/tix-hotel-deals/hotels/" + inputs.hotelId() + "/loyalties";

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }

    private static String getRefererUrl() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            return "https://tix.gatotkaca.tiket.com/en/app/promotions-and-campaigns/blibli-tiket-rewards";
        }
        return "https://tix.preprod.tiket.com/en/app/promotions-and-campaigns/blibli-tiket-rewards";
    }
}
