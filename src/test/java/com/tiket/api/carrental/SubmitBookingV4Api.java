package com.tiket.api.carrental;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.carrental.SubmitBookingV4Body;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record SubmitBookingV4Api(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String carRegionalId,
            String email,
            String phoneNumber,
            int profileId,
            String driverOption,
            String location,
            String locationId,
            String latitude,
            String longitude,
            String placeId,
            String startDate,
            String pickupTime,
            String type,
            String fullName,
            String nameTitle,
            int rentalDuration,
            int totalPrice
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/carrental/SubmitBooking_headers.properties");
        headers.put("Username", inputs.email);
        headers.put("Authorization", inputs.accessToken);

        JSONObject body = getStaticJson("src/test/resources/api/carrental/SubmitBookingV4_body.json");
        Gson gson = new Gson();
        SubmitBookingV4Body submitBookingBody = gson.fromJson(body.toString(), SubmitBookingV4Body.class);

        submitBookingBody.carRegionalId = inputs.carRegionalId;
        submitBookingBody.driverOption = inputs.driverOption;
        submitBookingBody.startDate = inputs.startDate;
        submitBookingBody.pickupTime = inputs.pickupTime;
        submitBookingBody.type = inputs.type;
        submitBookingBody.totalPrice = inputs.totalPrice;
        submitBookingBody.totalDays = inputs.rentalDuration;

        submitBookingBody.contact.profileId = String.valueOf(inputs.profileId);
        submitBookingBody.contact.phone = inputs.phoneNumber;
        submitBookingBody.contact.fullName = inputs.fullName;
        submitBookingBody.contact.title = inputs.nameTitle;
        submitBookingBody.contact.email = inputs.email;

        submitBookingBody.passengers.profileId = String.valueOf(inputs.profileId);
        submitBookingBody.passengers.phone = inputs.phoneNumber;
        submitBookingBody.passengers.fullName = inputs.fullName;
        submitBookingBody.passengers.title = inputs.nameTitle;

        submitBookingBody.zones.dropOff.address = inputs.location;
        submitBookingBody.zones.dropOff.id = inputs.locationId;
        submitBookingBody.zones.dropOff.latitude = inputs.latitude;
        submitBookingBody.zones.dropOff.longitude = inputs.longitude;
        submitBookingBody.zones.dropOff.placeId = inputs.placeId;

        submitBookingBody.zones.pickup.address = inputs.location;
        submitBookingBody.zones.pickup.id = inputs.locationId;
        submitBookingBody.zones.pickup.latitude = inputs.latitude;
        submitBookingBody.zones.pickup.longitude = inputs.longitude;
        submitBookingBody.zones.pickup.placeId = inputs.placeId;

        String requestBody = gson.toJson(submitBookingBody, SubmitBookingV4Body.class);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-car-vendor/v4/order/book"))
                .setHeaderParams(headers)
                .setBody(requestBody)
                .build();

        return apiTools.post(request);
    }
}