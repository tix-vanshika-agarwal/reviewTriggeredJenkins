package com.tiket.api.flight.dashboard;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record FlightDecryptApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String text
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        String urlGk = "http://flight-search.flight-ns.svc.tiket";
        String urlPreprod = "https://flight-search-be-svc.preprod-flight-cluster.tiket.com";
        UriHelper uriHelper = new UriHelper(urlGk, urlPreprod);

        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/dashboard/FlightDecryptApi_headers.properties");

        ApiRequest request = new ApiRequest.Builder()
                .setUri((uriHelper.withEndpoint("tix-flight-search/base64/decode?str=" + inputs.text)))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
