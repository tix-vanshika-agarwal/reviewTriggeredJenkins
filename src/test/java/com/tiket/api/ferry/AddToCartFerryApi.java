package com.tiket.api.ferry;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public record AddToCartFerryApi(Input input) implements BaseApi {
    public record Input(
        String accessToken,
        String scheduleIdDepart, 
        int adult,
        int child,
        int infant,
        int elderly
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
         Map<String, Object> headers = getStaticMap("src/test/resources/api/ferry/AddToCartFerryHeaders.properties");
        headers.put("Authorization",input.accessToken);

        //Payload structure can be seen at src/test/resources/api/ferry/AddToCartFerryPayload.json
        JSONArray schedules = new JSONArray();
        JSONObject schedule = new JSONObject();
        schedule.put("tripType", "DEPART");
        schedule.put("scheduleId", input.scheduleIdDepart);
        schedules.put(schedule);
        JSONObject passengerCount = new JSONObject();
        passengerCount.put("adult", input.adult);
        passengerCount.put("child", input.child);
        passengerCount.put("infant", input.infant);
        passengerCount.put("elderly", input.elderly);
        JSONObject body = new JSONObject();
        body.put("schedules", schedules);
        body.put("passengerCount", passengerCount);
        
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-ferry-demand/v1/carts?phoneVerified=false"))
                .setBody(body.toString())
                .setHeaderParams(headers)
                .build();

        return apiTools.post(request);
    }

}
