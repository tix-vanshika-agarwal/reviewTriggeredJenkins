package com.tiket.api.b2b;

import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public record GetBookingDetailDashboardApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String orderID,
            String startDate,
            String endDate,
            String type
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/GetOrderDetailDashboard_headers.properties");
        headers.put("cookie", "session_access_token=" + inputs.accessToken + ";");
        headers.put("referer", CoreFunctions.getCorporateDashboardBaseUrl() + "/purchase-list");

        // Widen the date window to [yesterday, tomorrow] in Asia/Jakarta so the query is
        // robust to the IST/Jakarta day flip and minor backend clock skew.
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", "0");
        queryParams.put("size", "10");
        queryParams.put("email", "");
        queryParams.put("orderId", inputs.orderID != null ? inputs.orderID : "");
        queryParams.put("type", inputs.type);
        queryParams.put("startDate", CoreFunctions.getJakartaDateYYYY_MM_DD(-1));
        queryParams.put("endDate", CoreFunctions.getJakartaDateYYYY_MM_DD(1));
        queryParams.put("source", "system");

        queryParams.put("subProductTypes[0]", "railink");
        queryParams.put("subProductTypes[1]", "airport_transfer");
        queryParams.put("subProductTypes[2]", "flight");
        queryParams.put("subProductTypes[3]", "tixhotel");
        queryParams.put("subProductTypes[4]", "car");
        queryParams.put("subProductTypes[5]", "train");
        queryParams.put("subProductTypes[6]", "whoosh");
        queryParams.put("subProductTypes[7]", "event");
        queryParams.put("subProductTypes[8]", "bus");
        queryParams.put("subProductTypes[9]", "ferry");

        UriHelper helper = new UriHelper("https://corporate-gatotkaca.tiket.com", "https://corporate.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/corporate-gateway/tix-corporate/corporate-dashboard/corporate-orders"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
