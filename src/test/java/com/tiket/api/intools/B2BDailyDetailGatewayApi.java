package com.tiket.api.intools;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import java.util.Map;

public record B2BDailyDetailGatewayApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String searchDateBy,
            String startDate,
            String endDate,
            String resellerId,
            String searchParam,
            String accessToken,
            int page,
            int size,
            String sortBy,
            String searchBy
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap(
                "src/test/resources/api/intools/B2BDailyDetailGateway/B2BDailyDetailGatewayApi_headers.properties");
        headers.put("Authorization", "Bearer " + inputs.accessToken());
        headers.put("Cookie", "session_access_token=" + inputs.accessToken() + ";");

        Map<String, Object> queryParams = getStaticMap(
                "src/test/resources/api/intools/B2BDailyDetailGateway/B2BDailyDetailGatewayApi_queryParams.properties");
        queryParams.put("searchDateBy", inputs.searchDateBy());
        queryParams.put("startSearchDateBy", inputs.startDate());
        queryParams.put("endSearchDateBy", inputs.endDate());
        queryParams.put("page", inputs.page());
        queryParams.put("size", inputs.size());
        queryParams.put("resellerId", inputs.resellerId());
        queryParams.put("sortBy", inputs.sortBy());
        queryParams.put("searchParam", inputs.searchParam());
        queryParams.put("searchBy", inputs.searchBy());

        UriHelper uriHelper = new UriHelper("https://gatotkaca.tiket.com", "https://preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/ms-gateway/tix-deposit/b2b/affiliates/report/daily"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
