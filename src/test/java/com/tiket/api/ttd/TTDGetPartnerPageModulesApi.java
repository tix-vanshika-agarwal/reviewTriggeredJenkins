package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record TTDGetPartnerPageModulesApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            String referenceId,
            String langCode,
            String userAgent
    ) {}
    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/ttd/TTDGetPartnerPageModules_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("Accept-Language", inputs.langCode);
        headers.put("User-Agent", inputs.userAgent);

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/ttd/TTDGetPartnerPageModules_queryParams.properties");
        queryParams.put("referenceId",inputs.referenceId);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-home/v2/page-modules-full"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
