package com.tiket.api.lambdatest;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public record GETLambdaTestTunnelsApi(Inputs inputs) implements BaseApi {
    
    public record Inputs(
            String username,
            String accessKey
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        
        // Create Basic Auth header
        String auth = inputs.username + ":" + inputs.accessKey;
        String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
        
        Map<String, Object> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("Authorization", "Basic " + encodedAuth);

        // Use full URL in setUri (not separate baseUrl + uri)
        ApiRequest request = new ApiRequest.Builder()
                .setUri("https://api.lambdatest.com/automation/api/v1/tunnels")
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}

