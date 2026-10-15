package com.tiket.api.gamification;

import com.tiket.io.api.ApiTools;
import com.tiket.model.Environment;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

/**
 * Lists harta-karun product-token overrides through
 * {@code GET /ggwp-server/harta-karun/admin/product-token}.
 *
 * <p>Hosts: GK {@code http://ggwp-server.eiffel-ns.svc.tiket} (verified 27 Aug 2026, GET
 * {@code 200} / {@code SUCCESS}); preprod
 * {@code https://ggwp-server-svc.preprod-platform-cluster.tiket.com}. The two-arg {@link UriHelper}
 * would send prod at preprod, so PROD is refused.
 */
public record GetAdminProductTokenApi() implements BaseApi {

    public static boolean isUnsupportedEnvironment() {
        return Configs.ENVIRONMENT == Environment.PROD;
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        if (isUnsupportedEnvironment()) {
            throw new IllegalStateException(
                    "admin product-token has no prod host, so a "
                            + Configs.ENVIRONMENT
                            + " run would be pointed at preprod. Run this on gk or preprod.");
        }

        Map<String, Object> headers = new HashMap<>();
        headers.put("accept", "application/json");

        UriHelper helper = new UriHelper(
                "http://ggwp-server.eiffel-ns.svc.tiket",
                "https://ggwp-server-svc.preprod-platform-cluster.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("ggwp-server/harta-karun/admin/product-token"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}