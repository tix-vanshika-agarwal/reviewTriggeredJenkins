package com.tiket.api.accommodation.hotel;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

/**
 * GET /tix-api-gateway-dashboard-hotel/tix-hotel-native-core/v2/session/change-hotel?hotelId={hotelId}
 *
 * Binds the given hotel to the current rates-and-availability dashboard session.
 * This must be called after the Extranet admin login and before any hotel-scoped
 * write (e.g. the allotments update), otherwise those calls return HTTP 401.
 *
 * Uses the same host as {@link HotelAllotmentsApi} so both requests share the
 * same dashboard session.
 */
public record ChangeHotelSessionApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String authorization,
            String hotelId
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://tix-gatotkaca.tiket.com", "https://tix.preprod.tiket.com");

        Map<String, Object> headers = new HashMap<>();
        headers.put("lang", "en");
        headers.put("Authorization", inputs.authorization());

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("hotelId", inputs.hotelId());

        String endpoint = "/tix-api-gateway-dashboard-hotel/tix-hotel-native-core/v2/session/change-hotel";

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
