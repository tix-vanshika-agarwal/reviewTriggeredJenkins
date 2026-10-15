package com.tiket.api.flight;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

import static com.tiket.core.CoreFunctions.log;

/**
 * Flight Core booking v7 with a caller-supplied body.
 *
 * <p>Unlike {@link FlightBookingApiDomestic} and friends, which mutate a fixed JSON template and so
 * lock in one passenger shape, this takes the finished body. Build it from the cart's
 * {@code passengerFormSection} / {@code contactSegment}: the required fields differ per cart
 * (firstName/lastName versus fullName, identityNumber present or not), so no single template fits.
 * Not specific to any itinerary type.</p>
 */
public record FlightBookingV7Api(Inputs inputs) implements BaseApi {
    public record Inputs(
            String body,
            String accessToken
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/FlightCart_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        log("FlightBookingV7Api body: " + inputs.body);
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-flight-core/v7/booking"))
                .setHeaderParams(headers)
                .setBody(inputs.body)
                .build();

        return apiTools.post(request);
    }
}
