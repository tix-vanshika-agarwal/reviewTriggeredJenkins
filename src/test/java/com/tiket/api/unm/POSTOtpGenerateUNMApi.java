package com.tiket.api.unm;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import com.tiket.model.Environment;

import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;
import java.util.Optional;

public record POSTOtpGenerateUNMApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String recipient,
            String action,
            String channel,
            Optional<String> authorization,
            long unmId
    ) {
        public Inputs(String recipient, String action, String channel) {
            this(recipient, action, channel, Optional.empty(), 0);
        }

        public Inputs(String recipient, String action, String channel, Optional<String> authorization) {
            this(recipient, action, channel, authorization, 0);
        }
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/POSTOtpGenerateUNMApi_headers.properties");
        //Added as a temporary fix due to turnstile, can remove later when turned off
        headers.put("True-Client-Ip", "34.124.173.71");
        headers.put("X-Forwarded-For", "34.124.173.71");
        this.inputs.authorization.ifPresent(s -> headers.put("Authorization", "Bearer " + s));
        headers.put("True-Client-Ip", Configs.ENVIRONMENT == Environment.PREPROD ? "34.124.173.71" : "34.87.5.234");
        if (this.inputs.unmId != 0) headers.put("X-User-Id", String.valueOf(this.inputs.unmId));

        JSONObject body = getStaticJson("src/test/resources/api/auth/POSTOtpGenerateUNMApi_body.json");
        body.put("action",inputs.action);
        body.put("recipient",inputs.recipient);
        body.put("channel",inputs.channel);

        UriHelper helper = this.inputs.authorization.isPresent()
                ? new UriHelper("https://account.test.bliblitiket.dev/gateway", "https://account.preprod.bliblitiket.dev/gateway")
                : new UriHelper("https://service.bliblitiket.tools", "https://sandbox.eph.bliblitiket.tools");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/gks-unm-go-be/api/v1/otp/generate"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
