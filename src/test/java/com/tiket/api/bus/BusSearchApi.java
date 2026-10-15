package com.tiket.api.bus;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record BusSearchApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            String originId,
            String destinationId,
            String departureDate,
            int adult
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/PayExpress_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/bus/BusSearch_queryParams.properties");
        queryParams.put("origin", inputs.originId);
        queryParams.put("destination", inputs.destinationId);
        queryParams.put("departureDate", inputs.departureDate);
        queryParams.put("adult", inputs.adult);

        ApiRequest apiRequest = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-bus-demand/v2/search"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();
        return apiTools.get(apiRequest);
    }
}
