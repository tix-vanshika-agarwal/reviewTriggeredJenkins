package com.tiket.api.loyalty;

import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.Environment;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

/**
 * Fetches the current active tiket loyalty points balance from the loyalty-core service.
 *
 * <p>This endpoint was removed from the API gateway, so it is now called directly on the load
 * balancer ({@code /tix-loyalty-core/active-point}). Unlike the gateway path, the LB does not accept
 * the {@code Authorization} bearer token; it identifies the user through the {@code X-Account-Id} and
 * {@code X-Username} headers (both required).
 *
 * <p>This active-point LB endpoint has no PROD equivalent here: the two-arg {@link UriHelper}
 * intentionally maps PROD to the preprod LB, so this must only be run against testing (GK) or preprod.
 */
public record ActiveTiketPointsApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            Long accountId,
            String username
    ) {
        /**
         * Resolves the account id / username by decoding the JWT access token, which must carry the
         * custom {@code accountid} and {@code email} claims (e.g. the UNM login-API token). The browser
         * {@code session_access_token} cookie does NOT carry these claims — use
         * {@link #Inputs(long, String)} for that path.
         */
        public Inputs(String accessToken) {
            this(accessToken, null, null);
        }

        /** Uses an explicitly resolved account id / username (e.g. from the member API); no JWT decoding. */
        public Inputs(long accountId, String username) {
            this(null, accountId, username);
        }
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        if (Configs.ENVIRONMENT == Environment.PROD) {
            throw new IllegalStateException(
                    "ActiveTiketPointsApi active-point LB endpoint has no PROD target: the two-arg UriHelper would "
                            + "silently map PROD to the PREPROD load balancer. Run this only against testing (GK) or preprod.");
        }

        long accountId;
        String username;
        if (inputs.accountId != null && inputs.username != null) {
            accountId = inputs.accountId;
            username = inputs.username;
        } else {
            JSONObject jwt = CoreFunctions.decodeJwtPayload(inputs.accessToken);
            if (!jwt.has("accountid") || !jwt.has("email")) {
                throw new IllegalArgumentException(
                        "JWT access token is missing required claims (expected 'accountid' and 'email'). Claims present: "
                                + jwt.keySet()
                                + ". For browser session tokens, build Inputs(accountId, username) instead.");
            }
            accountId = jwt.getLong("accountid");
            username = jwt.getString("email");
        }

        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com", "https://lb1-ms.preprod.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/loyalty/ActivePoint_headers.properties");
        headers.put("X-Account-Id", String.valueOf(accountId));
        headers.put("X-Username", username);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("tix-loyalty-core/active-point"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
