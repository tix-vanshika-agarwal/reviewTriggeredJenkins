package com.tiket.api.accommodation.hotel;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.util.Map;

// GET pending-retry-summary; headers from HotelPendingRetrySummaryCron_headers.properties.
public record HotelPendingRetrySummaryCronApi(Inputs inputs) implements BaseApi {

    public record Inputs(String endpointUrl) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap(
                "src/test/resources/api/accommodation/hotel/HotelPendingRetrySummaryCron_headers.properties");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(inputs.endpointUrl())
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
