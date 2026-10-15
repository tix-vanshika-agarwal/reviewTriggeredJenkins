package com.tiket.api.flight;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import static com.tiket.core.CoreFunctions.log;

public record FlightCartApi (Inputs inputs, InputsRoundTrip inputsRoundTrip) implements BaseApi {
    public record Inputs(
            String flightId,
            String supplierId, 
            String accessToken
    ) {}

    public record InputsRoundTrip(
            String departFlightId,
            String departSupplierId,
            String returnFlightId,
            String returnSupplierId,
            String accessToken
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/FlightCart_headers.properties");
        JSONObject body = getStaticJson("src/test/resources/api/flight/FlightCart_body.json");
        if (inputsRoundTrip == null) {
            headers.put("Authorization", inputs.accessToken);
            body.getJSONArray("cartDetails").
                    getJSONObject(0).
                    put("flightId", inputs.flightId);
            body.getJSONArray("cartDetails").
                    getJSONObject(0).
                    getJSONObject("supplierRequest").
                    put("supplierId", inputs.supplierId);
        } else {
            headers.put("Authorization", inputsRoundTrip.accessToken);
            JSONArray cartDetails = body.getJSONArray("cartDetails");

            JSONObject depart = cartDetails.getJSONObject(0);
            JSONObject returnFlight = new JSONObject(depart.toString());
            cartDetails.put(returnFlight);

            depart.put("flightId", inputsRoundTrip.departFlightId);
            depart.getJSONObject("supplierRequest").put("supplierId", inputsRoundTrip.departSupplierId);

            returnFlight.put("flightId", inputsRoundTrip.returnFlightId);
            returnFlight.getJSONObject("supplierRequest").put("supplierId", inputsRoundTrip.returnSupplierId);
        }


        log("FlightCartApi hitApi body: " + body.toString());
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-flight-cart/v5/cart"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
    
}
