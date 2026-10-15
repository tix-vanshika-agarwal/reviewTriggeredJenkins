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

public record HotelBookApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            int hotelId,
            int roomId,
            int ratePlanId,
            String startDate,
            int night,
            int room,
            int adult,
            List<Integer> childAges,
            String currency,
            String email,
            String firstName,
            String lastName,
            String salutation,
            String phone,
            long totalPrice
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/HotelBookApi_headers.properties");
        headers.put("currency", inputs.currency());

        JSONObject body = new JSONObject();
        body.put("bookingAddOnRequestList", new JSONArray());

        JSONObject guestInfo = new JSONObject();
        guestInfo.put("bedRequest", JSONObject.NULL);
        guestInfo.put("countryId", "ID");
        guestInfo.put("email", inputs.email());
        guestInfo.put("phone", inputs.phone());
        guestInfo.put("smokingRoom", 0);
        guestInfo.put("specialRequest", "Automation Test Booking");

        JSONArray guestInfoDetails = new JSONArray();
        JSONObject primaryGuest = new JSONObject();
        primaryGuest.put("firstName", inputs.firstName());
        primaryGuest.put("guestNumber", inputs.adult());
        primaryGuest.put("lastName", inputs.lastName());
        primaryGuest.put("salutation", inputs.salutation());
        guestInfoDetails.put(primaryGuest);

        guestInfo.put("guestInfoDetails", guestInfoDetails);
        body.put("guestInfo", guestInfo);

        body.put("hotelId", inputs.hotelId());
        body.put("roomId", inputs.roomId());
        body.put("ratePlanId", inputs.ratePlanId());
        body.put("startDate", inputs.startDate());
        body.put("night", inputs.night());
        body.put("room", inputs.room());
        body.put("adult", inputs.adult());
        body.put("childAges", new JSONArray(inputs.childAges()));
        body.put("totalPrice", inputs.totalPrice());

        String baseUrl = "https://hotel-native-booking-be-svc.preprod-hotel-cluster.tiket.com";
        String endpoint = "/tix-hotel-native-api/v2/hotel-api/book";

        ApiRequest request = new ApiRequest.Builder()
                .setUri(baseUrl + endpoint)
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}