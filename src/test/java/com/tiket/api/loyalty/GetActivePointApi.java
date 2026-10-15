package com.tiket.api.loyalty;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GetActivePointApi (Inputs inputs) implements BaseApi {

    public record Inputs(
            String searchBy,
            String email,
            String accessToken
    ){}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/loyalty/GetActivePointApi_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("Referer", uriHelper.getUri()+"admin/care/loyalty/bliblitiketpoint");

        Map<String,Object> queryParms = getStaticMap("src/test/resources/api/loyalty/GetActivePointApi_queryParams.properties");
        queryParms.put("value",inputs.email);
        queryParms.put("by",inputs.searchBy);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-loyalty-core/admin-cs/ulp/search-user"))
                .setHeaderParams(headers)
                .setQueryParams(queryParms)
                .build();

        return apiTools.get(request);
    }
}
