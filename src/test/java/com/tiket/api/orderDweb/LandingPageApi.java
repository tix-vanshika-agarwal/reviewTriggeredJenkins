package com.tiket.api.orderDweb;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;


public record LandingPageApi(Input input) implements BaseApi{
    public record Input(
        String referenceId,
        String orderHash
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/ferry/AddToCartFerryHeaders.properties");
        JSONObject body = new JSONObject();
        body.put("cardNumber", "");
        body.put("referenceId", input.referenceId);
        body.put("orderHash", input.orderHash);
        body.put("paymentMethod", "");
        body.put("affiliatePartnerId", "");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-payment-core/payment/v4/landing"))
                .setBody(body.toString())
                .setHeaderParams(headers)
                .build();

        return apiTools.post(request);
    }
}

