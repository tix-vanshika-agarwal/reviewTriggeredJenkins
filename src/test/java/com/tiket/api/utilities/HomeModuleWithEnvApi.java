package com.tiket.api.utilities;

import com.tiket.io.api.ApiTools;
import com.tiket.model.Environment;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.util.Map;

public record HomeModuleWithEnvApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String platform,
            String accessToken,
            String language,
            String currency,
            Environment environment
    ){}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getHeaders();

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/utilities/HomeModules_queryParams.properties");
        queryParams.put("availablePlatforms",inputs.platform.toUpperCase());

        String urlGK =  "https://gatotkaca.tiket.com";
        String urlPreProd = "https://preprod.tiket.com";
        String urlProd = "https://api.tiket.com";

        String url = switch (inputs.environment) {
            case GK -> urlGK;
            case PREPROD -> urlPreProd;
            case PROD -> urlProd;
        };
        url += "/ms-gateway/tix-home/v2/home-pages";

        ApiRequest request = new ApiRequest.Builder()
                .setUri(url)
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();
        return apiTools.get(request);
    }

    private Map<String, Object> getHeaders() {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/utilities/HomesModules_headers.properties");
        headers.put("authorization", inputs.accessToken);
        headers.put("Accept-Language", inputs.language);
        headers.put("currency", inputs.currency);

        String twh = inputs.platform.equalsIgnoreCase("ios") ? "20420882" : "20296642";
        String version = "v5.4.0"; // You can make this dynamic as needed
        String userAgent = String.format("tiketcom/%s-version (twh:%s) - %s", inputs.platform.toLowerCase(), twh, version);
        headers.put("User-Agent", userAgent);
        return headers;
    }
}

