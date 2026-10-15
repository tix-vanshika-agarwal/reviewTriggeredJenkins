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

public record FlightMultiCitySearchApi(Inputs inputs) implements BaseApi {
    public static final String DEFAULT_CABIN_CLASS = "ECONOMY";

    public record Route(String origin, String destination, String departureDate) {}

    public record Inputs(
            List<Route> routes,
            int adult,
            int child,
            int infant,
            String cabinClass,
            String accessToken
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/FlightCart_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        JSONObject body = getStaticJson("src/test/resources/api/flight/FlightMultiCitySearch_body.json");
        body.put("adult", inputs.adult).put("child", inputs.child).put("infant", inputs.infant)
                .put("cabinClass", resolveCabinClass(inputs.cabinClass))
                .put("routes", toRoutesJson(inputs.routes));

        log("FlightMultiCitySearchApi body: " + body);
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-flight-search/search/multi-city"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }

    /**
     * The API rejects a request with no cabin class, and {@link JSONObject#put(String, Object)} removes
     * the key when handed {@code null} — so a blank value must never reach the body.
     */
    public static String resolveCabinClass(String cabinClass) {
        return cabinClass == null || cabinClass.isBlank() ? DEFAULT_CABIN_CLASS : cabinClass.trim().toUpperCase();
    }

    public static JSONArray toRoutesJson(List<Route> routes) {
        JSONArray array = new JSONArray();
        for (Route route : routes) {
            array.put(new JSONObject()
                    .put("origin", route.origin())
                    .put("destination", route.destination())
                    .put("departureDate", route.departureDate()));
        }
        return array;
    }
}
