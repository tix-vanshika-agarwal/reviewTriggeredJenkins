package com.tiket.api.unm;

import com.tiket.io.api.ApiTools;
import com.tiket.model.Environment;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record POSTAuthLoginUNMApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String username,
            String password
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/POSTAuthLoginUNMApi_headers.properties");

        JSONObject body = getStaticJson("src/test/resources/api/auth/POSTAuthLoginUNMApi_body.json");
        body.put("identity",inputs.username);
        body.put("secret",inputs.password);

        if(Configs.ENVIRONMENT == Environment.GK) {
            body.put("ref","https://gatotkaca.tiket.com");
        }else {
            body.put("ref","https://preprod.tiket.com");
        }

        UriHelper helper = new UriHelper("https://service.bliblitiket.tools","https://account.preprod.bliblitiket.dev/gateway");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/gks-unm-go-be/api/v1/auth/login"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
