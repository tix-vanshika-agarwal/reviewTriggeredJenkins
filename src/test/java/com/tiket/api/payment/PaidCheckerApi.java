package com.tiket.api.payment;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record PaidCheckerApi(Inputs inputs) implements BaseApi{
    
    public record Inputs(
        String referenceId
    ) {}
    
    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/payment/PaidChecker_headers.properties");

        UriHelper baseUrl = new UriHelper("https://payment-callback-be-svc.test-payment-cluster.tiket.com","https://payment-callback-be-svc.preprod-payment-cluster.tiket.com");
        ApiRequest apiRequest = new ApiRequest.Builder()
                .setUri(baseUrl.withEndpoint("tix-payment-callback/payment/paid-checker/"+inputs.referenceId))
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(apiRequest);
    }
}
