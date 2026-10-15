package com.tiket.api.payment;

import java.util.LinkedHashMap;
import java.util.Map;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import lombok.SneakyThrows;

public record MultiCurrencyConfigGET(Inputs inputs) implements BaseApi {
    public record Inputs(
            String currencyFilter,
            String userIsActive,
            String verticalIsActive,
            String gbvFilter,
            String currencyCodeSearch
    ) {
        public Inputs(String currencyCodeSearch) {
            this("USER_CURRENCY", "ALL", "ALL", "ALL_GBV", currencyCodeSearch);
        }
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/payment/MultiCurrencyConfig_headers.properties");

        Map<String, Object> queryParams = new LinkedHashMap<>();
        queryParams.put("currencyFilter", inputs.currencyFilter());
        queryParams.put("userIsActive", inputs.userIsActive());
        queryParams.put("verticalIsActive", inputs.verticalIsActive());
        queryParams.put("gbvFilter", inputs.gbvFilter());
        queryParams.put("page", "0");
        queryParams.put("size", "999");
        queryParams.put("currencyCodeSearch", inputs.currencyCodeSearch() == null ? "" : inputs.currencyCodeSearch());
        queryParams.put("isDashboard", "true");

        UriHelper uriHelper = new UriHelper(
                "https://payment-dashboard-be-svc.test-payment-cluster.tiket.com",
                "",
                "https://payment-dashboard-be-svc.preprod-payment-cluster.tiket.com");
        String endpoint = "/tix-payment-dashboard/multi-currency/config";

        ApiRequest apiRequest = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint(endpoint))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();
        return apiTools.get(apiRequest);
    }
}
