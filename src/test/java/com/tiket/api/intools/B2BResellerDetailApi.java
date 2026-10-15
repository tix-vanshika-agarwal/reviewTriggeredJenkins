package com.tiket.api.intools;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import java.util.Map;

public record B2BResellerDetailApi(Inputs inputs)  implements BaseApi{
    public record Inputs(
            String resellerName
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/intools/B2BResellerDetail/B2BResellerDetail_headers.properties");

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/intools/B2BResellerDetail/B2BResellerDetail_queryParams.properties");
        queryParams.put("resellerName", inputs.resellerName);

        UriHelper uriHelper = new UriHelper("https://deposit-be-http-svc.test-platform-cluster.tiket.com","https://deposit-be-http-svc.preprod-platform-cluster.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/tix-deposit/b2b/affiliates/list"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}