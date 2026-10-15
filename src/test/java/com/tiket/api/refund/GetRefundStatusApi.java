package com.tiket.api.refund;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GetRefundStatusApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String refundRequestId
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://gatotkaca.tiket.com/ms-gateway","https://preprod.tiket.com/ms-gateway");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/refund/GetRefundStatus_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        String endpoint = "/tix-refund/dashboard/refunds";
        helper.addQueryParam("customerType", "b2b_corp");
        helper.addQueryParam("searchByKey", "refundRequestId");
        helper.addQueryParam("refundRequestId", inputs.refundRequestId);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }

}
