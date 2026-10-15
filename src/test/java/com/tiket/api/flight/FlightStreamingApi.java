package com.tiket.api.flight;

import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.Platform;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record FlightStreamingApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            List<String> requestItems,
            String authorization,
            String streamType,
            String returnDate,
            String currency,
            String deviceId
    ){ }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/FlightStreaming_headers.properties");
        headers.put("Authorization",inputs.authorization);
        headers.put("Referer", uriHelper.getUri()+"/pesawat/search");
        headers.put("User-Agent", "tiketcom/" + (Configs.PLATFORM.equals(Platform.ANDROID)? "android-version": "ios-version"));
        headers.put("deviceId", inputs.deviceId);
        headers.put("currency", inputs.currency);
        headers.put("X-Currency", inputs.currency);

        JSONObject body;
        if(inputs.streamType.equalsIgnoreCase("ow")){
            body = getStaticJson("src/test/resources/api/flight/FlightStreaming_body.json");
            body.put("requestItems",inputs.requestItems);
            body.put("streamType",inputs.streamType);
        }else{
            body = getStaticJson("src/test/resources/api/flight/FlightStreamingApiRoundTrip_body.json");
            body.put("requestItems",inputs.requestItems);
            body.put("streamType",inputs.streamType);
            body.put("returnDate",inputs.returnDate);
        }

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-flight-search/v3/search/streaming"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
