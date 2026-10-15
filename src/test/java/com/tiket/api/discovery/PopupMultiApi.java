package com.tiket.api.discovery;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

public record PopupMultiApi(Inputs inputs) implements BaseApi {
    public record Inputs(String utmSource, String screenName, String url, String sessionAccessToken) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/discovery/PopupMultiApi_headers.properties");
        String correlationId = UUID.randomUUID().toString();
        headers.put("x-correlation-id", correlationId);
        headers.put("x-request-id", correlationId);
        headers.put("deviceid", UUID.randomUUID().toString());
        headers.put("cookie", "session_access_token=" + inputs.sessionAccessToken());

        String endpoint = "/ms-gateway/tix-promotion-page/popups/multi"
                + "?utm_source=" + URLEncoder.encode(inputs.utmSource(), StandardCharsets.UTF_8)
                + "&screenName=" + URLEncoder.encode(inputs.screenName(), StandardCharsets.UTF_8)
                + "&url=" + URLEncoder.encode(inputs.url(), StandardCharsets.UTF_8);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
