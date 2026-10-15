package com.tiket.api.unm;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record POSTMfaSubmitUNMApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String secret,
            String token,
            String type
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/POSTMfaSubmitUNMApi_headers.properties");

        JSONObject body = getStaticJson("src/test/resources/api/auth/POSTMfaSubmitUNMApi_body.json");
        body.put("secret",inputs.secret);
        body.put("token",inputs.token);
        body.put("type",inputs.type);

        UriHelper helper = new UriHelper("https://service.bliblitiket.tools","https://account.preprod.bliblitiket.dev/gateway");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/gks-unm-go-be/api/v1/mfa/submit"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
