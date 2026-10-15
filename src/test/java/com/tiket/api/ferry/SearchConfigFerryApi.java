package com.tiket.api.ferry;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.util.Map;

public record SearchConfigFerryApi(Input input) implements BaseApi {
    public record Input(String accessToken) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/ferry/searchConfigFerryHeaders.properties");
        headers.put("Authorization", "Bearer " + input.accessToken);

        Map<String, Object> queryParams = getStringObjectMap();

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-ferry-demand/v1/search-config"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }

    private Map<String, Object> getStringObjectMap() {
        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/ferry/searchConfigFerryQueryParams.properties");
        queryParams.put("token", input.accessToken);
        return queryParams;
    }
}

