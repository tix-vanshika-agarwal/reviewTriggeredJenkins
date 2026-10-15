package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public record TTDSupplierSyncStatusApi(Inputs inputs) implements BaseApi {
    
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36";
    
    public record Inputs(
            String accessToken,
            String productId
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper uriHelper = new UriHelper(
                "https://gatotkaca.tiket.com",
                "https://prod.tiket.com",
                "https://preprod.tiket.com"
        );
        
        Map<String, Object> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("accept-language", "en-GB,en;q=0.9");
        headers.put("channelid", "WEB");
        headers.put("currency", "IDR");
        headers.put("islogin", "true");
        headers.put("language", "ID");
        headers.put("requestid", "23123123");
        headers.put("serviceid", "gateway");
        headers.put("storeid", "TIKETCOM");
        headers.put("user-agent", USER_AGENT);
        headers.put("useragent", USER_AGENT);
        headers.put("username", "username");
        headers.put("Authorization", inputs.accessToken);
        
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/tix-api-gateway-dashboard-event/tix-events-v2-inventory/admins/products/" + inputs.productId))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}