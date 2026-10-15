package com.tiket.api.unm;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import org.json.JSONObject;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record POSTGenerateTokenApi(Inputs inputs) implements BaseApi {

    private static final int TOKEN_LIFETIME_SECONDS = 3600;
    private static final String ENDPOINT = "/gks-unm-go-be/api/v1/tokens";

    private static final String PROD_SERVICE_URL = "https://service.bliblitiket.tools";
    private static final String SANDBOX_SERVICE_URL = "https://sandbox.eph.bliblitiket.tools";

    private static final String STAGING_ISSUER = "https://account.test.bliblitiket.dev";
    private static final String SANDBOX_ISSUER = "https://account.preprod.bliblitiket.dev";
    private static final String PROD_ISSUER = "https://account.bliblitiket.com";

    public record Inputs(String email) {
        public Inputs {
            if (email == null || email.isBlank()) {
                throw new IllegalArgumentException("Email cannot be null or blank");
            }
        }
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        long now = Instant.now().getEpochSecond();
        long exp = now + TOKEN_LIFETIME_SECONDS;
        String jti = UUID.randomUUID().toString();

        String issuer = getIssuerUrl();

        JSONObject body = new JSONObject().put("sub", inputs.email).put("nbf", now).put("iss", issuer).put("exp", exp).put("iat", now).put("email", inputs.email).put("jti", jti);

        Map<String, Object> headers = new HashMap<>();
        headers.put("Accept", "*/*");
        headers.put("Content-Type", "application/json");

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("id", inputs.email);
        queryParams.put("type", "JWTSymmetric");
        queryParams.put("cacheStrategy", "none");
        queryParams.put("revokeStrategy", "kv");
        queryParams.put("lifetimeInSec", String.valueOf(TOKEN_LIFETIME_SECONDS));

        UriHelper helper = new UriHelper(PROD_SERVICE_URL, SANDBOX_SERVICE_URL);

        ApiRequest request = new ApiRequest.Builder().setUri(helper.withEndpoint(ENDPOINT)).setHeaderParams(headers).setQueryParams(queryParams).setBody(body.toString()).build();

        return apiTools.post(request);
    }

    private String getIssuerUrl() {
        return switch (Configs.ENVIRONMENT) {
        case GK -> STAGING_ISSUER;
        case PREPROD -> SANDBOX_ISSUER;
        default -> PROD_ISSUER;
        };
    }
}
