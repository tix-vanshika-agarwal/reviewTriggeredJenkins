package com.tiket.api.b2b;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public record GetEmployeeIdApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String email
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://lb1-ms.preprod.tiket.com");
        Map<String, Object> headers = new HashMap<>();
        
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("tix-corporate/auth/corporate/check?email="+inputs.email))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }

}
