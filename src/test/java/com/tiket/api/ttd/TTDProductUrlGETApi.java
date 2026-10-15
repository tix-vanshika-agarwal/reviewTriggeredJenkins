package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record TTDProductUrlGETApi(Inputs inputs) implements BaseApi {
    
    public record Inputs(
            String productUrl,
            String acceptHeader,
            String userAgent,
            String cookie
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        
        Map<String, Object> headers = getStaticMap("src/test/resources/api/ttd/TTDProductUrlGETApi/TTDProductUrlGETApi_headers.properties");
        
        // Override with dynamic values if provided
        if (inputs.acceptHeader != null) {
            headers.put("accept", inputs.acceptHeader);
        }
        if (inputs.userAgent != null) {
            headers.put("useragent", inputs.userAgent);
        }
        if (inputs.cookie != null) {
            headers.put("Cookie", inputs.cookie);
        }

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-events-v2-inventory/v1/products/url/" + inputs.productUrl))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}