package com.tiket.api.loyalty;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import java.util.Map;

/**
 * Reads a user's eligibility for a mission via the mission backend microservice
 * {@code GET /tix-mission/api/mission-eligibility/{missionId}}.
 *
 * <p>{@code data.user.userStatus} is {@code NOT_ELIGIBLE} for a user outside the mission's segment,
 * {@code INACTIVE} for an eligible user who has not joined yet, and changes once the user joins;
 * {@code data.user.currentProgress} is the user's collected stamp count.
 */
public record MissionEligibilityApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String missionId,
            long accountId,
            String username
    ) {
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(
                "https://mission-be-svc.test-platform-cluster.tiket.com",
                "https://mission-be-svc.preprod-platform-cluster.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/loyalty/MissionEligibility_headers.properties");
        headers.put("X-Account-Id", String.valueOf(inputs.accountId));
        headers.put("X-Username", inputs.username);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("tix-mission/api/mission-eligibility/" + inputs.missionId))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
