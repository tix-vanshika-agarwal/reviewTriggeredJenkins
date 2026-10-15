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

public record SelectPaymentApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            long orderId,
            String orderHash
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        JSONArray body = getStaticJsonArray("src/test/resources/api/payment/SelectPayment_body.json");
        for (int i = 0; i < body.length(); i++) {
            JSONObject variables = body.getJSONObject(i).getJSONObject("variables");
            variables.put("referenceId", inputs.orderId);
            variables.put("orderHash", inputs.orderHash);
            variables.put("rescheduleOrderId", inputs.orderId);
            variables.put("rescheduleOrderHash", inputs.orderHash);
            body.getJSONObject(i).put("variables", variables);
        }

        UriHelper uriHelper = new UriHelper("https://gql-gatotkaca.tiket.com", "https://gql.tiket.com", "https://gql.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.getUri())
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
