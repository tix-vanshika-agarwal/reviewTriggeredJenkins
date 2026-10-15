package com.tiket.api.flight;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record FlightGetOrderApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String orderId,
            String email
    ) {
        public Inputs(String orderId) {
            this(orderId, null);
        }
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(
                "https://flight-core-be-svc.test-flight-cluster.tiket.com",
                "https://flight-core-be-svc.preprod-flight-cluster.tiket.com"
        );

        String uri = "/tix-flight-core/order/retrieve/" + inputs().orderId;

        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/FlightGetOrder_headers.properties");
        if (inputs().email != null && !inputs().email.isBlank()) {
            headers.put("username", inputs().email);
        }

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(uri))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }

}
