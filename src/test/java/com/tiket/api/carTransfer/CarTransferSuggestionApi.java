package com.tiket.api.carTransfer;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.Objects;

public record CarTransferSuggestionApi(Inputs inputs) implements BaseApi {
    /**
     * Inputs for the CarTransferSuggestionApi.
     *
     * @param accessToken       The access token for authentication.
     * @param locationType      The type of location (e.g.,AIRPORT, LOCATION, POPULARDESTINATION).
     * @param type              The type of transfer (e.g., OUT, IN).
     * @param keyword           The keyword for searching locations.
     * @param pos               The position coordinates (-latitude%2Clongitude).
     * @param contextAirport    The context airport for suggestions (DPS, etc.).
     * <p>Note: If locationType is AIRPORT, pos and keyword are not required.</p>
     */
    public record Inputs(
            String accessToken,
            String locationType,
            String type,
            String keyword,
            String pos,
            String contextAirport
    ) {}
    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/carTransfer/CarTransferSuggestionApi_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("referer", uriHelper.getUri()+"/airport-transfer");

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/carTransfer/CarTransferSuggestion_queryParams.properties");
        queryParams.put("locationType", inputs.locationType);
        queryParams.put("type", inputs.type);
        if(!"AIRPORT".equals(inputs.locationType)) {
            queryParams.put("pos", inputs.pos);
            queryParams.put("keyword", inputs.keyword);
            queryParams.put("contextAirport", inputs.contextAirport);
        }

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-airport-transfer-location/v3/suggestions"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
