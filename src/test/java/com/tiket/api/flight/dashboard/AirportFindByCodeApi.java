package com.tiket.api.flight.dashboard;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record AirportFindByCodeApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String originCode
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        String urlGk = "https://flight-issuance-be-svc.test-flight-cluster.tiket.com";
        String urlPreprod = "https://flight-rule-discovery-be-svc.preprod-flight-cluster.tiket.com";
        UriHelper uriHelper = new UriHelper(urlGk, urlPreprod);

        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/dashboard/AirportFindByCodeApi_headers.properties");

        ApiRequest request = new ApiRequest.Builder()
                .setUri((uriHelper.withEndpoint("tix-flight-master-discovery/airport/findByCode/"+inputs.originCode.toUpperCase())))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
