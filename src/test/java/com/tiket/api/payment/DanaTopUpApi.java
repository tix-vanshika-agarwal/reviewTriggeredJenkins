package com.tiket.api.payment;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.RestContentType;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.UUID;

public record DanaTopUpApi(Inputs inputs) implements BaseApi {

    private static final String BASE_URL = "https://dashboard-sandbox.dana.id";
    private static final String ENDPOINT = "/merchant-portal-app/api/sandbox-tools/execute";

    public record Inputs(String phoneNumber, long amount) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        String customerNumber = inputs.phoneNumber().startsWith("0")
                ? inputs.phoneNumber() : "0" + inputs.phoneNumber();

        JSONObject amountObj = new JSONObject();
        amountObj.put("value", String.format("%.2f", (double) inputs.amount()));
        amountObj.put("currency", "IDR");

        JSONObject feeAmount = new JSONObject();
        feeAmount.put("value", "0.00");
        feeAmount.put("currency", "IDR");

        JSONObject additionalInfo = new JSONObject();
        additionalInfo.put("fundType", "AGENT_TOPUP_FOR_USER_SETTLE");

        JSONObject requestBody = new JSONObject();
        requestBody.put("partnerReferenceNo", UUID.randomUUID().toString());
        requestBody.put("additionalInfo", additionalInfo);
        requestBody.put("amount", amountObj);
        requestBody.put("customerNumber", customerNumber);
        requestBody.put("feeAmount", feeAmount);

        JSONObject body = new JSONObject();
        body.put("urlEndpoint", "/v1.0/emoney/topup.htm");
        body.put("requestBody", requestBody);

        UriHelper helper = new UriHelper(BASE_URL);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(ENDPOINT))
                .setContentType(RestContentType.JSON)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
