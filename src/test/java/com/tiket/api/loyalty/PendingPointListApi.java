package com.tiket.api.loyalty;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public record PendingPointListApi(Inputs input) implements BaseApi {
    public record Inputs(long accountId) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper(
                "https://loyalty-core-be-svc.testing-platform-cluster.tiket.com",
                "https://loyalty-core-be-svc.preprod-platform-cluster.tiket.com"
        );

        Map<String, Object> headers = new HashMap<>();
        headers.put("accept", "*/*");
        headers.put("X-Channel-Id", "MICROSERVICE");
        headers.put("X-Store-Id", "TIKETCOM");
        headers.put("X-Service-Id", "loyalty");
        headers.put("X-Request-Id", "automation");
        headers.put("X-Username", "ms-loyalty@tiket.com");

        ApiRequest req = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(
                        "/tix-loyalty-core/admin-cs/ulp/pending-list?page=0&size=100&accountId=" + input.accountId()))
                .setHeaderParams(headers)
                .build();
        return apiTools.get(req);
    }
}
