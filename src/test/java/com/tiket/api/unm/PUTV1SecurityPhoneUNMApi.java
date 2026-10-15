package com.tiket.api.unm;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;

public record PUTV1SecurityPhoneUNMApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String phoneCountryCode,
            String phoneNationalNumber,
            String newPhoneOtpPassCode,
            long unmUserId
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/POSTOtpGenerateUNMApi_headers.properties");
        headers.put("X-User-Id", String.valueOf(inputs.unmUserId));
        headers.put("X-Request-Id", "add-phone-automation-" + UUID.randomUUID());
        headers.put("Accept-Language", "en-US");

        JSONObject body = new JSONObject();
        body.put("phoneCountryCode", inputs.phoneCountryCode);
        body.put("phoneNationalNumber", inputs.phoneNationalNumber);
        body.put("newPhoneOtpPassCode", inputs.newPhoneOtpPassCode);
        body.put("dvId", "");

        UriHelper helper = new UriHelper(
                "https://service.bliblitiket.tools",
                "https://sandbox.eph.bliblitiket.tools"
        );
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/gks-unm-go-be/api/v1/security/phone"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.put(request);
    }
}
