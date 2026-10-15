package com.tiket.api.accommodation.hotel;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public record HotelAvailabilityApi(Inputs inputs) implements BaseApi {

    public record Inputs(int adult, List<Integer> childAges, List<Integer> hotelIds, int night, int room, String startDate, String currency) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/HotelAvailabilityApi_headers.properties");
        headers.put("currency", inputs.currency);

        JSONObject body = new JSONObject();
        body.put("adult", inputs.adult);
        body.put("childAges", new JSONArray(inputs.childAges));
        body.put("hotelIds", new JSONArray(inputs.hotelIds));
        body.put("night", inputs.night);
        body.put("room", inputs.room);
        body.put("startDate", inputs.startDate);
        body.put("useErrorMapping", true);

        String baseUrl = "https://hotel-native-core-api-svc.preprod-hotel-cluster.tiket.com";
        String endpoint = "/tix-hotel-native-api/v2/availability";

        ApiRequest request = new ApiRequest.Builder()
                .setUri(baseUrl + endpoint)
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}

