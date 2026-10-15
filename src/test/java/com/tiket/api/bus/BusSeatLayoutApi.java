package com.tiket.api.bus;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record BusSeatLayoutApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            String scheduleId,
            int adult
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/PayExpress_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/bus/BusSeatLayout_queryParams.properties");
        queryParams.put("scheduleId", inputs.scheduleId);
        queryParams.put("adult", inputs.adult);

        ApiRequest apiRequest = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-bus-demand/v2/seat-layout"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();
        return apiTools.get(apiRequest);
    }
}
