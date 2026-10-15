package com.tiket.api.orderDweb;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record WaitingForPaymentOrderApi(Input inputs) implements BaseApi{
    public record Input(
        String account
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/orderDweb/WaitingForPaymentOrderApi.properties");
        headers.put("X-Account-Id", inputs.account);
        JSONObject body = new JSONObject();
        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://lb1-ms.preprod.tiket.com");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("tix-order-composer/yourorder/v1/waitingpaymentlist"))
                .setBody(body.toString())
                .setHeaderParams(headers)
                .build();

        return apiTools.post(request);
    }
}
