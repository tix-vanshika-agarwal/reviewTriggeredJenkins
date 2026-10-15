package com.tiket.api.order;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import java.util.Map;


public record CoreOrderIDApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String orderId
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/order/CoreOrderID_headers.properties");

        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://lb1-ms.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-events-v2-order/admins/orders/coreOrderId/"+inputs.orderId+""))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }

}
