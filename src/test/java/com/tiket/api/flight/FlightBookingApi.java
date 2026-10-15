package com.tiket.api.flight;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record FlightBookingApi (Inputs inputs) implements BaseApi {
    public record Inputs(
            String cartID,
            String email, 
            String accessToken, 
            String firstName,
            String lastName,
            String phone,
            String code,
            Double price
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/FlightCart_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        JSONObject body = getStaticJson("src/test/resources/api/flight/FlightBooking_body.json");
        body.put("cartId", inputs.cartID);
        body.getJSONObject("contact").put("email", inputs.email);
        body.getJSONObject("contact").put("fullName", inputs.firstName+" "+inputs.lastName);
        body.getJSONObject("contact").put("phone", inputs.phone);
        body.getJSONArray("adults").getJSONObject(0).put("firstName", inputs.firstName);
        body.getJSONArray("adults").getJSONObject(0).put("lastName", inputs.lastName);
        body.getJSONArray("multiInsurances").getJSONObject(0).put("code", inputs.code);
        body.getJSONArray("multiInsurances").getJSONObject(0).put("fare", inputs.price);
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-flight-core/v6/booking"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
    
}
