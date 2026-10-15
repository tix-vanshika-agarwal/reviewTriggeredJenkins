package com.tiket.api.unm;

import com.tiket.io.api.ApiTools;
import com.tiket.model.Environment;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;

/**
 * BCA cobrand cardholder registration: {@code POST /gks-unm-go-be/api/v1/entity/registration/submit}.
 * Hosts verified 2026-08-26: GK {@code https://service.bliblitiket.tools};
 * preprod {@code https://sandbox.eph.bliblitiket.tools} (type {@code BCA_COBRAND_PARTNER} not yet
 * accepted there). Production has no access — skip that env rather than falling through to preprod.
 */
public record POSTEntityRegistrationSubmitApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String email,
            String name,
            String phoneCountryCode,
            String phoneNationalNumber,
            String type
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        if (Configs.ENVIRONMENT == Environment.PROD) {
            Assertion.skip("BCA cobrand entity registration has no production access");
        }

        Map<String, Object> headers = getStaticMap(
                "src/test/resources/api/unm/POSTEntityRegistrationSubmitApi_headers.properties");
        headers.put("X-Request-Id", UUID.randomUUID().toString());

        JSONObject body = getStaticJson("src/test/resources/api/unm/POSTEntityRegistrationSubmitApi_body.json");
        body.put("email", inputs.email());
        body.put("name", inputs.name());
        body.put("phoneCountryCode", inputs.phoneCountryCode());
        body.put("phoneNationalNumber", inputs.phoneNationalNumber());
        body.put("type", inputs.type());

        UriHelper helper = new UriHelper(
                "https://service.bliblitiket.tools",
                "https://sandbox.eph.bliblitiket.tools");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/gks-unm-go-be/api/v1/entity/registration/submit"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
