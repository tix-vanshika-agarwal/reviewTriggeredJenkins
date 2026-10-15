package com.tiket.api.accommodation.hotel.prebook;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.accommodation.hotel.PreBookMultiRoomBody;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public record PreBookMultiRoomApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String authorization,
            int adult,
            String hotelId,
            int night,
            String startDate,
            int[] maxOccupancy,
            int qtyEachRoom,
            String[] rateCode,
            String[] roomId
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/prebook/PreBookMultiRoom_headers.properties");
        headers.put("Authorization", inputs.authorization);

        PreBookMultiRoomBody preBookMultiRoomBody = getBody();
        Gson gson = new Gson();
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-hotel-search/hotel/v4/prebook"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(preBookMultiRoomBody, PreBookMultiRoomBody.class))
                .build();

        return apiTools.post(request);
    }

    private PreBookMultiRoomBody getBody() {
        JSONObject body = getStaticJson("src/test/resources/api/accommodation/hotel/prebook/PreBookMultiRoom_body.json");
        Gson gson = new Gson();

        PreBookMultiRoomBody preBookMultiRoomBody = gson.fromJson(body.toString(), PreBookMultiRoomBody.class);
        preBookMultiRoomBody.hotels.get(0).adult = inputs.adult;
        preBookMultiRoomBody.hotels.get(0).hotelId = inputs.hotelId;
        preBookMultiRoomBody.hotels.get(0).night = inputs.night;
        preBookMultiRoomBody.hotels.get(0).startDate = inputs.startDate;

        int totalRooms = inputs.roomId.length;
        ArrayList<PreBookMultiRoomBody.Room> roomArrayList = new ArrayList<>();
        for (int i = 0; i < totalRooms; i++) {
            PreBookMultiRoomBody.Room roomData = new PreBookMultiRoomBody.Room();
            roomData.maxOccupancy = inputs.maxOccupancy[i];
            roomData.qty = inputs.qtyEachRoom;
            roomData.rateCode = inputs.rateCode[i];
            roomData.roomId = inputs.roomId[i];
            roomArrayList.add(roomData);
        }
        preBookMultiRoomBody.hotels.get(0).rooms = roomArrayList;

        return preBookMultiRoomBody;
    }
}
