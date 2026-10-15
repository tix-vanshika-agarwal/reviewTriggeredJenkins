package com.tiket.api.unm;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GETunmCountryListApi(Inputs inputs) implements BaseApi {
    public record Inputs(
    ) {
    }
    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/unm/GetProfileDetailApi_headers.properties");

        UriHelper helper = new UriHelper("https://service.bliblitiket.tools","https://sandbox.eph.bliblitiket.tools");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("gks-unm-go-be/api/v1/users/profile/country-list"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
