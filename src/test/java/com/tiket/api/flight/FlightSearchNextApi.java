package com.tiket.api.flight;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.flight.FlightStreamResponse;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;

import java.util.Map;

public record FlightSearchNextApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String origin,
            String destination,
            String originType,
            String destinationType,
            String departureDate,
            String returnDate,
            String flightId,
            String fareId,
            String supplierId,
            String authorization,
            FlightStreamResponse.DepartureFlight departureFlight,
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

        Map<String, Object> queryParams = getQueryParams();

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-flight-search/v3/search/next"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }

    private long roundPrice(double price) {
        return Math.round(price);
    }

    private Map<String, Object> getQueryParams() {
        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/flight/FlightSearchNext_queryParams.properties");
        queryParams.put("origin",inputs.origin);
        queryParams.put("destination",inputs.destination);
        queryParams.put("departureDate",inputs.departureDate);
        queryParams.put("returnDate",inputs.returnDate);
        queryParams.put("flightId",inputs.flightId);
        queryParams.put("fareId",inputs.fareId);
        queryParams.put("supplierId",inputs.supplierId);
        queryParams.put("originType",inputs.originType);
        queryParams.put("destinationType",inputs.destinationType);
        queryParams.put("adultPrice", roundPrice(inputs.departureFlight.fareDetail.fares.get(0).adultFare.totalWithoutAdjustment));
        queryParams.put("childPrice", roundPrice(inputs.departureFlight.fareDetail.fares.get(0).childFare.totalWithoutAdjustment));
        queryParams.put("infantPrice", roundPrice(inputs.departureFlight.fareDetail.fares.get(0).infantFare.totalWithoutAdjustment));
        queryParams.put("totalAdultPrice", roundPrice(inputs.departureFlight.fareDetail.fares.get(0).adultFare.total));
        queryParams.put("totalChildPrice", roundPrice(inputs.departureFlight.fareDetail.fares.get(0).childFare.total));
        queryParams.put("totalInfantPrice", roundPrice(inputs.departureFlight.fareDetail.fares.get(0).infantFare.total));
        queryParams.put("totalWithMarkupAdultPrice", roundPrice(inputs.departureFlight.fareDetail.fares.get(0).adultFare.totalWithMarkupWithoutDiscount));
        queryParams.put("totalWithMarkupChildPrice", roundPrice(inputs.departureFlight.fareDetail.fares.get(0).childFare.totalWithMarkupWithoutDiscount));
        queryParams.put("totalWithMarkupInfantPrice", roundPrice(inputs.departureFlight.fareDetail.fares.get(0).infantFare.totalWithMarkupWithoutDiscount));
        queryParams.put("roundTrip",inputs.departureFlight.roundTrip);
        return queryParams;
    }
}
