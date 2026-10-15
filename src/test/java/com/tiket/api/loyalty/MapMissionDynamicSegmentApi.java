package com.tiket.api.loyalty;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

/**
 * Maps a user into a dynamic-segment mission's segment via the mission backend microservice
 * {@code POST /tix-mission/api/dynamic-segment}, so the user becomes eligible to join that mission.
 *
 * <p>The mapping is one-shot per account and mission: the first call returns {@code 200 SUCCESS}, a
 * repeat returns {@code 409 DYNAMIC_SEGMENT_MAPPING_EXISTS}, and a mission without dynamic segment
 * enabled returns {@code 400 DYNAMIC_SEGMENT_DISABLED}. Like {@link JoinMissionApi}, the caller is
 * identified by {@code X-Account-Id} + {@code X-Username}, so no browser session is required.
 */
public record MapMissionDynamicSegmentApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            long accountId,
            String missionId,
            String username
    ) {
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(
                "https://mission-be-svc.test-platform-cluster.tiket.com",
                "https://mission-be-svc.preprod-platform-cluster.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/loyalty/MapMissionDynamicSegment_headers.properties");
        headers.put("X-Account-Id", String.valueOf(inputs.accountId));
        headers.put("X-Username", inputs.username);

        JSONObject body = getStaticJson("src/test/resources/api/loyalty/MapMissionDynamicSegment_body.json");
        body.put("accountId", inputs.accountId);
        body.put("missionId", inputs.missionId);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("tix-mission/api/dynamic-segment"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
