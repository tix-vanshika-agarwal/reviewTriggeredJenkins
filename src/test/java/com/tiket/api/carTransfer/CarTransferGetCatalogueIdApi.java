package com.tiket.api.carTransfer;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record CarTransferGetCatalogueIdApi(Inputs inputs) implements BaseApi {
    /**
     * Inputs for the CarTransferGetCatalogueIdApi.
     *
     * @param accessToken          The access token for authentication.
     * @param origin               The origin location.
     * @param destinationPlaceId   The destination place ID.
     * @param pickupDate           The pickup date in the format "yyyy-MM-dd".
     * @param pickupTime           The pickup time in the format "HH:mm".
     * @param type                 The type of transfer (e.g., "AIRPORT", "LOCATION").
     * @param pax                  The number of passengers.
     */
    public record Inputs(
            String accessToken,
            String origin,
            String destinationPlaceId,
            String pickupDate,
            String pickupTime,
            String type,
            int pax
    ){}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/carTransfer/CarTransferGetCatalogue_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("referer", uriHelper.getUri() + "/airport-transfer/cari");

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/carTransfer/CarTransferGetCatalogue_queryParams.properties");
        queryParams.put("origin", inputs.origin);
        queryParams.put("destination", inputs.destinationPlaceId);
        queryParams.put("pickupDate",inputs.pickupDate);
        queryParams.put("pickupTime",inputs.pickupTime);
        queryParams.put("type", inputs.type);
        queryParams.put("pax", inputs.pax);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-airport-transfer-catalogue/v6/available-fleets"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
