package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.Platform;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public record TTDPopularDestinationApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String authorization,
            String userAgent,
            String language
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = new HashMap<>();
        headers.put("authorization", inputs.authorization);
        headers.put("userAgent", inputs.userAgent);
        headers.put("language", inputs.language);

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/ttd/TTDPopularDestinationApi/TTDPopularDestinationApi_queryParams.properties");
        if (Configs.PLATFORM == Platform.IOS) {
            queryParams.put("countryCode", "");
            queryParams.put("notCountryCode", "");
        }

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-events-v2-inventory/v1/popular-destinations"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
