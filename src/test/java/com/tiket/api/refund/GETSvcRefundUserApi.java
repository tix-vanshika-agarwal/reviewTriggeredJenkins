package com.tiket.api.refund;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GETSvcRefundUserApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String auth,
            String orderId,
            String accountId
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://lb1-ms.preprod.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/refund/GETSvcRefundUser_headers.properties");

        headers.put("Authorization", inputs.auth);
        headers.put("X-Account-Id", inputs.accountId);

        String endpoint = "/tix-refund/svc/refunds/user";
        helper.addQueryParam("orderId", inputs.orderId);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}