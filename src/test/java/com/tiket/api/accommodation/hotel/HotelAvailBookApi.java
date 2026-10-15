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

public record HotelAvailBookApi(Inputs inputs) implements BaseApi {

    public record Inputs(int totalPrice, int ratePlanId, int night, int hotelId, List<Integer> childAges, int adult, int roomId, String startDate, int room, String firstName, String lastName, String salutation, String phone, String email, String countryId, String currency) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/HotelAvailBookApi_headers.properties");
        headers.put("currency", inputs.currency);

        JSONArray guestInfoDetails = new JSONArray();
        JSONObject guestDetail = new JSONObject();
        guestDetail.put("firstName", inputs.firstName);
        guestDetail.put("lastName", inputs.lastName);
        guestDetail.put("salutation", inputs.salutation);
        guestDetail.put("guestNumber", 1);
        guestInfoDetails.put(guestDetail);

        JSONObject guestInfo = new JSONObject();
        guestInfo.put("bedRequest", JSONObject.NULL);
        guestInfo.put("phone", inputs.phone);
        guestInfo.put("smokingRoom", 0);
        guestInfo.put("specialRequest", "");
        guestInfo.put("guestInfoDetails", guestInfoDetails);
        guestInfo.put("countryId", inputs.countryId);
        guestInfo.put("email", inputs.email);

        JSONObject body = new JSONObject();
        body.put("totalPrice", inputs.totalPrice);
        body.put("bookingAddOnRequestList", new JSONArray());
        body.put("ratePlanId", inputs.ratePlanId);
        body.put("night", inputs.night);
        body.put("hotelId", inputs.hotelId);
        body.put("childAges", new JSONArray(inputs.childAges));
        body.put("guestInfo", guestInfo);
        body.put("adult", inputs.adult);
        body.put("roomId", inputs.roomId);
        body.put("startDate", inputs.startDate);
        body.put("room", inputs.room);

        String baseUrl = "https://hotel-native-core-api-svc.preprod-hotel-cluster.tiket.com";
        String endpoint = "/tix-hotel-native-api/v2/avail/book";

        ApiRequest request = new ApiRequest.Builder()
                .setUri(baseUrl + endpoint)
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}

