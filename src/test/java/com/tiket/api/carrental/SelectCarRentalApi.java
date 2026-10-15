package com.tiket.api.carrental;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record SelectCarRentalApi(Inputs inputs) implements BaseApi {
    public record Inputs (
            String accessToken,
            String driverOption,
            String regionalId,
            String startDate,
            String pickupTime,
            String latitude,
            String longitude
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/carrental/SelectCarRental_queryParams.properties");
        queryParams.put("driverOption", inputs.driverOption);
        queryParams.put("regionalId", inputs.regionalId);
        queryParams.put("startDate", inputs.startDate);
        queryParams.put("pickupTime", inputs.pickupTime);
        queryParams.put("latitude", inputs.latitude);
        queryParams.put("longitude", inputs.longitude);
        Map<String, Object> headers = getStaticMap("src/test/resources/api/carrental/SubmitBooking_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-car-rental-customer-searching/v2/car/search"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}