package com.tiket.api.payment;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record PaymentDashboardTransactionApi(Inputs inputs) implements BaseApi {

    /** startDate and endDate are in yyyy-MM-dd format */
    public record Inputs(String accessToken, String startDate, String endDate) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = Map.of(
                "Content-Type", "application/json",
                "X-Cookie-Session-V2", "true",
                "Cookie", "session_access_token=" + inputs.accessToken().replace("Bearer ", "") + ";");

        JSONObject body = new JSONObject().put("startDate", inputs.startDate()).put("endDate", inputs.endDate());

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-payment-dashboard/transaction/getAll?page=0&size=20"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
