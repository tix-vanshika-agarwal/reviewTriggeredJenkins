package com.tiket.api.unm;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;
import java.util.Optional;

public record POSTOtpVerifyUNMApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String otpId,
            String action,
            Optional<String> authorization,
            long unmId
    ) {
        public Inputs(String otpId, String action) {
            this(otpId, action, Optional.empty(), 0);
        }

        public Inputs(String otpId, String action, Optional<String> authorization) {
            this(otpId, action, authorization, 0);
        }
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/POSTOtpVerifyUNMApi_headers.properties");
        this.inputs.authorization.ifPresent(s -> headers.put("Authorization", "Bearer " + s));
        if (this.inputs.unmId != 0) headers.put("X-User-Id", String.valueOf(this.inputs.unmId));

        JSONObject body = getStaticJson("src/test/resources/api/auth/POSTOtpVerifyUNMApi_body.json");
        body.put("action",inputs.action);
        body.put("otpId",inputs.otpId);

        UriHelper helper = this.inputs.authorization.isPresent()
                ? new UriHelper("https://account.test.bliblitiket.dev/gateway", "https://account.preprod.bliblitiket.dev/gateway")
                : new UriHelper("https://service.bliblitiket.tools", "https://sandbox.eph.bliblitiket.tools");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/gks-unm-go-be/api/v1/otp/verify"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
