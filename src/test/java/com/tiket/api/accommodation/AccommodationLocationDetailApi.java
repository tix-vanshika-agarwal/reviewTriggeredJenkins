package com.tiket.api.accommodation;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record AccommodationLocationDetailApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String publicId,
            String accessToken,
            String locationType,
            String language
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/AccommodationLocationDetail_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        JSONObject body = getStaticJson("src/test/resources/api/accommodation/AccommodationLocationDetail_body.json");
        body.getJSONObject("query")
                .getJSONObject("bool")
                .getJSONArray("must")
                .getJSONObject(0)
                .getJSONObject("match_phrase")
                .put("publicId", inputs.publicId);

        String uriLocation = inputs.locationType.toLowerCase();

        //This is the ES URL, please contact Accommodation SQA PIC for more detail
        UriHelper helper = new UriHelper("http://192.168.60.44:9200");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("hotel_location_" + uriLocation + "_v4/_search"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
