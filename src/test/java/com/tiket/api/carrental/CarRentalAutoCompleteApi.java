package com.tiket.api.carrental;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record CarRentalAutoCompleteApi(Inputs inputs) implements BaseApi {
    public record Inputs (
            String accessToken,
            String location,
            String driverOption
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/carrental/CarRentalAutoComplete_queryParams.properties");
        queryParams.put("keyword", inputs.location);
        queryParams.put("driverOption", inputs.driverOption);

        Map<String, Object> headers = getStaticMap("src/test/resources/api/carrental/SubmitBooking_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-car-rental-customer-searching/v3/locations/autocomplete"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}