package com.tiket.api.payment;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.payment.SubmitCreditCardBody;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public record SubmitCreditCardApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String orderHash,
            long referenceId,
            String cardNumber,
            String cardHolderName,
            String cardExpiredMonth,
            String cardExpiredYear,
            String cardCvvNumber
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", inputs.accessToken);
        headers.put("x-audience", "tiket.com");
        headers.put("currency", "IDR");
        headers.put("lang", "en");

        JSONObject body = getStaticJson("src/test/resources/api/payment/SubmitCreditCard_body.json");
        Gson gson = new Gson();
        SubmitCreditCardBody submitCreditCardBody = gson.fromJson(body.toString(), SubmitCreditCardBody.class);
        submitCreditCardBody.orderHash = inputs.orderHash;
        submitCreditCardBody.referenceId = inputs.referenceId;
        submitCreditCardBody.paymentData.creditCardRequest.cardNumber = inputs.cardNumber;
        submitCreditCardBody.paymentData.creditCardRequest.cardHolderName = inputs.cardHolderName;
        submitCreditCardBody.paymentData.creditCardRequest.cardExpiredMonth = inputs.cardExpiredMonth;
        submitCreditCardBody.paymentData.creditCardRequest.cardExpiredYear = inputs.cardExpiredYear;
        submitCreditCardBody.paymentData.creditCardRequest.cardCvvNumber = inputs.cardCvvNumber;
        // sessionId = account sub extracted from the JWT (strip "Bearer " prefix, decode to get sub)
        submitCreditCardBody.paymentData.creditCardRequest.sessionId = extractSubFromToken(inputs.accessToken);

        UriHelper baseUrl = new UriHelper("https://gatotkaca.tiket.com", "https://tiket.com", "https://preprod.tiket.com");
        ApiRequest apiRequest = new ApiRequest.Builder()
                .setUri(baseUrl.withEndpoint("ms-gateway/tix-payment-core/payment/detail/CREDITCARD"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(submitCreditCardBody, SubmitCreditCardBody.class))
                .build();

        return apiTools.put(apiRequest);
    }

    /**
     * Extracts the JWT subject (sub) from a Bearer token.
     * The sub is used as the sessionId in the CC payment body.
     * Example: "Bearer eyJ..." → decodes payload → returns the "sub" field value.
     * Falls back to the raw token if decoding fails.
     */
    private String extractSubFromToken(String bearerToken) {
        try {
            String jwt = bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : bearerToken;
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) return bearerToken;
            String payloadJson = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
            JSONObject payload = new JSONObject(payloadJson);
            return payload.optString("sub", bearerToken);
        } catch (Exception e) {
            return bearerToken;
        }
    }
}
