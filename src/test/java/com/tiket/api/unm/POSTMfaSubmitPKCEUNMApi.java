package com.tiket.api.unm;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

/**
 * PKCE-aware variant of {@code POST /gks-unm-go-be/api/v1/mfa/submit}.
 *
 * <p>Same endpoint as {@link POSTMfaSubmitUNMApi}, but the request body additionally carries
 * {@code identity} (the email/phone the user logged in with). Created as a sibling class so the
 * existing legacy MFA-submit call sites are not impacted.
 *
 * <p>Response: same envelope as the legacy MFA submit — {@code data.redirectUrl} that contains
 * the {@code authCode} to feed into {@link POSTCodeChallengeVerifyUNMApi}.
 */
public record POSTMfaSubmitPKCEUNMApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String secret,
            String token,
            String type,
            String identity
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/POSTMfaSubmitPKCEUNMApi_headers.properties");

        JSONObject body = getStaticJson("src/test/resources/api/auth/POSTMfaSubmitPKCEUNMApi_body.json");
        body.put("secret", inputs.secret);
        body.put("token", inputs.token);
        body.put("type", inputs.type);
        body.put("identity", inputs.identity);

        UriHelper helper = new UriHelper("https://service.bliblitiket.tools", "https://account.preprod.bliblitiket.dev/gateway");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/gks-unm-go-be/api/v1/mfa/submit"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
