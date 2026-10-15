package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public record GETLandingPageGroupIconApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            String userAgent,
            String language
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + inputs.accessToken);
        headers.put("userAgent", inputs.userAgent);
        headers.put("language", inputs.language);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/ms-gateway/tix-events-v2-inventory/v1/page-config?code=LANDING_PAGE"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}