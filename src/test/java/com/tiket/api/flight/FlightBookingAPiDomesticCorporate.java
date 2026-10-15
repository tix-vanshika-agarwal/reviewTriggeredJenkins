package com.tiket.api.flight;

import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import static com.tiket.core.CoreFunctions.log;

public record FlightBookingAPiDomesticCorporate(Inputs inputs) implements BaseApi {
    public record Inputs(
            String cartID,
            String email,
            String accessToken,
            String[] passengerNames,
            String phone,
            String corporateInterimId
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/FlightCart_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        JSONObject body = getStaticJson("src/test/resources/api/flight/FlightBookingDomesticCorporate_body.json");
        body.put("cartId", inputs.cartID);
        body.getJSONObject("contact").put("email", inputs.email);
        body.getJSONObject("contact").put("fullName", inputs.passengerNames[0]);
        body.getJSONObject("contact").put("phone", inputs.phone);
        JSONObject adultTemplate = body.getJSONArray("adults").getJSONObject(0);
        JSONArray adults = new JSONArray();
        for(int i=0; i<inputs.passengerNames.length; i++){
            JSONObject adult = new JSONObject(adultTemplate.toString()); // Copy the template
            adult.put("fullName", inputs.passengerNames[i]);
            adult.put("identityNumber", String.valueOf(CoreFunctions.randomInt(16)));
            // Add the modified adult to the new array
            adults.put(adult);
        }
        body.getJSONObject("corporateDataRequest").put("corporateInterimId",inputs.corporateInterimId);
        body.put("adults", adults);
        log("Request body: " + body.toString());
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-flight-core/v7/booking"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
