package com.tiket.api.accommodation.hotel.prebook;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.accommodation.hotel.PreBookGeneratingCartBody;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public record PreBookGeneratingCartApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String username,
            int adult,
            String hotelId,
            int night,
            String rateCode,
            int room,
            String roomId,
            String startDate,
            int maxRoomOccupancy,
            int maxChildOccupancy,
            String currency
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/prebook/PreBookGeneratingCart_headers.properties");
        headers.put("cookie", "session_access_token=" + inputs.accessToken +";");
        headers.put("currency", inputs.currency);

        PreBookGeneratingCartBody preBookGeneratingCartBody = getBody();
        Gson gson = new Gson();

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-hotel-search/hotel/v4/prebook"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(preBookGeneratingCartBody, PreBookGeneratingCartBody.class))
                .build();

        return apiTools.post(request);
    }

    private PreBookGeneratingCartBody getBody() {
        JSONObject body = getStaticJson("src/test/resources/api/accommodation/hotel/prebook/PreBookGeneratingCart_body.json");
        Gson gson = new Gson();

        PreBookGeneratingCartBody preBookGeneratingCartBody = gson.fromJson(body.toString(), PreBookGeneratingCartBody.class);
        preBookGeneratingCartBody.hotels.get(0).adult = inputs.adult;

        preBookGeneratingCartBody.hotels.get(0).hotelId = inputs.hotelId;
        preBookGeneratingCartBody.hotels.get(0).night = inputs.night;
        preBookGeneratingCartBody.hotels.get(0).startDate = inputs.startDate;
        preBookGeneratingCartBody.hotels.get(0).rooms = new ArrayList<>();

        PreBookGeneratingCartBody.Room room = new PreBookGeneratingCartBody.Room();
        room.roomId = inputs.roomId;
        room.rateCode = inputs.rateCode;
        room.qty = inputs.room;
        room.maxOccupancy = inputs.maxRoomOccupancy;
        room.maxChildOccupancy = inputs.maxChildOccupancy;

        preBookGeneratingCartBody.hotels.get(0).rooms.add(room);

        return preBookGeneratingCartBody;
    }
}
