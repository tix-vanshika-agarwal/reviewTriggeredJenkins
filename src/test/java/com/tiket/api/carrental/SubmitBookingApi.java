package com.tiket.api.carrental;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.carrental.SubmitBookingBody;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record SubmitBookingApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String carRegionalId,
            String email,
            String phoneNumber,
            String driverOption,
            String location,
            String locationId,
            String startDate,
            String pickupTime,
            String type,
            String fullName,
            String nameTitle,
            int rentalDuration
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/carrental/SubmitBooking_headers.properties");
        headers.put("Username", inputs.email);
        headers.put("Authorization", inputs.accessToken);

        JSONObject body = getStaticJson("src/test/resources/api/carrental/SubmitBooking_body.json");
        Gson gson = new Gson();
        SubmitBookingBody submitBookingBody = gson.fromJson(body.toString(), SubmitBookingBody.class);

        submitBookingBody.carRegionalId = inputs.carRegionalId;
        submitBookingBody.driverOption = inputs.driverOption;
        submitBookingBody.startDate = inputs.startDate;
        submitBookingBody.pickupTime = inputs.pickupTime;
        submitBookingBody.type = inputs.type;

        submitBookingBody.contact.phone = inputs.phoneNumber;
        submitBookingBody.contact.fullName = inputs.fullName;
        submitBookingBody.contact.title = inputs.nameTitle;
        submitBookingBody.contact.email = inputs.email;

        submitBookingBody.passengers.phone = inputs.phoneNumber;
        submitBookingBody.passengers.fullName = inputs.fullName;
        submitBookingBody.passengers.title = inputs.nameTitle;
        submitBookingBody.passengers.email = inputs.email;

        submitBookingBody.zones.dropOff.address = inputs.location;
        submitBookingBody.zones.dropOff.id = inputs.locationId;
        submitBookingBody.zones.pickup.address = inputs.location;
        submitBookingBody.zones.pickup.id = inputs.locationId;
        submitBookingBody.totalDays = inputs.rentalDuration;

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-car-vendor/v2/order/book"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(submitBookingBody, SubmitBookingBody.class))
                .build();

        return apiTools.post(request);
    }
}