package com.tiket.api.payment;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.util.HashMap;
import java.util.Map;

public record PromoNudgeApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String cookieHeader,
            String currency
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = new HashMap<>(getStaticMap("src/test/resources/api/payment/PaymentDashboardApi_headers.properties"));
        headers.put("cookie", inputs.cookieHeader());

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("currency", inputs.currency());
        queryParams.put("page", 0);
        queryParams.put("size", 100);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-payment-dashboard/promo-nudge"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
