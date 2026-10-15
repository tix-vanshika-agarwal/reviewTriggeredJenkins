package com.tiket.api.carTransfer;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.util.Map;

public record CarTransferSearchApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            BigDecimal latitude,
            BigDecimal longitude,
            String startDate,
            String regionalId,
            String accessToken
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/carTransfer/CarTransferSearch_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/carTransfer/CarTransferSearch_queryParams.properties");
        queryParams.put("latitude",inputs.latitude);
        queryParams.put("longitude",inputs.longitude);
        queryParams.put("startDate",inputs.startDate);
        queryParams.put("regionalId",inputs.regionalId);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-car-rental-customer-searching/v2/car/search"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }

}
