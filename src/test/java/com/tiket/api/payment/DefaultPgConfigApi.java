package com.tiket.api.payment;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.util.HashMap;
import java.util.Map;

public record DefaultPgConfigApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String cookieHeader,
            String paymentGateway
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = new HashMap<>(getStaticMap("src/test/resources/api/payment/PaymentDashboardApi_headers.properties"));
        headers.put("cookie", inputs.cookieHeader());

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("paymentGateway", inputs.paymentGateway());

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-credit-card/pgConfig/defaultPg"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .setBody("")
                .build();

        return apiTools.post(request);
    }
}
