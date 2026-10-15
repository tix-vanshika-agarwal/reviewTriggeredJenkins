package com.tiket.api.train;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * Adds a Whoosh journey to a new cart; each segment is the journey's first {@code segmentSchedules} entry from the search.
 * {@code returnSegment} is null for a one-way trip.
 */
public record WhooshCartApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            JSONObject departSegment,
            JSONObject returnSegment,
            int adultCount,
            int infantCount
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/train/TrainAddToCart_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        JSONObject journeys = new JSONObject().put("depart", journey(inputs.departSegment));
        if (inputs.returnSegment != null) journeys.put("return", journey(inputs.returnSegment));
        JSONObject body = new JSONObject()
                .put("productType", "WHOOSH")
                .put("passengerCount", new JSONObject().put("adult", inputs.adultCount).put("infant", inputs.infantCount))
                .put("selectedJourneys", journeys);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-train-trx/v6/whoosh/carts"))
                .setQueryParams(getStaticMap("src/test/resources/api/train/TrainCart_queryParams.properties"))
                .setBody(body.toString())
                .setHeaderParams(headers)
                .build();
        return apiTools.post(request);
    }

    private static JSONObject journey(JSONObject segment) {
        JSONObject schedule = new JSONObject()
                .put("id", segment.getString("id"))
                .put("departDate", segment.getString("departureDate"))
                .put("originCode", segment.getJSONObject("departureStation").getString("code"))
                .put("destinationCode", segment.getJSONObject("arrivalStation").getString("code"))
                .put("trainNumber", segment.getString("trainNumber"))
                .put("subClass", segment.getJSONObject("subClass").getString("code"))
                .put("wagonClass", segment.getJSONObject("wagonClass").getString("code"));
        return new JSONObject().put("schedules", new JSONArray().put(schedule));
    }
}
