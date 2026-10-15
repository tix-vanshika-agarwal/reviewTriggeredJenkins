package com.tiket.api.utilities;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.util.Map;

public record PromoListApi(Inputs inputs) implements BaseApi{
    public record Inputs(
            String accessToken,
            String language
    ){}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/utilities/PromoList_Page/headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("Accept-Language", inputs.language);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/ms-gateway/tix-promotion-page/promos"))
                .setHeaderParams(headers)
                .build();
        return apiTools.get(request);
    }
}
