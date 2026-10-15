package com.tiket.api.payment;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record SubmitPaymentApiB2B(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String orderHash,
            long referenceId,
            int employeeId,
            int corporateId
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/payment/SubmitPaymentB2B_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        JSONObject body = getStaticJson("src/test/resources/api/payment/SubmitPaymentB2B_body.json");
        body.put("orderHash", inputs.orderHash);
        body.put("referenceId", inputs.referenceId);
        body.getJSONObject("paymentData").put("employeeId", inputs.employeeId);
        body.getJSONObject("paymentData").put("corporateId", inputs.corporateId);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-payment-core/payment/detail/CORPORATE_BILLING"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
