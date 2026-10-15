package com.tiket.api.payment;

import java.util.Map;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import lombok.SneakyThrows;

/**
 * Updates a single field of a currency's multi-currency configuration (vertical status, user currency,
 * consumer visible, or suggested flag). The dashboard sends a partial JSON body identifying the currency by its
 * {@code id} plus the field to change, e.g. {@code {"id":"...","verticalIsActive":"1"}} or
 * {@code {"helperCode":"SGD","id":"...","isSuggested":false}}.
 */
public record MultiCurrencyConfigPUT(Inputs inputs) implements BaseApi {
    public record Inputs(String jsonBody) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/payment/MultiCurrencyConfig_headers.properties");

        UriHelper uriHelper = new UriHelper(
                "https://payment-dashboard-be-svc.test-payment-cluster.tiket.com",
                "",
                "https://payment-dashboard-be-svc.preprod-payment-cluster.tiket.com");
        String endpoint = "/tix-payment-dashboard/multi-currency/config";

        ApiRequest apiRequest = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .setBody(inputs.jsonBody())
                .build();
        return apiTools.put(apiRequest);
    }
}
