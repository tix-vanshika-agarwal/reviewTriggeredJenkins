package com.tiket.api.member;

import com.tiket.io.api.ApiTools;
import com.tiket.model.Environment;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;

import java.util.Map;

public record GetAccountDataApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken
    ){}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/member/GetAccountData_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        if(Configs.ENVIRONMENT == Environment.PREPROD)
            headers.put("Referer","https://preprod.tiket.com/myaccount");
        else
            headers.put("Referer","https://gatotkaca.tiket.com/myaccount");


        UriHelper helper = new UriHelper("https://gatotkaca.tiket.com","https://preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/ms-gateway/tix-member-core/v2/account"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
