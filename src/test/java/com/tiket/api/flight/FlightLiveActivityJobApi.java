package com.tiket.api.flight;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record FlightLiveActivityJobApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String origin,
            String destination,
            String flightNumber,
            String departureDate,
            String actionType
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(
                "https://flight-alert-be-svc.test-flight-cluster.tiket.com",
                "https://flight-alert-be-svc.preprod-flight-cluster.tiket.com"
        );

        Map<String, Object> headers = new HashMap<>();
        headers.put("accept", "*/*");
        headers.put("channelId", "WEB");
        headers.put("requestId", UUID.randomUUID().toString());
        headers.put("serviceId", "gateway");
        headers.put("storeId", "TIKETCOM");
        headers.put("username", "username");

        String pathParams = String.format("/%s/%s/%s/%s/%s",
                encodeString(inputs.flightNumber),
                encodeString(inputs.departureDate),
                inputs.origin,
                inputs.destination,
                inputs.actionType
        );

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/live-activity-job/by-flight" + pathParams))
                .setHeaderParams(headers)
                .build();
        return apiTools.get(request);
    }

    private String encodeString(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8)
                .replace("+", "%20");
    }
}
