package com.tiket.api.payment;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.payment.ConfigUserTickerPOSTBody;
import com.tiket.testbase.BaseApi;

import lombok.SneakyThrows;

public record ConfigUserTickerPOST(Inputs inputs) implements BaseApi{
    public record Inputs(
        List<String> messageEn,
        List<String> messageId
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/payment/ConfigUserTickerPOST_headers.properties");
        Gson gson = new Gson();
        if (inputs.messageEn.size() != inputs.messageId.size()) {
            throw new IllegalArgumentException("messageEn and messageId must have the same number of elements");
        }

        // Build list of messages (results in JSON array output)
        List<ConfigUserTickerPOSTBody.CurrencyMessage> messages = new ArrayList<>();
        for (int i = 0; i < inputs.messageEn.size(); i++) {
            messages.add(new ConfigUserTickerPOSTBody.CurrencyMessage(
                    inputs.messageEn.get(i),
                    inputs.messageId.get(i)
            ));
        }

        String jsonBody = gson.toJson(messages, new TypeToken<List<ConfigUserTickerPOSTBody.CurrencyMessage>>() {}.getType());

        UriHelper uriHelper = new UriHelper("https://payment-dashboard-be-svc.test-payment-cluster.tiket.com", "", "https://payment-dashboard-be-svc.preprod-payment-cluster.tiket.com");
        String endpoint = "/tix-payment-dashboard/multi-currency/config/user/ticker";
        ApiRequest apiRequest = new ApiRequest.Builder()
        .setUri(uriHelper.withEndpoint(endpoint))
        .setHeaderParams(headers)
        .setBody(jsonBody)
        .build();
        return apiTools.post(apiRequest);
    }
}
