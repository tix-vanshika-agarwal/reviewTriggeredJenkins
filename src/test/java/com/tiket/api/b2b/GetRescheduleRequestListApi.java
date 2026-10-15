package com.tiket.api.b2b;

import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GetRescheduleRequestListApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String orderID,
            String accessToken

    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/AcceptOrRejectReschedule/GetRescheduleRequestListApi_headers.properties");
        headers.put("cookie", "session_access_token=" + inputs.accessToken + ";");
        headers.put("referer", CoreFunctions.getCorporateDashboardBaseUrl() + "/reschedule-list");

        // Widen the date window to [yesterday, tomorrow] in Asia/Jakarta so the query is
        // robust to the IST/Jakarta day flip and minor backend clock skew.
        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/b2b/AcceptOrRejectReschedule/GetRescheduleRequestListApi_queryParams.properties");
        queryParams.put("orderId", inputs.orderID);
        queryParams.put("startDate", CoreFunctions.getJakartaDateYYYY_MM_DD(-1));
        queryParams.put("endDate", CoreFunctions.getJakartaDateYYYY_MM_DD(1));

        UriHelper helper = new UriHelper("https://corporate-gatotkaca.tiket.com","https://corporate.preprod.tiket.com");


        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/corporate-gateway/tix-corporate/corporate-dashboard/reschedule"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
