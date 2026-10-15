package com.tiket.api.extranet.login;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record POSTExtranetAdminLoginMfaGenerateApi(Inputs inputs) implements BaseApi {

    public record Inputs(String mfaTrxId, String aatToken, String artToken, long accountId, String requestId, String deviceId) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(
                "https://lb1-testing.tiket.com",
                "https://member-core-v2-be-svc.preprod-platform-cluster.tiket.com"
        );

        Map<String, Object> headers = getStaticMap("src/test/resources/api/extranet/login/POSTExtranetAdminLoginMfaGenerateApi_headers.properties");
        
        // Add dynamic headers
        headers.put("deviceId", inputs.deviceId);
        headers.put("X-Request-Id", inputs.requestId != null ? inputs.requestId : java.util.UUID.randomUUID().toString());
        headers.put("X-Account-Id", String.valueOf(inputs.accountId));
        
        // Add cookies if tokens are provided
        if (inputs.aatToken != null && inputs.artToken != null) {
            headers.put("Cookie", "aat=" + inputs.aatToken + "; art=" + inputs.artToken);
        }

        JSONObject body = getStaticJson("src/test/resources/api/extranet/login/POSTExtranetAdminLoginMfaGenerateApi_body.json");
        body.put("mfaTrxId", inputs.mfaTrxId);

        String endpoint = "/tix-member-core/v3/admin/login/mfa/generate";

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}

