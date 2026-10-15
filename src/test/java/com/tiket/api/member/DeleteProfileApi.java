package com.tiket.api.member;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record DeleteProfileApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            long accountId,
            long profileId
    ){}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/member/smartProfile_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-profile-core/profiles?id="+inputs.profileId+"&accountId="+inputs.accountId))
                .setHeaderParams(headers)
                .build();

        return apiTools.delete(request);
    }
}