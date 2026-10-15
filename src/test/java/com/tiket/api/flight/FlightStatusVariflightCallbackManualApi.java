package com.tiket.api.flight;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record FlightStatusVariflightCallbackManualApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String origin,
            String destination,
            String flightNumber,
            String departurePlanDate,
            String arrivalPlanDate,
            String state
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        ObjectMapper mapper = new ObjectMapper();

        UriHelper helper = new UriHelper(
                "https://flight-status-v3-be-svc.test-flight-cluster.tiket.com",
                "https://flight-status-v3-be-svc.preprod-flight-cluster.tiket.com"
        );

        Map<String, Object> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("Content-Type", "application/json");

        Map<String, Object> data = new HashMap<>();
        data.put("FlightNo", inputs.flightNumber);
        data.put("FlightDepcode", inputs.origin);
        data.put("FlightArrcode", inputs.destination);
        data.put("FlightDeptimePlanDate", inputs.departurePlanDate);
        data.put("FlightArrtimePlanDate", inputs.arrivalPlanDate);
        data.put("FlightState", inputs.state);

        List<Map<String, Object>> body = new ArrayList<>();
        body.add(data);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-flight-status-v3/variflight/callback-manual"))
                .setHeaderParams(headers)
                .setBody(mapper.writeValueAsString(body))
                .build();

        return apiTools.post(request);
    }
}
