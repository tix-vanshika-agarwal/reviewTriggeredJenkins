package com.tiket.api.loyalty;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import java.util.Map;

/**
 * Fetches the missions the Mission List Page shows a consumer, via the mission backend microservice
 * ({@code GET /tix-mission/api/mission-list}).
 *
 * <p>This is the payload behind the consumer Mission Center page ({@code /id-sg/mission}), so it is the
 * source of truth for the order missions are presented in. It identifies the user through the
 * {@code X-Account-Id} + {@code X-Username} headers, so it needs no browser session/cookie or Cloudflare
 * clearance.
 *
 * <p>Reached through the shared load balancer rather than the mission service's own cluster host, which
 * is only routable from inside the network and so times out from a developer machine.
 */
public record MissionListApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accountId,
            String username
    ) {
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com", "https://lb1-ms.preprod.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/loyalty/MissionList_headers.properties");
        headers.put("X-Account-Id", inputs.accountId);
        headers.put("X-Username", inputs.username);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("tix-mission/api/mission-list"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
