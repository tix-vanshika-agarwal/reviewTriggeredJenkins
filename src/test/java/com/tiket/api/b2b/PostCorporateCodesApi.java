package com.tiket.api.b2b;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record PostCorporateCodesApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String airlineCode,
            String code
            ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/CorporateCodes/CorporateCodes_headers.properties");
        headers.put("cookie", "session_access_token=" + inputs.accessToken + ";");

        JSONObject body = getStaticJson("src/test/resources/api/b2b/CorporateCodes/PostCorporateCode_body.json");
        body.put("airlineCode", inputs.airlineCode);
        body.put("code", inputs.code);

        UriHelper helper = new UriHelper("https://corporate-gatotkaca.tiket.com","https://corporate.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/corporate-gateway/tix-corporate/corporate-dashboard/corporate-codes"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
