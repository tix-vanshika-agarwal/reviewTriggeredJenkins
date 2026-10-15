package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record TTDSRPByLocationApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            String userAgent,
            String language,
            String locationPublicID,
            String productCategoryCodes
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/ttd/TTDSRPByLocation_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("userAgent", inputs.userAgent);
        headers.put("lang", inputs.language);

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/ttd/TTDSRPByLocation_queryParams.properties");
        queryParams.put("publicId", inputs.locationPublicID);
        if(!inputs.productCategoryCodes.isBlank()){
            queryParams.put("productCategoryCodes", inputs.productCategoryCodes);
        }

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-events-v2-inventory/v1/product-cards"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
