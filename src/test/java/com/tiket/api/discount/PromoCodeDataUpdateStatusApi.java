package com.tiket.api.discount;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record PromoCodeDataUpdateStatusApi(Inputs inputs) implements BaseApi {

    public record Inputs(
        String username,
        String accountId,
        String statusTo,
        String id
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/discount/PromoCodeDataUpdateStatus_headers.properties");
        headers.put("X-Username", inputs.username);
        headers.put("X-Account-Id", inputs.accountId);

        UriHelper uriHelper = new UriHelper("https://promocode-be-svc.test-misc-cluster.tiket.com","https://promocode-be-svc.preprod-misc-cluster.tiket.com");
        ApiRequest apiRequest = new ApiRequest.Builder()
            .setUri(uriHelper.withEndpoint(String.format("tix-promocode/promoCodeData/" + inputs.statusTo + "/" + inputs.id)))
            .setHeaderParams(headers)
            .build();

        return apiTools.put(apiRequest);
    }
}
