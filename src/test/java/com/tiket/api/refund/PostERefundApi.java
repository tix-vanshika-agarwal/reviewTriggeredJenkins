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
 * User-facing e-refund creation call:
 * {@code POST /ms-gateway/tix-refund/external/refunds/e-refund}.
 *
 * <p>Second step of the storefront e-refund flow. The {@code amountDetails} and {@code paymentMethods} are
 * taken from the preceding {@link PostRefundEstimationApi} response so the request mirrors exactly what the
 * "my-refund" UI submits. A successful call returns {@code 201} with {@code data.id} (the refund request id),
 * {@code data.requestHash} (used by the {@code /track} status call) and {@code data.paymentMethods[].id}
 * (the payment method id the admin force-submit needs). Authenticated with the <b>user</b> access token.
 */
public record PostERefundApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            long orderId,
            long orderDetailId,
            String reasonId,
            String paymentMethodKey,
            JSONArray amountDetails,
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
                .put("amountDetails", inputs.amountDetails)
                .put("estimationLogId", inputs.estimationLogId == null ? "" : inputs.estimationLogId)
                .put("documentIds", new JSONArray())
                .put("orderId", inputs.orderId)
                .put("orderDetailId", inputs.orderDetailId)
                .put("reasonId", inputs.reasonId)
                .put("items", new JSONArray())
                .put("paymentMethods", new JSONArray().put(inputs.paymentMethodKey))
                .put("token", "")
                .put("thirdPartyRequirements", JSONObject.NULL)
                .put("refundGenericParams", JSONObject.NULL)
                .put("isForceToBankTransfer", false);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("tix-refund/external/refunds/e-refund"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
