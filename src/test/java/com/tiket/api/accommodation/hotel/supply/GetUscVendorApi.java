package com.tiket.api.accommodation.hotel.supply;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Map;

/** GET /usc/v1/vendors/{vendorCode} — a missing/deleted vendor answers HTTP 400 with code DATA_NOT_EXIST. */
public record GetUscVendorApi(Inputs inputs) implements BaseApi {

    public record Inputs(String vendorCode, String username) {
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uri())
                .setHeaderParams(headers())
                .build();

        return apiTools.get(request);
    }

    /**
     * Same call without ApiTools' status validation, which throws on 4xx and drops the body.
     * Needed to read the DATA_NOT_EXIST body that comes back for a deleted vendor.
     */
    public Response hitApiRaw() {
        String uri = uri();
        BaseTest.mainLogger.get().log("Hitting GET request (raw) with, uri: " + uri);
        Response response = RestAssured.given().headers(headers()).get(uri);
        BaseTest.mainLogger.get().log("Got HTTP " + response.getStatusCode() + ": " + response.getBody().asString());
        return response;
    }

    private String uri() {
        return UscApiSupport.uriHelper().withEndpoint(UscApiSupport.BASE_PATH + "/vendors/" + inputs.vendorCode());
    }

    private Map<String, Object> headers() {
        Map<String, Object> headers = getStaticMap(UscApiSupport.HEADERS_PATH);
        headers.put("x-username", inputs.username());
        return headers;
    }
}
