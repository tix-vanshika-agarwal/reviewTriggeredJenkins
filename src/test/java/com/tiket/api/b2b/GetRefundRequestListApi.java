package com.tiket.api.b2b;

import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GetRefundRequestListApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String orderID,
            String accessToken,
            String source
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/GetRefundRequestList_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        // Widen the date window to [yesterday, tomorrow] in Asia/Jakarta so the query is
        // robust to the IST/Jakarta day flip and minor backend clock skew.
        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/b2b/GETRefundRequestList_queryParams.properties");
        queryParams.put("orderId", inputs.orderID);
        queryParams.put("startDate", CoreFunctions.getJakartaDateYYYY_MM_DD(-1));
        queryParams.put("endDate", CoreFunctions.getJakartaDateYYYY_MM_DD(1));
        queryParams.put("source",inputs.source);

        UriHelper helper = new UriHelper("https://corporate-gatotkaca.tiket.com","https://corporate.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/corporate-gateway/tix-corporate/corporate-dashboard/refund"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }

}
