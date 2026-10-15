package com.tiket.api.b2b.affiliate;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record DeleteAffiliateOrderApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String transactionId,
            String orderID
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/Affiliate/DeleteAffiliateOrderApi_headers.properties");

        UriHelper helper = new UriHelper("https://deposit-be-http-svc.test-platform-cluster.tiket.com", "https://deposit-be-http-svc.preprod-platform-cluster.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-deposit/b2b/affiliates/transaction/" + inputs.transactionId + "/" + inputs.orderID))
                .setHeaderParams(headers)
                .build();

        return apiTools.delete(request);
    }
}
