package com.tiket.api.extranet.login;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record POSTExtranetAdminLoginApi(Inputs inputs) implements BaseApi {

    public record Inputs(String username, String password, String deviceId) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(
                "https://lb1-testing.tiket.com",
                "https://member-core-v2-be-svc.preprod-platform-cluster.tiket.com"
        );

        Map<String, Object> headers = getStaticMap("src/test/resources/api/extranet/login/POSTExtranetAdminLoginApi_headers.properties");
        
        // Add dynamic deviceId
        headers.put("deviceId", inputs.deviceId);

        JSONObject body = getStaticJson("src/test/resources/api/extranet/login/POSTExtranetAdminLoginApi_body.json");
        body.put("username", inputs.username);
        body.put("password", inputs.password);

        String endpoint = "/tix-member-core/v3/admin/login";

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}

