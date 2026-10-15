package com.tiket.api.accommodation.homes;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record HomesAutocompleteApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String username,
            String query
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/HotelAutocomplete_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("username", inputs.username);

        JSONObject body = getStaticJson("src/test/resources/api/accommodation/homes/HomesAutocomplete_body.json");
        body.put("query", inputs.query);

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/accommodation/hotel/HotelAutocomplete_queryParams.properties");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-hotel-autocomplete/v3/autocomplete"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
