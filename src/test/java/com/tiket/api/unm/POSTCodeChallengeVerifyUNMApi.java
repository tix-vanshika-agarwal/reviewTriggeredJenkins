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
 * Exchanges a PKCE {@code authCode} (returned by {@link POSTAuthLoginPKCEUNMApi}) plus the
 * matching {@code codeVerifier} for a full session via
 * {@code POST /gks-unm-go-be/api/v1/auth/code-challenge/verify}.
 *
 * <p>This is the PKCE-flow counterpart of the legacy
 * {@link GETAuthLoginUNMApi GET /auth/code/verify} endpoint and replaces the "service ticket"
 * concept: the response carries the full session inline as
 * {@code data.session.id} (used as the {@code unmSessionId} on the deeplink),
 * {@code data.session.accessToken} and {@code data.session.refreshToken}, alongside
 * {@code data.userInfo}.
 */
public record POSTCodeChallengeVerifyUNMApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String authCode,
            String codeVerifier
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/POSTCodeChallengeVerifyUNMApi_headers.properties");

        JSONObject body = getStaticJson("src/test/resources/api/auth/POSTCodeChallengeVerifyUNMApi_body.json");
        body.put("authCode", inputs.authCode);
        body.put("codeVerifier", inputs.codeVerifier);

        UriHelper helper = new UriHelper("https://service.bliblitiket.tools", "https://account.preprod.bliblitiket.dev/gateway");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/gks-unm-go-be/api/v1/auth/code-challenge/verify"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
