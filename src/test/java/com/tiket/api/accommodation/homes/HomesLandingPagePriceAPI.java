package com.tiket.api.accommodation.homes;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record HomesLandingPagePriceAPI(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String userAgent,
            String language,
            String latitude,
            String longitude,
            String currency
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/homes/NHALandingPage_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("User-Agent", inputs.userAgent);
        headers.put("lang", inputs.language);
        headers.put("currency", inputs.currency);

        String uri = String.format("ms-gateway/tix-home/v2/page-modules-full?pageModuleCode=homes_landingPage&variant=homes_landingPage&taxDisplay=abt&accommPriceBeforeTax=true&latitude=%s&longitude=%s", inputs.latitude, inputs.longitude);
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint(uri))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}