package com.tiket.api.payment;

import com.google.gson.Gson;
import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.payment.SubmitSakukuBody;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public record SubmitSakukuApi(Inputs inputs) implements BaseApi {
    
    public record Inputs(
        String accessToken,
        // int accountId,
        String orderHash,
        long referenceId,
        // true pays through the gateway with the browser's headers and paymentData, which a promo-applied order needs
        boolean viaGateway
    ) {
        public Inputs(String accessToken, String orderHash, long referenceId) {
            this(accessToken, orderHash, referenceId, false);
        }
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", inputs.accessToken);
        headers.put("X-Username", "username");
        headers.put("X-Channel-Id", "WEB");
        headers.put("X-Service-Id", "gateway");
        headers.put("X-Store-Id", "TIKETCOM");

        JSONObject body = getStaticJson("src/test/resources/api/payment/SubmitSakuku_body.json");
        Gson gson = new Gson();
        SubmitSakukuBody submitSakukuBody = gson.fromJson(body.toString(), SubmitSakukuBody.class);
        submitSakukuBody.orderHash = inputs.orderHash;
        submitSakukuBody.referenceId = inputs.referenceId;

        UriHelper baseUrl = new UriHelper("https://payment-core-be-svc.test-payment-cluster.tiket.com", "", "https://payment-core-be-svc.preprod-payment-cluster.tiket.com");
        String endpoint = "/tix-payment-core/payment/detail/SAKUKU";
        if (inputs.viaGateway) {
            headers = getStaticMap("src/test/resources/api/payment/PaymentCorePromoSuggestion_headers.properties");
            headers.put("Authorization", inputs.accessToken);
            headers.put("deviceid", CoreFunctions.generateDeviceId());
            submitSakukuBody.paymentData = new SubmitSakukuBody.PaymentData();
            submitSakukuBody.paymentData.fdsToken = CoreFunctions.generateDeviceId().replace("-", "") + "_" + System.currentTimeMillis() + "__UDF43-m4_27ck__tt";
            baseUrl = new UriHelper("https://gatotkaca.tiket.com/ms-gateway", "https://tiket.com/ms-gateway", "https://preprod.tiket.com/ms-gateway");
        }
        ApiRequest apiRequest = new ApiRequest.Builder()
                .setUri(baseUrl.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .setBody(gson.toJson(submitSakukuBody, SubmitSakukuBody.class))
                .build();

        return apiTools.put(apiRequest);
    }
}
