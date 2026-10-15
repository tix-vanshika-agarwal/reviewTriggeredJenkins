package com.tiket.api.bus;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.bus.BusCheckoutBody;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record BusCheckoutApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            String cartId,
            String cartSecret,
            String fullName,
            String contactPhone,
            String contactEmailAddress,
            String contactProfileId,
            int totalPrice,
            String selectedSeatsName
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/PayExpress_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/bus/BusCheckout_queryParams.properties");
        queryParams.put("cartSecret", inputs.cartSecret);

        JSONObject jsonObject = getStaticJson("src/test/resources/api/bus/BusCheckout_body.json");
        Gson gson = new Gson();
        BusCheckoutBody busCheckoutBody = gson.fromJson(jsonObject.toString(), BusCheckoutBody.class);
        busCheckoutBody.contact.fullName = inputs.fullName;
        busCheckoutBody.contact.phone = inputs.contactPhone;
        busCheckoutBody.contact.emailAddress = inputs.contactEmailAddress;
        busCheckoutBody.contact.profileId = inputs.contactProfileId;
        busCheckoutBody.passengers.get(0).fullName = inputs.fullName;
        busCheckoutBody.passengers.get(0).profileId = inputs.contactProfileId;
        busCheckoutBody.totalPrice = inputs.totalPrice;
        busCheckoutBody.schedules.depart.selectedSeats.get(0).name = inputs.selectedSeatsName;

        ApiRequest apiRequest = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-bus-demand/v2/carts/" + inputs.cartId))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .setBody(gson.toJson(busCheckoutBody, BusCheckoutBody.class))
                .build();
        return apiTools.post(apiRequest);
    }
}
