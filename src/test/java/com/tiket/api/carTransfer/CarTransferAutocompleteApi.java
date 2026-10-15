package com.tiket.api.carTransfer;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record CarTransferAutocompleteApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String placeId
    ) {}
    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {


        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/carTransfer/CarTransferAutocomplete_queryParams.properties");
        queryParams.put("placeId",inputs.placeId);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-car-rental-customer-searching/v3/locations/details"))
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
