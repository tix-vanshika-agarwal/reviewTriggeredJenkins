package com.tiket.api.accommodation.hotel.supply;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.util.Map;

/** DELETE /usc/v1/config-values/{id} — soft-delete one config value; x-store-id must match the config value's store. */
public record DeleteUscConfigValueApi(Inputs inputs) implements BaseApi {

    public record Inputs(String configValueId, String username, String storeId) {
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap(UscApiSupport.HEADERS_PATH);
        headers.put("x-username", inputs.username());
        headers.put("x-store-id", inputs.storeId());

        ApiRequest request = new ApiRequest.Builder()
                .setUri(UscApiSupport.uriHelper().withEndpoint(UscApiSupport.BASE_PATH + "/config-values/" + inputs.configValueId()))
                .setHeaderParams(headers)
                .build();

        return apiTools.delete(request);
    }
}
