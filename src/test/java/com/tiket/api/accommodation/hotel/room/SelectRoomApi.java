package com.tiket.api.accommodation.hotel.room;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record SelectRoomApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String username,
            int adult,
            String hotelID,
            int night,
            int room,
            String startDate,
            String lang,
            String currency
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/room/SelectRoom_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("username", inputs.username);
        headers.put("lang",inputs.lang);
        headers.put("currency",inputs.currency);
        if (inputs.currency.isEmpty()) headers.put("currency", "IDR");

        JSONObject body = getStaticJson("src/test/resources/api/accommodation/hotel/room/SelectRoom_body.json");
        body.put("adult", inputs.adult);
        body.put("hotelId", inputs.hotelID);
        body.put("night", inputs.night);
        body.put("room", inputs.room);
        body.put("startDate", inputs.startDate);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-hotel-search/v3/room"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
