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
 * POST /tix-hotel-core/hidden/master/execute-query — hidden master-data endpoint. Sets {@code quality}
 * to null on one {@code image_raw} document, which puts the image back into "Not scored yet".
 * Writes straight to the DB: use for test-data cleanup only.
 */
public record ResetImageQualityApi(Inputs inputs) implements BaseApi {

    public record Inputs(String imageRawId) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(
                "http://hotel-core-be.accom-supply-ns.svc.tiket",
                "https://hotel-core-be-svc.preprod-hotel-cluster.tiket.com");

        Map<String, Object> headers = getStaticMap(
                "src/test/resources/api/accommodation/hotel/supply/ResetImageQualityApi_headers.properties");
        headers.put("requestId", UUID.randomUUID().toString());

        Map<String, Object> queryParams = new LinkedHashMap<>();
        queryParams.put("collectionName", "image_raw");
        queryParams.put("queryUpdateType", "SET");
        queryParams.put("id", inputs.imageRawId());

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-hotel-core/hidden/master/execute-query"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .setBody("{\"quality\": null}")
                .build();

        return apiTools.post(request);
    }
}
