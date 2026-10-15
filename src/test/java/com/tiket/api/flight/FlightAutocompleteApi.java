package com.tiket.api.flight;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record FlightAutocompleteApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String query,
            String authorization
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/FlightAutocompleteApi_headers.properties");
        headers.put("Authorization",inputs.authorization);

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/flight/FlightAutocomplete_queryParams.properties");
        queryParams.put("query",inputs.query);
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-flight-master-discovery/v3/airport/autocomplete"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
