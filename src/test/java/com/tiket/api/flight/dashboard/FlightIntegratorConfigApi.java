package com.tiket.api.flight.dashboard;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public record FlightIntegratorConfigApi(Inputs inputs) implements BaseApi {
    /** @param size how many ISSUED configs to ask for; a supplier can hold one per distribution type */
    public record Inputs(
            String supplierCode,
            String size
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        String urlGk = "http://flight-rule-discovery.flight-ns.svc.tiket";
        String urlPreprod = "https://flight-rule-discovery-be-svc.preprod-flight-cluster.tiket.com";
        UriHelper uriHelper = new UriHelper(urlGk, urlPreprod);

        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/dashboard/FlightIntegratorConfigApi_headers.properties");

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("action", "ISSUED");
        queryParams.put("columnSort", "SUPPLIER_CODE");
        queryParams.put("page", "0");
        queryParams.put("size", (inputs.size == null || inputs.size.isBlank()) ? "1" : inputs.size);
        queryParams.put("sortDirection", "ASC");
        queryParams.put("supplierCode", inputs.supplierCode);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("tix-flight-rule-discovery/integrator-config"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
