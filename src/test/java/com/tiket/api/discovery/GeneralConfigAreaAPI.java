package com.tiket.api.discovery;

import com.tiket.testbase.BaseApi;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;

import java.util.Map;

public record GeneralConfigAreaAPI() implements BaseApi {
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/discovery/GeneralConfigArea_headers.properties");

        UriHelper uriHelper = new UriHelper("https://general-config-be-svc.test-platform-cluster.tiket.com", "https://general-config-be-svc.preprod-platform-cluster.tiket.com");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("tix-general-config/admin/area/paginated?page=0&size=1&areaColumnSort=ORDER&sortDirection=DESC"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
