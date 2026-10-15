package com.tiket.api.accommodation;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.accommodation.master_tag.AccommodationMasterTagBody;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record AccommodationMasterTagApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String username,
            String lang,
            String accommodationType,
            String locationId,
            String locationType
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/master_tag/AccommodationMasterTag_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("username", inputs.username);
        headers.put("lang", inputs.lang);

        JSONObject body = getStaticJson("src/test/resources/api/accommodation/master_tag/AccommodationMasterTag_body.json");

        Gson gson = new Gson();
        AccommodationMasterTagBody accommodationMasterTagBody = gson.fromJson(body.toString(), AccommodationMasterTagBody.class);
        accommodationMasterTagBody.accommodationType = inputs.accommodationType;
        accommodationMasterTagBody.locationId = inputs.locationId;
        accommodationMasterTagBody.locationType = inputs.locationType;

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-hotel-search/v3/group-filter"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(accommodationMasterTagBody, AccommodationMasterTagBody.class))
                .build();

        return apiTools.post(request);
    }
}
