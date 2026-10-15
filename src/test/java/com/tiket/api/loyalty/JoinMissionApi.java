package com.tiket.api.loyalty;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import java.util.Map;

/**
 * Joins a loyalty mission for a user via the mission backend microservice, e.g.
 * {@code POST /tix-mission/api/mission/stay-with-benefits/join}.
 *
 * <p>This is the internal microservice endpoint (channel {@code MICROSERVICE}); it authenticates the
 * target user through the {@code X-Account-Id} + {@code X-Username} headers rather than a browser
 * session/cookie, so no login cookies or Cloudflare clearance are required.
 */
public record JoinMissionApi(Inputs inputs) implements BaseApi {

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

        Map<String, Object> headers = getStaticMap("src/test/resources/api/loyalty/JoinMission_headers.properties");
        headers.put("X-Account-Id", inputs.accountId);
        headers.put("X-Username", inputs.username);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("tix-mission/api/mission/" + inputs.missionSlug + "/join"))
                .setHeaderParams(headers)
                .setBody("")
                .build();

        return apiTools.post(request);
    }
}
