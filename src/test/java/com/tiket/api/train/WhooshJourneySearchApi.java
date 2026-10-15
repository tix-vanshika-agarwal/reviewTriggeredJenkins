package com.tiket.api.train;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.util.Map;

public record WhooshJourneySearchApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            String originStation,
            String destinationStation,
            String departureDate,
            // blank for a one-way search
            String returnDate,
            int adultCount,
            int infantCount
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/train/TrainAddToCart_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/train/TrainSearch_queryParams.properties");
        queryParams.put("orig", inputs.originStation);
        queryParams.put("dest", inputs.destinationStation);
        queryParams.put("ddate", inputs.departureDate);
        boolean roundTrip = inputs.returnDate != null && !inputs.returnDate.isBlank();
        queryParams.put("ttype", roundTrip ? "ROUND_TRIP" : "ONE_WAY");
        queryParams.put("rdate", roundTrip ? inputs.returnDate : "");
        queryParams.put("acount", inputs.adultCount);
        queryParams.put("icount", inputs.infantCount);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-train-search-v2/v7/whoosh/journeys"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();
        return apiTools.get(request);
    }
}
