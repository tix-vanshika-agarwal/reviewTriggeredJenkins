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

public record FlightMultiCityPrpApi(Inputs inputs) implements BaseApi {
    public record Detail(String flightId, String supplierId, boolean isFullRefund, boolean comboMC) {}

    public record Inputs(
            List<Detail> details,
            String accessToken
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/FlightCart_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        JSONArray details = new JSONArray();
        for (Detail detail : inputs.details) {
            details.put(new JSONObject()
                    .put("flightId", detail.flightId())
                    .put("supplierId", detail.supplierId())
                    .put("isFullRefund", detail.isFullRefund())
                    .put("comboMC", detail.comboMC()));
        }

        JSONObject body = getStaticJson("src/test/resources/api/flight/FlightMultiCityPrp_body.json");
        body.put("details", details);

        log("FlightMultiCityPrpApi body: " + body);
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-flight-search/multi-city/prp/flight-detail"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
