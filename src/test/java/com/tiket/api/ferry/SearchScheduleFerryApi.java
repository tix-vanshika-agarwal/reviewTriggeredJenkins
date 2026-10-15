package com.tiket.api.ferry;

import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.util.Map;

public record  SearchScheduleFerryApi(Input input) implements BaseApi {
    public record Input(
        String accessToken, 
        String origin,
        String destination,
        String departureDate,
        String departureTime,
        String vehicleType,
        int adult,
        int child,
        int infant,
        int elderly){}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/ferry/searchSchedulesFerryHeaders.properties");
        headers.put("Authorization", input.accessToken);
        headers.put("deviceId", CoreFunctions.generateDeviceId());

        Map<String, Object> queryParams = getStringObjectMap();

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-ferry-demand/v1/search"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }

    private Map<String, Object> getStringObjectMap() {
        Map<String,Object> queryParams = getStaticMap("src/test/resources/api/ferry/searchSchedulesFerryQueryParams.properties");
        queryParams.put("origin", input.origin);
        queryParams.put("destination", input.destination);
        queryParams.put("departureDate", input.departureDate);
        queryParams.put("departureTime", input.departureTime);
        queryParams.put("vehicleType", input.vehicleType);
        queryParams.put("adult", input.adult);
        queryParams.put("child", input.child);
        queryParams.put("infant", input.infant);
        queryParams.put("elderly", input.elderly);
        return queryParams;
    }
}
