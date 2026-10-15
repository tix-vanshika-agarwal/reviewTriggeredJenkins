package com.tiket.api.order;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.util.Map;

public record CompleteOrderListApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/order/OrderList_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-order-composer/yourorder/v1/activeorderlist"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}