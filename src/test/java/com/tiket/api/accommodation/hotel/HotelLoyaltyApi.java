package com.tiket.api.accommodation.hotel;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record HotelLoyaltyApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String hotelExternalId
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/HotelLoyalty_headers.properties");

        UriHelper helper = new UriHelper("https://hotel-deals-be-svc.test-hotel-cluster.tiket.com","https://hotel-deals-be-svc.preprod-hotel-cluster.tiket.com");


        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-hotel-deals/hotels/"+inputs.hotelExternalId+"/loyalties"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
