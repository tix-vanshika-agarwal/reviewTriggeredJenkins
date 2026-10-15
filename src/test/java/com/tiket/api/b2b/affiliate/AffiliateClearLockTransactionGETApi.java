package com.tiket.api.b2b.affiliate;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record AffiliateClearLockTransactionGETApi() implements BaseApi {
    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/Affiliate/AffiliateClearLockTransactionsGETApi_headers.properties");

        UriHelper helper = new UriHelper("https://deposit-be-http-svc.test-platform-cluster.tiket.com", "https://deposit-be-http-svc.preprod-platform-cluster.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-deposit/clear-lock-transaction"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
