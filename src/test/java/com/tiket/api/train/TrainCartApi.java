package com.tiket.api.train;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.train.TrainCartBody;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public record TrainCartApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            String journeyCode,
            int adultCount,
            int infantCount,
            String returnJourneyCode
    ) {
        public Inputs(String accessToken, String journeyCode, int adultCount, int infantCount) {
            this(accessToken, journeyCode, adultCount, infantCount, null);
        }
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/train/TrainCart_queryParams.properties");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/train/TrainAddToCart_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        JSONObject body = getStaticJson("src/test/resources/api/train/TrainAddToCart_body.json");
        Gson gson = new Gson();
        TrainCartBody cartBody = gson.fromJson(body.toString(), TrainCartBody.class);

        cartBody.passengerCount.adult = inputs.adultCount;
        cartBody.passengerCount.infant = inputs.infantCount;
        applySchedule(cartBody.selectedJourneys.depart.schedules.get(0), inputs.journeyCode);

        if (inputs.returnJourneyCode != null && !inputs.returnJourneyCode.isBlank()) {
            TrainCartBody.Schedule returnSchedule = new TrainCartBody.Schedule();
            applySchedule(returnSchedule, inputs.returnJourneyCode);
            cartBody.selectedJourneys.returnJourney = new TrainCartBody.Depart();
            cartBody.selectedJourneys.returnJourney.schedules = new ArrayList<>();
            cartBody.selectedJourneys.returnJourney.schedules.add(returnSchedule);
        }

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-train-trx/v6/train/carts"))
                .setQueryParams(queryParams)
                .setBody(gson.toJson(cartBody, TrainCartBody.class))
                .setHeaderParams(headers)
                .build();
        return apiTools.post(request);
    }

    private static void applySchedule(TrainCartBody.Schedule schedule, String journeyCode) {
        String[] journeyInfo = journeyCode.split("-");
        schedule.id = journeyCode;
        schedule.departDate = journeyInfo[2] + "-" + journeyInfo[3] + "-" + journeyInfo[4];
        schedule.destinationCode = journeyInfo[1];
        schedule.originCode = journeyInfo[0];
        schedule.subClass = journeyInfo[7];
        schedule.trainNumber = journeyInfo[5];
        schedule.wagonClass = journeyInfo[6] + ":" + journeyInfo[8];
    }

}
