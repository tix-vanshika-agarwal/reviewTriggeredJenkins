package com.tiket.api.accommodation.hotel.supply;

import com.tiket.io.api.ApiTools;
import com.tiket.model.Environment;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;

import java.util.LinkedHashMap;
import java.util.Map;

public record HotelApprovalApprovedApi(Inputs inputs) implements BaseApi {

    public record Inputs(String bearerToken, String hotelName, String cookie) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/HotelApprovalApprovedApi_headers.properties");
        headers.put("authorization", inputs.bearerToken());
        headers.put("requestid", String.valueOf(System.currentTimeMillis()));
        headers.put("referer", getRefererUrl());
        if (inputs.cookie() != null && !inputs.cookie().isBlank()) {
            headers.put("cookie", inputs.cookie());
        }

        Map<String, Object> queryParams = new LinkedHashMap<>();
        queryParams.put("columns[0].name", "name");
        queryParams.put("columns[0].value", inputs.hotelName());
        queryParams.put("page", 0);
        queryParams.put("size", 10);

        String endpoint = "/tix-api-gateway-dashboard-hotel/tix-hotel-native-dashboard/admin/approval/approved";

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }

    private static String getRefererUrl() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            return "https://gatotkaca.tiket.com/admin/mm-dashboard/hotel-native/registration/approve-activation";
        }
        return "https://preprod.tiket.com/admin/mm-dashboard/hotel-native/registration/approve-activation";
    }
}
