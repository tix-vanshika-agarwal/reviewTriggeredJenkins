package com.tiket.api.carrental;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record CarRentalGetLocationIdApi(Inputs inputs) implements BaseApi {
    public record Inputs (
            String regionalId,
            String latitude,
            String longitude,
            String driverOption,
            String accessToken
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/carrental/SubmitBooking_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/carrental/CarRentalGetLocationId_queryParams.properties");
        queryParams.put("regionalId", inputs.regionalId);
        queryParams.put("latitude", inputs.latitude);
        queryParams.put("longitude", inputs.longitude);
        queryParams.put("driverOption", inputs.driverOption);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-car-rental-customer-searching/v1/zone/zone-pricing"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}