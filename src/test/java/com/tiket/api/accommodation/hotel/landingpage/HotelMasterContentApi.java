package com.tiket.api.accommodation.hotel.landingpage;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record HotelMasterContentApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            boolean login,
            String accessToken,
            String username,
            String userAgent,
            String language,
            String templateCode
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/AccommodationLandingPage_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("username", inputs.username);
        headers.put("User-Agent", inputs.userAgent);
        headers.put("lang", inputs.language);

        JSONObject body = getStaticJson("src/test/resources/api/accommodation/AccommodationLandingPage_body.json");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-hotel-home-api/landing-page/get-master-content"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
