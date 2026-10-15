package com.tiket.api.auth;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public record POSTAdminLoginMfaGenerateApi(Inputs inputs) implements BaseApi {

    public record Inputs(String mfaTrxId, String mfaMethod, String aatToken, String artToken, long accountId) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(
                "https://member-core-v2-be-svc.tiket.com",
                "https://member-core-v2-be-svc.preprod-platform-cluster.tiket.com"
        );

        Map<String, Object> headers = new HashMap<>();
        headers.put("accept", "*/*");
        headers.put("deviceId", "new-device-id");
        headers.put("X-Store-Id", "TIKETCOM");
        headers.put("X-Channel-Id", "WEB");
        headers.put("X-Service-Id", "gateway");
        headers.put("X-Request-Id", java.util.UUID.randomUUID().toString());
        headers.put("X-Username", "username");
        headers.put("X-Account-Id", String.valueOf(inputs.accountId));
        headers.put("Accept-Language", "id");
        headers.put("Content-Type", "application/json");
        
        // Add cookies if tokens are provided
        if (inputs.aatToken != null && inputs.artToken != null) {
            headers.put("Cookie", "aat=" + inputs.aatToken + "; art=" + inputs.artToken);
        }

        JSONObject body = new JSONObject();
        body.put("mfaTrxId", inputs.mfaTrxId);
        body.put("mfaMethod", inputs.mfaMethod);

        String endpoint = "/tix-member-core/v3/admin/login/mfa/generate";

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}

