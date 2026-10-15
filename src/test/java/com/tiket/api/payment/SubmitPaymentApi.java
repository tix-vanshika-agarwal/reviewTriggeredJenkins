package com.tiket.api.payment;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public record SubmitPaymentApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String orderHash,
            long referenceId
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        JSONArray body = getStaticJsonArray("src/test/resources/api/payment/SubmitPayment_body.json");
        JSONObject variables = body.getJSONObject(0).getJSONObject("variables");
        variables.put("orderHash", inputs.orderHash);
        variables.put("referenceId", inputs.referenceId);
        body.getJSONObject(0).put("variables", variables);

        UriHelper uriHelper = new UriHelper("https://gql-gatotkaca.tiket.com", "https://gql.tiket.com", "https://gql.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.getUri())
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
