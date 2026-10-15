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

/** GET /tix-hotel-core/hotel — hotel search by vendor filters. */
public record GetHotelByVendorApi(Inputs inputs) implements BaseApi {

    public record Inputs(String vendorName, String vendorExternalId, int page, int limit) {
        public Inputs(String vendorName, String vendorExternalId) {
            this(vendorName, vendorExternalId, 0, 20);
        }
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(
                "http://hotel-core-be.accom-supply-ns.svc.tiket",
                "https://hotel-core-be-svc.preprod-hotel-cluster.tiket.com");

        Map<String, Object> headers = getStaticMap(
                "src/test/resources/api/accommodation/hotel/supply/GetHotelByVendorApi_headers.properties");
        headers.put("requestId", UUID.randomUUID().toString());

        Map<String, Object> queryParams = new LinkedHashMap<>();
        queryParams.put("isDeleted", 0);
        queryParams.put("vendorName", inputs.vendorName());
        queryParams.put("vendorExternalId", inputs.vendorExternalId());
        queryParams.put("page", inputs.page());
        queryParams.put("limit", inputs.limit());
        queryParams.put("sort", "updatedDate,asc");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-hotel-core/hotel"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
