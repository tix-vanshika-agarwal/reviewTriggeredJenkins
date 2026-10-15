package com.tiket.api.refund;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import java.util.Map;

//GK only
public record  GETRefundAbleStatus(Inputs inputs) implements BaseApi {

    public record Inputs(
        String username,
        String orderId,
        String orderDetailId,
        String accountId
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/refund/GETRefundAbleStatus_headers.properties");
        headers.put("X-Username", inputs.username);
        headers.put("X-Account-Id", inputs.accountId);

        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://lb1-ms.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
            .setUri(helper.withEndpoint("/tix-refund/external/refundable/"+inputs.orderId+"/"+inputs.orderDetailId+""))
            .setHeaderParams(headers)
            .build();

        return apiTools.get(request);
    }
    
}
