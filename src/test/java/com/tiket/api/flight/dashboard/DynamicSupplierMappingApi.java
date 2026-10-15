package com.tiket.api.flight.dashboard;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record DynamicSupplierMappingApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String destination,
            String origin,
            String supplier
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        String urlGk = "https://flight-issuance-be-svc.test-flight-cluster.tiket.com";
        String urlPreprod = "https://flight-rule-discovery-be-svc.preprod-flight-cluster.tiket.com";
        UriHelper uriHelper = new UriHelper(urlGk, urlPreprod);

        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/dashboard/DynamicSupplierMappingApi_headers.properties");

        Map<String,Object> queryParams = getStaticMap("src/test/resources/api/flight/dashboard/DynamicSupplierMappingApi_queryParams.properties");
        queryParams.put("origin", inputs.origin);
        queryParams.put("destination", inputs.destination);
        if(inputs.supplier.isEmpty()){
            queryParams.remove("supplier");
        }else
            queryParams.put("supplier", inputs.supplier);

        ApiRequest request = new ApiRequest.Builder()
                .setUri((uriHelper.withEndpoint("tix-flight-rule-discovery/dynamic-supplier-mapping/ods")))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
