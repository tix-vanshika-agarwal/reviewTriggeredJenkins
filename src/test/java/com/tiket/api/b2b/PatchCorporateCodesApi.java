package com.tiket.api.b2b;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record PatchCorporateCodesApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            int corporateCodeId,
            int corporateId
            ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://corporate-gatotkaca.tiket.com","https://corporate.preprod.tiket.com");
        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/CorporateCodes/CorporateCodes_headers.properties");
        headers.put("Cookie", "session_access_token=" + inputs.accessToken + ";");
        headers.put("referer",helper.getUri()+"/corporate-information");
        headers.put("origin", helper.getUri());
        headers.put("x-corporate-id",inputs.corporateId);

        JSONObject body = getStaticJson("src/test/resources/api/b2b/CorporateCodes/PatchCorporateCode_body.json");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/corporate-gateway/tix-corporate/corporate-dashboard/corporate-codes/" + inputs.corporateCodeId))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.patch(request);
    }
}
