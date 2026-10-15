package com.tiket.api.refund;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GETSvcRefundsPaymentMethod(Inputs inputs) implements BaseApi {

    public record Inputs(
            String orderId,
            String orderDetailId
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://lb1-ms.preprod.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/refund/GETSvcRefundsPaymentMethod_headers.properties");

        String endpoint = "/tix-refund/svc/refunds/payment-method/"+inputs.orderId+"/"+inputs.orderDetailId+"";
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }

}