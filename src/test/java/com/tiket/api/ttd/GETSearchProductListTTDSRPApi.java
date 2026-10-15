package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GETSearchProductListTTDSRPApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            String userAgent,
            String productCategoryCodes,
            String sortAttributes,
            String sortDirection,
            String language,
            String currency,
            int minPriceInCents,
            int maxPriceInCents
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/ttd/GETProductListTTDSRPApi/GETProductListTTDSRPApi_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("userAgent", inputs.userAgent);
        headers.put("currency", inputs.currency);
        headers.put("language", inputs.language);

        Map<String, Object> queryParams = getStringObjectMap();

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/ms-gateway/tix-events-v2-search/v3/product-cards"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }

    private Map<String, Object> getStringObjectMap() {
        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/ttd/GETProductListTTDSRPApi/GETSearchProductListTTDSRPApi_queryParams.properties");
        queryParams.put("productAllCategoryCodes", inputs.productCategoryCodes);
        queryParams.put("sortField", inputs.sortAttributes);
        queryParams.put("sortDirection", inputs.sortDirection);
        queryParams.put("startingPriceInCentsFrom", inputs.minPriceInCents);
        queryParams.put("startingPriceInCentsTo", inputs.maxPriceInCents);
        return queryParams;
    }
}
