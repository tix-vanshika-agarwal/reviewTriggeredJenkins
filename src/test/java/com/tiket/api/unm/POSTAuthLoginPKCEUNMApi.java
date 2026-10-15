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
 * PKCE-aware variant of {@code POST /gks-unm-go-be/api/v1/auth/login}.
 *
 * <p>Differs from {@link POSTAuthLoginUNMApi} in that the body carries a {@code codeChallenge}
 * (RFC&nbsp;7636 S256) and a deeplink-shaped {@code ref} (e.g. {@code unm<clientId>://loginResult}),
 * which is what the mobile SDK sends when initiating an Authorization Code with PKCE flow.
 *
 * <p>Created as a sibling class (rather than overloading the existing record) so the legacy
 * non-PKCE login flow is left completely untouched.
 *
 * <p>Response: same envelope as the legacy login — {@code data.redirectUrl} on success, or
 * {@code code=MFA_REQUIRED} with {@code data.token} when MFA is required.
 */
public record POSTAuthLoginPKCEUNMApi(Inputs inputs) implements BaseApi {

    /**
     * @param username      account email
     * @param password      account secret
     * @param codeChallenge base64url-no-padding SHA-256 of the PKCE {@code codeVerifier}
     * @param ref           deeplink callback target (e.g. {@code unm<clientId>://loginResult});
     *                      the corresponding {@code codeVerifier} must be retained for the
     *                      subsequent {@link POSTCodeChallengeVerifyUNMApi} call.
     */
    public record Inputs(
            String username,
            String password,
            String codeChallenge,
            String ref
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/POSTAuthLoginPKCEUNMApi_headers.properties");

        JSONObject body = getStaticJson("src/test/resources/api/auth/POSTAuthLoginPKCEUNMApi_body.json");
        body.put("identity", inputs.username);
        body.put("secret", inputs.password);
        body.put("ref", inputs.ref);
        body.put("codeChallenge", inputs.codeChallenge);

        UriHelper helper = new UriHelper("https://service.bliblitiket.tools", "https://account.preprod.bliblitiket.dev/gateway");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/gks-unm-go-be/api/v1/auth/login"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
