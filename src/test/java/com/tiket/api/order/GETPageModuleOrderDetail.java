package com.tiket.api.order;


import com.tiket.io.api.ApiTools;
import com.tiket.model.Platform;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;

import java.util.Map;

public record GETPageModuleOrderDetail(Inputs inputs) implements BaseApi {

    public record Inputs(    
        String accessToken,
        String orderId,
        String orderHash,
        String lang,
        String currency
        ) {
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/order/PageModuleOrderDetail.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("lang", inputs.lang.toLowerCase());
        headers.put("Accept-Language", inputs.lang.toLowerCase());
        headers.put("X-Currency", inputs.currency);
        headers.put("language", inputs.lang.toLowerCase());
        headers.put("currency", inputs.currency);
        headers.put("User-Agent", Configs.PLATFORM == Platform.ANDROID ? "tiketcom/android-version (twh:20296642) - v4.75.2-uat-HEAD" : "tiketcom/ios-version (twh:20420882) - v4.75.0");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-home/v2/page-modules-full?pageModuleCode=ORDER_DETAIL&orderId="+inputs.orderId+"&orderHash="+inputs.orderHash+""))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
