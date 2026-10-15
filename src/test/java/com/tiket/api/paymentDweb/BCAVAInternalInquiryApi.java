package com.tiket.api.paymentDweb;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;

public record BCAVAInternalInquiryApi(Input inputs) implements BaseApi {

    private static final String GK_URL = "https://api-gateway-payment-partner-svc.preprod-payment-cluster.tiket.com";
    private static final String PREPROD_URL = "https://api-gateway-payment-partner-svc.preprod-payment-cluster.tiket.com";
    private static final String ENDPOINT = "/payment-gateway-partner/tix-bank-transfer/bca/openapi/v1.0/transfer-va/inquiry";
    private static final String BODY_PATH = "src/test/resources/api/paymentDweb/BCAVASnapInquiryApi_body.json";

    public record Input(
            String accessToken,
            String customerNo,
            String virtualAccountNo,
            String orderId
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/paymentDweb/BCAVAInternalInquiryApiHeaders.properties");
        headers.put("Authorization", "Bearer " + inputs.accessToken());
        headers.put("X-EXTERNAL-ID", UUID.randomUUID().toString());

        String trxDateInit = String.valueOf(headers.get("X-TIMESTAMP"));
        JSONObject body = getStaticJson(BODY_PATH);
        String partnerServiceId = body.getString("partnerServiceId");

        body.put("customerNo", inputs.customerNo());
        body.put("virtualAccountNo", inputs.virtualAccountNo());
        body.put("trxDateInit", trxDateInit);
        body.put("inquiryRequestId", inputs.orderId());

        UriHelper helper = new UriHelper(GK_URL, PREPROD_URL);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(ENDPOINT))
                .setBody(body.toString())
                .setHeaderParams(headers)
                .build();
        return apiTools.post(request);
    }
}