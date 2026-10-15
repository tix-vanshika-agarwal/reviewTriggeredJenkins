package com.tiket.api.flight.dashboard;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public record DynamicSupplierMappingByAirlineApi(Inputs inputs) implements BaseApi {
    public record Inputs(String airlineCode, String origin, String destination) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        String urlGk = "http://flight-rule-discovery.flight-ns.svc.tiket";
        String urlPreprod = "https://flight-rule-discovery-be-svc.preprod-flight-cluster.tiket.com";
        UriHelper uriHelper = new UriHelper(urlGk, urlPreprod);

        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/dashboard/DynamicSupplierMappingApi_headers.properties");

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("airline", inputs.airlineCode);
        queryParams.put("origin", inputs.origin);
        queryParams.put("destination", inputs.destination);
        queryParams.put("page", "0");
        queryParams.put("size", "1000");
        queryParams.put("status", "ACTIVE");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("tix-flight-rule-discovery/dynamic-supplier-mapping"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
