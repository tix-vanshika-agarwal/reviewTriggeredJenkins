package com.tiket.api.accommodation.hotel.supply;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import java.util.Map;

public record HotelChangeNameListenerApi(Inputs inputs) implements BaseApi {

    public record Inputs(
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://hotel-core-be-svc.test-hotel-cluster.tiket.com","https://hotel-core-be-svc.preprod-hotel-cluster.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/supply/HotelChangeNameListener_headers.properties");

        Gson gson = new Gson();
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-hotel-core/kafka/start?listenerId=hotel-core-hotelChangeNameListenerV2"))
                .setHeaderParams(headers)
                .build();

        return apiTools.post(request);
    }

}
