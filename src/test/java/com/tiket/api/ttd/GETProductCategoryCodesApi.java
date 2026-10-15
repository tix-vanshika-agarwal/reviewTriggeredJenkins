package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GETProductCategoryCodesApi(Inputs inputs) implements BaseApi  {
    public record Inputs(
            String accessToken,
            String userAgent,
            String language,
            String currency
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/ttd/GETProductCategoryCodesApi/GETProductCategoryCodesApi_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("userAgent", inputs.userAgent);
        headers.put("currency", inputs.currency);
        headers.put("language", inputs.language);

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/ttd/GETProductCategoryCodesApi/GETProductCategoryCodesApi_queryParams.properties");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/ms-gateway/tix-events-v2-inventory/v2/product-categories"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
