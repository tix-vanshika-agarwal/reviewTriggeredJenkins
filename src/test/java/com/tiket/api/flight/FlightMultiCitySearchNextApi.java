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

public record FlightMultiCitySearchNextApi(Inputs inputs) implements BaseApi {
    public record SelectedFlight(
            long paxPrice,
            String flightId,
            boolean comboMC,
            String supplierId,
            String departureTime,
            String arrivalTime,
            int segment
    ) {}

    public record Inputs(
            int segment,
            List<SelectedFlight> selectedFlights,
            List<FlightMultiCitySearchApi.Route> allRoutes,
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

        JSONArray flightDetails = new JSONArray();
        for (SelectedFlight flight : inputs.selectedFlights) {
            flightDetails.put(new JSONObject()
                    .put("paxPrice", flight.paxPrice())
                    .put("flightId", flight.flightId())
                    .put("comboMC", flight.comboMC())
                    .put("supplierId", flight.supplierId())
                    .put("departureTime", flight.departureTime())
                    .put("arrivalTime", flight.arrivalTime())
                    .put("segment", String.valueOf(flight.segment())));
        }

        JSONObject body = getStaticJson("src/test/resources/api/flight/FlightMultiCitySearchNext_body.json");
        body.put("segment", String.valueOf(inputs.segment))
                .put("cabinClass", FlightMultiCitySearchApi.resolveCabinClass(inputs.cabinClass))
                .put("adult", inputs.adult).put("child", inputs.child).put("infant", inputs.infant)
                .put("flightDetails", flightDetails)
                .put("allRoutes", FlightMultiCitySearchApi.toRoutesJson(inputs.allRoutes));

        log("FlightMultiCitySearchNextApi body: " + body);
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-flight-search/search/multi-city/next"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
