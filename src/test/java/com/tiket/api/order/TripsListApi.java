package com.tiket.api.order;

import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.util.Map;

/**
 * Hits the your-order "trips" endpoint ({@code GET /ms-gateway/tix-order-composer/yourorder/v1/trips}),
 * which backs the itinerary/"Trips" tab of the your-order page ({@code /yourorder?tab=itinerary}).
 */
public record TripsListApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String deviceId,
            String currency,
            String countryCode,
            String lang,
            String clientOs
    ) {
        public Inputs(String accessToken) {
            this(accessToken, null, "IDR", "id", "en", "ANDROID");
        }
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        String token = inputs.accessToken().replace("Bearer ", "");
        String deviceId = inputs.deviceId() != null && !inputs.deviceId().isBlank()
                ? inputs.deviceId()
                : CoreFunctions.generateDeviceId();
        String countryCode = inputs.countryCode().toLowerCase();
        String lang = inputs.lang().toLowerCase();
        String currency = inputs.currency().toUpperCase();
        String requestId = CoreFunctions.generateDeviceId();

        Map<String, Object> headers = getStaticMap("src/test/resources/api/order/TripsListApi_headers.properties");
        headers.put("accept-language", lang);
        headers.put("countrycode", countryCode);
        headers.put("currency", currency);
        headers.put("deviceid", deviceId);
        headers.put("lang", lang);
        headers.put("referer", uriHelper.withEndpoint(lang + "-" + countryCode + "/yourorder?tab=itinerary"));
        headers.put("x-client-os", inputs.clientOs().toUpperCase());
        headers.put("x-correlation-id", requestId);
        headers.put("x-country-code", countryCode);
        headers.put("x-country-id", countryCode);
        headers.put("x-currency", currency);
        headers.put("x-request-id", requestId);
        headers.put("cookie", "device_id=" + deviceId
                + "; session_access_token=" + token
                + "; userlang=" + lang
                + "; tiket_currency=" + currency
                + "; uniqueId=" + deviceId + ";");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-order-composer/yourorder/v1/trips"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
