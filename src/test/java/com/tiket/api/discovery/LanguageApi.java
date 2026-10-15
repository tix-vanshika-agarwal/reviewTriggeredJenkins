package com.tiket.api.discovery;

import com.tiket.testbase.BaseApi;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import java.util.Map;

public class LanguageApi implements BaseApi {

    private final String languageId;

    public LanguageApi(String languageId) {
        this.languageId = languageId;
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/discovery/LanguageApi_headers.properties");

        UriHelper uriHelper = new UriHelper("https://general-config-be-svc.test-platform-cluster.tiket.com", "https://general-config-be-svc.preprod-platform-cluster.tiket.com");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("tix-general-config/admin/language/" + languageId))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
