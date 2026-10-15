package com.tiket.api;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

public record HitGetUrl(Inputs input) implements BaseApi{

    public record Inputs(String url){}


    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper baseUrl = new UriHelper(input.url);
        ApiRequest request = new ApiRequest.Builder()
                .setUri(baseUrl.getUri())
                .build();
        return apiTools.get(request);
    }
    
}
