package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GETProductListCountTTDApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String userAgent,
            boolean hasLoyalty,
            String productCategory
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/ttd/GETProductListCountTTDApi_headers.properties");

        headers.put("Cookie", inputs.accessToken);
        headers.put("userAgent", inputs.userAgent);
        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/ttd/GETProductListCountTTDApi_queryParams.properties");
        queryParams.put("hasLoyalty", inputs.hasLoyalty);
        queryParams.put("productAllCategoryCodes", inputs.productCategory);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/ms-gateway/tix-events-v2-search/v3/product-cards/count"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
