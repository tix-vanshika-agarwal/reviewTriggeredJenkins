package com.tiket.api.accommodation.hotel.supply;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * POST /tix-hotel-supply-dashboard/funnel/room-grouping/unmap-room — moves the given rooms out of
 * their current group (identified by {@code groupingKey}) back into the "Others" section.
 */
public record UnmapRoomApi(Inputs inputs) implements BaseApi {

    public record Inputs(String hotelId, String groupingKey, List<String> roomIds) {}

    // Only the preprod cluster host is known; reuse it for all envs.
    private static final String BASE_URL = "https://hotel-supply-dashboard-svc.preprod-hotel-cluster.tiket.com";

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(BASE_URL);

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/supply/RoomGroupingApi_headers.properties");
        headers.put("X-Request-Id", UUID.randomUUID().toString());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("hotelId", inputs.hotelId());
        body.put("groupingKey", inputs.groupingKey());
        body.put("roomIds", inputs.roomIds());

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-hotel-supply-dashboard/funnel/room-grouping/unmap-room"))
                .setHeaderParams(headers)
                .setBody(new Gson().toJson(body))
                .build();

        return apiTools.post(request);
    }
}
