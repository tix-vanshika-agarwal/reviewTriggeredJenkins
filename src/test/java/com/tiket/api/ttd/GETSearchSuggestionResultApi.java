package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GETSearchSuggestionResultApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String authorization,
            String userAgent,
            String language,
            String searchKeyword
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/ttd/GETSearchSuggestionResultApi/GETSearchSuggestionResultApi_headers.properties");
        headers.put("Authorization", inputs.authorization);
        headers.put("userAgent", inputs.userAgent);

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/ttd/GETSearchSuggestionResultApi/GETSearchSuggestionResultApi_queryParams.properties");
        queryParams.put("searchKeyword", inputs.searchKeyword);
        queryParams.put("language", inputs.language.toUpperCase());

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-events-v2-search/v2/suggestions"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
