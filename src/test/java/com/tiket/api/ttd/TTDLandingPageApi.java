package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record TTDLandingPageApi(Inputs inputs) implements BaseApi{
    public record Inputs(
            String accessToken,
            String userAgent,
            String language,
            String currency
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/ttd/TTDLandingPage_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("User-Agent", inputs.userAgent);
        headers.put("lang", inputs.language);
        if(inputs.currency != null) {
            headers.put("currency", inputs.currency);
        }

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/ttd/TTDLandingPage_queryParams.properties");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-home/v2/page-modules-full"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
