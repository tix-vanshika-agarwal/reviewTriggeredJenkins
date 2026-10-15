package com.tiket.api.accommodation.hotel.supply;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * GET /tix-hotel-supply-dashboard/funnel/rooms — lists a vendor hotel's rooms. Used to resolve each
 * room's current {@code masterRoomGroup.groupingKey} (required to unmap it back to "Others").
 */
public record GetRoomGroupingRoomsApi(Inputs inputs) implements BaseApi {

    public record Inputs(String vendorName, String vendorHotelId, String vendorRoomName, boolean isMapped, int page, int size) {}

    // Only the preprod cluster host is known; reuse it for all envs.
    private static final String BASE_URL = "https://hotel-supply-dashboard-svc.preprod-hotel-cluster.tiket.com";

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(BASE_URL);

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/supply/RoomGroupingApi_headers.properties");
        headers.put("X-Request-Id", UUID.randomUUID().toString());

        Map<String, Object> queryParams = new LinkedHashMap<>();
        queryParams.put("isMapped", inputs.isMapped());
        queryParams.put("vendorName", inputs.vendorName());
        queryParams.put("vendorHotelId", inputs.vendorHotelId());
        if (inputs.vendorRoomName() != null && !inputs.vendorRoomName().isBlank()) {
            queryParams.put("vendorRoomName", inputs.vendorRoomName());
        }
        queryParams.put("page", inputs.page());
        queryParams.put("size", inputs.size());
        queryParams.put("sort", "name.en,asc");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-hotel-supply-dashboard/funnel/rooms"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
