package com.tiket.api.flight;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record FlightGetCartApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String cartId
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/FlightCart_headers.properties");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-flight-cart/v5/cart/" + inputs.cartId))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
