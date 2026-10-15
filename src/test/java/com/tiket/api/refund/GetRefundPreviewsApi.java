package com.tiket.api.refund;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

/**
 * User-facing refund preview call:
 * {@code GET /ms-gateway/tix-refund/external/refunds/previews?orderId&orderDetailId&pageId=detail}.
 *
 * <p>The first call of the storefront "my-refund" journey. It tells whether the order detail is refundable
 * ({@code data.refundable.refundable.value}) and lists the refund reasons the user can pick
 * ({@code data.reasons[].id}). Authenticated with the <b>user</b> access token.
 */
public record GetRefundPreviewsApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            long orderId,
            long orderDetailId
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://gatotkaca.tiket.com/ms-gateway", "https://preprod.tiket.com/ms-gateway");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/refund/RefundExternal_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("orderId", inputs.orderId);
        queryParams.put("orderDetailId", inputs.orderDetailId);
        queryParams.put("pageId", "detail");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("tix-refund/external/refunds/previews"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
