package com.tiket.api.utilities;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.util.Map;

public record HomeModuleApi(Inputs inputs) implements BaseApi{
    public record Inputs(
        String platform,
        String accessToken,
        String language,
        String currency
    ){}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        
        Map<String, Object> headers = getStaticMap("src/test/resources/api/utilities/HomesModules_headers.properties");
        headers.put("authorization", inputs.accessToken);
        headers.put("Accept-Language", inputs.language);
        headers.put("currency", inputs.currency);

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/utilities/HomeModules_queryParams.properties");
        queryParams.put("availablePlatforms",inputs.platform.toUpperCase());

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/ms-gateway/tix-home/v2/home-pages"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();
        return apiTools.get(request);
    }
}

