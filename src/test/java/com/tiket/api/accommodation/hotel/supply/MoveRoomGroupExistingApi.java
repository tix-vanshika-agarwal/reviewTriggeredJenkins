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

/**
 * PATCH /tix-hotel-core/room-grouping/move-room-group/existing (public dashboard gateway) — moves
 * rooms into an existing group ({@code targetGroupingKey}). Each room carries the group it currently
 * sits in via {@code oldGroupingKey}. Used to revert test moves back to each room's original group
 * (and, with the static "Others" grouping key as the target, back into the Others section).
 *
 * <p>This endpoint is cookie-authenticated: pass the logged-in dashboard session cookie.
 */
public record MoveRoomGroupExistingApi(Inputs inputs) implements BaseApi {

    public record Room(String roomId, String oldGroupingKey) {}

    public record Inputs(String cookie, String hotelId, String targetGroupingKey, List<Room> rooms) {}

    // Preprod-only public gateway host.
    private static final String BASE_URL = "https://en.preprod.tiket.com";

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(BASE_URL);

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/supply/MoveRoomGroupExistingApi_headers.properties");
        headers.put("referer", BASE_URL + "/admin/mm-dashboard/v4/hotel-library/manage-room-group/" + inputs.hotelId());
        headers.put("cookie", inputs.cookie());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("targetGroupingKey", inputs.targetGroupingKey());
        body.put("hotelId", inputs.hotelId());
        body.put("rooms", inputs.rooms());

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-api-gateway-dashboard-hotel/tix-hotel-core/room-grouping/move-room-group/existing"))
                .setHeaderParams(headers)
                .setBody(new Gson().toJson(body))
                .build();

        return apiTools.patch(request);
    }
}
