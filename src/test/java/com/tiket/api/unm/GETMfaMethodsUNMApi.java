package com.tiket.api.unm;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GETMfaMethodsUNMApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String token
    ) {
    }
    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/auth/GETMfaMethodsUNMApi_queryParams.properties");
        queryParams.put("token",inputs.token);

        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/GETMfaMethodsUNMApi_headers.properties");

        UriHelper helper = new UriHelper("https://service.bliblitiket.tools","https://account.preprod.bliblitiket.dev/gateway");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/gks-unm-go-be/api/v1/mfa/methods"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
