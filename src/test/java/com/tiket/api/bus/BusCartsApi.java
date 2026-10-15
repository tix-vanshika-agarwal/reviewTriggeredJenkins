package com.tiket.api.bus;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.bus.BusCartsBody;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record BusCartsApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            String scheduleId,
            String scheduleHash,
            String seatName,
            int adult
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/PayExpress_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        JSONObject jsonObject = getStaticJson("src/test/resources/api/bus/BusCarts_body.json");
        Gson gson = new Gson();
        BusCartsBody busCartsBody = gson.fromJson(jsonObject.toString(), BusCartsBody.class);
        busCartsBody.schedules.depart.id = inputs.scheduleId;
        busCartsBody.schedules.depart.hash = inputs.scheduleHash;
        busCartsBody.passengerCount.adult = inputs.adult;

        ApiRequest apiRequest = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-bus-demand/v2/carts?phoneVerified=false"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(busCartsBody, BusCartsBody.class))
                .build();
        return apiTools.post(apiRequest);
    }
}
