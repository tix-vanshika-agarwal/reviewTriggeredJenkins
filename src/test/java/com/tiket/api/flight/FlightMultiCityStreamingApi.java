package com.tiket.api.flight;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static com.tiket.core.CoreFunctions.log;

public record FlightMultiCityStreamingApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            List<String> requestItems,
            String accessToken
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/FlightCart_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        JSONObject body = getStaticJson("src/test/resources/api/flight/FlightMultiCityStreaming_body.json");
        body.put("requestItems", new JSONArray(inputs.requestItems));

        log("FlightMultiCityStreamingApi requestItems count: " + inputs.requestItems.size());
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-flight-search/search/multi-city/streaming"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
