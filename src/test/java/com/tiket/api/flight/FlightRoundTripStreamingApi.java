package com.tiket.api.flight;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public record FlightRoundTripStreamingApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            ArrayList<String> requestItems,
            String authorization,
            String streamType,
            String returnDate

    ){ }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/FlightStreaming_headers.properties");
        headers.put("Authorization",inputs.authorization);

        JSONObject body = getStaticJson("src/test/resources/api/flight/FlightStreamingApiRoundTrip_body.json");
        body.put("requestItems",inputs.requestItems);
        body.put("streamType",inputs.streamType);
        body.put("returnDate",inputs.returnDate);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-flight-search/v3/search/streaming"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
