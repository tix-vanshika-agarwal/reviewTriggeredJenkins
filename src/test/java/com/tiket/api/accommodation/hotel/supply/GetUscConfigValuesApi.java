package com.tiket.api.accommodation.hotel.supply;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.util.LinkedHashMap;
import java.util.Map;

/** GET /usc/v1/config-values — list config values of a vendor for one store (x-store-id: TIKETCOM / TIKETB2B). */
public record GetUscConfigValuesApi(Inputs inputs) implements BaseApi {

    public record Inputs(String vendorCode, String username, String storeId, int pageNum, int pageSize) {
        public Inputs(String vendorCode, String username, String storeId) {
            this(vendorCode, username, storeId, 1, 50);
        }
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap(UscApiSupport.HEADERS_PATH);
        headers.put("x-username", inputs.username());
        headers.put("x-store-id", inputs.storeId());

        Map<String, Object> queryParams = new LinkedHashMap<>();
        queryParams.put("page.num", inputs.pageNum());
        queryParams.put("page.size", inputs.pageSize());
        queryParams.put("orderBy", "CREATED_AT_ASC");
        queryParams.put("q.vendorCode", inputs.vendorCode());

        ApiRequest request = new ApiRequest.Builder()
                .setUri(UscApiSupport.uriHelper().withEndpoint(UscApiSupport.BASE_PATH + "/config-values"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
