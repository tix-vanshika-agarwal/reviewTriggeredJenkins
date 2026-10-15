package com.tiket.api.auth;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record PayExpressApi() implements BaseApi {

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        UriHelper uriHelper = new UriHelper("https://lb1-testing.tiket.com", "https://api.tiket.com", "https://member-session-svc.preprod-platform-cluster.tiket.com");
        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/PayExpress_headers.properties");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/tix-member-session/v1/session"))
                .setHeaderParams(headers)
                .build();
        return apiTools.post(request);
    }
}
