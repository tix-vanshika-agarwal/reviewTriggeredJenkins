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

public record SubmitCreditCardV2(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String orderHash,
            long referenceId,
            String cardNumber,
            String cardHolderName,
            String cardExpiredMonth,
            String cardExpiredYear,
            String cardCvvNumber,
            String paymentKey,
            String subPaymentMethod,
            String currency
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/payment/SubmitCreditCardV2_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("currency", inputs.currency);
        headers.put("x-currency", inputs.currency);


        JSONObject body = getStaticJson("src/test/resources/api/payment/SubmitCreditCardV2_body.json");
        Gson gson = new Gson();
        SubmitCreditCardBody submitCreditCardBody = gson.fromJson(body.toString(), SubmitCreditCardBody.class);
        submitCreditCardBody.orderHash = inputs.orderHash;
        submitCreditCardBody.referenceId = inputs.referenceId;
        submitCreditCardBody.paymentKey = inputs.paymentKey;
        submitCreditCardBody.subPaymentMethod = inputs.subPaymentMethod;
        submitCreditCardBody.paymentData.creditCardRequest.cardNumber = inputs.cardNumber;
        submitCreditCardBody.paymentData.creditCardRequest.cardHolderName = inputs.cardHolderName;
        submitCreditCardBody.paymentData.creditCardRequest.cardExpiredMonth = inputs.cardExpiredMonth;
        submitCreditCardBody.paymentData.creditCardRequest.cardExpiredYear = inputs.cardExpiredYear;
        submitCreditCardBody.paymentData.creditCardRequest.cardCvvNumber = inputs.cardCvvNumber;
        submitCreditCardBody.paymentData.creditCardRequest.sessionId = inputs.accessToken.replace("Bearer ", "").trim();
        UriHelper baseUrl = uriHelper;
        ApiRequest apiRequest = new ApiRequest.Builder()
                .setUri(baseUrl.withEndpoint("ms-gateway/tix-payment-core/payment/detail/CREDITCARD"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(submitCreditCardBody, SubmitCreditCardBody.class))
                .build();

        return apiTools.put(apiRequest);
    }
}