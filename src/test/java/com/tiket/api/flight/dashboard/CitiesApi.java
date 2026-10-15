package com.tiket.api.flight.dashboard;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record CitiesApi(Inputs inputs) implements BaseApi {
    public record Inputs(String cityCode, String cookieHeader) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper uriHelper = new UriHelper("https://gatotkaca.tiket.com", "https://preprod.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/dashboard/CitiesApi_headers.properties");
        if (inputs.cookieHeader != null && !inputs.cookieHeader.isBlank()) {
            headers.put("Cookie", inputs.cookieHeader);
        }

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/flight/dashboard/CitiesApi_queryParams.properties");
        queryParams.put("cityCode", inputs.cityCode);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/tix-gateway-dashboard/cities"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
