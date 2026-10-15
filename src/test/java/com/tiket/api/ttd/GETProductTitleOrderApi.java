package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public record GETProductTitleOrderApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            String userAgent,
            String language,
            String refreshToken
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("cookie", "session_access_token="+inputs.accessToken+";"+"refresh_access_token="+inputs.refreshToken+";");
        headers.put("useragent", inputs.userAgent);
        headers.put("language", inputs.language.toUpperCase());
        headers.put("x-cookie-session-v2", true);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("qtype", "TUCANA_V2_0_ALIAS");
        queryParams.put("longLat", "");
        queryParams.put("searchKeyword", "");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/ms-gateway/tix-events-v2-search/v2/suggestions"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
