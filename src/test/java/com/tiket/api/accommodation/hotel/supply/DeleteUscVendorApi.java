package com.tiket.api.accommodation.hotel.supply;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Map;

/**
 * DELETE /usc/v1/vendors/{vendorCode} — soft-delete a vendor. Answers HTTP 400 BAD_REQUEST
 * "Cannot delete vendor: config values still reference it" while config values are still linked.
 */
public record DeleteUscVendorApi(Inputs inputs) implements BaseApi {

    public record Inputs(String vendorCode, String username) {
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uri())
                .setHeaderParams(headers())
                .build();

        return apiTools.delete(request);
    }

    /**
     * Same call without ApiTools' status validation, which throws on 4xx and drops the body.
     * Needed to read the BAD_REQUEST body and decide whether to retry.
     */
    public Response hitApiRaw() {
        String uri = uri();
        BaseTest.mainLogger.get().log("Hitting DELETE request (raw) with, uri: " + uri);
        Response response = RestAssured.given().headers(headers()).delete(uri);
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
