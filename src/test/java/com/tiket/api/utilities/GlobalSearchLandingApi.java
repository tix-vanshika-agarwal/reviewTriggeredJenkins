package com.tiket.api.utilities;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.util.Map;

public record GlobalSearchLandingApi(Inputs inputs) implements BaseApi {
    public record Inputs(
        String platform,
        String accessToken,
        String lang
    ){}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/utilities/GS_Landing/headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("lang", inputs.lang);
        headers.put("Accept-Language", inputs.lang);
        headers.put("language", inputs.lang);
        headers.put("platform", inputs.platform);
        headers.put("User-Agent", inputs.platform.equalsIgnoreCase("android")? "tiketcom/android-version"  : "tiketcom/ios-version");
        headers.put("channelId", inputs.platform);


        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-home/v2/page-modules-full?pageModuleCode=GLOBAL_SEARCH_LANDING&pageModuleType=GLOBAL_SEARCH"))
                .setHeaderParams(headers)
                .build();
        return apiTools.get(request);
    }
}
