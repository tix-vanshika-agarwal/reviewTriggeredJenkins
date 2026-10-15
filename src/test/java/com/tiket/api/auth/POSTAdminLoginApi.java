package com.tiket.api.auth;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record POSTAdminLoginApi(Inputs inputs) implements BaseApi {

    public record Inputs(String username, String password, String adminType, String businessType,
                         String dashboardId, String deviceId) {

        /**
         * Backward-compatible inputs without a dashboard scope or fixed device id: {@code dashboardId} is
         * omitted from the payload and {@code uniqueId} falls back to a random value.
         */
        public Inputs(String username, String password, String adminType, String businessType) {
            this(username, password, adminType, businessType, null, null);
        }
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(
                "https://lb1-testing.tiket.com",
                "https://member-core-v2-be-svc.preprod-platform-cluster.tiket.com"
        );

        Map<String, Object> headers = new HashMap<>();
        headers.put("accept", "*/*");
        headers.put("X-Store-Id", "TIKETCOM");
        headers.put("X-Channel-Id", "WEB");
        headers.put("X-Service-Id", "gateway");
        headers.put("X-Request-Id", UUID.randomUUID().toString());
        headers.put("X-Username", "username");
        headers.put("Accept-Language", "id");
        headers.put("Content-Type", "application/json");

        JSONObject body = new JSONObject();
        body.put("username", inputs.username());
        body.put("password", inputs.password());
        body.put("adminType", inputs.adminType());
        if (inputs.businessType() != null) {
            body.put("businessType", inputs.businessType());
        } else {
            body.put("businessType", JSONObject.NULL);
        }
        if (inputs.dashboardId() != null) {
            body.put("dashboardId", inputs.dashboardId());
        }

        JSONObject deviceIdentity = new JSONObject();
        deviceIdentity.put("uniqueId", inputs.deviceId() != null ? inputs.deviceId() : UUID.randomUUID().toString());
        deviceIdentity.put("type", "IOS");
        deviceIdentity.put("osVersion", "10.0.1");
        deviceIdentity.put("appVersion", "3.1.0");
        body.put("deviceIdentity", deviceIdentity);

        String endpoint = "/tix-member-core/v3/admin/login";

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}

