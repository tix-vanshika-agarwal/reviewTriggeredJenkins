package com.tiket.api.refund;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record POSTAutoRefundApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String auth,
            String orderId,
            String orderDetailId,
            String refundReason,
            String refundSubmissionExpiry
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://lb1-ms.preprod.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/refund/POSTAutoRefund_headers.properties");
        headers.put("Authorization", inputs.auth);

        JSONObject body = getStaticJson("src/test/resources/api/refund/POSTAutoRefund_body.json");
        body.put("orderId", Integer.parseInt(inputs.orderId));
        body.put("orderDetailId", Integer.parseInt(inputs.orderDetailId));
        body.put("refundReason", inputs.refundReason);
        body.put("refundSubmissionExpiry", inputs.refundSubmissionExpiry);

        ApiRequest request = new ApiRequest.Builder()
            .setUri(helper.withEndpoint("tix-refund/svc/refunds/automatic-refund"))
            .setHeaderParams(headers)
            .setBody(body.toString())
            .build();

        return apiTools.post(request);
    }
}