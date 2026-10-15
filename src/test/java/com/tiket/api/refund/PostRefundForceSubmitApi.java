package com.tiket.api.refund;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record PostRefundForceSubmitApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String status,
            String orderId,
            String refundRequestId,
            int paymentId
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://gatotkaca.tiket.com/ms-gateway","https://preprod.tiket.com/ms-gateway");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/refund/GetRefundStatus_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        JSONObject body = getStaticJson("src/test/resources/api/refund/PostRefundForceSubmitApi_body.json");
        body.put("status", inputs.status);
        body.put("orderId", Integer.parseInt(inputs.orderId));
        body.getJSONArray("refundRequestIds").put(Integer.parseInt(inputs.refundRequestId));
        body.getJSONArray("refundList").getJSONObject(0).put("refundRequestId", Integer.parseInt(inputs.refundRequestId));
        body.getJSONArray("refundList").getJSONObject(0).put("paymentMethodId", (inputs.paymentId));

        ApiRequest request = new ApiRequest.Builder()
            .setUri(helper.withEndpoint("tix-refund/dashboard/refunds/finance/force-submit"))
            .setHeaderParams(headers)
            .setBody(body.toString())
            .build();

        return apiTools.post(request);
    }

}

