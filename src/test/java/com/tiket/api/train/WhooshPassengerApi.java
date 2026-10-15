package com.tiket.api.train;

import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/** Stores the contact, the adult passengers and the infants (born a year ago) on a Whoosh cart; each gets a random identity number. */
public record WhooshPassengerApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            String cartId,
            String secret,
            String email,
            String phone,
            String[] adultNames,
            String[] infantNames
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/train/TrainAddToCart_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        JSONArray passengers = new JSONArray();
        for (String name : inputs.adultNames) {
            passengers.put(new JSONObject()
                    .put("paxType", "ADULT")
                    .put("title", "Mr")
                    .put("fullName", name)
                    .put("profileId", "")
                    .put("identityType", "identityNumber")
                    .put("identityNumber", "3171" + CoreFunctions.generateRandomNumeric(12))
                    .put("passportNumber", ""));
        }
        String birthDate = CoreFunctions.addToDateYYYY_MM_DD(CoreFunctions.getCurrentDateYYYY_MM_DD(), -365);
        for (String name : inputs.infantNames) {
            passengers.put(new JSONObject()
                    .put("paxType", "INFANT")
                    .put("title", "Mstr")
                    .put("fullName", name)
                    .put("birthDate", birthDate)
                    .put("profileId", "")
                    .put("identityType", "identityNumber")
                    .put("identityNumber", "3171" + CoreFunctions.generateRandomNumeric(12))
                    .put("passportNumber", ""));
        }
        JSONObject body = new JSONObject()
                .put("cartId", inputs.cartId)
                .put("secret", inputs.secret)
                .put("contact", new JSONObject()
                        .put("areaCode", "+62")
                        .put("emailAddress", inputs.email)
                        .put("fullName", inputs.adultNames[0])
                        .put("phone", inputs.phone)
                        .put("profileId", "")
                        .put("title", "Mr"))
                .put("passengers", passengers);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-train-trx/v6/whoosh/carts/" + inputs.cartId))
                .setQueryParams(Map.of("secret", inputs.secret))
                .setBody(body.toString())
                .setHeaderParams(headers)
                .build();
        return apiTools.post(request);
    }
}
