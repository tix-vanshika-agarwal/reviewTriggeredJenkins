package com.tiket.api.gamification;

import com.tiket.io.api.ApiTools;
import com.tiket.model.Environment;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import org.json.JSONObject;

import java.util.Map;

/**
 * Records a hit on a Berburu Tiket Murah hunt session for one account through
 * {@code POST /ggwp-server/harta-karun/session/{sessionId}/hit}, which is what makes that account
 * the current holder of the treasure.
 *
 * <p>The game will not let a player snatch a treasure they are already holding, so the in-app snatch
 * button stays inactive for the current holder. A test that needs to exercise snatching therefore
 * uses this endpoint to have a <i>second</i> account take the treasure first, which puts the account
 * under test back into a state where it can bid.
 *
 * <p>This endpoint is not published on the shared {@code lb1-ms} load balancer — that host routes
 * {@code /ggwp-server/} but answers {@code {"message":"Not Found"}} for the harta-karun paths — so it
 * is called directly on the hunt service in the preprod platform cluster, which is the only host verified
 * to serve it. PREPROD is therefore the only supported environment.
 */
public record SnatchTreasureApi(Inputs inputs) implements BaseApi {

    /**
     * @param accountId the account that should end up holding the treasure ({@code X-Account-Id})
     * @param sessionId the hunt session of the campaign being bid on
     * @param hitKey    opaque key the endpoint requires on every hit; the backend accepts a repeated
     *                  value, so this does not make the call idempotent
     */
    public record Inputs(long accountId, String sessionId, String hitKey) {
    }

    /**
     * Whether the current environment has a host serving this endpoint. Callers are expected to ask
     * first and decide what an unsupported environment means for them — a run that cannot reach the
     * hunt backend has not failed, it has nothing to say.
     */
    public static boolean isUnSupportedEnvironment() {
        return Configs.ENVIRONMENT != Environment.PREPROD;
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        if (isUnSupportedEnvironment()) {
            throw new IllegalStateException(
                    "this api is not supported in the " + Configs.ENVIRONMENT + " environment");
        }

        Map<String, Object> headers = getStaticMap(
                "src/test/resources/api/gamification/SnatchTreasure_headers.properties");
        headers.put("X-Account-Id", String.valueOf(inputs.accountId));

        JSONObject body = getStaticJson("src/test/resources/api/gamification/SnatchTreasure_body.json");
        body.put("key", inputs.hitKey);

        UriHelper helper = new UriHelper("https://ggwp-server-svc.preprod-platform-cluster.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("ggwp-server/harta-karun/session/" + inputs.sessionId + "/hit"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
