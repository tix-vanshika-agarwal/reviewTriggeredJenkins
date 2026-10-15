package com.tiket.api.flight.dashboard;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;

import java.util.Map;

public record FlightGetBookingCodeApi(Inputs inputs) implements BaseApi {
    /** @param size how many issuance records to ask for; there is one per booking code */
    public record Inputs(
            String orderId,
            String email,
            String size
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/FlightGetBookingCode_headers.properties");
        headers.put("username",inputs.email);

        String urlGk = "http://flight-issuance.flight-ns.svc.tiket/tix-flight-issuance/issuance-status/findAll?orderId=";
        String urlPreprod = "https://flight-issuance-be-svc.preprod-flight-cluster.tiket.com/tix-flight-issuance/issuance-status/findAll?orderId=";
        String url = (Configs.ENVIRONMENT == com.tiket.model.Environment.GK) ? urlGk : urlPreprod;

        ApiRequest request = new ApiRequest.Builder()
                .setUri((url+inputs.orderId+"&page=0&size="
                        + ((inputs.size == null || inputs.size.isBlank()) ? "1" : inputs.size)))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
