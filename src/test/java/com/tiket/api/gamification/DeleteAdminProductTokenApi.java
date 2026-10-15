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
 * Deletes a harta-karun product-token override through
 * {@code DELETE /ggwp-server/harta-karun/admin/product-token/{productTokenId}}.
 *
 * <p>Hosts: GK {@code http://ggwp-server.eiffel-ns.svc.tiket} (verified 27 Aug 2026, a missing
 * id returned the service's structured 404); preprod
 * {@code https://ggwp-server-svc.preprod-platform-cluster.tiket.com}. The two-arg {@link UriHelper}
 * would send prod at preprod, so PROD is refused.
 */
public record DeleteAdminProductTokenApi(Inputs inputs) implements BaseApi {

    /**
     * @param productTokenId the override id in the path (e.g. {@code 6a7c1aa6dc5ba8f7b9769217})
     */
    public record Inputs(String productTokenId) {
    }

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
                .setUri(helper.withEndpoint(
                        "ggwp-server/harta-karun/admin/product-token/" + inputs.productTokenId))
                .setHeaderParams(headers)
                .build();

        return apiTools.delete(request);
    }
}