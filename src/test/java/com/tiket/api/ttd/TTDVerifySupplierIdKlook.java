package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public record TTDVerifySupplierIdKlook(Inputs inputs) implements BaseApi {

    public record Inputs(
            String authorization,
            String userAgent,
            String language,
            String productName,
            String code
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", inputs.authorization);
        headers.put("userAgent", inputs.userAgent);
        headers.put("Accept-Language", inputs.language);
        headers.put("productAllCategoryCodes", inputs.code);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("sortDirection", "DESC");
        queryParams.put("sortField", "popularityScore");
        queryParams.put("ff-ttde-popularity-score-automation", true);
        queryParams.put("pageSize", 150);

        ApiRequest request = new ApiRequest.Builder()
                .setUri("ms-gateway/tix-events-v2-search/v3/product-cards")
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
