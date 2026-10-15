package com.tiket.api.accommodation.hotel.supply;

import com.tiket.io.api.ApiTools;
import com.tiket.model.Environment;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;

import java.util.Map;

public record GETMMDashboardMenuApi(Inputs inputs) implements BaseApi {

    public record Inputs(String aatBearerToken, String aatToken, String artToken) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/GETMMDashboardMenuApi_headers.properties");
        headers.put("authorization", inputs.aatBearerToken());
        headers.put("requestid", String.valueOf(System.currentTimeMillis()));
        headers.put("referer", getRefererUrl());
        headers.put("cookie", "aat=" + inputs.aatToken() + "; art=" + inputs.artToken());

        String endpoint = "/admin/mm-dashboard/v4/api/menu";

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }

    private static String getRefererUrl() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            return "https://gatotkaca.tiket.com/admin/mm-dashboard";
        }
        return "https://preprod.tiket.com/admin/mm-dashboard";
    }
}
