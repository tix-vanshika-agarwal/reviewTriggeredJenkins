package com.tiket.api.member;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GetAccountDataUsingEmailApi (Inputs inputs) implements BaseApi {
    public record Inputs(
            String email
    ){}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/member/GetAccountDataUsingEmailApi_headers.properties");
        headers.put("X-Username", inputs.email);

        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://member-core-v2-be-svc.preprod-platform-cluster.tiket.com");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-member-core/v2/account"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
