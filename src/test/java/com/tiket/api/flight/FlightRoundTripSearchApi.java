package com.tiket.api.flight;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;

import java.util.Map;

public record FlightRoundTripSearchApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String origin,
            String destination,
            String originType,
            String destinationType,
            String departureDate,
            String returnDate,
            int adult,
            int child,
            int infant,
            String searchType,
            String authorization,
            String currency,
            String deviceId
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://gatotkaca.tiket.com","https://preprod.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/FlightSearchApi_headers.properties");
        headers.put("Authorization",inputs.authorization);
        headers.put("Referer", helper.getUri()+"/pesawat/search");
        headers.put("X-Currency", inputs.currency);
        headers.put("currency", inputs.currency);
        headers.put("deviceId", inputs.deviceId);

        Map<String, Object> queryParams = getStringObjectMap();

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-flight-search/v3/search"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }

    private Map<String, Object> getStringObjectMap() {
        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/flight/FlightSearch_queryParams.properties");
        queryParams.put("origin",inputs.origin);
        queryParams.put("destination",inputs.destination);
        queryParams.put("departureDate",inputs.departureDate);
        queryParams.put("adult",inputs.adult);
        queryParams.put("child",inputs.child);
        queryParams.put("infant",inputs.infant);
        queryParams.put("searchType",inputs.searchType);
        queryParams.put("returnDate",inputs.returnDate);
        queryParams.put("originType",inputs.originType);
        queryParams.put("destinationType",inputs.destinationType);
        queryParams.put("resultType", "DEPARTURE");
        return queryParams;
    }
}
