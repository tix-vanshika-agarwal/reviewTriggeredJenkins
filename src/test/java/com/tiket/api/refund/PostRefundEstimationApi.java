package com.tiket.api.refund;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * User-facing e-refund estimation call:
 * {@code POST /ms-gateway/tix-refund/external/refunds/{orderId}/{orderDetailId}/estimation}.
 *
 * <p>This is the first step of the storefront ("my-refund") e-refund flow. It returns the refundable
 * {@code amountDetails} and the eligible refund method(s) that the subsequent {@code /external/refunds/e-refund}
 * create call must echo back. Authenticated with the <b>user</b> access token (Bearer), not an admin token.
 */
public record PostRefundEstimationApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            long orderId,
            long orderDetailId,
            String reasonId,
            String estimationLogId
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://gatotkaca.tiket.com/ms-gateway", "https://preprod.tiket.com/ms-gateway");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/refund/RefundExternal_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        JSONObject body = new JSONObject()
                .put("estimationLogId", inputs.estimationLogId == null ? "" : inputs.estimationLogId)
                .put("estimationType", JSONObject.NULL)
                .put("isForceToBankTransfer", false)
                .put("itemIds", new JSONArray())
                .put("reasonId", inputs.reasonId);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("tix-refund/external/refunds/" + inputs.orderId + "/" + inputs.orderDetailId + "/estimation"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
