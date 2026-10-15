package com.tiket.api.member;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GetAccountDetailsApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String paramType,
            String paramValue
    ){}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/member/accountDetails_headers.properties");

        UriHelper helper = new UriHelper("https://service.bliblitiket.tools","https://sandbox.eph.bliblitiket.tools");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/gks-unm-go-be/api/v1/entity/user-detail?"+inputs.paramType+"="+inputs.paramValue+""))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}