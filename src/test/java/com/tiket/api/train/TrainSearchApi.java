package com.tiket.api.train;

import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public record TrainSearchApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String originCity,
            String destinationCity,
            String startDate,
            String tripType,
            int adultCount,
            int infantCount,
            String authorization,
            String refreshToken
    ) {
        public Inputs(String originCity, String destinationCity, String startDate, String tripType, int adultCount, int infantCount) {
            this(originCity, destinationCity, startDate, tripType, adultCount, infantCount, null, null);
        }

        public Inputs(String originCity, String destinationCity, String startDate, String tripType, int adultCount, int infantCount, String authorization) {
            this(originCity, destinationCity, startDate, tripType, adultCount, infantCount, authorization, null);
        }
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/train/TrainSearch_queryParams.properties");
        queryParams.put("orig",inputs.originCity);
        queryParams.put("dest",inputs.destinationCity);
        queryParams.put("ddate",inputs.startDate);
        queryParams.put("ttype", mapTripType(inputs.tripType));
        queryParams.put("acount",inputs.adultCount);
        queryParams.put("icount",inputs.infantCount);

        ApiRequest.Builder requestBuilder = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-train-search-v2/v8/train/journeys"))
                .setQueryParams(queryParams);

        String deviceId = CoreFunctions.generateDeviceId();
        Map<String, Object> headers = new HashMap<>();

        if (inputs.authorization != null && !inputs.authorization.isBlank()) {
            headers.put("Authorization", inputs.authorization);
            headers.put("User-Agent", "tiketcom");
            headers.put("Accept", "*/*");

            String token = inputs.authorization.replace("Bearer ", "");
            StringBuilder cookie = new StringBuilder("device_id=" + deviceId + "; session_access_token=" + token + ";");
            if (inputs.refreshToken != null && !inputs.refreshToken.isBlank()) {
                cookie.append(" session_refresh_token=").append(inputs.refreshToken).append(";");
            }
            headers.put("Cookie", cookie.toString());
        } else {
            headers.put("Cookie", "device_id=" + deviceId + ";");
        }
        headers.put("x-cookie-session-v2", "true");

        requestBuilder.setHeaderParams(headers);

        return apiTools.get(requestBuilder.build());
    }

    /**
     * Normalizes the trip type to the value the search API expects. Only one-way is currently
     * supported/verified ("oneway"/"one_way"/"one-way" -> "ONE_WAY"); any other value is passed
     * through unchanged.
     */
    private static String mapTripType(String tripType) {
        if (tripType == null) {
            return null;
        }
        return switch (tripType.trim().toLowerCase()) {
            case "oneway", "one_way", "one-way" -> "ONE_WAY";
            default -> tripType;
        };
    }

}
