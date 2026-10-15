package com.tiket.api.order;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import java.util.Map;

public record InternalOrderDetailApi(Inputs inputs)  implements BaseApi{
     public record Inputs(
            String orderId
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
       Map<String, Object> headers = getStaticMap("src/test/resources/api/order/InternalOrderDetail_headers.properties");
        UriHelper uriHelper = new UriHelper("https://lb1-testing.tiket.com","https://lb1-ms.preprod.tiket.com");
        ApiRequest request = new ApiRequest.Builder()

                .setUri(uriHelper.withEndpoint("/tix-order-go/internal/order/"+inputs.orderId+"/details"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }

    
}
