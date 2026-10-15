package com.tiket.api.order;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record OrderListApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String username,
            String accessToken
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/order/OrderList_headers.properties");
        headers.put("Username", inputs.username);
        headers.put("Authorization", inputs.accessToken);

        JSONObject body = getStaticJson("src/test/resources/api/order/OrderList_body.json");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-my-order-core/order-list/active-order"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
