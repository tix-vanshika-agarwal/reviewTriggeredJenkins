package com.tiket.api.payment;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record PaymentCheckerApi() implements BaseApi {

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/payment/PaymentChecker_headers.properties");

        UriHelper helper = new UriHelper("https://payment-callback-be-svc.test-payment-cluster.tiket.com","https://payment-callback-be-svc.preprod-payment-cluster.tiket.com");
        ApiRequest apiRequest = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("tix-payment-callback/payment/paymentChecker?paymentSource=JATIS"))
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(apiRequest);
    }
}
