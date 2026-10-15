package com.tiket.api.unm;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import org.json.JSONObject;

import java.util.Map;
import java.util.Optional;

public record PUTV1SecurityEmail(Inputs inputs) implements com.tiket.testbase.BaseApi {
    public record Inputs(
            String newEmail,
            String passCodeNewEmail,
            String passCodeOldEmail,
            String type,
            Optional<String> authorization

    ) {
        public Inputs(String newEmail, String passCodeNewEmail, String passCodeOldEmail, String type) {
            this(newEmail, passCodeNewEmail, passCodeOldEmail, type, Optional.empty());
        }
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/POSTOtpGenerateUNMApi_headers.properties");
        this.inputs.authorization.ifPresent(s -> headers.put("Authorization", "Bearer " + s));

        JSONObject accountVerification = new JSONObject();
        accountVerification.put("secret", inputs.passCodeOldEmail);
        accountVerification.put("type", inputs.type);

        JSONObject body = new JSONObject();
        body.put("accountVerification", accountVerification);
        body.put("email", inputs.newEmail);
        body.put("newEmailOtpPassCode", inputs.passCodeNewEmail);

        UriHelper helper = new UriHelper("https://account.test.bliblitiket.dev/gateway","https://account.preprod.bliblitiket.dev/gateway");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/gks-unm-go-be/api/v1/security/email"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.put(request);
    }
}
