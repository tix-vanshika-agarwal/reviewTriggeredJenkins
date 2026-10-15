package com.tiket.api.auth;

import com.tiket.io.api.ApiTools;
import com.tiket.model.Environment;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record PayExpressWithEnvApi(Inputs inputs)implements BaseApi {
    public record Inputs(
            Environment environment
    ){}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/PayExpress_headers.properties");

        String UrlGK = "https://lb1-testing.tiket.com";
        String urlPreProd = "https://member-session-svc.preprod-platform-cluster.tiket.com";
        String urlProd = "https://api.tiket.com";
        String url = switch (inputs.environment) {
            case GK -> UrlGK;
            case PREPROD -> urlPreProd;
            case PROD -> urlProd;
        };
        url += "/tix-member-session/v1/session";

        ApiRequest request = new ApiRequest.Builder()
                .setUri(url)
                .setHeaderParams(headers)
                .build();
        return apiTools.post(request);
    }
}
