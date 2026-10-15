package com.tiket.api.accommodation.hotel;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record HotelSearchApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String username,
            int adult,
            int night,
            int room,
            String searchType,
            String searchValue,
            String startDate,
            String groupFilterKeyword,
            String accommodationType,
            String userAgent,
            String currency
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/HotelSearch_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("username", inputs.username);
        headers.put("User-Agent", inputs.userAgent);
        headers.put("X-Currency", inputs.currency);
        if (inputs.currency.isEmpty()) headers.put("X-Currency", "IDR");

        JSONObject body = getStaticJson("src/test/resources/api/accommodation/hotel/HotelSearch_body.json");
        body.put("adult", inputs.adult);
        body.put("night", inputs.night);
        body.put("room", inputs.room);
        body.put("searchType", inputs.searchType);
        body.put("searchValue", inputs.searchValue);
        body.put("startDate", inputs.startDate);
        body.put("groupFilterKeyword", inputs.groupFilterKeyword);
        body.put("accommodationType", inputs.accommodationType);
        body.put("currency", inputs.currency);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-hotel-search/v4/search"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
