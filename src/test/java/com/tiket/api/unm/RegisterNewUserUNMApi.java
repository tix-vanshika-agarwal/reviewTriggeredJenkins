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

public record RegisterNewUserUNMApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String email,
            String name,
            String passCode,
            String password,
            String phoneCountryCode,
            String phoneNationalNumber
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/member/RegisterNewUserUNMApi_headers.properties");

        JSONObject body = getStaticJson("src/test/resources/api/member/RegisterNewUserUNMApi_body.json");
        body.put("email", inputs.email);
        body.put("name", inputs.name);
        body.put("passCode", inputs.passCode);
        body.put("password", inputs.password);
        body.put("phoneCountryCode", inputs.phoneCountryCode);
        body.put("phoneNationalNumber", inputs.phoneNationalNumber);

        String refUrl = "https://gatotkaca.tiket.com/";
        if(Configs.ENVIRONMENT == Environment.PREPROD){
            refUrl= "https://preprod.tiket.com/";
        }
        body.put("refUrl", refUrl);

        UriHelper helper = new UriHelper("https://service.bliblitiket.tools","https://account.preprod.bliblitiket.dev/gateway");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/gks-unm-go-be/api/v1/registration/submit"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
