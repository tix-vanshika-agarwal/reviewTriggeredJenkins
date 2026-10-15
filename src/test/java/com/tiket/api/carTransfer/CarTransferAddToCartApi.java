package com.tiket.api.carTransfer;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record CarTransferAddToCartApi (Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String catalogueItemId
    ) { }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/carTransfer/CarTransferAddToCart_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("referer", uriHelper.getUri() + "/airport-transfer/detail");
        headers.put("origin", uriHelper.getUri());


        JSONObject body = getStaticJson("src/test/resources/api/carTransfer/CarTransferAddToCart_body.json");
        body.put("catalogueItemId", inputs.catalogueItemId);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-airport-transfer-order/v4/carts"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}