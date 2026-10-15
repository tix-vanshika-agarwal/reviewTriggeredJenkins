package com.tiket.api.payment;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.payment.PaymentCorePromoApplyBody;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record PaymentCorePromoApplyPOST(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String orderId,
            String orderHash,
            String discountCode,
            String currency,
            String paymentType,
            String productType,
            long totalPrice,
            String cardNumber,
            String cardNumberToken,
            String binNumber,
            String deviceId
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/payment/PaymentCorePromoApply_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        if (inputs.deviceId != null && !inputs.deviceId.isBlank()) {
            headers.put("deviceid", inputs.deviceId);
        }

        JSONObject bodyJson = getStaticJson("src/test/resources/api/payment/PaymentCorePromoApply_body.json");
        Gson gson = new Gson();
        PaymentCorePromoApplyBody body = gson.fromJson(bodyJson.toString(), PaymentCorePromoApplyBody.class);
        body.orderId = inputs.orderId;
        body.orderHash = inputs.orderHash;
        body.discountCode = inputs.discountCode;
        body.currency = inputs.currency;
        body.paymentType = inputs.paymentType;
        body.productType = inputs.productType;
        body.totalPrice = inputs.totalPrice;

        if (inputs.cardNumber != null && !inputs.cardNumber.isBlank()) {
            body.cardNumber = inputs.cardNumber;
        }
        if (inputs.cardNumberToken != null && !inputs.cardNumberToken.isBlank()) {
            body.cardNumberToken = inputs.cardNumberToken;
        }
        if (inputs.binNumber != null && !inputs.binNumber.isBlank()) {
            body.binNumber = inputs.binNumber;
        }

        UriHelper uriHelper = new UriHelper("https://gatotkaca.tiket.com/ms-gateway", "https://tiket.com/ms-gateway", "https://preprod.tiket.com/ms-gateway");
        String endpoint = "/tix-payment-core/promo/v2/apply";
        ApiRequest apiRequest = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .setBody(gson.toJson(body, PaymentCorePromoApplyBody.class))
                .build();
        return apiTools.post(apiRequest);
    }
}
