package com.tiket.api.discovery;

import com.tiket.testbase.BaseApi;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import java.util.Map;

public class EntryPointInfoPageApi implements BaseApi {
    private final String tabIndex;
    public EntryPointInfoPageApi(String tabIndex) {
        this.tabIndex = tabIndex;
    }
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/discovery/EntryPointInfoPageApi_headers.properties");
        UriHelper uriHelper = new UriHelper(
                "https://info-pages-be-svc.test-platform-cluster.tiket.com",
                "https://info-pages-be-svc.preprod-platform-cluster.tiket.com"
        );
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("tix-info-pages/admin/entry-points/tabs/" + tabIndex + "/info-pages"))
                .setHeaderParams(headers).build();
        return apiTools.get(request);
    }
}
