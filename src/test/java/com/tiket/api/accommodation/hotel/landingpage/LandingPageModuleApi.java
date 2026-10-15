package com.tiket.api.accommodation.hotel.landingpage;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record LandingPageModuleApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String username,
            String userAgent,
            String language,
            String templateCode,
            String masterContentId
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/AccommodationLandingPage_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("username", inputs.username);
        headers.put("User-Agent", inputs.userAgent);
        headers.put("lang", inputs.language);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-hotel-home-api/landing-page/module/" + inputs.masterContentId))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
