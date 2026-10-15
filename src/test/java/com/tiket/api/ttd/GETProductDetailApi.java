package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GETProductDetailApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String language,
            String adminAccessToken,
            String userAgent,
            String productId
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/carrental/CarRentalAdminConfirmOrder_headers.properties");
        headers.put("authorization", "Bearer "+inputs.adminAccessToken);
        headers.put("useragent", inputs.userAgent);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/tix-api-gateway-dashboard-event/tix-events-v2-inventory/admins/products/" + inputs.productId))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
