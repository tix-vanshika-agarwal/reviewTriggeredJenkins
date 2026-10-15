package com.tiket.api.loyalty;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.UUID;

public record PointHistoryApi(Inputs input) implements BaseApi {
    public record Inputs(long accountId, String currency, String acceptLanguage) {
        public Inputs(long accountId) {
            this(accountId, "IDR", "id");
        }
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(
                "https://loyalty-core-be-svc.testing-platform-cluster.tiket.com",
                "https://loyalty-core-be-svc.preprod-platform-cluster.tiket.com"
        );

        Map<String, Object> headers = getStaticMap("src/test/resources/api/loyalty/PointHistoryApi_headers.properties");
        headers.put("X-Request-Id", UUID.randomUUID().toString());
        headers.put("X-Currency", input.currency());
        headers.put("Accept-Language", input.acceptLanguage());

        ApiRequest req = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(
                        "/tix-loyalty-core/admin-cs/ulp/point-history?page=0&size=100&accountId=" + input.accountId()))
                .setHeaderParams(headers)
                .build();
        return apiTools.get(req);
    }
}
