package com.tiket.api.auth;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GETProfileApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/PayExpress_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-member-core/v2/account/v2/profile"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }

}
