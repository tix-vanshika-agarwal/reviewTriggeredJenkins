package com.tiket.api.member;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GetSmartProfileApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken
    ){}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/member/smartProfile_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-profile-core/profiles?showStatus=false"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}