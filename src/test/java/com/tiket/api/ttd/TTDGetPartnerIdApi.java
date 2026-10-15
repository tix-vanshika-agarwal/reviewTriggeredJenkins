package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record TTDGetPartnerIdApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            String keyWord
    ) {}
    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/ttd/TTDGetPartnerId_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/ttd/TTDGetPartnerId_queryParams.properties");
        queryParams.put("keyword",inputs.keyWord);

        UriHelper helper = new UriHelper("http://home-be-svc.test-platform-cluster.tiket.com","http://home-be-svc.preprod-platform-cluster.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-home/v2/admin/page-modules/inventory/partners"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
