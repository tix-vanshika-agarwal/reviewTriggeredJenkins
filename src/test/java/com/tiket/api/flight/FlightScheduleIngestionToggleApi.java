package com.tiket.api.flight;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.Environment;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public record FlightScheduleIngestionToggleApi(Inputs inputs) implements BaseApi {

    public record Inputs(boolean enabled) {}

    @Override
    @SneakyThrows
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers =
                getStaticMap("src/test/resources/api/flight/FlightScheduleIngestionToggle.properties");
        Map<String, Object> body = new HashMap<>();
        body.put("enable", inputs.enabled());

        Gson gson = new Gson();

        String gkUrl =
                "https://flight-schedule-builder-svc.test-flight-cluster.tiket.com" +
                        "/tix-flight-schedule-builder/schedule/enable-or-disable-ingestion";
        String preprodUrl =
                "https://flight-schedule-builder-svc.preprod-flight-cluster.tiket.com" +
                        "/tix-flight-schedule-builder/schedule/enable-or-disable-ingestion";

        String baseUrl = (Configs.ENVIRONMENT == Environment.GK) ? gkUrl : preprodUrl;

        ApiRequest request = new ApiRequest.Builder()
                .setUri(baseUrl)
                .setHeaderParams(headers)
                .setBody(gson.toJson(body))
                .build();

        return apiTools.post(request);
    }
}
