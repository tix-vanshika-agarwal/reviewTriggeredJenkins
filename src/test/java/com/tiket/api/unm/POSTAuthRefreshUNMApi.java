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
 * Exchanges a {@code sessionRefreshToken} for a new session via
 * {@code POST /gks-unm-go-be/api/v1/auth/refresh}.
 *
 * <p>Used to keep a session alive (or to mint a fresh {@code session.id} for deeplink injection)
 * without re-running the full PKCE login dance. The refresh token is the same JWT returned as
 * {@code data.session.refreshToken} from {@link POSTCodeChallengeVerifyUNMApi}.
 *
 * <p>Response: {@code data.session.id} / {@code data.session.accessToken} /
 * {@code data.session.refreshToken} (same shape as {@link POSTCodeChallengeVerifyUNMApi}).
 */
public record POSTAuthRefreshUNMApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String sessionRefreshToken
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/POSTAuthRefreshUNMApi_headers.properties");

        JSONObject body = getStaticJson("src/test/resources/api/auth/POSTAuthRefreshUNMApi_body.json");
        body.put("sessionRefreshToken", inputs.sessionRefreshToken);

        UriHelper helper = new UriHelper("https://service.bliblitiket.tools", "https://account.preprod.bliblitiket.dev/gateway");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/gks-unm-go-be/api/v1/auth/refresh"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
