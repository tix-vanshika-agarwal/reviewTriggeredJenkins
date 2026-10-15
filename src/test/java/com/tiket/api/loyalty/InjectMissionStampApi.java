package com.tiket.api.loyalty;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

/**
 * Injects mission progress (stamps) for a user via the mission backend microservice tools endpoint
 * {@code POST /tix-mission/api/tools/mission/progress}.
 *
 * <p>This lets a test fast-forward a fresh account straight to a <i>completed</i> mission without
 * having to perform {@code count} real eligible transactions, so the "claim reward" flow can be
 * exercised on its own.
 *
 * <p>The endpoint is protected by a signed challenge: {@code X-Challenge} and
 * {@code X-Challenge-Signature} are issued by the backend and rotate/expire. They are therefore
 * supplied as inputs (sourced from the test data sheet) so they can be refreshed whenever they
 * expire, instead of being hard-coded here.
 */
public record InjectMissionStampApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            long accountId,
            int amount,
            int count,
            String missionId,
            String status,
            String vertical,
            String challenge,
            String challengeSignature,
            String username
    ) {
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(
                "https://mission-be-svc.test-platform-cluster.tiket.com",
                "https://mission-be-svc.preprod-platform-cluster.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/loyalty/InjectMissionStamp_headers.properties");
        headers.put("X-Account-Id", String.valueOf(inputs.accountId));
        headers.put("X-Challenge", inputs.challenge);
        headers.put("X-Challenge-Signature", inputs.challengeSignature);
        headers.put("X-Username", inputs.username);

        JSONObject body = getStaticJson("src/test/resources/api/loyalty/InjectMissionStamp_body.json");
        body.put("accountId", inputs.accountId);
        body.put("amount", inputs.amount);
        body.put("count", inputs.count);
        body.put("missionId", inputs.missionId);
        body.put("status", inputs.status);
        body.put("vertical", inputs.vertical);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("tix-mission/api/tools/mission/progress"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
