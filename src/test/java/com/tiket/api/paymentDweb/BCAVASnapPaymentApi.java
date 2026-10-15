package com.tiket.api.paymentDweb;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record BCAVASnapPaymentApi(Input inputs) implements BaseApi {

    private static final String BASE_URL = "https://api-gateway-payment-partner-svc.preprod-payment-cluster.tiket.com";
    private static final String ENDPOINT = "/payment-gateway-partner/tix-bank-transfer/bca/openapi/v1.0/transfer-va/payment";
    private static final String BODY_PATH = "src/test/resources/api/paymentDweb/BCAVASnapPaymentApi_body.json";
    private static final String VA_PREFIX = "78001";

    public record Input(
            String accessToken,
            String customerNo,
            long paymentAmount,
            String virtualAccountName,
            String subCompany
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/paymentDweb/BCAVASnapPaymentApiHeaders.properties");
        headers.put("Authorization", "Bearer " + inputs.accessToken());

        String trxDateTime = String.valueOf(headers.get("X-TIMESTAMP"));
        String amountValue = String.valueOf(inputs.paymentAmount());

        JSONObject body = getStaticJson(BODY_PATH);
        String partnerServiceId = body.getString("partnerServiceId");

        body.put("customerNo", inputs.customerNo());
        body.put("virtualAccountNo", partnerServiceId + inputs.customerNo());
        body.put("virtualAccountName", inputs.virtualAccountName());
        body.put("paymentRequestId", VA_PREFIX + inputs.customerNo());
        body.getJSONObject("paidAmount").put("value", amountValue);
        body.getJSONObject("totalAmount").put("value", amountValue);
        body.put("trxDateTime", trxDateTime);
        body.put("subCompany", inputs.subCompany());

        UriHelper helper = new UriHelper(BASE_URL);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(ENDPOINT))
                .setBody(body.toString())
                .setHeaderParams(headers)
                .build();
        return apiTools.post(request);
    }
}
