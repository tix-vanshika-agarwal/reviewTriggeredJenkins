package com.tiket.api.b2b;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public record GetCorporateDashboardPurchaseOrdersApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String orderId,
            String startDate,
            String endDate
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/TravelPlans/GetCorporateDashboardOrdersApi_headers.properties");
        headers.put("cookie", "session_access_token=" + inputs.accessToken + ";");
        // Construct query parameters
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("orderId", inputs.orderId);
        queryParams.put("startDate", inputs.startDate);
        queryParams.put("endDate", inputs.endDate);
        queryParams.put("page", "0");
        queryParams.put("size", "10");
        queryParams.put("type", "pending");

        StringJoiner queryString = new StringJoiner("&", "?", "");
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            queryString.add(entry.getKey() + "=" + entry.getValue());
        }

        // Build the full URL with dynamic orderID and query parameters
        UriHelper helper = new UriHelper("https://corporate.preprod.tiket.com");
        String url = helper.withEndpoint("/corporate-gateway/tix-corporate/corporate-dashboard/corporate-orders/")
                + queryString.toString();

        // Create the API request
        ApiRequest request = new ApiRequest.Builder()
                .setUri(url)
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
