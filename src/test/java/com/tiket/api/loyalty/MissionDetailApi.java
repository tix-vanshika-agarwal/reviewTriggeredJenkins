package com.tiket.api.loyalty;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import java.util.Map;

/**
 * Fetches a user's live mission status via the mission backend microservice, e.g.
 * {@code GET /tix-mission/api/mission/stay-with-benefits/detail}.
 *
 * <p>This is the same payload the mission microsite (gateway path
 * {@code /ms-gateway/tix-mission/api/mission/{slug}/detail}) renders. Like {@link JoinMissionApi}, it
 * is called on the internal microservice and authenticates the target user through the
 * {@code X-Account-Id} + {@code X-Username} headers, so it needs no browser session/cookie or
 * Cloudflare clearance and can be polled reliably from the test runner.
 */
public record MissionDetailApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String missionSlug,
            String accountId,
            String username
    ) {
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(
                "https://mission-be-svc.test-platform-cluster.tiket.com",
                "https://mission-be-svc.preprod-platform-cluster.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/loyalty/MissionDetail_headers.properties");
        headers.put("X-Account-Id", inputs.accountId);
        headers.put("X-Username", inputs.username);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("tix-mission/api/mission/" + inputs.missionSlug + "/detail"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
