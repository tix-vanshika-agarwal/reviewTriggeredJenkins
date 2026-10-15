package com.tiket.api.giftCard;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import java.util.HashMap;
import java.util.Map;

public record GiftCardGetOrderDetailsApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String orderId,
            String orderHash
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/giftCard/GiftCardGetOrderDetails_headers.properties");
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("orderHash", inputs.orderHash);

        UriHelper uriHelper = new UriHelper("https://gift-card-be-svc.test-misc-cluster.tiket.com", "https://gift-card-be-svc.preprod-misc-cluster.tiket.com");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("tix-gift-card/gift-card/v1/orders/details/"+inputs.orderId))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
