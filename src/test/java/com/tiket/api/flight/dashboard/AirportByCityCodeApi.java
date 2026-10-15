package com.tiket.api.flight.dashboard;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public record AirportByCityCodeApi(Inputs inputs) implements BaseApi {
    public record Inputs(String cityCode) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        String urlGk = "https://flight-master-discovery-be-svc.test-flight-cluster.tiket.com";
        String urlPreprod = "https://flight-master-discovery-be-svc.preprod-flight-cluster.tiket.com";
        UriHelper uriHelper = new UriHelper(urlGk, urlPreprod);

        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/dashboard/AirportByCityCodeApi_headers.properties");

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("cityCode", inputs.cityCode);
        queryParams.put("status", "ACTIVE");
        queryParams.put("page", "0");
        queryParams.put("size", "1000");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("tix-flight-master-discovery/airport"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
