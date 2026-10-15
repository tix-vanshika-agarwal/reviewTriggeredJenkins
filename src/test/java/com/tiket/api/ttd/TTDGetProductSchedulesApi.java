package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public record TTDGetProductSchedulesApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            /** All fields below come from the test data sheet, not hardcoded. */
            String productId,
            String packageCode,
            String sortAttributes,
            String sortDirection,
            String dateTimeFrom,
            String dateTimeTo,
            String pageSize,
            String pageNumber
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/ttd/TTDGetProductSchedules_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("packageCode", inputs.packageCode);
        queryParams.put("productId", inputs.productId);
        queryParams.put("sortAttributes", inputs.sortAttributes);
        queryParams.put("sortDirection", inputs.sortDirection);
        queryParams.put("dateTimeFrom", inputs.dateTimeFrom);
        queryParams.put("dateTimeTo", inputs.dateTimeTo);
        queryParams.put("pageSize", inputs.pageSize);
        queryParams.put("pageNumber", inputs.pageNumber);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-events-v2-inventory/v1/productSchedules"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
