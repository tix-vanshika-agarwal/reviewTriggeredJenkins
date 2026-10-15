package com.tiket.api.accommodation.hotel.supply;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record UnmappedHotelsSearchApi(Inputs inputs) implements BaseApi {

    public record Inputs(String vendorName, int page, int limit) {
        public Inputs(String vendorName) {
            this(vendorName, 0, 10000);
        }
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(
                "https://hotel-core-be-svc.test-hotel-cluster.tiket.com",
                "https://hotel-core-be-svc.preprod-hotel-cluster.tiket.com");

        Map<String, Object> headers = getStaticMap(
                "src/test/resources/api/accommodation/hotel/supply/UnmappedHotelsSearchApi_headers.properties");
        headers.put("requestId", UUID.randomUUID().toString());

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("vendorName", inputs.vendorName);
        queryParams.put("page", inputs.page);
        queryParams.put("limit", inputs.limit);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-hotel-core/funnel/unmapped-hotels/search"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
