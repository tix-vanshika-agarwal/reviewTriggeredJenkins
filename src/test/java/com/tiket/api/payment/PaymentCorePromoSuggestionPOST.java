package com.tiket.api.payment;

import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record PaymentCorePromoSuggestionPOST(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String orderId,
            String orderHash,
            String currency,
            String productType,
            long totalPrice
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/payment/PaymentCorePromoSuggestion_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("deviceid", CoreFunctions.generateDeviceId());
        headers.put("x-request-id", CoreFunctions.generateDeviceId());

        JSONObject body = new JSONObject();
        body.put("currency", inputs.currency);
        body.put("productType", inputs.productType);
        body.put("size", 100);
        body.put("page", 0);
        body.put("lang", "en");
        body.put("totalPrice", inputs.totalPrice);
        body.put("orderId", Long.parseLong(inputs.orderId));
        body.put("orderHash", inputs.orderHash);

        UriHelper uriHelper = new UriHelper("https://gatotkaca.tiket.com/ms-gateway", "https://tiket.com/ms-gateway", "https://preprod.tiket.com/ms-gateway");
        ApiRequest apiRequest = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/tix-payment-core/promo/suggestion"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();
        return apiTools.post(apiRequest);
    }
}
